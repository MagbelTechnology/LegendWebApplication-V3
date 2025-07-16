package com.magbel.legend.servlet;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import magma.AssetRecordsBean;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;
   
public class FacilityPPMExportPost extends HttpServlet
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
	System.out.println("======>yyyy: "+yyyy);
	String mm = FromDate.substring(3, 5);
	System.out.println("======>mm: "+mm);
	String dd = FromDate.substring(0, 2);
	FromDate = yyyy+"-"+mm+"-"+dd;
    }
    System.out.println("======>FromDate: "+FromDate);
    if(!ToDate.equals("")){
	String Tyyyy = ToDate.substring(6, 10);
//	System.out.println("======>Tyyyy: "+Tyyyy);
	String Tmm = ToDate.substring(3, 5);
//	System.out.println("======>Tmm: "+Tmm);
	String Tdd = ToDate.substring(0, 2);
	ToDate = Tyyyy+"-"+Tmm+"-"+Tdd;
    }
    System.out.println("======>ToDate: "+ToDate);
    String dept_Code = request.getParameter("deptCode");
    String asset_Id = request.getParameter("assetId");
    String filter = request.getParameter("filter");
    System.out.println("<<<<<<filter: "+filter);
    String branchIdNo = records.getCodeName("select BRANCH from am_gb_User where USER_ID = ? ",userId);
    //String branchCode = request.getParameter("BRANCH_CODE");
    String userName = records.getCodeName("select USER_NAME from am_gb_User where USER_ID = ? ",userId);
//    System.out.println("<<<<<<branch_Id: "+branch_Id);
    String branchCode = "";
    if(!branchIdNo.equals("0")){
    	branchCode = records.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID = ? ",branchIdNo);
    }
    String fileName =  branchCode+"By"+userName+"FacilityPPMReport.xls";
//    if(reportType.equalsIgnoreCase("D")){fileName = branchCode+"By"+userName+"FacilityPPMReport.xls";}
//    if(reportType.equalsIgnoreCase("S")){fileName = branchCode+"By"+userName+"FacilityPPMReport.xls";}
//    System.out.println("<<<<<<branch_Code: "+branch_Code);
//    String userName = request.getParameter("userName");
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

        String categoryCode = request.getParameter("category");
     Report rep = new Report();
//   System.out.println("<<<<<<reportType: "+reportType);
     String ColQuery = "select b.ZONE_CODE, c.Zone_Name,a.BRANCH_CODE, * from FM_PPM a, am_ad_branch b, am_ad_Zone c "
     		+ "where a.BRANCH_CODE = b.BRANCH_CODE and b.ZONE_CODE = c.Zone_Code " + filter;

   System.out.println("======>>>>>>>ColQuery: "+ColQuery);
     java.util.ArrayList list =rep.getFacilityPPMRecords(ColQuery);
     System.out.println("======>>>>>>>list size: "+list.size());
     if(list.size()!=0){
    	 SXSSFWorkbook workbook = new SXSSFWorkbook();
         Sheet sheet = workbook.createSheet("Excel");
         Row rowhead = sheet.createRow((int) 0);
         
         rowhead.createCell((short) 0).setCellValue("S/No.");
         rowhead.createCell((short) 1).setCellValue("Branch Name");
         rowhead.createCell((short) 2).setCellValue("Vendor"); 
         rowhead.createCell((short) 3).setCellValue("Last ServiceDate");
         rowhead.createCell((short) 4).setCellValue("First Qtr");
         rowhead.createCell((short) 5).setCellValue("Status");
         rowhead.createCell((short) 6).setCellValue("Second Qtr");
         rowhead.createCell((short) 7).setCellValue("Status");
         rowhead.createCell((short) 8).setCellValue("Third Qtr");
         rowhead.createCell((short) 9).setCellValue("Status");
         rowhead.createCell((short) 10).setCellValue("Fourth Qtr");
         rowhead.createCell((short) 11).setCellValue("Status");

     int i = 1;
     System.out.println("<<<<<<list.size(): "+list.size());
	 for(int k=0;k<list.size();k++)
     {

			magma.net.vao.FMppmAllocation ppmtrans = (magma.net.vao.FMppmAllocation)list.get(k);
			
			String transId = ppmtrans.getTransId();
							
			String branchCode1 = ppmtrans.getBranchCode();
	
			String categoryCode1 = ppmtrans.getCategoryCode();
			
			String subCateCode = ppmtrans.getSubCategoryCode();
			
			String vendorCode = ppmtrans.getVendorCode();
			String description = ppmtrans.getDescription();
			String q1Date = ppmtrans.getFq_DueStatus1();
			String q2Date = ppmtrans.getFq_DueStatus2();
			String q3Date = ppmtrans.getFq_DueStatus3();
			String q4Date = ppmtrans.getFq_DueStatus4();
			String q1DueDate = ppmtrans.getFq_DueStatus1();
			String q2DueDate = ppmtrans.getFq_DueStatus2();
			q1DueDate = q1DueDate.substring(0,10);
			String q3DueDate = ppmtrans.getFq_DueStatus3();
			String q4DueDate = ppmtrans.getFq_DueStatus4();
			String q1Status = ppmtrans.getFq_Status1();
			String q2Status = ppmtrans.getFq_Status2();
			String q3Status = ppmtrans.getFq_Status3();
			String q4Status = ppmtrans.getFq_Status4();
			String type = ppmtrans.getType();
			String lastserviceDate = ppmtrans.getLastServiceDate();
			lastserviceDate = lastserviceDate.substring(0,10);
			String branchName = records.getCodeName("select BRANCH_NAME from am_ad_branch where BRANCH_CODE =  '"+branchCode1+"'");
			String vendorName = records.getCodeName("select Vendor_Name from am_ad_vendor where Vendor_Code =  '"+vendorCode+"'");
			Row row = sheet.createRow((int) i);

			//System.out.println("we are here 5");
			row.createCell((short) 0).setCellValue(i);
			row.createCell((short) 1).setCellValue(branchName);
            row.createCell((short) 2).setCellValue(vendorName);
            row.createCell((short) 3).setCellValue(lastserviceDate);
            row.createCell((short) 4).setCellValue(q1Date);
            row.createCell((short) 5).setCellValue(q1Status);
            row.createCell((short) 6).setCellValue(q2Date);
            row.createCell((short) 7).setCellValue(q2Status);
            row.createCell((short) 8).setCellValue(q3Date);
            row.createCell((short) 9).setCellValue(q3Status);
            row.createCell((short) 10).setCellValue(q4Date);
            row.createCell((short) 11).setCellValue(q4Status);
			
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