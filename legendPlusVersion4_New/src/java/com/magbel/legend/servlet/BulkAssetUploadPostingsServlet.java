package com.magbel.legend.servlet;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import magma.net.dao.MagmaDBConnection;

import com.magbel.util.*;
/**
 * Servlet implementation class RequisitionServlet
 */
public class BulkAssetUploadPostingsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;  
	MagmaDBConnection mgDbCon = null;
	ApplicationHelper applHelper = null;
    public BulkAssetUploadPostingsServlet()
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
			  	DeletebulktransferPostings();
			  	if((batch_Id!=null) || (batch_Id!="")){
			     java.util.ArrayList list =bulkUploadRecordforsReport(batch_Id);
			     insertBulkUploadSummaryPostingEntries(batch_Id);
			     
			     System.out.println("-->size>--> "+list.size()+"  Batch Id:  "+batch_Id);
			     for(int i=0;i<list.size();i++)
			     {
			    	 String assetId_batchNo=(String) list.get(i);
			    	 result = assetId_batchNo.split("#");
			    	 batchNo = result[0];
			    	 assetId = result[1];
			    	 if(!assetId.equalsIgnoreCase(""))
			    		 insertBulkUploadPostingEntries(assetId, batchNo);
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
					    		 insertBulkUploadPostingEntries(assetId, batchNo);
					     }		
				  	}			     
			     out.print("<script>window.location='DocumentHelp.jsp?np=rptMenuBulkPostings&batchId="+batch_Id+"&FromDate="+fromDate+"&ToDate="+toDate+"'</script>");
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
			String query = " SELECT  * from AM_GROUP_ASSET where Raise_entry = 'Y' and GROUP_ID = ?  ";
//			System.out.println("====bulkTransferRecordforsReport query-----> "+query);
		Connection c = null;
		ResultSet rs = null;
		PreparedStatement s = null;
//		Statement s = null;
	String result = "";
		try {
			   // c = getConnection();
			    c = mgDbCon.getConnection("legendPlus");
//				s = c.createStatement();
				s = c.prepareStatement(query);
				s.setString(1, batchId);
				rs = s.executeQuery();
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
			String query = " SELECT  * from AM_GROUP_ASSET where Posting_Date BETWEEN ? AND ? ";
//			System.out.println("====bulkTransferRecordforsReport query-----> "+query);
		Connection c = null;
		ResultSet rs = null;
//		Statement s = null;
		PreparedStatement s = null;
	String result = "";
		try {
			   // c = getConnection();
			    c = mgDbCon.getConnection("legendPlus");
//				s = c.createStatement();
//				rs = s.executeQuery(query);
				s = c.prepareStatement(query);
				s.setString(1, StartDate);
				s.setString(2, EndDate);
				rs = s.executeQuery();
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
		
	public boolean insertBulkUploadPostingEntries(String assetId, String batchId) {
		String query = "INSERT INTO am_gb_bulkPostingEntries (asset_id,Account,STATUS,BatchId,sbu_code,amount, narration)"+
	    		"(select e.asset_id,c.iso_code +(select accronym from am_ad_ledger_type where series = substring(a.Asset_Ledger,1,1))+ "+
	    		"d.branch_code +	b.asset_acq_ac ACCOUNT,'C' AS CR_DR,e.GROUP_ID,e.SBU_CODE AS SBU_CODE,e.COST_PRICE AS AMOUNT,"+
	    		"e.Description+'   '+e.ASSET_ID AS NARRATION  from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c, "+
	    		"am_gb_company b, AM_GROUP_ASSET e where a.currency_id = c.currency_id and a.category_ID = e.CATEGORY_ID "+
	    		"and d.branch_id = e.branch_id and e.GROUP_ID = ? and e.Asset_id = ? "+
	    		"UNION "+
	    		"select e.asset_id,c.iso_code +(select accronym from am_ad_ledger_type where series = substring(a.Asset_Ledger,1,1))+ "+
	    		"d.branch_code +	a.Asset_Ledger ACCOUNT,'D' AS CR_DR,e.GROUP_ID,e.SBU_CODE AS SBU_CODE,e.COST_PRICE AS AMOUNT, "+
	    		"e.Description+'   '+e.ASSET_ID AS NARRATION from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,  "+
	    		"am_gb_company b, AM_GROUP_ASSET e where a.currency_id = c.currency_id and a.category_ID = e.CATEGORY_ID "+
	    		"and d.branch_id = e.branch_id and e.GROUP_ID = ? and e.Asset_id = ?) ";
	Connection con = null;
	PreparedStatement ps = null;
	boolean done = false;
	 System.out.println("insertBulkTransferPostingEntries query: "+query);
	try {
	con = mgDbCon.getConnection("legendPlus");
	ps = con.prepareStatement(query);
	  ps.setString(1, batchId);
	  ps.setString(2, assetId);
	  ps.setString(3, batchId);
	  ps.setString(4, assetId);

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
	
public boolean insertBulkUploadSummaryPostingEntries(String batchId) {
	String query = "INSERT INTO am_gb_bulkPostingEntries (asset_id,Account,STATUS,BatchId,sbu_code,amount, narration)(select e.GROUP_ID,"+
    		"c.iso_code +(select accronym from am_ad_ledger_type where series = substring(a.Asset_Ledger,1,1))+ d.branch_code +"+
    		"b.asset_acq_ac ACCOUNT,'D' AS CR_DR,e.GROUP_ID,'' AS SBU_CODE,SUM(e.COST_PRICE) AS AMOUNT, "+
    		"'Group Posting for Batch_Id - '+convert(varchar ,e.GROUP_ID) AS NARRATION  from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c, am_gb_company b, "+
    		"AM_GROUP_ASSET e where a.currency_id = c.currency_id and a.category_ID = e.CATEGORY_ID and d.branch_id = e.branch_id and e.GROUP_ID = ? "+
    		"GROUP BY e.GROUP_ID,c.iso_code,a.Asset_Ledger,d.branch_code,b.asset_acq_ac)"+
    		"UNION "+
    		"select e.GROUP_ID AS asset_id,e.Vendor_AC ACCOUNT,'C' AS CR_DR,e.GROUP_ID,'' AS SBU_CODE,SUM(e.COST_PRICE) AS AMOUNT, "+
    		"'Group Posting for Batch_Id - '+convert(varchar ,e.GROUP_ID) AS NARRATION from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b, "+
    		"AM_GROUP_ASSET e where a.currency_id = c.currency_id and a.category_ID = e.CATEGORY_ID and d.branch_id = e.branch_id "+
    		"and e.GROUP_ID = ? "+
    		"GROUP BY e.GROUP_ID,c.iso_code,a.Asset_Ledger,d.branch_code,e.Vendor_AC";
	
Connection con = null;
PreparedStatement ps = null;
boolean done = false;
 System.out.println("insertBulkUploadSummaryPostingEntries query: "+query);
try {
con = mgDbCon.getConnection("legendPlus");
ps = con.prepareStatement(query);
ps.setString(1, batchId);
ps.setString(2, batchId);

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

	public void DeletebulktransferPostings(){
		String query_r ="delete from am_gb_bulkPostingEntries";
//		System.out.println(">>>>>===DeletebulktransferPostings  query_r: "+query_r);
		Connection con = null;
		        PreparedStatement ps = null;
		try {
			con = mgDbCon.getConnection("legendPlus");
		ps = con.prepareStatement(query_r);
		           int i =ps.executeUpdate();
		        } catch (Exception ex) {
		  
		            System.out.println("BulkAssetUploadPostingsServlet: DeletebulkUploadPostings()>>>>>" + ex);
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
