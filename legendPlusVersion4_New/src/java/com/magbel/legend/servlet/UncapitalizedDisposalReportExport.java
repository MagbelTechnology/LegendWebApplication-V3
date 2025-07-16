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
   
public class UncapitalizedDisposalReportExport extends HttpServlet
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
    if(report.equalsIgnoreCase("rptMenuBCUNCAPDIS")){fileName = branchCode+"By"+userName+"UncapitalizedDisposalReport.xlsx";}
    
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
  	     ColQuery ="SELECT\n"
  	     		+ "     AM_ASSET_UNCAPITALIZED.\"Asset_id\" AS am_Asset_Asset_id,\n"
  	     		+ "     AM_ASSET_UNCAPITALIZED.\"Description\" AS am_Asset_Description,\n"
  	     		+ "     am_ad_branch.\"BRANCH_NAME\" AS am_ad_branch_BRANCH_NAME,\n"
  	     		+ "     am_ad_category.\"category_name\" AS am_ad_category_category_name,\n"
  	     		+ "     AM_ASSET_UNCAPITALIZED.\"Accum_dep\" AS am_Asset_Accum_dep,\n"
  	     		+ "     AM_ASSET_UNCAPITALIZED.\"monthly_dep\" AS am_Asset_monthly_dep,\n"
  	     		+ "     AM_ASSET_UNCAPITALIZED.\"Cost_Price\" AS am_Asset_Cost_Price,\n"
  	     		+ "     AM_ASSET_UNCAPITALIZED.\"NBV\" AS am_Asset_NBV,\n"
  	     		+ "     AM_ASSET_UNCAPITALIZED.\"Date_purchased\" AS am_Asset_Date_purchased,\n"
  	     		+ "     am_ad_category.\"Dep_rate\" AS am_ad_category_Dep_rate,\n"
  	     		+ "     am_ad_category.\"Accum_Dep_ledger\" AS am_ad_category_Accum_Dep_ledger,\n"
  	     		+ "     am_ad_category.\"Dep_ledger\" AS am_ad_category_Dep_ledger,\n"
  	     		+ "     am_ad_category.\"Asset_Ledger\" AS am_ad_category_Asset_Ledger,\n"
  	     		+ "     am_ad_category.\"gl_account\" AS am_ad_category_gl_account,\n"
  	     		+ "     AM_ASSET_UNCAPITALIZED.\"BRANCH_CODE\" AS am_Asset_BRANCH_CODE,\n"
  	     		+ "     AM_ASSET_UNCAPITALIZED.\"CATEGORY_CODE\" AS am_Asset_CATEGORY_CODE,\n"
  	     		+ "     am_UncapitalizedDisposal.\"Disposal_reason\" AS am_AssetDisposal_Disposal_reason,\n"
  	     		+ "     am_UncapitalizedDisposal.\"Buyer_Account\" AS am_AssetDisposal_Buyer_Account,\n"
  	     		+ "     am_UncapitalizedDisposal.\"Disposal_Amount\" AS am_AssetDisposal_Disposal_Amount,\n"
  	     		+ "     am_UncapitalizedDisposal.\"Disposal_Date\" AS am_AssetDisposal_Disposal_Date,\n"
  	     		+ "     am_UncapitalizedDisposal.\"Profit_Loss\" AS am_AssetDisposal_Profit_Loss,\n"
  	     		+ "     am_ad_disposalReasons.\"description\" AS am_ad_disposalReasons_description,\n"
  	     		+ "     AM_ASSET_UNCAPITALIZED.\"Asset_User\" AS am_Asset_Asset_User,\n"
  	     		+ "     AM_ASSET_UNCAPITALIZED.\"BAR_CODE\" AS am_Asset_BAR_CODE,\n"
  	     		+ "     AM_ASSET_UNCAPITALIZED.\"Asset_Status\" AS am_Asset_Asset_Status,\n"
  	     		+ "     am_gb_company.\"company_name\" AS am_gb_company_company_name\n"
  	     		+ "FROM\n"
  	     		+ "     \"dbo\".\"AM_ASSET_UNCAPITALIZED\" AM_ASSET_UNCAPITALIZED INNER JOIN \"dbo\".\"am_ad_branch\" am_ad_branch ON AM_ASSET_UNCAPITALIZED.\"BRANCH_CODE\" = am_ad_branch.\"BRANCH_CODE\"\n"
  	     		+ "     INNER JOIN \"dbo\".\"am_ad_category\" am_ad_category ON AM_ASSET_UNCAPITALIZED.\"CATEGORY_CODE\" = am_ad_category.\"category_code\"\n"
  	     		+ "     INNER JOIN \"dbo\".\"am_UncapitalizedDisposal\" am_UncapitalizedDisposal ON AM_ASSET_UNCAPITALIZED.\"Asset_id\" = am_UncapitalizedDisposal.\"Asset_id\"\n"
  	     		+ "     INNER JOIN \"dbo\".\"am_ad_disposalReasons\" am_ad_disposalReasons ON am_UncapitalizedDisposal.\"Disposal_reason\" = am_ad_disposalReasons.\"reason_id\",\n"
  	     		+ "     \"dbo\".\"am_gb_company\" am_gb_company\n"
  	     		+ "WHERE\n"
  	     		+ "     am_UncapitalizedDisposal.\"Asset_id\" = AM_ASSET_UNCAPITALIZED.\"Asset_id\" and AM_ASSET_UNCAPITALIZED.\"Asset_Status\" = 'DISPOSED' \n"
  	     		+ "	\n"
  	     		+ "ORDER BY+\n"
  	     		+ "     am_ad_branch.\"BRANCH_NAME\" ASC,\n"
  	     		+ "     am_ad_category.\"category_code\" ASC";    
  		}
     
     
     if(branch_Id.equals("0")  && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")){
    	 System.out.println("======>>>>>>>Date Selected: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate);	     
	     ColQuery ="SELECT\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"Asset_id\" AS am_Asset_Asset_id,\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"Description\" AS am_Asset_Description,\n"
	     		+ "     am_ad_branch.\"BRANCH_NAME\" AS am_ad_branch_BRANCH_NAME,\n"
	     		+ "     am_ad_category.\"category_name\" AS am_ad_category_category_name,\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"Accum_dep\" AS am_Asset_Accum_dep,\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"monthly_dep\" AS am_Asset_monthly_dep,\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"Cost_Price\" AS am_Asset_Cost_Price,\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"NBV\" AS am_Asset_NBV,\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"Date_purchased\" AS am_Asset_Date_purchased,\n"
	     		+ "     am_ad_category.\"Dep_rate\" AS am_ad_category_Dep_rate,\n"
	     		+ "     am_ad_category.\"Accum_Dep_ledger\" AS am_ad_category_Accum_Dep_ledger,\n"
	     		+ "     am_ad_category.\"Dep_ledger\" AS am_ad_category_Dep_ledger,\n"
	     		+ "     am_ad_category.\"Asset_Ledger\" AS am_ad_category_Asset_Ledger,\n"
	     		+ "     am_ad_category.\"gl_account\" AS am_ad_category_gl_account,\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"BRANCH_CODE\" AS am_Asset_BRANCH_CODE,\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"CATEGORY_CODE\" AS am_Asset_CATEGORY_CODE,\n"
	     		+ "     am_UncapitalizedDisposal.\"Disposal_reason\" AS am_AssetDisposal_Disposal_reason,\n"
	     		+ "     am_UncapitalizedDisposal.\"Buyer_Account\" AS am_AssetDisposal_Buyer_Account,\n"
	     		+ "     am_UncapitalizedDisposal.\"Disposal_Amount\" AS am_AssetDisposal_Disposal_Amount,\n"
	     		+ "     am_UncapitalizedDisposal.\"Disposal_Date\" AS am_AssetDisposal_Disposal_Date,\n"
	     		+ "     am_UncapitalizedDisposal.\"Profit_Loss\" AS am_AssetDisposal_Profit_Loss,\n"
	     		+ "     am_ad_disposalReasons.\"description\" AS am_ad_disposalReasons_description,\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"Asset_User\" AS am_Asset_Asset_User,\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"BAR_CODE\" AS am_Asset_BAR_CODE,\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"Asset_Status\" AS am_Asset_Asset_Status,\n"
	     		+ "     am_gb_company.\"company_name\" AS am_gb_company_company_name\n"
	     		+ "FROM\n"
	     		+ "     \"dbo\".\"AM_ASSET_UNCAPITALIZED\" AM_ASSET_UNCAPITALIZED INNER JOIN \"dbo\".\"am_ad_branch\" am_ad_branch ON AM_ASSET_UNCAPITALIZED.\"BRANCH_CODE\" = am_ad_branch.\"BRANCH_CODE\"\n"
	     		+ "     INNER JOIN \"dbo\".\"am_ad_category\" am_ad_category ON AM_ASSET_UNCAPITALIZED.\"CATEGORY_CODE\" = am_ad_category.\"category_code\"\n"
	     		+ "     INNER JOIN \"dbo\".\"am_UncapitalizedDisposal\" am_UncapitalizedDisposal ON AM_ASSET_UNCAPITALIZED.\"Asset_id\" = am_UncapitalizedDisposal.\"Asset_id\"\n"
	     		+ "     INNER JOIN \"dbo\".\"am_ad_disposalReasons\" am_ad_disposalReasons ON am_UncapitalizedDisposal.\"Disposal_reason\" = am_ad_disposalReasons.\"reason_id\",\n"
	     		+ "     \"dbo\".\"am_gb_company\" am_gb_company\n"
	     		+ "WHERE\n"
	     		+ "     am_UncapitalizedDisposal.\"Asset_id\" = AM_ASSET_UNCAPITALIZED.\"Asset_id\" and AM_ASSET_UNCAPITALIZED.\"Asset_Status\" = 'DISPOSED' \n"
	     		+ "	AND am_UncapitalizedDisposal.Disposal_Date BETWEEN ? and ?\n"
	     		+ "ORDER BY+\n"
	     		+ "     am_ad_branch.\"BRANCH_NAME\" ASC,\n"
	     		+ "     am_ad_category.\"category_code\" ASC";  
	}      
	 if(!branch_Id.equals("0")  && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")){	   
	   System.out.println("======>>>>>>>Branch and Date Selected: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate);
	     ColQuery ="SELECT\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"Asset_id\" AS am_Asset_Asset_id,\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"Description\" AS am_Asset_Description,\n"
	     		+ "     am_ad_branch.\"BRANCH_NAME\" AS am_ad_branch_BRANCH_NAME,\n"
	     		+ "     am_ad_category.\"category_name\" AS am_ad_category_category_name,\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"Accum_dep\" AS am_Asset_Accum_dep,\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"monthly_dep\" AS am_Asset_monthly_dep,\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"Cost_Price\" AS am_Asset_Cost_Price,\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"NBV\" AS am_Asset_NBV,\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"Date_purchased\" AS am_Asset_Date_purchased,\n"
	     		+ "     am_ad_category.\"Dep_rate\" AS am_ad_category_Dep_rate,\n"
	     		+ "     am_ad_category.\"Accum_Dep_ledger\" AS am_ad_category_Accum_Dep_ledger,\n"
	     		+ "     am_ad_category.\"Dep_ledger\" AS am_ad_category_Dep_ledger,\n"
	     		+ "     am_ad_category.\"Asset_Ledger\" AS am_ad_category_Asset_Ledger,\n"
	     		+ "     am_ad_category.\"gl_account\" AS am_ad_category_gl_account,\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"BRANCH_CODE\" AS am_Asset_BRANCH_CODE,\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"CATEGORY_CODE\" AS am_Asset_CATEGORY_CODE,\n"
	     		+ "     am_UncapitalizedDisposal.\"Disposal_reason\" AS am_AssetDisposal_Disposal_reason,\n"
	     		+ "     am_UncapitalizedDisposal.\"Buyer_Account\" AS am_AssetDisposal_Buyer_Account,\n"
	     		+ "     am_UncapitalizedDisposal.\"Disposal_Amount\" AS am_AssetDisposal_Disposal_Amount,\n"
	     		+ "     am_UncapitalizedDisposal.\"Disposal_Date\" AS am_AssetDisposal_Disposal_Date,\n"
	     		+ "     am_UncapitalizedDisposal.\"Profit_Loss\" AS am_AssetDisposal_Profit_Loss,\n"
	     		+ "     am_ad_disposalReasons.\"description\" AS am_ad_disposalReasons_description,\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"Asset_User\" AS am_Asset_Asset_User,\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"BAR_CODE\" AS am_Asset_BAR_CODE,\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"Asset_Status\" AS am_Asset_Asset_Status,\n"
	     		+ "     am_gb_company.\"company_name\" AS am_gb_company_company_name\n"
	     		+ "FROM\n"
	     		+ "     \"dbo\".\"AM_ASSET_UNCAPITALIZED\" AM_ASSET_UNCAPITALIZED INNER JOIN \"dbo\".\"am_ad_branch\" am_ad_branch ON AM_ASSET_UNCAPITALIZED.\"BRANCH_CODE\" = am_ad_branch.\"BRANCH_CODE\"\n"
	     		+ "     INNER JOIN \"dbo\".\"am_ad_category\" am_ad_category ON AM_ASSET_UNCAPITALIZED.\"CATEGORY_CODE\" = am_ad_category.\"category_code\"\n"
	     		+ "     INNER JOIN \"dbo\".\"am_UncapitalizedDisposal\" am_UncapitalizedDisposal ON AM_ASSET_UNCAPITALIZED.\"Asset_id\" = am_UncapitalizedDisposal.\"Asset_id\"\n"
	     		+ "     INNER JOIN \"dbo\".\"am_ad_disposalReasons\" am_ad_disposalReasons ON am_UncapitalizedDisposal.\"Disposal_reason\" = am_ad_disposalReasons.\"reason_id\",\n"
	     		+ "     \"dbo\".\"am_gb_company\" am_gb_company\n"
	     		+ "WHERE\n"
	     		+ "     am_UncapitalizedDisposal.\"Asset_id\" = AM_ASSET_UNCAPITALIZED.\"Asset_id\" and AM_ASSET_UNCAPITALIZED.\"Asset_Status\" = 'DISPOSED' \n"
	     		+ "    AND am_ad_branch.branch_id = ?\n"
	     		+ "	AND am_UncapitalizedDisposal.Disposal_Date BETWEEN ? and ?\n"
	     		+ "ORDER BY+\n"
	     		+ "     am_ad_branch.\"BRANCH_NAME\" ASC,\n"
	     		+ "     am_ad_category.\"category_code\" ASC";   
		}  
	 
	 if(!branch_Id.equals("0")  && categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")){	   
		   System.out.println("======>>>>>>>Branch Selected: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate);
		     ColQuery ="SELECT\n"
		     		+ "     AM_ASSET_UNCAPITALIZED.\"Asset_id\" AS am_Asset_Asset_id,\n"
		     		+ "     AM_ASSET_UNCAPITALIZED.\"Description\" AS am_Asset_Description,\n"
		     		+ "     am_ad_branch.\"BRANCH_NAME\" AS am_ad_branch_BRANCH_NAME,\n"
		     		+ "     am_ad_category.\"category_name\" AS am_ad_category_category_name,\n"
		     		+ "     AM_ASSET_UNCAPITALIZED.\"Accum_dep\" AS am_Asset_Accum_dep,\n"
		     		+ "     AM_ASSET_UNCAPITALIZED.\"monthly_dep\" AS am_Asset_monthly_dep,\n"
		     		+ "     AM_ASSET_UNCAPITALIZED.\"Cost_Price\" AS am_Asset_Cost_Price,\n"
		     		+ "     AM_ASSET_UNCAPITALIZED.\"NBV\" AS am_Asset_NBV,\n"
		     		+ "     AM_ASSET_UNCAPITALIZED.\"Date_purchased\" AS am_Asset_Date_purchased,\n"
		     		+ "     am_ad_category.\"Dep_rate\" AS am_ad_category_Dep_rate,\n"
		     		+ "     am_ad_category.\"Accum_Dep_ledger\" AS am_ad_category_Accum_Dep_ledger,\n"
		     		+ "     am_ad_category.\"Dep_ledger\" AS am_ad_category_Dep_ledger,\n"
		     		+ "     am_ad_category.\"Asset_Ledger\" AS am_ad_category_Asset_Ledger,\n"
		     		+ "     am_ad_category.\"gl_account\" AS am_ad_category_gl_account,\n"
		     		+ "     AM_ASSET_UNCAPITALIZED.\"BRANCH_CODE\" AS am_Asset_BRANCH_CODE,\n"
		     		+ "     AM_ASSET_UNCAPITALIZED.\"CATEGORY_CODE\" AS am_Asset_CATEGORY_CODE,\n"
		     		+ "     am_UncapitalizedDisposal.\"Disposal_reason\" AS am_AssetDisposal_Disposal_reason,\n"
		     		+ "     am_UncapitalizedDisposal.\"Buyer_Account\" AS am_AssetDisposal_Buyer_Account,\n"
		     		+ "     am_UncapitalizedDisposal.\"Disposal_Amount\" AS am_AssetDisposal_Disposal_Amount,\n"
		     		+ "     am_UncapitalizedDisposal.\"Disposal_Date\" AS am_AssetDisposal_Disposal_Date,\n"
		     		+ "     am_UncapitalizedDisposal.\"Profit_Loss\" AS am_AssetDisposal_Profit_Loss,\n"
		     		+ "     am_ad_disposalReasons.\"description\" AS am_ad_disposalReasons_description,\n"
		     		+ "     AM_ASSET_UNCAPITALIZED.\"Asset_User\" AS am_Asset_Asset_User,\n"
		     		+ "     AM_ASSET_UNCAPITALIZED.\"BAR_CODE\" AS am_Asset_BAR_CODE,\n"
		     		+ "     AM_ASSET_UNCAPITALIZED.\"Asset_Status\" AS am_Asset_Asset_Status,\n"
		     		+ "     am_gb_company.\"company_name\" AS am_gb_company_company_name\n"
		     		+ "FROM\n"
		     		+ "     \"dbo\".\"AM_ASSET_UNCAPITALIZED\" AM_ASSET_UNCAPITALIZED INNER JOIN \"dbo\".\"am_ad_branch\" am_ad_branch ON AM_ASSET_UNCAPITALIZED.\"BRANCH_CODE\" = am_ad_branch.\"BRANCH_CODE\"\n"
		     		+ "     INNER JOIN \"dbo\".\"am_ad_category\" am_ad_category ON AM_ASSET_UNCAPITALIZED.\"CATEGORY_CODE\" = am_ad_category.\"category_code\"\n"
		     		+ "     INNER JOIN \"dbo\".\"am_UncapitalizedDisposal\" am_UncapitalizedDisposal ON AM_ASSET_UNCAPITALIZED.\"Asset_id\" = am_UncapitalizedDisposal.\"Asset_id\"\n"
		     		+ "     INNER JOIN \"dbo\".\"am_ad_disposalReasons\" am_ad_disposalReasons ON am_UncapitalizedDisposal.\"Disposal_reason\" = am_ad_disposalReasons.\"reason_id\",\n"
		     		+ "     \"dbo\".\"am_gb_company\" am_gb_company\n"
		     		+ "WHERE\n"
		     		+ "     am_UncapitalizedDisposal.\"Asset_id\" = AM_ASSET_UNCAPITALIZED.\"Asset_id\" and AM_ASSET_UNCAPITALIZED.\"Asset_Status\" = 'DISPOSED' \n"
		     		+ "    AND am_ad_branch.branch_id = ?\n"
		     		+ "ORDER BY+\n"
		     		+ "     am_ad_branch.\"BRANCH_NAME\" ASC,\n"
		     		+ "     am_ad_category.\"category_code\" ASC";   
			}      

	 if(branch_Id.equals("0")  && !categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")){	   
	   System.out.println("======>>>>>>>Category and Date Selected: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate);
	     ColQuery ="SELECT\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"Asset_id\" AS am_Asset_Asset_id,\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"Description\" AS am_Asset_Description,\n"
	     		+ "     am_ad_branch.\"BRANCH_NAME\" AS am_ad_branch_BRANCH_NAME,\n"
	     		+ "     am_ad_category.\"category_name\" AS am_ad_category_category_name,\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"Accum_dep\" AS am_Asset_Accum_dep,\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"monthly_dep\" AS am_Asset_monthly_dep,\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"Cost_Price\" AS am_Asset_Cost_Price,\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"NBV\" AS am_Asset_NBV,\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"Date_purchased\" AS am_Asset_Date_purchased,\n"
	     		+ "     am_ad_category.\"Dep_rate\" AS am_ad_category_Dep_rate,\n"
	     		+ "     am_ad_category.\"Accum_Dep_ledger\" AS am_ad_category_Accum_Dep_ledger,\n"
	     		+ "     am_ad_category.\"Dep_ledger\" AS am_ad_category_Dep_ledger,\n"
	     		+ "     am_ad_category.\"Asset_Ledger\" AS am_ad_category_Asset_Ledger,\n"
	     		+ "     am_ad_category.\"gl_account\" AS am_ad_category_gl_account,\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"BRANCH_CODE\" AS am_Asset_BRANCH_CODE,\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"CATEGORY_CODE\" AS am_Asset_CATEGORY_CODE,\n"
	     		+ "     am_UncapitalizedDisposal.\"Disposal_reason\" AS am_AssetDisposal_Disposal_reason,\n"
	     		+ "     am_UncapitalizedDisposal.\"Buyer_Account\" AS am_AssetDisposal_Buyer_Account,\n"
	     		+ "     am_UncapitalizedDisposal.\"Disposal_Amount\" AS am_AssetDisposal_Disposal_Amount,\n"
	     		+ "     am_UncapitalizedDisposal.\"Disposal_Date\" AS am_AssetDisposal_Disposal_Date,\n"
	     		+ "     am_UncapitalizedDisposal.\"Profit_Loss\" AS am_AssetDisposal_Profit_Loss,\n"
	     		+ "     am_ad_disposalReasons.\"description\" AS am_ad_disposalReasons_description,\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"Asset_User\" AS am_Asset_Asset_User,\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"BAR_CODE\" AS am_Asset_BAR_CODE,\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"Asset_Status\" AS am_Asset_Asset_Status,\n"
	     		+ "     am_gb_company.\"company_name\" AS am_gb_company_company_name\n"
	     		+ "FROM\n"
	     		+ "     \"dbo\".\"AM_ASSET_UNCAPITALIZED\" AM_ASSET_UNCAPITALIZED INNER JOIN \"dbo\".\"am_ad_branch\" am_ad_branch ON AM_ASSET_UNCAPITALIZED.\"BRANCH_CODE\" = am_ad_branch.\"BRANCH_CODE\"\n"
	     		+ "     INNER JOIN \"dbo\".\"am_ad_category\" am_ad_category ON AM_ASSET_UNCAPITALIZED.\"CATEGORY_CODE\" = am_ad_category.\"category_code\"\n"
	     		+ "     INNER JOIN \"dbo\".\"am_UncapitalizedDisposal\" am_UncapitalizedDisposal ON AM_ASSET_UNCAPITALIZED.\"Asset_id\" = am_UncapitalizedDisposal.\"Asset_id\"\n"
	     		+ "     INNER JOIN \"dbo\".\"am_ad_disposalReasons\" am_ad_disposalReasons ON am_UncapitalizedDisposal.\"Disposal_reason\" = am_ad_disposalReasons.\"reason_id\",\n"
	     		+ "     \"dbo\".\"am_gb_company\" am_gb_company\n"
	     		+ "WHERE\n"
	     		+ "     am_UncapitalizedDisposal.\"Asset_id\" = AM_ASSET_UNCAPITALIZED.\"Asset_id\" and AM_ASSET_UNCAPITALIZED.\"Asset_Status\" = 'DISPOSED' \n"
	     		+ "    AND am_ad_category.category_id = ?\n"
	     		+ "	AND am_UncapitalizedDisposal.Disposal_Date BETWEEN ? and ?\n"
	     		+ "ORDER BY+\n"
	     		+ "     am_ad_branch.\"BRANCH_NAME\" ASC,\n"
	     		+ "     am_ad_category.\"category_code\" ASC";    
		}      
	 
	 if(branch_Id.equals("0")  && !categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")){	   
		   System.out.println("======>>>>>>>Category Selected: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate);
		     ColQuery ="SELECT\n"
		     		+ "     AM_ASSET_UNCAPITALIZED.\"Asset_id\" AS am_Asset_Asset_id,\n"
		     		+ "     AM_ASSET_UNCAPITALIZED.\"Description\" AS am_Asset_Description,\n"
		     		+ "     am_ad_branch.\"BRANCH_NAME\" AS am_ad_branch_BRANCH_NAME,\n"
		     		+ "     am_ad_category.\"category_name\" AS am_ad_category_category_name,\n"
		     		+ "     AM_ASSET_UNCAPITALIZED.\"Accum_dep\" AS am_Asset_Accum_dep,\n"
		     		+ "     AM_ASSET_UNCAPITALIZED.\"monthly_dep\" AS am_Asset_monthly_dep,\n"
		     		+ "     AM_ASSET_UNCAPITALIZED.\"Cost_Price\" AS am_Asset_Cost_Price,\n"
		     		+ "     AM_ASSET_UNCAPITALIZED.\"NBV\" AS am_Asset_NBV,\n"
		     		+ "     AM_ASSET_UNCAPITALIZED.\"Date_purchased\" AS am_Asset_Date_purchased,\n"
		     		+ "     am_ad_category.\"Dep_rate\" AS am_ad_category_Dep_rate,\n"
		     		+ "     am_ad_category.\"Accum_Dep_ledger\" AS am_ad_category_Accum_Dep_ledger,\n"
		     		+ "     am_ad_category.\"Dep_ledger\" AS am_ad_category_Dep_ledger,\n"
		     		+ "     am_ad_category.\"Asset_Ledger\" AS am_ad_category_Asset_Ledger,\n"
		     		+ "     am_ad_category.\"gl_account\" AS am_ad_category_gl_account,\n"
		     		+ "     AM_ASSET_UNCAPITALIZED.\"BRANCH_CODE\" AS am_Asset_BRANCH_CODE,\n"
		     		+ "     AM_ASSET_UNCAPITALIZED.\"CATEGORY_CODE\" AS am_Asset_CATEGORY_CODE,\n"
		     		+ "     am_UncapitalizedDisposal.\"Disposal_reason\" AS am_AssetDisposal_Disposal_reason,\n"
		     		+ "     am_UncapitalizedDisposal.\"Buyer_Account\" AS am_AssetDisposal_Buyer_Account,\n"
		     		+ "     am_UncapitalizedDisposal.\"Disposal_Amount\" AS am_AssetDisposal_Disposal_Amount,\n"
		     		+ "     am_UncapitalizedDisposal.\"Disposal_Date\" AS am_AssetDisposal_Disposal_Date,\n"
		     		+ "     am_UncapitalizedDisposal.\"Profit_Loss\" AS am_AssetDisposal_Profit_Loss,\n"
		     		+ "     am_ad_disposalReasons.\"description\" AS am_ad_disposalReasons_description,\n"
		     		+ "     AM_ASSET_UNCAPITALIZED.\"Asset_User\" AS am_Asset_Asset_User,\n"
		     		+ "     AM_ASSET_UNCAPITALIZED.\"BAR_CODE\" AS am_Asset_BAR_CODE,\n"
		     		+ "     AM_ASSET_UNCAPITALIZED.\"Asset_Status\" AS am_Asset_Asset_Status,\n"
		     		+ "     am_gb_company.\"company_name\" AS am_gb_company_company_name\n"
		     		+ "FROM\n"
		     		+ "     \"dbo\".\"AM_ASSET_UNCAPITALIZED\" AM_ASSET_UNCAPITALIZED INNER JOIN \"dbo\".\"am_ad_branch\" am_ad_branch ON AM_ASSET_UNCAPITALIZED.\"BRANCH_CODE\" = am_ad_branch.\"BRANCH_CODE\"\n"
		     		+ "     INNER JOIN \"dbo\".\"am_ad_category\" am_ad_category ON AM_ASSET_UNCAPITALIZED.\"CATEGORY_CODE\" = am_ad_category.\"category_code\"\n"
		     		+ "     INNER JOIN \"dbo\".\"am_UncapitalizedDisposal\" am_UncapitalizedDisposal ON AM_ASSET_UNCAPITALIZED.\"Asset_id\" = am_UncapitalizedDisposal.\"Asset_id\"\n"
		     		+ "     INNER JOIN \"dbo\".\"am_ad_disposalReasons\" am_ad_disposalReasons ON am_UncapitalizedDisposal.\"Disposal_reason\" = am_ad_disposalReasons.\"reason_id\",\n"
		     		+ "     \"dbo\".\"am_gb_company\" am_gb_company\n"
		     		+ "WHERE\n"
		     		+ "     am_UncapitalizedDisposal.\"Asset_id\" = AM_ASSET_UNCAPITALIZED.\"Asset_id\" and AM_ASSET_UNCAPITALIZED.\"Asset_Status\" = 'DISPOSED' \n"
		     		+ "    AND am_ad_category.category_id = ?\n"
		     		+ "ORDER BY+\n"
		     		+ "     am_ad_branch.\"BRANCH_NAME\" ASC,\n"
		     		+ "     am_ad_category.\"category_code\" ASC";    
			} 
	 
	 if(!branch_Id.equals("0")  && !categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")){
		   System.out.println("======>>>>>>>Branch and Category Selected: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate);
		     ColQuery ="SELECT\n"
		     		+ "     AM_ASSET_UNCAPITALIZED.\"Asset_id\" AS am_Asset_Asset_id,\n"
		     		+ "     AM_ASSET_UNCAPITALIZED.\"Description\" AS am_Asset_Description,\n"
		     		+ "     am_ad_branch.\"BRANCH_NAME\" AS am_ad_branch_BRANCH_NAME,\n"
		     		+ "     am_ad_category.\"category_name\" AS am_ad_category_category_name,\n"
		     		+ "     AM_ASSET_UNCAPITALIZED.\"Accum_dep\" AS am_Asset_Accum_dep,\n"
		     		+ "     AM_ASSET_UNCAPITALIZED.\"monthly_dep\" AS am_Asset_monthly_dep,\n"
		     		+ "     AM_ASSET_UNCAPITALIZED.\"Cost_Price\" AS am_Asset_Cost_Price,\n"
		     		+ "     AM_ASSET_UNCAPITALIZED.\"NBV\" AS am_Asset_NBV,\n"
		     		+ "     AM_ASSET_UNCAPITALIZED.\"Date_purchased\" AS am_Asset_Date_purchased,\n"
		     		+ "     am_ad_category.\"Dep_rate\" AS am_ad_category_Dep_rate,\n"
		     		+ "     am_ad_category.\"Accum_Dep_ledger\" AS am_ad_category_Accum_Dep_ledger,\n"
		     		+ "     am_ad_category.\"Dep_ledger\" AS am_ad_category_Dep_ledger,\n"
		     		+ "     am_ad_category.\"Asset_Ledger\" AS am_ad_category_Asset_Ledger,\n"
		     		+ "     am_ad_category.\"gl_account\" AS am_ad_category_gl_account,\n"
		     		+ "     AM_ASSET_UNCAPITALIZED.\"BRANCH_CODE\" AS am_Asset_BRANCH_CODE,\n"
		     		+ "     AM_ASSET_UNCAPITALIZED.\"CATEGORY_CODE\" AS am_Asset_CATEGORY_CODE,\n"
		     		+ "     am_UncapitalizedDisposal.\"Disposal_reason\" AS am_AssetDisposal_Disposal_reason,\n"
		     		+ "     am_UncapitalizedDisposal.\"Buyer_Account\" AS am_AssetDisposal_Buyer_Account,\n"
		     		+ "     am_UncapitalizedDisposal.\"Disposal_Amount\" AS am_AssetDisposal_Disposal_Amount,\n"
		     		+ "     am_UncapitalizedDisposal.\"Disposal_Date\" AS am_AssetDisposal_Disposal_Date,\n"
		     		+ "     am_UncapitalizedDisposal.\"Profit_Loss\" AS am_AssetDisposal_Profit_Loss,\n"
		     		+ "     am_ad_disposalReasons.\"description\" AS am_ad_disposalReasons_description,\n"
		     		+ "     AM_ASSET_UNCAPITALIZED.\"Asset_User\" AS am_Asset_Asset_User,\n"
		     		+ "     AM_ASSET_UNCAPITALIZED.\"BAR_CODE\" AS am_Asset_BAR_CODE,\n"
		     		+ "     AM_ASSET_UNCAPITALIZED.\"Asset_Status\" AS am_Asset_Asset_Status,\n"
		     		+ "     am_gb_company.\"company_name\" AS am_gb_company_company_name\n"
		     		+ "FROM\n"
		     		+ "     \"dbo\".\"AM_ASSET_UNCAPITALIZED\" AM_ASSET_UNCAPITALIZED INNER JOIN \"dbo\".\"am_ad_branch\" am_ad_branch ON AM_ASSET_UNCAPITALIZED.\"BRANCH_CODE\" = am_ad_branch.\"BRANCH_CODE\"\n"
		     		+ "     INNER JOIN \"dbo\".\"am_ad_category\" am_ad_category ON AM_ASSET_UNCAPITALIZED.\"CATEGORY_CODE\" = am_ad_category.\"category_code\"\n"
		     		+ "     INNER JOIN \"dbo\".\"am_UncapitalizedDisposal\" am_UncapitalizedDisposal ON AM_ASSET_UNCAPITALIZED.\"Asset_id\" = am_UncapitalizedDisposal.\"Asset_id\"\n"
		     		+ "     INNER JOIN \"dbo\".\"am_ad_disposalReasons\" am_ad_disposalReasons ON am_UncapitalizedDisposal.\"Disposal_reason\" = am_ad_disposalReasons.\"reason_id\",\n"
		     		+ "     \"dbo\".\"am_gb_company\" am_gb_company\n"
		     		+ "WHERE\n"
		     		+ "     am_UncapitalizedDisposal.\"Asset_id\" = AM_ASSET_UNCAPITALIZED.\"Asset_id\" and AM_ASSET_UNCAPITALIZED.\"Asset_Status\" = 'DISPOSED' \n"
		     		+ "    AND am_ad_category.category_id = ?\n"
		     		+ "    AND am_ad_branch.branch_id = ?\n"
		     		+ "ORDER BY+\n"
		     		+ "     am_ad_branch.\"BRANCH_NAME\" ASC,\n"
		     		+ "     am_ad_category.\"category_code\" ASC";    
			}      
	 
   if(!branch_Id.equals("0")  && !categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")){
	   System.out.println("======>>>>>>>Branch and Category and Date Selected: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate);
	     ColQuery ="SELECT\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"Asset_id\" AS am_Asset_Asset_id,\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"Description\" AS am_Asset_Description,\n"
	     		+ "     am_ad_branch.\"BRANCH_NAME\" AS am_ad_branch_BRANCH_NAME,\n"
	     		+ "     am_ad_category.\"category_name\" AS am_ad_category_category_name,\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"Accum_dep\" AS am_Asset_Accum_dep,\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"monthly_dep\" AS am_Asset_monthly_dep,\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"Cost_Price\" AS am_Asset_Cost_Price,\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"NBV\" AS am_Asset_NBV,\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"Date_purchased\" AS am_Asset_Date_purchased,\n"
	     		+ "     am_ad_category.\"Dep_rate\" AS am_ad_category_Dep_rate,\n"
	     		+ "     am_ad_category.\"Accum_Dep_ledger\" AS am_ad_category_Accum_Dep_ledger,\n"
	     		+ "     am_ad_category.\"Dep_ledger\" AS am_ad_category_Dep_ledger,\n"
	     		+ "     am_ad_category.\"Asset_Ledger\" AS am_ad_category_Asset_Ledger,\n"
	     		+ "     am_ad_category.\"gl_account\" AS am_ad_category_gl_account,\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"BRANCH_CODE\" AS am_Asset_BRANCH_CODE,\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"CATEGORY_CODE\" AS am_Asset_CATEGORY_CODE,\n"
	     		+ "     am_UncapitalizedDisposal.\"Disposal_reason\" AS am_AssetDisposal_Disposal_reason,\n"
	     		+ "     am_UncapitalizedDisposal.\"Buyer_Account\" AS am_AssetDisposal_Buyer_Account,\n"
	     		+ "     am_UncapitalizedDisposal.\"Disposal_Amount\" AS am_AssetDisposal_Disposal_Amount,\n"
	     		+ "     am_UncapitalizedDisposal.\"Disposal_Date\" AS am_AssetDisposal_Disposal_Date,\n"
	     		+ "     am_UncapitalizedDisposal.\"Profit_Loss\" AS am_AssetDisposal_Profit_Loss,\n"
	     		+ "     am_ad_disposalReasons.\"description\" AS am_ad_disposalReasons_description,\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"Asset_User\" AS am_Asset_Asset_User,\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"BAR_CODE\" AS am_Asset_BAR_CODE,\n"
	     		+ "     AM_ASSET_UNCAPITALIZED.\"Asset_Status\" AS am_Asset_Asset_Status,\n"
	     		+ "     am_gb_company.\"company_name\" AS am_gb_company_company_name\n"
	     		+ "FROM\n"
	     		+ "     \"dbo\".\"AM_ASSET_UNCAPITALIZED\" AM_ASSET_UNCAPITALIZED INNER JOIN \"dbo\".\"am_ad_branch\" am_ad_branch ON AM_ASSET_UNCAPITALIZED.\"BRANCH_CODE\" = am_ad_branch.\"BRANCH_CODE\"\n"
	     		+ "     INNER JOIN \"dbo\".\"am_ad_category\" am_ad_category ON AM_ASSET_UNCAPITALIZED.\"CATEGORY_CODE\" = am_ad_category.\"category_code\"\n"
	     		+ "     INNER JOIN \"dbo\".\"am_UncapitalizedDisposal\" am_UncapitalizedDisposal ON AM_ASSET_UNCAPITALIZED.\"Asset_id\" = am_UncapitalizedDisposal.\"Asset_id\"\n"
	     		+ "     INNER JOIN \"dbo\".\"am_ad_disposalReasons\" am_ad_disposalReasons ON am_UncapitalizedDisposal.\"Disposal_reason\" = am_ad_disposalReasons.\"reason_id\",\n"
	     		+ "     \"dbo\".\"am_gb_company\" am_gb_company\n"
	     		+ "WHERE\n"
	     		+ "     am_UncapitalizedDisposal.\"Asset_id\" = AM_ASSET_UNCAPITALIZED.\"Asset_id\" and AM_ASSET_UNCAPITALIZED.\"Asset_Status\" = 'DISPOSED' \n"
	     		+ "    AND am_ad_category.category_id = ?\n"
	     		+ "    AND am_ad_branch.branch_id = ?\n"
	     		+ "	AND am_UncapitalizedDisposal.Disposal_Date BETWEEN ? and ?\n"
	     		+ "ORDER BY+\n"
	     		+ "     am_ad_branch.\"BRANCH_NAME\" ASC,\n"
	     		+ "     am_ad_category.\"category_code\" ASC";    
		}      
//   System.out.println("======>>>>>>>ColQuery: "+ColQuery);
     java.util.ArrayList list =rep.getUncapitalizedDisposalRecords(ColQuery,branch_Id,categoryCode,FromDate,ToDate,asset_Id);
     System.out.println("======>>>>>>>list size: "+list.size()+"        =====report: "+report);
     if(list.size()!=0){
   	 if(report.equalsIgnoreCase("rptMenuBCUNCAPDIS")){
    	 SXSSFWorkbook workbook = new SXSSFWorkbook();
         Sheet sheet = workbook.createSheet("Demo");
         Row rowhead = sheet.createRow((int) 0);
         
         rowhead.createCell( 0).setCellValue("S/No.");
         rowhead.createCell( 1).setCellValue("Category Name");
         rowhead.createCell( 2).setCellValue("Category Dep. Rate");
         rowhead.createCell( 3).setCellValue("Accum Dep. Ledger");
         rowhead.createCell( 4).setCellValue("Dep. Ledger");
         rowhead.createCell( 5).setCellValue("Asset Ledger");
         rowhead.createCell( 6).setCellValue("GL Account");
         rowhead.createCell( 7).setCellValue("Asset Id");
         rowhead.createCell( 8).setCellValue("Old Branch Id");
         rowhead.createCell( 9).setCellValue("Old Department Id");
		 rowhead.createCell( 10).setCellValue("Transfer Date");
		 rowhead.createCell( 11).setCellValue("Old Asset User");
		 rowhead.createCell( 12).setCellValue("Old Section");
		 rowhead.createCell( 13).setCellValue("Old Branch Code");
		 rowhead.createCell( 14).setCellValue("Old Section Code");
		 rowhead.createCell( 15).setCellValue("Old Department Code");
		 rowhead.createCell( 16).setCellValue("Branch Name");
		 rowhead.createCell( 17).setCellValue("Approval Status");
		 rowhead.createCell( 18).setCellValue("Old Category Code");
         rowhead.createCell( 19).setCellValue("Description");
         rowhead.createCell( 20).setCellValue("Cost Price");
         rowhead.createCell( 21).setCellValue("Accum Dep");
         rowhead.createCell( 22).setCellValue("Monthly Dep");
         rowhead.createCell( 23).setCellValue("New Branch Code");
         rowhead.createCell( 24).setCellValue("New Section code");
         rowhead.createCell( 25).setCellValue("New Department Code");
         rowhead.createCell( 26).setCellValue("NBV");
         rowhead.createCell( 27).setCellValue("New Asset Id");
         rowhead.createCell( 28).setCellValue("Branch Id");
         
         

     int i = 1;
     System.out.println("<<<<<<list.size(): "+list.size());
	 for(int k=0;k<list.size();k++)
     {
    	 com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k); 
    	 
			String assetId = newassettrans.getAssetId();
			String description = newassettrans.getDescription();
			String branchName = newassettrans.getBranchName();
			String categoryName = newassettrans.getCategoryName();
			double accumDep = newassettrans.getAccumDep();
			double monthlyDepr = newassettrans.getMonthlyDep();
			double costPrice = newassettrans.getCostPrice();
			double nbv = newassettrans.getNbv();
			String purchaseDate = newassettrans.getDatepurchased() != null ? getDate(newassettrans.getDatepurchased()) : "";
			double depRate = newassettrans.getDepRate();
			String accumDeprLedger = newassettrans.getAccumDepLedger();
			String depLedger = newassettrans.getDepLedger();
			String assetLedger = newassettrans.getAssetLedger();
			String gl_Account = newassettrans.getGlAccount();
			String branch_code = newassettrans.getBranchCode();
			String category_Code = newassettrans.getCategoryCode();
			String disposalReason = newassettrans.getDisposeReason();
			String buyerAccount = newassettrans.getCreditAccount();
			double disposalAmount = newassettrans.getDisposalAmount();
			String disposalDate = newassettrans.getDisposalDate();
			double profit_loss = newassettrans.getProfitAmount();
			String disposal_Description = newassettrans.getAction() != null ? newassettrans.getAction() : "";
			String asset_user = newassettrans.getAssetUser() != null ? newassettrans.getAssetUser() : "";
			String bar_Code = newassettrans.getBarCode() != null ? newassettrans.getBarCode() : "";
			String asset_status = newassettrans.getAssetStatus();
			String company_name = newassettrans.getOldBranchId();
			//			String vendorName = records.getCodeName("select VENDOR_NAME from am_ad_vendor where VENDOR_ID = "+vendorId+"");
			

			Row row = sheet.createRow((int) i);

			row.createCell((short) 0).setCellValue(i);
			row.createCell((short) 1).setCellValue(assetId);
            row.createCell((short) 2).setCellValue(description);
            row.createCell((short) 3).setCellValue(branchName);
            row.createCell((short) 4).setCellValue(categoryName);
            row.createCell((short) 5).setCellValue(accumDep);
            row.createCell((short) 6).setCellValue(monthlyDepr);
            row.createCell((short) 7).setCellValue(costPrice);
            row.createCell((short) 8).setCellValue(nbv);
            row.createCell((short) 9).setCellValue(purchaseDate);
			row.createCell((short) 10).setCellValue(depRate);
			row.createCell((short) 11).setCellValue(accumDeprLedger);
			row.createCell((short) 12).setCellValue(depLedger);
			row.createCell((short) 13).setCellValue(assetLedger);
			row.createCell((short) 14).setCellValue(gl_Account);
			row.createCell((short) 15).setCellValue(branch_code);
			row.createCell((short) 16).setCellValue(category_Code);
            row.createCell((short) 17).setCellValue(disposalReason);
            row.createCell((short) 18).setCellValue(buyerAccount);
            row.createCell((short) 19).setCellValue(disposalAmount);
            row.createCell((short) 20).setCellValue(disposalDate);
            row.createCell((short) 21).setCellValue(profit_loss);
            row.createCell((short) 22).setCellValue(disposal_Description);
            row.createCell((short) 23).setCellValue(asset_user);
            row.createCell((short) 24).setCellValue(bar_Code);
            row.createCell((short) 25).setCellValue(asset_status);
            row.createCell((short) 26).setCellValue(company_name);
 
            i++;
//            System.out.println("we are here");
     }
	 System.out.println("we are here 2");
	   OutputStream stream = response.getOutputStream();
         workbook.write(stream);
         stream.close();
         workbook.close();  
         System.out.println("Data is saved in excel file.");
         
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