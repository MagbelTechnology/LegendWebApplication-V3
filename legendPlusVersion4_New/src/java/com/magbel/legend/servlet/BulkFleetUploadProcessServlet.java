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
public class BulkFleetUploadProcessServlet extends HttpServlet {
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
    public BulkFleetUploadProcessServlet()
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
//		System.out.println("<<<<<<<<raiseBtn in BulkFleetUploadProcessServlet: "+raiseBtn+"     reject: "+reject);
		HttpSession session = request.getSession();
		String userClass = (String) session.getAttribute("UserClass");
		String userId =(String)session.getAttribute("CurrentUser");
			boolean transfer = false;
		  try
		        {  
			  if (!userClass.equals("NULL") || userClass!=null){
//			  System.out.println("<<<<<<<<userId>>>>>>>> "+userId);
			  if(raiseBtn.equalsIgnoreCase("Posting")){
			  java.util.ArrayList list =assetMan.getFleetAssetRecords(groupid);
//			  System.out.println("<<<<<<<<List Size>>>>>>>> "+list.size()+"  groupid: "+groupid);
			     for(int k=0;k<list.size();k++)
			     {
//			    	 System.out.println("<<<<<<<<List Size>>>>>>>> "+list.size());
			    	 magma.FleetExcelUploadBean  fleettrans = (magma.FleetExcelUploadBean)list.get(k);    	 
					String assetid =  fleettrans.getAsset_id();
					String groupId =  fleettrans.getGid();
					String vendorId = fleettrans.getVendor();
					String vendorAccount = fleettrans.getVendor_account();
					String costprice = fleettrans.getCost_price();
					String description = fleettrans.getDescription();
					String location = fleettrans.getLocation();
					String projectCode = fleettrans.getProjectCode();
					String tranType = fleettrans.getTranType();
					String page1 = "ASSET FLEET UPLOAD RAISE ENTRY";
					String narration = htmlUtil.findObject("SELECT DESCRIPTION FROM FT_PROCESSING_TYPE WHERE FT_TYPE_CODE="+tranType);
					String internalAccount = htmlUtil.findObject("SELECT GL_ACCOUNT FROM FT_PROCESSING_TYPE WHERE FT_TYPE_CODE="+tranType);
					htmlUtil.updateAssetStatusChange("update FT_GROUP_DUE_PERIOD set STATUS = 'PAID' where GROUP_ID='"+groupid+"'"); 
					htmlUtil.updateAssetStatusChange("update FT_DUE_PERIOD set STATUS = 'PAID' where GROUP_ID='"+groupid+"'"); 
					ad.insertVendorTransaction(userId,description,internalAccount,vendorAccount,Double.parseDouble(costprice),location,groupId,page1,projectCode,vendorId);
//					System.out.println("<<<<<<<<groupid>>>>>>>> "+groupid);
					htmlUtil.updateAssetStatusChange("update am_raisentry_post set GroupIdStatus = 'Y',entryPostFlag = 'Y' where id='"+groupid+"'"); 
					htmlUtil.updateAssetStatusChange("update am_asset_approval set Process_Status = 'A' where batch_id='"+groupid+"'");
			    	 if(!assetid.equalsIgnoreCase(""))
			    		 insertBulkFleetPostingEntries(assetid, groupId);
			     }
			  }
			  if(reject.equalsIgnoreCase("Reject")){
				  DeletebulkFleetUploadRejections(groupid);
//	                System.out.println(">>>>>>>>>>> About to send Mail on the Batch "+groupid);
	                String msgText1 = "Your transaction for Bulk Fleet Upload with Batch ID " + groupid + " has been Rejected. The new Batch ID is " + groupid;
	                mail.sendMailTransactionInitiator(Integer.parseInt(groupid), "Bulk Fleet Upload", msgText1);
			  }
	                out.print("<script>window.location='DocumentHelp.jsp?np=OpexRaiseEntry&batchId="+groupid+"'</script>");
		        }
		        }
		        catch(Exception e)
		        {
		            e.printStackTrace();
		        }
	//	    }
		
	}
	
public boolean insertBulkFleetPostingEntries(String assetId, String batchId) {
	String query = "INSERT INTO am_gb_bulkPostingEntries (asset_id,Account,STATUS,BatchId,sbu_code,amount, narration)"+
    		"select e.asset_id,e.VENDOR_ACCT AS ACCOUNT,'C' AS CR_DR,e.GROUP_ID,e.SBU_CODE,e.COST_PRICE AS AMOUNT, "+ 
    		"a.DESCRIPTION AS NARRATION from FT_PROCESSING_TYPE a,am_ad_branch d, AM_GB_CURRENCY_CODE c, "+ 
    		"am_gb_company b, FT_DUE_PERIOD e where a.FT_TYPE_CODE = e.TRANS_TYPE AND c.local_currency = 'Y'"+
    		"and d.branch_id = e.BRANCH_ID and e.GROUP_ID = '"+batchId+"' and e.Asset_id = '"+assetId+"' "+
    		"UNION "+
    		"(select e.asset_id,c.iso_code +(select accronym from am_ad_ledger_type where series = substring(a.GL_ACCOUNT,1,1))+ "+
    		"d.branch_code +	a.GL_ACCOUNT ACCOUNT,'D' AS CR_DR,e.GROUP_ID,e.SBU_CODE,e.COST_PRICE AS AMOUNT, "+
    		"a.DESCRIPTION AS NARRATION  from FT_PROCESSING_TYPE a,am_ad_branch d, AM_GB_CURRENCY_CODE c, "+
    		"am_gb_company b, FT_DUE_PERIOD e where a.FT_TYPE_CODE = e.TRANS_TYPE AND c.local_currency = 'Y' "+
    		"and d.branch_id = e.BRANCH_ID and e.GROUP_ID = '"+batchId+"' and e.Asset_id = '"+assetId+"')";
Connection con = null;
PreparedStatement ps = null;
boolean done = false;
// System.out.println("insertBulkFleetPostingEntries query: "+query);
try {
con = mgDbCon.getConnection("legendPlus");
ps = con.prepareStatement(query);

done = (ps.executeUpdate() != -1);
} catch (Exception ex) {
done = false;
System.out.println("Error insertBulkFleetPostingEntries() of BulkFleetUploadProcessServlet -> " + ex.getMessage());
ex.printStackTrace();
} finally {
closeConnection(con, ps);
}
return done;
}
public void DeletebulkFleetPostings(){
	String query_r ="delete from am_gb_bulktransferPostings";

	Connection con = null;
	        PreparedStatement ps = null;
	try {
		con = mgDbCon.getConnection("legendPlus");
	ps = con.prepareStatement(query_r);
	           int i =ps.executeUpdate();
	        } catch (Exception ex) {
	  
	            System.out.println("BulkFleetUploadProcessServlet: DeletebulkFleetPostings()>>>>>" + ex);
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
	
	public void DeletebulkFleetUploadRejections(String batchId){
		String query1 ="delete from FT_GROUP_DUE_PERIOD where GROUP_ID = '"+batchId+"'";
		String query2 ="delete from FT_DUE_PERIOD where GROUP_ID = '"+batchId+"'";
		String query3 ="delete from am_asset_approval where batch_id = '"+batchId+"'";
		Connection con = null;
		        PreparedStatement ps = null;
		try {
			con = mgDbCon.getConnection("legendPlus");
				ps = con.prepareStatement(query1);
				int i =ps.executeUpdate();
		        } catch (Exception ex) {
		            System.out.println("DeletebulkFleetUploadRejections: DeletebulkFleetUploadRejections() for FT_GROUP_DUE_PERIOD>>>>>" + ex);
		        } finally {
		            closeConnection(con, ps);
		        }
		try {
			con = mgDbCon.getConnection("legendPlus");
				ps = con.prepareStatement(query2);
				int i =ps.executeUpdate();
		        } catch (Exception ex) {
		            System.out.println("DeletebulkFleetUploadRejections: DeletebulkFleetUploadRejections() for FT_DUE_PERIOD>>>>>" + ex);
		        } finally {
		            closeConnection(con, ps);
		        }		
		try {
			con = mgDbCon.getConnection("legendPlus");
				ps = con.prepareStatement(query3);
				int i =ps.executeUpdate();
		        } catch (Exception ex) {
		            System.out.println("DeletebulkFleetUploadRejections: DeletebulkFleetUploadRejections() for am_asset_approval>>>>>" + ex);
		        } finally {
		            closeConnection(con, ps);
		        }					
		}	
	
}
