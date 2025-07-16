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
import com.magbel.util.ApplicationHelper;
import com.magbel.util.CurrencyNumberformat;
import com.magbel.util.PostingHelper;

public class GroupAssetToAssetProcess extends HttpServlet
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
	String exitPage="";
	String processType="";
	String iso_Status_qry="";
	String transType = "";
	
    public GroupAssetToAssetProcess()
    {
    	   exitPage="raiseGroupAssetToAssetDetail6";
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
        String userId =(String)session.getAttribute("CurrentUser");
        asset_id = request.getParameter("asset_id").trim();
        group_id = request.getParameter("group_id");
        processType =   request.getParameter("type"); 
        transType =   request.getParameter("transType"); 
        int pgNum=Integer.parseInt(request.getParameter("pageNum"));
        
		Properties prop = new Properties();
        File file = new File("C:\\Property\\LegendPlus.properties");
        FileInputStream input = new FileInputStream(file);
        prop.load(input);

        String ThirdPartyLabel = prop.getProperty("ThirdPartyLabel");
       // System.out.println("Asset_ID >>>>>> " + asset_id);
       // System.out.println("group_id >>>>>> " + group_id);
       // System.out.println("processType >>>>>>>>>  " + processType);
       // System.out.println("status >>>>>>>>>  " + status);
        
        String iso;
        String msgText1 ="";
        String to ="";
        String mail_code="1";
        String subject = "Raise Entry";
        String transaction_type="Raise_Entry";
        String batchId = "RE"+Integer.toString((int)(Math.random()*8000)+1000);
        String cdate = sdf.format(new Date());
        int assetCode = request.getParameter("assetCode") == null?0:Integer.parseInt(request.getParameter("assetCode"));
        String  systemIp = request.getRemoteAddr() ;

        iso_Status_qry="select description from am_error_description where error_code='";
        if (processType.equalsIgnoreCase("cost"))
        {
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
        String costdebitAcctNo = costdebitAccount.substring(8,16);
        String costcreditAcctNo = costcreditAccount.substring(8,16);
//        if(costdebitAccount.substring(0,2)=="NGN"){costdebitAcctNo = costdebitAccount.substring(8,16);}
//        if(costcreditAccount.substring(0,2)=="NGN"){costcreditAcctNo = costcreditAccount.substring(8,16);}    
        if(ThirdPartyLabel.equalsIgnoreCase("INTEGRIFY")){costdebitAcctNo = costdebitAccount.substring(8,16);}
        if(ThirdPartyLabel.equalsIgnoreCase("K2")){costdebitAcctNo = costdebitAccount.substring(6,14);}
        if(ThirdPartyLabel.equalsIgnoreCase("INTEGRIFY")){costcreditAcctNo = costcreditAccount.substring(8,16);}
        if(ThirdPartyLabel.equalsIgnoreCase("K2")){costcreditAcctNo = costcreditAccount.substring(6,14);}        
        String costamount = request.getParameter("cost_amount");
        costamount = costamount.replaceAll(",","");
        String supervisor = request.getParameter("supervisor");
        String type="Asset Creation";
        String transactionId = request.getParameter("transactionIdCost");
        String SbuCode = request.getParameter("SbuCode");
        String oldSbuCode = request.getParameter("oldSbuCode");
        String tranId = request.getParameter("tranId");
        String reversal = tranId.substring(1,1);
        String recType = request.getParameter("recType");
        String finacleTransId = approv.getGeneratedTransId();
        String sbu_code = SbuCode+ "|" +oldSbuCode;
        System.out.println("SbuCode: "+SbuCode+"    oldSbuCode: "+oldSbuCode+"    tranId: "+tranId);
        costdebitNarration = SbuCode+"|"+oldSbuCode+"|"+costdebitNarration;
       // costcreditAccount = SbuCode+"|"+oldSbuCode+"|"+costcreditAccount;
     //   System.out.println("costdebitNarration: "+costdebitNarration+"    costcreditAccount: "+costcreditAccount);
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
     
     String branchCode = approv.getCodeName("select Branch_Code from am_asset where Asset_code = "+assetCode+" ");
     to = approv.getCodeName("select email from am_ad_branch where Branch_code = '"+branchCode+"' ");
        try
        {
        	 if (supervisor == "" || supervisor == null || supervisor == "*" || supervisor.equals("*")) {
        		 supervisor = "";
             }
        //	if(ad.isoChecking(  asset_id, page1, transactionId))
        	 if(ad.isoChecking(  asset_id, page1, transactionId,tranId))
        	{
        		iso=bus.transferFund(costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),finacleTransId,SbuCode);
        		//iso=bus.interaccounttransferFund(costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),finacleTransId,SbuCode,SbuCode);
        		////iso="000";
      //  		ad.updateRaiseEntryTransaction(  asset_id, page1, transactionId, iso,finacleTransId,"","");
        		ad.updateRaiseEntryTransaction( asset_id, page1, transactionId, iso,systemIp,tranId,finacleTransId,"","");
        	}
        	else
        	{
               raiseMan.raiseEntry(costdebitAccount,costcreditAccount,costdebitAccType,costcreditAccType,costdebitTranCode
        		,costcreditTranCode,costdebitNarration,
        		costcreditNarration,Double.parseDouble(costamount), userId,batchId,cdate,supervisor,asset_id,type);
        		
              iso=bus.transferFund(costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),finacleTransId,SbuCode);
              //iso=bus.interaccounttransferFund(costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),finacleTransId,SbuCode,SbuCode);
              // iso="000";
 //             String tranId =  new ApplicationHelper().getGeneratedId("am_asset_approval");
              ad.insertRaiseEntryTransaction(userId,costdebitNarration,costcreditAccount,costdebitAccount,Double.parseDouble(costamount),iso,asset_id,page1,transactionId,systemIp,tranId,assetCode,finacleTransId,costdebitAcctName,costcreditAcctName,userId,recType);
              
        	}
        	String iso_status=approv.getCodeName(iso_Status_qry + iso +"'");
              if(iso.equalsIgnoreCase("000"))
              {
            	    processEntries_Cost();
            	    if(transType.equalsIgnoreCase("P")){
            		ad.updateAssetStatusChange("update am_Raisentry_Transaction set ISO = '' where asset_id='"+asset_id+"' "
            	    + "and page1 = 'ASSET CREATION RAISE ENTRY' and substring(trans_id,1,1) = 'R'");
            	    }
            	    if(transType.equalsIgnoreCase("R")){
            		ad.updateAssetStatusChange("update am_Raisentry_Transaction set ISO = '' where asset_id='"+asset_id+"' "
            	    + "and page1 = 'ASSET CREATION RAISE ENTRY' and substring(trans_id,1,1) != 'R'");
            		ad.updateAssetStatusChange("update AM_ASSET set ASSET_STATUS = 'PENDING' where ASSET_ID='"+asset_id+"' ");
            	    }
                  	msgText1 = message.MailMessage(mail_code, transaction_type)+", \n";
        			msgText1 += "For Asset Id  "+asset_id+"\n";
        	        msgText1 += "For  "+costdebitNarration+"\n";
        			msgText1 += "Debit Account  "+costdebitAccount+"\n";
        			msgText1 += "Credit Account  "+costcreditAccount+"\n";
        			msgText1 += "Amount  "+formata.formatAmount(costamount)+"\n";
        			msgText1 += "Login here http://172.27.11.192:8080/legendPlus.net" ;
        			/*****COMMENTED FOR TESTING******/
          			mail.sendMail(to,subject,msgText1);
        			/**********************************/
        			   	             
        	        status = approv.getCodeName("select post_flag from AM_GROUP_ASSET where asset_id='" + asset_id+"'");
        	        out.println("<script>alert('" + iso_status +"!')</script>");
        			//System.out.println("exitPage >>>> " + exitPage);
        			//System.out.println("status >>>> " + status);
        			//out.print("<script>window.close('" + exitPage+".jsp');</script>");	
        			String exitLink="'DocumentHelp.jsp?np=groupAssetToAssetPosting&gid="+ group_id+"&root=group&id="+ asset_id+"&status="+status+"&assetCode="+assetCode+"'";
        			//System.out.println("exitLink >>>> " + exitLink);
        			out.println("<script>");
      			  	out.println("window.location='DocumentHelp.jsp?np=groupAssetToAssetPosting&gid="+ group_id+"&root=group&id="+ asset_id+"&status="+status+"&assetCode="+assetCode+"'" );
      			  	out.println("</script>");
        		}
              else
              {
            	  out.println("<script>alert('" + iso_status +"!')</script>");
	             // System.out.println("exitPage >>>> " + exitPage);
	              out.println("<script>");
    			  out.println("window.location='DocumentHelp.jsp?np=groupAssetToAssetPosting&gid="+ group_id+"&root=group&id="+ asset_id+"&status="+status+"&assetCode="+assetCode+"'" );
    			  out.println("</script>");
              }
         }
  
        catch(Exception e)
        {
        	e.printStackTrace();	
        }
      }
     if (this.processType.equalsIgnoreCase("costBranch"))
        {
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
 //       String costdebitAcctNo = costdebitAccount.substring(8,16);
 //       String costcreditAcctNo = costcreditAccount.substring(8,16);
//        if(costdebitAccount.substring(0,2)=="NGN"){costdebitAcctNo = costdebitAccount.substring(8,16);}
//        if(costcreditAccount.substring(0,2)=="NGN"){costcreditAcctNo = costcreditAccount.substring(8,16);}
        String costdebitAcctNo = "";
        String costcreditAcctNo = ""; 
        if(ThirdPartyLabel.equalsIgnoreCase("INTEGRIFY")){costdebitAcctNo = costdebitAccount.substring(8,16);}
        if(ThirdPartyLabel.equalsIgnoreCase("K2")){costdebitAcctNo = costdebitAccount.substring(6,14);}
        if(ThirdPartyLabel.equalsIgnoreCase("INTEGRIFY")){costcreditAcctNo = costcreditAccount.substring(8,16);}
        if(ThirdPartyLabel.equalsIgnoreCase("K2")){costcreditAcctNo = costcreditAccount.substring(6,14);}  
        String costamount = request.getParameter("cost_amount");
        costamount = costamount.replaceAll(",","");
        String supervisor = request.getParameter("supervisor");
        String type="Asset Creation";
        String transactionId = request.getParameter("transactionIdCost");
        String SbuCode = request.getParameter("SbuCode");
        String oldSbuCode = request.getParameter("oldSbuCode");
        String tranId = request.getParameter("tranId");
        String recType = request.getParameter("recType");
         String finacleTransId = approv.getGeneratedTransId();
         String sbu_code = SbuCode+ "|" +SbuCode;
         costdebitNarration = SbuCode+"|"+oldSbuCode+"|"+costdebitNarration;
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
        String branchCode = approv.getCodeName("select Branch_Code from am_asset where Asset_code = "+assetCode+" ");
        to = approv.getCodeName("select email from am_ad_branch where Branch_code = '"+branchCode+"' ");

        try
        {
        	 if (supervisor == "" || supervisor == null || supervisor == "*" || supervisor.equals("*")) {
        		 supervisor = "";
             }
        		 costdebitNarration = costcreditNarration;
 //       	 System.out.println("=====>>>>>costdebitNarration: "+costdebitNarration+"    costcreditNarration: "+costcreditNarration);
 //       	if(ad.isoChecking(  asset_id, page1, transactionId))
        	 if(ad.isoChecking(  asset_id, page1, transactionId,tranId))
        	{
        		iso=bus.transferFund(costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),finacleTransId,sbu_code);
        		//iso=bus.interaccounttransferFund(costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),finacleTransId,SbuCode,SbuCode);
        		////iso="000";
        	//	ad.updateRaiseEntryTransaction(  asset_id, page1, transactionId, iso,finacleTransId,"","");
        		ad.updateRaiseEntryTransaction( asset_id, page1, transactionId, iso,systemIp,tranId,finacleTransId,"","");
        	}
        	else
        	{
               raiseMan.raiseEntry(costdebitAccount,costcreditAccount,costdebitAccType,costcreditAccType,costdebitTranCode
        		,costcreditTranCode,costdebitNarration,
        		costcreditNarration,Double.parseDouble(costamount), userId,batchId,cdate,supervisor,asset_id,type);

              iso=bus.transferFund(costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),finacleTransId,sbu_code);
              //iso=bus.interaccounttransferFund(costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),finacleTransId,SbuCode,SbuCode);
              // iso="000";
//              String tranId =  new ApplicationHelper().getGeneratedId("am_asset_approval");            
              ad.insertRaiseEntryTransaction(userId,costdebitNarration,costcreditAccount,costdebitAccount,Double.parseDouble(costamount),iso,asset_id,page1,transactionId,systemIp,tranId,assetCode,finacleTransId,costdebitAcctName,costcreditAcctName,userId,recType);

        	}
        	String iso_status=approv.getCodeName(iso_Status_qry + iso +"'");
              if(iso.equalsIgnoreCase("000"))
              {
            	    processEntries_Cost();

                  	msgText1 = message.MailMessage(mail_code, transaction_type)+", \n";
        			msgText1 += "For Asset Id  "+asset_id+"\n";
        	        msgText1 += "For  "+costdebitNarration+"\n";
        			msgText1 += "Debit Account  "+costdebitAccount+"\n";
        			msgText1 += "Credit Account  "+costcreditAccount+"\n";
        			msgText1 += "Amount  "+formata.formatAmount(costamount)+"\n";
        			msgText1 += "Login here http://172.27.11.192:8080/legend2.net" ;
        			/*****COMMENTED FOR TESTING******/
          			mail.sendMail(to,subject,msgText1);
        			/**********************************/

        	        status = approv.getCodeName("select post_flag from AM_GROUP_ASSET where asset_id='" + asset_id+"'");
        	        out.println("<script>alert('" + iso_status +"!')</script>");
        			//System.out.println("exitPage >>>> " + exitPage);
        			//System.out.println("status >>>> " + status);
        			//out.print("<script>window.close('" + exitPage+".jsp');</script>");
        			String exitLink="'DocumentHelp.jsp?np=groupAssetToAssetPostingBranch&gid="+ group_id+"&root=group&id="+ asset_id+"&status="+status+"&assetCode="+assetCode+"'";
        			//System.out.println("exitLink >>>> " + exitLink);
        			out.println("<script>");
      			  	out.println("window.location='DocumentHelp.jsp?np=groupAssetToAssetPostingBranch&gid="+ group_id+"&root=group&id="+ asset_id+"&status="+status+"&assetCode="+assetCode+"'" );
      			  	out.println("</script>");
        		}
              else
              {
            	  out.println("<script>alert('" + iso_status +"!')</script>");
	             // System.out.println("exitPage >>>> " + exitPage);
	              out.println("<script>");
    			  out.println("window.location='DocumentHelp.jsp?np=groupAssetToAssetPostingBranch&gid="+ group_id+"&root=group&id="+ asset_id+"&status="+status+"&assetCode="+assetCode+"'" );
    			  out.println("</script>");
              }
         }

        catch(Exception e)
        {
        	e.printStackTrace();
        }
      }
        if (this.processType.equalsIgnoreCase("tax"))
        {
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
//            String costdebitAcctNo = taxdebitAccount.substring(8,16);
//            String costcreditAcctNo = taxcreditAccount.substring(8,16);
            String costdebitAcctNo = "";
            String costcreditAcctNo = "";            
            if(ThirdPartyLabel.equalsIgnoreCase("INTEGRIFY")){costdebitAcctNo = taxdebitAccount.substring(8,16);}
            if(ThirdPartyLabel.equalsIgnoreCase("K2")){costdebitAcctNo = taxdebitAccount.substring(6,14);}
            if(ThirdPartyLabel.equalsIgnoreCase("INTEGRIFY")){costcreditAcctNo = taxcreditAccount.substring(8,16);}
            if(ThirdPartyLabel.equalsIgnoreCase("K2")){costcreditAcctNo = taxcreditAccount.substring(6,14);}             
            String taxamount = request.getParameter("wh_amount");
            taxamount = taxamount.replaceAll(",","");
            String supervisor = request.getParameter("supervisor");
            String type="Asset Creation";
            String transactionId = request.getParameter("transactionIdWitholding");
            String SbuCode = request.getParameter("SbuCode");
            String OldSbuCode = request.getParameter("SbuCode");
            String tranId = request.getParameter("tranId");
            String finacleTransId = approv.getGeneratedTransId();
            String raiseEntryNarration = taxdebitNarration;
            String sbu_code = SbuCode+ "|" +SbuCode;
            taxdebitNarration = SbuCode+"|"+OldSbuCode+"|"+taxdebitNarration;
            String costdebitAcctName = approv.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+costdebitAcctNo+"'");
            String costcreditAcctName = approv.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+costcreditAcctNo+"'");
            String branchCode = approv.getCodeName("select Branch_Code from am_asset where Asset_code = "+assetCode+" ");
            to = approv.getCodeName("select email from am_ad_branch where Branch_code = '"+branchCode+"' ");

        try
        {
        	 if (supervisor == "" || supervisor == null || supervisor == "*" || supervisor.equals("*")) {
        		 supervisor = "";
             } 
 //       	if(ad.isoChecking(  asset_id, page1, transactionId))
        	 if(ad.isoChecking(  asset_id, page1, transactionId,tranId))
        	{
        		 iso=bus.transferFund(taxdebitNarration,taxcreditAccount,taxdebitAccount ,Double.parseDouble(taxamount),finacleTransId,SbuCode);
        		// iso=bus.interaccounttransferFund(taxdebitNarration,taxcreditAccount,taxdebitAccount ,Double.parseDouble(taxamount),finacleTransId,SbuCode,OldSbuCode);
        		//iso="000";
        	//	ad.updateRaiseEntryTransaction(  asset_id, page1, transactionId, iso,finacleTransId,"","");
        		ad.updateRaiseEntryTransaction( asset_id, page1, transactionId, iso,systemIp,tranId,finacleTransId,"","");
        	}
        	else
        	{
        		raiseMan.raiseEntry(taxdebitAccount,taxcreditAccount,taxdebitAccType,taxcreditAccType,taxdebitTranCode
                		,taxcreditTranCode,taxdebitNarration,
                		taxcreditNarration,Double.parseDouble(taxamount), userId,batchId,cdate,supervisor,asset_id,type);
        		
               iso=bus.transferFund(taxdebitNarration,taxcreditAccount,taxdebitAccount ,Double.parseDouble(taxamount),finacleTransId,SbuCode);
               //iso=bus.interaccounttransferFund(taxdebitNarration,taxcreditAccount,taxdebitAccount ,Double.parseDouble(taxamount),finacleTransId,SbuCode,OldSbuCode);
              // iso="000";
               ad.insertRaiseEntryTransaction(userId,raiseEntryNarration,taxcreditAccount,taxdebitAccount ,Double.parseDouble(taxamount),iso,asset_id,page1,transactionId,assetCode,finacleTransId,costdebitAcctName,costcreditAcctName,userId);
        	}
        	String iso_status=approv.getCodeName(iso_Status_qry + iso +"'");
              if(iso.equalsIgnoreCase("000"))
              {
            	    processEntries_tax();
            	    
            	    msgText1 = message.MailMessage(mail_code, transaction_type)+", \n";
        			msgText1 += "For asset id  "+asset_id+"\n";
        			msgText1 += "For  "+taxdebitNarration+"\n";
        			msgText1 += "Debit Account  "+taxdebitAccount+"\n";
        			msgText1 += "Credit Account  "+taxcreditAccount+"\n";
        			msgText1 += "Amount  "+formata.formatAmount(taxamount)+"\n";
        			msgText1 += "Login here http://172.27.11.192:8080/legend2.net" ;
        			
        			/*****COMMENTED FOR TESTING******/
          			mail.sendMail(to,subject,msgText1);
        			/**********************************/
        			   	             
        	        status = approv.getCodeName("select post_flag from AM_GROUP_ASSET where asset_id='" + asset_id+"'");
        	        out.println("<script>alert('" + iso_status +"!')</script>");
        			//System.out.println("exitPage >>>> " + exitPage);
        			//System.out.println("status >>>> " + status);
        			//out.print("<script>window.close('" + exitPage+".jsp');</script>");	
        			String exitLink="'DocumentHelp.jsp?np=groupAssetToAssetPosting&gid="+ group_id+"&root=group&id="+ asset_id+"&status="+status+"'";
        			//System.out.println("exitLink >>>> " + exitLink);
        			out.println("<script>");
      			  	out.println("window.location='DocumentHelp.jsp?np=groupAssetToAssetPosting&gid="+ group_id+"&root=group&id="+ asset_id+"&status="+status+"'" );
      			  	out.println("</script>");
        		}
              else
              {
            	  out.println("<script>alert('" + iso_status +"!')</script>");
	              //System.out.println("exitPage >>>> " + exitPage);
	              out.println("<script>");
    			  out.println("window.location='DocumentHelp.jsp?np=groupAssetToAssetPosting&gid="+ group_id+"&root=group&id="+ asset_id+"&status="+status+"'" );
    			  out.println("</script>");
              }
         }
        catch(Exception e)
        {
        	e.printStackTrace();	
        }
      }       
    }
    
    private void processEntries_Cost() 
    {
    	System.out.println("<<<<<<<<<<< TRYING TO PROCESS ENTRIES FOR COST >>>>>>>>>>>>>>>>>");
    	String transactionIdWitholding = "25";
    	String processWithTax = ad.isoCheck( asset_id, page1,transactionIdWitholding);
    	//System.out.println("processWithTax >>>>>> " + processWithTax);
    	processWithTax="000";//Since we do not process witholding tax for individual assets--ayojava-18_08_09
    	if (processWithTax.equalsIgnoreCase("000"))
    		{
    			helper.postAssetStatus(asset_id);	
    		}
       	helper.postStatus(group_id);
	}

    private void processEntries_tax() 
    {
    	System.out.println("<<<<<<<<<<< TRYING TO PROCESS ENTRIES FOR TAX >>>>>>>>>>>>>>>>>");
    	String transactionIdCost = "24";
    	String processCost = ad.isoCheck( asset_id, page1,transactionIdCost);
    	//System.out.println("processCost >>>>>> " + processCost);
    	
    	if (processCost.equalsIgnoreCase("000"))
    		{
    			helper.postAssetStatus(asset_id);	
    		}
       	helper.postStatus(group_id);
	}
  
	public String getServletInfo()
    {
        return "GroupAssetToAssetProcess Servlet";
    }
}