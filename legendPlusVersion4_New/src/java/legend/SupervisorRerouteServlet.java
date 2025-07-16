/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package legend;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import javax.sql.DataSource;

import magma.BarCodeHistoryBean;
import magma.net.dao.MagmaDBConnection;
/**
 *
 * @author Olabo
 */
@SuppressWarnings("serial")
public class SupervisorRerouteServlet extends HttpServlet {
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
  
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    	PrintWriter out = response.getWriter();
    	HttpSession session=request.getSession();  
       String supervisor_id = request.getParameter("super_id");
       //System.out.println("<<<<< supervisor id: " + supervisor_id);
       
       if(supervisor_id != null) {
    	   try {
    	   String query = "select User_id from am_gb_user where user_name=?";
    	   con = getConnection();
   	    	ps = con.prepareStatement(query);
			ps.setString(1, supervisor_id);
			rs = ps.executeQuery();
			while(rs.next()) {
				String super_id = rs.getString(1);
				// System.out.println("<<<<< super_id: " + super_id);
				 session.setAttribute("supervisor_username", supervisor_id);
				 session.setAttribute("super_id", super_id);
				 response.sendRedirect("DocumentHelp.jsp?np=manageSupervisorRerouteList");
			}
				out.println("<script type=\"text/javascript\">");
			   out.println("alert('Incorrect Supervisor Id.');");
			   out.println("location='DocumentHelp.jsp?np=manageSupervisorReroute';");
			   out.println("</script>");
		} catch (SQLException e) {
			e.getMessage();
		}
   	
       }
    } 

   
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        doGet(request, response);
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
		//finally {
//			closeConnection(con);
//		}
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
