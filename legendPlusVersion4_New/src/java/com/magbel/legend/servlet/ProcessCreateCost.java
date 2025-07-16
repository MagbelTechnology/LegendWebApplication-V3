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

public class ProcessCreateCost extends HttpServlet
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
    public ProcessCreateCost()
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
        String systemIp =request.getRemoteAddr();
        String macAddress= "";
        /**
        Post Cost Transaction
        */
        String page1 = request.getParameter("page1");
        String ThirdPartyLabel = request.getParameter("ThirdPartyLabel");
        String costdebitAccount = request.getParameter("cost_dr_account");
        String costdebitAcctNo =  costdebitAccount;
//        String costdebitAcctNo = costdebitAccount.substring(8,16);
        System.out.println("====ThirdPartyLabel=====: "+ThirdPartyLabel+"      costdebitAccount: "+costdebitAccount);
        if(ThirdPartyLabel.equalsIgnoreCase("INTEGRIFY")){costdebitAcctNo = costdebitAccount.substring(8,16);}
        if(ThirdPartyLabel.equalsIgnoreCase("K2")){costdebitAcctNo = costdebitAccount.substring(6,14);}
        String costdebitAccType = request.getParameter("cost_dr_accttype");
        String costdebitTranCode = request.getParameter("cost_dr_trancode");
        String costdebitNarration = request.getParameter("cost_dr_narration");
        String costcreditAccount = request.getParameter("cost_cr_account");
        String costcreditAcctNo = "";
        if(ThirdPartyLabel.equalsIgnoreCase("K2")){ costcreditAcctNo = costcreditAccount.substring(6,14);}
        if(ThirdPartyLabel.equalsIgnoreCase("INTEGRIFY")){ costcreditAcctNo = costcreditAccount.substring(8,16);}
//        System.out.println("====costcreditAccount=====: "+costcreditAccount+"    ====costcreditAccount=====: "+costcreditAccount+"    ThirdPartyLabel: "+ThirdPartyLabel);
        String costcreditAccType = request.getParameter("cost_cr_accttype");
        String costcreditTranCode = request.getParameter("cost_cr_trancode");
        String costcreditNarration = request.getParameter("cost_cr_narration");
        String costamount = request.getParameter("cost_amount");
        costamount = costamount.replaceAll(",",""); 
        String supervisor = request.getParameter("supervisor");
        String type="Asset Creation";
        String transactionId = request.getParameter("transactionIdCost");
//        System.out.println("====transactionId: "+transactionId);
        String tranId = request.getParameter("tranId");
        String SbuCode = request.getParameter("SbuCode");
        String integrifyid = request.getParameter("integrifyid");
        String branchMail = request.getParameter("branchMail");
        String categoryCode = request.getParameter("categoryCode");
        String finacleTransId=records.getGeneratedTransId();
        String raiseEntryNarration = costdebitNarration; 
        double d = 100256785.56;
        d= 4676.46;
        String amount = String.valueOf(d);
        String str = String.format("%f", d);
        String strd = Double.toString(d);
        
//        System.out.println("Split Amount..."+amount+"   =======str: "+str+"    =====strd: "+strd);
        String anumber = str.replace(".","");

//        System.out.println("A anumber..."+anumber+"   =======str: "+str);
        int testAmount = (int)d;
 //       System.out.println("=======testAmount: "+testAmount);
       // String sbu_code = SbuCode+ "|" +SbuCode;  
        String sbu_code = SbuCode+SbuCode;
        costcreditNarration = SbuCode+"|"+SbuCode+"|"+costcreditNarration;
        costdebitNarration = SbuCode+"|"+SbuCode+"|"+costdebitNarration;
        String costdebitAcctName = "";
//        String test1 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+costdebitAccount+"' ";
//        String Test2 = "select category_name from am_ad_category where Asset_Ledger = '"+costcreditAcctNo+"' AND category_code = '"+categoryCode+"'";
//        System.out.println("====test1: "+test1+"   Test2: "+Test2);
        if(ThirdPartyLabel.equalsIgnoreCase("INTEGRIFY")){costdebitAcctName = records.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+costdebitAcctNo+"'  and substring(category_acronym,1,1) not in ('F','U')");}
//        System.out.println("====costdebitAcctName: "+costdebitAcctName);
        if(ThirdPartyLabel.equalsIgnoreCase("K2")){costdebitAcctName = records.getCodeName("select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+costdebitAcctNo+"' ");}
        String costcreditAcctName = records.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+costcreditAcctNo+"' AND category_code = '"+categoryCode+"'  and substring(category_acronym,1,1) not in ('F','U')");
//        System.out.println("====costcreditAcctName: "+costcreditAcctName);
        if(costdebitAcctName.equals("")){costdebitAcctName = "Asset Acqusition Suspense Account";}   
        if(costcreditAcctName.equals("")){costcreditAcctName = "Asset Acquisition Suspense Account";}
//        System.out.println("====costdebitNarration: "+costdebitNarration+"   costcreditAccount: "+costcreditAccount+"  costamount: "+costamount+"   finacleTransId: "+finacleTransId+"  SbuCode: "+SbuCode+"   tranId: "+tranId);
       // testfield = "125";
        String test1 = "select category_name from am_ad_category where Asset_Ledger = '"+costdebitAcctNo+"'  and substring(category_acronym,1,1) not in ('F','U')";
        String test2 = "select category_name from am_ad_category where Asset_Ledger = '"+costcreditAcctNo+"' AND category_code = '"+categoryCode+"'  and substring(category_acronym,1,1) not in ('F','U')";
        String test3 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+costdebitAcctNo+"' ";
//        System.out.println("========test1: "+test1+"    test2: "+test2+"     test3: "+test3);
        try 
        { 
//       	 System.out.println("====userClass: "+userClass);
        	 if (!userClass.equals("NULL") || userClass!=null){
        		// System.out.println("====About to process Transaction in ProcessCreateCost======");
        	 if (supervisor == "" || supervisor == null || supervisor == "*" || supervisor.equals("*")) {
        		 supervisor = "";
             }
        	 String tranIdReverse = "R"+tranId;
        	//if(ad.isoChecking(  id, page1, transactionId,tranId))
                    if(ad.isoChecking(  id, page1, transactionId,tranId))
        	{
        		iso=bus.transferFund(costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),finacleTransId,SbuCode);
        		//iso=bus.interaccounttransferFund(costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),finacleTransId,SbuCode,SbuCode);
        		ad.updateRaiseEntryTransaction(  id, page1, transactionId, iso, systemIp,tranId,finacleTransId,"","");
                      //  ad.updateRaiseEntryTransaction(  id, page1, transactionId, iso, systemIp);
                        ad.updateRaiseEntryTransactionArchive(id, page1, transactionId, iso,systemIp,macAddress,tranId);
                       // ad.updateRaiseEntryTransactionArchive(id, page1, transactionId, iso,systemIp,macAddress);
                }
        	else
        	{
               raiseMan.raiseEntry(costdebitAccount,costcreditAccount,costdebitAccType,costcreditAccType,costdebitTranCode
        		,costcreditTranCode,costdebitNarration,
        		costcreditNarration,Double.parseDouble(costamount), userId,batchId,cdate,supervisor,id,type);

               

              iso=bus.transferFund(costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),finacleTransId,sbu_code);
              //iso=bus.interaccounttransferFund(costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),finacleTransId,SbuCode,SbuCode);
             // ad.insertRaiseEntryTransaction(userId,costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),iso,id,page1,transactionId,systemIp);
                ad.insertRaiseEntryTransaction(userId,raiseEntryNarration,costcreditAccount,costdebitAccount,Double.parseDouble(costamount),iso,id,page1,transactionId,systemIp,tranId,assetCode,finacleTransId,costcreditAcctName,costdebitAcctName,userId,integrifyid);
                
              // ad.insertRaiseEntryTransactionArchive(userId,costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),iso,id,page1,transactionId,systemIp,macAddress);
                ad.insertRaiseEntryTransactionArchive(userId,raiseEntryNarration,costcreditAccount,costdebitAccount,Double.parseDouble(costamount),iso,id,page1,transactionId,systemIp,macAddress,tranId,assetCode);

                }
   
                      
              if(iso.equalsIgnoreCase("000"))
              {
                  String reversalId = tranId.substring(0, 1);
//                   System.out.println(">>>>>>>>>>>tranId====:  "+tranId+"   tranId.length: "+tranId.length());
//                   System.out.println(">>>>>>>>>>>tranId.substring(1, 1):  "+tranId.substring(0, 1)+"   New TranId: "+tranId.substring(1, tranId.length()));
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
//        			msgText1 += "Login here http://172.27.13.113:8080/legendPlus.net" ;
          			//ret = bulkMail.sendMail(signinname,transaction_type,msgText1,directory,to);
          			//System.out.println("Output of the mail is >>>>"+ret);
        			to = branchMail;
        			mail.sendMail(to,subject,msgText1);


               }
//      	    to display transaction status
  		    String status =  records.getCodeName("select description from am_error_description where error_code='"+iso+"' ");
  		    out.print("<script>alert('"+status+"');</script>");
              out.print("<script>window.close('raiseAssetDetailAdvancePayment.jsp');</script>");
           //   session.setAttribute("assetid", id);
       	  // session.setAttribute("page1", page1);
       	  // out.print("<script>window.open('raiseEntryList.jsp','raiseasset4','width=850,height=350,scrollbars=Yes,resizable=Yes,align=center');</script>");
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