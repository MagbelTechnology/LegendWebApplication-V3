package com.magbel.admin.mail;
 
 import javax.mail.*; 
 import javax.mail.internet.*; 
 import javax.activation.*; 
 import java.io.*; 
import java.util.Properties; 
 public class MailClient 
 { 
  
  
     public void sendMail(String mailServer, String from, String to, 
                             String subject, String messageBody, 
                             String[] attachments) throws 
MessagingException, AddressException 
     { 
         // Setup mail server 
         Properties props = System.getProperties(); 
         props.put("mail.smtp.host", mailServer); 
         System.out.println("in mailsend");
         // Get a mail session 
         Session session = Session.getDefaultInstance(props, null); 
          
         // Define a new mail message 
         Message message = new MimeMessage(session); 
         message.setFrom(new InternetAddress(from)); 
         message.addRecipient(Message.RecipientType.TO, new InternetAddress(to)); 
         message.setSubject(subject); 
          
         // Create a message part to represent the body text 
         BodyPart messageBodyPart = new MimeBodyPart(); 
         messageBodyPart.setText(messageBody); 
          
         //use a MimeMultipart as we need to handle the file attachments 
         Multipart multipart = new MimeMultipart(); 
          
         //add the message body to the mime message 
         multipart.addBodyPart(messageBodyPart); 
          
         
          
         // Put all message parts in the message 
         message.setContent(multipart); 
          
         // Send the message 
         Transport.send(message); 
  
  
     } 
  
    
     public static void main(String[] args) 
     { 
         try 
         { 
        	 System.out.println("sending");
             MailClient client = new MailClient(); 
             String server="homail4.ubagroup.com"; 
             String from="francis.odukoye@ubagroup.com"; 
             String to = "isqil.adeniji@ubagroup.com"; 
             String subject="Test"; 
             String message="Testing"; 
             String[] filenames =null; 
          
             client.sendMail(server,from,to,subject,message,filenames); 
             System.out.println("sent");
         } 
         catch(Exception e) 
         { 
             e.printStackTrace(System.out); 
         } 
          
     } 
 } 
