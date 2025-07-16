package com.magbel.legend.servlet;


import legend.admin.handlers.CompanyHandler;
import legend.admin.handlers.SecurityHandler;
import legend.admin.objects.UserDisableClass;
import legend.admin.objects.UserEnableClass;

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

public class UserEnableServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection con = null ;
        PreparedStatement ps = null;
         CompanyHandler  comp = new CompanyHandler(); 
        PrintWriter out = response.getWriter();
        int userID;   
        int sum=0;
        String userClass = (String) request.getSession().getAttribute("UserClass");
        String branchId = request.getParameter("branchId");
		 String userId = (String) request.getSession().getAttribute("CurrentUser");
		 String processingDate = request.getParameter("bankProcessingDate");
		 String [] listStatus =request.getParameterValues("status");
		 String class_id="";
		 String class_desc="";
		 String status ="";
		 int count=0;
		 
			//System.out.println("<<<<<<=====userId: "+userId+"    userClass: "+userClass + " branchId: " + branchId);
			
			if (userId == null) {
				userID = 0;
			} else {
				userID = Integer.parseInt(userId);
			}
			
		 if (!userClass.equals("NULL") || userClass!=null){
          try{
     		 for(String selectedItem : listStatus) 
           	  {
           		  String classId = request.getParameter("class_id"+selectedItem);
          		 String classDesc = request.getParameter("class_desc"+selectedItem);
          		 
        		   class_id = classId;
        		   class_desc = classDesc;
        		   status = listStatus.toString();
        		  
//        		  System.out.println("<<<<<<=====class_id: "+class_id+"    class_desc: "+class_desc +
//           				  "    status: "+status.toString());
        		  
        		     	  
        		  if(status != null) {
//        			  System.out.println("Hello...");
        			  String Status = "N";
        			  getUpdateReceord(Status, class_id);
        			  Status = "Y"; 
        			  boolean resp = getUpdateRec(Status, class_id);
        			  if(resp == true) { 
        				  count +=  getUserRecord(class_id);
//                		  System.out.println("<<<<<<< count:" + count);
          		  		
          		  	}
        		  }
        		  
//        		  else {
//        			  //status="Y";
//        			  String Status="Y";
//        			  sum = sum + getUserRecord(class_id);
//        		   System.out.println("<<<<<<< user list:" + sum);
//        		   boolean resp = getUpdateRec(class_id, Status);
//        		  	if(resp == true) {
//        		  		out.println("<script type=\"text/javascript\">");
// 					   out.println("alert('Successfully Enabled "+ sum + " users');");
// 					   out.println("location='DocumentHelp.jsp?np=userEnableParam';");
// 					   out.println("</script>");
//        		  	}
//        		  
//        		  }
        		 
        }
     		 out.println("<script type=\"text/javascript\">");
 		  	 out.println("alert('Successfully Enabled "+ count + " users');");
				   out.println("location='DocumentHelp.jsp?np=userEnableParam';");
				   out.println("</script>"); 	  
				   boolean delete = comp.deleteObject("DELETE FROM am_gb_classEnable");
          }catch (Exception e){
              e.getMessage();
          }finally {
        	  closeConnection(con, ps);
          }
		 }
	
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	 doPost(request, response);
    }
    
   

    public boolean getUpdateRec(String classStatus, String classId) {
    	//System.out.println("<<<< classDesc: " + classDesc);
    	Connection con = null ;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean resp = false;
    	
    	try {
    		String query = "update am_gb_classEnable set class_status=? where class_id=?";
        	con = getConnection();
        	ps = con.prepareStatement(query);
        	ps.setString(1, classStatus);
        	ps.setString(2, classId);
        	int i = ps.executeUpdate();
        	if(i>0) {
        	//	System.out.println("<<<< Update Successful...");
        		resp= true;
        		
        	}
    	}catch(Exception e) {
    		e.getMessage();
    	}finally {
    		closeConnection(con, ps, rs);
    	}
    	
    	return resp;
    }
    
   
    public boolean getUpdateReceord(String classStatus,String classId) {
    	Connection con = null ;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean resp = false;
    	
    	try {
    		String query = "UPDATE a SET a.class = b.Class_Id FROM am_gb_user a, am_gb_classEnable b " + 
    				"WHERE a.User_id = b.User_id AND b.class_status = ? AND class_id= ?";
		
//    		System.out.println("<<<< Update query.. ."+query+"    classStatus: "+classStatus+"     classId: "+classId);
        	con = getConnection();
        	ps = con.prepareStatement(query);
        	ps.setString(1, classStatus);
        	ps.setString(2, classId);
        	int i = ps.executeUpdate();
        	if(i>0) {
        		System.out.println("<<<< Update Successful...");
        		//deleteReceord(classId);
        		resp= true;
        	}
    	}catch(Exception e) {
    		e.getMessage();
    	}finally {
    		closeConnection(con, ps, rs);
    	}
    	
    	return resp;
    }
    
    
    
    
    public void deleteReceord(String classId) {
    	Connection con = null ;
        PreparedStatement ps = null;
        ResultSet rs = null;
    	
    	try {
    		String query = "delete from am_gb_classEnable where class_id=?";
        	con = getConnection();
        	ps = con.prepareStatement(query);
        	ps.setString(1, classId);
        	int i = ps.executeUpdate();
        	if(i>0) {
        		//System.out.println("<<<< Delete Successful...");
        	}
    	}catch(Exception e) {
    		e.getMessage();
    	}finally {
    		closeConnection(con, ps, rs);
    	}
    	
    }
    
    public int getUserRecord(String classId) {
    	int list = 0;
    	Connection con = null ;
        PreparedStatement ps = null;
        ResultSet rs = null;
    	
    	try {
    		
    		String query = "select count(*) from am_gb_classEnable where class_id=?";
        	con = getConnection();
        	ps = con.prepareStatement(query);
        	ps.setString(1, classId);
        	rs = ps.executeQuery();
        	while(rs.next()) {
        		list = rs.getInt(1);
        		//System.out.println("<<<<< Total Number of user: " + list);
        		
        	}

    	}catch(Exception e) {
    		e.getMessage();
    	}finally {
    		closeConnection(con, ps, rs);
    	}
    	
    	return list;
    	
    	
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
