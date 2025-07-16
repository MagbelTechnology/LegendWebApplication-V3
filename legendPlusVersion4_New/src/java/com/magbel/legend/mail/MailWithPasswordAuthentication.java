package com.magbel.legend.mail;

  import java.io.File;
  import java.io.FileInputStream;
  import java.io.InputStream;
  import java.util.Date;
  import java.util.Properties;
  
  import javax.activation.DataHandler;
  import javax.activation.FileDataSource;
  import javax.mail.Message;
  import javax.mail.MessagingException;
  import javax.mail.Multipart;
  import javax.mail.PasswordAuthentication;
  import javax.mail.Session;
  import javax.mail.Transport;
  import javax.mail.internet.InternetAddress;
  import javax.mail.internet.MimeBodyPart;
  import javax.mail.internet.MimeMessage;
  import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeMessage.RecipientType;
  
  public class MailWithPasswordAuthentication 
   {
	    public static void main(String[] args) throws Exception{
	        
			//String[] to={"lekan_matanmi@lycos.com","lekanmatanmi@magbeltech.com"};

			String[] cc={"XXX@yahoo.com"};
			String[] bcc={"XXX@yahoo.com"};
			//This is for google
            String url="";
			String  from = args[0];
			String subject = args[1];
			String message = args[2];
			String[] to = args[3].split(",");
			String filename = args[4];

			Properties props = new Properties();
			InputStream in = (InputStream)(new FileInputStream(new File(url+"\\db-config.properties")));
			
			props.load(in);
			String host = props.getProperty("mail.smtp.host");
			String authenticator = props.getProperty("mail-user");
			String password = props.getProperty("mail-password");
			String port = props.getProperty("mail.smtp.port");
			String starttsl = props.getProperty("mail.smtp.starttls.enable");
			String socketFactory = props.getProperty("mail.smtp.socketFactory.class");
			String auth = props.getProperty("enable-authentication");
			String debug = props.getProperty("mail.smtp.debug");
			String fallback = props.getProperty("mail.smtp.socketFactory.fallback");

			System.out.println("From = "+from+" filename = "+filename);

			System.out.println("<--props-->\n"+props);
	 }
  	
	    
		public synchronized  boolean sendMail(String userName,String passWord,String host,String port,String starttls,String auth,boolean debug,String socketFactoryClass,String fallback,String from,String to,String[] cc,String[] bcc,String subject,String text,String filename,String domain)
		{
			System.out.println("<<<<<<<method2>>>>>");
			System.out.println("send Mail>>>>>username>>>>> 2"+userName);
			System.out.println("send Mail>>>>>password>>>>> 2"+passWord);
			System.out.println("send Mail>>>>>from>>>>> 2"+from);
			System.out.println("send Mail>>>>>to>>>>> 2"+to);
			Properties props = new Properties();
			//Properties props=System.getProperties();
	       // props.put("mail.smtp.user", userName);
	        props.put("mail.smtp.host", host);
			if(!"".equals(port))
	        props.put("mail.smtp.port", port);

			if(!"false".equals(starttls))
	        props.put("mail.smtp.starttls.enable",starttls);
	        props.put("mail.smtp.auth", auth);
			if(debug){
		        props.put("mail.smtp.debug", "true");
			}else{
		        props.put("mail.smtp.debug", "false");
			}
			if(!"".equals(port))
	        props.put("mail.smtp.socketFactory.port", port);

			if(!"".equals(socketFactoryClass))
	        props.put("mail.smtp.socketFactory.class",socketFactoryClass);
			if(!"".equals(fallback))
	        props.put("mail.smtp.socketFactory.fallback", fallback);

			
			
	        try
	        {
//	        	MailAuthenticator ath = new MailAuthenticator();
//	        	ath.getPasswordAuthentication(userName,passWord);
//		        Session session = Session.getDefaultInstance(props,ath);
	        	
	        
		        Session session = Session.getDefaultInstance(props,null);
//	            session.setDebug(debug);
//	        	Session session = null;
	            MimeMessage msg = new MimeMessage(getSession(domain,passWord));
	            session.setDebug(debug);
	            msg.setText(text);
	            msg.setSubject(subject);
//	            msg.setFrom(new InternetAddress(from+"@firstinlandbankplc.net"));
	            msg.setFrom(new InternetAddress(from));
	            //msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
	            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));

				/*for(int i=0;i<to.length;i++){
	            	msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to[i]));
				}*/
	            
				/*
				for(int i=0;i<cc.length;i++){
	            msg.addRecipient(Message.RecipientType.CC, new InternetAddress(cc[i]));
				}
				for(int i=0;i<bcc.length;i++){
	            msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc[i]));
				}*/

				 // create and fill the first message part
				  MimeBodyPart mbp1 = new MimeBodyPart();
				  mbp1.setText(text);

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

	                msg.saveChanges();
	                
				Transport transport = session.getTransport("smtp");	
				transport.connect(host,userName,passWord);
				
	                  if(transport.isConnected())
	                   {
				  System.out.println("host connect");
				  transport.sendMessage(msg, msg.getAllRecipients());
				 }
	                   transport.close();
	                
//	            Transport.send(msg, msg.getAllRecipients());       
	            System.out.println("auth "+auth);
				System.out.println("host "+host);
				System.out.println("port "+port);
				System.out.println("starttls "+starttls);
				System.out.println("socketFactoryClass "+socketFactoryClass);
				System.out.println("fallback "+fallback);
				System.out.println("userName "+userName);
				System.out.println("passWord "+passWord);

				return true;

	        }catch (Exception mex)
	        {
	            mex.printStackTrace();

		    	return false;
	        }
		}
  	private  Session getSession(String domain,String passWord) {
//  		String url="";
  		System.out.println("<<<<<<send Mail3>>>>>>>>>>");
  		System.out.println("in send Mail send Mai2 session userName"+domain);
  		System.out.println("in send Mail send Mai2 session passWord"+passWord);
  		Authenticator authenticator = new Authenticator();
  		Properties properties = new Properties();
  		try{
  		
  		properties.setProperty("mail.smtp.submitter", authenticator.getPasswordAuthentication(domain,passWord).getUserName());
  		properties.setProperty("mail.smtp.auth", "true");
//  		InputStream in = (InputStream)(new FileInputStream(new File(url+"\\db-config.properties")));
//		properties.load(in);
//		String host = properties.getProperty("mail.smtp.host");
//		String port = properties.getProperty("mail.smtp.port");
		properties.setProperty("mail.smtp.host", "10.1.1.59");
  		properties.setProperty("mail.smtp.port", "25");
  		}
  		
  		catch (Exception mex)
        {
            mex.printStackTrace();
        }
  		return Session.getInstance(properties, authenticator);
  	}
  
  	private class Authenticator extends javax.mail.Authenticator 
  	{
  		private PasswordAuthentication authentication;
  		public Authenticator()
  		{
  			String username = "auth-user";
  			String password = "auth-password";
  			authentication = new PasswordAuthentication(username, password);
  		}
  
  		protected PasswordAuthentication getPasswordAuthentication(String domain,String passWord) 
  		{
  			System.out.println("in send Mail>>>>>>>>>>send Mai3");
  			System.out.println("in send Mail>>>>>>>>>>send Mai3 username"+domain);
  			System.out.println("in send Mail>>>>>>>>>>send Mai3 passssword"+passWord);
  			authentication = new PasswordAuthentication(domain, passWord);
  			System.out.println("value==="+authentication);
  			return authentication;
  		}
  	}
  }