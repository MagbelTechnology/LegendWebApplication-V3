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

public class GroupProcessCreateCost extends HttpServlet
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
	private PostingHelper helper;
	private String group_id="";
	private String asset_id="";
	private String processCost="";
	String page1 = "ASSET CREATION RAISE ENTRY";
	String partPay="";
	String deferPay="";
	String status="";
	
    public GroupProcessCreateCost()
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
        partPay = request.getParameter("partPay");
        deferPay = request.getParameter("deferPay");
        //processCost = request.getParameter("processCost").trim();
        
        int pgNum=Integer.parseInt(request.getParameter("pageNum"));
        
        
        System.out.println("Asset_ID >>>>>> " + asset_id);
        System.out.println("group_id >>>>>> " + group_id);
        System.out.println("pgNum >>>>>>>>>  " + pgNum);
        System.out.println("partPay >>>>>>>>>  " + partPay);
        System.out.println("deferPay >>>>>>>>>  " + deferPay);
        //System.out.println("status >>>>>>>>>  " + status);
        
        String iso;
        String msgText1 ="";
      
        String to ="";
        String mail_code="1";
        String subject = "Raise Entry";
        String transaction_type="Raise_Entry";
        String batchId = "RE"+Integer.toString((int)(Math.random()*8000)+1000);
        String cdate = sdf.format(new Date());
        /**
        Post Cost Transaction
        */
        String costdebitAccount = request.getParameter("cost_dr_account");
        String costdebitAccType = request.getParameter("cost_dr_accttype");
        String costdebitTranCode = request.getParameter("cost_dr_trancode");
        String costdebitNarration = request.getParameter("cost_dr_narration");
        String costcreditAccount = request.getParameter("cost_cr_account");
        String costcreditAccType = request.getParameter("cost_cr_accttype");
        String costcreditTranCode = request.getParameter("cost_cr_trancode");
        String costcreditNarration = request.getParameter("cost_cr_narration");
        String costamount = request.getParameter("cost_amount");
        costamount = costamount.replaceAll(",","");
        String supervisor = request.getParameter("supervisor");
        String SbuCode = request.getParameter("SbuCode");
//        String costdebitAcctNo = costdebitAccount.substring(8,16);
//        String costcreditAcctNo = costcreditAccount.substring(8,16);
        String costdebitAcctNo = "";
        String costcreditAcctNo = "";        
//        if(costdebitAccount.substring(0,2)=="NGN"){costdebitAcctNo = costdebitAccount.substring(8,16);}
//        if(costcreditAccount.substring(0,2)=="NGN"){costcreditAcctNo = costcreditAccount.substring(8,16);}    		    
        if(ThirdPartyLabel.equalsIgnoreCase("INTEGRIFY")){costdebitAcctNo = costdebitAccount.substring(8,16);}
        if(ThirdPartyLabel.equalsIgnoreCase("K2")){costdebitAcctNo = costdebitAccount.substring(6,14);}
        if(ThirdPartyLabel.equalsIgnoreCase("INTEGRIFY")){costcreditAcctNo = costcreditAccount.substring(8,16);}
        if(ThirdPartyLabel.equalsIgnoreCase("K2")){costcreditAcctNo = costcreditAccount.substring(6,14);} 
        String costdebitAcctName = approv.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+costdebitAcctNo+"'");
        String costcreditAcctName = approv.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+costcreditAcctNo+"'");
        String [] idSplit = asset_id.split("/");
        String test10 = "select category_name from am_ad_category where Asset_Ledger = '"+costdebitAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
        String test11 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+costdebitAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
        String test12 = "select category_name from am_ad_category where DEP_LEDGER = '"+costdebitAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
        String test13 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+costdebitAcctNo+"' ";
        costdebitAcctName = approv.getCodeName(test10);
        if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = approv.getCodeName(test11);}
        if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = approv.getCodeName(test12);}
        if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = approv.getCodeName(test13);}
        
        String test20 = "select category_name from am_ad_category where Asset_Ledger = '"+costcreditAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
        String test21 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+costcreditAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
        String test22 = "select category_name from am_ad_category where DEP_LEDGER = '"+costcreditAcctNo+"' and category_acronym = '"+idSplit[2]+"'";
        String test23 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+costcreditAcctNo+"' ";
        costcreditAcctName = approv.getCodeName(test20);
        if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test21);}
        if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test22);}
        if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test23);}        
        String type="Asset Creation";
        String transactionId = request.getParameter("transactionIdCost");
        String exitPage="";
           String finacleTransId = approv.getGeneratedTransId();
           String raiseEntryNarration = costdebitNarration;
           String assetCode = approv.getCodeName("select ASSET_CODE FROM AM_ASSET WHERE ASSET_ID = '"+asset_id+"'");           
      //     String sbu_code = SbuCode+ "|" +SbuCode;
           String sbu_code = SbuCode+SbuCode;
           costdebitNarration = SbuCode+"|"+SbuCode+"|"+costdebitNarration;
        try
        {
        	 if (!userClass.equals("NULL") || userClass!=null){
        	 if (supervisor == "" || supervisor == null || supervisor == "*" || supervisor.equals("*")) {
        		 supervisor = "";
             }
        	if(ad.isoChecking(  asset_id, page1, transactionId))
        	{
        		 iso=bus.transferFund(costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),finacleTransId,SbuCode);
        		 //iso=bus.interaccounttransferFund(costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),finacleTransId,SbuCode,SbuCode);
        		//iso="000";
        		ad.updateRaiseEntryTransaction(  asset_id, page1, transactionId, iso,finacleTransId,"","");
        	}
        	else
        	{
               raiseMan.raiseEntry(costdebitAccount,costcreditAccount,costdebitAccType,costcreditAccType,costdebitTranCode
        		,costcreditTranCode,costdebitNarration,
        		costcreditNarration,Double.parseDouble(costamount), userId,batchId,cdate,supervisor,asset_id,type);
        		
             iso=bus.transferFund(costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),finacleTransId,SbuCode);
             //iso=bus.interaccounttransferFund(costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),finacleTransId,SbuCode,SbuCode);
              // iso="000";
              ad.insertRaiseEntryTransactiongroup(userId,raiseEntryNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),iso,asset_id,page1,transactionId,Integer.parseInt(finacleTransId),costdebitAcctName,costcreditAcctName,userId);
        	}
              if(iso.equalsIgnoreCase("000"))
              {  
         	   	 String reversalId = group_id.substring(0, 1);
         	     if(reversalId.equals("R")){group_id = group_id.substring(1, group_id.length());}
            	     if(!reversalId.equals("R")){group_id = "R"+group_id;}
                     String q = "update am_Raisentry_Transaction set iso = '-001' where asset_id='"+asset_id+"' and Trans_id = '"+group_id+"' and transactionId = "+transactionId+"";
                     ad.updateAssetStatusChange(q);	 
                    //System.out.println("#####################################################################  ");
         			msgText1 = message.MailMessage(mail_code, transaction_type)+", \n";
        			msgText1 += "For Asset Id  "+asset_id+"\n";
        	            msgText1 += "For  "+costdebitNarration+"\n";
        			msgText1 += "Debit Account  "+costdebitAccount+"\n";
        			msgText1 += "Credit Account  "+costcreditAccount+"\n";
        			msgText1 += "Amount  "+formata.formatAmount(costamount)+"\n";
        			msgText1 += "Login here http://172.27.11.192:8080/legend2.net" ;
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
        	        if(pgNum == 5)
        	        {
        	        	exitPage="raiseGroupAssetToAssetDetailDefer";
        	        	processEntries5();
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
        			
        			//DocumentHelp.jsp?np=groupAssetToAssetPosting&gid=13&root=group&id=FCMB/GUIA/ME/6364&status=P
               }
              else
              {
            	  out.println("<script>alert(' Asset  Entry Posting Not Successfull !')</script>");
	              System.out.println("exitPage >>>> " + exitPage);
	              out.println("<script>");
    			  out.println("window.location='DocumentHelp.jsp?np=groupAssetToAssetPosting&gid="+ group_id+"&root=group&id="+ asset_id+"&status="+status+"'" );
    			  out.println("</script>");
              }
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
    
    private void processEntries() 
    {
    	System.out.println("<<<<<<<<<<< TRYING TO PROCESS ENTRIES >>>>>>>>>>>>>>>>>");
    	//DO AN ISO CHECK FOR THE OTHER PROCESSES
    	//String transactionIdCost = "1";
    	String transactionIdVendor = "2";
    	String transactionIdWitholding = "3";
    	String transactionIdVat = "25";
    	//String processCost = ad.isoCheck(  asset_id, page1,transactionIdCost);
    	String processCost2 = ad.isoCheck( asset_id, page1,transactionIdVendor);
    	if (partPay.equalsIgnoreCase("Y"))
    	{
    		processCost2 = "000";
    	}
    	String processWithTax = ad.isoCheck(asset_id, page1,transactionIdWitholding);
    	String processCreateVat = ad.isoCheck(asset_id, page1,transactionIdVat);
    	
    	System.out.println("processCost2 >>>>>> " + processCost2);
    	System.out.println("processWithTax >>>>>> " + processWithTax);
    	System.out.println("processCreateVat >>>>>> " + processCreateVat);
    	
    	if ((processCost2.equalsIgnoreCase("000"))&&  (processWithTax.equalsIgnoreCase("000")) &&  (processCreateVat.equalsIgnoreCase("000")))
    		{
    			helper.postAssetStatus(asset_id);	
    		}
    	
    	/*if ((processWithTax.equalsIgnoreCase("000")) &&  (processCreateVat.equalsIgnoreCase("000")))
		{
			helper.postAssetStatus(asset_id);	
		}
		*/
    	helper.postStatus(group_id);
	}

    private void processEntries2() 
    {
    	System.out.println("<<<<<<<<<<< TRYING TO PROCESS ENTRIES >>>>>>>>>>>>>>>>>");
    	//DO AN ISO CHECK FOR THE OTHER PROCESSES
    	//String transactionIdCost = "1";
    	String transactionIdVendor = "2";
    	//String transactionIdWitholding = "3";
    	String transactionIdVat = "25";
    	//String processCost = ad.isoCheck(  asset_id, page1,transactionIdCost);
    	String processCost2 = ad.isoCheck( asset_id, page1,transactionIdVendor);
    	if (partPay.equalsIgnoreCase("Y"))
    	{
    		processCost2 = "000";
    	}
    	//String processWithTax = ad.isoCheck(asset_id, page1,transactionIdWitholding);
    	String processCreateVat = ad.isoCheck(asset_id, page1,transactionIdVat);
    	
    	System.out.println("processCost2 >>>>>> " + processCost2);
    	//System.out.println("processWithTax >>>>>> " + processWithTax);
    	System.out.println("processCreateVat >>>>>> " + processCreateVat);
    	
    	if ((processCost2.equalsIgnoreCase("000"))&&  (processCreateVat.equalsIgnoreCase("000")))
    		{
    			helper.postAssetStatus(asset_id);	
    		}
    	/*
    	if (processCreateVat.equalsIgnoreCase("000"))
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
    		//For individual assets, the vendor account will no longer be credited
    		
    	
    	/*if (processWithTax.equalsIgnoreCase("000"))
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
    private void processEntries5() 
    {
    	System.out.println("<<<<<<<<<<< TRYING TO PROCESS ENTRIES >>>>>>>>>>>>>>>>>");
    	//DO AN ISO CHECK FOR THE OTHER PROCESSES
    	//String transactionIdCost = "1";
    	String transactionIdVendor = "2";
    	//String transactionIdWitholding = "3";
    	//String processCost = ad.isoCheck(  asset_id, page1,transactionIdCost);
    	String processCost2 = ad.isoCheck( asset_id, page1,transactionIdVendor);
    	if (deferPay.equalsIgnoreCase("Y"))
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
        return "Group Process  Action Servlet";
    }
}