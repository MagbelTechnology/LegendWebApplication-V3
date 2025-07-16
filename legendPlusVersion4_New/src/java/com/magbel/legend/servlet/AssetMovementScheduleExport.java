package com.magbel.legend.servlet;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import magma.AssetRecordsBean;
   
public class AssetMovementScheduleExport extends HttpServlet
{
	private EmailSmsServiceBus mail ;
	private AssetRecordsBean ad;
	private ApprovalRecords records;
   public void doPost(HttpServletRequest request, 
    HttpServletResponse response)
      throws ServletException, IOException
     
   {
	 String userClass = (String) request.getSession().getAttribute("UserClass");

		Properties prop = new Properties();
		File file = new File("C:\\Property\\LegendPlus.properties");
		FileInputStream input = new FileInputStream(file);
		prop.load(input);

		String BankingApp = prop.getProperty("BankingApp");	 
	 
	 if (!userClass.equals("NULL") || userClass!=null){
//	   PrintWriter out = response.getWriter();
//    OutputStream out = null; 
	mail= new EmailSmsServiceBus();
	records = new ApprovalRecords();
//    String branch_Code = request.getParameter("initiatorSOLID");
 //   String branch_Id = request.getParameter("branch");
    //String branchCode = request.getParameter("BRANCH_CODE");
	 String userId =(String) request.getSession().getAttribute("CurrentUser");
    String branch_Id =(String) request.getSession().getAttribute("UserCenter");
    String userName = records.getCodeName("select user_name from am_gb_user where user_id = ? ",userId);
    String branch_Code = records.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID = ? ",branch_Id);
    System.out.println("<<<<<<branch_Id: "+branch_Id);
    String reportType = request.getParameter("reportType");
    String FromDate = request.getParameter("startDate");
    String ToDate = request.getParameter("endDate");
    System.out.println("======>FromDate: "+FromDate);
  
    String branchCode = "";
   
    if(!branch_Id.equals("0")){
    	branchCode = records.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID = ? ",branch_Id);
    }
    System.out.println("<<<<<<branch_Code: "+branch_Code+"     reportType: "+reportType);
//    String userName = request.getParameter("userName");
    String fileName = branch_Code+"By"+userName+"AssetMovementScheduleExportReport.xlsx";    	
    String filePath = System.getProperty("user.home")+"\\Downloads";
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
    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", 
   "attachment; filename="+fileName+"");
    try
    {
    	ad = new AssetRecordsBean();
 
//    if(exists==false){   	

        String categoryCode = request.getParameter("category");
     Report rep = new Report();
//   System.out.println("<<<<<<branch_Id: "+branch_Id+"    categoryCode: "+categoryCode+"  branchCode: "+branchCode);
     String ColQuery = "";
if(BankingApp.equalsIgnoreCase("FLEXICUBE")) {  
    if(reportType.equals("BankWide")){
     if(FromDate.equals("")  && ToDate.equals("")){
    	 System.out.println("======>>>>>>>FromDate and ToDate Not Selected: ");
	     ColQuery ="select '1' AS SERIAL,'COST/VALUATION BALANCES AS AT BEGINING OF YEAR' AS NARRATION, * from (select class_name, cost_open_bal from fixed_asset_schedule) fixed_asset_schedule "
	     		+ "pivot(sum(cost_open_bal) for class_name "
	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
	     		+ "[COMPUTER SOFTWARE] "
	     		+ ") ) as pivottable "
	     		+ "UNION "
	     		+ "select '2' AS SERIAL,'ADDITIONS' AS NARRATION, * from (select class_name,cost_additions from fixed_asset_schedule) fixed_asset_schedule "
	     		+ "pivot(sum(cost_additions) for class_name "
	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
	     		+ "[COMPUTER SOFTWARE] "
	     		+ ") ) as pivottable "
	     		+ "UNION "
	     		+ "select '3' AS SERIAL,'DISPOSAL' AS NARRATION,* from (select class_name,cost_disposal from fixed_asset_schedule) fixed_asset_schedule "
	     		+ "pivot(sum(cost_disposal) for class_name "
	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
	     		+ "[COMPUTER SOFTWARE] "
	     		+ ") ) as pivottable "
	     		+ ""
	     		+ "UNION    "
	     		+ "select '4' AS SERIAL,'RECLASSIFICATION' AS NARRATION,* from (select class_name,cost_reclass from fixed_asset_schedule) fixed_asset_schedule "
	     		+ "pivot(sum(cost_reclass) for class_name "
	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
	     		+ "[COMPUTER SOFTWARE] "
	     		+ ") ) as pivottable "
	     		+ "UNION   "
	     		+ "select '5.0' AS SERIAL,'TRANSFER FROM' AS NARRATION,* from (select class_name,cost_TransferFrom from fixed_asset_schedule) fixed_asset_schedule "
	     		+ "pivot(sum(cost_TransferFrom) for class_name "
	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
	     		+ "[COMPUTER SOFTWARE] "
	     		+ ") ) as pivottable "
	     		+ "UNION   "
	     		+ "select '5.1' AS SERIAL,'TRANSFER TO' AS NARRATION,* from (select class_name,cost_TransferTo from fixed_asset_schedule) fixed_asset_schedule "
	     		+ "pivot(sum(cost_TransferTo) for class_name "
	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
	     		+ "[COMPUTER SOFTWARE] "
	     		+ ") ) as pivottable "	     		
	     		+ "UNION   "
	     		+ "select '6' AS SERIAL,'IMPROVEMENT' AS NARRATION,* from (select class_name,improvement from fixed_asset_schedule) fixed_asset_schedule "
	     		+ "pivot(sum(improvement) for class_name "
	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
	     		+ "[COMPUTER SOFTWARE] "
	     		+ ") ) as pivottable "
	     		+ ""
	     		+ "UNION "
	     		+ "select '7' AS SERIAL,'ACCUM. DEPR. BALANCES AS AT BEGINING OF YEAR', * from (select class_name, dep_open_bal from fixed_asset_schedule) fixed_asset_schedule "
	     		+ "pivot(sum(dep_open_bal) for class_name "
	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
	     		+ "[COMPUTER SOFTWARE]  "
	     		+ ") ) as pivottable "
	     		+ "UNION "
	     		+ "select '8' AS SERIAL,'CHARGE FOR THE YEAR' AS NARRATION, * from (select class_name,dep_charge from fixed_asset_schedule) fixed_asset_schedule "
	     		+ "pivot(sum(dep_charge) for class_name "
	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
	     		+ "[COMPUTER SOFTWARE] "
	     		+ ") ) as pivottable "
	     		+ "UNION "
	     		+ "select '9.0' AS SERIAL,'DISPOSAL' AS NARRATION,* from (select class_name,dep_disposal from fixed_asset_schedule) fixed_asset_schedule "
	     		+ "pivot(sum(dep_disposal) for class_name "
	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
	     		+ "[COMPUTER SOFTWARE] "
	     		+ ") ) as pivottable "
	     		+ "UNION "
	     		+ "select '9.1' AS SERIAL,'ACCELERATED CHARGES' AS NARRATION,* from (select class_name,Accelerate_charge from fixed_asset_schedule) fixed_asset_schedule "
	     		+ "pivot(sum(Accelerate_charge) for class_name "
	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
	     		+ "[COMPUTER SOFTWARE] "
	     		+ ") ) as pivottable "
	     		+ "UNION "
	     		+ "select '9.2' AS SERIAL,'RECLASSIFICATION' AS NARRATION,* from (select class_name,dep_reclass from fixed_asset_schedule) fixed_asset_schedule "
	     		+ "pivot(sum(dep_reclass) for class_name "
	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
	     		+ "[COMPUTER SOFTWARE] "
	     		+ ") ) as pivottable "
	     		+ "UNION "
	     		+ "select '9.3' AS SERIAL,'NBV AS YEAR END' AS NARRATION,* from (select class_name,nbv_closing_bal from fixed_asset_schedule) fixed_asset_schedule "
	     		+ "pivot(sum(nbv_closing_bal) for class_name "
	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
	     		+ "[COMPUTER SOFTWARE] "
	     		+ ") ) as pivottable "
	     		+ "UNION "
	     		+ "select '9.4' AS SERIAL,'NBV OPENING BALANCE' AS NARRATION,* from (select class_name,nbv_open_bal from fixed_asset_schedule) fixed_asset_schedule "
	     		+ "pivot(sum(nbv_open_bal) for class_name "
	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
	     		+ "[COMPUTER SOFTWARE] "
	     		+ ") ) as pivottable"
	     		+ "";
	}      
     if(!FromDate.equals("")  && !ToDate.equals("")){
    	 System.out.println("======>>>>>>>FromDate and ToDate Selected: ");
	     ColQuery ="select '1' AS SERIAL,'COST/VALUATION BALANCES AS AT BEGINING OF YEAR' AS NARRATION, * from (select class_name, cost_open_bal from fixed_asset_schedule) fixed_asset_schedule "
	     		+ "pivot(sum(cost_open_bal) for class_name "
	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
	     		+ "[COMPUTER SOFTWARE] "
	     		+ ") ) as pivottable "
	     		+ "UNION "
	     		+ "select '2' AS SERIAL,'ADDITIONS' AS NARRATION, * from (select class_name,cost_additions from fixed_asset_schedule) fixed_asset_schedule "
	     		+ "pivot(sum(cost_additions) for class_name "
	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
	     		+ "[COMPUTER SOFTWARE] "
	     		+ ") ) as pivottable "
	     		+ "UNION "
	     		+ "select '3' AS SERIAL,'DISPOSAL' AS NARRATION,* from (select class_name,cost_disposal from fixed_asset_schedule) fixed_asset_schedule "
	     		+ "pivot(sum(cost_disposal) for class_name "
	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
	     		+ "[COMPUTER SOFTWARE] "
	     		+ ") ) as pivottable "
	     		+ ""
	     		+ "UNION    "
	     		+ "select '4' AS SERIAL,'RECLASSIFICATION' AS NARRATION,* from (select class_name,cost_reclass from fixed_asset_schedule) fixed_asset_schedule "
	     		+ "pivot(sum(cost_reclass) for class_name "
	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
	     		+ "[COMPUTER SOFTWARE] "
	     		+ ") ) as pivottable "
	     		+ "UNION   "
	     		+ "select '5.0' AS SERIAL,'TRANSFER FROM' AS NARRATION,* from (select class_name,cost_TransferFrom from fixed_asset_schedule) fixed_asset_schedule "
	     		+ "pivot(sum(cost_TransferFrom) for class_name "
	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
	     		+ "[COMPUTER SOFTWARE] "
	     		+ ") ) as pivottable "
	     		+ "UNION   "
	     		+ "select '5.1' AS SERIAL,'TRANSFER TO' AS NARRATION,* from (select class_name,cost_TransferTo from fixed_asset_schedule) fixed_asset_schedule "
	     		+ "pivot(sum(cost_TransferTo) for class_name "
	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
	     		+ "[COMPUTER SOFTWARE] "
	     		+ ") ) as pivottable "	     		
	     		+ "UNION   "
	     		+ "select '6' AS SERIAL,'IMPROVEMENT' AS NARRATION,* from (select class_name,improvement from fixed_asset_schedule) fixed_asset_schedule "
	     		+ "pivot(sum(improvement) for class_name "
	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
	     		+ "[COMPUTER SOFTWARE] "
	     		+ ") ) as pivottable "
	     		+ ""
	     		+ "UNION "
	     		+ "select '7' AS SERIAL,'ACCUM. DEPR. BALANCES AS AT BEGINING OF YEAR', * from (select class_name, dep_open_bal from fixed_asset_schedule) fixed_asset_schedule "
	     		+ "pivot(sum(dep_open_bal) for class_name "
	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
	     		+ "[COMPUTER SOFTWARE]  "
	     		+ ") ) as pivottable "
	     		+ "UNION "
	     		+ "select '8' AS SERIAL,'CHARGE FOR THE YEAR' AS NARRATION, * from (select class_name,dep_charge from fixed_asset_schedule) fixed_asset_schedule "
	     		+ "pivot(sum(dep_charge) for class_name "
	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
	     		+ "[COMPUTER SOFTWARE] "
	     		+ ") ) as pivottable "
	     		+ "UNION "
	     		+ "select '9.0' AS SERIAL,'DISPOSAL' AS NARRATION,* from (select class_name,dep_disposal from fixed_asset_schedule) fixed_asset_schedule "
	     		+ "pivot(sum(dep_disposal) for class_name "
	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
	     		+ "[COMPUTER SOFTWARE] "
	     		+ ") ) as pivottable "
	     		+ "UNION "
	     		+ "select '9.1' AS SERIAL,'ACCELERATED CHARGES' AS NARRATION,* from (select class_name,Accelerate_charge from fixed_asset_schedule) fixed_asset_schedule "
	     		+ "pivot(sum(Accelerate_charge) for class_name "
	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
	     		+ "[COMPUTER SOFTWARE] "
	     		+ ") ) as pivottable "
	     		+ "UNION "
	     		+ "select '9.2' AS SERIAL,'RECLASSIFICATION' AS NARRATION,* from (select class_name,dep_reclass from fixed_asset_schedule) fixed_asset_schedule "
	     		+ "pivot(sum(dep_reclass) for class_name "
	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
	     		+ "[COMPUTER SOFTWARE] "
	     		+ ") ) as pivottable "
	     		+ "UNION "
	     		+ "select '9.3' AS SERIAL,'NBV AS YEAR END' AS NARRATION,* from (select class_name,nbv_closing_bal from fixed_asset_schedule) fixed_asset_schedule "
	     		+ "pivot(sum(nbv_closing_bal) for class_name "
	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
	     		+ "[COMPUTER SOFTWARE] "
	     		+ ") ) as pivottable "
	     		+ "UNION "
	     		+ "select '9.4' AS SERIAL,'NBV OPENING BALANCE' AS NARRATION,* from (select class_name,nbv_open_bal from fixed_asset_schedule) fixed_asset_schedule "
	     		+ "pivot(sum(nbv_open_bal) for class_name "
	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
	     		+ "[COMPUTER SOFTWARE] "
	     		+ ") ) as pivottable"
	     		+ "";
	}      
} 
    if(reportType.equals("ByBranch")){
        if(FromDate.equals("")  && ToDate.equals("")){
       	 System.out.println("======>>>>>>>FromDate and ToDate Not Selected: ");
   	     ColQuery ="select '1' AS SERIAL,'COST/VALUATION BALANCES AS AT BEGINING OF YEAR' AS NARRATION, * from (select class_name, cost_open_bal from fixed_asset_schedule_ByBranch) fixed_asset_schedule_ByBranch "
   	     		+ "pivot(sum(cost_open_bal) for class_name "
   	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
   	     		+ "[COMPUTER SOFTWARE] "
   	     		+ ") ) as pivottable "
   	     		+ "UNION "
   	     		+ "select '2' AS SERIAL,'ADDITIONS' AS NARRATION, * from (select class_name,cost_additions from fixed_asset_schedule_ByBranch) fixed_asset_schedule_ByBranch "
   	     		+ "pivot(sum(cost_additions) for class_name "
   	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
   	     		+ "[COMPUTER SOFTWARE] "
   	     		+ ") ) as pivottable "
   	     		+ "UNION "
   	     		+ "select '3' AS SERIAL,'DISPOSAL' AS NARRATION,* from (select class_name,cost_disposal from fixed_asset_schedule_ByBranch) fixed_asset_schedule_ByBranch "
   	     		+ "pivot(sum(cost_disposal) for class_name "
   	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
   	     		+ "[COMPUTER SOFTWARE] "
   	     		+ ") ) as pivottable "
   	     		+ ""
   	     		+ "UNION    "
   	     		+ "select '4' AS SERIAL,'RECLASSIFICATION' AS NARRATION,* from (select class_name,cost_reclass from fixed_asset_schedule_ByBranch) fixed_asset_schedule_ByBranch "
   	     		+ "pivot(sum(cost_reclass) for class_name "
   	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
   	     		+ "[COMPUTER SOFTWARE] "
   	     		+ ") ) as pivottable "
   	     		+ "UNION   "
   	     		+ "select '5.0' AS SERIAL,'TRANSFER FROM' AS NARRATION,* from (select class_name,cost_TransferFrom from fixed_asset_schedule_ByBranch) fixed_asset_schedule_ByBranch "
   	     		+ "pivot(sum(cost_TransferFrom) for class_name "
   	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
   	     		+ "[COMPUTER SOFTWARE] "
   	     		+ ") ) as pivottable "
   	     		+ "UNION   "
   	     		+ "select '5.1' AS SERIAL,'TRANSFER TO' AS NARRATION,* from (select class_name,cost_TransferTo from fixed_asset_schedule_ByBranch) fixed_asset_schedule_ByBranch "
   	     		+ "pivot(sum(cost_TransferTo) for class_name "
   	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
   	     		+ "[COMPUTER SOFTWARE] "
   	     		+ ") ) as pivottable "	     		
   	     		+ "UNION   "
   	     		+ "select '6' AS SERIAL,'IMPROVEMENT' AS NARRATION,* from (select class_name,improvement from fixed_asset_schedule_ByBranch) fixed_asset_schedule_ByBranch "
   	     		+ "pivot(sum(improvement) for class_name "
   	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
   	     		+ "[COMPUTER SOFTWARE] "
   	     		+ ") ) as pivottable "
   	     		+ ""
   	     		+ "UNION "
   	     		+ "select '7' AS SERIAL,'ACCUM. DEPR. BALANCES AS AT BEGINING OF YEAR', * from (select class_name, dep_open_bal from fixed_asset_schedule_ByBranch) fixed_asset_schedule_ByBranch "
   	     		+ "pivot(sum(dep_open_bal) for class_name "
   	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
   	     		+ "[COMPUTER SOFTWARE]  "
   	     		+ ") ) as pivottable "
   	     		+ "UNION "
   	     		+ "select '8' AS SERIAL,'CHARGE FOR THE YEAR' AS NARRATION, * from (select class_name,dep_charge from fixed_asset_schedule_ByBranch) fixed_asset_schedule_ByBranch "
   	     		+ "pivot(sum(dep_charge) for class_name "
   	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
   	     		+ "[COMPUTER SOFTWARE] "
   	     		+ ") ) as pivottable "
   	     		+ "UNION "
   	     		+ "select '9.0' AS SERIAL,'DISPOSAL' AS NARRATION,* from (select class_name,dep_disposal from fixed_asset_schedule_ByBranch) fixed_asset_schedule_ByBranch "
   	     		+ "pivot(sum(dep_disposal) for class_name "
   	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
   	     		+ "[COMPUTER SOFTWARE] "
   	     		+ ") ) as pivottable "
   	     		+ "UNION "
   	     		+ "select '9.1' AS SERIAL,'ACCELERATED CHARGES' AS NARRATION,* from (select class_name,Accelerate_charge from fixed_asset_schedule_ByBranch) fixed_asset_schedule_ByBranch "
   	     		+ "pivot(sum(Accelerate_charge) for class_name "
   	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
   	     		+ "[COMPUTER SOFTWARE] "
   	     		+ ") ) as pivottable "
   	     		+ "UNION "
   	     		+ "select '9.2' AS SERIAL,'RECLASSIFICATION' AS NARRATION,* from (select class_name,dep_reclass from fixed_asset_schedule_ByBranch) fixed_asset_schedule_ByBranch "
   	     		+ "pivot(sum(dep_reclass) for class_name "
   	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
   	     		+ "[COMPUTER SOFTWARE] "
   	     		+ ") ) as pivottable "
   	     		+ "UNION "
   	     		+ "select '9.3' AS SERIAL,'NBV AS YEAR END' AS NARRATION,* from (select class_name,nbv_closing_bal from fixed_asset_schedule_ByBranch) fixed_asset_schedule_ByBranch "
   	     		+ "pivot(sum(nbv_closing_bal) for class_name "
   	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
   	     		+ "[COMPUTER SOFTWARE] "
   	     		+ ") ) as pivottable "
   	     		+ "UNION "
   	     		+ "select '9.4' AS SERIAL,'NBV OPENING BALANCE' AS NARRATION,* from (select class_name,nbv_open_bal from fixed_asset_schedule_ByBranch) fixed_asset_schedule_ByBranch "
   	     		+ "pivot(sum(nbv_open_bal) for class_name "
   	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
   	     		+ "[COMPUTER SOFTWARE] "
   	     		+ ") ) as pivottable"
   	     		+ "";
   	}      
        if(!FromDate.equals("")  && !ToDate.equals("")){
       	 System.out.println("======>>>>>>>FromDate and ToDate Selected: ");
   	     ColQuery ="select '1' AS SERIAL,'COST/VALUATION BALANCES AS AT BEGINING OF YEAR' AS NARRATION, * from (select class_name, cost_open_bal from fixed_asset_schedule_ByBranch) fixed_asset_schedule_ByBranch "
   	     		+ "pivot(sum(cost_open_bal) for class_name "
   	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
   	     		+ "[COMPUTER SOFTWARE] "
   	     		+ ") ) as pivottable "
   	     		+ "UNION "
   	     		+ "select '2' AS SERIAL,'ADDITIONS' AS NARRATION, * from (select class_name,cost_additions from fixed_asset_schedule_ByBranch) fixed_asset_schedule_ByBranch "
   	     		+ "pivot(sum(cost_additions) for class_name "
   	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
   	     		+ "[COMPUTER SOFTWARE] "
   	     		+ ") ) as pivottable "
   	     		+ "UNION "
   	     		+ "select '3' AS SERIAL,'DISPOSAL' AS NARRATION,* from (select class_name,cost_disposal from fixed_asset_schedule_ByBranch) fixed_asset_schedule_ByBranch "
   	     		+ "pivot(sum(cost_disposal) for class_name "
   	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
   	     		+ "[COMPUTER SOFTWARE] "
   	     		+ ") ) as pivottable "
   	     		+ ""
   	     		+ "UNION    "
   	     		+ "select '4' AS SERIAL,'RECLASSIFICATION' AS NARRATION,* from (select class_name,cost_reclass from fixed_asset_schedule_ByBranch) fixed_asset_schedule_ByBranch "
   	     		+ "pivot(sum(cost_reclass) for class_name "
   	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
   	     		+ "[COMPUTER SOFTWARE] "
   	     		+ ") ) as pivottable "
   	     		+ "UNION   "
   	     		+ "select '5.0' AS SERIAL,'TRANSFER FROM' AS NARRATION,* from (select class_name,cost_TransferFrom from fixed_asset_schedule_ByBranch) fixed_asset_schedule_ByBranch "
   	     		+ "pivot(sum(cost_TransferFrom) for class_name "
   	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
   	     		+ "[COMPUTER SOFTWARE] "
   	     		+ ") ) as pivottable "
   	     		+ "UNION   "
   	     		+ "select '5.1' AS SERIAL,'TRANSFER TO' AS NARRATION,* from (select class_name,cost_TransferTo from fixed_asset_schedule_ByBranch) fixed_asset_schedule_ByBranch "
   	     		+ "pivot(sum(cost_TransferTo) for class_name "
   	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
   	     		+ "[COMPUTER SOFTWARE] "
   	     		+ ") ) as pivottable "	     		
   	     		+ "UNION   "
   	     		+ "select '6' AS SERIAL,'IMPROVEMENT' AS NARRATION,* from (select class_name,improvement from fixed_asset_schedule_ByBranch) fixed_asset_schedule_ByBranch "
   	     		+ "pivot(sum(improvement) for class_name "
   	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
   	     		+ "[COMPUTER SOFTWARE] "
   	     		+ ") ) as pivottable "
   	     		+ ""
   	     		+ "UNION "
   	     		+ "select '7' AS SERIAL,'ACCUM. DEPR. BALANCES AS AT BEGINING OF YEAR', * from (select class_name, dep_open_bal from fixed_asset_schedule_ByBranch) fixed_asset_schedule_ByBranch "
   	     		+ "pivot(sum(dep_open_bal) for class_name "
   	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
   	     		+ "[COMPUTER SOFTWARE]  "
   	     		+ ") ) as pivottable "
   	     		+ "UNION "
   	     		+ "select '8' AS SERIAL,'CHARGE FOR THE YEAR' AS NARRATION, * from (select class_name,dep_charge from fixed_asset_schedule_ByBranch) fixed_asset_schedule_ByBranch "
   	     		+ "pivot(sum(dep_charge) for class_name "
   	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
   	     		+ "[COMPUTER SOFTWARE] "
   	     		+ ") ) as pivottable "
   	     		+ "UNION "
   	     		+ "select '9.0' AS SERIAL,'DISPOSAL' AS NARRATION,* from (select class_name,dep_disposal from fixed_asset_schedule_ByBranch) fixed_asset_schedule_ByBranch "
   	     		+ "pivot(sum(dep_disposal) for class_name "
   	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
   	     		+ "[COMPUTER SOFTWARE] "
   	     		+ ") ) as pivottable "
   	     		+ "UNION "
   	     		+ "select '9.1' AS SERIAL,'ACCELERATED CHARGES' AS NARRATION,* from (select class_name,Accelerate_charge from fixed_asset_schedule_ByBranch) fixed_asset_schedule_ByBranch "
   	     		+ "pivot(sum(Accelerate_charge) for class_name "
   	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
   	     		+ "[COMPUTER SOFTWARE] "
   	     		+ ") ) as pivottable "
   	     		+ "UNION "
   	     		+ "select '9.2' AS SERIAL,'RECLASSIFICATION' AS NARRATION,* from (select class_name,dep_reclass from fixed_asset_schedule_ByBranch) fixed_asset_schedule_ByBranch "
   	     		+ "pivot(sum(dep_reclass) for class_name "
   	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
   	     		+ "[COMPUTER SOFTWARE] "
   	     		+ ") ) as pivottable "
   	     		+ "UNION "
   	     		+ "select '9.3' AS SERIAL,'NBV AS YEAR END' AS NARRATION,* from (select class_name,nbv_closing_bal from fixed_asset_schedule_ByBranch) fixed_asset_schedule_ByBranch "
   	     		+ "pivot(sum(nbv_closing_bal) for class_name "
   	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
   	     		+ "[COMPUTER SOFTWARE] "
   	     		+ ") ) as pivottable "
   	     		+ "UNION "
   	     		+ "select '9.4' AS SERIAL,'NBV OPENING BALANCE' AS NARRATION,* from (select class_name,nbv_open_bal from fixed_asset_schedule_ByBranch) fixed_asset_schedule_ByBranch "
   	     		+ "pivot(sum(nbv_open_bal) for class_name "
   	     		+ "in ([BUILDING],[LAND],[LEASEHOLD IMPROVEMENT],[FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MOTOR VEHICLES],[AIRCRAFT],"
   	     		+ "[COMPUTER SOFTWARE] "
   	     		+ ") ) as pivottable"
   	     		+ "";
   	}      
   }      
//   System.out.println("======>>>>>>>ColQuery: "+ColQuery);
     java.util.ArrayList list =rep.getFixedAssetMovementScheduleRecords(ColQuery,FromDate,ToDate,BankingApp);
     if(list.size()!=0){
	 
    	 SXSSFWorkbook workbook = new SXSSFWorkbook();
         Sheet sheet = workbook.createSheet("Demo");
         Row rowhead = sheet.createRow((int) 0);
         
         rowhead.createCell((short) 0).setCellValue("NARRATION");
         rowhead.createCell((short) 1).setCellValue("BUILDING");
         rowhead.createCell((short) 2).setCellValue("LAND");
         rowhead.createCell((short) 3).setCellValue("LEASEHOLD IMPROVEMENT");
         rowhead.createCell((short) 4).setCellValue("FURNITURE FITTINGS & EQUIPMENT");
         rowhead.createCell((short) 5).setCellValue("COMPUTER EQUIPMENT");
         rowhead.createCell((short) 6).setCellValue("MOTOR VEHICLES");
         rowhead.createCell((short) 7).setCellValue("AIRCRAFT");
         rowhead.createCell((short) 8).setCellValue("COMPUTER SOFTWARE");
         rowhead.createCell((short) 9).setCellValue("TOTAL");
     int i = 1;
     System.out.println("<<<<<<list.size(): "+list.size());
	 for(int k=0;k<list.size();k++)
     {
    	 com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);    	 
			String furniture_fittings =  newassettrans.getAssetMaintenance();//furniture_fittings;
//			System.out.println("<<<<<<furniture_fittings: "+furniture_fittings);
			String computer_equipment =  newassettrans.getAssetType();//computer_equipment;
			String barCode =  newassettrans.getBarCode();//narration;
			String computerEquipment =  newassettrans.getDescription();//computerEquipment
//			System.out.println("<<<<<<computer_equipment: "+computer_equipment);
			String intangible_assets = newassettrans.getCategoryName();  //intangible_assets; 
			String motor_vehicles = newassettrans.getEngineNo();//motor_vehicles
			String building = newassettrans.getSpare2();//building
			String spare1 = newassettrans.getSpare1();
			branchCode = newassettrans.getBranchCode();
			String land = newassettrans.getAssetUser();//Land
			String leasehold_Improvement = newassettrans.getDescription();//leasehold_Improvement
//			String airCraft = newassettrans.getCategoryName();//airCraft
			String computerSoftware = newassettrans.getLocation();//computerSoftware
			String aircraft = newassettrans.getCategoryName();//airCraft
//			System.out.println("<<<<<<aircraft: "+aircraft);
			if(aircraft==null || aircraft =="null") {aircraft = "0";}
			if(computerSoftware==null || computerSoftware =="null") {computerSoftware = "0";}
//			String branchName = records.getCodeName("select BRANCH_NAME from am_ad_branch where BRANCH_ID = "+branchId+"");
			double totalValue = Double.valueOf(building)+Double.valueOf(land)+Double.valueOf(leasehold_Improvement)+Double.valueOf(furniture_fittings)+
					Double.valueOf(computer_equipment)+Double.valueOf(motor_vehicles)+Double.valueOf(aircraft)+Double.valueOf(computerSoftware);
//			System.out.println("<<<<<<totalValue: "+totalValue);
			Row row = sheet.createRow((int) i);

            row.createCell((short) 0).setCellValue(barCode);
            row.createCell((short) 1).setCellValue(Double.valueOf(building));
            row.createCell((short) 2).setCellValue(Double.valueOf(land));
            row.createCell((short) 3).setCellValue(Double.valueOf(leasehold_Improvement));
            row.createCell((short) 4).setCellValue(Double.valueOf(furniture_fittings));
            row.createCell((short) 5).setCellValue(Double.valueOf(computer_equipment));
            row.createCell((short) 6).setCellValue(Double.valueOf(motor_vehicles));
            row.createCell((short) 7).setCellValue(Double.valueOf(aircraft));
            row.createCell((short) 8).setCellValue(Double.valueOf(computerSoftware));
            row.createCell((short) 9).setCellValue(Double.valueOf(totalValue));
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
 }
if(BankingApp.equalsIgnoreCase("FINACLE")) {     
    if(FromDate.equals("")  && ToDate.equals("")){
	     ColQuery =""
	     		+ "select '1' AS SERIAL,'COST/VALUATION BALANCES AS AT BEGINING OF YEAR' AS NARRATION, * from (select class_name, cost_open_bal from fixed_asset_schedule) fixed_asset_schedule "
	     		+ "pivot(sum(cost_open_bal) for class_name "
	     		+ "in ([LAND & LEASEHOLD],[WORK IN PROGRESS],[MOTOR VEHCILE],[MACHINE & EQUIPMENT],[COMPUTER],[FURNITURE & FITTINGS],[INTANGIBLE ASSETS]"
	     		+ ") ) as pivottable "
	     		+ "UNION "
	     		+ "select '2' AS SERIAL,'ADDITIONS' AS NARRATION, * from (select class_name,cost_additions from fixed_asset_schedule) fixed_asset_schedule "
	     		+ "pivot(sum(cost_additions) for class_name "
	     		+ "in ([LAND & LEASEHOLD],[WORK IN PROGRESS],[MOTOR VEHCILE],[MACHINE & EQUIPMENT],[COMPUTER],[FURNITURE & FITTINGS],[INTANGIBLE ASSETS]"
	     		+ ") ) as pivottable "
	     		+ "UNION "
	     		+ "select '3' AS SERIAL,'DISPOSAL' AS NARRATION,* from (select class_name,cost_disposal from fixed_asset_schedule) fixed_asset_schedule "
	     		+ "pivot(sum(cost_disposal) for class_name "
	     		+ "in ([LAND & LEASEHOLD],[WORK IN PROGRESS],[MOTOR VEHCILE],[MACHINE & EQUIPMENT],[COMPUTER],[FURNITURE & FITTINGS],[INTANGIBLE ASSETS]"
	     		+ ") ) as pivottable "
	     		+ ""
	     		+ "UNION    "
	     		+ "select '4' AS SERIAL,'RECLASSIFICATION' AS NARRATION,* from (select class_name,cost_reclass from fixed_asset_schedule) fixed_asset_schedule "
	     		+ "pivot(sum(cost_reclass) for class_name "
	     		+ "in ([LAND & LEASEHOLD],[WORK IN PROGRESS],[MOTOR VEHCILE],[MACHINE & EQUIPMENT],[COMPUTER],[FURNITURE & FITTINGS],[INTANGIBLE ASSETS]"
	     		+ ") ) as pivottable "
	     		+ "UNION   "
	     		+ "select '5.0' AS SERIAL,'TRANSFER FROM' AS NARRATION,* from (select class_name,cost_TransferFrom from fixed_asset_schedule) fixed_asset_schedule "
	     		+ "pivot(sum(cost_TransferFrom) for class_name "
	     		+ "in ([LAND & LEASEHOLD],[WORK IN PROGRESS],[MOTOR VEHCILE],[MACHINE & EQUIPMENT],[COMPUTER],[FURNITURE & FITTINGS],[INTANGIBLE ASSETS]"
	     		+ ") ) as pivottable "
	     		+ "UNION   "
	     		+ "select '5.1' AS SERIAL,'TRANSFER TO' AS NARRATION,* from (select class_name,cost_TransferTo from fixed_asset_schedule) fixed_asset_schedule "
	     		+ "pivot(sum(cost_TransferTo) for class_name "
	     		+ "in ([LAND & LEASEHOLD],[WORK IN PROGRESS],[MOTOR VEHCILE],[MACHINE & EQUIPMENT],[COMPUTER],[FURNITURE & FITTINGS],[INTANGIBLE ASSETS]"
	     		+ ") ) as pivottable 	     		"
	     		+ "UNION   "
	     		+ "select '6' AS SERIAL,'IMPROVEMENT' AS NARRATION,* from (select class_name,improvement from fixed_asset_schedule) fixed_asset_schedule "
	     		+ "pivot(sum(improvement) for class_name "
	     		+ "in ([LAND & LEASEHOLD],[WORK IN PROGRESS],[MOTOR VEHCILE],[MACHINE & EQUIPMENT],[COMPUTER],[FURNITURE & FITTINGS],[INTANGIBLE ASSETS]"
	     		+ ") ) as pivottable "
	     		+ ""
	     		+ "UNION "
	     		+ "select '7' AS SERIAL,'ACCUM. DEPR. BALANCES AS AT BEGINING OF YEAR', * from (select class_name, dep_open_bal from fixed_asset_schedule) fixed_asset_schedule "
	     		+ "pivot(sum(dep_open_bal) for class_name "
	     		+ "in ([LAND & LEASEHOLD],[WORK IN PROGRESS],[MOTOR VEHCILE],[MACHINE & EQUIPMENT],[COMPUTER],[FURNITURE & FITTINGS],[INTANGIBLE ASSETS] "
	     		+ ") ) as pivottable "
	     		+ "UNION "
	     		+ "select '8' AS SERIAL,'CHARGE FOR THE YEAR' AS NARRATION, * from (select class_name,dep_charge from fixed_asset_schedule) fixed_asset_schedule "
	     		+ "pivot(sum(dep_charge) for class_name "
	     		+ "in ([LAND & LEASEHOLD],[WORK IN PROGRESS],[MOTOR VEHCILE],[MACHINE & EQUIPMENT],[COMPUTER],[FURNITURE & FITTINGS],[INTANGIBLE ASSETS] "
	     		+ ") ) as pivottable "
	     		+ "UNION "
	     		+ "select '9.0' AS SERIAL,'DISPOSAL' AS NARRATION,* from (select class_name,dep_disposal from fixed_asset_schedule) fixed_asset_schedule "
	     		+ "pivot(sum(dep_disposal) for class_name "
	     		+ "in ([LAND & LEASEHOLD],[WORK IN PROGRESS],[MOTOR VEHCILE],[MACHINE & EQUIPMENT],[COMPUTER],[FURNITURE & FITTINGS],[INTANGIBLE ASSETS] "
	     		+ ") ) as pivottable "
	     		+ "UNION "
	     		+ "select '9.1' AS SERIAL,'ACCELERATED CHARGES' AS NARRATION,* from (select class_name,Accelerate_charge from fixed_asset_schedule) fixed_asset_schedule "
	     		+ "pivot(sum(Accelerate_charge) for class_name "
	     		+ "in ([LAND & LEASEHOLD],[WORK IN PROGRESS],[MOTOR VEHCILE],[MACHINE & EQUIPMENT],[COMPUTER],[FURNITURE & FITTINGS],[INTANGIBLE ASSETS] "
	     		+ ") ) as pivottable "
	     		+ "UNION "
	     		+ "select '9.2' AS SERIAL,'RECLASSIFICATION' AS NARRATION,* from (select class_name,dep_reclass from fixed_asset_schedule) fixed_asset_schedule "
	     		+ "pivot(sum(dep_reclass) for class_name "
	     		+ "in ([LAND & LEASEHOLD],[WORK IN PROGRESS],[MOTOR VEHCILE],[MACHINE & EQUIPMENT],[COMPUTER],[FURNITURE & FITTINGS],[INTANGIBLE ASSETS] "
	     		+ ") ) as pivottable "
	     		+ "UNION "
	     		+ "select '9.3' AS SERIAL,'NBV AS YEAR END' AS NARRATION,* from (select class_name,nbv_closing_bal from fixed_asset_schedule) fixed_asset_schedule "
	     		+ "pivot(sum(nbv_closing_bal) for class_name "
	     		+ "in ([LAND & LEASEHOLD],[WORK IN PROGRESS],[MOTOR VEHCILE],[MACHINE & EQUIPMENT],[COMPUTER],[FURNITURE & FITTINGS],[INTANGIBLE ASSETS] "
	     		+ ") ) as pivottable "
	     		+ "UNION "
	     		+ "select '9.4' AS SERIAL,'NBV OPENING BALANCE' AS NARRATION,* from (select class_name,nbv_open_bal from fixed_asset_schedule) fixed_asset_schedule "
	     		+ "pivot(sum(nbv_open_bal) for class_name "
	     		+ "in ([LAND & LEASEHOLD],[WORK IN PROGRESS],[MOTOR VEHCILE],[MACHINE & EQUIPMENT],[COMPUTER],[FURNITURE & FITTINGS],[INTANGIBLE ASSETS] "
	     		+ ") ) as pivottable";
	}      
    
//  System.out.println("======>>>>>>>ColQuery: "+ColQuery);
    java.util.ArrayList list =rep.getFixedAssetMovementScheduleRecords(ColQuery,FromDate,ToDate,BankingApp);
    if(list.size()!=0){
	 
   	 SXSSFWorkbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet("Demo");
        Row rowhead = sheet.createRow((int) 0);
        
        rowhead.createCell((short) 0).setCellValue("NARRATION");
        rowhead.createCell((short) 1).setCellValue("LAND & LEASEHOLD");
        rowhead.createCell((short) 2).setCellValue("WORK IN PROGRESS");
        rowhead.createCell((short) 3).setCellValue("MOTOR VEHCILE");
        rowhead.createCell((short) 4).setCellValue("MACHINE & EQUIPMENT");
        rowhead.createCell((short) 5).setCellValue("COMPUTER");
        rowhead.createCell((short) 6).setCellValue("FURNITURE & FITTINGS");
        rowhead.createCell((short) 7).setCellValue("INTANGIBLE ASSETS");
        rowhead.createCell((short) 8).setCellValue("TOTAL");
    int i = 1;
//    System.out.println("<<<<<<list.size(): "+list.size());
	 for(int k=0;k<list.size();k++)
    {
   	 com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);    	 
			String furniture_fittings =  newassettrans.getAssetMaintenance();//furniture_fittings;
			String computer_equipment =  newassettrans.getAssetType();//computer_equipment;
			String barCode =  newassettrans.getBarCode();//narration;
			String computerEquipment =  newassettrans.getDescription();//computerEquipment
			String intangible_assets = newassettrans.getCategoryName();  //intangible_assets; 
			String motor_vehicles = newassettrans.getEngineNo();//motor_vehicles
			String building = newassettrans.getSpare2();//building
			String spare1 = newassettrans.getSpare1();
			branchCode = newassettrans.getBranchCode();
			String land = newassettrans.getAssetUser();//Land
			String leasehold_Improvement = newassettrans.getDescription();//leasehold_Improvement
			String airCraft = newassettrans.getCategoryName();//airCraft
			String computerSoftware = newassettrans.getLocation();//computerSoftware
//			String branchName = records.getCodeName("select BRANCH_NAME from am_ad_branch where BRANCH_ID = "+branchId+"");
			double totalValue = Double.valueOf(building)+Double.valueOf(land)+Double.valueOf(leasehold_Improvement)+Double.valueOf(furniture_fittings)+
					Double.valueOf(computer_equipment)+Double.valueOf(motor_vehicles)+Double.valueOf(computerSoftware); 
			Row row = sheet.createRow((int) i);

           row.createCell((short) 0).setCellValue(barCode);
           row.createCell((short) 1).setCellValue(Double.valueOf(building));
           row.createCell((short) 2).setCellValue(Double.valueOf(land));
           row.createCell((short) 3).setCellValue(Double.valueOf(motor_vehicles));
           row.createCell((short) 4).setCellValue(Double.valueOf(leasehold_Improvement));
           row.createCell((short) 5).setCellValue(Double.valueOf(computer_equipment));
           row.createCell((short) 6).setCellValue(Double.valueOf(furniture_fittings));
           row.createCell((short) 7).setCellValue(Double.valueOf(computerSoftware));
           row.createCell((short) 8).setCellValue(Double.valueOf(totalValue));
           i++;
    }
	   OutputStream stream = response.getOutputStream();
//        new MailSender().AttachmentMail("ayomidematanmi4@gmail.com",file);
        workbook.write(stream);
        stream.close();
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