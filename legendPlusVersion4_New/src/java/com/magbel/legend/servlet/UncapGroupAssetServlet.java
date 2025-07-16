package com.magbel.legend.servlet;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
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
public class UncapGroupAssetServlet extends HttpServlet
{
	private EmailSmsServiceBus mail ;
	
	private ApprovalRecords records;
	 legend.admin.handlers.SecurityHandler sechanle = new legend.admin.handlers.SecurityHandler();
	 com.magbel.util.HtmlUtility htmlUtil  = new com.magbel.util.HtmlUtility();
	 com.magbel.legend.bus.ApprovalRecords aprecords  = new com.magbel.legend.bus.ApprovalRecords();
	 com.magbel.util.ApplicationHelper appHelper  = new com.magbel.util.ApplicationHelper();
	 legend.admin.handlers.CompanyHandler comp  = new legend.admin.handlers.CompanyHandler();
	 magma.AssetRecordsBean ad = null;
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
	//System.out.println("ThirdPartyLabel: " + ThirdPartyLabel);
	String groupAssetByAsset = prop.getProperty("groupAssetByAsset");
	String singleApproval = prop.getProperty("singleApproval");
	//System.out.println("groupAssetByAsset: " + groupAssetByAsset);
	String VATRequired = prop.getProperty("VATREQUIRED");
	String sbuRequired = prop.getProperty("SBUREQUIRED");
	
	AssetRecordsBean ad = new AssetRecordsBean();
	 magma.ApprovalBean apbean = new magma.ApprovalBean();
	 magma.GroupAssetBean adGroup = new magma.GroupAssetBean();
	 Part filePart = request.getPart("file");
	 

	 String destination = "DocumentHelp.jsp?np=groupAssetUpdate";
	
	 String fileName = filePart.getSubmittedFileName();
	 System.out.println("fileName: " + fileName);


	 
	 String currentPage = "groupAssetBranch";
	 String id = request.getParameter("id");
	 String categoryId = request.getParameter("category_id");
	 String groupid = request.getParameter("gid");
	// System.out.println("JJJJJJJJJJJJJ groupid "+groupid);
	 String saveBtn = request.getParameter("saveBtn");
	 String isNew =  request.getParameter("s");
	 String category = request.getParameter("category_id");
	 String userid = (String)session.getAttribute("CurrentUser");
	 String partPay = request.getParameter("partPay");
//	 String payType = request.getParameter("payType");
//	 String payCode = request.getParameter("category_id");
//	 String acronym = request.getParameter("acronym");
//	 System.out.println("JJJJJJJJJJJJJ acronym "+acronym);
//	 if(payCode == null){payCode = "0";}
//	 if(payType == null){payType = "N";}
//	 if(acronym == null){acronym = "";}
	 if(partPay == null){partPay = "N";}
	 if(category == null){category = "0";}
	 
	 String branchRestrict =aprecords.getCodeName(" SELECT branch_restriction from am_gb_user where user_id=?",userid);
	 branchRestrict = branchRestrict == null?"Y":branchRestrict.trim();
	 System.out.println("<<<<< branchRestrict: " + branchRestrict);
	 String branchRestrictCode =aprecords.getCodeName(" SELECT branch from am_gb_user where user_id=?",userid);
	 branchRestrictCode = branchRestrictCode == null?"0":branchRestrictCode.trim();
	 System.out.println("<<<<< branchRestrictCode: " + branchRestrictCode);
	 String branch_id = request.getParameter("branch_id");
	 System.out.println("<<<<< branch_id: " + branch_id);
	 String subjecttovat = aprecords.getCodeName("select VAT_SELECTION from am_gb_company");
	 String thirdpartyrqd = aprecords.getCodeName("select THIRDPARTY_REQUIRE from am_gb_company");
	 String lpo = request.getParameter("lpo");
	 String invoiceNo = request.getParameter("invNo");
	 String vendorNo = request.getParameter("sb");

	 String cost_threshold = aprecords.getCodeName("select cost_threshold from am_gb_company");
	 com.magbel.util.CurrencyNumberformat formata = new com.magbel.util.CurrencyNumberformat();
	 String newURL ="";
	 String sup_name_qry = "";
	 String Supervisor_name = "";

	 int numOfTransactionLevel =  ad.getNumOfTransactionLevel("19");
	 legend.admin.objects.User user = sechanle.getUserByUserID(userid);
	 String userName = user.getUserName();
	 //String branchRestrict = user.getBranchRestrict();
	 String UserRestrict = user.getDeptRestrict();
	 String departCode = user.getDeptCode();
	 String branch = user.getBranch();

 	String asset_id = request.getParameter("asset_id");
 	String date_of_purchase = request.getParameter("date_of_purchase");
    String sub_category_id = request.getParameter("sub_category_id");
    String sbu_code = request.getParameter("sbu_code");
    String cost_price = request.getParameter("cost_price");
    String vat_amount =  request.getParameter("vat_amount");
    String vatable_cost = request.getParameter("vatable_cost");
    String wh_tax_amount = request.getParameter("wh_tax_amount");
    String residual_value = request.getParameter("residual_value");
    if(residual_value.equals("")) {residual_value = "0";}
    String amountPTD = request.getParameter("amountPTD");
    String department_id = request.getParameter("department_id");
    String descriptions = request.getParameter("description");
    String region = request.getParameter("region");
    String make = request.getParameter("make");
    String location = request.getParameter("location");
    String section_id = request.getParameter("section_id");
  //  System.out.println("JJJJJJJJJJJJJ section_id "+section_id);
    String state = request.getParameter("state");
 //   System.out.println("JJJJJJJJJJJJJ state "+state);
    String driver = request.getParameter("driver");
  //  System.out.println("JJJJJJJJJJJJJ driver "+driver);
    String bar_code = request.getParameter("bar_code");
    
    
    String threshold = request.getParameter("threshold");
    String lposet = request.getParameter("lposet");
    
   String vendor_acc = request.getParameter("vendor_accountOld");
   String vendor_name =  request.getParameter("vendorName");
   String maintained_by = request.getParameter("maintained_by");
   String purchaseReason = request.getParameter("reason");
   String authorized_by = request.getParameter("authorized_by");
   String no_of_items = request.getParameter("no_of_items");
   String depr_start_date = request.getParameter("depreciation_start_date");
   String depr_end_date = request.getParameter("depreciation_end_date");
   String assetUser = request.getParameter("user");
   String subject_to_vatOld = request.getParameter("subject_to_vatOld");
    String wh_tax_cb= request.getParameter("wh_tax_cb");
    String transport_cost = request.getParameter("transport_cost");
    String other_cost = request.getParameter("other_cost");
    String depreciation_rate = request.getParameter("depreciation_rate");
    String accum_dep = request.getParameter("accum_dep");
    String nbv = request.getParameter("nbv");
    String supplied_by = request.getParameter("sb");
    String who_to_rem1 = request.getParameter("who_to_rem");
    String invoiceNum = request.getParameter("invoiceNum");
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
    String require_redistribution = request.getParameter("require_redistribution");
    String fullyPAID = request.getParameter("fullyPAID");
    String registration_no = request.getParameter("registration_no");
    String serial_number = request.getParameter("serial_number");
    String integrifyId = request.getParameter("integrifyId");
    String engine_number = request.getParameter("engine_number");
    String model = request.getParameter("model");
    String multiple = request.getParameter("multiple");

    if(require_redistribution == null){require_redistribution = "";}
    if(sbu_code == null){sbu_code = "";}
    if(projectCode == null){projectCode = "";}
    
  //  System.out.println("<<<<< We are here 5: ");
	//adGroup.setGroup_id(groupid);
	// System.out.println("<<<<< We are here 6: ");
		adGroup.setNo_of_items(no_of_items);
		adGroup.setDepreciation_start_date(depr_start_date);
   adGroup.setDepreciation_end_date(depr_end_date);
   adGroup.setDepartment_id(department_id);
    adGroup.setDate_of_purchase(date_of_purchase);
    adGroup.setSub_category_id(sub_category_id);
    adGroup.setSbu_code(sbu_code);
    adGroup.setBranch_id(branch_id);
    adGroup.setCategory_id(category);
    adGroup.setCost_price(cost_price);
    adGroup.setVat_amount(vat_amount);
    adGroup.setVatable_cost(vatable_cost);
    adGroup.setWh_tax_amount(wh_tax_amount);
    adGroup.setResidual_value(residual_value);
    adGroup.setAmountPTD(amountPTD);
    adGroup.setDescription(descriptions);
    adGroup.setVendorName(vendor_name);
//    adGroup.setRegionCode(region);
    adGroup.setMake(make);
    adGroup.setLocation(location);
    adGroup.setSection(section_id);
    adGroup.setSection_id(section_id);
    adGroup.setState(state);
    adGroup.setDriver(driver);
    adGroup.setBar_code(bar_code);
    adGroup.setVendor_account(vendor_acc);
    adGroup.setVendorName(vendor_name);
    adGroup.setMaintained_by(maintained_by);
    adGroup.setReason(purchaseReason);
    adGroup.setUser(assetUser);
    adGroup.setSupplied_by(vendorNo);
    adGroup.setSbu_code(sbu_code);
    adGroup.setLpo(lpo);
    adGroup.setAuthorized_by(authorized_by);
    System.out.println("<<<<< We are here 7: ");
    adGroup.setSubject_to_vat(subject_to_vatOld);
    adGroup.setWh_tax_cb(wh_tax_cb);
    adGroup.setTransport_cost(transport_cost);
    adGroup.setOther_cost(other_cost);
    adGroup.setDepreciation_rate(depreciation_rate);
    adGroup.setAccum_dep(accum_dep);
    adGroup.setSupplied_by(supplied_by);
    adGroup.setWho_to_rem(who_to_rem1);
    adGroup.setInvoiceNum(invoiceNum);
    adGroup.setEmail_1(e_mail1);
    adGroup.setWho_to_rem_2(who_to_rem2);
    adGroup.setPosting_date(posting_date);
    adGroup.setEmail2(email2);
    adGroup.setSpare_1(spare_1);
    adGroup.setSpare_2(spare_2);
    adGroup.setSpare_3(spare_3);
    adGroup.setSpare_4(spare_4);
    adGroup.setSpare_5(spare_5);
    adGroup.setSpare_6(spare_6);
    adGroup.setWarrantyStartDate(warrantyStartDate);
    adGroup.setNoOfMonths(noOfMonths);
    adGroup.setExpiryDate(expiryDate);
    adGroup.setProjectCode(projectCode);
    adGroup.setRequire_depreciation(require_depreciation);
    adGroup.setRequire_redistribution(require_redistribution);
    adGroup.setUser_id(user_Id);
    adGroup.setFullyPAID(fullyPAID);
   adGroup.setPartPAY(partPay);
    adGroup.setRegistration_no(registration_no);
    adGroup.setSerial_number(serial_number);
    adGroup.setIntegrifyId(integrifyId);
    adGroup.setEngine_number(engine_number);
    adGroup.setModel(model);
    
    
	
    String limitCode=apbean.getApprovalLimit(userid);
    System.out.println("JJJJJJJJJJJJJ limitCode "+limitCode );
    apbean.getApprovalDetail(limitCode);
    double min_approval = apbean.getMini_approval();
    double max_approval = apbean.getMax_approval();
    String vatRate ="0";
    String taxRate ="0";

    String branchrestricted = "";  
    if(userid != null){
       branchrestricted = sechanle.getBranchRestrictedUser(userid);	
    }

    //System.out.println ("min_approval >>>>> " + min_approval);
    //System.out.println ("max_approval >>>>> " + max_approval);
    String workstationIp = request.getRemoteAddr();
    InetAddress address = InetAddress.getByName(workstationIp);
    String workstationName =  address.getHostName();
    //System.out.println("workstationName : "+ workstationName);
    //System.out.println("workstationIp : "+ workstationIp);

    System.out.println("Transaction level : "+ numOfTransactionLevel);

    if(isNew!=null && isNew.equalsIgnoreCase("n")) session.setAttribute("newAsset",null);
    if(session.getAttribute("newAsset")!=null)
      {
      	ad=(magma.AssetRecordsBean)session.getAttribute("newAsset");
    	category = ad.getCategory_id();
      }

    if(request.getParameter("saveBtn") != null)
    {
    	try
    		{
    			long [] statux = adGroup.insertGroupAssetRecordUnclassified(groupAssetByAsset,singleApproval,branch,departCode,userName);
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
		                        
		                        String newPath = uploadPath + "W" + statux[1] + "." + ext;
//		                        System.out.println("The new file path is >=: " + newPath);
		                        
		                       
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
    			
    			if(statux[0] == 0 )
    			 {
    			 //SET ASSET CREATION FOR TRANSACTION APPROVAL
    			  //this if condition is to test for asset creation where approval level is 0.
    			    id = adGroup.getAsset_id();
    				System.out.println("$$$$$$$$$$$$$ 2: ");
//    				System.out.println(" Id >>>>>  "+id);
    			  long group_ID = adGroup.getGroupUncapID(id);
    			   System.out.println(" groupID >>>>>  "+group_ID);
    			  //if(ad.updateNewAssetStatus(id)){
    			  String groupID = Long.toString(group_ID);
//    			  System.out.println(" id >>>>> "+id);
    			  System.out.println(" groupID >>>>>  "+groupID);
    	 		 if(numOfTransactionLevel == 0)
    	 		 {
    			
    			  
    	 // ad.updateGroupAssetStatus(id); 
    	 //commented by ayo, since there is only one group asset for all the assets, using the assetId to update am_group_asset will mean that only one 	the assets will be updated, so that's why I changed it to the Group_id instead
    	 
    	  //ad.updateGroupAssetStatus(groupID); NOT NEEDED SINCE THEIR IS ANOTHER CALL TO THE METHOD JUST BELOW
    	  
    	 // ad.setPendingTrans(ad.setApprovalDataGroup(group_ID),"3"); 
    	  //ad.updateAssetStatusChange("update am_group_asset set asset_status='ACTIVE' where asset_id='"+id+"'");
    	 // ad.updateGroupAssetStatus(id); commented by ayojava; same as above
      	//  ad.updateGroupAssetStatus(groupID); will be done in the code
    	 // boolean b = ad.updateNewApprovalAssetStatus(id,Integer.parseInt(userid)); commented by ayojava
    	 
    	  //boolean b = ad.updateNewApprovalAssetStatus(groupID,Integer.parseInt(userid));
    	  
    	  //THE SEGMENT BELOW IS TO RAISE ENTRY FOR NEW ASSET CREATION THAT DOESN'T REQUIRE APPROVAL
    	  
    	   //THE SEGMENT BEGINS RAISE ENTRY FOR NEW ASSET CREATION THAT DOESN'T REQUIRE APPROVAL
    	
    	  String page1 = "Group ASSET CREATION RAISE ENTRY";
    	  //String url = "DocumentHelp.jsp?np=updateAsset&amp;id="+id+"&pageDirect=Y";
    	  String url = "DocumentHelp.jsp?np=groupAssetUpdateBranch&id="+groupID+"&pageDirect=Y";

    	  String flag= "";
    	  //String partPay="";
    	  String Name =aprecords.getCodeName(" SELECT full_name from am_gb_user where user_id=?",userid);
    String branchName= aprecords.getCodeName(" SELECT branch_name from am_ad_branch where branch_id=?",branch_id);
    	  String description= adGroup.getDescription();
    	  /*String subjectT= ad.subjectToVat(id);//modify this method to read from am_group_asset_main
    	  String whT= ad.whTax(id);//modify this method to read from am_group_asset_main*/
    	  String subjectT= adGroup.subjectToVat(id);
    	  String whT= adGroup.whTax(id);
    	  
    	 /* System.out.println("description : " + description );
    	  System.out.println("subjectT : " + subjectT );
    	  System.out.println("whT : " + whT );*/
    	  //aprecords.insertApproval(id, description, page1, flag, partPay,Name,branchName,subjectT,whT,url)
    	  //aprecords.updateRaiseEntry(id);
    	  //;-commented as above
//    	  note that the methods called have been modified to receive asset_id instead of group_id
    //TEMPORARILY COMMENTED BY AYOJAVA SINCE IT WILL BE DONE IN THE CODE
    	  //aprecords.insertApproval(groupID, description, page1, flag, partPay,Name,branchName,subjectT,whT,url);
    	  //aprecords.updateRaiseEntry(groupID);// look through this method,it is updating am_asset and we don't have entries in dt table
    	//  adGroup.updateGroupAssetRaiseEntry(groupID);
    	
    		 //THE SEGMENT ENDS RAISE ENTRY FOR NEW ASSET CREATION THAT DOESN'T REQUIRE APPROVAL
    	 
    	
    	//===============================BEGIN: THIS SEGMENT IS TO SEND E-MAIL TO RECEPIENT AFTER ASSET CREATION
    	/* 
    	 
    	 if(b){
    	 	
    		legend.admin.handlers.CompanyHandler compHandler = new legend.admin.handlers.CompanyHandler();
    			String mail_code="201"; //the mail code for each transaction is setup in AM_TRANSACTION_TYPE
    		
    		if(compHandler.getEmailStatus(mail_code).equalsIgnoreCase("Active")){ //if mail status is active then send email
    	 
    		String transaction_type="Group Asset Creation";
    		String subject ="Group Asset Creation";
    		
    		Codes message= new Codes();
    		
    		EmailSmsServiceBus mail = new EmailSmsServiceBus();

    		String to = message.MailTo(mail_code, transaction_type);  //retrieves recipients from database

    		String msgText1 ="New asset with ID: "+id +" was successfully created.";

    		mail.sendMail(to,subject,msgText1);
    	}//if(compHandler.getEmailStatus(mail_code).equalsIgnoreCase("Active"))
    	 
    	 }//if(b)
    	 
    	 
    	 */
    	 //================================END:  THIS SEGMENT IS TO SEND E-MAIL TO RECEPIENT AFTER ASSET CREATION
    		String invnumb = vendorNo+'-'+invoiceNo;
    	  htmlUtil.insToAm_Invoice_No(groupID,lpo,invnumb,"Group Uncapitalised Asset Creation");		 
    	  out.print("<script>");
    	  out.print("alert('Group Uncapitalized Asset creation successful');");
    	  out.print("window.location='DocumentHelp.jsp?np=groupAssetUpdateBranch&img=n&id="+statux[1]+"';");
    	  out.print("</script>");

    	  //}//if(ad.updateNewAssetStatus(id))
    	  
    	  }//if(numOfTransactionLevel == 0)
    	  
    	  else
    	  {
    		 sup_name_qry="select full_name from am_gb_user where user_id=(select supervisor from am_group_asset_main where group_id=?)";
//    		System.out.println("sup_name_qry >>>>> " + sup_name_qry);
     		Supervisor_name = aprecords.getCodeName((sup_name_qry).toUpperCase(),groupID);
    			  
    		//ad.setPendingTrans(ad.setApprovalDataGroup(groupid));
    		if(groupAssetByAsset.equalsIgnoreCase("Y")){
    newURL = "<script>alert('Group Uncapitalized Asset will be submitted for Approval after Final Creation Of Assets');window.location='DocumentHelp.jsp?np=groupAssetUpdateBranch&img=n&id="+statux[1]+"';</script>";
   // newURL = "<script>alert('Group Uncapitalized Asset Has Been Sent For Approval');window.location='DocumentHelp.jsp?np=uploadImage&img=n&assetId="+statux[1]+"&previousPage="+currentPage+"&assetCode="+statux[1]+"';</script>";
    		}
    		if(groupAssetByAsset.equalsIgnoreCase("N")){
    newURL = "<script>alert('Group Uncapitalized Asset Has Been Sent For Approval');window.location='DocumentHelp.jsp?np=groupAssetUpdateBranch&img=n&id="+statux[1]+"';</script>";
    //newURL = "<script>alert('Group Uncapitalized Asset Has Been Sent For Approval');window.location='DocumentHelp.jsp?np=uploadImage&img=n&assetId="+statux[1]+"&previousPage="+currentPage+"&assetCode="+statux[1]+"';</script>";
    		}
    	String invnumb = vendorNo+'-'+invoiceNo;
    	htmlUtil.insToAm_Invoice_No(groupID,lpo,invnumb,"Group Uncapitalised Asset Creation");	
    	   }//else for if(numOfTransactionLevel == 0) 
    			 
    			 out.print(newURL);
    	   
    			 
    	}
    	 else if(statux[0] == 1 || statux[0] == 2 ) 
    	 {  out.print("<script>");
    		out.print("alert('Addition of Assets will over shoot quarterly budget for this category and you are not allowed to do so. Please exit and seek supplementary budgetary Allocation "+statux+"');");
    		out.print("history.go(-2);");
    		out.print("</script>");
    		
    	  } else{
    		out.print("<script>");
    		out.print("alert('Records can not be saved!. Please Check your entries.');");
    		out.print("history.go(-2);");
    		out.print("</script>");
    	  }
    		}catch(Exception e){
    		e.printStackTrace();
    		System.out.println("\n\nError creating Group Asset\n"+e+"\n");
    			out.print("<script>alert('Unable to save. Error encountered');history.go(-1);</script>");
    		}
    	
    	}
    	
    	
    String[] sui = ad.setUpInfo();
    String lpoSetup = sui[0];
    String barcodeSetup = sui[1];
    double cpSetup = Double.parseDouble(sui[2]);





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