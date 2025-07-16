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
import legend.admin.objects.Mobiles;
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

public class MobileRegistrationUploadExecutionServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Hello from MobileRegistrationUploadExecutionServlet!!!!! ");
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
		
		System.out.println("userClass value: " + userClass);
		System.out.println("loginId value: " + loginId);

		 if (!userClass.equals("NULL") || userClass!=null){
		boolean tokenRequired =false;
		String ColQuery ="select * from AM_GB_REGMAC_UPLOAD where USER_NAME not in (select USER_NAME from AM_GB_REGMAC)";
		  java.util.ArrayList list =getUploadMobileSqlRecords(ColQuery);
//			try {		

				  System.out.println("list.size() ============================ "+list.size());
				  int listValue = list.size();
				for(int k=0;k<listValue;k++)
		     {
			System.out.println("K ============== "+k+"   list.size() =========== "+list.size());	
			legend.admin.objects.Mobiles  mobile = (legend.admin.objects.Mobiles)list.get(k);    	 					
//			String buttSave = request.getParameter("buttSave");
			// String buttAssg = request.getParameter("buttAssg");
//	       String RowId = request.getParameter("userId");
//	       String RecordId = usr.getUserName();
			
			 String userName = mobile.getUserName();
				String macAddress = mobile.getMacAddress();
			   String userStatus = mobile.getMobileStatus();
			   String createDate = mobile.getCreateDate();
			   String userId = mobile.getUserId();
			
			   mobile.setUserName(userName);
 			   	mobile.setMacAddress(macAddress);
 			   	mobile.setMobileStatus(userStatus);
 			   	mobile.setCreateDate(createDate);
 			   	mobile.setUserId(userId);
		
	       
         //  System.out.println("staffId ================= "+staffId+"  fullname: "+fullname+"  createdBy: "+user_Id);
				
           boolean value = MobileUpload(mobile);

           if(value == true) {
        	   out.print("<script>alert('Upload Done Successfully!!!')</script>");
				out.print("<script>window.location = 'DocumentHelp.jsp?np=manageMobiles&status=ACTIVE'</script>");
           }
		     }
				
		 }
		 out.print("<script>alert('Upload unsuccessful..')</script>");
		out.print("<script>window.location = 'DocumentHelp.jsp?np=manageMobiles&status=ACTIVE'</script>");
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
    
    
    public java.util.ArrayList getUploadMobileSqlRecords(String query)
    {
    	java.util.ArrayList _list = new java.util.ArrayList();
//         System.out.println("====query in getUploadUserSqlRecords-----> "+query);
    	Connection c = null;
    	ResultSet rs = null;
    	Statement s = null; 
    	 legend.admin.objects.Mobiles mobile = null;
    	try {
    		    c = getConnection();
    			s = c.createStatement();
    			rs = s.executeQuery(query);
    			while (rs.next())
    			   {				
    				  String userName = rs.getString("USER_NAME");
    	  				String macAddress = rs.getString("MAC_ADDRESS");
    	  			   String userStatus = rs.getString("STATUS");
    	  			   String createDate = rs.getString("CREATE_DATE");
    	  			   String userId = rs.getString("USER_ID");
                    
                    
                    mobile = new legend.admin.objects.Mobiles();
                	mobile.setUserName(userName);
      			   	mobile.setMacAddress(macAddress);
      			   	mobile.setMobileStatus(userStatus);
      			   	mobile.setCreateDate(createDate);
      			   	mobile.setUserId(userId);
                  
                    _list.add(mobile);				
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
    
    public boolean MobileUpload(Mobiles mobile) {

		String UPDATE_QUERY = "insert into AM_GB_REGMAC (USER_NAME, MAC_ADDRESS, STATUS, CREATE_DATE, USER_ID) values (?, ?, ?, ?, ?)";

		String UPDATE_QUERY2 = "delete from AM_GB_REGMAC_UPLOAD ";
		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;

		try {
			con = getConnection();
			ps = con.prepareStatement(UPDATE_QUERY);
			ps.setString(1, mobile.getUserName());
			ps.setString(2, mobile.getMacAddress());
			ps.setString(3, mobile.getMobileStatus());
			ps.setString(4, mobile.getCreateDate());
			ps.setString(5, mobile.getUserId());
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
