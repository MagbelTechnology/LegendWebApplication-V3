package com.magbel.legend.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.security.Key;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.util.Cryptomanager;

import magma.AssetRecordsBean;


public class PersonalUserUpdate extends HttpServlet {
   private static final long serialVersionUID = 1L;
   private static final String ALGO = "AES"; 

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  try {
	   HttpSession session = request.getSession();
	   Properties prop = new Properties();
	   Connection con = null ;
       PreparedStatement ps = null;
	   PrintWriter out = response.getWriter();
	   File file = new File("C:\\Property\\LegendPlus.properties");
	   FileInputStream input = new FileInputStream(file);
	   prop.load(input);

	   String ThirdPartyLabel = prop.getProperty("ThirdPartyLabel");
	   String loginImage = prop.getProperty("loginImage");
	   //System.out.println("<<<<<<<We are here: ");	
	   
	   String userName = request.getParameter("userName");
	   String userBranch = request.getParameter("userBranch");
	   String deptCode = request.getParameter("deptCode");
	   String loginId = (String) session.getAttribute("CurrentUser");
		String userClass = (String) session.getAttribute("UserClass");
		String password0 = request.getParameter("password");
		String userId = request.getParameter("userId");
		int loginID;
		if (loginId == null) {
			loginID = 0;
		} else {
			loginID = Integer.parseInt(loginId);
		}
		System.out.println("<<<<<<=====userId: "+userId+"    userClass: "+userClass+"   loginId: "+loginId);
		String query="update am_gb_user set Branch=?, dept_code=? where User_id=?";
		  con = getConnection();
			ps = con.prepareStatement(query);
			   ps.setString(1, userBranch);
		       ps.setString(2, deptCode);
		       ps.setString(3, userId);
		          int i = ps.executeUpdate();
		          if(i>0) {  
		        	  System.out.println("Inserted successfully");
		        	   out.println("<script type=\"text/javascript\">");
					   out.println("alert('Update Successful. Logout and Login Again!');");
					   out.println("location='DocumentHelp.jsp?np=PersonalUserUpdate';");
					   out.println("</script>");
		          }
       
		
	  }catch(Exception e) {
		  e.getMessage();
	  }
	  
	  
   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      this.doPost(request, response);
   }
   
  
   private Connection getConnection() {
		Connection con = null;
		try {
//       	if(con==null){
               Context initContext = new InitialContext();
               String dsJndi = "java:/legendPlus";
               DataSource ds = (DataSource) initContext.lookup(
               		dsJndi);
               con = ds.getConnection();
//       	}
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
  
   
}