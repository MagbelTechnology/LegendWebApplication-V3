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

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;
   
public class ConsolidatePendingReport extends HttpServlet
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
//    String branch_Code = request.getParameter("initiatorSOLID");
    String branch_Id = request.getParameter("branch");
    String FromDate = request.getParameter("FromDate");
    String ToDate = request.getParameter("ToDate");
    if(!FromDate.equals("")){
	String yyyy = FromDate.substring(6, 10);
//	System.out.println("======>yyyy: "+yyyy);
	String mm = FromDate.substring(3, 5);
//	System.out.println("======>mm: "+mm);
	String dd = FromDate.substring(0, 2);
	FromDate = yyyy+"-"+mm+"-"+dd;
    }
//    System.out.println("======>FromDate: "+FromDate);
    if(!ToDate.equals("")){
	String Tyyyy = ToDate.substring(6, 10);
//	System.out.println("======>Tyyyy: "+Tyyyy);
	String Tmm = ToDate.substring(3, 5);
//	System.out.println("======>Tmm: "+Tmm);
	String Tdd = ToDate.substring(0, 2);
	ToDate = Tyyyy+"-"+Tmm+"-"+Tdd;
    }
 //   System.out.println("======>ToDate: "+ToDate);
    String dept_Code = request.getParameter("deptCode");
    String asset_Id = request.getParameter("assetId");
    String report = request.getParameter("report");
    String branchIdNo = records.getCodeName("select BRANCH from am_gb_User where USER_ID = ? ",userId);
    //String branchCode = request.getParameter("BRANCH_CODE");
    String userName = records.getCodeName("select USER_NAME from am_gb_User where USER_ID = ? ",userId);
//    System.out.println("<<<<<<branch_Id: "+branch_Id);
    String branchCode = "";
    if(!branchIdNo.equals("0")){
    	branchCode = records.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID = ? ",branchIdNo);
    }
//    System.out.println("<<<<<<branch_Code: "+branch_Code);
//    String userName = request.getParameter("userName");
    String fileName = "";
    if(report.equalsIgnoreCase("rptMenuPendingConsolid")){fileName = branchCode+"By"+userName+"ConsolidatedPendingReport.xlsx";}
//    System.out.println("<<<<<<fileName: "+fileName);
    String filePath = System.getProperty("user.home")+"\\Downloads";
//    System.out.println("<<<<<<filePath: "+filePath);
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
 //    System.out.println("=====FromDate Length: "+FromDate.length());
 //    System.out.println("=====ToDate Length: "+ToDate.length());
//   System.out.println("<<<<<<bran ch_Id: "+branch_Id+"    categoryCode: "+categoryCode+"  branchCode: "+branchCode);
//   System.out.println("<<<<<<branch_Id: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  branchCode: "+branchCode);
     String ColQuery = "";
     
     if(branch_Id.equals("0")  && categoryCode.equals("0") && FromDate.equals("")  && ToDate.equals("")){
  	   System.out.println("======>>>>>>>No Selection: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  dept_Code: "+dept_Code+"   asset_Id: "+asset_Id);
  	    ColQuery ="select 'C' AS transType,a.BRANCH_CODE,b.BRANCH_NAME,c.category_name,d.Dept_name,a.Old_Asset_Id, a.Asset_id,Description,Cost_Price,a.Monthly_Dep,a.Accum_Dep,a.nbv, "
  	    		+ "a.Posting_Date,a.Date_purchased,a.Effective_Date,a.Dep_End_Date,a.Dep_Rate,a.Asset_User,a.VENDOR_NAME, a.Dep_Rate,a.Total_Life,a.Remaining_Life, a.IMPROV_COST, a.IMPROV_ACCUMDEP, a.IMPROV_NBV, "
  	    		+ "a.IMPROV_MONTHLYDEP, a.IMPROV_REMAINLIFE, a.IMPROV_USEFULLIFE, a.IMPROV_TOTALLIFE, a.BAR_CODE AS TAG from am_asset a, am_ad_branch b, am_ad_category c, am_ad_department d,am_gb_company p  "
  	    		+ "where a.BRANCH_CODE = b.BRANCH_CODE and a.Category_ID= c.category_ID and a.DEPT_CODE = d.Dept_code and a.asset_status='PENDING' and a.Cost_Price > p.Cost_Threshold - 0.01  "
  	    		+ "UNION  "
  	    		+ "select 'U' AS transType,a.BRANCH_CODE, b.BRANCH_NAME,c.category_name,d.Dept_name,a.Old_Asset_Id,a.Asset_id,Description,Cost_Price,a.Monthly_Dep,a.Accum_Dep,a.nbv, "
  	    		+ "a.Posting_Date,a.Date_purchased,a.Effective_Date,a.Dep_End_Date,a.Dep_Rate,a.Asset_User,a.VENDOR_NAME, a.Dep_Rate,a.Total_Life,a.Remaining_Life, a.IMPROV_COST, a.IMPROV_ACCUMDEP, a.IMPROV_NBV, "
  	    		+ "a.IMPROV_MONTHLYDEP, a.IMPROV_REMAINLIFE, a.IMPROV_USEFULLIFE, a.IMPROV_TOTALLIFE, a.BAR_CODE AS TAG from AM_ASSET_UNCAPITALIZED a, am_ad_branch b, am_ad_category c, am_ad_department d,am_gb_company p  "
  	    		+ "where a.BRANCH_CODE = b.BRANCH_CODE and a.Category_ID= c.category_ID and a.DEPT_CODE = d.Dept_code and a.asset_status='PENDING' and a.Cost_Price < p.Cost_Threshold + 0.01   ";    
  	}   
     
     if(!branch_Id.equals("0")  && !categoryCode.equals("0") && FromDate.equals("")  && ToDate.equals("")){
    	 System.out.println("======>>>>>>>Branch and Category Selected: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  dept_Code: "+dept_Code+"   asset_Id: "+asset_Id);	     
    	    ColQuery ="select 'C' AS transType,a.BRANCH_CODE,b.BRANCH_NAME,c.category_name,d.Dept_name,a.Old_Asset_Id, a.Asset_id,Description,Cost_Price,a.Monthly_Dep,a.Accum_Dep,a.nbv, "
    	    		+ "a.Posting_Date,a.Date_purchased,a.Effective_Date,a.Dep_End_Date,a.Dep_Rate,a.Asset_User,a.VENDOR_NAME, a.Dep_Rate,a.Total_Life,a.Remaining_Life, a.IMPROV_COST, a.IMPROV_ACCUMDEP, a.IMPROV_NBV, "
    	    		+ "a.IMPROV_MONTHLYDEP, a.IMPROV_REMAINLIFE, a.IMPROV_USEFULLIFE, a.IMPROV_TOTALLIFE, a.BAR_CODE AS TAG from am_asset a, am_ad_branch b, am_ad_category c, am_ad_department d,am_gb_company p  "
    	    		+ "where a.BRANCH_CODE = b.BRANCH_CODE and a.Category_ID= c.category_ID and a.DEPT_CODE = d.Dept_code and a.asset_status='PENDING' and a.Cost_Price > p.Cost_Threshold - 0.01  "
    	    		+ "and a.branch_id = ? and a.category_Id = ? "
    	    		+ "UNION  "
    	    		+ "select 'U' AS transType,a.BRANCH_CODE, b.BRANCH_NAME,c.category_name,d.Dept_name,a.Old_Asset_Id,a.Asset_id,Description,Cost_Price,a.Monthly_Dep,a.Accum_Dep,a.nbv, "
    	    		+ "a.Posting_Date,a.Date_purchased,a.Effective_Date,a.Dep_End_Date,a.Dep_Rate,a.Asset_User,a.VENDOR_NAME, a.Dep_Rate,a.Total_Life,a.Remaining_Life, a.IMPROV_COST, a.IMPROV_ACCUMDEP, a.IMPROV_NBV, "
    	    		+ "a.IMPROV_MONTHLYDEP, a.IMPROV_REMAINLIFE, a.IMPROV_USEFULLIFE, a.IMPROV_TOTALLIFE, a.BAR_CODE AS TAG from AM_ASSET_UNCAPITALIZED a, am_ad_branch b, am_ad_category c, am_ad_department d,am_gb_company p  "
    	    		+ "where a.BRANCH_CODE = b.BRANCH_CODE and a.Category_ID= c.category_ID and a.DEPT_CODE = d.Dept_code and a.asset_status='PENDING' and a.Cost_Price < p.Cost_Threshold + 0.01   "
    	    		+ "and a.branch_id = ? and a.category_Id = ? ";     
	}      
	 if(branch_Id.equals("0")  && !categoryCode.equals("0") && FromDate.equals("")  && ToDate.equals("")){	   
	   System.out.println("======>>>>>>>Category Selected: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  dept_Code: "+dept_Code+"   asset_Id: "+asset_Id);
	    ColQuery ="select 'C' AS transType,a.BRANCH_CODE,b.BRANCH_NAME,c.category_name,d.Dept_name,a.Old_Asset_Id, a.Asset_id,Description,Cost_Price,a.Monthly_Dep,a.Accum_Dep,a.nbv, "
	    		+ "a.Posting_Date,a.Date_purchased,a.Effective_Date,a.Dep_End_Date,a.Dep_Rate,a.Asset_User,a.VENDOR_NAME, a.Dep_Rate,a.Total_Life,a.Remaining_Life, a.IMPROV_COST, a.IMPROV_ACCUMDEP, a.IMPROV_NBV, "
	    		+ "a.IMPROV_MONTHLYDEP, a.IMPROV_REMAINLIFE, a.IMPROV_USEFULLIFE, a.IMPROV_TOTALLIFE, a.BAR_CODE AS TAG from am_asset a, am_ad_branch b, am_ad_category c, am_ad_department d,am_gb_company p  "
	    		+ "where a.BRANCH_CODE = b.BRANCH_CODE and a.Category_ID= c.category_ID and a.DEPT_CODE = d.Dept_code and a.asset_status='PENDING' and a.Cost_Price > p.Cost_Threshold - 0.01  "
	    		+ "and a.category_Id = ? "
	    		+ "UNION  "
	    		+ "select 'U' AS transType,a.BRANCH_CODE, b.BRANCH_NAME,c.category_name,d.Dept_name,a.Old_Asset_Id,a.Asset_id,Description,Cost_Price,a.Monthly_Dep,a.Accum_Dep,a.nbv, "
	    		+ "a.Posting_Date,a.Date_purchased,a.Effective_Date,a.Dep_End_Date,a.Dep_Rate,a.Asset_User,a.VENDOR_NAME, a.Dep_Rate,a.Total_Life,a.Remaining_Life, a.IMPROV_COST, a.IMPROV_ACCUMDEP, a.IMPROV_NBV, "
	    		+ "a.IMPROV_MONTHLYDEP, a.IMPROV_REMAINLIFE, a.IMPROV_USEFULLIFE, a.IMPROV_TOTALLIFE, a.BAR_CODE AS TAG from AM_ASSET_UNCAPITALIZED a, am_ad_branch b, am_ad_category c, am_ad_department d,am_gb_company p  "
	    		+ "where a.BRANCH_CODE = b.BRANCH_CODE and a.Category_ID= c.category_ID and a.DEPT_CODE = d.Dept_code and a.asset_status='PENDING' and a.Cost_Price < p.Cost_Threshold + 0.01   "
	    		+ "and a.category_Id = ? ";
	    }
	 
	 if(!branch_Id.equals("0")  && categoryCode.equals("0") && FromDate.equals("")  && ToDate.equals("")){	   
	   System.out.println("======>>>>>>>Branch Selected: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  dept_Code: "+dept_Code+"   asset_Id: "+asset_Id);
	    ColQuery ="select 'C' AS transType,a.BRANCH_CODE,b.BRANCH_NAME,c.category_name,d.Dept_name,a.Old_Asset_Id, a.Asset_id,Description,Cost_Price,a.Monthly_Dep,a.Accum_Dep,a.nbv, "
	    		+ "a.Posting_Date,a.Date_purchased,a.Effective_Date,a.Dep_End_Date,a.Dep_Rate,a.Asset_User,a.VENDOR_NAME, a.Dep_Rate,a.Total_Life,a.Remaining_Life, a.IMPROV_COST, a.IMPROV_ACCUMDEP, a.IMPROV_NBV, "
	    		+ "a.IMPROV_MONTHLYDEP, a.IMPROV_REMAINLIFE, a.IMPROV_USEFULLIFE, a.IMPROV_TOTALLIFE, a.BAR_CODE AS TAG from am_asset a, am_ad_branch b, am_ad_category c, am_ad_department d,am_gb_company p  "
	    		+ "where a.BRANCH_CODE = b.BRANCH_CODE and a.Category_ID= c.category_ID and a.DEPT_CODE = d.Dept_code and a.asset_status='PENDING' and a.Cost_Price > p.Cost_Threshold - 0.01  "
	    		+ "and a.branch_id = ?  "
	    		+ "UNION  "
	    		+ "select 'U' AS transType,a.BRANCH_CODE, b.BRANCH_NAME,c.category_name,d.Dept_name,a.Old_Asset_Id,a.Asset_id,Description,Cost_Price,a.Monthly_Dep,a.Accum_Dep,a.nbv, "
	    		+ "a.Posting_Date,a.Date_purchased,a.Effective_Date,a.Dep_End_Date,a.Dep_Rate,a.Asset_User,a.VENDOR_NAME, a.Dep_Rate,a.Total_Life,a.Remaining_Life, a.IMPROV_COST, a.IMPROV_ACCUMDEP, a.IMPROV_NBV, "
	    		+ "a.IMPROV_MONTHLYDEP, a.IMPROV_REMAINLIFE, a.IMPROV_USEFULLIFE, a.IMPROV_TOTALLIFE, a.BAR_CODE AS TAG from AM_ASSET_UNCAPITALIZED a, am_ad_branch b, am_ad_category c, am_ad_department d,am_gb_company p  "
	    		+ "where a.BRANCH_CODE = b.BRANCH_CODE and a.Category_ID= c.category_ID and a.DEPT_CODE = d.Dept_code and a.asset_status='PENDING' and a.Cost_Price < p.Cost_Threshold + 0.01   "
	    		+ "and a.branch_id = ? ";
	    }
   
   if(!FromDate.equals("")  && !ToDate.equals("") && branch_Id.equals("0")  && categoryCode.equals("0")){
	System.out.println("======>>>>>>> Date selected: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  dept_Code: "+dept_Code+"   asset_Id: "+asset_Id);
    ColQuery ="select 'C' AS transType,a.BRANCH_CODE,b.BRANCH_NAME,c.category_name,d.Dept_name,a.Old_Asset_Id, a.Asset_id,Description,Cost_Price,a.Monthly_Dep,a.Accum_Dep,a.nbv, "
    		+ "a.Posting_Date,a.Date_purchased,a.Effective_Date,a.Dep_End_Date,a.Dep_Rate,a.Asset_User,a.VENDOR_NAME, a.Dep_Rate,a.Total_Life,a.Remaining_Life, a.IMPROV_COST, a.IMPROV_ACCUMDEP, a.IMPROV_NBV, "
    		+ "a.IMPROV_MONTHLYDEP, a.IMPROV_REMAINLIFE, a.IMPROV_USEFULLIFE, a.IMPROV_TOTALLIFE, a.BAR_CODE AS TAG from am_asset a, am_ad_branch b, am_ad_category c, am_ad_department d,am_gb_company p  "
    		+ "where a.BRANCH_CODE = b.BRANCH_CODE and a.Category_ID= c.category_ID and a.DEPT_CODE = d.Dept_code and a.asset_status='PENDING' and a.Cost_Price > p.Cost_Threshold - 0.01  "
    		+ "and a.Posting_Date between ? and ? "
    		+ "UNION  "
    		+ "select 'U' AS transType,a.BRANCH_CODE, b.BRANCH_NAME,c.category_name,d.Dept_name,a.Old_Asset_Id,a.Asset_id,Description,Cost_Price,a.Monthly_Dep,a.Accum_Dep,a.nbv, "
    		+ "a.Posting_Date,a.Date_purchased,a.Effective_Date,a.Dep_End_Date,a.Dep_Rate,a.Asset_User,a.VENDOR_NAME, a.Dep_Rate,a.Total_Life,a.Remaining_Life, a.IMPROV_COST, a.IMPROV_ACCUMDEP, a.IMPROV_NBV, "
    		+ "a.IMPROV_MONTHLYDEP, a.IMPROV_REMAINLIFE, a.IMPROV_USEFULLIFE, a.IMPROV_TOTALLIFE, a.BAR_CODE AS TAG from AM_ASSET_UNCAPITALIZED a, am_ad_branch b, am_ad_category c, am_ad_department d,am_gb_company p  "
    		+ "where a.BRANCH_CODE = b.BRANCH_CODE and a.Category_ID= c.category_ID and a.DEPT_CODE = d.Dept_code and a.asset_status='PENDING' and a.Cost_Price < p.Cost_Threshold + 0.01   "
    		+ "and a.Posting_Date between ? and ? ";		     
	} 
   if(!branch_Id.equals("0")  && categoryCode.equals("0") && !FromDate.equals("")  && !ToDate.equals("")){
	System.out.println("======>>>>>>>Branch and Date Selection: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  dept_Code: "+dept_Code+"   asset_Id: "+asset_Id);
    ColQuery ="select 'C' AS transType,a.BRANCH_CODE,b.BRANCH_NAME,c.category_name,d.Dept_name,a.Old_Asset_Id, a.Asset_id,Description,Cost_Price,a.Monthly_Dep,a.Accum_Dep,a.nbv, "
    		+ "a.Posting_Date,a.Date_purchased,a.Effective_Date,a.Dep_End_Date,a.Dep_Rate,a.Asset_User,a.VENDOR_NAME, a.Dep_Rate,a.Total_Life,a.Remaining_Life, a.IMPROV_COST, a.IMPROV_ACCUMDEP, a.IMPROV_NBV, "
    		+ "a.IMPROV_MONTHLYDEP, a.IMPROV_REMAINLIFE, a.IMPROV_USEFULLIFE, a.IMPROV_TOTALLIFE, a.BAR_CODE AS TAG from am_asset a, am_ad_branch b, am_ad_category c, am_ad_department d,am_gb_company p  "
    		+ "where a.BRANCH_CODE = b.BRANCH_CODE and a.Category_ID= c.category_ID and a.DEPT_CODE = d.Dept_code and a.asset_status='PENDING' and a.Cost_Price > p.Cost_Threshold - 0.01 "
    		+ "and a.branch_id = ? and a.Posting_Date between ? and ? "
    		+ "UNION  "
    		+ "select 'U' AS transType,a.BRANCH_CODE, b.BRANCH_NAME,c.category_name,d.Dept_name,a.Old_Asset_Id,a.Asset_id,Description,Cost_Price,a.Monthly_Dep,a.Accum_Dep,a.nbv, "
    		+ "a.Posting_Date,a.Date_purchased,a.Effective_Date,a.Dep_End_Date,a.Dep_Rate,a.Asset_User,a.VENDOR_NAME, a.Dep_Rate,a.Total_Life,a.Remaining_Life, a.IMPROV_COST, a.IMPROV_ACCUMDEP, a.IMPROV_NBV, "
    		+ "a.IMPROV_MONTHLYDEP, a.IMPROV_REMAINLIFE, a.IMPROV_USEFULLIFE, a.IMPROV_TOTALLIFE, a.BAR_CODE AS TAG from AM_ASSET_UNCAPITALIZED a, am_ad_branch b, am_ad_category c, am_ad_department d,am_gb_company p  "
    		+ "where a.BRANCH_CODE = b.BRANCH_CODE and a.Category_ID= c.category_ID and a.DEPT_CODE = d.Dept_code and a.asset_status='PENDING' and a.Cost_Price < p.Cost_Threshold + 0.01  "
    		+ "and a.branch_id = ? and a.Posting_Date between ? and ? ";	    
	}    
   if(!FromDate.equals("")  && !ToDate.equals("") && !branch_Id.equals("0")  && !categoryCode.equals("0")){
	System.out.println("======>>>>>>>Branch, Category and Date Selection: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  dept_Code: "+dept_Code+"   asset_Id: "+asset_Id);
    ColQuery ="select 'C' AS transType,a.BRANCH_CODE,b.BRANCH_NAME,c.category_name,d.Dept_name,a.Old_Asset_Id, a.Asset_id,Description,Cost_Price,a.Monthly_Dep,a.Accum_Dep,a.nbv, "
    		+ "a.Posting_Date,a.Date_purchased,a.Effective_Date,a.Dep_End_Date,a.Dep_Rate,a.Asset_User,a.VENDOR_NAME, a.Dep_Rate,a.Total_Life,a.Remaining_Life, a.IMPROV_COST, a.IMPROV_ACCUMDEP, a.IMPROV_NBV, "
    		+ "a.IMPROV_MONTHLYDEP, a.IMPROV_REMAINLIFE, a.IMPROV_USEFULLIFE, a.IMPROV_TOTALLIFE, a.BAR_CODE AS TAG from am_asset a, am_ad_branch b, am_ad_category c, am_ad_department d,am_gb_company p  "
    		+ "where a.BRANCH_CODE = b.BRANCH_CODE and a.Category_ID= c.category_ID and a.DEPT_CODE = d.Dept_code and a.asset_status='PENDING' and a.Cost_Price > p.Cost_Threshold - 0.01  "
    		+ "and a.branch_id = ? and a.category_Id = ? and a.Posting_Date between ? and ? "
    		+ "UNION  "
    		+ "select 'U' AS transType,a.BRANCH_CODE, b.BRANCH_NAME,c.category_name,d.Dept_name,a.Old_Asset_Id,a.Asset_id,Description,Cost_Price,a.Monthly_Dep,a.Accum_Dep,a.nbv, "
    		+ "a.Posting_Date,a.Date_purchased,a.Effective_Date,a.Dep_End_Date,a.Dep_Rate,a.Asset_User,a.VENDOR_NAME, a.Dep_Rate,a.Total_Life,a.Remaining_Life, a.IMPROV_COST, a.IMPROV_ACCUMDEP, a.IMPROV_NBV, "
    		+ "a.IMPROV_MONTHLYDEP, a.IMPROV_REMAINLIFE, a.IMPROV_USEFULLIFE, a.IMPROV_TOTALLIFE, a.BAR_CODE AS TAG from AM_ASSET_UNCAPITALIZED a, am_ad_branch b, am_ad_category c, am_ad_department d,am_gb_company p  "
    		+ "where a.BRANCH_CODE = b.BRANCH_CODE and a.Category_ID= c.category_ID and a.DEPT_CODE = d.Dept_code and a.asset_status='PENDING' and a.Cost_Price < p.Cost_Threshold + 0.01   "
    		+ "and a.branch_id = ? and a.category_Id = ? and a.Posting_Date between ? and ? ";	
    }    

   if(!FromDate.equals("")  && !ToDate.equals("") && branch_Id.equals("0")  && !categoryCode.equals("0")){
		System.out.println("======>>>>>>>Category and Date Selection: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  dept_Code: "+dept_Code+"   asset_Id: "+asset_Id);
	    ColQuery ="select 'C' AS transType,a.BRANCH_CODE,b.BRANCH_NAME,c.category_name,d.Dept_name,a.Old_Asset_Id, a.Asset_id,Description,Cost_Price,a.Monthly_Dep,a.Accum_Dep,a.nbv, "
	    		+ "a.Posting_Date,a.Date_purchased,a.Effective_Date,a.Dep_End_Date,a.Dep_Rate,a.Asset_User,a.VENDOR_NAME, a.Dep_Rate,a.Total_Life,a.Remaining_Life, a.IMPROV_COST, a.IMPROV_ACCUMDEP, a.IMPROV_NBV, "
	    		+ "a.IMPROV_MONTHLYDEP, a.IMPROV_REMAINLIFE, a.IMPROV_USEFULLIFE, a.IMPROV_TOTALLIFE, a.BAR_CODE AS TAG from am_asset a, am_ad_branch b, am_ad_category c, am_ad_department d,am_gb_company p  "
	    		+ "where a.BRANCH_CODE = b.BRANCH_CODE and a.Category_ID= c.category_ID and a.DEPT_CODE = d.Dept_code and a.asset_status='PENDING' and a.Cost_Price > p.Cost_Threshold - 0.01  "
	    		+ "and a.category_Id = ? and a.Posting_Date between ? and ? "
	    		+ "UNION  "
	    		+ "select 'U' AS transType,a.BRANCH_CODE, b.BRANCH_NAME,c.category_name,d.Dept_name,a.Old_Asset_Id,a.Asset_id,Description,Cost_Price,a.Monthly_Dep,a.Accum_Dep,a.nbv, "
	    		+ "a.Posting_Date,a.Date_purchased,a.Effective_Date,a.Dep_End_Date,a.Dep_Rate,a.Asset_User,a.VENDOR_NAME, a.Dep_Rate,a.Total_Life,a.Remaining_Life, a.IMPROV_COST, a.IMPROV_ACCUMDEP, a.IMPROV_NBV, "
	    		+ "a.IMPROV_MONTHLYDEP, a.IMPROV_REMAINLIFE, a.IMPROV_USEFULLIFE, a.IMPROV_TOTALLIFE, a.BAR_CODE AS TAG from AM_ASSET_UNCAPITALIZED a, am_ad_branch b, am_ad_category c, am_ad_department d,am_gb_company p  "
	    		+ "where a.BRANCH_CODE = b.BRANCH_CODE and a.Category_ID= c.category_ID and a.DEPT_CODE = d.Dept_code and a.asset_status='PENDING' and a.Cost_Price < p.Cost_Threshold + 0.01   "
	    		+ "and a.category_Id = ? and a.Posting_Date between ? and ? ";		
	    }   
//   System.out.println("======>>>>>>>ColQuery: "+ColQuery);
     java.util.ArrayList list =rep.getPendingConsolidatedRecords(ColQuery,branch_Id,categoryCode,FromDate,ToDate);
     System.out.println("======>>>>>>>list size: "+list.size()+"        =====report: "+report);
     if(list.size()!=0){
   	 if(report.equalsIgnoreCase("rptMenuPendingConsolid")){
    	 SXSSFWorkbook workbook = new SXSSFWorkbook();
         Sheet sheet = workbook.createSheet("Demo");
         Row rowhead = sheet.createRow((int) 0);
         
         rowhead.createCell( 0).setCellValue("S/No.");
         rowhead.createCell( 1).setCellValue("Asset ID");
         rowhead.createCell( 2).setCellValue("Asset Description");
         rowhead.createCell( 3).setCellValue("Branch Code");
         rowhead.createCell( 4).setCellValue("Branch Name");
         rowhead.createCell( 5).setCellValue("Category Name");
         rowhead.createCell( 6).setCellValue("Department Name");
         rowhead.createCell( 7).setCellValue("Purchase Date");
         rowhead.createCell( 8).setCellValue("Depr. Start Date");
         rowhead.createCell( 9).setCellValue("Depr. End Date");
         rowhead.createCell( 10).setCellValue("Cost Price");
         rowhead.createCell( 11).setCellValue("Monthly Depreciation");
		 rowhead.createCell( 12).setCellValue("Accumulated Depr.");
		 rowhead.createCell( 13).setCellValue("NBV");
		 rowhead.createCell( 14).setCellValue("Asset User");
		 rowhead.createCell( 15).setCellValue("Asset Type");
		 rowhead.createCell( 16).setCellValue("Vendor Name");
		 rowhead.createCell( 17).setCellValue("Depr. Rate");
		 rowhead.createCell( 18).setCellValue("Total Life");
		 rowhead.createCell( 19).setCellValue("Remaining Life");
		 rowhead.createCell( 20).setCellValue("Improv Cost");
		 rowhead.createCell( 21).setCellValue("Improv AccumDep");
		 rowhead.createCell( 22).setCellValue("Improv NBV");
		 rowhead.createCell( 23).setCellValue("Improv MonthlyDep");
		 rowhead.createCell( 24).setCellValue("Improv RemainLife");
		 rowhead.createCell( 25).setCellValue("Improv UsefulLife");
		 rowhead.createCell( 26).setCellValue("Improv TotalLife");
		 rowhead.createCell( 27).setCellValue("Tag");

     int i = 1;
     System.out.println("<<<<<<list.size(): "+list.size());
	 for(int k=0;k<list.size();k++)
     {
    	 com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);    	 
			String assetId =  newassettrans.getAssetId();
			
			String oldassetId =  newassettrans.getOldassetId();
			String barcode =  newassettrans.getBarCode();
			String deptCode =  newassettrans.getDeptCode();
			String deptName =  newassettrans.getDeptName();
			String Description = newassettrans.getDescription();   
			String assetuser = newassettrans.getAssetUser();
			String assetcode = newassettrans.getAssetCode();
			String branch_code = newassettrans.getBranchCode();
			String branchName =  newassettrans.getBranchName();
			String categoryName =  newassettrans.getCategoryName();
			String branchId = newassettrans.getBranchId();
			//String branchName = records.getCodeName("select BRANCH_NAME from am_ad_branch where BRANCH_ID = "+branchId+"");
			double costprice = newassettrans.getCostPrice();
			double monthlyDepr = newassettrans.getMonthlyDep();
			double accumDepr = newassettrans.getAccumDep();
			double nbv = newassettrans.getNbv();
			double depchargetoDate = newassettrans.getDeprChargeToDate();
			double improvmonthldepr = newassettrans.getImprovmonthlyDep();
			double improvAccumDepr = newassettrans.getImprovaccumDep();
			double improvCostPrice = newassettrans.getImprovcostPrice();
			double improvNbv = newassettrans.getImprovnbv();
			double totalCost = newassettrans.getTotalCost();
			double totalnbv = newassettrans.getTotalnbv();
			int totalLife = newassettrans.getUsefullife();
			categoryCode = newassettrans.getCategoryCode();
			String assetStatus = newassettrans.getAssetStatus(); 
//			String categoryName = records.getCodeName("select BRANCH_NAME from am_ad_branch where BRANCH_ID = "+branchId+"");
			String batchId = newassettrans.getIntegrifyId();
			String sighted = newassettrans.getAssetsighted();
			String function = newassettrans.getAssetfunction();
			String comments = newassettrans.getAssetMaintenance();
			String make = newassettrans.getAssetMake();
			String model = newassettrans.getAssetModel();
			String purchaseReason = newassettrans.getPurchaseReason();
			String lpo = newassettrans.getLpo();
			String vendoAcct = newassettrans.getVendorAC();
			String vendorId = newassettrans.getSupplierName();
			String engineNo = newassettrans.getEngineNo();
			String serialNo = newassettrans.getSerialNo();
			String location = newassettrans.getLocation();
			String state = newassettrans.getState();
			String sectionName = newassettrans.getSectionName();
			String vendorName = newassettrans.getVendorName();
			String spare1 = newassettrans.getSpare1();
			String spare2 = newassettrans.getSpare2();
			String spare3 = newassettrans.getSpare3();
			String spare4 = newassettrans.getSpare4();
			String spare5 = newassettrans.getSpare5();
			String spare6 = newassettrans.getSpare6();
			String oldAssetId = newassettrans.getOldassetId();
			double depr_Rate = newassettrans.getDepRate();
			int remain_life = newassettrans.getRemainLife();
			double improv_cost = newassettrans.getImprovcostPrice();
			double improv_accum_dep = newassettrans.getImprovaccumDep();
			double improv_nbv = newassettrans.getImprovnbv();
            double improv_monthly_dep = newassettrans.getImprovmonthlyDep();
            int improv_remain_life = newassettrans.getImproveRemainLife();
            int improv_useful_life = newassettrans.getNoofitems();
            int improv_total_life = newassettrans.getImprovtotallife();
            String tag = newassettrans.getBarCode();
			

			//			String vendorName = records.getCodeName("select VENDOR_NAME from am_ad_vendor where VENDOR_ID = "+vendorId+"");
			
			String sbucode = newassettrans.getSbuCode();
			String purchaseDate = newassettrans.getDatepurchased() != null ? getDate(newassettrans.getDatepurchased()) : "";

			String depr_startDate = newassettrans.getEffectiveDate() != null ? getDate(newassettrans.getEffectiveDate()) : "";

			String depr_endDate = newassettrans.getDependDate() != null ? getDate(newassettrans.getDependDate()) : "";

			Row row = sheet.createRow((int) i);

	         rowhead.createCell( 0).setCellValue("S/No.");
	         rowhead.createCell( 1).setCellValue("Old Asset ID");
	         rowhead.createCell( 2).setCellValue("Asset ID");
	         rowhead.createCell( 3).setCellValue("Asset Description");
	         rowhead.createCell( 4).setCellValue("Branch Code");
	         rowhead.createCell( 5).setCellValue("Branch Name");
	         rowhead.createCell( 6).setCellValue("Category Name");
	         rowhead.createCell( 7).setCellValue("Department Name");
	         rowhead.createCell( 8).setCellValue("Purchase Date");
	         rowhead.createCell( 9).setCellValue("Depr. Start Date");
	         rowhead.createCell( 10).setCellValue("Depr. End Date");
	         rowhead.createCell( 11).setCellValue("Cost Price");
	         rowhead.createCell( 12).setCellValue("Monthly Depreciation");
			 rowhead.createCell( 13).setCellValue("Accumulated Depr.");
			 rowhead.createCell( 14).setCellValue("NBV");
			 rowhead.createCell( 15).setCellValue("Asset User");
			 rowhead.createCell( 16).setCellValue("Asset Type");
			 rowhead.createCell( 17).setCellValue("Vendor Name");
			 rowhead.createCell( 18).setCellValue("Depr. Rate");
			 rowhead.createCell( 19).setCellValue("Total Life");
			 rowhead.createCell( 20).setCellValue("Remaining Life");
			 rowhead.createCell( 21).setCellValue("Improv Cost");
			 rowhead.createCell( 22).setCellValue("Improv AccumDep");
			 rowhead.createCell( 23).setCellValue("Improv NBV");
			 rowhead.createCell( 24).setCellValue("Improv MonthlyDep");
			 rowhead.createCell( 25).setCellValue("Improv RemainLife");
			 rowhead.createCell( 26).setCellValue("Improv UsefulLife");
			 rowhead.createCell( 27).setCellValue("Improv TotalLife");
			 rowhead.createCell( 28).setCellValue("Tag");
			 
			row.createCell((short) 0).setCellValue(i);
			row.createCell((short) 1).setCellValue(oldassetId);
			row.createCell((short) 2).setCellValue(assetId);
            row.createCell((short) 3).setCellValue(Description);
            row.createCell((short) 4).setCellValue(branch_code);
            row.createCell((short) 5).setCellValue(branchName);
            row.createCell((short) 6).setCellValue(categoryName);
            row.createCell((short) 7).setCellValue(deptName);
            row.createCell((short) 8).setCellValue(purchaseDate);
            row.createCell((short) 9).setCellValue(depr_startDate);
            row.createCell((short) 10).setCellValue(depr_endDate);
            row.createCell((short) 11).setCellValue(costprice);
            row.createCell((short) 12).setCellValue(monthlyDepr);
            row.createCell((short) 13).setCellValue(accumDepr);
            row.createCell((short) 14).setCellValue(nbv);
			row.createCell((short) 15).setCellValue(assetuser);
			row.createCell((short) 16).setCellValue(assetStatus);
			row.createCell((short) 17).setCellValue(vendorName);
			row.createCell((short) 18).setCellValue(depr_Rate);
			row.createCell((short) 19).setCellValue(totalLife);
			row.createCell((short) 20).setCellValue(remain_life);
			row.createCell((short) 21).setCellValue(improv_cost);
			row.createCell((short) 22).setCellValue(improv_accum_dep);
			row.createCell((short) 23).setCellValue(improv_nbv);
			row.createCell((short) 24).setCellValue(improv_monthly_dep);
			row.createCell((short) 25).setCellValue(improv_remain_life);
			row.createCell((short) 26).setCellValue(improv_useful_life);
			row.createCell((short) 27).setCellValue(improv_total_life);
			row.createCell((short) 28).setCellValue(tag);
		
            //	System.out.println("======>oldAssetId====: "+oldAssetId+"  Index: "+i);

            i++;
//            System.out.println("we are here");
     }
//	 System.out.println("we are here 2");
	   OutputStream stream = response.getOutputStream();
         workbook.write(stream);
         stream.close();
         workbook.close();  
         System.out.println("Data is saved in excel file.");
         
    /* w.write();
     w.close(); */
     }
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
		   //System.out.println("<<<<<<<<<<<< Date: " + date);
		   String yyyy = date.substring(0, 4);
			String mm = date.substring(5, 7);
			String dd = date.substring(8, 10);
			date = dd+"/"+mm+"/"+yyyy;
		   
			
		   return date;
	   }
   
}