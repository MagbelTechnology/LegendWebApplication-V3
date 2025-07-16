package jdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class PasswordReset
 */
@WebServlet("/PasswordReset")
public class PasswordReset extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PasswordReset() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		try {
			String userName = request.getParameter("name");
			String OldPassword = request.getParameter("oldpass");
			String Npassword = request.getParameter("newPassword");
			String Cpassword = request.getParameter("confirmPassword");
			String dbDetail = "";
			
			
			
			
			
			
			 if(OldPassword.isEmpty() || Npassword.isEmpty() || Cpassword.isEmpty())
	 			{
				 out.print("<script>alert('All Fields are Mendatory. Try Again.')</script>");
		        	out.print("<script>window.location='pages/ChangePassword.jsp'</script>");
	 			}
			 else if(Npassword.equals(OldPassword)) {
				 out.print("<script>alert('new password and old password cannot match.')</script>");
		        	out.print("<script>window.location='pages/ChangePassword.jsp'</script>");
			 }
			 else if(!Npassword.equals(Cpassword)) {
				 out.print("<script>alert('new password and confirm new password is not matching.')</script>");
		        	out.print("<script>window.location='pages/ChangePassword.jsp'</script>");
			 }
			
			
			 OldPassword = md5(OldPassword);
			 Npassword = md5(Npassword);
			 
			 
			String sql = "Select * from Registration where username=?";
			 Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	         Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=FirstWork", "sa", "Fabregas4");
	         PreparedStatement ps = conn.prepareStatement(sql);
	         ps.setString(1, userName);
	        
	         
	         ResultSet rs = ps.executeQuery();
	        
	         
	         while(rs.next()) {
	        	 dbDetail = rs.getString(2);
	        	 System.out.println("Old Password is:" +dbDetail);
	        	 
	        	 if(dbDetail.equals(OldPassword)) {
	        		 
	        			
	        			 String sql1 = "Update  Registration set password=? where username=? ";
	        			 Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	        	         Connection conn1 = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=FirstWork", "sa", "Fabregas4");
	        	         PreparedStatement ps1 = conn1.prepareStatement(sql1);
	        	         ps1.setString(1, Npassword);
	        	         ps1.setString(2, userName);
	        	         
	        	         
	        	         ps1.executeUpdate();
	        	         response.sendRedirect("pages/Login.jsp");
	        	         System.out.println("Update Successfull");
	        	         
	        		 
	        		 
	        		 
	        	 }
	        	 else {
	        		 out.print("<script>alert('Invalid Detail.Try Again.')</script>");
			        	out.print("<script>window.location='pages/ChangePassword.jsp'</script>");
	        	 }
	        	 
	        	
	        	 
	       }
 	        	 
	         
		}
		catch(Exception e){
			
		}
	}
	private String md5(String encryptTest) {
		String name = encryptTest;//"ghost";
		String hash  = null;
		try {
			MessageDigest digs = MessageDigest.getInstance("MD5");
			digs.update(name.getBytes(),0, name.length());
			hash = new BigInteger(1, digs.digest()).toString(16);
		} catch(Exception e){
			
		}

		return hash;

	}
}
