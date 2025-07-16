package com.magbel.ia.legend; 

import com.magbel.ia.bus.AccountChartServiceBus;
import com.magbel.ia.bus.ApprovalRecords;
import com.magbel.ia.dao.PersistenceServiceDAO;
import com.magbel.ia.vao.Customer;
import com.magbel.ia.vao.ExcelBean;
import com.magbel.ia.vao.PaymentCategory;
import com.magbel.ia.vao.SmsTransaction;
import com.magbel.legend.vao.RFID;
//import com.magbel.util.ApplicationHelper;
import com.magbel.util.DatetimeFormat;
import com.magbel.util.HtmlUtilily;
//import com.mindcom.security.Licencer;



import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import com.magbel.ia.vao.Result;
import com.magbel.ia.vao.Transaction;
import com.magbel.util.DataConnect;
//import com.mindcom.security.Licencer;



import java.sql.SQLException;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import magma.net.dao.MagmaDBConnection;

/**
 * @author Lekan Matanmi
 * @Entities company,AssetmanagerInfo,Driver,Location,categoryClasses, ASSETMAKE
 */
public class CompanyHandler extends PersistenceServiceDAO	 {
//	public class CustomerHandler extends PersistenceServiceDAO	
	Connection con = null;
 //   private ApplicationHelper helper;
	private MagmaDBConnection dbConnection;
    AccountChartServiceBus serviceBus;
	Statement stmt = null;
	ApprovalRecords Apprecord;
	PreparedStatement ps = null;
	HtmlUtilily htmlUtil;
	ResultSet rs = null;

	DataConnect dc;
  
	SimpleDateFormat sdf;

	final String space = "  ";

	final String comma = ",";  

	java.util.Date date;

	com.magbel.util.DatetimeFormat df;

	public CompanyHandler() {

		sdf = new SimpleDateFormat("dd-MM-yyyy");
		df = new com.magbel.util.DatetimeFormat();
		System.out.println("USING_ " + this.getClass().getName());
		dbConnection = new MagmaDBConnection();
	}


	
public java.util.ArrayList getRfidRecords()
 {
 	java.util.ArrayList _list = new java.util.ArrayList();
 	String date = String.valueOf(df.dateConvert(new java.util.Date()));
 //	System.out.println("====Verify Rfid Server Date-----> "+date);
 	date = date.substring(0, 10);
 	String finacleTransId= null;
	String query = "SELECT *FROM ST_INVENTORY_VERIFY_RFID WHERE PROCESSED =  'N' ";
//	Transaction transaction = null;   
//	System.out.println("====query in getRfidRecords-----> "+query);
	Connection c = null;
	ResultSet rs = null;
	Statement s = null;  

	try {
		    c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next())
			   {				
				String rfid = rs.getString("RFID_TAG");
				String mtid = rs.getString("MTID");
				String location = rs.getString("LOCATION");
				String scannStatus = rs.getString("SCANN_STATUS");
				String scannType = rs.getString("SCANN_TYPE");
				//String groupid = rs.getString("groupid");
				String createDateTime = rs.getString("CREATE_DATETIME");
				String createDate = rs.getString("CREATE_DATE");
				String processed = rs.getString("PROCESSED");
//				String companyCode = rs.getString("COMP_CODE");
				RFID Rfid = new RFID(); 
				Rfid.setMtid(Integer.parseInt(mtid));
				Rfid.setRfidTag(rfid);
				Rfid.setLocation(location);
				Rfid.setScannStatus(scannStatus);
				Rfid.setIScannType(scannType);
			//	Rfid.setGroupid(groupid);
				Rfid.setCreateDate(createDate);
				Rfid.setCreateDateTime(createDateTime);
				Rfid.setProcessed(processed);
				_list.add(Rfid);
			   }
	 }
				 catch (Exception e)
					{
						e.printStackTrace();
					}
					finally
					{
						closeConnection(c, s, rs);
					}
 	return _list;
 }    
  
public java.util.ArrayList getRNewRfidRecords()
 {
 	java.util.ArrayList list1 = new java.util.ArrayList();
 	String date = String.valueOf(df.dateConvert(new java.util.Date()));
// 	System.out.println("==== New Rfid Server in getRNewRfidRecords-----> "+date);
 	date = date.substring(0, 10);  
 	String finacleTransId= null;
	String query = "SELECT *FROM ST_INVENTORY_RFID_UNUSED WHERE PROCESSED =  'N' ";
//	Transaction transaction = null;
//	System.out.println("==== query in getRNewRfidRecords-----> "+query);
	Connection c = null;
	ResultSet rs = null;
	Statement s = null;  

	try {
		    c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next())
			   {				
				String rfid = rs.getString("RFID_TAG");
				String mtid = rs.getString("MTID");
				String location = rs.getString("LOCATION");
				String scannStatus = rs.getString("SCANN_STATUS");
				String scannType = rs.getString("SCANN_TYPE");
				//String groupid = rs.getString("groupid");
				String createDateTime = rs.getString("CREATE_DATETIME");
				String createDate = rs.getString("CREATE_DATE");
				String processed = rs.getString("PROCESSED");
//				String companyCode = rs.getString("COMP_CODE");
	//			System.out.println("==== rfid in getRNewRfidRecords-----> "+rfid);
				RFID Rfid = new RFID(); 
				Rfid.setMtid(Integer.parseInt(mtid));
				Rfid.setRfidTag(rfid);
				Rfid.setLocation(location);
				Rfid.setScannStatus(scannStatus);
				Rfid.setIScannType(scannType);
			//	Rfid.setGroupid(groupid);
				Rfid.setCreateDate(createDate);
				Rfid.setCreateDateTime(createDateTime);
				Rfid.setProcessed(processed);
				list1.add(Rfid);
			   }
	 }
				 catch (Exception e)
					{
						e.printStackTrace();
					}
					finally
					{
						closeConnection(c, s, rs);
					}
 	return list1;
 }


public java.util.ArrayList getRfidRecordsforInput()
 {
 	java.util.ArrayList _list = new java.util.ArrayList();
 	String date = String.valueOf(df.dateConvert(new java.util.Date()));
// 	System.out.println("====Rfid Server Date-----> "+date);
 	date = date.substring(0, 10);
 	String finacleTransId= null;
		String query = " SELECT *FROM ST_INVENTORY_RFID_UNUSED WHERE SCANN_STATUS =  'I' ";
//	Transaction transaction = null;

	Connection c = null;
	ResultSet rs = null;
	Statement s = null;  

	try {
		    c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next())
			   {				
				String rfid = rs.getString("RFID_TAG");
				String mtid = rs.getString("MTID");
				String location = rs.getString("LOCATION");
				String scannStatus = rs.getString("SCANN_STATUS");
				String scannType = rs.getString("SCANN_TYPE");
				//String groupid = rs.getString("groupid");
				String createDateTime = rs.getString("CREATE_DATETIME");
				String createDate = rs.getString("CREATE_DATE");
				String processed = rs.getString("PROCESSED");
				int userId = rs.getInt("USER_ID");
//				String companyCode = rs.getString("COMP_CODE");
				RFID Rfid = new RFID(); 
				Rfid.setMtid(Integer.parseInt(mtid));
				Rfid.setRfidTag(rfid);
				Rfid.setLocation(location);
				Rfid.setScannStatus(scannStatus);
				Rfid.setIScannType(scannType);
		//		Rfid.setGroupid(groupid);
				Rfid.setCreateDate(createDate);
				Rfid.setCreateDateTime(createDateTime);
				Rfid.setProcessed(processed);
				_list.add(Rfid);
			   }
	 }
				 catch (Exception e)
					{
						e.printStackTrace();
					}
					finally
					{
						closeConnection(c, s, rs);
					}
 	return _list;
 }


public java.util.ArrayList getRfidRecordsforOutput()
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(df.dateConvert(new java.util.Date()));
//	System.out.println("====Rfid Server Date-----> "+date);
	date = date.substring(0, 10);
	String finacleTransId= null;
	String query = " SELECT *FROM ST_INVENTORY_RFID_UNUSED WHERE SCANN_STATUS =  'O' ";
//Transaction transaction = null;

Connection c = null;
ResultSet rs = null;
Statement s = null;  

try {
	    c = getConnection();
		s = c.createStatement();
		rs = s.executeQuery(query);
		while (rs.next())
		   {				
			String rfid = rs.getString("RFID_TAG");
			String mtid = rs.getString("MTID");
			String location = rs.getString("LOCATION");
			String scannStatus = rs.getString("SCANN_STATUS");
			String scannType = rs.getString("SCANN_TYPE");
			//String groupid = rs.getString("groupid");
			String createDateTime = rs.getString("CREATE_DATETIME");
			String createDate = rs.getString("CREATE_DATE");
			String processed = rs.getString("PROCESSED");
			int userId = rs.getInt("USER_ID");
//			String companyCode = rs.getString("COMP_CODE");
			RFID Rfid = new RFID(); 
			Rfid.setMtid(Integer.parseInt(mtid));
			Rfid.setRfidTag(rfid);
			Rfid.setLocation(location);
			Rfid.setScannStatus(scannStatus);
			Rfid.setIScannType(scannType);
//			Rfid.setGroupid(groupid);
			Rfid.setCreateDate(createDate);
			Rfid.setCreateDateTime(createDateTime);
			Rfid.setProcessed(processed);
			_list.add(Rfid);
		   }
 }
			 catch (Exception e)
				{
					e.printStackTrace();
				}
				finally
				{
					closeConnection(c, s, rs);
				}
	return _list;
}

public boolean updateSqlRecords( String iso,String finacleTransId)
	{
		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String date = String.valueOf(df.dateConvert(new java.util.Date()));
	 	date = date.substring(0, 10);
		String query = "UPDATE am_raisentry_transaction SET iso=?   where transaction_date >'"+date+"' and finacle_Trans_Id=? ";
//		System.out.println("=====updateSqlRecords query=====> "+query);
		try {   
			con = getConnection();
			ps = con.prepareStatement(query);
     
			ps.setString(1, iso);
			ps.setString(2, finacleTransId);
			done = (ps.executeUpdate() != -1);
		} catch (Exception e) { 
			e.printStackTrace();
			closeConnection(con, ps);
		}
		return done;
	}

public final boolean createRfidforUse(RFID rfid,String location)
{
    Connection con;
    ResultSet rs;
    PreparedStatement ps;
    boolean successtransaction;
    con = null;
    rs = null;
    Statement stmt = null;
    ps = null;
    successtransaction = false;
    int mtid = rfid.getMtid();
    String rfidTag = rfid.getRfidTag();
 //   String location = rfid.getLocation();
    String scannstatus = rfid.getScannStatus();
    String scannType = rfid.getScannType();
    String groupid = rfid.getGroupid();
    String createDate = rfid.getCreateDate();
    String createDateTime = rfid.getCreateDateTime();
    int userId = rfid.getUserId();
   String mtidrec = getId("ST_INVENTORY_RFID_UNUSED");
boolean done1 = createRFIDRecord(mtidrec, rfidTag, location, scannstatus, scannType, groupid, createDate, createDateTime, userId);
if(done1 = true){
//	updateRfidUnusedRecords(rfidTag);             				
	successtransaction = true;}	 
    return successtransaction;
}

public boolean updateRfidUnusedRecords(String rfidTag) {
    Connection con = null;
    PreparedStatement ps = null; 
    String UPDATE_QUERY = "UPDATE ST_INVENTORY_RFID_UNUSED SET PROCESSED = 'Y' WHERE RFID_TAG = '"+rfidTag+"' AND PROCESSED = 'N' ";
  //  System.out.println("=====updateRfidUnusedRecords query=====> "+UPDATE_QUERY);
    boolean done;
    done = false;
    try {
    	con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);         
        ps.executeUpdate();         
        done = true;
    } catch (Exception ex) {
        System.out.println("WARNING: cannot update ST_INVENTORY_RFID_UNUSED IN updateRfidUnusedRecords+" + ex.getMessage());
    } finally {
        closeConnection(con, ps);
    }
    return done;
}

public boolean updateRfidRecords(String rfidTag,String scannstatus) {
    Connection con = null;
    PreparedStatement ps = null; 
//    String UPDATE_QUERY = "UPDATE ST_INVENTORY_RFID SET SCANN_STATUS = '"+scannstatus+"' WHERE RFID_TAG = '"+rfidTag+"' ";
    String UPDATE_QUERY = "UPDATE ST_INVENTORY_RFID SET PROCESSED = 'Y' WHERE RFID_TAG = '"+rfidTag+"' AND PROCESSED = 'N' ";
//    System.out.println("=====updateRfidRecords query=====> "+UPDATE_QUERY);
    boolean done;
    done = false;
    try {
    	con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);         
        ps.executeUpdate();         
        done = true;
    } catch (Exception ex) {
        System.out.println("WARNING: cannot update ST_INVENTORY_RFID_UNUSED IN updateRfidRecords+" + ex.getMessage());
    } finally {
        closeConnection(con, ps);
    }
    return done;
}

public boolean createRFIDRecord(String mtidrec, String rfidTag, String location, String scannstatus, String scannType, String groupid, String createDate, String createDateTime, int userId)
{
    Connection con;
    PreparedStatement ps;
    boolean done;
    String familyID;
    
    String rfidquery = "INSERT INTO ST_INVENTORY_RFID (RFID_TAG,LOCATION,SCANN_STATUS,SCANN_TYPE,CREATE_DATE," +
"CREATE_DATETIME,USER_ID)VALUES (?,?,?,?,?,?,?)"
;    
 //   System.out.println("rfidquery=== "+rfidquery);
 //   String isAutoGen = getCodeName("SELECT auto_generate_ID FROM IA_GB_COMPANY");
    con = null;
    ps = null;
    done = false;
    int i,j,k;
    try{  
//    String glid = getGeneratedId("ST_INVENTORY_RFID");
//      System.out.println("MTID=== "+pymtid);
//    familyID = glid;
 //   	System.out.println("Location=== "+location);
    con = getConnection();
    done = con.getAutoCommit(); 
    con.setAutoCommit(false);
    done = false;
    ps = con.prepareStatement(rfidquery);
//    ps.setString(1, glid);
    ps.setString(1, rfidTag);
    ps.setString(2, location);
    ps.setString(3, "N");
    ps.setString(4, "N");
    ps.setString(5, createDate);
    ps.setString(6, createDateTime);
    ps.setInt(7, userId);
    j = ps.executeUpdate();   
 
 if((j != -1))
 {
   con.commit();
   con.setAutoCommit(done);
   done = true;
  }
// System.out.println("done 2=== "+done);
    closeConnection(con, ps);
	}catch(Exception ex){
    System.out.println((new StringBuilder()).append("ERROR Creating RFID Transaction Posting in createRFIDRecord.. ").append(ex.getMessage()).toString());
    ex.printStackTrace();
    done = false;
    closeConnection(con, ps);
	} finally{
    closeConnection(con, ps);
	}
    return done;
}   

public  String getId(String tableName) {
	//System.out.println("getGeneratedId tableName: "+tableName);
	//	dateFormat = new DatetimeFormat();
	    sdf = new SimpleDateFormat("dd-MM-yyyy");

	    int counter = 0;
		final String FINDER_QUERY = "SELECT MT_ID + 1 FROM IA_MTID_TABLE "+
								    "WHERE MT_TABLENAME = '"+tableName+"'";
		final String UPDATE_QUERY = "UPDATE IA_MTID_TABLE SET MT_ID = MT_ID + 1 "+
									"WHERE MT_TABLENAME = '"+tableName+"'";
		String id = "";
//		System.out.println("getGeneratedId FINDER_QUERY: "+FINDER_QUERY);
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try{

			con = getConnection();
			ps = con.prepareStatement(FINDER_QUERY);
			rs = ps.executeQuery();

			while(rs.next()){
				counter = rs.getInt(1);
			}

			ps = con.prepareStatement(UPDATE_QUERY);
			ps.execute();


		}catch(Exception ex){
			System.out.println("WARN:Error generating id for table:"+
			tableName+"\n"+ex);
		}finally{
			closeConnection(con,ps,rs);
		}

		 id = Integer.toString(counter);
		return id;

	   }
public  String getGeneratedId(String tableName) {

	//dateFormat = new DatetimeFormat();
    //sdf = new SimpleDateFormat("dd-MM-yyyy");

    int counter = 0;
	final String FINDER_QUERY = "SELECT MT_ID + 1 FROM IA_MTID_TABLE "+
							    "WHERE MT_TABLENAME = '"+tableName+"'";
	final String UPDATE_QUERY = "UPDATE IA_MTID_TABLE SET MT_ID = MT_ID + 1 "+
								"WHERE MT_TABLENAME = '"+tableName+"'";
	String id = "";

	Connection con = null;
	ResultSet rs = null;
	PreparedStatement ps = null;

	try{

		con = getConnection();
		ps = con.prepareStatement(FINDER_QUERY);
		rs = ps.executeQuery();

		while(rs.next()){
			counter = rs.getInt(1);
		}

		ps = con.prepareStatement(UPDATE_QUERY);
		ps.execute();


	}catch(Exception ex){
		System.out.println("WARN:Error generating id for table:"+
		tableName+"\n"+ex);
	}finally{
		closeConnection(con,ps,rs);
	}

	 id = Integer.toString(counter);
	return id;

   }

public boolean deleteRfidUnusedRecords(String rfidTag) {
    Connection con = null;
    PreparedStatement ps = null; 
    String DELETE_QUERY = "DELETE FROM  ST_INVENTORY_RFID_UNUSED WHERE RFID_TAG = '"+rfidTag+"' ";
    boolean done;
    done = false;
    try {
    	con = getConnection();
        ps = con.prepareStatement(DELETE_QUERY);         
        ps.executeUpdate();         
        done = true;
    } catch (Exception ex) {
        System.out.println("WARNING: cannot delete from ST_INVENTORY_RFID_UNUSED in deleteRfidUnusedRecords+" + ex.getMessage());
    } finally {
        closeConnection(con, ps);
    }
    return done;
}


public boolean RFIDTagVerifyList(String rfidtag) {
String query = "INSERT INTO ST_INVENTORY_VERIFY_RFID SELECT USER_ID,RFID_TAG,LOCATION,SCANN_TYPE,SCANN_STATUS,CREATE_DATE,CREATE_DATETIME,PROCESSED FROM ST_INVENTORY_NEW_RFID WHERE RFID_TAG = ? ";
//System.out.println("<<<<<<<<===query in RFIDTagVerifyList: "+query);
	Connection con = null;
PreparedStatement ps = null;
boolean done = false;
  
try {
con = getConnection();
ps = con.prepareStatement(query);
ps.setString(1,rfidtag);

done = (ps.executeUpdate() != -1);
} catch (Exception ex) {
done = false;
System.out.println("ERROR Creating RFID Tag Records for verification " + ex.getMessage());
ex.printStackTrace();
} finally {
	closeConnection(con, ps); 
}
return done;
}

public boolean deleteRfidVerifyRecords(String rfidTag) {
    Connection con = null;
    PreparedStatement ps = null; 
    String DELETE_QUERY = "DELETE FROM  ST_INVENTORY_NEW_RFID WHERE RFID_TAG = '"+rfidTag+"' ";
    boolean done;
    done = false;
    try {
    	con = getConnection();
        ps = con.prepareStatement(DELETE_QUERY);         
        ps.executeUpdate();         
        done = true;
    } catch (Exception ex) {
        System.out.println("WARNING: cannot delete from ST_INVENTORY_NEW_RFID in deleteRfidVerifyRecords+" + ex.getMessage());
    } finally {
        closeConnection(con, ps);
    }
    return done;
}

// HTTP POST request


public void sendMail(String email, String subject,String msgText1)
{
	System.out.println("Just called the sendEmail API");
	try
	{
		Properties prop = new Properties();
		File file = new File("C:\\Property\\Inventory.properties");
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
		String recepient[]=email.split(",");
		String to = recepient[0];
		System.out.println("Mail Address sending From: "+from);
		System.out.println("Mail Address sending to: "+to);
		 System.out.println("-->>Mail Message--> "+msgText1);
		InternetAddress[] address = { new InternetAddress(to) };
		System.out.println("point 4");
		msg.setRecipients(Message.RecipientType.TO,address);
 
		 msg.setSubject(subject);

		System.out.println("point 6");
		msg.setSentDate(new Date());
		System.out.println("point 7");
		String msgBody = msgText1;
	    System.out.print("The mail body: "+msgBody);
	    msg.setText(msgBody);
	    msg.saveChanges();
		
		System.out.println("point 8");
		
	   
	    		
		System.out.println("point 9");
		//String cc[]={recepient[1],recepient[2],recepient[3]};
		for(int i=0;i<recepient.length;i++)
		{
		InternetAddress addressCopy = new InternetAddress(recepient[i]);	
		msg.setRecipient(Message.RecipientType.CC, addressCopy);
		}	
		System.out.println("point 10");
	  	 
Transport tr = session.getTransport("smtp");
System.out.println("point 11");
tr.connect();
System.out.println("point 12");
//	Security.getProviders("smtp");

System.out.println("point test");
//tr.sendMessage(msg, msg.getAllRecipients());
tr.send(msg);
System.out.println("point 13");
tr.close(); 


System.out.println("point 14");
	} 
	catch (Exception ex) 
	{
		System.out.println("point 15");
		ex.printStackTrace();
	}

}		

public void sendAlarm()
{
	System.out.println("Just called the sendEmail API");
	try
	{
		Properties prop = new Properties();
		File file = new File("C:\\Property\\Inventory.properties");
		FileInputStream in = new FileInputStream(file);
		prop.load(in);
		System.out.print("Able to load Alarm properties into prop");
		String url = prop.getProperty("host.url");
		String USER_AGENT = "Accept";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);
		int responseCode = con.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);

	} 
	catch (Exception ex) 
	{
		System.out.println("point of Alarm");
		ex.printStackTrace();
	}

}		


public String getValue(String query) {
	String result = "";
	Connection con = null;
	ResultSet rs = null;
	PreparedStatement ps = null;
//System.out.println("query===>> "+query);
	
	try {
		con = getConnection();
//		con = (new DataConnect("eschool")).getConnection();
		ps = con.prepareStatement(query);
		rs = ps.executeQuery();
		while (rs.next()) {
			result = rs.getString(1) == null ? "" : rs.getString(1);
		}
        try
        {
            if(con != null)
            {
            	closeConnection(con, ps);
            }
            closeConnection(con, ps);
        }
        catch(Exception errorClosing)
        {
            System.out.println((new StringBuilder()).append("WARNING::Error Closing Connection >> ").append(errorClosing).toString());
        }			
	} catch (Exception er) {
		System.out.println("Error in " + this.getClass().getName()
				+ "- getValue()... ->" + er.getMessage());
		er.printStackTrace();
	} 
	return result;
}

 
public boolean RFIDNewTagList(String rfidtag) {
	String createDate = String.valueOf(df.dateConvert(new java.util.Date()));
	Timestamp CreateTime = dbConnection.getDateTime(new java.util.Date());
String query = "INSERT INTO ST_INVENTORY_NEW_RFID SELECT USER_ID,RFID_TAG,LOCATION,SCANN_TYPE,SCANN_STATUS,'"+createDate+"','"+CreateTime+"',PROCESSED FROM ST_INVENTORY_RFID_UNUSED WHERE RFID_TAG = '"+rfidtag+"' ";
//System.out.println("<<<<<<<<===query in RFIDNewTagList: "+query);
Connection con = null;
//PreparedStatement ps = null;
boolean done = false;
int i;
try {
con = getConnection();
done = con.getAutoCommit();
con.setAutoCommit(false);
done = false;
ps = con.prepareStatement(query);
i = ps.executeUpdate();   

if((i != -1))
{
con.commit();
con.setAutoCommit(done);
done = true;
}
//System.out.println("done in RFIDNewTagList=== "+done);
//done = (ps.executeUpdate() != -1);
} catch (Exception ex) {
done = false;
System.out.println("ERROR Creating Unused RFID Tag Records" + ex.getMessage());
ex.printStackTrace();
} finally {
	closeConnection(con, ps); 
}
return done;
}


public boolean deleteHandReaderRfidRecords(String rfidTag) {
    Connection con = null;
    PreparedStatement ps = null; 
    String DELETE_QUERY = "DELETE FROM  ST_INVENTORY_RFID_UNUSED WHERE RFID_TAG = '"+rfidTag+"' ";
    boolean done;
    done = false;
    try {
    	con = getConnection();
        ps = con.prepareStatement(DELETE_QUERY);         
        ps.executeUpdate();         
        done = true;
    } catch (Exception ex) {
        System.out.println("WARNING: cannot delete from ST_INVENTORY_RFID_UNUSED in deleteHandReaderRfidRecords+" + ex.getMessage());
    } finally {
        closeConnection(con, ps);
    }
    return done;
}


public boolean RFIDUsedTagList(String rfidtag) {
	String createDate = String.valueOf(df.dateConvert(new java.util.Date()));
	Timestamp CreateTime = dbConnection.getDateTime(new java.util.Date());
String query = "INSERT INTO ST_INVENTORY_RFID_USED SELECT USER_ID,RFID_TAG,LOCATION,SCANN_TYPE,SCANN_STATUS='U','"+createDate+"','"+CreateTime+"',PROCESSED FROM ST_INVENTORY_RFID_UNUSED WHERE RFID_TAG = '"+rfidtag+"' ";
System.out.println("<<<<<<<<===query in RFIDUsedTagList: "+query);
	Connection con = null;
PreparedStatement ps = null;
boolean done = false;
  
try {
con = getConnection();
ps = con.prepareStatement(query);
//ps.setString(1,rfidtag);

done = (ps.executeUpdate() != -1);
} catch (Exception ex) {
done = false;
System.out.println("ERROR Creating RFID Tag Records Used from Hand Held Reader " + ex.getMessage());
ex.printStackTrace();
} finally {
	closeConnection(con, ps); 
}
return done;
}
  
public boolean updateHandReaderRfidRecords(String rfidTag) {
    Connection con = null;
    PreparedStatement ps = null; 
   // String DELETE_QUERY = "UPDATE ST_INVENTORY_RFID_UNUSED SET PROCESSED = 'P' WHERE RFID_TAG = '"+rfidTag+"' ";
    String DELETE_QUERY = "DELETE FROM ST_INVENTORY_RFID_UNUSED WHERE RFID_TAG = '"+rfidTag+"' ";
    System.out.println("<<<<<<<<===query in updateHandReaderRfidRecords: "+DELETE_QUERY);
    boolean done;
    done = false;
    try {
    	con = getConnection();
        ps = con.prepareStatement(DELETE_QUERY);         
        ps.executeUpdate();         
        done = true;
    } catch (Exception ex) {
        System.out.println("WARNING: cannot update table ST_INVENTORY_RFID_UNUSED in updateHandReaderRfidRecords+" + ex.getMessage());
    } finally {
        closeConnection(con, ps);
    }
    return done;
}

public boolean updateRfidVerifyRecords(String rfidTag) {
    Connection con = null;
    PreparedStatement ps = null; 
    String UPDATE_QUERY = "UPDATE ST_INVENTORY_VERIFY_RFID SET PROCESSED = 'P' WHERE RFID_TAG = '"+rfidTag+"' AND PROCESSED = 'N' ";
//    System.out.println("=====updateRfidVerifyRecords query=====> "+UPDATE_QUERY);
    boolean done;
    done = false;
    try {
    	con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);         
        ps.executeUpdate();         
        done = true;
    } catch (Exception ex) {
        System.out.println("WARNING: cannot update ST_INVENTORY_VERIFY_RFID IN updateRfidVerifyRecords+" + ex.getMessage());
    } finally {
        closeConnection(con, ps);
    }
    return done;
}

public boolean RFIDMaterialUtilizedTagList(String rfidtag) {
String createDate = String.valueOf(df.dateConvert(new java.util.Date()));
Timestamp CreateTime = dbConnection.getDateTime(new java.util.Date());
String query = "INSERT INTO ST_INVENTORY_MATERIAL_USED SELECT USER_ID,RFID_TAG,LOCATION,SCANN_TYPE,SCANN_STATUS='U','"+createDate+"','"+CreateTime+"',PROCESSED FROM ST_INVENTORY_RFID_UNUSED WHERE RFID_TAG = '"+rfidtag+"' ";
System.out.println("<<<<<<<<===query in RFIDMaterialUtilizedTagList: "+query);
	Connection con = null;
PreparedStatement ps = null;
boolean done = false;
  
try {
con = getConnection();
ps = con.prepareStatement(query);
//ps.setString(1,rfidtag);

done = (ps.executeUpdate() != -1);
} catch (Exception ex) {
done = false;
System.out.println("ERROR Creating RFID Tag Records Used from Hand Held Reader " + ex.getMessage());
ex.printStackTrace();
} finally {
	closeConnection(con, ps); 
}
return done;
}

public boolean RFIDTagForDistributionList(String rfidtag) {
	String createDate = String.valueOf(df.dateConvert(new java.util.Date()));
	Timestamp CreateTime = dbConnection.getDateTime(new java.util.Date());
String query = "INSERT INTO ST_STOCK_DISTRBUTE SELECT USER_ID,RFID_TAG,LOCATION,SCANN_TYPE,SCANN_STATUS='U','"+createDate+"','"+CreateTime+"',PROCESSED FROM ST_INVENTORY_RFID_UNUSED WHERE RFID_TAG = '"+rfidtag+"' ";
System.out.println("<<<<<<<<===query in RFIDTagForDistributionList: "+query);
	Connection con = null;
PreparedStatement ps = null;
boolean done = false;
  
try {
con = getConnection();
ps = con.prepareStatement(query);
//ps.setString(1,rfidtag);

done = (ps.executeUpdate() != -1);
} catch (Exception ex) {
done = false;
System.out.println("ERROR Creating RFID Tag Records Used from Hand Held Reader " + ex.getMessage());
ex.printStackTrace();
} finally {
	closeConnection(con, ps); 
}
return done;
}

public boolean deletedistributedRfidRecord(String rfidTag) {
    Connection con = null;
    PreparedStatement ps = null; 
   // String DELETE_QUERY = "UPDATE ST_INVENTORY_RFID_UNUSED SET PROCESSED = 'P' WHERE RFID_TAG = '"+rfidTag+"' ";
    String DELETE_QUERY = "DELETE FROM ST_STOCK_DISTRBUTE WHERE RFID_TAG = '"+rfidTag+"' ";
    boolean done;
    done = false;
    try {
    	con = getConnection();
        ps = con.prepareStatement(DELETE_QUERY);         
        ps.executeUpdate();         
        done = true;
    } catch (Exception ex) {
        System.out.println("WARNING: cannot update table ST_INVENTORY_RFID_UNUSED in updateHandReaderRfidRecords+" + ex.getMessage());
    } finally {
        closeConnection(con, ps);
    }
    return done;
}

public java.util.ArrayList getUsernotSignOutRecords(String sessionTimeOut)
 {
//	System.out.println("<<<<<df.getDateTime().substring(10)>>>>: "+df.getDateTime().substring(10));
 	java.util.ArrayList list = new java.util.ArrayList();
 	String notSignOutquery = "select  user_id from gb_user_login  where time_out is null and datediff(minute, time_in, '"+df.getDateTime().substring(10)+"') / 60.0 > "+sessionTimeOut+"/60";
	Connection c = null;
	ResultSet rs = null;
	Statement s = null;
//	System.out.println("<<<<<<<<notSignOutquery in getUsernotSignOutRecords: "+notSignOutquery);
	try {
		    c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(notSignOutquery);
			while (rs.next())
			   {
				String notSignoutuserId = rs.getString("user_id");
				list.add(notSignoutuserId);
			   }
	 }
				 catch (Exception e)
					{
						e.printStackTrace();
					}
					finally
					{
						closeConnection(c, s, rs);
					}
 	return list;
 }

public boolean updateUsernotSignOutRecords( String userId)
{
	Connection con = null;
	PreparedStatement ps = null;
	boolean done = false;
	//String date = String.valueOf(dateConvert(new java.util.Date()));
 	//date = date.substring(0, 10);
    Timestamp signoutdate =  dbConnection.getDateTime(new java.util.Date());
    //String timeout = signoutdate.substring(0, 10);
//    System.out.println("<<<<<<<<<signoutdate: "+signoutdate);
	String query = "UPDATE am_gb_User SET login_status=0   where user_id ="+userId+" ";
	String query2 = "UPDATE gb_user_login SET time_out='"+df.getDateTime().substring(10)+"'   where user_id ="+userId+" and time_out is null";
//	 System.out.println("<<<<<<<<<query: "+query);
//	 System.out.println("<<<<<<<<<query2: "+query2);
	try {   
		con = getConnection();
		ps = con.prepareStatement(query);
		done = (ps.executeUpdate() != -1);
		ps = con.prepareStatement(query2);
		done = (ps.executeUpdate() != -1);
	} catch (Exception e) { 
		e.printStackTrace();
		closeConnection(con, ps);
	}
	closeConnection(con, ps);
	return done;
}

}
