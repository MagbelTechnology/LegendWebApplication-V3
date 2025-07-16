package com.magbel.legend.servlet;



import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.vao.Approval;

import java.io.*;

import magma.AssetRecordsBean;
import magma.asset.manager.AssetManager;
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

public class ProcessImprovementWitholding extends HttpServlet
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
    public ProcessImprovementWitholding()
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
//        String page1 = "ASSET IMPROVEMENT RAISE ENTRY";
        String iso;
        String msgText1 ="";
        String to ="";
        String mail_code="1";
        String subject = "Raise Entry";
        String transaction_type="Raise_Entry";
        String batchId = "RE"+Integer.toString((int)(Math.random()*8000)+1000);
        String cdate = sdf.format(new Date());
		/**
        Post Wht Transaction
        */
        String page1 = request.getParameter("page1");
         String systemIp =request.getRemoteAddr();
        String macAddress= "";
        String tranId = request.getParameter("tranId");
        
        String whtDrAcct = request.getParameter("whtDrAcct");
        if(whtDrAcct==null){whtDrAcct="";}
        String whtCrAcct = request.getParameter("whtCrAcct");
        if(whtCrAcct==null){whtCrAcct="";}
        String whtDrAcctType = request.getParameter("whtDrAcctType");
        if(whtDrAcctType==null){whtDrAcctType="";}
        String whtCrAcctType = request.getParameter("whtCrAcctType");
        if(whtCrAcctType==null){whtCrAcctType="";}
        String whtDrTranCode = request.getParameter("whtDrTranCode");
        if(whtDrTranCode==null){whtDrTranCode="";}
        String whtCrTranCode = request.getParameter("whtCrTranCode");
        if(whtCrTranCode==null){whtCrTranCode="";}
        String whtDrNarration = request.getParameter("whtDrNarration");
        if(whtDrNarration==null){whtDrNarration="";}
        String whtCrNarration = request.getParameter("whtCrNarration");
        if(whtCrNarration==null){whtCrNarration="";}
        String superId = request.getParameter("superId");
        //String batchId = "RE"+userId+raiseMan.getMaxNum(userId);
        String whtAmt = request.getParameter("whtAmt");
        whtAmt = whtAmt.replaceAll(",","");
        String type="Asset Improvement";
        String supervisor = request.getParameter("supervisor");
        String transactionId = request.getParameter("transactionIdWitholding");
        String SbuCode = request.getParameter("SbuCode");
        String categoryCode = request.getParameter("categoryCode");
        int assetCode = request.getParameter("assetCode") == null?0:Integer.parseInt(request.getParameter("assetCode"));
        
    	double CalcCost = Double.parseDouble(request.getParameter("CalcCost"));
    	double CalcNBV = Double.parseDouble(request.getParameter("CalcNBV"));
    	double Calcvatable = Double.parseDouble(request.getParameter("Calcvatable"));
    	double CalcVatAmt = Double.parseDouble(request.getParameter("CalcVatAmt"));
    	double CalcWhtAmt = Double.parseDouble(request.getParameter("CalcWhtAmt"));
    	double costValue = Double.parseDouble(request.getParameter("costValue"));
    	double vatableCost = Double.parseDouble(request.getParameter("vatableCost"));
    	String nbvresidual = request.getParameter("nbvresidual");
    	String usefullife = request.getParameter("usefullife");
    	String recType = request.getParameter("recType");
    	String subjectTowht = request.getParameter("subjectTowht"); 
    	String finacleTransId = records.getGeneratedTransId();
		//String sbu_code = SbuCode+ "|" +SbuCode;
		String raiseEntryNarration = whtDrNarration;
		String sbu_code = SbuCode+SbuCode;
		whtDrNarration = SbuCode+"|"+SbuCode+"|"+whtDrNarration;

        String vatdebitAcctNo = "";
        String vatcreditAcctNo = "";
        String vatdebitAcctName = "";
        String vatcreditAcctName = "";
        System.out.println("=========whtDrAcct: "+whtDrAcct+"     whtCrAcct: "+whtCrAcct);
        if(whtDrAcct.substring(0,3).equalsIgnoreCase("NGN")){vatdebitAcctNo = whtDrAcct.substring(8,16);}
        if(whtDrAcct.substring(2,5).equalsIgnoreCase("NGN")){vatdebitAcctNo = whtDrAcct.substring(6,14);}
        if(whtCrAcct.substring(0,3).equalsIgnoreCase("NGN")){vatcreditAcctNo = whtCrAcct.substring(8,16);} 
        if(whtCrAcct.substring(2,5).equalsIgnoreCase("NGN")){vatcreditAcctNo = whtCrAcct.substring(6,14);}
//	       String [] idSplit = id.split("/");
//	      String test10 = "select category_name from am_ad_category where Asset_Ledger = '"+vatdebitAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
//	      String test11 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+vatdebitAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
//	      String test12 = "select category_name from am_ad_category where DEP_LEDGER = '"+vatdebitAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
//	      String test13 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+vatdebitAcctNo+"' ";
//	      String test14 = "select asset_acq_ac from am_gb_company where asset_acq_ac = '"+vatcreditAcctNo+"' ";
//	      if(vatcreditAcctName.equalsIgnoreCase("") && !records.getCodeName(test14).equalsIgnoreCase("")){vatcreditAcctName = "Asset Acqusition Suspense Account";}
//	      vatdebitAcctName = records.getCodeName(test10);
//	      if(vatdebitAcctName.equalsIgnoreCase("")){vatdebitAcctName = records.getCodeName(test11);}
//	      if(vatdebitAcctName.equalsIgnoreCase("")){vatdebitAcctName = records.getCodeName(test12);}
//	      if(vatdebitAcctName.equalsIgnoreCase("")){vatdebitAcctName = records.getCodeName(test13);}
//	      
//	      System.out.println("=========test10: "+test10);
//	      System.out.println("=========test11: "+test11);
//	      System.out.println("=========test12: "+test12); 
//	      System.out.println("=========test13: "+test13);
//	      System.out.println("=========vatdebitAcctName: "+vatdebitAcctName);
//	      
//	      String test20 = "select category_name from am_ad_category where Asset_Ledger = '"+vatcreditAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
//	      String test21 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+vatcreditAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
//	      String test22 = "select category_name from am_ad_category where DEP_LEDGER = '"+vatcreditAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
//	      String test23 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+vatcreditAcctNo+"' ";
//	      String test24 = "select asset_acq_ac from am_gb_company where asset_acq_ac = '"+vatdebitAcctNo+"' ";
//	      vatcreditAcctName = records.getCodeName(test20);
//       if(vatcreditAcctName.equalsIgnoreCase("")){vatcreditAcctName = records.getCodeName(test21);}
//       if(vatcreditAcctName.equalsIgnoreCase("")){vatcreditAcctName = records.getCodeName(test22);}
//       if(vatcreditAcctName.equalsIgnoreCase("")){vatcreditAcctName = records.getCodeName(test23);} 
//       
//	      System.out.println("=========test20: "+test20);
//	      System.out.println("=========test21: "+test21);
//	      System.out.println("=========test22: "+test22);
//	      System.out.println("=========test23: "+test23);
//	      System.out.println("=========vatcreditAcctName: "+vatcreditAcctName+"      userClass: "+userClass);
	      
//	  	 taxdebitNarration = SbuCode+"|"+SbuCode+"|"+taxdebitNarration;
	        String costdebitAcctName = records.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+vatdebitAcctNo+"'");
	        String costcreditAcctName = records.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+vatcreditAcctNo+"'");
	   	        System.out.println("ID >>>>>> " + id+"  vatdebitAcctNo: "+vatdebitAcctNo+"   costcreditAcctNo: "+vatcreditAcctNo);
//	        String [] idSplit = id.split("/");
	        String test10 = "select category_name from am_ad_category where Asset_Ledger = '"+vatdebitAcctNo+"' and category_code = '"+categoryCode+"'";
	        String test11 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+vatdebitAcctNo+"' and category_code = '"+categoryCode+"'";
	        String test12 = "select category_name from am_ad_category where DEP_LEDGER = '"+vatdebitAcctNo+"' and category_code = '"+categoryCode+"'";
	        String test13 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+vatdebitAcctNo+"' ";
	        String test14 = "select asset_acq_ac from am_gb_company where asset_acq_ac = '"+vatdebitAcctNo+"' ";
	        String test15 = "";
	        System.out.println("subjectTowht>>>>>> " + subjectTowht);
	        if(subjectTowht.equals("F")){test15 = "select Fed_Wht_Account from am_gb_company where Fed_Wht_Account = '"+vatdebitAcctNo+"' ";}
	        if(subjectTowht.equals("S")){test15 = "select Wht_Account from am_gb_company where Wht_Account = '"+vatdebitAcctNo+"' ";}
	        System.out.println("test15 in Number 3 >>>>>> " + test15);
	        costdebitAcctName = records.getCodeName(test10);
	        if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = records.getCodeName(test11);}
	        if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = records.getCodeName(test12);}
	        if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = records.getCodeName(test13);}  
//		        if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = approv.getCodeName(test14);}
	        if(records.getCodeName(test14)!="" && costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = "Asset Acquisition Suspense Account";}
	        if(records.getCodeName(test15)!="" && costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = "With Holding Tax Account";}
	        System.out.println("costdebitAcctName >>>>>> " + costdebitAcctName);
	        String test20 = "select category_name from am_ad_category where Asset_Ledger = '"+vatcreditAcctNo+"' and category_code = '"+categoryCode+"'";
	        String test21 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+vatcreditAcctNo+"' and category_code = '"+categoryCode+"'";
	        String test22 = "select category_name from am_ad_category where DEP_LEDGER = '"+vatcreditAcctNo+"' and category_code = '"+categoryCode+"'";
	        String test23 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+vatcreditAcctNo+"' ";
	        String test24 = "";
	        if(subjectTowht.equals("F")){test24 = "select Fed_Wht_Account from am_gb_company where Fed_Wht_Account = '"+vatcreditAcctNo+"' ";}
	        if(subjectTowht.equals("S")){test24 = "select Wht_Account from am_gb_company where Wht_Account = '"+vatcreditAcctNo+"' ";}
	        String test25 = "select asset_acq_ac from am_gb_company where asset_acq_ac = '"+vatcreditAcctNo+"' ";
	        System.out.println("test24 in Number 3 >>>>>> " + test24); 
	      costcreditAcctName = records.getCodeName(test20);
	      if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = records.getCodeName(test21);}
	      if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = records.getCodeName(test22);}    
	      if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = records.getCodeName(test23);}
//	      if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test24);}
	      
	      if(records.getCodeName(test24)!="" && costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = "With Holding Tax Account";}
	      if(records.getCodeName(test25)!="" && costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = "Asset Acquisition Suspense Account";}
	      System.out.println("costcreditAcctName >>>>>> " + costcreditAcctName);
	      
        try
        {
        if (!userClass.equals("NULL") || userClass!=null){
        if (supervisor == "" || supervisor == null || supervisor == "*" || supervisor.equals("*")) {
   		 supervisor = "";
        }
        if(ad.isoChecking(   id, page1, transactionId,tranId))
        {
        	 iso=bus.transferFund(whtDrNarration,whtCrAcct,whtDrAcct ,Double.parseDouble(whtAmt),finacleTransId,SbuCode);
        	 //iso=bus.interaccounttransferFund(whtDrNarration,whtDrAcct,whtCrAcct ,Double.parseDouble(whtAmt),finacleTransId,SbuCode,SbuCode);
        	 ad.updateRaiseEntryTransaction2(   id, page1, transactionId, iso,tranId,finacleTransId);
        }
        else
        {
        raiseMan.raiseEntry(whtDrAcct,whtCrAcct,whtDrAcctType,whtCrAcctType,whtDrTranCode
        		,whtCrTranCode,whtDrNarration,
        		whtCrNarration,Double.parseDouble(whtAmt), userId,batchId,cdate,supervisor,id,type);
        		
        iso=bus.transferFund(whtDrNarration,whtCrAcct,whtDrAcct,Double.parseDouble(whtAmt),finacleTransId,SbuCode);
       // iso=bus.interaccounttransferFund(whtDrNarration,whtDrAcct,whtCrAcct ,Double.parseDouble(whtAmt),finacleTransId,SbuCode,SbuCode);
           //out.print("<script>alert('"+iso+"');window.close();</script>");
         System.out.println("#####################################################################>>>>>>>>  3"+iso);
 //        ad.insertRaiseEntryTransactionTranId(userId,raiseEntryNarration,whtDrAcct,whtCrAcct ,Double.parseDouble(whtAmt),iso,id,page1,transactionId,systemIp,tranId,assetCode,finacleTransId);
         ad.insertRaiseEntryTransaction (userId,raiseEntryNarration,whtDrAcct,whtCrAcct,Double.parseDouble(whtAmt),iso,id,page1,transactionId,systemIp,tranId,assetCode,finacleTransId,costdebitAcctName,costcreditAcctName,userId,recType);
        }
         if(iso.equalsIgnoreCase("000"))
             {

		       	  String  improveStatus = records.getCodeName("SELECT IMPROVED FROM am_asset_improvement WHERE revalue_Id = '"+tranId+"'"); 
		       	  String test = "select count(*) from am_Raisentry_Transaction where Trans_id='"+tranId+"' and page1 = 'ASSET IMPROVEMENT RAISE ENTRY' and iso = '000' ";
		       	  String isoValue=records.getCodeName(test);
		       	  if(isoValue.equalsIgnoreCase("3") && improveStatus.equalsIgnoreCase("Y"))
		       	  {
		       	  assetMan.processImprovement(id,CalcCost,CalcNBV,Calcvatable,CalcVatAmt,CalcWhtAmt,Integer.parseInt(usefullife),costValue,vatableCost,nbvresidual,Integer.parseInt(tranId));
		       	  }
                     System.out.println("#####################################################################  3");
         			msgText1 = message.MailMessage(mail_code, transaction_type)+", \n";
        			msgText1 += "For asset id  "+id+"\n";
        			msgText1 += "For  "+whtDrNarration+"\n";
        			msgText1 += "Debit Account  "+whtDrAcct+"\n";
        			msgText1 += "Credit Account  "+whtCrAcct+"\n";
        			msgText1 += "Amount  "+formata.formatAmount(whtAmt)+"\n";
        			msgText1 += "Login here http://172.27.13.113:8080/legend2.net" ;
          			//ret = bulkMail.sendMail(signinname,transaction_type,msgText1,directory,to);
          			//System.out.println("Output of the mail is >>>>"+ret);
        			mail.sendMail(to,subject,msgText1);
        			
               }
// 	    to display transaction status
		    String status =  records.getCodeName("select description from am_error_description where error_code='"+iso+"' ");
		    out.print("<script>alert('"+status+"');</script>");
           out.print("<script>window.close('raiseAssetDetailAdvancePayment.jsp');</script>");
          // session.setAttribute("assetid", id);
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