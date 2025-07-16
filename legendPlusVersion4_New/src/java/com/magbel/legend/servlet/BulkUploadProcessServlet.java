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
public class BulkUploadProcessServlet extends HttpServlet {
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
    public BulkUploadProcessServlet()
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
		 String page1 = "ASSET UPLOAD CREATION RAISE ENTRY";
		String groupid = request.getParameter("groupid");
		String raiseBtn = request.getParameter("raiseBtn");
		String reject = request.getParameter("reject");
		String vendordebitNarration = request.getParameter("description");
		String vendoramount = request.getParameter("totalcostPrice");
//		System.out.println("<<<<<<<<=====vendoramount: "+vendoramount);
		String vendorcreditAccount = request.getParameter("vendorAccount");
		String vendorIdgrp = request.getParameter("vendorId");
		String vendordebitAccount = "UploadAccount";
        String locationgrp = request.getParameter("location");
        String projectCodegrp = request.getParameter("projectCode");
        String transactionId = "grp";
		String status = "Uploaded Assets have been made Available in the Register Ensure Bulk Posting is done on Legacy System"; 
		response.setContentType("text/html");
//		System.out.println("<<<<<<<<raiseBtn: "+raiseBtn+"     reject: "+reject);
		HttpSession session = request.getSession();
		String userClass = (String) session.getAttribute("UserClass");
		String userId =(String)session.getAttribute("CurrentUser");
			boolean transfer = false;
		  try
		        { 
			  if (!userClass.equals("NULL") || userClass!=null){
			  if(raiseBtn.equalsIgnoreCase("Posting")){
			  java.util.ArrayList list =assetMan.getUploadAssetRecords(groupid);
			     for(int k=0;k<list.size();k++)
			     {
			    	 magma.net.vao.GroupAsset  uploadtrans = (magma.net.vao.GroupAsset)list.get(k);    	 
					String assetid =  uploadtrans.getAssetId();
					String groupId =  uploadtrans.getGroupId();
					String assetCode = uploadtrans.getAsset_code();
					String vendorId = uploadtrans.getVendorId();
					String vendorAccount = uploadtrans.getVendorAccount();
					double costprice = uploadtrans.getCostPrice();
					String description = uploadtrans.getDescription();
					String location = uploadtrans.getLocation();
					String projectCode = uploadtrans.getProjectCode();
					String categoryCode = uploadtrans.getCategory();
					String branchCode = uploadtrans.getBranchCode();
					String assetProcurementDr =ad.checkAssetLedgerAccount(categoryCode.trim(),branchCode.trim());
					
					
					ad.insertVendorTransaction(userId,description,assetProcurementDr,vendorAccount,costprice,location,groupId,page1,projectCode,vendorId);
		//			System.out.println("<<<<<<<<groupid>>>>>>>> "+groupid+"   assetid: "+assetid);
					ad.updateAssetStatusChange("update AM_ASSET set ASSET_STATUS = 'ACTIVE' where GROUP_ID='"+groupid+"' AND ASSET_ID = '"+assetid+"'");
					ad.updateAssetStatusChange("update AM_GROUP_ASSET set ASSET_STATUS = 'ACTIVE', RAISE_ENTRY = 'Y' where GROUP_ID='"+groupid+"' AND ASSET_ID = '"+assetid+"'");
					ad.updateAssetStatusChange("update am_group_asset_main set ASSET_STATUS = 'ACTIVE' where GROUP_ID='"+groupid+"' ");
			     }
					ad.updateAssetStatusChange("update am_raisentry_post set GroupIdStatus = 'Y',entryPostFlag = 'Y' where id='"+groupid+"'"); 
					ad.updateAssetStatusChange("update am_asset_approval set Process_Status = 'A' where batch_id='"+groupid+"'"); 
					ad.insertVendorTransaction(userId,vendordebitNarration,vendordebitAccount,vendorcreditAccount,Double.parseDouble(vendoramount),locationgrp,transactionId,page1,projectCodegrp,vendorIdgrp);
			  }
			  if(reject.equalsIgnoreCase("Reject")){
				  DeletebulkImprovementUploadRejections(groupid); 
//	                System.out.println(">>>>>>>>>>> About to send Mail on the Batch "+groupid);
	                String msgText1 = "Your transaction for Bulk asset Upload with Batch ID " + groupid + " has been Rejected. The new Batch ID is " + groupid;
	                mail.sendMailTransactionInitiator(Integer.parseInt(groupid), "Bulk Asset Upload", msgText1);
			  }
			  		out.print("<script>alert('"+status+"');</script>");
//	                out.print("<script>window.location='DocumentHelp.jsp?np=raiseEntry&batchId="+groupid+"'</script>");
	                out.print("<script>window.location='DocumentHelp.jsp?np=raiseEntry&tranType=Asset Upload'</script>");
		        }
		        }
		        catch(Exception e)
		        {
		            e.printStackTrace();
		        }
	//	    }
		
	}
	
	public void DeletebulkImprovementUploadRejections(String batchId){
		String query1 ="delete from AM_GROUP_ASSET where GROUP_ID = '"+batchId+"'";
		String query2 ="delete from AM_ASSET where GROUP_ID = '"+batchId+"'";
		String query3 ="delete from am_group_asset_main where GROUP_ID = '"+batchId+"'";
		String query4 ="delete from am_asset_approval where batch_id = '"+batchId+"'";
		Connection con = null;
		        PreparedStatement ps = null;
		try {
			con = mgDbCon.getConnection("legendPlus");
				ps = con.prepareStatement(query1);
				int i =ps.executeUpdate();
		        } catch (Exception ex) {
		            System.out.println("BulkUploadRejectionServlet: DeletebulkUploadRejections() for AM_GROUP_ASSET>>>>>" + ex);
		        } finally {
		            closeConnection(con, ps);
		        }
		try {
			con = mgDbCon.getConnection("legendPlus");
				ps = con.prepareStatement(query2);
				int i =ps.executeUpdate();
		        } catch (Exception ex) {
		            System.out.println("BulkUploadRejectionServlet: DeletebulkUploadRejections() for AM_ASSET>>>>>" + ex);
		        } finally {
		            closeConnection(con, ps);
		        }		
		try {
			con = mgDbCon.getConnection("legendPlus");
				ps = con.prepareStatement(query3);
				int i =ps.executeUpdate();
		        } catch (Exception ex) {
		            System.out.println("BulkUploadRejectionServlet: DeletebulkUploadRejections() for am_group_asset_main>>>>>" + ex);
		        } finally {
		            closeConnection(con, ps);
		        }		
		try {
			con = mgDbCon.getConnection("legendPlus");
				ps = con.prepareStatement(query4);
				int i =ps.executeUpdate();
		        } catch (Exception ex) {
		            System.out.println("BulkUploadRejectionServlet: DeletebulkUploadRejections() for am_asset_approval>>>>>" + ex);
		        } finally {
		            closeConnection(con, ps);
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
