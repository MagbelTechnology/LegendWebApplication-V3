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
   
public class LegacyTransactionImportReport extends HttpServlet
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
    //String branchCode = request.getParameter("BRANCH_CODE");
//    System.out.println("<<<<<<branch_Id: "+branch_Id);
    branch_Code = records.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID = ? ",branch_Code);
    String branchCode = "";
    if(!branch_Id.equals("***")){
    	branchCode = records.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID = ? ",branch_Id);
    }
//    System.out.println("<<<<<<branch_Code: "+branch_Code);
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
     if(!branch_Id.equals("0") && !start_Date.equals("") && !end_Date.equals("")){
//    	 System.out.println("======>>>>>>>Branch Selected: ");
    	  
	     ColQuery ="SELECT "
	     		+ "LEGACY_TRANSACTION.BATCH_NO AS BATCH_NO,"
	     		+ "LEGACY_TRANSACTION.SERIAL_NO AS SERIAL_NO,"
	     		+ "LEGACY_TRANSACTION.BRANCH_CODE AS BRANCH_CODE,"
	     		+ "LEGACY_TRANSACTION.POSTING_DATE AS DATE_FIELD,"
	     		+ "LEGACY_TRANSACTION.ACCOUNT_NO AS ACCOUNT_NO,"
	     		+ "LEGACY_TRANSACTION.TRANSACTION_DESCRIPTION AS DESCRIPTION,"
	     		+ "LEGACY_TRANSACTION.AMOUNT AS cost_price,"
	     		+ "LEGACY_TRANSACTION.TRANSACTION_TYPE AS TRANSTYPE,"
	     		+ "LEGACY_TRANSACTION.MAKER_ID AS MAKER,"
	     		+ "LEGACY_TRANSACTION.CHECKER_ID AS CHECKER,"
	     		+ "LEGACY_TRANSACTION.AMOUNT AS AMOUNT "
	     		+ "FROM "
	     		+ "dbo.LEGACY_TRANSACTION LEGACY_TRANSACTION "
	     		+ "WHERE "
	     		+ "BRANCH_CODE = '"+branchCode+"' "
	     		+ "AND POSTING_DATE BETWEEN '"+startDate+"' AND '"+endDate+"' "
	     		+ "ORDER BY "
	     		+ "LEGACY_TRANSACTION.BATCH_NO ASC,"
	     		+ "LEGACY_TRANSACTION.BRANCH_CODE ASC ";
	}      
	 if(branch_Id.equals("0") && !start_Date.equals("") && !end_Date.equals("")){	   
	   System.out.println("======>>>>>>>Category Selected: ");
	     ColQuery ="SELECT "
		     		+ "LEGACY_TRANSACTION.BATCH_NO AS BATCH_NO,"
		     		+ "LEGACY_TRANSACTION.SERIAL_NO AS SERIAL_NO,"
		     		+ "LEGACY_TRANSACTION.BRANCH_CODE AS BRANCH_CODE,"
		     		+ "LEGACY_TRANSACTION.POSTING_DATE AS DATE_FIELD,"
		     		+ "LEGACY_TRANSACTION.ACCOUNT_NO AS ACCOUNT_NO,"
		     		+ "LEGACY_TRANSACTION.TRANSACTION_DESCRIPTION AS DESCRIPTION,"
		     		+ "LEGACY_TRANSACTION.AMOUNT AS cost_price,"
		     		+ "LEGACY_TRANSACTION.TRANSACTION_TYPE AS TRANSTYPE,"
		     		+ "LEGACY_TRANSACTION.MAKER_ID AS MAKER,"
		     		+ "LEGACY_TRANSACTION.CHECKER_ID AS CHECKER,"
		     		+ "LEGACY_TRANSACTION.AMOUNT AS AMOUNT"
		     		+ "FROM "
		     		+ "dbo.LEGACY_TRANSACTION LEGACY_TRANSACTION "
		     		+ "WHERE "
		     		+ "POSTING_DATE BETWEEN '"+startDate+"' AND '"+endDate+"' "
		     		+ "ORDER BY "
		     		+ "LEGACY_TRANSACTION.BATCH_NO ASC, "
		     		+ "LEGACY_TRANSACTION.BRANCH_CODE ASC ";   
	     }
   if(!branch_Id.equals("0") && start_Date.equals("") && end_Date.equals("")){
	   System.out.println("======>>>>>>>No Selection: ");
	     ColQuery ="SELECT "
		     		+ "LEGACY_TRANSACTION.BATCH_NO AS BATCH_NO,"
		     		+ "LEGACY_TRANSACTION.SERIAL_NO AS SERIAL_NO,"
		     		+ "LEGACY_TRANSACTION.BRANCH_CODE AS BRANCH_CODE,"
		     		+ "LEGACY_TRANSACTION.POSTING_DATE AS DATE_FIELD,"
		     		+ "LEGACY_TRANSACTION.ACCOUNT_NO AS ACCOUNT_NO,"
		     		+ "LEGACY_TRANSACTION.TRANSACTION_DESCRIPTION AS DESCRIPTION,"
		     		+ "LEGACY_TRANSACTION.AMOUNT AS cost_price,"
		     		+ "LEGACY_TRANSACTION.TRANSACTION_TYPE AS TRANSTYPE,"
		     		+ "LEGACY_TRANSACTION.MAKER_ID AS MAKER,"
		     		+ "LEGACY_TRANSACTION.CHECKER_ID AS CHECKER,"
		     		+ "LEGACY_TRANSACTION.AMOUNT AS AMOUNT"
		     		+ "FROM "
		     		+ "dbo.LEGACY_TRANSACTION LEGACY_TRANSACTION  "
		     		+ "WHERE "
		     		+ "BRANCH_CODE = '"+branchCode+"' "
		     		+ "ORDER BY "
		     		+ "LEGACY_TRANSACTION.BATCH_NO ASC,"
		     		+ "LEGACY_TRANSACTION.BRANCH_CODE ASC ";	
	     }   
//   System.out.println("======>>>>>>>ColQuery: "+ColQuery);
     java.util.ArrayList list =rep.getLegacyTransactionImportRecords(ColQuery,branch_Id,startDate,endDate,BankingApp);
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
         rowhead.createCell((short) 5).setCellValue("AMOUNT");
         rowhead.createCell((short) 6).setCellValue("TRANSACTION DESCRIPTION");
         rowhead.createCell((short) 7).setCellValue("MAKER ID");
         rowhead.createCell((short) 8).setCellValue("CHECKER ID");
         rowhead.createCell((short) 9).setCellValue("POSTING DATE");

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
            row.createCell((short) 6).setCellValue(transDescription);
            row.createCell((short) 7).setCellValue(makerId);
            row.createCell((short) 8).setCellValue(checkerId);
            row.createCell((short) 9).setCellValue(postingDate);

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
     rowhead.createCell((short) 6).setCellValue("TRANSACTION DESCRIPTION");
     rowhead.createCell((short) 7).setCellValue("MAKER ID");
     rowhead.createCell((short) 8).setCellValue("CHECKER ID");
     rowhead.createCell((short) 9).setCellValue("POSTING DATE");
     
     int j = 1;
// System.out.println("<<<<<<list.size(): "+list.size());
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
		Row row = sheet.createRow((int) j);

        row.createCell((short) 0).setCellValue(batchNo);
        row.createCell((short) 1).setCellValue(serialNo);
        row.createCell((short) 2).setCellValue(accountNo);
        row.createCell((short) 3).setCellValue(branchCode);
        row.createCell((short) 4).setCellValue(tranType);
        row.createCell((short) 5).setCellValue(amount);
        row.createCell((short) 6).setCellValue(transDescription);
        row.createCell((short) 7).setCellValue(makerId);
        row.createCell((short) 8).setCellValue(checkerId);
        row.createCell((short) 9).setCellValue(postingDate);

        j++;
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