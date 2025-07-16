package com.magbel.fixedassetcreationLegendPlus.bus;
 
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import legend.admin.handlers.CompanyHandler;
import magma.net.manager.AssetPaymentManager;





import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*; 

import com.magbel.legend.vao.newAssetTransaction;
import com.magbel.util.DatetimeFormat;
 
public class Jobs14
    implements Job
{  
        
    private static Log _log = LogFactory.getLog(Jobs14.class);
    CompanyHandler comp = new CompanyHandler();
    AssetPaymentManager adp = new AssetPaymentManager();
    DatetimeFormat df;
    public Jobs14()
    {
        
    }
 
    public void execute(JobExecutionContext context)
        throws JobExecutionException
    {
  //  	int statux = 0;
//		int statuk = 0;
    	df = new com.magbel.util.DatetimeFormat();
  try
        {  
	  int  j=0;
//	     java.util.ArrayList list_iso =comp.getSqlRecords();
//	     System.out.println("-->WIP Asset Creation size>--> "+list_iso.size());
	     java.util.ArrayList list =comp.getNewWIPAssetSqlRecords();
	     System.out.println("-->size of Creation Asset in WIP>--> "+list.size());
	     //Iso Starts	

//	     for(int i=0;i<list_iso.size();i++)
//	     {
//	    	 com.magbel.legend.vao.newAssetTransaction  newtrans = (com.magbel.legend.vao.newAssetTransaction)list_iso.get(i);    	 
//				String TransId =  newtrans.getAssetId();
//				String finacleTransId =  newtrans.getTranType();
////	    	 String finacleTransId=(String) list_iso.get(i);
//	    //	 System.out.println("-->>--> "+finacleTransId);
//	    	 String isoValue=comp.getFinacleRecords(finacleTransId); 
//	    //	 System.out.println("-->isoValue>--> "+isoValue);
//	    	 if(isoValue.equalsIgnoreCase("000"))
//	    		 comp.updateSqlRecords(isoValue, finacleTransId,TransId);
//	     }	
	     // Iso End
	   //  if(list.size()>0){
	     String oldintegrify = "";
//	     String ProcessingThirdPartyRecord =comp.findObject(" SELECT THIRDPARTY_REQUIRE from am_gb_company ");
//	     if(ProcessingThirdPartyRecord.equalsIgnoreCase("Y")){
	     String delimiter = comp.findObject("SELECT DELIMITER FROM am_ad_auto_identity ");
	    	
	     while (j < list.size())
	     {
//	    	 System.out.println("<<<<<<<<J Loop>>>>>>  "+j+"  list.size>> "+list.size() );
	    	 com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(j);    	 
			String integrifyId =  newassettrans.getIntegrifyId();
//			 System.out.println("<<<<<<<<integrifyId in Loop>>>>>>  "+integrifyId);
			String compintegrifyId =  newassettrans.getIntegrifyId();
			String Description = newassettrans.getDescription();
			String RegistrationNo = newassettrans.getRegistrationNo();
			String VendorAC = newassettrans.getVendorAC();
			String Datepurchased = newassettrans.getDatepurchased();
			String AssetMake = newassettrans.getAssetMake();
			String AssetModel = newassettrans.getAssetModel();
			String AssetSerialNo = newassettrans.getAssetSerialNo();
			String AssetEngineNo = newassettrans.getAssetEngineNo();
			String SupplierName = newassettrans.getSupplierName();
			String AssetUser = newassettrans.getAssetUser();
			String AssetMaintenance = newassettrans.getAssetMaintenance();
			double VatableCost = newassettrans.getCostPrice();
			String AuthorizedBy = newassettrans.getAuthorizedBy();
			String WhTax = newassettrans.getWhTax();
			String PostingDate = newassettrans.getPostingDate();
			String EffectiveDate = newassettrans.getEffectiveDate();
			String PurchaseReason = newassettrans.getPurchaseReason();
			String SubjectTOVat = newassettrans.getSubjectTOVat();
			//String AssetStatus = newassettrans.getAssetStatus();
			String State = newassettrans.getState();
			String Driver = newassettrans.getDriver();
			String UserName = newassettrans.getUserID();
			String branchCode = newassettrans.getBranchCode();
			String sectionCode = newassettrans.getSectionCode();
			String deptCode = newassettrans.getDeptCode();
			String categoryCode = newassettrans.getCategoryCode();
			String subcategoryCode = newassettrans.getSubcategoryCode();
			if(subcategoryCode==null){subcategoryCode = "000";}
			String barCode = newassettrans.getBarCode();
			String sbuCode = newassettrans.getSbuCode();
			String lpo = newassettrans.getLpo();
			String invoiceNo = newassettrans.getInvoiceNo();
			String supervisorName = newassettrans.getSupervisor();
			String posted = newassettrans.getposted();
			String assetId = newassettrans.getAssetId();
			String assetCode = newassettrans.getAssetCode();
			String errormessage = newassettrans.getErrormessage();
			String assettype = newassettrans.getAssetType();
			double whtaxvalue = newassettrans.getWhTaxValue();
			double vatvalue = newassettrans.getVatValue();
			String tranType = newassettrans.getTranType();
			int whtaxrate = (int)whtaxvalue;
			int noofitems = newassettrans.getNoofitems();
			String systemIp = newassettrans.getSystemIp();
			String location = newassettrans.getLocation();
			String spare1 = newassettrans.getSpare1();
			String spare2 = newassettrans.getSpare2();
			String spare3 = newassettrans.getSpare3();
			String spare4 = newassettrans.getSpare4();
			String spare5 = newassettrans.getSpare5();
			String spare6 = newassettrans.getSpare6();
			String memo = newassettrans.getMemo();
			String memovalue = newassettrans.getMemovalue();
			String multiple = newassettrans.getMultiple();
			int usefullife = newassettrans.getUsefullife();
			String projectCode = newassettrans.getProjectCode();
//			String location = newassettrans.getLocation();
//			System.out.println("vatvalue>>>>>> "+vatvalue+"  whtaxvalue:  "+whtaxvalue);
			if(multiple==null){multiple="N";}
		//	String groupitemsNo = comp.findObject("SELECT count(*) FROM NEW_GROUP_ASSET_INTERFACE WHERE POSTED = 'N' AND TRAN_TYPE = 'N' AND INTEGRIFY_ID = '"+integrifyId+"'");
			//int recpend = Integer.parseInt(groupitemsNo);
//			System.out.println("integrifyId>>>>>> "+integrifyId+"  noofitems:  "+noofitems+" oldintegrify: "+oldintegrify+"  tranType: "+tranType);
			String errorMessage = "";
			String tablename = "";
			String transtype = "";
			String AssetStatus = "ACTIVE";
			String assetstatus = "";
			String infofld = "";
			//String assetexist = "";
			String integrifyIdexist = "";
			String approvalassetexist = "";
			double CostPrice = VatableCost;
			double whtaxamount = 0.00;
			double vatamount = 0.00;   
			String fromRepost = "N";
			String page1 = "ASSET CREATION RAISE ENTRY";
			String subjectr ="Asset Creation Approval";
			String url = "";
			 String groupid = "";
			 int statux = 1;
			 String supervisor_confirm = "";
			 String user_confirm = "";
			 String UserID = "";
			 String supervisor = "";
			 double vatrate = 0.0;
			 double whtrate = 0.0;
			 String approvedbyName = "";
			 String groupexist = "";
			 String statuk = "";
			 assetstatus = "P";
			 boolean donetemp = false;
		     if(integrifyId!=oldintegrify){
			      errorMessage = "Process Ongoing; ";
			       donetemp = comp.newassetinterface(errorMessage, integrifyId,assetstatus,assetId,assetCode);
			     }
		     
			// String branch_id = "";
//			 System.out.println("User Name>>>>>> "+UserName); 
			//errorMessage=""; assetstatus = "P";
			//comp.newassetinterface(errorMessage, integrifyId,assetstatus,assetId,assetCode);
			 integrifyIdexist = comp.findObject("SELECT INTEGRIFY FROM AM_ASSET WHERE INTEGRIFY = '"+integrifyId+"'");
//			 System.out.println("assettype Before Looping: "+assettype+" User Name: "+UserName );
//			 String groupitemsNo = comp.findObject("SELECT count(*) FROM NEW_GROUP_ASSET_INTERFACE WHERE POSTED = 'N' AND TRAN_TYPE = 'N' AND INTEGRIFY_ID = '"+compintegrifyId+"'");
//			 System.out.println("Group Item Number>>>>>> "+groupitemsNo+"  compintegrifyId: "+compintegrifyId);
			String assetRaiseEntry =comp.findObject(" SELECT raise_entry from am_gb_company ");
			String branch_id = comp.findObject("SELECT BRANCH_ID FROM am_ad_branch WHERE BRANCH_CODE = '"+branchCode+"' ");
//			System.out.println("supervisorName >>>>>> "+supervisorName);
			 if(supervisorName==null){
			      assetstatus = "C"; 
			      errorMessage = "Suppervisor Id must be provided; ";
			      boolean done = comp.newassetinterface(errorMessage, integrifyId,assetstatus,assetId,assetCode);						 
			 }	
//			 System.out.println("supervisorName 0>>>>>> "+supervisorName);
			 if(UserName==null){
			      assetstatus = "C";
			      errorMessage = "User Id must be provided; ";
			      boolean done = comp.newassetinterface(errorMessage, integrifyId,assetstatus,assetId,assetCode);						 
			 }
//			 System.out.println("UserName 0>>>>>> "+UserName);
//			 if(Description==null){
//			      assetstatus = "C";  
//			      errorMessage = "Description must be provided; ";
//			      boolean done = comp.newassetinterface(errorMessage, integrifyId,assetstatus,assetId,assetCode);						 
//			 }	
//			 if(branch_id==null){
//			      assetstatus = "C";
//			      errorMessage = "Branch Code must be provided; ";
//			      boolean done = comp.newassetinterface(errorMessage, integrifyId,assetstatus,assetId,assetCode);						 
//			 }			
//			 if(categoryCode.equalsIgnoreCase("WIP")){
//			      assetstatus = "C";
//			      errorMessage = "Selected Category must be WIP; ";
//			      boolean done = comp.newassetinterface(errorMessage, integrifyId,assetstatus,assetId,assetCode);						 
//			 }					 
//			 System.out.println("supervisorName 2>>>>>> "+supervisorName);
			if(supervisorName==null){supervisorName="";}
//			System.out.println("UserName 2>>>>>> "+UserName);
			if(!UserName.equalsIgnoreCase("") && UserName!=null){user_confirm = comp.findObject("select full_name from am_gb_user where user_name = '"+UserName+"' AND USER_STATUS = 'ACTIVE'");}
			if(!UserName.equalsIgnoreCase("") && UserName!=null){UserID = comp.findObject("select USER_ID from am_gb_user where user_name = '"+UserName+"' AND USER_STATUS = 'ACTIVE'");}
			if(!supervisorName.equalsIgnoreCase("")){supervisor = comp.findObject("select USER_ID from am_gb_user where user_name = '"+supervisorName+"' AND USER_STATUS = 'ACTIVE'");}
//			System.out.println("supervisor 2>>>>>> "+supervisor);
			if(supervisor==null){supervisor="0";}
			if(UserName==null){UserName="";}
			if(!supervisorName.equalsIgnoreCase("") && (supervisorName!=null))
			{ if(!branch_id.equalsIgnoreCase("")){supervisor_confirm = comp.findObject("select full_name from am_gb_user where user_Name = '"+supervisorName+"' AND IS_SUPERVISOR = 'Y' AND BRANCH = "+branch_id+" AND USER_STATUS = 'ACTIVE'");}}
			double treshhold =Double.parseDouble(comp.findObject(" SELECT Cost_Threshold from am_gb_company "));
			int numOfTransactionLevel =  comp.getNumOfTransactionLevel("27");
//			System.out.println("Description: "+Description);
			if((supervisor==null) || (supervisor=="")){supervisor="0";}
//			System.out.println("Supervisor by Id After Zerorising: "+supervisor);
			if(supervisor!="0"){approvedbyName =comp.findObject(" SELECT full_name from am_gb_user where user_id="+Integer.parseInt(supervisor)+"");}
//			System.out.println("numOfTransactionLevel>>>>>> "+numOfTransactionLevel+"  User Confirm: "+user_confirm);
			if(supervisor=="0"){approvedbyName="Supervisor Not Found";}
//			System.out.println("Supervisor by Id After: "+supervisor);
//			System.out.println("Approved by Name: "+approvedbyName);
//			System.out.println("invoiceNo by Name: "+invoiceNo);
			 try {
				 errorMessage = "";
				 if(invoiceNo == null){
				      assetstatus = "C";
				      errorMessage = "Invoice No Cannot be Null; ";
				      boolean done = comp.newassetinterface(errorMessage, integrifyId,assetstatus,assetId,assetCode);						 
				 }	
				 if(lpo == null){
				      assetstatus = "C";
				      errorMessage = errorMessage +"LPO No Cannot be Null; ";
				      boolean done = comp.newassetinterface(errorMessage, integrifyId,assetstatus,assetId,assetCode);						 
				 }				 
				 if(user_confirm.equalsIgnoreCase("")){
				      assetstatus = "C";
				      errorMessage = errorMessage+"Invalid User Name; ";
				      boolean done = comp.newassetinterface(errorMessage, integrifyId,assetstatus,assetId,assetCode);						 
				 }				 
				 if(Datepurchased==null){
				      assetstatus = "C";
				      errorMessage = errorMessage+"Purchase Date Cannot be empty; ";
				      boolean done = comp.newassetinterface(errorMessage, integrifyId,assetstatus,assetId,assetCode);						 
				 }	
				 System.out.println("PostingDate:  "+PostingDate);
				 if(PostingDate==null){
				      assetstatus = "C";
				      errorMessage = errorMessage+"Posting Date Cannot be empty; ";
				      boolean done = comp.newassetinterface(errorMessage, integrifyId,assetstatus,assetId,assetCode);						 
				 }		
				 if(EffectiveDate==null){
				      assetstatus = "C";
				      errorMessage = errorMessage+"Effective Date Cannot be empty; ";
				      boolean done = comp.newassetinterface(errorMessage, integrifyId,assetstatus,assetId,assetCode);						 
				 }					 
				 if(numOfTransactionLevel!=0){
				 if(supervisor_confirm.equalsIgnoreCase("")){
				      assetstatus = "C";
				      errorMessage = errorMessage+"Invalid Supervisor; ";
				      boolean done = comp.newassetinterface(errorMessage, integrifyId,assetstatus,assetId,assetCode);						 
				 }	
				 }
//				 System.out.println("WhTax:  "+WhTax+"   SubjectTOVat: "+SubjectTOVat);
				 if((!WhTax.equalsIgnoreCase("S")) && (!WhTax.equalsIgnoreCase("F")) && (!WhTax.equalsIgnoreCase("N"))){
				      assetstatus = "C";
				      errorMessage = errorMessage+"Invalid Withholding Tax Parameter; ";
				      boolean done = comp.newassetinterface(errorMessage, integrifyId,assetstatus,assetId,assetCode);						 
				 }					 
				 //if((!SubjectTOVat.equalsIgnoreCase("Y")) && (!SubjectTOVat.equalsIgnoreCase("N"))){
				 if((!SubjectTOVat.equalsIgnoreCase("Y")) && (!SubjectTOVat.equalsIgnoreCase("N"))){				 
				      assetstatus = "C";
				      errorMessage = errorMessage+"Invalid Vat Parameter It Must be Y OR N; ";
				      boolean done = comp.newassetinterface(errorMessage, integrifyId,assetstatus,assetId,assetCode);						 
				 }					 
			//	 System.out.println("assettype: "+assettype );
				 //integrifyIdexist = comp.findObject("SELECT INTEGRIFY_ID FROM Am_Invoice_no WHERE INTEGRIFY_ID = '"+integrifyId+"'");
		//		 System.out.println(".:  "+integrifyIdexist);
//				 if(assettype.equalsIgnoreCase("C") && errorMessage.equalsIgnoreCase("")){
//		//			 System.out.println("Capitalised CostPrice: "+CostPrice+"  treshhold: "+treshhold);
//					 if(CostPrice < treshhold+0.01){
//					      assetstatus = "C";
//					      errorMessage = errorMessage +"This Asset cannot be Capitalised; ";
//					      boolean done = comp.newassetinterface(errorMessage, integrifyId,assetstatus,assetId,assetCode);						 
//					 }
//					// assetexist = comp.findObject("SELECT ASSET_ID FROM AM_ASSET WHERE INTEGRIFY = '"+integrifyId+"' AND GROUP_ID = "+noofitems+"");
//					 tablename = "AM_ASSET";
//					 transtype = "Asset Creation";
//		//			 System.out.println("assetexist: "+integrifyIdexist);
//				 }
				 if(assettype.equalsIgnoreCase("C")&& errorMessage.equalsIgnoreCase("")){
				//	 double TotalCostPrice = Double.parseDouble(comp.findObject("select sum(cost_price) from NEW_GROUP_ASSET_INTERFACE where INTEGRIFY_ID = '"+integrifyId+"' AND ASSET_TYPE = 'U'"));
					 //assetexist = comp.findObject("SELECT ASSET_ID FROM AM_ASSET_UNCAPITALIZED WHERE INTEGRIFY = '"+integrifyId+"' AND GROUP_ID = "+noofitems+"");
					 tablename = "AM_ASSET";
					 transtype = "WIP Asset Creation ";
				 }	
				 
//				 if(assettype.equalsIgnoreCase("C")&& errorMessage.equalsIgnoreCase("") && (noofitems!=0)){
//					 String costPrice = comp.findObject("select sum(cost_price) from NEW_GROUP_ASSET_INTERFACE where INTEGRIFY_ID = '"+integrifyId+"' AND ASSET_TYPE = 'C'");
//					 if(costPrice==null){costPrice="0.00";}
//					 //			 System.out.println("Total Cost Price: "+Double.parseDouble(comp.findObject("select sum(cost_price) from NEW_GROUP_ASSET_INTERFACE where INTEGRIFY_ID = '"+integrifyId+"' AND ASSET_TYPE = 'C'")));
//					 double TotalCostPrice = Double.parseDouble(costPrice);
////					 System.out.println("Capitalised TotalCostPrice: "+TotalCostPrice+"  treshhold: "+treshhold+"  CostPrice: "+CostPrice);
//					 if(TotalCostPrice != CostPrice){
//					      assetstatus = "C";
//					      errorMessage = errorMessage +"Summary and Detail Total not Equal; ";
//					      boolean done = comp.newassetinterface(errorMessage, integrifyId,assetstatus,assetId,assetCode);						 
//					 }
//				 }	
				 
				 
				// if(tranType.equalsIgnoreCase("P") && (noofitems==0)){
					 //assetexist = comp.findObject("SELECT ASSET_ID FROM am_asset_improvement WHERE INTEGRIFY = '"+integrifyId+"'");
					// tablename = "AM_ASSET_UNCAPITALIZED";
					// transtype = "Asset Creation Uncapitalized";
				// }	
				 String residualvalue =comp.findObject(" SELECT residual_value from am_gb_company ");
				 System.out.println("SubjectTOVat:  "+SubjectTOVat);
/*				 if(SubjectTOVat.equalsIgnoreCase("Y")){
					 String vatvalue =comp.findObject(" SELECT vat_rate from am_gb_company ");
					 vatrate = Double.parseDouble(vatvalue)/100; 
				//	 System.out.println("vatvalue:  "+vatvalue+"  vatrate: "+vatrate);
					 vatamount = VatableCost*vatrate;
					 CostPrice = VatableCost + (VatableCost*vatrate);
				//	 System.out.println("CostPrice:  "+CostPrice+" vatamount:"+vatamount);
				 }*/
				 if((!SubjectTOVat.equalsIgnoreCase(null)) && (!SubjectTOVat.equalsIgnoreCase("")) && (SubjectTOVat.equalsIgnoreCase("Y"))){				
					 vatrate =  vatvalue/100;
					 vatamount = VatableCost*vatrate;
					 CostPrice = VatableCost + (VatableCost*vatrate);
					 System.out.println("vatamount:  "+vatamount+"   vatvalue: "+vatvalue+"   vatrate: "+vatrate+"   SubjectTOVat: "+SubjectTOVat);
				 }					 
				 System.out.println("WhTax:  "+WhTax);
				 if((!WhTax.equalsIgnoreCase(null)) && (!WhTax.equalsIgnoreCase(""))  && (!WhTax.equalsIgnoreCase("N"))){				
					 whtaxamount =  VatableCost*(whtaxvalue/100);
					 System.out.println("whtaxamount:  "+whtaxamount+"   whtaxvalue: "+whtaxvalue+"    WhTax: "+WhTax+"  VatableCost: "+VatableCost);
				 }		
				 System.out.println("errorMessage Before Processing: "+errorMessage);
				 if(errorMessage.equalsIgnoreCase("")){
		//		 System.out.println("assetexist Before Processing: "+integrifyIdexist+"  oldintegrify: "+oldintegrify+"  integrifyId: "+integrifyId);
				 //if((!assetexist.equalsIgnoreCase("")) && (oldintegrify==integrifyId)){
//					 if(!integrifyIdexist.equalsIgnoreCase("")){					 
//				      assetstatus = "C";
//				      errorMessage = "Asset already Posted ";
//				      boolean done = comp.newassetinterface(errorMessage, integrifyId,assetstatus,assetId,assetCode);
//			//	      System.out.println("Record Not Successfully Updated because assetexist 2>>> "+integrifyIdexist+" errorMessage: "+errorMessage);
//				 }
//				      else{
//					 
//				 System.out.println("Posted: "+posted+"  Invoice No: "+invoiceNo+" assettype: "+assettype+"  tranType: "+tranType);
				 if(tranType.equalsIgnoreCase("P") && noofitems==0){

				// String branch_id = comp.findObject("SELECT BRANCH_ID FROM am_ad_branch WHERE BRANCH_CODE = '"+branchCode+"'");
				 String section_id = comp.findObject("SELECT SECTION_ID FROM am_ad_section WHERE SECTION_CODE = '"+sectionCode+"'");
				 String dept_id = comp.findObject("SELECT DEPT_ID FROM am_ad_department WHERE DEPT_CODE = '"+deptCode+"'");
				 String cat_id = comp.findObject("SELECT CATEGORY_ID FROM am_ad_category WHERE CATEGORY_CODE = '"+categoryCode+"'");
				 //String sub_cat_id = comp.findObject("SELECT SUB_CATEGORY_ID FROM AM_AD_SUB_CATEGORY WHERE SUB_CATEGORY_CODE = '"+subcategoryCode+"'");
				 String sub_cat_id = "0";
//				 System.out.println("<<<<=====sub_cat_id: "+sub_cat_id+"    subcategoryCode: "+subcategoryCode);
				 String invoice_confirm = comp.findObject("SELECT *FROM AM_INVOICE_NO WHERE INVOICE_NO = '"+invoiceNo+"'");
				// String lpo_confirm = comp.findObject("SELECT *FROM AM_INVOICE_NO WHERE LPO = '"+lpo+"'");
//				 System.out.println("branch_confirm>>>>>> "+invoice_confirm+"  section_confirm:  "+section_id+" dept_confirm: "+dept_id
	//					 +" cat_confirm: "+cat_id+" invoice_confirm: "+invoice_confirm);
				 if(branch_id.equalsIgnoreCase("")){errorMessage = errorMessage + " Invalid Branch : ";}
				 if(section_id.equalsIgnoreCase("")){errorMessage = errorMessage + " Invalid Section : ";}
				 if(dept_id.equalsIgnoreCase("")){errorMessage = errorMessage + " Invalid Department : ";}
				 if(cat_id.equalsIgnoreCase("")){errorMessage = errorMessage + " Invalid Category : ";}
				// if(!lpo_confirm.equalsIgnoreCase("")){errorMessage = errorMessage + "LPO Number Already Used : ";}
				 if((!invoice_confirm.equalsIgnoreCase("")) && (noofitems==0)){errorMessage = errorMessage + " Invoice Number already Used ";}
				// if((!lpo_confirm.equalsIgnoreCase("")) && (noofitems==0)){errorMessage = errorMessage + " LPO Number already Used ";}
//				 System.out.println("errorMessage>>>>>> "+errorMessage+"   invoice_confirm>>>>> "+invoice_confirm);
//				 System.out.println("Asset errorMessage>>>>>> "+errorMessage+" Asset Status>>>>>> "+AssetStatus);
			if(errorMessage.equalsIgnoreCase("")){
//				System.out.println("Asset Status>>>>>> "+AssetStatus);
//				System.out.println("For Individual supervisor>>>>>> "+supervisor+"  UserID: "+UserID+"  noofitems: "+noofitems);
				if(noofitems==0){
				String groupId = "0";
				integrifyIdexist = comp.findObject("SELECT INTEGRIFY FROM AM_ASSET WHERE INTEGRIFY = '"+integrifyId+"'");
//				System.out.println("Asset integrifyIdexist>>>>>> "+integrifyIdexist);
				if(integrifyIdexist.equalsIgnoreCase("")){
//					System.out.println("Before Statusx>>>>>> "+statux);
				 statux = comp.insertAssetRecord(integrifyId,Description,RegistrationNo,VendorAC,Datepurchased,
						AssetMake,AssetModel,AssetSerialNo,AssetEngineNo,SupplierName,AssetUser,AssetMaintenance,
						CostPrice,VatableCost,AuthorizedBy,WhTax,whtaxamount,PostingDate,EffectiveDate,PurchaseReason,SubjectTOVat,AssetStatus,
						State,Driver,UserID,branchCode,sectionCode,deptCode,categoryCode,subcategoryCode,barCode,sbuCode,lpo,invoiceNo,
						supervisor,posted,assetId,assetCode,assettype,branch_id,dept_id,section_id,cat_id,sub_cat_id,vatamount,residualvalue,whtaxrate,
						location,memovalue,memo,spare1,spare2,spare3,spare4,spare5,spare6,multiple,projectCode,groupId,delimiter);
				 		errorMessage=""; assetstatus = "P";
						comp.newassetinterface(errorMessage, integrifyId,assetstatus,assetId,assetCode);
//						System.out.println("Asset Statusx>>>>>> "+statux);
//						String infoScript = "SELECT ASSET_ID, ASSET_CODE FROM "+tablename+" WHERE INTEGRIFY = '"+integrifyId+"'";
//						System.out.println("Asset infoScript>>>>>> "+infoScript);
				 if(statux == 0 )
				 { 
					 infofld = comp.findtwoinfo("SELECT ASSET_ID, ASSET_CODE FROM "+tablename+" WHERE INTEGRIFY = '"+integrifyId+"'");
//					 System.out.println("Information fld: "+infofld);
					 String []others = infofld.split(",");
					 assetId = others[0];
					 assetCode = others[1];
					// String msgText11 ="Asset with ID: "+ assetId +" is waiting for your approval.";	
//				 System.out.println("ASSET ID: "+assetId+"  Statux:  "+statux+" assetCode: "+assetCode+" LPO: "+lpo+" invoiceNo: "+invoiceNo+" Trans Type: "+transtype);
				 comp.insToAm_Invoice_No(assetId, lpo, invoiceNo, transtype,integrifyId);
//				 System.out.println("ASSET Successfully Posted>>> ");
			//	if(!assetId.equalsIgnoreCase("")){errorMessage="Successfully Posted"; assetstatus = "Y";
					//		  comp.newassetinterface(errorMessage, integrifyId,assetstatus,assetId,assetCode);
						//	  System.out.println("Record Successfully Updated 1>>> ");
			//				 }
			}
				  if(numOfTransactionLevel == 0)
//					  System.out.println("numOfTransactionLevel: "+numOfTransactionLevel);
					  
				  {
					  if(comp.updateNewAssetStatux(assetId, tablename))
					  {
//						  System.out.println("updateNewAssetStatux: ");
						  comp.setPendingTrans(comp.setApprovalData(assetId,assettype),"27",Integer.parseInt(assetCode));
						  String lastMTID = comp.getCurrentMtid("am_asset_approval");
						  comp.setPendingTransArchive(comp.setApprovalData(assetId,assettype),"27",Integer.parseInt(lastMTID),Integer.parseInt(assetCode));
						  boolean b = comp.updateNewApprovalAssetStatus(assetId,Integer.parseInt(UserID));
						  
//						  System.out.println("assetRaiseEntry: "+assetRaiseEntry);
							if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("Y")){
						    	if(assettype.equalsIgnoreCase("C")){								
						    		url = "DocumentHelp.jsp?np=updateAsset&amp;id="+assetId+"&pageDirect=Y";
						    	}
						    	if(assettype.equalsIgnoreCase("P")){						    	
						    		url = "DocumentHelp.jsp?np=updateAssetBranch&amp;id="+assetId+"&pageDirect=Y";
						    	}
							  String flag= "";
							  String partPay="";	
							  
							  String branchName= comp.findObject(" SELECT branch_name from am_ad_branch where branch_code='"+branchCode+"'");
							  //String description= ad.getDescription();
							  String subjectT= comp.subjectToVat(assetId);
							  String whT= comp.whTax(assetId);
//							  System.out.println("Supervisor Name: "+approvedbyName+"  User Id: "+Integer.parseInt(supervisor));
						//	  comp.insertApprovalx(assetId, Description, page1, flag, partPay,approvedbyName,branchName,subjectT,whT,url,lastMTID,Integer.parseInt(assetCode));
								}
//							  if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("N")){
								  comp.updateAssetStatusChange("update "+tablename+" set asset_status='ACTIVE',PART_PAY = 'Y' where asset_id='"+assetId+"'");
								  comp.updateAssetStatusChange("update am_asset_archive set asset_status='ACTIVE' where asset_id='"+assetId+"'");
				//				  comp.updateAssetStatusChange("update am_asset_approval set process_status='A', asset_status='ACTIVE' where asset_id='"+assetId+"'");
				//				  comp.updateAssetStatusChange("update am_asset_approval_archive set process_status='A', asset_status='ACTIVE'where asset_id='"+assetId+"'");
//								  }
								
								  comp.updateRaiseEntry(assetId);	
								  
									String[] mailSetUp = comp.getEmailStatusAndName("asset creation");
									String Status1 = mailSetUp[0];
									String mail_code = mailSetUp[1];
			//						System.out.println("#mail_code: "+mail_code);
									Status1 = Status1.trim();
									if(Status1.equalsIgnoreCase("Approved"))
									{ 
								 
									String transaction_type="Asset Creation";
									String subject ="Asset Creation";
									
									//Codes message= new Codes();
																		
									String to = comp.MailTo(mail_code, transaction_type);  //retrieves recipients from database

									String msgText1 =comp.MailMessage(mail_code, transaction_type);//"New asset with ID: "+id +" was successfully created.";
				//					System.out.println("#$$$$$$$$$$$ "+to);
									comp.sendMail(to,subject,msgText1);	
									}
									errorMessage=" Successfully Created "; assetstatus = "Y";
									comp.newassetinterface(errorMessage, integrifyId,assetstatus,assetId,assetCode);
					  }
				  }
				}else{
				      assetstatus = "C";
				      errorMessage = errorMessage+"Asset Already Created; ";
				      boolean done = comp.newassetinterface(errorMessage, integrifyId,assetstatus,assetId,assetCode);	
				      }
				
			}	
			
				 }
			else{
				 assetstatus = "C";
				 errorMessage = errorMessage+"Asset Has Error; ";
				boolean done = comp.newassetinterface(errorMessage, integrifyId,assetstatus,assetId,assetCode);
			//	System.out.println("Record Successfully Updated 2>>> ");
				}
			
			}else{
				//Improvement Transaction Postings TranType is I (meaning Improvements)
				 System.out.println("About to Start Part Payment: "+assetId+"   noofitems: "+noofitems+"     tranType: "+tranType+"    errorMessage: "+errorMessage);
				 if(tranType.equalsIgnoreCase("P") && noofitems!=0 && errorMessage.equalsIgnoreCase("")){
					 if(!assetId.equalsIgnoreCase("")){
					 errorMessage = "";
					 System.out.println("Inside Improvement Transaction Posting");
					 String availableForTrans = comp.checkAssetAvalability(assetId);
					 System.out.println("availableForTrans: "+availableForTrans);
					 if(availableForTrans.equalsIgnoreCase("")) {
					 
					 assetCode =comp.getCodeName("select asset_code from am_asset where asset_id = '"+assetId+"'");

					 String raiseEntry = "N";
					  //String vendorAcct = "";
					  double cost = 0;
					  double vatAmt = 0;
					  double whtAmt = 0;
					  String revalueReason = "";
					  String revDate = "";
					  double vatableCost = 0;
					  double nbv = 0;
					  double accumDep = 0;
					  String effDate = "";
			//		  System.out.println("Initializing 1");
					  //old declaration
					  double oldCost = 0;
					  double oldVatAmt = 0;
					  double oldWhtAmt = 0;
					  double oldVatableCost = 0;
					  double oldNbv = 0;
					  double oldAccumDep = 0;
					  String oldVendorAcct="";
				//	  System.out.println("Initializing 2");
					  //new declaration
					  double newCost = 0;
					  double newCostPrice = 0.00;
					  double newVatAmt = 0;
					  double newWhtAmt = 0;
					  double newVatableCost = 0;
					  double newNbv = 0;
					  double newAccumDep = 0;
					  double payment = 0.00;
			          String initiatorId = "0";
			          String mtid = "";
					//  java.util.Date date = new java.util.Date();
					  Date date = new Date();

					  String postingDate = df.formatDate(new Date()); 
					  String year = postingDate.substring(0, 4);
					  String month = postingDate.substring(4, 6);
					  String day = postingDate.substring(6);
					  postingDate = year+"-"+month+"-"+day;
						 if((!SubjectTOVat.equalsIgnoreCase(null)) && (!SubjectTOVat.equalsIgnoreCase("")) && (SubjectTOVat.equalsIgnoreCase("Y"))){				
							 vatrate =  vatvalue/100;
							 vatamount = VatableCost*vatrate;
							 CostPrice = VatableCost + (VatableCost*vatrate);
							 System.out.println("vatamount:  "+vatamount);
						 }					 
						 System.out.println("WhTax:  "+WhTax);
						 if((!WhTax.equalsIgnoreCase(null)) && (!WhTax.equalsIgnoreCase(""))  && (!WhTax.equalsIgnoreCase("N"))){
							 whtrate = whtaxvalue/100;
							 whtaxamount =  VatableCost*(whtaxvalue/100);
//							 System.out.println("whtaxamount:  "+whtaxamount);
						 }	
//					  System.out.println("Initializing 3");
					  magma.asset.dto.Asset asset = (magma.asset.dto.Asset)comp.getAsset(assetId);
					  oldCost = asset.getCost();
					  oldVatAmt = asset.getVatAmt();
					  oldWhtAmt = asset.getWhtAmt();
					  oldVatableCost = asset.getVatableCost();
					  oldNbv = asset.getNbv();
					  oldAccumDep = asset.getAccumDep();
					  oldVendorAcct = asset.getVendorAcct();
				//	  System.out.println("old Cost for Initializing 4: "+oldVendorAcct);
					   //old assignment
					   if(fromRepost != null && fromRepost.equalsIgnoreCase("y")){
						   magma.asset.dto.Improvement revalue = (magma.asset.dto.Improvement)comp.getMaintenanceAsset(assetId);							   
						   oldCost = revalue.getOldCost();
						   oldVatAmt = revalue.getOldVatAmt();
						   oldWhtAmt = revalue.getOldWhtAmt();
						   oldVatableCost = revalue.getOldVatableCost();
						   oldNbv = revalue.getOldNbv();
						   oldAccumDep = revalue.getOldAccumDep();
					   }
					  //int numOfTransactionLevel0 =  comp.getNumOfTransactionLevel("29");
					  //double nbv = 0;
					  //double accumDep = 0;
					   //new assignment
					   newCost = asset.getCost();
					   newVatAmt = asset.getVatAmt();
					   newWhtAmt = asset.getWhtAmt();
					   newVatableCost = asset.getVatableCost();
					   newNbv = asset.getNbv();
					   newAccumDep = asset.getAccumDep();
					   String supplier_nameOld = SupplierName;
					   String vendorAcctOld = VendorAC;
					   String supplier_name = SupplierName;						   
					   String costPrice = new Double(VatableCost).toString();
//					   System.out.println("supervisor for improvement: "+supervisor);
					   String[] pa = new String[11];
					     pa[0]=assetId; 
						 pa[1]= UserID; 
						 pa[2]= supervisor; 
						 pa[3]=costPrice; 
						 pa[4]= EffectiveDate;
					 	 pa[5]= Description; 
						 pa[6]= EffectiveDate; 
						 pa[8]="ACTIVE"; 
						 pa[9]="Asset Part Payment"; 
						 pa[10]="P"; 
//						 System.out.println("numOfTransactionLevel Try 1 End "+numOfTransactionLevel);
						 if(invoiceNo != null || lpo != null){
						 comp.insToAm_Invoice_No(assetId,lpo,invoiceNo,"Asset Part Payment",integrifyId); }
							newCost = CostPrice;
							newCostPrice = oldCost+CostPrice;
							newVatAmt = oldVatAmt+vatamount;
							//newVatAmt = vatamount;
							newWhtAmt = oldWhtAmt+whtaxamount;
							//newWhtAmt = whtaxamount;
							newVatableCost = oldVatableCost+VatableCost;
							//newVatableCost = VatableCost;
//							System.out.println("Vatable Cost: "+VatableCost+"  new Vatable Cost: "+newVatableCost+"  Cost Price: "+CostPrice);
							newNbv = CostPrice+oldNbv;		
//							payment = newCost+vatamount;
						// numOfTransactionLevel=0;//test
						 if(numOfTransactionLevel==0){
						//	 pa[2]=UserID;
						//	 pa[10]="A";
							 System.out.println("Initializing 4");
						//	 String mtidexist = comp.findObject("SELECT count(*) FROM AM_ASSET_PAYMENT where ASSET_ID = '"+assetId+"' AND TRANID = " + mtid);
							 mtid =  comp.setPendingTrans2(pa,"11",Integer.parseInt(assetCode));	
							 String mtidexist = comp.findObject("SELECT count(*) FROM AM_ASSET_PAYMENT where ASSET_ID = '"+assetId+"' AND TRANID = '"+mtid+"' ");
//							 System.out.println("EffectiveDate >>>>>>>>>>> " + EffectiveDate+"   mtid: "+mtid+"    J Loop: "+j);  
//							System.out.println("About to Insert Record into Table am_asset_improvement with usefullife: "+usefullife);
//							 int b = comp.insertAssetMaintananceNoSupervisor(newCost,newNbv,assetId,CostPrice,PurchaseReason,EffectiveDate,
//									 Integer.parseInt(UserID),VatableCost,vatamount,whtaxamount,VendorAC,assetRaiseEntry,accumDep,oldCost,oldVatableCost,oldVatAmt,
//									 oldWhtAmt,oldNbv,oldAccumDep,EffectiveDate,mtid,WhTax, whtaxrate,SubjectTOVat,supplier_name,
//									 supplier_nameOld,vendorAcctOld,Description,categoryCode,subcategoryCode,branchCode,lpo,invoiceNo,newCost,Integer.parseInt(assetCode),
//									 newCostPrice,newVatAmt,newWhtAmt,newVatableCost,newNbv,integrifyId,usefullife,sbuCode);
//							 	String revalue_query = "update am_asset_improvement set approval_Status='ACTIVE' where revalue_id = '"+mtid+"'";
////							 	System.out.println("revalue_query:  "+revalue_query);
//							 	comp.updateAssetStatusChange(revalue_query);
//							String Test = "SELECT count(*) FROM AM_ASSET_PAYMENT where ASSET_ID = '"+assetId+"' AND TRANID = " + mtid;
//							 System.out.println("=======Test: "+Test);
//							 String mtidexist = comp.findObject("SELECT count(*) FROM AM_ASSET_PAYMENT where ASSET_ID = '"+assetId+"' AND TRANID = " + mtid);
//							 System.out.println("=======mtidexist:  "+mtidexist+"    mtid: "+mtid);
							 if(mtidexist.equals("0")){
//								 System.out.println("=======Inside mtidexist test:  ");
							adp.createPaymentRecord( assetId,  CostPrice,postingDate, "Y", Description,Integer.parseInt(mtid),Integer.parseInt(assetCode),initiatorId,vatrate,whtrate,vatamount,whtaxamount,VatableCost);
						 }
							 mtidexist = "";
//							System.out.println("About to Insert Record into Table AM_ASSET_PAYMENT on assetId: "+assetId);
								 String[] raiseEntryInfo = comp.raiseEntryInfo(assetId);
								 
						           //String description = raiseEntryInfo[0];
						           branch_id = raiseEntryInfo[1];
						           String subject_to_vat = raiseEntryInfo[2];
						           String wh_tax = raiseEntryInfo[3];
						           String flag = "";  
						           String partPay = "";
						           String pidId = comp.findObject("SELECT PAY_ID FROM AM_ASSET_PAYMENT where TRANID = " + mtid);
						           String asset_User_Name = comp.findObject("SELECT full_name from am_gb_user where user_id = " + supervisor);
						           String branchName = comp.findObject(" SELECT branch_name from am_ad_branch where branch_id= " + Integer.parseInt(branch_id) );
						           page1 = "ASSET PART PAYMENT RAISE ENTRY";
						           url = "DocumentHelp.jsp?np=PaymentDisplay&id=" +assetId + "&pid="+mtid + "&pageDirect=Y" +"&tranId="+mtid + "&assetCode="+assetCode;
//						           System.out.println("################################## raise entry: "+assetRaiseEntry);	
						           comp.updateAssetStatusChange("update am_asset_approval set process_status='A', asset_status='ACTIVE' where asset_id='"+assetId+"' and transaction_id = '"+mtid+"'");
						           
						     	  if(assetRaiseEntry.equalsIgnoreCase("Y")){
						     		  	comp.insertApproval(assetId, Description, page1, flag, partPay,asset_User_Name,branchName,subject_to_vat,wh_tax,url,mtid,Integer.parseInt(assetCode));
						     		  }
						     		  comp.updateRaiseEntry(assetId);
//						     		System.out.println("################################## finished raise entry");
						     		 //THE SEGMENT ENDS RAISE ENTRY FOR NEW ASSET CREATION THAT DOESN'T REQUIRE APPROVAL

						     		 errorMessage = "Asset Part Payment successful"; assetstatus = "Y";
						     		 boolean done = comp.newassetinterface(errorMessage, integrifyId,assetstatus,assetId,assetCode);
						//	 }  MTIDExist
						 }	
						 else{
							 System.out.println("Improvement Requires Approval >>> ");
							 if(fromRepost != null && fromRepost.equalsIgnoreCase("y")){
								 //comp.updateRepostInfo(Integer.parseInt(mtidRepost));
								 
								 }
							 mtid  =  comp.setPendingTrans2(pa,"11",Integer.parseInt(assetCode));
//							  System.out.println("EffectiveDate >>>>>>>>>>> " + EffectiveDate+"   mtid: "+mtid+"   Payment Approval");  
							 // int result = assetMan.insertAssetMaintanance(cost,nbv,id,0.0,reason,revDate,userId,vatableCost,vatAmt,whtAmt,vendorAcct,raiseEntry,accumDep,oldCost,oldVatableCost,oldVatAmt,oldWhtAmt,oldNbv,oldAccumDep,effDate,mtid,wh_tax_cb, Integer.parseInt(selectTax),subject2vat);
//							 int result = comp.insertAssetMaintanance(CostPrice,nbv,assetId,CostPrice,PurchaseReason,EffectiveDate,Integer.parseInt(UserID),VatableCost,vatamount,whtaxamount,VendorAC,raiseEntry,accumDep,oldCost,oldVatableCost,oldVatAmt,oldWhtAmt,oldNbv,oldAccumDep,EffectiveDate,mtid,WhTax, whtaxrate,SubjectTOVat,newCost,newVatableCost,newNbv,newVatAmt,newWhtAmt,supplier_name,supplier_nameOld,vendorAcctOld,Description,categoryCode,branchCode,lpo,invoiceNo,Integer.parseInt(assetCode),integrifyId,usefullife,sbuCode);
//							 String revalue_query = "update am_asset_improvement set approval_Status='PENDING' where revalue_id = '"+mtid+"'";
//							 System.out.println("revalue_query >>>>>>>>>>> " + revalue_query);
//							comp.updateAssetStatusChange(revalue_query);
							subjectr ="Asset Part Payment Approval";
							String msgText11 ="Asset with ID: "+ assetId +" is waiting for your approval.";
							comp.sendMailSupervisor(supervisor, subjectr, msgText11);								
				     		 errorMessage = "Transaction submitted for approval"; assetstatus = "Y";					     		
				     		 boolean done = comp.newassetinterface(errorMessage, integrifyId,assetstatus,assetId,assetCode);
						 }
			//      System.out.println("Record Successfully Updated 3>>> ");
					 }
					 else{
					      assetstatus = "C";
					      errorMessage = "Part Payment has been done on this asset this month. Cannot be initiated for Part Payment OR Asset is still pending for approval. Cannot be initiated for Part Payment";
					      boolean done = comp.newassetinterface(errorMessage, integrifyId,assetstatus,assetId,assetCode);
					//      System.out.println("Record Successfully Updated 3>>> ");
					 }						 
				 }
					 else{
					      assetstatus = "C";
					      errorMessage = "Asset Id Can not be Null or empty ";
					      boolean done = comp.newassetinterface(errorMessage, integrifyId,assetstatus,assetId,assetCode);
					//      System.out.println("Record Successfully Updated 3>>> ");
					 }						 
			 }
				 else{
				      assetstatus = "C";
				      errorMessage = errorMessage+"Record Already Processed; ";
				      boolean done = comp.newassetinterface(errorMessage, integrifyId,assetstatus,assetId,assetCode);	
				      }						 
			 } //End of Improvement
			 }
			 else{
			      assetstatus = "C";
			      errorMessage = errorMessage+"Asset Already has Error; ";
			      boolean done = comp.newassetinterface(errorMessage, integrifyId,assetstatus,assetId,assetCode);	
			      }			 
				 //else() Integrify Test

	     }catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	    	 //comp.sendPost(smsdescription,phoneNo,schoolName);
			 oldintegrify=integrifyId;
			errorMessage = "";
//			 System.out.println("====List Size: "+j);
			 j++;
//			 System.out.println("====List Size After: "+j);
	     }	

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    
    
    //fetch today records from am_raisentry_transaction where iso not equals 000
    //compare record against finacle iso values
    //if 000 update am_raisentry_transaction with value
    

    
    
}

