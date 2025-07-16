package jdbc;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import jdbc.TemporaryPasswordGenerator;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SendingEmail
 */
@WebServlet("/SendingEmail")
public class SendingEmail extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendingEmail() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String to = request.getParameter("email");

	      // Assuming you are sending email from localhost
	      String host = "localhost:1433";

	      // Get system properties
	      Properties properties = System.getProperties();

	      // Setup mail server
	      properties.setProperty("mail.smtp.host", host);

	      // Get the default Session object.
	      Session session = Session.getDefaultInstance(properties);
 		try {
 		   // Create a default MimeMessage object.
          MimeMessage message = new MimeMessage(session);

          // Set From: header field of the header.
          message.setFrom(new InternetAddress(host));

          // Set To: header field of the header.
          message.addRecipient(Message.RecipientType.TO,
                                   new InternetAddress(to));

          // Set Subject: header field
          message.setSubject("Password Reset");

          // Now set the actual message
          message.setText(TemporaryPasswordGenerator.generateRandomPassword());

          // Send message
          response.sendRedirect("pages/ForgotPasswordRedirect.jsp");
 		}
 		catch(Exception e) {
 			
 		}
	}

}
