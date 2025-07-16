package com.magbel.legend.servlet;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
   
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
   


import legend.objects.PersistenceServiceDAO;
import magma.AssetRecordsBean;
import ng.com.magbel.token.ParallexTokenClass;
import ng.com.magbel.token.ZenithTokenClass;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.json.JSONArray;
import org.json.JSONObject;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.util.DataConnect;
import com.magbel.util.DatetimeFormat;

import au.com.bytecode.opencsv.CSVWriter;
import magma.AssetRecordsBean;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
   
public class DepreciationBulkPosting_11_11_2024 extends HttpServlet
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
		String BatchApiUrl = prop.getProperty("BatchApiUrl");
//		System.out.println("BatchApiUrl: " + BatchApiUrl);
//		String BatchStatusUrl = prop.getProperty("BatchStatusApiUrl");
//		System.out.println("BatchStatusApiUrl: " + BatchStatusUrl);
//		String BatchPostingUrl = prop.getProperty("BatchPostExceptionApiUrl");
//		System.out.println("BatchPostingExceptionApiUrl: " + BatchPostingUrl);
		
		String bank = prop.getProperty("bank");
//		System.out.println("bank: " + bank);
		
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
	   if(batchNo.equals("")){batchNo ="123456";} 
//	   if(!BatchStatusUrl.equals("")){
//		   try {
//				 String status = ZenithTokenClass.batchStatusValidation(batchNo);
//				   JSONObject json = new JSONObject(status);
//				   JSONArray jArray = json.getJSONArray("data");
//				 // System.out.println(json.toString());
//				  
//				  String responseCode =  json.getString("responseCode");
//				  String responseDescription = json.getString("responseDescription");
//				  
//				  System.out.println("Response Code: " + responseCode);
//				  System.out.println("Response Description: " + responseDescription);
//				  System.out.println("\n");
////				  System.out.println(jArray.toString());
//			  System.out.println(" Batch Details: ");
//				  for(int i=0; i<jArray.length(); i++) {
//					 // System.out.println("Array Length:" + jArray.length());
//					  JSONObject json2 = jArray.getJSONObject(i);
//					  String reponseStatus = json2.getString("status");
//					  String statusDesc = json2.getString("statusDesc");
//					  JSONObject json3 = json2.getJSONObject("batch");
//					  String totalCount = String.valueOf(json3.getInt("totalCount"));
//					  String processed = String.valueOf(json3.getInt("processed"));
//					  String pending = String.valueOf(json3.getInt("pending"));
//					  String failed = String.valueOf(json3.getInt("failed"));
//					  String suspense = String.valueOf(json3.getInt("suspense"));
//					  
//					 // System.out.println("json3:" + json3);
//					  System.out.println("totalCount:" + totalCount);
//					  System.out.println("processed:" + processed);
//					  System.out.println("pending:" + pending);
//					  System.out.println("failed:" + failed);
//					  System.out.println("suspense:" + suspense);
//					  System.out.println("reponseStatus:" + reponseStatus);
//					  //System.out.println("Processed: " + processed);
//				  }
//			}catch(Exception e) {
//				e.getMessage();
//			}
//		  }
//	   if(!BatchPostingUrl.equals("")){
//		   try{ 
//		   String status = ZenithTokenClass.batchPostingExceptionValidation(batchNo);
//		   JSONObject json = new JSONObject(status);
//		   JSONArray jArray = json.getJSONArray("data");
//		   Connection conn;
//		//  System.out.println(json.toString());
//		  
//		 // System.out.println("\n");
//		  System.out.println(jArray.toString());
//		  for(int i=0; i<jArray.length(); i++) {
//	//		System.out.println("Array Length:" + jArray.length());
//			  JSONObject json2 = jArray.getJSONObject(i);
////			  System.out.println(" Data Details: ");
//			  String currNo = json2.getString("currNo");
//			  String seqNo = json2.getString("seqNo");
//			  String errorCode = json2.getString("errorCode");
//			  String accountNo = json2.getString("parameters");
//			  String errorMessage = json2.getString("errorMessage");
//			  String currentDate = sdf.format(date);
//			 // System.out.println(currentDate);
//			  //System.out.println(errorMessage);
//			 String sql = "insert into AM_GB_POSTING_EXCEPTION (batch_no, current_no, sequence_no, error_code, account_no, error_msg, user_id, create_date) values\r\n"
//			 		+ "(?, ?, ?, ?, ?, ?, ?, ?)";
//			  conn = getConnection();
//			  ps = conn.prepareStatement(sql);
//			  ps.setString(1, batchNo);
//			  ps.setString(2, currNo);
//			  ps.setString(3, seqNo);
//			  ps.setString(4, errorCode);
//			  ps.setString(5, accountNo);
//			  ps.setString(6, errorMessage);
//			  ps.setString(7, userId);
//			  ps.setString(8, currentDate);
//			  int x = ps.executeUpdate();
//			  if(x > 0) {
//				  System.out.println("Inserted data successfully..");
//			  }
//			 
//			 
//			  
//		  }
//		   }catch(Exception e){
//			   e.getMessage();
//	   		}
//		  }
	   String tableName = "FINACLE_EXT"; String columnName = "BATCH_NO";
	  
//	   PrintWriter out = response.getWriter();
//    OutputStream out = null;
	   sdf = new java.text.SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		mail= new EmailSmsServiceBus();
        records = new ApprovalRecords();
        String branchId = request.getParameter("branch_id");  
        String processingDate = request.getParameter("processingDate");  
        String type = request.getParameter("type");
        System.out.println("type: " + type);
//        String processing_date = records.getCodeName("select processing_date from am_gb_company");
//        String yyyy = processing_date.substring(0, 4);
//        String Month = processing_date.substring(5, 7);
//        String dd = processing_date.substring(8, 10); 
//        String processingDate = dd+"/"+Month+"/"+yyyy;
        records.updateBatchPostingWithBatchId(tableName,columnName,batchNo);
//        System.out.println("<<<<<<yyyy: "+yyyy+"    Month: "+Month+"    YYYY: "+yyyy+"    processingDate: "+processingDate);
//        System.out.println("<<<<<<branchId: "+branchId+"    User Id: "+userId);
        String deptCode = records.getCodeName("select dept_code from am_gb_User where USER_ID = "+userId+" ");
        String maker = records.getCodeName("select Legacy_Sys_id from am_gb_User where USER_ID = "+userId+" ");
        String checker = records.getCodeName("select Legacy_Sys_id from am_gb_User where USER_ID = "+userId+" ");
//        System.out.println("<<<<<<branchId: "+branchId+"    dept Code: "+deptCode);
        if(branchId.equals("***")){branchId = records.getCodeName("select BRANCH from am_gb_User where USER_ID = "+userId+" ");}
        String branchCode = records.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID = "+branchId+" ");
//        String subjectTovat = records.getCodeName("select distinct Subject_TO_Vat from AM_ASSET where GROUP_ID = '"+groupid+"'");
//        String subjectTowhTax = records.getCodeName("select distinct Wh_Tax from AM_ASSET where GROUP_ID = '"+groupid+"'");
//        String accountNumber = records.getCodeName("select distinct SUBSTRING(vendor_ac, 1,len(LTRIM(RTRIM(vendor_ac)))-1) from AM_ASSET where GROUP_ID = '"+groupid+"'");
//        String supervisorNameQry = records.getCodeName("select distinct Subject_TO_Vat+':'+substring(Wh_Tax,1,1)+':'+coalesce(TIN,'N')+':'+coalesce(RCNo,'N') from AM_ASSET a, am_ad_vendor v where v.account_number like '%"+accountNumber+"%' and GROUP_ID = '"+groupid+"'  and v.Vendor_Status = 'ACTIVE'");
////        System.out.println("<<<<<<supervisorNameQry: "+supervisorNameQry);
//        String[] sprvResult = supervisorNameQry.split(":");
////        String[] sprvResult = (records.retrieve4Array(supervisorNameQry)).split(":");
//        String subjectTovat = sprvResult[0];
//        String subjectTowhTax = sprvResult[1];
//        String TIN = sprvResult[2];
//        String RCNo = sprvResult[3];
        String userName = records.getCodeName("SELECT USER_NAME FROM am_gb_User WHERE USER_ID = "+userId+"");
        String monthName = records.getCodeName("select CONVERT(varchar(3), getdate(), 100)");
//        System.out.println("<<<<<<branch Id: "+branchId+"   branch_Code: "+branchCode+"  subjectTovat: "+subjectTovat+"  subjectTowhTax: "+subjectTowhTax+"  TIN: "+TIN+"    RCNo: "+RCNo);
//        if(TIN.equalsIgnoreCase("N")){TIN = RCNo; subjectTovat = "N";}
//        Date date = new Date();  
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
        String dateField = DD+"-"+MM+"-"+YYYY;
//        System.out.println("<<<<<<currentDate: "+currentDate);
        String fileName = branchCode+"By"+userName+"FinacleAssetExport.xls";
//        if(!BatchApiUrl.equals("")){fileName = "DE_UPLOAD_"+deptCode+"_"+currentDate+".csv";}
        if(!BatchApiUrl.equals("")){fileName = "DE_UPLOAD_LEGENDDEPR_"+currentDate+".csv";}
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
        String currency = records.getCodeName("SELECT iso_code FROM AM_GB_CURRENCY_CODE WHERE local_currency = 'Y'"); 
        
//        response.setContentType("application/vnd.ms-excel");
//        response.setHeader("Content-Disposition",
//                "attachment; filename="+fileName+"");
        try
        {
        if (!userClass.equals("NULL") || userClass!=null){
            ad = new AssetRecordsBean();

//    if(exists==false){


//            String chargeYear = request.getParameter("reportYear");
//            String reportDate = request.getParameter("reportDate");

//            String assetType  = request.getParameter("assetType");
            Report rep = new Report();
//            System.out.println("<<<<<<assetType: "+assetType);
            String ColQuery = records.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where THIRDPARTY = '"+bank+"' AND TYPE = '"+type+"'");
//           System.out.println("======>>>>>>>ColQuery=======: "+ColQuery);
           String narration = "DEPRECIATON FOR THE MONTH OF " + getMonthPartOfDate(processingDate).toUpperCase() + " " + getYearPartOfDate(processingDate);
           if((appName.equalsIgnoreCase("FLEXICUBE-10.2.18")) && (!BatchApiUrl.equals(""))){
        	   narration = "DEPTN " + getMonthPartOfDate(processingDate).toUpperCase() + " " + getYearPartOfDate(processingDate);
           }
            java.util.ArrayList list =rep.getDepreciationPostingRecords(ColQuery,narration);
            System.out.println("======1>>>>>>>list.size(): "+list.size());
            if(list.size()!=0){
            	if((appName.equalsIgnoreCase("FINACLE-7.0.9")) && (BatchApiUrl.equals(""))){
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
            	if((appName.equalsIgnoreCase("FINACLE-10.2.18")) && (BatchApiUrl.equals(""))){
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
            	
            	if((appName.equalsIgnoreCase("FLEXICUBE-10.2.18")) && (!BatchApiUrl.equals(""))){
//                	System.out.println("======1>>>>>>>appName: "+appName);
//                  response.setContentType("text/csv");
//                  response.setHeader("Content-Disposition",
//                          "attachment; filename="+fileName+"");
                	CSVWriter my_csv_output = null;
                    HSSFWorkbook workbook = new HSSFWorkbook();
                    HSSFSheet sheet = workbook.createSheet("Legacy Asset Export Record");
                    HSSFRow rowhead = sheet.createRow((int) 0);
                    
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
    				 
//                    System.out.println("<<<<<<list.size(): "+list.size());
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
                        String transCode = "FAS";
                        String accountNo = newassettrans.getVendorAC();
                        String purposeCode = "";
                        String status = "N";
                        Description = assetId+"**"+Description+"**"+transCode;
                        HSSFRow row = sheet.createRow((int) i);

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
                        //int recNo =  Integer.parseInt(records.getCodeName("select count(*) from AM_GB_BATCH_POSTING where id = "+i+" AND GROUP_ID = '"+processingDate+"'"));
                        String recNo =  records.getCodeName("select count(*) from AM_GB_BATCH_POSTING where id = "+i+" AND GROUP_ID = '"+processingDate+"'");
//                        System.out.println("recNo value is " + recNo);
                        if(!recNo.equals("1")){
                        records.insertBatchTransactions(i, processingDate, dateField, accountNo, costPrice, transType, Description, transCode, currency, branchCode, userId, purposeCode, maker, batchNo, checker,status);
                        }  
//                        records.insertBatchTransactions(i, processingDate, dateField, accountNo, costPrice, transType, Description, transCode, currency, branchCode, userId, purposeCode, maker, batchNo, checker,status);
                        i++;  
                      //  System.out.println("filePath value is " + filePath + fileName);
                        
                        
                     String fullPath = filePath +"\\DE_UPLOAD_LEGENDDEPR_"+currentDate+".csv";
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
              		 response.sendRedirect("DocumentHelp.jsp?np=legacyExporter&s=n");
                   // OutputStream stream = response.getOutputStream();
//                  new MailSender().AttachmentMail("ayomidematanmi4@gmail.com",file);
                //    my_csv_output.close();
//                  workbook.write(stream);
//                  stream.close();
                  System.out.println("Data is saved in CSV file.");
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

public String getMonthPartOfDate(String date) {
String output = null;
    try {SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
Date myDate = sdf.parse(date);
java.util.Calendar cal = java.util.Calendar.getInstance();
cal.setTime(myDate);
int month = cal.get(Calendar.MONTH);

String[] monthName = new String[]{"January","February","March","April","May","June","July","August","September","October","November","December"};
 output = monthName[month];
}catch(Exception e){e.printStackTrace();}
return output;

}   


public String getYearPartOfDate(String date) {
String output = null;
    try {SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
Date myDate = sdf.parse(date);
java.util.Calendar cal = java.util.Calendar.getInstance();
cal.setTime(myDate);
output = Integer.toString(cal.get(Calendar.YEAR));


}catch(Exception e){e.printStackTrace();}
return output;

}

}
