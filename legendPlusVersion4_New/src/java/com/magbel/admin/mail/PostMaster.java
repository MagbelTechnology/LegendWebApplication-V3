package com.magbel.admin.mail;
/**
*USAGE
*
* ArrayList messages = PostMaster.read(user,pass);
* for(int x = 0 ; x < messages.size(); x++){
*	String msg = PostMaster.content((EmailStamp)messages[x]);
*	System.out.println(msg);
* }
*/
import java.io.*;
import java.util.Date;
import java.util.Properties;
import java.util.ArrayList;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.DataSource;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;

public class PostMaster {

	public final static int FILE_CONTENT = 0;
  	public final static int PLAIN_CONTENT = 1;
  	public final static int HTML_CONTENT = 2;
  	private static Properties props;
  	private static String host;
  	private static String port;
  	private static String password;
  	private static String username;
  	private static String driver;
  	private static String url;
  	private static String dbuser; 
  	private static String dbpass;

  private static void init(String propsurl){

	  try{

		   props = new Properties();
		   FileInputStream in = new FileInputStream(propsurl+"\\helpdesk.mail.properties");
		   props.load(in);
		   host 		= props.getProperty("helpdesk.mail.host");
		   port 		= props.getProperty("helpdesk.mail.port");
		   username 	= props.getProperty("helpdesk.mail.username");
		   password 	= props.getProperty("helpdesk.mail.password");
		   driver 		= props.getProperty("helpdesk.mail.dbdriver");
		   url 			= props.getProperty("helpdesk.mail.dburl");
		   dbuser 		= props.getProperty("helpdesk.mail.dbuser");
		   dbpass 		= props.getProperty("helpdesk.mail.dbpass");
		   in.close();
		   System.out.println("====port===> "+port);		   
//System.out.println("====username===> "+username);
//System.out.println("====password===> "+password);
	  }catch(IOException e){
	   		throw new RuntimeException("WARN: Could not read the mail config file.\r\n" + e.toString());
	  }
  }

  private static Session getSession(String host)throws Exception{

	  Properties prop = new Properties();
	  prop.put("mail.smtp.host", host);
	  prop.put("mail.smtp.port", port);
	  Session session = Session.getDefaultInstance(prop, null);
	  session.setDebug(false); 

	  return session;
  }

  private static String content(EmailStamp message){
  	  return message.getContent();
    }

    public static ArrayList read(String propsurl) {

      	Message[] messages = null;
      	ArrayList records = new ArrayList();
      	init(propsurl);
      	try {
      		  // Store store = session.getStore("imap");
      		  Session session = getSession(host);
  			  Store store = session.getStore("pop3");
  			  store.connect(host,username,password);

  			  Folder folder = store.getFolder("INBOX");
  			  folder.open(Folder.READ_ONLY);
  			  messages = folder.getMessages();

  			  for(int x = 0 ; x < messages.length; x++){

				  javax.mail.Address[] a = messages[x].getFrom();
				  String from = a[0].toString();
				  Date d = messages[x].getSentDate();
                  String content = handleMessage(messages[x]);
  				  records.add(new EmailStamp(from,messages[x].getSubject(),content,d));
                }

  			  folder.close(false);
  			  store.close();

      		} catch (Exception e) {
      		  e.printStackTrace();
      	 }

      	 return records;
    }

  private static String convertToString(InputStream is)throws IOException{

	  ByteArrayOutputStream baos = new ByteArrayOutputStream();
	  int ch;
	  while((ch = is.read()) != -1) {
		  baos.write(ch);
		  /*
		  if (ch == '\n') {
		  ch = dis.read();
		  // the second newline??
		  if (ch == '\n')
			  break;
		  baos.write(ch);

		}*/
	  }
      return new String(baos.toByteArray());
  }

public static String handleMessage(Message message) throws MessagingException, IOException {

    String s = "undecoded";
    Object content = message.getContent();
    if (content instanceof String)
    {
        s = (String)((Part)message).getContent();
    }
    else if (content instanceof Multipart)
    {
        Multipart mp = (Multipart)content; 
        s = handleMultipart(mp);
        // handle multi part
    }

    return s;
}

public static  String handleMultipart(Multipart mp) throws MessagingException, IOException {

    int count = mp.getCount();
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < count; i++)
    {
        BodyPart bp = mp.getBodyPart(i);
        Object content = bp.getContent();
        if (content instanceof String)
        {
            sb.append("\r\n"+(String)content);
        }
        else if (content instanceof InputStream)
        {
            //skip attachements
            //sb.append("\r\n"+convertToString((InputStream)content));
        }
        else if (content instanceof Message)
        {
            Message message = (Message)content;
            sb.append("\r\n"+handleMessage(message));
        }
        else if (content instanceof Multipart)
        {
            Multipart mp2 = (Multipart)content;
            sb.append("\r\n"+handleMultipart(mp2));
        }
    }

    return sb.toString();
}

private static void log(EmailStamp email,String propsurl){

	java.sql.Connection con = null;
	java.sql.PreparedStatement ps = null;
	String id = Integer.toString(email.getContent().getBytes().length)+Integer.toString(email.getFrom().getBytes().length);

	try{

		init(propsurl);
		Class.forName(driver);
        con = java.sql.DriverManager.getConnection(url,dbuser,dbpass);
        System.out.println("connection - >"+con);
        String sql = "insert into mailer(id,sender,title,recv_dt,msg) values(?,?,?,?,?)";

        ps = con.prepareStatement(sql);
        ps.setString(1,id);
        ps.setString(2,email.getFrom());
        ps.setString(3,email.getTitle());
        ps.setDate(4,new java.sql.Date(email.getDate().getTime()));
        ps.setString(5,email.getContent());

        ps.execute();

	}catch(Exception e){
        e.printStackTrace();
	}finally{

		if(ps != null) try{ps.close();}catch(Exception e){}
		if(con != null) try{con.close();}catch(Exception e){}
	}
  
}  

  public static void main(String[] args){

	  ArrayList<EmailStamp> messages = PostMaster.read(args[0]);
	  System.out.println("total messages = "+messages.size());
	  for(int x = 0 ; x < messages.size(); x++){

		  EmailStamp email = (EmailStamp)messages.get(x);
		  PostMaster.log(email,args[0]);
	  }
  }
}
