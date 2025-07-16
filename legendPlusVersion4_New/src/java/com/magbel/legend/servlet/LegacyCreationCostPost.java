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
import java.util.Random;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import magma.AssetRecordsBean;
import magma.asset.manager.AssetManager;
import magma.net.manager.RaiseEntryManager;
import magma.util.Codes;
import ng.com.magbel.token.ParallexTokenClass;
import ng.com.magbel.token.ZenithTokenClass;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.legend.vao.AccountingCreditDetails;
import com.magbel.legend.vao.AccountingDebitDetails;
import com.magbel.legend.vao.FlexAccounting;
import com.magbel.util.CurrencyNumberformat;
import com.magbel.util.DataConnect;
import com.magbel.util.DatetimeFormat;

import au.com.bytecode.opencsv.CSVWriter;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
   
public class LegacyCreationCostPost extends HttpServlet
{
	private EmailSmsServiceBus mail ;
	private AssetRecordsBean ad;
	private ApprovalRecords records;
	private DatetimeFormat dateFormat;
    java.text.SimpleDateFormat sdf;
    PreparedStatement ps = null;
    private RaiseEntryManager raiseMan;
    private AssetManager assetMan;
    private Codes message;
    private CurrencyNumberformat formata = new CurrencyNumberformat();
    FlexAccounting accountingDetails = new FlexAccounting();
    JSONObject json;	
    AccountingDebitDetails debit = new AccountingDebitDetails();
    AccountingCreditDetails credit = new AccountingCreditDetails();
	ResultSet rs = null;
	
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
	   Date date = new Date();  
	   sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
	 
		mail= new EmailSmsServiceBus();
        records = new ApprovalRecords();
        raiseMan = new RaiseEntryManager();
        assetMan = new AssetManager();
        message= new Codes();
        formata = new CurrencyNumberformat();
	   
		Properties prop = new Properties();
		File file = new File("C:\\Property\\LegendPlus.properties");
		FileInputStream input = new FileInputStream(file);
		prop.load(input);

//		String ThirdPartyLabel = prop.getProperty("ThirdPartyLabel");
//		System.out.println("ThirdPartyLabel: " + ThirdPartyLabel);
//		String singleApproval = prop.getProperty("singleApproval");
//		System.out.println("singleApproval: " + singleApproval);
//		String BatchApiUrl = prop.getProperty("BatchApiUrl");
//		System.out.println("BatchApiUrl: " + BatchApiUrl);
//		String BatchChannel = prop.getProperty("BatchChannel");
//		String BatchFolder = prop.getProperty("BatchFolder");

		

	
//	   if(batchNo.equals("")){batchNo ="123456";} 
        String branchId = request.getParameter("branch_id");  
        String groupid = request.getParameter("groupid");
        String categoryCode = request.getParameter("categoryCode");
        String assetStatus = request.getParameter("Asset_Status");
//       System.out.println("assetStatus: " + assetStatus);
        String tableName = request.getParameter("tableName");  
        String columnName = request.getParameter("columnName");  
        String MenuPage = request.getParameter("MenuPage") == null ? "" : request.getParameter("MenuPage"); 
        
        String costdebitAccType = "";
        String costdebitTranCode = "";
        String costdebitNarration = "";
        String costcreditAccount = "";
        String costcreditAcctNo = "";
        String costcreditAccType = "";
        String costcreditTranCode = "";
        String costdebitAccount = "";
        String costcreditNarration = "";
        String costamount = "";
        
        String transactionId = request.getParameter("transactionIdCost");
        System.out.println("transactionId >>>>> " + transactionId);
        
        String tranType = request.getParameter("tranType");
        String transIdentifier = request.getParameter("transIdentifier");
        System.out.println("tranType >>>>> " + tranType);
        
        if(tranType.equals("ASSET TRANSFER")  && transIdentifier.equals("COST ENTRIES")) {
        	 costdebitAccType = request.getParameter("costDrAcctType");
              costdebitTranCode = request.getParameter("costDrTranCode");
              costdebitNarration = request.getParameter("costDrNarration") == null ? "" : request.getParameter("costDrNarration");
              costcreditAccount = request.getParameter("costDrAcct");
              costcreditAcctNo = "";
              costcreditAccType = request.getParameter("costCrAcctType");
              costcreditTranCode = request.getParameter("costCrTranCode");
              costdebitAccount = request.getParameter("costCrAcct");
              costcreditNarration = request.getParameter("costCrNarration")== null ? "" : request.getParameter("costCrNarration");
              costamount = request.getParameter("cost")== null ? "0" : request.getParameter("cost");
             costamount = costamount.replaceAll(",","");
        	
        }

        
        if(tranType.equals("ASSET TRANSFER")  && transIdentifier.equals("ACCUM ENTRIES")) {
          	 costdebitAccType = request.getParameter("accumDrAcctType");
             costdebitTranCode = request.getParameter("accumDrTranCode");
             costdebitNarration = request.getParameter("accumDrNarration") == null ? "" : request.getParameter("accumDrNarration");
             costcreditAccount = request.getParameter("accumDrAcct");
             costcreditAcctNo = "";
             costcreditAccType = request.getParameter("accumCrAcctType");
             costcreditTranCode = request.getParameter("accumCrTranCode");
             costdebitAccount = request.getParameter("accumCrAcct");
             costcreditNarration = request.getParameter("accumCrNarration")== null ? "" : request.getParameter("accumCrNarration");
             costamount = request.getParameter("accumDep")== null ? "0" : request.getParameter("accumDep");
            costamount = costamount.replaceAll(",","");
        	
        }
        
        if(tranType.equals("ASSET CLOSE") && transIdentifier.equals("COST ENTRIES")) {            
       	  costdebitAccType = request.getParameter("vendor_dr_accttype");
          costdebitTranCode = request.getParameter("vendor_dr_trancode");
          costdebitNarration = request.getParameter("vendor_dr_narration") == null ? "" : request.getParameter("vendor_dr_narration");
          costcreditAccount = request.getParameter("vendor_cr_account");
          costcreditAcctNo = "";
          costcreditAccType = request.getParameter("vendor_cr_accttype");
          costcreditTranCode = request.getParameter("vendor_cr_trancode");
          costdebitAccount = request.getParameter("vendor_dr_account");
          costcreditNarration = request.getParameter("vendor_cr_narration")== null ? "" : request.getParameter("vendor_dr_narration");
          costamount = request.getParameter("vendor_amount")== null ? "0" : request.getParameter("vendor_amount");
         costamount = costamount.replaceAll(",","");        	
        }
        if(tranType.equals("ASSET CLOSE") && transIdentifier.equals("ACCUM ENTRIES")) {
       	 costdebitAccType = request.getParameter("accumDrAcctType");
         costdebitTranCode = request.getParameter("accumDrTranCode");
         costdebitNarration = request.getParameter("accumDrNarration") == null ? "" : request.getParameter("accumDrNarration");
         costcreditAccount = request.getParameter("accumDrAcct");
         costcreditAcctNo = "";
         costcreditAccType = request.getParameter("accumCrAcctType");
         costcreditTranCode = request.getParameter("accumCrTranCode");
         costdebitAccount = request.getParameter("accumCrAcct");
         costcreditNarration = request.getParameter("accumCrNarration")== null ? "" : request.getParameter("accumCrNarration");
         costamount = request.getParameter("accumDep")== null ? "0" : request.getParameter("accumDep");
        costamount = costamount.replaceAll(",","");
        }
        
        if(tranType.equals("") || tranType.equals(null)) {
        	  costdebitAccType = request.getParameter("vendor_dr_accttype");
              costdebitTranCode = request.getParameter("vendor_dr_trancode");
              costdebitNarration = request.getParameter("vendor_dr_narration") == null ? "" : request.getParameter("vendor_dr_narration");
              costcreditAccount = request.getParameter("vendor_cr_account");
              costcreditAcctNo = "";
              costcreditAccType = request.getParameter("vendor_cr_accttype");
              costcreditTranCode = request.getParameter("vendor_cr_trancode");
              costdebitAccount = request.getParameter("vendor_dr_account");
              costcreditNarration = request.getParameter("vendor_cr_narration")== null ? "" : request.getParameter("vendor_dr_narration");
              costamount = request.getParameter("vendor_amount")== null ? "0" : request.getParameter("vendor_amount");
             costamount = costamount.replaceAll(",","");
        }
        
        
       
        String tranId = request.getParameter("tranId");
        System.out.println("tranId >>>>> " + tranId);
        
        String page1 = request.getParameter("page1");
        System.out.println("page1 >>>>> " + page1);
 
        String id = request.getParameter("asset_id");
        System.out.println("id >>>>> " + id);
        
        String supervisor = request.getParameter("supervisor");
        String ccy = records.getCodeName("select iso_code from AM_GB_CURRENCY_CODE where local_currency = 'Y' ");
        String lgcyUserId = records.getCodeName("select Legacy_Sys_id from am_gb_User where USER_ID = "+userId+" "); 
        String raiseEntryNarration = costdebitNarration;
        
  	   String valueDate = sdf.format(date);
  	   String iso;
  	  String msgText1 ="";
       String to ="";
       String mail_code="1";
       String subject = "Raise Entry";
       String transaction_type="Raise_Entry";
       String type="Asset Improvement";
       String integrifyid = "";
       String costdebitAcctName = "";
       String batchId = "RE"+Integer.toString((int)(Math.random()*8000)+1000);

       String branchMail = request.getParameter("branchMail");
       
       String systemIp =request.getRemoteAddr();
       String macAddress= "";

       int assetCode = request.getParameter("assetCode") == null?0:Integer.parseInt(request.getParameter("assetCode"));
       
     //   System.out.println("====costdebitAccount: "+costdebitAccount);
        String costdebitAcctNo =  costdebitAccount == null ? "" : costdebitAccount;  
      //  System.out.println("====costdebitAcctNo: "+costdebitAcctNo);
        String costcreditAcctName = records.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+costcreditAcctNo+"' AND category_code = '"+categoryCode+"'  and substring(category_acronym,1,1) not in ('F','U')");
//      System.out.println("====costcreditAcctName: "+costcreditAcctName);
      if(costdebitAcctName.equals("")){costdebitAcctName = "Asset Acqusition Suspense Account";}   
      if(costcreditAcctName.equals("")){costcreditAcctName = "Asset Acquisition Suspense Account";}
//      System.out.println("====costdebitNarration: "+costdebitNarration+"   costcreditAccount: "+costcreditAccount+"  costamount: "+costamount+"   finacleTransId: "+finacleTransId+"  SbuCode: "+SbuCode+"   tranId: "+tranId);
     // testfield = "125";
      String test1 = "select category_name from am_ad_category where Asset_Ledger = '"+costdebitAcctNo+"'  and substring(category_acronym,1,1) not in ('F','U')";
      String test2 = "select category_name from am_ad_category where Asset_Ledger = '"+costcreditAcctNo+"' AND category_code = '"+categoryCode+"'  and substring(category_acronym,1,1) not in ('F','U')";
      String test3 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+costdebitAcctNo+"' ";

  	String branch = branchId;
// 	   if(!BatchApiUrl.equals("")){
 	   try{ 
 		  ad = new AssetRecordsBean();
 	   String dateStatus = ZenithTokenClass.postingDateValidation(branchId);
	   JSONObject jsonDate = new JSONObject(dateStatus);
	   String postingDate = jsonDate.getJSONObject("data").getString("postingDate");
	   System.out.println("postingDate >>>> " + postingDate);

       
		//debit
		System.out.println("branchId >>>> " + branchId);
		debit.setAcBranch(branch);
		debit.setAccCcy(ccy);
		debit.setAcNo(costdebitAcctNo);
		debit.setAmtTag("TXN_AMT");
		debit.setAvailableBalReq("Y");
		debit.setChequeMandatory("N");
		debit.setTrnCode("MSC");
		debit.setTrnRefNo("");
		debit.setTxNarrative(costdebitNarration);
		debit.setFcyAmt(0);
		debit.setValueDt(valueDate);
		debit.setDrCrInd("D");
		debit.setExchRate(0);
		debit.setRelatedCustomer("");
		debit.setInstrumentCode("");
		debit.setLcyAmt(costamount);
		
		//credit
		credit.setAcBranch(branch);
		credit.setAccCcy(ccy);
		credit.setAcNo(costcreditAccount);
		credit.setAmtTag("TXN_AMT");
		credit.setAvailableBalReq("Y");
		credit.setChequeMandatory("N");
		credit.setDrCrInd("C");
		credit.setExchRate(0);
		credit.setFcyAmt(0);
		credit.setInstrumentCode("");
		credit.setLcyAmt(costamount);
		credit.setRelatedCustomer("");
		credit.setTrnCode("MSC");
		credit.setTrnRefNo("");
		credit.setTxNarrative(costcreditNarration);
		credit.setValueDt(valueDate);
		
		JSONArray arr = new JSONArray();
			
			//String debitDetails =new Gson().toJson(debit);
			//String creditDetails =new Gson().toJson(credit);
			
			JSONObject debitObject = new JSONObject(debit);
			JSONObject creditObject = new JSONObject(credit);

			arr.put(debitObject);
			arr.put(creditObject);
		
			System.out.println("arr Response >>>> " + arr.toString());
			
			Random random = new Random();
			int number = 100000 + random.nextInt(900000);
			String bankingRefNumber = "LGD" + String.valueOf(number);
			System.out.println("bankingRefNumber: " + bankingRefNumber);
			
			
			accountingDetails.setBranch(branchId);
			//accountingDetails.setCoreBankingRef("LGD000235");
			accountingDetails.setCoreBankingRef(bankingRefNumber);
			accountingDetails.setUserId(lgcyUserId);
			accountingDetails.setSource("LEGEND");
			//accountingDetails.setAccEntryDetails(arr);

	//	String jsonDetails =new Gson().toJson(accountingDetails);
		iso = "";
		json = new JSONObject(accountingDetails);
		json.put("accEntryDetails", arr);
		//String paramBody = json.toString();
		//System.out.println("paramBody >>>> " + json.toString());
		
		String apiResponse = ZenithTokenClass.realTimeTransactionValidation(json.toString());
		System.out.println("API Response >>>> " + apiResponse);
		
		   JSONObject responseJSON = new JSONObject(apiResponse);
		  // System.out.println("responseJSON >>>> " + responseJSON);
		   
		   String error_Code="";
		   String error_Message="";
		   String warning_Code="";
		   String warning_Message="";
		   String accountId = responseJSON.getJSONObject("data").getString("accountingId");
		   //String accountId ="null";
		  // System.out.println("accountId >>>> " + accountId);
		   String coreBankingRef = responseJSON.getJSONObject("data").getString("coreBankingRef");
		   String trnRefNo = responseJSON.getJSONObject("data").getString("trnRefNo");
		   String datastatus = responseJSON.getJSONObject("data").getString("status");
		 //  System.out.println("datastatus >>>> " + datastatus);
		   String msgId = responseJSON.getJSONObject("data").getString("msgId");
		   
		   String uniqueRefNo = responseJSON.getJSONObject("data").getString("uniqueRefNo");
		 //  System.out.println("uniqueRefNo >>>> " + uniqueRefNo);
		   JSONArray jArray = responseJSON.getJSONObject("data").getJSONArray("errors");
		  // System.out.println("jArray >>>> " + jArray);
		   if(jArray.length() > 0) {
		   for(int i=0; i<jArray.length(); i++) {
						  JSONObject json3 = jArray.getJSONObject(i);
						   error_Code = json3.getString("code");
						   error_Message = json3.getString("message");
		   }
		   }
		  // System.out.println("We are here ");
		   
		   JSONArray jArray2 = responseJSON.getJSONObject("data").getJSONArray("warnings");
		  // System.out.println("jArray2 >>>> " + jArray2);
		   if(jArray2.length() > 0) {
		   for(int i=0; i<jArray2.length(); i++) {
				  JSONObject json4 = jArray2.getJSONObject(i);
				  warning_Code = json4.getString("code");
				  warning_Message = json4.getString("message");
		   }   
		   }
		   System.out.println("accountId>>> " + accountId + " coreBankingRef>>> " + coreBankingRef + " trnRefNo>>> " + trnRefNo +
				   " status>>> " + datastatus + " msgId>>> " + msgId + " uniqueRefNo>>> " + uniqueRefNo +
				  " error_Code>>> " + error_Code + " error_Message>>> " + error_Message +
				  " warning_Code>>> " + warning_Code + " warning_Message>>> " + warning_Message);
		
		String legacyTransId=bankingRefNumber;
		System.out.println("legacyTransId >>>>> " + legacyTransId);
	
		
 //   	if(datastatus.equals("SUCCESS")){iso = "000";}
    	if(datastatus.equals("FAILURE")){iso = "001";}
    	
    	System.out.println("We are here 2 ");
   	 String tranIdReverse = "R"+tranId;
 //  	System.out.println("ad value >>>>  " + ad.isoChecking( id, page1, transactionId,tranId));
   	//if(ad.isoChecking(  id, page1, transactionId,tranId))
               if(ad.isoChecking( id, page1, transactionId,tranId))
   	{
            		//System.out.println("We are here 3 ");
//   		iso=bus.transferFund(costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),CoreBankingRef,SbuCode);
   		//iso=bus.interaccounttransferFund(costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),finacleTransId,SbuCode,SbuCode);
   		ad.updateRaiseEntryTransaction(  id, page1, transactionId, iso, systemIp,tranId,legacyTransId,"","");
                 //  ad.updateRaiseEntryTransaction(  id, page1, transactionId, iso, systemIp);
                   ad.updateRaiseEntryTransactionArchive(id, page1, transactionId, iso,systemIp,macAddress,tranId);
                  // ad.updateRaiseEntryTransactionArchive(id, page1, transactionId, iso,systemIp,macAddress);
           }
   	else
   	{
   		//System.out.println("We are here 4 ");
          raiseMan.raiseEntry(costdebitAccount,costcreditAccount,costdebitAccType,costcreditAccType,costdebitTranCode
   		,costcreditTranCode,costdebitNarration,
   		costcreditNarration,Double.parseDouble(costamount), userId,batchId,postingDate,supervisor,id,type);

          

//         iso=bus.transferFund(costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),finacleTransId,sbu_code);
         //iso=bus.interaccounttransferFund(costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),finacleTransId,SbuCode,SbuCode);
        // ad.insertRaiseEntryTransaction(userId,costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),iso,id,page1,transactionId,systemIp);
           ad.insertRaiseEntryTransaction(userId,raiseEntryNarration,costcreditAccount,costdebitAccount,Double.parseDouble(costamount),iso,id,page1,transactionId,systemIp,tranId,assetCode,legacyTransId,costcreditAcctName,costdebitAcctName,userId,integrifyid);
           
         // ad.insertRaiseEntryTransactionArchive(userId,costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),iso,id,page1,transactionId,systemIp,macAddress);
           ad.insertRaiseEntryTransactionArchive(userId,raiseEntryNarration,costcreditAccount,costdebitAccount,Double.parseDouble(costamount),iso,id,page1,transactionId,systemIp,macAddress,tranId,assetCode);

           }
               int recCount = 1;
               if(tranType.equals("ASSET TRANSFER")) {recCount = 2;}
               if(tranType.equals("ASSET CLOSE")) {recCount = 2;}
               int recNo =Integer.parseInt(records.getCodeName("select count(*) from am_Raisentry_Transaction where asset_id = '"+id+"' and page1 = '"+page1+"' and iso = '000' "));
               if(recNo ==recCount && datastatus.equals("SUCCESS")){iso = "000";}
               if(iso.equalsIgnoreCase("000") && page1.equals("ASSET CREATION RAISE ENTRY"))
               {
               String qp = "update am_raisentry_post set entryPostFlag = 'Y',GroupIdStatus = 'Y' where asset_id='"+id+"' and Trans_id = '"+tranId+"' and Page = '"+page1+"' ";
               ad.updateAssetStatusChange(qp);	 
               String qa = "update am_asset set Asset_Status = 'ACTIVE' where asset_id='"+id+"' ";
               ad.updateAssetStatusChange(qa);	           
               }
               if(iso.equalsIgnoreCase("000") && page1.equals("ASSET TRANSFER RAISE ENTRY"))
               {
               String qp = "update am_raisentry_post set entryPostFlag = 'Y',GroupIdStatus = 'Y' where asset_id='"+id+"' and Trans_id = '"+tranId+"' and Page = '"+page1+"' ";
               ad.updateAssetStatusChange(qp);	 
               String revalue_query = "update am_assettransfer set approval_Status='ACTIVE' where transfer_id = '" + tranId + "'";
               ad.updateAssetStatusChange(revalue_query);
                              
        	   String updateqry = "UPDATE a SET a.Asset_id  = b.new_asset_id,a.OLD_ASSET_ID = b.Asset_id, a.Branch_ID = b.New_branch_id,a.Dept_ID = b.New_dept_id,"
           	   		+ "a.Section_id = b.New_Section,a.BRANCH_CODE = b.NEW_BRANCH_CODE,a.DEPT_CODE = b.NEW_DEPT_CODE,a.SECTION_CODE = b.NEW_SECTION_CODE "
           	   		+ "from  AM_ASSET a, am_assetTransfer b where a.Asset_id = b.Asset_id and a.Asset_id = '"+ id + "'";
//        	   System.out.println("@@@ the updateqry is  " + updateqry);
            ad.updateAssetStatusChange(updateqry); 
 
            ad.updateAssetStatusChange("update am_assetTransfer set REVERSED = 'Y' where TRANSFER_ID = "+tranId+" ");  
               
               }   
               if(iso.equalsIgnoreCase("000") && page1.equals("ASSET CLOSE RAISE ENTRY"))
               {
               String qp = "update am_raisentry_post set entryPostFlag = 'Y',GroupIdStatus = 'Y' where asset_id='"+id+"' and Trans_id = '"+tranId+"' and Page = '"+page1+"' ";
               ad.updateAssetStatusChange(qp);	 
               String qa = "update am_asset set Asset_Status = 'CLOSED' where asset_id='"+id+"' ";
               ad.updateAssetStatusChange(qa);	           
               }               
               if(iso.equalsIgnoreCase("000"))
               {
                   String reversalId = tranId.substring(0, 1);
//                    System.out.println(">>>>>>>>>>>tranId====:  "+tranId+"   tranId.length: "+tranId.length());
//                    System.out.println(">>>>>>>>>>>tranId.substring(1, 1):  "+tranId.substring(0, 1)+"   New TranId: "+tranId.substring(1, tranId.length()));
           	     if(reversalId.equals("R")){tranId = tranId.substring(1, tranId.length());}
            	     if(!reversalId.equals("R")){tranId = "R"+tranId;}
                     String q = "update am_Raisentry_Transaction set iso = '-1' where asset_id='"+id+"' and Trans_id = '"+tranId+"' and transactionId = "+transactionId+"";
                     ad.updateAssetStatusChange(q);	            	 
             	  
  //           	  ad.updateAssetStatusChange("update am_Raisentry_Transaction set iso = '' where trans_id='"+tranIdReverse+"' and transactionId = "+transactionId+""); 
  //                   System.out.println("#####################################################################  ");
          			msgText1 = message.MailMessage(mail_code, transaction_type)+", \n";
         			msgText1 += "For asset id  "+id+"\n";
         	        msgText1 += "For  "+costdebitNarration+"\n";
         			msgText1 += "Debit Account  "+costdebitAccount+"\n";
         			msgText1 += "Credit Account  "+costcreditAccount+"\n";
         			msgText1 += "Amount  "+formata.formatAmount(costamount)+"\n";
//         			msgText1 += "Login here http://172.27.13.113:8080/legendPlus.net" ;
           			//ret = bulkMail.sendMail(signinname,transaction_type,msgText1,directory,to);
           			//System.out.println("Output of the mail is >>>>"+ret);
         			to = branchMail;
         			mail.sendMail(to,subject,msgText1);


                }
		    String errorstatus =  records.getCodeName("select description from am_error_description where error_code='"+iso+"' ");
		    out.print("<script>alert('"+errorstatus+"');</script>");
         // out.print("<script>window.close('raiseAssetDetailAdvancePayment.jsp');</script>");
		 //   out.print("<script>window.close();</script>");
        
		}catch(Exception e) {
			e.getMessage();
		}
   }


}

