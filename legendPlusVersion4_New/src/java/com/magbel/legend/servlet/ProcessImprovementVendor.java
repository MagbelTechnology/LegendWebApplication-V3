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

public class ProcessImprovementVendor extends HttpServlet
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
    public ProcessImprovementVendor()
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
  //      String page1 = "ASSET IMPROVEMENT RAISE ENTRY";
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
        String tranIdInt = request.getParameter("tranIdInt");
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
        String vendorDrAcct = request.getParameter("vendor_dr_account");
        if(vendorDrAcct==null){vendorDrAcct="";}
        String page1 = request.getParameter("page1");
        System.out.println("<<<<<<a======vendorDrAcct: "+vendorDrAcct);
        //String vatdebitAcctNo = vtDrAcct.substring(8,16);
        String vatdebitAcctNo = vendorDrAcct;
        //String vatdebitAcctNo = vatCrAcct.substring(8,16);
        String vatCrAcct = request.getParameter("vendor_cr_account");
        if(vatCrAcct==null){vatCrAcct="";}
        System.out.println("<<<<<<a======vatCrAcct: "+vatCrAcct);
    //    String vatcreditAcctNo = vatCrAcct.substring(8,16);
        String vatcreditAcctNo = vatCrAcct;
        String vatDrAcctType = request.getParameter("vendor_dr_accttype");
        if(vatDrAcctType==null){vatDrAcctType="";}
        String vatCrAcctType = request.getParameter("vendor_cr_accttype");
        if(vatCrAcctType==null){vatCrAcctType="";}
        String vatDrTranCode = request.getParameter("vendor_dr_trancode");
        if(vatDrTranCode==null){vatDrTranCode="";}
        String vatCrTranCode = request.getParameter("vendor_cr_trancode");
        if(vatCrTranCode==null){vatCrTranCode="";}
        String vatDrNarration = request.getParameter("vendor_dr_narration");
        if(vatDrNarration==null){vatDrNarration="";}
        String vatCrNarration = request.getParameter("vendor_cr_narration");
        if(vatCrNarration==null){vatCrNarration="";}
        String supervisor = request.getParameter("supervisor");
        String transactionId = request.getParameter("transactionIdVendor");
        String transactionNarration = request.getParameter("vendor_cr_narration");
        String vatAmt = request.getParameter("vendorAmt");
        vatAmt = vatAmt.replaceAll(",","");
        String type="Asset Improvement";
        String SbuCode = request.getParameter("SbuCode");
        String oldSbuCode = request.getParameter("SbuCode");
        String InvNo = request.getParameter("InvNo");
        String vendorId = request.getParameter("vendor_Id");
        String vendordesc = request.getParameter("vendordesc");
        String vendorName = request.getParameter("vendorName");
        String location = request.getParameter("location");
        String projectCode = request.getParameter("projectCode");

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
         String finacleTransId = records.getGeneratedTransId();
        String Subheading = "This is to kindly inform you of the payment that has been credited into your account for execution of the job commissioned by FCMB to your company.";
 		String summary1 = "For clarification on the above transaction, kindly send an email to Adminpayment@firstcitygroup.com or call  07098000779.";
 		String summary2 = "Thank you.";
 		String summary3 = "For: First City Monument Bank Plc"; 
 		String raiseEntryNarration = vatDrNarration;
 		//String sbu_code = SbuCode+ "|" +oldSbuCode;
 		String sbu_code = SbuCode+oldSbuCode;
 		vatDrNarration = SbuCode+"|"+oldSbuCode+"|"+vatDrNarration;
        String vatdebitAcctName = records.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+vatdebitAcctNo+"'");
        String vatcreditAcctName = records.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+vatcreditAcctNo+"'");
        System.out.println("=========vatCrAcct: "+vatCrAcct+"  vendorDrAcct: "+vendorDrAcct);
        if(vatCrAcct.substring(0,3).equalsIgnoreCase("NGN")){vatcreditAcctNo = vatCrAcct.substring(8,16);}
        if(vatCrAcct.substring(2,5).equalsIgnoreCase("NGN")){vatcreditAcctNo = vatCrAcct.substring(6,14);}
        if(vendorDrAcct.substring(0,3).equalsIgnoreCase("NGN")){vatdebitAcctNo = vendorDrAcct.substring(8,16);} 
        if(vendorDrAcct.substring(2,5).equalsIgnoreCase("NGN")){vatdebitAcctNo = vendorDrAcct.substring(6,14);}
	       String [] idSplit = id.split("/");
//	       System.out.println("=========vatdebitAcctNo: "+vatdebitAcctNo+"     vatcreditAcctNo: "+vatcreditAcctNo);
	      String test10 = "select category_name from am_ad_category where Asset_Ledger = '"+vatcreditAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
	      String test11 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+vatcreditAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
	      String test12 = "select category_name from am_ad_category where DEP_LEDGER = '"+vatcreditAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
	      String test13 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+vatcreditAcctNo+"' ";
	      String test14 = "select asset_acq_ac from am_gb_company where asset_acq_ac = '"+vatcreditAcctNo+"' ";
	      vatcreditAcctName = records.getCodeName(test10);
	      if(vatcreditAcctName.equalsIgnoreCase("")){vatcreditAcctName = records.getCodeName(test11);}
	      if(vatcreditAcctName.equalsIgnoreCase("")){vatcreditAcctName = records.getCodeName(test12);}
	      if(vatcreditAcctName.equalsIgnoreCase("")){vatcreditAcctName = records.getCodeName(test13);}
	      if(vatcreditAcctName.equalsIgnoreCase("") && !records.getCodeName(test14).equalsIgnoreCase("")){vatcreditAcctName = "Asset Acqusition Suspense Account";}
//	      System.out.println("=========test10: "+test10);
//	      System.out.println("=========test11: "+test11);
//	      System.out.println("=========test12: "+test12); 
//	      System.out.println("=========test13: "+test13);
//	      System.out.println("=========vatcreditAcctName: "+vatcreditAcctName);
	      String test20 = "select category_name from am_ad_category where Asset_Ledger = '"+vatdebitAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
	      String test21 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+vatdebitAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
	      String test22 = "select category_name from am_ad_category where DEP_LEDGER = '"+vatdebitAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
	      String test23 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+vatdebitAcctNo+"' ";
	      String test24 = "select asset_acq_ac from am_gb_company where asset_acq_ac = '"+vatdebitAcctNo+"' ";
	      vatdebitAcctName = records.getCodeName(test20);
       if(vatdebitAcctName.equalsIgnoreCase("")){vatdebitAcctName = records.getCodeName(test21);}
       if(vatdebitAcctName.equalsIgnoreCase("")){vatdebitAcctName = records.getCodeName(test22);}
       if(vatdebitAcctName.equalsIgnoreCase("")){vatdebitAcctName = records.getCodeName(test23);} 	
       if(vatdebitAcctName.equalsIgnoreCase("") && !records.getCodeName(test24).equalsIgnoreCase("")){vatdebitAcctName = "Asset Acqusition Suspense Account";}
//	      System.out.println("=========test20: "+test20);
//	      System.out.println("=========test21: "+test21);
//	      System.out.println("=========test22: "+test22);
//	      System.out.println("=========test23: "+test23);
//	      System.out.println("=========vatdebitAcctName: "+vatdebitAcctName+"      userClass: "+userClass);
   try
        {
	   if (!userClass.equals("NULL") || userClass!=null){
        if (supervisor == "" || supervisor == null || supervisor == "*" || supervisor.equals("*")) {
   		 supervisor = "";
        }
       if(ad.isoChecking(   id, page1, transactionId,tranId))
       {
    	   iso=bus.transferFund(vatDrNarration,vatCrAcct,vendorDrAcct,Double.parseDouble(vatAmt),finacleTransId,SbuCode);
    	   //iso=bus.interaccounttransferFund(vatDrNarration,vatCrAcct,vatCrAcct,Double.parseDouble(vatAmt),finacleTransId,SbuCode,oldSbuCode);
    	   ad.updateRaiseEntryTransaction(   id, page1, transactionId, iso,systemIp,tranId,finacleTransId,"","");
       }
       else   
       {
        raiseMan.raiseEntry(vendorDrAcct,vatCrAcct,vatDrAcctType,vatCrAcctType,vatDrTranCode
        		,vatCrTranCode,vatDrNarration,
        		vatCrNarration,Double.parseDouble(vatAmt), userId,batchId,cdate,supervisor,id,type);
        		
        iso=bus.transferFund(vatDrNarration,vatCrAcct,vendorDrAcct,Double.parseDouble(vatAmt),finacleTransId,SbuCode);
        //iso=bus.interaccounttransferFund(vatDrNarration,vatCrAcct,vatCrAcct,Double.parseDouble(vatAmt),finacleTransId,SbuCode,oldSbuCode);
         ad.insertRaiseEntryTransaction (userId,raiseEntryNarration,vendorDrAcct,vatCrAcct,Double.parseDouble(vatAmt),iso,id,page1,transactionId,systemIp,tranId,assetCode,finacleTransId,vatcreditAcctName,vatdebitAcctName,userId,recType);
       }
         if(iso.equalsIgnoreCase("000"))
             {  
    	   	 String reversalId = tranId.substring(0, 1);
    	     if(reversalId.equals("R")){tranId = tranId.substring(1, tranId.length());}
       	     if(!reversalId.equals("R")){tranId = "R"+tranId;}
                String q = "update am_Raisentry_Transaction set iso = '-001' where asset_id='"+id+"' and Trans_id = '"+tranId+"' and transactionId = "+transactionId+"";
                ad.updateAssetStatusChange(q);	           	 
       	  String  improveStatus = records.getCodeName("SELECT IMPROVED FROM am_asset_improvement WHERE revalue_Id = '"+tranIdInt+"'"); 
       	  String test = "select count(*) from am_Raisentry_Transaction where Trans_id='"+tranId+"' and page1 = 'ASSET IMPROVEMENT RAISE ENTRY' and iso = '000' ";
       	  String isoValue=records.getCodeName(test);
       	  if(isoValue.equalsIgnoreCase("3") && improveStatus.equalsIgnoreCase("Y"))
       	  {
       	  assetMan.processImprovement(id,CalcCost,CalcNBV,Calcvatable,CalcVatAmt,CalcWhtAmt,Integer.parseInt(usefullife),costValue,vatableCost,nbvresidual,Integer.parseInt(tranId));
       	  }        	 
        	 ad.insertVendorTransaction(userId,transactionNarration,vendorDrAcct,vatCrAcct,Double.parseDouble(vatAmt),location,transactionId,page1,projectCode,vendorId);
                    System.out.println("#####################################################################  2");
         /*			msgText1 = message.MailMessage(mail_code, transaction_type)+", \n";
        			msgText1 += "For asset id  "+id+"\n";
                          msgText1 += "For  "+vatDrNarration +"\n";
        			msgText1 += "Debit Account  "+vatCrAcct+"\n";
        			msgText1 += "Credit Account  "+vatCrAcct+"\n";
        			msgText1 += "Amount  "+formata.formatAmount(vatAmt)+"\n";
        			msgText1 += "Login here http://172.27.13.113:8080/legend2.net" ;
          			//ret = bulkMail.sendMail(signinname,transaction_type,msgText1,directory,to);
          			//System.out.println("Output of the mail is >>>>"+ret);
        			*/
                    msgText1 = "CREDIT ADVICE \n";
                    msgText1 += ""+Subheading+"\n";
         			msgText1 += message.MailMessage(mail_code, transaction_type)+" \n";        			
        			msgText1 += "VENDOR NAME "+vendorName+"\n";
        			msgText1 += "ACCOUNT NUMBER  "+vatCrAcct+"\n";
        			msgText1 += "INVOICE NO  "+InvNo+"\n";
        			msgText1 += "AMOUNT  "+formata.formatAmount(Double.parseDouble(vatAmt))+"\n";
                    msgText1 += "NARRATION  "+vatDrNarration +"\n";
                    msgText1 += "PAYMENT ID  "+id+"\n";        			        		        			
        			//msgText1 += "TRANSACTION DATE  "+newdate+"\n";
        			msgText1 += ""+summary1+"\n";
        			msgText1 += ""+summary2+"\n";
        			msgText1 += ""+summary3+"\n"; 	                      
        			mail.sendMail(to,subject,msgText1);
        			
            }	
// 	    to display transaction status
		    String status =  records.getCodeName("select description from am_error_description where error_code='"+iso+"' ");
		    out.print("<script>alert('"+status+"');</script>");
           out.print("<script>window.close('raiseAssetDetailAdvancePayment.jsp');</script>");
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