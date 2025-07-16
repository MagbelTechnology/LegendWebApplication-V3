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
//		Connection con = null ;
//        PreparedStatement ps = null;
        PrintWriter out = response.getWriter();
        int userID;
//        String userClass = (String) request.getSession().getAttribute("UserClass");
//        String branchId = request.getParameter("branchId");
//		 String userId = (String) request.getSession().getAttribute("CurrentUser");
//		 String processingDate = request.getParameter("bankProcessingDate");
//		 String [] status = request.getParameterValues("status");
//		 String class_id="";
//		 String class_desc="";
//		 String report_type="";
//		 String Status="";
        sdf = new java.text.SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        df = new DatetimeFormat();
		 String procDate = sdf.format(new java.util.Date());
	 
		//	System.out.println("<<<<<<=====userId: "+userId+"    userClass: "+userClass + " branchId: " + branchId);
			
		    Connection con;
		    ResultSet rs;
		    PreparedStatement ps;
		    PreparedStatement ps1;
		    PreparedStatement ps2;
		    String query;
		    String insertquery;
		    String deletequery;
		    con = null;
		    rs = null; 
		    ps = null;  
		    ps1 = null;  
		    ps2 = null;  
//		    System.out.println("FirstDayProcessDate in procdate: "+procdate);
		    deletequery = "delete from am_gb_classEnable ";
		    query = "UPDATE b SET b.class = a.DefaultClass_Id FROM am_gb_classDisable a,am_gb_user b " +
		            "WHERE a.Class_Id = b.Class AND a.class_status = 'Y'";
		    insertquery = "insert into am_gb_classEnable(class_id,class_desc,class_name,User_Id,class_status,create_date) " +
		            "select a.class_id,a.class_desc,a.class_name,b.User_Id,'N',? from am_gb_classDisable a " +
		    		"INNER JOIN am_gb_user b ON b.Class = a.class_id " +
		    		"where a.class_status = 'Y'";    
//		    System.out.println("query in OldAssetDepreciation: "+query+"      procdate: "+procdate);
		    try {
		    con = getConnection();
		    ps = con.prepareStatement(deletequery);
		    ps.execute();
		    ps1 = con.prepareStatement(insertquery);
		    ps1.setDate(1, df.dateConvert(procDate));
		    ps1.execute();
		    ps2 = con.prepareStatement(query);
		    ps2.execute();
		    
		    out.println("<script type='text/javascript'>alert('Successfully Disabled Selected Users.');</script>");
            out.println((new StringBuilder("<script> window.location ='DocumentHelp.jsp'</script>"))); 

		} catch (Exception ex) {  
		    System.out.println("WARN: Disable User ->" +
		            ex.getMessage());
		} finally {
		    closeConnection(con, ps, rs);
		    closeConnection(con, ps1, rs);
		    closeConnection(con, ps2, rs);
		}        
		}
	
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	 doPost(request, response);
    }
    
    public boolean updateDisableClass(ArrayList list, int userid, String branchCode, int loginId, String eff_date)
    {
        for(int i = 0; i < list.size(); i++)
        {
//            System.out.println();
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
