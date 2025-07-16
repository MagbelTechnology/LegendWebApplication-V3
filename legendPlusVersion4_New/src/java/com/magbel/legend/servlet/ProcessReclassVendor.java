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

public class ProcessReclassVendor extends HttpServlet
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
    public ProcessReclassVendor()
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

        	
        String vendordebitAccount = request.getParameter("vendor_dr_account");
//        String vendorDRAcctNo = vendordebitAccount.substring(8,16);
        String vendorDRAcctNo = vendordebitAccount;
        if(vendordebitAccount.substring(0,2)=="NGN"){vendorDRAcctNo = vendordebitAccount.substring(8,16);}
        if(vendordebitAccount.substring(2,5)=="NGN"){vendorDRAcctNo = vendordebitAccount.substring(6,14);}
        String vendordebitAccType = request.getParameter("vendor_dr_accttype");
        String vendordebitTranCode = request.getParameter("vendor_dr_trancode");
        String vendordebitNarration = request.getParameter("vendor_dr_narration");
        String vendorcreditAccount = request.getParameter("vendor_cr_account");
        String vendorcreditAccType = request.getParameter("vendor_cr_accttype");
        String vendorcreditTranCode = request.getParameter("vendor_cr_trancode");
        String vendorcreditNarration = request.getParameter("vendor_cr_narration");
        String vendoramount = request.getParameter("vendor_amount");
//        String vendorcreditAcctNo = vendorcreditAccount.substring(8,16);
        String vendorcreditAcctNo = vendorcreditAccount;
        if(vendorcreditAcctNo.substring(0,2)=="NGN"){vendorcreditAcctNo = vendorcreditAcctNo.substring(8,16);}
        if(vendorcreditAcctNo.substring(2,5)=="NGN"){vendorcreditAcctNo = vendorcreditAcctNo.substring(6,14);}
        String supervisor = request.getParameter("supervisor");
        String type="Asset Creation";
        String transactionId = request.getParameter("transactionIdVendor");
         String tranId = request.getParameter("tranId");
         String raiseEntryNarration = vendordebitNarration;
         String SbuCode = request.getParameter("SbuCode");
         String recType = request.getParameter("recType");
         vendordebitNarration = SbuCode+"|"+SbuCode+"|"+vendordebitNarration;
          String systemIp =request.getRemoteAddr();
        String macAddress= "";
        int assetCode = request.getParameter("assetCode") == null?0:Integer.parseInt(request.getParameter("assetCode"));
        String finacleTransId = records.getGeneratedTransId();
	//	String sbu_code = SbuCode+ "|" +SbuCode;
	    String sbu_code = SbuCode+SbuCode;
        String costdebitAcctName = records.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+vendorDRAcctNo+"'");
        String costcreditAcctName = records.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+vendorcreditAcctNo+"'");
        try
        {
        if (!userClass.equals("NULL") || userClass!=null){
        if(vendoramount!=null)
        {
        vendoramount = vendoramount.replaceAll(",","");
        if (supervisor == "" || supervisor == null || supervisor == "*" || supervisor.equals("*")) {
   		 supervisor = "";
        }
       if(ad.isoChecking(   id, page1, transactionId,tranId))
       {
    	   iso=bus.transferFund(vendordebitNarration,vendorcreditAccount,vendordebitAccount,Double.parseDouble(vendoramount),finacleTransId,SbuCode);
    	   //iso=bus.interaccounttransferFund(vendordebitNarration,vendorcreditAccount,vendordebitAccount,Double.parseDouble(vendoramount),finacleTransId,SbuCode,SbuCode);
    	   ad.updateRaiseEntryTransaction(   id, page1, transactionId,  iso,systemIp,tranId,finacleTransId,"","");
       }
       else
       {
        raiseMan.raiseEntry(vendordebitAccount,vendorcreditAccount,vendordebitAccType,vendorcreditAccType,vendordebitTranCode
        		,vendorcreditTranCode,vendordebitNarration,
        		vendorcreditNarration,Double.parseDouble(vendoramount), userId,batchId,cdate,supervisor,id,type);
        		
         iso=bus.transferFund(vendordebitNarration,vendorcreditAccount,vendordebitAccount,Double.parseDouble(vendoramount),finacleTransId,SbuCode);
        // iso=bus.interaccounttransferFund(vendordebitNarration,vendorcreditAccount,vendordebitAccount,Double.parseDouble(vendoramount),finacleTransId,SbuCode,SbuCode);
         ad.insertRaiseEntryTransaction(userId,raiseEntryNarration,vendorcreditAccount,vendordebitAccount,Double.parseDouble(vendoramount),iso,id,page1,transactionId,systemIp,tranId,assetCode,finacleTransId,costdebitAcctName,costcreditAcctName,userId,recType);
       }
         if(iso.equalsIgnoreCase("000"))
             {
     	   	 String reversalId = tranId.substring(0, 1);
     	     if(reversalId.equals("R")){tranId = tranId.substring(1, tranId.length());}
        	     if(!reversalId.equals("R")){tranId = "R"+tranId;}
                 String q = "update am_Raisentry_Transaction set iso = '-001' where asset_id='"+id+"' and Trans_id = '"+tranId+"' and transactionId = "+transactionId+"";
                 ad.updateAssetStatusChange(q);	      
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