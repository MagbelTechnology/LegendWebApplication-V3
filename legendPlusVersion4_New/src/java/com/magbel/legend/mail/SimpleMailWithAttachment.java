package com.magbel.legend.mail;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
public class SimpleMailWithAttachment {
 
/**
 * @param args
 */

	
String email = "magbeltech@gmail.com",
password = "lmblagos",
host = "smtp.gmail.com",
port = "465",
to = "oreolanrewaju@gmail.com",
subject = "Testing",
text = "Hey, this is the testing email msg.";
 
public void SimpleMailWithAttachment()
{   
Properties props = new Properties();
props.put("mail.smtp.user", email);
props.put("mail.smtp.host", host);
props.put("mail.smtp.port", port);
props.put("mail.smtp.starttls.enable","true");
props.put("mail.smtp.auth", "true");
props.put("mail.smtp.socketFactory.port", port);
props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
props.put("mail.smtp.socketFactory.fallback", "false");
 
SecurityManager security = System.getSecurityManager();
 
try
{
Authenticator auth = new SMTPAuthenticator();
Session session = Session.getInstance(props, auth);
//session.setDebug(true);
 
MimeMessage msg = new MimeMessage(session);
msg.setText(text);
msg.setSubject(subject);
msg.setFrom(new InternetAddress(email));
msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

Transport.send(msg);
}
catch (Exception ex)
{
ex.printStackTrace();
}



}
 

public void sendMail(String text, String mailFrom, String mailTo, String subject) throws Exception {
    try {
        Properties props = System.getProperties();
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "587");
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.auth", "true");
 
        Authenticator auth = new MyAuthenticator();
        Session smtpSession = Session.getInstance(props, auth);
        smtpSession.setDebug(false);
 
        MimeMessage message = new MimeMessage(smtpSession);
        message.setFrom(new InternetAddress(mailFrom));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailTo));
 
        final String encoding = "UTF-8";
 
        message.setSubject(subject, encoding);
        message.setText(text, encoding);
        Transport.send(message);
    } catch (Exception e) {
        throw new Exception("sendMail()->Exception", e);
    }
}
 
static class MyAuthenticator extends Authenticator {
    public PasswordAuthentication getPasswordAuthentication() {
        String username = "nsiidika@gmail.com";
        String password = "kingsley";
 
        return new PasswordAuthentication(username, password);
    }
}
private class SMTPAuthenticator extends javax.mail.Authenticator
{
public PasswordAuthentication getPasswordAuthentication()
{
return new PasswordAuthentication(email, password);
}
}
public static void main(String[] args) throws Exception{
SimpleMailWithAttachment swa = new SimpleMailWithAttachment();
//swa.sendMail("text", "nsiidika@gmail.com", "oreolanrewaju@gmail.com", "subject");
swa.SimpleMailWithAttachment(); 
}
     
 
}    