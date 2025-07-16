package com.magbel.legend.servlet;



import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.vao.Approval;

import java.io.*;

import magma.AssetRecordsBean;
import magma.GroupAssetToAssetBean;
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
import com.magbel.util.PostingHelper;

import java.text.SimpleDateFormat;

public class GroupProcessCreateWitholding extends HttpServlet
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
	String status="";
	private PostingHelper helper;
    public GroupProcessCreateWitholding()
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
        
        asset_id = request.getParameter("asset_id").trim();
        group_id = request.getParameter("group_id");
        partPay = request.getParameter("partPay");
        
        //processCost = request.getParameter("processCost").trim();
        
        int pgNum=Integer.parseInt(request.getParameter("pageNum"));        
        System.out.println("Asset_ID >>>>>> " + asset_id);
        System.out.println("group_id >>>>>> " + group_id);
        System.out.println("pgNum >>>>>>>>>  " + pgNum);
        System.out.println("partPay >>>>>>>>>  " + partPay);
        String userClass = (String) session.getAttribute("UserClass");
        
        String userId =(String)session.getAttribute("CurrentUser");
        String iso="";
        String msgText1 ="";
        String to ="";
        String mail_code="1";
        String subject = "Raise Entry";
        String transaction_type="Raise_Entry";
        String batchId = "RE"+Integer.toString((int)(Math.random()*8000)+1000);
        String cdate = sdf.format(new Date());
		/**
        Post TAX Transaction
        */
        String taxdebitAccount = request.getParameter("tax_dr_account");
        String taxdebitAccType = request.getParameter("tax_dr_accttype");
        String taxdebitTranCode = request.getParameter("tax_dr_trancode");
        String taxdebitNarration = request.getParameter("tax_dr_narration");
        String taxcreditAccount = request.getParameter("tax_cr_account");
        String taxcreditAccType = request.getParameter("tax_cr_accttype");
        String taxcreditTranCode = request.getParameter("tax_cr_trancode");
        String taxcreditNarration = request.getParameter("tax_cr_narration");
        String taxamount = request.getParameter("wh_amount");
        String supervisor = request.getParameter("supervisor");
        String type="Asset Creation";
        String transactionId = request.getParameter("transactionIdWitholding");
        String SbuCode = request.getParameter("SbuCode");
        String recType = request.getParameter("recType");
        String exitPage="";
           String finacleTransId = approv.getGeneratedTransId();
           String raiseEntryNarration = taxdebitNarration;           
        //   String sbu_code = SbuCode+ "|" +SbuCode;
           String sbu_code = SbuCode+SbuCode;
           taxdebitNarration = SbuCode+"|"+SbuCode+"|"+taxdebitNarration;
        try
        {
        if (!userClass.equals("NULL") || userClass!=null){
        if(taxamount!=null)
        {
        taxamount = taxamount.replaceAll(",","");
        if (supervisor == "" || supervisor == null || supervisor == "*" || supervisor.equals("*")) {
   		 supervisor = "";
        }
        if(ad.isoChecking(   asset_id, page1, transactionId))
        {
        	 iso=bus.transferFund(taxdebitNarration,taxcreditAccount,taxdebitAccount ,Double.parseDouble(taxamount),finacleTransId,SbuCode);
        	// iso=bus.interaccounttransferFund(taxdebitNarration,taxcreditAccount,taxdebitAccount ,Double.parseDouble(taxamount),finacleTransId,SbuCode,SbuCode);
        	//iso="000";
        	 ad.updateRaiseEntryTransaction(   asset_id, page1, transactionId, iso,finacleTransId,"","");
        }
        else
        {
        raiseMan.raiseEntry(taxdebitAccount,taxcreditAccount,taxdebitAccType,taxcreditAccType,taxdebitTranCode
        		,taxcreditTranCode,taxdebitNarration,
        		taxcreditNarration,Double.parseDouble(taxamount), userId,batchId,cdate,supervisor,asset_id,type);
        		
         iso=bus.transferFund(taxdebitNarration,taxcreditAccount,taxdebitAccount ,Double.parseDouble(taxamount),finacleTransId,SbuCode);
         //iso=bus.interaccounttransferFund(taxdebitNarration,taxcreditAccount,taxdebitAccount ,Double.parseDouble(taxamount),finacleTransId,SbuCode,SbuCode);
        //iso="000";
         ad.insertRaiseEntryTransaction(userId,raiseEntryNarration,taxcreditAccount,taxdebitAccount ,Double.parseDouble(taxamount),iso,asset_id,page1,transactionId,finacleTransId,recType);
        }
         if(iso.equalsIgnoreCase("000"))
             {
     	   	 String reversalId = group_id.substring(0, 1);
     	     if(reversalId.equals("R")){group_id = group_id.substring(1, group_id.length());}
        	     if(!reversalId.equals("R")){group_id = "R"+group_id;}
                 String q = "update am_Raisentry_Transaction set iso = '-001' where asset_id='"+asset_id+"' and Trans_id = '"+group_id+"' and transactionId = "+transactionId+"";
                 ad.updateAssetStatusChange(q);	 
              //    System.out.println("#####################################################################  3");
         			msgText1 = message.MailMessage(mail_code, transaction_type)+", \n";
        			msgText1 += "For asset id  "+asset_id+"\n";
        			msgText1 += "For  "+taxdebitNarration+"\n";
        			msgText1 += "Debit Account  "+taxdebitAccount+"\n";
        			msgText1 += "Credit Account  "+taxcreditAccount+"\n";
        			msgText1 += "Amount  "+formata.formatAmount(taxamount)+"\n";
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
        	        	//Edit the above method
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
        	        	//Edit the above method to reflect what is needed . ayojava
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
    	//String transactionIdWitholding = "3";
    	String transactionIdVat = "25";
    	String processCost = ad.isoCheck(  asset_id, page1,transactionIdCost);
    	String processCost2 = ad.isoCheck( asset_id, page1,transactionIdVendor);
    	if (partPay.equalsIgnoreCase("Y"))
    	{
    		processCost2 = "000";
    	}
    	//String processWithTax = ad.isoCheck(asset_id, page1,transactionIdWitholding);
    	String processCreateVat = ad.isoCheck(asset_id, page1,transactionIdVat);
    	
    	System.out.println("processCost2 >>>>>> " + processCost2);
    	System.out.println("processCreateVat >>>>>> " + processCreateVat);
    	System.out.println("processCost >>>>>> " + processCost);
    	
    	if ((processCost2.equalsIgnoreCase("000"))&&  (processCreateVat.equalsIgnoreCase("000")) &&  (processCost.equalsIgnoreCase("000")))
    		{
    			helper.postAssetStatus(asset_id);	
    		}
    	
    	/*if ((processCreateVat.equalsIgnoreCase("000")) &&  (processCost.equalsIgnoreCase("000")))
		{
			helper.postAssetStatus(asset_id);	
		}*/
    	
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
    	if ((processCreateVat.equalsIgnoreCase("000")))
		{
			helper.postAssetStatus(asset_id);	
		}*/
				
    	helper.postStatus(group_id);		
		
	}
    private void processEntries3() 
    {
    	System.out.println("<<<<<<<<<<< TRYING TO PROCESS ENTRIES >>>>>>>>>>>>>>>>>");
    	//DO AN ISO CHECK FOR THE OTHER PROCESSES
    	String transactionIdCost = "1";
    	String transactionIdVendor = "2";
    	//String transactionIdWitholding = "3";
    	String processCost = ad.isoCheck(  asset_id, page1,transactionIdCost);
    	String processCost2 = ad.isoCheck( asset_id, page1,transactionIdVendor);
    	if (partPay.equalsIgnoreCase("Y"))
    	{
    		processCost2 = "000";
    	}
    	//String processWithTax = ad.isoCheck(  asset_id, page1,transactionIdWitholding);
    	System.out.println("processCost2 >>>>>> " + processCost2);
    	System.out.println("processCost >>>>>> " + processCost);
    	
    	if ((processCost2.equalsIgnoreCase("000"))&&  (processCost.equalsIgnoreCase("000")))
    		{
    			helper.postAssetStatus(asset_id);	
    		}
    	
    	/*if  (processCost.equalsIgnoreCase("000"))
		{
			helper.postAssetStatus(asset_id);	
		}*/
		
    	helper.postStatus(group_id);		
	}
    private void processEntries4() 
    {
    	/*
    	System.out.println("<<<<<<<<<<< TRYING TO PROCESS ENTRIES >>>>>>>>>>>>>>>>>");
    	//DO AN ISO CHECK FOR THE OTHER PROCESSES
    	String transactionIdCost = "1";
    	String transactionIdVendor = "2";
    	String transactionIdWitholding = "3";
    	String processCost = ad.isoCheck(  asset_id, page1,transactionIdCost);
    	String processCost2 = ad.isoCheck( asset_id, page1,transactionIdVendor);
    	String processWithTax = ad.isoCheck(  asset_id, page1,transactionIdWitholding);
    	System.out.println("processCost2 >>>>>> " + processCost2);
    	System.out.println("processCost >>>>>> " + processCost);
    	System.out.println("processWithTax >>>>>> " + processWithTax);
    	
    	if ((processCost2.equalsIgnoreCase("000"))&&  (processCost.equalsIgnoreCase("000")))
    		{
		    	String upd_Asset_Status="update am_group_asset set post_flag='D' where asset_id='" + asset_id+"'";
		    	System.out.println("upd_Asset_Status Query >>>> " + upd_Asset_Status);
				adGroup.updateStatusUtil(upd_Asset_Status);
    		}
		
		String chk_asset_count="select count(*) from am_group_asset where group_id=" + group_id;
		System.out.println("chk_asset_count Query >>>> " + chk_asset_count);
    	String chk_asset_processed_count="select count(*) from am_group_asset where post_flag='D' and group_id="+ group_id;
    	System.out.println("chk_asset_processed_count Query >>>> " + chk_asset_processed_count);
    	
    	int asset_cnt=Integer.parseInt(approv.getCodeName(chk_asset_count));
    	System.out.println("asset_cnt Result >>>> " + asset_cnt);
    	int asset_proc_cnt= Integer.parseInt(approv.getCodeName(chk_asset_processed_count));
    	System.out.println("asset_proc_cnt Result >>>> " + asset_proc_cnt);
    	if (asset_cnt == asset_proc_cnt)
    	{
    		String upd_raise_entry_flag="update am_raisentry_post set entrypostflag='Y' where id="+group_id+" " +
    				"and page='" + "ASSET GROUP CREATION RAISE ENTRY".trim()+"'";
    		System.out.println("upd_raise_entry_flag Query >>>> " + upd_raise_entry_flag);
    		adGroup.updateStatusUtil(upd_raise_entry_flag);
    	}*/
		
	}
    public String getServletInfo()
    {
        return "Process Action Servlet";
    }
}