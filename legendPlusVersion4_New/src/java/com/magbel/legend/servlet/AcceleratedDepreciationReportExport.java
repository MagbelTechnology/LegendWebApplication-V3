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
   
public class AcceleratedDepreciationReportExport extends HttpServlet
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
//    if(!branch_Id.equals("0")){
//    	//branch_Code = records.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID = ? ",branch_Id);
//    }
 //   String categoryCode = request.getParameter("category") == null ? "0" : request.getParameter("category");
//    String catCode = "";
//    if(!categoryCode.equals("0")){
//    	//catCode = records.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID = ? ",categoryCode);
//    }
//    String asset_Id = request.getParameter("assetId");
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
   

    //String report = "rptFixedTagUpdate";
    String branchIdNo = records.getCodeName("select BRANCH from am_gb_User where USER_ID = ? ",userId);
    //String branchCode = request.getParameter("BRANCH_CODE");
    String userName = records.getCodeName("select USER_NAME from am_gb_User where USER_ID = ? ",userId);
    String branchCode = "";
    if(!branchIdNo.equals("0")){
    	branchCode = records.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID = ? ",branchIdNo);
    }

    String fileName = "";
    //if(report.equalsIgnoreCase("rptFixedTagUpdate")){
    fileName = "AcceleratedDepreciationReport.xlsx";
    	//}
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
    	     ColQuery ="select * from am_Asset a,am_ad_category b,am_gb_company c,am_ad_category_class d,am_asset_approval e,am_AcceleratedDepreciation t\n"
    	     		+ "	    where a.category_code = b.category_code\n"
    	     		+ "	    and b.category_class = d.class_id\n"
    	     		+ "		and a.asset_id = e.asset_id\n"
    	     		+ "	    and a.Asset_Status = 'Active'\n"
    	     		+ "		and e.process_status = 'A'\n"
    	     		+ "		and a.asset_status = 'ACTIVE'\n"
    	     		+ "		and a.asset_id = t.asset_id\n"
    	     		+ "		and e.tran_type = 'Accelerated Depreciation'\n"
    	     		+ "		AND b.CATEGORY_TYPE = 'C' AND SUBSTRING(b.CATEGORY_ACRONYM,1,1) != 'F'";
    

  
//   System.out.println("======>>>>>>>ColQuery: "+ColQuery);
     java.util.ArrayList list =rep.getAcceleratedDepreciationRecords(ColQuery,branch_Id,FromDate,ToDate);
     //System.out.println("======>>>>>>>list size: "+list.size()+"        =====report: "+report);
     if(list.size()!=0){
   	 //if(report.equalsIgnoreCase("rptFixedTagUpdate")){
    	 SXSSFWorkbook workbook = new SXSSFWorkbook();
         Sheet sheet = workbook.createSheet("Demo");
         Row rowhead = sheet.createRow((int) 0);
         
         rowhead.createCell(0).setCellValue("S/No.");
         rowhead.createCell(1).setCellValue("Acclerated ID");
         rowhead.createCell(2).setCellValue("TransactionID Code");
         rowhead.createCell(3).setCellValue("Asset Id");
         rowhead.createCell(4).setCellValue("Accelerated Reason");
         rowhead.createCell(5).setCellValue("Debit Account");
         rowhead.createCell(6).setCellValue("Credit Account");
         rowhead.createCell(7).setCellValue("Accelerated Months");
         rowhead.createCell(8).setCellValue("Useful Life");
         rowhead.createCell(9).setCellValue("Remaining Life");
         rowhead.createCell(10).setCellValue("Accelerated Date");
         rowhead.createCell(11).setCellValue("Accum Dep");
         rowhead.createCell(12).setCellValue("Monthly Dep");
         rowhead.createCell(13).setCellValue("Accelerated Amount");
         rowhead.createCell(14).setCellValue("Status");


         

		 

     int i = 1;
    // System.out.println("<<<<<<list.size(): "+list.size());
	 for(int k=0;k<list.size();k++)
     {
    	 com.magbel.legend.vao.newAssetTransaction  newTransaction = (com.magbel.legend.vao.newAssetTransaction)list.get(k);    	 
			
    	   
  
    	    
    	   String accleratedID = newTransaction.getOldassetId();
    	   String transactionID = newTransaction.getAssetId();
    	   String assetId =  newTransaction.getNewAssetId();
    	   String acceleratedReason = newTransaction.getPurchaseReason();
    	   String drAcct = newTransaction.getDebitAccount();
    	   String crAcct = newTransaction.getCreditAccount();
    	   int accMonths = newTransaction.getNoofitems();
    	   int usefullife = newTransaction.getRemainLife();
    	   int remainLife = newTransaction.getImproveRemainLife();
    	   String accDate = newTransaction.getDependDate();
    	   double accumDep = newTransaction.getAccumDep();
    	   double monthlyDep = newTransaction.getMonthlyDep();
    	   double accAmount = newTransaction.getAmount();
    	   String status = newTransaction.getAssetStatus();

    	   
    	
    	   
    	   

			
			
			Row row = sheet.createRow((int) i);

			 
			row.createCell((short) 0).setCellValue(i);
			row.createCell((short) 1).setCellValue(accleratedID);
			row.createCell((short) 2).setCellValue(transactionID);
			row.createCell((short) 3).setCellValue(assetId);
			row.createCell((short) 4).setCellValue(acceleratedReason);
			row.createCell((short) 5).setCellValue(drAcct);
			row.createCell((short) 6).setCellValue(crAcct);
			row.createCell((short) 7).setCellValue(accMonths);
			row.createCell((short) 8).setCellValue(usefullife);
			row.createCell((short) 9).setCellValue(remainLife);
			row.createCell((short) 10).setCellValue(accDate);
			row.createCell((short) 11).setCellValue(accumDep);
			row.createCell((short) 12).setCellValue(monthlyDep);
			row.createCell((short) 13).setCellValue(accAmount);
			row.createCell((short) 14).setCellValue(status);


  

            i++;

     }
	// System.out.println("we are here 2");
	   OutputStream stream = response.getOutputStream();
         workbook.write(stream);
         stream.close();
         workbook.close();  
         System.out.println("Data is saved in excel file.");
         
    /* w.write();
     w.close(); */
     //}
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