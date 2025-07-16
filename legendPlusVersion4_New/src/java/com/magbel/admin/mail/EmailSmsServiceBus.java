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

package com.magbel.admin.mail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.FetchProfile;  
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;  
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage; 

import com.magbel.admin.dao.MagmaDBConnection; 
import com.magbel.admin.mail.MailClient;
 
import javax.mail.PasswordAuthentication;
 

import java.text.*;

import com.simplewire.sms.*;
import com.magbel.util.*;
 

public class EmailSmsServiceBus extends MagmaDBConnection 
 { 
	NumberFormat format = new DecimalFormat("0.00");
	HtmlUtility util;
    SimpleDateFormat timer = null;
	HtmlComboRecord htmlcombo = new HtmlComboRecord(); 
	MailClient send = new MailClient();
	//String email="taofeek.gidado@valucardnigeria.com";
	String email="magbeltech@gmail.com";
//	 String //From_mail=	htmlcombo.describeCode("select email from am_gb_user where user_id='"+userId+"'");
	//String password="vnp-2001";
	String password="lmblagos"; 
	com.magbel.util.DatetimeFormat df;
	public EmailSmsServiceBus()
		{
		timer = new SimpleDateFormat("kk:mm:ss");
		}
	public void sendEmail(String email, String recipient,String msgBody,String url)
	{
		
		System.out.println("Just called the sendApprovalEmail API");
		//ControlParameter ctrlParam = new ControlParameter();
		//ctrlParam = findCtrlParamValueByCode("PROPERTY_FILE_LOC");
		try
		{
			//File file = new File(ctrlParam.getParameterValue()+"\\CheckPoint.properties");
			Properties prop = new Properties();
			//System.out.print("D:\\Property\\CheckPoint.properties");
			File file = new File(url+"\\helpdesk.properties");
			System.out.print("Absolute Path:>>> "+file.getAbsolutePath()); 
			//if(file.exists())
			//{
			
			System.out.print("loading file.................. "); 
			FileInputStream in = new FileInputStream(file);
			System.out.print("Able to load file ");
			//InputStream in = this.getClass().getResourceAsStream("CheckPoint.properties"); //(InputStream)(new FileInputStream(file));
			prop.load(in);
			System.out.print("Able to load properties into prop");
			String host = prop.getProperty("mail.smtp.host");
			String from = prop.getProperty("mail-user");
			String password = prop.getProperty("mail-password");
			String port = prop.getProperty("mail.smtp.port");
			String starttls = prop.getProperty("mail.smtp.starttls.enable");
			String socketFactoryClass = prop.getProperty("mail.smtp.socketFactory.class");
			String auth = prop.getProperty("enable-authentication");
			String strDebug = prop.getProperty("mail.smtp.debug");
			boolean debug = new Boolean(strDebug.trim());
			String fallback = prop.getProperty("mail.smtp.socketFactory.fallback");
			
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
	
			if(!"".equals(socketFactoryClass))
	        props.put("mail.smtp.socketFactory.class",socketFactoryClass);
			if(!"".equals(fallback))
	        props.put("mail.smtp.socketFactory.fallback", fallback);
			
			Session session = Session.getInstance(props, null);
			session.setDebug(debug);
		
		    // create a message
			System.out.println("About the send the mail>>>>>inside sendApprovalEmail API");
		    Message msg = new MimeMessage(session);
		    msg.setFrom(new InternetAddress(from));
		    InternetAddress address = new InternetAddress(email);
		    //InternetAddress addressBlindCopy = new InternetAddress(blindCopyAddress);
		    msg.setRecipient(Message.RecipientType.TO, address);
		    InternetAddress addressCopy = new InternetAddress(recipient);
	    	msg.setRecipient(Message.RecipientType.CC, addressCopy);
		    //msg.setRecipient(Message.RecipientType.BCC, addressBlindCopy);
		    msg.setSubject("Check Confirmation Notification");
		    msg.setSentDate(new Date());
		    		    
		/*    String msgBody = "Dear " + data.getAccountName() +", \n";
		    msgBody += "The confirmation of your cheque has been recieved.\n";
		    msgBody += "Below are the details of the confirmed cheque:\n";
		    msgBody += "Cheque Number: "+data.getChequeNo()+"\n";
		    msgBody += "Cheque Date: "+data.getChequeDate()+"\n";
		    msgBody += "Account Number: "+data.getAccountNo()+"\n";
		    msgBody += "Amount: "+data.getAmount()+"\n";
		    msgBody += "Beneficiary: "+data.getBeneficiary()+"\n";
		    msgBody += "Mandate: "+data.getMandate()+"\n\n";
		    msgBody += "From:\n UBA Cheque Confirmation Manager";
		 */   
		    System.out.print("The mail body: "+msgBody);
		    
		    msg.setText(msgBody);
		    msg.saveChanges();
			Transport transport = session.getTransport("smtp");
			transport.connect(host, from, password);
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
	
	
		 public String readPropertyFile(String propertyKey)
		 {
			String propertyValue = ""; 
			//ControlParameter ctrlParam = new ControlParameter();
			//ctrlParam = findCtrlParamValueByCode("PROPERTY_FILE_LOC");
			try
			{
				File file = new File("CheckPoint.properties");
				if(file.exists())
				{
					
					Properties property = new Properties();
					FileInputStream input = new FileInputStream(file);
					property.load(input);
			        propertyValue = property.getProperty(propertyKey);
			        System.out.println("Property Value: >>>"+propertyValue);
			     }
			     else
			     {
			          System.out.println("File not found!");
			     }
			}catch(IOException e){
			System.out.print(e.toString());
			}
			return propertyValue;
	 }
	 
	 
	 	public void sendRegistrationEmail(String email, String id,String role,String url)
	 		{
	 			System.out.println("Just called the sendApprovalEmail API");
	 			try
	 			{
	 			//	String roleDescription =serviceBus.getCodeName2("SELECT DESCRIPTION FROM cp_gb_designation where code='"+role+"'");
	 				String roleDescription ="14";
	 				Properties prop = new Properties();
	 				File file = new File(url+"\\helpdesk.properties");
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
	 			//	System.out.println("setting auth");
	 			  	
	 			  	
	 				String port = prop.getProperty("mail.smtp.port");
	 				props.put("mail.smtp.port", port);
	 				props.put("mail.smtp.starttls.enable","true");
	 			 	props.put("mail.smtp.auth", "true");
	 				Authenticator auth = new SMTPAuthenticator();
	 			 	session = Session.getInstance(props,auth);
	 				 
	 				
	 				session.setDebug(sessionDebug);
	 
	 				System.out.println("From = "+from);
	 				System.out.println("point 1");
	 				Message msg = new MimeMessage(session);
	 				System.out.println("point 2");
	 				msg.setFrom(new InternetAddress(from));
	 				System.out.println("point 3");
	 				InternetAddress[] address = { new InternetAddress(email) };
	 				System.out.println("point 4"+email);
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
	 				 msg.setSubject("User Registration Approval Notification");
	 
	 				System.out.println("point 6");
	 				msg.setSentDate(new Date());
	 				System.out.println("point 7");
	 
	 				String msgBody = "Dear Sir/Ma, \n";
	 				msgBody += "  \n";
	 			    msgBody += "Registration of a new user.\n";
	 			    msgBody += "With User Id "+id+".\n";
	 			    msgBody += "And role "+roleDescription+"\n";
	 			    msgBody += "Approval required\n";
	 			    System.out.print("The mail body: "+msgBody);
	 			    msg.setText(msgBody);
	 			    msg.saveChanges();
	 				
	 				System.out.println("point 8");
	 				 
	 			    System.out.println("point 13");
	 		Transport tr = session.getTransport("smtp");
	 		tr.connect();
	 	//	tr.sendMessage(msg, msg.getAllRecipients());
	 		tr.send(msg);
	 		tr.close(); 
	 		System.out.println("point 15");  
	 			} 
	 			catch (Exception ex) 
	 			{
	 				ex.printStackTrace();
	 			}
	 
		}
	 	public void SimpleMailWithAttachment(String url,String subject,String msgBody,String notifymail,String userId, String mailto)
	 	{  //System.out.println("<<<<<<Inside SimpleMailWithAttachment>>>>>>>");
	 		try
		 	{    
	 			//System.out.println("url= "+url +" Subject "+subject+" subject "+subject+" userId "+userId+" mailto"+mailto);
	 			 String From_mail=	htmlcombo.describeCode("select Full_Name from am_gb_user where user_id='"+userId+"'");
	 		Properties prop = new Properties();
				File file = new File(url+"\\helpdesk.properties");
			//	System.out.println("=====file====>>>>> "+file);
			//	System.out.print("Absolute Path:>>> "+file.getAbsolutePath());
			//	System.out.print("Able to load file ");
				FileInputStream in = new FileInputStream(file);
				prop.load(in);
		//		System.out.print("Able to load properties into prop");
				String host = prop.getProperty("mail.smtp.host"); 
				String email = prop.getProperty("mail-user");
				String User = prop.getProperty("mail-user");
			//	email = "["+"ORIENTAL"+"]"+From_mail.replace(" ", ".").trim(); 
				email = From_mail.replace(" ", ".").trim()+".ORIENTAL"; 
				//email = subject;          
				System.out.print("From email Mail "+email);
				String password = prop.getProperty("mail-password");   
				String port = prop.getProperty("mail.smtp.port");
				String auth1 = prop.getProperty("enable-authentication");  
				String socketFactory = prop.getProperty("mail.smtp.socketFactory.class");
				String debug = prop.getProperty("mail.smtp.debug");
				String fallback = prop.getProperty("mail.smtp.socketFactory.fallback");
				String starttsl = prop.getProperty("mail.smtp.starttls.enable");
				boolean sessionDebug = true;
			//	System.out.print("From Addresss Mail "+From_mail);
	 	Properties props = new Properties();
	 	//props.put("mail.smtp.user", email);
	 	props.put("mail.smtp.user", email);
	 	props.put("mail.smtp.host", host);
	 	props.put("mail.smtp.port", port);
	 	props.put("mail.smtp.starttls.enable",starttsl);
	 	props.put("mail.smtp.auth", auth1);
	 	props.put("mail.smtp.socketFactory.port", port);
	 	props.put("mail.smtp.socketFactory.class",socketFactory);
	 	props.put("mail.smtp.socketFactory.fallback", fallback);
	 //	SecurityManager security = System.getSecurityManager();
	 	 String userEmail=	htmlcombo.describeCode("select email from am_gb_user where user_id='"+userId+"'");
	 	//System.out.println("====userEmail in SimpleMailWithAttachment=== "+userEmail+" "+userId);	 	
	 	System.out.println("====userEmail in SimpleMailWithAttachment mailto === "+mailto);
	 	System.out.println("====userEmail in SimpleMailWithAttachment userId === "+userId);
	 	userEmail = email;  
	 	Authenticator auth = new SMTPAuthenticator();
	 //	System.out.println(auth); 
	 	Session session = Session.getInstance(props, auth);
	 	session.setDebug(true);
	 	String recepientEmail=notifymail;
	 	System.out.println("====To Address in SimpleMailWithAttachment === "+notifymail);
		String recepient[]=recepientEmail.split(","); 
		String cc = recepient[0]; 
		System.out.println("====recepient[0]=== "+recepient[0]);
		System.out.println("====recepient[1]=== "+recepient[0]);
	 	MimeMessage msg = new MimeMessage(session); 
	 	msg.setText(msgBody,"UTF-8","html"); 
	 	msg.setSubject(subject,"text/html;charset=iso-8859-1");
	 	//msg.setFrom(new InternetAddress(email));
	 	msg.setFrom(new InternetAddress(userEmail));
	 	msg.addRecipient(Message.RecipientType.TO, new InternetAddress(mailto));
//		System.out.println("====msgBody=== "+msgBody);
		//System.out.println("====subject=== "+subject);
	 	
		int Recipient_length = recepient.length;
		System.out.println("====Recipient_length=== "+Recipient_length);
	 	for(int i=0;i<(recepient.length);i++)
		{ 
		InternetAddress addressCopy = new InternetAddress(recepient[i]);
		System.out.println("====addressCopy in SimpleMailWithAttachment === "+addressCopy);
//		System.out.println("====recepient.length=== "+recepient.length);
		
		msg.setRecipient(Message.RecipientType.CC, addressCopy);
		
		//}	
	 	Transport.send(msg);
		}
	 	}
	 	catch (Exception ex)
	 	{
	 	ex.printStackTrace();
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
				con = mgDbCon.getConnection("helpDesk");
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
//			String tech=util.findObject("select full_name from am_gb_user where user_id='"+user+"'");		
//				tech=tech=="UNKNOWN" ? "work in progress" :tech;
		//		System.out.println("=====tech ===== "+tech);
//			String dateReported=util.findObject("select create_date from "+FileName+" where "+FieldName+"='"+complaintId+"'");	
//			dateReported=dateReported=="UNKNOWN" ? String.valueOf(df.dateConvert(new java.util.Date())) :dateReported;
		//	System.out.println("=====dateReported ===== "+dateReported);

			mailDescription=
		        "Subject: <a href='http://172.19.2.39:419/Oriental?id="+complaintId+"'>"+Subject+"</a> <br/>"+			
				"Hi,  <br/>"+  
				"The issue "+complaintId+" is due for action.<br/>"+  
				"Details are as follows :- <br/>"+
				"Issue Id: 	"+complaintId+" <br/>"+
				"Title:"+	requestSubject +" <br/>"+
				"Description:"+ 	requestDescription +" <br/>" + 
				"Created By:"+ 	tech.toLowerCase() + " <br/>"+
//				"Date Reported:"+ 	dateReported+" <br/>"+
				"Issue Current Status:"+ 	statusMail +" <br/>";
//				statusMail+" By:"+ 	tech.toLowerCase() + " <br/>";
//				statusMail+" By:"+ 	ResolvedBy.toLowerCase() + " <br/>"+
//				"Date "+statusMail+":"+ 	df.dateConvert(new java.util.Date())+"+<br/>";
//				"Solution/"+statusMail+" Comment:"+ requestDescription+	" <br/>"+
			
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
				con = mgDbCon.getConnection("helpDesk");
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
	        "Subject: <a href='http://172.19.2.116:2012/Oriental?id="+complaintId+"&Status="+status+"'>"+Subject+"</a> <br/>"+			
			"Hi,  <br/>"+  
			"The issue "+complaintId+" has been "+statusMail+".<br/>"+  
			"Details are as follows :- <br/>"+
			"Issue Id: 	"+complaintId+" <br/>"+
			"Title:"+	requestSubject +" <br/>"+
			"Description:"+ 	requestDescription +" <br/>" + 
			"Created By:"+ 	tech.toLowerCase() + " <br/>"+
//			"Date Reported:"+ 	dateReported+" <br/>"+
			"Issue Current Status:"+ 	statusMail +" <br/>"+
			statusMail+" By:"+ 	tech.toLowerCase() + " <br/>"+
//			statusMail+" By:"+ 	ResolvedBy.toLowerCase() + " <br/>"+ 
//			"Date "+statusMail+":"+ 	df.dateConvert(new java.util.Date())+"+<br/>"+
			"Date "+statusMail+":"+ 	Date1+"+<br/>"+
			"Time "+statusMail+":"+ timer.format(new java.util.Date())+"+<br/>"+
			"Comments:"+ 	requestDescriptionNew +" <br/>";
			}
			if(Change == "EDITED"){ 
				//"Subject: <a href='http://172.19.2.116:419/Oriental?id="+complaintId+"&Status="+status+"'>"+Subject+"</a> <br/>"+
			mailDescription=
				"Subject: <a href='http://172.19.2.116:2012/Oriental?id="+complaintId+"&Status="+status+"'>"+Subject+"</a> <br/>"+		
				"Hi,  <br/>"+  
				"The issue "+complaintId+" has been "+Change+".<br/>"+  
				"Details are as follows :- <br/>"+
				"Issue Id: 	"+complaintId+" <br/>"+
				"Title:"+	requestSubject +" <br/>"+
				"Description:"+ 	requestDescription +" <br/>" + 
				"Created By:"+ 	tech.toLowerCase() + " <br/>"+
//				"Date Reported:"+ 	dateReported+" <br/>"+
				"Issue Current Status:"+ 	statusMail +" <br/>"+
				statusMail+" By:"+ 	tech.toLowerCase() + " <br/>"+
//				statusMail+" By:"+ 	ResolvedBy.toLowerCase() + " <br/>"+
				"Date "+statusMail+":"+ 	df.dateConvert(new java.util.Date())+"+<br/>"+
				"Time "+statusMail+":"+ timer.format(new java.util.Date())+"+<br/>"+
				"Comments:"+ 	requestDescriptionNew +" <br/>"+
				"Old Status Are as Follows:"+ " <br/>"+
				"Title:"+	requestSubjectold +" <br/>"+
				"Description:"+ 	requestDescriptionold +" <br/>" + 
//				"Date Reported:"+ 	dateReported+" <br/>"+
				"Issue Status:"+ 	statusMail +" <br/>"+
				statusMail+" By:"+ 	tech.toLowerCase() + " <br/>";
				
//			"Solution/"+statusMail+" Comment:"+ requestDescription+	" <br/>"+
//			"Title:"+	Description +" <br/>"+
//			"Thank you,<br/>"+
//			"Customer Care Centre<br/>";
			}
			if(Change == "RE-ASSIGNED"){ 
				//"Subject: <a href='http://172.19.2.116:419/Oriental?id="+complaintId+"&Status="+status+"'>"+Subject+"</a> <br/>"+
			mailDescription=
				"Subject: <a href='http://172.19.2.116:2012/Oriental?id="+complaintId+"&Status="+status+"'>"+Subject+"</a> <br/>"+		
				"Hi,  <br/>"+  
				"The issue "+complaintId+" has been "+Change+".<br/>"+  
				"Details are as follows :- <br/>"+
				"Issue Id: 	"+complaintId+" <br/>"+
				"Category:"+	requestSubject +" <br/>"+
				"Issue Type:"+ 	requestDescription +" <br/>" + 
				"Created By:"+ 	tech.toLowerCase() + " <br/>"+
//				"Date Reported:"+ 	dateReported+" <br/>"+
				"Issue Current Status:"+ 	statusMail +" <br/>"+
				statusMail+" By:"+ 	tech.toLowerCase() + " <br/>"+
//				statusMail+" By:"+ 	ResolvedBy.toLowerCase() + " <br/>"+
				"Date "+statusMail+":"+ 	df.dateConvert(new java.util.Date())+"+<br/>"+
				"Time "+statusMail+":"+ timer.format(new java.util.Date())+"+<br/>"+
				"Comments:"+ 	requestDescriptionNew +" <br/>"+
				"Old Status Are as Follows:"+ " <br/>"+
				"Category:"+	requestSubjectold +" <br/>"+
				"Issue Type:"+ 	requestDescriptionold +" <br/>" + 
//				"Date Reported:"+ 	dateReported+" <br/>"+
//				"Old Issue Status:"+ 	statusMail +" <br/>"+
				statusMail+" By:"+ 	tech.toLowerCase() + " <br/>";
				
//			"Solution/"+statusMail+" Comment:"+ requestDescription+	" <br/>"+
//			"Title:"+	Description +" <br/>"+
//			"Thank you,<br/>"+
//			"Customer Care Centre<br/>";
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
				File file = new File(url+"\\helpdesk.properties");
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
	 	 
	 	
	 	Authenticator auth = new SMTPAuthenticator();
	 	Session session = Session.getInstance(props, auth);
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
	 	 
	 	private class SMTPAuthenticator extends javax.mail.Authenticator
	 	{
	 	public PasswordAuthentication getPasswordAuthentication()
	 	{
	 	return new PasswordAuthentication(email, password);
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
	 			 String to=	htmlcombo.describeCode("select email from am_gb_user where user_id='"+userId+"'");
				Properties prop = new Properties();
			//	File file = new File("C:\\Property\\helpDesk.properties");
				File file = new File(url+"\\helpdesk.properties");
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
	 	Authenticator auth = new SMTPAuthenticator();
	 	Session session = Session.getInstance(props, auth);
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
//		System.out.println("====recepient.length=== "+recepient.length);
		
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
				File file = new File(url+"\\helpdesk.properties");
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
	 	 
	 	
	 	Authenticator auth = new SMTPAuthenticator();
	 	Session session = Session.getInstance(props, auth);
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
			    mail.SimpleMailWithAttachment("L:\\jboss-4.0.5.GA\\server\\epostserver\\deploy\\HelpDesk.war\\", "subject", "msgBody", "oreolanrewaju@gmail.com");
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
						con = mgDbCon.getConnection("helpDesk");
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
			System.out.println("=====query==> "+query);
			        Connection Con2 = null;
			        PreparedStatement Stat = null;
			        ResultSet result = null;
			        String found = null;

			        String finder = "UNKNOWN";

			        double sequence = 0.00d;
			        try {

			            Con2 = new DataConnect("helpDesk").getConnection();
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



			   
 }

