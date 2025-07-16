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
import magma.net.vao.FleetManatainanceRecord;
import magma.net.vao.Transaction;

import com.magbel.legend.mail.EmailSmsServiceBus;

import magma.util.Codes;

import java.util.*;

import  com.magbel.util.CurrencyNumberformat ;

import java.text.SimpleDateFormat;

public class EndAssetCreationEntry extends HttpServlet
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
	private  AssetRecordsBean transRec;
    public EndAssetCreationEntry()
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
        transRec = new AssetRecordsBean();
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
        String tranId = request.getParameter("tranId");
        int assetCode = request.getParameter("assetCode") == null?0:Integer.parseInt(request.getParameter("assetCode"));

        String page1 = "ASSET CREATION RAISE ENTRY";

        /**
        Post Cost Transaction
        */
        String destination = request.getParameter("destination");
        String transIdCost = "1";
        String transIdVendor = "2";
        String transIdWitholding = "3";
        String isoValue = "";
//        String transactionIdCost = ad.isoCheck(id, page1, "1");
//        String transactionIdVendor = ad.isoCheck(id, page1, "2");
//        String transactionIdWitholding = ad.isoCheck(id, page1, "3");
        String transactionIdCost = ad.isoCheck(id, page1,transIdCost,tranId);
        String transactionIdVendor = ad.isoCheck(id, page1,transIdVendor,tranId);
        String transactionIdWitholding = ad.isoCheck(id, page1,transIdWitholding,tranId);


        String isoValueReverse = "";
   //     String isoValue=records.getCodeName("select count(*) from am_Raisentry_Transaction where Trans_id='"+tranId+"' and page1 = 'ASSET IMPROVEMENT RAISE ENTRY' and iso != '000' ");
        String tranIdReverse = "R"+tranId;
        String prcessCost = ad.isoCheck(  id, page1,transIdCost,tranIdReverse);
        String prcessCost2 = ad.isoCheck( id, page1,transIdVendor,tranIdReverse);
        String prcessWithTax = ad.isoCheck(  id, page1,transIdWitholding,tranIdReverse);
  
        String branchCode=records.getCodeName("select BRANCH_CODE from am_asset where asset_id='"+id+"'");
        String assetNarration = records.getCodeName("select Description from am_asset where asset_id='"+id+"'");
        System.out.println("transactionIdCost: "+transactionIdCost+"  transactionIdVendor: "+transactionIdVendor+"   transactionIdWitholding: "+transactionIdWitholding);
         if((transactionIdCost.equals("000"))&&(transactionIdVendor.equals("000"))&&(transactionIdWitholding.equals("000"))){
        	 isoValue = "0";
         }    
         if((prcessCost.equals("000"))&&(prcessCost2.equals("000"))&&(prcessWithTax.equals("000"))){
        	 isoValueReverse = "0";
         }  
     	
     	java.util.ArrayList list =transRec.gettransactionRecords(tranId);
        try 
        {
        	System.out.println("userClass ================== "+userClass);
        	if(userClass==null){userClass = "";}
        	 if (!userClass.equals("")){
  //      		 if (!userClass.equals(null) || userClass!=null){
//        	AssetRecordsBean transRec = new AssetRecordsBean();
//        	java.util.ArrayList list =transRec.gettransactionRecords(tranId);
        	System.out.println("list.size() ================== "+list.size());
            if(isoValue.equalsIgnoreCase("0")){
             	System.out.println("isoValue ================== "+isoValue+" Asset Id: "+id+"    destination: "+destination );
            	ad.updateAssetStatusChange("update am_raisentry_post set entrypostflag='Y', GroupIdStatus = 'Y' where id='"+id+"' and page='ASSET CREATION RAISE ENTRY' "); 
            	ad.updateAssetStatusChange("update am_asset set asset_status='ACTIVE' where asset_id='"+id+"'"); 
/*       	     for(int i=0;i<list.size();i++)
    	     { 
       	    	magma.net.vao.Transaction  trans = (magma.net.vao.Transaction)list.get(i);  
//      	    	Transaction trans = (Transaction)list.get(i);
				String transaction_Id = trans.getTransId();
				String iso = trans.getCode();
				String assetId = trans.getAssetId();
				System.out.println("=======assetId==: "+assetId+"  transaction_Id: "+transaction_Id+"   iso: "+iso);
//				if(transIdCost == transaction_Id && iso == "000"){
//            	ad.updateAssetStatusChange("update am_Raisentry_Transaction set iso = '' where trans_id='"+tranIdReverse+"' and transactionId = '"+transIdCost+"'");
//    	        }
//				if(transIdVendor == transaction_Id && iso == "000"){
//	            	ad.updateAssetStatusChange("update am_Raisentry_Transaction set iso = '' where trans_id='"+tranIdReverse+"' and transactionId = '"+transIdVendor+"'");
//	    	        }
//				if(transIdWitholding == transaction_Id && iso == "000"){
//	            	ad.updateAssetStatusChange("update am_Raisentry_Transaction set iso = '' where trans_id='"+tranIdReverse+"' and transactionId = '"+transIdWitholding+"'");
//	    	        }
    	     }*/
            	String mailaddress=records.getCodeName("select email from am_ad_branch where BRANCH_CODE='"+branchCode+"'");
            	String to = mailaddress;
            	String subject = "New Asset Creation";
            	String msgText1 = "Your Branch has been debited for the Asset "+assetNarration+" with asset Id "+id+"";
            	mail.sendMail(to,subject,msgText1);	
            	
             }
            java.util.ArrayList list1 =transRec.gettransactionRecords(tranIdReverse);
            if(isoValueReverse.equalsIgnoreCase("0") && !isoValue.equalsIgnoreCase("0"))
            {
            	System.out.println("<<<<<<<<<<<<<<<<Reversal isoValueReverse ================== "+isoValueReverse);
            	        
        	ad.updateAssetStatusChange("update am_raisentry_post set entrypostflag='N', GroupIdStatus = 'N' where id='"+id+"' and page='ASSET CREATION RAISE ENTRY' "); 
        	ad.updateAssetStatusChange("update am_asset set asset_status='APPROVED' where asset_id='"+id+"'"); 
        	ad.updateAssetStatusChange("update am_asset_approval set Process_Status = 'P' where asset_id='"+id+"'");
        	
      	     for(int i=0;i<list1.size();i++)
   	     {
   	    	magma.net.vao.Transaction  trans = (magma.net.vao.Transaction)list1.get(i);    	 
				String transaction_Id = trans.getTransId();
				String iso = trans.getCode();
				String assetId = trans.getAssetId();
				if(transIdCost == transaction_Id && iso == "000"){
	           	ad.updateAssetStatusChange("update am_Raisentry_Transaction set iso = '' where trans_id='"+tranId+"' and transactionId = '"+transIdCost+"'");
	   	        }
				if(transIdVendor == transaction_Id && iso == "000"){
            	ad.updateAssetStatusChange("update am_Raisentry_Transaction set iso = '' where trans_id='"+tranId+"' and transactionId = '"+transIdVendor+"'");
    	        }
				if(transIdWitholding == transaction_Id && iso == "000"){
            	ad.updateAssetStatusChange("update am_Raisentry_Transaction set iso = '' where trans_id='"+tranId+"' and transactionId = '"+transIdWitholding+"'");
    	        }
   	     }
  //      	ad.updateAssetStatusChange("update am_Raisentry_Transaction set iso = '' where trans_id='"+tranId+"'");         	
           	String mailaddress=records.getCodeName("select email from am_ad_branch where BRANCH_CODE='"+branchCode+"'");
           	String to = mailaddress;
        	String subject = "New Asset Creation Reversal";
        	String msgText1 = "Your Branch has been Credited for the Asset "+assetNarration+" with asset Id "+id+"";
           	mail.sendMail(to,subject,msgText1);		
            	
            } 
            System.out.println("destination ================== "+destination+" iso Value: "+isoValue );
            
//            out.print("<script>alert('About To Close the Page ')</script>");
            out.print("<script>window.location='"+destination+"'</script>");
        }else {
        	out.print("<script>alert('About To Close the Page ')</script>");
        	out.print("<script>window.location='"+destination+"'</script>");
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
        return "End Asset Creation Entry Servlet";
    }
}