package com.magbel.legend.servlet;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Properties;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONObject;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import magma.AssetRecordsBean;
import ng.com.magbel.token.ZenithTokenClass;
   
public class FinacleAssetDisposalUploadExport extends HttpServlet
{
	private EmailSmsServiceBus mail ;
	private AssetRecordsBean ad;
	private ApprovalRecords records;
	java.text.SimpleDateFormat sdf;
   public void doPost(HttpServletRequest request, 
    HttpServletResponse response)
      throws ServletException, IOException
   {
	   
	   
		Properties prop = new Properties();
		File file = new File("C:\\Property\\LegendPlus.properties");
		FileInputStream input = new FileInputStream(file);
		prop.load(input);

		String ThirdPartyLabel = prop.getProperty("ThirdPartyLabel");
		System.out.println("ThirdPartyLabel: " + ThirdPartyLabel);
		String singleApproval = prop.getProperty("singleApproval");
		System.out.println("singleApproval: " + singleApproval);
		String BatchApiUrl = prop.getProperty("BatchApiUrl");
		System.out.println("BatchApiUrl: " + BatchApiUrl);
		String BatchChannel = prop.getProperty("BatchChannel");
		System.out.println("BatchChannel: " + BatchChannel);
		
	   String batchNo ="";
	   if(!BatchApiUrl.equals("")){
	   try{
	   String status = ZenithTokenClass.validation();
	   JSONObject json = new JSONObject(status);
	   batchNo = json.getString("batchId");
	   }catch(Exception e){
		   e.getMessage();
   		}
   }
	   
	   String userId =(String) request.getSession().getAttribute("CurrentUser");
	   String userClass = (String) request.getSession().getAttribute("UserClass");
//	   PrintWriter out = response.getWriter();
//    OutputStream out = null;
	   sdf = new java.text.SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		mail= new EmailSmsServiceBus();
        records = new ApprovalRecords();
        String branchId = request.getParameter("branch");  
        String groupid = request.getParameter("groupid");
        String appName = records.getCodeName("SELECT APP_NAME+'-'+VERSION FROM AM_AD_LEGACY_SYS_CONFIG"); 
        String currency = records.getCodeName("SELECT iso_code FROM AM_GB_CURRENCY_CODE WHERE local_currency = 'Y'"); 
        String reportType  = request.getParameter("FinacleReport");
//        System.out.println("<<<<<<branchId: "+branchId+"    Group Id: "+groupid);
        if(branchId.equals("***")){branchId = records.getCodeName("select BRANCH from am_gb_User where USER_ID = "+userId+" ");}
        String branchCode = records.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID = "+branchId+" ");
//        String subjectTovat = records.getCodeName("select distinct Subject_TO_Vat from AM_ASSET where GROUP_ID = '"+groupid+"'");
//        String subjectTowhTax = records.getCodeName("select distinct Wh_Tax from AM_ASSET where GROUP_ID = '"+groupid+"'");
        String accountNumber = records.getCodeName("select distinct SUBSTRING(vendor_ac, 1,len(LTRIM(RTRIM(vendor_ac)))-1) from AM_ASSET where GROUP_ID = '"+groupid+"'");
//        String supervisorNameQry = records.getCodeName("select distinct Subject_TO_Vat+':'+substring(Wh_Tax,1,1)+':'+coalesce(TIN,'N')+':'+coalesce(RCNo,'N') from AM_ASSET a, am_ad_vendor v where v.account_number like '%"+accountNumber+"%' and GROUP_ID = '"+groupid+"' and v.Vendor_Status = 'ACTIVE'");
//        System.out.println("<<<<<<supervisorNameQry: "+supervisorNameQry);
//        String[] sprvResult = supervisorNameQry.split(":");
////        String[] sprvResult = (records.retrieve4Array(supervisorNameQry)).split(":");
//        String subjectTovat = sprvResult[0];
//        String subjectTowhTax = sprvResult[1];
//        String TIN = sprvResult[2];
//        String RCNo = sprvResult[3];
        Date date = new Date();  
        System.out.println(sdf.format(date)); 
        String TIN = "";
        String RCNo = "";
        String deptCode = records.getCodeName("select dept_code from am_gb_User where USER_ID = "+userId+" ");
        String monthName = records.getCodeName("select CONVERT(varchar(3), getdate(), 100)");
//        System.out.println("<<<<<<branch Id: "+branchId+"   branch_Code: "+branchCode+"  subjectTovat: "+subjectTovat+"  subjectTowhTax: "+subjectTowhTax+"  TIN: "+TIN+"    RCNo: "+RCNo);
        String userName = records.getCodeName("SELECT USER_NAME FROM am_gb_User WHERE USER_ID = "+userId+"");
//        if(TIN.equalsIgnoreCase("N")){TIN = RCNo; subjectTovat = "N";}
        String fileName = "";
        String currentDate = sdf.format(date);
        String DD = currentDate.substring(0,2);
        String MM = currentDate.substring(3,5);
        String YYYY = currentDate.substring(6,10);
        String HH = currentDate.substring(11,13);
        String M = currentDate.substring(14,16);
        String SS = currentDate.substring(17,19);
        currentDate = DD+MM+YYYY+HH+M+SS;
        String dateField = DD+"-"+monthName+"-"+YYYY;
        if(reportType.equalsIgnoreCase("LegacyExport")){fileName = branchCode+"By"+userName+"FinacleAssetDisposalUploadExport.xlsx";}
        if(reportType.equalsIgnoreCase("ShowReport")){fileName = branchCode+"By"+userName+"ReportAssetDisposalExport.xls";}
        if(!BatchApiUrl.equals("") && reportType.equalsIgnoreCase("LegacyExport")){fileName = "DE_UPLOAD_"+deptCode+"_"+sdf.format(date)+".csv";}
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
//            System.out.println("======reportType: "+reportType);
            Report rep = new Report();
//   System.out.println("<<<<<<branch_Id====>: "+branch_Id+"    categoryId: "+categoryId+"  branchCode: "+branchCode+"  subjectTovat: "+subjectTovat+"  subjectTowhTax: "+subjectTowhTax);
            String ColQuery = "";
//            System.out.println("======>>>>>>>subjectTovat: "+subjectTovat+"   subjectTowhTax: "+subjectTowhTax);
//            if(subjectTovat.equals("Y")  && subjectTowhTax.equals("S")){
//            	System.out.println("===subjectTovat.equals('Y')  && subjectTowhTax.equals('S')");
            if(reportType.equalsIgnoreCase("LegacyExport")){
            	ColQuery = records.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where THIRDPARTY = 'LEGDISPOSALUPLOAD'");
//                ColQuery ="SELECT DISTINCT (select c.iso_code +(select accronym from am_ad_ledger_type where series = substring(b.suspense_acct,1,1))+ "+
//                		"b.default_branch +	b.suspense_acct asd "+
//                		"from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b "+
//                		"where a.currency_id = c.currency_id and a.category_code = am_asset_disposal_Upload.CATEGORY_CODE "+
//                		"and d.branch_code = am_asset_disposal_Upload.BRANCH_CODE) "+
//                		"AS DR_ACCT,	'D' AS DR_CR, am_asset_disposal_Upload.Cost_Price AS COST_PRICE, "+
//                		"(coalesce(am_asset_disposal_Upload.INTEGRIFY,'')+ '*'+am_asset_disposal_Upload.Description) AS Description, '' AS E, '' AS F, "+
//                		"am_asset_disposal_Upload.Asset_id AS ASSET_ID, '' AS H, '' AS I, am_asset_disposal_Upload.SBU_CODE AS SBU_CODE "+
//                		"FROM "+
//                		"am_ad_branch am_ad_branch INNER JOIN am_asset_disposal_Upload am_asset_disposal_Upload ON am_ad_branch.BRANCH_CODE = am_asset_disposal_Upload.BRANCH_CODE "+ 
//                		"INNER JOIN am_ad_category am_ad_category ON am_asset_disposal_Upload.CATEGORY_CODE = am_ad_category.category_code "+
//                		"INNER JOIN am_asset_approval am_asset_approval ON CAST(am_asset_disposal_Upload.disposal_ID AS VARCHAR(50)) = am_asset_approval.batch_id "+
//                		"WHERE am_asset_disposal_Upload.Approval_Status = 'ACTIVE' AND am_asset_disposal_Upload.disposal_ID = ? "+ 
//                		"UNION "+
//                		"SELECT DISTINCT (select c.iso_code +(select accronym from am_ad_ledger_type where series = substring(a.Asset_Ledger,1,1))+ "+
//                		"d.branch_code +	a.Asset_Ledger asd "+
//                		"from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c, am_gb_company b "+
//                		"where a.currency_id = c.currency_id and a.category_code = am_asset_disposal_Upload.category_code "+
//                		"and d.branch_code = am_asset_disposal_Upload.branch_code) "+
//                		"AS DR_ACCT,	'C' AS DR_CR, am_asset_disposal_Upload.Cost_Price AS COST_PRICE, "+
//                		"(coalesce(am_asset_disposal_Upload.INTEGRIFY,'')+ '*'+am_asset_disposal_Upload.Description) AS Description, '' AS E, '' AS F, "+
//                		"am_asset_disposal_Upload.Asset_id AS ASSET_ID, '' AS H, '' AS I, am_asset_disposal_Upload.SBU_CODE AS SBU_CODE "+
//                		"FROM "+
//                		"am_ad_branch am_ad_branch INNER JOIN am_asset_disposal_Upload am_asset_disposal_Upload ON am_ad_branch.BRANCH_CODE = am_asset_disposal_Upload.BRANCH_CODE "+
//                		"INNER JOIN am_ad_category am_ad_category ON am_asset_disposal_Upload.CATEGORY_CODE = am_ad_category.category_code "+
//                		"INNER JOIN am_asset_approval am_asset_approval ON CAST(am_asset_disposal_Upload.disposal_ID AS VARCHAR(50)) = am_asset_approval.batch_id "+
//                		"WHERE am_asset_disposal_Upload.Approval_Status = 'ACTIVE' AND am_asset_disposal_Upload.disposal_ID = ? "+ 
//                		"UNION "+
//                		"SELECT DISTINCT (select c.iso_code +(select accronym from am_ad_ledger_type where series = substring(a.Accum_Dep_ledger,1,1))+ "+
//                		"d.branch_code +	a.Accum_Dep_ledger asd "+
//                		"from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b "+
//                		"where a.currency_id = c.currency_id and a.category_code = am_asset_disposal_Upload.category_code "+
//                		"and d.branch_code = am_asset_disposal_Upload.branch_code) "+
//                		"AS DR_ACCT,	'D' AS DR_CR, am_asset_disposal_Upload.accum_dep AS COST_PRICE, "+
//                		"(coalesce(am_asset_disposal_Upload.INTEGRIFY,'')+ '*'+am_asset_disposal_Upload.Description) AS Description, '' AS E, '' AS F, "+
//                		"am_asset_disposal_Upload.Asset_id AS ASSET_ID, '' AS H, '' AS I, am_asset_disposal_Upload.SBU_CODE AS SBU_CODE "+
//                		"FROM "+
//                		"am_ad_branch am_ad_branch INNER JOIN am_asset_disposal_Upload am_asset_disposal_Upload ON am_ad_branch.BRANCH_CODE = am_asset_disposal_Upload.BRANCH_CODE "+
//                		"INNER JOIN am_ad_category am_ad_category ON am_asset_disposal_Upload.CATEGORY_CODE = am_ad_category.category_code "+
//                		"INNER JOIN am_asset_approval am_asset_approval ON CAST(am_asset_disposal_Upload.disposal_ID AS VARCHAR(50)) = am_asset_approval.batch_id "+
//                		"WHERE am_asset_disposal_Upload.Approval_Status = 'ACTIVE' AND am_asset_disposal_Upload.disposal_ID = ? "+ 
//                		"UNION "+
//                		"SELECT DISTINCT (select c.iso_code +(select accronym from am_ad_ledger_type where series = substring(b.suspense_acct,1,1))+ "+
//                		"b.default_branch +	b.suspense_acct asd "+
//                		"from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b "+
//                		"where a.currency_id = c.currency_id and a.category_code = am_asset_disposal_Upload.CATEGORY_CODE "+
//                		"and d.branch_code = am_asset_disposal_Upload.BRANCH_CODE) "+
//                		"AS DR_ACCT,	'C' AS DR_CR, am_asset_disposal_Upload.accum_dep AS COST_PRICE, "+
//                		"(coalesce(am_asset_disposal_Upload.INTEGRIFY,'')+ '*'+am_asset_disposal_Upload.Description) AS Description, '' AS E, '' AS F, "+
//                		"am_asset_disposal_Upload.Asset_id AS ASSET_ID, '' AS H, '' AS I, am_asset_disposal_Upload.SBU_CODE AS SBU_CODE "+
//                		"FROM "+
//                		"am_ad_branch am_ad_branch INNER JOIN am_asset_disposal_Upload am_asset_disposal_Upload ON am_ad_branch.BRANCH_CODE = am_asset_disposal_Upload.BRANCH_CODE "+
//                		"INNER JOIN am_ad_category am_ad_category ON am_asset_disposal_Upload.CATEGORY_CODE = am_ad_category.category_code "+
//                		"INNER JOIN am_asset_approval am_asset_approval ON CAST(am_asset_disposal_Upload.disposal_ID AS VARCHAR(50)) = am_asset_approval.batch_id "+
//                		"WHERE am_asset_disposal_Upload.Approval_Status = 'ACTIVE' AND am_asset_disposal_Upload.disposal_ID = ? "+
//                		"UNION "+
//                		"SELECT buyer_ac AS DR_ACCT,'C' AS DR_CR,SUM(disposalAmount) AS COST_PRICE, (coalesce(INTEGRIFY,'')+'*'+Description) AS Description,'' AS E,'' AS F,asset_id AS ASSET_ID,'' AS H,'' AS I, "+
//                		"SBU_CODE FROM am_asset_disposal_Upload WHERE disposal_ID = ? GROUP BY asset_id,buyer_ac,DESCRIPTION,INTEGRIFY,SBU_CODE "+
////                		"UNION "+
////                		"SELECT DISTINCT (select c.iso_code +(select accronym from am_ad_ledger_type where series = substring(b.suspense_acct,1,1))+ "+
////                		"b.default_branch +	b.suspense_acct asd "+
////                		"from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b "+
////                		"where a.currency_id = c.currency_id and a.category_code = am_asset_disposal_Upload.CATEGORY_CODE "+
////                		"and d.branch_code = am_asset_disposal_Upload.BRANCH_CODE) "+
////                		"AS DR_ACCT,	'C' AS DR_CR, am_asset_disposal_Upload.disposalAmount AS COST_PRICE, "+
////                		"(coalesce(am_asset_disposal_Upload.INTEGRIFY,'')+ '*'+am_asset_disposal_Upload.Description) AS Description, '' AS E, '' AS F, "+
////                		"am_asset_disposal_Upload.Asset_id AS ASSET_ID, '' AS H, '' AS I, am_asset_disposal_Upload.SBU_CODE AS SBU_CODE "+
////                		"FROM "+
////                		"am_ad_branch am_ad_branch INNER JOIN am_asset_disposal_Upload am_asset_disposal_Upload ON am_ad_branch.BRANCH_CODE = am_asset_disposal_Upload.BRANCH_CODE "+
////                		"INNER JOIN am_ad_category am_ad_category ON am_asset_disposal_Upload.CATEGORY_CODE = am_ad_category.category_code "+
////                		"INNER JOIN am_asset_approval am_asset_approval ON CAST(am_asset_disposal_Upload.disposal_ID AS VARCHAR(50)) = am_asset_approval.batch_id "+
////                		"WHERE am_asset_disposal_Upload.Approval_Status = 'ACTIVE' AND am_asset_disposal_Upload.disposal_ID = ? "+
////                		
//                		"UNION "+
//                		"SELECT DISTINCT (select c.iso_code +(select accronym from am_ad_ledger_type where series = substring(b.suspense_acct,1,1))+ "+
//                		"b.default_branch +	b.suspense_acct asd "+
//                		"from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b "+
//                		"where a.currency_id = c.currency_id and a.category_code = am_asset_disposal_Upload.CATEGORY_CODE "+
//                		"and d.branch_code = am_asset_disposal_Upload.BRANCH_CODE) "+
//                		"AS DR_ACCT,	'D' AS DR_CR, am_asset_disposal_Upload.disposalCost AS COST_PRICE, "+
//                		"(coalesce(am_asset_disposal_Upload.INTEGRIFY,'')+ '*'+am_asset_disposal_Upload.Description) AS Description, '' AS E, '' AS F, "+
//                		"am_asset_disposal_Upload.Asset_id AS ASSET_ID, '' AS H, '' AS I, am_asset_disposal_Upload.SBU_CODE AS SBU_CODE "+
//                		"FROM "+
//                		"am_ad_branch am_ad_branch INNER JOIN am_asset_disposal_Upload am_asset_disposal_Upload ON am_ad_branch.BRANCH_CODE = am_asset_disposal_Upload.BRANCH_CODE "+
//                		"INNER JOIN am_ad_category am_ad_category ON am_asset_disposal_Upload.CATEGORY_CODE = am_ad_category.category_code "+
//                		"INNER JOIN am_asset_approval am_asset_approval ON CAST(am_asset_disposal_Upload.disposal_ID AS VARCHAR(50)) = am_asset_approval.batch_id "+
//                		"WHERE am_asset_disposal_Upload.Approval_Status = 'ACTIVE' AND am_asset_disposal_Upload.disposal_ID = ? "+ 
//                		"UNION "+
//                		"SELECT DISTINCT (select c.iso_code +(select accronym from am_ad_ledger_type where series = substring(b.suspense_acct,1,1))+ "+
//                		"b.default_branch +	b.suspense_acct asd "+
//                		"from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b "+
//                		"where a.currency_id = c.currency_id and a.category_code = am_asset_disposal_Upload.CATEGORY_CODE "+ 
//                		"and d.branch_code = am_asset_disposal_Upload.BRANCH_CODE) "+
//                		"AS DR_ACCT,	'D' AS DR_CR, am_asset_disposal_Upload.PROFIT_LOSS AS COST_PRICE, "+
//                		"(coalesce(am_asset_disposal_Upload.INTEGRIFY,'')+ '*'+am_asset_disposal_Upload.Description) AS Description, '' AS E, '' AS F, "+
//                		"am_asset_disposal_Upload.Asset_id AS ASSET_ID, '' AS H, '' AS I, am_asset_disposal_Upload.SBU_CODE AS SBU_CODE "+
//                		"FROM "+
//                		"am_ad_branch am_ad_branch INNER JOIN am_asset_disposal_Upload am_asset_disposal_Upload ON am_ad_branch.BRANCH_CODE = am_asset_disposal_Upload.BRANCH_CODE "+
//                		"INNER JOIN am_ad_category am_ad_category ON am_asset_disposal_Upload.CATEGORY_CODE = am_ad_category.category_code "+
//                		"INNER JOIN am_asset_approval am_asset_approval ON CAST(am_asset_disposal_Upload.disposal_ID AS VARCHAR(50)) = am_asset_approval.batch_id "+
//                		"WHERE am_asset_disposal_Upload.Approval_Status = 'ACTIVE' AND am_asset_disposal_Upload.disposal_ID = ? "+
//                		"UNION "+
//                		//If Disposal is Profit or Loss
//                		"SELECT DISTINCT (SELECT IIF(PROFIT_LOSS < 0, "+
//                		"(select c.iso_code +(select accronym from am_ad_ledger_type where series = substring(b.Loss_Disposal_Account,1,1))+ "+
//                		"b.default_branch +	(substring(b.Loss_Disposal_Account,2,9)) asd "+
//                		"from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b "+
//                		"where a.currency_id = c.currency_id and a.category_code = am_asset_disposal_Upload.category_code "+
//                		"and d.branch_code = am_asset_disposal_Upload.branch_code), "+
//                		"(select c.iso_code +(select accronym from am_ad_ledger_type where series = substring(b.PL_Disposal_account,1,1))+ "+
//                		"d.branch_code +b.PL_Disposal_account asd "+
//                		"from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b "+
//                		"where a.currency_id = c.currency_id and a.category_code = am_asset_disposal_Upload.category_code "+
//                		"and d.branch_code = am_asset_disposal_Upload.branch_code))) "+
//                		"AS DR_ACCT,	'C' AS DR_CR, am_asset_disposal_Upload.PROFIT_LOSS AS COST_PRICE, "+
//                		"(coalesce(am_asset_disposal_Upload.INTEGRIFY,'')+ '*'+am_asset_disposal_Upload.Description) AS Description, '' AS E, '' AS F, "+
//                		"am_asset_disposal_Upload.Asset_id AS ASSET_ID, '' AS H, '' AS I, am_asset_disposal_Upload.SBU_CODE AS SBU_CODE "+
//                		"FROM "+
//                		"am_ad_branch am_ad_branch INNER JOIN am_asset_disposal_Upload am_asset_disposal_Upload ON am_ad_branch.BRANCH_CODE = am_asset_disposal_Upload.BRANCH_CODE "+
//                		"INNER JOIN am_ad_category am_ad_category ON am_asset_disposal_Upload.CATEGORY_CODE = am_ad_category.category_code "+
//                		"INNER JOIN am_asset_approval am_asset_approval ON CAST(am_asset_disposal_Upload.disposal_ID AS VARCHAR(50)) = am_asset_approval.batch_id "+
//                		"WHERE am_asset_disposal_Upload.Approval_Status = 'ACTIVE' AND am_asset_disposal_Upload.disposal_ID = ? ";
        
//            System.out.println("======>>>>>>>ColQuery: "+ColQuery);
            java.util.ArrayList list =rep.getFinacleDisposalUploadExportRecords(ColQuery,groupid);
            if(list.size()!=0){ 
            	if(appName.equalsIgnoreCase("FINACLE-7.0.9")){
                HSSFWorkbook workbook = new HSSFWorkbook();
                HSSFSheet sheet = workbook.createSheet("Depreciation Charges Export");
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
                    if(costPrice<0){costPrice = costPrice*-1;}
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
            	if((appName.equalsIgnoreCase("FINACLE-10.2.18")) && (BatchApiUrl.equals(""))){
                    HSSFWorkbook workbook = new HSSFWorkbook();
                    HSSFSheet sheet = workbook.createSheet("Depreciation Charges Export");
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

//         System.out.println("<<<<<<list.size(): "+list.size());
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
//                  new MailSender().AttachmentMail("ayomidematanmi4@gmail.com",file);
                  workbook.write(stream);
                  stream.close();
                  System.out.println("Data is saved in excel file.");
                }  
            	if(appName.equalsIgnoreCase("FLEXCUBE-7.0.9")){
                    HSSFWorkbook workbook = new HSSFWorkbook();
                    HSSFSheet sheet = workbook.createSheet("Depreciation Charges Export");
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

//         System.out.println("<<<<<<list.size(): "+list.size());
                    for(int k=0;k<list.size();k++)
                    {
                        com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);
                        String assetId =  newassettrans.getAssetId();
                        String drCr =  newassettrans.getBarCode();
                        String sbuCode =  newassettrans.getSbuCode();
                        double costPrice = newassettrans.getCostPrice();
                        if(costPrice<0){costPrice = costPrice*-1;}
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
            	
            	if((appName.equalsIgnoreCase("FINACLE-10.2.18")) && (!BatchApiUrl.equals(""))){
                	System.out.println("======1>>>>>>>appName: "+appName);
                    HSSFWorkbook workbook = new HSSFWorkbook();
                    HSSFSheet sheet = workbook.createSheet("Legacy Asset Export Record");
                    HSSFRow rowhead = sheet.createRow((int) 0);
                    
                    rowhead.createCell((int) 0).setCellValue("Date");
                    rowhead.createCell((int) 1).setCellValue("Acc number");
                    rowhead.createCell((int) 2).setCellValue("Amt");
                    rowhead.createCell((int) 3).setCellValue("Transaction type");
                    rowhead.createCell((int) 4).setCellValue("Desc ");
                    rowhead.createCell((int) 5).setCellValue("Transaction Code ");
                    rowhead.createCell((int) 6).setCellValue("Currency");
                    rowhead.createCell((int) 7).setCellValue("Branch code ");
                    rowhead.createCell((int) 8).setCellValue("Purpose Code ");
                    rowhead.createCell((int) 9).setCellValue("Batch number ");
                    rowhead.createCell((int) 10).setCellValue("Maker");
                    rowhead.createCell((int) 11).setCellValue("Checker");
                    int i = 1;

                    System.out.println("<<<<<<list.size(): "+list.size());
                    for(int k=0;k<list.size();k++)
                    {
                        com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);
                        String assetId =  newassettrans.getAssetId();
                        String drCr =  newassettrans.getBarCode();
                        String sbuCode =  newassettrans.getSbuCode();
                        double costPrice = newassettrans.getCostPrice();
                        String Description = newassettrans.getDescription();
                        String transType = "";
                        if(costPrice > 0){transType = "C";}
                        if(costPrice < 0){transType = "D";}
                        String transCode = "";
                        String accountNo = newassettrans.getVendorAC();
                        String maker = "";
                        String checker = "";
                        String purposeCode = "";
                        
                        HSSFRow row = sheet.createRow((int) i);

                        row.createCell((int) 0).setCellValue(dateField);
                        row.createCell((int) 1).setCellValue(accountNo);
                        row.createCell((int) 2).setCellValue(costPrice);
                        row.createCell((int) 3).setCellValue(transType);
                        row.createCell((int) 4).setCellValue(Description);
                        row.createCell((int) 5).setCellValue(transCode);
                        row.createCell((int) 6).setCellValue(currency);
                        row.createCell((int) 7).setCellValue(branchCode);
                        row.createCell((int) 8).setCellValue(purposeCode);
                        row.createCell((int) 9).setCellValue(maker);
                        row.createCell((int) 10).setCellValue(batchNo);
                        row.createCell((int) 11).setCellValue(checker);
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

            if(reportType.equalsIgnoreCase("ShowReport")){
                ColQuery ="SELECT "+
                "am_gb_company.company_name AS am_gb_company_company_name,AM_ASSET.Asset_id AS AM_ASSET_Asset_id,"+
                "AM_ASSET.Description AS AM_ASSET_Description,AM_ASSET.BRANCH_CODE AS AM_ASSET_BRANCH_CODE,"+
                "am_ad_branch.BRANCH_NAME AS am_ad_branch_BRANCH_NAME,AM_ASSET.ASSET_USER AS AM_ASSET_ASSET_USER,"+
                "AM_ASSET.BAR_CODE AS AM_ASSET_BAR_CODE,am_ad_category.category_name AS am_ad_category_category_name,"+
                "am_ad_category.Dep_rate AS am_ad_category_Dep_rate,am_ad_category.Accum_Dep_ledger AS am_ad_category_Accum_Dep_ledger,"+
                "am_ad_category.Dep_ledger AS am_ad_category_Dep_ledger,am_ad_category.Asset_Ledger AS am_ad_category_Asset_Ledger,"+
                "am_ad_category.gl_account AS am_ad_category_gl_account,am_ad_department.Dept_name AS am_ad_department_Dept_name,"+
                "am_asset_disposal_Upload.Disposal_ID AS am_asset_disposal_Upload_Disposal_ID,am_asset_disposal_Upload.asset_id AS am_asset_disposal_Upload_asset_id, "+
                "am_asset_disposal_Upload.disposalcost AS am_asset_disposal_Upload_disposalcost,am_asset_disposal_Upload.disposal_reason AS am_asset_disposal_Upload_disposal_reason,"+
                "am_asset_disposal_Upload.Disposal_Date AS am_asset_disposal_Upload_Disposal_Date,am_asset_disposal_Upload.User_ID AS am_asset_disposal_Upload_User_ID,"+
                "am_asset_disposal_Upload.raise_entry AS am_asset_disposal_Upload_raise_entry,am_asset_disposal_Upload.cost_price AS am_asset_disposal_Upload_cost_price,"+
                "am_asset_disposal_Upload.disposalAmount AS am_asset_disposal_Upload_disposalAmount,am_asset_disposal_Upload.vat_amount AS am_asset_disposal_Upload_vat_amount,"+
                "am_asset_disposal_Upload.wht_amount AS am_asset_disposal_Upload_wht_amount,am_asset_disposal_Upload.nbv AS am_asset_disposal_Upload_nbv,"+
                "am_asset_disposal_Upload.accum_dep AS am_asset_disposal_Upload_accum_dep,am_asset_disposal_Upload.effDate AS am_asset_disposal_Upload_effDate,"+
                "am_asset_disposal_Upload.approval_status AS am_asset_disposal_Upload_approval_status,am_asset_disposal_Upload.WHT_PERCENT AS am_asset_disposal_Upload_WHT_PERCENT,"+
                "am_asset_disposal_Upload.WH_tax AS am_asset_disposal_Upload_WH_tax,am_asset_disposal_Upload.Subject_to_vat AS am_asset_disposal_Upload_Subject_to_vat,"+
                "am_asset_disposal_Upload.BUYER_AC AS am_asset_disposal_Upload_BUYER_AC,am_asset_disposal_Upload.branch_code AS am_asset_disposal_Upload_branch_code,"+
                "am_asset_disposal_Upload.category_code AS am_asset_disposal_Upload_category_code,am_asset_disposal_Upload.description AS am_asset_disposal_Upload_description,"+
                "am_asset_disposal_Upload.lpoNum AS am_asset_disposal_Upload_lpoNum,am_asset_disposal_Upload.invoice_no AS am_asset_disposal_Upload_invoice_no,"+
                "am_asset_disposal_Upload.asset_code AS am_asset_disposal_Upload_asset_code,am_asset_disposal_Upload.INTEGRIFY AS am_asset_disposal_Upload_INTEGRIFY,"+
                "am_asset_disposal_Upload.DISPOSED AS am_asset_disposal_Upload_DISPOSED,am_asset_disposal_Upload.PROFIT_LOSS AS am_asset_disposal_Upload_PROFIT_LOSS "+
                "FROM "+
                "am_ad_branch am_ad_branch INNER JOIN AM_ASSET AM_ASSET ON am_ad_branch.BRANCH_CODE = AM_ASSET.BRANCH_CODE "+
                "INNER JOIN am_ad_category am_ad_category ON AM_ASSET.CATEGORY_CODE = am_ad_category.category_code "+
                "INNER JOIN am_ad_department am_ad_department ON AM_ASSET.BRANCH_CODE = am_ad_department.Dept_code "+
                "INNER JOIN am_asset_disposal_Upload am_asset_disposal_Upload ON AM_ASSET.Asset_id = am_asset_disposal_Upload.asset_id "+
                "INNER JOIN am_asset_approval am_asset_approval ON convert(varchar ,am_asset_disposal_Upload.Disposal_ID) = am_asset_approval.batch_id, "+
                "am_gb_company am_gb_company "+
                "WHERE "+
                "am_asset_disposal_Upload.APPROVAL_STATUS = 'ACTIVE' "+
//                "AND am_asset_disposal_Upload.DISPOSAL_DATE BETWEEN ? AND ? "+
                "AND convert(varchar,am_asset_disposal_Upload.DISPOSAL_ID)  = ? "+
//                "AND am_asset_disposal_Upload.DISPOSAL_DATE BETWEEN ? AND ? "+
                " ORDER BY AM_ASSET.BRANCH_CODE ASC,AM_ASSET.CATEGORY_CODE ASC,AM_ASSET.GROUP_ID ASC";
        
//            System.out.println("======>>>>>>>ColQuery: "+ColQuery);
            java.util.ArrayList list =rep.getReportDisposalBulkUploadExportRecords(ColQuery,groupid);
            if(list.size()!=0){
                HSSFWorkbook workbook = new HSSFWorkbook();
                HSSFSheet sheet = workbook.createSheet("Depreciation Charges Export");
                HSSFRow rowhead = sheet.createRow((int) 0);
                
                rowhead.createCell((int) 0).setCellValue("SOL");
                rowhead.createCell((int) 1).setCellValue("Asset Id");
                rowhead.createCell((int) 2).setCellValue("Asset Description");
                rowhead.createCell((int) 3).setCellValue("Cost Price");
                rowhead.createCell((int) 4).setCellValue("Department");
                rowhead.createCell((int) 5).setCellValue("Disposal Date");
                rowhead.createCell((int) 6).setCellValue("Disposal Amount");
                rowhead.createCell((int) 7).setCellValue("Profit/Loss");
                rowhead.createCell((int) 8).setCellValue("Asset User");
                rowhead.createCell((int) 9).setCellValue("QR/BAR Code");
                rowhead.createCell((int) 10).setCellValue("Batch Id");
                int i = 1;

//     System.out.println("<<<<<<list.size(): "+list.size());
                for(int k=0;k<list.size();k++)
                {
                    com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);
                    String assetId =  newassettrans.getAssetId();
                    String batchId =  newassettrans.getIntegrifyId();
                    double costPrice = newassettrans.getCostPrice();
                    double disposalPrice = newassettrans.getImprovcostPrice();
                    String Description = newassettrans.getDescription();
                    String assetUser = newassettrans.getAssetUser();
                    String QRCode = newassettrans.getBarCode();  
                    String solId = newassettrans.getBranchCode();
                    double profitLoss = newassettrans.getTotalCost();
                    if(profitLoss<0){profitLoss = profitLoss*-1;}
                    String disposalDate = newassettrans.getDepDate();
                    String deptName = newassettrans.getDeptName();
//                    deptCode = records.getCodeName("select DEPT_NAME from am_ad_department where DEPT_CODE = ? ",deptCode);
                    
                    HSSFRow row = sheet.createRow((int) i);

                    row.createCell((int) 0).setCellValue(solId);
                    row.createCell((int) 1).setCellValue(assetId);
                    row.createCell((int) 2).setCellValue(Description);
                    row.createCell((int) 3).setCellValue(costPrice);
                    row.createCell((int) 4).setCellValue(deptName);
                    row.createCell((int) 5).setCellValue(disposalDate);
                    row.createCell((int) 6).setCellValue(disposalPrice);
                    row.createCell((int) 7).setCellValue(profitLoss);
                    row.createCell((int) 8).setCellValue(assetUser);
                    row.createCell((int) 9).setCellValue(QRCode);
                    row.createCell((int) 10).setCellValue(batchId);
                    i++;
                }
                OutputStream stream = response.getOutputStream();
//              new MailSender().AttachmentMail("ayomidematanmi4@gmail.com",file);
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