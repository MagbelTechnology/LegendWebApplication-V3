package com.magbel.legend.servlet;



import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

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

public class GroupProcess extends HttpServlet
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
	String page1 = "ASSET GROUP CREATION RAISE ENTRY";
	String partPay="";
	String deferPay="";
	String iso_Status_qry="";
	
    public GroupProcess()
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
        {
        	et.printStackTrace();
        }
    }
    public void destroy()
    {
    }
       
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
       
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
                
        String userId =(String)session.getAttribute("CurrentUser");
        
       // asset_id = request.getParameter("asset_id").trim();
        group_id = request.getParameter("group_id");       
        partPay = request.getParameter("partPay");
        //deferPay = request.getParameter("deferPay");
        //processCost = request.getParameter("processCost").trim();
        String systemIp = request.getRemoteAddr() ;
        int pgNum=Integer.parseInt(request.getParameter("pageNum"));
        String SbuCode = request.getParameter("SbuCode");   
        String vendorname = request.getParameter("vendorname");
        String invNo = request.getParameter("invNo");
        String id = request.getParameter("group_id");
        String vendorId = request.getParameter("vendorId");
        String location = request.getParameter("location");
        String projectCode = request.getParameter("projectCode");	        
        String categoryCode = request.getParameter("categoryCode");
        int assetCode = request.getParameter("assetCode") == null?0:Integer.parseInt(request.getParameter("assetCode"));
       String  tranID = request.getParameter("tranID");// == null?0:Integer.parseInt(request.getParameter("tranID"));
        System.out.println("tranID in classs >>>>>> " + tranID);
        String userClass = (String) session.getAttribute("UserClass");
  //      System.out.println("vendorname >>>>>>>>>  " + vendorname);
  //      System.out.println("invNo >>>>>>>>>  " + invNo);
  //      System.out.println("<<<<<<<<<< SbuCode >>>>>>>>>  " + SbuCode);
          
        String iso;
        String msgText1 ="";
      
        String to ="";
        String mail_code="1";
        String subject = "Raise Entry";
        String transaction_type="Raise_Entry";
        String batchId = "RE"+Integer.toString((int)(Math.random()*8000)+1000);
        String cdate = sdf.format(new Date());
        iso_Status_qry="select description from am_error_description where error_code='";
        /**
        Post Cost Transaction
        */
        String Subheading = "This is to kindly inform you of the payment that has been credited into your account for execution of the job commissioned by FCMB to your company.";
		String summary1 = "For clarification on the above transaction, kindly send an email to Adminpayment@firstcitygroup.com or call  07098000779.";
		String summary2 = "Thank you.";
		String summary3 = "For: First City Monument Bank Plc";         
	if (!userClass.equals("NULL") || userClass!=null){
    if (pgNum==1)
     {
//    	System.out.println("<<<<<<<<<<<<< Process Type Cost Account >>>>>>>>>>> 1");
    	
        String costdebitAccount = request.getParameter("cost_dr_account");
       // System.out.println("costdebitAccount >>>>>> " + costdebitAccount );
        
        String costdebitAccType = request.getParameter("cost_dr_accttype");
        //System.out.println("costdebitAccType >>>>>> " + costdebitAccType );
        
        String costdebitTranCode = request.getParameter("cost_dr_trancode");
       // System.out.println("costdebitTranCode >>>>>> " + costdebitTranCode );
        
        String costdebitNarration = request.getParameter("cost_dr_narration");
       // System.out.println("costdebitNarration >>>>>> " + costdebitNarration );
        
        String costcreditAccount = request.getParameter("cost_cr_account");
        //System.out.println("costcreditAccount >>>>>> " + costcreditAccount );
        
        String costcreditAccType = request.getParameter("cost_cr_accttype");
       // System.out.println("costcreditAccType >>>>>> " + costcreditAccType );
        
        String costcreditTranCode = request.getParameter("cost_cr_trancode");
       // System.out.println("costcreditTranCode >>>>>> " + costcreditTranCode );
        
        String costcreditNarration = request.getParameter("cost_cr_narration");
       // System.out.println("costcreditNarration >>>>>> " + costcreditNarration );
        
        String costamount = request.getParameter("cost_amount");
       // System.out.println("costamount >>>>>> " + costamount );
        
        costamount = costamount.replaceAll(",","");
       // System.out.println("costdebitAccount >>>>>> " + costdebitAccount );
        
        String supervisor = request.getParameter("supervisor");
       // System.out.println("supervisor >>>>>> " + supervisor );
 //       String costdebitAcctNo = costdebitAccount.substring(8,16);
//        String costcreditAcctNo = costcreditAccount.substring(8,16);
        String costdebitAcctNo = "";
        String costcreditAcctNo = "";        
        if(costdebitAccount.substring(0,2)=="NGN"){costdebitAcctNo = costdebitAccount.substring(8,16);}
        if(costcreditAccount.substring(0,2)=="NGN"){costcreditAcctNo = costcreditAccount.substring(8,16);}
        if(costdebitAccount.substring(2,5).equals("NGN")){costdebitAcctNo = costdebitAccount.substring(6,14);}
        if(costcreditAccount.substring(2,5).equals("NGN")){costcreditAcctNo = costcreditAccount.substring(6,14);}        
        String type="Group Asset Creation";
        String transactionId = request.getParameter("transactionIdCost");
        String recType = request.getParameter("recType");
       // System.out.println("transactionId >>>>>> " + transactionId );        
//        System.out.println("SbuCode in GroupProcess >>>>>> " + SbuCode+"   costdebitAcctNo: "+costdebitAcctNo+"   costcreditAcctNo: "+costcreditAcctNo );
         String finacleTransId = approv.getGeneratedTransId();
        String exitPage="";
        //String sbu_code = SbuCode+ "|" +SbuCode;
        String sbu_code = SbuCode+SbuCode;
        costdebitNarration = SbuCode+"|"+SbuCode+"|"+costdebitNarration;
        String costdebitAcctName = approv.getCodeName("select category_name from am_ad_category where CATEGORY_CODE = '"+categoryCode+"' AND Asset_Ledger = '"+costdebitAcctNo+"'");
        String costcreditAcctName = approv.getCodeName("select category_name from am_ad_category where CATEGORY_CODE = '"+categoryCode+"' AND Asset_Ledger = '"+costcreditAcctNo+"'");
//        System.out.println("costdebitAcctName >>>>>> "+costdebitAcctName);
        String [] idSplit = id.split("/");
        
        String test10 = "select category_name from am_ad_category where CATEGORY_CODE = '"+categoryCode+"' AND Asset_Ledger = '"+costdebitAcctNo+"'";
        String test11 = "select category_name from am_ad_category where CATEGORY_CODE = '"+categoryCode+"' AND ACCUM_DEP_LEDGER = '"+costdebitAcctNo+"' ";
        String test12 = "select category_name from am_ad_category where CATEGORY_CODE = '"+categoryCode+"' AND DEP_LEDGER = '"+costdebitAcctNo+"' ";
        String test13 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+costdebitAcctNo+"' ";
        String test14 = "select VENDOR_NAME from am_gb_company where asset_acq_ac = '"+costdebitAcctNo+"' ";
        costdebitAcctName = approv.getCodeName(test10);
//        System.out.println("Vendor Name querry of test13 >>>>>> "+test13);
        
        if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = approv.getCodeName(test11);}
        if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = approv.getCodeName(test12);}
        if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = approv.getCodeName(test13);}
        if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = approv.getCodeName(test14);}
        
        String test20 = "select category_name from am_ad_category where CATEGORY_CODE = '"+categoryCode+"' AND Asset_Ledger = '"+costcreditAcctNo+"' ";
        String test21 = "select category_name from am_ad_category where CATEGORY_CODE = '"+categoryCode+"' AND ACCUM_DEP_LEDGER = '"+costcreditAcctNo+"' ";
        String test22 = "select category_name from am_ad_category where CATEGORY_CODE = '"+categoryCode+"' AND DEP_LEDGER = '"+costcreditAcctNo+"'";
        String test23 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+costcreditAcctNo+"' ";
        String test24 = "select VENDOR_NAME from am_gb_company where asset_acq_ac = '"+costcreditAcctNo+"' ";
        costcreditAcctName = approv.getCodeName(test20);
        
//        System.out.println("Vendor Name querry of test23 >>>>>> "+test23);
        if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test21);}
        if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test22);}
        if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test23);}
        if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test24);}
        try
        {      
        	if (supervisor == "" || supervisor == null || supervisor == "*" || supervisor.equals("*")) 
        	{
        		supervisor = "";
            }
        	if(ad.isoChecking(  group_id, page1, transactionId,tranID))
        	{  
        		iso=bus.transferFund(costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),finacleTransId,SbuCode);
        		//iso=bus.interaccounttransferFund(costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),finacleTransId,SbuCode,SbuCode);
        		//iso="000";
//        		System.out.println("<<<<<<<< ISO >>>>>> "+iso);
        		ad.updateRaiseEntryTransaction(  group_id, page1, transactionId, iso,finacleTransId,"","");
        	}
        	else
        	{
               raiseMan.raiseEntry(costdebitAccount,costcreditAccount,costdebitAccType,costcreditAccType,costdebitTranCode
        		,costcreditTranCode,costdebitNarration,
        		costcreditNarration,Double.parseDouble(costamount), userId,batchId,cdate,supervisor,group_id,type);
        		
              iso=bus.transferFund(costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),finacleTransId,SbuCode);
              //iso=bus.interaccounttransferFund(costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),finacleTransId,SbuCode,SbuCode);
             //  iso="000";
//              System.out.println("costcreditAcctName >>>>>> "+costcreditAcctName);
//              System.out.println("====== ISO >>>>>> "+iso);
              ad.insertRaiseEntryTransaction(userId,costdebitNarration,costdebitAccount,costcreditAccount,Double.parseDouble(costamount),iso,group_id,page1,transactionId,systemIp,tranID,assetCode,finacleTransId,costdebitAcctName,costcreditAcctName,userId,recType);
        	}
        	String iso_status=approv.getCodeName(iso_Status_qry + iso +"'");
        	
            if(iso.equalsIgnoreCase("000"))
            {
        	   	 String reversalId = tranID.substring(0, 1);
        	     if(reversalId.equals("R")){tranID = tranID.substring(1, tranID.length());}
//           	     if(!reversalId.equals("R")){tranID = "R"+tranID;}
                    String q = "update am_Raisentry_Transaction set iso = '-001' where asset_id='"+id+"' and Trans_id = '"+tranID+"' and transactionId = "+transactionId+"";
                    ad.updateAssetStatusChange(q);	      
               	msgText1 = message.MailMessage(mail_code, transaction_type)+", \n";
    			msgText1 += "For Asset Id  "+asset_id+"\n";
    	        msgText1 += "For  "+costdebitNarration+"\n";
    			msgText1 += "Debit Account  "+costdebitAccount+"\n";
    			msgText1 += "Credit Account  "+costcreditAccount+"\n";
    			msgText1 += "Amount  "+formata.formatAmount(costamount)+"\n";
    			msgText1 += "Login here http://172.27.11.192:8080/legendPlus.net" ;
    			/*****COMMENTED FRO TESTING******/
      			mail.sendMail(to,subject,msgText1);
    			/**********************************/
    			
    			out.println("<script>alert('" + iso_status +"!')</script>");
    			out.println("<script>");
				out.println("window.location='DocumentHelp.jsp?np=groupAssetPosting&id="+group_id+"&pageDirect=Y&tranId="+tranID+"&assetCode="+assetCode+"'" );
				out.println("</script>");
            }
            else
            {
            	out.println("<script>alert('" + iso_status +"!')</script>");
        	  out.println("<script>");
			//  out.println("window.location='DocumentHelp.jsp?np=groupAssetPosting&id="+ group_id+"'" );
			out.println("window.location='DocumentHelp.jsp?np=groupAssetPosting&id="+group_id+"&pageDirect=Y&tranId="+tranID+"&assetCode="+assetCode+"'" );
                          out.println("</script>");
            }
       	  }
        catch(Exception e)
        {
        	e.printStackTrace();	
        }
      }
        
    if (pgNum==2)
    {
    //	System.out.println("<<<<<<<<<<<<< Process Type Vendor Account >>>>>>>>>>> 2");
	/**
    Post Vendor Transaction
    */
    String vendordebitAccount = request.getParameter("vendor_dr_account");
   // System.out.println("vendordebitAccount >>>>>> " + vendordebitAccount );
    
    String vendordebitAccType = request.getParameter("vendor_dr_accttype");
   // System.out.println("vendordebitAccType >>>>>> " + vendordebitAccType );
    
    String vendordebitTranCode = request.getParameter("vendor_dr_trancode");
   // System.out.println("vendordebitTranCode >>>>>> " + vendordebitTranCode );
    
    String vendordebitNarration = request.getParameter("vendor_dr_narration");
  //  System.out.println("vendordebitNarration >>>>>> " + vendordebitNarration );
    
    String vendorcreditAccount = request.getParameter("vendor_cr_account");
   // System.out.println("vendorcreditAccount >>>>>> " + vendorcreditAccount );
    
    String vendorcreditAccType = request.getParameter("vendor_cr_accttype");
  //  System.out.println("vendorcreditAccType >>>>>> " + vendorcreditAccType );
    
    String vendorcreditTranCode = request.getParameter("vendor_cr_trancode");
  //  System.out.println("vendorcreditTranCode >>>>>> " + vendorcreditTranCode );
    
    String vendorcreditNarration = request.getParameter("vendor_cr_narration");
  //  System.out.println("vendorcreditNarration >>>>>> " + vendorcreditNarration );
    
    String vendoramount = request.getParameter("vendor_amount");
  //  System.out.println("vendoramount >>>>>> " + vendoramount );
//    String costdebitAcctNo = vendordebitAccount.substring(8,16);
//    String costcreditAcctNo = vendorcreditAccount.substring(8,16);
    String costdebitAcctNo = "";
    String costcreditAcctNo = "";  
   // System.out.println("vendordebitAccount.substring(0,3) >>>>>> " + vendordebitAccount.substring(0,3)+"    vendorcreditAccount.substring(0,3): "+vendorcreditAccount.substring(0,3));
    if(vendordebitAccount.substring(0,3).equalsIgnoreCase("NGN")){costdebitAcctNo = vendordebitAccount.substring(8,16);}else{costdebitAcctNo = vendordebitAccount;}
    if(vendorcreditAccount.substring(0,3).equalsIgnoreCase("NGN")){costcreditAcctNo = vendorcreditAccount.substring(8,16);}else{costcreditAcctNo = vendorcreditAccount;}  
    if(vendordebitAccount.substring(2,5).equalsIgnoreCase("NGN")){costdebitAcctNo = vendordebitAccount.substring(6,14);}
    if(vendorcreditAccount.substring(2,5).equalsIgnoreCase("NGN")){costcreditAcctNo = vendorcreditAccount.substring(6,14);}    
    String supervisor = request.getParameter("supervisor");
    String recType = request.getParameter("recType");
  //  System.out.println("ID >>>>>> " + id+"    recType: "+recType);
    String type="Group Asset Creation";
    SbuCode = request.getParameter("SbuCode");
    String transactionId = request.getParameter("transactionIdVendor");
 //   System.out.println("SbuCode >>>>>> " + SbuCode+"   costdebitAcctNo: "+costdebitAcctNo+"  costcreditAcctNo: "+costcreditAcctNo+"  vendordebitAccount: "+vendordebitAccount+"  vendorcreditAccount: "+vendorcreditAccount);
    String finacleTransId = approv.getGeneratedTransId();
  //  String sbu_code = SbuCode+ "|" +SbuCode;
    String sbu_code = SbuCode+SbuCode;
    vendordebitNarration = SbuCode+"|"+SbuCode+"|"+vendordebitNarration;
    String costdebitAcctName = approv.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+costdebitAcctNo+"'");
    String costcreditAcctName = approv.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+costcreditAcctNo+"'");
   // String [] idSplit = id.split("/");
    String test10 = "select category_name from am_ad_category where Asset_Ledger = '"+costdebitAcctNo+"' and category_code = '"+categoryCode+"'";
    String test11 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+costdebitAcctNo+"' and category_code = '"+categoryCode+"'";
    String test12 = "select category_name from am_ad_category where DEP_LEDGER = '"+costdebitAcctNo+"' and category_code = '"+categoryCode+"'";
    String test13 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+costdebitAcctNo+"' ";
    String test14 = "select asset_acq_ac from am_gb_company where asset_acq_ac = '"+costdebitAcctNo+"' ";
    costdebitAcctName = approv.getCodeName(test10);
//    System.out.println("<<<<<Vendor Name querry of test13 ===== "+test13);
    if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = approv.getCodeName(test11);}
    if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = approv.getCodeName(test12);}
    if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = approv.getCodeName(test13);}
    if(approv.getCodeName(test14)!="" && costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = "Asset Acquisition Account";}
    
//    System.out.println("costdebitAcctName >>>>>> " + costdebitAcctName );
    String test20 = "select category_name from am_ad_category where Asset_Ledger = '"+costcreditAcctNo+"' and category_code = '"+categoryCode+"'";
    String test21 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+costcreditAcctNo+"' and category_code = '"+categoryCode+"'";
    String test22 = "select category_name from am_ad_category where DEP_LEDGER = '"+costcreditAcctNo+"' and category_code = '"+categoryCode+"'";
    String test23 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+costcreditAcctNo+"' ";
    String test24 = "select asset_acq_ac from am_gb_company where asset_acq_ac = '"+costcreditAcctNo+"' ";
    
    costcreditAcctName = approv.getCodeName(test20);
//    System.out.println("=====costcreditAcctName >>>>>> "+costcreditAcctName);
//    System.out.println("<<<<<Vendor Name querry of test23 ==== "+test23);
    
    if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test21);}
    if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test22);}
    if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test23);}
    if(approv.getCodeName(test24)!="" && costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = "Asset Acquisition Account";}
    
//    System.out.println("=====costcreditAcctName >>>>>> " + costcreditAcctName );
    if(vendoramount!=null)
    	{
    try
    {
		vendoramount = vendoramount.replaceAll(",","");
		if (supervisor == "" || supervisor == null || supervisor == "*" || supervisor.equals("*")) 
		{
		 supervisor = "";
		}
    	if(ad.isoChecking(group_id, page1, transactionId,tranID))
    	{
			//System.out.println("Came Here ============ 1");
			/***************************************************/
    		 iso=bus.transferFund(vendordebitNarration,vendorcreditAccount,vendordebitAccount,Double.parseDouble(vendoramount),finacleTransId,SbuCode);
    		 //iso=bus.interaccounttransferFund(vendordebitNarration,vendorcreditAccount,vendordebitAccount,Double.parseDouble(vendoramount),finacleTransId,SbuCode,SbuCode);
			/***************************************************/
			//iso="000";
    		ad.updateRaiseEntryTransaction(group_id, page1, transactionId, iso,finacleTransId,"","");
			//System.out.println("Came Here ============ 3");
    	}
    	else
    	{
    		raiseMan.raiseEntry(vendordebitAccount,vendorcreditAccount,vendordebitAccType,vendorcreditAccType,vendordebitTranCode
    		,vendorcreditTranCode,vendordebitNarration,
    		vendorcreditNarration,Double.parseDouble(vendoramount), userId,batchId,cdate,supervisor,group_id,type);
			/***************************************************/
    		 iso=bus.transferFund(vendordebitNarration,vendorcreditAccount,vendordebitAccount,Double.parseDouble(vendoramount),finacleTransId,SbuCode);
    		 //iso=bus.interaccounttransferFund(vendordebitNarration,vendorcreditAccount,vendordebitAccount,Double.parseDouble(vendoramount),finacleTransId,SbuCode,SbuCode);
			/***************************************************/
		// iso="000";
         ad.insertRaiseEntryTransaction(userId,vendordebitNarration,vendordebitAccount,vendorcreditAccount,
        		 Double.parseDouble(vendoramount),iso,group_id,page1,transactionId,systemIp,tranID,assetCode,finacleTransId,costdebitAcctName,costcreditAcctName,userId,recType);
		
    	}
    	String iso_status=approv.getCodeName(iso_Status_qry + iso +"'");
        if(iso.equalsIgnoreCase("000"))
        {
   	   	 String reversalId = tranID.substring(0, 1);
   	     if(reversalId.equals("R")){tranID = tranID.substring(1, tranID.length());}
 //     	     if(reversalId.equals("R")){tranID = "R"+tranID;}
               String q = "update am_Raisentry_Transaction set iso = '-001' where asset_id='"+id+"' and Trans_id = '"+tranID+"' and transactionId = "+transactionId+"";
               ad.updateAssetStatusChange(q);	 
                //System.out.println("#####################################################################  ");
			/*	
     			msgText1 = message.MailMessage(mail_code, transaction_type)+", \n";
    			msgText1 += "For Group id  "+group_id+"\n";
    	            msgText1 += "For  "+vendordebitNarration+"\n";
    			msgText1 += "Debit Account  "+vendordebitAccount+"\n";
    			msgText1 += "Credit Account  "+vendorcreditAccount+"\n";
    			msgText1 += "Amount  "+formata.formatAmount(vendoramount)+"\n";
    			msgText1 += "Login here http://172.27.11.192:8080/legend2.net" ;
      			mail.sendMail(to,subject,msgText1);	
							//	System.out.println("Came Here ============ 8");
				   */
                   msgText1 = "CREDIT ADVICE \n";
                   msgText1 += ""+Subheading+"\n";
        			msgText1 += message.MailMessage(mail_code, transaction_type)+" \n";        			
       			msgText1 += "VENDOR NAME "+vendorname+"\n";
       			msgText1 += "ACCOUNT NUMBER  "+vendorcreditAccount+"\n";
       			msgText1 += "INVOICE NO  "+invNo+"\n";
       			msgText1 += "AMOUNT  "+formata.formatAmount(vendoramount)+"\n";
                   msgText1 += "NARRATION  "+vendorcreditNarration +"\n";
                   msgText1 += "PAYMENT ID  "+id+"\n";        			        		        			
       			//msgText1 += "TRANSACTION DATE  "+newdate+"\n";
       			msgText1 += ""+summary1+"\n";
       			msgText1 += ""+summary2+"\n";
       			msgText1 += ""+summary3+"\n"; 				   
        		out.println("<script>alert('" + iso_status +"!')</script>");
				out.println("<script>");
                                out.println("window.location='DocumentHelp.jsp?np=groupAssetPosting&id="+group_id+"&pageDirect=Y&tranId="+tranID+"&assetCode="+assetCode+"'" );
				//out.println("window.location='DocumentHelp.jsp?np=groupAssetPosting&id="+ group_id+"'" );
				out.println("</script>");
						
        }
		else
		{ 
				 out.println("<script>alert('" + iso_status +"!')</script>");
				 out.println("<script>");
				// out.println("window.location='DocumentHelp.jsp?np=groupAssetPosting&id="+ group_id+"'" );
				out.println("window.location='DocumentHelp.jsp?np=groupAssetPosting&id="+group_id+"&pageDirect=Y&tranId="+tranID+"&assetCode="+assetCode+"'" );
                                 out.println("</script>");
	    }
     }
    catch(Exception e)
    {
    	e.printStackTrace();	
    }
  }
    }
    if (pgNum==3)
    {
    		System.out.println("<<<<<<<<<<<<< Process Type Witholding Tax>>>>>>>>>>> 3");
    		
           /**
            Post TAX Transaction
            */
            String taxdebitAccount = request.getParameter("tax_dr_account");
         //   System.out.println("taxdebitAccount >>>>>> " + taxdebitAccount );
            
            String taxdebitAccType = request.getParameter("tax_dr_accttype");
          //  System.out.println("taxdebitAccType >>>>>> " + taxdebitAccType );
            
            String taxdebitTranCode = request.getParameter("tax_dr_trancode");
          //  System.out.println("taxdebitTranCode >>>>>> " + taxdebitTranCode );
            
            String taxdebitNarration = request.getParameter("tax_dr_narration");
          //  System.out.println("taxdebitNarration >>>>>> " + taxdebitNarration );
            
            String taxcreditAccount = request.getParameter("tax_cr_account");
          //  System.out.println("taxcreditAccount >>>>>> " + taxcreditAccount );
            
            String taxcreditAccType = request.getParameter("tax_cr_accttype");
          //  System.out.println("taxcreditAccType >>>>>> " + taxcreditAccType );
            
            String taxcreditTranCode = request.getParameter("tax_cr_trancode");
        //    System.out.println("taxcreditTranCode >>>>>> " + taxcreditTranCode );
            
            String taxcreditNarration = request.getParameter("tax_cr_narration");
        //    System.out.println("taxcreditNarration >>>>>> " + taxcreditNarration );
            String subjectTowht = request.getParameter("subjectTowht"); 
            String taxamount = request.getParameter("wh_amount");
         //   System.out.println("taxamount >>>>>> " + taxamount );
            
            String supervisor = request.getParameter("supervisor");
            String recType = request.getParameter("recType");
          //  System.out.println("supervisor >>>>>> " + supervisor );
           // String SbuCode = request.getParameter("SbuCode");
            String type="Group Asset Creation";
            String transactionId = request.getParameter("transactionIdWitholding");
      //      System.out.println("transactionId >>>>>> " + transactionId );
    		 String finacleTransId = approv.getGeneratedTransId();
    		// String sbu_code = SbuCode+ "|" +SbuCode;
//    		    String costdebitAcctNo = taxdebitAccount.substring(8,16);
//    		    String costcreditAcctNo = taxcreditAccount.substring(8,16);
	 		    String costdebitAcctNo = taxdebitAccount;
	 		    String costcreditAcctNo = taxcreditAccount;  		
//     		   System.out.println("taxdebitAccount.substring(0,3) >>>>>> " + taxdebitAccount.substring(0,3)+"    taxcreditAccount.substring(0,3): "+taxcreditAccount.substring(0,3));
    	        if(taxdebitAccount.substring(0,3).equalsIgnoreCase("NGN")){costdebitAcctNo = taxdebitAccount.substring(8,16);}
//    	        System.out.print("======costdebitAcctNo: "+costdebitAcctNo);
    	        if(taxcreditAccount.substring(0,3).equalsIgnoreCase("NGN")){costcreditAcctNo = taxcreditAccount.substring(8,16);}
    	        if(taxdebitAccount.substring(2,5).equalsIgnoreCase("NGN")){costdebitAcctNo = taxdebitAccount.substring(6,14);}
    	        if(taxcreditAccount.substring(2,5).equalsIgnoreCase("NGN")){costcreditAcctNo = taxcreditAccount.substring(6,14);}
    		 String sbu_code = SbuCode+SbuCode;
    		 taxdebitNarration = SbuCode+"|"+SbuCode+"|"+taxdebitNarration;
    	        String costdebitAcctName = approv.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+costdebitAcctNo+"'");
    	        String costcreditAcctName = approv.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+costcreditAcctNo+"'");
 //   	        System.out.println("ID >>>>>> " + id+"  costdebitAcctNo: "+costdebitAcctNo+"   costcreditAcctNo: "+costcreditAcctNo);
//    	        String [] idSplit = id.split("/");
    	        String test10 = "select category_name from am_ad_category where Asset_Ledger = '"+costdebitAcctNo+"' and category_code = '"+categoryCode+"'";
    	        String test11 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+costdebitAcctNo+"' and category_code = '"+categoryCode+"'";
    	        String test12 = "select category_name from am_ad_category where DEP_LEDGER = '"+costdebitAcctNo+"' and category_code = '"+categoryCode+"'";
    	        String test13 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+costdebitAcctNo+"' ";
    	        String test14 = "select asset_acq_ac from am_gb_company where asset_acq_ac = '"+costdebitAcctNo+"' ";
    	        String test15 = "select Fed_Wht_Account from am_gb_company where Fed_Wht_Account = '"+costdebitAcctNo+"' ";
//    	        System.out.println("test14 in Number 3 >>>>>> " + test14);
    	        costdebitAcctName = approv.getCodeName(test10);
    	        if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = approv.getCodeName(test11);}
    	        if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = approv.getCodeName(test12);}
    	        if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = approv.getCodeName(test13);}
    //	        if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = approv.getCodeName(test14);}
    	        if(approv.getCodeName(test14)!="" && costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = "Asset Acquisition Suspense Account";}
    	        if(approv.getCodeName(test15)!="" && costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = "With Holding Tax Account";}
    	        
    	        String test20 = "select category_name from am_ad_category where Asset_Ledger = '"+costcreditAcctNo+"' and category_code = '"+categoryCode+"'";
    	        String test21 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+costcreditAcctNo+"' and category_code = '"+categoryCode+"'";
    	        String test22 = "select category_name from am_ad_category where DEP_LEDGER = '"+costcreditAcctNo+"' and category_code = '"+categoryCode+"'";
    	        String test23 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+costcreditAcctNo+"' ";
    	        String test24 = "";
    	        if(subjectTowht.equals("F")){test24 = "select Fed_Wht_Account from am_gb_company where Fed_Wht_Account = '"+costcreditAcctNo+"' ";}
    	        if(subjectTowht.equals("S")){test24 = "select Wht_Account from am_gb_company where Wht_Account = '"+costcreditAcctNo+"' ";}
    	        String test25 = "select asset_acq_ac from am_gb_company where asset_acq_ac = '"+costcreditAcctNo+"' ";
//    	        System.out.println("test24 in Number 3 >>>>>> " + test24); 
	          costcreditAcctName = approv.getCodeName(test20);
	          if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test21);}
	          if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test22);}
	          if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test23);}
//	          if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test24);}
	          if(approv.getCodeName(test24)!="" && costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = "With Holding Tax Account";}
	          if(approv.getCodeName(test25)!="" && costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = "Asset Acquisition Suspense Account";}
    if(taxamount!=null)
         {
    	    try
            {
    			taxamount = taxamount.replaceAll(",","");
    			if (supervisor == "" || supervisor == null || supervisor == "*" || supervisor.equals("*")) 
    			{
    			 supervisor = "";
    			}
            	if(ad.isoChecking(group_id, page1, transactionId,tranID))
            	{
    				/***************************************************/
            		 iso=bus.transferFund(taxdebitNarration,taxcreditAccount,taxdebitAccount,Double.parseDouble(taxamount),finacleTransId,SbuCode);
            		 //iso=bus.interaccounttransferFund(taxdebitNarration,taxcreditAccount,taxdebitAccount,Double.parseDouble(taxamount),finacleTransId,SbuCode,SbuCode);
    				/***************************************************/
    				//iso="000";
            		ad.updateRaiseEntryTransaction(group_id, page1, transactionId, iso,finacleTransId,"","");
    			}
            	else
            	{
            		raiseMan.raiseEntry(taxdebitAccount,taxcreditAccount,taxdebitAccType,taxcreditAccType,taxdebitTranCode
            		,taxcreditTranCode,taxdebitNarration,
            		taxcreditNarration,Double.parseDouble(taxamount), userId,batchId,cdate,supervisor,group_id,type);
    				/***************************************************/
            		 iso=bus.transferFund(taxdebitNarration,taxcreditAccount,taxdebitAccount ,Double.parseDouble(taxamount),finacleTransId,SbuCode);
            		 //iso=bus.interaccounttransferFund(taxdebitNarration,taxcreditAccount,taxdebitAccount ,Double.parseDouble(taxamount),finacleTransId,SbuCode,SbuCode);
    				/***************************************************/
    			// iso="000";
                 ad.insertRaiseEntryTransaction(userId,taxdebitNarration,taxdebitAccount,taxcreditAccount ,Double.parseDouble(taxamount),iso,group_id,page1,transactionId,systemIp,tranID,assetCode,finacleTransId,costdebitAcctName,costcreditAcctName,userId,recType);
    			
            	}
            	String iso_status=approv.getCodeName(iso_Status_qry + iso +"'");
                if(iso.equalsIgnoreCase("000"))
                {
           	   	 String reversalId = tranID.substring(0, 1);
           	     if(reversalId.equals("R")){tranID = tranID.substring(1, tranID.length());}
              	     if(!reversalId.equals("R")){tranID = "R"+tranID;}
                       String q = "update am_Raisentry_Transaction set iso = '-001' where asset_id='"+id+"' and Trans_id = '"+tranID+"' and transactionId = "+transactionId+"";
                       ad.updateAssetStatusChange(q);	 
                        //System.out.println("#####################################################################  ");
    					
             			msgText1 = message.MailMessage(mail_code, transaction_type)+", \n";
            			msgText1 += "For Group id  "+group_id+"\n";
            	            msgText1 += "For  "+taxdebitNarration+"\n";
            			msgText1 += "Debit Account  "+taxdebitAccount+"\n";
            			msgText1 += "Credit Account  "+taxcreditAccount+"\n";
            			msgText1 += "Amount  "+formata.formatAmount(taxamount)+"\n";
            			msgText1 += "Login here http://172.27.11.192:8080/legend2.net" ;
              			mail.sendMail(to,subject,msgText1);	
    					
    					//	System.out.println("Came Here ============ 8");
                		out.println("<script>alert('" + iso_status +"!')</script>");
    					out.println("<script>");
                                        out.println("window.location='DocumentHelp.jsp?np=groupAssetPosting&id="+group_id+"&pageDirect=Y&tranId="+tranID+"&assetCode="+assetCode+"'" );
    					//out.println("window.location='DocumentHelp.jsp?np=groupAssetPosting&id="+ group_id+"'" );
    					out.println("</script>");
                }
    			else
    			{ 
    					// System.out.println("Came Here ============ 1");
    					 out.println("<script>alert('" + iso_status +"!')</script>");
    					 out.println("<script>");
    					// out.println("window.location='DocumentHelp.jsp?np=groupAssetPosting&id="+ group_id+"'" );
    					 out.println("window.location='DocumentHelp.jsp?np=groupAssetPosting&id="+group_id+"&pageDirect=Y&tranId="+tranID+"&assetCode="+assetCode+"'" );
                                         out.println("</script>");
    		    }
                
    	    }
            catch(Exception e)
            {
            	e.printStackTrace();	
            }
    	}  }


        
        //uncapitalized asset posting

            if (pgNum==4)
    {
//    	System.out.println("<<<<<<<<<<<<< Process Type Vendor Account >>>>>>>>>>> 2");
	/**
    Post Vendor Transaction
    */
    String vendordebitAccount = request.getParameter("vendor_dr_account");
   // System.out.println("vendordebitAccount >>>>>> " + vendordebitAccount );

    String vendordebitAccType = request.getParameter("vendor_dr_accttype");
   // System.out.println("vendordebitAccType >>>>>> " + vendordebitAccType );

    String vendordebitTranCode = request.getParameter("vendor_dr_trancode");
   // System.out.println("vendordebitTranCode >>>>>> " + vendordebitTranCode );

    String vendordebitNarration = request.getParameter("vendor_dr_narration");
  //  System.out.println("vendordebitNarration >>>>>> " + vendordebitNarration );

    String vendorcreditAccount = request.getParameter("vendor_cr_account");
   // System.out.println("vendorcreditAccount >>>>>> " + vendorcreditAccount );

    String vendorcreditAccType = request.getParameter("vendor_cr_accttype");
  //  System.out.println("vendorcreditAccType >>>>>> " + vendorcreditAccType );

    String vendorcreditTranCode = request.getParameter("vendor_cr_trancode");
  //  System.out.println("vendorcreditTranCode >>>>>> " + vendorcreditTranCode );

    String vendorcreditNarration = request.getParameter("vendor_cr_narration");
  //  System.out.println("vendorcreditNarration >>>>>> " + vendorcreditNarration );

    String vendoramount = request.getParameter("vendor_amount");
  //  System.out.println("vendoramount >>>>>> " + vendoramount );

    String supervisor = request.getParameter("supervisor");
   // System.out.println("supervisor >>>>>> " + supervisor );
    String type="Group Asset Creation";
    SbuCode = request.getParameter("SbuCode");
    String transactionId = request.getParameter("transactionIdVendor");
  //  System.out.println("transactionId >>>>>> " + transactionId );
    String categoryAcronym = request.getParameter("categoryAcronym");
 String finacleTransId = approv.getGeneratedTransId();
 String recType = request.getParameter("recType");
 //String sbu_code = SbuCode+ "|" +SbuCode;
// String costdebitAcctNo = vendordebitAccount.substring(8,16);
// String costcreditAcctNo = vendorcreditAccount.substring(8,16);
// String costdebitAcctNo = "";
// String costcreditAcctNo = ""; 
// if(vendordebitAccount.substring(0,2)=="NGN"){costdebitAcctNo = vendordebitAccount.substring(8,16);}
// if(vendorcreditAccount.substring(0,2)=="NGN"){costcreditAcctNo = vendorcreditAccount.substring(8,16);}
// if(vendordebitAccount.substring(2,5).equals("NGN")){costdebitAcctNo = vendordebitAccount.substring(6,14);}
// if(vendorcreditAccount.substring(2,5).equals("NGN")){costcreditAcctNo = vendorcreditAccount.substring(6,14);}
// String sbu_code = SbuCode+SbuCode;
// vendordebitNarration = SbuCode+"|"+SbuCode+"|"+vendordebitNarration;
// String costdebitAcctName = approv.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+costdebitAcctNo+"'");
// String vendorAcctName = approv.getCodeName("select vendor_name from am_ad_vendor where account_number = '"+costcreditAcctNo+"'");
// String [] idSplit = id.split("/");
// System.out.println("<<<<<<<<<<<<<<<<=====idSplit::: "+idSplit.length);
// if(idSplit.length==1){categoryAcronym = categoryAcronym;}else{categoryAcronym = idSplit[2];}
// String test10 = "select category_name from am_ad_category where Asset_Ledger = '"+costdebitAcctNo+"' and category_acronym = '"+categoryAcronym+"'";
// String test11 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+costdebitAcctNo+"' and category_acronym = '"+categoryAcronym+"'";
// String test12 = "select category_name from am_ad_category where DEP_LEDGER = '"+costdebitAcctNo+"' and category_acronym = '"+categoryAcronym+"'";
// String test13 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+costdebitAcctNo+"' ";
// costdebitAcctName = approv.getCodeName(test10);
// if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = approv.getCodeName(test11);}
// if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = approv.getCodeName(test12);}
// if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = approv.getCodeName(test13);}
// 
// String test20 = "select category_name from am_ad_category where Asset_Ledger = '"+costcreditAcctNo+"' and category_acronym = '"+categoryAcronym+"'";
// String test21 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+costcreditAcctNo+"' and category_acronym = '"+categoryAcronym+"'";
// String test22 = "select category_name from am_ad_category where DEP_LEDGER = '"+costcreditAcctNo+"' and category_acronym = '"+categoryAcronym+"'";
// String test23 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+costcreditAcctNo+"' ";
// vendorAcctName = approv.getCodeName(test20);
// if(vendorAcctName.equalsIgnoreCase("")){vendorAcctName = approv.getCodeName(test21);}
// if(vendorAcctName.equalsIgnoreCase("")){vendorAcctName = approv.getCodeName(test22);}
// if(vendorAcctName.equalsIgnoreCase("")){vendorAcctName = approv.getCodeName(test23);}  
 
 String costdebitAcctNo = "";
 String costcreditAcctNo = "";  
// System.out.println("vendordebitAccount.substring(0,3) >>>>>> " + vendordebitAccount.substring(0,3)+"    vendorcreditAccount.substring(0,3): "+vendorcreditAccount.substring(0,3));
 if(vendordebitAccount.substring(0,3).equalsIgnoreCase("NGN")){costdebitAcctNo = vendordebitAccount.substring(8,16);}else{costdebitAcctNo = vendordebitAccount;}
 if(vendorcreditAccount.substring(0,3).equalsIgnoreCase("NGN")){costcreditAcctNo = vendorcreditAccount.substring(8,16);}else{costcreditAcctNo = vendorcreditAccount;}  
 if(vendordebitAccount.substring(2,5).equalsIgnoreCase("NGN")){costdebitAcctNo = vendordebitAccount.substring(6,14);}
 if(vendorcreditAccount.substring(2,5).equalsIgnoreCase("NGN")){costcreditAcctNo = vendorcreditAccount.substring(6,14);}    

 SbuCode = request.getParameter("SbuCode");

//  String sbu_code = SbuCode+ "|" +SbuCode;
 String sbu_code = SbuCode+SbuCode;
 vendordebitNarration = SbuCode+"|"+SbuCode+"|"+vendordebitNarration;
 String costdebitAcctName = approv.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+costdebitAcctNo+"'");
 String costcreditAcctName = approv.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+costcreditAcctNo+"'");
// String [] idSplit = id.split("/");
 String test10 = "select category_name from am_ad_category where Asset_Ledger = '"+costdebitAcctNo+"' and category_code = '"+categoryCode+"'";
 String test11 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+costdebitAcctNo+"' and category_code = '"+categoryCode+"'";
 String test12 = "select category_name from am_ad_category where DEP_LEDGER = '"+costdebitAcctNo+"' and category_code = '"+categoryCode+"'";
 String test13 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+costdebitAcctNo+"' ";
 String test14 = "select asset_acq_ac from am_gb_company where asset_acq_ac = '"+costdebitAcctNo+"' ";
 costdebitAcctName = approv.getCodeName(test10);
// System.out.println("<<<<<Vendor Name querry of test13 ===== "+test13);
 if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = approv.getCodeName(test11);}
 if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = approv.getCodeName(test12);}
 if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = approv.getCodeName(test13);}
 if(approv.getCodeName(test14)!="" && costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = "Asset Acquisition Account";}
 
// System.out.println("costdebitAcctName >>>>>> " + costdebitAcctName );
 String test20 = "select category_name from am_ad_category where Asset_Ledger = '"+costcreditAcctNo+"' and category_code = '"+categoryCode+"'";
 String test21 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+costcreditAcctNo+"' and category_code = '"+categoryCode+"'";
 String test22 = "select category_name from am_ad_category where DEP_LEDGER = '"+costcreditAcctNo+"' and category_code = '"+categoryCode+"'";
 String test23 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+costcreditAcctNo+"' ";
 String test24 = "select asset_acq_ac from am_gb_company where asset_acq_ac = '"+costcreditAcctNo+"' ";
 
 costcreditAcctName = approv.getCodeName(test20);
// System.out.println("=====costcreditAcctName >>>>>> "+costcreditAcctName);
// System.out.println("<<<<<Vendor Name querry of test23 ==== "+test23);
 
 if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test21);}
 if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test22);}
 if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test23);}
 if(approv.getCodeName(test24)!="" && costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = "Asset Acquisition Account";}
 
    if(vendoramount!=null)
    	{
    try
    {
		vendoramount = vendoramount.replaceAll(",","");
		if (supervisor == "" || supervisor == null || supervisor == "*" || supervisor.equals("*"))
		{
		 supervisor = "";
		}
    	if(ad.isoChecking(group_id, page1, transactionId,tranID))
    	{
			//System.out.println("Came Here ============ 1");
			/***************************************************/
    		 iso=bus.transferFund(vendordebitNarration,vendorcreditAccount,vendordebitAccount,Double.parseDouble(vendoramount),finacleTransId,sbu_code);
    		 //iso=bus.interaccounttransferFund(vendordebitNarration,vendorcreditAccount,vendordebitAccount,Double.parseDouble(vendoramount),finacleTransId,SbuCode,SbuCode);
			/***************************************************/
			//iso="000";
    		ad.updateRaiseEntryTransaction(group_id, page1, transactionId, iso,finacleTransId,"","");
			//System.out.println("Came Here ============ 3");
    	}
    	else
    	{
    		raiseMan.raiseEntry(vendordebitAccount,vendorcreditAccount,vendordebitAccType,vendorcreditAccType,vendordebitTranCode
    		,vendorcreditTranCode,vendordebitNarration,
    		vendorcreditNarration,Double.parseDouble(vendoramount), userId,batchId,cdate,supervisor,group_id,type);
			/***************************************************/
    		 iso=bus.transferFund(vendordebitNarration,vendorcreditAccount,vendordebitAccount,Double.parseDouble(vendoramount),finacleTransId,SbuCode);
    		// iso=bus.interaccounttransferFund(vendordebitNarration,vendorcreditAccount,vendordebitAccount,Double.parseDouble(vendoramount),finacleTransId,SbuCode,SbuCode);
			/***************************************************/
		// iso="000";
    		 System.out.println("Came Here ============ 1");
         ad.insertRaiseEntryTransaction(userId,vendordebitNarration,vendordebitAccount,vendorcreditAccount,
        		 Double.parseDouble(vendoramount),iso,group_id,page1,transactionId,systemIp,tranID,assetCode,finacleTransId,costdebitAcctName,costcreditAcctName,userId,recType);

    	}
    	String iso_status=approv.getCodeName(iso_Status_qry + iso +"'");
        if(iso.equalsIgnoreCase("000"))
        {
   	   	 String reversalId = tranID.substring(0, 1);
   	     if(reversalId.equals("R")){tranID = tranID.substring(1, tranID.length());}
      	     if(!reversalId.equals("R")){tranID = "R"+tranID;}
               String q = "update am_Raisentry_Transaction set iso = '-001' where asset_id='"+id+"' and Trans_id = '"+tranID+"' and transactionId = "+transactionId+"";
               ad.updateAssetStatusChange(q);	 
                //System.out.println("#####################################################################  ");

     			msgText1 = message.MailMessage(mail_code, transaction_type)+", \n";
    			msgText1 += "For Group id  "+group_id+"\n";
    	            msgText1 += "For  "+vendordebitNarration+"\n";
    			msgText1 += "Debit Account  "+vendordebitAccount+"\n";
    			msgText1 += "Credit Account  "+vendorcreditAccount+"\n";
    			msgText1 += "Amount  "+formata.formatAmount(vendoramount)+"\n";
    			msgText1 += "Login here http://172.27.11.192:8080/legend2.net" ;
      			mail.sendMail(to,subject,msgText1);
							//	System.out.println("Came Here ============ 8");

        		out.println("<script>alert('" + iso_status +"!')</script>");
				out.println("<script>");
                                out.println("window.location='DocumentHelp.jsp?np=groupAssetPostingBranch&id="+group_id+"&pageDirect=Y&tranId="+tranID+"&assetCode="+assetCode+"'" );
				//out.println("window.location='DocumentHelp.jsp?np=groupAssetPostingBranch&id="+ group_id+"'" );
				out.println("</script>");

        }
		else
		{
				 out.println("<script>alert('" + iso_status +"!')</script>");
				 out.println("<script>");
				   out.println("window.location='DocumentHelp.jsp?np=groupAssetPostingBranch&id="+group_id+"&pageDirect=Y&tranId="+tranID+"&assetCode="+assetCode+"'" );
				 out.println("</script>");
	    }
     }
    catch(Exception e)
    {
    	e.printStackTrace();
    }
  }
    }


   if (pgNum==5)
    {
//    		System.out.println("<<<<<<<<<<<<< Process Type Witholding Tax>>>>>>>>>>> 3");

           /**
            Post TAX Transaction
            */
            String taxdebitAccount = request.getParameter("tax_dr_account");
         //   System.out.println("taxdebitAccount >>>>>> " + taxdebitAccount );

            String taxdebitAccType = request.getParameter("tax_dr_accttype");
          //  System.out.println("taxdebitAccType >>>>>> " + taxdebitAccType );

            String taxdebitTranCode = request.getParameter("tax_dr_trancode");
          //  System.out.println("taxdebitTranCode >>>>>> " + taxdebitTranCode );

            String taxdebitNarration = request.getParameter("tax_dr_narration");
          //  System.out.println("taxdebitNarration >>>>>> " + taxdebitNarration );

            String taxcreditAccount = request.getParameter("tax_cr_account");
          //  System.out.println("taxcreditAccount >>>>>> " + taxcreditAccount );

            String taxcreditAccType = request.getParameter("tax_cr_accttype");
          //  System.out.println("taxcreditAccType >>>>>> " + taxcreditAccType );

            String taxcreditTranCode = request.getParameter("tax_cr_trancode");
        //    System.out.println("taxcreditTranCode >>>>>> " + taxcreditTranCode );

            String taxcreditNarration = request.getParameter("tax_cr_narration");
        //    System.out.println("taxcreditNarration >>>>>> " + taxcreditNarration );
            String subjectTowht = request.getParameter("subjectTowht"); 

            String taxamount = request.getParameter("wh_amount");
         //   System.out.println("taxamount >>>>>> " + taxamount );
            
            String supervisor = request.getParameter("supervisor");
          //  System.out.println("supervisor >>>>>> " + supervisor );
            SbuCode = request.getParameter("SbuCode");
            String recType = request.getParameter("recType");
            String type="Group Asset Creation";
            String transactionId = request.getParameter("transactionIdWitholding");
            String categoryAcronym = request.getParameter("categoryAcronym");
      //      System.out.println("transactionId >>>>>> " + transactionId );
 String finacleTransId = approv.getGeneratedTransId();
 			//String sbu_code = SbuCode+ "|" +SbuCode;
 			String raiseEntryNarration = taxdebitNarration;
 			String sbu_code = SbuCode+SbuCode;
 			taxdebitNarration = SbuCode+"|"+SbuCode+"|"+taxdebitNarration;
// 			 String costdebitAcctNo = taxdebitAccount.substring(8,16);
// 			 String costcreditAcctNo = taxcreditAccount.substring(8,16);
//			 String costdebitAcctNo = "";
//			 String costcreditAcctNo = "";			 
// 	        if(taxdebitAccount.substring(0,2)=="NGN"){costdebitAcctNo = taxdebitAccount.substring(8,16);}
// 	        if(taxcreditAccount.substring(0,2)=="NGN"){costcreditAcctNo = taxcreditAccount.substring(8,16);} 
// 	       if(taxdebitAccount.substring(2,5).equals("NGN")){costdebitAcctNo = taxdebitAccount.substring(6,14);}
// 	      if(taxcreditAccount.substring(2,5).equals("NGN")){costcreditAcctNo = taxcreditAccount.substring(6,14);}
// 	        String costdebitAcctName = approv.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+costdebitAcctNo+"'");
// 	        String costcreditAcctName = approv.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+costcreditAcctNo+"'");
// 	       String [] idSplit = id.split("/");
//// 	      System.out.println("<<<<<<<<<<<<<<<<=====idSplit::: "+idSplit.length);
// 	     if(idSplit.length==1){categoryAcronym = categoryAcronym;} else {categoryAcronym = idSplit[2];} 	       
// 	      String test10 = "select category_name from am_ad_category where Asset_Ledger = '"+costdebitAcctNo+"' and category_acronym = '"+categoryAcronym+"'";
// 	      String test11 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+costdebitAcctNo+"' and category_acronym = '"+categoryAcronym+"'";
// 	      String test12 = "select category_name from am_ad_category where DEP_LEDGER = '"+costdebitAcctNo+"' and category_acronym = '"+categoryAcronym+"'";
// 	      String test13 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+costdebitAcctNo+"' ";
// 	      costdebitAcctName = approv.getCodeName(test10);
// 	      if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = approv.getCodeName(test11);}
// 	      if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = approv.getCodeName(test12);}
// 	      if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = approv.getCodeName(test13);}
// 	      
// 	      String test20 = "select category_name from am_ad_category where Asset_Ledger = '"+costcreditAcctNo+"' and category_acronym = '"+categoryAcronym+"'";
// 	      String test21 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+costcreditAcctNo+"' and category_acronym = '"+categoryAcronym+"'";
// 	      String test22 = "select category_name from am_ad_category where DEP_LEDGER = '"+costcreditAcctNo+"' and category_acronym = '"+categoryAcronym+"'";
// 	      String test23 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+costcreditAcctNo+"' ";
//          costcreditAcctName = approv.getCodeName(test20);
//          if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test21);}
//          if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test22);}
//          if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test23);} 	       
// 			 
 		    String costdebitAcctNo = taxdebitAccount;
 		    String costcreditAcctNo = taxcreditAccount;  		
// 		   System.out.println("taxdebitAccount.substring(0,3) >>>>>> " + taxdebitAccount.substring(0,3)+"    taxcreditAccount.substring(0,3): "+taxcreditAccount.substring(0,3));
	        if(taxdebitAccount.substring(0,3).equalsIgnoreCase("NGN")){costdebitAcctNo = taxdebitAccount.substring(8,16);}
//	        System.out.print("======costdebitAcctNo: "+costdebitAcctNo);
	        if(taxcreditAccount.substring(0,3).equalsIgnoreCase("NGN")){costcreditAcctNo = taxcreditAccount.substring(8,16);}
	        if(taxdebitAccount.substring(2,5).equalsIgnoreCase("NGN")){costdebitAcctNo = taxdebitAccount.substring(6,14);}
	        if(taxcreditAccount.substring(2,5).equalsIgnoreCase("NGN")){costcreditAcctNo = taxcreditAccount.substring(6,14);}
		 taxdebitNarration = SbuCode+"|"+SbuCode+"|"+taxdebitNarration;
	        String costdebitAcctName = approv.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+costdebitAcctNo+"'");
	        String costcreditAcctName = approv.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+costcreditAcctNo+"'");
//   	        System.out.println("ID >>>>>> " + id+"  costdebitAcctNo: "+costdebitAcctNo+"   costcreditAcctNo: "+costcreditAcctNo);
//	        String [] idSplit = id.split("/");
	        String test10 = "select category_name from am_ad_category where Asset_Ledger = '"+costdebitAcctNo+"' and category_code = '"+categoryCode+"'";
	        String test11 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+costdebitAcctNo+"' and category_code = '"+categoryCode+"'";
	        String test12 = "select category_name from am_ad_category where DEP_LEDGER = '"+costdebitAcctNo+"' and category_code = '"+categoryCode+"'";
	        String test13 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+costdebitAcctNo+"' ";
	        String test14 = "select asset_acq_ac from am_gb_company where asset_acq_ac = '"+costdebitAcctNo+"' ";
	        String test15 = "select Fed_Wht_Account from am_gb_company where Fed_Wht_Account = '"+costdebitAcctNo+"' ";
//	        System.out.println("test14 in Number 3 >>>>>> " + test14);
	        costdebitAcctName = approv.getCodeName(test10);
	        if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = approv.getCodeName(test11);}
	        if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = approv.getCodeName(test12);}
	        if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = approv.getCodeName(test13);}
//	        if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = approv.getCodeName(test14);}
	        if(approv.getCodeName(test14)!="" && costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = "Asset Acquisition Suspense Account";}
	        if(approv.getCodeName(test15)!="" && costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = "With Holding Tax Account";}
	        
	        String test20 = "select category_name from am_ad_category where Asset_Ledger = '"+costcreditAcctNo+"' and category_code = '"+categoryCode+"'";
	        String test21 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+costcreditAcctNo+"' and category_code = '"+categoryCode+"'";
	        String test22 = "select category_name from am_ad_category where DEP_LEDGER = '"+costcreditAcctNo+"' and category_code = '"+categoryCode+"'";
	        String test23 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+costcreditAcctNo+"' ";
	        String test24 = "";
	        if(subjectTowht.equals("F")){test24 = "select Fed_Wht_Account from am_gb_company where Fed_Wht_Account = '"+costcreditAcctNo+"' ";}
	        if(subjectTowht.equals("S")){test24 = "select Wht_Account from am_gb_company where Wht_Account = '"+costcreditAcctNo+"' ";}
	        String test25 = "select asset_acq_ac from am_gb_company where asset_acq_ac = '"+costcreditAcctNo+"' ";
//	        System.out.println("test24 in Number 3 >>>>>> " + test24); 
          costcreditAcctName = approv.getCodeName(test20);
          if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test21);}
          if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test22);}
          if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test23);}
//          if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test24);}
          if(approv.getCodeName(test24)!="" && costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = "With Holding Tax Account";}
          if(approv.getCodeName(test25)!="" && costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = "Asset Acquisition Suspense Account";} 			
    if(taxamount!=null)
         {
    	    try
            {
    			taxamount = taxamount.replaceAll(",","");
    			if (supervisor == "" || supervisor == null || supervisor == "*" || supervisor.equals("*"))
    			{
    			 supervisor = "";
    			}
            	if(ad.isoChecking(group_id, page1, transactionId,tranID))
            	{
    				/***************************************************/
            		 iso=bus.transferFund(taxdebitNarration,taxcreditAccount,taxdebitAccount,Double.parseDouble(taxamount),finacleTransId,SbuCode);
            		 //iso=bus.interaccounttransferFund(taxdebitNarration,taxcreditAccount,taxdebitAccount,Double.parseDouble(taxamount),finacleTransId,SbuCode,SbuCode);
    				/***************************************************/
    				//iso="000";
            		ad.updateRaiseEntryTransaction(group_id, page1, transactionId, iso,finacleTransId,"","");
    			}
            	else
            	{
            		raiseMan.raiseEntry(taxdebitAccount,taxcreditAccount,taxdebitAccType,taxcreditAccType,taxdebitTranCode
            		,taxcreditTranCode,taxdebitNarration,
            		taxcreditNarration,Double.parseDouble(taxamount), userId,batchId,cdate,supervisor,group_id,type);
    				/***************************************************/
            		 iso=bus.transferFund(taxdebitNarration,taxcreditAccount,taxdebitAccount ,Double.parseDouble(taxamount),finacleTransId,SbuCode);
            		// iso=bus.interaccounttransferFund(taxdebitNarration,taxcreditAccount,taxdebitAccount ,Double.parseDouble(taxamount),finacleTransId,SbuCode,SbuCode);
    				/***************************************************/
    			// iso="000";
                 ad.insertRaiseEntryTransaction(userId,raiseEntryNarration,taxdebitAccount ,taxcreditAccount,Double.parseDouble(taxamount),iso,group_id,page1,transactionId,systemIp,tranID,assetCode,finacleTransId,costdebitAcctName,costcreditAcctName,userId,recType);

            	}
            	String iso_status=approv.getCodeName(iso_Status_qry + iso +"'");
                if(iso.equalsIgnoreCase("000"))
                {
           	   	 String reversalId = tranID.substring(0, 1);
           	     if(reversalId.equals("R")){tranID = tranID.substring(1, tranID.length());}
              	     if(!reversalId.equals("R")){tranID = "R"+tranID;}
                       String q = "update am_Raisentry_Transaction set iso = '-001' where asset_id='"+id+"' and Trans_id = '"+tranID+"' and transactionId = "+transactionId+"";
                       ad.updateAssetStatusChange(q);	 
                        //System.out.println("#####################################################################  ");

             			msgText1 = message.MailMessage(mail_code, transaction_type)+", \n";
            			msgText1 += "For Group id  "+group_id+"\n";
            	            msgText1 += "For  "+taxdebitNarration+"\n";
            			msgText1 += "Debit Account  "+taxdebitAccount+"\n";
            			msgText1 += "Credit Account  "+taxcreditAccount+"\n";
            			msgText1 += "Amount  "+formata.formatAmount(taxamount)+"\n";
            			msgText1 += "Login here http://172.27.11.192:8080/legend2.net" ;
              			mail.sendMail(to,subject,msgText1);

    					//	System.out.println("Came Here ============ 8");
                		out.println("<script>alert('" + iso_status +"!')</script>");
    					out.println("<script>");
    					//out.println("window.location='DocumentHelp.jsp?np=groupAssetPostingBranch&id="+ group_id+"'" );
    					   out.println("window.location='DocumentHelp.jsp?np=groupAssetPostingBranch&id="+group_id+"&pageDirect=Y&tranId="+tranID+"&assetCode="+assetCode+"'" );
                                        out.println("</script>");
                }
    			else
    			{
    					// System.out.println("Came Here ============ 1");
    					 out.println("<script>alert('" + iso_status +"!')</script>");
    					 out.println("<script>");
    					    out.println("window.location='DocumentHelp.jsp?np=groupAssetPostingBranch&id="+group_id+"&pageDirect=Y&tranId="+tranID+"&assetCode="+assetCode+"'" );
    					 out.println("</script>");
    		    }

    	    }
            catch(Exception e)
            {
            	e.printStackTrace();
            }
    	}  }
   
   
if (pgNum==6)
{
/**
Post Vendor Transaction
*/
String vendordebitAccount = request.getParameter("vendor_dr_account");
String vendordebitAccType = request.getParameter("vendor_dr_accttype");
String vendordebitTranCode = request.getParameter("vendor_dr_trancode");
String vendordebitNarration = request.getParameter("vendor_dr_narration");
String vendorcreditAccount = request.getParameter("vendor_cr_account");
String vendorcreditAccType = request.getParameter("vendor_cr_accttype");
String vendorcreditTranCode = request.getParameter("vendor_cr_trancode");
String vendorcreditNarration = request.getParameter("vendor_cr_narration");
String vendoramount = request.getParameter("vendor_amount");
String costdebitAcctNo = "";
String costcreditAcctNo = "";    
if(vendordebitAccount.substring(0,2)=="NGN"){costdebitAcctNo = vendordebitAccount.substring(8,16);}
if(vendorcreditAccount.substring(0,2)=="NGN"){costcreditAcctNo = vendorcreditAccount.substring(8,16);}  
if(vendordebitAccount.substring(2,5).equals("NGN")){costdebitAcctNo = vendordebitAccount.substring(6,14);}
if(vendorcreditAccount.substring(2,5).equals("NGN")){costcreditAcctNo = vendorcreditAccount.substring(6,14);}
String supervisor = request.getParameter("supervisor");
String recType = request.getParameter("recType");
String type="Group Stock Creation";
SbuCode = request.getParameter("SbuCode");
String transactionId = request.getParameter("transactionIdVendor");
//System.out.println("SbuCode >>>>>> " + SbuCode );
String finacleTransId = approv.getGeneratedTransId();
//  String sbu_code = SbuCode+ "|" +SbuCode;
String sbu_code = SbuCode+SbuCode;
vendordebitNarration = SbuCode+"|"+SbuCode+"|"+vendordebitNarration;
String costdebitAcctName = approv.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+costdebitAcctNo+"'");
String costcreditAcctName = approv.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+costcreditAcctNo+"'");
// String [] idSplit = id.split("/");
String test10 = "select category_name from am_ad_category where Asset_Ledger = '"+costdebitAcctNo+"' and category_code = '"+categoryCode+"'";
String test11 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+costdebitAcctNo+"' and category_code = '"+categoryCode+"'";
String test12 = "select category_name from am_ad_category where DEP_LEDGER = '"+costdebitAcctNo+"' and category_code = '"+categoryCode+"'";
String test13 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+costdebitAcctNo+"' ";
costdebitAcctName = approv.getCodeName(test10);
if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = approv.getCodeName(test11);}
if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = approv.getCodeName(test12);}
if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = approv.getCodeName(test13);}
//System.out.println("costdebitAcctName >>>>>> " + costdebitAcctName );
String test20 = "select category_name from am_ad_category where Asset_Ledger = '"+costcreditAcctNo+"' and category_code = '"+categoryCode+"'";
String test21 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+costcreditAcctNo+"' and category_code = '"+categoryCode+"'";
String test22 = "select category_name from am_ad_category where DEP_LEDGER = '"+costcreditAcctNo+"' and category_code = '"+categoryCode+"'";
String test23 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+costcreditAcctNo+"' ";

costcreditAcctName = approv.getCodeName(test20);
if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test21);}
if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test22);}
if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test23);}
//System.out.println("costcreditAcctName >>>>>> " + costcreditAcctName );
if(vendoramount!=null)
	{
try
{
	vendoramount = vendoramount.replaceAll(",","");
	if (supervisor == "" || supervisor == null || supervisor == "*" || supervisor.equals("*")) 
	{
	 supervisor = "";
	}
	if(ad.isoChecking(group_id, page1, transactionId,tranID))
	{
//		System.out.println("Came Here ============ 1");
		/***************************************************/
		 iso=bus.transferFund(vendordebitNarration,vendorcreditAccount,vendordebitAccount,Double.parseDouble(vendoramount),finacleTransId,SbuCode);
		 //iso=bus.interaccounttransferFund(vendordebitNarration,vendorcreditAccount,vendordebitAccount,Double.parseDouble(vendoramount),finacleTransId,SbuCode,SbuCode);
		/***************************************************/
		//iso="000";
		ad.updateRaiseEntryTransaction(group_id, page1, transactionId, iso,finacleTransId,"","");
		//System.out.println("Came Here ============ 3");
	}
	else
	{
		raiseMan.raiseEntry(vendordebitAccount,vendorcreditAccount,vendordebitAccType,vendorcreditAccType,vendordebitTranCode
		,vendorcreditTranCode,vendordebitNarration,
		vendorcreditNarration,Double.parseDouble(vendoramount), userId,batchId,cdate,supervisor,group_id,type);
		/***************************************************/
		 iso=bus.transferFund(vendordebitNarration,vendorcreditAccount,vendordebitAccount,Double.parseDouble(vendoramount),finacleTransId,SbuCode);
		 //iso=bus.interaccounttransferFund(vendordebitNarration,vendorcreditAccount,vendordebitAccount,Double.parseDouble(vendoramount),finacleTransId,SbuCode,SbuCode);
		/***************************************************/
	// iso="000";
//		 System.out.println("Came Here ============ 3");
    ad.insertRaiseEntryTransaction(userId,vendordebitNarration,vendordebitAccount,vendorcreditAccount,
   		 Double.parseDouble(vendoramount),iso,group_id,page1,transactionId,systemIp,tranID,assetCode,finacleTransId,costdebitAcctName,costcreditAcctName,userId,recType);
    ad.insertVendorTransaction(userId,vendordebitNarration,vendordebitAccount,vendorcreditAccount,Double.parseDouble(vendoramount),location,transactionId,page1,projectCode,vendorId);
	}
	String iso_status=approv.getCodeName(iso_Status_qry + iso +"'");
   if(iso.equalsIgnoreCase("000"))
   {
	   	 String reversalId = tranID.substring(0, 1);
	     if(reversalId.equals("R")){tranID = tranID.substring(1, tranID.length());}
 	     if(!reversalId.equals("R")){tranID = "R"+tranID;}
          String q = "update am_Raisentry_Transaction set iso = '-001' where asset_id='"+id+"' and Trans_id = '"+tranID+"' and transactionId = "+transactionId+"";
          ad.updateAssetStatusChange(q);	 
   	           //System.out.println("#####################################################################  ");
		/*	
			msgText1 = message.MailMessage(mail_code, transaction_type)+", \n";
			msgText1 += "For Group id  "+group_id+"\n";
	            msgText1 += "For  "+vendordebitNarration+"\n";
			msgText1 += "Debit Account  "+vendordebitAccount+"\n";
			msgText1 += "Credit Account  "+vendorcreditAccount+"\n";
			msgText1 += "Amount  "+formata.formatAmount(vendoramount)+"\n";
			msgText1 += "Login here http://172.27.11.192:8080/legend2.net" ;
 			mail.sendMail(to,subject,msgText1);	
						//	System.out.println("Came Here ============ 8");
			   */
              msgText1 = "CREDIT ADVICE \n";
              msgText1 += ""+Subheading+"\n";
   			msgText1 += message.MailMessage(mail_code, transaction_type)+" \n";        			
  			msgText1 += "VENDOR NAME "+vendorname+"\n";
  			msgText1 += "ACCOUNT NUMBER  "+vendorcreditAccount+"\n";
  			msgText1 += "INVOICE NO  "+invNo+"\n";
  			msgText1 += "AMOUNT  "+formata.formatAmount(vendoramount)+"\n";
              msgText1 += "NARRATION  "+vendorcreditNarration +"\n";
              msgText1 += "PAYMENT ID  "+id+"\n";        			        		        			
  			//msgText1 += "TRANSACTION DATE  "+newdate+"\n";
  			msgText1 += ""+summary1+"\n";
  			msgText1 += ""+summary2+"\n";
  			msgText1 += ""+summary3+"\n"; 				   
   		out.println("<script>alert('" + iso_status +"!')</script>");
			out.println("<script>");
                           out.println("window.location='DocumentHelp.jsp?np=groupStockPosting&id="+group_id+"&pageDirect=Y&tranId="+tranID+"&assetCode="+assetCode+"'" );
			//out.println("window.location='DocumentHelp.jsp?np=groupAssetPosting&id="+ group_id+"'" );
			out.println("</script>");
					
   }
	else
	{ 
			 out.println("<script>alert('" + iso_status +"!')</script>");
			 out.println("<script>");
			// out.println("window.location='DocumentHelp.jsp?np=groupAssetPosting&id="+ group_id+"'" );
			out.println("window.location='DocumentHelp.jsp?np=groupStockPosting&id="+group_id+"&pageDirect=Y&tranId="+tranID+"&assetCode="+assetCode+"'" );
                            out.println("</script>");
   }
}
catch(Exception e)
{
	e.printStackTrace();	
}
}
}   

if (pgNum==7)
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
        String taxamount = request.getParameter("wh_amount");
        String supervisor = request.getParameter("supervisor");
        String recType = request.getParameter("recType");
        String type="Group Asset Creation";
        String transactionId = request.getParameter("transactionIdWitholding");
  //      System.out.println("transactionId >>>>>> " + transactionId );
		 String finacleTransId = approv.getGeneratedTransId();
		// String sbu_code = SbuCode+ "|" +SbuCode;
//		    String costdebitAcctNo = taxdebitAccount.substring(8,16);
//		    String costcreditAcctNo = taxcreditAccount.substring(8,16);
 		    String costdebitAcctNo = "";
 		    String costcreditAcctNo = "";  		    
	        if(taxdebitAccount.substring(0,2)=="NGN"){costdebitAcctNo = taxdebitAccount.substring(8,16);}
	        if(taxcreditAccount.substring(0,2)=="NGN"){costcreditAcctNo = taxcreditAccount.substring(8,16);} 
	        if(taxdebitAccount.substring(2,5).equals("NGN")){costdebitAcctNo = taxdebitAccount.substring(6,14);}
	        if(taxcreditAccount.substring(2,5).equals("NGN")){costcreditAcctNo = taxcreditAccount.substring(6,14);}
		 String sbu_code = SbuCode+SbuCode;
		 taxdebitNarration = SbuCode+"|"+SbuCode+"|"+taxdebitNarration;
	        String costdebitAcctName = approv.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+costdebitAcctNo+"'");
	        String costcreditAcctName = approv.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+costcreditAcctNo+"'");
	        System.out.println("ID >>>>>> " + id );
//	        String [] idSplit = id.split("/");
	        String test10 = "select category_name from am_ad_category where Asset_Ledger = '"+costdebitAcctNo+"' and category_code = '"+categoryCode+"'";
	        String test11 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+costdebitAcctNo+"' and category_code = '"+categoryCode+"'";
	        String test12 = "select category_name from am_ad_category where DEP_LEDGER = '"+costdebitAcctNo+"' and category_code = '"+categoryCode+"'";
	        String test13 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+costdebitAcctNo+"' ";
	        costdebitAcctName = approv.getCodeName(test10);
	        if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = approv.getCodeName(test11);}
	        if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = approv.getCodeName(test12);}
	        if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = approv.getCodeName(test13);}
	        
	        String test20 = "select category_name from am_ad_category where Asset_Ledger = '"+costcreditAcctNo+"' and category_code = '"+categoryCode+"'";
	        String test21 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+costcreditAcctNo+"' and category_code = '"+categoryCode+"'";
	        String test22 = "select category_name from am_ad_category where DEP_LEDGER = '"+costcreditAcctNo+"' and category_code = '"+categoryCode+"'";
	        String test23 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+costcreditAcctNo+"' ";
          costcreditAcctName = approv.getCodeName(test20);
          if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test21);}
          if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test22);}
          if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test23);}
if(taxamount!=null)
     {
	    try
        {
			taxamount = taxamount.replaceAll(",","");
			if (supervisor == "" || supervisor == null || supervisor == "*" || supervisor.equals("*")) 
			{
			 supervisor = "";
			}
        	if(ad.isoChecking(group_id, page1, transactionId,tranID))
        	{
				/***************************************************/
        		 iso=bus.transferFund(taxdebitNarration,taxcreditAccount,taxdebitAccount,Double.parseDouble(taxamount),finacleTransId,SbuCode);
        		 //iso=bus.interaccounttransferFund(taxdebitNarration,taxcreditAccount,taxdebitAccount,Double.parseDouble(taxamount),finacleTransId,SbuCode,SbuCode);
				/***************************************************/
				//iso="000";
        		ad.updateRaiseEntryTransaction(group_id, page1, transactionId, iso,finacleTransId,"","");
			}
        	else
        	{
        		raiseMan.raiseEntry(taxdebitAccount,taxcreditAccount,taxdebitAccType,taxcreditAccType,taxdebitTranCode
        		,taxcreditTranCode,taxdebitNarration,
        		taxcreditNarration,Double.parseDouble(taxamount), userId,batchId,cdate,supervisor,group_id,type);
				/***************************************************/
        		 iso=bus.transferFund(taxdebitNarration,taxcreditAccount,taxdebitAccount ,Double.parseDouble(taxamount),finacleTransId,SbuCode);
        		 //iso=bus.interaccounttransferFund(taxdebitNarration,taxcreditAccount,taxdebitAccount ,Double.parseDouble(taxamount),finacleTransId,SbuCode,SbuCode);
				/***************************************************/
			// iso="000";
             ad.insertRaiseEntryTransaction(userId,taxdebitNarration,taxdebitAccount ,taxcreditAccount,Double.parseDouble(taxamount),iso,group_id,page1,transactionId,systemIp,tranID,assetCode,finacleTransId,costdebitAcctName,costcreditAcctName,userId,recType);
			
        	}
        	String iso_status=approv.getCodeName(iso_Status_qry + iso +"'");
            if(iso.equalsIgnoreCase("000"))
            {
       	   	 String reversalId = tranID.substring(0, 1);
       	     if(reversalId.equals("R")){tranID = tranID.substring(1, tranID.length());}
          	     if(!reversalId.equals("R")){tranID = "R"+tranID;}
                   String q = "update am_Raisentry_Transaction set iso = '-001' where asset_id='"+id+"' and Trans_id = '"+tranID+"' and transactionId = "+transactionId+"";
                   ad.updateAssetStatusChange(q);	 
                    //System.out.println("#####################################################################  ");
					
         			msgText1 = message.MailMessage(mail_code, transaction_type)+", \n";
        			msgText1 += "For Group id  "+group_id+"\n";
        	            msgText1 += "For  "+taxdebitNarration+"\n";
        			msgText1 += "Debit Account  "+taxdebitAccount+"\n";
        			msgText1 += "Credit Account  "+taxcreditAccount+"\n";
        			msgText1 += "Amount  "+formata.formatAmount(taxamount)+"\n";
        			msgText1 += "Login here http://172.27.11.192:8080/legendPlus.net" ;
          			mail.sendMail(to,subject,msgText1);	
					
					//	System.out.println("Came Here ============ 8");
            		out.println("<script>alert('" + iso_status +"!')</script>");
					out.println("<script>");
                                    out.println("window.location='DocumentHelp.jsp?np=groupStockPosting&id="+group_id+"&pageDirect=Y&tranId="+tranID+"&assetCode="+assetCode+"'" );
					//out.println("window.location='DocumentHelp.jsp?np=groupAssetPosting&id="+ group_id+"'" );
					out.println("</script>");
            }
			else
			{ 
					// System.out.println("Came Here ============ 1");
					 out.println("<script>alert('" + iso_status +"!')</script>");
					 out.println("<script>");
					// out.println("window.location='DocumentHelp.jsp?np=groupAssetPosting&id="+ group_id+"'" );
					 out.println("window.location='DocumentHelp.jsp?np=groupStockPosting&id="+group_id+"&pageDirect=Y&tranId="+tranID+"&assetCode="+assetCode+"'" );
                                     out.println("</script>");
		    }
            
	    }
        catch(Exception e)
        {
        	e.printStackTrace();	
        }
	}  }


if (pgNum==8)
{
/**
Post Vendor Transaction
*/
String vendordebitAccount = request.getParameter("vendor_dr_account");
String vendordebitAccType = request.getParameter("vendor_dr_accttype");
String vendordebitTranCode = request.getParameter("vendor_dr_trancode");
String vendordebitNarration = request.getParameter("vendor_dr_narration");
String vendorcreditAccount = request.getParameter("vendor_cr_account");
String vendorcreditAccType = request.getParameter("vendor_cr_accttype");
String vendorcreditTranCode = request.getParameter("vendor_cr_trancode");
String vendorcreditNarration = request.getParameter("vendor_cr_narration");
String vendoramount = request.getParameter("vendor_amount");
String costdebitAcctNo = "";
String costcreditAcctNo = "";    
if(vendordebitAccount.substring(0,2)=="NGN"){costdebitAcctNo = vendordebitAccount.substring(8,16);}
if(vendorcreditAccount.substring(0,2)=="NGN"){costcreditAcctNo = vendorcreditAccount.substring(8,16);}   
if(vendordebitAccount.substring(2,5).equals("NGN")){costdebitAcctNo = vendordebitAccount.substring(6,14);}
if(vendorcreditAccount.substring(2,5).equals("NGN")){costcreditAcctNo = vendorcreditAccount.substring(6,14);}
String supervisor = request.getParameter("supervisor");
String recType = request.getParameter("recType");
String type="Group Stock Creation";
SbuCode = request.getParameter("SbuCode");
String transactionId = request.getParameter("transactionIdVendor");
//System.out.println("SbuCode >>>>>> " + SbuCode );
String finacleTransId = approv.getGeneratedTransId();
//String sbu_code = SbuCode+ "|" +SbuCode;
String sbu_code = SbuCode+SbuCode;
vendordebitNarration = SbuCode+"|"+SbuCode+"|"+vendordebitNarration;
String costdebitAcctName = approv.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+costdebitAcctNo+"'");
String costcreditAcctName = approv.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+costcreditAcctNo+"'");
//String [] idSplit = id.split("/");
String test10 = "select category_name from am_ad_category where Asset_Ledger = '"+costdebitAcctNo+"' and category_code = '"+categoryCode+"'";
String test11 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+costdebitAcctNo+"' and category_code = '"+categoryCode+"'";
String test12 = "select category_name from am_ad_category where DEP_LEDGER = '"+costdebitAcctNo+"' and category_code = '"+categoryCode+"'";
String test13 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+costdebitAcctNo+"' ";
costdebitAcctName = approv.getCodeName(test10);
if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = approv.getCodeName(test11);}
if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = approv.getCodeName(test12);}
if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = approv.getCodeName(test13);}
System.out.println("costdebitAcctName >>>>>> " + costdebitAcctName );
String test20 = "select category_name from am_ad_category where Asset_Ledger = '"+costcreditAcctNo+"' and category_code = '"+categoryCode+"'";
String test21 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+costcreditAcctNo+"' and category_code = '"+categoryCode+"'";
String test22 = "select category_name from am_ad_category where DEP_LEDGER = '"+costcreditAcctNo+"' and category_code = '"+categoryCode+"'";
String test23 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+costcreditAcctNo+"' ";

costcreditAcctName = approv.getCodeName(test20);
if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test21);}
if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test22);}
if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test23);}
//System.out.println("costcreditAcctName >>>>>> " + costcreditAcctName );
if(vendoramount!=null)
	{
try
{
	vendoramount = vendoramount.replaceAll(",","");
	if (supervisor == "" || supervisor == null || supervisor == "*" || supervisor.equals("*")) 
	{
	 supervisor = "";
	}
	if(ad.isoChecking(group_id, page1, transactionId,tranID))
	{
//		System.out.println("Came Here ============ 1");
		/***************************************************/
		 iso=bus.transferFund(vendordebitNarration,vendorcreditAccount,vendordebitAccount,Double.parseDouble(vendoramount),finacleTransId,SbuCode);
		 //iso=bus.interaccounttransferFund(vendordebitNarration,vendorcreditAccount,vendordebitAccount,Double.parseDouble(vendoramount),finacleTransId,SbuCode,SbuCode);
		/***************************************************/
		//iso="000";
		ad.updateRaiseEntryTransaction(group_id, page1, transactionId, iso,finacleTransId,"","");
		//System.out.println("Came Here ============ 3");
	}
	else
	{
		raiseMan.raiseEntry(vendordebitAccount,vendorcreditAccount,vendordebitAccType,vendorcreditAccType,vendordebitTranCode
		,vendorcreditTranCode,vendordebitNarration,
		vendorcreditNarration,Double.parseDouble(vendoramount), userId,batchId,cdate,supervisor,group_id,type);
		/***************************************************/
		 iso=bus.transferFund(vendordebitNarration,vendorcreditAccount,vendordebitAccount,Double.parseDouble(vendoramount),finacleTransId,SbuCode);
		 //iso=bus.interaccounttransferFund(vendordebitNarration,vendorcreditAccount,vendordebitAccount,Double.parseDouble(vendoramount),finacleTransId,SbuCode,SbuCode);
		/***************************************************/
	// iso="000";
		 System.out.println("Came Here ============ 3");
 ad.insertRaiseEntryTransaction(userId,vendordebitNarration,vendordebitAccount,vendorcreditAccount,
		 Double.parseDouble(vendoramount),iso,group_id,page1,transactionId,systemIp,tranID,assetCode,finacleTransId,costdebitAcctName,costcreditAcctName,userId,recType);
 ad.insertVendorTransaction(userId,vendordebitNarration,vendordebitAccount,vendorcreditAccount,Double.parseDouble(vendoramount),location,transactionId,page1,projectCode,vendorId);
	}
	String iso_status=approv.getCodeName(iso_Status_qry + iso +"'");
if(iso.equalsIgnoreCase("000"))
{
  	 String reversalId = tranID.substring(0, 1);
    if(reversalId.equals("R")){tranID = tranID.substring(1, tranID.length());}
	     if(!reversalId.equals("R")){tranID = "R"+tranID;}
       String q = "update am_Raisentry_Transaction set iso = '-001' where asset_id='"+id+"' and Trans_id = '"+tranID+"' and transactionId = "+transactionId+"";
       ad.updateAssetStatusChange(q);	 
	           //System.out.println("#####################################################################  ");
		/*	
			msgText1 = message.MailMessage(mail_code, transaction_type)+", \n";
			msgText1 += "For Group id  "+group_id+"\n";
	            msgText1 += "For  "+vendordebitNarration+"\n";
			msgText1 += "Debit Account  "+vendordebitAccount+"\n";
			msgText1 += "Credit Account  "+vendorcreditAccount+"\n";
			msgText1 += "Amount  "+formata.formatAmount(vendoramount)+"\n";
			msgText1 += "Login here http://172.27.11.192:8080/legend2.net" ;
			mail.sendMail(to,subject,msgText1);	
						//	System.out.println("Came Here ============ 8");
			   */
           msgText1 = "CREDIT ADVICE \n";
           msgText1 += ""+Subheading+"\n";
			msgText1 += message.MailMessage(mail_code, transaction_type)+" \n";        			
			msgText1 += "STOCK ACCOUNT NAME "+vendorname+"\n";
			msgText1 += "BRANCH ACCOUNT NUMBER  "+vendorcreditAccount+"\n";
			msgText1 += "INVOICE NO  "+invNo+"\n";
			msgText1 += "AMOUNT  "+formata.formatAmount(vendoramount)+"\n";
           msgText1 += "NARRATION  "+vendorcreditNarration +"\n";
           msgText1 += "PAYMENT ID  "+id+"\n";        			        		        			
			//msgText1 += "TRANSACTION DATE  "+newdate+"\n";
			msgText1 += ""+summary1+"\n";
			msgText1 += ""+summary2+"\n";
			msgText1 += ""+summary3+"\n"; 				   
		out.println("<script>alert('" + iso_status +"!')</script>");
			out.println("<script>");
                        out.println("window.location='DocumentHelp.jsp?np=bulkStockIssuancePay&id="+group_id+"&pageDirect=Y&tranId="+tranID+"&assetCode="+assetCode+"'" );
			//out.println("window.location='DocumentHelp.jsp?np=groupAssetPosting&id="+ group_id+"'" );
			out.println("</script>");
					
}
	else 
	{ 
			 out.println("<script>alert('" + iso_status +"!')</script>");
			 out.println("<script>");
			// out.println("window.location='DocumentHelp.jsp?np=groupAssetPosting&id="+ group_id+"'" );
			out.println("window.location='DocumentHelp.jsp?np=bulkStockIssuancePay&id="+group_id+"&pageDirect=Y&tranId="+tranID+"&assetCode="+assetCode+"'" );
                         out.println("</script>");
}
}
catch(Exception e)
{
	e.printStackTrace();	
}
}
} 

if (pgNum==9)
{
//		System.out.println("<<<<<<<<<<<<< Process Type Vat>>>>>>>>>>> 3");
		
       /**
        Post TAX Transaction
        */
		String vat = request.getParameter("vat");
        String taxdebitAccount = request.getParameter("vat_dr_account");
     //   System.out.println("taxdebitAccount >>>>>> " + taxdebitAccount );
        
        String taxdebitAccType = request.getParameter("vat_dr_accttype");
      //  System.out.println("taxdebitAccType >>>>>> " + taxdebitAccType );
        
        String taxdebitTranCode = request.getParameter("vat_dr_trancode");
      //  System.out.println("taxdebitTranCode >>>>>> " + taxdebitTranCode );
        
        String taxdebitNarration = request.getParameter("vat_dr_narration");
      //  System.out.println("taxdebitNarration >>>>>> " + taxdebitNarration );
        
        String taxcreditAccount = request.getParameter("vat_cr_account");
      //  System.out.println("taxcreditAccount >>>>>> " + taxcreditAccount );
        
        String taxcreditAccType = request.getParameter("vat_cr_accttype");
      //  System.out.println("taxcreditAccType >>>>>> " + taxcreditAccType );
        
        String taxcreditTranCode = request.getParameter("vat_cr_trancode");
    //    System.out.println("taxcreditTranCode >>>>>> " + taxcreditTranCode );
        
        String taxcreditNarration = request.getParameter("vat_cr_narration");
    //    System.out.println("taxcreditNarration >>>>>> " + taxcreditNarration );
        
        String taxamount = request.getParameter("vat_amount");
//        System.out.println("taxamount >>>>>> " + taxamount );
        
        String supervisor = request.getParameter("supervisor");
        String recType = request.getParameter("recType");
      //  System.out.println("supervisor >>>>>> " + supervisor );
       // String SbuCode = request.getParameter("SbuCode");
        String type="Group Asset Creation";
        String transactionId = request.getParameter("transactionIdVat");
//        System.out.println("transactionId >>>>>> " + transactionId );
		 String finacleTransId = approv.getGeneratedTransId();
		// String sbu_code = SbuCode+ "|" +SbuCode;
//		    String costdebitAcctNo = taxdebitAccount.substring(8,16);
//		    String costcreditAcctNo = taxcreditAccount.substring(8,16);
 		    String costdebitAcctNo = taxdebitAccount;
 		    String costcreditAcctNo = taxcreditAccount;  		
// 		    System.out.println("<<<<<<<<<<<<<taxdebitAccount in GroupProcess >>>>>> "+taxdebitAccount);
// 		    System.out.println("taxdebitAccount.substring(8,16) in GroupProcess >>>>>> " + taxdebitAccount.substring(8,16));
	        if(taxdebitAccount.substring(0,3).equalsIgnoreCase("NGN")){costdebitAcctNo = taxdebitAccount.substring(8,16);}
	        if(taxcreditAccount.substring(0,3).equalsIgnoreCase("NGN")){costcreditAcctNo = taxcreditAccount.substring(8,16);}  
	        if(taxdebitAccount.substring(2,5).equalsIgnoreCase("NGN")){costdebitAcctNo = taxdebitAccount.substring(6,14);}
	        if(taxcreditAccount.substring(2,5).equalsIgnoreCase("NGN")){costcreditAcctNo = taxcreditAccount.substring(6,14);}
//	        System.out.println("SbuCode in GroupProcess >>>>>> " + SbuCode+"   costdebitAcctNo: "+costdebitAcctNo+"   costcreditAcctNo: "+costcreditAcctNo );
		 String sbu_code = SbuCode+SbuCode;
		 taxdebitNarration = SbuCode+"|"+SbuCode+"|"+taxdebitNarration;
	        String costdebitAcctName = approv.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+costdebitAcctNo+"'");
	        String costcreditAcctName = approv.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+costcreditAcctNo+"'");
//	        System.out.println("ID >>>>>> " + id );
//	        String [] idSplit = id.split("/");
	        String test10 = "select category_name from am_ad_category where Asset_Ledger = '"+costdebitAcctNo+"' and category_code = '"+categoryCode+"'";
	        String test11 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+costdebitAcctNo+"' and category_code = '"+categoryCode+"'";
	        String test12 = "select category_name from am_ad_category where DEP_LEDGER = '"+costdebitAcctNo+"' and category_code = '"+categoryCode+"'";
	        String test13 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+costdebitAcctNo+"' ";
	        String test14 = "select asset_acq_ac from am_gb_company where asset_acq_ac = '"+costdebitAcctNo+"' ";
//	        System.out.println("test14 0>>>>>> " + test14);
	        costdebitAcctName = approv.getCodeName(test10);
//	        System.out.println("costdebitAcctName 1>>>>>> " + costdebitAcctName);
	        if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = approv.getCodeName(test11);}
//	        System.out.println("costdebitAcctName 2>>>>>> " + costdebitAcctName);
	        if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = approv.getCodeName(test12);}
//	        System.out.println("costdebitAcctName 3>>>>>> " + costdebitAcctName);
	        if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = approv.getCodeName(test13);}
//	        System.out.println("costdebitAcctName 4>>>>>> " + costdebitAcctName);
	        if(costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = approv.getCodeName(test14);}
//	        System.out.println("costdebitAcctName 5>>>>>> " + costdebitAcctName);
//	        System.out.println("costdebitAcctName 1>>>>>> " + costdebitAcctName+"   vat: "+vat);
	        if(!costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = "Asset Acquisition Suspense Account";}
//	        System.out.println("costdebitAcctName 6>>>>>> " + costdebitAcctName);
	          if(costdebitAcctName.equalsIgnoreCase("") && vat.equalsIgnoreCase("N")){costdebitAcctName = approv.getCodeName("select SelfChargeVAT from am_gb_company where SelfChargeVAT = '"+costdebitAcctNo+"' "); 
	          costdebitAcctName = "Self Charge VAT Account";}
	          if(costdebitAcctName.equalsIgnoreCase("") && vat.equalsIgnoreCase("Y")){costdebitAcctName = approv.getCodeName("select Vat_Account from am_gb_company where Vat_Account = '"+costdebitAcctNo+"' ");
	          costdebitAcctName = "VAT Account";}	
	          
	        String test20 = "select category_name from am_ad_category where Asset_Ledger = '"+costcreditAcctNo+"' and category_code = '"+categoryCode+"'";
	        String test21 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+costcreditAcctNo+"' and category_code = '"+categoryCode+"'";
	        String test22 = "select category_name from am_ad_category where DEP_LEDGER = '"+costcreditAcctNo+"' and category_code = '"+categoryCode+"'";
	        String test23 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+costcreditAcctNo+"' ";
	        String test24 = "select asset_acq_ac from am_gb_company where asset_acq_ac = '"+costcreditAcctNo+"' ";
//	        String test24 = "";
//	          if(approv.getCodeName(test14)!="" && costdebitAcctName.equalsIgnoreCase("")){costdebitAcctName = "Asset Acquisition Suspense Account";}
          costcreditAcctName = approv.getCodeName(test20);
//          System.out.println("costcreditAcctName 1>>>>>> " + costcreditAcctName);
          if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test21);}
//          System.out.println("costcreditAcctName 2>>>>>> " + costcreditAcctName);
          if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test22);}
//          System.out.println("costcreditAcctName 3>>>>>> " + costcreditAcctName);
          if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test23);}
//          System.out.println("costcreditAcctName 4>>>>>> " + costcreditAcctName);
          if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test24);}
          if(!costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = "Asset Acquisition Suspense Account";}
 //         if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test24);costcreditAcctName = "Asset Acquisition Suspense Account";}
//          if(approv.getCodeName(test24)!="" && costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = "Asset Acquisition Account";}
//          if(costdebitAcctName==""){costdebitAcctName = "Asset Acquisition Account";}
//          System.out.println("<<<<< costdebitAcctName >>>>>> " + costdebitAcctName);
          
//	        System.out.println("<<<<<<<=====costcreditAcctName: "+costcreditAcctName+"   costdebitAcctName: "+costdebitAcctName+"  VAT: "+vat);
          if(costcreditAcctName.equalsIgnoreCase("") && vat.equalsIgnoreCase("N")){costcreditAcctName = approv.getCodeName("select SelfChargeVAT from am_gb_company where SelfChargeVAT = '"+costcreditAcctNo+"' "); 
          costcreditAcctName = "Self Charge VAT Account";}
          if(costcreditAcctName.equalsIgnoreCase("") && vat.equalsIgnoreCase("Y")){costcreditAcctName = approv.getCodeName("select Vat_Account from am_gb_company where Vat_Account = '"+costcreditAcctNo+"' ");
          costcreditAcctName = "VAT Account";}	
//          System.out.println("costcreditAcctsName >>>>>> " + costcreditAcctName+"   costcreditAcctName: "+costcreditAcctName);
if(taxamount!=null)
     {
	    try
        {
			taxamount = taxamount.replaceAll(",","");
			if (supervisor == "" || supervisor == null || supervisor == "*" || supervisor.equals("*")) 
			{
			 supervisor = "";
			}
        	if(ad.isoChecking(group_id, page1, transactionId,tranID))
        	{
//        		 System.out.println("Existing VAT Transaction  >>>>>> ");
				/***************************************************/
        		 iso=bus.transferFund(taxdebitNarration,taxcreditAccount,taxdebitAccount,Double.parseDouble(taxamount),finacleTransId,SbuCode);
        		 //iso=bus.interaccounttransferFund(taxdebitNarration,taxcreditAccount,taxdebitAccount,Double.parseDouble(taxamount),finacleTransId,SbuCode,SbuCode);
				/***************************************************/
				//iso="000";
        		ad.updateRaiseEntryTransaction(group_id, page1, transactionId, iso,finacleTransId,"","");
			}
        	else
        	{
//        		System.out.println("VAT Transaction not Exist  >>>>>> ");
        		raiseMan.raiseEntry(taxdebitAccount,taxcreditAccount,taxdebitAccType,taxcreditAccType,taxdebitTranCode
        		,taxcreditTranCode,taxdebitNarration,
        		taxcreditNarration,Double.parseDouble(taxamount), userId,batchId,cdate,supervisor,group_id,type);
				/***************************************************/
        		 iso=bus.transferFund(taxdebitNarration,taxcreditAccount,taxdebitAccount ,Double.parseDouble(taxamount),finacleTransId,SbuCode);
        		 //iso=bus.interaccounttransferFund(taxdebitNarration,taxcreditAccount,taxdebitAccount ,Double.parseDouble(taxamount),finacleTransId,SbuCode,SbuCode);
				/***************************************************/
			// iso="000";
             ad.insertRaiseEntryTransaction(userId,taxdebitNarration,taxdebitAccount ,taxcreditAccount,Double.parseDouble(taxamount),iso,group_id,page1,transactionId,systemIp,tranID,assetCode,finacleTransId,costdebitAcctName,costcreditAcctName,userId,recType);
			
        	}
        	String iso_status=approv.getCodeName(iso_Status_qry + iso +"'");
            if(iso.equalsIgnoreCase("000"))
            {
       	   	 String reversalId = tranID.substring(0, 1);
       	     if(reversalId.equals("R")){tranID = tranID.substring(1, tranID.length());}
          	     if(!reversalId.equals("R")){tranID = "R"+tranID;}
                   String q = "update am_Raisentry_Transaction set iso = '-001' where asset_id='"+id+"' and Trans_id = '"+tranID+"' and transactionId = "+transactionId+"";
                   ad.updateAssetStatusChange(q);	 
                    //System.out.println("#####################################################################  ");
					
         			msgText1 = message.MailMessage(mail_code, transaction_type)+", \n";
        			msgText1 += "For Group id  "+group_id+"\n";
        	            msgText1 += "For  "+taxdebitNarration+"\n";
        			msgText1 += "Debit Account  "+taxdebitAccount+"\n";
        			msgText1 += "Credit Account  "+taxcreditAccount+"\n";
        			msgText1 += "Amount  "+formata.formatAmount(taxamount)+"\n";
        			msgText1 += "Login here http://172.27.11.192:8080/legend2.net" ;
          			mail.sendMail(to,subject,msgText1);	
					
					//	System.out.println("Came Here ============ 8");
            		out.println("<script>alert('" + iso_status +"!')</script>");
					out.println("<script>");
                                    out.println("window.location='DocumentHelp.jsp?np=groupAssetPosting&id="+group_id+"&pageDirect=Y&tranId="+tranID+"&assetCode="+assetCode+"'" );
					//out.println("window.location='DocumentHelp.jsp?np=groupAssetPosting&id="+ group_id+"'" );
					out.println("</script>");
            }
			else
			{ 
					// System.out.println("Came Here ============ 1");
					 out.println("<script>alert('" + iso_status +"!')</script>");
					 out.println("<script>");
					// out.println("window.location='DocumentHelp.jsp?np=groupAssetPosting&id="+ group_id+"'" );
					 out.println("window.location='DocumentHelp.jsp?np=groupAssetPosting&id="+group_id+"&pageDirect=Y&tranId="+tranID+"&assetCode="+assetCode+"'" );
                                     out.println("</script>");
		    }
            
	    }
        catch(Exception e)
        {
        	e.printStackTrace();	
        }
	}  }


if (pgNum==222)
{

try
{  
    String query= "UPDATE a SET a.Asset_Id = b.NEW_ASSET_ID,a.OLD_ASSET_ID = b.Asset_Id,a.Branch_ID = b.newbranch_id," +
            "a.BRANCH_CODE = b.NEW_BRANCH_CODE,a.SBU_CODE = b.newSBU_CODE,a.Section_id = b.newsection_id," +
            "a.dept_id = b.newdept_id,a.DEPT_CODE = b.NEW_DEPT_CODE,a.SECTION_CODE = b.NEW_SECTION_CODE," +
            "a.Asset_User = b.newAsset_User FROM am_asset a,am_gb_bulkTransfer b WHERE a.Asset_Id = b.Asset_Id " +
            "AND b.Batch_Id = '"+id+"'";
    ad.updateAssetStatusChange(query);
//    System.out.println("======query in GroupPrpcess: "+query);
	String q = "update am_gb_bulkTransfer set STATUS = 'POSTED' where Batch_id = '"+id+"'";
	ad.updateAssetStatusChange(q);
	String aq = "update am_raisentry_post set entryPostFlag = 'Y',GroupIdStatus = 'Y' where id = '"+id+"'";
	ad.updateAssetStatusChange(aq);	
	
			out.println("<script>");
   //       out.println("window.location='DocumentHelp.jsp?np=bulkAssetTransferPosting&id="+group_id+"&pageDirect=Y&tranId="+group_id+" '" );
			out.println("window.location='DocumentHelp.jsp?np=raiseEntry'" );
			out.println("</script>");
					

}
catch(Exception e)
{
	e.printStackTrace();	
}
}

    }else {
		out.print("<script>alert('You have No Right')</script>");
	}
}  
public String getServletInfo()
    {
        return "Group Process   Servlet";
    }
}