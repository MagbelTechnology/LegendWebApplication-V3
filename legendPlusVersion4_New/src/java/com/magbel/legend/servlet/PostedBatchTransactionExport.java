package com.magbel.legend.servlet;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

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
   
public class PostedBatchTransactionExport extends HttpServlet
{
	private EmailSmsServiceBus mail ;
	private AssetRecordsBean ad;
	private ApprovalRecords records;
//   public void doPost(HttpServletRequest request, 
//    HttpServletResponse response)
//      throws ServletException, IOException
//   {
//	   
////	   PrintWriter out = response.getWriter();
////    OutputStream out = null;
//		mail= new EmailSmsServiceBus();
//        records = new ApprovalRecords();
//        String userName = request.getParameter("user");
//        String start_Date = request.getParameter("FromDate");
//        String end_Date = request.getParameter("ToDate");
//       // end_Date = end_Date+" 23:59:59:000";
//        String endDate = "";
//        String startDate = "";
////        System.out.println("<<<<<<startDate: "+start_Date+"   end_Date: "+end_Date);
//        if(!start_Date.equals("")){
//        String dd = start_Date.substring(0,2);
//        String mm = start_Date.substring(3,5);
//        String yyyy = start_Date.substring(6,10);
//        startDate = yyyy+"-"+mm+"-"+dd;
//        }
//        if(!end_Date.equals("")){
//        String enddd = end_Date.substring(0,2);
//        String endmm = end_Date.substring(3,5);
//        String endyyyy = end_Date.substring(6,10);
//        endDate = endyyyy+"-"+endmm+"-"+enddd;
//       // endDate = endDate+" 23:59:59:000";
//        }
//        System.out.println("<<<<<<startDate: "+startDate+"    endDate: "+endDate);
////        System.out.println("<<<<<<userName: "+userName);
//        String status = "COST PRICE";
////        String userName = request.getParameter("userName");
//        String fileName = "PostedTransactionBy"+userName+".xls";
//        String filePath = System.getProperty("user.home")+"\\Downloads";
//        File tmpDir = new File(filePath);
//        boolean exists = tmpDir.exists();
//
//        response.setContentType("application/vnd.ms-excel");
//        response.setHeader("Content-Disposition",  
//                "attachment; filename="+fileName+"");
//        try
//        {
//            ad = new AssetRecordsBean();
//            Report rep = new Report();
////            String b = "update a set a.BRANCH_CODE = b.BRANCH_CODE from AM_GB_BATCH_POSTING a, am_asset b where PARSENAME(REPLACE(a.description, '**', '.'), 3) = b.Asset_id";
////            ad.updateAssetStatusChange(b);   
////            String b1 = "update AM_GB_BATCH_POSTING set BRANCH_CODE = PARSENAME(REPLACE(description, '**', '.'), 3) where LEN(PARSENAME(REPLACE(description, '**', '.'), 3)) = 3";
////            ad.updateAssetStatusChange(b1);            
////   System.out.println("<<<<<<branch_Id: "+branch_Id+"    categoryId: "+categoryId+"  branchCode: "+branchCode);
//            String ColQuery = "";
//            if(!userName.equals("***")){
//            ColQuery ="SELECT c.full_name AS initiatorName, d.full_name AS supervisorName,b.BRANCH_NAME, PARSENAME(REPLACE(description, '**', '.'), 3) AS Asset_Id,COST_PRICE AS AMOUNT,DATE_FIELD AS TRANSACTION_DATE, *FROM AM_GB_BATCH_POSTING a, am_gb_User c, am_gb_User d,am_ad_branch b " +
//            		 "WHERE a.User_id = c.User_id AND a.User_id = d.User_id AND a.BRANCH_CODE = b.BRANCH_CODE" +
//            		 "AND a.user_Id = ? ORDER BY BATCH_NO,GROUP_ID,PARSENAME(REPLACE(description, '**', '.'), 3) ASC ";
//            }
//            if(userName.equals("***") && start_Date.equals("") && end_Date.equals("")){
//                ColQuery ="SELECT c.full_name AS initiatorName, d.full_name AS supervisorName,b.BRANCH_NAME, PARSENAME(REPLACE(description, '**', '.'), 3) AS Asset_Id,COST_PRICE AS AMOUNT,DATE_FIELD AS TRANSACTION_DATE, *FROM AM_GB_BATCH_POSTING a, am_gb_User c, am_gb_User d,am_ad_branch b " +
//               		 "WHERE a.User_id = c.User_id AND a.User_id = d.User_id AND a.BRANCH_CODE = b.BRANCH_CODE ORDER BY BATCH_NO,GROUP_ID,PARSENAME(REPLACE(description, '**', '.'), 3) ASC ";
//            }
//            if(!start_Date.equals("") && !end_Date.equals("")) {
//            	ColQuery = "SELECT  c.full_name AS initiatorName, d.full_name AS supervisorName,b.BRANCH_NAME, PARSENAME(REPLACE(description, '**', '.'), 3) AS Asset_Id,COST_PRICE AS AMOUNT,DATE_FIELD AS TRANSACTION_DATE, *FROM AM_GB_BATCH_POSTING a, am_gb_User c, am_gb_User d,am_ad_branch b \n"
//            			+ "WHERE a.User_id = c.User_id AND a.User_id = d.User_id AND a.BRANCH_CODE = b.BRANCH_CODE\n"
//            			+ "AND (SUBSTRING(DATE_FIELD,7,4)+'-'+SUBSTRING(DATE_FIELD,4,2)+'-'+SUBSTRING(DATE_FIELD,0,3))\n"
//            			+ "BETWEEN '"+startDate+"' AND '"+endDate+"' ORDER BY BATCH_NO,GROUP_ID,PARSENAME(REPLACE(description, '**', '.'), 3) ASC ";
////            	ColQuery = "SELECT  * FROM AM_GB_BATCH_POSTING WHERE  (SUBSTRING(DATE_FIELD,7,4)+'-'+SUBSTRING(DATE_FIELD,4,2)+'-'+SUBSTRING(DATE_FIELD,0,3)) BETWEEN '"+startDate+"' AND '"+endDate+"'";
//            }
//            
////            System.out.println("======>>>>>>>ColQuery: "+ColQuery);
//            java.util.ArrayList list =rep.getPostedBatchTransactionExportRecords(ColQuery,userName,startDate,endDate);
//            if(list.size()!=0){
//                HSSFWorkbook workbook = new HSSFWorkbook();
//                HSSFSheet sheet = workbook.createSheet("Posted Transaction Export");
//                HSSFRow rowhead = sheet.createRow((int) 0);
//
//                rowhead.createCell((int) 0).setCellValue("Group Id");
//                rowhead.createCell((int) 1).setCellValue("Batch No");
//                rowhead.createCell((int) 2).setCellValue("Branch Code");
//                rowhead.createCell((int) 3).setCellValue("Branch Name");
//                rowhead.createCell((int) 4).setCellValue("Asset Id");
//                rowhead.createCell((int) 5).setCellValue("Transaction Description");
//                rowhead.createCell((int) 6).setCellValue("Account");
//                rowhead.createCell((int) 7).setCellValue("Account Name");
//                rowhead.createCell((int) 8).setCellValue("Transaction Type");
//                rowhead.createCell((int) 9).setCellValue("Amount)");
//                rowhead.createCell((int) 10).setCellValue("Posted By");
//                rowhead.createCell((int) 11).setCellValue("Transaction Date");
//                int i = 1;
//
////     System.out.println("<<<<<<list.size(): "+list.size());
//                for(int k=0;k<list.size();k++)
//                {
//                    com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);
//                    String assetId =  newassettrans.getAssetId();
//                    String groupId = newassettrans.getOldassetId();
//                    String batchNo = newassettrans.getBarCode();
//                    String branchCode =  newassettrans.getBranchCode();
//                    String branchName =  newassettrans.getBranchName();
//                    String Description = newassettrans.getDescription();
//                    String accountNo = newassettrans.getDebitAccount();
//                    String transType = newassettrans.getTranType();
//                    String transactionDate = newassettrans.getTransDate();
//                    String action = newassettrans.getAction();
////			String branchName = records.getCodeName("select BRANCH_NAME from am_ad_branch where BRANCH_ID = "+branchId+"");
//                    double amount = newassettrans.getAmount();
//                    String AccountName = records.getCodeName("select VENDOR_NAME from am_ad_vendor where VENDOR_CODE = '"+branchCode+"' and account_number = '"+accountNo+"' ");
//                    if(AccountName==""){
//                    if(AccountName==""){AccountName = records.getCodeName("select VENDOR_NAME from am_ad_vendor where VENDOR_CODE = 'D1' and account_number = '"+accountNo+"' ");}
//                    if(AccountName==""){AccountName = records.getCodeName("select VENDOR_NAME from am_ad_vendor where VENDOR_CODE = '00001' and account_number = '"+accountNo+"' ");}
//                    if(AccountName==""){AccountName = records.getCodeName("select category_name from am_ad_category where Asset_Ledger = ? ",accountNo);}
//                    if(AccountName==""){AccountName = records.getCodeName("select category_name from am_ad_category where Dep_ledger = ? ",accountNo);}
//                    if(AccountName==""){AccountName = records.getCodeName("select category_name from am_ad_category where Accum_Dep_ledger = ? ",accountNo);}
//                    AccountName = AccountName+" - "+branchName;
//                    }
//                    String postedBy = newassettrans.getPostedBy();;
//                    String initiatorId = newassettrans.getInitiatorId();
//                    String supervisorId = newassettrans.getSupervisorId();
//
//
//                    HSSFRow row = sheet.createRow((int) i);
//
//                    row.createCell((int) 0).setCellValue(groupId);
//                    row.createCell((int) 1).setCellValue(batchNo);
//                    row.createCell((int) 2).setCellValue(branchCode);
//                    row.createCell((int) 3).setCellValue(branchName);
//                    row.createCell((int) 4).setCellValue(assetId);
//                    row.createCell((int) 5).setCellValue(Description);
//                    row.createCell((int) 6).setCellValue(accountNo);
//                    row.createCell((int) 7).setCellValue(AccountName);
//                    row.createCell((int) 8).setCellValue(transType);
//                    row.createCell((int) 9).setCellValue(amount);
//                    row.createCell((int) 10).setCellValue(postedBy);
//                    row.createCell((int) 11).setCellValue(transactionDate);
//                    
//                    i++;
//                }
//                OutputStream stream = response.getOutputStream();
////              new MailSender().AttachmentMail("ayomidematanmi4@gmail.com",file);
//              workbook.write(stream);
//              stream.close();
//              System.out.println("Data is saved in excel file.");
//
//            }
//        } catch (Exception e)
//        {
//            throw new ServletException("Exception in Excel Sample Servlet", e);
//        } finally
//        {
////     if (out != null)
////      out.close();
//        }
//   }
	
	public void doPost(HttpServletRequest request,
            HttpServletResponse response)
 throws ServletException, IOException {

OutputStream stream = null;

try {
 mail = new EmailSmsServiceBus();
 records = new ApprovalRecords();
 ad = new AssetRecordsBean();
 Report rep = new Report();

 String userName = request.getParameter("user");
 String start_Date = request.getParameter("FromDate");
 String end_Date = request.getParameter("ToDate");

 String startDate = convertToSqlDate(start_Date);
 String endDate = convertToSqlDate(end_Date);

 System.out.println("<<<<<<startDate: " + startDate + "    endDate: " + endDate);

 // =========================
 // RESPONSE SETUP
 // =========================
 String fileName = "PostedTransactionBy" + userName + ".xls";

 response.setContentType("application/vnd.ms-excel");
 response.setHeader("Content-Disposition",
         "attachment; filename=" + fileName);

 // =========================
 // BUILD QUERY (CLEAN)
 // =========================
 StringBuilder query = new StringBuilder();

 query.append("SELECT c.full_name AS initiatorName, ")
      .append("d.full_name AS supervisorName, ")
      .append("b.BRANCH_NAME, ")
      .append("PARSENAME(REPLACE(description, '**', '.'), 3) AS Asset_Id, ")
      .append("COST_PRICE AS AMOUNT, ")
      .append("DATE_FIELD AS TRANSACTION_DATE, * ")
      .append("FROM AM_GB_BATCH_POSTING a ")
      .append("JOIN am_gb_User c ON a.User_id = c.User_id ")
      .append("JOIN am_gb_User d ON a.User_id = d.User_id ")
      .append("JOIN am_ad_branch b ON a.BRANCH_CODE = b.BRANCH_CODE ")
      .append("WHERE 1=1 ");

 // User filter
 if (!"***".equals(userName)) {
     query.append("AND a.user_Id = ? ");
 }

 // Date filter
 if (isNotEmpty(startDate) && isNotEmpty(endDate)) {
     query.append("AND (SUBSTRING(DATE_FIELD,7,4)+'-'+SUBSTRING(DATE_FIELD,4,2)+'-'+SUBSTRING(DATE_FIELD,0,3)) ")
          .append("BETWEEN ? AND ? ");
 }

 query.append("ORDER BY BATCH_NO, GROUP_ID, PARSENAME(REPLACE(description, '**', '.'), 3) ASC");

 System.out.println("======>>>>>>>ColQuery: " + query);

 // =========================
 // FETCH DATA
 // =========================
 ArrayList list = rep.getPostedBatchTransactionExportRecords(
         query.toString(), userName, startDate, endDate
 );

 if (list == null || list.isEmpty()) {
     return;
 }

 // =========================
 // CREATE EXCEL
 // =========================
 SXSSFWorkbook workbook = new SXSSFWorkbook();
 Sheet sheet = workbook.createSheet("Posted Transaction Export");

 // =========================
 // HEADER
 // =========================
 Row header = sheet.createRow(0);

 header.createCell(0).setCellValue("Group Id");
 header.createCell(1).setCellValue("Batch No");
 header.createCell(2).setCellValue("Branch Code");
 header.createCell(3).setCellValue("Branch Name");
 header.createCell(4).setCellValue("Asset Id");
 header.createCell(5).setCellValue("Transaction Description");
 header.createCell(6).setCellValue("Account");
 header.createCell(7).setCellValue("Account Name");
 header.createCell(8).setCellValue("Transaction Type");
 header.createCell(9).setCellValue("Amount");
 header.createCell(10).setCellValue("Posted By");
 header.createCell(11).setCellValue("Transaction Date");

 // =========================
 // POPULATE DATA
 // =========================
 int rowIndex = 1;

 for (Object obj : list) {

     com.magbel.legend.vao.newAssetTransaction trans =
             (com.magbel.legend.vao.newAssetTransaction) obj;

     String accountName = resolveAccountName(
             trans.getDebitAccount(),
             trans.getBranchCode(),
             trans.getBranchName()
     );

     Row row = sheet.createRow(rowIndex++);

     row.createCell(0).setCellValue(trans.getOldassetId());
     row.createCell(1).setCellValue(trans.getBarCode());
     row.createCell(2).setCellValue(trans.getBranchCode());
     row.createCell(3).setCellValue(trans.getBranchName());
     row.createCell(4).setCellValue(trans.getAssetId());
     row.createCell(5).setCellValue(trans.getDescription());
     row.createCell(6).setCellValue(trans.getDebitAccount());
     row.createCell(7).setCellValue(accountName);
     row.createCell(8).setCellValue(trans.getTranType());
     row.createCell(9).setCellValue(trans.getAmount());
     row.createCell(10).setCellValue(trans.getPostedBy());
     row.createCell(11).setCellValue(trans.getTransDate());
 }
 // =========================
 // WRITE FILE
 // =========================
 stream = response.getOutputStream();
 workbook.write(stream);

 System.out.println("Data is saved in excel file.");

} catch (Exception e) {
 throw new ServletException("Exception in Excel Sample Servlet", e);
} finally {
 if (stream != null) {
     stream.close();
 }
}
}
   public void doGet(HttpServletRequest request, 
		    HttpServletResponse response)
		      throws ServletException, IOException
		   {
	   doPost(request, response);
		   }
   
   private String convertToSqlDate(String input) {
	    if (input == null || input.trim().isEmpty()) {
	        return "";
	    }

	    try {
	        input = input.trim();

	        // Case 1: Already in yyyy-MM-dd → return as is
	        if (input.matches("\\d{4}-\\d{2}-\\d{2}")) {
	            return input;
	        }

	        // Case 2: dd-MM-yyyy
	        if (input.matches("\\d{2}-\\d{2}-\\d{4}")) {
	            String dd = input.substring(0, 2);
	            String mm = input.substring(3, 5);
	            String yyyy = input.substring(6, 10);
	            return yyyy + "-" + mm + "-" + dd;
	        }

	        // Case 3: dd/MM/yyyy
	        if (input.matches("\\d{2}/\\d{2}/\\d{4}")) {
	            String[] parts = input.split("/");
	            return parts[2] + "-" + parts[1] + "-" + parts[0];
	        }

	        // Case 4: fallback (invalid format)
	        throw new IllegalArgumentException("Unsupported date format: " + input);

	    } catch (Exception e) {
	        System.out.println("Error converting date: " + input);
	        return "";
	    }
	}
   
   private String resolveAccountName(String accountNo, String branchCode, String branchName) {

	    String accountName = null;

	    // 1. Try primary vendor lookup
	    accountName = records.getCodeName(
	        "SELECT VENDOR_NAME FROM am_ad_vendor WHERE VENDOR_CODE = ? AND account_number = ?",
	        branchCode, accountNo
	    );

	    // 2. Fallback vendor codes
	    if (isEmpty(accountName)) {
	        accountName = records.getCodeName(
	            "SELECT VENDOR_NAME FROM am_ad_vendor WHERE VENDOR_CODE = 'D1' AND account_number = ?",
	            accountNo
	        );
	    }

	    if (isEmpty(accountName)) {
	        accountName = records.getCodeName(
	            "SELECT VENDOR_NAME FROM am_ad_vendor WHERE VENDOR_CODE = '00001' AND account_number = ?",
	            accountNo
	        );
	    }

	    // 3. Fallback to category tables
	    if (isEmpty(accountName)) {
	        accountName = records.getCodeName(
	            "SELECT category_name FROM am_ad_category WHERE Asset_Ledger = ?",
	            accountNo
	        );
	    }

	    if (isEmpty(accountName)) {
	        accountName = records.getCodeName(
	            "SELECT category_name FROM am_ad_category WHERE Dep_ledger = ?",
	            accountNo
	        );
	    }

	    if (isEmpty(accountName)) {
	        accountName = records.getCodeName(
	            "SELECT category_name FROM am_ad_category WHERE Accum_Dep_ledger = ?",
	            accountNo
	        );
	    }

	    // 4. Final fallback (never return null)
	    if (isEmpty(accountName)) {
	        accountName = "UNKNOWN ACCOUNT";
	    }

	    return accountName + " - " + branchName;
	}
   
   private boolean isEmpty(String val) {
	    return val == null || val.trim().isEmpty();
	}

	private boolean isNotEmpty(String val) {
	    return !isEmpty(val);
	}
}

