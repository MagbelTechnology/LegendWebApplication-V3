package jdbc;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Registration
 */
@WebServlet("/Registration")
public class Registration extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Registration() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection conn=null;
		try {
			String Name = request.getParameter("name");
			String Password = request.getParameter("password");
			String ConfirmPassword = request.getParameter("confirmPassword");
			String Email = request.getParameter("email");
			String user_type  = request.getParameter("userId");
			String sql = "insert into Registration(username,password,ConfirmPassword,Email,UserId) values (?,?,?,?,?)";
			 if(Name.isEmpty() || Password.isEmpty() || ConfirmPassword.isEmpty() || Email.isEmpty())
	 			{
	 				RequestDispatcher req = request.getRequestDispatcher("registration.jsp");
	 				req.include(request, response);
	 			} 
			Password = md5(Password);
			ConfirmPassword = md5(ConfirmPassword);
			
			//Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			
	        // conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=FirstWork", "sa", "Fabregas4");
			 Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
             conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=FirstWork", "sa", "Fabregas4");
	        	  PreparedStatement ps = conn.prepareStatement(sql);
	 	         ps.setString(1, Name);
	 	         ps.setString(2, Password);
	 	         ps.setString(3, ConfirmPassword);
	 	         ps.setString(4, Email);
	 	         ps.setString(5, user_type);
	 	       
	 	        
	 	         ps.executeUpdate();
	 	      
	 
	 	        response.sendRedirect("pages/Login.jsp");
	        	System.out.println("Loging Successful");
	        	
	        	
	         
		}
		 catch (ClassNotFoundException e)
	    {
	      e.printStackTrace();
	     
	    }
	    catch (SQLException e)
	    {
	      e.printStackTrace();
	      
	    } catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
