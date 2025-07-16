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

public class ProcessDisposal extends HttpServlet
{

	private DepreciationProcessingManager service;
	private FinancialExchangeServiceBus bus;
	private RaiseEntryManager raiseMan;
	private EmailSmsServiceBus mail ;
	private Codes message;
	private AssetRecordsBean ad;
	private ApprovalRecords records;
    public ProcessDisposal()
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
        String page1 = request.getParameter("page1");
        String iso;
        String msgText1 ="";
        String to ="";
        String mail_code="1";
        String subject = "Raise Entry";
        String transaction_type="Raise_Entry";
	   
//      DISPOSE ENTRIES
        String disposeDrAcct = request.getParameter("disposeDrAcct");
        String disposeCrAcct = request.getParameter("disposeCrAcct");
//        String disposeddebitAcctNo = disposeDrAcct.substring(8,16);
        String disposeddebitAcctNo = request.getParameter("disposeDrAcct");
        if(disposeddebitAcctNo.substring(0,2)=="NGN"){disposeddebitAcctNo = disposeddebitAcctNo.substring(8,16);}
//        String disposedcreditAcctNo = disposeCrAcct.substring(8,16);
        String disposedcreditAcctNo = request.getParameter("disposeCrAcct");
        if(disposedcreditAcctNo.substring(0,2)=="NGN"){disposedcreditAcctNo = disposedcreditAcctNo.substring(8,16);}
        String disposeDrAcctType = request.getParameter("disposeDrAcctType");
        if(disposeDrAcctType==null){disposeDrAcctType="";}
        String disposeCrAcctType = request.getParameter("disposeCrAcctType");
        if(disposeCrAcctType==null){disposeCrAcctType="";}
        String disposeDrTranCode = request.getParameter("disposeDrTranCode");
        if(disposeDrTranCode==null){disposeDrTranCode="";}
        String disposeCrTranCode = request.getParameter("disposeCrTranCode");
        if(disposeCrTranCode==null){disposeCrTranCode="";}
        String disposeDrNarration = request.getParameter("disposeDrNarration");
        if(disposeDrNarration==null){disposeDrNarration="";}
        String disposeCrNarration = request.getParameter("disposeCrNarration");
        if(disposeCrNarration==null){disposeCrNarration="";}
        
        String disposalAmtAmount = request.getParameter("disposalAmt");
        if(disposalAmtAmount==null){disposalAmtAmount="0.0";}
        double disposalAmt = Double.parseDouble(disposalAmtAmount);
        String superId = request.getParameter("superId");
        if(superId==null){superId="";}
        String batchId = request.getParameter("batchId");
        if(batchId==null){batchId="";}
        String disposeEffDate = request.getParameter("disposeEffDate");
        if(disposeEffDate==null){disposeEffDate="";}
        String transactionId = request.getParameter("transactionIdDisposal");
         String tranId = request.getParameter("tranId");
          String systemIp =request.getRemoteAddr();
          String macAddress= "";
          String SbuCode = request.getParameter("SbuCode");
          String oldSbuCode = request.getParameter("oldSbuCode");
          String recType = request.getParameter("recType");
          String finacleTransId = records.getGeneratedTransId();
          String raiseEntryNarration = disposeDrNarration;
         // String sbu_code = SbuCode+ "|" +oldSbuCode;
          String sbu_code = SbuCode+oldSbuCode;
          disposeDrNarration = SbuCode+"|"+SbuCode+"|"+disposeDrNarration;
          int assetCode = request.getParameter("assetCode") == null?0:Integer.parseInt(request.getParameter("assetCode"));
          String disposeddebitAcctName = records.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+disposeddebitAcctNo+"'");
          String disposedcreditAcctName = records.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+disposedcreditAcctNo+"'");
          String [] idSplit = id.split("/");         
          String test10 = "select category_name from am_ad_category where Asset_Ledger = '"+disposeddebitAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
          String test11 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+disposeddebitAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
          String test12 = "select category_name from am_ad_category where DEP_LEDGER = '"+disposeddebitAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
          String test13 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+disposeddebitAcctNo+"' ";
          disposeddebitAcctName = records.getCodeName(test10);
          if(disposeddebitAcctName.equalsIgnoreCase("")){disposeddebitAcctName = records.getCodeName(test11);}
          if(disposeddebitAcctName.equalsIgnoreCase("")){disposeddebitAcctName = records.getCodeName(test12);}  
          if(disposeddebitAcctName.equalsIgnoreCase("")){disposeddebitAcctName = records.getCodeName(test13);}  
          
          String test20 = "select category_name from am_ad_category where Asset_Ledger = '"+disposedcreditAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
          String test21 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+disposedcreditAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
          String test22 = "select category_name from am_ad_category where DEP_LEDGER = '"+disposedcreditAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
          String test23 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+disposedcreditAcctNo+"' ";
          disposedcreditAcctName = records.getCodeName(test20);
          if(disposedcreditAcctName.equalsIgnoreCase("")){disposedcreditAcctName = records.getCodeName(test21);}
          if(disposedcreditAcctName.equalsIgnoreCase("")){disposedcreditAcctName = records.getCodeName(test22);}  
          if(disposedcreditAcctName.equalsIgnoreCase("")){disposedcreditAcctName = records.getCodeName(test23);}  
          
 try
    {
	 if (!userClass.equals("NULL") || userClass!=null){
	 System.out.println(" <<<<<<<<<<<<<************************************>>>>>>>>>>>>> ");
	 if(disposalAmt > 0)
	   {
		 if(ad.isoChecking(   id, page1, transactionId,tranId))
		 {  
			 iso=bus.transferFund(disposeDrNarration,disposeDrAcct,disposeCrAcct,disposalAmt,finacleTransId,SbuCode);
			 //iso=bus.interaccounttransferFund(disposeDrNarration,disposeDrAcct,disposeCrAcct,disposalAmt,finacleTransId,SbuCode,oldSbuCode);
			 ad.updateRaiseEntryTransaction( id, page1, transactionId, iso,systemIp,tranId,finacleTransId,"","");
		 }
		 else
		 {
		 	raiseMan.raiseEntry(disposeDrAcct,disposeCrAcct,disposeDrAcctType,disposeCrAcctType,disposeDrTranCode,disposeCrTranCode,disposeDrNarration,disposeCrNarration,disposalAmt,userId,batchId,disposeEffDate,superId,id,"DISPOSAL");
	   //iso mail sending
		 	iso=bus.transferFund(disposeDrNarration,disposeDrAcct,disposeCrAcct,disposalAmt,finacleTransId,SbuCode);
		 	//iso=bus.interaccounttransferFund(disposeDrNarration,disposeDrAcct,disposeCrAcct,disposalAmt,finacleTransId,SbuCode,oldSbuCode);
		   ad.insertRaiseEntryTransaction(userId,raiseEntryNarration,disposeDrAcct,disposeCrAcct,disposalAmt,iso,id,page1,transactionId,systemIp,tranId,assetCode,finacleTransId,disposeddebitAcctName,disposedcreditAcctName,userId,recType);
		   System.out.println("****************disposeDrNarration,disposeDrAcct,disposeCrAcct,disposalAmt********************2 "+iso);
		   System.out.println(disposeDrNarration+" "+disposeDrAcct+" "+disposeCrAcct+" "+disposalAmt);
		   System.out.println("************************************2 "+iso);
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
//		    to display transaction status
		    String status =  records.getCodeName("select description from am_error_description where error_code='"+iso+"' ");
		    out.print("<script>alert('"+status+"');</script>");
	     out.print("<script>window.close('disposeRaiseEntry.jsp');</script>");
//	     session.setAttribute("assetid", id);
//		   session.setAttribute("page1", page1);
//		   out.print("<script>window.open('raiseEntryList.jsp','raiseasset4','width=850,height=350,scrollbars=Yes,resizable=Yes,align=center');</script>");

	   }
    }else{
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