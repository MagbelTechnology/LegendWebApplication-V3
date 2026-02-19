package com.magbel.legend.servlet;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.legend.vao.newAssetTransaction;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Properties;

import magma.AssetRecordsBean;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class ConsolidatedFixedAssetVerificationReportExport extends HttpServlet {
   private EmailSmsServiceBus mail;
   private AssetRecordsBean ad;
   private ApprovalRecords records;

   public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      String userClass = (String)request.getSession().getAttribute("UserClass");
      String userId = (String)request.getSession().getAttribute("CurrentUser");
      
		Properties prop = new Properties();
		File file = new File("C:\\Property\\LegendPlus.properties");
		FileInputStream input = new FileInputStream(file);
		prop.load(input);

		String ThirdPartyLabel = prop.getProperty("ThirdPartyLabel");
		
      if (!userClass.equals("NULL") || userClass != null) {
         this.mail = new EmailSmsServiceBus();
         this.records = new ApprovalRecords();
         String branch_Code = request.getParameter("initiatorSOLID");
         String branch_Id = request.getParameter("branch");
         String region = request.getParameter("region");
         String FromDate = request.getParameter("FromDate");
         String ToDate = request.getParameter("ToDate");
         String asset_Id;
         String report;
         String branchIdNo;
         if (!FromDate.equals("")) {
            asset_Id = FromDate.substring(6, 10);
            System.out.println("======>yyyy: " + asset_Id);
            report = FromDate.substring(3, 5);
            System.out.println("======>mm: " + report);
            branchIdNo = FromDate.substring(0, 2);
            FromDate = asset_Id + "-" + report + "-" + branchIdNo;
         }

         System.out.println("======>FromDate: " + FromDate);
         if (!ToDate.equals("")) {
            asset_Id = ToDate.substring(6, 10);
            System.out.println("======>Tyyyy: " + asset_Id);
            report = ToDate.substring(3, 5);
            System.out.println("======>Tmm: " + report);
            branchIdNo = ToDate.substring(0, 2);
            ToDate = asset_Id + "-" + report + "-" + branchIdNo;
         }

         System.out.println("======>ToDate: " + ToDate);
         asset_Id = request.getParameter("assetId");
         report = request.getParameter("report");
         branchIdNo = this.records.getCodeName("select BRANCH from am_gb_User where USER_ID = ? ", userId);
         String userName = this.records.getCodeName("select USER_NAME from am_gb_User where USER_ID = ? ", userId);
         System.out.println("<<<<<<region: " + region);
         String branchCode = "";
         if (!branchIdNo.equals("0")) {
            branchCode = this.records.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID = ? ", branchIdNo);
         }

         System.out.println("<<<<<<branch_Code: " + branch_Code);
         String fileName = "";
         if (report.equalsIgnoreCase("rptMenuConsolidVerify")) {
            fileName = branchCode + "By" + userName + "ConsolidatedAssetVerificationReport.xlsx";
         }

         String filePath = System.getProperty("user.home") + "\\Downloads";
         System.out.println("<<<<<<filePath: " + filePath);
         File tmpDir = new File(filePath);
         boolean exists = tmpDir.exists();
         response.setIntHeader("Content-Length", -1);
         response.setContentType("application/octet-stream");
         response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

         try {
            this.ad = new AssetRecordsBean();
            Report rep = new Report();
            System.out.println("<<<<<<region: " + region + "   branchCode: " + branchCode);
            String ColQuery = "";
            if(ThirdPartyLabel.equals("ZENITH")) {            
            if (region.equals("0") && FromDate.equals("") && ToDate.equals("")) {
               System.out.println("======>>>>>>>No Selection: " + region + "    FromDate: " + FromDate + "   ToDate: " + ToDate);
               ColQuery = "select LEVEL,CLASIFICATION,BRANCH_CODE,BRANCH_NAME,ZONE_CODE,REGION_CODE,ZONE_NAME,REGION_NAME, SUM(ISNULL([BUILDING], 0 )) AS [BUILDING], SUM(ISNULL([GENERATOR - HOUSE], 0 )) AS [GENERATOR - HOUSE], SUM(ISNULL([GENERATORS], 0 )) AS [GENERATORS], SUM(ISNULL([HOUSEHOLD FURNITURE & FITTING], 0 )) AS [HOUSEHOLD FURNITURE & FITTING], "
               		+ "SUM(ISNULL([LAND], 0 )) AS [LAND],SUM(ISNULL([LEASEHOLD IMPROVEMENT], 0 )) AS [LEASEHOLD IMPROVEMENT],SUM(ISNULL([LEASEHOLD IMPROVEMENT - HOUSE], 0 )) AS [LEASEHOLD IMPROVEMENT - HOUSE],SUM(ISNULL([MOTOR VEHICLES], 0 )) AS [MOTOR VEHICLES],SUM(ISNULL([AIRCRAFT], 0 )) AS [AIRCRAFT],SUM(ISNULL([COMPUTER SOFTWARE], 0 )) AS [COMPUTER SOFTWARE],"
               		+ "SUM(ISNULL([OFFICE EQUIPMENT - COMPUTER], 0 )) AS [OFFICE EQUIPMENT - COMPUTER], SUM(ISNULL([OFFICE EQUIPMENT - OTHERS], 0 ))[OFFICE EQUIPMENT - OTHERS],SUM(ISNULL([OFFICE FURNITURE & FITTINGS], 0 )) AS [OFFICE FURNITURE & FITTINGS],"
               		+ "SUM(ISNULL([WIP-BUILDING], 0 )) AS [WIP-BUILDING],SUM(ISNULL([WIP-COMPUTER SOFTWARE], 0 )) AS [WIP-COMPUTER SOFTWARE],SUM(ISNULL([WIP-GENERATORS], 0 )) AS [WIP-GENERATORS],SUM(ISNULL([WIP-GENERATORS-HOUSE], 0 )) AS [WIP-GENERATORS-HOUSE],SUM(ISNULL([WIP-HOUSEHOLD FURNITURE AND FITTINGS], 0 )) AS [WIP-HOUSEHOLD FURNITURE AND FITTINGS],"
               		+ "SUM(ISNULL([WIP-LEASEHOLD IMPROVEMENT], 0 )) AS [WIP-LEASEHOLD IMPROVEMENT],SUM(ISNULL([WIP-LEASEHOLD IMPROVEMENT-HOUSE], 0 )) AS [WIP-LEASEHOLD IMPROVEMENT-HOUSE],SUM(ISNULL([WIP-MOTOR VEHICLES], 0 )) AS [WIP-MOTOR VEHICLES],SUM(ISNULL([WIP-OFFICE EQUIPMENT - COMPUTERS], 0 )) AS [WIP-OFFICE EQUIPMENT - COMPUTERS],"
               		+ "SUM(ISNULL([WIP-OFFICE EQUIPMENT - OTHERS], 0 )) AS [WIP-OFFICE EQUIPMENT - OTHERS],SUM(ISNULL([WIP-OFFICE FURNITURE AND FITTINGS], 0 )) AS [WIP-OFFICE FURNITURE AND FITTINGS],SUM(ISNULL([WIP-LAND], 0 )) AS [WIP-LAND]"
               		+ "from ConsolidatedVerification WHERE LEVEL IS NOT NULL GROUP BY LEVEL,CLASIFICATION,BRANCH_CODE,BRANCH_NAME,ZONE_CODE,REGION_CODE,ZONE_NAME,REGION_NAME ";

            }

//            if (region.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
//               System.out.println("======>>>>>>>Date Selected: " + region + "    FromDate: " + FromDate + "   ToDate: " + ToDate);
//               ColQuery = "select LEVEL,CLASIFICATION,BRANCH_CODE,BRANCH_NAME,ZONE_CODE,REGION_CODE,ZONE_NAME,REGION_NAME, SUM(ISNULL([BUILDING], 0 )) AS [BUILDING], SUM(ISNULL([GENERATOR - HOUSE], 0 )) AS [GENERATOR - HOUSE], SUM(ISNULL([GENERATORS], 0 )) AS [GENERATORS], SUM(ISNULL([HOUSEHOLD FURNITURE & FITTING], 0 )) AS [HOUSEHOLD FURNITURE & FITTING], "
//                  		+ "SUM(ISNULL([LAND], 0 )) AS [LAND],SUM(ISNULL([LEASEHOLD IMPROVEMENT], 0 )) AS [LEASEHOLD IMPROVEMENT],SUM(ISNULL([LEASEHOLD IMPROVEMENT - HOUSE], 0 )) AS [LEASEHOLD IMPROVEMENT - HOUSE],SUM(ISNULL([MOTOR VEHICLES], 0 )) AS [MOTOR VEHICLES],SUM(ISNULL([AIRCRAFT], 0 )) AS [AIRCRAFT],SUM(ISNULL([COMPUTER SOFTWARE], 0 )) AS [COMPUTER SOFTWARE],"
//                  		+ "SUM(ISNULL([OFFICE EQUIPMENT - COMPUTER], 0 )) AS [OFFICE EQUIPMENT - COMPUTER], SUM(ISNULL([OFFICE EQUIPMENT - OTHERS], 0 ))[OFFICE EQUIPMENT - OTHERS],SUM(ISNULL([OFFICE FURNITURE & FITTINGS], 0 )) AS [OFFICE FURNITURE & FITTINGS],"
//                  		+ "SUM(ISNULL([WIP-BUILDING], 0 )) AS [WIP-BUILDING],SUM(ISNULL([WIP-COMPUTER SOFTWARE], 0 )) AS [WIP-COMPUTER SOFTWARE],SUM(ISNULL([WIP-GENERATORS], 0 )) AS [WIP-GENERATORS],SUM(ISNULL([WIP-GENERATORS-HOUSE], 0 )) AS [WIP-GENERATORS-HOUSE],SUM(ISNULL([WIP-HOUSEHOLD FURNITURE AND FITTINGS], 0 )) AS [WIP-HOUSEHOLD FURNITURE AND FITTINGS],"
//                  		+ "SUM(ISNULL([WIP-LEASEHOLD IMPROVEMENT], 0 )) AS [WIP-LEASEHOLD IMPROVEMENT],SUM(ISNULL([WIP-LEASEHOLD IMPROVEMENT-HOUSE], 0 )) AS [WIP-LEASEHOLD IMPROVEMENT-HOUSE],SUM(ISNULL([WIP-MOTOR VEHICLES], 0 )) AS [WIP-MOTOR VEHICLES],SUM(ISNULL([WIP-OFFICE EQUIPMENT - COMPUTERS], 0 )) AS [WIP-OFFICE EQUIPMENT - COMPUTERS],"
//                  		+ "SUM(ISNULL([WIP-OFFICE EQUIPMENT - OTHERS], 0 )) AS [WIP-OFFICE EQUIPMENT - OTHERS],SUM(ISNULL([WIP-OFFICE FURNITURE AND FITTINGS], 0 )) AS [WIP-OFFICE FURNITURE AND FITTINGS],SUM(ISNULL([WIP-LAND], 0 )) AS [WIP-LAND]"
//                  		+ "from ConsolidatedVerification WHERE LEVEL IS NOT NULL GROUP BY LEVEL,CLASIFICATION,BRANCH_CODE,BRANCH_NAME,ZONE_CODE,REGION_CODE,ZONE_NAME,REGION_NAME ";
//
//            }
//
//            if (!region.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
//               System.out.println("======>>>>>>>Region and Date Selected: " + region + "    FromDate: " + FromDate + "   ToDate: " + ToDate);
//               ColQuery = "select LEVEL,CLASIFICATION,BRANCH_CODE,BRANCH_NAME,ZONE_CODE,REGION_CODE,ZONE_NAME,REGION_NAME, SUM(ISNULL([BUILDING], 0 )) AS [BUILDING], SUM(ISNULL([GENERATOR - HOUSE], 0 )) AS [GENERATOR - HOUSE], SUM(ISNULL([GENERATORS], 0 )) AS [GENERATORS], SUM(ISNULL([HOUSEHOLD FURNITURE & FITTING], 0 )) AS [HOUSEHOLD FURNITURE & FITTING], "
//                  		+ "SUM(ISNULL([LAND], 0 )) AS [LAND],SUM(ISNULL([LEASEHOLD IMPROVEMENT], 0 )) AS [LEASEHOLD IMPROVEMENT],SUM(ISNULL([LEASEHOLD IMPROVEMENT - HOUSE], 0 )) AS [LEASEHOLD IMPROVEMENT - HOUSE],SUM(ISNULL([MOTOR VEHICLES], 0 )) AS [MOTOR VEHICLES],SUM(ISNULL([AIRCRAFT], 0 )) AS [AIRCRAFT],SUM(ISNULL([COMPUTER SOFTWARE], 0 )) AS [COMPUTER SOFTWARE],"
//                  		+ "SUM(ISNULL([OFFICE EQUIPMENT - COMPUTER], 0 )) AS [OFFICE EQUIPMENT - COMPUTER], SUM(ISNULL([OFFICE EQUIPMENT - OTHERS], 0 ))[OFFICE EQUIPMENT - OTHERS],SUM(ISNULL([OFFICE FURNITURE & FITTINGS], 0 )) AS [OFFICE FURNITURE & FITTINGS],"
//                  		+ "SUM(ISNULL([WIP-BUILDING], 0 )) AS [WIP-BUILDING],SUM(ISNULL([WIP-COMPUTER SOFTWARE], 0 )) AS [WIP-COMPUTER SOFTWARE],SUM(ISNULL([WIP-GENERATORS], 0 )) AS [WIP-GENERATORS],SUM(ISNULL([WIP-GENERATORS-HOUSE], 0 )) AS [WIP-GENERATORS-HOUSE],SUM(ISNULL([WIP-HOUSEHOLD FURNITURE AND FITTINGS], 0 )) AS [WIP-HOUSEHOLD FURNITURE AND FITTINGS],"
//                  		+ "SUM(ISNULL([WIP-LEASEHOLD IMPROVEMENT], 0 )) AS [WIP-LEASEHOLD IMPROVEMENT],SUM(ISNULL([WIP-LEASEHOLD IMPROVEMENT-HOUSE], 0 )) AS [WIP-LEASEHOLD IMPROVEMENT-HOUSE],SUM(ISNULL([WIP-MOTOR VEHICLES], 0 )) AS [WIP-MOTOR VEHICLES],SUM(ISNULL([WIP-OFFICE EQUIPMENT - COMPUTERS], 0 )) AS [WIP-OFFICE EQUIPMENT - COMPUTERS],"
//                  		+ "SUM(ISNULL([WIP-OFFICE EQUIPMENT - OTHERS], 0 )) AS [WIP-OFFICE EQUIPMENT - OTHERS],SUM(ISNULL([WIP-OFFICE FURNITURE AND FITTINGS], 0 )) AS [WIP-OFFICE FURNITURE AND FITTINGS],SUM(ISNULL([WIP-LAND], 0 )) AS [WIP-LAND]"
//                  		+ "from ConsolidatedVerification WHERE LEVEL IS NOT NULL "
//        	     		+ "AND branch_code = ? "
//                  		+ "GROUP BY LEVEL,CLASIFICATION,BRANCH_CODE,BRANCH_NAME,ZONE_CODE,REGION_CODE,ZONE_NAME,REGION_NAME ";
//
//            }

            if (!region.equals("0") && FromDate.equals("") && ToDate.equals("")) {
               System.out.println("======>>>>>>>Region Selected: " + branch_Id + "    FromDate: " + FromDate + "   ToDate: " + ToDate);
               ColQuery = "select LEVEL,CLASIFICATION,BRANCH_CODE,BRANCH_NAME,ZONE_CODE,REGION_CODE,ZONE_NAME,REGION_NAME, SUM(ISNULL([BUILDING], 0 )) AS [BUILDING], SUM(ISNULL([GENERATOR - HOUSE], 0 )) AS [GENERATOR - HOUSE], SUM(ISNULL([GENERATORS], 0 )) AS [GENERATORS], SUM(ISNULL([HOUSEHOLD FURNITURE & FITTING], 0 )) AS [HOUSEHOLD FURNITURE & FITTING], "
                  		+ "SUM(ISNULL([LAND], 0 )) AS [LAND],SUM(ISNULL([LEASEHOLD IMPROVEMENT], 0 )) AS [LEASEHOLD IMPROVEMENT],SUM(ISNULL([LEASEHOLD IMPROVEMENT - HOUSE], 0 )) AS [LEASEHOLD IMPROVEMENT - HOUSE],SUM(ISNULL([MOTOR VEHICLES], 0 )) AS [MOTOR VEHICLES],SUM(ISNULL([AIRCRAFT], 0 )) AS [AIRCRAFT],SUM(ISNULL([COMPUTER SOFTWARE], 0 )) AS [COMPUTER SOFTWARE],"
                  		+ "SUM(ISNULL([OFFICE EQUIPMENT - COMPUTER], 0 )) AS [OFFICE EQUIPMENT - COMPUTER], SUM(ISNULL([OFFICE EQUIPMENT - OTHERS], 0 ))[OFFICE EQUIPMENT - OTHERS],SUM(ISNULL([OFFICE FURNITURE & FITTINGS], 0 )) AS [OFFICE FURNITURE & FITTINGS],"
                  		+ "SUM(ISNULL([WIP-BUILDING], 0 )) AS [WIP-BUILDING],SUM(ISNULL([WIP-COMPUTER SOFTWARE], 0 )) AS [WIP-COMPUTER SOFTWARE],SUM(ISNULL([WIP-GENERATORS], 0 )) AS [WIP-GENERATORS],SUM(ISNULL([WIP-GENERATORS-HOUSE], 0 )) AS [WIP-GENERATORS-HOUSE],SUM(ISNULL([WIP-HOUSEHOLD FURNITURE AND FITTINGS], 0 )) AS [WIP-HOUSEHOLD FURNITURE AND FITTINGS],"
                  		+ "SUM(ISNULL([WIP-LEASEHOLD IMPROVEMENT], 0 )) AS [WIP-LEASEHOLD IMPROVEMENT],SUM(ISNULL([WIP-LEASEHOLD IMPROVEMENT-HOUSE], 0 )) AS [WIP-LEASEHOLD IMPROVEMENT-HOUSE],SUM(ISNULL([WIP-MOTOR VEHICLES], 0 )) AS [WIP-MOTOR VEHICLES],SUM(ISNULL([WIP-OFFICE EQUIPMENT - COMPUTERS], 0 )) AS [WIP-OFFICE EQUIPMENT - COMPUTERS],"
                  		+ "SUM(ISNULL([WIP-OFFICE EQUIPMENT - OTHERS], 0 )) AS [WIP-OFFICE EQUIPMENT - OTHERS],SUM(ISNULL([WIP-OFFICE FURNITURE AND FITTINGS], 0 )) AS [WIP-OFFICE FURNITURE AND FITTINGS],SUM(ISNULL([WIP-LAND], 0 )) AS [WIP-LAND]"
                  		+ "from ConsolidatedVerification WHERE LEVEL IS NOT NULL "
        	     		+ "AND REGION_CODE = ? "
                  		+ "GROUP BY LEVEL,CLASIFICATION,BRANCH_CODE,BRANCH_NAME,ZONE_CODE,REGION_CODE,ZONE_NAME,REGION_NAME ";

            }
            }
            if(ThirdPartyLabel.equals("INTEGRIFY")) {            
            if (region.equals("0") && FromDate.equals("") && ToDate.equals("")) {
               System.out.println("======>>>>>>>No Selection: " + region + "    FromDate: " + FromDate + "   ToDate: " + ToDate);
               ColQuery = "select LEVEL,CLASIFICATION,BRANCH_CODE,BRANCH_NAME,ZONE_CODE,REGION_CODE,ZONE_NAME,REGION_NAME, SUM(ISNULL([COMPUTER], 0 )) AS [COMPUTER], SUM(ISNULL([FURNITURE & FITTINGS], 0 )) AS [FURNITURE & FITTINGS], SUM(ISNULL([MACHINE AND EQUIPMENT], 0 )) AS [MACHINE AND EQUIPMENT], SUM(ISNULL([BUILDING], 0 )) AS [BUILDING], SUM(ISNULL([MOTOR VEHICLE], 0 )) AS [MOTOR VEHICLE],SUM(ISNULL([STAFF FUNITURE & FITTINGS], 0 )) AS [STAFF FUNITURE & FITTINGS], SUM(ISNULL([LEASEHOLD IMPROVEMENT 1], 0 ))[LEASEHOLD IMPROVEMENT 1],SUM(ISNULL([INTANGIBLE ASSETS - SOFTWARE], 0 )) AS [INTANGIBLE ASSETS - SOFTWARE],SUM(ISNULL([LEASEHOLD LAND], 0 )) AS [LEASEHOLD LAND],SUM(ISNULL([LEASEHOLD IMPROVEMENT 2], 0 )) AS [LEASEHOLD IMPROVEMENT 2],SUM(ISNULL([LEASEHOLD IMPROVEMENT 3], 0 )) AS [LEASEHOLD IMPROVEMENT 3],SUM(ISNULL([LEASEHOLD IMPROVEMENT 4], 0 )) AS [LEASEHOLD IMPROVEMENT 4],SUM(ISNULL([LEASEHOLD IMPROVEMENT 5], 0 )) AS [LEASEHOLD IMPROVEMENT 5],SUM(ISNULL([STAFF MACHINE & EQUIPMENTS], 0 )) AS [STAFF MACHINE & EQUIPMENTS],SUM(ISNULL([WORK-IN-PROGRESS], 0 )) AS [WORK-IN-PROGRESS] from ConsolidatedVerification WHERE LEVEL IS NOT NULL GROUP BY LEVEL,CLASIFICATION,BRANCH_CODE,BRANCH_NAME,ZONE_CODE,REGION_CODE,ZONE_NAME,REGION_NAME ";
            }

//            if (region.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
//               System.out.println("======>>>>>>>Date Selected: " + region + "    FromDate: " + FromDate + "   ToDate: " + ToDate);
//               ColQuery = "select LEVEL,CLASIFICATION,BRANCH_CODE,BRANCH_NAME,ZONE_CODE,REGION_CODE,ZONE_NAME,REGION_NAME, SUM(ISNULL([COMPUTER], 0 )) AS [COMPUTER], SUM(ISNULL([FURNITURE & FITTINGS], 0 )) AS [FURNITURE & FITTINGS], SUM(ISNULL([MACHINE AND EQUIPMENT], 0 )) AS [MACHINE AND EQUIPMENT], SUM(ISNULL([BUILDING], 0 )) AS [BUILDING], SUM(ISNULL([MOTOR VEHICLE], 0 )) AS [MOTOR VEHICLE],SUM(ISNULL([STAFF FUNITURE & FITTINGS], 0 )) AS [STAFF FUNITURE & FITTINGS], SUM(ISNULL([LEASEHOLD IMPROVEMENT 1], 0 ))[LEASEHOLD IMPROVEMENT 1],SUM(ISNULL([INTANGIBLE ASSETS - SOFTWARE], 0 )) AS [INTANGIBLE ASSETS - SOFTWARE],SUM(ISNULL([LEASEHOLD LAND], 0 )) AS [LEASEHOLD LAND],SUM(ISNULL([LEASEHOLD IMPROVEMENT 2], 0 )) AS [LEASEHOLD IMPROVEMENT 2],SUM(ISNULL([LEASEHOLD IMPROVEMENT 3], 0 )) AS [LEASEHOLD IMPROVEMENT 3],SUM(ISNULL([LEASEHOLD IMPROVEMENT 4], 0 )) AS [LEASEHOLD IMPROVEMENT 4],SUM(ISNULL([LEASEHOLD IMPROVEMENT 5], 0 )) AS [LEASEHOLD IMPROVEMENT 5],SUM(ISNULL([STAFF MACHINE & EQUIPMENTS], 0 )) AS [STAFF MACHINE & EQUIPMENTS],SUM(ISNULL([WORK-IN-PROGRESS], 0 )) AS [WORK-IN-PROGRESS] from ConsolidatedVerification WHERE LEVEL IS NOT NULL AND PROOF_DATE BETWEEN ? AND ? GROUP BY LEVEL,CLASIFICATION,BRANCH_CODE,BRANCH_NAME,ZONE_CODE,REGION_CODE,ZONE_NAME,REGION_NAME ";
//            }
//
//            if (!region.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
//               System.out.println("======>>>>>>>Region and Date Selected: " + region + "    FromDate: " + FromDate + "   ToDate: " + ToDate);
//               ColQuery = "select LEVEL,CLASIFICATION,BRANCH_CODE,BRANCH_NAME,ZONE_CODE,REGION_CODE,ZONE_NAME,REGION_NAME, SUM(ISNULL([COMPUTER], 0 )) AS [COMPUTER], SUM(ISNULL([FURNITURE & FITTINGS], 0 )) AS [FURNITURE & FITTINGS], SUM(ISNULL([MACHINE AND EQUIPMENT], 0 )) AS [MACHINE AND EQUIPMENT], SUM(ISNULL([BUILDING], 0 )) AS [BUILDING], SUM(ISNULL([MOTOR VEHICLE], 0 )) AS [MOTOR VEHICLE],SUM(ISNULL([STAFF FUNITURE & FITTINGS], 0 )) AS [STAFF FUNITURE & FITTINGS], SUM(ISNULL([LEASEHOLD IMPROVEMENT 1], 0 ))[LEASEHOLD IMPROVEMENT 1],SUM(ISNULL([INTANGIBLE ASSETS - SOFTWARE], 0 )) AS [INTANGIBLE ASSETS - SOFTWARE],SUM(ISNULL([LEASEHOLD LAND], 0 )) AS [LEASEHOLD LAND],SUM(ISNULL([LEASEHOLD IMPROVEMENT 2], 0 )) AS [LEASEHOLD IMPROVEMENT 2],SUM(ISNULL([LEASEHOLD IMPROVEMENT 3], 0 )) AS [LEASEHOLD IMPROVEMENT 3],SUM(ISNULL([LEASEHOLD IMPROVEMENT 4], 0 )) AS [LEASEHOLD IMPROVEMENT 4],SUM(ISNULL([LEASEHOLD IMPROVEMENT 5], 0 )) AS [LEASEHOLD IMPROVEMENT 5],SUM(ISNULL([STAFF MACHINE & EQUIPMENTS], 0 )) AS [STAFF MACHINE & EQUIPMENTS],SUM(ISNULL([WORK-IN-PROGRESS], 0 )) AS [WORK-IN-PROGRESS] from ConsolidatedVerification WHERE LEVEL IS NOT NULL AND ZONE_CODE =? AND PROOF_DATE BETWEEN ? AND ? GROUP BY LEVEL,CLASIFICATION,BRANCH_CODE,BRANCH_NAME,ZONE_CODE,REGION_CODE,ZONE_NAME,REGION_NAME ";
//            }
 
            if (!region.equals("0") && FromDate.equals("") && ToDate.equals("")) {
               System.out.println("======>>>>>>>Region Selected: " + branch_Id + "    FromDate: " + FromDate + "   ToDate: " + ToDate);
               ColQuery = "select LEVEL,CLASIFICATION,BRANCH_CODE,BRANCH_NAME,ZONE_CODE,REGION_CODE,ZONE_NAME,REGION_NAME, SUM(ISNULL([COMPUTER], 0 )) AS [COMPUTER], SUM(ISNULL([FURNITURE & FITTINGS], 0 )) AS [FURNITURE & FITTINGS], SUM(ISNULL([MACHINE AND EQUIPMENT], 0 )) AS [MACHINE AND EQUIPMENT], SUM(ISNULL([BUILDING], 0 )) AS [BUILDING], SUM(ISNULL([MOTOR VEHICLE], 0 )) AS [MOTOR VEHICLE],SUM(ISNULL([STAFF FUNITURE & FITTINGS], 0 )) AS [STAFF FUNITURE & FITTINGS], SUM(ISNULL([LEASEHOLD IMPROVEMENT 1], 0 ))[LEASEHOLD IMPROVEMENT 1],SUM(ISNULL([INTANGIBLE ASSETS - SOFTWARE], 0 )) AS [INTANGIBLE ASSETS - SOFTWARE],SUM(ISNULL([LEASEHOLD LAND], 0 )) AS [LEASEHOLD LAND],SUM(ISNULL([LEASEHOLD IMPROVEMENT 2], 0 )) AS [LEASEHOLD IMPROVEMENT 2],SUM(ISNULL([LEASEHOLD IMPROVEMENT 3], 0 )) AS [LEASEHOLD IMPROVEMENT 3],SUM(ISNULL([LEASEHOLD IMPROVEMENT 4], 0 )) AS [LEASEHOLD IMPROVEMENT 4],SUM(ISNULL([LEASEHOLD IMPROVEMENT 5], 0 )) AS [LEASEHOLD IMPROVEMENT 5],SUM(ISNULL([STAFF MACHINE & EQUIPMENTS], 0 )) AS [STAFF MACHINE & EQUIPMENTS],SUM(ISNULL([WORK-IN-PROGRESS], 0 )) AS [WORK-IN-PROGRESS] from ConsolidatedVerification WHERE LEVEL IS NOT NULL AND ZONE_CODE=? GROUP BY LEVEL,CLASIFICATION,BRANCH_CODE,BRANCH_NAME,ZONE_CODE,REGION_CODE,ZONE_NAME,REGION_NAME ";
            }
            }            
            ArrayList list = rep.getConsolidatedFixedAssetVerificationRecords(ColQuery, region, FromDate, ToDate, asset_Id,ThirdPartyLabel);
            System.out.println("======>>>>>>>list size: " + list.size() + "        =====report: " + report);
            if (list.size() != 0 && report.equalsIgnoreCase("rptMenuConsolidVerify")) {
               SXSSFWorkbook workbook = new SXSSFWorkbook();
               Sheet sheet = workbook.createSheet("Demo");
               Row rowhead = sheet.createRow(0);
               rowhead.createCell(0).setCellValue("S/No.");
               rowhead.createCell(1).setCellValue("Level");
               rowhead.createCell(2).setCellValue("Classification");
               rowhead.createCell(3).setCellValue("Branch Code");
               rowhead.createCell(4).setCellValue("Branch Name");
               rowhead.createCell(5).setCellValue("Zone Code");
               rowhead.createCell(6).setCellValue("Region Code");
               rowhead.createCell(7).setCellValue("Zone Name");
               rowhead.createCell(8).setCellValue("Region Name");
               rowhead.createCell(9).setCellValue("Computer");
               rowhead.createCell(10).setCellValue("Furniture & Fittings");
               rowhead.createCell(11).setCellValue("Machine and Equipment");
               rowhead.createCell(12).setCellValue("Building");
               rowhead.createCell(13).setCellValue("Motor Vehicle");
               rowhead.createCell(14).setCellValue("Staff Furniture & Fittings");
               rowhead.createCell(15).setCellValue("Leashold Improvement 1");
               rowhead.createCell(16).setCellValue("Intangible Assets-Software");
               rowhead.createCell(17).setCellValue("Leasehold Land");
               rowhead.createCell(18).setCellValue("Leasehold Improvement 2");
               rowhead.createCell(19).setCellValue("Leasehold Improvement 3");
               rowhead.createCell(20).setCellValue("Leasehold Improvement 4");
               rowhead.createCell(21).setCellValue("Leasehold Improvement 5");
               rowhead.createCell(22).setCellValue("Staff Machine & Equipments");
               rowhead.createCell(23).setCellValue("Work-In-Progress");
               int i = 1;
               System.out.println("<<<<<<list.size(): " + list.size());

               for(int k = 0; k < list.size(); ++k) {
                  newAssetTransaction newassettrans = (newAssetTransaction)list.get(k);
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
                  Row row = sheet.createRow(i);
                  row.createCell(0).setCellValue((double)i);
                  row.createCell(1).setCellValue(level);
                  row.createCell(2).setCellValue(classification);
                  row.createCell(3).setCellValue(branch_code);
                  row.createCell(4).setCellValue(branchName);
                  row.createCell(5).setCellValue(zoneCode);
                  row.createCell(6).setCellValue(regionCode);
                  row.createCell(7).setCellValue(zoneName);
                  row.createCell(8).setCellValue(regionName);
                  row.createCell(9).setCellValue(computer);
                  row.createCell(10).setCellValue(furniture_Fittings);
                  row.createCell(11).setCellValue(machine_Equipment);
                  row.createCell(12).setCellValue(building);
                  row.createCell(13).setCellValue(motorVehicle);
                  row.createCell(14).setCellValue(staff_Furniture_Fittings);
                  row.createCell(15).setCellValue(leaseholdimprovement1);
                  row.createCell(16).setCellValue(intangibleAsset);
                  row.createCell(17).setCellValue(leasehold_Land);
                  row.createCell(18).setCellValue(leaseholdimprovement2);
                  row.createCell(19).setCellValue(leaseholdimprovement3);
                  row.createCell(20).setCellValue(leaseholdimprovement4);
                  row.createCell(21).setCellValue(leaseholdimprovement5);
                  row.createCell(22).setCellValue(staff_Machine_Equipment);
                  row.createCell(23).setCellValue(work_In_Progress);
                  ++i;
               }

               System.out.println("we are here 2");
               OutputStream stream = response.getOutputStream();
               workbook.write(stream);
               stream.close();
               workbook.close();
               System.out.println("Data is saved in excel file.");
            }
         } catch (Exception var52) {
            var52.getMessage();
         }
      }

   }

   public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      this.doPost(request, response);
   }

   public String getDate(String date) {
      String yyyy = date.substring(0, 4);
      String mm = date.substring(5, 7);
      String dd = date.substring(8, 10);
      date = dd + "/" + mm + "/" + yyyy;
      return date;
   }
}
