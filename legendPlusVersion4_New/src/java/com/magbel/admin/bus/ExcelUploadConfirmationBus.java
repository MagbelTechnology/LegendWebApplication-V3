/**
 * <p>Title: ConfirmationServiceBus.java</p>
 *
 * <p>Description:Manages Cheque confirmation records </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: MagBel Technology LTD.</p>
 *
 * @author Jejelowo B.Festus
 * @version 1.0
 * updated by Kalu Nsi Idika
 * for version 2.0
 * May 2008
 */

package com.magbel.admin.bus;

import java.util.ArrayList;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import com.magbel.admin.objects.VerifyExcelData;
import oracle.jdbc.OracleResultSet;
import oracle.sql.BLOB;
import sun.net.ApplicationProxy;
import com.magbel.util.ApplicationHelper;
import com.magbel.admin.dao.ConnectionClass;
import com.magbel.admin.dao.MagmaDBConnection;
import com.magbel.admin.mail.EmailSmsServiceBus;
import com.magbel.admin.objects.ExcelConfirmation;
public class ExcelUploadConfirmationBus extends ConnectionClass {
	private ApplicationHelper helper;
//	ApplicationProxy helper;
	com.magbel.util.DatetimeFormat df;
//	AssetExcelUploadManagerFile bus;
	private MagmaDBConnection dbConnection;
	SimpleDateFormat sdf;
	private VerifyExcelData helpdeskData = null;
	boolean created = true;

	public ExcelUploadConfirmationBus() throws Exception	{
		try {		
		helper = new ApplicationHelper();
		sdf = new SimpleDateFormat("dd-MM-yyyy");

//		bus =new AssetExcelUploadManagerFile();
	    } catch (Exception ex) {
	    }
	}

	public boolean isCreated() {

		return this.created;
	}

	public void setCreated(boolean created) {
		this.created = created;
	}

	/**
	 * Create new Mandatory Field createConfirmation
	 * 
	 * @param formId
	 *            String
	 * @param formField
	 *            String
	 * @param formLabel
	 *            String
	 * @param message
	 *            String
	 */
	public int insertBlobData(final String Issue_id, final String accountNo, final String filePath) {

		/*
		*	 Initialize the necessary parameters
		*/
		int returnValue = 0;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		InputStream is = null;
		OutputStream os = null;
		String query = null;
	//	Date sysdate = dateConvert(new java.util.Date());
		try {
			 /* 
			* Register the Oracle driver 
			*/
			conn = getConnection(); 
			/*
			* Establish a connection to the Oracle database. I have used the Oracle Thin driver.
			* jdbc:oracle:thin@host:port:sid, "user name", "password"
			*/
//			conn = DriverManager.getConnection("jdbc:oracle:thin:@visions-bwckzjd:1521:o8i", "internal", "oracle");
			/*
			* Set auto commit to false, it helps to speed up things, by default JDBC's auto commit feature is on.
			* This means that each SQL statement is commited as it is executed.
			*/
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			/*
			* First execute a query to see if a file with the same Issue Id already exists.
			*/
//			query = "SELECT FileId FROM tblBlobDemo WHERE FileName=\'" + fileName + "\' AND FileDescription=\'" + fileDescription + "\' ORDER BY FileId DESC";
			System.out.println("checking");
			query =  "SELECT complaint_id  FROM HD_COMPLAINT WHERE complaint_id='"+Issue_id+"'";		
			rs = stmt.executeQuery(query);
			if(!rs.next()) {
				System.out.println("inserting");
				/*
				* Insert all the data, for the BLOB column we use the function empty_blob(), which creates a locator for the BLOB datatype.
				* A locator is an object that ponts to the actual location of the BLOB data in the database. A locator is essential to manipulate BLOB data. 
				*/
//				query = "INSERT INTO tblBlobDemo VALUES(seqFileId.NextVal,  sysdate, \'" + fileName + "\', empty_blob(), \'" + fileDescription + "\')";
				query = "INSERT INTO CP_CONFIRMATION_IMAGE(CHEQUE_NO,ACCOUNT_NO) VALUES('"+Issue_id+"','"+accountNo+"')";
				
				stmt.execute(query);
				/*
				* Once the locator has been inserted, we retrieve the locator by executing a SELECT statement with the FOR UPDATE clause to manually lock the row.
				*/
				System.out.println("updating");
				query =  "SELECT IMAGE FROM CP_CONFIRMATION_IMAGE WHERE CHEQUE_NO='"+Issue_id+"' AND ACCOUNT_NO='"+accountNo+"' ORDER BY ACCOUNT_NO DESC FOR UPDATE";
				rs = stmt.executeQuery(query);
				if(rs.next()) 
				{
					/*
					* Once a locator has been retrieved we can use it to insert the binary data into the database.
					*/
					BLOB blob = ((OracleResultSet)rs).getBLOB("IMAGE");//image
					os = blob.getBinaryOutputStream();
					final File f = new File(filePath);//location
					is = new FileInputStream(f);
		
					final byte[] buffer = new byte[blob.getBufferSize()];
					int bytesRead = 0;
					while((bytesRead = is.read(buffer)) != -1) {
						os.write(buffer, 0, bytesRead);
						System.out.println("success");
					}
					blob = null;
				}
				else {
					returnValue = 1;
				}
			}
			else {
				returnValue = 2;
			}
		}
		
		catch(FileNotFoundException e) {
			returnValue = 4;
			e.printStackTrace();
		}
		catch(Exception e) {
			returnValue = 5;
			e.printStackTrace();
		}
		finally {
			/*
			* Clean up.
			*/
			try {
				if(is != null) {
					is.close();
				}
				if(os != null) {
					os.flush();
					os.close();
				}
				if(stmt != null) {
					stmt.close();
				}
				if(rs != null) {
					rs.close();
				}
				if(conn != null) {
					conn.commit();
					conn.close();
				}
				is = null;
				os = null;
				stmt = null;
				rs = null;
				conn = null;
				query = null;
			}
			catch(Exception e) {
				returnValue = 6;
				e.printStackTrace();
			}
		}
		return returnValue;
	}

	
		public byte[] selectBlobData(final String chequeNo, final String accountNo) {
			/*
			*	 Initialize the necessary parameters
			*/
			System.out.println("INFO::Loading Customer image -->");
	        byte[] byteArray = null;
			int returnValue = 0;
			Connection conn = null;
			Statement stmt = null;
			ResultSet rs = null;
			InputStream is = null;
			OutputStream os = null;
			BLOB blob = null;
			try {
//				final String query2 = "SELECT FileName, FileData FROM tblBlobDemo WHERE FileId = " + fileId;
				 /* 
				* Register the Oracle driver 
				*/
				conn = getConnection(); 
				/*
				* Establish a connection to the Oracle database. I have used the Oracle Thin driver.
				* jdbc:oracle:thin@host:port:sid, "user name", "password"
				*/
//				conn = DriverManager.getConnection("jdbc:oracle:thin:@visions-bwckzjd:1521:o8i", "internal", "oracle");
				/*
				* Set auto commit to false, it helps to speed up things, by default JDBC's auto commit feature is on.
				* This means that each SQL statement is commited as it is executed.
				*/
				conn.setAutoCommit(false);
				stmt = conn.createStatement();
				/*
				* Execute the SELECT statement
				*/
				String query =  "SELECT IMAGE FROM CP_CONFIRMATION_IMAGE WHERE CHEQUE_NO='"+chequeNo+"' AND ACCOUNT_NO='"+accountNo+"'";
				rs = stmt.executeQuery(query);
				if(rs.next()) {
					/*
					* Extract the BLOB data to a file on the local file system.
					*/
					blob = ((OracleResultSet)rs).getBLOB("IMAGE");
					is = blob.getBinaryStream();
//					final String fileName = rs.getString("FileName");
//					final String filePath = path + fileName;
//					os = new FileOutputStream(filePath);
					final int bufferSize = blob.getBufferSize();
					final byte[] buffer = new byte[bufferSize];
					int bytesRead = 0;
					while ((bytesRead = is.read(buffer)) != -1) {
						os.write(buffer, 0, bytesRead);
						byteArray = buffer;
					}
				}
				else {
					returnValue = 1;
				}
			}

			catch(FileNotFoundException e) {
				returnValue = 3;
				e.printStackTrace();
			}
			catch(Exception e) {
				returnValue = 4;
				e.printStackTrace();
			}
			finally {
				/*
				* Clean up.
				*/
				try {
					if(is != null) {
						is.close();
					}
					if(os != null) {
						os.flush();
						os.close();
					}
					if(stmt != null) {
						stmt.close();
					}
					if(rs != null) {
						rs.close();
					}
					if(conn != null) {
						conn.commit();
						conn.close();
					}
					is = null;
					os = null;
					stmt = null;
					rs = null;
					conn = null;
					blob = null;
				}
				catch(Exception e) {
					returnValue = 5;
					e.printStackTrace();
				}
			}
			return byteArray;
		}
	
	
/*	
	
	
	public void notifyCustomer(String accountNo,String chequeNo)
	{
		    EmailSmsServiceBus emailSmsService = new EmailSmsServiceBus();
		//	SMSEmailMessage smsEmailMsg = new SMSEmailMessage();
	      //  smsEmailMsg = findMessageEnablement();
			
			
				//check acct
				VerifyAcctNumberData acctInfo = findAccountByNo(accountNo);
				//email
				 if (smsEmailMsg.getEmailEnabled().equalsIgnoreCase("Y"))
				    {
					    System.out.print("Before Sending mail in servlet: >>>>"+accountNo);
					    System.out.print("The Email Address in servlet: >>>>"+acctInfo.getEmail());
					    emailSmsService.sendNoConfirmationEmail(acctInfo.getEmail(), chequeNo, acctInfo.getAccountNumber(), acctInfo.getAccountName());
					    System.out.print("After Sending mail successfully in servlet: >>>>"+accountNo);
				    }
				//sms
				 if (smsEmailMsg.getSmsEnabled().equalsIgnoreCase("Y"))
				    {
					    String 	fromUser = "UBA ALERT";
					    String msgBody = "NO VERIFICATION FOR CHEQUE: ";
					    msgBody	+= "THE CHQ WITH NO: "+chequeNo+"\n";
					    msgBody	+= "ACCT NO: "+accountNo+"\n";
					    System.out.print("Before Sending SMS in servlet: >>>>"+accountNo);
					    System.out.print("The recipient phone number in servlet: >>>>"+acctInfo.getPhoneNo());
					    String channel = "209";
					    String phoneConvert = acctInfo.getPhoneNo();
					    phoneConvert ="+234"+phoneConvert.substring(1, phoneConvert.length());
					    
					    System.out.println("phone number  ="+phoneConvert);
					    String sentSMS = emailSmsService.sendApprovalSMS(fromUser, phoneConvert, msgBody, channel);
					    //String sentSMS = emailSmsService.sendApprovalSMS(fromUser, acctInfo.getPhoneNo(), msgBody, channel);
					    System.out.print("After Sending successfully SMS in servlet: >>>>"+accountNo);
					    System.out.print("SMS sent in servlet: >>>>"+sentSMS);
				    }
			
			
			
	}*/
	public void createConfirmation(String chequeNo, String chequeDate,
			String accountNo, String accountName, double amount,
			String amountInWords, String beneficiary, String mandate,
			String userId, String branch,String scannedimage) {

		String CREATE_QUERY = "INSERT INTO CP_CONFIRMATION( "
				+ "MTID,CHEQUE_NO,CHEQUE_DT,ACCOUNT_NO,"
				+ "ACCOUNT_NAME,AMOUNT,AMOUNT_IN_WORDS,BENEFICIARY,"
				+ "MANDATE_INSTR,STATUS,USER_ID,BRANCH,TRAN_DT,SCANNEDIMAGE) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection con = null;
		PreparedStatement ps = null;
		String id = helper.getGeneratedId("CP_CONFIRMATION");

		try {

			con = getConnection();
			ps = con.prepareStatement(CREATE_QUERY);

			ps.setString(1, id);
			ps.setString(2, chequeNo);
	//		ps.setDate(3, dateConvert(chequeDate));
			ps.setString(4, accountNo);

			ps.setString(5, accountName);
			ps.setDouble(6, amount);
			ps.setString(7, amountInWords);
			ps.setString(8, beneficiary);
			ps.setString(9, mandate);

			ps.setString(10, "A");

			ps.setString(11, userId);
			ps.setString(12, branch);
//			ps.setDate(13, dateConvert(new java.util.Date()));
			ps.setString(14, scannedimage);

			ps.execute();

		} catch (Exception er) {
			System.out.println("WARN:Error creating confirmation ->"
					+ er.getMessage());
			setCreated(false);
		} finally {
			dbConnection.closeConnection(con, ps);
		}

	}
	public void createConfirmationApprove(String chequeNo, String chequeDate,
			String accountNo, String accountName, double amount,
			String amountInWords, String beneficiary, String mandate,
			String userId, String branch,String scannedimage,String scanimage,String phone,String bom	
	) {

		String CREATE_QUERY = "INSERT INTO CP_CONFIRMATION_APPROVE( "
				+ "MTID,CHEQUE_NO,CHEQUE_DT,ACCOUNT_NO,"
				+ "ACCOUNT_NAME,AMOUNT,AMOUNT_IN_WORDS,BENEFICIARY,"
				+ "MANDATE_INSTR,STATUS,USER_ID,BRANCH,TRAN_DT,SCANNEDIMAGE,SCANIMAGE,PHONE,BOM) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection con = null;
		PreparedStatement ps = null;
		String id = helper.getGeneratedId("CP_CONFIRMATION");

		try {

			con = getConnection();
			ps = con.prepareStatement(CREATE_QUERY);

			ps.setString(1, id);
			ps.setString(2, chequeNo);
//			ps.setDate(3, dateConvert(chequeDate));
			ps.setString(4, accountNo);

			ps.setString(5, accountName);
			ps.setDouble(6, amount);
			ps.setString(7, amountInWords);
			ps.setString(8, beneficiary);
			ps.setString(9, mandate);

			ps.setString(10, "A");

			ps.setString(11, userId);
			ps.setString(12, branch);
//			ps.setDate(13, dateConvert(new java.util.Date()));
			ps.setString(14, scannedimage);
			ps.setString(15, scanimage);
			ps.setString(16, phone);
			ps.setString(17, bom);

			ps.execute();

		} catch (Exception er) {
			System.out.println("WARN:Error creating confirmation ->"
					+ er.getMessage());
			setCreated(false);
		} finally {
			dbConnection.closeConnection(con, ps);
		}

	}
	public ArrayList findConfirmationByQuery(String filter) {

		ArrayList records = new ArrayList();
		String SELECT_QUERY = "SELECT MTID,CHEQUE_NO,CHEQUE_DT,ACCOUNT_NO,ACCOUNT_NAME,AMOUNT,AMOUNT_IN_WORDS,BENEFICIARY,STATUS,USER_ID,AUTH_USER,CONF_USER,CONF_BRANCH,BRANCH,MANDATE_INSTR,DELETE_REASON FROM CP_CONFIRMATION " + filter;
		System.out.println(SELECT_QUERY);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = getConnection();
			ps = con.prepareStatement(SELECT_QUERY);

			rs = ps.executeQuery();

			while (rs.next()) {

				String id = rs.getString("MTID");
				String chequeNo = rs.getString("CHEQUE_NO");
				java.util.Date chequeDT = rs.getDate("CHEQUE_DT");
				String chequeDate = sdf.format(chequeDT);
				String accountNo = rs.getString("ACCOUNT_NO");
				String acountName = rs.getString("ACCOUNT_NAME");
				double amount = rs.getDouble("AMOUNT");
				String amountInWords = rs.getString("AMOUNT_IN_WORDS");
				String beneficiary = rs.getString("BENEFICIARY");
				String status = rs.getString("STATUS");
				String userId = rs.getString("USER_ID");
				String authourizedUser = rs.getString("AUTH_USER");
				String confirmedUser = rs.getString("CONF_USER");
				String confirmedBranch = rs.getString("CONF_BRANCH");
				String branch = rs.getString("BRANCH");
				String mandate = rs.getString("MANDATE_INSTR");
				String deleteReason = rs.getString("DELETE_REASON");

				ExcelConfirmation confirmation = new ExcelConfirmation(id, chequeNo,
						chequeDate, accountNo, acountName, amount,
						amountInWords, beneficiary, branch, status, userId,
						authourizedUser, confirmedUser, confirmedBranch,
						mandate,deleteReason);
				records.add(confirmation);
			}

		} catch (Exception er) {
			System.out.println("Error finding All Confirmation...->"
					+ er.getMessage());
		} finally {
			dbConnection.closeConnection(con, ps, rs);
		}

		return records;
	}
	/*public ConfirmationDirect findConfirmationByIdDirect(String id) {

		ConfirmationDirect confirmation = null;
		String filter = " WHERE MTID = '" + id + "' ";
		ArrayList records = findConfirmationByQueryDirectUpdate(filter);
		if (records.size() > 0) {
			confirmation = (ConfirmationDirect) records.get(0);
		}

		return confirmation;
	}*/
	/*
	public ArrayList findConfirmationByQueryDirectUpdate(String filter) {

		ArrayList records = new ArrayList();
		String SELECT_QUERY = "SELECT MTID,CHEQUE_NO,CHEQUE_DT,ACCOUNT_NO,ACCOUNT_NAME,AMOUNT,AMOUNT_IN_WORDS,BENEFICIARY,STATUS,USER_ID,AUTH_USER,CONF_USER,CONF_BRANCH,BRANCH,MANDATE_INSTR,DELETE_REASON,LOWER_AMOUNT,UPPER_AMOUNT,START_DATE,EXPIRY_DATE,FREQUENCY,AMOUNT_TYPE FROM CP_CONFIRMATION " + filter;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = getConnection();
			ps = con.prepareStatement(SELECT_QUERY);

			rs = ps.executeQuery();

			while (rs.next()) {

				String id = rs.getString("MTID");
				String chequeNo = rs.getString("CHEQUE_NO");
				java.util.Date chequeDT = rs.getDate("CHEQUE_DT");
				String chequeDate = sdf.format(chequeDT);
				String accountNo = rs.getString("ACCOUNT_NO");
				String acountName = rs.getString("ACCOUNT_NAME");
				double amount = rs.getDouble("AMOUNT");
				String amountInWords = rs.getString("AMOUNT_IN_WORDS");
				String beneficiary = rs.getString("BENEFICIARY");
				String status = rs.getString("STATUS");
				String userId = rs.getString("USER_ID");
				String authourizedUser = rs.getString("AUTH_USER");
				String confirmedUser = rs.getString("CONF_USER");
				String confirmedBranch = rs.getString("CONF_BRANCH");
				String branch = rs.getString("BRANCH");
				String mandate = rs.getString("MANDATE_INSTR");
				String deleteReason = rs.getString("DELETE_REASON");
				
				
				String lower = rs.getString("LOWER_AMOUNT");
				String upper = rs.getString("UPPER_AMOUNT");
				String startDate = rs.getString("START_DATE");
				String expiryDate = rs.getString("EXPIRY_DATE");
				String frequency = rs.getString("FREQUENCY");
				String amountType = rs.getString("AMOUNT_TYPE");
				
				ConfirmationDirect confirmation = new ConfirmationDirect(id, chequeNo,
						chequeDate, accountNo, acountName, amount,
						amountInWords, beneficiary, branch, status, userId,
						authourizedUser, confirmedUser, confirmedBranch,
						mandate,deleteReason,lower,upper,startDate,
						expiryDate,frequency,amountType);
				records.add(confirmation);
			}

		} catch (Exception er) {
			System.out.println("Error finding All Confirmation...->"
					+ er.getMessage());
		} finally {
			dbConnection.closeConnection(con, ps, rs);
		}

		return records;
	}*/
	public void createConfirmation2(String chequeNo, String chequeDate,
			String accountNo, String accountName, double amount,
			String amountInWords, String beneficiary, String mandate,
			String userId, String branch) {

		String CREATE_QUERY = "INSERT INTO CP_CONFIRMATION( "
				+ "MTID,CHEQUE_NO,CHEQUE_DT,ACCOUNT_NO,"
				+ "ACCOUNT_NAME,AMOUNT,AMOUNT_IN_WORDS,BENEFICIARY,"
				+ "MANDATE_INSTR,STATUS,USER_ID,BRANCH,TRAN_DT) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection con = null;
		PreparedStatement ps = null;
		String id = helper.getGeneratedId("CP_CONFIRMATION");

		try {

			con = getConnection();
			ps = con.prepareStatement(CREATE_QUERY);

			ps.setString(1, id);
			ps.setString(2, chequeNo);
	//		ps.setDate(3, dateConvert(chequeDate));
			ps.setString(4, accountNo);

			ps.setString(5, accountName);
			ps.setDouble(6, amount);
			ps.setString(7, amountInWords);
			ps.setString(8, beneficiary);
			ps.setString(9, mandate);

			ps.setString(10, "O");

			ps.setString(11, userId);
			ps.setString(12, branch);
		//	ps.setDate(13, dateConvert(new java.util.Date()));

			ps.execute();

		} catch (Exception er) {
			System.out.println("WARN:Error creating confirmation ->"
					+ er.getMessage());
			setCreated(false);
		} finally {
			dbConnection.closeConnection(con, ps);
		}

	}

	public void createConfirmationException(String id, String reason,
			String userid) {

		String CREATE_QUERY = "INSERT INTO CP_CONFIRMATION_EXCEPTION( "
				+ "MTID,REASON,USER_ID) VALUES('" + id + "','" + reason + "','"
				+ userid + "')";
//		executeQuery(CREATE_QUERY);
		notifyExemptedConfirmation(id);

	}

	/**
	 * Deletes a Mandatory Field deleteConfirmation
	 * 
	 * @param formId
	 *            String
	 */

	public void deleteConfirmation(String id) {
		String DELETE_QUERY = "DELETE FROM CP_CONFIRMATION  WHERE MTID = '"
				+ id + "'";
//		executeQuery(DELETE_QUERY);
	}

/*	public void updateConfirmation(String id, String accountName,
			double amount, String amountInWords, String beneficiary,
			String mandate,String status,String representReason) {

		String query = "UPDATE CP_CONFIRMATION SET ACCOUNT_NAME = '"
				+ accountName + "'," + "AMOUNT = " + amount
				+ ",AMOUNT_IN_WORDS = '" +amountInWords + "',"
				+ "BENEFICIARY = '" + beneficiary + "',MANDATE_INSTR = '"
				+ mandate + "',STATUS ='"+ status +"',REPRESENT_REASON='"+ representReason +"' WHERE MTID = '" + id + "'";
		executeQuery(query);
	}
	public void updateRejectConfirmation(String id, String accountName,
			double amount, String amountInWords, String beneficiary,
			String mandate,String status,String representReason,String bom) {

		String query = "UPDATE CP_CONFIRMATION_APPROVE SET ACCOUNT_NAME = '"
				+ accountName + "'," + "AMOUNT = " + amount
				+ ",AMOUNT_IN_WORDS = '" +amountInWords + "',"
				+ "BENEFICIARY = '" + beneficiary + "',MANDATE_INSTR = '"
				+ mandate + "',STATUS ='"+ status +"',REPRESENT_REASON='"+ representReason +"',bom='"+ bom +"',CFLAG='N' WHERE MTID = '" + id + "'";
		executeQuery(query);
	}*/
	public void deleteConfirmation(String status, String id,String deleteReason) {

		String query = "UPDATE CP_CONFIRMATION SET STATUS = '" + status + "', "
		+ " DATE_DELETED=?,DELETE_REASON='"+deleteReason+"' WHERE MTID = '" + id + "'";
	    Connection con = null;
	    PreparedStatement ps = null;
	    try {

	            con = getConnection();
	            ps = con.prepareStatement(query);
	            
	//            ps.setDate(1, dateConvert(new java.util.Date()));

	            ps.execute();

	    } catch (Exception er) {
	            System.out.println("WARN:Error deleting confirmation ->"
	                            + er.getMessage());
	            setCreated(false);
	    } finally {
	            dbConnection.closeConnection(con, ps);
	    }
	}

	// By Kalu: I need to separate this method into : one for Approve and the 
	// other for Reject so that Email and SMS messages can be customise for each case. 
	public void approveConfirmation(String id, String authUser,
			String rejectionReason) {
		String status = "A";
		if ((rejectionReason != null) && (rejectionReason.length() > 2)) {
			status = "R";
		}
		String query = "UPDATE CP_CONFIRMATION SET STATUS ='" + status + "', "
				+ "AUTH_USER = '" + authUser + "',REJECTION_REASON = '"
				+ rejectionReason + "', " 
				+ " DATE_REJECTED_CONF=? WHERE MTID = '" + id + "'";
	    Connection con = null;
	    PreparedStatement ps = null;
            
	    try {

	            con = getConnection();
	            ps = con.prepareStatement(query);	            
//	            ps.setDate(1, dateConvert(new java.util.Date()));

	            ps.execute();
	           // sendApprovalEmail();// here

	    } catch (Exception er) {
	            System.out.println("WARN:Error approving confirmation ->"
	                            + er.getMessage());
	    } finally {
	            dbConnection.closeConnection(con, ps);
	    }
	}

	public void rejectConfirmationPayment(String id, String userid,
			String branchCode, String rejectionReason) {
		String query = "UPDATE CP_CONFIRMATION SET STATUS ='R', "
				+ "CONF_USER = '" + userid + "',CONF_BRANCH = '" + branchCode
				+ "', " + "REJECTION_REASON = '" + rejectionReason + "', "
				+ " DATE_REJECTED_PAY=? WHERE MTID = '" + id + "'";
	    Connection con = null;
	    PreparedStatement ps = null;
	    try {

	            con = getConnection();
	            ps = con.prepareStatement(query);
	            
//	            ps.setDate(1, dateConvert(new java.util.Date()));

	            ps.execute();

	    } catch (Exception er) {
	            System.out.println("WARN:Error deleting confirmation ->"
	                            + er.getMessage());
	            setCreated(false);
	    } finally {
	            dbConnection.closeConnection(con, ps);
	    }
	}

	public void rejectConfirmationPayment(String id, String userid,
			String branchCode, String rejectionReason,String accountNo,String chequeNo) {
		String query = "UPDATE CP_CONFIRMATION SET STATUS ='R', "
				+ "CONF_USER = '" + userid + "',CONF_BRANCH = '" + branchCode
				+ "', " + "REJECTION_REASON = '" + rejectionReason + "', "
				+ " DATE_REJECTED_PAY=? WHERE ACCOUNT_NO = '" + accountNo + "'  AND CHEQUE_NO = '" + chequeNo + "' ";
	    Connection con = null;
	    PreparedStatement ps = null;
	    try {

	            con = getConnection();
	            ps = con.prepareStatement(query);
	            
	//            ps.setDate(1, dateConvert(new java.util.Date()));

	            ps.execute();

	    } catch (Exception er) {
	            System.out.println("WARN:Error deleting confirmation ->"
	                            + er.getMessage());
	            setCreated(false);
	    } finally {
	            dbConnection.closeConnection(con, ps);
	    }
	}
	public void verifyConfirmation(String id, String confUser, String conBranch,String transCode) {
		String query = "UPDATE CP_CONFIRMATION SET STATUS ='V', "
				+ "CONF_USER = '" + confUser + "',CONF_BRANCH = '" + conBranch + "'"
				+ " ,  DATE_PAID=?,TRANSACTIONCODE='" + transCode + "' WHERE MTID = '" + id + "'";
	    Connection con = null;
	    PreparedStatement ps = null;
	    try {

	            con = getConnection();
	            ps = con.prepareStatement(query);
	            
	        //    ps.setDate(1, dateConvert(new java.util.Date()));

	            ps.execute();

	    } catch (Exception er) {
	            System.out.println("WARN:Error deleting confirmation ->"
	                            + er.getMessage());
	            setCreated(false);
	    } finally {
	            dbConnection.closeConnection(con, ps);
	    }
	}

	public void notifyExemptedConfirmation(String id) {
		String query = "UPDATE CP_CONFIRMATION SET STATUS ='E' "
				+ "WHERE MTID = '" + id + "'";
//		executeQuery(query);
	}

	public ArrayList findAllConfirmation() {

		String filter = "";
		ArrayList records = findConfirmationByQuery(filter);

		return records;
	}

	public ArrayList findConfirmationByAccountNo(String accountno) {

		String filter = " WHERE ACCOUNT_NO = '" + accountno + "'";
		ArrayList records = findConfirmationByQuery(filter);

		return records;
	}

	public ArrayList findConfirmationByTransactionCode(String TransactionCode) {

		String filter = " WHERE TRANSACTIONCODE = '" + TransactionCode + "'";
		ArrayList records = findConfirmationByQuery(filter);

		return records;
	}
	
	public ArrayList findConfirmationByBranch(String solid) {

		String filter = " WHERE BRANCH = '" + solid + "'";
		ArrayList records = findConfirmationByQuery(filter);

		return records;
	}
/*
	public ArrayList findConfirmationByUserApprove(String id) {

		String filter = " WHERE BOM = '" + id + "' AND CFLAG != 'Y' ";
		ArrayList records = findConfirmationByQueryApprove(filter);

		return records;
	}
	
	public ArrayList findConfirmationForApproval(String branchCode) {
		String filter = " WHERE BRANCH = '" + branchCode + "' "
				+ " AND STATUS = 'O'";
		ArrayList records = findConfirmationByQuery(filter);

		return records;
	}*/
	public ArrayList findConfirmationForTransactionCode(String TransactionCode) {
		String filter = " WHERE TRANSACTIONCODE = '" + TransactionCode + "' ";
		ArrayList records = findConfirmationByQuery(filter);

		return records;
	}
	public ArrayList findConfirmationForException(String branchCode) {
		String filter = " WHERE BRANCH = '" + branchCode + "' "
				+ " AND STATUS = 'A'";
		ArrayList records = findConfirmationByQuery(filter);

		return records;
	}

	public ExcelConfirmation findConfirmationByChequeNo(String chequeno) {
		ExcelConfirmation Excelconfirmation = null;
		String filter = " WHERE CHEQUE_NO = '" + chequeno + "' ";
		ArrayList records = findConfirmationByQuery(filter);
		if (records.size() > 0) {
			Excelconfirmation = (ExcelConfirmation) records.get(0);
		}

		return Excelconfirmation;
	}

	public ExcelConfirmation findConfirmationByChequeAccountNo(String chequeno,
			String accountNo) {
		ExcelConfirmation ExcelConfirmation = null;
		String filter = " WHERE CHEQUE_NO = '" + chequeno + "' "
				+ "AND ACCOUNT_NO = '" + accountNo + "'";
		ArrayList records = findConfirmationByQuery(filter);
		if (records.size() > 0) {
			ExcelConfirmation = (ExcelConfirmation) records.get(0);
		}

		return ExcelConfirmation;
	}
	public ExcelConfirmation findConfirmationByChequeAccountNoDirect(String accountNo) {
		ExcelConfirmation ExcelConfirmation = null;
		String filter = " WHERE  ACCOUNT_NO = '" + accountNo + "'";
		ArrayList records = findConfirmationByQuery(filter);
		if (records.size() > 0) {
			ExcelConfirmation = (ExcelConfirmation) records.get(0);
		}

		return ExcelConfirmation;
	}
	public ExcelConfirmation findConfirmationById(String id) {

		ExcelConfirmation confirmation = null;
		String filter = " WHERE MTID = '" + id + "' ";
		ArrayList records = findConfirmationByQuery(filter);
		if (records.size() > 0) {
			confirmation = (ExcelConfirmation) records.get(0);
		}

		return confirmation;
	}
/*	public Confirmation findConfirmationByIdApprove(String id) {

		Confirmation confirmation = null;
		String filter = " WHERE MTID = '" + id + "' ";
		ArrayList records = findConfirmationByQueryApprove(filter);
		if (records.size() > 0) {
			confirmation = (Confirmation) records.get(0);
		}

		return confirmation;
	}
	public ConfirmationDirect findDirectConfirmationById(String id) {

		ConfirmationDirect confirmation = null;
		String filter = " WHERE MTID = '" + id + "' ";
		ArrayList records = findConfirmationByQueryDirectUpdate(filter);
		if (records.size() > 0) {
			confirmation = (ConfirmationDirect) records.get(0);
		}

		return confirmation;
	}*/
/*	public boolean isConfirmationExisting(String chequeNo, String accountNo) {

		boolean exists = false;
		String filter = " WHERE CHEQUE_NO = '" + chequeNo
				+ "' AND ACCOUNT_NO = '" + accountNo + "' ";
		ArrayList records = findConfirmationByQuery(filter);
		if (records.size() > 0) {
			exists = true;
		}
		return exists;
	} */
/*	public boolean isConfirmationExistingApprove(String chequeNo, String accountNo) {

		boolean exists = false;
		String filter = " WHERE CHEQUE_NO = '" + chequeNo
				+ "' AND ACCOUNT_NO = '" + accountNo + "' ";
		ArrayList records = findConfirmationByQueryApprove(filter);
		if (records.size() > 0) {
			exists = true;
		}
		return exists;
	}
	*/
	public ArrayList findUploadfailByQuery(String filter) {

		ArrayList records = new ArrayList();
		String SELECT_QUERY = "SELECT MTID,CHEQUE_NO,CHEQUE_DT,ACCOUNT_NO,ACCOUNT_NAME,AMOUNT,AMOUNT_IN_WORDS,BENEFICIARY,STATUS,USER_ID,AUTH_USER,CONF_USER,CONF_BRANCH,BRANCH,MANDATE_INSTR,DELETE_REASON FROM CP_CONFIRMATION_FAILED_UPLOAD " + filter;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = getConnection();
			ps = con.prepareStatement(SELECT_QUERY);

			rs = ps.executeQuery();

			while (rs.next()) {

				String id = rs.getString("MTID");
				String chequeNo = rs.getString("CHEQUE_NO");
				java.util.Date chequeDT = rs.getDate("CHEQUE_DT");
				String chequeDate = sdf.format(chequeDT);
				String accountNo = rs.getString("ACCOUNT_NO");
				String acountName = rs.getString("ACCOUNT_NAME");
				double amount = rs.getDouble("AMOUNT");
				String amountInWords = rs.getString("AMOUNT_IN_WORDS");
				String beneficiary = rs.getString("BENEFICIARY");
				String status = rs.getString("STATUS");
				String userId = rs.getString("USER_ID");
				String authourizedUser = rs.getString("AUTH_USER");
				String confirmedUser = rs.getString("CONF_USER");
				String confirmedBranch = rs.getString("CONF_BRANCH");
				String branch = rs.getString("BRANCH");
				String mandate = rs.getString("MANDATE_INSTR");
				String deleteReason = rs.getString("DELETE_REASON");

				ExcelConfirmation ExcelConfirmation = new ExcelConfirmation(id, chequeNo,
						chequeDate, accountNo, acountName, amount,
						amountInWords, beneficiary, branch, status, userId,
						authourizedUser, confirmedUser, confirmedBranch,
						mandate,deleteReason);
				records.add(ExcelConfirmation);
			}

		} catch (Exception er) {
			System.out.println("Error finding All ExcelConfirmation...->"
					+ er.getMessage());
		} finally {
			dbConnection.closeConnection(con, ps, rs);
		}

		return records;
	}
	public ArrayList findUploadSuccesByQuery(String filter) {

		ArrayList records = new ArrayList();
		String SELECT_QUERY = "SELECT MTID,CHEQUE_NO,CHEQUE_DT,ACCOUNT_NO,ACCOUNT_NAME,AMOUNT,AMOUNT_IN_WORDS,BENEFICIARY,STATUS,USER_ID,AUTH_USER,CONF_USER,CONF_BRANCH,BRANCH,MANDATE_INSTR,DELETE_REASON FROM CP_ExcelConfirmation " + filter;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = getConnection();
			ps = con.prepareStatement(SELECT_QUERY);

			rs = ps.executeQuery();

			while (rs.next()) {

				String id = rs.getString("MTID");
				String chequeNo = rs.getString("CHEQUE_NO");
				java.util.Date chequeDT = rs.getDate("CHEQUE_DT");
				String chequeDate = sdf.format(chequeDT);
				String accountNo = rs.getString("ACCOUNT_NO");
				String acountName = rs.getString("ACCOUNT_NAME");
				double amount = rs.getDouble("AMOUNT");
				String amountInWords = rs.getString("AMOUNT_IN_WORDS");
				String beneficiary = rs.getString("BENEFICIARY");
				String status = rs.getString("STATUS");
				String userId = rs.getString("USER_ID");
				String authourizedUser = rs.getString("AUTH_USER");
				String confirmedUser = rs.getString("CONF_USER");
				String confirmedBranch = rs.getString("CONF_BRANCH");
				String branch = rs.getString("BRANCH");
				String mandate = rs.getString("MANDATE_INSTR");
				String deleteReason = rs.getString("DELETE_REASON");

				ExcelConfirmation ExcelConfirmation = new ExcelConfirmation(id, chequeNo,
						chequeDate, accountNo, acountName, amount,
						amountInWords, beneficiary, branch, status, userId,
						authourizedUser, confirmedUser, confirmedBranch,
						mandate,deleteReason);
				records.add(ExcelConfirmation);
			}

		} catch (Exception er) {
			System.out.println("Error finding All ExcelConfirmation...->"
					+ er.getMessage());
		} finally {
			dbConnection.closeConnection(con, ps, rs);
		}

		return records;
	}
	public ArrayList findExcelConfirmationByQueryApprove(String filter) {

		ArrayList records = new ArrayList();
		String SELECT_QUERY = "SELECT MTID,CHEQUE_NO,CHEQUE_DT,ACCOUNT_NO,ACCOUNT_NAME,AMOUNT,AMOUNT_IN_WORDS,BENEFICIARY,STATUS,USER_ID,AUTH_USER,CONF_USER,CONF_BRANCH,BRANCH,MANDATE_INSTR,DELETE_REASON FROM CP_ExcelConfirmation_APPROVE " + filter;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = getConnection();
			ps = con.prepareStatement(SELECT_QUERY);

			rs = ps.executeQuery();

			while (rs.next()) {

				String id = rs.getString("MTID");
				String chequeNo = rs.getString("CHEQUE_NO");
				java.util.Date chequeDT = rs.getDate("CHEQUE_DT");
				String chequeDate = sdf.format(chequeDT);
				String accountNo = rs.getString("ACCOUNT_NO");
				String acountName = rs.getString("ACCOUNT_NAME");
				double amount = rs.getDouble("AMOUNT");
				String amountInWords = rs.getString("AMOUNT_IN_WORDS");
				String beneficiary = rs.getString("BENEFICIARY");
				String status = rs.getString("STATUS");
				String userId = rs.getString("USER_ID");
				String authourizedUser = rs.getString("AUTH_USER");
				String confirmedUser = rs.getString("CONF_USER");
				String confirmedBranch = rs.getString("CONF_BRANCH");
				String branch = rs.getString("BRANCH");
				String mandate = rs.getString("MANDATE_INSTR");
				String deleteReason = rs.getString("DELETE_REASON");

				ExcelConfirmation ExcelConfirmation = new ExcelConfirmation(id, chequeNo,
						chequeDate, accountNo, acountName, amount,
						amountInWords, beneficiary, branch, status, userId,
						authourizedUser, confirmedUser, confirmedBranch,
						mandate,deleteReason);
				records.add(ExcelConfirmation);
			}

		} catch (Exception er) {
			System.out.println("Error finding All ExcelConfirmation...->"
					+ er.getMessage());
		} finally {
			dbConnection.closeConnection(con, ps, rs);
		}

		return records;
	}
	public ArrayList findExcelConfirmationByQuery(String filter) {

		ArrayList records = new ArrayList();
		String SELECT_QUERY = "SELECT MTID,CHEQUE_NO,CHEQUE_DT,ACCOUNT_NO,ACCOUNT_NAME,AMOUNT,AMOUNT_IN_WORDS,BENEFICIARY,STATUS,USER_ID,AUTH_USER,CONF_USER,CONF_BRANCH,BRANCH,MANDATE_INSTR,DELETE_REASON FROM CP_ExcelConfirmation " + filter;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = getConnection();
			ps = con.prepareStatement(SELECT_QUERY);

			rs = ps.executeQuery();

			while (rs.next()) {

				String id = rs.getString("MTID");
				String chequeNo = rs.getString("CHEQUE_NO");
				java.util.Date chequeDT = rs.getDate("CHEQUE_DT");
				String chequeDate = sdf.format(chequeDT);
				String accountNo = rs.getString("ACCOUNT_NO");
				String acountName = rs.getString("ACCOUNT_NAME");
				double amount = rs.getDouble("AMOUNT");
				String amountInWords = rs.getString("AMOUNT_IN_WORDS");
				String beneficiary = rs.getString("BENEFICIARY");
				String status = rs.getString("STATUS");
				String userId = rs.getString("USER_ID");
				String authourizedUser = rs.getString("AUTH_USER");
				String confirmedUser = rs.getString("CONF_USER");
				String confirmedBranch = rs.getString("CONF_BRANCH");
				String branch = rs.getString("BRANCH");
				String mandate = rs.getString("MANDATE_INSTR");
				String deleteReason = rs.getString("DELETE_REASON");

				ExcelConfirmation ExcelConfirmation = new ExcelConfirmation(id, chequeNo,
						chequeDate, accountNo, acountName, amount,
						amountInWords, beneficiary, branch, status, userId,
						authourizedUser, confirmedUser, confirmedBranch,
						mandate,deleteReason);
				records.add(ExcelConfirmation);
			}

		} catch (Exception er) {
			System.out.println("Error finding All ExcelConfirmation...->"
					+ er.getMessage());
		} finally {
			dbConnection.closeConnection(con, ps, rs);
		}

		return records;
	}
	public ArrayList findExcelConfirmationByQuery(String chequeno,String accountno) {

		ArrayList records = new ArrayList();
		String SELECT_QUERY = "SELECT MTID,CHEQUE_NO,CHEQUE_DT,ACCOUNT_NO,ACCOUNT_NAME,AMOUNT,AMOUNT_IN_WORDS,BENEFICIARY,STATUS,USER_ID,AUTH_USER,CONF_USER,CONF_BRANCH,BRANCH,MANDATE_INSTR,DELETE_REASON FROM CP_ExcelConfirmation WHERE CHEQUE_NO='"+chequeno+"' AND ACCOUNT_NO='"+accountno+"'";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = getConnection();
			ps = con.prepareStatement(SELECT_QUERY);

			rs = ps.executeQuery();

			while (rs.next()) {

				String id = rs.getString("MTID");
				String chequeNo = rs.getString("CHEQUE_NO");
				java.util.Date chequeDT = rs.getDate("CHEQUE_DT");
				String chequeDate = sdf.format(chequeDT);
				String accountNo = rs.getString("ACCOUNT_NO");
				String acountName = rs.getString("ACCOUNT_NAME");
				double amount = rs.getDouble("AMOUNT");
				String amountInWords = rs.getString("AMOUNT_IN_WORDS");
				String beneficiary = rs.getString("BENEFICIARY");
				String status = rs.getString("STATUS");
				String userId = rs.getString("USER_ID");
				String authourizedUser = rs.getString("AUTH_USER");
				String confirmedUser = rs.getString("CONF_USER");
				String confirmedBranch = rs.getString("CONF_BRANCH");
				String branch = rs.getString("BRANCH");
				String mandate = rs.getString("MANDATE_INSTR");
				String deleteReason = rs.getString("DELETE_REASON");

				ExcelConfirmation ExcelConfirmation = new ExcelConfirmation(id, chequeNo,
						chequeDate, accountNo, acountName, amount,
						amountInWords, beneficiary, branch, status, userId,
						authourizedUser, confirmedUser, confirmedBranch,
						mandate,deleteReason);
				records.add(ExcelConfirmation);
			}

		} catch (Exception er) {
			System.out.println("Error finding All ExcelConfirmation...->"
					+ er.getMessage());
		} finally {
			dbConnection.closeConnection(con, ps, rs);
		}

		return records;
	}
	public ArrayList findExcelConfirmationExceptionByQuery(String filter) {

		ArrayList records = new ArrayList();
		String SELECT_QUERY = "SELECT MTID,REASON,EXCEPTION_USER "
				+ "FROM CP_ExcelConfirmation_EXCEPTION " + filter;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = getConnection();
			ps = con.prepareStatement(SELECT_QUERY);

			rs = ps.executeQuery();

			while (rs.next()) {

				String id = rs.getString("MTID");
				String reason = rs.getString("REASON");
				String exceptionUser = rs.getString("EXCEPTION_USER");

	//			ConfirmationException Confirmation = new ConfirmationException(
	//					id, reason, exceptionUser);
	//			records.add(ExcelConfirmation);
			}

		} catch (Exception er) {
			System.out.println("Error finding All ExcelConfirmationException...->"
					+ er.getMessage());
		} finally {
			dbConnection.closeConnection(con, ps, rs);
		}

		return records;
	}

	// Addtional Methods added by Kalu Nsi Idika.
	// check if account numbr exist in finacle and fetch the account data
	// by Kalu Nsi Idika
	public VerifyExcelData findAccountByNo(String acctNo) {
		//boolean accountNoExist = false ;
		VerifyExcelData acctData = new VerifyExcelData();
		String query = "SELECT * FROM HD_COMPLAINT  "
			+ "WHERE complaint_id = ?";
	//	System.out.println("====acctNo== "+acctNo);
		Connection c = null; 
		ResultSet rs = null; 
		PreparedStatement ps = null;
	try {  
		c = getConnection();
		//c = getConnection2();// We will connect to finacle database here
		ps = c.prepareStatement(query);
		ps.setString(1, acctNo);
		rs = ps.executeQuery();
		while (rs.next()) {
			//acctData.setCamAccountStatus(rs.getString("CAM_ACCT_STATUS"));
			//acctData.setSmtAccountStatus(rs.getString("SMT_ACCT_STATUS"));
			acctData.setId(rs.getString("complaint_id"));
			acctData.setIssueSubject(rs.getString("request_Subject"));
			acctData.setIssueDescription(rs.getString("request_Description"));
			acctData.setAssignee(rs.getString("technician"));
			//accountNoExist = true;
		}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			 try {
	             if (c != null) {
	                 c.close();
	             }
	             if (ps != null) {
	                 ps.close();
	             }
	             if (rs != null) {
	                 rs.close();
	             }
	         } catch (Exception ex) {
	             ex.printStackTrace();
	         }
//			dbConnection.closeConnection(c, ps, rs);
		}
	//	System.out.println("====acctData== "+acctData);
		return acctData; //accountNoExist;

	}
	
//	 check if account numbr exist in finacle
	// by Kalu Nsi Idika
	
	
	
	//check if miscode exist for an account in finacle
/*	public MisCodes findMisCodeAccountByNo(String accountNo) {
		
		MisCodes acctData = new MisCodes();
		String query = "SELECT * FROM V_MIS_CODES  WHERE FORACID = ?";
		Connection c = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
	try {
		c = getConnection();
		//c = getConnection2();// We will connect to finacle database here
		ps = c.prepareStatement(query);
		ps.setString(1, accountNo);
		rs = ps.executeQuery();
		while (rs.next()) {
			acctData.setAccountName(rs.getString("ACCT_NAME"));
			acctData.setAcid(rs.getString("ACID"));
			acctData.setForAcid(rs.getString("FORACID"));
			acctData.setFreeCode5(rs.getString("FREE_CODE_5"));
		
		}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			dbConnection.closeConnection(c, ps, rs);
		}
		return acctData; 

	}
	*/
	public boolean createStopCheque(int mtid,String accountNo, String chequeNo,String chequeDate,String accountName,double amount,String stopReason,String beneficiary,String postilionCodeCharge)
    {

        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        String mtid2 = helper.getGeneratedId("CP_STOP_CHEQUE");
		mtid = Integer.parseInt(mtid2);
		
	    	
	    
        CREATE_QUERY = "INSERT INTO CP_STOP_CHEQUE(MTID,ACCOUNTNO,CHEQUENO,CHEQUEDATE,ACCOUNTNAME,AMOUNT,STOPREASON,BENEFICIARY,CHARGECODE,TRANS_DATE) VALUES(?,?,?,?,?,?,?,?,?,?)";
        con = null;
        ps = null;
        done = false;


        try
        {
        	con = getConnection();
            ps = con.prepareStatement(CREATE_QUERY);

            ps.setInt(1, mtid);
            ps.setString(2, accountNo);
			ps.setString(3, chequeNo);
	//		ps.setDate(4, dateConvert(chequeDate));
			ps.setString(5, accountName);
			ps.setDouble(6, amount);
			ps.setString(7, stopReason);
			ps.setString(8, beneficiary);
			ps.setString(9, postilionCodeCharge);
	//		ps.setDate(10, dateConvert(new java.util.Date()));

            done = ps.executeUpdate() != -1;
        }
        catch(Exception er)
        {
            System.out.println((new StringBuilder("Error creating Stop Cheque... ->")).append(er.getMessage()).toString());
            er.printStackTrace();

        }
        finally{
                                dbConnection.closeConnection(con,ps);
                        }
        return done;
    }
	
	public boolean createSmsExcept( String accountNo,String accountName)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        String mtid = helper.getGeneratedId("CP_SMS_EXCEPT");
        CREATE_QUERY = "INSERT INTO CP_SMS_EXCEPT(MTID,ACCOUNTNO,ACCOUNTNAME,CREATEDATE) VALUES(?,?,?,?)";
        con = null;
        ps = null;
        done = false;
        try
        {
        	con = getConnection();
            ps = con.prepareStatement(CREATE_QUERY);

            ps.setString(1, mtid);
            ps.setString(2, accountNo);
			ps.setString(3, accountName);
//			ps.setDate(4, dateConvert(new java.util.Date()));

            done = ps.executeUpdate() != -1;
        }
        catch(Exception er)
        {
            System.out.println((new StringBuilder("Error creating SMS EXCEPT... ->")).append(er.getMessage()).toString());
            er.printStackTrace();
        }
        finally
         {
                   dbConnection.closeConnection(con,ps);
         }
        return done;
    }
	
	public ArrayList findSmsExcept(String filter) 
	  {

		ArrayList records = new ArrayList();
		String SELECT_QUERY = "SELECT ACCOUNTNO,ACCOUNTNAME,CREATEDATE "
				+ "FROM CP_SMS_EXCEPT " + filter;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = getConnection();
			ps = con.prepareStatement(SELECT_QUERY);

			rs = ps.executeQuery();

			while (rs.next()) {

				
				String accountNo = rs.getString("ACCOUNTNO");
				String accountName = rs.getString("ACCOUNTNAME");
				String createDate = rs.getString("CREATEDATE");
//				SmsExcept sms = new SmsExcept(accountNo, accountName,createDate);
//				records.add(sms);
			}

		} catch (Exception er) {
			System.out.println("Error finding All ExcelConfirmationException...->"
					+ er.getMessage());
		} finally {
			dbConnection.closeConnection(con, ps, rs);
		}

		return records;
	}

	public boolean clearSmsExcept(String accountNo)
	{
		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "DELETE FROM CP_SMS_EXCEPT WHERE ACCOUNTNO=?";

		try {

			con = getConnection();
			ps = con.prepareStatement(query);

			ps.setString(1,accountNo);
			

			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " ERROR:Error deletting account ->" + e.getMessage());
		} finally {
			dbConnection.closeConnection(con, ps);
		}
		return done;
	}
	
	public boolean createCancelCheque(String accountNo, String chequeNo,String chequeNo2,String accountName,int mtid,String cancelReason,String otherReason)
    {

        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        String mtid2 = helper.getGeneratedId("CP_CANCEL_CHEQUE");
		mtid = Integer.parseInt(mtid2);
		
	    	
	    
        CREATE_QUERY = "INSERT INTO CP_CANCEL_CHEQUE(MTID,ACCOUNTNO,ACCOUNTNAME,BEGIN_CHEQUE,END_CHEQUE,REASON_CODE,OTHER_REASONS,TRANS_DATE ) VALUES(?,?,?,?,?,?,?,?)";
        con = null;
        ps = null;
        done = false;


        try
        {
        	con = getConnection();
            ps = con.prepareStatement(CREATE_QUERY);

            ps.setInt(1, mtid);
            ps.setString(2, accountNo);
            ps.setString(3, accountName);
			ps.setString(4, chequeNo);
			ps.setString(5, chequeNo2);
			ps.setString(6, cancelReason);
			ps.setString(7, otherReason);
//			ps.setDate(8, dateConvert(new java.util.Date()));
			

            done = ps.executeUpdate() != -1;
        }
        catch(Exception er)
        {
            System.out.println((new StringBuilder("Error creating Cancel Cheque... ->")).append(er.getMessage()).toString());
            er.printStackTrace();

        }
        finally{
                                dbConnection.closeConnection(con,ps);
                        }
        return done;
    }
	public boolean doesAccountExist(String acctNo) {
		boolean accountNoExist = false ;
		//VerifyAcctNumberData acctData = new VerifyAcctNumberData();
		String query = "SELECT FORACID FROM CHK.FINACLE_CLEARING_VIEW  "
			+ "WHERE FORACID = ?";
		Connection c = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
	try {
		//c = getSiteConnection();
		c = getConnection();
		//c = getConnection();// We will connect to finacle database here
		ps = c.prepareStatement(query);
		ps.setString(1, acctNo);
		rs = ps.executeQuery();
		if (rs.next()) {
			accountNoExist = true;
		}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			dbConnection.closeConnection(c, ps, rs);
		}
		return accountNoExist;

	}
	public boolean smsAccountExist(String acctNo) {
		boolean accountNoExist = false ;
		//VerifyAcctNumberData acctData = new VerifyAcctNumberData();
		String query = "SELECT ACCOUNTNO FROM CP_SMS_EXCEPT  "
			+ "WHERE ACCOUNTNO = ?";
		Connection c = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
	try {
		//c = getSiteConnection();
		c = getConnection();// We will connect to finacle database here
		//c = getConnection();// We will connect to finacle database here
		ps = c.prepareStatement(query);
		ps.setString(1, acctNo);
		rs = ps.executeQuery();
		if (rs.next()) {
			accountNoExist = true;
		}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			dbConnection.closeConnection(c, ps, rs);
		}
		return accountNoExist;

	}
	
	public boolean doesAccountExist2(String acctNo) {
		boolean accountNoExist = false ;
		//VerifyAcctNumberData acctData = new VerifyAcctNumberData();
		String query = "SELECT ACCOUNT_NO FROM CP_CHARGE_EXCEPTION  "
			+ "WHERE ACCOUNT_NO = ?";
		Connection c = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
	try {
		//c = getSiteConnection();
		c = getConnection();// We will connect to finacle database here
		//c = getConnection();// We will connect to finacle database here
		ps = c.prepareStatement(query);
		ps.setString(1, acctNo);
		rs = ps.executeQuery();
		
		if (rs.next()) {
			accountNoExist = true;
		}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			dbConnection.closeConnection(c, ps, rs);
		}
		return accountNoExist;

	}
	
	public String returnAcid(String acctNo) {
		String acid = "UNKNOWN" ;
		
		String query = "SELECT DISTINCT ACID FROM CBT WHERE ACID=(SELECT ACID FROM CHK.FINACLE_CLEARING_VIEW WHERE FORACID =?)";
		Connection c = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
	try {
		//c = getSiteConnection();
		c = getConnection();
		//c = getConnection();// We will connect to finacle database here
		ps = c.prepareStatement(query);
		ps.setString(1, acctNo);
		rs = ps.executeQuery();
		if (rs.next()) {
			acid = rs.getString(1);
		}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			dbConnection.closeConnection(c, ps, rs);
		}
		return acid;

	}
	
	public String clearingCheque(String accountNo,String chequeNo) {
		String acid = "UNKNOWN" ;
		
		String query = "SELECT DISTINCT ACID FROM CCMS.FINACLE_INWARD_TABLE WHERE FORACID =? AND INST_NUM=?";
		Connection c = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
	try {
		//c = getSiteConnection();
		c = getConnection();
		//c = getConnection();// We will connect to finacle database here
		ps = c.prepareStatement(query);
		ps.setString(1, accountNo);
		ps.setString(2, chequeNo);
		rs = ps.executeQuery();
		if (rs.next()) {
//			acid = rs.getString(1);
			acid="KNOWN";
		}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			dbConnection.closeConnection(c, ps, rs);
		}
		return acid;

	}
/*	
	public SMSEmailMessage findMessageEnablement() 
	{
	SMSEmailMessage message = new SMSEmailMessage();
	String query = "SELECT * FROM CP_MESSAGE ";
	Connection c = null;
	ResultSet rs = null;
	PreparedStatement ps = null;
	try {
		c = getConnection();
		ps = c.prepareStatement(query);
		rs = ps.executeQuery();
		while (rs.next()) {
			message.setEmailEnabled(rs.getString("Email_Enabled"));
			message.setSmsEnabled(rs.getString("SMS_Enabled"));
			message.setMtid(rs.getInt("mtid"));
			message.setThreshold(rs.getDouble("THRESHOLD"));
		}

	} catch (Exception e) {
			e.printStackTrace();
		}

	finally {
			dbConnection.closeConnection(c, ps, rs);
		}
	return message;

	}*/
/*	public ArrayList findApprovalEnablement(String filter) 
	{
	ArrayList list = new ArrayList();
	ApprovalTable approve = null;
	String query = "SELECT * FROM CP_APPROVE_TABLE "+filter;
	System.out.println(query);
	Connection c = null;
	ResultSet rs = null;
	PreparedStatement ps = null;
	try {
		c = getConnection();
		ps = c.prepareStatement(query);
		rs = ps.executeQuery();
		while (rs.next()) {
		String id =	rs.getString("ID");
		String pageName =	rs.getString("PAGE_NAME");
		String status = rs.getString("STATUS");
		String submitedBy =	rs.getString("SUBMITED_BY");
		String url =	rs.getString("URL");
			
			approve= new ApprovalTable(id,pageName,status,submitedBy,url);
			approve.setId(id);
			approve.setPageName(pageName);
			approve.setStatus(status);
			approve.setSubmitedBy(submitedBy);
			approve.setUrl(url);
			list.add(approve);
		}

	} catch (Exception e) {
			e.printStackTrace();
		}

	finally {
			dbConnection.closeConnection(c, ps, rs);
		}
	return list;

	} 
	public SMSEmailMessage findMessageEnablementByIdUser(int mtid) 
	{
	SMSEmailMessage message = new SMSEmailMessage();
	String query = "SELECT * FROM CP_MESSAGE_APPROVE where MTID = ?";
	Connection c = null;
	ResultSet rs = null;
	PreparedStatement ps = null;
	try {
		c = getConnection();
		ps = c.prepareStatement(query);
		ps.setInt(1, mtid);
		rs = ps.executeQuery();
		while (rs.next()) {
			message.setEmailEnabled(rs.getString("Email_Enabled"));
			message.setSmsEnabled(rs.getString("SMS_Enabled"));
			message.setMtid(rs.getInt("mtid"));
			message.setThreshold(rs.getDouble("THRESHOLD"));
		}

	} catch (Exception e) {
			e.printStackTrace();
		}

	finally {
			dbConnection.closeConnection(c, ps, rs);
		}
	return message;

	}
	public SMSEmailMessage findMessageEnablementById(int mtid) 
	{
	SMSEmailMessage message = new SMSEmailMessage();
	String query = "SELECT * FROM CP_MESSAGE where MTID = ?";
	Connection c = null;
	ResultSet rs = null;
	PreparedStatement ps = null;
	try {
		c = getConnection();
		ps = c.prepareStatement(query);
		ps.setInt(1, mtid);
		rs = ps.executeQuery();
		while (rs.next()) {
			message.setEmailEnabled(rs.getString("Email_Enabled"));
			message.setSmsEnabled(rs.getString("SMS_Enabled"));
			message.setMtid(rs.getInt("mtid"));
			message.setThreshold(rs.getDouble("THRESHOLD"));
		}

	} catch (Exception e) {
			e.printStackTrace();
		}

	finally {
			dbConnection.closeConnection(c, ps, rs);
		}
	return message;

	}
	
	public void createMessage(SMSEmailMessage message) {

	String createQuery = "INSERT INTO CP_MESSAGE (" 
		+  "SMS_Enabled,Email_Enabled,user_Id,create_dt) VALUES(?,?,?,?)";
	
	Connection con = null;
	PreparedStatement ps = null;
	try {
		con = getConnection();
		ps = con.prepareStatement(createQuery);
		
		ps.setString(1, message.getSmsEnabled());
		ps.setString(2,message.getEmailEnabled());
		ps.setString(3, message.getUserId());
		ps.setDate(4, df.dateConvert(new java.util.Date()));
		ps.execute();
	
	} catch (Exception er) {
		System.out.println("WARN:Error creating ExcelConfirmation ->"+ er.getMessage());
		setCreated(false);
	} finally {
		dbConnection.closeConnection(con, ps);
		}
	
	}
	
	public void updateMessage(SMSEmailMessage message) {

		String updateQuery = "UPDATE CP_MESSAGE SET " 
			+ "SMS_Enabled = ?,Email_Enabled = ?,user_Id = ?,create_dt = ?, Threshold = ?"
			+ " WHERE MTID = ?";
		
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(updateQuery);
			
			ps.setString(1, message.getSmsEnabled());
			ps.setString(2,message.getEmailEnabled());
			ps.setString(3, message.getUserId());
			ps.setDate(4, dateConvert(new java.util.Date()));
			ps.setDouble(5, message.getThreshold());
			ps.setInt(6, message.getMtid());
			ps.execute();
		
		} catch (Exception er) {
			System.out.println("WARN:Error creating ExcelConfirmation ->"+ er.getMessage());
			setCreated(false);
		} finally {
			dbConnection.closeConnection(con, ps);
			}
		
	}*/
	public void updateStatus(String status,String userId,String  chqStatusCode) {
		
		String updateQuery = "UPDATE CP_APPROVE_TABLE SET " 
			+ "STATUS = ? WHERE SUBMITED_BY = ? AND ID=?";
		
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(updateQuery);
			
			ps.setString(1, status);
			ps.setString(2,userId);
			ps.setString(3,chqStatusCode);
			ps.execute();
		
		} catch (Exception er) {
			System.out.println("WARN:Error updateing CP_APPROVE_TABLE ->"+ er.getMessage());
			setCreated(false);
		} finally {
			dbConnection.closeConnection(con, ps);
			}
		
	}
/*	public void updateStatus(String status,AcctErrorMessage message) {

		String updateQuery = "UPDATE CP_APPROVE_TABLE SET " 
			+ "STATUS = ? WHERE SUBMITED_BY = ? AND ID=?";
		
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(updateQuery);
			
			ps.setString(1, status);
			ps.setString(2,message.getUserId());
			ps.setString(3,message.getAcctStatusCode());
			ps.execute();
		
		} catch (Exception er) {
			System.out.println("WARN:Error updateing CP_APPROVE_TABLE ->"+ er.getMessage());
			setCreated(false);
		} finally {
			dbConnection.closeConnection(con, ps);
			}
		
	}
	public void updateStatus(String status,SMSEmailMessage message) {

		String updateQuery = "UPDATE CP_APPROVE_TABLE SET " 
			+ "STATUS = ? WHERE SUBMITED_BY = ? AND ID=? ";
		
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(updateQuery);
			
			ps.setString(1, status);
			ps.setString(2,message.getUserId());
			ps.setString(3,String.valueOf(message.getMtid()));
			ps.execute();
		
		} catch (Exception er) {
			System.out.println("WARN:Error updateing CP_APPROVE_TABLE ->"+ er.getMessage());
			setCreated(false);
		} finally {
			dbConnection.closeConnection(con, ps);
			}
		
	}
	public void updateMessageUser(SMSEmailMessage message,String status,String userId) {

		String updateQuery = "UPDATE CP_MESSAGE_APPROVE SET " 
			+ "SMS_Enabled = ?,Email_Enabled = ?,user_Id = ?,create_dt = ?, Threshold = ?,Approve=?,userId=?"
			+ " WHERE MTID = ?";
		
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(updateQuery);
			
			ps.setString(1, message.getSmsEnabled());
			ps.setString(2,message.getEmailEnabled());
			ps.setString(3, message.getUserId());
			ps.setDate(4, dateConvert(new java.util.Date()));
			ps.setDouble(5, message.getThreshold());
			ps.setString(6, status);
			ps.setString(7, userId);
			
			ps.setInt(8, message.getMtid());
			ps.execute();
		   
		} catch (Exception er) {
			System.out.println("WARN:Error creating ExcelConfirmation ->"+ er.getMessage());
			setCreated(false);
		} finally {
			dbConnection.closeConnection(con, ps);
			}
		
	}
	
	
	
	public java.util.ArrayList findcreateStopCheque() 
	{
		java.util.ArrayList _list = new java.util.ArrayList();
		com.magbel.checkpoint.vao.StopCheque sp = null;
		String query =  "SELECT * FROM CP_STOP_CHEQUE WHERE CHARGECODE != '000' OR CHARGECODE == '99'";

	Connection c = null;
	ResultSet rs = null;
	Statement s = null;

	try {
		    c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) 
			   {
				
				int mtid=rs.getInt("MTID");
				String accountNo=rs.getString("ACCOUNTNO");
				String chequeNo=rs.getString("CHEQUENO");
				String chequeDate=rs.getString("CHEQUEDATE");
				String accountName=rs.getString("ACCOUNTNAME");
				double amount=rs.getDouble("AMOUNT");
				String stopReason=rs.getString("STOPREASON");
				String beneficiary=rs.getString("BENEFICIARY");
				String postilionCodeCharge=rs.getString("CHARGECODE");
				String tranDate=rs.getString("TRANS_DATE");
				
				sp = new com.magbel.checkpoint.vao.StopCheque(mtid,accountNo,chequeNo
						,chequeDate,accountName,amount
						,stopReason, beneficiary,postilionCodeCharge
						,tranDate);
				_list.add(sp);
		        }

		} 
	 catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			dbConnection.closeConnection(c, s, rs);
		}
		return _list;
	}
	
	public java.util.ArrayList findcreateStopCheque(String accountNo,String chequeNo,String accountName,double amount,String filter) 
	{
		java.util.ArrayList _list = new java.util.ArrayList();
		com.magbel.checkpoint.vao.StopCheque sp = null;
		String query =  "SELECT * FROM CP_STOP_CHEQUE"+filter;

	Connection c = null;
	ResultSet rs = null;
	Statement s = null;

	try {
		    c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) 
			   {
				
				int mtid=rs.getInt("MTID");
				String accountNo1=rs.getString("ACCOUNTNO");
				String chequeNo1=rs.getString("CHEQUENO");
				String chequeDate=rs.getString("CHEQUEDATE");
				String accountName1=rs.getString("ACCOUNTNAME");
				double amount1=rs.getDouble("AMOUNT");
				String stopReason=rs.getString("STOPREASON");
				String beneficiary=rs.getString("BENEFICIARY");
				String postilionCodeCharge=rs.getString("CHARGECODE");
				String tranDate=rs.getString("TRANS_DATE");
				
				sp = new com.magbel.checkpoint.vao.StopCheque(mtid,accountNo1,chequeNo1
						,chequeDate,accountName1,amount1
						,stopReason, beneficiary,postilionCodeCharge
						,tranDate);
				_list.add(sp);
		        }

		} 
	 catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			dbConnection.closeConnection(c, s, rs);
		}
		return _list;
	}
	
	
	
	public java.util.ArrayList findUserCheque(String accountNo,String chequeNo,String accountName,double amount) 
	{
		java.util.ArrayList _list = new java.util.ArrayList();
		com.magbel.checkpoint.vao.StopCheque sp = null;
		String query =  "SELECT * FROM CP_STOP_CHEQUE";

	Connection c = null;
	ResultSet rs = null;
	Statement s = null;

	try {
		    c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) 
			   {
				
				int mtid=rs.getInt("MTID");
				String accountNo1=rs.getString("ACCOUNTNO");
				String chequeNo1=rs.getString("CHEQUENO");
				String chequeDate=rs.getString("CHEQUEDATE");
				String accountName1=rs.getString("ACCOUNTNAME");
				double amount1=rs.getDouble("AMOUNT");
				String stopReason=rs.getString("STOPREASON");
				String beneficiary=rs.getString("BENEFICIARY");
				String postilionCodeCharge=rs.getString("CHARGECODE");
				String tranDate=rs.getString("TRANS_DATE");
				
				sp = new com.magbel.checkpoint.vao.StopCheque(mtid,accountNo1,chequeNo1
						,chequeDate,accountName1,amount1
						,stopReason, beneficiary,postilionCodeCharge
						,tranDate);
				_list.add(sp);
		        }

		} 
	 catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			dbConnection.closeConnection(c, s, rs);
		}
		return _list;
	}
	
	
	
	
public void updateCreateStopCheque(String postilionCodeCharge,StopCheque sp) 
{
		String updateQuery = "UPDATE CP_STOP_CHEQUE SET " 
			+ "CHARGECODE = ?"
			+ " WHERE ACCOUNTNO = ? AND CHEQUENO=?";
		
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(updateQuery);		
			ps.setString(1, postilionCodeCharge);
			ps.setString(2,sp.getAccountNo());
			ps.setString(3, sp.getChequeNo());
			
			ps.execute();
		
		} 
		catch (Exception er)
		{
			System.out.println("WARN:Error update stop cheque ->"+ er.getMessage());
			setCreated(false);
		}
		finally 
		    {
			dbConnection.closeConnection(con, ps);
			}
		
	}
	
	
	
	
	
	*/
	
	
	public String createAppovalTable(String ID,String PAGE_NAME,String SUBMITED_BY,String URL,String STATUS)
	{
		String result="";
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String query ="insert into cp_approve_table values(?,?,?,?,?)";
		try{
			conn = getConnection();
			st = conn.prepareStatement(query);
			
			st.setString(1, ID);
			st.setString(2, PAGE_NAME);
			st.setString(3, SUBMITED_BY);
			st.setString(4, URL);
			st.setString(5, STATUS);
			
			st.executeUpdate();
			result="Y";
		}
		catch(Exception e){
			System.out.println("Error creating approve table");
			e.printStackTrace();
		}
		finally{
			dbConnection.closeConnection(conn,st);
			
		}
		return result;
	}
	
/*	public java.util.ArrayList findCheque() {
		java.util.ArrayList _list = new java.util.ArrayList();
		com.magbel.checkpoint.vao.Cbt cb = null;
		String query =  "SELECT BEGIN_CHQ_NUM FROM CBT";

	Connection c = null;
	ResultSet rs = null;
	Statement s = null;

	try {
		    c = getSiteConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) 
			   {
				String beginChequeNumber = rs.getString("BEGIN_CHQ_NUM");
				cb = new com.magbel.checkpoint.vao.Cbt(beginChequeNumber);
				
				cb.setBeginChqNum(beginChequeNumber);
				_list.add(cb);
		        }

		} 
	 catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			dbConnection.closeConnection(c, s, rs);
		}
		return _list;
	}
	
	*/
	public static String toMonth(int m)
	{
		String result =null;
	
	   if(m == 1)
	   {
		   result= "Jan";
	   }
	   else if(m == 2)
	   {
		   result= "Feb";
	   }
	   else if(m == 3)
	   {
	     result ="Mar";
	   }
	   else if(m == 4)
	   {
	     result ="Apr";
	   }
	   else if(m == 5)
	   {
	     result ="May";
	   }
	   else if(m ==6)
	   {
	     result = "Jun";
	   }
	   else if(m ==7)
	   {
	     result = "Jul";
	   }
	   else if(m ==8)
	   {
	     result = "Aug";
	   }
	   else if(m ==9)
	   {
	     result = "Sep";
	   }
	   else if(m ==10)
	   {
	     result = "Oct";
	   }
	   else if(m ==11)
	   {
	     result = "Nov";
	   }
	   else if(m ==12)
	   {
	     result = "Dec";
	   }
	   else
	   {
		   result ="not valid";
	   }
	 return result;
}
	
	
	public boolean findInwardChequeByQuery(String chequeno,String accountno) {

		boolean found = true;
		String SELECT_QUERY = "SELECT * FROM INWARD_CHEQUE WHERE CHEQUE_NO='"+chequeno+"' AND CUST_ACCOUNT_NO='"+accountno+"'";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = getConnection();
			ps = con.prepareStatement(SELECT_QUERY);

			rs = ps.executeQuery();

			while (rs.next()) 
			{

				found = false;
			}

		} catch (Exception er) {
			System.out.println("Error finding All inward cheque...->"
					+ er.getMessage());
		} finally {
			dbConnection.closeConnection(con, ps, rs);
		}

		return found;
	}
	
	
	
	public String createInwardCheque(String chequeNo, String instType, double amount,
			String accountNo, String chqBankBranch,
			String valueDate, String postingDate, String clearType,
			String narration, String chqBankCode,String tranCode) 
	{
		String output =null;
String CREATE_QUERY = "INSERT INTO INWARD_CHEQUE(MTID,CHEQUE_NO,INSTRUMENT_TYPE,AMOUNT,CUST_ACCOUNT_NO, " +
				    "CHQ_BANK_BRANCH, VALUE_DATE, POSTING_DATE, CLEAR_TYPE, NARRATION,CHQ_BANK_CODE,BANK_APP_DATE,TRAN_CODE) "+
				    "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection con = null;
		PreparedStatement ps = null;
		String id = helper.getGeneratedId("INWARD_CHEQUE");
	
		try {

			con = getConnection();
			//con.setAutoCommit(false);
			
			ps = con.prepareStatement(CREATE_QUERY);

			ps.setString(1, id);
			ps.setString(2, chequeNo);
			ps.setString(3, instType);
			ps.setDouble(4, amount);
			ps.setString(5, accountNo);
			ps.setString(6, chqBankBranch);
			ps.setString(7, valueDate);
			ps.setString(8, postingDate);
			ps.setString(9, clearType);
			ps.setString(10, narration);
			ps.setString(11, chqBankCode);
			ps.setString(12, postingDate);
			ps.setString(13, tranCode);
			
			int a=ps.executeUpdate();
			System.out.println("value returned>>>   "+a);
			if(a>0)
			{
				//con.setAutoCommit(true);
				//con.commit();
				output ="sucess";
			}
			else
			{
				//con.rollback();
				output ="failure";
			}
		} catch (Exception er) {
			er.printStackTrace();
			System.out.println("WARN:Error creating inward cheque ->"+ er.getMessage());
			setCreated(false);
		} finally {
			dbConnection.closeConnection(con, ps);
		}
		return output;

	}
	
	/*
	public ArrayList inwardInsert2(String filter){

		ArrayList records = new ArrayList();
		Finacle cem = new Finacle();
		                                                                                
	   String SELECT_QUERY = " SELECT INST_NUM, INST_TYPE,TRAN_CODE, " 
	   						+"INST_AMT,FORACID, ACCT_SOL_ID,VALUE_DATE,"
	   						+" ENTRY_DATE,ACCT_NAME FROM CHK.FINACLE_INWARD_TABLE" 
	   						+" WHERE TRAN_CODE NOT IN ('11','12')";

   
	    Connection con = null;
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		try{
			
			con = getConnection("finacleView");
			con.setAutoCommit(false);
			ps = con.prepareStatement(SELECT_QUERY);
            System.out.println("query ="+SELECT_QUERY);
			rs = ps.executeQuery();

			while(rs.next()){

				            String instNum = rs.getString("INST_NUM");
	                        String instType = rs.getString("INST_TYPE");
	                        double instAmt = rs.getDouble("INST_AMT");
	                        String forAcid = rs.getString("FORACID");
	                        String acctSolId = rs.getString("ACCT_SOL_ID"); 
	                        String valueDate = rs.getString("VALUE_DATE");
	                        String entryDate = rs.getString("ENTRY_DATE");
	                        String acctName = rs.getString("ACCT_NAME");
	                        String tranCode = rs.getString("TRAN_CODE");

				 cem = new Finacle();
				 cem.setAcctName(acctName);
				 cem.setAcctSolId(acctSolId);
				 cem.setEntryDate(entryDate);
				 cem.setForAcid(forAcid);
				 cem.setInstAmt(instAmt);
				 cem.setInstNum(instNum);
				 cem.setInstType(instType);
				 cem.setValueDate(valueDate);
				 cem.setTranCode(tranCode);
				 

				records.add(cem);
			}

		}catch(Exception er){
			er.printStackTrace();
			System.out.println("Error Selecting Finacle Error Message by query ...->"+er.getMessage());
		}finally{
			dbConnection.closeConnection(con,ps,rs);
		}

		return records ;
	}
	*/
	/*
	public ArrayList inwardInsert2(String zoneCode,String zoneDate,String solId)
	{
		ArrayList records = new ArrayList();
		Finacle cem = new Finacle();
		Connection c = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
        String SELECT_QUERY = " SELECT INST_NUM, INST_TYPE,TRAN_CODE, " 
	   						+"INST_AMT,FORACID, ACCT_SOL_ID,VALUE_DATE,"
	   						+" ENTRY_DATE,ACCT_NAME FROM CCMS.FINACLE_INWARD_TABLE" 
	   						+" WHERE TRAN_CODE NOT IN('11','12') AND ZONE_CODE=? AND ZONE_DATE=? AND SOL_ID=?";
	   						

		try
		{
			c = getConnection("finacleView");
			c.setAutoCommit(false);
			ps = c.prepareStatement(SELECT_QUERY);
            System.out.println("query ="+SELECT_QUERY);
            ps.setString(1, zoneCode);
            ps.setDate(2, dateConvert(zoneDate));
            ps.setString(3, solId);
			rs = ps.executeQuery();
			while(rs.next())
			{

				            String instNum = rs.getString("INST_NUM");
	                        String instType = rs.getString("INST_TYPE");
	                        double instAmt = rs.getDouble("INST_AMT");
	                        String forAcid = rs.getString("FORACID");
	                        String acctSolId = rs.getString("ACCT_SOL_ID"); 
	                        String valueDate = rs.getString("VALUE_DATE");
	                        String entryDate = rs.getString("ENTRY_DATE");
	                        String acctName = rs.getString("ACCT_NAME");
	                        String tranCode = rs.getString("TRAN_CODE");

				 cem = new Finacle();
				 cem.setAcctName(acctName);
				 cem.setAcctSolId(acctSolId);
				 cem.setEntryDate(entryDate);
				 cem.setForAcid(forAcid);
				 cem.setInstAmt(instAmt);
				 cem.setInstNum(instNum);
				 cem.setInstType(instType);
				 cem.setValueDate(valueDate);
				 cem.setTranCode(tranCode);
				 

				records.add(cem);
			}

		}catch(Exception er){
			er.printStackTrace();
			System.out.println("Error Selecting Finacle Error Message by query ...->"+er.getMessage());
		}finally{
			dbConnection.closeConnection(c,ps,rs);
		}

		return records ;
	}
	
	public ArrayList inwardInsertDirectDebit(String zoneCode,String zoneDate,String solId)
	{
		ArrayList records = new ArrayList();
		Finacle cem = new Finacle();
		Connection c = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
        String SELECT_QUERY = " SELECT INST_NUM, INST_TYPE,TRAN_CODE, " 
	   						+"INST_AMT,FORACID, ACCT_SOL_ID,VALUE_DATE,"
	   						+" ENTRY_DATE,ACCT_NAME FROM CCMS.FINACLE_INWARD_TABLE" 
	   						+" WHERE TRAN_CODE IN('11','12') AND ZONE_CODE=? AND ZONE_DATE=? AND SOL_ID=?";
	   						

		try
		{
			c = getConnection("finacleView");
			c.setAutoCommit(false);
			ps = c.prepareStatement(SELECT_QUERY);
            System.out.println("query ="+SELECT_QUERY);
            ps.setString(1, zoneCode);
            ps.setDate(2, dateConvert(zoneDate));
            ps.setString(3, solId);
			rs = ps.executeQuery();
			while(rs.next())
			{

				            String instNum = rs.getString("INST_NUM");
	                        String instType = rs.getString("INST_TYPE");
	                        double instAmt = rs.getDouble("INST_AMT");
	                        String forAcid = rs.getString("FORACID");
	                        String acctSolId = rs.getString("ACCT_SOL_ID"); 
	                        String valueDate = rs.getString("VALUE_DATE");
	                        String entryDate = rs.getString("ENTRY_DATE");
	                        String acctName = rs.getString("ACCT_NAME");
	                        String tranCode = rs.getString("TRAN_CODE");

				 cem = new Finacle();
				 cem.setAcctName(acctName);
				 cem.setAcctSolId(acctSolId);
				 cem.setEntryDate(entryDate);
				 cem.setForAcid(forAcid);
				 cem.setInstAmt(instAmt);
				 cem.setInstNum(instNum);
				 cem.setInstType(instType);
				 cem.setValueDate(valueDate);
				 cem.setTranCode(tranCode);
				 

				records.add(cem);
			}

		}catch(Exception er){
			er.printStackTrace();
			System.out.println("Error Selecting Finacle Error Message by query ...->"+er.getMessage());
		}finally{
			dbConnection.closeConnection(c,ps,rs);
		}

		return records ;
	}
	
	
	public String inwardInsert(String valueDate) {
		String accountVal = "" ;
		String id = helper.getGeneratedId("INWARD_CHEQUE");
		String query = " insert into INWARD_CHEQUE(MTID,CHEQUE_NO, INSTRUMENT_TYPE, AMOUNT, CUST_ACCOUNT_NO, CHQ_BANK_BRANCH, VALUE_DATE, POSTING_DATE, CLEAR_TYPE, NARRATION, CHQ_BANK_CODE)"
					   + " select   '"+id+"',i.INST_NUM, i.INST_TYPE, i.INST_AMT,g.foracid, i.ACCT_SOL_ID,i.VALUE_DATE,i.ENTRY_DATE,i.INST_TYPE,g.acct_name,'033'"
					   + " FROM GAM g, INW_CLG_INST_TABLE i where g.acid = i.ACID(+) AND i.VALUE_DATE=?";
		
		
		
		Connection c = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
	try {
		c = getConnection();// We will connect to finacle database here
		ps = c.prepareStatement(query);
		ps.setString(1, valueDate);
		rs = ps.executeQuery();
		if (rs.next()) {
			accountVal = rs.getString(1);
		}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			dbConnection.closeConnection(c, ps, rs);
		}
		return accountVal;

	}
	
	public String getCodeName(String query)
	{
		String result = "";
		Connection c = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try
		{
			c = getConnection("finacleView");
			ps = c.prepareStatement(query);
			rs = ps.executeQuery();
			while(rs.next())
			{
				result = rs.getString(1) == null ? "" : rs.getString(1);
			}
		}
		catch(Exception er)
		{
			System.out.println("Error in Query getCodeName()"+er);
			er.printStackTrace();
		}
		finally
		{
			dbConnection.closeConnection(c,ps);
		}
		return result;
	}
	
	public String getCodeName2(String query)
	{
		String result = "";
		Connection c = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try
		{
			c = getConnection();
			ps = c.prepareStatement(query);
			rs = ps.executeQuery();
			while(rs.next())
			{
				result = rs.getString(1) == null ? "" : rs.getString(1);
			}
		}
		catch(Exception er)
		{
			System.out.println("Error in Query getCodeName()"+er);
			er.printStackTrace();
		}
		finally
		{
			dbConnection.closeConnection(c,ps);
		}
		return result;
	}
	public ArrayList findExcelConfirmationArchiveByBranch(String solid) {

		String filter = " WHERE BRANCH = '" + solid + "' AND STATUS = 'R'";
		ArrayList records = findExcelConfirmationArchiveByQuery(filter);

		return records;
	}

	public ArrayList findExcelConfirmationArchiveByQuery(String filter) {

		ArrayList records = new ArrayList();
		String SELECT_QUERY = "SELECT MTID,CHEQUE_NO,CHEQUE_DT,ACCOUNT_NO,"
				+ "ACCOUNT_NAME,AMOUNT,AMOUNT_IN_WORDS,BENEFICIARY,"
				+ "STATUS,USER_ID,AUTH_USER,CONF_USER,CONF_BRANCH,"
				+ "BRANCH,MANDATE_INSTR,DELETE_REASON FROM CP_ExcelConfirmation " + filter;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = getConnection();
			ps = con.prepareStatement(SELECT_QUERY);

			rs = ps.executeQuery();

			while (rs.next()) {

				String id = rs.getString("MTID");
				String chequeNo = rs.getString("CHEQUE_NO");
				java.util.Date chequeDT = rs.getDate("CHEQUE_DT");
				String chequeDate = sdf.format(chequeDT);
				String accountNo = rs.getString("ACCOUNT_NO");
				String acountName = rs.getString("ACCOUNT_NAME");
				double amount = rs.getDouble("AMOUNT");
				String amountInWords = rs.getString("AMOUNT_IN_WORDS");
				String beneficiary = rs.getString("BENEFICIARY");
				String status = rs.getString("STATUS");
				String userId = rs.getString("USER_ID");
				String authourizedUser = rs.getString("AUTH_USER");
				String confirmedUser = rs.getString("CONF_USER");
				String confirmedBranch = rs.getString("CONF_BRANCH");
				String branch = rs.getString("BRANCH");
				String mandate = rs.getString("MANDATE_INSTR");
				String deleteReason = rs.getString("DELETE_REASON");

				ExcelConfirmation ExcelConfirmation = new ExcelConfirmation(id, chequeNo,
						chequeDate, accountNo, acountName, amount,
						amountInWords, beneficiary, branch, status, userId,
						authourizedUser, confirmedUser, confirmedBranch,
						mandate,deleteReason);
				records.add(ExcelConfirmation);
			}

		} catch (Exception er) {
			System.out.println("Error finding All ExcelConfirmation...->"
					+ er.getMessage());
		} finally {
			dbConnection.closeConnection(con, ps, rs);
		}

		return records;
	}
	*/
	public boolean doesCheckBookExist(String acid, String checkNumber)
	{
		boolean doesExist = false;
		int chequeNumber = Integer.parseInt(checkNumber);
		String query = "SELECT BEGIN_CHQ_NUM,CHQ_NUM_OF_LVS, ACID "
			+ "FROM CBT WHERE ACID = ? AND BEGIN_CHQ_NUM + CHQ_NUM_OF_LVS >= "+chequeNumber+" AND BEGIN_CHQ_NUM <="+chequeNumber+"";
		Connection c = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		try {
			//c = getSiteConnection();
			//c = getConnection();// Finacle Datasource Connection
			c = getConnection();
			ps = c.prepareStatement(query);
			ps.setString(1, acid);
			rs = ps.executeQuery();
			
			if (rs.next())
			{
				    
					String chequeBeginNo = rs.getString("BEGIN_CHQ_NUM");
					int numberOfChqLeaves = rs.getInt("CHQ_NUM_OF_LVS");
					int beginChqNo = Integer.parseInt(chequeBeginNo);
					if (chequeNumber >= beginChqNo && chequeNumber <= beginChqNo + numberOfChqLeaves - 1)
					{
						doesExist = true;
					}
					else{
						return doesExist;
		     			}
					
			 }
			else
			 {
				return doesExist;
			 }
		 	
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

		finally {
			dbConnection.closeConnection(c, ps, rs);
		}
		return doesExist;
	}
	/*
	public CheckBook findChqBookByAcctNumber(String acid,String checkNumber) 
	{
		CheckBook chequeBook = null;
	String query = "SELECT BEGIN_CHQ_NUM, ACID, BEGIN_CHQ_ALPHA, ENTITY_CRE_FLG, DEL_FLG, " 	
		+ "CHQ_ISSU_DATE, CHQ_NUM_OF_LVS, CHQ_LVS_STAT, CHQ_ISSU_AUTH_ID, "                 	
		+ "RCRE_USER_ID, RCRE_TIME, LCHG_USER_ID, LCHG_TIME, PRINTED_FLG, TS_CNT "
		+ "FROM CBT WHERE (BEGIN_CHQ_NUM+CHQ_NUM_OF_LVS)  >= "+checkNumber+" and BEGIN_CHQ_NUM <="+checkNumber+"  and ACID = ?";
	Connection c = null;
	ResultSet rs = null;
	PreparedStatement ps = null;
	try {
		//c = getConnection2();// Finacle Datasource Connection 
		c = getConnection("finacleView");
		//c = getSiteConnection();
		ps = c.prepareStatement(query);
		ps.setString(1, acid);
		rs = ps.executeQuery();
		while (rs.next()) {
			chequeBook = new CheckBook();
			chequeBook.setBeginChqNo(rs.getString("BEGIN_CHQ_NUM"));
			chequeBook.setAcid(rs.getString("ACID"));
			chequeBook.setBeginChqAlpha(rs.getString("BEGIN_CHQ_ALPHA"));
			chequeBook.setEntityCreateFlag(rs.getString("ENTITY_CRE_FLG"));
			chequeBook.setDeleteFlag(rs.getString("DEL_FLG"));
			java.util.Date chequeDT = rs.getDate("CHQ_ISSU_DATE");
			chequeBook.setChqIssueDate(sdf.format(chequeDT));
			chequeBook.setChqNumberOfLeaves(rs.getInt("CHQ_NUM_OF_LVS"));
			chequeBook.setChqLeavesStatus(rs.getString("CHQ_LVS_STAT"));
			chequeBook.setChqIssueAuthId(rs.getString("CHQ_ISSU_AUTH_ID"));
			chequeBook.setRcreUserId(rs.getString("RCRE_USER_ID"));
			java.util.Date rcreTime = rs.getDate("RCRE_TIME");
			chequeBook.setRcreTime(sdf.format(rcreTime));
			chequeBook.setLchgUserId(rs.getString("LCHG_USER_ID"));
			java.util.Date lchgTime = rs.getDate("LCHG_TIME");
			chequeBook.setLchgTime(sdf.format(lchgTime));
			chequeBook.setPrintedFlag(rs.getString("PRINTED_FLG"));
			chequeBook.setTsCount(rs.getInt("TS_CNT"));
		}

	} catch (Exception e) {
			e.printStackTrace();
		}

	finally {
			dbConnection.closeConnection(c, ps, rs);
		}
	return chequeBook;

	}
	public boolean isChequeNumberValidExcelConfirmation (String checkNumber,String acctNumber)
	{
		boolean isValid = false;
		
		ArrayList list = new ArrayList();
//		ExcelConfirmation ExcelConfirmation = new ExcelConfirmation();
		list =findExcelConfirmationByQuery(checkNumber,acctNumber);
//		String test = ExcelConfirmation.getAmountInWords();
//		System.out.println("test "+test);
		if(list!=null && list.size()>0)
		{
			isValid = true;
		}
        System.out.println("isValid "+isValid);
		return isValid;
	}
	
	public boolean isChequeNumberValid (String acctNumber, String checkNumber)
	{
		boolean isValid = false;
		
		int beginChqNo;
		int numberOfChqLeaves;
		String validCheck = checkNumber.trim();
		int chequeNumber = Integer.parseInt(validCheck);
		//if(doesCheckBookExist(acctNumber,checkNumber))
		//{
		try
		{
			CheckBook checkBook  = this.findChqBookByAcctNumber(acctNumber,checkNumber);
			System.out.print("ChequeBook inside isChequeNumberValid:>>>>  "+checkBook);
			
			
			if(checkBook!=null && !checkBook.equals(""))
			{
				
				String strBeginChqNo = checkBook.getBeginChqNo();
				System.out.print("Cheque Begin No inside isChequeNumberValid method:>>>> "+strBeginChqNo);
				beginChqNo = Integer.parseInt(strBeginChqNo.trim());
				numberOfChqLeaves = checkBook.getChqNumberOfLeaves();
				if (chequeNumber >= beginChqNo && chequeNumber <= beginChqNo + numberOfChqLeaves - 1)
				{
					isValid = true;
				}
			}
		//}
		}
		 catch(Exception e)
		 {
			 isValid = false; 
		 }
			System.out.println("isvalid "+isValid);
			
			
			
		return isValid;
	}
	
	public String AccountValue(String acctNo) {
		String accountVal = "" ;
		
		String query = "SELECT AMOUNT FROM CHK.FINACLE_CLEARING_VIEW  "
			+ "WHERE FORACID = ?";
		Connection c = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
	try {
		c = getConnection("finacleView");// We will connect to finacle database here
		ps = c.prepareStatement(query);
		ps.setString(1, acctNo);
		rs = ps.executeQuery();
		if (rs.next()) {
			accountVal = rs.getString(1);
		}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			dbConnection.closeConnection(c, ps, rs);
		}
		return accountVal;

	}
	public void createExcelConfirmation(String chequeNo, String chequeDate,
			String accountNo, String accountName, double amount,
			String amountInWords, String beneficiary, String mandate,
			String userId, String branch,String frequency,String startDate,String expiryDate,String radiobutton,double upper,double lower) {

		String CREATE_QUERY = "INSERT INTO CP_ExcelConfirmation( "
				+ "MTID,CHEQUE_NO,PROBABLE_DAY,ACCOUNT_NO,"
				+ "ACCOUNT_NAME,AMOUNT,AMOUNT_IN_WORDS,BENEFICIARY,"
				+ "MANDATE_INSTR,STATUS,USER_ID,BRANCH,TRAN_DT,FREQUENCY,START_DATE,EXPIRY_DATE,AMOUNT_TYPE,UPPER_AMOUNT,LOWER_AMOUNT,CHEQUE_DT) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection con = null;
		PreparedStatement ps = null;
		String id = helper.getGeneratedId("CP_ExcelConfirmation");
	
		try {

			con = getConnection();
			ps = con.prepareStatement(CREATE_QUERY);

			ps.setString(1, id);
			ps.setString(2, chequeNo);
			ps.setString(3, chequeDate);
			ps.setString(4, accountNo);

			ps.setString(5, accountName);
			ps.setDouble(6, amount);
			ps.setString(7, amountInWords);
			ps.setString(8, beneficiary);
			ps.setString(9, mandate);

			ps.setString(10, "A");

			ps.setString(11, userId);
			ps.setString(12, branch);
			ps.setDate(13, dateConvert(new java.util.Date()));
            ps.setString(14, frequency);
            ps.setDate(15, dateConvert(startDate));
            ps.setDate(16, dateConvert(expiryDate));
            ps.setString(17, radiobutton);
            ps.setDouble(18, upper);
            ps.setDouble(19, lower);
            ps.setDate(20, dateConvert(new java.util.Date()));
			ps.execute();
			setCreated(true);
		} catch (Exception er) {
			System.out.println("WARN:Error creating ExcelConfirmation ->"
					+ er.getMessage());
			setCreated(false);
		} finally {
			dbConnection.closeConnection(con, ps);
		}

	} */
	public void createExcelConfirmation(String chequeNo, String chequeDate,
			String accountNo, String accountName, double amount,
			String amountInWords, String beneficiary, String mandate,
			String userId, String branch,String frequency,String startDate,String expiryDate) {

		String CREATE_QUERY = "INSERT INTO CP_ExcelConfirmation( "
				+ "MTID,CHEQUE_NO,CHEQUE_DT,ACCOUNT_NO,"
				+ "ACCOUNT_NAME,AMOUNT,AMOUNT_IN_WORDS,BENEFICIARY,"
				+ "MANDATE_INSTR,STATUS,USER_ID,BRANCH,TRAN_DT,FREQUENCY,START_DATE,EXPIRY_DATE) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection con = null;
		PreparedStatement ps = null;
		String id = helper.getGeneratedId("CP_ExcelConfirmation");
	
		try {

			con = getConnection();
			ps = con.prepareStatement(CREATE_QUERY);

			ps.setString(1, id);
			ps.setString(2, chequeNo);
	//		ps.setDate(3, dateConvert(chequeDate));
			ps.setString(4, accountNo);

			ps.setString(5, accountName);
			ps.setDouble(6, amount);
			ps.setString(7, amountInWords);
			ps.setString(8, beneficiary);
			ps.setString(9, mandate);

			ps.setString(10, "A");

			ps.setString(11, userId);
			ps.setString(12, branch);
//			ps.setDate(13, dateConvert(new java.util.Date()));
            ps.setString(14, frequency);
 //           ps.setDate(15, dateConvert(startDate));
  //          ps.setDate(16, dateConvert(expiryDate));
			ps.execute();

		} catch (Exception er) {
			System.out.println("WARN:Error creating ExcelConfirmation ->"
					+ er.getMessage());
			setCreated(false);
		} finally {
			dbConnection.closeConnection(con, ps);
		}

	}
	public void createExcelConfirmation(String chequeNo, String chequeDate, String accountNo, String accountName,double amount,String  amountInWords, String beneficiary, String mandate, String userId, String branch) 
	{

		String CREATE_QUERY = "INSERT INTO CP_ExcelConfirmation( "
				+ "MTID,CHEQUE_NO,CHEQUE_DT,ACCOUNT_NO,"
				+ "ACCOUNT_NAME,AMOUNT,AMOUNT_IN_WORDS,BENEFICIARY,"
				+ "MANDATE_INSTR,STATUS,USER_ID,BRANCH,TRAN_DT) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection con = null;
		PreparedStatement ps = null;
		String id = helper.getGeneratedId("CP_ExcelConfirmation");
	
		try {

			con = getConnection();
			ps = con.prepareStatement(CREATE_QUERY);

			ps.setString(1, id);
			ps.setString(2, chequeNo);
//			ps.setDate(3, dateConvert(chequeDate));
			ps.setString(4, accountNo);

			ps.setString(5, accountName);
			ps.setDouble(6, amount);
			ps.setString(7, amountInWords);
			ps.setString(8, beneficiary);
			ps.setString(9, mandate);

			ps.setString(10, "A");

			ps.setString(11, userId);
			ps.setString(12, branch);
//			ps.setDate(13, dateConvert(new java.util.Date()));
         
			ps.execute();

		} catch (Exception er) {
			System.out.println("WARN:Error creating ExcelConfirmation ->"
					+ er.getMessage());
			setCreated(false);
		} finally {
			dbConnection.closeConnection(con, ps);
		}

	}
/*	
	public void updateExcelConfirmation(String id, String accountName,
			double amount, String amountInWords, String beneficiary,
			String mandate) {

		String query = "UPDATE CP_ExcelConfirmation SET ACCOUNT_NAME = '"
				+ accountName + "'," + "AMOUNT = " + amount
				+ ",AMOUNT_IN_WORDS = '" +amountInWords + "',"
				+ "BENEFICIARY = '" + beneficiary + "',MANDATE_INSTR = '"
				+ mandate + "' " + "WHERE MTID = '" + id + "'";
		executeQuery(query);
	}

	
	public String getChequeLeaveStatus(String acctNumber, String checkNumber)
	{
		char [] arrChequeStatus;
		int beginChqNo;
		CheckBook checkBook = null;
		String strChqLeaveStatus = "";
		System.out.println("Account Number >>>>: "+acctNumber);
		System.out.println("Check Number >>>>: "+checkNumber);
		int chequeNumber = Integer.parseInt(checkNumber);
		checkBook = this.findChqBookByAcctNumber(acctNumber,checkNumber);
		if (checkBook != null)
		{
			String strBeginChqNo = checkBook.getBeginChqNo();
			System.out.println("Begin Check Number >>>>: "+strBeginChqNo.trim());
			beginChqNo = Integer.parseInt(strBeginChqNo.trim());
			arrChequeStatus = checkBook.getChqLeavesStatus().toCharArray();
			char chqLeaveStatus = arrChequeStatus[chequeNumber - beginChqNo];
			strChqLeaveStatus = String.valueOf(chqLeaveStatus);
		}
		System.out.println("Check Status >>>>: "+strChqLeaveStatus);
		return strChqLeaveStatus;
	}
	//return desc cheque
	public String descCheque(String test) {
		String desc = "UNKNOWN" ;
		System.out.println("Test value   "+test);
		String query = "SELECT ERROR_DESC FROM CP_CHQ_STATUS_ERROR_MESG WHERE STATUS_CODE='"+test+"'";
		Connection c = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
	try {
		//c = getSiteConnection();
		c = getConnection();
		
		ps = c.prepareStatement(query);
		ps.setString(1, test);
		rs = ps.executeQuery();
		if (rs.next()) {
			desc = rs.getString(1);
		}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			dbConnection.closeConnection(c, ps, rs);
		}
		return desc;

	}
	// 
	public String ChequeLeaveCStatus(String accountNo, String checkNumber)
	{
		String acid = returnAcid(accountNo);
		String value = null;
		char [] arrChequeStatus;
		int beginChqNo;
		System.out.println("Account Number >>>>: "+acid);
		System.out.println("Check Number >>>>: "+checkNumber);
		int chequeNumber = Integer.parseInt(checkNumber);
		CheckBook checkBook = new CheckBook();
		checkBook = this.findChqBookByAcctNumber(acid,checkNumber);
		if(checkBook == null)
		{
			value = "A";
		}
		else{
		
		String strBeginChqNo = checkBook.getBeginChqNo();
		System.out.println("Begin Check Number >>>>: "+strBeginChqNo);
		beginChqNo = Integer.parseInt(strBeginChqNo);
		System.out.println("Checked "+beginChqNo);
		arrChequeStatus = checkBook.getChqLeavesStatus().toCharArray();
		
		String strChqLeaveStatusNew = String.valueOf(arrChequeStatus);
		
		int x =chequeNumber - beginChqNo;
		
		String test = strChqLeaveStatusNew.substring(x,x+1);
		
        value=test;
        
		}
		System.out.println("<<<<<<<"+value);
           return value;
	}
	// Update Cheque Leave Status
	public String ChequeLeaveStatus(String acid, String checkNumber, String operation)
	{
		String value = null;
		char [] arrChequeStatus;
		int beginChqNo;
		System.out.println("Account Number >>>>: "+acid);
		System.out.println("Check Number >>>>: "+checkNumber);
		int chequeNumber = Integer.parseInt(checkNumber);
		CheckBook checkBook = new CheckBook();
		checkBook = this.findChqBookByAcctNumber(acid,checkNumber);
		String strBeginChqNo = checkBook.getBeginChqNo();
		System.out.println("Begin Check Number >>>>: "+strBeginChqNo);
		beginChqNo = Integer.parseInt(strBeginChqNo);
		arrChequeStatus = checkBook.getChqLeavesStatus().toCharArray();
		String strChqLeaveStatusNew = String.valueOf(arrChequeStatus);
		int x =chequeNumber - beginChqNo;
	
		String test = strChqLeaveStatusNew.substring(x,x+1);
        value=test;
		return value;
	}
	public String updateChequeLeaveStatus(String acid, String checkNumber, String operation)
	{
		String value = null;
		char [] arrChequeStatus;
		int beginChqNo;
		System.out.println("Account Number >>>>: "+acid);
		System.out.println("Check Number >>>>: "+checkNumber);
		int chequeNumber = Integer.parseInt(checkNumber);
		CheckBook checkBook = new CheckBook();
		checkBook = this.findChqBookByAcctNumber(acid,checkNumber);
		String strBeginChqNo = checkBook.getBeginChqNo();
		System.out.println("Begin Check Number >>>>: "+strBeginChqNo);
		beginChqNo = Integer.parseInt(strBeginChqNo);
		arrChequeStatus = checkBook.getChqLeavesStatus().toCharArray();
		String strChqLeaveStatusNew = String.valueOf(arrChequeStatus);
		int x =chequeNumber - beginChqNo;
	
		String test = strChqLeaveStatusNew.substring(x,x+1);
		
		System.out.println("test ."+test);
	if   (test.equals("U") )
	{
		if (operation.equalsIgnoreCase("cancel"))
		{
			arrChequeStatus[chequeNumber - beginChqNo] = 'X';
			value = "cancel";
		}
		else if (operation.equalsIgnoreCase("stop"))
		{
			arrChequeStatus[chequeNumber - beginChqNo] = 'S';
			value = "stop";
		}
	}
	else
	{
		String descChequeError = getCodeName2("SELECT ERROR_DESC FROM CP_CHQ_STATUS_ERROR_MESG WHERE STATUS_CODE='"+test+"'");
		System.out.println(descChequeError);
		value = test;
	}
		String strChqLeaveStatus = String.valueOf(arrChequeStatus);
		System.out.println("Check Status >>>>: "+strChqLeaveStatus);
		
		
		String updateQuery = "UPDATE CBT SET " 
			+ "CHQ_LVS_STAT = ? WHERE ACID = ? AND BEGIN_CHQ_NUM = ?";
		
		Connection con = null;
		PreparedStatement ps = null;
		try {
			//con = getConnection2(); // Get Connection from Finacle data source 
			con = getConnection("finacleView");
			//con = getSiteConnection();
			ps = con.prepareStatement(updateQuery);
			
			ps.setString(1, strChqLeaveStatus);
			ps.setString(2,acid);
			ps.setString(3,strBeginChqNo);
			ps.execute();
		
		} catch (Exception er) {
			System.out.println("WARN:Error creating ExcelConfirmation ->"+ er.getMessage());
			setCreated(false);
		} finally {
			dbConnection.closeConnection(con, ps);
			}
		return value;
	}
	
	public String updateChequeLeaveStatus2(String acid, String checkNumber, String checkNumber2, String operation)
	{
		String value = null;
		char [] arrChequeStatus;
		int beginChqNo;
		System.out.println("Account Number >>>>: "+acid);
		System.out.println("Check Number >>>>: "+checkNumber);
		int chequeNumber = Integer.parseInt(checkNumber);
		
		int chequeNumber2 = Integer.parseInt(checkNumber2);
		
		CheckBook checkBook = new CheckBook();
		checkBook = this.findChqBookByAcctNumber(acid,checkNumber);
		String strBeginChqNo = checkBook.getBeginChqNo();
		System.out.println("Begin Check Number >>>>: "+strBeginChqNo);
		beginChqNo = Integer.parseInt(strBeginChqNo);
		arrChequeStatus = checkBook.getChqLeavesStatus().toCharArray();
		String strChqLeaveStatusNew = String.valueOf(arrChequeStatus);
		
		int x = chequeNumber - beginChqNo;
	    System.out.println("X value "+x);
	    
		String test = strChqLeaveStatusNew.substring(x,x+1);
		
	
	if   (test.equals("U") )
	{
		if (operation.equalsIgnoreCase("cancel"))
		{
			int n = (chequeNumber2 - chequeNumber);
			n = n + 1;
			for (int i=0;i<n;i++)
			{
				int nextChqNo = chequeNumber + i;
				arrChequeStatus[nextChqNo-beginChqNo]='X';
			}
			value="cancel";
			/*
			value = "cancel";
			//int n = chequeNumber2 - chequeNumber;
			int n = (chequeNumber2 - chequeNumber) + 1;
			for (int i=1;i<=n;i++)
			{
				int nextChqNo = beginChqNo + i;
				System.out.println("chequeNumber value "+nextChqNo);
			    arrChequeStatus[chequeNumber - nextChqNo] = 'X';
			    //arrChequeStatus[chequeNumber - beginChqNo] = 'S';
			}
			
		}
		else if (operation.equalsIgnoreCase("stop"))
		{		
			arrChequeStatus[chequeNumber - beginChqNo] = 'S';
			value="stop";
		}
	}
	else
	{
		
		String descChequeError = getCodeName2("SELECT ERROR_DESC FROM CP_CHQ_STATUS_ERROR_MESG WHERE STATUS_CODE='"+test+"'");
		System.out.println(descChequeError);
		value = test;
	}
		String strChqLeaveStatus = String.valueOf(arrChequeStatus);
		System.out.println("Check Status >>>>: "+strChqLeaveStatus);
		
		
		String updateQuery = "UPDATE CBT SET " 
			+ "CHQ_LVS_STAT = ? WHERE ACID = ? AND BEGIN_CHQ_NUM = ?";
		
		Connection con = null;
		PreparedStatement ps = null;
		try {
			//con = getConnection2(); // Get Connection from Finacle data source 
			con = getConnection("finacleView");
			//con = getSiteConnection();
			ps = con.prepareStatement(updateQuery);
			
			ps.setString(1, strChqLeaveStatus);
			ps.setString(2,acid);
			ps.setString(3,strBeginChqNo);
			ps.execute();
		
		} catch (Exception er) {
			System.out.println("WARN:Error creating ExcelConfirmation ->"+ er.getMessage());
			setCreated(false);
		} finally {
			dbConnection.closeConnection(con, ps);
			}
		return value;
	}
	public void createAcctStatusErrorMessage(AcctErrorMessage data) {

		String createQuery = "INSERT INTO CP_ACCT_STATUS_ERROR_MESG (" 
			+  "STATUS_CODE,ERROR_DESC,CREATE_DT,USER_ID) VALUES(?,?,?,?)";
		
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(createQuery);
			
			ps.setString(1, data.getAcctStatusCode());
			ps.setString(2,data.getErrorDescription());
			ps.setDate(3, dateConvert(new java.util.Date()));
			ps.setString(4, data.getUserId());
			ps.execute();
			System.out.print("Just create new arror msg");
		
		} catch (Exception er) {
			System.out.println("WARN:Error creating ExcelConfirmation ->"+ er.getMessage());
			setCreated(false);
		} finally {
			dbConnection.closeConnection(con, ps);
			}
	}
	public void createAcctStatusErrorMessageTemp(AcctErrorMessage data) {

		String createQuery = "INSERT INTO CP_ACCT_STATUS_ERROR_MESG_TEMP (" 
			+  "STATUS_CODE,ERROR_DESC,CREATE_DT,USER_ID) VALUES(?,?,?,?)";
		
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(createQuery);
			
			ps.setString(1, data.getAcctStatusCode());
			ps.setString(2,data.getErrorDescription());
			ps.setDate(3, dateConvert(new java.util.Date()));
			ps.setString(4, data.getUserId());
			ps.execute();
			System.out.print("Just create new arror msg");
		
		} catch (Exception er) {
			System.out.println("WARN:Error creating ExcelConfirmation ->"+ er.getMessage());
			setCreated(false);
		} finally {
			dbConnection.closeConnection(con, ps);
			}
	}
	
	public void updateAcctStatusErrorMessage(AcctErrorMessage data) {

		String updateQuery = "UPDATE CP_ACCT_STATUS_ERROR_MESG SET " 
			+  "ERROR_DESC = ? ,CREATE_DT = ? ,USER_ID = ? "
			+ " WHERE STATUS_CODE = ?";
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(updateQuery);
			
			ps.setString(1, data.getErrorDescription());
			ps.setDate(2,dateConvert(new java.util.Date()));
			ps.setString(3, data.getUserId());
			ps.setString(4, data.getAcctStatusCode());
			ps.execute();
			System.out.print("Just modified acct error msg");
		
		} catch (Exception er) {
			System.out.println("WARN:Error creating ExcelConfirmation ->"+ er.getMessage());
		} finally {
			dbConnection.closeConnection(con, ps);
			}
		
	}
	
	public void updateAcctStatusErrorMessageUser(AcctErrorMessage data,String status,String userId) {

		String updateQuery = "UPDATE CP_ACC_STATUS_ERR_MSG_APPROVE SET " 
			+  "ERROR_DESC = ? ,CREATE_DT = ? ,USER_ID = ? ,Approve=?,userId=?"
			+ " WHERE STATUS_CODE = ?";
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(updateQuery);
			
			ps.setString(1, data.getErrorDescription());
			ps.setDate(2,dateConvert(new java.util.Date()));
			ps.setString(3, data.getUserId());
			ps.setString(4, status);
			ps.setString(5, userId);
			ps.setString(6, data.getAcctStatusCode());
			ps.execute();
			System.out.print("Just modified acct error msg");
		
		} catch (Exception er) {
			System.out.println("WARN:Error updating CP_ACC_STATUS_ERR_MSG_APPROVE ->"+ er.getMessage());
			setCreated(false);
		} finally {
			dbConnection.closeConnection(con, ps);
			}
		
	}
	
	public boolean doesAcctStatusExist(AcctErrorMessage data) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet res = null;
		boolean result = false;

		try {

			con = getConnection();
			final String query = "select STATUS_CODE from CP_ACCT_STATUS_ERROR_MESG where STATUS_CODE = ?";
			ps = con.prepareStatement(query);

			ps.setString(1, data.getAcctStatusCode());
			res = ps.executeQuery();
			if (res.next())
				result = true;

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			dbConnection.closeConnection(con, ps);
		}
		return result;
	}
	
	public AcctErrorMessage findAcctStatusErrorMessageByCode(String acctStatusCode) 
	{
		AcctErrorMessage acctErrorMessage = new AcctErrorMessage();
	String query = "SELECT STATUS_CODE,ERROR_DESC,CREATE_DT,USER_ID FROM CP_ACCT_STATUS_ERROR_MESG WHERE STATUS_CODE = ?";
	Connection c = null;
	ResultSet rs = null;
	PreparedStatement ps = null;
	try {
		c = getConnection();
		ps = c.prepareStatement(query);
		ps.setString(1, acctStatusCode);
		rs = ps.executeQuery();
		while (rs.next()) {
			acctErrorMessage.setAcctStatusCode(rs.getString("STATUS_CODE"));
			acctErrorMessage.setErrorDescription(rs.getString("ERROR_DESC"));
			java.util.Date createDate = rs.getDate("CREATE_DT");
			acctErrorMessage.setCreateDate(sdf.format(createDate));
			acctErrorMessage.setUserId(rs.getString("USER_ID"));
		
		}

	} catch (Exception e) {
			e.printStackTrace();
		}

	finally {
			dbConnection.closeConnection(c, ps, rs);
		}
	return acctErrorMessage;
	}
	
	public void createChequeStatusErrorMessage(ChequeErrorMessage data) {

		String createQuery = "INSERT INTO CP_CHQ_STATUS_ERROR_MESG (" 
			+  "STATUS_CODE,ERROR_DESC,CREATE_DT,USER_ID) VALUES(?,?,?,?)";
		
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(createQuery);
			
			ps.setString(1, data.getChqStatusCode());
			ps.setString(2,data.getErrorDescription());
			ps.setDate(3, df.dateConvert(new java.util.Date()));
			ps.setString(4, data.getUserId());
			ps.execute();
		
		} catch (Exception er) {
			System.out.println("WARN:Error creating ExcelConfirmation ->"+ er.getMessage());
			setCreated(false);
		} finally {
			dbConnection.closeConnection(con, ps);
			}
	}
	
	public void updateChequeStatusErrorMessage(ChequeErrorMessage data) {
		
		String updateQuery = "UPDATE CP_CHQ_STATUS_ERROR_MESG SET " 
			+  "ERROR_DESC = ? ,CREATE_DT = ? ,USER_ID = ? "
			+ " WHERE STATUS_CODE = ?";
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(updateQuery);
			
			ps.setString(1, data.getErrorDescription());
			ps.setDate(2,df.dateConvert(new java.util.Date()));
			ps.setString(3, data.getUserId());
			ps.setString(4, data.getChqStatusCode());
			System.out.println("welcome");
			ps.execute();
		
		} catch (Exception er) {
			System.out.println("WARN:Error creating ExcelConfirmation ->"+ er.getMessage());
		} finally {
			dbConnection.closeConnection(con, ps);
			}
		
	}
	//================
	public boolean createChequeStatusErrorMessage2Insert(String chqStatusCode,String errorDescription,String loginID) 
    {

        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
     
		
	    	
	    
        CREATE_QUERY = "INSERT INTO CP_CHQ_STATUS_ERROR_MESG_TEMP (STATUS_CODE,ERROR_DESC,CREATE_DT,USER_ID) VALUES(?,?,?,?)";
        con = null;
        ps = null;
        done = false;


        try
        {
            con = getConnection();
            ps = con.prepareStatement(CREATE_QUERY);

            ps.setString(1, chqStatusCode);
            ps.setString(2, errorDescription);
			ps.setDate(3, dateConvert(new java.util.Date()));
			ps.setString(4, loginID);
			
            done = ps.executeUpdate() != -1;
        }
        catch(Exception er)
        {
            System.out.println((new StringBuilder("WARN:Error creating ExcelConfirmation ->")).append(er.getMessage()).toString());
            er.printStackTrace();
            setCreated(false);
        }
        finally{
                                dbConnection.closeConnection(con,ps);
                        }
        return done;
    }
	public boolean createChequeStatusErrorMessage2(String chqStatusCode,String errorDescription,String loginID) 
    {

        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
     
		
	    	
	    
        CREATE_QUERY = "INSERT INTO CP_CHQ_STATUS_ERROR_MESG (STATUS_CODE,ERROR_DESC,CREATE_DT,USER_ID) VALUES(?,?,?,?)";
        con = null;
        ps = null;
        done = false;


        try
        {
            con = getConnection();
            ps = con.prepareStatement(CREATE_QUERY);

            ps.setString(1, chqStatusCode);
            ps.setString(2, errorDescription);
			ps.setDate(3, dateConvert(new java.util.Date()));
			ps.setString(4, loginID);
			
            done = ps.executeUpdate() != -1;
        }
        catch(Exception er)
        {
            System.out.println((new StringBuilder("WARN:Error creating ExcelConfirmation ->")).append(er.getMessage()).toString());
            er.printStackTrace();
            setCreated(false);
        }
        finally{
                                dbConnection.closeConnection(con,ps);
                        }
        return done;
    }
	

	public boolean updateChequeStatusErrorMessage2(String chqStatusCode,String errorDescription,String loginID) 
	{
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        CREATE_QUERY = "UPDATE CP_CHQ_STATUS_ERROR_MESG SET " 
			+  "ERROR_DESC = ?  ,CREATE_DT = ?,USER_ID = ? "
			+ " WHERE STATUS_CODE = ?";
        con = null;
        ps = null;
        done = false;
        try
        {
            con = getConnection();
            ps = con.prepareStatement(CREATE_QUERY);
           
            ps.setString(1, errorDescription);
             ps.setDate(2,  dateConvert(new java.util.Date()));
            ps.setString(3, loginID);
			ps.setString(4, chqStatusCode);
            done = ps.executeUpdate() != -1;
        }
        catch(Exception er)
        {
            System.out.println((new StringBuilder("WARN:Error updating ExcelConfirmation ... ->")).append(er.getMessage()).toString());
            er.printStackTrace();
            
        }
        finally{
                                dbConnection.closeConnection(con,ps);
                        }
        return done;
    }
	public boolean updateChequeStatusErrorMessage2User(String chqStatusCode,String errorDescription,String loginID,String status,String userId) 
	{
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        CREATE_QUERY = "UPDATE CP_CHQ_STATUS_ERR_MSG_APPROVE SET " 
			+  "ERROR_DESC = ?  ,CREATE_DT = ?,USER_ID = ? ,Approve=?,userId=?"
			+ " WHERE STATUS_CODE = ?";
        con = null;
        ps = null;
        done = false;
        try
        {
            con = getConnection();
            ps = con.prepareStatement(CREATE_QUERY);
           
            ps.setString(1, errorDescription);
             ps.setDate(2,  dateConvert(new java.util.Date()));
            ps.setString(3, loginID);
            ps.setString(4, status);
			ps.setString(5, userId);
			ps.setString(6, chqStatusCode);
            done = ps.executeUpdate() != -1;
        }
        catch(Exception er)
        {
            System.out.println((new StringBuilder("WARN:Error updating ExcelConfirmation ... ->")).append(er.getMessage()).toString());
            er.printStackTrace();
            
        }
        finally{
                                dbConnection.closeConnection(con,ps);
                        }
        return done;
    }
	//==================================
	public boolean doesChequeStatusExist(ChequeErrorMessage data) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet res = null;
		boolean result = false;

		try {

			con = getConnection();
			final String query = "select STATUS_CODE from CP_CHQ_STATUS_ERROR_MESG where STATUS_CODE = ?";
			ps = con.prepareStatement(query);

			ps.setString(1, data.getChqStatusCode());
			res = ps.executeQuery();
			if (res.next())
				result = true;

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			dbConnection.closeConnection(con, ps);
		}
		return result;
	}
	
	public ChequeErrorMessage findChequeStatusErrorMessageByCode(String chequeStatusCode) 
	{
		ChequeErrorMessage chqErrorMessage = new ChequeErrorMessage();
	String query = "SELECT STATUS_CODE,ERROR_DESC,CREATE_DT,USER_ID FROM CP_CHQ_STATUS_ERROR_MESG WHERE STATUS_CODE = ?";
	Connection c = null;
	ResultSet rs = null;
	PreparedStatement ps = null;
	try {
		c = getConnection();
		ps = c.prepareStatement(query);
		ps.setString(1, chequeStatusCode);
		rs = ps.executeQuery();
		while (rs.next()) {
			chqErrorMessage.setChqStatusCode(rs.getString("STATUS_CODE"));
			chqErrorMessage.setErrorDescription(rs.getString("ERROR_DESC"));
			java.util.Date createDate = rs.getDate("CREATE_DT");
			chqErrorMessage.setCreateDate(sdf.format(createDate));
			chqErrorMessage.setUserId(rs.getString("USER_ID"));
		
		}

	} catch (Exception e) {
			e.printStackTrace();
		}

	finally {
			dbConnection.closeConnection(c, ps, rs);
		}
	return chqErrorMessage;
	}
	
	public boolean isPostDatedCheque(String strChequeDate)
	{
		boolean isPostDated = false;
		System.out.println("Date from page is::"+strChequeDate);
		Date chequeDate = dateConvert(strChequeDate);
		String strPageDate = sdf.format(chequeDate);
		System.out.println("Date got after conversion: "+strPageDate);
		Date sysdate = dateConvert(new Date());
		if (chequeDate.after(sysdate))
		{
			isPostDated = true;
		}
		return isPostDated;
	}
	
	public boolean isStaleCheque(String strChequeDate)
	{
		 boolean isStalled = false; 
		 Calendar currentCalendar = Calendar.getInstance(); //new GregorianCalendar(pdt);
		 Date current = dateConvert(new Date());
		 Date chequeDate = dateConvert(strChequeDate);
		 currentCalendar.setTime(current);
		 //print out a bunch of interesting things
		 System.out.println("currentCalendar YEAR: " + currentCalendar.get(Calendar.YEAR));
		 System.out.println("currentCalendar MONTH: " + currentCalendar.get(Calendar.MONTH));
		 System.out.println("currentCalendar DATE: " + currentCalendar.get(Calendar.DATE));
		 Calendar chequeCalendar = Calendar.getInstance();
		 chequeCalendar.setTime(chequeDate);
		 System.out.println("chequeCalendar YEAR: " + chequeCalendar.get(Calendar.YEAR));
		 System.out.println("chequeCalendar MONTH: " + chequeCalendar.get(Calendar.MONTH));
		 System.out.println("chequeCalendar DATE: " + chequeCalendar.get(Calendar.DATE));
		 chequeCalendar.add(Calendar.MONTH, 6);
		 System.out.println("chequeCalendar YEAR after adding 6 months: " + chequeCalendar.get(Calendar.YEAR));
		 System.out.println("chequeCalendar MONTH after adding 6 months: " + chequeCalendar.get(Calendar.MONTH));
		 System.out.println("chequeCalendar DATE after adding 6 months: " + chequeCalendar.get(Calendar.DATE));
		 System.out.println("currentCalendar "+currentCalendar);
		 System.out.println("chequeCalendar "+chequeCalendar);
		 if (currentCalendar.before(chequeCalendar))
		 {
			 isStalled = true;
		 }
		 return isStalled;
	}
	
	public boolean doesContactExist(String acctNumber)
	{
		boolean contactExist = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet res = null;
	
		try {

			con = getConnection();
			final String query = "select ACCT_NO from CP_CONTACT where ACCT_NO = ?";
			ps = con.prepareStatement(query);

			ps.setString(1, acctNumber);
			res = ps.executeQuery();
			if (res.next())
				contactExist = true;

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			dbConnection.closeConnection(con, ps);
		}
		return contactExist;
	}
	
	public String findContactEmailByAcctNo(String acctNumber)
	{
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet res = null;
		String email = "";
	
		try {

			con = getConnection();
			final String query = "select EMAIL,ACCT_NO from CP_CONTACT where ACCT_NO = ?";
			ps = con.prepareStatement(query);

			ps.setString(1, acctNumber);
			res = ps.executeQuery();
			while(res.next())
			{
				email = res.getString("EMAIL");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			dbConnection.closeConnection(con, ps);
		}
		return email;
	}
	
//	account error message processes 
	//by Terseer Anthony Shaguy
	public ArrayList findAllAcctErrorMessage(){
		String filter = "";
		return findAcctErrorMessageByQuery(filter);
	}

	public AcctErrorMessage findAcctErrorMessageById(String acctStatusCode){
		String filter = " AND STATUS_CODE = '"+acctStatusCode+"'";
		ArrayList records = findAcctErrorMessageByQuery(filter);
		return (records.size() > 0)?(AcctErrorMessage)records.get(0):null;
	}
	public AcctErrorMessage findAcctErrorMessageByIdInsert(String acctStatusCode){
		String filter = " AND STATUS_CODE = '"+acctStatusCode+"'";
		ArrayList records = findAcctErrorMessageByQueryInsert(filter);
		return (records.size() > 0)?(AcctErrorMessage)records.get(0):null;
	}
	public AcctErrorMessage findAcctErrorMessageByIdUser(String acctStatusCode){
		String filter = " AND STATUS_CODE = '"+acctStatusCode+"'";
		ArrayList records = findAcctErrorMessageByQueryUser(filter);
		return (records.size() > 0)?(AcctErrorMessage)records.get(0):null;
	}
   public AcctErrorMessage findAccountErrorMsgByAcctStatusCode(String acctStatusCode){
	   AcctErrorMessage aem = null;
	   String filter = " AND STATUS_CODE = '"+acctStatusCode+"'";
	   ArrayList list = findAcctErrorMessageByQuery(filter);
	   if(list.size() > 0){
		  aem = (AcctErrorMessage)list.get(0);
	   }

	   return aem;
   }

   public boolean isOrgBusExisting2(String acctStatusCode) {

		boolean exists = false;
		String filter = " WHERE STATUS_CODE = '"+acctStatusCode+"' ";

		ArrayList records = findAcctErrorMessageByQuery(filter);
		if (records.size() > 0) {
			exists = true;
		}
		return exists;
	}
   public ArrayList findAcctErrorMessageByQueryUser(String filter){

		ArrayList records = new ArrayList();
		AcctErrorMessage aem = new AcctErrorMessage();
		//String SELECT_QUERY = "SELECT mtid,bus_type_code,description,status,empl_id,create_date "+
		//					  "FROM ORG_BUS_TYPE  WHERE mtid IS NOT NULL "+filter+" ORDER BY bus_type_code";
          
	         String SELECT_QUERY = " SELECT STATUS_CODE,ERROR_DESC,CREATE_DT,USER_ID FROM CP_ACC_STATUS_ERR_MSG_APPROVE "
 		                         + " WHERE STATUS_CODE IS NOT NULL "+filter+" ORDER BY STATUS_CODE";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try{

			con = getConnection();
			ps = con.prepareStatement(SELECT_QUERY);

			rs = ps.executeQuery();

			while(rs.next()){

				String acctStatusCode = rs.getString("STATUS_CODE");
	                        String errorDescription = rs.getString("ERROR_DESC");
	                        String createDate = rs.getString("CREATE_DT");
	                        String userId = rs.getString("USER_ID");  

				 aem = new AcctErrorMessage();
				 aem.setAcctStatusCode(acctStatusCode);
				 aem.setErrorDescription(errorDescription);
				 aem.setCreateDate(createDate);
				 aem.setUserId(userId);
				 

				records.add(aem);
			}

		}catch(Exception er){
			System.out.println("Error Selecting Account Error Message by query ...->"+er.getMessage());
		}finally{
			dbConnection.closeConnection(con,ps,rs);
		}

		return records ;
	}
   
   public ArrayList findAcctErrorMessageByQueryInsert(String filter){

		ArrayList records = new ArrayList();
		AcctErrorMessage aem = new AcctErrorMessage();
		//String SELECT_QUERY = "SELECT mtid,bus_type_code,description,status,empl_id,create_date "+
		//					  "FROM ORG_BUS_TYPE  WHERE mtid IS NOT NULL "+filter+" ORDER BY bus_type_code";
          
	         String SELECT_QUERY = " SELECT STATUS_CODE,ERROR_DESC,CREATE_DT,USER_ID FROM CP_ACCT_STATUS_ERROR_MESG_TEMP "
 		                         + " WHERE STATUS_CODE IS NOT NULL "+filter+" ORDER BY STATUS_CODE";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try{

			con = getConnection();
			ps = con.prepareStatement(SELECT_QUERY);

			rs = ps.executeQuery();

			while(rs.next()){

				String acctStatusCode = rs.getString("STATUS_CODE");
	                        String errorDescription = rs.getString("ERROR_DESC");
	                        String createDate = rs.getString("CREATE_DT");
	                        String userId = rs.getString("USER_ID");  

				 aem = new AcctErrorMessage();
				 aem.setAcctStatusCode(acctStatusCode);
				 aem.setErrorDescription(errorDescription);
				 aem.setCreateDate(createDate);
				 aem.setUserId(userId);
				 

				records.add(aem);
			}

		}catch(Exception er){
			System.out.println("Error Selecting Account Error Message by query ...->"+er.getMessage());
		}finally{
			dbConnection.closeConnection(con,ps,rs);
		}

		return records ;
	}
   public ArrayList findAcctErrorMessageByQuery(String filter){

		ArrayList records = new ArrayList();
		AcctErrorMessage aem = new AcctErrorMessage();
		//String SELECT_QUERY = "SELECT mtid,bus_type_code,description,status,empl_id,create_date "+
		//					  "FROM ORG_BUS_TYPE  WHERE mtid IS NOT NULL "+filter+" ORDER BY bus_type_code";
           
	         String SELECT_QUERY = " SELECT STATUS_CODE,ERROR_DESC,CREATE_DT,USER_ID FROM CP_ACCT_STATUS_ERROR_MESG "
  		                         + " WHERE STATUS_CODE IS NOT NULL "+filter+" ORDER BY STATUS_CODE";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try{

			con = getConnection();
			ps = con.prepareStatement(SELECT_QUERY);

			rs = ps.executeQuery();

			while(rs.next()){

				String acctStatusCode = rs.getString("STATUS_CODE");
	                        String errorDescription = rs.getString("ERROR_DESC");
	                        String createDate = rs.getString("CREATE_DT");
	                        String userId = rs.getString("USER_ID");  

				 aem = new AcctErrorMessage();
				 aem.setAcctStatusCode(acctStatusCode);
				 aem.setErrorDescription(errorDescription);
				 aem.setCreateDate(createDate);
				 aem.setUserId(userId);
				 

				records.add(aem);
			}

		}catch(Exception er){
			System.out.println("Error Selecting Account Error Message by query ...->"+er.getMessage());
		}finally{
			dbConnection.closeConnection(con,ps,rs);
		}

		return records ;
	}
	
	
	
	//cheque error message processes 
	//by Terseer Anthony Shaguy
	
	public ArrayList findAllChqErrorMessage(){
		String filter = "";
		return findChqErrorMessageByQuery(filter);
	}

	public ChequeErrorMessage findChqErrorMessageById(String chqStatusCode){
		String filter = " AND STATUS_CODE = '"+chqStatusCode+"'";
		ArrayList records = findChqErrorMessageByQuery(filter);
		return (records.size() > 0)?(ChequeErrorMessage)records.get(0):null;
	}
	public ChequeErrorMessage findChqErrorMessageByIdInsert(String chqStatusCode){
		String filter = " AND STATUS_CODE = '"+chqStatusCode+"'";
		ArrayList records = findChqErrorMessageByQueryInsert(filter);
		return (records.size() > 0)?(ChequeErrorMessage)records.get(0):null;
	}
	public ChequeErrorMessage findChqErrorMessageByIdUser(String chqStatusCode){
		String filter = " AND STATUS_CODE = '"+chqStatusCode+"'";
		ArrayList records = findChqErrorMessageByQueryUser(filter);
		return (records.size() > 0)?(ChequeErrorMessage)records.get(0):null;
	}
	 public ChequeErrorMessage findChqErrorMsgByAcctStatusCode(String acctStatusCode){
		   ChequeErrorMessage cem = null;
		   String filter = " AND STATUS_CODE = '"+acctStatusCode+"'";
		   ArrayList list = findChequeErrorMessageByQuery(filter);
		   if(list.size() > 0){
			  cem = (ChequeErrorMessage)list.get(0);
		   }

		   return cem;
	   }
   public boolean isChqStatusExisting2(String chqStatusCode) {

		boolean exists = false;
		String filter = " WHERE STATUS_CODE = '"+chqStatusCode+"' ";

		ArrayList records = findChqErrorMessageByQuery(filter);
		if (records.size() > 0) {
			exists = true;
		}
		return exists;
	}

   public void updateExcelConfirmationDirect(String recordno, String status) {

		String updateQuery = "UPDATE CP_ExcelConfirmation SET STATUS = ? WHERE CHEQUE_NO = ? ";
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(updateQuery);
			
			ps.setString(1, status);
			ps.setString(2,recordno);
			
			ps.execute();
			System.out.print("Updating ExcelConfirmation");
		
		} catch (Exception er) {
			System.out.println("WARN:Error creating ExcelConfirmation ->"+ er.getMessage());
		} finally {
			dbConnection.closeConnection(con, ps);
			}
		
	}
   public ArrayList findChequeErrorMessageByQuery(String filter){

		ArrayList records = new ArrayList();
		ChequeErrorMessage aem = new ChequeErrorMessage();
		//String SELECT_QUERY = "SELECT mtid,bus_type_code,description,status,empl_id,create_date "+
		//					  "FROM ORG_BUS_TYPE  WHERE mtid IS NOT NULL "+filter+" ORDER BY bus_type_code";
         
	         String SELECT_QUERY = " SELECT STATUS_CODE,ERROR_DESC,CREATE_DT,USER_ID FROM CP_CHQ_STATUS_ERROR_MESG "
		                         + " WHERE STATUS_CODE IS NOT NULL "+filter+" ORDER BY STATUS_CODE";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try{

			con = getConnection();
			ps = con.prepareStatement(SELECT_QUERY);

			rs = ps.executeQuery();

			while(rs.next()){

				String acctStatusCode = rs.getString("STATUS_CODE");
	                        String errorDescription = rs.getString("ERROR_DESC");
	                        String createDate = rs.getString("CREATE_DT");
	                        String userId = rs.getString("USER_ID");  

				 aem = new ChequeErrorMessage();
				 aem.setChqStatusCode(acctStatusCode);
				 aem.setErrorDescription(errorDescription);
				 aem.setCreateDate(createDate);
				 aem.setUserId(userId);
				 

				records.add(aem);
			}

		}catch(Exception er){
			System.out.println("Error Selecting Account Error Message by query ...->"+er.getMessage());
		}finally{
			dbConnection.closeConnection(con,ps,rs);
		}

		return records ;
	}
   public ArrayList findChqErrorMessageByQueryUser(String filter){

		ArrayList records = new ArrayList();
		ChequeErrorMessage cem = new ChequeErrorMessage();
		                                                                                
	         String SELECT_QUERY = " SELECT STATUS_CODE,ERROR_DESC,CREATE_DT,USER_ID FROM CP_CHQ_STATUS_ERR_MSG_APPROVE "
 		                         + " WHERE STATUS_CODE IS NOT NULL "+filter+" ORDER BY STATUS_CODE";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try{

			con = getConnection();
			ps = con.prepareStatement(SELECT_QUERY);

			rs = ps.executeQuery();

			while(rs.next()){

				String chqStatusCode = rs.getString("STATUS_CODE");
	                        String errorDescription = rs.getString("ERROR_DESC");
	                        String createDate = rs.getString("CREATE_DT");
	                        String userId = rs.getString("USER_ID");  

				 cem = new ChequeErrorMessage();
				 cem.setChqStatusCode(chqStatusCode);
				 cem.setErrorDescription(errorDescription);
				 cem.setCreateDate(createDate);
				 cem.setUserId(userId);
				 

				records.add(cem);
			}

		}catch(Exception er){
			System.out.println("Error Selecting Cheque Error Message by query ...->"+er.getMessage());
		}finally{
			dbConnection.closeConnection(con,ps,rs);
		}

		return records ;
	}
   public ArrayList findChqErrorMessageByQueryInsert(String filter){

		ArrayList records = new ArrayList();
		ChequeErrorMessage cem = new ChequeErrorMessage();
		                                                                                
	         String SELECT_QUERY = " SELECT STATUS_CODE,ERROR_DESC,CREATE_DT,USER_ID FROM CP_CHQ_STATUS_ERROR_MESG_TEMP "
 		                         + " WHERE STATUS_CODE IS NOT NULL "+filter+" ORDER BY STATUS_CODE";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try{

			con = getConnection();
			ps = con.prepareStatement(SELECT_QUERY);

			rs = ps.executeQuery();

			while(rs.next()){

				String chqStatusCode = rs.getString("STATUS_CODE");
	                        String errorDescription = rs.getString("ERROR_DESC");
	                        String createDate = rs.getString("CREATE_DT");
	                        String userId = rs.getString("USER_ID");  

				 cem = new ChequeErrorMessage();
				 cem.setChqStatusCode(chqStatusCode);
				 cem.setErrorDescription(errorDescription);
				 cem.setCreateDate(createDate);
				 cem.setUserId(userId);
				 

				records.add(cem);
			}

		}catch(Exception er){
			System.out.println("Error Selecting Cheque Error Message by query ...->"+er.getMessage());
		}finally{
			dbConnection.closeConnection(con,ps,rs);
		}

		return records ;
	}
   public ArrayList findChqErrorMessageByQuery(String filter){

		ArrayList records = new ArrayList();
		ChequeErrorMessage cem = new ChequeErrorMessage();
		                                                                                
	         String SELECT_QUERY = " SELECT STATUS_CODE,ERROR_DESC,CREATE_DT,USER_ID FROM CP_CHQ_STATUS_ERROR_MESG "
  		                         + " WHERE STATUS_CODE IS NOT NULL "+filter+" ORDER BY STATUS_CODE";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try{

			con = getConnection();
			ps = con.prepareStatement(SELECT_QUERY);

			rs = ps.executeQuery();

			while(rs.next()){

				String chqStatusCode = rs.getString("STATUS_CODE");
	                        String errorDescription = rs.getString("ERROR_DESC");
	                        String createDate = rs.getString("CREATE_DT");
	                        String userId = rs.getString("USER_ID");  

				 cem = new ChequeErrorMessage();
				 cem.setChqStatusCode(chqStatusCode);
				 cem.setErrorDescription(errorDescription);
				 cem.setCreateDate(createDate);
				 cem.setUserId(userId);
				 

				records.add(cem);
			}

		}catch(Exception er){
			System.out.println("Error Selecting Cheque Error Message by query ...->"+er.getMessage());
		}finally{
			dbConnection.closeConnection(con,ps,rs);
		}

		return records ;
	}
   public void verifyInwardCheques()
   {
	   
	   
   }
 //To check cp_cheque_exception
   public String checkException(String query) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
        String accountNo ="";
		try {

			con = getConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				accountNo = rs.getString(accountNo);
		    }

		} catch (Exception ee) {
			System.out.println("Cannot Fetch Value..." + ee);
		} finally {
			dbConnection.closeConnection(con,ps,rs);
		}

		return accountNo;
	}
  
   
   public void checkChequeCharges(String accountNo,String glAccount, int chargeAmount,String chargeCode)
   {
	Connection c = null;
	ResultSet rs = null;
	Statement s = null;
    String query = "SELECT CHARGE_CODE,CHARGE_AMOUNT,GL_ACCOUNT FROM CP_CHEQUE_CHARGES WHERE GL_ACCOUNT = '"+glAccount+"'";
	try 
	{
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
     
 //    executeQuery(query);
     String isEmployeeExist = this.checkException("SELECT ACCOUNT_NO FROM CP_CHARGE_EXCEPTION WHERE ACCOUNT_NO='"+accountNo+"'");
      if((isEmployeeExist!=null)&&(!isEmployeeExist.equals("")))
         {
           //call iso
         }
     }
   catch (Exception e) 
   {
		System.out.println("ERROR:Error Selecting accounts ->"
				+ e.getMessage());
	} 
   finally {
		dbConnection.closeConnection(c, s, rs);
	}

	return ;
}
   
   //2
    public ArrayList findChequeCharge()
    {
    	ArrayList iaList;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        String query;
        iaList = new ArrayList();
        
        ChequeCharge impacc = null;
        con = null;
        ps = null;
        rs = null;
        query = "SELECT CHARGE_CODE,CHARGE_AMOUNT,GL_ACCOUNT,PROCESS_TYPE,STATUS,CHARGE_TYPE , PERCENTAGE  FROM CP_CHEQUE_CHARGES WHERE PROCESS_TYPE='S' AND STATUS='A' ";
   try
        {
            con = getConnection();
            ps = con.prepareStatement(query);
         
            for(rs = ps.executeQuery(); rs.next(); iaList.add(impacc))
            {
			
				String exceptionCode = rs.getString("CHARGE_CODE");
				double amount = rs.getInt("CHARGE_AMOUNT");
				String glAccount = rs.getString("GL_ACCOUNT");
				String processType = rs.getString("PROCESS_TYPE");
				String status = rs.getString("STATUS");
				String chargeType = rs.getString("CHARGE_TYPE");
				double percentage = rs.getDouble("PERCENTAGE");
				
					
                impacc = new ChequeCharge(exceptionCode,amount,glAccount,processType, status,chargeType,percentage);
            }

        }
        catch(Exception ex)
        {
            System.out.println((new StringBuilder("ERROR fetching Charges>>>>>>>>>>>>>>>>> ")).append(ex.getMessage()).toString());
            ex.printStackTrace();
            
        }
        finally{
                                dbConnection.closeConnection(con,ps);
                        }
        return iaList;
    }
    public ArrayList findChequeCharge2()
    {
    	ArrayList iaList;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        String query;
        iaList = new ArrayList();
        
        ChequeCharge2 impacc = null;
        con = null;
        ps = null;
        rs = null;
        query = "SELECT CHARGE_CODE,CHARGE_AMOUNT,GL_ACCOUNT,VT_ACCOUNT,PROCESS_TYPE,STATUS,CHARGE_TYPE , PERCENTAGE  FROM CP_CHEQUE_CHARGES WHERE PROCESS_TYPE='S' AND STATUS='A' ";
   try
        {
            con = getConnection();
            ps = con.prepareStatement(query);
         
            for(rs = ps.executeQuery(); rs.next(); iaList.add(impacc))
            {
			
				String exceptionCode = rs.getString("CHARGE_CODE");
				double amount = rs.getInt("CHARGE_AMOUNT");
				String glAccount = rs.getString("GL_ACCOUNT");
				String vtAccount = rs.getString("VT_ACCOUNT");
				String processType = rs.getString("PROCESS_TYPE");
				String status = rs.getString("STATUS");
				String chargeType = rs.getString("CHARGE_TYPE");
				double percentage = rs.getDouble("PERCENTAGE");
				
					
                impacc = new ChequeCharge2(exceptionCode,amount,glAccount,vtAccount,processType, status,chargeType,percentage);
            }

        }
        catch(Exception ex)
        {
            System.out.println((new StringBuilder("ERROR fetching Charges>>>>>>>>>>>>>>>>> ")).append(ex.getMessage()).toString());
            ex.printStackTrace();
            
        }
        finally{
                                dbConnection.closeConnection(con,ps);
                        }
        return iaList;
    }
	private ArrayList findExceptionCode(String filter)
    {
        ArrayList iaList;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        String query;
        iaList = new ArrayList();
        ChargeException impacc = null;
        con = null;
        ps = null;
        rs = null;
       
        query = "SELECT  EXCEPT_CODE,ACCOUNT_NO,STATUS FROM CP_CHARGE_EXCEPTION" ;		
		query = (new StringBuilder(String.valueOf(query))).append(filter).toString();
        try
        {
            con = getConnection();
            ps = con.prepareStatement(query);
            //Imprest impacc;
            for(rs = ps.executeQuery(); rs.next(); iaList.add(impacc))
            {
			
				String exceptionCode = rs.getString("EXCEPT_CODE");
				String accountNo = rs.getString("ACCOUNT_NO");
				String status = rs.getString("STATUS");
				
				
					
                impacc = new ChargeException(exceptionCode,accountNo,status);
            }

        }
        catch(Exception ex)
        {
            System.out.println((new StringBuilder("ERROR fetching Exception ")).append(ex.getMessage()).toString());
            ex.printStackTrace();
            
        }
        finally{
                                dbConnection.closeConnection(con,ps);
                        }
        return iaList;
    }
	
	public ChargeException findException(String accountNo)
	   {
		ChargeException tabs = null;
		   String filter = " WHERE ACCOUNT_NO = '"+accountNo+"'";
		   ArrayList list = findExceptionCode(filter);
		   if(list.size() > 0)
		   {
			  tabs = (ChargeException)list.get(0);
		   }

		   return tabs;
	   }
	public String createInwardCheque(String chequeNo, String instType, double amount,
			String accountNo, String chqBankBranch,
			String valueDate, String postingDate, String clearType,
			String narration, String chqBankCode,String tranCode,String zoneCode,String solId) 
	{
		String output =null;
String CREATE_QUERY = "INSERT INTO INWARD_CHEQUE(MTID,CHEQUE_NO,INSTRUMENT_TYPE,AMOUNT,CUST_ACCOUNT_NO, " +
				    "CHQ_BANK_BRANCH, VALUE_DATE, POSTING_DATE, CLEAR_TYPE, NARRATION,CHQ_BANK_CODE,BANK_APP_DATE,TRAN_CODE,ZONE_CODE,SOL_ID) "+
				    "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection con = null;
		PreparedStatement ps = null;
		String id = helper.getGeneratedId("INWARD_CHEQUE");
	
		try {

			con = getConnection();
			//con.setAutoCommit(false);
			
			ps = con.prepareStatement(CREATE_QUERY);

			ps.setString(1, id);
			ps.setString(2, chequeNo);
			ps.setString(3, instType);
			ps.setDouble(4, amount);
			ps.setString(5, accountNo);
			ps.setString(6, chqBankBranch);
			ps.setString(7, valueDate);
			ps.setString(8, postingDate);
			ps.setString(9, clearType);
			ps.setString(10, narration);
			ps.setString(11, chqBankCode);
			ps.setString(12, postingDate);
			ps.setString(13, tranCode);
			ps.setString(14, zoneCode);
			ps.setString(15, solId);
			 
			
			int a=ps.executeUpdate();
			System.out.println("value returned>>>   "+a);
			if(a>0)
			{
				//con.setAutoCommit(true);
				//con.commit();
				output ="sucess";
			}
			else
			{
				//con.rollback();
				output ="failure";
			}
		} catch (Exception er) {
			er.printStackTrace();
			System.out.println("WARN:Error creating inward cheque ->"+ er.getMessage());
			setCreated(false);
		} finally {
			dbConnection.closeConnection(con, ps);
		}
		return output;

	}
	public String findControlAcctNumber(String acctNumber,String controlNumber) 
	{
		String deleteFlag = "U";
	String query = "SELECT DELETE_FLG FROM Chqcontrl WHERE CONTROL_NUMBER  = "+controlNumber+" AND ACCOUNT_NUMBER = ?";
	Connection c = null;
	ResultSet rs = null;
	PreparedStatement ps = null;
	try {
		c = getConnection("finacleView");// Finacle Datasource Connection 
		ps = c.prepareStatement(query);
		ps.setString(1, acctNumber);
		rs = ps.executeQuery();
		while (rs.next()) {
			 
			deleteFlag=rs.getString("DELETE_FLG");
			 
		}

	} catch (Exception e) {
			e.printStackTrace();
		}

	finally {
			dbConnection.closeConnection(c, ps, rs);
		}
	return deleteFlag;

	} 
	public String findControlAcctNumber(String acctNumber,String chequeNumber,String controlNumber)  
	{
		String deleteFlag = "U";
	String query = "SELECT DELETE_FLG FROM Chqcontrl WHERE CONTROL_NUMBER  = "+controlNumber+" AND INSTRUMENT_NUMBER="+chequeNumber+" AND ACCOUNT_NUMBER = ?";
	Connection c = null;
	ResultSet rs = null;
	PreparedStatement ps = null;
	try {
		c = getConnection("finacleView");// Finacle Datasource Connection 
		ps = c.prepareStatement(query);
		ps.setString(1, acctNumber);
		rs = ps.executeQuery();
		while (rs.next()) {
			 
			deleteFlag=rs.getString("DELETE_FLG");
			 
		}

	} catch (Exception e) {
			e.printStackTrace();
		}

	finally {
			dbConnection.closeConnection(c, ps, rs);
		}
	return deleteFlag;

	} 
	public void createExcelConfirmation(String chequeNo, String chequeDate,
			String accountNo, String accountName, double amount,
			String amountInWords, String beneficiary, String mandate,
			String userId, String branch,String scannedimage,String controlNumber) {

		String CREATE_QUERY = "INSERT INTO CP_ExcelConfirmation( "
				+ "MTID,CHEQUE_NO,CHEQUE_DT,ACCOUNT_NO,"
				+ "ACCOUNT_NAME,AMOUNT,AMOUNT_IN_WORDS,BENEFICIARY,"
				+ "MANDATE_INSTR,STATUS,USER_ID,BRANCH,TRAN_DT,SCANNEDIMAGE,CONTROL_NUMBER) "
		+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection con = null;
		PreparedStatement ps = null;
		String id = helper.getGeneratedId("CP_ExcelConfirmation");

		try {

			con = getConnection();
			ps = con.prepareStatement(CREATE_QUERY);

			ps.setString(1, id);
			ps.setString(2, chequeNo);
			ps.setDate(3, dateConvert(chequeDate));
			ps.setString(4, accountNo);

			ps.setString(5, accountName);
			ps.setDouble(6, amount);
			ps.setString(7, amountInWords);
			ps.setString(8, beneficiary);
			ps.setString(9, mandate);

			ps.setString(10, "A");

			ps.setString(11, userId);
			ps.setString(12, branch);
			ps.setDate(13, dateConvert(new java.util.Date()));
			ps.setString(14, scannedimage);
			ps.setString(15, controlNumber);
			ps.execute();

		} catch (Exception er) {
			System.out.println("WARN:Error creating ExcelConfirmation ->"
					+ er.getMessage());
			setCreated(false);
		} finally {
			dbConnection.closeConnection(con, ps);
		}

	}
	
	public void createExcelConfirmationControl(String chequeNo, String chequeDate,
			String accountNo, String accountName,double amount,
			String  amountInWords, String beneficiary, String mandate,
			String userId, String branch,String controlNumber) 
	{

		String CREATE_QUERY = "INSERT INTO CP_ExcelConfirmation( "
				+ "MTID,CHEQUE_NO,CHEQUE_DT,ACCOUNT_NO,"
				+ "ACCOUNT_NAME,AMOUNT,AMOUNT_IN_WORDS,BENEFICIARY,"
				+ "MANDATE_INSTR,STATUS,USER_ID,BRANCH,TRAN_DT,CONTROL_NUMBER) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection con = null;
		PreparedStatement ps = null;
		String id = helper.getGeneratedId("CP_ExcelConfirmation");
	
		try {

			con = getConnection();
			ps = con.prepareStatement(CREATE_QUERY);

			ps.setString(1, id);
			ps.setString(2, chequeNo);
			ps.setDate(3, dateConvert(chequeDate));
			ps.setString(4, accountNo);

			ps.setString(5, accountName);
			ps.setDouble(6, amount);
			ps.setString(7, amountInWords);
			ps.setString(8, beneficiary);
			ps.setString(9, mandate);

			ps.setString(10, "A");

			ps.setString(11, userId);
			ps.setString(12, branch);
			ps.setDate(13, dateConvert(new java.util.Date()));
			ps.setString(14, controlNumber);
			ps.execute();

		} catch (Exception er) {
			System.out.println("WARN:Error creating ExcelConfirmation ->"
					+ er.getMessage());
			setCreated(false);
		} finally {
			dbConnection.closeConnection(con, ps);
		}

	}
	public void createExcelConfirmationApprove(String chequeNo, String chequeDate,
			String accountNo, String accountName, double amount,
			String amountInWords, String beneficiary, String mandate,
			String userId, String branch,String scannedimage,String scanimage,String phone,String bom,String controlNumber	
	) {

		String CREATE_QUERY = "INSERT INTO CP_ExcelConfirmation_APPROVE( "
				+ "MTID,CHEQUE_NO,CHEQUE_DT,ACCOUNT_NO,"
				+ "ACCOUNT_NAME,AMOUNT,AMOUNT_IN_WORDS,BENEFICIARY,"
				+ "MANDATE_INSTR,STATUS,USER_ID,BRANCH,TRAN_DT,SCANNEDIMAGE,SCANIMAGE,PHONE,BOM,CONTROL_NUMBER) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection con = null;
		PreparedStatement ps = null;
		String id = helper.getGeneratedId("CP_ExcelConfirmation");

		try {

			con = getConnection();
			ps = con.prepareStatement(CREATE_QUERY);

			ps.setString(1, id);
			ps.setString(2, chequeNo);
			ps.setDate(3, dateConvert(chequeDate));
			ps.setString(4, accountNo);

			ps.setString(5, accountName);
			ps.setDouble(6, amount);
			ps.setString(7, amountInWords);
			ps.setString(8, beneficiary);
			ps.setString(9, mandate);

			ps.setString(10, "A");

			ps.setString(11, userId);
			ps.setString(12, branch);
			ps.setDate(13, dateConvert(new java.util.Date()));
			ps.setString(14, scannedimage);
			ps.setString(15, scanimage);
			ps.setString(16, phone);
			ps.setString(17, bom);
			ps.setString(18, controlNumber);
			ps.execute();

		} catch (Exception er) {
			System.out.println("WARN:Error creating ExcelConfirmation ->"
					+ er.getMessage());
			setCreated(false);
		} finally {
			dbConnection.closeConnection(con, ps);
		}

	}
	
	public String findChqBookNumber(String acid,String checkNumber) 
	{
		String output="";
	String query = "SELECT BEGIN_CHQ_NUM, CHQ_NUM_OF_LVS  and ACID = ?";
	Connection c = null;
	ResultSet rs = null;
	PreparedStatement ps = null;
	try {
		//c = getConnection2();// Finacle Datasource Connection 
		c = getConnection("finacleView");
		//c = getSiteConnection();
		ps = c.prepareStatement(query);
		ps.setString(1, acid);
		rs = ps.executeQuery();
		while (rs.next()) {
			 
			output=rs.getString("BEGIN_CHQ_NUM") + "--" + rs.getInt("CHQ_NUM_OF_LVS");
			 
		}

	} catch (Exception e) {
			e.printStackTrace();
		}

	finally {
			dbConnection.closeConnection(c, ps, rs);
		}
	return output;

	}
	   public String accountNoCbn(String accountNo) 
	   {

			Connection con = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
	        String  accountNoCbn ="";
			try 
			{

				con = getConnection("finacleView");
				
				ps = con.prepareStatement("select acct_num from oldact where old_acct_num =?");
			
				rs = ps.executeQuery();
				ps.setString(1, accountNo);
				if (rs.next()) 
				{
					 accountNoCbn = rs.getString("acct_num");
			    }
				else accountNoCbn=accountNo;

			} 
			catch (Exception ee) 
			{
				System.out.println("Cannot Fetch Value..." + ee);
			} 
			finally 
			{
				dbConnection.closeConnection(con,ps,rs);
			}

			return accountNoCbn;
		}
	   */
}