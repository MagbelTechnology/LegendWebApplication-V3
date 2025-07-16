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

public class ProcessIssuanceCost extends HttpServlet
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
	
    public ProcessIssuanceCost()
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
        String userClass = (String) session.getAttribute("UserClass");
        
        String userId =(String)session.getAttribute("CurrentUser");
       // asset_id = request.getParameter("asset_id").trim();
        group_id = request.getParameter("group_id");       
        partPay = request.getParameter("partPay");
        //deferPay = request.getParameter("deferPay");
        //processCost = request.getParameter("processCost").trim();
        String systemIp = request.getRemoteAddr() ;
//        int pgNum=Integer.parseInt(request.getParameter("pageNum"));
        String SbuCode = request.getParameter("SbuCode");   
        String vendorname = request.getParameter("vendorname");
        String invNo = request.getParameter("invNo");
        String id = request.getParameter("group_id");
        String categoryCode = request.getParameter("categoryCode");
        int assetCode = request.getParameter("assetCode") == null?0:Integer.parseInt(request.getParameter("assetCode"));
       String  tranID = request.getParameter("tranID");// == null?0:Integer.parseInt(request.getParameter("tranID"));
        System.out.println("tranID in classs >>>>>> " + tranID+"  ====Id: "+id);
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
        
 //   if (pgNum==2)
//    {
    //	System.out.println("<<<<<<<<<<<<< Process Type Vendor Account >>>>>>>>>>> 2");
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
    if(vendordebitAccount.substring(2,5)=="NGN"){costdebitAcctNo = vendordebitAccount.substring(6,14);}
    if(vendorcreditAccount.substring(0,2)=="NGN"){costcreditAcctNo = vendorcreditAccount.substring(8,16);}
    if(vendorcreditAccount.substring(2,5)=="NGN"){costcreditAcctNo = vendorcreditAccount.substring(6,14);}
    String supervisor = request.getParameter("supervisor");
    String recType = request.getParameter("recType");
  //  System.out.println("ID >>>>>> " + id+"    recType: "+recType);
    String type="Group Asset Creation";
    SbuCode = request.getParameter("SbuCode");
    String transactionId = request.getParameter("transactionIdVendor");
    System.out.println("SbuCode >>>>>> " + SbuCode );
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
    System.out.println("costdebitAcctName >>>>>> " + costdebitAcctName );
    String test20 = "select category_name from am_ad_category where Asset_Ledger = '"+costcreditAcctNo+"' and category_code = '"+categoryCode+"'";
    String test21 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+costcreditAcctNo+"' and category_code = '"+categoryCode+"'";
    String test22 = "select category_name from am_ad_category where DEP_LEDGER = '"+costcreditAcctNo+"' and category_code = '"+categoryCode+"'";
    String test23 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+costcreditAcctNo+"' ";
    
    costcreditAcctName = approv.getCodeName(test20);
    if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test21);}
    if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test22);}
    if(costcreditAcctName.equalsIgnoreCase("")){costcreditAcctName = approv.getCodeName(test23);}
    System.out.println("costcreditAcctName >>>>>> " + costcreditAcctName );
    System.out.println("costcreditAcctName >>>>>> " + costcreditAcctName );
    if(vendoramount!=null)
    	{
    try
    {
    	if (!userClass.equals("NULL") || userClass!=null){
		vendoramount = vendoramount.replaceAll(",","");
		if (supervisor == "" || supervisor == null || supervisor == "*" || supervisor.equals("*")) 
		{
		 supervisor = "";
		}
    	if(ad.isoChecking(group_id, page1, transactionId,tranID))
    	{
			/***************************************************/
    		 iso=bus.transferFund(vendordebitNarration,vendorcreditAccount,vendordebitAccount,Double.parseDouble(vendoramount),finacleTransId,SbuCode);
    		 //iso=bus.interaccounttransferFund(vendordebitNarration,vendorcreditAccount,vendordebitAccount,Double.parseDouble(vendoramount),finacleTransId,SbuCode,SbuCode);
			/***************************************************/
			//iso="000";
    		ad.updateRaiseEntryTransaction(group_id, page1, transactionId, iso,finacleTransId,"","");
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
         ad.insertRaiseEntryTransaction(userId,vendordebitNarration,vendorcreditAccount,vendordebitAccount,
        		 Double.parseDouble(vendoramount),iso,group_id,page1,transactionId,systemIp,tranID,assetCode,finacleTransId,costdebitAcctName,costcreditAcctName,userId,recType);
		
    	} 
    	System.out.println("=====ISO: "+iso+"    ====group_id: "+group_id+"  Id==: "+id);
   /*     if(iso.equalsIgnoreCase("000"))
        { 
            String q = "update am_asset_approval set process_status = 'A' where asset_id='"+id+"'";
            ad.updateAssetStatusChange(q);		
        }*/
    	String iso_status=approv.getCodeName(iso_Status_qry + iso +"'");
        if(iso.equalsIgnoreCase("000"))
        {
   	   	 String reversalId = tranID.substring(0, 1);
   	     if(reversalId.equals("R")){tranID = tranID.substring(1, tranID.length());}
      	     if(!reversalId.equals("R")){tranID = "R"+tranID;}
               String q = "update am_Raisentry_Transaction set iso = '-001' where asset_id='"+group_id+"' and Trans_id = '"+tranID+"' and transactionId = "+transactionId+"";             
               ad.updateAssetStatusChange(q);	           	
        	ad.updateAssetStatusChange("update am_raisentry_post set entrypostflag='Y', GroupIdStatus = 'Y' where id='"+id+"' and page='STOCK ISSUANCE RAISE ENTRY' "); 
        	ad.updateAssetStatusChange("update am_asset_approval set PROCESS_STATUS='A' where  asset_id = '"+id+"' and tran_type ='Bulk Stock Transfer'"); 
        	String apq = "update am_asset_approval set PROCESS_STATUS='A' where  asset_id = '"+id+"' and tran_type ='Bulk Stock Transfer'";
        	
        	  System.out.println("=====apq: "+apq);
                   msgText1 = "CREDIT ADVICE \n";
                   msgText1 += ""+Subheading+"\n";
        			msgText1 += message.MailMessage(mail_code, transaction_type)+" \n";        			
       			msgText1 += "REQUESTED BY "+vendorname+"\n";
       			msgText1 += "ACCOUNT NUMBER  "+vendorcreditAccount+"\n";
       			msgText1 += "BATCH ID  "+group_id+"\n";
       			msgText1 += "AMOUNT  "+formata.formatAmount(vendoramount)+"\n";
                   msgText1 += "NARRATION  "+vendorcreditNarration +"\n";
                   msgText1 += "PAYMENT ID  "+id+"\n";        			        		        			
       			//msgText1 += "TRANSACTION DATE  "+newdate+"\n";
       			msgText1 += ""+summary1+"\n";
       			msgText1 += ""+summary2+"\n";
       			msgText1 += ""+summary3+"\n"; 				   
        		out.println("<script>alert('" + iso_status +"!')</script>");
				out.println("<script>");
               // out.println("window.location='DocumentHelp.jsp?np=StockIssuanceTransactionPostings&id="+group_id+"&pageDirect=Y&tranId="+tranID+"&assetCode="+assetCode+"'" );
				out.println("window.location='DocumentHelp.jsp?np=StockIssuanceTransactionPostings&id="+ group_id+"'" );
				out.println("</script>");
						
        }
		else
		{ 
				 out.println("<script>alert('" + iso_status +"!')</script>");
				 out.println("<script>");
				 out.println("window.location='DocumentHelp.jsp?np=StockIssuanceTransactionPostings&id="+ group_id+"'" );
				// out.println("window.location='DocumentHelp.jsp?np=StockIssuanceTransactionPostings&id="+group_id+"&pageDirect=Y&tranId="+tranID+"&assetCode="+assetCode+"'" );
                                 out.println("</script>");
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
 //   }

}  
public String getServletInfo()
    {
        return "Group Process   Servlet";
    }
}