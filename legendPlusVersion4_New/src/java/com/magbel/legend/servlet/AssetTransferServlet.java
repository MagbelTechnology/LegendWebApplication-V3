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
import legend.AutoIDSetup;
import magma.AssetRecordsBean;
import magma.net.manager.SytemsManager;
import magma.util.Codes;
import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;


@MultipartConfig(
		  fileSizeThreshold = 0x300000, 
		  maxFileSize = 0xa00000,    
		  maxRequestSize = 0x3200000
		)
public class AssetTransferServlet extends HttpServlet
{
	private EmailSmsServiceBus mail ;
	
	private ApprovalRecords records;
	 legend.admin.handlers.SecurityHandler sechanle = new legend.admin.handlers.SecurityHandler();
	 com.magbel.util.HtmlUtility htmlUtil  = new com.magbel.util.HtmlUtility();
	 com.magbel.legend.bus.ApprovalRecords aprecords  = new com.magbel.legend.bus.ApprovalRecords();
	 com.magbel.util.ApplicationHelper appHelper  = new com.magbel.util.ApplicationHelper();
	 legend.admin.handlers.CompanyHandler comp  = new legend.admin.handlers.CompanyHandler();
	 magma.asset.manager.AssetManager assetMan =  new magma.asset.manager.AssetManager();
	 magma.net.manager.RaiseEntryManager raiseMan = new magma.net.manager.RaiseEntryManager();
	 
   public void doPost(HttpServletRequest request, 
    HttpServletResponse response)
      throws ServletException, IOException
     
   {
	   try {
		   ApprovalRecords aprecords = new ApprovalRecords();
		   HttpSession session = request.getSession();
		   magma.AssetRecordsBean ad = new magma.AssetRecordsBean();
		   magma.net.manager.AssetManager assetManager = null;
		   
		   Properties prop = new Properties();
		   File file = new File("C:\\Property\\LegendPlus.properties");
		   FileInputStream input = new FileInputStream(file);
		   prop.load(input);
		   
		   String singleApproval = prop.getProperty("singleApproval");
		   System.out.println("singleApproval: " + singleApproval);
		   
		   PrintWriter out = response.getWriter();
		   
		     int userId = Integer.parseInt((String)session.getAttribute("CurrentUser"));

		      int numOfTransactionLevel =  ad.getNumOfTransactionLevel("6");
		  	
		   // int assetCode = request.getParameter("assetCode") == null?0:Integer.parseInt(request.getParameter("assetCode"));
		     String userid = (String)session.getAttribute("CurrentUser");
		    String id = request.getParameter("assetId");
		    String opt = request.getParameter("operation");
		    magma.asset.dto.Asset asset = (magma.asset.dto.Asset)assetMan.getAsset(id);
		    
		    int assetCode = request.getParameter("assetCode") == null?0:Integer.parseInt(request.getParameter("assetCode"));
		    
		    
		     legend.admin.objects.User user = sechanle.getUserByUserID(userid);
		  	String userName = user.getUserName();
		  	String branchRestrict = user.getBranchRestrict();
		  	String UserRestrict = user.getDeptRestrict();
		  	String departCode = user.getDeptCode();
		  	String branch = user.getBranch();

		     EmailSmsServiceBus mail = new EmailSmsServiceBus();
		     
		     java.util.ArrayList approvelist =ad.getApprovalsId(branch,departCode,userName);
		        
		    String raiseEntry = request.getParameter("entryStatus");
		  //  System.out.println("<<<<<<<<<<Inside>>>>>>>>>>>> ");
		    int newBranch  = Integer.parseInt(request.getParameter("newBranch"));
		  //  System.out.println("newBranch==== "+newBranch);
		    int newDept = Integer.parseInt(request.getParameter("newDept"));
		  //  System.out.println("newDept===== "+newDept);
		    int newSec = Integer.parseInt(request.getParameter("newSection"));
		  //  System.out.println("newSec==== "+newSec);
		    String newWhoToRem1 = request.getParameter("newWhoToRem1");
		    String newWhoToRem2 = request.getParameter("newWhoToRem2");
		    String newEmail1 = request.getParameter("newEmail1");
		    String newEmail2 = request.getParameter("newEmail2");
		    String newUser = request.getParameter("userList");
		    System.out.println("newUser>>>>>>>>>>>> "+newUser);
		    
		    String transferDate = request.getParameter("newTransferDate");
		    String effDate = request.getParameter("newEffDate");
		    String supervisor = request.getParameter("supervisor");
		    String description = request.getParameter("description");
		    String cost = request.getParameter("COST");
		    String category = request.getParameter("category");
		    String tran_status= request.getParameter("tran_status");
		    String newsbu_code = request.getParameter("newsbu_code");
		    String oldsbu_code = request.getParameter("oldsbu_code");
		    String asset_user = request.getParameter("userList");
		  //  System.out.println("newsbu_code>>>>>>>>>>>> "+newsbu_code);
		  //  System.out.println("oldsbu_code>>>>>>>>>>>> "+oldsbu_code);
		   // int assetCode = asset.getAssetCode();
		      
		    String change_id_query4= "";
		   
		    double nbv = request.getParameter("nbv") == null || request.getParameter("nbv").equalsIgnoreCase("null")?0:Double.parseDouble(request.getParameter("nbv").replaceAll(",",""));
		    
		    double monthDep= request.getParameter("monthDep") == null || request.getParameter("monthDep").equalsIgnoreCase("null")?0:Double.parseDouble(request.getParameter("monthDep").replaceAll(",",""));
//		      System.out.println("oldsbu_code>>>>>>>>>>>> "+oldsbu_code)
		    double accumDep=request.getParameter("accumDep") == null || request.getParameter("accumDep").equalsIgnoreCase("null")?0:Double.parseDouble(request.getParameter("accumDep").replaceAll(",",""));
		    
		    String fromRepost = request.getParameter("fromRepost");
		    String oldUser = request.getParameter("oldUser");
		    
		    int categoryId = request.getParameter("categoryId") == null || request.getParameter("categoryId").equalsIgnoreCase("null")?0:Integer.parseInt(request.getParameter("categoryId"));
		    
		    String categoryCode = aprecords.getCodeName("SELECT  category_code from am_asset where asset_id = '" + id+"'");
		    
		    int mtidRepost = request.getParameter("mtid") == null || request.getParameter("mtid").equalsIgnoreCase("null")?0:Integer.parseInt(request.getParameter("mtid"));
		    
		    
		    int oldBranchId = request.getParameter("oldBranchId") == null?0:Integer.parseInt(request.getParameter("oldBranchId"));
		    int oldDeptId = request.getParameter("oldDeptId") == null?0:Integer.parseInt(request.getParameter("oldDeptId"));
		    int oldSectionId = request.getParameter("oldSectionId") == null?0:Integer.parseInt(request.getParameter("oldSectionId"));
		    
		   

		    
		    
		    			cost = cost.replaceAll(",","");
		    String[] pa = new String[12];
		    
		     pa[0]=id; 
		     pa[1]= Integer.toString(userId); 
		     pa[2]=supervisor; 
		     pa[3]=cost; 
		     pa[4]= transferDate ;
		     pa[5]= description; 
		     pa[6]= effDate; 
		     pa[8]="ACTIVE"; 
		     pa[9]="Asset Transfer"; 
		     pa[10]="P"; 
		     pa[11]=cost;
		   
		    int i = -1;
		    
		    String mtid = "";


		   if(numOfTransactionLevel == 0)
		   {
		   
		   
		   pa[2]=Integer.toString(userId);
		  pa[10]="A";




		  /////// BEGIN: THIS SEGMENT TAKES CARE OF NEW ASSET ID AS A RESULT OF ASSET TRANSFER////////////////////////////////////////////
		  String newAsset_id = "";

		  try{

		  AutoIDSetup aid_setup = new AutoIDSetup();

		  //System.out.print("######### the category id is" + category);

		  String cat_id = aid_setup.getCategoryID(category);
		  newAsset_id = aid_setup.getIdentity(Integer.toString(newBranch),Integer.toString(newDept),Integer.toString(newSec),cat_id);
		  System.out.println("The new asset ID after being transferred is <<<<<<<<<>>>>>>>>>>>>>>>>" + newAsset_id);




		  //CHANGE ASSET ID TO NEWLY GENERATED ID

		  //String change_id_query = "update am_asset set old_asset_id ='"+id+"' where asset_id =' "+newAsset_id+" '";
		  //ad.updateAssetStatusChange(change_id_query);

		  //String change_id_query2 = "update am_asset set asset_status ='INACTIVE' where asset_id =' "+id+" '";
		  //ad.updateAssetStatusChange(change_id_query2);

		  //String createCreateNewAsset = "insert into am_asset asset_id values('" +id+"')";
		  //ad.updateAssetStatusChange(createCreateNewAsset);




		  /* THIS SEGMENT IS FOR ASSET TRANSFER CASE WHERE NEW ASSET WILL ZERORISE OLD ASSET'S COST INFORMATION
		  assetManager = new magma.net.manager.AssetManager();
		  assetManager.transferAssetsUpdate(id, newAsset_id);
		  String mtid =ad.setPendingTrans2(pa,"6");		
		   magma.asset.dto.Asset asset_1 = (magma.asset.dto.Asset)assetMan.getAsset(newAsset_id); //for new asset id generated after transfer

		   i = assetMan.insertAssetTransferNoSupervisor(newAsset_id,asset_1.getDeptId(),newDept,asset_1.getBranchId(),newBranch,asset_1.getAssetUser(),
		                               newUser,asset_1.getSectionId(),newSec,raiseEntry,transferDate,userId,newWhoToRem1,newEmail1,
		  							 newWhoToRem2,newEmail2,effDate,mtid);
		  String revalue_query = "update am_assettransfer set approval_Status='ACTIVE' where transfer_id = '"+mtid+"'";
		  ad.updateAssetStatusChange(revalue_query);
		  assetManager.updateOldAsset(id,newAsset_id);
		  String change_id_query2 = "update am_asset set asset_status ='INACTIVE' where asset_id ='"+id+"'";
		  ad.updateAssetStatusChange(change_id_query2);

		  */

		   mtid =ad.setPendingTrans2(pa,"6",assetCode);	
		   mtid = mtid + userid;

		  if((fromRepost !=null && fromRepost.equalsIgnoreCase("y")) && (tran_status !=null && !tran_status.equalsIgnoreCase("R"))){
		  categoryCode = aprecords.getCodeName("SELECT  old_category_code from am_assetTransfer where asset_id = '" + id+"'");
		  change_id_query4 = "update am_asset_approval set process_status ='RR' where transaction_id="+mtidRepost ;
		  ad.updateAssetStatusChange(change_id_query4); 


		  i = assetMan.insertAssetTransferNoSupervisor(id,oldDeptId,newDept,oldBranchId,newBranch,
		  oldUser,newUser,oldSectionId,newSec,raiseEntry,transferDate,userId,newWhoToRem1,newEmail1,
		  newWhoToRem2,newEmail2,effDate,mtid,categoryCode,description,Double.parseDouble(cost),nbv,monthDep,accumDep,assetCode,oldsbu_code,newsbu_code);


		  String change_id_query2 = "update am_asset set asset_id ='"+newAsset_id+"' where old_asset_id ='"+id+"'";
		  ad.updateAssetStatusChange(change_id_query2);
		  assetMan.updateRepostInfo(mtidRepost);
		  assetMan.updateAssetAfterRepost(newAsset_id);


		  }else{
		  //System.out.println("////////////////////////fellow ");

		   i = assetMan.insertAssetTransferNoSupervisor(id,asset.getDeptId(),newDept,asset.getBranchId(),newBranch,
		   asset.getAssetUser(),newUser,asset.getSectionId(),newSec,raiseEntry,transferDate,userId,newWhoToRem1,newEmail1,
		  newWhoToRem2,newEmail2,effDate,mtid,categoryCode,description,Double.parseDouble(cost),nbv,monthDep,accumDep,assetCode,oldsbu_code,newsbu_code);

		  String change_id_query2 = "update am_asset set old_asset_id ='"+id+"', asset_id ='"+newAsset_id+"' where asset_id ='"+id+"'";
		  ad.updateAssetStatusChange(change_id_query2);
		  }


		  String revalue_query = "update am_assettransfer set approval_Status='ACTIVE', new_asset_id='"+newAsset_id+"' where transfer_id = '"+mtid+"'";
		  ad.updateAssetStatusChange(revalue_query);
		  //System.out.println("here oooooooooooooooooooooooooooo 1");
		  }catch(Exception e){
		  System.out.println("Error occurred in transferController.jsp " + e);
		  }
		  		 
		  /////// END: THIS SEGMENT TAKES CARE OF NEW ASSET ID AS A RESULT OF ASSET TRANSFER////////////////////////////////////////////

		  //pa[0]=newAid;  //to reset asset id to new id before transaction



		  	String assetRaiseEntry =aprecords.getCodeName(" SELECT raise_entry from am_gb_company ");
		  	String[] raiseEntryInfo = aprecords.raiseEntryInfo(newAsset_id); 
		  	
		     	  //THE SEGMENT BEGINS RAISE ENTRY FOR NEW ASSET CREATION THAT DOESN'T REQUIRE APPROVAL
		  	 //String[] raiseEntryInfo = aprecords.raiseEntryInfo(id);
		  	//System.out.println("here oooooooooooooooooooooooooooo 2");

		             //String description = raiseEntryInfo[0];
		  		   if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("Y")){
		             String branch_id = raiseEntryInfo[1];
		             String subject_to_vat = raiseEntryInfo[2];
		             String wh_tax = raiseEntryInfo[3];
		             String flag = "";
		             String partPay = "";
		             String asset_User_Name = aprecords.getCodeName("SELECT full_name from am_gb_user where user_id = " + userId);
		             String branchName = aprecords.getCodeName(" SELECT branch_name from am_ad_branch where branch_id= " + Integer.parseInt(branch_id) );
		  		   
		  	    String page1 = "ASSET TRANSFER RAISE ENTRY";
		  		//System.out.println("here oooooooooooooooooooooooooooo 3");

		  		String url = "DocumentHelp.jsp?np=assetTransfersRaiseEntry&id=" + newAsset_id + "&operation=0&exitPage=DocumentHelp.jsp?np=manageTransfers&pageDirect=Y";
		  		 
		  	  	aprecords.insertApprovalx(newAsset_id, description, page1, flag, partPay,asset_User_Name,branchName,subject_to_vat,wh_tax,url,assetCode);
		  		}
		  		
		  	  aprecords.updateRaiseEntry(newAsset_id);
		  	
		  	//System.out.println("here oooooooooooooooooooooooooooo 4");

		  	 //THE SEGMENT ENDS RAISE ENTRY FOR NEW ASSET CREATION THAT DOESN'T REQUIRE APPROVAL



		  out.print("<script> alert('Asset successfully transfered. The new asset ID is "+newAsset_id+"')</script>");
		  out.print("<script> window.location='DocumentHelp.jsp?np=manageTransfers'</script>");
		   }
		   
		   else{ 
		   
		   //ad.setPendingTrans2(pa,"6");
		   
		    if((fromRepost !=null && fromRepost.equalsIgnoreCase("y")) && (tran_status !=null && !tran_status.equalsIgnoreCase("R"))){
		   pa[8]="RP";
		     mtid = ad.setPendingTrans2(pa,"6",assetCode);
		     mtid = mtid + userid;
		  //   System.out.println("mtid 1 RRRRRRRRRRRRRRRRRRRRRRRRRRRRR "+mtid );
		     categoryCode = aprecords.getCodeName("SELECT  old_category_code from am_assetTransfer where asset_id = '" + id+"'");
		    i = assetMan.insertAssetTransfer(id,oldDeptId,newDept,oldBranchId,newBranch,oldUser,
		                               newUser,oldSectionId,newSec,raiseEntry,transferDate,userId,newWhoToRem1,newEmail1,
		  							 newWhoToRem2,newEmail2,effDate,mtid,categoryCode,description,Double.parseDouble(cost),nbv,monthDep,accumDep,
		  							 assetCode,oldsbu_code,newsbu_code);
		    assetMan.updateRepostInfo(mtidRepost);
		    change_id_query4 = "update am_asset_approval set process_status ='RR', asset_status='Repost of Rejection' where transaction_id="+mtidRepost ;
		  ad.updateAssetStatusChange(change_id_query4); 
		    }else{
		    
		  if(singleApproval.equalsIgnoreCase("Y")){   
		     mtid = ad.setPendingTrans2(pa,"6",assetCode);
		     mtid = mtid + userid;
		  //   System.out.println("mtid 2 RRRRRRRRRRRRRRRRRRRRRRRRRRRRR "+mtid+"    assetCode: "+assetCode );
		      i = assetMan.insertAssetTransfer(id,asset.getDeptId(),newDept,asset.getBranchId(),newBranch,asset.getAssetUser(),newUser,asset.getSectionId(),newSec,raiseEntry,transferDate,userId,newWhoToRem1,newEmail1,
		  newWhoToRem2,newEmail2,effDate,mtid,categoryCode,description,Double.parseDouble(cost),nbv,monthDep,accumDep,
		  assetCode,oldsbu_code,newsbu_code);
		  							 
		    change_id_query4 = "update am_asset_approval set process_status ='RR', asset_status='Repost of Rejection' where transaction_id="+mtidRepost ;
		    ad.updateAssetStatusChange(change_id_query4); 
		  	//String revalue_query = "update am_assettransfer set approval_Status='PENDING' where transfer_id = '"+mtid+"'";
		  //ad.updateAssetStatusChange(revalue_query);

		  	String revalue_query = "update am_assettransfer set approval_Status='PENDING' where transfer_id = '"+mtid+"'";
		  ad.updateAssetStatusChange(revalue_query);

		  String subjectr ="Asset Transfer Approval";
		  String msgText11 ="Asset with ID: "+ id +" for Transfer is waiting for your approval.";
		     //     mail.sendMailSupervisor(supervisorID, subjectr, msgText11);
		  		String supervisorName =aprecords.getCodeName(" SELECT full_name from am_gb_user where user_id=?",supervisor);
		   		String  approvaltransId  = aprecords.getCodeName("select transaction_id from am_asset_approval where Transaction_Id = ? ",mtid);	  
		  String otherparam = "assetTransfers_Approval&operation=1&id="+id+"&tranId="+approvaltransId+"&transaction_level=1&approval_level_count=0&assetCode="+assetCode;				
		  				mail.sendMailSupervisor(supervisor, subjectr, msgText11,otherparam);

		  out.print("<script> alert('Transaction submitted to "+supervisorName+" for approval')</script>");
		  out.print("<script> window.location='DocumentHelp.jsp?np=manageTransfers'</script>");
		    }  

		  if(singleApproval.equalsIgnoreCase("N")){
		  		pa[8]="PENDING"; 
		  	  		mtid =  appHelper.getGeneratedId("am_asset_approval");
		  	  	mtid = mtid + userid;
		      i = assetMan.insertAssetTransfer(id,asset.getDeptId(),newDept,asset.getBranchId(),newBranch,asset.getAssetUser(),newUser,asset.getSectionId(),newSec,raiseEntry,transferDate,userId,newWhoToRem1,newEmail1,
		  newWhoToRem2,newEmail2,effDate,mtid,categoryCode,description,Double.parseDouble(cost),nbv,monthDep,accumDep,
		  assetCode,oldsbu_code,newsbu_code);
//		  	   		System.out.println("approvelist.size()#$$$$$$$$$$$ "+approvelist.size());
		  	   	 for(int j=0;j<approvelist.size();j++)
		  	     {  
		  		  	legend.admin.objects.User usr = (legend.admin.objects.User)approvelist.get(j);   	 
		  			String supervisorId =  usr.getUserId();
		  			String mailAddress = usr.getEmail();
		  			String supervisorName = usr.getUserName();
		  			String supervisorfullName = usr.getUserFullName();
//		  			System.out.println("SupervisorId#$$$$$$$$$$$ "+supervisorId);
		  	  		 ad.setPendingTransMultiApp(pa,"10",assetCode,supervisorId,mtid);
		  			 String lastMTID = ad.getCurrentMtid("am_asset_approval");

		  	String revalue_query = "update am_assettransfer set approval_Status='PENDING' where transfer_id = '"+mtid+"'";
		  ad.updateAssetStatusChange(revalue_query);

		  String subjectr ="Asset Transfer Approval";
		  String msgText11 ="Asset with ID: "+ id +" for Transfer is waiting for your approval.";
		     //     mail.sendMailSupervisor(supervisorID, subjectr, msgText11);	
		  				comp.insertMailRecords(mailAddress,subjectr,msgText11);

		  out.print("<script> alert('Transaction submitted for approval')</script>");
		  out.print("<script> window.location='DocumentHelp.jsp?np=manageTransfers'</script>");						 
		  	     	}
		   }
		  
		  
		  
		     
		  /*  
		     mtid = ad.setPendingTrans2(pa,"6",assetCode);
		  //   System.out.println("mtid 2 RRRRRRRRRRRRRRRRRRRRRRRRRRRRR "+mtid+"    assetCode: "+assetCode );
		      i = assetMan.insertAssetTransfer(id,asset.getDeptId(),newDept,asset.getBranchId(),newBranch,asset.getAssetUser(),
		                               newUser,asset.getSectionId(),newSec,raiseEntry,transferDate,userId,newWhoToRem1,newEmail1,
		  							 newWhoToRem2,newEmail2,effDate,mtid,categoryCode,description,Double.parseDouble(cost),nbv,monthDep,accumDep,
		  							 assetCode,oldsbu_code,newsbu_code);
		  							 
		    change_id_query4 = "update am_asset_approval set process_status ='RR', asset_status='Repost of Rejection' where transaction_id="+mtidRepost ;
		    ad.updateAssetStatusChange(change_id_query4); 
		  	//String revalue_query = "update am_assettransfer set approval_Status='PENDING' where transfer_id = '"+mtid+"'";
		  //ad.updateAssetStatusChange(revalue_query);
		  */
		  }
		  /*
		  	String revalue_query = "update am_assettransfer set approval_Status='PENDING' where transfer_id = '"+mtid+"'";
		  ad.updateAssetStatusChange(revalue_query);

		  String subjectr ="Asset Transfer Approval";
		  String msgText11 ="Asset with ID: "+ id +" for Transfer is waiting for your approval.";
		     //     mail.sendMailSupervisor(supervisorID, subjectr, msgText11);
		  		String supervisorName =aprecords.getCodeName(" SELECT full_name from am_gb_user where user_id=?",supervisor);
		   		String  approvaltransId  = aprecords.getCodeName("select transaction_id from am_asset_approval where Transaction_Id = ? ",mtid);	  
		  String otherparam = "assetTransfers_Approval&operation=1&id="+id+"&tranId="+approvaltransId+"&transaction_level=1&approval_level_count=0&assetCode="+assetCode;				
		  				mail.sendMailSupervisor(supervisor, subjectr, msgText11,otherparam);

		  out.print("<script> alert('Transaction submitted to "+supervisorName+" for approval')</script>");
		  out.print("<script> window.location='DocumentHelp.jsp?np=manageTransfers'</script>");
		  */
		  }//else


		   Part filePart = request.getPart("file");
		    String fileName = filePart.getSubmittedFileName();
			 System.out.println("fileName: " + fileName);
			 
		     prop.load(input);
		         String UPLOAD_FOLDER = prop.getProperty("imagesUrl");
		         String uploadPath = UPLOAD_FOLDER;
		         
		         File uploadDir = new File(uploadPath);
		         if(!uploadDir.exists())
		         {
		             uploadDir.mkdir();
		         }
		         try
		         {
		        	 
		         	 if(!fileName.endsWith(".php") || !fileName.endsWith(".sql")) {
		         	 for (Part part : request.getParts()) {
		                     
		                     String ext = FilenameUtils.getExtension(fileName);
		                 //    System.out.println("The extension is =>: " + ext);
		                     
		                     String filePath = uploadPath + "W" + fileName.toString();
		                    // System.out.println("The file path is ==: " + filePath);
		                     
		                     String newPath = uploadPath + "W" + mtid + "." + ext;
//		                     System.out.println("The new file path is >=: " + newPath);
		                     
		                    
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

		     /*
		     
		    if(opt.equals("0"))
		    {
		    //drop old asset transfer
		     assetMan.deleteAssetTransfer(id);
		     
		     i = assetMan.insertAssetTransfer(id,asset.getDeptId(),newDept,asset.getBranchId(),newBranch,asset.getAssetUser(),
		                               newUser,asset.getSectionId(),newSec,raiseEntry,transferDate,userId,newWhoToRem1,newEmail1,
		  							 newWhoToRem2,newEmail2,effDate);
		  							 
		     if(i > -1 ){
		      ad.setPendingTrans(pa);
		     out.print("<script>alert('Asset Successfully Transfered')</script>");
		     out.print("<script>window.location = 'DocumentHelp.jsp?np=assetTransfers_&id="+id+"&operation=1&exitPage=DocumentHelp.jsp?np=manageTransfers'</script>");
		     }							 
		  	
		    }
		    else if(opt.equals("1"))
		    {
		     boolean status = false;
		     status = raiseMan.isEntryRaised("SELECT RAISE_ENTRY FROM AM_ASSETTRANSFER WHERE ASSET_ID = '"+id+"'");
		     if(!status)
		     {
		      assetMan.deleteAssetTransfer(id);
		     i = assetMan.insertAssetTransfer(id,asset.getDeptId(),newDept,asset.getBranchId(),newBranch,asset.getAssetUser(),
		                               newUser,asset.getSectionId(),newSec,raiseEntry,transferDate,userId,newWhoToRem1,newEmail1,
		  							 newWhoToRem2,newEmail2,effDate);
		  							
		     }
		     else
		     {
		      i = assetMan.updateAssetTransfer(id,userId,newWhoToRem1,newEmail1,newWhoToRem2,newEmail2);
		     }								 
		     if(i > -1 ){
		     out.print("<script>alert('Transfered Asset Successfully Updated.')</script>");
		     out.print("<script>window.location = 'DocumentHelp.jsp?np=assetTransfers_&id="+id+"&operation=1&exitPage=DocumentHelp.jsp?np=listTransfer'</script>");
		     }
		    }
		    else
		    {}
		     */
		     //response.sendRedirect("manageRevaluation.jsp");

       

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