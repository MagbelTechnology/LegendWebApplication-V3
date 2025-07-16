package com.magbel.legend.servlet;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import magma.AssetRecordsBean;
   
public class LegacyLegendTransactionComaprismReport extends HttpServlet
{
	private EmailSmsServiceBus mail ;
	private AssetRecordsBean ad;
	private ApprovalRecords records;
   public void doPost(HttpServletRequest request, 
    HttpServletResponse response)
      throws ServletException, IOException
     
   {
	   Properties prop = new Properties();
	   File file = new File("C:\\Property\\LegendPlus.properties");
	   FileInputStream input = new FileInputStream(file);
	   prop.load(input);

	   String BankingApp = prop.getProperty("BankingApp");	
	   
	 String userClass = (String) request.getSession().getAttribute("UserClass");

	 String branch_Code = (String) request.getSession().getAttribute("UserCenter");
	 
	 String userName = (String) request.getSession().getAttribute("SignInName");
	 
	 if (!userClass.equals("NULL") || userClass!=null){
//	   PrintWriter out = response.getWriter();
//    OutputStream out = null; 
	mail= new EmailSmsServiceBus();
	records = new ApprovalRecords();
//    String branch_Code = request.getParameter("initiatorSOLID");
    String branch_Id = request.getParameter("branch");
//    String userName = request.getParameter("user");
    String start_Date = request.getParameter("FromDate");
    String end_Date = request.getParameter("ToDate");   
    end_Date = end_Date+" 23:59:59:000";
    String endDate = "";
    String startDate = "";
//    System.out.println("<<<<<<startDate: "+start_Date+"   end_Date: "+end_Date);
    if(!start_Date.equals("")){
    String dd = start_Date.substring(0,2);
    String mm = start_Date.substring(3,5);
    String yyyy = start_Date.substring(6,10);
    startDate = yyyy+"-"+mm+"-"+dd;
    }
    if(!end_Date.equals("")){
    String enddd = end_Date.substring(0,2);
    String endmm = end_Date.substring(3,5);
    String endyyyy = end_Date.substring(6,10);
    endDate = endyyyy+"-"+endmm+"-"+enddd;
    endDate = endDate+" 23:59:59:000";
    }    
    branch_Code = records.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID = ? ",branch_Code);
    //String branchCode = request.getParameter("BRANCH_CODE");
    System.out.println("<<<<<<branch_Id: "+branch_Id);
    String branchCode = "";
    if(!branch_Id.equals("***")){
    	branchCode = records.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID = ? ",branch_Id);
    }
    System.out.println("<<<<<<branchCode: "+branchCode+"     branch_Code: "+branch_Code);
    String fileName = branch_Code+"By"+userName+"LegacyTransactionDownloadReport.xlsx";    	
    String filePath = System.getProperty("user.home")+"\\Downloads";
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
    response.setContentType("application/vnd.ms-excel");
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
     if(!branch_Id.equals("***") && !start_Date.equals("") && !end_Date.equals("")){
//    	 System.out.println("======>>>>>>>Branch Selected: ");
	     ColQuery ="SELECT "
	     		+ "AM_GB_BATCH_POSTING.GROUP_ID AS GROUP_ID,"
	     		+ "AM_GB_BATCH_POSTING.ID_GROUP_ID AS ID_GROUP_ID,"
	     		+ "AM_GB_BATCH_POSTING.BATCH_NO AS BATCH_NO,"
	     		+ "AM_GB_BATCH_POSTING.BRANCH_CODE AS BRANCH_CODE,"
	     		+ "AM_GB_BATCH_POSTING.DATE_FIELD AS DATE_FIELD,"
	     		+ "AM_GB_BATCH_POSTING.ACCOUNT_NO AS ACCOUNT_NO,"
	     		+ "AM_GB_BATCH_POSTING.DESCRIPTION AS DESCRIPTION,"
	     		+ "AM_GB_BATCH_POSTING.cost_price AS cost_price,"
	     		+ "AM_GB_BATCH_POSTING.User_ID AS User_ID,"
	     		+ "AM_GB_BATCH_POSTING.TRANSTYPE AS TRANSTYPE,"
	     		+ "AM_GB_BATCH_POSTING.TRANSCODE AS TRANSCODE,"
	     		+ "AM_GB_BATCH_POSTING.CURRENCY AS CURRENCY,"
	     		+ "AM_GB_BATCH_POSTING.PURPOSE_CODE AS PURPOSE_CODE,"
	     		+ "AM_GB_BATCH_POSTING.MAKER AS MAKER,"
	     		+ "AM_GB_BATCH_POSTING.CHECKER AS CHECKER,"
	     		+ "LEGACY_TRANSACTION.AMOUNT AS AMOUNT,"
	     		+ "AM_GB_BATCH_POSTING.cost_price - coalesce(LEGACY_TRANSACTION.AMOUNT,0) AS COST_DIFFERENCE "
	     		+ "FROM "
	     		+ "dbo.LEGACY_TRANSACTION LEGACY_TRANSACTION RIGHT OUTER JOIN dbo.AM_GB_BATCH_POSTING AM_GB_BATCH_POSTING ON LEGACY_TRANSACTION.BATCH_NO = AM_GB_BATCH_POSTING.BATCH_NO "
	     		+ "AND LEGACY_TRANSACTION.ACCOUNT_NO = AM_GB_BATCH_POSTING.ACCOUNT_NO "
	     		+ "AND LEGACY_TRANSACTION.SERIAL_NO = AM_GB_BATCH_POSTING.ID "
	     		+ "AND LEGACY_TRANSACTION.POSTING_DATE = CONVERT(DATETIME,AM_GB_BATCH_POSTING.DATE_FIELD,105) "
	     		+ "WHERE "
	     		+ "AM_GB_BATCH_POSTING.User_ID <> 0 "
	     		+ "AND AM_GB_BATCH_POSTING.BRANCH_CODE = '"+branchCode+"' "
	     		+ "AND (SUBSTRING(DATE_FIELD,7,4)+'-'+SUBSTRING(DATE_FIELD,4,2)+'-'+SUBSTRING(DATE_FIELD,0,3)) BETWEEN '"+startDate+"' AND '"+endDate+"' "
	     		+ "ORDER BY "
	     		+ "AM_GB_BATCH_POSTING.BATCH_NO ASC,"
	     		+ "AM_GB_BATCH_POSTING.BRANCH_CODE ASC ";
	}      
	 if(branch_Id.equals("***") && !start_Date.equals("") && !end_Date.equals("")){	   
//	   System.out.println("======>>>>>>>Category Selected: ");
	     ColQuery ="SELECT "
		     		+ "AM_GB_BATCH_POSTING.GROUP_ID AS GROUP_ID,"
		     		+ "AM_GB_BATCH_POSTING.ID_GROUP_ID AS ID_GROUP_ID,"
		     		+ "AM_GB_BATCH_POSTING.BATCH_NO AS BATCH_NO,"
		     		+ "AM_GB_BATCH_POSTING.BRANCH_CODE AS BRANCH_CODE,"
		     		+ "AM_GB_BATCH_POSTING.DATE_FIELD AS DATE_FIELD,"
		     		+ "AM_GB_BATCH_POSTING.ACCOUNT_NO AS ACCOUNT_NO,"
		     		+ "AM_GB_BATCH_POSTING.DESCRIPTION AS DESCRIPTION,"
		     		+ "AM_GB_BATCH_POSTING.cost_price AS cost_price,"
		     		+ "AM_GB_BATCH_POSTING.User_ID AS User_ID,"
		     		+ "AM_GB_BATCH_POSTING.TRANSTYPE AS TRANSTYPE,"
		     		+ "AM_GB_BATCH_POSTING.TRANSCODE AS TRANSCODE,"
		     		+ "AM_GB_BATCH_POSTING.CURRENCY AS CURRENCY,"
		     		+ "AM_GB_BATCH_POSTING.PURPOSE_CODE AS PURPOSE_CODE,"
		     		+ "AM_GB_BATCH_POSTING.MAKER AS MAKER,"
		     		+ "AM_GB_BATCH_POSTING.CHECKER AS CHECKER,"
		     		+ "LEGACY_TRANSACTION.AMOUNT AS AMOUNT,"
		     		+ "AM_GB_BATCH_POSTING.cost_price - coalesce(LEGACY_TRANSACTION.AMOUNT,0) AS COST_DIFFERENCE "
		     		+ "FROM "
		     		+ "dbo.LEGACY_TRANSACTION LEGACY_TRANSACTION RIGHT OUTER JOIN dbo.AM_GB_BATCH_POSTING AM_GB_BATCH_POSTING ON LEGACY_TRANSACTION.BATCH_NO = AM_GB_BATCH_POSTING.BATCH_NO "
		     		+ "AND LEGACY_TRANSACTION.ACCOUNT_NO = AM_GB_BATCH_POSTING.ACCOUNT_NO "
		     		+ "AND LEGACY_TRANSACTION.SERIAL_NO = AM_GB_BATCH_POSTING.ID "
		     		+ "AND LEGACY_TRANSACTION.POSTING_DATE = CONVERT(DATETIME,AM_GB_BATCH_POSTING.DATE_FIELD,105) "
		     		+ "WHERE "
		     		+ " AM_GB_BATCH_POSTING.User_ID <> 0 "
		     		+ "AND (SUBSTRING(DATE_FIELD,7,4)+'-'+SUBSTRING(DATE_FIELD,4,2)+'-'+SUBSTRING(DATE_FIELD,0,3)) BETWEEN '"+startDate+"' AND '"+endDate+"' "
		     		+ "ORDER BY "
		     		+ "AM_GB_BATCH_POSTING.BATCH_NO ASC,"
		     		+ "AM_GB_BATCH_POSTING.BRANCH_CODE ASC ";   
	     }
   if(!branch_Id.equals("***") && start_Date.equals("") && end_Date.equals("")){
//	   System.out.println("======>>>>>>>No Selection: ");
	     ColQuery ="SELECT "
		     		+ "AM_GB_BATCH_POSTING.GROUP_ID AS GROUP_ID,"
		     		+ "AM_GB_BATCH_POSTING.ID_GROUP_ID AS ID_GROUP_ID,"
		     		+ "AM_GB_BATCH_POSTING.BATCH_NO AS BATCH_NO,"
		     		+ "AM_GB_BATCH_POSTING.BRANCH_CODE AS BRANCH_CODE,"
		     		+ "AM_GB_BATCH_POSTING.DATE_FIELD AS DATE_FIELD,"
		     		+ "AM_GB_BATCH_POSTING.ACCOUNT_NO AS ACCOUNT_NO,"
		     		+ "AM_GB_BATCH_POSTING.DESCRIPTION AS DESCRIPTION,"
		     		+ "AM_GB_BATCH_POSTING.cost_price AS cost_price,"
		     		+ "AM_GB_BATCH_POSTING.User_ID AS User_ID,"
		     		+ "AM_GB_BATCH_POSTING.TRANSTYPE AS TRANSTYPE,"
		     		+ "AM_GB_BATCH_POSTING.TRANSCODE AS TRANSCODE,"
		     		+ "AM_GB_BATCH_POSTING.CURRENCY AS CURRENCY,"
		     		+ "AM_GB_BATCH_POSTING.PURPOSE_CODE AS PURPOSE_CODE,"
		     		+ "AM_GB_BATCH_POSTING.MAKER AS MAKER,"
		     		+ "AM_GB_BATCH_POSTING.CHECKER AS CHECKER,"
		     		+ "LEGACY_TRANSACTION.AMOUNT AS AMOUNT,"
		     		+ "AM_GB_BATCH_POSTING.cost_price - coalesce(LEGACY_TRANSACTION.AMOUNT,0) AS COST_DIFFERENCE "
		     		+ "FROM "
		     		+ "dbo.LEGACY_TRANSACTION LEGACY_TRANSACTION RIGHT OUTER JOIN dbo.AM_GB_BATCH_POSTING AM_GB_BATCH_POSTING ON LEGACY_TRANSACTION.BATCH_NO = AM_GB_BATCH_POSTING.BATCH_NO "
		     		+ "AND LEGACY_TRANSACTION.ACCOUNT_NO = AM_GB_BATCH_POSTING.ACCOUNT_NO "
		     		+ "AND LEGACY_TRANSACTION.SERIAL_NO = AM_GB_BATCH_POSTING.ID "
		     		+ "AND LEGACY_TRANSACTION.POSTING_DATE = CONVERT(DATETIME,AM_GB_BATCH_POSTING.DATE_FIELD,105) "
		     		+ "WHERE "
		     		+ " AM_GB_BATCH_POSTING.User_ID <> 0 "
		     		+ "AND AM_GB_BATCH_POSTING.BRANCH_CODE = '"+branchCode+"' "
//		     		+ " AND LEGACY_TRANSACTION.POSTING_DATE BETWEEN '"+startDate+"' AND '"+endDate+"' "
		     		+ "ORDER BY "
		     		+ "AM_GB_BATCH_POSTING.BATCH_NO ASC,"
		     		+ "AM_GB_BATCH_POSTING.BRANCH_CODE ASC ";	
	     }   
//   System.out.println("======>>>>>>>ColQuery: "+ColQuery);
     java.util.ArrayList list =rep.getLegendLegacyTransactionComparismRecords(ColQuery,branch_Id,startDate,endDate,BankingApp);
     if(list.size()!=0){
	 
    	 SXSSFWorkbook workbook = new SXSSFWorkbook();
         Sheet sheet = workbook.createSheet("Demo");
         Row rowhead = sheet.createRow((int) 0);
      if(BankingApp.equalsIgnoreCase("FLEXICUBE")) {          
         rowhead.createCell((short) 0).setCellValue("BATCH NO");
         rowhead.createCell((short) 1).setCellValue("SERIAL No.");
         rowhead.createCell((short) 2).setCellValue("ACCOUNT NO");
         rowhead.createCell((short) 3).setCellValue("BRANCH CODE");
         rowhead.createCell((short) 4).setCellValue("TRANSACTION TYPE");
         rowhead.createCell((short) 5).setCellValue("LEGEND COST PRICE");
         rowhead.createCell((short) 6).setCellValue("LEGACY COST PRICE");
         rowhead.createCell((short) 7).setCellValue("COST DIFFERENCE (LEGEND COST - LEGACY COST)");
         rowhead.createCell((short) 8).setCellValue("TRANSACTION DESCRIPTION");
         rowhead.createCell((short) 9).setCellValue("MAKER ID");
         rowhead.createCell((short) 10).setCellValue("CHECKER ID");
         rowhead.createCell((short) 11).setCellValue("POSTING DATE");
         
         int i = 1;
//     System.out.println("<<<<<<list.size(): "+list.size());
	 for(int k=0;k<list.size();k++)
     {
    	 com.magbel.legend.vao.newAssetTransaction  newTransaction = (com.magbel.legend.vao.newAssetTransaction)list.get(k);    	 
			String makerId = newTransaction.getAssetId();
			String serialNo = newTransaction.getBarCode();
			String checkerId = newTransaction.getSbuCode();
			String transDescription = newTransaction.getDescription();
			String batchNo = newTransaction.getAssetUser();
			String accountNo = newTransaction.getVendorAC();
			double costPrice = newTransaction.getCostPrice();
			double amount = newTransaction.getAmount();
			double cost_Difference = newTransaction.getAccumDifference();
			String tranType = newTransaction.getAssetType();
			branchCode = newTransaction.getBranchCode();
			String postingDate = newTransaction.getPostingDate();
			String blankField = "";
			Row row = sheet.createRow((int) i);

            row.createCell((short) 0).setCellValue(batchNo);
            row.createCell((short) 1).setCellValue(serialNo);
            row.createCell((short) 2).setCellValue(accountNo);
            row.createCell((short) 3).setCellValue(branchCode);
            row.createCell((short) 4).setCellValue(tranType);
            row.createCell((short) 5).setCellValue(costPrice);
            row.createCell((short) 6).setCellValue(amount);
            row.createCell((short) 7).setCellValue(cost_Difference);
            row.createCell((short) 8).setCellValue(transDescription);
            row.createCell((short) 9).setCellValue(makerId);
            row.createCell((short) 10).setCellValue(checkerId);
            row.createCell((short) 11).setCellValue(postingDate);

            i++;
     }
     }
      if(BankingApp.equalsIgnoreCase("FINACLE")) {          
          rowhead.createCell((short) 0).setCellValue("BATCH NO");
          rowhead.createCell((short) 1).setCellValue("SERIAL No.");
          rowhead.createCell((short) 2).setCellValue("ACCOUNT NO");
          rowhead.createCell((short) 3).setCellValue("BRANCH CODE");
          rowhead.createCell((short) 4).setCellValue("TRANSACTION TYPE");
          rowhead.createCell((short) 5).setCellValue("AMOUNT");
          rowhead.createCell((short) 6).setCellValue("COST DIFFERENCE (LEGEND COST - LEGACY COST)");
          rowhead.createCell((short) 7).setCellValue("TRANSACTION DESCRIPTION");
          rowhead.createCell((short) 8).setCellValue("MAKER ID");
          rowhead.createCell((short) 9).setCellValue("CHECKER ID");
          rowhead.createCell((short) 10).setCellValue("POSTING DATE");
          
          int i = 1;
      System.out.println("<<<<<<list.size(): "+list.size());
 	 for(int k=0;k<list.size();k++)
      {
     	 com.magbel.legend.vao.newAssetTransaction  newTransaction = (com.magbel.legend.vao.newAssetTransaction)list.get(k);    	 
 			String makerId = newTransaction.getAssetId();
 			String serialNo = newTransaction.getBarCode();
 			String checkerId = newTransaction.getSbuCode();
 			String transDescription = newTransaction.getDescription();
 			String batchNo = newTransaction.getAssetUser();
 			String accountNo = newTransaction.getVendorAC();
 			double amount = newTransaction.getCostPrice();
 			double cost_Difference = newTransaction.getCostPrice();
 			String tranType = newTransaction.getAssetType();
 			branchCode = newTransaction.getBranchCode();
 			String postingDate = newTransaction.getPostingDate();
 			String blankField = "";
 			Row row = sheet.createRow((int) i);

             row.createCell((short) 0).setCellValue(batchNo);
             row.createCell((short) 1).setCellValue(serialNo);
             row.createCell((short) 2).setCellValue(accountNo);
             row.createCell((short) 3).setCellValue(branchCode);
             row.createCell((short) 4).setCellValue(tranType);
             row.createCell((short) 5).setCellValue(amount);
             row.createCell((short) 6).setCellValue(cost_Difference);
             row.createCell((short) 7).setCellValue(transDescription);
             row.createCell((short) 8).setCellValue(makerId);
             row.createCell((short) 9).setCellValue(checkerId);
             row.createCell((short) 10).setCellValue(postingDate);

             i++;
      }
      }

      OutputStream stream = response.getOutputStream();
//         new MailSender().AttachmentMail("ayomidematanmi4@gmail.com",file);
         workbook.write(stream);
         stream.close();
         System.out.println("Data is saved in excel file.");
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
}