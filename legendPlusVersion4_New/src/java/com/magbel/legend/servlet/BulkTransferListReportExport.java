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
   
public class BulkTransferListReportExport extends HttpServlet
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

    String report = request.getParameter("report");
    String batch_Id = request.getParameter("batchId");
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
    if(report.equalsIgnoreCase("rptMenuBulktransferList")){fileName = branchCode+"By"+userName+"BulkTransferReport.xlsx";}
    
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
   System.out.println("<<<<<<batch_Id: "+batch_Id+"  branchCode: "+branchCode);
     String ColQuery = "";
     if(branch_Id.equals("0")  && batch_Id.equals("") && FromDate.equals("") && ToDate.equals("")){
  	   System.out.println("======>>>>>>>No Selection: "+branch_Id+"   batch_Id: "+batch_Id+"    FromDate: "+FromDate+"   ToDate: "+ToDate);
  	     ColQuery ="SELECT\n"
  	     		+ "    am_gb_bulkTransfer.\"Asset_id\" AS am_gb_bulkTransfer_Asset_id,\n"
  	     		+ "     am_gb_bulkTransfer.\"Registration_No\" AS am_gb_bulkTransfer_Registration_No,\n"
  	     		+ "     am_gb_bulkTransfer.\"Description\" AS am_gb_bulkTransfer_Description,\n"
  	     		+ "     am_gb_bulkTransfer.\"oldbranch_id\" AS am_gb_bulkTransfer_oldbranch_id,\n"
  	     		+ "     am_gb_bulkTransfer.\"olddept_id\" AS am_gb_bulkTransfer_olddept_id,\n"
  	     		+ "     am_gb_bulkTransfer.\"oldSBU_CODE\" AS am_gb_bulkTransfer_oldSBU_CODE,\n"
  	     		+ "     am_gb_bulkTransfer.\"oldsection_id\" AS am_gb_bulkTransfer_oldsection_id,\n"
  	     		+ "     am_gb_bulkTransfer.\"oldAsset_User\" AS am_gb_bulkTransfer_oldAsset_User,\n"
  	     		+ "     am_gb_bulkTransfer.\"newbranch_id\" AS am_gb_bulkTransfer_newbranch_id,\n"
  	     		+ "     am_gb_bulkTransfer.\"newdept_id\" AS am_gb_bulkTransfer_newdept_id,\n"
  	     		+ "     am_gb_bulkTransfer.\"newSBU_CODE\" AS am_gb_bulkTransfer_newSBU_CODE,\n"
  	     		+ "     am_gb_bulkTransfer.\"newsection_id\" AS am_gb_bulkTransfer_newsection_id,\n"
  	     		+ "     am_gb_bulkTransfer.\"newAsset_User\" AS am_gb_bulkTransfer_newAsset_User,\n"
  	     		+ "     am_gb_bulkTransfer.\"Batch_id\" AS am_gb_bulkTransfer_Batch_id,\n"
  	     		+ "     am_gb_bulkTransfer.\"Transferby_id\" AS am_gb_bulkTransfer_Transferby_id,\n"
  	     		+ "     am_gb_bulkTransfer.\"CATEGORY_ID\" AS am_gb_bulkTransfer_CATEGORY_ID,\n"
  	     		+ "     am_gb_bulkTransfer.\"NEW_ASSET_ID\" AS am_gb_bulkTransfer_NEW_ASSET_ID,\n"
  	     		+ "     am_gb_bulkTransfer.\"ACCUM_DEP\" AS am_gb_bulkTransfer_ACCUM_DEP,\n"
  	     		+ "     am_gb_bulkTransfer.\"MONTHLY_DEP\" AS am_gb_bulkTransfer_MONTHLY_DEP,\n"
  	     		+ "     am_gb_bulkTransfer.\"NBV\" AS am_gb_bulkTransfer_NBV,\n"
  	     		+ "     am_gb_bulkTransfer.\"TRANSFER_DATE\" AS am_gb_bulkTransfer_TRANSFER_DATE,\n"
  	     		+ "     am_gb_bulkTransfer.\"transId\" AS am_gb_bulkTransfer_transId,\n"
  	     		+ "     am_gb_bulkTransfer.\"TRANSFER_COST\" AS am_gb_bulkTransfer_TRANSFER_COST,\n"
  	     		+ "     am_gb_bulkTransfer.\"asset_code\" AS am_gb_bulkTransfer_asset_code,\n"
  	     		+ "     am_ad_branch.\"BRANCH_NAME\" AS am_ad_branch_BRANCH_NAME,\n"
  	     		+ "     am_ad_branch_A.\"BRANCH_NAME\" AS am_ad_branch_A_BRANCH_NAME,\n"
  	     		+ "     am_gb_User.\"Full_Name\" AS am_gb_User_Full_Name,\n"
  	     		+ "     am_gb_User.\"User_Name\" AS am_gb_User_User_Name,\n"
  	     		+ "     am_gb_bulkTransfer.\"COST_PRICE\" AS am_gb_bulkTransfer_COST_PRICE\n"
  	     		+ "FROM\n"
  	     		+ "     \"dbo\".\"am_ad_branch\" am_ad_branch INNER JOIN \"dbo\".\"am_gb_bulkTransfer\" am_gb_bulkTransfer ON am_ad_branch.\"BRANCH_ID\" = am_gb_bulkTransfer.\"oldbranch_id\"\n"
  	     		+ "     INNER JOIN \"dbo\".\"am_ad_branch\" am_ad_branch_A ON am_gb_bulkTransfer.\"newbranch_id\" = am_ad_branch_A.\"BRANCH_ID\"\n"
  	     		+ "     INNER JOIN \"dbo\".\"am_gb_User\" am_gb_User ON am_gb_bulkTransfer.\"Transferby_id\" = am_gb_User.\"User_id\"\n"
  	     		+ "	WHERE ASSET_ID IS NOT NULL\n"
  	     		+ "ORDER BY\n"
  	     		+ "     Batch_id ASC";    
  		}
     
     
     if(branch_Id.equals("0")  && batch_Id.equals("") && !FromDate.equals("") && !ToDate.equals("")){
    	 System.out.println("======>>>>>>>Date Selected: "+branch_Id+"   batch_Id: "+batch_Id+"    FromDate: "+FromDate+"   ToDate: "+ToDate);	     
	     ColQuery =" SELECT\n"
	     		+ "    am_gb_bulkTransfer.\"Asset_id\" AS am_gb_bulkTransfer_Asset_id,\n"
	     		+ "     am_gb_bulkTransfer.\"Registration_No\" AS am_gb_bulkTransfer_Registration_No,\n"
	     		+ "     am_gb_bulkTransfer.\"Description\" AS am_gb_bulkTransfer_Description,\n"
	     		+ "     am_gb_bulkTransfer.\"oldbranch_id\" AS am_gb_bulkTransfer_oldbranch_id,\n"
	     		+ "     am_gb_bulkTransfer.\"olddept_id\" AS am_gb_bulkTransfer_olddept_id,\n"
	     		+ "     am_gb_bulkTransfer.\"oldSBU_CODE\" AS am_gb_bulkTransfer_oldSBU_CODE,\n"
	     		+ "     am_gb_bulkTransfer.\"oldsection_id\" AS am_gb_bulkTransfer_oldsection_id,\n"
	     		+ "     am_gb_bulkTransfer.\"oldAsset_User\" AS am_gb_bulkTransfer_oldAsset_User,\n"
	     		+ "     am_gb_bulkTransfer.\"newbranch_id\" AS am_gb_bulkTransfer_newbranch_id,\n"
	     		+ "     am_gb_bulkTransfer.\"newdept_id\" AS am_gb_bulkTransfer_newdept_id,\n"
	     		+ "     am_gb_bulkTransfer.\"newSBU_CODE\" AS am_gb_bulkTransfer_newSBU_CODE,\n"
	     		+ "     am_gb_bulkTransfer.\"newsection_id\" AS am_gb_bulkTransfer_newsection_id,\n"
	     		+ "     am_gb_bulkTransfer.\"newAsset_User\" AS am_gb_bulkTransfer_newAsset_User,\n"
	     		+ "     am_gb_bulkTransfer.\"Batch_id\" AS am_gb_bulkTransfer_Batch_id,\n"
	     		+ "     am_gb_bulkTransfer.\"Transferby_id\" AS am_gb_bulkTransfer_Transferby_id,\n"
	     		+ "     am_gb_bulkTransfer.\"CATEGORY_ID\" AS am_gb_bulkTransfer_CATEGORY_ID,\n"
	     		+ "     am_gb_bulkTransfer.\"NEW_ASSET_ID\" AS am_gb_bulkTransfer_NEW_ASSET_ID,\n"
	     		+ "     am_gb_bulkTransfer.\"ACCUM_DEP\" AS am_gb_bulkTransfer_ACCUM_DEP,\n"
	     		+ "     am_gb_bulkTransfer.\"MONTHLY_DEP\" AS am_gb_bulkTransfer_MONTHLY_DEP,\n"
	     		+ "     am_gb_bulkTransfer.\"NBV\" AS am_gb_bulkTransfer_NBV,\n"
	     		+ "     am_gb_bulkTransfer.\"TRANSFER_DATE\" AS am_gb_bulkTransfer_TRANSFER_DATE,\n"
	     		+ "     am_gb_bulkTransfer.\"transId\" AS am_gb_bulkTransfer_transId,\n"
	     		+ "     am_gb_bulkTransfer.\"TRANSFER_COST\" AS am_gb_bulkTransfer_TRANSFER_COST,\n"
	     		+ "     am_gb_bulkTransfer.\"asset_code\" AS am_gb_bulkTransfer_asset_code,\n"
	     		+ "     am_ad_branch.\"BRANCH_NAME\" AS am_ad_branch_BRANCH_NAME,\n"
	     		+ "     am_ad_branch_A.\"BRANCH_NAME\" AS am_ad_branch_A_BRANCH_NAME,\n"
	     		+ "     am_gb_User.\"Full_Name\" AS am_gb_User_Full_Name,\n"
	     		+ "     am_gb_User.\"User_Name\" AS am_gb_User_User_Name,\n"
	     		+ "     am_gb_bulkTransfer.\"COST_PRICE\" AS am_gb_bulkTransfer_COST_PRICE\n"
	     		+ "FROM\n"
	     		+ "     \"dbo\".\"am_ad_branch\" am_ad_branch INNER JOIN \"dbo\".\"am_gb_bulkTransfer\" am_gb_bulkTransfer ON am_ad_branch.\"BRANCH_ID\" = am_gb_bulkTransfer.\"oldbranch_id\"\n"
	     		+ "     INNER JOIN \"dbo\".\"am_ad_branch\" am_ad_branch_A ON am_gb_bulkTransfer.\"newbranch_id\" = am_ad_branch_A.\"BRANCH_ID\"\n"
	     		+ "     INNER JOIN \"dbo\".\"am_gb_User\" am_gb_User ON am_gb_bulkTransfer.\"Transferby_id\" = am_gb_User.\"User_id\"\n"
	     		+ " WHERE ASSET_ID IS NOT NULL\n"
	     		+ " AND am_gb_bulkTransfer.TRANSFER_DATE BETWEEN ? and ?\n"
	     		+ "ORDER BY\n"
	     		+ "     Batch_id ASC";  
	}      
	 if(!branch_Id.equals("0")  && batch_Id.equals("") && !FromDate.equals("") && !ToDate.equals("")){	   
	   System.out.println("======>>>>>>>Branch and Date Selected: "+branch_Id+"   batch_Id: "+batch_Id+"    FromDate: "+FromDate+"   ToDate: "+ToDate);
	     ColQuery =" SELECT\n"
	     		+ "    am_gb_bulkTransfer.\"Asset_id\" AS am_gb_bulkTransfer_Asset_id,\n"
	     		+ "     am_gb_bulkTransfer.\"Registration_No\" AS am_gb_bulkTransfer_Registration_No,\n"
	     		+ "     am_gb_bulkTransfer.\"Description\" AS am_gb_bulkTransfer_Description,\n"
	     		+ "     am_gb_bulkTransfer.\"oldbranch_id\" AS am_gb_bulkTransfer_oldbranch_id,\n"
	     		+ "     am_gb_bulkTransfer.\"olddept_id\" AS am_gb_bulkTransfer_olddept_id,\n"
	     		+ "     am_gb_bulkTransfer.\"oldSBU_CODE\" AS am_gb_bulkTransfer_oldSBU_CODE,\n"
	     		+ "     am_gb_bulkTransfer.\"oldsection_id\" AS am_gb_bulkTransfer_oldsection_id,\n"
	     		+ "     am_gb_bulkTransfer.\"oldAsset_User\" AS am_gb_bulkTransfer_oldAsset_User,\n"
	     		+ "     am_gb_bulkTransfer.\"newbranch_id\" AS am_gb_bulkTransfer_newbranch_id,\n"
	     		+ "     am_gb_bulkTransfer.\"newdept_id\" AS am_gb_bulkTransfer_newdept_id,\n"
	     		+ "     am_gb_bulkTransfer.\"newSBU_CODE\" AS am_gb_bulkTransfer_newSBU_CODE,\n"
	     		+ "     am_gb_bulkTransfer.\"newsection_id\" AS am_gb_bulkTransfer_newsection_id,\n"
	     		+ "     am_gb_bulkTransfer.\"newAsset_User\" AS am_gb_bulkTransfer_newAsset_User,\n"
	     		+ "     am_gb_bulkTransfer.\"Batch_id\" AS am_gb_bulkTransfer_Batch_id,\n"
	     		+ "     am_gb_bulkTransfer.\"Transferby_id\" AS am_gb_bulkTransfer_Transferby_id,\n"
	     		+ "     am_gb_bulkTransfer.\"CATEGORY_ID\" AS am_gb_bulkTransfer_CATEGORY_ID,\n"
	     		+ "     am_gb_bulkTransfer.\"NEW_ASSET_ID\" AS am_gb_bulkTransfer_NEW_ASSET_ID,\n"
	     		+ "     am_gb_bulkTransfer.\"ACCUM_DEP\" AS am_gb_bulkTransfer_ACCUM_DEP,\n"
	     		+ "     am_gb_bulkTransfer.\"MONTHLY_DEP\" AS am_gb_bulkTransfer_MONTHLY_DEP,\n"
	     		+ "     am_gb_bulkTransfer.\"NBV\" AS am_gb_bulkTransfer_NBV,\n"
	     		+ "     am_gb_bulkTransfer.\"TRANSFER_DATE\" AS am_gb_bulkTransfer_TRANSFER_DATE,\n"
	     		+ "     am_gb_bulkTransfer.\"transId\" AS am_gb_bulkTransfer_transId,\n"
	     		+ "     am_gb_bulkTransfer.\"TRANSFER_COST\" AS am_gb_bulkTransfer_TRANSFER_COST,\n"
	     		+ "     am_gb_bulkTransfer.\"asset_code\" AS am_gb_bulkTransfer_asset_code,\n"
	     		+ "     am_ad_branch.\"BRANCH_NAME\" AS am_ad_branch_BRANCH_NAME,\n"
	     		+ "     am_ad_branch_A.\"BRANCH_NAME\" AS am_ad_branch_A_BRANCH_NAME,\n"
	     		+ "     am_gb_User.\"Full_Name\" AS am_gb_User_Full_Name,\n"
	     		+ "     am_gb_User.\"User_Name\" AS am_gb_User_User_Name,\n"
	     		+ "     am_gb_bulkTransfer.\"COST_PRICE\" AS am_gb_bulkTransfer_COST_PRICE\n"
	     		+ "FROM\n"
	     		+ "     \"dbo\".\"am_ad_branch\" am_ad_branch INNER JOIN \"dbo\".\"am_gb_bulkTransfer\" am_gb_bulkTransfer ON am_ad_branch.\"BRANCH_ID\" = am_gb_bulkTransfer.\"oldbranch_id\"\n"
	     		+ "     INNER JOIN \"dbo\".\"am_ad_branch\" am_ad_branch_A ON am_gb_bulkTransfer.\"newbranch_id\" = am_ad_branch_A.\"BRANCH_ID\"\n"
	     		+ "     INNER JOIN \"dbo\".\"am_gb_User\" am_gb_User ON am_gb_bulkTransfer.\"Transferby_id\" = am_gb_User.\"User_id\"\n"
	     		+ "	 WHERE ASSET_ID IS NOT NULL\n"
	     		+ "	 AND am_gb_bulkTransfer.oldbranch_id = ?\n"
	     		+ "	 AND am_gb_bulkTransfer.TRANSFER_DATE BETWEEN ? and ?\n"
	     		+ "ORDER BY\n"
	     		+ "     Batch_id ASC";   
		}  
	 
	 if(!branch_Id.equals("0")  && batch_Id.equals("") && FromDate.equals("") && ToDate.equals("")){	   
		   System.out.println("======>>>>>>>Branch Selected: "+branch_Id+"   batch_Id: "+batch_Id+"    FromDate: "+FromDate+"   ToDate: "+ToDate);
		     ColQuery ="	 SELECT\n"
		     		+ "    am_gb_bulkTransfer.\"Asset_id\" AS am_gb_bulkTransfer_Asset_id,\n"
		     		+ "     am_gb_bulkTransfer.\"Registration_No\" AS am_gb_bulkTransfer_Registration_No,\n"
		     		+ "     am_gb_bulkTransfer.\"Description\" AS am_gb_bulkTransfer_Description,\n"
		     		+ "     am_gb_bulkTransfer.\"oldbranch_id\" AS am_gb_bulkTransfer_oldbranch_id,\n"
		     		+ "     am_gb_bulkTransfer.\"olddept_id\" AS am_gb_bulkTransfer_olddept_id,\n"
		     		+ "     am_gb_bulkTransfer.\"oldSBU_CODE\" AS am_gb_bulkTransfer_oldSBU_CODE,\n"
		     		+ "     am_gb_bulkTransfer.\"oldsection_id\" AS am_gb_bulkTransfer_oldsection_id,\n"
		     		+ "     am_gb_bulkTransfer.\"oldAsset_User\" AS am_gb_bulkTransfer_oldAsset_User,\n"
		     		+ "     am_gb_bulkTransfer.\"newbranch_id\" AS am_gb_bulkTransfer_newbranch_id,\n"
		     		+ "     am_gb_bulkTransfer.\"newdept_id\" AS am_gb_bulkTransfer_newdept_id,\n"
		     		+ "     am_gb_bulkTransfer.\"newSBU_CODE\" AS am_gb_bulkTransfer_newSBU_CODE,\n"
		     		+ "     am_gb_bulkTransfer.\"newsection_id\" AS am_gb_bulkTransfer_newsection_id,\n"
		     		+ "     am_gb_bulkTransfer.\"newAsset_User\" AS am_gb_bulkTransfer_newAsset_User,\n"
		     		+ "     am_gb_bulkTransfer.\"Batch_id\" AS am_gb_bulkTransfer_Batch_id,\n"
		     		+ "     am_gb_bulkTransfer.\"Transferby_id\" AS am_gb_bulkTransfer_Transferby_id,\n"
		     		+ "     am_gb_bulkTransfer.\"CATEGORY_ID\" AS am_gb_bulkTransfer_CATEGORY_ID,\n"
		     		+ "     am_gb_bulkTransfer.\"NEW_ASSET_ID\" AS am_gb_bulkTransfer_NEW_ASSET_ID,\n"
		     		+ "     am_gb_bulkTransfer.\"ACCUM_DEP\" AS am_gb_bulkTransfer_ACCUM_DEP,\n"
		     		+ "     am_gb_bulkTransfer.\"MONTHLY_DEP\" AS am_gb_bulkTransfer_MONTHLY_DEP,\n"
		     		+ "     am_gb_bulkTransfer.\"NBV\" AS am_gb_bulkTransfer_NBV,\n"
		     		+ "     am_gb_bulkTransfer.\"TRANSFER_DATE\" AS am_gb_bulkTransfer_TRANSFER_DATE,\n"
		     		+ "     am_gb_bulkTransfer.\"transId\" AS am_gb_bulkTransfer_transId,\n"
		     		+ "     am_gb_bulkTransfer.\"TRANSFER_COST\" AS am_gb_bulkTransfer_TRANSFER_COST,\n"
		     		+ "     am_gb_bulkTransfer.\"asset_code\" AS am_gb_bulkTransfer_asset_code,\n"
		     		+ "     am_ad_branch.\"BRANCH_NAME\" AS am_ad_branch_BRANCH_NAME,\n"
		     		+ "     am_ad_branch_A.\"BRANCH_NAME\" AS am_ad_branch_A_BRANCH_NAME,\n"
		     		+ "     am_gb_User.\"Full_Name\" AS am_gb_User_Full_Name,\n"
		     		+ "     am_gb_User.\"User_Name\" AS am_gb_User_User_Name,\n"
		     		+ "     am_gb_bulkTransfer.\"COST_PRICE\" AS am_gb_bulkTransfer_COST_PRICE\n"
		     		+ "FROM\n"
		     		+ "     \"dbo\".\"am_ad_branch\" am_ad_branch INNER JOIN \"dbo\".\"am_gb_bulkTransfer\" am_gb_bulkTransfer ON am_ad_branch.\"BRANCH_ID\" = am_gb_bulkTransfer.\"oldbranch_id\"\n"
		     		+ "     INNER JOIN \"dbo\".\"am_ad_branch\" am_ad_branch_A ON am_gb_bulkTransfer.\"newbranch_id\" = am_ad_branch_A.\"BRANCH_ID\"\n"
		     		+ "     INNER JOIN \"dbo\".\"am_gb_User\" am_gb_User ON am_gb_bulkTransfer.\"Transferby_id\" = am_gb_User.\"User_id\"\n"
		     		+ "	 WHERE ASSET_ID IS NOT NULL\n"
		     		+ "	 AND am_gb_bulkTransfer.oldbranch_id = ?\n"
		     		+ "ORDER BY\n"
		     		+ "     Batch_id ASC";   
			}      

	 if(branch_Id.equals("0")  && !batch_Id.equals("") && !FromDate.equals("") && !ToDate.equals("")){	   
	   System.out.println("======>>>>>>>Batch Id and Date Selected: "+branch_Id+"   batch_Id: "+batch_Id+"    FromDate: "+FromDate+"   ToDate: "+ToDate);
	     ColQuery =" SELECT\n"
	     		+ "    am_gb_bulkTransfer.\"Asset_id\" AS am_gb_bulkTransfer_Asset_id,\n"
	     		+ "     am_gb_bulkTransfer.\"Registration_No\" AS am_gb_bulkTransfer_Registration_No,\n"
	     		+ "     am_gb_bulkTransfer.\"Description\" AS am_gb_bulkTransfer_Description,\n"
	     		+ "     am_gb_bulkTransfer.\"oldbranch_id\" AS am_gb_bulkTransfer_oldbranch_id,\n"
	     		+ "     am_gb_bulkTransfer.\"olddept_id\" AS am_gb_bulkTransfer_olddept_id,\n"
	     		+ "     am_gb_bulkTransfer.\"oldSBU_CODE\" AS am_gb_bulkTransfer_oldSBU_CODE,\n"
	     		+ "     am_gb_bulkTransfer.\"oldsection_id\" AS am_gb_bulkTransfer_oldsection_id,\n"
	     		+ "     am_gb_bulkTransfer.\"oldAsset_User\" AS am_gb_bulkTransfer_oldAsset_User,\n"
	     		+ "     am_gb_bulkTransfer.\"newbranch_id\" AS am_gb_bulkTransfer_newbranch_id,\n"
	     		+ "     am_gb_bulkTransfer.\"newdept_id\" AS am_gb_bulkTransfer_newdept_id,\n"
	     		+ "     am_gb_bulkTransfer.\"newSBU_CODE\" AS am_gb_bulkTransfer_newSBU_CODE,\n"
	     		+ "     am_gb_bulkTransfer.\"newsection_id\" AS am_gb_bulkTransfer_newsection_id,\n"
	     		+ "     am_gb_bulkTransfer.\"newAsset_User\" AS am_gb_bulkTransfer_newAsset_User,\n"
	     		+ "     am_gb_bulkTransfer.\"Batch_id\" AS am_gb_bulkTransfer_Batch_id,\n"
	     		+ "     am_gb_bulkTransfer.\"Transferby_id\" AS am_gb_bulkTransfer_Transferby_id,\n"
	     		+ "     am_gb_bulkTransfer.\"CATEGORY_ID\" AS am_gb_bulkTransfer_CATEGORY_ID,\n"
	     		+ "     am_gb_bulkTransfer.\"NEW_ASSET_ID\" AS am_gb_bulkTransfer_NEW_ASSET_ID,\n"
	     		+ "     am_gb_bulkTransfer.\"ACCUM_DEP\" AS am_gb_bulkTransfer_ACCUM_DEP,\n"
	     		+ "     am_gb_bulkTransfer.\"MONTHLY_DEP\" AS am_gb_bulkTransfer_MONTHLY_DEP,\n"
	     		+ "     am_gb_bulkTransfer.\"NBV\" AS am_gb_bulkTransfer_NBV,\n"
	     		+ "     am_gb_bulkTransfer.\"TRANSFER_DATE\" AS am_gb_bulkTransfer_TRANSFER_DATE,\n"
	     		+ "     am_gb_bulkTransfer.\"transId\" AS am_gb_bulkTransfer_transId,\n"
	     		+ "     am_gb_bulkTransfer.\"TRANSFER_COST\" AS am_gb_bulkTransfer_TRANSFER_COST,\n"
	     		+ "     am_gb_bulkTransfer.\"asset_code\" AS am_gb_bulkTransfer_asset_code,\n"
	     		+ "     am_ad_branch.\"BRANCH_NAME\" AS am_ad_branch_BRANCH_NAME,\n"
	     		+ "     am_ad_branch_A.\"BRANCH_NAME\" AS am_ad_branch_A_BRANCH_NAME,\n"
	     		+ "     am_gb_User.\"Full_Name\" AS am_gb_User_Full_Name,\n"
	     		+ "     am_gb_User.\"User_Name\" AS am_gb_User_User_Name,\n"
	     		+ "     am_gb_bulkTransfer.\"COST_PRICE\" AS am_gb_bulkTransfer_COST_PRICE\n"
	     		+ "FROM\n"
	     		+ "     \"dbo\".\"am_ad_branch\" am_ad_branch INNER JOIN \"dbo\".\"am_gb_bulkTransfer\" am_gb_bulkTransfer ON am_ad_branch.\"BRANCH_ID\" = am_gb_bulkTransfer.\"oldbranch_id\"\n"
	     		+ "     INNER JOIN \"dbo\".\"am_ad_branch\" am_ad_branch_A ON am_gb_bulkTransfer.\"newbranch_id\" = am_ad_branch_A.\"BRANCH_ID\"\n"
	     		+ "     INNER JOIN \"dbo\".\"am_gb_User\" am_gb_User ON am_gb_bulkTransfer.\"Transferby_id\" = am_gb_User.\"User_id\"\n"
	     		+ "	 WHERE ASSET_ID IS NOT NULL\n"
	     		+ "	 AND am_gb_bulkTransfer.batch_id = ?\n"
	     		+ "	 AND am_gb_bulkTransfer.TRANSFER_DATE BETWEEN ? and ?\n"
	     		+ "ORDER BY\n"
	     		+ "     Batch_id ASC";    
		}      
	 
	 if(branch_Id.equals("0")  && !batch_Id.equals("") && FromDate.equals("") && ToDate.equals("")){	   
		   System.out.println("======>>>>>>>batch_Id Selected: "+branch_Id+"   batch_Id: "+batch_Id+"    FromDate: "+FromDate+"   ToDate: "+ToDate);
		     ColQuery =" SELECT\n"
		     		+ "    am_gb_bulkTransfer.\"Asset_id\" AS am_gb_bulkTransfer_Asset_id,\n"
		     		+ "     am_gb_bulkTransfer.\"Registration_No\" AS am_gb_bulkTransfer_Registration_No,\n"
		     		+ "     am_gb_bulkTransfer.\"Description\" AS am_gb_bulkTransfer_Description,\n"
		     		+ "     am_gb_bulkTransfer.\"oldbranch_id\" AS am_gb_bulkTransfer_oldbranch_id,\n"
		     		+ "     am_gb_bulkTransfer.\"olddept_id\" AS am_gb_bulkTransfer_olddept_id,\n"
		     		+ "     am_gb_bulkTransfer.\"oldSBU_CODE\" AS am_gb_bulkTransfer_oldSBU_CODE,\n"
		     		+ "     am_gb_bulkTransfer.\"oldsection_id\" AS am_gb_bulkTransfer_oldsection_id,\n"
		     		+ "     am_gb_bulkTransfer.\"oldAsset_User\" AS am_gb_bulkTransfer_oldAsset_User,\n"
		     		+ "     am_gb_bulkTransfer.\"newbranch_id\" AS am_gb_bulkTransfer_newbranch_id,\n"
		     		+ "     am_gb_bulkTransfer.\"newdept_id\" AS am_gb_bulkTransfer_newdept_id,\n"
		     		+ "     am_gb_bulkTransfer.\"newSBU_CODE\" AS am_gb_bulkTransfer_newSBU_CODE,\n"
		     		+ "     am_gb_bulkTransfer.\"newsection_id\" AS am_gb_bulkTransfer_newsection_id,\n"
		     		+ "     am_gb_bulkTransfer.\"newAsset_User\" AS am_gb_bulkTransfer_newAsset_User,\n"
		     		+ "     am_gb_bulkTransfer.\"Batch_id\" AS am_gb_bulkTransfer_Batch_id,\n"
		     		+ "     am_gb_bulkTransfer.\"Transferby_id\" AS am_gb_bulkTransfer_Transferby_id,\n"
		     		+ "     am_gb_bulkTransfer.\"CATEGORY_ID\" AS am_gb_bulkTransfer_CATEGORY_ID,\n"
		     		+ "     am_gb_bulkTransfer.\"NEW_ASSET_ID\" AS am_gb_bulkTransfer_NEW_ASSET_ID,\n"
		     		+ "     am_gb_bulkTransfer.\"ACCUM_DEP\" AS am_gb_bulkTransfer_ACCUM_DEP,\n"
		     		+ "     am_gb_bulkTransfer.\"MONTHLY_DEP\" AS am_gb_bulkTransfer_MONTHLY_DEP,\n"
		     		+ "     am_gb_bulkTransfer.\"NBV\" AS am_gb_bulkTransfer_NBV,\n"
		     		+ "     am_gb_bulkTransfer.\"TRANSFER_DATE\" AS am_gb_bulkTransfer_TRANSFER_DATE,\n"
		     		+ "     am_gb_bulkTransfer.\"transId\" AS am_gb_bulkTransfer_transId,\n"
		     		+ "     am_gb_bulkTransfer.\"TRANSFER_COST\" AS am_gb_bulkTransfer_TRANSFER_COST,\n"
		     		+ "     am_gb_bulkTransfer.\"asset_code\" AS am_gb_bulkTransfer_asset_code,\n"
		     		+ "     am_ad_branch.\"BRANCH_NAME\" AS am_ad_branch_BRANCH_NAME,\n"
		     		+ "     am_ad_branch_A.\"BRANCH_NAME\" AS am_ad_branch_A_BRANCH_NAME,\n"
		     		+ "     am_gb_User.\"Full_Name\" AS am_gb_User_Full_Name,\n"
		     		+ "     am_gb_User.\"User_Name\" AS am_gb_User_User_Name,\n"
		     		+ "     am_gb_bulkTransfer.\"COST_PRICE\" AS am_gb_bulkTransfer_COST_PRICE\n"
		     		+ "FROM\n"
		     		+ "     \"dbo\".\"am_ad_branch\" am_ad_branch INNER JOIN \"dbo\".\"am_gb_bulkTransfer\" am_gb_bulkTransfer ON am_ad_branch.\"BRANCH_ID\" = am_gb_bulkTransfer.\"oldbranch_id\"\n"
		     		+ "     INNER JOIN \"dbo\".\"am_ad_branch\" am_ad_branch_A ON am_gb_bulkTransfer.\"newbranch_id\" = am_ad_branch_A.\"BRANCH_ID\"\n"
		     		+ "     INNER JOIN \"dbo\".\"am_gb_User\" am_gb_User ON am_gb_bulkTransfer.\"Transferby_id\" = am_gb_User.\"User_id\"\n"
		     		+ "	 WHERE ASSET_ID IS NOT NULL\n"
		     		+ "	 AND am_gb_bulkTransfer.batch_id = ?\n"
		     		+ "ORDER BY\n"
		     		+ "     Batch_id ASC";    
			}      
	 
     
//   System.out.println("======>>>>>>>ColQuery: "+ColQuery);
     java.util.ArrayList list =rep.getBulkTransferListRecords(ColQuery,branch_Id,batch_Id,FromDate,ToDate);
     System.out.println("======>>>>>>>list size: "+list.size()+"        =====report: "+report);
     if(list.size()!=0){
   	 if(report.equalsIgnoreCase("rptMenuBulktransferList")){
    	 SXSSFWorkbook workbook = new SXSSFWorkbook();
         Sheet sheet = workbook.createSheet("Demo");
         Row rowhead = sheet.createRow((int) 0);
         
         rowhead.createCell( 0).setCellValue("S/No.");
         rowhead.createCell( 1).setCellValue("Asset Id");
         rowhead.createCell( 2).setCellValue("Registration No.");
         rowhead.createCell( 3).setCellValue("Description");
         rowhead.createCell( 4).setCellValue("Old Branch Id");
         rowhead.createCell( 5).setCellValue("Old Dept Id");
         rowhead.createCell( 6).setCellValue("Old Sbu Code");
         rowhead.createCell( 7).setCellValue("Old Section Id");
         rowhead.createCell( 8).setCellValue("Old Asset User");
         rowhead.createCell( 9).setCellValue("New Branch Id");
		 rowhead.createCell( 10).setCellValue("New Dept Id");
		 rowhead.createCell( 11).setCellValue("New Sbu Code");
		 rowhead.createCell( 12).setCellValue("New Section Id");
		 rowhead.createCell( 13).setCellValue("New Asset User");
		 rowhead.createCell( 14).setCellValue("Batch Id");
		 rowhead.createCell( 15).setCellValue("TransferBy Id");
		 rowhead.createCell( 16).setCellValue("Category Id");
		 rowhead.createCell( 17).setCellValue("New Asset Id");
		 rowhead.createCell( 18).setCellValue("Accum Dep");
         rowhead.createCell( 19).setCellValue("Monthly Dep");
         rowhead.createCell( 20).setCellValue("NBV");
         rowhead.createCell( 21).setCellValue("Transfer Date");
         rowhead.createCell( 22).setCellValue("TransId");
         rowhead.createCell( 23).setCellValue("Transfer Cost");
         rowhead.createCell( 24).setCellValue("Asset Code");
         rowhead.createCell( 25).setCellValue("Old Branch Name");
         rowhead.createCell( 26).setCellValue("New Branch Name");
         rowhead.createCell( 27).setCellValue("Full Name");
         rowhead.createCell( 28).setCellValue("User Name");
         rowhead.createCell( 29).setCellValue("Cost Price");
         
         

     int i = 1;
     System.out.println("<<<<<<list.size(): "+list.size());
	 for(int k=0;k<list.size();k++)
     {
    	 com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);    	 
    	 
         	String asset_Id = newassettrans.getAssetId();
			String registrationNo = newassettrans.getRegistrationNo();
			String description = newassettrans.getDescription();
			String oldBranchId = newassettrans.getOldBranchId();
			String oldDeptId = newassettrans.getOldDeptId();
			String oldSbuCode = newassettrans.getOldsbuCode();
			String oldSectionId = newassettrans.getOldSection();
			String oldAssetUser = newassettrans.getOldAssetUser();
			String newBranchId = newassettrans.getNewBranchCode();
			String newDeptId = newassettrans.getNewDeptCode();
			String newSbuCode = newassettrans.getNewsbuCode();
			String newSectionId = newassettrans.getNewSectionCode();
			String newAssetUser = newassettrans.getAssetUser();
			String batchId = newassettrans.getBarCode();
			String transferById = newassettrans.getComments();
			String categoryId = newassettrans.getCategoryCode();
			String newAssetId = newassettrans.getNewAssetId();
			double accumDep = newassettrans.getAccumDep();
			double monthlyDep = newassettrans.getMonthlyDep();
			double nbv = newassettrans.getNbv();
			String transfer_Date = newassettrans.getTransDate() != null ? getDate(newassettrans.getTransDate()) : "";
			String transId = newassettrans.getTranType();
			double transfer_Cost = newassettrans.getTotalCost();
			String assetCode = newassettrans.getAssetCode();
			String old_BranchName = newassettrans.getCategoryName();
			String new_BranchName = newassettrans.getBranchName();
			String full_Name = newassettrans.getAssetfunction();
			String user_Name = newassettrans.getUserID();
			double costPrice = newassettrans.getCostPrice();

			//			String vendorName = records.getCodeName("select VENDOR_NAME from am_ad_vendor where VENDOR_ID = "+vendorId+"");
			

			Row row = sheet.createRow((int) i);

			row.createCell((short) 0).setCellValue(i);
			row.createCell((short) 1).setCellValue(asset_Id);
            row.createCell((short) 2).setCellValue(registrationNo);
            row.createCell((short) 3).setCellValue(description);
            row.createCell((short) 4).setCellValue(oldBranchId);
            row.createCell((short) 5).setCellValue(oldDeptId);
            row.createCell((short) 6).setCellValue(oldSbuCode);
            row.createCell((short) 7).setCellValue(oldSectionId);
            row.createCell((short) 8).setCellValue(oldAssetUser);
            row.createCell((short) 9).setCellValue(newBranchId);
			row.createCell((short) 10).setCellValue(newDeptId);
			row.createCell((short) 11).setCellValue(newSbuCode);
			row.createCell((short) 12).setCellValue(newSectionId);
			row.createCell((short) 13).setCellValue(newAssetUser);
			row.createCell((short) 14).setCellValue(batchId);
            row.createCell((short) 15).setCellValue(transferById);
            row.createCell((short) 16).setCellValue(categoryId);
            row.createCell((short) 17).setCellValue(newAssetId);
            row.createCell((short) 18).setCellValue(accumDep);
            row.createCell((short) 19).setCellValue(monthlyDep);
            row.createCell((short) 20).setCellValue(nbv);
            row.createCell((short) 21).setCellValue(transfer_Date);
            row.createCell((short) 22).setCellValue(transId);
            row.createCell((short) 23).setCellValue(transfer_Cost);
            row.createCell((short) 24).setCellValue(assetCode);
            row.createCell((short) 25).setCellValue(old_BranchName);
            row.createCell((short) 26).setCellValue(new_BranchName);
        	row.createCell((short) 27).setCellValue(full_Name);
			row.createCell((short) 28).setCellValue(user_Name);
			row.createCell((short) 29).setCellValue(costPrice);

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