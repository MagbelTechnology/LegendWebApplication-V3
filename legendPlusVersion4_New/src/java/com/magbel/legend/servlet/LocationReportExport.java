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
   
public class LocationReportExport extends HttpServlet
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
 //   System.out.println("======>ToDate: "+ToDate);
    String region = request.getParameter("region");
    String dept_Code = request.getParameter("deptCode");
    String asset_Id = request.getParameter("assetId");
    String report = request.getParameter("report");
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
    if(report.equalsIgnoreCase("rptMenuRBC")){fileName = branchCode+"By"+userName+"AssetRegisterConsolidationReport.xlsx";}
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

        String categoryCode = request.getParameter("category");
     Report rep = new Report();
 //    System.out.println("=====FromDate Length: "+FromDate.length());
 //    System.out.println("=====ToDate Length: "+ToDate.length());
//   System.out.println("<<<<<<bran ch_Id: "+branch_Id+"    categoryCode: "+categoryCode+"  branchCode: "+branchCode);
//   System.out.println("<<<<<<branch_Id: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  branchCode: "+branchCode);
     String ColQuery = "";
     if(branch_Id.equals("0")  && categoryCode.equals("0")  && FromDate.equals("") && ToDate.equals("")){
    	   System.out.println("======>>>>>>>No Selection: ");
    	     ColQuery ="";    
    		}
     if(!branch_Id.equals("0")  && !categoryCode.equals("0") && FromDate.equals("")  && ToDate.equals("")){
    	 System.out.println("======>>>>>>>Branch and Category Selected: "+branch_Id+"   categoryCode: "+categoryCode+"    Region: "+region);	     
    	    ColQuery ="";     
	}      
	 if(branch_Id.equals("0")  && !categoryCode.equals("0") && region.equals("0")  && ToDate.equals("")){	   
	   System.out.println("======>>>>>>>Category Selected: "+branch_Id+"   categoryCode: "+categoryCode+"    Region: "+region);
	    ColQuery ="";
	    }
	 
	 if(!branch_Id.equals("0")  && categoryCode.equals("0") && FromDate.equals("")  && ToDate.equals("")){	   
	   System.out.println("======>>>>>>>Branch Selected: "+branch_Id+"   categoryCode: "+categoryCode+"    Region: "+region);
	    ColQuery ="";
	    }
   if(branch_Id.equals("0")  && categoryCode.equals("0") && FromDate.equals("")  && ToDate.equals("")){
	   System.out.println("======>>>>>>>Region Selected: "+branch_Id+"   categoryCode: "+categoryCode+"    Region: "+region);
	    ColQuery ="";    
	}   
   if(!FromDate.equals("")  && !ToDate.equals("") && branch_Id.equals("0")  && categoryCode.equals("0")){
	System.out.println("======>>>>>>> Region and Category selected: "+branch_Id+"   categoryCode: "+categoryCode+"    Region: "+region);
    ColQuery ="";		     
	} 
   if(!branch_Id.equals("0")  && categoryCode.equals("0") && !FromDate.equals("")  && !ToDate.equals("")){
	System.out.println("======>>>>>>>Branch and Region Selection: "+branch_Id+"   categoryCode: "+categoryCode+"    Region: "+region);
    ColQuery ="";	    
	}    
   if(!FromDate.equals("")  && !ToDate.equals("") && !branch_Id.equals("0")  && !categoryCode.equals("0")){
	System.out.println("======>>>>>>>Branch, Category and Region Selection: "+branch_Id+"   categoryCode: "+categoryCode+"    Region: "+region);
    ColQuery ="";	
    }    
  
//   System.out.println("======>>>>>>>ColQuery: "+ColQuery);
     java.util.ArrayList list =rep.getAssetRegisterConcolidatedRecords(ColQuery,branch_Id,categoryCode,FromDate,ToDate);
     System.out.println("======>>>>>>>list size: "+list.size()+"        =====report: "+report);
     if(list.size()!=0){
   	 if(report.equalsIgnoreCase("rptMenuRBC")){
    	 SXSSFWorkbook workbook = new SXSSFWorkbook();
         Sheet sheet = workbook.createSheet("Demo");
         Row rowhead = sheet.createRow((int) 0);
         
         rowhead.createCell( 0).setCellValue("S/No.");
         rowhead.createCell( 1).setCellValue("Asset ID");
         rowhead.createCell( 2).setCellValue("Asset Description");
         rowhead.createCell( 3).setCellValue("Branch Name");
         rowhead.createCell( 4).setCellValue("Category Name");
         rowhead.createCell( 5).setCellValue("Department Name");
         rowhead.createCell( 6).setCellValue("Purchase Date");
         rowhead.createCell( 7).setCellValue("Depr. Start Date");
         rowhead.createCell( 8).setCellValue("Depr. End Date");
         rowhead.createCell( 9).setCellValue("Cost Price");
         rowhead.createCell( 10).setCellValue("Monthly Depreciation");
		 rowhead.createCell( 11).setCellValue("Accumulated Depr.");
		 rowhead.createCell( 12).setCellValue("NBV");
		 rowhead.createCell( 13).setCellValue("Asset User");
		 rowhead.createCell( 14).setCellValue("Asset Type");

     int i = 1;
     System.out.println("<<<<<<list.size(): "+list.size());
	 for(int k=0;k<list.size();k++)
     {
    	 com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);    	 
			String assetId =  newassettrans.getAssetId();
			
			String oldassetId =  newassettrans.getOldassetId();
			String barcode =  newassettrans.getBarCode();
			String deptCode =  newassettrans.getDeptCode();
			String deptName =  newassettrans.getDeptName();
			String Description = newassettrans.getDescription();   
			String assetuser = newassettrans.getAssetUser();
			String assetcode = newassettrans.getAssetCode();
			branchCode = newassettrans.getBranchCode();
			String branchName =  newassettrans.getBranchName();
			String categoryName =  newassettrans.getCategoryName();
			String branchId = newassettrans.getBranchId();
			//String branchName = records.getCodeName("select BRANCH_NAME from am_ad_branch where BRANCH_ID = "+branchId+"");
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
			categoryCode = newassettrans.getCategoryCode();
			String assetStatus = newassettrans.getAssetStatus(); 
//			String categoryName = records.getCodeName("select BRANCH_NAME from am_ad_branch where BRANCH_ID = "+branchId+"");
			String batchId = newassettrans.getIntegrifyId();
			String sighted = newassettrans.getAssetsighted();
			String function = newassettrans.getAssetfunction();
			String comments = newassettrans.getAssetMaintenance();
			String make = newassettrans.getAssetMake();
			String model = newassettrans.getAssetModel();
			String purchaseReason = newassettrans.getPurchaseReason();
			String lpo = newassettrans.getLpo();
			String vendoAcct = newassettrans.getVendorAC();
			String vendorId = newassettrans.getSupplierName();
			String engineNo = newassettrans.getEngineNo();
			String serialNo = newassettrans.getSerialNo();
			String location = newassettrans.getLocation();
			String state = newassettrans.getState();
			String sectionName = newassettrans.getSectionName();
			String vendorName = newassettrans.getVendorName();
			String spare1 = newassettrans.getSpare1();
			String spare2 = newassettrans.getSpare2();
			String spare3 = newassettrans.getSpare3();
			String spare4 = newassettrans.getSpare4();
			String spare5 = newassettrans.getSpare5();
			String spare6 = newassettrans.getSpare6();
			String oldAssetId = newassettrans.getOldassetId();

			//			String vendorName = records.getCodeName("select VENDOR_NAME from am_ad_vendor where VENDOR_ID = "+vendorId+"");
			
			String sbucode = newassettrans.getSbuCode();
			String purchaseDate = newassettrans.getDatepurchased() != null ? getDate(newassettrans.getDatepurchased()) : "";

			String depr_startDate = newassettrans.getEffectiveDate() != null ? getDate(newassettrans.getEffectiveDate()) : "";

			String depr_endDate = newassettrans.getDependDate() != null ? getDate(newassettrans.getDependDate()) : "";

			Row row = sheet.createRow((int) i);

	         rowhead.createCell( 0).setCellValue("S/No.");
	         rowhead.createCell( 1).setCellValue("Asset ID");
	         rowhead.createCell( 2).setCellValue("Asset Description");
	         rowhead.createCell( 3).setCellValue("Branch Name");
	         rowhead.createCell( 4).setCellValue("Category Name");
	         rowhead.createCell( 5).setCellValue("Department Name");
	         rowhead.createCell( 6).setCellValue("Purchase Date");
	         rowhead.createCell( 7).setCellValue("Depr. Start Date");
	         rowhead.createCell( 8).setCellValue("Depr. End Date");
	         rowhead.createCell( 9).setCellValue("Cost Price");
	         rowhead.createCell( 10).setCellValue("Monthly Depreciation");
			 rowhead.createCell( 11).setCellValue("Accumulated Depr.");
			 rowhead.createCell( 12).setCellValue("NBV");
			 rowhead.createCell( 13).setCellValue("Asset User");
			 rowhead.createCell( 14).setCellValue("Asset Type");
			 
			row.createCell((short) 0).setCellValue(i);
			row.createCell((short) 1).setCellValue(assetId);
            row.createCell((short) 2).setCellValue(Description);
            row.createCell((short) 3).setCellValue(branchName);
            row.createCell((short) 4).setCellValue(categoryName);
            row.createCell((short) 5).setCellValue(deptName);
            row.createCell((short) 6).setCellValue(purchaseDate);
            row.createCell((short) 7).setCellValue(depr_startDate);
            row.createCell((short) 8).setCellValue(depr_endDate);
            row.createCell((short) 9).setCellValue(costprice);
            row.createCell((short) 10).setCellValue(monthlyDepr);
            row.createCell((short) 11).setCellValue(accumDepr);
            row.createCell((short) 12).setCellValue(nbv);
			row.createCell((short) 13).setCellValue(assetuser);
			row.createCell((short) 14).setCellValue(assetStatus);
		
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