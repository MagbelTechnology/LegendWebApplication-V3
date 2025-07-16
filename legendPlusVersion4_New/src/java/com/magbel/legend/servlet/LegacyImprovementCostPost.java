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
   
public class LegacyImprovementCostPost extends HttpServlet
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
       try
       {
	   //sdf = new java.text.SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
    	   sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
		mail= new EmailSmsServiceBus();
        records = new ApprovalRecords();
        raiseMan = new RaiseEntryManager();
        assetMan = new AssetManager();
        message= new Codes();
        ad = new AssetRecordsBean();
        formata = new CurrencyNumberformat();
       }

       catch(Exception et)
       {et.printStackTrace();}
       
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
        System.out.println("=========branchId: "+branchId);
        String groupid = request.getParameter("groupid");
        String assetStatus = request.getParameter("Asset_Status");
        System.out.println("assetStatus: " + assetStatus);
        String tableName = request.getParameter("tableName");  
        String columnName = request.getParameter("columnName");  
        String MenuPage = request.getParameter("MenuPage"); 
        String costDrNarration = request.getParameter("vendor_dr_narration");
        String costCrNarration = request.getParameter("vendor_cr_narration");
        String costDrAcct = request.getParameter("vendor_dr_account");
        String costCrAcct = request.getParameter("vendor_cr_account");
        String costDrTranCode = request.getParameter("vendor_dr_trancode");
        String costCrTranCode = request.getParameter("vendor_cr_trancode");
        String transactionId = request.getParameter("transactionIdVendor");
        String costDrAcctType = request.getParameter("vendor_dr_accttype");
        String costCrAcctType = request.getParameter("vendor_cr_accttype");
        String vendoramount = request.getParameter("vendorAmt") == null ? "0" : request.getParameter("vendorAmt");
        vendoramount = vendoramount.replaceAll(",", "");
        String cost = request.getParameter("vendorAmt");
        cost = cost.replaceAll(",", "");
        String tranId = request.getParameter("tranId");
        String page1 = request.getParameter("page1");
        String id = request.getParameter("asset_id");
        //String supervisor = request.getParameter("supervisor");
        String supervisor = records.getCodeName("select super_id from am_asset_approval where asset_id = '"+id+"' "); 
        String ccy = records.getCodeName("select iso_code from AM_GB_CURRENCY_CODE where local_currency = 'Y' ");
        String lgcyUserId = records.getCodeName("select Legacy_Sys_id from am_gb_User where USER_ID = "+userId+" "); 

        
  	   String iso;
  	  String msgText1 ="";
       String to ="";
       String mail_code="1";
       String subject = "Raise Entry";
       String transaction_type="Raise_Entry";
       String type="Asset Improvement";
       String costdebitAcctName = "";
       String costcreditAcctName = "";
       String integrifyid = "";
       String batchId = "RE"+Integer.toString((int)(Math.random()*8000)+1000);
       
        String systemIp =request.getRemoteAddr();
        int assetCode = request.getParameter("assetCode") == null?0:Integer.parseInt(request.getParameter("assetCode"));
        String [] idSplit = id.split("/");
	      String test10 = "select category_name from am_ad_category where Asset_Ledger = '"+costDrAcct+"' and category_acronym = '"+idSplit[2]+"'";
	      String test11 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+costDrAcct+"' and category_acronym = '"+idSplit[2]+"'";
	      String test12 = "select category_name from am_ad_category where DEP_LEDGER = '"+costDrAcct+"' and category_acronym = '"+idSplit[2]+"'";
	      String test13 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+costDrAcct+"' ";
	      String test14 = "select asset_acq_ac from am_gb_company where asset_acq_ac = '"+costDrAcct+"' ";
	      costdebitAcctName = records.getCodeName(test10);
	      if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = records.getCodeName(test11);}
	      if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = records.getCodeName(test12);}
	      if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = records.getCodeName(test13);}
//	      if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = records.getCodeName(test14);}
	      if(costdebitAcctName.equalsIgnoreCase("") && !records.getCodeName(test14).equalsIgnoreCase("")){costdebitAcctName = "Asset Acqusition Suspense Account";}
//	      System.out.println("=========test10: "+test10);
//	      System.out.println("=========test11: "+test11);
//	      System.out.println("=========test12: "+test12);
//	      System.out.println("=========test13: "+test13);
//	      System.out.println("=========costdebitAcctName: "+costdebitAcctName);
	      String test20 = "select category_name from am_ad_category where Asset_Ledger = '"+costCrAcct+"' and category_acronym = '"+idSplit[2]+"'";
	      String test21 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+costCrAcct+"' and category_acronym = '"+idSplit[2]+"'";
	      String test22 = "select category_name from am_ad_category where DEP_LEDGER = '"+costCrAcct+"' and category_acronym = '"+idSplit[2]+"'";
	      String test23 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+costCrAcct+"' ";
	      String test24 = "select asset_acq_ac from am_gb_company where asset_acq_ac = '"+costCrAcct+"' ";
	        costcreditAcctName = records.getCodeName(test20);
	        if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = records.getCodeName(test21);}
	        if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = records.getCodeName(test22);}
	        if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = records.getCodeName(test23);} 	
	        if(records.getCodeName(test24)!="" && costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = "Asset Acquisition Suspense Account";}
	      
    	double CalcCost = Double.parseDouble(request.getParameter("CalcCost"));
    	double CalcNBV = Double.parseDouble(request.getParameter("CalcNBV"));
    	double Calcvatable = Double.parseDouble(request.getParameter("Calcvatable"));
    	double CalcVatAmt = Double.parseDouble(request.getParameter("CalcVatAmt"));
    	double CalcWhtAmt = Double.parseDouble(request.getParameter("CalcWhtAmt"));
    	double costValue = Double.parseDouble(request.getParameter("costValue"));
    	double vatableCost = Double.parseDouble(request.getParameter("vatableCost"));
    	String nbvresidual = request.getParameter("nbvresidual");
    	String usefullife = request.getParameter("usefullife");
    	
    	
// 	   if(!BatchApiUrl.equals("")){
 	   try{ 
 	   String dateStatus = ZenithTokenClass.postingDateValidation(branchId);
	   JSONObject jsonDate = new JSONObject(dateStatus);
	   String postingDate = jsonDate.getJSONObject("data").getString("postingDate");
	   System.out.println("postingDate >>>> " + postingDate);
	   String yyyy = postingDate.substring(6, 10);
	   String mm = postingDate.substring(3, 5);
	   String dd = postingDate.substring(0, 2);
	   String valueDate =yyyy+"-"+ mm+"-"+dd ;
	   System.out.println("valueDate >>>> " + valueDate);
// 	  }
        JSONObject json;
		JSONObject json2 = new JSONObject();
		FlexAccounting accountingDetails = new FlexAccounting();
		System.out.println("===========1 >>>> ");
		AccountingDebitDetails debit = new AccountingDebitDetails();
		System.out.println("===========2 >>>> ");
		debit.setAcBranch(branchId);
		debit.setAccCcy(ccy);
		debit.setAcNo(costDrAcct);
		System.out.println("costDrAcct >>>> " + costDrAcct);
		debit.setAmtTag("TXN_AMT");
		debit.setAvailableBalReq("Y");
		debit.setChequeMandatory("N");
		debit.setTrnCode("MSC");
		debit.setTrnRefNo("");
		debit.setTxNarrative(costDrNarration);
		debit.setFcyAmt(0);
		debit.setValueDt(valueDate);
		System.out.println("valueDate =====>>>> " + valueDate);
		debit.setDrCrInd("D");
		debit.setExchRate(0);
		debit.setRelatedCustomer("");
		debit.setInstrumentCode("");
		debit.setLcyAmt(vendoramount);
		AccountingCreditDetails credit = new AccountingCreditDetails();
		credit.setAcBranch(branchId);
		credit.setAccCcy(ccy);
		credit.setAcNo(costCrAcct);
		System.out.println("costCrAcct >>>> " + costCrAcct);
		credit.setAmtTag("TXN_AMT");
		credit.setAvailableBalReq("Y");
		credit.setChequeMandatory("N");
		credit.setDrCrInd("C");
		credit.setExchRate(0);
		credit.setFcyAmt(0);
		credit.setInstrumentCode("");
		credit.setLcyAmt(vendoramount);
		System.out.println("vendoramount >>>> " + vendoramount);
		credit.setRelatedCustomer("");
		credit.setTrnCode("MSC");
		credit.setTrnRefNo("");
		credit.setTxNarrative(costCrNarration);
		credit.setValueDt(valueDate);
		System.out.println("valueDate >>>> " + valueDate);
		JSONArray arr = new JSONArray();
		System.out.println("===========3 >>>> ");
			
			String debitDetails =new Gson().toJson(debit);
			String creditDetails =new Gson().toJson(credit);
			System.out.println("===========4 >>>> ");
			JSONObject debitObject = new JSONObject(debit);
			JSONObject creditObject = new JSONObject(credit);
			System.out.println("===========5 >>>> ");
			arr.put(debitObject);
			arr.put(creditObject);
			
			//String JArray = arr.toString();

			Random random = new Random();
			int number = 100000 + random.nextInt(900000);
			String bankingRefNumber = "LGD" + String.valueOf(number);
			System.out.println("bankingRefNumber: " + bankingRefNumber);

			
			System.out.println("===========6 >>>> ");
			//JSONObject JArray = new JSONObject(arr);
			//arr.toString().replace("\"", "");
			System.out.println("============7 >>>> ");
			accountingDetails.setBranch(branchId);
			System.out.println("branchId >>>> " + branchId);
			accountingDetails.setCoreBankingRef(bankingRefNumber);
			System.out.println("===========8 >>>> ");
			accountingDetails.setUserId(lgcyUserId);
			System.out.println("lgcyUserId >>>> " + lgcyUserId);
			accountingDetails.setSource("LEGEND");
		//accountingDetails.setAccEntryDetails(arr.toString().replace("\"", ""));
		//System.out.println("lgcyUserId >>>> " + lgcyUserId);
		String jsonDetails =new Gson().toJson(accountingDetails);
		iso = "";
		json = new JSONObject(accountingDetails);
		json.put("accEntryDetails", arr);
		String paramBody = json.toString();
		//System.out.println("paramBody >>>> " + paramBody);
		String apiResponse = ZenithTokenClass.realTimeTransactionValidation(json.toString());
		System.out.println("API Response >>>> " + apiResponse);
		   JSONObject responseJSON = new JSONObject(apiResponse);
		   String error_Code="";
		   String error_Message="";
		   String warning_Code=""; 
		   String warning_Message="";
		   String accountId = responseJSON.getJSONObject("data").getString("accountingId");
		   String coreBankingRef = responseJSON.getJSONObject("data").getString("coreBankingRef");
		   String trnRefNo = responseJSON.getJSONObject("data").getString("trnRefNo");
		   String datastatus = responseJSON.getJSONObject("data").getString("status");
		   String msgId = responseJSON.getJSONObject("data").getString("msgId");
		   String uniqueRefNo = responseJSON.getJSONObject("data").getString("uniqueRefNo");
		   JSONArray jArray = responseJSON.getJSONObject("data").getJSONArray("errors");
		   if(jArray.length() > 0) {
		   for(int i=0; i<jArray.length(); i++) {
						  JSONObject json3 = jArray.getJSONObject(i);
						   error_Code = json3.getString("code");
						   error_Message = json3.getString("message");
		   }
		   }
		   
		   JSONArray jArray2 = responseJSON.getJSONObject("data").getJSONArray("warnings");
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
		System.out.println("legacyTransId >>>> " + legacyTransId);
		
    	if(datastatus.equals("SUCCESS")){iso = "000";}
    	if(datastatus.equals("FAILURE")){iso = "001";}
    	 System.out.println("Id..."+id+"   =======page1: "+page1+"    =====transactionId: "+transactionId+"    =====tranId: "+tranId+"    =====ISO: "+iso);
    	if(ad.isoChecking( id,page1,transactionId,tranId))
    	{
//    		  System.out.println("#####################################################################  1");
//    		iso=bus.transferFund(costDrNarration,costDrAcct,costCrAcct,Double.parseDouble(cost),finacleTransId,SbuCode);
    		//iso=bus.interaccounttransferFund(costDrNarration,costDrAcct,costCrAcct,Double.parseDouble(cost),finacleTransId,SbuCode,oldSbuCode);
    		  System.out.println("#####################################################################  2");
    		ad.updateRaiseEntryTransaction(id, page1, transactionId, iso,systemIp,tranId,legacyTransId,"","");
    		  System.out.println("#####################################################################  3");
    	}
    	else
    	{  
    		  System.out.println("#####################################################################  4");
   		   System.out.println("costDrAcct>>> " + costDrAcct + " costCrAcct>>> " + costCrAcct + " costDrAcctType>>> " + costDrAcctType +
				   " costCrAcctType>>> " + costCrAcctType + " costDrTranCode>>> " + costDrTranCode + " costCrTranCode>>> " + costCrTranCode +
				  " costDrNarration>>> " + costDrNarration + " cost>>> " + cost +
				  " userId>>> " + userId + " batchId>>> " + batchId+" valueDate>>> " + valueDate + " supervisor>>> " + supervisor+
				  " id>>> " + id + " type>>> " + type);    		  
           raiseMan.raiseEntry(costDrAcct,costCrAcct,costDrAcctType,costCrAcctType,costDrTranCode
    		,costCrTranCode,costDrNarration,
    		costCrNarration,Double.parseDouble(cost), userId,batchId,valueDate,supervisor,id,type);
          
    		
           System.out.println("#####################################################################  5");
           String raiseEntryNarration = costDrNarration;
           //iso=bus.interaccounttransferFund(costDrNarration,costDrAcct,costCrAcct,Double.parseDouble(cost),finacleTransId,SbuCode,oldSbuCode);
           System.out.println("#####################################################################  6");
          //ad.insertRaiseEntryTransactionTranId(userId,costDrNarration,costDrAcct,costCrAcct,Double.parseDouble(cost),iso,id,page1,transactionId,tranId);
           ad.insertRaiseEntryTransaction (userId,raiseEntryNarration,costCrAcct,costDrAcct,Double.parseDouble(cost),iso,id,page1,transactionId,systemIp,tranId,assetCode,legacyTransId,costcreditAcctName,costdebitAcctName,userId,integrifyid);
          System.out.println("#####################################################################  7");
    	}
        if(iso.equalsIgnoreCase("000"))
        {
        	System.out.println("iso >>>> " + iso);
   	   	 String reversalId = tranId.substring(0, 1);
   	  System.out.println("reversalId >>>> " + reversalId);
   	     if(reversalId.equals("R")){tranId = tranId.substring(1, tranId.length());}
   	  System.out.println("tranId ====>>>> " + tranId);
      	     if(!reversalId.equals("R")){tranId = "R"+tranId;}
      	   System.out.println("tranId >>>> " + tranId);
               String q = "update am_Raisentry_Transaction set iso = '-001' where asset_id='"+id+"' and Trans_id = '"+tranId+"' and transactionId = "+transactionId+"";
               System.out.println("q >>>> " + q);
               ad.updateAssetStatusChange(q);	               	  
      	  String  improveStatus = records.getCodeName("SELECT IMPROVED FROM am_asset_improvement WHERE revalue_Id = '"+tranId+"'");
      	System.out.println("improveStatus >>>> " + improveStatus);
      	  String test = "select count(*) from am_Raisentry_Transaction where Trans_id='"+tranId+"' and page1 = 'ASSET IMPROVEMENT RAISE ENTRY' and iso = '000' ";
      	System.out.println("test >>>> " + test);
      	  String isoValue=records.getCodeName(test);
      	  if(isoValue.equalsIgnoreCase("3") && improveStatus.equalsIgnoreCase("Y"))
      	  {
      	  assetMan.processImprovement(id,CalcCost,CalcNBV,Calcvatable,CalcVatAmt,CalcWhtAmt,Integer.parseInt(usefullife),costValue,vatableCost,nbvresidual,Integer.parseInt(tranId));
      	  }
              System.out.println("#####################################################################  ");
   			msgText1 = message.MailMessage(mail_code, transaction_type)+", \n";
  			msgText1 += "For asset id  "+id+"\n";
  	        msgText1 += "For  "+costDrNarration+"\n";
  			msgText1 += "Debit Account  "+costDrAcct+"\n";
  			msgText1 += "Credit Account  "+costCrAcct+"\n";
  			msgText1 += "Amount  "+formata.formatAmount(cost)+"\n";
 // 			msgText1 += "Login here http://172.27.13.113:8080/legend2.net" ;
    			//ret = bulkMail.sendMail(signinname,transaction_type,msgText1,directory,to);
    			//System.out.println("Output of the mail is >>>>"+ret);
  			mail.sendMail(to,subject,msgText1);
  			
  			 
         }
		    String errorstatus =  records.getCodeName("select description from am_error_description where error_code='"+iso+"' ");
		    out.print("<script>alert('"+errorstatus+"');</script>");
          out.print("<script>window.close('raiseAssetDetailAdvancePayment.jsp');</script>");
        
        
		}catch(Exception e) {
			e.getMessage();
		}
   }


}

