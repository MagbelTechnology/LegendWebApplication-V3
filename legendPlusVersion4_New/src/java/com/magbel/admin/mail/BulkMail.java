package com.magbel.admin.mail;

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

import javax.naming.*;
import javax.sql.DataSource;

import java.sql.Connection;  
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

import com.magbel.admin.dao.MagmaDBConnection;
import com.magbel.util.HtmlComboRecord; 
import com.magbel.util.HtmlUtility;;

/**
 *
 * @author Jejelowo B.Festus
 * @since 2008 
 * @version 1.0.0
 */

public class BulkMail
{ 

	SimpleDateFormat sdf;

	final String space = "  ";
	final String comma = ",";
	java.util.Date date;
	SendMailFile mailer;
    SimpleDateFormat timer = null;
	com.magbel.util.DatetimeFormat df;
	public static ArrayList mailList =new ArrayList();
	public static com.magbel.admin.objects.BranchContact brContact;
	HtmlUtility util;
	HtmlComboRecord htmlCombo = new HtmlComboRecord(); 
	public BulkMail()
	{
		sdf = new SimpleDateFormat("dd-MM-yyyy");
		mailer = new SendMailFile();
		df = new com.magbel.util.DatetimeFormat();
		util = new HtmlUtility();
		timer = new SimpleDateFormat("kk:mm:ss");
	}

	public String getEmailByBranch(String branchCode){

		String query = "SELECT EMAIL FROM AD_GB_BRANCH_CONTACT  "+
						"WHERE BRANCH_CODE = '"+branchCode.trim()+"' "+
						"AND EMAIL IS NOT NULL";
		String email =null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try
		{

			con = getConnection("helpDesk");
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			if(rs.next()){
				email = rs.getString(1);
			}

		}
		catch(Exception e)
		{
			System.out.println("Error fetching branch emails ->>"+e);
		}finally{
			closeConnection(con,ps,rs);
		}
		return email;
	}
	public String getCodeByBranchEmail(String email){

		String query = "SELECT BRANCH_CODE  FROM AD_GB_BRANCH_CONTACT  "+
						"WHERE EMAIL = '"+email.trim()+"' "+
						"AND EMAIL IS NOT NULL";
		String code =null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try
		{

			con = getConnection("helpDesk");
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			if(rs.next()){
				code = rs.getString(1);
			}

		}
		catch(Exception e)
		{
			System.out.println("Error fetching branch emails email->>"+e);
		}finally{
			closeConnection(con,ps,rs);
		}
		return code;
	}
	public String[] getBranchRecords(){

		String[] branches = null;
		String query = "SELECT BRANCH_CODE FROM AD_GB_BRANCH";

		ArrayList list = new ArrayList();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try{

			con = getConnection("helpDesk");
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while(rs.next())
			{
				list.add(rs.getString(1));
			}

		}
		catch(Exception e)
		{
			System.out.println("Error fetching branch records ->>"+e);
		}
		finally
		{
			closeConnection(con,ps,rs);
		}

		branches = new String[list.size()];
		for(int x = 0; x < list.size(); x++){
			branches[x] = (String)list.get(x);
		}

		return branches;
	}

	public com.magbel.admin.objects.BranchContact getBranchUsers(String branchCode){

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		com.magbel.admin.objects.BranchContact branchContact = null;

		try
		{
			String query = "SELECT MTID, BRANCH_CODE,  " +
					"STAFF_NAME, EMAIL,  PHONE,  STATUS, USER_ID, CREATE_DATE "+
					" FROM   AD_GB_BRANCH_CONTACT WHERE BRANCH_CODE ='"+branchCode.trim()+"' ";

			con = getConnection("helpDesk");
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while(rs.next()){

				branchContact =  new  com.magbel.admin.objects.BranchContact();
				branchContact.setBranchCode(rs.getString("BRANCH_CODE"));
				branchContact.setStaffName(rs.getString("STAFF_NAME"));
				branchContact.setEmail(rs.getString("EMAIL"));
				branchContact.setPhone(rs.getString("PHONE"));

			}

		}
		catch(Exception ex) {
			System.out.println("Error occured getting branch users for report " + ex.getMessage());
			ex.printStackTrace();
		}
		finally
		{
			closeConnection(con,ps,rs);
		}
		return branchContact;
	}

	//generating excel file for branch users sent mail to.

	public String getPrepareBranchUsers(ArrayList _list, String date, String clrSession) {

		String fileName = "C:/temp/branchUser/";

		String query = null;
		FileOutputStream fileOut = null;
		HSSFWorkbook wb = null;
		HSSFSheet sheet = null;
		HSSFRow  row = null;

		int count = 0;
		com.magbel.admin.objects.BranchContact branchContact = null;

		java.io.File f  =new java.io.File(fileName);
		String fileDate =date;

		try
		{
			if(!f.exists())
			{
				f.mkdir();
			}
			else
			{
				String fDate[] =fileDate.split("-") ;
				fileName = "C:/temp/branchUser/mailList"+fDate[0]+fDate[1]+fDate[2]+"_"+setSession(clrSession).trim()+".xls";
				f = new java.io.File(fileName);
			}

		}catch(Exception ex) {
			System.out.println("Error occured trying create folder which will contain file for branch users : " + ex.getMessage());
			ex.printStackTrace();
		}

		wb = new HSSFWorkbook();
		sheet = wb.createSheet();

		//create a style for the header cell
		HSSFRow headerRow = sheet.createRow((short) 0);
		HSSFCellStyle headerStyle = wb.createCellStyle();
		headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		//create a style for this header columns
		HSSFCellStyle columnHeaderStyle = wb.createCellStyle();
		//columnHeaderStyle.setFillBackgroundColor(HSSFColor.BLUE_GREY.index);
		//columnHeaderStyle.setFillForegroundColor(HSSFColor.BLUE_GREY.index);
		HSSFFont font = wb.createFont();
		font.setColor(HSSFFont.COLOR_RED);
		columnHeaderStyle.setFont(font);

		HSSFCell colHeading1 = headerRow.createCell((short) 0);
		colHeading1.setCellStyle(headerStyle);
		HSSFCell colHeading2 = headerRow.createCell((short) 1);
		colHeading2.setCellStyle(headerStyle);
		HSSFCell colHeading3 = headerRow.createCell((short) 2);
		colHeading3.setCellStyle(headerStyle);
		HSSFCell colHeading4 = headerRow.createCell((short) 3);
		colHeading4.setCellStyle(headerStyle);

		colHeading1.setCellStyle(columnHeaderStyle);
		colHeading2.setCellStyle(columnHeaderStyle);
		colHeading3.setCellStyle(columnHeaderStyle);
		colHeading4.setCellStyle(columnHeaderStyle);

		colHeading1.setCellValue("BRANCH_CODE");
		colHeading2.setCellValue("STAFF_NAME");
		colHeading3.setCellValue("EMAIL");
		colHeading4.setCellValue("PHONE");

		if(_list.size() > 0 && _list != null) {

			for(int i=0; i<_list.size(); i++) {

				branchContact =(com.magbel.admin.objects.BranchContact)_list.get(i);

				row = sheet.createRow(count+1);
				HSSFCell cell1 = row.createCell((short) 0);
				HSSFCell cell2 = row.createCell((short) 1);
				HSSFCell cell3 = row.createCell((short) 2);
				HSSFCell cell4 = row.createCell((short) 3);

				cell1.setCellValue(branchContact.getBranchCode());
				cell2.setCellValue(branchContact.getStaffName());
				cell3.setCellValue(branchContact.getEmail());
				cell4.setCellValue(branchContact.getPhone());

				count++;
			}

		}
		if(count > 0){
			try
			{
				fileOut = new FileOutputStream(f);
				wb.write(fileOut);
			}catch(Exception ex){}
		}

		return fileName;
	}

	//generating excel that contains transactions for the various branches

	public String getPrepareBranchDocument(String branchCode, String valueDate, String clrSession)
	{

		String fileName = "C:/temp/";
		String fileDate =valueDate;

		java.io.File f =null;

		f = new java.io.File(fileName);

		if(!f.exists())
		{
			f.mkdir();
		}
		else
		{
			try
			{
				String fDate[] =fileDate.split("-");
				fileName = "C:/temp/INWARD_CHEQUE_"+fDate[0]+fDate[1]+fDate[2]+"_"+setSession(clrSession.trim())+"_"+branchCode+".xls";
				f = new java.io.File(fileName);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}

		String query = "SELECT ACCT_NO, CHEQUE_NO, BRANCH_CODE, STATUS_CODE, AMOUNT, POST_DATE, BENE,INSTRUMENT_TYPE   "+
						"FROM LEGACY_SYS_RETURN_IC   "+
						"WHERE BRANCH_CODE IS NOT NULL AND BRANCH_CODE = '"+branchCode+"' AND VALUE_DATE = '"+df.dateConvert(valueDate)+"' AND TRAN_SESSN ='"+clrSession.trim()+"'";

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		FileOutputStream fileOut = null;
		HSSFWorkbook wb = null;
		HSSFSheet sheet = null;
		HSSFRow  row = null;
		int count = 0;

	try
	{


		wb = new HSSFWorkbook();
		sheet = wb.createSheet();

		con = getConnection("helpDesk");
		ps = con.prepareStatement(query);
		rs = ps.executeQuery();

		//create a style for the header cell
		HSSFRow headerRow = sheet.createRow((short) 0);
		HSSFCellStyle headerStyle = wb.createCellStyle();
		headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		//create a style for this header columns
		HSSFCellStyle columnHeaderStyle = wb.createCellStyle();
		//columnHeaderStyle.setFillBackgroundColor(HSSFColor.BLUE_GREY.index);
		//columnHeaderStyle.setFillForegroundColor(HSSFColor.BLUE_GREY.index);
		HSSFFont font = wb.createFont();
		font.setColor(HSSFFont.COLOR_RED);
		columnHeaderStyle.setFont(font);

		HSSFCell colHeading1 = headerRow.createCell((short) 0);
		colHeading1.setCellStyle(headerStyle);
		HSSFCell colHeading2 = headerRow.createCell((short) 1);
		colHeading2.setCellStyle(headerStyle);
		HSSFCell colHeading3 = headerRow.createCell((short) 2);
		colHeading3.setCellStyle(headerStyle);
		HSSFCell colHeading4 = headerRow.createCell((short) 3);
		colHeading4.setCellStyle(headerStyle);
		HSSFCell colHeading5 = headerRow.createCell((short) 4);
		colHeading5.setCellStyle(headerStyle);
		HSSFCell colHeading6 = headerRow.createCell((short) 5);
		colHeading6.setCellStyle(headerStyle);
		HSSFCell colHeading7 = headerRow.createCell((short) 6);
		colHeading7.setCellStyle(headerStyle);
		HSSFCell colHeading8 = headerRow.createCell((short) 7);
		colHeading8.setCellStyle(headerStyle);
		
		colHeading1.setCellStyle(columnHeaderStyle);
		colHeading2.setCellStyle(columnHeaderStyle);
		colHeading3.setCellStyle(columnHeaderStyle);
		colHeading4.setCellStyle(columnHeaderStyle);
		colHeading5.setCellStyle(columnHeaderStyle);
		colHeading6.setCellStyle(columnHeaderStyle);
		colHeading7.setCellStyle(columnHeaderStyle);
		colHeading8.setCellStyle(columnHeaderStyle);

		colHeading1.setCellValue("ACCT NO");
		colHeading2.setCellValue("CHEQUE_NO");
		colHeading3.setCellValue("BRANCH_CODE");
		colHeading4.setCellValue("STATUS");
		colHeading5.setCellValue("AMOUNT");
		colHeading6.setCellValue("POST_DATE");
		colHeading7.setCellValue("BENEFICIARY");
		colHeading8.setCellValue("INSTRUMENT TYPE");
		
		while(rs.next())
		{

		  	row = sheet.createRow(count+1);
		  	HSSFCell cell1 = row.createCell((short) 0);
		  	HSSFCell cell2 = row.createCell((short) 1);
		  	HSSFCell cell3 = row.createCell((short) 2);
		  	HSSFCell cell4 = row.createCell((short) 3);
		  	HSSFCell cell5 = row.createCell((short) 4);
		  	HSSFCell cell6 = row.createCell((short) 5);
		  	HSSFCell cell7 = row.createCell((short) 6);
		  	HSSFCell cell8 = row.createCell((short) 7);

		  	String code =rs.getString("STATUS_CODE");

			String query1 ="SELECT REASON FROM RET_REASON WHERE CODE ='"+code.trim()+"'";

			ps1 = con.prepareStatement(query1);
			rs1 = ps1.executeQuery();

			String reason ="";
			  double amount=0.00;
	            amount=rs.getDouble("AMOUNT")/100;
			while(rs1.next()){
				reason =rs1.getString("REASON");
			}

		    cell1.setCellValue(rs.getString("ACCT_NO"));
		  	cell2.setCellValue(rs.getString("CHEQUE_NO"));
		 	cell3.setCellValue(rs.getString("BRANCH_CODE"));
		  	cell4.setCellValue(reason);
		  	cell5.setCellValue(amount);
		  //	cell5.setCellValue(rs.getDouble("AMOUNT"));
		  	cell6.setCellValue(rs.getString("POST_DATE"));
		  	cell7.setCellValue(rs.getString("BENE"));
		  	cell8.setCellValue(rs.getString("INSTRUMENT_TYPE"));

			count++;

		}

        if(count > 0){

			fileOut = new FileOutputStream(f);
			wb.write(fileOut);
	    }
	    else{
			fileName = "NO_DATA";
		}

		}catch(Exception e){
			System.out.println("Error preparing branch report file ->>"+e);
			e.printStackTrace();
		}finally{
			closeConnection(con,ps,rs);
			closeConnection(con,ps1,rs1);
		}

		return fileName+","+Integer.toString(count);
	}

	public String confirmMessage() throws Exception
	{
		String result ="";
		result +="\nThis mail is to confirm that mails has been successfully sent to the various branches!\n\n";
		result +="Furthermore, You can check attachment to see the list of branches and their corresponding emails.\n\n";
		result +="Thank you.";
		return result;
	}

	public static String setSession(String string) throws Exception
	{
		String strSess =null;

		if(string.equalsIgnoreCase("FIRST"))
		{
			strSess = "01";
		}
		else if(string.equalsIgnoreCase("SECOND"))
		{
			strSess ="02";
		}
		return strSess;
	}
	public String sendMail(String subject, String msgText1, String url, String postDate, String clrSession){

		System.out.println("in 1 sendMail>>>>>>>>"); 
		String result ="";
		String fileUsers =null;
		String fileOutx =null;
		int cnt =0, pos =0;
		int cn=0;

       // System.out.println("from "+from);
      //  System.out.println("Password "+Password);
        System.out.println("msgText1 "+msgText1);


		try{
        System.out.println("getting prop file");
			Properties props = new Properties();
			InputStream in = (InputStream)(new FileInputStream(new File(url+"\\helpdesk.properties")));
			props.load(in);
			String host = props.getProperty("mail.smtp.host");
			String from = props.getProperty("mail-user");
			String Password = props.getProperty("mail-password");
			String port = props.getProperty("mail.smtp.port");
			String starttsl = props.getProperty("mail.smtp.starttls.enable");
			String socketFactory = props.getProperty("mail.smtp.socketFactory.class");
			String auth = props.getProperty("enable-authentication");
			String debug = props.getProperty("mail.smtp.debug");
			String fallback = props.getProperty("mail.smtp.socketFactory.fallback");
			System.out.println("getting branch code");
			String[] branchCodes = getBranchRecords();

			String[] cc = {""};
			String[] bcc = {""};

			for(int x = 0; x < branchCodes.length; x++)
			{
				System.out.println("saving and sending mail");
				String to = getEmailByBranch(branchCodes[x]);
				System.out.println("to "+to);
				fileOutx = getPrepareBranchDocument(branchCodes[x], postDate, clrSession);
				brContact = getBranchUsers(branchCodes[x]);
				String[] filedata =fileOutx.split(",");
				String fileOut =filedata[0];
				System.out.println("fileOut "+fileOut);
				int reportCount =Integer.parseInt(filedata[1]);
                System.out.println("reportCount "+reportCount);
					if((to != null && to.length() > 0)  && (reportCount > 0) && !(fileOut.equalsIgnoreCase("NO_DATA")))

					//if(to != null && to.length() > 0)
					{ 
						String []senderName=from.split("@");
						System.out.println("Sender Mail Address=== "+from);
							if(Mail.sendMail(senderName[0],Password,host,port,starttsl,auth,(new Boolean(debug)).booleanValue(),socketFactory,fallback,from,to,cc,bcc,subject,msgText1,fileOut))
							{
								mailList.add(brContact);
								result ="SUCCESSFUL";
								System.out.println("in sendMail result=== "+result);
							}
							else 
							{
								result = "FAILURE";
								System.out.println("in sendMail result=== "+result);
							}
							cn++;
					}

					if((mailList != null) && (mailList.size()>0))
					{
						fileUsers = getPrepareBranchUsers(mailList, postDate, clrSession);
					}
					cnt++;
				}

				String messContent =confirmMessage();
				String strSubject ="Confirmation Mail List";

				if(result.indexOf("SUCCESSFUL")>=0)
				{
					String []senderName=from.split("@");
					if(Mail.sendMail(senderName[0],Password,host,port,starttsl,auth,(new Boolean(debug)).booleanValue(),socketFactory,fallback,from,from,cc,bcc,strSubject,messContent,fileUsers))
					{

						result ="SUCCESS";
						System.out.println("when correct result==="+result);
						cleanFileDirectory();
					}
					cleanFileDirectory();
				}

		}
		catch(Exception error)
		{
			System.out.println("Error sending mail..."+error);
			error.printStackTrace();
			cleanFileDirectory();
		}
		return result;
	}
	public String sendMail(String from,String Password, String subject, String msgText1, String url, String postDate, String clrSession){

		System.out.println("in 1 sendMail>>>>>>>>"); 
		String result ="";
		String fileUsers =null;
		String fileOutx =null;
		int cnt =0, pos =0;
		int cn=0;

       // System.out.println("from "+from);
      //  System.out.println("Password "+Password);
        System.out.println("msgText1 "+msgText1);


		try{
        System.out.println("getting prop file");
			Properties props = new Properties();
			InputStream in = (InputStream)(new FileInputStream(new File(url+"\\helpdesk.properties")));
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
			System.out.println("getting branch code");
			String[] branchCodes = getBranchRecords();

			String[] cc = {""};
			String[] bcc = {""};

			for(int x = 0; x < branchCodes.length; x++)
			{
				System.out.println("saving and sending mail");
				String to = getEmailByBranch(branchCodes[x]);
				System.out.println("to "+to);
				fileOutx = getPrepareBranchDocument(branchCodes[x], postDate, clrSession);
				brContact = getBranchUsers(branchCodes[x]);
				String[] filedata =fileOutx.split(",");
				String fileOut =filedata[0];
				System.out.println("fileOut "+fileOut);
				int reportCount =Integer.parseInt(filedata[1]);
                System.out.println("reportCount "+reportCount);
					if((to != null && to.length() > 0)  && (reportCount > 0) && !(fileOut.equalsIgnoreCase("NO_DATA")))

					//if(to != null && to.length() > 0)
					{ 
						String []senderName=from.split("@");
							if(Mail.sendMail(senderName[0],Password,host,port,starttsl,auth,(new Boolean(debug)).booleanValue(),socketFactory,fallback,from,to,cc,bcc,subject,msgText1,fileOut))
							{
								mailList.add(brContact);
								result ="SUCCESSFUL";
								System.out.println("in sendMail result==="+result);
							}
							else 
							{
								result = "FAILURE";
								System.out.println("in sendMail result==="+result);
							}
							cn++;
					}

					if((mailList != null) && (mailList.size()>0))
					{
						fileUsers = getPrepareBranchUsers(mailList, postDate, clrSession);
					}
					cnt++;
				}

				String messContent =confirmMessage();
				String strSubject ="Confirmation Mail List";

				if(result.indexOf("SUCCESSFUL")>=0)
				{
					String []senderName=from.split("@");
					if(Mail.sendMail(senderName[0],Password,host,port,starttsl,auth,(new Boolean(debug)).booleanValue(),socketFactory,fallback,from,from,cc,bcc,strSubject,messContent,fileUsers))
					{

						result ="SUCCESS";
						System.out.println("when correct result==="+result);
						cleanFileDirectory();
					}
					cleanFileDirectory();
				}

		}
		catch(Exception error)
		{
			System.out.println("Error sending mail..."+error);
			error.printStackTrace();
			cleanFileDirectory();
		}
		return result;
	}
	public String sendMail(String from,String Password, String subject, String msgText1, String url, String postDate, String clrSession,String username){

		System.out.println("in 2sendMail>>>>>>>>");
		String result ="";
		String fileUsers =null;
		String fileOutx =null;
		int cnt =0, pos =0;
		int cn=0;

       // System.out.println("from "+from);
      //  System.out.println("Password "+Password);
        System.out.println("msgText1 "+msgText1);


		try{
        System.out.println("getting prop file");
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
			System.out.println("getting branch code");
			String[] branchCodes = getBranchRecords();

			String[] cc = {""};
			String[] bcc = {""};

			for(int x = 0; x < branchCodes.length; x++)
			{
				System.out.println("saving and sending mail");
				String to = getEmailByBranch(branchCodes[x]);
				System.out.println("to "+to);
				fileOutx = getPrepareBranchDocument(branchCodes[x], postDate, clrSession);
				brContact = getBranchUsers(branchCodes[x]);
				String[] filedata =fileOutx.split(",");
				String fileOut =filedata[0];
				System.out.println("fileOut "+fileOut);
				int reportCount =Integer.parseInt(filedata[1]);
                System.out.println("reportCount "+reportCount);
					if((to != null && to.length() > 0)  && (reportCount > 0) && !(fileOut.equalsIgnoreCase("NO_DATA")))

					//if(to != null && to.length() > 0)
					{ 
						String []senderName=username.split("@");
							if(Mail.sendMail(senderName[0],Password,host,port,starttsl,auth,(new Boolean(debug)).booleanValue(),socketFactory,fallback,from,to,cc,bcc,subject,msgText1,fileOut))
							{
								mailList.add(brContact);
								result ="SUCCESSFUL";
								System.out.println("in sendMail result==="+result);
							}
							else 
							{
								result = "FAILURE";
								System.out.println("in sendMail result==="+result);
							}
							cn++;
					}

					if((mailList != null) && (mailList.size()>0))
					{
						fileUsers = getPrepareBranchUsers(mailList, postDate, clrSession);
					}
					cnt++;
				}

				String messContent =confirmMessage();
				String strSubject ="Confirmation Mail List";

				if(result.indexOf("SUCCESSFUL")>=0)
				{
					String []senderName=username.split("@"); 
					if(Mail.sendMail(senderName[0],Password,host,port,starttsl,auth,(new Boolean(debug)).booleanValue(),socketFactory,fallback,from,from,cc,bcc,strSubject,messContent,fileUsers))
					{

						result ="SUCCESS";
						System.out.println("when correct result==="+result);
						cleanFileDirectory();
					}
					cleanFileDirectory();
				}

		}
		catch(Exception error)
		{
			System.out.println("Error sending mail..."+error);
			error.printStackTrace();
			cleanFileDirectory();
		}
		return result;
	}

public void cleanFileDirectory()
{
	String downloadFolder = "C:/temp/";
	java.io.File toSendFile = null;
	java.io.File toSendFileUsers =null;

	String fileUsers ="C:/temp/branchUser/";

	try
	{
		toSendFile = new java.io.File(downloadFolder);

		toSendFileUsers =new java.io.File(fileUsers);

	   if(toSendFile.isDirectory())
	   {
		   File fh[] =toSendFile.listFiles();
		   if(fh.length > 0)
		   {
				for(int i=0; i<fh.length; i++)
				{
					fh[i].delete();
				}
			}
	   }
	   if(!toSendFileUsers.exists())
	   {
		   toSendFileUsers.mkdir();
	   }
	   else
	   {
		   if(toSendFileUsers.isDirectory())
		   {
		   		File fUser[] =toSendFileUsers.listFiles();
		   		if(fUser.length > 0)
		   		{
					for(int j=0; j<fUser.length; j++)
					{
						fUser[j].delete();
					}
		   		}
	   	   }
		}
	}
	catch(Exception ex)
	{
		System.out.println("Error cleaning file directory "+ex.getMessage());
	}

}

/**
	 * closeConnection
	 *
	 * @param con Connection
	 * @param rs ResultSet
	 * @param ps PreparedStatement
	 * @todo Implement this ememo.dao.ConnectionDAO method
	 */
	public void closeConnection(Connection con, PreparedStatement ps,
								ResultSet rs) {
		try {
			if (ps != null) {
				ps.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (Exception ex) {
			System.out.println("WARNING:Error closing Connection ->" + ex);
		}
    }


    public Connection getConnection(String jndiName) throws Exception {
          Connection con = null;

        try {

            Context initContext = new InitialContext();
            DataSource ds = (DataSource) initContext.lookup(
                    "java:comp/env/jdbc/" + jndiName);
            con = ds.getConnection();

        } catch (Exception _e) {
            System.out.println("WARNING::DataAccess::getConnection()!" + _e);
            con = null;
        }

        return con;
    }
    public String sendMailComment(String from,String Password, String subject, String msgText1, String url, String postDate, String clrSession,String username)
	{

		System.out.println("in sendMail>>>>>>>>");
		String result ="";
		String fileUsers =null;
		String fileOutx =null;
		int cnt =0, pos =0;
		int cn=0;
 
         

		try{
        System.out.println("getting prop file");
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
			System.out.println("getting branch code");
			String[] mail = getMailRecords();

			String[] cc = {""};
			String[] bcc = {""};

			for(int x = 0; x < mail.length; x++)
			{
				System.out.println("saving and sending mail"); 
				String to =  mail[x];
				System.out.println("to "+to);
				 
				String []senderName=username.split("@");
					if(Mail.sendMail2(senderName[0],Password,host,port,starttsl,auth,(new Boolean(debug)).booleanValue(),socketFactory,fallback,from,to,cc,bcc,subject,msgText1))
					{
						mailList.add(brContact);
						result ="SUCCESSFUL";
						System.out.println("in sendMail result==="+result);
					}
					else 
					{
						result = "FAILURE";
						System.out.println("in sendMail result==="+result);
					}
				 
				}

			 
				 

		}
		catch(Exception error)
		{
			System.out.println("Error sending mail..."+error);
			error.printStackTrace();
			cleanFileDirectory();
		}
		return result;
	}
    public String sendMailAuthen(String from,String Password, String subject, String msgText1, String url, String postDate, String clrSession,String username)
	{

		System.out.println("in sendMail>>>>>>>>");
		String result ="";
		String fileUsers =null;
		String fileOutx =null;
		int cnt =0, pos =0;
		int cn=0;
 
         

		try{
        System.out.println("getting prop file");
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
			System.out.println("getting branch code");
			String[] mail = getMailRecords();

			String[] cc = {""};
			String[] bcc = {""};

			for(int x = 0; x < mail.length; x++)
			{
				System.out.println("saving and sending mail"); 
				String to =  mail[x];
				System.out.println("to "+to);
				 
				String []senderName=username.split("@");
					if(Mail.sendMailAuthenticate(senderName[0],Password,host,port,starttsl,auth,(new Boolean(debug)).booleanValue(),socketFactory,fallback,from,to,cc,bcc,subject,msgText1))
					{
						//mailList.add(brContact);
						result ="SUCCESSFUL";
						System.out.println("in sendMail result==="+result);
					}
					else 
					{
						result = "FAILURE";
						System.out.println("in sendMail result==="+result);
					}
				 
				}

			 
				 

		}
		catch(Exception error)
		{
			System.out.println("Error sending mail..."+error);
			error.printStackTrace();
			cleanFileDirectory();
		}
		return result;
	}
    public String sendMailTo(String to, String subject, String msgText1,String url)
	{

		System.out.println("in sendMail>>>>>>>>");
		String result ="";
		String fileUsers =null;
		String fileOutx =null;
		int cnt =0, pos =0;
		int cn=0;
		 

		try{
        System.out.println("getting prop file");
			Properties props = new Properties();
			InputStream in = (InputStream)(new FileInputStream(new File(url+"\\db-config.properties")));
			props.load(in);
			String host = props.getProperty("mail.smtp.host");
			String authenticator = props.getProperty("mail-user");
			String password = props.getProperty("mail.password");
			String port = props.getProperty("mail.smtp.port");
			String starttsl = props.getProperty("mail.smtp.starttls.enable");
			String socketFactory = props.getProperty("mail.smtp.socketFactory.class");
			String auth = props.getProperty("enable-authentication");
			String debug = props.getProperty("mail.smtp.debug");
			String fallback = props.getProperty("mail.smtp.socketFactory.fallback");
			 
		      
		        
			String []cc={"a@j.com,c@h.com"};
			String []bcc={"ga@j.com,jc@h.com"};
			String []senderName=authenticator.split("@"); 
			
			 
		         
			if(Mail.sendMail(senderName[0],password,host,port,starttsl,auth,(new Boolean(debug)).booleanValue(),socketFactory,fallback,authenticator,to,cc,bcc,subject,msgText1))
			{
				//mailList.add(brContact);
				result ="SUCCESSFUL";
				System.out.println("in sendMail result==="+result);
			}
			else 
			{
				result = "FAILURE";
				System.out.println("in sendMail result==="+result);
			}
			 

		 
			 
				 

		}
		catch(Exception error)
		{
			System.out.println("Error sending mail..."+error);
			error.printStackTrace();
			cleanFileDirectory();
		}
		return result;
	}
	public String[] getMailRecords() 
	{

		String[] mails = null;
		String mailAdrresses ="";
		String query = "SELECT clearing_mail FROM ad_gb_company";

	 

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try{

			con = getConnection("helpDesk");
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while(rs.next())
			{
				 mailAdrresses =rs.getString(1);
			}

		}
		catch(Exception e)
		{
			System.out.println("Error fetching branch records ->>"+e);
		}
		finally
		{
			closeConnection(con,ps,rs);
		}

		mails=mailAdrresses.split(",");

		return mails;
}
	public String sendMail(String from,String Password, String subject, String msgText1, String url, String postDate, String clrSession,String username,String recipients){

		System.out.println("in 2sendMail>>>>>>>>");
		String result ="";
		String fileUsers =null;
		String fileOutx =null;
		int cnt =0, pos =0;
		int cn=0;

       // System.out.println("from "+from);
      //  System.out.println("Password "+Password);
        System.out.println("msgText1 "+msgText1);


		try{
        System.out.println("getting prop file");
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
			System.out.println("getting branch code");
			//String[] branchCodes = getBranchRecords();
			String[] codeFromMail = recipients.split(",");
			String[] cc = {""};
			String[] bcc = {""};

			for(int x = 0; x < codeFromMail.length; x++)
			{
				System.out.println("saving and sending mail"); 
				String code=getCodeByBranchEmail(codeFromMail[x]);
				//String to = getEmailByBranch(branchCodes[x]);
				String to = codeFromMail[x];
				System.out.println("to "+to);
				fileOutx = getPrepareBranchDocument(code, postDate, clrSession);
				brContact = getBranchUsers(code);
				String[] filedata =fileOutx.split(",");
				String fileOut =filedata[0];
				System.out.println("fileOut "+fileOut);
				int reportCount =Integer.parseInt(filedata[1]);
                System.out.println("reportCount "+reportCount);
					if((to != null && to.length() > 0)  && (reportCount > 0) && !(fileOut.equalsIgnoreCase("NO_DATA")))

					//if(to != null && to.length() > 0)
					{ 
						String []senderName=username.split("@");
							if(Mail.sendMail(senderName[0],Password,host,port,starttsl,auth,(new Boolean(debug)).booleanValue(),socketFactory,fallback,from,to,cc,bcc,subject,msgText1,fileOut))
							{
								mailList.add(brContact);
								result ="SUCCESSFUL";
								System.out.println("in sendMail result==="+result);
							}
							else 
							{
								result = "FAILURE";
								System.out.println("in sendMail result==="+result);
							}
							cn++;
					}

					if((mailList != null) && (mailList.size()>0))
					{
						fileUsers = getPrepareBranchUsers(mailList, postDate, clrSession);
					}
					cnt++;
				}

				String messContent =confirmMessage();
				String strSubject ="Confirmation Mail List";

				if(result.indexOf("SUCCESSFUL")>=0)
				{
					String []senderName=username.split("@"); 
					if(Mail.sendMail(senderName[0],Password,host,port,starttsl,auth,(new Boolean(debug)).booleanValue(),socketFactory,fallback,from,from,cc,bcc,strSubject,messContent,fileUsers))
					{

						result ="SUCCESS";
						System.out.println("when correct result==="+result);
						cleanFileDirectory();
					}
					cleanFileDirectory();
				}

		}
		catch(Exception error)
		{
			System.out.println("Error sending mail..."+error);
			error.printStackTrace();
			cleanFileDirectory();
		}
		return result;
	}
	public String getEmailMessage(String transactionType,String categoryCode,String helpType,String technician,String user,String requestDescription,String complaintId)
	{     
		System.out.println("<<<<<<<<Inside getEmailMessage>>>>>>>");
		String query = " SELECT mail_description,mail_heading,mail_address,Status  FROM HD_MAIL_STATEMENT  WHERE  transaction_Type = '"+transactionType.trim()+"'"+
						"  And category_Code = '"+categoryCode.trim()+"'  And help_Type = '"+helpType.trim()+"'  " ;
		String mailDescription =null;
		String mailHeading=null;
		String mailAddress=null;
		String status=null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null; 
		System.out.println("<<<<<<<< query getEmailMessage>>>>>>>"+query);
		try
		{

			con = getConnection("helpDesk"); 
			ps = con.prepareStatement(query); 
			rs = ps.executeQuery();
			System.out.println("--query->>"+query);  
			if(rs.next()){
				mailDescription = rs.getString("mail_description");
				mailHeading=rs.getString("mail_heading");
				mailAddress=rs.getString("mail_address");
				status=rs.getString("Status");
			}

		String statusMail=util.findObject("select status_description from hd_status where status_code='"+transactionType+"'");		
		String	Subject="Subject: Notification for Issue : "+complaintId;
		String tech=util.findObject("select full_name from am_gb_user where user_id='"+technician+"'");
			tech=tech=="UNKNOWN" ? "work in progress" :tech;
		 
		String dateReported=util.findObject("select create_date from HD_COMPLAINT where complaint_id='"+complaintId+"'");	
		dateReported=dateReported=="UNKNOWN" ? String.valueOf(df.dateConvert(new java.util.Date())) :dateReported;
		mailDescription=
		"Hi,  \n"+
		"The issue "+complaintId+" has been "+statusMail+".\n"+ 
		"Details are as follows :- \n"+
		"Issue Id: 	"+complaintId+" \n"+
		"Title:"+	mailHeading +" \n"+
		"Description:"+ 	requestDescription +" \n" + 
		"Resolved By:"+ 	tech.toLowerCase() + " \n"+
		"Date Reported:"+ 	dateReported+" \n"+
		"Issue Current Status:"+ 	status +" \n"+
		statusMail+" By:"+ 	user+"  \n"+
		"Date "+statusMail+":"+ 	df.dateConvert(new java.util.Date())+"\n"+
		"Solution/"+statusMail+" Comment:"+ requestDescription+	" \n"+
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
			} 
			String ResolvedBy = "";   
		String Assignee=util.findObject("SELECT technician, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
		//System.out.println("=====Assignee Before ===== "+Assignee);
	  	  if(Assignee == null || Assignee.equalsIgnoreCase("")){
			 ResolvedBy=util.findObject("SELECT UnitHead, email   FROM   AM_AD_DEPARTMENT WHERE Dept_code = '"+categoryCode+"'");
			// System.out.println("=====Assignee is Null ===== "+ResolvedBy);
		}else{
		ResolvedBy =util.findObject("select full_name from am_gb_user where user_id  = '"+Assignee+"'");
		// System.out.println("=====Assignee is Not Null ===== "+ResolvedBy);
		}  
	//	System.out.println("=====ResolvedBy ===== "+ResolvedBy);
		String statusMail=util.findObject("select status_description from hd_status where status_code='"+status+"'");
	//	System.out.println("=====statusMail ===== "+statusMail);
		//String	Subject="Subject: Notification for Issue : "+complaintId;
		String tech=util.findObject("select full_name from am_gb_user where user_id='"+user+"'");		
			tech=tech=="UNKNOWN" ? "work in progress" :tech;
	//		System.out.println("=====tech ===== "+tech);
		String dateReported=util.findObject("select create_date from "+FileName+" where "+FieldName+"='"+complaintId+"'");	
		dateReported=dateReported=="UNKNOWN" ? String.valueOf(df.dateConvert(new java.util.Date())) :dateReported;
	//	System.out.println("=====dateReported ===== "+dateReported);
		System.out.println("=====Change ===== "+Change);
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
			"Subject: <a href='http://172.19.2.116:2012/Oriental?id="+complaintId+"&Status="+status+"'>"+Subject+"</a> <br/>"+		
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
			"Subject: <a href='http://172.19.2.116:2012/Oriental?id="+complaintId+"&Status="+status+"'>"+Subject+"</a> <br/>"+		
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

	public String getEmailMessageAnnouce(String user,String requestDescription,String complaintId,String Announcetitle,String copy, String Subject)
	{    
	//	System.out.println("=====Inside getEmailMessageAnnouce ====== ");
	//	String query = " SELECT mail_description,mail_heading,mail_address,Status  FROM HD_MAIL_STATEMENT  WHERE  transaction_Type = '"+transactionType.trim()+"'"+
	//					"  And category_Code = '"+categoryCode.trim()+"' And mail_type = '"+copy.trim()+"' And help_Type = '"+helpType.trim()+"'  " ;
		String mailDescription =null;
		String mailHeading=null;
		String mailAddress=null;
		String status=null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try
		{
/* 
			con = getConnection("helpDesk"); 
			ps = con.prepareStatement(query); 
			rs = ps.executeQuery();
			System.out.println("--query->>"+query);  
			if(rs.next()){
				mailDescription = rs.getString("mail_description");
				mailHeading=rs.getString("mail_heading");
				mailAddress=rs.getString("mail_address");
				status=rs.getString("Status");
			}
*/
//		String statusMail=util.findObject("select status_description from hd_status where status_code='"+transactionType+"'");		
//		String	Subject="Subject: Notification for Issue : "+complaintId;
//		String tech=util.findObject("select full_name from am_gb_user where user_id='"+technician+"'");
//			tech=tech=="UNKNOWN" ? "work in progress" :tech;
		 
		String dateReported=util.findObject("select create_date from HD_ANNOUNCEMENT where Announce_id='"+complaintId+"'");	
		dateReported=dateReported=="UNKNOWN" ? String.valueOf(df.dateConvert(new java.util.Date())) :dateReported;
		mailDescription=
		"Subject: <a href='http://172.19.2.116:2012/Oriental?id="+complaintId+"'>"+Subject+"</a> <br/>"+			
		"Hi,  \n"+
		"The Announcement "+complaintId+" has been Created.\n"+ 
		"Details are as follows :- \n"+
		"Issue Id: 	"+complaintId+" \n"+
		"Title:"+	mailHeading +" \n"+
		"Description:"+ 	mailDescription +" \n" + 
		//"Resolved By:"+ 	tech.toLowerCase() + " \n"+
		"Date Reported:"+ 	dateReported+" \n"+
		"Issue Current Status:"+ 	status +" \n"+
		" By:"+ 	user+"  \n"+
		"Date: "+ 	df.dateConvert(new java.util.Date())+"\n"+
		"Solution/"+ " Comment:"+ requestDescription+	" \n"+
		"Thank you,\n"+
		"Customer Care Centre\n";

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
	public String getEmailMessageCopy(String transactionType,String categoryCode,String mailType,String helpType)
	{

		String query = " SELECT mail_description,mail_heading,mail_address,Status  FROM HD_MAIL_STATEMENT  WHERE  transaction_Type = '"+transactionType.trim()+"'"+
						"  And category_Code = '"+categoryCode.trim()+"'  And mail_Type = '"+mailType.trim()+"' And help_Type = '"+helpType.trim()+"'  " ;
		String mailDescription =null;
		String mailHeading=null;
		String mailAddress=null;
		String status=null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try
		{

			con = getConnection("helpDesk"); 
			ps = con.prepareStatement(query); 
			rs = ps.executeQuery();
	 //		System.out.println("--query->>"+query); 
			if(rs.next()){
				mailDescription = rs.getString("mail_description");
				mailHeading=rs.getString("mail_heading");
				mailAddress=rs.getString("mail_address");
				status=rs.getString("Status");
			}

			
		String	Subject="Subject: Closure Notification for Issue : IT200905211028";

		mailDescription=
		"Hi,  \n"+
		"The issue IT200905211028 has been closed.\n"+
		"Details are as follows :- \n"+
		"Issue Id: 	IT200905211028 \n"+
		"Title:"+	mailHeading +" \n"+
		"Description:"+ 	mailDescription +" \n" + 
		"Resolved By:"+ 	"olugbenga.orunto \n"+
		"Date Reported:"+ 	df.dateConvert(new java.util.Date())+" \n"+
		"Issue Current Status:"+ 	status +" \n"+
		"Closed By:"+ 	"olugbenga.orunto \n"+ 
		"Solution/Closure Comment:"+ 	"Root cause: Faulty modem. Resolution: The modem problem was fixed. \n"+
		"Thank you,\n"+
		"IT Service Desk / IT Care\n";

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
    public static void main(String args[])
    {
    	
    	System.out.println("Error fetching branch \n emails email->>");
    }
}
