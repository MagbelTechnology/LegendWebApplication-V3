package com.magbel.legend.mail;

import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.internet.MimeMessage.RecipientType;

import com.magbel.legend.bus.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
public class MailSender
{
	Properties props = null;
	GenerateList genList= null;
	ApprovalRecords apRecords = null;
	public MailSender()
	{
		props = new Properties();
		genList = new GenerateList();
		apRecords = new ApprovalRecords();
	} 
	//ApplicationHelper2 applHelper = new  ApplicationHelper2() ;
	public void sendMailToSupervisor(FileInputStream fis, String emailAddress,String reqnID)
	{
		System.out.print("About to load properties into prop");
		try 
		{
			System.out.print("<<<<<<<<<About to load properties into prop");
			props.load(fis);
			System.out.print("Able to load properties into prop");
			String host = props.getProperty("mail.smtp.host");
			String from = props.getProperty("mail-user");
			String port = props.getProperty("mail.smtp.port");
			String password = props.getProperty("mail-password");
			String SUBJECT = props.getProperty("mail-subject");
			
/*//			System.out.println("host >>>>>>>>>>>>>> " + host);
//			System.out.println("from >>>>>>>>>>>>>> " + from);
//			System.out.println("port >>>>>>>>>>>>>> " + port);
//			System.out.println("password >>>>>>>>>>>>>> " + password);
//			System.out.println("SUBJECT >>>>>>>>>>>>>> " + SUBJECT);
//			System.out.println("emailAddress >>>>>>>>>>>>>> " + emailAddress);
*/			 
			String STARTTLS = "true";
		    String AUTH = "true";
		    String DEBUG = "true";
		    String SOCKET_FACTORY = "javax.net.ssl.SSLSocketFactory";
		   
		    String emailMSg="Requisition with ID " + reqnID + " is waiting for your approval.";
		    
		    props.put("mail.smtp.host", host);
	        props.put("mail.smtp.port", port);
	        props.put("mail.smtp.user", from);

	        props.put("mail.smtp.auth", AUTH);
	        props.put("mail.smtp.starttls.enable", STARTTLS);
	        props.put("mail.smtp.debug", DEBUG);

	        props.put("mail.smtp.socketFactory.port", port);
	        props.put("mail.smtp.socketFactory.class", SOCKET_FACTORY);
	        props.put("mail.smtp.socketFactory.fallback", "false");
	        
	        Session session = Session.getDefaultInstance(props, null);
            session.setDebug(true);

            //Construct the mail message
            MimeMessage message = new MimeMessage(session);
            message.setText(emailMSg);
            message.setSubject(SUBJECT);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(RecipientType.TO, new InternetAddress(emailAddress));
            message.saveChanges();

            //Use Transport to deliver the message
            
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
		    
		} 
		catch (Exception ex)
		{
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}

	public void sendMailToAdmin(FileInputStream fis, String reqnID,String compCode) 
	{
		String msgDescription="";
		String msgMailAddress="";
		String admin_MailAddress_Qry="select email from am_gb_user where user_status='Active' and is_storekeeper='Y'";
		
		//System.out.println("admin_MailAddress_Qry >>>>>>>>>>>>>> " + admin_MailAddress_Qry);
		
		//String result= apRecords.retrieveArray(admin_MailAddress_Qry);
		String result = genList.retrieveEmailAddress(admin_MailAddress_Qry);
		
		//System.out.println("result >>>>>>>>>>>>>> " + result);
		if ((result != null)&&(result != ""))
		{
			String [] mail = result.split(":");
			String mailAddresses = result;
			String mailDescription = "Requisition Requires Approval By Admin";
			//System.out.println("mailAddress >>>>>>>>>>> " + mailAddresses);
			
			try 
			
			{
				props.load(fis);
			
				System.out.print("Able to load properties into prop");
				String host = props.getProperty("mail.smtp.host");
				String from = props.getProperty("mail-user");
				String port = props.getProperty("mail.smtp.port");
				String password = props.getProperty("mail-password");
				String SUBJECT = props.getProperty("mail-subject");
				String STARTTLS = "true";
			    String AUTH = "true";
			    String DEBUG = "true";
			    String SOCKET_FACTORY = "javax.net.ssl.SSLSocketFactory";
			    
				/*System.out.println("host >>>>>>>>>>>>>> " + host);
				System.out.println("from >>>>>>>>>>>>>> " + from);
				System.out.println("port >>>>>>>>>>>>>> " + port);
				System.out.println("password >>>>>>>>>>>>>> " + password);
				System.out.println("SUBJECT >>>>>>>>>>>>>> " + SUBJECT);*/
				
				props.put("mail.smtp.host", host);
		        props.put("mail.smtp.port", port);
		        props.put("mail.smtp.user", from);

		        props.put("mail.smtp.auth", AUTH);
		        props.put("mail.smtp.starttls.enable", STARTTLS);
		        props.put("mail.smtp.debug", DEBUG);

		        props.put("mail.smtp.socketFactory.port", port);
		        props.put("mail.smtp.socketFactory.class", SOCKET_FACTORY);
		        props.put("mail.smtp.socketFactory.fallback", "false");
		        
		        String emailMSg="Requisition with ID " + reqnID + " requires Approval By Admin.";
		        
		        Session session = Session.getDefaultInstance(props, null);
	            session.setDebug(true);
	            
		        MimeMessage message = new MimeMessage(session);
		        message.setText(emailMSg);
	            message.setSubject(SUBJECT);
	            message.setFrom(new InternetAddress(from));
	            message.saveChanges();
	            
	            String recepient[]=mailAddresses.split(",");
	            InternetAddress[] addressTo = new InternetAddress[recepient.length]; 
	        	for(int i=0;i<recepient.length;i++)
				{
	        		addressTo[i] = new InternetAddress(recepient[i]);	
					//System.out.println("Recipient >>>>>>>>>>>> " + recepient[i]);
					
				}
	        	message.setRecipients(Message.RecipientType.TO, addressTo);
	        	        	
	            //Use Transport to deliver the message
	            Transport transport = session.getTransport("smtp");
	            transport.connect(host, from, password);
	            transport.sendMessage(message, message.getAllRecipients());
	            transport.close();
			
	            
			} 
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void sendMailToSupervisors(FileInputStream fis, String reqnID,String compCode) 
	{
		String msgDescription="";
		String msgMailAddress="";
		String sprv_MailAddress_Qry="Select emailAddress from am_Approval_Remark where "
			+ " status = 'Approved' and asset_ID ='"+reqnID+"'";
		
		//System.out.println("sprv_MailAddress_Qry >>>>>>>>>>>>>> " + sprv_MailAddress_Qry);
		
		String result= genList.retrieveEmailAddress(sprv_MailAddress_Qry);
		
		//System.out.println("result >>>>>>>>>>>>>> " + result);
		if ((result != null)&&(result != ""))
		{
			String mailAddresses = result;
			String emailMSg = "Requisition with ID  " +reqnID+" Has Been Rejected !!!" ;
			//System.out.println("mailAddress >>>>>>>>>>> " + mailAddresses);
			
			try 
			{
				props.load(fis);
			
				System.out.print("Able to load properties into prop");
				String host = props.getProperty("mail.smtp.host");
				String from = props.getProperty("mail-user");
				String port = props.getProperty("mail.smtp.port");
				String password = props.getProperty("mail-password");
				String SUBJECT = "REQUISITION REJECTED";
				String STARTTLS = "true";
			    String AUTH = "true";
			    String DEBUG = "true";
			    String SOCKET_FACTORY = "javax.net.ssl.SSLSocketFactory";
			    
				/*System.out.println("host >>>>>>>>>>>>>> " + host);
				System.out.println("from >>>>>>>>>>>>>> " + from);
				System.out.println("port >>>>>>>>>>>>>> " + port);
				System.out.println("password >>>>>>>>>>>>>> " + password);
				System.out.println("SUBJECT >>>>>>>>>>>>>> " + SUBJECT);*/
				
				props.put("mail.smtp.host", host);
		        props.put("mail.smtp.port", port);
		        props.put("mail.smtp.user", from);

		        props.put("mail.smtp.auth", AUTH);
		        props.put("mail.smtp.starttls.enable", STARTTLS);
		        props.put("mail.smtp.debug", DEBUG);

		        props.put("mail.smtp.socketFactory.port", port);
		        props.put("mail.smtp.socketFactory.class", SOCKET_FACTORY);
		        props.put("mail.smtp.socketFactory.fallback", "false");
		        
		       // String emailMSg="Requisition with ID " + reqnID + " requires Approval By Admin.";
		        
		        Session session = Session.getDefaultInstance(props, null);
	            session.setDebug(true);
	            
		        MimeMessage message = new MimeMessage(session);
		        message.setText(emailMSg);
	            message.setSubject(SUBJECT);
	            message.setFrom(new InternetAddress(from));
	            message.saveChanges();
	            
	            String recepient[]=mailAddresses.split(",");
	            InternetAddress[] addressTo = new InternetAddress[recepient.length]; 
	        	for(int i=0;i<recepient.length;i++)
				{
	        		addressTo[i] = new InternetAddress(recepient[i]);	
					//System.out.println("Recipient >>>>>>>>>>>> " + recepient[i]);
					
				}
	        	message.setRecipients(Message.RecipientType.TO, addressTo);
	        	        	
	            //Use Transport to deliver the message
	            Transport transport = session.getTransport("smtp");
	            transport.connect(host, from, password);
	            transport.sendMessage(message, message.getAllRecipients());
	            transport.close();
			
	            
			} 
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
public void sendMailToFacAcceptanceDept(FileInputStream fis, String reqnID,String compCode)
	{
		String msgDescription="";
		String msgMailAddress="";
		String admin_MailAddress_Qry="select email from am_gb_user where user_status='Active' and Fleet_admin='Y'";

		//System.out.println("admin_MailAddress_Qry >>>>>>>>>>>>>> " + admin_MailAddress_Qry);

		//String result= apRecords.retrieveArray(admin_MailAddress_Qry);
		String result = genList.retrieveEmailAddress(admin_MailAddress_Qry);

		//System.out.println("result >>>>>>>>>>>>>> " + result);
		if ((result != null)&&(result != ""))
		{
			String [] mail = result.split(":");
			String mailAddresses = result;
			String mailDescription = "Requisition Requires Approval By Facility Acceptance Dept";
			//System.out.println("mailAddress >>>>>>>>>>> " + mailAddresses);

			try

			{
				props.load(fis);

				System.out.print("Able to load properties into prop");
				String host = props.getProperty("mail.smtp.host");
				String from = props.getProperty("mail-user");
				String port = props.getProperty("mail.smtp.port");
				String password = props.getProperty("mail-password");
				String SUBJECT = props.getProperty("mail-subject");
				String STARTTLS = "true";
			    String AUTH = "true";
			    String DEBUG = "true";
			    String SOCKET_FACTORY = "javax.net.ssl.SSLSocketFactory";

				/*System.out.println("host >>>>>>>>>>>>>> " + host);
				System.out.println("from >>>>>>>>>>>>>> " + from);
				System.out.println("port >>>>>>>>>>>>>> " + port);
				System.out.println("password >>>>>>>>>>>>>> " + password);
				System.out.println("SUBJECT >>>>>>>>>>>>>> " + SUBJECT);*/

				props.put("mail.smtp.host", host);
		        props.put("mail.smtp.port", port);
		        props.put("mail.smtp.user", from);

		        props.put("mail.smtp.auth", AUTH);
		        props.put("mail.smtp.starttls.enable", STARTTLS);
		        props.put("mail.smtp.debug", DEBUG);

		        props.put("mail.smtp.socketFactory.port", port);
		        props.put("mail.smtp.socketFactory.class", SOCKET_FACTORY);
		        props.put("mail.smtp.socketFactory.fallback", "false");

		        String emailMSg="Requisition with ID " + reqnID + " requires Approval By Facility Acceptance Dept.";

		        Session session = Session.getDefaultInstance(props, null);
	            session.setDebug(true);

		        MimeMessage message = new MimeMessage(session);
		        message.setText(emailMSg);
	            message.setSubject(SUBJECT);
	            message.setFrom(new InternetAddress(from));
	            message.saveChanges();

	            String recepient[]=mailAddresses.split(",");
	            InternetAddress[] addressTo = new InternetAddress[recepient.length];
	        	for(int i=0;i<recepient.length;i++)
				{
	        		addressTo[i] = new InternetAddress(recepient[i]);
					//System.out.println("Recipient >>>>>>>>>>>> " + recepient[i]);

				}
	        	message.setRecipients(Message.RecipientType.TO, addressTo);

	            //Use Transport to deliver the message
	            Transport transport = session.getTransport("smtp");
	            transport.connect(host, from, password);
	            transport.sendMessage(message, message.getAllRecipients());
	            transport.close();


			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

        public void sendMailToAUser(FileInputStream fis, String emailAddress,String msgHead, String msgBody)
	{

		try
		{
			props.load(fis);
			System.out.print("Able to load properties into prop");
			String host = props.getProperty("mail.smtp.host");
			String from = props.getProperty("mail-user");
			String port = props.getProperty("mail.smtp.port");
			String password = props.getProperty("mail-password");
			String SUBJECT = msgHead;


			String STARTTLS = "true";
		    String AUTH = "true";
		    String DEBUG = "true";
		    String SOCKET_FACTORY = "javax.net.ssl.SSLSocketFactory";

		    String emailMSg=msgBody;

		    props.put("mail.smtp.host", host);
	        props.put("mail.smtp.port", port);
	        props.put("mail.smtp.user", from);

	        props.put("mail.smtp.auth", AUTH);
	        props.put("mail.smtp.starttls.enable", STARTTLS);
	        props.put("mail.smtp.debug", DEBUG);

	        props.put("mail.smtp.socketFactory.port", port);
	        props.put("mail.smtp.socketFactory.class", SOCKET_FACTORY);
	        props.put("mail.smtp.socketFactory.fallback", "false");

	        Session session = Session.getDefaultInstance(props, null);
            session.setDebug(true);

            //Construct the mail message
            MimeMessage message = new MimeMessage(session);
            message.setText(emailMSg);
            message.setSubject(SUBJECT);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(RecipientType.TO, new InternetAddress(emailAddress));
            message.saveChanges();

            //Use Transport to deliver the message

            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

		}
		catch (Exception ex)
		{
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}

        public void sendMailToFacAcceptanceSupervisor(FileInputStream fis, String emailAddress,String acceptanceId)
	{

		try
		{
			props.load(fis);
			System.out.print("Able to load properties into prop");
			String host = props.getProperty("mail.smtp.host");
			String from = props.getProperty("mail-user");
			String port = props.getProperty("mail.smtp.port");
			String password = props.getProperty("mail-password");
			String SUBJECT = props.getProperty("mail-subject");

/*//
*/
			String STARTTLS = "true";
		    String AUTH = "true";
		    String DEBUG = "true";
		    String SOCKET_FACTORY = "javax.net.ssl.SSLSocketFactory";

		    String emailMSg="Requisition with ID " + acceptanceId + " is waiting for your approval.";

		    props.put("mail.smtp.host", host);
	        props.put("mail.smtp.port", port);
	        props.put("mail.smtp.user", from);

	        props.put("mail.smtp.auth", AUTH);
	        props.put("mail.smtp.starttls.enable", STARTTLS);
	        props.put("mail.smtp.debug", DEBUG);

	        props.put("mail.smtp.socketFactory.port", port);
	        props.put("mail.smtp.socketFactory.class", SOCKET_FACTORY);
	        props.put("mail.smtp.socketFactory.fallback", "false");

	        Session session = Session.getDefaultInstance(props, null);
            session.setDebug(true);

            //Construct the mail message
            MimeMessage message = new MimeMessage(session);
            message.setText(emailMSg);
            message.setSubject(SUBJECT);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(RecipientType.TO, new InternetAddress(emailAddress));
            message.saveChanges();

            //Use Transport to deliver the message

            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

		}
		catch (Exception ex)
		{
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}


         public void sendMailToWorkCompletionSupervisors(FileInputStream fis, int tranId,String compCode,String messageBody)
	{
		String msgDescription="";
		String msgMailAddress="";
		String sprv_MailAddress_Qry="Select emailAddress from am_Approval_Remark where "
			+ " status = 'Approved' and transaction_id = "+tranId;

		//System.out.println("sprv_MailAddress_Qry >>>>>>>>>>>>>> " + sprv_MailAddress_Qry);

		String result= genList.retrieveEmailAddress(sprv_MailAddress_Qry);

		//System.out.println("result >>>>>>>>>>>>>> " + result);
		if ((result != null)&&(result != ""))
		{
			String mailAddresses = result;
			String emailMSg = messageBody ;
			//System.out.println("mailAddress >>>>>>>>>>> " + mailAddresses);

			try
			{
				props.load(fis);

				System.out.print("Able to load properties into prop");
				String host = props.getProperty("mail.smtp.host");
				String from = props.getProperty("mail-user");
				String port = props.getProperty("mail.smtp.port");
				String password = props.getProperty("mail-password");
				String SUBJECT = "REQUISITION REJECTED";
				String STARTTLS = "true";
			    String AUTH = "true";
			    String DEBUG = "true";
			    String SOCKET_FACTORY = "javax.net.ssl.SSLSocketFactory";

				/*System.out.println("host >>>>>>>>>>>>>> " + host);
				System.out.println("from >>>>>>>>>>>>>> " + from);
				System.out.println("port >>>>>>>>>>>>>> " + port);
				System.out.println("password >>>>>>>>>>>>>> " + password);
				System.out.println("SUBJECT >>>>>>>>>>>>>> " + SUBJECT);*/

				props.put("mail.smtp.host", host);
		        props.put("mail.smtp.port", port);
		        props.put("mail.smtp.user", from);

		        props.put("mail.smtp.auth", AUTH);
		        props.put("mail.smtp.starttls.enable", STARTTLS);
		        props.put("mail.smtp.debug", DEBUG);

		        props.put("mail.smtp.socketFactory.port", port);
		        props.put("mail.smtp.socketFactory.class", SOCKET_FACTORY);
		        props.put("mail.smtp.socketFactory.fallback", "false");

		       // String emailMSg="Requisition with ID " + reqnID + " requires Approval By Admin.";

		        Session session = Session.getDefaultInstance(props, null);
	            session.setDebug(true);

		        MimeMessage message = new MimeMessage(session);
		        message.setText(emailMSg);
	            message.setSubject(SUBJECT);
	            message.setFrom(new InternetAddress(from));
	            message.saveChanges();

	            String recepient[]=mailAddresses.split(",");
	            InternetAddress[] addressTo = new InternetAddress[recepient.length];
	        	for(int i=0;i<recepient.length;i++)
				{
	        		addressTo[i] = new InternetAddress(recepient[i]);
					//System.out.println("Recipient >>>>>>>>>>>> " + recepient[i]);

				}
	        	message.setRecipients(Message.RecipientType.TO, addressTo);

	            //Use Transport to deliver the message
	            Transport transport = session.getTransport("smtp");
	            transport.connect(host, from, password);
	            transport.sendMessage(message, message.getAllRecipients());
	            transport.close();


			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}


              public void sendMailToStockAdmins(FileInputStream fis, String reqnID,String compCode)
     	{
     		String msgDescription="";
     		String msgMailAddress="";
     		String admin_MailAddress_Qry="select email from am_gb_user where user_status='Active' and Stock_admin='Y' and is_supervisor='N'";

     		//System.out.println("admin_MailAddress_Qry >>>>>>>>>>>>>> " + admin_MailAddress_Qry);

     		//String result= apRecords.retrieveArray(admin_MailAddress_Qry);
     		String result = genList.retrieveEmailAddress(admin_MailAddress_Qry);

     		//System.out.println("result >>>>>>>>>>>>>> " + result);
     		if ((result != null)&&(result != ""))
     		{
     			String [] mail = result.split(":");
     			String mailAddresses = result;
     			//String mailDescription = "Requisition Requires Approval By Facility Acceptance Dept";
     			//System.out.println("mailAddress >>>>>>>>>>> " + mailAddresses);

     			try

     			{
     				props.load(fis);

     				System.out.print("Able to load properties into prop");
     				String host = props.getProperty("mail.smtp.host");
     				String from = props.getProperty("mail-user");
     				String port = props.getProperty("mail.smtp.port");
     				String password = props.getProperty("mail-password");
     				String SUBJECT = props.getProperty("mail-subject");
     				String STARTTLS = "true";
     			    String AUTH = "true";
     			    String DEBUG = "true";
     			    String SOCKET_FACTORY = "javax.net.ssl.SSLSocketFactory";

     				/*System.out.println("host >>>>>>>>>>>>>> " + host);
     				System.out.println("from >>>>>>>>>>>>>> " + from);
     				System.out.println("port >>>>>>>>>>>>>> " + port);
     				System.out.println("password >>>>>>>>>>>>>> " + password);
     				System.out.println("SUBJECT >>>>>>>>>>>>>> " + SUBJECT);*/

     				props.put("mail.smtp.host", host);
     		        props.put("mail.smtp.port", port);
     		        props.put("mail.smtp.user", from);

     		        props.put("mail.smtp.auth", AUTH);
     		        props.put("mail.smtp.starttls.enable", STARTTLS);
     		        props.put("mail.smtp.debug", DEBUG);

     		        props.put("mail.smtp.socketFactory.port", port);
     		        props.put("mail.smtp.socketFactory.class", SOCKET_FACTORY);
     		        props.put("mail.smtp.socketFactory.fallback", "false");

     		        String emailMSg="Stock Requisition with Requisition ID " + reqnID + " has been approved for distribution.";

     		        Session session = Session.getDefaultInstance(props, null);
     	            session.setDebug(true);

     		        MimeMessage message = new MimeMessage(session);
     		        message.setText(emailMSg);
     	            message.setSubject(SUBJECT);
     	            message.setFrom(new InternetAddress(from));
     	            message.saveChanges();

     	            String recepient[]=mailAddresses.split(",");
     	            InternetAddress[] addressTo = new InternetAddress[recepient.length];
     	        	for(int i=0;i<recepient.length;i++)
     				{
     	        		addressTo[i] = new InternetAddress(recepient[i]);
     					//System.out.println("Recipient >>>>>>>>>>>> " + recepient[i]);

     				}
     	        	message.setRecipients(Message.RecipientType.TO, addressTo);

     	            //Use Transport to deliver the message
     	            Transport transport = session.getTransport("smtp");
     	            transport.connect(host, from, password);
     	            transport.sendMessage(message, message.getAllRecipients());
     	            transport.close();


     			}
     			catch (Exception e)
     			{
     				// TODO Auto-generated catch block
     				e.printStackTrace();
     			}
     		}

     	}

}
