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
   
public class AssetManagementExport extends HttpServlet
{
	private EmailSmsServiceBus mail ;
	private AssetRecordsBean ad;
	private ApprovalRecords records;
   public void doPost(HttpServletRequest request, 
    HttpServletResponse response)
      throws ServletException, IOException
     
   {
	 String userClass = (String) request.getSession().getAttribute("UserClass");
	 
	 if (!userClass.equals("NULL") || userClass!=null){
//	   PrintWriter out = response.getWriter();
//    OutputStream out = null; 
	mail= new EmailSmsServiceBus();
	records = new ApprovalRecords();
    String branch_Code = request.getParameter("initiatorSOLID");
    String branch_Id = request.getParameter("branch");
    //String branchCode = request.getParameter("BRANCH_CODE");
    System.out.println("<<<<<<branch_Id: "+branch_Id);
    String branchCode = "";
    if(!branch_Id.equals("0")){
    	branchCode = records.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID = ? ",branch_Id);
    }
    System.out.println("<<<<<<branch_Code: "+branch_Code);
    String userName = request.getParameter("userName");
    String fileName = branch_Code+"By"+userName+"AssetManagementReport.xlsx";    	
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
   System.out.println("<<<<<<branch_Id: "+branch_Id+"    categoryCode: "+categoryCode+"  branchCode: "+branchCode);
     String ColQuery = "";
     if(!branch_Id.equals("0")  && !categoryCode.equals("0")){
    	 System.out.println("======>>>>>>>Branch and Category Selected: ");
	     ColQuery ="SELECT comp.company_name,a.Asset_id,a.Description,b.BRANCH_NAME,c.category_name,e.Dept_name,a.SBU_CODE,a.Accum_dep,"
	 + "a.monthly_dep,a.Cost_Price,d.DEPCHARGETODATE,a.NBV,a.IMPROV_COST,a.IMPROV_ACCUMDEP,a.IMPROV_MONTHLYDEP,a.IMPROV_NBV,a.Cost_Price + a.IMPROV_COST AS Total_Cost_Price,"
	 + "a.TOTAL_NBV,a.Date_purchased,c.Dep_rate,c.Accum_Dep_ledger,c.Dep_ledger,c.Asset_Ledger,c.gl_account,a.Branch_ID,b.BRANCH_CODE,c.category_code,a.Effective_Date" 
     + " FROM am_ad_branch b INNER JOIN am_Asset a ON b.BRANCH_CODE = a.BRANCH_CODE"
     + " INNER JOIN am_ad_category c ON a.CATEGORY_CODE = c.category_code"
     + " LEFT OUTER JOIN DepreciationChargeToDate d ON a.Asset_id = d.Asset_id"
     + " LEFT OUTER JOIN am_ad_department e ON a.DEPT_CODE = e.DEPT_CODE,am_gb_company comp"
+ " WHERE a.Asset_Status = 'ACTIVE' and a.Cost_Price > (comp.old_threshhold-0.01) AND a.branch_id = ? AND a.CATEGORY_CODE = ?"
+ " ORDER BY a.BRANCH_CODE ASC,a.CATEGORY_CODE ASC ";
	}      
	 if(branch_Id.equals("0")  && !categoryCode.equals("0")){	   
	   System.out.println("======>>>>>>>Category Selected: ");
     ColQuery ="SELECT comp.company_name,a.Asset_id,a.Description,b.BRANCH_NAME,c.category_name,e.Dept_name,a.SBU_CODE,a.Accum_dep,"
	 + "a.monthly_dep,a.Cost_Price,d.DEPCHARGETODATE,a.NBV,a.IMPROV_COST,a.IMPROV_ACCUMDEP,a.IMPROV_MONTHLYDEP,a.IMPROV_NBV,a.Cost_Price + a.IMPROV_COST AS Total_Cost_Price,"
	 + "a.TOTAL_NBV,a.Date_purchased,c.Dep_rate,c.Accum_Dep_ledger,c.Dep_ledger,c.Asset_Ledger,c.gl_account,a.Branch_ID,b.BRANCH_CODE,c.category_code,a.Effective_Date" 
     + " FROM am_ad_branch b INNER JOIN am_Asset a ON b.BRANCH_CODE = a.BRANCH_CODE"
     + " INNER JOIN am_ad_category c ON a.CATEGORY_CODE = c.category_code"
     + " LEFT OUTER JOIN DepreciationChargeToDate d ON a.Asset_id = d.Asset_id"
     + " LEFT OUTER JOIN am_ad_department e ON a.DEPT_CODE = e.DEPT_CODE,am_gb_company comp"
+ " WHERE a.Asset_Status = 'ACTIVE' and a.Cost_Price > (comp.old_threshhold-0.01) AND a.CATEGORY_CODE = ? "
+ " ORDER BY a.BRANCH_CODE ASC,a.CATEGORY_CODE ASC ";
   }
	 if(!branch_Id.equals("0")  && categoryCode.equals("0")){	   
	   System.out.println("======>>>>>>>Branch Selected: ");
	     ColQuery ="SELECT comp.company_name,a.Asset_id,a.Description,b.BRANCH_NAME,c.category_name,e.Dept_name,a.SBU_CODE,a.Accum_dep,"
	 + "a.monthly_dep,a.Cost_Price,d.DEPCHARGETODATE,a.NBV,a.IMPROV_COST,a.IMPROV_ACCUMDEP,a.IMPROV_MONTHLYDEP,a.IMPROV_NBV,a.Cost_Price + a.IMPROV_COST AS Total_Cost_Price,"
	 + "a.TOTAL_NBV,a.Date_purchased,c.Dep_rate,c.Accum_Dep_ledger,c.Dep_ledger,c.Asset_Ledger,c.gl_account,a.Branch_ID,b.BRANCH_CODE,c.category_code,a.Effective_Date" 
     + " FROM am_ad_branch b INNER JOIN am_Asset a ON b.BRANCH_CODE = a.BRANCH_CODE"
     + " INNER JOIN am_ad_category c ON a.CATEGORY_CODE = c.category_code"
     + " LEFT OUTER JOIN DepreciationChargeToDate d ON a.Asset_id = d.Asset_id"
     + " LEFT OUTER JOIN am_ad_department e ON a.DEPT_CODE = e.DEPT_CODE,am_gb_company comp"
+ " WHERE a.Asset_Status = 'ACTIVE' and a.Cost_Price > (comp.old_threshhold-0.01) AND a.branch_id = ? "
+ " ORDER BY a.BRANCH_CODE ASC,a.CATEGORY_CODE ASC ";
	}
   if(branch_Id.equals("0")  && categoryCode.equals("0")){
	   System.out.println("======>>>>>>>No Selection: ");
	     ColQuery ="SELECT comp.company_name,a.Asset_id,a.Description,b.BRANCH_NAME,c.category_name,e.Dept_name,a.SBU_CODE,a.Accum_dep,"
	 + "a.monthly_dep,a.Cost_Price,d.DEPCHARGETODATE,a.NBV,a.IMPROV_COST,a.IMPROV_ACCUMDEP,a.IMPROV_MONTHLYDEP,a.IMPROV_NBV,a.Cost_Price + a.IMPROV_COST AS Total_Cost_Price,"
	 + "a.TOTAL_NBV,a.Date_purchased,c.Dep_rate,c.Accum_Dep_ledger,c.Dep_ledger,c.Asset_Ledger,c.gl_account,a.Branch_ID,b.BRANCH_CODE,c.category_code,a.Effective_Date" 
     + " FROM am_ad_branch b INNER JOIN am_Asset a ON b.BRANCH_CODE = a.BRANCH_CODE"
     + " INNER JOIN am_ad_category c ON a.CATEGORY_CODE = c.category_code"
     + " LEFT OUTER JOIN DepreciationChargeToDate d ON a.Asset_id = d.Asset_id"
     + " LEFT OUTER JOIN am_ad_department e ON a.DEPT_CODE = e.DEPT_CODE,am_gb_company comp"
+ " WHERE a.Asset_Status = 'ACTIVE' and a.Cost_Price > (comp.old_threshhold-0.01)  "
+ " ORDER BY a.BRANCH_CODE ASC,a.CATEGORY_CODE ASC ";
	}   
//   System.out.println("======>>>>>>>ColQuery: "+ColQuery);
     java.util.ArrayList list =rep.getAssetManagementRecords(ColQuery,branch_Id,categoryCode);
     if(list.size()!=0){
	 
    	 SXSSFWorkbook workbook = new SXSSFWorkbook();
         Sheet sheet = workbook.createSheet("Demo");
         Row rowhead = sheet.createRow((int) 0);
         
         rowhead.createCell((short) 0).setCellValue("ASSET ID");
         rowhead.createCell((short) 1).setCellValue("CATEGORY NAME");
         rowhead.createCell((short) 2).setCellValue("ASSET DESCRIPTION");
         rowhead.createCell((short) 3).setCellValue("DEPARTMENT NAME");
         rowhead.createCell((short) 4).setCellValue("SBU CODE");
         rowhead.createCell((short) 5).setCellValue("PURCHASE DATE");
         rowhead.createCell((short) 6).setCellValue("ACCUM DEPREC.");
		 rowhead.createCell((short) 7).setCellValue("MONTHLY DEPREC.");
		 rowhead.createCell((short) 8).setCellValue("COST PRICE");
		 rowhead.createCell((short) 9).setCellValue("NBV");
		 rowhead.createCell((short) 10).setCellValue("DEPR.CHARGE TODATE");
		 rowhead.createCell((short) 11).setCellValue("IMPROVE ACCUM");
		 rowhead.createCell((short) 12).setCellValue("IMPROVE MONTHLY");
		 rowhead.createCell((short) 13).setCellValue("IMPROVE COST PRICE");
		 rowhead.createCell((short) 14).setCellValue("IMPROVE NBV");
		 rowhead.createCell((short) 15).setCellValue("TOTAL COST PRICE");
		 rowhead.createCell((short) 16).setCellValue("TOTAL NBV");
		 rowhead.createCell((short) 17).setCellValue("DEPREC.START DATE ");
		 rowhead.createCell((short) 18).setCellValue("BRANCH CODE");

/*
     s.addCell(new Label(0, 0, "ASSET ID"));
     s.addCell(new Label(1, 0, "ASSET DESCRIPTION"));
     s.addCell(new Label(2, 0, "DEPARTMENT NAME"));
     s.addCell(new Label(3, 0, "SBU CODE"));
     s.addCell(new Label(4, 0, "PURCHASE DATE"));
     s.addCell(new Label(5, 0, "ACCUM DEPREC."));
     s.addCell(new Label(6, 0, "MONTHLY DEPREC."));
     s.addCell(new Label(7, 0, "COST PRICE"));
     s.addCell(new Label(8, 0, "NBV"));
     s.addCell(new Label(9, 0, "DEPR.CHARGE TODATE"));
     s.addCell(new Label(10, 0, "IMPROVE ACCUM"));
     s.addCell(new Label(11, 0, "IMPROVE MONTHLY"));
     s.addCell(new Label(12, 0, "IMPROVE COST PRICE"));
     s.addCell(new Label(13, 0, "IMPROVE NBV"));
     s.addCell(new Label(14, 0, "TOTAL COST PRICE"));
     s.addCell(new Label(15, 0, "TOTAL NBV"));
     s.addCell(new Label(16, 0, "DEPREC.START DATE "));
     s.addCell(new Label(17, 0, "BRANCH CODE")); */
     int i = 1;
//     System.out.println("<<<<<<list.size(): "+list.size());
	 for(int k=0;k<list.size();k++)
     {
    	 com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);    	 
			String assetId =  newassettrans.getAssetId();
			String oldassetId =  newassettrans.getOldassetId();
			String barcode =  newassettrans.getBarCode();
			String deptName =  newassettrans.getDeptCode();
			String categoryName = newassettrans.getCategoryName();
			String Description = newassettrans.getDescription();   
			String assetuser = newassettrans.getAssetUser();
			String assetcode = newassettrans.getAssetCode();
			branchCode = newassettrans.getBranchCode();
//			String branchName = records.getCodeName("select BRANCH_NAME from am_ad_branch where BRANCH_ID = "+branchId+"");
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
//			String categoryName = records.getCodeName("select BRANCH_NAME from am_ad_branch where BRANCH_ID = "+branchId+"");
			String batchId = newassettrans.getIntegrifyId();
			String sighted = newassettrans.getAssetsighted();
			String function = newassettrans.getAssetfunction();
			String comments = newassettrans.getAssetMaintenance();
			String make = newassettrans.getAssetMake();
			String lpo = newassettrans.getLpo();
			String vendoAcct = newassettrans.getVendorAC();
			String vendorId = newassettrans.getSupplierName();
//			String vendorName = records.getCodeName("select VENDOR_NAME from am_ad_vendor where VENDOR_ID = "+vendorId+"");
			String sbucode = newassettrans.getSbuCode();
			String purchaseDate = newassettrans.getDatepurchased();
			String depr_startDate = newassettrans.getEffectiveDate();
//			System.out.println("======>depr_startDate====: "+depr_startDate);
			String yyyy = depr_startDate.substring(0, 4);
//			System.out.println("======>yyyy: "+yyyy);
			String mm = depr_startDate.substring(5, 7);
//			System.out.println("======>mm: "+mm);
			String dd = depr_startDate.substring(8, 10);
			depr_startDate = dd+"/"+mm+"/"+yyyy;
			
			String pyyyy = purchaseDate.substring(0, 4);
			String pmm = purchaseDate.substring(5, 7);
			String pdd = purchaseDate.substring(8, 10);
			purchaseDate = pdd+"/"+pmm+"/"+pyyyy;
			
//			System.out.println("======>depr_startDate: "+depr_startDate);
			String deprciationDate = newassettrans.getPostingDate();
			
//			String newAccumDepr = Double.toString(accumDepr);
//			System.out.println("======>newAccumDepr: "+newAccumDepr);



			Row row = sheet.createRow((int) i);

            row.createCell((short) 0).setCellValue(assetId);
            row.createCell((short) 1).setCellValue(categoryName);
            row.createCell((short) 2).setCellValue(Description);
            row.createCell((short) 3).setCellValue(deptName);
            row.createCell((short) 4).setCellValue(sbucode);
            row.createCell((short) 5).setCellValue(purchaseDate);
            row.createCell((short) 6).setCellValue(accumDepr);
			row.createCell((short) 7).setCellValue(monthlyDepr);
			row.createCell((short) 8).setCellValue(costprice);
			row.createCell((short) 9).setCellValue(nbv);
			row.createCell((short) 10).setCellValue(depchargetoDate);
			row.createCell((short) 11).setCellValue(improvAccumDepr);
			row.createCell((short) 12).setCellValue(improvmonthldepr);
			row.createCell((short) 13).setCellValue(improvCostPrice);
			row.createCell((short) 14).setCellValue(improvNbv);
			row.createCell((short) 15).setCellValue(totalCost);
			row.createCell((short) 16).setCellValue(totalnbv);
			row.createCell((short) 17).setCellValue(depr_startDate);
			row.createCell((short) 18).setCellValue(branchCode);

		/*				 
           s.addCell(new Label(0, i,  assetId));
           s.addCell(new Label(1, i, Description));
           s.addCell(new Label(2, i, deptName));
           s.addCell(new Label(3, i, sbucode));
           s.addCell(new Label(4, i, purchaseDate));
           s.addCell(new Label(5, i, String.valueOf(accumDepr))); 
           s.addCell(new Label(6, i, String.valueOf(monthlyDepr)));
           s.addCell(new Label(7, i, String.valueOf(costprice)));
           s.addCell(new Label(8, i, String.valueOf(nbv)));
           s.addCell(new Label(9, i, String.valueOf(depchargetoDate)));
           s.addCell(new Label(10, i, String.valueOf(improvAccumDepr)));
           s.addCell(new Label(11, i, String.valueOf(improvmonthldepr)));
           s.addCell(new Label(12, i, String.valueOf(improvCostPrice)));
           s.addCell(new Label(13, i, String.valueOf(improvNbv)));
           s.addCell(new Label(14, i, String.valueOf(totalCost)));
           s.addCell(new Label(15, i, String.valueOf(totalnbv)));
           s.addCell(new Label(16, i, depr_startDate));
           s.addCell(new Label(17, i, branchCode));
      */     

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