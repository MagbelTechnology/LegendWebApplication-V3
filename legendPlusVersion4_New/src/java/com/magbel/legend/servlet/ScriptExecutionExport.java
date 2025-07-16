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

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;

import jxl.Workbook; 
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
   
public class ScriptExecutionExport extends HttpServlet
{
	private EmailSmsServiceBus mail ;
	private AssetRecordsBean ad;
	private ApprovalRecords records;
   public void doPost(HttpServletRequest request, 
    HttpServletResponse response)
      throws ServletException, IOException
     
   {
	 String userClass = (String) request.getSession().getAttribute("UserClass");
	 
	 if (!userClass.equals("NULL") || userClass!=null){
//	   PrintWriter out = response.getWriter();
//    OutputStream out = null; 
	mail= new EmailSmsServiceBus();
	records = new ApprovalRecords();
    String branch_Code = request.getParameter("initiatorSOLID");
    String branch_Id = request.getParameter("branch");
    //String branchCode = request.getParameter("BRANCH_CODE");
    System.out.println("<<<<<<branch_Id: "+branch_Id);
    String branchCode = "";
    if(!branch_Id.equals("0")){
    	branchCode = records.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID = ? ",branch_Id);
    }
    System.out.println("<<<<<<branch_Code: "+branch_Code);
    String userName = request.getParameter("userName");
    String fileName = branch_Code+"By"+userName+"AssetManagementReport.xlsx";    	
    String filePath = System.getProperty("user.home")+"\\Downloads";
	File tmpDir = new File(filePath);
	boolean exists = tmpDir.exists();	

    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", 
   "attachment; filename="+fileName+"");
    try
    {
    	ad = new AssetRecordsBean();
 
//    if(exists==false){   	
    	String fromDate = request.getParameter("fromDate");
        String toDate = request.getParameter("toDate");
     Report rep = new Report();
   System.out.println("<<<<<<fromDate: "+fromDate+"    toDate: "+toDate);
     String ColQuery = "";
     if(!fromDate.equals("0")  && !toDate.equals("0")){
    	 System.out.println("======>>>>>>>Date Selected: ");
	     ColQuery ="select *from am_gb_script where create_date between ? and ?  ";
	}      
	 if(fromDate.equals("0")  && !toDate.equals("0")){	   
	   System.out.println("======>>>>>>>Date Not Selected: ");
     ColQuery ="select *from am_gb_script ";
   }

//   System.out.println("======>>>>>>>ColQuery: "+ColQuery);
     java.util.ArrayList list =rep.getScriptExecutionRecords(ColQuery,branch_Id,branch_Code,fromDate,toDate);
     if(list.size()!=0){
	 
    	 SXSSFWorkbook workbook = new SXSSFWorkbook();
         Sheet sheet = workbook.createSheet("Demo");
         Row rowhead = sheet.createRow((int) 0);
         
         rowhead.createCell((short) 0).setCellValue("S/No");
         rowhead.createCell((short) 1).setCellValue("SCRIPT EXECUTED ");
         rowhead.createCell((short) 2).setCellValue("REASONS FOR EXECUTION");
         rowhead.createCell((short) 3).setCellValue("EXECUTED BY");
         rowhead.createCell((short) 4).setCellValue("EXECUTION DATE");
         rowhead.createCell((short) 5).setCellValue("APPROVED BY");
		 rowhead.createCell((short) 6).setCellValue("APPROVAL DATE");
		 rowhead.createCell((short) 7).setCellValue("STATUS");
		 rowhead.createCell((short) 8).setCellValue("CONFIRMED");

     int i = 1;
//     System.out.println("<<<<<<list.size(): "+list.size());
	 for(int k=0;k<list.size();k++)
     {
    	 com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);    	 
			String assetId =  newassettrans.getAssetId();
			String oldassetId =  newassettrans.getOldassetId();
			String barcode =  newassettrans.getBarCode();
			String deptName =  newassettrans.getDeptCode();
			String Description = newassettrans.getDescription();   
			String assetuser = newassettrans.getAssetUser();
			String assetcode = newassettrans.getAssetCode();
			branchCode = newassettrans.getBranchCode();
//			String branchName = records.getCodeName("select BRANCH_NAME from am_ad_branch where BRANCH_ID = "+branchId+"");
			double costprice = newassettrans.getCostPrice();
			double monthlyDepr = newassettrans.getMonthlyDep();
			double accumDepr = newassettrans.getAccumDep();
			double nbv = newassettrans.getNbv();
			double depchargetoDate = newassettrans.getDeprChargeToDate();
			double improvmonthldepr = newassettrans.getImprovmonthlyDep();
			double improvAccumDepr = newassettrans.getImprovaccumDep();
			double improvCostPrice = newassettrans.getImprovcostPrice();
			double improvNbv = newassettrans.getImprovnbv();
			double totalCost = newassettrans.getTotalCost();
			double totalnbv = newassettrans.getTotalnbv();
			int totalLife = newassettrans.getUsefullife();
			String categoryCode = newassettrans.getCategoryCode();
			String execBy = records.getCodeName("select USER_NAME+' - '+full_Name from am_gb_User where User_Id = "+userId+"");
			String approvedBy = records.getCodeName("select USER_NAME+' - '+full_Name from am_gb_User where User_Id = "+supervisorId+"");
			String batchId = newassettrans.getIntegrifyId();
			String sighted = newassettrans.getAssetsighted();
			String function = newassettrans.getAssetfunction();
			String comments = newassettrans.getAssetMaintenance();
			String make = newassettrans.getAssetMake();
			String lpo = newassettrans.getLpo();
			String vendoAcct = newassettrans.getVendorAC();
			String vendorId = newassettrans.getSupplierName();
//			String vendorName = records.getCodeName("select VENDOR_NAME from am_ad_vendor where VENDOR_ID = "+vendorId+"");
			String sbucode = newassettrans.getSbuCode();
			String purchaseDate = newassettrans.getDatepurchased();
			String depr_startDate = newassettrans.getEffectiveDate();
//			System.out.println("======>depr_startDate====: "+depr_startDate);
			String yyyy = depr_startDate.substring(0, 4);
//			System.out.println("======>yyyy: "+yyyy);
			String mm = depr_startDate.substring(5, 7);
//			System.out.println("======>mm: "+mm);
			String dd = depr_startDate.substring(8, 10);
			depr_startDate = dd+"/"+mm+"/"+yyyy;
//			System.out.println("======>depr_startDate: "+depr_startDate);
			String deprciationDate = newassettrans.getPostingDate();
			
//			String newAccumDepr = Double.toString(accumDepr);
//			System.out.println("======>newAccumDepr: "+newAccumDepr);



			Row row = sheet.createRow((int) i);

            row.createCell((short) 0).setCellValue(k);
            row.createCell((short) 1).setCellValue(scriptExecuted);
            row.createCell((short) 2).setCellValue(execReason);
            row.createCell((short) 3).setCellValue(execBy);
            row.createCell((short) 4).setCellValue(execDate);
            row.createCell((short) 5).setCellValue(approvedBy);
			row.createCell((short) 6).setCellValue(aprovedDate);
			row.createCell((short) 7).setCellValue(status);
			row.createCell((short) 8).setCellValue(confirmed);

            i++;
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