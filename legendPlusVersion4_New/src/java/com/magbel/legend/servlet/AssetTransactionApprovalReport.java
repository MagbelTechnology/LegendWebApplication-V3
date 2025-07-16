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
   
public class AssetTransactionApprovalReport extends HttpServlet
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
    if(report.equalsIgnoreCase("rptMenuATAR")){fileName = branchCode+"By"+userName+"AssetTransactionApprovalReport.xlsx";}
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
    
     if(branch_Id.equals("0")  && FromDate.equals("")  && ToDate.equals("")){
  	   System.out.println("======>>>>>>>No Selection: "+branch_Id+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  dept_Code: "+dept_Code+"   asset_Id: "+asset_Id);
  	    ColQuery ="select asset_id,a.user_id,super_id,b.Full_Name AS POSTED_BY,c.Full_Name AS APPROVED_BY,description,tran_type,a.posting_date,a.DATE_APPROVED,h.BRANCH_NAME from am_asset_approval a, am_gb_User b, am_gb_User c,  am_ad_branch h \n"
  	    		+ "where substring(asset_id,0,5) = 'ZPLC' AND b.Branch = h.BRANCH_ID AND  process_status = 'A' and a.user_id = b.User_id and a.super_id = c.User_id\n"
  	    		+ "UNION\n"
  	    		+ "select g.asset_id,a.user_id,super_id,b.Full_Name AS POSTED_BY,c.Full_Name AS APPROVED_BY,g.description,tran_type,a.posting_date,a.DATE_APPROVED,h.BRANCH_NAME from am_asset_approval a, am_group_asset g,am_gb_User b, am_gb_User c, am_ad_branch h  \n"
  	    		+ "where a.Asset_id = CONVERT(varchar, g.Group_id) AND b.Branch = h.BRANCH_ID AND a.Asset_Status = 'ACTIVE' and substring(a.asset_id,0,5) != 'ZPLC'  and a.user_id = b.User_id and a.super_id = c.User_id\n"
  	    		+ "UNION\n"
  	    		+ "select g.asset_id,a.user_id,super_id,b.Full_Name AS POSTED_BY,c.Full_Name AS APPROVED_BY,g.description,tran_type,a.posting_date,a.DATE_APPROVED,h.BRANCH_NAME from am_asset_approval a, AM_GROUP_ASSET_UNCAPITALIZED g,am_gb_User b, am_gb_User c, am_ad_branch h   \n"
  	    		+ "where a.Asset_id = CONVERT(varchar, g.Group_id) AND b.Branch = h.BRANCH_ID AND a.Asset_Status = 'ACTIVE' and substring(a.asset_id,0,5) != 'ZPLC'  and a.user_id = b.User_id and a.super_id = c.User_id \n"
  	    		+ "UNION\n"
  	    		+ "select g.asset_id,a.user_id,super_id,b.Full_Name AS POSTED_BY,c.Full_Name AS APPROVED_BY,g.description,tran_type,a.posting_date,a.DATE_APPROVED,h.BRANCH_NAME from am_asset_approval a, am_gb_bulkTransfer g,am_gb_User b, am_gb_User c,  am_ad_branch h   \n"
  	    		+ "where a.Asset_id = CONVERT(varchar, g.Batch_id) AND b.Branch = h.BRANCH_ID AND a.process_status = 'A' and substring(a.asset_id,0,5) != 'ZPLC'  and a.user_id = b.User_id and a.super_id = c.User_id \n"
  	    		+ "UNION\n"
  	    		+ "select g.asset_id,a.user_id,super_id,b.Full_Name AS POSTED_BY,c.Full_Name AS APPROVED_BY,g.description,tran_type,a.posting_date,a.DATE_APPROVED,h.BRANCH_NAME from am_asset_approval a, am_gb_bulkUpdate g,am_gb_User b, am_gb_User c, am_ad_branch h  \n"
  	    		+ "where a.Asset_id = CONVERT(varchar, g.Batch_id) AND b.Branch = h.BRANCH_ID AND a.process_status = 'A' and substring(a.asset_id,0,5) != 'ZPLC'  and a.user_id = b.User_id and a.super_id = c.User_id\n"
  	    		+ "\n"
  	    		+ "UNION\n"
  	    		+ "select g.asset_id,a.user_id,super_id,b.Full_Name AS POSTED_BY,c.Full_Name AS APPROVED_BY,g.description,tran_type,a.posting_date,a.DATE_APPROVED,h.BRANCH_NAME from am_asset_approval a, AM_GROUP_DISPOSAL g,am_gb_User b, am_gb_User c, am_ad_branch h  \n"
  	    		+ "where a.Asset_id = CONVERT(varchar, g.disposal_ID) AND b.Branch = h.BRANCH_ID AND a.process_status = 'A' and substring(a.asset_id,0,5) != 'ZPLC'  and a.user_id = b.User_id and a.super_id = c.User_id\n"
  	    		+ "UNION\n"
  	    		+ "select g.asset_id,a.user_id,super_id,b.Full_Name AS POSTED_BY,c.Full_Name AS APPROVED_BY,g.description,tran_type,a.posting_date,a.DATE_APPROVED,h.BRANCH_NAME from am_asset_approval a, am_asset_improvement_Upload g,am_gb_User b, am_gb_User c, am_ad_branch h   \n"
  	    		+ "where a.Asset_id = CONVERT(varchar, g.Revalue_ID) AND b.Branch = h.BRANCH_ID AND a.process_status = 'A' and substring(a.asset_id,0,5) != 'ZPLC'  and a.user_id = b.User_id and a.super_id = c.User_id\n"
  	    		+ "";    
  	}   
	 
	 if(!branch_Id.equals("0")  && FromDate.equals("")  && ToDate.equals("")){	   
	   System.out.println("======>>>>>>>Branch Selected: "+branch_Id+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  dept_Code: "+dept_Code+"   asset_Id: "+asset_Id);
	    ColQuery ="select asset_id,a.user_id,super_id,b.Full_Name AS POSTED_BY,c.Full_Name AS APPROVED_BY,description,tran_type,a.posting_date,a.DATE_APPROVED,h.BRANCH_NAME from am_asset_approval a, am_gb_User b, am_gb_User c,  am_ad_branch h \n"
	    		+ "where substring(asset_id,0,5) = 'ZPLC' AND b.Branch = h.BRANCH_ID AND  process_status = 'A' and a.user_id = b.User_id and a.super_id = c.User_id and b.branch=?\n"
	    		+ "UNION\n"
	    		+ "select g.asset_id,a.user_id,super_id,b.Full_Name AS POSTED_BY,c.Full_Name AS APPROVED_BY,g.description,tran_type,a.posting_date,a.DATE_APPROVED,h.BRANCH_NAME from am_asset_approval a, am_group_asset g,am_gb_User b, am_gb_User c, am_ad_branch h  \n"
	    		+ "where a.Asset_id = CONVERT(varchar, g.Group_id) AND b.Branch = h.BRANCH_ID AND a.Asset_Status = 'ACTIVE' and substring(a.asset_id,0,5) != 'ZPLC'  and a.user_id = b.User_id and a.super_id = c.User_id and b.branch=?\n"
	    		+ "UNION\n"
	    		+ "select g.asset_id,a.user_id,super_id,b.Full_Name AS POSTED_BY,c.Full_Name AS APPROVED_BY,g.description,tran_type,a.posting_date,a.DATE_APPROVED,h.BRANCH_NAME from am_asset_approval a, AM_GROUP_ASSET_UNCAPITALIZED g,am_gb_User b, am_gb_User c, am_ad_branch h   \n"
	    		+ "where a.Asset_id = CONVERT(varchar, g.Group_id) AND b.Branch = h.BRANCH_ID AND a.Asset_Status = 'ACTIVE' and substring(a.asset_id,0,5) != 'ZPLC'  and a.user_id = b.User_id and a.super_id = c.User_id and b.branch=?\n"
	    		+ "UNION\n"
	    		+ "select g.asset_id,a.user_id,super_id,b.Full_Name AS POSTED_BY,c.Full_Name AS APPROVED_BY,g.description,tran_type,a.posting_date,a.DATE_APPROVED,h.BRANCH_NAME from am_asset_approval a, am_gb_bulkTransfer g,am_gb_User b, am_gb_User c,  am_ad_branch h   \n"
	    		+ "where a.Asset_id = CONVERT(varchar, g.Batch_id) AND b.Branch = h.BRANCH_ID AND a.process_status = 'A' and substring(a.asset_id,0,5) != 'ZPLC'  and a.user_id = b.User_id and a.super_id = c.User_id and b.branch=?\n"
	    		+ "UNION\n"
	    		+ "select g.asset_id,a.user_id,super_id,b.Full_Name AS POSTED_BY,c.Full_Name AS APPROVED_BY,g.description,tran_type,a.posting_date,a.DATE_APPROVED,h.BRANCH_NAME from am_asset_approval a, am_gb_bulkUpdate g,am_gb_User b, am_gb_User c, am_ad_branch h  \n"
	    		+ "where a.Asset_id = CONVERT(varchar, g.Batch_id) AND b.Branch = h.BRANCH_ID AND a.process_status = 'A' and substring(a.asset_id,0,5) != 'ZPLC'  and a.user_id = b.User_id and a.super_id = c.User_id and b.branch=?\n"
	    		+ "UNION\n"
	    		+ "select g.asset_id,a.user_id,super_id,b.Full_Name AS POSTED_BY,c.Full_Name AS APPROVED_BY,g.description,tran_type,a.posting_date,a.DATE_APPROVED,h.BRANCH_NAME from am_asset_approval a, AM_GROUP_DISPOSAL g,am_gb_User b, am_gb_User c, am_ad_branch h  \n"
	    		+ "where a.Asset_id = CONVERT(varchar, g.disposal_ID) AND b.Branch = h.BRANCH_ID AND a.process_status = 'A' and substring(a.asset_id,0,5) != 'ZPLC'  and a.user_id = b.User_id and a.super_id = c.User_id and b.branch=?\n"
	    		+ "UNION\n"
	    		+ "select g.asset_id,a.user_id,super_id,b.Full_Name AS POSTED_BY,c.Full_Name AS APPROVED_BY,g.description,tran_type,a.posting_date,a.DATE_APPROVED,h.BRANCH_NAME from am_asset_approval a, am_asset_improvement_Upload g,am_gb_User b, am_gb_User c, am_ad_branch h   \n"
	    		+ "where a.Asset_id = CONVERT(varchar, g.Revalue_ID) AND b.Branch = h.BRANCH_ID AND a.process_status = 'A' and substring(a.asset_id,0,5) != 'ZPLC'  and a.user_id = b.User_id and a.super_id = c.User_id and b.branch=?\n"
	    		+ "";
	    }
  
   if(!FromDate.equals("")  && !ToDate.equals("") && branch_Id.equals("0")){
	System.out.println("======>>>>>>> Date selected: "+branch_Id+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  dept_Code: "+dept_Code+"   asset_Id: "+asset_Id);
    ColQuery ="select asset_id,a.user_id,super_id,b.Full_Name AS POSTED_BY,c.Full_Name AS APPROVED_BY,description,tran_type,a.posting_date,a.DATE_APPROVED,h.BRANCH_NAME from am_asset_approval a, am_gb_User b, am_gb_User c,  am_ad_branch h \n"
    		+ "where substring(asset_id,0,5) = 'ZPLC' AND b.Branch = h.BRANCH_ID AND  process_status = 'A' and a.user_id = b.User_id and a.super_id = c.User_id and a.DATE_APPROVED between ? and ?\n"
    		+ "UNION\n"
    		+ "select g.asset_id,a.user_id,super_id,b.Full_Name AS POSTED_BY,c.Full_Name AS APPROVED_BY,g.description,tran_type,a.posting_date,a.DATE_APPROVED,h.BRANCH_NAME from am_asset_approval a, am_group_asset g,am_gb_User b, am_gb_User c, am_ad_branch h  \n"
    		+ "where a.Asset_id = CONVERT(varchar, g.Group_id) AND b.Branch = h.BRANCH_ID AND a.Asset_Status = 'ACTIVE' and substring(a.asset_id,0,5) != 'ZPLC'  and a.user_id = b.User_id and a.super_id = c.User_id and a.DATE_APPROVED between ? and ?\n"
    		+ "UNION\n"
    		+ "select g.asset_id,a.user_id,super_id,b.Full_Name AS POSTED_BY,c.Full_Name AS APPROVED_BY,g.description,tran_type,a.posting_date,a.DATE_APPROVED,h.BRANCH_NAME from am_asset_approval a, AM_GROUP_ASSET_UNCAPITALIZED g,am_gb_User b, am_gb_User c, am_ad_branch h   \n"
    		+ "where a.Asset_id = CONVERT(varchar, g.Group_id) AND b.Branch = h.BRANCH_ID AND a.Asset_Status = 'ACTIVE' and substring(a.asset_id,0,5) != 'ZPLC'  and a.user_id = b.User_id and a.super_id = c.User_id and a.DATE_APPROVED between ? and ?\n"
    		+ "UNION\n"
    		+ "select g.asset_id,a.user_id,super_id,b.Full_Name AS POSTED_BY,c.Full_Name AS APPROVED_BY,g.description,tran_type,a.posting_date,a.DATE_APPROVED,h.BRANCH_NAME from am_asset_approval a, am_gb_bulkTransfer g,am_gb_User b, am_gb_User c,  am_ad_branch h   \n"
    		+ "where a.Asset_id = CONVERT(varchar, g.Batch_id) AND b.Branch = h.BRANCH_ID AND a.process_status = 'A' and substring(a.asset_id,0,5) != 'ZPLC'  and a.user_id = b.User_id and a.super_id = c.User_id and a.DATE_APPROVED between ? and ?\n"
    		+ "UNION\n"
    		+ "select g.asset_id,a.user_id,super_id,b.Full_Name AS POSTED_BY,c.Full_Name AS APPROVED_BY,g.description,tran_type,a.posting_date,a.DATE_APPROVED,h.BRANCH_NAME from am_asset_approval a, am_gb_bulkUpdate g,am_gb_User b, am_gb_User c, am_ad_branch h  \n"
    		+ "where a.Asset_id = CONVERT(varchar, g.Batch_id) AND b.Branch = h.BRANCH_ID AND a.process_status = 'A' and substring(a.asset_id,0,5) != 'ZPLC'  and a.user_id = b.User_id and a.super_id = c.User_id and a.DATE_APPROVED between ? and ?\n"
    		+ "UNION\n"
    		+ "select g.asset_id,a.user_id,super_id,b.Full_Name AS POSTED_BY,c.Full_Name AS APPROVED_BY,g.description,tran_type,a.posting_date,a.DATE_APPROVED,h.BRANCH_NAME from am_asset_approval a, AM_GROUP_DISPOSAL g,am_gb_User b, am_gb_User c, am_ad_branch h  \n"
    		+ "where a.Asset_id = CONVERT(varchar, g.disposal_ID) AND b.Branch = h.BRANCH_ID AND a.process_status = 'A' and substring(a.asset_id,0,5) != 'ZPLC'  and a.user_id = b.User_id and a.super_id = c.User_id and a.DATE_APPROVED between ? and ?\n"
    		+ "UNION\n"
    		+ "select g.asset_id,a.user_id,super_id,b.Full_Name AS POSTED_BY,c.Full_Name AS APPROVED_BY,g.description,tran_type,a.posting_date,a.DATE_APPROVED,h.BRANCH_NAME from am_asset_approval a, am_asset_improvement_Upload g,am_gb_User b, am_gb_User c, am_ad_branch h   \n"
    		+ "where a.Asset_id = CONVERT(varchar, g.Revalue_ID) AND b.Branch = h.BRANCH_ID AND a.process_status = 'A' and substring(a.asset_id,0,5) != 'ZPLC'  and a.user_id = b.User_id and a.super_id = c.User_id and a.DATE_APPROVED between ? and ?\n"
    		+ "";		     
	} 
   if(!branch_Id.equals("0") && !FromDate.equals("")  && !ToDate.equals("")){
	System.out.println("======>>>>>>>Branch and Date Selection: "+branch_Id+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  dept_Code: "+dept_Code+"   asset_Id: "+asset_Id);
    ColQuery ="select asset_id,a.user_id,super_id,b.Full_Name AS POSTED_BY,c.Full_Name AS APPROVED_BY,description,tran_type,a.posting_date,a.DATE_APPROVED,h.BRANCH_NAME from am_asset_approval a, am_gb_User b, am_gb_User c,  am_ad_branch h \n"
    		+ "where substring(asset_id,0,5) = 'ZPLC' AND b.Branch = h.BRANCH_ID AND  process_status = 'A' and a.user_id = b.User_id and a.super_id = c.User_id and a.DATE_APPROVED between ? and ? and b.branch=?\n"
    		+ "UNION\n"
    		+ "select g.asset_id,a.user_id,super_id,b.Full_Name AS POSTED_BY,c.Full_Name AS APPROVED_BY,g.description,tran_type,a.posting_date,a.DATE_APPROVED,h.BRANCH_NAME from am_asset_approval a, am_group_asset g,am_gb_User b, am_gb_User c, am_ad_branch h  \n"
    		+ "where a.Asset_id = CONVERT(varchar, g.Group_id) AND b.Branch = h.BRANCH_ID AND a.Asset_Status = 'ACTIVE' and substring(a.asset_id,0,5) != 'ZPLC'  and a.user_id = b.User_id and a.super_id = c.User_id and a.DATE_APPROVED between ? and ? and b.branch=?\n"
    		+ "UNION\n"
    		+ "select g.asset_id,a.user_id,super_id,b.Full_Name AS POSTED_BY,c.Full_Name AS APPROVED_BY,g.description,tran_type,a.posting_date,a.DATE_APPROVED,h.BRANCH_NAME from am_asset_approval a, AM_GROUP_ASSET_UNCAPITALIZED g,am_gb_User b, am_gb_User c, am_ad_branch h   \n"
    		+ "where a.Asset_id = CONVERT(varchar, g.Group_id) AND b.Branch = h.BRANCH_ID AND a.Asset_Status = 'ACTIVE' and substring(a.asset_id,0,5) != 'ZPLC'  and a.user_id = b.User_id and a.super_id = c.User_id and a.DATE_APPROVED between ? and ? and b.branch=?\n"
    		+ "UNION\n"
    		+ "select g.asset_id,a.user_id,super_id,b.Full_Name AS POSTED_BY,c.Full_Name AS APPROVED_BY,g.description,tran_type,a.posting_date,a.DATE_APPROVED,h.BRANCH_NAME from am_asset_approval a, am_gb_bulkTransfer g,am_gb_User b, am_gb_User c,  am_ad_branch h   \n"
    		+ "where a.Asset_id = CONVERT(varchar, g.Batch_id) AND b.Branch = h.BRANCH_ID AND a.process_status = 'A' and substring(a.asset_id,0,5) != 'ZPLC'  and a.user_id = b.User_id and a.super_id = c.User_id and a.DATE_APPROVED between ? and ? and b.branch=?\n"
    		+ "UNION\n"
    		+ "select g.asset_id,a.user_id,super_id,b.Full_Name AS POSTED_BY,c.Full_Name AS APPROVED_BY,g.description,tran_type,a.posting_date,a.DATE_APPROVED,h.BRANCH_NAME from am_asset_approval a, am_gb_bulkUpdate g,am_gb_User b, am_gb_User c, am_ad_branch h  \n"
    		+ "where a.Asset_id = CONVERT(varchar, g.Batch_id) AND b.Branch = h.BRANCH_ID AND a.process_status = 'A' and substring(a.asset_id,0,5) != 'ZPLC'  and a.user_id = b.User_id and a.super_id = c.User_id and a.DATE_APPROVED between ? and ? and b.branch=?\n"
    		+ "UNION\n"
    		+ "select g.asset_id,a.user_id,super_id,b.Full_Name AS POSTED_BY,c.Full_Name AS APPROVED_BY,g.description,tran_type,a.posting_date,a.DATE_APPROVED,h.BRANCH_NAME from am_asset_approval a, AM_GROUP_DISPOSAL g,am_gb_User b, am_gb_User c, am_ad_branch h  \n"
    		+ "where a.Asset_id = CONVERT(varchar, g.disposal_ID) AND b.Branch = h.BRANCH_ID AND a.process_status = 'A' and substring(a.asset_id,0,5) != 'ZPLC'  and a.user_id = b.User_id and a.super_id = c.User_id and a.DATE_APPROVED between ? and ? and b.branch=?\n"
    		+ "UNION\n"
    		+ "select g.asset_id,a.user_id,super_id,b.Full_Name AS POSTED_BY,c.Full_Name AS APPROVED_BY,g.description,tran_type,a.posting_date,a.DATE_APPROVED,h.BRANCH_NAME from am_asset_approval a, am_asset_improvement_Upload g,am_gb_User b, am_gb_User c, am_ad_branch h   \n"
    		+ "where a.Asset_id = CONVERT(varchar, g.Revalue_ID) AND b.Branch = h.BRANCH_ID AND a.process_status = 'A' and substring(a.asset_id,0,5) != 'ZPLC'  and a.user_id = b.User_id and a.super_id = c.User_id and a.DATE_APPROVED between ? and ? and b.branch=?\n"
    		+ "";	    
	}    
//   if(!FromDate.equals("")  && !ToDate.equals("") && !branch_Id.equals("0")  && !categoryCode.equals("0")){
//	System.out.println("======>>>>>>>Branch, Category and Date Selection: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  dept_Code: "+dept_Code+"   asset_Id: "+asset_Id);
//    ColQuery ="select 'C' AS transType,a.BRANCH_CODE,b.BRANCH_NAME,c.category_name,d.Dept_name, a.Asset_id,Description,Cost_Price,a.Monthly_Dep,a.Accum_Dep,a.nbv,"
//    		+ "a.Posting_Date,a.Date_purchased,a.Effective_Date,a.Dep_End_Date,a.Dep_Rate,a.Asset_User from am_asset a, am_ad_branch b,am_ad_category c, "
//    		+ "am_ad_department d,am_gb_company p "
//    		+ "where a.BRANCH_CODE = b.BRANCH_CODE and a.CATEGORY_CODE = c.category_code and a.DEPT_CODE = d.Dept_code and a.Cost_Price > p.Cost_Threshold - 0.01 "
//    		+ " and a.branch_id = ? and a.category_Id = ? and a.Posting_Date between ? and ? "
//    		+ "UNION "
//    		+ "select 'U' AS transType,a.BRANCH_CODE, b.BRANCH_NAME,c.category_name,d.Dept_name,a.Asset_id,Description,Cost_Price,a.Monthly_Dep,a.Accum_Dep,a.nbv,"
//    		+ "a.Posting_Date,a.Date_purchased,a.Effective_Date,a.Dep_End_Date,a.Dep_Rate,a.Asset_User from AM_ASSET_UNCAPITALIZED a, am_ad_branch b,"
//    		+ "am_ad_category c, am_ad_department d,am_gb_company p "
//    		+ "where a.BRANCH_CODE = b.BRANCH_CODE and a.CATEGORY_CODE = c.category_code and a.DEPT_CODE = d.Dept_code and a.Cost_Price < p.Cost_Threshold  "
//    		+ " and a.branch_id = ? and a.category_Id = ? and a.Posting_Date between ? and ? ";	
//    }    

   
//   System.out.println("======>>>>>>>ColQuery: "+ColQuery);
     java.util.ArrayList list =rep.getAssetTransactionApprovalRecords(ColQuery,branch_Id,FromDate,ToDate);
     System.out.println("======>>>>>>>list size: "+list.size()+"        =====report: "+report);
     if(list.size()!=0){
   	 if(report.equalsIgnoreCase("rptMenuATAR")){
    	 SXSSFWorkbook workbook = new SXSSFWorkbook();
         Sheet sheet = workbook.createSheet("Demo");
         Row rowhead = sheet.createRow((int) 0);
         
         rowhead.createCell( 0).setCellValue("S/No.");
         rowhead.createCell( 1).setCellValue("Asset ID");
         rowhead.createCell( 2).setCellValue("User ID");
         rowhead.createCell( 3).setCellValue("Super ID");
         rowhead.createCell( 4).setCellValue("Posted By");
         rowhead.createCell( 5).setCellValue("Branch Name");
         rowhead.createCell( 6).setCellValue("Approved By");
         rowhead.createCell( 7).setCellValue("Description");
         rowhead.createCell( 8).setCellValue("Transaction Type");
         rowhead.createCell( 9).setCellValue("Posting Date");
         rowhead.createCell( 10).setCellValue("Date Approved");
         
     int i = 1;
     System.out.println("<<<<<<list.size(): "+list.size());
	 for(int k=0;k<list.size();k++)
     {
    	 com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);    	 
    		String assetId = newassettrans.getAssetId();
			String user_id = newassettrans.getAssetCode();
			String super_id = newassettrans.getAction();
			String posted_by = newassettrans.getPostedBy();
			String approved_by =newassettrans.getApprovalStatus();
			String branch_Name = newassettrans.getBranchName();
			String description = newassettrans.getDescription();
			String tran_type = newassettrans.getTranType();
			String posting_date = newassettrans.getPostingDate() != null ? getDate(newassettrans.getPostingDate()) : "";	
			String date_approved = newassettrans.getDatepurchased() != null ? getDate(newassettrans.getDatepurchased()) : "";
			
	

			Row row = sheet.createRow((int) i);
			 
			row.createCell((short) 0).setCellValue(i);
			row.createCell((short) 1).setCellValue(assetId);
            row.createCell((short) 2).setCellValue(user_id);
            row.createCell((short) 3).setCellValue(super_id);
            row.createCell((short) 4).setCellValue(posted_by);
            row.createCell((short) 5).setCellValue(branch_Name);
            row.createCell((short) 6).setCellValue(approved_by);
            row.createCell((short) 7).setCellValue(description);
            row.createCell((short) 8).setCellValue(tran_type);
            row.createCell((short) 9).setCellValue(posting_date);
            row.createCell((short) 10).setCellValue(date_approved);
            
            //	System.out.println("======>oldAssetId====: "+oldAssetId+"  Index: "+i);

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