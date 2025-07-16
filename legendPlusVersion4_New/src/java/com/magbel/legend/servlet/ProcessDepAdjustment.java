package com.magbel.legend.servlet;



import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.FinancialExchangeServiceBus;
import com.magbel.legend.mail.EmailSmsServiceBus;
import  com.magbel.util.CurrencyNumberformat ;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import magma.AssetRecordsBean;
import magma.net.manager.DepreciationProcessingManager;
import magma.net.manager.RaiseEntryManager;
import magma.util.Codes;

public class ProcessDepAdjustment extends HttpServlet
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
    public ProcessDepAdjustment()
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

        String page1 = "DEPRECIATION ADJUSTMENT RAISE ENTRY";
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
        String costdebitAccount = request.getParameter("costDrAcct");
//        String costdebitAcctNo = costdebitAccount.substring(8,16);
        String costdebitAccType = request.getParameter("costDrAcctType");
        String costdebitTranCode = request.getParameter("costDrTranCode");
        String costdebitNarration = request.getParameter("costDrNarration");
        String costcreditAccount = request.getParameter("costCrAcct");
//       String costcreditAcctNo = costcreditAccount.substring(8,16);
        String costcreditAccType = request.getParameter("costCrAcctType");
        String costcreditTranCode = request.getParameter("costCrTranCode");
        String costcreditNarration = request.getParameter("costCrNarration");
        String costamount = request.getParameter("cost");
        String costEffDate = request.getParameter("vatEntryDate");
        costamount = costamount.replaceAll(",","");
        String supervisor = "";
//        String supervisor = request.getParameter("supervisor");
        String type="Asset Creation";
        String transactionId = request.getParameter("transactionIdCost");
        String tranId = request.getParameter("tranId");
        String SbuCode = request.getParameter("SbuCode");
        String integrifyid = request.getParameter("integrifyid");
        String finacleTransId=records.getGeneratedTransId();
        String raiseEntryNarration = costdebitNarration; 
       // String sbu_code = SbuCode+ "|" +SbuCode;
        String sbu_code = SbuCode+SbuCode;
        costcreditNarration = SbuCode+"|"+SbuCode+"|"+costcreditNarration;
        costdebitNarration = SbuCode+"|"+SbuCode+"|"+costdebitNarration;
        String costdebitAcctName = records.getCodeName("select category_name from am_ad_category where Accum_Dep_ledger = '"+costdebitAccount+"' and substring(category_acronym,1,1) != 'F'");
        String costcreditAcctName = records.getCodeName("select category_name from am_ad_category where Dep_ledger = '"+costcreditAccount+"' and substring(category_acronym,1,1) != 'F'");
       // testfield = "125";
        try
        {
        	if (!userClass.equals("NULL") || userClass!=null){
        	 if (supervisor == "" || supervisor == null || supervisor == "*" || supervisor.equals("*")) {
        		 supervisor = "";
             }
        	 String tranIdReverse = "R"+tranId;
        	//if(ad.isoChecking(  id, page1, transactionId,tranId))
                    if(ad.isoChecking(  id, page1, transactionId,tranId))
        	{
        		iso=bus.transferFund(costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),finacleTransId,SbuCode);
        		//iso=bus.interaccounttransferFund(costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),finacleTransId,SbuCode,SbuCode);
        		ad.updateRaiseEntryTransaction(  id, page1, transactionId, iso, systemIp,tranId,finacleTransId,"","");
                      //  ad.updateRaiseEntryTransaction(  id, page1, transactionId, iso, systemIp);
                        ad.updateRaiseEntryTransactionArchive(id, page1, transactionId, iso,systemIp,macAddress,tranId);
                       // ad.updateRaiseEntryTransactionArchive(id, page1, transactionId, iso,systemIp,macAddress);
                        if(iso.equalsIgnoreCase("000"))
                        {
                        int i = raiseMan.raiseEntry(costdebitAccount,costcreditAccount,costdebitAccType,costcreditAccType,
                    		   costdebitTranCode,costcreditTranCode,costdebitNarration,costcreditNarration,
                    		   Double.parseDouble(costamount),userId,batchId,costEffDate,supervisor,id,"DEPRECIATION ADJUSTMENT");
                        }
                }
        	else
        	{
               raiseMan.raiseEntry(costdebitAccount,costcreditAccount,costdebitAccType,costcreditAccType,costdebitTranCode
        		,costcreditTranCode,costdebitNarration,
        		costcreditNarration,Double.parseDouble(costamount), userId,batchId,cdate,supervisor,id,type);



              iso=bus.transferFund(costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),finacleTransId,sbu_code);
              //iso=bus.interaccounttransferFund(costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),finacleTransId,SbuCode,SbuCode);
             // ad.insertRaiseEntryTransaction(userId,costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),iso,id,page1,transactionId,systemIp);
                ad.insertRaiseEntryTransaction(userId,raiseEntryNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),iso,id,page1,transactionId,systemIp,tranId,assetCode,finacleTransId,costdebitAcctName,costcreditAcctName,userId,integrifyid);
              // ad.insertRaiseEntryTransactionArchive(userId,costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),iso,id,page1,transactionId,systemIp,macAddress);
                ad.insertRaiseEntryTransactionArchive(userId,raiseEntryNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),iso,id,page1,transactionId,systemIp,macAddress,tranId,assetCode);

                }
   
                      
              if(iso.equalsIgnoreCase("000"))
              {
                  String reversalId = tranId.substring(0, 1);
//                   System.out.println(">>>>>>>>>>>tranId====:  "+tranId+"   tranId.length: "+tranId.length());
//                   System.out.println(">>>>>>>>>>>tranId.substring(1, 1):  "+tranId.substring(0, 1)+"   New TranId: "+tranId.substring(1, tranId.length()));
          	     if(reversalId.equals("R")){tranId = tranId.substring(1, tranId.length());}
           	     if(!reversalId.equals("R")){tranId = "R"+tranId;}
                    String q = "update am_Raisentry_Transaction set iso = '-001' where asset_id='"+id+"' and Trans_id = '"+tranId+"' and transactionId = "+transactionId+"";
                    ad.updateAssetStatusChange(q);	            	 
            	  
 //           	  ad.updateAssetStatusChange("update am_Raisentry_Transaction set iso = '' where trans_id='"+tranIdReverse+"' and transactionId = "+transactionId+""); 
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
              out.print("<script>window.close('depAdjustRaiseEntry.jsp');</script>");
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