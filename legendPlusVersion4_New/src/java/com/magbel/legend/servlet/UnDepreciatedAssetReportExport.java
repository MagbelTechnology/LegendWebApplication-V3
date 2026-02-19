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
   
public class UnDepreciatedAssetReportExport extends HttpServlet
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
    if(report.equalsIgnoreCase("rptMenuBCFD")){fileName = branchCode+"By"+userName+"UnDepreciatedAssetReport.xlsx";}
    
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

        String categoryCode = request.getParameter("category");
     Report rep = new Report();
   System.out.println("<<<<<<branch_Id: "+branch_Id+"   categoryId: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate);
     String ColQuery = "";
     if(branch_Id.equals("0") && categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")){
  	   System.out.println("======>>>>>>>No Selection: ");
	     ColQuery ="select Asset_id,Description,Asset_Status,Last_Dep_Date,BRANCH_CODE,CATEGORY_CODE,Accum_dep,monthly_dep,Cost_Price,NBV,IMPROV_COST,"
	     		+ "IMPROV_MONTHLYDEP,IMPROV_ACCUMDEP,IMPROV_NBV,Date_purchased,Asset_User,Dep_Rate "
		     		+ "from am_asset where asset_id not in (select asset_id from monthly_depreciation_processing) "
		     		+ "and NBV > 10 and Asset_Status = 'ACTIVE'";  
      }
     
     
     if(branch_Id.equals("0")  && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")){
    	 System.out.println("======>>>>>>>Date Selected: ");	     
	     ColQuery ="select select Asset_id,Description,Asset_Status,Last_Dep_Date,BRANCH_CODE,CATEGORY_CODE,Accum_dep,monthly_dep,Cost_Price,NBV,IMPROV_COST,"
	     		+ "IMPROV_MONTHLYDEP,IMPROV_ACCUMDEP,IMPROV_NBV,Date_purchased,Asset_User,Dep_Rate "
		     		+ "from am_asset where asset_id not in (select asset_id from monthly_depreciation_processing where dep_date between ? and ?) "
		     		+ "and NBV > 10 and Asset_Status = 'ACTIVE'";  
	}      
	 if(!branch_Id.equals("0")  && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")){	   
	   System.out.println("======>>>>>>>Branch and Date Selected: ");
	     ColQuery ="select Asset_id,Description,Asset_Status,Last_Dep_Date,BRANCH_CODE,CATEGORY_CODE,Accum_dep,monthly_dep,Cost_Price,NBV,IMPROV_COST,"
	     		+ "IMPROV_MONTHLYDEP,IMPROV_ACCUMDEP,IMPROV_NBV,Date_purchased,Asset_User,Dep_Rate "
	     		+ "from am_asset where asset_id not in (select asset_id from monthly_depreciation_processing where dep_date between ? and ?) "
	     		+ "and NBV > 10 and Branch_Id = ? and Asset_Status = 'ACTIVE'";  
		}  
	 
	 if(!branch_Id.equals("0")  && categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")){	   
		   System.out.println("======>>>>>>>Branch Selected: ");
		     ColQuery ="select Asset_id,Description,Asset_Status,Last_Dep_Date,BRANCH_CODE,CATEGORY_CODE,Accum_dep,monthly_dep,Cost_Price,NBV,IMPROV_COST,"
		     		+ "IMPROV_MONTHLYDEP,IMPROV_ACCUMDEP,IMPROV_NBV,Date_purchased,Asset_User,Dep_Rate "
			     		+ "from am_asset where asset_id not in (select asset_id from monthly_depreciation_processing) "
			     		+ "and NBV > 10 and Branch_Id = ? and Asset_Status = 'ACTIVE'";   
			}      

	 if(branch_Id.equals("0")  && !categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")){	   
	   System.out.println("======>>>>>>>Category and Date Selected: ");
	     ColQuery ="select Asset_id,Description,Asset_Status,Last_Dep_Date,BRANCH_CODE,CATEGORY_CODE,Accum_dep,monthly_dep,Cost_Price,NBV,IMPROV_COST,"
	     		+ "IMPROV_MONTHLYDEP,IMPROV_ACCUMDEP,IMPROV_NBV,Date_purchased,Asset_User,Dep_Rate "
		     		+ "from am_asset where asset_id not in (select asset_id from monthly_depreciation_processing where dep_date between ? and ?) "
		     		+ "and NBV > 10 and Category_Id = ? and Asset_Status = 'ACTIVE'";    
		}      
	 
	 if(branch_Id.equals("0")  && !categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")){	   
		   System.out.println("======>>>>>>>Category Selected: ");
		     ColQuery ="select Asset_id,Description,Asset_Status,Last_Dep_Date,BRANCH_CODE,CATEGORY_CODE,Accum_dep,monthly_dep,Cost_Price,NBV,IMPROV_COST,"
		     		+ "IMPROV_MONTHLYDEP,IMPROV_ACCUMDEP,IMPROV_NBV,Date_purchased,Asset_User,Dep_Rate "
			     		+ "from am_asset where asset_id not in (select asset_id from monthly_depreciation_processing) "
			     		+ "and NBV > 10 and Category_Id = ? and Asset_Status = 'ACTIVE'";    
			}      
	 
   if(!branch_Id.equals("0")  && !categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")){
	   System.out.println("======>>>>>>>Branch and Category and Date Selected: ");
	     ColQuery ="select Asset_id,Description,Asset_Status,Last_Dep_Date,BRANCH_CODE,CATEGORY_CODE,Accum_dep,monthly_dep,Cost_Price,NBV,IMPROV_COST,"
	     		+ "IMPROV_MONTHLYDEP,IMPROV_ACCUMDEP,IMPROV_NBV,Date_purchased,Asset_User,Dep_Rate "
		     		+ "from am_asset where asset_id not in (select asset_id from monthly_depreciation_processing where dep_date between ? and ?) "
		     		+ "and NBV > 10 and Branch_Id = ? and Category_Id = ? and Asset_Status = 'ACTIVE'";    
		}      
   
   if(!branch_Id.equals("0")  && !categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")){
	   System.out.println("======>>>>>>>Branch and Category ");
	     ColQuery ="select Asset_id,Description,Asset_Status,Last_Dep_Date,BRANCH_CODE,CATEGORY_CODE,Accum_dep,monthly_dep,Cost_Price,NBV,IMPROV_COST,"
	     		+ "IMPROV_MONTHLYDEP,IMPROV_ACCUMDEP,IMPROV_NBV,Date_purchased,Asset_User,Dep_Rate "
		     		+ "from am_asset where asset_id not in (select asset_id from monthly_depreciation_processing) "
		     		+ "and NBV > 10 and Branch_Id = ? and Category_Id = ? and Asset_Status = 'ACTIVE'";    
		}      
  //	 System.out.println("======>>>>>>>ColQuery: "+ColQuery);
     java.util.ArrayList list =rep.getUnDepreciatedAssetRecords(ColQuery,branch_Id,categoryCode,FromDate,ToDate,asset_Id);
     System.out.println("======>>>>>>>list size: "+list.size()+"        =====report: "+report);
     if(list.size()!=0){
   	 if(report.equalsIgnoreCase("rptMenuBCFD")){
    	 SXSSFWorkbook workbook = new SXSSFWorkbook();
         Sheet sheet = workbook.createSheet("Demo");
         Row rowhead = sheet.createRow((int) 0);
         
         rowhead.createCell( 0).setCellValue("S/No.");
         rowhead.createCell( 1).setCellValue("Asset Id");
         rowhead.createCell( 2).setCellValue("Asset Description");
         rowhead.createCell( 3).setCellValue("Branch Name");
         rowhead.createCell( 4).setCellValue("Category Name");
         rowhead.createCell( 5).setCellValue("Accum Dep");
         rowhead.createCell( 6).setCellValue("Monthly Dep");
         rowhead.createCell( 7).setCellValue("Cost Price");
         rowhead.createCell( 8).setCellValue("NBV");
         rowhead.createCell( 9).setCellValue("Improv Cost");
         rowhead.createCell( 10).setCellValue("Improv Monthly Dep");
		 rowhead.createCell( 11).setCellValue("Improv Accum Dep");
		 rowhead.createCell( 12).setCellValue("Improv NBV");
		 rowhead.createCell( 13).setCellValue("Date Purchased");
		 rowhead.createCell( 14).setCellValue("Dep Rate");;
		 rowhead.createCell( 15).setCellValue("Last Depr Date");
		 rowhead.createCell( 16).setCellValue("Asset User");

     int i = 1;
     System.out.println("<<<<<<list.size(): "+list.size());
	 for(int k=0;k<list.size();k++)
     {
    	 com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);    	 
    	 
    	 	String assetId = newassettrans.getAssetId();
			String description = newassettrans.getDescription();
			branchCode = newassettrans.getBranchCode();
			categoryCode = newassettrans.getCategoryCode();
			double accumDepr = newassettrans.getAccumDep();
			double monthlyDepr = newassettrans.getMonthlyDep();
			double costPrice = newassettrans.getCostPrice();
			double nbv = newassettrans.getNbv();
			double improvCost = newassettrans.getImprovcostPrice();
			double improvMonthlyDep = newassettrans.getImprovmonthlyDep();
			double improvAccumDep = newassettrans.getImprovaccumDep();
			double improvNbv = newassettrans.getImprovnbv();
			String datePurchased = newassettrans.getDatepurchased() != null ? getDate(newassettrans.getDatepurchased()) : "";
			double depRate = newassettrans.getDepRate();
			String assetUser = newassettrans.getAssetUser();
			String lastDeprDate = newassettrans.getSpare1();
			String branchName = records.getCodeName("select BRANCH_Name from am_ad_branch where BRANCH_CODE = ? ",branchCode);
			String categoryName = records.getCodeName("select CATEGORY_Name from am_ad_category where CATEGORY_CODE = ? ",categoryCode);
			

			Row row = sheet.createRow((int) i);

			row.createCell((short) 0).setCellValue(i);
			row.createCell((short) 1).setCellValue(assetId);
            row.createCell((short) 2).setCellValue(description);
            row.createCell((short) 3).setCellValue(branchName);
            row.createCell((short) 4).setCellValue(categoryName);
            row.createCell((short) 5).setCellValue(accumDepr);
            row.createCell((short) 6).setCellValue(monthlyDepr);
            row.createCell((short) 7).setCellValue(costPrice);
            row.createCell((short) 8).setCellValue(nbv);
            row.createCell((short) 9).setCellValue(improvCost);
			row.createCell((short) 10).setCellValue(improvMonthlyDep);
			row.createCell((short) 11).setCellValue(improvAccumDep);
			row.createCell((short) 12).setCellValue(improvNbv);
			row.createCell((short) 13).setCellValue(datePurchased);
			row.createCell((short) 14).setCellValue(depRate);
			row.createCell((short) 15).setCellValue(lastDeprDate);
            row.createCell((short) 16).setCellValue(assetUser);

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