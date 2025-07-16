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
import magma.AssetRecordsBean;
import magma.net.dao.MagmaDBConnection;

import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.util.*;
/**
 * Servlet implementation class RequisitionServlet
 */
public class BulkUploadRejectionServlet extends HttpServlet {
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
	HtmlUtility htmlUtil =null;
    public BulkUploadRejectionServlet()
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
		PrintWriter out = response.getWriter();
		 EmailSmsServiceBus mail = new EmailSmsServiceBus();
		String groupid = request.getParameter("group_id");
		String pageName = request.getParameter("MenuPage");
		System.out.println(">>>>>>>>>>> About Menu Page: "+pageName+"    groupid: "+groupid);
		
		HttpSession session = request.getSession();
		String userClass = (String) session.getAttribute("UserClass");
			boolean transfer = false;
		  try
		        {  
			  if (!userClass.equals("NULL") || userClass!=null){
			     DeletebulkUploadRejections(groupid);
			     if(transfer){
	                System.out.println(">>>>>>>>>>> About to send Mail on the Batch "+groupid);
	                String msgText1 = "Your transaction for Bulk asset Upload with Batch ID " + groupid + " has been Rejected. The new Batch ID is " + groupid;
	                mail.sendMailTransactionInitiator(Integer.parseInt(groupid), "Bulk Asset Upload", msgText1);
			     	}
			     if(pageName.equalsIgnoreCase("groupAssetUploadPosting")){
	                out.print("<script>window.location='DocumentHelp.jsp?np=raiseEntry&tranType=Asset Upload'</script>");
			     }else{
			    	 out.print("<script>window.location='DocumentHelp.jsp?np=raiseEntry'</script>");
			     }
		        }
		        }
		        catch(Exception e)
		        {
		            e.printStackTrace();
		        }
	//	    }
		
	}
	
	public void DeletebulkUploadRejections(String batchId){
//		String query1 ="delete from am_asset where GROUP_ID = ?";
//		String query2 ="delete from am_group_asset where GROUP_ID = ?";
//		String query3 ="delete from am_group_asset_main where GROUP_ID = ?";
//		String query4 ="delete from am_asset_approval where batch_id = ?";
		
        String qa1 = "update am_asset SET ASSET_STATUS = 'REJECTED' where GROUP_ID = '"+batchId+"'";
        ad.updateAssetStatusChange(qa1);	 
        String qa2 = "update am_group_asset SET ASSET_STATUS = 'REJECTED' where GROUP_ID = '"+batchId+"'";
        ad.updateAssetStatusChange(qa2);	 		
        String qa3 = "update am_asset_approval SET ASSET_STATUS = 'REJECTED',PROCESS_STATUS = 'R' where Asset_Id = '"+batchId+"'";
        ad.updateAssetStatusChange(qa3);	 
//        String qa4 = "update am_group_asset_main SET ASSET_STATUS = 'REJECTED' where GROUP_ID = '"+batchId+"'";
//        ad.updateAssetStatusChange(qa4);	
		
//		Connection con = null;
//		        PreparedStatement ps = null;
//		try {
//			con = mgDbCon.getConnection("legendPlus");
//				ps = con.prepareStatement(query1);
//				ps.setString(1, batchId);
//				int i =ps.executeUpdate();
//		        } catch (Exception ex) {
//		            System.out.println("BulkUploadRejectionServlet: DeletebulkUploadRejections() for am_asset>>>>>" + ex);
//		        } finally {
//		            closeConnection(con, ps);
//		        }
//		try {
//			con = mgDbCon.getConnection("legendPlus");
//				ps = con.prepareStatement(query2);
//				ps.setString(1, batchId);
//				int i =ps.executeUpdate();
//		        } catch (Exception ex) {
//		            System.out.println("BulkUploadRejectionServlet: DeletebulkUploadRejections() for am_group_asset>>>>>" + ex);
//		        } finally {
//		            closeConnection(con, ps);
//		        }		
//		try {
//			con = mgDbCon.getConnection("legendPlus");
//				ps = con.prepareStatement(query3);
//				ps.setString(1, batchId);
//				int i =ps.executeUpdate();
//		        } catch (Exception ex) {
//		            System.out.println("BulkUploadRejectionServlet: DeletebulkUploadRejections() for am_group_asset_main>>>>>" + ex);
//		        } finally {
//		            closeConnection(con, ps);
//		        }		
//		try {
//			con = mgDbCon.getConnection("legendPlus");
//				ps = con.prepareStatement(query4);
//				ps.setString(1, batchId);
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
