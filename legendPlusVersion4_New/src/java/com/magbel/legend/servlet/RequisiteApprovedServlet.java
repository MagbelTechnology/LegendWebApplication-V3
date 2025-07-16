/*
 * x500SecurityCheck.java
 *
 * Created on May 13, 2006, 3:47 PM

 * @author  Jejelowo.B.Festus
 * @Docs Integrated Accounting Software
 * @version 1.0
 * Description -->This is a servlet that performs
 		security check[validate] user information
 		and retrived assigned priviledges based on
 		the role of the user.
*@Modified by Bolanle M. Sule @ 8th Jan. 2008.
 */
package com.magbel.legend.servlet;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import java.util.*;

import com.magbel.ia.dao.PersistenceServiceDAO;
import com.magbel.ia.util.ApplicationHelper2;
import com.magbel.ia.util.MailSender;
import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.legend.bus.ApprovalManager;
import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.util.License;
import com.magbel.ia.bus.DistributionOrderServiceBus;
public class RequisiteApprovedServlet extends HttpServlet {
	PersistenceServiceDAO persistenceServiceDAO = null; 
	ApplicationHelper2 applHelper = null;
	MailSender mailSender = null;
	EmailSmsServiceBus mail = new EmailSmsServiceBus();
	DistributionOrderServiceBus Order = null;
	private ApprovalRecords aprecords;
  /** Initializes the servlet.
   */

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    aprecords = new ApprovalRecords();
    applHelper = new ApplicationHelper2();
    mailSender = new MailSender ();
    Order = new DistributionOrderServiceBus();
  }

  /** Destroys the servlet.
   */
  public void destroy() {

  }

  /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
   * @param request servlet request
   * @param response servlet response
   */
  public void service(HttpServletRequest request, HttpServletResponse response) throws
      ServletException, IOException {

	response.setContentType("text/html");

    HttpSession session = request.getSession();
    PrintWriter out = response.getWriter();
    String userClass = (String) session.getAttribute("UserClass");
    
	String realPath="C:/Property/Inventory.properties";
	System.out.println("realPath >>>>>>>>>>> " + realPath);
	
	FileInputStream fis =  new FileInputStream(realPath);  
	ApprovalManager approverManager = new ApprovalManager();
	String pageName = request.getParameter("pageName");	
	String userID = request.getParameter("userid");	
	String compCode = request.getParameter("comp_code");
    String approved = request.getParameter("Approve");
    String supervisorID = userID;
    String reject = request.getParameter("Reject");
    String reqnID = request.getParameter("ReqnID").trim();
    String postingDate = request.getParameter("postingDate").trim();
    String returnStock = request.getParameter("returnStock");
    String itemType = request.getParameter("itemType");
    String warehouseCode = request.getParameter("warehousecode");
    String availableQuant = request.getParameter("availableQuant");
    String QtyApproved = request.getParameter("no_of_items");
    String rejectionReason = request.getParameter("rejectionReason");
    String day = postingDate.substring(0, 2);
    String month = postingDate.substring(3, 5);
    String year = postingDate.substring(6, 10);
    postingDate = year+"-"+month+"-"+day;  
    String itemCode = aprecords.getCodeName("select ItemRequested from am_ad_Requisition where ReqnID='"+reqnID+"'");
    System.out.println("QtyApproved in RequisiteApprovedServlet >>>>>>>>>>> " + QtyApproved+"    itemCode: "+itemCode);
 //   System.out.println("<<<<<<<<<approved: "+approved+"   reject >>>>>>>>>>> " + reject+"   <<<<<<<reqnID: "+reqnID);
    //String [] asset_Id = returnStock.split(";");
    String approvedCount = aprecords.getCodeName("select count(*) from am_asset_approval where asset_id  = '"+reqnID+"' and process_status != 'A'");
    int counter = 0;

  //  Date approveddate = persistenceServiceDAO.getDateTime(new java.util.Date());
//    System.out.println("<<<<<<<RequisiteApprovedServlet Date Time: "+persistenceServiceDAO.getDateTime(new java.util.Date()));
//    System.out.println("<<<<<<<Posting Date in approval day: "+day+"     month: "+month+"   Year: "+year+"   postingDate: "+postingDate);
//	ServletContext sc = this.getServletContext();
//	System.out.println("<<<<<<<RequisiteApprovedServlet approved: "+approved+"     reqnID: "+reqnID+"  reject: "+reject);
	String supervisorName ="";
	String supervisorNameQry = "select Full_name,email,approval_limit from am_gb_user where user_ID ="+userID+"";
     try
     
     { 
    	 if (!userClass.equals("NULL") || userClass!=null){
    	 String approvedby = aprecords.getCodeName("select full_name from am_gb_User where User_ID="+userID+" ");
    	 String batchId = aprecords.getCodeName("select distinct batch_id from am_asset_approval where asset_id = '"+reqnID+"'");
			supervisorID = request.getParameter("supervisor");
			 String[] sprvResult=(applHelper.retrieveArray(supervisorNameQry)).split(":");
			supervisorName = sprvResult[0];  

			 String apprLevelCountQry = "SELECT COUNT(*) FROM am_asset_approval WHERE ASSET_ID = '"+reqnID+"' AND PROCESS_STATUS != 'A'";
			 int TransactionLevelCount = Integer.parseInt(aprecords.getCodeName(apprLevelCountQry));
			 
			 String apprLevelQry = "select Level from Approval_Level_Setup where code='30'";
			 int numOfTransactionLevel = Integer.parseInt(aprecords.getCodeName(apprLevelQry));
         	String approverUser = approverManager.SupervisorMailAddressList(batchId);
         	String []SupervisorId = approverUser.split("#");
         	int No = SupervisorId.length;
    if(approved.equalsIgnoreCase("Approve")){
    	 if(reqnID !="")
    		 {
    		 String status = "A"; 
    		 String approvedStatus = "Approved"; 
    		 String distributionStatus = "PENDING";
    //		 if((TransactionLevelCount != 1) && numOfTransactionLevel != 1){
    			 if(TransactionLevelCount != 1){    			 
    		 Order.ApprovalStatusUpdate(reqnID,status,distributionStatus,postingDate,userID);
 			for(int j=0;j<No;j++){
                String approvalMail = aprecords.getCodeName("select email from am_gb_User where user_id="+SupervisorId[j]+"");
                String subject ="SCRIN Approved";
                String msgText1 = "Your SCRIN with Requisition Id: "+ reqnID +" has been approved by "+approvedby+".";
                System.out.println("#$$$$$$$$$$$ msgText1: "+msgText1+"  approvalMail: "+approvalMail);
       		mail.sendMail(approvalMail,subject,msgText1);
			}    		 
    		 out.println("<script>alert('Transaction has been Approved');</script>");
			 out.print("<script>window.location ='DocumentHelp.jsp?np=transactionForApprovalList'</script>");
    		 }
    		 else{
    		 boolean done = Order.updateTotalForRequisition(status,itemCode,warehouseCode,Integer.parseInt(QtyApproved));
    		 if(done==true){
    			 Order.ApprovalStatusUpdateforMultiple(reqnID,status,distributionStatus,postingDate,userID);
    			 
//    			System.out.println("<<<<<<<RequisiteApprovedServlet 2 approved: "+approved+"     reqnID: "+reqnID);
   // 		 aprecords.getCodeName("UPDATE am_ad_Requisition SET Status = 'A', distributedstatus='pending' WHERE ReqnID = '"+reqnID+"'");
   // 		 aprecords.getCodeName("UPDATE am_asset_approval SET process_status = 'A',DATE_APPROVED = '"+postingDate+"' WHERE asset_id = '"+reqnID+"'");
 //   		 System.out.println("pageName in RequisiteApprovedServlet >>>>>>>>>>> " + pageName);
    		 if(pageName.equalsIgnoreCase("returnedUnuseApproval")){
    			 distributionStatus = "RETURNED";
    			 String [] asset_Id = returnStock.split(";");
    			 String assetId = "";
    			    for (int i = 0; i < asset_Id.length; i ++){
    			    	if((i!=0)||(i!=(asset_Id.length - 1))){assetId=assetId+",";}
    			    	assetId = assetId+"'"+asset_Id[i]+"'";
    			        if (asset_Id[i] != null)
    			            counter ++;}
//    			    System.out.println("<<<<<<<<=====assetId: "+assetId+"    Counter: "+counter);
    			     status = "ACTIVE";
    			    Order.returnStockForDistributionAccepted(assetId,status,distributionStatus);
    			// aprecords.getCodeName("UPDATE ST_STOCK SET ASSET_STATUS = 'ACTIVE' WHERE ASSET_ID IN ("+assetId+")");
    			 	 }
    		 System.out.println("<<<<<<<<=====sprvResult: "+sprvResult[1]);
	/*		if ((sprvResult[1] != null)&&(sprvResult[1] != ""))
			{
				mailSender.sendMailToSupervisor(fis,sprvResult[1],reqnID);
				mailSender.sendMailToAdmin(fis,reqnID,compCode);
			}
    		 */
    		
    		 if(numOfTransactionLevel==1){aprecords.getCodeName("delete from am_asset_approval where asset_id = '"+reqnID+"' and super_id != '"+userID+"'");}
    		 aprecords.getCodeName("update am_ad_Requisition set Supervisor = '"+userID+"' where ReqnID = '"+reqnID+"'");
    		 String createdUserId = aprecords.getCodeName("select UserID from am_ad_Requisition where ReqnID='"+reqnID+"'");
    		 String createdby = aprecords.getCodeName("select email from am_gb_User where user_id="+createdUserId+"");
    		 String initiatedName = aprecords.getCodeName("select full_name from am_gb_User where user_id="+createdUserId+"");
    		 String subject ="SCRIN Approved";
    			String msgText1 = "Your SCRIN with Requisition Id: "+ reqnID +" has been approved by "+approvedby+".";
    			System.out.println("#$$$$$$$$$$$ "+createdby);
    			System.out.println("#$$$$$$$$$$$ msgText1: "+msgText1);
    			mail.sendMail(createdby,subject,msgText1);
    			//msgText1 = "SCRIN with Requisition Id: "+ reqnID +" has been approved by '"+supervisorName+"'.";
    			String tomail = aprecords.getCodeName("select mail_address from am_mail_statement where mail_code='APP'");
    			mail.sendMail(tomail,subject,msgText1);   
    			
            	System.out.println("#@@@@@@@ No: "+No);
    			for(int j=0;j<No;j++){
                    String approvalMail = aprecords.getCodeName("select email from am_gb_User where user_id="+SupervisorId[j]+"");
                    subject ="SCRIN Approved";
                    msgText1 = "Your SCRIN with Requisition Id: "+ reqnID +" has been approved by "+approvedby+".";
                    System.out.println("#$$$$$$$$$$$ msgText1: "+msgText1+"  approvalMail: "+approvalMail);
           		mail.sendMail(approvalMail,subject,msgText1);
    			}
    	//	 }
    		 out.println("<script>alert('Transaction has been Approved');</script>");
				out.print("<script>window.location ='DocumentHelp.jsp?np=transactionForApprovalList'</script>");
    		 }
    		 }
    		 }
    	 else {
    		 out.println("<script>alert('Transaction cannot be Approved');</script>");
    		 out.print("<script>window.location ='DocumentHelp.jsp?np=transactionForApprovalList'</script>");
    	      }
     }
    if(reject.equalsIgnoreCase("Reject")){
   	 if(reqnID !="")
   		 {
   		 aprecords.getCodeName("UPDATE am_ad_Requisition SET Status = 'R', distributedstatus='Rejected' WHERE ReqnID = '"+reqnID+"'");
   		 aprecords.getCodeName("UPDATE am_asset_approval SET process_status = 'R',DATE_APPROVED = '"+postingDate+"' WHERE asset_id = '"+reqnID+"'");
//   		if(numOfTransactionLevel==1){aprecords.getCodeName("delete from am_asset_approval where asset_id = '"+reqnID+"' and super_id != '"+userID+"'");}
//   		if(Integer.parseInt(approvedCount) == numOfTransactionLevel){
   		String createdUserId = aprecords.getCodeName("select UserID from am_ad_Requisition where ReqnID='"+reqnID+"'");
		 String createdby = aprecords.getCodeName("select email from am_gb_User where user_id="+createdUserId+"");
		 String subject ="SCRIN Rejected";
			String msgText1 = "Your Transaction with Requisition Id: "+ reqnID +" has been Rejected by "+supervisorName+". Reason: "+rejectionReason+" ";
			System.out.println("#$$$$$$$$$$$ "+createdby);
			System.out.println("#$$$$$$$$$$$ msgText1: "+msgText1);
			mail.sendMail(createdby,subject,msgText1);
			String othermail = aprecords.getCodeName("select MAIL_ADDRESS from am_mail_statement where MAIL_CODE = '00'");
			mail.sendMail(othermail,subject,msgText1);
  // 		}
   		 out.println("<script>alert('Transaction has been Rejected');</script>");
				out.print("<script>window.location ='DocumentHelp.jsp?np=transactionForApprovalList'</script>");
	/*			if ((sprvResult[1] != null)&&(sprvResult[1] != ""))
				{
					mailSender.sendMailToSupervisor(fis,sprvResult[1],reqnID);
					mailSender.sendMailToAdmin(fis,reqnID,compCode);
				}   	*/	 
   		 }
   	 else {
   		 out.println("<script>alert('Transaction cannot be Rejected');</script>");
   		 out.print("<script>window.location ='DocumentHelp.jsp?np=transactionForApprovalList'</script>");
   	      }
    } 
     }else {
 		out.print("<script>alert('You have No Right')</script>");
 	}
     }
     catch(NullPointerException ne)
     { 
    	 ne.printStackTrace();
      }

}


  public String getServletInfo() {
    return "Approval Servlet";
  }

}
