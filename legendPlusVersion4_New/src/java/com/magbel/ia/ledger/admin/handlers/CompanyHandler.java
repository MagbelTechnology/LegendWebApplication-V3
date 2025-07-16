package com.magbel.ia.legend.admin.handlers; 

import com.magbel.util.ApplicationHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import com.magbel.util.DataConnect;
import java.sql.SQLException;

/**
 * @author Rahman Oloritun
 * @Entities company,AssetmanagerInfo,Driver,Location,categoryClasses, ASSETMAKE
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
		System.out.println("USING_ " + this.getClass().getName());
	}


	public com.magbel.ia.legend.admin.objects.Company getCompany() {
		com.magbel.ia.legend.admin.objects.Company company = null;
		String query = "SELECT Company_Code, Company_Name, Acronym, Company_Address"
				+ ",  Vat_Rate, Wht_Rate,  Financial_Start_Date, Financial_No_OfMonths"
				+ ", Financial_End_Date,Minimum_Password, Password_Expiry, Session_Timeout"
				+ ", Enforce_Acq_Budget, Enforce_Pm_Budget, Enforce_Fuel_Allocation, Require_Quarterly_Pm"
				+ ", Quarterly_Surplus_Cf,User_Id, Processing_Status, Trans_Wait_Time,loguseraudit "
				+ ", password_upper,password_lower,password_numeric,password_special,password_limit FROM AM_GB_COMPANY";

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
				double whtRate = rs.getDouble("Wht_Rate");//to do;get value for federal and state rate
			String financialStartDate = sdf.format(rs
						.getDate("Financial_Start_Date"));
				;
				int financialNoOfMonths = rs.getInt("Financial_No_OfMonths");
				String financialEndDate = sdf.format(rs
						.getDate("Financial_End_Date"));
				int minimumPassword = rs.getInt("Minimum_Password");
				int passwordExpiry = rs.getInt("Password_Expiry");
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
				String password_upper = rs.getString("password_upper");
				String password_lower = rs.getString("password_lower");
				String password_numeric = rs.getString("password_numeric");
				String password_special = rs.getString("password_special");
                                int password_limit = rs.getInt("password_limit");
				//to do give a condition for federal or state and use the value for whtRate

				company = new com.magbel.ia.legend.admin.objects.Company(companyCode,
						companyName, acronym, companyAddress, vatRate, whtRate,
						financialStartDate, financialNoOfMonths,
						financialEndDate, minimumPassword, passwordExpiry,
						sessionTimeout, enforceAcqBudget, enforcePmBudget,
						enforceFuelAllocation, requireQuarterlyPM,
						quarterlySurplusCf, userId, processingStatus,
						transWaitTime);
				company.setLogUserAudit(logUserAudit);
                                company.setPassword_lower(password_lower);
				company.setPassword_numeric(password_numeric);
				company.setPassword_special(password_special);
				company.setPassword_upper(password_upper);
                                company.setPasswordLimit(password_limit);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return company;
	}

	


//Ganiyu's code: the getCompanyFed() method that has both state and federal withholding tax rate

	public com.magbel.ia.legend.admin.objects.Company getCompanyFed() {
		com.magbel.ia.legend.admin.objects.Company company = null;
		/*
        String query = "SELECT Company_Code, Company_Name, Acronym, Company_Address"
				+ ",  Vat_Rate, Wht_Rate,  Financial_Start_Date, Financial_No_OfMonths"
				+ ", Financial_End_Date,Minimum_Password, Password_Expiry, Session_Timeout"
				+ ", Enforce_Acq_Budget, Enforce_Pm_Budget, Enforce_Fuel_Allocation, Require_Quarterly_Pm"
				+ ", Quarterly_Surplus_Cf,User_Id, Processing_Status, Trans_Wait_Time,loguseraudit "
				+ ", Fed_Wht_Rate,Attempt_Logon " + "FROM AM_GB_COMPANY";
         */

String query = "SELECT Company_Code, Company_Name, Acronym, Company_Address"
			+ ",  Vat_Rate, Wht_Rate,  Financial_Start_Date, Financial_No_OfMonths"
			+ ", Financial_End_Date,Minimum_Password, Password_Expiry, Session_Timeout"
			+ ", Enforce_Acq_Budget, Enforce_Pm_Budget, Enforce_Fuel_Allocation, Require_Quarterly_Pm"
			+ ", Quarterly_Surplus_Cf,User_Id, Processing_Status, Trans_Wait_Time,loguseraudit "
			+ ", Fed_Wht_Rate,Attempt_Logon,component_delimiter " + "FROM AM_GB_COMPANY";

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
				int minimumPassword = rs.getInt("Minimum_Password");
				int passwordExpiry = rs.getInt("Password_Expiry");
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
                	System.out.println("**********************comp_delimiter**********************"+comp_delimiter);

				if (comp_delimiter == null)
				{
					comp_delimiter= "";
				}

                //String fedWhtAcctStatus rs.getString("Fed_wht_acct_status");
				

				//to do give a condition for federal or state and use the value for whtRate

				company = new com.magbel.ia.legend.admin.objects.Company(companyCode,
						companyName, acronym, companyAddress, vatRate, whtRate, fedWhtRate,
						financialStartDate, financialNoOfMonths,
						financialEndDate, minimumPassword, passwordExpiry,
						sessionTimeout, enforceAcqBudget, enforcePmBudget,
						enforceFuelAllocation, requireQuarterlyPM,
						quarterlySurplusCf, userId, processingStatus,
						transWaitTime,attempt_logon);
				company.setLogUserAudit(logUserAudit);
				company.setComp_delimiter(comp_delimiter);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return company;
	}


	public com.magbel.ia.legend.admin.objects.Company getCompanyFed1() {
		com.magbel.ia.legend.admin.objects.Company company = null;
		/*
        String query = "SELECT Company_Code, Company_Name, Acronym, Company_Address"
				+ ",  Vat_Rate, Wht_Rate,  Financial_Start_Date, Financial_No_OfMonths"
				+ ", Financial_End_Date,Minimum_Password, Password_Expiry, Session_Timeout"
				+ ", Enforce_Acq_Budget, Enforce_Pm_Budget, Enforce_Fuel_Allocation, Require_Quarterly_Pm"
				+ ", Quarterly_Surplus_Cf,User_Id, Processing_Status, Trans_Wait_Time,loguseraudit "
				+ ", Fed_Wht_Rate,Attempt_Logon " + "FROM AM_GB_COMPANY";
         */

String query = "SELECT Company_Code, Company_Name, Acronym, Company_Address"
			+ ",  Vat_Rate, Wht_Rate,  Financial_Start_Date, Financial_No_OfMonths"
			+ ", Financial_End_Date,Minimum_Password, Password_Expiry, Session_Timeout"
			+ ", Enforce_Acq_Budget, Enforce_Pm_Budget, Enforce_Fuel_Allocation, Require_Quarterly_Pm"
			+ ", Quarterly_Surplus_Cf,User_Id, Processing_Status, Trans_Wait_Time,loguseraudit "
			+ ", Fed_Wht_Rate,Attempt_Logon,component_delimiter, password_upper,password_lower,password_numeric,password_special,password_limit FROM AM_GB_COMPANY";

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
				int minimumPassword = rs.getInt("Minimum_Password");
				int passwordExpiry = rs.getInt("Password_Expiry");
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
                                String password_upper = rs.getString("password_upper");
				String password_lower = rs.getString("password_lower");
				String password_numeric = rs.getString("password_numeric");
				String password_special = rs.getString("password_special");
                                int passwordLimit = rs.getInt("password_limit");
                	System.out.println("**********************comp_delimiter**********************"+comp_delimiter);

				if (comp_delimiter == null)
				{
					comp_delimiter= "";
				}

                //String fedWhtAcctStatus rs.getString("Fed_wht_acct_status");


				//to do give a condition for federal or state and use the value for whtRate

				company = new com.magbel.ia.legend.admin.objects.Company(companyCode,
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



public com.magbel.ia.legend.admin.objects.AssetManagerInfo getAssetManagerInfo() {
	com.magbel.ia.legend.admin.objects.AssetManagerInfo ami = null;
		String query = "SELECT  Processing_Date, Processing_Frequency, Next_Processing_Date, Default_Branch"
				+ ", Branch_Name,Suspense_Acct, Auto_Generate_Id, Residual_Value"
				+ ", Depreciation_Method, Vat_Account, Wht_Account"
				+ ", PL_Disposal_Account, PLD_Status, Vat_Acct_Status, Wht_Acct_Status"
				+ ", Suspense_Ac_Status,Sbu_Required, Sbu_Level,system_date,asset_acq_ac "
				+ " FROM AM_GB_COMPANY";

		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String processingDate = sdf.format(rs
						.getDate("Processing_Date"));
				String processingFrequency = rs
						.getString("Processing_Frequency");
				String nextProcessingDate = sdf.format(rs
						.getDate("Next_Processing_Date"));
				String defaultBranch = rs.getString("Default_Branch");
				String branchName = rs.getString("Branch_Name");
				String suspenseAcct = rs.getString("Suspense_Acct");
				String autoGenId = rs.getString("Auto_Generate_Id");
				String residualValue = rs.getString("Residual_Value");
				String depreciationMethod = rs.getString("Depreciation_Method");
				String vatAccount = rs.getString("Vat_Account");
				String whtAccount = rs.getString("Wht_Account");
				String PLDisposalAccount = rs.getString("PL_Disposal_Account");
				String PLDStatus = rs.getString("PLD_Status");
				String vatAcctStatus = rs.getString("Vat_Acct_Status");
				String whtAcctStatus = rs.getString("Wht_Acct_Status");
				String suspenseAcctStatus = rs.getString("Suspense_Ac_Status");
				String sbuRequired = rs.getString("Sbu_Required");
				String sbuLevel = rs.getString("Sbu_Level");
				String sysDate = sdf.format(rs.getDate("system_date"));
				String assetSuspenseAcct = rs.getString("asset_acq_ac");
				ami = new com.magbel.ia.legend.admin.objects.AssetManagerInfo(processingDate,
						processingFrequency, nextProcessingDate, defaultBranch,
						branchName, suspenseAcct, autoGenId, residualValue,
						depreciationMethod, vatAccount, whtAccount,
						PLDisposalAccount, PLDStatus, vatAcctStatus,
						whtAcctStatus, suspenseAcctStatus, sbuRequired,
						sbuLevel);
				ami.setSysDate(sysDate);
				ami.setAssetSuspenseAcct(assetSuspenseAcct);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return ami;
	}







/*
	//ganiyu's code
	public legend.admin.objects.AssetManagerInfo getAssetManagerInfoFed() {
		legend.admin.objects.AssetManagerInfo ami = null;
		String query = "SELECT  Processing_Date, Processing_Frequency, Next_Processing_Date, Default_Branch"
				+ ", Branch_Name,Suspense_Acct, Auto_Generate_Id, Residual_Value"
				+ ", Depreciation_Method, Vat_Account, Wht_Account, Fed_Wht_Account"
				+ ", PL_Disposal_Account, PLD_Status, Vat_Acct_Status, Wht_Acct_Status, Fed_wht_acct_status"
				+ ", Suspense_Ac_Status,Sbu_Required, Sbu_Level,system_date,asset_acq_ac,LPO_Required,Barcode_Fld,Cost_Threshold,defer_account "
				+ " FROM AM_GB_COMPANY";

		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String processingDate = sdf.format(rs
						.getDate("Processing_Date"));
				String processingFrequency = rs
						.getString("Processing_Frequency");
				String nextProcessingDate = sdf.format(rs
						.getDate("Next_Processing_Date"));
				String defaultBranch = rs.getString("Default_Branch");
				String branchName = rs.getString("Branch_Name");
				String suspenseAcct = rs.getString("Suspense_Acct");
				String autoGenId = rs.getString("Auto_Generate_Id");
				String residualValue = rs.getString("Residual_Value");
				String depreciationMethod = rs.getString("Depreciation_Method");
				String vatAccount = rs.getString("Vat_Account");
				String whtAccount = rs.getString("Wht_Account");
				String fedWhtAccount=rs.getString("Fed_Wht_Account");//ganiyu's code
				String PLDisposalAccount = rs.getString("PL_Disposal_Account");
				String PLDStatus = rs.getString("PLD_Status");
				String vatAcctStatus = rs.getString("Vat_Acct_Status");
				String whtAcctStatus = rs.getString("Wht_Acct_Status");
				String suspenseAcctStatus = rs.getString("Suspense_Ac_Status");
				String sbuRequired = rs.getString("Sbu_Required");
				String sbuLevel = rs.getString("Sbu_Level");
				String sysDate = sdf.format(rs.getDate("system_date"));
				String assetSuspenseAcct = rs.getString("asset_acq_ac");
				String fedWhtAcctStatus = rs.getString("Fed_wht_acct_status");
                String lpo_r = rs.getString("LPO_Required");
                String bar_code_r = rs.getString("Barcode_Fld");
              double cp_threshold = rs.getDouble("Cost_Threshold");
              String defer_account =rs.getString("defer_account");

				ami = new legend.admin.objects.AssetManagerInfo(processingDate,
						processingFrequency, nextProcessingDate, defaultBranch,
						branchName, suspenseAcct, autoGenId, residualValue,
						depreciationMethod, vatAccount, whtAccount, fedWhtAccount,
						PLDisposalAccount, PLDStatus, vatAcctStatus,
						whtAcctStatus, fedWhtAcctStatus, suspenseAcctStatus, sbuRequired,
						sbuLevel,lpo_r,bar_code_r,cp_threshold);
				ami.setSysDate(sysDate);
				ami.setAssetSuspenseAcct(assetSuspenseAcct);
                ami.setDeferAccount(defer_account);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return ami;
	}


    */

	private Connection getConnection() {
		Connection con = null;
		dc = new DataConnect("fixedasset");
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

	private boolean executeQuery(String query) {
		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			done = ps.execute();

		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;
	}

	/**
	 * createCurrency
	 */
	public boolean createCompany(com.magbel.ia.legend.admin.objects.Company company) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "INSERT INTO AM_GB_COMPANY(Company_Code, Company_Name, Acronym, Company_Address"
				+ ", Vat_Rate, Wht_Rate,Financial_Start_Date, Financial_No_OfMonths"
				+ ", Financial_End_Date,Minimum_Password"
				+ ", Password_Expiry, Session_Timeout, Enforce_Acq_Budget, Enforce_Pm_Budget"
				+ ", Enforce_Fuel_Allocation, Require_Quarterly_Pm,Quarterly_Surplus_Cf,User_Id,loguseraudit,Attempt_Logon,component_delimiter,password_limit)"
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, company.getCompanyCode());
			ps.setString(2, company.getCompanyName());
			ps.setString(3, company.getAcronym());
			ps.setString(4, company.getCompanyAddress());
			ps.setDouble(5, company.getVatRate());
			ps.setDouble(6, company.getWhtRate());
			ps.setDate(7, df.dateConvert(company.getFinancialStartDate()));
			ps.setInt(8, company.getFinancialNoOfMonths());
			ps.setDate(9, df.dateConvert(company.getFinancialEndDate()));
			ps.setInt(10, company.getMinimumPassword());
			ps.setInt(11, company.getPasswordExpiry());
			ps.setInt(12, company.getSessionTimeout());
			ps.setString(13, company.getEnforceAcqBudget());
			ps.setString(14, company.getEnforcePmBudget());
			ps.setString(15, company.getEnforceFuelAllocation());
			ps.setString(16, company.getRequireQuarterlyPM());
			ps.setString(17, company.getQuarterlySurplusCf());
			ps.setString(18, company.getUserId());
			ps.setString(19, company.getLogUserAudit());
			ps.setInt(20,company.getLog_on());
			ps.setString(21,company.getComp_delimiter());
                        ps.setInt(22, company.getPasswordLimit());
			done = (ps.executeUpdate() != -1);
			System.out.println("=========================checking if success"+done);

		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

	public boolean updateCompany(com.magbel.ia.legend.admin.objects.Company company) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "UPDATE AM_GB_COMPANY"
				+ " SET Company_Name = ?, Acronym = ?, Company_Address = ?,Vat_Rate = ?, Wht_Rate = ?"
				+ ", Financial_Start_Date = ?, Financial_No_OfMonths = ?"
				+ ", Financial_End_Date = ?, Minimum_Password = ?"
				+ ", Password_Expiry = ?, Session_Timeout = ?, Enforce_Acq_Budget = ?, Enforce_Pm_Budget = ?"
				+ ", Enforce_Fuel_Allocation = ?, Require_Quarterly_Pm = ?"
				+ ", Quarterly_Surplus_Cf = ?,loguseraudit=?,Attempt_Logon=?,component_delimiter=?,"
                                + " password_upper=?,password_lower=?,password_numeric=?,password_special=?,password_limit=?  WHERE Company_Code=? ";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps = con.prepareStatement(query);
		
			ps.setString(1, company.getCompanyName());
			ps.setString(2, company.getAcronym());
			ps.setString(3, company.getCompanyAddress());
			ps.setDouble(4, company.getVatRate());
			ps.setDouble(5, company.getWhtRate());
			ps.setDate(6, df.dateConvert(company.getFinancialStartDate()));
			ps.setInt(7, company.getFinancialNoOfMonths());
			ps.setDate(8, df.dateConvert(company.getFinancialEndDate()));
			ps.setInt(9, company.getMinimumPassword());
			ps.setInt(10, company.getPasswordExpiry());
			ps.setInt(11, company.getSessionTimeout());
			ps.setString(12, company.getEnforceAcqBudget());
			ps.setString(13, company.getEnforcePmBudget());
			ps.setString(14, company.getEnforceFuelAllocation());
			ps.setString(15, company.getRequireQuarterlyPM());
			ps.setString(16, company.getQuarterlySurplusCf());
			ps.setString(17, company.getLogUserAudit());
                        ps.setInt(18, company.getLog_on());
                        ps.setString(19,company.getComp_delimiter());
            System.out.print("update am_gb_company 1...>>>>>>>>>>>"+company.getComp_delimiter());
                        ps.setString(20,company.getPassword_upper());
                        ps.setString(21,company.getPassword_lower());
                        ps.setString(22,company.getPassword_numeric());
                        ps.setString(23,company.getPassword_special());
                        ps.setInt(24, company.getPasswordLimit());
                        ps.setString(25, company.getCompanyCode());
        	  System.out.print("update am_gb_company 2...>>>>>>>>>>>"+company.getCompanyCode());

            done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

	public boolean updateAssetManagerInfo(com.magbel.ia.legend.admin.objects.AssetManagerInfo ami) {
       /*
        System.out.println("in updateAssetManagerInfo ##################### ");
        System.out.println("the value of cost threshold is ##################### " + ami.getCp_threshold());
        System.out.println("the value of trans threshold is ##################### " + ami.getTrans_threshold());
        System.out.println("the value of df.dateConvert(ami.getProcessingDate()) is ##################### " + df.dateConvert(ami.getProcessingDate()));
        System.out.println("the value of ami.getProcessingFrequency() is ##################### " + ami.getProcessingFrequency());
        System.out.println("the value of df.dateConvert(ami.getNextProcessingDate()) is ##################### " +df.dateConvert(ami.getNextProcessingDate()));
        System.out.println("the value of ami.getDefaultBranch() is ##################### " + ami.getDefaultBranch());
        System.out.println("the value of ami.getBranchName() is ##################### " + ami.getBranchName());
        System.out.println("the value of ami.getSuspenseAcct() is ##################### " + ami.getSuspenseAcct());
        System.out.println("the value of ami.getAutoGenId() is ##################### " + ami.getAutoGenId());
        System.out.println("the value of ami.getResidualValue() is ##################### " + ami.getResidualValue());
        System.out.println("the value of ami.getDepreciationMethod() is ##################### " + ami.getDepreciationMethod());
        System.out.println("the value of ami.getVatAccount() is ##################### " + ami.getVatAccount());
        System.out.println("the value of ami.getWhtAccount() is ##################### " + ami.getWhtAccount());
        System.out.println("the value of ami.getPLDisposalAccount() is ##################### " + ami.getPLDisposalAccount());
        System.out.println("the value of ami.getPLDStatus() is ##################### " + ami.getPLDStatus());
        System.out.println("the value of ami.getVatAcctStatus() is ##################### " + ami.getVatAcctStatus());
        System.out.println("the value of ami.getWhtAcctStatus() is ##################### " + ami.getWhtAcctStatus());
        System.out.println("the value of ami.getSuspenseAcctStatus() is ##################### " + ami.getSuspenseAcctStatus());
        System.out.println("the value of ami.getSbuRequired() is ##################### " + ami.getSbuRequired());
        System.out.println("the value of ami.getSbuLevel() is ##################### " + ami.getSbuLevel());
        System.out.println("the value of ami.getAssetSuspenseAcct() is ##################### " + ami.getAssetSuspenseAcct());
        System.out.println("the value of  ami.getLpo_r()is ##################### " +  ami.getLpo_r());
        System.out.println("the value of ami.getBar_code_r() is ##################### " + ami.getBar_code_r());
        System.out.println("the value of ami.getCp_threshold()is ##################### " + ami.getCp_threshold());
        System.out.println("the value of ami.getTrans_threshold() is ##################### " + ami.getTrans_threshold());
        System.out.println("the value of defer account is ##################### " + ami.getDeferAccount());
        System.out.println("the value of ami.getFedWhtAccount() is ##################### " + ami.getFedWhtAccount());
        System.out.println("the value of ami.getFedWhtAcctStatus() is ##################### " + ami.getFedWhtAcctStatus());
       */

        Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		
        String query = "UPDATE AM_GB_COMPANY "
				+ "SET  Processing_Date = ? ,Processing_Frequency = ?, "
				 + "Next_Processing_Date = ?, Default_Branch = ?, "
				+ "Branch_Name = ?,Suspense_Acct = ? ,Auto_Generate_Id = ?, "
				 + "Residual_Value = ?, Depreciation_Method = ?, "
				+ "Vat_Account = ?, Wht_Account = ?,  PL_Disposal_Account = ?, PLD_Status = ?, "
				+ "Vat_Acct_Status = ?, Wht_Acct_Status = ?, " 
				 + "Suspense_Ac_Status = ?, Sbu_Required = ?, "
				 + "Sbu_Level = ?, asset_acq_ac=?, LPO_Required=?, "
                 + "Barcode_Fld=?, Cost_Threshold=?, Trans_threshold=?, Defer_account=?, "
                 +"Fed_Wht_Account =?, Fed_Wht_Acct_Status = ?,part_pay_acct=?, "
                 +"asset_acq_status=?, asset_defer_status=?, part_pay_status=?";

       
        System.out.println("HERE ##################### " );

       // String query = "update am_gb_company set cost_threshold =?";
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			         //System.out.println("the query is############ " + query);
                     //ps.setDouble(1,ami.getCp_threshold());
          
			ps.setDate(1, df.dateConvert(ami.getProcessingDate()));
			ps.setString(2, ami.getProcessingFrequency());
			ps.setDate(3, df.dateConvert(ami.getNextProcessingDate()));
			ps.setString(4, ami.getDefaultBranch());
			ps.setString(5, ami.getBranchName());
			ps.setString(6, ami.getSuspenseAcct());
			ps.setString(7, ami.getAutoGenId());
			ps.setString(8, ami.getResidualValue());
			ps.setString(9, ami.getDepreciationMethod());
			ps.setString(10, ami.getVatAccount());
			ps.setString(11, ami.getWhtAccount());
			ps.setString(12, ami.getPLDisposalAccount());
			ps.setString(13, ami.getPLDStatus());
			ps.setString(14, ami.getVatAcctStatus());
			ps.setString(15, ami.getWhtAcctStatus());
			ps.setString(16, ami.getSuspenseAcctStatus());
			ps.setString(17, ami.getSbuRequired());
			ps.setString(18, ami.getSbuLevel());
			ps.setString(19, ami.getAssetSuspenseAcct());
            ps.setString(20, ami.getLpo_r());
            ps.setString(21,ami.getBar_code_r());
            ps.setDouble(22,ami.getCp_threshold());
            ps.setDouble(23, ami.getTrans_threshold());
            ps.setString(24, ami.getDeferAccount());
            ps.setString(25, ami.getFedWhtAccount());
            ps.setString(26, ami.getFedWhtAcctStatus());
            ps.setString(27, ami.getPart_pay());
            ps.setString(28, ami.getAsset_acq_status());
            ps.setString(29, ami.getAsset_defer_status());
            ps.setString(30, ami.getPart_pay_status());
                
            System.out.println("The content of ps is################### "+ ps.toString());
            int psResult = ps.executeUpdate();
			System.out.println("The content of ps is################### "+ ps.toString());
			done = (psResult != -1);
            System.out.println("outcome of update psResult is ########## "+ psResult);


		}
        catch(SQLException ex) {//start catch block
					System.out.println("\n--- SQLException caught in updateAssetManagerInfo METHOD of COMPANY HANDLER CLASS---\n");
					while (ex != null) {//start while block
								System.out.println("Message:   "
														+ ex.getMessage ());
								System.out.println("SQLState:  "
														+ ex.getSQLState ());
								System.out.println("ErrorCode: "
														+ ex.getErrorCode ());
								ex = ex.getNextException();
								System.out.println("");
							}//end while block
     			}//end catch block

        catch (Exception e) {

            System.out.println("#######ERROR OCCURED IN updateAssetManagerInfo METHOD OF COMPANY HANDLER CLASS");
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

	public java.util.ArrayList getAllDrivers() {
		java.util.ArrayList _list = new java.util.ArrayList();
		com.magbel.ia.legend.admin.objects.Driver driver = null;
		String query = "SELECT Driver_ID, Driver_Code, Driver_License, dl_issue_date,"
				+ "dl_expiry_date, Driver_LastName, Driver_FirstName, Driver_OtherName,"
				+ "Driver_Branch, Driver_Dept, Contact_Address, Driver_State, Driver_Phone,"
				+ "Driver_Fax, Driver_email, Driver_Status, driver_province, User_id,"
				+ "Create_date FROM am_ad_driver";

		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String driverId = rs.getString("Driver_ID");
				String code = rs.getString("Driver_Code");
				String license = rs.getString("Driver_License");
				String issueD = sdf.format(rs.getDate("dl_issue_date"));
				String expD = sdf.format(rs.getDate("dl_expiry_date"));
				String lastN = rs.getString("Driver_LastName");
				String firstN = rs.getString("Driver_FirstName");
				String otherN = rs.getString("Driver_OtherName");
				String branch = rs.getString("Driver_Branch");
				String dept = rs.getString("Driver_Dept");
				String address = rs.getString("Contact_Address");
				String state = rs.getString("Driver_State");
				String phone = rs.getString("Driver_Phone");
				String fax = rs.getString("Driver_Fax");
				String email = rs.getString("Driver_email");
				String status = rs.getString("Driver_Status");
				String province = rs.getString("driver_province");
				String userId = rs.getString("User_id");
				String createD = sdf.format(rs.getDate("Create_date"));

				driver = new com.magbel.ia.legend.admin.objects.Driver();
				driver.setDriverId(driverId);
				driver.setDriverCode(code);
				driver.setDriverLicense(license);
				driver.setDlIssueDate(issueD);
				driver.setDlExpiryDate(expD);
				driver.setDriverLastName(lastN);
				driver.setDriverFirstname(firstN);
				driver.setDriverOtherName(otherN);
				driver.setDriverBranch(branch);
				driver.setDriverDept(dept);
				driver.setContatcAddress(address);
				driver.setDriverState(state);
				driver.setDriverPhone(phone);
				driver.setDriverFax(fax);
				driver.setDriverEmail(email);
				driver.setDriverStatus(status);
				driver.setDriverProvince(province);
				driver.setUserId(userId);
				driver.setCreateDate(createD);
				_list.add(driver);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}

	public java.util.ArrayList getDriverByQuery(String filter) {
		java.util.ArrayList _list = new java.util.ArrayList();
		com.magbel.ia.legend.admin.objects.Driver driver = null;
		String query = "SELECT Driver_ID, Driver_Code, Driver_License, dl_issue_date,"
				+ "dl_expiry_date, Driver_LastName, Driver_FirstName, Driver_OtherName,"
				+ "Driver_Branch, Driver_Dept, Contact_Address, Driver_State, Driver_Phone,"
				+ "Driver_Fax, Driver_email, Driver_Status, driver_province, User_id,"
				+ "Create_date FROM am_ad_driver WHERE Driver_ID IS NOT NULL ";

		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String driverId = rs.getString("Driver_ID");
				String code = rs.getString("Driver_code");
				String license = rs.getString("Driver_License");
				String issueD = sdf.format(rs.getDate("dl_issue_date"));
				String expD = sdf.format(rs.getDate("dl_expiry_date"));
				String lastN = rs.getString("Driver_LastName");
				String firstN = rs.getString("Driver_FirstName");
				String otherN = rs.getString("Driver_OtherName");
				String branch = rs.getString("Driver_Branch");
				String dept = rs.getString("Driver_Dept");
				String address = rs.getString("Contact_Address");
				String state = rs.getString("Driver_State");
				String phone = rs.getString("Driver_Phone");
				String fax = rs.getString("Driver_Fax");
				String email = rs.getString("Driver_email");
				String status = rs.getString("Driver_Status");
				String province = rs.getString("driver_province");
				String userId = rs.getString("User_id");
				String createD = sdf.format(rs.getDate("Create_date"));

				driver = new com.magbel.ia.legend.admin.objects.Driver();
				driver.setDriverId(driverId);
				driver.setDriverCode(code);
				driver.setDriverLicense(license);
				driver.setDlIssueDate(issueD);
				driver.setDlExpiryDate(expD);
				driver.setDriverLastName(lastN);
				driver.setDriverFirstname(firstN);
				driver.setDriverOtherName(otherN);
				driver.setDriverBranch(branch);
				driver.setDriverDept(dept);
				driver.setContatcAddress(address);
				driver.setDriverState(state);
				driver.setDriverPhone(phone);
				driver.setDriverFax(fax);
				driver.setDriverEmail(email);
				driver.setDriverStatus(status);
				driver.setDriverProvince(province);
				driver.setUserId(userId);
				driver.setCreateDate(createD);
				_list.add(driver);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}

	public com.magbel.ia.legend.admin.objects.Driver getDriverByDriverID(String driverid) {

		com.magbel.ia.legend.admin.objects.Driver driver = null;
		String query = "SELECT Driver_ID, Driver_Code, Driver_License, dl_issue_date,"
				+ "dl_expiry_date, Driver_LastName, Driver_FirstName, Driver_OtherName,"
				+ "Driver_Branch, Driver_Dept, Contact_Address, Driver_State, Driver_Phone,"
				+ "Driver_Fax, Driver_email, Driver_Status, driver_province, User_id,"
				+ "Create_date FROM am_ad_driver WHERE Driver_ID=" + driverid;

		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String driverId = rs.getString("Driver_ID");
				String code = rs.getString("Driver_code");
				String license = rs.getString("Driver_License");
				String issueD = sdf.format(rs.getDate("dl_issue_date"));
				String expD = sdf.format(rs.getDate("dl_expiry_date"));
				String lastN = rs.getString("Driver_LastName");
				String firstN = rs.getString("Driver_FirstName");
				String otherN = rs.getString("Driver_OtherName");
				String branch = rs.getString("Driver_Branch");
				String dept = rs.getString("Driver_Dept");
				String address = rs.getString("Contact_Address");
				String state = rs.getString("Driver_State");
				String phone = rs.getString("Driver_Phone");
				String fax = rs.getString("Driver_Fax");
				String email = rs.getString("Driver_email");
				String status = rs.getString("Driver_Status");
				String province = rs.getString("driver_province");
				String userId = rs.getString("User_id");
				String createD = sdf.format(rs.getDate("Create_date"));

				driver = new com.magbel.ia.legend.admin.objects.Driver();
				driver.setDriverId(driverId);
				driver.setDriverCode(code);
				driver.setDriverLicense(license);
				driver.setDlIssueDate(issueD);
				driver.setDlExpiryDate(expD);
				driver.setDriverLastName(lastN);
				driver.setDriverFirstname(firstN);
				driver.setDriverOtherName(otherN);
				driver.setDriverBranch(branch);
				driver.setDriverDept(dept);
				driver.setContatcAddress(address);
				driver.setDriverState(state);
				driver.setDriverPhone(phone);
				driver.setDriverFax(fax);
				driver.setDriverEmail(email);
				driver.setDriverStatus(status);
				driver.setDriverProvince(province);
				driver.setUserId(userId);
				driver.setCreateDate(createD);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return driver;

	}

	public com.magbel.ia.legend.admin.objects.Driver getDriverByDriverCode(String driverid) {

		com.magbel.ia.legend.admin.objects.Driver driver = null;
		String query = "SELECT Driver_ID, Driver_Code, Driver_License, dl_issue_date,"
				+ "dl_expiry_date, Driver_LastName, Driver_FirstName, Driver_OtherName,"
				+ "Driver_Branch, Driver_Dept, Contact_Address, Driver_State, Driver_Phone,"
				+ "Driver_Fax, Driver_email, Driver_Status, driver_province, User_id,"
				+ "Create_date FROM am_ad_driver WHERE Driver_Code='"
				+ driverid + "'";

		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String driverId = rs.getString("Driver_ID");
				String code = rs.getString("Driver_code");
				String license = rs.getString("Driver_License");
				String issueD = sdf.format(rs.getDate("dl_issue_date"));
				String expD = sdf.format(rs.getDate("dl_expiry_date"));
				String lastN = rs.getString("Driver_LastName");
				String firstN = rs.getString("Driver_FirstName");
				String otherN = rs.getString("Driver_OtherName");
				String branch = rs.getString("Driver_Branch");
				String dept = rs.getString("Driver_Dept");
				String address = rs.getString("Contact_Address");
				String state = rs.getString("Driver_State");
				String phone = rs.getString("Driver_Phone");
				String fax = rs.getString("Driver_Fax");
				String email = rs.getString("Driver_email");
				String status = rs.getString("Driver_Status");
				String province = rs.getString("driver_province");
				String userId = rs.getString("User_id");
				String createD = sdf.format(rs.getDate("Create_date"));

				driver = new com.magbel.ia.legend.admin.objects.Driver();
				driver.setDriverId(driverId);
				driver.setDriverCode(code);
				driver.setDriverLicense(license);
				driver.setDlIssueDate(issueD);
				driver.setDlExpiryDate(expD);
				driver.setDriverLastName(lastN);
				driver.setDriverFirstname(firstN);
				driver.setDriverOtherName(otherN);
				driver.setDriverBranch(branch);
				driver.setDriverDept(dept);
				driver.setContatcAddress(address);
				driver.setDriverState(state);
				driver.setDriverPhone(phone);
				driver.setDriverFax(fax);
				driver.setDriverEmail(email);
				driver.setDriverStatus(status);
				driver.setDriverProvince(province);
				driver.setUserId(userId);
				driver.setCreateDate(createD);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return driver;

	}

	public boolean createDriver(com.magbel.ia.legend.admin.objects.Driver ccode) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "INSERT INTO am_ad_driver(Driver_Code, Driver_License, dl_issue_date,"
				+ "dl_expiry_date, Driver_LastName, Driver_FirstName, Driver_OtherName,"
				+ "Driver_Branch, Driver_Dept, Contact_Address, Driver_State, Driver_Phone,"
				+ "Driver_Fax, Driver_email, Driver_Status, driver_province, User_id,"
				+ "Create_date) "
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, ccode.getDriverCode());
			ps.setString(2, ccode.getDriverLicense());
			ps.setDate(3, df.dateConvert(ccode.getDlIssueDate()));
			ps.setDate(4, df.dateConvert(ccode.getDlExpiryDate()));
			ps.setString(5, ccode.getDriverLastName());
			ps.setString(6, ccode.getDriverFirstname());
			ps.setString(7, ccode.getDriverOtherName());
			ps.setString(8, ccode.getDriverBranch());
			ps.setString(9, ccode.getDriverDept());
			ps.setString(10, ccode.getContatcAddress());
			ps.setString(11, ccode.getDriverState());
			ps.setString(12, ccode.getDriverPhone());
			ps.setString(13, ccode.getDriverFax());
			ps.setString(14, ccode.getDriverEmail());
			ps.setString(15, ccode.getDriverStatus());
			ps.setString(16, ccode.getDriverProvince());
			ps.setString(17, ccode.getUserId());
			ps.setDate(18, df.dateConvert(new java.util.Date()));
			// ps.setLong(20, System.currentTimeMillis());

			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

	public boolean updateDriver(com.magbel.ia.legend.admin.objects.Driver ccode) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "UPDATE am_ad_driver"
				+ " SET Driver_Code = ?,Driver_License=?,dl_issue_date = ?"
				+ ",dl_expiry_date = ?,Driver_LastName =?,Driver_FirstName =?,Driver_OtherName = ?"
				+ ",Driver_Branch =?, Driver_Dept = ?, Contact_Address = ?, Driver_State = ?"
				+ ",Driver_Phone = ?,Driver_Fax = ?, Driver_email = ?, Driver_Status = ?"
				+ ",driver_province = ?" + " WHERE Driver_ID = ?";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, ccode.getDriverCode());
			ps.setString(2, ccode.getDriverLicense());
			ps.setDate(3, df.dateConvert(ccode.getDlIssueDate()));
			ps.setDate(4, df.dateConvert(ccode.getDlExpiryDate()));
			ps.setString(5, ccode.getDriverLastName());
			ps.setString(6, ccode.getDriverFirstname());
			ps.setString(7, ccode.getDriverOtherName());
			ps.setString(8, ccode.getDriverBranch());
			ps.setString(9, ccode.getDriverDept());
			ps.setString(10, ccode.getContatcAddress());
			ps.setString(11, ccode.getDriverState());
			ps.setString(12, ccode.getDriverPhone());
			ps.setString(13, ccode.getDriverFax());
			ps.setString(14, ccode.getDriverEmail());
			ps.setString(15, ccode.getDriverStatus());
			ps.setString(16, ccode.getDriverProvince());
			ps.setString(17, ccode.getDriverId());
			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

	public java.util.List getAllLocations() {
		java.util.List _list = new java.util.ArrayList();
		com.magbel.ia.legend.admin.objects.Locations location = null;
		String query = "SELECT Location_Id, Location_Code, Location"
				+ ", Status, User_Id, Create_Date" + " FROM AM_GB_LOCATION";

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

				location = new com.magbel.ia.legend.admin.objects.Locations();
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

	public java.util.ArrayList getLocationByQuery(String filter) {
		java.util.ArrayList _list = new java.util.ArrayList();
		com.magbel.ia.legend.admin.objects.Locations location = null;

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

				location = new com.magbel.ia.legend.admin.objects.Locations();
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

	public com.magbel.ia.legend.admin.objects.Locations getLocationByLocID(String LocID) {
		com.magbel.ia.legend.admin.objects.Locations location = null;
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

				location = new com.magbel.ia.legend.admin.objects.Locations();
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

    /*
	public boolean createLocation(legend.admin.objects.Locations location) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "INSERT INTO AM_GB_LOCATION( Location_Code, Location"
				+ ", Status, User_Id, Create_Date)" + " VALUES (?,?,?,?,?)";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			// ps.setLong(1, System.currentTimeMillis());
			ps.setString(1, location.getLocationCode());
			ps.setString(2, location.getLocation());
			ps.setString(3, location.getStatus());
			ps.setString(4, location.getUserId());
			ps.setDate(5, df.dateConvert(new java.util.Date()));

			done = (ps.executeUpdate() != -1);
		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

	public boolean updateLocation(legend.admin.objects.Locations location) {

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
*/


    public boolean createLocation(com.magbel.ia.legend.admin.objects.Locations location) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "INSERT INTO AM_GB_LOCATION( Location_Code, Location"
				+ ", Status, User_Id, Create_Date,location_id)" + " VALUES (?,?,?,?,?,?)";
			String mtid = new ApplicationHelper().getGeneratedId("AM_GB_LOCATION");
			System.out.println("################ the mtid value is " + mtid);
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			// ps.setLong(1, System.currentTimeMillis());
			ps.setString(1, location.getLocationCode());
			ps.setString(2, location.getLocation());
			ps.setString(3, location.getStatus());
			ps.setString(4, location.getUserId());
			ps.setDate(5, df.dateConvert(new java.util.Date()));
			ps.setString(6,mtid );

			done = (ps.executeUpdate() != -1);
		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

    public boolean updateLocation(com.magbel.ia.legend.admin.objects.Locations location) {

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


    /*
	public legend.admin.objects.Locations getLocationByLocCode(String LocCode) {

		legend.admin.objects.Locations location = null;
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

				location = new legend.admin.objects.Locations();
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

    */

	public com.magbel.ia.legend.admin.objects.Locations getLocationByLocCode(String LocCode) {

		com.magbel.ia.legend.admin.objects.Locations location = null;
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

				location = new com.magbel.ia.legend.admin.objects.Locations();
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

	public java.util.ArrayList getAllCategoryClass() {
		java.util.ArrayList _list = new java.util.ArrayList();
		com.magbel.ia.legend.admin.objects.CategoryClass catclass = null;
		String query = "SELECT Class_ID, Class_Code, Class_Name"
				+ ", Class_Status, User_Id, Create_Date"
				+ " FROM AM_AD_CATEGORY_CLASS";

		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String classId = rs.getString("Class_Id");
				String classCode = rs.getString("Class_Code");
				String className = rs.getString("Class_Name");
				String classStatus = rs.getString("Class_Status");
				String userId = rs.getString("User_Id");
				String createDate = rs.getString("Create_Date");

				catclass = new com.magbel.ia.legend.admin.objects.CategoryClass();
				catclass.setClassId(classId);
				catclass.setClassCode(classCode);
				catclass.setClassName(className);
				catclass.setUserId(userId);
				catclass.setClassStatus(classStatus);
				catclass.setCreateDate(createDate);
				_list.add(catclass);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}

	public java.util.List getCategoryClassByQuery(String filter) {
		java.util.List _list = new java.util.ArrayList();
		com.magbel.ia.legend.admin.objects.CategoryClass catclass = null;

		String query = "SELECT Class_Id, Class_Code, Class_Name"
				+ ", Class_Status, User_Id, Create_Date"
				+ " FROM AM_AD_CATEGORY_CLASS WHERE Class_Id IS NOT NULL ";

		Connection c = null;
		ResultSet rs = null;
		Statement s = null;
		query += filter;
		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String classId = rs.getString("Class_Id");
				String classCode = rs.getString("Class_Code");
				String className = rs.getString("Class_Name");
				String classStatus = rs.getString("Class_Status");
				String userId = rs.getString("User_Id");
				String createDate = rs.getString("Create_Date");

				catclass = new com.magbel.ia.legend.admin.objects.CategoryClass();
				catclass.setClassId(classId);
				catclass.setClassCode(classCode);
				catclass.setClassName(className);
				catclass.setUserId(userId);
				catclass.setCreateDate(createDate);
				catclass.setClassStatus(classStatus);
				_list.add(catclass);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}

	public com.magbel.ia.legend.admin.objects.CategoryClass getCategoryClassByClassID(
			String CatClassID) {
		com.magbel.ia.legend.admin.objects.CategoryClass catclass = null;
		String query = "SELECT Class_Id, Class_Code, Class_Name"
				+ ", Class_Status, User_Id, Create_Date"
				+ " FROM AM_AD_CATEGORY_CLASS WHERE Class_ID = '" + CatClassID
				+ "'";

		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String classId = rs.getString("Class_Id");
				String classCode = rs.getString("Class_Code");
				String className = rs.getString("Class_Name");
				String classStatus = rs.getString("Class_Status");
				String userId = rs.getString("User_Id");
				String createDate = rs.getString("Create_Date");

				catclass = new com.magbel.ia.legend.admin.objects.CategoryClass();
				catclass.setClassId(classId);
				catclass.setClassCode(classCode);
				catclass.setClassName(className);
				catclass.setUserId(userId);
				catclass.setClassStatus(classStatus);
				catclass.setCreateDate(createDate);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return catclass;
	}

	public boolean createCategoryClass(
			com.magbel.ia.legend.admin.objects.CategoryClass catgclass) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "INSERT INTO AM_AD_CATEGORY_CLASS( Class_Code, Class_Name"
				+ ", Class_Status, User_Id, Create_Date)"
				+ " VALUES (?,?,?,?,?)";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);

			ps.setString(1, catgclass.getClassCode());
			ps.setString(2, catgclass.getClassName());
			ps.setString(3, catgclass.getClassStatus());
			ps.setString(4, catgclass.getUserId());
			ps.setDate(5, df.dateConvert(new java.util.Date()));
		//	ps.setLong(6, System.currentTimeMillis());

			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

	public boolean updateCategoryClass(
			com.magbel.ia.legend.admin.objects.CategoryClass catgclass) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "UPDATE AM_AD_CATEGORY_CLASS" + " SET Class_Code = ?"
				+ ",Class_Name = ?,Class_Status = ?" + " WHERE  Class_ID = ?";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, catgclass.getClassCode());
			ps.setString(2, catgclass.getClassName());
			ps.setString(3, catgclass.getClassStatus());
			//ps.setString(4, catgclass.getUserId());
			ps.setString(4, catgclass.getClassId());
			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

	public com.magbel.ia.legend.admin.objects.CategoryClass getCategoryClassByClassCode(
			String CatClassCode) {

		com.magbel.ia.legend.admin.objects.CategoryClass catclass = null;
		String query = "SELECT Class_ID, Class_Code, Class_Name"
				+ ", Class_Status, User_Id, Create_Date"
				+ " FROM AM_AD_CATEGORY_CLASS WHERE Class_Code = '"
				+ CatClassCode + "'";

		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String classId = rs.getString("Class_Id");
				String classCode = rs.getString("Class_Code");
				String className = rs.getString("Class_Name");
				String classStatus = rs.getString("Class_Status");
				String userId = rs.getString("User_Id");
				String createDate = rs.getString("Create_Date");

				catclass = new com.magbel.ia.legend.admin.objects.CategoryClass();
				catclass.setClassId(classId);
				catclass.setClassCode(classCode);
				catclass.setClassName(className);
				catclass.setUserId(userId);
				catclass.setCreateDate(createDate);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return catclass;

	}

	private com.magbel.ia.legend.admin.objects.AssetMake getAnAssetMake(String filter) {
		com.magbel.ia.legend.admin.objects.AssetMake am = null;
		String query = "SELECT AssetMake_ID,AssetMake_Code,AssetMake"
				+ " ,status,Category_ID,user_id,create_date"
				+ " FROM am_gb_assetMake  ";

		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {

				String assetMakeId = rs.getString("AssetMake_ID");
				String assetMakeCode = rs.getString("AssetMake_Code");
				String assetMake = rs.getString("AssetMake");
				String status = rs.getString("status");
				String category = rs.getString("Category_ID");
				String userid = rs.getString("user_id");
				String createDate = sdf.format(rs.getDate("create_date"));
				am = new com.magbel.ia.legend.admin.objects.AssetMake(assetMakeId,
						assetMakeCode, assetMake, status, category, userid,
						createDate);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return am;

	}

	public java.util.List getAssetMakeByQuery(String filter) {
		com.magbel.ia.legend.admin.objects.AssetMake am = null;
		java.util.List<com.magbel.ia.legend.admin.objects.AssetMake> _list = new java.util.ArrayList<com.magbel.ia.legend.admin.objects.AssetMake>();
		String query = "SELECT AssetMake_ID,AssetMake_Code,AssetMake"
				+ " ,status,Category_ID,user_id,create_date"
				+ " FROM am_gb_assetMake  ";

		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {

				String assetMakeId = rs.getString("AssetMake_ID");
				String assetMakeCode = rs.getString("AssetMake_Code");
				String assetMake = rs.getString("AssetMake");
				String status = rs.getString("status");
				String category = rs.getString("Category_ID");
				String userid = rs.getString("user_id");
				String createDate = sdf.format(rs.getDate("create_date"));
				am = new com.magbel.ia.legend.admin.objects.AssetMake(assetMakeId,
						assetMakeCode, assetMake, status, category, userid,
						createDate);
				_list.add(am);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}

	public com.magbel.ia.legend.admin.objects.AssetMake getAssetMakeByID(String id) {
		String filter = " WHERE AssetMake_ID = " + id;
		com.magbel.ia.legend.admin.objects.AssetMake am = getAnAssetMake(filter);
		return am;

	}

	public com.magbel.ia.legend.admin.objects.AssetMake getAssetMakeByCode(String code) {
		String filter = " WHERE AssetMake_Code = '" + code + "'";
		com.magbel.ia.legend.admin.objects.AssetMake am = getAnAssetMake(filter);
		return am;

	}

	public boolean createAssetMake(com.magbel.ia.legend.admin.objects.AssetMake am) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
                
		String query = "INSERT INTO am_gb_assetMake(AssetMake_Code,AssetMake,"
				+ "  status,Category_ID,user_id,create_date)"
				+ " VALUES (?,?,?,?, ?,?)";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			 ps.setString(1, am.getAssetMakeCode());
                      //  System.out.println(0+assetMakeCode);
                       // ps.setString(1, 0+assetMakeCode);
			ps.setString(2, am.getAssetMake());
			ps.setString(3, am.getStatus());
			ps.setString(4, am.getCategory());
			ps.setString(5, am.getUserid());
			ps.setDate(6, df.dateConvert(new java.util.Date()));
			//ps.setLong(7, System.currentTimeMillis());
			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " ERROR:Error creating Department ->" + e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

	public boolean updateAssetMake(com.magbel.ia.legend.admin.objects.AssetMake am) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "UPDATE am_gb_assetMake SET AssetMake_Code = ?,AssetMake = ?,status =?,"
				+ "  Category_ID = ?" + " WHERE AssetMake_ID =?";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, am.getAssetMakeCode());
			ps.setString(2, am.getAssetMake());
			ps.setString(3, am.getStatus());
			ps.setString(4, am.getCategory());
			// ps.setString(5, am.getUserid());
			ps.setString(5, am.getAssetMakeId());
			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " ERROR:Error updating  Asset Make ->" + e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

	public java.util.ArrayList getDisposableReasonsByQuery(String filter) {
		java.util.ArrayList _list = new java.util.ArrayList();
		com.magbel.ia.legend.admin.objects.DisposableReasons dispres = null;

		String query = "SELECT reason_id, reason_code, description, reason_status, user_id,"
				+ " create_date FROM am_ad_disposalReasons WHERE reason_id IS NOT NULL ";

		query += filter;

		try {
			con = getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				String reasonId = rs.getString("reason_id");
				String reasonCode = rs.getString("reason_code");
				String description = rs.getString("description");
				String reasonStatus = rs.getString("reason_status");
				String userId = rs.getString("user_id");
				String createDate = sdf.format(rs.getDate("create_date"));

				dispres = new com.magbel.ia.legend.admin.objects.DisposableReasons();
				dispres.setReasonId(reasonId);
				dispres.setReasonCode(reasonCode);
				dispres.setDescription(description);
				dispres.setReasonStatus(reasonStatus);
				dispres.setUserId(userId);
				dispres.setCreateDate(createDate);

				_list.add(dispres);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(con, stmt, rs);
		}
		return _list;

	}

	public java.util.ArrayList getDisposableReasonsByStatus(String status) {
		String filter = " AND reason_status='" + status + "'";
		java.util.ArrayList _list = getDisposableReasonsByQuery(filter);
		return _list;
	}

	public com.magbel.ia.legend.admin.objects.DisposableReasons getDisposableReasonsByReasonId(
			String reasonId) {
		String filter = " AND reason_id='" + reasonId + "'";
		com.magbel.ia.legend.admin.objects.DisposableReasons dispres = null;
		java.util.ArrayList _list = getDisposableReasonsByQuery(filter);

		dispres = (com.magbel.ia.legend.admin.objects.DisposableReasons) _list.get(0);
		return dispres;
	}

	public com.magbel.ia.legend.admin.objects.DisposableReasons getDisposableReasonsByReasonCode(
			String reasonCode) {
		String filter = " AND reason_code='" + reasonCode + "'";
		com.magbel.ia.legend.admin.objects.DisposableReasons dispres = null;
		java.util.ArrayList _list = getDisposableReasonsByQuery(filter);
		if (_list != null && _list.size() > 0) {
			dispres = (com.magbel.ia.legend.admin.objects.DisposableReasons) _list.get(0);
		}

		return dispres;
	}

	public boolean createDisposableReasons(
			com.magbel.ia.legend.admin.objects.DisposableReasons dispose) {
		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "INSERT INTO am_ad_disposalReasons(reason_code, "
				+ "description, reason_status, user_id, create_date) "
				+ " VALUES(?, ?, ?, ?, ?)";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			//ps.setLong(1, System.currentTimeMillis());
			ps.setString(1, dispose.getReasonCode());
			ps.setString(2, dispose.getDescription());
			ps.setString(3, dispose.getReasonStatus());
			ps.setString(4, dispose.getUserId());
			ps.setDate(5, df.dateConvert(dispose.getCreateDate()));

			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;
	}

	public boolean updateDisposableReasons(
			com.magbel.ia.legend.admin.objects.DisposableReasons dispose) {
		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "UPDATE am_ad_disposalReasons SET reason_code=?, description=?, "
				+ "reason_status=?, user_id=?"
				+ " WHERE reason_id=?";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);

			ps.setString(1, dispose.getReasonCode());
			ps.setString(2, dispose.getDescription());
			ps.setString(3, dispose.getReasonStatus());
			ps.setString(4, dispose.getUserId());
			//ps.setDate(5, df.dateConvert(dispose.getCreateDate()));
			ps.setString(5, dispose.getReasonId());

			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;
	}

	public java.util.ArrayList getDriverByStatus(String status) {
		String filter = " WHERE Driver_Status='" + status + "'";
		java.util.ArrayList _list = getDriverByQuery(filter);
		return _list;
	}

	public java.util.List getCategoryClassByStatus(String filter) {
		String filt = " and Class_Status = '" + filter + "'";
		java.util.List _list = getCategoryClassByQuery(filt);
		return _list;
	}


    public String getEmailStatus(String mailCode){
String  branch="";

            String query = "SELECT status FROM am_mail_statement where mail_code = '"+mailCode+"'";


            Connection c = null;
            ResultSet rs = null;
            Statement s = null;
            try
            {
                c = getConnection();
                s = c.createStatement();
                for(rs = s.executeQuery(query); rs.next();)
                {
                   branch = rs.getString("status");


                }

            }
            catch(Exception e)
            {
                System.out.println("CompanyHandler:getEmailStatus:Erorr getting mail status "+ e );

            }
            finally
            {
                closeConnection(c, s, rs);
            }
            return branch;
}//getEmailStatus(String brachCode)

//Joshua's update
public com.magbel.ia.legend.admin.objects.AssetManagerInfo getAssetManagerInfoFed() {
	com.magbel.ia.legend.admin.objects.AssetManagerInfo ami = null;
		String query = "SELECT  Processing_Date, Processing_Frequency, Next_Processing_Date, Default_Branch"
				+ ", Branch_Name,Suspense_Acct, Auto_Generate_Id, Residual_Value"
				+ ", Depreciation_Method, Vat_Account, Wht_Account, Fed_Wht_Account"
				+ ", PL_Disposal_Account, PLD_Status, Vat_Acct_Status, Wht_Acct_Status, Fed_wht_acct_status"
				+ ", Suspense_Ac_Status,Sbu_Required, Sbu_Level,system_date,asset_acq_ac,LPO_Required,Barcode_Fld,Cost_Threshold,defer_account,Trans_Threshold, part_pay_acct"
                + ", asset_acq_status, asset_defer_status, part_pay_status"
				+ " FROM AM_GB_COMPANY";

		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String processingDate = sdf.format(rs
						.getDate("Processing_Date"));
				String processingFrequency = rs
						.getString("Processing_Frequency");
				String nextProcessingDate = sdf.format(rs
						.getDate("Next_Processing_Date"));
				String defaultBranch = rs.getString("Default_Branch");
				String branchName = rs.getString("Branch_Name");
				String suspenseAcct = rs.getString("Suspense_Acct");
				String autoGenId = rs.getString("Auto_Generate_Id");
				String residualValue = rs.getString("Residual_Value");
				String depreciationMethod = rs.getString("Depreciation_Method");
				String vatAccount = rs.getString("Vat_Account");
				String whtAccount = rs.getString("Wht_Account");
				String fedWhtAccount=rs.getString("Fed_Wht_Account");//ganiyu's code
				String PLDisposalAccount = rs.getString("PL_Disposal_Account");
				String PLDStatus = rs.getString("PLD_Status");
				String vatAcctStatus = rs.getString("Vat_Acct_Status");
				String whtAcctStatus = rs.getString("Wht_Acct_Status");
				String suspenseAcctStatus = rs.getString("Suspense_Ac_Status");
				String sbuRequired = rs.getString("Sbu_Required");
				String sbuLevel = rs.getString("Sbu_Level");
				String sysDate = sdf.format(rs.getDate("system_date"));
				String assetSuspenseAcct = rs.getString("asset_acq_ac");
				String fedWhtAcctStatus = rs.getString("Fed_wht_acct_status");
                String lpo_r = rs.getString("LPO_Required");
                String bar_code_r = rs.getString("Barcode_Fld");
              double cp_threshold = rs.getDouble("Cost_Threshold");
              String defer_account =rs.getString("defer_account");
              double tr_threshold = rs.getDouble("Trans_Threshold");
              String part_pay_acct = rs.getString("part_pay_acct");
              String asset_acq_status=rs.getString("asset_acq_status");
              String asset_defer_status=rs.getString("asset_defer_status");
               String part_pay_status=rs.getString("part_pay_status");

				ami = new com.magbel.ia.legend.admin.objects.AssetManagerInfo(processingDate,
						processingFrequency, nextProcessingDate, defaultBranch,
						branchName, suspenseAcct, autoGenId, residualValue,
						depreciationMethod, vatAccount, whtAccount, fedWhtAccount,
						PLDisposalAccount, PLDStatus, vatAcctStatus,
						whtAcctStatus, fedWhtAcctStatus, suspenseAcctStatus, sbuRequired,
						sbuLevel,lpo_r,bar_code_r,cp_threshold);
				ami.setSysDate(sysDate);
				ami.setAssetSuspenseAcct(assetSuspenseAcct);
                ami.setDeferAccount(defer_account);
                ami.setTrans_threshold(tr_threshold);
                ami.setPart_pay(part_pay_acct);
                ami.setAsset_acq_status(asset_acq_status);
                ami.setAsset_defer_status(asset_defer_status);
                ami.setPart_pay_status(part_pay_status);
			}

		} catch (Exception e) {

            System.out.println("######ERROR OCCURED IN getAssetManagerInfoFed() OF COMPANYHANDLER CLASS");
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return ami;
	}

 public String[] getEmailStatusAndName(String tran_type){
String[]  branch= new String[2];

            String query = "SELECT status, mail_code FROM am_mail_statement where transaction_type = '"+tran_type+"'";


            Connection c = null;
            ResultSet rs = null;
            Statement s = null;
            try
            {
                c = getConnection();
                s = c.createStatement();
                for(rs = s.executeQuery(query); rs.next();)
                {
                   branch[0] = rs.getString("status");
                   branch[1] = rs.getString("mail_code");


                }

            }
            catch(Exception e)
            {
                System.out.println("CompanyHandler:getEmailStatusAndName:Erorr getting mail status "+ e );

            }
            finally
            {
                closeConnection(c, s, rs);
            }
            return branch;
}//getEmailStatus(String brachCode)
 public  String getGeneratedTransId()
 {



	    int counter = 0;
	    String tableDate="";
	    String presentDate="";
		final String FINDER_QUERY = "SELECT MT_ID + 1,MT_DATE FROM IA_TRANSID_TABLE " ;
		final String UPDATE_QUERY = "UPDATE IA_TRANSID_TABLE SET MT_ID = 1 , MT_DATE= getdate() ";
		final String UPDATE = "UPDATE IA_TRANSID_TABLE SET MT_ID =MT_ID + 1  ";
		String id = "";

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try{

			con = getConnection();
			ps = con.prepareStatement(FINDER_QUERY);
			rs = ps.executeQuery();

			while(rs.next())
			{
				tableDate= rs.getString(2);
				//System.out.println("-----> "+tableDate);
				tableDate=tableDate.substring(0, 10);
				presentDate = String.valueOf(df.dateConvert(new java.util.Date()));
				presentDate = presentDate.substring(0, 10);
				//System.out.println("-----> "+presentDate);
				counter=rs.getInt(1);
			}
				if(tableDate.equals(presentDate))
					{
 			          ps = con.prepareStatement(UPDATE);
				 	  ps.execute();
					}
				else{
					 counter=1;
					 ps = con.prepareStatement(UPDATE_QUERY);
			 	     ps.execute();
				    }



		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			closeConnection(con,ps,rs);
		}

		 id = Integer.toString(counter);
		 System.out.println(">>-->--> "+id);
		return id;

	   }

public java.util.ArrayList getSqlRecords()
 {
 	java.util.ArrayList _list = new java.util.ArrayList();
 	String date = String.valueOf(df.dateConvert(new java.util.Date()));
 	System.out.println("====Server Date-----> "+date);
 	date = date.substring(0, 10);
 	String finacleTransId= null;
		String query = " SELECT  finacle_Trans_Id from am_raisentry_transaction where transaction_date >'"+date+"' and iso<>'000' ";

	Connection c = null;
	ResultSet rs = null;
	Statement s = null;

	try {
		    c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next())
			   {
				String finacle_Trans_Id = rs.getString("finacle_Trans_Id");
				_list.add(finacle_Trans_Id);
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
public String getFinacleRecords(String finacleTransId)
 {
 	String date = String.valueOf(df.dateConvert(new java.util.Date()));
 	date = date.substring(0, 10);
	System.out.println("System Date in getFinacleRecords====> "+date+"====finacleTransId==>> "+finacleTransId);
 	String iso ="";  
		String query = " SELECT  tran_particular_2 from fix_tb " +
				"where tran_particular_2='"+finacleTransId+"' and to_char(tran_date,'DD-MM-YYYY') >= '"+getOracleDateFormat(date)+"'";
		System.out.println("Query on getFinacleRecords====> "+query);
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
private Connection getConnectionFinacle() {
	Connection con = null;
	dc = new DataConnect("custom");
	try {
		con = dc.getConnection();
	} catch (Exception e) {
		System.out.println("WARNING: Error getting connection ->"
				+ e.getMessage());
	}
	return con;
}


}
