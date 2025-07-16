package jdbc;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Email  {

	public static void send(String to,String pass)
	
{ 
		String from = "ayomidematanmi1@gmail.com";
		String host = "smtp.gmail.com";
		String messageText = "This is your new Password: " + pass;
		String user = "ayomidematanmi1@gmail.com";
		String password = "ayomide256";
//create an instance of Properties Class   
Properties props = new Properties();

/* Specifies the IP address of your default mail server
for e.g if you are using gmail server as an email sever
you will pass smtp.gmail.com as value of mail.smtp host. 
As shown here in the code. 
Change accordingly, if your email id is not a gmail id
*/
props.put("mail.smtp.host", host);
props.put("mail.smtp.port", "587");
props.put("mail.pasword", "ayomide256");
props.put("mail.smtp.auth", "true");
props.put("mail.smtp.starttls.enable", "true");

/* Pass Properties object(props) and Authenticator object   
for authentication to Session instance 
*/

Session session = Session.getInstance(props,new javax.mail.Authenticator()
{
	  protected PasswordAuthentication getPasswordAuthentication() 
	  {
	 	 return new PasswordAuthentication(user,password);
	  }
});

try
{
	
/* Create an instance of MimeMessage, 
it accept MIME types and headers 
*/

MimeMessage message = new MimeMessage(session);
message.setFrom(new InternetAddress(from));
message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
message.setSubject("Password Reset");
message.setText(messageText);

/* Transport class is used to deliver the message to the recipients */

Transport.send(message);


}
catch(Exception e)
{
e.printStackTrace();
}
}


	
	
}
