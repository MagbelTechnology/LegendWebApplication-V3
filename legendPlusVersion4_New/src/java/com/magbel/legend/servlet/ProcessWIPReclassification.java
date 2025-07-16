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

import magma.asset.manager.AssetManager;

public class ProcessWIPReclassification extends HttpServlet
{

	private DepreciationProcessingManager service;
	private FinancialExchangeServiceBus bus;
	private RaiseEntryManager raiseMan;
	private EmailSmsServiceBus mail ;
	private Codes message;
	private AssetRecordsBean ad; 
	private ApprovalRecords records;
        private AssetManager asm;
    public ProcessWIPReclassification()
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
        String id = request.getParameter("id");
        String page1 = "WIP RECLASSIFICATION";
        String iso;
        String msgText1 ="";
        String to ="";
        String mail_code="1";
        String subject = "Raise Entry";
        String transaction_type="Raise_Entry";
	   
        //COST ENTRIES
        String  costDrAcct = request.getParameter("costDrAcct");
        String costCrAcct = request.getParameter("costCrAcct");
//        String costdebitAcctNo = costDrAcct.substring(8,16);
//        String costcreditAcctNo = costCrAcct.substring(8,16);
        String costdebitAcctNo = costDrAcct;
        String costcreditAcctNo = costCrAcct;
        if(costdebitAcctNo.substring(0,2)=="NGN"){costdebitAcctNo = costdebitAcctNo.substring(8,16);}
        if(costdebitAcctNo.substring(2,5)=="NGN"){costdebitAcctNo = costdebitAcctNo.substring(6,14);}
        if(costcreditAcctNo.substring(0,2)=="NGN"){costcreditAcctNo = costcreditAcctNo.substring(8,16);}
        if(costcreditAcctNo.substring(2,5)=="NGN"){costcreditAcctNo = costcreditAcctNo.substring(6,14);}
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
        String costAmount = request.getParameter("cost");
        if(costAmount==null){costCrNarration="0.0";}
        double cost = Double.parseDouble(costAmount);
        String superId = request.getParameter("superId");
        if(superId==null){superId="";}
        String batchId = request.getParameter("batchId");
        if(batchId==null){batchId="";}
        String costEffDate = request.getParameter("vatEntryDate");
        if(costEffDate==null){costEffDate="";}
        String transactionId = request.getParameter("transactionIdCost");
         String tranId = request.getParameter("tranId");
         String SbuCode = request.getParameter("SbuCode");
         String raiseEntryNarration = costDrNarration;
          String systemIp =request.getRemoteAddr();
          String macAddress= "";
          String oldSbuCode = request.getParameter("SbuCode");
          String recType = request.getParameter("recType");
          costDrNarration = SbuCode+"|"+oldSbuCode+"|"+costDrNarration;
          int assetCode = request.getParameter("assetCode") == null?0:Integer.parseInt(request.getParameter("assetCode"));
          String costdebitAcctName = records.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+costdebitAcctNo+"'");
          String costcreditAcctName = records.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+costcreditAcctNo+"'");
 try  
    {
	 if (!userClass.equals("NULL") || userClass!=null){
	// System.out.println(" <<<<<<<<<<<<<**********Inside ProcessWIPReclassification**************************>>>>>>>>>>>>> ");
	 if(cost > 0)
	   {
             String finacleTransId = records.getGeneratedTransId();
	     if(ad.isoChecking(  id, page1, transactionId,tranId))
	      {
		   iso=bus.transferFund(costDrNarration,costDrAcct,costCrAcct,cost,finacleTransId,SbuCode);
	       ad.updateRaiseEntryTransaction( id, page1, transactionId, iso,systemIp,tranId,finacleTransId,"","");
	      }
	     else{
	       raiseMan.raiseEntry(costDrAcct,costCrAcct,costDrAcctType,costCrAcctType,costDrTranCode,costCrTranCode,
                       costDrNarration,costCrNarration,cost,userId,batchId,costEffDate,superId,id,"WIP RECLASSIFICATION");
	       iso=bus.transferFund(costDrNarration,costDrAcct,costCrAcct,cost,finacleTransId,SbuCode);
	       ad.insertRaiseEntryTransaction(userId,raiseEntryNarration,costDrAcct,costCrAcct,cost,iso,id,page1,transactionId,systemIp,tranId,assetCode,finacleTransId,costdebitAcctName,costcreditAcctName,userId,recType);
	       System.out.println("*****************costDrNarration,costDrAcct,costCrAcct,cost******************* "+iso);
	       System.out.println(costDrNarration+" "+costDrAcct+" "+costCrAcct+" "+cost);
	       System.out.println("************************************ "+iso);
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
				mail.sendMail(to,subject,msgText1);
                                  String AssetCode= String.valueOf(assetCode);
	    	                asm.updateAssetWip(AssetCode);
                                
	       }
	  //System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>  "+id);
	  //System.out.println(">>>>>>>>>>>>>>>>>>>>>>   "+page1);
	   //session.setAttribute("assetid", id);
	   //session.setAttribute("page1", page1);
	   //out.println("<script>");
	   //out.println("window.location='raiseEntryList.jsp','raiseasset4','width=850,height=350,scrollbars=Yes,resizable=Yes,align=center';");
	   //out.println("</script>");
	   
	  // out.print("<script>window.open('raiseEntryList.jsp','raiseasset4','width=850,height=350,scrollbars=Yes,resizable=Yes,align=center');</script>");
	   //out.print("<script>window.close('transferRaiseEntry.jsp');</script>");
	   //session.setAttribute("assetid", id);
	  // session.setAttribute("page1", page1);
	  // out.print("<script>window.open('raiseEntryList.jsp','raiseasset4','width=850,height=350,scrollbars=Yes,resizable=Yes,align=center');</script>");
	  //to display transaction status
	    String status =  records.getCodeName("select description from am_error_description where error_code='"+iso+"' ");
	    out.print("<script>alert('"+status+"');</script>");
	    
	    out.print("<script>window.close('WipReclassificationPostingEntry.jsp');</script>");
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
        return "ProcessWIPReclassification Servlet";
    }
}