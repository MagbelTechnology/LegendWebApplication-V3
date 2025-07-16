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
   
public class UncapitalisedAssetReportExport extends HttpServlet
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
    if(report.equalsIgnoreCase("rptMenuBCRU")){fileName = branchCode+"By"+userName+"UncapitalisedAssetReport.xlsx";}
    
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
  	     ColQuery ="SELECT"
  	     		+ "     am_gb_company.company_name AS am_gb_company_company_name,"
  	     		+ "     AM_ASSET_UNCAPITALIZED.Old_Asset_id AS AM_ASSET_UNCAPITALIZED_Old_Asset_id,"
  	     		+ "     AM_ASSET_UNCAPITALIZED.Asset_id AS AM_ASSET_UNCAPITALIZED_Asset_id,"
  	     		+ "     AM_ASSET_UNCAPITALIZED.Description AS AM_ASSET_UNCAPITALIZED_Description,"
  	     		+ "     am_ad_branch.BRANCH_NAME AS am_ad_branch_BRANCH_NAME,"
  	     		+ "     am_ad_branch.BRANCH_CODE AS am_ad_branch_BRANCH_CODE,"
  	     		+ "     am_ad_category.category_name AS am_ad_category_category_name,"
  	     		+ "     AM_ASSET_UNCAPITALIZED.Accum_dep AS AM_ASSET_UNCAPITALIZED_Accum_dep,"
  	     		+ "     AM_ASSET_UNCAPITALIZED.monthly_dep AS AM_ASSET_UNCAPITALIZED_monthly_dep,"
  	     		+ "     AM_ASSET_UNCAPITALIZED.Cost_Price AS AM_ASSET_UNCAPITALIZED_Cost_Price,"
  	     		+ "     AM_ASSET_UNCAPITALIZED.NBV AS AM_ASSET_UNCAPITALIZED_NBV,"
  	     		+ "     AM_ASSET_UNCAPITALIZED.Date_purchased AS AM_ASSET_UNCAPITALIZED_Date_purchased,"
  	     		+ "     am_ad_category.Dep_rate AS am_ad_category_Dep_rate,"
  	     		+ "     am_ad_category.Accum_Dep_ledger AS am_ad_category_Accum_Dep_ledger,"
  	     		+ "     am_ad_category.Dep_ledger AS am_ad_category_Dep_ledger,"
  	     		+ "     am_ad_category.Asset_Ledger AS am_ad_category_Asset_Ledger,"
  	     		+ "     am_ad_category.gl_account AS am_ad_category_gl_account,"
  	     		+ "     AM_ASSET_UNCAPITALIZED.dep_end_date AS AM_ASSET_UNCAPITALIZED_dep_end_date,"
  	     		+ "     AM_ASSET_UNCAPITALIZED.effective_date AS AM_ASSET_UNCAPITALIZED_effective_date,"
  	     		+ "     AM_ASSET_UNCAPITALIZED.Posting_Date AS AM_ASSET_UNCAPITALIZED_Posting_Date,"
  	     		+ "     am_ad_department.Dept_name AS am_ad_department_Dept_name,"
  	     		+ "     AM_ASSET_UNCAPITALIZED.SBU_CODE AS AM_ASSET_UNCAPITALIZED_SBU_CODE,"
  	     		+ "	 AM_ASSET_UNCAPITALIZED.BAR_CODE AS AM_ASSET_UNCAPITALIZED_BAR_CODE,"
  	     		+ "     AM_ASSET_UNCAPITALIZED.Asset_User AS AM_ASSET_UNCAPITALIZED_Asset_User "
	     		+ "FROM"
	     		+ "     dbo.am_ad_branch am_ad_branch INNER JOIN dbo.AM_ASSET_UNCAPITALIZED AM_ASSET_UNCAPITALIZED ON am_ad_branch.BRANCH_CODE = AM_ASSET_UNCAPITALIZED.BRANCH_CODE"
	     		+ "     INNER JOIN dbo.am_ad_category am_ad_category ON AM_ASSET_UNCAPITALIZED.CATEGORY_CODE = am_ad_category.category_code"
	     		+ "     INNER JOIN dbo.am_ad_department am_ad_department ON AM_ASSET_UNCAPITALIZED.DEPT_CODE = am_ad_department.Dept_code,"
	     		+ "     dbo.am_gb_company am_gb_company "
	     		+ "WHERE "
	     		+ "     am_ad_branch.BRANCH_CODE = AM_ASSET_UNCAPITALIZED.BRANCH_CODE"
	     		+ "     And AM_ASSET_UNCAPITALIZED.Asset_Status = 'ACTIVE'"
  	     		+ "ORDER BY"
  	     		+ "     AM_ASSET_UNCAPITALIZED.BRANCH_CODE ASC,"
  	     		+ "     AM_ASSET_UNCAPITALIZED.CATEGORY_CODE ASC";    
  		}
     
     
     if(branch_Id.equals("0")  && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")){
    	 System.out.println("======>>>>>>>Date Selected: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate);	     
	     ColQuery ="SELECT"
	     		+ "     am_gb_company.company_name AS am_gb_company_company_name,"
	     		+ "     AM_ASSET_UNCAPITALIZED.Old_Asset_id AS AM_ASSET_UNCAPITALIZED_Old_Asset_id,"
	     		+ "     AM_ASSET_UNCAPITALIZED.Asset_id AS AM_ASSET_UNCAPITALIZED_Asset_id,"
	     		+ "     AM_ASSET_UNCAPITALIZED.Description AS AM_ASSET_UNCAPITALIZED_Description,"
	     		+ "     am_ad_branch.BRANCH_NAME AS am_ad_branch_BRANCH_NAME,"
	     		+ "     am_ad_branch.BRANCH_CODE AS am_ad_branch_BRANCH_CODE,"
	     		+ "     am_ad_category.category_name AS am_ad_category_category_name,"
	     		+ "     AM_ASSET_UNCAPITALIZED.Accum_dep AS AM_ASSET_UNCAPITALIZED_Accum_dep,"
	     		+ "     AM_ASSET_UNCAPITALIZED.monthly_dep AS AM_ASSET_UNCAPITALIZED_monthly_dep,"
	     		+ "     AM_ASSET_UNCAPITALIZED.Cost_Price AS AM_ASSET_UNCAPITALIZED_Cost_Price,"
	     		+ "     AM_ASSET_UNCAPITALIZED.NBV AS AM_ASSET_UNCAPITALIZED_NBV,"
	     		+ "     AM_ASSET_UNCAPITALIZED.Date_purchased AS AM_ASSET_UNCAPITALIZED_Date_purchased,"
	     		+ "     am_ad_category.Dep_rate AS am_ad_category_Dep_rate,"
	     		+ "     am_ad_category.Accum_Dep_ledger AS am_ad_category_Accum_Dep_ledger,"
	     		+ "     am_ad_category.Dep_ledger AS am_ad_category_Dep_ledger,"
	     		+ "     am_ad_category.Asset_Ledger AS am_ad_category_Asset_Ledger,"
	     		+ "     am_ad_category.gl_account AS am_ad_category_gl_account,"
	     		+ "     AM_ASSET_UNCAPITALIZED.dep_end_date AS AM_ASSET_UNCAPITALIZED_dep_end_date,"
	     		+ "     AM_ASSET_UNCAPITALIZED.effective_date AS AM_ASSET_UNCAPITALIZED_effective_date,"
	     		+ "     AM_ASSET_UNCAPITALIZED.Posting_Date AS AM_ASSET_UNCAPITALIZED_Posting_Date,"
	     		+ "     am_ad_department.Dept_name AS am_ad_department_Dept_name,"
	     		+ "     AM_ASSET_UNCAPITALIZED.SBU_CODE AS AM_ASSET_UNCAPITALIZED_SBU_CODE,"
	     		+ "	 AM_ASSET_UNCAPITALIZED.BAR_CODE AS AM_ASSET_UNCAPITALIZED_BAR_CODE,"
	     		+ "     AM_ASSET_UNCAPITALIZED.Asset_User AS AM_ASSET_UNCAPITALIZED_Asset_User "
	     		+ "FROM"
	     		+ "     dbo.am_ad_branch am_ad_branch INNER JOIN dbo.AM_ASSET_UNCAPITALIZED AM_ASSET_UNCAPITALIZED ON am_ad_branch.BRANCH_CODE = AM_ASSET_UNCAPITALIZED.BRANCH_CODE"
	     		+ "     INNER JOIN dbo.am_ad_category am_ad_category ON AM_ASSET_UNCAPITALIZED.CATEGORY_CODE = am_ad_category.category_code"
	     		+ "     INNER JOIN dbo.am_ad_department am_ad_department ON AM_ASSET_UNCAPITALIZED.DEPT_CODE = am_ad_department.Dept_code,"
	     		+ "     dbo.am_gb_company am_gb_company "
	     		+ "WHERE "
	     		+ "     am_ad_branch.BRANCH_CODE = AM_ASSET_UNCAPITALIZED.BRANCH_CODE"
	     		+ "     And AM_ASSET_UNCAPITALIZED.Asset_Status = 'ACTIVE'"
	     		+ "	 AND AM_ASSET_UNCAPITALIZED.date_purchased BETWEEN ? AND ? "
	     		+ "ORDER BY"
	     		+ "     AM_ASSET_UNCAPITALIZED.BRANCH_CODE ASC,"
	     		+ "     AM_ASSET_UNCAPITALIZED.CATEGORY_CODE ASC";  
	}      
	 if(!branch_Id.equals("0")  && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")){	   
	   System.out.println("======>>>>>>>Branch and Date Selected: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate);
	     ColQuery ="SELECT"
	     		+ "     am_gb_company.company_name AS am_gb_company_company_name,"
	     		+ "     AM_ASSET_UNCAPITALIZED.Old_Asset_id AS AM_ASSET_UNCAPITALIZED_Old_Asset_id,"
	     		+ "     AM_ASSET_UNCAPITALIZED.Asset_id AS AM_ASSET_UNCAPITALIZED_Asset_id,"
	     		+ "     AM_ASSET_UNCAPITALIZED.Description AS AM_ASSET_UNCAPITALIZED_Description,"
	     		+ "     am_ad_branch.BRANCH_NAME AS am_ad_branch_BRANCH_NAME,"
	     		+ "     am_ad_branch.BRANCH_CODE AS am_ad_branch_BRANCH_CODE,"
	     		+ "     am_ad_category.category_name AS am_ad_category_category_name,"
	     		+ "     AM_ASSET_UNCAPITALIZED.Accum_dep AS AM_ASSET_UNCAPITALIZED_Accum_dep,"
	     		+ "     AM_ASSET_UNCAPITALIZED.monthly_dep AS AM_ASSET_UNCAPITALIZED_monthly_dep,"
	     		+ "     AM_ASSET_UNCAPITALIZED.Cost_Price AS AM_ASSET_UNCAPITALIZED_Cost_Price,"
	     		+ "     AM_ASSET_UNCAPITALIZED.NBV AS AM_ASSET_UNCAPITALIZED_NBV,"
	     		+ "     AM_ASSET_UNCAPITALIZED.Date_purchased AS AM_ASSET_UNCAPITALIZED_Date_purchased,"
	     		+ "     am_ad_category.Dep_rate AS am_ad_category_Dep_rate,"
	     		+ "     am_ad_category.Accum_Dep_ledger AS am_ad_category_Accum_Dep_ledger,"
	     		+ "     am_ad_category.Dep_ledger AS am_ad_category_Dep_ledger,"
	     		+ "     am_ad_category.Asset_Ledger AS am_ad_category_Asset_Ledger,"
	     		+ "     am_ad_category.gl_account AS am_ad_category_gl_account,"
	     		+ "     AM_ASSET_UNCAPITALIZED.dep_end_date AS AM_ASSET_UNCAPITALIZED_dep_end_date,"
	     		+ "     AM_ASSET_UNCAPITALIZED.effective_date AS AM_ASSET_UNCAPITALIZED_effective_date,"
	     		+ "     AM_ASSET_UNCAPITALIZED.Posting_Date AS AM_ASSET_UNCAPITALIZED_Posting_Date,"
	     		+ "     am_ad_department.Dept_name AS am_ad_department_Dept_name,"
	     		+ "     AM_ASSET_UNCAPITALIZED.SBU_CODE AS AM_ASSET_UNCAPITALIZED_SBU_CODE,"
	     		+ "	 AM_ASSET_UNCAPITALIZED.BAR_CODE AS AM_ASSET_UNCAPITALIZED_BAR_CODE,"
	     		+ "     AM_ASSET_UNCAPITALIZED.Asset_User AS AM_ASSET_UNCAPITALIZED_Asset_User "
	     		+ "FROM"
	     		+ "     dbo.am_ad_branch am_ad_branch INNER JOIN dbo.AM_ASSET_UNCAPITALIZED AM_ASSET_UNCAPITALIZED ON am_ad_branch.BRANCH_CODE = AM_ASSET_UNCAPITALIZED.BRANCH_CODE"
	     		+ "     INNER JOIN dbo.am_ad_category am_ad_category ON AM_ASSET_UNCAPITALIZED.CATEGORY_CODE = am_ad_category.category_code"
	     		+ "     INNER JOIN dbo.am_ad_department am_ad_department ON AM_ASSET_UNCAPITALIZED.DEPT_CODE = am_ad_department.Dept_code,"
	     		+ "     dbo.am_gb_company am_gb_company "
	     		+ "WHERE "
	     		+ "     am_ad_branch.BRANCH_CODE = AM_ASSET_UNCAPITALIZED.BRANCH_CODE"
	     		+ "     and AM_ASSET_UNCAPITALIZED.Asset_Status = 'ACTIVE'"
	     		+ "     AND am_ad_branch.branch_id = ?"
	     		+ "	 AND AM_ASSET_UNCAPITALIZED.date_purchased BETWEEN ? AND ? "
	     		+ "ORDER BY"
	     		+ "     AM_ASSET_UNCAPITALIZED.BRANCH_CODE ASC,"
	     		+ "     AM_ASSET_UNCAPITALIZED.CATEGORY_CODE ASC";   
		}  
	 
	 if(!branch_Id.equals("0")  && categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")){	   
		   System.out.println("======>>>>>>>Branch Selected: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate);
		     ColQuery ="SELECT"
		     		+ "     am_gb_company.company_name AS am_gb_company_company_name,"
		     		+ "     AM_ASSET_UNCAPITALIZED.Old_Asset_id AS AM_ASSET_UNCAPITALIZED_Old_Asset_id,"
		     		+ "     AM_ASSET_UNCAPITALIZED.Asset_id AS AM_ASSET_UNCAPITALIZED_Asset_id,"
		     		+ "     AM_ASSET_UNCAPITALIZED.Description AS AM_ASSET_UNCAPITALIZED_Description,"
		     		+ "     am_ad_branch.BRANCH_NAME AS am_ad_branch_BRANCH_NAME,"
		     		+ "     am_ad_branch.BRANCH_CODE AS am_ad_branch_BRANCH_CODE,"
		     		+ "     am_ad_category.category_name AS am_ad_category_category_name,"
		     		+ "     AM_ASSET_UNCAPITALIZED.Accum_dep AS AM_ASSET_UNCAPITALIZED_Accum_dep,"
		     		+ "     AM_ASSET_UNCAPITALIZED.monthly_dep AS AM_ASSET_UNCAPITALIZED_monthly_dep,"
		     		+ "     AM_ASSET_UNCAPITALIZED.Cost_Price AS AM_ASSET_UNCAPITALIZED_Cost_Price,"
		     		+ "     AM_ASSET_UNCAPITALIZED.NBV AS AM_ASSET_UNCAPITALIZED_NBV,"
		     		+ "     AM_ASSET_UNCAPITALIZED.Date_purchased AS AM_ASSET_UNCAPITALIZED_Date_purchased,"
		     		+ "     am_ad_category.Dep_rate AS am_ad_category_Dep_rate,"
		     		+ "     am_ad_category.Accum_Dep_ledger AS am_ad_category_Accum_Dep_ledger,"
		     		+ "     am_ad_category.Dep_ledger AS am_ad_category_Dep_ledger,"
		     		+ "     am_ad_category.Asset_Ledger AS am_ad_category_Asset_Ledger,"
		     		+ "     am_ad_category.gl_account AS am_ad_category_gl_account,"
		     		+ "     AM_ASSET_UNCAPITALIZED.dep_end_date AS AM_ASSET_UNCAPITALIZED_dep_end_date,"
		     		+ "     AM_ASSET_UNCAPITALIZED.effective_date AS AM_ASSET_UNCAPITALIZED_effective_date,"
		     		+ "     AM_ASSET_UNCAPITALIZED.Posting_Date AS AM_ASSET_UNCAPITALIZED_Posting_Date,"
		     		+ "     am_ad_department.Dept_name AS am_ad_department_Dept_name,"
		     		+ "     AM_ASSET_UNCAPITALIZED.SBU_CODE AS AM_ASSET_UNCAPITALIZED_SBU_CODE,"
		     		+ "	 AM_ASSET_UNCAPITALIZED.BAR_CODE AS AM_ASSET_UNCAPITALIZED_BAR_CODE,"
		     		+ "     AM_ASSET_UNCAPITALIZED.Asset_User AS AM_ASSET_UNCAPITALIZED_Asset_User "
		     		+ "FROM"
		     		+ "     dbo.am_ad_branch am_ad_branch INNER JOIN dbo.AM_ASSET_UNCAPITALIZED AM_ASSET_UNCAPITALIZED ON am_ad_branch.BRANCH_CODE = AM_ASSET_UNCAPITALIZED.BRANCH_CODE"
		     		+ "     INNER JOIN dbo.am_ad_category am_ad_category ON AM_ASSET_UNCAPITALIZED.CATEGORY_CODE = am_ad_category.category_code"
		     		+ "     INNER JOIN dbo.am_ad_department am_ad_department ON AM_ASSET_UNCAPITALIZED.DEPT_CODE = am_ad_department.Dept_code,"
		     		+ "     dbo.am_gb_company am_gb_company "
		     		+ "WHERE "
		     		+ "     am_ad_branch.BRANCH_CODE = AM_ASSET_UNCAPITALIZED.BRANCH_CODE"
		     		+ "     and AM_ASSET_UNCAPITALIZED.Asset_Status = 'ACTIVE'"
		     		+ "     AND am_ad_branch.branch_id = ? "
		     		+ "ORDER BY"
		     		+ "     AM_ASSET_UNCAPITALIZED.BRANCH_CODE ASC,"
		     		+ "     AM_ASSET_UNCAPITALIZED.CATEGORY_CODE ASC";   
			}      

	 if(branch_Id.equals("0")  && !categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")){	   
	   System.out.println("======>>>>>>>Category and Date Selected: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate);
	     ColQuery ="SELECT"
	     		+ "     am_gb_company.company_name AS am_gb_company_company_name,"
	     		+ "     AM_ASSET_UNCAPITALIZED.Old_Asset_id AS AM_ASSET_UNCAPITALIZED_Old_Asset_id,"
	     		+ "     AM_ASSET_UNCAPITALIZED.Asset_id AS AM_ASSET_UNCAPITALIZED_Asset_id,"
	     		+ "     AM_ASSET_UNCAPITALIZED.Description AS AM_ASSET_UNCAPITALIZED_Description,"
	     		+ "     am_ad_branch.BRANCH_NAME AS am_ad_branch_BRANCH_NAME,"
	     		+ "     am_ad_branch.BRANCH_CODE AS am_ad_branch_BRANCH_CODE,"
	     		+ "     am_ad_category.category_name AS am_ad_category_category_name,"
	     		+ "     AM_ASSET_UNCAPITALIZED.Accum_dep AS AM_ASSET_UNCAPITALIZED_Accum_dep,"
	     		+ "     AM_ASSET_UNCAPITALIZED.monthly_dep AS AM_ASSET_UNCAPITALIZED_monthly_dep,"
	     		+ "     AM_ASSET_UNCAPITALIZED.Cost_Price AS AM_ASSET_UNCAPITALIZED_Cost_Price,"
	     		+ "     AM_ASSET_UNCAPITALIZED.NBV AS AM_ASSET_UNCAPITALIZED_NBV,"
	     		+ "     AM_ASSET_UNCAPITALIZED.Date_purchased AS AM_ASSET_UNCAPITALIZED_Date_purchased,"
	     		+ "     am_ad_category.Dep_rate AS am_ad_category_Dep_rate,"
	     		+ "     am_ad_category.Accum_Dep_ledger AS am_ad_category_Accum_Dep_ledger,"
	     		+ "     am_ad_category.Dep_ledger AS am_ad_category_Dep_ledger,"
	     		+ "     am_ad_category.Asset_Ledger AS am_ad_category_Asset_Ledger,"
	     		+ "     am_ad_category.gl_account AS am_ad_category_gl_account,"
	     		+ "     AM_ASSET_UNCAPITALIZED.dep_end_date AS AM_ASSET_UNCAPITALIZED_dep_end_date,"
	     		+ "     AM_ASSET_UNCAPITALIZED.effective_date AS AM_ASSET_UNCAPITALIZED_effective_date,"
	     		+ "     AM_ASSET_UNCAPITALIZED.Posting_Date AS AM_ASSET_UNCAPITALIZED_Posting_Date,"
	     		+ "     am_ad_department.Dept_name AS am_ad_department_Dept_name,"
	     		+ "     AM_ASSET_UNCAPITALIZED.SBU_CODE AS AM_ASSET_UNCAPITALIZED_SBU_CODE,"
	     		+ "	 AM_ASSET_UNCAPITALIZED.BAR_CODE AS AM_ASSET_UNCAPITALIZED_BAR_CODE,"
	     		+ "     AM_ASSET_UNCAPITALIZED.Asset_User AS AM_ASSET_UNCAPITALIZED_Asset_User "
	     		+ "FROM"
	     		+ "     dbo.am_ad_branch am_ad_branch INNER JOIN dbo.AM_ASSET_UNCAPITALIZED AM_ASSET_UNCAPITALIZED ON am_ad_branch.BRANCH_CODE = AM_ASSET_UNCAPITALIZED.BRANCH_CODE"
	     		+ "     INNER JOIN dbo.am_ad_category am_ad_category ON AM_ASSET_UNCAPITALIZED.CATEGORY_CODE = am_ad_category.category_code"
	     		+ "     INNER JOIN dbo.am_ad_department am_ad_department ON AM_ASSET_UNCAPITALIZED.DEPT_CODE = am_ad_department.Dept_code,"
	     		+ "     dbo.am_gb_company am_gb_company "
	     		+ "WHERE "
	     		+ "     am_ad_branch.BRANCH_CODE = AM_ASSET_UNCAPITALIZED.BRANCH_CODE"
	     		+ "     and AM_ASSET_UNCAPITALIZED.Asset_Status = 'ACTIVE'"
	     		+ "     AND am_ad_category.category_id = ?"
	     		+ "	 AND AM_ASSET_UNCAPITALIZED.date_purchased BETWEEN ? AND ? "
	     		+ "ORDER BY"
	     		+ "     AM_ASSET_UNCAPITALIZED.BRANCH_CODE ASC,"
	     		+ "     AM_ASSET_UNCAPITALIZED.CATEGORY_CODE ASC";    
		}      
	 
	 if(branch_Id.equals("0")  && !categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")){	   
		   System.out.println("======>>>>>>>Category Selected: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate);
		   ColQuery ="SELECT"
		     		+ "     am_gb_company.company_name AS am_gb_company_company_name,"
		     		+ "     AM_ASSET_UNCAPITALIZED.Old_Asset_id AS AM_ASSET_UNCAPITALIZED_Old_Asset_id,"
		     		+ "     AM_ASSET_UNCAPITALIZED.Asset_id AS AM_ASSET_UNCAPITALIZED_Asset_id,"
		     		+ "     AM_ASSET_UNCAPITALIZED.Description AS AM_ASSET_UNCAPITALIZED_Description,"
		     		+ "     am_ad_branch.BRANCH_NAME AS am_ad_branch_BRANCH_NAME,"
		     		+ "     am_ad_branch.BRANCH_CODE AS am_ad_branch_BRANCH_CODE,"
		     		+ "     am_ad_category.category_name AS am_ad_category_category_name,"
		     		+ "     AM_ASSET_UNCAPITALIZED.Accum_dep AS AM_ASSET_UNCAPITALIZED_Accum_dep,"
		     		+ "     AM_ASSET_UNCAPITALIZED.monthly_dep AS AM_ASSET_UNCAPITALIZED_monthly_dep,"
		     		+ "     AM_ASSET_UNCAPITALIZED.Cost_Price AS AM_ASSET_UNCAPITALIZED_Cost_Price,"
		     		+ "     AM_ASSET_UNCAPITALIZED.NBV AS AM_ASSET_UNCAPITALIZED_NBV,"
		     		+ "     AM_ASSET_UNCAPITALIZED.Date_purchased AS AM_ASSET_UNCAPITALIZED_Date_purchased,"
		     		+ "     am_ad_category.Dep_rate AS am_ad_category_Dep_rate,"
		     		+ "     am_ad_category.Accum_Dep_ledger AS am_ad_category_Accum_Dep_ledger,"
		     		+ "     am_ad_category.Dep_ledger AS am_ad_category_Dep_ledger,"
		     		+ "     am_ad_category.Asset_Ledger AS am_ad_category_Asset_Ledger,"
		     		+ "     am_ad_category.gl_account AS am_ad_category_gl_account,"
		     		+ "     AM_ASSET_UNCAPITALIZED.dep_end_date AS AM_ASSET_UNCAPITALIZED_dep_end_date,"
		     		+ "     AM_ASSET_UNCAPITALIZED.effective_date AS AM_ASSET_UNCAPITALIZED_effective_date,"
		     		+ "     AM_ASSET_UNCAPITALIZED.Posting_Date AS AM_ASSET_UNCAPITALIZED_Posting_Date,"
		     		+ "     am_ad_department.Dept_name AS am_ad_department_Dept_name,"
		     		+ "     AM_ASSET_UNCAPITALIZED.SBU_CODE AS AM_ASSET_UNCAPITALIZED_SBU_CODE,"
		     		+ "	 AM_ASSET_UNCAPITALIZED.BAR_CODE AS AM_ASSET_UNCAPITALIZED_BAR_CODE,"
		     		+ "     AM_ASSET_UNCAPITALIZED.Asset_User AS AM_ASSET_UNCAPITALIZED_Asset_User "
		     		+ "FROM"
		     		+ "     dbo.am_ad_branch am_ad_branch INNER JOIN dbo.AM_ASSET_UNCAPITALIZED AM_ASSET_UNCAPITALIZED ON am_ad_branch.BRANCH_CODE = AM_ASSET_UNCAPITALIZED.BRANCH_CODE"
		     		+ "     INNER JOIN dbo.am_ad_category am_ad_category ON AM_ASSET_UNCAPITALIZED.CATEGORY_CODE = am_ad_category.category_code"
		     		+ "     INNER JOIN dbo.am_ad_department am_ad_department ON AM_ASSET_UNCAPITALIZED.DEPT_CODE = am_ad_department.Dept_code,"
		     		+ "     dbo.am_gb_company am_gb_company "
		     		+ "WHERE "
		     		+ "     am_ad_branch.BRANCH_CODE = AM_ASSET_UNCAPITALIZED.BRANCH_CODE"
		     		+ "     and AM_ASSET_UNCAPITALIZED.Asset_Status = 'ACTIVE'"
		     		+ "     AND am_ad_category.category_id = ? "
		     		+ "ORDER BY"
		     		+ "     AM_ASSET_UNCAPITALIZED.BRANCH_CODE ASC,"
		     		+ "     AM_ASSET_UNCAPITALIZED.CATEGORY_CODE ASC";   
			}      
	 
   if(!branch_Id.equals("0")  && !categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")){
	   System.out.println("======>>>>>>>Branch and Category and Date Selected: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate);
	     ColQuery ="SELECT"
	     		+ "     am_gb_company.company_name AS am_gb_company_company_name,"
	     		+ "     AM_ASSET_UNCAPITALIZED.Old_Asset_id AS AM_ASSET_UNCAPITALIZED_Old_Asset_id,"
	     		+ "     AM_ASSET_UNCAPITALIZED.Asset_id AS AM_ASSET_UNCAPITALIZED_Asset_id,"
	     		+ "     AM_ASSET_UNCAPITALIZED.Description AS AM_ASSET_UNCAPITALIZED_Description,"
	     		+ "     am_ad_branch.BRANCH_NAME AS am_ad_branch_BRANCH_NAME,"
	     		+ "     am_ad_branch.BRANCH_CODE AS am_ad_branch_BRANCH_CODE,"
	     		+ "     am_ad_category.category_name AS am_ad_category_category_name,"
	     		+ "     AM_ASSET_UNCAPITALIZED.Accum_dep AS AM_ASSET_UNCAPITALIZED_Accum_dep,"
	     		+ "     AM_ASSET_UNCAPITALIZED.monthly_dep AS AM_ASSET_UNCAPITALIZED_monthly_dep,"
	     		+ "     AM_ASSET_UNCAPITALIZED.Cost_Price AS AM_ASSET_UNCAPITALIZED_Cost_Price,"
	     		+ "     AM_ASSET_UNCAPITALIZED.NBV AS AM_ASSET_UNCAPITALIZED_NBV,"
	     		+ "     AM_ASSET_UNCAPITALIZED.Date_purchased AS AM_ASSET_UNCAPITALIZED_Date_purchased,"
	     		+ "     am_ad_category.Dep_rate AS am_ad_category_Dep_rate,"
	     		+ "     am_ad_category.Accum_Dep_ledger AS am_ad_category_Accum_Dep_ledger,"
	     		+ "     am_ad_category.Dep_ledger AS am_ad_category_Dep_ledger,"
	     		+ "     am_ad_category.Asset_Ledger AS am_ad_category_Asset_Ledger,"
	     		+ "     am_ad_category.gl_account AS am_ad_category_gl_account,"
	     		+ "     AM_ASSET_UNCAPITALIZED.dep_end_date AS AM_ASSET_UNCAPITALIZED_dep_end_date,"
	     		+ "     AM_ASSET_UNCAPITALIZED.effective_date AS AM_ASSET_UNCAPITALIZED_effective_date,"
	     		+ "     AM_ASSET_UNCAPITALIZED.Posting_Date AS AM_ASSET_UNCAPITALIZED_Posting_Date,"
	     		+ "     am_ad_department.Dept_name AS am_ad_department_Dept_name,"
	     		+ "     AM_ASSET_UNCAPITALIZED.SBU_CODE AS AM_ASSET_UNCAPITALIZED_SBU_CODE,"
	     		+ "	 AM_ASSET_UNCAPITALIZED.BAR_CODE AS AM_ASSET_UNCAPITALIZED_BAR_CODE,"
	     		+ "     AM_ASSET_UNCAPITALIZED.Asset_User AS AM_ASSET_UNCAPITALIZED_Asset_User "
	     		+ "FROM"
	     		+ "     dbo.am_ad_branch am_ad_branch INNER JOIN dbo.AM_ASSET_UNCAPITALIZED AM_ASSET_UNCAPITALIZED ON am_ad_branch.BRANCH_CODE = AM_ASSET_UNCAPITALIZED.BRANCH_CODE"
	     		+ "     INNER JOIN dbo.am_ad_category am_ad_category ON AM_ASSET_UNCAPITALIZED.CATEGORY_CODE = am_ad_category.category_code"
	     		+ "     INNER JOIN dbo.am_ad_department am_ad_department ON AM_ASSET_UNCAPITALIZED.DEPT_CODE = am_ad_department.Dept_code,"
	     		+ "     dbo.am_gb_company am_gb_company "
	     		+ "WHERE "
	     		+ "     am_ad_branch.BRANCH_CODE = AM_ASSET_UNCAPITALIZED.BRANCH_CODE"
	     		+ "     And AM_ASSET_UNCAPITALIZED.Asset_Status = 'ACTIVE'"
	     		+ "	  AND am_ad_category.category_id = ? "
	     		+ "	 AND am_ad_branch.branch_id = ?"
	     		+ "ORDER BY"
	     		+ "     AM_ASSET_UNCAPITALIZED.BRANCH_CODE ASC,"
	     		+ "     AM_ASSET_UNCAPITALIZED.CATEGORY_CODE ASC";    
		} 
   
   if(!branch_Id.equals("0")  && !categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")){
	   System.out.println("======>>>>>>>Branch and Category and Date Selected: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate);
	     ColQuery ="SELECT"
	     		+ "     am_gb_company.company_name AS am_gb_company_company_name,"
	     		+ "     AM_ASSET_UNCAPITALIZED.Old_Asset_id AS AM_ASSET_UNCAPITALIZED_Old_Asset_id,"
	     		+ "     AM_ASSET_UNCAPITALIZED.Asset_id AS AM_ASSET_UNCAPITALIZED_Asset_id,"
	     		+ "     AM_ASSET_UNCAPITALIZED.Description AS AM_ASSET_UNCAPITALIZED_Description,"
	     		+ "     am_ad_branch.BRANCH_NAME AS am_ad_branch_BRANCH_NAME,"
	     		+ "     am_ad_branch.BRANCH_CODE AS am_ad_branch_BRANCH_CODE,"
	     		+ "     am_ad_category.category_name AS am_ad_category_category_name,"
	     		+ "     AM_ASSET_UNCAPITALIZED.Accum_dep AS AM_ASSET_UNCAPITALIZED_Accum_dep,"
	     		+ "     AM_ASSET_UNCAPITALIZED.monthly_dep AS AM_ASSET_UNCAPITALIZED_monthly_dep,"
	     		+ "     AM_ASSET_UNCAPITALIZED.Cost_Price AS AM_ASSET_UNCAPITALIZED_Cost_Price,"
	     		+ "     AM_ASSET_UNCAPITALIZED.NBV AS AM_ASSET_UNCAPITALIZED_NBV,"
	     		+ "     AM_ASSET_UNCAPITALIZED.Date_purchased AS AM_ASSET_UNCAPITALIZED_Date_purchased,"
	     		+ "     am_ad_category.Dep_rate AS am_ad_category_Dep_rate,"
	     		+ "     am_ad_category.Accum_Dep_ledger AS am_ad_category_Accum_Dep_ledger,"
	     		+ "     am_ad_category.Dep_ledger AS am_ad_category_Dep_ledger,"
	     		+ "     am_ad_category.Asset_Ledger AS am_ad_category_Asset_Ledger,"
	     		+ "     am_ad_category.gl_account AS am_ad_category_gl_account,"
	     		+ "     AM_ASSET_UNCAPITALIZED.dep_end_date AS AM_ASSET_UNCAPITALIZED_dep_end_date,"
	     		+ "     AM_ASSET_UNCAPITALIZED.effective_date AS AM_ASSET_UNCAPITALIZED_effective_date,"
	     		+ "     AM_ASSET_UNCAPITALIZED.Posting_Date AS AM_ASSET_UNCAPITALIZED_Posting_Date,"
	     		+ "     am_ad_department.Dept_name AS am_ad_department_Dept_name,"
	     		+ "     AM_ASSET_UNCAPITALIZED.SBU_CODE AS AM_ASSET_UNCAPITALIZED_SBU_CODE,"
	     		+ "	 AM_ASSET_UNCAPITALIZED.BAR_CODE AS AM_ASSET_UNCAPITALIZED_BAR_CODE,"
	     		+ "     AM_ASSET_UNCAPITALIZED.Asset_User AS AM_ASSET_UNCAPITALIZED_Asset_User "
	     		+ "FROM"
	     		+ "     dbo.am_ad_branch am_ad_branch INNER JOIN dbo.AM_ASSET_UNCAPITALIZED AM_ASSET_UNCAPITALIZED ON am_ad_branch.BRANCH_CODE = AM_ASSET_UNCAPITALIZED.BRANCH_CODE"
	     		+ "     INNER JOIN dbo.am_ad_category am_ad_category ON AM_ASSET_UNCAPITALIZED.CATEGORY_CODE = am_ad_category.category_code"
	     		+ "     INNER JOIN dbo.am_ad_department am_ad_department ON AM_ASSET_UNCAPITALIZED.DEPT_CODE = am_ad_department.Dept_code,"
	     		+ "     dbo.am_gb_company am_gb_company "
	     		+ "WHERE "
	     		+ "     am_ad_branch.BRANCH_CODE = AM_ASSET_UNCAPITALIZED.BRANCH_CODE"
	     		+ "     And AM_ASSET_UNCAPITALIZED.Asset_Status = 'ACTIVE'"
	     		+ "	  AND am_ad_category.category_id = ?"
	     		+ "	 AND am_ad_branch.branch_id = ?"
	     		+ "	 AND AM_ASSET_UNCAPITALIZED.date_purchased BETWEEN ? AND ? "
	     		+ "ORDER BY"
	     		+ "     AM_ASSET_UNCAPITALIZED.BRANCH_CODE ASC,"
	     		+ "     AM_ASSET_UNCAPITALIZED.CATEGORY_CODE ASC";    
		}      
//   System.out.println("======>>>>>>>ColQuery: "+ColQuery);
     java.util.ArrayList list =rep.getUncapitalisedAsseRecords(ColQuery,branch_Id,categoryCode,FromDate,ToDate,asset_Id);
     System.out.println("======>>>>>>>list size: "+list.size()+"        =====report: "+report);
     if(list.size()!=0){
   	 if(report.equalsIgnoreCase("rptMenuBCRU")){
    	 SXSSFWorkbook workbook = new SXSSFWorkbook();
         Sheet sheet = workbook.createSheet("Demo");
         Row rowhead = sheet.createRow((int) 0);
         
         rowhead.createCell( 0).setCellValue("S/No.");
         rowhead.createCell( 1).setCellValue("Company Name");
         rowhead.createCell( 2).setCellValue("Old Asset Id");
         rowhead.createCell( 3).setCellValue("Asset Id");
         rowhead.createCell( 4).setCellValue("Description");
         rowhead.createCell( 5).setCellValue("Branch Name");
         rowhead.createCell( 6).setCellValue("Branch Code");
         rowhead.createCell( 7).setCellValue("Category Name");
         rowhead.createCell( 8).setCellValue("Accum Dep.");
         rowhead.createCell( 9).setCellValue("Monthly Dep.");
         rowhead.createCell( 10).setCellValue("Cost Price");
         rowhead.createCell( 11).setCellValue("NBV");
		 rowhead.createCell( 12).setCellValue("Date Purchased");
		 rowhead.createCell( 13).setCellValue("Depreciation Rate");
		 rowhead.createCell( 14).setCellValue("Accum Dep Ledger");
		 rowhead.createCell( 15).setCellValue("Dep Ledger");
		 rowhead.createCell( 16).setCellValue("Asset ledger");
		 rowhead.createCell( 17).setCellValue("GL Account");
		 rowhead.createCell( 18).setCellValue("Depreciation End Date");
		 rowhead.createCell( 19).setCellValue("Effective Date");
		 rowhead.createCell( 20).setCellValue("Posting Date");
         rowhead.createCell( 21).setCellValue("Department Name");
         rowhead.createCell( 22).setCellValue("SBU Code");
         rowhead.createCell( 23).setCellValue("Bar Code");
         rowhead.createCell( 24).setCellValue("Asset User");
         
         

     int i = 1;
     System.out.println("<<<<<<list.size(): "+list.size());
	 for(int k=0;k<list.size();k++)
     {
    	 com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);    	 
    	 
			String companyName = newassettrans.getOldBranchId();
			String oldAssetId = newassettrans.getOldassetId();
			String assetId = newassettrans.getAssetId();
			String description = newassettrans.getDescription();
			String branchName = newassettrans.getBranchName();
			String branch_code = newassettrans.getBranchCode();
			String categoryName = newassettrans.getCategoryName();
			double accumDepr = newassettrans.getAccumDep();
			double monthlyDepr = newassettrans.getMonthlyDep();
			double costPrice = newassettrans.getCostPrice();
			double nbv = newassettrans.getNbv();
			String datePurchased = newassettrans.getDatepurchased();
			double depRate = newassettrans.getDepRate();
			String accumDepLedger = newassettrans.getAccumDepLedger();
			String depLedger = newassettrans.getDepLedger();
			String assetLedger = newassettrans.getAssetLedger();  
			String gl_Account = newassettrans.getGlAccount(); 
			String depreciationEndDate = newassettrans.getDependDate();
			String effectiveDate = newassettrans.getEffectiveDate();
			String postingDate = newassettrans.getPostingDate();
			String deptName = newassettrans.getDeptName();
			String sbuCode = newassettrans.getSbuCode();
			String barCode = newassettrans.getBarCode();
			String assetUser = newassettrans.getAssetUser();

			//			String vendorName = records.getCodeName("select VENDOR_NAME from am_ad_vendor where VENDOR_ID = "+vendorId+"");
			

			Row row = sheet.createRow((int) i);

			row.createCell((short) 0).setCellValue(i);
			row.createCell((short) 1).setCellValue(companyName);
			row.createCell((short) 2).setCellValue(oldAssetId);
            row.createCell((short) 3).setCellValue(assetId);
            row.createCell((short) 4).setCellValue(description);
            row.createCell((short) 5).setCellValue(branchName);
            row.createCell((short) 6).setCellValue(branch_code);
            row.createCell((short) 7).setCellValue(categoryName);
            row.createCell((short) 8).setCellValue(accumDepr);
            row.createCell((short) 9).setCellValue(monthlyDepr);
            row.createCell((short) 10).setCellValue(costPrice);
            row.createCell((short) 11).setCellValue(nbv);
			row.createCell((short) 12).setCellValue(datePurchased);
			row.createCell((short) 13).setCellValue(depRate);
			row.createCell((short) 14).setCellValue(accumDepLedger);
			row.createCell((short) 15).setCellValue(depLedger);
			row.createCell((short) 16).setCellValue(assetLedger);
			row.createCell((short) 17).setCellValue(gl_Account);
			row.createCell((short) 18).setCellValue(depreciationEndDate);
            row.createCell((short) 19).setCellValue(effectiveDate);
            row.createCell((short) 20).setCellValue(postingDate);
            row.createCell((short) 21).setCellValue(deptName);
            row.createCell((short) 22).setCellValue(sbuCode);
            row.createCell((short) 23).setCellValue(barCode);
            row.createCell((short) 24).setCellValue(assetUser);
          

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