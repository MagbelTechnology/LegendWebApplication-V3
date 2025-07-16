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
public class EndAssetTransferEntry extends HttpServlet
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
    public EndAssetTransferEntry()
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
        String newAssetId = request.getParameter("asset_id");
        String destination = request.getParameter("destination");
//        System.out.println("destination ================== "+destination);
        String tranId = request.getParameter("tranId");
        String old_asset_id = request.getParameter("old_asset_id");
        String newDeptID = request.getParameter("newDeptID");
        String newBranchID = request.getParameter("newBranchID"); 
        String newSectionID = request.getParameter("newSectionID");
        String newAssetUser = request.getParameter("newAssetUser");
        String newSbuCode = request.getParameter("newSbuCode");
        String newbranchcode = request.getParameter("newbranchcode");
        String newdeptcode = request.getParameter("newdeptcode");
        String newscetioncode = request.getParameter("newscetioncode");
        String page1 = request.getParameter("page1"); 
        
        if (!userClass.equals("NULL") || userClass!=null){
        	
        int assetCode = request.getParameter("assetCode") == null?0:Integer.parseInt(request.getParameter("assetCode"));

//        String page1 = "ASSET TRANSFER RAISE ENTRY";
        if (page1.equalsIgnoreCase("ASSET TRANSFER RAISE ENTRY"))
        {
        /**
        Post Cost Transaction
        */
        String transIdCost = "20";
        String transIdAccum = "21";
        String transactionIdCost = ad.isoCheck(newAssetId, page1,transIdCost,tranId);
        String transactionIdAccum = ad.isoCheck(newAssetId, page1,transIdAccum,tranId);
        String isoValue = "";
        String isoValueReverse = "";
        String tranIdReverse = "R"+tranId;
        String prcessCost = ad.isoCheck(  newAssetId, page1,transIdCost,tranIdReverse);
        String prcessAccum = ad.isoCheck( newAssetId, page1,transIdAccum,tranIdReverse);         
 //       String  improveStatus = records.getCodeName("SELECT IMPROVED FROM am_asset_improvement WHERE revalue_Id = '"+tranId+"'"); 
        String  reversalStatus = records.getCodeName("SELECT REVERSED FROM am_assetTransfer WHERE Transfer_ID = '"+tranId+"'"); 
        String branchCode=records.getCodeName("select BRANCH_CODE from am_asset where asset_id='"+old_asset_id+"'");
        String assetNarration = records.getCodeName("select Description from am_asset where asset_id='"+old_asset_id+"'");
//        System.out.println("transactionIdCost: "+transactionIdCost+"  transactionIdAccum: "+transactionIdAccum);
         if((transactionIdCost.equals("000"))&&(transactionIdAccum.equals("000"))){
        	 isoValue = "0";
         } 
//         System.out.println("prcessCost: "+prcessCost+"  prcessAccum: "+prcessAccum);
         if((prcessCost.equals("000"))&&(prcessAccum.equals("000"))){
        	 isoValueReverse = "0";
         }          
        try
        {
//        	System.out.println("isoValue: "+isoValue+"   isoValueReverse: "+isoValueReverse+"   reversalStatus: "+reversalStatus);
        	if(isoValue.equalsIgnoreCase("0") && !reversalStatus.equalsIgnoreCase("Y"))
            {
            	ad.updateAssetStatusChange("update am_asset set old_asset_id ='" + old_asset_id + "', asset_id ='" + newAssetId + "', DEPT_ID = '"+newDeptID+"', BRANCH_ID = '"+newBranchID+"', section_id = '"+newSectionID+"', asset_user = '"+newAssetUser+"', SBU_CODE = '"+newSbuCode+"',BRANCH_CODE = '"+newbranchcode+"', DEPT_CODE = '"+newdeptcode+"', SECTION_CODE = '"+newscetioncode+"'    where asset_id ='" + old_asset_id + "'"); 
            	ad.updateAssetStatusChange("update am_raisentry_post set GroupIdStatus = 'Y',entryPostFlag = 'Y' where asset_code="+assetCode+" AND page = 'ASSET TRANSFER RAISE ENTRY'"); 
            	ad.updateAssetStatusChange("update am_assetTransfer set REVERSED = 'Y' where TRANSFER_ID = "+tranId+" ");  
           	String mailaddress=records.getCodeName("select email from am_ad_branch where BRANCH_CODE='"+branchCode+"'");
           	String to = mailaddress;
           	String subject = "Asset Transfer";
           	String msgText1 = "Your Branch has been debited for the Transfer of the Asset "+assetNarration+" with asset Id "+newAssetId+"";
           	mail.sendMail(to,subject,msgText1);		
            }
            if(isoValueReverse.equalsIgnoreCase("0") && reversalStatus.equalsIgnoreCase("Y"))
            {
//            	System.out.println("<<<<<<<<<Reversal isoValueReverse ======= "+isoValueReverse+"     reversalStatus: "+reversalStatus);
            	ad.updateAssetStatusChange("UPDATE AM_ASSET SET AM_ASSET.Asset_id = am_assetTransfer.asset_id,AM_ASSET.ASSET_USER = am_assetTransfer.OLD_Asset_user,"
            			+ "AM_ASSET.BRANCH_CODE = am_assetTransfer.OLD_BRANCH_CODE, AM_ASSET.BRANCH_ID = am_assetTransfer.OLD_branch_Id, "
            			+ "AM_ASSET.CATEGORY_CODE = am_assetTransfer.OLD_CATEGORY_CODE,AM_ASSET.DEPT_CODE = am_assetTransfer.OLD_DEPT_CODE,"
            			+ "AM_ASSET.DEPT_ID = am_assetTransfer.OLD_dept_ID,AM_ASSET.SBU_CODE = am_assetTransfer.OLD_SBU_CODE,"
            			+ "AM_ASSET.SECTION = am_assetTransfer.OLD_Section,AM_ASSET.SECTION_CODE = am_assetTransfer.OLD_SECTION_CODE "
            			+ "FROM am_assetTransfer, AM_ASSET WHERE AM_ASSET.ASSET_ID = am_assetTransfer.NEW_ASSET_ID "
            			+ "AND am_assetTransfer.TRANSFER_ID = "+tranId+""); 
            	ad.updateAssetStatusChange("update am_assetTransfer set REVERSED = 'R'where TRANSFER_ID = "+tranId+"  "); 
           	String mailaddress=records.getCodeName("select email from am_ad_branch where BRANCH_CODE='"+branchCode+"'");
           	String to = mailaddress;
           	String subject = "Asset Transfer";
           	String msgText1 = "Your Branch has been credited for the previous debit for Transfer of the Asset "+assetNarration+" with asset Id "+newAssetId+"";
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
        }
        

//      String page1 = "UNCAPITALISED ASSET TRANSFER RAISE ENTRY";
      if (page1.equalsIgnoreCase("UNCAPITALISED ASSET TRANSFER RAISE ENTRY"))
      {
      /**
      Post Cost Transaction
      */
      String transIdCost = "20";
      String transIdAccum = "23";
//      System.out.println("=======> page1: "+page1+"  tranId: "+tranId+"  transIdCost: "+transIdCost+"  newAssetId: "+newAssetId);
      String transactionIdCost = ad.isoCheck(newAssetId, page1,transIdCost,tranId);
      String transactionIdAccum = ad.isoCheck(newAssetId, page1,transIdAccum,tranId);
      String isoValue = "";
      String isoValueReverse = "";
      String tranIdReverse = "R"+tranId;
      String prcessCost = ad.isoCheck(  newAssetId, page1,transIdCost,tranIdReverse);
//      String prcessAccum = ad.isoCheck( newAssetId, page1,transIdAccum,tranIdReverse);         
//       String  improveStatus = records.getCodeName("SELECT IMPROVED FROM am_asset_improvement WHERE revalue_Id = '"+tranId+"'"); 
      String  reversalStatus = records.getCodeName("SELECT REVERSED FROM am_UncapitalizedTransfer WHERE Transfer_ID = '"+tranId+"'"); 
      String branchCode=records.getCodeName("select BRANCH_CODE from AM_ASSET_UNCAPITALIZED where asset_id='"+old_asset_id+"'");
      String assetNarration = records.getCodeName("select Description from AM_ASSET_UNCAPITALIZED where asset_id='"+old_asset_id+"'");
//      System.out.println("transactionIdCost: "+transactionIdCost);
       if(transactionIdCost.equals("000")){
      	 isoValue = "0";
       } 
//       System.out.println("====>processCost: "+prcessCost);
       if(prcessCost.equals("000")){
      	 isoValueReverse = "0";
       }          
      try
      {
//      	System.out.println("isoValue: "+isoValue+"   isoValueReverse: "+isoValueReverse+"   reversalStatus: "+reversalStatus);
      	if(isoValue.equalsIgnoreCase("0") && !reversalStatus.equalsIgnoreCase("Y"))
          {
          	ad.updateAssetStatusChange("update AM_ASSET_UNCAPITALIZED set old_asset_id ='" + old_asset_id + "', asset_id ='" + newAssetId + "', DEPT_ID = '"+newDeptID+"', BRANCH_ID = '"+newBranchID+"', section_id = '"+newSectionID+"', asset_user = '"+newAssetUser+"', SBU_CODE = '"+newSbuCode+"',BRANCH_CODE = '"+newbranchcode+"', DEPT_CODE = '"+newdeptcode+"', SECTION_CODE = '"+newscetioncode+"'    where asset_id ='" + old_asset_id + "'"); 
          	ad.updateAssetStatusChange("update am_raisentry_post set GroupIdStatus = 'Y',entryPostFlag = 'Y' where asset_code="+assetCode+" AND page = 'UNCAPITALISED ASSET TRANSFER RAISE ENTRY'"); 
          	ad.updateAssetStatusChange("update am_UncapitalizedTransfer set REVERSED = 'Y' where TRANSFER_ID = "+tranId+" ");  
         	String mailaddress=records.getCodeName("select email from am_ad_branch where BRANCH_CODE='"+branchCode+"'");
         	String to = mailaddress;
         	String subject = "Uncapitalized Asset Transfer";
         	String msgText1 = "Your Branch has been debited for the Transfer of the Uncapitalized Asset "+assetNarration+" with asset Id "+newAssetId+"";
         	mail.sendMail(to,subject,msgText1);		
          }
          if(isoValueReverse.equalsIgnoreCase("0") && reversalStatus.equalsIgnoreCase("Y"))
          {
//          	System.out.println("<<<<<<<<<Reversal isoValueReverse ======= "+isoValueReverse+"     reversalStatus: "+reversalStatus);
          	ad.updateAssetStatusChange("UPDATE AM_ASSET_UNCAPITALIZED SET AM_ASSET_UNCAPITALIZED.Asset_id = am_UncapitalizedTransfer.asset_id,AM_ASSET_UNCAPITALIZED.ASSET_USER = am_UncapitalizedTransfer.OLD_Asset_user,"
          			+ "AM_ASSET_UNCAPITALIZED.BRANCH_CODE = am_UncapitalizedTransfer.OLD_BRANCH_CODE, AM_ASSET_UNCAPITALIZED.BRANCH_ID = am_UncapitalizedTransfer.OLD_branch_Id, "
          			+ "AM_ASSET_UNCAPITALIZED.CATEGORY_CODE = am_UncapitalizedTransfer.OLD_CATEGORY_CODE,AM_ASSET_UNCAPITALIZED.DEPT_CODE = am_UncapitalizedTransfer.OLD_DEPT_CODE,"
          			+ "AM_ASSET_UNCAPITALIZED.DEPT_ID = am_UncapitalizedTransfer.OLD_dept_ID,AM_ASSET_UNCAPITALIZED.SBU_CODE = am_UncapitalizedTransfer.OLD_SBU_CODE,"
          			+ "AM_ASSET_UNCAPITALIZED.SECTION = am_UncapitalizedTransfer.OLD_Section,AM_ASSET_UNCAPITALIZED.SECTION_CODE = am_UncapitalizedTransfer.OLD_SECTION_CODE "
          			+ "FROM am_UncapitalizedTransfer, AM_ASSET_UNCAPITALIZED WHERE AM_ASSET_UNCAPITALIZED.ASSET_ID = am_UncapitalizedTransfer.NEW_ASSET_ID "
          			+ "AND am_UncapitalizedTransfer.TRANSFER_ID = "+tranId+""); 
          	ad.updateAssetStatusChange("update am_UncapitalizedTransfer set REVERSED = 'R'where TRANSFER_ID = "+tranId+"  "); 
         	String mailaddress=records.getCodeName("select email from am_ad_branch where BRANCH_CODE='"+branchCode+"'");
         	String to = mailaddress;
         	String subject = "Uncapitalized Asset Transfer";
         	String msgText1 = "Your Branch has been credited for the previous debit for Transfer of the Uncapitalized Asset "+assetNarration+" with asset Id "+newAssetId+"";
         	mail.sendMail(to,subject,msgText1);		
          	
          } 
//          System.out.println("destination ================== "+destination+" iso Value: "+isoValue );
          
//          out.print("<script>alert('About To Close the Page ')</script>");
          out.print("<script>window.location='"+destination+"'</script>");
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
        return "End Asset Creation Entry Servlet";
    }
}