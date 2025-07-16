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

public class ProcessMaintenancePayment extends HttpServlet
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
    public ProcessMaintenancePayment()
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
        int assetCode = request.getParameter("assetCode") == null?0:Integer.parseInt(request.getParameter("assetCode"));

        String page1 = "FLEET MAINTENANCE RAISE ENTRY";
        String iso;
        String msgText1 ="";
        String to ="";
        String mail_code="1";
        String subject = "Raise Entry";
        String transaction_type="Raise_Entry";
        String batchId = "RE"+Integer.toString((int)(Math.random()*8000)+1000);
        String cdate = sdf.format(new Date());
        String systemIp =request.getRemoteAddr();
        String macAddress= "";
        /**
        Post Cost Transaction
        */
        String ThirdPartyLabel = request.getParameter("ThirdPartyLabel");
        String costdebitAccount = request.getParameter("costDrAcct");
//        String auisitionDRAcctNo = costdebitAccount.substring(8,16);
        String auisitionDRAcctNo = "";
        if(ThirdPartyLabel.equalsIgnoreCase("K2")){ auisitionDRAcctNo = costdebitAccount.substring(6,14);}
        if(!ThirdPartyLabel.equalsIgnoreCase("INTEGRIFY")){ auisitionDRAcctNo = costdebitAccount.substring(8,16);}
        String costdebitAccType = request.getParameter("costDrAcctType");
        String costdebitTranCode = request.getParameter("costDrTranCode");
        String costdebitNarration = request.getParameter("costDrNarration");
        String costcreditAccount = request.getParameter("costCrAcct");
        String costcreditAccType = request.getParameter("costCrAcctType");
        String costcreditTranCode = request.getParameter("costCrTranCode");
        String costcreditNarration = request.getParameter("costCrNarration");
        String transactionNarration = request.getParameter("costCrNarration");
        String costamount = request.getParameter("cost");
        costamount = costamount.replaceAll(",","");
        String supervisor = request.getParameter("superId");
        String type="Insurance Payment";
        String transactionId = request.getParameter("transactionIdCost");
        String tranId = request.getParameter("tranId");
        String SbuCode = request.getParameter("SbuCode");
        String location = request.getParameter("location");
        String projectCode = request.getParameter("projectCode");
        String vendorCode = request.getParameter("vendorCode");
        String recType = request.getParameter("recType");
        String finacleTransId=records.getGeneratedTransId();
        String raiseEntryNarration = costdebitNarration; 
       // String sbu_code = SbuCode+ "|" +SbuCode;
        System.out.println("projectCode >>>>"+projectCode);
        String sbu_code = SbuCode+SbuCode;
        costcreditNarration = SbuCode+"|"+SbuCode+"|"+costcreditNarration;
        costdebitNarration = SbuCode+"|"+SbuCode+"|"+costdebitNarration;
        String aquisitiondebitAcctName = records.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+auisitionDRAcctNo+"'");
        String creditrAcctName = records.getCodeName("select vendor_name from am_ad_vendor where account_number = '"+costcreditAccount+"'");
        
       // testfield = "125";
        try
        {
        	if (!userClass.equals("NULL") || userClass!=null){
        	 if (supervisor == "" || supervisor == null || supervisor == "*" || supervisor.equals("*")) {
        		 supervisor = "";
             }
        	//if(ad.isoChecking(  id, page1, transactionId,tranId))
                    if(ad.isoChecking(  id, page1, transactionId))
        	{
        		iso=bus.transferFund(costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),finacleTransId,SbuCode);
        		//iso=bus.interaccounttransferFund(costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),finacleTransId,SbuCode,SbuCode);
        		ad.updateRaiseEntryTransaction(  id, page1, transactionId, iso, systemIp,tranId,finacleTransId,"","");
                      //  ad.updateRaiseEntryTransaction(  id, page1, transactionId, iso, systemIp);
                        ad.updateRaiseEntryTransactionArchive(id, page1, transactionId, iso,systemIp,macAddress,tranId);
                       // ad.updateRaiseEntryTransactionArchive(id, page1, transactionId, iso,systemIp,macAddress);
                }
        	else
        	{
               raiseMan.raiseEntry(costdebitAccount,costcreditAccount,costdebitAccType,costcreditAccType,costdebitTranCode
        		,costcreditTranCode,costdebitNarration,
        		costcreditNarration,Double.parseDouble(costamount), userId,batchId,cdate,supervisor,id,type);



              iso=bus.transferFund(costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),finacleTransId,sbu_code);
              //iso=bus.interaccounttransferFund(costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),finacleTransId,SbuCode,SbuCode);
             // ad.insertRaiseEntryTransaction(userId,costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),iso,id,page1,transactionId,systemIp);
                ad.insertRaiseEntryTransaction(userId,raiseEntryNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),iso,id,page1,transactionId,systemIp,tranId,assetCode,finacleTransId,aquisitiondebitAcctName,creditrAcctName,userId,recType);
              // ad.insertRaiseEntryTransactionArchive(userId,costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),iso,id,page1,transactionId,systemIp,macAddress);
                ad.insertRaiseEntryTransactionArchive(userId,raiseEntryNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),iso,id,page1,transactionId,systemIp,macAddress,tranId,assetCode);

                }
                    
              if(iso.equalsIgnoreCase("000"))
              {
            	  ad.insertVendorTransaction(userId,transactionNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),location,transactionId,page1,projectCode,vendorCode);
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