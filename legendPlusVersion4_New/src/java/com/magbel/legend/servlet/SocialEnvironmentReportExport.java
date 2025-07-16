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
   
public class SocialEnvironmentReportExport extends HttpServlet
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
    //String categoryCode = request.getParameter("category") == null ? "0" : request.getParameter("category");
    //String user = request.getParameter("user") == null ? "0" : request.getParameter("user");
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
    String report = "rptMenuSocial";
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
    if(report.equalsIgnoreCase("rptMenuSocial")){fileName = branchCode+"By"+userName+"SocialEnvironmentReportExport.xlsx";}
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
    	     ColQuery ="SELECT b.BRANCH_NAME,c.Region_Name,z.Zone_Name,a.BRANCH_CODE, isnull([001], 0) AS '001',isnull([002], 0) AS '002',isnull([003], 0) AS '003',isnull([004], 0) AS '004',isnull([005], 0) AS '005',isnull([006], 0) AS '006' from SocialEnvPvt a, am_ad_branch b, am_ad_region c,am_ad_Zone z\n"
    	     		+ "where a.BRANCH_CODE = b.BRANCH_CODE\n"
    	     		+ "and b.REGION_CODE = c.Region_Code\n"
    	     		+ "and b.ZONE_CODE = z.Zone_Code";
    

  
//   System.out.println("======>>>>>>>ColQuery: "+ColQuery);
     java.util.ArrayList list =rep.getSocialEnvironmentListRecords(ColQuery,branch_Id,FromDate,ToDate);
     System.out.println("======>>>>>>>list size: "+list.size()+"        =====report: "+report);
     if(list.size()!=0){
   	 if(report.equalsIgnoreCase("rptMenuSocial")){
    	 SXSSFWorkbook workbook = new SXSSFWorkbook();
         Sheet sheet = workbook.createSheet("Demo");
         Row rowhead = sheet.createRow((int) 0);
         
         
         
         
         rowhead.createCell( 0).setCellValue("S/No.");
         rowhead.createCell( 1).setCellValue("BRANCH_NAME");
         rowhead.createCell( 2).setCellValue("Region_Name");
         rowhead.createCell( 3).setCellValue("Zone_Name");
         rowhead.createCell( 4).setCellValue("BRANCH_CODE");
         rowhead.createCell( 5).setCellValue("001");
         rowhead.createCell( 6).setCellValue("002");
         rowhead.createCell( 7).setCellValue("003");
         rowhead.createCell( 8).setCellValue("004");
         rowhead.createCell( 9).setCellValue("005");
         rowhead.createCell( 10).setCellValue("006");

		 

     int i = 1;
     System.out.println("<<<<<<list.size(): "+list.size());
	 for(int k=0;k<list.size();k++)
     {
    	 com.magbel.legend.vao.newAssetTransaction  newTransaction = (com.magbel.legend.vao.newAssetTransaction)list.get(k);    	 
			String branchName =  newTransaction.getBranchName();
			String regionName = newTransaction.getDeptName();
			String zoneName = newTransaction.getCategoryName();
			String branch_Code = newTransaction.getBranchCode();
			double digitOne = newTransaction.getAmount();
			double digitTwo = newTransaction.getCostPrice();
			double digitThree = newTransaction.getImprovaccumDep();
			double digitFour = newTransaction.getImprovcostPrice();
			double digitFive = newTransaction.getDepRate();
			double digitSix = newTransaction.getDeprChargeToDate();
	
			
			
			//			String vendorName = records.getCodeName("select VENDOR_NAME from am_ad_vendor where VENDOR_ID = "+vendorId+"")

			Row row = sheet.createRow((int) i);

			 
			row.createCell((short) 0).setCellValue(i);
			row.createCell((short) 1).setCellValue(branchName);
            row.createCell((short) 2).setCellValue(regionName);
            row.createCell((short) 3).setCellValue(zoneName);
            row.createCell((short) 4).setCellValue(branch_Code);
            row.createCell((short) 5).setCellValue(digitOne);
            row.createCell((short) 6).setCellValue(digitTwo);
            row.createCell((short) 7).setCellValue(digitThree);
            row.createCell((short) 8).setCellValue(digitFour);
            row.createCell((short) 9).setCellValue(digitFive);
            row.createCell((short) 10).setCellValue(digitFive);

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