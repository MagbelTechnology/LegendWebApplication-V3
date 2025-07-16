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

public class ProcessAccum extends HttpServlet
{

	private DepreciationProcessingManager service;
	private FinancialExchangeServiceBus bus;
	private RaiseEntryManager raiseMan;
	private EmailSmsServiceBus mail ;
	private Codes message;
	private AssetRecordsBean ad;
	private ApprovalRecords records;
    public ProcessAccum()
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
        String page1 = "ASSET DISPOSAL RAISE ENTRY";
        String iso;
        String msgText1 ="";
        String to ="";
        String mail_code="1";
        String subject = "Raise Entry";
        String transaction_type="Raise_Entry";
	   
        String accumDrAcct = request.getParameter("accumDrAcct");
        String accumCrAcct = request.getParameter("accumCrAcct");
        String accumdebitAcctNo = accumDrAcct.substring(8,16);
        if(accumDrAcct.substring(0,2)=="NGN"){accumdebitAcctNo = accumDrAcct.substring(8,16);}
        String accumcreditAcctNo = accumCrAcct.substring(8,16);
        if(accumCrAcct.substring(0,2)=="NGN"){accumcreditAcctNo = accumCrAcct.substring(8,16);}
        String accumDrAcctType = request.getParameter("accumDrAcctType");
        if(accumDrAcctType==null){accumDrAcctType="";}
        String accumCrAcctType = request.getParameter("accumCrAcctType");
        if(accumCrAcctType==null){accumCrAcctType="";}
        String accumDrTranCode = request.getParameter("accumDrTranCode");
        if(accumDrTranCode==null){accumDrTranCode="";}
        String accumCrTranCode = request.getParameter("accumCrTranCode");
        if(accumCrTranCode==null){accumCrTranCode="";}
        String accumDrNarration = request.getParameter("accumDrNarration");
        if(accumDrNarration==null){accumDrNarration="";}
        String accumCrNarration = request.getParameter("accumCrNarration");
        if(accumCrNarration==null){accumCrNarration="";}
        
        String accumDepAmount = request.getParameter("accumDep");
        if(accumDepAmount==null){accumDepAmount="0.0";}
        double accumDep = Double.parseDouble(accumDepAmount);
        String superId = request.getParameter("superId");
        if(superId==null){superId="";}
        String batchId = request.getParameter("batchId");
        if(batchId==null){batchId="";}
        String accumEffDate = request.getParameter("accumEffDate");
        if(accumEffDate==null){accumEffDate="";}
        String transactionId = request.getParameter("transactionIdAccum");
          String tranId = request.getParameter("tranId");
          String SbuCode = request.getParameter("SbuCode");
          String oldSbuCode = request.getParameter("oldSbuCode");
          String recType = request.getParameter("recType");
          String raiseEntryNarration = accumDrNarration;  
        //  String sbu_code = SbuCode+ "|" +oldSbuCode;
          accumDrNarration = SbuCode+"|"+SbuCode+"|"+accumDrNarration;
          String sbu_code = SbuCode+oldSbuCode;
          String systemIp =request.getRemoteAddr();
          String macAddress= "";
          int assetCode = request.getParameter("assetCode") == null?0:Integer.parseInt(request.getParameter("assetCode"));
          String accumdebitAcctName = records.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+accumdebitAcctNo+"'");
          String accumcreditAcctName = records.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+accumcreditAcctNo+"'");
          
          String [] idSplit = id.split("/");         
          String test10 = "select category_name from am_ad_category where Asset_Ledger = '"+accumdebitAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
          String test11 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+accumdebitAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
          String test12 = "select category_name from am_ad_category where DEP_LEDGER = '"+accumdebitAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
          String test13 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+accumdebitAcctNo+"' ";
          accumdebitAcctName = records.getCodeName(test10);
          if(accumdebitAcctName.equalsIgnoreCase("")){accumdebitAcctName = records.getCodeName(test11);}
          if(accumdebitAcctName.equalsIgnoreCase("")){accumdebitAcctName = records.getCodeName(test12);} 
          if(accumdebitAcctName.equalsIgnoreCase("")){accumdebitAcctName = records.getCodeName(test13);}  

          String test20 = "select category_name from am_ad_category where Asset_Ledger = '"+accumcreditAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
          String test21 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+accumcreditAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
          String test22 = "select category_name from am_ad_category where DEP_LEDGER = '"+accumcreditAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
          String test23 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+accumcreditAcctNo+"' ";
          accumcreditAcctName = records.getCodeName(test20);
          if(accumcreditAcctName.equalsIgnoreCase("")){accumcreditAcctName = records.getCodeName(test21);}
          if(accumcreditAcctName.equalsIgnoreCase("")){accumcreditAcctName = records.getCodeName(test22);}  
          if(accumcreditAcctName.equalsIgnoreCase("")){accumcreditAcctName = records.getCodeName(test23);}  
 try
    {    
	 if (!userClass.equals("NULL") || userClass!=null){
//	 System.out.println(" <<<<<<<<<<<<<***************Accum****************>>>>>>>>>>>>> ");
//	 if(accumDep > 0)
//	   {
             String finacleTransId = records.getGeneratedTransId();
//		   System.out.println("***********"+accumDrAcct+"--"+accumCrAcct+"--"+accumDrAcctType+"--"+accumCrAcctType+"--"+accumDrTranCode+"--"+accumCrTranCode+"--"+accumDrNarration+"--"+accumCrNarration+"--"+accumDep+"--"+userId+"--"+batchId+"--"+accumEffDate+"--"+superId+"--"+id+"");
		 if(ad.isoChecking( id, page1, transactionId,tranId))
		    {
			 iso=bus.transferFund(accumDrNarration,accumDrAcct,accumCrAcct,accumDep,finacleTransId,SbuCode);
			 //iso=bus.interaccounttransferFund(accumDrNarration,accumDrAcct,accumCrAcct,accumDep,finacleTransId,SbuCode,oldSbuCode);
		     ad.updateRaiseEntryTransaction(   id, page1, transactionId, iso,systemIp,tranId,finacleTransId,"","");
		    }
		 else
		 {
	      raiseMan.raiseEntry(accumDrAcct,accumCrAcct,accumDrAcctType,accumCrAcctType,accumDrTranCode,accumCrTranCode,accumDrNarration,accumCrNarration,accumDep,userId,batchId,accumEffDate,superId,id,"DISPOSAL");
	   //iso mail sending
		  iso=bus.transferFund(accumDrNarration,accumDrAcct,accumCrAcct,accumDep,finacleTransId,SbuCode);
		  //iso=bus.interaccounttransferFund(accumDrNarration,accumDrAcct,accumCrAcct,accumDep,finacleTransId,SbuCode,oldSbuCode);
		  ad.insertRaiseEntryTransaction(userId,raiseEntryNarration,accumDrAcct,accumCrAcct,accumDep,iso,id,page1,transactionId,systemIp,tranId,assetCode,finacleTransId,accumdebitAcctName,accumcreditAcctName,userId,recType);
//	      System.out.println("*****************accumDrNarration,accumDrAcct,accumCrAcct,accumDep*******************1 "+iso);
//	      System.out.println(accumDrNarration+" "+accumDrAcct+" "+accumCrAcct+" "+accumDep);
//	      System.out.println("************************************1 "+iso);
		 }
	      if(iso.equalsIgnoreCase("000"))
	      {
	            msgText1 = message.MailMessage(mail_code, transaction_type)+", \n";
				msgText1 += "For asset id "+id+".\n";
				to=message.MailTo(mail_code, transaction_type);
	            //ret = bulkMail.sendMail(userId,subject,msgText1,directory,to);
	            // System.out.println("Output of the mail is >>>>"+ret);
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
	      //session.setAttribute("assetid", id);
		  // session.setAttribute("page1", page1);
		  // out.print("<script>window.open('raiseEntryList.jsp','raiseasset4','width=850,height=350,scrollbars=Yes,resizable=Yes,align=center');</script>");

	// Accum > 0  }
    }else {
		out.print("<script>alert('You have No Right')</script>");
	}
    }
   catch(Exception e)
       {
	   out.print("<script>alert('You have No Right')</script>");
	     e.printStackTrace();
	   }
  }   
  
    public String getServletInfo()
    {
        return "Process Action Servlet";
    }
}