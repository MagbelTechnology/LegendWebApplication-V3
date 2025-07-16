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
   
public class StockOnHandReportExport extends HttpServlet
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

   // String asset_Id = request.getParameter("assetId");
    String report = "rptSTOCKATHAND";
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
    if(report.equalsIgnoreCase("rptSTOCKATHAND")){fileName = branchCode+"By"+userName+"StockOnHandReportExport.xlsx";}
//    System.out.println("<<<<<<fileName: "+fileName);
    String filePath = System.getProperty("user.home")+"\\Downloads";
//    System.out.println("<<<<<<filePath: "+filePath);
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

        
     Report rep = new Report();
 //    System.out.println("=====FromDate Length: "+FromDate.length());
 //    System.out.println("=====ToDate Length: "+ToDate.length());
//   System.out.println("<<<<<<bran ch_Id: "+branch_Id+"    categoryCode: "+categoryCode+"  branchCode: "+branchCode);
//   System.out.println("<<<<<<branch_Id: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  branchCode: "+branchCode);
     String ColQuery = "";
    	     ColQuery ="select b.BRANCH_NAME,c.category_name,DESCRIPTION,a.QUANTITY AS QTY from ST_STOCK a, am_ad_branch b, am_ad_category c\n"
    	     		+ "where Asset_Status = 'ACTIVE'\n"
    	     		+ "AND a.BRANCH_ID = b.BRANCH_ID\n"
    	     		+ "AND a.CATEGORY_ID = c.CATEGORY_ID";
    

  
//   System.out.println("======>>>>>>>ColQuery: "+ColQuery);
     java.util.ArrayList list =rep.getStockAtHandRecords(ColQuery);
     System.out.println("======>>>>>>>list size: "+list.size()+"        =====report: "+report);
     if(list.size()!=0){
   	 if(report.equalsIgnoreCase("rptSTOCKATHAND")){
    	 SXSSFWorkbook workbook = new SXSSFWorkbook();
         Sheet sheet = workbook.createSheet("Demo");
         Row rowhead = sheet.createRow((int) 0);
         
         
         
         rowhead.createCell( 0).setCellValue("S/No.");
         rowhead.createCell( 1).setCellValue("BRANCH_NAME");
         rowhead.createCell( 2).setCellValue("category_name");
         rowhead.createCell( 3).setCellValue("DESCRIPTION");
         rowhead.createCell( 4).setCellValue("DESCRIPTION");
         rowhead.createCell( 5).setCellValue("QTY");

		 

     int i = 1;
     System.out.println("<<<<<<list.size(): "+list.size());
	 for(int k=0;k<list.size();k++)
     {
    	 com.magbel.legend.vao.newAssetTransaction  newTransaction = (com.magbel.legend.vao.newAssetTransaction)list.get(k);    	 
			String branch_Name =  newTransaction.getBranchName();
			String category_Name = newTransaction.getCategoryName();
			String description = newTransaction.getDescription();
			int quantity = newTransaction.getNoofitems();
	
			

			Row row = sheet.createRow((int) i);

			 
			row.createCell((short) 0).setCellValue(i);
			row.createCell((short) 1).setCellValue(branch_Name);
            row.createCell((short) 2).setCellValue(category_Name);
            row.createCell((short) 3).setCellValue(description);
            row.createCell((short) 4).setCellValue(quantity);

		
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