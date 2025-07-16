package com.magbel.legend.servlet;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import magma.net.dao.MagmaDBConnection;

import com.magbel.util.*;
/**
 * Servlet implementation class RequisitionServlet
 */
public class BulkFleetUploadPostingsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;  
	MagmaDBConnection mgDbCon = null;
	ApplicationHelper applHelper = null;
    public BulkFleetUploadPostingsServlet()
    {
        super();
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
		PrintWriter out = response.getWriter();
		
		String batch_Id = request.getParameter("batchId");
		String startdd = request.getParameter("start_dd");
		String startmm = request.getParameter("start_mm");
		String startyy = request.getParameter("start_yy");
		String enddd = request.getParameter("end_dd");
		String endmm = request.getParameter("end_mm");
		String endyy = request.getParameter("end_yy");
		String fromDate = request.getParameter("FromDate");
		String toDate = request.getParameter("ToDate");
		String StartDate = startyy+"-"+startmm+"-"+startdd;
		String EndDate = endyy+"-"+endmm+"-"+enddd;

		HttpSession session = request.getSession();
		String userClass = (String) session.getAttribute("UserClass");
		String []result = null ;
			String batchNo = "";
			String assetId = "";
		  try
		        {  
			  if (!userClass.equals("NULL") || userClass!=null){
			  DeletebulkFleetPostings();
			  	if((batch_Id!=null) || (batch_Id!="")){
			     java.util.ArrayList list =bulkUploadRecordforsReport(batch_Id);
			     insertBulkFleetUploadSummaryPostingEntries(batch_Id);
			     
			     System.out.println("-->size>--> "+list.size()+"  Batch Id:  "+batch_Id);
			     for(int i=0;i<list.size();i++)
			     {
			    	 String assetId_batchNo=(String) list.get(i);
			    	 result = assetId_batchNo.split("#");
			    	 batchNo = result[0];
			    	 assetId = result[1];
			    	 if(!assetId.equalsIgnoreCase(""))
			    		 insertBulkFleetUploadPostingEntries(assetId, batchNo);
			     }
		        }
				  	if((batch_Id==null) || (batch_Id=="")){
					     java.util.ArrayList list1 =bulkUploadRecordforsReport(StartDate,EndDate);
					     for(int i=0;i<list1.size();i++)
					     {
					    	 String assetId_batchNo=(String) list1.get(i);
					    	 result = assetId_batchNo.split("#");
					    	 batchNo = result[0];
					    	 assetId = result[1];
					    	 if(!assetId.equalsIgnoreCase(""))
					    		 insertBulkFleetUploadPostingEntries(assetId, batchNo);
					     }		
				  	}			     
			     out.print("<script>window.location='DocumentHelp.jsp?np=rptMenuBulkFleetPostings&batchId="+batch_Id+"&FromDate="+fromDate+"&ToDate="+toDate+"'</script>");
		        }
		        }
		        catch(Exception e)
		        {
		            e.printStackTrace();
		        }
	//	    }
		
	}

	public java.util.ArrayList bulkUploadRecordforsReport(String batchId)
	{
		java.util.ArrayList _list = new java.util.ArrayList();
		String finacleTransId= null;
			String query = " SELECT  * from FT_GROUP_DUE_PERIOD where STATUS = 'PAID' and GROUP_ID = '"+batchId+"'  ";
			System.out.println("====bulkTransferRecordforsReport query-----> "+query);
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;
	String result = "";
		try {
			   // c = getConnection();
			    c = mgDbCon.getConnection("legendPlus");
				s = c.createStatement();
				rs = s.executeQuery(query);
				while (rs.next())
				   {
					String batch_Id = rs.getString("GROUP_ID");
					String asset_id = rs.getString("Asset_id");
					result = batch_Id + "#"+asset_id;
//					System.out.println("batch_Id:  "+batch_Id+"  asset_id: "+asset_id);
					_list.add(result);
				   }
		 }
					 catch (Exception e)
						{
							e.printStackTrace();
						}
						finally
						{
							closeConnection(c, s, rs);
						}
		return _list;
	}

	public java.util.ArrayList bulkUploadRecordforsReport(String StartDate, String EndDate)
	{
		java.util.ArrayList _list = new java.util.ArrayList();
		String finacleTransId= null;
			String query = " SELECT  * from FT_GROUP_DUE_PERIOD where Posting_Date BETWEEN '"+StartDate+"' AND '"+EndDate+"' AND STATUS = 'PAID'";
			System.out.println("====bulkTransferRecordforsReport query-----> "+query);
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;
	String result = "";
		try {
			   // c = getConnection();
			    c = mgDbCon.getConnection("legendPlus");
				s = c.createStatement();
				rs = s.executeQuery(query);
				while (rs.next())
				   {
					String batch_Id = rs.getString("GROUP_ID");
					String asset_id = rs.getString("Asset_id");
					result = batch_Id + "#"+asset_id;
//					System.out.println("batch_Id:  "+batch_Id+"  asset_id: "+asset_id);
					_list.add(result);
				   }
		 }
					 catch (Exception e)
						{
							e.printStackTrace();
						}
						finally
						{
							closeConnection(c, s, rs);
						}
		return _list;
	}
		
	public boolean insertBulkFleetUploadPostingEntries(String assetId, String batchId) {
		String query = "INSERT INTO am_gb_bulkPostingEntries (asset_id,Account,STATUS,BatchId,sbu_code,amount, narration)"+
	    		"(select e.asset_id,c.iso_code +(select accronym from am_ad_ledger_type where series = substring(a.Asset_Ledger,1,1))+ "+
	    		"d.branch_code +	b.asset_acq_ac ACCOUNT,'C' AS CR_DR,e.GROUP_ID,e.SBU_CODE AS SBU_CODE,e.COST_PRICE AS AMOUNT,"+
	    		"e.Description+'   '+e.ASSET_ID AS NARRATION  from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c, "+
	    		"am_gb_company b, FT_GROUP_DUE_PERIOD e where a.currency_id = c.currency_id and a.category_Code = e.CATEGORY_CODE "+
	    		"and d.branch_id = e.branch_id and e.GROUP_ID = '"+batchId+"' and e.Asset_id = '"+assetId+"' AND e.STATUS = 'PAID' "+
	    		"UNION "+
	    		"select e.asset_id,c.iso_code +(select accronym from am_ad_ledger_type where series = substring(a.Asset_Ledger,1,1))+ "+
	    		"d.branch_code +	a.Asset_Ledger ACCOUNT,'D' AS CR_DR,e.GROUP_ID,e.SBU_CODE AS SBU_CODE,e.COST_PRICE AS AMOUNT, "+
	    		"e.Description+'   '+e.ASSET_ID AS NARRATION from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,  "+
	    		"am_gb_company b, FT_GROUP_DUE_PERIOD e where a.currency_id = c.currency_id and a.category_Code = e.CATEGORY_CODE "+
	    		"and d.branch_id = e.branch_id and e.GROUP_ID = '"+batchId+"' and e.Asset_id = '"+assetId+"' AND e.STATUS = 'PAID') ";
	Connection con = null;
	PreparedStatement ps = null;
	boolean done = false;
//	 System.out.println("insertBulkFleetUploadPostingEntries query: "+query);
	try {
	con = mgDbCon.getConnection("legendPlus");
	ps = con.prepareStatement(query);

	done = (ps.executeUpdate() != -1);
	} catch (Exception ex) {
	done = false;
	System.out.println("Error insertBulkFleetUploadPostingEntries() of BulkFleetUploadPostingsServlet -> " + ex.getMessage());
	ex.printStackTrace();
	} finally {
	closeConnection(con, ps);
	}
	return done;
	}
	
public boolean insertBulkFleetUploadSummaryPostingEntries(String batchId) {
	String query = "INSERT INTO am_gb_bulkPostingEntries (asset_id,Account,STATUS,BatchId,sbu_code,amount, narration)(select e.GROUP_ID,"+
    		"c.iso_code +(select accronym from am_ad_ledger_type where series = substring(a.Asset_Ledger,1,1))+ d.branch_code +"+
    		"b.asset_acq_ac ACCOUNT,'D' AS CR_DR,e.GROUP_ID,'' AS SBU_CODE,SUM(e.COST_PRICE) AS AMOUNT, "+
    		"'Group Posting for Batch_Id - '+convert(varchar ,e.GROUP_ID) AS NARRATION  from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c, am_gb_company b, "+
    		"FT_GROUP_DUE_PERIOD e where a.currency_id = c.currency_id and a.category_Code = e.CATEGORY_CODE and d.branch_id = e.branch_id and e.GROUP_ID = '"+batchId+"' AND e.STATUS = 'PAID' "+
    		"GROUP BY e.GROUP_ID,c.iso_code,a.Asset_Ledger,d.branch_code,b.asset_acq_ac)"+
    		"UNION "+
    		"select e.GROUP_ID AS asset_id,e.VENDOR_ACCT ACCOUNT,'C' AS CR_DR,e.GROUP_ID,'' AS SBU_CODE,SUM(e.COST_PRICE) AS AMOUNT, "+
    		"'Group Posting for Batch_Id - '+convert(varchar ,e.GROUP_ID) AS NARRATION from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b, "+
    		"FT_GROUP_DUE_PERIOD e where a.currency_id = c.currency_id and a.category_Code = e.CATEGORY_CODE and d.branch_id = e.branch_id "+
    		"and e.GROUP_ID = '"+batchId+"' AND e.STATUS = 'PAID' "+
    		"GROUP BY e.GROUP_ID,c.iso_code,a.Asset_Ledger,d.branch_code,e.VENDOR_ACCT";
	
Connection con = null;
PreparedStatement ps = null;
boolean done = false;
// System.out.println("insertBulkFleetUploadSummaryPostingEntries query: "+query);
try {
con = mgDbCon.getConnection("legendPlus");
ps = con.prepareStatement(query);

done = (ps.executeUpdate() != -1);
} catch (Exception ex) {
done = false;
System.out.println("Error insertBulkUploadPostingEntries() of BulkAssetUploadPostingsServlet -> " + ex.getMessage());
ex.printStackTrace();
} finally {
closeConnection(con, ps);
}
return done;
}

	public void DeletebulkFleetPostings(){
		String query_r ="delete from am_gb_bulkPostingEntries";
//		System.out.println(">>>>>===DeletebulkFleetPostings  query_r: "+query_r);
		Connection con = null;
		        PreparedStatement ps = null;
		try {
			con = mgDbCon.getConnection("legendPlus");
		ps = con.prepareStatement(query_r);
		           int i =ps.executeUpdate();
		        } catch (Exception ex) {
		  
		            System.out.println("BulkFleetUploadPostingsServlet: DeletebulkFleetPostings()>>>>>" + ex);
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
