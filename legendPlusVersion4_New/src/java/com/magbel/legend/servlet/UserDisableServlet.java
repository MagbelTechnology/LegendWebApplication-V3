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

import java.io.*; 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserDisableServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection con = null ;
        PreparedStatement ps = null;
        PrintWriter out = response.getWriter();
        legend.admin.handlers.SecurityHandler  security = new legend.admin.handlers.SecurityHandler();
        int userID;
        String userClass = (String) request.getSession().getAttribute("UserClass");
        String branchId = request.getParameter("branchId");
		 String userId = (String) request.getSession().getAttribute("CurrentUser");
		 String processingDate = request.getParameter("bankProcessingDate");
		 String[] checkedIds = request.getParameterValues("status");
		 Set<String> checkedSet = checkedIds != null
				    ? new HashSet<String>(Arrays.asList(checkedIds))
				    : new HashSet<String>();
		 
		 String class_id="";
		 String class_desc="";
		 String report_type="";
		 String Status="";
		
		 
		//	System.out.println("<<<<<<=====userId: "+userId+"    userClass: "+userClass + " branchId: " + branchId);
			
			if (userId == null) {
				userID = 0;
			} else {
				userID = Integer.parseInt(userId);
			}
			
			if (userClass != null && !userClass.equals("NULL")) {

			    ArrayList<UserDisableClass> allClasses = security.getAllClassDisableDetails();
			    ArrayList<UserDisableClass> ulist = new ArrayList<>();
			    
			    try {

			    for (UserDisableClass selectedItem : allClasses) {

			        String classId = selectedItem.getClassId();
			        String classDesc = request.getParameter("class_desc" + classId);
			        String reportType = request.getParameter("report_type" + classId);

			        // Determine if this checkbox was selected
			        String status = checkedSet.contains(classId) ? "Y" : "N";

			        System.out.println("class_id: " + classId +
			                           " class_desc: " + classDesc +
			                           " report_type: " + reportType +
			                           " status: " + status);

			        // Create a new object to store
			        UserDisableClass disableClass = new UserDisableClass();
			        disableClass.setClassId(classId);
			        disableClass.setClassDesc(classDesc);
			        disableClass.setDefaultClassId(reportType);
			        disableClass.setClassStatus(status);

			        ulist.add(disableClass);
			    }

			    // Now update the entire list once
			    if (!ulist.isEmpty()) {
			        boolean updated = updateDisableClass(ulist, userID, branchId, Integer.parseInt(userId), processingDate);

			        if (updated) {
			            out.println("<script type=\"text/javascript\">");
			            out.println("alert('Successful');");
			            out.println("location='DocumentHelp.jsp?np=userDisableParam';");
			            out.println("</script>");
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
       // System.out.println("\n\n >>>>>>>>> dc.getDefaultClassId(): " + dc.getDefaultClassId() + " dc.getClassStatus(): " + dc.getClassStatus() + " dc.getClassId(): " + dc.getClassId());
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
    
    public void updateTable(String classId)
    {
        Connection con;
        PreparedStatement ps;
        String NOTIFY_QUERY;
        con = null;
        ps = null;
        NOTIFY_QUERY = "update am_gb_classDisable set class_status=? where class_id   ";
        try
        {
            con = getConnection();
            ps = con.prepareStatement(NOTIFY_QUERY);
            ps.setString(1, "N");
            ps.setString(2, classId);
            ps.executeUpdate();

        } catch (Exception ex) {
            System.out.println("WARNING: cannot update AM_GB_BATCH_POSTING+" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }

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
