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

public class ProcessPartPayWitholding extends HttpServlet
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
    public ProcessPartPayWitholding()
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
        Post TAX Transaction
        */
        String taxdebitAccount = request.getParameter("tax_dr_account");
        String taxdebitAccType = request.getParameter("tax_dr_accttype");
        String taxdebitTranCode = request.getParameter("tax_dr_trancode");
        String taxdebitNarration = request.getParameter("tax_dr_narration");
        String taxcreditAccount = request.getParameter("tax_cr_account");
        String taxcreditAccType = request.getParameter("tax_cr_accttype");
        String taxcreditTranCode = request.getParameter("tax_cr_trancode");
        String taxcreditNarration = request.getParameter("tax_cr_narration");
//        String taxdebitAcctNo = taxdebitAccount.substring(8,16);
//        String taxcreditAcctNo = taxcreditAccount.substring(8,16);
        String taxdebitAcctNo = "";
        String taxcreditAcctNo = "";
        if(taxdebitAccount.substring(0,2)=="NGN"){taxdebitAcctNo = taxdebitAccount.substring(8,16);}
        if(taxdebitAccount.substring(2,5)=="NGN"){taxdebitAcctNo = taxdebitAccount.substring(6,14);}
        if(taxcreditAccount.substring(0,2)=="NGN"){taxcreditAcctNo = taxcreditAccount.substring(8,16);}
        if(taxcreditAccount.substring(2,5)=="NGN"){taxcreditAcctNo = taxcreditAccount.substring(6,14);}
        String taxamount = request.getParameter("wh_amount");
        String supervisor = request.getParameter("supervisor");
        String pid = request.getParameter("pid");
        String transactionId = request.getParameter("transactionIdPartPayWitholding");
        String tranId = request.getParameter("tranId");
        String SbuCode = request.getParameter("SbuCode");
        String recType = request.getParameter("recType");
         String systemIp =request.getRemoteAddr();
         int assetCode = request.getParameter("assetCode") == null?0:Integer.parseInt(request.getParameter("assetCode"));
        String macAddress= "";
        String finacleTransId = records.getGeneratedTransId();
        String raiseEntryNarration = taxdebitNarration;
       // String sbu_code = SbuCode+ "|" +SbuCode;
        String sbu_code = SbuCode+SbuCode;
        taxdebitNarration = SbuCode+"|"+SbuCode+"|"+taxdebitNarration;
        String taxdebitAcctName = records.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+taxdebitAcctNo+"'");
        String taxcreditAcctName = records.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+taxcreditAcctNo+"'");
        try{ 
        	if (!userClass.equals("NULL") || userClass!=null){
        if(taxamount!=null)
        {
        taxamount = taxamount.replaceAll(",","");
    //    if(ad.isoChecking(  id, page1, transactionId))
    if(ad.isoChecking(  id, page1, transactionId,tranId))
        {
        	iso=bus.transferFund(taxdebitNarration,taxdebitAccount,taxcreditAccount,Double.parseDouble(taxamount),finacleTransId,SbuCode);
        	//iso=bus.interaccounttransferFund(taxdebitNarration,taxdebitAccount,taxcreditAccount,Double.parseDouble(taxamount),finacleTransId,SbuCode,SbuCode);
        	//ad.updateRaiseEntryTransaction(  id, page1, transactionId, iso);
 	        ad.updateRaiseEntryTransaction(  id, page1, transactionId, iso,systemIp,tranId,finacleTransId,"","");
        }
        else
        {
        raiseMan.raiseEntry(taxdebitAccount,taxcreditAccount,taxdebitAccType,taxcreditAccType,taxdebitTranCode
        		,taxcreditTranCode,taxdebitNarration,
        		taxcreditNarration,Double.parseDouble(taxamount), userId,batchId,cdate,supervisor,pid,"ASSET PAYMENT");
       



        iso=bus.transferFund(taxdebitNarration,taxdebitAccount,taxcreditAccount,Double.parseDouble(taxamount),finacleTransId,SbuCode);
       // iso=bus.interaccounttransferFund(taxdebitNarration,taxdebitAccount,taxcreditAccount,Double.parseDouble(taxamount),finacleTransId,SbuCode,SbuCode);
          // out.print("<script>alert('"+iso+"');window.close();</script>");
    //       ad.insertRaiseEntryTransaction(userId,taxdebitNarration,taxdebitAccount,taxcreditAccount,Double.parseDouble(taxamount),iso,id,page1,transactionId);
                 ad.insertRaiseEntryTransaction (userId,raiseEntryNarration,taxdebitAccount,taxcreditAccount,Double.parseDouble(taxamount),iso,id,page1,transactionId,systemIp,tranId,assetCode,finacleTransId,taxdebitAcctName,taxcreditAcctName,userId,recType);
                 ad.insertRaiseEntryTransactionArchive (userId,raiseEntryNarration,taxdebitAccount,taxcreditAccount,Double.parseDouble(taxamount),iso,id,page1,transactionId,systemIp,macAddress,tranId,assetCode);
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
        	            msgText1 += "For  "+taxdebitNarration+"\n";
        			msgText1 += "Debit Account  "+taxdebitAccount+"\n";
        			msgText1 += "Credit Account  "+taxcreditAccount+"\n";
        			msgText1 += "Amount  "+formata.formatAmount(taxamount)+"\n";
        			msgText1 += "Login here http://172.27.13.113:8080/legend2.net" ;
          			//ret = bulkMail.sendMail(signinname,transaction_type,msgText1,directory,to);
          			//System.out.println("Output of the mail is >>>>"+ret);
        			mail.sendMail(to,subject,msgText1);
            }
//   	    to display transaction status
		    String status =  records.getCodeName("select description from am_error_description where error_code='"+iso+"' ");
		    out.print("<script>alert('"+status+"');</script>");
           out.print("<script>window.close('raiseAssetPayment.jsp');</script>");
//           session.setAttribute("assetid", id);
//    	   session.setAttribute("page1", page1);
 //   	   out.print("<script>window.open('raiseEntryList.jsp','raiseasset4','width=850,height=350,scrollbars=Yes,resizable=Yes,align=center');</script>");
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