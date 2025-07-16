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
   
public class AssetReclassificationReportExport extends HttpServlet
{
	private EmailSmsServiceBus mail;
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
    if(report.equalsIgnoreCase("rptMenuBCCLAS")){fileName = branchCode+"By"+userName+"AssetReclassificationReport.xlsx";}
    
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
     if(branch_Id.equals("0")  && categoryCode.equals("0")  && FromDate.equals("") && ToDate.equals("")){
  	   System.out.println("======>>>>>>>No Selection: ");
  	     ColQuery ="SELECT\n"
  	     		+ "am_gb_company.company_name AS am_gb_company_company_name,\n"
  	     		+ "am_ad_branch.BRANCH_NAME AS am_ad_branch_BRANCH_NAME,\n"
  	     		+ "am_ad_category.category_name AS am_ad_category_category_name,\n"
  	     		+ "am_ad_category.Dep_rate AS am_ad_category_Dep_rate,\n"
  	     		+ "am_ad_category.Accum_Dep_ledger AS am_ad_category_Accum_Dep_ledger,\n"
  	     		+ "am_ad_category.Dep_ledger AS am_ad_category_Dep_ledger,\n"
  	     		+ "am_ad_category.Asset_Ledger AS am_ad_category_Asset_Ledger,\n"
  	     		+ "am_ad_category.gl_account AS am_ad_category_gl_account,\n"
  	     		+ "am_assetReclassification.asset_id AS am_assetReclassification_asset_id,\n"
  	     		+ "am_assetReclassification.old_category_id AS am_assetReclassification_old_category_id,\n"
  	     		+ "am_assetReclassification.new_category_id AS am_assetReclassification_new_category_id,\n"
  	     		+ "am_assetReclassification.old_depr_rate AS am_assetReclassification_old_depr_rate,\n"
  	     		+ "am_assetReclassification.reclassify_reason AS am_assetReclassification_reclassify_reason,\n"
  	     		+ "am_assetReclassification.reclassify_date AS am_assetReclassification_reclassify_date,\n"
  	     		+ "am_assetReclassification.old_accum_dep AS am_assetReclassification_old_accum_dep,\n"
  	     		+ "am_assetReclassification.new_depr_rate AS am_assetReclassification_new_depr_rate,\n"
  	     		+ "am_assetReclassification.new_accum_dep AS am_assetReclassification_new_accum_dep,\n"
  	     		+ "am_assetReclassification.recalc_depr AS am_assetReclassification_recalc_depr,\n"
  	     		+ "am_assetReclassification.raise_entry AS am_assetReclassification_raise_entry,\n"
  	     		+ "am_assetReclassification.recalc_difference AS am_assetReclassification_recalc_difference,\n"
  	     		+ "am_assetReclassification.cost_price AS am_assetReclassification_cost_price,\n"
  	     		+ "am_assetReclassification.User_ID AS am_assetReclassification_User_ID,\n"
  	     		+ "am_assetReclassification.old_dep_ytd AS am_assetReclassification_old_dep_ytd,\n"
  	     		+ "am_assetReclassification.old_category_code AS am_assetReclassification_old_category_code,\n"
  	     		+ "am_assetReclassification.old_branch_code AS am_assetReclassification_old_branch_code,\n"
  	     		+ "am_assetReclassification.description AS am_assetReclassification_description,\n"
  	     		+ "am_assetReclassification.New_asset_id AS am_assetReclassification_New_asset_id,\n"
  	     		+ "am_assetReclassification.Reclassify_ID AS am_assetReclassification_Reclassify_ID,\n"
  	     		+ "am_assetReclassification.Monthly_Dep AS am_assetReclassification_Monthly_Dep,\n"
  	     		+ "am_assetReclassification.nbv AS am_assetReclassification_nbv\n"
  	     		+ "FROM\n"
  	     		+ "dbo.am_ad_branch am_ad_branch INNER JOIN dbo.am_assetReclassification am_assetReclassification ON am_ad_branch.BRANCH_CODE = am_assetReclassification.old_branch_code "
  	     		+ "INNER JOIN dbo.am_ad_category am_ad_category ON am_assetReclassification.new_category_id = am_ad_category.category_ID, "
  	     		+ "dbo.am_gb_company am_gb_company\n"
  	     		+ "WHERE\n"
  	     		+ " am_assetReclassification.User_ID <> 0 \n"
  	     		+ " and am_assetReclassification.status='ACTIVE' \n"
  	     		+ "	 \n"
  	     		+ "ORDER BY\n"
  	     		+ "am_assetReclassification.old_branch_code ASC,\n"
  	     		+ "am_assetReclassification.old_category_code ASC\n"
  	     		+ "";    
  		}
     
     
     if(branch_Id.equals("0")  && categoryCode.equals("0")  && !FromDate.equals("") && !ToDate.equals("")){
    	 System.out.println("======>>>>>>>Date Selected: ");	     
	     ColQuery ="SELECT\n"
	     		+ "am_gb_company.company_name AS am_gb_company_company_name,\n"
	     		+ "am_ad_branch.BRANCH_NAME AS am_ad_branch_BRANCH_NAME,\n"
	     		+ "am_ad_category.category_name AS am_ad_category_category_name,\n"
	     		+ "am_ad_category.Dep_rate AS am_ad_category_Dep_rate,\n"
	     		+ "am_ad_category.Accum_Dep_ledger AS am_ad_category_Accum_Dep_ledger,\n"
	     		+ "am_ad_category.Dep_ledger AS am_ad_category_Dep_ledger,\n"
	     		+ "am_ad_category.Asset_Ledger AS am_ad_category_Asset_Ledger,\n"
	     		+ "am_ad_category.gl_account AS am_ad_category_gl_account,\n"
	     		+ "am_assetReclassification.asset_id AS am_assetReclassification_asset_id,\n"
	     		+ "am_assetReclassification.old_category_id AS am_assetReclassification_old_category_id,\n"
	     		+ "am_assetReclassification.new_category_id AS am_assetReclassification_new_category_id,\n"
	     		+ "am_assetReclassification.old_depr_rate AS am_assetReclassification_old_depr_rate,\n"
	     		+ "am_assetReclassification.reclassify_reason AS am_assetReclassification_reclassify_reason,\n"
	     		+ "am_assetReclassification.reclassify_date AS am_assetReclassification_reclassify_date,\n"
	     		+ "am_assetReclassification.old_accum_dep AS am_assetReclassification_old_accum_dep,\n"
	     		+ "am_assetReclassification.new_depr_rate AS am_assetReclassification_new_depr_rate,\n"
	     		+ "am_assetReclassification.new_accum_dep AS am_assetReclassification_new_accum_dep,\n"
	     		+ "am_assetReclassification.recalc_depr AS am_assetReclassification_recalc_depr,\n"
	     		+ "am_assetReclassification.raise_entry AS am_assetReclassification_raise_entry,\n"
	     		+ "am_assetReclassification.recalc_difference AS am_assetReclassification_recalc_difference,\n"
	     		+ "am_assetReclassification.cost_price AS am_assetReclassification_cost_price,\n"
	     		+ "am_assetReclassification.User_ID AS am_assetReclassification_User_ID,\n"
	     		+ "am_assetReclassification.old_dep_ytd AS am_assetReclassification_old_dep_ytd,\n"
	     		+ "am_assetReclassification.old_category_code AS am_assetReclassification_old_category_code,\n"
	     		+ "am_assetReclassification.old_branch_code AS am_assetReclassification_old_branch_code,\n"
	     		+ "am_assetReclassification.description AS am_assetReclassification_description,\n"
	     		+ "am_assetReclassification.New_asset_id AS am_assetReclassification_New_asset_id,\n"
	     		+ "am_assetReclassification.Reclassify_ID AS am_assetReclassification_Reclassify_ID,\n"
	     		+ "am_assetReclassification.Monthly_Dep AS am_assetReclassification_Monthly_Dep,\n"
	     		+ "am_assetReclassification.nbv AS am_assetReclassification_nbv\n"
	     		+ "FROM\n"
	     		+ "dbo.am_ad_branch am_ad_branch INNER JOIN dbo.am_assetReclassification am_assetReclassification ON am_ad_branch.BRANCH_CODE = am_assetReclassification.old_branch_code\n"
	     		+ "INNER JOIN dbo.am_ad_category am_ad_category ON am_assetReclassification.new_category_id = am_ad_category.category_ID,\n"
	     		+ "dbo.am_gb_company am_gb_company\n"
	     		+ "WHERE\n"
	     		+ " am_assetReclassification.User_ID <> 0 \n"
	     		+ " and am_assetReclassification.status='ACTIVE' \n"
	     		+ " and reclassify_date BETWEEN ? and ?\n"
	     		+ "	 \n"
	     		+ "ORDER BY\n"
	     		+ "am_assetReclassification.old_branch_code ASC,\n"
	     		+ "am_assetReclassification.old_category_code ASC";  
	}      
	 if(!branch_Id.equals("0")  && categoryCode.equals("0")  && !FromDate.equals("") && !ToDate.equals("")){	   
	   System.out.println("======>>>>>>>Branch and Date Selected: ");
	     ColQuery ="SELECT\n"
	     		+ "am_gb_company.company_name AS am_gb_company_company_name,\n"
	     		+ "am_ad_branch.BRANCH_NAME AS am_ad_branch_BRANCH_NAME,\n"
	     		+ "am_ad_category.category_name AS am_ad_category_category_name,\n"
	     		+ "am_ad_category.Dep_rate AS am_ad_category_Dep_rate,\n"
	     		+ "am_ad_category.Accum_Dep_ledger AS am_ad_category_Accum_Dep_ledger,\n"
	     		+ "am_ad_category.Dep_ledger AS am_ad_category_Dep_ledger,\n"
	     		+ "am_ad_category.Asset_Ledger AS am_ad_category_Asset_Ledger,\n"
	     		+ "am_ad_category.gl_account AS am_ad_category_gl_account,\n"
	     		+ "am_assetReclassification.asset_id AS am_assetReclassification_asset_id,\n"
	     		+ "am_assetReclassification.old_category_id AS am_assetReclassification_old_category_id,\n"
	     		+ "am_assetReclassification.new_category_id AS am_assetReclassification_new_category_id,\n"
	     		+ "am_assetReclassification.old_depr_rate AS am_assetReclassification_old_depr_rate,\n"
	     		+ "am_assetReclassification.reclassify_reason AS am_assetReclassification_reclassify_reason,\n"
	     		+ "am_assetReclassification.reclassify_date AS am_assetReclassification_reclassify_date,\n"
	     		+ "am_assetReclassification.old_accum_dep AS am_assetReclassification_old_accum_dep,\n"
	     		+ "am_assetReclassification.new_depr_rate AS am_assetReclassification_new_depr_rate,\n"
	     		+ "am_assetReclassification.new_accum_dep AS am_assetReclassification_new_accum_dep,\n"
	     		+ "am_assetReclassification.recalc_depr AS am_assetReclassification_recalc_depr,\n"
	     		+ "am_assetReclassification.raise_entry AS am_assetReclassification_raise_entry,\n"
	     		+ "am_assetReclassification.recalc_difference AS am_assetReclassification_recalc_difference,\n"
	     		+ "am_assetReclassification.cost_price AS am_assetReclassification_cost_price,\n"
	     		+ "am_assetReclassification.User_ID AS am_assetReclassification_User_ID,\n"
	     		+ "am_assetReclassification.old_dep_ytd AS am_assetReclassification_old_dep_ytd,\n"
	     		+ "am_assetReclassification.old_category_code AS am_assetReclassification_old_category_code,\n"
	     		+ "am_assetReclassification.old_branch_code AS am_assetReclassification_old_branch_code,\n"
	     		+ "am_assetReclassification.description AS am_assetReclassification_description,\n"
	     		+ "am_assetReclassification.New_asset_id AS am_assetReclassification_New_asset_id,\n"
	     		+ "am_assetReclassification.Reclassify_ID AS am_assetReclassification_Reclassify_ID,\n"
	     		+ "am_assetReclassification.Monthly_Dep AS am_assetReclassification_Monthly_Dep,\n"
	     		+ "am_assetReclassification.nbv AS am_assetReclassification_nbv\n"
	     		+ "FROM\n"
	     		+ "dbo.am_ad_branch am_ad_branch INNER JOIN dbo.am_assetReclassification am_assetReclassification ON am_ad_branch.BRANCH_CODE = am_assetReclassification.old_branch_code\n"
	     		+ "INNER JOIN dbo.am_ad_category am_ad_category ON am_assetReclassification.new_category_id = am_ad_category.category_ID,\n"
	     		+ "dbo.am_gb_company am_gb_company\n"
	     		+ "WHERE\n"
	     		+ " am_assetReclassification.User_ID <> 0 \n"
	     		+ " and am_assetReclassification.status='ACTIVE' \n"
	     		+ " and am_ad_branch.branch_id = ?\n"
	     		+ " and reclassify_date BETWEEN ? and ?\n"
	     		+ "	 \n"
	     		+ "ORDER BY\n"
	     		+ "am_assetReclassification.old_branch_code ASC,\n"
	     		+ "am_assetReclassification.old_category_code ASC";   
		}  
	 
	 if(!branch_Id.equals("0")  && categoryCode.equals("0")  && FromDate.equals("") && ToDate.equals("")){	   
		   System.out.println("======>>>>>>>Branch Selected: ");
		     ColQuery ="SELECT\n"
		     		+ "am_gb_company.company_name AS am_gb_company_company_name,\n"
		     		+ "am_ad_branch.BRANCH_NAME AS am_ad_branch_BRANCH_NAME,\n"
		     		+ "am_ad_category.category_name AS am_ad_category_category_name,\n"
		     		+ "am_ad_category.Dep_rate AS am_ad_category_Dep_rate,\n"
		     		+ "am_ad_category.Accum_Dep_ledger AS am_ad_category_Accum_Dep_ledger,\n"
		     		+ "am_ad_category.Dep_ledger AS am_ad_category_Dep_ledger,\n"
		     		+ "am_ad_category.Asset_Ledger AS am_ad_category_Asset_Ledger,\n"
		     		+ "am_ad_category.gl_account AS am_ad_category_gl_account,\n"
		     		+ "am_assetReclassification.asset_id AS am_assetReclassification_asset_id,\n"
		     		+ "am_assetReclassification.old_category_id AS am_assetReclassification_old_category_id,\n"
		     		+ "am_assetReclassification.new_category_id AS am_assetReclassification_new_category_id,\n"
		     		+ "am_assetReclassification.old_depr_rate AS am_assetReclassification_old_depr_rate,\n"
		     		+ "am_assetReclassification.reclassify_reason AS am_assetReclassification_reclassify_reason,\n"
		     		+ "am_assetReclassification.reclassify_date AS am_assetReclassification_reclassify_date,\n"
		     		+ "am_assetReclassification.old_accum_dep AS am_assetReclassification_old_accum_dep,\n"
		     		+ "am_assetReclassification.new_depr_rate AS am_assetReclassification_new_depr_rate,\n"
		     		+ "am_assetReclassification.new_accum_dep AS am_assetReclassification_new_accum_dep,\n"
		     		+ "am_assetReclassification.recalc_depr AS am_assetReclassification_recalc_depr,\n"
		     		+ "am_assetReclassification.raise_entry AS am_assetReclassification_raise_entry,\n"
		     		+ "am_assetReclassification.recalc_difference AS am_assetReclassification_recalc_difference,\n"
		     		+ "am_assetReclassification.cost_price AS am_assetReclassification_cost_price,\n"
		     		+ "am_assetReclassification.User_ID AS am_assetReclassification_User_ID,\n"
		     		+ "am_assetReclassification.old_dep_ytd AS am_assetReclassification_old_dep_ytd,\n"
		     		+ "am_assetReclassification.old_category_code AS am_assetReclassification_old_category_code,\n"
		     		+ "am_assetReclassification.old_branch_code AS am_assetReclassification_old_branch_code,\n"
		     		+ "am_assetReclassification.description AS am_assetReclassification_description,\n"
		     		+ "am_assetReclassification.New_asset_id AS am_assetReclassification_New_asset_id,\n"
		     		+ "am_assetReclassification.Reclassify_ID AS am_assetReclassification_Reclassify_ID,\n"
		     		+ "am_assetReclassification.Monthly_Dep AS am_assetReclassification_Monthly_Dep,\n"
		     		+ "am_assetReclassification.nbv AS am_assetReclassification_nbv\n"
		     		+ "FROM\n"
		     		+ "dbo.am_ad_branch am_ad_branch INNER JOIN dbo.am_assetReclassification am_assetReclassification ON am_ad_branch.BRANCH_CODE = am_assetReclassification.old_branch_code\n"
		     		+ "INNER JOIN dbo.am_ad_category am_ad_category ON am_assetReclassification.new_category_id = am_ad_category.category_ID,\n"
		     		+ "dbo.am_gb_company am_gb_company\n"
		     		+ "WHERE\n"
		     		+ " am_assetReclassification.User_ID <> 0 \n"
		     		+ " and am_assetReclassification.status='ACTIVE' \n"
		     		+ " and am_ad_branch.branch_id = ? "
		     		+ "	 \n"
		     		+ "ORDER BY\n"
		     		+ "am_assetReclassification.old_branch_code ASC,\n"
		     		+ "am_assetReclassification.old_category_code ASC";   
			}      

	 if(branch_Id.equals("0")  && !categoryCode.equals("0")  && !FromDate.equals("") && !ToDate.equals("")){	   
	   System.out.println("======>>>>>>>Category and Date Selected: ");
	     ColQuery ="SELECT\n"
	     		+ "am_gb_company.company_name AS am_gb_company_company_name,\n"
	     		+ "am_ad_branch.BRANCH_NAME AS am_ad_branch_BRANCH_NAME,\n"
	     		+ "am_ad_category.category_name AS am_ad_category_category_name,\n"
	     		+ "am_ad_category.Dep_rate AS am_ad_category_Dep_rate,\n"
	     		+ "am_ad_category.Accum_Dep_ledger AS am_ad_category_Accum_Dep_ledger,\n"
	     		+ "am_ad_category.Dep_ledger AS am_ad_category_Dep_ledger,\n"
	     		+ "am_ad_category.Asset_Ledger AS am_ad_category_Asset_Ledger,\n"
	     		+ "am_ad_category.gl_account AS am_ad_category_gl_account,\n"
	     		+ "am_assetReclassification.asset_id AS am_assetReclassification_asset_id,\n"
	     		+ "am_assetReclassification.old_category_id AS am_assetReclassification_old_category_id,\n"
	     		+ "am_assetReclassification.new_category_id AS am_assetReclassification_new_category_id,\n"
	     		+ "am_assetReclassification.old_depr_rate AS am_assetReclassification_old_depr_rate,\n"
	     		+ "am_assetReclassification.reclassify_reason AS am_assetReclassification_reclassify_reason,\n"
	     		+ "am_assetReclassification.reclassify_date AS am_assetReclassification_reclassify_date,\n"
	     		+ "am_assetReclassification.old_accum_dep AS am_assetReclassification_old_accum_dep,\n"
	     		+ "am_assetReclassification.new_depr_rate AS am_assetReclassification_new_depr_rate,\n"
	     		+ "am_assetReclassification.new_accum_dep AS am_assetReclassification_new_accum_dep,\n"
	     		+ "am_assetReclassification.recalc_depr AS am_assetReclassification_recalc_depr,\n"
	     		+ "am_assetReclassification.raise_entry AS am_assetReclassification_raise_entry,\n"
	     		+ "am_assetReclassification.recalc_difference AS am_assetReclassification_recalc_difference,\n"
	     		+ "am_assetReclassification.cost_price AS am_assetReclassification_cost_price,\n"
	     		+ "am_assetReclassification.User_ID AS am_assetReclassification_User_ID,\n"
	     		+ "am_assetReclassification.old_dep_ytd AS am_assetReclassification_old_dep_ytd,\n"
	     		+ "am_assetReclassification.old_category_code AS am_assetReclassification_old_category_code,\n"
	     		+ "am_assetReclassification.old_branch_code AS am_assetReclassification_old_branch_code,\n"
	     		+ "am_assetReclassification.description AS am_assetReclassification_description,\n"
	     		+ "am_assetReclassification.New_asset_id AS am_assetReclassification_New_asset_id,\n"
	     		+ "am_assetReclassification.Reclassify_ID AS am_assetReclassification_Reclassify_ID,\n"
	     		+ "am_assetReclassification.Monthly_Dep AS am_assetReclassification_Monthly_Dep,\n"
	     		+ "am_assetReclassification.nbv AS am_assetReclassification_nbv\n"
	     		+ "FROM\n"
	     		+ "dbo.am_ad_branch am_ad_branch INNER JOIN dbo.am_assetReclassification am_assetReclassification ON am_ad_branch.BRANCH_CODE = am_assetReclassification.old_branch_code\n"
	     		+ "INNER JOIN dbo.am_ad_category am_ad_category ON am_assetReclassification.new_category_id = am_ad_category.category_ID,\n"
	     		+ "dbo.am_gb_company am_gb_company\n"
	     		+ "WHERE\n"
	     		+ " am_assetReclassification.User_ID <> 0 \n"
	     		+ " and am_assetReclassification.status='ACTIVE' \n"
	     		+ " and am_ad_category.category_id = ?\n"
	     		+ " and reclassify_date BETWEEN ? and ?\n"
	     		+ "	 \n"
	     		+ "ORDER BY\n"
	     		+ "am_assetReclassification.old_branch_code ASC,\n"
	     		+ "am_assetReclassification.old_category_code ASC";    
		}      
	 
	 if(branch_Id.equals("0")  && !categoryCode.equals("0")  && FromDate.equals("") && ToDate.equals("")){	   
		   System.out.println("======>>>>>>>Category Selected: ");
		     ColQuery ="SELECT\n"
		     		+ "am_gb_company.company_name AS am_gb_company_company_name,\n"
		     		+ "am_ad_branch.BRANCH_NAME AS am_ad_branch_BRANCH_NAME,\n"
		     		+ "am_ad_category.category_name AS am_ad_category_category_name,\n"
		     		+ "am_ad_category.Dep_rate AS am_ad_category_Dep_rate,\n"
		     		+ "am_ad_category.Accum_Dep_ledger AS am_ad_category_Accum_Dep_ledger,\n"
		     		+ "am_ad_category.Dep_ledger AS am_ad_category_Dep_ledger,\n"
		     		+ "am_ad_category.Asset_Ledger AS am_ad_category_Asset_Ledger,\n"
		     		+ "am_ad_category.gl_account AS am_ad_category_gl_account,\n"
		     		+ "am_assetReclassification.asset_id AS am_assetReclassification_asset_id,\n"
		     		+ "am_assetReclassification.old_category_id AS am_assetReclassification_old_category_id,\n"
		     		+ "am_assetReclassification.new_category_id AS am_assetReclassification_new_category_id,\n"
		     		+ "am_assetReclassification.old_depr_rate AS am_assetReclassification_old_depr_rate,\n"
		     		+ "am_assetReclassification.reclassify_reason AS am_assetReclassification_reclassify_reason,\n"
		     		+ "am_assetReclassification.reclassify_date AS am_assetReclassification_reclassify_date,\n"
		     		+ "am_assetReclassification.old_accum_dep AS am_assetReclassification_old_accum_dep,\n"
		     		+ "am_assetReclassification.new_depr_rate AS am_assetReclassification_new_depr_rate,\n"
		     		+ "am_assetReclassification.new_accum_dep AS am_assetReclassification_new_accum_dep,\n"
		     		+ "am_assetReclassification.recalc_depr AS am_assetReclassification_recalc_depr,\n"
		     		+ "am_assetReclassification.raise_entry AS am_assetReclassification_raise_entry,\n"
		     		+ "am_assetReclassification.recalc_difference AS am_assetReclassification_recalc_difference,\n"
		     		+ "am_assetReclassification.cost_price AS am_assetReclassification_cost_price,\n"
		     		+ "am_assetReclassification.User_ID AS am_assetReclassification_User_ID,\n"
		     		+ "am_assetReclassification.old_dep_ytd AS am_assetReclassification_old_dep_ytd,\n"
		     		+ "am_assetReclassification.old_category_code AS am_assetReclassification_old_category_code,\n"
		     		+ "am_assetReclassification.old_branch_code AS am_assetReclassification_old_branch_code,\n"
		     		+ "am_assetReclassification.description AS am_assetReclassification_description,\n"
		     		+ "am_assetReclassification.New_asset_id AS am_assetReclassification_New_asset_id,\n"
		     		+ "am_assetReclassification.Reclassify_ID AS am_assetReclassification_Reclassify_ID,\n"
		     		+ "am_assetReclassification.Monthly_Dep AS am_assetReclassification_Monthly_Dep,\n"
		     		+ "am_assetReclassification.nbv AS am_assetReclassification_nbv\n"
		     		+ "FROM\n"
		     		+ "dbo.am_ad_branch am_ad_branch INNER JOIN dbo.am_assetReclassification am_assetReclassification ON am_ad_branch.BRANCH_CODE = am_assetReclassification.old_branch_code\n"
		     		+ "INNER JOIN dbo.am_ad_category am_ad_category ON am_assetReclassification.new_category_id = am_ad_category.category_ID,\n"
		     		+ "dbo.am_gb_company am_gb_company\n"
		     		+ "WHERE\n"
		     		+ " am_assetReclassification.User_ID <> 0 \n"
		     		+ " and am_assetReclassification.status='ACTIVE' \n"
		     		+ " and am_ad_category.category_id = ? \n"
		     		+ "	 \n"
		     		+ "ORDER BY\n"
		     		+ "am_assetReclassification.old_branch_code ASC,\n"
		     		+ "am_assetReclassification.old_category_code ASC";    
			}
	 
	 if(!branch_Id.equals("0")  && !categoryCode.equals("0")  && FromDate.equals("") && ToDate.equals("")){
		   System.out.println("======>>>>>>>Branch and Category Selected: ");
		     ColQuery ="SELECT\n"
		     		+ "am_gb_company.company_name AS am_gb_company_company_name,\n"
		     		+ "am_ad_branch.BRANCH_NAME AS am_ad_branch_BRANCH_NAME,\n"
		     		+ "am_ad_category.category_name AS am_ad_category_category_name,\n"
		     		+ "am_ad_category.Dep_rate AS am_ad_category_Dep_rate,\n"
		     		+ "am_ad_category.Accum_Dep_ledger AS am_ad_category_Accum_Dep_ledger,\n"
		     		+ "am_ad_category.Dep_ledger AS am_ad_category_Dep_ledger,\n"
		     		+ "am_ad_category.Asset_Ledger AS am_ad_category_Asset_Ledger,\n"
		     		+ "am_ad_category.gl_account AS am_ad_category_gl_account,\n"
		     		+ "am_assetReclassification.asset_id AS am_assetReclassification_asset_id,\n"
		     		+ "am_assetReclassification.old_category_id AS am_assetReclassification_old_category_id,\n"
		     		+ "am_assetReclassification.new_category_id AS am_assetReclassification_new_category_id,\n"
		     		+ "am_assetReclassification.old_depr_rate AS am_assetReclassification_old_depr_rate,\n"
		     		+ "am_assetReclassification.reclassify_reason AS am_assetReclassification_reclassify_reason,\n"
		     		+ "am_assetReclassification.reclassify_date AS am_assetReclassification_reclassify_date,\n"
		     		+ "am_assetReclassification.old_accum_dep AS am_assetReclassification_old_accum_dep,\n"
		     		+ "am_assetReclassification.new_depr_rate AS am_assetReclassification_new_depr_rate,\n"
		     		+ "am_assetReclassification.new_accum_dep AS am_assetReclassification_new_accum_dep,\n"
		     		+ "am_assetReclassification.recalc_depr AS am_assetReclassification_recalc_depr,\n"
		     		+ "am_assetReclassification.raise_entry AS am_assetReclassification_raise_entry,\n"
		     		+ "am_assetReclassification.recalc_difference AS am_assetReclassification_recalc_difference,\n"
		     		+ "am_assetReclassification.cost_price AS am_assetReclassification_cost_price,\n"
		     		+ "am_assetReclassification.User_ID AS am_assetReclassification_User_ID,\n"
		     		+ "am_assetReclassification.old_dep_ytd AS am_assetReclassification_old_dep_ytd,\n"
		     		+ "am_assetReclassification.old_category_code AS am_assetReclassification_old_category_code,\n"
		     		+ "am_assetReclassification.old_branch_code AS am_assetReclassification_old_branch_code,\n"
		     		+ "am_assetReclassification.description AS am_assetReclassification_description,\n"
		     		+ "am_assetReclassification.New_asset_id AS am_assetReclassification_New_asset_id,\n"
		     		+ "am_assetReclassification.Reclassify_ID AS am_assetReclassification_Reclassify_ID,\n"
		     		+ "am_assetReclassification.Monthly_Dep AS am_assetReclassification_Monthly_Dep,\n"
		     		+ "am_assetReclassification.nbv AS am_assetReclassification_nbv\n"
		     		+ "FROM\n"
		     		+ "dbo.am_ad_branch am_ad_branch INNER JOIN dbo.am_assetReclassification am_assetReclassification ON am_ad_branch.BRANCH_CODE = am_assetReclassification.old_branch_code\n"
		     		+ "INNER JOIN dbo.am_ad_category am_ad_category ON am_assetReclassification.new_category_id = am_ad_category.category_ID,\n"
		     		+ "dbo.am_gb_company am_gb_company\n"
		     		+ "WHERE\n"
		     		+ " am_assetReclassification.User_ID <> 0 \n"
		     		+ " and am_assetReclassification.status='ACTIVE' \n"
		     		+ " and am_ad_category.category_id = ? \n"
		     		+ " and am_ad_branch.branch_id = ? \n"
		     		+ "	 \n"
		     		+ "ORDER BY\n"
		     		+ "am_assetReclassification.old_branch_code ASC,\n"
		     		+ "am_assetReclassification.old_category_code ASC";    
			}      
	 
	 
   if(!branch_Id.equals("0")  && !categoryCode.equals("0")  && !FromDate.equals("") && !ToDate.equals("")){
	   System.out.println("======>>>>>>>Branch and Category and Date Selected: ");
	     ColQuery ="SELECT\n"
	     		+ "am_gb_company.company_name AS am_gb_company_company_name,\n"
	     		+ "am_ad_branch.BRANCH_NAME AS am_ad_branch_BRANCH_NAME,\n"
	     		+ "am_ad_category.category_name AS am_ad_category_category_name,\n"
	     		+ "am_ad_category.Dep_rate AS am_ad_category_Dep_rate,\n"
	     		+ "am_ad_category.Accum_Dep_ledger AS am_ad_category_Accum_Dep_ledger,\n"
	     		+ "am_ad_category.Dep_ledger AS am_ad_category_Dep_ledger,\n"
	     		+ "am_ad_category.Asset_Ledger AS am_ad_category_Asset_Ledger,\n"
	     		+ "am_ad_category.gl_account AS am_ad_category_gl_account,\n"
	     		+ "am_assetReclassification.asset_id AS am_assetReclassification_asset_id,\n"
	     		+ "am_assetReclassification.old_category_id AS am_assetReclassification_old_category_id,\n"
	     		+ "am_assetReclassification.new_category_id AS am_assetReclassification_new_category_id,\n"
	     		+ "am_assetReclassification.old_depr_rate AS am_assetReclassification_old_depr_rate,\n"
	     		+ "am_assetReclassification.reclassify_reason AS am_assetReclassification_reclassify_reason,\n"
	     		+ "am_assetReclassification.reclassify_date AS am_assetReclassification_reclassify_date,\n"
	     		+ "am_assetReclassification.old_accum_dep AS am_assetReclassification_old_accum_dep,\n"
	     		+ "am_assetReclassification.new_depr_rate AS am_assetReclassification_new_depr_rate,\n"
	     		+ "am_assetReclassification.new_accum_dep AS am_assetReclassification_new_accum_dep,\n"
	     		+ "am_assetReclassification.recalc_depr AS am_assetReclassification_recalc_depr,\n"
	     		+ "am_assetReclassification.raise_entry AS am_assetReclassification_raise_entry,\n"
	     		+ "am_assetReclassification.recalc_difference AS am_assetReclassification_recalc_difference,\n"
	     		+ "am_assetReclassification.cost_price AS am_assetReclassification_cost_price,\n"
	     		+ "am_assetReclassification.User_ID AS am_assetReclassification_User_ID,\n"
	     		+ "am_assetReclassification.old_dep_ytd AS am_assetReclassification_old_dep_ytd,\n"
	     		+ "am_assetReclassification.old_category_code AS am_assetReclassification_old_category_code,\n"
	     		+ "am_assetReclassification.old_branch_code AS am_assetReclassification_old_branch_code,\n"
	     		+ "am_assetReclassification.description AS am_assetReclassification_description,\n"
	     		+ "am_assetReclassification.New_asset_id AS am_assetReclassification_New_asset_id,\n"
	     		+ "am_assetReclassification.Reclassify_ID AS am_assetReclassification_Reclassify_ID,\n"
	     		+ "am_assetReclassification.Monthly_Dep AS am_assetReclassification_Monthly_Dep,\n"
	     		+ "am_assetReclassification.nbv AS am_assetReclassification_nbv\n"
	     		+ "FROM\n"
	     		+ "dbo.am_ad_branch am_ad_branch INNER JOIN dbo.am_assetReclassification am_assetReclassification ON am_ad_branch.BRANCH_CODE = am_assetReclassification.old_branch_code\n"
	     		+ "INNER JOIN dbo.am_ad_category am_ad_category ON am_assetReclassification.new_category_id = am_ad_category.category_ID,\n"
	     		+ "dbo.am_gb_company am_gb_company\n"
	     		+ "WHERE\n"
	     		+ " am_assetReclassification.User_ID <> 0 \n"
	     		+ " and am_assetReclassification.status='ACTIVE' \n"
	     		+ " and am_ad_category.category_id = ? \n"
	     		+ " and am_ad_branch.branch_id = ? \n"
	     		+ " and reclassify_date BETWEEN ? and ? \n"
	     		+ "	 \n"
	     		+ "ORDER BY\n"
	     		+ "am_assetReclassification.old_branch_code ASC,\n"
	     		+ "am_assetReclassification.old_category_code ASC";    
		}      
//   System.out.println("======>>>>>>>ColQuery: "+ColQuery);
     java.util.ArrayList list =rep.getAssetReclassificationRecords(ColQuery,branch_Id,categoryCode,FromDate,ToDate,asset_Id);
     System.out.println("======>>>>>>>list size: "+list.size()+"        =====report: "+report);
     if(list.size()!=0){
   	 if(report.equalsIgnoreCase("rptMenuBCCLAS")){
    	 SXSSFWorkbook workbook = new SXSSFWorkbook();
         Sheet sheet = workbook.createSheet("Demo");
         Row rowhead = sheet.createRow((int) 0);
         
         rowhead.createCell( 0).setCellValue("S/No.");
         rowhead.createCell( 1).setCellValue("Branch Name");
         rowhead.createCell( 2).setCellValue("Category Name");
         rowhead.createCell( 3).setCellValue("Dep Rate");
         rowhead.createCell( 4).setCellValue("Accum Dep. Ledger");
         rowhead.createCell( 5).setCellValue("Dep Ledger");
         rowhead.createCell( 6).setCellValue("Asset Ledger");
         rowhead.createCell( 7).setCellValue("GL Account");
         rowhead.createCell( 8).setCellValue("Asset Id");
         rowhead.createCell( 9).setCellValue("Old Category Id");
         rowhead.createCell( 10).setCellValue("New Category Id");
         rowhead.createCell( 11).setCellValue("Old Depr Rate");
		 rowhead.createCell( 12).setCellValue("Reclassify Reason");
		 rowhead.createCell( 13).setCellValue("Reclassify Date");
		 rowhead.createCell( 14).setCellValue("Old Accum Dep");
		 rowhead.createCell( 15).setCellValue("New Depr Date");
		 rowhead.createCell( 16).setCellValue("New Accum Dep");
		 rowhead.createCell( 17).setCellValue("Recalc Depr");
		 rowhead.createCell( 18).setCellValue("Raise Entry");
		 rowhead.createCell( 19).setCellValue("Recalc Difference");
		 rowhead.createCell( 20).setCellValue("Cost Price");
         rowhead.createCell( 21).setCellValue("User Id");
         rowhead.createCell( 22).setCellValue("Old Dep YTD");
         rowhead.createCell( 23).setCellValue("Old Category Code");
         rowhead.createCell( 24).setCellValue("Old Branch Code");
         rowhead.createCell( 25).setCellValue("Description");
         rowhead.createCell( 26).setCellValue("New Asset Id");
         rowhead.createCell( 27).setCellValue("Reclassify Id");
         rowhead.createCell( 28).setCellValue("Monthly Dep");
         rowhead.createCell( 29).setCellValue("NBV");
         
         

     int i = 1;
     System.out.println("<<<<<<list.size(): "+list.size());
	 for(int k=0;k<list.size();k++)
     {
    	 com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);    	 
    	    
            String branchName = newassettrans.getBranchName();
			String categoryName = newassettrans.getCategoryName();
			double depRate = newassettrans.getDepRate();
			String accumDepLedger = newassettrans.getAccumDepLedger();
			String depLedger = newassettrans.getDepLedger();
			String assetLedger = newassettrans.getAssetLedger();
			String gl_Account = newassettrans.getGlAccount();
			String assetId =  newassettrans.getOldassetId();
			String old_Category_Id = newassettrans.getCategoryCode();
			double old_Depr_Rate = newassettrans.getPrevMonthlyDep();
			String reclassify_Reason = newassettrans.getDisposeReason();
			String reclassify_Date = newassettrans.getDisposalDate() != null ? getDate(newassettrans.getDisposalDate()) : "" ;
			double old_Accum_Dep = newassettrans.getPrevAccumDep();
			double new_Depr_Rate = newassettrans.getPrevIMPROVAccumDep();
			double new_Accum_Dep = newassettrans.getAccumDep();
			String recalc_Depr = newassettrans.getAction();
			String raise_Entry = newassettrans.getAssetEngineNo();
			double recalc_Difference = newassettrans.getAmount();
			double costPrice =  newassettrans.getCostPrice();
			String user_Id = newassettrans.getUserID();
			String old_Dep_Ytd = newassettrans.getOldDeptCode();
			String old_Category_Code = newassettrans.getOldCategoryCode();
			String old_Branch_Code = newassettrans.getOldBranchCode();
			String description = newassettrans.getDescription();
			String new_AssetId = newassettrans.getAssetId();
			String reclassifyId = newassettrans.getAssetMake();
			double monthly_Dep = newassettrans.getMonthlyDep();
			double nbv = newassettrans.getNbv();
			String new_Category_Id = newassettrans.getNewsbuCode();
			

			Row row = sheet.createRow((int) i);

			row.createCell((short) 0).setCellValue(i);
			row.createCell((short) 1).setCellValue(branchName);
            row.createCell((short) 2).setCellValue(categoryName);
            row.createCell((short) 3).setCellValue(depRate);
            row.createCell((short) 4).setCellValue(accumDepLedger);
            row.createCell((short) 5).setCellValue(depLedger);
            row.createCell((short) 6).setCellValue(assetLedger);
            row.createCell((short) 7).setCellValue(gl_Account);
            row.createCell((short) 8).setCellValue(assetId);
            row.createCell((short) 9).setCellValue(old_Category_Id);
            row.createCell((short) 10).setCellValue(new_Category_Id);
			row.createCell((short) 11).setCellValue(old_Depr_Rate);
			row.createCell((short) 12).setCellValue(reclassify_Reason);
			row.createCell((short) 13).setCellValue(reclassify_Date);
			row.createCell((short) 14).setCellValue(old_Accum_Dep);
			row.createCell((short) 15).setCellValue(new_Depr_Rate);
			row.createCell((short) 16).setCellValue(new_Accum_Dep);
			row.createCell((short) 17).setCellValue(recalc_Depr);
            row.createCell((short) 18).setCellValue(raise_Entry);
            row.createCell((short) 19).setCellValue(recalc_Difference);
            row.createCell((short) 20).setCellValue(costPrice);
            row.createCell((short) 21).setCellValue(user_Id);
            row.createCell((short) 22).setCellValue(old_Dep_Ytd);
            row.createCell((short) 23).setCellValue(old_Category_Code);
            row.createCell((short) 24).setCellValue(old_Branch_Code);
            row.createCell((short) 25).setCellValue(description);
            row.createCell((short) 26).setCellValue(new_AssetId);
            row.createCell((short) 27).setCellValue(reclassifyId);
            row.createCell((short) 28).setCellValue(monthly_Dep);
            row.createCell((short) 29).setCellValue(nbv);


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