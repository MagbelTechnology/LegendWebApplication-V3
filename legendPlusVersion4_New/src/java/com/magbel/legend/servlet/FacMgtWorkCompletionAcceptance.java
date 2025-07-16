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

public class FacMgtWorkCompletionAcceptance extends HttpServlet {

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
    public FacMgtWorkCompletionAcceptance() {
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

        String message = "";
        int updateStatus = 0;
        String statuxApproval = "";
        String realPath = getServletConfig().getServletContext().getRealPath("/mailConfig/legend.properties");

        FileInputStream fis = new FileInputStream(realPath);
        int[] outcome  = new int[3];

        String userClass = (String) request.getSession().getAttribute("UserClass");
        
        String assetId = request.getParameter("assetId");
        String tranId = request.getParameter("tranId");
        String transId = request.getParameter("transId");
        String workDescId = request.getParameter("workDescId");
        String workOrderCode = request.getParameter("workOrderCode1");
        String noOfWorkDes= request.getParameter("NoOfWorkDes");
        double contractValue = request.getParameter("contractValue1") ==null?0.00: Double.parseDouble(request.getParameter("contractValue1").replaceAll(",",""));
        String assetDesc = request.getParameter("assetDesc"); 
        String MaintenanceDate = request.getParameter("MaintenanceDate");
        String FQ_Due1 = request.getParameter("FQ_Due1");
        String FQ_Status1 = request.getParameter("FQ_Status1");
        String MaintenanceDate2 = request.getParameter("MaintenanceDate2");
        String FQ_Due2 = request.getParameter("FQ_Due2");
        String FQ_Status2 = request.getParameter("FQ_Status2");
        String MaintenanceDate3 = request.getParameter("MaintenanceDate3");
        String FQ_Due3 = request.getParameter("FQ_Due3");
        String FQ_Status3 = request.getParameter("FQ_Status3");
        String MaintenanceDate4 = request.getParameter("MaintenanceDate4");
        String FQ_Due4 = request.getParameter("FQ_Due4");
        String FQ_Status4 = request.getParameter("FQ_Status4");
        String vendorCode = request.getParameter("vendorCode");
        String reqnbranchCode = request.getParameter("reqnbranchCode");
        
        if (!userClass.equals("NULL") || userClass!=null){
        	
        String reqnID = request.getParameter("reqnId");
        int recepientId = request.getParameter("recepientId")==null?0:Integer.parseInt(request.getParameter("recepientId"));

     //String isWordDescriptionCreated = (String) request.getSession().getAttribute("isWordDescriptionCreated");
        if (noOfWorkDes == null) {
            noOfWorkDes = "0";
        }

        if (workDescId == null) {
            workDescId = "0";
        }

        if (Integer.parseInt(noOfWorkDes)<1) {
            out.print("<script>alert('You did not create the works done by vendor on the facility. ')</script>");
            out.print("<script>window.location='DocumentHelp.jsp?np=facilityMgtWorkCompletionForm&" +
                    "workOrderCode=" + reqnID + "&assetId="+assetId+"&tranId=" + tranId + "'</script>");
        } else {

            int NoOfWorkDesint =Integer.parseInt(noOfWorkDes);
            int workDescIdInt = Integer.parseInt(workDescId);
            //String categoryCode = aprecords.getCodeName("select CATEGORY_CODE from am_asset where asset_id ='" + assetId + "'");

           // String categoryShortName = aprecords.getCodeName("select category_acronym from am_ad_category where category_code = '" + categoryCode + "'");

            //String acceptanceID = applHelper.getGeneratedId("FM_MAITENANCE_DUE") + categoryShortName + currentDate.replaceAll("-", "");

            String mtid = applHelper.getGeneratedId("am_asset_approval");


            String userID = request.getParameter("userid");
            // String reqnBranch = request.getParameter("requestBranch");
            // String reqnSection = request.getParameter("ReqSection");
           // String image = aprecords.getCodeName("select image from FM_Requisition where reqnid = '" + reqnID + "'");
          //--  String requistionInitiatorEmail = aprecords.getCodeName("select email from am_gb_user where user_id in (select userId from FM_Requisition where reqnid='" + reqnID + "' )");
            String requistionInitiatorEmail = aprecords.getCodeName("select email from am_gb_user where user_id ="+recepientId);

            String acceptanceRemark = request.getParameter("acceptanceRemark") == null ? "" : request.getParameter("acceptanceRemark").toUpperCase();

            String supervisorID = userID;
String isWorkDescUpdatedQuery = "select work_description_updated from FM_MAITENANCE_DUE where acceptance_code='"+workOrderCode+"'";
String isWorkDescUpdated = aprecords.getCodeName(isWorkDescUpdatedQuery);

           // String acceptanceReceiveMeans = request.getParameter("acceptanceReceiveMeans");


            String status = "A";
            String supervisorName = "";
            String supervisorNameQry = "select Full_name,email,approval_limit from am_gb_user where user_ID ='" + request.getParameter("supervisor") + "'";
            int apprvLevel = 0;
            String reqnStatus = "";
            String adm_Approv_Lvl_Qry = "select level from Approval_Level_Setup where transaction_type='Facility Mgt Work Completion'";

            //String branchCode_Qry = "select branch_Code from am_ad_branch where branch_id=" + reqnBranch;

            // String branchCode = aprecords.getCodeName(branchCode_Qry);

            // System.out.println("the value of >>>>>>>>>> is aprecords.getCodeName(adm_Approv_Lvl_Qry) "+ aprecords.getCodeName(adm_Approv_Lvl_Qry) );

            int var = Integer.parseInt(aprecords.getCodeName(adm_Approv_Lvl_Qry));
            // System.out.println(">>>>>>>>>> request.getParameter(txnLevel)"+ request.getParameter("txnLevel"));

            int approvalLevelLimit = Integer.parseInt(request.getParameter("txnLevel"));



            if (approvalLevelLimit > 0) {
                outcome[1]=1;
                message = "Facility Mgt Work Completion with Work Order " + workOrderCode + " is waiting for your approval.";
                status = "WCFP";
                reqnStatus = "WORK DONE PENDING AT FAC MGT DEPT";
                statuxApproval = "WCFP";
                supervisorID = request.getParameter("supervisor");
                String[] sprvResult = (aprecords.retrieveArray(supervisorNameQry)).split(":");
                supervisorName = sprvResult[0];
                if ((sprvResult[1] != null) && !(sprvResult[1].equals(""))) {
                     String updateWorkOrderQuery=" update fm_work_order set status ='WCFP' where ReqnID='"+ reqnID+"'";
            genList.updateTable(updateWorkOrderQuery);
   /*         
            String insertLastServiceDateQuery="INSERT FM_PPM_SERVICEDATE SET  LASTSERVICE_DATE = '"+ MaintenanceDate+"',Q1_STATUS = '"+ FQ_Status1+"',Q2_STATUS = '"+ FQ_Status2+"', "+
    				"Q3_STATUS = '"+ FQ_Status3+"',Q4_STATUS = '"+ FQ_Status4+"',Q1_DUE_DATE = '"+ FQ_Due1+"',Q2_DUE_DATE = '"+ FQ_Due2+"',Q3_DUE_DATE = '"+ FQ_Due3+"',Q4_DUE_DATE = '"+ FQ_Due4+"' "+  
    				"WHERE REQNID = '"+ reqnID+"'";
                    genList.updateTable(insertLastServiceDateQuery);*/
                   
                    String insertLastServiceDateQuery = "INSERT INTO FM_PPM_SERVICEDATE(LASTSERVICE_DATE,Q1_STATUS,Q2_STATUS,Q3_STATUS,Q4_STATUS," +
                            "Q1_DUE_DATE,Q2_DUE_DATE,Q3_DUE_DATE, Q4_DUE_DATE,REQNID,TRANSID)" +
                            " values('"+ MaintenanceDate+"','"+ FQ_Status1+"','"+ FQ_Status2+"','"+ FQ_Status3+"','"+ FQ_Status4+"','"+ FQ_Due1+"','"+ FQ_Due2+"','"+ FQ_Due3+"','"+ FQ_Due4+"','"+ reqnID+"','"+ transId+"')";
                    genList.updateTable(insertLastServiceDateQuery);
                    System.out.println("======>insertLastServiceDateQuery: "+insertLastServiceDateQuery);
                    mailSender.sendMailToAUser(fis, sprvResult[1], "Facility Mgt Work Completion Approval", message);
                }
            } else {
                status = "WCX";
                reqnStatus = "PENDING FOR REQN INITIATOR";
                statuxApproval = "WCX";
                approvalLevelLimit = var;
                supervisorID = userID; 
                
                String updatePPMScheduleQuery="UPDATE FM_PPM SET  LASTSERVICE_DATE = '"+ MaintenanceDate+"',Q1_STATUS = '"+ FQ_Status1+"',Q2_STATUS = '"+ FQ_Status2+"', "+
				"Q3_STATUS = '"+ FQ_Status3+"',Q4_STATUS = '"+ FQ_Status4+"',Q1_DUE_DATE = '"+ FQ_Due1+"',Q2_DUE_DATE = '"+ FQ_Due2+"',Q3_DUE_DATE = '"+ FQ_Due3+"',Q4_DUE_DATE = '"+ FQ_Due4+"' "+  
				"WHERE BRANCH_CODE = '"+ reqnbranchCode+"',SUB_CATEGORY_CODE = '"+ assetId+"',VENDOR_CODE = '"+ vendorCode+"'";
                genList.updateTable(updatePPMScheduleQuery);

                String serviceDate_Query = "delete from FM_PPM_SERVICEDATE where REQNID = '" + reqnID +"'";
                genList.updateTable(serviceDate_Query);
                
            if(isWorkDescUpdated != null && isWorkDescUpdated.equalsIgnoreCase("Y")){
                String deleteInitialWorks_Query = "delete from FM_WORK_DESCRIPTION where work_id=" + workDescIdInt;
                genList.updateTable(deleteInitialWorks_Query);
                outcome = createWorkDescriptionsFromTemp(request,workDescIdInt);
            if (outcome[1] != -1) {
            //if the new work descriptions are successfully updated, then change  work_description_updated field to N
            String updateMaintenanceDueQuery=" update FM_MAITENANCE_DUE set work_description_updated ='N' where ReqnID='"+ reqnID+"'";
            genList.updateTable(updateMaintenanceDueQuery);

            //update status of work order so that it will not show in work order list a
            String updateWorkOrderQuery=" update fm_work_order set status ='WCX' where ReqnID='"+ reqnID+"'";
            genList.updateTable(updateWorkOrderQuery);
            
            }

            }

            }
          

            String facilityInsertQry = "insert into FM_COMPLETED_WORK (WORK_ORDER_CODE,ReqnID,ACCEPTANCE_REMARK,ASSET_ID,RECEIVE_BY," +
                    " RECEIVE_DATE,RECEIVE_TIME,ApprovalLevel,ApprovalLevelLimit,Supervisor,Status," +
                    "workStationIP,tran_id,work_id,work_completion_recepient) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";


            //int[] outcome = createWorkDescriptionsFromTemp(request,);



            boolean done = false;
            boolean result = false;
            try {
                if (outcome[1] != -1) {

                    int workId = workDescIdInt;
                    //int numberOfWorkDescriptions = outcome[2];

                    con = mgDbCon.getConnection("legendPlus");
                    pstmt = con.prepareStatement(facilityInsertQry);
                    pstmt.setString(1, workOrderCode);
                    pstmt.setString(2, reqnID);
                    pstmt.setString(3, acceptanceRemark);
                    pstmt.setString(4, assetId);
                    //pstmt.setString(5, acceptanceReceiveMeans);
                    pstmt.setInt(5, Integer.parseInt(userID));
                    pstmt.setTimestamp(6, mgDbCon.getDateTime(new java.util.Date()));
                    pstmt.setString(7, timer.format(new java.util.Date()));
                    pstmt.setInt(8, 0);
                    pstmt.setInt(9, approvalLevelLimit);
                    pstmt.setInt(10, Integer.parseInt(supervisorID));
                    pstmt.setString(11, status);
                    pstmt.setString(12, request.getRemoteAddr());
                    //pstmt.setString(14, "N");
                    pstmt.setInt(13, Integer.parseInt(mtid));
                    pstmt.setInt(14, workId);
                    pstmt.setInt(15,recepientId);
                   // pstmt.setInt(17, numberOfWorkDescriptions);
                    //pstmt.setString(18, image);


                    done = (pstmt.executeUpdate() == -1);
                    System.out.println("done >>>>>>>>> " + done);

                    if (!done)//successful
                    {
                        //to delete the work descriptions in FM_WORK_DESCRIPTION_TEMP
                        //
                       // GenerateList genList = new GenerateList();
                       

                        String ins_am_asset_approval_qry = "insert into am_asset_approval(asset_id,user_id,super_id,posting_date,description," +
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
                        pstmt.setString(8, "Facility Mgt Work Completion");
                        pstmt.setString(9, status);
                        pstmt.setString(10, timer.format(new java.util.Date()));
                        pstmt.setString(11, mtid);
                        pstmt.setString(12, mtid);
                        pstmt.setInt(13, var);
                        pstmt.setDouble(14,contractValue);
                        //pstmt.setString(15,assetDesc);
                        result = (pstmt.executeUpdate() == -1);



                    }

                    updateStatus = aprecords.updateUtil("update am_asset_approval set process_status ='" + statuxApproval + "' where asset_id='" + reqnID + "' and tran_type='Facility Mgt Requisition'");

                    if ((!result) && (status.equalsIgnoreCase("WCFP")) && (updateStatus != -1)) {



                        out.print("<script>alert('Facility Work Completion successfully sent to " + supervisorName + "  for Approval.')</script>");
                       // out.print("<script>window.location='DocumentHelp.jsp?np=facilityMgtAcceptanceUpdate&" + "ReqnID=" + reqnID + "&acceptanceID=" + acceptanceID + "'</script>");
                    out.print("<script>window.location='DocumentHelp.jsp?np=facilityMgtWorkCompletionFormUpdate&" +
                    "workOrderCode=" + workOrderCode + "&assetId="+assetId+"&tranId=" + tranId + "'</script>");
                    } else if ((!result) && (status.equalsIgnoreCase("WCX")) && (updateStatus != -1)) {

                         String deleteWorks_Query = "delete from FM_WORK_DESCRIPTION_TEMP where work_id=" + workId;
                        genList.updateTable(deleteWorks_Query);

                        message = "Work has been completed by vendor on Requisition with Requistion ID: " + reqnID + " for your confirmation and approval.";
                        mailSender.sendMailToAUser(fis, requistionInitiatorEmail, "Facility Mgt Work Completion", message);

                        System.out.println(">>>>>>> here 3");
                        out.print("<script>alert('Facility Work Completion sent to Requistion Initiator for confirmation.')</script>");
                       // out.print("<script>window.location='DocumentHelp.jsp?np=facilityMgtAcceptanceUpdate&" +"ReqnID=" + reqnID + "&acceptanceID=" + acceptanceID + "'</script>");
                    out.print("<script>window.location='DocumentHelp.jsp?np=facilityMgtWorkCompletionFormUpdate&" +
                    "workOrderCode=" + workOrderCode + "&assetId="+assetId+"&tranId=" + tranId + "'</script>");
                    } else {
                    }

                }//if for creating work descriptions
                else {


                    out.print("<script>alert('Error Occured when creating work descriptions. ')</script>");
                   // out.print("<script>window.location='DocumentHelp.jsp?np=facilityMgtAcceptance&" + "ReqnID=" + reqnID + "&tranId=" + tranId + "'</script>");
                   out.print("<script>window.location='DocumentHelp.jsp?np=facilityMgtWorkCompletionForm&" +
                    "workOrderCode=" + workOrderCode + "&assetId="+assetId+"&tranId=" + tranId + "'</script>");
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
        }//gan
    }else {
		out.print("<script>alert('You have No Right')</script>");
	}
    }

    private int[] createWorkDescriptions(HttpServletRequest request) throws ServletException {
        //int workId =0;
        int[] numb = new int[2];
        try {

            FacilityManager facilityManager = new FacilityManager();

            ArrayList workList = new ArrayList();
            int numOfWork = (Integer) request.getSession().getAttribute("numOfWorks");

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

    private int[] createWorkDescriptionsFromTemp(HttpServletRequest request,int workId) throws ServletException {
        //int workId =0;
        int[] numb = new int[3];
        ArrayList workList = new ArrayList();
        String reqnId = request.getParameter("reqnId");
        try {

            FacilityManager facilityManager = new FacilityManager();
            //int workId = (Integer) request.getSession().getAttribute("facilityMgtWorkId");
            System.out.println("the >>>>>>>>>>value of workid is " + workId);
            workList = facilityManager.getWorkDescriptionsBackup(workId,reqnId);
            numb = facilityManager.creatWorkDescription(workList, workId);
            numb[2] = workList.size();

        } catch (Exception e) {
            System.out.println(">>Error occured in createWorkDescriptionsTemp of FacMgtWorkCompletionAcceptanceServlet " + e);

        }


        return numb;


    }
}
