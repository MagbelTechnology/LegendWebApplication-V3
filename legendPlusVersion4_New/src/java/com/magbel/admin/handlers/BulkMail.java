package com.magbel.admin.handlers;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFont;
//import org.apache.poi.hssf.usermodel.HSSFColor;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.naming.*;
import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import com.magbel.admin.handlers.ApprovalRecords;
/**

 */
public class BulkMail{
	String cc[]={""};
	SimpleDateFormat sdf;

	final String space = "  ";
	final String comma = ",";
	java.util.Date date;
	SendMailFile mailer;
	ApprovalRecords app = new ApprovalRecords();
	public BulkMail() {

		sdf = new SimpleDateFormat("dd-MM-yyyy");
		mailer = new SendMailFile();
	}




	public String sendMail(String from,String subject,String msgText1,String url,String to)
	{

		String result ="";

		try{

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
			
			if(to=="")
			{result ="No recipient";}
			else
			{
				/*
			String recipient[] =to.split(",");
			to = recipient[0];
			
			String cc[] = {recipient[1],recipient[2],recipient[3]};
			
			
			String[] bcc = {""};
		    String fileOut="2";
			*/
				String cc =to;
				String[] bcc = {""};
			    String fileOut="2";
		    
			System.out.println("host "+host);
			System.out.println("port "+port);
			System.out.println("authauth "+auth);
			
				   if(!Mail.sendMail(authenticator,password,host,port,starttsl,auth,(new Boolean(debug)).booleanValue(),socketFactory,fallback,from,to,cc,bcc,subject,msgText1,fileOut))
				   {
				    result = "Failed sending mail";
				   }
				   else
					   result="success";
			 }
		  }
	catch(Exception error){
			System.out.println("Error sending mail..."+error);
			error.printStackTrace();
		}
		return result;
	}
	public String sendEMail(String from,String subject,String msgText1,String url,String to,String id,String asset_id)
	{

		String result ="";

		try{

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
			
			String toMail = app.userEmail(id);
			if(toMail=="")
			{result ="No recipient";}
			else
			{
			
			
			
			String cc [] = {""};
			
			
			String[] bcc = {""};
		    String fileOut="2";
			
		    
			System.out.println("host "+host);
			System.out.println("port "+port);
			System.out.println("authauth "+auth);
			
				   if(!Mail.sendEMail(authenticator,password,host,port,starttsl,auth,(new Boolean(debug)).booleanValue(),socketFactory,fallback,from,toMail,cc,bcc,subject,msgText1,fileOut))
				   {
				    result = "Failed sending mail";
				   }
				   else
					   result="success";
			 }
		  }
	catch(Exception error)
	    {
			System.out.println("Error sending mail..."+error);
			error.printStackTrace();
		}
		return result;
	}

 
    
    public String sendEMailAsset(String from,String subject,String msgText1,String url,String to,String id,String asset_id)
	{

		String result ="";

		try{

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
			
			String toMail = app.userEmail(app.userToEmail(asset_id));
			if(toMail=="")
			{result ="No recipient";}
			else
			{
			
			
			
			String cc [] = {""};
			
			
			String[] bcc = {""};
		    String fileOut="2";
			
		    
			System.out.println("host "+host);
			System.out.println("port "+port);
			System.out.println("authauth "+auth);
			
				   if(!Mail.sendEMail(authenticator,password,host,port,starttsl,auth,(new Boolean(debug)).booleanValue(),socketFactory,fallback,from,toMail,cc,bcc,subject,msgText1,fileOut))
				   {
				    result = "Failed sending mail";
				   }
				   else
					   result="success";
			 }
		  }
	catch(Exception error){
			System.out.println("Error sending mail..."+error);
			error.printStackTrace();
		}
		return result;
	}
}
