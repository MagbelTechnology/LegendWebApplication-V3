package com.magbel.ia.ledger; 

import com.magbel.ia.bus.AccountChartServiceBus;
import com.magbel.ia.bus.ApprovalRecords;
import com.magbel.ia.dao.PersistenceServiceDAO;
import com.magbel.ia.vao.Customer;
import com.magbel.ia.vao.ExcelBean;
import com.magbel.ia.vao.PaymentCategory;
import com.magbel.ia.vao.SmsTransaction;
//import com.magbel.util.ApplicationHelper;
import com.magbel.util.DatetimeFormat;
import com.magbel.util.HtmlUtilily;
import com.mindcom.security.Licencer;

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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import com.magbel.ia.vao.Result;
import com.magbel.ia.vao.Transaction;
import com.magbel.util.DataConnect;
import com.mindcom.security.Licencer;
import com.sun.net.ssl.HttpsURLConnection;

import java.sql.SQLException;

/**
 * @author Matanmi
 */
public class CompanyHandler extends PersistenceServiceDAO	 {
//	public class CustomerHandler extends PersistenceServiceDAO	
	Connection con = null;
 //   private ApplicationHelper helper;
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
	}



public java.util.ArrayList getSqlRecords()
 {
 	java.util.ArrayList _list = new java.util.ArrayList();
 	String date = String.valueOf(df.dateConvert(new java.util.Date()));
 	System.out.println("====IAS Server Date-----> "+date);
 	date = date.substring(0, 10);
 	String finacleTransId= null;
		String query = " SELECT  * from IA_INTERFACE_TABLE where STATUS = 'N' ";
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
				String transactionid = rs.getString("TRANSACTION_ID");
				String mtid = rs.getString("MTID");
				String draccount = rs.getString("DRACCOUNT");
				String drnarration = rs.getString("DRNARRATION");
				String drschoolcode = rs.getString("DRSCHOOL_CODE");
				String craccount = rs.getString("CRACCOUNT");
				String crnarration = rs.getString("CRNARRATION");
				String crschoolcode = rs.getString("CRSCHOOL_CODE");
				double transactionamt =  rs.getDouble("TRANSACTION_AMT");
				String transactionDate = rs.getString("TRANSACTION_DATE");
				String companyCode = rs.getString("COMP_CODE");
				Transaction Transaction = new Transaction();
				Transaction.setMtId(mtid);
				Transaction.setTransactionId(transactionid);
				Transaction.setCraccount(craccount);
				Transaction.setDraccount(draccount);
				Transaction.setDrnarration(drnarration);
				Transaction.setDrschoolcode(drschoolcode);
				Transaction.setCrschoolcode(crschoolcode);
				Transaction.setTranamount(transactionamt);
				System.out.println("getSqlRecords transactionamt: "+transactionamt);
				Transaction.setCrnarration(crnarration);
				Transaction.setTransactionDate(transactionDate);
				Transaction.setCompanyCode(companyCode);
				_list.add(Transaction);
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
public java.util.ArrayList getSMSSqlRecords()
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(df.dateConvert(new java.util.Date()));
	//System.out.println("====eschool Server Date-----> "+date);
	date = date.substring(0, 10);
	String finacleTransId= null;
		String query = " SELECT a.SMS_code,a.ADMIN_NO,a.Company_Code,a.BRANCH_CODE,a.Tran_code,a.Phone_Number,a.status," +
				"a.Tran_Description,b.SMS_description,b.SCHOOL_NAME from IA_SMS_RECORDS a, IA_SMS_SETUP b " +
				"where a.STATUS = 'ACTIVE' " +
				"AND a.Branch_Code = b.Branch_Code " +
				"AND a.Company_Code = b.Company_Code " +
				"AND a.Tran_Code = b.Tran_Code ";
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
				String transcode = rs.getString("Tran_code");
				String mtid = rs.getString("SMS_code");
				String adminNo = rs.getString("ADMIN_NO");
				String phoneNo = rs.getString("Phone_Number");
				String Smsdescription = rs.getString("SMS_description");
				String status = rs.getString("status");
				String TranDescription = rs.getString("Tran_Description");
				String schoolcode = rs.getString("BRANCH_CODE");
				String companyCode = rs.getString("Company_Code");
				String schoolname = rs.getString("SCHOOL_NAME");
				SmsTransaction SmsTransaction = new SmsTransaction();
				SmsTransaction.setMtId(mtid);
				SmsTransaction.setTransactionId(transcode);
				SmsTransaction.setAdmin_no(adminNo);
				SmsTransaction.setPhoneNo(phoneNo);
				SmsTransaction.setSmsdescription(Smsdescription);
				SmsTransaction.setStatus(status);
				SmsTransaction.setTranDescription(TranDescription);
				SmsTransaction.setSchoolcode(schoolcode);
				SmsTransaction.setCompanyCode(companyCode);
				SmsTransaction.setSchoolname(schoolname);
				_list.add(SmsTransaction);
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

/*
public String getInterfaceRecords(String adminNo)
 {
 	String date = String.valueOf(df.dateConvert(new java.util.Date()));
 	date = date.substring(0, 10);
	System.out.println("System Date in getInterfaceRecords====> "+date+"====adminNo==>> "+adminNo);
 	String iso ="";  
		String query = " SELECT  tran_particular_2 from fix_tb " +
				"where tran_particular_2='"+adminNo+"' and to_char(tran_date,'DD-MM-YYYY') >= '"+getOracleDateFormat(date)+"'";
		System.out.println("Query on getInterfaceRecords====> "+query);
	Connection c = null;
	ResultSet rs = null;  
	Statement s = null; 

	try {
		    c = getConnectionFinacle();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next())
			   {
				 iso = "000";
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
 	return iso;
 }
	public String getOracleDateFormat(String date)
	{
		String dd=date.substring(8,10);
		String mm=date.substring(5, 7);
		String yyyy=date.substring(0, 4);
		date=dd+"-"+mm+"-"+yyyy;
		return date;
	}
*/	
public boolean updateSqlRecords( String iso,String finacleTransId)
	{
		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String date = String.valueOf(df.dateConvert(new java.util.Date()));
		System.out.println("=====updateSqlRecords iso=====> "+iso);		
		System.out.println("=====updateSqlRecords date=====> "+date);
		System.out.println("=====updateSqlRecords finacleTransId=====> "+finacleTransId);		
	 	date = date.substring(0, 10);
		String query = "UPDATE am_raisentry_transaction SET iso=?   where transaction_date >'"+date+"' and finacle_Trans_Id=? ";
		System.out.println("=====updateSqlRecords query=====> "+query);
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

public final boolean createInterfaceTransaction(Transaction transaction, double amount)
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
/*    String INSERT_QUERY = "INSERT INTO   IA_INTERFACE_TABLE (MTID,TRANSACTION_ID, DRACCOUNT,  DRNARRATION, DRSCHOOL_CODE," +
" CRACCOUNT,  CRNARRATION, CRSCHOOL_CODE, TRANSACTION_DATE, TRANSACTION_AMT,  STATUS, USERID)  " +
"VALUES       (?,?,?,?,?,?,?,?,?,?,?,?)" +
" "
;
//    System.out.println("createCustomer localgovt: "+customer.getLocalgovmt());
           
 */  
   // String mtId = helper.getGeneratedId("IA_INTERFACE_TABLE");  
    String transactionid = transaction.getTransactionId();
    String craccount = transaction.getCraccount();
    String crschoolcode = transaction.getCrschoolcode();
    String crnarration = transaction.getCrnarration();
    String draccount = transaction.getDraccount();
    String drschoolcode = transaction.getDrschoolcode();
    String drnarration = transaction.getDrnarration();
    String transactionDate = transaction.getTransactionDate();
    double transactionamt = transaction.getTranamount();
    System.out.println("createInterfaceTransaction transactionamt: "+transactionamt);
    String createDate = formatDate(new Date());
    String status = transaction.getStatus();
    String companyCode = transaction.getCompanyCode();
    if(transactionDate == "" || transactionDate == null)
    {
    	transactionDate = createDate;
    }
    int userId = transaction.getUserId();
    int drCurrCode = 0;
    String drAcctType = "IP";
    String crAcctType = "IP";
    String crTranCode= "117";
    String drTranCode= "157";
    String drSBU = "1";
    String crSBU = "1";
 //   String drReference = "1";
    double drSysExchRate = 1;
    double drAcctExchRate = 1;
    double crAcctExchRate = 1;
    double crSysExchRate = 1;
    int crCurrCode =  0;
    String Term = "001";
    String receiptNo = "01";
    
   String familyid = getId("IA_FAMILY_ID");
//    System.out.println("createInterfaceTransaction userId: "+userId+" drSysExchRate: "+drSysExchRate+" drAcctExchRate: "+drAcctExchRate+" crAcctExchRate: "+crAcctExchRate+" crSysExchRate: "+crSysExchRate+" transactionamt: "+transactionamt);
//    System.out.println("createInterfaceTransaction drschoolcode: "+drschoolcode+" drCurrCode: "+drCurrCode+" drAcctType: "+drAcctType+" draccount: "+draccount+" drTranCode: "+drTranCode+" drSBU: "+crSBU+" drnarration: "+drnarration+" transactionid: "+transactionid);
//    System.out.println("createInterfaceTransaction crCurrCode: "+crCurrCode+" crnarration: "+crnarration+" crTranCode: "+crTranCode+" craccount: "+craccount+" crAcctType: "+crAcctType+" drSBU: "+drSBU+" companyCode: "+companyCode+" transactionDate: "+transactionDate);   
boolean done1 = createGLHistory(drschoolcode, drCurrCode, drAcctType, draccount, drTranCode, drSBU, drnarration, transactionamt, transactionid, drAcctExchRate, drSysExchRate, crschoolcode, crCurrCode, crAcctType, draccount, crTranCode, crSBU, drnarration, transactionamt, drAcctExchRate, drSysExchRate, transactionDate, userId, companyCode,familyid,Term,craccount,receiptNo);
if(done1 = true){
   	updateDRBalances(draccount,drschoolcode,transactionamt);             				
	updateCRBalances(craccount,crschoolcode,transactionamt);	
	successtransaction = true;}	 
    return successtransaction;
}

public boolean updateInterfaceTable(String tranId, String errorMessage) {
    Connection con = null;
    PreparedStatement ps = null; 
    String UPDATE_QUERY = "UPDATE IA_INTERFACE_TABLE SET STATUS = '"+errorMessage+"' WHERE TRANSACTION_ID = '"+tranId+"' AND STATUS = 'N' ";
    boolean done;
    done = false;
    try {
    	con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);         
        ps.executeUpdate();         
        done = true;
    } catch (Exception ex) {
        System.out.println("WARNING: cannot update IA_INTERFACE_TABLE IN updateInterfaceTable+" + ex.getMessage());
    } finally {
        closeConnection(con, ps);
    }
    return done;
}

public boolean createGLHistory(String drBranch, int drCurrCode, String drAcctType, String drAcctNo, String drTranCode, String drSBU, String drNarration, 
        double drAmount, String drReference, double drAcctExchRate, double drSysExchRate, 
        String crBranch, int crCurrCode, String crAcctType, String crAcctNo, String crTranCode, String crSBU, String crNarration, 
        double crAmount, double crAcctExchRate, double crSysExchRate, String effDate, 
        int userId,String companyCode,String familyid,String Term,String OtherlegAcct,String receiptNo)
{
    String GLCRHistoryquery;
    Connection con;
    PreparedStatement ps;
    boolean done;
    String familyID;
    
    GLCRHistoryquery = "INSERT INTO IA_GL_HISTORY (BRANCH_NO,ACCT_TYPE,GL_ACCT_NO,TRAN_CODE,SBU_CODE,DES" +
"CRIPTION,AMT,REFERENCE,EFFECTIVE_DT,USERID,CREATE_DT,FAMILY_ID,currency_id,sell_" +
"act_exch_rate,sell_sys_exch_rate,MTID,sys_tran_code,orig_branch_no,COMP_CODE,OTHERACCTLEG,BRANCH_CODE,RECEIPT_NO,Processed)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
;    
    System.out.println("GLCRHistoryquery=== "+GLCRHistoryquery);
 //   String isAutoGen = getCodeName("SELECT auto_generate_ID FROM IA_GB_COMPANY");
    con = null;
    ps = null;
    done = false;
    int i,j,k;
    try{  
    String glid = getGeneratedId("IA_GL_HISTORY");
//      System.out.println("MTID=== "+pymtid);
    familyID = glid;
    con = getConnection();
    done = con.getAutoCommit();
    con.setAutoCommit(false);
    done = false;
    ps = con.prepareStatement(GLCRHistoryquery);
    System.out.println("crBranch=== "+crBranch);
    ps.setString(1, crBranch);
    System.out.println("crBranch=== "+crAcctType);
    ps.setString(2, crAcctType);
    System.out.println("crAcctNo=== "+crAcctNo);
    ps.setString(3, crAcctNo);
    System.out.println("crTranCode=== "+crTranCode);
    ps.setString(4, crTranCode);
    System.out.println("crSBU=== "+crSBU);
    ps.setString(5, crSBU);
    System.out.println("crNarration=== "+crNarration);
    ps.setString(6, crNarration);
    System.out.println("crAmount=== "+crAmount);
    ps.setDouble(7, crAmount);
    System.out.println("drReference=== "+drReference);
    ps.setString(8, drReference);    
    ps.setDate(9, dateConvert(new java.util.Date()));
    System.out.println("userId=== "+userId);
    ps.setInt(10, userId);
    ps.setDate(11, dateConvert(new java.util.Date()));
    System.out.println("familyID=== "+familyID);
    ps.setString(12, familyID);
    System.out.println("crCurrCode=== "+crCurrCode);
    ps.setInt(13, crCurrCode);
    System.out.println("crAcctExchRate=== "+crAcctExchRate);
    ps.setDouble(14, crAcctExchRate);
    System.out.println("crSysExchRate=== "+crSysExchRate);
    ps.setDouble(15, crSysExchRate);
    System.out.println("glid=== "+Integer.parseInt(glid));
    ps.setInt(16, Integer.parseInt(glid));
    System.out.println("crTranCode=== "+Integer.parseInt(crTranCode));
    ps.setInt(17, Integer.parseInt(crTranCode));
    System.out.println("crBranch=== "+crBranch);
    ps.setString(18, crBranch);
    System.out.println("companyCode=== "+companyCode);
    ps.setString(19, companyCode);
    System.out.println("OtherlegAcct=== "+OtherlegAcct);
    ps.setString(20, OtherlegAcct);
    System.out.println("crBranch=== "+crBranch);
    ps.setString(21, crBranch);
    System.out.println("receiptNo=== "+receiptNo);
    ps.setString(22, receiptNo);
    ps.setString(23, "N");
 //   ps.addBatch();
    //done = ps.executeBatch().length != -1;
  //  done = (ps.execute(GLHistoryquery));
    j = ps.executeUpdate();   
    
    ps = con.prepareStatement(GLCRHistoryquery);
    System.out.println("drBranch=== "+drBranch);
    ps.setString(1, drBranch);
    System.out.println("drAcctType=== "+drAcctType);
    ps.setString(2, drAcctType);
    System.out.println("drAcctNo=== "+drAcctNo);
    ps.setString(3, drAcctNo);
    System.out.println("drTranCode=== "+drTranCode);
    ps.setString(4, drTranCode);
    System.out.println("drSBU=== "+drSBU);
    ps.setString(5, drSBU);
    System.out.println("drNarration=== "+drNarration);
    ps.setString(6, drNarration);
    System.out.println("drAmount=== "+drAmount);
    ps.setDouble(7, drAmount);
    System.out.println("drReference=== "+drReference);
    ps.setString(8, drReference);    
    ps.setDate(9, dateConvert(new java.util.Date()));
    System.out.println("userId=== "+userId);
    ps.setInt(10, userId);
    ps.setDate(11, dateConvert(new java.util.Date()));
    System.out.println("familyID=== "+familyID);
    ps.setString(12, familyID);
    System.out.println("drCurrCode=== "+drCurrCode);
    ps.setInt(13, drCurrCode);
    System.out.println("drAcctExchRate=== "+drAcctExchRate);
    ps.setDouble(14, drAcctExchRate);
    System.out.println("crSysExchRate=== "+drSysExchRate);
    ps.setDouble(15, drSysExchRate);
    System.out.println("glid=== "+Integer.parseInt(glid));
    ps.setInt(16, Integer.parseInt(glid));
    System.out.println("drTranCode=== "+Integer.parseInt(drTranCode));
    ps.setInt(17, Integer.parseInt(drTranCode));
    System.out.println("drBranch=== "+drBranch);
    ps.setString(18, drBranch);
    System.out.println("companyCode=== "+companyCode);
    ps.setString(19, companyCode);
    System.out.println("crAcctNo=== "+crAcctNo);
    ps.setString(20, crAcctNo);
    System.out.println("crBranch=== "+drBranch);
    ps.setString(21, drBranch);
    System.out.println("receiptNo=== "+receiptNo);
    ps.setString(22, receiptNo);
    ps.setString(23, "N");
 //   ps.addBatch();
    //done = ps.executeBatch().length != -1;
  //  done = (ps.execute(GLHistoryquery));
    i = ps.executeUpdate();    
 if((j != -1) && (i != -1))
 {
   con.commit();
   con.setAutoCommit(done);
   done = true;
  }
 System.out.println("done 2=== "+done);
    closeConnection(con, ps);
	}catch(Exception ex){
    System.out.println((new StringBuilder()).append("ERROR Creating Transaction Posting in createGLHistory.. ").append(ex.getMessage()).toString());
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
		System.out.println("getGeneratedId FINDER_QUERY: "+FINDER_QUERY);
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

public void updateDRBalances(String AcctNo, String BranchNo,double tranAmount) {
    Connection con = null;
    PreparedStatement ps = null;
  //  String BranchNo = HtmlUtilily.getCodeName("Select branch_code from MG_AD_Branch where branch_id = "+BranchId+"");
    String UPDATE_QUERY = "UPDATE IA_GL_BALANCES SET current_bal = current_bal - "+tranAmount+",no_debits_ptd = no_debits_ptd + 1,amt_debits_ptd = amt_debits_ptd + "+tranAmount+" WHERE gl_acct_no = '"+AcctNo+"' AND Branch_Code = '"+BranchNo+"' ";
//       String UPDATEProcess_QUERY = "UPDATE IA_GL_HISTORY SET Processed = 'Y' WHERE gl_acct_no = '"+AcctNo+"' AND Branch_code = "+BranchNo+" AND Processed = 'N' ";
    System.out.println("updateDRBalancesUPDATE_QUERY: "+UPDATE_QUERY);
    try {
       // con = getConnection("eschool");
   	   con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);                    
        ps.executeUpdate();
//           ps = con.prepareStatement(UPDATEProcess_QUERY); 
//           ps.executeUpdate();
        
    } catch (Exception ex) {
        System.out.println("WARNING: cannot update IA_GL_BALANCES+" + ex.getMessage());
    } finally {
        closeConnection(con, ps);
    }

}

public boolean updateCRBalances(String AcctNo, String BranchNo,double tranAmount) {
  //  String BranchNo = HtmlUtilily.getCodeName("Select branch_code from MG_AD_Branch where branch_id = "+BranchId+"");
    Connection con = null;
    PreparedStatement ps = null; 
    String UPDATE_QUERY = "UPDATE IA_GL_BALANCES SET current_bal = current_bal + "+tranAmount+",no_credits_ptd = no_credits_ptd + 1,amt_credits_ptd = amt_credits_ptd + "+tranAmount+" WHERE gl_acct_no = '"+AcctNo.trim()+"' AND Branch_Code = '"+BranchNo+"' ";
//       String UPDATEProcess_QUERY = "UPDATE IA_GL_HISTORY SET Processed = 'Y' WHERE gl_acct_no = '"+AcctNo+"' AND Branch_Code = "+BranchNo+" ";
    System.out.println("updateCRBalances UPDATE_QUERY: "+UPDATE_QUERY);
//    System.out.println("tranAmount: "+tranAmount+" BranchNo: "+BranchNo+" AcctNo: "+AcctNo);
    boolean done;
    done = false;
    try {
       // con = getConnection("eschool");
    	con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);         
        ps.executeUpdate();
//           ps = con.prepareStatement(UPDATEProcess_QUERY);
//          ps.executeUpdate();            
        done = true;
    } catch (Exception ex) {
        System.out.println("WARNING: cannot update IA_GL_BALANCES IN updateCRBalances+" + ex.getMessage());
    } finally {
        closeConnection(con, ps);
    }
    return done;
}  
   
// HTTP POST request
@SuppressWarnings("deprecation")
public void sendPost(String smsdescription, String phoneNo,String schoolName) throws Exception {
	String USER_AGENT = "Mozilla/5.0";

	String url = "http://api.infobip.com/api/v3/sendsms/plain";
	URL obj = new URL(url);
	HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	//add reuqest header
	con.setRequestMethod("POST");
	con.setRequestProperty("User-Agent", USER_AGENT);
	con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

	String urlParameters = "http://api.infobip.com/api/v3/sendsms/plain?user=magbel&password=technology&sender="+schoolName+"&SMSText="+smsdescription+"&GSM="+phoneNo+"";

	// Send post request
	con.setDoOutput(true);
	DataOutputStream wr = new DataOutputStream(con.getOutputStream());
	wr.writeBytes(urlParameters);
	wr.flush();
	wr.close();

	int responseCode = con.getResponseCode();
	System.out.println("\nSending 'POST' request to URL : " + url);
	System.out.println("Post parameters : " + urlParameters);
	System.out.println("Response Code : " + responseCode);

	BufferedReader in = new BufferedReader(
	        new InputStreamReader(con.getInputStream()));
	String inputLine;
	StringBuffer response = new StringBuffer();

	while ((inputLine = in.readLine()) != null) {
		response.append(inputLine);
	}
	in.close();

	//print result
	System.out.println(response.toString());

}

public boolean login(String location,String compName, String Apps,String LicfileName)
{
	boolean result = false;
	System.out.println("company Name in Login: "+compName+"    location: "+location+"    Apps: "+Apps+"   LicfileName: "+LicfileName);
//	String compname = htmlUtil.findObject("SELECT company_name FROM MG_gb_company WHERE company_code = '"+compcode+"'");
	try{
	File file = new File(location);
	Properties props = new Properties();
	InputStream in = new FileInputStream(file);
	Licencer lic = new Licencer();
	props.load(in);  
	            String strCompany = props.getProperty("comp");
	            String app = props.getProperty("app");
	            String license = props.getProperty("lic-code");
	            String authcode = props.getProperty("author.code");
				license = license != null ? license.replaceAll("-", "") : "";
	            if(lic.isKeyOkay(license, strCompany, app, Long.parseLong(authcode),LicfileName) && (strCompany.equalsIgnoreCase(compName)) && (Apps.equalsIgnoreCase(app)))
	            {
	            	result=true;
	            }
	}catch(Exception e)
	{
		e.printStackTrace();
	}
	return result;
}

public java.util.ArrayList getStudentRecords(String filter)
 {
 	java.util.ArrayList _list = new java.util.ArrayList();
 	String date = String.valueOf(df.dateConvert(new java.util.Date()));
 	date = date.substring(0, 10);
 //	String finacleTransId= null;
	  String SELECT_QUERY = " SELECT * FROM   IA_STUDENT_SCORED "+filter+" ORDER BY MTID ";
//	Transaction transaction = null;

	Connection c = null;
	ResultSet rs = null;
	Statement s = null;

	try {
		    c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(SELECT_QUERY);
			while (rs.next())
			   {				
				String batch = rs.getString("BATCH");
				String mtid = rs.getString("MTID");
				String program = rs.getString("PROGRAM");
				String school = rs.getString("SCHOOL");
				String adminNo = rs.getString("ADMIN_NO");
				String session = rs.getString("SESSION");
				String semester = rs.getString("SEMESTER");
				String courses = rs.getString("COURSES");
				double examscore =  rs.getDouble("EXAM_SCORE");
				double cascore =  rs.getDouble("CA_SCORE");
				String createdate = rs.getString("CREATE_DATE");
				String companyCode = rs.getString("COMP_CODE");
				int userid = rs.getInt("USERID");
				Result Result = new Result();
				Result.setMtId(mtid);
				Result.setBatch(batch);
				Result.setProgram(program);
				Result.setSchool(school);
				Result.setAdmin_no(adminNo);
				Result.setSession(session);
				Result.setSemester(semester);
				Result.setExamscore(examscore);
				Result.setCascore(cascore);		
				Result.setCourses(courses);
				Result.setCreatedate(createdate);
				Result.setCompanyCode(companyCode);
				_list.add(Result);
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
public java.util.ArrayList getStudentResults()
{
	java.util.ArrayList list2 = new java.util.ArrayList();
	String date = String.valueOf(df.dateConvert(new java.util.Date()));
	System.out.println("====eschool Server Date for Student Records-----> "+date);
	date = date.substring(0, 10);
	String finacleTransId= null;
//	String query = " SELECT  * from IA_STUDENT_SCORED where STATUS <> 'P' ";
	String query = " SELECT  * from IA_STUDENT_SCORED where STATUS IS NULL ";
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
				String batch = rs.getString("BATCH");
				String mtid = rs.getString("MTID");
				String program = rs.getString("PROGRAM");
				String school = rs.getString("SCHOOL");
				String adminNo = rs.getString("ADMIN_NO");
				String session = rs.getString("SESSION");
				String semester = rs.getString("SEMESTER");
				String courses = rs.getString("COURSES");
				double examscore =  rs.getDouble("EXAM_SCORE");
				double cascore =  rs.getDouble("CA_SCORE");
				String createdate = rs.getString("CREATE_DATE");
				String companyCode = rs.getString("COMP_CODE");
				String subjectcode = rs.getString("Subject_code");
				System.out.println("adminNo in getStudentResults: "+adminNo);
				int userid = rs.getInt("USERID");
				Result Result = new Result();
				Result.setMtId(mtid);
				Result.setBatch(batch);
				Result.setProgram(program);
				Result.setSchool(school);
				Result.setAdmin_no(adminNo);
				Result.setSession(session);
				Result.setSemester(semester);
				Result.setExamscore(examscore);
				Result.setCascore(cascore);		
				Result.setCourses(courses);
				Result.setCreatedate(createdate);
				Result.setCompanyCode(companyCode);
				Result.setSubjectcode(subjectcode);
				list2.add(Result);
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
	return list2;
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
            	con.close();
            }
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


public boolean InsertStudentrecord(String adminno, String companyCode, String school, String program, String courses, String semester, String session, 
        double examscore, double cascore, double totalscore,String term,String gradecode,String gradename,String subjectcode)
{
    String PerformanceQuery;
    Connection con;
    PreparedStatement ps;
    boolean done;
    
    PerformanceQuery = "INSERT INTO IA_PERFORMANCE (MTID,COMP_CODE,SCHOOL,ADMIN_NO,SUBJECT_CODE,PROGRAM,COURSES,SEMESTER," +
"SESSION,EXAM_SCORE,CA_SCORE,FIRST_TERM_SCORE,SECOND_TERM_SCORE,THIRD_TERM_SCORE,GRADE_CODE,GRADE_NAME,TERM,CREATE_DATE,TOTAL_SCORE" +
")VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
;    
 //   System.out.println("PerformanceQuery=== "+PerformanceQuery);
 //   String isAutoGen = getCodeName("SELECT auto_generate_ID FROM IA_GB_COMPANY");
    con = null;
    ps = null;
    done = false;
    double firstterm = 0.00;
    double secondterm = 0.00;
    double thirdterm = 0.00;
    int i,j,k;
    try{  
    	if(term.equalsIgnoreCase("FIRST")){firstterm = totalscore;}
    	if(term.equalsIgnoreCase("SECOND")){secondterm = totalscore;}
    	if(term.equalsIgnoreCase("THIRD")){thirdterm = totalscore;}
    String id = getGeneratedId("IA_PERFORMANCE");
 //     System.out.println("totalscore=== "+totalscore+"  term: "+term);
      
    con = getConnection();
    done = con.getAutoCommit();
    con.setAutoCommit(false);
    done = false;
    ps = con.prepareStatement(PerformanceQuery);
    ps.setString(1, id);
    ps.setString(2, companyCode);
    ps.setString(3, school);
    ps.setString(4, adminno);
    ps.setString(5, subjectcode);
    ps.setString(6, program);
    ps.setString(7, courses);
    ps.setString(8, semester);
    ps.setString(9, session);
    ps.setDouble(10, examscore);
    ps.setDouble(11, cascore);
    ps.setDouble(12, firstterm);
    ps.setDouble(13, secondterm);
    ps.setDouble(14, thirdterm);
    ps.setString(15, gradecode);  
    ps.setString(16, gradename);
    ps.setString(17, term);
    ps.setDate(18, dateConvert(new java.util.Date()));
    ps.setDouble(19, totalscore);
    i = ps.executeUpdate();    
 if(i != -1)
 {
   con.commit();
   con.setAutoCommit(done);
   done = true;
  }
    closeConnection(con, ps);
	}catch(Exception ex){
    System.out.println((new StringBuilder()).append("ERROR Creating Student Records in InsertStudentrecord.. ").append(ex.getMessage()).toString());
    ex.printStackTrace();
    done = false;
    closeConnection(con, ps);
	} finally{
    closeConnection(con, ps);
	}
    return done;
}  
public boolean StudentScoreupdate(String mtid) {
	    Connection con = null;
	    PreparedStatement ps = null; 
	    String UPDATE_QUERY = "UPDATE IA_STUDENT_SCORED SET STATUS = 'P'  WHERE MTID = "+mtid+"";
	//    System.out.println("UPDATE_QUERY IN StudentScoreupdate:  "+UPDATE_QUERY);
	    boolean done;
	    done = false;  
	    try {
	    	con = getConnection();
	        ps = con.prepareStatement(UPDATE_QUERY);         
	        ps.executeUpdate();           
	        done = true;
	    } catch (Exception ex) {
	        System.out.println("WARNING: cannot update IA_STUDENT_SCORED IN StudentScoreupdate" + ex.getMessage());
	    } finally {
	        closeConnection(con, ps);
	    }
	    return done;
	}

public java.util.ArrayList getStudentPosition(String filter)
 {
 	java.util.ArrayList _list = new java.util.ArrayList();
 	String date = String.valueOf(df.dateConvert(new java.util.Date()));
 	date = date.substring(0, 10);
 //	String finacleTransId= null;
	  String SELECT_QUERY =  filter;
//	Transaction transaction = null;
  
	Connection c = null;
	ResultSet rs = null;
	Statement s = null;

	try {
		    c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(SELECT_QUERY);
			while (rs.next())
			   {				
				//String batch = rs.getString("BATCH");
				String mtid = rs.getString("MTID");
				String program = rs.getString("PROGRAM");
				String school = rs.getString("SCHOOL");
				String adminNo = rs.getString("ADMIN_NO");
				String session = rs.getString("SESSION");
				String semester = rs.getString("SEMESTER");
				String courses = rs.getString("COURSES");
				double examscore =  rs.getDouble("EXAM_SCORE");
				double cascore =  rs.getDouble("CA_SCORE");
				String createdate = rs.getString("CREATE_DATE");
				String companyCode = rs.getString("COMP_CODE");
				double totalscore =  rs.getDouble("TOTAL_SCORE");
			//	int userid = rs.getInt("USERID");
				Result Result = new Result();
				Result.setMtId(mtid);
//				Result.setBatch(batch);
				Result.setProgram(program);
				Result.setSchool(school);
				Result.setAdmin_no(adminNo);
				Result.setSession(session);
				Result.setSemester(semester);
				Result.setExamscore(examscore);
				Result.setCascore(cascore);		
				Result.setCourses(courses);
				Result.setTotalScore(totalscore);
				Result.setCreatedate(createdate);
				Result.setCompanyCode(companyCode);
				_list.add(Result);
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
public boolean StudentPositionupdate(String mtid,String adminno, String subjectcode, int position) {
    Connection con = null;
    PreparedStatement ps = null; 
    String UPDATE_QUERY = "UPDATE IA_PERFORMANCE SET POSITION = "+position+"  WHERE MTID = '"+mtid+"'";
    System.out.println("IA_PERFORMANCE IN StudentScoreupdate Query: " + UPDATE_QUERY);
    boolean done;
    done = false;
    try {
    	con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);         
        ps.executeUpdate();           
        done = true;
    } catch (Exception ex) {
        System.out.println("WARNING: cannot update IA_PERFORMANCE IN StudentScoreupdate" + ex.getMessage());
    } finally {
        closeConnection(con, ps);
    }
    return done;
}
public boolean StudentRecordsupdate(String adminno, String companyCode, String school, String program, String courses, String semester, String session, 
        double examscore, double cascore, double totalscore,String term,String gradecode,String gradename,String subjectcode) 
{
    double firstterm = 0.00;
    double secondterm = 0.00;
    double thirdterm = 0.00;    
    Connection con = null;
    PreparedStatement ps = null; 
    String UPDATE_QUERY = "UPDATE IA_PERFORMANCE SET EXAM_SCORE = "+examscore+", " +
    		"CA_SCORE = "+cascore+", TOTAL_SCORE = "+totalscore+", FIRST_TERM_SCORE = "+firstterm+", " +
    		"SECOND_TERM_SCORE = "+secondterm+", THIRD_TERM_SCORE = "+thirdterm+" " +
    		"WHERE ADMIN_NO = '"+adminno+"' AND SUBJECT_CODE = '"+subjectcode+"'";
    boolean done;
    done = false;

    try {
    	if(term.equalsIgnoreCase("FIRST")){firstterm = totalscore;}
    	if(term.equalsIgnoreCase("SECOND")){secondterm = totalscore;}
    	if(term.equalsIgnoreCase("THIRD")){thirdterm = totalscore;}    	
    	con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);         
        ps.executeUpdate();           
        done = true;
    } catch (Exception ex) {
        System.out.println("WARNING: cannot update IA_STUDENT_SCORED IN StudentRecordsupdate" + ex.getMessage());
    } finally {
        closeConnection(con, ps);
    }
    return done;
}
public boolean DeleteStudentRecord(String adminno, String subjectcode) {
    Connection con = null;
    PreparedStatement ps = null; 
    String DELETE_QUERY = "DELETE IA_PERFORMANCE WHERE ADMIN_NO = '"+adminno+"' AND SUBJECT_CODE = '"+subjectcode+"'";
    boolean done;
    done = false;
    try {
    	con = getConnection();
        ps = con.prepareStatement(DELETE_QUERY);         
        ps.executeUpdate();           
        done = true;
    } catch (Exception ex) {
        System.out.println("WARNING: cannot Delete Record from IA_STUDENT_SCORED IN DeleteStudentRecord" + ex.getMessage());
    } finally {
        closeConnection(con, ps);
    }
    return done;
}
public java.util.ArrayList getClassList()
{
	java.util.ArrayList list2 = new java.util.ArrayList();
		String query = " SELECT  * from MG_GB_COURSES where STATUS ='ACTIVE' ";
	Connection c = null;
	ResultSet rs = null;
	Statement s = null;
//	System.out.println("getClassList query: "+query);
	try {
		    c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next())
			   {				
				String coursecode = rs.getString("Course_code");
				String Description = rs.getString("description");
				Result Result = new Result();
				Result.setCourses(coursecode);
				Result.setDescription(Description);
				list2.add(Result);
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
	return list2;
}
public java.util.ArrayList getSubjectList()
{
	java.util.ArrayList list2 = new java.util.ArrayList();
		String query = " SELECT SUBJECT_CODE,SUBJECT_NAME from IA_SUBJECT_COURSES where STATUS ='ACTIVE' ";
	Connection c = null;
	ResultSet rs = null;
	Statement s = null;
	try {
		    c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next())
			   {				
				String Subjectcode = rs.getString("SUBJECT_CODE");
				String Description = rs.getString("SUBJECT_NAME");
				Result Result = new Result();
				Result.setSubjectcode(Subjectcode);
				Result.setDescription(Description);
				list2.add(Result);
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
	return list2;
}
public java.util.ArrayList getSchoolList()
{
	java.util.ArrayList list2 = new java.util.ArrayList();
	String query = " SELECT  * from MG_AD_Branch where STATUS ='ACTIVE' ";
	Connection c = null;
	ResultSet rs = null;
	Statement s = null;
	try {
		    c = getConnection();  
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next())
			   {				
				String School = rs.getString("BRANCH_CODE");
				String Description = rs.getString("BRANCH_NAME");
				String positionbyarm = rs.getString("POSITION_BY_ARM");
				Result Result = new Result();
				Result.setSchool(School);
				Result.setDescription(Description);
				Result.setPositionbyarm(positionbyarm);
				list2.add(Result);
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
	return list2;
}

public java.util.ArrayList getStudentOverallPosition(String filter)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	  String SELECT_QUERY =  filter; 
	Connection c = null;
	ResultSet rs = null;
	Statement s = null;

	try {
		    c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(SELECT_QUERY);
			while (rs.next())
			   {				
				//String batch = rs.getString("BATCH");
				String mtid = rs.getString("MTID");
				String program = rs.getString("PROGRAM");
				String school = rs.getString("SCHOOL");
				String adminNo = rs.getString("ADMIN_NO");
				String session = rs.getString("SESSION");
				String semester = rs.getString("SEMESTER");
				String courses = rs.getString("COURSES");
				String createdate = rs.getString("CREATE_DATE");
				String companyCode = rs.getString("COMP_CODE");
				double totalscore =  rs.getDouble("TOTAL_SCORE");
			//	int userid = rs.getInt("USERID");
				Result Result = new Result();
				Result.setMtId(mtid);
//				Result.setBatch(batch);
				Result.setProgram(program);
				Result.setSchool(school);
				Result.setAdmin_no(adminNo);
				Result.setSession(session);
				Result.setSemester(semester);	
				Result.setCourses(courses);
				Result.setTotalScore(totalscore);
				Result.setCreatedate(createdate);
				Result.setCompanyCode(companyCode);
				_list.add(Result);
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

public boolean StudentOverallPositionUpdate(String mtid,String adminno, int position) {
    Connection con = null;
    PreparedStatement ps = null; 
    String UPDATE_QUERY = "UPDATE IA_STUDENT_RESULT SET POSITION = "+position+"  WHERE MTID = '"+mtid+"' AND ADMIN_NO = '"+adminno+"'";
  //  System.out.println("StudentOverallPositionUpdate UPDATE_QUERY: "+UPDATE_QUERY);
    boolean done;
    done = false;
    try {
    	con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);         
        ps.executeUpdate();           
        done = true;
    } catch (Exception ex) {
        System.out.println("WARNING: cannot update IA_STUDENT_RESULT IN StudentOverallPosition" + ex.getMessage());
    } finally {
        closeConnection(con, ps);
    }
    return done;
}
public java.util.ArrayList getClassArmList(String filter)
{
	java.util.ArrayList list2 = new java.util.ArrayList();
		String query = filter;
	Connection c = null;
	ResultSet rs = null;
	Statement s = null;
	try {
		    c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next())
			   {				
				String armclass = rs.getString("CLASS_ARM");
				String Description = rs.getString("CLASS_NAME");
				Result Result = new Result();
				Result.setClassarm(armclass);
				Result.setDescription(Description);
				list2.add(Result);
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
	return list2;
}

public java.util.ArrayList getStudentUploadRecords()
{
	java.util.ArrayList list2 = new java.util.ArrayList();
	String date = String.valueOf(df.dateConvert(new java.util.Date()));
	//System.out.println("====eschool Server Date for Student Records-----> "+date);
	date = date.substring(0, 10);
	String finacleTransId= null;
//	String query = " SELECT  * from IA_STUDENT_SCORED where STATUS <> 'P' ";
	String query = " SELECT  * from REVERSAL_UPLOAD_TMP where PROCESS_FLAG = 'N' ";
//	Transaction transaction = null;
	ExcelBean Result = null;
	Connection c = null;
	ResultSet rs = null;
	Statement s = null;

	try {
		    c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next())
			   {	
				
				String id = rs.getString("ID");
				String companyCode = rs.getString("COMP_CODE");
				String description = rs.getString("TRANSNARRATION");

			   String schoolCode = rs.getString("SCHOOL_CODE");
			   String adminNo = rs.getString("ADMIN_NO");
			   String draccount = rs.getString("DRACCOUNT");
			   String draccountType = rs.getString("DRACCT_TYPE");
			   double amount = rs.getDouble("AMOUNT");
			   String createdate = formatDate(rs.getDate("CREATION_DATE"));
			   String craccount = rs.getString("CRACCOUNT");
			   String craccountType = rs.getString("CRACCT_TYPE");
			   String term = rs.getString("TERM");
			   Result = new ExcelBean();
			   Result.setMtId(id);
			   Result.setCompanyCode(companyCode);
			   Result.setSchoolCode(schoolCode);
	           Result.setAdminNo(adminNo);
	           Result.setDraccountNo(draccount);
	           Result.setDraccountType(draccountType);
	           Result.setCraccountNo(craccount);
	           Result.setCraccountType(craccountType);
	           Result.setTransdescription(description);
	           Result.setTransamount(String.valueOf(amount));
	           Result.setTerm(term);

				list2.add(Result);
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
	return list2;
}

}
