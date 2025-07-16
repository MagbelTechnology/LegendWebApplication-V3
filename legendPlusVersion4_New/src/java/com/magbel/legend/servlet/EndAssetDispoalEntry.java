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

import magma.asset.manager.AssetManager;
public class EndAssetDispoalEntry extends HttpServlet
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
	private AssetManager assetMan;
    public EndAssetDispoalEntry()
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
        assetMan = new AssetManager();
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
        String destination = request.getParameter("destination");
//        System.out.println("destination ================== "+destination);
        if(id!=null){
        String tranId = request.getParameter("tranId");
        String cdate = request.getParameter("CRDATE");
        String strrate = request.getParameter("rate");
        //int rate = Integer.parseInt(strrate);
        System.out.println("strrate ================== "+strrate+"    tranId: "+tranId+"  cdate:"+cdate);
        String page1 = "ASSET DISPOSAL RAISE ENTRY";

        /**
        Post Cost Transaction
        */
        String prcessCost = ad.isoCheck(id, page1, "4",tranId);
        String prcessAccum = ad.isoCheck(id, page1, "5",tranId);
        String prcessDisposal = ad.isoCheck(id, page1, "6",tranId);
        String prcessProfit = ad.isoCheck(id, page1, "7",tranId);
        String isoValue = "";
        String isoValueReverse = "";
   //     String isoValue=records.getCodeName("select count(*) from am_Raisentry_Transaction where Trans_id='"+tranId+"' and page1 = 'ASSET IMPROVEMENT RAISE ENTRY' and iso != '000' ");
        String tranIdReverse = "R"+tranId;
        String prcessCostRev = ad.isoCheck(id, page1, "4",tranIdReverse);
        String prcessAccumRev = ad.isoCheck(id, page1, "5",tranIdReverse);
        String prcessDisposalRev = ad.isoCheck(id, page1, "6",tranIdReverse);
        String prcessProfitRev = ad.isoCheck(id, page1, "7",tranIdReverse);
  //      String isoValueReverse=records.getCodeName("select count(*) from am_Raisentry_Transaction where Trans_id='"+tranIdReverse+"' and page1 = 'ASSET IMPROVEMENT RAISE ENTRY' and iso != '000' ");         
 //       String isoValueReverse=records.getCodeName("select count(*) from am_Raisentry_Transaction where Trans_id='"+tranIdReverse+"' and page1 = 'ASSET DISPOSAL RAISE ENTRY' and iso != '000' "); 
        String  reversalStatus = records.getCodeName("SELECT REVERSED FROM AM_ASSETDISPOSAL WHERE Disposal_ID = '"+tranId+"'"); 
        String branchCode=records.getCodeName("select BRANCH_CODE from am_asset where asset_id='"+id+"'");
        String assetNarration = records.getCodeName("select Description from am_asset where asset_id='"+id+"'");
        System.out.println("prcessCost: "+prcessCost+"  prcessAccum: "+prcessAccum+"   prcessDisposal: "+prcessDisposal+"    prcessProfit: "+prcessProfit);
         if((prcessCost.equals("000"))&&(prcessAccum.equals("000"))&&(prcessDisposal.equals("000"))&&(prcessProfit.equals("000"))){
        	 isoValue = "0";
         } 
         System.out.println("prcessCostRev: "+prcessCostRev+"  prcessAccumRev: "+prcessAccumRev+"   prcessDisposalRev: "+prcessDisposalRev+"     prcessProfitRev: "+prcessProfitRev);
         if((prcessCostRev.equals("000"))&&(prcessAccumRev.equals("000"))&&(prcessDisposalRev.equals("000"))&&(prcessProfitRev.equals("000"))){
        	 isoValueReverse = "0";
         }     
        try
        {
        	 if (!userClass.equals("NULL") || userClass!=null){
        	System.out.println("isoValue"+isoValue+"   isoValueReverse: "+isoValueReverse+"   reversalStatus: "+reversalStatus);
            if(isoValue.equalsIgnoreCase("0") && strrate.equalsIgnoreCase("100.0") && !reversalStatus.equalsIgnoreCase("Y")){
        		System.out.println("rate ================== "+strrate+"  isoValue: "+isoValue+"   cdate: "+cdate+" Asset Id: "+id );
        	ad.updateAssetStatusChange("update am_assetDisposal set email_sent ='Y',disposal_status='P',REVERSED = 'Y' where asset_id='"+id+"'"); 
        	ad.updateAssetStatusChange("update am_asset set asset_status='Disposed',date_disposed='"+cdate+"' where asset_id='"+id+"'"); 
        	ad.updateAssetStatusChange("update am_raisentry_post set GroupIdStatus = 'Y',entryPostFlag = 'Y' where id='"+id+"' AND page = 'ASSET DISPOSAL RAISE ENTRY'");       	
        	String mailaddress=records.getCodeName("select email from am_ad_branch where BRANCH_CODE='"+branchCode+"'");
        	
           	String to = mailaddress;
           	String subject = "Asset Disposal";
           	String msgText1 = "Your Asset "+assetNarration+" with asset Id "+id+" has been disposed";
           	mail.sendMail(to,subject,msgText1);		
            }
            if(isoValueReverse.equalsIgnoreCase("0") && reversalStatus.equalsIgnoreCase("Y"))
            {
            ad.updateAssetStatusChange("update am_assetDisposal set email_sent ='Y',disposal_status='R',REVERSED = 'R' where asset_id='"+id+"'"); 
           	ad.updateAssetStatusChange("update am_asset set asset_status='ACTIVE',date_disposed=NULL where asset_id='"+id+"'"); 
           	String mailaddress=records.getCodeName("select email from am_ad_branch where BRANCH_CODE='"+branchCode+"'");
           	String to = mailaddress;
           	String subject = "Asset Dissposal Reversal";
           	String msgText1 = "Your Asset "+assetNarration+" with asset Id "+id+" which has been previously disposed has been reversed";
           	mail.sendMail(to,subject,msgText1);		
            } 

//            out.print("<script>alert('About To Close the Page ')</script>");
            out.print("<script>window.location='"+destination+"'</script>");
        }else {
    		out.print("<script>alert('You have No Right')</script>");
    	}
	  }

        catch(Exception e)
        {
        	e.printStackTrace();
        }
    }else{out.print("<script>window.location='"+destination+"'</script>");}
    }
    public String getServletInfo()
    {
        return "End Asset Creation Entry Servlet";
    }
}