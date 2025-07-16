package jdbc;

import java.io.IOException;
import java.math.BigInteger;
import java.security.Key;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.JComponent;
import java.io.PrintWriter;
import javax.swing.JOptionPane;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
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
			String Name = request.getParameter("name");
			String Password = request.getParameter("password");
			String user_type = request.getParameter("userId");
			String dbName = null;
			String dbPassword = null;
			String dropdown = null;

			Password = md5(Password);
			
			
			String sql = "select * from Registration where username=? and password=? and userId=?";
			 Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	         Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=FirstWork", "sa", "Fabregas4");
	         PreparedStatement ps = conn.prepareStatement(sql);
	         ps.setString(1, Name);
	         ps.setString(2, Password);  
	         ps.setString(3, user_type);
	         ResultSet rs = ps.executeQuery();
	        
	         
	         while(rs.next()) {
	        	
	        	 dbName = rs.getString(1);
 	        	 dbPassword = rs.getString(2);
 	        	 dropdown = rs.getString(5);
 	        	
 	        	
	        	 if(dbPassword.equals(Password)) {
	        		if(user_type.equals("admin")) {
	        			HttpSession session = request.getSession();
		        		session.setAttribute("userName", dbName);
		        		session.setAttribute("user_type", dropdown);
		        		
		        		 response.sendRedirect("pages/Welcome.jsp");
	        		}
	        		else if(user_type.equals("pharmacist")){
	        			HttpSession session = request.getSession();
		        		session.setAttribute("userName", dbName);
		        		session.setAttribute("user_type", dropdown);
		        		 response.sendRedirect("pages/home.jsp");
	        		}
	        		else if(user_type.equals("salesman")){
	        			HttpSession session = request.getSession();
		        		session.setAttribute("userName", dbName);
		        		session.setAttribute("user_type", dropdown);
		        		 response.sendRedirect("pages/home.jsp");
	        		}
	        		
			        	
	        	 }
	        	 else {
	        		 
	        	System.out.println("Wrong Entry!!!!");
	        	out.print("<script>alert('Invalid User Name/Password details. Try Again.')</script>");
	        	out.print("<script>window.location='pages/Login.jsp'</script>");
	        		  
	        	 }
	        	 
	         }
	  // response.sendRedirect("Login.jsp");        
	         out.print("<script>alert('Invalid User Name/Password details. Try Again.')</script>");
	        	out.print("<script>window.location='pages/Login.jsp'</script>");
	                
		}
		 catch (ClassNotFoundException e)
	    {
	      e.printStackTrace();
	     
	    }
	    catch (SQLException e)
	    {
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
//	private static String algorithm = "DESede";
//    private static Key key = null;
//    private static Cipher cipher = null;
//private static byte[] encrypt(String input)throws Exception {
//		String name = input;
//        cipher.init(Cipher.ENCRYPT_MODE, key);
//        byte[] inputBytes = name.getBytes();
//        return cipher.doFinal(inputBytes);
//    }
//private static String decrypt(byte[] encryptionBytes)throws Exception {
//    cipher.init(Cipher.DECRYPT_MODE, key);
//    byte[] recoveredBytes =  cipher.doFinal(encryptionBytes);
//    String recovered =  new String(recoveredBytes);
//    return recovered;
//  }
}

