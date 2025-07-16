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
   
public class ConsolidatedFixedAssetVerificationReportExport extends HttpServlet
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
    String region = request.getParameter("region");
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
	System.out.println("======>Tyyyy: "+Tyyyy);
	String Tmm = ToDate.substring(3, 5);
	System.out.println("======>Tmm: "+Tmm);
	String Tdd = ToDate.substring(0, 2);
	ToDate = Tyyyy+"-"+Tmm+"-"+Tdd;
    }
    System.out.println("======>ToDate: "+ToDate);
//    String dept_Code = request.getParameter("deptCode");
    String asset_Id = request.getParameter("assetId");
    String report = request.getParameter("report");
    String branchIdNo = records.getCodeName("select BRANCH from am_gb_User where USER_ID = ? ",userId);
    //String branchCode = request.getParameter("BRANCH_CODE");
    String userName = records.getCodeName("select USER_NAME from am_gb_User where USER_ID = ? ",userId);
    System.out.println("<<<<<<region: "+region);
    String branchCode = "";
    if(!branchIdNo.equals("0")){
    	branchCode = records.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID = ? ",branchIdNo);
    }
    System.out.println("<<<<<<branch_Code: "+branch_Code);
//    String userName = request.getParameter("userName");
    String fileName = "";
//    if(report.equalsIgnoreCase("rptMenuBCRList")){fileName = branchCode+"By"+userName+"AssetRegisterListReport.xlsx";}
//    if(report.equalsIgnoreCase("rptMenuBCLDTL")){fileName = branchCode+"By"+userName+"AssetDetailReport.xlsx";}
    if(report.equalsIgnoreCase("rptMenuConsolidVerify")){fileName = branchCode+"By"+userName+"ConsolidatedAssetVerificationReport.xlsx";}
    
    String filePath = System.getProperty("user.home")+"\\Downloads";
    System.out.println("<<<<<<filePath: "+filePath);
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

       // String categoryCode = request.getParameter("category");
     Report rep = new Report();
   System.out.println("<<<<<<region: "+region+"   branchCode: "+branchCode);
     String ColQuery = "";
     if(region.equals("0") && FromDate.equals("") && ToDate.equals("")){
  	   System.out.println("======>>>>>>>No Selection: "+region+"    FromDate: "+FromDate+"   ToDate: "+ToDate);
  	     ColQuery ="select LEVEL,CLASIFICATION,BRANCH_CODE,BRANCH_NAME,ZONE_CODE,REGION_CODE,ZONE_NAME,REGION_NAME, SUM(ISNULL([COMPUTER], 0 )) AS [COMPUTER], SUM(ISNULL([FURNITURE & FITTINGS], 0 )) AS [FURNITURE & FITTINGS], SUM(ISNULL([MACHINE AND EQUIPMENT], 0 )) AS [MACHINE AND EQUIPMENT], SUM(ISNULL([BUILDING], 0 )) AS [BUILDING], SUM(ISNULL([MOTOR VEHICLE], 0 )) AS [MOTOR VEHICLE],\n"
  	     		+ "SUM(ISNULL([STAFF FUNITURE & FITTINGS], 0 )) AS [STAFF FUNITURE & FITTINGS], SUM(ISNULL([LEASEHOLD IMPROVEMENT 1], 0 ))[LEASEHOLD IMPROVEMENT 1],SUM(ISNULL([INTANGIBLE ASSETS - SOFTWARE], 0 )) AS [INTANGIBLE ASSETS - SOFTWARE],SUM(ISNULL([LEASEHOLD LAND], 0 )) AS [LEASEHOLD LAND],\n"
  	     		+ "SUM(ISNULL([LEASEHOLD IMPROVEMENT 2], 0 )) AS [LEASEHOLD IMPROVEMENT 2],SUM(ISNULL([LEASEHOLD IMPROVEMENT 3], 0 )) AS [LEASEHOLD IMPROVEMENT 3],SUM(ISNULL([LEASEHOLD IMPROVEMENT 4], 0 )) AS [LEASEHOLD IMPROVEMENT 4],SUM(ISNULL([LEASEHOLD IMPROVEMENT 5], 0 )) AS [LEASEHOLD IMPROVEMENT 5],\n"
  	     		+ "SUM(ISNULL([STAFF MACHINE & EQUIPMENTS], 0 )) AS [STAFF MACHINE & EQUIPMENTS],SUM(ISNULL([WORK-IN-PROGRESS], 0 )) AS [WORK-IN-PROGRESS] from ConsolidatedVerification\n"
  	     		+ "WHERE LEVEL IS NOT NULL\n"
  	     		+ "GROUP BY LEVEL,CLASIFICATION,BRANCH_CODE,BRANCH_NAME,ZONE_CODE,REGION_CODE,ZONE_NAME,REGION_NAME ";    
  		}
     
     
     if(region.equals("0")  && !FromDate.equals("") && !ToDate.equals("")){
    	 System.out.println("======>>>>>>>Date Selected: "+region+"    FromDate: "+FromDate+"   ToDate: "+ToDate);	     
	     ColQuery ="select LEVEL,CLASIFICATION,BRANCH_CODE,BRANCH_NAME,ZONE_CODE,REGION_CODE,ZONE_NAME,REGION_NAME, SUM(ISNULL([COMPUTER], 0 )) AS [COMPUTER], SUM(ISNULL([FURNITURE & FITTINGS], 0 )) AS [FURNITURE & FITTINGS], SUM(ISNULL([MACHINE AND EQUIPMENT], 0 )) AS [MACHINE AND EQUIPMENT], SUM(ISNULL([BUILDING], 0 )) AS [BUILDING], SUM(ISNULL([MOTOR VEHICLE], 0 )) AS [MOTOR VEHICLE],\n"
	     		+ "SUM(ISNULL([STAFF FUNITURE & FITTINGS], 0 )) AS [STAFF FUNITURE & FITTINGS], SUM(ISNULL([LEASEHOLD IMPROVEMENT 1], 0 ))[LEASEHOLD IMPROVEMENT 1],SUM(ISNULL([INTANGIBLE ASSETS - SOFTWARE], 0 )) AS [INTANGIBLE ASSETS - SOFTWARE],SUM(ISNULL([LEASEHOLD LAND], 0 )) AS [LEASEHOLD LAND],\n"
	     		+ "SUM(ISNULL([LEASEHOLD IMPROVEMENT 2], 0 )) AS [LEASEHOLD IMPROVEMENT 2],SUM(ISNULL([LEASEHOLD IMPROVEMENT 3], 0 )) AS [LEASEHOLD IMPROVEMENT 3],SUM(ISNULL([LEASEHOLD IMPROVEMENT 4], 0 )) AS [LEASEHOLD IMPROVEMENT 4],SUM(ISNULL([LEASEHOLD IMPROVEMENT 5], 0 )) AS [LEASEHOLD IMPROVEMENT 5],\n"
	     		+ "SUM(ISNULL([STAFF MACHINE & EQUIPMENTS], 0 )) AS [STAFF MACHINE & EQUIPMENTS],SUM(ISNULL([WORK-IN-PROGRESS], 0 )) AS [WORK-IN-PROGRESS] from ConsolidatedVerification\n"
	     		+ "WHERE LEVEL IS NOT NULL\n"
	     		+ "AND PROOF_DATE BETWEEN ? AND ?\n"
	     		+ "GROUP BY LEVEL,CLASIFICATION,BRANCH_CODE,BRANCH_NAME,ZONE_CODE,REGION_CODE,ZONE_NAME,REGION_NAME ";  
	}      
	 if(!region.equals("0")  && !FromDate.equals("") && !ToDate.equals("")){	   
	   System.out.println("======>>>>>>>Region and Date Selected: "+region+"    FromDate: "+FromDate+"   ToDate: "+ToDate);
	     ColQuery ="select LEVEL,CLASIFICATION,BRANCH_CODE,BRANCH_NAME,ZONE_CODE,REGION_CODE,ZONE_NAME,REGION_NAME, SUM(ISNULL([COMPUTER], 0 )) AS [COMPUTER], SUM(ISNULL([FURNITURE & FITTINGS], 0 )) AS [FURNITURE & FITTINGS], SUM(ISNULL([MACHINE AND EQUIPMENT], 0 )) AS [MACHINE AND EQUIPMENT], SUM(ISNULL([BUILDING], 0 )) AS [BUILDING], SUM(ISNULL([MOTOR VEHICLE], 0 )) AS [MOTOR VEHICLE],\n"
	     		+ "SUM(ISNULL([STAFF FUNITURE & FITTINGS], 0 )) AS [STAFF FUNITURE & FITTINGS], SUM(ISNULL([LEASEHOLD IMPROVEMENT 1], 0 ))[LEASEHOLD IMPROVEMENT 1],SUM(ISNULL([INTANGIBLE ASSETS - SOFTWARE], 0 )) AS [INTANGIBLE ASSETS - SOFTWARE],SUM(ISNULL([LEASEHOLD LAND], 0 )) AS [LEASEHOLD LAND],\n"
	     		+ "SUM(ISNULL([LEASEHOLD IMPROVEMENT 2], 0 )) AS [LEASEHOLD IMPROVEMENT 2],SUM(ISNULL([LEASEHOLD IMPROVEMENT 3], 0 )) AS [LEASEHOLD IMPROVEMENT 3],SUM(ISNULL([LEASEHOLD IMPROVEMENT 4], 0 )) AS [LEASEHOLD IMPROVEMENT 4],SUM(ISNULL([LEASEHOLD IMPROVEMENT 5], 0 )) AS [LEASEHOLD IMPROVEMENT 5],\n"
	     		+ "SUM(ISNULL([STAFF MACHINE & EQUIPMENTS], 0 )) AS [STAFF MACHINE & EQUIPMENTS],SUM(ISNULL([WORK-IN-PROGRESS], 0 )) AS [WORK-IN-PROGRESS] from ConsolidatedVerification\n"
	     		+ "WHERE LEVEL IS NOT NULL\n"
	     		+ "AND ZONE_CODE =? AND PROOF_DATE BETWEEN ? AND ?\n"
	     		+ "GROUP BY LEVEL,CLASIFICATION,BRANCH_CODE,BRANCH_NAME,ZONE_CODE,REGION_CODE,ZONE_NAME,REGION_NAME ";   
		}  
	 
	 if(!region.equals("0")  && FromDate.equals("") && ToDate.equals("")){	   
		   System.out.println("======>>>>>>>Region Selected: "+branch_Id+"    FromDate: "+FromDate+"   ToDate: "+ToDate);
		     ColQuery ="select LEVEL,CLASIFICATION,BRANCH_CODE,BRANCH_NAME,ZONE_CODE,REGION_CODE,ZONE_NAME,REGION_NAME, SUM(ISNULL([COMPUTER], 0 )) AS [COMPUTER], SUM(ISNULL([FURNITURE & FITTINGS], 0 )) AS [FURNITURE & FITTINGS], SUM(ISNULL([MACHINE AND EQUIPMENT], 0 )) AS [MACHINE AND EQUIPMENT], SUM(ISNULL([BUILDING], 0 )) AS [BUILDING], SUM(ISNULL([MOTOR VEHICLE], 0 )) AS [MOTOR VEHICLE],\n"
		     		+ "SUM(ISNULL([STAFF FUNITURE & FITTINGS], 0 )) AS [STAFF FUNITURE & FITTINGS], SUM(ISNULL([LEASEHOLD IMPROVEMENT 1], 0 ))[LEASEHOLD IMPROVEMENT 1],SUM(ISNULL([INTANGIBLE ASSETS - SOFTWARE], 0 )) AS [INTANGIBLE ASSETS - SOFTWARE],SUM(ISNULL([LEASEHOLD LAND], 0 )) AS [LEASEHOLD LAND],\n"
		     		+ "SUM(ISNULL([LEASEHOLD IMPROVEMENT 2], 0 )) AS [LEASEHOLD IMPROVEMENT 2],SUM(ISNULL([LEASEHOLD IMPROVEMENT 3], 0 )) AS [LEASEHOLD IMPROVEMENT 3],SUM(ISNULL([LEASEHOLD IMPROVEMENT 4], 0 )) AS [LEASEHOLD IMPROVEMENT 4],SUM(ISNULL([LEASEHOLD IMPROVEMENT 5], 0 )) AS [LEASEHOLD IMPROVEMENT 5],\n"
		     		+ "SUM(ISNULL([STAFF MACHINE & EQUIPMENTS], 0 )) AS [STAFF MACHINE & EQUIPMENTS],SUM(ISNULL([WORK-IN-PROGRESS], 0 )) AS [WORK-IN-PROGRESS] from ConsolidatedVerification\n"
		     		+ "WHERE LEVEL IS NOT NULL\n"
		     		+ "AND ZONE_CODE=?\n"
		     		+ "GROUP BY LEVEL,CLASIFICATION,BRANCH_CODE,BRANCH_NAME,ZONE_CODE,REGION_CODE,ZONE_NAME,REGION_NAME ";   
			}      

	 
//   System.out.println("======>>>>>>>ColQuery: "+ColQuery);
     java.util.ArrayList list =rep.getConsolidatedFixedAssetVerificationRecords(ColQuery,region,FromDate,ToDate,asset_Id);
     System.out.println("======>>>>>>>list size: "+list.size()+"        =====report: "+report);
     if(list.size()!=0){
   	 if(report.equalsIgnoreCase("rptMenuConsolidVerify")){
    	 SXSSFWorkbook workbook = new SXSSFWorkbook();
         Sheet sheet = workbook.createSheet("Demo");
         Row rowhead = sheet.createRow((int) 0);
         
         rowhead.createCell( 0).setCellValue("S/No.");
         rowhead.createCell( 1).setCellValue("Level");
         rowhead.createCell( 2).setCellValue("Classification");
         rowhead.createCell( 3).setCellValue("Branch Code");
         rowhead.createCell( 4).setCellValue("Branch Name");
         rowhead.createCell( 5).setCellValue("Zone Code");
         rowhead.createCell( 6).setCellValue("Region Code");
         rowhead.createCell( 7).setCellValue("Zone Name");
         rowhead.createCell( 8).setCellValue("Region Name");
         rowhead.createCell( 9).setCellValue("Computer");
		 rowhead.createCell( 10).setCellValue("Furniture & Fittings");
		 rowhead.createCell( 11).setCellValue("Machine and Equipment");
		 rowhead.createCell( 12).setCellValue("Building");
		 rowhead.createCell( 13).setCellValue("Motor Vehicle");
		 rowhead.createCell( 14).setCellValue("Staff Furniture & Fittings");
		 rowhead.createCell( 15).setCellValue("Leashold Improvement 1");
		 rowhead.createCell( 16).setCellValue("Intangible Assets-Software");
		 rowhead.createCell( 17).setCellValue("Leasehold Land");
		 rowhead.createCell( 18).setCellValue("Leasehold Improvement 2");
         rowhead.createCell( 19).setCellValue("Leasehold Improvement 3");
         rowhead.createCell( 20).setCellValue("Leasehold Improvement 4");
         rowhead.createCell( 21).setCellValue("Leasehold Improvement 5");
         rowhead.createCell( 22).setCellValue("Staff Machine & Equipments");
         rowhead.createCell( 23).setCellValue("Work-In-Progress");
         
         

     int i = 1;
     System.out.println("<<<<<<list.size(): "+list.size());
	 for(int k=0;k<list.size();k++)
     {
    	 com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);    	 
    	 
    	 	String level = newassettrans.getAssetId();
    	 	String classification = newassettrans.getCategoryName();
			 String branch_code = newassettrans.getBranchCode();
			 String branchName = newassettrans.getBranchName();
			 String zoneCode = newassettrans.getDeptCode();
			 String regionCode = newassettrans.getSbuCode();
			 String zoneName = newassettrans.getDeptName();
			 String regionName = newassettrans.getSbuName();
			 String computer = newassettrans.getGlAccount();
			 String furniture_Fittings = newassettrans.getAssetLedger();
			 String machine_Equipment = newassettrans.getOldBranchId();
			 String building = newassettrans.getOldDeptId();
			 String motorVehicle = newassettrans.getTransDate();
			 String staff_Furniture_Fittings = newassettrans.getOldAssetUser();
			 String leaseholdimprovement1 = newassettrans.getOldSection();
			 String intangibleAsset = newassettrans.getOldBranchCode();
			 String leasehold_Land = newassettrans.getOldSectionCode();
			 String leaseholdimprovement2 = newassettrans.getOldDeptCode();
			 String leaseholdimprovement3 = newassettrans.getApprovalStatus();
			 String leaseholdimprovement4 = newassettrans.getOldCategoryCode();
			 String leaseholdimprovement5 = newassettrans.getDescription();
			 String staff_Machine_Equipment = newassettrans.getNewSectionCode();
			 String work_In_Progress = newassettrans.getNewBranchCode();
			//			String vendorName = records.getCodeName("select VENDOR_NAME from am_ad_vendor where VENDOR_ID = "+vendorId+"");
			

			Row row = sheet.createRow((int) i);

			row.createCell((short) 0).setCellValue(i);
			row.createCell((short) 1).setCellValue(level);
            row.createCell((short) 2).setCellValue(classification);
            row.createCell((short) 3).setCellValue(branch_code);
            row.createCell((short) 4).setCellValue(branchName);
            row.createCell((short) 5).setCellValue(zoneCode);
            row.createCell((short) 6).setCellValue(regionCode);
            row.createCell((short) 7).setCellValue(zoneName);
            row.createCell((short) 8).setCellValue(regionName);
            row.createCell((short) 9).setCellValue(computer);
			row.createCell((short) 10).setCellValue(furniture_Fittings);
			row.createCell((short) 11).setCellValue(machine_Equipment);
			row.createCell((short) 12).setCellValue(building);
			row.createCell((short) 13).setCellValue(motorVehicle);
			row.createCell((short) 14).setCellValue(staff_Furniture_Fittings);
			row.createCell((short) 15).setCellValue(leaseholdimprovement1);
			row.createCell((short) 16).setCellValue(intangibleAsset);
            row.createCell((short) 17).setCellValue(leasehold_Land);
            row.createCell((short) 18).setCellValue(leaseholdimprovement2);
            row.createCell((short) 19).setCellValue(leaseholdimprovement3);
            row.createCell((short) 20).setCellValue(leaseholdimprovement4);
            row.createCell((short) 21).setCellValue(leaseholdimprovement5);
            row.createCell((short) 22).setCellValue(staff_Machine_Equipment);
            row.createCell((short) 23).setCellValue(work_In_Progress);

            i++;
//            System.out.println("we are here");
     }
	 System.out.println("we are here 2");
	   OutputStream stream = response.getOutputStream();
         workbook.write(stream);
         stream.close();
         workbook.close();  
         System.out.println("Data is saved in excel file.");
         
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