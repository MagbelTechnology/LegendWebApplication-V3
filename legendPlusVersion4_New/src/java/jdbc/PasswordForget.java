package jdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import jdbc.TemporaryPasswordGenerator;
import jdbc.Email;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class PasswordForget
 */
@WebServlet("/PasswordForget")
public class PasswordForget extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PasswordForget() {
        super();
        // TODO Auto-generated constructor stub
    }
    
   


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		try {
		String to = request.getParameter("email");
		String dbEmail ="";
		String newPass = TemporaryPasswordGenerator.generateRandomPassword();
		String pass = "";
		
		if(to.isEmpty())
			{
		 out.print("<script>alert('All Fields are Mendatory. Try Again.')</script>");
        	out.print("<script>window.location='pages/ForgotPassword.jsp'</script>");
			}
		
		String sql = "select Email FROM Registration where email=?";
		 Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=FirstWork", "sa", "Fabregas4");
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, to);
      
        
        ResultSet rs = ps.executeQuery();
        
       while(rs.next()) {
    	  
       	dbEmail = rs.getString(1);
       	
       	if(dbEmail.equals(to)) {
       		HttpSession session = request.getSession();
       		session.setAttribute("Email", to);
       		pass= md5(newPass);
       		
         // String sql1 = "update Registration set password='"+newPass+"' where Email='"+dbEmail+"'";
       	 String sql1 = "update Registration set password=? where Email=?";
         //System.out.println("My name 1: "+ sql1);
   		 Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        Connection conn1 = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=FirstWork", "sa", "Fabregas4");
        PreparedStatement ps1 = conn1.prepareStatement(sql1);
        ps1.setString(1, pass);
        ps1.setString(2, dbEmail);
    	
        
       int rs1 = ps1.executeUpdate(); 
      
        if(rs1 > 0) {
        	Email.send(to,newPass);
        	System.out.println("Send This to: "+ dbEmail + " with the value " + newPass);
        	response.sendRedirect("pages/ForgotPasswordRedirect.jsp");
          	System.out.println("Sent Successfully");
        }
        else {
        	out.print("<script>alert('Invalid Email Id !.Try Again.')</script>");
        	out.print("<script>window.location='pages/ForgotPassword.jsp'</script>");
        }
        
   	 
		
     
//            request.setAttribute("Email", to);
//       		request.getRequestDispatcher("pages/ForgotPasswordRedirect.jsp").forward(request, response);

       	}
       	else {
       		out.print("<script>alert('Invalid Email Id !.Try Again.')</script>");
	        	out.print("<script>window.location='pages/ForgotPassword.jsp'</script>");
       	}
       	
       		
       }
		}
	catch(Exception e) {
		
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