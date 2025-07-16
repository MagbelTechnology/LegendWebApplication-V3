package com.magbel.legend.servlet;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
   
import java.io.PrintWriter;
import java.util.Date;
import java.util.Properties;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
   

import magma.AssetRecordsBean;
import ng.com.magbel.token.ParallexTokenClass;
import ng.com.magbel.token.ZenithTokenClass;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONObject;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.util.DatetimeFormat;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
   
public class FinacleAssetUploadExport extends HttpServlet
{
	private EmailSmsServiceBus mail ;
	private AssetRecordsBean ad;
	private ApprovalRecords records;
	private DatetimeFormat dateFormat;
    java.text.SimpleDateFormat sdf;
   public void doPost(HttpServletRequest request, 
    HttpServletResponse response)
      throws ServletException, IOException
   {
//	   String BatchApiUrl = request.getParameter("BatchApiUrl");
	   
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
        String deptCode = records.getCodeName("select dept_code from am_gb_User where USER_ID = "+userId+" ");
//        System.out.println("<<<<<<branchId: "+branchId+"    Group Id: "+groupid);
        if(branchId.equals("***")){branchId = records.getCodeName("select BRANCH from am_gb_User where USER_ID = "+userId+" ");}
        String branchCode = records.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID = "+branchId+" ");
//        String subjectTovat = records.getCodeName("select distinct Subject_TO_Vat from AM_ASSET where GROUP_ID = '"+groupid+"'");
//        String subjectTowhTax = records.getCodeName("select distinct Wh_Tax from AM_ASSET where GROUP_ID = '"+groupid+"'");
        String accountNumber = records.getCodeName("select distinct SUBSTRING(vendor_ac, 1,len(LTRIM(RTRIM(vendor_ac)))-1) from AM_ASSET where GROUP_ID = '"+groupid+"'");
        String supervisorNameQry = records.getCodeName("select distinct Subject_TO_Vat+':'+substring(Wh_Tax,1,1)+':'+coalesce(TIN,'N')+':'+coalesce(RCNo,'N') from AM_ASSET a, am_ad_vendor v where v.account_number like '%"+accountNumber+"%' and GROUP_ID = '"+groupid+"'  and v.Vendor_Status = 'ACTIVE'");
//        System.out.println("<<<<<<supervisorNameQry: "+supervisorNameQry);
        String[] sprvResult = supervisorNameQry.split(":");
//        String[] sprvResult = (records.retrieve4Array(supervisorNameQry)).split(":");
        String subjectTovat = sprvResult[0];
        String subjectTowhTax = sprvResult[1];
        String TIN = sprvResult[2];
        String RCNo = sprvResult[3];
        String userName = records.getCodeName("SELECT USER_NAME FROM am_gb_User WHERE USER_ID = "+userId+"");
        String monthName = records.getCodeName("select CONVERT(varchar(3), getdate(), 100)");
//        System.out.println("<<<<<<branch Id: "+branchId+"   branch_Code: "+branchCode+"  subjectTovat: "+subjectTovat+"  subjectTowhTax: "+subjectTowhTax+"  TIN: "+TIN+"    RCNo: "+RCNo);
        if(TIN.equalsIgnoreCase("N")){TIN = RCNo; subjectTovat = "N";}
        Date date = new Date();  
        System.out.println(sdf.format(date)); 
        String currentDate = sdf.format(date);
        String DD = currentDate.substring(0,2);
        String MM = currentDate.substring(3,5);
        String YYYY = currentDate.substring(6,10);
        String HH = currentDate.substring(11,13);
        String M = currentDate.substring(14,16);
        String SS = currentDate.substring(17,19);
        System.out.println("<<<<<<DD: "+DD+"  <<<<<<MM: "+MM+"  <<<<<<YYYY: "+YYYY+"  <<<<<<HH: "+HH+"  <<<<<<M: "+M+"  <<<<<<SS: "+SS);
        currentDate = DD+MM+YYYY+HH+M+SS;
        String dateField = DD+"-"+monthName+"-"+YYYY;
        System.out.println("<<<<<<currentDate: "+currentDate);
        String fileName = branchCode+"By"+userName+"FinacleAssetExport.xls";
        if(!BatchApiUrl.equals("")){fileName = "DE_UPLOAD_"+deptCode+"_"+currentDate+".csv";}
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
            	 ColQuery = records.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'VATYWHTS'");
//            	 System.out.println("======>>>>>>>ColQuery=======: "+ColQuery);
            }
            if(!subjectTovat.equals("Y")  && subjectTowhTax.equals("S")){  
            	System.out.println("===!subjectTovat.equals('Y')  && subjectTowhTax.equals('S')");
//            	System.out.println("======>>>>>>>subjectTovat: "+subjectTovat+"   subjectTowhTax: "+subjectTowhTax);
                ColQuery = records.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'WHTS'");
                System.out.println("======>>>>>>>ColQuery=======: "+ColQuery);
            }
            if(subjectTovat.equals("Y")  && subjectTowhTax.equals("Y")){
            	System.out.println("===1 subjectTovat.equals('Y')  && !subjectTowhTax.equals('S')");
 //           	 System.out.println("======>>>>>>>subjectTovat: "+subjectTovat+"   subjectTowhTax: "+subjectTowhTax);
                ColQuery = records.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'VATYWHTY'");
//                System.out.println("======>>>>>>>ColQuery=======: "+ColQuery);
            }
            if(!subjectTovat.equals("Y")  && subjectTowhTax.equals("F")){
            	System.out.println("===!subjectTovat.equals('Y')  && subjectTowhTax.equals('F')");
//            	System.out.println("======>>>>>>>subjectTovat: "+subjectTovat+"   subjectTowhTax: "+subjectTowhTax);
                ColQuery = records.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'WHTF'");
//                System.out.println("======>>>>>>>ColQuery=======: "+ColQuery);
            }
            if(subjectTovat.equals("Y")  && subjectTowhTax.equals("F")){
            	System.out.println("===2 subjectTovat.equals('Y')  && subjectTowhTax.equals('F')");
//            	 System.out.println("======>>>>>>>subjectTovat: "+subjectTovat+"   subjectTowhTax: "+subjectTowhTax);
                ColQuery = records.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'VATYWHTF'");
            }
            if(subjectTovat.equals("N")  && !subjectTowhTax.equals("N")){  
            	System.out.println("===!subjectTovat.equals('Y')  && subjectTowhTax.equals('S')");
//            	System.out.println("======>>>>>>>subjectTovat: "+subjectTovat+"   subjectTowhTax: "+subjectTowhTax);
                ColQuery = records.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'VATN'");
//                System.out.println("======>>>>>>>ColQuery=======: "+ColQuery);
            }
//            System.out.println("======>>>>>>>ColQuery=======: "+ColQuery);
          //  ColQuery = ColQuery+" AND ASSET_STATUS = 'ACTIVE' ";
    //        System.out.println("======>>>>>>>ColQuery: "+ColQuery);
            java.util.ArrayList list =rep.getFinacleAssetUploadExportRecords(ColQuery,branchCode,groupid);
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