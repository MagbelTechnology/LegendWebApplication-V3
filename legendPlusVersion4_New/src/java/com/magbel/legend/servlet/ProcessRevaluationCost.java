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

public class ProcessRevaluationCost extends HttpServlet
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
    public ProcessRevaluationCost()
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
        Post Cost Transaction
        */
        String tranId = request.getParameter("tranId");
        String systemIp =request.getRemoteAddr();
        String macAddress= "";
        
        int assetCode = request.getParameter("assetCode") == null?0:Integer.parseInt(request.getParameter("assetCode"));

        String costDrAcct = request.getParameter("costDrAcct");
        if(costDrAcct==null){costDrAcct="";}
        String costCrAcct = request.getParameter("costCrAcct");
        if(costCrAcct==null){costCrAcct="";}
        String costdebitAcctNo = costDrAcct.substring(8,16);
        String costcreditAcctNo = costCrAcct.substring(8,16);
        String costDrAcctType = request.getParameter("costDrAcctType");
        if(costDrAcctType==null){costDrAcctType="";}
        String costCrAcctType = request.getParameter("costCrAcctType");
        if(costCrAcctType==null){costCrAcctType="";}
        String costDrTranCode = request.getParameter("costDrTranCode");
        if(costDrTranCode==null){costDrTranCode="";}
        String costCrTranCode = request.getParameter("costCrTranCode");
        if(costCrTranCode==null){costCrTranCode="";}
        String costDrNarration = request.getParameter("costDrNarration");
        if(costDrNarration==null){costDrNarration="";}
        String costCrNarration = request.getParameter("costCrNarration");
        if(costCrNarration==null){costCrNarration="";}
        String supervisor = request.getParameter("supervisor");
        String transactionId = request.getParameter("transactionIdCost");
        String cost = request.getParameter("cost");
        String SbuCode = request.getParameter("SbuCode");
        String recType = request.getParameter("recType");
        cost = cost.replaceAll(",","");
        String type="Asset Revaluation";
         String finacleTransId = records.getGeneratedTransId();
         String raiseEntryNarration = costDrNarration;
         //String sbu_code = SbuCode+ "|" +SbuCode;
         String sbu_code = SbuCode+SbuCode;
         costDrNarration = SbuCode+"|"+SbuCode+"|"+costDrNarration;
         String costdebitAcctName = records.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+costdebitAcctNo+"'");
         String costcreditAcctName = records.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+costcreditAcctNo+"'");
        try
        {
        	if (!userClass.equals("NULL") || userClass!=null){
        	 if (supervisor == "" || supervisor == null || supervisor == "*" || supervisor.equals("*")) {
        		 supervisor = "";
             }
        	if(ad.isoChecking( id,page1,transactionId,tranId))
        	{
        	//	  System.out.println("#####################################################################  1");
        		iso=bus.transferFund(costDrNarration,costDrAcct,costCrAcct,Double.parseDouble(cost),finacleTransId,SbuCode);
        		//iso=bus.interaccounttransferFund(costDrNarration,costDrAcct,costCrAcct,Double.parseDouble(cost),finacleTransId,SbuCode,SbuCode);
        	//	  System.out.println("#####################################################################  2");
        		ad.updateRaiseEntryTransaction(  id, page1, transactionId, iso,systemIp,tranId,finacleTransId,"","");
        	//	  System.out.println("#####################################################################  3");
        	}
        	else
        	{
        		//  System.out.println("#####################################################################  4");
               raiseMan.raiseEntry(costDrAcct,costCrAcct,costDrAcctType,costCrAcctType,costDrTranCode
        		,costCrTranCode,costDrNarration,
        		costCrNarration,Double.parseDouble(cost), userId,batchId,cdate,supervisor,id,type);
        		
              // System.out.println("#####################################################################  5");

              iso=bus.transferFund(costDrNarration,costDrAcct,costCrAcct,Double.parseDouble(cost),finacleTransId,SbuCode);
              // iso=bus.interaccounttransferFund(costDrNarration,costDrAcct,costCrAcct,Double.parseDouble(cost),finacleTransId,SbuCode,SbuCode);
            //   System.out.println("#####################################################################  6");
              //ad.insertRaiseEntryTransactionTranId(userId,costDrNarration,costDrAcct,costCrAcct,Double.parseDouble(cost),iso,id,page1,transactionId,tranId);
               ad.insertRaiseEntryTransaction (userId,raiseEntryNarration,costDrAcct,costCrAcct,Double.parseDouble(cost),iso,id,page1,transactionId,systemIp,tranId,assetCode,finacleTransId,costdebitAcctName,costcreditAcctName,userId,recType);
            //  System.out.println("#####################################################################  7");
        	}
              if(iso.equalsIgnoreCase("000"))
              {
          	   	 String reversalId = tranId.substring(0, 1);
          	     if(reversalId.equals("R")){tranId = tranId.substring(1, tranId.length());}
             	     if(!reversalId.equals("R")){tranId = "R"+tranId;}
                      String q = "update am_Raisentry_Transaction set iso = '-001' where asset_id='"+id+"' and Trans_id = '"+tranId+"' and transactionId = "+transactionId+"";
                      ad.updateAssetStatusChange(q);	      
                //    System.out.println("#####################################################################  ");
         			msgText1 = message.MailMessage(mail_code, transaction_type)+", \n";
        			msgText1 += "For asset id  "+id+"\n";
        	            msgText1 += "For  "+costDrNarration+"\n";
        			msgText1 += "Debit Account  "+costDrAcct+"\n";
        			msgText1 += "Credit Account  "+costCrAcct+"\n";
        			msgText1 += "Amount  "+formata.formatAmount(cost)+"\n";
        			msgText1 += "Login here http://172.27.13.113:8080/legend2.net" ;
          			//ret = bulkMail.sendMail(signinname,transaction_type,msgText1,directory,to);
          			//System.out.println("Output of the mail is >>>>"+ret);
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