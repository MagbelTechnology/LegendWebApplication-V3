package com.magbel.legend.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import magma.net.dao.MagmaDBConnection;
import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.GenerateList;
import com.magbel.legend.bus.ProcurementManager;
import com.magbel.legend.mail.MailSender;
import com.magbel.util.ApplicationHelper;
import magma.net.vao.ProcureRequisition;

public class ProcureNewRequisitionRepostServlet extends HttpServlet {

    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    MagmaDBConnection mgDbCon = null;
    ApplicationHelper applHelper = null;
    MailSender mailSender = null;
    ApprovalRecords aprecords = null;
    SimpleDateFormat timer = null;
    GenerateList genList = null;

    public ProcureNewRequisitionRepostServlet() {
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
        genList = new GenerateList();
        boolean done = false;
        String operation = request.getParameter("operation");
        String reqnID = request.getParameter("reqnId");
        String supervisorID = "";
        String successMessage = "";
        String errorMessage = "";
        boolean isFirstRequisition = false;
        String mtid = request.getParameter("mtid");
        boolean doneSave = false;
        String summaryStatus="";
        //String supervisorName="";
        // System.out.println(" operation >>>>>>>>> " + operation);
        // System.out.println(" mtid >>>>>>>>> " + mtid);
        // System.out.println(" reqnID >>>>>>>>> " + reqnID);



        String requisitionTitle = request.getParameter("requisitionTitle");
        String compCode = request.getParameter("comp_code");
        String userID = request.getParameter("userid");
        String reqnBranch = request.getParameter("requestBranch");
        String reqnSection = request.getParameter("ReqSection");
        String reqnDepartment = request.getParameter("requestDepartment");
        String reqnUserID = request.getParameter("requestFor");
        String remark = request.getParameter("remark").toUpperCase();
        String itemType = request.getParameter("itemType");
        String itemRequested = request.getParameter("itemRequested");
        String itemQty = request.getParameter("no_of_items");
        String reqnNumber = request.getParameter("reqnNumber");

        int approvalLevelLimit = Integer.parseInt(request.getParameter("txnLevel"));
        String adm_Approv_Lvl_Qry = "select level from Approval_Level_Setup where transaction_type='Multiple Proc. Requisition'";
        int var = Integer.parseInt(aprecords.getCodeName(adm_Approv_Lvl_Qry));
        if (approvalLevelLimit > 0) {
            supervisorID = request.getParameter("supvrbackup");
        } else {
            supervisorID = userID;
        }



        if (operation != null && operation.equalsIgnoreCase("add")) {
            //System.out.println(" here >>>>>>> reqnID " + reqnID);
            successMessage = " New Procurement Requisition Successfully Added ";
            errorMessage = " New Procurement Requisition Not Added";
            if (reqnID == null || reqnID.equalsIgnoreCase("null")) {
                //System.out.println("  here 2  >>>>>>>");
                reqnID = "PRREQN/" + applHelper.getGeneratedId("PR_REQUISITION");
                mtid = applHelper.getGeneratedId("am_asset_approval");
                // System.out.println(" mtid 2 >>>>>>>>> " + mtid);
                // System.out.println(" reqnID 2>>>>>>>>> " + reqnID);
                isFirstRequisition = true;
            } else {
                requisitionTitle = aprecords.getCodeName("select reqtitle from PR_REQUISITION_SUMMARY where reqnid='" + reqnID + "'");
               // supervisorID = aprecords.getCodeName("select supervisor from PR_REQUISITION where reqnid='" + reqnID + "'");
                summaryStatus = aprecords.getCodeName("select status from PR_REQUISITION_SUMMARY where reqnid='" + reqnID + "'");
            }

        }
        if (reqnID != null && !reqnID.equalsIgnoreCase("null") && operation != null && operation.equalsIgnoreCase("update")) {
            successMessage = " Update on Procurement Requisition Successfull";
            errorMessage = " New Procurement Requisition Not Updated";

            requisitionTitle = aprecords.getCodeName("select reqtitle from PR_REQUISITION_SUMMARY where reqnid='" + reqnID + "'");//request.getParameter("");
            supervisorID = aprecords.getCodeName("select supervisor from PR_REQUISITION where reqnid='" + reqnID + "'");//request.getParameter("");

            // String deleteFormalReqn = " delete from PR_REQUISITION  where ReqnID='" + reqnID + "'";
            // genList.updateTable(deleteFormalReqn);

            ProcureRequisition procReq = new ProcureRequisition();

            procReq.setRequestingBranch(reqnBranch);
            procReq.setRequestingDept(reqnDepartment);
            procReq.setRequestingSection(reqnSection);
            procReq.setReqnUserId(reqnUserID);
            procReq.setQuantity(Integer.parseInt(itemQty));
            procReq.setItemType(itemType);
            procReq.setItemRequested(itemRequested);
            procReq.setIpAddress(request.getRemoteAddr());
            procReq.setRemark(remark);
            procReq.setRequisitionNum(Integer.parseInt(reqnNumber));
            procReq.setReqnID(reqnID);

            try {
                ProcurementManager procManager = new ProcurementManager();
                int i = procManager.updateProcurementRequisition(procReq);
                done = (i == -1);
            } catch (Exception ex) {
                System.out.println("error occurred in ProcedureNewRequisitionRepostServlet>>> " + ex);
            }

        }


        if (reqnID != null && !reqnID.equalsIgnoreCase("null") && operation != null && operation.equalsIgnoreCase("save")) {
            String status = "";
            String processStatus = "";
            String updateReqnTeable = "";
            String updateReqnSummaryTable = "";
            String supervisorEmail = "";

            if (approvalLevelLimit > 0) {
                status = "PENDING";
                processStatus = "P";
                updateReqnTeable = " update PR_REQUISITION  set status ='PENDING' where ReqnID='" + reqnID + "'";
                updateReqnSummaryTable = " update PR_REQUISITION_SUMMARY  set status ='PENDING' ,Create_Date='" + mgDbCon.getDateTime(new java.util.Date()) + "',workStationIP='" + request.getRemoteAddr() + "',Create_Time='" + timer.format(new java.util.Date()) + "' where ReqnID='" + reqnID + "'";

                String supervisorName = "";
                String supervisorId = aprecords.getCodeName("select supervisor from pr_requisition where reqnid='" + reqnID + "'");
                String supervisorNameQry = "select Full_name,email,approval_limit from am_gb_user where user_ID ='" + supervisorId + "'";
                String[] sprvResult = (aprecords.retrieveArray(supervisorNameQry)).split(":");
                supervisorName = sprvResult[0];
                supervisorEmail = sprvResult[1];

                successMessage = " Procurement Requisition Successfully sent to " + supervisorName + "  for Approval.";
            } else {
                status = "APPROVED FOR QUOTATION";
                processStatus = "X";
                successMessage = " Procurement Requisition Sent for Quotation Preparation";
                updateReqnTeable = " update PR_REQUISITION  set status ='ACTIVE' where ReqnID='" + reqnID + "'";
                updateReqnSummaryTable = " update PR_REQUISITION_SUMMARY  set status ='ACTIVE',Create_Date='" + mgDbCon.getDateTime(new java.util.Date()) + "',workStationIP='" + request.getRemoteAddr() + "',Create_Time='" + timer.format(new java.util.Date()) + "' where ReqnID='" + reqnID + "'";
            }
            errorMessage = " Procurement Requisition Not Saved";
            String branchCode_Qry = "select branch_Code from am_ad_branch where branch_id=" + request.getParameter("userBranch");
            String branchCode = aprecords.getCodeName(branchCode_Qry);
            requisitionTitle = aprecords.getCodeName("select reqtitle from PR_REQUISITION_SUMMARY where reqnid='" + reqnID + "'");
            supervisorID = aprecords.getCodeName("select supervisor from PR_REQUISITION where reqnid='" + reqnID + "'");

            try {

               // String ins_am_asset_approval_qry = "insert into am_asset_approval(asset_id,user_id,super_id,posting_date,description," +
                 //       "effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time,transaction_id,batch_id," +
                 //       "transaction_level) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                String ins_am_asset_approval_qry = "update am_asset_approval set asset_id=?,user_id=?,super_id=?,posting_date=?,description=?," +
                        "effective_date=?,branchCode=?,asset_status=?,tran_type=?, process_status=?,tran_sent_time=?," +
                        "transaction_level=? where transaction_id=?";

                con = mgDbCon.getConnection("legendPlus");

                pstmt = con.prepareStatement(ins_am_asset_approval_qry);
                pstmt.setString(1, reqnID);
                pstmt.setString(2, userID);
                pstmt.setString(3, supervisorID);
                pstmt.setTimestamp(4, mgDbCon.getDateTime(new java.util.Date()));
                pstmt.setString(5, requisitionTitle);
                pstmt.setTimestamp(6, mgDbCon.getDateTime(new java.util.Date()));
                pstmt.setString(7, branchCode);
                pstmt.setString(8, status); //asset_status
                pstmt.setString(9, "Multiple Proc. Requisition");
                pstmt.setString(10, processStatus);
                pstmt.setString(11, timer.format(new java.util.Date()));
                pstmt.setInt(12, var);
                pstmt.setString(13, mtid);
                doneSave = (pstmt.executeUpdate() == -1);
                if (!doneSave) {

                    // to send update pr_requistion and pr_requisition_summary tables after completing procure requistion
                    genList.updateTable(updateReqnTeable);
                    genList.updateTable(updateReqnSummaryTable);

                    //to send email to supervisor
                    if (approvalLevelLimit > 0) {
                        String realPath = getServletConfig().getServletContext().getRealPath("/mailConfig/legend.properties");
                        FileInputStream fis = new FileInputStream(realPath);
                        if ((supervisorEmail != null) && !(supervisorEmail.equalsIgnoreCase(""))) {
                            mailSender.sendMailToSupervisor(fis, supervisorEmail, reqnID);
                        }
                    }

                }
            } catch (Exception e) {
                System.out.println("Error occurred when save repost procurement requisition :" + e);
            }

        }



        //String statux = "PIP";
        String statux = "Proc. In Progress";
        String reqnStatus = "";


        String branchCode_Qry = "select branch_Code from am_ad_branch where branch_id=" + reqnBranch;

        String branchCode = aprecords.getCodeName(branchCode_Qry);




        if (operation != null && (operation.equalsIgnoreCase("add"))) {


            String ReqnInsertQry = "insert into PR_REQUISITION (ReqnID,UserID,ReqnBranch,ReqnSection,ReqnDepartment," +
                    " ReqnUserID,ItemType,ItemRequested,Status,ApprovalLevel,ApprovalLevelLimit,Supervisor,company_code,Image," +
                    "Remark,workStationIP,Quantity,trans_id,ReqnDate) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

            String ReqnInsertSummaryQry = "insert into PR_REQUISITION_SUMMARY (Company_Code,ReqnID," +
                    "ReqTitle,UserID,Create_Date,Status,workStationIP,Create_Time,trans_id,supervisor) " +
                    "values (?,?,?,?,?,?,?,?,?,?) ";


            boolean doneSummary = false;
            // System.out.println("the value of mtid is >>>>>>>> " + mtid);
            try {
                //this is to ensure that subsquent requisition has same status as existing ones
                if(!isFirstRequisition){statux=summaryStatus;}

                con = mgDbCon.getConnection("legendPlus");
                pstmt = con.prepareStatement(ReqnInsertQry);


                pstmt.setString(1, reqnID);
                pstmt.setString(2, userID);
                pstmt.setString(3, reqnBranch);
                pstmt.setString(4, reqnSection);
                pstmt.setString(5, reqnDepartment);
                pstmt.setString(6, reqnUserID);
                pstmt.setString(7, itemType);
                pstmt.setString(8, itemRequested);
                pstmt.setString(9, statux);
                pstmt.setInt(10, 0);
                pstmt.setInt(11, approvalLevelLimit);
                pstmt.setString(12, supervisorID);
                pstmt.setString(13, compCode);
                pstmt.setString(14, "N");
                pstmt.setString(15, remark);
                pstmt.setString(16, request.getRemoteAddr());
                pstmt.setString(17, itemQty);
                pstmt.setInt(18, Integer.parseInt(mtid));
                pstmt.setTimestamp(19, mgDbCon.getDateTime(new java.util.Date()));
                done = (pstmt.executeUpdate() == -1);
                System.out.println("done >>>>>>>>> " + done);

                if (isFirstRequisition) {

                    pstmt = con.prepareStatement(ReqnInsertSummaryQry);
                    pstmt.setString(1, compCode);
                    pstmt.setString(2, reqnID);
                    pstmt.setString(3, requisitionTitle);
                    pstmt.setString(4, userID);
                    pstmt.setTimestamp(5, mgDbCon.getDateTime(new java.util.Date()));
                    pstmt.setString(6, statux);
                    pstmt.setString(7, request.getRemoteAddr());
                    pstmt.setString(8, timer.format(new java.util.Date()));
                    pstmt.setInt(9, Integer.parseInt(mtid));
                    pstmt.setString(10,supervisorID);
                    doneSummary = (pstmt.executeUpdate() == -1);


                }


                if (!done && !doneSummary)//successful
                {
                    if ((!done)) {
                        out.print("<script>alert('" + successMessage + "')</script>");
                        out.print("<script>window.location='DocumentHelp.jsp?np=prNewRequisitionFormRepost&" +
                                "reqnId=" + reqnID + "&CompCode=" + compCode + "&requisitionTitle=" + requisitionTitle + "&supervisorId=" + supervisorID + "&mtid=" + mtid + "&image=N'</script>");
                    } else {
                        out.print("<script>alert('" + errorMessage + "')</script>");
                        out.print("<script>window.location='DocumentHelp.jsp?np=prNewRequisitionFormRepost&" +
                                "reqnId=" + reqnID + "&CompCode=" + compCode + "&requisitionTitle=" + requisitionTitle + "&supervisorId=" + supervisorID + "&mtid=" + mtid + "&image=N'</script>");
                    }
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

        }

        if (operation != null && (operation.equalsIgnoreCase("update"))) {

            if ((!done)) {
                out.print("<script>alert('" + successMessage + "')</script>");
                out.print("<script>window.location='DocumentHelp.jsp?np=prNewRequisitionFormRepost&" +
                        "reqnId=" + reqnID + "&CompCode=" + compCode + "&requisitionTitle=" + requisitionTitle + "&supervisorId=" + supervisorID + "&mtid=" + mtid + "&image=N'</script>");
            } else {
                out.print("<script>alert('" + errorMessage + "')</script>");
                out.print("<script>window.location='DocumentHelp.jsp?np=prNewRequisitionFormRepost&" +
                        "reqnId=" + reqnID + "&CompCode=" + compCode + "&requisitionTitle=" + requisitionTitle + "&supervisorId=" + supervisorID + "&mtid=" + mtid + "&image=N'</script>");
            }


        }


            if (operation != null && (operation.equalsIgnoreCase("save"))) {

            if ((!doneSave)) {
                out.print("<script>alert('" + successMessage + "')</script>");
                out.print("<script>window.location='DocumentHelp.jsp?np=prNewRequisitionList'</script>");
            } else {
                out.print("<script>alert('" + errorMessage + "')</script>");
                out.print("<script>window.location='DocumentHelp.jsp?np=prNewRequisitionForm&" +
                        "reqnId=" + reqnID + "&CompCode=" + compCode + "&requisitionTitle=" + requisitionTitle + "&supervisorId=" + supervisorID + "&mtid=" + mtid + "&image=N'</script>");
            }


        }


    }
}
