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

import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.util.*;
/**
 * Servlet implementation class RequisitionServlet
 */
public class BulkDisposalUploadRejectServlet extends HttpServlet {
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
    public BulkDisposalUploadRejectServlet()
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
		String groupid = request.getParameter("groupid");
		String pageName = request.getParameter("MenuPage");
//		System.out.println(">>>>>>>>>>> About Menu Page: "+pageName);
		
		HttpSession session = request.getSession();
		String userClass = (String) session.getAttribute("UserClass");
			boolean transfer = false;
		  try
		        {  
			  if (!userClass.equals("NULL") || userClass!=null){
				  String transId = htmlUtil.getCodeName("select transaction_id from am_asset_approval WHERE batch_id = ?",groupid);
				  System.out.println(">>>>>>>>>>> About to send Mail on the Batch "+transId);
			     DeletebulkUploadRejections(groupid);
			     if(transfer){
	                String msgText1 = "Your transaction for Bulk asset Upload with Batch ID " + groupid + " has been Rejected. The new Batch ID is " + groupid;
	                mail.sendMailTransactionInitiator(Integer.parseInt(transId), "Bulk Asset Upload", msgText1);
			     	}
			     if(!pageName.equalsIgnoreCase("groupAssetUploadPosting")){
//			    	 System.out.println(">>>>>>>>>>> About to Redisplay the Page "+groupid);
			    	 out.print("<script>alert('Assets with the Group Id "+groupid+" have been Rejected ')</script>");
	                out.print("<script>window.location='DocumentHelp.jsp?np=rptMenuAssetUpload&batchId="+groupid+"'</script>");
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
		String query1 ="UPDATE FT_FLEET_MASTER SET STATUS='ACTIVE' WHERE ASSET_ID = ?";
		String query2 ="update a SET a.ASSET_STATUS='ACTIVE',DATE_DISPOSED = NULL from AM_ASSET a,am_asset_disposal_Upload b where a.ASSET_ID = b.ASSET_ID and b.DISPOSAL_ID = ?";
		String query3 ="delete from AM_ASSETDISPOSAL where DISPOSAL_ID = ?";
		String query4 ="update am_asset_approval set process_status = 'R',Asset_Status = 'Rejected' where batch_id = ?";
		String query5 ="delete from AM_GROUP_DISPOSAL where DISPOSAL_ID = ?";
		String query6 ="delete from am_asset_disposal_Upload where DISPOSAL_ID = ?";
		String query7 ="update am_raisentry_post set entryPostFlag = 'R', GroupIdStatus = 'R' where id = ?";
		
		Connection con = null;
		        PreparedStatement ps = null;
		try {
			con = mgDbCon.getConnection("legendPlus");
				ps = con.prepareStatement(query1);
				ps.setString(1, batchId);
				int i =ps.executeUpdate();
		        } catch (Exception ex) {
		            System.out.println("BulkUploadRejectionServlet: DeletebulkUploadRejections() for FT_FLEET_MASTER>>>>>" + ex);
		        } finally {
		            closeConnection(con, ps);
		        }
		try {
			con = mgDbCon.getConnection("legendPlus");
				ps = con.prepareStatement(query2);
				ps.setString(1, batchId);
				int i =ps.executeUpdate();
		        } catch (Exception ex) {
		            System.out.println("BulkUploadRejectionServlet: DeletebulkUploadRejections() for AM_ASSET>>>>>" + ex);
		        } finally {
		            closeConnection(con, ps);
		        }		
		try { 
			con = mgDbCon.getConnection("legendPlus");
				ps = con.prepareStatement(query3);
				ps.setString(1, batchId);
				int i =ps.executeUpdate();
		        } catch (Exception ex) {
		            System.out.println("BulkUploadRejectionServlet: DeletebulkUploadRejections() for AM_ASSETDISPOSAL>>>>>" + ex);
		        } finally {
		            closeConnection(con, ps);
		        }		
		try {
			con = mgDbCon.getConnection("legendPlus");
				ps = con.prepareStatement(query4);
				ps.setString(1, batchId);
				int i =ps.executeUpdate();
		        } catch (Exception ex) {
		            System.out.println("BulkUploadRejectionServlet: DeletebulkUploadRejections() for am_asset_approval>>>>>" + ex);
		        } finally {
		            closeConnection(con, ps);
		        }	
		try {
			con = mgDbCon.getConnection("legendPlus");
				ps = con.prepareStatement(query5);
				ps.setString(1, batchId);
				int i =ps.executeUpdate();
		        } catch (Exception ex) {
		            System.out.println("BulkUploadRejectionServlet: DeletebulkUploadRejections() for AM_GROUP_DISPOSAL>>>>>" + ex);
		        } finally {
		            closeConnection(con, ps);
		        }	
		try {
			con = mgDbCon.getConnection("legendPlus");
				ps = con.prepareStatement(query6);
				ps.setString(1, batchId);
				int i =ps.executeUpdate();
		        } catch (Exception ex) {
		            System.out.println("BulkUploadRejectionServlet: DeletebulkUploadRejections() for am_asset_disposal_Upload>>>>>" + ex);
		        } finally {
		            closeConnection(con, ps);
		        }
		try {
			con = mgDbCon.getConnection("legendPlus");
				ps = con.prepareStatement(query7);
				ps.setString(1, batchId);
				int i =ps.executeUpdate();
		        } catch (Exception ex) {
		            System.out.println("BulkUploadRejectionServlet: DeletebulkUploadRejections() for am_raisentry_post>>>>>" + ex);
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
