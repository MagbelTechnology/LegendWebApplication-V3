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
   
public class AssetMgtSummaryCatExport extends HttpServlet
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
    String report = request.getParameter("report");
    //String branchCode = request.getParameter("BRANCH_CODE");
    System.out.println("<<<<<<branch_Id: "+branch_Id+"  report: "+report);
    String branchCode = "";
    if(!branch_Id.equals("0")){
    	branchCode = records.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID = ? ",branch_Id);
    }
    String userName = records.getCodeName("select USER_NAME from am_gb_User where USER_ID = ? ",userId);
    System.out.println("<<<<<<branch_Code: "+branch_Code);
//    String fileName = branch_Code+"By"+userName+"AssetMgtSummaryCategoryReport.xlsx"; 
    String fileName = "";
    if(report.equalsIgnoreCase("rptMenuBCSC")){fileName = branchCode+"By"+userName+"AssetMgtSummaryCategoryReport.xlsx";}
    if(report.equalsIgnoreCase("rptMenuBCSB")){fileName = branchCode+"By"+userName+"AssetMgtSummaryBranchReport.xlsx";}
    if(report.equalsIgnoreCase("rptMenuBCSD")){fileName = branchCode+"By"+userName+"AssetMgtSummaryDepartmentReport.xlsx";}
    
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

        String categoryCode = request.getParameter("category");
     Report rep = new Report();
   System.out.println("<<<<<<branch_Id: "+branch_Id+"    categoryCode: "+categoryCode+"  branchCode: "+branchCode+"  report: "+report);
     String ColQuery = "";
   //********Asset Management Summary By Category
     if(report.equalsIgnoreCase("rptMenuBCSC")){
     if(!branch_Id.equals("0")  && !categoryCode.equals("0")){
    	 System.out.println("======>>>>>>>Branch and Category Selected: ");
	     ColQuery ="SELECT COUNT(*) AS TOTAL,a.BRANCH_CODE,b.BRANCH_NAME,'' AS DEPT_CODE,'' AS DEPT_NAME,a.CATEGORY_CODE,c.category_name,SUM(a.Accum_dep) AS Accum_dep,"
	     + "SUM(a.monthly_dep) AS Total_monthly_dep,SUM(a.Accum_Dep) AS Total_Accum_dep,SUM(a.Cost_Price) AS Total_Cost,SUM(a.NBV) AS TOTAL_NBV,SUM(a.IMPROV_COST) AS TOTAL_IMPROV_COST,"
	     + "SUM(a.IMPROV_ACCUMDEP) AS TOTAL_IMPROV_ACCUMDEP,SUM(a.IMPROV_MONTHLYDEP) AS TOTAL_IMPROV_MONTHLYDEP,SUM(a.IMPROV_NBV) AS TOTAL_IMPROV_NBV," 
	     + "SUM(a.Cost_Price + a.IMPROV_COST) AS Total_Final_Cost,SUM(a.NBV+a.IMPROV_NBV) AS FIANL_TOTAL_NBV,SUM(a.Accum_Dep+a.IMPROV_ACCUMDEP) AS FINAL_TOTAL_ACCUM"
   		 + " FROM am_ad_branch b INNER JOIN am_Asset a ON b.BRANCH_CODE = a.BRANCH_CODE"
   		 + " INNER JOIN am_ad_category c ON a.CATEGORY_CODE = c.category_code"
   		 + " LEFT OUTER JOIN am_ad_department e ON a.DEPT_CODE = e.DEPT_CODE,am_gb_company comp"
   		 + " WHERE a.Asset_Status = 'ACTIVE' and a.Cost_Price > (comp.Cost_Threshold-0.01) AND a.branch_id = ? AND a.CATEGORY_CODE = ? "
   		 + " GROUP BY comp.company_name,a.CATEGORY_CODE,c.category_name,a.BRANCH_CODE,b.BRANCH_NAME ";	
	}      
	 if(branch_Id.equals("0")  && !categoryCode.equals("0")){	 
	   System.out.println("======>>>>>>>Category Selected: ");
	     ColQuery ="SELECT COUNT(*) AS TOTAL,a.BRANCH_CODE,b.BRANCH_NAME,'' AS DEPT_CODE,'' AS DEPT_NAME,a.CATEGORY_CODE,c.category_name,SUM(a.Accum_dep) AS Accum_dep,"
	 + "SUM(a.monthly_dep) AS Total_monthly_dep,SUM(a.Accum_Dep) AS Total_Accum_dep,SUM(a.Cost_Price) AS Total_Cost,SUM(a.NBV) AS TOTAL_NBV,SUM(a.IMPROV_COST) AS TOTAL_IMPROV_COST,"
	 + "SUM(a.IMPROV_ACCUMDEP) AS TOTAL_IMPROV_ACCUMDEP,SUM(a.IMPROV_MONTHLYDEP) AS TOTAL_IMPROV_MONTHLYDEP,SUM(a.IMPROV_NBV) AS TOTAL_IMPROV_NBV," 
	 + "SUM(a.Cost_Price + a.IMPROV_COST) AS Total_Final_Cost,SUM(a.NBV+a.IMPROV_NBV) AS FIANL_TOTAL_NBV,SUM(a.Accum_Dep+a.IMPROV_ACCUMDEP) AS FINAL_TOTAL_ACCUM"
	 + " FROM am_ad_branch b INNER JOIN am_Asset a ON b.BRANCH_CODE = a.BRANCH_CODE"
	 + " INNER JOIN am_ad_category c ON a.CATEGORY_CODE = c.category_code"
	 + " LEFT OUTER JOIN am_ad_department e ON a.DEPT_CODE = e.DEPT_CODE,am_gb_company comp"
	 + " WHERE a.Asset_Status = 'ACTIVE' and a.Cost_Price > (comp.Cost_Threshold-0.01) AND a.CATEGORY_CODE = ? "
	 + " GROUP BY comp.company_name,a.CATEGORY_CODE,c.category_name,a.BRANCH_CODE,b.BRANCH_NAME ";	
   }
	 if(!branch_Id.equals("0")  && categoryCode.equals("0")){	   
	   System.out.println("======>>>>>>>Branch Selected: ");
	     ColQuery ="SELECT COUNT(*) AS TOTAL,a.BRANCH_CODE,b.BRANCH_NAME,'' AS DEPT_CODE,'' AS DEPT_NAME,a.CATEGORY_CODE,c.category_name,SUM(a.Accum_dep) AS Accum_dep,"
	 + "SUM(a.monthly_dep) AS Total_monthly_dep,SUM(a.Accum_Dep) AS Total_Accum_dep,SUM(a.Cost_Price) AS Total_Cost,SUM(a.NBV) AS TOTAL_NBV,SUM(a.IMPROV_COST) AS TOTAL_IMPROV_COST,"
	 + "SUM(a.IMPROV_ACCUMDEP) AS TOTAL_IMPROV_ACCUMDEP,SUM(a.IMPROV_MONTHLYDEP) AS TOTAL_IMPROV_MONTHLYDEP,SUM(a.IMPROV_NBV) AS TOTAL_IMPROV_NBV," 
	 + "SUM(a.Cost_Price + a.IMPROV_COST) AS Total_Final_Cost,SUM(a.NBV+a.IMPROV_NBV) AS FIANL_TOTAL_NBV,SUM(a.Accum_Dep+a.IMPROV_ACCUMDEP) AS FINAL_TOTAL_ACCUM"
     + " FROM am_ad_branch b INNER JOIN am_Asset a ON b.BRANCH_CODE = a.BRANCH_CODE"
     + " INNER JOIN am_ad_category c ON a.CATEGORY_CODE = c.category_code"
     + " LEFT OUTER JOIN am_ad_department e ON a.DEPT_CODE = e.DEPT_CODE,am_gb_company comp"
     + " WHERE a.Asset_Status = 'ACTIVE' and a.Cost_Price > (comp.Cost_Threshold-0.01) AND a.branch_id = ? "
     + " GROUP BY comp.company_name,a.CATEGORY_CODE,c.category_name,a.BRANCH_CODE,b.BRANCH_NAME ";	     
	}
   if(branch_Id.equals("0")  && categoryCode.equals("0")){
	   System.out.println("======>>>>>>>No Selection: ");
	     ColQuery ="SELECT COUNT(*) AS TOTAL,a.BRANCH_CODE,b.BRANCH_NAME,'' AS DEPT_CODE,'' AS DEPT_NAME,a.CATEGORY_CODE,c.category_name,SUM(a.Accum_dep) AS Accum_dep,"
	 + "SUM(a.monthly_dep) AS Total_monthly_dep,SUM(a.Accum_Dep) AS Total_Accum_dep,SUM(a.Cost_Price) AS Total_Cost,SUM(a.NBV) AS TOTAL_NBV,SUM(a.IMPROV_COST) AS TOTAL_IMPROV_COST,"
	 + "SUM(a.IMPROV_ACCUMDEP) AS TOTAL_IMPROV_ACCUMDEP,SUM(a.IMPROV_MONTHLYDEP) AS TOTAL_IMPROV_MONTHLYDEP,SUM(a.IMPROV_NBV) AS TOTAL_IMPROV_NBV," 
     + "SUM(a.Cost_Price + a.IMPROV_COST) AS Total_Final_Cost,SUM(a.NBV+a.IMPROV_NBV) AS FIANL_TOTAL_NBV,SUM(a.Accum_Dep+a.IMPROV_ACCUMDEP) AS FINAL_TOTAL_ACCUM"
     + " FROM am_ad_branch b INNER JOIN am_Asset a ON b.BRANCH_CODE = a.BRANCH_CODE"
     + " INNER JOIN am_ad_category c ON a.CATEGORY_CODE = c.category_code"
     + " LEFT OUTER JOIN am_ad_department e ON a.DEPT_CODE = e.DEPT_CODE,am_gb_company comp"
     + " WHERE a.Asset_Status = 'ACTIVE' and a.Cost_Price > (comp.Cost_Threshold-0.01)  "
     + " GROUP BY comp.company_name,a.CATEGORY_CODE,c.category_name,a.BRANCH_CODE,b.BRANCH_NAME ";
	}   
   } 
     //***********Asset Management Summary By Branch  
     if(report.equalsIgnoreCase("rptMenuBCSB")){
     if(!branch_Id.equals("0")  && !categoryCode.equals("0")){
    	 System.out.println("======>>>>>>>Branch and Category Selected: ");
	     ColQuery ="SELECT COUNT(*) AS TOTAL,a.BRANCH_CODE,b.BRANCH_NAME,'' AS DEPT_CODE,'' AS DEPT_NAME,a.CATEGORY_CODE,c.category_name,SUM(a.Accum_dep) AS Accum_dep,"
	     + "SUM(a.monthly_dep) AS Total_monthly_dep,SUM(a.Accum_Dep) AS Total_Accum_dep,SUM(a.Cost_Price) AS Total_Cost,SUM(a.NBV) AS TOTAL_NBV,SUM(a.IMPROV_COST) AS TOTAL_IMPROV_COST,"
	     + "SUM(a.IMPROV_ACCUMDEP) AS TOTAL_IMPROV_ACCUMDEP,SUM(a.IMPROV_MONTHLYDEP) AS TOTAL_IMPROV_MONTHLYDEP,SUM(a.IMPROV_NBV) AS TOTAL_IMPROV_NBV," 
	     + "SUM(a.Cost_Price + a.IMPROV_COST) AS Total_Final_Cost,SUM(a.NBV+a.IMPROV_NBV) AS FIANL_TOTAL_NBV,SUM(a.Accum_Dep+a.IMPROV_ACCUMDEP) AS FINAL_TOTAL_ACCUM"
   		 + " FROM am_ad_branch b INNER JOIN am_Asset a ON b.BRANCH_CODE = a.BRANCH_CODE"
   		 + " INNER JOIN am_ad_category c ON a.CATEGORY_CODE = c.category_code"
   		 + " LEFT OUTER JOIN am_ad_department e ON a.DEPT_CODE = e.DEPT_CODE,am_gb_company comp"
   		 + " WHERE a.Asset_Status = 'ACTIVE' and a.Cost_Price > (comp.Cost_Threshold-0.01) AND a.branch_id = ? AND a.CATEGORY_CODE = ? "
   		 + " GROUP BY comp.company_name,a.BRANCH_CODE,b.BRANCH_NAME,a.CATEGORY_CODE,c.category_name ";	
	}      
	 if(branch_Id.equals("0")  && !categoryCode.equals("0")){	 
	   System.out.println("======>>>>>>>Category Selected: ");
	     ColQuery ="SELECT COUNT(*) AS TOTAL,a.BRANCH_CODE,b.BRANCH_NAME,'' AS DEPT_CODE,'' AS DEPT_NAME,a.CATEGORY_CODE,c.category_name,SUM(a.Accum_dep) AS Accum_dep,"
	 + "SUM(a.monthly_dep) AS Total_monthly_dep,SUM(a.Accum_Dep) AS Total_Accum_dep,SUM(a.Cost_Price) AS Total_Cost,SUM(a.NBV) AS TOTAL_NBV,SUM(a.IMPROV_COST) AS TOTAL_IMPROV_COST,"
	 + "SUM(a.IMPROV_ACCUMDEP) AS TOTAL_IMPROV_ACCUMDEP,SUM(a.IMPROV_MONTHLYDEP) AS TOTAL_IMPROV_MONTHLYDEP,SUM(a.IMPROV_NBV) AS TOTAL_IMPROV_NBV," 
	 + "SUM(a.Cost_Price + a.IMPROV_COST) AS Total_Final_Cost,SUM(a.NBV+a.IMPROV_NBV) AS FIANL_TOTAL_NBV,SUM(a.Accum_Dep+a.IMPROV_ACCUMDEP) AS FINAL_TOTAL_ACCUM"
	 + " FROM am_ad_branch b INNER JOIN am_Asset a ON b.BRANCH_CODE = a.BRANCH_CODE"
	 + " INNER JOIN am_ad_category c ON a.CATEGORY_CODE = c.category_code"
	 + " LEFT OUTER JOIN am_ad_department e ON a.DEPT_CODE = e.DEPT_CODE,am_gb_company comp"
	 + " WHERE a.Asset_Status = 'ACTIVE' and a.Cost_Price > (comp.Cost_Threshold-0.01) AND a.CATEGORY_CODE = ? "
	 + " GROUP BY comp.company_name,a.BRANCH_CODE,b.BRANCH_NAME,a.CATEGORY_CODE,c.category_name ";		
   }
	 if(!branch_Id.equals("0")  && categoryCode.equals("0")){	   
	   System.out.println("======>>>>>>>Branch Selected: ");
	     ColQuery ="SELECT COUNT(*) AS TOTAL,a.BRANCH_CODE,b.BRANCH_NAME,'' AS DEPT_CODE,'' AS DEPT_NAME,a.CATEGORY_CODE,c.category_name,SUM(a.Accum_dep) AS Accum_dep,"
	 + "SUM(a.monthly_dep) AS Total_monthly_dep,SUM(a.Accum_Dep) AS Total_Accum_dep,SUM(a.Cost_Price) AS Total_Cost,SUM(a.NBV) AS TOTAL_NBV,SUM(a.IMPROV_COST) AS TOTAL_IMPROV_COST,"
	 + "SUM(a.IMPROV_ACCUMDEP) AS TOTAL_IMPROV_ACCUMDEP,SUM(a.IMPROV_MONTHLYDEP) AS TOTAL_IMPROV_MONTHLYDEP,SUM(a.IMPROV_NBV) AS TOTAL_IMPROV_NBV," 
	 + "SUM(a.Cost_Price + a.IMPROV_COST) AS Total_Final_Cost,SUM(a.NBV+a.IMPROV_NBV) AS FIANL_TOTAL_NBV,SUM(a.Accum_Dep+a.IMPROV_ACCUMDEP) AS FINAL_TOTAL_ACCUM"
     + " FROM am_ad_branch b INNER JOIN am_Asset a ON b.BRANCH_CODE = a.BRANCH_CODE"
     + " INNER JOIN am_ad_category c ON a.CATEGORY_CODE = c.category_code"
     + " LEFT OUTER JOIN am_ad_department e ON a.DEPT_CODE = e.DEPT_CODE,am_gb_company comp"
     + " WHERE a.Asset_Status = 'ACTIVE' and a.Cost_Price > (comp.Cost_Threshold-0.01) AND a.branch_id = ? "
	 + " GROUP BY comp.company_name,a.BRANCH_CODE,b.BRANCH_NAME,a.CATEGORY_CODE,c.category_name ";		     
	}
   if(branch_Id.equals("0")  && categoryCode.equals("0")){
	   System.out.println("======>>>>>>>No Selection: ");
	     ColQuery ="SELECT COUNT(*) AS TOTAL,a.BRANCH_CODE,b.BRANCH_NAME,'' AS DEPT_CODE,'' AS DEPT_NAME,a.CATEGORY_CODE,c.category_name,SUM(a.Accum_dep) AS Accum_dep,"
	 + "SUM(a.monthly_dep) AS Total_monthly_dep,SUM(a.Accum_Dep) AS Total_Accum_dep,SUM(a.Cost_Price) AS Total_Cost,SUM(a.NBV) AS TOTAL_NBV,SUM(a.IMPROV_COST) AS TOTAL_IMPROV_COST,"
	 + "SUM(a.IMPROV_ACCUMDEP) AS TOTAL_IMPROV_ACCUMDEP,SUM(a.IMPROV_MONTHLYDEP) AS TOTAL_IMPROV_MONTHLYDEP,SUM(a.IMPROV_NBV) AS TOTAL_IMPROV_NBV," 
     + "SUM(a.Cost_Price + a.IMPROV_COST) AS Total_Final_Cost,SUM(a.NBV+a.IMPROV_NBV) AS FIANL_TOTAL_NBV,SUM(a.Accum_Dep+a.IMPROV_ACCUMDEP) AS FINAL_TOTAL_ACCUM"
     + " FROM am_ad_branch b INNER JOIN am_Asset a ON b.BRANCH_CODE = a.BRANCH_CODE"
     + " INNER JOIN am_ad_category c ON a.CATEGORY_CODE = c.category_code"
     + " LEFT OUTER JOIN am_ad_department e ON a.DEPT_CODE = e.DEPT_CODE,am_gb_company comp"
     + " WHERE a.Asset_Status = 'ACTIVE' and a.Cost_Price > (comp.Cost_Threshold-0.01)  "
	 + " GROUP BY comp.company_name,a.BRANCH_CODE,b.BRANCH_NAME,a.CATEGORY_CODE,c.category_name ";	
	}   
   } 
     //***********Asset Management Summary By Department  
     if(report.equalsIgnoreCase("rptMenuBCSD")){
     if(!branch_Id.equals("0")  && !categoryCode.equals("0")){
    	 System.out.println("======>>>>>>>Branch and Category Selected: ");
	     ColQuery ="SELECT COUNT(*) AS TOTAL,a.BRANCH_CODE,b.BRANCH_NAME,a.DEPT_CODE,e.DEPT_NAME,a.CATEGORY_CODE,c.category_name,SUM(a.Accum_dep) AS Accum_dep,"
	     + "SUM(a.monthly_dep) AS Total_monthly_dep,SUM(a.Accum_Dep) AS Total_Accum_dep,SUM(a.Cost_Price) AS Total_Cost,SUM(a.NBV) AS TOTAL_NBV,SUM(a.IMPROV_COST) AS TOTAL_IMPROV_COST,"
	     + "SUM(a.IMPROV_ACCUMDEP) AS TOTAL_IMPROV_ACCUMDEP,SUM(a.IMPROV_MONTHLYDEP) AS TOTAL_IMPROV_MONTHLYDEP,SUM(a.IMPROV_NBV) AS TOTAL_IMPROV_NBV," 
	     + "SUM(a.Cost_Price + a.IMPROV_COST) AS Total_Final_Cost,SUM(a.NBV+a.IMPROV_NBV) AS FIANL_TOTAL_NBV,SUM(a.Accum_Dep+a.IMPROV_ACCUMDEP) AS FINAL_TOTAL_ACCUM"
   		 + " FROM am_ad_branch b INNER JOIN am_Asset a ON b.BRANCH_CODE = a.BRANCH_CODE"
   		 + " INNER JOIN am_ad_category c ON a.CATEGORY_CODE = c.category_code"
   		 + " LEFT OUTER JOIN am_ad_department e ON a.DEPT_CODE = e.DEPT_CODE,am_gb_company comp"
   		 + " WHERE a.Asset_Status = 'ACTIVE' and a.Cost_Price > (comp.Cost_Threshold-0.01) AND a.branch_id = ? AND a.CATEGORY_CODE = ? "
   		 + " GROUP BY comp.company_name,a.DEPT_CODE,e.DEPT_NAME,a.BRANCH_CODE,b.BRANCH_NAME,a.CATEGORY_CODE,c.category_name ";	
	}      
	 if(branch_Id.equals("0")  && !categoryCode.equals("0")){	 
	   System.out.println("======>>>>>>>Category Selected: ");
	     ColQuery ="SELECT COUNT(*) AS TOTAL,a.BRANCH_CODE,b.BRANCH_NAME,a.DEPT_CODE,e.DEPT_NAME,a.CATEGORY_CODE,c.category_name,SUM(a.Accum_dep) AS Accum_dep,"
	 + "SUM(a.monthly_dep) AS Total_monthly_dep,SUM(a.Accum_Dep) AS Total_Accum_dep,SUM(a.Cost_Price) AS Total_Cost,SUM(a.NBV) AS TOTAL_NBV,SUM(a.IMPROV_COST) AS TOTAL_IMPROV_COST,"
	 + "SUM(a.IMPROV_ACCUMDEP) AS TOTAL_IMPROV_ACCUMDEP,SUM(a.IMPROV_MONTHLYDEP) AS TOTAL_IMPROV_MONTHLYDEP,SUM(a.IMPROV_NBV) AS TOTAL_IMPROV_NBV," 
	 + "SUM(a.Cost_Price + a.IMPROV_COST) AS Total_Final_Cost,SUM(a.NBV+a.IMPROV_NBV) AS FIANL_TOTAL_NBV,SUM(a.Accum_Dep+a.IMPROV_ACCUMDEP) AS FINAL_TOTAL_ACCUM"
	 + " FROM am_ad_branch b INNER JOIN am_Asset a ON b.BRANCH_CODE = a.BRANCH_CODE"
	 + " INNER JOIN am_ad_category c ON a.CATEGORY_CODE = c.category_code"
	 + " LEFT OUTER JOIN am_ad_department e ON a.DEPT_CODE = e.DEPT_CODE,am_gb_company comp"
	 + " WHERE a.Asset_Status = 'ACTIVE' and a.Cost_Price > (comp.Cost_Threshold-0.01) AND a.CATEGORY_CODE = ? "
	 + " GROUP BY comp.company_name,a.DEPT_CODE,e.DEPT_NAME,a.BRANCH_CODE,b.BRANCH_NAME,a.CATEGORY_CODE,c.category_name ";		
   }
	 if(!branch_Id.equals("0")  && categoryCode.equals("0")){	   
	   System.out.println("======>>>>>>>Branch Selected: ");
	     ColQuery ="SELECT COUNT(*) AS TOTAL,a.BRANCH_CODE,b.BRANCH_NAME,a.DEPT_CODE,e.DEPT_NAME,a.CATEGORY_CODE,c.category_name,SUM(a.Accum_dep) AS Accum_dep,"
	 + "SUM(a.monthly_dep) AS Total_monthly_dep,SUM(a.Accum_Dep) AS Total_Accum_dep,SUM(a.Cost_Price) AS Total_Cost,SUM(a.NBV) AS TOTAL_NBV,SUM(a.IMPROV_COST) AS TOTAL_IMPROV_COST,"
	 + "SUM(a.IMPROV_ACCUMDEP) AS TOTAL_IMPROV_ACCUMDEP,SUM(a.IMPROV_MONTHLYDEP) AS TOTAL_IMPROV_MONTHLYDEP,SUM(a.IMPROV_NBV) AS TOTAL_IMPROV_NBV," 
	 + "SUM(a.Cost_Price + a.IMPROV_COST) AS Total_Final_Cost,SUM(a.NBV+a.IMPROV_NBV) AS FIANL_TOTAL_NBV,SUM(a.Accum_Dep+a.IMPROV_ACCUMDEP) AS FINAL_TOTAL_ACCUM"
     + " FROM am_ad_branch b INNER JOIN am_Asset a ON b.BRANCH_CODE = a.BRANCH_CODE"
     + " INNER JOIN am_ad_category c ON a.CATEGORY_CODE = c.category_code"
     + " LEFT OUTER JOIN am_ad_department e ON a.DEPT_CODE = e.DEPT_CODE,am_gb_company comp"
     + " WHERE a.Asset_Status = 'ACTIVE' and a.Cost_Price > (comp.Cost_Threshold-0.01) AND a.branch_id = ? "
	 + " GROUP BY comp.company_name,a.DEPT_CODE,e.DEPT_NAME,a.BRANCH_CODE,b.BRANCH_NAME,a.CATEGORY_CODE,c.category_name ";		     
	}
   if(branch_Id.equals("0")  && categoryCode.equals("0")){
	   System.out.println("======>>>>>>>No Selection: ");
	     ColQuery ="SELECT COUNT(*) AS TOTAL,a.BRANCH_CODE,b.BRANCH_NAME,a.DEPT_CODE,e.DEPT_NAME,a.CATEGORY_CODE,c.category_name,SUM(a.Accum_dep) AS Accum_dep,"
	 + "SUM(a.monthly_dep) AS Total_monthly_dep,SUM(a.Accum_Dep) AS Total_Accum_dep,SUM(a.Cost_Price) AS Total_Cost,SUM(a.NBV) AS TOTAL_NBV,SUM(a.IMPROV_COST) AS TOTAL_IMPROV_COST,"
	 + "SUM(a.IMPROV_ACCUMDEP) AS TOTAL_IMPROV_ACCUMDEP,SUM(a.IMPROV_MONTHLYDEP) AS TOTAL_IMPROV_MONTHLYDEP,SUM(a.IMPROV_NBV) AS TOTAL_IMPROV_NBV," 
     + "SUM(a.Cost_Price + a.IMPROV_COST) AS Total_Final_Cost,SUM(a.NBV+a.IMPROV_NBV) AS FIANL_TOTAL_NBV,SUM(a.Accum_Dep+a.IMPROV_ACCUMDEP) AS FINAL_TOTAL_ACCUM"
     + " FROM am_ad_branch b INNER JOIN am_Asset a ON b.BRANCH_CODE = a.BRANCH_CODE"
     + " INNER JOIN am_ad_category c ON a.CATEGORY_CODE = c.category_code"
     + " LEFT OUTER JOIN am_ad_department e ON a.DEPT_CODE = e.DEPT_CODE,am_gb_company comp"
     + " WHERE a.Asset_Status = 'ACTIVE' and a.Cost_Price > (comp.Cost_Threshold-0.01)  "
	 + " GROUP BY comp.company_name,a.DEPT_CODE,e.DEPT_NAME,a.BRANCH_CODE,b.BRANCH_NAME,a.CATEGORY_CODE,c.category_name ";	
	}   
   } 
        
//   System.out.println("======>>>>>>>ColQuery: "+ColQuery);
     java.util.ArrayList list =rep.getAssetMgtSummaryRecords(ColQuery,branch_Id,categoryCode);
     if(report.equalsIgnoreCase("rptMenuBCSC") || report.equalsIgnoreCase("rptMenuBCSB")){
     if(list.size()!=0){
	 
    	 SXSSFWorkbook workbook = new SXSSFWorkbook();
         Sheet sheet = workbook.createSheet("Demo");
         Row rowhead = sheet.createRow((int) 0);
         
         rowhead.createCell((short) 0).setCellValue("BRANCH NAME");
         rowhead.createCell((short) 1).setCellValue("BRANCH CODE");
         rowhead.createCell((short) 2).setCellValue("CATEGORY CODE");
         rowhead.createCell((short) 3).setCellValue("CATEGORY NAME");
         rowhead.createCell((short) 4).setCellValue("TOTAL ASSET");
         rowhead.createCell((short) 5).setCellValue("TOTAL COST");
         rowhead.createCell((short) 6).setCellValue("ACCUM DEPREC.");
		 rowhead.createCell((short) 7).setCellValue("MONTHLY DEPREC.");
		 rowhead.createCell((short) 8).setCellValue("NBV");
		 rowhead.createCell((short) 9).setCellValue("IMPROVE TOTAL COST");
		 rowhead.createCell((short) 10).setCellValue("IMPROVE ACCUM.");
		 rowhead.createCell((short) 11).setCellValue("IMPROVE MONTHLY");
		 rowhead.createCell((short) 12).setCellValue("IMPROVE NBV");
		 rowhead.createCell((short) 13).setCellValue("FINAL TOTAL COST");
		 rowhead.createCell((short) 14).setCellValue("FINAL TOTAL ACCUM");
		 rowhead.createCell((short) 15).setCellValue("FINAL TOTAL NBV");

     int i = 1;
//     System.out.println("<<<<<<list.size(): "+list.size());
	 for(int k=0;k<list.size();k++)
     {
    	 com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);    	 

    	 	int itemtotal = newassettrans.getNoofitems();
			branchCode = newassettrans.getBranchCode();
			String branchName = newassettrans.getBranchName();
			String categoryName = newassettrans.getCategoryName();
			String deptCode = newassettrans.getDeptCode();
			String deptName = newassettrans.getDeptName();
			double costprice = newassettrans.getCostPrice();
			double monthlyDepr = newassettrans.getMonthlyDep();
			double accumDepr = newassettrans.getAccumDep();
			double nbv = newassettrans.getNbv();
			double improvmonthldepr = newassettrans.getImprovmonthlyDep();
			double improvAccumDepr = newassettrans.getImprovaccumDep();
			double improvCostPrice = newassettrans.getImprovcostPrice();
			double improvNbv = newassettrans.getImprovnbv();
			double totalCost = newassettrans.getTotalCost();
			double totalnbv = newassettrans.getTotalnbv();
			double finaltotalAccum = accumDepr+improvAccumDepr;
			categoryCode = newassettrans.getCategoryCode();

			Row row = sheet.createRow((int) i);

            row.createCell((short) 0).setCellValue(branchName);
            row.createCell((short) 1).setCellValue(branchCode);
            row.createCell((short) 2).setCellValue(categoryName);
            row.createCell((short) 3).setCellValue(categoryCode);
            row.createCell((short) 4).setCellValue(itemtotal);
            row.createCell((short) 5).setCellValue(costprice);
            row.createCell((short) 6).setCellValue(accumDepr);
			row.createCell((short) 7).setCellValue(monthlyDepr);
			row.createCell((short) 8).setCellValue(nbv);
			row.createCell((short) 9).setCellValue(improvCostPrice);
			row.createCell((short) 10).setCellValue(improvAccumDepr);
			row.createCell((short) 11).setCellValue(improvmonthldepr);
			row.createCell((short) 12).setCellValue(improvNbv);
			row.createCell((short) 13).setCellValue(totalCost);
			row.createCell((short) 14).setCellValue(finaltotalAccum);
			row.createCell((short) 15).setCellValue(totalnbv); 

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
     if(report.equalsIgnoreCase("rptMenuBCSD")){
     if(list.size()!=0){
	 
    	 SXSSFWorkbook workbook = new SXSSFWorkbook();
         Sheet sheet = workbook.createSheet("Demo");
         Row rowhead = sheet.createRow((int) 0);
         
         rowhead.createCell((short) 0).setCellValue("DEPARTMENT NAME");
         rowhead.createCell((short) 1).setCellValue("DEPARTMENT CODE");
         rowhead.createCell((short) 2).setCellValue("BRANCH NAME");
         rowhead.createCell((short) 3).setCellValue("BRANCH CODE");
         rowhead.createCell((short) 4).setCellValue("CATEGORY CODE");
         rowhead.createCell((short) 5).setCellValue("CATEGORY NAME");
         rowhead.createCell((short) 6).setCellValue("TOTAL ASSET");
         rowhead.createCell((short) 7).setCellValue("TOTAL COST");
         rowhead.createCell((short) 8).setCellValue("ACCUM DEPREC.");
		 rowhead.createCell((short) 9).setCellValue("MONTHLY DEPREC.");
		 rowhead.createCell((short) 10).setCellValue("NBV");
		 rowhead.createCell((short) 11).setCellValue("IMPROVE TOTAL COST");
		 rowhead.createCell((short) 12).setCellValue("IMPROVE ACCUM.");
		 rowhead.createCell((short) 13).setCellValue("IMPROVE MONTHLY");
		 rowhead.createCell((short) 14).setCellValue("IMPROVE NBV");
		 rowhead.createCell((short) 15).setCellValue("FINAL TOTAL COST");
		 rowhead.createCell((short) 16).setCellValue("FINAL TOTAL ACCUM");
		 rowhead.createCell((short) 17).setCellValue("FINAL TOTAL NBV");

     int i = 1;
//     System.out.println("<<<<<<list.size(): "+list.size());
	 for(int k=0;k<list.size();k++)
     {
    	 com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);    	 

    	 	int itemtotal = newassettrans.getNoofitems();
			branchCode = newassettrans.getBranchCode();
			String branchName = newassettrans.getBranchName();
			String categoryName = newassettrans.getCategoryName();
			String deptCode = newassettrans.getDeptCode();
			String deptName = newassettrans.getDeptName();
			double costprice = newassettrans.getCostPrice();
			double monthlyDepr = newassettrans.getMonthlyDep();
			double accumDepr = newassettrans.getAccumDep();
			double nbv = newassettrans.getNbv();
			double improvmonthldepr = newassettrans.getImprovmonthlyDep();
			double improvAccumDepr = newassettrans.getImprovaccumDep();
			double improvCostPrice = newassettrans.getImprovcostPrice();
			double improvNbv = newassettrans.getImprovnbv();
			double totalCost = newassettrans.getTotalCost();
			double totalnbv = newassettrans.getTotalnbv();
			double finaltotalAccum = accumDepr+improvAccumDepr;
			categoryCode = newassettrans.getCategoryCode();

			Row row = sheet.createRow((int) i);

            row.createCell((short) 0).setCellValue(deptName);
            row.createCell((short) 1).setCellValue(deptCode);
            row.createCell((short) 2).setCellValue(branchName);
            row.createCell((short) 3).setCellValue(branchCode);
            row.createCell((short) 4).setCellValue(categoryName);
            row.createCell((short) 5).setCellValue(categoryCode);
            row.createCell((short) 6).setCellValue(itemtotal);
            row.createCell((short) 7).setCellValue(costprice);
            row.createCell((short) 8).setCellValue(accumDepr);
			row.createCell((short) 9).setCellValue(monthlyDepr);
			row.createCell((short) 10).setCellValue(nbv);
			row.createCell((short) 11).setCellValue(improvCostPrice);
			row.createCell((short) 12).setCellValue(improvAccumDepr);
			row.createCell((short) 13).setCellValue(improvmonthldepr);
			row.createCell((short) 14).setCellValue(improvNbv);
			row.createCell((short) 15).setCellValue(totalCost);
			row.createCell((short) 16).setCellValue(finaltotalAccum);
			row.createCell((short) 17).setCellValue(totalnbv); 

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