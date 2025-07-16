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

public class EndWIPRaiseEntry extends HttpServlet
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
    public EndWIPRaiseEntry()
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
        String id = request.getParameter("tranId");
        int assetCode = request.getParameter("assetCode") == null?0:Integer.parseInt(request.getParameter("assetCode"));
        String assetId = request.getParameter("assetId");
        String page1 = request.getParameter("page1");

        /**
        Post Cost Transaction
        */
        String newAssetId =records.getCodeName("select new_asset_Id from AM_WIP_RECLASSIFICATION where asset_id='"+id+"'");
        String destination = request.getParameter("destination");
        String tranId =  id;
        String isoValue = "";
        String isoValueReverse = "";
        System.out.println("<<<<<tranId: "+tranId+"   page1: "+page1);
   //     String isoValue=records.getCodeName("select count(*) from am_Raisentry_Transaction where Trans_id='"+tranId+"' and page1 = 'ASSET IMPROVEMENT RAISE ENTRY' and iso != '000' ");
        String tranIdReverse = "R"+tranId;
        String transactionIdCost = ad.isoCheck(assetId, page1, "32",tranId);
        String transactionIdCostRev = ad.isoCheck(assetId, page1, "32",tranIdReverse);
        System.out.println("isoValue ================== "+isoValue+" Asset Id: "+id+"    destination: "+destination );
//        String branchCode=records.getCodeName("select BRANCH_CODE from AM_ASSET_UNCAPITALIZED where asset_id='"+id+"'");
//        String assetNarration = records.getCodeName("select Description from AM_ASSET_UNCAPITALIZED where asset_id='"+id+"'");
//        System.out.println("transactionIdCost: "+transactionIdCost+"     transactionIdWitholding: "+transactionIdWitholding);
//        System.out.println("transactionIdCostRev: "+transactionIdCostRev+"     transactionIdWitholdingRev: "+transactionIdWitholdingRev);
         if(transactionIdCost.equals("000")){
        	 isoValue = "0";
         }    
         if(transactionIdCostRev.equals("000")){
        	 isoValueReverse = "0";
         }  
        try
        {
        	 if (!userClass.equals("NULL") || userClass!=null){
            if(isoValue.equalsIgnoreCase("0")){
            // 	System.out.println("isoValue ================== "+isoValue+" Asset Id: "+id+"    destination: "+destination );
            	ad.updateAssetStatusChange("update am_raisentry_post set entrypostflag='Y', GroupIdStatus = 'Y' where trans_Id='"+id+"' and page='WIP RECLASSIFICATION' "); 
            	ad.updateAssetStatusChange("UPDATE am_asset SET am_asset.Branch_ID = AM_WIP_RECLASSIFICATION.New_branch_id,am_asset.BRANCH_CODE = AM_WIP_RECLASSIFICATION.NEW_BRANCH_CODE,am_asset.Dept_ID = AM_WIP_RECLASSIFICATION.New_dept_id,"
            			+ "am_asset.DEPT_CODE = AM_WIP_RECLASSIFICATION.NEW_DEPT_CODE,am_asset.Section_id = AM_WIP_RECLASSIFICATION.New_Section,am_asset.SECTION_CODE = AM_WIP_RECLASSIFICATION.NEW_SECTION_CODE,"
            			+ "am_asset.OLD_ASSET_ID = AM_WIP_RECLASSIFICATION.ASSET_ID,am_asset.ASSET_ID = AM_WIP_RECLASSIFICATION.NEW_ASSET_ID,"
            			+ "am_asset.Category_ID = AM_WIP_RECLASSIFICATION.New_Cat_ID,am_asset.CATEGORY_CODE = AM_WIP_RECLASSIFICATION.NEW_CAT_CODE FROM AM_WIP_RECLASSIFICATION, am_asset "
            			+ "WHERE AM_WIP_RECLASSIFICATION.ASSET_ID = am_asset.ASSET_ID AND AM_WIP_RECLASSIFICATION.Transfer_ID = '"+id+"'"); 
            	ad.updateAssetStatusChange("update am_asset_approval set Process_Status = 'A' where transaction_id='"+id+"'"); 
             }
            if(isoValueReverse.equalsIgnoreCase("0") && isoValue.equalsIgnoreCase("0"))
            { 
//            	System.out.println("isoValueReverse ================== "+isoValueReverse+" Asset Id: "+id+"    destination: "+destination );
            ad.updateAssetStatusChange("update am_raisentry_post set entrypostflag='Y', GroupIdStatus = 'Y' where trans_Id='"+id+"' and page='WIP RECLASSIFICATION' ");  
            ad.updateAssetStatusChange("am_asset SET am_asset.Branch_ID = AM_WIP_RECLASSIFICATION.OLD_branch_Id,am_asset.BRANCH_CODE = AM_WIP_RECLASSIFICATION.OLD_BRANCH_CODE,am_asset.Dept_ID = AM_WIP_RECLASSIFICATION.OLD_dept_ID,"
        			+ "am_asset.DEPT_CODE = AM_WIP_RECLASSIFICATION.OLD_DEPT_CODE,am_asset.Section_id = AM_WIP_RECLASSIFICATION.OLD_Section,am_asset.SECTION_CODE = AM_WIP_RECLASSIFICATION.OLD_SECTION_CODE,"
        			+ "am_asset.OLD_ASSET_ID = '',am_asset.ASSET_ID = AM_WIP_RECLASSIFICATION.ASSET_ID,"
        			+ "am_asset.Category_ID = AM_WIP_RECLASSIFICATION.OLD_CAT_ID,am_asset.CATEGORY_CODE = AM_WIP_RECLASSIFICATION.OLD_CAT_CODE FROM AM_WIP_RECLASSIFICATION, am_asset "
        			+ "WHERE AM_WIP_RECLASSIFICATION.ASSET_ID = am_asset.ASSET_ID AND AM_WIP_RECLASSIFICATION.Transfer_ID = '"+id+"'");  
            ad.updateAssetStatusChange("update am_asset_approval set Process_Status = 'A' where transaction_id='"+id+"'");
            }             
//            System.out.println("destination ================== "+destination+" iso Value: "+isoValue );
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
    }
    public String getServletInfo()
    {
        return "End Asset Creation Entry Servlet";
    }
}