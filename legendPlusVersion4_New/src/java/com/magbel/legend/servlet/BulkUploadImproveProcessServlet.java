package com.magbel.legend.servlet;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import legend.admin.handlers.CompanyHandler;
import magma.net.dao.MagmaDBConnection;
import magma.net.manager.DepreciationProcessingManager;
import magma.net.manager.RaiseEntryManager;
import magma.util.Codes;
import magma.asset.manager.AssetManager;
import magma.AssetRecordsBean;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.FinancialExchangeServiceBus;
import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.util.*;
/**
 * Servlet implementation class RequisitionServlet
 */
public class BulkUploadImproveProcessServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private AssetRecordsBean ad;
    /**
     * @see HttpServlet#HttpServlet()
     */
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;  
	MagmaDBConnection mgDbCon = null;
	ApplicationHelper applHelper = null;
	AssetManager assetMan = null;
	HtmlUtility htmlUtil =null;
    public BulkUploadImproveProcessServlet()
    {
        super();
        try
        {
        ad = new AssetRecordsBean();
        }
        
        catch(Exception et)
        {et.printStackTrace();}
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processCategoryItemType(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processCategoryItemType(request,response);
	}

	private void processCategoryItemType(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException 
	{
		// TODO Auto-generated method stub
		mgDbCon = new MagmaDBConnection();
		applHelper = new ApplicationHelper();
		htmlUtil = new HtmlUtility();
		assetMan = new AssetManager();
		PrintWriter out = response.getWriter();
		 EmailSmsServiceBus mail = new EmailSmsServiceBus();
		String groupid = request.getParameter("groupid");
		String raiseBtn = request.getParameter("raiseBtn");
		String reject = request.getParameter("reject");
		
		response.setContentType("text/html");
		System.out.println("<<<<<<<<raiseBtn: "+raiseBtn+"     reject: "+reject);
		HttpSession session = request.getSession();
		String userClass = (String) session.getAttribute("UserClass");
		String userId =(String)session.getAttribute("CurrentUser");
			boolean transfer = false;
		  try
		        {  
			  if (!userClass.equals("NULL") || userClass!=null){
			  if(raiseBtn.equalsIgnoreCase("Posting") && !reject.equalsIgnoreCase("Reject")){
			  java.util.ArrayList list =assetMan.getImprovedAssetRecords(groupid);
			     for(int k=0;k<list.size();k++)
			     {
			    	 magma.ExcelAssetImproveBean  improvetrans = (magma.ExcelAssetImproveBean)list.get(k);    	 
					String assetid =  improvetrans.getAsset_id();
					String groupId =  improvetrans.getGid();
					String assetCode = improvetrans.getAsset_code();
					String branchId = improvetrans.getBranch_id();
					String vendorId = improvetrans.getVendor();
					String vendorAccount = improvetrans.getVendor_account();
					String costprice = improvetrans.getCost_price();
					String description = improvetrans.getDescription();
					String location = improvetrans.getLocation();
					String projectCode = improvetrans.getProjectCode();
					String noofMonths = improvetrans.getNoOfMonths();
					String vatablecost = improvetrans.getVatable_cost();
					String vatamount = improvetrans.getVat_amount();
					String vatrate = improvetrans.getVatRate();
					String whtAmount = improvetrans.getWh_tax_amount();
					String newcostprice = improvetrans.getNewcost_price();
					String newnbv = improvetrans.getNewnbv();
					String newvatamount = improvetrans.getNewvat_amount();
					String newvatablecost = improvetrans.getNewvatable_cost();
					String usefullife = improvetrans.getUsefullife();
					String newwhtamount = improvetrans.getNewwht_amount();
					String oldAccumDep = improvetrans.getOldaccum_dep();
					String oldCostPrice = improvetrans.getOldcost_price();
					String oldimprovAccum = improvetrans.getOldIMPROV_ACCUMDEP();
					String oldimprovCost = improvetrans.getOldIMPROV_COST();
					String oldimprovNBV = improvetrans.getOldIMPROV_NBV();
					String oldimprovvatablecost = improvetrans.getOldIMPROV_VATABLECOST();
					String oldnbv = improvetrans.getOldnbv();
					String oldvatamount = improvetrans.getOldvat_amount();
					String oldvatablecost = improvetrans.getOldvatable_cost();
					String oldwhtAmount = improvetrans.getOldwht_amount();
					String page1 = "ASSET IMPROVEMENT UPLOAD RAISE ENTRY";
//					System.out.println("<<<<<<<<newcostprice>>>>>>  "+newcostprice+"  newnbv>> "+newnbv+" newvatablecost: "+newvatablecost+"  newvatamount: "+newvatamount+"  newwhtamount: "+newwhtamount);
//					System.out.println("<<<<<<<<usefullife>>>>>>  "+usefullife+"  costprice>> "+costprice+" vatablecost: "+vatablecost+"  oldnbv: "+oldnbv);
					assetMan.processGroupImprovement(assetid,Double.parseDouble(newcostprice),Double.parseDouble(newnbv),
					Double.parseDouble(newvatablecost),Double.parseDouble(newvatamount),Double.parseDouble(newwhtamount),
					Integer.parseInt(usefullife),Double.parseDouble(costprice),Double.parseDouble(vatablecost),oldnbv,
					Integer.parseInt(groupid));
					ad.insertVendorTransaction(userId,description,vendorAccount,vendorAccount,Double.parseDouble(costprice),location,groupId,page1,projectCode,vendorId);
//					System.out.println("<<<<<<<<groupid>>>>>>>> "+groupid);
					htmlUtil.updateAssetStatusChange("update am_raisentry_post set GroupIdStatus = 'Y',entryPostFlag = 'Y' where id='"+groupid+"'"); 
					htmlUtil.updateAssetStatusChange("update am_asset_approval set Process_Status = 'A' where batch_id='"+groupid+"'");
					htmlUtil.updateAssetStatusChange("update am_asset_improvement_Upload set approval_status = 'ACTIVE', RAISE_ENTRY = 'Y' where Revalue_ID='"+groupid+"' ");
			     }
			  }
			  if(reject.equalsIgnoreCase("Reject")){
				  DeletebulkImprovementUploadRejections(groupid);
//	                System.out.println(">>>>>>>>>>> About to send Mail on the Batch "+groupid);
	                String msgText1 = "Your transaction for Bulk asset Improvement Upload with Batch ID " + groupid + " has been Rejected. The new Batch ID is " + groupid;
	                mail.sendMailTransactionInitiator(Integer.parseInt(groupid), "Bulk Asset Upload", msgText1);
			  }
	                out.print("<script>window.location='DocumentHelp.jsp?np=raiseEntry&batchId="+groupid+"'</script>");
		        } 
		        }
		        catch(Exception e)
		        {
		            e.printStackTrace();
		        }
	//	    }
		
	}
	
	public void DeletebulkImprovementUploadRejections(String batchId){
//		String query1 ="delete from AM_GROUP_IMPROVEMENT where REVALUE_ID = '"+batchId+"'";
        String qa1 = "update AM_GROUP_IMPROVEMENT SET STATUS = 'REJECTED' where REVALUE_ID = '"+batchId+"'";
        ad.updateAssetStatusChange(qa1);	 
		
//		String query2 ="delete from am_asset_improvement where REVALUE_ID = '"+batchId+"'";
        String qa2 = "update am_asset_improvement SET STATUS = 'REJECTED' where REVALUE_ID = '"+batchId+"'";
        ad.updateAssetStatusChange(qa2);	 		
//		String query3 ="delete from am_asset_approval where batch_id = '"+batchId+"'";
        String qa3 = "update am_asset_approval SET ASSET_STATUS = 'REJECTED',PROCESS_STATUS = 'R' where batch_id = '"+batchId+"'";
        ad.updateAssetStatusChange(qa3);	 
//		String query4 ="delete from am_asset_improvement_Upload where REVALUE_ID = '"+batchId+"'";
        String qa4 = "update am_asset_improvement_Upload SET STATUS = 'REJECTED' where REVALUE_ID = '"+batchId+"'";
        ad.updateAssetStatusChange(qa4);	 
//		Connection con = null;
//		        PreparedStatement ps = null;
//		try {
//			con = mgDbCon.getConnection("legendPlus");
//				ps = con.prepareStatement(query1);
//				int i =ps.executeUpdate();
//		        } catch (Exception ex) {
//		            System.out.println("BulkUploadRejectionServlet: DeletebulkUploadRejections() for AM_GROUP_IMPROVEMENT>>>>>" + ex);
//		        } finally {
//		            closeConnection(con, ps);
//		        }
//		try {
//			con = mgDbCon.getConnection("legendPlus");
//				ps = con.prepareStatement(query2);
//				int i =ps.executeUpdate();
//		        } catch (Exception ex) {
//		            System.out.println("BulkUploadRejectionServlet: DeletebulkUploadRejections() for am_asset_improvement>>>>>" + ex);
//		        } finally {
//		            closeConnection(con, ps);
//		        }		
//		try {
//			con = mgDbCon.getConnection("legendPlus");
//				ps = con.prepareStatement(query3);
//				int i =ps.executeUpdate();
//		        } catch (Exception ex) {
//		            System.out.println("BulkUploadRejectionServlet: DeletebulkUploadRejections() for am_asset_approval>>>>>" + ex);
//		        } finally {
//		            closeConnection(con, ps);
//		        }	
//		try {
//			con = mgDbCon.getConnection("legendPlus");
//				ps = con.prepareStatement(query4);
//				int i =ps.executeUpdate();  
//		        } catch (Exception ex) {
//		            System.out.println("BulkUploadRejectionServlet: DeletebulkUploadRejections() for am_asset_approval>>>>>" + ex);
//		        } finally {
//		            closeConnection(con, ps);
//		        }
		}	
    private void closeConnection(Connection con, PreparedStatement ps) {
		try {
			if (ps != null) {
				ps.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (Exception e) {
			System.out.println("WARNING: Error closing connection ->"
					+ e.getMessage());
		}

	}	
	
	private void closeConnection(Connection con, Statement s, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (s != null) {
				s.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (Exception e) {
			System.out.println("WARNING: Error closing connection ->"
					+ e.getMessage());
		}
	}

}
