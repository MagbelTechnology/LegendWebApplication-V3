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
import magma.ExcelAssetDisposalBean;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.FinancialExchangeServiceBus;
import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.util.*;
/**
 * Servlet implementation class RequisitionServlet
 */
public class BulkUploadDisposalProcessServlet extends HttpServlet {
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
    public BulkUploadDisposalProcessServlet()
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
//		System.out.println("<<<<<<<<raiseBtn: "+raiseBtn+"     reject: "+reject);
		HttpSession session = request.getSession();
		String userClass = (String) session.getAttribute("UserClass");
		String userId =(String)session.getAttribute("CurrentUser");
			boolean transfer = false;
		  try
		        {  
			  if (!userClass.equals("NULL") || userClass!=null){
			  if(raiseBtn.equalsIgnoreCase("Posting") && !reject.equalsIgnoreCase("Reject")){
//				  System.out.println("<<<<<<<<==groupid===>>>>>>>>: "+groupid);
			  java.util.ArrayList list =assetMan.getUploadAssetDisposalRecords(groupid);
//			  System.out.println("<<<<<<<<list.size(): "+list.size());
			     for(int k=0;k<list.size();k++)
			     {  
			    	 ExcelAssetDisposalBean  disposaltrans = (magma.ExcelAssetDisposalBean)list.get(k);  
//			    	 System.out.println("<<<<<<<<disposaltrans.getAsset_id(): "+disposaltrans.getAsset_id());
					String assetid =  disposaltrans.getAsset_id();
					String groupId =  disposaltrans.getGid();
					String vendorId = disposaltrans.getVendor();
					String vendorAccount = disposaltrans.getVendor_account();
					String costprice = disposaltrans.getCost_price();
					String description = disposaltrans.getDescription();
					String location = disposaltrans.getLocation();
					String projectCode = disposaltrans.getProjectCode();
					String vatablecost = disposaltrans.getDisposalAmount();
					String newcostprice = disposaltrans.getNewcost_price();
					String newnbv = disposaltrans.getNewnbv();
					String newvatamount = disposaltrans.getNewvat_amount();
					String newvatablecost = disposaltrans.getNewvatable_cost();
					String usefullife = disposaltrans.getUsefullife();
					String newwhtamount = disposaltrans.getNewwht_amount();
					String oldnbv = disposaltrans.getOldnbv();

					String page1 = "ASSET DISPOSAL UPLOAD RAISE ENTRY";
//					System.out.println("<<<<<<<<newcostprice>>>>>>  "+newcostprice+"  newnbv>> "+newnbv+" newvatablecost: "+newvatablecost+"  newvatamount: "+newvatamount+"  newwhtamount: "+newwhtamount);
//					System.out.println("<<<<<<<<usefullife>>>>>>  "+usefullife+"  costprice>> "+costprice+" vatablecost: "+vatablecost+"  oldnbv: "+oldnbv);
//					assetMan.processGroupImprovement(assetid,Double.parseDouble(newcostprice),Double.parseDouble(newnbv),
//					Double.parseDouble(newvatablecost),Double.parseDouble(newvatamount),Double.parseDouble(newwhtamount),
//					Integer.parseInt(usefullife),Double.parseDouble(costprice),Double.parseDouble(vatablecost),oldnbv,
//					Integer.parseInt(groupid));
//					ad.insertVendorTransaction(userId,description,vendorAccount,vendorAccount,Double.parseDouble(costprice),location,groupId,page1,projectCode,vendorId);
//					System.out.println("<<<<<<<<groupid>>>>>>>> "+groupid);
					htmlUtil.updateAssetStatusChange("update am_raisentry_post set GroupIdStatus = 'Y',entryPostFlag = 'Y' where id='"+groupid+"'"); 
					htmlUtil.updateAssetStatusChange("update am_asset_approval set Process_Status = 'A' where batch_id='"+groupid+"'");
					htmlUtil.updateAssetStatusChange("update am_asset_Disposal_Upload set approval_status = 'ACTIVE', RAISE_ENTRY = 'Y' where DISPOSAL_ID='"+groupid+"' ");

//    	        	System.out.println("MMMMMMMMM: ");
					htmlUtil.updateAssetStatusChange("update am_asset_approval set asset_status = 'ACTIVE', process_status = 'A' where batch_Id='"+groupid+"'");
//    	        	System.out.println("WWWWWWWWWWWWWW: ");
					htmlUtil.updateAssetStatusChange("update AM_GROUP_Disposal set DISPOSED = 'Y', STATUS = 'POSTED' where DISPOSAL_ID='"+groupid+"'");
					htmlUtil.updateAssetStatusChange("update a SET a.Asset_Status = 'Disposed' from AM_ASSET a, am_asset_disposal_Upload b where a.Asset_Id = b.Asset_Id and b.disposal_id = '"+groupid+"'");
//    	        	System.out.println("QQQQQQQQQQ: ");
			     }
			  }
			  if(reject.equalsIgnoreCase("Reject")){
				  DeletebulkDisposalUploadRejections(groupid);
//	                System.out.println(">>>>>>>>>>> About to send Mail on the Batch "+groupid);
	                String msgText1 = "Your transaction for Bulk asset Disposal Upload with Batch ID " + groupid + " has been Rejected. The new Batch ID is " + groupid;
	                mail.sendMailTransactionInitiator(Integer.parseInt(groupid), "Bulk Asset Disposal Upload", msgText1);
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
	
	public void DeletebulkDisposalUploadRejections(String batchId){
		String query0 ="update a SET a.Asset_Status = 'ACTIVE' from AM_ASSET a, AM_GROUP_DISPOSAL b where a.Asset_id = b.Asset_id and b.disposal_ID = '"+batchId+"'";
		String query1 ="delete from AM_GROUP_DISPOSAL where DISPOSAL_ID = '"+batchId+"'";
		String query2 ="delete from AM_ASSETDISPOSAL where DISPOSAL_ID = '"+batchId+"'";
		String query3 ="update am_asset_approval set process_status = 'R',Asset_Status = 'Rejected' where batch_id = '"+batchId+"'";
		String query4 ="delete from am_asset_disposal_Upload where DISPOSAL_ID = '"+batchId+"'";
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps4 = null;
		try {
			con = mgDbCon.getConnection("legendPlus");
				ps = con.prepareStatement(query0);
				int i =ps.executeUpdate();
		        } catch (Exception ex) { 
		            System.out.println("DeletebulkDisposalUploadRejections: for AM_ASSET Update>>>>>" + ex);
		        } finally {
		            closeConnection(con, ps);
		        }
		try {
			con = mgDbCon.getConnection("legendPlus");
				ps1 = con.prepareStatement(query1);
				int i =ps1.executeUpdate();
		        } catch (Exception ex) {
		            System.out.println("DeletebulkDisposalUploadRejections: for AM_GROUP_DISPOSAL>>>>>" + ex);
		        } finally {
		            closeConnection(con, ps1);
		        }		
		try {
			con = mgDbCon.getConnection("legendPlus");
				ps2 = con.prepareStatement(query2);
				int i =ps2.executeUpdate();
		        } catch (Exception ex) {
		            System.out.println("DeletebulkDisposalUploadRejections: for AM_ASSETDISPOSAL>>>>>" + ex);
		        } finally {
		            closeConnection(con, ps2);
		        }		
		try {
			con = mgDbCon.getConnection("legendPlus");
				ps3 = con.prepareStatement(query3);
				int i =ps3.executeUpdate();
		        } catch (Exception ex) {
		            System.out.println("DeletebulkDisposalUploadRejections: for am_asset_approval>>>>>" + ex);
		        } finally {
		            closeConnection(con, ps3);
		        }	
		try {
			con = mgDbCon.getConnection("legendPlus");
				ps4 = con.prepareStatement(query4);
				int i =ps4.executeUpdate();  
		        } catch (Exception ex) {
		            System.out.println("DeletebulkDisposalUploadRejections: for am_asset_disposal_Upload>>>>>" + ex);
		        } finally {
		            closeConnection(con, ps4);
		        }
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
