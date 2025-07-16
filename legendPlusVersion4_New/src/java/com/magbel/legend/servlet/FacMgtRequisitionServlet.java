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

import java.util.Locale;

public class FacMgtRequisitionServlet extends HttpServlet {

    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    MagmaDBConnection mgDbCon = null;
    ApplicationHelper applHelper = null;
    MailSender mailSender = null;
    ApprovalRecords aprecords = null;
    SimpleDateFormat timer = null;

    public FacMgtRequisitionServlet() {
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
       //int workId= (Integer)request.getAttribute("facilityMgtWorkId");
      //-- int workId= (Integer)request.getSession().getAttribute("facilityMgtWorkId");

        String userClass = (String) request.getSession().getAttribute("UserClass");
        
       //-- System.out.println("the workId for this request is >>>>>>>> "+workId);
    //    String realPath = getServletConfig().getServletContext().getRealPath("/mailConfig/legend.properties");
        String realPath = "C:/Property/LegendPlus.properties";
        System.out.println("realPath >>>>>>>>>>> " + realPath);
        FileInputStream fis = new FileInputStream(realPath);

        String reqnID = "FMREQN/" + applHelper.getGeneratedId("FM_Requisition");
        System.out.println("reqnID >>>>>>>>>>> " + reqnID);
        String mtid = applHelper.getGeneratedId("am_asset_approval");

        String compCode = request.getParameter("comp_code");
        String userID = request.getParameter("userid");
        String reqnBranch = request.getParameter("requestBranch");
        String reqnSection = request.getParameter("ReqSection");
        String reqnDepartment = request.getParameter("requestDepartment");
        String reqnUserID = request.getParameter("requestFor").toUpperCase();
        String remark = request.getParameter("remark")== null?"":request.getParameter("remark").toUpperCase();
      
        if (!userClass.equals("NULL") || userClass!=null){
        	
        String supervisorID = userID;
        //new fields for facility management
        String assetId = request.getParameter("facility");
        String reqMeans = request.getParameter("reqMeans");
        String categoryOfWork = request.getParameter("categoryOfWork");
        String maintenanceNature = request.getParameter("maintenanceNature");
        String priority = request.getParameter("priority");
        int NoOfWorkDes = request.getParameter("NoOfWorkDes")== null || request.getParameter("NoOfWorkDes").equals("")?0:Integer.parseInt(request.getParameter("NoOfWorkDes"));
        String facLocation = request.getParameter("facLocation") == null?"":request.getParameter("facLocation").toUpperCase();

        String status = "A";
        String supervisorName = "";
        String supervisorNameQry = "select Full_name,email,approval_limit from am_gb_user where user_ID ='" + request.getParameter("supervisor") + "'";
        int apprvLevel = 0;
        String reqnStatus = "";
        String adm_Approv_Lvl_Qry = "select level from Approval_Level_Setup where transaction_type='Facility Mgt Requisition'";

        String branchCode_Qry = "select branch_Code from am_ad_branch where branch_id=" + reqnBranch;

        String branchCode = aprecords.getCodeName(branchCode_Qry);

        System.out.println("the value of >>>>>>>>>> is aprecords.getCodeName(adm_Approv_Lvl_Qry) "+ aprecords.getCodeName(adm_Approv_Lvl_Qry) );

        int var = Integer.parseInt(aprecords.getCodeName(adm_Approv_Lvl_Qry));
        System.out.println(">>>>>>>>>> request.getParameter(txnLevel)"+ request.getParameter("txnLevel"));

        int approvalLevelLimit = Integer.parseInt(request.getParameter("txnLevel"));

        if (approvalLevelLimit > 0) {
            status = "P";
            reqnStatus = "PENDING";
            supervisorID = request.getParameter("supervisor");
            String[] sprvResult = (aprecords.retrieveArray(supervisorNameQry)).split(":");
            supervisorName = sprvResult[0];
            if ((sprvResult[1] != null) && !(sprvResult[1].equals(""))) {
                mailSender.sendMailToSupervisor(fis, sprvResult[1], reqnID);
            }
        } else {
            //send a mail to all members of the department
            //supervisor = userid
            status = "FX";
           // status = "A";
            reqnStatus = "APPROVED FOR FACILITY MGT DEPT.";
            approvalLevelLimit = var;
            supervisorID = userID;
            //work on mail to be sent to admin department
           
            mailSender.sendMailToFacAcceptanceDept(fis, reqnID, compCode);
           
        }

        String ReqnInsertQry_old = "insert into FM_Requisition (ReqnID,UserID,ReqnBranch,ReqnSection,ReqnDepartment," +
                " ReqnUserID,Status,ApprovalLevel,ApprovalLevelLimit,Supervisor,Image," +
                "Remark,workStationIP,Asset_id,req_Means,category_Work,Maintenance_Nature,Priority,No_Work_Description,"+
                " Location,Request_Time,work_id) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

         String ReqnInsertQry = "insert into FM_Requisition (ReqnID,UserID,ReqnBranch,ReqnSection,ReqnDepartment," +
                " ReqnUserID,Status,ApprovalLevel,ApprovalLevelLimit,Supervisor,Image," +
                "Remark,workStationIP,Asset_id,req_Means,category_Work,Maintenance_Nature,Priority,No_Work_Description,"+
                " Location,Request_Time,reqnDate) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        boolean done = false;
        boolean result = false; 
        try {
            con = mgDbCon.getConnection("legendPlus");
            pstmt = con.prepareStatement(ReqnInsertQry);
            pstmt.setString(1, reqnID);
            pstmt.setString(2, userID);
            pstmt.setString(3, reqnBranch);
            pstmt.setString(4, reqnSection);
            pstmt.setString(5, reqnDepartment);
            pstmt.setString(6, reqnUserID);
            //pstmt.setString(7, itemType);
            //pstmt.setString(8, itemRequested);
            pstmt.setString(7, status);
            pstmt.setInt(8, 0);
            pstmt.setInt(9, approvalLevelLimit);
            pstmt.setString(10, supervisorID);
           // pstmt.setString(13, compCode);
            pstmt.setString(11, "N");
            pstmt.setString(12, remark);
            pstmt.setString(13, request.getRemoteAddr());
           // pstmt.setString(17, itemQty);
            pstmt.setString(14, assetId);
            pstmt.setString(15, reqMeans);
            pstmt.setString(16, categoryOfWork);
            pstmt.setString(17, maintenanceNature);
            pstmt.setString(18, priority);
            pstmt.setInt(19, NoOfWorkDes);
            pstmt.setString(20, facLocation);
            pstmt.setString(21, timer.format(new java.util.Date()));
            pstmt.setTimestamp(22, mgDbCon.getDateTime(new java.util.Date()));
            //--pstmt.setInt(22, workId);
            done = (pstmt.executeUpdate() == -1);
            System.out.println("done >>>>>>>>> " + done);

            if (!done)//successful
            {
                

                String ins_am_asset_approval_qry = "insert into am_asset_approval(asset_id,user_id,super_id,posting_date,description," +
                        "effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time,transaction_id,batch_id," +
                        "transaction_level) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                pstmt = con.prepareStatement(ins_am_asset_approval_qry);
                pstmt.setString(1, reqnID);
                pstmt.setString(2, userID);
                pstmt.setString(3, supervisorID);
                pstmt.setTimestamp(4, mgDbCon.getDateTime(new java.util.Date()));
                pstmt.setString(5, remark);
                pstmt.setTimestamp(6, mgDbCon.getDateTime(new java.util.Date()));
                pstmt.setString(7, branchCode);
                pstmt.setString(8, reqnStatus); //asset_status
                pstmt.setString(9, "Facility Mgt Requisition");
                pstmt.setString(10, status);
                pstmt.setString(11, timer.format(new java.util.Date()));
                pstmt.setString(12, mtid);
                pstmt.setString(13, mtid);
                pstmt.setInt(14, var);
                result = (pstmt.executeUpdate() == -1);
            }

            if ((!result) && (status.equalsIgnoreCase("P"))) {
                
                out.print("<script>alert('Facility Management Requisition successfully sent to " + supervisorName + "  for Approval.')</script>");
                out.print("<script>window.location='DocumentHelp.jsp?np=facilityRequisitionFormUpdate&" +
                        "ReqnID=" + reqnID + "&CompCode=" + compCode + "&image=N'</script>");
                        //--"ReqnID=" + reqnID + "&CompCode=" + compCode + "&image=N&workId="+workId+"'</script>");
            } else if ((!result) && (status.equalsIgnoreCase("FX"))) {
                System.out.println(">>>>>>> here 3");
                out.print("<script>alert('Facility Management Requisition successfully sent to Facility Management Dept  for Approval.')</script>");
                out.print("<script>window.location='DocumentHelp.jsp?np=facilityRequisitionFormUpdate&" +
                        "ReqnID=" + reqnID + "&CompCode=" + compCode + "&image=N'</script>");
            } else {
                
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
