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

import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.util.*;
/**
 * Servlet implementation class RequisitionServlet
 */
public class BulkAssetTransferRejectionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;  
	MagmaDBConnection mgDbCon = null;
	ApplicationHelper applHelper = null;
	HtmlUtility htmlUtil =null;
    public BulkAssetTransferRejectionServlet()
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
		htmlUtil = new HtmlUtility();
		PrintWriter out = response.getWriter();
		 EmailSmsServiceBus mail = new EmailSmsServiceBus();
		String batch_Id = request.getParameter("group_id");
		System.out.println("-->Batch Id:  "+batch_Id);
		String reject_reason = request.getParameter("reject_reason"); 
		HttpSession session = request.getSession();
		String userClass = (String) session.getAttribute("UserClass");
		String []result = null ;
			String batchNo = "";
			String assetId = "";
			String branchid = "";
			String deptid = "";
			String sectionid = "";
			String sbucode = "";
			String branchcode = "";
			String deptcode = "";
			String sectioncode = "";
			int assetcode = 0;
			boolean transfer = false;
		  try
		        {  
			
				if (!userClass.equals("NULL") || userClass!=null){
			     java.util.ArrayList list =bulkTransferRecordforsRejection(batch_Id);
			     
			     System.out.println("-->size>--> "+list.size()+"  Batch Id:  "+batch_Id);
			     for(int i=0;i<list.size();i++)
			     {
			    	 String assetId_batchNo=(String) list.get(i);
			    	 result = assetId_batchNo.split("#");
			    	 batchNo = result[0];
			    	 assetId = result[1];
			    	 branchid = result[2];
			    	 deptid = result[3];
			    	 sectionid = result[4];
			    	 sbucode = result[5];
			    	 assetcode = Integer.parseInt(result[6]);
			    	 branchcode = htmlUtil.findObject("SELECT BRANCH_CODE FROM am_ad_branch WHERE branch_id = "+branchid+"");
			    	 deptcode = htmlUtil.findObject("SELECT DEPT_CODE FROM am_ad_department WHERE dept_id = "+deptid+"");
			    	 sectioncode = htmlUtil.findObject("SELECT SECTION_CODE FROM am_ad_section WHERE Section_id = "+sectionid+"");
			    	 if(sectioncode =="UNKNOWN"){sectioncode = "008";}
			    	 if(!assetId.equalsIgnoreCase(""))
			    		 transfer = insertBulkTransferRejectEntries(assetId, batchNo,branchid,deptid,sectionid,sbucode,branchcode,deptcode,sectioncode,assetcode,reject_reason);
//			    	 	 DeletebulktransferRejections(batchNo);
			     }
			     if(transfer){
	             //   System.out.println(">>>>>>>>>>> About to send Mail on the Batch ");
	                String msgText1 = "Your transaction for Bulk asset transfer with Batch ID " + batchNo + " has been Rejected. The new Batch ID is " + batchNo;
	                mail.sendMailTransactionInitiator(Integer.parseInt(batchNo), "Bulk Asset Transfer", msgText1);
			     	}
	                out.print("<script>window.location='DocumentHelp.jsp?np=raiseEntry&tranType=Bulk Asset Transfer'</script>");
		        }
		        }
		        catch(Exception e)
		        {
		            e.printStackTrace();
		        }
	//	    }
		
	}

	public java.util.ArrayList bulkTransferRecordforsRejection(String batchId)
	{
		java.util.ArrayList _list = new java.util.ArrayList();
		String finacleTransId= null;
		System.out.println("====bulkTransferRecordforsReport batchId-----> "+batchId);
			String query = " SELECT  * from am_gb_bulkTransfer where batch_id = '"+batchId+"' ";
//			System.out.println("====bulkTransferRecordforsReport query-----> "+query);
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
					String batch_Id = rs.getString("Batch_id");
					String asset_id = rs.getString("Asset_id");
					String oldbranchId = rs.getString("oldbranch_id");
					String olddeptId = rs.getString("olddept_id");
					String oldsectionid = rs.getString("oldsection_id");
					String oldsbucode = rs.getString("oldSBU_CODE");
					int assetcode = rs.getInt("ASSET_CODE");
					result = batch_Id + "#"+asset_id + "#"+oldbranchId + "#"+olddeptId + "#"+oldsectionid + "#"+oldsbucode + "#"+assetcode;
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
	
	public boolean insertBulkTransferRejectEntries(String assetId, String batchId,String branchid,String deptid,String sectionid,String sbucode,
			String branchcode,String deptcode,String sectioncode,int assetcode,String reject_reason) {
//		String query = "UPDATE AM_ASSET SET ASSET_ID = '"+assetId+"', Branch_ID = "+branchid+", Dept_ID = "+deptid+", "+
//				"Section_id = "+sectionid+", SBU_CODE = '"+sbucode+"', BRANCH_CODE = '"+branchcode+"', DEPT_CODE = '"+deptcode+"', SECTION_CODE = '"+sectioncode+"' "+
//				"where ASSET_CODE = "+assetcode+" ";
		System.out.println("assetId:  "+assetId+"  batchId: "+batchId+"    reject_reason: "+reject_reason);
		String query = "UPDATE am_gb_bulkTransfer set STATUS = 'REJECTED' WHERE BATCH_ID = '"+batchId+"' ";
		String query2 = "UPDATE am_asset_approval SET PROCESS_STATUS = 'R', ASSET_STATUS = 'REJECTED',reject_reason = '"+reject_reason+"' WHERE BATCH_ID = '"+batchId+"' ";
		String query3 = "UPDATE am_raisentry_post set entryPostFlag = 'P' , GroupIdStatus = 'P' WHERE TRANS_ID = '"+batchId+"'";
	Connection con = null;
	PreparedStatement ps = null;
	PreparedStatement ps2 = null;
	PreparedStatement ps3 = null;
	boolean done = false;
	 System.out.println("insertBulkTransferRejectEntries query: "+query);
	 System.out.println("insertBulkTransferRejectEntries query2: "+query2);
	 System.out.println("insertBulkTransferRejectEntries query3: "+query3);
	try {
	con = mgDbCon.getConnection("legendPlus");
	ps = con.prepareStatement(query);
	done = (ps.executeUpdate() != -1);
	
	ps2 = con.prepareStatement(query2);
	done = (ps2.executeUpdate() != -1);
	
	ps3 = con.prepareStatement(query3);
	done = (ps3.executeUpdate() != -1);	
	} catch (Exception ex) {
	done = false;
	System.out.println("Error insertBulkTransferRejectEntries() of BulkAssetTransferRejectionServlet -> " + ex.getMessage());
	ex.printStackTrace();
	} finally {
	closeConnection(con, ps);
	closeConnection(con, ps2);
	closeConnection(con, ps3);
	}
	return done;
	}
	public void DeletebulktransferRejections(String batchId){
		String query_r ="delete from am_gb_bulkTransfer where Batch_id = "+batchId+"";

		Connection con = null;
		        PreparedStatement ps = null;
		try {
			con = mgDbCon.getConnection("legendPlus");
		ps = con.prepareStatement(query_r);
		           int i =ps.executeUpdate();
		        } catch (Exception ex) {
		  
		            System.out.println("BulkAssetTransferRejectionServlet: DeletebulktransferRejections()>>>>>" + ex);
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
