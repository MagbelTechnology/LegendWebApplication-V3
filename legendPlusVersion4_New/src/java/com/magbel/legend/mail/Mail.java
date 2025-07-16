package com.magbel.legend.mail;

//set CLASSPATH=%CLASSPATH%;activation.jar;mail.jar
import javax.mail.*;
import javax.activation.*;
import javax.mail.internet.*;
import java.util.*;
import java.io.*;

public class Mail
{
    //static
  
	public synchronized static boolean sendMail(String userName,String passWord,String host,String port,String starttls,String auth,boolean debug,String socketFactoryClass,String fallback,String from,String to,String cc,String[] bcc,String subject,String text,String filename)
	{
		System.out.println("*********************************************");
		Properties props = new Properties();
		//Properties props=System.getProperties();
       // props.put("mail.smtp.user", userName);
		//filename="C:\\Documents and Settings\\Development-01\\Desktop\\ubad.txt";
		System.out.println("******************>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
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

		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		
		
		 try
        {
	        Session session = Session.getDefaultInstance(props, null);
            session.setDebug(debug);
            MimeMessage msg = new MimeMessage(session);
            msg.setText(text);
            msg.setSubject(subject);
            msg.setFrom(new InternetAddress(from));
			
           //                                              msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
           
            String[] cc1 =cc.split(",");
            for(int i=0;i<cc1.length;i++)
                {
                msg.addRecipient(Message.RecipientType.CC, new InternetAddress(cc1[i]));
    			}
           
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
//			  MimeBodyPart mbp2 = new MimeBodyPart();

					// attach the file to the message
//			  FileDataSource fds = new FileDataSource(filename);
//			  mbp2.setDataHandler(new DataHandler(fds));
//			  mbp2.setFileName(fds.getName());

			  // create the Multipart and add its parts to it
			  Multipart mp = new MimeMultipart();
			  mp.addBodyPart(mbp1);
//			  mp.addBodyPart(mbp2);

			  // add the Multipart to the message
			  msg.setContent(mp);

			  // set the Date: header
            msg.setSentDate(new Date());

            msg.saveChanges();
			Transport transport = session.getTransport("smtp");
			transport.connect(host, userName, passWord);
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();
			return true;
        }catch (Exception mex)
        {
            mex.printStackTrace();
	    return false;
        }
	}

	public synchronized static boolean sendEMail(String userName,String passWord,String host,String port,String starttls,String auth,boolean debug,String socketFactoryClass,String fallback,String from,String to,String[] cc,String[] bcc,String subject,String text,String filename)
	{
		System.out.println("*********************************************");
		Properties props = new Properties();
		System.out.println("******************>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
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

		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+to);
		
		
		 try
        {
	        Session session = Session.getDefaultInstance(props, null);
            session.setDebug(debug);
            MimeMessage msg = new MimeMessage(session);
            msg.setText(text);
            msg.setSubject(subject);
            msg.setFrom(new InternetAddress(from));
			
              msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
   
			  MimeBodyPart mbp1 = new MimeBodyPart();
			  mbp1.setText(text);

			  Multipart mp = new MimeMultipart();
			  mp.addBodyPart(mbp1);
			  msg.setContent(mp);
              msg.setSentDate(new Date());

            msg.saveChanges();
			Transport transport = session.getTransport("smtp");
			transport.connect(host, userName, passWord);
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();
			return true;
        }catch (Exception mex)
        {
            mex.printStackTrace();
	    return false;
        }
	}
}

