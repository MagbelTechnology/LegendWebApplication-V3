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
   
public class FixedTagUpdateReportExport extends HttpServlet
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
    String branch_Id = request.getParameter("branch") == null ? "0" : request.getParameter("branch");
    String categoryCode = request.getParameter("category") == null ? "0" : request.getParameter("category");
   
  

    String report = "rptFixedTagUpdate";
    String branchIdNo = records.getCodeName("select BRANCH from am_gb_User where USER_ID = ? ",userId);
    //String branchCode = request.getParameter("BRANCH_CODE");
    String userName = records.getCodeName("select USER_NAME from am_gb_User where USER_ID = ? ",userId);
    String branchCode = "";
    if(!branchIdNo.equals("0")){
    	branchCode = records.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID = ? ",branchIdNo);
    }

    String fileName = "";
    if(report.equalsIgnoreCase("rptFixedTagUpdate")){fileName = branchCode+"By"+userName+"FixedTagUpdateReportExport.xlsx";}
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

     String ColQuery = "";
    	     ColQuery ="select ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS SNo, a.ASSET_ID,DESCRIPTION,'' AS NEW_TAG,'' AS COMPONENT_TAG,a.BRANCH_CODE,BAR_CODE AS OLD_TAG,a.CATEGORY_CODE,b.BRANCH_NAME,c.CATEGORY_NAME from AM_ASSET a, am_ad_branch b, am_ad_category c\n"
    	     		+ "WHERE a.Branch_ID = b.BRANCH_ID\n"
    	     		+ "AND a.category_ID = c.category_ID\n";
    

   
//   System.out.println("======>>>>>>>ColQuery: "+ColQuery);
     java.util.ArrayList list =rep.getFixedTagUpdateReportRecords(ColQuery,branch_Id,categoryCode);
     System.out.println("======>>>>>>>list size: "+list.size()+"        =====report: "+report);
     if(list.size()!=0){
   	 if(report.equalsIgnoreCase("rptFixedTagUpdate")){
    	 SXSSFWorkbook workbook = new SXSSFWorkbook();
         Sheet sheet = workbook.createSheet("Demo");
         Row rowhead = sheet.createRow((int) 0);
         
         rowhead.createCell(0).setCellValue("S/No.");
         rowhead.createCell(1).setCellValue("ASSET_ID");
         rowhead.createCell(2).setCellValue("DESCRIPTION");
         rowhead.createCell(3).setCellValue("NEW_TAG");
         rowhead.createCell(4).setCellValue("COMPONENT_TAG");
         rowhead.createCell(5).setCellValue("BRANCH_CODE");
         rowhead.createCell(6).setCellValue("OLD_TAG");
         rowhead.createCell(7).setCellValue("CATEGORY_CODE");
         rowhead.createCell(8).setCellValue("BRANCH_NAME");
         rowhead.createCell(9).setCellValue("CATEGORY_NAME");
         
		 

     int i = 1;
     System.out.println("<<<<<<list.size(): "+list.size());
	 for(int k=0;k<list.size();k++)
     {
    	 com.magbel.legend.vao.newAssetTransaction  newTransaction = (com.magbel.legend.vao.newAssetTransaction)list.get(k);    	 
			
    	    String assetId =  newTransaction.getAssetId();
    	    String description = newTransaction.getDescription();
    	    String new_Tag = newTransaction.getBarCode();
    	    String componentTag = newTransaction.getAssetEngineNo();
    	    String branch_Code = newTransaction.getBranchCode();
    	    String oldTag = newTransaction.getAssetModel();
    	    String category_Code = newTransaction.getCategoryCode();
    	    String branchName = newTransaction.getBranchName();
    	    String categoryName = newTransaction.getCategoryName();
    	 
			
			
			Row row = sheet.createRow((int) i);

			 
			row.createCell((short) 0).setCellValue(i);
			row.createCell((short) 1).setCellValue(assetId);
			row.createCell((short) 2).setCellValue(description);
			row.createCell((short) 3).setCellValue(new_Tag);
			row.createCell((short) 4).setCellValue(componentTag);
			row.createCell((short) 5).setCellValue(branch_Code);
			row.createCell((short) 6).setCellValue(oldTag);
			row.createCell((short) 7).setCellValue(category_Code);
			row.createCell((short) 8).setCellValue(branchName);
			row.createCell((short) 9).setCellValue(categoryName);
  

            i++;

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
   
  
}