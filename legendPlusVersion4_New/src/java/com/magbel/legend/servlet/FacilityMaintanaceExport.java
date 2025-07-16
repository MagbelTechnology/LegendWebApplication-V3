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

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;
   
public class FacilityMaintanaceExport extends HttpServlet
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
//    System.out.println("======>ToDate: "+ToDate);
    String dept_Code = request.getParameter("deptCode");
    String asset_Id = request.getParameter("assetId");
    String reportType = request.getParameter("reportType");
    String branchIdNo = records.getCodeName("select BRANCH from am_gb_User where USER_ID = ? ",userId);
    //String branchCode = request.getParameter("BRANCH_CODE");
    String userName = records.getCodeName("select USER_NAME from am_gb_User where USER_ID = ? ",userId);
//    System.out.println("<<<<<<branch_Id: "+branch_Id);
    String branchCode = "";
    if(!branchIdNo.equals("0")){
    	branchCode = records.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID = ? ",branchIdNo);
    }
    String fileName = "";
    if(reportType.equalsIgnoreCase("D")){fileName = branchCode+"By"+userName+"FacilityMaintenanceDetailReport.xls";}
    if(reportType.equalsIgnoreCase("S")){fileName = branchCode+"By"+userName+"FacilityMaintenanceSummaryReport.xls";}
//    System.out.println("<<<<<<branch_Code: "+branch_Code);
//    String userName = request.getParameter("userName");
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

        String categoryCode = request.getParameter("category");
     Report rep = new Report();
//   System.out.println("<<<<<<reportType: "+reportType);
     String ColQuery = "";
     if(reportType.equalsIgnoreCase("D")){
    	 if(asset_Id.equals("")  && !FromDate.equals("")  && !ToDate.equals("")){	  
//    	 System.out.println("======>>>>>>>Details Selected asset_Id: "+asset_Id+"    FromDate: "+FromDate+"   ToDate: "+ToDate);	     
	     ColQuery ="select b.Asset_Id,REGISTRATION_NO,a.DESCRIPTION,a.Cost_Price,repaired_date,Vendor_Name,details,component_replaced,"
	 + "last_Pm_Date, next_Pm_Date,c.CREATE_DATE,b.transaction_date "
	 + "from FM_MAINTENANCE_DETAILS a,am_Raisentry_Transaction b,FM_MAINTENANCE_HISTORY c, am_ad_vendor v  "
	 + "where a.HIST_ID = c.HIST_ID and CONVERT(varchar, c.LT_ID) = CONVERT(varchar, b.TRANS_ID) "
	 + "and TECHNICIAN_NAME = v.Vendor_Id and b.page1 = 'FACILITY MAINTENANCE RAISE ENTRY' and b.ISO = '000' " 
     + "and transaction_date between ? and ? "
     + " ORDER BY b.ASSET_ID, REPAIRED_DATE,TRANSACTION_DATE ";	
    	 }
    	 if(!asset_Id.equals("")  && !FromDate.equals("")  && !ToDate.equals("")){	  
//    	 System.out.println("======>>>>>>>Details Selected asset_Id: "+asset_Id+"    FromDate: "+FromDate+"   ToDate: "+ToDate);	     
	     ColQuery ="select b.Asset_Id,REGISTRATION_NO,a.DESCRIPTION,a.Cost_Price,repaired_date,Vendor_Name,details,component_replaced,"
	 + "last_Pm_Date, next_Pm_Date,c.CREATE_DATE,b.transaction_date "
	 + "from FM_MAINTENANCE_DETAILS a,am_Raisentry_Transaction b,FM_MAINTENANCE_HISTORY c, am_ad_vendor v  "
	 + "where a.HIST_ID = c.HIST_ID and CONVERT(varchar, c.LT_ID) = CONVERT(varchar, b.TRANS_ID) "
	 + "and TECHNICIAN_NAME = v.Vendor_Id and b.page1 = 'FACILITY MAINTENANCE RAISE ENTRY' and b.ISO = '000' " 
     + "and b.Asset_id = ? and transaction_date between ? and ? "
     + " ORDER BY b.ASSET_ID, REPAIRED_DATE,TRANSACTION_DATE ";	
    	 }    	 
    	 if(!asset_Id.equals("")  && FromDate.equals("")  && ToDate.equals("")){	  
//    	 System.out.println("======>>>>>>>Details Selected asset_Id: "+asset_Id+"    FromDate: "+FromDate+"   ToDate: "+ToDate);	     
	     ColQuery ="select b.Asset_Id,REGISTRATION_NO,a.DESCRIPTION,a.Cost_Price,repaired_date,Vendor_Name,details,component_replaced,"
	 + "last_Pm_Date, next_Pm_Date,c.CREATE_DATE,b.transaction_date "
	 + "from FM_MAINTENANCE_DETAILS a,am_Raisentry_Transaction b,FM_MAINTENANCE_HISTORY c, am_ad_vendor v  "
	 + "where a.HIST_ID = c.HIST_ID and CONVERT(varchar, c.LT_ID) = CONVERT(varchar, b.TRANS_ID) "
	 + "and TECHNICIAN_NAME = v.Vendor_Id and b.page1 = 'FACILITY MAINTENANCE RAISE ENTRY' and b.ISO = '000' " 
	 + "and b.Asset_id = ? "
     + " ORDER BY b.ASSET_ID, REPAIRED_DATE,TRANSACTION_DATE ";	
    	 }
	}      
     if(reportType.equalsIgnoreCase("S")){	   
    	 if(asset_Id.equals("")  && !FromDate.equals("")  && !ToDate.equals("")){	    	 
//	   System.out.println("======>>>>>>>reportType Selected: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  dept_Code: "+dept_Code+"   asset_Id: "+asset_Id);
     ColQuery ="select b.Asset_Id,REGISTRATION_NO,details AS DESCRIPTION,a.Cost_Price,repaired_date,Vendor_Name,details,component_replaced,"
     + "last_Pm_Date, next_Pm_Date,a.CREATE_DATE,b.transaction_date  "
     + "from FM_MAINTENANCE_HISTORY a,am_Raisentry_Transaction b, am_ad_vendor v "
	 + "where CONVERT(varchar, a.LT_ID) = CONVERT(varchar, b.TRANS_ID) and TECHNICIAN_NAME = v.Vendor_Id "
	 + "and b.page1 = 'FACILITY MAINTENANCE RAISE ENTRY' and b.ISO = '000' "
	 + "and transaction_date between ? and ? " 
	 + "ORDER BY b.ASSET_ID, REPAIRED_DATE,TRANSACTION_DATE ";     
    	 }
    	 if(!asset_Id.equals("")  && !FromDate.equals("")  && !ToDate.equals("")){	    	 
//	   System.out.println("======>>>>>>>reportType Selected: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  dept_Code: "+dept_Code+"   asset_Id: "+asset_Id);
     ColQuery ="select b.Asset_Id,REGISTRATION_NO,details AS DESCRIPTION,a.Cost_Price,repaired_date,Vendor_Name,details,component_replaced,"
     + "last_Pm_Date, next_Pm_Date,a.CREATE_DATE,b.transaction_date  "
     + "from FM_MAINTENANCE_HISTORY a,am_Raisentry_Transaction b, am_ad_vendor v "
	 + "where CONVERT(varchar, a.LT_ID) = CONVERT(varchar, b.TRANS_ID) and TECHNICIAN_NAME = v.Vendor_Id "
	 + "and b.page1 = 'FACILITY MAINTENANCE RAISE ENTRY' and b.ISO = '000' "
	 + "and b.Asset_id = ? and transaction_date between ? and ? "
	 + "ORDER BY b.ASSET_ID, REPAIRED_DATE,TRANSACTION_DATE ";     
    	 }    	 
    	 if(!asset_Id.equals("")  && FromDate.equals("")  && ToDate.equals("")){	    	 
//	   System.out.println("======>>>>>>>reportType Selected: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  dept_Code: "+dept_Code+"   asset_Id: "+asset_Id);
     ColQuery ="select b.Asset_Id,REGISTRATION_NO,details AS DESCRIPTION,a.Cost_Price,repaired_date,Vendor_Name,details,component_replaced,"
     + "last_Pm_Date, next_Pm_Date,a.CREATE_DATE,b.transaction_date  "
     + "from FM_MAINTENANCE_HISTORY a,am_Raisentry_Transaction b, am_ad_vendor v "
	 + "where CONVERT(varchar, a.LT_ID) = CONVERT(varchar, b.TRANS_ID) and TECHNICIAN_NAME = v.Vendor_Id "
	 + "and b.page1 = 'FACILITY MAINTENANCE RAISE ENTRY' and b.ISO = '000' "
	 + "and b.Asset_id = ? "
	 + "ORDER BY b.ASSET_ID, REPAIRED_DATE,TRANSACTION_DATE ";     
    	 }    	 
   }

//   System.out.println("======>>>>>>>ColQuery: "+ColQuery);
     java.util.ArrayList list =rep.getFacilityMaintenanceRecords(ColQuery,FromDate,ToDate,asset_Id);
     System.out.println("======>>>>>>>list size: "+list.size());
     if(list.size()!=0){
    	 if(reportType.equalsIgnoreCase("D")){
    	 SXSSFWorkbook workbook = new SXSSFWorkbook();
         Sheet sheet = workbook.createSheet("Excel");
         Row rowhead = sheet.createRow((int) 0);
         
         rowhead.createCell((short) 0).setCellValue("S/No.");
         rowhead.createCell((short) 1).setCellValue("ASSET ID");
         rowhead.createCell((short) 2).setCellValue("VEHICLE NUMBER"); 
         rowhead.createCell((short) 3).setCellValue("DESCRIPTION");
         rowhead.createCell((short) 4).setCellValue("REPAIR DATE");
         rowhead.createCell((short) 5).setCellValue("DETAILS");
         rowhead.createCell((short) 6).setCellValue("COMPONENT REPLACED");
         rowhead.createCell((short) 7).setCellValue("LAST MAINTENANCE DATE");
         rowhead.createCell((short) 8).setCellValue("NEXT MAINTENANCE DATE");
         rowhead.createCell((short) 9).setCellValue("COST PRICE");
         rowhead.createCell((short) 10).setCellValue("CREATE DATE");
         rowhead.createCell((short) 11).setCellValue("TRANSACTION DATE");

     int i = 1;
     System.out.println("<<<<<<list.size(): "+list.size());
	 for(int k=0;k<list.size();k++)
     {
		
    	magma.net.vao.FleetManatainanceRecord  fleettrans = (magma.net.vao.FleetManatainanceRecord)list.get(k);    	 
			String assetId =  fleettrans.getAsseId();
			//System.out.println("======K: "+k);
		//System.out.println("======assetId: "+assetId);
			String registrationNo =  fleettrans.getRegistrationNo();
			String componentReplace =  fleettrans.getComponentReplaced();
			String Description = fleettrans.getDescription();   
			String lastMaintDate = fleettrans.getLastPerformedDate();
			String repairDate =  fleettrans.getDateOfRepair();
			String details =  fleettrans.getDetails();
			String nextMaintDate = fleettrans.getNextPerformedDate();
			String createDate = fleettrans.getNextNotificationDate();
			String TransactionDate = fleettrans.getFirstNotificationDate();

			//String branchName = records.getCodeName("select BRANCH_NAME from am_ad_branch where BRANCH_ID = "+branchId+"");
			
			double costprice = fleettrans.getCost();
			
			//String categoryName = records.getCodeName("select BRANCH_NAME from am_ad_branch where BRANCH_ID = "+branchId+"");

		//	String vendorName = records.getCodeName("select VENDOR_NAME from am_ad_vendor where VENDOR_ID = "+vendorId+"");
			
			String sbucode = fleettrans.getSbuCode();
			
			Row row = sheet.createRow((int) i);

			//System.out.println("we are here 5");
			row.createCell((short) 0).setCellValue(i);
			row.createCell((short) 1).setCellValue(assetId);
            row.createCell((short) 2).setCellValue(registrationNo);
            row.createCell((short) 3).setCellValue(Description);
            row.createCell((short) 4).setCellValue(repairDate);
            row.createCell((short) 5).setCellValue(details);
            row.createCell((short) 6).setCellValue(componentReplace);
            row.createCell((short) 7).setCellValue(lastMaintDate);
            row.createCell((short) 8).setCellValue(nextMaintDate);
            row.createCell((short) 9).setCellValue(costprice);
            row.createCell((short) 10).setCellValue(createDate);
            row.createCell((short) 11).setCellValue(TransactionDate);
			
		//	System.out.println("======>oldAssetId====: "+oldAssetId+"  Index: "+i);

            i++;
     }
//	 System.out.println("we are here 2");
	   OutputStream stream = response.getOutputStream();
         workbook.write(stream);
        // stream.close();
         workbook.close();  
         System.out.println("Data is saved in excel file.");
         
    /* w.write();
     w.close(); */
     }
    	 if(reportType.equalsIgnoreCase("S")){
    	 SXSSFWorkbook workbook = new SXSSFWorkbook();
         Sheet sheet = workbook.createSheet("Excel");
         Row rowhead = sheet.createRow((int) 0);
         
         rowhead.createCell((short) 0).setCellValue("S/No.");
         rowhead.createCell((short) 1).setCellValue("ASSET ID");
         rowhead.createCell((short) 2).setCellValue("VEHICLE NUMBER"); 
         rowhead.createCell((short) 3).setCellValue("REPAIR DATE");
         rowhead.createCell((short) 4).setCellValue("DETAILS");
         rowhead.createCell((short) 5).setCellValue("COMPONENT REPLACED");
         rowhead.createCell((short) 6).setCellValue("LAST MAINTENANCE DATE");
         rowhead.createCell((short) 7).setCellValue("NEXT MAINTENANCE DATE");
         rowhead.createCell((short) 8).setCellValue("COST PRICE");
         rowhead.createCell((short) 9).setCellValue("CREATE DATE");
         rowhead.createCell((short) 10).setCellValue("TRANSACTION DATE");
     int i = 1;
     System.out.println("<<<<<<list.size(): "+list.size());
	 for(int k=0;k<list.size();k++)
     {
		
		 magma.net.vao.FleetManatainanceRecord  fleettrans = (magma.net.vao.FleetManatainanceRecord)list.get(k); 
			String assetId =  fleettrans.getAsseId();
			String registrationNo =  fleettrans.getRegistrationNo();
			String componentReplace =  fleettrans.getComponentReplaced();
			String Description = fleettrans.getDescription();   
			String lastMaintDate = fleettrans.getLastPerformedDate();
			String repairDate =  fleettrans.getDateOfRepair();
			String details =  fleettrans.getDetails();
			String nextMaintDate = fleettrans.getNextPerformedDate();
			String createDate = fleettrans.getNextNotificationDate();
			String TransactionDate = fleettrans.getFirstNotificationDate();
			
			//String branchName = records.getCodeName("select BRANCH_NAME from am_ad_branch where BRANCH_ID = "+branchId+"");
			
			double costprice = fleettrans.getCost();

			String sbucode = fleettrans.getSbuCode();

			Row row = sheet.createRow((int) i);

			//System.out.println("we are here 5");
			row.createCell((short) 0).setCellValue(i);
			row.createCell((short) 1).setCellValue(assetId);
            row.createCell((short) 2).setCellValue(registrationNo);
//            row.createCell((short) 3).setCellValue(Description);
            row.createCell((short) 3).setCellValue(repairDate);
            row.createCell((short) 4).setCellValue(details);
            row.createCell((short) 5).setCellValue(componentReplace);
            row.createCell((short) 6).setCellValue(lastMaintDate);
            row.createCell((short) 7).setCellValue(nextMaintDate);
            row.createCell((short) 8).setCellValue(costprice);
            row.createCell((short) 9).setCellValue(createDate);
            row.createCell((short) 10).setCellValue(TransactionDate);
		
			
		//	System.out.println("======>oldAssetId====: "+oldAssetId+"  Index: "+i);

            i++;
     }
//	 System.out.println("we are here 2");
	   OutputStream stream = response.getOutputStream();
         workbook.write(stream);
        // stream.close();
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
	  // System.out.println("<<<<<<<<<<<< Date: " + date);
	   String yyyy = date.substring(0, 4);
		String mm = date.substring(5, 7);
		String dd = date.substring(8, 10);
		date = dd+"/"+mm+"/"+yyyy;
	   
		
	   return date;
   }
}