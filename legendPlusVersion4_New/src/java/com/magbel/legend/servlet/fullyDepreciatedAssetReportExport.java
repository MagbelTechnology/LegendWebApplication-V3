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
   
public class fullyDepreciatedAssetReportExport extends HttpServlet
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
    if(report.equalsIgnoreCase("rptMenuBCFD")){fileName = branchCode+"By"+userName+"FullyDepreciatedAssetReport.xlsx";}
    
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
   System.out.println("<<<<<<branch_Id: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate);
     String ColQuery = "";
     if(branch_Id.equals("0") && categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")){
  	   System.out.println("======>>>>>>>No Selection: ");
  	     ColQuery ="SELECT\n"
  	     		+ "     am_gb_company.\"company_name\" AS am_gb_company_company_name,\n"
  	     		+ "     am_Asset.\"Asset_id\" AS am_Asset_Asset_id,\n"
  	     		+ "     am_Asset.\"Description\" AS am_Asset_Description,\n"
  	     		+ "     am_ad_branch.\"BRANCH_NAME\" AS am_ad_branch_BRANCH_NAME,\n"
  	     		+ "     am_ad_category.\"category_name\" AS am_ad_category_category_name,\n"
  	     		+ "     am_Asset.\"Accum_dep\" AS am_Asset_Accum_dep,\n"
  	     		+ "     am_Asset.\"monthly_dep\" AS am_Asset_monthly_dep,\n"
  	     		+ "     am_Asset.\"Cost_Price\" AS am_Asset_Cost_Price,\n"
  	     		+ "	 am_Asset.\"NBV\" AS am_Asset_NBV,\n"
  	     		+ "     am_Asset.\"IMPROV_COST\" AS am_Asset_IMPROV_COST,\n"
  	     		+ "	 am_Asset.\"IMPROV_MONTHLYDEP\" AS am_Asset_IMPROV_MONTHLYDEP,\n"
  	     		+ "	 am_Asset.\"IMPROV_ACCUMDEP\" AS am_Asset_IMPROV_IMPROV_ACCUMDEP,\n"
  	     		+ "	 am_Asset.\"IMPROV_NBV\" AS am_Asset_IMPROV_IMPROV_NBV,\n"
  	     		+ "     am_Asset.\"Date_purchased\" AS am_Asset_Date_purchased,\n"
  	     		+ "     am_ad_category.\"Dep_rate\" AS am_ad_category_Dep_rate,\n"
  	     		+ "     am_ad_category.\"Accum_Dep_ledger\" AS am_ad_category_Accum_Dep_ledger,\n"
  	     		+ "     am_ad_category.\"Dep_ledger\" AS am_ad_category_Dep_ledger,\n"
  	     		+ "     am_ad_category.\"Asset_Ledger\" AS am_ad_category_Asset_Ledger,\n"
  	     		+ "     am_ad_category.\"gl_account\" AS am_ad_category_gl_account,\n"
  	     		+ "     am_Asset.\"Asset_User\" AS am_Asset_Asset_User\n"
  	     		+ "FROM\n"
  	     		+ "     \"dbo\".\"am_ad_branch\" am_ad_branch INNER JOIN \"dbo\".\"am_Asset\" am_Asset ON am_ad_branch.\"BRANCH_CODE\" = am_Asset.\"BRANCH_CODE\"\n"
  	     		+ "     INNER JOIN \"dbo\".\"am_ad_category\" am_ad_category ON am_Asset.\"CATEGORY_CODE\" = am_ad_category.\"category_code\",\n"
  	     		+ "     \"dbo\".\"am_gb_company\" am_gb_company\n"
  	     		+ "WHERE\n"
  	     		+ "     am_Asset.\"NBV\" = am_ad_category.\"residual_value\"\n"
  	     		+ "     and am_Asset.\"Asset_Status\" = 'ACTIVE'\n"
  	     		+ "     and upper(am_Asset.\"Req_Depreciation\") = 'Y'\n"
  	     		+ "     and am_Asset.\"Cost_Price\" > (am_gb_company.\"Cost_Threshold\"-0.01) \n"
  	     		+ "     \n"
  	     		+ "ORDER BY\n"
  	     		+ "     am_ad_branch.\"BRANCH_CODE\" ASC,\n"
  	     		+ "     am_Asset.\"CATEGORY_CODE\" ASC";    
  		}
     
     
     if(branch_Id.equals("0")  && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")){
    	 System.out.println("======>>>>>>>Date Selected: ");	     
	     ColQuery ="SELECT\n"
	     		+ "     am_gb_company.\"company_name\" AS am_gb_company_company_name,\n"
	     		+ "     am_Asset.\"Asset_id\" AS am_Asset_Asset_id,\n"
	     		+ "     am_Asset.\"Description\" AS am_Asset_Description,\n"
	     		+ "     am_ad_branch.\"BRANCH_NAME\" AS am_ad_branch_BRANCH_NAME,\n"
	     		+ "     am_ad_category.\"category_name\" AS am_ad_category_category_name,\n"
	     		+ "     am_Asset.\"Accum_dep\" AS am_Asset_Accum_dep,\n"
	     		+ "     am_Asset.\"monthly_dep\" AS am_Asset_monthly_dep,\n"
	     		+ "     am_Asset.\"Cost_Price\" AS am_Asset_Cost_Price,\n"
	     		+ "	 am_Asset.\"NBV\" AS am_Asset_NBV,\n"
	     		+ "     am_Asset.\"IMPROV_COST\" AS am_Asset_IMPROV_COST,\n"
	     		+ "	 am_Asset.\"IMPROV_MONTHLYDEP\" AS am_Asset_IMPROV_MONTHLYDEP,\n"
	     		+ "	 am_Asset.\"IMPROV_ACCUMDEP\" AS am_Asset_IMPROV_IMPROV_ACCUMDEP,\n"
	     		+ "	 am_Asset.\"IMPROV_NBV\" AS am_Asset_IMPROV_IMPROV_NBV,\n"
	     		+ "     am_Asset.\"Date_purchased\" AS am_Asset_Date_purchased,\n"
	     		+ "     am_ad_category.\"Dep_rate\" AS am_ad_category_Dep_rate,\n"
	     		+ "     am_ad_category.\"Accum_Dep_ledger\" AS am_ad_category_Accum_Dep_ledger,\n"
	     		+ "     am_ad_category.\"Dep_ledger\" AS am_ad_category_Dep_ledger,\n"
	     		+ "     am_ad_category.\"Asset_Ledger\" AS am_ad_category_Asset_Ledger,\n"
	     		+ "     am_ad_category.\"gl_account\" AS am_ad_category_gl_account,\n"
	     		+ "     am_Asset.\"Asset_User\" AS am_Asset_Asset_User\n"
	     		+ "FROM\n"
	     		+ "     \"dbo\".\"am_ad_branch\" am_ad_branch INNER JOIN \"dbo\".\"am_Asset\" am_Asset ON am_ad_branch.\"BRANCH_CODE\" = am_Asset.\"BRANCH_CODE\"\n"
	     		+ "     INNER JOIN \"dbo\".\"am_ad_category\" am_ad_category ON am_Asset.\"CATEGORY_CODE\" = am_ad_category.\"category_code\",\n"
	     		+ "     \"dbo\".\"am_gb_company\" am_gb_company\n"
	     		+ "WHERE\n"
	     		+ "     am_Asset.\"NBV\" = am_ad_category.\"residual_value\"\n"
	     		+ "     and am_Asset.\"Asset_Status\" = 'ACTIVE'\n"
	     		+ "     and upper(am_Asset.\"Req_Depreciation\") = 'Y'\n"
	     		+ "     and am_Asset.\"Cost_Price\" > (am_gb_company.\"Cost_Threshold\"-0.01)\n"
	     		+ "     and am_Asset.dep_end_date BETWEEN ? AND ?\n"
	     		+ "     \n"
	     		+ "ORDER BY\n"
	     		+ "     am_ad_branch.\"BRANCH_CODE\" ASC,\n"
	     		+ "     am_Asset.\"CATEGORY_CODE\" ASC";  
	}      
	 if(!branch_Id.equals("0")  && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")){	   
	   System.out.println("======>>>>>>>Branch and Date Selected: ");
	     ColQuery ="SELECT\n"
	     		+ "     am_gb_company.\"company_name\" AS am_gb_company_company_name,\n"
	     		+ "     am_Asset.\"Asset_id\" AS am_Asset_Asset_id,\n"
	     		+ "     am_Asset.\"Description\" AS am_Asset_Description,\n"
	     		+ "     am_ad_branch.\"BRANCH_NAME\" AS am_ad_branch_BRANCH_NAME,\n"
	     		+ "     am_ad_category.\"category_name\" AS am_ad_category_category_name,\n"
	     		+ "     am_Asset.\"Accum_dep\" AS am_Asset_Accum_dep,\n"
	     		+ "     am_Asset.\"monthly_dep\" AS am_Asset_monthly_dep,\n"
	     		+ "     am_Asset.\"Cost_Price\" AS am_Asset_Cost_Price,\n"
	     		+ "	 am_Asset.\"NBV\" AS am_Asset_NBV,\n"
	     		+ "     am_Asset.\"IMPROV_COST\" AS am_Asset_IMPROV_COST,\n"
	     		+ "	 am_Asset.\"IMPROV_MONTHLYDEP\" AS am_Asset_IMPROV_MONTHLYDEP,\n"
	     		+ "	 am_Asset.\"IMPROV_ACCUMDEP\" AS am_Asset_IMPROV_IMPROV_ACCUMDEP,\n"
	     		+ "	 am_Asset.\"IMPROV_NBV\" AS am_Asset_IMPROV_IMPROV_NBV,\n"
	     		+ "     am_Asset.\"Date_purchased\" AS am_Asset_Date_purchased,\n"
	     		+ "     am_ad_category.\"Dep_rate\" AS am_ad_category_Dep_rate,\n"
	     		+ "     am_ad_category.\"Accum_Dep_ledger\" AS am_ad_category_Accum_Dep_ledger,\n"
	     		+ "     am_ad_category.\"Dep_ledger\" AS am_ad_category_Dep_ledger,\n"
	     		+ "     am_ad_category.\"Asset_Ledger\" AS am_ad_category_Asset_Ledger,\n"
	     		+ "     am_ad_category.\"gl_account\" AS am_ad_category_gl_account,\n"
	     		+ "     am_Asset.\"Asset_User\" AS am_Asset_Asset_User\n"
	     		+ "FROM\n"
	     		+ "     \"dbo\".\"am_ad_branch\" am_ad_branch INNER JOIN \"dbo\".\"am_Asset\" am_Asset ON am_ad_branch.\"BRANCH_CODE\" = am_Asset.\"BRANCH_CODE\"\n"
	     		+ "     INNER JOIN \"dbo\".\"am_ad_category\" am_ad_category ON am_Asset.\"CATEGORY_CODE\" = am_ad_category.\"category_code\",\n"
	     		+ "     \"dbo\".\"am_gb_company\" am_gb_company\n"
	     		+ "WHERE\n"
	     		+ "     am_Asset.\"NBV\" = am_ad_category.\"residual_value\"\n"
	     		+ "     and am_Asset.\"Asset_Status\" = 'ACTIVE'\n"
	     		+ "     and upper(am_Asset.\"Req_Depreciation\") = 'Y'\n"
	     		+ "     and am_Asset.\"Cost_Price\" > (am_gb_company.\"Cost_Threshold\"-0.01)\n"
	     		+ "     and am_ad_branch.branch_id = ?\n"
	     		+ "     and am_Asset.dep_end_date BETWEEN ? AND ?\n"
	     		+ "     \n"
	     		+ "ORDER BY\n"
	     		+ "     am_ad_branch.\"BRANCH_CODE\" ASC,\n"
	     		+ "     am_Asset.\"CATEGORY_CODE\" ASC";   
		}  
	 
	 if(!branch_Id.equals("0")  && categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")){	   
		   System.out.println("======>>>>>>>Branch Selected: ");
		     ColQuery ="SELECT\n"
		     		+ "     am_gb_company.\"company_name\" AS am_gb_company_company_name,\n"
		     		+ "     am_Asset.\"Asset_id\" AS am_Asset_Asset_id,\n"
		     		+ "     am_Asset.\"Description\" AS am_Asset_Description,\n"
		     		+ "     am_ad_branch.\"BRANCH_NAME\" AS am_ad_branch_BRANCH_NAME,\n"
		     		+ "     am_ad_category.\"category_name\" AS am_ad_category_category_name,\n"
		     		+ "     am_Asset.\"Accum_dep\" AS am_Asset_Accum_dep,\n"
		     		+ "     am_Asset.\"monthly_dep\" AS am_Asset_monthly_dep,\n"
		     		+ "     am_Asset.\"Cost_Price\" AS am_Asset_Cost_Price,\n"
		     		+ "	 am_Asset.\"NBV\" AS am_Asset_NBV,\n"
		     		+ "     am_Asset.\"IMPROV_COST\" AS am_Asset_IMPROV_COST,\n"
		     		+ "	 am_Asset.\"IMPROV_MONTHLYDEP\" AS am_Asset_IMPROV_MONTHLYDEP,\n"
		     		+ "	 am_Asset.\"IMPROV_ACCUMDEP\" AS am_Asset_IMPROV_IMPROV_ACCUMDEP,\n"
		     		+ "	 am_Asset.\"IMPROV_NBV\" AS am_Asset_IMPROV_IMPROV_NBV,\n"
		     		+ "     am_Asset.\"Date_purchased\" AS am_Asset_Date_purchased,\n"
		     		+ "     am_ad_category.\"Dep_rate\" AS am_ad_category_Dep_rate,\n"
		     		+ "     am_ad_category.\"Accum_Dep_ledger\" AS am_ad_category_Accum_Dep_ledger,\n"
		     		+ "     am_ad_category.\"Dep_ledger\" AS am_ad_category_Dep_ledger,\n"
		     		+ "     am_ad_category.\"Asset_Ledger\" AS am_ad_category_Asset_Ledger,\n"
		     		+ "     am_ad_category.\"gl_account\" AS am_ad_category_gl_account,\n"
		     		+ "     am_Asset.\"Asset_User\" AS am_Asset_Asset_User\n"
		     		+ "FROM\n"
		     		+ "     \"dbo\".\"am_ad_branch\" am_ad_branch INNER JOIN \"dbo\".\"am_Asset\" am_Asset ON am_ad_branch.\"BRANCH_CODE\" = am_Asset.\"BRANCH_CODE\"\n"
		     		+ "     INNER JOIN \"dbo\".\"am_ad_category\" am_ad_category ON am_Asset.\"CATEGORY_CODE\" = am_ad_category.\"category_code\",\n"
		     		+ "     \"dbo\".\"am_gb_company\" am_gb_company\n"
		     		+ "WHERE\n"
		     		+ "     am_Asset.\"NBV\" = am_ad_category.\"residual_value\"\n"
		     		+ "     and am_Asset.\"Asset_Status\" = 'ACTIVE'\n"
		     		+ "     and upper(am_Asset.\"Req_Depreciation\") = 'Y'\n"
		     		+ "     and am_Asset.\"Cost_Price\" > (am_gb_company.\"Cost_Threshold\"-0.01)\n"
		     		+ "     and am_ad_branch.branch_id = ?\n"
		     		+ "     \n"
		     		+ "ORDER BY\n"
		     		+ "     am_ad_branch.\"BRANCH_CODE\" ASC,\n"
		     		+ "     am_Asset.\"CATEGORY_CODE\" ASC";   
			}      

	 if(branch_Id.equals("0")  && !categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")){	   
	   System.out.println("======>>>>>>>Category and Date Selected: ");
	     ColQuery ="SELECT\n"
	     		+ "     am_gb_company.\"company_name\" AS am_gb_company_company_name,\n"
	     		+ "     am_Asset.\"Asset_id\" AS am_Asset_Asset_id,\n"
	     		+ "     am_Asset.\"Description\" AS am_Asset_Description,\n"
	     		+ "     am_ad_branch.\"BRANCH_NAME\" AS am_ad_branch_BRANCH_NAME,\n"
	     		+ "     am_ad_category.\"category_name\" AS am_ad_category_category_name,\n"
	     		+ "     am_Asset.\"Accum_dep\" AS am_Asset_Accum_dep,\n"
	     		+ "     am_Asset.\"monthly_dep\" AS am_Asset_monthly_dep,\n"
	     		+ "     am_Asset.\"Cost_Price\" AS am_Asset_Cost_Price,\n"
	     		+ "	 am_Asset.\"NBV\" AS am_Asset_NBV,\n"
	     		+ "     am_Asset.\"IMPROV_COST\" AS am_Asset_IMPROV_COST,\n"
	     		+ "	 am_Asset.\"IMPROV_MONTHLYDEP\" AS am_Asset_IMPROV_MONTHLYDEP,\n"
	     		+ "	 am_Asset.\"IMPROV_ACCUMDEP\" AS am_Asset_IMPROV_IMPROV_ACCUMDEP,\n"
	     		+ "	 am_Asset.\"IMPROV_NBV\" AS am_Asset_IMPROV_IMPROV_NBV,\n"
	     		+ "     am_Asset.\"Date_purchased\" AS am_Asset_Date_purchased,\n"
	     		+ "     am_ad_category.\"Dep_rate\" AS am_ad_category_Dep_rate,\n"
	     		+ "     am_ad_category.\"Accum_Dep_ledger\" AS am_ad_category_Accum_Dep_ledger,\n"
	     		+ "     am_ad_category.\"Dep_ledger\" AS am_ad_category_Dep_ledger,\n"
	     		+ "     am_ad_category.\"Asset_Ledger\" AS am_ad_category_Asset_Ledger,\n"
	     		+ "     am_ad_category.\"gl_account\" AS am_ad_category_gl_account,\n"
	     		+ "     am_Asset.\"Asset_User\" AS am_Asset_Asset_User\n"
	     		+ "FROM\n"
	     		+ "     \"dbo\".\"am_ad_branch\" am_ad_branch INNER JOIN \"dbo\".\"am_Asset\" am_Asset ON am_ad_branch.\"BRANCH_CODE\" = am_Asset.\"BRANCH_CODE\"\n"
	     		+ "     INNER JOIN \"dbo\".\"am_ad_category\" am_ad_category ON am_Asset.\"CATEGORY_CODE\" = am_ad_category.\"category_code\",\n"
	     		+ "     \"dbo\".\"am_gb_company\" am_gb_company\n"
	     		+ "WHERE\n"
	     		+ "     am_Asset.\"NBV\" = am_ad_category.\"residual_value\"\n"
	     		+ "     and am_Asset.\"Asset_Status\" = 'ACTIVE'\n"
	     		+ "     and upper(am_Asset.\"Req_Depreciation\") = 'Y'\n"
	     		+ "     and am_Asset.\"Cost_Price\" > (am_gb_company.\"Cost_Threshold\"-0.01)\n"
	     		+ "     and am_ad_category.category_id = ?\n"
	     		+ "     and am_Asset.dep_end_date BETWEEN ? AND ?\n"
	     		+ "     \n"
	     		+ "ORDER BY\n"
	     		+ "     am_ad_branch.\"BRANCH_CODE\" ASC,\n"
	     		+ "     am_Asset.\"CATEGORY_CODE\" ASC";    
		}      
	 
	 if(branch_Id.equals("0")  && !categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")){	   
		   System.out.println("======>>>>>>>Category Selected: ");
		     ColQuery ="SELECT\n"
		     		+ "     am_gb_company.\"company_name\" AS am_gb_company_company_name,\n"
		     		+ "     am_Asset.\"Asset_id\" AS am_Asset_Asset_id,\n"
		     		+ "     am_Asset.\"Description\" AS am_Asset_Description,\n"
		     		+ "     am_ad_branch.\"BRANCH_NAME\" AS am_ad_branch_BRANCH_NAME,\n"
		     		+ "     am_ad_category.\"category_name\" AS am_ad_category_category_name,\n"
		     		+ "     am_Asset.\"Accum_dep\" AS am_Asset_Accum_dep,\n"
		     		+ "     am_Asset.\"monthly_dep\" AS am_Asset_monthly_dep,\n"
		     		+ "     am_Asset.\"Cost_Price\" AS am_Asset_Cost_Price,\n"
		     		+ "	 am_Asset.\"NBV\" AS am_Asset_NBV,\n"
		     		+ "     am_Asset.\"IMPROV_COST\" AS am_Asset_IMPROV_COST,\n"
		     		+ "	 am_Asset.\"IMPROV_MONTHLYDEP\" AS am_Asset_IMPROV_MONTHLYDEP,\n"
		     		+ "	 am_Asset.\"IMPROV_ACCUMDEP\" AS am_Asset_IMPROV_IMPROV_ACCUMDEP,\n"
		     		+ "	 am_Asset.\"IMPROV_NBV\" AS am_Asset_IMPROV_IMPROV_NBV,\n"
		     		+ "     am_Asset.\"Date_purchased\" AS am_Asset_Date_purchased,\n"
		     		+ "     am_ad_category.\"Dep_rate\" AS am_ad_category_Dep_rate,\n"
		     		+ "     am_ad_category.\"Accum_Dep_ledger\" AS am_ad_category_Accum_Dep_ledger,\n"
		     		+ "     am_ad_category.\"Dep_ledger\" AS am_ad_category_Dep_ledger,\n"
		     		+ "     am_ad_category.\"Asset_Ledger\" AS am_ad_category_Asset_Ledger,\n"
		     		+ "     am_ad_category.\"gl_account\" AS am_ad_category_gl_account,\n"
		     		+ "     am_Asset.\"Asset_User\" AS am_Asset_Asset_User\n"
		     		+ "FROM\n"
		     		+ "     \"dbo\".\"am_ad_branch\" am_ad_branch INNER JOIN \"dbo\".\"am_Asset\" am_Asset ON am_ad_branch.\"BRANCH_CODE\" = am_Asset.\"BRANCH_CODE\"\n"
		     		+ "     INNER JOIN \"dbo\".\"am_ad_category\" am_ad_category ON am_Asset.\"CATEGORY_CODE\" = am_ad_category.\"category_code\",\n"
		     		+ "     \"dbo\".\"am_gb_company\" am_gb_company\n"
		     		+ "WHERE\n"
		     		+ "     am_Asset.\"NBV\" = am_ad_category.\"residual_value\"\n"
		     		+ "     and am_Asset.\"Asset_Status\" = 'ACTIVE'\n"
		     		+ "     and upper(am_Asset.\"Req_Depreciation\") = 'Y'\n"
		     		+ "     and am_Asset.\"Cost_Price\" > (am_gb_company.\"Cost_Threshold\"-0.01)\n"
		     		+ "     and am_ad_category.category_id = ?\n"
		     		+ "     \n"
		     		+ "ORDER BY\n"
		     		+ "     am_ad_branch.\"BRANCH_CODE\" ASC,\n"
		     		+ "     am_Asset.\"CATEGORY_CODE\" ASC";    
			}      
	 
   if(!branch_Id.equals("0")  && !categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")){
	   System.out.println("======>>>>>>>Branch and Category and Date Selected: ");
	     ColQuery ="SELECT\n"
	     		+ "     am_gb_company.\"company_name\" AS am_gb_company_company_name,\n"
	     		+ "     am_Asset.\"Asset_id\" AS am_Asset_Asset_id,\n"
	     		+ "     am_Asset.\"Description\" AS am_Asset_Description,\n"
	     		+ "     am_ad_branch.\"BRANCH_NAME\" AS am_ad_branch_BRANCH_NAME,\n"
	     		+ "     am_ad_category.\"category_name\" AS am_ad_category_category_name,\n"
	     		+ "     am_Asset.\"Accum_dep\" AS am_Asset_Accum_dep,\n"
	     		+ "     am_Asset.\"monthly_dep\" AS am_Asset_monthly_dep,\n"
	     		+ "     am_Asset.\"Cost_Price\" AS am_Asset_Cost_Price,\n"
	     		+ "	 am_Asset.\"NBV\" AS am_Asset_NBV,\n"
	     		+ "     am_Asset.\"IMPROV_COST\" AS am_Asset_IMPROV_COST,\n"
	     		+ "	 am_Asset.\"IMPROV_MONTHLYDEP\" AS am_Asset_IMPROV_MONTHLYDEP,\n"
	     		+ "	 am_Asset.\"IMPROV_ACCUMDEP\" AS am_Asset_IMPROV_IMPROV_ACCUMDEP,\n"
	     		+ "	 am_Asset.\"IMPROV_NBV\" AS am_Asset_IMPROV_IMPROV_NBV,\n"
	     		+ "     am_Asset.\"Date_purchased\" AS am_Asset_Date_purchased,\n"
	     		+ "     am_ad_category.\"Dep_rate\" AS am_ad_category_Dep_rate,\n"
	     		+ "     am_ad_category.\"Accum_Dep_ledger\" AS am_ad_category_Accum_Dep_ledger,\n"
	     		+ "     am_ad_category.\"Dep_ledger\" AS am_ad_category_Dep_ledger,\n"
	     		+ "     am_ad_category.\"Asset_Ledger\" AS am_ad_category_Asset_Ledger,\n"
	     		+ "     am_ad_category.\"gl_account\" AS am_ad_category_gl_account,\n"
	     		+ "     am_Asset.\"Asset_User\" AS am_Asset_Asset_User\n"
	     		+ "FROM\n"
	     		+ "     \"dbo\".\"am_ad_branch\" am_ad_branch INNER JOIN \"dbo\".\"am_Asset\" am_Asset ON am_ad_branch.\"BRANCH_CODE\" = am_Asset.\"BRANCH_CODE\"\n"
	     		+ "     INNER JOIN \"dbo\".\"am_ad_category\" am_ad_category ON am_Asset.\"CATEGORY_CODE\" = am_ad_category.\"category_code\",\n"
	     		+ "     \"dbo\".\"am_gb_company\" am_gb_company\n"
	     		+ "WHERE\n"
	     		+ "     am_Asset.\"NBV\" = am_ad_category.\"residual_value\"\n"
	     		+ "     and am_Asset.\"Asset_Status\" = 'ACTIVE'\n"
	     		+ "     and upper(am_Asset.\"Req_Depreciation\") = 'Y'\n"
	     		+ "     and am_Asset.\"Cost_Price\" > (am_gb_company.\"Cost_Threshold\"-0.01)\n"
	     		+ "     and am_ad_category.category_id = ?\n"
	     		+ "     and am_ad_branch.branch_id = ?\n"
	     		+ "     and am_Asset.dep_end_date BETWEEN ? AND ?\n"
	     		+ "     \n"
	     		+ "ORDER BY\n"
	     		+ "     am_ad_branch.\"BRANCH_CODE\" ASC,\n"
	     		+ "     am_Asset.\"CATEGORY_CODE\" ASC";    
		}      
   
   if(!branch_Id.equals("0")  && !categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")){
	   System.out.println("======>>>>>>>Branch and Category ");
	     ColQuery ="SELECT\n"
	     		+ "     am_gb_company.\"company_name\" AS am_gb_company_company_name,\n"
	     		+ "     am_Asset.\"Asset_id\" AS am_Asset_Asset_id,\n"
	     		+ "     am_Asset.\"Description\" AS am_Asset_Description,\n"
	     		+ "     am_ad_branch.\"BRANCH_NAME\" AS am_ad_branch_BRANCH_NAME,\n"
	     		+ "     am_ad_category.\"category_name\" AS am_ad_category_category_name,\n"
	     		+ "     am_Asset.\"Accum_dep\" AS am_Asset_Accum_dep,\n"
	     		+ "     am_Asset.\"monthly_dep\" AS am_Asset_monthly_dep,\n"
	     		+ "     am_Asset.\"Cost_Price\" AS am_Asset_Cost_Price,\n"
	     		+ "	 am_Asset.\"NBV\" AS am_Asset_NBV,\n"
	     		+ "     am_Asset.\"IMPROV_COST\" AS am_Asset_IMPROV_COST,\n"
	     		+ "	 am_Asset.\"IMPROV_MONTHLYDEP\" AS am_Asset_IMPROV_MONTHLYDEP,\n"
	     		+ "	 am_Asset.\"IMPROV_ACCUMDEP\" AS am_Asset_IMPROV_IMPROV_ACCUMDEP,\n"
	     		+ "	 am_Asset.\"IMPROV_NBV\" AS am_Asset_IMPROV_IMPROV_NBV,\n"
	     		+ "     am_Asset.\"Date_purchased\" AS am_Asset_Date_purchased,\n"
	     		+ "     am_ad_category.\"Dep_rate\" AS am_ad_category_Dep_rate,\n"
	     		+ "     am_ad_category.\"Accum_Dep_ledger\" AS am_ad_category_Accum_Dep_ledger,\n"
	     		+ "     am_ad_category.\"Dep_ledger\" AS am_ad_category_Dep_ledger,\n"
	     		+ "     am_ad_category.\"Asset_Ledger\" AS am_ad_category_Asset_Ledger,\n"
	     		+ "     am_ad_category.\"gl_account\" AS am_ad_category_gl_account,\n"
	     		+ "     am_Asset.\"Asset_User\" AS am_Asset_Asset_User\n"
	     		+ "FROM\n"
	     		+ "     \"dbo\".\"am_ad_branch\" am_ad_branch INNER JOIN \"dbo\".\"am_Asset\" am_Asset ON am_ad_branch.\"BRANCH_CODE\" = am_Asset.\"BRANCH_CODE\"\n"
	     		+ "     INNER JOIN \"dbo\".\"am_ad_category\" am_ad_category ON am_Asset.\"CATEGORY_CODE\" = am_ad_category.\"category_code\",\n"
	     		+ "     \"dbo\".\"am_gb_company\" am_gb_company\n"
	     		+ "WHERE\n"
	     		+ "     am_Asset.\"NBV\" = am_ad_category.\"residual_value\"\n"
	     		+ "     and am_Asset.\"Asset_Status\" = 'ACTIVE'\n"
	     		+ "     and upper(am_Asset.\"Req_Depreciation\") = 'Y'\n"
	     		+ "     and am_Asset.\"Cost_Price\" > (am_gb_company.\"Cost_Threshold\"-0.01)\n"
	     		+ "     and am_ad_category.category_id = ?\n"
	     		+ "     and am_ad_branch.branch_id = ?\n"
	     		+ "     \n"
	     		+ "ORDER BY\n"
	     		+ "     am_ad_branch.\"BRANCH_CODE\" ASC,\n"
	     		+ "     am_Asset.\"CATEGORY_CODE\" ASC";    
		}      
//   System.out.println("======>>>>>>>ColQuery: "+ColQuery);
     java.util.ArrayList list =rep.getFullyDepreciatedAssetRecords(ColQuery,branch_Id,categoryCode,FromDate,ToDate,asset_Id);
     System.out.println("======>>>>>>>list size: "+list.size()+"        =====report: "+report);
     if(list.size()!=0){
   	 if(report.equalsIgnoreCase("rptMenuBCFD")){
    	 SXSSFWorkbook workbook = new SXSSFWorkbook();
         Sheet sheet = workbook.createSheet("Demo");
         Row rowhead = sheet.createRow((int) 0);
         
         rowhead.createCell( 0).setCellValue("S/No.");
         rowhead.createCell( 1).setCellValue("Asset Id");
         rowhead.createCell( 2).setCellValue("Asset Description");
         rowhead.createCell( 3).setCellValue("Branch Name");
         rowhead.createCell( 4).setCellValue("Category Name");
         rowhead.createCell( 5).setCellValue("Accum Dep");
         rowhead.createCell( 6).setCellValue("Monthly Dep");
         rowhead.createCell( 7).setCellValue("Cost Price");
         rowhead.createCell( 8).setCellValue("NBV");
         rowhead.createCell( 9).setCellValue("Improv Cost");
         rowhead.createCell( 10).setCellValue("Improv Monthly Dep");
		 rowhead.createCell( 11).setCellValue("Improv Accum Dep");
		 rowhead.createCell( 12).setCellValue("Improv NBV");
		 rowhead.createCell( 13).setCellValue("Date Purchased");
		 rowhead.createCell( 14).setCellValue("Dep Rate");
		 rowhead.createCell( 15).setCellValue("Accum Dep Ledger");
		 rowhead.createCell( 16).setCellValue("Dep Ledger");
		 rowhead.createCell( 17).setCellValue("Asset Ledger");
		 rowhead.createCell( 18).setCellValue("GL Account");
		 rowhead.createCell( 19).setCellValue("Asset User");
        // rowhead.createCell( 20).setCellValue("SBU Name");
        
         
         

     int i = 1;
     System.out.println("<<<<<<list.size(): "+list.size());
	 for(int k=0;k<list.size();k++)
     {
    	 com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);    	 
    	 
    	 	String assetId = newassettrans.getAssetId();
			String description = newassettrans.getDescription();
			String branchName = newassettrans.getBranchName();
			String categoryName = newassettrans.getCategoryName();
			double accumDepr = newassettrans.getAccumDep();
			double monthlyDepr = newassettrans.getMonthlyDep();
			double costPrice = newassettrans.getCostPrice();
			double nbv = newassettrans.getNbv();
			double improvCost = newassettrans.getImprovcostPrice();
			double improvMonthlyDep = newassettrans.getImprovmonthlyDep();
			double improvAccumDep = newassettrans.getImprovaccumDep();
			double improvNbv = newassettrans.getImprovnbv();
			String datePurchased = newassettrans.getDatepurchased() != null ? getDate(newassettrans.getDatepurchased()) : "";
			double depRate = newassettrans.getDepRate();
			String accumDepLedger = newassettrans.getAccumDepLedger();
			String depLedger = newassettrans.getDepLedger();
			String assetLedger = newassettrans.getAssetLedger();  
			String gl_Account = newassettrans.getGlAccount(); 
			String assetUser = newassettrans.getAssetUser();
		//	String sbuName = newassettrans.getSbuName();

			//			String vendorName = records.getCodeName("select VENDOR_NAME from am_ad_vendor where VENDOR_ID = "+vendorId+"");
			

			Row row = sheet.createRow((int) i);

			row.createCell((short) 0).setCellValue(i);
			row.createCell((short) 1).setCellValue(assetId);
            row.createCell((short) 2).setCellValue(description);
            row.createCell((short) 3).setCellValue(branchName);
            row.createCell((short) 4).setCellValue(categoryName);
            row.createCell((short) 5).setCellValue(accumDepr);
            row.createCell((short) 6).setCellValue(monthlyDepr);
            row.createCell((short) 7).setCellValue(costPrice);
            row.createCell((short) 8).setCellValue(nbv);
            row.createCell((short) 9).setCellValue(improvCost);
			row.createCell((short) 10).setCellValue(improvMonthlyDep);
			row.createCell((short) 11).setCellValue(improvAccumDep);
			row.createCell((short) 12).setCellValue(improvNbv);
			row.createCell((short) 13).setCellValue(datePurchased);
			row.createCell((short) 14).setCellValue(depRate);
			row.createCell((short) 15).setCellValue(accumDepLedger);
			row.createCell((short) 16).setCellValue(depLedger);
            row.createCell((short) 17).setCellValue(assetLedger);
            row.createCell((short) 18).setCellValue(gl_Account);
            row.createCell((short) 19).setCellValue(assetUser);
          //  row.createCell((short) 20).setCellValue(sbuName);

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