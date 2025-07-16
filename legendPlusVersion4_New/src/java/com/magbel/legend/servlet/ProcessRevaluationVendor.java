package com.magbel.legend.servlet;



import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.vao.Approval;

import java.io.*;

import magma.AssetRecordsBean;
import magma.net.manager.DepreciationProcessingManager;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.magbel.legend.bus.FinancialExchangeServiceBus;

import magma.net.manager.RaiseEntryManager;

import com.magbel.legend.mail.EmailSmsServiceBus;

import magma.util.Codes;

import java.util.*;

import  com.magbel.util.CurrencyNumberformat ;

import java.text.SimpleDateFormat;

public class ProcessRevaluationVendor extends HttpServlet
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
    public ProcessRevaluationVendor()
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
        
       
        
        String userId =(String)session.getAttribute("CurrentUser");
        String id = request.getParameter("asset_id");
        String page1 = "ASSET REVALUATION RAISE ENTRY";
        String iso;
        String msgText1 ="";
        String to ="";
        String mail_code="1";
        String subject = "Raise Entry";
        String transaction_type="Raise_Entry";
        String batchId = "RE"+Integer.toString((int)(Math.random()*8000)+1000);
        String cdate = sdf.format(new Date());
        /**
        Vendor Transaction
        **/
        String tranId = request.getParameter("tranId");
         String systemIp =request.getRemoteAddr();
        String macAddress= "";
        /*	
        String vendordebitAccount = request.getParameter("vendor_dr_account");
        String vendordebitAccType = request.getParameter("vendor_dr_accttype");
        String vendordebitTranCode = request.getParameter("vendor_dr_trancode");
        String vendordebitNarration = request.getParameter("vendor_dr_narration");
        String vendorcreditAccount = request.getParameter("vendor_cr_account");
        String vendorcreditAccType = request.getParameter("vendor_cr_accttype");
        String vendorcreditTranCode = request.getParameter("vendor_cr_trancode");
        String vendorcreditNarration = request.getParameter("vendor_cr_narration");
        String vendoramount = request.getParameter("vendor_amount");
        String supervisor = request.getParameter("supervisor");
        String type="Asset Creation";
        String transactionId = request.getParameter("transactionIdVendor");
        */
        int assetCode = request.getParameter("assetCode") == null?0:Integer.parseInt(request.getParameter("assetCode"));
        String vatDrAcct = request.getParameter("vatDrAcct");
        if(vatDrAcct==null){vatDrAcct="";}
        String vatCrAcct = request.getParameter("vatCrAcct");
        if(vatCrAcct==null){vatCrAcct="";}
        String vatDrAcctType = request.getParameter("vatDrAcctType");
        if(vatDrAcctType==null){vatDrAcctType="";}
        String vatCrAcctType = request.getParameter("vatCrAcctType");
        if(vatCrAcctType==null){vatCrAcctType="";}
        String vatDrTranCode = request.getParameter("vatDrTranCode");
        if(vatDrTranCode==null){vatDrTranCode="";}
        String vatCrTranCode = request.getParameter("vatCrTranCode");
        if(vatCrTranCode==null){vatCrTranCode="";}
        String vatDrNarration = request.getParameter("vatDrNarration");
        if(vatDrNarration==null){vatDrNarration="";}
        String vatCrNarration = request.getParameter("vatCrNarration");
        if(vatCrNarration==null){vatCrNarration="";}
        String supervisor = request.getParameter("supervisor");
        String transactionId = request.getParameter("transactionIdVendor");
        String vatAmt = request.getParameter("vatAmt");
        String SbuCode = request.getParameter("SbuCode");
        String recType = request.getParameter("recType");
        vatAmt = vatAmt.replaceAll(",","");
        String type="Asset Revaluation";
         String finacleTransId = records.getGeneratedTransId();
         String raiseEntryNarration = vatDrNarration;
         //String sbu_code = SbuCode+ "|" +SbuCode;
         String sbu_code = SbuCode+SbuCode;
         vatDrNarration = SbuCode+"|"+SbuCode+"|"+vatDrNarration;
   try
        {
       
        if (supervisor == "" || supervisor == null || supervisor == "*" || supervisor.equals("*")) {
   		 supervisor = "";
        }
       if(ad.isoChecking(   id, page1, transactionId,tranId))
       {
    	  // iso=bus.transferFund(vatDrNarration,vatDrAcct,vatCrAcct,Double.parseDouble(vatAmt),finacleTransId,sbu_code);
    	   iso=bus.interaccounttransferFund(vatDrNarration,vatDrAcct,vatCrAcct,Double.parseDouble(vatAmt),finacleTransId,SbuCode,SbuCode);
    	   ad.updateRaiseEntryTransaction(   id, page1, transactionId, iso,systemIp,tranId,finacleTransId,"","");
       }
       else
       {
        raiseMan.raiseEntry(vatDrAcct,vatCrAcct,vatDrAcctType,vatCrAcctType,vatDrTranCode
        		,vatCrTranCode,vatDrNarration,
        		vatCrNarration,Double.parseDouble(vatAmt), userId,batchId,cdate,supervisor,id,type);
        		
        //iso=bus.transferFund(vatDrNarration,vatDrAcct,vatCrAcct,Double.parseDouble(vatAmt),finacleTransId,sbu_code);
        iso=bus.interaccounttransferFund(vatDrNarration,vatDrAcct,vatCrAcct,Double.parseDouble(vatAmt),finacleTransId,SbuCode,SbuCode);
         ad.insertRaiseEntryTransaction (userId,raiseEntryNarration,vatDrAcct,vatCrAcct,Double.parseDouble(vatAmt),iso,id,page1,transactionId,systemIp,tranId,assetCode,finacleTransId);
       }
         if(iso.equalsIgnoreCase("000"))
             {
            //        System.out.println("#####################################################################  2");
                    ad.revaluationsumation(id,assetCode,Double.parseDouble(vatAmt));
         			msgText1 = message.MailMessage(mail_code, transaction_type)+", \n";
        			msgText1 += "For asset id  "+id+"\n";
                          msgText1 += "For  "+vatDrNarration +"\n";
        			msgText1 += "Debit Account  "+vatDrAcct+"\n";
        			msgText1 += "Credit Account  "+vatCrAcct+"\n";
        			msgText1 += "Amount  "+formata.formatAmount(vatAmt)+"\n";
        			msgText1 += "Login here http://172.27.13.113:8080/legend2.net" ;
          			//ret = bulkMail.sendMail(signinname,transaction_type,msgText1,directory,to);
          			//System.out.println("Output of the mail is >>>>"+ret);
        			mail.sendMail(to,subject,msgText1);
        			
            }	
// 	    to display transaction status
		    String status =  records.getCodeName("select description from am_error_description where error_code='"+iso+"' ");
		    out.print("<script>alert('"+status+"');</script>");
           out.print("<script>window.close('raiseAssetDetailAdvancePayment.jsp');</script>");
         //  session.setAttribute("assetid", id);
    	 //  session.setAttribute("page1", page1);
    	 //  out.print("<script>window.open('raiseEntryList.jsp','raiseasset4','width=850,height=350,scrollbars=Yes,resizable=Yes,align=center');</script>");

          
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