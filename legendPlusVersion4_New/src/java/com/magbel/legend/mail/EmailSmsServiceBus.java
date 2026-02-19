/**
 * <p>Title: EmailSmsServiceBus.java</p>
 *
 * <p>Description:Manages Sending of Email and SMS when Confirmation is approved. </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: MagBel Technology LTD.</p>
 *
 * @author Kalu Nsi Idika
 * @version 1.0
 */

package com.magbel.legend.mail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.security.Security;



import java.net.Socket;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.internet.*;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import magma.net.dao.MagmaDBConnection;

import com.magbel.legend.bus.ApprovalRecords;








import com.magbel.util.DataConnect;
import com.magbel.util.DatetimeFormat;

import java.text.*;


public class EmailSmsServiceBus  {
	NumberFormat format = new DecimalFormat("0.00");
	ApprovalRecords app = new ApprovalRecords();
	MagmaDBConnection mgDbCon = null;
	//MailClient send = new MailClient();
	SimpleDateFormat timer;
	DatetimeFormat df;
	java.text.SimpleDateFormat sdf;
	public EmailSmsServiceBus()
		{
		mgDbCon = new MagmaDBConnection();
		timer = new SimpleDateFormat("kk:mm:ss");
		df = new com.magbel.util.DatetimeFormat();
		}   
	
	public boolean sendRecordMail(String usermails, String subject, String msgText1) {
	    boolean sent = false;

	    try {
	        // Load properties
	        Properties prop = new Properties();
	        File file = new File("C:\\Property\\LegendPlus.properties");
	        try (FileInputStream in = new FileInputStream(file)) {
	            prop.load(in);
	        }

	        String host = prop.getProperty("mail.smtp.host");
	        String from = prop.getProperty("mail-user");
	        String mailAuthenticator = prop.getProperty("mailAuthenticator");
	        String templatePath = prop.getProperty("mailTemplate");
	        String logoPath = prop.getProperty("mailLogo");
	        String legendlgoPath = prop.getProperty("legendLogo");

	        InternetAddress[] toAddresses = Arrays.stream(usermails.split(","))
	                .map(String::trim)
	                .filter(s -> !s.isEmpty())
	                .map(addr -> {
	                    try {
	                        return new InternetAddress(addr, true);
	                    } catch (Exception e) {
	                       // System.err.println("⚠️ Invalid email skipped: " + addr);
	                        return null;
	                    }
	                })
	                .filter(Objects::nonNull)
	                .toArray(InternetAddress[]::new);

	        if (toAddresses.length == 0) {
	            //System.err.println("⚠️ No valid recipient emails found. Skipping send.");
	            return false; 
	        }


	        // ✅ Extract recipient name safely
	        String primaryEmail = toAddresses[0].getAddress();
	        int atIndex = primaryEmail.indexOf('@');
	        String full_Name;
	        if (atIndex > 0) {
	            full_Name = primaryEmail.substring(0, atIndex)
	                                    .replaceAll("[0-9.]", " ")
	                                    .trim()
	                                    .toUpperCase();
	        } else {
	            full_Name = "USER";
	        }

	        // Prepare placeholders for email template
	        Map<String, String> placeholders = new HashMap<>();
	        placeholders.put("recipientName", full_Name);
	        placeholders.put("subject", subject);
	        placeholders.put("bodyText", msgText1);

	        String htmlBody;
	        try {
	            htmlBody = EmailTemplateUtil.loadTemplate(templatePath, placeholders);
	        } catch (Exception e) {
	            System.err.println("⚠️ Error loading email template: " + e.getMessage());
	            htmlBody = msgText1; // fallback plain text
	        }

	        // Build session
	        Session session;
	        if ("Y".equalsIgnoreCase(mailAuthenticator)) {
	            final String user = prop.getProperty("mail-user");
	            final String password = prop.getProperty("mail-password");
	            String port = prop.getProperty("mail.smtp.port");
	            String protocol = prop.getProperty("mail.smtp.ssl.protocols");

	            Properties authProps = new Properties();
	            authProps.put("mail.smtp.host", host);
	            authProps.put("mail.smtp.port", port);
	            authProps.put("mail.smtp.auth", "true");
	            authProps.put("mail.smtp.starttls.enable", "true");
	            authProps.put("mail.smtp.ssl.protocols", protocol);
	            authProps.put("mail.smtp.ssl.trust", host);

	            session = Session.getInstance(authProps, new jakarta.mail.Authenticator() {
	                @Override
	                protected PasswordAuthentication getPasswordAuthentication() {
	                    return new PasswordAuthentication(user, password);
	                }
	            });
	        } else {
	            String port = prop.getProperty("mail.smtp.port");
	            Properties noAuthProps = new Properties();
	            noAuthProps.put("mail.smtp.host", host);
	            noAuthProps.put("mail.smtp.port", port);
	            noAuthProps.put("mail.smtp.auth", "false");
	            session = Session.getInstance(noAuthProps);
	        }

	        // Build message
	        Message msg = new MimeMessage(session);
	        msg.setFrom(new InternetAddress(from));
	        msg.setRecipients(Message.RecipientType.TO, toAddresses);
	        msg.setSubject("Legend - " + subject.trim());
	        msg.setSentDate(new Date());

	        // Build multipart body
	        MimeMultipart multipart = new MimeMultipart("related");

	        MimeBodyPart htmlPart = new MimeBodyPart();
	        htmlPart.setContent(htmlBody, "text/html; charset=UTF-8");
	        multipart.addBodyPart(htmlPart);

	        // ✅ Add inline logos if provided
	        if (logoPath != null && !logoPath.isEmpty()) {
	            MimeBodyPart imagePart = new MimeBodyPart();
	            DataSource fds = new FileDataSource(logoPath);
	            imagePart.setDataHandler(new DataHandler(fds));
	            imagePart.setHeader("Content-ID", "<logo>");
	            imagePart.setDisposition(MimeBodyPart.INLINE);
	            multipart.addBodyPart(imagePart);
	        }

	        if (legendlgoPath != null && !legendlgoPath.isEmpty()) {
	            MimeBodyPart legendImagePart = new MimeBodyPart();
	            DataSource legendDataSource = new FileDataSource(legendlgoPath);
	            legendImagePart.setDataHandler(new DataHandler(legendDataSource));
	            legendImagePart.setHeader("Content-ID", "<legendLogo>");
	            legendImagePart.setDisposition(MimeBodyPart.INLINE);
	            multipart.addBodyPart(legendImagePart);
	        }

	        msg.setContent(multipart);

	        // Send
	        if ("Y".equalsIgnoreCase(mailAuthenticator)) {
	            Transport.send(msg);
	        } else {
	            try (Transport transport = session.getTransport("smtp")) {
	                transport.connect();
	                transport.sendMessage(msg, msg.getAllRecipients());
	            }
	        }

	        System.out.println("✅ Email sent successfully to: " + Arrays.toString(toAddresses));
	        sent = true;

	    } catch (Exception ex) {
	        System.err.println("❌ Error sending email: " + ex.getMessage());
	        ex.printStackTrace();
	    }

	    return sent;
	}


	
	
	
	public void sendMail(String email, String subject, String msgText1) {
	    try {
	        Properties prop = new Properties();
	        FileInputStream in = new FileInputStream("C:\\Property\\LegendPlus.properties");
	        prop.load(in);

	        String host = prop.getProperty("mail.smtp.host");
	        String from = prop.getProperty("mail-user");
	        String mailAuthenticator = prop.getProperty("mailAuthenticator");
	        String templatePath = prop.getProperty("mailTemplate");
	        String logoPath = prop.getProperty("mailLogo");
	        String legendlgoPath = prop.getProperty("legendLogo");

	        String[] recipients = email.split(",");
	        String to = recipients[0];
	        System.out.println("Mail To: " + to + "     Mail from: " + from);

	        // Load template
	        String htmlBody = EmailTemplateUtil.loadTemplate(
	                templatePath, Map.of("recipientName", "Supervisor", "bodyText", msgText1));

	        Session session;
	        if ("Y".equalsIgnoreCase(mailAuthenticator)) {
	            final String user = prop.getProperty("mail-user");
	            final String password = prop.getProperty("mail-password");
	            String port = prop.getProperty("mail.smtp.port");
	            String protocol = prop.getProperty("mail.smtp.ssl.protocols");

	            Properties authProps = new Properties();
	            authProps.put("mail.smtp.host", host);
	            authProps.put("mail.smtp.port", port);
	            authProps.put("mail.smtp.auth", "true");
	            authProps.put("mail.smtp.starttls.enable", "true");
	            authProps.put("mail.smtp.ssl.protocols", protocol);
	            authProps.put("mail.smtp.ssl.trust", host);

	            session = Session.getInstance(authProps, new jakarta.mail.Authenticator() {
	                protected PasswordAuthentication getPasswordAuthentication() {
	                    return new PasswordAuthentication(user, password);
	                }
	            }); 

	        } else {
	            String port = prop.getProperty("mail.smtp.port");
	            Properties noAuthProps = new Properties();
	            noAuthProps.put("mail.smtp.host", host);
	            noAuthProps.put("mail.smtp.port", port);
	            noAuthProps.put("mail.smtp.auth", "false");
	            session = Session.getInstance(noAuthProps, null);
	        }

	        
	        Message msg = new MimeMessage(session);
	        msg.setFrom(new InternetAddress(from));
	        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
	        msg.setSubject("Legend " + subject);
	        msg.setSentDate(new Date());

	        MimeMultipart multipart = new MimeMultipart("related");
	        MimeBodyPart htmlPart = new MimeBodyPart();
	        htmlPart.setContent(htmlBody, "text/html");
	        multipart.addBodyPart(htmlPart);

	        MimeBodyPart imagePart = new MimeBodyPart();
	        imagePart.setDataHandler(new DataHandler(new FileDataSource(logoPath)));
	        imagePart.setHeader("Content-ID", "<logo>");
	        multipart.addBodyPart(imagePart);

	        if (legendlgoPath != null && !legendlgoPath.isEmpty()) {
	            MimeBodyPart legendImagePart = new MimeBodyPart();
	            legendImagePart.setDataHandler(new DataHandler(new FileDataSource(legendlgoPath)));
	            legendImagePart.setHeader("Content-ID", "<legendLogo>");
	            legendImagePart.setDisposition(MimeBodyPart.INLINE);
	            multipart.addBodyPart(legendImagePart);
	        }

	        msg.setContent(multipart);

	        // Add CCs
	        for (int i = 1; i < recipients.length; i++) {
	            msg.addRecipient(Message.RecipientType.CC, new InternetAddress(recipients[i]));
	        }

	        // Send
	        Transport.send(msg);
	        System.out.println("Email Sent.");

	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	}


	
		public void sendMailOld(String email, String subject,String msgText1)
		{
//			System.out.println("Just called the sendApprovalEmail API");
			try
			{
				Properties prop = new Properties();
				File file = new File("C:\\Property\\LegendPlus.properties");
//				System.out.print("Absolute Path:>>> "+file.getAbsolutePath());
//				System.out.print("Able to load file ");
				FileInputStream in = new FileInputStream(file);
				prop.load(in);
				System.out.print("Able to load properties into prop");
				String host = prop.getProperty("mail.smtp.host");
				String from = prop.getProperty("mail-user");
				String mailAuthenticator =  prop.getProperty("mailAuthenticator");
				
				if(mailAuthenticator.equals("N")) {
				Session session = Session.getDefaultInstance(prop,null);
				String port = prop.getProperty("mail.smtp.port");
				boolean sessionDebug = true;
				Properties props = System.getProperties();
				props.put("mail.host",host);
				props.put("mail.smtp.auth","false");
				props.put("mail.smtp.port",port);
				props.put("mail.transport.protocols","smtp");
//				System.out.println("setting auth");
				session = Session.getDefaultInstance(props,null);
				session.setDebug(sessionDebug);

//				System.out.println("From = "+from);
//				System.out.println("point 1");
				
				Message msg = new MimeMessage(session);
//				System.out.println("point 2");
				msg.setFrom(new InternetAddress(from));
//				System.out.println("point 3");

				if(email.equals("")){email= "0";}
				String recepient[]=email.split(",");
				
				String to = recepient[0];
				System.out.println("Mail To: "+to+"     Mail from: "+from);
				InternetAddress[] address = { new InternetAddress(to) };
//				System.out.println("point 4");
				msg.setRecipients(Message.RecipientType.TO,address);
         
				 msg.setSubject(subject);

//				System.out.println("point 6");
				msg.setSentDate(new Date());
//				System.out.println("point 7");

				String msgBody = msgText1;
//			    System.out.print("The mail body: "+msgBody);
			    msg.setText(msgBody);
			    msg.saveChanges();
				
//				System.out.println("point 8");
				
			   
			    		
//				System.out.println("point 9");
				//String cc[]={recepient[1],recepient[2],recepient[3]};
				for(int i=0;i<recepient.length;i++)
				{
				InternetAddress addressCopy = new InternetAddress(recepient[i]);	
				msg.setRecipient(Message.RecipientType.CC, addressCopy);
				}	
//				System.out.println("point 10");
			  	 
	    Transport tr = session.getTransport("smtp");
//	    System.out.println("point 11");
		tr.connect();
//		System.out.println("point 12");
	//	Security.getProviders("smtp");
		
//		System.out.println("point test");
		//tr.sendMessage(msg, msg.getAllRecipients());
		tr.send(msg);
//		System.out.println("point 13");
		tr.close(); 
		
       
//		System.out.println("point 14");
			} else if(mailAuthenticator.equals("Y")) {
				final String user = prop.getProperty("mail-user");
				final String password = prop.getProperty("mail-password");
				 String port = prop.getProperty("mail.smtp.port");
				 String protocol = prop.getProperty("mail.smtp.ssl.protocols");
				boolean sessionDebug = true;
				Properties props = System.getProperties();
				props.put("mail.smtp.host",host);
			    props.put("mail.smtp.port", port);
				 props.put("mail.smtp.auth", "true");
			     props.put("mail.smtp.starttls.enable", "true");
			     props.put("mail.smtp.ssl.protocols", protocol);
			     props.put("mail.smtp.ssl.trust", host);
				Session session = Session.getDefaultInstance(props, 
					    new jakarta.mail.Authenticator(){
					        protected PasswordAuthentication getPasswordAuthentication() {
					            return new PasswordAuthentication(
					            user, password);
					        }
					});
				
				Message msg = new MimeMessage(session);
//				System.out.println("point 2");
				msg.setFrom(new InternetAddress(from));
//				System.out.println("point 3");

				if(email.equals("")){email= "0";}
				String recepient[]=email.split(",");
				
				String to = recepient[0];
				System.out.println("Mail To: "+to+"     Mail from: "+from);
				InternetAddress[] address = { new InternetAddress(to) };
//				System.out.println("point 4");
				msg.setRecipients(Message.RecipientType.TO,address);
         
				 msg.setSubject(subject);

//				System.out.println("point 6");
				msg.setSentDate(new Date());
//				System.out.println("point 7");

				String msgBody = msgText1;
//			    System.out.print("The mail body: "+msgBody);
			    msg.setText(msgBody);
			    msg.saveChanges();
				

				for(int i=0;i<recepient.length;i++)
				{
				InternetAddress addressCopy = new InternetAddress(recepient[i]);	
				msg.setRecipient(Message.RecipientType.CC, addressCopy);
				}	
//				System.out.println("point 10");
				   Transport.send(msg);
				   System.out.println("Email Sent..");
			}
			}
			catch (Exception ex) 
			{
				System.out.println("point 15");
				ex.printStackTrace();
			}

		}		
	
		public void sendMailSupervisor(String id, String subject, String msgText1, String otherparam) {
		    try {
		        Properties prop = new Properties();
		        try (FileInputStream in = new FileInputStream("C:\\Property\\LegendPlus.properties")) {
		            prop.load(in);
		        }

		        String host = prop.getProperty("mail.smtp.host");
		        String appport = prop.getProperty("mail.app.port");
		        String appName = prop.getProperty("mail.appName");
		        String from = prop.getProperty("mail-user");
		        String mailAuthenticator = prop.getProperty("mailAuthenticator");
		        String templatePath = prop.getProperty("mailTemplate");

		        String to = app.userEmail(id);
		        String fullLink = "http://" + host + ":" + appport + "/" + appName + "/DocumentHelp.jsp?np=" + otherparam + "&userId=" + id;

		        Map<String, String> placeholders = new HashMap<>();
		        placeholders.put("userId", id);
		        placeholders.put("subject", subject);
		        placeholders.put("bodyText", msgText1);
		        placeholders.put("link", fullLink);

		        String htmlBody = EmailTemplateUtil.loadTemplate(templatePath, placeholders);

		        Properties props = new Properties();
		        Session session;

		        if ("Y".equalsIgnoreCase(mailAuthenticator)) {
		            final String user = prop.getProperty("mail-user");
		            final String password = prop.getProperty("mail-password");
		            String port = prop.getProperty("mail.smtp.port");
		            String protocol = prop.getProperty("mail.smtp.ssl.protocols");

		            props.put("mail.smtp.host", host);
		            props.put("mail.smtp.port", port);
		            props.put("mail.smtp.auth", "true");
		            props.put("mail.smtp.starttls.enable", "true");
		            props.put("mail.smtp.ssl.protocols", protocol);
		            props.put("mail.smtp.ssl.trust", host);

		            session = Session.getInstance(props, new jakarta.mail.Authenticator() {
		                protected PasswordAuthentication getPasswordAuthentication() {
		                    return new PasswordAuthentication(user, password);
		                }
		            });
		        } else {
		            String port = prop.getProperty("mail.smtp.port");
		            props.put("mail.smtp.host", host);
		            props.put("mail.smtp.port", port);
		            props.put("mail.smtp.auth", "false");

		            session = Session.getInstance(props, null);
		        }

		        Message msg = new MimeMessage(session);
		        msg.setFrom(new InternetAddress(from));
		        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
		        msg.setSubject("Legend " + subject);
		        msg.setSentDate(new Date());

		        // Set HTML content
		        MimeMultipart multipart = new MimeMultipart("alternative");
		        MimeBodyPart htmlPart = new MimeBodyPart();
		        htmlPart.setContent(htmlBody, "text/html; charset=utf-8");
		        multipart.addBodyPart(htmlPart);

		        msg.setContent(multipart);
		        msg.saveChanges();

		        Transport.send(msg);
		        System.out.println("Supervisor email sent successfully.");
		    } catch (Exception ex) {
		        System.out.println("Error in sendMailSupervisor:");
		        ex.printStackTrace();
		    }
		}

		
		public void sendMailSupervisorOld(String id, String subject,String msgText1,String otherparam)
		{
//			System.out.println("Just called the sendApprovalEmail API");
			try
			{
				Properties prop = new Properties();
				File file = new File("C:\\Property\\LegendPlus.properties");
//				System.out.print("Absolute Path:>>> "+file.getAbsolutePath());
//				System.out.print("Able to load file ");
				FileInputStream in = new FileInputStream(file);
				prop.load(in);
//				System.out.print("Able to load properties into prop");
//				String host = prop.getProperty("mail.ipaddress.host");
				String host = prop.getProperty("mail.smtp.host");
				String appport = prop.getProperty("mail.app.port");
				String appName = prop.getProperty("mail.appName");
				String from = prop.getProperty("mail-user");
				String mailAuthenticator =  prop.getProperty("mailAuthenticator");
				
				if(mailAuthenticator.equals("N")) {
				Session session = Session.getDefaultInstance(prop,null);
				String port = prop.getProperty("mail.smtp.port");
				boolean sessionDebug = true;
				Properties props = System.getProperties();
				props.put("mail.host",host);
				props.put("mail.smtp.auth","false");
				props.put("mail.smtp.port",port);
				props.put("mail.transport.protocols","smtp");
//				System.out.println("setting auth");
				session = Session.getDefaultInstance(props,null);
				session.setDebug(sessionDebug);

//				System.out.println("From = "+from);
//				System.out.println("point 1");
				
				Message msg = new MimeMessage(session);
//				System.out.println("point 2");
				msg.setFrom(new InternetAddress(from));
//				System.out.println("point 3");
				String to = app.userEmail(id);
				System.out.println("To: = "+to);
				InternetAddress[] address = { new InternetAddress(to) };
//				System.out.println("point 4");
				msg.setRecipients(Message.RecipientType.TO,address);
         
				 msg.setSubject(subject);

//				System.out.println("point 6");
				msg.setSentDate(new Date());
//				System.out.println("point 7");
				appName = appName+"/DocumentHelp.jsp?np="+otherparam;
				String appaddress = host+":"+appport+"/"+appName;
				System.out.println("appaddress: "+appaddress);
				String msgBody = "Subject: <a href='http://"+appaddress+"&"+"userId="+id+" '>"+msgText1+"</a> <br/>";							
//				String msgBody = msgText1;
			    System.out.print("The mail body: "+msgBody);
			    msg.setText(msgBody);
			    msg.saveChanges();
				
//				System.out.println("point 8");
				
			   
			    		
//				System.out.println("point 9");
				
//				System.out.println("point 10");
			  	 
	    Transport tr = session.getTransport("smtp");
//	    System.out.println("point 11");
		tr.connect();
//		System.out.println("point 12");
	//	Security.getProviders("smtp");
		
//		System.out.println("point test");
		//tr.sendMessage(msg, msg.getAllRecipients());
		tr.send(msg);
//		System.out.println("point 13");
		tr.close(); 
		
//		System.out.println("point 14");
				} else if(mailAuthenticator.equals("Y")) {
					final String user = prop.getProperty("mail-user");
					final String password = prop.getProperty("mail-password");
					 String port = prop.getProperty("mail.smtp.port");
					 String protocol = prop.getProperty("mail.smtp.ssl.protocols");
					boolean sessionDebug = true;
					Properties props = System.getProperties();
					props.put("mail.smtp.host",host);
				    props.put("mail.smtp.port", port);
					 props.put("mail.smtp.auth", "true");
				     props.put("mail.smtp.starttls.enable", "true");
				     props.put("mail.smtp.ssl.protocols", protocol);
				     props.put("mail.smtp.ssl.trust", host);
					Session session = Session.getDefaultInstance(props, 
						    new jakarta.mail.Authenticator(){
						        protected PasswordAuthentication getPasswordAuthentication() {
						            return new PasswordAuthentication(
						            user, password);
						        }
						});
					
					Message msg = new MimeMessage(session);
//					System.out.println("point 2");
					msg.setFrom(new InternetAddress(from));
//					System.out.println("point 3");
					String to = app.userEmail(id);
					System.out.println("To: = "+to);
					InternetAddress[] address = { new InternetAddress(to) };
//					System.out.println("point 4");
					msg.setRecipients(Message.RecipientType.TO,address);
	         
					 msg.setSubject(subject);

//					System.out.println("point 6");
					msg.setSentDate(new Date());
//					System.out.println("point 7");
					appName = appName+"/DocumentHelp.jsp?np="+otherparam;
					String appaddress = host+":"+appport+"/"+appName;
					System.out.println("appaddress: "+appaddress);
					String msgBody = "Subject: <a href='http://"+appaddress+"&"+"userId="+id+" '>"+msgText1+"</a> <br/>";							
//					String msgBody = msgText1;
				    System.out.print("The mail body: "+msgBody);
				    msg.setText(msgBody);
				    msg.saveChanges();
				    
					Transport.send(msg);
					System.out.println("Email Sent..");

				}
			} 
			catch (Exception ex) 
			{
				System.out.println("point 15");
				ex.printStackTrace();
			}

		}	
	
		public void sendMailwithAddress(String recipient, String subject, String msgText1, String otherparam) {
		    try {
		        Properties prop = new Properties();
		        try (FileInputStream in = new FileInputStream("C:\\Property\\LegendPlus.properties")) {
		            prop.load(in);
		        }

		        String host = prop.getProperty("mail.smtp.host");
		        String appPort = prop.getProperty("mail.app.port");
		        String appName = prop.getProperty("mail.appName");
		        String from = prop.getProperty("mail-user");
		        String mailAuthenticator = prop.getProperty("mailAuthenticator");
		        String templatePath = prop.getProperty("mailTemplate"); 
		        String logoPath = prop.getProperty("mailLogo");
		        String legendlgoPath = prop.getProperty("legendLogo");
		        String htmlBody = null;

		        // Construct dynamic URL
		        String appAddress = "http://" + host + ":" + appPort + "/" + appName + "/systemConnect.jsp?np=" + otherparam;

		        // Load and prepare email content
		        Map<String, String> placeholders = new HashMap<>();
		        placeholders.put("subject", subject);
		        placeholders.put("bodyText", msgText1);
		        placeholders.put("link", appAddress);

		        try {
		         htmlBody = EmailTemplateUtil.loadTemplate(templatePath, placeholders);
		        }catch(Exception e) {
		        	e.getMessage();
		        }

		        Session session;
		        Properties sessionProps = new Properties();

		        if ("Y".equalsIgnoreCase(mailAuthenticator)) {
		            final String user = prop.getProperty("mail-user");
		            final String password = prop.getProperty("mail-password");
		            String port = prop.getProperty("mail.smtp.port");
		            String protocol = prop.getProperty("mail.smtp.ssl.protocols");

		            sessionProps.put("mail.smtp.host", host);
		            sessionProps.put("mail.smtp.port", port);
		            sessionProps.put("mail.smtp.auth", "true");
		            sessionProps.put("mail.smtp.starttls.enable", "true");
		            sessionProps.put("mail.smtp.ssl.protocols", protocol);
		            sessionProps.put("mail.smtp.ssl.trust", host);

		            session = Session.getInstance(sessionProps, new jakarta.mail.Authenticator() {
		                protected PasswordAuthentication getPasswordAuthentication() {
		                    return new PasswordAuthentication(user, password);
		                }
		            });
		        } else {
		            String port = prop.getProperty("mail.smtp.port");
		            sessionProps.put("mail.smtp.host", host);
		            sessionProps.put("mail.smtp.port", port);
		            sessionProps.put("mail.smtp.auth", "false");

		            session = Session.getInstance(sessionProps);
		        }

		       
		        Message msg = new MimeMessage(session);
		        msg.setFrom(new InternetAddress(from));
		        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
		        msg.setSubject("Legend " + subject);
		        msg.setSentDate(new Date());

		        MimeMultipart multipart = new MimeMultipart("related");

		        
		        MimeBodyPart htmlPart = new MimeBodyPart();
		        htmlPart.setContent(htmlBody, "text/html");
		        multipart.addBodyPart(htmlPart);

		        
		        MimeBodyPart imagePart = new MimeBodyPart();
		        DataSource fds = new FileDataSource(logoPath);
		        imagePart.setDataHandler(new DataHandler(fds));
		        imagePart.setHeader("Content-ID", "<logo>");
		        multipart.addBodyPart(imagePart);
		        
		        if (legendlgoPath != null && !legendlgoPath.isEmpty()) {
		            MimeBodyPart legendImagePart = new MimeBodyPart();
		            DataSource legendDataSource = new FileDataSource(legendlgoPath);
		            legendImagePart.setDataHandler(new DataHandler(legendDataSource));
		            legendImagePart.setHeader("Content-ID", "<legendLogo>");
		            legendImagePart.setDisposition(MimeBodyPart.INLINE);
		            multipart.addBodyPart(legendImagePart);
		        }
		        
		        msg.saveChanges();

		        // Send message
		        Transport.send(msg);
		        System.out.println("Email sent to: " + recipient);

		    } catch (Exception ex) {
		        System.out.println("Error in sendMailwithAddress:");
		        ex.printStackTrace();
		    }
		}

		
		public void sendMailwithAddressOld(String receipient, String subject,String msgText1,String otherparam)
		{
//			System.out.println("Just called the sendApprovalEmail API");
			try
			{
				Properties prop = new Properties();
				File file = new File("C:\\Property\\LegendPlus.properties");
//				System.out.print("Absolute Path:>>> "+file.getAbsolutePath());
//				System.out.print("Able to load file ");
				FileInputStream in = new FileInputStream(file);
				prop.load(in);
//				System.out.print("Able to load properties into prop");
//				String host = prop.getProperty("mail.ipaddress.host");
				String host = prop.getProperty("mail.smtp.host");
				String appport = prop.getProperty("mail.app.port");
				String appName = prop.getProperty("mail.appName");
				String from = prop.getProperty("mail-user");
				String mailAuthenticator =  prop.getProperty("mailAuthenticator");
				
				if(mailAuthenticator.equals("N")) {
				Session session = Session.getDefaultInstance(prop,null);
				String port = prop.getProperty("mail.smtp.port");
				boolean sessionDebug = true;
				Properties props = System.getProperties();
				props.put("mail.host",host);
				props.put("mail.smtp.auth","false");
				props.put("mail.smtp.port",port);
				props.put("mail.transport.protocols","smtp");
//				System.out.println("setting auth");
				session = Session.getDefaultInstance(props,null);
				session.setDebug(sessionDebug);

//				System.out.println("From = "+from);
//				System.out.println("point 1");
				
				Message msg = new MimeMessage(session);
//				System.out.println("point 2");
				msg.setFrom(new InternetAddress(from));
//				System.out.println("point 3");
	//			String to = app.userEmail(id);
				String to = receipient;
//				System.out.println("To: = "+to);
				InternetAddress[] address = { new InternetAddress(to) };
//				System.out.println("point 4");
				msg.setRecipients(Message.RecipientType.TO,address);
         
				 msg.setSubject(subject);

//				System.out.println("point 6");
				msg.setSentDate(new Date());
//				System.out.println("point 7");
				appName = appName+"/systemConnect.jsp?np="+otherparam;
				String appaddress = host+":"+appport+"/"+appName;
//				System.out.println("appaddress: "+appaddress);
//				String msgBody = "Subject: <a href='http://"+appaddress+" '>"+msgText1+"</a> <br/>";							
				String msgBody = msgText1;
			    System.out.print("The mail body: "+msgBody);
			    msg.setText(msgBody);
			    msg.saveChanges();
				
//				System.out.println("point 8");
				
			   
			    		
//				System.out.println("point 9");
				
//				System.out.println("point 10");
			  	 
	    Transport tr = session.getTransport("smtp");
//	    System.out.println("point 11");
		tr.connect();
//		System.out.println("point 12");
	//	Security.getProviders("smtp");
		
//		System.out.println("point test");
		//tr.sendMessage(msg, msg.getAllRecipients());
		tr.send(msg);
//		System.out.println("point 13");
		tr.close(); 
		
//		System.out.println("point 14");
			} else if(mailAuthenticator.equals("Y")) {
				final String user = prop.getProperty("mail-user");
				final String password = prop.getProperty("mail-password");
				 String port = prop.getProperty("mail.smtp.port");
				 String protocol = prop.getProperty("mail.smtp.ssl.protocols");
				boolean sessionDebug = true;
				Properties props = System.getProperties();
				props.put("mail.smtp.host",host);
			    props.put("mail.smtp.port", port);
				 props.put("mail.smtp.auth", "true");
			     props.put("mail.smtp.starttls.enable", "true");
			     props.put("mail.smtp.ssl.protocols", protocol);
			     props.put("mail.smtp.ssl.trust", host);
				Session session = Session.getDefaultInstance(props, 
					    new jakarta.mail.Authenticator(){
					        protected PasswordAuthentication getPasswordAuthentication() {
					            return new PasswordAuthentication(
					            user, password);
					        }
					});
				
				Message msg = new MimeMessage(session);
//				System.out.println("point 2");
				msg.setFrom(new InternetAddress(from));
//				System.out.println("point 3");
	//			String to = app.userEmail(id);
				String to = receipient;
//				System.out.println("To: = "+to);
				InternetAddress[] address = { new InternetAddress(to) };
//				System.out.println("point 4");
				msg.setRecipients(Message.RecipientType.TO,address);
         
				 msg.setSubject(subject);

//				System.out.println("point 6");
				msg.setSentDate(new Date());
//				System.out.println("point 7");
				appName = appName+"/systemConnect.jsp?np="+otherparam;
				String appaddress = host+":"+appport+"/"+appName;
//				System.out.println("appaddress: "+appaddress);
//				String msgBody = "Subject: <a href='http://"+appaddress+" '>"+msgText1+"</a> <br/>";							
				String msgBody = msgText1;
			    System.out.print("The mail body: "+msgBody);
			    msg.setText(msgBody);
			    msg.saveChanges();
			    
			    Transport.send(msg);
				   System.out.println("Email Sent..");
			}
			}
			catch (Exception ex) 
			{
				System.out.println("point 15");
				ex.printStackTrace();
			}

		}	
		
		public void sendMailUser(String assetId, String subject, String msgText1) {
		    System.out.println("Just called the sendApprovalEmail API");
		    try {
		        Properties prop = new Properties();
		        File file = new File("C:\\Property\\LegendPlus.properties");
		        FileInputStream in = new FileInputStream(file);
		        prop.load(in);

		        String host = prop.getProperty("mail.smtp.host");
		        String from = prop.getProperty("mail-user");
		        String mailAuthenticator = prop.getProperty("mailAuthenticator");
		        String templatePath = prop.getProperty("mailTemplate");
		        String logoPath = prop.getProperty("mailLogo");
		        String legendlgoPath = prop.getProperty("legendLogo");
		        String htmlBody = null;            

		        String recipientEmail = app.userEmail(app.userToEmail(assetId));

		        Map<String, String> placeholders = new HashMap<>();
		        placeholders.put("recipientName", "Supervisor");
		        placeholders.put("bodyText", msgText1);

		        try {
		        htmlBody = EmailTemplateUtil.loadTemplate(templatePath, placeholders);
		        }catch(Exception e) {
		        	e.getMessage();
		        }

		        Session session;
		        Message msg;

		        if (mailAuthenticator.equalsIgnoreCase("Y")) {
		            // Authenticated SMTP setup
		            final String user = prop.getProperty("mail-user");
		            final String password = prop.getProperty("mail-password");
		            String port = prop.getProperty("mail.smtp.port");
		            String protocol = prop.getProperty("mail.smtp.ssl.protocols");

		            Properties authProps = System.getProperties();
		            authProps.put("mail.smtp.host", host);
		            authProps.put("mail.smtp.port", port);
		            authProps.put("mail.smtp.auth", "true");
		            authProps.put("mail.smtp.starttls.enable", "true");
		            authProps.put("mail.smtp.ssl.protocols", protocol);
		            authProps.put("mail.smtp.ssl.trust", host);

		            session = Session.getInstance(authProps, 
						    new jakarta.mail.Authenticator(){
						        protected PasswordAuthentication getPasswordAuthentication() {
						            return new PasswordAuthentication(
						            user, password);
						        }
						});

		        } else {
		            String port = prop.getProperty("mail.smtp.port");

		            Properties noAuthProps = System.getProperties();
		            noAuthProps.put("mail.smtp.host", host);
		            noAuthProps.put("mail.smtp.port", port);
		            noAuthProps.put("mail.smtp.auth", "false");

		            session = Session.getDefaultInstance(noAuthProps);
		        }

		        msg = new MimeMessage(session);
		        msg.setFrom(new InternetAddress(from));
		        msg.setRecipients(Message.RecipientType.TO, new InternetAddress[] {
		            new InternetAddress(recipientEmail)
		        });
		        msg.setSubject("Legend " + subject);
		        msg.setSentDate(new Date());

		        
		        MimeMultipart multipart = new MimeMultipart("related");

		        
		        MimeBodyPart htmlPart = new MimeBodyPart();
		        htmlPart.setContent(htmlBody, "text/html");
		        multipart.addBodyPart(htmlPart);

		        
		        MimeBodyPart imagePart = new MimeBodyPart();
		        DataSource fds = new FileDataSource(logoPath);
		        imagePart.setDataHandler(new DataHandler(fds));
		        imagePart.setHeader("Content-ID", "<logo>");
		        multipart.addBodyPart(imagePart);
		        
		        if (legendlgoPath != null && !legendlgoPath.isEmpty()) {
		            MimeBodyPart legendImagePart = new MimeBodyPart();
		            DataSource legendDataSource = new FileDataSource(legendlgoPath);
		            legendImagePart.setDataHandler(new DataHandler(legendDataSource));
		            legendImagePart.setHeader("Content-ID", "<legendLogo>");
		            legendImagePart.setDisposition(MimeBodyPart.INLINE);
		            multipart.addBodyPart(legendImagePart);
		        }

		        msg.setContent(multipart);

		        if (mailAuthenticator.equalsIgnoreCase("Y")) {
		            Transport.send(msg);
		        } else {
		            Transport transport = session.getTransport("smtp");
		            transport.connect();
		            transport.sendMessage(msg, msg.getAllRecipients());
		            transport.close();
		        }

		        System.out.println("Email Sent Here.");

		    } catch (Exception ex) {
		        System.out.println("Error occurred in sendMailUser");
		        ex.printStackTrace();
		    }
		}

		
		public void sendMailUserOld(String Asset_id, String subject,String msgText1)
		{
			System.out.println("Just called the sendApprovalEmail API");
			try
			{
				Properties prop = new Properties();
				File file = new File("C:\\Property\\LegendPlus.properties");
//				System.out.print("Absolute Path:>>> "+file.getAbsolutePath());
//				System.out.print("Able to load file ");
				FileInputStream in = new FileInputStream(file);
				prop.load(in);
//				System.out.print("Able to load properties into prop");
				String host = prop.getProperty("mail.smtp.host");
				String from = prop.getProperty("mail-user");
				String mailAuthenticator =  prop.getProperty("mailAuthenticator");
				
				if(mailAuthenticator.equals("N")) {
				Session session = Session.getDefaultInstance(prop,null);
				String port = prop.getProperty("mail.smtp.port");
				boolean sessionDebug = true;
				Properties props = System.getProperties();
				props.put("mail.host",host);
				props.put("mail.smtp.auth","false");
				props.put("mail.smtp.port",port);
				props.put("mail.transport.protocols","smtp");
//				System.out.println("setting auth");
				session = Session.getDefaultInstance(props,null);
				session.setDebug(sessionDebug);

//				System.out.println("From = "+from);
//				System.out.println("point 1");
				
				Message msg = new MimeMessage(session);
//				System.out.println("point 2");
				msg.setFrom(new InternetAddress(from));
//				System.out.println("point 3");
				String to = app.userEmail(app.userToEmail(Asset_id));
				InternetAddress[] address = { new InternetAddress(to) };
//				System.out.println("point 4");
				msg.setRecipients(Message.RecipientType.TO,address);
         
				 msg.setSubject(subject);

//				System.out.println("point 6");
				msg.setSentDate(new Date());
//				System.out.println("point 7");

				String msgBody = msgText1;
//			    System.out.print("The mail body: "+msgBody);
			    msg.setText(msgBody);
			    msg.saveChanges();
				
//				System.out.println("point 8");
//				System.out.println("point 9");
				
//				System.out.println("point 10");
			  	 
	    Transport tr = session.getTransport("smtp");
//	    System.out.println("point 11");
		tr.connect();
//		System.out.println("point 12");
	//	Security.getProviders("smtp");
		
//		System.out.println("point test");
		//tr.sendMessage(msg, msg.getAllRecipients());
		tr.send(msg);
//		System.out.println("point 13");
		tr.close(); 
//		System.out.println("point 14");
			} else if(mailAuthenticator.equals("Y")) {
				final String user = prop.getProperty("mail-user");
				final String password = prop.getProperty("mail-password");
				 String port = prop.getProperty("mail.smtp.port");
				 String protocol = prop.getProperty("mail.smtp.ssl.protocols");
				boolean sessionDebug = true;
				Properties props = System.getProperties();
				props.put("mail.smtp.host",host);
			    props.put("mail.smtp.port", port);
				 props.put("mail.smtp.auth", "true");
			     props.put("mail.smtp.starttls.enable", "true");
			     props.put("mail.smtp.ssl.protocols", protocol);
			     props.put("mail.smtp.ssl.trust", host);
				Session session = Session.getDefaultInstance(props, 
					    new jakarta.mail.Authenticator(){
					        protected PasswordAuthentication getPasswordAuthentication() {
					            return new PasswordAuthentication(
					            user, password);
					        }
					});
				
				
				Message msg = new MimeMessage(session);
//				System.out.println("point 2");
				msg.setFrom(new InternetAddress(from));
//				System.out.println("point 3");
				String to = app.userEmail(app.userToEmail(Asset_id));
				InternetAddress[] address = { new InternetAddress(to) };
//				System.out.println("point 4");
				msg.setRecipients(Message.RecipientType.TO,address);
         
				 msg.setSubject(subject);

//				System.out.println("point 6");
				msg.setSentDate(new Date());
//				System.out.println("point 7");

				String msgBody = msgText1;
//			    System.out.print("The mail body: "+msgBody);
			    msg.setText(msgBody);
			    msg.saveChanges();
			    
			    Transport.send(msg);
				 System.out.println("Email Sent..");
			}
			}
			catch (Exception ex) 
			{
				System.out.println("point 15");
				ex.printStackTrace();
			}

		}	
	
		public void sendMail(String email, String accountName)
	{
		System.out.println("Just called the sendApprovalEmail API");
		try
		{
			Properties prop = new Properties();
			File file = new File("C:\\Property\\LegendPlus.properties");
//			System.out.print("Absolute Path:>>> "+file.getAbsolutePath());
//			System.out.print("Able to load file ");
			FileInputStream in = new FileInputStream(file);
			prop.load(in);
//			System.out.print("Able to load properties into prop");
			String host = prop.getProperty("mail.smtp.host");
			String from = prop.getProperty("mail-user");
			Session session = Session.getDefaultInstance(prop,null);
			String port = prop.getProperty("mail.smtp.port");
			boolean sessionDebug = true;
			Properties props = System.getProperties();
			props.put("mail.host",host);
			props.put("mail.smtp.auth","false");
			props.put("mail.smtp.port",port);
			props.put("mail.transport.protocols","smtp");
			//props.put("mail.smtp.auth","enable-authentication");
			System.out.println("setting auth");
			session = Session.getDefaultInstance(props,null);
			session.setDebug(sessionDebug);

//			System.out.println("From = "+from);
//			System.out.println("point 1");
			
			Message msg = new MimeMessage(session);
//			System.out.println("point 2");
			msg.setFrom(new InternetAddress(from));
//			System.out.println("point 3");
			InternetAddress[] address = { new InternetAddress(email) };
//			System.out.println("point 4");
			msg.setRecipients(Message.RecipientType.TO,address);
     
			 msg.setSubject("Stop Cheque Success Notification");

//			System.out.println("point 6");
			msg.setSentDate(new Date());
//			System.out.println("point 7");

			String msgBody = "Dear Customer, \n";
		    msgBody += "Notification of successful\n";
		    msgBody += "Cheque Stopped by user "+accountName+".\n";
//		    System.out.print("The mail body: "+msgBody);
		    msg.setText(msgBody);
		    msg.saveChanges();
			
//			System.out.println("point 8");

			InternetAddress addressCopy = new InternetAddress("legendtestmtl@gmail.com");	
//			System.out.println("point 9");
			msg.setRecipient(Message.RecipientType.CC, addressCopy);
		    	
//			System.out.println("point 10");
		  	 
    Transport tr = session.getTransport("smtp");
 //   System.out.println("point 11");
	tr.connect();
//	System.out.println("point 12");
//	Security.getProviders("smtp");
	
//	System.out.println("point test");
	tr.send(msg);
	//tr.sendMessage(msg, msg.getAllRecipients());
//	System.out.println("point 13");
	tr.close(); 
	
   
//	System.out.println("point 14");
		} 
		catch (Exception ex) 
		{
			System.out.println("point 15");
			ex.printStackTrace();
		}

	}
        public void sendMail( String fromm, String email, String subject,String msgText1)
		{
			System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXX SENDING MAIL xxxxxxxxxxxxxxxxxxx");
			try
			{
				Properties prop = new Properties();
				File file = new File("C:\\Property\\LegendPlus.properties");
                                System.out.print("Absolute Path:>>> "+file.getAbsolutePath());
				System.out.print("Able to load file ");
				FileInputStream in = new FileInputStream(file);
				prop.load(in);
				System.out.print("Able to load properties into prop");
				String host = prop.getProperty("mail.smtp.host");
				//String from = prop.getProperty(fromm);
				Session session = Session.getDefaultInstance(prop,null);

				boolean sessionDebug = true;
				Properties props = System.getProperties();
				props.put("mail.host",host);
				props.put("mail.transport.protocols","smtp");
				System.out.println("setting auth");
				session = Session.getDefaultInstance(props,null);
				session.setDebug(sessionDebug);

				System.out.println("From = "+fromm);
				System.out.println("point 1");

				Message msg = new MimeMessage(session);
				System.out.println("point 2");
				msg.setFrom(new InternetAddress(fromm));
				System.out.println("point 3");
				String recepient[]= email.split(",");
				String to = recepient[0];
				InternetAddress[] address = { new InternetAddress(to) };
				System.out.println("point 4");
				msg.setRecipients(Message.RecipientType.TO,address);

				 msg.setSubject(subject);

				System.out.println("point 6");
				msg.setSentDate(new Date());
				System.out.println("point 7");

				String msgBody = msgText1;
			    System.out.print("The mail body: "+msgBody);
			    msg.setText(msgBody);
			    msg.saveChanges();

				System.out.println("point 8");



				System.out.println("point 9");
				//String cc[]={recepient[1],recepient[2],recepient[3]};
				for(int i=0;i<recepient.length;i++)
				{
				InternetAddress addressCopy = new InternetAddress(recepient[i]);
				msg.setRecipient(Message.RecipientType.CC, addressCopy);
				}
				System.out.println("point 10");

	    Transport tr = session.getTransport("smtp");
	    System.out.println("point 11");
		tr.connect();
		System.out.println("point 12");
	//	Security.getProviders("smtp");

		System.out.println("point test");
		//tr.sendMessage(msg, msg.getAllRecipients());
		tr.send(msg);
		System.out.println("point 13");
		tr.close();


		System.out.println("point 14");
			}
			catch (Exception ex)
			{
				System.out.println("point 15");
				ex.printStackTrace();
			}

		}
        
        public void sendMailwithMultipeAddress(String emails, String subject, String msgText1, String otherparam) {
            System.out.println("Sending email to multiple recipients...");

            try {
                Properties prop = new Properties();
                try (FileInputStream in = new FileInputStream("C:\\Property\\LegendPlus.properties")) {
                    prop.load(in);
                }

                String host = prop.getProperty("mail.smtp.host");
                String port = prop.getProperty("mail.smtp.port");
                String appPort = prop.getProperty("mail.app.port");
                String appName = prop.getProperty("mail.appName");
                String from = prop.getProperty("mail-user");
                String mailAuthenticator = prop.getProperty("mailAuthenticator");
                String templatePath = prop.getProperty("mailTemplate"); 
                String logoPath = prop.getProperty("mailLogo");
		        String legendlgoPath = prop.getProperty("legendLogo");
		        String htmlBody = null; 

                String appUrl = "http://" + host + ":" + appPort + "/" + appName + "/systemConnect.jsp?np=" + otherparam;

                
                Map<String, String> placeholders = new HashMap<>();
                placeholders.put("subject", subject);
                placeholders.put("bodyText", msgText1);
                placeholders.put("link", appUrl);

                try {
                 htmlBody = EmailTemplateUtil.loadTemplate(templatePath, placeholders);
                }catch(Exception e) {
                	e.getMessage();
                }

                
                Properties sessionProps = new Properties();
                Session session;

                if ("Y".equalsIgnoreCase(mailAuthenticator)) {
                    final String user = prop.getProperty("mail-user");
                    final String password = prop.getProperty("mail-password");
                    String protocol = prop.getProperty("mail.smtp.ssl.protocols");

                    sessionProps.put("mail.smtp.host", host);
                    sessionProps.put("mail.smtp.port", port);
                    sessionProps.put("mail.smtp.auth", "true");
                    sessionProps.put("mail.smtp.starttls.enable", "true");
                    sessionProps.put("mail.smtp.ssl.protocols", protocol);
                    sessionProps.put("mail.smtp.ssl.trust", host);

                    session = Session.getInstance(sessionProps, 
    					    new jakarta.mail.Authenticator(){
    					        protected PasswordAuthentication getPasswordAuthentication() {
    					            return new PasswordAuthentication(
    					            user, password);
    					        }
    					});
                } else {
                    sessionProps.put("mail.smtp.host", host);
                    sessionProps.put("mail.smtp.port", port);
                    sessionProps.put("mail.smtp.auth", "false");

                    session = Session.getInstance(sessionProps);
                }

                // Build message
                Message msg = new MimeMessage(session);
                msg.setFrom(new InternetAddress(from));
                msg.setSubject(subject);
                msg.setSentDate(new Date());

                // Add recipients
                String[] recipientArray = emails.split(",");
                InternetAddress[] addressTo = new InternetAddress[recipientArray.length];
                for (int i = 0; i < recipientArray.length; i++) {
                    addressTo[i] = new InternetAddress(recipientArray[i].trim());
                }
                msg.setRecipients(Message.RecipientType.TO, addressTo);

                // HTML body
                MimeMultipart multipart = new MimeMultipart("alternative");
                MimeBodyPart htmlPart = new MimeBodyPart();
    	        htmlPart.setContent(htmlBody, "text/html");
    	        multipart.addBodyPart(htmlPart);

    	        
    	        MimeBodyPart imagePart = new MimeBodyPart();
    	        DataSource fds = new FileDataSource(logoPath);
    	        imagePart.setDataHandler(new DataHandler(fds));
    	        imagePart.setHeader("Content-ID", "<logo>");
    	        multipart.addBodyPart(imagePart);
    	        
    	        if (legendlgoPath != null && !legendlgoPath.isEmpty()) {
    	            MimeBodyPart legendImagePart = new MimeBodyPart();
    	            DataSource legendDataSource = new FileDataSource(legendlgoPath);
    	            legendImagePart.setDataHandler(new DataHandler(legendDataSource));
    	            legendImagePart.setHeader("Content-ID", "<legendLogo>");
    	            legendImagePart.setDisposition(MimeBodyPart.INLINE);
    	            multipart.addBodyPart(legendImagePart);
    	        }

                msg.setContent(multipart);
                msg.saveChanges();

                // Send email
                Transport.send(msg);
                System.out.println("Email sent successfully to multiple recipients.");

            } catch (Exception e) {
                System.out.println("Error in sendMailwithMultipeAddress:");
                e.printStackTrace();
            }
        }

        
        public void sendMailwithMultipeAddressOld(String email, String subject,String msgText1,String otherparam)
		{
			System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXX SENDING MULTIPLE MAIL xxxxxxxxxxxxxxxxxxx");
			try
			{
				Properties prop = new Properties();
				File file = new File("C:\\Property\\LegendPlus.properties");
                                System.out.print("Absolute Path:>>> "+file.getAbsolutePath());
				System.out.print("Able to load file ");
				FileInputStream in = new FileInputStream(file);
				prop.load(in);
				System.out.print("Able to load properties into prop");
//				String host = prop.getProperty("mail.smtp.host");
//				String host = prop.getProperty("mail.ipaddress.host");
				String host = prop.getProperty("mail.smtp.host");
				String appport = prop.getProperty("mail.app.port");
				String appName = prop.getProperty("mail.appName");
				String from = prop.getProperty("mail-user");
				//String from = prop.getProperty(fromm);
				Session session = Session.getDefaultInstance(prop,null);

				boolean sessionDebug = true;
				Properties props = System.getProperties();
				props.put("mail.host",host);
				props.put("mail.transport.protocols","smtp");
				System.out.println("setting auth");
				session = Session.getDefaultInstance(props,null);
				session.setDebug(sessionDebug);

				System.out.println("From = "+from);
				System.out.println("point 1");

				Message msg = new MimeMessage(session);
				System.out.println("point 2");
				msg.setFrom(new InternetAddress(from));
				System.out.println("point 3");
				String recepient[]= email.split(",");
				String to = recepient[0];
				InternetAddress[] address = { new InternetAddress(to) };
				System.out.println("point 4");
				msg.setRecipients(Message.RecipientType.TO,address);

				 msg.setSubject(subject);

				System.out.println("point 6");
				msg.setSentDate(new Date());
				System.out.println("point 7");

	//			String msgBody = msgText1;
				
				appName = appName+"/systemConnect.jsp?np="+otherparam;
				String appaddress = host+":"+appport+"/"+appName;
//				System.out.println("appaddress: "+appaddress);
//				String msgBody = "<a href='http://"+appaddress+" '>"+msgText1+"</a> <br/>";	
				String msgBody = msgText1;	
			    System.out.print("The mail body: "+msgBody);
			    msg.setText(msgBody);
			    msg.saveChanges();

				System.out.println("point 8");



				System.out.println("point 9");
				//String cc[]={recepient[1],recepient[2],recepient[3]};
				for(int i=0;i<recepient.length;i++)
				{
				InternetAddress addressCopy = new InternetAddress(recepient[i]);
				msg.setRecipient(Message.RecipientType.CC, addressCopy);
				}
				System.out.println("point 10");

	    Transport tr = session.getTransport("smtp");
	    System.out.println("point 11");
		tr.connect();
		System.out.println("point 12");
	//	Security.getProviders("smtp");

		System.out.println("point test");
		//tr.sendMessage(msg, msg.getAllRecipients());
		tr.send(msg);
		System.out.println("point 13");
		tr.close();


		System.out.println("point 14");
			}
			catch (Exception ex)
			{
				System.out.println("point 15");
				ex.printStackTrace();
			}

		}
		
public void sendEmail(String email, String accountName)
		{
			
			System.out.println("Just called the sendApprovalEmail API");
			//ControlParameter ctrlParam = new ControlParameter();
			//ctrlParam = findCtrlParamValueByCode("PROPERTY_FILE_LOC");
			try
			{
				//File file = new File(ctrlParam.getParameterValue()+"\\CheckPoint.properties");
				Properties prop = new Properties();
				//System.out.print("D:\\Property\\CheckPoint.properties");
				File file = new File("C:\\Property\\LegendPlus.properties");
				System.out.print("Absolute Path:>>> "+file.getAbsolutePath());
				//if(file.exists())
				//{
				System.out.print("Able to load file ");
		
				FileInputStream in = new FileInputStream(file);
				
				//InputStream in = this.getClass().getResourceAsStream("CheckPoint.properties"); //(InputStream)(new FileInputStream(file));
				prop.load(in);
				System.out.print("Able to load properties into prop");
				String host = prop.getProperty("mail.smtp.host");
				String from = prop.getProperty("mail-user");
				String password = prop.getProperty("mail-password");
				String port = prop.getProperty("mail.smtp.port");
				String starttls = prop.getProperty("mail.smtp.starttls.enable");
				
				String auth = prop.getProperty("enable-authentication");
				String strDebug = prop.getProperty("mail.smtp.debug");
				boolean debug = new Boolean(strDebug.trim());
				
				
				//String copyAddress = prop.getProperty("mail.copy.address");
				//String blindCopyAddress = prop.getProperty("mail.blind.copy.address");
			
				System.out.println("From = "+from);
			
				// create some properties and get the default Session
				Properties props = new Properties();
				props.put("mail.smtp.host", host);
				if(!"false".equals(starttls))
				{
			        props.put("mail.smtp.starttls.enable",starttls);
			        props.put("mail.smtp.auth", auth);
				}   
				if(debug){
			        props.put("mail.smtp.debug", "true");
				}else{
			        props.put("mail.smtp.debug", "false");
				}
				if(!"".equals(port))
		        props.put("mail.smtp.socketFactory.port", port);
		
				
				
				Session session = Session.getInstance(props, null);
				session.setDebug(debug);
			
			    // create a message
				System.out.println("About the send the mail>>>>>inside sendApprovalEmail API");
			    Message msg = new MimeMessage(session);
			    msg.setFrom(new InternetAddress(from));
			    InternetAddress address = new InternetAddress(email);
			    //InternetAddress addressBlindCopy = new InternetAddress(blindCopyAddress);
			    msg.setRecipient(Message.RecipientType.TO, address);
			  
				    	InternetAddress addressCopy = new InternetAddress(email);
				    	msg.setRecipient(Message.RecipientType.CC, addressCopy);
			    
			    //msg.setRecipient(Message.RecipientType.BCC, addressBlindCopy);
			    msg.setSubject("Check Confirmation Notification");
			    msg.setSentDate(new Date());
			    		    
			    String msgBody = "Dear  \n";
			    msgBody += "The confirmation of your cheque has been recieved.\n";
			    msgBody += "Below are the details of the confirmed cheque:\n";
			  
			    msgBody += "From:\n UBA Cheque Confirmation Manager";
			    System.out.print("The mail body: "+msgBody);
			    msg.setText(msgBody);
			    msg.saveChanges();
				Transport transport = session.getTransport("smtp");
				transport.connect();
				//transport.connect(host, from, password);
				transport.sendMessage(msg, msg.getAllRecipients());
				transport.close();
				 System.out.print("After sending the mail successfully inside sendApprovalEmail : ");
			//}
			   
			} catch (MessagingException mex) {
			    System.out.println("\n--Exception handling in msgsendsample.java");

			    mex.printStackTrace();
			    System.out.println();
			    Exception ex = mex;
			    do {
				if (ex instanceof SendFailedException) {
				    SendFailedException sfex = (SendFailedException)ex;
				    Address[] invalid = sfex.getInvalidAddresses();
				    if (invalid != null) {
					System.out.println("    ** Invalid Addresses");
					if (invalid != null) {
					    for (int i = 0; i < invalid.length; i++) 
						System.out.println("         " + invalid[i]);
					}
				    }
				    Address[] validUnsent = sfex.getValidUnsentAddresses();
				    if (validUnsent != null) {
					System.out.println("    ** ValidUnsent Addresses");
					if (validUnsent != null) {
					    for (int i = 0; i < validUnsent.length; i++) 
						System.out.println("         "+validUnsent[i]);
					}
				    }
				    Address[] validSent = sfex.getValidSentAddresses();
				    if (validSent != null) {
					System.out.println("    ** ValidSent Addresses");
					if (validSent != null) {
					    for (int i = 0; i < validSent.length; i++) 
						System.out.println("         "+validSent[i]);
					}
				    }
				}
				System.out.println();
				if (ex instanceof MessagingException)
				    ex = ((MessagingException)ex).getNextException();
				else
				    ex = null;
			    } while (ex != null);
			}catch(IOException ioe)
			{
				ioe.printStackTrace();
			}
		}


public void sendMailTransactionInitiator(int tran_id, String subject, String msgText1) {
    System.out.println("Just called sendMailTransactionInitiator API");
    FileInputStream in = null;

    try {
        Properties prop = new Properties();
        File file = new File("C:\\Property\\LegendPlus.properties");
        in = new FileInputStream(file);
        prop.load(in);

        String host = prop.getProperty("mail.smtp.host");
        String from = prop.getProperty("mail-user");
        String mailAuthenticator = prop.getProperty("mailAuthenticator");
        String templatePath = prop.getProperty("mailTemplate");
        String logoPath = prop.getProperty("mailLogo");
        String legendlgoPath = prop.getProperty("legendLogo");
        String protocol = prop.getProperty("mail.smtp.ssl.protocols");
        String port = prop.getProperty("mail.smtp.port");

        // Get recipient email and extract full name
        String toEmail = app.userEmail(app.userToEmailTransInitiator(tran_id));
        String fullName = toEmail.split("@")[0].replaceAll("[0-9.]", " ").toUpperCase();

        // Format date
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        String currentDate = sdf.format(new Date());

        // Prepare placeholders
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("recipientName", fullName);
        placeholders.put("subject", subject);
        placeholders.put("bodyText", msgText1);
        placeholders.put("date", currentDate); 

        // Load HTML content from template
        String htmlBody = EmailTemplateUtil.loadTemplate(templatePath, placeholders);

        // Prepare session
        Session session;
        Properties mailProps = new Properties();
        mailProps.put("mail.smtp.host", host);
        mailProps.put("mail.smtp.port", port);

        if ("Y".equalsIgnoreCase(mailAuthenticator)) {
            final String user = prop.getProperty("mail-user");
            final String password = prop.getProperty("mail-password");

            mailProps.put("mail.smtp.auth", "true");
            mailProps.put("mail.smtp.starttls.enable", "true");
            mailProps.put("mail.smtp.ssl.protocols", protocol);
            mailProps.put("mail.smtp.ssl.trust", host);

            session = Session.getInstance(mailProps, 
				    new jakarta.mail.Authenticator(){
				        protected PasswordAuthentication getPasswordAuthentication() {
				            return new PasswordAuthentication(
				            user, password);
				        }
				});
        } else {
            mailProps.put("mail.smtp.auth", "false");
            session = Session.getInstance(mailProps);
        }

        session.setDebug(true);

        // Compose message
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(toEmail)});
        message.setSubject("Legend - " + subject.trim());
        message.setSentDate(new Date());

        // Multipart with HTML and optional images
        MimeMultipart multipart = new MimeMultipart("related");

        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(htmlBody, "text/html");
        multipart.addBodyPart(htmlPart);

        // Add mail logo
        if (logoPath != null && !logoPath.isEmpty()) {
            MimeBodyPart logoPart = new MimeBodyPart();
            logoPart.setDataHandler(new DataHandler(new FileDataSource(logoPath)));
            logoPart.setHeader("Content-ID", "<logo>");
            logoPart.setDisposition(MimeBodyPart.INLINE);
            multipart.addBodyPart(logoPart);
        }

        // Add legend logo if available
        if (legendlgoPath != null && !legendlgoPath.isEmpty()) {
            MimeBodyPart legendPart = new MimeBodyPart();
            legendPart.setDataHandler(new DataHandler(new FileDataSource(legendlgoPath)));
            legendPart.setHeader("Content-ID", "<legendLogo>");
            legendPart.setDisposition(MimeBodyPart.INLINE);
            multipart.addBodyPart(legendPart);
        }

        message.setContent(multipart);

        // Send message
        if ("Y".equalsIgnoreCase(mailAuthenticator)) {
            Transport.send(message);
        } else {
            Transport transport = session.getTransport("smtp");
            transport.connect();
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }

        System.out.println("HTML email sent to transaction initiator: " + toEmail);

    } catch (Exception ex) {
        System.out.println("Error in sendMailTransactionInitiator:");
        ex.printStackTrace();
    } finally {
        try {
            if (in != null) in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public void sendMailTransactionInitiator(Long tran_id, String subject, String msgText1) {
    System.out.println("Just called sendMailTransactionInitiator API");
    FileInputStream in = null;

    try {
        Properties prop = new Properties();
        File file = new File("C:\\Property\\LegendPlus.properties");
        in = new FileInputStream(file);
        prop.load(in);

        String host = prop.getProperty("mail.smtp.host");
        String from = prop.getProperty("mail-user");
        String mailAuthenticator = prop.getProperty("mailAuthenticator");
        String templatePath = prop.getProperty("mailTemplate");
        String logoPath = prop.getProperty("mailLogo");
        String legendlgoPath = prop.getProperty("legendLogo");
        String protocol = prop.getProperty("mail.smtp.ssl.protocols");
        String port = prop.getProperty("mail.smtp.port");

        // Get recipient email and extract full name
        String toEmail = app.userEmail(app.userToEmailTransInitiator(tran_id));
        String fullName = toEmail.split("@")[0].replaceAll("[0-9.]", " ").toUpperCase();

        // Format date
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        String currentDate = sdf.format(new Date());

        // Prepare placeholders
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("recipientName", fullName);
        placeholders.put("subject", subject);
        placeholders.put("bodyText", msgText1);
        placeholders.put("date", currentDate); 

        // Load HTML content from template
        String htmlBody = EmailTemplateUtil.loadTemplate(templatePath, placeholders);

        // Prepare session
        Session session;
        Properties mailProps = new Properties();
        mailProps.put("mail.smtp.host", host);
        mailProps.put("mail.smtp.port", port);

        if ("Y".equalsIgnoreCase(mailAuthenticator)) {
            final String user = prop.getProperty("mail-user");
            final String password = prop.getProperty("mail-password");

            mailProps.put("mail.smtp.auth", "true");
            mailProps.put("mail.smtp.starttls.enable", "true");
            mailProps.put("mail.smtp.ssl.protocols", protocol);
            mailProps.put("mail.smtp.ssl.trust", host);

            session = Session.getInstance(mailProps, 
				    new jakarta.mail.Authenticator(){
				        protected PasswordAuthentication getPasswordAuthentication() {
				            return new PasswordAuthentication(
				            user, password);
				        }
				});
        } else {
            mailProps.put("mail.smtp.auth", "false");
            session = Session.getInstance(mailProps);
        }

        session.setDebug(true);

        // Compose message
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(toEmail)});
        message.setSubject("Legend - " + subject.trim());
        message.setSentDate(new Date());

        // Multipart with HTML and optional images
        MimeMultipart multipart = new MimeMultipart("related");

        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(htmlBody, "text/html");
        multipart.addBodyPart(htmlPart);

        // Add mail logo
        if (logoPath != null && !logoPath.isEmpty()) {
            MimeBodyPart logoPart = new MimeBodyPart();
            logoPart.setDataHandler(new DataHandler(new FileDataSource(logoPath)));
            logoPart.setHeader("Content-ID", "<logo>");
            logoPart.setDisposition(MimeBodyPart.INLINE);
            multipart.addBodyPart(logoPart);
        }

        // Add legend logo if available
        if (legendlgoPath != null && !legendlgoPath.isEmpty()) {
            MimeBodyPart legendPart = new MimeBodyPart();
            legendPart.setDataHandler(new DataHandler(new FileDataSource(legendlgoPath)));
            legendPart.setHeader("Content-ID", "<legendLogo>");
            legendPart.setDisposition(MimeBodyPart.INLINE);
            multipart.addBodyPart(legendPart);
        }

        message.setContent(multipart);

        // Send message
        if ("Y".equalsIgnoreCase(mailAuthenticator)) {
            Transport.send(message);
        } else {
            Transport transport = session.getTransport("smtp");
            transport.connect();
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }

        System.out.println("HTML email sent to transaction initiator: " + toEmail);

    } catch (Exception ex) {
        System.out.println("Error in sendMailTransactionInitiator:");
        ex.printStackTrace();
    } finally {
        try {
            if (in != null) in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



public void sendMailHelpDesk(String senderEmail, String subject,String msgText1,String otherMails)
		{

    System.out.println("Just called the sendApprovalEmail API");
			try
			{
				Properties prop = new Properties();
				File file = new File("C:\\Property\\LegendPlus.properties");
				System.out.print("Absolute Path:>>> "+file.getAbsolutePath());
				System.out.print("Able to load file ");
				FileInputStream in = new FileInputStream(file);
				prop.load(in);
				System.out.print("Able to load properties into prop");
				String host = prop.getProperty("mail.smtp.host");
                String recepientEmail= prop.getProperty("mail.helpdesk");
                String from = prop.getProperty("mail-user");
//                String recepientEmail= "legendhelpdesk@magbeltech.com";
                recepientEmail = recepientEmail+','+otherMails;
				System.out.println("the recepient email is >>>>>>>>> "+recepientEmail );
                                //String from = prop.getProperty("mail.helpdesk");
                                //String from = senderEmail;
				Session session = Session.getDefaultInstance(prop,null);

				boolean sessionDebug = true;
				Properties props = System.getProperties();
				String port = prop.getProperty("mail.smtp.port");
//				boolean sessionDebug = true;
//				Properties props = System.getProperties();
				props.put("mail.host",host);
				props.put("mail.smtp.auth","false");
				props.put("mail.smtp.port",port);
				props.put("mail.transport.protocols","smtp");

				//System.out.println("setting auth");
				session = Session.getDefaultInstance(props,null);
				session.setDebug(sessionDebug);

				System.out.println("From = "+ from);
				System.out.println("point 1");

				Message msg = new MimeMessage(session);
				//System.out.println("point 2");
				msg.setFrom(new InternetAddress(from));
				//System.out.println("point 3");
				String recepient[]=recepientEmail.split(",");
				String to = recepient[0];
				InternetAddress[] address = { new InternetAddress(to) };
				//System.out.println("point 4");
				msg.setRecipients(Message.RecipientType.TO,address);

				 msg.setSubject(subject);

				//System.out.println("point 6");
				msg.setSentDate(new Date());
				//System.out.println("point 7");

				String msgBody = msgText1;
			    //System.out.print("The mail body: "+msgBody);
			    msg.setText(msgBody);
			    msg.saveChanges();

				//System.out.println("point 8");



				//System.out.println("point 9");
				//String cc[]={recepient[1],recepient[2],recepient[3]};
				for(int i=0;i<recepient.length;i++)
				{
				InternetAddress addressCopy = new InternetAddress(recepient[i]);
				msg.setRecipient(Message.RecipientType.CC, addressCopy);
				}
				//System.out.println("point 10");

	    Transport tr = session.getTransport("smtp");
	  //  System.out.println("point 11");
		tr.connect();
		// System.out.println("point 12");
	//	Security.getProviders("smtp");

		//System.out.println("point test");
		//tr.sendMessage(msg, msg.getAllRecipients());
		tr.send(msg);
		//System.out.println("point 13");
		tr.close();


		//System.out.println("point 14");
			}
			catch (Exception ex)
			{
				//System.out.println("point 15");
                            System.out.println(">> Error occurred while sending mail");
				ex.printStackTrace();
			}

		}



public void sendMailUser2(String Asset_id, String subject,String msgText1)
{
	System.out.println("Just called the sendApprovalEmail API");
	try
	{
		Properties prop = new Properties();
		File file = new File("C:\\Property\\LegendPlus.properties");
		System.out.print("Absolute Path:>>> "+file.getAbsolutePath());
		System.out.print("Able to load file ");
		FileInputStream in = new FileInputStream(file);
		prop.load(in);
		System.out.print("Able to load properties into prop");
		String host = prop.getProperty("mail.smtp.host");
		String from = prop.getProperty("mail-user");
		Session session = Session.getDefaultInstance(prop,null);

		boolean sessionDebug = true;
		Properties props = System.getProperties();
		props.put("mail.host",host);
		props.put("mail.transport.protocols","smtp");
		System.out.println("setting auth");
		session = Session.getDefaultInstance(props,null);
		session.setDebug(sessionDebug);

		System.out.println("From = "+from);
		System.out.println("point 1");

		Message msg = new MimeMessage(session);
		System.out.println("point 2");
		msg.setFrom(new InternetAddress(from));
		System.out.println("point 3");
		System.out.println("point ---------->"+Asset_id);
		System.out.println("point ,---------->>"+app.userToEmail2(Asset_id));
		String to = app.userEmail(app.userToEmail2(Asset_id));
		InternetAddress[] address = { new InternetAddress(to) };
		System.out.println("point 4");
		msg.setRecipients(Message.RecipientType.TO,address);

		 msg.setSubject(subject);

		System.out.println("point 6");
		msg.setSentDate(new Date());
		System.out.println("point 7");

		String msgBody = msgText1;
	    System.out.print("The mail body: "+msgBody);
	    msg.setText(msgBody);
	    msg.saveChanges();

		System.out.println("point 8");



		System.out.println("point 9");

		System.out.println("point 10");

Transport tr = session.getTransport("smtp");
System.out.println("point 11");
tr.connect();
System.out.println("point 12");
//	Security.getProviders("smtp");

System.out.println("point test");
//tr.sendMessage(msg, msg.getAllRecipients());
tr.send(msg);
System.out.println("point 13");
tr.close();


System.out.println("point 14");
	}
	catch (Exception ex)
	{
		System.out.println("point 15");
		ex.printStackTrace();
	}

}	


public void sendMailWithAttachmentPersonal(String id, String subject,String msgText1,String fileName)
{
	System.out.println("Just called the sendApprovalEmail API");
	try
	{
		Properties prop = new Properties();
		File file = new File("C:\\Property\\LegendPlus.properties");
		System.out.print("Absolute Path:>>> "+file.getAbsolutePath());
		System.out.print("Able to load file ");
		FileInputStream in = new FileInputStream(file);
		prop.load(in);
//		System.out.print("Able to load properties into prop");
//		String host = prop.getProperty("mail.ipaddress.host");
		String host = prop.getProperty("mail.smtp.host");
		String appport = prop.getProperty("mail.app.port");
		String appName = prop.getProperty("mail.appName");
		String from = prop.getProperty("mail-user");
		Session session = Session.getDefaultInstance(prop,null);
		
		boolean sessionDebug = true;
		Properties props = System.getProperties();
		props.put("mail.host",host);
		props.put("mail.transport.protocols","smtp");
//		System.out.println("setting auth");
		session = Session.getDefaultInstance(props,null);
		session.setDebug(sessionDebug);

//		System.out.println("From = "+from);
		System.out.println("point 1");
		
		Message msg = new MimeMessage(session);
		System.out.println("point 2");
		msg.setFrom(new InternetAddress(from));
		System.out.println("point 3");
//		String to = app.userEmail(id);
		String to = id;
		System.out.println("To: = "+to);
		
		String[] mailList = to.split(";");
		for(int i=0;i<(mailList.length);i++)
		{ 
			System.out.println("====loop Id ===: "+i);
		InternetAddress sendTo = new InternetAddress(mailList[i]);
		System.out.println("====Mail send To ===: "+sendTo);
		msg.addRecipient(Message.RecipientType.TO,sendTo);
		//	Transport.send(msg);
		}		
		 msg.setSubject(subject);

		System.out.println("point 6");
		msg.setSentDate(new Date());
					
			String msgBody = msgText1;
	    System.out.print("The mail body: "+msgBody);
	    msg.setText(msgBody);
	    msg.saveChanges();
		
    
		System.out.println("point 8");
        // Create a multipar message
        Multipart multipart = new MimeMultipart();

        String fileA = System.getProperty("user.home")+"\\Downloads";
        System.out.print("Absolute file:>>> "+fileA);
        System.out.print("Absolute fileName:>>> "+fileName);
        File fileattach =    new File(fileA+"\\"+fileName);	
        System.out.print("Absolute fileattach:>>> "+fileattach);
        
        MimeBodyPart attachmentBodyPart= new MimeBodyPart();
        DataSource source = new FileDataSource(fileattach); // ex : "C:\\test.pdf"
        attachmentBodyPart.setDataHandler(new DataHandler(source));
        attachmentBodyPart.setFileName(fileName); // ex : "test.pdf"
        
 //       multipart.addBodyPart(textBodyPart);  // add the text part
        multipart.addBodyPart(attachmentBodyPart); // add the attachement part
	    		
//		System.out.println("point 9");
		
//		System.out.println("point 10");
	  	 
Transport tr = session.getTransport("smtp");
//System.out.println("point 11");
tr.connect();

tr.send(msg);
//System.out.println("point 13");
tr.close(); 

//System.out.println("point 14");
	} 
	catch (Exception ex) 
	{
		System.out.println("point 15");
		ex.printStackTrace();
	}

}	


public void sendMailWithAttachment(String id, String subject, String msgText1, String branchId, String fileName) {
    FileInputStream in = null;

    try {
        Properties prop = new Properties();
        File file = new File("C:\\Property\\LegendPlus.properties");
        in = new FileInputStream(file);
        prop.load(in);

        String host = prop.getProperty("mail.smtp.host");
        String from = prop.getProperty("mail-user");
        String mailAuthenticator = prop.getProperty("mailAuthenticator");
        String templatePath = prop.getProperty("mailTemplate");
        String logoPath = prop.getProperty("mailLogo");
        String legendlgoPath = prop.getProperty("legendLogo");
        String port = prop.getProperty("mail.smtp.port");
        String protocol = prop.getProperty("mail.smtp.ssl.protocols");

        // Setup mail properties
        Properties mailProps = new Properties();
        mailProps.put("mail.smtp.host", host);
        mailProps.put("mail.smtp.port", port);

        Session session;
        if ("Y".equalsIgnoreCase(mailAuthenticator)) {
            final String user = prop.getProperty("mail-user");
            final String password = prop.getProperty("mail-password");

            mailProps.put("mail.smtp.auth", "true");
            mailProps.put("mail.smtp.starttls.enable", "true");
            mailProps.put("mail.smtp.ssl.protocols", protocol);
            mailProps.put("mail.smtp.ssl.trust", host);

            session = Session.getInstance(mailProps, 
				    new jakarta.mail.Authenticator(){
				        protected PasswordAuthentication getPasswordAuthentication() {
				            return new PasswordAuthentication(
				            user, password);
				        }
				});
        } else {
            mailProps.put("mail.smtp.auth", "false");
            session = Session.getInstance(mailProps);
        }

        session.setDebug(true);

        // Format date
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        String currentDate = sdf.format(new Date());

        // Determine recipient name
        String[] recipientArray = id.split(";");
        String firstRecipient = recipientArray[0];
        String fullName = firstRecipient.split("@")[0].replaceAll("[0-9.]", " ").toUpperCase();

        // Load HTML email body
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("recipientName", fullName);
        placeholders.put("subject", subject);
        placeholders.put("bodyText", msgText1);
        placeholders.put("date", currentDate); // optional

        String htmlBody = EmailTemplateUtil.loadTemplate(templatePath, placeholders);

        // Create message
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        InternetAddress[] toAddresses = new InternetAddress[recipientArray.length];
        for (int i = 0; i < recipientArray.length; i++) {
            toAddresses[i] = new InternetAddress(recipientArray[i].trim());
        }


        message.setRecipients(Message.RecipientType.TO, toAddresses);
        message.setSubject("Legend - " + subject.trim());
        message.setSentDate(new Date());

        // Multipart for HTML + images + attachment
        MimeMultipart multipart = new MimeMultipart("mixed");

        // HTML body part
        MimeBodyPart htmlPart = new MimeBodyPart();
        MimeMultipart relatedMultipart = new MimeMultipart("related");

        MimeBodyPart htmlBodyPart = new MimeBodyPart();
        htmlBodyPart.setContent(htmlBody, "text/html");
        relatedMultipart.addBodyPart(htmlBodyPart);

        // Inline logo
        if (logoPath != null && !logoPath.isEmpty()) {
            MimeBodyPart logoPart = new MimeBodyPart();
            logoPart.setDataHandler(new DataHandler(new FileDataSource(logoPath)));
            logoPart.setHeader("Content-ID", "<logo>");
            logoPart.setDisposition(MimeBodyPart.INLINE);
            relatedMultipart.addBodyPart(logoPart);
        }

        if (legendlgoPath != null && !legendlgoPath.isEmpty()) {
            MimeBodyPart legendPart = new MimeBodyPart();
            legendPart.setDataHandler(new DataHandler(new FileDataSource(legendlgoPath)));
            legendPart.setHeader("Content-ID", "<legendLogo>");
            legendPart.setDisposition(MimeBodyPart.INLINE);
            relatedMultipart.addBodyPart(legendPart);
        }

        htmlPart.setContent(relatedMultipart);
        multipart.addBodyPart(htmlPart);

        // File attachment
        String downloadPath = System.getProperty("user.home") + "\\Downloads\\" + fileName;
        File attachmentFile = new File(downloadPath);
        if (attachmentFile.exists()) {
            MimeBodyPart attachmentPart = new MimeBodyPart();
            DataSource source = new FileDataSource(attachmentFile);
            attachmentPart.setDataHandler(new DataHandler(source));
            attachmentPart.setFileName(fileName);
            multipart.addBodyPart(attachmentPart);
        } else {
            System.out.println("Attachment file not found: " + downloadPath);
        }

        message.setContent(multipart);

        // Send email
        if ("Y".equalsIgnoreCase(mailAuthenticator)) {
            Transport.send(message);
        } else {
            Transport transport = session.getTransport("smtp");
            transport.connect();
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }

        System.out.println("Email with attachment sent successfully.");

    } catch (Exception ex) {
        System.out.println("Error sending email with attachment:");
        ex.printStackTrace();
    } finally {
        try {
            if (in != null) in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



public void sendRegistrationEmail(String email, String id, String role, String basePath) {
    FileInputStream in = null;

    try {
        // Load properties
        Properties prop = new Properties();
        File file = new File(basePath + "\\Property\\LegendPlus.properties");
        in = new FileInputStream(file);
        prop.load(in);

        String host = prop.getProperty("mail.smtp.host");
        String from = prop.getProperty("mail-user");
        String mailAuthenticator = prop.getProperty("mailAuthenticator");
        String templatePath = prop.getProperty("mailTemplate");
        String logoPath = prop.getProperty("mailLogo");
        String legendLogoPath = prop.getProperty("legendLogo");
        String port = prop.getProperty("mail.smtp.port");
        String protocol = prop.getProperty("mail.smtp.ssl.protocols");

        
        String roleDescription = "14"; 

        // Format date
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        String currentDate = sdf.format(new Date());

        // Prepare placeholders for HTML template
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("recipientName", email.split("@")[0].replaceAll("[0-9.]", " ").toUpperCase());
        placeholders.put("subject", "User Registration Approval");
        placeholders.put("bodyText",
                "A new user has been registered.<br><br>" +
                "<b>User ID:</b> " + id + "<br>" +
                "<b>Role:</b> " + roleDescription + "<br><br>" +
                "Approval is required.<br><br>Thank you.");
        placeholders.put("date", currentDate);

        // Load HTML template
        String htmlBody = EmailTemplateUtil.loadTemplate(templatePath, placeholders);

        // Configure session
        Properties mailProps = new Properties();
        mailProps.put("mail.smtp.host", host);
        mailProps.put("mail.smtp.port", port);

        Session session;
        if ("Y".equalsIgnoreCase(mailAuthenticator)) {
            final String user = prop.getProperty("mail-user");
            final String password = prop.getProperty("mail-password");

            mailProps.put("mail.smtp.auth", "true");
            mailProps.put("mail.smtp.starttls.enable", "true");
            mailProps.put("mail.smtp.ssl.protocols", protocol);
            mailProps.put("mail.smtp.ssl.trust", host);

            session = Session.getInstance(mailProps, new jakarta.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(user, password);
                }
            });
        } else {
            mailProps.put("mail.smtp.auth", "false");
            session = Session.getInstance(mailProps);
        }

        session.setDebug(true);

        // Build message
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(email)});
        message.setSubject("Legend - User Registration Approval Notification");
        message.setSentDate(new Date());

        // Compose multipart (HTML + inline images)
        MimeMultipart multipart = new MimeMultipart("related");

        // HTML part
        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(htmlBody, "text/html");
        multipart.addBodyPart(htmlPart);

        // Inline logo
        if (logoPath != null && !logoPath.isEmpty()) {
            MimeBodyPart logoPart = new MimeBodyPart();
            logoPart.setDataHandler(new DataHandler(new FileDataSource(logoPath)));
            logoPart.setHeader("Content-ID", "<logo>");
            logoPart.setDisposition(MimeBodyPart.INLINE);
            multipart.addBodyPart(logoPart);
        }

        if (legendLogoPath != null && !legendLogoPath.isEmpty()) {
            MimeBodyPart legendPart = new MimeBodyPart();
            legendPart.setDataHandler(new DataHandler(new FileDataSource(legendLogoPath)));
            legendPart.setHeader("Content-ID", "<legendLogo>");
            legendPart.setDisposition(MimeBodyPart.INLINE);
            multipart.addBodyPart(legendPart);
        }

        message.setContent(multipart);

        // Send
        if ("Y".equalsIgnoreCase(mailAuthenticator)) {
            Transport.send(message);
        } else {
            Transport transport = session.getTransport("smtp");
            transport.connect();
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }

        System.out.println("Registration approval email sent to: " + email);

    } catch (Exception ex) {
        System.out.println("Error sending registration email:");
        ex.printStackTrace();
    } finally {
        try {
            if (in != null) in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public void SimpleMailWithAttachment(String url, String subject, String msgBody, String notifymail, String userId, String mailto) {
    FileInputStream in = null;

    try {
        // Load properties
        Properties prop = new Properties();
        File file = new File(url + "\\Property\\LegendPlus.properties");
        in = new FileInputStream(file);
        prop.load(in);

        String host = prop.getProperty("mail.smtp.host");
        final String fromEmail = prop.getProperty("mail-user");
        final String password = prop.getProperty("mail-password");
        String port = prop.getProperty("mail.smtp.port");
        String protocol = prop.getProperty("mail.smtp.ssl.protocols");
        String mailAuthenticator = prop.getProperty("mailAuthenticator");
        String templatePath = prop.getProperty("mailTemplate");

        // Get user full name and email
        String fullName = app.getCodeName("SELECT Full_Name FROM am_gb_user WHERE user_id='" + userId + "'");
        String userEmail = app.getCodeName("SELECT email FROM am_gb_user WHERE user_id='" + userId + "'");

        if (fullName == null || fullName.trim().isEmpty()) {
            fullName = userId;
        }

        // Session setup
        Session session;
        Properties mailProps = new Properties();
        mailProps.put("mail.smtp.host", host);
        mailProps.put("mail.smtp.port", port);

        if ("Y".equalsIgnoreCase(mailAuthenticator)) {
            mailProps.put("mail.smtp.auth", "true");
            mailProps.put("mail.smtp.starttls.enable", "true");
            mailProps.put("mail.smtp.ssl.protocols", protocol);
            mailProps.put("mail.smtp.ssl.trust", host);

            session = Session.getInstance(mailProps, 
				    new jakarta.mail.Authenticator(){
				        protected PasswordAuthentication getPasswordAuthentication() {
				            return new PasswordAuthentication(
				            fromEmail, password);
				        }
				});
        } else {
            mailProps.put("mail.smtp.auth", "false");
            session = Session.getInstance(mailProps);
        }

        session.setDebug(true);

        // Load HTML template
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("recipientName", fullName.toUpperCase());
        placeholders.put("subject", subject);
        placeholders.put("bodyText", msgBody);

        String htmlBody = EmailTemplateUtil.loadTemplate(templatePath, placeholders);

        // Build message
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(fromEmail));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailto));
        msg.setSubject("Legend - " + subject.trim());

        // Add CC recipients
        if (notifymail != null && !notifymail.trim().isEmpty()) {
            String[] ccAddresses = notifymail.split(",");
            for (String cc : ccAddresses) {
                msg.addRecipient(Message.RecipientType.CC, new InternetAddress(cc.trim()));
            }
        }

        // Set content as HTML
        MimeMultipart multipart = new MimeMultipart("related");

        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(htmlBody, "text/html; charset=UTF-8");
        multipart.addBodyPart(htmlPart);

        msg.setContent(multipart);
        msg.setSentDate(new Date());

        // Send
        Transport.send(msg);

        System.out.println("Simple HTML email sent successfully.");

    } catch (Exception ex) {
        System.out.println("Error in SimpleMailWithAttachment:");
        ex.printStackTrace();
    } finally {
        try {
            if (in != null) in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

	
public String getEmailMessageEscalate(String transactionType,String categoryCode,String subcategory, String helpType,String technician,String user,String requestDescription,String complaintId,String requestSubject, String FileName,String FieldName,String statusMail,String tech,String dateReported)
{    
	System.out.println("TEST ESCALATE MAIL");
	System.out.println("===transactionType== "+transactionType +"===categoryCode== "+categoryCode+"===helpType== "+helpType);
	String query = " SELECT mail_description,mail_heading,mail_address,Status  FROM HD_MAIL_STATEMENT  WHERE  transaction_Type = '"+transactionType.trim()+"'"+
	"  And category_Code = '"+categoryCode.trim()+"'  And help_Type = '"+helpType.trim()+"'  " ;		
	System.out.println("--query->> 1 "+query);  
	String mailDescription =null;
	String mailHeading=null;
	String mailAddress=null;
	String status=null;
	Connection con = null; 

	PreparedStatement ps = null;
	ResultSet rs = null;
	String Heading = "";
	String Description ="";
	String Fld1 = "";
	String FldDesc = "";
	String FldDesc2 = "";
	String IssueSubCategory = "";
	String IssueId = "";
	String IssueCategory = "";
	try
	{ 
		mgDbCon = new MagmaDBConnection();
		con = mgDbCon.getConnection("legendPlus");
		//con = getConnection("helpDesk"); 
		ps = con.prepareStatement(query); 
		rs = ps.executeQuery();
		System.out.println("--query->>"+query);  
/*
		if(rs.next()){
			mailDescription = rs.getString("mail_description");
			mailHeading=rs.getString("mail_heading");
			mailAddress=rs.getString("mail_address");
		     System.out.println("=======mailHeading>>>>>>> "+mailHeading);
		     System.out.println("=======mailDescription>>>>>>> "+mailDescription);	
		     String[] Mail_Head = mailHeading.split(";");
		     String[] Mail_Description = mailDescription.split(";");
		     status=rs.getString("Status");
		     int i = 0; int j = 0;
		     int sizelentHeading = mailHeading.split(";").length;  
		     int sizelentDescr = mailDescription.split(";").length;  
	//	     System.out.println("=======Heading 0 >>>> "+Mail_Head[0]+"==Index i == "+i+"===sizelentHeading== "+sizelentHeading);
		     if (i < sizelentHeading){
		//    	 System.out.println("=======Heading 0 >>>> "+Mail_Head[0]+"==Index i == "+i+"===sizelentHeading== "+sizelentHeading);
		     if (Mail_Head[0].trim().equalsIgnoreCase("COMPLAINT_ID")){
		    	 Mail_Head[0]=util.findObject("SELECT complaint_id, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
		     }		
		     if (Mail_Head[0].trim().equalsIgnoreCase("complain_category")&& i < sizelentHeading){
		    	 Mail_Head[0]=util.findObject("SELECT Dept_name, UnitHead   FROM   AM_AD_DEPARTMENT WHERE Dept_code = '"+categoryCode+"'");
		     }		
		     if (Mail_Head[0].trim().equalsIgnoreCase("complain_sub_category")){
		    	 Mail_Head[0]=util.findObject("SELECT sub_category_name, sub_category_desc   FROM   HD_COMPLAIN_SUBCATEGORY WHERE sub_category_code = '"+subcategory+"'");	   	  
		     }
		     Heading = Mail_Head[0];  
		     }  
		     i = i + 1;
		     if (i < sizelentHeading){ 
		     if (Mail_Head[1].trim().equalsIgnoreCase("COMPLAINT_ID")){
		    	 Mail_Head[1]=util.findObject("SELECT complaint_id, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");								  
		     }			     			     
		     if (Mail_Head[1].trim().equalsIgnoreCase("complain_category")&& sizelentHeading > i){
		    	 Mail_Head[1]=util.findObject("SELECT Dept_name, UnitHead   FROM   AM_AD_DEPARTMENT WHERE Dept_code = '"+categoryCode+"'");
		     }		
		     if (Mail_Head[1].trim().equalsIgnoreCase("complain_sub_category")){
		    	 Mail_Head[1]=util.findObject("SELECT sub_category_name, sub_category_desc   FROM   HD_COMPLAIN_SUBCATEGORY WHERE sub_category_code = '"+subcategory+"'");			    	
		     }	
		     Heading = Heading + " " + Mail_Head[1];
		     }    
		     i = i + 1;  
		     if (i < sizelentHeading){
		     if (Mail_Head[2].trim().equalsIgnoreCase("COMPLAINT_ID")){
		    	 Mail_Head[2]=util.findObject("SELECT complaint_id, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");								  
		     }			     			     
		     if (Mail_Head[2].trim().equalsIgnoreCase("complain_category")&& i < sizelentHeading){
		    	// String Dept_Code=util.findObject("SELECT complain_category, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
		    	 Mail_Head[2]=util.findObject("SELECT Dept_name, UnitHead   FROM   AM_AD_DEPARTMENT WHERE Dept_code = '"+categoryCode+"'");
		     }			
		     if (Mail_Head[2].trim().equalsIgnoreCase("complain_sub_category")){
		    	// String SubCatCode=util.findObject("SELECT complain_sub_category, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
		    	 Mail_Head[2]=util.findObject("SELECT sub_category_name, sub_category_desc   FROM   HD_COMPLAIN_SUBCATEGORY WHERE sub_category_code = '"+subcategory+"'");
		     }	
		     Heading = Heading + Mail_Head[2];
		     }  
		    // i = i + 1;   
		     if (j < sizelentDescr){
		     if (Mail_Description[0].trim().equalsIgnoreCase("COMPLAINT_ID")){
		    	 Mail_Description[0]=util.findObject("SELECT complaint_id, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
		     }			     			     
		     if (Mail_Description[0].trim().equalsIgnoreCase("complain_category")&& sizelentDescr > j){
		    	// String Dept_Code=util.findObject("SELECT complain_category, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
		    	 Mail_Description[0]=util.findObject("SELECT Dept_name, UnitHead   FROM   AM_AD_DEPARTMENT WHERE Dept_code = '"+categoryCode+"'");
		     }		
		     if (Mail_Description[0].trim().equalsIgnoreCase("complain_sub_category")&& sizelentDescr > j){
		    	// String SubCatCode=util.findObject("SELECT complain_sub_category, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
		    	 Mail_Description[0]=util.findObject("SELECT sub_category_name, sub_category_desc   FROM   HD_COMPLAIN_SUBCATEGORY WHERE sub_category_code = '"+subcategory+"'");
		     }
		     Description =  Mail_Description[0];
		     }  	
		     j = j + 1;
		     if (j < sizelentDescr){
		     if (Mail_Description[1].trim().equalsIgnoreCase("COMPLAINT_ID")&& sizelentDescr > j){
		    	 Mail_Description[1]=util.findObject("SELECT complaint_id, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");								  
		     }			     			     
		     if (Mail_Description[1].trim().equalsIgnoreCase("complain_category")&& sizelentDescr > j){
		    //	 String Dept_Code=util.findObject("SELECT complain_category, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
		    	 Mail_Description[1]=util.findObject("SELECT Dept_name, UnitHead   FROM   AM_AD_DEPARTMENT WHERE Dept_code = '"+categoryCode+"'"); 
		     }		
		     if (Mail_Description[1].trim().equalsIgnoreCase("complain_sub_category")&& sizelentDescr > j){
		    	// String SubCatCode=util.findObject("SELECT complain_sub_category, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
		    	 Mail_Description[1]=util.findObject("SELECT sub_category_name, sub_category_desc   FROM   HD_COMPLAIN_SUBCATEGORY WHERE sub_category_code = '"+subcategory+"'");
		     }	
		     Description = Description + " " + Mail_Description[1];
		     }  	
		     j = j + 1;
		     if (j < sizelentDescr){
		     if (Mail_Description[2].trim().equalsIgnoreCase("COMPLAINT_ID")&& sizelentDescr > j){
		    	 Mail_Description[2]=util.findObject("SELECT complaint_id, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");								  
		     }			     			     
		     if (Mail_Description[2].trim().equalsIgnoreCase("complain_category")&& sizelentDescr > j){
		    	// String Dept_Code=util.findObject("SELECT complain_category, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
		    	 Mail_Description[2]=util.findObject("SELECT Dept_name, UnitHead   FROM   AM_AD_DEPARTMENT WHERE Dept_code = '"+categoryCode+"'");
		     }		
		     if (Mail_Description[2].trim().equalsIgnoreCase("complain_sub_category")&& sizelentDescr > j){
		    //	 String SubCatCode=util.findObject("SELECT complain_sub_category, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
		    	 Mail_Description[2]=util.findObject("SELECT sub_category_name, sub_category_desc   FROM   HD_COMPLAIN_SUBCATEGORY WHERE sub_category_code = '"+subcategory+"'");
		     }	
		     Description = Description + " " + Mail_Description[2];	
		     } 
		} */
		String ResolvedBy = "";
/*		System.out.println("=====complaintId ===== "+complaintId);
	String =util.findObject("SELECT technician, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
	System.out.println("=====Assignee  ===== "+Assignee);
	if (Assignee==null || Assignee.equals("")){
		 ResolvedBy=util.findObject("SELECT UnitHead, email   FROM   AM_AD_DEPARTMENT WHERE Dept_code = '"+categoryCode+"'");
	}else{
	ResolvedBy =util.findObject("select full_name from am_gb_user where user_id  = '"+Assignee+"'");
	}  */
	//System.out.println("=====ResolvedBy ===== "+ResolvedBy);
//	String statusMail=util.findObject("select status_description from hd_status where status_code='"+transactionType+"'");
//	System.out.println("=====statusMail ===== "+statusMail);
	String	Subject="Subject: Escalation of Issue : "+complaintId;
//	String tech=util.findObject("select full_name from am_gb_user where user_id='"+user+"'");		
//		tech=tech=="UNKNOWN" ? "work in progress" :tech;
//		System.out.println("=====tech ===== "+tech);
//	String dateReported=util.findObject("select create_date from "+FileName+" where "+FieldName+"='"+complaintId+"'");	
//	dateReported=dateReported=="UNKNOWN" ? String.valueOf(df.dateConvert(new java.util.Date())) :dateReported;
//	System.out.println("=====dateReported ===== "+dateReported);

	mailDescription=
        "Subject: <a href='http://172.19.2.39:419/LEGEND?id="+complaintId+"'>"+Subject+"</a> <br/>"+			
		"Hi,  <br/>"+  
		"The issue "+complaintId+" is due for action.<br/>"+  
		"Details are as follows :- <br/>"+
		"Issue Id: 	"+complaintId+" <br/>"+
		"Title:"+	requestSubject +" <br/>"+
		"Description:"+ 	requestDescription +" <br/>" + 
		"Created By:"+ 	tech.toLowerCase() + " <br/>"+
//		"Date Reported:"+ 	dateReported+" <br/>"+
		"Issue Current Status:"+ 	statusMail +" <br/>";
//		statusMail+" By:"+ 	tech.toLowerCase() + " <br/>";
//		statusMail+" By:"+ 	ResolvedBy.toLowerCase() + " <br/>"+
//		"Date "+statusMail+":"+ 	df.dateConvert(new java.util.Date())+"+<br/>";
//		"Solution/"+statusMail+" Comment:"+ requestDescription+	" <br/>"+
	
//		 String link = "<a href=\"WWW.google.es\">ACTIVAR CUENTA</a>";
	}
	catch(Exception e)
	{
		System.out.println("Error fetching branch emails email->>");
		e.printStackTrace();
	}finally{
		closeConnection(con,ps,rs); 
	}
	return mailDescription;
}

public String getNewEmailMessageOriginal(String transactionType,String categoryCode,String subcategory, String helpType,String technician,String user,String requestDescription,String complaintId,String requestSubject, String FileName,String FieldName,String Subject, String status, String requestDescriptionNew, String Change,String requestSubjectold,String requestDescriptionold, String Assignee, String ResolvedBy, String statusMail, String tech, String dateReported,Date Date1)
{    
	System.out.println("TESTMAIL ME");
	String query = " SELECT mail_description,mail_heading,mail_address,Status  FROM HD_MAIL_STATEMENT  WHERE  transaction_Type = '"+transactionType.trim()+"'"+
	"  And category_Code = '"+categoryCode.trim()+"'  And help_Type = '"+helpType.trim()+"'  " ;		
	System.out.println("--query->> 1 "+query);  
	String mailDescription =null;
	String mailHeading=null;   
	String mailAddress=null;   
	//String status=null;   
	Connection con = null;  
    MagmaDBConnection mgDbCon = null; 
	PreparedStatement ps = null;
	ResultSet rs = null;
	String Heading = "";
	String Description ="";
	String Fld1 = "";
	String FldDesc = "";
	String FldDesc2 = "";
	String IssueSubCategory = "";
	String IssueId = "";
	String IssueCategory = "";
	try
	{ 
		mgDbCon = new MagmaDBConnection();
		con = mgDbCon.getConnection("legendPlus");
		//con = getConnection("helpDesk"); 
		ps = con.prepareStatement(query); 
		rs = ps.executeQuery();
		System.out.println("--query->>"+query);  
		/*
		if(rs.next()){
			mailDescription = rs.getString("mail_description");
			mailHeading=rs.getString("mail_heading");
			mailAddress=rs.getString("mail_address");
//		     System.out.println("=======mailHeading>>>>>>> "+mailHeading);
//		     System.out.println("=======mailDescription>>>>>>> "+mailDescription);	
		     String[] Mail_Head = mailHeading.split(";");
		     String[] Mail_Description = mailDescription.split(";");
		     status=rs.getString("Status");
		     int i = 0; int j = 0;
		     int sizelentHeading = mailHeading.split(";").length;  
		     int sizelentDescr = mailDescription.split(";").length;  
		//     System.out.println("=======Heading 0 >>>> "+Mail_Head[0]+"==Index i == "+i+"===sizelentHeading== "+sizelentHeading);
		     if (i < sizelentHeading){
		    //	 System.out.println("=======Heading 0 >>>> "+Mail_Head[0]+"==Index i == "+i+"===sizelentHeading== "+sizelentHeading);
		     if (Mail_Head[0].trim().equalsIgnoreCase("COMPLAINT_ID")){
		    	 Mail_Head[0]=util.findObject("SELECT complaint_id, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
		     }		
		     if (Mail_Head[0].trim().equalsIgnoreCase("complain_category")&& i < sizelentHeading){
		    	 Mail_Head[0]=util.findObject("SELECT Dept_name, UnitHead   FROM   AM_AD_DEPARTMENT WHERE Dept_code = '"+categoryCode+"'");
		     }		
		     if (Mail_Head[0].trim().equalsIgnoreCase("complain_sub_category")){
		    	 Mail_Head[0]=util.findObject("SELECT sub_category_name, sub_category_desc   FROM   HD_COMPLAIN_SUBCATEGORY WHERE sub_category_code = '"+subcategory+"'");	   	  
		     }
		     Heading = Mail_Head[0];  
		     }  
		     i = i + 1;
		     if (i < sizelentHeading){ 
		     if (Mail_Head[1].trim().equalsIgnoreCase("COMPLAINT_ID")){
		    	 Mail_Head[1]=util.findObject("SELECT complaint_id, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");								  
		     }			     			     
		     if (Mail_Head[1].trim().equalsIgnoreCase("complain_category")&& sizelentHeading > i){
		    	 Mail_Head[1]=util.findObject("SELECT Dept_name, UnitHead   FROM   AM_AD_DEPARTMENT WHERE Dept_code = '"+categoryCode+"'");
		     }		
		     if (Mail_Head[1].trim().equalsIgnoreCase("complain_sub_category")){
		    	 Mail_Head[1]=util.findObject("SELECT sub_category_name, sub_category_desc   FROM   HD_COMPLAIN_SUBCATEGORY WHERE sub_category_code = '"+subcategory+"'");			    	
		     }	
		     Heading = Heading + " " + Mail_Head[1];
		     }    
		     i = i + 1;  
		     if (i < sizelentHeading){
		     if (Mail_Head[2].trim().equalsIgnoreCase("COMPLAINT_ID")){
		    	 Mail_Head[2]=util.findObject("SELECT complaint_id, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");								  
		     }			     			     
		     if (Mail_Head[2].trim().equalsIgnoreCase("complain_category")&& i < sizelentHeading){
		    	// String Dept_Code=util.findObject("SELECT complain_category, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
		    	 Mail_Head[2]=util.findObject("SELECT Dept_name, UnitHead   FROM   AM_AD_DEPARTMENT WHERE Dept_code = '"+categoryCode+"'");
		     }			
		     if (Mail_Head[2].trim().equalsIgnoreCase("complain_sub_category")){
		    	// String SubCatCode=util.findObject("SELECT complain_sub_category, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
		    	 Mail_Head[2]=util.findObject("SELECT sub_category_name, sub_category_desc   FROM   HD_COMPLAIN_SUBCATEGORY WHERE sub_category_code = '"+subcategory+"'");
		     }	
		     Heading = Heading + Mail_Head[2];
		     }  
		    // i = i + 1;   
		     if (j < sizelentDescr){
		     if (Mail_Description[0].trim().equalsIgnoreCase("COMPLAINT_ID")){
		    	 Mail_Description[0]=util.findObject("SELECT complaint_id, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
		     }			     			     
		     if (Mail_Description[0].trim().equalsIgnoreCase("complain_category")&& sizelentDescr > j){
		    	// String Dept_Code=util.findObject("SELECT complain_category, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
		    	 Mail_Description[0]=util.findObject("SELECT Dept_name, UnitHead   FROM   AM_AD_DEPARTMENT WHERE Dept_code = '"+categoryCode+"'");
		     }		
		     if (Mail_Description[0].trim().equalsIgnoreCase("complain_sub_category")&& sizelentDescr > j){
		    	// String SubCatCode=util.findObject("SELECT complain_sub_category, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
		    	 Mail_Description[0]=util.findObject("SELECT sub_category_name, sub_category_desc   FROM   HD_COMPLAIN_SUBCATEGORY WHERE sub_category_code = '"+subcategory+"'");
		     }
		     Description =  Mail_Description[0];
		     }  	
		     j = j + 1;
		     if (j < sizelentDescr){
		     if (Mail_Description[1].trim().equalsIgnoreCase("COMPLAINT_ID")&& sizelentDescr > j){
		    	 Mail_Description[1]=util.findObject("SELECT complaint_id, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");								  
		     }			     			     
		     if (Mail_Description[1].trim().equalsIgnoreCase("complain_category")&& sizelentDescr > j){
		    //	 String Dept_Code=util.findObject("SELECT complain_category, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
		    	 Mail_Description[1]=util.findObject("SELECT Dept_name, UnitHead   FROM   AM_AD_DEPARTMENT WHERE Dept_code = '"+categoryCode+"'"); 
		     }		
		     if (Mail_Description[1].trim().equalsIgnoreCase("complain_sub_category")&& sizelentDescr > j){
		    	// String SubCatCode=util.findObject("SELECT complain_sub_category, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
		    	 Mail_Description[1]=util.findObject("SELECT sub_category_name, sub_category_desc   FROM   HD_COMPLAIN_SUBCATEGORY WHERE sub_category_code = '"+subcategory+"'");
		     }	
		     Description = Description + " " + Mail_Description[1];
		     }  	
		     j = j + 1;
		     if (j < sizelentDescr){
		     if (Mail_Description[2].trim().equalsIgnoreCase("COMPLAINT_ID")&& sizelentDescr > j){
		    	 Mail_Description[2]=util.findObject("SELECT complaint_id, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");								  
		     }			     			     
		     if (Mail_Description[2].trim().equalsIgnoreCase("complain_category")&& sizelentDescr > j){
		    	// String Dept_Code=util.findObject("SELECT complain_category, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
		    	 Mail_Description[2]=util.findObject("SELECT Dept_name, UnitHead   FROM   AM_AD_DEPARTMENT WHERE Dept_code = '"+categoryCode+"'");
		     }		
		     if (Mail_Description[2].trim().equalsIgnoreCase("complain_sub_category")&& sizelentDescr > j){
		    //	 String SubCatCode=util.findObject("SELECT complain_sub_category, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
		    	 Mail_Description[2]=util.findObject("SELECT sub_category_name, sub_category_desc   FROM   HD_COMPLAIN_SUBCATEGORY WHERE sub_category_code = '"+subcategory+"'");
		     }	
		     Description = Description + " " + Mail_Description[2];	
		     } 
		}*/ 
/*				
		String ResolvedBy = "";   
	String Assignee=util.findObject("SELECT technician, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
	System.out.println("=====Assignee Before ===== "+Assignee);
  	  if(Assignee == null || Assignee.equalsIgnoreCase("")){
		 ResolvedBy=util.findObject("SELECT UnitHead, email   FROM   AM_AD_DEPARTMENT WHERE Dept_code = '"+categoryCode+"'");
		// System.out.println("=====Assignee is Null ===== "+ResolvedBy);
	}else{
	ResolvedBy =util.findObject("select full_name from am_gb_user where user_id  = '"+Assignee+"'");
	// System.out.println("=====Assignee is Not Null ===== "+ResolvedBy);
	}  
	System.out.println("=====ResolvedBy ===== "+ResolvedBy);
	String statusMail=util.findObject("select status_description from hd_status where status_code='"+status+"'");
	System.out.println("=====statusMail ===== "+statusMail);
	//String	Subject="Subject: Notification for Issue : "+complaintId;
	String tech=util.findObject("select full_name from am_gb_user where user_id='"+user+"'");		
		tech=tech=="UNKNOWN" ? "work in progress" :tech;
		System.out.println("=====tech ===== "+tech);
	String dateReported=util.findObject("select create_date from "+FileName+" where "+FieldName+"='"+complaintId+"'");	
	dateReported=dateReported=="UNKNOWN" ? String.valueOf(df.dateConvert(new java.util.Date())) :dateReported;
	System.out.println("=====dateReported ===== "+dateReported);
	System.out.println("=====Change ===== "+Change);
	*/ 
		System.out.println("=====statusMail ===== "+statusMail);
		System.out.println("=====tech.toLowerCase() ===== "+tech.toLowerCase());
	if(Change != "EDITED"||Change != "RE-ASSIGNED"){
	mailDescription=  
    "Subject: <a href='http://172.19.2.116:2012/legendPlus?id="+complaintId+"&Status="+status+"'>"+Subject+"</a> <br/>"+			
	"Hi,  <br/>"+  
	"The issue "+complaintId+" has been "+statusMail+".<br/>"+  
	"Details are as follows :- <br/>"+
	"Issue Id: 	"+complaintId+" <br/>"+
	"Title:"+	requestSubject +" <br/>"+
	"Description:"+ 	requestDescription +" <br/>" + 
	"Created By:"+ 	tech.toLowerCase() + " <br/>"+
//	"Date Reported:"+ 	dateReported+" <br/>"+
	"Issue Current Status:"+ 	statusMail +" <br/>"+
	statusMail+" By:"+ 	tech.toLowerCase() + " <br/>"+
//	statusMail+" By:"+ 	ResolvedBy.toLowerCase() + " <br/>"+ 
//	"Date "+statusMail+":"+ 	df.dateConvert(new java.util.Date())+"+<br/>"+
	"Date "+statusMail+":"+ 	Date1+"+<br/>"+
	"Time "+statusMail+":"+ timer.format(new java.util.Date())+"+<br/>"+
	"Comments:"+ 	requestDescriptionNew +" <br/>";
	}
	if(Change == "EDITED"){ 
		//"Subject: <a href='http://172.19.2.116:419/legendPlus?id="+complaintId+"&Status="+status+"'>"+Subject+"</a> <br/>"+
	mailDescription=
		"Subject: <a href='http://172.19.2.116:2012/legendPlus?id="+complaintId+"&Status="+status+"'>"+Subject+"</a> <br/>"+		
		"Hi,  <br/>"+  
		"The issue "+complaintId+" has been "+Change+".<br/>"+  
		"Details are as follows :- <br/>"+
		"Issue Id: 	"+complaintId+" <br/>"+
		"Title:"+	requestSubject +" <br/>"+
		"Description:"+ 	requestDescription +" <br/>" + 
		"Created By:"+ 	tech.toLowerCase() + " <br/>"+
//		"Date Reported:"+ 	dateReported+" <br/>"+
		"Issue Current Status:"+ 	statusMail +" <br/>"+
		statusMail+" By:"+ 	tech.toLowerCase() + " <br/>"+
//		statusMail+" By:"+ 	ResolvedBy.toLowerCase() + " <br/>"+
		"Date "+statusMail+":"+ 	df.dateConvert(new java.util.Date())+"+<br/>"+
		"Time "+statusMail+":"+ timer.format(new java.util.Date())+"+<br/>"+
		"Comments:"+ 	requestDescriptionNew +" <br/>"+
		"Old Status Are as Follows:"+ " <br/>"+
		"Title:"+	requestSubjectold +" <br/>"+
		"Description:"+ 	requestDescriptionold +" <br/>" + 
//		"Date Reported:"+ 	dateReported+" <br/>"+
		"Issue Status:"+ 	statusMail +" <br/>"+
		statusMail+" By:"+ 	tech.toLowerCase() + " <br/>";
		
//	"Solution/"+statusMail+" Comment:"+ requestDescription+	" <br/>"+
//	"Title:"+	Description +" <br/>"+
//	"Thank you,<br/>"+
//	"Customer Care Centre<br/>";
	}
	if(Change == "RE-ASSIGNED"){ 
		//"Subject: <a href='http://172.19.2.116:419/Oriental?id="+complaintId+"&Status="+status+"'>"+Subject+"</a> <br/>"+
	mailDescription=
		"Subject: <a href='http://172.19.2.116:2012/legendPlus?id="+complaintId+"&Status="+status+"'>"+Subject+"</a> <br/>"+		
		"Hi,  <br/>"+  
		"The issue "+complaintId+" has been "+Change+".<br/>"+  
		"Details are as follows :- <br/>"+
		"Issue Id: 	"+complaintId+" <br/>"+
		"Category:"+	requestSubject +" <br/>"+
		"Issue Type:"+ 	requestDescription +" <br/>" + 
		"Created By:"+ 	tech.toLowerCase() + " <br/>"+
//		"Date Reported:"+ 	dateReported+" <br/>"+
		"Issue Current Status:"+ 	statusMail +" <br/>"+
		statusMail+" By:"+ 	tech.toLowerCase() + " <br/>"+
//		statusMail+" By:"+ 	ResolvedBy.toLowerCase() + " <br/>"+
		"Date "+statusMail+":"+ 	df.dateConvert(new java.util.Date())+"+<br/>"+
		"Time "+statusMail+":"+ timer.format(new java.util.Date())+"+<br/>"+
		"Comments:"+ 	requestDescriptionNew +" <br/>"+
		"Old Status Are as Follows:"+ " <br/>"+
		"Category:"+	requestSubjectold +" <br/>"+
		"Issue Type:"+ 	requestDescriptionold +" <br/>" + 
//		"Date Reported:"+ 	dateReported+" <br/>"+
//		"Old Issue Status:"+ 	statusMail +" <br/>"+
		statusMail+" By:"+ 	tech.toLowerCase() + " <br/>";
		
//	"Solution/"+statusMail+" Comment:"+ requestDescription+	" <br/>"+
//	"Title:"+	Description +" <br/>"+
//	"Thank you,<br/>"+
//	"Customer Care Centre<br/>";
	}
	
	}
	catch(Exception e)
	{
		System.out.println("Error fetching emails Info email->>");
		e.printStackTrace();
	}finally{
		closeConnection(con,ps,rs);
	}
	return mailDescription;
}
	
	
	public void SimpleMailWithAttachment(String url,String subject,String msgBody,String to)
	{
		try
 	{
		Properties prop = new Properties();
		File file = new File(url+"\\LegendPlus.properties");
		System.out.print("Absolute Path:>>> "+file.getAbsolutePath());
		System.out.print("Able to load file ");
		FileInputStream in = new FileInputStream(file);
		prop.load(in);
		System.out.print("Able to load properties into prop");
		String host = prop.getProperty("mail.smtp.host"); 
		String email = prop.getProperty("mail-user");
		String password = prop.getProperty("mail-password");
		String port = prop.getProperty("mail.smtp.port");
		boolean sessionDebug = true;
		 
	Properties props = new Properties();
	props.put("mail.smtp.user", email);
	props.put("mail.smtp.host", host);
	props.put("mail.smtp.port", port);
	props.put("mail.smtp.starttls.enable","true");
	props.put("mail.smtp.auth", "true");
	props.put("mail.smtp.socketFactory.port", port);
	props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
	props.put("mail.smtp.socketFactory.fallback", "false");
	 
//	SecurityManager security = System.getSecurityManager();
	 
	Session session = Session.getDefaultInstance(prop,null);
	
//	Authenticator auth = new SMTPAuthenticator();
//	Session session = Session.getInstance(props, auth);
	session.setDebug(true);
	System.out.println("===email=== > "+email);
	String recepientEmail=to;
String recepient[]=recepientEmail.split(";");
String cc = recepient[0];

	MimeMessage msg = new MimeMessage(session); 
	msg.setText(msgBody);
	msg.setSubject(subject);
	msg.setFrom(new InternetAddress(email));
	msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

	for(int i=0;i<recepient.length;i++)
{
InternetAddress addressCopy = new InternetAddress(recepient[i]);
msg.setRecipient(Message.RecipientType.CC, addressCopy);
}
	
	Transport.send(msg);
	}
	catch (Exception ex)
	{
	ex.printStackTrace();
	}



	}
	 

	
	public void sendStopChequeEmail(String email, String accountName)
		{
			System.out.println("Just called the sendApprovalEmail API");
			try
			{
				Properties prop = new Properties();
				File file = new File("C:\\Property\\CheckPoint.properties");
				System.out.print("Absolute Path:>>> "+file.getAbsolutePath());
				System.out.print("Able to load file ");
				FileInputStream in = new FileInputStream(file);
				prop.load(in);
				System.out.print("Able to load properties into prop");
				String host = prop.getProperty("mail.smtp.host");
				String from = prop.getProperty("mail-user");
				Session session = Session.getDefaultInstance(prop,null);
				
				boolean sessionDebug = true;
				Properties props = System.getProperties();
				props.put("mail.host",host);
				props.put("mail.transport.protocols","smtp");
				System.out.println("setting auth");
				session = Session.getDefaultInstance(props,null);
				session.setDebug(sessionDebug);
 
				System.out.println("From = "+from);
				System.out.println("point 1");
				Message msg = new MimeMessage(session);
				System.out.println("point 2");
				msg.setFrom(new InternetAddress(from));
				System.out.println("point 3");
				InternetAddress[] address = { new InternetAddress(email) };
				System.out.println("point 4");
				msg.setRecipients(Message.RecipientType.TO,address);
                /*
				 if (serviceBus.doesContactExist(data.getAccountNo()))
				    {
				    	String acctOfficerEmail = serviceBus.findContactEmailByAcctNo(data.getAccountNo());
				    	System.out.print("The acct officer email address:>>>"+acctOfficerEmail);
				    	if (!acctOfficerEmail.equalsIgnoreCase(""))
				    	{
					    	InternetAddress addressCopy = new InternetAddress(acctOfficerEmail);
					    	msg.setRecipient(Message.RecipientType.CC, addressCopy);
				    	}
				    }
				 */
				 msg.setSubject("Stop Cheque Success Notification");

				System.out.println("point 6");
				msg.setSentDate(new Date());
				System.out.println("point 7");

				String msgBody = "Dear Customer, \n";
			    msgBody += "Notification of successful\n";
			    msgBody += "Cheque Stopped by user "+accountName+".\n";
			    System.out.print("The mail body: "+msgBody);
			    msg.setText(msgBody);
			    msg.saveChanges();
				
				System.out.println("point 8");
				
			   
			    		
				    	InternetAddress addressCopy = new InternetAddress("francis.odukuye@ubagroup.com");
				    	
				    	msg.setRecipient(Message.RecipientType.CC, addressCopy);
			    	
			   
			    System.out.println("point 13");
		Transport tr = session.getTransport("smtp");
		tr.connect();
		tr.sendMessage(msg, msg.getAllRecipients());
		tr.close(); 
		System.out.println("point 15");
			} 
			catch (Exception ex) 
			{
				ex.printStackTrace();
			}

}	 


public void sendMail(String email, String subject,String msgText1,String url, String userId)
{
	System.out.println("Just called the sendApprovalEmail API");
	try
	{
			 String to=	app.getCodeName("select email from am_gb_user where user_id='"+userId+"'");
		Properties prop = new Properties();
	//	File file = new File("C:\\Property\\LegendPlus.properties");
		File file = new File(url+"\\LegendPlus.properties");
		System.out.print("Absolute Path:>>> "+file.getAbsolutePath());
		System.out.print("Able to load file ");
		FileInputStream in = new FileInputStream(file);
		prop.load(in);
		System.out.print("Able to load properties into prop");
		String host = prop.getProperty("mail.smtp.host"); 
		//String email = prop.getProperty("mail-user");
		//email = From_mail; 
		String password = prop.getProperty("mail-password");
		String port = prop.getProperty("mail.smtp.port");
		String auth1 = prop.getProperty("enable-authentication");
		String socketFactory = prop.getProperty("mail.smtp.socketFactory.class");
		String debug = prop.getProperty("mail.smtp.debug");
		String fallback = prop.getProperty("mail.smtp.socketFactory.fallback");
		String starttsl = prop.getProperty("mail.smtp.starttls.enable");
		String from = prop.getProperty("mail.From.address");
		String Copy = prop.getProperty("mail.Copy.address");
		String Host_Mail =  prop.getProperty("mail-user");	
	 	String recepientEmail=Copy;
		boolean sessionDebug = true;
		System.out.println("===host=== "+host);
		System.out.println("===password=== "+password);
		System.out.println("===port=== "+port);
		System.out.println("===Host_Mail=== "+Host_Mail);
	Properties props = new Properties();
	//props.put("mail.smtp.user", from);
	props.put("mail.smtp.user", Host_Mail);
	props.put("mail.smtp.host", host);
	props.put("mail.smtp.port", port);
	props.put("mail.smtp.starttls.enable",starttsl);
	props.put("mail.smtp.auth", auth1);
	props.put("mail.smtp.socketFactory.port", port);
	props.put("mail.smtp.socketFactory.class",socketFactory);
	props.put("mail.smtp.socketFactory.fallback", fallback);
	Session session = Session.getDefaultInstance(prop,null);
	
//	Authenticator auth = new SMTPAuthenticator();
//	Session session = Session.getInstance(props, auth);
	session.setDebug(true);
	System.out.println("====To Address=== "+to);
String recepient[]=recepientEmail.split(";");
String cc = recepient[0]; 
System.out.print("From Addresss Mail "+from);
///	

System.out.println("====recepient[0]=== "+recepient[0]);
	MimeMessage msg = new MimeMessage(session); 
	msg.setText(msgText1);
	msg.setSubject(subject);
	//msg.setFrom(new InternetAddress(email));
	msg.setFrom(new InternetAddress(to));
	msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
System.out.println("====msgBody=== "+msgText1);
System.out.println("====subject=== "+subject);
int Recipient_length = recepient.length;
System.out.println("====Recipient_length=== "+Recipient_length);
	for(int i=0;i<(recepient.length - 1);i++)
{
InternetAddress addressCopy = new InternetAddress(recepient[i]);
System.out.println("====addressCopy=== "+Copy);
//System.out.println("====recepient.length=== "+recepient.length);

msg.setRecipient(Message.RecipientType.CC, addressCopy);
 

}
	
	Transport.send(msg);
	}
	catch (Exception ex)
	{
	ex.printStackTrace();
	}	 	

}	 	
	
public void SimpleMailWithAttachment2(String url,String subject,String msgBody,String to)
	{
		try
 	{
		Properties prop = new Properties();
		File file = new File(url+"\\LegendPlus.properties");
	//	System.out.print("Absolute Path:>>> "+file.getAbsolutePath());
	//	System.out.print("Able to load file ");
		FileInputStream in = new FileInputStream(file);
		prop.load(in);
	//	System.out.print("Able to load properties into prop");
		String host = prop.getProperty("mail.smtp.host"); 
		String email = prop.getProperty("mail-user");
		String password = prop.getProperty("mail-password");
		String port = prop.getProperty("mail.smtp.port");
		boolean sessionDebug = true;
		 
	Properties props = new Properties();
	props.put("mail.smtp.user", email);
	props.put("mail.smtp.host", host);
	props.put("mail.smtp.port", port);
	props.put("mail.smtp.starttls.enable","true");
	props.put("mail.smtp.auth", "true");
	props.put("mail.smtp.socketFactory.port", port);
	props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
	props.put("mail.smtp.socketFactory.fallback", "false");
	 
//	SecurityManager security = System.getSecurityManager();
	 
	Session session = Session.getDefaultInstance(prop,null);

//	Authenticator auth = new SMTPAuthenticator();
//	Session session = Session.getInstance(props, auth);
	session.setDebug(false);
	
	/* 
	MimeMessage msg = new MimeMessage(session); 
	msg.setText(msgBody);
	msg.setSubject(subject);
	msg.setFrom(new InternetAddress(email));
	msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

	Transport.send(msg);
	*/
	  Message[] messages = null;
  Store store = session.getStore("pop3");
  store.connect(host,email,password); 
  System.out.println("-----"+store.isConnected());
  Folder folder = store.getFolder("inbox"); 
 
 
  folder.open(Folder.READ_ONLY);
  System.out.println("-----"+folder.isOpen()); 
  messages = folder.getMessages();

  for(int x = 0 ; x < messages.length; x++)
   {
 	  messages[x].getContent();
	 
	 
//	 messages[x].getFrom();
	 
	// System.out.println("---------------"+x);
	  }
 
  folder.close(false);
	}
	catch (Exception ex)
	{
	ex.printStackTrace();
	}


	}	
  
 private static String content(Message message)throws Exception{

		try{
			return (String)((MimeMessage)message).getContent();
		}catch(Exception e){
			throw new RuntimeException(e); 
		}
  }
	   public static void main(String args[])
	     {  
	 	  com.magbel.admin.mail.EmailSmsServiceBus mail = new com.magbel.admin.mail.EmailSmsServiceBus(); 
	//  mail.sendRegistrationEmail("oreolanrewaju@gmail.com", "id", "role", "L:\\jboss-4.0.5.GA\\server\\epostserver\\deploy\\HelpDesk.war\\");
	    mail.SimpleMailWithAttachment("D:\\wildfly-26.1.1.Final\\standalone\\deployments\\legendPlus.war\\", "subject", "msgBody", "oreolanrewaju@gmail.com");
	     }	 
	   
		public String getEmailMessageThread(String transactionType,String categoryCode,String subcategory, String helpType,String technician,String user,String requestDescription,String complaintId,String requestSubject, String FileName,String FieldName)
		{    
		//	System.out.println("TESTMAIL Thread Mail");
			String query = " SELECT mail_description,mail_heading,mail_address,Status  FROM HD_MAIL_STATEMENT  WHERE  transaction_Type = '"+transactionType.trim()+"'"+
			"  And category_Code = '"+categoryCode.trim()+"'  And help_Type = '"+helpType.trim()+"'  " ;		
	//		System.out.println("--query->> 1 "+query);  
			String mailDescription =null;
			String mailHeading=null;
			String mailAddress=null;
			String status=null;
			Connection con = null; 
		    MagmaDBConnection mgDbCon = null;
			PreparedStatement ps = null;
			com.magbel.util.DatetimeFormat df;
			df = new com.magbel.util.DatetimeFormat();
			ResultSet rs = null;
			String Heading = "";
			String Description ="";
			String Fld1 = "";
			String FldDesc = "";
			String FldDesc2 = "";
			String IssueSubCategory = "";
			String IssueId = "";
			String IssueCategory = "";
			try
			{ 
				mgDbCon = new MagmaDBConnection();
				con = mgDbCon.getConnection("legendPlus");
				//con = getConnection("helpDesk"); 
				ps = con.prepareStatement(query); 
				rs = ps.executeQuery();
		//		System.out.println("--query->>"+query);  
				if(rs.next()){
					mailDescription = rs.getString("mail_description");
					mailHeading=rs.getString("mail_heading");
					mailAddress=rs.getString("mail_address");
				//     System.out.println("=======mailHeading>>>>>>> "+mailHeading);
				//     System.out.println("=======mailDescription>>>>>>> "+mailDescription);	
				     String[] Mail_Head = mailHeading.split(";");
				     String[] Mail_Description = mailDescription.split(";");
				     status=rs.getString("Status");
				     int i = 0; int j = 0;
				     int sizelentHeading = mailHeading.split(";").length;  
				     int sizelentDescr = mailDescription.split(";").length;  
				//     System.out.println("=======Heading 0 >>>> "+Mail_Head[0]+"==Index i == "+i+"===sizelentHeading== "+sizelentHeading);
				     if (i < sizelentHeading){
				//    	 System.out.println("=======Heading 0 >>>> "+Mail_Head[0]+"==Index i == "+i+"===sizelentHeading== "+sizelentHeading);
				     if (Mail_Head[0].trim().equalsIgnoreCase("COMPLAINT_ID")){
				    	 Mail_Head[0]=findObject("SELECT complaint_id, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");							   
				     }		
				     if (Mail_Head[0].trim().equalsIgnoreCase("complain_category")&& i < sizelentHeading){
				    	 Mail_Head[0]=findObject("SELECT Dept_name, UnitHead   FROM   AM_AD_DEPARTMENT WHERE Dept_code = '"+categoryCode+"'");						    	 
				     }		
				     if (Mail_Head[0].trim().equalsIgnoreCase("complain_sub_category")){
				    	 Mail_Head[0]=findObject("SELECT sub_category_name, sub_category_desc   FROM   HD_COMPLAIN_SUBCATEGORY WHERE sub_category_code = '"+subcategory+"'");							     
				     }
				     Heading = Mail_Head[0];  
				     
				     }  
				     i = i + 1;
				     if (i < sizelentHeading){ 
				   // 	 System.out.println("====complaintId===== "+complaintId);
				    //	 System.out.println("====Mail_Head[1].trim()===== "+Mail_Head[1].trim());
				     if (Mail_Head[1].trim().equalsIgnoreCase("COMPLAINT_ID")){
				    	 Mail_Head[1]=findObject("SELECT complaint_id, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
				    //	  System.out.println("===Heading=== 1 complaintId "+complaintId);
				     }			     			     
				     if (Mail_Head[1].trim().equalsIgnoreCase("complain_category")){
				    	 Mail_Head[1]=findObject("SELECT Dept_name, UnitHead   FROM   AM_AD_DEPARTMENT WHERE Dept_code = '"+categoryCode+"'");
				    	 System.out.println("===Heading=== 1 categoryCode "+categoryCode);
				     }		
				     if (Mail_Head[1].trim().equalsIgnoreCase("complain_sub_category")){
				    	 Mail_Head[1]=findObject("SELECT sub_category_name, sub_category_desc   FROM   HD_COMPLAIN_SUBCATEGORY WHERE sub_category_code = '"+subcategory+"'");
				   // 	 System.out.println("===Heading=== 1 subcategory "+subcategory);
				     }	
				     Heading = Heading + " " + Mail_Head[1];
				     System.out.println("===Heading=== 2 "+Heading);
				     }    
				     i = i + 1;  
				     if (i < sizelentHeading){
				     if (Mail_Head[2].trim().equalsIgnoreCase("COMPLAINT_ID")){
				    	 Mail_Head[2]=findObject("SELECT complaint_id, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");								  
				     }			     			     
				     if (Mail_Head[2].trim().equalsIgnoreCase("complain_category")){
				    	// String Dept_Code=util.findObject("SELECT complain_category, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
				    	 Mail_Head[2]=findObject("SELECT Dept_name, UnitHead   FROM   AM_AD_DEPARTMENT WHERE Dept_code = '"+categoryCode+"'");
				     }			
				     if (Mail_Head[2].trim().equalsIgnoreCase("complain_sub_category")){
				    	// String SubCatCode=util.findObject("SELECT complain_sub_category, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
				    	 Mail_Head[2]=findObject("SELECT sub_category_name, sub_category_desc   FROM   HD_COMPLAIN_SUBCATEGORY WHERE sub_category_code = '"+subcategory+"'");
				     }	
				     Heading = Heading + Mail_Head[2];
				     System.out.println("===Heading=== 3 "+Heading);
				     }  
				    // i = i + 1;   
				     if (j < sizelentDescr){
				     if (Mail_Description[0].trim().equalsIgnoreCase("COMPLAINT_ID")){
				    	 Mail_Description[0]=findObject("SELECT complaint_id, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
				     }			     			     
				     if (Mail_Description[0].trim().equalsIgnoreCase("complain_category")){
				    	// String Dept_Code=util.findObject("SELECT complain_category, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
				    	 Mail_Description[0]=findObject("SELECT Dept_name, UnitHead   FROM   AM_AD_DEPARTMENT WHERE Dept_code = '"+categoryCode+"'");
				     }		
				     if (Mail_Description[0].trim().equalsIgnoreCase("complain_sub_category")){
				    	// String SubCatCode=util.findObject("SELECT complain_sub_category, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
				    	 Mail_Description[0]=findObject("SELECT sub_category_name, sub_category_desc   FROM   HD_COMPLAIN_SUBCATEGORY WHERE sub_category_code = '"+subcategory+"'");
				     }
				     Description =  Mail_Description[0];						     
				     }  	
				     j = j + 1;
				     if (j < sizelentDescr){
				     if (Mail_Description[1].trim().equalsIgnoreCase("COMPLAINT_ID")){
				    	 Mail_Description[1]=findObject("SELECT complaint_id, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");								  
				     }		
				     System.out.println("===Mail_Description[1].trim()=== 1 "+Mail_Description[1].trim());
				     if (Mail_Description[1].trim().equalsIgnoreCase("complain_category")){
				    //	 String Dept_Code=util.findObject("SELECT complain_category, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
				    	 Mail_Description[1]=findObject("SELECT Dept_name, UnitHead   FROM   AM_AD_DEPARTMENT WHERE Dept_code = '"+categoryCode+"'"); 
				     }		
				     if (Mail_Description[1].trim().equalsIgnoreCase("complain_sub_category")){
				    	// String SubCatCode=util.findObject("SELECT complain_sub_category, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
				    	 Mail_Description[1]=findObject("SELECT sub_category_name, sub_category_desc   FROM   HD_COMPLAIN_SUBCATEGORY WHERE sub_category_code = '"+subcategory+"'");
				     }	
				     Description = Description + " " + Mail_Description[1];
				     System.out.println("===Description=== 2 "+Description);
				     }  	
				     j = j + 1;
				     if (j < sizelentDescr){
				     if (Mail_Description[2].trim().equalsIgnoreCase("COMPLAINT_ID")){
				    	 Mail_Description[2]=findObject("SELECT complaint_id, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");								  
				     }			     			     
				     if (Mail_Description[2].trim().equalsIgnoreCase("complain_category")){
				    	// String Dept_Code=util.findObject("SELECT complain_category, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
				    	 Mail_Description[2]=findObject("SELECT Dept_name, UnitHead   FROM   AM_AD_DEPARTMENT WHERE Dept_code = '"+categoryCode+"'");
				     }		
				     if (Mail_Description[2].trim().equalsIgnoreCase("complain_sub_category")){
				    //	 String SubCatCode=util.findObject("SELECT complain_sub_category, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
				    	 Mail_Description[2]=findObject("SELECT sub_category_name, sub_category_desc   FROM   HD_COMPLAIN_SUBCATEGORY WHERE sub_category_code = '"+subcategory+"'");
				     }	
				     Description = Description + " " + Mail_Description[2];	
				     System.out.println("===Description=== 3 "+Description);
				     } 
				} 
				String ResolvedBy = "";
			String Assignee=findObject("SELECT technician, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
			System.out.println("=====Assignee ===== "+Assignee);
			if (Assignee==null || Assignee.equals("")){
				 ResolvedBy=findObject("SELECT UnitHead, email   FROM   AM_AD_DEPARTMENT WHERE Dept_code = '"+categoryCode+"'");
			}else{
			ResolvedBy =findObject("select full_name from am_gb_user where user_id  = '"+Assignee+"'");
			}  
			//System.out.println("=====ResolvedBy ===== "+ResolvedBy);
			String statusMail=findObject("select status_description from hd_status where status_code='"+transactionType+"'");
		//	System.out.println("=====statusMail ===== "+statusMail);
			String	Subject="Subject: Notification for Issue : "+complaintId;
			String tech=findObject("select full_name from am_gb_user where user_id='"+user+"'");		
				tech=tech=="UNKNOWN" ? "work in progress" :tech;
		//		System.out.println("=====tech ===== "+tech);
			String dateReported=findObject("select create_date from "+FileName+" where "+FieldName+"='"+complaintId+"'");	
			dateReported=dateReported=="UNKNOWN" ? String.valueOf(df.dateConvert(new java.util.Date())) :dateReported;
		//	System.out.println("=====dateReported ===== "+dateReported);
			mailDescription=
	        "Subject: "+Heading+" \n"+			
			"Hi,  \n"+  
			"The issue "+complaintId+" has been "+statusMail+".\n"+ 
			"Details are as follows :- \n"+
			"Issue Id: 	"+complaintId+" \n"+
			"Title:"+	requestSubject +" \n"+
			"Description:"+ 	requestDescription +" \n" + 
			"Created By:"+ 	tech.toLowerCase() + " \n"+
			"Date Reported:"+ 	dateReported+" \n"+
			"Issue Current Status:"+ 	statusMail +" \n"+
			"Resolved By:"+ 	ResolvedBy.toLowerCase() + " \n"+
			"Date "+statusMail+":"+ 	df.dateConvert(new java.util.Date())+"\n"+
			"Solution/"+statusMail+" Comment:"+ requestDescription+	" \n"+
			"Title:"+	Description +" \n"+
			"Thank you,\n"+
			"Customer Care Centre\n";

			}
			catch(Exception e)
			{
				System.out.println("Error fetching branch emails email->>");
				e.printStackTrace();
			}finally{
				closeConnection(con,ps,rs);
			}
			return mailDescription;
		}
		
	    public String findObject(String query)
	    {
//	System.out.println("=====query==> "+query);
	        Connection Con2 = null;
	        PreparedStatement Stat = null;
	        ResultSet result = null;
	        String found = null;

	        String finder = "UNKNOWN";

	        double sequence = 0.00d;
	        try {

	            Con2 = new DataConnect("legendPlus").getConnection();
	            Stat = Con2.prepareStatement(query);
	            result = Stat.executeQuery();

	            while (result.next()) {
	                finder = result.getString(1);
	            }

	        } catch (Exception ee2) {
	            System.out.println("WARN:ERROR OBTAINING OBJ --> " + ee2);
	            ee2.printStackTrace();
	        } finally {
	            closeConnection(Con2, Stat, result);
	        }

	        return finder;
	    }

		
		public void closeConnection(Connection con, Statement ps,ResultSet rs) 
		{
			try 
			{
				if (ps != null) 
				{
					ps.close();
				}
				
				if (rs != null)
				{
					rs.close();
				}
				
				if (con != null) 
				{
					con.close();
				}
			}
			catch (Exception ex) 
			{
				System.out.println("WARNING:Error closing Connection ->" + ex);
			}
		}
		

}
