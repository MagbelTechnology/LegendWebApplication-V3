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

public class ProcessCreateWitholding extends HttpServlet
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
    public ProcessCreateWitholding()
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
        String page = request.getParameter("page1");
        int assetCode = request.getParameter("assetCode") == null?0:Integer.parseInt(request.getParameter("assetCode"));
        System.out.println("<<<userId >>>>"+userId);
        String page1 = "";
        if(page==null){
//         page1 = "ASSET CREATION RAISE ENTRY";
        }else{
        	page1 = page;
        }
        String iso;
        String msgText1 ="";
        String to ="";
        String mail_code="1";
        String subject = "Raise Entry";
        String transaction_type="Raise_Entry";
        String taxdebitAcctName = "";
        String taxcreditAcctName = "";
        String batchId = "RE"+Integer.toString((int)(Math.random()*8000)+1000);
        String cdate = sdf.format(new Date());
		/**
        Post TAX Transaction
        */
        String categoryCode = request.getParameter("categoryCode");
        String taxdebitAccount = request.getParameter("tax_dr_account");
        String taxdebitAcctNo = request.getParameter("tax_dr_account");
        if(taxdebitAcctNo.substring(0,3)=="NGN"){taxdebitAcctNo = taxdebitAcctNo.substring(8,16);}
        if(taxdebitAcctNo.substring(2,5)=="NGN"){taxdebitAcctNo = taxdebitAcctNo.substring(6,14);}
//        System.out.println("<<<taxdebitAcctNo >>>>"+taxdebitAcctNo);
        
        String taxdebitAccType = request.getParameter("tax_dr_accttype");
        String taxdebitTranCode = request.getParameter("tax_dr_trancode");
        String taxdebitNarration = request.getParameter("tax_dr_narration");
        String taxcreditAccount = request.getParameter("tax_cr_account");
 //       String taxcreditAcctNo = taxcreditAccount.substring(8,16);
        String taxcreditAcctNo = request.getParameter("tax_cr_account");
        if(taxcreditAcctNo.substring(0,3).equals("NGN")){taxcreditAcctNo = taxcreditAcctNo.substring(8,16);}
        if(taxcreditAcctNo.substring(2,5).equals("NGN")){taxcreditAcctNo = taxcreditAcctNo.substring(6,14);}
//       System.out.println("<<<taxcreditAcctNo >>>>"+taxcreditAcctNo+"    <<<taxdebitAcctNo >>>>: "+taxdebitAcctNo);
        String taxcreditAccType = request.getParameter("tax_cr_accttype");
        String taxcreditTranCode = request.getParameter("tax_cr_trancode");
        String taxcreditNarration = request.getParameter("tax_cr_narration");
        String taxamount = request.getParameter("wh_amount");
        String supervisor = request.getParameter("supervisor");
        String subjectTowht = request.getParameter("subjectTowht"); 
        String type="Asset Creation";
        String transactionId = request.getParameter("transactionIdWitholding");
        String tranId = request.getParameter("tranId");
        String SbuCode = request.getParameter("SbuCode");
        String recType = request.getParameter("recType");
        String systemIp =request.getRemoteAddr();
        String macAddress= "";
        String finacleTransId = records.getGeneratedTransId();
        String raiseEntryNarration = taxdebitNarration;
       // String sbu_code = SbuCode+ "|" +SbuCode;
        taxdebitNarration = SbuCode+"|"+SbuCode+"|"+taxdebitNarration;
        String sbu_code = SbuCode+SbuCode;
//        String taxdebitAcctName = records.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+taxdebitAcctNo+"'");
//        String taxcreditAcctName = records.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+taxcreditAcctNo+"'");
//        System.out.println("=====Id in ProcessCreateWitholding: "+id);
//        String [] idSplit = id.split("/");       
////        String test10 = "select category_name from am_ad_category where Asset_Ledger = '"+taxdebitAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
////        String test11 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+taxdebitAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
////        String test12 = "select category_name from am_ad_category where DEP_LEDGER = '"+taxdebitAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
//        String test13 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+taxdebitAcctNo+"' ";
////        taxdebitAcctName = records.getCodeName(test10);
////        System.out.println("==========test10: "+test10+"  test11: "+test11+"   test12: "+test12+"    test13: "+test13);
////        System.out.println("==========ProcessCreateWitholding 1: ");
////        if(taxdebitAcctName.equalsIgnoreCase("")){taxdebitAcctName = records.getCodeName(test11);}
////        if(taxdebitAcctName.equalsIgnoreCase("")){taxdebitAcctName = records.getCodeName(test12);}  
////        if(taxdebitAcctName.equalsIgnoreCase("")){taxdebitAcctName = records.getCodeName(test13);}  
//        System.out.println("==========ProcessCreateWitholding 2: ");
//        String test20 = "select category_name from am_ad_category where Asset_Ledger = '"+taxcreditAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
//        String test21 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+taxcreditAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
//        String test22 = "select category_name from am_ad_category where DEP_LEDGER = '"+taxcreditAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
////        String test23 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+taxcreditAcctNo+"' ";
//        taxcreditAcctName = records.getCodeName(test20);
////        System.out.println("==========test20: "+test20+"  test21: "+test21+"   test22: "+test22);
////        System.out.println("==========ProcessCreateWitholding 3: ");
//        if(taxcreditAcctName.equalsIgnoreCase("")){taxcreditAcctName = records.getCodeName(test21);}
//        if(taxcreditAcctName.equalsIgnoreCase("")){taxcreditAcctName = records.getCodeName(test22);}  
//        if(taxcreditAcctName.equalsIgnoreCase("")){taxcreditAcctName = records.getCodeName(test20);}  
//        if(taxcreditAcctName.equalsIgnoreCase("")){taxcreditAcctName="Witholding Account";}
//        System.out.println("==========ProcessCreateWitholding 4: ");
//		    String costdebitAcctNo = taxdebitAccount;
//		    String costcreditAcctNo = taxcreditAccount;  		
////		   System.out.println("taxdebitAccount.substring(0,3) >>>>>> " + taxdebitAccount.substring(0,3)+"    taxcreditAccount.substring(0,3): "+taxcreditAccount.substring(0,3));
//        if(taxdebitAccount.substring(0,3).equalsIgnoreCase("NGN")){costdebitAcctNo = taxdebitAccount.substring(8,16);}
////        System.out.print("======costdebitAcctNo: "+costdebitAcctNo);
//        if(taxcreditAccount.substring(0,3).equalsIgnoreCase("NGN")){costcreditAcctNo = taxcreditAccount.substring(8,16);}
//        if(taxdebitAccount.substring(2,5).equalsIgnoreCase("NGN")){costdebitAcctNo = taxdebitAccount.substring(6,14);}
//        if(taxcreditAccount.substring(2,5).equalsIgnoreCase("NGN")){costcreditAcctNo = taxcreditAccount.substring(6,14);}
////	 String sbu_code = SbuCode+SbuCode;
//	 taxdebitNarration = SbuCode+"|"+SbuCode+"|"+taxdebitNarration;
//        String costdebitAcctName = records.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+costdebitAcctNo+"'");
//        String costcreditAcctName = records.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+costcreditAcctNo+"'");
////   	        System.out.println("ID >>>>>> " + id+"  costdebitAcctNo: "+costdebitAcctNo+"   costcreditAcctNo: "+costcreditAcctNo);
////        String [] idSplit = id.split("/");
//        String test10 = "select category_name from am_ad_category where Asset_Ledger = '"+costdebitAcctNo+"' and category_code = '"+categoryCode+"'";
//        String test11 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+costdebitAcctNo+"' and category_code = '"+categoryCode+"'";
//        String test12 = "select category_name from am_ad_category where DEP_LEDGER = '"+costdebitAcctNo+"' and category_code = '"+categoryCode+"'";
//        String test13 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+costdebitAcctNo+"' ";
//        String test14 = "select asset_acq_ac from am_gb_company where asset_acq_ac = '"+costdebitAcctNo+"' ";
//        String test15 = "select Fed_Wht_Account from am_gb_company where Fed_Wht_Account = '"+costdebitAcctNo+"' ";
////        System.out.println("test14 in Number 3 >>>>>> " + test14);
//        costdebitAcctName = records.getCodeName(test10);
//        if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = records.getCodeName(test11);}
//        if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = records.getCodeName(test12);}
//        if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = records.getCodeName(test13);}
////	        if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = approv.getCodeName(test14);}
//        if(records.getCodeName(test14)!="" && costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = "Asset Acquisition Suspense Account";}
//        if(records.getCodeName(test15)!="" && costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = "With Holding Tax Account";}
//        
//        String test20 = "select category_name from am_ad_category where Asset_Ledger = '"+costcreditAcctNo+"' and category_code = '"+categoryCode+"'";
//        String test21 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+costcreditAcctNo+"' and category_code = '"+categoryCode+"'";
//        String test22 = "select category_name from am_ad_category where DEP_LEDGER = '"+costcreditAcctNo+"' and category_code = '"+categoryCode+"'";
//        String test23 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+costcreditAcctNo+"' ";
//        String test24 = "select Fed_Wht_Account from am_gb_company where Fed_Wht_Account = '"+costcreditAcctNo+"' ";
//        String test25 = "select asset_acq_ac from am_gb_company where asset_acq_ac = '"+costcreditAcctNo+"' ";
////        System.out.println("test24 in Number 3 >>>>>> " + test24); 
//      costcreditAcctName = records.getCodeName(test20);
//      if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = records.getCodeName(test21);}
//      if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = records.getCodeName(test22);}
//      if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = records.getCodeName(test23);}
////      if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test24);}
//      if(records.getCodeName(test24)!="" && costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = "With Holding Tax Account";}
//      if(records.getCodeName(test25)!="" && costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = "Asset Acquisition Suspense Account";}        

        String costdebitAcctNo = taxdebitAccount;  
		    String costcreditAcctNo = taxcreditAccount;  		
		   System.out.println("taxdebitAccount.substring(0,3) >>>>>> " + taxdebitAccount.substring(0,3)+"    taxcreditAccount.substring(0,3): "+taxcreditAccount.substring(0,3));
        if(taxdebitAccount.substring(0,3).equalsIgnoreCase("NGN")){costdebitAcctNo = taxdebitAccount.substring(8,16);}
        System.out.print("======costdebitAcctNo: "+costdebitAcctNo);
        if(taxcreditAccount.substring(0,3).equalsIgnoreCase("NGN")){costcreditAcctNo = taxcreditAccount.substring(8,16);}
        if(taxdebitAccount.substring(2,5).equalsIgnoreCase("NGN")){costdebitAcctNo = taxdebitAccount.substring(6,14);}
        if(taxcreditAccount.substring(2,5).equalsIgnoreCase("NGN")){costcreditAcctNo = taxcreditAccount.substring(6,14);}
//	 String sbu_code = SbuCode+SbuCode;
	 taxdebitNarration = SbuCode+"|"+SbuCode+"|"+taxdebitNarration;
        String costdebitAcctName = records.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+costdebitAcctNo+"'");
        String costcreditAcctName = records.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+costcreditAcctNo+"'");
   	        System.out.println("ID >>>>>> " + id+"  costdebitAcctNo: "+costdebitAcctNo+"   costcreditAcctNo: "+costcreditAcctNo);
//        String [] idSplit = id.split("/");
        String test10 = "select category_name from am_ad_category where Asset_Ledger = '"+costdebitAcctNo+"' and category_code = '"+categoryCode+"'";
        String test11 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+costdebitAcctNo+"' and category_code = '"+categoryCode+"'";
        String test12 = "select category_name from am_ad_category where DEP_LEDGER = '"+costdebitAcctNo+"' and category_code = '"+categoryCode+"'";
        String test13 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+costdebitAcctNo+"' ";
        String test14 = "select asset_acq_ac from am_gb_company where asset_acq_ac = '"+costdebitAcctNo+"' ";
        String test15 = "";
        if(subjectTowht.equals("F")){test15 = "select Fed_Wht_Account from am_gb_company where Fed_Wht_Account = '"+costdebitAcctNo+"' ";}
        if(subjectTowht.equals("S")){test15 = "select Wht_Account from am_gb_company where Wht_Account = '"+costdebitAcctNo+"' ";}
        System.out.println("test15 in Number 3 >>>>>> " + test15);
        costdebitAcctName = records.getCodeName(test10);
        if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = records.getCodeName(test11);}
        if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = records.getCodeName(test12);}
        if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = records.getCodeName(test13);}  
//	        if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = approv.getCodeName(test14);}
        if(records.getCodeName(test14)!="" && costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = "Asset Acquisition Suspense Account";}
        if(records.getCodeName(test15)!="" && costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = "With Holding Tax Account";}
        System.out.println("costdebitAcctName >>>>>> " + costdebitAcctName);
        String test20 = "select category_name from am_ad_category where Asset_Ledger = '"+costcreditAcctNo+"' and category_code = '"+categoryCode+"'";
        String test21 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+costcreditAcctNo+"' and category_code = '"+categoryCode+"'";
        String test22 = "select category_name from am_ad_category where DEP_LEDGER = '"+costcreditAcctNo+"' and category_code = '"+categoryCode+"'";
        String test23 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+costcreditAcctNo+"' ";
        String test24 = "";
        if(subjectTowht.equals("F")){test24 = "select Fed_Wht_Account from am_gb_company where Fed_Wht_Account = '"+costcreditAcctNo+"' ";}
        if(subjectTowht.equals("S")){test24 = "select Wht_Account from am_gb_company where Wht_Account = '"+costcreditAcctNo+"' ";}
        String test25 = "select asset_acq_ac from am_gb_company where asset_acq_ac = '"+costcreditAcctNo+"' ";
        System.out.println("test24 in Number 3 >>>>>> " + test24); 
      costcreditAcctName = records.getCodeName(test20);
      if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = records.getCodeName(test21);}
      if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = records.getCodeName(test22);}    
      if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = records.getCodeName(test23);}
//      if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test24);}
      
      if(records.getCodeName(test24)!="" && costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = "With Holding Tax Account";}
      if(records.getCodeName(test25)!="" && costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = "Asset Acquisition Suspense Account";}
      System.out.println("costcreditAcctName >>>>>> " + costcreditAcctName);
       try
        {      
        if (!userClass.equals("NULL") || userClass!=null){	
//        	 System.out.println("==========taxamount in ProcessCreateWitholding: "+taxamount);
        if(taxamount!=null)
        {
        taxamount = taxamount.replaceAll(",","");
        if (supervisor == "" || supervisor == null || supervisor == "*" || supervisor.equals("*")) {
   		 supervisor = "";
        }
        String tranIdReverse = "R"+tranId;
      //     if(ad.isoChecking(   id, page1, transactionId))
//        System.out.println("==========ProcessCreateWitholding 5: ");
//        System.out.println("==========Id: "+id+"    page1: "+page1+"   transactionId: "+transactionId+"      tranId: "+tranId);
        if(ad.isoChecking(   id, page1, transactionId,tranId))
        {
//        	System.out.println("==========ProcessCreateWitholding 5.0: ");
        	 //iso=bus.interaccounttransferFund(taxdebitNarration,taxcreditAccount,taxdebitAccount ,Double.parseDouble(taxamount),finacleTransId,SbuCode,SbuCode);
        	 iso=bus.transferFund(taxdebitNarration,taxcreditAccount,taxdebitAccount ,Double.parseDouble(taxamount),finacleTransId,SbuCode);
        	//ad.updateRaiseEntryTransaction(   id, page1, transactionId, iso);
                 ad.updateRaiseEntryTransaction(   id, page1, transactionId, iso,systemIp,tranId,finacleTransId,"","");
        }
        else
        { // System.out.println("==========ProcessCreateWitholding 6: ");
//        	System.out.println("====taxdebitAccType: "+taxdebitAccType+"    taxcreditAccType: "+taxcreditAccType+"  taxdebitTranCode: "+taxdebitTranCode+"  taxcreditTranCode: "+taxcreditTranCode+"  userId: "+userId);
        	if(taxdebitAccType==null){taxdebitAccType = "";}
        	if(taxcreditAccType==null){taxcreditAccType = "";}
        	if(taxdebitTranCode==null){taxdebitTranCode = "";}
        	if(taxcreditTranCode==null){taxcreditTranCode = "";}
        raiseMan.raiseEntry(taxdebitAccount,taxcreditAccount,taxdebitAccType,taxcreditAccType,taxdebitTranCode
        		,taxcreditTranCode,taxdebitNarration,
        		taxcreditNarration,Double.parseDouble(taxamount), userId,batchId,cdate,supervisor,id,type);
        		
           //iso=bus.interaccounttransferFund(taxdebitNarration,taxcreditAccount,taxdebitAccount ,Double.parseDouble(taxamount),finacleTransId,SbuCode,SbuCode);
           iso=bus.transferFund(taxdebitNarration,taxcreditAccount,taxdebitAccount ,Double.parseDouble(taxamount),finacleTransId,SbuCode);
           //out.print("<script>alert('"+iso+"');window.close();</script>");
//         System.out.println("#####################################################################>>>>>>>>  3"+iso);
    //       ad.insertRaiseEntryTransaction(userId,taxdebitNarration,taxcreditAccount,taxdebitAccount ,Double.parseDouble(taxamount),iso,id,page1,transactionId);
         ad.insertRaiseEntryTransaction (userId,raiseEntryNarration,taxdebitAccount ,taxcreditAccount,Double.parseDouble(taxamount),iso,id,page1,transactionId,systemIp,tranId,assetCode,finacleTransId,costdebitAcctName,costcreditAcctName,userId,recType);
          ad.insertRaiseEntryTransactionArchive (userId,raiseEntryNarration,taxcreditAccount,taxdebitAccount ,Double.parseDouble(taxamount),iso,id,page1,transactionId,systemIp,macAddress,tranId,assetCode);
        }
         if(iso.equalsIgnoreCase("000"))
             {
    	   	 String reversalId = tranId.substring(0, 1);
    	     if(reversalId.equals("R")){tranId = tranId.substring(1, tranId.length());}
       	     if(!reversalId.equals("R")){tranId = "R"+tranId;}
                String q = "update am_Raisentry_Transaction set iso = '-001' where asset_id='"+id+"' and Trans_id = '"+tranId+"' and transactionId = "+transactionId+"";
                ad.updateAssetStatusChange(q);	           	 
 //       	 ad.updateAssetStatusChange("update am_Raisentry_Transaction set iso = '' where trans_id='"+tranIdReverse+"' and transactionId = "+transactionId+"");
 //                    System.out.println("#####################################################################  3");
         			msgText1 = message.MailMessage(mail_code, transaction_type)+", \n";
        			msgText1 += "For asset id  "+id+"\n";
        			msgText1 += "For  "+taxdebitNarration+"\n";
        			msgText1 += "Debit Account  "+taxdebitAccount+"\n";
        			msgText1 += "Credit Account  "+taxcreditAccount+"\n";
        			msgText1 += "Amount  "+formata.formatAmount(taxamount)+"\n";
        			msgText1 += "Login here http://172.27.13.113:8080/legend2.net" ;
          			//ret = bulkMail.sendMail(signinname,transaction_type,msgText1,directory,to);
          			//System.out.println("Output of the mail is >>>>"+ret);
        			mail.sendMail(to,subject,msgText1);
        			
               }
//         System.out.println("==========ProcessCreateWitholding 10: ");
// 	    to display transaction status  
		    String status =  records.getCodeName("select description from am_error_description where error_code='"+iso+"' ");
		    out.print("<script>alert('"+status+"');</script>");
           out.print("<script>window.close('raiseAssetDetailAdvancePayment.jsp');</script>");
          // session.setAttribute("assetid", id);
    	  // session.setAttribute("page1", page1);
    	  // out.print("<script>window.open('raiseEntryList.jsp','raiseasset4','width=850,height=350,scrollbars=Yes,resizable=Yes,align=center');</script>");

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