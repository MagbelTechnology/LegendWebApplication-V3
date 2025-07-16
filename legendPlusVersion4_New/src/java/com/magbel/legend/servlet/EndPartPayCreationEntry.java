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

import com.magbel.legend.mail.EmailSmsServiceBus;

import magma.util.Codes;

import java.util.*;

import  com.magbel.util.CurrencyNumberformat ;

import java.text.SimpleDateFormat;

public class EndPartPayCreationEntry extends HttpServlet
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
    public EndPartPayCreationEntry()
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

        String page1 = "ASSET PART PAYMENT ENTRY";

        /**
        Post Cost Transaction
        */
        String destination = request.getParameter("destination");

        String isoValue = "";
        String transactionIdCost = ad.isoCheck(id, page1, "8");
        String transactionIdVendor = ad.isoCheck(id, page1, "9");
        String transactionIdWitholding = ad.isoCheck(id, page1, "10");
        String branchCode=records.getCodeName("select BRANCH_CODE from am_asset where asset_id='"+id+"'");
        String assetNarration = records.getCodeName("select Description from am_asset where asset_id='"+id+"'");
        System.out.println("<<<<<<<Id: "+id+"     transactionIdCost: "+transactionIdCost+"  transactionIdVendor: "+transactionIdVendor+"   transactionIdWitholding: "+transactionIdWitholding);
         if((transactionIdCost.equals("000"))&&(transactionIdVendor.equals("000"))&&(transactionIdWitholding.equals("000"))){
        	 isoValue = "0";
         }        
        try
        {
        	 if (!userClass.equals("NULL") || userClass!=null){
            if(isoValue.equalsIgnoreCase("0")){
             	System.out.println("isoValue ================== "+isoValue+" Asset Id: "+id+"    destination: "+destination );
            	ad.updateAssetStatusChange("update am_raisentry_post set entrypostflag='Y', GroupIdStatus = 'Y' where id='"+id+"' and page='ASSET PART PAYMENT ENTRY' "); 
            	ad.updateAssetStatusChange("update am_asset set asset_status='ACTIVE' where asset_id='"+id+"'"); 
            	
            	String mailaddress=records.getCodeName("select email from am_ad_branch where BRANCH_CODE='"+branchCode+"'");
            	String to = mailaddress;
            	String subject = "New Asset Part Payment";
            	String msgText1 = "Your Branch has been debited for the Asset "+assetNarration+" with asset Id "+id+"";
            	mail.sendMail(to,subject,msgText1);	
            	
             }
            System.out.println("destination ================== "+destination+" iso Value: "+isoValue );
            
//            out.print("<script>alert('About To Close the Page ')</script>");
            out.print("<script>window.location='"+destination+"'</script>");
        }else {
    		out.print("<script>alert('You have No Right')</script>");
    		 out.print("<script>window.location='"+destination+"'</script>");
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