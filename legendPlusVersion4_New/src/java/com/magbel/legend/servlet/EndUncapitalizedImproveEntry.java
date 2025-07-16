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

import magma.asset.manager.UncapitaliseManager;;
public class EndUncapitalizedImproveEntry extends HttpServlet
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
	private UncapitaliseManager assetMan;
    public EndUncapitalizedImproveEntry()
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
        assetMan = new UncapitaliseManager();
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
        if (!userClass.equals("NULL") || userClass!=null){
//        System.out.println("destination ================== "+destination);
        if(id!=null){
        String tranId = request.getParameter("tranId");
        String strCalcCost = request.getParameter("CalcCost");
 //       System.out.println("strCalcCost"+strCalcCost+"  id: "+id+"   tranId: "+tranId);
        double CalcCost = Double.parseDouble(strCalcCost);
        String strCalcNBV = request.getParameter("CalcNBV");
        double CalcNBV = Double.parseDouble(strCalcNBV);
        String strCalcvatable = request.getParameter("Calcvatable"); 
        double Calcvatable = Double.parseDouble(strCalcvatable);
        String strCalcVatAmt = request.getParameter("CalcVatAmt");
        double CalcVatAmt = Double.parseDouble(strCalcVatAmt);
        String strCalcWhtAmt = request.getParameter("CalcWhtAmt");
        double CalcWhtAmt = Double.parseDouble(strCalcWhtAmt);
        String strusefullife = request.getParameter("usefullife");
        int usefullife = Integer.parseInt(strusefullife);
        String strcost = request.getParameter("cost");
        strcost = strcost.toString().replaceAll(",", "");
        double cost = Double.parseDouble(strcost);
        String strnewCost = request.getParameter("newCost");
        strnewCost = strnewCost.toString().replaceAll(",", "");
        double newCost = Double.parseDouble(strnewCost);
        String strnewVatableCost = request.getParameter("newVatable_Cost");
        strnewVatableCost = strnewVatableCost.toString().replaceAll(",", "");
        double newVatableCost = Double.parseDouble(strnewVatableCost);
       	String strvatableCost = request.getParameter("vatableCost");
       	strvatableCost = strvatableCost.toString().replaceAll(",", "");
       	double vatableCost = Double.parseDouble(strvatableCost);
       	String nbvresidual = request.getParameter("nbvresidual");
       	String strtranIdInt = request.getParameter("tranIdInt");
       	int tranIdInt = Integer.parseInt(strtranIdInt);
       	String strvatAmt = request.getParameter("vatAmt");
       	strvatAmt = strvatAmt.toString().replaceAll(",", "");
       	double vatAmt = Double.parseDouble(strvatAmt);
       	String strwhtAmt = request.getParameter("whtAmt");
       	strwhtAmt = strwhtAmt.toString().replaceAll(",", "");
       	double whtAmt = Double.parseDouble(strwhtAmt);
        String stroldCost = request.getParameter("oldCost");
        stroldCost = stroldCost.toString().replaceAll(",", "");
        double oldCost = Double.parseDouble(stroldCost);
        String stroldVatAmt = request.getParameter("oldVatAmt");
        stroldVatAmt = stroldVatAmt.toString().replaceAll(",", "");
        double oldVatAmt = Double.parseDouble(stroldVatAmt);
        String stroldWhtAmt = request.getParameter("oldWhtAmt");
        stroldWhtAmt = stroldWhtAmt.toString().replaceAll(",", "");
        double oldWhtAmt = Double.parseDouble(stroldWhtAmt);
        String stroldVatableCost = request.getParameter("oldVatableCost");
        stroldVatableCost = stroldVatableCost.toString().replaceAll(",", "");
        double oldVatableCost = Double.parseDouble(stroldVatableCost);
        String stroldNbv = request.getParameter("oldNbv");
        stroldNbv = stroldNbv.toString().replaceAll(",", "");
        double oldNbv = Double.parseDouble(stroldNbv);
        String stroldAccumDep = request.getParameter("oldAccumDep");
        stroldAccumDep = stroldAccumDep.toString().replaceAll(",", "");
        double oldAccumDep = Double.parseDouble(stroldAccumDep);
                
        String stroldimprovCost = request.getParameter("oldimprovCost");
        stroldimprovCost = stroldimprovCost.toString().replaceAll(",", "");
        double oldimprovCost = Double.parseDouble(stroldimprovCost);
        String stroldimprovvatableCost = request.getParameter("oldimprovvatableCost");
        stroldimprovvatableCost = stroldimprovvatableCost.toString().replaceAll(",", "");
        double oldimprovvatableCost = Double.parseDouble(stroldimprovvatableCost);
        String stroldimprovNBV = request.getParameter("oldimprovNBV");
        stroldimprovNBV = stroldimprovNBV.toString().replaceAll(",", "");
        double oldimprovNBV = Double.parseDouble(stroldimprovNBV);
        String stroldimprovaccum = request.getParameter("oldimprovaccum");
        stroldimprovaccum = stroldimprovaccum.toString().replaceAll(",", "");
        double oldimprovaccum = Double.parseDouble(stroldimprovaccum);
        int assetCode = request.getParameter("assetCode") == null?0:Integer.parseInt(request.getParameter("assetCode"));

        String page1 = "UNCAPITALIZED IMPROVEMENT RAISE ENTRY";

        /**
        Post Cost Transaction
        */
        String transIdCost = "30";
        String transIdVendor = "31";
        String transIdWitholding = "32";
        String transactionIdCost = ad.isoCheck(id, page1,transIdCost,tranId);
        String transactionIdVendor = ad.isoCheck(id, page1,transIdWitholding,tranId);
        String transactionIdWitholding = ad.isoCheck(id, page1,transIdWitholding,tranId);
        String isoValue = "";
        String isoValueReverse = "";
   //     String isoValue=records.getCodeName("select count(*) from am_Raisentry_Transaction where Trans_id='"+tranId+"' and page1 = 'ASSET IMPROVEMENT RAISE ENTRY' and iso != '000' ");
        String tranIdReverse = "R"+tranId;
        String prcessCost = ad.isoCheck(  id, page1,transIdCost,tranIdReverse);
        String prcessCost2 = ad.isoCheck( id, page1,transIdVendor,tranIdReverse);
        String prcessWithTax = ad.isoCheck(  id, page1,transIdWitholding,tranIdReverse);
  //      String isoValueReverse=records.getCodeName("select count(*) from am_Raisentry_Transaction where Trans_id='"+tranIdReverse+"' and page1 = 'ASSET IMPROVEMENT RAISE ENTRY' and iso != '000' ");         
        String  improveStatus = records.getCodeName("SELECT IMPROVED FROM am_Uncapitalized_improvement WHERE revalue_Id = '"+tranId+"'"); 
        String  reversalStatus = records.getCodeName("SELECT REVERSED FROM am_Uncapitalized_improvement WHERE revalue_Id = '"+tranId+"'"); 
        String branchCode=records.getCodeName("select BRANCH_CODE from AM_ASSET_UNCAPITALIZED where asset_id='"+id+"'");
        String assetNarration = records.getCodeName("select Description from AM_ASSET_UNCAPITALIZED where asset_id='"+id+"'");
        System.out.println("transactionIdCost: "+transactionIdCost+"  transactionIdVendor: "+transactionIdVendor+"   transactionIdWitholding: "+transactionIdWitholding);
         if((transactionIdCost.equals("000"))&&(transactionIdVendor.equals("000"))&&(transactionIdWitholding.equals("000"))){
        	 isoValue = "0";
         } 
         System.out.println("prcessCost: "+prcessCost+"  prcessCost2: "+prcessCost2+"   prcessWithTax: "+prcessWithTax);
         if((prcessCost.equals("000"))&&(prcessCost2.equals("000"))&&(prcessWithTax.equals("000"))){
        	 isoValueReverse = "0";
         }          
        try
        {

        	System.out.println("isoValue"+isoValue+"   isoValueReverse: "+isoValueReverse+"  improveStatus: "+improveStatus+"   reversalStatus: "+reversalStatus);
            if(isoValue.equalsIgnoreCase("0") && improveStatus.equalsIgnoreCase("Y") && !reversalStatus.equalsIgnoreCase("Y"))
            {
           	assetMan.processUncapImprovement(id,CalcCost,CalcNBV,Calcvatable,CalcVatAmt,CalcWhtAmt,usefullife,newCost,newVatableCost,nbvresidual,tranIdInt);
           	String mailaddress=records.getCodeName("select email from am_ad_branch where BRANCH_CODE='"+branchCode+"'");
           	String to = mailaddress;
           	String subject = "Uncapitalized Improvement";
           	String msgText1 = "Your Branch has been debited for the Improvement of the Asset "+assetNarration+" with asset Id "+id+"";
           	mail.sendMail(to,subject,msgText1);		
            }
            if(isoValueReverse.equalsIgnoreCase("0") && reversalStatus.equalsIgnoreCase("Y") && !improveStatus.equalsIgnoreCase("Y"))
            {
//            	System.out.println("<<<<<<<<<<<<<<<<Reversal isoValueReverse ================== "+isoValueReverse);
            	        
           	assetMan.processUncapImprovementReversal(id,oldCost,oldNbv,oldVatableCost,oldVatAmt,oldWhtAmt,usefullife,oldimprovCost,oldimprovvatableCost,nbvresidual,tranIdInt,oldimprovNBV,oldimprovaccum);
           	String mailaddress=records.getCodeName("select email from am_ad_branch where BRANCH_CODE='"+branchCode+"'");
           	String to = mailaddress;
           	String subject = "Uncapitalized Improvement";
           	String msgText1 = "Your Branch has been credited for the previous debit for Improvement of the Asset "+assetNarration+" with asset Id "+id+"";
           	mail.sendMail(to,subject,msgText1);		
            	
            } 
//            System.out.println("destination ================== "+destination+" iso Value: "+isoValue );
            
//            out.print("<script>alert('About To Close the Page ')</script>");
            out.print("<script>window.location='"+destination+"'</script>");
	  }

        catch(Exception e)
        {
        	e.printStackTrace();
        }
    }else{out.print("<script>window.location='"+destination+"'</script>");}
    }else {
		out.print("<script>alert('You have No Right')</script>");
	}
}
    public String getServletInfo()
    {
        return "End Asset Creation Entry Servlet";
    }
}