package com.magbel.legend.servlet;



import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.vao.Approval;

import java.io.*;

import magma.AssetRecordsBean;
import magma.net.dao.MagmaDBConnection;
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
import com.magbel.util.CurrentDateTime;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class ProcessCreateVendor extends HttpServlet
{

	private DepreciationProcessingManager service;
	private FinancialExchangeServiceBus bus;
	private RaiseEntryManager raiseMan;
	private EmailSmsServiceBus mail ;
	private Codes message;
	private AssetRecordsBean ad;
	private CurrencyNumberformat formata = new CurrencyNumberformat();
	private SimpleDateFormat sdf;
	private SimpleDateFormat df;
	private ApprovalRecords records;
	private CurrentDateTime datetime;
	private MagmaDBConnection dbConnection;
    public ProcessCreateVendor()
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
//        String page1 = "ASSET CREATION RAISE ENTRY";
        String iso;
        String msgText1 ="";
        String to ="";
        String mail_code="1";
        String subject = "Raise Entry";
        String transaction_type="Raise_Entry";
        String aquisitiondebitAcctName = "";
        String batchId = "RE"+Integer.toString((int)(Math.random()*8000)+1000);
        String cdate = sdf.format(new Date());
        /**
        Vendor Transaction
        **/        	
        String page1 = request.getParameter("page1");
        String companyName = request.getParameter("compName");
        String vendordebitAccount = request.getParameter("vendor_dr_account");
        String vendordebitAcct = request.getParameter("vendor_dr_account");
        String vendorcreditAccount = request.getParameter("vendor_cr_account");
        System.out.println("====vendordebitAccount: "+vendordebitAccount+"     vendorcreditAccount: "+vendorcreditAccount);
//        String auisitionDRAcctNo = vendordebitAccount.substring(8,16);
//        System.out.println("====vendordebitAccount.substring(0,3): "+vendordebitAccount.substring(0,3)+"   vendordebitAccount.substring(8,16);: "+vendordebitAccount.substring(8,16));
        if(vendordebitAccount.substring(0,3).equals("NGN")){vendordebitAccount = vendordebitAccount.substring(8,16);}
        if(vendordebitAccount.substring(2,5).equals("NGN")){vendordebitAccount = vendordebitAccount.substring(6,14);}
//        System.out.println("====vendordebitAccount: "+vendordebitAccount);
        String vendordebitAccType = request.getParameter("vendor_dr_accttype");
        String vendordebitTranCode = request.getParameter("vendor_dr_trancode");
        String vendordebitNarration = request.getParameter("vendor_dr_narration");
        String vendorcreditAcct = request.getParameter("vendor_cr_account");
        if(vendorcreditAccount.substring(0,3).equals("NGN")){vendorcreditAccount = vendorcreditAccount.substring(8,16);}
        if(vendorcreditAccount.substring(2,5).equals("NGN")){vendorcreditAccount = vendorcreditAccount.substring(6,14);}
        String vendorcreditAccType = request.getParameter("vendor_cr_accttype");
        String vendorcreditTranCode = request.getParameter("vendor_cr_trancode");
        String vendorcreditNarration = request.getParameter("vendor_cr_narration");
        String vendoramount = request.getParameter("vendor_amount");
        String supervisor = request.getParameter("supervisor");
        String vendorname = request.getParameter("vendorname");
        String vendorId = request.getParameter("vendorId");
        String location = request.getParameter("location");
        String projectCode = request.getParameter("projectCode");	
        String invNo = request.getParameter("invNo");
        String SbuCode = request.getParameter("SbuCode");
        String recType = request.getParameter("recType");
        String transactionNarration = request.getParameter("vendor_cr_narration");
        String type="Asset Creation";
        String transactionId = request.getParameter("transactionIdVendor");
        String tranId = request.getParameter("tranId");
        to = request.getParameter("toaddress");
        String systemIp =request.getRemoteAddr();
        String macAddress= "";
        //String testfield = "125";  
        String finacleTransId = records.getGeneratedTransId();
        String Subheading = "This is to kindly inform you of the payment that has been credited into your account for execution of the job commissioned by FCMB to your company.";
		String summary1 = "For clarification on the above transaction, kindly send an email to Adminpayment@firstcitygroup.com or call  07098000779.";
		String summary2 = "Thank you.";
		String summary3 = companyName; 
//		String summary3 = "For: First City Monument Bank Plc"; 
		String raiseEntryNarration = vendordebitNarration; 
		//String sbu_code = SbuCode+ "|" +SbuCode;
		String sbu_code = SbuCode+SbuCode;
		vendordebitNarration = SbuCode+"|"+SbuCode+"|"+vendordebitNarration;
//        String aquisitiondebitAcctName = records.getCodeName("select category_name from am_ad_category where Asset_Ledger = '"+auisitionDRAcctNo+"'");
        String [] idSplit = id.split("/");         
        String test1 = "select category_name from am_ad_category where Asset_Ledger = '"+vendordebitAccount+"' and category_acronym = '"+idSplit[2]+"'  and substring(category_acronym,1,1) not in ('F','U')";
        String test2 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+vendordebitAccount+"' and category_acronym = '"+idSplit[2]+"'  and substring(category_acronym,1,1) not in ('F','U')";
        String test3 = "select category_name from am_ad_category where DEP_LEDGER = '"+vendordebitAccount+"' and category_acronym = '"+idSplit[2]+"'  and substring(category_acronym,1,1) not in ('F','U')";
       String test4 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+vendordebitAccount+"' ";
//        aquisitiondebitAcctName = records.getCodeName(test1);
//        System.out.println("==========test1: "+test1+"  test2: "+test2+"   test3: "+test3);
        if(aquisitiondebitAcctName.equalsIgnoreCase("")){aquisitiondebitAcctName = records.getCodeName(test1);}
        if(aquisitiondebitAcctName.equalsIgnoreCase("")){aquisitiondebitAcctName = records.getCodeName(test2);}
        if(aquisitiondebitAcctName.equalsIgnoreCase("")){aquisitiondebitAcctName = records.getCodeName(test3);}  
        if(aquisitiondebitAcctName.equalsIgnoreCase("")){aquisitiondebitAcctName = records.getCodeName(test4);}   
        if(aquisitiondebitAcctName.equals("")){aquisitiondebitAcctName = "Asset Acqusition Suspense Account";}
//        System.out.println("<<<auisitionDRAcctNo >>>>"+auisitionDRAcctNo);
//        System.out.println("==========aquisitiondebitAcctName: "+aquisitiondebitAcctName);  
        String vendorAcctName = records.getCodeName("select vendor_name from am_ad_vendor where account_number = '"+vendorcreditAccount+"'");
        
        String test20 = "select category_name from am_ad_category where Asset_Ledger = '"+vendorcreditAccount+"' and category_acronym = '"+idSplit[2]+"'  and substring(category_acronym,1,1) not in ('F','U')";
        String test21 = "select category_name from am_ad_category where ACCUM_DEP_LEDGER = '"+vendorcreditAccount+"' and category_acronym = '"+idSplit[2]+"'  and substring(category_acronym,1,1) not in ('F','U')";
        String test22 = "select category_name from am_ad_category where DEP_LEDGER = '"+vendorcreditAccount+"' and category_acronym = '"+idSplit[2]+"'  and substring(category_acronym,1,1) not in ('F','U')";
        String test23 = "select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = '"+vendorcreditAccount+"' ";
        String test24 = "select asset_acq_ac from am_gb_company where asset_acq_ac = '"+vendorcreditAccount+"' ";
        vendorAcctName = records.getCodeName(test23);
//        System.out.println("==========test23: "+test23);
//        System.out.println("==========test24: "+test24);
//        System.out.println("==========test20: "+test20+"  test21: "+test21+"   test22: "+test22+"    test23: "+test23);
        if(vendorAcctName.equalsIgnoreCase("")){vendorAcctName = records.getCodeName(test20);}
        if(vendorAcctName.equalsIgnoreCase("")){vendorAcctName = records.getCodeName(test21);}
        if(vendorAcctName.equalsIgnoreCase("")){vendorAcctName = records.getCodeName(test22);}  
        if(vendorAcctName.equalsIgnoreCase("")){vendorAcctName = records.getCodeName(test23);}  
        if(records.getCodeName(test24)!="" && vendorAcctName.equalsIgnoreCase("")){vendorAcctName = "Asset Acquisition Suspense Account";}
//        System.out.println("==========vendorAcctName: "+vendorAcctName);
        try
        {        
        if (!userClass.equals("NULL") || userClass!=null){
        if(vendoramount!=null)
        {
        vendoramount = vendoramount.replaceAll(",","");
        if (supervisor == "" || supervisor == null || supervisor == "*" || supervisor.equals("*")) {
   		 supervisor = "";
        }
        String tranIdReverse = "R"+tranId;
      // if(ad.isoChecking(id, page1, transactionId))
            if(ad.isoChecking(id, page1, transactionId,tranId))  
       {
    	   //iso=bus.interaccounttransferFund(vendordebitNarration,vendorcreditAccount,vendordebitAcct,Double.parseDouble(vendoramount),finacleTransId,SbuCode,SbuCode);
    	   iso=bus.transferFund(vendordebitNarration,vendorcreditAcct,vendordebitAcct,Double.parseDouble(vendoramount),finacleTransId,SbuCode);
    	   ad.updateRaiseEntryTransaction(id, page1, transactionId, iso,systemIp,tranId,finacleTransId,"","");
//			System.out.println("The value of iso 000 issssss?????????????? "+iso);
//			System.out.println("Vendor Credit Narration issssss?????????????? "+vendorcreditNarration);
//			System.out.println("Vendor Debit Narration issssss?????????????? "+vendordebitNarration);
          //  ad.updateRaiseEntryTransaction(id, page1, transactionId, iso);
       }
       else  
       {      
        raiseMan.raiseEntry(vendordebitAcct,vendorcreditAcct,vendordebitAccType,vendorcreditAccType,vendordebitTranCode
        		,vendorcreditTranCode,vendordebitNarration,
        		vendorcreditNarration,Double.parseDouble(vendoramount), userId,batchId,cdate,supervisor,id,type);
        		  
         //iso=bus.interaccounttransferFund(vendordebitNarration,vendorcreditAcct,vendordebitAcct,Double.parseDouble(vendoramount),finacleTransId,SbuCode,SbuCode);
         iso=bus.transferFund(vendordebitNarration,vendorcreditAcct,vendordebitAcct,Double.parseDouble(vendoramount),finacleTransId,SbuCode);
//        System.out.println("The value of iso 111 issssss?????????????? "+iso);
         // ad.insertRaiseEntryTransaction(userId,vendordebitNarration,vendorcreditAcct,vendordebitAcct,Double.parseDouble(vendoramount),iso,id,page1,transactionId);
          ad.insertRaiseEntryTransaction(userId,raiseEntryNarration,vendordebitAcct,vendorcreditAcct,Double.parseDouble(vendoramount),iso,id,page1,transactionId,systemIp,tranId,assetCode,finacleTransId,aquisitiondebitAcctName,vendorAcctName,userId,recType);
           ad.insertRaiseEntryTransactionArchive(userId,raiseEntryNarration,vendorcreditAcct,vendordebitAcct,Double.parseDouble(vendoramount),iso,id,page1,transactionId,systemIp,macAddress,tranId,assetCode);
       }
//            System.out.println("The value of iso issssss?????????????? "+iso);
//            System.out.println("The value of mail_code ?????????????? "+mail_code+"  ??????? transaction_type ????  "+transaction_type);
         //   iso = "000";
            Date newdate = new java.util.Date();
            
            df = new SimpleDateFormat("dd-MM-yyyy  HH:mm:ss");
            String sdt = df.format(newdate);
            System.out.println(sdt);

            
            String date = substring(newdate,1,10);
            String yy = substring(newdate,24,4);
            String hrmmss = substring(newdate,12,8);
//            System.out.println("The value of Date?????????????? "+newdate);
           // Timestamp transdate = datetime.getSystemDate(newdate);
          //  System.out.println("The value of transdate?????????????? "+date+"  yy: "+yy+"  hrmmss: "+hrmmss);
         if(iso.equalsIgnoreCase("000"))
             {  
    	   	 String reversalId = tranId.substring(0, 1);
    	     if(reversalId.equals("R")){tranId = tranId.substring(1, tranId.length());}
       	     if(!reversalId.equals("R")){tranId = "R"+tranId;}
                String q = "update am_Raisentry_Transaction set iso = '-001' where asset_id='"+id+"' and Trans_id = '"+tranId+"' and transactionId = "+transactionId+"";
                ad.updateAssetStatusChange(q);	           	 
 //       	 ad.updateAssetStatusChange("update am_Raisentry_Transaction set iso = '' where trans_id='"+tranIdReverse+"' and transactionId = "+transactionId+"");
        	 ad.insertVendorTransaction(userId,transactionNarration,vendorcreditAcct,vendorcreditAccount,Double.parseDouble(vendoramount),location,transactionId,page1,projectCode,vendorId);
 //                   System.out.println("#####################################################################  2");                   
                    msgText1 = "CREDIT ADVICE \n";
                    msgText1 += ""+Subheading+"\n";
         			msgText1 += message.MailMessage(mail_code, transaction_type)+" \n";        			
        			msgText1 += "VENDOR NAME "+vendorname+"\n";
        			msgText1 += "ACCOUNT NUMBER  "+vendorcreditAcct+"\n";
        			msgText1 += "INVOICE NO  "+invNo+"\n";
        			msgText1 += "AMOUNT  "+formata.formatAmount(vendoramount)+"\n";
                    msgText1 += "NARRATION  "+vendorcreditNarration +"\n";
                    msgText1 += "PAYMENT ID  "+id+"\n";        			        		        			
        			//msgText1 += "TRANSACTION DATE  "+newdate+"\n";
        			msgText1 += ""+summary1+"\n";
        			msgText1 += ""+summary2+"\n";
        			msgText1 += ""+summary3+"\n"; 
        			
        			//msgText1 += "Login here http://172.27.13.113:8080/legend2.net" ;
          			//ret = bulkMail.sendMail(signinname,transaction_type,msgText1,directory,to);
          			//System.out.println("Output of the mail to >>>>"+to);
        			mail.sendMail(to,subject,msgText1);
        			
            }	
// 	    to display transaction status
		    String status =  records.getCodeName("select description from am_error_description where error_code='"+iso+"' ");
		    out.print("<script>alert('"+status+"');</script>");
           out.print("<script>window.close('raiseAssetDetailAdvancePayment.jsp');</script>");
         //  session.setAttribute("assetid", id);
    	 //  session.setAttribute("page1", page1);
    	 //  out.print("<script>window.open('raiseEntryList.jsp','raiseasset4','width=850,height=350,scrollbars=Yes,resizable=Yes,align=center');</script>");

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
    private String substring(Date newdate, int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getServletInfo()
    {
        return "Process Action Servlet";
    }
}