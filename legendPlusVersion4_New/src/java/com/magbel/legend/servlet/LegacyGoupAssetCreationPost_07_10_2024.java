package com.magbel.legend.servlet;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
   
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
   

import magma.AssetRecordsBean;
import ng.com.magbel.token.ParallexTokenClass;
import ng.com.magbel.token.ZenithTokenClass;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.json.JSONArray;
import org.json.JSONObject;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.util.DataConnect;
import com.magbel.util.DatetimeFormat;

import au.com.bytecode.opencsv.CSVWriter;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
   
public class LegacyGoupAssetCreationPost extends HttpServlet
{
	private EmailSmsServiceBus mail ;
	private AssetRecordsBean ad;
	private ApprovalRecords records;
	private DatetimeFormat dateFormat;
    java.text.SimpleDateFormat sdf;
    PreparedStatement ps = null;
	ResultSet rs = null;
	
   public void doPost(HttpServletRequest request, 
    HttpServletResponse response)
      throws ServletException, IOException
   {
//	   String BatchApiUrl = request.getParameter("BatchApiUrl");
	   String userId =(String) request.getSession().getAttribute("CurrentUser");
	   String userClass = (String) request.getSession().getAttribute("UserClass");
//	   PrintWriter out = response.getWriter();
//    OutputStream out = null;
	   Date date = new Date();  
	   sdf = new java.text.SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		mail= new EmailSmsServiceBus();
        records = new ApprovalRecords();
	   
		Properties prop = new Properties();
		File file = new File("C:\\Property\\LegendPlus.properties");
		FileInputStream input = new FileInputStream(file);
		prop.load(input);

		String ThirdPartyLabel = prop.getProperty("ThirdPartyLabel");
//		System.out.println("ThirdPartyLabel: " + ThirdPartyLabel);
		String singleApproval = prop.getProperty("singleApproval");
//		System.out.println("singleApproval: " + singleApproval);
		String BatchApiUrl = prop.getProperty("BatchApiUrl");
//		System.out.println("BatchApiUrl: " + BatchApiUrl);
		String BatchChannel = prop.getProperty("BatchChannel");
		String BatchFolder = prop.getProperty("BatchFolder");
//		System.out.println("BatchChannel: " + BatchChannel);
//		String BatchStatusUrl = prop.getProperty("BatchStatusApiUrl");
//		System.out.println("BatchStatusApiUrl: " + BatchStatusUrl);
//		String BatchPostingUrl = prop.getProperty("BatchPostExceptionApiUrl");
//		System.out.println("BatchPostingExceptionApiUrl: " + BatchPostingUrl);
		
		String fileNameOnly="";
		String fullPath="";
		
	   String batchNo ="";
	   if(!BatchApiUrl.equals("")){
	   try{ 
	   String status = ZenithTokenClass.validation();
	   System.out.println("status >>>> " + status);
	   JSONObject json = new JSONObject(status);
	   batchNo = json.getString("batchId");
	   System.out.println("batchNo ====>>>> " + batchNo);
	   }catch(Exception e){
		   e.getMessage();
   		}
	  }
	   String branchId = request.getParameter("branch_id"); 
	   String valueDate ="";
	   try{ 
	 	   String dateStatus = ZenithTokenClass.postingDateValidation(branchId);
		   JSONObject jsonDate = new JSONObject(dateStatus);
		   String postingDate = jsonDate.getJSONObject("data").getString("postingDate");
		   System.out.println("postingDate >>>> " + postingDate);
		   valueDate =postingDate;
	   }catch(Exception e){
		   e.getMessage();
   		}
	   
//	   if(batchNo.equals("")){batchNo ="123456";} 
//        String branchId = request.getParameter("branch_id");  
        String groupid = request.getParameter("groupid");
        String assetStatus = request.getParameter("Asset_Status");
        System.out.println("assetStatus: " + assetStatus);
        String tableName = request.getParameter("tableName");  
        String columnName = request.getParameter("columnName"); 
        String MenuPage = request.getParameter("MenuPage"); 
        System.out.println("<<<<<<groupid: "+groupid);
        String maker = records.getCodeName("select USER_ID from am_asset_approval where asset_id = '"+groupid+"' ");
        String checker = records.getCodeName("select super_id from am_asset_approval where asset_id = '"+groupid+"' ");
        String deptCode = records.getCodeName("select dept_code from am_gb_User where USER_ID = "+userId+" ");
        maker = records.getCodeName("select Legacy_Sys_id from am_gb_User where USER_ID = "+maker+" ");
        checker = records.getCodeName("select Legacy_Sys_id from am_gb_User where USER_ID = "+checker+" ");
        System.out.println("<<<<<<branchId: "+branchId+"    Group Id: "+groupid);
        if(branchId.equals("***") || branchId.equals(null)){branchId = records.getCodeName("select BRANCH from am_gb_User where USER_ID = "+userId+" ");}
        String branchCode = records.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID = "+branchId+" ");
//        String subjectTovat = records.getCodeName("select distinct Subject_TO_Vat from AM_ASSET where GROUP_ID = '"+groupid+"'");
//        String subjectTowhTax = records.getCodeName("select distinct Wh_Tax from AM_ASSET where GROUP_ID = '"+groupid+"'");
        String accountNumber = records.getCodeName("select distinct SUBSTRING(vendor_ac, 1,len(LTRIM(RTRIM(vendor_ac)))-1) from AM_ASSET where GROUP_ID = '"+groupid+"'");
        String subjectTovat = "N";
        String subjectTowhTax = "N";
        String TIN = "N";
        String RCNo = "";
        String supervisorNameQry = "";
        if(batchNo.equals("")){
        if(tableName.equals("AM_GROUP_IMPROVEMENT")){
         supervisorNameQry = records.getCodeName("select distinct TOP 1 Subject_TO_Vat+':'+substring(Wh_Tax,1,1)+':'+coalesce(TIN,'N')+':'+coalesce(RCNo,'N') from am_asset_improvement_Upload a, am_ad_vendor v where v.account_number like '%"+accountNumber+"%' and Revalue_ID = '"+groupid+"'  and v.Vendor_Status = 'ACTIVE'");
        }
        if(tableName.equals("am_group_asset")){
         supervisorNameQry = records.getCodeName("select distinct TOP 1 Subject_TO_Vat+':'+substring(Wh_Tax,1,1)+':'+coalesce(TIN,'N')+':'+coalesce(RCNo,'N') from AM_ASSET a, am_ad_vendor v where v.account_number like '%"+accountNumber+"%' and GROUP_ID = '"+groupid+"'  and v.Vendor_Status = 'ACTIVE'");
        }
        System.out.println("<<<<<<supervisorNameQry: "+supervisorNameQry);
        String[] sprvResult = supervisorNameQry.split(":");
//        String[] sprvResult = (records.retrieve4Array(supervisorNameQry)).split(":");
         subjectTovat = sprvResult[0];
         subjectTowhTax = sprvResult[1];
         TIN = sprvResult[2];
         RCNo = sprvResult[3];
        }  
        String userName = records.getCodeName("SELECT USER_NAME FROM am_gb_User WHERE USER_ID = "+userId+"");
        String monthName = records.getCodeName("select CONVERT(varchar(3), getdate(), 100)");
//        System.out.println("<<<<<<branch Id: "+branchId+"   branch_Code: "+branchCode+"  subjectTovat: "+subjectTovat+"  subjectTowhTax: "+subjectTowhTax+"  TIN: "+TIN+"    RCNo: "+RCNo);
        if(TIN.equalsIgnoreCase("N")){TIN = RCNo; subjectTovat = "N";}
       
        records.updateBatchPostingWithBatchId(tableName,columnName, batchNo,groupid);
        
        System.out.println(sdf.format(date));    
        String currentDate = sdf.format(date);
        String DD = currentDate.substring(0,2);
        String MM = currentDate.substring(3,5);
        String YYYY = currentDate.substring(6,10);
        String HH = currentDate.substring(11,13);
        String M = currentDate.substring(14,16);
        String SS = currentDate.substring(17,19);
//        System.out.println("<<<<<<DD: "+DD+"  <<<<<<MM: "+MM+"  <<<<<<YYYY: "+YYYY+"  <<<<<<HH: "+HH+"  <<<<<<M: "+M+"  <<<<<<SS: "+SS);
        currentDate = DD+MM+YYYY+HH+M+SS;
        String dateField = DD+"/"+MM+"/"+YYYY;
//        System.out.println("<<<<<<currentDate: "+currentDate);
        String fileName = branchCode+"By"+userName+"FinacleAssetExport.xls";
        if(!BatchApiUrl.equals("")){fileName = "DE_UPLOAD_"+deptCode+"_"+currentDate+".csv";}
//        String filePath = System.getProperty("user.home")+"\\Downloads";
        String filePath = BatchFolder;
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
        String currency = records.getCodeName("SELECT iso_code FROM AM_GB_CURRENCY_CODE WHERE local_currency = 'Y'"); 
        System.out.println(">>>>>>>appName: "+appName);
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
            String assetType  = request.getParameter("assetType");
            Report rep = new Report();
            System.out.println("<<<<<<assetType: "+assetType);
//   System.out.println("<<<<<<branch_Id====>: "+branch_Id+"    categoryId: "+categoryId+"  branchCode: "+branchCode+"  subjectTovat: "+subjectTovat+"  subjectTowhTax: "+subjectTowhTax);
            String ColQuery = ""; 
            int paramCount = 0;
            System.out.println("======>>>>>>>subjectTovat: "+subjectTovat+"   subjectTowhTax: "+subjectTowhTax);
            if(subjectTovat.equals("Y")  && subjectTowhTax.equals("S")){
            	//paramCount = Integer.parseInt(records.getCodeName("select PREFIX, LEN(PREFIX)- LEN(REPLACE(PREFIX, '?', '') from ACCOUNT_GLPREFIX_PARAM where THIRDPARTY = '"+assetType+"' AND TYPE = 'VATYWHTS'"));
            	paramCount = Integer.parseInt(records.getCodeName("select LEN(PREFIX)- LEN(REPLACE(PREFIX, '?', '')) from ACCOUNT_GLPREFIX_PARAM where THIRDPARTY = '"+assetType+"' AND TYPE = 'VATYWHTS'"));
            	System.out.println("===subjectTovat.equals('Y')  && subjectTowhTax.equals('S')");
            	 ColQuery = records.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where THIRDPARTY = '"+assetType+"' AND TYPE = 'VATYWHTS'");
//            	 System.out.println("======>>>>>>>ColQuery=======: "+ColQuery);
            }
            if(!subjectTovat.equals("Y")  && subjectTowhTax.equals("S")){  
            	//paramCount = Integer.parseInt(records.getCodeName("select PREFIX, LEN(PREFIX)- LEN(REPLACE(PREFIX, '?', '') from ACCOUNT_GLPREFIX_PARAM where THIRDPARTY = '"+assetType+"' AND TYPE = 'VATNWHTS'"));
            	paramCount = Integer.parseInt(records.getCodeName("select LEN(PREFIX)- LEN(REPLACE(PREFIX, '?', '')) from ACCOUNT_GLPREFIX_PARAM where THIRDPARTY = '"+assetType+"' AND TYPE = 'VATNWHTS'"));
            	System.out.println("===!subjectTovat.equals('Y')  && subjectTowhTax.equals('S')");
            	System.out.println("======>>>>>>>subjectTovat: "+subjectTovat+"   subjectTowhTax: "+subjectTowhTax);
                ColQuery = records.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where THIRDPARTY = '"+assetType+"' AND TYPE = 'VATNWHTS'");
//                System.out.println("======>>>>>>>ColQuery=======: "+ColQuery);
            }
//            if(subjectTovat.equals("Y")  && subjectTowhTax.equals("Y")){
//            	System.out.println("===1 subjectTovat.equals('Y')  && !subjectTowhTax.equals('S')");
// //           	 System.out.println("======>>>>>>>subjectTovat: "+subjectTovat+"   subjectTowhTax: "+subjectTowhTax);
//                ColQuery = records.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'VATYWHTY'");
//                 System.out.println("======>>>>>>>ColQuery=======: "+ColQuery);
//            }
            if(!subjectTovat.equals("Y")  && subjectTowhTax.equals("F")){
            	System.out.println("===!subjectTovat.equals('Y')  && subjectTowhTax.equals('F')");
            	paramCount = Integer.parseInt(records.getCodeName("select LEN(PREFIX)- LEN(REPLACE(PREFIX, '?', '')) from ACCOUNT_GLPREFIX_PARAM where THIRDPARTY = '"+assetType+"' AND TYPE = 'VATNWHTF'"));
            	System.out.println("======>>>>>>>subjectTovat: "+subjectTovat+"   subjectTowhTax: "+subjectTowhTax);
                ColQuery = records.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where THIRDPARTY = '"+assetType+"' AND TYPE = 'VATNWHTF'");
//                System.out.println("======>>>>>>>ColQuery=======: "+ColQuery);
            }
            if(subjectTovat.equals("Y")  && subjectTowhTax.equals("F")){
            	System.out.println("===2 subjectTovat.equals('Y')  && subjectTowhTax.equals('F')");
            	paramCount = Integer.parseInt(records.getCodeName("select LEN(PREFIX)- LEN(REPLACE(PREFIX, '?', '')) from ACCOUNT_GLPREFIX_PARAM where THIRDPARTY = '"+assetType+"' AND TYPE = 'VATYWHTF'"));
//            	 System.out.println("======>>>>>>>subjectTovat: "+subjectTovat+"   subjectTowhTax: "+subjectTowhTax);
                ColQuery = records.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where THIRDPARTY = '"+assetType+"' AND TYPE = 'VATYWHTF'");
//                System.out.println("======>>>>>>>ColQuery=======: "+ColQuery);
            }
            if(subjectTovat.equals("N")  && subjectTowhTax.equals("N") && MenuPage.equals("groupAssetUploadPosting")){  
            	System.out.println("===subjectTovat.equals('N')  && subjectTowhTax.equals('N')");
            	paramCount = Integer.parseInt(records.getCodeName("select LEN(PREFIX)- LEN(REPLACE(PREFIX, '?', '')) from ACCOUNT_GLPREFIX_PARAM where THIRDPARTY = '"+assetType+"' AND TYPE = 'VATNWHTN'"));
//            	System.out.println("======>>>>>>>subjectTovat: "+subjectTovat+"   subjectTowhTax: "+subjectTowhTax);
                ColQuery = records.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where THIRDPARTY = '"+assetType+"' AND TYPE = 'VATNWHTN'");
//                System.out.println("======>>>>>>>ColQuery=======: "+ColQuery);
            }
            if(subjectTovat.equals("Y")  && subjectTowhTax.equals("N")){  
            	System.out.println("===subjectTovat.equals('Y')  && subjectTowhTax.equals('N')");
            	paramCount = Integer.parseInt(records.getCodeName("select LEN(PREFIX)- LEN(REPLACE(PREFIX, '?', '')) from ACCOUNT_GLPREFIX_PARAM where THIRDPARTY = '"+assetType+"' AND TYPE = 'VATYWHTN'"));
//            	System.out.println("======>>>>>>>subjectTovat: "+subjectTovat+"   subjectTowhTax: "+subjectTowhTax);
                ColQuery = records.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where THIRDPARTY = '"+assetType+"' AND TYPE = 'VATYWHTN'");
//                System.out.println("======>>>>>>>ColQuery=======: "+ColQuery);
            }
            if(subjectTovat.equals("Y")  && subjectTowhTax.equals("Y")){  
            	System.out.println("===subjectTovat.equals('Y')  && subjectTowhTax.equals('Y')");
            	paramCount = Integer.parseInt(records.getCodeName("select LEN(PREFIX)- LEN(REPLACE(PREFIX, '?', '')) from ACCOUNT_GLPREFIX_PARAM where THIRDPARTY = '"+assetType+"' AND TYPE = 'VATYWHTY'"));
            	System.out.println("======>>>>>>>subjectTovat: "+subjectTovat+"   subjectTowhTax: "+subjectTowhTax);
                ColQuery = records.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where THIRDPARTY = '"+assetType+"' AND TYPE = 'VATYWHTY'");
//                System.out.println("======>>>>>>>ColQuery=======: "+ColQuery);
            }    
            if(subjectTovat.equals("N")  && subjectTowhTax.equals("N") && MenuPage.equals("bulkAssetTransferPosting")){  
            	System.out.println("===subjectTovat.equals('N')  && subjectTowhTax.equals('N')");
            	paramCount = Integer.parseInt(records.getCodeName("select LEN(PREFIX)- LEN(REPLACE(PREFIX, '?', '')) from ACCOUNT_GLPREFIX_PARAM where THIRDPARTY = '"+assetType+"' AND TYPE = 'GENERATION'"));
            	System.out.println("======>>>>>>>subjectTovat: "+subjectTovat+"   subjectTowhTax: "+subjectTowhTax);
                ColQuery = records.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where THIRDPARTY = '"+assetType+"' AND TYPE = 'GENERATION'");
//                System.out.println("======>>>>>>>ColQuery=======: "+ColQuery);
            }
            if(subjectTovat.equals("N")  && subjectTowhTax.equals("N") && MenuPage.equals("groupAssetImprovementPosting")){  
            	System.out.println("===subjectTovat.equals('N')  && subjectTowhTax.equals('N')");
            	paramCount = Integer.parseInt(records.getCodeName("select LEN(PREFIX)- LEN(REPLACE(PREFIX, '?', '')) from ACCOUNT_GLPREFIX_PARAM where THIRDPARTY = '"+assetType+"' AND TYPE = 'VATNWHTN'"));
            	System.out.println("======>>>>>>>subjectTovat: "+subjectTovat+"   subjectTowhTax: "+subjectTowhTax);
                ColQuery = records.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where THIRDPARTY = '"+assetType+"' AND TYPE = 'VATNWHTN'");
//                System.out.println("======>>>>>>>ColQuery=======: "+ColQuery);
            }
            if(subjectTovat.equals("N")  && subjectTowhTax.equals("N") && MenuPage.equals("groupUncapitaliseUploadPosting")){  
            	System.out.println("===subjectTovat.equals('N')  && subjectTowhTax.equals('N')");
            	paramCount = Integer.parseInt(records.getCodeName("select LEN(PREFIX)- LEN(REPLACE(PREFIX, '?', '')) from ACCOUNT_GLPREFIX_PARAM where THIRDPARTY = '"+assetType+"' AND TYPE = 'VATNWHTN'"));
            	System.out.println("======>>>>>>>subjectTovat: "+subjectTovat+"   subjectTowhTax: "+subjectTowhTax);
                ColQuery = records.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where THIRDPARTY = '"+assetType+"' AND TYPE = 'VATNWHTN'");
//                System.out.println("======>>>>>>>ColQuery=======: "+ColQuery);
            }
//            System.out.println("======>>>>>>>ColQuery=======: "+ColQuery);
          //  ColQuery = ColQuery+" AND ASSET_STATUS = 'ACTIVE' ";
           System.out.println("======>>>>>>>paramCount: "+paramCount);   
            java.util.ArrayList list =rep.getLegacyAssetGrpPostRecords(ColQuery,branchCode,groupid,subjectTovat,subjectTowhTax,paramCount);
            System.out.println("<<<<<<list.size()====: "+list.size());
            if(list.size()!=0){
            	if((appName.equalsIgnoreCase("FINACLE-7.0.9"))){
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
            	if((appName.equalsIgnoreCase("FINACLE-10.2.18"))){
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
            	if((appName.equalsIgnoreCase("FLEXCUBE-7.0.9")) && (BatchApiUrl.equals(""))){
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
                        row.createCell((int) 10).setCellValue(sbuCode);
                        i++;
                    }
                    OutputStream stream = response.getOutputStream();
//                  new MailSender().AttachmentMail("ayomidematanmi4@gmail.com",file);
                  workbook.write(stream);
                  stream.close();
                  System.out.println("Data is saved in excel file.");
                  }                  
            	String groupId = groupid;
            	if(assetStatus.equals("R")){groupId = "R"+groupId; }
            	if((appName.equalsIgnoreCase("FLEXICUBE-10.2.18"))){
                	System.out.println("======1>>>>>>>appName: "+appName);
                	CSVWriter my_csv_output = null;
                    HSSFWorkbook workbook = new HSSFWorkbook();
                    HSSFSheet sheet = workbook.createSheet("Legacy Asset Export Record");
                    HSSFRow rowhead = sheet.createRow((int) 0);
                    String header1 = "SRLNO";
                    String header2 = "UPLOAD_DATE";
                    String header3 = "ACC NUMBER";
                    String header4 = "AMT";
                    String header5 = "TRANSACTION TYPE";
                    String header6 = "TRANSACTION DESC";
                    String header7 = "TRANSACTION CODE";
                    String header8 = "CURRENCY";
                    String header9 = "BRANCH CODE";
                    String header10 = "PURPOSE CODE";
                    String header11 = "BATCH NUMBER";
                    String header12 = "MAKER";
                    String header13 = "CHECKER";
                     
                    rowhead.createCell((int) 0).setCellValue("SRLNO");
                    rowhead.createCell((int) 1).setCellValue("UPLOAD_DATE");
                    rowhead.createCell((int) 2).setCellValue("ACC NUMBER");
                    rowhead.createCell((int) 3).setCellValue("AMT");
                    rowhead.createCell((int) 4).setCellValue("TRANSACTION TYPE");
                    rowhead.createCell((int) 5).setCellValue("TRANSACTION DESC");
                    rowhead.createCell((int) 6).setCellValue("TRANSACTION CODE");
                    rowhead.createCell((int) 7).setCellValue("CURRENCY");
                    rowhead.createCell((int) 8).setCellValue("BRANCH CODE");
                    rowhead.createCell((int) 9).setCellValue("PURPOSE CODE");
                    rowhead.createCell((int) 10).setCellValue("BATCH NUMBER");
                    rowhead.createCell((int) 11).setCellValue("MAKER");
                    rowhead.createCell((int) 12).setCellValue("CHECKER");
                    int i = 1;
    				
                    System.out.println("<<<<<<list.size(): "+list.size());
                    for(int k=0;k<list.size();k++)
                    {
                        com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);
                        String assetId =  newassettrans.getAssetId();
                        String recType =  newassettrans.getBarCode();
                        String sbuCode =  newassettrans.getSbuCode();
                        String branch_Code =  newassettrans.getBranchCode();
                        double costPrice = newassettrans.getCostPrice();
                        String Description = newassettrans.getDescription();
                        String transType = newassettrans.getAssetType();
                        String transCode = newassettrans.getAssetUser();
                        String accountNo = newassettrans.getVendorAC();
                        String purposeCode = "";
                        String status = "N";
                        Description = assetId+"**"+Description+"**"+transCode;
                        HSSFRow row = sheet.createRow((int) i);
                       String Id = String.valueOf(i);
//                       System.out.println("<<<<<<Value of Id: "+Id);
                        row.createCell((int) 0).setCellValue(String.valueOf(i));
                        row.createCell((int) 1).setCellValue(dateField);
                        row.createCell((int) 2).setCellValue(accountNo);
                        row.createCell((int) 3).setCellValue(String.valueOf(costPrice));
                        row.createCell((int) 4).setCellValue(transType);
                        row.createCell((int) 5).setCellValue(Description);
                        row.createCell((int) 6).setCellValue(transCode);
                        row.createCell((int) 7).setCellValue(currency);
                        row.createCell((int) 8).setCellValue(branch_Code);
                        row.createCell((int) 9).setCellValue(purposeCode);
                        row.createCell((int) 10).setCellValue(batchNo); 
                        row.createCell((int) 11).setCellValue(maker);
                        row.createCell((int) 12).setCellValue(checker);
                        
                        String recNo =  records.getCodeName("select count(*) from AM_GB_BATCH_POSTING where id = "+i+" AND GROUP_ID = '"+groupId+"'");
//                      System.out.println("recNo value is " + recNo);
                      if(!recNo.equals("1")){
                      records.insertBatchTransactions(i, groupId, dateField, accountNo, costPrice, transType, Description, transCode, currency, branchCode, userId, purposeCode, maker, batchNo, checker,status);
                      }  
                        i++;
                        fileNameOnly = "DE_UPLOAD_LEGENDBULKCREATE_"+currentDate+".csv";
                      fullPath = filePath +"\\DE_UPLOAD_LEGENDBULKCREATE_"+currentDate+".csv";
//                       String fullPath = "C:\\Users\\matanmi\\Downloads\\Balance.csv";
                        FileWriter my_csv=new FileWriter(fullPath);
                       my_csv_output=new CSVWriter(my_csv); 
               	
                      
                       
               		//FileOutputStream fileOut = new FileOutputStream(filename);  
               		 Iterator<Row> rowIterator = sheet.iterator();
               		
               		//CSVPrinter csvPrinter = new CSVPrinter(out, CSVFormat.DEFAULT.withDelimiter(';'));
               		 while(rowIterator.hasNext()) {
                            Row rows = rowIterator.next(); 
                            int x=0;//String array
                            //change this depending on the length of your sheet
                            String[] csvdata = new String[13];
                            Iterator<Cell> cellIterator = rows.cellIterator();
                                    while(cellIterator.hasNext()) {
                                            Cell cell = cellIterator.next(); //Fetch CELL
                                            switch(cell.getCellType()) { //Identify CELL type
                                                    //you need to add more code here based on
                                                    //your requirement / transformations
                                            case STRING:
                                                    csvdata[x]= cell.getStringCellValue();                                              
                                                    break;
                                            }
                                            x=x+1;
                                          //  System.out.println("x value is " + x);
                                    }
                    my_csv_output.writeNext(csvdata);
                    }

               		//closing the Stream  
               	//	fileOut.close();  
               		//closing the workbook  
               	//	workbook.close();  
//               		 my_csv_output.close();
//               		 response.sendRedirect("DocumentHelp.jsp?np=legacyExporter&s=n");
                    }    
                    my_csv_output.close();
              		 response.sendRedirect("DocumentHelp.jsp?np="+MenuPage+"&id="+groupid+"&pageDirect=Y");
                   // OutputStream stream = response.getOutputStream();
//                  new MailSender().AttachmentMail("ayomidematanmi4@gmail.com",file);
                //    my_csv_output.close();
//                  workbook.write(stream);
//                  stream.close();
                  System.out.println("Data is saved in CSV file.");
                	}
            	 boolean uploadResponse = flexCubeFileUpload(fullPath);
                 System.out.println("<<<<uploadResponse: " + uploadResponse);
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
   
   public Connection getConnection() {
       Connection con = null;
       try {  
           con = new DataConnect("legendPlus").getConnection();
       } catch (Exception conError) {
           System.out.println("WARNING:Error getting connection - >" +
                              conError);
       } 
       return con;
}

   
   public void closeConnection(Connection con, PreparedStatement ps) {

       try {
           if (ps != null) {
               ps.close();
           }
           if (con != null) {
               con.close();
           }
       } catch (Exception ex) {
           System.out.println("WARNING:Error closing Connection ->" + ex);
       }
   }

   public static boolean flexCubeFileUploadOld(String fullPath,String fileNameOnly) {
		boolean response = false;
		FTPClient ftpClient = new FTPClient();
		try {
		Properties prop = new Properties();
		 File file = new File("C:\\Property\\legendPlus.properties");
        FileInputStream input = new FileInputStream(file);
        prop.load(input);

        String flexcube_address = prop.getProperty("FlexCube-Upload-Address");
        String flexcube_port = prop.getProperty("FlexCube-Upload-Port");
        String flexcube_user = prop.getProperty("FlexCube-Upload-User");
        String flexcube_password = prop.getProperty("FlexCube-Upload-Password");
        String flexcube_folder = prop.getProperty("FlexCube-Upload-Folder");
        
        String server = flexcube_address;
        System.out.println("server==== " +server);
	        int port = Integer.valueOf(flexcube_port);
	        System.out.println("port==== " +port);
	        String user = flexcube_user;
	        System.out.println("user==== " +user);
	        String pass = flexcube_password;
	        System.out.println("pass==== " +pass);
	      
	        ftpClient.connect(server, port);
	        System.out.println("we are here..");
           ftpClient.login(user, pass);
           System.out.println("we are here 2..");
           ftpClient.enterLocalPassiveMode();
           ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
           System.out.println("connected successfully..");
           
	        File secondLocalFile = new File(fullPath);
           String secondRemoteFile = flexcube_folder + fileNameOnly;
           InputStream inputStream = new FileInputStream(secondLocalFile);

           System.out.println("Start uploading file");
           OutputStream outputStream = ftpClient.storeFileStream(secondRemoteFile);
           byte[] bytesIn = new byte[4096];
           int read = 0;

           while ((read = inputStream.read(bytesIn)) != -1) {
               outputStream.write(bytesIn, 0, read);
           }
           inputStream.close();
           outputStream.close();

           boolean completed = ftpClient.completePendingCommand();
           if (completed) {
               System.out.println(" file uploaded successfully.");
               response = true;
           }
		}catch(Exception e) {
			e.getMessage();
		}
        
        return response;
	}

   public static boolean flexCubeFileUpload(String fullPath) {
		boolean response = false;
		 Session session = null;
	     Channel channel = null;
	     ChannelSftp channelSftp = null;
		try {
		Properties prop = new Properties();
		 File file = new File("C:\\Property\\legendPlus.properties");
        FileInputStream input = new FileInputStream(file);
        prop.load(input);

        String flexcube_address = prop.getProperty("FlexCube-Upload-Address");
        String flexcube_port = prop.getProperty("FlexCube-Upload-Port");
        String flexcube_user = prop.getProperty("FlexCube-Upload-User");
        String flexcube_password = prop.getProperty("FlexCube-Upload-Password");
        String flexcube_folder = prop.getProperty("FlexCube-Upload-Folder");
        
//        File theDir = new File(flexcube_folder);
//        if(!theDir.exists()) {
//       	 theDir.mkdir();
//        }
        
        String server = flexcube_address;
	        int port = Integer.valueOf(flexcube_port);
	        String user = flexcube_user;
	        String pass = flexcube_password;
	        
	        String SFTPHOST = server;
	      //  System.out.println("SFTPHOST>>>> " + SFTPHOST);
	        int SFTPPORT = port;
	      //  System.out.println("SFTPPORT>>>> " + SFTPPORT);
	        String SFTPUSER = user;
	       // System.out.println("SFTPUSER>>>> " + SFTPUSER);
	        String SFTPPASS = pass;
	       // System.out.println("SFTPPASS>>>> " + SFTPPASS);
	        String SFTPWORKINGDIR = flexcube_folder;
	       // System.out.println("SFTPWORKINGDIR>>>> " + SFTPWORKINGDIR);

	        System.out.println("preparing the host information for sftp.");
	        
	       // String fileName = "C:/Users/Matanmi/Downloads/Legend Fixed Asset Solution_Business Units_Test Cases.xlsx";

	        JSch jsch = new JSch();
	        session = jsch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
	        session.setPassword(SFTPPASS);
	        java.util.Properties config = new java.util.Properties();
	        config.put("StrictHostKeyChecking", "no");
	        session.setConfig(config);
	        session.connect();
	        System.out.println("Host connected.");
	        channel = session.openChannel("sftp");
	        channel.connect();
	        System.out.println("sftp channel opened and connected.");
	        channelSftp = (ChannelSftp) channel;
	        channelSftp.cd(SFTPWORKINGDIR);
	        File f = new File(fullPath);
	        channelSftp.put(new FileInputStream(f), f.getName());
	        response = true;
	        System.out.println("File transfered successfully to host.");
	        
	       
		}catch(Exception e) {
			e.getMessage();
		}finally {
	        channelSftp.exit();
	        System.out.println("sftp Channel exited.");
	        channel.disconnect();
	        System.out.println("Channel disconnected.");
	        session.disconnect();
	        System.out.println("Host Session disconnected.");
	    }
        
        return response;
	}
   
}

