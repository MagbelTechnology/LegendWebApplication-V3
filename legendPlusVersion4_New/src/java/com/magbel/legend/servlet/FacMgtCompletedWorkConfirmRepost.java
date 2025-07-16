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
import com.magbel.legend.bus.FacilityManager;
import com.magbel.legend.mail.MailSender;
import com.magbel.util.ApplicationHelper;

import java.util.ArrayList;

import magma.net.vao.WorkDescription;

import com.magbel.legend.bus.GenerateList;

public class FacMgtCompletedWorkConfirmRepost extends HttpServlet {

    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    MagmaDBConnection mgDbCon = null;
    ApplicationHelper applHelper = null;
    MailSender mailSender = null;
    ApprovalRecords aprecords = null;
    SimpleDateFormat timer = null;
    SimpleDateFormat sdf = null;
    GenerateList genList = null;

    public FacMgtCompletedWorkConfirmRepost() {
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
        genList = new GenerateList();

        String userClass = (String) request.getSession().getAttribute("UserClass");
        
        String message = "";
        int updateStatus = 0;
        String statuxApproval = "";
        String realPath = getServletConfig().getServletContext().getRealPath("/mailConfig/legend.properties");

        FileInputStream fis = new FileInputStream(realPath);
        int[] outcome  = new int[3];
        
        if (!userClass.equals("NULL") || userClass!=null){
        	
        String assetId = request.getParameter("assetId");
        String tranId = request.getParameter("tranId");
        String workOrderCode = request.getParameter("workOrderCode1");
        double contractValue = request.getParameter("contractValue1") ==null?0.00: Double.parseDouble(request.getParameter("contractValue1").replaceAll(",",""));
        String assetDesc = request.getParameter("assetDesc");
        String reqnID = request.getParameter("reqnId");
        int recepientId = request.getParameter("recepientId")==null?0:Integer.parseInt(request.getParameter("recepientId"));

          //String categoryCode = aprecords.getCodeName("select CATEGORY_CODE from am_asset where asset_id ='" + assetId + "'");

           // String categoryShortName = aprecords.getCodeName("select category_acronym from am_ad_category where category_code = '" + categoryCode + "'");

            //String acceptanceID = applHelper.getGeneratedId("FM_MAITENANCE_DUE") + categoryShortName + currentDate.replaceAll("-", "");

             String deleteFromCompletedWork = " delete from FM_COMPLETED_WORK_ACCEPTANCE where WORK_ORDER_CODE='"+workOrderCode+"'";
            String deleteFromAmAssetApproval = " delete from am_asset_approval where transaction_id='"+tranId+"'";


           //-- String mtid = applHelper.getGeneratedId("am_asset_approval");


            String userID = request.getParameter("userid");
            // String reqnBranch = request.getParameter("requestBranch");
            // String reqnSection = request.getParameter("ReqSection");
           // String image = aprecords.getCodeName("select image from FM_Requisition where reqnid = '" + reqnID + "'");
          //--  String requistionInitiatorEmail = aprecords.getCodeName("select email from am_gb_user where user_id in (select userId from FM_Requisition where reqnid='" + reqnID + "' )");
            String requistionInitiatorEmail = aprecords.getCodeName("select email from am_gb_user where user_id ="+recepientId);

            String acceptanceRemark = request.getParameter("acceptanceRemark") == null ? "" : request.getParameter("acceptanceRemark").toUpperCase();

            String supervisorID = userID;
            String status = "A";
            String supervisorName = "";
            String supervisorNameQry = "select Full_name,email,approval_limit from am_gb_user where user_ID ='" + request.getParameter("supervisor") + "'";
            String reqnStatus = "";
            String adm_Approv_Lvl_Qry = "select level from Approval_Level_Setup where transaction_type='Facility Mgt Work Acceptance'";

            //String branchCode_Qry = "select branch_Code from am_ad_branch where branch_id=" + reqnBranch;

            // String branchCode = aprecords.getCodeName(branchCode_Qry);

           int var = Integer.parseInt(aprecords.getCodeName(adm_Approv_Lvl_Qry));
           int approvalLevelLimit = Integer.parseInt(request.getParameter("txnLevel"));

             genList.updateTable(deleteFromCompletedWork);
             genList.updateTable(deleteFromAmAssetApproval);

            if (approvalLevelLimit > 0) {


                outcome[1]=1;
                message = "The work done by vendor on facility with " + workOrderCode + " is waiting for your confirmation and approval.";
                status = "WCIB";
                reqnStatus = "WORK DONE PENDING AT REQ INITIATOR BRANCH";
                statuxApproval = "WCIB";
                supervisorID = request.getParameter("supervisor");
                String[] sprvResult = (aprecords.retrieveArray(supervisorNameQry)).split(":");
                supervisorName = sprvResult[0];
                if ((sprvResult[1] != null) && !(sprvResult[1].equals(""))) {
                     String updateWorkOrderQuery=" update FM_COMPLETED_WORK set status ='WCIB' where ReqnID='"+ reqnID+"'";
            genList.updateTable(updateWorkOrderQuery);
                    mailSender.sendMailToAUser(fis, sprvResult[1], "Work Completion Confirmation Approval", message);
                }
            } else {

                status = "WCA";
                reqnStatus = "WORK DONE ACCEPTED BY REQN INITIATOR";
                statuxApproval = "WCA";
                approvalLevelLimit = var;
                supervisorID = userID;
            }


            String facilityInsertQry = "insert into FM_COMPLETED_WORK_ACCEPTANCE (WORK_ORDER_CODE,ReqnID,ACCEPTANCE_REMARK,RECEIVE_BY," +
                    " RECEIVE_DATE,RECEIVE_TIME,ApprovalLevel,ApprovalLevelLimit,Supervisor,Status," +
                    "workStationIP,tran_id,work_completion_recepient) values (?,?,?,?,?,?,?,?,?,?,?,?,?) ";


            boolean done = false;
            boolean result = false;
            try {
                if (outcome[1] != -1) {
                    con = mgDbCon.getConnection("legendPlus");
                    pstmt = con.prepareStatement(facilityInsertQry);
                    pstmt.setString(1, workOrderCode);
                    pstmt.setString(2, reqnID);
                    pstmt.setString(3, acceptanceRemark);
                    pstmt.setInt(4, Integer.parseInt(userID));
                    pstmt.setTimestamp(5, mgDbCon.getDateTime(new java.util.Date()));
                    pstmt.setString(6, timer.format(new java.util.Date()));
                    pstmt.setInt(7, 0);
                    pstmt.setInt(8, approvalLevelLimit);
                    pstmt.setInt(9, Integer.parseInt(supervisorID));
                    pstmt.setString(10, status);
                    pstmt.setString(11, request.getRemoteAddr());
                    pstmt.setInt(12, Integer.parseInt(tranId));
                    pstmt.setInt(13,recepientId);

                    done = (pstmt.executeUpdate() == -1);
                    System.out.println("done >>>>>>>>> " + done);

                    if (!done)//successful
                    {                        String ins_am_asset_approval_qry = "insert into am_asset_approval(asset_id,user_id,super_id,posting_date,description," +
                                "effective_date,asset_status,tran_type, process_status,tran_sent_time,transaction_id,batch_id," +
                                "transaction_level,amount) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                        pstmt = con.prepareStatement(ins_am_asset_approval_qry);
                        pstmt.setString(1, reqnID);
                        pstmt.setString(2, userID);
                        pstmt.setString(3, supervisorID);
                        pstmt.setTimestamp(4, mgDbCon.getDateTime(new java.util.Date()));
                        pstmt.setString(5, assetDesc);
                        pstmt.setTimestamp(6, mgDbCon.getDateTime(new java.util.Date()));
                        pstmt.setString(7, reqnStatus); //asset_status
                        pstmt.setString(8, "Facility Mgt Work Acceptance");
                        pstmt.setString(9, status);
                        pstmt.setString(10, timer.format(new java.util.Date()));
                        pstmt.setString(11, tranId);
                        pstmt.setString(12, tranId);
                        pstmt.setInt(13, var);
                        pstmt.setDouble(14,contractValue);
                        
                        result = (pstmt.executeUpdate() == -1);
                    }

                  //-- updateStatus = aprecords.updateUtil("update am_asset_approval set process_status ='" + statuxApproval + "' where asset_id='" + reqnID + "' and tran_type='Facility Mgt Requisition'");

                    //--if ((!result) && (status.equalsIgnoreCase("WCIB")) && (updateStatus != -1)) {
                    if ((!result) && (status.equalsIgnoreCase("WCIB"))) {
                        out.print("<script>alert('Facility Work Completion Confirmation successfully sent to " + supervisorName + "  for Approval.')</script>");
                        out.print("<script>window.location='DocumentHelp.jsp?np=facilityMgtWorkCompletionConfirmationRepostUpdate&" +
                    "workOrderCode=" + workOrderCode + "&assetId="+assetId+"&tranId=" + tranId + "'</script>");
                    } else if ((!result) && (status.equalsIgnoreCase("WCA")) && (updateStatus != -1)) {
                       String workCompletionUpdateQuery = "update fm_work_order set job_completion='Y' where work_order_code='"+workOrderCode+"'";

                String upd_AM_Asset_Apprv_Qry_Final_Approved=
"update am_asset_Approval set process_status='WCA' , asset_status='WORK DONE ACCEPTED BY REQN INITIATOR' " +
                        "where asset_id ='"+reqnID+"' and tran_type='Facility Mgt Work Completion' " ;

                        genList.updateTable(workCompletionUpdateQuery);
                        genList.updateTable(upd_AM_Asset_Apprv_Qry_Final_Approved);
                        
                        message = "Work done by vendor on Requisition with Requistion ID: " + reqnID + " has been accepted by requistion initiator.";
                        mailSender.sendMailToAUser(fis, requistionInitiatorEmail, "Work Completion Acceptance", message);

                        System.out.println(">>>>>>> here 3");
                        out.print("<script>alert('Work Completion Acceptance Notice has been sent to Fac Mgt Dept.')</script>");
                        out.print("<script>window.location='DocumentHelp.jsp?np=facilityMgtWorkCompletionConfirmationRepostUpdate&" +
                    "workOrderCode=" + workOrderCode + "&assetId="+assetId+"&tranId=" + tranId + "'</script>");
                    } else {
                    }

                }//if for creating work descriptions
                             
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

