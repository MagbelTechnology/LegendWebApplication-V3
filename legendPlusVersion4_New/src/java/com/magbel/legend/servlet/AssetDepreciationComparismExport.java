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
   
public class AssetDepreciationComparismExport extends HttpServlet
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
    String dd = "";
    String mm = "";
    String yyyy = "";
    String Tdd = "";
    String Tmm = "";
    String Tyyyy = "";
    if(!FromDate.equals("")){
	yyyy = FromDate.substring(6, 10);
//	System.out.println("======>yyyy: "+yyyy);
	mm = FromDate.substring(3, 5);
//	System.out.println("======>mm: "+mm);
	dd = FromDate.substring(0, 2);
	FromDate = yyyy+"-"+mm+"-"+dd;
    }
//    System.out.println("======>FromDate: "+FromDate);
    if(!ToDate.equals("")){
	Tyyyy = ToDate.substring(6, 10);
//	System.out.println("======>Tyyyy: "+Tyyyy);
	Tmm = ToDate.substring(3, 5);
//	System.out.println("======>Tmm: "+Tmm);
	Tdd = ToDate.substring(0, 2);
	ToDate = Tyyyy+"-"+Tmm+"-"+Tdd;
    }
//    System.out.println("======>ToDate: "+ToDate);
    String dept_Code = request.getParameter("deptCode");
    String asset_Id = request.getParameter("assetId");
    String cdayVal = dd;
    String cmonthVal = mm;
    String cyearVal = yyyy;
    
    String pdayVal = Tdd;
    String pmonthVal = Tmm;
    String pyearVal = Tyyyy;
    
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
    String fileName = branchCode+"By"+userName+"DepreciationComparismReport.xlsx";    	
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

//        String categoryCode = request.getParameter("category");
     Report rep = new Report();
 //  System.out.println("<<<<<<branch_Id: "+branch_Id+"    branchCode: "+branchCode);
   
   String createquery = "drop table Depreciation_Comparism_Current "+
   " drop table Depreciation_Comparism_Previous "+
   "CREATE TABLE [dbo].[Depreciation_Comparism_Current]([Asset_Id] [varchar](50) NULL,[Branch_Code] [varchar](50) NULL, " +
   "[Category_Code] [varchar](50) NULL,[Asset_Status] [varchar](50) NULL,[Monthly_Dep] [decimal](18, 2) NULL,[Accum_Dep] [decimal](18, 2) NULL,"+
   "[Cost_Price] [decimal](18, 2) NULL,[NBV] [decimal](18, 2) NULL,"+
   "[IMPROV_Monthly_Dep] [decimal](18, 2) NULL,[IMPROV_Accum_Dep] [decimal](18, 2) NULL, "+
   "[IMPROV_COST] [decimal](18, 2) NULL,[IMPROV_NBV] [decimal](18, 2) NULL,"+
   "[day_val] [varchar](2) NULL,[month_val] [varchar](2) NULL,"+
   "[year_val] [varchar](4) NULL,[RecType] [varchar](50) NULL) ON [PRIMARY] "+
   "CREATE TABLE [dbo].[Depreciation_Comparism_Previous]([Asset_Id] [varchar](50) NULL,[Branch_Code] [varchar](50) NULL,"+
   "[Category_Code] [varchar](50) NULL,[Asset_Status] [varchar](50) NULL,[Monthly_Dep] [decimal](1"
   + "8, 2) NULL,"+
   "[Accum_Dep] [decimal](18, 2) NULL,[Cost_Price] [decimal](18, 2) NULL,[NBV] [decimal](18, 2) NULL,"+
   "[IMPROV_Monthly_Dep] [decimal](18, 2) NULL,[IMPROV_Accum_Dep] [decimal](18, 2) NULL, "+
   "[IMPROV_COST] [decimal](18, 2) NULL,[IMPROV_NBV] [decimal](18, 2) NULL,"+
   "[day_val] [varchar](2) NULL,[month_val] [varchar](2) NULL,[year_val] [varchar](4) NULL,"+
   "[RecType] [varchar](50) NULL) ON [PRIMARY] "+
   "insert into Depreciation_Comparism_Current select Asset_Id,Branch_Code,Category_code, Asset_Status, Monthly_Dep, "+
   "Accum_Dep, Cost_Price,NBV,IMPROV_MONTHLYDEP,IMPROV_AccumDep,IMPROV_COST,IMPROV_NBV, "+
   "day_val, month_val, year_val, 'CURRENT' AS RECTYPE "+
   "from AM_ASSET_DEPRECIATION_ARCHIVE where month_val = "+cmonthVal+" and year_val = "+cyearVal+" and Asset_Status = 'ACTIVE' "+
   "insert into Depreciation_Comparism_Previous select Asset_Id,Branch_Code,Category_code, Asset_Status, Monthly_Dep, "+
   "Accum_Dep, Cost_Price,NBV,IMPROV_MONTHLYDEP,IMPROV_AccumDep,IMPROV_COST,IMPROV_NBV, "+
   "day_val, month_val, year_val, 'PREVIOUS' AS RECTYPE "+
   "from AM_ASSET_DEPRECIATION_ARCHIVE where month_val = "+pmonthVal+" and year_val = "+pyearVal+" and Asset_Status = 'ACTIVE' ";
//   System.out.println("<<<<<<<createquery in create Depreciation_Comparism tables: "+createquery);  
   
   ad.updateAssetStatusChange(createquery);   
   
   
     String ColQuery = "";	     
	     ColQuery ="select a.Asset_Id, a.Branch_Code,a.Category_Code,a.Monthly_Dep,a.Accum_Dep,a.Cost_Price,a.NBV,\n" + 
	     		" a.IMPROV_Monthly_Dep,a.IMPROV_Accum_Dep,a.IMPROV_COST,a.IMPROV_NBV,b.Monthly_Dep AS Prev_Monthly_Dep,\n" + 
	     		" b.Accum_Dep AS Prev_Accum_Dep,b.Cost_Price AS Prev_Cost_Price,b.NBV AS Prev_NBV,a.Monthly_Dep-b.Monthly_Dep AS Monthly_Difference,\n" + 
	     		" a.Accum_Dep-b.Accum_Dep AS Accum_Difference,a.NBV-b.NBV AS NBV_Difference, b.IMPROV_Monthly_Dep AS Prev_IMPROV_Monthly_Dep,\n" + 
	     		" b.IMPROV_Accum_Dep AS Prev_IMPROV_Accum_Dep,b.IMPROV_COST AS Prev_IMPROV_COST,b.IMPROV_NBV AS Prev_IMPROV_NBV,\n" + 
	     		" a.IMPROV_Monthly_Dep-b.IMPROV_Monthly_Dep AS Prev_Monthly_Difference from Depreciation_Comparism_Current a, \n" + 
	     		" Depreciation_Comparism_Previous b  where a.Asset_Id = b.Asset_Id UNION select Asset_Id, Branch_Code,Category_Code,\n" + 
	     		" Monthly_Dep,Accum_Dep,Cost_Price,NBV,IMPROV_Monthly_Dep, IMPROV_Accum_Dep,IMPROV_COST,IMPROV_NBV,0.00 AS bMonthly_Dep,\n" + 
	     		" 0.00 AS bAccum_Dep, 0.00 AS bCost_Price,0.00 AS bNBV,Monthly_Dep AS Monthly_Difference,Accum_Dep AS Accum_Difference,\n" + 
	     		" NBV AS NBV_Difference, 0.00 AS Prev_IMPROV_Monthly_Dep,0.00 AS Prev_IMPROV_Accum_Dep,0.00 AS Prev_IMPROV_COST,0.00 AS Prev_IMPROV_NBV,\n" + 
	     		" IMPROV_Monthly_Dep AS Prev_Monthly_Difference from Depreciation_Comparism_Current where Asset_Id not in (select Asset_Id from Depreciation_Comparism_Previous)";	          
    
//   System.out.println("======>>>>>>>ColQuery: "+ColQuery);
     java.util.ArrayList list =rep.getAssetDepreciationComparismRecords(ColQuery);
//     System.out.println("======>>>>>>>list size: "+list.size());
     if(list.size()!=0){  
  
    	 SXSSFWorkbook workbook = new SXSSFWorkbook();
         Sheet sheet = workbook.createSheet("Demo");
         Row rowhead = sheet.createRow((int) 0);
         
         rowhead.createCell((short) 0).setCellValue("S/No.");
         rowhead.createCell((short) 1).setCellValue("ASSET ID");
         rowhead.createCell((short) 2).setCellValue("BRANCH CODE");
         rowhead.createCell((short) 3).setCellValue("CATEGORY CODE");
         rowhead.createCell((short) 4).setCellValue("COST PRICE");
         rowhead.createCell((short) 5).setCellValue("Monthly Dep");
         rowhead.createCell((short) 6).setCellValue("Accum Dep");
         rowhead.createCell((short) 7).setCellValue("NBV");
         rowhead.createCell((short) 8).setCellValue("IMPROVE COST");
         rowhead.createCell((short) 9).setCellValue("IMPROVE ACCUMDEP");
         rowhead.createCell((short) 10).setCellValue("IMPROVE MONTHLYDEP");
         rowhead.createCell((short) 11).setCellValue("IMPROVE NBV");
         rowhead.createCell((short) 12).setCellValue("Prev Monthly Deprec.");
         rowhead.createCell((short) 13).setCellValue("Prev Accum Deprec.");
         rowhead.createCell((short) 14).setCellValue("Prev Cost Price");
		 rowhead.createCell((short) 15).setCellValue("Prev NBV");
		 rowhead.createCell((short) 16).setCellValue("Monthly Difference");
		 rowhead.createCell((short) 17).setCellValue("Accum Difference");
		 rowhead.createCell((short) 18).setCellValue("NBV Difference");
		 rowhead.createCell((short) 19).setCellValue("Prev IMPROV Monthly Deprec.");
		 rowhead.createCell((short) 20).setCellValue("Prev IMPROV Accum_Deprec.");
		 rowhead.createCell((short) 21).setCellValue("Prev IMPROV Cost Price");
		 rowhead.createCell((short) 22).setCellValue("Prev IMPROV NBV");
		 rowhead.createCell((short) 23).setCellValue("Prev Monthly Difference");

     int i = 1;
     System.out.println("<<<<<<list.size(): "+list.size());
	 for(int k=0;k<list.size();k++)
     {
    	 com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);    	 
			String assetId =  newassettrans.getAssetId();
			String solId = newassettrans.getBranchCode();
			String categoryCode = newassettrans.getCategoryCode();
//			String branchName = records.getCodeName("select BRANCH_NAME from am_ad_branch where BRANCH_ID = "+branchId+"");
			double costprice = newassettrans.getCostPrice();
			double monthlyDepr = newassettrans.getMonthlyDep();
			double accumDepr = newassettrans.getAccumDep();
			double nbv = newassettrans.getNbv();
			double improvmonthldepr = newassettrans.getImprovmonthlyDep();
			double improvAccumDepr = newassettrans.getImprovaccumDep();
			double improvCostPrice = newassettrans.getImprovcostPrice();
			double improvNbv = newassettrans.getImprovnbv();

			double prevMonthlyDep = newassettrans.getPrevMonthlyDep();
			double prevAccumDep = newassettrans.getPrevAccumDep();
			double prevCostPrice = newassettrans.getPrevCostPrice();
			double prevNBV = newassettrans.getPrevNBV();
			double monthlyDifference = newassettrans.getMonthlyDifference();
			double accumDifference = newassettrans.getAccumDifference();
			double nbvDifference = newassettrans.getNbvDifference();
			double prevIMPROVMonthlyDep = newassettrans.getPrevIMPROVMonthlyDep();
			double prevIMPROVAccumDep = newassettrans.getPrevIMPROVAccumDep();
			double prevIMPROVCostPrice = newassettrans.getPrevIMPROVCostPrice();
			double prevIMPROVNBV = newassettrans.getPrevIMPROVNBV();
			double prevMonthlyDifference = newassettrans.getPrevMonthlyDifference();
			Row row = sheet.createRow((int) i);

			row.createCell((short) 0).setCellValue(i);
			row.createCell((short) 1).setCellValue(assetId);
            row.createCell((short) 2).setCellValue(solId);
            row.createCell((short) 3).setCellValue(categoryCode);
            row.createCell((short) 4).setCellValue(costprice);
            row.createCell((short) 5).setCellValue(monthlyDepr);
            row.createCell((short) 6).setCellValue(accumDepr);
            row.createCell((short) 7).setCellValue(nbv);
            row.createCell((short) 8).setCellValue(improvCostPrice);
            row.createCell((short) 9).setCellValue(improvmonthldepr);
            row.createCell((short) 10).setCellValue(improvAccumDepr);
            row.createCell((short) 11).setCellValue(improvNbv);
            row.createCell((short) 12).setCellValue(prevMonthlyDep); 
            row.createCell((short) 13).setCellValue(prevAccumDep); 
            row.createCell((short) 14).setCellValue(prevCostPrice);
            row.createCell((short) 15).setCellValue(prevNBV);
			row.createCell((short) 16).setCellValue(monthlyDifference);
			row.createCell((short) 17).setCellValue(accumDifference);
			row.createCell((short) 18).setCellValue(nbvDifference);
			row.createCell((short) 19).setCellValue(prevIMPROVMonthlyDep);
			row.createCell((short) 20).setCellValue(prevIMPROVAccumDep);
			row.createCell((short) 21).setCellValue(prevIMPROVCostPrice);
			row.createCell((short) 22).setCellValue(prevIMPROVNBV);
			row.createCell((short) 23).setCellValue(prevMonthlyDifference);
			//System.out.println("======>assetId====: "+assetId+"  Index: "+i);

            i++;
//            System.out.println("we are here");
     }
	 System.out.println("we are here 2");
	   OutputStream stream = response.getOutputStream();
         workbook.write(stream);
         stream.close();
 //        workbook.close();  
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