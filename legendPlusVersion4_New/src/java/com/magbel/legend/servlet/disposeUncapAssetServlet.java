package com.magbel.legend.servlet;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import magma.AssetRecordsBean;
import magma.net.manager.SytemsManager;
import magma.util.Codes;
import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.util.ApplicationHelper;


@MultipartConfig(
		  fileSizeThreshold = 0x300000, 
		  maxFileSize = 0xa00000,    
		  maxRequestSize = 0x3200000
		)
public class disposeUncapAssetServlet extends HttpServlet
{
	private EmailSmsServiceBus mail ;
	
	private ApprovalRecords records;
	 legend.admin.handlers.SecurityHandler sechanle = new legend.admin.handlers.SecurityHandler();
	 com.magbel.util.HtmlUtility htmlUtil  = new com.magbel.util.HtmlUtility();
	 com.magbel.legend.bus.ApprovalRecords aprecords  = new com.magbel.legend.bus.ApprovalRecords();
	 com.magbel.util.ApplicationHelper appHelper  = new com.magbel.util.ApplicationHelper();
	 legend.admin.handlers.CompanyHandler comp  = new legend.admin.handlers.CompanyHandler();
	 magma.asset.manager.UncapitaliseManager assetMan =  new magma.asset.manager.UncapitaliseManager();
	 magma.net.manager.RaiseEntryManager raiseMan = new magma.net.manager.RaiseEntryManager();
	 com.magbel.legend.bus.ApprovalRecords appRecords = new com.magbel.legend.bus.ApprovalRecords();
	 magma.AssetRecordsBean ad = null;
   public void doPost(HttpServletRequest request, 
    HttpServletResponse response)
      throws ServletException, IOException
     
   {
	   try {
	  ad = new magma.AssetRecordsBean();
	 String userClass = (String) request.getSession().getAttribute("UserClass");
	 String user_Id =(String) request.getSession().getAttribute("CurrentUser");
	// System.out.println("<<<<<<user_Id: "+user_Id);
	 PrintWriter out = response.getWriter();
	
	 HttpSession session = request.getSession();
	 Properties prop = new Properties();
	 File file = new File("C:\\Property\\LegendPlus.properties");
	 FileInputStream input = new FileInputStream(file);
	 prop.load(input);

	 Part filePart = request.getPart("file");
	 String fileName = filePart.getSubmittedFileName();
	 System.out.println("fileName: " + fileName);
	 
	 String ThirdPartyLabel = prop.getProperty("ThirdPartyLabel");
	 String singleApproval = prop.getProperty("singleApproval");
	 String VATRequired = prop.getProperty("VATREQUIRED");
	 String sbuRequired = prop.getProperty("SBUREQUIRED");

	 System.out.println("ThirdPartyLabel: " + ThirdPartyLabel);
	 
	 //To get information if approval is required for a transaction
	  com.magbel.util.ApplicationHelper appHelper = new com.magbel.util.ApplicationHelper();
	  
	int numOfTransactionLevel =  ad.getNumOfTransactionLevel("20");

	 int userId = Integer.parseInt((String)session.getAttribute("CurrentUser"));

	String[] pa= new String[12];
	 String id = request.getParameter("asset_id");

	 String opt = request.getParameter("operation");
//	 System.out.println("opt: " + opt);

	 String raiseEntry = request.getParameter("entryStatus");

	 String reason = request.getParameter("reason");

	 String strAmount = request.getParameter("disposalAmt");

	 String buyerAcct = request.getParameter("buyerAcct");

	 String strProfitLoss = request.getParameter("profitLoss");

	 String effDate = request.getParameter("effDate");

	 String disposalDate = request.getParameter("disposalDate");

	 String supervisor = request.getParameter("supervisor");

	 String description = request.getParameter("description");

	 String tranId = request.getParameter("tranId");
	// System.out.print("======tranId======> "+tranId);
	 int tranIDInt = tranId == null?0:Integer.parseInt(tranId);
	 System.out.println("tranIDInt: " + tranIDInt);
	 String mtid= "";
	  String userid = (String)session.getAttribute("CurrentUser");
	  String disposalId = new ApplicationHelper().getGeneratedId("am_group_asset_main");
	  disposalId =  disposalId + userId;
	  System.out.println("disposalId: " + disposalId);
	  
	  legend.admin.objects.User user = sechanle.getUserByUserID(userid);
	String userName = user.getUserName();
	String branchRestrict = user.getBranchRestrict();
	String UserRestrict = user.getDeptRestrict();
	String departCode = user.getDeptCode();
	String branch = user.getBranch();

	  EmailSmsServiceBus mail = new EmailSmsServiceBus();
	  
	 java.util.ArrayList approvelist =ad.getApprovalsId(branch,departCode,userName);
	   
	  int assetCode = request.getParameter("assetCode") == null?0:Integer.parseInt(request.getParameter("assetCode"));
	//  System.out.println("assetCode: " + assetCode);
	 
	 strAmount = strAmount.replaceAll(",","");
	 double amount = Double.parseDouble(strAmount);
	 strProfitLoss = strProfitLoss.replaceAll(",","");
	 double profitLoss = Double.parseDouble(strProfitLoss);
	 
	 pa[0]=id; pa[1]= Integer.toString(userId); pa[2]=supervisor; pa[3]=strAmount; pa[4]= disposalDate;
	 pa[5]= description; pa[6]= effDate; pa[8]="ACTIVE"; pa[9]="Uncapitalized Asset Disposal"; pa[10]="P";pa[11]="0"; 
	 
	 prop.load(input);
     String UPLOAD_FOLDER = prop.getProperty("imagesUrl");
     String uploadPath = UPLOAD_FOLDER;
     
     File uploadDir = new File(uploadPath);
     if(!uploadDir.exists())
     {
         uploadDir.mkdir();
     }
    
	 
	  
	 
	 int i = 0;
	 if(opt.equals("0"))
	  {
	  
	   if(numOfTransactionLevel ==0){
	  i = assetMan.insertAssetDisposalNoSupervisor(id,reason,buyerAcct,amount,raiseEntry,profitLoss,disposalDate,effDate,userId,supervisor,assetCode,disposalId);
	  
	  //change the transaction status to A for approved and supervisor id to current user id 
	  //since approval level is not involved in the transaction 
	  if(i != -1){
	   pa[2]=Integer.toString(userId);
	   pa[10]="A";
	   
	    mtid=ad.setPendingTrans2(pa,"20",assetCode);
	   
	   //BEGIN: THIS SEGMENT IS TO SET ASSET STATUS OF DISPOSED ASSET TO APPROVED.
	   String q3 = "update AM_ASSET_UNCAPITALIZED set Asset_Status='ACTIVE' where asset_id = '"+id+"'";
	         ad.updateAssetStatusChange(q3);
	   //END: THIS SEGMENT IS TO SET ASSET STATUS OF DISPOSED ASSET TO APPROVED.
	   
	   	  //THE SEGMENT BEGINS RAISE ENTRY FOR NEW ASSET CREATION THAT DOESN'T REQUIRE APPROVAL
		 
		 String assetRaiseEntry =appRecords.getCodeName(" SELECT raise_entry from am_gb_company ");
		 ApprovalRecords aprecords = new ApprovalRecords();
		
		 String[] raiseEntryInfo = appRecords.raiseEntryInfo(id);

	           //String description = raiseEntryInfo[0];
	           String branch_id = raiseEntryInfo[1];
	           String subject_to_vat = raiseEntryInfo[2];
	           String wh_tax = raiseEntryInfo[3];
	           String flag = "";
	           String partPay = "";
	           String asset_User_Name = appRecords.getCodeName("SELECT full_name from am_gb_user where user_id = " + userId);
	           String branchName = appRecords.getCodeName(" SELECT branch_name from am_ad_branch where branch_id= " + Integer.parseInt(branch_id) );
		    String page1 = "Uncapitalized Asset Disposal";
			String url = "DocumentHelp.jsp?np=disposeUncapitalizedAssetRaiseEntry&id=" +id+"&operation=1&exitPage=manageUncapitalDisposals&pageDirect=Y";
			
//			if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("Y")){  
//		  	appRecords.insertApprovalx(id, description, page1, flag, partPay,asset_User_Name,branchName,subject_to_vat,wh_tax,url,mtid,assetCode);
//			}	 
		  appRecords.updateRaiseEntry(id);
		
		 //THE SEGMENT ENDS RAISE ENTRY FOR NEW ASSET CREATION THAT DOESN'T REQUIRE APPROVAL
		 
		 
		 
		 
		 
		 //===============================BEGIN: THIS SEGMENT IS TO SEND E-MAIL TO RECEPIENT AFTER ASSET CREATION
		/* UNCOMMENT TO ALLOW EMAIL TO BE SENT TO USER AFTER ASSET IS DISPOSED
		
		// if(b){
		 	
			legend.admin.handlers.CompanyHandler compHandler = new legend.admin.handlers.CompanyHandler();
				//String mail_code="002"; //the mail code for each transaction is setup in AM_TRANSACTION_TYPE
				//String Status1=compHandler.getEmailStatus(mail_code);

			String[] mailSetUp = compHandler.getEmailStatusAndName("asset disposal");
			String Status1 = mailSetUp[0];
			String mail_code = mailSetUp[1];
			
			Status1 = Status1.trim();
			System.out.println("#$$$$$$$$$$$ Status1 "+Status1+" $$$$$$$$$$$$$$$$$");
			System.out.println("#$$$$$$$$$$$ mail_code "+mail_code+" $$$$$$$$$$$$$$$$$");
			
			if(Status1.equalsIgnoreCase("Active"))
			{ //if mail status is active then send email
		 
			String transaction_type="Asset Disposal";
			String subject ="Asset Disposal";
			
			Codes message= new Codes();
			
			EmailSmsServiceBus mail = new EmailSmsServiceBus();

			String to = message.MailTo(mail_code, transaction_type);  //retrieves recipients from database

			String msgText1 =message.MailMessage(mail_code, transaction_type);//"New asset with ID: "+id +" was successfully created.";
	System.out.println("#$$$$$$$$$$$ "+to);
			mail.sendMail(to,subject,msgText1);
		}//if(compHandler.getEmailStatus(mail_code).equalsIgnoreCase("Active"))
		 
		// }//if(b)
		 */
		 //================================END:  THIS SEGMENT IS TO SEND E-MAIL TO RECEPIENT AFTER ASSET CREATION
		 
		  
	   
	   
	   out.print("<script>alert('Asset successfully disposed')</script>");
	   out.print("<script>window.location ='DocumentHelp.jsp?np=manageUncapitalDisposals'</script>");
	  // response.sendRedirect("DocumentHelp.jsp?np=disposeAsset&id="+id+"&operation=1&exitPage=manageDisposals");
	   } // if(i != -1)
	   
	 
	  
	  
	   }//if(numOfTransactionLevel ==0)
	   else{
	   i = assetMan.insertAssetDisposal(id,reason,buyerAcct,amount,raiseEntry,profitLoss,disposalDate,effDate,userId,supervisor,assetCode,disposalId);
	    if(singleApproval.equalsIgnoreCase("Y")){
	   ad.setPendingTrans(pa,"20",assetCode);
	   
	   //to set asset status to active and date disposed to null by gani
	   String changeStatus="update AM_ASSET_UNCAPITALIZED set asset_status='ACTIVE', date_disposed= NULL where asset_id='"+id+"'";
	   ad.updateAssetStatusChange(changeStatus);
	  // out.print("<script>alert('Request for asset disposal submitted')</script>");
	   			 
	  // System.out.println(">>>>>>insertion  value " + i);
	   if(i != -1 )
	   {  // System.out.println(">>>>>> about to populate the approval table >>>>>>>>>>>>>>>>>>>>>>>"  );
	    out.print("<script>alert('Request for asset disposal submitted')</script>");
		out.print("<script>window.location ='DocumentHelp.jsp?np=manageUncapitalDisposals'</script>");
			//response.sendRedirect("DocumentHelp.jsp?np=disposeAsset&id="+id+"&operation=1&exitPage=manageDisposals");
	   }//if(i != -1 )
	  }   
			   if(singleApproval.equalsIgnoreCase("N")){
		   		pa[8]="PENDING";
				pa[10]="P";
		  		mtid =  appHelper.getGeneratedId("am_asset_approval");
//		   		System.out.println("approvelist.size()#$$$$$$$$$$$ "+approvelist.size());
		   	 for(int j=0;j<approvelist.size();j++)
		     {  
			  	legend.admin.objects.User usr = (legend.admin.objects.User)approvelist.get(j);   	 
				String supervisorId =  usr.getUserId();
				String mailAddress = usr.getEmail();
				String supervisorName = usr.getUserName();
				String supervisorfullName = usr.getUserFullName();
//				System.out.println("SupervisorId#$$$$$$$$$$$ "+supervisorId);
		  		 ad.setPendingTransMultiApp(pa,"20",assetCode,supervisorId,disposalId);
				 String lastMTID = ad.getCurrentMtid("am_asset_approval");
	   String changeStatus="update AM_ASSET_UNCAPITALIZED set asset_status='ACTIVE', date_disposed= NULL where asset_id='"+id+"'";
	   ad.updateAssetStatusChange(changeStatus);
	   
		String subjectr ="Uncapitalized Asset Disposal Approval";
		String msgText11 ="Uncapitalized Asset with ID: "+ id +" for Disposal is waiting for your approval.";
	 
	//String otherparam = "newAssetApproval&operation=1&id="+id+"&tranId="+approvaltransId+"&transaction_level=1&approval_level_count=0&assetCode="+assetCode;				
					 comp.insertMailRecords(mailAddress,subjectr,msgText11);
					
	 out.print("<script>alert('Request for Uncapitalized asset disposal submitted for Approval')</script>");
		out.print("<script>window.location ='DocumentHelp.jsp?np=manageUncapitalDisposals'</script>");
							 
		     	}
		  	 }
	   
	   }//else
	   
	   try
	     {
	     	 if(!fileName.endsWith(".php") || !fileName.endsWith(".sql")) {
	     	 for (Part part : request.getParts()) {
	                 
	                 String ext = FilenameUtils.getExtension(fileName);
	             //    System.out.println("The extension is =>: " + ext);
	                 
	                 String filePath = uploadPath + "W" + fileName.toString();
	                // System.out.println("The file path is ==: " + filePath);
	                 
	                 String newPath = uploadPath + "W" + disposalId + "." + ext;
//	                 System.out.println("The new file path is >=: " + newPath);
	                 
	                
	                 File oldFile = new File(filePath);
	                 
	                 File newFile = new File(newPath);
	                 
	                 String newFileName = newFile.getName().substring(0, newFile.getName().lastIndexOf("."));
	                 
	               //  System.out.println("newFileName >=: " + newFileName);
	                 
	                 File folder = new File(UPLOAD_FOLDER);
	     	        File[] listOfFiles = folder.listFiles();
	     	        
	     	       String listFiles = Arrays.toString(listOfFiles);
	     	       	       
	                 
	                 oldFile.renameTo(newFile);

	                 part.write(newPath);
	                 
	                // System.out.println("uploaded successfully.");
	           
	 	    }
	     	 
	        // System.out.println("The destination is ==: " + destination);
	        System.out.println("Upload has been done successfully!");

	         
	         }else {
	         	System.out.println("Incorrect File..");
	         }
	     }
	     catch(Exception ex)
	     {
	         System.out.println((new StringBuilder("There was an error: ")).append(ex.getMessage()).toString());
	       
	     }
	   
	  } //if(opt.equals("0"))
	  
	  else if(opt.equals("1"))
	  {
	   boolean status = false;
	   status = raiseMan.isEntryRaised("SELECT RAISE_ENTRY FROM AM_ASSETDISPOSAL WHERE ASSET_ID = '"+id+"'");
	   if(!status)
	   {
	    i = assetMan.updateAssetDisposal(id,reason,buyerAcct,amount,raiseEntry,profitLoss,disposalDate,effDate,userId);
		//String updateAmAssetApproval ="update am_asset_approval set"
		String deleteFromAmAssetApproval = "delete from am_asset_approval where transaction_id=" +tranIDInt;
		appRecords.deleteQuery(deleteFromAmAssetApproval);
		ad.setPendingTransRepost(pa,"2",tranIDInt,assetCode);
	   }
	   else
	   {
	    i = assetMan.updateAssetDisposal(id,reason,buyerAcct,raiseEntry,userId);
	   }	
	   if(i !=-1 )
	   {
	   
	      		String subjectr ="Asset Disposal Approval";
			String msgText11 ="Asset with ID: "+ id +" for Disposal is waiting for your approval.";
			String supervisorName =appRecords.getCodeName(" SELECT full_name from am_gb_user where user_id=?",supervisor);
	 		String  approvaltransId  = appRecords.getCodeName("select transaction_id from am_asset_approval where Transaction_Id = '"+mtid+"' and ASSET_ID= '"+id+"' ");	  
	String otherparam = "disposeAssetApproval&operation=1&id="+id+"&tranId="+approvaltransId+"&transaction_level=1&approval_level_count=0&assetCode="+assetCode;				
					mail.sendMailSupervisor(supervisor, subjectr, msgText11,otherparam);
	   
	 //   out.print("<script>alert('Disposed Asset Successfully Updated.')</script>");
	out.print("<script>alert('Request for asset disposal submitted to "+supervisorName+" for Approval')</script>");
		//ad.setPendingTrans(pa);
	    //response.sendRedirect("DocumentHelp.jsp?np=disposeAsset&id="+id+"&operation=1&exitPage=disposedAssetList");
		out.print("<script>window.location ='DocumentHelp.jsp?np=manageUncapitalDisposals'</script>");
	   }
	  }
	  else
	  {}
	 	   	 
       

	 }catch(Throwable e) {
		 e.getMessage();
	 }
	 
	
   }
   public void doGet(HttpServletRequest request, 
		    HttpServletResponse response)
		      throws ServletException, IOException
		   {
	   doPost(request, response);
		   }
}