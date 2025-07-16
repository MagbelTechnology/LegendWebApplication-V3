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
   
public class AssetTransferReportExport extends HttpServlet
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
    if(report.equalsIgnoreCase("rptMenuBCTRANS")){fileName = branchCode+"By"+userName+"AssetTransferReport.xlsx";}
    
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
  	     		+ "am_gb_company.company_name AS am_gb_company_company_name,\n"
  	     		+ "am_ad_category.category_name AS am_ad_category_category_name,\n"
  	     		+ "am_ad_category.Dep_rate AS am_ad_category_Dep_rate,\n"
  	     		+ "am_ad_category.Accum_Dep_ledger AS am_ad_category_Accum_Dep_ledger,\n"
  	     		+ "am_ad_category.Dep_ledger AS am_ad_category_Dep_ledger,\n"
  	     		+ "am_ad_category.Asset_Ledger AS am_ad_category_Asset_Ledger,\n"
  	     		+ "am_ad_category.gl_account AS am_ad_category_gl_account,\n"
  	     		+ "am_assetTransfer.asset_id AS am_assetTransfer_asset_id,\n"
  	     		+ "am_assetTransfer.OLD_branch_Id AS am_assetTransfer_OLD_branch_Id,\n"
  	     		+ "am_assetTransfer.OLD_dept_ID AS am_assetTransfer_OLD_dept_ID,\n"
  	     		+ "am_assetTransfer.Transfer_Date AS am_assetTransfer_Transfer_Date,\n"
  	     		+ "am_assetTransfer.OLD_Asset_user AS am_assetTransfer_OLD_Asset_user,\n"
  	     		+ "am_assetTransfer.OLD_Section AS am_assetTransfer_OLD_Section,\n"
  	     		+ "am_assetTransfer.OLD_BRANCH_CODE AS am_assetTransfer_OLD_BRANCH_CODE,\n"
  	     		+ "am_assetTransfer.OLD_SECTION_CODE AS am_assetTransfer_OLD_SECTION_CODE,\n"
  	     		+ "am_assetTransfer.OLD_DEPT_CODE AS am_assetTransfer_OLD_DEPT_CODE,\n"
  	     		+ "am_ad_branch_A.BRANCH_NAME AS am_ad_branch_A_BRANCH_NAME,\n"
  	     		+ "am_assetTransfer.approval_status AS am_assetTransfer_approval_status,\n"
  	     		+ "am_assetTransfer.OLD_CATEGORY_CODE AS am_assetTransfer_OLD_CATEGORY_CODE,\n"
  	     		+ "am_assetTransfer.description AS am_assetTransfer_description,\n"
  	     		+ "am_assetTransfer.cost_price AS am_assetTransfer_cost_price,\n"
  	     		+ "am_assetTransfer.Accum_Dep AS am_assetTransfer_Accum_Dep,\n"
  	     		+ "am_assetTransfer.Monthly_Dep AS am_assetTransfer_Monthly_Dep,\n"
  	     		+ "am_assetTransfer.NEW_BRANCH_CODE AS am_assetTransfer_NEW_BRANCH_CODE,\n"
  	     		+ "am_assetTransfer.NEW_SECTION_CODE AS am_assetTransfer_NEW_SECTION_CODE,\n"
  	     		+ "am_assetTransfer.NEW_DEPT_CODE AS am_assetTransfer_NEW_DEPT_CODE,\n"
  	     		+ "am_assetTransfer.NBV AS am_assetTransfer_NBV,\n"
  	     		+ "am_assetTransfer.New_Asset_id AS am_assetTransfer_New_Asset_id,\n"
  	     		+ "am_ad_branch_A.BRANCH_ID AS am_ad_branch_A_BRANCH_ID\n"
  	     		+ "FROM \n"
  	     		+ "dbo.am_assetTransfer am_assetTransfer \n"
  	     		+ "INNER JOIN dbo.am_ad_branch am_ad_branch_A ON am_assetTransfer.OLD_BRANCH_CODE = am_ad_branch_A.BRANCH_CODE\n"
  	     		+ "INNER JOIN dbo.am_ad_category am_ad_category ON am_assetTransfer.OLD_CATEGORY_CODE = am_ad_category.category_code,\n"
  	     		+ "dbo.am_gb_company am_gb_company\n"
  	     		+ "WHERE \n"
  	     		+ "am_assetTransfer.approval_status = 'POSTED' \n"
  	     		+ "UNION \n"
  	     		+ "SELECT \n"
  	     		+ "am_gb_company.company_name AS am_gb_company_company_name,\n"
  	     		+ "am_ad_category.category_name AS am_ad_category_category_name,\n"
  	     		+ "am_ad_category.Dep_rate AS am_ad_category_Dep_rate,\n"
  	     		+ "am_ad_category.Accum_Dep_ledger AS am_ad_category_Accum_Dep_ledger,\n"
  	     		+ "am_ad_category.Dep_ledger AS am_ad_category_Dep_ledger,\n"
  	     		+ "am_ad_category.Asset_Ledger AS am_ad_category_Asset_Ledger,\n"
  	     		+ "am_ad_category.gl_account AS am_ad_category_gl_account,\n"
  	     		+ "am_gb_bulkTransfer.asset_id AS am_gb_bulkTransfer_asset_id,\n"
  	     		+ "am_gb_bulkTransfer.OLDbranch_Id AS am_gb_bulkTransfer_OLD_branch_Id,\n"
  	     		+ "am_gb_bulkTransfer.OLDdept_ID AS am_gb_bulkTransfer_OLD_dept_ID,\n"
  	     		+ "am_gb_bulkTransfer.Transfer_Date AS am_gb_bulkTransfer_Transfer_Date,\n"
  	     		+ "am_gb_bulkTransfer.OLDAsset_user AS am_gb_bulkTransfer_OLD_Asset_user,\n"
  	     		+ "am_gb_bulkTransfer.OLDSection_ID AS am_gb_bulkTransfer_OLD_Section,\n"
  	     		+ "'',\n"
  	     		+ "'',\n"
  	     		+ "'',\n"
  	     		+ "am_ad_branch_A.BRANCH_NAME AS am_ad_branch_A_BRANCH_NAME,\n"
  	     		+ "am_gb_bulkTransfer.STATUS AS am_gb_bulkTransfer_approval_status,\n"
  	     		+ "'',\n"
  	     		+ "am_gb_bulkTransfer.description AS am_gb_bulkTransfer_description,\n"
  	     		+ "am_gb_bulkTransfer.cost_price AS am_gb_bulkTransfer_cost_price,\n"
  	     		+ "am_gb_bulkTransfer.Accum_Dep AS am_gb_bulkTransfer_Accum_Dep,\n"
  	     		+ "am_gb_bulkTransfer.Monthly_Dep AS am_gb_bulkTransfer_Monthly_Dep,\n"
  	     		+ "am_gb_bulkTransfer.NEW_BRANCH_CODE AS am_gb_bulkTransfer_NEW_BRANCH_CODE,\n"
  	     		+ "am_gb_bulkTransfer.NEW_SECTION_CODE AS am_gb_bulkTransfer_NEW_SECTION_CODE,\n"
  	     		+ "am_gb_bulkTransfer.NEW_DEPT_CODE AS am_gb_bulkTransfer_NEW_DEPT_CODE,\n"
  	     		+ "am_gb_bulkTransfer.NBV AS am_gb_bulkTransfer_NBV,\n"
  	     		+ "am_gb_bulkTransfer.New_Asset_id AS am_gb_bulkTransfer_New_Asset_id,\n"
  	     		+ "am_ad_branch_A.BRANCH_ID AS am_ad_branch_A_BRANCH_ID\n"
  	     		+ "FROM \n"
  	     		+ "dbo.am_gb_bulkTransfer am_gb_bulkTransfer \n"
  	     		+ "INNER JOIN dbo.am_ad_branch am_ad_branch_A ON am_gb_bulkTransfer.OLDBRANCH_ID = am_ad_branch_A.BRANCH_ID\n"
  	     		+ "INNER JOIN dbo.am_ad_category am_ad_category ON am_gb_bulkTransfer.CATEGORY_ID = am_ad_category.category_ID,\n"
  	     		+ "dbo.am_gb_company am_gb_company\n"
  	     		+ "WHERE \n"
  	     		+ "am_gb_bulkTransfer.STATUS = 'POSTED' \n"
  	     		+ "ORDER BY \n"
  	     		+ "am_assetTransfer.OLD_BRANCH_CODE ASC, \n"
  	     		+ "am_assetTransfer.OLD_CATEGORY_CODE ASC";    
  		}
     
     
     if(branch_Id.equals("0")  && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")){
    	 System.out.println("======>>>>>>>Date Selected: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate);	     
	     ColQuery ="SELECT\n"
	     		+ "am_gb_company.company_name AS am_gb_company_company_name,\n"
	     		+ "am_ad_category.category_name AS am_ad_category_category_name,\n"
	     		+ "am_ad_category.Dep_rate AS am_ad_category_Dep_rate,\n"
	     		+ "am_ad_category.Accum_Dep_ledger AS am_ad_category_Accum_Dep_ledger,\n"
	     		+ "am_ad_category.Dep_ledger AS am_ad_category_Dep_ledger,\n"
	     		+ "am_ad_category.Asset_Ledger AS am_ad_category_Asset_Ledger,\n"
	     		+ "am_ad_category.gl_account AS am_ad_category_gl_account,\n"
	     		+ "am_assetTransfer.asset_id AS am_assetTransfer_asset_id,\n"
	     		+ "am_assetTransfer.OLD_branch_Id AS am_assetTransfer_OLD_branch_Id,\n"
	     		+ "am_assetTransfer.OLD_dept_ID AS am_assetTransfer_OLD_dept_ID,\n"
	     		+ "am_assetTransfer.Transfer_Date AS am_assetTransfer_Transfer_Date,\n"
	     		+ "am_assetTransfer.OLD_Asset_user AS am_assetTransfer_OLD_Asset_user,\n"
	     		+ "am_assetTransfer.OLD_Section AS am_assetTransfer_OLD_Section,\n"
	     		+ "am_assetTransfer.OLD_BRANCH_CODE AS am_assetTransfer_OLD_BRANCH_CODE,\n"
	     		+ "am_assetTransfer.OLD_SECTION_CODE AS am_assetTransfer_OLD_SECTION_CODE,\n"
	     		+ "am_assetTransfer.OLD_DEPT_CODE AS am_assetTransfer_OLD_DEPT_CODE,\n"
	     		+ "am_ad_branch_A.BRANCH_NAME AS am_ad_branch_A_BRANCH_NAME,\n"
	     		+ "am_assetTransfer.approval_status AS am_assetTransfer_approval_status,\n"
	     		+ "am_assetTransfer.OLD_CATEGORY_CODE AS am_assetTransfer_OLD_CATEGORY_CODE,\n"
	     		+ "am_assetTransfer.description AS am_assetTransfer_description,\n"
	     		+ "am_assetTransfer.cost_price AS am_assetTransfer_cost_price,\n"
	     		+ "am_assetTransfer.Accum_Dep AS am_assetTransfer_Accum_Dep,\n"
	     		+ "am_assetTransfer.Monthly_Dep AS am_assetTransfer_Monthly_Dep,\n"
	     		+ "am_assetTransfer.NEW_BRANCH_CODE AS am_assetTransfer_NEW_BRANCH_CODE,\n"
	     		+ "am_assetTransfer.NEW_SECTION_CODE AS am_assetTransfer_NEW_SECTION_CODE,\n"
	     		+ "am_assetTransfer.NEW_DEPT_CODE AS am_assetTransfer_NEW_DEPT_CODE,\n"
	     		+ "am_assetTransfer.NBV AS am_assetTransfer_NBV,\n"
	     		+ "am_assetTransfer.New_Asset_id AS am_assetTransfer_New_Asset_id,\n"
	     		+ "am_ad_branch_A.BRANCH_ID AS am_ad_branch_A_BRANCH_ID\n"
	     		+ "FROM \n"
	     		+ "dbo.am_assetTransfer am_assetTransfer \n"
	     		+ "INNER JOIN dbo.am_ad_branch am_ad_branch_A ON am_assetTransfer.OLD_BRANCH_CODE = am_ad_branch_A.BRANCH_CODE\n"
	     		+ "INNER JOIN dbo.am_ad_category am_ad_category ON am_assetTransfer.OLD_CATEGORY_CODE = am_ad_category.category_code,\n"
	     		+ "dbo.am_gb_company am_gb_company\n"
	     		+ "WHERE \n"
	     		+ "am_assetTransfer.approval_status = 'POSTED' \n"
	     		+ "AND Transfer_Date BETWEEN ? AND ? \n"
	     		+ "UNION\n"
	     		+ "SELECT\n"
	     		+ "am_gb_company.company_name AS am_gb_company_company_name,\n"
	     		+ "am_ad_category.category_name AS am_ad_category_category_name,\n"
	     		+ "am_ad_category.Dep_rate AS am_ad_category_Dep_rate,\n"
	     		+ "am_ad_category.Accum_Dep_ledger AS am_ad_category_Accum_Dep_ledger,\n"
	     		+ "am_ad_category.Dep_ledger AS am_ad_category_Dep_ledger,\n"
	     		+ "am_ad_category.Asset_Ledger AS am_ad_category_Asset_Ledger,\n"
	     		+ "am_ad_category.gl_account AS am_ad_category_gl_account,\n"
	     		+ "am_gb_bulkTransfer.asset_id AS am_gb_bulkTransfer_asset_id,\n"
	     		+ "am_gb_bulkTransfer.OLDbranch_Id AS am_gb_bulkTransfer_OLD_branch_Id,\n"
	     		+ "am_gb_bulkTransfer.OLDdept_ID AS am_gb_bulkTransfer_OLD_dept_ID,\n"
	     		+ "am_gb_bulkTransfer.Transfer_Date AS am_gb_bulkTransfer_Transfer_Date,\n"
	     		+ "am_gb_bulkTransfer.OLDAsset_user AS am_gb_bulkTransfer_OLD_Asset_user,\n"
	     		+ "am_gb_bulkTransfer.OLDSection_ID AS am_gb_bulkTransfer_OLD_Section,\n"
	     		+ "'',\n"
	     		+ "'',\n"
	     		+ "'',\n"
	     		+ "am_ad_branch_A.BRANCH_NAME AS am_ad_branch_A_BRANCH_NAME,\n"
	     		+ "am_gb_bulkTransfer.STATUS AS am_gb_bulkTransfer_approval_status,\n"
	     		+ "'',\n"
	     		+ "am_gb_bulkTransfer.description AS am_gb_bulkTransfer_description,\n"
	     		+ "am_gb_bulkTransfer.cost_price AS am_gb_bulkTransfer_cost_price,\n"
	     		+ "am_gb_bulkTransfer.Accum_Dep AS am_gb_bulkTransfer_Accum_Dep,\n"
	     		+ "am_gb_bulkTransfer.Monthly_Dep AS am_gb_bulkTransfer_Monthly_Dep,\n"
	     		+ "am_gb_bulkTransfer.NEW_BRANCH_CODE AS am_gb_bulkTransfer_NEW_BRANCH_CODE,\n"
	     		+ "am_gb_bulkTransfer.NEW_SECTION_CODE AS am_gb_bulkTransfer_NEW_SECTION_CODE,\n"
	     		+ "am_gb_bulkTransfer.NEW_DEPT_CODE AS am_gb_bulkTransfer_NEW_DEPT_CODE,\n"
	     		+ "am_gb_bulkTransfer.NBV AS am_gb_bulkTransfer_NBV,\n"
	     		+ "am_gb_bulkTransfer.New_Asset_id AS am_gb_bulkTransfer_New_Asset_id,\n"
	     		+ "am_ad_branch_A.BRANCH_ID AS am_ad_branch_A_BRANCH_ID\n"
	     		+ "FROM \n"
	     		+ "dbo.am_gb_bulkTransfer am_gb_bulkTransfer \n"
	     		+ "INNER JOIN dbo.am_ad_branch am_ad_branch_A ON am_gb_bulkTransfer.OLDBRANCH_ID = am_ad_branch_A.BRANCH_ID\n"
	     		+ "INNER JOIN dbo.am_ad_category am_ad_category ON am_gb_bulkTransfer.CATEGORY_ID = am_ad_category.category_ID,\n"
	     		+ "dbo.am_gb_company am_gb_company\n"
	     		+ "WHERE \n"
	     		+ "am_gb_bulkTransfer.STATUS = 'POSTED' \n"
	     		+ "AND Transfer_Date BETWEEN ? AND ?  \n"
	     		+ "ORDER BY \n"
	     		+ "am_assetTransfer.OLD_BRANCH_CODE ASC,\n"
	     		+ "am_assetTransfer.OLD_CATEGORY_CODE ASC";  
	}      
	 if(!branch_Id.equals("0")  && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")){	   
	   System.out.println("======>>>>>>>Branch and Date Selected: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate);
	     ColQuery ="SELECT\n"
	     		+ "am_gb_company.company_name AS am_gb_company_company_name,\n"
	     		+ "am_ad_category.category_name AS am_ad_category_category_name,\n"
	     		+ "am_ad_category.Dep_rate AS am_ad_category_Dep_rate,\n"
	     		+ "am_ad_category.Accum_Dep_ledger AS am_ad_category_Accum_Dep_ledger,\n"
	     		+ "am_ad_category.Dep_ledger AS am_ad_category_Dep_ledger,\n"
	     		+ "am_ad_category.Asset_Ledger AS am_ad_category_Asset_Ledger,\n"
	     		+ "am_ad_category.gl_account AS am_ad_category_gl_account,\n"
	     		+ "am_assetTransfer.asset_id AS am_assetTransfer_asset_id,\n"
	     		+ "am_assetTransfer.OLD_branch_Id AS am_assetTransfer_OLD_branch_Id,\n"
	     		+ "am_assetTransfer.OLD_dept_ID AS am_assetTransfer_OLD_dept_ID,\n"
	     		+ "am_assetTransfer.Transfer_Date AS am_assetTransfer_Transfer_Date,\n"
	     		+ "am_assetTransfer.OLD_Asset_user AS am_assetTransfer_OLD_Asset_user,\n"
	     		+ "am_assetTransfer.OLD_Section AS am_assetTransfer_OLD_Section,\n"
	     		+ "am_assetTransfer.OLD_BRANCH_CODE AS am_assetTransfer_OLD_BRANCH_CODE,\n"
	     		+ "am_assetTransfer.OLD_SECTION_CODE AS am_assetTransfer_OLD_SECTION_CODE,\n"
	     		+ "am_assetTransfer.OLD_DEPT_CODE AS am_assetTransfer_OLD_DEPT_CODE,\n"
	     		+ "am_ad_branch_A.BRANCH_NAME AS am_ad_branch_A_BRANCH_NAME,\n"
	     		+ "am_assetTransfer.approval_status AS am_assetTransfer_approval_status,\n"
	     		+ "am_assetTransfer.OLD_CATEGORY_CODE AS am_assetTransfer_OLD_CATEGORY_CODE,\n"
	     		+ "am_assetTransfer.description AS am_assetTransfer_description,\n"
	     		+ "am_assetTransfer.cost_price AS am_assetTransfer_cost_price,\n"
	     		+ "am_assetTransfer.Accum_Dep AS am_assetTransfer_Accum_Dep,\n"
	     		+ "am_assetTransfer.Monthly_Dep AS am_assetTransfer_Monthly_Dep,\n"
	     		+ "am_assetTransfer.NEW_BRANCH_CODE AS am_assetTransfer_NEW_BRANCH_CODE,\n"
	     		+ "am_assetTransfer.NEW_SECTION_CODE AS am_assetTransfer_NEW_SECTION_CODE,\n"
	     		+ "am_assetTransfer.NEW_DEPT_CODE AS am_assetTransfer_NEW_DEPT_CODE,\n"
	     		+ "am_assetTransfer.NBV AS am_assetTransfer_NBV,\n"
	     		+ "am_assetTransfer.New_Asset_id AS am_assetTransfer_New_Asset_id,\n"
	     		+ "am_ad_branch_A.BRANCH_ID AS am_ad_branch_A_BRANCH_ID\n"
	     		+ "FROM \n"
	     		+ "dbo.am_assetTransfer am_assetTransfer \n"
	     		+ "INNER JOIN dbo.am_ad_branch am_ad_branch_A ON am_assetTransfer.OLD_BRANCH_CODE = am_ad_branch_A.BRANCH_CODE\n"
	     		+ "INNER JOIN dbo.am_ad_category am_ad_category ON am_assetTransfer.OLD_CATEGORY_CODE = am_ad_category.category_code,\n"
	     		+ "dbo.am_gb_company am_gb_company\n"
	     		+ "WHERE \n"
	     		+ "am_assetTransfer.approval_status = 'POSTED' \n"
	     		+ "and am_ad_branch_A.BRANCH_ID =? \n"
	     		+ "AND Transfer_Date BETWEEN ? AND ? \n"
	     		+ "UNION \n"
	     		+ "SELECT \n"
	     		+ "am_gb_company.company_name AS am_gb_company_company_name,\n"
	     		+ "am_ad_category.category_name AS am_ad_category_category_name,\n"
	     		+ "am_ad_category.Dep_rate AS am_ad_category_Dep_rate,\n"
	     		+ "am_ad_category.Accum_Dep_ledger AS am_ad_category_Accum_Dep_ledger,\n"
	     		+ "am_ad_category.Dep_ledger AS am_ad_category_Dep_ledger,\n"
	     		+ "am_ad_category.Asset_Ledger AS am_ad_category_Asset_Ledger,\n"
	     		+ "am_ad_category.gl_account AS am_ad_category_gl_account,\n"
	     		+ "am_gb_bulkTransfer.asset_id AS am_gb_bulkTransfer_asset_id,\n"
	     		+ "am_gb_bulkTransfer.OLDbranch_Id AS am_gb_bulkTransfer_OLD_branch_Id,\n"
	     		+ "am_gb_bulkTransfer.OLDdept_ID AS am_gb_bulkTransfer_OLD_dept_ID,\n"
	     		+ "am_gb_bulkTransfer.Transfer_Date AS am_gb_bulkTransfer_Transfer_Date,\n"
	     		+ "am_gb_bulkTransfer.OLDAsset_user AS am_gb_bulkTransfer_OLD_Asset_user,\n"
	     		+ "am_gb_bulkTransfer.OLDSection_ID AS am_gb_bulkTransfer_OLD_Section,\n"
	     		+ "'',\n"
	     		+ "'',\n"
	     		+ "'',\n"
	     		+ "am_ad_branch_A.BRANCH_NAME AS am_ad_branch_A_BRANCH_NAME,\n"
	     		+ "am_gb_bulkTransfer.STATUS AS am_gb_bulkTransfer_approval_status,\n"
	     		+ "'',\n"
	     		+ "am_gb_bulkTransfer.description AS am_gb_bulkTransfer_description,\n"
	     		+ "am_gb_bulkTransfer.cost_price AS am_gb_bulkTransfer_cost_price,\n"
	     		+ "am_gb_bulkTransfer.Accum_Dep AS am_gb_bulkTransfer_Accum_Dep,\n"
	     		+ "am_gb_bulkTransfer.Monthly_Dep AS am_gb_bulkTransfer_Monthly_Dep,\n"
	     		+ "am_gb_bulkTransfer.NEW_BRANCH_CODE AS am_gb_bulkTransfer_NEW_BRANCH_CODE,\n"
	     		+ "am_gb_bulkTransfer.NEW_SECTION_CODE AS am_gb_bulkTransfer_NEW_SECTION_CODE,\n"
	     		+ "am_gb_bulkTransfer.NEW_DEPT_CODE AS am_gb_bulkTransfer_NEW_DEPT_CODE,\n"
	     		+ "am_gb_bulkTransfer.NBV AS am_gb_bulkTransfer_NBV,\n"
	     		+ "am_gb_bulkTransfer.New_Asset_id AS am_gb_bulkTransfer_New_Asset_id,\n"
	     		+ "am_ad_branch_A.BRANCH_ID AS am_ad_branch_A_BRANCH_ID\n"
	     		+ "FROM \n"
	     		+ "dbo.am_gb_bulkTransfer am_gb_bulkTransfer \n"
	     		+ "INNER JOIN dbo.am_ad_branch am_ad_branch_A ON am_gb_bulkTransfer.OLDBRANCH_ID = am_ad_branch_A.BRANCH_ID\n"
	     		+ "INNER JOIN dbo.am_ad_category am_ad_category ON am_gb_bulkTransfer.CATEGORY_ID = am_ad_category.category_ID,\n"
	     		+ "dbo.am_gb_company am_gb_company\n"
	     		+ "WHERE \n"
	     		+ "am_gb_bulkTransfer.STATUS = 'POSTED' \n"
	     		+ "and am_ad_branch_A.BRANCH_ID =? \n"
	     		+ "AND Transfer_Date BETWEEN ? AND ?  \n"
	     		+ "ORDER BY \n"
	     		+ "am_assetTransfer.OLD_BRANCH_CODE ASC,\n"
	     		+ "am_assetTransfer.OLD_CATEGORY_CODE ASC";   
		}  
	 
	 if(!branch_Id.equals("0")  && categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")){	   
		   System.out.println("======>>>>>>>Branch Selected: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate);
		     ColQuery ="SELECT\n"
		     		+ "am_gb_company.company_name AS am_gb_company_company_name,\n"
		     		+ "am_ad_category.category_name AS am_ad_category_category_name,\n"
		     		+ "am_ad_category.Dep_rate AS am_ad_category_Dep_rate,\n"
		     		+ "am_ad_category.Accum_Dep_ledger AS am_ad_category_Accum_Dep_ledger,\n"
		     		+ "am_ad_category.Dep_ledger AS am_ad_category_Dep_ledger,\n"
		     		+ "am_ad_category.Asset_Ledger AS am_ad_category_Asset_Ledger,\n"
		     		+ "am_ad_category.gl_account AS am_ad_category_gl_account,\n"
		     		+ "am_assetTransfer.asset_id AS am_assetTransfer_asset_id,\n"
		     		+ "am_assetTransfer.OLD_branch_Id AS am_assetTransfer_OLD_branch_Id,\n"
		     		+ "am_assetTransfer.OLD_dept_ID AS am_assetTransfer_OLD_dept_ID,\n"
		     		+ "am_assetTransfer.Transfer_Date AS am_assetTransfer_Transfer_Date,\n"
		     		+ "am_assetTransfer.OLD_Asset_user AS am_assetTransfer_OLD_Asset_user,\n"
		     		+ "am_assetTransfer.OLD_Section AS am_assetTransfer_OLD_Section,\n"
		     		+ "am_assetTransfer.OLD_BRANCH_CODE AS am_assetTransfer_OLD_BRANCH_CODE,\n"
		     		+ "am_assetTransfer.OLD_SECTION_CODE AS am_assetTransfer_OLD_SECTION_CODE,\n"
		     		+ "am_assetTransfer.OLD_DEPT_CODE AS am_assetTransfer_OLD_DEPT_CODE,\n"
		     		+ "am_ad_branch_A.BRANCH_NAME AS am_ad_branch_A_BRANCH_NAME,\n"
		     		+ "am_assetTransfer.approval_status AS am_assetTransfer_approval_status,\n"
		     		+ "am_assetTransfer.OLD_CATEGORY_CODE AS am_assetTransfer_OLD_CATEGORY_CODE,\n"
		     		+ "am_assetTransfer.description AS am_assetTransfer_description,\n"
		     		+ "am_assetTransfer.cost_price AS am_assetTransfer_cost_price,\n"
		     		+ "am_assetTransfer.Accum_Dep AS am_assetTransfer_Accum_Dep,\n"
		     		+ "am_assetTransfer.Monthly_Dep AS am_assetTransfer_Monthly_Dep,\n"
		     		+ "am_assetTransfer.NEW_BRANCH_CODE AS am_assetTransfer_NEW_BRANCH_CODE,\n"
		     		+ "am_assetTransfer.NEW_SECTION_CODE AS am_assetTransfer_NEW_SECTION_CODE,\n"
		     		+ "am_assetTransfer.NEW_DEPT_CODE AS am_assetTransfer_NEW_DEPT_CODE,\n"
		     		+ "am_assetTransfer.NBV AS am_assetTransfer_NBV,\n"
		     		+ "am_assetTransfer.New_Asset_id AS am_assetTransfer_New_Asset_id,\n"
		     		+ "am_ad_branch_A.BRANCH_ID AS am_ad_branch_A_BRANCH_ID\n"
		     		+ "FROM \n"
		     		+ "dbo.am_assetTransfer am_assetTransfer \n"
		     		+ "INNER JOIN dbo.am_ad_branch am_ad_branch_A ON am_assetTransfer.OLD_BRANCH_CODE = am_ad_branch_A.BRANCH_CODE\n"
		     		+ "INNER JOIN dbo.am_ad_category am_ad_category ON am_assetTransfer.OLD_CATEGORY_CODE = am_ad_category.category_code,\n"
		     		+ "dbo.am_gb_company am_gb_company\n"
		     		+ "WHERE \n"
		     		+ "am_assetTransfer.approval_status = 'POSTED' \n"
		     		+ "and am_ad_branch_A.BRANCH_ID =? \n"
		     		+ "UNION \n"
		     		+ "SELECT \n"
		     		+ "am_gb_company.company_name AS am_gb_company_company_name,\n"
		     		+ "am_ad_category.category_name AS am_ad_category_category_name,\n"
		     		+ "am_ad_category.Dep_rate AS am_ad_category_Dep_rate,\n"
		     		+ "am_ad_category.Accum_Dep_ledger AS am_ad_category_Accum_Dep_ledger,\n"
		     		+ "am_ad_category.Dep_ledger AS am_ad_category_Dep_ledger,\n"
		     		+ "am_ad_category.Asset_Ledger AS am_ad_category_Asset_Ledger,\n"
		     		+ "am_ad_category.gl_account AS am_ad_category_gl_account,\n"
		     		+ "am_gb_bulkTransfer.asset_id AS am_gb_bulkTransfer_asset_id,\n"
		     		+ "am_gb_bulkTransfer.OLDbranch_Id AS am_gb_bulkTransfer_OLD_branch_Id,\n"
		     		+ "am_gb_bulkTransfer.OLDdept_ID AS am_gb_bulkTransfer_OLD_dept_ID,\n"
		     		+ "am_gb_bulkTransfer.Transfer_Date AS am_gb_bulkTransfer_Transfer_Date,\n"
		     		+ "am_gb_bulkTransfer.OLDAsset_user AS am_gb_bulkTransfer_OLD_Asset_user,\n"
		     		+ "am_gb_bulkTransfer.OLDSection_ID AS am_gb_bulkTransfer_OLD_Section,\n"
		     		+ "'',\n"
		     		+ "'',\n"
		     		+ "'',\n"
		     		+ "am_ad_branch_A.BRANCH_NAME AS am_ad_branch_A_BRANCH_NAME,\n"
		     		+ "am_gb_bulkTransfer.STATUS AS am_gb_bulkTransfer_approval_status,\n"
		     		+ "'',\n"
		     		+ "am_gb_bulkTransfer.description AS am_gb_bulkTransfer_description,\n"
		     		+ "am_gb_bulkTransfer.cost_price AS am_gb_bulkTransfer_cost_price,\n"
		     		+ "am_gb_bulkTransfer.Accum_Dep AS am_gb_bulkTransfer_Accum_Dep,\n"
		     		+ "am_gb_bulkTransfer.Monthly_Dep AS am_gb_bulkTransfer_Monthly_Dep,\n"
		     		+ "am_gb_bulkTransfer.NEW_BRANCH_CODE AS am_gb_bulkTransfer_NEW_BRANCH_CODE,\n"
		     		+ "am_gb_bulkTransfer.NEW_SECTION_CODE AS am_gb_bulkTransfer_NEW_SECTION_CODE,\n"
		     		+ "am_gb_bulkTransfer.NEW_DEPT_CODE AS am_gb_bulkTransfer_NEW_DEPT_CODE,\n"
		     		+ "am_gb_bulkTransfer.NBV AS am_gb_bulkTransfer_NBV,\n"
		     		+ "am_gb_bulkTransfer.New_Asset_id AS am_gb_bulkTransfer_New_Asset_id,\n"
		     		+ "am_ad_branch_A.BRANCH_ID AS am_ad_branch_A_BRANCH_ID\n"
		     		+ "FROM \n"
		     		+ "dbo.am_gb_bulkTransfer am_gb_bulkTransfer \n"
		     		+ "INNER JOIN dbo.am_ad_branch am_ad_branch_A ON am_gb_bulkTransfer.OLDBRANCH_ID = am_ad_branch_A.BRANCH_ID\n"
		     		+ "INNER JOIN dbo.am_ad_category am_ad_category ON am_gb_bulkTransfer.CATEGORY_ID = am_ad_category.category_ID,\n"
		     		+ "dbo.am_gb_company am_gb_company\n"
		     		+ "WHERE \n"
		     		+ "am_gb_bulkTransfer.STATUS = 'POSTED' \n"
		     		+ "and am_ad_branch_A.BRANCH_ID =?\n"
		     		+ "ORDER BY \n"
		     		+ "am_assetTransfer.OLD_BRANCH_CODE ASC,\n"
		     		+ "am_assetTransfer.OLD_CATEGORY_CODE ASC";   
			}      

	 if(branch_Id.equals("0")  && !categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")){	   
	   System.out.println("======>>>>>>>Category and Date Selected: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate);
	     ColQuery ="SELECT\n"
	     		+ "am_gb_company.company_name AS am_gb_company_company_name,\n"
	     		+ "am_ad_category.category_name AS am_ad_category_category_name,\n"
	     		+ "am_ad_category.Dep_rate AS am_ad_category_Dep_rate,\n"
	     		+ "am_ad_category.Accum_Dep_ledger AS am_ad_category_Accum_Dep_ledger,\n"
	     		+ "am_ad_category.Dep_ledger AS am_ad_category_Dep_ledger,\n"
	     		+ "am_ad_category.Asset_Ledger AS am_ad_category_Asset_Ledger,\n"
	     		+ "am_ad_category.gl_account AS am_ad_category_gl_account,\n"
	     		+ "am_assetTransfer.asset_id AS am_assetTransfer_asset_id,\n"
	     		+ "am_assetTransfer.OLD_branch_Id AS am_assetTransfer_OLD_branch_Id,\n"
	     		+ "am_assetTransfer.OLD_dept_ID AS am_assetTransfer_OLD_dept_ID,\n"
	     		+ "am_assetTransfer.Transfer_Date AS am_assetTransfer_Transfer_Date,\n"
	     		+ "am_assetTransfer.OLD_Asset_user AS am_assetTransfer_OLD_Asset_user,\n"
	     		+ "am_assetTransfer.OLD_Section AS am_assetTransfer_OLD_Section,\n"
	     		+ "am_assetTransfer.OLD_BRANCH_CODE AS am_assetTransfer_OLD_BRANCH_CODE,\n"
	     		+ "am_assetTransfer.OLD_SECTION_CODE AS am_assetTransfer_OLD_SECTION_CODE,\n"
	     		+ "am_assetTransfer.OLD_DEPT_CODE AS am_assetTransfer_OLD_DEPT_CODE,\n"
	     		+ "am_ad_branch_A.BRANCH_NAME AS am_ad_branch_A_BRANCH_NAME,\n"
	     		+ "am_assetTransfer.approval_status AS am_assetTransfer_approval_status,\n"
	     		+ "am_assetTransfer.OLD_CATEGORY_CODE AS am_assetTransfer_OLD_CATEGORY_CODE,\n"
	     		+ "am_assetTransfer.description AS am_assetTransfer_description,\n"
	     		+ "am_assetTransfer.cost_price AS am_assetTransfer_cost_price,\n"
	     		+ "am_assetTransfer.Accum_Dep AS am_assetTransfer_Accum_Dep,\n"
	     		+ "am_assetTransfer.Monthly_Dep AS am_assetTransfer_Monthly_Dep,\n"
	     		+ "am_assetTransfer.NEW_BRANCH_CODE AS am_assetTransfer_NEW_BRANCH_CODE,\n"
	     		+ "am_assetTransfer.NEW_SECTION_CODE AS am_assetTransfer_NEW_SECTION_CODE,\n"
	     		+ "am_assetTransfer.NEW_DEPT_CODE AS am_assetTransfer_NEW_DEPT_CODE,\n"
	     		+ "am_assetTransfer.NBV AS am_assetTransfer_NBV,\n"
	     		+ "am_assetTransfer.New_Asset_id AS am_assetTransfer_New_Asset_id,\n"
	     		+ "am_ad_branch_A.BRANCH_ID AS am_ad_branch_A_BRANCH_ID\n"
	     		+ "FROM \n"
	     		+ "dbo.am_assetTransfer am_assetTransfer \n"
	     		+ "INNER JOIN dbo.am_ad_branch am_ad_branch_A ON am_assetTransfer.OLD_BRANCH_CODE = am_ad_branch_A.BRANCH_CODE\n"
	     		+ "INNER JOIN dbo.am_ad_category am_ad_category ON am_assetTransfer.OLD_CATEGORY_CODE = am_ad_category.category_code,\n"
	     		+ "dbo.am_gb_company am_gb_company\n"
	     		+ "WHERE \n"
	     		+ "am_assetTransfer.approval_status = 'POSTED'\n"
	     		+ "and am_ad_category.category_id = ? \n"
	     		+ "AND Transfer_Date BETWEEN ? AND ? \n"
	     		+ "UNION \n"
	     		+ "SELECT \n"
	     		+ "am_gb_company.company_name AS am_gb_company_company_name,\n"
	     		+ "am_ad_category.category_name AS am_ad_category_category_name,\n"
	     		+ "am_ad_category.Dep_rate AS am_ad_category_Dep_rate,\n"
	     		+ "am_ad_category.Accum_Dep_ledger AS am_ad_category_Accum_Dep_ledger,\n"
	     		+ "am_ad_category.Dep_ledger AS am_ad_category_Dep_ledger,\n"
	     		+ "am_ad_category.Asset_Ledger AS am_ad_category_Asset_Ledger,\n"
	     		+ "am_ad_category.gl_account AS am_ad_category_gl_account,\n"
	     		+ "am_gb_bulkTransfer.asset_id AS am_gb_bulkTransfer_asset_id,\n"
	     		+ "am_gb_bulkTransfer.OLDbranch_Id AS am_gb_bulkTransfer_OLD_branch_Id,\n"
	     		+ "am_gb_bulkTransfer.OLDdept_ID AS am_gb_bulkTransfer_OLD_dept_ID,\n"
	     		+ "am_gb_bulkTransfer.Transfer_Date AS am_gb_bulkTransfer_Transfer_Date,\n"
	     		+ "am_gb_bulkTransfer.OLDAsset_user AS am_gb_bulkTransfer_OLD_Asset_user,\n"
	     		+ "am_gb_bulkTransfer.OLDSection_ID AS am_gb_bulkTransfer_OLD_Section,\n"
	     		+ "'',\n"
	     		+ "'',\n"
	     		+ "'',\n"
	     		+ "am_ad_branch_A.BRANCH_NAME AS am_ad_branch_A_BRANCH_NAME,\n"
	     		+ "am_gb_bulkTransfer.STATUS AS am_gb_bulkTransfer_approval_status,\n"
	     		+ "'',\n"
	     		+ "am_gb_bulkTransfer.description AS am_gb_bulkTransfer_description,\n"
	     		+ "am_gb_bulkTransfer.cost_price AS am_gb_bulkTransfer_cost_price,\n"
	     		+ "am_gb_bulkTransfer.Accum_Dep AS am_gb_bulkTransfer_Accum_Dep,\n"
	     		+ "am_gb_bulkTransfer.Monthly_Dep AS am_gb_bulkTransfer_Monthly_Dep,\n"
	     		+ "am_gb_bulkTransfer.NEW_BRANCH_CODE AS am_gb_bulkTransfer_NEW_BRANCH_CODE,\n"
	     		+ "am_gb_bulkTransfer.NEW_SECTION_CODE AS am_gb_bulkTransfer_NEW_SECTION_CODE,\n"
	     		+ "am_gb_bulkTransfer.NEW_DEPT_CODE AS am_gb_bulkTransfer_NEW_DEPT_CODE,\n"
	     		+ "am_gb_bulkTransfer.NBV AS am_gb_bulkTransfer_NBV,\n"
	     		+ "am_gb_bulkTransfer.New_Asset_id AS am_gb_bulkTransfer_New_Asset_id,\n"
	     		+ "am_ad_branch_A.BRANCH_ID AS am_ad_branch_A_BRANCH_ID\n"
	     		+ "FROM\n"
	     		+ "dbo.am_gb_bulkTransfer am_gb_bulkTransfer \n"
	     		+ "INNER JOIN dbo.am_ad_branch am_ad_branch_A ON am_gb_bulkTransfer.OLDBRANCH_ID = am_ad_branch_A.BRANCH_ID\n"
	     		+ "INNER JOIN dbo.am_ad_category am_ad_category ON am_gb_bulkTransfer.CATEGORY_ID = am_ad_category.category_ID,\n"
	     		+ "dbo.am_gb_company am_gb_company\n"
	     		+ "WHERE \n"
	     		+ "am_gb_bulkTransfer.STATUS = 'POSTED'\n"
	     		+ "and am_ad_category.category_id = ? \n"
	     		+ "AND Transfer_Date BETWEEN ? AND ?  \n"
	     		+ "ORDER BY \n"
	     		+ "am_assetTransfer.OLD_BRANCH_CODE ASC,\n"
	     		+ "am_assetTransfer.OLD_CATEGORY_CODE ASC\n"
	     		+ "";    
		}      
	 
	 if(branch_Id.equals("0")  && !categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")){	   
		   System.out.println("======>>>>>>>Category Selected: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate);
		     ColQuery ="SELECT\n"
		     		+ "am_gb_company.company_name AS am_gb_company_company_name,\n"
		     		+ "am_ad_category.category_name AS am_ad_category_category_name,\n"
		     		+ "am_ad_category.Dep_rate AS am_ad_category_Dep_rate,\n"
		     		+ "am_ad_category.Accum_Dep_ledger AS am_ad_category_Accum_Dep_ledger,\n"
		     		+ "am_ad_category.Dep_ledger AS am_ad_category_Dep_ledger,\n"
		     		+ "am_ad_category.Asset_Ledger AS am_ad_category_Asset_Ledger,\n"
		     		+ "am_ad_category.gl_account AS am_ad_category_gl_account,\n"
		     		+ "am_assetTransfer.asset_id AS am_assetTransfer_asset_id,\n"
		     		+ "am_assetTransfer.OLD_branch_Id AS am_assetTransfer_OLD_branch_Id,\n"
		     		+ "am_assetTransfer.OLD_dept_ID AS am_assetTransfer_OLD_dept_ID,\n"
		     		+ "am_assetTransfer.Transfer_Date AS am_assetTransfer_Transfer_Date,\n"
		     		+ "am_assetTransfer.OLD_Asset_user AS am_assetTransfer_OLD_Asset_user,\n"
		     		+ "am_assetTransfer.OLD_Section AS am_assetTransfer_OLD_Section,\n"
		     		+ "am_assetTransfer.OLD_BRANCH_CODE AS am_assetTransfer_OLD_BRANCH_CODE,\n"
		     		+ "am_assetTransfer.OLD_SECTION_CODE AS am_assetTransfer_OLD_SECTION_CODE,\n"
		     		+ "am_assetTransfer.OLD_DEPT_CODE AS am_assetTransfer_OLD_DEPT_CODE,\n"
		     		+ "am_ad_branch_A.BRANCH_NAME AS am_ad_branch_A_BRANCH_NAME,\n"
		     		+ "am_assetTransfer.approval_status AS am_assetTransfer_approval_status,\n"
		     		+ "am_assetTransfer.OLD_CATEGORY_CODE AS am_assetTransfer_OLD_CATEGORY_CODE,\n"
		     		+ "am_assetTransfer.description AS am_assetTransfer_description,\n"
		     		+ "am_assetTransfer.cost_price AS am_assetTransfer_cost_price,\n"
		     		+ "am_assetTransfer.Accum_Dep AS am_assetTransfer_Accum_Dep,\n"
		     		+ "am_assetTransfer.Monthly_Dep AS am_assetTransfer_Monthly_Dep,\n"
		     		+ "am_assetTransfer.NEW_BRANCH_CODE AS am_assetTransfer_NEW_BRANCH_CODE,\n"
		     		+ "am_assetTransfer.NEW_SECTION_CODE AS am_assetTransfer_NEW_SECTION_CODE,\n"
		     		+ "am_assetTransfer.NEW_DEPT_CODE AS am_assetTransfer_NEW_DEPT_CODE,\n"
		     		+ "am_assetTransfer.NBV AS am_assetTransfer_NBV,\n"
		     		+ "am_assetTransfer.New_Asset_id AS am_assetTransfer_New_Asset_id,\n"
		     		+ "am_ad_branch_A.BRANCH_ID AS am_ad_branch_A_BRANCH_ID\n"
		     		+ "FROM \n"
		     		+ "dbo.am_assetTransfer am_assetTransfer \n"
		     		+ "INNER JOIN dbo.am_ad_branch am_ad_branch_A ON am_assetTransfer.OLD_BRANCH_CODE = am_ad_branch_A.BRANCH_CODE\n"
		     		+ "INNER JOIN dbo.am_ad_category am_ad_category ON am_assetTransfer.OLD_CATEGORY_CODE = am_ad_category.category_code,\n"
		     		+ "dbo.am_gb_company am_gb_company\n"
		     		+ "WHERE \n"
		     		+ "am_assetTransfer.approval_status = 'POSTED'\n"
		     		+ "and am_ad_category.category_id = ? \n"
		     		+ "UNION \n"
		     		+ "SELECT \n"
		     		+ "am_gb_company.company_name AS am_gb_company_company_name,\n"
		     		+ "am_ad_category.category_name AS am_ad_category_category_name,\n"
		     		+ "am_ad_category.Dep_rate AS am_ad_category_Dep_rate,\n"
		     		+ "am_ad_category.Accum_Dep_ledger AS am_ad_category_Accum_Dep_ledger,\n"
		     		+ "am_ad_category.Dep_ledger AS am_ad_category_Dep_ledger,\n"
		     		+ "am_ad_category.Asset_Ledger AS am_ad_category_Asset_Ledger,\n"
		     		+ "am_ad_category.gl_account AS am_ad_category_gl_account,\n"
		     		+ "am_gb_bulkTransfer.asset_id AS am_gb_bulkTransfer_asset_id,\n"
		     		+ "am_gb_bulkTransfer.OLDbranch_Id AS am_gb_bulkTransfer_OLD_branch_Id,\n"
		     		+ "am_gb_bulkTransfer.OLDdept_ID AS am_gb_bulkTransfer_OLD_dept_ID,\n"
		     		+ "am_gb_bulkTransfer.Transfer_Date AS am_gb_bulkTransfer_Transfer_Date,\n"
		     		+ "am_gb_bulkTransfer.OLDAsset_user AS am_gb_bulkTransfer_OLD_Asset_user,\n"
		     		+ "am_gb_bulkTransfer.OLDSection_ID AS am_gb_bulkTransfer_OLD_Section,\n"
		     		+ "'',\n"
		     		+ "'',\n"
		     		+ "'',\n"
		     		+ "am_ad_branch_A.BRANCH_NAME AS am_ad_branch_A_BRANCH_NAME,\n"
		     		+ "am_gb_bulkTransfer.STATUS AS am_gb_bulkTransfer_approval_status,\n"
		     		+ "'',\n"
		     		+ "am_gb_bulkTransfer.description AS am_gb_bulkTransfer_description,\n"
		     		+ "am_gb_bulkTransfer.cost_price AS am_gb_bulkTransfer_cost_price,\n"
		     		+ "am_gb_bulkTransfer.Accum_Dep AS am_gb_bulkTransfer_Accum_Dep,\n"
		     		+ "am_gb_bulkTransfer.Monthly_Dep AS am_gb_bulkTransfer_Monthly_Dep,\n"
		     		+ "am_gb_bulkTransfer.NEW_BRANCH_CODE AS am_gb_bulkTransfer_NEW_BRANCH_CODE,\n"
		     		+ "am_gb_bulkTransfer.NEW_SECTION_CODE AS am_gb_bulkTransfer_NEW_SECTION_CODE,\n"
		     		+ "am_gb_bulkTransfer.NEW_DEPT_CODE AS am_gb_bulkTransfer_NEW_DEPT_CODE,\n"
		     		+ "am_gb_bulkTransfer.NBV AS am_gb_bulkTransfer_NBV,\n"
		     		+ "am_gb_bulkTransfer.New_Asset_id AS am_gb_bulkTransfer_New_Asset_id,\n"
		     		+ "am_ad_branch_A.BRANCH_ID AS am_ad_branch_A_BRANCH_ID\n"
		     		+ "FROM \n"
		     		+ "dbo.am_gb_bulkTransfer am_gb_bulkTransfer \n"
		     		+ "INNER JOIN dbo.am_ad_branch am_ad_branch_A ON am_gb_bulkTransfer.OLDBRANCH_ID = am_ad_branch_A.BRANCH_ID\n"
		     		+ "INNER JOIN dbo.am_ad_category am_ad_category ON am_gb_bulkTransfer.CATEGORY_ID = am_ad_category.category_ID,\n"
		     		+ "dbo.am_gb_company am_gb_company\n"
		     		+ "WHERE \n"
		     		+ "am_gb_bulkTransfer.STATUS = 'POSTED'\n"
		     		+ "and am_ad_category.category_id = ? \n"
		     		+ "ORDER BY \n"
		     		+ "am_assetTransfer.OLD_BRANCH_CODE ASC,\n"
		     		+ "am_assetTransfer.OLD_CATEGORY_CODE ASC\n"
		     		+ "";    
			}      
	 
   if(!branch_Id.equals("0")  && !categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")){
	   System.out.println("======>>>>>>>Branch and Category and Date Selected: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate);
	     ColQuery ="SELECT\n"
	     		+ "am_gb_company.company_name AS am_gb_company_company_name,\n"
	     		+ "am_ad_category.category_name AS am_ad_category_category_name,\n"
	     		+ "am_ad_category.Dep_rate AS am_ad_category_Dep_rate,\n"
	     		+ "am_ad_category.Accum_Dep_ledger AS am_ad_category_Accum_Dep_ledger,\n"
	     		+ "am_ad_category.Dep_ledger AS am_ad_category_Dep_ledger,\n"
	     		+ "am_ad_category.Asset_Ledger AS am_ad_category_Asset_Ledger,\n"
	     		+ "am_ad_category.gl_account AS am_ad_category_gl_account,\n"
	     		+ "am_assetTransfer.asset_id AS am_assetTransfer_asset_id,\n"
	     		+ "am_assetTransfer.OLD_branch_Id AS am_assetTransfer_OLD_branch_Id,\n"
	     		+ "am_assetTransfer.OLD_dept_ID AS am_assetTransfer_OLD_dept_ID,\n"
	     		+ "am_assetTransfer.Transfer_Date AS am_assetTransfer_Transfer_Date,\n"
	     		+ "am_assetTransfer.OLD_Asset_user AS am_assetTransfer_OLD_Asset_user,\n"
	     		+ "am_assetTransfer.OLD_Section AS am_assetTransfer_OLD_Section,\n"
	     		+ "am_assetTransfer.OLD_BRANCH_CODE AS am_assetTransfer_OLD_BRANCH_CODE,\n"
	     		+ "am_assetTransfer.OLD_SECTION_CODE AS am_assetTransfer_OLD_SECTION_CODE,\n"
	     		+ "am_assetTransfer.OLD_DEPT_CODE AS am_assetTransfer_OLD_DEPT_CODE,\n"
	     		+ "am_ad_branch_A.BRANCH_NAME AS am_ad_branch_A_BRANCH_NAME,\n"
	     		+ "am_assetTransfer.approval_status AS am_assetTransfer_approval_status,\n"
	     		+ "am_assetTransfer.OLD_CATEGORY_CODE AS am_assetTransfer_OLD_CATEGORY_CODE,\n"
	     		+ "am_assetTransfer.description AS am_assetTransfer_description,\n"
	     		+ "am_assetTransfer.cost_price AS am_assetTransfer_cost_price,\n"
	     		+ "am_assetTransfer.Accum_Dep AS am_assetTransfer_Accum_Dep,\n"
	     		+ "am_assetTransfer.Monthly_Dep AS am_assetTransfer_Monthly_Dep,\n"
	     		+ "am_assetTransfer.NEW_BRANCH_CODE AS am_assetTransfer_NEW_BRANCH_CODE,\n"
	     		+ "am_assetTransfer.NEW_SECTION_CODE AS am_assetTransfer_NEW_SECTION_CODE,\n"
	     		+ "am_assetTransfer.NEW_DEPT_CODE AS am_assetTransfer_NEW_DEPT_CODE,\n"
	     		+ "am_assetTransfer.NBV AS am_assetTransfer_NBV,\n"
	     		+ "am_assetTransfer.New_Asset_id AS am_assetTransfer_New_Asset_id,\n"
	     		+ "am_ad_branch_A.BRANCH_ID AS am_ad_branch_A_BRANCH_ID\n"
	     		+ "FROM\n"
	     		+ "dbo.am_assetTransfer am_assetTransfer \n"
	     		+ "INNER JOIN dbo.am_ad_branch am_ad_branch_A ON am_assetTransfer.OLD_BRANCH_CODE = am_ad_branch_A.BRANCH_CODE\n"
	     		+ "INNER JOIN dbo.am_ad_category am_ad_category ON am_assetTransfer.OLD_CATEGORY_CODE = am_ad_category.category_code,\n"
	     		+ "dbo.am_gb_company am_gb_company\n"
	     		+ "WHERE\n"
	     		+ "am_assetTransfer.approval_status = 'POSTED'\n"
	     		+ "and am_ad_category.category_id = ?  \n"
	     		+ "and am_ad_branch_A.BRANCH_ID =? \n"
	     		+ "AND Transfer_Date BETWEEN ? AND ? \n"
	     		+ "UNION\n"
	     		+ "SELECT\n"
	     		+ "am_gb_company.company_name AS am_gb_company_company_name,\n"
	     		+ "am_ad_category.category_name AS am_ad_category_category_name,\n"
	     		+ "am_ad_category.Dep_rate AS am_ad_category_Dep_rate,\n"
	     		+ "am_ad_category.Accum_Dep_ledger AS am_ad_category_Accum_Dep_ledger,\n"
	     		+ "am_ad_category.Dep_ledger AS am_ad_category_Dep_ledger,\n"
	     		+ "am_ad_category.Asset_Ledger AS am_ad_category_Asset_Ledger,\n"
	     		+ "am_ad_category.gl_account AS am_ad_category_gl_account,\n"
	     		+ "am_gb_bulkTransfer.asset_id AS am_gb_bulkTransfer_asset_id,\n"
	     		+ "am_gb_bulkTransfer.OLDbranch_Id AS am_gb_bulkTransfer_OLD_branch_Id,\n"
	     		+ "am_gb_bulkTransfer.OLDdept_ID AS am_gb_bulkTransfer_OLD_dept_ID,\n"
	     		+ "am_gb_bulkTransfer.Transfer_Date AS am_gb_bulkTransfer_Transfer_Date,\n"
	     		+ "am_gb_bulkTransfer.OLDAsset_user AS am_gb_bulkTransfer_OLD_Asset_user,\n"
	     		+ "am_gb_bulkTransfer.OLDSection_ID AS am_gb_bulkTransfer_OLD_Section,\n"
	     		+ "'',\n"
	     		+ "'',\n"
	     		+ "'',\n"
	     		+ "am_ad_branch_A.BRANCH_NAME AS am_ad_branch_A_BRANCH_NAME,\n"
	     		+ "am_gb_bulkTransfer.STATUS AS am_gb_bulkTransfer_approval_status,\n"
	     		+ "'',\n"
	     		+ "am_gb_bulkTransfer.description AS am_gb_bulkTransfer_description,\n"
	     		+ "am_gb_bulkTransfer.cost_price AS am_gb_bulkTransfer_cost_price,\n"
	     		+ "am_gb_bulkTransfer.Accum_Dep AS am_gb_bulkTransfer_Accum_Dep,\n"
	     		+ "am_gb_bulkTransfer.Monthly_Dep AS am_gb_bulkTransfer_Monthly_Dep,\n"
	     		+ "am_gb_bulkTransfer.NEW_BRANCH_CODE AS am_gb_bulkTransfer_NEW_BRANCH_CODE,\n"
	     		+ "am_gb_bulkTransfer.NEW_SECTION_CODE AS am_gb_bulkTransfer_NEW_SECTION_CODE,\n"
	     		+ "am_gb_bulkTransfer.NEW_DEPT_CODE AS am_gb_bulkTransfer_NEW_DEPT_CODE,\n"
	     		+ "am_gb_bulkTransfer.NBV AS am_gb_bulkTransfer_NBV,\n"
	     		+ "am_gb_bulkTransfer.New_Asset_id AS am_gb_bulkTransfer_New_Asset_id,\n"
	     		+ "am_ad_branch_A.BRANCH_ID AS am_ad_branch_A_BRANCH_ID\n"
	     		+ "FROM\n"
	     		+ "dbo.am_gb_bulkTransfer am_gb_bulkTransfer \n"
	     		+ "INNER JOIN dbo.am_ad_branch am_ad_branch_A ON am_gb_bulkTransfer.OLDBRANCH_ID = am_ad_branch_A.BRANCH_ID\n"
	     		+ "INNER JOIN dbo.am_ad_category am_ad_category ON am_gb_bulkTransfer.CATEGORY_ID = am_ad_category.category_ID,\n"
	     		+ "dbo.am_gb_company am_gb_company\n"
	     		+ "WHERE\n"
	     		+ "am_gb_bulkTransfer.STATUS = 'POSTED'\n"
	     		+ "and am_ad_category.category_id = ? \n"
	     		+ "and am_ad_branch_A.BRANCH_ID =? \n"
	     		+ "AND Transfer_Date BETWEEN ? AND ?  \n"
	     		+ "ORDER BY\n"
	     		+ "am_assetTransfer.OLD_BRANCH_CODE ASC,\n"
	     		+ "am_assetTransfer.OLD_CATEGORY_CODE ASC\n"
	     		+ "";    
		}      
//   System.out.println("======>>>>>>>ColQuery: "+ColQuery);
     java.util.ArrayList list =rep.getAssetTransferRecords(ColQuery,branch_Id,categoryCode,FromDate,ToDate,asset_Id);
     System.out.println("======>>>>>>>list size: "+list.size()+"        =====report: "+report);
     if(list.size()!=0){
   	 if(report.equalsIgnoreCase("rptMenuBCTRANS")){
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
    	 
			String categoryName = newassettrans.getCategoryName();
			double depRate = newassettrans.getDepRate();
			String accumDepLedger = newassettrans.getAccumDepLedger();
			String depLedger = newassettrans.getDepLedger();
			String assetLedger = newassettrans.getAssetLedger();
			String gl_Account = newassettrans.getGlAccount();
			String assetId = newassettrans.getAssetId();
			String oldBranchId = newassettrans.getOldBranchId();
			String oldDeptId = newassettrans.getOldDeptId();
			String transferDate = newassettrans.getTransDate() != null ? getDate(newassettrans.getTransDate()) : "";
			String oldAssetUser = newassettrans.getAssetUser();
			String oldSection = newassettrans.getOldSection();
			String oldBranchCode = newassettrans.getOldBranchCode();
			String oldSectionCode = newassettrans.getOldSectionCode();
			String oldDeptCode = newassettrans.getOldDeptCode();
			String branchName = newassettrans.getBranchName();
			String approvalStatus = newassettrans.getApprovalStatus();
			String oldCategoryCode = newassettrans.getOldCategoryCode();
			String description = newassettrans.getDescription();
			double costPrice = newassettrans.getCostPrice();
			double accumDepr = newassettrans.getAccumDep();
			double monthlyDepr = newassettrans.getMonthlyDep();
			String newBranchCode = newassettrans.getNewBranchCode();
            String newSectionCode = newassettrans.getNewSectionCode();
            String newDeptCode = newassettrans.getNewDeptCode();
            double nbv = newassettrans.getNbv();
            String newAssetId = newassettrans.getNewAssetId();
            String branchId = newassettrans.getBranchId();

			//			String vendorName = records.getCodeName("select VENDOR_NAME from am_ad_vendor where VENDOR_ID = "+vendorId+"");
			

			Row row = sheet.createRow((int) i);

			row.createCell((short) 0).setCellValue(i);
			row.createCell((short) 1).setCellValue(categoryName);
            row.createCell((short) 2).setCellValue(depRate);
            row.createCell((short) 3).setCellValue(accumDepLedger);
            row.createCell((short) 4).setCellValue(depLedger);
            row.createCell((short) 5).setCellValue(assetLedger);
            row.createCell((short) 6).setCellValue(gl_Account);
            row.createCell((short) 7).setCellValue(assetId);
            row.createCell((short) 8).setCellValue(oldBranchId);
            row.createCell((short) 9).setCellValue(oldDeptId);
			row.createCell((short) 10).setCellValue(transferDate);
			row.createCell((short) 11).setCellValue(oldAssetUser);
			row.createCell((short) 12).setCellValue(oldSection);
			row.createCell((short) 13).setCellValue(oldBranchCode);
			row.createCell((short) 14).setCellValue(oldSectionCode);
			row.createCell((short) 15).setCellValue(oldDeptCode);
			row.createCell((short) 16).setCellValue(branchName);
            row.createCell((short) 17).setCellValue(approvalStatus);
            row.createCell((short) 18).setCellValue(oldCategoryCode);
            row.createCell((short) 19).setCellValue(description);
            row.createCell((short) 20).setCellValue(costPrice);
            row.createCell((short) 21).setCellValue(accumDepr);
            row.createCell((short) 22).setCellValue(monthlyDepr);
            row.createCell((short) 23).setCellValue(newBranchCode);
            row.createCell((short) 24).setCellValue(newSectionCode);
            row.createCell((short) 25).setCellValue(newDeptCode);
            row.createCell((short) 26).setCellValue(nbv);
            row.createCell((short) 27).setCellValue(newAssetId);
            row.createCell((short) 28).setCellValue(branchId);

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