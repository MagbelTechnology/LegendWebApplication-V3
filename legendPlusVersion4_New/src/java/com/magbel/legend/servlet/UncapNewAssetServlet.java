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
public class UncapNewAssetServlet extends HttpServlet
{
	private EmailSmsServiceBus mail ;
	
	private ApprovalRecords records;
	 legend.admin.handlers.SecurityHandler sechanle = new legend.admin.handlers.SecurityHandler();
	 com.magbel.util.HtmlUtility htmlUtil  = new com.magbel.util.HtmlUtility();
	 com.magbel.legend.bus.ApprovalRecords aprecords  = new com.magbel.legend.bus.ApprovalRecords();
	 com.magbel.util.ApplicationHelper appHelper  = new com.magbel.util.ApplicationHelper();
	 legend.admin.handlers.CompanyHandler comp  = new legend.admin.handlers.CompanyHandler();
   public void doPost(HttpServletRequest request, 
    HttpServletResponse response)
      throws ServletException, IOException
     
   {
	   try {
	 String userClass = (String) request.getSession().getAttribute("UserClass");
	 String user_Id =(String) request.getSession().getAttribute("CurrentUser");
	// System.out.println("<<<<<<user_Id: "+user_Id);
	 PrintWriter out = response.getWriter();
	
	 HttpSession session = request.getSession();
	 Properties prop = new Properties();
	 File file = new File("C:\\Property\\LegendPlus.properties");
	 FileInputStream input = new FileInputStream(file);
	 prop.load(input);

	 String ThirdPartyLabel = prop.getProperty("ThirdPartyLabel");
	 String singleApproval = prop.getProperty("singleApproval");
	 String VATRequired = prop.getProperty("VATREQUIRED");
	 String sbuRequired = prop.getProperty("SBUREQUIRED");

	 System.out.println("ThirdPartyLabel: " + ThirdPartyLabel);
	 
	// System.out.println("\n\n ============== the systemIp for current user is ============ ");
	
	AssetRecordsBean ad = new AssetRecordsBean();
	 magma.ApprovalBean apbean = new magma.ApprovalBean();
	 Part filePart = request.getPart("file");
	 
	 String fileName = filePart.getSubmittedFileName();
	 System.out.println("fileName: " + fileName);
 	
	 String vatCostLabel = "";
	 if(VATRequired.equals("N")){vatCostLabel = "Asset Cost";}
	 if(VATRequired.equals("Y")){vatCostLabel = "Vatable Cost";}
	 //System.out.println("\n\n ============== the systemIp for current user is ============ ");
	 String categoryId = request.getParameter("categoryId");
	 if(categoryId==null){categoryId = "0";}
	 String subjecttovat = aprecords.getCodeName("select VAT_SELECTION from am_gb_company");
	 String thirdpartyrqd = aprecords.getCodeName("select THIRDPARTY_REQUIRE from am_gb_company");
	 //String subjecttovat = "";
	 String root = "new";
	 String userid = (String)session.getAttribute("CurrentUser");
	 String systemIp = request.getRemoteAddr();
	 //System.out.println("\n\n ============== the systemIp for current user is ============ " + systemIp);
	 legend.admin.objects.User user = sechanle.getUserByUserID(userid);
	 String userName = user.getUserName();
	 String branchRestrict = user.getBranchRestrict();
	 String UserRestrict = user.getDeptRestrict();
	 String departCode = user.getDeptCode();
	 String branch = user.getBranch();
	 String tran_status ="A";
	 String currentPage = "DocumentHelp.jsp?np=updateAssetViewBranch";	 
	 String tran_type = "Asset Update"; 
	 String dept = aprecords.getCodeName("select dept_Id from am_ad_department where dept_code = ? ",departCode);
	  java.util.ArrayList approvelist =ad.getApprovalsId(branch,departCode,userName);
	 String makeExist = aprecords.getCodeName("SELECT COUNT(*) FROM am_ad_category WHERE CATEGORY_ID IN (SELECT CATEGORY_ID FROM am_gb_assetMake WHERE STATUS = 'ACTIVE') AND CATEGORY_ID = ? ",categoryId);	

	 com.magbel.util.CurrencyNumberformat formata = new com.magbel.util.CurrencyNumberformat();

	 String enforceBarcode = aprecords.getCodeName("select enforceBarcode from am_ad_category where CATEGORY_ID = ?",categoryId);
	 //System.out.print("<<<<<enforceBarcode: "+enforceBarcode+"    <<<<<categoryId: "+categoryId);

	 String branchrestricted = "";  
	 if(userid != null){
	    branchrestricted = sechanle.getBranchRestrictedUser(userid);	
	 }
	   //To get information if approval is required for a transaction
	   
	 int numOfTransactionLevel =  ad.getNumOfTransactionLevel("18");

	 String limitCode=apbean.getApprovalLimit(userid);
	 apbean.getApprovalDetail(limitCode);
	 double min_approval = apbean.getMini_approval();
	 double max_approval = apbean.getMax_approval();
	 String vatRate ="0";
	 String taxRate ="0";
	 String category = request.getParameter("categoryId");
	 String isNew =  request.getParameter("s");
	 String branch_id = request.getParameter("branch_id2"); //ganiyu
	 String supervisorID = request.getParameter("supervisor");
	 EmailSmsServiceBus mail = new EmailSmsServiceBus();
	 String lpo = request.getParameter("lpo");
	 String invoiceNo = request.getParameter("invNo");
	 String vendorNo = request.getParameter("sb");
	 String ID = request.getParameter("ID");
	 
	 String asset_id = request.getParameter("asset_id");
	    String legacyId = request.getParameter("integrifyId");
	    String category_id = request.getParameter("category_id");
	    String date_of_purchase = request.getParameter("date_of_purchase");
	    String sub_category_id = request.getParameter("sub_category_id");
	    String registrationNo = request.getParameter("registration_no");
	    String sbu_code = request.getParameter("sbu_code");
	    String cost_price = request.getParameter("cost_price");
	    String vat_amount =  request.getParameter("vat_amount");
	    String vatable_cost = request.getParameter("vatable_cost");
	    String wh_tax_amount = request.getParameter("wh_tax_amount");
	    String residual_value = request.getParameter("residual_value");
	    String amountPTD = request.getParameter("amountPTD");
	    String department_id = request.getParameter("department_id");
	    String descriptions = request.getParameter("description");
	    System.out.println("descriptions >>>>>>>>>>>>> " + descriptions);
	    String memo = request.getParameter("memo");
	    String region = request.getParameter("regionCode");
	    String make = request.getParameter("make");
	    String location = request.getParameter("location");
	    String serial_number = request.getParameter("serial_number");
	    
	   // String section_id = request.getParameter("section_id");
	    String fullyPAID = request.getParameter("fullyPAID");
	    System.out.println("fullyPAID >>>>>>>>>>>>> " + fullyPAID);
	    String state = request.getParameter("state");
	    String driver = request.getParameter("driver");
	    String bar_code = request.getParameter("bar_code");
	   String vendor_acc = request.getParameter("vendor_accountOld");
	   String vendor_name =  request.getParameter("vendorName");
	   String maintained_by = request.getParameter("maintained_by");
	   String purchaseReason = request.getParameter("reason");
	   String assetUser = request.getParameter("user");
	    String authorized_by = request.getParameter("authorized_by");
	    String subject_to_vatOld = request.getParameter("subject_to_vatOLD");
	    String wh_tax_cb= request.getParameter("wh_tax_cb");
	    String transport_cost = request.getParameter("transport_cost");
	    String other_cost = request.getParameter("other_cost");
	    String depreciation_rate = request.getParameter("depreciation_rate");
	    String accum_dep = request.getParameter("accum_dep");
	    String nbv = request.getParameter("nbv");
	    String supplied_by = request.getParameter("sb");
	    String who_to_rem1 = request.getParameter("who_to_remind");
	    String invoiceNum = request.getParameter("invNo");
	    String e_mail1 = request.getParameter("email_1");
	    String who_to_rem2 = request.getParameter("who_to_rem2");
	    String posting_date = request.getParameter("posting_date");
	    String email2 = request.getParameter("email2");
	    String spare_1 = request.getParameter("spare_1");
	    String spare_2 = request.getParameter("spare_2");
	    String spare_3 = request.getParameter("spare_3");
	    String spare_4 = request.getParameter("spare_4");
	    String spare_5 = request.getParameter("spare_5");
	    String spare_6 = request.getParameter("spare_6");
	    String warrantyStartDate = request.getParameter("warrantyStartDate");
	    String noOfMonths = request.getParameter("noOfMonths");
	    String expiryDate = request.getParameter("expiryDate");
	    String projectCode = request.getParameter("projectCode");
	    String require_depreciation = request.getParameter("require_depreciation");
	    String require_redistribution = request.getParameter("require_distribution");
	    int assetCode = request.getParameter("assetCode") == null?0:Integer.parseInt(request.getParameter("assetCode"));
	    String engine_number = request.getParameter("engine_number");
	    String model = request.getParameter("model");
	    
		ad.setAsset_id(asset_id);
	   	ad.setIntegrifyId(legacyId);
	   	ad.setCategory_id(category_id);
	    ad.setDepartment_id(department_id);
	    ad.setDate_of_purchase(date_of_purchase);
	    ad.setSub_category_id(sub_category_id);
	    ad.setSbu_code(sbu_code);
	    ad.setBranch_id(branch_id);
	    ad.setCategory_id(category);
	    ad.setCost_price(cost_price);
	    ad.setVat_amount(vat_amount);
	    ad.setVatable_cost(vatable_cost);
	    ad.setWh_tax_amount(wh_tax_amount);
	    ad.setResidual_value(residual_value);
	    ad.setAmountPTD(amountPTD);
	    ad.setDescription(descriptions);
	    ad.setRegionCode(region);
	    ad.setMake(make);
	    ad.setLocation(location);
	    ad.setSerial_number(serial_number);
	  //  ad.setSection_id(section_id);
	    ad.setState(state);
	    ad.setDriver(driver);
	    ad.setBar_code(bar_code);
	    ad.setVendor_account(vendor_acc);
	    ad.setVendorName(vendor_name);
	    ad.setMaintained_by(maintained_by);
	    ad.setReason(purchaseReason);
	    ad.setUser(assetUser);
	    ad.setSupplied_by(vendorNo);
	    ad.setSbu_code(sbu_code);
	    ad.setLpo(lpo);
	  //  ad.setUser_id(user_Id);
	    ad.setAuthorized_by(authorized_by);
	   // ad.setUser_id(user_Id);
	    ad.setSubject_to_vat(subject_to_vatOld);
	    ad.setWh_tax_cb(wh_tax_cb);
	    ad.setTransport_cost(transport_cost);
	    ad.setOther_cost(other_cost);
	    ad.setDepreciation_rate(depreciation_rate);
	    ad.setAccum_dep(accum_dep);
	    ad.setNbv(nbv);
	    ad.setSupplied_by(supplied_by);
	    ad.setMemo(memo);
	    ad.setEngine_number(engine_number);
	    ad.setEmail_1(e_mail1);
	    ad.setModel(model);
	    ad.setPosting_date(posting_date);
	    ad.setEmail2(email2);
	    ad.setSpare_1(spare_1);
	    ad.setSpare_2(spare_2);
	    ad.setSpare_3(spare_3);
	    ad.setSpare_4(spare_4);
	    ad.setSpare_5(spare_5);
	    ad.setSpare_6(spare_6);
	    ad.setWarrantyStartDate(warrantyStartDate);
	    ad.setNoOfMonths(noOfMonths);
	    ad.setExpiryDate(expiryDate);
	    ad.setProjectCode(projectCode);
	    ad.setRequire_depreciation(require_depreciation);
	    ad.setRequire_redistribution(require_redistribution);
	    ad.setUser_id(user_Id);
	    ad.setFullyPAID(fullyPAID);
	    

	 //System.out.print("supervisorID== "+supervisorID+"       barCode: "+barCode);
	 //String branch_sbu = ad.getBranch_id();
	 //String branch_sbu = "1";//ad.getBranch_id();
	 //System.out.println("================================"+branch_sbu+"===========================================");
	 if(isNew!=null && isNew.equalsIgnoreCase("n")) session.setAttribute("newAsset",null);
	 if(session.getAttribute("newAsset")!=null)
	   {
	   	ad=(magma.AssetRecordsBean)session.getAttribute("newAsset");
	 	category = ad.getCategory_id();
	   branch_id = ad.getBranch_id(); //ganiyu
	   }
	 //System.out.print("<<<<<getVatRate: "+ad.getVatRate());
	 SytemsManager sm = new SytemsManager();
	   if(request.getParameter("saveBtn") != null){
	   String multiple = request.getParameter("multiple");
	   String section = request.getParameter("section_id");
	   ad.setMultiple(multiple);
	   ad.setSection_id(section);
	   String memoV = request.getParameter("memo");
	   ad.setMemo(memoV);
	    session.setAttribute("newAsset", ad);

	     int statux = ad.insertAssetRecordUnclassified(branch);
//	 	  System.out.println("statux >>>>>>>>>>>>> " + statux);
//	 	String invoiceNumber = vendorNo+'-'+invoiceNo;
	 	String invnumb = vendorNo+'-'+invoiceNo;
//	 	System.out.println("invoice Number in JSP >>>>>>>>>>>>> " + invnumb);
	 	htmlUtil.insToAm_Invoice_No(ad.getAsset_id(),lpo,invnumb,"Asset Creation");	   
	 	 if(statux == 0 )
	 	 {
//	 	  	String q3_rfid = "update ST_INVENTORY_RFID set SCANN_STATUS='P' where RFID_TAG = ?";
//	 		ad.updateAssetStatusChange(q3_rfid,ad.getBar_code());
	 		 
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
	                     
	                     String newPath = uploadPath + "W" + ad.getAssetCode() + "." + ext;
//	                     System.out.println("The new file path is >=: " + newPath);
	                     
	                    
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
	 	 
	 	  String id = ad.getAsset_id();
	 	  
	 	  
	 	 
	 	  
	 	  //this if condition is to test for asset creation where approval level is 0.
	 	  if(numOfTransactionLevel == 0)
	 	  {
	 	  
	 	  if(ad.updateNewAssetStatuxUncapitalized(id))
	 	  {
	 	  
	 	  /////SETUP INFORMATION FOR RAISING ENTRY STARTS HERE//////////////////////////////
	 	 // String page1 = "ASSET CREATION RAISE ENTRY";
	 	 // String url = "DocumentHelp.jsp?np=updateAsset&amp;id="+id+"&pageDirect=Y";
	 	 // String flag= "y";
	 	  //String partPay="1";
	 	  //String Name =aprecords.getCodeName(" SELECT full_name from am_gb_user where user_id="+userid+"");
	 	  //String branchName= aprecords.getCodeName(" SELECT branch_name from am_ad_branch where branch_id='"+branch_id+"'");
	 	  
	 	  ////SETUP INFORMATION FOR RAISING ENTRY ENDS HERE//////////////////////////////
	 	  
	 	  
	 	  ad.setPendingTrans(ad.setApprovalDataBranch(id),"1",assetCode);
	 	  String lastMTID = ad.getCurrentMtid("am_asset_approval");
	 	  ad.setPendingTransArchive(ad.setApprovalDataBranch(id),"1",Integer.parseInt(lastMTID),assetCode);
	 	  boolean b = ad.updateNewApprovalAssetStatus(id,Integer.parseInt(userid));
	 	  
	 	  //THE SEGMENT BELOW IS TO RAISE ENTRY FOR NEW ASSET CREATION THAT DOESN'T REQUIRE APPROVAL

	 	  //THE SEGMENT BEGINS RAISE ENTRY FOR NEW ASSET CREATION THAT DOESN'T REQUIRE APPROVAL
	 	 
	 	 //--int assetCode = request.getParameter("assetCode") == null?0:Integer.parseInt(request.getParameter("assetCode"));


	 String assetRaiseEntry =aprecords.getCodeName(" SELECT raise_entry from am_gb_company ");


	 	if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("Y")){
	 	  String page1 = "ASSET CREATION RAISE ENTRY";
	 	  String url = "DocumentHelp.jsp?np=updateAssetBranch&amp;id="+id+"&pageDirect=Y";
	 	  String flag= "";
	 	  String partPay="";
	 	  String Name =aprecords.getCodeName(" SELECT full_name from am_gb_user where user_id=?",userid);
	 	String branchName= aprecords.getCodeName(" SELECT branch_name from am_ad_branch where branch_id=?",branch_id);
	 	String description= ad.getDescription();
	 	String subjectT= ad.subjectToVat(id);
	 	String whT= ad.whTax(id);
	 	aprecords.insertApprovalx(id, description, page1, flag, partPay,Name,branchName,subjectT,whT,url,Integer.parseInt(lastMTID),assetCode);
	 	}
	 	
	 	if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("N")){
	 	  ad.updateAssetStatusChange("update am_asset set asset_status='ACTIVE' where asset_id=?",id);
	 	  ad.updateAssetStatusChange("update am_asset_archive set asset_status='ACTIVE' where asset_id=?",id);
	 	  ad.updateAssetStatusChange("update am_asset_approval set process_status='A', asset_status='ACTIVE' where asset_id=?",id);
	 	  ad.updateAssetStatusChange("update am_asset_approval_archive set process_status='A', asset_status='ACTIVE'where asset_id=?",id);
	 	  }
	 	
	 	
	 	aprecords.updateRaiseEntry(id);
	 	
	   
	 	
	 	 //THE SEGMENT ENDS RAISE ENTRY FOR NEW ASSET CREATION THAT DOESN'T REQUIRE APPROVAL
	 	 
	 	
	 	//===============================BEGIN: THIS SEGMENT IS TO SEND E-MAIL TO RECEPIENT AFTER ASSET CREATION
	 	// if(b){
	 	 	
	 		legend.admin.handlers.CompanyHandler compHandler = new legend.admin.handlers.CompanyHandler();
	 			//String mail_code="002"; //the mail code for each transaction is setup in AM_TRANSACTION_TYPE
	 			//String Status1=compHandler.getEmailStatus(mail_code);

	 		String[] mailSetUp = compHandler.getEmailStatusAndName("asset creation");
	 		String Status1 = mailSetUp[0];
	 		String mail_code = mailSetUp[1];
	 		
	 		Status1 = Status1.trim();
	 		
	 		if(Status1.equalsIgnoreCase("Approved"))
	 		{ //if mail status is active then send email
	 	 
	 		String transaction_type="Asset Creation";
	 		String subject ="Asset Creation";
	 		
	 		Codes message= new Codes();
	 		
	 		

	 		String to = message.MailTo(mail_code, transaction_type);  //retrieves recipients from database

	 		String msgText1 =message.MailMessage(mail_code, transaction_type);//"New asset with ID: "+id +" was successfully created.";
	 		//System.out.println("#$$$$$$$$$$$ "+to);
	 		mail.sendMail(to,subject,msgText1);
	 	}//if(compHandler.getEmailStatus(mail_code).equalsIgnoreCase("Active"))
	 	 
	 	// }//if(b)
	 	 
	 	 //================================END:  THIS SEGMENT IS TO SEND E-MAIL TO RECEPIENT AFTER ASSET CREATION
	 	 
	 	  out.print("<script>");
	 	out.print("alert('Uncapitalized Asset creation successful');");
	 	//out.print("window.location='DocumentHelp.jsp'");
//	 	out.print("window.location='DocumentHelp.jsp?np=updateAssetViewBranch&id="+id+"&root="+root+"';");
	 	out.print("window.location='DocumentHelp.jsp?np=uploadImage&assetId="+id+"&previousPage="+currentPage+"&tran_type="+tran_type+"&root="+root+"&assetCode="+assetCode+"';");	
	 	out.print("</script>");
	 	  
	 	  }//if(ad.updateNewAssetStatus(id))
	 	  
	 	  }//if(numOfTransactionLevel == 0)
	 	  
	 	  else{
	 	   assetCode =ad.getAssetCode();
	 	  String supervisorName = "";
	 	  String mailAddress = "";	  
	 	  if(singleApproval.equalsIgnoreCase("Y")){	    
//	 	  if(updateNewAssetStatus(id))
	 	  //System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@the supervisor id is 1 ");
	 	  ad.setPendingTrans(ad.setApprovalDataBranch(id),"1",assetCode);
	 	 // System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@the supervisor id is 2 ");
	 	  String lastMTID = ad.getCurrentMtid("am_asset_approval");
	 	  //System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@the supervisor id is lastMTID " +lastMTID);
	 	  ad.setPendingTransArchive(ad.setApprovalDataBranch(id),"1",Integer.parseInt(lastMTID),assetCode);
	 	// System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@the supervisor id is 4 ");
	 	 // ad.setPendingTransArchive(ad.setApprovalData(id),"1");
	 	  
	 	  //===============================BEGIN: THIS SEGMENT IS TO SEND E-MAIL TO RECEPIENT AFTER ASSET CREATION
	 		//System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@the supervisor id is " + supervisorID);
	 		String subjectr ="Uncapitalized Asset Creation Approval";
	 		String msgText11 ="Uncapitalized Asset with ID: "+ id +" is waiting for your approval.";
	    //     mail.sendMailSupervisor(supervisorID, subjectr, msgText11);
	 		
	  		String  approvaltransId  = aprecords.getCodeName("select transaction_id from am_asset_approval where ASSET_ID=?",id);	  
	 String otherparam = "newAssetApprovalBranch&operation=1&id="+id+"&tranId="+approvaltransId+"&transaction_level=1&approval_level_count=0&assetCode="+assetCode;				
//	 				mail.sendMailSupervisor(supervisorID, subjectr, msgText11,otherparam);
	 	mailAddress =aprecords.getCodeName(" SELECT full_name from am_gb_user where user_id=?",supervisorID);	  			       comp.insertMailRecords(mailAddress,subjectr,msgText11);
	 				
	 		
	 //================================END:  THIS SEGMENT IS TO SEND E-MAIL TO RECEPIENT AFTER ASSET CREATION
	 	  
	 	  
	 	out.print("<script>");
	 	out.print("alert('Asset creation submitted for approval');");
	 	//out.print("window.location='DocumentHelp.jsp'");
//	 	out.print("window.location='DocumentHelp.jsp?np=updateAssetViewBranch&id="+id+"&root="+root+"&category="+category+"';");
//	 	out.print("window.location='DocumentHelp.jsp?np=uploadImage&assetId="+id+"&previousPage="+currentPage+"&tran_type="+tran_type+"&root="+root+"&assetCode="+assetCode+"';");
	 	 out.print("window.location='"+currentPage+"&id="+id +"&tran_status="+tran_status+"&tran_type="+tran_type+"&assetId="+id+"&reason=&pPage="+currentPage+"';");
	 	out.print("</script>");
	 	//==========================================TRANSACTION PROCESS===============================================
	 	
	 	//ad. staticApprovalInfo(id);
	 	}	
	 	   if(singleApproval.equalsIgnoreCase("N")){
	 	   
	 	  		String mtid =  appHelper.getGeneratedId("am_asset_approval");
//	 	   		System.out.println("approvelist.size()#$$$$$$$$$$$ "+approvelist.size());
	 	   	 for(int i=0;i<approvelist.size();i++)
	 	     {  
	 		  	legend.admin.objects.User usr = (legend.admin.objects.User)approvelist.get(i);   	 
	 			String supervisorId =  usr.getUserId();
	 			mailAddress = usr.getEmail();
	 			supervisorName = usr.getUserName();
	 			String supervisorfullName = usr.getUserFullName();
//	 			System.out.println("SupervisorId#$$$$$$$$$$$ "+supervisorId);
	 	  		 ad.setPendingTransMultiApp(ad.setApprovalDataBranch(id),"1",assetCode,supervisorId,mtid);
	 			 String lastMTID = ad.getCurrentMtid("am_asset_approval");
//	 		 ad.setPendingMultiApprTransArchive(ad.setApprovalData(id),"1",Integer.parseInt(lastMTID),assetCode,supervisorId);
	 			String subjectr ="Uncapitalized Asset Creation Approval";
	 			String msgText11 ="Uncapitalized Asset with ID: "+ id +" is waiting for your approval.";
	  		String  approvaltransId  = aprecords.getCodeName("select transaction_id from am_asset_approval where ASSET_ID='"+id+"'");	  
	 //String otherparam = "newAssetApproval&operation=1&id="+id+"&tranId="+approvaltransId+"&transaction_level=1&approval_level_count=0&assetCode="+assetCode;				
	 				 comp.insertMailRecords(mailAddress,subjectr,msgText11);
	 	     	}
	 	out.print("<script>");
	 	out.print("alert('Uncapitalized Asset creation submitted for approval');");
	 	//out.print("window.location='DocumentHelp.jsp?np=uploadImage&assetId="+id+"&previousPage="+currentPage+"&tran_type="+tran_type+"&root="+root+"&assetCode="+assetCode+"';");
	    out.print("window.location='"+currentPage+"&id="+id +"&tran_status="+tran_status+"&tran_type="+tran_type+"&assetId="+id+"&reason=&pPage="+currentPage+"';");
	 	out.print("</script>");			
	 	  	 }
	 		
	 	}//else of if(numOfTransactionLevel == 0)
	 	
	 	}
	 /*	 else if(statux == 1 || statux == 2 ) 
	 	 {
	 		out.print("<script>");
	 		out.print("alert('Addition of Asset will over shoot quarterly budget for this category and you are not allowed to do so. Please exit and seek supplementary budgetary Allocation "+statux+"');");
	 		out.print("window.location='DocumentHelp.jsp?np=newAsset&id';");
	 		out.print("</script>");
	 	  }
	 	  else{
	 		out.print("<script>");
	 		out.print("alert('Records can not be saved!. Your Asset does not fall in any budget quarter.');");
	 		out.print("window.location='DocumentHelp.jsp?np=newAsset&id';");
	 		out.print("</script>");
	 	  } */
	   }
	   
	  //int len = htmlUtil.getArrayList().size(); 
	  //System.out.print("===================== the length is @@@@=="+len+"================================");

	 String[] sui = ad.setUpInfo();
	 String lpoSetup = sui[0];
	 String barcodeSetup = sui[1];
	 double cpSetup = Double.parseDouble(sui[2]);
	 double cphold = Double.parseDouble(sui[3]);


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