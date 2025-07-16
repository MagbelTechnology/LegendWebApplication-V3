package com.magbel.legend.servlet;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import magma.AssetRecordsBean;
import magma.GroupAssetToAssetBean;
import magma.net.manager.DepreciationProcessingManager;
import magma.net.manager.RaiseEntryManager;
import magma.util.Codes;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.FinancialExchangeServiceBus;
import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.util.CurrencyNumberformat;
import com.magbel.util.PostingHelper;

public class GroupProcessCreateVat extends HttpServlet
{

	private DepreciationProcessingManager service;
	private FinancialExchangeServiceBus bus;
	private RaiseEntryManager raiseMan;
	private EmailSmsServiceBus mail ;
	private Codes message;
	private AssetRecordsBean ad;
	private CurrencyNumberformat formata = new CurrencyNumberformat();
	private SimpleDateFormat sdf;
	private GroupAssetToAssetBean adGroup;
	private ApprovalRecords approv;
	private String group_id="";
	private String asset_id="";
	private String processCost="";
	String page1 = "ASSET CREATION RAISE ENTRY";
	String partPay="";
	private PostingHelper helper;
	String status="";
    public GroupProcessCreateVat()
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
        adGroup = new GroupAssetToAssetBean();
        approv = new ApprovalRecords();
        helper = new PostingHelper();
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
        
		Properties prop = new Properties();
        File file = new File("C:\\Property\\LegendPlus.properties");
        FileInputStream input = new FileInputStream(file);
        prop.load(input);

        String ThirdPartyLabel = prop.getProperty("ThirdPartyLabel");
        
        String userClass = (String) session.getAttribute("UserClass");
        
        String userId =(String)session.getAttribute("CurrentUser");
        asset_id = request.getParameter("asset_id").trim();
        group_id = request.getParameter("group_id");
        int pgNum=Integer.parseInt(request.getParameter("pageNum"));
        partPay = request.getParameter("partPay");
        
        System.out.println("Asset_ID >>>>>> " + asset_id);
        System.out.println("group_id >>>>>> " + group_id);
        System.out.println("pgNum >>>>>>>>>  " + pgNum);
        System.out.println("partPay >>>>>>>>>  " + partPay);
        
        String iso;
        String msgText1 ="";
        String to ="";
        String mail_code="1";
        String subject = "Raise Entry";
        String transaction_type="Raise_Entry";
        String batchId = "RE"+Integer.toString((int)(Math.random()*8000)+1000);
        String cdate = sdf.format(new Date());
        String exitPage="";
       
		/*
        Post VAT Transaction
        */
        String vatdebitAccount = request.getParameter("vat_dr_account");
        String vatdebitAccType = request.getParameter("vat_dr_accttype");
        String vatdebitTranCode = request.getParameter("vat_dr_trancode");
        String vatdebitNarration = request.getParameter("vat_dr_narration");
        String vatcreditAccount = request.getParameter("vat_cr_account");
        String vatcreditAccType = request.getParameter("vat_cr_accttype");
        String vatcreditTranCode = request.getParameter("vat_cr_trancode");
        String vatcreditNarration = request.getParameter("vat_cr_narration");
        String vatamount = request.getParameter("vat_amount");
        String supervisor = request.getParameter("supervisor");
        String SbuCode = request.getParameter("SbuCode");
        String recType = request.getParameter("recType");
        String type="Asset Creation";String transactionId = request.getParameter("transactionIdVat");
        String finacleTransId = approv.getGeneratedTransId();
        String raiseEntryNarration = vatdebitNarration;
       // String sbu_code = SbuCode+ "|" +SbuCode;
        String sbu_code = SbuCode+ SbuCode;
        vatdebitNarration = SbuCode+"|"+SbuCode+"|"+vatdebitNarration;
        
        if(ThirdPartyLabel.equalsIgnoreCase("INTEGRIFY")){vatdebitAccount = vatdebitAccount.substring(8,16);}
        if(ThirdPartyLabel.equalsIgnoreCase("K2")){vatdebitAccount = vatdebitAccount.substring(6,14);}
        if(ThirdPartyLabel.equalsIgnoreCase("INTEGRIFY")){vatcreditAccount = vatcreditAccount.substring(8,16);}
        if(ThirdPartyLabel.equalsIgnoreCase("K2")){vatcreditAccount = vatcreditAccount.substring(6,14);} 
        
        try
        {
        if (!userClass.equals("NULL") || userClass!=null){
        if(vatamount!=null)
        {
        	vatamount = vatamount.replaceAll(",","");
        	
        	 if(ad.isoChecking(   asset_id, page1, transactionId))
             {
        		 iso=bus.transferFund(vatdebitNarration,vatdebitAccount,vatcreditAccount,Double.parseDouble(vatamount),finacleTransId,SbuCode);
        		 //iso=bus.interaccounttransferFund(vatdebitNarration,vatdebitAccount,vatcreditAccount,Double.parseDouble(vatamount),finacleTransId,SbuCode,SbuCode);
        		 //iso="000";
                  ad.updateRaiseEntryTransaction(   asset_id, page1, transactionId, iso,finacleTransId,"","");
             }
             else
             {
            	 raiseMan.raiseEntry(vatdebitAccount,vatcreditAccount,vatdebitAccType,vatcreditAccType,vatdebitTranCode
                 		,vatcreditTranCode,vatdebitNarration,
                 		vatcreditNarration,Double.parseDouble(vatamount), userId,batchId,cdate,supervisor,asset_id,type);
                 		
            	 iso=bus.transferFund(vatdebitNarration,vatdebitAccount,vatcreditAccount,Double.parseDouble(vatamount),finacleTransId,SbuCode);
            	 //iso=bus.interaccounttransferFund(vatdebitNarration,vatdebitAccount,vatcreditAccount,Double.parseDouble(vatamount),finacleTransId,SbuCode,SbuCode);
            	 //iso="000";
                 ad.insertRaiseEntryTransaction(userId,raiseEntryNarration,vatdebitAccount,vatcreditAccount,Double.parseDouble(vatamount),iso,asset_id,page1,"",finacleTransId,recType);
             }
     	
           if(iso.equalsIgnoreCase("000"))
             {
       	   	 String reversalId = group_id.substring(0, 1);
       	     if(reversalId.equals("R")){group_id = group_id.substring(1, group_id.length());}
          	     if(!reversalId.equals("R")){group_id = "R"+group_id;}
                   String q = "update am_Raisentry_Transaction set iso = '-001' where asset_id='"+asset_id+"' and Trans_id = '"+group_id+"' and transactionId = "+transactionId+"";
                   ad.updateAssetStatusChange(q);	 
           			msgText1 = message.MailMessage(mail_code, transaction_type)+", \n";
        			msgText1 += "For asset id  "+asset_id+"\n";
        			msgText1 += "For  "+vatdebitNarration+"\n";
        			msgText1 += "Debit Account  "+vatdebitAccount+"\n";
        			msgText1 += "Credit Account  "+vatcreditAccount+"\n";
        			msgText1 += "Amount  "+formata.formatAmount(vatamount)+"\n";
        			msgText1 += "Login here http://172.27.11.192:8080/legend2.net" ;
          			//ret = bulkMail.sendMail(signinname,transaction_type,msgText1,directory,to);
          			//System.out.println("Output of the mail is >>>>"+ret);
        			/*****COMMENTED FRO TESTING******/
          			//mail.sendMail(to,subject,msgText1);
        			/**********************************/
        			if(pgNum == 1)
        	        {
        				exitPage="raiseGroupAssetToAssetDetail";
        	        	processEntries();
        	        }
        	        if(pgNum == 2)
        	        {
        	        	exitPage="raiseGroupAssetToAssetDetail2";
        	        	processEntries2();
        	        }
        	        if(pgNum == 3)
        	        {
        	        	exitPage="raiseGroupAssetToAssetDetail3";
        	        	processEntries3();
        	        }
        	        if(pgNum == 4)
        	        {
        	        	exitPage="raiseGroupAssetToAssetDetail4";
        	        	processEntries4();
        	        }
        	        status = approv.getCodeName("select post_flag from AM_GROUP_ASSET where asset_id='" + asset_id+"'");
        			out.println("<script>alert('Asset  Entry Successfully posted !')</script>");
        			System.out.println("exitPage >>>> " + exitPage);
        			System.out.println("status >>>> " + status);
        			//out.print("<script>window.close('" + exitPage+".jsp');</script>");	
        			String exitLink="'DocumentHelp.jsp?np=groupAssetToAssetPosting&gid="+ group_id+"&root=group&id="+ asset_id+"&status="+status+"'";
        			System.out.println("exitLink >>>> " + exitLink);
        			out.println("<script>");
      			  	out.println("window.location='DocumentHelp.jsp?np=groupAssetToAssetPosting&gid="+ group_id+"&root=group&id="+ asset_id+"&status="+status+"'" );
      			  	out.println("</script>");
               }
              else
              {
            	  out.println("<script>alert(' Asset  Entry Posting Not Successfull !')</script>");
	              System.out.println("exitPage >>>> " + exitPage);
	              out.println("<script>");
    			  out.println("window.location='DocumentHelp.jsp?np=groupAssetToAssetPosting&gid="+ group_id+"&root=group&id="+ asset_id+"&status="+status+"'" );
    			  out.println("</script>");
              }	
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
    
    private void processEntries() 
    {
    	System.out.println("<<<<<<<<<<< TRYING TO PROCESS ENTRIES >>>>>>>>>>>>>>>>>");
    	//DO AN ISO CHECK FOR THE OTHER PROCESSES
    	String transactionIdCost = "1";
    	String transactionIdVendor = "2";
    	String transactionIdWitholding = "3";
    	//String transactionIdVat = "25";
    	String processCost = ad.isoCheck(  asset_id, page1,transactionIdCost);
    	String processCost2 = ad.isoCheck( asset_id, page1,transactionIdVendor);
    	if (partPay.equalsIgnoreCase("Y"))
    	{
    		processCost2 = "000";
    	}
    	String processWithTax = ad.isoCheck(asset_id, page1,transactionIdWitholding);
    	//String processCreateVat = ad.isoCheck(asset_id, page1,transactionIdVat);
    	
    	System.out.println("processCost2 >>>>>> " + processCost2);
    	System.out.println("processWithTax >>>>>> " + processWithTax);
    	System.out.println("processCost >>>>>> " + processCost);
    	
    	if ((processCost2.equalsIgnoreCase("000"))&&  (processWithTax.equalsIgnoreCase("000")) &&  (processCost.equalsIgnoreCase("000")))
    		{
    			helper.postAssetStatus(asset_id);	
    		}
    	
    	/*if ((processWithTax.equalsIgnoreCase("000")) &&  (processCost.equalsIgnoreCase("000")))
		{
			helper.postAssetStatus(asset_id);	
		}*/
			
    	helper.postStatus(group_id);
	}
    private void processEntries2() 
    {
    	System.out.println("<<<<<<<<<<< TRYING TO PROCESS ENTRIES >>>>>>>>>>>>>>>>>");
    	//DO AN ISO CHECK FOR THE OTHER PROCESSES
    	String transactionIdCost = "1";
    	String transactionIdVendor = "2";
    	//String transactionIdWitholding = "3";
    	//String transactionIdVat = "25";
    	String processCost = ad.isoCheck(  asset_id, page1,transactionIdCost);
    	String processCost2 = ad.isoCheck( asset_id, page1,transactionIdVendor);
    	if (partPay.equalsIgnoreCase("Y"))
    	{
    		processCost2 = "000";
    	}
    	//String processWithTax = ad.isoCheck(asset_id, page1,transactionIdWitholding);
    	//String processCreateVat = ad.isoCheck(asset_id, page1,transactionIdVat);
    	
    	System.out.println("processCost2 >>>>>> " + processCost2);
    	//System.out.println("processWithTax >>>>>> " + processWithTax);
    	//System.out.println("processCreateVat >>>>>> " + processCreateVat);
    	System.out.println("processCost >>>>>> " + processCost);
    	
    	if ((processCost2.equalsIgnoreCase("000"))&&  (processCost.equalsIgnoreCase("000")))
    		{
    			helper.postAssetStatus(asset_id);	
    		}
    	
    	/*if ( (processCost.equalsIgnoreCase("000")))
		{
			helper.postAssetStatus(asset_id);	
		}*/
		
    	helper.postStatus(group_id);	
	}

    private void processEntries3() 
    {
    	System.out.println("<<<<<<<<<<< TRYING TO PROCESS ENTRIES >>>>>>>>>>>>>>>>>");
    	//DO AN ISO CHECK FOR THE OTHER PROCESSES
    	//String transactionIdCost = "1";
    	String transactionIdVendor = "2";
    	String transactionIdWitholding = "3";
    	//String processCost = ad.isoCheck(  asset_id, page1,transactionIdCost);
    	String processCost2 = ad.isoCheck( asset_id, page1,transactionIdVendor);
    	if (partPay.equalsIgnoreCase("Y"))
    	{
    		processCost2 = "000";
    	}
    	String processWithTax = ad.isoCheck(  asset_id, page1,transactionIdWitholding);
    	System.out.println("processCost2 >>>>>> " + processCost2);
    	System.out.println("processWithTax >>>>>> " + processWithTax);
    	
    	if ((processCost2.equalsIgnoreCase("000"))&&  (processWithTax.equalsIgnoreCase("000")))
    		{
    			helper.postAssetStatus(asset_id);	
    		}
    	/*if ( (processWithTax.equalsIgnoreCase("000")))
		{
			helper.postAssetStatus(asset_id);	
		}*/
    	
    	helper.postStatus(group_id);			
	}

    private void processEntries4() 
    {
    	System.out.println("<<<<<<<<<<< TRYING TO PROCESS ENTRIES >>>>>>>>>>>>>>>>>");
    	//DO AN ISO CHECK FOR THE OTHER PROCESSES
    	//String transactionIdCost = "1";
    	String transactionIdVendor = "2";
    	//String transactionIdWitholding = "3";
    	//String processCost = ad.isoCheck(  asset_id, page1,transactionIdCost);
    	String processCost2 = ad.isoCheck( asset_id, page1,transactionIdVendor);
    	if (partPay.equalsIgnoreCase("Y"))
    	{
    		processCost2 = "000";
    	}
    	//String processWithTax = ad.isoCheck(  asset_id, page1,transactionIdWitholding);
    	System.out.println("processCost2 >>>>>> " + processCost2);
    	//System.out.println("processWithTax >>>>>> " + processWithTax);
    	
    	if (processCost2.equalsIgnoreCase("000"))
    		{
    			helper.postAssetStatus(asset_id);	
    		}
		
    	helper.postStatus(group_id);			
		
	}
    public String getServletInfo()
    {
        return "Process Action Servlet";
    }
}