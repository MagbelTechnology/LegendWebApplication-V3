package com.magbel.legend.servlet;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.util.ApplicationHelper;
import com.magbel.util.Cryptomanager;
import com.magbel.util.HtmlUtility;

import audit.AuditTrailGen;
//import javafx.scene.shape.Path;
import magma.AssetRecordsBean;

import javax.naming.Context;
import javax.naming.InitialContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import legend.admin.objects.Staffs;

import javax.sql.DataSource;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;

import static java.lang.System.out;

public class StaffUploadExecutionServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection con = null ;
        PreparedStatement ps = null;
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        String statusMessage = "";
		boolean updtst = false;


		ApprovalRecords aprecords = new ApprovalRecords();
		String userClass = (String)request.getSession().getAttribute("UserClass");
		int loginID;
		String loginId = (String) session.getAttribute("CurrentUser");
		String password0 = request.getParameter("password");
		
		if (loginId == null) {
			loginID = 0;
		} else {
			loginID = Integer.parseInt(loginId);
		}

		String branchcode = (String) session.getAttribute("UserCenter");
		if (branchcode == null) {
			branchcode = "not set";
		}

		 if (!userClass.equals("NULL") || userClass!=null){
		boolean tokenRequired =false;
		String ColQuery ="select *from am_gb_Staff_Upload where StaffId not in (select StaffId from am_gb_Staff)";
		  java.util.ArrayList list =getUploadStaffSqlRecords(ColQuery);
//			try {		

//				  System.out.println("list.size() ============================ "+list.size());
				  int listValue = list.size();
				for(int k=0;k<listValue;k++)
		     {
			System.out.println("K ============== "+k+"   list.size() =========== "+list.size());	
			legend.admin.objects.Staffs  staff = (legend.admin.objects.Staffs)list.get(k);    	 					
//			String buttSave = request.getParameter("buttSave");
			// String buttAssg = request.getParameter("buttAssg");
//	       String RowId = request.getParameter("userId");
//	       String RecordId = usr.getUserName();
			String staffId = staff.getStaffId();
			String fullname = staff.getFullName();
			String dept_Code = staff.getDeptCode();
			String branch_Code = staff.getBranchCode();
			String user_Id = staff.getCreatedBy();
			
			staff.setStaffId(staffId);
			staff.setFullName(fullname);
			staff.setDeptCode(dept_Code);
			staff.setBranchCode(branch_Code);
			staff.setCreatedBy(user_Id);
		
	       
         //  System.out.println("staffId ================= "+staffId+"  fullname: "+fullname+"  createdBy: "+user_Id);
				
           boolean value = StaffUpload(staff);

           if(value == true) {
        	   out.print("<script>alert('Upload Done Successfully!!!')</script>");
				out.print("<script>window.location = 'DocumentHelp.jsp?np=manageStaffsList'</script>");
           }
		     }
				
		 }
		 out.print("<script>alert('Upload unsuccessful..')</script>");
		out.print("<script>window.location = 'DocumentHelp.jsp?np=manageStaffsList'</script>");
		/*	}  catch (Throwable e) {
				e.printStackTrace();
				// statusMessage = "Ensure unique record entry";
				out.print("<script>alert('Ensure unique record entry.')</script>");
				System.err.print(e.getMessage());
				out.print("<script>window.location = 'DocumentHelp.jsp?np=systemUsers&userId="
						+ userId + "&PC=11'</script>");
			}	*/
	
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	 doPost(request, response);
    }
    
    
    public java.util.ArrayList getUploadStaffSqlRecords(String query)
    {
    	java.util.ArrayList _list = new java.util.ArrayList();
//         System.out.println("====query in getUploadUserSqlRecords-----> "+query);
    	Connection c = null;
    	ResultSet rs = null;
    	Statement s = null; 
    	 legend.admin.objects.Staffs staff = null;
    	try {
    		    c = getConnection();
    			s = c.createStatement();
    			rs = s.executeQuery(query);
    			while (rs.next())
    			   {				
                    String staffId = rs.getString("StaffId");
                    String fullName = rs.getString("Full_Name");
                    String dept_Code = rs.getString("dept_code");
                    String branch_Code = rs.getString("branch_code");
                    String userId = rs.getString("UserId");
                    
                    
                    staff = new legend.admin.objects.Staffs();
                    staff.setStaffId(staffId);
                    staff.setFullName(fullName);
                    staff.setDeptCode(dept_Code);
                    staff.setBranchCode(branch_Code);
                    staff.setCreatedBy(userId);
                  
                    _list.add(staff);				
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
    
    public boolean StaffUpload(Staffs staff) {

		String UPDATE_QUERY = "insert into am_gb_staff (StaffId, Full_Name, dept_code, branch_code, UserId) values (?, ?, ?, ?, ?)";

		String UPDATE_QUERY2 = "delete from am_gb_Staff_Upload ";
		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;

		try {
			con = getConnection();
			ps = con.prepareStatement(UPDATE_QUERY);
			ps.setString(1, staff.getStaffId());
			ps.setString(2, staff.getFullName());
			ps.setString(3, staff.getDeptCode());
			ps.setString(4, staff.getBranchCode());
			ps.setString(5, staff.getCreatedBy());
			done = ps.execute();
			//System.out.println("<<<<====Query: "+UPDATE_QUERY+"   done: "+done);
			closeConnection(con, ps);
			con = getConnection();
			ps = con.prepareStatement(UPDATE_QUERY2);
			done = (ps.executeUpdate() != -1);
			//System.out.println("<<<<====DELETE_QUERY in deleteUserUpload: "+UPDATE_QUERY2+"   done: "+done);
			closeConnection(con, ps);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(con, ps);
		}
		return done;
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
