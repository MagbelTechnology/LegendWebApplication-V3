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
   
public class WipReclassificationReportExport extends HttpServlet
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
    if(report.equalsIgnoreCase("rptMenuBCWIP")){fileName = branchCode+"By"+userName+"WipReclassificationReport.xlsx";}
    
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
  	     		+ "     AM_WIP_RECLASSIFICATION.\"Asset_id\" AS AM_WIP_RECLASSIFICATION_Asset_id,\n"
  	     		+ "     AM_ASSET.\"Description\" AS AM_ASSET_Description,\n"
  	     		+ "     AM_ASSET.\"Cost_Price\" AS AM_ASSET_Cost_Price,\n"
  	     		+ "     AM_ASSET.\"Accum_Dep\" AS AM_ASSET_Accum_Dep,\n"
  	     		+ "     AM_ASSET.\"Monthly_Dep\" AS AM_ASSET_Monthly_Dep,\n"
  	     		+ "     AM_ASSET.\"NBV\" AS AM_ASSET_NBV,\n"
  	     		+ "     AM_ASSET.\"Date_purchased\" AS AM_ASSET_Date_purchased,\n"
  	     		+ "     AM_ASSET.\"Posting_Date\" AS AM_ASSET_Posting_Date,\n"
  	     		+ "     AM_ASSET.\"Effective_Date\" AS AM_ASSET_Effective_Date,\n"
  	     		+ "     AM_ASSET.\"BRANCH_CODE\" AS AM_ASSET_BRANCH_CODE,\n"
  	     		+ "     AM_ASSET.\"CATEGORY_CODE\" AS AM_ASSET_CATEGORY_CODE,\n"
  	     		+ "     am_gb_company.\"company_name\" AS am_gb_company_company_name,\n"
  	     		+ "     AM_ASSET.\"Asset_id\" AS AM_ASSET_Asset_id,\n"
  	     		+ "     AM_ASSET.\"OLD_ASSET_ID\" AS AM_ASSET_OLD_ASSET_ID,\n"
  	     		+ "     am_ad_branch.\"BRANCH_NAME\" AS am_ad_branch_BRANCH_NAME,\n"
  	     		+ "     AM_ASSET.\"Asset_Status\" AS AM_ASSET_Asset_Status\n"
  	     		+ "FROM\n"
  	     		+ "     \"dbo\".\"AM_WIP_RECLASSIFICATION\" AM_WIP_RECLASSIFICATION INNER JOIN \"dbo\".\"am_ad_branch\" am_ad_branch ON AM_WIP_RECLASSIFICATION.\"NEW_BRANCH_CODE\" = am_ad_branch.\"BRANCH_CODE\"\n"
  	     		+ "     INNER JOIN \"dbo\".\"AM_ASSET\" AM_ASSET ON AM_WIP_RECLASSIFICATION.\"Asset_id\" = AM_ASSET.\"OLD_ASSET_ID\",\n"
  	     		+ "     \"dbo\".\"am_gb_company\" am_gb_company\n"
  	     		+ "where AM_ASSET.\"Asset_Status\" = 'ACTIVE'";    
  		}
     
     
     if(branch_Id.equals("0")  && categoryCode.equals("0")  && !FromDate.equals("") && !ToDate.equals("")){
    	 System.out.println("======>>>>>>>Date Selected: ");	     
	     ColQuery ="SELECT\n"
	     		+ "     AM_WIP_RECLASSIFICATION.\"Asset_id\" AS AM_WIP_RECLASSIFICATION_Asset_id,\n"
	     		+ "     AM_ASSET.\"Description\" AS AM_ASSET_Description,\n"
	     		+ "     AM_ASSET.\"Cost_Price\" AS AM_ASSET_Cost_Price,\n"
	     		+ "     AM_ASSET.\"Accum_Dep\" AS AM_ASSET_Accum_Dep,\n"
	     		+ "     AM_ASSET.\"Monthly_Dep\" AS AM_ASSET_Monthly_Dep,\n"
	     		+ "     AM_ASSET.\"NBV\" AS AM_ASSET_NBV,\n"
	     		+ "     AM_ASSET.\"Date_purchased\" AS AM_ASSET_Date_purchased,\n"
	     		+ "     AM_ASSET.\"Posting_Date\" AS AM_ASSET_Posting_Date,\n"
	     		+ "     AM_ASSET.\"Effective_Date\" AS AM_ASSET_Effective_Date,\n"
	     		+ "     AM_ASSET.\"BRANCH_CODE\" AS AM_ASSET_BRANCH_CODE,\n"
	     		+ "     AM_ASSET.\"CATEGORY_CODE\" AS AM_ASSET_CATEGORY_CODE,\n"
	     		+ "     am_gb_company.\"company_name\" AS am_gb_company_company_name,\n"
	     		+ "     AM_ASSET.\"Asset_id\" AS AM_ASSET_Asset_id,\n"
	     		+ "     AM_ASSET.\"OLD_ASSET_ID\" AS AM_ASSET_OLD_ASSET_ID,\n"
	     		+ "     am_ad_branch.\"BRANCH_NAME\" AS am_ad_branch_BRANCH_NAME,\n"
	     		+ "     AM_ASSET.\"Asset_Status\" AS AM_ASSET_Asset_Status\n"
	     		+ "FROM\n"
	     		+ "     \"dbo\".\"AM_WIP_RECLASSIFICATION\" AM_WIP_RECLASSIFICATION INNER JOIN \"dbo\".\"am_ad_branch\" am_ad_branch ON AM_WIP_RECLASSIFICATION.\"NEW_BRANCH_CODE\" = am_ad_branch.\"BRANCH_CODE\"\n"
	     		+ "     INNER JOIN \"dbo\".\"AM_ASSET\" AM_ASSET ON AM_WIP_RECLASSIFICATION.\"Asset_id\" = AM_ASSET.\"OLD_ASSET_ID\",\n"
	     		+ "     \"dbo\".\"am_gb_company\" am_gb_company\n"
	     		+ "where AM_ASSET.\"Asset_Status\" = 'ACTIVE'\n"
	     		+ "AND transfer_date BETWEEN ? and ?";  
	}      
	 if(!branch_Id.equals("0")  && categoryCode.equals("0")  && !FromDate.equals("") && !ToDate.equals("")){	   
	   System.out.println("======>>>>>>>Branch and Date Selected: ");
	     ColQuery ="SELECT\n"
	     		+ "     AM_WIP_RECLASSIFICATION.\"Asset_id\" AS AM_WIP_RECLASSIFICATION_Asset_id,\n"
	     		+ "     AM_ASSET.\"Description\" AS AM_ASSET_Description,\n"
	     		+ "     AM_ASSET.\"Cost_Price\" AS AM_ASSET_Cost_Price,\n"
	     		+ "     AM_ASSET.\"Accum_Dep\" AS AM_ASSET_Accum_Dep,\n"
	     		+ "     AM_ASSET.\"Monthly_Dep\" AS AM_ASSET_Monthly_Dep,\n"
	     		+ "     AM_ASSET.\"NBV\" AS AM_ASSET_NBV,\n"
	     		+ "     AM_ASSET.\"Date_purchased\" AS AM_ASSET_Date_purchased,\n"
	     		+ "     AM_ASSET.\"Posting_Date\" AS AM_ASSET_Posting_Date,\n"
	     		+ "     AM_ASSET.\"Effective_Date\" AS AM_ASSET_Effective_Date,\n"
	     		+ "     AM_ASSET.\"BRANCH_CODE\" AS AM_ASSET_BRANCH_CODE,\n"
	     		+ "     AM_ASSET.\"CATEGORY_CODE\" AS AM_ASSET_CATEGORY_CODE,\n"
	     		+ "     am_gb_company.\"company_name\" AS am_gb_company_company_name,\n"
	     		+ "     AM_ASSET.\"Asset_id\" AS AM_ASSET_Asset_id,\n"
	     		+ "     AM_ASSET.\"OLD_ASSET_ID\" AS AM_ASSET_OLD_ASSET_ID,\n"
	     		+ "     am_ad_branch.\"BRANCH_NAME\" AS am_ad_branch_BRANCH_NAME,\n"
	     		+ "     AM_ASSET.\"Asset_Status\" AS AM_ASSET_Asset_Status\n"
	     		+ "FROM\n"
	     		+ "     \"dbo\".\"AM_WIP_RECLASSIFICATION\" AM_WIP_RECLASSIFICATION INNER JOIN \"dbo\".\"am_ad_branch\" am_ad_branch ON AM_WIP_RECLASSIFICATION.\"NEW_BRANCH_CODE\" = am_ad_branch.\"BRANCH_CODE\"\n"
	     		+ "     INNER JOIN \"dbo\".\"AM_ASSET\" AM_ASSET ON AM_WIP_RECLASSIFICATION.\"Asset_id\" = AM_ASSET.\"OLD_ASSET_ID\",\n"
	     		+ "     \"dbo\".\"am_gb_company\" am_gb_company\n"
	     		+ "where AM_ASSET.\"Asset_Status\" = 'ACTIVE'\n"
	     		+ "AND am_ad_branch.branch_id = ?\n"
	     		+ "AND transfer_date BETWEEN ? and ?";   
		}  
	 
	 if(!branch_Id.equals("0")  && categoryCode.equals("0")  && FromDate.equals("") && ToDate.equals("")){	   
		   System.out.println("======>>>>>>>Branch Selected: ");
		     ColQuery ="SELECT\n"
		     		+ "     AM_WIP_RECLASSIFICATION.\"Asset_id\" AS AM_WIP_RECLASSIFICATION_Asset_id,\n"
		     		+ "     AM_ASSET.\"Description\" AS AM_ASSET_Description,\n"
		     		+ "     AM_ASSET.\"Cost_Price\" AS AM_ASSET_Cost_Price,\n"
		     		+ "     AM_ASSET.\"Accum_Dep\" AS AM_ASSET_Accum_Dep,\n"
		     		+ "     AM_ASSET.\"Monthly_Dep\" AS AM_ASSET_Monthly_Dep,\n"
		     		+ "     AM_ASSET.\"NBV\" AS AM_ASSET_NBV,\n"
		     		+ "     AM_ASSET.\"Date_purchased\" AS AM_ASSET_Date_purchased,\n"
		     		+ "     AM_ASSET.\"Posting_Date\" AS AM_ASSET_Posting_Date,\n"
		     		+ "     AM_ASSET.\"Effective_Date\" AS AM_ASSET_Effective_Date,\n"
		     		+ "     AM_ASSET.\"BRANCH_CODE\" AS AM_ASSET_BRANCH_CODE,\n"
		     		+ "     AM_ASSET.\"CATEGORY_CODE\" AS AM_ASSET_CATEGORY_CODE,\n"
		     		+ "     am_gb_company.\"company_name\" AS am_gb_company_company_name,\n"
		     		+ "     AM_ASSET.\"Asset_id\" AS AM_ASSET_Asset_id,\n"
		     		+ "     AM_ASSET.\"OLD_ASSET_ID\" AS AM_ASSET_OLD_ASSET_ID,\n"
		     		+ "     am_ad_branch.\"BRANCH_NAME\" AS am_ad_branch_BRANCH_NAME,\n"
		     		+ "     AM_ASSET.\"Asset_Status\" AS AM_ASSET_Asset_Status\n"
		     		+ "FROM\n"
		     		+ "     \"dbo\".\"AM_WIP_RECLASSIFICATION\" AM_WIP_RECLASSIFICATION INNER JOIN \"dbo\".\"am_ad_branch\" am_ad_branch ON AM_WIP_RECLASSIFICATION.\"NEW_BRANCH_CODE\" = am_ad_branch.\"BRANCH_CODE\"\n"
		     		+ "     INNER JOIN \"dbo\".\"AM_ASSET\" AM_ASSET ON AM_WIP_RECLASSIFICATION.\"Asset_id\" = AM_ASSET.\"OLD_ASSET_ID\",\n"
		     		+ "     \"dbo\".\"am_gb_company\" am_gb_company\n"
		     		+ "where AM_ASSET.\"Asset_Status\" = 'ACTIVE'\n"
		     		+ "AND am_ad_branch.branch_id = ?";   
			}      

	 if(branch_Id.equals("0")  && !categoryCode.equals("0")  && !FromDate.equals("") && !ToDate.equals("")){	   
	   System.out.println("======>>>>>>>Category and Date Selected: ");
	     ColQuery ="SELECT\n"
	     		+ "     AM_WIP_RECLASSIFICATION.\"Asset_id\" AS AM_WIP_RECLASSIFICATION_Asset_id,\n"
	     		+ "     AM_ASSET.\"Description\" AS AM_ASSET_Description,\n"
	     		+ "     AM_ASSET.\"Cost_Price\" AS AM_ASSET_Cost_Price,\n"
	     		+ "     AM_ASSET.\"Accum_Dep\" AS AM_ASSET_Accum_Dep,\n"
	     		+ "     AM_ASSET.\"Monthly_Dep\" AS AM_ASSET_Monthly_Dep,\n"
	     		+ "     AM_ASSET.\"NBV\" AS AM_ASSET_NBV,\n"
	     		+ "     AM_ASSET.\"Date_purchased\" AS AM_ASSET_Date_purchased,\n"
	     		+ "     AM_ASSET.\"Posting_Date\" AS AM_ASSET_Posting_Date,\n"
	     		+ "     AM_ASSET.\"Effective_Date\" AS AM_ASSET_Effective_Date,\n"
	     		+ "     AM_ASSET.\"BRANCH_CODE\" AS AM_ASSET_BRANCH_CODE,\n"
	     		+ "     AM_ASSET.\"CATEGORY_CODE\" AS AM_ASSET_CATEGORY_CODE,\n"
	     		+ "     am_gb_company.\"company_name\" AS am_gb_company_company_name,\n"
	     		+ "     AM_ASSET.\"Asset_id\" AS AM_ASSET_Asset_id,\n"
	     		+ "     AM_ASSET.\"OLD_ASSET_ID\" AS AM_ASSET_OLD_ASSET_ID,\n"
	     		+ "     am_ad_branch.\"BRANCH_NAME\" AS am_ad_branch_BRANCH_NAME,\n"
	     		+ "     AM_ASSET.\"Asset_Status\" AS AM_ASSET_Asset_Status\n"
	     		+ "FROM\n"
	     		+ "     \"dbo\".\"AM_WIP_RECLASSIFICATION\" AM_WIP_RECLASSIFICATION INNER JOIN \"dbo\".\"am_ad_branch\" am_ad_branch ON AM_WIP_RECLASSIFICATION.\"NEW_BRANCH_CODE\" = am_ad_branch.\"BRANCH_CODE\"\n"
	     		+ "     INNER JOIN \"dbo\".\"AM_ASSET\" AM_ASSET ON AM_WIP_RECLASSIFICATION.\"Asset_id\" = AM_ASSET.\"OLD_ASSET_ID\",\n"
	     		+ "     \"dbo\".\"am_gb_company\" am_gb_company\n"
	     		+ "where AM_ASSET.\"Asset_Status\" = 'ACTIVE'\n"
	     		+ "AND AM_ASSET.category_id = ?\n"
	     		+ "AND transfer_date BETWEEN ? and ?";    
		}      
	 
	 if(branch_Id.equals("0")  && !categoryCode.equals("0")  && FromDate.equals("") && ToDate.equals("")){	   
		   System.out.println("======>>>>>>>Category Selected: ");
		     ColQuery ="SELECT\n"
		     		+ "     AM_WIP_RECLASSIFICATION.\"Asset_id\" AS AM_WIP_RECLASSIFICATION_Asset_id,\n"
		     		+ "     AM_ASSET.\"Description\" AS AM_ASSET_Description,\n"
		     		+ "     AM_ASSET.\"Cost_Price\" AS AM_ASSET_Cost_Price,\n"
		     		+ "     AM_ASSET.\"Accum_Dep\" AS AM_ASSET_Accum_Dep,\n"
		     		+ "     AM_ASSET.\"Monthly_Dep\" AS AM_ASSET_Monthly_Dep,\n"
		     		+ "     AM_ASSET.\"NBV\" AS AM_ASSET_NBV,\n"
		     		+ "     AM_ASSET.\"Date_purchased\" AS AM_ASSET_Date_purchased,\n"
		     		+ "     AM_ASSET.\"Posting_Date\" AS AM_ASSET_Posting_Date,\n"
		     		+ "     AM_ASSET.\"Effective_Date\" AS AM_ASSET_Effective_Date,\n"
		     		+ "     AM_ASSET.\"BRANCH_CODE\" AS AM_ASSET_BRANCH_CODE,\n"
		     		+ "     AM_ASSET.\"CATEGORY_CODE\" AS AM_ASSET_CATEGORY_CODE,\n"
		     		+ "     am_gb_company.\"company_name\" AS am_gb_company_company_name,\n"
		     		+ "     AM_ASSET.\"Asset_id\" AS AM_ASSET_Asset_id,\n"
		     		+ "     AM_ASSET.\"OLD_ASSET_ID\" AS AM_ASSET_OLD_ASSET_ID,\n"
		     		+ "     am_ad_branch.\"BRANCH_NAME\" AS am_ad_branch_BRANCH_NAME,\n"
		     		+ "     AM_ASSET.\"Asset_Status\" AS AM_ASSET_Asset_Status\n"
		     		+ "FROM\n"
		     		+ "     \"dbo\".\"AM_WIP_RECLASSIFICATION\" AM_WIP_RECLASSIFICATION INNER JOIN \"dbo\".\"am_ad_branch\" am_ad_branch ON AM_WIP_RECLASSIFICATION.\"NEW_BRANCH_CODE\" = am_ad_branch.\"BRANCH_CODE\"\n"
		     		+ "     INNER JOIN \"dbo\".\"AM_ASSET\" AM_ASSET ON AM_WIP_RECLASSIFICATION.\"Asset_id\" = AM_ASSET.\"OLD_ASSET_ID\",\n"
		     		+ "     \"dbo\".\"am_gb_company\" am_gb_company\n"
		     		+ "where AM_ASSET.\"Asset_Status\" = 'ACTIVE'\n"
		     		+ "AND AM_ASSET.category_id = ?";    
			}
	 
	 if(!branch_Id.equals("0")  && !categoryCode.equals("0")  && FromDate.equals("") && ToDate.equals("")){
		   System.out.println("======>>>>>>>Branch and Category Selected: ");
		     ColQuery ="SELECT\n"
		     		+ "     AM_WIP_RECLASSIFICATION.\"Asset_id\" AS AM_WIP_RECLASSIFICATION_Asset_id,\n"
		     		+ "     AM_ASSET.\"Description\" AS AM_ASSET_Description,\n"
		     		+ "     AM_ASSET.\"Cost_Price\" AS AM_ASSET_Cost_Price,\n"
		     		+ "     AM_ASSET.\"Accum_Dep\" AS AM_ASSET_Accum_Dep,\n"
		     		+ "     AM_ASSET.\"Monthly_Dep\" AS AM_ASSET_Monthly_Dep,\n"
		     		+ "     AM_ASSET.\"NBV\" AS AM_ASSET_NBV,\n"
		     		+ "     AM_ASSET.\"Date_purchased\" AS AM_ASSET_Date_purchased,\n"
		     		+ "     AM_ASSET.\"Posting_Date\" AS AM_ASSET_Posting_Date,\n"
		     		+ "     AM_ASSET.\"Effective_Date\" AS AM_ASSET_Effective_Date,\n"
		     		+ "     AM_ASSET.\"BRANCH_CODE\" AS AM_ASSET_BRANCH_CODE,\n"
		     		+ "     AM_ASSET.\"CATEGORY_CODE\" AS AM_ASSET_CATEGORY_CODE,\n"
		     		+ "     am_gb_company.\"company_name\" AS am_gb_company_company_name,\n"
		     		+ "     AM_ASSET.\"Asset_id\" AS AM_ASSET_Asset_id,\n"
		     		+ "     AM_ASSET.\"OLD_ASSET_ID\" AS AM_ASSET_OLD_ASSET_ID,\n"
		     		+ "     am_ad_branch.\"BRANCH_NAME\" AS am_ad_branch_BRANCH_NAME,\n"
		     		+ "     AM_ASSET.\"Asset_Status\" AS AM_ASSET_Asset_Status\n"
		     		+ "FROM\n"
		     		+ "     \"dbo\".\"AM_WIP_RECLASSIFICATION\" AM_WIP_RECLASSIFICATION INNER JOIN \"dbo\".\"am_ad_branch\" am_ad_branch ON AM_WIP_RECLASSIFICATION.\"NEW_BRANCH_CODE\" = am_ad_branch.\"BRANCH_CODE\"\n"
		     		+ "     INNER JOIN \"dbo\".\"AM_ASSET\" AM_ASSET ON AM_WIP_RECLASSIFICATION.\"Asset_id\" = AM_ASSET.\"OLD_ASSET_ID\",\n"
		     		+ "     \"dbo\".\"am_gb_company\" am_gb_company\n"
		     		+ "where AM_ASSET.\"Asset_Status\" = 'ACTIVE'\n"
		     		+ "AND AM_ASSET.category_id = ?\n"
		     		+ "AND am_ad_branch.branch_id = ?";    
			}      
	 
	 
   if(!branch_Id.equals("0")  && !categoryCode.equals("0")  && !FromDate.equals("") && !ToDate.equals("")){
	   System.out.println("======>>>>>>>Branch and Category and Date Selected: ");
	     ColQuery ="SELECT\n"
	     		+ "     AM_WIP_RECLASSIFICATION.\"Asset_id\" AS AM_WIP_RECLASSIFICATION_Asset_id,\n"
	     		+ "     AM_ASSET.\"Description\" AS AM_ASSET_Description,\n"
	     		+ "     AM_ASSET.\"Cost_Price\" AS AM_ASSET_Cost_Price,\n"
	     		+ "     AM_ASSET.\"Accum_Dep\" AS AM_ASSET_Accum_Dep,\n"
	     		+ "     AM_ASSET.\"Monthly_Dep\" AS AM_ASSET_Monthly_Dep,\n"
	     		+ "     AM_ASSET.\"NBV\" AS AM_ASSET_NBV,\n"
	     		+ "     AM_ASSET.\"Date_purchased\" AS AM_ASSET_Date_purchased,\n"
	     		+ "     AM_ASSET.\"Posting_Date\" AS AM_ASSET_Posting_Date,\n"
	     		+ "     AM_ASSET.\"Effective_Date\" AS AM_ASSET_Effective_Date,\n"
	     		+ "     AM_ASSET.\"BRANCH_CODE\" AS AM_ASSET_BRANCH_CODE,\n"
	     		+ "     AM_ASSET.\"CATEGORY_CODE\" AS AM_ASSET_CATEGORY_CODE,\n"
	     		+ "     am_gb_company.\"company_name\" AS am_gb_company_company_name,\n"
	     		+ "     AM_ASSET.\"Asset_id\" AS AM_ASSET_Asset_id,\n"
	     		+ "     AM_ASSET.\"OLD_ASSET_ID\" AS AM_ASSET_OLD_ASSET_ID,\n"
	     		+ "     am_ad_branch.\"BRANCH_NAME\" AS am_ad_branch_BRANCH_NAME,\n"
	     		+ "     AM_ASSET.\"Asset_Status\" AS AM_ASSET_Asset_Status\n"
	     		+ "FROM\n"
	     		+ "     \"dbo\".\"AM_WIP_RECLASSIFICATION\" AM_WIP_RECLASSIFICATION INNER JOIN \"dbo\".\"am_ad_branch\" am_ad_branch ON AM_WIP_RECLASSIFICATION.\"NEW_BRANCH_CODE\" = am_ad_branch.\"BRANCH_CODE\"\n"
	     		+ "     INNER JOIN \"dbo\".\"AM_ASSET\" AM_ASSET ON AM_WIP_RECLASSIFICATION.\"Asset_id\" = AM_ASSET.\"OLD_ASSET_ID\",\n"
	     		+ "     \"dbo\".\"am_gb_company\" am_gb_company\n"
	     		+ "where AM_ASSET.\"Asset_Status\" = 'ACTIVE'\n"
	     		+ "AND AM_ASSET.category_id = ?\n"
	     		+ "AND am_ad_branch.branch_id = ?\n"
	     		+ "AND transfer_date BETWEEN ? and ?";    
		}      
//   System.out.println("======>>>>>>>ColQuery: "+ColQuery);
     java.util.ArrayList list =rep.getWipReclassificationRecords(ColQuery,branch_Id,categoryCode,FromDate,ToDate,asset_Id);
     System.out.println("======>>>>>>>list size: "+list.size()+"        =====report: "+report);
     if(list.size()!=0){
   	 if(report.equalsIgnoreCase("rptMenuBCWIP")){
    	 SXSSFWorkbook workbook = new SXSSFWorkbook();
         Sheet sheet = workbook.createSheet("Demo");
         Row rowhead = sheet.createRow((int) 0);
         
         rowhead.createCell( 0).setCellValue("S/No.");
         rowhead.createCell( 1).setCellValue("Aset Id");
         rowhead.createCell( 2).setCellValue("Description");
         rowhead.createCell( 3).setCellValue("Cost Price");
         rowhead.createCell( 4).setCellValue("Accum Dep.");
         rowhead.createCell( 5).setCellValue("Monthly Dep");
         rowhead.createCell( 6).setCellValue("Asset Ledger");
         rowhead.createCell( 7).setCellValue("NBV");
         rowhead.createCell( 8).setCellValue("Purchase Date");
         rowhead.createCell( 9).setCellValue("Posting Date");
         rowhead.createCell( 10).setCellValue("Effective Date");
		 rowhead.createCell( 11).setCellValue("Branch Code");
		 rowhead.createCell( 12).setCellValue("Category Code");
		 rowhead.createCell( 13).setCellValue("Company Name");
		 rowhead.createCell( 14).setCellValue("Asset Id");
		 rowhead.createCell( 15).setCellValue("Old Asset Id");
		 rowhead.createCell( 16).setCellValue("Branch Name");
		 rowhead.createCell( 17).setCellValue("Asset Status");
		
         
         

     int i = 1;
     System.out.println("<<<<<<list.size(): "+list.size());
	 for(int k=0;k<list.size();k++)
     {
    	 com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);    	 
    	    
    	 String Wip_Asset_Id =  newassettrans.getAction();
			String description = newassettrans.getDescription();
			double costPrice = newassettrans.getCostPrice();
			double accum_Dep = newassettrans.getAccumDep();
			double monthly_Dep = newassettrans.getMonthlyDep();
			double nbv = newassettrans.getNbv();
			String purchaseDate = newassettrans.getDatepurchased() != null ? getDate(newassettrans.getDatepurchased()) : "";
			String postingDate = newassettrans.getPostingDate() != null ? getDate(newassettrans.getPostingDate()) : "";
			String effectiveDate = newassettrans.getEffectiveDate() != null ? getDate(newassettrans.getEffectiveDate()) : "";
			String branch_code = newassettrans.getBranchCode();
			String category_Code = newassettrans.getCategoryCode();
			String companyName = newassettrans.getSpare1() != null ? newassettrans.getSpare1() : "";
			String assetId = newassettrans.getAssetId();
			String old_AssetId = newassettrans.getOldassetId();
			String branchName = newassettrans.getBranchName() != null ? newassettrans.getBranchName() : "";
			String assetStatus = newassettrans.getAssetStatus() != null ? newassettrans.getAssetStatus() : "";
			

			Row row = sheet.createRow((int) i);

			row.createCell((short) 0).setCellValue(i);
			row.createCell((short) 1).setCellValue(Wip_Asset_Id);
            row.createCell((short) 2).setCellValue(description);
            row.createCell((short) 3).setCellValue(costPrice);
            row.createCell((short) 4).setCellValue(accum_Dep);
            row.createCell((short) 5).setCellValue(monthly_Dep);
            row.createCell((short) 6).setCellValue(nbv);
            row.createCell((short) 7).setCellValue(purchaseDate);
            row.createCell((short) 8).setCellValue(postingDate);
            row.createCell((short) 9).setCellValue(effectiveDate);
			row.createCell((short) 10).setCellValue(branch_code);
			row.createCell((short) 11).setCellValue(category_Code);
			row.createCell((short) 12).setCellValue(companyName);
			row.createCell((short) 13).setCellValue(assetId);
			row.createCell((short) 14).setCellValue(old_AssetId);
			row.createCell((short) 15).setCellValue(branchName);
			row.createCell((short) 16).setCellValue(assetStatus);

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