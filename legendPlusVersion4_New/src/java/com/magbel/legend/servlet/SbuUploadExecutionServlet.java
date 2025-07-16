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
import magma.SbuExcelUploadBean;

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

public class SbuUploadExecutionServlet extends HttpServlet {
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
		String ColQuery ="select *from Sbu_SetUp_Upload where Sbu_code not in (select Sbu_code from Sbu_SetUp)";
		  java.util.ArrayList list =getUploadSbuSqlRecords(ColQuery);
//			try {		

//				  System.out.println("list.size() ============================ "+list.size());
				  int listValue = list.size();
				for(int k=0;k<listValue;k++)
		     {
			System.out.println("K ============== "+k+"   list.size() =========== "+list.size());	
			SbuExcelUploadBean  sbuDetails = (SbuExcelUploadBean)list.get(k);    	 					
//			String buttSave = request.getParameter("buttSave");
			// String buttAssg = request.getParameter("buttAssg");
//	       String RowId = request.getParameter("userId");
//	       String RecordId = usr.getUserName();
			String Sbu_Code = sbuDetails.getSbu_code();
			String Sbu_Name = sbuDetails.getDescription();
			String Sbu_Contact = sbuDetails.getRegistration_no();
			String Status = sbuDetails.getTranType();
			String Contact_Email = sbuDetails.getBar_code();
			
			sbuDetails.setBar_code(Sbu_Code);
			sbuDetails.setDescription(Sbu_Name);
			sbuDetails.setRegistration_no(Sbu_Contact);
			sbuDetails.setTranType(Status);
			sbuDetails.setBar_code(Contact_Email);
		
	       
         //  System.out.println("staffId ================= "+staffId+"  fullname: "+fullname+"  createdBy: "+user_Id);
				
           boolean value = SbuUpload(sbuDetails);

           if(value == true) {
        	   out.print("<script>alert('Upload  Successful!!!')</script>");
				out.print("<script>window.location = 'DocumentHelp.jsp?np=SbumanageBranches'</script>");
           }
		     }
				
		 }
		 out.print("<script>alert('Upload unsuccessful..')</script>");
		out.print("<script>window.location = 'DocumentHelp.jsp?np=SbumanageBranches'</script>");
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
    
    
    public java.util.ArrayList getUploadSbuSqlRecords(String query)
    {
    	java.util.ArrayList _list = new java.util.ArrayList();
//         System.out.println("====query in getUploadUserSqlRecords-----> "+query);
    	Connection c = null;
    	ResultSet rs = null;
    	Statement s = null; 
    	SbuExcelUploadBean sbu = null;
    	try {
    		    c = getConnection();
    			s = c.createStatement();
    			rs = s.executeQuery(query);
    			while (rs.next())
    			   {				
                    String sbu_Code = rs.getString("Sbu_code");
                    String sbu_Name = rs.getString("Sbu_name");
                    String sbu_Contact = rs.getString("Sbu_contact");
                    String status = rs.getString("status");
                    String contact_Email = rs.getString("Contact_email");
                    
                    
                    sbu = new SbuExcelUploadBean();
                    sbu.setSbu_code(sbu_Code);
                    sbu.setDescription(sbu_Name);
                    sbu.setRegistration_no(sbu_Contact);
                    sbu.setTranType(status);
                    sbu.setBar_code(contact_Email);
                  
                    _list.add(sbu);				
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
    
    public boolean SbuUpload(SbuExcelUploadBean sbu) {

		String UPDATE_QUERY = "insert into Sbu_SetUp (Sbu_code, Sbu_name, Sbu_contact, status, Contact_email) values (?, ?, ?, ?, ?)";

		String UPDATE_QUERY2 = "delete from Sbu_SetUp_Upload ";
		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;

		try {
			con = getConnection();
			ps = con.prepareStatement(UPDATE_QUERY);
			ps.setString(1, sbu.getSbu_code());
			ps.setString(2, sbu.getDescription());
			ps.setString(3, sbu.getRegistration_no());
			ps.setString(4, sbu.getTranType());
			ps.setString(5, sbu.getBar_code());
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
