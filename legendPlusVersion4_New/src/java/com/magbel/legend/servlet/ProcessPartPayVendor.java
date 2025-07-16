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

public class ProcessPartPayVendor extends HttpServlet
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
    public ProcessPartPayVendor()
    {
    }

    public void init(ServletConfig config)
        throws ServletException
    {
        super.init(config);
        try
        {
        service  = new DepreciationProcessingManager();
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
        String page1 = "ASSET PART PAYMENT ENTRY";
        String iso;
        String msgText1 ="";
        String to ="";
        String mail_code="1";
        String subject = "Raise Entry";
        String transaction_type="Raise_Entry";
        String batchId = "RE"+Integer.toString((int)(Math.random()*8000)+1000);
        String cdate = sdf.format(new Date());
		/**
        Post VAT Transaction
        */
        String ThirdPartyLabel = request.getParameter("ThirdPartyLabel");
        String vatdebitAccount = request.getParameter("vendor_cost_dr_account");
        String vatdebitAccType = request.getParameter("vendor_cost_dr_accttype");
        String vatdebitTranCode = request.getParameter("vendor_cost_dr_trancode");
        String vatdebitNarration = request.getParameter("vendor_cost_dr_narration");
        String vatcreditAccount = request.getParameter("vendor_cost_cr_account");
        String vatcreditAccType = request.getParameter("vendor_cost_cr_accttype");
        String vatcreditTranCode = request.getParameter("vendor_cost_cr_trancode");
        String vatcreditNarration = request.getParameter("vendor_cost_cr_narration");
//        String aquisitionDRAcctNo = vatdebitAccount.substring(8,16);
        String aquisitionDRAcctNo = "";
        if(ThirdPartyLabel.equalsIgnoreCase("K2")){ aquisitionDRAcctNo = vatdebitAccount.substring(6,14);}
        if(!ThirdPartyLabel.equalsIgnoreCase("INTEGRIFY")){ aquisitionDRAcctNo = vatdebitAccount.substring(8,16);}
        String vatamount = request.getParameter("vendor_cost_amount");
        String SbuCode = request.getParameter("SbuCode");
        String supervisor = request.getParameter("supervisor");
        String pid = request.getParameter("pid");
        String transactionId = request.getParameter("transactionIdPartPayVendor");
        String tranId = request.getParameter("tranId");
        String recType = request.getParameter("recType");
         String systemIp =request.getRemoteAddr();
        String macAddress= "";
        int assetCode = request.getParameter("assetCode") == null?0:Integer.parseInt(request.getParameter("assetCode"));
String finacleTransId = records.getGeneratedTransId();
		String raiseEntryNarration = vatdebitNarration;
	//	String sbu_code = SbuCode+ "|" +SbuCode;
		String sbu_code = SbuCode+SbuCode;
		vatdebitNarration = SbuCode+"|"+SbuCode+"|"+vatdebitNarration;
        String aquisitiondebitAcctName = records.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+aquisitionDRAcctNo+"'");
        String vendorAcctName = records.getCodeName("select vendor_name from am_ad_vendor where account_number = '"+vatcreditAccount+"'");
        try{ 
        	if (!userClass.equals("NULL") || userClass!=null){
        if(vatamount!=null)
        {
        	vatamount = vatamount.replaceAll(",","");

         // if(ad.isoChecking(  id, page1, transactionId))
    if(ad.isoChecking(  id, page1, transactionId,tranId))
          {
        	  iso=bus.transferFund(vatdebitNarration,vatdebitAccount,vatcreditAccount,Double.parseDouble(vatamount),finacleTransId,SbuCode);
        	 // iso=bus.interaccounttransferFund(vatdebitNarration,vatdebitAccount,vatcreditAccount,Double.parseDouble(vatamount),finacleTransId,SbuCode,SbuCode);
                //   ad.updateRaiseEntryTransaction(  id, page1, transactionId, iso);
        	  ad.updateRaiseEntryTransaction(  id, page1, transactionId, iso,systemIp,tranId,finacleTransId,"","");
          }
          else
          {
        	raiseMan.raiseEntry(vatdebitAccount,vatcreditAccount,vatdebitAccType,vatcreditAccType,vatdebitTranCode
        		,vatcreditTranCode,vatdebitNarration,
        		vatcreditNarration,Double.parseDouble(vatamount), userId,batchId,cdate,supervisor,pid,"ASSET PAYMENT");             	
        	
        	
        	iso=bus.transferFund(vatdebitNarration,vatdebitAccount,vatcreditAccount,Double.parseDouble(vatamount),finacleTransId,SbuCode);
        	//iso=bus.interaccounttransferFund(vatdebitNarration,vatdebitAccount,vatcreditAccount,Double.parseDouble(vatamount),finacleTransId,SbuCode,SbuCode);
          // out.print("<script>alert('"+iso+"');window.close();</script>");
     //      ad.insertRaiseEntryTransaction(userId,vatdebitNarration,vatdebitAccount,vatcreditAccount,Double.parseDouble(vatamount),iso,id,page1,transactionId);
            ad.insertRaiseEntryTransaction (userId,raiseEntryNarration,vatdebitAccount,vatcreditAccount,Double.parseDouble(vatamount),iso,id,page1,transactionId,systemIp,tranId,assetCode,finacleTransId,aquisitiondebitAcctName,vendorAcctName,userId,recType);
             ad.insertRaiseEntryTransactionArchive (userId,raiseEntryNarration,vatdebitAccount,vatcreditAccount,Double.parseDouble(vatamount),iso,id,page1,transactionId,systemIp,macAddress,tranId,assetCode);

          }
           if(iso.equalsIgnoreCase("000"))
             {
       	   	 String reversalId = tranId.substring(0, 1);
       	     if(reversalId.equals("R")){tranId = tranId.substring(1, tranId.length());}
          	     if(!reversalId.equals("R")){tranId = "R"+tranId;}
                   String q = "update am_Raisentry_Transaction set iso = '-001' where asset_id='"+id+"' and Trans_id = '"+tranId+"' and transactionId = "+transactionId+"";
                   ad.updateAssetStatusChange(q);	              	   
                    System.out.println("#####################################################################  ");
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
           out.print("<script>window.close('raiseAssetPayment.jsp');</script>");
 //          session.setAttribute("assetid", id);
   // 	   session.setAttribute("page1", page1);
   // 	   out.print("<script>window.open('raiseEntryList.jsp','raiseasset4','width=850,height=350,scrollbars=Yes,resizable=Yes,align=center');</script>");
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