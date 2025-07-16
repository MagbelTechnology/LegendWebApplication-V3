package com.magbel.admin.handlers;

import java.util.*;
import java.io.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import javax.mail.Authenticator;

public class SendMailFile{

private boolean mailSent;

public static class MailAuthenticator extends Authenticator {
	private PasswordAuthentication autentic;

	public MailAuthenticator(String username,String password) {
		autentic = new PasswordAuthentication(username, password);
	}

	public PasswordAuthentication getPasswordAuthentication() {
		return autentic;
	}
}

public void sendMail(String subject,String msgText1,String[] to,String from,
					String host,String filename,String authenticator,
					String password){

  // create some properties and get the default Session
  Properties props = System.getProperties();
  props.put("mail.smtp.host", host);
  props.put("mail.smtp.auth", "true");

//new MailAuthenticator(authenticator,password)
  Session session = Session.getInstance(props,null );

  try
  {
      // create a message
      MimeMessage msg = new MimeMessage(session);
      msg.setFrom(new InternetAddress(from));
      InternetAddress[] address = new InternetAddress[to.length];
      for(int i = 0; i < to.length; i++){

		  if((to[i] == null) || (to[i].equals("null"))){
			  to[i] = "lekan_matanmi@lycos.com";
			  System.out.println("null found as e-mail... defaulting to -->"+to[i]);

		  }
		  address[i] = new InternetAddress(to[i]);
	  }
      msg.setRecipients(Message.RecipientType.TO, address);
      msg.setSubject(subject);

      // create and fill the first message part
      MimeBodyPart mbp1 = new MimeBodyPart();
      mbp1.setText(msgText1);

      // create the second message part
      MimeBodyPart mbp2 = new MimeBodyPart();

            // attach the file to the message
      FileDataSource fds = new FileDataSource(filename);
      mbp2.setDataHandler(new DataHandler(fds));
      mbp2.setFileName(fds.getName());

      // create the Multipart and add its parts to it
      Multipart mp = new MimeMultipart();
      mp.addBodyPart(mbp1);
      mp.addBodyPart(mbp2);

      // add the Multipart to the message
      msg.setContent(mp);

      // set the Date: header
      msg.setSentDate(new Date());
      // send the message
      System.out.println("Sending Message ...");
      System.out.println(msg.toString());
       String userName = to[0];
       msg.saveChanges();
		Transport transport = session.getTransport("smtp");
		transport.connect(host, userName, password);
		transport.sendMessage(msg, msg.getAllRecipients());
		transport.close();
      System.out.println("Mail Sent Sucessfully.");

      setMailSent(true);

  }catch (MessagingException mex){

	  System.out.println("Error sending mail ->>"+mex.getMessage());
	  mex.printStackTrace();

      mex.printStackTrace();
      Exception ex = null;
      if ((ex = mex.getNextException()) != null) {
   			 ex.printStackTrace();
      }
  }

}

public boolean isMailSent(){
	return this.mailSent;
}

public void setMailSent(boolean mailSent){
	this.mailSent = mailSent;
}

public static void main(String[] args) {

 }

}
