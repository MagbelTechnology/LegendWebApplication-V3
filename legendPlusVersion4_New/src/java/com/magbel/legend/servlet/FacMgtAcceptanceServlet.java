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

public class FacMgtAcceptanceServlet extends HttpServlet {

    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    MagmaDBConnection mgDbCon = null;
    ApplicationHelper applHelper = null;
    MailSender mailSender = null;
    ApprovalRecords aprecords = null;
    SimpleDateFormat timer = null;
    SimpleDateFormat sdf = null;

    public FacMgtAcceptanceServlet() {
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
        String message = "";
        int updateStatus =0;
        String statuxApproval ="";
        String realPath = getServletConfig().getServletContext().getRealPath("/mailConfig/legend.properties");

        FileInputStream fis = new FileInputStream(realPath);

        String userClass = (String) request.getSession().getAttribute("UserClass");
        
        String assetId = request.getParameter("assetID");
        String tranId = request.getParameter("tranId");
        String requestBranch = request.getParameter("requestBranch");
        String requestDepartment = request.getParameter("requestDepartment");
        String reqMeans = request.getParameter("reqMeans");
        String isWordDescriptionCreated = request.getParameter("isWordDescriptionCreated");
        
        int work_Id= Integer.parseInt(request.getParameter("facilityMgtWorkId"));
         String reqnID = request.getParameter("ReqnID");
   //     String isWordDescriptionCreated = (String)request.getSession().getAttribute("isWordDescriptionCreated");
        System.out.println("<<<<<<=====isWordDescriptionCreated: "+isWordDescriptionCreated);
        if(isWordDescriptionCreated == null){
            isWordDescriptionCreated="";
        }

        if(isWordDescriptionCreated.equalsIgnoreCase("") || isWordDescriptionCreated.equalsIgnoreCase("N")){
          out.print("<script>alert('You did not create Work Description before creating Fac Mgt. ')</script>");
                    out.print("<script>window.location='DocumentHelp.jsp?np=facilityMgtAcceptance&" +
                            "ReqnID=" + reqnID + "&tranId="+tranId+"'</script>");
        }

        else

        {
        	 if (!userClass.equals("NULL") || userClass!=null){
        		 
        int NoOfWorkDes = request.getParameter("NoOfWorkDes") == null || request.getParameter("NoOfWorkDes").equals("") ? 0 : Integer.parseInt(request.getParameter("NoOfWorkDes"));

        String categoryCode = aprecords.getCodeName("select CATEGORY_CODE from am_asset where asset_id ='" + assetId + "'");

        String categoryShortName = aprecords.getCodeName("select category_acronym from am_ad_category where category_code = '" + categoryCode + "'");

        String acceptanceID = applHelper.getGeneratedId("FM_MAITENANCE_DUE") + categoryShortName + currentDate.replaceAll("-", "");

        String mtid = applHelper.getGeneratedId("am_asset_approval");

        
        String userID = request.getParameter("userid");
        // String reqnBranch = request.getParameter("requestBranch");
        // String reqnSection = request.getParameter("ReqSection");
        String image = aprecords.getCodeName("select image from FM_Requisition where reqnid = '" + reqnID + "'");
        String requistionInitiatorEmail = aprecords.getCodeName("select email from am_gb_user where user_id in (select userId from FM_Requisition where reqnid='" + reqnID + "' )");


        String acceptanceRemark = request.getParameter("acceptanceRemark") == null ? "" : request.getParameter("acceptanceRemark").toUpperCase();

        String supervisorID = userID;


        String acceptanceReceiveMeans = request.getParameter("acceptanceReceiveMeans");


        String status = "A";
        String supervisorName = "";
        String supervisorNameQry = "select Full_name,email,approval_limit from am_gb_user where user_ID ='" + request.getParameter("supervisor") + "'";
        int apprvLevel = 0;
        String reqnStatus = "";
        String adm_Approv_Lvl_Qry = "select level from Approval_Level_Setup where transaction_type='Facility Mgt Acceptance'";

        //String branchCode_Qry = "select branch_Code from am_ad_branch where branch_id=" + reqnBranch;

        // String branchCode = aprecords.getCodeName(branchCode_Qry);

        // System.out.println("the value of >>>>>>>>>> is aprecords.getCodeName(adm_Approv_Lvl_Qry) "+ aprecords.getCodeName(adm_Approv_Lvl_Qry) );

        int var = Integer.parseInt(aprecords.getCodeName(adm_Approv_Lvl_Qry));
        // System.out.println(">>>>>>>>>> request.getParameter(txnLevel)"+ request.getParameter("txnLevel"));

        int approvalLevelLimit = Integer.parseInt(request.getParameter("txnLevel"));



        if (approvalLevelLimit > 0) {
            message = "Facility Mgt Request with ID " + acceptanceID + " is waiting for your approval.";
            status = "FP";
            reqnStatus = "PENDING AT FAC MGT DEPT";
            statuxApproval="FD";
            supervisorID = request.getParameter("supervisor");
            
            String[] sprvResult = (aprecords.retrieveArray(supervisorNameQry)).split(":");
            supervisorName = sprvResult[0];
            if ((sprvResult[1] != null) && !(sprvResult[1].equals(""))) {
                mailSender.sendMailToAUser(fis, sprvResult[1], "Facility Mgt Approval", message);
            }
        } else {
            status = "WX";
            reqnStatus = "APPROVED FOR WORK ORDER";
            statuxApproval ="WX";
            approvalLevelLimit = var;
            supervisorID = userID;


        }

        String facilityInsertQry = "insert into FM_MAITENANCE_DUE (ACCEPTANCE_CODE,ReqnID,ACCEPTANCE_REMARK,ASSET_ID,RECEIVE_MEANS,RECEIVE_BY," +
                " RECEIVE_DATE,RECEIVE_TIME,ApprovalLevel,ApprovalLevelLimit,Supervisor,Status," +
                "workStationIP,PROCESS_WORK_ORDER,TRAN_ID,work_id,No_Work_Description,image) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        System.out.println("request >>>>>>>>> "+request);
//        int[] outcome = createWorkDescriptionsFromTemp(request);
        int[] outcome = createWorkDescriptionsFromTemp(work_Id,reqnID);
        

       
        boolean done = false;
        boolean result = false;
        try {
            if (outcome[1] != -1) {

                int workId = outcome[0];
                int numberOfWorkDescriptions =outcome[2];
                System.out.println("workId >>>>>>>>> " + workId+"     numberOfWorkDescriptions: "+numberOfWorkDescriptions);
                con = mgDbCon.getConnection("legendPlus");
                pstmt = con.prepareStatement(facilityInsertQry);
                pstmt.setString(1, acceptanceID);
                pstmt.setString(2, reqnID);
                pstmt.setString(3, acceptanceRemark);
                pstmt.setString(4, assetId);
                pstmt.setString(5, reqMeans);
                pstmt.setInt(6, Integer.parseInt(userID));
                pstmt.setTimestamp(7, mgDbCon.getDateTime(new java.util.Date()));
                pstmt.setString(8, timer.format(new java.util.Date()));
                pstmt.setInt(9, 0);
                pstmt.setInt(10, approvalLevelLimit);
                pstmt.setInt(11, Integer.parseInt(supervisorID));
                pstmt.setString(12, status);
                pstmt.setString(13, request.getRemoteAddr());
                pstmt.setString(14, "N");
                pstmt.setInt(15, Integer.parseInt(mtid));
                pstmt.setInt(16, workId);
                pstmt.setInt(17,numberOfWorkDescriptions );
                pstmt.setString(18,image);
//                pstmt.setString(19,reqMeans);
                done = (pstmt.executeUpdate() == -1);
                System.out.println("done >>>>>>>>> " + done);

                if (!done)//successful 
                {
                    //to delete the work descriptions in FM_WORK_DESCRIPTION_TEMP
                GenerateList genList = new GenerateList();
                String deleteWorks_Query = "delete from FM_WORK_DESCRIPTION_TEMP where work_id="+workId;
                 genList.updateTable(deleteWorks_Query);

                    String ins_am_asset_approval_qry = "insert into am_asset_approval(asset_id,user_id,super_id,posting_date,description," +
                            "effective_date,asset_status,tran_type, process_status,tran_sent_time,transaction_id,batch_id," +
                            "transaction_level,BranchCode) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                    pstmt = con.prepareStatement(ins_am_asset_approval_qry);
                    pstmt.setString(1, reqnID);
                    pstmt.setString(2, userID);
                    pstmt.setString(3, supervisorID);
                    pstmt.setTimestamp(4, mgDbCon.getDateTime(new java.util.Date()));
                    pstmt.setString(5, acceptanceRemark);
                    pstmt.setTimestamp(6, mgDbCon.getDateTime(new java.util.Date()));
                    pstmt.setString(7, reqnStatus); //asset_status
                    pstmt.setString(8, "Facility Mgt Acceptance");
                    pstmt.setString(9, status);
                    pstmt.setString(10, timer.format(new java.util.Date()));
                    pstmt.setString(11, mtid);
                    pstmt.setString(12, mtid);
                    pstmt.setInt(13, var);
                    pstmt.setString(14, requestBranch);
                    result = (pstmt.executeUpdate() == -1);

                              

                }

                updateStatus=aprecords.updateUtil("update am_asset_approval set process_status ='"+statuxApproval+"' where asset_id='"+reqnID+"' and tran_type='Facility Mgt Requisition'");

                if ((!result) && (status.equalsIgnoreCase("FP") ) && (updateStatus!= -1)) {

                    message = "Requisition with ID " + reqnID + " has been received by Facility Mgt Dept. The AcceptanceID is " + acceptanceID;

                    mailSender.sendMailToAUser(fis, requistionInitiatorEmail, "Facility Mgt Acknowlegement", message);


                    out.print("<script>alert('Facility Acceptance successfully sent to " + supervisorName + "  for Approval.')</script>");
                    out.print("<script>window.location='DocumentHelp.jsp?np=facilityMgtAcceptanceUpdate&" +
                            "ReqnID=" + reqnID + "&acceptanceID=" + acceptanceID + "'</script>");
                } else if ((!result) && (status.equalsIgnoreCase("WX"))&& (updateStatus!= -1) ) {
                	
                    message = "Requisition with ID " + reqnID + " has been received by Facility Mgt Dept. The AcceptanceID is " + acceptanceID;

                    mailSender.sendMailToAUser(fis, requistionInitiatorEmail, "Facility Mgt Acknowlegement", message);

                    System.out.println(">>>>>>> here 3");
                    out.print("<script>alert('Facility Management Requisition sent for Work Order.')</script>");
                    out.print("<script>window.location='DocumentHelp.jsp?np=facilityMgtAcceptanceUpdate&" +
                            "ReqnID=" + reqnID + "&acceptanceID=" + acceptanceID + "'</script>");
                } else {
                }

            }//if for creating work descriptions
            else {
                
                
                out.print("<script>alert('Error Occured when creating work descriptions. ')</script>");
                    out.print("<script>window.location='DocumentHelp.jsp?np=facilityMgtAcceptance&" +
                            "ReqnID=" + reqnID + "&tranId="+tranId+"'</script>");
                
            }//else for creating work descriptions

            request.getSession().removeAttribute("isWordDescriptionCreated");
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
    }//gan

    }

    private int[] createWorkDescriptions(HttpServletRequest request) throws ServletException {
        //int workId =0;
        int[] numb = new int[2];
        try {

            FacilityManager facilityManager = new FacilityManager();

            ArrayList workList = new ArrayList();
            int numOfWork = (Integer) request.getSession().getAttribute("numOfWorks");

            String reqnID = request.getParameter("ReqnID");
            
            for (int j = 1; j <= numOfWork; ++j) {
                workList.add((WorkDescription) request.getSession().getAttribute("facilityMgtWorkDescrip" + j));
                request.getSession().removeAttribute("facilityMgtWorkDescrip" + j);
            }
            numb = facilityManager.creatWorkDescription(workList);

        } catch (Exception e) {
            System.out.println(">>Error occured in createWorkDescriptions of FacMgtAcceptanceServlet " + e);

        }


        return numb;


    }

   //  private int[] createWorkDescriptionsFromTemp(HttpServletRequest request) throws ServletException {
    	 private int[] createWorkDescriptionsFromTemp(int workId,String reqnID) throws ServletException {
        //int workId =0;
    	 System.out.println("In the createWorkDescriptionsFromTemp>>>>>>>>>>value of workId is "+ workId);
        int[] numb = new int[3];
        ArrayList workList = new ArrayList();
       

        try {

            FacilityManager facilityManager = new FacilityManager();
//           int workId = Integer.parseInt(request.getParameter("facilityMgtWorkId"));
            System.out.println("the >>>>>>>>>>value of workid is "+ workId);
           workList= facilityManager.getWorkDescriptionsBackup(workId,reqnID);
           numb = facilityManager.creatWorkDescription(workList, workId);
           numb[2]= workList.size();

        } catch (Exception e) {
            System.out.println(">>Error occured in createWorkDescriptionsTemp of FacMgtAcceptanceServlet " + e);

        }


        return numb;


    }



}
