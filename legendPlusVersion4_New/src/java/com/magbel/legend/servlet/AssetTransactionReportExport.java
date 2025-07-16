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
   
public class AssetTransactionReportExport extends HttpServlet
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
    String report = request.getParameter("report");
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
    String fileName = "";
//    if(report.equalsIgnoreCase("rptMenuBCRList")){fileName = branchCode+"By"+userName+"AssetRegisterListReport.xlsx";}
//    if(report.equalsIgnoreCase("rptMenuBCLDTL")){fileName = branchCode+"By"+userName+"AssetDetailReport.xlsx";}
    if(report.equalsIgnoreCase("rptMenuBATRA")){fileName = branchCode+"By"+userName+"AssetTransactionReport.xlsx";}
    
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
     if(branch_Id.equals("0")  && categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")){
    	   System.out.println("======>>>>>>>No Selection: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate);
    	     ColQuery ="select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,\n"
    	     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger, \n"
    	     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date as Legacy_Posted_Date,\n"
    	     		+ "amount,transaction_date,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE\n"
    	     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c, \n"
    	     		+ "am_ad_department d, am_gb_company z\n"
    	     		+ "where a.asset_id = t.asset_id\n"
    	     		+ "and b.BRANCH_CODE = a.BRANCH_CODE\n"
    	     		+ "and a.CATEGORY_CODE = c.category_code\n"
    	     		+ "and a.DEPT_CODE = d.Dept_code\n"
    	     		+ "and cost_price = amount\n"
    	     		+ "and iso ='000'\n"
    	     		+ "and transactionId in ('1','8','32','20','4','15')\n"
    	     		+ "union\n"
    	     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,\n"
    	     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger, \n"
    	     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date as Legacy_Posted_Date,\n"
    	     		+ "amount,transaction_date,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE\n"
    	     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c, \n"
    	     		+ "am_ad_department d, am_gb_company z\n"
    	     		+ "where a.asset_id = t.asset_id\n"
    	     		+ "and b.BRANCH_CODE = a.BRANCH_CODE\n"
    	     		+ "and a.CATEGORY_CODE = c.category_code\n"
    	     		+ "and a.DEPT_CODE = d.Dept_code\n"
    	     		+ "and page1 = 'ASSET CREATION RAISE ENTRY'\n"
    	     		+ "and cost_price = amount\n"
    	     		+ "and iso ='000' and transactionId =24\n"
    	     		+ "union\n"
    	     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,\n"
    	     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger, \n"
    	     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date as Legacy_Posted_Date,\n"
    	     		+ "amount,transaction_date,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE\n"
    	     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c, \n"
    	     		+ "am_ad_department d, am_gb_company z\n"
    	     		+ "where a.asset_id = t.asset_id\n"
    	     		+ "and b.BRANCH_CODE = a.BRANCH_CODE\n"
    	     		+ "and a.CATEGORY_CODE = c.category_code\n"
    	     		+ "and a.DEPT_CODE = d.Dept_code\n"
    	     		+ "and page1 = 'ASSET CREATION RAISE ENTRY'\n"
    	     		+ "and cost_price = amount\n"
    	     		+ "and iso ='000' and transactionId =30\n"
    	     		+ "union\n"
    	     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,\n"
    	     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger, \n"
    	     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date as Legacy_Posted_Date, \n"
    	     		+ "amount,transaction_date,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE\n"
    	     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_asset_improvement m, am_ad_branch b, am_ad_category c, \n"
    	     		+ "am_ad_department d, am_gb_company z\n"
    	     		+ "where a.asset_id = t.asset_id\n"
    	     		+ "and b.BRANCH_CODE = a.BRANCH_CODE\n"
    	     		+ "and a.CATEGORY_CODE = c.category_code\n"
    	     		+ "and a.DEPT_CODE = d.Dept_code\n"
    	     		+ "and t.asset_id = m.asset_id\n"
    	     		+ "and page1 = 'ASSET IMPROVEMENT RAISE ENTRY'\n"
    	     		+ "and m.cost_price = amount\n"
    	     		+ "and iso ='000'and transactionId =26\n"
    	     		+ "\n"
    	     		+ "ORDER BY\n"
    	     		+ "     a.BRANCH_CODE ASC,\n"
    	     		+ "     a.CATEGORY_CODE ASC";    
    		}
       if(branch_Id.equals("0")  && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")){
      	 System.out.println("======>>>>>>>Date Selected: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate);	     
  	     ColQuery ="select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,\n"
  	     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger, \n"
  	     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date as Legacy_Posted_Date,\n"
  	     		+ "amount,transaction_date,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE\n"
  	     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c, \n"
  	     		+ "am_ad_department d, am_gb_company z\n"
  	     		+ "where a.asset_id = t.asset_id\n"
  	     		+ "and b.BRANCH_CODE = a.BRANCH_CODE\n"
  	     		+ "and a.CATEGORY_CODE = c.category_code\n"
  	     		+ "and a.DEPT_CODE = d.Dept_code\n"
  	     		+ "and cost_price = amount\n"
  	     		+ "and iso ='000'\n"
  	     		+ "and transactionId in ('1','8','32','20','4','15')\n"
  	     		+ "and t.transaction_date BETWEEN ? AND ? \n"
  	     		+ "union\n"
  	     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,\n"
  	     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger, \n"
  	     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date as Legacy_Posted_Date,\n"
  	     		+ "amount,transaction_date,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE\n"
  	     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c, \n"
  	     		+ "am_ad_department d, am_gb_company z\n"
  	     		+ "where a.asset_id = t.asset_id\n"
  	     		+ "and b.BRANCH_CODE = a.BRANCH_CODE\n"
  	     		+ "and a.CATEGORY_CODE = c.category_code\n"
  	     		+ "and a.DEPT_CODE = d.Dept_code\n"
  	     		+ "and page1 = 'ASSET CREATION RAISE ENTRY'\n"
  	     		+ "and cost_price = amount\n"
  	     		+ "and iso ='000' and transactionId =24\n"
  	     		+ "and t.transaction_date BETWEEN ? AND ? \n"
  	     		+ "union\n"
  	     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,\n"
  	     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger, \n"
  	     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date as Legacy_Posted_Date,\n"
  	     		+ "amount,transaction_date,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE\n"
  	     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c, \n"
  	     		+ "am_ad_department d, am_gb_company z\n"
  	     		+ "where a.asset_id = t.asset_id\n"
  	     		+ "and b.BRANCH_CODE = a.BRANCH_CODE\n"
  	     		+ "and a.CATEGORY_CODE = c.category_code\n"
  	     		+ "and a.DEPT_CODE = d.Dept_code\n"
  	     		+ "and page1 = 'ASSET CREATION RAISE ENTRY'\n"
  	     		+ "and cost_price = amount\n"
  	     		+ "and iso ='000' and transactionId =30\n"
  	     		+ "and t.transaction_date BETWEEN ? AND ? \n"
  	     		+ "\n"
  	     		+ "union\n"
  	     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,\n"
  	     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger, \n"
  	     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date as Legacy_Posted_Date, \n"
  	     		+ "amount,transaction_date,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE\n"
  	     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_asset_improvement m, am_ad_branch b, am_ad_category c, \n"
  	     		+ "am_ad_department d, am_gb_company z\n"
  	     		+ "where a.asset_id = t.asset_id\n"
  	     		+ "and b.BRANCH_CODE = a.BRANCH_CODE\n"
  	     		+ "and a.CATEGORY_CODE = c.category_code\n"
  	     		+ "and a.DEPT_CODE = d.Dept_code\n"
  	     		+ "and t.asset_id = m.asset_id\n"
  	     		+ "and page1 = 'ASSET IMPROVEMENT RAISE ENTRY'\n"
  	     		+ "and m.cost_price = amount\n"
  	     		+ "and iso ='000'and transactionId =26\n"
  	     		+ "and t.transaction_date BETWEEN ? AND ? \n"
  	     		+ "\n"
  	     		+ "ORDER BY\n"
  	     		+ "     a.BRANCH_CODE ASC,\n"
  	     		+ "     a.CATEGORY_CODE ASC";  
  	}      
  	 if(!branch_Id.equals("0")  && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")){	   
  	   System.out.println("======>>>>>>>Branch and Date Selected: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate);
  	     ColQuery ="select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,\n"
  	     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger, \n"
  	     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date as Legacy_Posted_Date,\n"
  	     		+ "amount,transaction_date,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE\n"
  	     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c, \n"
  	     		+ "am_ad_department d, am_gb_company z\n"
  	     		+ "where a.asset_id = t.asset_id\n"
  	     		+ "and b.BRANCH_CODE = a.BRANCH_CODE\n"
  	     		+ "and a.CATEGORY_CODE = c.category_code\n"
  	     		+ "and a.DEPT_CODE = d.Dept_code\n"
  	     		+ "and cost_price = amount\n"
  	     		+ "and iso ='000'\n"
  	     		+ "and transactionId in ('1','8','32','20','4','15')\n"
  	     		+ "and a.Branch_ID=? \n"
  	     		+ "and t.transaction_date BETWEEN ? AND ? \n"
  	     		+ "union\n"
  	     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,\n"
  	     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger, \n"
  	     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date as Legacy_Posted_Date,\n"
  	     		+ "amount,transaction_date,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE\n"
  	     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c, \n"
  	     		+ "am_ad_department d, am_gb_company z\n"
  	     		+ "where a.asset_id = t.asset_id\n"
  	     		+ "and b.BRANCH_CODE = a.BRANCH_CODE\n"
  	     		+ "and a.CATEGORY_CODE = c.category_code\n"
  	     		+ "and a.DEPT_CODE = d.Dept_code\n"
  	     		+ "and page1 = 'ASSET CREATION RAISE ENTRY'\n"
  	     		+ "and cost_price = amount\n"
  	     		+ "and iso ='000' and transactionId =24\n"
  	     		+ "and a.Branch_ID=? \n"
  	     		+ "and t.transaction_date BETWEEN ? AND ? \n"
  	     		+ "union\n"
  	     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,\n"
  	     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger, \n"
  	     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date as Legacy_Posted_Date,\n"
  	     		+ "amount,transaction_date,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE\n"
  	     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c, \n"
  	     		+ "am_ad_department d, am_gb_company z\n"
  	     		+ "where a.asset_id = t.asset_id\n"
  	     		+ "and b.BRANCH_CODE = a.BRANCH_CODE\n"
  	     		+ "and a.CATEGORY_CODE = c.category_code\n"
  	     		+ "and a.DEPT_CODE = d.Dept_code\n"
  	     		+ "and page1 = 'ASSET CREATION RAISE ENTRY'\n"
  	     		+ "and cost_price = amount\n"
  	     		+ "and iso ='000' \n"
  	     		+ "and transactionId =30\n"
  	     		+ "and a.Branch_ID=? \n"
  	     		+ "and t.transaction_date BETWEEN ? AND ? \n"
  	     		+ "\n"
  	     		+ "union\n"
  	     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,\n"
  	     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger, \n"
  	     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date as Legacy_Posted_Date, \n"
  	     		+ "amount,transaction_date,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE\n"
  	     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_asset_improvement m, am_ad_branch b, am_ad_category c, \n"
  	     		+ "am_ad_department d, am_gb_company z\n"
  	     		+ "where a.asset_id = t.asset_id\n"
  	     		+ "and b.BRANCH_CODE = a.BRANCH_CODE\n"
  	     		+ "and a.CATEGORY_CODE = c.category_code\n"
  	     		+ "and a.DEPT_CODE = d.Dept_code\n"
  	     		+ "and t.asset_id = m.asset_id\n"
  	     		+ "and page1 = 'ASSET IMPROVEMENT RAISE ENTRY'\n"
  	     		+ "and m.cost_price = amount\n"
  	     		+ "and iso ='000'and transactionId =26\n"
  	     		+ "and a.Branch_ID=? \n"
  	     		+ "and t.transaction_date BETWEEN ? AND ? \n"
  	     		+ "\n"
  	     		+ "ORDER BY\n"
  	     		+ "     a.BRANCH_CODE ASC,\n"
  	     		+ "     a.CATEGORY_CODE ASC";   
  		}  
  	 
  	if(!branch_Id.equals("0")  && categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")){	   
   	   System.out.println("======>>>>>>>Branch Selected: "+branch_Id);
   	     ColQuery ="select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,\n"
   	     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger, \n"
   	     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date as Legacy_Posted_Date,\n"
   	     		+ "amount,transaction_date,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE\n"
   	     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c, \n"
   	     		+ "am_ad_department d, am_gb_company z\n"
   	     		+ "where a.asset_id = t.asset_id\n"
   	     		+ "and b.BRANCH_CODE = a.BRANCH_CODE\n"
   	     		+ "and a.CATEGORY_CODE = c.category_code\n"
   	     		+ "and a.DEPT_CODE = d.Dept_code\n"
   	     		+ "and cost_price = amount\n"
   	     		+ "and iso ='000'\n"
   	     		+ "and transactionId in ('1','8','32','20','4','15')\n"
   	     		+ "and a.Branch_ID=? \n"
   	     		+ "union\n"
   	     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,\n"
   	     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger, \n"
   	     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date as Legacy_Posted_Date,\n"
   	     		+ "amount,transaction_date,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE\n"
   	     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c, \n"
   	     		+ "am_ad_department d, am_gb_company z\n"
   	     		+ "where a.asset_id = t.asset_id\n"
   	     		+ "and b.BRANCH_CODE = a.BRANCH_CODE\n"
   	     		+ "and a.CATEGORY_CODE = c.category_code\n"
   	     		+ "and a.DEPT_CODE = d.Dept_code\n"
   	     		+ "and page1 = 'ASSET CREATION RAISE ENTRY'\n"
   	     		+ "and cost_price = amount\n"
   	     		+ "and iso ='000' and transactionId =24\n"
   	     		+ "and a.Branch_ID=? \n"
   	     		+ "union\n"
   	     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,\n"
   	     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger, \n"
   	     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date as Legacy_Posted_Date,\n"
   	     		+ "amount,transaction_date,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE\n"
   	     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c, \n"
   	     		+ "am_ad_department d, am_gb_company z\n"
   	     		+ "where a.asset_id = t.asset_id\n"
   	     		+ "and b.BRANCH_CODE = a.BRANCH_CODE\n"
   	     		+ "and a.CATEGORY_CODE = c.category_code\n"
   	     		+ "and a.DEPT_CODE = d.Dept_code\n"
   	     		+ "and page1 = 'ASSET CREATION RAISE ENTRY'\n"
   	     		+ "and cost_price = amount\n"
   	     		+ "and iso ='000' \n"
   	     		+ "and transactionId =30\n"
   	     		+ "and a.Branch_ID=? \n"
   	     		+ "\n"
   	     		+ "union\n"
   	     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,\n"
   	     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger, \n"
   	     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date as Legacy_Posted_Date, \n"
   	     		+ "amount,transaction_date,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE\n"
   	     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_asset_improvement m, am_ad_branch b, am_ad_category c, \n"
   	     		+ "am_ad_department d, am_gb_company z\n"
   	     		+ "where a.asset_id = t.asset_id\n"
   	     		+ "and b.BRANCH_CODE = a.BRANCH_CODE\n"
   	     		+ "and a.CATEGORY_CODE = c.category_code\n"
   	     		+ "and a.DEPT_CODE = d.Dept_code\n"
   	     		+ "and t.asset_id = m.asset_id\n"
   	     		+ "and page1 = 'ASSET IMPROVEMENT RAISE ENTRY'\n"
   	     		+ "and m.cost_price = amount\n"
   	     		+ "and iso ='000'and transactionId =26\n"
   	     		+ "and a.Branch_ID=? \n"
   	     		+ "\n"
   	     		+ "ORDER BY\n"
   	     		+ "     a.BRANCH_CODE ASC,\n"
   	     		+ "     a.CATEGORY_CODE ASC";   
   		}  
  	
  	 if(branch_Id.equals("0")  && !categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")){	   
  	   System.out.println("======>>>>>>>Category and Date Selected: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate);
  	     ColQuery ="select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,\n"
  	     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger, \n"
  	     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date as Legacy_Posted_Date,\n"
  	     		+ "amount,transaction_date,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE\n"
  	     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c, \n"
  	     		+ "am_ad_department d, am_gb_company z\n"
  	     		+ "where a.asset_id = t.asset_id\n"
  	     		+ "and b.BRANCH_CODE = a.BRANCH_CODE\n"
  	     		+ "and a.CATEGORY_CODE = c.category_code\n"
  	     		+ "and a.DEPT_CODE = d.Dept_code\n"
  	     		+ "and cost_price = amount\n"
  	     		+ "and iso ='000'\n"
  	     		+ "and transactionId in ('1','8','32','20','4','15')\n"
  	     		+ "and a.Category_ID=? \n"
  	     		+ "and t.transaction_date BETWEEN ? AND ? \n"
  	     		+ "union\n"
  	     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,\n"
  	     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger, \n"
  	     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date as Legacy_Posted_Date,\n"
  	     		+ "amount,transaction_date,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE\n"
  	     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c, \n"
  	     		+ "am_ad_department d, am_gb_company z\n"
  	     		+ "where a.asset_id = t.asset_id\n"
  	     		+ "and b.BRANCH_CODE = a.BRANCH_CODE\n"
  	     		+ "and a.CATEGORY_CODE = c.category_code\n"
  	     		+ "and a.DEPT_CODE = d.Dept_code\n"
  	     		+ "and page1 = 'ASSET CREATION RAISE ENTRY'\n"
  	     		+ "and cost_price = amount\n"
  	     		+ "and iso ='000' and transactionId =24\n"
  	     		+ "and a.Category_ID=? \n"
  	     		+ "and t.transaction_date BETWEEN ? AND ? \n"
  	     		+ "union\n"
  	     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,\n"
  	     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger, \n"
  	     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date as Legacy_Posted_Date,\n"
  	     		+ "amount,transaction_date,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE\n"
  	     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c, \n"
  	     		+ "am_ad_department d, am_gb_company z\n"
  	     		+ "where a.asset_id = t.asset_id\n"
  	     		+ "and b.BRANCH_CODE = a.BRANCH_CODE\n"
  	     		+ "and a.CATEGORY_CODE = c.category_code\n"
  	     		+ "and a.DEPT_CODE = d.Dept_code\n"
  	     		+ "and page1 = 'ASSET CREATION RAISE ENTRY'\n"
  	     		+ "and cost_price = amount\n"
  	     		+ "and iso ='000' and transactionId =30\n"
  	     		+ "and a.Category_ID=? \n"
  	     		+ "and t.transaction_date BETWEEN ? AND ? \n"
  	     		+ "\n"
  	     		+ "union\n"
  	     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,\n"
  	     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger, \n"
  	     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date as Legacy_Posted_Date, \n"
  	     		+ "amount,transaction_date,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE\n"
  	     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_asset_improvement m, am_ad_branch b, am_ad_category c, \n"
  	     		+ "am_ad_department d, am_gb_company z\n"
  	     		+ "where a.asset_id = t.asset_id\n"
  	     		+ "and b.BRANCH_CODE = a.BRANCH_CODE\n"
  	     		+ "and a.CATEGORY_CODE = c.category_code\n"
  	     		+ "and a.DEPT_CODE = d.Dept_code\n"
  	     		+ "and t.asset_id = m.asset_id\n"
  	     		+ "and page1 = 'ASSET IMPROVEMENT RAISE ENTRY'\n"
  	     		+ "and m.cost_price = amount\n"
  	     		+ "and iso ='000'and transactionId =26\n"
  	     		+ "and a.Category_ID=? \n"
  	     		+ "and t.transaction_date BETWEEN ? AND ? \n"
  	     		+ "\n"
  	     		+ "ORDER BY\n"
  	     		+ "     a.BRANCH_CODE ASC,\n"
  	     		+ "     a.CATEGORY_CODE ASC";    
  		}      
  	 
  	if(branch_Id.equals("0")  && !categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")){	   
   	   System.out.println("======>>>>>>>Category Selected: "+branch_Id+"   categoryCode: "+categoryCode);
   	     ColQuery ="select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,\n"
   	     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger, \n"
   	     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date as Legacy_Posted_Date,\n"
   	     		+ "amount,transaction_date,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE\n"
   	     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c, \n"
   	     		+ "am_ad_department d, am_gb_company z\n"
   	     		+ "where a.asset_id = t.asset_id\n"
   	     		+ "and b.BRANCH_CODE = a.BRANCH_CODE\n"
   	     		+ "and a.CATEGORY_CODE = c.category_code\n"
   	     		+ "and a.DEPT_CODE = d.Dept_code\n"
   	     		+ "and cost_price = amount\n"
   	     		+ "and iso ='000'\n"
   	     		+ "and transactionId in ('1','8','32','20','4','15')\n"
   	     		+ "and a.Category_ID=? \n"
   	     		+ "union\n"
   	     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,\n"
   	     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger, \n"
   	     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date as Legacy_Posted_Date,\n"
   	     		+ "amount,transaction_date,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE\n"
   	     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c, \n"
   	     		+ "am_ad_department d, am_gb_company z\n"
   	     		+ "where a.asset_id = t.asset_id\n"
   	     		+ "and b.BRANCH_CODE = a.BRANCH_CODE\n"
   	     		+ "and a.CATEGORY_CODE = c.category_code\n"
   	     		+ "and a.DEPT_CODE = d.Dept_code\n"
   	     		+ "and page1 = 'ASSET CREATION RAISE ENTRY'\n"
   	     		+ "and cost_price = amount\n"
   	     		+ "and iso ='000' and transactionId =24\n"
   	     		+ "and a.Category_ID=? \n"
   	     		+ "union\n"
   	     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,\n"
   	     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger, \n"
   	     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date as Legacy_Posted_Date,\n"
   	     		+ "amount,transaction_date,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE\n"
   	     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c, \n"
   	     		+ "am_ad_department d, am_gb_company z\n"
   	     		+ "where a.asset_id = t.asset_id\n"
   	     		+ "and b.BRANCH_CODE = a.BRANCH_CODE\n"
   	     		+ "and a.CATEGORY_CODE = c.category_code\n"
   	     		+ "and a.DEPT_CODE = d.Dept_code\n"
   	     		+ "and page1 = 'ASSET CREATION RAISE ENTRY'\n"
   	     		+ "and cost_price = amount\n"
   	     		+ "and iso ='000' and transactionId =30\n"
   	     		+ "and a.Category_ID=? \n"
   	     		+ "\n"
   	     		+ "union\n"
   	     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,\n"
   	     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger, \n"
   	     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date as Legacy_Posted_Date, \n"
   	     		+ "amount,transaction_date,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE\n"
   	     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_asset_improvement m, am_ad_branch b, am_ad_category c, \n"
   	     		+ "am_ad_department d, am_gb_company z\n"
   	     		+ "where a.asset_id = t.asset_id\n"
   	     		+ "and b.BRANCH_CODE = a.BRANCH_CODE\n"
   	     		+ "and a.CATEGORY_CODE = c.category_code\n"
   	     		+ "and a.DEPT_CODE = d.Dept_code\n"
   	     		+ "and t.asset_id = m.asset_id\n"
   	     		+ "and page1 = 'ASSET IMPROVEMENT RAISE ENTRY'\n"
   	     		+ "and m.cost_price = amount\n"
   	     		+ "and iso ='000'and transactionId =26\n"
   	     		+ "and a.Category_ID=? \n"
   	     		+ "\n"
   	     		+ "ORDER BY\n"
   	     		+ "     a.BRANCH_CODE ASC,\n"
   	     		+ "     a.CATEGORY_CODE ASC";    
   		}      
  	
     if(!branch_Id.equals("0")  && !categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")){
  	   System.out.println("======>>>>>>>Branch and Category and Date Selected: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate);
  	     ColQuery ="select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,\n"
  	     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger, \n"
  	     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date as Legacy_Posted_Date,\n"
  	     		+ "amount,transaction_date,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE\n"
  	     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c, \n"
  	     		+ "am_ad_department d, am_gb_company z\n"
  	     		+ "where a.asset_id = t.asset_id\n"
  	     		+ "and b.BRANCH_CODE = a.BRANCH_CODE\n"
  	     		+ "and a.CATEGORY_CODE = c.category_code\n"
  	     		+ "and a.DEPT_CODE = d.Dept_code\n"
  	     		+ "and cost_price = amount\n"
  	     		+ "and iso ='000'\n"
  	     		+ "and transactionId in ('1','8','32','20','4','15')\n"
  	     		+ "and a.Category_ID=? \n"
  	     		+ "and a.Branch_ID=? \n"
  	     		+ "and t.transaction_date BETWEEN ? AND ? \n"
  	     		+ "union\n"
  	     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,\n"
  	     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger, \n"
  	     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date as Legacy_Posted_Date,\n"
  	     		+ "amount,transaction_date,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE\n"
  	     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c, \n"
  	     		+ "am_ad_department d, am_gb_company z\n"
  	     		+ "where a.asset_id = t.asset_id\n"
  	     		+ "and b.BRANCH_CODE = a.BRANCH_CODE\n"
  	     		+ "and a.CATEGORY_CODE = c.category_code\n"
  	     		+ "and a.DEPT_CODE = d.Dept_code\n"
  	     		+ "and page1 = 'ASSET CREATION RAISE ENTRY'\n"
  	     		+ "and cost_price = amount\n"
  	     		+ "and iso ='000' and transactionId =24\n"
  	     		+ "and a.Category_ID=? \n"
  	     		+ "and a.Branch_ID=? \n"
  	     		+ "and t.transaction_date BETWEEN ? AND ? \n"
  	     		+ "union\n"
  	     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,\n"
  	     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger, \n"
  	     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date as Legacy_Posted_Date,\n"
  	     		+ "amount,transaction_date,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE\n"
  	     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_ad_branch b, am_ad_category c, \n"
  	     		+ "am_ad_department d, am_gb_company z\n"
  	     		+ "where a.asset_id = t.asset_id\n"
  	     		+ "and b.BRANCH_CODE = a.BRANCH_CODE\n"
  	     		+ "and a.CATEGORY_CODE = c.category_code\n"
  	     		+ "and a.DEPT_CODE = d.Dept_code\n"
  	     		+ "and page1 = 'ASSET CREATION RAISE ENTRY'\n"
  	     		+ "and cost_price = amount\n"
  	     		+ "and iso ='000' and transactionId =30\n"
  	     		+ "and a.Category_ID=? \n"
  	     		+ "and a.Branch_ID=? \n"
  	     		+ "and t.transaction_date BETWEEN ? AND ? \n"
  	     		+ "\n"
  	     		+ "union\n"
  	     		+ "select distinct t.asset_id, a.Description, b.BRANCH_NAME, c.category_name, a.Accum_dep, a.monthly_dep,\n"
  	     		+ "a.Cost_Price, a.NBV, a.Date_purchased, c.Dep_rate, c.Accum_Dep_ledger, c.Dep_ledger, c.Asset_Ledger, \n"
  	     		+ "c.gl_account, a.dep_end_date, a.effective_date, a.Posting_Date, d.Dept_name, a.Finacle_Posted_Date as Legacy_Posted_Date, \n"
  	     		+ "amount,transaction_date,page1, z.processing_date, z.company_name, a.BRANCH_CODE, a.CATEGORY_CODE\n"
  	     		+ "from am_asset a, dbo.am_Raisentry_Transaction t, am_asset_improvement m, am_ad_branch b, am_ad_category c, \n"
  	     		+ "am_ad_department d, am_gb_company z\n"
  	     		+ "where a.asset_id = t.asset_id\n"
  	     		+ "and b.BRANCH_CODE = a.BRANCH_CODE\n"
  	     		+ "and a.CATEGORY_CODE = c.category_code\n"
  	     		+ "and a.DEPT_CODE = d.Dept_code\n"
  	     		+ "and t.asset_id = m.asset_id\n"
  	     		+ "and page1 = 'ASSET IMPROVEMENT RAISE ENTRY'\n"
  	     		+ "and m.cost_price = amount\n"
  	     		+ "and iso ='000'and transactionId =26\n"
  	     		+ "and a.Category_ID=? \n"
  	     		+ "and a.Branch_ID=? \n"
  	     		+ "and t.transaction_date BETWEEN ? AND ? \n"
  	     		+ "\n"
  	     		+ "ORDER BY\n"
  	     		+ "     a.BRANCH_CODE ASC,\n"
  	     		+ "     a.CATEGORY_CODE ASC";    
  		}      
//   System.out.println("======>>>>>>>ColQuery: "+ColQuery);
     java.util.ArrayList list =rep.getAssetTransactionListRecords(ColQuery,branch_Id,categoryCode,FromDate,ToDate,asset_Id);
     System.out.println("======>>>>>>>list size: "+list.size()+"        =====report: "+report);
     if(list.size()!=0){
   	 if(report.equalsIgnoreCase("rptMenuBATRA")){
    	 SXSSFWorkbook workbook = new SXSSFWorkbook();
         Sheet sheet = workbook.createSheet("Demo");
         Row rowhead = sheet.createRow((int) 0);
         
         rowhead.createCell( 0).setCellValue("S/No.");
         rowhead.createCell( 1).setCellValue("Asset ID");
         rowhead.createCell( 2).setCellValue("Asset Description");
         rowhead.createCell( 3).setCellValue("Branch Name");
         rowhead.createCell( 4).setCellValue("Category Name");
         rowhead.createCell( 5).setCellValue("Accum. Deprec.");
         rowhead.createCell( 6).setCellValue("Monthly Depreciation");
         rowhead.createCell( 7).setCellValue("Cost Price");
         rowhead.createCell( 8).setCellValue("NBV");
         rowhead.createCell( 9).setCellValue("Date Purchased");
		 rowhead.createCell( 10).setCellValue("Dep Rate");
		 rowhead.createCell( 11).setCellValue("Accum Dep Ledger");
		 rowhead.createCell( 12).setCellValue("Dep Ledger");
		 rowhead.createCell( 13).setCellValue("Asset Ledger");
		 rowhead.createCell( 14).setCellValue("GL Account");
		 rowhead.createCell( 15).setCellValue("Depr. End Date");
		 rowhead.createCell( 16).setCellValue("Effective Date");
		 rowhead.createCell( 17).setCellValue("Posting Date");
		 rowhead.createCell( 18).setCellValue("Dept Name");
         rowhead.createCell( 19).setCellValue("Legacy Posted Date");
         rowhead.createCell( 20).setCellValue("Amount");
         rowhead.createCell( 21).setCellValue("Transaction Date");
         rowhead.createCell( 22).setCellValue("Transaction Type");
         rowhead.createCell( 23).setCellValue("Processing Date");
         rowhead.createCell( 24).setCellValue("Branch code");
         rowhead.createCell( 25).setCellValue("Category Code");
         
         

     int i = 1;
     System.out.println("<<<<<<list.size(): "+list.size());
	 for(int k=0;k<list.size();k++)
     {
    	 com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);    	 
			String assetId =  newassettrans.getAssetId();
			String Description = newassettrans.getDescription(); 
			String branchName =  newassettrans.getBranchName();
			String categoryName =  newassettrans.getCategoryName();
			double accumDepr = newassettrans.getAccumDep();
			double monthlyDepr = newassettrans.getMonthlyDep();
			double costprice = newassettrans.getCostPrice();
			double nbv = newassettrans.getNbv();
			String datePurchased = newassettrans.getDatepurchased() != null ? getDate(newassettrans.getDatepurchased()) : "";
			double depRate = newassettrans.getDepRate();
			String accumDepLedger = newassettrans.getAccumDepLedger();
			String depLedger = newassettrans.getDepLedger();
			String assetLedger = newassettrans.getAssetLedger();
			String gl_Account = newassettrans.getGlAccount();
			String depEndDate = newassettrans.getDependDate() != null ? getDate(newassettrans.getDependDate()) : "";
			String effectiveDate = newassettrans.getEffectiveDate() != null ? getDate(newassettrans.getEffectiveDate()) : "";
			String postingDate = newassettrans.getPostingDate()!= null ? getDate(newassettrans.getPostingDate()) : "";
			String deptName = newassettrans.getDeptName();
			String legacyPostedDate = newassettrans.getLegacyPostedDate() != null ? getDate(newassettrans.getLegacyPostedDate()) : "";
			double amount = newassettrans.getAmount();
			String transactionDate = newassettrans.getTransDate();
			String tranType = newassettrans.getTranType();
			String processingDate = newassettrans.getProcessingDate() != null ? getDate(newassettrans.getProcessingDate()) : "";
			branchCode = newassettrans.getBranchCode();
			categoryCode = newassettrans.getCategoryCode();

			//			String vendorName = records.getCodeName("select VENDOR_NAME from am_ad_vendor where VENDOR_ID = "+vendorId+"");
			

			Row row = sheet.createRow((int) i);

			row.createCell((short) 0).setCellValue(i);
			row.createCell((short) 1).setCellValue(assetId);
            row.createCell((short) 2).setCellValue(Description);
            row.createCell((short) 3).setCellValue(branchName);
            row.createCell((short) 4).setCellValue(categoryName);
            row.createCell((short) 5).setCellValue(accumDepr);
            row.createCell((short) 6).setCellValue(monthlyDepr);
            row.createCell((short) 7).setCellValue(costprice);
            row.createCell((short) 8).setCellValue(nbv);
            row.createCell((short) 9).setCellValue(datePurchased);
			row.createCell((short) 10).setCellValue(depRate);
			row.createCell((short) 11).setCellValue(accumDepLedger);
			row.createCell((short) 12).setCellValue(depLedger);
			row.createCell((short) 13).setCellValue(assetLedger);
			row.createCell((short) 14).setCellValue(gl_Account);
			row.createCell((short) 15).setCellValue(depEndDate);
			row.createCell((short) 16).setCellValue(effectiveDate);
            row.createCell((short) 17).setCellValue(postingDate);
            row.createCell((short) 18).setCellValue(deptName);
            row.createCell((short) 19).setCellValue(legacyPostedDate);
            row.createCell((short) 20).setCellValue(amount);
            row.createCell((short) 21).setCellValue(transactionDate);
            row.createCell((short) 22).setCellValue(tranType);
            row.createCell((short) 23).setCellValue(processingDate);
            row.createCell((short) 24).setCellValue(branchCode);
            row.createCell((short) 25).setCellValue(categoryCode);
		

            i++;
//            System.out.println("we are here");
     }
	 System.out.println("we are here 2");
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