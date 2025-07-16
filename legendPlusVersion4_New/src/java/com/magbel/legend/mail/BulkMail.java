package com.magbel.legend.mail;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import java.sql.Statement;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

import com.magbel.legend.bus.ApprovalRecords;

import magma.net.dao.MagmaDBConnection;

import com.magbel.util.DatetimeFormat;
/**
 *
 * @author Jejelowo B.Festus
 * @author Ocular-Minds Softwares
 * @author www.ocularminds.com
 * @since 2008
 * @version 1.0.0
 */
public class BulkMail{
	String cc[]={""};
	SimpleDateFormat sdf;
	SimpleDateFormat timer;
	DatetimeFormat df;
	final String space = "  ";
	final String comma = ",";
	java.util.Date date;
	SendMailFile mailer;
	ApprovalRecords app = new ApprovalRecords();
	MagmaDBConnection mgDbCon = null;
	public BulkMail() {
  
		sdf = new SimpleDateFormat("dd-MM-yyyy");
		timer = new SimpleDateFormat("kk:mm:ss");
		mailer = new SendMailFile();
		mgDbCon = new MagmaDBConnection();
		df = new com.magbel.util.DatetimeFormat();
	}



	public String sendMail(String from,String subject,String msgText1,String url,String to)
	{

		String result ="";

		try{

			Properties props = new Properties();
	//		InputStream in = (InputStream)(new FileInputStream(new File(url+"C:\\Property\\LegendPlus.properties")));
			InputStream in = (InputStream)(new FileInputStream(new File("C:\\Property\\LegendPlus.properties")));
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
			InputStream in = (InputStream)(new FileInputStream(new File("C:\\Property\\LegendPlus.properties")));
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
			InputStream in = (InputStream)(new FileInputStream(new File("C:\\Property\\LegendPlus.properties")));
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
    
	public String getEmailMessageOriginal(String transactionType,String categoryCode,String subcategory, String helpType,String technician,String user,String requestDescription,String complaintId,String requestSubject, String FileName,String FieldName,String Subject, String status, String requestDescriptionNew, String Change,String requestSubjectold,String requestDescriptionold)
	{    
	//	System.out.println("TESTMAIL ME");
		String query = " SELECT mail_description,mail_heading,mail_address,Status  FROM HD_MAIL_STATEMENT  WHERE  transaction_Type = '"+transactionType.trim()+"'"+
		"  And category_Code = '"+categoryCode.trim()+"'  And help_Type = '"+helpType.trim()+"'  " ;		
	//	System.out.println("--query->> 1 "+query);  
		String mailDescription =null;
		String mailHeading=null;   
		String mailAddress=null;   
		//String status=null;
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
			con = mgDbCon.getConnection("legendPlus");
			//con = getConnection("helpDesk"); 
			ps = con.prepareStatement(query); 
			rs = ps.executeQuery();
		//	System.out.println("--query->>"+query);  
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
			    	 Mail_Head[0]=app.getCodeName("SELECT complaint_id, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
			     }		
			     if (Mail_Head[0].trim().equalsIgnoreCase("complain_category")&& i < sizelentHeading){
			    	 Mail_Head[0]=app.getCodeName("SELECT Dept_name, UnitHead   FROM   AM_AD_DEPARTMENT WHERE Dept_code = '"+categoryCode+"'");
			     }		
			     if (Mail_Head[0].trim().equalsIgnoreCase("complain_sub_category")){
			    	 Mail_Head[0]=app.getCodeName("SELECT sub_category_name, sub_category_desc   FROM   HD_COMPLAIN_SUBCATEGORY WHERE sub_category_code = '"+subcategory+"'");	   	  
			     }
			     Heading = Mail_Head[0];  
			     }  
			     i = i + 1;
			     if (i < sizelentHeading){ 
			     if (Mail_Head[1].trim().equalsIgnoreCase("COMPLAINT_ID")){
			    	 Mail_Head[1]=app.getCodeName("SELECT complaint_id, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");								  
			     }			     			     
			     if (Mail_Head[1].trim().equalsIgnoreCase("complain_category")&& sizelentHeading > i){
			    	 Mail_Head[1]=app.getCodeName("SELECT Dept_name, UnitHead   FROM   AM_AD_DEPARTMENT WHERE Dept_code = '"+categoryCode+"'");
			     }		
			     if (Mail_Head[1].trim().equalsIgnoreCase("complain_sub_category")){
			    	 Mail_Head[1]=app.getCodeName("SELECT sub_category_name, sub_category_desc   FROM   HD_COMPLAIN_SUBCATEGORY WHERE sub_category_code = '"+subcategory+"'");			    	
			     }	
			     Heading = Heading + " " + Mail_Head[1];
			     }    
			     i = i + 1;  
			     if (i < sizelentHeading){
			     if (Mail_Head[2].trim().equalsIgnoreCase("COMPLAINT_ID")){
			    	 Mail_Head[2]=app.getCodeName("SELECT complaint_id, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");								  
			     }			     			     
			     if (Mail_Head[2].trim().equalsIgnoreCase("complain_category")&& i < sizelentHeading){
			    	// String Dept_Code=app.getCodeName("SELECT complain_category, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
			    	 Mail_Head[2]=app.getCodeName("SELECT Dept_name, UnitHead   FROM   AM_AD_DEPARTMENT WHERE Dept_code = '"+categoryCode+"'");
			     }			
			     if (Mail_Head[2].trim().equalsIgnoreCase("complain_sub_category")){
			    	// String SubCatCode=app.getCodeName("SELECT complain_sub_category, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
			    	 Mail_Head[2]=app.getCodeName("SELECT sub_category_name, sub_category_desc   FROM   HD_COMPLAIN_SUBCATEGORY WHERE sub_category_code = '"+subcategory+"'");
			     }	
			     Heading = Heading + Mail_Head[2];
			     }  
			    // i = i + 1;   
			     if (j < sizelentDescr){
			     if (Mail_Description[0].trim().equalsIgnoreCase("COMPLAINT_ID")){
			    	 Mail_Description[0]=app.getCodeName("SELECT complaint_id, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
			     }			     			     
			     if (Mail_Description[0].trim().equalsIgnoreCase("complain_category")&& sizelentDescr > j){
			    	// String Dept_Code=app.getCodeName("SELECT complain_category, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
			    	 Mail_Description[0]=app.getCodeName("SELECT Dept_name, UnitHead   FROM   AM_AD_DEPARTMENT WHERE Dept_code = '"+categoryCode+"'");
			     }		
			     if (Mail_Description[0].trim().equalsIgnoreCase("complain_sub_category")&& sizelentDescr > j){
			    	// String SubCatCode=app.getCodeName("SELECT complain_sub_category, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
			    	 Mail_Description[0]=app.getCodeName("SELECT sub_category_name, sub_category_desc   FROM   HD_COMPLAIN_SUBCATEGORY WHERE sub_category_code = '"+subcategory+"'");
			     } 
			     Description =  Mail_Description[0];
			     }  	
			     j = j + 1;
			     if (j < sizelentDescr){
			     if (Mail_Description[1].trim().equalsIgnoreCase("COMPLAINT_ID")&& sizelentDescr > j){
			    	 Mail_Description[1]=app.getCodeName("SELECT complaint_id, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");								  
			     }			     			     
			     if (Mail_Description[1].trim().equalsIgnoreCase("complain_category")&& sizelentDescr > j){
			    //	 String Dept_Code=app.getCodeName("SELECT complain_category, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
			    	 Mail_Description[1]=app.getCodeName("SELECT Dept_name, UnitHead   FROM   AM_AD_DEPARTMENT WHERE Dept_code = '"+categoryCode+"'"); 
			     }		
			     if (Mail_Description[1].trim().equalsIgnoreCase("complain_sub_category")&& sizelentDescr > j){
			    	// String SubCatCode=app.getCodeName("SELECT complain_sub_category, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
			    	 Mail_Description[1]=app.getCodeName("SELECT sub_category_name, sub_category_desc   FROM   HD_COMPLAIN_SUBCATEGORY WHERE sub_category_code = '"+subcategory+"'");
			     }	
			     Description = Description + " " + Mail_Description[1];
			     }  	
			     j = j + 1;
			     if (j < sizelentDescr){
			     if (Mail_Description[2].trim().equalsIgnoreCase("COMPLAINT_ID")&& sizelentDescr > j){
			    	 Mail_Description[2]=app.getCodeName("SELECT complaint_id, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");								  
			     }			     			     
			     if (Mail_Description[2].trim().equalsIgnoreCase("complain_category")&& sizelentDescr > j){
			    	// String Dept_Code=app.getCodeName("SELECT complain_category, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
			    	 Mail_Description[2]=app.getCodeName("SELECT Dept_name, UnitHead   FROM   AM_AD_DEPARTMENT WHERE Dept_code = '"+categoryCode+"'");
			     }		
			     if (Mail_Description[2].trim().equalsIgnoreCase("complain_sub_category")&& sizelentDescr > j){
			    //	 String SubCatCode=app.getCodeName("SELECT complain_sub_category, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
			    	 Mail_Description[2]=app.getCodeName("SELECT sub_category_name, sub_category_desc   FROM   HD_COMPLAIN_SUBCATEGORY WHERE sub_category_code = '"+subcategory+"'");
			     }	
			     Description = Description + " " + Mail_Description[2];	
			     } 
			} 
			String ResolvedBy = "";   
		String Assignee=app.getCodeName("SELECT technician, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
		//System.out.println("=====Assignee Before ===== "+Assignee);
	  	  if(Assignee == null || Assignee.equalsIgnoreCase("")){
			 ResolvedBy=app.getCodeName("SELECT UnitHead, email   FROM   AM_AD_DEPARTMENT WHERE Dept_code = '"+categoryCode+"'");
			// System.out.println("=====Assignee is Null ===== "+ResolvedBy);
		}else{
		ResolvedBy =app.getCodeName("select full_name from am_gb_user where user_id  = '"+Assignee+"'");
		// System.out.println("=====Assignee is Not Null ===== "+ResolvedBy);
		}  
	//	System.out.println("=====ResolvedBy ===== "+ResolvedBy);
		String statusMail=app.getCodeName("select status_description from hd_status where status_code='"+status+"'");
	//	System.out.println("=====statusMail ===== "+statusMail);
		//String	Subject="Subject: Notification for Issue : "+complaintId;
		String tech=app.getCodeName("select full_name from am_gb_user where user_id='"+user+"'");		
			tech=tech=="UNKNOWN" ? "work in progress" :tech;
	//		System.out.println("=====tech ===== "+tech);
		String dateReported=app.getCodeName("select create_date from "+FileName+" where "+FieldName+"='"+complaintId+"'");	
		dateReported=dateReported=="UNKNOWN" ? String.valueOf(df.dateConvert(new java.util.Date())) :dateReported;
	//	System.out.println("=====dateReported ===== "+dateReported);
		System.out.println("=====Change ===== "+Change);
		if(Change != "EDITED"||Change != "RE-ASSIGNED"){
		mailDescription=  
        "Subject: <a href='http://172.19.2.116:8080/legenPlus?id="+complaintId+"&Status="+status+"'>"+Subject+"</a> <br/>"+			
		"Hi,  <br/>"+  
		"The issue "+complaintId+" has been "+statusMail+".<br/>"+  
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
		"Comments:"+ 	requestDescriptionNew +" <br/>";
		}
		if(Change == "EDITED"){ 
			//"Subject: <a href='http://172.19.2.116:419/Oriental?id="+complaintId+"&Status="+status+"'>"+Subject+"</a> <br/>"+
		mailDescription=
			"Subject: <a href='http://172.19.2.116:8080/legenPlus?id="+complaintId+"&Status="+status+"'>"+Subject+"</a> <br/>"+		
			"Hi,  <br/>"+  
			"The issue "+complaintId+" has been "+Change+".<br/>"+  
			"Details are as follows :- <br/>"+
			"Issue Id: 	"+complaintId+" <br/>"+
			"Title:"+	requestSubject +" <br/>"+
			"Description:"+ 	requestDescription +" <br/>" + 
			"Created By:"+ 	tech.toLowerCase() + " <br/>"+
//			"Date Reported:"+ 	dateReported+" <br/>"+
			"Issue Current Status:"+ 	statusMail +" <br/>"+
			statusMail+" By:"+ 	tech.toLowerCase() + " <br/>"+
//			statusMail+" By:"+ 	ResolvedBy.toLowerCase() + " <br/>"+
			"Date "+statusMail+":"+ 	df.dateConvert(new java.util.Date())+"+<br/>"+
			"Time "+statusMail+":"+ timer.format(new java.util.Date())+"+<br/>"+
			"Comments:"+ 	requestDescriptionNew +" <br/>"+
			"Old Status Are as Follows:"+ " <br/>"+
			"Title:"+	requestSubjectold +" <br/>"+
			"Description:"+ 	requestDescriptionold +" <br/>" + 
//			"Date Reported:"+ 	dateReported+" <br/>"+
			"Issue Status:"+ 	statusMail +" <br/>"+
			statusMail+" By:"+ 	tech.toLowerCase() + " <br/>";
			
//		"Solution/"+statusMail+" Comment:"+ requestDescription+	" <br/>"+
//		"Title:"+	Description +" <br/>"+
//		"Thank you,<br/>"+
//		"Customer Care Centre<br/>";
		}
		if(Change == "RE-ASSIGNED"){ 
			//"Subject: <a href='http://172.19.2.116:419/Oriental?id="+complaintId+"&Status="+status+"'>"+Subject+"</a> <br/>"+
		mailDescription=
			"Subject: <a href='http://172.19.2.116:8080/legenPlus?id="+complaintId+"&Status="+status+"'>"+Subject+"</a> <br/>"+		
			"Hi,  <br/>"+  
			"The issue "+complaintId+" has been "+Change+".<br/>"+  
			"Details are as follows :- <br/>"+
			"Issue Id: 	"+complaintId+" <br/>"+
			"Category:"+	requestSubject +" <br/>"+
			"Issue Type:"+ 	requestDescription +" <br/>" + 
			"Created By:"+ 	tech.toLowerCase() + " <br/>"+
//			"Date Reported:"+ 	dateReported+" <br/>"+
			"Issue Current Status:"+ 	statusMail +" <br/>"+
			statusMail+" By:"+ 	tech.toLowerCase() + " <br/>"+
//			statusMail+" By:"+ 	ResolvedBy.toLowerCase() + " <br/>"+
			"Date "+statusMail+":"+ 	df.dateConvert(new java.util.Date())+"+<br/>"+
			"Time "+statusMail+":"+ timer.format(new java.util.Date())+"+<br/>"+
			"Comments:"+ 	requestDescriptionNew +" <br/>"+
			"Old Status Are as Follows:"+ " <br/>"+
			"Category:"+	requestSubjectold +" <br/>"+
			"Issue Type:"+ 	requestDescriptionold +" <br/>" + 
//			"Date Reported:"+ 	dateReported+" <br/>"+
//			"Old Issue Status:"+ 	statusMail +" <br/>"+
			statusMail+" By:"+ 	tech.toLowerCase() + " <br/>";
			
//		"Solution/"+statusMail+" Comment:"+ requestDescription+	" <br/>"+
//		"Title:"+	Description +" <br/>"+
//		"Thank you,<br/>"+
//		"Customer Care Centre<br/>";
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

    public String getEmailMessageAnnouce(String user, String requestDescription, String complaintId, String Announcetitle, String copy, String Subject)
    {
        String mailDescription;
        String mailHeading;
        String status;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        mailDescription = null;
        mailHeading = null;
        String mailAddress = null;
        status = null;
        con = null;
        ps = null;
        rs = null;
        try
        {
            String dateReported = app.getCodeName((new StringBuilder("select create_date from HD_ANNOUNCEMENT where Announce_id='")).append(complaintId).append("'").toString());
            dateReported = dateReported != "UNKNOWN" ? dateReported : String.valueOf(df.dateConvert(new Date()));
            mailDescription = (new StringBuilder("Subject: <a href='http://172.19.2.116:2012/Oriental?id=")).append(complaintId).append("'>").append(Subject).append("</a> <br/>").append("Hi,  \n").append("The Announcement ").append(complaintId).append(" has been Created.\n").append("Details are as follows :- \n").append("Issue Id: \t").append(complaintId).append(" \n").append("Title:").append(mailHeading).append(" \n").append("Description:").append(mailDescription).append(" \n").append("Date Reported:").append(dateReported).append(" \n").append("Issue Current Status:").append(status).append(" \n").append(" By:").append(user).append("  \n").append("Date: ").append(df.dateConvert(new Date())).append("\n").append("Solution/").append(" Comment:").append(requestDescription).append(" \n").append("Thank you,\n").append("Customer Care Centre\n").toString();
		}
		catch(Exception e)
		{
			System.out.println("Error fetching Announcement emails email->>");
			e.printStackTrace();
		}finally{
			closeConnection(con,ps,rs);
			
		}
		return mailDescription;
	}

}
