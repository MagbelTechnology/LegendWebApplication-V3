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

public class ProcessImprovementCost extends HttpServlet
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
	private AssetManager assetMan;
    public ProcessImprovementCost()
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
        assetMan = new AssetManager();
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
      //  String page1 = "ASSET IMPROVEMENT RAISE ENTRY";
        String iso;
        String msgText1 ="";
        String to ="";
        String mail_code="1";
        String subject = "Raise Entry";
        String transaction_type="Raise_Entry";
        String costdebitAcctName = "";
        String costcreditAcctName = "";
        
        String batchId = "RE"+Integer.toString((int)(Math.random()*8000)+1000);
        String cdate = sdf.format(new Date());
        /**
        Post Cost Transaction
        */
        String tranId = request.getParameter("tranId");
        String systemIp =request.getRemoteAddr();
        String macAddress= "";
        
        int assetCode = request.getParameter("assetCode") == null?0:Integer.parseInt(request.getParameter("assetCode"));
        String page1 = request.getParameter("page1");
        String costDrAcct = request.getParameter("costDrAcct");
        if(costDrAcct==null){costDrAcct="";}
 //       String costdebitAcctNo = costDrAcct.substring(8,16);
        String costdebitAcctNo = costDrAcct;
        String costCrAcct = request.getParameter("costCrAcct");
        if(costCrAcct==null){costCrAcct="";}
 //       String costcreditAcctNo = costCrAcct.substring(8,16);
        String costcreditAcctNo = costCrAcct;
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
        String oldSbuCode = request.getParameter("SbuCode");
        String integrifyid = request.getParameter("integrifyid");

    	double CalcCost = Double.parseDouble(request.getParameter("CalcCost"));
    	double CalcNBV = Double.parseDouble(request.getParameter("CalcNBV"));
    	double Calcvatable = Double.parseDouble(request.getParameter("Calcvatable"));
    	double CalcVatAmt = Double.parseDouble(request.getParameter("CalcVatAmt"));
    	double CalcWhtAmt = Double.parseDouble(request.getParameter("CalcWhtAmt"));
    	double costValue = Double.parseDouble(request.getParameter("costValue"));
    	double vatableCost = Double.parseDouble(request.getParameter("vatableCost"));
    	String nbvresidual = request.getParameter("nbvresidual");
    	String usefullife = request.getParameter("usefullife");
    			
      //  String sbu_code = SbuCode+ "|" +oldSbuCode;    
        String raiseEntryNarration = costDrNarration;
        String sbu_code = SbuCode+ oldSbuCode;
        cost = cost.replaceAll(",","");
        String type="Asset Improvement";
         String finacleTransId = records.getGeneratedTransId();
         costDrNarration = SbuCode+"|"+oldSbuCode+"|"+costDrNarration;
//         String costdebitAcctName = records.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+costdebitAcctNo+"'");
//         String costcreditAcctName = records.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+costcreditAcctNo+"'");

         if(costDrAcct.substring(0,3).equalsIgnoreCase("NGN")){costdebitAcctNo = costDrAcct.substring(8,16);}
         if(costDrAcct.substring(2,5).equalsIgnoreCase("NGN")){costdebitAcctNo = costDrAcct.substring(6,14);}
         if(costCrAcct.substring(0,3).equalsIgnoreCase("NGN")){costcreditAcctNo = costCrAcct.substring(8,16);} 
         if(costCrAcct.substring(2,5).equalsIgnoreCase("NGN")){costcreditAcctNo = costCrAcct.substring(6,14);}
	       String [] idSplit = id.split("/");
	      String test10 = "select category_name from am_ad_category where Asset_Ledger = '"+costdebitAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
	      String test11 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+costdebitAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
	      String test12 = "select category_name from am_ad_category where DEP_LEDGER = '"+costdebitAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
	      String test13 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+costdebitAcctNo+"' ";
	      String test14 = "select asset_acq_ac from am_gb_company where asset_acq_ac = '"+costdebitAcctNo+"' ";
	      costdebitAcctName = records.getCodeName(test10);
	      if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = records.getCodeName(test11);}
	      if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = records.getCodeName(test12);}
	      if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = records.getCodeName(test13);}
//	      if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = records.getCodeName(test14);}
	      if(costdebitAcctName.equalsIgnoreCase("") && !records.getCodeName(test14).equalsIgnoreCase("")){costdebitAcctName = "Asset Acqusition Suspense Account";}
//	      System.out.println("=========test10: "+test10);
//	      System.out.println("=========test11: "+test11);
//	      System.out.println("=========test12: "+test12);
//	      System.out.println("=========test13: "+test13);
//	      System.out.println("=========costdebitAcctName: "+costdebitAcctName);
	      String test20 = "select category_name from am_ad_category where Asset_Ledger = '"+costcreditAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
	      String test21 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+costcreditAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
	      String test22 = "select category_name from am_ad_category where DEP_LEDGER = '"+costcreditAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
	      String test23 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+costcreditAcctNo+"' ";
	      String test24 = "select asset_acq_ac from am_gb_company where asset_acq_ac = '"+costcreditAcctNo+"' ";
//	      System.out.println("=========test20: "+test20);
//	      System.out.println("=========test21: "+test21);
//	      System.out.println("=========test22: "+test22);
//	      System.out.println("=========test23: "+test23);
//	      System.out.println("=========test24: "+test24);
        costcreditAcctName = records.getCodeName(test20);
        if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = records.getCodeName(test21);}
        if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = records.getCodeName(test22);}
        if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = records.getCodeName(test23);} 	
        if(records.getCodeName(test24)!="" && costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = "Asset Acquisition Suspense Account";}
//        System.out.println("=========costcreditAcctName: "+costcreditAcctName);
        try
        {
        	if (!userClass.equals("NULL") || userClass!=null){
        	 if (supervisor == "" || supervisor == null || supervisor == "*" || supervisor.equals("*")) {
        		 supervisor = "";
             } 
        	if(ad.isoChecking( id,page1,transactionId,tranId))
        	{
//        		  System.out.println("#####################################################################  1");
        		iso=bus.transferFund(costDrNarration,costDrAcct,costCrAcct,Double.parseDouble(cost),finacleTransId,SbuCode);
        		//iso=bus.interaccounttransferFund(costDrNarration,costDrAcct,costCrAcct,Double.parseDouble(cost),finacleTransId,SbuCode,oldSbuCode);
//        		  System.out.println("#####################################################################  2");
        		ad.updateRaiseEntryTransaction(  id, page1, transactionId, iso,systemIp,tranId,finacleTransId,"","");
//        		  System.out.println("#####################################################################  3");
        	}
        	else
        	{  
//        		  System.out.println("#####################################################################  4");
               raiseMan.raiseEntry(costDrAcct,costCrAcct,costDrAcctType,costCrAcctType,costDrTranCode
        		,costCrTranCode,costDrNarration,
        		costCrNarration,Double.parseDouble(cost), userId,batchId,cdate,supervisor,id,type);
        		
//               System.out.println("#####################################################################  5");

               iso=bus.transferFund(costDrNarration,costDrAcct,costCrAcct,Double.parseDouble(cost),finacleTransId,SbuCode);
               //iso=bus.interaccounttransferFund(costDrNarration,costDrAcct,costCrAcct,Double.parseDouble(cost),finacleTransId,SbuCode,oldSbuCode);
//               System.out.println("#####################################################################  6");
              //ad.insertRaiseEntryTransactionTranId(userId,costDrNarration,costDrAcct,costCrAcct,Double.parseDouble(cost),iso,id,page1,transactionId,tranId);
               ad.insertRaiseEntryTransaction (userId,raiseEntryNarration,costCrAcct,costDrAcct,Double.parseDouble(cost),iso,id,page1,transactionId,systemIp,tranId,assetCode,finacleTransId,costcreditAcctName,costdebitAcctName,userId,integrifyid);
//              System.out.println("#####################################################################  7");
        	}
        	
              if(iso.equalsIgnoreCase("000"))
              {
         	   	 String reversalId = tranId.substring(0, 1);
         	     if(reversalId.equals("R")){tranId = tranId.substring(1, tranId.length());}
            	     if(!reversalId.equals("R")){tranId = "R"+tranId;}
                     String q = "update am_Raisentry_Transaction set iso = '-001' where asset_id='"+id+"' and Trans_id = '"+tranId+"' and transactionId = "+transactionId+"";
                     ad.updateAssetStatusChange(q);	               	  
            	  String  improveStatus = records.getCodeName("SELECT IMPROVED FROM am_asset_improvement WHERE revalue_Id = '"+tranId+"'"); 
            	  String test = "select count(*) from am_Raisentry_Transaction where Trans_id='"+tranId+"' and page1 = 'ASSET IMPROVEMENT RAISE ENTRY' and iso = '000' ";
            	  String isoValue=records.getCodeName(test);
            	  if(isoValue.equalsIgnoreCase("3") && improveStatus.equalsIgnoreCase("Y"))
            	  {
            	  assetMan.processImprovement(id,CalcCost,CalcNBV,Calcvatable,CalcVatAmt,CalcWhtAmt,Integer.parseInt(usefullife),costValue,vatableCost,nbvresidual,Integer.parseInt(tranId));
            	  }
//                    System.out.println("#####################################################################  ");
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