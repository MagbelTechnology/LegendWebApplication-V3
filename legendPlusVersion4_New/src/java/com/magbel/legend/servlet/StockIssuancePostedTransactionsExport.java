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
   
public class StockIssuancePostedTransactionsExport extends HttpServlet
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
   // String region = request.getParameter("region");
   // String dept_Code = request.getParameter("deptCode");
   // String asset_Id = request.getParameter("assetId");
    String report = "rptMenuStockPostedTransact";
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
    if(report.equalsIgnoreCase("rptMenuStockPostedTransact")){fileName = branchCode+"By"+userName+"StockIssuancePostedTransactionsExport.xlsx";}
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

        
     Report rep = new Report();
 //    System.out.println("=====FromDate Length: "+FromDate.length());
 //    System.out.println("=====ToDate Length: "+ToDate.length());
//   System.out.println("<<<<<<bran ch_Id: "+branch_Id+"    categoryCode: "+categoryCode+"  branchCode: "+branchCode);
//   System.out.println("<<<<<<branch_Id: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  branchCode: "+branchCode);
     String ColQuery = "";
    	     ColQuery ="SELECT a.asset_id,c.User_id, c.full_name AS initiatorName, \n"
    	     		+ "CASE WHEN SUBSTRING(creditAccount,9,8) = '16330003' THEN 'Stock Account'\n"
    	     		+ "             ELSE 'Asset Aquisition Account' END AS creditAccountName,\n"
    	     		+ "CASE WHEN SUBSTRING(debitAccount,9,8) = '16330003' THEN 'Stock Account'\n"
    	     		+ "             ELSE 'Asset Aquisition Account' END AS debitAccountName,\n"
    	     		+ "d.full_name AS supervisorName,a.creditAccount,debitAccount,a.Description,a.amount,c.Full_Name,a.transaction_date FROM posted_transaction_view a, am_gb_User c, am_gb_User d\n"
    	     		+ "WHERE a.initiatorId = c.User_id\n"
    	     		+ "AND a.supervisorId = d.User_id\n"
    	     		+ "AND a.Description like '%Stock Issuance%'";
    

  
//   System.out.println("======>>>>>>>ColQuery: "+ColQuery);
     java.util.ArrayList list =rep.getStockIssuancePostedRecords(ColQuery,FromDate,ToDate);
     System.out.println("======>>>>>>>list size: "+list.size()+"        =====report: "+report);
     if(list.size()!=0){
   	 if(report.equalsIgnoreCase("rptMenuStockPostedTransact")){
    	 SXSSFWorkbook workbook = new SXSSFWorkbook();
         Sheet sheet = workbook.createSheet("Demo");
         Row rowhead = sheet.createRow((int) 0);
         
         
         

         rowhead.createCell( 0).setCellValue("S/No.");
         rowhead.createCell( 1).setCellValue("asset_id");
         rowhead.createCell( 2).setCellValue("User_id");
         rowhead.createCell( 3).setCellValue("initiatorName");
         rowhead.createCell( 4).setCellValue("creditAccountName");
         rowhead.createCell( 5).setCellValue("debitAccountName");
         rowhead.createCell( 6).setCellValue("supervisorName");
         rowhead.createCell( 7).setCellValue("creditAccount");
         rowhead.createCell( 8).setCellValue("debitAccount");
         rowhead.createCell( 9).setCellValue("Description");
         rowhead.createCell( 10).setCellValue("amount");
         rowhead.createCell( 11).setCellValue("Full_Name");
		 rowhead.createCell( 12).setCellValue("transaction_date");


     int i = 1;
     System.out.println("<<<<<<list.size(): "+list.size());
	 for(int k=0;k<list.size();k++)
     {
    	 com.magbel.legend.vao.newAssetTransaction  newTransaction = (com.magbel.legend.vao.newAssetTransaction)list.get(k);    	 
			String assetId =  newTransaction.getAssetId();
			String user_ID = newTransaction.getUserID();
			String initiatorName = newTransaction.getInitiatorId();
			String creditAcctName = newTransaction.getCreditAccountName();
			String debitAcctName = newTransaction.getDebitAccountName();
			String supervisor = newTransaction.getSupervisor();
			String creditAcct = newTransaction.getCreditAccount();
			String debitAcct = newTransaction.getDebitAccount();
			String description = newTransaction.getDescription();
			double amount = newTransaction.getAmount();
			String fullName = newTransaction.getAssetUser();
			String tranDate = newTransaction.getTransDate();

			
			
			
			//			String vendorName = records.getCodeName("select VENDOR_NAME from am_ad_vendor where VENDOR_ID = "+vendorId+"")

			Row row = sheet.createRow((int) i);

			 
			row.createCell((short) 0).setCellValue(i);
			row.createCell((short) 1).setCellValue(assetId);
            row.createCell((short) 2).setCellValue(user_ID);
            row.createCell((short) 3).setCellValue(initiatorName);
            row.createCell((short) 4).setCellValue(creditAcctName);
            row.createCell((short) 5).setCellValue(debitAcctName);
            row.createCell((short) 6).setCellValue(supervisor);
            row.createCell((short) 7).setCellValue(creditAcct);
            row.createCell((short) 8).setCellValue(debitAcct);
            row.createCell((short) 9).setCellValue(description);
            row.createCell((short) 10).setCellValue(amount);
            row.createCell((short) 11).setCellValue(fullName);
            row.createCell((short) 12).setCellValue(tranDate);

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