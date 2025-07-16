package com.magbel.admin.mail;

//set CLASSPATH=%CLASSPATH%;activation.jar;mail.jar 
import javax.mail.*;
import javax.activation.*;
import javax.mail.internet.*;
import java.util.*;
import java.io.*;
//import org.apache.james.userrepository.DefaultJamesUser;
//import org.apache.james.services.usersStore;

public class Mail extends Authenticator 
{
    //static
    //protected PasswordAuthetication aut;
    public static void main(String[] args) throws Exception{
    
		//String[] to={"lekan_matanmi@lycos.com","lekanmatanmi@magbeltech.com"};

		String[] cc={"XXX@yahoo.com"};
		String[] bcc={"XXX@yahoo.com"};
		//This is for google

		String  from = args[0];
		String subject = args[1];
		String message = args[2];
		String[] to = args[3].split(",");
		String filename = args[4];

		Properties props = new Properties();
		InputStream in = (InputStream)(new FileInputStream(new File("db-config.properties")));
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
		//Mail.sendMail(authenticator,password,host,port,starttsl,auth,new Boolean(debug).booleanValue(),socketFactory,fallback,from,to,cc,bcc,subject,message,filename);
		//Mail.sendMail(authenticator,"locus1",host,port,starttsl,auth.trim(),new Boolean(debug.trim()).booleanValue(),socketFactory.trim(),fallback.trim(),from,to,cc,bcc,subject,message,filename);
    }







	public synchronized static boolean sendMail(String userName,String passWord,String host,String port,String starttls,String auth,boolean debug,String socketFactoryClass,String fallback,String from,String to,String[] cc,String[] bcc,String subject,String text,String filename)
	{
		System.out.println("in send Mail>>>>>>>>>>");
            
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
          
	    Session session = Session.getDefaultInstance(props,null );
            session.setDebug(debug);
            MimeMessage msg = new MimeMessage(session);
            msg.setText(text);
            msg.setSubject(subject);
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
			transport.connect(host, userName, passWord);
                  if(transport.isConnected())
                   {
			  System.out.println("host connect");
			  transport.sendMessage(msg, msg.getAllRecipients());
			 }
                   transport.close();
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
	
	public synchronized static boolean sendMail(String userName,String passWord,String host,String port,String starttls,String auth,boolean debug,String socketFactoryClass,String fallback,String from,String to,String[] cc,String[] bcc,String subject,String text)
	{
		System.out.println("in send Mail>>>>>>>>>>");
            
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
          
	    Session session = Session.getDefaultInstance(props,null );
            session.setDebug(debug);
            MimeMessage msg = new MimeMessage(session);
            msg.setText(text);
            msg.setSubject(subject);
            msg.setFrom(new InternetAddress(from));
            
            msg.setSender(new InternetAddress(userName));
            //msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            
            msg.addRecipient(Message.RecipientType.CC, new InternetAddress(to));
    			 
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
			//---  MimeBodyPart mbp1 = new MimeBodyPart();
			//---  mbp1.setText(text);

			  // create the second message part
			//---  MimeBodyPart mbp2 = new MimeBodyPart();

				// attach the file to the message
			//  FileDataSource fds = new FileDataSource(filename);
			//  mbp2.setDataHandler(new DataHandler(fds));
			//  mbp2.setFileName(fds.getName());

			  // create the Multipart and add its parts to it
			//---  Multipart mp = new MimeMultipart();
			//---  mp.addBodyPart(mbp1);
			//---  mp.addBodyPart(mbp2);

			  // add the Multipart to the message
			//---  msg.setContent(mp);

			  // set the Date: header
            	msg.setSentDate(new Date());

                msg.saveChanges();
                
			Transport transport = session.getTransport("smtp");
			transport.connect(host, userName, passWord);
                  if(transport.isConnected())
                   {
			  System.out.println("host connect"); 
			//  transport.send(msg);
			  
			  transport.sendMessage(msg, msg.getAllRecipients());
			 }
                   transport.close(); 

			return true;

        }catch (Exception mex)
        {
            mex.printStackTrace();

	    	return false;
        }
	}
	
	public synchronized static boolean sendMail2(String userName,String passWord,String host,String port,String starttls,String auth,boolean debug,String socketFactoryClass,String fallback,String from,String to,String[] cc,String[] bcc,String subject,String text)
	{
		System.out.println("in send Mail>>>>>>>>>>");
            
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
          
	    Session session = Session.getDefaultInstance(props,null );
            session.setDebug(debug);
            MimeMessage msg = new MimeMessage(session);
            msg.setText(text);
            msg.setSubject(subject);
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
			//---  MimeBodyPart mbp1 = new MimeBodyPart();
			//---  mbp1.setText(text);

			  // create the second message part
			//---  MimeBodyPart mbp2 = new MimeBodyPart();

				// attach the file to the message
			// --- FileDataSource fds = new FileDataSource(filename);
			// --- mbp2.setDataHandler(new DataHandler(fds));
			// --- mbp2.setFileName(fds.getName());

			  // create the Multipart and add its parts to it
			//---  Multipart mp = new MimeMultipart();
			//---  mp.addBodyPart(mbp1);
			//---  mp.addBodyPart(mbp2);

			  // add the Multipart to the message
			//---  msg.setContent(mp);

			  // set the Date: header
            	msg.setSentDate(new Date());

                msg.saveChanges();
                
			Transport transport = session.getTransport("smtp");
			transport.connect(host, userName, passWord);
                  if(transport.isConnected())
                   {
			  System.out.println("host connect");
			 // transport.send(msg);
			   transport.sendMessage(msg, msg.getAllRecipients());
			 }
                   transport.close(); 

			return true;

        }catch (Exception mex)
        {
            mex.printStackTrace();

	    	return false;
        }
	} 
	
	public synchronized static boolean sendMailAuthenticate(String userName,String passWord,String host,String port,String starttls,String auth,boolean debug,String socketFactoryClass,String fallback,String from,String to,String[] cc,String[] bcc,String subject,String text)
	{
		System.out.println("in send Mail>>>>>>>>>>");
        boolean result= false;   
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
          
	    Session session = Session.getDefaultInstance(props,null );
            session.setDebug(debug);
            MimeMessage msg = new MimeMessage(session);
            msg.setText(text);
            msg.setSubject(subject);
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
			//---  MimeBodyPart mbp1 = new MimeBodyPart();
			//---  mbp1.setText(text);

			  // create the second message part
			//---  MimeBodyPart mbp2 = new MimeBodyPart();

				// attach the file to the message
			// --- FileDataSource fds = new FileDataSource(filename);
			// --- mbp2.setDataHandler(new DataHandler(fds));
			// --- mbp2.setFileName(fds.getName());

			  // create the Multipart and add its parts to it
			//---  Multipart mp = new MimeMultipart();
			//---  mp.addBodyPart(mbp1);
			//---  mp.addBodyPart(mbp2);

			  // add the Multipart to the message
			//---  msg.setContent(mp);

			  // set the Date: header
            	msg.setSentDate(new Date());

                msg.saveChanges();
                
			Transport transport = session.getTransport("smtp");
			transport.connect(host, userName, passWord);
                  if(transport.isConnected())
                   {
			  System.out.println("host connect");
			  result= true;
			 // transport.send(msg);
			 //  transport.sendMessage(msg, msg.getAllRecipients());
			 }
                   transport.close(); 

			

        }catch (Exception mex)
        {
            mex.printStackTrace();

            result= false;
        }
        return result;
	}
}

