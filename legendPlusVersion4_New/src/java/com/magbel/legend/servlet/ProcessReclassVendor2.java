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

public class ProcessReclassVendor2 extends HttpServlet
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
    public ProcessReclassVendor2()
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
        String page1_old = "ASSET RECLASSIFICATION RAISE ENTRY";
        String page1 = "ASSET RECLASSIFICATION PAYMENT ENTRY";
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

        	
        String vendordebitAccount = request.getParameter("vendor_dr_account2");
        String auisitionDRAcctNo = vendordebitAccount.substring(8,16);
        String vendordebitAccType = request.getParameter("vendor_dr_accttype2");
        String vendordebitTranCode = request.getParameter("vendor_dr_trancode2");
        String vendordebitNarration = request.getParameter("vendor_dr_narration2");
        String vendorcreditAccount = request.getParameter("vendor_cr_account2");
        String vendorcreditAccType = request.getParameter("vendor_cr_accttype2");
        String vendorcreditTranCode = request.getParameter("vendor_cr_trancode2");
        String vendorcreditNarration = request.getParameter("vendor_cr_narration2");
        String vendoramount = request.getParameter("vendor_amount2");
        String supervisor = request.getParameter("supervisor");
        String type="Asset Creation";
        String transactionId = request.getParameter("transactionIdVendor2");
        String tranId = request.getParameter("tranId");
        String SbuCode = request.getParameter("SbuCode");
        String location = request.getParameter("location");
        String projectCode = request.getParameter("projectCode");
        String vendorId = request.getParameter("vendorId");
        String transactionNarration = request.getParameter("vendor_cr_narration2");
        String recType = request.getParameter("recType");
        
          String systemIp =request.getRemoteAddr();
          String macAddress= "";
          String aquisitiondebitAcctName = records.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+auisitionDRAcctNo+"'");
          String vendorAcctName = records.getCodeName("select vendor_name from am_ad_vendor where account_number = '"+vendorcreditAccount+"'");

          int assetCode = request.getParameter("assetCode") == null?0:Integer.parseInt(request.getParameter("assetCode"));
        String finacleTransId = records.getGeneratedTransId();
          try
        {
        if(vendoramount!=null)
        {
        vendoramount = vendoramount.replaceAll(",","");
        if (supervisor == "" || supervisor == null || supervisor == "*" || supervisor.equals("*")) {
   		 supervisor = "";
        }
       if(ad.isoChecking(   id, page1, transactionId,tranId))
       {
    	   iso=bus.transferFund(vendordebitNarration,vendorcreditAccount,vendordebitAccount,Double.parseDouble(vendoramount),finacleTransId,SbuCode);
    	   ad.updateRaiseEntryTransaction(   id, page1, transactionId, iso,systemIp,tranId,finacleTransId,"","");
       }
       else
       {
        raiseMan.raiseEntry(vendordebitAccount,vendorcreditAccount,vendordebitAccType,vendorcreditAccType,vendordebitTranCode
        		,vendorcreditTranCode,vendordebitNarration,
        		vendorcreditNarration,Double.parseDouble(vendoramount), userId,batchId,cdate,supervisor,id,type);
        		
         iso=bus.transferFund(vendordebitNarration,vendorcreditAccount,vendordebitAccount,Double.parseDouble(vendoramount),finacleTransId,SbuCode);
         ad.insertRaiseEntryTransaction(userId,vendordebitNarration,vendorcreditAccount,vendordebitAccount,Double.parseDouble(vendoramount),iso,id,page1,transactionId,systemIp,tranId,assetCode,finacleTransId,aquisitiondebitAcctName,vendorAcctName,userId,recType);
       }
         if(iso.equalsIgnoreCase("000"))
             {
        	 ad.insertVendorTransaction(userId,transactionNarration,vendordebitAccount,vendorcreditAccount,Double.parseDouble(vendoramount),location,transactionId,page1,projectCode,vendorId);
                    System.out.println("#####################################################################  2");
         			msgText1 = message.MailMessage(mail_code, transaction_type)+", \n";
        			msgText1 += "For asset id  "+id+"\n";
                          msgText1 += "For  "+vendorcreditNarration +"\n";
        			msgText1 += "Debit Account  "+vendordebitAccount+"\n";
        			msgText1 += "Credit Account  "+vendorcreditAccount+"\n";
        			msgText1 += "Amount  "+formata.formatAmount(vendoramount)+"\n";
        			msgText1 += "Login here http://172.27.13.113:8080/legend2.net" ;
          			//ret = bulkMail.sendMail(signinname,transaction_type,msgText1,directory,to);
          			//System.out.println("Output of the mail is >>>>"+ret);
        			mail.sendMail(to,subject,msgText1);
        			
            }
// 	    to display transaction status
		    String status =  records.getCodeName("select description from am_error_description where error_code='"+iso+"' ");
		    out.print("<script>alert('"+status+"');</script>");
           out.print("<script>window.close('reclassAssetDetail2.jsp');</script>");
         //  session.setAttribute("assetid", id);
    	 //  session.setAttribute("page1", page1);
    	 //  out.print("<script>window.open('raiseEntryList.jsp','raiseasset4','width=850,height=350,scrollbars=Yes,resizable=Yes,align=center');</script>");

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