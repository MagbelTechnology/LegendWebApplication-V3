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

public class ProcessProfit extends HttpServlet
{

	private DepreciationProcessingManager service;
	private FinancialExchangeServiceBus bus;
	private RaiseEntryManager raiseMan;
	private EmailSmsServiceBus mail ;
	private Codes message;
	private AssetRecordsBean ad;
	private ApprovalRecords records;
    public ProcessProfit()
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
        String id = request.getParameter("id");
     //   String pageType = request.getParameter("pageType");
        String page1 = request.getParameter("page1");
        String iso;
        String msgText1 ="";
        String to ="";
        String mail_code="1";
        String subject = "Raise Entry";
        String transaction_type="Raise_Entry";
        String costdebitAcctName = "";
        String costcreditAcctName = "";
//      PROFIT/LOSS ENTRIES
        String plDrAcct = request.getParameter("plDrAcct");
        String plCrAcct = request.getParameter("plCrAcct");
//        String costdebitAcctNo = plDrAcct.substring(8,16);
        String costdebitAcctNo = plDrAcct;
        if(plDrAcct.substring(0,2)=="NGN"){costdebitAcctNo = plDrAcct.substring(8,16);}
        if(plDrAcct.substring(2,5)=="NGN"){costdebitAcctNo = plDrAcct.substring(6,14);}
 //       String costcreditAcctNo = plCrAcct.substring(8,16);
        String costcreditAcctNo = plCrAcct;
        if(plCrAcct.substring(0,2)=="NGN"){costcreditAcctNo = costcreditAcctNo.substring(8,16);}
        if(plCrAcct.substring(2,5)=="NGN"){costcreditAcctNo = costcreditAcctNo.substring(6,14);}
        String plDrAcctType = request.getParameter("plDrAcctType");
        if(plDrAcctType==null){plDrAcctType="";}
        String plCrAcctType = request.getParameter("plCrAcctType");
        if(plCrAcctType==null){plCrAcctType="";}   
        String plDrTranCode = request.getParameter("plDrTranCode");
         plDrTranCode = (plDrTranCode == null) ? "090" : plDrTranCode;  
        String plCrTranCode = request.getParameter("plCrTranCode");
        plCrTranCode = (plCrTranCode == null) ? "090" : plCrTranCode; 
        String plDrNarration = request.getParameter("plDrNarration");
        if(plDrNarration==null){plDrNarration="";}
        String plCrNarration = request.getParameter("plCrNarration");
        if(plCrNarration==null){plCrNarration="";}
 
        String profitLossAmount = request.getParameter("profitLoss");
        if(profitLossAmount==null){profitLossAmount="0.0";}
        double profitLoss = Double.parseDouble(profitLossAmount);
        String superId = request.getParameter("superId");
        if(superId==null){superId="";}
        String batchId = request.getParameter("batchId");
        if(batchId==null){batchId="";}
        String plEffDate = request.getParameter("plEffDate");
        if(plEffDate==null){plEffDate="";}
        String transactionId = request.getParameter("transactionIdProfit");
        String tranId = request.getParameter("tranId");
        String SbuCode = request.getParameter("SbuCode");
        String oldSbuCode = request.getParameter("oldSbuCode");
        String recType = request.getParameter("recType");
          String systemIp =request.getRemoteAddr();
          String macAddress= "";
          int assetCode = request.getParameter("assetCode") == null?0:Integer.parseInt(request.getParameter("assetCode"));
  String finacleTransId = records.getGeneratedTransId();
  		String raiseEntryNarration = plDrNarration;
  	//	String sbu_code = SbuCode+ "|" +oldSbuCode;
  		 String sbu_code = SbuCode+ "|" +oldSbuCode;
  		plDrNarration = SbuCode+"|"+oldSbuCode+"|"+plDrNarration;
  		String [] idSplit = id.split("/");
 //       String costdebitAcctName = records.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+costdebitAcctNo+"'");
 //       String costcreditAcctName = records.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+costcreditAcctNo+"'");
        String test10 = "select category_name from am_ad_category where Asset_Ledger = '"+costdebitAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
        String test11 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+costdebitAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
        String test12 = "select category_name from am_ad_category where DEP_LEDGER = '"+costdebitAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
        String test13 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+costdebitAcctNo+"' ";
        costdebitAcctName = records.getCodeName(test10);
        if(costcreditAcctName.equalsIgnoreCase("")){costdebitAcctName = records.getCodeName(test11);}
        if(costcreditAcctName.equalsIgnoreCase("")){costdebitAcctName = records.getCodeName(test12);}
        if(costcreditAcctName.equalsIgnoreCase("")){costdebitAcctName = records.getCodeName(test13);}  
 //       System.out.println("<<<costdebitAcctNo >>>>"+costdebitAcctNo);
        
        String test20 = "select category_name from am_ad_category where Asset_Ledger = '"+costcreditAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
        String test21 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+costcreditAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
        String test22 = "select category_name from am_ad_category where DEP_LEDGER = '"+costcreditAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
        String test23 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+costcreditAcctNo+"' ";
        costcreditAcctName = records.getCodeName(test20);
        if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = records.getCodeName(test21);}
        if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = records.getCodeName(test22);}
        if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = records.getCodeName(test23);}
 //       System.out.println("<<<costcreditAcctNo >>>>"+costcreditAcctNo);
          try  
          {
        	 if (!userClass.equals("NULL") || userClass!=null){
//	 System.out.println(" <<<<<<<<<<<<<************************************>>>>>>>>>>>>> ");
	 if(profitLoss > 0)
	   {
		 if(ad.isoChecking(  id, page1, transactionId,tranId))
		 {
			 iso=bus.transferFund(plDrNarration,plDrAcct,plCrAcct,profitLoss,finacleTransId,SbuCode);
			 //iso=bus.interaccounttransferFund(plDrNarration,plDrAcct,plCrAcct,profitLoss,finacleTransId,SbuCode,oldSbuCode);
			 ad.updateRaiseEntryTransaction(  id, page1, transactionId, iso,systemIp,tranId,finacleTransId,"","");
		 }
		 else
		 {
		 raiseMan.raiseEntry(plDrAcct,plCrAcct,plDrAcctType,plCrAcctType,plDrTranCode,plCrTranCode,plDrNarration,plCrNarration,profitLoss,userId,batchId,plEffDate,superId,id,"DISPOSAL");
	   //iso mail sending
		  iso=bus.transferFund(plDrNarration,plDrAcct,plCrAcct,profitLoss,finacleTransId,SbuCode);
		 // iso=bus.interaccounttransferFund(plDrNarration,plDrAcct,plCrAcct,profitLoss,finacleTransId,SbuCode,oldSbuCode);
		  ad.insertRaiseEntryTransaction(userId,raiseEntryNarration,plDrAcct,plCrAcct,profitLoss,iso,id,page1,transactionId,systemIp,tranId,assetCode,finacleTransId,costdebitAcctName,costcreditAcctName,userId,recType);
//		  System.out.println("******************plDrNarration,plDrAcct,plCrAcct,profitLoss******************3 "+iso);
//		  System.out.println(plDrNarration+" "+plDrAcct+" "+plCrAcct+" "+profitLoss);
//		  System.out.println("************************************3 "+iso);
		 }

	    if(iso.equalsIgnoreCase("000"))
	     {
    	   	 String reversalId = tranId.substring(0, 1);
    	     if(reversalId.equals("R")){tranId = tranId.substring(1, tranId.length());}
       	     if(!reversalId.equals("R")){tranId = "R"+tranId;}
                String q = "update am_Raisentry_Transaction set iso = '-001' where asset_id='"+id+"' and Trans_id = '"+tranId+"' and transactionId = "+transactionId+"";
                ad.updateAssetStatusChange(q);	      	    	
	            msgText1 = message.MailMessage(mail_code, transaction_type)+", \n";
				msgText1 += "For asset id "+id+".\n";
				to=message.MailTo(mail_code, transaction_type);
	            //ret = bulkMail.sendMail(userId,subject,msgText1,directory,to);
	            //System.out.println("Output of the mail is >>>>"+ret);
				mail.sendMail(to,subject,msgText1);
	       }	
	  //System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>  "+id);
	  //System.out.println(">>>>>>>>>>>>>>>>>>>>>>   "+page1);
	  //session.setAttribute("assetid", id);
	  //session.setAttribute("page1", page1);
	  //out.println("<script>");
	  //out.println("window.location='raiseEntryList.jsp','raiseasset4','width=850,height=350,scrollbars=Yes,resizable=Yes,align=center';");
	  //out.println("</script>");
	   
	  // out.print("<script>window.open('raiseEntryList.jsp','raiseasset4','width=850,height=350,scrollbars=Yes,resizable=Yes,align=center');</script>");
//	    to display transaction status
	    String status =  records.getCodeName("select description from am_error_description where error_code='"+iso+"' ");
	    out.print("<script>alert('"+status+"');</script>");
	    out.print("<script>window.close('disposeRaiseEntry.jsp');</script>");
	  //  session.setAttribute("assetid", id);
		//   session.setAttribute("page1", page1);
		//   out.print("<script>window.open('raiseEntryList.jsp','raiseasset4','width=850,height=350,scrollbars=Yes,resizable=Yes,align=center');</script>");

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