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
import com.magbel.legend.bus.GenerateList;
import com.magbel.legend.mail.MailSender;
import com.magbel.util.ApplicationHelper;

public class ProcureRequisitionServlet extends HttpServlet {

    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    MagmaDBConnection mgDbCon = null;
    ApplicationHelper applHelper = null;
    MailSender mailSender = null;
    ApprovalRecords aprecords = null;
    SimpleDateFormat timer = null;
    GenerateList genList = null;

    public ProcureRequisitionServlet() {
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

        String realPath = getServletConfig().getServletContext().getRealPath("/mailConfig/legend.properties");
        //System.out.println("realPath >>>>>>>>>>> " + realPath);
        FileInputStream fis = new FileInputStream(realPath);
        String repost = request.getParameter("repost");
        String reqnID = "";
        String mtid = "";

        if (repost != null && repost.equalsIgnoreCase("Y")) {
            reqnID = request.getParameter("reqnIdRepost");
            mtid = request.getParameter("tranIdRepost");
            String deleteFormalReqn = " delete from PR_REQUISITION  where ReqnID='" + reqnID + "'";
            String deleteFromAmAssetApproval = " delete from am_asset_approval where transaction_id='" + mtid + "'";
            genList.updateTable(deleteFormalReqn);
            genList.updateTable(deleteFromAmAssetApproval);

        } else {

            reqnID = "PRREQN/" + applHelper.getGeneratedId("PR_REQUISITION");
            mtid = applHelper.getGeneratedId("am_asset_approval");

        }



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
        // String reqnCode = request.getParameter("reqnCode");
        String supervisorID = userID;
        String statux = "";
        String status = "A";
        String supervisorName = "";
        String supervisorNameQry = "select Full_name,email,approval_limit from am_gb_user where user_ID ='" + request.getParameter("supervisor") + "'";
        int apprvLevel = 0;
        String reqnStatus = "";
        String adm_Approv_Lvl_Qry = "select level from Approval_Level_Setup where transaction_type='Procurement Requisition'";

        String branchCode_Qry = "select branch_Code from am_ad_branch where branch_id=" + reqnBranch;

        String branchCode = aprecords.getCodeName(branchCode_Qry);

        int var = Integer.parseInt(aprecords.getCodeName(adm_Approv_Lvl_Qry));


        int approvalLevelLimit = Integer.parseInt(request.getParameter("txnLevel"));

        if (approvalLevelLimit > 0) {
            status = "P";
            statux = "PENDING";
            reqnStatus = "PENDING";
            supervisorID = request.getParameter("supervisor");
            String[] sprvResult = (aprecords.retrieveArray(supervisorNameQry)).split(":");
            supervisorName = sprvResult[0];
            if ((sprvResult[1] != null) && (sprvResult[1] != "")) {
                mailSender.sendMailToSupervisor(fis, sprvResult[1], reqnID);
            }
        } else {
            //send a mail to all members of the department
            //supervisor = userid
            status = "X";
            statux = "ACTIVE";
            reqnStatus = "APPROVED FOR QUOTATION";
            approvalLevelLimit = var;
            supervisorID = userID;
            //work on mail to be sent to admin department
            //mailSender.sendMailToAdmin(fis,reqnID,compCode);

        }

        String ReqnInsertQry = "insert into PR_REQUISITION (ReqnID,UserID,ReqnBranch,ReqnSection,ReqnDepartment," +
                " ReqnUserID,ItemType,ItemRequested,Status,ApprovalLevel,ApprovalLevelLimit,Supervisor,company_code,Image," +
                "Remark,workStationIP,Quantity,trans_id,ReqnDate) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

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
                pstmt.setString(9, "Procurement Requisition");
                pstmt.setString(10, status);
                pstmt.setString(11, timer.format(new java.util.Date()));
                pstmt.setString(12, mtid);
                pstmt.setString(13, mtid);
                pstmt.setInt(14, var);
                result = (pstmt.executeUpdate() == -1);
            }

            if ((!result) && (status.equalsIgnoreCase("P"))) {
                out.print("<script>alert('Procurement Requisition Successfully sent to " + supervisorName + "  for Approval.')</script>");
                out.print("<script>window.location='DocumentHelp.jsp?np=prRequisitionFormUpdate&" +
                        "reqnId=" + reqnID + "&CompCode=" + compCode + "&repost="+repost+"&image=N'</script>");
            } else if ((!result) && (status.equalsIgnoreCase("A"))) {
                out.print("<script>alert('Procurement Requisition Successfully sent to Admin Department  for Approval.')</script>");
                out.print("<script>window.location='DocumentHelp.jsp?np=prRequisitionFormUpdate&" +
                        "reqnId=" + reqnID + "&CompCode=" + compCode +"&repost="+repost+ "&image=N'</script>");
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
    }
}
