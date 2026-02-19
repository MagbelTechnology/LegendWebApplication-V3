package com.magbel.legend.servlet;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import magma.AssetRecordsBean;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;
   
public class AssetRegisterExport extends HttpServlet
{
	private EmailSmsServiceBus mail ;
	private AssetRecordsBean ad;
	private ApprovalRecords records;
   public void doPost(HttpServletRequest request, 
    HttpServletResponse response)
      throws ServletException, IOException
     
   {
	 String userClass = (String) request.getSession().getAttribute("UserClass");
	 String userId = (String) request.getSession().getAttribute("CurrentUser");
	 
	 if (!userClass.equals("NULL") || userClass!=null){
//	   PrintWriter out = response.getWriter();
//    OutputStream out = null; 
	mail= new EmailSmsServiceBus();
	records = new ApprovalRecords();
    String branch_Code = request.getParameter("initiatorSOLID");
    String branch_Id = request.getParameter("branch");
    String FromDate = request.getParameter("FromDate");
    String ToDate = request.getParameter("ToDate");
    if(!FromDate.equals("")){
	String yyyy = FromDate.substring(6, 10);
	System.out.println("======>yyyy: "+yyyy);
	String mm = FromDate.substring(3, 5);
	System.out.println("======>mm: "+mm);
	String dd = FromDate.substring(0, 2);
	FromDate = yyyy+"-"+mm+"-"+dd;
    }
    System.out.println("======>FromDate: "+FromDate);
    if(!ToDate.equals("")){
	String Tyyyy = ToDate.substring(6, 10);
	System.out.println("======>Tyyyy: "+Tyyyy);
	String Tmm = ToDate.substring(3, 5);
	System.out.println("======>Tmm: "+Tmm);
	String Tdd = ToDate.substring(0, 2);
	ToDate = Tyyyy+"-"+Tmm+"-"+Tdd;
    }
    System.out.println("======>ToDate: "+ToDate);
    String dept_Code = request.getParameter("deptCode");
    String asset_Id = request.getParameter("assetId");
    String branchIdNo = records.getCodeName("select BRANCH from am_gb_User where USER_ID = ? ",userId);
    //String branchCode = request.getParameter("BRANCH_CODE");
    String userName = records.getCodeName("select USER_NAME from am_gb_User where USER_ID = ? ",userId);
    System.out.println("<<<<<<branch_Id: "+branch_Id);
    String branchCode = "";
    if(!branchIdNo.equals("0")){
    	branchCode = records.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID = ? ",branchIdNo);
    }
    System.out.println("<<<<<<branch_Code: "+branch_Code);
//    String userName = request.getParameter("userName");
    String fileName = branchCode+"By"+userName+"AssetRegisterReport.xlsx";    	
    String filePath = System.getProperty("user.home")+"\\Downloads";
    System.out.println("<<<<<<filePath: "+filePath);
	File tmpDir = new File(filePath);
	boolean exists = tmpDir.exists();	
/*	if(exists==true){
		File f1 = new File(filePath);
		if (f1.delete()) {
		    System.out.println("File " + f1.getName() + " is deleted.");
		} else {
		    System.out.println("File " + f1.getName() + " not found.");
		}
	}*/
	response.setIntHeader("Content-Length", -1);
    response.setContentType("application/octet-stream");
    response.setHeader("Content-Disposition", 
   "attachment; filename="+fileName+"");
    try
    {
    	ad = new AssetRecordsBean();
 
//    if(exists==false){   	

        String categoryCode = request.getParameter("category");
     Report rep = new Report();
   System.out.println("<<<<<<branch_Id: "+branch_Id+"    categoryCode: "+categoryCode+"  branchCode: "+branchCode);
     String ColQuery = "";
     if(!branch_Id.equals("0")  && !categoryCode.equals("0") && FromDate.equals("")  && ToDate.equals("")){
    	 System.out.println("======>>>>>>>Branch and Category Selected: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  dept_Code: "+dept_Code+"   asset_Id: "+asset_Id);	     
	     ColQuery ="SELECT company_name,old_threshhold, Asset_id, Description, BRANCH_CODE, BRANCH_NAME, category_name, Accum_Dep,"
	 + "Monthly_Dep, Cost_Price, NBV, IMPROV_COST, IMPROV_ACCUMDEP, IMPROV_MONTHLYDEP, IMPROV_NBV,"
	 + "Cost_Price + IMPROV_COST AS Total_Cost_Price, TOTAL_NBV, Date_purchased, Dep_rate AS am_ad_category_Dep_rate, Accum_Dep_ledger," 
     + "Dep_ledger, Asset_Ledger, gl_account, Dep_End_Date, Effective_Date, Posting_Date, Dept_name, Asset_User,"
     + "Sbu_name, BRANCH_CODE AS am_asset_BRANCH_CODE, BAR_CODE, Asset_Serial_No, Spare_1, Spare_2, SPARE_3, SPARE_4,"
     + "SPARE_5, SPARE_6, Purchase_Reason, Registration_No, Asset_Make, Asset_Model, Asset_Engine_No, Supplier_Name, Wh_Tax,"
     + "Wh_Tax_Amount, Req_Depreciation, Vat, Subject_TO_Vat, Asset_Status, State, Driver, Vendor_AC, Branch_ID, Dept_ID,"
     + "Category_ID, Section_id, Dep_Rate, Asset_Maintenance, Residual_Value, Authorized_By, Req_Redistribution, Useful_Life,"
     + "Total_Life, location, Remaining_Life, Vatable_Cost, Who_TO_Rem, Email1, Who_To_Rem_2, Email2, Raise_Entry,"
     + "Dep_Ytd, Section, User_ID, Date_Disposed, PROVINCE, Multiple, WAR_START_DATE, WAR_MONTH, WAR_EXPIRY_DATE," 
     + "Last_Dep_Date, SECTION_CODE, DEPT_CODE, CATEGORY_CODE, AMOUNT_PTD, AMOUNT_REM, PART_PAY, FULLY_PAID, "
     + "GROUP_ID, SBU_CODE, LPO, supervisor, defer_pay, OLD_ASSET_ID, WHT_PERCENT, Post_reject_reason, Finacle_Posted_Date, "
     + "system_ip, mac_address, asset_code, memo, memovalue, state_name, Section_Name, Vendor_Name"
     + " FROM AssetRegister"
     + " WHERE Asset_Status = 'ACTIVE' and Cost_Price > (old_threshhold-0.01) AND branch_id = ? AND CATEGORY_CODE = ?"
     + " ORDER BY BRANCH_CODE ASC,CATEGORY_CODE ASC ";	
	}      
	 if(branch_Id.equals("0")  && !categoryCode.equals("0") && FromDate.equals("")  && ToDate.equals("")){	   
//	   System.out.println("======>>>>>>>Category Selected: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  dept_Code: "+dept_Code+"   asset_Id: "+asset_Id);
     ColQuery ="SELECT company_name,Cost_Threshold, Asset_id, Description, BRANCH_CODE, BRANCH_NAME, category_name, Accum_Dep,"
	 + "Monthly_Dep, Cost_Price, NBV, IMPROV_COST, IMPROV_ACCUMDEP, IMPROV_MONTHLYDEP, IMPROV_NBV,"
	 + "Cost_Price + IMPROV_COST AS Total_Cost_Price, TOTAL_NBV, Date_purchased, Dep_rate AS am_ad_category_Dep_rate, Accum_Dep_ledger," 
	 + "Dep_ledger, Asset_Ledger, gl_account, Dep_End_Date, Effective_Date, Posting_Date, Dept_name, Asset_User,"
	 + "Sbu_name, BRANCH_CODE AS am_asset_BRANCH_CODE, BAR_CODE, Asset_Serial_No, Spare_1, Spare_2, SPARE_3, SPARE_4,"
	 + "SPARE_5, SPARE_6, Purchase_Reason, Registration_No, Asset_Make, Asset_Model, Asset_Engine_No, Supplier_Name, Wh_Tax,"
	 + "Wh_Tax_Amount, Req_Depreciation, Vat, Subject_TO_Vat, Asset_Status, State, Driver, Vendor_AC, Branch_ID, Dept_ID,"
	 + "Category_ID, Section_id, Dep_Rate, Asset_Maintenance, Residual_Value, Authorized_By, Req_Redistribution, Useful_Life,"
	 + "Total_Life, location, Remaining_Life, Vatable_Cost, Who_TO_Rem, Email1, Who_To_Rem_2, Email2, Raise_Entry,"
	 + "Dep_Ytd, Section, User_ID, Date_Disposed, PROVINCE, Multiple, WAR_START_DATE, WAR_MONTH, WAR_EXPIRY_DATE," 
	 + "Last_Dep_Date, SECTION_CODE, DEPT_CODE, CATEGORY_CODE, AMOUNT_PTD, AMOUNT_REM, PART_PAY, FULLY_PAID, "
	 + "GROUP_ID, SBU_CODE, LPO, supervisor, defer_pay, OLD_ASSET_ID, WHT_PERCENT, Post_reject_reason, Finacle_Posted_Date, "
	 + "system_ip, mac_address, asset_code, memo, memovalue, state_name, Section_Name, Vendor_Name"
	 + " FROM AssetRegister"
	 + " WHERE Asset_Status = 'ACTIVE' and Cost_Price > (old_threshhold-0.01) AND CATEGORY_CODE = ? "
	 + " ORDER BY BRANCH_CODE ASC,CATEGORY_CODE ASC ";     
   }
	 if(!branch_Id.equals("0")  && categoryCode.equals("0") && FromDate.equals("")  && ToDate.equals("")){	   
	   System.out.println("======>>>>>>>Branch Selected: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  dept_Code: "+dept_Code+"   asset_Id: "+asset_Id);
	     ColQuery ="SELECT company_name,Cost_Threshold, Asset_id, Description, BRANCH_CODE, BRANCH_NAME, category_name, Accum_Dep,"
	 + "Monthly_Dep, Cost_Price, NBV, IMPROV_COST, IMPROV_ACCUMDEP, IMPROV_MONTHLYDEP, IMPROV_NBV,"
	 + "Cost_Price + IMPROV_COST AS Total_Cost_Price, TOTAL_NBV, Date_purchased, Dep_rate AS am_ad_category_Dep_rate, Accum_Dep_ledger," 
     + "Dep_ledger, Asset_Ledger, gl_account, Dep_End_Date, Effective_Date, Posting_Date, Dept_name, Asset_User,"
     + "Sbu_name, BRANCH_CODE AS am_asset_BRANCH_CODE, BAR_CODE, Asset_Serial_No, Spare_1, Spare_2, SPARE_3, SPARE_4,"
     + "SPARE_5, SPARE_6, Purchase_Reason, Registration_No, Asset_Make, Asset_Model, Asset_Engine_No, Supplier_Name, Wh_Tax,"
     + "Wh_Tax_Amount, Req_Depreciation, Vat, Subject_TO_Vat, Asset_Status, State, Driver, Vendor_AC, Branch_ID, Dept_ID,"
     + "Category_ID, Section_id, Dep_Rate, Asset_Maintenance, Residual_Value, Authorized_By, Req_Redistribution, Useful_Life,"
     + "Total_Life, location, Remaining_Life, Vatable_Cost, Who_TO_Rem, Email1, Who_To_Rem_2, Email2, Raise_Entry,"
     + "Dep_Ytd, Section, User_ID, Date_Disposed, PROVINCE, Multiple, WAR_START_DATE, WAR_MONTH, WAR_EXPIRY_DATE," 
     + "Last_Dep_Date, SECTION_CODE, DEPT_CODE, CATEGORY_CODE, AMOUNT_PTD, AMOUNT_REM, PART_PAY, FULLY_PAID, "
     + "GROUP_ID, SBU_CODE, LPO, supervisor, defer_pay, OLD_ASSET_ID, WHT_PERCENT, Post_reject_reason, Finacle_Posted_Date, "
     + "system_ip, mac_address, asset_code, memo, memovalue, state_name, Section_Name, Vendor_Name"
     + " FROM AssetRegister"
     + " WHERE Asset_Status = 'ACTIVE' and Cost_Price > (old_threshhold-0.01) AND branch_id = ? "
     + " ORDER BY BRANCH_CODE ASC,CATEGORY_CODE ASC ";
	}
   if(branch_Id.equals("0")  && categoryCode.equals("0") && FromDate.equals("")  && ToDate.equals("")){
	   System.out.println("======>>>>>>>No Selection: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  dept_Code: "+dept_Code+"   asset_Id: "+asset_Id);
     ColQuery ="SELECT company_name,Cost_Threshold, Asset_id, Description, BRANCH_CODE, BRANCH_NAME, category_name, Accum_Dep,"
	 + "Monthly_Dep, Cost_Price, NBV, IMPROV_COST, IMPROV_ACCUMDEP, IMPROV_MONTHLYDEP, IMPROV_NBV,"
	 + "Cost_Price + IMPROV_COST AS Total_Cost_Price, TOTAL_NBV, Date_purchased, Dep_rate AS am_ad_category_Dep_rate, Accum_Dep_ledger," 
     + "Dep_ledger, Asset_Ledger, gl_account, Dep_End_Date, Effective_Date, Posting_Date, Dept_name, Asset_User,"
     + "Sbu_name, BRANCH_CODE AS am_asset_BRANCH_CODE, BAR_CODE, Asset_Serial_No, Spare_1, Spare_2, SPARE_3, SPARE_4,"
     + "SPARE_5, SPARE_6, Purchase_Reason, Registration_No, Asset_Make, Asset_Model, Asset_Engine_No, Supplier_Name, Wh_Tax,"
     + "Wh_Tax_Amount, Req_Depreciation, Vat, Subject_TO_Vat, Asset_Status, State, Driver, Vendor_AC, Branch_ID, Dept_ID,"
     + "Category_ID, Section_id, Dep_Rate, Asset_Maintenance, Residual_Value, Authorized_By, Req_Redistribution, Useful_Life,"
     + "Total_Life, location, Remaining_Life, Vatable_Cost, Who_TO_Rem, Email1, Who_To_Rem_2, Email2, Raise_Entry,"
     + "Dep_Ytd, Section, User_ID, Date_Disposed, PROVINCE, Multiple, WAR_START_DATE, WAR_MONTH, WAR_EXPIRY_DATE," 
     + "Last_Dep_Date, SECTION_CODE, DEPT_CODE, CATEGORY_CODE, AMOUNT_PTD, AMOUNT_REM, PART_PAY, FULLY_PAID, "
     + "GROUP_ID, SBU_CODE, LPO, supervisor, defer_pay, OLD_ASSET_ID, WHT_PERCENT, Post_reject_reason, Finacle_Posted_Date, "
     + "system_ip, mac_address, asset_code, memo, memovalue, state_name, Section_Name, Vendor_Name"
     + " FROM AssetRegister"
     + " WHERE Asset_Status = 'ACTIVE' and Cost_Price > (old_threshhold-0.01)  "
     + " ORDER BY BRANCH_CODE ASC,CATEGORY_CODE ASC ";	     
	}   
   if(!FromDate.equals("")  && !ToDate.equals("") && branch_Id.equals("0")  && categoryCode.equals("0")){
	System.out.println("======>>>>>>>No Selection but Date selected: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  dept_Code: "+dept_Code+"   asset_Id: "+asset_Id);
     ColQuery ="SELECT company_name,Cost_Threshold, Asset_id, Description, BRANCH_CODE, BRANCH_NAME, category_name, Accum_Dep,"
	 + "Monthly_Dep, Cost_Price, NBV, IMPROV_COST, IMPROV_ACCUMDEP, IMPROV_MONTHLYDEP, IMPROV_NBV,"
	 + "Cost_Price + IMPROV_COST AS Total_Cost_Price, TOTAL_NBV, Date_purchased, Dep_rate AS am_ad_category_Dep_rate, Accum_Dep_ledger," 
     + "Dep_ledger, Asset_Ledger, gl_account, Dep_End_Date, Effective_Date, Posting_Date, Dept_name, Asset_User,"
     + "Sbu_name, BRANCH_CODE AS am_asset_BRANCH_CODE, BAR_CODE, Asset_Serial_No, Spare_1, Spare_2, SPARE_3, SPARE_4,"
     + "SPARE_5, SPARE_6, Purchase_Reason, Registration_No, Asset_Make, Asset_Model, Asset_Engine_No, Supplier_Name, Wh_Tax,"
     + "Wh_Tax_Amount, Req_Depreciation, Vat, Subject_TO_Vat, Asset_Status, State, Driver, Vendor_AC, Branch_ID, Dept_ID,"
     + "Category_ID, Section_id, Dep_Rate, Asset_Maintenance, Residual_Value, Authorized_By, Req_Redistribution, Useful_Life,"
     + "Total_Life, location, Remaining_Life, Vatable_Cost, Who_TO_Rem, Email1, Who_To_Rem_2, Email2, Raise_Entry,"
     + "Dep_Ytd, Section, User_ID, Date_Disposed, PROVINCE, Multiple, WAR_START_DATE, WAR_MONTH, WAR_EXPIRY_DATE," 
     + "Last_Dep_Date, SECTION_CODE, DEPT_CODE, CATEGORY_CODE, AMOUNT_PTD, AMOUNT_REM, PART_PAY, FULLY_PAID, "
     + "GROUP_ID, SBU_CODE, LPO, supervisor, defer_pay, OLD_ASSET_ID, WHT_PERCENT, Post_reject_reason, Finacle_Posted_Date, "
     + "system_ip, mac_address, asset_code, memo, memovalue, state_name, Section_Name, Vendor_Name"
     + " FROM AssetRegister"
     + " WHERE Asset_Status = 'ACTIVE' and Cost_Price > (old_threshhold-0.01) and Posting_Date between ? and ?  "
     + " ORDER BY BRANCH_CODE ASC,CATEGORY_CODE ASC ";	     
	} 
   if(!FromDate.equals("")  && !ToDate.equals("") && !branch_Id.equals("0")  && categoryCode.equals("0")){
	System.out.println("======>>>>>>>Branch and Date Selection: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  dept_Code: "+dept_Code+"   asset_Id: "+asset_Id);
     ColQuery ="SELECT company_name,Cost_Threshold, Asset_id, Description, BRANCH_CODE, BRANCH_NAME, category_name, Accum_Dep,"
	 + "Monthly_Dep, Cost_Price, NBV, IMPROV_COST, IMPROV_ACCUMDEP, IMPROV_MONTHLYDEP, IMPROV_NBV,"
	 + "Cost_Price + IMPROV_COST AS Total_Cost_Price, TOTAL_NBV, Date_purchased, Dep_rate AS am_ad_category_Dep_rate, Accum_Dep_ledger," 
     + "Dep_ledger, Asset_Ledger, gl_account, Dep_End_Date, Effective_Date, Posting_Date, Dept_name, Asset_User,"
     + "Sbu_name, BRANCH_CODE AS am_asset_BRANCH_CODE, BAR_CODE, Asset_Serial_No, Spare_1, Spare_2, SPARE_3, SPARE_4,"
     + "SPARE_5, SPARE_6, Purchase_Reason, Registration_No, Asset_Make, Asset_Model, Asset_Engine_No, Supplier_Name, Wh_Tax,"
     + "Wh_Tax_Amount, Req_Depreciation, Vat, Subject_TO_Vat, Asset_Status, State, Driver, Vendor_AC, Branch_ID, Dept_ID,"
     + "Category_ID, Section_id, Dep_Rate, Asset_Maintenance, Residual_Value, Authorized_By, Req_Redistribution, Useful_Life,"
     + "Total_Life, location, Remaining_Life, Vatable_Cost, Who_TO_Rem, Email1, Who_To_Rem_2, Email2, Raise_Entry,"
     + "Dep_Ytd, Section, User_ID, Date_Disposed, PROVINCE, Multiple, WAR_START_DATE, WAR_MONTH, WAR_EXPIRY_DATE," 
     + "Last_Dep_Date, SECTION_CODE, DEPT_CODE, CATEGORY_CODE, AMOUNT_PTD, AMOUNT_REM, PART_PAY, FULLY_PAID, "
     + "GROUP_ID, SBU_CODE, LPO, supervisor, defer_pay, OLD_ASSET_ID, WHT_PERCENT, Post_reject_reason, Finacle_Posted_Date, "
     + "system_ip, mac_address, asset_code, memo, memovalue, state_name, Section_Name, Vendor_Name"
     + " FROM AssetRegister"
     + " WHERE Asset_Status = 'ACTIVE' and Cost_Price > (old_threshhold-0.01) AND branch_id = ? AND Posting_Date between ? and ?  "
     + " ORDER BY BRANCH_CODE ASC,CATEGORY_CODE ASC ";	     
	}    
   if(!FromDate.equals("")  && !ToDate.equals("") && !branch_Id.equals("0")  && !categoryCode.equals("0")){
	System.out.println("======>>>>>>>Branch, Category and Date Selection: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  dept_Code: "+dept_Code+"   asset_Id: "+asset_Id);
     ColQuery ="SELECT company_name,Cost_Threshold, Asset_id, Description, BRANCH_CODE, BRANCH_NAME, category_name, Accum_Dep,"
	 + "Monthly_Dep, Cost_Price, NBV, IMPROV_COST, IMPROV_ACCUMDEP, IMPROV_MONTHLYDEP, IMPROV_NBV,"
	 + "Cost_Price + IMPROV_COST AS Total_Cost_Price, TOTAL_NBV, Date_purchased, Dep_rate AS am_ad_category_Dep_rate, Accum_Dep_ledger," 
     + "Dep_ledger, Asset_Ledger, gl_account, Dep_End_Date, Effective_Date, Posting_Date, Dept_name, Asset_User,"
     + "Sbu_name, BRANCH_CODE AS am_asset_BRANCH_CODE, BAR_CODE, Asset_Serial_No, Spare_1, Spare_2, SPARE_3, SPARE_4,"
     + "SPARE_5, SPARE_6, Purchase_Reason, Registration_No, Asset_Make, Asset_Model, Asset_Engine_No, Supplier_Name, Wh_Tax,"
     + "Wh_Tax_Amount, Req_Depreciation, Vat, Subject_TO_Vat, Asset_Status, State, Driver, Vendor_AC, Branch_ID, Dept_ID,"
     + "Category_ID, Section_id, Dep_Rate, Asset_Maintenance, Residual_Value, Authorized_By, Req_Redistribution, Useful_Life,"
     + "Total_Life, location, Remaining_Life, Vatable_Cost, Who_TO_Rem, Email1, Who_To_Rem_2, Email2, Raise_Entry,"
     + "Dep_Ytd, Section, User_ID, Date_Disposed, PROVINCE, Multiple, WAR_START_DATE, WAR_MONTH, WAR_EXPIRY_DATE," 
     + "Last_Dep_Date, SECTION_CODE, DEPT_CODE, CATEGORY_CODE, AMOUNT_PTD, AMOUNT_REM, PART_PAY, FULLY_PAID, "
     + "GROUP_ID, SBU_CODE, LPO, supervisor, defer_pay, OLD_ASSET_ID, WHT_PERCENT, Post_reject_reason, Finacle_Posted_Date, "
     + "system_ip, mac_address, asset_code, memo, memovalue, state_name, Section_Name, Vendor_Name"
     + " FROM AssetRegister"
     + " WHERE Asset_Status = 'ACTIVE' and Cost_Price > (old_threshhold-0.01) AND branch_id = ? AND CATEGORY_CODE = ? and Posting_Date between ? and ?  "
     + " ORDER BY BRANCH_CODE ASC,CATEGORY_CODE ASC ";	     
	}    
   if(!asset_Id.equals("") && FromDate.equals("")  && ToDate.equals("") && branch_Id.equals("0")  && categoryCode.equals("0")){
	System.out.println("======>>>>>>>Asset Id Selection: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  dept_Code: "+dept_Code+"   asset_Id: "+asset_Id);
     ColQuery ="SELECT company_name,Cost_Threshold, Asset_id, Description, BRANCH_CODE, BRANCH_NAME, category_name, Accum_Dep,"
	 + "Monthly_Dep, Cost_Price, NBV, IMPROV_COST, IMPROV_ACCUMDEP, IMPROV_MONTHLYDEP, IMPROV_NBV,"
	 + "Cost_Price + IMPROV_COST AS Total_Cost_Price, TOTAL_NBV, Date_purchased, Dep_rate AS am_ad_category_Dep_rate, Accum_Dep_ledger," 
     + "Dep_ledger, Asset_Ledger, gl_account, Dep_End_Date, Effective_Date, Posting_Date, Dept_name, Asset_User,"
     + "Sbu_name, BRANCH_CODE AS am_asset_BRANCH_CODE, BAR_CODE, Asset_Serial_No, Spare_1, Spare_2, SPARE_3, SPARE_4,"
     + "SPARE_5, SPARE_6, Purchase_Reason, Registration_No, Asset_Make, Asset_Model, Asset_Engine_No, Supplier_Name, Wh_Tax,"
     + "Wh_Tax_Amount, Req_Depreciation, Vat, Subject_TO_Vat, Asset_Status, State, Driver, Vendor_AC, Branch_ID, Dept_ID,"
     + "Category_ID, Section_id, Dep_Rate, Asset_Maintenance, Residual_Value, Authorized_By, Req_Redistribution, Useful_Life,"
     + "Total_Life, location, Remaining_Life, Vatable_Cost, Who_TO_Rem, Email1, Who_To_Rem_2, Email2, Raise_Entry,"
     + "Dep_Ytd, Section, User_ID, Date_Disposed, PROVINCE, Multiple, WAR_START_DATE, WAR_MONTH, WAR_EXPIRY_DATE," 
     + "Last_Dep_Date, SECTION_CODE, DEPT_CODE, CATEGORY_CODE, AMOUNT_PTD, AMOUNT_REM, PART_PAY, FULLY_PAID, "
     + "GROUP_ID, SBU_CODE, LPO, supervisor, defer_pay, OLD_ASSET_ID, WHT_PERCENT, Post_reject_reason, Finacle_Posted_Date, "
     + "system_ip, mac_address, asset_code, memo, memovalue, state_name, Section_Name, Vendor_Name"
     + " FROM AssetRegister"
     + " WHERE Asset_Status = 'ACTIVE' and Cost_Price > (old_threshhold-0.01) AND Asset_id = ? "
     + " ORDER BY BRANCH_CODE ASC,CATEGORY_CODE ASC ";	     
	}    

   if(!FromDate.equals("")  && !ToDate.equals("") && branch_Id.equals("0")  && !categoryCode.equals("0")){
		System.out.println("======>>>>>>>Asset Id Selection: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  dept_Code: "+dept_Code+"   asset_Id: "+asset_Id);
	     ColQuery ="SELECT company_name,Cost_Threshold, Asset_id, Description, BRANCH_CODE, BRANCH_NAME, category_name, Accum_Dep,"
		 + "Monthly_Dep, Cost_Price, NBV, IMPROV_COST, IMPROV_ACCUMDEP, IMPROV_MONTHLYDEP, IMPROV_NBV,"
		 + "Cost_Price + IMPROV_COST AS Total_Cost_Price, TOTAL_NBV, Date_purchased, Dep_rate AS am_ad_category_Dep_rate, Accum_Dep_ledger," 
	     + "Dep_ledger, Asset_Ledger, gl_account, Dep_End_Date, Effective_Date, Posting_Date, Dept_name, Asset_User,"
	     + "Sbu_name, BRANCH_CODE AS am_asset_BRANCH_CODE, BAR_CODE, Asset_Serial_No, Spare_1, Spare_2, SPARE_3, SPARE_4,"
	     + "SPARE_5, SPARE_6, Purchase_Reason, Registration_No, Asset_Make, Asset_Model, Asset_Engine_No, Supplier_Name, Wh_Tax,"
	     + "Wh_Tax_Amount, Req_Depreciation, Vat, Subject_TO_Vat, Asset_Status, State, Driver, Vendor_AC, Branch_ID, Dept_ID,"
	     + "Category_ID, Section_id, Dep_Rate, Asset_Maintenance, Residual_Value, Authorized_By, Req_Redistribution, Useful_Life,"
	     + "Total_Life, location, Remaining_Life, Vatable_Cost, Who_TO_Rem, Email1, Who_To_Rem_2, Email2, Raise_Entry,"
	     + "Dep_Ytd, Section, User_ID, Date_Disposed, PROVINCE, Multiple, WAR_START_DATE, WAR_MONTH, WAR_EXPIRY_DATE," 
	     + "Last_Dep_Date, SECTION_CODE, DEPT_CODE, CATEGORY_CODE, AMOUNT_PTD, AMOUNT_REM, PART_PAY, FULLY_PAID, "
	     + "GROUP_ID, SBU_CODE, LPO, supervisor, defer_pay, OLD_ASSET_ID, WHT_PERCENT, Post_reject_reason, Finacle_Posted_Date, "
	     + "system_ip, mac_address, asset_code, memo, memovalue, state_name, Section_Name, Vendor_Name"
	     + " FROM AssetRegister"
	     + " WHERE Asset_Status = 'ACTIVE' and Cost_Price > (old_threshhold-0.01) AND CATEGORY_CODE = ? AND Posting_Date between ? and ? "
	     + " ORDER BY BRANCH_CODE ASC,CATEGORY_CODE ASC ";	     
		}
//   System.out.println("======>>>>>>>ColQuery: "+ColQuery);
     java.util.ArrayList list =rep.getAssetRegisterRecords(ColQuery,branch_Id,categoryCode,FromDate,ToDate,asset_Id);
     System.out.println("======>>>>>>>list size: "+list.size());
     if(list.size()!=0){
  
    	 SXSSFWorkbook workbook = new SXSSFWorkbook();
         Sheet sheet = workbook.createSheet("Excel");
         Row rowhead = sheet.createRow((int) 0);
         
         rowhead.createCell((short) 0).setCellValue("S/No.");
         rowhead.createCell((short) 1).setCellValue("Asset ID");
         rowhead.createCell((short) 2).setCellValue("Asset Description"); 
         rowhead.createCell((short) 3).setCellValue("Branch Name");
         rowhead.createCell((short) 4).setCellValue("Category Name");
         rowhead.createCell((short) 5).setCellValue("Department Name");
         rowhead.createCell((short) 6).setCellValue("Purchase Date");
         rowhead.createCell((short) 7).setCellValue("Depr. Start Date");
         rowhead.createCell((short) 8).setCellValue("Depr. End Date");
         rowhead.createCell((short) 9).setCellValue("Cost Price");
         rowhead.createCell((short) 10).setCellValue("Depr. Rate %");
         rowhead.createCell((short) 11).setCellValue("Monthly Depr.");
         rowhead.createCell((short) 12).setCellValue("Accum Depr.");
         rowhead.createCell((short) 13).setCellValue("NBV");
         rowhead.createCell((short) 14).setCellValue("Improv Cost Price");
		 rowhead.createCell((short) 15).setCellValue("Improv Monthly Depr.");
		 rowhead.createCell((short) 16).setCellValue("Improv Accum Depr.");
		 rowhead.createCell((short) 17).setCellValue("Improve NBV");
		 rowhead.createCell((short) 18).setCellValue("Total Cost Price");
		 rowhead.createCell((short) 19).setCellValue("Total NBV");
		 rowhead.createCell((short) 20).setCellValue("USER");
		 rowhead.createCell((short) 21).setCellValue("Engine No.");
		 rowhead.createCell((short) 22).setCellValue("SBU CODE");
		 rowhead.createCell((short) 23).setCellValue("QRCode/Vehicle");
		 rowhead.createCell((short) 24).setCellValue("Serial No");
		 rowhead.createCell((short) 25).setCellValue("Asset Model");
		 rowhead.createCell((short) 26).setCellValue("Asset Make");
		 rowhead.createCell((short) 27).setCellValue("Purchase Reason");
		 rowhead.createCell((short) 28).setCellValue("Location");
		 rowhead.createCell((short) 29).setCellValue("State");
		 rowhead.createCell((short) 30).setCellValue("Section/Unit");
		 rowhead.createCell((short) 31).setCellValue("LPO Number");
		 rowhead.createCell((short) 32).setCellValue("Vendor");
		 rowhead.createCell((short) 33).setCellValue("Spare Field 1");
		 rowhead.createCell((short) 34).setCellValue("Spare Field 2");
		 rowhead.createCell((short) 35).setCellValue("Registration No");
		 rowhead.createCell((short) 36).setCellValue("Vendor Account");
		 rowhead.createCell((short) 37).setCellValue("Old Asset Id");

     int i = 1;
     System.out.println("<<<<<<list.size(): "+list.size());
	 for(int k=0;k<list.size();k++)
     {
		
    	 com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);    	 
			String assetId =  newassettrans.getAssetId();
			//System.out.println("======K: "+k);
		//System.out.println("======assetId: "+assetId);
			String barcode =  newassettrans.getBarCode();
			String deptName =  newassettrans.getDeptName();
			//System.out.println("======>deptName: "+deptName);
			String Description = newassettrans.getDescription();   
			branchCode = newassettrans.getBranchCode();
			String branchName =  newassettrans.getBranchName();
			String categoryName =  newassettrans.getCategoryName();
			double depRate = newassettrans.getDepRate();
			
			//String branchName = records.getCodeName("select BRANCH_NAME from am_ad_branch where BRANCH_ID = "+branchId+"");
			
			double costprice = newassettrans.getCostPrice();
			double monthlyDepr = newassettrans.getMonthlyDep();
			double accumDepr = newassettrans.getAccumDep();
			double nbv = newassettrans.getNbv();
			double improvmonthldepr = newassettrans.getImprovmonthlyDep();
			double improvAccumDepr = newassettrans.getImprovaccumDep();
			double improvCostPrice = newassettrans.getImprovcostPrice();
			double improvNbv = newassettrans.getImprovnbv();
			double totalCost = newassettrans.getTotalCost();
			double totalnbv = newassettrans.getTotalnbv();
			categoryCode = newassettrans.getCategoryCode();
			
			//String categoryName = records.getCodeName("select BRANCH_NAME from am_ad_branch where BRANCH_ID = "+branchId+"");

			String assetUser = newassettrans.getAssetUser();
			String make = newassettrans.getAssetMake();
			String model = newassettrans.getAssetModel();
			String purchaseReason = newassettrans.getPurchaseReason();
			String lpo = newassettrans.getLpo();
			String registrationNo = newassettrans.getRegistrationNo();
			String vendoAcct = newassettrans.getVendorAC();
			String engineNo = newassettrans.getEngineNo();
			String serialNo = newassettrans.getSerialNo();
			//System.out.println("======>serialNo: "+serialNo);
			String location = newassettrans.getLocation();
			String state = newassettrans.getState();
			String sectionName = newassettrans.getSectionName();
			String vendorName = newassettrans.getVendorName();
			String spare1 = newassettrans.getSpare1();
			String spare2 = newassettrans.getSpare2();
			String oldAssetId = newassettrans.getOldassetId();
	
		//	String vendorName = records.getCodeName("select VENDOR_NAME from am_ad_vendor where VENDOR_ID = "+vendorId+"");
			
			String sbucode = newassettrans.getSbuCode();
			
			String purchaseDate = newassettrans.getDatepurchased() != null ? getDate(newassettrans.getDatepurchased()) : "";
//			if(!purchaseDate.equals("null")){
//			//System.out.println("======>purchaseDate: "+purchaseDate);
//			String yyyy = purchaseDate.substring(0, 4);
//			String mm = purchaseDate.substring(5, 7);
//			String dd = purchaseDate.substring(8, 10);
//			purchaseDate = dd+"/"+mm+"/"+yyyy;
//			}
			
			String depr_startDate = newassettrans.getEffectiveDate() != null ? getDate(newassettrans.getEffectiveDate()) : "";
//			if(depr_startDate == "NULL" || depr_startDate == null){
//				depr_startDate = " ";
//			} else if(!depr_startDate.equals("NULL")) {
//				String yyyy = depr_startDate.substring(0, 4);
//				String mm = depr_startDate.substring(5, 7);
//				String dd = depr_startDate.substring(8, 10);
//				depr_startDate = dd+"/"+mm+"/"+yyyy;
//			}
			
			//System.out.println("======>depr_startDate====: "+depr_startDate);
			
			String depr_endDate = newassettrans.getDependDate() != null ? getDate(newassettrans.getDependDate()) : "";

			//System.out.println("======>depr_endDate====: "+depr_endDate);
			
			
//			System.out.println("======>depr_startDate====: "+depr_startDate);
//			String yyyy = depr_startDate.substring(0, 4);
////			System.out.println("======>yyyy: "+yyyy);
//			String mm = depr_startDate.substring(5, 7);
////			System.out.println("======>mm: "+mm);
//			String dd = depr_startDate.substring(8, 10);
//			depr_startDate = dd+"/"+mm+"/"+yyyy;
//			System.out.println("======>depr_startDate: "+depr_startDate);
//			String deprciationDate = newassettrans.getPostingDate();
			
			String newAccumDepr = Double.toString(accumDepr);
			//System.out.println("======>newAccumDepr: "+newAccumDepr);
			
			
String value = "16/06/2023";
			Row row = sheet.createRow((int) i);

			//System.out.println("we are here 5");
			row.createCell((short) 0).setCellValue(i);
			row.createCell((short) 1).setCellValue(assetId);
            row.createCell((short) 2).setCellValue(Description);
            row.createCell((short) 3).setCellValue(branchName);
            row.createCell((short) 4).setCellValue(categoryName);
            row.createCell((short) 5).setCellValue(deptName);
            row.createCell((short) 6).setCellValue(purchaseDate);
           row.createCell((short) 7).setCellValue(depr_startDate);
           row.createCell((short) 8).setCellValue(depr_endDate);
            row.createCell((short) 9).setCellValue(costprice);
            row.createCell((short) 10).setCellValue(depRate);
            row.createCell((short) 11).setCellValue(monthlyDepr);
            row.createCell((short) 12).setCellValue(accumDepr);
            row.createCell((short) 13).setCellValue(nbv);
            row.createCell((short) 14).setCellValue(improvCostPrice);
            row.createCell((short) 15).setCellValue(improvmonthldepr);
            row.createCell((short) 16).setCellValue(improvAccumDepr);
            row.createCell((short) 17).setCellValue(improvNbv);
            row.createCell((short) 18).setCellValue(totalCost); 
            row.createCell((short) 19).setCellValue(totalnbv); 
            row.createCell((short) 20).setCellValue(assetUser);
            row.createCell((short) 21).setCellValue(engineNo);
			row.createCell((short) 22).setCellValue(sbucode);
			row.createCell((short) 23).setCellValue(barcode);
			row.createCell((short) 24).setCellValue(serialNo);
			row.createCell((short) 25).setCellValue(model);
			row.createCell((short) 26).setCellValue(make);
			row.createCell((short) 27).setCellValue(purchaseReason);
			row.createCell((short) 28).setCellValue(location);
			row.createCell((short) 29).setCellValue(state);
			row.createCell((short) 30).setCellValue(sectionName);
			row.createCell((short) 31).setCellValue(lpo);
			row.createCell((short) 32).setCellValue(vendorName);
			row.createCell((short) 33).setCellValue(spare1);
			row.createCell((short) 34).setCellValue(spare2);
			row.createCell((short) 35).setCellValue(registrationNo);
			row.createCell((short) 36).setCellValue(vendoAcct);
			row.createCell((short) 37).setCellValue(oldAssetId);
		
			
		//	System.out.println("======>oldAssetId====: "+oldAssetId+"  Index: "+i);

            i++;
     }
	 System.out.println("we are here 2");
	   OutputStream stream = response.getOutputStream();
         workbook.write(stream);
        // stream.close();
         workbook.close();  
         System.out.println("Data is saved in excel file.");
         
    /* w.write();
     w.close(); */

 }
    } catch (Exception e)
    {
     e.getMessage();
    } 
    //finally
   // {
//     if (out != null)
//      out.close();
   // }
   }
   }
   public void doGet(HttpServletRequest request, 
		    HttpServletResponse response)
		      throws ServletException, IOException
		   {
	   doPost(request, response);
		   }
   
   
   public String getDate(String date) {
	  // System.out.println("<<<<<<<<<<<< Date: " + date);
	   String yyyy = date.substring(0, 4);
		String mm = date.substring(5, 7);
		String dd = date.substring(8, 10);
		date = dd+"/"+mm+"/"+yyyy;
	   
		
	   return date;
   }
}