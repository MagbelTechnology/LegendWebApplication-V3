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
   
public class AssetAdditionExport extends HttpServlet
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
//    System.out.println("======>ToDate: "+ToDate);
    String dept_Code = request.getParameter("deptCode");
    String asset_Id = request.getParameter("assetId");
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
    String fileName = branchCode+"By"+userName+"AssetAdditionListReport.xls";    	
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
//   System.out.println("<<<<<<branch_Id: "+branch_Id+"    categoryCode: "+categoryCode+"  branchCode: "+branchCode);
     String ColQuery = "";
     if(!branch_Id.equals("0")  && !categoryCode.equals("0") && FromDate.equals("")  && ToDate.equals("")){
    	 System.out.println("======>>>>>>>Branch and Category Selected: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  dept_Code: "+dept_Code+"   asset_Id: "+asset_Id);	     
	     ColQuery ="select distinct t.asset_id, a.Description, b.BRANCH_CODE, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,"
	     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger,"
	     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date,"
	     		+ "amount,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE,t.transaction_date"
	     		+ " from AM_ASSETADDITIONS a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c,"
	     		+ "am_ad_department d, am_gb_company z"
	     		+ " where a.asset_id = t.asset_id and b.BRANCH_CODE = a.BRANCH_CODE "
	     		+ "and a.CATEGORY_CODE = c.category_code and a.DEPT_CODE = d.Dept_code and page1 = 'ASSET CREATION RAISE ENTRY' "
	     		+ "and iso ='000' and transactionId =1 "
	     		+ "AND a.category_id = ? AND a.branch_id = ? AND (a.asset_status = 'ACTIVE' OR a.asset_status = 'CLOSED') "
	     		+ "union "
//	     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,"
//	     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger,"
//	     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date,"
//	     		+ "amount,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE "
//	     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c,"
//	     		+ "am_ad_department d, am_gb_company z "
//	     		+ "where a.asset_id = t.asset_id and b.BRANCH_CODE = a.BRANCH_CODE and a.CATEGORY_CODE = c.category_code "
//	     		+ "and a.DEPT_CODE = d.Dept_code and page1 = 'ASSET CREATION RAISE ENTRY' "
//	     		+ "and cost_price = amount and iso ='000' and transactionId =24 "
//	     		+ "AND a.category_id = ? AND a.branch_id = ? AND a.Asset_Status = 'ACTIVE' "
//	     		+ "union "
//	     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,"
//	     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger,"
//	     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date,"
//	     		+ "amount,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE "
//	     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c,"
//	     		+ "am_ad_department d, am_gb_company z "
//	     		+ "where a.asset_id = t.asset_id "
//	     		+ "and b.BRANCH_CODE = a.BRANCH_CODE and a.CATEGORY_CODE = c.category_code and a.DEPT_CODE = d.Dept_code "
//	     		+ "and page1 = 'ASSET CREATION RAISE ENTRY' and cost_price = amount and iso ='000' and transactionId =30 "
//	     		+ "AND a.category_id = ? AND a.branch_id = ? AND a.Asset_Status = 'ACTIVE' "
//	     		+ "union "
//	     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,"
//	     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger,"
//	     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date,"
//	     		+ "amount,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE,t.transaction_date "
//	     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_asset_improvement m, am_ad_branch b, am_ad_category c,"
//	     		+ "am_ad_department d, am_gb_company z "
//	     		+ "where a.asset_id = t.asset_id and b.BRANCH_CODE = a.BRANCH_CODE and a.CATEGORY_CODE = c.category_code "
//	     		+ "and a.DEPT_CODE = d.Dept_code and t.asset_id = m.asset_id and page1 = 'ASSET IMPROVEMENT RAISE ENTRY' "
//	     		+ "and iso ='000'and transactionId =26 "
//	     		+ "AND a.category_id = ? AND a.branch_id = ? AND (a.asset_status = 'ACTIVE' OR a.asset_status = 'CLOSED' OR a.asset_status = 'DISPOSED') "
//	     		+ "UNION "
	     		+ "select distinct a.asset_id, a.Description, b.BRANCH_CODE, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep, "
	     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger, "
	     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date,a.cost_price AS amount, "
	     		+ "'ASSET CREATION RAISE ENTRY' AS page1, z.processing_date, z.company_name, a.BRANCH_CODE, "
	     		+ "a.CATEGORY_CODE,Posting_Date AS transaction_date from AM_ASSETADDITIONS a, dbo.GROUP_ASSET_UPLOAD t, am_ad_branch b, am_ad_category c,am_ad_department d, "
	     		+ "am_gb_company z where a.GROUP_ID = t.GROUP_ID and b.BRANCH_CODE = a.BRANCH_CODE "
	     		+ "and a.CATEGORY_CODE = c.category_code and a.DEPT_CODE = d.Dept_code  and t.GROUP_ID != 0 "
	     		+ "AND a.category_id =? AND a.branch_id =? AND (a.asset_status = 'ACTIVE' OR a.asset_status = 'CLOSED') "
	     		+ "order by page1";	
	}      
	 if(branch_Id.equals("0")  && !categoryCode.equals("0") && FromDate.equals("")  && ToDate.equals("")){	   
//	   System.out.println("======>>>>>>>Category Selected: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  dept_Code: "+dept_Code+"   asset_Id: "+asset_Id);
	     ColQuery ="select distinct t.asset_id, a.Description, b.BRANCH_CODE, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,"
		     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger,"
		     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date,"
		     		+ "amount,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE,t.transaction_date "
		     		+ " from AM_ASSETADDITIONS a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c,"
		     		+ "am_ad_department d, am_gb_company z"
		     		+ " where a.asset_id = t.asset_id and b.BRANCH_CODE = a.BRANCH_CODE "
		     		+ "and a.CATEGORY_CODE = c.category_code and a.DEPT_CODE = d.Dept_code and page1 = 'ASSET CREATION RAISE ENTRY' "
		     		+ "and iso ='000' and transactionId =1 "
		     		+ "AND a.category_id = ? AND (a.asset_status = 'ACTIVE' OR a.asset_status = 'CLOSED') "
		     		+ "union "
//		     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,"
//		     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger,"
//		     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date,"
//		     		+ "amount,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE "
//		     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c,"
//		     		+ "am_ad_department d, am_gb_company z "
//		     		+ "where a.asset_id = t.asset_id and b.BRANCH_CODE = a.BRANCH_CODE and a.CATEGORY_CODE = c.category_code "
//		     		+ "and a.DEPT_CODE = d.Dept_code and page1 = 'ASSET CREATION RAISE ENTRY' "
//		     		+ "and cost_price = amount and iso ='000' and transactionId =24 "
//		     		+ "AND a.category_id = ? AND a.Asset_Status = 'ACTIVE' "
//		     		+ "union "
//		     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,"
//		     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger,"
//		     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date,"
//		     		+ "amount,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE "
//		     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c,"
//		     		+ "am_ad_department d, am_gb_company z "
//		     		+ "where a.asset_id = t.asset_id "
//		     		+ "and b.BRANCH_CODE = a.BRANCH_CODE and a.CATEGORY_CODE = c.category_code and a.DEPT_CODE = d.Dept_code "
//		     		+ "and page1 = 'ASSET CREATION RAISE ENTRY' and cost_price = amount and iso ='000' and transactionId =30 "
//		     		+ "AND a.category_id = ? AND a.Asset_Status = 'ACTIVE' "
//		     		+ "union "
//		     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,"
//		     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger,"
//		     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date,"
//		     		+ "amount,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE,t.transaction_date "
//		     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_asset_improvement m, am_ad_branch b, am_ad_category c,"
//		     		+ "am_ad_department d, am_gb_company z "
//		     		+ "where a.asset_id = t.asset_id and b.BRANCH_CODE = a.BRANCH_CODE and a.CATEGORY_CODE = c.category_code "
//		     		+ "and a.DEPT_CODE = d.Dept_code and t.asset_id = m.asset_id and page1 = 'ASSET IMPROVEMENT RAISE ENTRY' "
//		     		+ "and iso ='000'and transactionId =26 "
//		     		+ "AND a.category_id = ? AND (a.asset_status = 'ACTIVE' OR a.asset_status = 'CLOSED' OR a.asset_status = 'DISPOSED') "
//		     		+ "UNION "
		  		+ "select distinct a.asset_id, a.Description, b.BRANCH_CODE, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep, "
		  		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger, "
		  		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date,a.cost_price AS amount, "
		  		+ "'ASSET CREATION RAISE ENTRY' AS page1, z.processing_date, z.company_name, a.BRANCH_CODE, "
		  		+ "a.CATEGORY_CODE,Posting_Date AS transaction_date from AM_ASSETADDITIONS a, dbo.GROUP_ASSET_UPLOAD t, am_ad_branch b, am_ad_category c,am_ad_department d, "
		  		+ "am_gb_company z where a.GROUP_ID = t.GROUP_ID and b.BRANCH_CODE = a.BRANCH_CODE  and t.GROUP_ID != 0 "
		  		+ "AND a.category_id =? and a.CATEGORY_CODE = c.category_code and a.DEPT_CODE = d.Dept_code  AND (a.asset_status = 'ACTIVE' OR a.asset_status = 'CLOSED') "
		  		+ "order by page1";		     
   }
	 if(!branch_Id.equals("0")  && categoryCode.equals("0") && FromDate.equals("")  && ToDate.equals("")){	   
	   System.out.println("======>>>>>>>Branch Selected: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  dept_Code: "+dept_Code+"   asset_Id: "+asset_Id);
	     ColQuery ="select distinct t.asset_id, a.Description, b.BRANCH_CODE, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,"
		     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger,"
		     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date,"
		     		+ "amount,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE,t.transaction_date "
		     		+ " from AM_ASSETADDITIONS a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c,"
		     		+ "am_ad_department d, am_gb_company z"
		     		+ " where a.asset_id = t.asset_id and b.BRANCH_CODE = a.BRANCH_CODE "
		     		+ "and a.CATEGORY_CODE = c.category_code and a.DEPT_CODE = d.Dept_code and page1 = 'ASSET CREATION RAISE ENTRY' "
		     		+ "and iso ='000' and transactionId =1 "
		     		+ "AND a.branch_id = ? AND (a.asset_status = 'ACTIVE' OR a.asset_status = 'CLOSED') "
//		     		+ "union "
//		     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,"
//		     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger,"
//		     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date,"
//		     		+ "amount,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE "
//		     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c,"
//		     		+ "am_ad_department d, am_gb_company z "
//		     		+ "where a.asset_id = t.asset_id and b.BRANCH_CODE = a.BRANCH_CODE and a.CATEGORY_CODE = c.category_code "
//		     		+ "and a.DEPT_CODE = d.Dept_code and page1 = 'ASSET CREATION RAISE ENTRY' "
//		     		+ "and cost_price = amount and iso ='000' and transactionId =24 "
//		     		+ "AND a.branch_id = ? AND a.Asset_Status = 'ACTIVE' "
//		     		+ "union "
//		     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,"
//		     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger,"
//		     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date,"
//		     		+ "amount,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE "
//		     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c,"
//		     		+ "am_ad_department d, am_gb_company z "
//		     		+ "where a.asset_id = t.asset_id "
//		     		+ "and b.BRANCH_CODE = a.BRANCH_CODE and a.CATEGORY_CODE = c.category_code and a.DEPT_CODE = d.Dept_code "
//		     		+ "and page1 = 'ASSET CREATION RAISE ENTRY' and cost_price = amount and iso ='000' and transactionId =30 "
//		     		+ "AND a.branch_id = ? AND a.Asset_Status = 'ACTIVE' "
//		     		+ "union "
//		     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,"
//		     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger,"
//		     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date,"
//		     		+ "amount,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE,t.transaction_date "
//		     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_asset_improvement m, am_ad_branch b, am_ad_category c,"
//		     		+ "am_ad_department d, am_gb_company z "
//		     		+ "where a.asset_id = t.asset_id and b.BRANCH_CODE = a.BRANCH_CODE and a.CATEGORY_CODE = c.category_code "
//		     		+ "and a.DEPT_CODE = d.Dept_code and t.asset_id = m.asset_id and page1 = 'ASSET IMPROVEMENT RAISE ENTRY' "
//		     		+ "and iso ='000'and transactionId =26 "
//		     		+ "AND a.branch_id = ? AND (a.asset_status = 'ACTIVE' OR a.asset_status = 'CLOSED' OR a.asset_status = 'DISPOSED') "
		     		+ "UNION "
		     		+ "select distinct a.asset_id, a.Description, b.BRANCH_CODE, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep, "
		     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger, "
		     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date,a.cost_price AS amount, "
		     		+ "'ASSET CREATION RAISE ENTRY' AS page1, z.processing_date, z.company_name, a.BRANCH_CODE, "
		     		+ "a.CATEGORY_CODE,Posting_Date AS transaction_date from AM_ASSETADDITIONS a, dbo.GROUP_ASSET_UPLOAD t, am_ad_branch b, am_ad_category c,am_ad_department d, "
		     		+ "am_gb_company z where a.GROUP_ID = t.GROUP_ID and b.BRANCH_CODE = a.BRANCH_CODE "
		     		+ "and a.CATEGORY_CODE = c.category_code and a.DEPT_CODE = d.Dept_code  and t.GROUP_ID != 0 "
		     		+ "AND a.branch_id =? AND (a.asset_status = 'ACTIVE' OR a.asset_status = 'CLOSED') "
		     		+ "order by page1";	
	}
   if(branch_Id.equals("0")  && categoryCode.equals("0") && FromDate.equals("")  && ToDate.equals("")){
	   System.out.println("======>>>>>>>No Selection: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  dept_Code: "+dept_Code+"   asset_Id: "+asset_Id);
	     ColQuery ="select distinct t.asset_id, a.Description, b.BRANCH_CODE, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,"
		     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger,"
		     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date,"
		     		+ "amount,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE,t.transaction_date "
		     		+ " from AM_ASSETADDITIONS a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c,"
		     		+ "am_ad_department d, am_gb_company z"
		     		+ " where a.asset_id = t.asset_id and b.BRANCH_CODE = a.BRANCH_CODE "
		     		+ "and a.CATEGORY_CODE = c.category_code and a.DEPT_CODE = d.Dept_code and page1 = 'ASSET CREATION RAISE ENTRY' "
		     		+ "and iso ='000' and transactionId =1 AND (a.asset_status = 'ACTIVE' OR a.asset_status = 'CLOSED') "
		     		+ "union "
//		     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,"
//		     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger,"
//		     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date,"
//		     		+ "amount,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE "
//		     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c,"
//		     		+ "am_ad_department d, am_gb_company z "
//		     		+ "where a.asset_id = t.asset_id and b.BRANCH_CODE = a.BRANCH_CODE and a.CATEGORY_CODE = c.category_code "
//		     		+ "and a.DEPT_CODE = d.Dept_code and page1 = 'ASSET CREATION RAISE ENTRY' "
//		     		+ "and cost_price = amount and iso ='000' and transactionId =24 AND a.Asset_Status = 'ACTIVE' "
//		     		+ "union "
//		     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,"
//		     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger,"
//		     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date,"
//		     		+ "amount,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE "
//		     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c,"
//		     		+ "am_ad_department d, am_gb_company z "
//		     		+ "where a.asset_id = t.asset_id "
//		     		+ "and b.BRANCH_CODE = a.BRANCH_CODE and a.CATEGORY_CODE = c.category_code and a.DEPT_CODE = d.Dept_code "
//		     		+ "and page1 = 'ASSET CREATION RAISE ENTRY' and cost_price = amount and iso ='000' and transactionId =30 AND a.Asset_Status = 'ACTIVE' "
//		     		+ "union "
//		     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,"
//		     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger,"
//		     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date,"
//		     		+ "amount,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE,t.transaction_date "
//		     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_asset_improvement m, am_ad_branch b, am_ad_category c,"
//		     		+ "am_ad_department d, am_gb_company z "
//		     		+ "where a.asset_id = t.asset_id and b.BRANCH_CODE = a.BRANCH_CODE and a.CATEGORY_CODE = c.category_code "
//		     		+ "and a.DEPT_CODE = d.Dept_code and t.asset_id = m.asset_id and page1 = 'ASSET IMPROVEMENT RAISE ENTRY' "
//		     		+ "and iso ='000'and transactionId =26 AND (a.asset_status = 'ACTIVE' OR a.asset_status = 'CLOSED' OR a.asset_status = 'DISPOSED') "
//		     		+ "UNION "
		     		+ "select distinct a.asset_id, a.Description, b.BRANCH_CODE, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep, "
		     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger, "
		     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date,a.cost_price AS amount, "
		     		+ "'ASSET CREATION RAISE ENTRY' AS page1, z.processing_date, z.company_name, a.BRANCH_CODE, "
		     		+ "a.CATEGORY_CODE,Posting_Date AS transaction_date from AM_ASSETADDITIONS a, dbo.GROUP_ASSET_UPLOAD t, am_ad_branch b, am_ad_category c,am_ad_department d, "
		     		+ "am_gb_company z where a.GROUP_ID = t.GROUP_ID and b.BRANCH_CODE = a.BRANCH_CODE  and t.GROUP_ID != 0 "
		     		+ "and a.CATEGORY_CODE = c.category_code and a.DEPT_CODE = d.Dept_code  AND (a.asset_status = 'ACTIVE' OR a.asset_status = 'CLOSED') "
		     		+ "order by page1";	
	     }   
   if(!FromDate.equals("")  && !ToDate.equals("") && branch_Id.equals("0")  && categoryCode.equals("0")){
	System.out.println("======>>>>>>>No Selection but Date selected: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  dept_Code: "+dept_Code+"   asset_Id: "+asset_Id);
    ColQuery ="select distinct t.asset_id, a.Description, b.BRANCH_CODE, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,"
     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger,"
     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date,"
     		+ "amount,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE,t.transaction_date "
     		+ " from AM_ASSETADDITIONS a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c,"
     		+ "am_ad_department d, am_gb_company z"
     		+ " where a.asset_id = t.asset_id and b.BRANCH_CODE = a.BRANCH_CODE "
     		+ "and a.CATEGORY_CODE = c.category_code and a.DEPT_CODE = d.Dept_code and page1 = 'ASSET CREATION RAISE ENTRY' "
     		+ "and iso ='000' and transactionId =1 "
     		+ "AND a.Posting_Date BETWEEN ? AND ?  AND (a.asset_status = 'ACTIVE' OR a.asset_status = 'CLOSED') "
//     		+ "union "
//     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,"
//     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger,"
//     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date,"
//     		+ "amount,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE "
//     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c,"
//     		+ "am_ad_department d, am_gb_company z "
//     		+ "where a.asset_id = t.asset_id and b.BRANCH_CODE = a.BRANCH_CODE and a.CATEGORY_CODE = c.category_code "
//     		+ "and a.DEPT_CODE = d.Dept_code and page1 = 'ASSET CREATION RAISE ENTRY' "
//     		+ "and cost_price = amount and iso ='000' and transactionId =24 "
//     		+ "AND a.Posting_Date BETWEEN ? AND ?  AND a.Asset_Status = 'ACTIVE' "
//     		+ "union "
//     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,"
//     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger,"
//     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date,"
//     		+ "amount,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE "
//     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c,"
//     		+ "am_ad_department d, am_gb_company z "
//     		+ "where a.asset_id = t.asset_id "
//     		+ "and b.BRANCH_CODE = a.BRANCH_CODE and a.CATEGORY_CODE = c.category_code and a.DEPT_CODE = d.Dept_code "
//     		+ "and page1 = 'ASSET CREATION RAISE ENTRY' and cost_price = amount and iso ='000' and transactionId =30 "
//     		+ "AND a.Posting_Date BETWEEN ? AND ?  AND a.Asset_Status = 'ACTIVE' "
//     		+ "union "
//     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,"
//     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger,"
//     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date,"
//     		+ "amount,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE,t.transaction_date "
//     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_asset_improvement m, am_ad_branch b, am_ad_category c,"
//     		+ "am_ad_department d, am_gb_company z "
//     		+ "where a.asset_id = t.asset_id and b.BRANCH_CODE = a.BRANCH_CODE and a.CATEGORY_CODE = c.category_code "
//     		+ "and a.DEPT_CODE = d.Dept_code and t.asset_id = m.asset_id and page1 = 'ASSET IMPROVEMENT RAISE ENTRY' "
//     		+ "and iso ='000'and transactionId =26 "
//     		+ "AND a.Posting_Date BETWEEN ? AND ?  AND (a.asset_status = 'ACTIVE' OR a.asset_status = 'CLOSED' OR a.asset_status = 'DISPOSED') "
     		+ "UNION "
     		+ "select distinct a.asset_id, a.Description, b.BRANCH_CODE, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep, "
     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger, "
     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date,a.cost_price AS amount, "
     		+ "'ASSET CREATION RAISE ENTRY' AS page1, z.processing_date, z.company_name, a.BRANCH_CODE, "
     		+ "a.CATEGORY_CODE,Posting_Date AS transaction_date from AM_ASSETADDITIONS a, dbo.GROUP_ASSET_UPLOAD t, am_ad_branch b, am_ad_category c,am_ad_department d, "
     		+ "am_gb_company z where a.GROUP_ID = t.GROUP_ID and b.BRANCH_CODE = a.BRANCH_CODE "
     		+ "and a.CATEGORY_CODE = c.category_code and a.DEPT_CODE = d.Dept_code  and t.GROUP_ID != 0 "
     		+ "AND a.Posting_Date BETWEEN ? AND ?  AND (a.asset_status = 'ACTIVE' OR a.asset_status = 'CLOSED') "
     		+ "order by page1";	
	} 
   if(!FromDate.equals("")  && !ToDate.equals("") && !branch_Id.equals("0")  && categoryCode.equals("0")){
	System.out.println("======>>>>>>>Branch and Date Selection: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  dept_Code: "+dept_Code+"   asset_Id: "+asset_Id);
    ColQuery ="select distinct t.asset_id, a.Description, b.BRANCH_CODE, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,"
     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger,"
     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date,"
     		+ "amount,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE,t.transaction_date "
     		+ " from AM_ASSETADDITIONS a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c,"
     		+ "am_ad_department d, am_gb_company z"
     		+ " where a.asset_id = t.asset_id and b.BRANCH_CODE = a.BRANCH_CODE "
     		+ "and a.CATEGORY_CODE = c.category_code and a.DEPT_CODE = d.Dept_code and page1 = 'ASSET CREATION RAISE ENTRY' "
     		+ "and iso ='000' and transactionId =1 "
     		+ "AND a.branch_id =? AND a.Posting_Date BETWEEN ? AND ?  AND (a.asset_status = 'ACTIVE' OR a.asset_status = 'CLOSED') "
//     		+ "union "
//     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,"
//     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger,"
//     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date,"
//     		+ "amount,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE "
//     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c,"
//     		+ "am_ad_department d, am_gb_company z "
//     		+ "where a.asset_id = t.asset_id and b.BRANCH_CODE = a.BRANCH_CODE and a.CATEGORY_CODE = c.category_code "
//     		+ "and a.DEPT_CODE = d.Dept_code and page1 = 'ASSET CREATION RAISE ENTRY' "
//     		+ "and cost_price = amount and iso ='000' and transactionId =24 "
//     		+ "AND a.branch_id =? AND a.Posting_Date BETWEEN ? AND ?  AND a.Asset_Status = 'ACTIVE' "
//     		+ "union "
//     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,"
//     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger,"
//     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date,"
//     		+ "amount,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE "
//     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c,"
//     		+ "am_ad_department d, am_gb_company z "
//     		+ "where a.asset_id = t.asset_id "
//     		+ "and b.BRANCH_CODE = a.BRANCH_CODE and a.CATEGORY_CODE = c.category_code and a.DEPT_CODE = d.Dept_code "
//     		+ "and page1 = 'ASSET CREATION RAISE ENTRY' and cost_price = amount and iso ='000' and transactionId =30 "
//     		+ "AND a.branch_id =? AND a.Posting_Date BETWEEN ? AND ?  AND a.Asset_Status = 'ACTIVE' "
//     		+ "union "
//     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,"
//     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger,"
//     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date,"
//     		+ "amount,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE,t.transaction_date "
//     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_asset_improvement m, am_ad_branch b, am_ad_category c,"
//     		+ "am_ad_department d, am_gb_company z "
//     		+ "where a.asset_id = t.asset_id and b.BRANCH_CODE = a.BRANCH_CODE and a.CATEGORY_CODE = c.category_code "
//     		+ "and a.DEPT_CODE = d.Dept_code and t.asset_id = m.asset_id and page1 = 'ASSET IMPROVEMENT RAISE ENTRY' "
//     		+ "and iso ='000'and transactionId =26 "
//     		+ "AND a.branch_id =? AND a.Posting_Date BETWEEN ? AND ?  AND (a.asset_status = 'ACTIVE' OR a.asset_status = 'CLOSED' OR a.asset_status = 'DISPOSED') "
     		+ "UNION "
     		+ "select distinct a.asset_id, a.Description, b.BRANCH_CODE, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep, "
     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger, "
     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date,a.cost_price AS amount, "
     		+ "'ASSET CREATION RAISE ENTRY' AS page1, z.processing_date, z.company_name, a.BRANCH_CODE, "
     		+ "a.CATEGORY_CODE,Posting_Date AS transaction_date from AM_ASSETADDITIONS a, dbo.GROUP_ASSET_UPLOAD t, am_ad_branch b, am_ad_category c,am_ad_department d, "
     		+ "am_gb_company z where a.GROUP_ID = t.GROUP_ID and b.BRANCH_CODE = a.BRANCH_CODE "
     		+ "and a.CATEGORY_CODE = c.category_code and a.DEPT_CODE = d.Dept_code  and t.GROUP_ID != 0 "
     		+ "AND a.branch_id =? AND a.Posting_Date BETWEEN ? AND ?  AND (a.asset_status = 'ACTIVE' OR a.asset_status = 'CLOSED') "
     		+ "order by page1";	
    }    
   if(!FromDate.equals("")  && !ToDate.equals("") && !branch_Id.equals("0")  && !categoryCode.equals("0")){
	System.out.println("======>>>>>>>Branch, Category and Date Selection: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  dept_Code: "+dept_Code+"   asset_Id: "+asset_Id);
    ColQuery ="select distinct t.asset_id, a.Description, b.BRANCH_CODE, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,"
     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger,"
     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date,"
     		+ "amount,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE"
     		+ " from AM_ASSETADDITIONS a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c,"
     		+ "am_ad_department d, am_gb_company z"
     		+ " where a.asset_id = t.asset_id and b.BRANCH_CODE = a.BRANCH_CODE "
     		+ "and a.CATEGORY_CODE = c.category_code and a.DEPT_CODE = d.Dept_code and page1 = 'ASSET CREATION RAISE ENTRY' "
     		+ "and iso ='000' and transactionId =1 "
     		+ "AND a.branch_id =? AND a.category_id =? AND a.Posting_Date BETWEEN ? AND ?  AND (a.asset_status = 'ACTIVE' OR a.asset_status = 'CLOSED') "
//     		+ "union "
//     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,"
//     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger,"
//     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date,"
//     		+ "amount,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE "
//     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c,"
//     		+ "am_ad_department d, am_gb_company z "
//     		+ "where a.asset_id = t.asset_id and b.BRANCH_CODE = a.BRANCH_CODE and a.CATEGORY_CODE = c.category_code "
//     		+ "and a.DEPT_CODE = d.Dept_code and page1 = 'ASSET CREATION RAISE ENTRY' "
//     		+ "and cost_price = amount and iso ='000' and transactionId =24 "
//     		+ "AND a.branch_id =? AND a.category_id =? AND a.Posting_Date BETWEEN ? AND ?  AND a.Asset_Status = 'ACTIVE' "
//     		+ "union "
//     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,"
//     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger,"
//     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date,"
//     		+ "amount,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE "
//     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c,"
//     		+ "am_ad_department d, am_gb_company z "
//     		+ "where a.asset_id = t.asset_id "
//     		+ "and b.BRANCH_CODE = a.BRANCH_CODE and a.CATEGORY_CODE = c.category_code and a.DEPT_CODE = d.Dept_code "
//     		+ "and page1 = 'ASSET CREATION RAISE ENTRY' and cost_price = amount and iso ='000' and transactionId =30 "
//     		+ "AND a.branch_id =? AND a.category_id =? AND a.Posting_Date BETWEEN ? AND ?  AND a.Asset_Status = 'ACTIVE' "
//     		+ "union "
//     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,"
//     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger,"
//     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date,"
//     		+ "amount,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE,t.transaction_date "
//     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_asset_improvement m, am_ad_branch b, am_ad_category c,"
//     		+ "am_ad_department d, am_gb_company z "
//     		+ "where a.asset_id = t.asset_id and b.BRANCH_CODE = a.BRANCH_CODE and a.CATEGORY_CODE = c.category_code "
//     		+ "and a.DEPT_CODE = d.Dept_code and t.asset_id = m.asset_id and page1 = 'ASSET IMPROVEMENT RAISE ENTRY' "
//     		+ "and iso ='000'and transactionId =26 "
//     		+ "AND a.branch_id =? AND a.category_id =? AND a.Posting_Date BETWEEN ? AND ?  AND (a.asset_status = 'ACTIVE' OR a.asset_status = 'CLOSED' OR a.asset_status = 'DISPOSED') "
     		+ "UNION "
			+ "select distinct a.asset_id, a.Description, b.BRANCH_CODE, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep, "
			+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger, "
			+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date,a.cost_price AS amount, "
			+ "'ASSET CREATION RAISE ENTRY' AS page1, z.processing_date, z.company_name, a.BRANCH_CODE, "
			+ "a.CATEGORY_CODE,Posting_Date AS transaction_date from AM_ASSETADDITIONS a, dbo.GROUP_ASSET_UPLOAD t, am_ad_branch b, am_ad_category c,am_ad_department d, "
			+ "am_gb_company z where a.GROUP_ID = t.GROUP_ID and b.BRANCH_CODE = a.BRANCH_CODE  and t.GROUP_ID != 0 "
			+ "AND a.branch_id =? AND a.category_id =? AND a.Posting_Date BETWEEN ? AND ? and a.CATEGORY_CODE = c.category_code and a.DEPT_CODE = d.Dept_code  AND (a.asset_status = 'ACTIVE' OR a.asset_status = 'CLOSED') "
			+ "order by page1";	    
	}    

   if(!FromDate.equals("")  && !ToDate.equals("") && branch_Id.equals("0")  && !categoryCode.equals("0")){
		System.out.println("======>>>>>>>Asset Id Selection: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  dept_Code: "+dept_Code+"   asset_Id: "+asset_Id);
	     ColQuery ="select distinct t.asset_id, a.Description, b.BRANCH_CODE, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,"
		     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger,"
		     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date,"
		     		+ "amount,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE,t.transaction_date "
		     		+ " from AM_ASSETADDITIONS a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c,"
		     		+ "am_ad_department d, am_gb_company z"
		     		+ " where a.asset_id = t.asset_id and b.BRANCH_CODE = a.BRANCH_CODE "
		     		+ "and a.CATEGORY_CODE = c.category_code and a.DEPT_CODE = d.Dept_code and page1 = 'ASSET CREATION RAISE ENTRY' "
		     		+ "and iso ='000' and transactionId =1 "
		     		+ "AND a.category_id =? AND a.Posting_Date BETWEEN ? AND ?  AND (a.asset_status = 'ACTIVE' OR a.asset_status = 'CLOSED') "
//		     		+ "union "
//		     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,"
//		     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger,"
//		     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date,"
//		     		+ "amount,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE "
//		     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c,"
//		     		+ "am_ad_department d, am_gb_company z "
//		     		+ "where a.asset_id = t.asset_id and b.BRANCH_CODE = a.BRANCH_CODE and a.CATEGORY_CODE = c.category_code "
//		     		+ "and a.DEPT_CODE = d.Dept_code and page1 = 'ASSET CREATION RAISE ENTRY' "
//		     		+ "and cost_price = amount and iso ='000' and transactionId =24 "
//		     		+ "AND a.category_id =? AND a.Posting_Date BETWEEN ? AND ?  AND a.Asset_Status = 'ACTIVE' "
//		     		+ "union "
//		     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,"
//		     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger,"
//		     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date,"
//		     		+ "amount,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE "
//		     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c,"
//		     		+ "am_ad_department d, am_gb_company z "
//		     		+ "where a.asset_id = t.asset_id "
//		     		+ "and b.BRANCH_CODE = a.BRANCH_CODE and a.CATEGORY_CODE = c.category_code and a.DEPT_CODE = d.Dept_code "
//		     		+ "and page1 = 'ASSET CREATION RAISE ENTRY' and cost_price = amount and iso ='000' and transactionId =30 "
//		     		+ "AND a.category_id =? AND a.Posting_Date BETWEEN ? AND ?  AND a.Asset_Status = 'ACTIVE' "
//		     		+ "union "
//		     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,"
//		     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger,"
//		     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date,"
//		     		+ "amount,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE,t.transaction_date "
//		     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_asset_improvement m, am_ad_branch b, am_ad_category c,"
//		     		+ "am_ad_department d, am_gb_company z "
//		     		+ "where a.asset_id = t.asset_id and b.BRANCH_CODE = a.BRANCH_CODE and a.CATEGORY_CODE = c.category_code "
//		     		+ "and a.DEPT_CODE = d.Dept_code and t.asset_id = m.asset_id and page1 = 'ASSET IMPROVEMENT RAISE ENTRY' "
//		     		+ "and iso ='000'and transactionId =26 "
//		     		+ "AND a.category_id =? AND a.Posting_Date BETWEEN ? AND ?  AND (a.asset_status = 'ACTIVE' OR a.asset_status = 'CLOSED' OR a.asset_status = 'DISPOSED') "
		     		+ "UNION "
		     		+ "select distinct a.asset_id, a.Description, b.BRANCH_CODE, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep, "
		     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger, "
		     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date,a.cost_price AS amount, "
		     		+ "'ASSET CREATION RAISE ENTRY' AS page1, z.processing_date, z.company_name, a.BRANCH_CODE, "
		     		+ "a.CATEGORY_CODE,Posting_Date AS transaction_date from AM_ASSETADDITIONS a, dbo.GROUP_ASSET_UPLOAD t, am_ad_branch b, am_ad_category c,am_ad_department d, "
		     		+ "am_gb_company z where a.GROUP_ID = t.GROUP_ID and b.BRANCH_CODE = a.BRANCH_CODE "
		     		+ "and a.CATEGORY_CODE = c.category_code and a.DEPT_CODE = d.Dept_code  and t.GROUP_ID != 0 "
		     		+ "AND a.category_id =? AND a.Posting_Date BETWEEN ? AND ?  AND (a.asset_status = 'ACTIVE' OR a.asset_status = 'CLOSED') "
		     		+ "order by page1";		
	     }
   System.out.println("======>>>>>>>ColQuery: "+ColQuery); 
     java.util.ArrayList list =rep.getAssetAdditionRecords(ColQuery,branch_Id,categoryCode,FromDate,ToDate,asset_Id);
     System.out.println("======>>>>>>>list size: "+list.size());
     if(list.size()!=0){ 
  
    	 SXSSFWorkbook workbook = new SXSSFWorkbook();
         Sheet sheet = workbook.createSheet("Excel");
         Row rowhead = sheet.createRow((int) 0);
         
         rowhead.createCell((short) 0).setCellValue("S/No.");
         rowhead.createCell((short) 1).setCellValue("Asset ID");
         rowhead.createCell((short) 2).setCellValue("Asset Description"); 
         rowhead.createCell((short) 3).setCellValue("Branch Code");
         rowhead.createCell((short) 4).setCellValue("Branch Name");
         rowhead.createCell((short) 5).setCellValue("Category Name");
         rowhead.createCell((short) 6).setCellValue("Purchase Date");
         rowhead.createCell((short) 7).setCellValue("Depr. Start Date");
         rowhead.createCell((short) 8).setCellValue("Addition Date");
         rowhead.createCell((short) 9).setCellValue("Depr. End Date");
         rowhead.createCell((short) 10).setCellValue("Posting Date");
         rowhead.createCell((short) 11).setCellValue("Cost Price");
         rowhead.createCell((short) 12).setCellValue("Posted Amount");
         rowhead.createCell((short) 13).setCellValue("Transaction Type");

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
			String branch_code = newassettrans.getBranchCode();
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
			String finaclePostedDate = newassettrans.getPostingDate();
			String additionDate = newassettrans.getSpare1();
			double amount = newassettrans.getAmount();
			String transType = newassettrans.getTranType();
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
			
//			System.out.println("======>depr_startDate====: "+depr_startDate+"      finaclePostedDate: "+finaclePostedDate);
			
			String depr_endDate = newassettrans.getDependDate() != null ? getDate(newassettrans.getDependDate()) : "";

//			System.out.println("======>depr_endDate====: "+depr_endDate+"       additionDate: "+additionDate+"   amount: "+amount);
			
			
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
			
			
//String value = "16/06/2023";
			Row row = sheet.createRow((int) i);

			//System.out.println("we are here 5");
			row.createCell((short) 0).setCellValue(i);
			row.createCell((short) 1).setCellValue(assetId);
            row.createCell((short) 2).setCellValue(Description);
            row.createCell((short) 3).setCellValue(branch_code);
            row.createCell((short) 4).setCellValue(branchName);
            row.createCell((short) 5).setCellValue(categoryName);
            row.createCell((short) 6).setCellValue(purchaseDate);
            row.createCell((short) 7).setCellValue(depr_startDate);
            row.createCell((short) 8).setCellValue(additionDate);
            row.createCell((short) 9).setCellValue(depr_endDate);
            row.createCell((short) 10).setCellValue(finaclePostedDate);
            row.createCell((short) 11).setCellValue(costprice);
            row.createCell((short) 12).setCellValue(amount);
            row.createCell((short) 13).setCellValue(transType);
			
		//	System.out.println("======>oldAssetId====: "+oldAssetId+"  Index: "+i);

            i++;
     }
//	 System.out.println("we are here 2");
	   OutputStream stream = response.getOutputStream();
         workbook.write(stream);
        // stream.close();
         workbook.close();  
//         System.out.println("Data is saved in excel file.");
         
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