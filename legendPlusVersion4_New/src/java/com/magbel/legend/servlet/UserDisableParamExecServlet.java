package com.magbel.legend.servlet;


import legend.admin.handlers.SecurityHandler;
import legend.admin.objects.UserDisableClass;
import javax.naming.Context;
import javax.naming.InitialContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.magbel.util.DatetimeFormat;

import java.io.*; 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UserDisableParamExecServlet extends HttpServlet {
    /**
	 * 
	 */
	private SimpleDateFormat sdf;
	private DatetimeFormat df;
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    response.setContentType("text/html");
	    PrintWriter out = response.getWriter();

	    Connection con = null;
	    PreparedStatement ps = null;
	    PreparedStatement ps1 = null;
	    PreparedStatement ps2 = null;

	    try {
	        con = getConnection();
	        con.setAutoCommit(false); 

	        String deleteQuery = "DELETE FROM am_gb_classEnable";
	        ps = con.prepareStatement(deleteQuery);
	        ps.executeUpdate();

	        System.out.println("Entering Insertion Script");
	        String insertQuery = "INSERT INTO am_gb_classEnable (class_id, class_desc, class_name, User_Id, class_status, create_date) " +
	                             "SELECT a.class_id, a.class_desc, a.class_name, b.User_Id, 'N', ? " +
	                             "FROM am_gb_classDisable a " +
	                             "INNER JOIN am_gb_user b ON b.Class = a.class_id " +  
	                             "WHERE a.class_status = 'Y'";

	        ps1 = con.prepareStatement(insertQuery);
	        java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
	        ps1.setDate(1, sqlDate);
	        int i = ps1.executeUpdate();
	       // System.out.println("Insertion Script Done");
	        
	        if (i > 0) {
	        	System.out.println("Entering Update Script");
	            String updateQuery = "UPDATE b SET b.class = a.DefaultClass_Id " +
	                                 "FROM am_gb_classDisable a, am_gb_user b " +
	                                 "WHERE a.Class_Id = b.Class AND a.class_status = 'Y'";
	            ps2 = con.prepareStatement(updateQuery);
	            int x = ps2.executeUpdate();
	            if(x > 0) {
	            	// System.out.println("Update Script Done");
	            	 con.commit(); 
	     	       out.println("<script type=\"text/javascript\">");
		            out.println("alert('Successfully Disabled Selected Users.');");
		            out.println("location='DocumentHelp.jsp?np=userDisableParam';");
		            out.println("</script>");
	            }
	           
	        }

	       

	    } catch (Exception ex) {
	        ex.printStackTrace();
	        if (con != null) {
	            try {
	                con.rollback(); 
	            } catch (SQLException rollbackEx) {
	                rollbackEx.printStackTrace();
	            }
	        }
	        
	        String errorMsg = ex.getMessage().replace("'", "\\'");
	        out.println("<script type='text/javascript'>alert('Error occurred: " + errorMsg + "');</script>");
	    } finally {
	        closeConnection(null, ps2, null);
	        closeConnection(null, ps1, null);
	        closeConnection(con, ps, null); 
	    }
	}

	
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	 doPost(request, response);
    }
    
    public boolean updateDisableClass(ArrayList list, int userid, String branchCode, int loginId, String eff_date)
    {
        for(int i = 0; i < list.size(); i++)
        {
        	
            UserDisableClass dc = (UserDisableClass)list.get(i);
            updateDisableClass(dc, userid, branchCode, loginId, eff_date);
        }
   //     System.out.println("Nos of rtecords updated = " + list.size());
 //       System.out.println((new StringBuilder()).append("Nos of rtecords updated = ").append(list.size()).toString());
        return true;
    }

    public void updateDisableClass(UserDisableClass dc, int userid, String branchCode, int loginId, String eff_date)
    {
        String query;
        Connection con;
        PreparedStatement ps;
        SecurityHandler sh = new SecurityHandler();
        query = "update am_gb_classDisable set class_status=?, DefaultClass_id=? where class_id=?";
        con = null;
        ps = null;
        try {
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, dc.getClassStatus());
        ps.setString(2, dc.getDefaultClassId());
        ps.setString(3, dc.getClassId());
        ps.executeUpdate();
        
        String id = dc.getDefaultClassId();
       // System.out.println("\n\n >>>>>>>>> id: " + id);
        String description = getDisableDescription(id);
       // System.out.println("\n\n >>>>>>>>> description: " + description);
        
        sh.compareAuditValues(dc.getClassStatus(), dc.getDefaultClassId(), dc.getClassId(), String.valueOf(userid), branchCode, loginId, eff_date, description);

      //  System.out.println("\n\n >>>>>>>>>>>>>>>>>> ps.execute() " + ps.executeUpdate());

        }
        catch (Exception ex) {
        System.out.println("WARN: Error doing updateClassPrivilege ->" + ex);
    } finally {
        closeConnection(con, ps);
    }
}
    
    public String getDisableDescription(String classId) {
    	String result = "";
    	Connection con = null ;
        PreparedStatement ps = null;
        ResultSet rs = null;
    	
    	try {
    		String query = "select class_desc from am_gb_classDisable where class_id=?";
        	con = getConnection();
        	ps = con.prepareStatement(query);
        	ps.setString(1, classId);
        	rs = ps.executeQuery();
        	while(rs.next()) {
        		result = rs.getString("class_desc");
        	}
    	}catch(Exception e) {
    		e.getMessage();
    	}finally {
    		closeConnection(con, ps, rs);
    	}
    	
    	return result;
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
