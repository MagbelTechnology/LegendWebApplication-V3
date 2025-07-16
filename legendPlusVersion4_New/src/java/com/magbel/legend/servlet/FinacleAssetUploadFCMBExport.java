package com.magbel.legend.servlet;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
   
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
   






import magma.AssetRecordsBean;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
   
public class FinacleAssetUploadFCMBExport extends HttpServlet
{
	private EmailSmsServiceBus mail ;
	private AssetRecordsBean ad;
	private ApprovalRecords records;
   public void doPost(HttpServletRequest request, 
    HttpServletResponse response)
      throws ServletException, IOException
   {
	   
//		Properties prop = new Properties();
//		File file = new File("C:\\Property\\LegendPlus.properties");
//		String uploadFormat = prop.getProperty("legacyUploadFormat");
	   
	   String userId =(String) request.getSession().getAttribute("CurrentUser");
	   String userClass = (String) request.getSession().getAttribute("UserClass");
//	   PrintWriter out = response.getWriter();
//    OutputStream out = null;
		mail= new EmailSmsServiceBus();
        records = new ApprovalRecords();
        String branchId = request.getParameter("branch");  
        String groupid = request.getParameter("groupid");
//        System.out.println("<<<<<<branchId: "+branchId+"    Group Id: "+groupid);
        if(branchId.equals("***")){branchId = records.getCodeName("select BRANCH from am_gb_User where USER_ID = "+userId+" ");}
        String branchCode = records.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID = "+branchId+" ");
//        String subjectTovat = records.getCodeName("select distinct Subject_TO_Vat from AM_ASSET where GROUP_ID = '"+groupid+"'");
//        String subjectTowhTax = records.getCodeName("select distinct Wh_Tax from AM_ASSET where GROUP_ID = '"+groupid+"'");
        String accountNumber = records.getCodeName("select distinct SUBSTRING(vendor_ac, 1,len(LTRIM(RTRIM(vendor_ac)))-1) from AM_ASSET where GROUP_ID = '"+groupid+"'");
        String supervisorNameQry = records.getCodeName("select distinct Subject_TO_Vat+':'+substring(Wh_Tax,1,1)+':'+coalesce(TIN,'N')+':'+coalesce(RCNo,'N') from AM_ASSET a, am_ad_vendor v where v.account_number like '%"+accountNumber+"%' and GROUP_ID = '"+groupid+"'  and v.Vendor_Status = 'ACTIVE'");
        System.out.println("<<<<<<supervisorNameQry: "+supervisorNameQry);
        String[] sprvResult = supervisorNameQry.split(":");
//        String[] sprvResult = (records.retrieve4Array(supervisorNameQry)).split(":");
        String subjectTovat = sprvResult[0];
        String subjectTowhTax = sprvResult[1];
        String TIN = sprvResult[2];
        String RCNo = sprvResult[3];
        String userName = records.getCodeName("SELECT USER_NAME FROM am_gb_User WHERE USER_ID = "+userId+"");
//        System.out.println("<<<<<<branch Id: "+branchId+"   branch_Code: "+branchCode+"  subjectTovat: "+subjectTovat+"  subjectTowhTax: "+subjectTowhTax+"  TIN: "+TIN+"    RCNo: "+RCNo);
        if(TIN.equalsIgnoreCase("N")){TIN = RCNo; subjectTovat = "N";}
        String fileName = branchCode+"By"+userName+"FinacleAssetExport.xls";
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
        String appName = records.getCodeName("SELECT APP_NAME+'-'+VERSION FROM AM_AD_LEGACY_SYS_CONFIG"); 
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition",
                "attachment; filename="+fileName+"");
        try
        {
        if (!userClass.equals("NULL") || userClass!=null){
            ad = new AssetRecordsBean();

//    if(exists==false){


//            String chargeYear = request.getParameter("reportYear");
//            String reportDate = request.getParameter("reportDate");
            String branch_Id = request.getParameter("branch");
            String tranId = request.getParameter("tranId");
            String categoryId = request.getParameter("category");
            String mails = request.getParameter("mails1");
            String subject = request.getParameter("subject1");
            String msgText   = request.getParameter("msgText1");
            String otherparam  = request.getParameter("otherparam");
            Report rep = new Report();
//   System.out.println("<<<<<<branch_Id====>: "+branch_Id+"    categoryId: "+categoryId+"  branchCode: "+branchCode+"  subjectTovat: "+subjectTovat+"  subjectTowhTax: "+subjectTowhTax);
            String ColQuery = ""; 
//            System.out.println("======>>>>>>>subjectTovat: "+subjectTovat+"   subjectTowhTax: "+subjectTowhTax);
            if(subjectTovat.equals("Y")  && subjectTowhTax.equals("S")){
            	System.out.println("===subjectTovat.equals('Y')  && subjectTowhTax.equals('S')");
//            	System.out.println("======>>>>>>>subjectTovat: "+subjectTovat+"   subjectTowhTax: "+subjectTowhTax);
                ColQuery ="SELECT DISTINCT (select c.iso_code +(select accronym from am_ad_ledger_type where series = substring(a.Asset_Ledger,1,1))+"+
                		"d.branch_code +	a.Asset_Ledger asd from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c, am_gb_company b "+
                		"where a.currency_id = c.currency_id and a.category_code = AM_ASSET.CATEGORY_CODE "+
                		"and d.branch_code = AM_ASSET.BRANCH_CODE) AS DR_ACCT,	'D' AS DR_CR, AM_ASSET.Cost_Price AS COST_PRICE, "+
                		"(coalesce(AM_ASSET.INTEGRIFY,'')+ '*'+AM_ASSET.Description) AS Description, '' AS E, '' AS F, "+
                		"AM_ASSET.Asset_id AS ASSET_ID, '' AS H, '' AS I, AM_ASSET.SBU_CODE AS SBU_CODE "+
                		"FROM "+
                		"am_ad_branch am_ad_branch INNER JOIN AM_ASSET AM_ASSET ON am_ad_branch.BRANCH_CODE = AM_ASSET.BRANCH_CODE "+
                		"INNER JOIN am_ad_category am_ad_category ON AM_ASSET.CATEGORY_CODE = am_ad_category.category_code "+
                		"INNER JOIN am_ad_department am_ad_department ON AM_ASSET.Dept_code = am_ad_department.Dept_code "+
                		"INNER JOIN am_asset_approval am_asset_approval ON CAST(AM_ASSET.GROUP_ID AS VARCHAR(50)) = am_asset_approval.batch_id "+
                		"WHERE AM_ASSET.Asset_Status = 'ACTIVE' AND AM_ASSET.GROUP_ID = ? "+
                		"UNION "+
                		"SELECT Vendor_AC AS DR_ACCT,'C' AS DR_CR,SUM(VATABLE_COST-VAT) AS COST_PRICE, (coalesce(INTEGRIFY,'')+'*'+Description) AS Description,'' AS E,'' AS F,'' AS ASSET_ID,'' AS H,'' AS I, "+
                		"SBU_CODE FROM AM_ASSET WHERE GROUP_ID = ? AND ASSET_STATUS = 'ACTIVE' GROUP BY Vendor_AC,DESCRIPTION,INTEGRIFY,SBU_CODE "+
                		"UNION "+
                		"SELECT DISTINCT ( select c.iso_code +(select accronym from am_ad_ledger_type where series = substring(b.Wht_Account,1,1))+ "+
                		"b.default_branch + b.Wht_Account asd from am_ad_category a,am_ad_branch d, "+ 
                		"AM_GB_CURRENCY_CODE c, am_gb_company b where a.currency_id = c.currency_id "+
                		"and a.category_code = s.CATEGORY_CODE and d.branch_code = ?)  AS DR_ACCT, "+
                		"'C' AS DR_CR,WH_TAX_AMOUNT AS COST_PRICE, (coalesce(s.INTEGRIFY,'')+ '*'+VENDOR_NAME) AS Description,'' AS E,'' AS F,'' AS ASSET_ID,'' AS H,'' AS I, "+
                		"SBU_CODE FROM AM_ASSET s, am_ad_vendor v WHERE GROUP_ID = ? "+
                		"AND S.Vendor_AC = v.ACCOUNT_NUMBER AND S.ASSET_STATUS = 'ACTIVE' "+
//                		"GROUP BY Vendor_AC,VENDOR_NAME,INTEGRIFY,SBU_CODE,CATEGORY_CODE,BRANCH_CODE "+
                		"UNION "+
                		"SELECT DISTINCT ( select c.iso_code +(select accronym from am_ad_ledger_type where series = substring(b.VAT_ACCOUNT,1,1))+ "+
                		"b.default_branch + b.VAT_ACCOUNT asd from am_ad_category a,am_ad_branch d, "+
                		"AM_GB_CURRENCY_CODE c, am_gb_company b where a.currency_id = c.currency_id "+
                		"and a.category_code = s.CATEGORY_CODE and d.branch_code = ?)  AS DR_ACCT, "+
                		"'C' AS DR_CR,VAT AS COST_PRICE, (coalesce(s.INTEGRIFY,'')+ '*'+VENDOR_NAME) AS Description,'' AS E,'' AS F,'' AS ASSET_ID,'' AS H,'' AS I, "+
                		"SBU_CODE FROM AM_ASSET s, am_ad_vendor v WHERE GROUP_ID = ? AND S.Vendor_AC = v.ACCOUNT_NUMBER AND s.ASSET_STATUS = 'ACTIVE' ";
//                		"GROUP BY Vendor_AC,VENDOR_NAME,INTEGRIFY,SBU_CODE,CATEGORY_CODE,BRANCH_CODE";
            }
            if(!subjectTovat.equals("Y")  && subjectTowhTax.equals("S")){  
            	System.out.println("===!subjectTovat.equals('Y')  && subjectTowhTax.equals('S')");
//            	System.out.println("======>>>>>>>subjectTovat: "+subjectTovat+"   subjectTowhTax: "+subjectTowhTax);
                ColQuery ="SELECT DISTINCT (select c.iso_code +(select accronym from am_ad_ledger_type where series = substring(a.Asset_Ledger,1,1))+"+
                		"d.branch_code +	a.Asset_Ledger asd from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c, am_gb_company b "+
                		"where a.currency_id = c.currency_id and a.category_code = AM_ASSET.CATEGORY_CODE "+
                		"and d.branch_code = AM_ASSET.BRANCH_CODE) AS DR_ACCT,	'D' AS DR_CR, AM_ASSET.Cost_Price AS COST_PRICE, "+
                		"(coalesce(AM_ASSET.INTEGRIFY,'')+ '*'+AM_ASSET.Description) AS Description, '' AS E, '' AS F, "+
                		"AM_ASSET.Asset_id AS ASSET_ID, '' AS H, '' AS I, AM_ASSET.SBU_CODE AS SBU_CODE "+
                		"FROM "+
                		"am_ad_branch am_ad_branch INNER JOIN AM_ASSET AM_ASSET ON am_ad_branch.BRANCH_CODE = AM_ASSET.BRANCH_CODE "+
                		"INNER JOIN am_ad_category am_ad_category ON AM_ASSET.CATEGORY_CODE = am_ad_category.category_code "+
                		"INNER JOIN am_ad_department am_ad_department ON AM_ASSET.Dept_code = am_ad_department.Dept_code "+
                		"INNER JOIN am_asset_approval am_asset_approval ON CAST(AM_ASSET.GROUP_ID AS VARCHAR(50)) = am_asset_approval.batch_id "+
                		"WHERE AM_ASSET.Asset_Status = 'ACTIVE' AND AM_ASSET.GROUP_ID = ? "+
                		"UNION "+
                		"SELECT Vendor_AC AS DR_ACCT,'C' AS DR_CR,SUM(VATABLE_COST-VAT) AS COST_PRICE, (coalesce(INTEGRIFY,'')+'*'+Description) AS Description,'' AS E,'' AS F,'' AS ASSET_ID,'' AS H,'' AS I, "+
                		"SBU_CODE FROM AM_ASSET WHERE GROUP_ID = ? AND ASSET_STATUS = 'ACTIVE' GROUP BY Vendor_AC,DESCRIPTION,INTEGRIFY,SBU_CODE "+
                		"UNION "+
                		"SELECT DISTINCT ( select c.iso_code +(select accronym from am_ad_ledger_type where series = substring(b.Fed_Wht_Account,1,1))+ "+
                		"b.default_branch + b.Fed_Wht_Account asd from am_ad_category a,am_ad_branch d, "+ 
                		"AM_GB_CURRENCY_CODE c, am_gb_company b where a.currency_id = c.currency_id "+
                		"and a.category_code = s.CATEGORY_CODE and d.branch_code = ?)  AS DR_ACCT, "+
                		"'C' AS DR_CR,WH_TAX_AMOUNT AS COST_PRICE, (coalesce(s.INTEGRIFY,'')+ '*'+VENDOR_NAME) AS Description,'' AS E,'' AS F,'' AS ASSET_ID,'' AS H,'' AS I, "+
                		"SBU_CODE FROM AM_ASSET s, am_ad_vendor v WHERE GROUP_ID = ? "+
                		"AND S.Vendor_AC = v.ACCOUNT_NUMBER AND s.ASSET_STATUS = 'ACTIVE' "+
//                		"GROUP BY Vendor_AC,VENDOR_NAME,INTEGRIFY,SBU_CODE,CATEGORY_CODE,BRANCH_CODE "+
                		"UNION "+
                		"SELECT DISTINCT ( select c.iso_code +(select accronym from am_ad_ledger_type where series = substring(b.VAT_ACCOUNT,1,1))+ "+
                		"b.default_branch + b.VAT_ACCOUNT asd from am_ad_category a,am_ad_branch d, "+
                		"AM_GB_CURRENCY_CODE c, am_gb_company b where a.currency_id = c.currency_id "+
                		"and a.category_code = s.CATEGORY_CODE and d.branch_code = ?)  AS DR_ACCT, "+
                		"'C' AS DR_CR,VAT AS COST_PRICE, (coalesce(s.INTEGRIFY,'')+ '*'+VENDOR_NAME) AS Description,'' AS E,'' AS F,'' AS ASSET_ID,'' AS H,'' AS I, "+
                		"SBU_CODE FROM AM_ASSET s, am_ad_vendor v WHERE GROUP_ID = ? AND S.Vendor_AC = v.ACCOUNT_NUMBER AND s.ASSET_STATUS = 'ACTIVE' ";
//                		"GROUP BY Vendor_AC,VENDOR_NAME,INTEGRIFY,SBU_CODE,CATEGORY_CODE,BRANCH_CODE";
            }
            if(subjectTovat.equals("Y")  && subjectTowhTax.equals("Y")){
            	System.out.println("===1 subjectTovat.equals('Y')  && !subjectTowhTax.equals('S')");
 //           	 System.out.println("======>>>>>>>subjectTovat: "+subjectTovat+"   subjectTowhTax: "+subjectTowhTax);
                ColQuery ="SELECT DISTINCT (select c.iso_code +(select accronym from am_ad_ledger_type where series = substring(a.Asset_Ledger,1,1))+"+
                		"d.branch_code +	a.Asset_Ledger asd from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c, am_gb_company b "+
                		"where a.currency_id = c.currency_id and a.category_code = AM_ASSET.CATEGORY_CODE "+
                		"and d.branch_code = AM_ASSET.BRANCH_CODE) AS DR_ACCT,	'D' AS DR_CR, AM_ASSET.Cost_Price AS COST_PRICE, "+
                		"(coalesce(AM_ASSET.INTEGRIFY,'')+ '*'+AM_ASSET.Description) AS Description, '' AS E, '' AS F, "+
                		"AM_ASSET.Asset_id AS ASSET_ID, '' AS H, '' AS I, AM_ASSET.SBU_CODE AS SBU_CODE "+
                		"FROM "+
                		"am_ad_branch am_ad_branch INNER JOIN AM_ASSET AM_ASSET ON am_ad_branch.BRANCH_CODE = AM_ASSET.BRANCH_CODE "+
                		"INNER JOIN am_ad_category am_ad_category ON AM_ASSET.CATEGORY_CODE = am_ad_category.category_code "+
                		"INNER JOIN am_ad_department am_ad_department ON AM_ASSET.Dept_code = am_ad_department.Dept_code "+
                		"INNER JOIN am_asset_approval am_asset_approval ON CAST(AM_ASSET.GROUP_ID AS VARCHAR(50)) = am_asset_approval.batch_id "+
                		"WHERE AM_ASSET.Asset_Status = 'ACTIVE' AND AM_ASSET.GROUP_ID = ? "+
                		"UNION "+
                		"SELECT Vendor_AC AS DR_ACCT,'C' AS DR_CR,SUM(VATABLE_COST-VAT) AS COST_PRICE, (coalesce(INTEGRIFY,'')+'*'+Description) AS Description,'' AS E,'' AS F,'' AS ASSET_ID,'' AS H,'' AS I, "+
                		"SBU_CODE FROM AM_ASSET WHERE GROUP_ID = ? AND ASSET_STATUS = 'ACTIVE' GROUP BY Vendor_AC,DESCRIPTION,INTEGRIFY,SBU_CODE "+
                		"UNION "+
                		"SELECT DISTINCT ( select c.iso_code +(select accronym from am_ad_ledger_type where series = substring(b.Fed_Wht_Account,1,1))+ "+
                		"b.default_branch + b.Fed_Wht_Account asd from am_ad_category a,am_ad_branch d, "+ 
                		"AM_GB_CURRENCY_CODE c, am_gb_company b where a.currency_id = c.currency_id "+
                		"and a.category_code = s.CATEGORY_CODE and d.branch_code = ?)  AS DR_ACCT, "+
                		"'C' AS DR_CR,WH_TAX_AMOUNT AS COST_PRICE, (coalesce(s.INTEGRIFY,'')+ '*'+VENDOR_NAME) AS Description,'' AS E,'' AS F,'' AS ASSET_ID,'' AS H,'' AS I, "+
                		"SBU_CODE FROM AM_ASSET s, am_ad_vendor v WHERE GROUP_ID = ? "+
                		"AND S.Vendor_AC = v.ACCOUNT_NUMBER AND s.ASSET_STATUS = 'ACTIVE' "+
//                		"GROUP BY Vendor_AC,VENDOR_NAME,INTEGRIFY,SBU_CODE,CATEGORY_CODE,BRANCH_CODE "+
                		"UNION "+
                		"SELECT DISTINCT ( select c.iso_code +(select accronym from am_ad_ledger_type where series = substring(b.VAT_ACCOUNT,1,1))+ "+
                		"b.default_branch + b.VAT_ACCOUNT asd from am_ad_category a,am_ad_branch d, "+
                		"AM_GB_CURRENCY_CODE c, am_gb_company b where a.currency_id = c.currency_id "+
                		"and a.category_code = s.CATEGORY_CODE and d.branch_code = ?)  AS DR_ACCT, "+
                		"'C' AS DR_CR,VAT AS COST_PRICE, (coalesce(s.INTEGRIFY,'')+ '*'+VENDOR_NAME) AS Description,'' AS E,'' AS F,'' AS ASSET_ID,'' AS H,'' AS I, "+
                		"SBU_CODE FROM AM_ASSET s, am_ad_vendor v WHERE GROUP_ID = ? AND S.Vendor_AC = v.ACCOUNT_NUMBER AND s.ASSET_STATUS = 'ACTIVE' ";
//                		"GROUP BY Vendor_AC,VENDOR_NAME,INTEGRIFY,SBU_CODE,CATEGORY_CODE,BRANCH_CODE";
            }
            if(!subjectTovat.equals("Y")  && subjectTowhTax.equals("F")){
            	System.out.println("===!subjectTovat.equals('Y')  && subjectTowhTax.equals('F')");
//            	System.out.println("======>>>>>>>subjectTovat: "+subjectTovat+"   subjectTowhTax: "+subjectTowhTax);
                ColQuery ="SELECT DISTINCT (select c.iso_code +(select accronym from am_ad_ledger_type where series = substring(a.Asset_Ledger,1,1))+"+
                		"d.branch_code +	a.Asset_Ledger asd from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c, am_gb_company b "+
                		"where a.currency_id = c.currency_id and a.category_code = AM_ASSET.CATEGORY_CODE "+
                		"and d.branch_code = AM_ASSET.BRANCH_CODE) AS DR_ACCT,	'D' AS DR_CR, AM_ASSET.Cost_Price AS COST_PRICE, "+
                		"(coalesce(AM_ASSET.INTEGRIFY,'')+ '*'+AM_ASSET.Description) AS Description, '' AS E, '' AS F, "+
                		"AM_ASSET.Asset_id AS ASSET_ID, '' AS H, '' AS I, AM_ASSET.SBU_CODE AS SBU_CODE "+
                		"FROM "+
                		"am_ad_branch am_ad_branch INNER JOIN AM_ASSET AM_ASSET ON am_ad_branch.BRANCH_CODE = AM_ASSET.BRANCH_CODE "+
                		"INNER JOIN am_ad_category am_ad_category ON AM_ASSET.CATEGORY_CODE = am_ad_category.category_code "+
                		"INNER JOIN am_ad_department am_ad_department ON AM_ASSET.Dept_code = am_ad_department.Dept_code "+
                		"INNER JOIN am_asset_approval am_asset_approval ON CAST(AM_ASSET.GROUP_ID AS VARCHAR(50)) = am_asset_approval.batch_id "+
                		"WHERE AM_ASSET.Asset_Status = 'ACTIVE' AND AM_ASSET.GROUP_ID = ? "+
                		"UNION "+
                		"SELECT Vendor_AC AS DR_ACCT,'C' AS DR_CR,SUM(VATABLE_COST-VAT) AS COST_PRICE, (coalesce(INTEGRIFY,'')+'*'+Description) AS Description,'' AS E,'' AS F,'' AS ASSET_ID,'' AS H,'' AS I, "+
                		"SBU_CODE FROM AM_ASSET WHERE GROUP_ID = ? AND ASSET_STATUS = 'ACTIVE' GROUP BY Vendor_AC,DESCRIPTION,INTEGRIFY,SBU_CODE "+
                		"UNION "+
                		"SELECT DISTINCT ( select c.iso_code +(select accronym from am_ad_ledger_type where series = substring(b.Fed_Wht_Account,1,1))+ "+
                		"b.default_branch + b.Fed_Wht_Account asd from am_ad_category a,am_ad_branch d, "+ 
                		"AM_GB_CURRENCY_CODE c, am_gb_company b where a.currency_id = c.currency_id "+
                		"and a.category_code = s.CATEGORY_CODE and d.branch_code = ?)  AS DR_ACCT, "+
                		"'C' AS DR_CR,WH_TAX_AMOUNT AS COST_PRICE, (coalesce(s.INTEGRIFY,'')+ '*'+VENDOR_NAME) AS Description,'' AS E,'' AS F,'' AS ASSET_ID,'' AS H,'' AS I, "+
                		"SBU_CODE FROM AM_ASSET s, am_ad_vendor v WHERE GROUP_ID = ? "+
                		"AND S.Vendor_AC = v.ACCOUNT_NUMBER AND s.ASSET_STATUS = 'ACTIVE' "+
//                		"GROUP BY Vendor_AC,VENDOR_NAME,INTEGRIFY,SBU_CODE,CATEGORY_CODE,BRANCH_CODE "+
                		"UNION "+
                		"SELECT DISTINCT ( select c.iso_code +(select accronym from am_ad_ledger_type where series = substring(b.VAT_ACCOUNT,1,1))+ "+
                		"b.default_branch + b.VAT_ACCOUNT asd from am_ad_category a,am_ad_branch d, "+
                		"AM_GB_CURRENCY_CODE c, am_gb_company b where a.currency_id = c.currency_id "+
                		"and a.category_code = s.CATEGORY_CODE and d.branch_code = ?)  AS DR_ACCT, "+
                		"'C' AS DR_CR,SUM(VAT) AS COST_PRICE, (coalesce(s.INTEGRIFY,'')+ '*'+VENDOR_NAME) AS Description,'' AS E,'' AS F,'' AS ASSET_ID,'' AS H,'' AS I, "+
                		"SBU_CODE FROM AM_ASSET s, am_ad_vendor v WHERE GROUP_ID = ? AND S.Vendor_AC = v.ACCOUNT_NUMBER AND s.ASSET_STATUS = 'ACTIVE' ";
//                		"GROUP BY Vendor_AC,VENDOR_NAME,INTEGRIFY,SBU_CODE,CATEGORY_CODE,BRANCH_CODE";
            }
            if(subjectTovat.equals("Y")  && subjectTowhTax.equals("F")){
            	System.out.println("===2 subjectTovat.equals('Y')  && subjectTowhTax.equals('F')");
//            	 System.out.println("======>>>>>>>subjectTovat: "+subjectTovat+"   subjectTowhTax: "+subjectTowhTax);
                ColQuery ="SELECT DISTINCT (select c.iso_code +(select accronym from am_ad_ledger_type where series = substring(a.Asset_Ledger,1,1))+"+
                		"d.branch_code +	a.Asset_Ledger asd from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c, am_gb_company b "+
                		"where a.currency_id = c.currency_id and a.category_code = AM_ASSET.CATEGORY_CODE "+
                		"and d.branch_code = AM_ASSET.BRANCH_CODE) AS DR_ACCT,	'D' AS DR_CR, AM_ASSET.Cost_Price AS COST_PRICE, "+
                		"(coalesce(AM_ASSET.INTEGRIFY,'')+ '*'+AM_ASSET.Description) AS Description, '' AS E, '' AS F, "+
                		"AM_ASSET.Asset_id AS ASSET_ID, '' AS H, '' AS I, AM_ASSET.SBU_CODE AS SBU_CODE "+
                		"FROM "+
                		"am_ad_branch am_ad_branch INNER JOIN AM_ASSET AM_ASSET ON am_ad_branch.BRANCH_CODE = AM_ASSET.BRANCH_CODE "+
                		"INNER JOIN am_ad_category am_ad_category ON AM_ASSET.CATEGORY_CODE = am_ad_category.category_code "+
                		"INNER JOIN am_ad_department am_ad_department ON AM_ASSET.Dept_code = am_ad_department.Dept_code "+
                		"INNER JOIN am_asset_approval am_asset_approval ON CAST(AM_ASSET.GROUP_ID AS VARCHAR(50)) = am_asset_approval.batch_id "+
                		"WHERE AM_ASSET.Asset_Status = 'ACTIVE' AND AM_ASSET.GROUP_ID = ? "+
                		"UNION "+
                		"SELECT Vendor_AC AS DR_ACCT,'C' AS DR_CR,SUM(VATABLE_COST-VAT) AS COST_PRICE, (coalesce(INTEGRIFY,'')+'*'+Description) AS Description,'' AS E,'' AS F,'' AS ASSET_ID,'' AS H,'' AS I, "+
                		"SBU_CODE FROM AM_ASSET WHERE GROUP_ID = ? AND ASSET_STATUS = 'ACTIVE' GROUP BY Vendor_AC,DESCRIPTION,INTEGRIFY,SBU_CODE "+
                		"UNION "+
                		"SELECT DISTINCT( select c.iso_code +(select accronym from am_ad_ledger_type where series = substring(b.Fed_Wht_Account,1,1))+ "+
                		"b.default_branch + b.Fed_Wht_Account asd from am_ad_category a,am_ad_branch d, "+ 
                		"AM_GB_CURRENCY_CODE c, am_gb_company b where a.currency_id = c.currency_id "+
                		"and a.category_code = s.CATEGORY_CODE and d.branch_code = ?)  AS DR_ACCT, "+
                		"'C' AS DR_CR,WH_TAX_AMOUNT AS COST_PRICE, (coalesce(s.INTEGRIFY,'')+ '*'+VENDOR_NAME) AS Description,'' AS E,'' AS F,'' AS ASSET_ID,'' AS H,'' AS I, "+
                		"SBU_CODE FROM AM_ASSET s, am_ad_vendor v WHERE GROUP_ID = ? "+
                		"AND S.Vendor_AC = v.ACCOUNT_NUMBER AND S.ASSET_STATUS = 'ACTIVE' "+
//                		"GROUP BY Vendor_AC,VENDOR_NAME,INTEGRIFY,SBU_CODE,CATEGORY_CODE,BRANCH_CODE "+
                		"UNION "+
                		"SELECT DISTINCT ( select c.iso_code +(select accronym from am_ad_ledger_type where series = substring(b.VAT_ACCOUNT,1,1))+ "+
                		"b.default_branch + b.VAT_ACCOUNT asd from am_ad_category a,am_ad_branch d, "+
                		"AM_GB_CURRENCY_CODE c, am_gb_company b where a.currency_id = c.currency_id "+
                		"and a.category_code = s.CATEGORY_CODE and d.branch_code = ?)  AS DR_ACCT, "+
                		"'C' AS DR_CR,VAT AS COST_PRICE, (coalesce(s.INTEGRIFY,'')+ '*'+VENDOR_NAME) AS Description,'' AS E,'' AS F,'' AS ASSET_ID,'' AS H,'' AS I, "+
                		"SBU_CODE FROM AM_ASSET s, am_ad_vendor v WHERE GROUP_ID = ? AND S.Vendor_AC = v.ACCOUNT_NUMBER AND S.ASSET_STATUS = 'ACTIVE' ";
//                		"GROUP BY Vendor_AC,VENDOR_NAME,INTEGRIFY,SBU_CODE,CATEGORY_CODE,BRANCH_CODE";
            }
            if(subjectTovat.equals("N")  && !subjectTowhTax.equals("N")){  
            	System.out.println("===!subjectTovat.equals('Y')  && subjectTowhTax.equals('S')");
//            	System.out.println("======>>>>>>>subjectTovat: "+subjectTovat+"   subjectTowhTax: "+subjectTowhTax);
                ColQuery ="SELECT DISTINCT (select c.iso_code +(select accronym from am_ad_ledger_type where series = substring(a.Asset_Ledger,1,1))+"+
                		"d.branch_code +	a.Asset_Ledger asd from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c, am_gb_company b "+
                		"where a.currency_id = c.currency_id and a.category_code = AM_ASSET.CATEGORY_CODE "+
                		"and d.branch_code = AM_ASSET.BRANCH_CODE) AS DR_ACCT,	'D' AS DR_CR, AM_ASSET.Cost_Price AS COST_PRICE, "+
                		"(coalesce(AM_ASSET.INTEGRIFY,'')+ '*'+AM_ASSET.Description) AS Description, '' AS E, '' AS F, "+
                		"AM_ASSET.Asset_id AS ASSET_ID, '' AS H, '' AS I, AM_ASSET.SBU_CODE AS SBU_CODE "+
                		"FROM "+
                		"am_ad_branch am_ad_branch INNER JOIN AM_ASSET AM_ASSET ON am_ad_branch.BRANCH_CODE = AM_ASSET.BRANCH_CODE "+
                		"INNER JOIN am_ad_category am_ad_category ON AM_ASSET.CATEGORY_CODE = am_ad_category.category_code "+
                		"INNER JOIN am_ad_department am_ad_department ON AM_ASSET.Dept_code = am_ad_department.Dept_code "+
                		"INNER JOIN am_asset_approval am_asset_approval ON CAST(AM_ASSET.GROUP_ID AS VARCHAR(50)) = am_asset_approval.batch_id "+
                		"WHERE AM_ASSET.Asset_Status = 'ACTIVE' AND AM_ASSET.GROUP_ID = ? "+
                		"UNION "+
                		"SELECT Vendor_AC AS DR_ACCT,'C' AS DR_CR,SUM(VATABLE_COST-VAT) AS COST_PRICE, (coalesce(INTEGRIFY,'')+'*'+Description) AS Description,'' AS E,'' AS F,'' AS ASSET_ID,'' AS H,'' AS I, "+
                		"SBU_CODE FROM AM_ASSET WHERE GROUP_ID = ? AND ASSET_STATUS = 'ACTIVE' GROUP BY Vendor_AC,DESCRIPTION,INTEGRIFY,SBU_CODE "+
                		"UNION "+
                		"SELECT DISTINCT ( select c.iso_code +(select accronym from am_ad_ledger_type where series = substring(b.Fed_Wht_Account,1,1))+ "+
                		"b.default_branch + b.Fed_Wht_Account asd from am_ad_category a,am_ad_branch d, "+ 
                		"AM_GB_CURRENCY_CODE c, am_gb_company b where a.currency_id = c.currency_id "+
                		"and a.category_code = s.CATEGORY_CODE and d.branch_code = ?)  AS DR_ACCT, "+
                		"'C' AS DR_CR,WH_TAX_AMOUNT AS COST_PRICE, (coalesce(s.INTEGRIFY,'')+ '*'+VENDOR_NAME) AS Description,'' AS E,'' AS F,'' AS ASSET_ID,'' AS H,'' AS I, "+
                		"SBU_CODE FROM AM_ASSET s, am_ad_vendor v WHERE GROUP_ID = ? "+
                		"AND S.Vendor_AC = v.ACCOUNT_NUMBER AND s.ASSET_STATUS = 'ACTIVE' "+
//                		"GROUP BY Vendor_AC,VENDOR_NAME,INTEGRIFY,SBU_CODE,CATEGORY_CODE,BRANCH_CODE "+
                		"UNION "+
                		"SELECT DISTINCT ( select c.iso_code +(select accronym from am_ad_ledger_type where series = substring(b.VAT_ACCOUNT,1,1))+ "+
                		"b.default_branch + b.VAT_ACCOUNT asd from am_ad_category a,am_ad_branch d, "+
                		"AM_GB_CURRENCY_CODE c, am_gb_company b where a.currency_id = c.currency_id "+
                		"and a.category_code = s.CATEGORY_CODE and d.branch_code = ?)  AS DR_ACCT, "+
                		"'C' AS DR_CR,VAT AS COST_PRICE, (coalesce(s.INTEGRIFY,'')+ '*'+VENDOR_NAME) AS Description,'' AS E,'' AS F,'' AS ASSET_ID,'' AS H,'' AS I, "+
                		"SBU_CODE FROM AM_ASSET s, am_ad_vendor v WHERE GROUP_ID = ? AND S.Vendor_AC = v.ACCOUNT_NUMBER AND s.ASSET_STATUS = 'ACTIVE' ";
//                		"GROUP BY Vendor_AC,VENDOR_NAME,INTEGRIFY,SBU_CODE,CATEGORY_CODE,BRANCH_CODE";
            }
//            System.out.println("======>>>>>>>ColQuery=======: "+ColQuery);
          //  ColQuery = ColQuery+" AND ASSET_STATUS = 'ACTIVE' ";
    //        System.out.println("======>>>>>>>ColQuery: "+ColQuery);
            java.util.ArrayList list =rep.getFinacleAssetUploadExportRecords(ColQuery,branchCode,groupid);
            if(list.size()!=0){
            	if(appName.equalsIgnoreCase("FINACLE-7.0.9")){
//            	System.out.println("======1>>>>>>>appName: "+appName);
                HSSFWorkbook workbook = new HSSFWorkbook();
                HSSFSheet sheet = workbook.createSheet("Finacle Asset Export Record");
                HSSFRow rowhead = sheet.createRow((int) 0);
                
                rowhead.createCell((int) 0).setCellValue("Account No.");
                rowhead.createCell((int) 1).setCellValue("DR CR");
                rowhead.createCell((int) 2).setCellValue("Amount ");
                rowhead.createCell((int) 3).setCellValue("Description");
                rowhead.createCell((int) 4).setCellValue("E");
                rowhead.createCell((int) 5).setCellValue("F");
                rowhead.createCell((int) 6).setCellValue("Asset Id");
                rowhead.createCell((int) 7).setCellValue("H");
                rowhead.createCell((int) 8).setCellValue("I");
                rowhead.createCell((int) 9).setCellValue("SBU CODE");
                int i = 1;

//     System.out.println("<<<<<<list.size(): "+list.size());
                for(int k=0;k<list.size();k++)
                {
                    com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);
                    String assetId =  newassettrans.getAssetId();
                    String drCr =  newassettrans.getBarCode();
                    String sbuCode =  newassettrans.getSbuCode();
                    double costPrice = newassettrans.getCostPrice();
                    String Description = newassettrans.getDescription();
                    String E = newassettrans.getAssetUser();
                    String F = newassettrans.getAssetCode();
                    String accountNo = newassettrans.getVendorAC();
                    String H = newassettrans.getCategoryCode();
                    String I = newassettrans.getIntegrifyId();

                    HSSFRow row = sheet.createRow((int) i);

                    row.createCell((int) 0).setCellValue(accountNo);
                    row.createCell((int) 1).setCellValue(drCr);
                    row.createCell((int) 2).setCellValue(costPrice);
                    row.createCell((int) 3).setCellValue(Description);
                    row.createCell((int) 4).setCellValue(E);
                    row.createCell((int) 5).setCellValue(F);
                    row.createCell((int) 6).setCellValue(assetId);
                    row.createCell((int) 7).setCellValue(H);
                    row.createCell((int) 8).setCellValue(I);
                    row.createCell((int) 9).setCellValue(sbuCode);
                    i++;
                }
                OutputStream stream = response.getOutputStream();
//              new MailSender().AttachmentMail("ayomidematanmi4@gmail.com",file);
              workbook.write(stream);
              stream.close();
              System.out.println("Data is saved in excel file.");
            	}
            	if(appName.equalsIgnoreCase("FINACLE-10.2.18")){
//              System.out.println("======2>>>>>>>appName: "+appName);
              HSSFWorkbook workbook = new HSSFWorkbook();
              HSSFSheet sheet = workbook.createSheet("Finacle Asset Export Record");
              HSSFRow rowhead = sheet.createRow((int) 0);
              
              rowhead.createCell((int) 0).setCellValue("Account No.");
              rowhead.createCell((int) 1).setCellValue("Curency");
              rowhead.createCell((int) 2).setCellValue("Narration (30 xters max)");
              rowhead.createCell((int) 3).setCellValue("Remarks1 (30 xters max)");
              rowhead.createCell((int) 4).setCellValue("Remarks2 (50 xters max)");
              rowhead.createCell((int) 5).setCellValue("Amount");
              rowhead.createCell((int) 6).setCellValue("ValueDate(DD/MM/YYYY)");
              rowhead.createCell((int) 7).setCellValue("ReportCode");
              rowhead.createCell((int) 8).setCellValue("Narration Checker");
              rowhead.createCell((int) 9).setCellValue("Remarks1 Checker");
              int i = 1;
              
//   System.out.println("<<<<<<list.size(): "+list.size());
              for(int k=0;k<list.size();k++)
              {
                  com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);
                  String assetId =  newassettrans.getAssetId();
                  String drCr =  newassettrans.getBarCode();
                  String sbuCode =  newassettrans.getSbuCode();
                  double costPrice = newassettrans.getCostPrice();
                  String Description = newassettrans.getDescription();
                  String E = newassettrans.getAssetUser();
                  String F = newassettrans.getAssetCode();
                  String accountNo = newassettrans.getVendorAC();
                  String H = newassettrans.getCategoryCode();
                  String I = newassettrans.getIntegrifyId();
                  String J = newassettrans.getCategoryCode();
                  String currency = "NGN";
                  HSSFRow row = sheet.createRow((int) i);

                  row.createCell((int) 0).setCellValue(accountNo);
                  row.createCell((int) 1).setCellValue(currency);
                  row.createCell((int) 2).setCellValue(Description);
                  row.createCell((int) 3).setCellValue(assetId);
                  row.createCell((int) 4).setCellValue(E);
                  row.createCell((int) 5).setCellValue(costPrice);
                  row.createCell((int) 6).setCellValue(new java.util.Date());
                  row.createCell((int) 7).setCellValue(sbuCode);
                  row.createCell((int) 8).setCellValue(I);
                  row.createCell((int) 9).setCellValue(J);
                  i++;
              }
              OutputStream stream = response.getOutputStream();
//            new MailSender().AttachmentMail("ayomidematanmi4@gmail.com",file);
            workbook.write(stream);
            stream.close();
            System.out.println("Data is saved in excel file.");
            }
            	if(appName.equalsIgnoreCase("FLEXCUBE-7.0.9")){
//            		 System.out.println("======3>>>>>>>appName: "+appName);
                    HSSFWorkbook workbook = new HSSFWorkbook();
                    HSSFSheet sheet = workbook.createSheet("Finacle Asset Export Record");
                    HSSFRow rowhead = sheet.createRow((int) 0);
                    
                    rowhead.createCell((int) 0).setCellValue("Account No.");
                    rowhead.createCell((int) 1).setCellValue("DR CR");
                    rowhead.createCell((int) 2).setCellValue("Amount ");
                    rowhead.createCell((int) 3).setCellValue("Description");
                    rowhead.createCell((int) 4).setCellValue("E");
                    rowhead.createCell((int) 5).setCellValue("F");
                    rowhead.createCell((int) 6).setCellValue("Asset Id");
                    rowhead.createCell((int) 7).setCellValue("H");
                    rowhead.createCell((int) 8).setCellValue("I");
                    rowhead.createCell((int) 9).setCellValue("SBU CODE");
                    int i = 1;

      //   System.out.println("<<<<<<list.size(): "+list.size());
                    for(int k=0;k<list.size();k++)
                    {
                        com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);
                        String assetId =  newassettrans.getAssetId();
                        String drCr =  newassettrans.getBarCode();
                        String sbuCode =  newassettrans.getSbuCode();
                        double costPrice = newassettrans.getCostPrice();
                        String Description = newassettrans.getDescription();
                        String E = newassettrans.getAssetUser();
                        String F = newassettrans.getAssetCode();
                        String accountNo = newassettrans.getVendorAC();
                        String H = newassettrans.getCategoryCode();
                        String I = newassettrans.getIntegrifyId();

                        HSSFRow row = sheet.createRow((int) i);

                        row.createCell((int) 0).setCellValue(accountNo);
                        row.createCell((int) 1).setCellValue(drCr);
                        row.createCell((int) 2).setCellValue(costPrice);
                        row.createCell((int) 3).setCellValue(Description);
                        row.createCell((int) 4).setCellValue(E);
                        row.createCell((int) 5).setCellValue(F);
                        row.createCell((int) 6).setCellValue(assetId);
                        row.createCell((int) 7).setCellValue(H);
                        row.createCell((int) 8).setCellValue(I);
                        row.createCell((int) 9).setCellValue(sbuCode);
                        i++;
                    }
                    OutputStream stream = response.getOutputStream();
//                  new MailSender().AttachmentMail("ayomidematanmi4@gmail.com",file);
                  workbook.write(stream);
                  stream.close();
                  System.out.println("Data is saved in excel file.");
                  }                  
              
            }
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
   public void doGet(HttpServletRequest request, 
		    HttpServletResponse response)
		      throws ServletException, IOException
		   {
	   doPost(request, response);
		   }
}