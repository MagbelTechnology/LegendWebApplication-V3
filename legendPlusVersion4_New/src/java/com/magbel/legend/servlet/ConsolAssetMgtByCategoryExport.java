package com.magbel.legend.servlet;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
   







import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
   






























import magma.AssetRecordsBean;

import org.apache.poi.hssf.usermodel.HSSFRow;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
   
public class ConsolAssetMgtByCategoryExport extends HttpServlet
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
//    System.out.println("<<<<<<branch_Id: "+branch_Id);
    String branchCode = "";
    if(!branch_Id.equals("***")){
    	branchCode = records.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID = "+branch_Id+"");
    }
 //   System.out.println("<<<<<<branch_Code: "+branch_Code);
    String userName = request.getParameter("userName");
    String fileName = branch_Code+"By"+userName+"ConsolidateAssetManagementSummaryByCategoryReport.XLSX";    	
    String filePath = System.getProperty("user.home")+"Downloads";
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
     if(branch_Id.equals("***")  && categoryCode.equals("***")){
	     ColQuery ="SELECT am_gb_company.company_name AS am_gb_company_company_name,am_Asset.Asset_id AS am_Asset_Asset_id,am_Asset.Description AS am_Asset_Description,"
	     		+ "am_ad_branch.BRANCH_NAME AS am_ad_branch_BRANCH_NAME,am_ad_category.category_name AS am_ad_category_category_name,am_Asset.Accum_dep AS am_Asset_Accum_dep,"
	     		+ "am_Asset.monthly_dep AS am_Asset_monthly_dep,am_Asset.Cost_Price AS am_Asset_Cost_Price,am_Asset.NBV AS am_Asset_NBV,am_Asset.IMPROV_COST AS am_Asset_IMPROV_COST, "
	     		+ "am_Asset.IMPROV_ACCUMDEP AS am_Asset_IMPROV_ACCUMDEP,am_Asset.IMPROV_MONTHLYDEP AS am_Asset_IMPROV_MONTHLYDEP,am_Asset.IMPROV_NBV AS am_Asset_IMPROV_NBV,"
	     		+ "am_Asset.TOTAL_NBV AS am_Asset_TOTAL_NBV,am_Asset.Date_purchased AS am_Asset_Date_purchased,am_ad_category.Dep_rate AS am_ad_category_Dep_rate,"
	     		+ "am_ad_category.gl_account AS am_ad_category_gl_account,am_ad_branch.BRANCH_CODE AS am_ad_branch_BRANCH_CODE,"
	     		+ "am_Asset.Cost_Price + (coalesce(am_Asset.IMPROV_COST,0)) AS Final_Total_Cost,am_Asset.Accum_dep + (coalesce(am_Asset.IMPROV_ACCUMDEP,0)) AS Final_Total_Accum"
	     		+ "FROM dbo.am_ad_branch am_ad_branch INNER JOIN dbo.am_Asset am_Asset ON am_ad_branch.BRANCH_CODE = am_Asset.BRANCH_CODE "
	     		+ "INNER JOIN dbo.am_ad_category am_ad_category ON am_Asset.CATEGORY_CODE = am_ad_category.category_code,dbo.am_gb_company am_gb_company "
	     		+ "where am_Asset.Asset_Status = 'ACTIVE' "
	     		+ "UNION ALL "
	     		+ "SELECT am_gb_company.company_name AS am_gb_company_company_name,am_Asset_uncapitalized.Asset_id AS am_Asset_uncapitalized_Asset_id,am_Asset_uncapitalized.Description AS am_Asset_uncapitalized_Description,"
	     		+ "am_ad_branch.BRANCH_NAME AS am_ad_branch_BRANCH_NAME,am_ad_category.category_name AS am_ad_category_category_name,am_Asset_uncapitalized.Accum_dep AS am_Asset_uncapitalized_Accum_dep,"
	     		+ "am_Asset_uncapitalized.monthly_dep AS am_Asset_uncapitalized_monthly_dep,am_Asset_uncapitalized.Cost_Price AS am_Asset_uncapitalized_Cost_Price,am_Asset_uncapitalized.NBV AS am_Asset_uncapitalized_NBV,am_Asset_uncapitalized.IMPROV_COST AS am_Asset_uncapitalized_IMPROV_COST, "
	     		+ "am_Asset_uncapitalized.IMPROV_ACCUMDEP AS am_Asset_uncapitalized_IMPROV_ACCUMDEP,am_Asset_uncapitalized.IMPROV_MONTHLYDEP AS am_Asset_uncapitalized_IMPROV_MONTHLYDEP,am_Asset_uncapitalized.IMPROV_NBV AS am_Asset_uncapitalized_IMPROV_NBV,"
	     		+ "am_Asset_uncapitalized.TOTAL_NBV AS am_Asset_uncapitalized_TOTAL_NBV,am_Asset_uncapitalized.Date_purchased AS am_Asset_uncapitalized_Date_purchased,am_ad_category.Dep_rate AS am_ad_category_Dep_rate,"
	     		+ "am_ad_category.gl_account AS am_ad_category_gl_account,am_ad_branch.BRANCH_CODE AS am_ad_branch_BRANCH_CODE,"
	     		+ "am_Asset_uncapitalized.Cost_Price + (coalesce(am_Asset_uncapitalized.IMPROV_COST,0)) AS Final_Total_Cost,am_Asset_uncapitalized.Accum_dep + (coalesce(am_Asset_uncapitalized.IMPROV_ACCUMDEP,0)) AS Final_Total_Accum"
	     		+ "FROM dbo.am_ad_branch am_ad_branch INNER JOIN dbo.am_Asset_uncapitalized am_Asset_uncapitalized ON am_ad_branch.BRANCH_CODE = am_Asset_uncapitalized.BRANCH_CODE "
	     		+ "INNER JOIN dbo.am_ad_category am_ad_category ON am_Asset_uncapitalized.CATEGORY_CODE = am_ad_category.category_code,dbo.am_gb_company am_gb_company "
	     		+ "where am_Asset_uncapitalized.Asset_Status = 'ACTIVE' "
	     		+ "order by am_ad_category_category_name,am_ad_branch_BRANCH_CODE";
	}      
   if(!branch_Id.equals("***")  && categoryCode.equals("***")){
	     ColQuery ="SELECT am_gb_company.company_name AS am_gb_company_company_name,am_Asset.Asset_id AS am_Asset_Asset_id,am_Asset.Description AS am_Asset_Description,"
	     		+ "am_ad_branch.BRANCH_NAME AS am_ad_branch_BRANCH_NAME,am_ad_category.category_name AS am_ad_category_category_name,am_Asset.Accum_dep AS am_Asset_Accum_dep,"
	     		+ "am_Asset.monthly_dep AS am_Asset_monthly_dep,am_Asset.Cost_Price AS am_Asset_Cost_Price,am_Asset.NBV AS am_Asset_NBV,am_Asset.IMPROV_COST AS am_Asset_IMPROV_COST, "
	     		+ "am_Asset.IMPROV_ACCUMDEP AS am_Asset_IMPROV_ACCUMDEP,am_Asset.IMPROV_MONTHLYDEP AS am_Asset_IMPROV_MONTHLYDEP,am_Asset.IMPROV_NBV AS am_Asset_IMPROV_NBV,"
	     		+ "am_Asset.TOTAL_NBV AS am_Asset_TOTAL_NBV,am_Asset.Date_purchased AS am_Asset_Date_purchased,am_ad_category.Dep_rate AS am_ad_category_Dep_rate,"
	     		+ "am_ad_category.gl_account AS am_ad_category_gl_account,am_ad_branch.BRANCH_CODE AS am_ad_branch_BRANCH_CODE,"
	     		+ "am_Asset.Cost_Price + (coalesce(am_Asset.IMPROV_COST,0)) AS Final_Total_Cost,am_Asset.Accum_dep + (coalesce(am_Asset.IMPROV_ACCUMDEP,0)) AS Final_Total_Accum"
	     		+ "FROM dbo.am_ad_branch am_ad_branch INNER JOIN dbo.am_Asset am_Asset ON am_ad_branch.BRANCH_CODE = am_Asset.BRANCH_CODE "
	     		+ "INNER JOIN dbo.am_ad_category am_ad_category ON am_Asset.CATEGORY_CODE = am_ad_category.category_code,dbo.am_gb_company am_gb_company "
	     		+ "where am_Asset.Asset_Status = 'ACTIVE' AND am_ad_branch.branch_id = "+branch_Id+" "
	     		+ "UNION ALL "
	     		+ "SELECT am_gb_company.company_name AS am_gb_company_company_name,am_Asset_uncapitalized.Asset_id AS am_Asset_uncapitalized_Asset_id,am_Asset_uncapitalized.Description AS am_Asset_uncapitalized_Description,"
	     		+ "am_ad_branch.BRANCH_NAME AS am_ad_branch_BRANCH_NAME,am_ad_category.category_name AS am_ad_category_category_name,am_Asset_uncapitalized.Accum_dep AS am_Asset_uncapitalized_Accum_dep,"
	     		+ "am_Asset_uncapitalized.monthly_dep AS am_Asset_uncapitalized_monthly_dep,am_Asset_uncapitalized.Cost_Price AS am_Asset_uncapitalized_Cost_Price,am_Asset_uncapitalized.NBV AS am_Asset_uncapitalized_NBV,am_Asset_uncapitalized.IMPROV_COST AS am_Asset_uncapitalized_IMPROV_COST, "
	     		+ "am_Asset_uncapitalized.IMPROV_ACCUMDEP AS am_Asset_uncapitalized_IMPROV_ACCUMDEP,am_Asset_uncapitalized.IMPROV_MONTHLYDEP AS am_Asset_uncapitalized_IMPROV_MONTHLYDEP,am_Asset_uncapitalized.IMPROV_NBV AS am_Asset_uncapitalized_IMPROV_NBV,"
	     		+ "am_Asset_uncapitalized.TOTAL_NBV AS am_Asset_uncapitalized_TOTAL_NBV,am_Asset_uncapitalized.Date_purchased AS am_Asset_uncapitalized_Date_purchased,am_ad_category.Dep_rate AS am_ad_category_Dep_rate,"
	     		+ "am_ad_category.gl_account AS am_ad_category_gl_account,am_ad_branch.BRANCH_CODE AS am_ad_branch_BRANCH_CODE,"
	     		+ "am_Asset_uncapitalized.Cost_Price + (coalesce(am_Asset_uncapitalized.IMPROV_COST,0)) AS Final_Total_Cost,am_Asset_uncapitalized.Accum_dep + (coalesce(am_Asset_uncapitalized.IMPROV_ACCUMDEP,0)) AS Final_Total_Accum"
	     		+ "FROM dbo.am_ad_branch am_ad_branch INNER JOIN dbo.am_Asset_uncapitalized am_Asset_uncapitalized ON am_ad_branch.BRANCH_CODE = am_Asset_uncapitalized.BRANCH_CODE "
	     		+ "INNER JOIN dbo.am_ad_category am_ad_category ON am_Asset_uncapitalized.CATEGORY_CODE = am_ad_category.category_code,dbo.am_gb_company am_gb_company "
	     		+ "where am_Asset_uncapitalized.Asset_Status = 'ACTIVE' AND am_ad_branch.branch_id = "+branch_Id+" "
	     		+ "order by am_ad_category_category_name,am_ad_branch_BRANCH_CODE ";
   }
   if(branch_Id.equals("***")  && !categoryCode.equals("***")){
	     ColQuery ="SELECT am_gb_company.company_name AS am_gb_company_company_name,am_Asset.Asset_id AS am_Asset_Asset_id,am_Asset.Description AS am_Asset_Description,"
	     		+ "am_ad_branch.BRANCH_NAME AS am_ad_branch_BRANCH_NAME,am_ad_category.category_name AS am_ad_category_category_name,am_Asset.Accum_dep AS am_Asset_Accum_dep,"
	     		+ "am_Asset.monthly_dep AS am_Asset_monthly_dep,am_Asset.Cost_Price AS am_Asset_Cost_Price,am_Asset.NBV AS am_Asset_NBV,am_Asset.IMPROV_COST AS am_Asset_IMPROV_COST, "
	     		+ "am_Asset.IMPROV_ACCUMDEP AS am_Asset_IMPROV_ACCUMDEP,am_Asset.IMPROV_MONTHLYDEP AS am_Asset_IMPROV_MONTHLYDEP,am_Asset.IMPROV_NBV AS am_Asset_IMPROV_NBV,"
	     		+ "am_Asset.TOTAL_NBV AS am_Asset_TOTAL_NBV,am_Asset.Date_purchased AS am_Asset_Date_purchased,am_ad_category.Dep_rate AS am_ad_category_Dep_rate,"
	     		+ "am_ad_category.gl_account AS am_ad_category_gl_account,am_ad_branch.BRANCH_CODE AS am_ad_branch_BRANCH_CODE,"
	     		+ "am_Asset.Cost_Price + (coalesce(am_Asset.IMPROV_COST,0)) AS Final_Total_Cost,am_Asset.Accum_dep + (coalesce(am_Asset.IMPROV_ACCUMDEP,0)) AS Final_Total_Accum"
	     		+ "FROM dbo.am_ad_branch am_ad_branch INNER JOIN dbo.am_Asset am_Asset ON am_ad_branch.BRANCH_CODE = am_Asset.BRANCH_CODE "
	     		+ "INNER JOIN dbo.am_ad_category am_ad_category ON am_Asset.CATEGORY_CODE = am_ad_category.category_code,dbo.am_gb_company am_gb_company "
	     		+ "where am_Asset.Asset_Status = 'ACTIVE' AND am_ad_category.category_id = "+categoryCode+" "
	     		+ "UNION ALL "
	     		+ "SELECT am_gb_company.company_name AS am_gb_company_company_name,am_Asset_uncapitalized.Asset_id AS am_Asset_uncapitalized_Asset_id,am_Asset_uncapitalized.Description AS am_Asset_uncapitalized_Description,"
	     		+ "am_ad_branch.BRANCH_NAME AS am_ad_branch_BRANCH_NAME,am_ad_category.category_name AS am_ad_category_category_name,am_Asset_uncapitalized.Accum_dep AS am_Asset_uncapitalized_Accum_dep,"
	     		+ "am_Asset_uncapitalized.monthly_dep AS am_Asset_uncapitalized_monthly_dep,am_Asset_uncapitalized.Cost_Price AS am_Asset_uncapitalized_Cost_Price,am_Asset_uncapitalized.NBV AS am_Asset_uncapitalized_NBV,am_Asset_uncapitalized.IMPROV_COST AS am_Asset_uncapitalized_IMPROV_COST, "
	     		+ "am_Asset_uncapitalized.IMPROV_ACCUMDEP AS am_Asset_uncapitalized_IMPROV_ACCUMDEP,am_Asset_uncapitalized.IMPROV_MONTHLYDEP AS am_Asset_uncapitalized_IMPROV_MONTHLYDEP,am_Asset_uncapitalized.IMPROV_NBV AS am_Asset_uncapitalized_IMPROV_NBV,"
	     		+ "am_Asset_uncapitalized.TOTAL_NBV AS am_Asset_uncapitalized_TOTAL_NBV,am_Asset_uncapitalized.Date_purchased AS am_Asset_uncapitalized_Date_purchased,am_ad_category.Dep_rate AS am_ad_category_Dep_rate,"
	     		+ "am_ad_category.gl_account AS am_ad_category_gl_account,am_ad_branch.BRANCH_CODE AS am_ad_branch_BRANCH_CODE,"
	     		+ "am_Asset_uncapitalized.Cost_Price + (coalesce(am_Asset_uncapitalized.IMPROV_COST,0)) AS Final_Total_Cost,am_Asset_uncapitalized.Accum_dep + (coalesce(am_Asset_uncapitalized.IMPROV_ACCUMDEP,0)) AS Final_Total_Accum"
	     		+ "FROM dbo.am_ad_branch am_ad_branch INNER JOIN dbo.am_Asset_uncapitalized am_Asset_uncapitalized ON am_ad_branch.BRANCH_CODE = am_Asset_uncapitalized.BRANCH_CODE "
	     		+ "INNER JOIN dbo.am_ad_category am_ad_category ON am_Asset_uncapitalized.CATEGORY_CODE = am_ad_category.category_code,dbo.am_gb_company am_gb_company "
	     		+ "where am_Asset_uncapitalized.Asset_Status = 'ACTIVE' AND am_ad_category.category_id = "+categoryCode+" "
	     		+ "order by am_ad_category_category_name,am_ad_branch_BRANCH_CODE";
	}
   if(!branch_Id.equals("***")  && !categoryCode.equals("***")){
	     ColQuery ="SELECT am_gb_company.company_name AS am_gb_company_company_name,am_Asset.Asset_id AS am_Asset_Asset_id,am_Asset.Description AS am_Asset_Description,"
	     		+ "am_ad_branch.BRANCH_NAME AS am_ad_branch_BRANCH_NAME,am_ad_category.category_name AS am_ad_category_category_name,am_Asset.Accum_dep AS am_Asset_Accum_dep,"
	     		+ "am_Asset.monthly_dep AS am_Asset_monthly_dep,am_Asset.Cost_Price AS am_Asset_Cost_Price,am_Asset.NBV AS am_Asset_NBV,am_Asset.IMPROV_COST AS am_Asset_IMPROV_COST, "
	     		+ "am_Asset.IMPROV_ACCUMDEP AS am_Asset_IMPROV_ACCUMDEP,am_Asset.IMPROV_MONTHLYDEP AS am_Asset_IMPROV_MONTHLYDEP,am_Asset.IMPROV_NBV AS am_Asset_IMPROV_NBV,"
	     		+ "am_Asset.TOTAL_NBV AS am_Asset_TOTAL_NBV,am_Asset.Date_purchased AS am_Asset_Date_purchased,am_ad_category.Dep_rate AS am_ad_category_Dep_rate,"
	     		+ "am_ad_category.gl_account AS am_ad_category_gl_account,am_ad_branch.BRANCH_CODE AS am_ad_branch_BRANCH_CODE,"
	     		+ "am_Asset.Cost_Price + (coalesce(am_Asset.IMPROV_COST,0)) AS Final_Total_Cost,am_Asset.Accum_dep + (coalesce(am_Asset.IMPROV_ACCUMDEP,0)) AS Final_Total_Accum"
	     		+ "FROM dbo.am_ad_branch am_ad_branch INNER JOIN dbo.am_Asset am_Asset ON am_ad_branch.BRANCH_CODE = am_Asset.BRANCH_CODE "
	     		+ "INNER JOIN dbo.am_ad_category am_ad_category ON am_Asset.CATEGORY_CODE = am_ad_category.category_code,dbo.am_gb_company am_gb_company"
	     		+ "where am_Asset.Asset_Status = 'ACTIVE' AND am_ad_branch.branch_id = "+branch_Id+" AND am_ad_category.category_id = "+categoryCode+""
	     		+ "UNION ALL"
	     		+ "SELECT am_gb_company.company_name AS am_gb_company_company_name,am_Asset_uncapitalized.Asset_id AS am_Asset_uncapitalized_Asset_id,am_Asset_uncapitalized.Description AS am_Asset_uncapitalized_Description,"
	     		+ "am_ad_branch.BRANCH_NAME AS am_ad_branch_BRANCH_NAME,am_ad_category.category_name AS am_ad_category_category_name,am_Asset_uncapitalized.Accum_dep AS am_Asset_uncapitalized_Accum_dep,"
	     		+ "am_Asset_uncapitalized.monthly_dep AS am_Asset_uncapitalized_monthly_dep,am_Asset_uncapitalized.Cost_Price AS am_Asset_uncapitalized_Cost_Price,am_Asset_uncapitalized.NBV AS am_Asset_uncapitalized_NBV,am_Asset_uncapitalized.IMPROV_COST AS am_Asset_uncapitalized_IMPROV_COST, "
	     		+ "am_Asset_uncapitalized.IMPROV_ACCUMDEP AS am_Asset_uncapitalized_IMPROV_ACCUMDEP,am_Asset_uncapitalized.IMPROV_MONTHLYDEP AS am_Asset_uncapitalized_IMPROV_MONTHLYDEP,am_Asset_uncapitalized.IMPROV_NBV AS am_Asset_uncapitalized_IMPROV_NBV,"
	     		+ "am_Asset_uncapitalized.TOTAL_NBV AS am_Asset_uncapitalized_TOTAL_NBV,am_Asset_uncapitalized.Date_purchased AS am_Asset_uncapitalized_Date_purchased,am_ad_category.Dep_rate AS am_ad_category_Dep_rate,"
	     		+ "am_ad_category.gl_account AS am_ad_category_gl_account,am_ad_branch.BRANCH_CODE AS am_ad_branch_BRANCH_CODE,"
	     		+ "am_Asset_uncapitalized.Cost_Price + (coalesce(am_Asset_uncapitalized.IMPROV_COST,0)) AS Final_Total_Cost,am_Asset_uncapitalized.Accum_dep + (coalesce(am_Asset_uncapitalized.IMPROV_ACCUMDEP,0)) AS Final_Total_Accum"
	     		+ "FROM dbo.am_ad_branch am_ad_branch INNER JOIN dbo.am_Asset_uncapitalized am_Asset_uncapitalized ON am_ad_branch.BRANCH_CODE = am_Asset_uncapitalized.BRANCH_CODE "
	     		+ "INNER JOIN dbo.am_ad_category am_ad_category ON am_Asset_uncapitalized.CATEGORY_CODE = am_ad_category.category_code,dbo.am_gb_company am_gb_company"
	     		+ "where am_Asset_uncapitalized.Asset_Status = 'ACTIVE' AND am_ad_branch.branch_id = "+branch_Id+" AND am_ad_category.category_id = "+categoryCode+""
	     		+ "order by am_ad_category_category_name,am_ad_branch_BRANCH_CODE";
	}   
//   System.out.println("======>>>>>>>ColQuery: "+ColQuery);
//     java.util.ArrayList list =rep.getAssetManagementRecords(ColQuery);
   java.util.ArrayList list =rep.getConsolidatedAssetManagementRecords(ColQuery,branch_Id,categoryCode);
     if(list.size()!=0){
     WritableWorkbook w = Workbook.createWorkbook(response.getOutputStream());
     WritableSheet s = w.createSheet("Demo", 0);

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
     s.addCell(new Label(17, 0, "BRANCH CODE"));
     int i = 1;
//     System.out.println("<<<<<<list.size(): "+list.size());
	 for(int k=0;k<list.size();k++)
     {
    	 com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);    	 
			String assetId =  newassettrans.getAssetId();
			String oldassetId =  newassettrans.getOldassetId();
			String barcode =  newassettrans.getBarCode();
			String deptName =  newassettrans.getDeptCode();
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
//			System.out.println("======>depr_startDate: "+depr_startDate);
			String deprciationDate = newassettrans.getPostingDate();
			
//			String newAccumDepr = Double.toString(accumDepr);
//			System.out.println("======>newAccumDepr: "+newAccumDepr);
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
           

            i++;
     }
     w.write();
     w.close();

 }
    } catch (Exception e)
    {
     throw new ServletException("Exception in Excel Sample Servlet", e);
    } finally
    {
//     if (out != null)
//      out.close();
    }
   }
   }
   public void doGet(HttpServletRequest request, 
		    HttpServletResponse response)
		      throws ServletException, IOException
		   {
	   doPost(request, response);
		   }
}