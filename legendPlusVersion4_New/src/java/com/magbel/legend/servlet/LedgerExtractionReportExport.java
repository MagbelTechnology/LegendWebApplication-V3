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
   
public class LedgerExtractionReportExport extends HttpServlet
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
   // String branch_Code = request.getParameter("initiatorSOLID");
    String branch_Id = request.getParameter("branch") == null ? "0" : request.getParameter("branch");
    String report = "rptMenuCLDG";
    String branchIdNo = records.getCodeName("select BRANCH from am_gb_User where USER_ID = ? ",userId);
    //String branchCode = request.getParameter("BRANCH_CODE");
    String userName = records.getCodeName("select USER_NAME from am_gb_User where USER_ID = ? ",userId);
    System.out.println("<<<<<<branch_Id: "+branch_Id);
    String branchCode = "";
    if(!branchIdNo.equals("0")){
    	branchCode = records.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID = ? ",branchIdNo);
    }
    //System.out.println("<<<<<<branch_Code: "+branch_Code);
//    String userName = request.getParameter("userName");
    String fileName = "";

   // if(report.equalsIgnoreCase("rptMenuBidBasePrice")){fileName = branchCode+"By"+userName+"BidBasePriceReportExport.xlsx";}
    
     fileName = userName+"LedgerExtractionReportExport.xlsx";    	
    
     
    String filePath = System.getProperty("user.home")+"\\Downloads";
    System.out.println("<<<<<<filePath: "+filePath);
	File tmpDir = new File(filePath);
	boolean exists = tmpDir.exists();	

	response.setIntHeader("Content-Length", -1);
    response.setContentType("application/octet-stream");
    response.setHeader("Content-Disposition", 
   "attachment; filename="+fileName+"");
    try
    {
    	ad = new AssetRecordsBean();
 
//    if(exists==false){   	

        String categoryCode = request.getParameter("category") == null ? "0" : request.getParameter("category");
     Report rep = new Report();
   System.out.println("<<<<<<branch_Id: "+branch_Id+"    categoryCode: "+categoryCode+"  branchCode: "+branchCode);
     String ColQuery = "";
     
    	 System.out.println("======>>>>>>>No Selection: ");	     
	     ColQuery = "SELECT FINACLE_EXT.\"TYPE\" AS FINACLE_EXT_TYPE, FINACLE_EXT.\"DR_ACCT\" AS FINACLE_EXT_DR_ACCT, FINACLE_EXT.\"CR_ACCT\" AS FINACLE_EXT_CR_ACCT, FINACLE_EXT.\"AMOUNT\" AS FINACLE_EXT_AMOUNT,\n"
	     		+ "FINACLE_EXT.\"NARRATION\" AS FINACLE_EXT_NARRATION, FINACLE_EXT.\"VALUE_DATE\" AS FINACLE_EXT_VALUE_DATE, am_gb_company.\"company_name\" AS am_gb_company_company_name\n"
	     		+ "FROM \"dbo\".\"FINACLE_EXT\" FINACLE_EXT, \"dbo\".\"am_gb_company\" am_gb_company";   
	     
	 
 // System.out.println("======>>>>>>>ColQuery: "+ColQuery);
     java.util.ArrayList list =rep.getLedgerExtracts(ColQuery,branch_Id,categoryCode);
     System.out.println("======>>>>>>>list size: "+list.size()+"        =====report: "+report);
     if(list.size()!=0){
   	 if(report.equalsIgnoreCase("rptMenuCLDG")){
    	 SXSSFWorkbook workbook = new SXSSFWorkbook();
         Sheet sheet = workbook.createSheet("Demo");
         Row rowhead = sheet.createRow((int) 0);

         rowhead.createCell( 0).setCellValue("S/No.");
         rowhead.createCell( 1).setCellValue("FINACLE_EXT_TYPE");
         rowhead.createCell( 2).setCellValue("FINACLE_EXT_DR_ACCT");
         rowhead.createCell( 3).setCellValue("FINACLE_EXT_CR_ACCT");
         rowhead.createCell( 4).setCellValue("FINACLE_EXT_AMOUNT");
         rowhead.createCell( 5).setCellValue("FINACLE_EXT_CR_NARRATION");
         rowhead.createCell( 6).setCellValue("FINACLE_EXT_VALUE_DATE");
         rowhead.createCell( 7).setCellValue("am_gb_company_name");

     int i = 1;
     System.out.println("<<<<<<list.size(): "+list.size());
	 for(int k=0;k<list.size();k++)
     {
    	 com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);   
			
			String type =  newassettrans.getDescription();
			String drAcct = newassettrans.getAccumDepLedger();   
			String crAcct = newassettrans.getCategoryCode();
			double amount = newassettrans.getAmount();
			String narration = newassettrans.getLocation();
			String valueDate = newassettrans.getAssetId();
			String companyName = newassettrans.getNewBranchCode();
			

			Row row = sheet.createRow((int) i);

			row.createCell((short) 0).setCellValue(i);
			row.createCell((short) 1).setCellValue(type);
            row.createCell((short) 2).setCellValue(drAcct);
            row.createCell((short) 3).setCellValue(crAcct);
            row.createCell((short) 4).setCellValue(amount);
            row.createCell((short) 5).setCellValue(narration);
            row.createCell((short) 6).setCellValue(valueDate);
            row.createCell((short) 7).setCellValue(companyName);
		
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