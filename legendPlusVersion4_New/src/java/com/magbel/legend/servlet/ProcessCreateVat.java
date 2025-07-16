package com.magbel.legend.servlet;



import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.vao.Approval;

import java.io.*;

import magma.AssetRecordsBean;
import magma.net.manager.DepreciationProcessingManager;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import com.magbel.legend.bus.FinancialExchangeServiceBus;

import magma.net.manager.RaiseEntryManager;

import com.magbel.legend.mail.EmailSmsServiceBus;

import magma.util.Codes;

import java.util.*;

import  com.magbel.util.CurrencyNumberformat ;

import java.text.SimpleDateFormat;

public class ProcessCreateVat extends HttpServlet
{

	private DepreciationProcessingManager service;
	private FinancialExchangeServiceBus bus;
	private RaiseEntryManager raiseMan;
	private EmailSmsServiceBus mail ;
	private Codes message;
	private AssetRecordsBean ad;
	private CurrencyNumberformat formata = new CurrencyNumberformat();
	private SimpleDateFormat sdf;
	private ApprovalRecords records;
    public ProcessCreateVat()
    {
    }

    public void init(ServletConfig config)
        throws ServletException
    {
        super.init(config);
        try
        {
        service = new DepreciationProcessingManager();
        raiseMan = new RaiseEntryManager();
        bus = new FinancialExchangeServiceBus();
        mail= new EmailSmsServiceBus();
        message= new Codes();
        ad = new AssetRecordsBean();
        formata = new CurrencyNumberformat();
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        records = new ApprovalRecords();
        }
        
        catch(Exception et)
        {et.printStackTrace();}
    }
    public void destroy()
    {
    }
       
  

    public void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        response.setContentType("text/html");
       
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();               
        
        String userClass = (String) session.getAttribute("UserClass");
        
        String userId =(String)session.getAttribute("CurrentUser");
        String id = request.getParameter("asset_id");
        int assetCode = request.getParameter("assetCode") == null?0:Integer.parseInt(request.getParameter("assetCode"));

//        String page1 = "ASSET CREATION RAISE ENTRY";
        String iso;
        String msgText1 ="";
        String to ="";
        String mail_code="1";
        String subject = "Raise Entry";
        String transaction_type="Raise_Entry";
        String batchId = "RE"+Integer.toString((int)(Math.random()*8000)+1000);
        String cdate = sdf.format(new Date());
		/*
        Post VAT Transaction
        */
        String page1 = request.getParameter("page1");
        String vat = request.getParameter("vat");
        String categoryCode = request.getParameter("categoryCode");
        String ThirdPartyLabel = request.getParameter("ThirdPartyLabel");
        String vatdebitAccount = request.getParameter("vat_dr_account");
//        String vatdebitAcctNo = vatdebitAccount.substring(8,16);
        String vatdebitAccType = request.getParameter("vat_dr_accttype");
        String vatdebitTranCode = request.getParameter("vat_dr_trancode");
        String vatdebitNarration = request.getParameter("vat_dr_narration");
        String vatcreditAccount = request.getParameter("vat_cr_account");
//        String vatcreditAcctNo = vatcreditAccount.substring(8,16);
        String vatcreditAccType = request.getParameter("vat_cr_accttype");
        String vatcreditTranCode = request.getParameter("vat_cr_trancode");
        String vatcreditNarration = request.getParameter("vat_cr_narration");
        String vatamount = request.getParameter("vat_amount"); 
        String supervisor = request.getParameter("supervisor");
        System.out.println("<<<<<<<<=====supervisor before Conversion>>>>>: "+supervisor);
        if(supervisor==null || supervisor.equals(null)){supervisor="0";}
        if(supervisor.equals("*")){supervisor = "0";}
        System.out.println("<<<<<<<<=====supervisor>>>>>: "+supervisor);
        String type="Asset Creation";
        String transactionId = request.getParameter("transactionIdVat");
         String systemIp =request.getRemoteAddr();
          String tranId = request.getParameter("tranId");
          String SbuCode = request.getParameter("SbuCode");
          String recType = request.getParameter("recType");
          String macAddress ="";
          String finacleTransId = records.getGeneratedTransId();
          String raiseEntryNarration = vatdebitNarration; 
        //  String sbu_code = SbuCode+ "|" +SbuCode;
          String sbu_code = SbuCode +SbuCode;
          vatdebitNarration = SbuCode+"|"+SbuCode+"|"+vatdebitNarration;
          vatdebitNarration = SbuCode+"|"+SbuCode+"|"+vatdebitNarration;
   //       String costdebitAcctName = records.getCodeName("select vendor_name from am_ad_vendor where account_number = '"+vatdebitAcctNo+"'");
         // String costcreditAcctName = records.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+vatcreditAcctNo+"'");
/*
          String vatdebitAcctNo = "";
          String vatcreditAcctNo = "";        
//          if(vatdebitAccount.substring(0,2)=="NGN"){vatdebitAcctNo = vatdebitAccount.substring(8,16);}
//          if(vatdebitAccount.substring(2,5)=="NGN"){vatdebitAcctNo = vatdebitAccount.substring(6,14);}
//          if(vatcreditAccount.substring(0,2)=="NGN"){vatcreditAcctNo = vatcreditAccount.substring(8,16);}
//          if(vatcreditAccount.substring(2,5)=="NGN"){vatcreditAcctNo = vatcreditAccount.substring(6,14);}
          System.out.println("===vatdebitAccount: "+vatdebitAccount+"   vatcreditAccount: "+vatcreditAccount+"  ThirdPartyLabel: "+ThirdPartyLabel);
          if(ThirdPartyLabel.equalsIgnoreCase("INTEGRIFY")){vatdebitAcctNo = vatdebitAccount.substring(8,16);}
          if(ThirdPartyLabel.equalsIgnoreCase("K2")){vatdebitAcctNo = vatdebitAccount.substring(6,14);}
          if(ThirdPartyLabel.equalsIgnoreCase("K2")){ vatcreditAcctNo = vatcreditAccount.substring(6,14);}
          if(ThirdPartyLabel.equalsIgnoreCase("INTEGRIFY")){ vatcreditAcctNo = vatcreditAccount.substring(8,16);}
          System.out.println("===vatdebitAcctNo: "+vatdebitAcctNo+"   vatcreditAcctNo: "+vatcreditAcctNo);
          
          String costdebitAcctName = records.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+vatdebitAcctNo+"'");
          String costcreditAcctName = records.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+vatcreditAcctNo+"'");
          String [] idSplit = id.split("/");
          String test10 = "select category_name from am_ad_category where Asset_Ledger = '"+vatdebitAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
          String test11 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+vatdebitAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
          String test12 = "select category_name from am_ad_category where DEP_LEDGER = '"+vatdebitAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
          String test13 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+vatdebitAcctNo+"' ";
          costdebitAcctName = records.getCodeName(test10);
          if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = records.getCodeName(test11);}
          if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = records.getCodeName(test12);}
          if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = records.getCodeName(test13);}
          System.out.println("===vatdebitAcctNo: "+vatdebitAcctNo+"   vatcreditAcctNo: "+vatcreditAcctNo);
          String test20 = "select category_name from am_ad_category where Asset_Ledger = '"+vatcreditAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
          String test21 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+vatcreditAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
          String test22 = "select category_name from am_ad_category where DEP_LEDGER = '"+vatcreditAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
          String test23 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+vatcreditAcctNo+"' ";
          costcreditAcctName = records.getCodeName(test20);
          if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = records.getCodeName(test21);}
          if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = records.getCodeName(test22);}
          if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = records.getCodeName(test23);}  
          System.out.println("===vatdebitAcctNo: "+vatdebitAcctNo+"   vatcreditAcctNo: "+vatcreditAcctNo);
          if(costdebitAcctName.equals("")){costdebitAcctName = "Asset Acqusition Suspense Account";}   
          if(costcreditAcctName.equals("")){costcreditAcctName = "Asset Acquisition Account";}
           costcreditAcctName = "VAT ACCOUNT";
           */
  		// String sbu_code = SbuCode+ "|" +SbuCode;
//		    String costdebitAcctNo = taxdebitAccount.substring(8,16);
//		    String costcreditAcctNo = taxcreditAccount.substring(8,16);
		    String costdebitAcctNo = vatdebitAccount;
		    String costcreditAcctNo = vatcreditAccount;  		
//		    System.out.println("<<<<<<<<<<<<<taxdebitAccount in GroupProcess >>>>>> "+taxdebitAccount);
//		    System.out.println("taxdebitAccount.substring(8,16) in GroupProcess >>>>>> " + taxdebitAccount.substring(8,16));
	        if(vatdebitAccount.substring(0,3).equalsIgnoreCase("NGN")){costdebitAcctNo = vatdebitAccount.substring(8,16);}
	        if(vatcreditAccount.substring(0,3).equalsIgnoreCase("NGN")){costcreditAcctNo = vatcreditAccount.substring(8,16);}  
	        if(vatdebitAccount.substring(2,5).equalsIgnoreCase("NGN")){costdebitAcctNo = vatdebitAccount.substring(6,14);}
	        if(vatcreditAccount.substring(2,5).equalsIgnoreCase("NGN")){costcreditAcctNo = vatcreditAccount.substring(6,14);}
//	        System.out.println("SbuCode in GroupProcess >>>>>> " + SbuCode+"   costdebitAcctNo: "+costdebitAcctNo+"   costcreditAcctNo: "+costcreditAcctNo );
//	        taxdebitNarration = SbuCode+"|"+SbuCode+"|"+taxdebitNarration;
	        String costdebitAcctName = records.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+costdebitAcctNo+"'");
	        String costcreditAcctName = records.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+costcreditAcctNo+"'");
//	        System.out.println("ID >>>>>> " + id );
//	        String [] idSplit = id.split("/");
	        String test10 = "select category_name from am_ad_category where Asset_Ledger = '"+costdebitAcctNo+"' and category_code = '"+categoryCode+"'";
	        String test11 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+costdebitAcctNo+"' and category_code = '"+categoryCode+"'";
	        String test12 = "select category_name from am_ad_category where DEP_LEDGER = '"+costdebitAcctNo+"' and category_code = '"+categoryCode+"'";
	        String test13 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+costdebitAcctNo+"' ";
	        String test14 = "select asset_acq_ac from am_gb_company where asset_acq_ac = '"+costdebitAcctNo+"' ";
	        System.out.println("test14 0>>>>>> " + test14);
	        costdebitAcctName = records.getCodeName(test10);
	        System.out.println("costdebitAcctName 1>>>>>> " + costdebitAcctName);
	        if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = records.getCodeName(test11);}
	        System.out.println("costdebitAcctName 2>>>>>> " + costdebitAcctName);
	        if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = records.getCodeName(test12);}
	        System.out.println("costdebitAcctName 3>>>>>> " + costdebitAcctName);
	        if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = records.getCodeName(test13);}
	        System.out.println("costdebitAcctName 4>>>>>> " + costdebitAcctName);
	        if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = records.getCodeName(test14);}
	        System.out.println("costdebitAcctName 5>>>>>> " + costdebitAcctName);
	        System.out.println("costdebitAcctName 1>>>>>> " + costdebitAcctName+"   vat: "+vat);
	        if(!costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = "Asset Acquisition Suspense Account";}
	        System.out.println("costdebitAcctName 6>>>>>> " + costdebitAcctName);
	          if(costdebitAcctName.equalsIgnoreCase("") && vat.equalsIgnoreCase("N")){costdebitAcctName = records.getCodeName("select SelfChargeVAT from am_gb_company where SelfChargeVAT = '"+costdebitAcctNo+"' "); 
	          costdebitAcctName = "Self Charge VAT Account";}
	          if(costdebitAcctName.equalsIgnoreCase("") && vat.equalsIgnoreCase("Y")){costdebitAcctName = records.getCodeName("select Vat_Account from am_gb_company where Vat_Account = '"+costdebitAcctNo+"' ");
	          costdebitAcctName = "VAT Account";}	
	          
	        String test20 = "select category_name from am_ad_category where Asset_Ledger = '"+costcreditAcctNo+"' and category_code = '"+categoryCode+"'";
	        String test21 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+costcreditAcctNo+"' and category_code = '"+categoryCode+"'";
	        String test22 = "select category_name from am_ad_category where DEP_LEDGER = '"+costcreditAcctNo+"' and category_code = '"+categoryCode+"'";
	        String test23 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+costcreditAcctNo+"' ";
	        String test24 = "select asset_acq_ac from am_gb_company where asset_acq_ac = '"+costcreditAcctNo+"' ";
//	        String test24 = "";
//	          if(approv.getCodeName(test14)!="" && costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = "Asset Acquisition Suspense Account";}
        costcreditAcctName = records.getCodeName(test20);
        System.out.println("costcreditAcctName 1>>>>>> " + costcreditAcctName);
        if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = records.getCodeName(test21);}
        System.out.println("costcreditAcctName 2>>>>>> " + costcreditAcctName);
        if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = records.getCodeName(test22);}
        System.out.println("costcreditAcctName 3>>>>>> " + costcreditAcctName);
        if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = records.getCodeName(test23);}
        System.out.println("costcreditAcctName 4>>>>>> " + costcreditAcctName);
        if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = records.getCodeName(test24);}
        if(!costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = "Asset Acquisition Suspense Account";}
//         if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test24);costcreditAcctName = "Asset Acquisition Suspense Account";}
//        if(approv.getCodeName(test24)!="" && costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = "Asset Acquisition Account";}
//        if(costdebitAcctName==""){costdebitAcctName = "Asset Acquisition Account";}
        System.out.println("<<<<< costdebitAcctName >>>>>> " + costdebitAcctName+"  =======  costcreditAcctName====>: "+costcreditAcctName+"   costcreditAcctNo:"+costcreditAcctNo);
        
//	        System.out.println("<<<<<<<=====costcreditAcctName: "+costcreditAcctName+"   costdebitAcctName: "+costdebitAcctName+"  VAT: "+vat);
        if(costcreditAcctName.equalsIgnoreCase("") && vat.equalsIgnoreCase("N")){costcreditAcctName = records.getCodeName("select SelfChargeVAT from am_gb_company where SelfChargeVAT = '"+costcreditAcctNo+"' "); 
        costcreditAcctName = "Self Charge VAT Account";}
        if(costcreditAcctName.equalsIgnoreCase("") && vat.equalsIgnoreCase("Y")){costcreditAcctName = records.getCodeName("select Vat_Account from am_gb_company where Vat_Account = '"+costcreditAcctNo+"' ");
        costcreditAcctName = "VAT Account";}	 
        System.out.println("<<<<< costcreditAcctName >>>>>> " + costcreditAcctName);
        try
        {  
        	 if (!userClass.equals("NULL") || userClass!=null){
        if(vatamount!=null)
        {
        	vatamount = vatamount.replaceAll(",","");
        	System.out.println("1========vatamount: "+vatamount+"   Id: "+id+"    page1: "+page1+"    transactionId: "+transactionId+"   tranId: "+tranId);
        	 if(ad.isoChecking(id, page1, transactionId,tranId))
             {
        		 System.out.println("0========vatamount: "+vatamount+"   Result======: "+ad.isoChecking(id, page1, transactionId));
        		 iso=bus.transferFund(vatdebitNarration,vatdebitAccount,vatcreditAccount,Double.parseDouble(vatamount),finacleTransId,SbuCode);
        		 //iso=bus.interaccounttransferFund(vatdebitNarration,vatdebitAccount,vatcreditAccount,Double.parseDouble(vatamount),finacleTransId,SbuCode,SbuCode);
                  ad.updateRaiseEntryTransaction(   id, page1, transactionId, iso,finacleTransId,"","");
             }
             else
             { 
            	 System.out.println("2========vatdebitAccount: "+vatdebitAccount+"   vatcreditAccount: "+vatcreditAccount+"    vatdebitAccType: "+vatdebitAccType+"    vatcreditAccType: "+vatcreditAccType
            			 +"    vatdebitTranCode: "+vatdebitTranCode+"  vatcreditTranCode: "+vatcreditTranCode+"  vatcreditTranCode: "+vatcreditTranCode+"  vatdebitNarration: "+vatdebitNarration
            			 +"     vatamount: "+vatamount+"   userId: "+userId+"  batchId: "+batchId+"   cdate: "+cdate+"   supervisor: "+supervisor+"  id: "+id+"   type: "+type+"    tranId: "+tranId);
//            	 raiseMan.raiseEntry(vatdebitAccount,vatcreditAccount,vatdebitAccType,vatcreditAccType,vatdebitTranCode
//                 		,vatcreditTranCode,vatdebitNarration,
//                 		vatcreditNarration,Double.parseDouble(vatamount), userId,batchId,cdate,supervisor,id,type);
         		raiseMan.raiseEntry(vatdebitAccount,vatcreditAccount,vatdebitAccType,vatcreditAccType,vatdebitTranCode
                		,vatcreditTranCode,vatdebitNarration,
                		vatcreditNarration,Double.parseDouble(vatamount), userId,batchId,cdate,supervisor,id,type);
       
            	 iso=bus.transferFund(vatdebitNarration,vatcreditAccount,vatdebitAccount,Double.parseDouble(vatamount),finacleTransId,SbuCode);
            	 //iso=bus.interaccounttransferFund(vatdebitNarration,vatdebitAccount,vatcreditAccount,Double.parseDouble(vatamount),finacleTransId,SbuCode,SbuCode);
                  System.out.println("#####################################################################>>>>>>>>  3: "+iso);
//                  ad.insertRaiseEntryTransaction(userId,raiseEntryNarration,vatdebitAccount,vatcreditAccount,Double.parseDouble(vatamount),iso,id,page1,"",assetCode,finacleTransId,costdebitAcctName,costcreditAcctName,userId);
                  ad.insertRaiseEntryTransaction(userId,raiseEntryNarration,vatdebitAccount ,vatcreditAccount,Double.parseDouble(vatamount),iso,id,page1,transactionId,systemIp,tranId,assetCode,finacleTransId,costdebitAcctName,costcreditAcctName,userId,recType);                  
                  ad.insertRaiseEntryTransactionArchive(userId,raiseEntryNarration,vatdebitAccount,vatcreditAccount,Double.parseDouble(vatamount),iso,id,page1,transactionId,systemIp,macAddress,tranId,assetCode);
             }
     	 
           if(iso.equalsIgnoreCase("000"))
             {
        	   	 String reversalId = tranId.substring(0, 1);
        	     if(reversalId.equals("R")){tranId = tranId.substring(1, tranId.length());}
           	     if(!reversalId.equals("R")){tranId = "R"+tranId;}
                    String q = "update am_Raisentry_Transaction set iso = '-001' where asset_id='"+id+"' and Trans_id = '"+tranId+"' and transactionId = "+transactionId+"";
                    ad.updateAssetStatusChange(q);	               	   
                    System.out.println("#####################################################################  4");
         			msgText1 = message.MailMessage(mail_code, transaction_type)+", \n";
        			msgText1 += "For asset id  "+id+"\n";
        			msgText1 += "For  "+vatdebitNarration+"\n";
        			msgText1 += "Debit Account  "+vatdebitAccount+"\n";
        			msgText1 += "Credit Account  "+vatcreditAccount+"\n";
        			msgText1 += "Amount  "+formata.formatAmount(vatamount)+"\n";
        			msgText1 += "Login here http://172.27.13.113:8080/legend2.net" ;
          			//ret = bulkMail.sendMail(signinname,transaction_type,msgText1,directory,to);
          			//System.out.println("Output of the mail is >>>>"+ret);
        			mail.sendMail(to,subject,msgText1);
            }	
//   	    to display transaction status 
		    String status =  records.getCodeName("select description from am_error_description where error_code='"+iso+"' ");
		    out.print("<script>alert('"+status+"');</script>");
		    out.print("<script>window.close('raiseAssetDetailAdvancePayment.jsp');</script>");
           }	
        }else {
    		out.print("<script>alert('You have No Right')</script>");
    	}
        }
      
        catch(Exception e)
        {
        	e.printStackTrace();	
        }
    }
    public String getServletInfo()
    {
        return "Process Action Servlet";
    }
}