package com.magbel.legend.servlet;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;

import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.util.ApplicationHelper;
import com.magbel.util.HtmlUtility;


//import javafx.scene.shape.Path;
import magma.AssetRecordsBean;

import javax.naming.Context;
import javax.naming.InitialContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

public class PpmDescriptionServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection con = null ;
        PreparedStatement ps = null;
        PrintWriter out = response.getWriter();
        HtmlUtility html = new HtmlUtility();
        EmailSmsServiceBus mail = new EmailSmsServiceBus();
        AssetRecordsBean ad = null;
		
		String userClass = (String) request.getSession().getAttribute("UserClass");
		 String userId = (String) request.getSession().getAttribute("CurrentUser");
//			System.out.println("<<<<<<=====userId: "+userId+"    userClass: "+userClass);
			
		 if (!userClass.equals("NULL") || userClass!=null){
		 String type = request.getParameter("type");
		 String description = request.getParameter("description");
		 String duration = request.getParameter("duration");
		
		 System.out.println("<<<<<<<<< type: " + type + " ======= description: " + description + " ======= duration: " + duration);
		 
		 	
		 
//		 System.out.println("<<<<<<< Script: " + script + " Reason: " + reason + " Confirm:" + confirm + 
//				 " Supervisor: " + supervisor_id + " Transaction Level: " + transaction_level + " Created Date: " + date);
          try{
        	 
           if(type!=null && description !=null && duration!=null) {
        	   String query = "insert into FM_PPM_TYPE (type, description, duration)values(?,?,?)";
        	   con = getConnection();
               ps = con.prepareStatement(query);
               ps.setString(1, type);
               ps.setString(2, description);
               ps.setString(3, duration);
               int i = ps.executeUpdate();
               if(i>0) {  
					out.println("<script type=\"text/javascript\">");
					   out.println("alert('Insertion Successful.');");
					   out.println("location='DocumentHelp.jsp?np=facility_ppmDescription';");
					   out.println("</script>");
					//out.print("<script>alert('Your request has been sent to "+supervisor_id+" for approval.')</script>");
					//response.sendRedirect("DocumentHelp.jsp?np=scriptExecution");
               }
           }
      	 
           
          }catch (Exception e){
              e.getCause();
          }finally {
        	  closeConnection(con, ps);
          }
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
