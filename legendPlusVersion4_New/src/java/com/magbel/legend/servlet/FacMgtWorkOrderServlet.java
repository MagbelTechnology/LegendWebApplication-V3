package com.magbel.legend.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import magma.net.dao.MagmaDBConnection;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.mail.MailSender;
import com.magbel.util.ApplicationHelper;

public class FacMgtWorkOrderServlet extends HttpServlet {

    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    MagmaDBConnection mgDbCon = null;
    ApplicationHelper applHelper = null;
    MailSender mailSender = null;
    ApprovalRecords aprecords = null;
    SimpleDateFormat timer = null;
     SimpleDateFormat sdf = null;
    public FacMgtWorkOrderServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        processRequisition(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        processRequisition(request, response);
    }

    private void processRequisition(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        mgDbCon = new MagmaDBConnection();
        applHelper = new ApplicationHelper();
        PrintWriter out = response.getWriter();
        mailSender = new MailSender();
        aprecords = new ApprovalRecords();
        timer = new SimpleDateFormat("kk:mm:ss");
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = sdf.format(new java.util.Date());
      
        String userClass = (String) request.getSession().getAttribute("UserClass");

         String assetId = request.getParameter("assetId");
         String workOrderCode = request.getParameter("workOrderCode");
         //System.out.println("The value of work order code in >>>>>>> "+workOrderCode);
         String vendorCode = request.getParameter("vendorCode");
         String contactTitle= request.getParameter("contractTitle")== null?"":request.getParameter("contractTitle").toUpperCase();
         double  contactValue =request.getParameter("contactValue")== null ||request.getParameter("contactValue").equals("")?0.00:Double.parseDouble(request.getParameter("contactValue").replaceAll(",", "")) ;
         int contactDuration = request.getParameter("contactDuration")== null ||request.getParameter("contactDuration").equals("")?0:Integer.parseInt(request.getParameter("contactDuration")) ;
         int workId = request.getParameter("workId")== null ||request.getParameter("workId").equals("")?0:Integer.parseInt(request.getParameter("workId")) ;
         int tranId = request.getParameter("tranId")== null ||request.getParameter("tranId").equals("")?0:Integer.parseInt(request.getParameter("tranId")) ;
         String paymentTermCode = request.getParameter("paymentTermCode");
         String signatureCode1 = request.getParameter("signatureCode1");
         String signatureCode2 = request.getParameter("signatureCode2");
         String reqnId = request.getParameter("reqnID");

         if (!userClass.equals("NULL") || userClass!=null){
         String subjectToVat = request.getParameter("subjectToVat");
         double  vatAmount=0.00;
         double vatRate =0.00;
         int userid = request.getParameter("userid")== null ||request.getParameter("userid").equals("")?0:Integer.parseInt(request.getParameter("userid")) ;

        String facilityInsertQry = "insert into FM_WORK_ORDER (WORK_ORDER_CODE,ASSET_ID,Vendor_Code,Contract_Title,Contract_value,Contract_duration," +
                " Payment_Type_code,work_id,Signature_code1,Signature_code2,prepared_by,tran_Id," +
                "prepare_date,prepare_time,workStationIP,status,Contract_Balance,ReqnID,Job_Completion,Subject_To_Vat,Vat_amount,total_amount) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        if(subjectToVat == null){
        subjectToVat ="N";

        }else

        {   String vatRateString  = aprecords.getCodeName("select vat_rate from am_gb_company ");
            if(vatRateString  == null || vatRateString.equals("")) vatRateString ="1";
            vatRate = Double.parseDouble(vatRateString);
            vatAmount =(vatRate/100.00) * contactValue;
                 subjectToVat ="Y";
        }



        boolean done = false;
       
        try {
            con = mgDbCon.getConnection("legendPlus");
            pstmt = con.prepareStatement(facilityInsertQry);
            pstmt.setString(1, workOrderCode);
            pstmt.setString(2, assetId);
            pstmt.setString(3, vendorCode);
            pstmt.setString(4, contactTitle);
            pstmt.setDouble(5, contactValue);
            pstmt.setInt(6, contactDuration);
            pstmt.setString(7, paymentTermCode);
            pstmt.setInt(8, workId);
            pstmt.setString(9, signatureCode1);
            pstmt.setString(10, signatureCode2);
            pstmt.setInt(11, userid);
            pstmt.setInt(12, tranId);
            pstmt.setTimestamp(13,  mgDbCon.getDateTime(new java.util.Date()));
            pstmt.setString(14, timer.format(new java.util.Date()));
            pstmt.setString(15, request.getRemoteAddr());
            pstmt.setString(16,"WA");
            pstmt.setDouble(17,contactValue);//this is to set same value for contract value and contract balance
            pstmt.setString(18,reqnId);
            pstmt.setString(19,"N");
            pstmt.setString(20,subjectToVat);
            pstmt.setDouble(21,vatAmount);
            pstmt.setDouble(22,vatAmount+ contactValue);
            done = (pstmt.executeUpdate() == -1);
            System.out.println("done >>>>>>>>> " + done);

            if (!done) {
                aprecords.updateUtil("update FM_MAITENANCE_DUE set PROCESS_WORK_ORDER='Y' WHERE ACCEPTANCE_CODE='" +workOrderCode+"'");
                //the update below will change the process status of both facility management requisition transaction
                //and facility management acceptance transaction with same requisition id to WA is WORK ORDER PREPARED
                //String reqnId = aprecords.getCodeName("select reqnid from FM_MAITENANCE_DUE where tran_id "+tranId);
                aprecords.updateUtil("update am_asset_approval set process_status='WA',asset_status='WORK ORDER PREPARED' where asset_id='"+ reqnId +"'");

                out.print("<script>alert('Work Order successfully created.')</script>");
                out.print("<script>window.location='DocumentHelp.jsp?np=facilityMgtWorkOrderUpdate&" +
                        "workOrderCode=" + workOrderCode + "&tranId="+tranId+"&assetId="+assetId+"'</script>");
            } else {
                out.print("<script>alert('Work Order not created.')</script>");
                out.print("<script>window.location='DocumentHelp.jsp?np=facilityMgtWorkOrderUpdate&" +
                        "workOrderCode=" + workOrderCode + "&tranId="+tranId+"&assetId="+assetId+"'</script>");
            } 


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }else {
		out.print("<script>alert('You have No Right')</script>");
	}
    }
}
