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
public class TransactionAuditLogServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;  
	MagmaDBConnection mgDbCon = null;
	ApplicationHelper applHelper = null;
    public TransactionAuditLogServlet()
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
		HttpSession session = request.getSession();

		String []result = null ;
			String batchNo = "";
			String assetId = "";
		  try
		        {  
		//	  	if((batch_Id!=null) || (batch_Id!="")){
		//	     java.util.ArrayList list =bulkTransferRecordforsReport(batch_Id);
			  insertTransactionLogEntries();

		   //     }
		     
			     out.print("<script>window.location='DocumentHelp.jsp?np=rptMenuTransLog'</script>");
		        }
		        catch(Exception e)
		        {
		            e.printStackTrace();
		        }
	//	    }
		
	}

		
	public boolean insertTransactionLogEntries() {
/*		String query = "DROP TABLE REPORT_TABLE "+
	    		"SELECT ReqnID,a.UserID,ReqnBranch,ReqnDepartment,workStationIP, ItemRequested, FAULT_ID,d.full_name AS RequestedBY, ReqnDate AS TRANS_DATE, 'PENDING'AS Approvalstatus,s.STATUS_NAME,c.User_Name AS Collector, "+
	    		"Quantity, Remark AS REMARKS,substring(supervisor,1,4) AS SUPERVISOR1,substring(supervisor,6,4)  AS SUPERVISOR2,substring(supervisor,11,4)  AS SUPERVISOR3,"+
	    		"substring(supervisor,16,4)  AS SUPERVISOR4,'                                           ' AS SUPERVISORNAME1,'                                                  ' AS SUPERVISORNAME2,'                             ' AS SUPERVISORNAME3, "+
	    		"'                                 ' AS SUPERVISORNAME4,e.DESCRIPTION,f.BRANCH_NAME,g.Dept_name, d.Full_Name AS POSTEDBY   INTO REPORT_TABLE "+
	    		"FROM am_ad_Requisition a, ST_INVENTORY_USERS c, am_gb_User d, ST_GB_STATUS s, ST_INVENTORY_ITEMS e, am_ad_branch f, am_ad_department g  "+
	    		"WHERE a.ReqnUserID != c.USER_CODE AND a.UserID = d.User_id AND s.STATUS_CODE = a.Status AND a.ItemRequested = e.ITEM_CODE AND a.ReqnBranch = f.BRANCH_ID AND a.ReqnDepartment = g.Dept_ID AND a.UserID = d.User_id "+
	    		"UPDATE REPORT_TABLE  SET REPORT_TABLE.SUPERVISORNAME1 = am_gb_User.Full_Name "+
	    		"FROM REPORT_TABLE  INNER JOIN  am_gb_User ON REPORT_TABLE.supervisor1 = am_gb_User.USER_ID "+
	    		"UPDATE REPORT_TABLE SET REPORT_TABLE.SUPERVISORNAME2 = am_gb_User.Full_Name "+
	    		"FROM REPORT_TABLE  INNER JOIN  am_gb_User ON REPORT_TABLE.supervisor2 = am_gb_User.USER_ID "+
	    		"UPDATE REPORT_TABLE SET REPORT_TABLE.SUPERVISORNAME3 = am_gb_User.Full_Name "+
	    		"FROM REPORT_TABLE  INNER JOIN  am_gb_User ON REPORT_TABLE.supervisor3 = am_gb_User.USER_ID "+
	    		"UPDATE REPORT_TABLE SET REPORT_TABLE.SUPERVISORNAME4 = am_gb_User.Full_Name "+
	    		"FROM REPORT_TABLE  INNER JOIN  am_gb_User ON REPORT_TABLE.supervisor4 = am_gb_User.USER_ID ";
	    		*/
		String query = "DROP TABLE REPORT_TABLE "+
	    		"SELECT ReqnID,a.UserID,ReqnBranch,ReqnDepartment,workStationIP, ItemRequested, FAULT_ID,d.full_name AS RequestedBY, ReqnDate AS TRANS_DATE, STATUS AS Approvalstatus,s.STATUS_NAME,c.User_Name AS Collector, "+
	    		"Quantity, Remark AS REMARKS,substring(supervisor,1,4) AS SUPERVISOR1,substring(supervisor,6,4)  AS SUPERVISOR2,substring(supervisor,11,4)  AS SUPERVISOR3,"+
	    		"substring(supervisor,16,4)  AS SUPERVISOR4,'                                           ' AS SUPERVISORNAME1,'                                                  ' AS SUPERVISORNAME2,'                             ' AS SUPERVISORNAME3, "+
	    		"'                                 ' AS SUPERVISORNAME4,e.DESCRIPTION,f.BRANCH_NAME, d.Full_Name AS POSTEDBY   INTO REPORT_TABLE "+
	    		"FROM am_ad_Requisition a, ST_INVENTORY_USERS c, am_gb_User d, ST_GB_STATUS s, ST_INVENTORY_ITEMS e, am_ad_branch f "+
	    		"WHERE a.ReqnUserID = c.USER_CODE AND a.UserID = d.User_id AND s.STATUS_CODE = a.Status AND a.ItemRequested = e.ITEM_CODE AND a.ReqnBranch = f.BRANCH_ID AND a.UserID = d.User_id "+
	    		"UPDATE REPORT_TABLE SET REPORT_TABLE.SUPERVISORNAME1 = am_gb_User.Full_Name FROM am_gb_User, REPORT_TABLE  "+
	    		"WHERE am_gb_User.user_id = REPORT_TABLE.SUPERVISOR1 "+
	    		"UPDATE REPORT_TABLE SET REPORT_TABLE.SUPERVISORNAME2 = am_gb_User.Full_Name FROM am_gb_User, REPORT_TABLE  "+
	    		"WHERE am_gb_User.user_id = REPORT_TABLE.SUPERVISOR2 "+
	    		"UPDATE REPORT_TABLE SET REPORT_TABLE.SUPERVISORNAME3 = am_gb_User.Full_Name FROM am_gb_User, REPORT_TABLE "+
	    		"WHERE am_gb_User.user_id = REPORT_TABLE.SUPERVISOR3 "+
	    		"UPDATE REPORT_TABLE SET REPORT_TABLE.SUPERVISORNAME4 = am_gb_User.Full_Name FROM am_gb_User, REPORT_TABLE "+
	    		"WHERE am_gb_User.user_id = REPORT_TABLE.SUPERVISOR4 "+
	    		"UPDATE REPORT_TABLE SET REPORT_TABLE.Approvalstatus = 'Pending' WHERE REPORT_TABLE.Approvalstatus != 'A' AND REPORT_TABLE.Approvalstatus != 'R' "+
	    		"UPDATE REPORT_TABLE SET REPORT_TABLE.Approvalstatus = 'Approved' WHERE REPORT_TABLE.Approvalstatus = 'A' "+
	    		"UPDATE REPORT_TABLE SET REPORT_TABLE.Approvalstatus = 'Rejected' WHERE REPORT_TABLE.Approvalstatus = 'R' ";		
	Connection con = null;
	PreparedStatement ps = null; 
	boolean done = false;
//	 System.out.println("insertTransactionLogEntries query: "+query);
	try {
	con = mgDbCon.getConnection("legendPlus");
	ps = con.prepareStatement(query);

	done = (ps.executeUpdate() != -1);
	} catch (Exception ex) {
	done = false;
	System.out.println("Error insertTransactionLogEntries() of TransactionAuditLogServlet -> " + ex.getMessage());
	ex.printStackTrace();
	} finally {
	closeConnection(con, ps);
	}
	return done;
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
