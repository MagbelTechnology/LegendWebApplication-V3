package com.magbel.legend.servlet;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;

import com.magbel.util.HtmlUtility;

import magma.AssetRecordsBean;
import magma.ScriptExecutionBeans;
import magma.net.dao.MagmaDBConnection;

import javax.naming.Context;
import javax.naming.InitialContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import javax.sql.DataSource;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import static java.lang.System.out;

public class ScriptExecutionApprovalServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection con = null ;
        PreparedStatement ps = null;
        Random rand = new Random();
        HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		MagmaDBConnection dbConnection = new MagmaDBConnection();
		HtmlUtility html = new HtmlUtility();
		Timestamp approveddate =  dbConnection.getDateTime(new java.util.Date());
		 String userid = (String) session.getAttribute("CurrentUser");
		 String script = request.getParameter("script");
		 String reason = request.getParameter("reason");
		 String confirm = request.getParameter("confirm");
		 String supervisor_id = request.getParameter("supervisor");
		 String transaction_level = request.getParameter("transaction_level");
		 String userId = request.getParameter("user_id");
		 String astatus = request.getParameter("astatus");
		 String rr = request.getParameter("reject_reason");
		 int tranId = Integer.parseInt(request.getParameter("tranId"));
		 String idNo = request.getParameter("id");
//		 int id = Integer.parseInt(idNo.substring(1));
//		 int id = Integer.parseInt(request.getParameter("id"));
		 String id = request.getParameter("id");
		 String buttSave = request.getParameter("buttSave");  
		 
//		 System.out.println("<<<<<<< Script: " + script + " Reason: " + reason + " Confirm:" + confirm + 
//				 " userId: " + userId + " Transaction Level: " + transaction_level);
		 System.out.print("=======astatus: "+astatus+"   =====rr: "+rr+"    tranId: "+tranId+"  id:"+id+"  buttSave: "+buttSave + " idNo: " + idNo);
       
		 try { 
			 AssetRecordsBean arb = new AssetRecordsBean();
				if (buttSave != null && astatus.equalsIgnoreCase("A") && !astatus.equalsIgnoreCase("R")) {
//					String q = "update am_asset_approval set process_status='A', asset_status='APPROVED',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
//					String a = "update am_gb_script set Status='APPROVED', Approval_Date='" +approveddate+"' where id=" + id;
//					 //arb.scriptProcessing(script);  updateAssetStatusChange
					String d = "DELETE FROM am_gb_script WHERE id = '"+tranId+"' and supervior_id != '"+userid+"' and confirm = 'Y'";
					html.updateAssetStatusChange(d);
					arb.deleteOtherSupervisors(id, userid);
					html.updateAssetStatusChange(script);
//					 arb.updateAssetStatusChange(q);
//					 arb.updateAssetStatusChange(a);
						String q = "update am_asset_approval set process_status='A', asset_status='APPROVED',DATE_APPROVED = ? where transaction_id=?";
						//String a = "update am_gb_script set Status='APPROVED', Approval_Date=? where id=?";
						String a = "update am_gb_script set Status='APPROVED', Approval_Date='"+approveddate+"' where id='"+id+"'";
						 String pq = "update am_asset_approval set process_status='PR', asset_status='PREVIOUS REJECTED' where transaction_id!='"+tranId+"' and Asset_id = '"+id+"' and Tran_Type = 'Script Execution' ";
//						 System.out.println("<<<<<<=====Q: "+q);
//						 System.out.println("<<<<<<=====A: "+a);
//						 System.out.println("<<<<<<=====pq: "+pq);
						 html.updateAssetStatusChange(q, approveddate, tranId);
						 html.updateAssetStatusChange(a);
						 html.updateAssetStatusChange(pq);
					  
					 
					 out.print("<script>alert('Record Successfully Approved')</script>");
					out.print("<script>window.location = 'DocumentHelp.jsp?np=transactionForApprovalList'</script>");
				}
				if (buttSave != null && astatus.equalsIgnoreCase("R")) {
					
					String d = "DELETE FROM am_gb_script WHERE id = '"+tranId+"' and supervior_id != '"+userid+"' and confirm = 'Y'";
					html.updateAssetStatusChange(d);
					arb.deleteOtherSupervisors(id, userid);
//					 String q = "update am_asset_approval set process_status='R', asset_status='REJECTED', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
//					 String r = "update am_gb_script set Status='REJECTED', Approval_Date='" +approveddate+"' where id=" + id;
//					 arb.updateAssetStatusChange(q);
//					 arb.updateAssetStatusChange(r);
					 String q = "update am_asset_approval set process_status='R', asset_status='REJECTED', reject_reason=?,DATE_APPROVED = ? where transaction_id=?";
					 String pq = "update am_asset_approval set process_status='PR', asset_status='PREVIOUS REJECTED' where transaction_id!='"+tranId+"' and Asset_id = '"+id+"' and Tran_Type = 'Script Execution' ";
					 String r = "update am_gb_script set Status='REJECTED', Approval_Date='"+approveddate+"' where id='"+id+"'";
					 html.updateAssetStatusChange(q,rr, approveddate, tranId);
					 html.updateAssetStatusChange(pq);
					 html.updateAssetStatusChange(r);				 
					out.print("<script>alert('Rejection Successfull')</script>");
					out.print("<script>window.location = 'DocumentHelp.jsp?np=transactionForApprovalList'</script>");
				}
		 }catch(Exception e) {
			 e.getMessage();
		 }
	
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doPost(request, response);
    }
    
    private Connection getConnection() {
		Connection con = null;
		try {
//        	if(con==null){
                Context initContext = new InitialContext();
                String dsJndi = "java:/legendPlus";
                DataSource ds = (DataSource) initContext.lookup(
                		dsJndi);
                con = ds.getConnection();
//        	}
		} catch (Exception e) {
			System.out.println("WARNING: Error 1 getting connection ->"
					+ e.getMessage());
		}
		
		return con;
    }
    
    private void closeConnection(Connection con, Statement s)
    {
        try
        {
            if(s != null)
            {
                s.close();
            }
            if(con != null)
            {
                con.close();
            }
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARNING: Error getting connection ->").append(e.getMessage()).toString());
        }
    }

    private void closeConnection(Connection con, PreparedStatement ps)
    {
        try
        {
            if(ps != null)
            {
                ps.close();
            }
            if(con != null)
            {
                con.close();
            }
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARNING: Error closing connection ->").append(e.getMessage()).toString());
        }
    }

    private void closeConnection(Connection con, Statement s, ResultSet rs)
    {
        try
        {
            if(rs != null)
            {
                rs.close();
            }
            if(s != null)
            {
                s.close();
            }
            if(con != null)
            {
                con.close();
            }
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARNING: Error closing connection ->").append(e.getMessage()).toString());
        }
    }

    private void closeConnection(Connection con, PreparedStatement ps, ResultSet rs)
    {
        try
        {
            if(rs != null)
            {
                rs.close();
            }
            if(ps != null)
            {
                ps.close();
            }
            if(con != null)
            {
                con.close();
            }
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARNING: Error closing connection ->").append(e.getMessage()).toString());
        }
    }

}
