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

import magma.asset.manager.AssetManager;

public class ProcessReclassCost extends HttpServlet
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
          private AssetManager asm;
    public ProcessReclassCost()
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
          asm = new AssetManager();
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
        Post Cost Transaction
        */
        String costdebitAccount = request.getParameter("cost_dr_account");
        String costdebitAccType = request.getParameter("cost_dr_accttype");
        String costdebitTranCode = request.getParameter("cost_dr_trancode");
        String costdebitNarration = request.getParameter("cost_dr_narration");
        String costcreditAccount = request.getParameter("cost_cr_account");
        String costcreditAccType = request.getParameter("cost_cr_accttype");
        String costcreditTranCode = request.getParameter("cost_cr_trancode");
        String costcreditNarration = request.getParameter("cost_cr_narration");
        String costamount = request.getParameter("cost_amount");
        costamount = costamount.replaceAll(",","");
        String supervisor = request.getParameter("supervisor");
        String pid = request.getParameter("pid");
        String transactionId = request.getParameter("transactionIdCost");
        String tranId = request.getParameter("tranId");
        String SbuCode = request.getParameter("SbuCode");
        String recType = request.getParameter("recType");
//        String costdebitAcctNo = costdebitAccount.substring(8,16);
//        String costcreditAcctNo = costcreditAccount.substring(8,16);
        String costdebitAcctNo = costdebitAccount;
        String costcreditAcctNo = costcreditAccount;	
        if(costdebitAcctNo.substring(0,2)=="NGN"){costdebitAcctNo = costdebitAcctNo.substring(8,16);}
        if(costcreditAcctNo.substring(2,5)=="NGN"){costcreditAcctNo = costcreditAcctNo.substring(6,14);}
        String systemIp =request.getRemoteAddr();
        String macAddress= "";
        String raiseEntryNarration = costdebitNarration;
     //   String sbu_code = SbuCode+ "|" +SbuCode;
        String sbu_code = SbuCode+SbuCode;
        costdebitNarration = SbuCode+"|"+SbuCode+"|"+costdebitNarration;
        int assetCode = request.getParameter("assetCode") == null?0:Integer.parseInt(request.getParameter("assetCode"));
        String costdebitAcctName = records.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+costdebitAcctNo+"'");
        String costcreditAcctName = records.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+costcreditAcctNo+"'");

String finacleTransId = records.getGeneratedTransId();
        try{   
        	if (!userClass.equals("NULL") || userClass!=null){
        if(ad.isoChecking(   id, page1, transactionId,tranId))
        {
        	iso=bus.transferFund(costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),finacleTransId,SbuCode);
        	//iso=bus.interaccounttransferFund(costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),finacleTransId,SbuCode,SbuCode);
        	ad.updateRaiseEntryTransaction(   id, page1, transactionId,iso,  systemIp,tranId,finacleTransId,"","");
        }
        else
        {
        raiseMan.raiseEntry(costdebitAccount,costcreditAccount,costdebitAccType,costcreditAccType,costdebitTranCode
        		,costcreditTranCode,costdebitNarration,
        		costcreditNarration,Double.parseDouble(costamount), userId,batchId,cdate,supervisor,pid,"ASSET PAYMENT");
        		
        iso=bus.transferFund(costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),finacleTransId,SbuCode);
       // iso=bus.interaccounttransferFund(costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),finacleTransId,SbuCode,SbuCode);
           //out.print("<script>alert('"+iso+"');window.close();</script>");
           ad.insertRaiseEntryTransaction(userId,raiseEntryNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),iso,id,page1,transactionId,systemIp,tranId,assetCode,finacleTransId,costdebitAcctName,costcreditAcctName,userId,recType);
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
        	            msgText1 += "For  "+costdebitNarration+"\n";
        			msgText1 += "Debit Account  "+costdebitAccount+"\n";
        			msgText1 += "Credit Account  "+costcreditAccount+"\n";
        			msgText1 += "Amount  "+formata.formatAmount(costamount)+"\n";
        			msgText1 += "Login here http://172.27.13.113:8080/legend2.net" ;
          			//ret = bulkMail.sendMail(signinname,transaction_type,msgText1,directory,to);
          			//System.out.println("Output of the mail is >>>>"+ret);
        			mail.sendMail(to,subject,msgText1);
                                 String AssetCode= String.valueOf(assetCode);
        	                asm.updateAssetReclass2(AssetCode); 

            }
//	    to display transaction status
	    String status =  records.getCodeName("select description from am_error_description where error_code='"+iso+"' ");
	    out.print("<script>alert('"+status+"');</script>");
           out.print("<script>window.close('reclassAssetDetail2.jsp');</script>");
         //  session.setAttribute("assetid", id);
    	 //  session.setAttribute("page1", page1);
    	 //  out.print("<script>window.open('raiseEntryList.jsp','raiseasset4','width=850,height=350,scrollbars=Yes,resizable=Yes,align=center');</script>");
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