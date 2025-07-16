

package com.magbel.admin.handlers;
 
import com.magbel.util.ApplicationHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import com.magbel.util.DataConnect;
import java.sql.SQLException;

/**
 *
 * @author Developer - Ganiyu
 */
public class CompanyHandler {

    Connection con = null;

	Statement stmt = null;

	PreparedStatement ps = null;

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
		//System.out.println("USING_ " + this.getClass().getName());
	}



	public com.magbel.admin.objects.Company getCompany() {
		com.magbel.admin.objects.Company company = null;
		String query = "SELECT Company_Code, Company_Name, Acronym, Company_Address"
				
				+ ", Minimum_Psw, Psw_Expiry, Session_Timeout"
				
				+ ",User_Id, Trans_Wait_Time,loguseraudit "
				+ ", Psw_upper,Psw_lower,NumericPass,SpecPassword,Pass_Limit,attempted_login FROM AM_GB_COMPANY";

		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String companyCode = rs.getString("Company_Code");
				String companyName = rs.getString("Company_Name");
				
				String acronym = rs.getString("Acronym");
				
				String companyAddress = rs.getString("Company_Address");
							
				int minimumPassword = rs.getInt("Minimum_Psw");
				int passwordExpiry = rs.getInt("Psw_Expiry");
				int sessionTimeout = rs.getInt("Session_Timeout");
				
				String userId = rs.getString("User_Id");
				  
				String logUserAudit = rs.getString("loguseraudit");

				double transWaitTime = rs.getDouble("Trans_Wait_Time");
				String password_upper = rs.getString("Psw_upper");
				String password_lower = rs.getString("Psw_lower");
				String password_numeric = rs.getString("NumericPass");
				String password_special = rs.getString("SpecPassword");
                int password_limit = rs.getInt("Pass_Limit");
                int attempt_logon = rs.getInt("attempted_login");
				//to do give a condition for federal or state and use the value for whtRate

				company = new com.magbel.admin.objects.Company();
                                company.setCompanyName(companyName);
                                company.setCompanyCode(companyCode);
                                company.setAcronym(acronym);
                                company.setCompanyAddress(companyAddress);
                               company.setMinimumPassword(minimumPassword) ;
                               company.setPasswordExpiry(passwordExpiry);
                               company.setSessionTimeout(sessionTimeout);
				company.setUserId(userId);
                                company.setTransWaitTime(transWaitTime);
				
				company.setLogUserAudit(logUserAudit);
                                company.setPassword_lower(password_lower);
				company.setPassword_numeric(password_numeric);
				company.setPassword_special(password_special);
				company.setPassword_upper(password_upper);
                company.setPasswordLimit(password_limit);
                company.setLog_on(attempt_logon);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return company;
	}


 	private Connection getConnection() {
		Connection con = null;
		dc = new DataConnect("helpDesk");
		try {
			con = dc.getConnection();
		} catch (Exception e) {
			System.out.println("WARNING: Error getting connection ->"
					+ e.getMessage());
		}
		return con;
	}

	private void closeConnection(Connection con, Statement s) {
		try {
			if (s != null) {
				s.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (Exception e) {
			System.out.println("WARNING: Error getting connection ->"
					+ e.getMessage());
		}

	}

	private void closeConnection(Connection con, PreparedStatement ps) {
		try {
			if (ps != null) {
				ps.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (Exception e) {
			System.out.println("WARNING: Error closing connection ->"
					+ e.getMessage());
		}

	}

	/**
	 *
	 * @param con
	 *            Connection
	 * @param s
	 *            Statement
	 * @param rs
	 *            ResultSet
	 */
	private void closeConnection(Connection con, Statement s, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (s != null) {
				s.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (Exception e) {
			System.out.println("WARNING: Error closing connection ->"
					+ e.getMessage());
		}
	}

	/**
	 *
	 * @param con
	 *            Connection
	 * @param ps
	 *            PreparedStatement
	 * @param rs
	 *            ResultSet
	 */
	private void closeConnection(Connection con, PreparedStatement ps,
			ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (Exception e) {
			System.out.println("WARNING: Error closing connection ->"
					+ e.getMessage());
		}
	}
public boolean createCompany(com.magbel.admin.objects.Company company) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "INSERT INTO AM_GB_COMPANY(Company_Code, Company_Name, Acronym, Company_Address"
				+ ", Minimum_Psw, Psw_Expiry, Session_Timeout"
				+ ", User_Id,loguseraudit,Attempt_Logon,Pass_Limit" +
                                ", Psw_upper,Psw_lower,NumericPass,SpecPassword)"
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, company.getCompanyCode());
			ps.setString(2, company.getCompanyName());
			ps.setString(3, company.getAcronym());
			ps.setString(4, company.getCompanyAddress());
			ps.setInt(5, company.getMinimumPassword());
			ps.setInt(6, company.getPasswordExpiry());
			ps.setInt(7, company.getSessionTimeout());
			ps.setString(8, company.getUserId());
                        ps.setString(9, company.getLogUserAudit());
			ps.setInt(10,company.getLog_on());
			ps.setInt(11, company.getPasswordLimit());
                        ps.setString(12,company.getPassword_upper());
                        ps.setString(13,company.getPassword_lower());
                        ps.setString(14,company.getPassword_numeric());
                        ps.setString(15,company.getPassword_special());

			done = (ps.executeUpdate() != -1);
			//System.out.println("=========================checking if success"+done);

		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

public boolean updateCompany(com.magbel.admin.objects.Company company) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "UPDATE AM_GB_COMPANY"
				+ " SET Company_Name = ?, Acronym = ?, Company_Address = ?"
				+ ", Minimum_Psw = ?"
				+ ", Psw_Expiry = ?, Session_Timeout = ?"
				+ ", loguseraudit=?,attempted_login=?,"
                                + " Psw_upper=?,Psw_lower=?,NumericPass=?," +
                                "SpecPassword=?,Pass_Limit=?,user_id=?  WHERE Company_Code=? ";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps = con.prepareStatement(query);
			ps.setString(1, company.getCompanyName());
			ps.setString(2, company.getAcronym());
			ps.setString(3, company.getCompanyAddress());
			ps.setInt(4, company.getMinimumPassword());
			ps.setInt(5, company.getPasswordExpiry());
			ps.setInt(6, company.getSessionTimeout());
			ps.setString(7, company.getLogUserAudit());
                        ps.setInt(8, company.getLog_on());
                        ps.setString(9,company.getPassword_upper());
                        ps.setString(10,company.getPassword_lower());
                        ps.setString(11,company.getPassword_numeric());
                        ps.setString(12,company.getPassword_special());
                        ps.setInt(13, company.getPasswordLimit());
                        ps.setString(14, company.getUserId());
                        ps.setString(15, company.getCompanyCode());


            done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

public com.magbel.admin.objects.Company getCompanyFed1() {
		com.magbel.admin.objects.Company company = null;


String query = "SELECT Company_Code, Company_Name, Acronym, Company_Address"
			+ ",  Vat_Rate, Wht_Rate,  Financial_Start_Date, Financial_No_OfMonths"
			+ ", Financial_End_Date,Minimum_Psw, Psw_Expiry, Session_Timeout"
			+ ", Enforce_Acq_Budget, Enforce_Pm_Budget, Enforce_Fuel_Allocation, Require_Quarterly_Pm"
			+ ", Quarterly_Surplus_Cf,User_Id, Processing_Status, Trans_Wait_Time,loguseraudit "
			+ ", Fed_Wht_Rate,Attempt_Logon,component_delimiter, Psw_upper,Psw_lower,NumericPass,SpecPassword,Pass_Limit FROM AM_GB_COMPANY";

Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {

				String companyCode = rs.getString("Company_Code");
				String companyName = rs.getString("Company_Name");
				;
				String acronym = rs.getString("Acronym");
				;
				String companyAddress = rs.getString("Company_Address");
				;
				double vatRate = rs.getDouble("Vat_Rate");
				double whtRate = rs.getDouble("Wht_Rate");
				String financialStartDate = sdf.format(rs
						.getDate("Financial_Start_Date"));
				;
				int financialNoOfMonths = rs.getInt("Financial_No_OfMonths");
				String financialEndDate = sdf.format(rs
						.getDate("Financial_End_Date"));
				int minimumPassword = rs.getInt("Minimum_Psw");
				int passwordExpiry = rs.getInt("Psw_Expiry");
				int sessionTimeout = rs.getInt("Session_Timeout");
				String enforceAcqBudget = rs.getString("Enforce_Acq_Budget");

				String enforcePmBudget = rs.getString("Enforce_Pm_Budget");

				String enforceFuelAllocation = rs
						.getString("Enforce_Fuel_Allocation");

				String requireQuarterlyPM = rs
						.getString("Require_Quarterly_Pm");

				String quarterlySurplusCf = rs
						.getString("Quarterly_Surplus_Cf");

				String userId = rs.getString("User_Id");
				String processingStatus = rs.getString("Processing_Status");
				String logUserAudit = rs.getString("loguseraudit");

				double transWaitTime = rs.getDouble("Trans_Wait_Time");
				double fedWhtRate = rs.getDouble("Fed_Wht_Rate");
				int attempt_logon = rs.getInt("Attempt_Logon");
                                String comp_delimiter = rs.getString("component_delimiter");
                                String password_upper = rs.getString("Psw_upper");
				String password_lower = rs.getString("password_lower");
				String password_numeric = rs.getString("NumericPass");
				String password_special = rs.getString("SpecPassword");
                                int passwordLimit = rs.getInt("Pass_Limit");
                	//System.out.println("**********************comp_delimiter**********************"+comp_delimiter);

				if (comp_delimiter == null)
				{
					comp_delimiter= "";
				}

				company = new com.magbel.admin.objects.Company(companyCode,
						companyName, acronym, companyAddress, vatRate, whtRate, fedWhtRate,
						financialStartDate, financialNoOfMonths,
						financialEndDate, minimumPassword, passwordExpiry,
						sessionTimeout, enforceAcqBudget, enforcePmBudget,
						enforceFuelAllocation, requireQuarterlyPM,
						quarterlySurplusCf, userId, processingStatus,
						transWaitTime,attempt_logon);
				company.setLogUserAudit(logUserAudit);
				company.setComp_delimiter(comp_delimiter);
                                company.setPassword_lower(password_lower);
				company.setPassword_numeric(password_numeric);
				company.setPassword_special(password_special);
				company.setPassword_upper(password_upper);
                                company.setPasswordLimit(passwordLimit);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return company;
	}


public com.magbel.admin.objects.Locations getLocationByLocID(String LocID) {
		com.magbel.admin.objects.Locations location = null;
		String query = "SELECT Location_Id, Location_Code, Location"
				+ ", Status, User_Id, Create_Date"
				+ " FROM AM_GB_LOCATION WHERE Location_Id = '" + LocID + "'";

		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String locationId = rs.getString("Location_Id");
				String locationCode = rs.getString("Location_Code");
				String locate = rs.getString("Location");
				String status = rs.getString("Status");
				String userId = rs.getString("User_Id");
				String createDate = rs.getString("Create_Date");

				location = new com.magbel.admin.objects.Locations();
				location.setLocationId(locationId);
				location.setLocationCode(locationCode);
				location.setLocation(locate);
				location.setUserId(userId);
				location.setCreateDate(createDate);
				location.setStatus(status);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return location;
	}


    public boolean createLocation(com.magbel.admin.objects.Locations location) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "INSERT INTO AM_GB_LOCATION( Location_Code, Location"
				+ ", Status, User_Id, Create_Date,location_id)" + " VALUES (?,?,?,?,?,?)";
			int mtid = Integer.parseInt(new ApplicationHelper().getGeneratedId("AM_GB_LOCATION"));
			//System.out.println("################ the mtid value is " + mtid);
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			// ps.setLong(1, System.currentTimeMillis());
			ps.setString(1, location.getLocationCode());
			ps.setString(2, location.getLocation());
			ps.setString(3, location.getStatus());
			ps.setString(4, location.getUserId());
			ps.setDate(5, df.dateConvert(new java.util.Date()));
			ps.setInt(6,mtid );

			done = (ps.executeUpdate() != -1);
		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

    public boolean updateLocation(com.magbel.admin.objects.Locations location) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "UPDATE AM_GB_LOCATION" + " SET Location_Code = ?"
				+ ",Location = ?,Status = ?" + " WHERE Location_Id = ?";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, location.getLocationCode());
			ps.setString(2, location.getLocation());
			ps.setString(3, location.getStatus());

			ps.setString(4, location.getLocationId());
			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}



	public com.magbel.admin.objects.Locations getLocationByLocCode(String LocCode) {

		com.magbel.admin.objects.Locations location = null;
		String query = "SELECT Location_Id, Location_Code, Location"
				+ ", Status, User_Id, Create_Date"
				+ " FROM AM_GB_LOCATION WHERE Location_Code = '" + LocCode
				+ "'";

		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String locationId = rs.getString("Location_Id");
				String locationCode = rs.getString("Location_Code");
				String locate = rs.getString("Location");
				String locationStatus = rs.getString("Status");
				String userId = rs.getString("User_Id");
				String createDate = rs.getString("Create_Date");

				location = new com.magbel.admin.objects.Locations();
				location.setLocationId(locationId);
				location.setLocationCode(locationCode);
				location.setLocation(locate);
				location.setStatus(locationStatus);
				location.setUserId(userId);
				location.setCreateDate(createDate);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return location;

	}
public java.util.ArrayList getLocationByQuery(String filter) {
		java.util.ArrayList _list = new java.util.ArrayList();
		com.magbel.admin.objects.Locations location = null;

		String query = "SELECT Location_Id, Location_Code, Location"
				+ ", Status, User_Id, Create_Date"
				+ " FROM AM_GB_LOCATION WHERE Location_Id IS NOT NULL ";

		Connection c = null;
		ResultSet rs = null;
		Statement s = null;
		query = query + filter;
		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String locationId = rs.getString("Location_Id");
				String locationCode = rs.getString("Location_Code");
				String locate = rs.getString("Location");
				String status = rs.getString("Status");
				String userId = rs.getString("User_Id");
				String createDate = rs.getString("Create_Date");

				location = new com.magbel.admin.objects.Locations();
				location.setLocationId(locationId);
				location.setLocationCode(locationCode);
				location.setLocation(locate);
				location.setUserId(userId);
				location.setCreateDate(createDate);
				location.setStatus(status);
				_list.add(location);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}

}
