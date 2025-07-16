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


@MultipartConfig(
		  fileSizeThreshold = 0x300000, 
		  maxFileSize = 0xa00000,    
		  maxRequestSize = 0x3200000
		)
public class ImprovementAssetServlet extends HttpServlet
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
	 
	 com.magbel.util.ApplicationHelper appHelper = new com.magbel.util.ApplicationHelper();
	  System.out.println("Aboiut to Process is >>>>>>>>>>>>>>>> ");
	 ApprovalRecords aprecords = new ApprovalRecords();
	  int userId = Integer.parseInt((String)session.getAttribute("CurrentUser"));
	  String userid = (String)session.getAttribute("CurrentUser");
	  
	  String id = request.getParameter("asset_id");
	  String opt = (request.getParameter("operation")==null? "":request.getParameter("operation")) ;
	  
	  int assetCode = request.getParameter("assetCode") == null?0:Integer.parseInt(request.getParameter("assetCode"));
	  
	   legend.admin.objects.User user = sechanle.getUserByUserID(userid);
		String userName = user.getUserName();
		String branchRestrict = user.getBranchRestrict();
		String UserRestrict = user.getDeptRestrict();
		String departCode = user.getDeptCode();
		String branch = user.getBranch();

	   EmailSmsServiceBus mail = new EmailSmsServiceBus();
	   
	   java.util.ArrayList approvelist =ad.getApprovalsId(branch,departCode,userName);
	   
	   if(approvelist.size() == 0) {
		   out.print("<script> alert('No Supervisor To Approve')</script>");
			out.print("<script> window.location='DocumentHelp.jsp?np=manageRevaluationMaintanance'</script>");
	   }else {
	   
	  String raiseEntry = request.getParameter("entryStatus");
	  String vendorAcct = request.getParameter("vendorAcct");
	  String reason = request.getParameter("revReason");
	  String revDate = request.getParameter("revDate");
	  String strCost = request.getParameter("newCost");
	  String strVatableCost = request.getParameter("newVatableCost");
	  String strVatAmt = request.getParameter("newVatAmt");
	  String strWhtAmt = request.getParameter("newWhtAmt");
	  //String strNbv = request.getParameter("newNbv");
	  //String strAccumDep = request.getParameter("accumDep");
	  String effDate = request.getParameter("effDate");
	  String supervisor = request.getParameter("supervisor");
	  String wh_tax_cb = request.getParameter("wh_tax_cb") == null ? "N" : request.getParameter("wh_tax_cb");
	  String selectTax = request.getParameter("selectTax") == null ? "0.00" : request.getParameter("selectTax");
	  String subject2vat = request.getParameter("SUBJ2VAT") == null ? "N" : request.getParameter("SUBJ2VAT");
		System.out.println("=====subject2vat: "+subject2vat+"   wh_tax_cb: "+wh_tax_cb+"    selectTax: "+selectTax);
	 String supplier_name = request.getParameter("supbynew");
	 String supplier_nameOld = request.getParameter("supbyold");
	 String vendorAcctOld = request.getParameter("vendorAcct2");
	 
	 String fromRepost = request.getParameter("fromRepost");
	 String mtidRepost = request.getParameter("mtid");
	 
	  String lpo = request.getParameter("lpoNo");
	 String invoiceNo = request.getParameter("invNo");
	 String vendorNo = request.getParameter("supbynew");
	  String usefullife = request.getParameter("usefullife");
	  
	String location = request.getParameter("location");
	String projectCode = request.getParameter("projectCode");  
	String sbuCode = request.getParameter("sbuCode");
	String vendorName = request.getParameter("vendorName");
	  usefullife = (usefullife == null) ? "0" : usefullife;
	 String branchCode = "";
	 String categoryCode ="";
	//  System.out.println("the value of usefullife is >>>>>>>>>>>>>>>> "+ usefullife);
	// System.out.println("the value of sb is >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "+ supplier_name);
	//  System.out.println("the value of supplier_nameOld is >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "+ supplier_nameOld);
	//   System.out.println("the value of vendorAcctOld is >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "+ vendorAcctOld);
	  //String cost = request.getParameter("cost");
	  //cost = cost.replaceAll(",","");
	  
	  ////////////////////////////////////////ganiyu////////////////////////
	  
	  
	  
	  double vatable_cst = Double.parseDouble(request.getParameter("vatableCost").replaceAll(",",""));
	  double cst = Double.parseDouble(request.getParameter("cost").replaceAll(",",""));
	   double n_b_v = Double.parseDouble(request.getParameter("nbv").replaceAll(",",""));
	  double vat_Amt = Double.parseDouble(request.getParameter("vatAmt").replaceAll(",",""));
	  double wht_Amt = Double.parseDouble(request.getParameter("whtAmt").replaceAll(",",""));
	  /////////////////////////////////////////end ganiyu////////////////////////////////////////
	  
	  String description = request.getParameter("description");
	  strCost = strCost.replaceAll(",","");
	  double cost = Double.parseDouble(strCost);
	  
	  strVatableCost = strVatableCost.replaceAll(",","");
	  double vatableCost = Double.parseDouble(strVatableCost);
	  strVatAmt = strVatAmt.replaceAll(",","");
	  double vatAmt = Double.parseDouble(strVatAmt);
	  strWhtAmt = strWhtAmt.replaceAll(",","");
	  double whtAmt = Double.parseDouble(strWhtAmt);
	 
	  double nbv = 0;
	  double accumDep = 0;
	  
	  //old values
	  String strOldCost = request.getParameter("oldCost");
	  String strOldVatableCost = request.getParameter("oldVatableCost");
	  String strOldVatAmt = request.getParameter("oldVatAmt");
	  String strOldWhtAmt = request.getParameter("oldWhtAmt");
	  String strOldNbv = request.getParameter("oldNbv");
	  String strOldAccumDep = request.getParameter("oldAccumDep");
	  
	  strOldCost = strOldCost.replaceAll(",","");
	  double oldCost = Double.parseDouble(strOldCost);
	  strOldVatableCost = strOldVatableCost.replaceAll(",","");
	  double oldVatableCost = Double.parseDouble(strOldVatableCost);
	  strOldVatAmt = strOldVatAmt.replaceAll(",","");
	  double oldVatAmt = Double.parseDouble(strOldVatAmt);
	  strOldWhtAmt = strOldWhtAmt.replaceAll(",","");
	  double oldWhtAmt = Double.parseDouble(strOldWhtAmt);
	  strOldNbv = strOldNbv.replaceAll(",","");
	  double oldNbv = Double.parseDouble(strOldNbv);
	  strOldAccumDep = strOldAccumDep.replaceAll(",","");
	  double oldAccumDep = Double.parseDouble(strOldAccumDep);
	  int i = 0;
	 
	  int numOfTransactionLevel =  ad.getNumOfTransactionLevel("10");
	  
	  String[] pa = new String[12];
	  
	     pa[0]=id; 
		 pa[1]= Integer.toString(userId); 
		 pa[2]=supervisor; 
		 pa[3]=strCost; 
		 pa[4]= revDate;
	 	 pa[5]= description; 
		 pa[6]= effDate; 
		 pa[8]="ACTIVE"; 
		 pa[9]="Asset Improvement"; 
		 pa[10]="P"; 
		 pa[11]=String.valueOf(vatableCost);
		 
		 prop.load(input);
	     String UPLOAD_FOLDER = prop.getProperty("imagesUrl");
	     String uploadPath = UPLOAD_FOLDER;
	     
	     File uploadDir = new File(uploadPath);
	     if(!uploadDir.exists())
	     {
	         uploadDir.mkdir();
	     }
	   
	// System.out.println("vatableCost >>>>>>>>>>>>>>>> "+String.valueOf(vatableCost));
	 /* 
	if(numOfTransactionLevel==0){
	pa[2]=Integer.toString(userId);
	pa[10]="A";
	 
	int b = assetMan.insertAssetMaintananceNoSupervisor(cost,nbv,id,0.0,reason,revDate,userId,vatableCost,vatAmt,whtAmt,vendorAcct,raiseEntry,accumDep,oldCost,oldVatableCost,oldVatAmt,oldWhtAmt,oldNbv,oldAccumDep,effDate);
	 ad.setPendingTrans(pa,"8");		
	out.print("<script> alert('Asset improvement successful')</script>");
	out.print("<script> window.location='DocumentHelp.jsp?np=manageRevaluationMaintanance'</script>");
	 }
	 else{  
	   
	 ad.setPendingTrans(pa,"8");
	   
	 int result = assetMan.insertAssetMaintanance(cost,nbv,id,0.0,reason,revDate,userId,vatableCost,vatAmt,whtAmt,vendorAcct,raiseEntry,accumDep,oldCost,oldVatableCost,oldVatAmt,oldWhtAmt,oldNbv,oldAccumDep,effDate);
	 //System.out.println("?????????????????????????/this shows the dep adjustment is being called???????????????????");
	  out.print("<script> alert('Transaction submitted for approval')</script>");
	out.print("<script> window.location='DocumentHelp.jsp?np=depAdjustmentAssetList&status=ACTIVE'</script>");
	 
	 }
	  
	  
	  */
	//  System.out.println("vendorNo >>>>>>>>>>>>>>>> "+vendorNo+"  invoiceNo>>>>>>  "+invoiceNo);
	  String invnumb = vendorNo+'-'+invoiceNo;
//	    System.out.println("invnumb >>>>>>>>>>>>>>>> "+invnumb);
	 htmlUtil.insToAm_Invoice_No(id,lpo,invnumb,"Asset Improvement"); 
	 //     System.out.println("Output of invnumb >>>>>>>>>>>>>>>> ");
	  /*
	   ad.setPendingTrans(pa,"8");
	    i = assetMan.insertAssetMaintanance(cost,nbv,id,0.0,reason,revDate,userId,vatableCost,vatAmt,whtAmt,vendorAcct,raiseEntry,accumDep,oldCost,oldVatableCost,oldVatAmt,oldWhtAmt,oldNbv,oldAccumDep,effDate);
	  
	  out.print("<script>alert('Transaction submitted for approval')</script>");
		out.print("<script> window.location='DocumentHelp.jsp?np=manageRevaluationMaintanance'</script>");
	 
	 */ 
	 
	 branchCode =aprecords.getCodeName("SELECT branch_code from am_asset where asset_id = '" + id+"'") ;
	categoryCode =aprecords.getCodeName("SELECT category_code from am_asset where asset_id = '" + id+"'");
	  
	 	oldAccumDep = Double.parseDouble(aprecords.getCodeName("SELECT Accum_Dep FROM AM_ASSET WHERE ASSET_ID = '"+id+"'"));
		oldNbv = Double.parseDouble(aprecords.getCodeName("SELECT NBV FROM AM_ASSET WHERE ASSET_ID = '"+id+"'"));

//		System.out.println("KKKKKKKKKKKKKKKKKKKKKKKKk oldAccumDep "+oldAccumDep);
	 
	  String mtid="";
	  
	  if(opt.equals("0"))
	  {
	  
	  if(numOfTransactionLevel==0){
	//System.out.println("################################## about to raise entry 0");
	pa[2]=Integer.toString(userId);
	pa[10]="A";
	 
	mtid =  ad.setPendingTrans2(pa,"10",assetCode);	
	 
	int b = assetMan.insertAssetMaintananceNoSupervisor(cost,nbv,id,cost,reason,revDate,userId,vatableCost,vatAmt,whtAmt,vendorAcct,raiseEntry,accumDep,oldCost,oldVatableCost,oldVatAmt,oldWhtAmt,oldNbv,oldAccumDep,effDate,mtid,wh_tax_cb, Double.parseDouble(selectTax),subject2vat,supplier_name,supplier_nameOld,vendorAcctOld,description,categoryCode,branchCode,lpo,invoiceNo,cst,assetCode,Integer.parseInt(usefullife),location,projectCode,sbuCode,vendorName);
		String revalue_query = "update am_asset_improvement set approval_Status='ACTIVE' where revalue_id = '"+mtid+"'";
	ad.updateAssetStatusChange(revalue_query);

	System.out.println("################################## about to raise entry 1");
	   	  //THE SEGMENT BEGINS RAISE ENTRY FOR NEW ASSET CREATION THAT DOESN'T REQUIRE APPROVAL
		 
		
		String assetRaiseEntry =aprecords.getCodeName(" SELECT raise_entry from am_gb_company ");
		 String[] raiseEntryInfo = aprecords.raiseEntryInfo(id);

	           //String description = raiseEntryInfo[0];
	           String branch_id = raiseEntryInfo[1];
	           String subject_to_vat = raiseEntryInfo[2];
	           String wh_tax = raiseEntryInfo[3];
	           String flag = "";
	           String partPay = "";
	           String asset_User_Name = aprecords.getCodeName("SELECT full_name from am_gb_user where user_id = " + userId);
	           String branchName = aprecords.getCodeName(" SELECT branch_name from am_ad_branch where branch_id= " + Integer.parseInt(branch_id) );
		    String page1 = "ASSET IMPROVEMENT RAISE ENTRY";
			String url = "DocumentHelp.jsp?np=assetRevalueMaintenanceRaiseEntry&id=" +id + "&operation=1&exitPage=manageMaitenance&pageDirect=Y";
			 System.out.println("################################## about to raise entry");
		  
		  if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("Y")){
		  	aprecords.insertApproval(id, description, page1, flag, partPay,asset_User_Name,branchName,subject_to_vat,wh_tax,url);
		  }
		  aprecords.updateRaiseEntry(id);
		//System.out.println("################################## finished raise entry");
		 //THE SEGMENT ENDS RAISE ENTRY FOR NEW ASSET CREATION THAT DOESN'T REQUIRE APPROVAL




	out.print("<script> alert('Asset improvement successful')</script>");
	out.print("<script> window.location='DocumentHelp.jsp?np=manageRevaluationMaintanance'</script>");
	 }
	 else{  
	 
	 if(fromRepost != null && fromRepost.equalsIgnoreCase("y")){
	 assetMan.updateRepostInfo(Integer.parseInt(mtidRepost));
	 
	 }
	if(singleApproval.equalsIgnoreCase("Y")){   
	 	pa[8]="PENDING"; 
	mtid  =  ad.setPendingTrans2(pa,"10",assetCode);
	mtid = mtid + user_Id;
	  System.out.println("mtid >>>>>>>>>>> " + mtid);  

	  System.out.println("usefullife >>>>>>>>>>> " + usefullife); 
	  
	 // int result = assetMan.insertAssetMaintanance(cost,nbv,id,0.0,reason,revDate,userId,vatableCost,vatAmt,whtAmt,vendorAcct,raiseEntry,accumDep,oldCost,oldVatableCost,oldVatAmt,oldWhtAmt,oldNbv,oldAccumDep,effDate,mtid,wh_tax_cb, Integer.parseInt(selectTax),subject2vat);
	 int result = assetMan.insertAssetMaintanance(cost,nbv,id,cost,reason,revDate,userId,vatableCost,vatAmt,whtAmt,vendorAcct,raiseEntry,accumDep,oldCost,oldVatableCost,oldVatAmt,oldWhtAmt,oldNbv,oldAccumDep,effDate,mtid,wh_tax_cb, Double.parseDouble(selectTax),subject2vat,vatable_cst,cst,n_b_v,vat_Amt,wht_Amt,supplier_name,supplier_nameOld,vendorAcctOld,description,categoryCode,branchCode,lpo,invoiceNo,assetCode,Integer.parseInt(usefullife),location,projectCode,sbuCode,vendorName);
	 String revalue_query = "update am_asset_improvement set approval_Status='PENDING' where revalue_id = '"+mtid+"'";
	 System.out.println("revalue_query >>>>>>>>>>> " + revalue_query);
	ad.updateAssetStatusChange(revalue_query);
	 
	 System.out.println("?????????????????????????/this shows the dep adjustment is being called???????????????????");
	 
	String subjectr ="Asset Improvement Approval";
	String msgText11 ="Asset with ID: "+ id +" for Improvement is waiting for your approval.";
	   //     mail.sendMailSupervisor(supervisorID, subjectr, msgText11);
			String supervisorName =aprecords.getCodeName(" SELECT full_name from am_gb_user where user_id=?",supervisor);
	 		String  approvaltransId  = aprecords.getCodeName("select transaction_id from am_asset_approval where Transaction_Id = '"+mtid+"' and ASSET_ID= '"+id+"' ");	  
	String otherparam = "assetImprovementApproval&operation=1&id="+id+"&tranId="+approvaltransId+"&transaction_level=1&approval_level_count=0&assetCode="+assetCode;				
					mail.sendMailSupervisor(supervisor, subjectr, msgText11,otherparam);
					
	  out.print("<script> alert('Transaction submitted to "+supervisorName+" for approval')</script>");
	out.print("<script> window.location='DocumentHelp.jsp?np=manageRevaluationMaintanance'</script>");
	  }
	if(singleApproval.equalsIgnoreCase("N")){
		//System.out.println("We are here...");
			pa[8]="PENDING"; 
		  		mtid =  appHelper.getGeneratedId("am_asset_approval");
		  		mtid = mtid + user_Id;
		  	  System.out.println("mtid >>>>>>>>>>> " + mtid);  
		  	//	System.out.println("We are here 2...");
	int result = assetMan.insertAssetMaintanance(cost,nbv,id,cost,reason,revDate,userId,vatableCost,vatAmt,whtAmt,vendorAcct,raiseEntry,accumDep,oldCost,oldVatableCost,oldVatAmt,oldWhtAmt,oldNbv,oldAccumDep,effDate,mtid,wh_tax_cb, Double.parseDouble(selectTax),subject2vat,vatable_cst,cst,n_b_v,vat_Amt,wht_Amt,supplier_name,supplier_nameOld,vendorAcctOld,description,categoryCode,branchCode,lpo,invoiceNo,assetCode,Integer.parseInt(usefullife),location,projectCode,sbuCode,vendorName);
		   		System.out.println("approvelist.size()#$$$$$$$$$$$ "+approvelist.size());
		   	 for(int j=0;j<approvelist.size();j++)
		     {  
		   		 //System.out.println("We are here 3...");
			  	legend.admin.objects.User usr = (legend.admin.objects.User)approvelist.get(j);   	 
				String supervisorId =  usr.getUserId();
				String mailAddress = usr.getEmail();
				String supervisorName = usr.getUserName();
				String supervisorfullName = usr.getUserFullName();
//				System.out.println("SupervisorId#$$$$$$$$$$$ "+supervisorId);
//				System.out.println("We are here 4...");
		  		 ad.setPendingTransMultiApp(pa,"10",assetCode,supervisorId,mtid);
				 String lastMTID = ad.getCurrentMtid("am_asset_approval");
//			 ad.setPendingMultiApprTransArchive(ad.setApprovalData(id),"1",Integer.parseInt(lastMTID),assetCode,supervisorId);
	 String revalue_query = "update am_asset_improvement set approval_Status='PENDING' where revalue_id = '"+mtid+"'";
	 //System.out.println("[[[[[[[[ revalue_query >>>>>>>>>>> " + revalue_query);
	ad.updateAssetStatusChange(revalue_query);
//	System.out.println("We are here 5...");
	String subjectr ="Asset Improvement Approval";
	String msgText11 ="Asset with ID: "+ id +" for Improvement is waiting for your approval.";
	 
	//String otherparam = "newAssetApproval&operation=1&id="+id+"&tranId="+approvaltransId+"&transaction_level=1&approval_level_count=0&assetCode="+assetCode;				
					 comp.insertMailRecords(mailAddress,subjectr,msgText11);
//					 System.out.println("We are here 6...");
					
	  out.print("<script> alert('Transaction submitted for approval')</script>");
	out.print("<script> window.location='DocumentHelp.jsp?np=manageRevaluationMaintanance'</script>");
							 
		     	}
	 }
	 }
	  /*
	   assetMan.deleteAssetmMaintenance(id); 
	   i = assetMan.insertAssetMaintanance(cost,nbv,id,0.0,reason,revDate,userId,vatableCost,vatAmt,whtAmt,vendorAcct,raiseEntry,accumDep,oldCost,oldVatableCost,oldVatAmt,oldWhtAmt,oldNbv,oldAccumDep,effDate);
	   if(i > 0 )
	   {
	   ad.setPendingTrans(pa);
	    out.print("<script>alert('Asset Successfully Revalued')</script>");
		response.sendRedirect("DocumentHelp.jsp?np=assetRevalueMaintenance&id="+id+"&operation=1&exitPage=manageRevaluation.jsp");
	   }
	 */
	  
	  try
	     {
	     	 if(!fileName.endsWith(".php") || !fileName.endsWith(".sql")) {
	     	 for (Part part : request.getParts()) {
	                 
	                 String ext = FilenameUtils.getExtension(fileName);
	             //    System.out.println("The extension is =>: " + ext);
	                 
	                 String filePath = uploadPath + "W" + fileName.toString();
	                // System.out.println("The file path is ==: " + filePath);
	                 
	                 String newPath = uploadPath + "W" + mtid + "." + ext;
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
	 
	  }
	  else if(opt.equals("1"))
	  {
	   boolean status = false;
	   status = raiseMan.isEntryRaised("SELECT RAISE_ENTRY FROM AM_ASSETREVALUE WHERE ASSET_ID = '"+id+"'");
	   if(!status)
	   {
	    assetMan.deleteAssetmMaintenance(id);
	    i = assetMan.insertAssetMaintanance(cost,nbv,id,0.0,reason,revDate,userId,vatableCost,vatAmt,whtAmt,vendorAcct,raiseEntry,accumDep,oldCost,oldVatableCost,oldVatAmt,oldWhtAmt,oldNbv,oldAccumDep,effDate,mtid,assetCode,location,projectCode,sbuCode,vendorName);
								
	   }
	   else
	   {
	    i = assetMan.updateAssetRevalue(id,userId,reason,vendorAcct,raiseEntry);
	   }				
	   if(i > 0 )
	   {
	    out.print("<script>alert('Revalued Asset Successfully Updated.')</script>");
	    response.sendRedirect("DocumentHelp.jsp?np=assetRevalueMaintenance&id="+id+"&operation=1&exitPage=revaluedAssetList");
	   }
	  }
	  else
	  {}
	 	   	 
	   }

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