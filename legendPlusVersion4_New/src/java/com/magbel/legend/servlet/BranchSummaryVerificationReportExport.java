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
   
public class BranchSummaryVerificationReportExport extends HttpServlet
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
	System.out.println("======>Tyyyy: "+Tyyyy);
	String Tmm = ToDate.substring(3, 5);
	System.out.println("======>Tmm: "+Tmm);
	String Tdd = ToDate.substring(0, 2);
	ToDate = Tyyyy+"-"+Tmm+"-"+Tdd;
    }
    System.out.println("======>ToDate: "+ToDate);
    String dept_Code = request.getParameter("deptCode");
    String asset_Id = request.getParameter("assetId");
    String report = request.getParameter("report");
    String branchIdNo = records.getCodeName("select BRANCH from am_gb_User where USER_ID = ? ",userId);
    //String branchCode = request.getParameter("BRANCH_CODE");
    String userName = records.getCodeName("select USER_NAME from am_gb_User where USER_ID = ? ",userId);
    System.out.println("<<<<<<branch_Id: "+branch_Id);
    String branchCode = "";
    if(!branchIdNo.equals("0")){
    	branchCode = records.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID = ? ",branchIdNo);
    }
    System.out.println("<<<<<<branch_Code: "+branch_Code);
//    String userName = request.getParameter("userName");
    String fileName = "";
//    if(report.equalsIgnoreCase("rptMenuBCRList")){fileName = branchCode+"By"+userName+"AssetRegisterListReport.xlsx";}
//    if(report.equalsIgnoreCase("rptMenuBCLDTL")){fileName = branchCode+"By"+userName+"AssetDetailReport.xlsx";}
    if(report.equalsIgnoreCase("rptMenuBranchVerify")){fileName = branchCode+"By"+userName+"BranchSummaryVerificationReport.xlsx";}
    
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

        String departmentCode = request.getParameter("department");
     Report rep = new Report();
   System.out.println("<<<<<<branch_Id: "+branch_Id+"    departmentCode: "+departmentCode+"  branchCode: "+branchCode);
     String ColQuery = "";
     if(branch_Id.equals("0")  && departmentCode.equals("0") && FromDate.equals("") && ToDate.equals("")){
  	   System.out.println("======>>>>>>>No Selection: "+branch_Id+"   departmentCode: "+departmentCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate);
  	     ColQuery ="select LEVEL,CLASIFICATION,BRANCH_CODE,BRANCH_NAME,DEPT_CODE,DEPT_NAME, SUM(ISNULL([BUILDING], 0 )) AS [BUILDING], SUM(ISNULL([GENERATOR - HOUSE], 0 )) AS [GENERATOR - HOUSE], SUM(ISNULL([GENERATORS], 0 )) AS [GENERATORS], SUM(ISNULL([HOUSEHOLD FURNITURE & FITTING], 0 )) AS [HOUSEHOLD FURNITURE & FITTING], "
  	     		+ "SUM(ISNULL([LAND], 0 )) AS [LAND],SUM(ISNULL([LEASEHOLD IMPROVEMENT], 0 )) AS [LEASEHOLD IMPROVEMENT],SUM(ISNULL([LEASEHOLD IMPROVEMENT - HOUSE], 0 )) AS [LEASEHOLD IMPROVEMENT - HOUSE],SUM(ISNULL([MOTOR VEHICLES], 0 )) AS [MOTOR VEHICLES],SUM(ISNULL([AIRCRAFT], 0 )) AS [AIRCRAFT],SUM(ISNULL([COMPUTER SOFTWARE], 0 )) AS [COMPUTER SOFTWARE],"
  	     		+ "SUM(ISNULL([OFFICE EQUIPMENT - COMPUTER], 0 )) AS [OFFICE EQUIPMENT - COMPUTER], SUM(ISNULL([OFFICE EQUIPMENT - OTHERS], 0 ))[OFFICE EQUIPMENT - OTHERS],SUM(ISNULL([OFFICE FURNITURE & FITTINGS], 0 )) AS [OFFICE FURNITURE & FITTINGS],"
  	     		+ "SUM(ISNULL([WIP-BUILDING], 0 )) AS [WIP-BUILDING],SUM(ISNULL([WIP-COMPUTER SOFTWARE], 0 )) AS [WIP-COMPUTER SOFTWARE],SUM(ISNULL([WIP-GENERATORS], 0 )) AS [WIP-GENERATORS],SUM(ISNULL([WIP-GENERATORS-HOUSE], 0 )) AS [WIP-GENERATORS-HOUSE],SUM(ISNULL([WIP-HOUSEHOLD FURNITURE AND FITTINGS], 0 )) AS [WIP-HOUSEHOLD FURNITURE AND FITTINGS],"
  	     		+ "SUM(ISNULL([WIP-LEASEHOLD IMPROVEMENT], 0 )) AS [WIP-LEASEHOLD IMPROVEMENT],SUM(ISNULL([WIP-LEASEHOLD IMPROVEMENT-HOUSE], 0 )) AS [WIP-LEASEHOLD IMPROVEMENT-HOUSE],SUM(ISNULL([WIP-MOTOR VEHICLES], 0 )) AS [WIP-MOTOR VEHICLES],SUM(ISNULL([WIP-OFFICE EQUIPMENT - COMPUTERS], 0 )) AS [WIP-OFFICE EQUIPMENT - COMPUTERS],"
  	     		+ "SUM(ISNULL([WIP-OFFICE EQUIPMENT - OTHERS], 0 )) AS [WIP-OFFICE EQUIPMENT - OTHERS],SUM(ISNULL([WIP-OFFICE FURNITURE AND FITTINGS], 0 )) AS [WIP-OFFICE FURNITURE AND FITTINGS],SUM(ISNULL([WIP-LAND], 0 )) AS [WIP-LAND] from ConsolidatedVerification "
  	     		+ "WHERE LEVEL IS NOT NULL "
  	     		+ "GROUP BY DEPT_CODE,LEVEL,CLASIFICATION,BRANCH_CODE,BRANCH_NAME,DEPT_NAME";    
  		}
     
     
     if(branch_Id.equals("0")  && departmentCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")){
    	 System.out.println("======>>>>>>>Date Selected: "+branch_Id+"   departmentCode: "+departmentCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate);	     
	     ColQuery ="select LEVEL,CLASIFICATION,BRANCH_CODE,BRANCH_NAME,DEPT_CODE,DEPT_NAME, SUM(ISNULL([BUILDING], 0 )) AS [BUILDING], SUM(ISNULL([GENERATOR - HOUSE], 0 )) AS [GENERATOR - HOUSE], SUM(ISNULL([GENERATORS], 0 )) AS [GENERATORS], SUM(ISNULL([HOUSEHOLD FURNITURE & FITTING], 0 )) AS [HOUSEHOLD FURNITURE & FITTING], "
	     		+ "SUM(ISNULL([LAND], 0 )) AS [LAND],SUM(ISNULL([LEASEHOLD IMPROVEMENT], 0 )) AS [LEASEHOLD IMPROVEMENT],SUM(ISNULL([LEASEHOLD IMPROVEMENT - HOUSE], 0 )) AS [LEASEHOLD IMPROVEMENT - HOUSE],SUM(ISNULL([MOTOR VEHICLES], 0 )) AS [MOTOR VEHICLES],SUM(ISNULL([AIRCRAFT], 0 )) AS [AIRCRAFT],SUM(ISNULL([COMPUTER SOFTWARE], 0 )) AS [COMPUTER SOFTWARE],"
	     		+ "SUM(ISNULL([OFFICE EQUIPMENT - COMPUTER], 0 )) AS [OFFICE EQUIPMENT - COMPUTER], SUM(ISNULL([OFFICE EQUIPMENT - OTHERS], 0 ))[OFFICE EQUIPMENT - OTHERS],SUM(ISNULL([OFFICE FURNITURE & FITTINGS], 0 )) AS [OFFICE FURNITURE & FITTINGS],"
	     		+ "SUM(ISNULL([WIP-BUILDING], 0 )) AS [WIP-BUILDING],SUM(ISNULL([WIP-COMPUTER SOFTWARE], 0 )) AS [WIP-COMPUTER SOFTWARE],SUM(ISNULL([WIP-GENERATORS], 0 )) AS [WIP-GENERATORS],SUM(ISNULL([WIP-GENERATORS-HOUSE], 0 )) AS [WIP-GENERATORS-HOUSE],SUM(ISNULL([WIP-HOUSEHOLD FURNITURE AND FITTINGS], 0 )) AS [WIP-HOUSEHOLD FURNITURE AND FITTINGS],"
	     		+ "SUM(ISNULL([WIP-LEASEHOLD IMPROVEMENT], 0 )) AS [WIP-LEASEHOLD IMPROVEMENT],SUM(ISNULL([WIP-LEASEHOLD IMPROVEMENT-HOUSE], 0 )) AS [WIP-LEASEHOLD IMPROVEMENT-HOUSE],SUM(ISNULL([WIP-MOTOR VEHICLES], 0 )) AS [WIP-MOTOR VEHICLES],SUM(ISNULL([WIP-OFFICE EQUIPMENT - COMPUTERS], 0 )) AS [WIP-OFFICE EQUIPMENT - COMPUTERS],"
	     		+ "SUM(ISNULL([WIP-OFFICE EQUIPMENT - OTHERS], 0 )) AS [WIP-OFFICE EQUIPMENT - OTHERS],SUM(ISNULL([WIP-OFFICE FURNITURE AND FITTINGS], 0 )) AS [WIP-OFFICE FURNITURE AND FITTINGS],SUM(ISNULL([WIP-LAND], 0 )) AS [WIP-LAND] from ConsolidatedVerification "
	     		+ "WHERE LEVEL IS NOT NULL "
	     		+ "AND PROOF_DATE  BETWEEN ? and ?"
	     		+ "GROUP BY DEPT_CODE,LEVEL,CLASIFICATION,BRANCH_CODE,BRANCH_NAME,DEPT_NAME";
	}      
	 if(!branch_Id.equals("0")  && departmentCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")){	   
	   System.out.println("======>>>>>>>Branch and Date Selected: "+branch_Id+"   departmentCode: "+departmentCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate);
	     ColQuery ="select LEVEL,CLASIFICATION,BRANCH_CODE,BRANCH_NAME,DEPT_CODE,DEPT_NAME, SUM(ISNULL([BUILDING], 0 )) AS [BUILDING], SUM(ISNULL([GENERATOR - HOUSE], 0 )) AS [GENERATOR - HOUSE], SUM(ISNULL([GENERATORS], 0 )) AS [GENERATORS], SUM(ISNULL([HOUSEHOLD FURNITURE & FITTING], 0 )) AS [HOUSEHOLD FURNITURE & FITTING], "
	     		+ "SUM(ISNULL([LAND], 0 )) AS [LAND],SUM(ISNULL([LEASEHOLD IMPROVEMENT], 0 )) AS [LEASEHOLD IMPROVEMENT],SUM(ISNULL([LEASEHOLD IMPROVEMENT - HOUSE], 0 )) AS [LEASEHOLD IMPROVEMENT - HOUSE],SUM(ISNULL([MOTOR VEHICLES], 0 )) AS [MOTOR VEHICLES],SUM(ISNULL([AIRCRAFT], 0 )) AS [AIRCRAFT],SUM(ISNULL([COMPUTER SOFTWARE], 0 )) AS [COMPUTER SOFTWARE],"
	     		+ "SUM(ISNULL([OFFICE EQUIPMENT - COMPUTER], 0 )) AS [OFFICE EQUIPMENT - COMPUTER], SUM(ISNULL([OFFICE EQUIPMENT - OTHERS], 0 ))[OFFICE EQUIPMENT - OTHERS],SUM(ISNULL([OFFICE FURNITURE & FITTINGS], 0 )) AS [OFFICE FURNITURE & FITTINGS],"
	     		+ "SUM(ISNULL([WIP-BUILDING], 0 )) AS [WIP-BUILDING],SUM(ISNULL([WIP-COMPUTER SOFTWARE], 0 )) AS [WIP-COMPUTER SOFTWARE],SUM(ISNULL([WIP-GENERATORS], 0 )) AS [WIP-GENERATORS],SUM(ISNULL([WIP-GENERATORS-HOUSE], 0 )) AS [WIP-GENERATORS-HOUSE],SUM(ISNULL([WIP-HOUSEHOLD FURNITURE AND FITTINGS], 0 )) AS [WIP-HOUSEHOLD FURNITURE AND FITTINGS],"
	     		+ "SUM(ISNULL([WIP-LEASEHOLD IMPROVEMENT], 0 )) AS [WIP-LEASEHOLD IMPROVEMENT],SUM(ISNULL([WIP-LEASEHOLD IMPROVEMENT-HOUSE], 0 )) AS [WIP-LEASEHOLD IMPROVEMENT-HOUSE],SUM(ISNULL([WIP-MOTOR VEHICLES], 0 )) AS [WIP-MOTOR VEHICLES],SUM(ISNULL([WIP-OFFICE EQUIPMENT - COMPUTERS], 0 )) AS [WIP-OFFICE EQUIPMENT - COMPUTERS],"
	     		+ "SUM(ISNULL([WIP-OFFICE EQUIPMENT - OTHERS], 0 )) AS [WIP-OFFICE EQUIPMENT - OTHERS],SUM(ISNULL([WIP-OFFICE FURNITURE AND FITTINGS], 0 )) AS [WIP-OFFICE FURNITURE AND FITTINGS],SUM(ISNULL([WIP-LAND], 0 )) AS [WIP-LAND] from ConsolidatedVerification "
	     		+ "WHERE LEVEL IS NOT NULL "
	     		+ "AND branch_code = ?"
	     		+ "AND PROOF_DATE  BETWEEN ? and ?"
	     		+ "GROUP BY DEPT_CODE,LEVEL,CLASIFICATION,BRANCH_CODE,BRANCH_NAME,DEPT_NAME";   
		}  
	 
	 if(!branch_Id.equals("0")  && departmentCode.equals("0") && FromDate.equals("") && ToDate.equals("")){	   
		   System.out.println("======>>>>>>>Branch Selected: "+branch_Id+"   departmentCode: "+departmentCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate);
		     ColQuery ="select LEVEL,CLASIFICATION,BRANCH_CODE,BRANCH_NAME,DEPT_CODE,DEPT_NAME, SUM(ISNULL([BUILDING], 0 )) AS [BUILDING], SUM(ISNULL([GENERATOR - HOUSE], 0 )) AS [GENERATOR - HOUSE], SUM(ISNULL([GENERATORS], 0 )) AS [GENERATORS], SUM(ISNULL([HOUSEHOLD FURNITURE & FITTING], 0 )) AS [HOUSEHOLD FURNITURE & FITTING], "
		     		+ "SUM(ISNULL([LAND], 0 )) AS [LAND],SUM(ISNULL([LEASEHOLD IMPROVEMENT], 0 )) AS [LEASEHOLD IMPROVEMENT],SUM(ISNULL([LEASEHOLD IMPROVEMENT - HOUSE], 0 )) AS [LEASEHOLD IMPROVEMENT - HOUSE],SUM(ISNULL([MOTOR VEHICLES], 0 )) AS [MOTOR VEHICLES],SUM(ISNULL([AIRCRAFT], 0 )) AS [AIRCRAFT],SUM(ISNULL([COMPUTER SOFTWARE], 0 )) AS [COMPUTER SOFTWARE],"
		     		+ "SUM(ISNULL([OFFICE EQUIPMENT - COMPUTER], 0 )) AS [OFFICE EQUIPMENT - COMPUTER], SUM(ISNULL([OFFICE EQUIPMENT - OTHERS], 0 ))[OFFICE EQUIPMENT - OTHERS],SUM(ISNULL([OFFICE FURNITURE & FITTINGS], 0 )) AS [OFFICE FURNITURE & FITTINGS],"
		     		+ "SUM(ISNULL([WIP-BUILDING], 0 )) AS [WIP-BUILDING],SUM(ISNULL([WIP-COMPUTER SOFTWARE], 0 )) AS [WIP-COMPUTER SOFTWARE],SUM(ISNULL([WIP-GENERATORS], 0 )) AS [WIP-GENERATORS],SUM(ISNULL([WIP-GENERATORS-HOUSE], 0 )) AS [WIP-GENERATORS-HOUSE],SUM(ISNULL([WIP-HOUSEHOLD FURNITURE AND FITTINGS], 0 )) AS [WIP-HOUSEHOLD FURNITURE AND FITTINGS],"
		     		+ "SUM(ISNULL([WIP-LEASEHOLD IMPROVEMENT], 0 )) AS [WIP-LEASEHOLD IMPROVEMENT],SUM(ISNULL([WIP-LEASEHOLD IMPROVEMENT-HOUSE], 0 )) AS [WIP-LEASEHOLD IMPROVEMENT-HOUSE],SUM(ISNULL([WIP-MOTOR VEHICLES], 0 )) AS [WIP-MOTOR VEHICLES],SUM(ISNULL([WIP-OFFICE EQUIPMENT - COMPUTERS], 0 )) AS [WIP-OFFICE EQUIPMENT - COMPUTERS],"
		     		+ "SUM(ISNULL([WIP-OFFICE EQUIPMENT - OTHERS], 0 )) AS [WIP-OFFICE EQUIPMENT - OTHERS],SUM(ISNULL([WIP-OFFICE FURNITURE AND FITTINGS], 0 )) AS [WIP-OFFICE FURNITURE AND FITTINGS],SUM(ISNULL([WIP-LAND], 0 )) AS [WIP-LAND] from ConsolidatedVerification "
		     		+ "WHERE LEVEL IS NOT NULL "
		     		+ "AND branch_code = ?"
		     		+ "GROUP BY DEPT_CODE,LEVEL,CLASIFICATION,BRANCH_CODE,BRANCH_NAME,DEPT_NAME";   
			}      

	 if(branch_Id.equals("0")  && !departmentCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")){	   
	   System.out.println("======>>>>>>>Department and Date Selected: "+branch_Id+"   departmentCode: "+departmentCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate);
	     ColQuery ="select LEVEL,CLASIFICATION,BRANCH_CODE,BRANCH_NAME,DEPT_CODE,DEPT_NAME, SUM(ISNULL([BUILDING], 0 )) AS [BUILDING], SUM(ISNULL([GENERATOR - HOUSE], 0 )) AS [GENERATOR - HOUSE], SUM(ISNULL([GENERATORS], 0 )) AS [GENERATORS], SUM(ISNULL([HOUSEHOLD FURNITURE & FITTING], 0 )) AS [HOUSEHOLD FURNITURE & FITTING], "
	     		+ "SUM(ISNULL([LAND], 0 )) AS [LAND],SUM(ISNULL([LEASEHOLD IMPROVEMENT], 0 )) AS [LEASEHOLD IMPROVEMENT],SUM(ISNULL([LEASEHOLD IMPROVEMENT - HOUSE], 0 )) AS [LEASEHOLD IMPROVEMENT - HOUSE],SUM(ISNULL([MOTOR VEHICLES], 0 )) AS [MOTOR VEHICLES],SUM(ISNULL([AIRCRAFT], 0 )) AS [AIRCRAFT],SUM(ISNULL([COMPUTER SOFTWARE], 0 )) AS [COMPUTER SOFTWARE],"
	     		+ "SUM(ISNULL([OFFICE EQUIPMENT - COMPUTER], 0 )) AS [OFFICE EQUIPMENT - COMPUTER], SUM(ISNULL([OFFICE EQUIPMENT - OTHERS], 0 ))[OFFICE EQUIPMENT - OTHERS],SUM(ISNULL([OFFICE FURNITURE & FITTINGS], 0 )) AS [OFFICE FURNITURE & FITTINGS],"
	     		+ "SUM(ISNULL([WIP-BUILDING], 0 )) AS [WIP-BUILDING],SUM(ISNULL([WIP-COMPUTER SOFTWARE], 0 )) AS [WIP-COMPUTER SOFTWARE],SUM(ISNULL([WIP-GENERATORS], 0 )) AS [WIP-GENERATORS],SUM(ISNULL([WIP-GENERATORS-HOUSE], 0 )) AS [WIP-GENERATORS-HOUSE],SUM(ISNULL([WIP-HOUSEHOLD FURNITURE AND FITTINGS], 0 )) AS [WIP-HOUSEHOLD FURNITURE AND FITTINGS],"
	     		+ "SUM(ISNULL([WIP-LEASEHOLD IMPROVEMENT], 0 )) AS [WIP-LEASEHOLD IMPROVEMENT],SUM(ISNULL([WIP-LEASEHOLD IMPROVEMENT-HOUSE], 0 )) AS [WIP-LEASEHOLD IMPROVEMENT-HOUSE],SUM(ISNULL([WIP-MOTOR VEHICLES], 0 )) AS [WIP-MOTOR VEHICLES],SUM(ISNULL([WIP-OFFICE EQUIPMENT - COMPUTERS], 0 )) AS [WIP-OFFICE EQUIPMENT - COMPUTERS],"
	     		+ "SUM(ISNULL([WIP-OFFICE EQUIPMENT - OTHERS], 0 )) AS [WIP-OFFICE EQUIPMENT - OTHERS],SUM(ISNULL([WIP-OFFICE FURNITURE AND FITTINGS], 0 )) AS [WIP-OFFICE FURNITURE AND FITTINGS],SUM(ISNULL([WIP-LAND], 0 )) AS [WIP-LAND] from ConsolidatedVerification "
	     		+ "WHERE LEVEL IS NOT NULL "
	     		+ "AND Dept_Code = ?"
	     		+ "AND PROOF_DATE  BETWEEN ? and ?"
	     		+ "GROUP BY DEPT_CODE,LEVEL,CLASIFICATION,BRANCH_CODE,BRANCH_NAME,DEPT_NAME";    
		}      
	 
	 if(branch_Id.equals("0")  && !departmentCode.equals("0") && FromDate.equals("") && ToDate.equals("")){	   
		   System.out.println("======>>>>>>>Department Selected: "+branch_Id+"   departmentCode: "+departmentCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate);
		     ColQuery ="select LEVEL,CLASIFICATION,BRANCH_CODE,BRANCH_NAME,DEPT_CODE,DEPT_NAME, SUM(ISNULL([BUILDING], 0 )) AS [BUILDING], SUM(ISNULL([GENERATOR - HOUSE], 0 )) AS [GENERATOR - HOUSE], SUM(ISNULL([GENERATORS], 0 )) AS [GENERATORS], SUM(ISNULL([HOUSEHOLD FURNITURE & FITTING], 0 )) AS [HOUSEHOLD FURNITURE & FITTING], "
		     		+ "SUM(ISNULL([LAND], 0 )) AS [LAND],SUM(ISNULL([LEASEHOLD IMPROVEMENT], 0 )) AS [LEASEHOLD IMPROVEMENT],SUM(ISNULL([LEASEHOLD IMPROVEMENT - HOUSE], 0 )) AS [LEASEHOLD IMPROVEMENT - HOUSE],SUM(ISNULL([MOTOR VEHICLES], 0 )) AS [MOTOR VEHICLES],SUM(ISNULL([AIRCRAFT], 0 )) AS [AIRCRAFT],SUM(ISNULL([COMPUTER SOFTWARE], 0 )) AS [COMPUTER SOFTWARE],"
		     		+ "SUM(ISNULL([OFFICE EQUIPMENT - COMPUTER], 0 )) AS [OFFICE EQUIPMENT - COMPUTER], SUM(ISNULL([OFFICE EQUIPMENT - OTHERS], 0 ))[OFFICE EQUIPMENT - OTHERS],SUM(ISNULL([OFFICE FURNITURE & FITTINGS], 0 )) AS [OFFICE FURNITURE & FITTINGS],"
		     		+ "SUM(ISNULL([WIP-BUILDING], 0 )) AS [WIP-BUILDING],SUM(ISNULL([WIP-COMPUTER SOFTWARE], 0 )) AS [WIP-COMPUTER SOFTWARE],SUM(ISNULL([WIP-GENERATORS], 0 )) AS [WIP-GENERATORS],SUM(ISNULL([WIP-GENERATORS-HOUSE], 0 )) AS [WIP-GENERATORS-HOUSE],SUM(ISNULL([WIP-HOUSEHOLD FURNITURE AND FITTINGS], 0 )) AS [WIP-HOUSEHOLD FURNITURE AND FITTINGS],"
		     		+ "SUM(ISNULL([WIP-LEASEHOLD IMPROVEMENT], 0 )) AS [WIP-LEASEHOLD IMPROVEMENT],SUM(ISNULL([WIP-LEASEHOLD IMPROVEMENT-HOUSE], 0 )) AS [WIP-LEASEHOLD IMPROVEMENT-HOUSE],SUM(ISNULL([WIP-MOTOR VEHICLES], 0 )) AS [WIP-MOTOR VEHICLES],SUM(ISNULL([WIP-OFFICE EQUIPMENT - COMPUTERS], 0 )) AS [WIP-OFFICE EQUIPMENT - COMPUTERS],"
		     		+ "SUM(ISNULL([WIP-OFFICE EQUIPMENT - OTHERS], 0 )) AS [WIP-OFFICE EQUIPMENT - OTHERS],SUM(ISNULL([WIP-OFFICE FURNITURE AND FITTINGS], 0 )) AS [WIP-OFFICE FURNITURE AND FITTINGS],SUM(ISNULL([WIP-LAND], 0 )) AS [WIP-LAND] from ConsolidatedVerification "
		     		+ "WHERE LEVEL IS NOT NULL "
		     		+ "AND Dept_Code = ?"
		     		+ "GROUP BY DEPT_CODE,LEVEL,CLASIFICATION,BRANCH_CODE,BRANCH_NAME,DEPT_NAME";    
			}  
	 
	 if(!branch_Id.equals("0")  && !departmentCode.equals("0") && FromDate.equals("") && ToDate.equals("")){	   
		   System.out.println("======>>>>>>>Branch and Department Selected: "+branch_Id+"   departmentCode: "+departmentCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate);
		     ColQuery ="select LEVEL,CLASIFICATION,BRANCH_CODE,BRANCH_NAME,DEPT_CODE,DEPT_NAME, SUM(ISNULL([BUILDING], 0 )) AS [BUILDING], SUM(ISNULL([GENERATOR - HOUSE], 0 )) AS [GENERATOR - HOUSE], SUM(ISNULL([GENERATORS], 0 )) AS [GENERATORS], SUM(ISNULL([HOUSEHOLD FURNITURE & FITTING], 0 )) AS [HOUSEHOLD FURNITURE & FITTING], "
		     		+ "SUM(ISNULL([LAND], 0 )) AS [LAND],SUM(ISNULL([LEASEHOLD IMPROVEMENT], 0 )) AS [LEASEHOLD IMPROVEMENT],SUM(ISNULL([LEASEHOLD IMPROVEMENT - HOUSE], 0 )) AS [LEASEHOLD IMPROVEMENT - HOUSE],SUM(ISNULL([MOTOR VEHICLES], 0 )) AS [MOTOR VEHICLES],SUM(ISNULL([AIRCRAFT], 0 )) AS [AIRCRAFT],SUM(ISNULL([COMPUTER SOFTWARE], 0 )) AS [COMPUTER SOFTWARE],"
		     		+ "SUM(ISNULL([OFFICE EQUIPMENT - COMPUTER], 0 )) AS [OFFICE EQUIPMENT - COMPUTER], SUM(ISNULL([OFFICE EQUIPMENT - OTHERS], 0 ))[OFFICE EQUIPMENT - OTHERS],SUM(ISNULL([OFFICE FURNITURE & FITTINGS], 0 )) AS [OFFICE FURNITURE & FITTINGS],"
		     		+ "SUM(ISNULL([WIP-BUILDING], 0 )) AS [WIP-BUILDING],SUM(ISNULL([WIP-COMPUTER SOFTWARE], 0 )) AS [WIP-COMPUTER SOFTWARE],SUM(ISNULL([WIP-GENERATORS], 0 )) AS [WIP-GENERATORS],SUM(ISNULL([WIP-GENERATORS-HOUSE], 0 )) AS [WIP-GENERATORS-HOUSE],SUM(ISNULL([WIP-HOUSEHOLD FURNITURE AND FITTINGS], 0 )) AS [WIP-HOUSEHOLD FURNITURE AND FITTINGS],"
		     		+ "SUM(ISNULL([WIP-LEASEHOLD IMPROVEMENT], 0 )) AS [WIP-LEASEHOLD IMPROVEMENT],SUM(ISNULL([WIP-LEASEHOLD IMPROVEMENT-HOUSE], 0 )) AS [WIP-LEASEHOLD IMPROVEMENT-HOUSE],SUM(ISNULL([WIP-MOTOR VEHICLES], 0 )) AS [WIP-MOTOR VEHICLES],SUM(ISNULL([WIP-OFFICE EQUIPMENT - COMPUTERS], 0 )) AS [WIP-OFFICE EQUIPMENT - COMPUTERS],"
		     		+ "SUM(ISNULL([WIP-OFFICE EQUIPMENT - OTHERS], 0 )) AS [WIP-OFFICE EQUIPMENT - OTHERS],SUM(ISNULL([WIP-OFFICE FURNITURE AND FITTINGS], 0 )) AS [WIP-OFFICE FURNITURE AND FITTINGS],SUM(ISNULL([WIP-LAND], 0 )) AS [WIP-LAND] from ConsolidatedVerification "
		     		+ "WHERE LEVEL IS NOT NULL "
		     		+ "AND Dept_Code = ?"
		     		+ "AND branch_code = ?"
		     		+ "GROUP BY DEPT_CODE,LEVEL,CLASIFICATION,BRANCH_CODE,BRANCH_NAME,DEPT_NAME";    
			}  
	 
   if(!branch_Id.equals("0")  && !departmentCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")){
	   System.out.println("======>>>>>>>Branch and Department and Date Selected: "+branch_Id+"   departmentCode: "+departmentCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate);
	     ColQuery ="select LEVEL,CLASIFICATION,BRANCH_CODE,BRANCH_NAME,DEPT_CODE,DEPT_NAME, SUM(ISNULL([BUILDING], 0 )) AS [BUILDING], SUM(ISNULL([GENERATOR - HOUSE], 0 )) AS [GENERATOR - HOUSE], SUM(ISNULL([GENERATORS], 0 )) AS [GENERATORS], SUM(ISNULL([HOUSEHOLD FURNITURE & FITTING], 0 )) AS [HOUSEHOLD FURNITURE & FITTING], "
	     		+ "SUM(ISNULL([LAND], 0 )) AS [LAND],SUM(ISNULL([LEASEHOLD IMPROVEMENT], 0 )) AS [LEASEHOLD IMPROVEMENT],SUM(ISNULL([LEASEHOLD IMPROVEMENT - HOUSE], 0 )) AS [LEASEHOLD IMPROVEMENT - HOUSE],SUM(ISNULL([MOTOR VEHICLES], 0 )) AS [MOTOR VEHICLES],SUM(ISNULL([AIRCRAFT], 0 )) AS [AIRCRAFT],SUM(ISNULL([COMPUTER SOFTWARE], 0 )) AS [COMPUTER SOFTWARE],"
	     		+ "SUM(ISNULL([OFFICE EQUIPMENT - COMPUTER], 0 )) AS [OFFICE EQUIPMENT - COMPUTER], SUM(ISNULL([OFFICE EQUIPMENT - OTHERS], 0 ))[OFFICE EQUIPMENT - OTHERS],SUM(ISNULL([OFFICE FURNITURE & FITTINGS], 0 )) AS [OFFICE FURNITURE & FITTINGS],"
	     		+ "SUM(ISNULL([WIP-BUILDING], 0 )) AS [WIP-BUILDING],SUM(ISNULL([WIP-COMPUTER SOFTWARE], 0 )) AS [WIP-COMPUTER SOFTWARE],SUM(ISNULL([WIP-GENERATORS], 0 )) AS [WIP-GENERATORS],SUM(ISNULL([WIP-GENERATORS-HOUSE], 0 )) AS [WIP-GENERATORS-HOUSE],SUM(ISNULL([WIP-HOUSEHOLD FURNITURE AND FITTINGS], 0 )) AS [WIP-HOUSEHOLD FURNITURE AND FITTINGS],"
	     		+ "SUM(ISNULL([WIP-LEASEHOLD IMPROVEMENT], 0 )) AS [WIP-LEASEHOLD IMPROVEMENT],SUM(ISNULL([WIP-LEASEHOLD IMPROVEMENT-HOUSE], 0 )) AS [WIP-LEASEHOLD IMPROVEMENT-HOUSE],SUM(ISNULL([WIP-MOTOR VEHICLES], 0 )) AS [WIP-MOTOR VEHICLES],SUM(ISNULL([WIP-OFFICE EQUIPMENT - COMPUTERS], 0 )) AS [WIP-OFFICE EQUIPMENT - COMPUTERS],"
	     		+ "SUM(ISNULL([WIP-OFFICE EQUIPMENT - OTHERS], 0 )) AS [WIP-OFFICE EQUIPMENT - OTHERS],SUM(ISNULL([WIP-OFFICE FURNITURE AND FITTINGS], 0 )) AS [WIP-OFFICE FURNITURE AND FITTINGS],SUM(ISNULL([WIP-LAND], 0 )) AS [WIP-LAND] from ConsolidatedVerification "
	     		+ "WHERE LEVEL IS NOT NULL "
	     		+ "AND Dept_Code = ?"
	     		+ "AND branch_code = ?"
	     		+ "AND PROOF_DATE  BETWEEN ? and ?"
	     		+ "GROUP BY DEPT_CODE,LEVEL,CLASIFICATION,BRANCH_CODE,BRANCH_NAME,DEPT_NAME";    
		}      
   System.out.println("======>>>>>>>ColQuery: "+ColQuery);
     java.util.ArrayList list =rep.getBranchSummaryVerificationRecords(ColQuery,branch_Id,departmentCode,FromDate,ToDate,asset_Id);
     System.out.println("======>>>>>>>list size: "+list.size()+"        =====report: "+report);
     if(list.size()!=0){
   	 if(report.equalsIgnoreCase("rptMenuBranchVerify")){
    	 SXSSFWorkbook workbook = new SXSSFWorkbook();
         Sheet sheet = workbook.createSheet("Demo");
         Row rowhead = sheet.createRow((int) 0);
         
         rowhead.createCell( 0).setCellValue("S/No.");
         rowhead.createCell( 1).setCellValue("Level");
         rowhead.createCell( 2).setCellValue("Clasification");
         rowhead.createCell( 3).setCellValue("Branch Code");
         rowhead.createCell( 4).setCellValue("Branch Name");
         rowhead.createCell( 5).setCellValue("Dept Code");
         rowhead.createCell( 6).setCellValue("Dept Name");
         rowhead.createCell( 7).setCellValue("OFFICE EQUIPMENT - COMPUTER");
         rowhead.createCell( 8).setCellValue("HOUSEHOLD FURNITURE & FITTING");
         rowhead.createCell( 9).setCellValue("OFFICE EQUIPMENT - OTHERS");
		 rowhead.createCell( 10).setCellValue("BUILDING");
		 rowhead.createCell( 11).setCellValue("MOTOR VEHICLES");
		 rowhead.createCell( 12).setCellValue("OFFICE FURNITURE & FITTINGS");
		 rowhead.createCell( 13).setCellValue("LEASEHOLD IMPROVEMENT - HOUSE");
		 rowhead.createCell( 14).setCellValue("COMPUTER SOFTWARE");
		 rowhead.createCell( 15).setCellValue("LEASEHOLD IMPROVEMENT");
		 rowhead.createCell( 16).setCellValue("WIP-LEASEHOLD IMPROVEMENT");
		 rowhead.createCell( 17).setCellValue("WIP-LEASEHOLD IMPROVEMENT-HOUSE");
		 rowhead.createCell( 18).setCellValue("WIP-HOUSEHOLD FURNITURE AND FITTINGS");
         rowhead.createCell( 19).setCellValue("WIP-OFFICE FURNITURE AND FITTINGS");
         rowhead.createCell( 20).setCellValue("WIP-OFFICE EQUIPMENT - OTHERS");
         rowhead.createCell( 21).setCellValue("WIP-BUILDING");
         rowhead.createCell( 22).setCellValue("AIRCRAFT");
         rowhead.createCell( 23).setCellValue("LAND");
         rowhead.createCell( 24).setCellValue("WIP-LAND");
         rowhead.createCell( 25).setCellValue("WIP-COMPUTER SOFTWARE");
         rowhead.createCell( 26).setCellValue("WIP-GENERATORS");
         rowhead.createCell( 27).setCellValue("WIP-GENERATORS-HOUSE");
         rowhead.createCell( 28).setCellValue("WIP-MOTOR VEHICLES");
         rowhead.createCell( 29).setCellValue("WIP-OFFICE EQUIPMENT - COMPUTERS");   
			
     int i = 1;
     System.out.println("<<<<<<list.size(): "+list.size());
	 for(int k=0;k<list.size();k++)
     {
    	 com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);    	 
			String level =  newassettrans.getAssetId();
			String clasification = newassettrans.getCategoryName() != null ? newassettrans.getCategoryName() : "";
			String branch_code = newassettrans.getBranchCode();
			String branchName = newassettrans.getBranchName() != null ? newassettrans.getBranchName() : "";
			String deptCode = newassettrans.getDeptCode();
			String deptName = newassettrans.getDeptName() != null ? newassettrans.getDeptName() : "";
			String computer = newassettrans.getGlAccount();
			String furniture_and_Fitting = newassettrans.getAssetLedger();
			String machine_and_equipment = newassettrans.getOldBranchId();
			String building = newassettrans.getOldDeptId();
			String motorVehicle = newassettrans.getTransDate();
			String staff_Furniture_and_Fitting = newassettrans.getOldAssetUser();
			String leaseholdImprovement1 = newassettrans.getOldSection();
			String intangibleAsset = newassettrans.getOldBranchCode();
			String leaseholdLand = newassettrans.getOldSectionCode();
			String leaseholdImprovement2 = newassettrans.getOldDeptCode();
			String leaseholdImprovement3 = newassettrans.getApprovalStatus();
			String leaseholdImprovement4 = newassettrans.getOldCategoryCode();
			String leaseholdImprovement5 = newassettrans.getDescription();
			String staff_Machine_and_Equipments = newassettrans.getNewSectionCode();
			String work_In_Progress = newassettrans.getNewBranchCode();
			String airCraft = newassettrans.getAirCraft();
			String land = newassettrans.getLand(); 
			String wipLand = newassettrans.getWipLand();
			String wipComputerSoftware = newassettrans.getWipComputerSoftware();
			String wipGenerators = newassettrans.getWipGenerators();
			String wipGeneratorsHouse = newassettrans.getWipGeneratorsHouse();
			String wipMotorVehicles = newassettrans.getWipMotorVehicles();
			String wipOfficeEquipmentComputers = newassettrans.getWipOfficeEquipmentComputers();

			//			String vendorName = records.getCodeName("select VENDOR_NAME from am_ad_vendor where VENDOR_ID = "+vendorId+"");
			

			Row row = sheet.createRow((int) i);

			row.createCell((short) 0).setCellValue(i);
			row.createCell((short) 1).setCellValue(level);
            row.createCell((short) 2).setCellValue(clasification);
            row.createCell((short) 3).setCellValue(branch_code);
            row.createCell((short) 4).setCellValue(branchName);
            row.createCell((short) 5).setCellValue(deptCode);
            row.createCell((short) 6).setCellValue(deptName);
            row.createCell((short) 7).setCellValue(computer);
            row.createCell((short) 8).setCellValue(furniture_and_Fitting);
            row.createCell((short) 9).setCellValue(machine_and_equipment);
			row.createCell((short) 10).setCellValue(building);
			row.createCell((short) 11).setCellValue(motorVehicle);
			row.createCell((short) 12).setCellValue(staff_Furniture_and_Fitting);
			row.createCell((short) 13).setCellValue(leaseholdImprovement1);
			row.createCell((short) 14).setCellValue(intangibleAsset);
			row.createCell((short) 15).setCellValue(leaseholdLand);
			row.createCell((short) 16).setCellValue(leaseholdImprovement2);
            row.createCell((short) 17).setCellValue(leaseholdImprovement3);
            row.createCell((short) 18).setCellValue(leaseholdImprovement4);
            row.createCell((short) 19).setCellValue(leaseholdImprovement5);
            row.createCell((short) 20).setCellValue(staff_Machine_and_Equipments);
            row.createCell((short) 21).setCellValue(work_In_Progress);
            row.createCell((short) 22).setCellValue(airCraft);
            row.createCell((short) 23).setCellValue(land);
            row.createCell((short) 24).setCellValue(wipLand);
            row.createCell((short) 25).setCellValue(wipComputerSoftware);
            row.createCell((short) 26).setCellValue(wipGenerators);
            row.createCell((short) 27).setCellValue(wipGeneratorsHouse);
            row.createCell((short) 28).setCellValue(wipMotorVehicles);
            row.createCell((short) 29).setCellValue(wipOfficeEquipmentComputers);

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