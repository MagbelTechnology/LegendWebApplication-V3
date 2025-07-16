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
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
   
public class LegacyExceptionTransactionPosting extends HttpServlet
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
	   String userBranch =(String) request.getSession().getAttribute("UserCenter");
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
		System.out.println("ThirdPartyLabel: " + ThirdPartyLabel);
		String singleApproval = prop.getProperty("singleApproval");
		System.out.println("singleApproval: " + singleApproval);
		String BatchApiUrl = prop.getProperty("BatchApiUrl");
		System.out.println("BatchApiUrl: " + BatchApiUrl);
		String BatchChannel = prop.getProperty("BatchChannel");
		System.out.println("BatchChannel: " + BatchChannel);
//		String BatchStatusUrl = prop.getProperty("BatchStatusApiUrl");
//		System.out.println("BatchStatusApiUrl: " + BatchStatusUrl);
//		String BatchPostingUrl = prop.getProperty("BatchPostExceptionApiUrl");
//		System.out.println("BatchPostingExceptionApiUrl: " + BatchPostingUrl);
	   String sourceCode = "";
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
        String branchId = request.getParameter("branch_id");  
        String groupid = request.getParameter("groupid");
        String tableName = request.getParameter("tableName");  
        String columnName = request.getParameter("columnName");  
        String deptCode = records.getCodeName("select dept_code from am_gb_User where USER_ID = "+userId+" ");
//       String maker = records.getCodeName("select USER_ID from am_asset_approval where asset_id = "+userId+" ");
        String checker = records.getCodeName("select distinct Legacy_Sys_id from am_gb_User where dept_code = '"+deptCode+"' and Branch = "+userBranch+" and is_supervisor = 'Y' and Legacy_Sys_id != ''");        
        String maker = records.getCodeName("select Legacy_Sys_id from am_gb_User where USER_ID = "+userId+" ");
//        checker = records.getCodeName("select Legacy_Sys_id from am_gb_User where USER_ID = "+checker+" ");
        System.out.println("<<<<<<branchId: "+branchId+"    Group Id: "+groupid);
        if(branchId.equals("***") || branchId.equals(null)){branchId = records.getCodeName("select BRANCH from am_gb_User where USER_ID = "+userId+" ");}
        String branchCode = records.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID = "+branchId+" ");
//        String subjectTovat = records.getCodeName("select distinct Subject_TO_Vat from AM_ASSET where GROUP_ID = '"+groupid+"'");
//        String subjectTowhTax = records.getCodeName("select distinct Wh_Tax from AM_ASSET where GROUP_ID = '"+groupid+"'");
        String accountNumber = records.getCodeName("select distinct SUBSTRING(vendor_ac, 1,len(LTRIM(RTRIM(vendor_ac)))-1) from AM_ASSET where GROUP_ID = '"+groupid+"'");
        String userName = records.getCodeName("SELECT USER_NAME FROM am_gb_User WHERE USER_ID = "+userId+"");
        String monthName = records.getCodeName("select CONVERT(varchar(3), getdate(), 100)");
//        System.out.println("<<<<<<branch Id: "+branchId+"   branch_Code: "+branchCode+"  subjectTovat: "+subjectTovat+"  subjectTowhTax: "+subjectTowhTax+"  TIN: "+TIN+"    RCNo: "+RCNo);
//        records.updateBatchPostingWithBatchId(tableName,columnName, batchNo);
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
        String fileName = branchCode+"By"+userName+"FlexicubeAssetExport.xls";
//        if(!BatchApiUrl.equals("")){fileName = "DE_UPLOAD_"+deptCode+"_"+currentDate+".csv";}
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

                ColQuery = records.getCodeName("select a.ID,b.GROUP_ID,a.ID_GROUP_ID,BRANCH_CODE,a.ACCOUNT_NO,AMOUNT AS COST_PRICE,"
                		+ "TRANS_DESCRIPTION AS DECRIPTION,a.TRANSCODE,TRANSTYPE from AM_GB_BATCH_POSTING a, AM_GB_POSTING_EXCEPTION b, Approval_Level_setup c "
                		+ "where a.GROUP_ID = b.GROUP_ID and a.TRANS_CODE = c.CODE and a.ID = b.SERIAL_NO and a.GROUP_ID = '"+groupid+"' "
                		+ "group by a.ID,b.GROUP_ID,a.ID_GROUP_ID,BRANCH_CODE,DATE_FIELD,a.ACCOUNT_NO,AMOUNT,TRANS_DESCRIPTION,a.TRANSCODE,TRANSTYPE ");
//                System.out.println("======>>>>>>>ColQuery=======: "+ColQuery);

//            System.out.println("======>>>>>>>ColQuery=======: "+ColQuery);
          //  ColQuery = ColQuery+" AND ASSET_STATUS = 'ACTIVE' ";
//           System.out.println("======>>>>>>>ColQuery: "+ColQuery);   
            java.util.ArrayList list =rep.getLegacyExceptionpPostRecords(ColQuery);
            System.out.println("<<<<<<list.size()====: "+list.size());
            if(list.size()!=0){
            	
            	if((appName.equalsIgnoreCase("FLEXICUBE-10.2.18")) && (!BatchApiUrl.equals(""))){
                	System.out.println("======1>>>>>>>appName: "+appName);
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
                    rowhead.createCell((int) 11).setCellValue("SOURCE CODE");
                    rowhead.createCell((int) 12).setCellValue("MAKER_ID");
                    rowhead.createCell((int) 13).setCellValue("CHECKER_ID");
                    int i = 1;
    				
                    System.out.println("<<<<<<list.size(): "+list.size());
                    for(int k=0;k<list.size();k++)
                    {
                        com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);
                        String assetId =  newassettrans.getAssetId();
                        String id =  newassettrans.getIntegrifyId();
                        String recType =  newassettrans.getBarCode();
                        String sbuCode =  newassettrans.getSbuCode();
                        String branch_Code =  newassettrans.getBranchCode();
                        double costPrice = newassettrans.getCostPrice();
                        String Description = newassettrans.getDescription();
                        String transType = newassettrans.getAssetType();
                        String transCode = "FAS";
//                        String transCode = newassettrans.getAssetUser();
                        String accountNo = newassettrans.getVendorAC();
                        String purposeCode = "";
                        String status = "N";
//                        Description = assetId+"**"+Description+"**"+transCode;
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
                        row.createCell((int) 13).setCellValue(sourceCode);
                        
                        //int recNo =  Integer.parseInt(records.getCodeName("select count(*) from AM_GB_BATCH_POSTING where id = "+i+" AND GROUP_ID = '"+processingDate+"'"));
//                        String recNo =  records.getCodeName("select count(*) from AM_GB_BATCH_POSTING where id = "+id+" AND GROUP_ID = '"+groupid+"'");
//                        System.out.println("recNo value is " + recNo);
//                        if(!recNo.equals("1")){
//                        records.insertBatchTransactions(i, groupid, dateField, accountNo, costPrice, transType, Description, transCode, currency, branchCode, userId, purposeCode, maker, batchNo, checker,status);
//                        }  
                        i++;
                        String fullPath = filePath +"\\DE_UPLOAD_LEGENDEXCEPT_"+currentDate+".csv";
//                      String fullPath = "C:\\Users\\matanmi\\Downloads\\Balance.csv";
                       FileWriter my_csv=new FileWriter(fullPath);
                      my_csv_output=new CSVWriter(my_csv); 
              	
              		
              		//FileOutputStream fileOut = new FileOutputStream(filename);  
              		 Iterator<Row> rowIterator = sheet.iterator();
              		
              		//CSVPrinter csvPrinter = new CSVPrinter(out, CSVFormat.DEFAULT.withDelimiter(';'));
              		 while(rowIterator.hasNext()) {
                           Row rows = rowIterator.next(); 
                           int x=0;//String array
                           //change this depending on the length of your sheet
                           String[] csvdata = new String[14];
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
//              		 my_csv_output.close();
//              		 response.sendRedirect("DocumentHelp.jsp?np=legacyExporter&s=n");
                   }    
                   my_csv_output.close();
             		 response.sendRedirect("DocumentHelp.jsp?np=legacyPostingExporter&s=n");
                  // OutputStream stream = response.getOutputStream();
//                 new MailSender().AttachmentMail("ayomidematanmi4@gmail.com",file);
               //    my_csv_output.close();
//                 workbook.write(stream);
//                 stream.close();
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

}

