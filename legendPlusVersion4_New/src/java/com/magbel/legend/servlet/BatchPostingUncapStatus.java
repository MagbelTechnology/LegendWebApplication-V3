package com.magbel.legend.servlet;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
   
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Properties;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
   

import magma.AssetRecordsBean;
import magma.net.dao.MagmaDBConnection;
import ng.com.magbel.token.ParallexTokenClass;
import ng.com.magbel.token.ZenithTokenClass;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.util.DataConnect;
import com.magbel.util.DatetimeFormat;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import legend.AutoIDSetup;
   
public class BatchPostingUncapStatus extends HttpServlet
{
	private EmailSmsServiceBus mail ;
	private AssetRecordsBean ad;
	private ApprovalRecords records;
	private DatetimeFormat dateFormat;
    java.text.SimpleDateFormat sdf;
    PreparedStatement ps = null;
	ResultSet rs = null;
	Connection conn = null;
	private MagmaDBConnection dbConnection;
	
   public void doPost(HttpServletRequest request, 
    HttpServletResponse response)
      throws ServletException, IOException
   {
	   PrintWriter out = response.getWriter();
//	   String BatchApiUrl = request.getParameter("BatchApiUrl");
	   String userId =(String) request.getSession().getAttribute("CurrentUser");
	   String userClass = (String) request.getSession().getAttribute("UserClass");

//	   PrintWriter out = response.getWriter();
//    OutputStream out = null;
	  // PrintWriter out = response.getWriter();
	   Date date = new Date();  
	   sdf = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
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
		String BatchStatusUrl = prop.getProperty("BatchStatusUrl");
		System.out.println("BatchStatusApiUrl: " + BatchStatusUrl);
		String BatchPostingUrl = prop.getProperty("BatchPostingExceptionUrl");
		System.out.println("BatchPostingExceptionApiUrl: " + BatchPostingUrl);
        String tableName = request.getParameter("tableName");  
        String columnName = request.getParameter("columnName2"); 
        String groupid = request.getParameter("groupid");
        String legacyStatus = request.getParameter("legacyStatus");
        String MenuPage = request.getParameter("MenuPage"); 
        String tranType = request.getParameter("tranType");
        String id = request.getParameter("asset_id"); 
 	   String userBranch =(String) request.getSession().getAttribute("UserCenter");
 	   System.out.println("========>>>>userBranch: "+userBranch);
        System.out.println("===== tableName:" + tableName + " columnName: " + columnName); 
        String processed = "0";
        String totalCount = "0";
        String pending = "0";
        String failed = "0";
        String suspense = "0";
       
//	   String batchNo ="";
//	   if(!BatchApiUrl.equals("")){
//	   try{ 
//	   String status = ZenithTokenClass.validation();
//	   JSONObject json = new JSONObject(status);
//	   batchNo = json.getString("batchId");
//	   }catch(Exception e){
//		   e.getMessage();
//   		}
//	  }  
//        String test = "select distinct batch_No from "+tableName+" where "+columnName+" = '"+groupid+"' ";
//        System.out.println("Test=======: " + test);
	   String batchNo = records.getCodeName("select distinct batch_No from "+tableName+" where "+columnName+" = ? ",groupid);
//       String batchNo = "55tq";
//	   batchNo ="218612";
	  // legacyStatus = records.getCodeName("select distinct LEGACY_STATUS from AM_GB_BATCH_POSTING where BATCH_NO  = ?",groupid);
	   legacyStatus = records.getCodeName("select distinct LEGACY_STATUS from AM_GB_BATCH_POSTING where GROUP_ID  = ?",groupid);
	   System.out.println("===== groupid:" + groupid+"  legacyStatus: "+legacyStatus);
	   if(legacyStatus.equalsIgnoreCase("Y")) {
	   boolean result = records.deleteBatachPostingException(groupid);
	   if(batchNo==null){batchNo ="";} 
	   if(!BatchStatusUrl.equals("")){
		   try {
				 String status = ZenithTokenClass.batchStatusValidation(batchNo);
				   JSONObject json = new JSONObject(status);
				   JSONArray jArray = json.getJSONArray("data");
				 // System.out.println(json.toString());
				  
				  String responseCode =  json.getString("responseCode");
				  String responseDescription = json.getString("responseDescription");
				  
				  System.out.println("Response Code: " + responseCode);
				  System.out.println("Response Description: " + responseDescription);
				  System.out.println("\n");
//				  System.out.println(jArray.toString());
			  System.out.println(" Batch Details: ");
				  for(int i=0; i<jArray.length(); i++) {
					 // System.out.println("Array Length:" + jArray.length());
					  JSONObject json2 = jArray.getJSONObject(i);
					  String reponseStatus = json2.getString("status");
					  String statusDesc = json2.getString("statusDesc");
					  JSONObject json3 = json2.getJSONObject("batch");
					  totalCount = String.valueOf(json3.getInt("totalCount"));
					  processed = String.valueOf(json3.getInt("processed"));
					  pending = String.valueOf(json3.getInt("pending"));
					  failed = String.valueOf(json3.getInt("failed"));
					  suspense = String.valueOf(json3.getInt("suspense"));
					  
					 // System.out.println("json3:" + json3);
					  System.out.println("totalCount:" + totalCount);
					  System.out.println("processed:" + processed);
					  System.out.println("pending:" + pending);
					  System.out.println("failed:" + failed);
					  System.out.println("suspense:" + suspense);
					  System.out.println("reponseStatus:" + reponseStatus);
					  //System.out.println("Processed: " + processed);
				  }
			}catch(Exception e) {
				e.getMessage();
			}
		  }
	   if(!BatchPostingUrl.equals("")){
		   try{ 
		   String status = ZenithTokenClass.batchPostingExceptionValidation(batchNo);
		   JSONObject json = new JSONObject(status);
		   JSONObject statusData = json.getJSONObject("data");
		   JSONArray jArray = statusData.getJSONArray("pageItems");
		   
		//  System.out.println(json.toString());
		  
		 // System.out.println("\n");
		  System.out.println(jArray.toString());
		  for(int i=0; i<jArray.length(); i++) {
			System.out.println("Array Length:" + jArray.length());
			  JSONObject json2 = jArray.getJSONObject(i);
			  
			 
			  System.out.println(" Data Details: ");
			  String seqNo = json2.getString("serialNo");
			  String currNo = json2.getString("currNo");
			  String amount = json2.getString("amount");
			  String errorCode = json2.getString("errorCode") == "" ? "NULL" : json2.getString("errorCode");
			  String errorDescription = json2.getString("errorDescription") == "" ? "NULL" : json2.getString("errorDescription");
			  String transType = json2.getString("transType");
			  String accountNo = json2.getString("accountNo");
			  String transDescription = json2.getString("transDescription");
			  String asset_status = json2.getString("status");
			  String displayStatus = json2.getString("displayStatus");
			  String currentDate = sdf.format(date);
			  
//			  System.out.println(" seqNo <<<<<" + seqNo);
//			  System.out.println(" batchNo: "+ batchNo);
//			  System.out.println(" currNo: " + currNo);
//			  System.out.println(" amount: " + amount);
//			  System.out.println(" errorCode: " + errorCode);
//			  System.out.println(" errorDescription: " + errorDescription);
//			  System.out.println(" transType: " + transType);
//			  System.out.println(" accountNo: " + accountNo);
//			  System.out.println(" transDescription <<<<<" + transDescription);
//			  System.out.println(" asset_status: " + asset_status);
//			  System.out.println(" displayStatus: " + displayStatus);
//			  System.out.println(" userId <<<<<" + userId);
//			  System.out.println(" currentDate <<<<<" + currentDate);
//			  System.out.println(" groupid <<<<<" + groupid);
			 String sql = "insert into AM_GB_POSTING_EXCEPTION (serial_no, batch_no, currency_no, amount, error_code, error_description, trans_type, account_no, trans_description, status, display_status, user_id, create_date, group_id) values "
			 		+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			  conn = getConnection("legendPlus");
			  ps = conn.prepareStatement(sql.toString());
			 
			  ps.setString(1, seqNo);
			  ps.setString(2, batchNo);
			  ps.setString(3, currNo);
			  ps.setString(4, amount);
			  ps.setString(5, errorCode);
			  ps.setString(6, errorDescription);
			  ps.setString(7, transType);
			  ps.setString(8, accountNo);
			  ps.setString(9, transDescription);
			  ps.setString(10, asset_status);
			  ps.setString(11, displayStatus);
			  ps.setString(12, userId);
			  ps.setString(13, currentDate);
			  ps.setString(14, groupid);
			  boolean x = ps.execute();
			  if(x==true) {
			  System.out.println("Inserted data successfully..");
			  }
			 
			 
			  
		  }
		   }catch(Exception e){
			   e.getMessage();
	   		}
		  }
	   
	   String FileName = request.getParameter("FileName"); 
	   String assetStatus = request.getParameter("Asset_Status");
	   String status = "";
	   String columnId = request.getParameter("columnId");
	   String columnStatus = request.getParameter("columnStatus");
	   records.updatePostingRecordWithBatchId(batchNo,tableName,columnStatus,columnId);
        String branchId = request.getParameter("branch_id");  
//        String groupid = request.getParameter("groupid");
        String deptCode = records.getCodeName("select dept_code from am_gb_User where USER_ID = "+userId+" ");
        System.out.println("<<<<<<branchId: "+branchId+"    Group Id: "+groupid+"    userBranch: "+userBranch);
        System.out.println("<<<<<<userId: "+userId);
//        if(branchId.equals(null) || branchId.equals("***") || branchId==null ){
//   
//        	 System.out.println("<<<<<<Hello: ");
//        	branchId = records.getCodeName("select BRANCH from am_gb_User where USER_ID = "+userId+" ");
//        	}
        String branchCode = records.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID = "+userBranch+" ");
//        String subjectTovat = records.getCodeName("select distinct Subject_TO_Vat from AM_ASSET where GROUP_ID = '"+groupid+"'");
//        String subjectTowhTax = records.getCodeName("select distinct Wh_Tax from AM_ASSET where GROUP_ID = '"+groupid+"'");
//        String accountNumber = records.getCodeName("select distinct SUBSTRING(vendor_ac, 1,len(LTRIM(RTRIM(vendor_ac)))-1) from AM_ASSET where GROUP_ID = '"+groupid+"'");

        String userName = records.getCodeName("SELECT USER_NAME FROM am_gb_User WHERE USER_ID = "+userId+"");
        String monthName = records.getCodeName("select CONVERT(varchar(3), getdate(), 100)");
//        System.out.println("<<<<<<branch Id: "+branchId+"   branch_Code: "+branchCode+"  subjectTovat: "+subjectTovat+"  subjectTowhTax: "+subjectTowhTax+"  TIN: "+TIN+"    RCNo: "+RCNo);
       
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
        String dateField = DD+"-"+monthName+"-"+YYYY;
//        System.out.println("<<<<<<currentDate: "+currentDate);
        String fileName = branchCode+"By"+userName+"BatchPostingStatus.xls";
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
//        String appName = records.getCodeName("SELECT APP_NAME+'-'+VERSION FROM AM_AD_LEGACY_SYS_CONFIG"); 
        String currency = records.getCodeName("SELECT iso_code FROM AM_GB_CURRENCY_CODE WHERE local_currency = 'Y'"); 
        System.out.println(">>>>>>>currency: "+currency);
       
        try
        {  
        	 AutoIDSetup aid_setup = new AutoIDSetup();
        if (!userClass.equals("NULL") || userClass!=null){
            ad = new AssetRecordsBean();

//    if(exists==false){


//            String chargeYear = request.getParameter("reportYear");
//            String reportDate = request.getParameter("reportDate");
            String tranId = request.getParameter("tranId");
            String categoryId = request.getParameter("category");
            String mails = request.getParameter("mails1");
            String subject = request.getParameter("subject1");
            String msgText   = request.getParameter("msgText1");
            String otherparam  = request.getParameter("otherparam");
            String assetType  = request.getParameter("assetType");
            Report rep = new Report();
            System.out.println("<<<<<<assetType: "+assetType+"  <<<<<<assetStatus: "+assetStatus+"  <<<<<<FileName: "+FileName);
//   System.out.println("<<<<<<branch_Id====>: "+branch_Id+"    categoryId: "+categoryId+"  branchCode: "+branchCode+"  subjectTovat: "+subjectTovat+"  subjectTowhTax: "+subjectTowhTax);
            String ColQuery = "SELECT *FROM AM_GB_POSTING_EXCEPTION WHERE BATCH_NO = ? AND USER_ID = ? "; 
//            System.out.println("======>>>>>>>ColQuery: "+ColQuery+"   batchNo: "+batchNo+"   userId: "+userId);
            java.util.ArrayList list =rep.getBatchExceptionPostingRecords(ColQuery,batchNo,userId);
            System.out.println("<<<<<<list.size()====: "+list.size());
            if(list.size()!=0){
            	 response.setContentType("application/vnd.ms-excel");
                 response.setHeader("Content-Disposition",
                         "attachment; filename="+fileName+"");
                    HSSFWorkbook workbook = new HSSFWorkbook();
                    HSSFSheet sheet = workbook.createSheet("Batch Posting Status Record");
                    HSSFRow rowhead = sheet.createRow((int) 0);
                    rowhead.createCell((int) 0).setCellValue("Id");
                    rowhead.createCell((int) 1).setCellValue("Serial No");
                    rowhead.createCell((int) 2).setCellValue("Batch No");
                    rowhead.createCell((int) 3).setCellValue("Currency number");
                    rowhead.createCell((int) 4).setCellValue("Amount");
                    rowhead.createCell((int) 5).setCellValue("Error Code");
                    rowhead.createCell((int) 6).setCellValue("Error Description");
                    rowhead.createCell((int) 7).setCellValue("Transaction Type");
                    rowhead.createCell((int) 8).setCellValue("Account Number ");
                    rowhead.createCell((int) 9).setCellValue("Transaction Description");
                    rowhead.createCell((int) 10).setCellValue("Status");
                    rowhead.createCell((int) 11).setCellValue("Display Status");
                 //   rowhead.createCell((int) 12).setCellValue("User Id");
                    rowhead.createCell((int) 12).setCellValue("Create Date");
                    rowhead.createCell((int) 13).setCellValue("Group Id");
//                    rowhead.createCell((int) 6).setCellValue("User Id");
                   
                    int i = 1;
    				
                    System.out.println("<<<<<<list.size(): "+list.size());
                    for(int k=0;k<list.size();k++)
                    {
                        com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);
                    	String strId = newassettrans.getAssetId();
                    	String strSerialNo = newassettrans.getBarCode();
                    	String strBatchNo = newassettrans.getSbuCode();
                    	String StrCurrentNo = newassettrans.getDescription();
                    	double StrAmount = newassettrans.getAmount();
                    	String StrErrorCode = newassettrans.getAssetType() == "NULL" ? "" : newassettrans.getAssetType();
                    	String StrErrorDescription =newassettrans.getAssetUser() == "NULL" ? "" : newassettrans.getAssetUser();
                    	String StrTransType = newassettrans.getTranType();
                    	String StrAccountNo = 	newassettrans.getVendorAC();
                    	String StrTransDescription= newassettrans.getAction();
                    	String StrStatus =newassettrans.getAssetStatus();
                    	String StrDisplayStatus = newassettrans.getAssetfunction();
                       // String StrUserId = newassettrans.getUserID();
                        String StrCreateDate = newassettrans.getTransDate();
                        String StrGroupId =newassettrans.getInitiatorId();
                        HSSFRow row = sheet.createRow((int) i);

                        row.createCell((int) 0).setCellValue(strId+",");
                        row.createCell((int) 1).setCellValue(strSerialNo+",");
                        row.createCell((int) 2).setCellValue(strBatchNo+",");
                        row.createCell((int) 3).setCellValue(StrCurrentNo+",");
                        row.createCell((int) 4).setCellValue(StrAmount+",");
                        row.createCell((int) 5).setCellValue(StrErrorCode+",");
                        row.createCell((int) 6).setCellValue(StrErrorDescription+",");
                        row.createCell((int) 7).setCellValue(StrTransType+",");
                        row.createCell((int) 8).setCellValue(StrAccountNo+",");
                        row.createCell((int) 9).setCellValue(StrTransDescription+",");
                        row.createCell((int) 10).setCellValue(StrStatus+",");
                        row.createCell((int) 11).setCellValue(StrDisplayStatus+",");
                      //  row.createCell((int) 12).setCellValue(StrUserId+",");
                        row.createCell((int) 12).setCellValue(StrCreateDate+",");
                        row.createCell((int) 13).setCellValue(StrGroupId+",");
                       
                        i++;
                    }
                    try (OutputStream outputStream = response.getOutputStream()) {
                        workbook.write(outputStream); // Write Excel data to the response output stream
                    } finally {
                    	workbook.close();
                    }
                 //   OutputStream stream = response.getOutputStream();
//                  new MailSender().AttachmentMail("ayomidematanmi4@gmail.com",file);
//                  workbook.write(stream);
//                  stream.close();
                  System.out.println("Data is saved in csv file.");
            	
            }
        }
//        else {
//                out.print("<script>window.location='DocumentHelp.jsp?np=legacyExporter&s=n'</script>");
//            
//        }
   	//	if(assetStatus.equals("A")){
//   		if(Integer.parseInt(processed)>0){        
   		String columnName1 = "";
   		String columnName2 = "";
   		String columnName3 = "";
   		String whereFld = "";
   		String status1 = "Y";
   		String status2 = "Y";
   		String status3 = "";
   		String posttable = "";
   		if(tableName.equals("AM_ASSET_UNCAPITALIZED")){
   	   
   			
   		tableName = "AM_ASSET_UNCAPITALIZED";
   		columnName = "Asset_Status";
   		status = "ACTIVE";
   		whereFld = "POSTING_BATCH";
//   		records.updateAssetWithBatchId(tableName, columnName,  whereFld,  groupid, status);
        String qb = "update a set a.asset_status = 'ACTIVE' FROM AM_ASSET_UNCAPITALIZED a,AM_ASSET_UNCAPITALIZED g where a.Asset_id = g.Asset_id and g.POSTING_BATCH ='"+groupid+"' ";
        ad.updateAssetStatusChange(qb);	
   		 posttable = "am_asset_approval";
  		 status1 = "A";
  		 status2 = "ACTIVE";
  		 String page = "ASSET CREATION RAISE ENTRY"; 
  		records.updateApprovalWithBatchId(tableName, posttable, groupid, status1, status2);
  		tableName = "AM_ASSET_UNCAPITALIZED";   			
   		posttable = "am_raisentry_post";
   		columnName1 = "entryPostFlag";
   		columnName2 = "GroupIdStatus";
   		columnName3 = "";
   		whereFld = "Id";
   		status1 = "Y";
   		status2 = "Y";
   		String query_r ="update a SET a.entryPostFlag = 'Y',a.GroupIdStatus = 'Y' from " +posttable+ " a, "+tableName+" g where CONVERT(VARCHAR, a.id) = CONVERT(VARCHAR, g.Asset_Id) and entryPostFlag = 'N' and GroupIdStatus = 'N' and POSTING_BATCH = '"+groupid+"' " ;
   	    ad.updateAssetStatusChange(query_r);	
   	   	//records.updatePostAssetWithBatchId(tableName, posttable, groupid,status1,status2);
   		//records.updateAssetApprovalWithBatchId(tableName, posttable, groupid, status1, status2); 		
        boolean done = records.insertRaiseEntryTransaction(groupid,page,userId);
        
        }
   		if(tableName.equals("AM_GROUP_ASSET_UNCAPITALIZED")){
   	   	   	tableName = "AM_GROUP_ASSET_UNCAPITALIZED";   			
   	   		posttable = "am_raisentry_post";
   	   		columnName1 = "entryPostFlag";
   	   		columnName2 = "GroupIdStatus";
   	   		columnName3 = "";
   	   		whereFld = "Id";
   	   		status1 = "Y";
   	   		status2 = "Y";
   	   	   	records.updatePostWithBatchId(tableName, posttable, groupid,status1,status2);
   	   			
   	   		tableName = "AM_GROUP_ASSET_UNCAPITALIZED";
   	   		columnName = "Asset_Status";
   	   		status = "ACTIVE";
   	   		whereFld = "POSTING_BATCH";
//   	   		records.updateAssetWithBatchId(tableName, columnName,  whereFld,  groupid, status);
   	        String qa = "update AM_GROUP_ASSET_UNCAPITALIZED set Asset_Status = 'ACTIVE' where POSTING_BATCH='"+groupid+"' ";
   	        ad.updateAssetStatusChange(qa);	
   	        String qb = "update a set a.asset_status = 'ACTIVE' FROM AM_ASSET_UNCAPITALIZED a,AM_GROUP_ASSET_UNCAPITALIZED g where a.Asset_id = g.Asset_id and g.POSTING_BATCH ='"+groupid+"' ";
   	        ad.updateAssetStatusChange(qb);	
	        String qg = "update a SET a.entryPostFlag = 'Y',a.GroupIdStatus = 'Y' from am_raisentry_post a, AM_GROUP_ASSET_UNCAPITALIZED g where CONVERT(VARCHAR, a.id) = CONVERT(VARCHAR, g.group_Id) and g.POSTING_BATCH = '"+groupid+"'";
	        ad.updateAssetStatusChange(qg);	   
	        String qap = "update a SET a.process_status = 'A',a.asset_status = 'ACTIVE' from am_asset_approval a, AM_GROUP_ASSET_UNCAPITALIZED g where CONVERT(VARCHAR, a.asset_id) = CONVERT(VARCHAR, g.group_Id) and POSTING_BATCH = '"+groupid+"'";
	        ad.updateAssetStatusChange(qap);	
	        
   	   		 posttable = "am_asset_approval";
   	  		 status1 = "A";
   	  		 status2 = "ACTIVE";
	  	  	 String page = "ASSET CREATION RAISE ENTRY";
   	   		records.updateApprovalWithBatchId(tableName, posttable, groupid, status1, status2);
            boolean done = records.insertRaiseEntryTransaction(groupid,page,userId);
   	        }
//        if(tableName.equals("AM_GROUP_IMPROVEMENT")){
//   		tableName = "AM_GROUP_IMPROVEMENT";
//   		posttable = "am_raisentry_post";
//   		columnName1 = "entryPostFlag";
//   		columnName2 = "GroupIdStatus";
//   		columnName3 = "";
//   		whereFld = "Id";
//   		status1 = "Y";
//   		status2 = "Y";
//   	   	records.updatePostWithBatchId(tableName, posttable, groupid,status1,status2);
//   		tableName = "AM_GROUP_IMPROVEMENT";
//   		columnName1 = "IMPROVED";
//   		columnName2 = "STATUS";
//   		columnName3 = "approval_status";
//   		 status1 = "Y";
//   		 status2 = "IMPROVED";
//   		 status3 = "POSTED";
//   		whereFld = "POSTING_BATCH";
//   		records.updatePostWithBatchId(tableName, columnName1, columnName2, columnName3,  whereFld, groupid, status1, status2, status3);
////        }
   		
///////Matanmi Commented it   		
   		
//        if(tableName.equals("am_Uncapitalized_improvement")){        
//   		tableName = "am_Uncapitalized_improvement";
//   		posttable = "am_raisentry_post";
//   		columnName1 = "entryPostFlag";
//   		columnName2 = "GroupIdStatus";
//   		columnName3 = "";
//   		whereFld = "Id";
//   		status1 = "Y";
//   		status2 = "Y";
//   	   	records.updatePostWithBatchId(tableName, posttable, groupid,status1,status2);   		
////   		tableName = "am_asset_improvement_Upload";
////   		columnName1 = "IMPROVED";
////   		columnName2 = "STATUS";
////   		columnName3 = "approval_status";
////   		 status1 = "Y";
////   		 status2 = "IMPROVED";
////   		 status3 = "POSTED";
////   		whereFld = "POSTING_BATCH";
////   		records.updatePostWithBatchId(tableName, columnName1, columnName2, columnName3,  whereFld, groupid, status1, status2, status3);
//   		posttable = "am_asset_approval";
//  		 status1 = "A";
//  		 status2 = "ACTIVE";
//   		records.updatePostWithBatchId(tableName, posttable, groupid, status1, status2);
//   		tableName = "AM_ASSET_UNCAPITALIZED";
//   		posttable = "am_Uncapitalized_improvement";
//   		String page = "ASSET IMPROVEMENT RAISE ENTRY";
//   		records.updateImprovementWithBatchId(tableName, posttable, groupid);
//        boolean done = records.insertRaiseEntryTransaction(groupid,page,userId);
//        }
//// 	   if(FileName.equalsIgnoreCase("AssetUploadPosting")) {
//// 		   records.updateAssetWithBatchId("AM_ASSET","Asset_Status","Asset_id", groupid,"ACTIVE");
//// 		}
//// 	   if(FileName.equalsIgnoreCase("groupAssetPosting")) {
//// 		   records.updateAssetWithBatchId("AM_ASSET","Asset_Status","Asset_id", groupid,"ACTIVE");
//// 		} 	   
//// 		   if(FileName.equalsIgnoreCase("groupAssetDisposalPosting")) {
//// 		   records.updateAssetWithBatchId("AM_ASSET","Asset_Status","Asset_id", groupid,"DISPOSED");
//// 		}
//           if(tableName.equals("am_UncapitalizedDisposal")){
////      		records.updatePostWithBatchId(tableName, columnName1, columnName2, whereFld, groupid, status1,status2);
////      		tableName = "AM_GROUP_DISPOSAL";
////      		columnName1 = "DISPOSED";
////      		columnName2 = "STATUS";
////      		columnName3 = "approval_status";
////      		 status1 = "Y";
////      		 status2 = "POSTED";
////      		 status3 = "ACTIVE";
////      		whereFld = "POSTING_BATCH";
////      		records.updatePostWithBatchId(tableName, columnName1, columnName2, columnName3,  whereFld, groupid, status1, status2, status3);
//       		posttable = "am_asset_approval";
//     		 status1 = "A";
//     		 status2 = "ACTIVE";
//      		records.updatePostWithBatchId(tableName, posttable, groupid, status1, status2);
//      		tableName = "am_UncapitalizedDisposal";
//      		columnName1 = "DISPOSED";
//      		columnName2 = "STATUS";
//      		columnName3 = "approval_status";
//      		 status1 = "Y";
//      		 status2 = "POSTED";
//      		 status3 = "ACTIVE";
//      		whereFld = "POSTING_BATCH";
//      		records.updatePostWithBatchId(tableName, columnName1, columnName2, columnName3,  whereFld, groupid, status1, status2, status3);
//      		tableName = "AM_ASSET"; 
//       		posttable = "am_UncapitalizedDisposal";
//     		 status1 = "DISPOSED";
//     		String page = "ASSET DISPOSAL RAISE ENTRY";
//      		records.updatePostWithBatchId(tableName, posttable, groupid, status1, status2);
//      		 boolean done = records.insertRaiseEntryTransaction(groupid,page,userId);
//      		
//           } 
//           if(tableName.equals("am_UncapitalizedTransfer")){
//        	   status3 = "ACTIVE";
////        	   records.updateAssetWithBatchId("am_gb_bulkTransfer","STATUS","BATCH_ID", groupid,"POSTED");      		
//        	   records.updatePostingRecordScript("update am_UncapitalizedTransfer set approval_status = ? where asset_id in (select asset_id from am_gb_bulkTransfer where Batch_id = ?) ",status3,groupid);
//          		tableName = "AM_ASSET";
//           		posttable = "am_UncapitalizedTransfer";
//           		String page = "ASSET TRANSFER RAISE ENTRY";
//           		records.updateTransferWithBatchId(tableName, posttable, groupid);
//           	 boolean done = records.insertRaiseEntryTransaction(groupid,page,userId);
//           } 
////           if(tableName.equals("am_assetReclassification")){
////        	   status3 = "ACTIVE";
////        	   records.updatePostingRecordScript("UPDATE a SET a.Asset_id  = b.new_asset_id,a.OLD_ASSET_ID = b.Asset_id, a.Category_ID = b.new_category_id,a.CATEGORY_CODE = c.category_code "
////        	   		+ "from  am_asset a, am_assetReclassification b, am_ad_category c where a.Asset_id = b.Asset_id and a.Category_ID = c.category_ID and b.status is null and b.POSTING_BATCH = ? ",groupid);
////        	   records.updateAssetWithBatchId("am_assetReclassification","STATUS","POSTING_BATCH", groupid,"A");      		
////          		tableName = "am_assetReclassification";		
////           		posttable = "am_raisentry_post";
////           		columnName1 = "entryPostFlag";
////           		columnName2 = "GroupIdStatus";
////           		columnName3 = "";
////           		whereFld = "Id";
////           		status1 = "Y";
////           		status2 = "Y";
////           	   	records.updatePostAssetWithBatchId(tableName, posttable, groupid,status1,status2);
////           		String page = "ASSET RECLASSIFICATION PAYMENT ENTRY";
////           		posttable = "am_assetReclassification";
////           		records.updateTransferWithBatchId(tableName, posttable, groupid);
////           	 boolean done = records.insertRaiseEntryTransaction(groupid,page,userId);
////           }           
//           if(tableName.equals("am_assetUpdate")){
//        	   status3 = "ACTIVE";
//        	   records.updatePostingRecordScript("UPDATE a SET a.Asset_Status  = b.Status "
//        	   		+ "from  am_asset a, am_assetUpdate b, am_ad_category c where a.Asset_id = b.Asset_id and a.Category_ID = c.category_ID and b.status is null and b.POSTING_BATCH = ? ",groupid);
//        	   records.updateAssetWithBatchId("am_asset","STATUS","POSTING_BATCH", groupid,"A");      		
//          		tableName = "am_assetUpdate";		
//           		posttable = "am_raisentry_post";
//           		columnName1 = "entryPostFlag";
//           		columnName2 = "GroupIdStatus";
//           		columnName3 = "";
//           		whereFld = "Id";
//           		status1 = "Y";
//           		status2 = "Y";
//           	   	records.updatePostAssetWithBatchId(tableName, posttable, groupid,status1,status2);
//           		String page = "ASSET CLOSE RAISE ENTRY";
//           		posttable = "am_assetUpdate";
//           		records.updateTransferWithBatchId(tableName, posttable, groupid);
//           	 boolean done = records.insertRaiseEntryTransaction(groupid,page,userId);
//           }               
////      		if(tableName.equals("FINACLE_EXT")){
////      	   		tableName = "FINACLE_EXT";
////      	   		columnName = "record_processed";
////      	   		status = "Y";
////      	   		groupid = "N";
////      	   		whereFld = "record_processed";
////      	   		records.updateAssetWithBatchId(tableName, columnName,  whereFld,  groupid, status);
////      	   		status = "0";
////      	   		columnName = "processing_status";
////      	   		records.updateAssetWithNoWhereClause(tableName, columnName,status);
////      	        }           
////   		tableName = "am_asset_approval";
////   		columnName = "Asset_Status";
////   		columnName1 = "Process_Status";
////   		columnName2 = "Asset_Status";
////  		 status1 = "A";
////  		 status2 = "ACTIVE";
////   		whereFld = "asset_id";
////   		records.updatePostWithBatchId(tableName, columnName1, columnName2,  whereFld,  groupid, status1, status2);
//           
// Matanmi end of Comment           //
        // Uncapitalized commences
           if(tableName.equals("am_assetUpdate")){
        	   status3 = "ACTIVE";
        	   records.updatePostingRecordScript("UPDATE a SET a.Asset_Status  = b.Asset_Status "
        	   		+ "from  AM_ASSET_UNCAPITALIZED a, am_assetUpdate b, am_ad_category c where a.Asset_id = b.Asset_id and a.Category_ID = c.category_ID and b.Asset_Status is null and b.POSTING_BATCH = ? ",groupid);
//        	   records.updateAssetWithBatchId("am_asset","STATUS","POSTING_BATCH", groupid,"A");      		
          		tableName = "am_assetUpdate";		
           		posttable = "am_raisentry_post";
           		columnName1 = "entryPostFlag";
           		columnName2 = "GroupIdStatus";
           		columnName3 = "";
           		whereFld = "Id";
           		status1 = "Y";
           		status2 = "Y";
           	   	records.updatePostAssetWithBatchId(tableName, posttable, groupid,status1,status2);
           	 System.out.println("am_assetUpdate uncapitalised 1");           		
        		String query_tapp ="update a SET a.process_status = 'A',a.asset_status = 'ACTIVE' from am_asset_approval a, am_assetUpdate g where CONVERT(VARCHAR, a.asset_id) = CONVERT(VARCHAR, g.asset_id) and POSTING_BATCH = '"+groupid+"' ";     		
        		ad.updateAssetStatusChange(query_tapp);              		
           		System.out.println("am_assetUpdate uncapitalised 2");
          		String query_tr ="update a SET a.entryPostFlag = 'Y',a.GroupIdStatus = 'Y' from am_raisentry_post a,am_assetUpdate g where CONVERT(VARCHAR, a.id) = CONVERT(VARCHAR, g.asset_id) and POSTING_BATCH = '"+groupid+"' " ;      		
          		ad.updateAssetStatusChange(query_tr);
          		System.out.println("am_assetUpdate uncapitalised 3");
           		String page = "ASSET CLOSE RAISE ENTRY";
           	 boolean done = records.insertRaiseEntryTransaction(groupid,page,userId);
           	System.out.println("am_assetUpdate uncapitalised 4");
           }  
           if(tableName.equals("am_UncapitalizedTransfer")){
        	   status3 = "ACTIVE";
        	   records.updateAssetWithBatchId("am_UncapitalizedTransfer","approval_status","CONVERT(VARCHAR,Transfer_ID)", groupid,"POSTED");   
        	   System.out.println("am_UncapitalizedTransfer 1");
          		tableName = "AM_ASSET_UNCAPITALIZED";
           		posttable = "am_UncapitalizedTransfer";
           		String page = "ASSET TRANSFER RAISE ENTRY";
           		String newBranch = records.getCodeName("select New_branch_id from am_UncapitalizedTransfer where POSTING_BATCH = '"+groupid+"'");
           		String newDept = records.getCodeName("select New_dept_id from am_UncapitalizedTransfer where POSTING_BATCH = '"+groupid+"'");
           		String newSec = records.getCodeName("select Section_ID from am_UncapitalizedTransfer a, am_ad_section s where a.NEW_SECTION_CODE = s.Section_Code and POSTING_BATCH = '"+groupid+"'");
           		String cat_id = records.getCodeName("select category_ID from am_UncapitalizedTransfer a, am_ad_category c where a.OLD_CATEGORY_CODE = c.category_code and POSTING_BATCH = '"+groupid+"'");
           		String newAsset_id = aid_setup.getIdentity(newBranch, newDept, newSec, cat_id);
           		records.updateTransferWithBatchId(tableName, posttable, groupid);
           		System.out.println("am_UncapitalizedTransfer 2");
          		String query_tr ="update a SET a.entryPostFlag = 'Y',a.GroupIdStatus = 'Y' from am_raisentry_post a,am_UncapitalizedTransfer g where CONVERT(VARCHAR, a.id) = CONVERT(VARCHAR, g.asset_id) and POSTING_BATCH = '"+groupid+"' " ;      		
          		ad.updateAssetStatusChange(query_tr);
          		System.out.println("am_UncapitalizedTransfer 3");
           		String query_trt ="update g SET g.approval_status = 'POSTED' from am_asset_approval a, am_UncapitalizedTransfer g where CONVERT(VARCHAR, a.asset_id) = CONVERT(VARCHAR, g.asset_id) and POSTING_BATCH = '"+groupid+"' ";     		
           		ad.updateAssetStatusChange(query_trt); 
           		System.out.println("am_UncapitalizedTransfer 4");
           		posttable = "am_asset_approval";
         		 status1 = "A";
         		 status2 = "ACTIVE";     
           		String query_tapp ="update a SET a.process_status = 'A',a.asset_status = 'ACTIVE' from am_asset_approval a, am_UncapitalizedTransfer g where CONVERT(VARCHAR, a.asset_id) = CONVERT(VARCHAR, g.asset_id) and POSTING_BATCH = '"+groupid+"' ";     		
           		ad.updateAssetStatusChange(query_tapp);             		
           		System.out.println("am_UncapitalizedTransfer 5");
           		posttable = "am_UncapitalizedTransfer";
           	 boolean done = records.insertRaiseEntryTransactionforTransfer(groupid,page,userId,posttable);
           	System.out.println("am_UncapitalizedTransfer 6");
           }       		
           if(tableName.equals("am_UncapitalizedDisposal")){
//      		records.updatePostWithBatchId(tableName, columnName1, columnName2, whereFld, groupid, status1,status2);
      		tableName = "am_UncapitalizedDisposal";
      		columnName1 = "DISPOSAL_STATUS";
      		columnName2 = "STATUS";
      		columnName3 = "approval_status";
      		 status1 = "Y";
      		 status2 = "POSTED";
      		 status3 = "ACTIVE";
      		whereFld = "POSTING_BATCH";
//      		records.updatePostWithBatchId(tableName, columnName1, columnName2, columnName3,  whereFld, groupid, status1, status2, status3);
            String qa = "update am_UncapitalizedDisposal SET DISPOSAL_STATUS = 'P' where POSTING_BATCH = '"+groupid+"'";
            ad.updateAssetStatusChange(qa);	       		
      		System.out.println("am_UncapitalizedDisposal 1");
      		tableName = "am_UncapitalizedDisposal";
      		 posttable = "am_asset_approval";
      		 status1 = "A";
      		 status2 = "ACTIVE";
      		 
      		String query_r = "update a set a.process_status = 'A',Asset_Status = 'ACTIVE' from am_asset_approval a, am_UncapitalizedDisposal g where convert(varchar, a.batch_id) = convert(varchar, g.Disposal_ID) and convert(varchar, g.POSTING_BATCH) = = '"+groupid+"' ";
            ad.updateAssetStatusChange(query_r);

      		 
      		//records.updateApprovalWithBatchId(tableName, posttable, groupid, status1, status2);
       		//records.updateAssetApprovalWithBatchId(tableName, posttable, groupid, status1, status2);      		
      		System.out.println("am_UncapitalizedDisposal 2");
      		tableName = "am_UncapitalizedDisposal";
      		columnName1 = "DISPOSED";
      		columnName2 = "STATUS";
      		columnName3 = "approval_status";
      		 status1 = "Y";
      		 status2 = "POSTED";
      		 status3 = "ACTIVE";
      		whereFld = "POSTING_BATCH";
//      		records.updatePostWithBatchId(tableName, columnName1, columnName2, columnName3,  whereFld, groupid, status1, status2, status3);
	        String qb = "update a SET a.entryPostFlag = 'Y',a.GroupIdStatus = 'Y' from am_raisentry_post a, am_UncapitalizedDisposal g where CONVERT(VARCHAR, a.id) = CONVERT(VARCHAR, g.asset_id) and g.POSTING_BATCH = '"+groupid+"'";
	        ad.updateAssetStatusChange(qb);	 
      		System.out.println("am_UncapitalizedDisposal 3");
      		tableName = "AM_ASSET_UNCAPITALIZED"; 
       		posttable = "am_UncapitalizedDisposal";
     		 status1 = "DISPOSED";
     		String page = "ASSET DISPOSAL RAISE ENTRY";
//      		records.updatePostWithBatchId(tableName, posttable, groupid, status1, status2);
	        String qd = "update a SET a.Asset_Status = 'DISPOSED' from AM_ASSET a, am_UncapitalizedDisposal g where CONVERT(VARCHAR, a.Asset_id) = CONVERT(VARCHAR, g.asset_id) and g.POSTING_BATCH = '"+groupid+"'";
	        ad.updateAssetStatusChange(qd);	       		
      		System.out.println("am_UncapitalizedDisposal 4");
      		 boolean done = records.insertRaiseEntryTransactionforDisposal(groupid,page,userId,posttable);
      		System.out.println("am_UncapitalizedDisposal 5");
      		
           }  
           if(tableName.equals("am_Uncapitalized_improvement")){
          		tableName = "am_Uncapitalized_improvement";
          		posttable = "am_raisentry_post";
          		columnName1 = "entryPostFlag";
          		columnName2 = "GroupIdStatus";
          		columnName3 = "";
          		whereFld = "Id";
          		status1 = "Y";
          		status2 = "Y";
//          	   	records.updatePostWithBatchId(tableName, posttable, groupid,status1,status2);
       	        String qb = "update a SET a.entryPostFlag = 'Y',a.GroupIdStatus = 'Y' from am_raisentry_post a, am_Uncapitalized_improvement g where CONVERT(VARCHAR, a.id) = CONVERT(VARCHAR, g.asset_id) and g.POSTING_BATCH = '"+groupid+"'";
       	        ad.updateAssetStatusChange(qb);	   	   	
          	   	System.out.println("am_Uncapitalized_improvement 1");
          	   	tableName = "am_Uncapitalized_improvement";
          		columnName1 = "IMPROVED";
          		columnName2 = "STATUS";
          		columnName3 = "approval_status";
          		 status1 = "Y";
          		 status2 = "IMPROVED";
          		 status3 = "POSTED";
          		whereFld = "POSTING_BATCH";
          		records.updatePostWithBatchId(tableName, columnName1, columnName2, columnName3,  whereFld, groupid, status1, status2, status3);
          		System.out.println("am_Uncapitalized_improvement 2");
          		tableName = "am_Uncapitalized_improvement";
          		columnName1 = "IMPROVED";
          		columnName2 = "STATUS";
          		columnName3 = "approval_status";
          		 status1 = "Y";
          		 status2 = "IMPROVED";
          		 status3 = "POSTED";
          		whereFld = "POSTING_BATCH";
          		records.updatePostWithBatchId(tableName, columnName1, columnName2, columnName3,  whereFld, groupid, status1, status2, status3);
          		System.out.println("am_Uncapitalized_improvement 4");
          		tableName = "am_Uncapitalized_improvement";
          		posttable = "am_asset_approval";
         		 status1 = "A";
         		 status2 = "ACTIVE";
//         		records.updatePostWithBatchId(tableName, posttable, groupid, status1, status2);
               String qa = "update a SET a.process_status = 'A',a.asset_status = 'ACTIVE' from am_asset_approval a, am_Uncapitalized_improvement g where CONVERT(VARCHAR, a.asset_id) = CONVERT(VARCHAR, g.asset_id) and POSTING_BATCH = '"+groupid+"'";
               ad.updateAssetStatusChange(qa);	   	   	
          		tableName = "AM_ASSET_UNCAPITALIZED";
          		posttable = "am_Uncapitalized_improvement";
          		String page = "ASSET IMPROVEMENT RAISE ENTRY";
          		records.insertRaiseEntryTransactionforImprovement(groupid,page,userId,posttable);
          		//records.updateImprovementWithBatchId(tableName, posttable, groupid);
          		System.out.println("am_Uncapitalized_improvement 5");
               boolean done = records.insertRaiseEntryTransactionforImprovement(groupid,page,userId,posttable);
               }               
//Uncapitalized ends 
           
           
   		String exceptionNo = records.getCodeName("select count(*) from AM_GB_BATCH_POSTING a, AM_GB_POSTING_EXCEPTION b where a.GROUP_ID = b.GROUP_ID and a.GROUP_ID = '"+groupid+"' and a.ID = b.SERIAL_NO ");
   		if(Integer.parseInt(exceptionNo)>0){
   			records.updateAllPostingTransactionsBatchId(groupid);
   			records.updatePostingTransactionsBatchId(groupid);
   			}
//        }
       // }
   		String query_update ="update AM_POSTING_GROUPID set Status='Y' where posting_batch = '"+groupid+"' ";
   		System.out.println("query_update==: "+query_update);   		
   		ad.updateAssetStatusChange(query_update);	
        } catch (Exception e)
        {
            throw new ServletException("Exception in csv Sample Servlet", e);
        } finally
        {
//     if (out != null)
//      out.close();
        }
  	  out.println("<script type='text/javascript'>alert('Batch Successfully Executed');</script>");
  	  out.println((new StringBuilder("<script> window.location ='DocumentHelp.jsp?np=raiseEntryBranchSummary'</script>")));
      //out.println((new StringBuilder("<script> window.location ='DocumentHelp.jsp?np="+MenuPage+"&P=Y&id="+id+"&tranType="+tranType+"'</script>")));         
   
	   }else {
 	  out.println("<script type='text/javascript'>alert('Batch could not be Executed')</script>");
      out.println((new StringBuilder("<script> window.location ='DocumentHelp.jsp?np="+MenuPage+"&P=Y&id="+id+"&tranType="+tranType+"'</script>"))); 
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
   
   public Connection getConnection(String jndiName) {
       Connection con = null;
       try {
           con = new DataConnect(jndiName).getConnection();
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

