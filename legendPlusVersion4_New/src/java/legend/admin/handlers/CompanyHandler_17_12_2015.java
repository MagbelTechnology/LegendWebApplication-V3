package legend.admin.handlers; 

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.vao.ViewAssetDetails;
import com.magbel.legend.vao.newAssetTransaction;
import com.magbel.util.ApplicationHelper;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;

import com.magbel.util.DataConnect;

import java.sql.SQLException;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import magma.DateManipulations;
import magma.asset.dto.Asset;
import magma.asset.dto.Improvement;
import magma.net.dao.MagmaDBConnection;
import magma.net.manager.AssetPaymentManager;
import magma.net.manager.FleetHistoryManager;
//import magma.net.manager.AssetPaymentManager;
//import magma.net.manager.FleetHistoryManager;
import magma.util.Codes;
/**
 * @author Rahman Oloritun
 * @Updated by Lekan Matanmi
 * @Entities company,AssetmanagerInfo,Driver,Location,categoryClasses, ASSETMAKE
 */
public class CompanyHandler_17_12_2015 {
	Connection con = null;
	
	private MagmaDBConnection dbConnection;
		
	Statement stmt = null;
 
	PreparedStatement ps = null;

	ResultSet rs = null;

	DataConnect dc;
  
	SimpleDateFormat sdf;
   
	final String space = "  ";

	final String comma = ",";  
   
	java.util.Date date;
	newAssetTransaction newassettrans;
	//com.magbel.util.DatetimeFormat df;

	public CompanyHandler_17_12_2015() {

		sdf = new SimpleDateFormat("dd-MM-yyyy");
//		df = new com.magbel.util.DatetimeFormat();
		newassettrans = new newAssetTransaction();
		System.out.println("USING_ " + this.getClass().getName());
	}
    public String computeTotalLife(String depRate) {

        String totalLife = "0";
        if (depRate == null || depRate.equals("")) {
            depRate = "0.0";
        }

        double division = 100 / (Double.parseDouble(depRate));
        int intTotal = (int) (division * 12);

        totalLife = Integer.toString(intTotal);

        return totalLife;
        
    }

    private String getDepreciationRate(String category_code) throws Exception {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
       // System.out.println("category_code:  "+category_code);
        String rate = "0.0";
        String query = "SELECT DEP_RATE FROM AM_AD_CATEGORY " +
                       "WHERE CATEGORY_CODE = " + category_code;
      //  System.out.println("getDepreciationRate query :  "+query);
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                rate = rs.getString(1);
            }

        } catch (Exception ex) {
            System.out.println("WARN: Error fetching DepreciationRate ->" + ex);
        } finally {
           closeConnection(con, ps);
        }
      //  System.out.println("getDepreciationRate Rate:  "+rate);
        return rate;
    }

	public legend.admin.objects.Company getCompany() {
		legend.admin.objects.Company company = null;
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

				company = new legend.admin.objects.Company(companyCode,
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

	public legend.admin.objects.Company getCompanyFed() {
		legend.admin.objects.Company company = null;
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
             //   	System.out.println("**********************comp_delimiter**********************"+comp_delimiter);

				if (comp_delimiter == null)
				{
					comp_delimiter= "";
				}

                //String fedWhtAcctStatus rs.getString("Fed_wht_acct_status");
				

				//to do give a condition for federal or state and use the value for whtRate

				company = new legend.admin.objects.Company(companyCode,
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


	public legend.admin.objects.Company getCompanyFed1() {
		legend.admin.objects.Company company = null;
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
         //       	System.out.println("**********************comp_delimiter**********************"+comp_delimiter);

				if (comp_delimiter == null)
				{
					comp_delimiter= "";
				}

                //String fedWhtAcctStatus rs.getString("Fed_wht_acct_status");


				//to do give a condition for federal or state and use the value for whtRate

				company = new legend.admin.objects.Company(companyCode,
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



public legend.admin.objects.AssetManagerInfo getAssetManagerInfo() {
		legend.admin.objects.AssetManagerInfo ami = null;
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
				ami = new legend.admin.objects.AssetManagerInfo(processingDate,
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
	public boolean createCompany(legend.admin.objects.Company company) {

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
			ps.setDate(7, dateConvert(company.getFinancialStartDate()));
			ps.setInt(8, company.getFinancialNoOfMonths());
			ps.setDate(9, dateConvert(company.getFinancialEndDate()));
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
	//		System.out.println("=========================checking if success"+done);

		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

	public boolean updateCompany(legend.admin.objects.Company company) {

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
			ps.setDate(6, dateConvert(company.getFinancialStartDate()));
			ps.setInt(7, company.getFinancialNoOfMonths());
			ps.setDate(8, dateConvert(company.getFinancialEndDate()));
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
    //        System.out.print("update am_gb_company 1...>>>>>>>>>>>"+company.getComp_delimiter());
                        ps.setString(20,company.getPassword_upper());
                        ps.setString(21,company.getPassword_lower());
                        ps.setString(22,company.getPassword_numeric());
                        ps.setString(23,company.getPassword_special());
                        ps.setInt(24, company.getPasswordLimit());
                        ps.setString(25, company.getCompanyCode());
  //      	  System.out.print("update am_gb_company 2...>>>>>>>>>>>"+company.getCompanyCode());

            done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

	public boolean updateAssetManagerInfo(legend.admin.objects.AssetManagerInfo ami) {

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
                 +"asset_acq_status=?, asset_defer_status=?, part_pay_status=?,THIRDPARTY_REQUIRE=?";

       
  //      System.out.println("HERE ##################### " );

       // String query = "update am_gb_company set cost_threshold =?";
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			         //System.out.println("the query is############ " + query);
                     //ps.setDouble(1,ami.getCp_threshold());
          
			ps.setDate(1, dateConvert(ami.getProcessingDate()));
			ps.setString(2, ami.getProcessingFrequency());
			ps.setDate(3, dateConvert(ami.getNextProcessingDate()));
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
            ps.setString(31, ami.getThirdpartytransaction());                
    //        System.out.println("The content of ps is################### "+ ps.toString());
            int psResult = ps.executeUpdate();
	//		System.out.println("The content of ps is################### "+ ps.toString());
			done = (psResult != -1);
  //          System.out.println("outcome of update psResult is ########## "+ psResult);


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
		legend.admin.objects.Driver driver = null;
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

				driver = new legend.admin.objects.Driver();
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
		legend.admin.objects.Driver driver = null;
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

				driver = new legend.admin.objects.Driver();
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

	public legend.admin.objects.Driver getDriverByDriverID(String driverid) {

		legend.admin.objects.Driver driver = null;
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

				driver = new legend.admin.objects.Driver();
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

	public legend.admin.objects.Driver getDriverByDriverCode(String driverid) {

		legend.admin.objects.Driver driver = null;
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

				driver = new legend.admin.objects.Driver();
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

	public boolean createDriver(legend.admin.objects.Driver ccode) {

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
			ps.setDate(3, dateConvert(ccode.getDlIssueDate()));
			ps.setDate(4, dateConvert(ccode.getDlExpiryDate()));
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
			ps.setDate(18, dateConvert(new java.util.Date()));
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

	public boolean updateDriver(legend.admin.objects.Driver ccode) {

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
			ps.setDate(3, dateConvert(ccode.getDlIssueDate()));
			ps.setDate(4, dateConvert(ccode.getDlExpiryDate()));
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
		legend.admin.objects.Locations location = null;
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

				location = new legend.admin.objects.Locations();
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
		legend.admin.objects.Locations location = null;

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

				location = new legend.admin.objects.Locations();
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

	public legend.admin.objects.Locations getLocationByLocID(String LocID) {
		legend.admin.objects.Locations location = null;
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

				location = new legend.admin.objects.Locations();
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



    public boolean createLocation(legend.admin.objects.Locations location) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "INSERT INTO AM_GB_LOCATION( Location_Code, Location"
				+ ", Status, User_Id, Create_Date,location_id)" + " VALUES (?,?,?,?,?,?)";
			int mtid = Integer.parseInt(new ApplicationHelper().getGeneratedId("AM_GB_LOCATION"));
		//	System.out.println("################ the mtid value is " + mtid);
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			// ps.setLong(1, System.currentTimeMillis());
			ps.setString(1, location.getLocationCode());
			ps.setString(2, location.getLocation());
			ps.setString(3, location.getStatus());
			ps.setString(4, location.getUserId());
			ps.setDate(5, dateConvert(new java.util.Date()));
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

	public java.util.ArrayList getAllCategoryClass() {
		java.util.ArrayList _list = new java.util.ArrayList();
		legend.admin.objects.CategoryClass catclass = null;
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

				catclass = new legend.admin.objects.CategoryClass();
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
		legend.admin.objects.CategoryClass catclass = null;

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

				catclass = new legend.admin.objects.CategoryClass();
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

	public legend.admin.objects.CategoryClass getCategoryClassByClassID(
			String CatClassID) {
		legend.admin.objects.CategoryClass catclass = null;
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

				catclass = new legend.admin.objects.CategoryClass();
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
			legend.admin.objects.CategoryClass catgclass) {

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
			ps.setDate(5, dateConvert(new java.util.Date()));
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
			legend.admin.objects.CategoryClass catgclass) {

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

	public legend.admin.objects.CategoryClass getCategoryClassByClassCode(
			String CatClassCode) {

		legend.admin.objects.CategoryClass catclass = null;
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

				catclass = new legend.admin.objects.CategoryClass();
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

	private legend.admin.objects.AssetMake getAnAssetMake(String filter) {
		legend.admin.objects.AssetMake am = null;
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
				am = new legend.admin.objects.AssetMake(assetMakeId,
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
		legend.admin.objects.AssetMake am = null;
		java.util.List<legend.admin.objects.AssetMake> _list = new java.util.ArrayList<legend.admin.objects.AssetMake>();
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
				am = new legend.admin.objects.AssetMake(assetMakeId,
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

	public legend.admin.objects.AssetMake getAssetMakeByID(String id) {
		String filter = " WHERE AssetMake_ID = " + id;
		legend.admin.objects.AssetMake am = getAnAssetMake(filter);
		return am;

	}

	public legend.admin.objects.AssetMake getAssetMakeByCode(String code) {
		String filter = " WHERE AssetMake_Code = '" + code + "'";
		legend.admin.objects.AssetMake am = getAnAssetMake(filter);
		return am;

	}

	public boolean createAssetMake(legend.admin.objects.AssetMake am) {

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
			ps.setDate(6, dateConvert(new java.util.Date()));
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

	public boolean updateAssetMake(legend.admin.objects.AssetMake am) {

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
		legend.admin.objects.DisposableReasons dispres = null;

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

				dispres = new legend.admin.objects.DisposableReasons();
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

	public legend.admin.objects.DisposableReasons getDisposableReasonsByReasonId(
			String reasonId) {
		String filter = " AND reason_id='" + reasonId + "'";
		legend.admin.objects.DisposableReasons dispres = null;
		java.util.ArrayList _list = getDisposableReasonsByQuery(filter);

		dispres = (legend.admin.objects.DisposableReasons) _list.get(0);
		return dispres;
	}

	public legend.admin.objects.DisposableReasons getDisposableReasonsByReasonCode(
			String reasonCode) {
		String filter = " AND reason_code='" + reasonCode + "'";
		legend.admin.objects.DisposableReasons dispres = null;
		java.util.ArrayList _list = getDisposableReasonsByQuery(filter);
		if (_list != null && _list.size() > 0) {
			dispres = (legend.admin.objects.DisposableReasons) _list.get(0);
		}

		return dispres;
	}

	public boolean createDisposableReasons(
			legend.admin.objects.DisposableReasons dispose) {
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
			ps.setDate(5, dateConvert(dispose.getCreateDate()));

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
			legend.admin.objects.DisposableReasons dispose) {
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
public legend.admin.objects.AssetManagerInfo getAssetManagerInfoFed() {
		legend.admin.objects.AssetManagerInfo ami = null;
		String query = "SELECT  Processing_Date, Processing_Frequency, Next_Processing_Date, Default_Branch"
				+ ", Branch_Name,Suspense_Acct, Auto_Generate_Id, Residual_Value"
				+ ", Depreciation_Method, Vat_Account, Wht_Account, Fed_Wht_Account"
				+ ", PL_Disposal_Account, PLD_Status, Vat_Acct_Status, Wht_Acct_Status, Fed_wht_acct_status"
				+ ", Suspense_Ac_Status,Sbu_Required, Sbu_Level,system_date,asset_acq_ac" 
				+", LPO_Required,Barcode_Fld,Cost_Threshold,defer_account,Trans_Threshold, part_pay_acct,THIRDPARTY_REQUIRE"
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
               String thirdpartytransaction=rs.getString("THIRDPARTY_REQUIRE");
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
                ami.setTrans_threshold(tr_threshold);
                ami.setPart_pay(part_pay_acct);
                ami.setAsset_acq_status(asset_acq_status);
                ami.setAsset_defer_status(asset_defer_status);
                ami.setPart_pay_status(part_pay_status);
                ami.setThirdpartytransaction(thirdpartytransaction);
                
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
          //  System.out.println("getEmailStatusAndName query: "+query);
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
				presentDate = String.valueOf(dateConvert(new java.util.Date()));
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
		// System.out.println(">>-->--> "+id);
		return id;

	   }

public java.util.ArrayList getSqlRecords()
 {
 	java.util.ArrayList _list = new java.util.ArrayList();
 	String date = String.valueOf(dateConvert(new java.util.Date()));
 	//System.out.println("====Server Date-----> "+date);
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
 	String date = String.valueOf(dateConvert(new java.util.Date()));
 	date = date.substring(0, 10);
//	System.out.println("System Date in getFinacleRecords====> "+date+"====finacleTransId==>> "+finacleTransId);
 	String iso ="";    
		String query = " SELECT  tran_particular_2 from custom.fix_tb " +
				"where tran_particular_2='"+finacleTransId+"' and to_char(tran_date,'DD-MM-YYYY') >= '"+getOracleDateFormat(date)+"'";
	//	System.out.println("Query on getFinacleRecords====> "+query);
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
		String date = String.valueOf(dateConvert(new java.util.Date()));
	//	System.out.println("=====updateSqlRecords iso=====> "+iso);		
	//	System.out.println("=====updateSqlRecords date=====> "+date);
	//	System.out.println("=====updateSqlRecords finacleTransId=====> "+finacleTransId);		
	 	date = date.substring(0, 10);
		String query = "UPDATE am_raisentry_transaction SET iso=?   where transaction_date >'"+date+"' and finacle_Trans_Id=? ";
	//	System.out.println("=====updateSqlRecords query=====> "+query);
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

public java.util.ArrayList getNewAssetSqlRecords()
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	//System.out.println("====eschool Server Date-----> "+date);
	date = date.substring(0, 10);
	String finacleTransId= null;
		String query = " SELECT INTEGRIFY_ID,Description,Registration_No,Vendor_AC,Date_purchased,Asset_Make,Asset_Model," +
				"Asset_Serial_No,Asset_Engine_No,Supplier_Name,Asset_User,Asset_Maintenance,Cost_Price,Authorized_By," +
				"Wh_Tax,Posting_Date,Effective_Date,Purchase_Reason,Subject_TO_Vat,Asset_Status,State,Driver,User_ID," +
				"BRANCH_CODE,SECTION_CODE,DEPT_CODE,CATEGORY_CODE,BAR_CODE,SBU_CODE,LPO,INVOICE_NO,supervisor,POSTED,ASSET_ID," +
				"ASSET_CODE,ERROR_MESSAGE,ASSET_TYPE,Wh_Tax_Value,QUANTITY,TRAN_TYPE,Location,Spare_1,Spare_2,Multiple,Memo,MemoValue from NEW_ASSET_INTERFACE WHERE POSTED = 'N' ";
//	Transaction transaction = null;
//		System.out.println("====getNewAssetSqlRecords query-----> "+query);
	Connection c = null;
	ResultSet rs = null;
	Statement s = null;

	try {
		    c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next())
			   {				
				String strintegrifyId = rs.getString("INTEGRIFY_ID");
				String strDescription = rs.getString("Description");
				String strRegistrationNo = rs.getString("Registration_No");
				String strVendorAC = rs.getString("Vendor_AC");
				String strDatepurchased = rs.getString("Date_purchased");
				String strAssetMake = rs.getString("Asset_Make");
				String strAssetModel = rs.getString("Asset_Model");
				String strAssetSerialNo = rs.getString("Asset_Serial_No");
				String strAssetEngineNo = rs.getString("Asset_Engine_No");
				String strSupplierName = rs.getString("Supplier_Name");
				String strAssetUser = rs.getString("Asset_User");
				String strAssetMaintenance = rs.getString("Asset_Maintenance");
				double strCostPrice = rs.getDouble("Cost_Price");
				String strAuthorizedBy = rs.getString("Authorized_By");
				String strWhTax = rs.getString("Wh_Tax");
				String strPostingDate = rs.getString("Posting_Date");
				String strEffectiveDate = rs.getString("Effective_Date");
				String strPurchaseReason = rs.getString("Purchase_Reason");
				String strSubjectTOVat = rs.getString("Subject_TO_Vat");
				String strAssetStatus = rs.getString("Asset_Status");
				String strState = rs.getString("State");
				String strDriver = rs.getString("Driver");
				String strUserID = rs.getString("User_ID");
				String strbranchCode = rs.getString("BRANCH_CODE");
				String strsectionCode = rs.getString("SECTION_CODE");
				String strdeptCode = rs.getString("DEPT_CODE");
				String strcategoryCode = rs.getString("CATEGORY_CODE");
				String strbarCode = rs.getString("BAR_CODE");
				String strsbuCode = rs.getString("SBU_CODE");
				String strlpo = rs.getString("LPO");
				String strinvoiceNo = rs.getString("INVOICE_NO");
				String strsupervisor = rs.getString("supervisor");
				String Strposted = rs.getString("POSTED");
				String StrassetId = rs.getString("ASSET_ID");
				String StrassetCode = rs.getString("ASSET_CODE");
				String Strerrormessage = rs.getString("ERROR_MESSAGE");
				String Strassettype = rs.getString("ASSET_TYPE");
				String Strtrantype = rs.getString("TRAN_TYPE");
				double StrwhTaxValue = rs.getDouble("Wh_Tax_Value");
				int Strnoofitems = rs.getInt("QUANTITY");
				String Strlocation = rs.getString("Location");
				String Strspare1 = rs.getString("Spare_1");
				String Strspare2 = rs.getString("Spare_2");
				String Strmultiple = rs.getString("Multiple");
				String Strmemo = rs.getString("Memo");
				String Strmemovalue = rs.getString("MemoValue");
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setIntegrifyId(strintegrifyId);
				newTransaction.setDescription(strDescription);
				newTransaction.setRegistrationNo(strRegistrationNo);
				newTransaction.setVendorAC(strVendorAC);
				newTransaction.setDatepurchased(strDatepurchased);
				newTransaction.setAssetMake(strAssetMake);
				newTransaction.setAssetModel(strAssetModel);
				newTransaction.setAssetSerialNo(strAssetSerialNo);
				newTransaction.setAssetEngineNo(strAssetEngineNo);
				newTransaction.setSupplierName(strSupplierName);
				newTransaction.setAssetUser(strAssetUser);				
				newTransaction.setAssetMaintenance(strAssetMaintenance);
				newTransaction.setCostPrice(strCostPrice);
				newTransaction.setAuthorizedBy(strAuthorizedBy);
				newTransaction.setWhTax(strWhTax);
				newTransaction.setPostingDate(strPostingDate);
				newTransaction.setEffectiveDate(strEffectiveDate);
				newTransaction.setPurchaseReason(strPurchaseReason);
				newTransaction.setSubjectTOVat(strSubjectTOVat);
				newTransaction.setAssetStatus(strAssetStatus);
				newTransaction.setState(strState);
				newTransaction.setDriver(strDriver);				
				newTransaction.setUserID(strUserID);			
				newTransaction.setBranchCode(strbranchCode);
				newTransaction.setSectionCode(strsectionCode);
				newTransaction.setDeptCode(strdeptCode);
				newTransaction.setCategoryCode(strcategoryCode);
				newTransaction.setBarCode(strbarCode);
				newTransaction.setSbuCode(strsbuCode);
				newTransaction.setLpo(strlpo);
				newTransaction.setInvoiceNo(strinvoiceNo);
				newTransaction.setSupervisor(strsupervisor);
				newTransaction.setPosted(Strposted);
				newTransaction.setAssetId(StrassetId);
				newTransaction.setAssetCode(StrassetCode);
				newTransaction.setErrormessage(Strerrormessage);
				newTransaction.setAssetType(Strassettype);
				newTransaction.setWhTaxValue(StrwhTaxValue);
				newTransaction.setTranType(Strtrantype);
				newTransaction.setNoofitems(Strnoofitems);
				newTransaction.setLocation(Strlocation);
				newTransaction.setSpare1(Strspare1);
				newTransaction.setSpare2(Strspare2);
				newTransaction.setMemo(Strmemo);
				newTransaction.setMemovalue(Strmemovalue);
				newTransaction.setMultiple(Strmultiple);
				_list.add(newTransaction);
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
public java.util.ArrayList getGroupDetailrecords(String integrifyId)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	//System.out.println("====eschool Server Date-----> "+date);
	date = date.substring(0, 10);
	String finacleTransId= null;
		String query = " SELECT INTEGRIFY_ID,Description,Registration_No,Vendor_AC,Date_purchased,Asset_Make,Asset_Model," +
				"Asset_Serial_No,Asset_Engine_No,Supplier_Name,Asset_User,Asset_Maintenance,Cost_Price,Authorized_By," +
				"Wh_Tax,Posting_Date,Effective_Date,Purchase_Reason,Subject_TO_Vat,Asset_Status,State,Driver,User_ID," +
				"BRANCH_CODE,SECTION_CODE,DEPT_CODE,CATEGORY_CODE,BAR_CODE,SBU_CODE,LPO,INVOICE_NO,supervisor,POSTED,ASSET_ID," +
				"ASSET_CODE,ERROR_MESSAGE,ASSET_TYPE,Wh_Tax_Value,ITEMCOUNT,Location,Spare_1,Spare_2,Multiple,Memo,MemoValue from NEW_GROUP_ASSET_INTERFACE WHERE POSTED = 'N' AND TRAN_TYPE = 'N' AND INTEGRIFY_ID = '"+integrifyId+"' ";
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
				String strintegrifyId = rs.getString("INTEGRIFY_ID");
				String strDescription = rs.getString("Description");
				String strRegistrationNo = rs.getString("Registration_No");
				String strVendorAC = rs.getString("Vendor_AC");
				String strDatepurchased = rs.getString("Date_purchased");
				String strAssetMake = rs.getString("Asset_Make");
				String strAssetModel = rs.getString("Asset_Model");
				String strAssetSerialNo = rs.getString("Asset_Serial_No");
				String strAssetEngineNo = rs.getString("Asset_Engine_No");
				String strSupplierName = rs.getString("Supplier_Name");
				String strAssetUser = rs.getString("Asset_User");
				String strAssetMaintenance = rs.getString("Asset_Maintenance");
				double strCostPrice = rs.getDouble("Cost_Price");
				String strAuthorizedBy = rs.getString("Authorized_By");
				String strWhTax = rs.getString("Wh_Tax");
				String strPostingDate = rs.getString("Posting_Date");
				String strEffectiveDate = rs.getString("Effective_Date");
				String strPurchaseReason = rs.getString("Purchase_Reason");
				String strSubjectTOVat = rs.getString("Subject_TO_Vat");
				String strAssetStatus = rs.getString("Asset_Status");
				String strState = rs.getString("State");
				String strDriver = rs.getString("Driver");
				String strUserID = rs.getString("User_ID");
				String strbranchCode = rs.getString("BRANCH_CODE");
				String strsectionCode = rs.getString("SECTION_CODE");
				String strdeptCode = rs.getString("DEPT_CODE");
				String strcategoryCode = rs.getString("CATEGORY_CODE");
				String strbarCode = rs.getString("BAR_CODE");
				String strsbuCode = rs.getString("SBU_CODE");
				String strlpo = rs.getString("LPO");
				String strinvoiceNo = rs.getString("INVOICE_NO");
				String strsupervisor = rs.getString("supervisor");
				String Strposted = rs.getString("POSTED");
				String StrassetId = rs.getString("ASSET_ID");
				String StrassetCode = rs.getString("ASSET_CODE");
				String Strerrormessage = rs.getString("ERROR_MESSAGE");
				String Strassettype = rs.getString("ASSET_TYPE");
				double StrwhTaxValue = rs.getDouble("Wh_Tax_Value");
				int recno = rs.getInt("ITEMCOUNT");
				String Strlocation = rs.getString("Location");
				String Strspare1 = rs.getString("Spare_1");
				String Strspare2 = rs.getString("Spare_2");
				String Strmultiple = rs.getString("Multiple");
				String Strmemo = rs.getString("Memo");
				String Strmemovalue = rs.getString("MemoValue");				
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setIntegrifyId(strintegrifyId);
				newTransaction.setDescription(strDescription);
				newTransaction.setRegistrationNo(strRegistrationNo);
				newTransaction.setVendorAC(strVendorAC);
				newTransaction.setDatepurchased(strDatepurchased);
				newTransaction.setAssetMake(strAssetMake);
				newTransaction.setAssetModel(strAssetModel);
				newTransaction.setAssetSerialNo(strAssetSerialNo);
				newTransaction.setAssetEngineNo(strAssetEngineNo);
				newTransaction.setSupplierName(strSupplierName);
				newTransaction.setAssetUser(strAssetUser);				
				newTransaction.setAssetMaintenance(strAssetMaintenance);
				newTransaction.setCostPrice(strCostPrice);
				newTransaction.setAuthorizedBy(strAuthorizedBy);
				newTransaction.setWhTax(strWhTax);
				newTransaction.setPostingDate(strPostingDate);
				newTransaction.setEffectiveDate(strEffectiveDate);
				newTransaction.setPurchaseReason(strPurchaseReason);
				newTransaction.setSubjectTOVat(strSubjectTOVat);
				newTransaction.setAssetStatus(strAssetStatus);
				newTransaction.setState(strState);
				newTransaction.setDriver(strDriver);				
				newTransaction.setUserID(strUserID);			
				newTransaction.setBranchCode(strbranchCode);
				newTransaction.setSectionCode(strsectionCode);
				newTransaction.setDeptCode(strdeptCode);
				newTransaction.setCategoryCode(strcategoryCode);
				newTransaction.setBarCode(strbarCode);
				newTransaction.setSbuCode(strsbuCode);
				newTransaction.setLpo(strlpo);
				newTransaction.setInvoiceNo(strinvoiceNo);
				newTransaction.setSupervisor(strsupervisor);
				newTransaction.setPosted(Strposted);
				newTransaction.setAssetId(StrassetId);
				newTransaction.setAssetCode(StrassetCode);
				newTransaction.setErrormessage(Strerrormessage);
				newTransaction.setAssetType(Strassettype);
				newTransaction.setWhTaxValue(StrwhTaxValue);
				newTransaction.setNoofitems(recno);
				newTransaction.setLocation(Strlocation);
				newTransaction.setSpare1(Strspare1);
				newTransaction.setSpare2(Strspare2);
				newTransaction.setMemo(Strmemo);
				newTransaction.setMemovalue(Strmemovalue);
				newTransaction.setMultiple(Strmultiple);
				_list.add(newTransaction);
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

public boolean chkidExists(String assetid,String assettype) {
    boolean exists = false;
    Connection con = null;
    String SQLC = "";
    String SQLU = "";
    PreparedStatement ps = null;
    ResultSet rs = null;
    if(assettype.equalsIgnoreCase("C")){
    	SQLC = "SELECT * FROM am_asset WHERE asset_id='" + assetid + "'";
    }
    if(assettype.equalsIgnoreCase("U")){
       SQLU = "SELECT * FROM AM_ASSET_UNCAPITALIZED WHERE asset_id='" + assetid + "'";
    }
    try {
        //con = dbConnection.getConnection("fixedasset");
    	con = getConnection();
    	if(assettype.equalsIgnoreCase("C")){
        ps = con.prepareStatement(SQLC);
    	}
    	if(assettype.equalsIgnoreCase("U")){
            ps = con.prepareStatement(SQLU);
        	}    	
        rs = ps.executeQuery();
        exists = rs.next();

    } catch (Exception ex) {
        System.out.println("WARN: Error isDepreciationReCalculatable ->" +
                           ex);
    } finally {
    	closeConnection(con, ps);
    }

    return exists;

}

private boolean rinsertAssetRecord(String integrifyId,String Description,String RegistrationNo,String VendorAC,String Datepurchased,
		String AssetMake,String AssetModel,String AssetSerialNo,String AssetEngineNo,String SupplierName,String AssetUser,
		String AssetMaintenance,double CostPrice,double VatableCost,String AuthorizedBy,String WhTax,double whtaxamount,String PostingDate,String EffectiveDate,
		String PurchaseReason,String SubjectTOVat,String AssetStatus,String State,String Driver,String UserID,String branchCode,
		String sectionCode,String deptCode,String categoryCode,String barCode,String sbuCode,String lpo,String invoiceNo,String supervisor,
		String posted,String assetId,String assetCode,String assettype,String branch_id,String dept_id,String section_id,String category_id,double vatamount,String residualvalue,int whtaxvalue,
		String location,String memovalue,String memo, String spare1,String spare2,String multiple) throws Exception, Throwable {
	
//	String location = "";
	String vat_amount = "0.0";
	String vatable_cost = "0.0";
	String wh_tax_amount = "0";
	String province = "0";
	String noOfMonths = "0";
	String residual_value = "0";
	String warrantyStartDate = null;   
	String expiryDate = null;
//	String memo = "N";
//	String memoValue = "0";    
//	double costPrice =  newassettrans.getCostPrice();
	String amountPTD = "0.0";
//	String integrify = newassettrans.getIntegrifyId();
//	System.out.println("Branch Code:    "+branchCode+"  deptCode: "+deptCode+"  sectionCode: "+sectionCode+"  categoryCode:  "+categoryCode);
    String asset_id = getIdentity(branch_id,
    		dept_id, section_id, category_id);
//    String branch_id = findObject("SELECT BRANCH_ID FROM am_ad_branch WHERE BRANCH_CODE = '"+branchCode+"'");
//    String dept_id = findObject("SELECT DEPT_ID FROM am_ad_department WHERE DEPT_CODE = '"+branchCode+"'");
//    String section_id = findObject("SELECT SECTION_ID FROM am_ad_section WHERE SECTION_CODE = '"+sectionCode+"'");
//    String category_id = findObject("SELECT CATEGORY_ID FROM am_ad_category WHERE CATEGORY_CODE = '"+categoryCode+"'");
    Connection con = null;
    PreparedStatement ps = null;
    boolean done = true;   

    if (AssetMake == null || AssetMake.equals("")) {
    	AssetMake = "0";
    }
    if (AssetMaintenance == null || AssetMaintenance.equals("")) {
    	AssetMaintenance = "0";
    }
    if (SupplierName == null || SupplierName.equals("")) {
    	SupplierName = "0";
    }
    if (AssetUser == null || AssetUser.equals("")) {
    	AssetUser = "";
    }
    if (location == null || location.equals("")) {
        location = "0";
    }
    if (Driver == null || Driver.equals("")) {
    	Driver = "0";
    }
    if (State == null || State.equals("")) {
    	State = "0";
    }
    if (deptCode == null || deptCode.equals("")) {
    	deptCode = "0";
    }
    if (vat_amount == null || vat_amount.equals("")) {
        vat_amount = "0.0";
    }
    if (vatable_cost == null || vatable_cost.equals("")) {
        vatable_cost = "0.0";
    }
    if (wh_tax_amount == null || wh_tax_amount.equals("")) {
        wh_tax_amount = "0";
    }
    if (branchCode == null || branchCode.equals("")) {
    	branchCode = "0";
    }
    if (province == null || province.equals("")) {
        province = "0";
    }
    if (categoryCode == null || categoryCode.equals("")) {
    	categoryCode = "0";
    }

    if (residual_value == null || residual_value.equals("")) {
        residual_value = "0";
    }
    if (sectionCode == null || sectionCode.equals("")) {
    	sectionCode = "0";
    }

    if (noOfMonths == null || noOfMonths.equals("")) {
        noOfMonths = "0";
    }
    if (warrantyStartDate == null || warrantyStartDate.equals("")) {
        warrantyStartDate = null;
    }
    if (expiryDate == null || expiryDate.equals("")) {
        expiryDate = null;
    }

    if (memo == null || memo.equals("")) {
    	memo = "N";
	}

/*    if (memoValue == null || memoValue.equals("")) {
    	memoValue = "0";
	}*/
    String strnewDateMonth = "";
	int purchase_start_year = Integer.parseInt(Datepurchased.substring(0,4));
	//System.out.println("purchase start year: "+purchase_start_year+"  Date Purchased:  "+Datepurchased);
	int purchase_start_month = Integer.parseInt(Datepurchased.substring(5,7));
	//System.out.println("purchase start month: "+purchase_start_month+"  Date Purchased:  "+Datepurchased);
	int purchase_start_day = Integer.parseInt(Datepurchased.substring(8,10));
	//System.out.println("purchase start Day: "+purchase_start_day+"  Date Purchased:  "+Datepurchased);
		
	int newDateYear= purchase_start_year;
	//System.out.println("purchase start month: "+purchase_start_month+" Sum: "+purchase_start_month + 1);
	int newDateMonth = purchase_start_month + 1;
//	System.out.println("New month: "+newDateMonth);
	String newDateDay ="01";
	if(newDateMonth == 10){strnewDateMonth = "10";}
	if(newDateMonth == 11){strnewDateMonth = "11";}
	if(newDateMonth == 12){strnewDateMonth = "12";}
  if (newDateMonth > 12)
   { 
     newDateMonth = 01;
     strnewDateMonth = "01";
     newDateYear = newDateYear + 1;
   }
	
	if (newDateMonth < 10)
   { 
		int lengthfld = String.valueOf(newDateMonth).length();
		if(newDateMonth == 7){strnewDateMonth = "0"+"7";}
		if(newDateMonth == 1){strnewDateMonth = "0"+"1";}
		if(newDateMonth == 2){strnewDateMonth = "0"+"2";}
		if(newDateMonth == 3){strnewDateMonth = "0"+"3";}
		if(newDateMonth == 4){strnewDateMonth = "0"+"4";}
		if(newDateMonth == 5){strnewDateMonth = "0"+"5";}
		if(newDateMonth == 6){strnewDateMonth = "0"+"6";}
		if(newDateMonth == 8){strnewDateMonth = "0"+"8";}
		if(newDateMonth == 9){strnewDateMonth = "0"+"9";}
	//	System.out.println("New month2: "+newDateMonth+"  lengthfld:  "+lengthfld);
    // newDateMonth = '0'+newDateMonth;
   //  strnewDateMonth = "0"+String.valueOf(newDateMonth);
  //   System.out.println("New month3: "+String.valueOf(newDateMonth));

    // newDateYear = newDateYear + 1;
   }
	String depreciation_start_date = newDateDay+'-'+strnewDateMonth+'-'+newDateYear;
	//System.out.println("newDateDay: "+newDateDay+"  newDateMonth: "+strnewDateMonth+"  newDateYear:  "+newDateYear);
	//alert('before');
	//getDepEndDate(form);
//	}
	String require_depreciation = "Y";
    String require_redistribution = "N";
    String who_to_remind = "";
    String email_1 = "";
    String who_to_remind_2 = "";
    String email2 = "";
    String raise_entry = "N";    
    String spare_1 = "";
    String status = "";   
    String user_id = "";
//    String multiple = "N";
    String spare_2 = "";
    String partPAY = "N";
    String fullyPAID = "Y";
    String deferPay = "N";
    int selectTax = 0;
    String MacAddress = "";
    String SystemIp = "";
    String section = "";  
    String rate = getDepreciationRate(categoryCode);
 //   System.out.println("spare1 in rinsertAssetRecord : "+spare1+"   spare2:  "+spare2);
  //  System.out.println("Depreciation Date: "+depreciation_start_date+"  rate:  "+rate);
    String vals = rate+","+depreciation_start_date;
  //  System.out.println("Values for Calculating Depreciation Date: "+vals);
    String depreciation_end_date = getDepEndDate(vals);
//	int memoValueS = Integer.parseInt(memoValue);
	//cost_price = cost_price.replaceAll(",", "");
    vat_amount = vat_amount.replaceAll(",", "");
    vatable_cost = vatable_cost.replaceAll(",", "");
    wh_tax_amount = wh_tax_amount.replaceAll(",", "");
    residual_value = residual_value.replaceAll(",", "");
    amountPTD = amountPTD.replaceAll(",","");
    int asset_Code = Integer.parseInt(new ApplicationHelper().getGeneratedId("AM_ASSET"));
//System.out.println("assetCode: "+asset_Code+"   Description:  "+Description+"  costPrice:  "+CostPrice);
//For Capitalized Assets
	String createQueryC = "INSERT INTO AM_ASSET         " +
                         "(" +
                         "ASSET_ID, REGISTRATION_NO, BRANCH_ID, DEPT_ID," +
                         "SECTION_ID, CATEGORY_ID, DESCRIPTION, VENDOR_AC," +
                         "DATE_PURCHASED, DEP_RATE, ASSET_MAKE, ASSET_MODEL," +
                         "ASSET_SERIAL_NO, ASSET_ENGINE_NO, SUPPLIER_NAME," +

                         "ASSET_USER, ASSET_MAINTENANCE, ACCUM_DEP, MONTHLY_DEP," +
                         "COST_PRICE, NBV, DEP_END_DATE, RESIDUAL_VALUE," +
                         "AUTHORIZED_BY, POSTING_DATE, EFFECTIVE_DATE, PURCHASE_REASON," +
                         "USEFUL_LIFE, TOTAL_LIFE, LOCATION, REMAINING_LIFE," +

                         "VATABLE_COST,VAT, WH_TAX, WH_TAX_AMOUNT, REQ_DEPRECIATION," +
                         "REQ_REDISTRIBUTION, SUBJECT_TO_VAT, WHO_TO_REM, EMAIL1," +
                         "WHO_TO_REM_2, EMAIL2, RAISE_ENTRY, DEP_YTD, SECTION," +
                         "STATE, DRIVER, SPARE_1, SPARE_2, ASSET_STATUS, USER_ID," +
                         "MULTIPLE,PROVINCE,WAR_START_DATE,WAR_MONTH,WAR_EXPIRY_DATE," +
                         "BRANCH_CODE,DEPT_CODE,SECTION_CODE,CATEGORY_CODE,	AMOUNT_PTD," +
                         "AMOUNT_REM,PART_PAY,FULLY_PAID,BAR_CODE,SBU_CODE,LPO,supervisor,defer_pay,wht_percent," +
                         "system_ip,mac_address,asset_code,memo,memoValue,INTEGRIFY) " +

                         "VALUES" +
                         "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                         "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

//For Uncapitalized Assets
    String createQueryU = "INSERT INTO AM_ASSET_UNCAPITALIZED      " +
    "(" +
    "ASSET_ID, REGISTRATION_NO, BRANCH_ID, DEPT_ID," +
    "SECTION_ID, CATEGORY_ID, [DESCRIPTION], VENDOR_AC," +
    "DATE_PURCHASED, DEP_RATE, ASSET_MAKE, ASSET_MODEL," +
    "ASSET_SERIAL_NO, ASSET_ENGINE_NO, SUPPLIER_NAME," +

    "ASSET_USER, ASSET_MAINTENANCE, ACCUM_DEP, MONTHLY_DEP," +
    "COST_PRICE, NBV, DEP_END_DATE, RESIDUAL_VALUE," +
    "AUTHORIZED_BY, POSTING_DATE, EFFECTIVE_DATE, PURCHASE_REASON," +
    "USEFUL_LIFE, TOTAL_LIFE, LOCATION, REMAINING_LIFE," +

    "VATABLE_COST,VAT, WH_TAX, WH_TAX_AMOUNT, REQ_DEPRECIATION," +
    "REQ_REDISTRIBUTION, SUBJECT_TO_VAT, WHO_TO_REM, EMAIL1," +
    "WHO_TO_REM_2, EMAIL2, RAISE_ENTRY, DEP_YTD, SECTION," +
    "STATE, DRIVER, SPARE_1, SPARE_2, ASSET_STATUS, USER_ID," +
    "MULTIPLE,PROVINCE,WAR_START_DATE,WAR_MONTH,WAR_EXPIRY_DATE," +
    "BRANCH_CODE,DEPT_CODE,SECTION_CODE,CATEGORY_CODE,	AMOUNT_PTD," +
    "AMOUNT_REM,PART_PAY,FULLY_PAID,BAR_CODE,SBU_CODE,LPO,supervisor,defer_pay,wht_percent," +
    "system_ip,mac_address,asset_code,memo,memoValue,INTEGRIFY) " +

    "VALUES" +
    "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
    "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";



    /*
     *First Create Asset Records
     * and then determine if it
     * should be made available for fleet.
     */
    if (this.chkidExists(asset_id,assettype)) {
        done = false;
        return done;
    }
    try {

//        double costPrice = Double.parseDouble(vat_amount) +
//                           Double.parseDouble(vatable_cost);
//        double costPrice = Double.parseDouble(cost_price);
        con = getConnection();
      //  con = dbConnection.getConnection("fixedasset");
        if(assettype.equalsIgnoreCase("C")){
       // 	System.out.println("assettype in rinsertAssetRecord for C: "+assettype);
          ps = con.prepareStatement(createQueryC);
        }
        if(assettype.equalsIgnoreCase("U")){
        //	System.out.println("assettype in rinsertAssetRecord for U: "+assettype);
           ps = con.prepareStatement(createQueryU);
          // require_depreciation = "N";
        }   
      //  System.out.println("assettype in rinsertAssetRecord: "+assettype);
        ps.setString(1, asset_id);
        ps.setString(2, RegistrationNo);
        ps.setInt(3, Integer.parseInt(branch_id));
        ps.setInt(4, Integer.parseInt(dept_id));
        ps.setInt(5, Integer.parseInt(section_id));
        ps.setInt(6, Integer.parseInt(category_id));
        ps.setString(7, Description);
        ps.setString(8, VendorAC);
        ps.setString(9, Datepurchased);
        ps.setString(10, getDepreciationRate(categoryCode));
        ps.setString(11, AssetMake);
        ps.setString(12, AssetModel);
        ps.setString(13, AssetSerialNo);
        ps.setString(14, AssetEngineNo);
        ps.setInt(15, Integer.parseInt(SupplierName));
        ps.setString(16, AssetUser);
        ps.setInt(17, Integer.parseInt(AssetMaintenance));
        ps.setInt(18, 0);
        ps.setInt(19, 0);
        ps.setDouble(20, CostPrice);
        ps.setDouble(21, CostPrice);
        ps.setDate(22, dateConvert(depreciation_end_date));
        ps.setDouble(23, Double.parseDouble(residualvalue));
        ps.setString(24, AuthorizedBy);
        ps.setTimestamp(25, getDateTime(new java.util.Date()));
        ps.setDate(26, dateConvert(depreciation_start_date));
        ps.setString(27, PurchaseReason);
        ps.setString(28, "0");
        ps.setString(29, computeTotalLife(getDepreciationRate(categoryCode)));
        ps.setInt(30, Integer.parseInt(location));
        ps.setString(31, computeTotalLife(getDepreciationRate(categoryCode)));
        ps.setDouble(32, VatableCost);
        ps.setDouble(33, vatamount);
        ps.setString(34, WhTax);
        ps.setDouble(35, whtaxamount);
        ps.setString(36, require_depreciation);
        ps.setString(37, require_redistribution);
        ps.setString(38, SubjectTOVat);
        ps.setString(39, who_to_remind);
        ps.setString(40, email_1);
        ps.setString(41, who_to_remind_2);
        ps.setString(42, email2);
        ps.setString(43, raise_entry);
        ps.setString(44, "0");
        ps.setString(45, sectionCode);
        ps.setInt(46, Integer.parseInt(State));
        ps.setInt(47, Integer.parseInt(Driver));
        ps.setString(48, spare1);
        ps.setString(49, spare2);
        ps.setString(50, AssetStatus);
        ps.setString(51, UserID);
        ps.setString(52, multiple);
        ps.setString(53, province);
        ps.setDate(54, dateConvert(warrantyStartDate));
        ps.setInt(55, new Integer(noOfMonths).intValue());
        ps.setDate(56, dateConvert(expiryDate));
        ps.setString(57, branchCode);
        ps.setString(58, deptCode);
        ps.setString(59, sectionCode);
        ps.setString(60, categoryCode);
        ps.setDouble(61, Double.parseDouble(amountPTD));
        ps.setDouble(62, VatableCost);
        ps.setString(63, partPAY);
        ps.setString(64, fullyPAID);
        ps.setString(65, barCode);
        ps.setString(66,sbuCode);
        ps.setString(67, lpo);
        ps.setString(68,supervisor);
        ps.setString(69, deferPay);
        ps.setInt(70, whtaxvalue);
        ps.setString(71, SystemIp);
        ps.setString(72,MacAddress);
        ps.setInt(73,asset_Code);
        ps.setString(74, memo); 
        ps.setString(75, memovalue);
        ps.setString(76, integrifyId);
        ps.execute(); 
    //     System.out.println("createQueryC: "+createQueryC);
        rinsertAssetRecord2(integrifyId,Description,RegistrationNo,VendorAC,Datepurchased,
        		AssetMake,AssetModel,AssetSerialNo,AssetEngineNo,SupplierName,AssetUser,
        		AssetMaintenance,CostPrice,VatableCost,AuthorizedBy,WhTax,whtaxamount,PostingDate,EffectiveDate,
        		PurchaseReason,SubjectTOVat,AssetStatus,State,Driver,UserID,branchCode,require_depreciation,
        		sectionCode,deptCode,categoryCode,barCode,sbuCode,lpo,invoiceNo,supervisor,
        		posted,asset_id,asset_Code,assettype,branch_id,dept_id,section_id,depreciation_end_date,
        		section,spare_1,spare_2,status,partPAY,fullyPAID,
        		deferPay,SystemIp,MacAddress,who_to_remind,email_1,who_to_remind_2,email2,raise_entry,
        		noOfMonths,warrantyStartDate,expiryDate,category_id,vatamount,residual_value,whtaxvalue,
        		location,memovalue,memo,spare1,spare2,multiple);


    } catch (Exception ex) {
        done = false;
        System.out.println("WARN:Error creating asset in rinsertAssetRecord->" + ex);
    } finally {
    	closeConnection(con, ps, rs);
    }

    return done;
}
public boolean chkBudgetAllocation(String quarter, double values[],
        boolean cf,double CostPrice) {
	//String branch_id = newassettrans.getBranchCode();
double cost_price = CostPrice;
boolean allocation = true;
double result = 0.00;
if (cf) {
if (quarter.equalsIgnoreCase("FIRST")) {
result = values[0] -
(values[1] + CostPrice);
} else if (quarter.equalsIgnoreCase("2ND")) {
result = (values[0] + values[2]) -
(values[1] + values[3] +
		CostPrice);
} else if (quarter.equalsIgnoreCase("3RD")) {
result = (values[0] + values[2] + values[4]) -
(values[1] + values[3] + values[5] +
		CostPrice);
} else if (quarter.equalsIgnoreCase("4TH")) {
result = (values[0] + values[2] + values[4] + values[6]) -
(values[1] + values[3] + values[5] + values[7] +
		CostPrice);
}    

} else {
if (quarter.equalsIgnoreCase("FIRST")) {
result = values[0] -
(values[1] +
		CostPrice);
} else if (quarter.equalsIgnoreCase("2ND")) {
result = values[2] -
(values[3] +
		CostPrice);
} else if (quarter.equalsIgnoreCase("3RD")) {
result = values[4] -
(values[5] +
		CostPrice);
} else if (quarter.equalsIgnoreCase("4TH")) {
result = values[6] -
(values[7] +
		CostPrice);
}

}

if (result < 0) {
allocation = false;
}
return allocation;
} 
public void updateBudget(String quarter, String[] bugdetinfo,String categoryCode,String branchCode,double CostPrice ) {

    String fisdate = "";
    int finomonth = 0;
    String fiedate = "";
    Connection conn = getConnection(); 
    Statement stmt = null;
    try {
        stmt = conn.createStatement();
        if (quarter.equalsIgnoreCase("FIRST")) {
            String budgetUpdate1 = "UPDATE AM_ACQUISITION_BUDGET "
                                   +
                                   " SET Q1_ACTUAL = (Q1_ACTUAL + " +
                                   CostPrice +
                                   ") WHERE BRANCH_ID='" +
                                   branchCode +
                                   "' AND CATEGORY='" + categoryCode +
                                   "' AND ACC_START_DATE='" +
                                   dateConvert(bugdetinfo[0])
                                   + "' AND ACC_END_DATE='" +
                                   dateConvert(bugdetinfo[2]) +
                                   "'";
           // System.out.println(budgetUpdate1);
            stmt.executeUpdate(budgetUpdate1);
           // System.out.println("Updated 1st Quarter");
        } else if (quarter.equalsIgnoreCase("2ND")) {
            String budgetUpdate1 = "UPDATE AM_ACQUISITION_BUDGET "
                                   +
                                   " SET Q2_ACTUAL = (Q2_ACTUAL + " +
                                   CostPrice +
                                   ") WHERE BRANCH_ID='" +
                                   branchCode +
                                   "' AND CATEGORY='" + categoryCode +
                                   "' AND ACC_START_DATE='" +
                                   dateConvert(bugdetinfo[0])
                                   + "' AND ACC_END_DATE='" +
                                   dateConvert(bugdetinfo[2]) +
                                   "'";

            //System.out.println(budgetUpdate1);

            stmt.executeUpdate(budgetUpdate1);
            //System.out.println("Updated 2nd Quarter");
        } else if (quarter.equalsIgnoreCase("3RD")) {
            String budgetUpdate1 = "UPDATE AM_ACQUISITION_BUDGET "
                                   +
                                   " SET Q3_ACTUAL =(Q3_ACTUAL + " +
                                   CostPrice +
                                   ") WHERE BRANCH_ID='" +
                                   branchCode +
                                   "' AND CATEGORY='" + categoryCode +
                                   "' AND ACC_START_DATE='" +
                                   dateConvert(bugdetinfo[0])
                                   + "' AND ACC_END_DATE='" +
                                   dateConvert(bugdetinfo[2]) +
                                   "'";

            //System.out.println(budgetUpdate1);

            stmt.executeUpdate(budgetUpdate1);

            //System.out.println("Updated 3rd Quarter");
        } else if (quarter.equalsIgnoreCase("4TH")) {
            String budgetUpdate1 = "UPDATE AM_ACQUISITION_BUDGET "
                                   +
                                   " SET Q4_ACTUAL = (Q4_ACTUAL + " +
                                   CostPrice +
                                   ") WHERE BRANCH_ID='" +
                                   branchCode +
                                   "' AND CATEGORY='" + categoryCode +
                                   "' AND ACC_START_DATE='" +
                                   dateConvert(bugdetinfo[0])
                                   + "' AND ACC_END_DATE='" +
                                   dateConvert(bugdetinfo[2]) +
                                   "'";

            //System.out.println(budgetUpdate1);

            stmt.executeUpdate(budgetUpdate1);

            //System.out.println("Updated 4th Quarter");
        }

    } catch (Exception ex) {
        System.out.println("ERROR_ " + this.getClass().getName() + "---" +
                           ex.getMessage() + "--");
        ex.printStackTrace();
    } finally {
        //freeResource();
       closeConnection(conn, stmt);
    }
    //System.out.println(
           // "Exiting update of Aquicisition Budget due to Asset Creation");
}


public int insertAssetRecord(String integrifyId,String Description,String RegistrationNo,String VendorAC,String Datepurchased,
		String AssetMake,String AssetModel,String AssetSerialNo,String AssetEngineNo,String SupplierName,String AssetUser,
		String AssetMaintenance,double CostPrice,double VatableCost,String AuthorizedBy,String WhTax,double whtaxamount,String PostingDate,String EffectiveDate,
		String PurchaseReason,String SubjectTOVat,String AssetStatus,String State,String Driver,String UserID,String branchCode,
		String sectionCode,String deptCode,String categoryCode,String barCode,String sbuCode,String lpo,String invoiceNo,String supervisor,
		String posted,String assetId,String assetCode,String assettype,String branch_id,String dept_id,String section_id,String category_id,double vatamount,
		String residualvalue,int whtaxvalue,String location,String memovalue,String memo, String spare1,String spare2,String multiple) throws Exception, Throwable {
//	System.out.println("integrifyId in insertAssetRecord>>>>>> "+integrifyId+"  sectionCode:  "+sectionCode);
//	System.out.println("insertAssetRecord Purchase Date>>>>>>  "+Datepurchased);
    String[] budget = getBudgetInfo();
    double[] bugdetvalues = getBudgetValues(branchCode,categoryCode);
    int DONE = 0; //everything is oK
    int BUDGETENFORCED = 1; //EF budget = Yes, CF = NO, ERROR_FLAG
    int BUDGETENFORCEDCF = 2; //EF budget = Yes, CF = Yes, ERROR_FLAG
    int ASSETPURCHASEBD = 3; //asset falls into no quarter purchase date older than bugdet
    String Q = getQuarter(Datepurchased);
    if(budget[3].equalsIgnoreCase("N")){
		rinsertAssetRecord(integrifyId,Description,RegistrationNo,VendorAC,Datepurchased,
				AssetMake,AssetModel,AssetSerialNo,AssetEngineNo,SupplierName,AssetUser,AssetMaintenance,
				CostPrice,VatableCost,AuthorizedBy,WhTax,whtaxamount,PostingDate,EffectiveDate,PurchaseReason,SubjectTOVat,AssetStatus,
				State,Driver,UserID,branchCode,sectionCode,deptCode,categoryCode,barCode,sbuCode,lpo,invoiceNo,
				supervisor,posted,assetId,assetCode,assettype,branch_id,dept_id,section_id,category_id,vatamount,residualvalue,whtaxvalue,
				location,memovalue,memo,spare1,spare2,multiple);
		return DONE;
	}
	else if(budget[3].equalsIgnoreCase("Y")){
    if (!Q.equalsIgnoreCase("NQ")) {
        if (budget[3].equalsIgnoreCase("Y") &&
            budget[4].equalsIgnoreCase("N")) {
            if (chkBudgetAllocation(Q, bugdetvalues, false,CostPrice)) {
                updateBudget(Q, budget,categoryCode,branchCode,CostPrice);
                rinsertAssetRecord(integrifyId,Description,RegistrationNo,VendorAC,Datepurchased,
        				AssetMake,AssetModel,AssetSerialNo,AssetEngineNo,SupplierName,AssetUser,AssetMaintenance,
        				CostPrice,VatableCost,AuthorizedBy,WhTax,whtaxamount,PostingDate,EffectiveDate,PurchaseReason,SubjectTOVat,AssetStatus,
        				State,Driver,UserID,branchCode,sectionCode,deptCode,categoryCode,barCode,sbuCode,lpo,invoiceNo,
        				supervisor,posted,assetId,assetCode,assettype,branch_id,dept_id,section_id,category_id,vatamount,residualvalue,whtaxvalue,
        				location,memovalue,memo,spare1,spare2, multiple);
                return DONE;
            } else {
                return BUDGETENFORCED;
            }

        } else if (budget[3].equalsIgnoreCase("Y") &&
                   budget[4].equalsIgnoreCase("Y")) {
            if (chkBudgetAllocation(Q, bugdetvalues, true,CostPrice)) {
                updateBudget(Q, budget,categoryCode,branchCode,CostPrice);
                rinsertAssetRecord(integrifyId,Description,RegistrationNo,VendorAC,Datepurchased,
        				AssetMake,AssetModel,AssetSerialNo,AssetEngineNo,SupplierName,AssetUser,AssetMaintenance,
        				CostPrice,VatableCost,AuthorizedBy,WhTax,whtaxamount,PostingDate,EffectiveDate,PurchaseReason,SubjectTOVat,AssetStatus,
        				State,Driver,UserID,branchCode,sectionCode,deptCode,categoryCode,barCode,sbuCode,lpo,invoiceNo,
        				supervisor,posted,assetId,assetCode,assettype,branch_id,dept_id,section_id,category_id,vatamount,residualvalue,whtaxvalue,
        				location,memovalue,memo,spare1,spare2,multiple);
                return DONE;
            } else {
                return BUDGETENFORCEDCF;
            }

        } else {
            rinsertAssetRecord(integrifyId,Description,RegistrationNo,VendorAC,Datepurchased,
    				AssetMake,AssetModel,AssetSerialNo,AssetEngineNo,SupplierName,AssetUser,AssetMaintenance,
    				CostPrice,VatableCost,AuthorizedBy,WhTax,whtaxamount,PostingDate,EffectiveDate,PurchaseReason,SubjectTOVat,AssetStatus,
    				State,Driver,UserID,branchCode,sectionCode,deptCode,categoryCode,barCode,sbuCode,lpo,invoiceNo,
    				supervisor,posted,assetId,assetCode,assettype,branch_id,dept_id,section_id,category_id,vatamount,residualvalue,whtaxvalue,
    				location,memovalue,memo,spare1,spare2,multiple);
            return DONE;
        }
    } else {
        //rinsertAssetRecord();
        return ASSETPURCHASEBD;
    }}
    return 0;
}

public String[] getBudgetInfo() {
    String[] result = new String[5];

    String query = " SELECT financial_start_date,financial_no_ofmonths"
                   +
                   ",financial_end_date,enforce_acq_budget,quarterly_surplus_cf"
                   + " FROM am_gb_company";
//System.out.println("query for getBudgetInfo: "+query);
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
        //con = dbConnection.getConnection("fixedasset");
    	con = getConnection();
        ps = con.prepareStatement(query);
        rs = ps.executeQuery();
        while (rs.next()) {
            result[0] = sdf.format(rs.getDate("financial_start_date"));
           // System.out.println("financial_start_date: "+result[0]);
            result[1] = rs.getString("financial_no_ofmonths");
          //  System.out.println("financial_no_ofmonths: "+result[1]);
            result[2] = sdf.format(rs.getDate("financial_end_date"));
            result[3] = rs.getString("enforce_acq_budget");
            result[4] = rs.getString("quarterly_surplus_cf");

        }
    } catch (Exception e) {
        String warning = "WARNING:Error Fetching Company Details" +
                         " ->" + e.getMessage();
        System.out.println(warning);
    } finally {
        closeConnection(con, ps, rs);
    }

    return result;
}

public double[] getBudgetValues(String branchcode, String categorycode) {
    double[] result = new double[8];

    String query = " SELECT Q1_ALLOCATION,Q1_ACTUAL,Q2_ALLOCATION"
                   +
                   ",Q2_ACTUAL,Q3_ALLOCATION,Q3_ACTUAL,Q4_ALLOCATION,Q4_ACTUAL"
                   + " FROM AM_ACQUISITION_BUDGET WHERE CATEGORY='" +
                   categorycode + "' AND "
                   + " BRANCH_ID='" + branchcode + "'";

    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
        //con = dbConnection.getConnection("fixedasset");
    	con = getConnection();
        ps = con.prepareStatement(query);
        rs = ps.executeQuery();
        while (rs.next()) {
            result[0] = rs.getDouble("Q1_ALLOCATION");
            result[1] = rs.getDouble("Q1_ACTUAL");
            result[2] = rs.getDouble("Q2_ALLOCATION");
            result[3] = rs.getDouble("Q2_ACTUAL");
            result[4] = rs.getDouble("Q3_ALLOCATION");
            result[5] = rs.getDouble("Q3_ACTUAL");
            result[6] = rs.getDouble("Q4_ALLOCATION");
            result[7] = rs.getDouble("Q4_ACTUAL");
            //result[4] = rs.getDouble("quarterly_surplus_cf");


        }
    } catch (Exception e) {
        String warning = "WARNING:Error Fetching Budget Acquisition  Details" +
                         " ->" + e.getMessage();
        System.out.println(warning);
    } finally {        
        closeConnection(con, ps, rs);
    }

    return result;
}

public String getQuarter(String purchasedate) {
 //   System.out.println("getting quarters");
    String quarter = "NQ";
    String[] budg = getBudgetInfo();
    //System.out.println("fsdate  " + budg[0]);
    ///System.out.println("pdate  " + date_of_purchase);
    double q1 = (double) (Double.parseDouble(budg[1]) / 4);
  //  System.out.println("getQuarter Purchase Date>>>>>>  "+purchasedate);
    int month = (int) getDayDifference(purchasedate, budg[0]) /
                30;
    //System.out.println("pdate  " + date_of_purchase);
    //System.out.println("financial start and pdate diff months " + month);
    boolean btw = isDateBetween(budg[0], budg[2],
    		purchasedate);
    if (btw) {
        if ((double) month <= q1) {
            quarter = "FIRST";
           // System.out.println("1st Quarter");
        } else if ((double) month > q1 && (double) month <= (q1 * 2.0)) {
            quarter = "2ND";
            //System.out.println("2nd Quarter");
        } else if ((double) month > (q1 * 2.0) &&
                   (double) month <= (q1 * 3.0)) {
            quarter = "3RD";
            //System.out.println("3rd Quarter");
        } else if (month > (q1 * 3.0)) {
            quarter = "4TH";
            //System.out.println("4th Quarter");
        }

    }
    //System.out.println("the assets quarter is  " + quarter);
    return quarter;

}
public static String getDepEndDate(String vals) {

    String endDate = "ERROR";

    if (vals != null) {

        StringTokenizer st1 = new StringTokenizer(vals, ",");
        if (st1.countTokens() == 2) {

            String s1 = st1.nextToken();
            String s2 = st1.nextToken();

            Float rate = Float.parseFloat(s1);
            if (s2 != null) {
                s2 = s2.replaceAll("/", "-");
            }

            StringTokenizer st2 = new StringTokenizer(s2, "-");
            if (st2.countTokens() == 3) {
                String day = st2.nextToken();
                String month = st2.nextToken();
                String year = st2.nextToken();

                if ((year.length() == 4) && (day.length() > 0) &&
                    (day.length() < 3) && (month.length() > 0) &&
                    (month.length() < 3)) {
                    Calendar c = new GregorianCalendar(Integer.parseInt(
                            year),
                            Integer.parseInt(month) - 1,
                            Integer.parseInt(day) - 1);

                    int months = (int) (100 / rate * 12);
                    c.add(Calendar.MONTH, months);
                    // c.add(Calendar.DAY_OF_YEAR, -1*Integer.parseInt(day));

                    int endDay = c.get(Calendar.DAY_OF_MONTH);
                    int endMonth = c.get(Calendar.MONTH) + 1;
                    int endYear = c.get(Calendar.YEAR);

                    endDate = endDay + "-" + endMonth + "-" + endYear;
                }

            }
        }
    }

    return endDate;
}

public String getBranchCode(String BranchId)
{
    String query =
           "SELECT BRANCH_CODE  FROM am_ad_branch  " +
           "WHERE BRANCH_ID = '" + BranchId + "' ";

      Connection con = null;
      ResultSet rs = null;
      Statement stmt = null;
   String branchcode = "0";
   try {
       con=getConnection();
       stmt = con.createStatement();
       rs = stmt.executeQuery(query);
       while (rs.next()) {

           branchcode = rs.getString(1);

       }

   } catch (Exception ex) {
       ex.printStackTrace();
   } finally {
       closeConnection(con,stmt,rs);
   }

   return branchcode;

}
public String getDeptCode(String DeptId)
{
   String query =
          "SELECT DEPT_CODE  FROM am_ad_department  " +
          "WHERE DEPT_ID = '" + DeptId + "' ";

     Connection con = null;
     ResultSet rs = null;
     Statement stmt = null;
  String deptcode = "0";
  try {
      con=getConnection();
      stmt = con.createStatement();
      rs = stmt.executeQuery(query);
      while (rs.next()) {

          deptcode = rs.getString(1);

      }

  } catch (Exception ex) {
      ex.printStackTrace();
  } finally {
      closeConnection(con,stmt,rs);
  }

  return deptcode;

}
public String getSectionCode(String SectionId)
 {
     String query =
            "SELECT SECTION_CODE  FROM am_ad_section  " +
            "WHERE SECTION_ID = '" + SectionId + "' ";

       Connection con = null;
       ResultSet rs = null;
       Statement stmt = null;
    String sectioncode = "0";
    try {
        con=getConnection();
        stmt = con.createStatement();
        rs = stmt.executeQuery(query);
        while (rs.next()) {

            sectioncode = rs.getString(1);
     
        }

    } catch (Exception ex) {
        ex.printStackTrace();
    } finally {
        closeConnection(con,stmt,rs);
    }

    return sectioncode;
  
 }
 public String getCategoryCode(String categoryId)
     {
         String query =
             "SELECT CATEGORY_CODE  FROM am_ad_category  " +
            "WHERE category_id = '" + categoryId + "' ";

           Connection con = null;
           ResultSet rs = null;
           Statement stmt = null;
        String categorycode = "0";
        try {
            con=getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {

                categorycode = rs.getString(1);

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(con,stmt,rs);
        }

        return categorycode;
     
 }   

 public int getDayDifference(String strFirstDate, String strSecondDate) {

     int daysDifference = 0;
  //   System.out.println("strFirstDate>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>   "+strFirstDate);
  //   System.out.println("strSecondDate>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>   "+strSecondDate);
     long dayDiffMills = getDateDiffrenceMillis(strFirstDate, strSecondDate);
 //    System.out.println("dayDiffMills>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>   "+dayDiffMills);
     daysDifference = (int) (Math.abs(dayDiffMills) / (24 * 60 * 60 * 1000));
 //    System.out.println("daysDifference>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>   "+daysDifference);
     return daysDifference;
        
 }  
 public int getMonthDifference(String strFirstDate, String strSecondDate) {

     int monthDifference = 0;
     long yearDiffMills = getDateDiffrenceMillis(strFirstDate, strSecondDate);
     monthDifference = (int) (Math.abs(yearDiffMills)*12) /(365 * 24 * 60 * 60 * 1000);

     return monthDifference;

 }

 public int getYearDifference(String strFirstDate, String strSecondDate) {

     int yearDifference = 0;
     long yearDiffMills = getDateDiffrenceMillis(strFirstDate, strSecondDate);
     yearDifference = (int) (Math.abs(yearDiffMills) /
                             (365 * 24 * 60 * 60 * 1000));

     return yearDifference;

 }
 public long getDateDiffrenceMillis(String strFirstDate,
         String strSecondDate) {

long dateDifferencesMills = 0;
strFirstDate = reArrangeDate(strFirstDate);
try {
Date firstDate = sdf.parse(strFirstDate);
Date secondDate = sdf.parse(strSecondDate);

dateDifferencesMills = firstDate.
     getTime() -
     secondDate.getTime();
} catch (Exception e) {
System.out.println("WARNING:Error finding Date - days different :" +
 e);
}
return dateDifferencesMills;

} 

 public String reArrangeDate(String date){
     String re="";
     String year =date.substring(0,4);
     String month =date.substring(5,7);
     String day = date.substring(8,10);
     re = day +"-"+month+"-"+year;
     return re;
     }//reArrangeDate()

 public java.sql.Date dateConvert(String strDate) {
	// System.out.println("dateConvert strDate:  "+strDate);
     if (strDate == null) {
         strDate = sdf.format(new java.util.Date());
     }
     strDate = strDate.replaceAll("/", "-");
     java.util.Date inputDate = null;
     try {
         inputDate = sdf.parse(strDate);
     } catch (Exception e) {
         System.out.println("WARNING: Error formating Date:" + e.getMessage());
     }
     return this.getSQLFormatedDate(inputDate);

 }
 
 public java.sql.Date getSQLFormatedDate(java.util.Date tDate) {

     java.text.DateFormat formatter = java.text.DateFormat.getDateInstance(
             java.text.DateFormat.SHORT, Locale.ENGLISH);

     String dDate = formatter.format(tDate);
     String strDate = dDate.replaceAll("/", "-");

     int year = Integer.parseInt(strDate.substring(strDate.lastIndexOf("-") +
             1, strDate.length()));
     int mon = Integer.parseInt(strDate.substring(0, strDate.indexOf("-")));
     int day = Integer.parseInt(strDate.substring(strDate.indexOf("-") + 1,
             strDate.lastIndexOf("-")));
     String strDay = "";
     String strMon = "";
     if (year < 1000) {
         year = year + 2000;
     }
     if (mon < 10) {
         strMon = "0" + Integer.toString(mon);
     } else {
         strMon = Integer.toString(mon);
     }
     if (day < 10) {
         strDay = "0" + Integer.toString(day);
     } else {
         strDay = Integer.toString(day);
     }

     strDate = strDay + "-" + strMon + "-" + Integer.toString(year);
     java.sql.Date formatedDate = null;
     try {
         formatedDate = new java.sql.Date(sdf.parse(strDate).getTime());
     } catch (Exception ee) {}
     return formatedDate;
 }
 public boolean isDateBetween(String date1, String date2, String testDate) {
     java.util.Date d1 = null;
     java.util.Date d2 = null;
     java.util.Date test = null;
     boolean isYes = false;
     try {
         d1 = sdf.parse(date1.replaceAll("/", "-"));
         d2 = sdf.parse(date2.replaceAll("/", "-"));
         test = sdf.parse(testDate.replaceAll("/", "-"));
         if ((test.after(d1) && test.before(d2)) ||
             (test.equals(d1) || test.equals(d2))) {
             isYes = true;
         }
     } catch (ParseException ex) {
         System.out.println(this.getClass().getName() + "--" + ex.getMessage());
     }

     return isYes;
 }
	public java.sql.Timestamp getDateTime(java.util.Date inputDate){

		String strDate = null;
		try{
		if(inputDate == null){
			strDate = sdf.format(new java.util.Date());
			}else{
				strDate =  sdf.format(inputDate);
			}
		}catch(Exception er){
			System.out.println("WARN : Error getting datetime ->"+er);
		}

		return getDateTime(strDate);
} 
	
    public java.sql.Timestamp getDateTime(String inputDate){

		java.sql.Timestamp inputTime = null;

		try{

			if(inputDate == null){inputDate = sdf.format(new java.util.Date());}
			inputDate = inputDate.replaceAll("/","-");

			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss.SSS");
			String strDate = dateFormat.format(new java.util.Date());
			String transInputDate = inputDate + strDate.substring(10,strDate.length());

			inputTime = new java.sql.Timestamp((dateFormat.parse(transInputDate)).getTime());

		}catch(Exception er){
			System.out.println("WARN : Error getting datetime ->"+er);
		}

			return inputTime;
		}

    public String getIdentity(String bra, String dep, String sec, String cat) throws
            Throwable {
        StringBuffer sb = new StringBuffer(100);

        String v1 = "", v2 = "", v3 = "", v4 = "", v5 = "", v6 = "", v7 = "",
                dl = "";
        int curr = 0;
        String identity = "";

        ResultSet rsa = getStatement().executeQuery(
                "select * from am_ad_auto_identity");
        ResultSet rsb = getStatement().executeQuery(
                "select * from am_ad_cart_identity");

        ResultSet rs1 = getStatement().executeQuery(
                "select acronym from am_gb_company");
        ResultSet rs2 = getStatement().executeQuery(
                "select group_acronym from am_ad_group");
        ResultSet rs3 = getStatement().executeQuery(
                "select region_acronym from am_ad_region where region_id = (select region from am_ad_branch where branch_id = '" +
                bra + "')");
        ResultSet rs4 = getStatement().executeQuery(
                "select branch_acronym from am_ad_branch where branch_id = '" +
                bra + "'");
        ResultSet rs5 = getStatement().executeQuery(
                "select dept_acronym from am_ad_department where dept_id = '" +
                dep + "'");
        ResultSet rs6 = getStatement().executeQuery(
                "select section_acronym from am_ad_section where section_id = '" +
                sec + "'");
        ResultSet rs7 = getStatement().executeQuery(
                "select category_acronym from am_ad_category where category_id = '" +
                cat + "'");
        ResultSet rs8 = getStatement().executeQuery(
                "select cart_cr from am_ad_cart_identity where cart_id = '" +
                cat + "'");
        ResultSet rs9 = getStatement().executeQuery(
                "select sequ_cr from am_ad_sequ_identity");

        if (rsa.next()) {
            if (rsa.getString(1).equals("COMP")) {
                if (rs1.next()) {
                    v1 = rs1.getString(1);
                } else {
                    v1 = "";
                }
            } else if (rsa.getString(1).equals("GRPP")) {
                if (rs2.next()) {
                    v1 = rs2.getString(1);
                } else {
                    v1 = "";
                }
            } else if (rsa.getString(1).equals("REGN")) {
                if (rs3.next()) {
                    v1 = rs3.getString(1);
                } else {
                    v1 = "";
                }
            } else if (rsa.getString(1).equals("BRCH")) {
                if (rs4.next()) {
                    v1 = rs4.getString(1);
                } else {
                    v1 = "";
                }
            } else if (rsa.getString(1).equals("DEPT")) {
                if (rs5.next()) {
                    v1 = rs5.getString(1);
                } else {
                    v1 = "";
                }
            } else if (rsa.getString(1).equals("SECT")) {
                if (rs6.next()) {
                    v1 = rs6.getString(1);
                } else {
                    v1 = "";
                }
            } else if (rsa.getString(1).equals("CATG")) {
                if (rs7.next()) {
                    v1 = rs7.getString(1);
                } else {
                    v1 = "";
                }
            } else {
                v1 = rsa.getString(1);
            }

            if (rsa.getString(2).equals("COMP")) {
                if (rs1.next()) {
                    v2 = rs1.getString(1);
                } else {
                    v2 = "";
                }
            } else if (rsa.getString(2).equals("GRPP")) {
                if (rs2.next()) {
                    v2 = rs2.getString(1);
                } else {
                    v2 = "";
                }
            } else if (rsa.getString(2).equals("REGN")) {
                if (rs3.next()) {
                    v2 = rs3.getString(1);
                } else {
                    v2 = "";
                }
            } else if (rsa.getString(2).equals("BRCH")) {
                if (rs4.next()) {
                    v2 = rs4.getString(1);
                } else {
                    v2 = "";
                }
            } else if (rsa.getString(2).equals("DEPT")) {
                if (rs5.next()) {
                    v2 = rs5.getString(1);
                } else {
                    v2 = "";
                }
            } else if (rsa.getString(2).equals("SECT")) {
                if (rs6.next()) {
                    v2 = rs6.getString(1);
                } else {
                    v2 = "";
                }
            } else if (rsa.getString(2).equals("CATG")) {
                if (rs7.next()) {
                    v2 = rs7.getString(1);
                } else {
                    v2 = "";
                }
            } else {
                v2 = rsa.getString(2);
            }

            if (rsa.getString(3).equals("COMP")) {
                if (rs1.next()) {
                    v3 = rs1.getString(1);
                } else {
                    v3 = "";
                }
            } else if (rsa.getString(3).equals("GRPP")) {
                if (rs2.next()) {
                    v3 = rs2.getString(1);
                } else {
                    v3 = "";
                }
            } else if (rsa.getString(3).equals("REGN")) {
                if (rs3.next()) {
                    v3 = rs3.getString(1);
                } else {
                    v3 = "";
                }
            } else if (rsa.getString(3).equals("BRCH")) {
                if (rs4.next()) {
                    v3 = rs4.getString(1);
                } else {
                    v3 = "";
                }
            } else if (rsa.getString(3).equals("DEPT")) {
                if (rs5.next()) {
                    v3 = rs5.getString(1);
                } else {
                    v3 = "";
                }
            } else if (rsa.getString(3).equals("SECT")) {
                if (rs6.next()) {
                    v3 = rs6.getString(1);
                } else {
                    v3 = "";
                }
            } else if (rsa.getString(3).equals("CATG")) {
                if (rs7.next()) {
                    v3 = rs7.getString(1);
                } else {
                    v3 = "";
                }
            } else {
                v3 = rsa.getString(3);
            }

            if (rsa.getString(4).equals("COMP")) {
                if (rs1.next()) {
                    v4 = rs1.getString(1);
                } else {
                    v4 = "";
                }
            } else if (rsa.getString(4).equals("GRPP")) {
                if (rs2.next()) {
                    v4 = rs2.getString(1);
                } else {
                    v4 = "";
                }
            } else if (rsa.getString(4).equals("REGN")) {
                if (rs3.next()) {
                    v4 = rs3.getString(1);
                } else {
                    v4 = "";
                }
            } else if (rsa.getString(4).equals("BRCH")) {
                if (rs4.next()) {
                    v4 = rs4.getString(1);
                } else {
                    v4 = "";
                }
            } else if (rsa.getString(4).equals("DEPT")) {
                if (rs5.next()) {
                    v4 = rs5.getString(1);
                } else {
                    v4 = "";
                }
            } else if (rsa.getString(4).equals("SECT")) {
                if (rs6.next()) {
                    v4 = rs6.getString(1);
                } else {
                    v4 = "";
                }
            } else if (rsa.getString(4).equals("CATG")) {
                if (rs7.next()) {
                    v4 = rs7.getString(1);
                } else {
                    v4 = "";
                }
            } else {
                v4 = rsa.getString(4);
            }

            if (rsa.getString(5).equals("COMP")) {
                if (rs1.next()) {
                    v5 = rs1.getString(1);
                } else {
                    v5 = "";
                }
            } else if (rsa.getString(5).equals("GRPP")) {
                if (rs2.next()) {
                    v5 = rs2.getString(1);
                } else {
                    v5 = "";
                }
            } else if (rsa.getString(5).equals("REGN")) {
                if (rs3.next()) {
                    v5 = rs3.getString(1);
                } else {
                    v5 = "";
                }
            } else if (rsa.getString(5).equals("BRCH")) {
                if (rs4.next()) {
                    v5 = rs4.getString(1);
                } else {
                    v5 = "";
                }
            } else if (rsa.getString(5).equals("DEPT")) {
                if (rs5.next()) {
                    v5 = rs5.getString(1);
                } else {
                    v5 = "";
                }
            } else if (rsa.getString(5).equals("SECT")) {
                if (rs6.next()) {
                    v5 = rs6.getString(1);
                } else {
                    v5 = "";
                }
            } else if (rsa.getString(5).equals("CATG")) {
                if (rs7.next()) {
                    v5 = rs7.getString(1);
                } else {
                    v5 = "";
                }
            } else {
                v5 = rsa.getString(5);
            }

            if (rsa.getString(6).equals("COMP")) {
                if (rs1.next()) {
                    v6 = rs1.getString(1);
                } else {
                    v6 = "";
                }
            } else if (rsa.getString(6).equals("GRPP")) {
                if (rs2.next()) {
                    v6 = rs2.getString(1);
                } else {
                    v6 = "";
                }
            } else if (rsa.getString(6).equals("REGN")) {
                if (rs3.next()) {
                    v6 = rs3.getString(1);
                } else {
                    v6 = "";
                }
            } else if (rsa.getString(6).equals("BRCH")) {
                if (rs4.next()) {
                    v6 = rs4.getString(1);
                } else {
                    v6 = "";
                }
            } else if (rsa.getString(6).equals("DEPT")) {
                if (rs5.next()) {
                    v6 = rs5.getString(1);
                } else {
                    v6 = "";
                }
            } else if (rsa.getString(6).equals("SECT")) {
                if (rs6.next()) {
                    v6 = rs6.getString(1);
                } else {
                    v6 = "";
                }
            } else if (rsa.getString(6).equals("CATG")) {
                if (rs7.next()) {
                    v6 = rs7.getString(1);
                } else {
                    v6 = "";
                }
            } else {
                v6 = rsa.getString(6);
            }

            if (rsa.getString(7).equals("COMP")) {
                if (rs1.next()) {
                    v7 = rs1.getString(1);
                } else {
                    v7 = "";
                }
            } else if (rsa.getString(7).equals("GRPP")) {
                if (rs2.next()) {
                    v7 = rs2.getString(1);
                } else {
                    v7 = "";
                }
            } else if (rsa.getString(7).equals("REGN")) {
                if (rs3.next()) {
                    v7 = rs3.getString(1);
                } else {
                    v7 = "";
                }
            } else if (rsa.getString(7).equals("BRCH")) {
                if (rs4.next()) {
                    v7 = rs4.getString(1);
                } else {
                    v7 = "";
                }
            } else if (rsa.getString(7).equals("DEPT")) {
                if (rs5.next()) {
                    v7 = rs5.getString(1);
                } else {
                    v7 = "";
                }
            } else if (rsa.getString(7).equals("SECT")) {
                if (rs6.next()) {
                    v7 = rs6.getString(1);
                } else {
                    v7 = "";
                }
            } else if (rsa.getString(7).equals("CATG")) {
                if (rs7.next()) {
                    v7 = rs7.getString(1);
                } else {
                    v7 = "";
                }
            } else {
                v7 = rsa.getString(7);
            }

            dl = rsa.getString(8);

            if (!v1.equals("")) {
                sb.append(v1 + dl);
            }
            if (!v2.equals("")) {
                sb.append(v2 + dl);
            }
            if (!v3.equals("")) {
                sb.append(v3 + dl);
            }
            if (!v4.equals("")) {
                sb.append(v4 + dl);
            }
            if (!v5.equals("")) {
                sb.append(v5 + dl);
            }
            if (!v6.equals("")) {
                sb.append(v6 + dl);
            }
            if (!v7.equals("")) {
                sb.append(v7 + dl);
            }

            rs8.next();
            curr = rs8.getInt(1);
            ++curr;

            getStatement().executeUpdate(
                    "update am_ad_cart_identity set cart_cr = " + curr +
                    " where cart_id = (select category_id from am_ad_category where category_id = " +
                    "'" + cat + "')");
   
            identity = sb.toString() + (curr - 1);
        } else if (rsb.next()) {
            rs9.next();
            curr = rs9.getInt(1);
            ++curr;
  
            getStatement().executeUpdate(
                    "update am_ad_sequ_identity set sequ_cr = " + curr);

            identity = String.valueOf(curr - 1);
        }
        closeConnection(con, ps);
        return identity;
    }
    public Statement getStatement() throws Exception {
        if (con!= null) {
            return con.createStatement();
        } else if(con== null) {
            return getConnection().createStatement();
        }
        else{
            return null;
        }
    }
    public java.sql.Date dateConvert(java.util.Date inputDate) {

        return this.getSQLFormatedDate(inputDate);

    }

    public String findObject(String query)
    {
    //	System.out.println("====findObject query=====  "+query);
        Connection Con2 = null;
        PreparedStatement Stat = null;
        ResultSet result = null;
        String found = null;

        //String finder = "UNKNOWN";
        String finder = "";

       // double sequence = 0.00d;
        try {

        	Con2 = getConnection();
            Stat = Con2.prepareStatement(query);
            result = Stat.executeQuery();

            while (result.next()) {
                finder = result.getString(1);
            }

        } catch (Exception ee2) {
            System.out.println("WARN:ERROR OBTAINING OBJ --> " + ee2);
            ee2.printStackTrace();
        } finally {
            closeConnection(Con2, Stat, result);
        }

        return finder;
    }
    
    public boolean deleteObject(String query)
    {
    	//System.out.println("====findObject query=====  "+query);
    	boolean done = false;
        Connection Con2 = null;
        PreparedStatement Stat = null;
        ResultSet result = null;

        try {

        	Con2 = getConnection();
			ps = Con2.prepareStatement(query);
			done = (ps.executeUpdate() != -1);

        } catch (Exception ee2) {
            System.out.println("WARN:ERROR deleteObject --> " + ee2);
            ee2.printStackTrace();
        } finally {
            closeConnection(Con2, Stat);
        }

        return done;
    }
    

	public boolean newassetinterface(String errormessage,String integrifyId,String status,String assetid,String assetcode) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "";
		//System.out.println("errormessage: "+errormessage+" integrifyId: "+integrifyId+" status: "+status+" assetid: "+assetid+" assetcode: "+assetcode);
		if(status.equalsIgnoreCase("Y")){
		query = "UPDATE NEW_ASSET_INTERFACE"
				+ " SET ERROR_MESSAGE = '"+errormessage+"',POSTED='"+status+"',ASSET_ID = '"+assetid.trim()+"'," 
				+ " ASSET_CODE = '"+assetcode.trim()+"' "
				+ " WHERE INTEGRIFY_ID = '"+integrifyId+"'";
	//	System.out.println("query: "+query);
		}
		if(!status.equalsIgnoreCase("Y")){
		query = "UPDATE NEW_ASSET_INTERFACE"
			+ " SET ERROR_MESSAGE = '"+errormessage+"',POSTED='"+status+"' " 
			+ " WHERE INTEGRIFY_ID = '"+integrifyId+"'";
	//	System.out.println("query2: "+query);
		}
		try {
			con = getConnection();
		//	if(status.equalsIgnoreCase("Y")){
			ps = con.prepareStatement(query);//}
			//else{ps = con.prepareStatement(query2);}
			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}


	public void insToAm_Invoice_No(String assetID,String lpo,String invoiceNo,String TransType,String integrifyid)
	    {

	        Connection Con2 = null;
	        PreparedStatement Stat = null;
	        ResultSet result = null;
	        String found = null;

	        String query="Insert into Am_Invoice_no (asset_id,lpo,invoice_no,trans_type,integrify_Id) values (?,?,?,?,?)";

	             try
	             {
	            Con2 = getConnection();
	            Stat = Con2.prepareStatement(query);
	            Stat.setString(1, assetID);
	            Stat.setString(2, lpo);
	            Stat.setString(3, invoiceNo);
	            Stat.setString(4, TransType);
	            Stat.setString(5, integrifyid);
	            Stat.execute();

	        } catch (Exception ee2) {
	            System.out.println("WARN:ERROR insToAm_Invoice_No  --> " + ee2);
	            ee2.printStackTrace();
	        } finally {
	            closeConnection(Con2, Stat, result);
	        }

	        }
    public String findtwoinfo(String query)
    {
    	//System.out.println("====findObject query=====  "+query);
        Connection Con2 = null;
        PreparedStatement Stat = null;
        ResultSet result = null;
        String found = null;

        String finder = "UNKNOWN";

        double sequence = 0.00d;
        try {

        	Con2 = getConnection();
            Stat = Con2.prepareStatement(query);
            result = Stat.executeQuery();

            while (result.next()) {
                finder = result.getString(1);
                finder = finder+","+result.getString(2);
            }

        } catch (Exception ee2) {
            System.out.println("WARN:ERROR OBTAINING OBJ --> " + ee2);
            ee2.printStackTrace();
        } finally {
            closeConnection(Con2, Stat, result);
        }

        return finder;
    }

    public boolean insertApprovalx(String id, String description, String page, String flag,
            String partPay, String UserId, String Branch, String subjectToVat, String whTax, String url, String tranId,int assetCode) {
        boolean done = true;
        flag = "Y";
        Connection con = null;
        PreparedStatement ps = null;
     //   System.out.println("insertApprovalx Description: "+description);
        String query = "INSERT INTO [am_raisentry_post](Id,Description,Page,Flag,partPay,"
                + "UserId,Branch,subjectToVat,whTax,url,trans_Id,asset_code)"
                + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, id);
            ps.setString(2, description);
            ps.setString(3, page);
            ps.setString(4, flag);
            ps.setString(5, partPay);
            ps.setString(6, UserId);
            ps.setString(7, Branch);
            ps.setString(8, subjectToVat);
            ps.setString(9, whTax);
            ps.setString(10, url);
            ps.setString(11, tranId);
            ps.setInt(12, assetCode);
            ps.execute();

        } catch (Exception ex) {
            done = false;
            System.out.println(
                    "WARNING:cannot insert am_raisentry_post->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done; 
    }


    public String[] setApprovalData(String id,String assettype){

    //String q ="select asset_id, asset_status,user_ID,supervisor,Cost_Price,Posting_Date,description,effective_date,BRANCH_CODE from am_asset where asset_id ='" +id+"'";
     //String currentDate  = reArrangeDate(getCurrentDate1());
        
        String[] result= new String[12];
        String query = "";
        Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            String tranType = "";
            if(assettype.equalsIgnoreCase("C")){
                query = "select asset_id,user_ID,supervisor,Cost_Price,Posting_Date," +
                " description,effective_date,BRANCH_CODE,Asset_Status from am_asset where asset_id ='" +id+"'"; 
                tranType = "Asset Creation";
            }
             if(assettype.equalsIgnoreCase("U")){
                 query = "select asset_id,user_ID,supervisor,Cost_Price,Posting_Date," +
                 " description,effective_date,BRANCH_CODE,Asset_Status from AM_ASSET_UNCAPITALIZED where asset_id ='" +id+"'";
                 tranType = "Asset Creation Uncapitalized";
             }

            try {
                con = getConnection();
                ps = con.prepareStatement(query);
                rs = ps.executeQuery();
                while (rs.next()) {
                    result[0] = rs.getString(1);
                    result[1]= rs.getString(2);
                    result[2] = rs.getString(3);
                   result[3] = rs.getString(4);
                   result[4] = rs.getString(5);
                   result[5] = rs.getString(6);
                   result[6] = rs.getString(7);
                   result[7] = rs.getString(8);
                   result[8] = rs.getString(9);//asset_status

                }
              //  System.out.println("Query setApprovalData "+query);
            } catch (Exception ex) {
                System.out.println("WARN: Error fetching CategoryCode ->" + ex);
            } finally {
                closeConnection(con, ps);
            }
    result[9] = tranType;
    result[10] = "P";
    //result[11] = timeInstance();
            return result;

    }//setApprovalData()

    //the methods below are to set the asset code in am_asset_approval and am_asset_approval_archive

    public void setPendingTrans(String[] a, String code,int assetCode){

        int transaction_level=0;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
 String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description," +
         "effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time,transaction_id,batch_id," +
         "transaction_level,asset_code) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 String tranLevelQuery = "select level from approval_level_setup where code ='"+code+"'";
// System.out.println("tranLevelQuery in setPendingTrans: "+tranLevelQuery);
        con = null;
        ps = null;
        rs = null;
        try
        {
            con = getConnection();


            /////////////To get transaction level
             ps = con.prepareStatement(tranLevelQuery);
              rs = ps.executeQuery();


            while(rs.next())
            {
            transaction_level = rs.getInt(1);
 //           System.out.println("$$$$$$$$$$$$$$$$$$$$$$");
 //            System.out.println(transaction_level);
 //             System.out.println(code);
            }//if



            ////////////To set values for approval table

            ps = con.prepareStatement(pq);


            SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");

            String mtid =  new ApplicationHelper().getGeneratedId("am_asset_approval");
            ps.setString(1, (a[0]==null)?"":a[0]);
            ps.setString(2, (a[1]==null)?"":a[1]);
            ps.setString(3, (a[2]==null)?"":a[2]);
            ps.setDouble(4, (a[3]==null)?0:Double.parseDouble(a[3]));
            //ps.setDate(5, (a[4])==null?null:dbConnection.dateConvert(a[4]));
            ps.setTimestamp(5,getDateTime(new java.util.Date()));
            ps.setString(6, (a[5]==null)?"":a[5]);
            ps.setDate(7,(a[6])==null?null:dateConvert(a[6]));
            ps.setString(8, (a[7]==null)?"":a[7]);
            ps.setString(9, (a[8]==null)?"":a[8]); //asset_status
            ps.setString(10, (a[9]==null)?"":a[9]);
            //System.out.println("Tran Type In setPendingTrans: "+a[10]);
            ps.setString(11, a[10]);
           // System.out.println("In setPendingTrans: "+a[9]);
            ps.setString(12, timer.format(new java.util.Date()));
            ps.setString(13,mtid);
            ps.setString(14, mtid);
            ps.setInt(15, transaction_level);
            ps.setInt(16,assetCode);

            ps.execute();  

        }
        catch(Exception er)
        {
            System.out.println(">>>AssetRecordBeans:setPendingTrans(>>>>>>" + er);

        }finally{
        closeConnection(con, ps);

        }

   //String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description,effective_date,branchCode,tran_type, process_status,tran_sent_time) values(?,?,?,?,?,?,?,?,?,?,?)";

    }//staticApprovalInfo()


    public void setPendingTransArchive(String[] a, String code,int mtid, int assetCode){

          int transaction_level=0;
          Connection con;
          PreparedStatement ps;
          ResultSet rs;
   String pq = "insert into am_asset_approval_archive(asset_id,user_id,super_id,amount,posting_date,description," +
           "effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time,transaction_id,batch_id," +
           "transaction_level,asset_code) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
   String tranLevelQuery = "select level from approval_level_setup where code ='"+code+"'";
 //  System.out.println("setPendingTransArchive pq: "+pq);
 //  System.out.println("setPendingTransArchive tranLevelQuery: "+tranLevelQuery);
          con = null;
          ps = null;
          rs = null;
          try
          {
              con = getConnection();


              /////////////To get transaction level
               ps = con.prepareStatement(tranLevelQuery);
                rs = ps.executeQuery();


              while(rs.next()){
              transaction_level = rs.getInt(1);

              }//if



              ////////////To set values for approval table

              ps = con.prepareStatement(pq);


              SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");

              //String mtid =  new ApplicationHelper().getGeneratedId("am_asset_approval");
              ps.setString(1, (a[0]==null)?"":a[0]);
              ps.setString(2, (a[1]==null)?"":a[1]);
              ps.setString(3, (a[2]==null)?"":a[2]);
              ps.setDouble(4, (a[3]==null)?0:Double.parseDouble(a[3]));
              //ps.setDate(5, (a[4])==null?null:dbConnection.dateConvert(a[4]));
              ps.setTimestamp(5,getDateTime(new java.util.Date()));
              ps.setString(6, (a[5]==null)?"":a[5]);
              ps.setDate(7,(a[6])==null?null:dateConvert(a[6]));
              ps.setString(8, (a[7]==null)?"":a[7]);
              ps.setString(9, (a[8]==null)?"":a[8]); //asset_status
              ps.setString(10, (a[9]==null)?"":a[9]);
              ps.setString(11, a[10]);
              ps.setString(12, timer.format(new java.util.Date()));
              ps.setInt(13,mtid);
              ps.setString(14, String.valueOf(mtid));
              ps.setInt(15, transaction_level);
              ps.setInt(16,assetCode);

              ps.execute();

          }
          catch(Exception er)
          {
              System.out.println(">>>AssetRecordBeans:setPendingTransArchive()>>>>>>" + er);

          }finally{
          closeConnection(con, ps);

          }
     //String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description,effective_date,branchCode,tran_type, process_status,tran_sent_time) values(?,?,?,?,?,?,?,?,?,?,?)";
      }//staticApprovalInfo()
	
	public void sendMailSupervisor(String id, String subject,String msgText1)
	{
//		System.out.println("Just called the sendApprovalEmail API");
		try
		{
			Properties prop = new Properties();
			File file = new File("C:\\Property\\FixedAsset.properties");
	//		System.out.print("Absolute Path:>>> "+file.getAbsolutePath());
	//		System.out.print("Able to load file ");
			FileInputStream in = new FileInputStream(file);
			prop.load(in);
	//		System.out.print("Able to load properties into prop");
			String host = prop.getProperty("mail.smtp.host");
			String from = prop.getProperty("mail-user");
			Session session = Session.getDefaultInstance(prop,null);
			
			boolean sessionDebug = true;
			Properties props = System.getProperties();
			props.put("mail.host",host);
			props.put("mail.transport.protocols","smtp");
	//		System.out.println("setting auth");
			session = Session.getDefaultInstance(props,null);
			session.setDebug(sessionDebug);

	//		System.out.println("From = "+from);
	//		System.out.println("point 1");
			
			Message msg = new MimeMessage(session);
	//		System.out.println("point 2");
			msg.setFrom(new InternetAddress(from));
	//		System.out.println("point 3");
			String to = userEmail(id);
			InternetAddress[] address = { new InternetAddress(to) };
//			System.out.println("point 4");
			msg.setRecipients(Message.RecipientType.TO,address);
     
			 msg.setSubject(subject);

//			System.out.println("point 6");
			msg.setSentDate(new Date());
//			System.out.println("point 7");

			String msgBody = msgText1;
//		    System.out.print("The mail body: "+msgBody);
		    msg.setText(msgBody);
		    msg.saveChanges();
			
//			System.out.println("point 8");
			
		   
		    		
//			System.out.println("point 9");
			
//			System.out.println("point 10");
		  	 
    Transport tr = session.getTransport("smtp");
 //   System.out.println("point 11");
	tr.connect();
//	System.out.println("point 12");
//	Security.getProviders("smtp");
	
//	System.out.println("point test");
	//tr.sendMessage(msg, msg.getAllRecipients());
	tr.send(msg);
//	System.out.println("point 13");
	tr.close(); 
	
   
//	System.out.println("point 14");
		} 
		catch (Exception ex) 
		{
			System.out.println("point 15");
			ex.printStackTrace();
		}

	}	

    public String userEmail(String user_id) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String email = "";

        String FINDER_QUERY = "SELECT email from am_gb_user WHERE user_id = ? ";

        try {
            con = getConnection();
            ps = con.prepareStatement(FINDER_QUERY);
            ps.setString(1, user_id);

            rs = ps.executeQuery();

            while (rs.next()) {
                email = rs.getString("email");
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch email->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
  //      System.out.println(">>>>>The user id is " + user_id + " the emial is " + email);
        return email;

    }	

    public String getCurrentMtid(String tableName) {
 //   	System.out.println("getCurrentMtid tableName: "+tableName);
        String query =
                "select max(mt_id) from IA_MTID_TABLE where mt_tablename=?";// +tableName + "' ";
        String mtid = "0";
        Connection con = null;
       PreparedStatement ps = null;
       ResultSet rs = null;

        try {
            con = getConnection();
                       ps = con.prepareStatement(query);
                       ps.setString(1, tableName);
                       rs = ps.executeQuery();

            while (rs.next()) {

                mtid = rs.getString(1);

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(con, ps, rs);
        }

        return mtid;

    }

    public boolean updateNewAssetStatux(String assetId,String tablename) throws Exception {

            String query = "update "+tablename+" SET  asset_status = 'APPROVED' where asset_id ='" +assetId+"'";
             boolean done = true;
            Connection con = null;
            PreparedStatement ps = null;

            try {



                con = getConnection();
                ps = con.prepareStatement(query);

                ps.execute();

            } catch (Exception ex) {
                done = false;
                System.out.println("AssetRecordsBean: updateNewAssetStatux(): WARN:Error updating asset->" + ex);
            } finally {
                closeConnection(con, ps);
            }
            return done;
        }

    public int getNumOfTransactionLevel(String levelCode){
       String query = "select level from approval_level_setup where code = '"+levelCode+"'";
       int result=0;
        Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
           try {
                con = getConnection();
                ps = con.prepareStatement(query);
                rs = ps.executeQuery();
                while (rs.next()) {
                    result = rs.getInt(1);

                }

            } catch (Exception ex) {
                System.out.println("AssetRecordsBean: getNumOfTransactionLevel(String levelCode) WARN: Error fetching CategoryCode ->" + ex);
            } finally {
                closeConnection(con, ps);
            }

            return result;
       } //getNumOfTransactionLevel()

    public boolean updateNewApprovalAssetStatus(String groupID, int supervise) throws Exception {

            /*String query = "update am_asset_approval set  asset_status = ?, process_status=?, " +
            		"super_id =?, approval1 =? where asset_id =?";*/
            String query = "update am_asset_approval set  asset_status = ?, process_status=?, " +
    		"super_id =?, approval1 =? where asset_id =?";
             boolean done = true;
            Connection con = null;
            PreparedStatement ps = null;

            try {



                con = getConnection();
                ps = con.prepareStatement(query);
                ps.setString(1, "ACTIVE");
                ps.setString(2, "A");
                ps.setInt(3, supervise);
                ps.setInt(4, supervise);
                ps.setString(5,groupID);
                ps.execute();

            } catch (Exception ex) {
                done = false;
                System.out.println("AssetRecordsBean: updateNewApprovalAssetStatus: WARN:Error updating asset->" + ex);
            } finally {
                closeConnection(con, ps);
            }
            return done;

        }

    public String subjectToVat(String id){
    String result="";
        Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

             String query =
                    "SELECT Subject_TO_Vat FROM am_asset  " +
                    "WHERE asset_id = '" + id + "' ";


            try {
                con = getConnection();
                ps = con.prepareStatement(query);
                rs = ps.executeQuery();
                while (rs.next()) {
                    result = rs.getString(1);
                }

            } catch (Exception ex) {
                System.out.println("WARN: Error fetching DepreciationRate ->" + ex);
            } finally {
                closeConnection(con, ps);
            }

            return result;
    }

    public String whTax(String id){
    String result="";
        Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

             String query =
                    "SELECT wh_tax FROM am_asset  " +
                    "WHERE asset_id = '" + id + "' ";


            try {
                con = getConnection();
                ps = con.prepareStatement(query);
                rs = ps.executeQuery();
                while (rs.next()) {
                    result = rs.getString(1);
                }

            } catch (Exception ex) {
                System.out.println("WARN: Error fetching WHTAX ->" + ex);
            } finally {
               closeConnection(con, ps);
            }

            return result;
    }

    public void updateAssetStatusChange(String query_r){
    Connection con = null;
            PreparedStatement ps = null;
    try {
        con = getConnection();


    ps = con.prepareStatement(query_r);
               int i =ps.executeUpdate();
            } catch (Exception ex) {

                System.out.println("AssetRecordBean: updateAssetStatusChange()>>>>>" + ex);
            } finally {
                closeConnection(con, ps);
            }
    }
    public void updateRaiseEntry(String assetid) {

        Connection con = null;
        PreparedStatement ps = null;
        String NOTIFY_QUERY = "UPDATE am_asset SET raise_entry = ? WHERE ASSET_ID = ?  ";

        try {
            con = getConnection();
            ps = con.prepareStatement(NOTIFY_QUERY);
            ps.setString(1, "Y");
            ps.setString(2, assetid);
            ps.executeUpdate();

        } catch (Exception ex) {
            System.out.println("WARNING: cannot update am_asset+" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }

    }
    
    public String MailMessage(String Mail_Code,String Transaction_Type)
	{
		String message="";
		String query="SELECT Mail_Description FROM am_mail_statement where Mail_Code='"+Mail_Code+"' and Transaction_Type='"+Transaction_Type+"'";
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = getConnection();	
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) 
			 {

				message  = rs.getString("Mail_Description");
				

			 }
		   }
			catch (Exception er) 
			{
			 er.printStackTrace();	
				
			} 
			finally 
			{
				closeConnection(con, ps);
			}
	return 	message;
	}
    public String MailTo(String Mail_Code,String Transaction_Type)
  	{
  		String to="";
  		String query="SELECT mail_address FROM am_mail_statement where Mail_Code='"+Mail_Code+"' and Transaction_Type='"+Transaction_Type+"'";
  		
  		Connection con = null;
  		PreparedStatement ps = null;
  		ResultSet rs = null;
  		try {
  			con = getConnection();	
  			ps = con.prepareStatement(query);
  			rs = ps.executeQuery();

  			while (rs.next()) 
  			 {
  				to  = rs.getString("mail_address");
  			 }
  		   }
  			catch (Exception er) 
  			{
  			 er.printStackTrace();	
  				
  			} 
  			finally 
  			{
  				closeConnection(con, ps);
  			}
  	return 	to;
  	}
	public void sendMail(String email, String subject,String msgText1)
	{
//		System.out.println("Just called the sendApprovalEmail API");
		try
		{
			Properties prop = new Properties();
			File file = new File("C:\\Property\\FixedAsset.properties");
//			System.out.print("Absolute Path:>>> "+file.getAbsolutePath());
//			System.out.print("Able to load file ");
			FileInputStream in = new FileInputStream(file);
			prop.load(in);
//			System.out.print("Able to load properties into prop");
			String host = prop.getProperty("mail.smtp.host");
			String from = prop.getProperty("mail-user");
			Session session = Session.getDefaultInstance(prop,null);
			
			boolean sessionDebug = true;
			Properties props = System.getProperties();
			props.put("mail.host",host);
			props.put("mail.transport.protocols","smtp");
//			System.out.println("setting auth");
			session = Session.getDefaultInstance(props,null);
			session.setDebug(sessionDebug);

//			System.out.println("From = "+from);
//			System.out.println("point 1");
			
			Message msg = new MimeMessage(session);
	//		System.out.println("point 2");
			msg.setFrom(new InternetAddress(from));
	//		System.out.println("point 3");
			String recepient[]=email.split(",");
			String to = recepient[0];
			InternetAddress[] address = { new InternetAddress(to) };
	//		System.out.println("point 4");
			msg.setRecipients(Message.RecipientType.TO,address);
     
			 msg.setSubject(subject);

//			System.out.println("point 6");
			msg.setSentDate(new Date());
//			System.out.println("point 7");

			String msgBody = msgText1;
		  //  System.out.print("The mail body: "+msgBody);
		    msg.setText(msgBody);
		    msg.saveChanges();
			
//			System.out.println("point 8");
				    		
//			System.out.println("point 9");
			//String cc[]={recepient[1],recepient[2],recepient[3]};
			for(int i=0;i<recepient.length;i++)
			{
			InternetAddress addressCopy = new InternetAddress(recepient[i]);	
			msg.setRecipient(Message.RecipientType.CC, addressCopy);
			}	
//			System.out.println("point 10");
		  	 
    Transport tr = session.getTransport("smtp");
//    System.out.println("point 11");
	tr.connect();
//	System.out.println("point 12");
//	Security.getProviders("smtp");
	
//	System.out.println("point test");
	//tr.sendMessage(msg, msg.getAllRecipients());
	tr.send(msg);
//	System.out.println("point 13");
	tr.close(); 
	
   
//	System.out.println("point 14");
		} 
		catch (Exception ex) 
		{
			System.out.println("point 15");
			ex.printStackTrace();
		}

	}		
	 
	String section = "";
    private void rinsertAssetRecord2(String integrifyId,String Description,String RegistrationNo,String VendorAC,String Datepurchased,
    		String make,String AssetModel,String AssetSerialNo,String AssetEngineNo,String supplied_by,String user,
    		String maintained_by,double CostPrice,double VatableCost,String AuthorizedBy,String WhTax,double whtaxamount,String PostingDate,String EffectiveDate,
    		String PurchaseReason,String SubjectTOVat,String AssetStatus,String state,String driver,String UserID,String branchCode,String reqdepreciation,
    		String sectionCode,String deptCode,String categoryCode,String barCode,String sbuCode,String lpo,String invoiceNo,String supervisor,
    		String posted,String assetId,int assetCode,String assettype,String branch_id,String department_id,String section_id,String depreciation_end_date,
    		String section,String spare_1,String spare_2,String status,String partPAY,String fullyPAID,
    		String deferPay,String systemIp,String macAddress,String who_to_remind,String email_1,String who_to_remind_2,String email2,String raise_entry,
    		String noOfMonths,String warrantyStartDate,String expiryDate,String category_id,double vatamount,String residual_value,int whtaxvalue,
    		String location,String memovalue,String memo, String spare1,String spare2,String multiple) throws Exception, Throwable {

    Connection con = null;
    PreparedStatement ps = null;
    boolean done = true;
AssetPaymentManager payment = null;
    /*if (require_redistribution.equalsIgnoreCase("Y")) {
        status = "Z";
             }*/
    if (make == null || make.equals("")) {
        make = "0";
    }
    if (maintained_by == null || maintained_by.equals("")) {
        maintained_by = "0";
    }
    if (supplied_by == null || supplied_by.equals("")) {
        supplied_by = "0";
    }
    if (user == null || user.equals("")) {
        user = "";
    }
    if (location == null || location.equals("")) {
        location = "0";
    }
    if (driver == null || driver.equals("")) {
        driver = "0";
    }
    if (state == null || state.equals("")) {
        state = "0";
    }
    if (department_id == null || department_id.equals("")) {
        department_id = "0";
    }
  /*  if (vat_amount == null || vat_amount.equals("")) {
        vat_amount = "0.0";
    }
    if (vatable_cost == null || vatable_cost.equals("")) {
        vatable_cost = "0.0";
    }
    if (wh_tax_amount == null || wh_tax_amount.equals("")) {
        wh_tax_amount = "0";
    }*/
    if (branch_id == null || branch_id.equals("")) {
        branch_id = "0";
    }
/*    if (province == null || province.equals("")) {
        province = "0";
    }*/
    if (category_id == null || category_id.equals("")) {
        category_id = "0";
    }

    if (residual_value == null || residual_value.equals("")) {
        residual_value = "0";
    }
//    if (section_id == null || section_id.equals("")) {
//        section_id = "0";
//    }

    if (noOfMonths == null || noOfMonths.equals("")) {
        noOfMonths = "0";
    }
    if (warrantyStartDate == null || warrantyStartDate.equals("")) {
        warrantyStartDate = null;
    }
    if (expiryDate == null || expiryDate.equals("")) {
        expiryDate = null;
    }
    String require_redistribution = "N";
    String amountPTD = "0.00";
    String province = "0";
//    System.out.println("Whholding Tax Rate: "+whtaxvalue+"  assetCode: "+assetCode);
    //vat_amount = vat_amount.replaceAll(",", "");
    //vatable_cost = vatable_cost.replaceAll(",", "");
    //wh_tax_amount = wh_tax_amount.replaceAll(",", "");
   // residual_value = residual_value.replaceAll(",", "");
   // amountPTD = amountPTD.replaceAll(",","");
    String createQuery = "INSERT INTO AM_ASSET_ARCHIVE        " +
                         "(" +
                         "ASSET_ID, REGISTRATION_NO, BRANCH_ID, DEPT_ID," +
                         "SECTION_ID, CATEGORY_ID, [DESCRIPTION], VENDOR_AC," +
                         "DATE_PURCHASED, DEP_RATE, ASSET_MAKE, ASSET_MODEL," +
                         "ASSET_SERIAL_NO, ASSET_ENGINE_NO, SUPPLIER_NAME," +

                         "ASSET_USER, ASSET_MAINTENANCE, ACCUM_DEP, MONTHLY_DEP," +
                         "COST_PRICE, NBV, DEP_END_DATE, RESIDUAL_VALUE," +
                         "AUTHORIZED_BY, POSTING_DATE, EFFECTIVE_DATE, PURCHASE_REASON," +
                         "USEFUL_LIFE, TOTAL_LIFE, LOCATION, REMAINING_LIFE," +

                         "VATABLE_COST,VAT, WH_TAX, WH_TAX_AMOUNT, REQ_DEPRECIATION," +
                         "REQ_REDISTRIBUTION, SUBJECT_TO_VAT, WHO_TO_REM, EMAIL1," +
                         "WHO_TO_REM_2, EMAIL2, RAISE_ENTRY, DEP_YTD, SECTION," +
                         "STATE, DRIVER, SPARE_1, SPARE_2, ASSET_STATUS, [USER_ID]," +
                         "MULTIPLE,PROVINCE,WAR_START_DATE,WAR_MONTH,WAR_EXPIRY_DATE," +
                         "BRANCH_CODE,DEPT_CODE,SECTION_CODE,CATEGORY_CODE,	AMOUNT_PTD," +
                         "AMOUNT_REM,PART_PAY,FULLY_PAID,BAR_CODE,SBU_CODE,LPO,supervisor,defer_pay,wht_percent,system_ip,mac_address,asset_code) " +

                         "VALUES" +
                         "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                          "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    try {

      //  double costPrice = CostPrice +  VatableCost;

        con = getConnection();
        ps = con.prepareStatement(createQuery);
        ps.setString(1, assetId);
        ps.setString(2, RegistrationNo);
        ps.setInt(3, Integer.parseInt(branch_id));
        ps.setInt(4, Integer.parseInt(department_id));
        ps.setInt(5, Integer.parseInt(section_id));
        ps.setInt(6, Integer.parseInt(category_id));
        ps.setString(7, Description);
        ps.setString(8, VendorAC);
        ps.setString(9, Datepurchased);
        ps.setString(10, getDepreciationRate(categoryCode));
        ps.setString(11, make);
        ps.setString(12, AssetModel);
        ps.setString(13, AssetSerialNo);
        ps.setString(14, AssetEngineNo);
        ps.setInt(15, Integer.parseInt(supplied_by));
        ps.setString(16, user);
        ps.setInt(17, Integer.parseInt(maintained_by));
        ps.setInt(18, 0);
        ps.setInt(19, 0);
        ps.setDouble(20, CostPrice);
        ps.setDouble(21, CostPrice);
        ps.setDate(22, dateConvert(depreciation_end_date));
        ps.setDouble(23, Double.parseDouble(residual_value));
        ps.setString(24, AuthorizedBy);
        ps.setTimestamp(25, getDateTime(new java.util.Date()));
        ps.setDate(26, dateConvert(EffectiveDate));
        ps.setString(27, PurchaseReason);
        ps.setString(28, "0");
        ps.setString(29, computeTotalLife(getDepreciationRate(categoryCode)));
        ps.setInt(30, Integer.parseInt(location));
        ps.setString(31, computeTotalLife(getDepreciationRate(categoryCode)));
        ps.setDouble(32, VatableCost);
        ps.setDouble(33, vatamount);
        ps.setString(34, WhTax);
        ps.setDouble(35, whtaxamount);
        ps.setString(36, reqdepreciation);
        ps.setString(37, require_redistribution);
        ps.setString(38, SubjectTOVat);
        ps.setString(39, who_to_remind);
        ps.setString(40, email_1);
        ps.setString(41, who_to_remind_2);
        ps.setString(42, email2);
        ps.setString(43, raise_entry);
        ps.setString(44, "0");
        ps.setString(45, section);
        ps.setInt(46, Integer.parseInt(state));
        ps.setInt(47, Integer.parseInt(driver));
        ps.setString(48, spare1);
        ps.setString(49, spare2);
        ps.setString(50, status);
        ps.setString(51, UserID);
        ps.setString(52, multiple);
        ps.setString(53, province);
        ps.setDate(54, dateConvert(warrantyStartDate));
        ps.setInt(55, new Integer(noOfMonths).intValue());
        ps.setDate(56, dateConvert(expiryDate));
        ps.setString(57, getBranchCode(branch_id));
        ps.setString(58, getDeptCode(department_id));
        ps.setString(59, getSectionCode(section_id));
        ps.setString(60, getCategoryCode(category_id));
        ps.setDouble(61, Double.parseDouble(amountPTD));
        ps.setDouble(62, VatableCost);
        ps.setString(63, partPAY);
        ps.setString(64, fullyPAID);
        ps.setString(65, barCode);
        ps.setString(66,sbuCode);
        ps.setString(67, lpo);
        ps.setString(68,supervisor);
        ps.setString(69, deferPay);
        ps.setInt(70, whtaxvalue);
        ps.setString(71,systemIp);
        ps.setString(72, macAddress);
        ps.setInt(73,assetCode);

        ps.execute();

    } catch (Exception ex) {
        done = false;
        System.out.println("WARN:Error inserting into  asset creation archive->" + ex);
    } finally {
        closeConnection(con, ps);
    }

   // return done;
}

    public Asset getAsset(String assetId) {
        String selectQuery = "SELECT * FROM AM_ASSET_MAIN WHERE ASSET_ID = '"+assetId+"' ";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Asset _obj = null;

        try {
            con = getConnection();
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {
                //String assetId = rs.getString("ASSET_ID");
                String regNo = rs.getString("REGISTRATION_NO");
                int branchId = rs.getInt("BRANCH_ID");
                String branchName = rs.getString("BRANCH_NAME");
                int deptId = rs.getInt("DEPT_ID");
                String deptName = rs.getString("DEPT_NAME");
                int sectionId = rs.getInt("SECTION_ID");
                String sectionName = rs.getString("SECTION_NAME");
                int categoryId = rs.getInt("CATEGORY_ID");
                String categoryName = rs.getString("CATEGORY_NAME");
                String description = rs.getString("DESCRIPTION");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                double depRate = rs.getDouble("DEP_RATE");
                String make = rs.getString("ASSET_MAKE");
                String assetUser = rs.getString("ASSET_USER");
                double accumDep = rs.getDouble("ACCUM_DEP");
                double monthDep = rs.getDouble("MONTHLY_DEP");
                double costPrice = rs.getDouble("COST_PRICE");
                String depEndDate = formatDate(rs.getDate("DEP_END_DATE"));
                double residualValue = rs.getDouble("RESIDUAL_VALUE");
                String effDate = formatDate(rs.getDate("EFFECTIVE_DATE"));
                String raiseEntry = rs.getString("RAISE_ENTRY");
                double NBV = rs.getDouble("NBV");
                //String effDate = rs.getString("EFFECTIVE_DATE");
                String vendorAcct = rs.getString("VENDOR_AC");
                String model = rs.getString("ASSET_MODEL");
                String engineNo = rs.getString("ASSET_ENGINE_NO");
                String email1 = rs.getString("EMAIL1");
                String email2 = rs.getString("EMAIL2");
                //String vendorName = rs.getString("VENDOR_NAME");
                int regionId = rs.getInt("REGION_ID");
                String regionName = rs.getString("REGION_NAME");
                String whoToRem1 = rs.getString("WHO_TO_REM");
                String whoToRem2 = rs.getString("WHO_TO_REM_2");
                String reqRedistbtn = rs.getString("REQ_REDISTRIBUTION");
                double vatAmt = rs.getDouble("VAT");
                double whtAmt = rs.getDouble("WH_TAX_AMOUNT");
                String subj2Vat = rs.getString("SUBJECT_TO_VAT");
                String subj2Wht = rs.getString("WH_TAX");
                double vatableCost = rs.getDouble("VATABLE_COST");
                int assetCode = rs.getInt("asset_code");
               // int wht_percent = rs.getInt("WHT_PERCENT");

                _obj = new Asset(assetId, regNo, branchId, branchName, deptId,
                                 deptName, sectionId, sectionName,
                                 categoryId, categoryName, regionId, regionName,
                                 description, datePurchased, depRate, make,
                                 assetUser,
                                 accumDep, monthDep, costPrice, depEndDate,
                                 residualValue, raiseEntry, NBV, effDate,
                                 vendorAcct, model,
                                 engineNo, email1, email2, whoToRem1, whoToRem2,
                                 reqRedistbtn, vatAmt, whtAmt, subj2Vat,
                                 subj2Wht, vatableCost);
                _obj.setAssetCode(assetCode);

              //  _obj.setWht_percent(wht_percent);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching Asset BY ID ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return _obj;

    }

    //get Maintenance asset by ID
    public Improvement getMaintenanceAsset(String id) {

        String query =
                "SELECT REVALUE_ID,ASSET_ID,COST_INCREASE,REVALUE_REASON,REVALUE_DATE," +
                "USER_ID,RAISE_ENTRY,R_VENDOR_AC,COST_PRICE,VATABLE_COST,VAT_AMOUNT," +
                "WHT_AMOUNT,NBV,ACCUM_DEP,OLD_COST_PRICE,OLD_VATABLE_COST,OLD_VAT_AMOUNT," +
                "OLD_WHT_AMOUNT,OLD_NBV,OLD_ACCUM_DEP,EFFDATE FROM am_asset_improvement " +
                "WHERE ASSET_ID = '" + id + "'";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        ArrayList list = new ArrayList();
        //declare DTO object
        Improvement improve = null;

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                int revalueId = rs.getInt("REVALUE_ID");
                String assetId = rs.getString("ASSET_ID");
                double costIncrease = rs.getDouble("COST_INCREASE");
                String reason = rs.getString("REVALUE_REASON");
                String revalueDate = formatDate(rs.getDate("REVALUE_DATE"));
                int userId = rs.getInt("USER_ID");
                String raiseEntry = rs.getString("RAISE_ENTRY");
                String revVendorAcct = rs.getString("R_VENDOR_AC");
                double cost = rs.getDouble("COST_PRICE");
                double vatableCost = rs.getDouble("VATABLE_COST");
                double vatAmt = rs.getDouble("VAT_AMOUNT");
                double whtAmt = rs.getDouble("WHT_AMOUNT");
                double nbv = rs.getDouble("NBV");
                double accumDep = rs.getDouble("ACCUM_DEP");
                double oldCost = rs.getDouble("OLD_COST_PRICE");
                double oldVatableCost = rs.getDouble("OLD_VATABLE_COST");
                double oldVatAmt = rs.getDouble("OLD_VAT_AMOUNT");
                double oldWhtAmt = rs.getDouble("OLD_WHT_AMOUNT");
                double oldNbv = rs.getDouble("OLD_NBV");
                double oldAccumDep = rs.getDouble("OLD_ACCUM_DEP");
                String effDate = formatDate(rs.getDate("EFFDATE"));

                improve = new Improvement(revalueId, assetId, costIncrease,
                                          reason, revalueDate,
                                          userId, raiseEntry, revVendorAcct,
                                          cost, vatableCost, vatAmt, whtAmt,
                                          nbv, accumDep,
                                          oldCost, oldVatableCost, oldVatAmt,
                                          oldWhtAmt, oldNbv, oldAccumDep,
                                          effDate, 0, 0, 0, "", "", "", "");


            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching am_asset_improvement Asset by ID ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return improve;
    }
    
    public String formatDate(java.util.Date date) {
        String formated = "";

        if (date == null) {
            date = new java.util.Date();
        }
        try {
            sdf = new SimpleDateFormat("dd-MM-yyyy");
            formated = sdf.format(date);
        } catch (Exception e) {
            System.out.println("WARNING:Error formating Date ->" + e.getMessage());
        }

        return formated;
    }	    

    public String setPendingTrans2(String[] a, String code,int assetCode){

        String mtid_id ="";
            int transaction_level=0;
            Connection con;
            PreparedStatement ps;
            ResultSet rs;
     String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description," +
             "effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time,transaction_id,batch_id," +
             "transaction_level,asset_code) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
     String tranLevelQuery = "select level from approval_level_setup where code ='"+code+"'";
            con = null;
            ps = null;
            rs = null;
            try
            {
                con = getConnection();


                /////////////To get transaction level
                 ps = con.prepareStatement(tranLevelQuery);
                  rs = ps.executeQuery();


                while(rs.next()){
                transaction_level = rs.getInt(1);

                }//if



                ////////////To set values for approval table

                ps = con.prepareStatement(pq);


                SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");

                String mtid =  new ApplicationHelper().getGeneratedId("am_asset_approval");
                ps.setString(1, (a[0]==null)?"":a[0]);
                ps.setString(2, (a[1]==null)?"":a[1]);
                ps.setString(3, (a[2]==null)?"":a[2]);
                ps.setDouble(4, (a[3]==null)?0:Double.parseDouble(a[3]));
                //ps.setDate(5, (a[4])==null?null:dbConnection.dateConvert(a[4]));
                ps.setTimestamp(5,getDateTime(new java.util.Date()));
                ps.setString(6, (a[5]==null)?"":a[5]);
                ps.setDate(7,(a[6])==null?null:dateConvert(a[6]));
                ps.setString(8, (a[7]==null)?"":a[7]);
                ps.setString(9, (a[8]==null)?"":a[8]); //asset_status
                ps.setString(10, (a[9]==null)?"":a[9]);
                ps.setString(11, a[10]);
                ps.setString(12, timer.format(new java.util.Date()));
                ps.setString(13,mtid);
                ps.setString(14, mtid);
                ps.setInt(15, transaction_level);
                ps.setInt(16,assetCode);
                ps.execute();

                mtid_id = mtid;
            }
            catch(Exception er)
            {
                System.out.println(">>>AssetRecordBeans:setPendingTrans(>>>>>>" + er);

            }finally{
            closeConnection(con, ps);

            }

       //String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description,effective_date,branchCode,tran_type, process_status,tran_sent_time) values(?,?,?,?,?,?,?,?,?,?,?)";



    return mtid_id;


        }//setPendingTrans2()



    public int insertAssetMaintananceNoSupervisor(double cost, double nbv, String assetId,
                 double costIncrease, String revalueReason,
                 String revalueDate,
                 int userId, double vatableCost, double vatAmt,
                 double whtAmt,
                 String vendorAcct, String raiseEntry,
                 double accumDep,
                 double oldCost, double oldVatableCost,
                 double oldVatAmt, double oldWhtAmt,
                 double oldNbv, double oldAccumDep,
                 String effDate, String mtid, String wh_tax,int wht_percent, String subject_to_vat,
                 String vendorId,String vendorIdOld,String vendorAccOld,String description,String categoryCode, String
                 branchCode,String lpo,String invoiceNo, double newCP,int assetCode,double newCostPrice,
                 double newVatAmt,double newWhtAmt,double newVatableCost,double newNbv, String integrifyId) {
//    	System.out.println("Inside Record Insertion into Table am_asset_improvement");
    // nbv += cost;
   // costPrice += cost;
     int i = 0;
    // System.out.println("cost Increase: "+costIncrease+"   Vatable Cost: "+vatableCost+"  vat Amt: "+vatAmt+" Cost Price: "+cost);
/*
     String updateQuery = "UPDATE AM_ASSET SET COST_PRICE = COST_PRICE + ?," +
            " NBV = ?,VATABLE_COST = VATABLE_COST + ?, VAT = VAT + ?, WH_TAX_AMOUNT = WH_TAX_AMOUNT + ?, ACCUM_DEP =ACCUM_DEP + ?, "+
           "WH_TAX = ?, WHT_PERCENT = ?, SUBJECT_TO_VAT = ?,VENDOR_AC=?,SUPPLIER_NAME=? "+
            " WHERE ASSET_ID = ?";
  */
     String insertQuery =
     "INSERT INTO am_asset_improvement(ASSET_ID,COST_INCREASE,REVALUE_REASON," +
     "REVALUE_DATE,USER_ID,COST_PRICE,VATABLE_COST,VAT_AMOUNT,WHT_AMOUNT,R_VENDOR_AC," +
     "RAISE_ENTRY,NBV,ACCUM_DEP,OLD_COST_PRICE,OLD_VATABLE_COST,OLD_VAT_AMOUNT,OLD_WHT_AMOUNT," +
             "OLD_NBV,OLD_ACCUM_DEP,EFFDATE,REVALUE_ID,WH_TAX,WHT_PERCENT,SUBJECT_TO_VAT," +
     "R_VENDOR_ID,OLD_VENDOR_ACC,OLD_VENDOR_ID,DESCRIPTION,CATEGORY_CODE,BRANCH_CODE,lpoNum,invoice_no,"+
      "new_cost_price,asset_code,NEW_VATABLE_COST,NEW_NBV,NEW_VAT_AMOUNT,NEW_WHT_AMOUNT,INTEGRIFY,APPROVAL_STATUS)"+
     " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//     System.out.println("updateQuery:  "+updateQuery);
//     System.out.println("insertQuery:  "+insertQuery);
     Connection con = null;
     PreparedStatement ps = null;

     try {
     con = getConnection();
     
     ps = con.prepareStatement(insertQuery);
     ps.setString(1, assetId);
     ps.setDouble(2, costIncrease);
     ps.setString(3, revalueReason);
     ps.setString(4, revalueDate);
     ps.setInt(5, userId);
     ps.setDouble(6, cost);
     ps.setDouble(7, vatableCost);
     ps.setDouble(8, vatAmt);
     ps.setDouble(9, whtAmt);
     ps.setString(10, vendorAcct);
     ps.setString(11, raiseEntry);
     ps.setDouble(12, nbv);
     ps.setDouble(13, accumDep);
     ps.setDouble(14, oldCost);
     ps.setDouble(15, oldVatableCost);
     ps.setDouble(16, oldVatAmt);
     ps.setDouble(17, oldWhtAmt);
     ps.setDouble(18, oldNbv);
     ps.setDouble(19, oldAccumDep);
//     System.out.println("effDate:  "+effDate);
     ps.setString(20, effDate);
     ps.setString(21, mtid);
     ps.setString(22,wh_tax);
     ps.setInt(23,wht_percent);
     ps.setString(24,subject_to_vat);
     ps.setInt(25, Integer.parseInt(vendorId));
     ps.setString(26, vendorAccOld);
     ps.setInt(27, Integer.parseInt(vendorIdOld));
     ps.setString(28, description);
     ps.setString(29, categoryCode);
     ps.setString(30, branchCode);
     ps.setString(31, lpo);
     ps.setString(32, invoiceNo);
     ps.setDouble(33, newCostPrice);
     ps.setInt(34, assetCode);
     ps.setDouble(35, newVatableCost);
     ps.setDouble(36, newNbv);
     ps.setDouble(37, newVatAmt);
     ps.setDouble(38, newWhtAmt);
     ps.setString(39, integrifyId);
     ps.setString(40, "ACTIVE");
     i = ps.executeUpdate();
     /*
     ps = con.prepareStatement(updateQuery);
     ps.setDouble(1, cost);
     ps.setDouble(2, newNbv);
     ps.setDouble(3, vatableCost);
     ps.setDouble(4, vatAmt);
     ps.setDouble(5, whtAmt);
     ps.setDouble(6, accumDep);
     ps.setString(7, wh_tax);
     ps.setInt(8, wht_percent);
     ps.setString(9, subject_to_vat);
     ps.setString(10,vendorAcct);
     ps.setInt(11, Integer.parseInt(vendorId));
    // ps.setString(12, "APPROVED");
     ps.setString(12, assetId);

     i = ps.executeUpdate();
*/
     } catch (Exception e) {
     String warning = "WARNING:Error inserting am_asset_improvement ->" +
            e.getMessage();
     System.out.println(warning);
     e.printStackTrace();
     } finally {
     closeConnection(con, ps);
     }
     return i;
     }
    public String[] raiseEntryInfo(String asset_id) {
        String[] result = new String[4];
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        String query = "select description, branch_id, subject_to_vat, wh_tax from am_asset "
                + "where asset_id = '" + asset_id + " '";
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();


            while (rs.next()) {
                result[0] = rs.getString(1) == null ? "" : rs.getString(1);
                result[1] = rs.getString(2) == null ? "" : rs.getString(2);
                result[2] = rs.getString(3) == null ? "" : rs.getString(3);
                result[3] = rs.getString(4) == null ? "" : rs.getString(4);


            }
        } catch (Exception er) {
            System.out.println("Error in Query- raiseEntryInfo() in class ApprovalRecords... ->" + er);
            er.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
        return result;
    }    

    public int insertAssetMaintanance(double cost, double nbv, String assetId,
                 double costIncrease, String revalueReason,
                 String revalueDate,
                 int userId, double vatableCost, double vatAmt,
                 double whtAmt,
                 String vendorAcct, String raiseEntry,
                 double accumDep,
                 double oldCost, double oldVatableCost,
                 double oldVatAmt, double oldWhtAmt,
                 double oldNbv, double oldAccumDep,
                 String effDate,String mtid,String wh_tax_cb, int selectTax,String subject_to_vat,
                 double newVC,double newCP,double newNBV,
                 double newVA,double newWA,String vendorId,String vendorIdOld,
                 String vendorAccOld, String description,String categoryCode,String branchCode,String lpo,String invoiceNum,int assetCode,String integrify) {
 
//	System.out.println("revalueDate: "+revalueDate);  
//	System.out.println("effDate: "+effDate);  
    	
	if (revalueDate == null || revalueDate.equals("")) {
		revalueDate = null;
    }
    if (effDate == null || effDate.equals("")) {
    	effDate = null;
    }
   //  nbv += cost;
   //  costPrice += cost;
     int i = 0;

    // String updateQuery = "UPDATE AM_ASSET SET COST_PRICE = COST_PRICE + ?," +
           // " NBV = NBV + ?,VATABLE_COST = VATABLE_COST + ?, VAT = VAT + ?, WH_TAX_AMOUNT = WH_TAX_AMOUNT + ?, ACCUM_DEP = ACCUM_DEP + ? WHERE ASSET_ID = ?";

     String insertQuery =
     "INSERT INTO am_asset_improvement(ASSET_ID,COST_INCREASE,REVALUE_REASON," +
     "REVALUE_DATE,USER_ID,COST_PRICE,VATABLE_COST,VAT_AMOUNT,WHT_AMOUNT,R_VENDOR_AC,RAISE_ENTRY,NBV,ACCUM_DEP," +
     "OLD_COST_PRICE,OLD_VATABLE_COST,OLD_VAT_AMOUNT,OLD_WHT_AMOUNT,OLD_NBV,OLD_ACCUM_DEP,EFFDATE,REVALUE_ID,WH_TAX,"+
     "WHT_PERCENT,SUBJECT_TO_VAT,NEW_VATABLE_COST, NEW_COST_PRICE,NEW_NBV,NEW_VAT_AMOUNT,NEW_WHT_AMOUNT," +
     "R_VENDOR_ID,OLD_VENDOR_ACC,OLD_VENDOR_ID,DESCRIPTION,CATEGORY_CODE,BRANCH_CODE,lpoNum,invoice_no,INTEGRIFY,asset_code,approval_Status)"+
     " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

     Connection con = null;
     PreparedStatement ps = null;

     try {
     con = getConnection();

     ps = con.prepareStatement(insertQuery);
     ps.setString(1, assetId);
     ps.setDouble(2, costIncrease);
     ps.setString(3, revalueReason);
     ps.setString(4, revalueDate);
     ps.setInt(5, userId);
     ps.setDouble(6, cost);
     ps.setDouble(7, vatableCost);
     ps.setDouble(8, vatAmt);
     ps.setDouble(9, whtAmt);
     ps.setString(10, vendorAcct);
     ps.setString(11, raiseEntry);
     ps.setDouble(12, nbv);
     ps.setDouble(13, accumDep);
     ps.setDouble(14, oldCost);
     ps.setDouble(15, oldVatableCost);
     ps.setDouble(16, oldVatAmt);
     ps.setDouble(17, oldWhtAmt);
     ps.setDouble(18, oldNbv);
     ps.setDouble(19, oldAccumDep);
     ps.setString(20, effDate);
     ps.setString(21, mtid);
     ps.setString(22, wh_tax_cb);
     ps.setInt(23, selectTax);
     ps.setString(24, subject_to_vat);
     ps.setDouble(25, newVC);
     ps.setDouble(26, newCP);
     ps.setDouble(27, newNBV);
     ps.setDouble(28, newVA);
     ps.setDouble(29, newWA);
     ps.setInt(30, Integer.parseInt(vendorId));
     ps.setString(31, vendorAccOld);
     ps.setInt(32, Integer.parseInt(vendorIdOld));
     ps.setString(33, description);
     ps.setString(34, categoryCode);
     ps.setString(35, branchCode);
     ps.setString(36, lpo);
     ps.setString(37, invoiceNum);
     ps.setString(38, integrify);
     ps.setInt(39,assetCode);
     ps.setString(40, "PENDING");
     i = ps.executeUpdate();

    // ps = con.prepareStatement(updateQuery);
     //ps.setDouble(1, cost);
     //ps.setDouble(2, nbv);
     //ps.setDouble(3, vatableCost);
     //ps.setDouble(4, vatAmt);
     //ps.setDouble(5, whtAmt);
     ///ps.setDouble(6, accumDep);
     //ps.setString(7, assetId);

     //i = ps.executeUpdate();

     } catch (Exception e) {
     String warning = "WARNING:Error inserting am_asset_improvement Revaluation ->" +
            e.getMessage();
     System.out.println(warning);
     e.printStackTrace();
     } finally {
     closeConnection(con, ps);
     }
     return i;
     }
    public boolean insertApproval(String id, String description, String page, String flag,
            String partPay, String UserId, String Branch, String subjectToVat, 
            String whTax, String url,String tranId,int assetCode) {
        boolean done = true;
        flag = "Y";
        Connection con = null;
        PreparedStatement ps = null;
        String query = "INSERT INTO [am_raisentry_post](Id,Description,Page,Flag,partPay,"
                + "UserId,Branch,subjectToVat,whTax,url,trans_id,asset_code)"
                + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, id);
            ps.setString(2, description);
            ps.setString(3, page);
            ps.setString(4, flag);
            ps.setString(5, partPay);
            ps.setString(6, UserId);
            ps.setString(7, Branch);
            ps.setString(8, subjectToVat);
            ps.setString(9, whTax);
            ps.setString(10, url);
            ps.setString(11, tranId);
            ps.setInt(12, assetCode);
            ps.execute();

        } catch (Exception ex) {
            done = false;
            System.out.println(
                    "WARNING:cannot insert am_raisentry_post->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;
    }
    public int updateRepostInfo(int mtid) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int i = 0;
        String updateQuery =
                "UPDATE AM_ASSET_APPROVAL SET PROCESS_STATUS ='RP'WHERE TRANSACTION_ID = ?";
        try {
            con = getConnection();
            ps = con.prepareStatement(updateQuery);
            ps.setInt(1, mtid);
            i = ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("INFO:Error Updating Repost Information AssetManager:updateRepostInfo() -> " +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return i;
    }


	public long createGroup(String integrifyId,String Description,String RegistrationNo,String VendorAC,String Datepurchased,
			String AssetMake,String AssetModel,String AssetSerialNo,String AssetEngineNo,String SupplierName,String AssetUser,
			String AssetMaintenance,double CostPrice,double VatableCost,String AuthorizedBy,String WhTax,double whtaxamount,String PostingDate,String EffectiveDate,
			String PurchaseReason,String SubjectTOVat,String AssetStatus,String State,String Driver,String UserID,String branchCode,
			String sectionCode,String deptCode,String categoryCode,String barCode,String sbuCode,String lpo,String invoiceNo,String supervisor,
			String posted,String assetId,String assetCode,String assettype,String branch_id,String dept_id,String section_id,
			String category_id,double vatamount,String residualvalue,int whtaxvalue,int noofitems,
			String location,String memovalue,String memo, String spare_1,String spare_2,String multiple) throws Exception,Throwable
	{
	    Connection con = null;
	    PreparedStatement ps1 = null;
		String rate = findObject("select dep_rate from am_ad_category where category_id = "+category_id+"");
	//	aprecords = new ApprovalRecords();
//		String location = "";
		String vat_amount = "0.0";
		String vatable_cost = "0.0";
		String wh_tax_amount = "0";
		String province = "0";
		String noOfMonths = "0";
		String residual_value = "0";
		String warrantyStartDate = null;   
		String expiryDate = null;
//		String memo = "N";
//		String memoValue = "0";
//		double costPrice =  newassettrans.getCostPrice();
		String amountPTD = "0.0";
//		System.out.println("createGroup spare_1: "+spare_1+" createGroup  spare_2: "+spare_2);
//		String integrify = newassettrans.getIntegrifyId();
//		System.out.println(">>>>>>>>> INSIDE CREATE GROUP IN GROUP ASSET BEAN <<<<<<<<<< ");
		String require_depreciation = "Y";
	    String require_redistribution = "N";
	    String who_to_remind = "";
	    String email_1 = "";
	    String who_to_remind_2 = "";
	    String email2 = "";
	    String raise_entry = "N";    
	 //   String spare_1 = "";
	    String status = "";   
	    String user_id = "";
//	    String multiple = "N";
	//    String spare_2 = "";
	    String partPAY = "N";
	    String fullyPAID = "Y";
	    String deferPay = "N";
	    int selectTax = 0;
	    String MacAddress = "";
	    String SystemIp = "";
	    String section = ""; 
	    double accum_dep = 0.0;
	    String depreciation_end_date = "";
        String strnewDateMonth = "";
    	int purchase_start_year = Integer.parseInt(EffectiveDate.substring(0,4));
    	int purchase_start_month = Integer.parseInt(EffectiveDate.substring(5,7));
    	int purchase_start_day = Integer.parseInt(EffectiveDate.substring(8,10));    		
    	int newDateYear= purchase_start_year;
    	int newDateMonth = purchase_start_month + 1;
    	String newDateDay ="01";
    	if(newDateMonth == 10){strnewDateMonth = "10";}
    	if(newDateMonth == 11){strnewDateMonth = "11";}
    	if(newDateMonth == 12){strnewDateMonth = "12";}
      if (newDateMonth > 12)
       { 
         newDateMonth = 01;
         strnewDateMonth = "01";
         newDateYear = newDateYear + 1;
       }
    	
    	if (newDateMonth < 10)
       { 
    		int lengthfld = String.valueOf(newDateMonth).length();
    		if(newDateMonth == 7){strnewDateMonth = "0"+"7";}
    		if(newDateMonth == 1){strnewDateMonth = "0"+"1";}
    		if(newDateMonth == 2){strnewDateMonth = "0"+"2";}
    		if(newDateMonth == 3){strnewDateMonth = "0"+"3";}
    		if(newDateMonth == 4){strnewDateMonth = "0"+"4";}
    		if(newDateMonth == 5){strnewDateMonth = "0"+"5";}
    		if(newDateMonth == 6){strnewDateMonth = "0"+"6";}
    		if(newDateMonth == 8){strnewDateMonth = "0"+"8";}
    		if(newDateMonth == 9){strnewDateMonth = "0"+"9";}
       }
 //   	System.out.println("=====EffectiveDate=====?  "+EffectiveDate);	 
    	String depreciation_start_date = newDateDay+'-'+strnewDateMonth+'-'+newDateYear;
    	String start_date_depreciation = newDateDay+'-'+strnewDateMonth+'-'+newDateYear;
//    	System.out.println("=====depreciation_start_date=====?  "+depreciation_start_date);	 
//        System.out.println("=====rate=====?  "+rate);
        String vals = rate+","+depreciation_start_date;
 //         System.out.println("Values for Calculating Depreciation Date In createGroup: "+vals);
          depreciation_end_date = getDepEndDate(vals);
//       System.out.println("=====depreciation_end_date=====?  "+depreciation_end_date);	    
		StringBuffer b = new StringBuffer(400);
                StringBuffer sb = new StringBuffer(400);
		Codes code = new Codes();
		/*if (noofitems == null || noofitems.equals("")) {
			noofitems = "0";
		}*/
	/*	if (province == null || province.equals("")) {
			province = "0";
		}*/
		int itemsCount = noofitems;
/*		if (noOfMonths == null || noOfMonths.equals("")) {
			noOfMonths = "0";
		}*/
		if (warrantyStartDate == null || warrantyStartDate.equals("")) {
			warrantyStartDate = null;
		}
		if (expiryDate == null || expiryDate.equals("")) {
			expiryDate = null;
		}
		if(supervisor.equals("") ||supervisor=="0")
		{
			supervisor= UserID;
		}
		if (AssetMake == null || AssetMake.equals(""))
		{
			AssetMake="0";
		}
	    if (location == null || location.equals("")) {
	        location = "0";
	    }
	    int reccount = 0;
//		System.out.println(">>>>>>>>> itemsCount BEFORE CALLING CREATE GROUP MAIN IN GROUP ASSET BEAN <<<<<<<<<< "+itemsCount);
		long gid = createGroupMain(integrifyId,Description,RegistrationNo,VendorAC,Datepurchased,
				AssetMake,AssetModel,AssetSerialNo,AssetEngineNo,SupplierName,AssetUser,
				AssetMaintenance,CostPrice,VatableCost,AuthorizedBy,WhTax,whtaxamount,PostingDate,depreciation_start_date,
				PurchaseReason,SubjectTOVat,AssetStatus,State,Driver,UserID,branchCode,
				sectionCode,deptCode,categoryCode,barCode,sbuCode,lpo,invoiceNo,supervisor,
				posted,assetId,assetCode,assettype,branch_id,dept_id,section_id,category_id,vatamount,
				residualvalue,whtaxvalue,noofitems,warrantyStartDate,expiryDate,depreciation_end_date,
				SystemIp,require_depreciation,accum_dep,rate,require_redistribution,spare_1,spare_2);

		for (int x = 0; x < itemsCount; x++)
		{
			reccount = reccount +1;
			try {

			//asset_id = new legend.AutoIDSetup().getIdentity(branch_id,department_id, section_id, category_id);
			String asset_id = new ApplicationHelper().getGeneratedId("am_group_asset");
                        con = getConnection();

                        String query = "";
		//System.out.println(">>>>>>>>>  GENERATED ASSET_ID <<<<<<<<<< " + asset_id);
      if(assettype.equalsIgnoreCase("C")){
		query = "INSERT INTO AM_GROUP_ASSET( ASSET_ID,"
			+ "REGISTRATION_NO,BRANCH_ID,DEPT_ID,"
			+ "CATEGORY_ID,SECTION_ID,DESCRIPTION,"
			+ "VENDOR_AC,DATE_PURCHASED,DEP_RATE,"
			+ "ASSET_MAKE,ASSET_MODEL,ASSET_SERIAL_NO,"
			+ "ASSET_ENGINE_NO,SUPPLIER_NAME,"
			+ "ASSET_USER,ASSET_MAINTENANCE,"
			+ "COST_PRICE,DEP_END_DATE,RESIDUAL_VALUE,"
			+ "AUTHORIZED_BY,WH_TAX,WH_TAX_AMOUNT,"
			+ "POSTING_DATE,EFFECTIVE_DATE,"
			+ "PURCHASE_REASON,LOCATION,VATABLE_COST,"
			+ "VAT,REQ_DEPRECIATION,SUBJECT_TO_VAT,"
			+ "WHO_TO_REM,	EMAIL1,	WHO_TO_REM_2,"
			+ "EMAIL2,	STATE,DRIVER,SPARE_1,SPARE_2,[USER_ID], PROVINCE,"
			+ " WAR_START_DATE, WAR_MONTH, WAR_EXPIRY_DATE,BRANCH_CODE,DEPT_CODE,"
			+ "SECTION_CODE,CATEGORY_CODE,GROUP_ID,AMOUNT_PTD,AMOUNT_REM,ACCUM_DEP," +
                        "PART_PAY,FULLY_PAID,Asset_Status,"
			+ "supervisor,LPO,BAR_CODE,req_redistribution,Raise_entry,defer_pay,process_flag ," +
                        "SBU_CODE,post_flag,Invoice_no,workstationIp,INTEGRIFY,RECCOUNT"
			+ " )	VALUES " +
					"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
					"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			}
		if(assettype.equalsIgnoreCase("U")){
			
			query = "INSERT INTO AM_GROUP_ASSET_UNCAPITALIZED( ASSET_ID,"
				+ "REGISTRATION_NO,BRANCH_ID,DEPT_ID,"
				+ "CATEGORY_ID,SECTION_ID,DESCRIPTION,"
				+ "VENDOR_AC,DATE_PURCHASED,DEP_RATE,"
				+ "ASSET_MAKE,ASSET_MODEL,ASSET_SERIAL_NO,"
				+ "ASSET_ENGINE_NO,SUPPLIER_NAME,"
				+ "ASSET_USER,ASSET_MAINTENANCE,"
				+ "COST_PRICE,DEP_END_DATE,RESIDUAL_VALUE,"
				+ "AUTHORIZED_BY,WH_TAX,WH_TAX_AMOUNT,"
				+ "POSTING_DATE,EFFECTIVE_DATE,"
				+ "PURCHASE_REASON,LOCATION,VATABLE_COST,"
				+ "VAT,REQ_DEPRECIATION,SUBJECT_TO_VAT,"
				+ "WHO_TO_REM,	EMAIL1,	WHO_TO_REM_2,"
				+ "EMAIL2,	STATE,DRIVER,SPARE_1,SPARE_2,[USER_ID], PROVINCE,"
				+ " WAR_START_DATE, WAR_MONTH, WAR_EXPIRY_DATE,BRANCH_CODE,DEPT_CODE,"
				+ "SECTION_CODE,CATEGORY_CODE,GROUP_ID,AMOUNT_PTD,AMOUNT_REM,ACCUM_DEP," +
	                        "PART_PAY,FULLY_PAID,Asset_Status,"
				+ "supervisor,LPO,BAR_CODE,req_redistribution,Raise_entry,defer_pay,process_flag ," +
	                        "SBU_CODE,post_flag,Invoice_no,workstationIp,INTEGRIFY,RECCOUNT"
				+ " )	VALUES " +
						"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
						"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";			
		}
		
		ps1 = con.prepareStatement(query);
		
		amountPTD = amountPTD.replaceAll(",","");
		ps1.setString(1, asset_id);
		ps1.setString(2, RegistrationNo);
		ps1.setString(3, branch_id);
		ps1.setString(4, dept_id);
		ps1.setString(5, category_id);
		ps1.setString(6, section_id);
		ps1.setString(7, Description);
		ps1.setString(8, VendorAC);
		ps1.setString(9, Datepurchased);        
		ps1.setString(10, rate);
		ps1.setString(11, AssetMake);
		ps1.setString(12, AssetModel);
		ps1.setString(13, AssetSerialNo);
		ps1.setString(14, AssetEngineNo);
		ps1.setInt(15, Integer.parseInt(SupplierName));
		ps1.setString(16, AssetUser);
		ps1.setInt(17, Integer.parseInt(AssetMaintenance));
		ps1.setDouble(18, CostPrice / itemsCount);
		ps1.setDate(19, dateConvert(depreciation_end_date));
		ps1.setDouble(20, Double.parseDouble(residualvalue));
		ps1.setString(21, AuthorizedBy);
		ps1.setString(22, WhTax);
		ps1.setDouble(23, whtaxamount / itemsCount);
		ps1.setString(24,PostingDate);
		ps1.setDate(25, dateConvert(EffectiveDate));
//		System.out.println("=====EffectiveDate=====?  "+EffectiveDate);
		ps1.setString(26, PurchaseReason);
		ps1.setInt(27, Integer.parseInt(location));
		ps1.setDouble(28, VatableCost / itemsCount);
		ps1.setDouble(29, whtaxamount / itemsCount);
		ps1.setString(30, require_depreciation);
		ps1.setString(31, SubjectTOVat);
		ps1.setString(32, who_to_remind);
		ps1.setString(33, email_1);
		ps1.setString(34, who_to_remind_2);
		ps1.setString(35, email2);
		ps1.setInt(36, Integer.parseInt(State));
		ps1.setInt(37, Integer.parseInt(Driver));
		ps1.setString(38, spare_1);
		ps1.setString(39, spare_2);
		ps1.setString(40, user_id);
		ps1.setInt(41, Integer.parseInt(province));
//		System.out.println("=====Before WarrantyStartDate=====?  ");
		ps1.setDate(42, dateConvert(warrantyStartDate));
//		System.out.println("=====warrantyStartDate=====?  "+warrantyStartDate);
		ps1.setInt(43, Integer.parseInt(noOfMonths));
		ps1.setDate(44, dateConvert(expiryDate));
//		System.out.println("=====expiryDate=====?  "+expiryDate);
		ps1.setString(45,branchCode);
		ps1.setString(46,deptCode);
		ps1.setString(47,sectionCode);
		ps1.setString(48,categoryCode);
//		System.out.println("=====gid=====?  "+gid);
		ps1.setLong(49, gid);
//		System.out.println("=====CostPrice=====?  "+CostPrice+"  itemsCount:  "+itemsCount);
		ps1.setDouble(50, CostPrice / itemsCount);
//		System.out.println("=====itemsCount=====?  "+itemsCount);
		ps1.setDouble(51, ((VatableCost / itemsCount)-(CostPrice/ itemsCount)));
		ps1.setDouble(52, accum_dep);
		ps1.setString(53,"N");
		ps1.setString(54,"Y");
        ps1.setString(55,status);
        ps1.setString(56,supervisor);
        ps1.setString(57,lpo);
        ps1.setString(58,barCode);
 //       System.out.println("=====require_redistribution=====?  "+require_redistribution);
        ps1.setString(59,require_redistribution);
        ps1.setString(60,"N");
        ps1.setString(61,"N");
        ps1.setString(62,"N");
 //       System.out.println("=====sbuCode=====?  "+sbuCode);
        ps1.setString(63, sbuCode);
        ps1.setString(64, "P");
//        System.out.println("=====invoiceNo=====?  "+invoiceNo);
        ps1.setString(65, invoiceNo);
        ps1.setString(66, SystemIp); 
        ps1.setString(67, integrifyId); 
        ps1.setInt(68, reccount);
 //       System.out.println("=====SystemIp=====?  "+SystemIp);
//        System.out.println(">>>>>>>>>  Create GroupMain query <<<<<<<<<< " + query);
        boolean result = ps1.execute();
//         ps1.execute();
 //       System.out.println("=====Result End=====?  "+result);
	archiveUpdate(asset_id,itemsCount,gid,integrifyId,Description,RegistrationNo,VendorAC,Datepurchased,
			AssetMake,AssetModel,AssetSerialNo,AssetEngineNo,SupplierName,AssetUser,
			AssetMaintenance,CostPrice,VatableCost,AuthorizedBy,WhTax,whtaxamount,PostingDate,EffectiveDate,
			PurchaseReason,SubjectTOVat,AssetStatus,State,Driver,UserID,branchCode,
			sectionCode,deptCode,categoryCode,barCode,sbuCode,lpo,invoiceNo,supervisor,
			posted,assetId,assetCode,assettype,branch_id,dept_id,section_id,category_id,
			vatamount,residualvalue,whtaxvalue,noofitems,depreciation_end_date,SystemIp,require_depreciation,
			accum_dep,rate,require_redistribution,reccount,spare_1,spare_1);
//	 System.out.println("=====Result End=====?  "+result);
			}
			
                        catch (Exception r) {
				System.out.println("INFO: Error creating group aset >>" + r);
			} finally {
				freeResource(); 
			}
        }
		//ad.updateGroupAssetStatus(Long.toString(gid));
		return gid;
	}



        public void archiveUpdate(String asset_id,int itemsCount,long gid,String integrifyId,String Description,String RegistrationNo,String VendorAC,String Datepurchased,
    			String AssetMake,String AssetModel,String AssetSerialNo,String AssetEngineNo,String SupplierName,String AssetUser,
    			String AssetMaintenance,double CostPrice,double VatableCost,String AuthorizedBy,String WhTax,double whtaxamount,String PostingDate,String EffectiveDate,
    			String PurchaseReason,String SubjectTOVat,String AssetStatus,String State,String Driver,String UserID,String branchCode,
    			String sectionCode,String deptCode,String categoryCode,String barCode,String sbuCode,String lpo,String invoiceNo,String supervisor,
    			String posted,String assetId,String assetCode,String assettype,String branch_id,String dept_id,String section_id,
    			String category_id,double vatamount,String residualvalue,int whtaxvalue,int recordNo,String depreciation_end_date,
    			String SystemIp,String require_depreciation,double accum_dep,String rate,String require_redistribution,int reccount,String spare_1,String spare_2)
        {
            Codes code = new Codes();
            String amountPTD = "0.0";
    	    String multiple = "N";
//    	    String spare_2 = "";
    	    String partPAY = "N";
    	    String fullyPAID = "Y";
    	    String deferPay = "N";
    	    String who_to_remind = "";
    	    String email_1 = "";
    	    String who_to_remind_2 = "";
    	    String email2 = "";
    	    String raise_entry = "N";    
//    	    String spare_1 = "";
    	    String province = "";
    	    String noOfMonths = "";
    	    String warrantyStartDate = "";
    	    String expiryDate = "";
    	    String location = "";
    		if (warrantyStartDate == null || warrantyStartDate.equals("")) {
    			warrantyStartDate = null;
    		}
    		if (expiryDate == null || expiryDate.equals("")) {
    			expiryDate = null;
    		}
    		if(supervisor.equals("") ||supervisor=="0")
    		{
    			supervisor= UserID;
    		}
    		if (AssetMake == null || AssetMake.equals(""))
    		{
    			AssetMake="0";
    		}    	    
    	    if (location == null || location.equals("")) {
    	        location = "0";
    	    }	  
    	    if (province == null || province.equals("")) {
    	    	province = "0";
    	    }	
    	    if (noOfMonths == null || noOfMonths.equals("")) {
    	    	noOfMonths = "0";
    	    }    	    
            String query = "INSERT INTO AM_GROUP_ASSET_ARCHIVE( ASSET_ID,"
			+ "REGISTRATION_NO,BRANCH_ID,DEPT_ID,"
			+ "CATEGORY_ID,SECTION_ID,DESCRIPTION,"
			+ "VENDOR_AC,DATE_PURCHASED,DEP_RATE,"
			+ "ASSET_MAKE,ASSET_MODEL,ASSET_SERIAL_NO,"
			+ "ASSET_ENGINE_NO,SUPPLIER_NAME,"
			+ "ASSET_USER,ASSET_MAINTENANCE,"
			+ "COST_PRICE,DEP_END_DATE,RESIDUAL_VALUE,"
			+ "AUTHORIZED_BY,WH_TAX,WH_TAX_AMOUNT,"
			+ "POSTING_DATE,EFFECTIVE_DATE,"
			+ "PURCHASE_REASON,LOCATION,VATABLE_COST,"
			+ "VAT,REQ_DEPRECIATION,SUBJECT_TO_VAT,"
			+ "WHO_TO_REM,	EMAIL1,	WHO_TO_REM_2,"
			+ "EMAIL2,	STATE,DRIVER,SPARE_1,SPARE_2,[USER_ID], PROVINCE,"
			+ " WAR_START_DATE, WAR_MONTH, WAR_EXPIRY_DATE,BRANCH_CODE,DEPT_CODE,"
			+ "SECTION_CODE,CATEGORY_CODE,GROUP_ID,AMOUNT_PTD,AMOUNT_REM,ACCUM_DEP," +
                        "PART_PAY,FULLY_PAID,Asset_Status,"
			+ "supervisor,LPO,BAR_CODE,req_redistribution,Raise_entry,defer_pay,process_flag ," +
                        "SBU_CODE,post_flag,Invoice_no,workstationIp,INTEGRIFY,RECCOUNT"
			+ " )	VALUES " +
					"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
					"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            try {
            con = getConnection();
            ps = con.prepareStatement(query);
		//b.append(query);
		amountPTD = amountPTD.replaceAll(",","");
		//b.append(asset_id);
//		System.out.println("=====Start archiveUpdate=====?  ");
		ps.setString(1, asset_id);
		ps.setString(2, RegistrationNo);
		ps.setString(3, branch_id);
		ps.setString(4, dept_id);
		ps.setString(5, category_id);
		ps.setString(6, section_id);
		ps.setString(7, Description);
		ps.setString(8, VendorAC);
		ps.setString(9, Datepurchased);
		ps.setString(10, rate);
		ps.setString(11, AssetMake);
		ps.setString(12, AssetModel);
		ps.setString(13, AssetSerialNo);
		ps.setString(14, AssetEngineNo);
		ps.setInt(15, Integer.parseInt(SupplierName));
		ps.setString(16, AssetUser);
		ps.setInt(17, Integer.parseInt(AssetMaintenance));
		ps.setDouble(18, CostPrice / itemsCount);
		ps.setDate(19, dateConvert(depreciation_end_date));
		ps.setDouble(20, Double.parseDouble(residualvalue));
		ps.setString(21, AuthorizedBy);
		ps.setString(22, WhTax);
		ps.setDouble(23, whtaxamount / itemsCount);
		ps.setString(24,PostingDate);
		ps.setDate(25, dateConvert(EffectiveDate));
		ps.setString(26, PurchaseReason);
		ps.setInt(27, Integer.parseInt(location));
		ps.setDouble(28, VatableCost / itemsCount);
		ps.setDouble(29, vatamount / itemsCount);
		ps.setString(30, require_depreciation);
		ps.setString(31, SubjectTOVat);
		ps.setString(32, who_to_remind);
		ps.setString(33, email_1);
		ps.setString(34, who_to_remind_2);
		ps.setString(35, email2);
		ps.setInt(36, Integer.parseInt(State));
		ps.setInt(37, Integer.parseInt(Driver));
		ps.setString(38, spare_1);
		ps.setString(39, spare_2);
		ps.setString(40, UserID);
		ps.setInt(41, Integer.parseInt(province));
//		System.out.println("=====archiveUpdate warrantyStartDate=====?  "+warrantyStartDate);
		ps.setDate(42, dateConvert(warrantyStartDate));
		ps.setInt(43, Integer.parseInt(noOfMonths));
//		System.out.println("=====archiveUpdate expiryDate=====?  "+expiryDate);
		ps.setDate(44, dateConvert(expiryDate));
		ps.setString(45,branchCode);
		ps.setString(46,deptCode);
		ps.setString(47,sectionCode);
		ps.setString(48,categoryCode);
		ps.setLong(49, gid);
		ps.setDouble(50, CostPrice / itemsCount);
		ps.setDouble(51, ((CostPrice / itemsCount)-(CostPrice/ itemsCount)));
		ps.setDouble(52, accum_dep);
		ps.setString(53,"N");
		ps.setString(54,"Y");
        ps.setString(55,AssetStatus);
        ps.setString(56,supervisor);
        ps.setString(57,lpo);
        ps.setString(58,barCode);
        ps.setString(59,require_redistribution);
        ps.setString(60,"N");
        ps.setString(61,"N");
        ps.setString(62,"N");
        ps.setString(63, sbuCode);
        ps.setString(64, "P");
        ps.setString(65, invoiceNo);
        ps.setString(66, SystemIp);
        ps.setString(67, integrifyId); 
        ps.setInt(68, reccount);
        boolean result = ps.execute();
            }
catch (Exception r) {
				System.out.println("INFO: Error creating group aset in archive >>" + r);
			} finally {
				closeConnection(con, ps);
			}
        }


    	public long createGroupMain(String integrifyId,String Description,String RegistrationNo,String VendorAC,String Datepurchased,
    			String AssetMake,String AssetModel,String AssetSerialNo,String AssetEngineNo,String SupplierName,String AssetUser,
    			String AssetMaintenance,double CostPrice,double VatableCost,String AuthorizedBy,String WhTax,double whtaxamount,String PostingDate,String EffectiveDate,
    			String PurchaseReason,String SubjectTOVat,String AssetStatus,String State,String Driver,String UserID,String branchCode,
    			String sectionCode,String deptCode,String categoryCode,String barCode,String sbuCode,String lpo,String invoiceNo,String supervisor,
    			String posted,String assetId,String assetCode,String assettype,String branch_id,String dept_id,String section_id,String category_id,
    			double vatamount,String residualvalue,int whtaxvalue,int recordNo,String warrantyStartDate,String expiryDate,
    			String depreciation_end_date,String SystemIp,String require_depreciation,double accum_dep,String rate,String require_redistribution,String spare_1,String spare_2) throws Exception {
 //   		System.out.println(">>>>>> INSIDE CREATE GROUP MAIN OF GROUP ASSET BEAN <<<<<<");
    		System.out.println("createGroupMain spare_1: "+spare_1+" createGroupMain  spare_2: "+spare_2);
    		StringBuffer b = new StringBuffer(400);
    		String no_of_items = "";
    		String province = "";
    		String noOfMonths = "";
//    	    String spare_2 = "";
    	    String partPAY = "N";
    	    String fullyPAID = "Y";
    	    String deferPay = "N";
    	    String who_to_remind = "";
    	    String email_1 = "";
    	    String who_to_remind_2 = "";
    	    String email2 = "";
    	    String raise_entry = "N";    
//    	    String spare_1 = "";
    	    String location = "";
                    StringBuffer sbf = new StringBuffer(400);
    		//Codes code = new Codes();
    		if (no_of_items == null || no_of_items.equals("")) {
    			no_of_items = "0";
    		}
    		if (province == null || province.equals("")) {
    			province = "0";
    		}
    		int itemsCount = Integer.parseInt(no_of_items);
    		if (noOfMonths == null || noOfMonths.equals("")) {
    			noOfMonths = "0";
    		}
    		if (warrantyStartDate == null || warrantyStartDate.equals("")) {
    			warrantyStartDate = null;
    		}
    		if (expiryDate == null || expiryDate.equals("")) {
    			expiryDate = null;
    		}
    		if(supervisor.equals("")||supervisor=="0")
    		{
    			supervisor= UserID;
    		}
    		if (AssetMake == null || AssetMake.equals(""))
    		{
    			AssetMake="0";
    		}
    	    if (location == null || location.equals("")) {
    	        location = "0";
    	    }    		
    		long gid=0;
			//String group_id = findObject("select max(mt_id) from IA_MTID_TABLE where mt_tablename='am_asset_approval'");
			String group_id =  new ApplicationHelper().getGeneratedId("am_asset_approval");
//			 System.out.println("Before Generating gid >>>>>>>>>> "+group_id);
		double grpid = Double.parseDouble(group_id);
		//gid = Long.parseLong(String.valueOf(grpid));
		gid = Long.parseLong(group_id);
    		String query = "INSERT INTO AM_GROUP_ASSET_MAIN(GROUP_ID,QUANTITY,"
    			+ "REGISTRATION_NO,BRANCH_ID,DEPT_ID,"
    			+ "CATEGORY_ID,SECTION_ID,DESCRIPTION,"
    			+ "VENDOR_AC,DATE_PURCHASED,DEP_RATE,ACCUM_DEP,"
    			+ "ASSET_MAKE,ASSET_MODEL,ASSET_SERIAL_NO,"
    			+ "ASSET_ENGINE_NO,SUPPLIER_NAME,"
    			+ "ASSET_USER,ASSET_MAINTENANCE,"
    			+ "COST_PRICE,DEP_END_DATE,RESIDUAL_VALUE,"
    			+ "AUTHORIZED_BY,WH_TAX,WH_TAX_AMOUNT,"
    			+ "POSTING_DATE,EFFECTIVE_DATE,"
    			+ "PURCHASE_REASON,LOCATION,VATABLE_COST,"
    			+ "VAT,REQ_DEPRECIATION,SUBJECT_TO_VAT,"
    			+ "WHO_TO_REM,	EMAIL1,	WHO_TO_REM_2,"
    			+ "EMAIL2,	STATE,DRIVER,SPARE_1,SPARE_2,[USER_ID], PROVINCE,"
    			+ " WAR_START_DATE, WAR_MONTH, WAR_EXPIRY_DATE,BRANCH_CODE,DEPT_CODE,"
    			+ "SECTION_CODE,CATEGORY_CODE,raise_entry,AMOUNT_PTD,AMOUNT_REM,"
    			+ "PART_PAY,FULLY_PAID,Asset_Status,supervisor,"
    			+ "LPO,BAR_CODE,req_redistribution,defer_pay ,Vatable_Cost_Bal,process_flag," +
                            "sbu_code,pend_GrpAssets,Invoice_No,workstationIp"
    			+ ")	VALUES(";
    		b.append(query);
    		b.append("'");
    		b.append(gid);
    		b.append("',");
    		b.append(itemsCount);
    		b.append(",'");
    		b.append(RegistrationNo);
    		b.append("',");
    		b.append(branch_id);
    		b.append(",");
    		b.append(dept_id);
    		b.append(",");
    		b.append(category_id);
    		b.append(",");
    		b.append(section_id);
    		b.append(",'");
    		b.append(Description);
    		b.append("','");
    		b.append(VendorAC);
    		b.append("','");
    		//System.out.println(DateManipulations.CalendarToDb(date_of_purchase));
    		b.append(Datepurchased);
    		b.append("',");
    		b.append(rate);
    		b.append(",");
    		b.append(accum_dep);
    		b.append(",");
    		b.append(AssetMake);
    		b.append(",'");
    		b.append(AssetModel);
    		b.append("','");
    		b.append(AssetSerialNo);
    		b.append("','");
    		b.append(AssetEngineNo);
    		b.append("',");
    		b.append(SupplierName);
    		b.append(",'");
    		b.append(AssetUser);
    		b.append("',");
    		b.append(AssetMaintenance);
    		b.append(",");
    		b.append(CostPrice);
    		b.append(",'");
    		// System.out.println(DateManipulations.CalendarToDb(date_of_purchase));
    		//b.append(DateManipulations.CalendarToDb(depreciation_end_date));
    		//System.out.println(">>>>>> depreciation_end_date before Convert <<<<<<"+depreciation_end_date);
    		//System.out.println(">>>>>> depreciation_end_date <<<<<<"+dateConvert(depreciation_end_date));
    		b.append(dateConvert(depreciation_end_date));
    		b.append("',");
    		b.append(residualvalue);
    		b.append(",'");
    		b.append(AuthorizedBy);
    		b.append("','");
    		b.append(WhTax);
    		b.append("',");
    		b.append(whtaxamount);
    		b.append(",'");
    		b.append(PostingDate);
    		b.append("','");
 //   		System.out.println(">>>>>> EffectiveDate <<<<<<"+dateConvert(EffectiveDate));
    		b.append(dateConvert(EffectiveDate));
    		b.append("','");
    		b.append(PurchaseReason);
    		b.append("',");
    		b.append(location);
    		b.append(",");
    		b.append(VatableCost);
    		b.append(",");
    		b.append(vatamount);
    		b.append(",'");
    		b.append(require_depreciation);
    		b.append("','");
    		b.append(SubjectTOVat);
    		b.append("','");
    		b.append(who_to_remind);
    		b.append("','");
    		b.append(email_1);
    		b.append("','");
    		b.append(who_to_remind_2);
    		b.append("','");
    		b.append(email2);
    		b.append("',");
    		b.append(State);
    		b.append(",");
    		b.append(Driver);
    		b.append(",'");
    		b.append(spare_1);
    		b.append("','");
    		b.append(spare_2);
    		b.append("',");
    		b.append(UserID + ",");
    		b.append(province);
    		b.append(",'");
    		//System.out.println(">>>>>> warrantyStartDate <<<<<<"+dateConvert(warrantyStartDate));
    		b.append(dateConvert(warrantyStartDate));
    		b.append("',");
    		b.append(noOfMonths);
    		b.append(",'");
 //   		System.out.println(">>>>>> expiryDate <<<<<<"+dateConvert(expiryDate));
    		b.append(dateConvert(expiryDate));
    		b.append("','");
    		b.append(branchCode);
    		b.append("','");
    		b.append(deptCode);
    		b.append("','");
    		b.append(sectionCode);
    		b.append("','");
    		b.append(categoryCode);
    		b.append("','");
    		//System.out.println("RAISE ENTRY IN AM_GROUP_ASSET_MAIN : " + raise_entry);
    		b.append("N");
    		b.append("',");
    		b.append(VatableCost);
    		b.append(",");
    		b.append((CostPrice)-(VatableCost));
    		b.append(",'");
    		b.append(partPAY);
    		b.append("','");
    		b.append(fullyPAID);
                    b.append("','");
                    b.append(AssetStatus);
                    b.append("','");
                    b.append(supervisor);
                    b.append("','");
                    b.append(lpo);
                    b.append("','");
                    b.append(barCode);
                    b.append("','");
                    b.append(require_redistribution);
                    b.append("','");
                    b.append(deferPay);
    		b.append("',");
    		b.append(VatableCost);
    		b.append(",'");
    		b.append("N");
    		b.append("','");
    		b.append(sbuCode);
    		b.append("',");
    		b.append(itemsCount);
                    b.append(",'");
    		b.append(invoiceNo);
                    b.append("','");
                    b.append(SystemIp);
    		b.append("')");
//    		 System.out.println("Query Used First >>>>>>>>>> " + b.toString());
    			try
    			{
 //   				 System.out.println("After Query Used First >>>>>>>>>> ");
    				getStatement().executeUpdate(b.toString());
//    				String group_id = findObject("select max(mt_id) from IA_MTID_TABLE where mt_tablename='am_asset_approval'");
//    				 System.out.println("Before Generating gid >>>>>>>>>> "+group_id);
//    			double grpid = Double.parseDouble(group_id);
    			//gid = Long.parseLong(String.valueOf(grpid));
//    			gid = Long.parseLong(group_id);
    				//long groupid = retrieveMaxGroupID();
//    				 System.out.println("After Before Generating gid >>>>>>>>>> " );
    				//String groupid = findObject("select MAX(group_id) from am_group_asset_main");
    			//	System.out.println("groupid:  "+groupid);
                    String query_archive = "INSERT INTO AM_GROUP_ASSET_MAIN_Archive(group_id,QUANTITY,"
    			+ "REGISTRATION_NO,BRANCH_ID,DEPT_ID,"
    			+ "CATEGORY_ID,SECTION_ID,DESCRIPTION,"
    			+ "VENDOR_AC,DATE_PURCHASED,DEP_RATE,ACCUM_DEP,"
    			+ "ASSET_MAKE,ASSET_MODEL,ASSET_SERIAL_NO,"
    			+ "ASSET_ENGINE_NO,SUPPLIER_NAME,"
    			+ "ASSET_USER,ASSET_MAINTENANCE,"
    			+ "COST_PRICE,DEP_END_DATE,RESIDUAL_VALUE,"
    			+ "AUTHORIZED_BY,WH_TAX,WH_TAX_AMOUNT,"
    			+ "POSTING_DATE,EFFECTIVE_DATE,"
    			+ "PURCHASE_REASON,LOCATION,VATABLE_COST,"
    			+ "VAT,REQ_DEPRECIATION,SUBJECT_TO_VAT,"
    			+ "WHO_TO_REM,	EMAIL1,	WHO_TO_REM_2,"
    			+ "EMAIL2,	STATE,DRIVER,SPARE_1,SPARE_2,[USER_ID], PROVINCE,"
    			+ " WAR_START_DATE, WAR_MONTH, WAR_EXPIRY_DATE,BRANCH_CODE,DEPT_CODE,"
    			+ "SECTION_CODE,CATEGORY_CODE,raise_entry,AMOUNT_PTD,AMOUNT_REM,"
    			+ "PART_PAY,FULLY_PAID,Asset_Status,supervisor,"
    			+ "LPO,BAR_CODE,req_redistribution,defer_pay ,Vatable_Cost_Bal,process_flag," +
                            "sbu_code,pend_GrpAssets,Invoice_No,workstationIp"
    			+ ")	VALUES(";
    		sbf.append(query_archive);
    		/*long gid = System.nanoTime()/1000;*/
    		sbf.append(group_id);
    		sbf.append(",");
    		sbf.append(itemsCount);
    		sbf.append(",'");
    		sbf.append(RegistrationNo);
    		sbf.append("',");
    		sbf.append(branch_id);
    		sbf.append(",");
    		sbf.append(dept_id);
    		sbf.append(",");
    		sbf.append(category_id);
    		sbf.append(",");
    		sbf.append(section_id);
    		sbf.append(",'");
    		sbf.append(Description);
    		sbf.append("','");
    		sbf.append(VendorAC);
    		sbf.append("','");
    		//System.out.println(DateManipulations.CalendarToDb(date_of_purchase));
    		sbf.append(Datepurchased);
    		sbf.append("',");
    		sbf.append(rate);
    		sbf.append(",");
    		sbf.append(accum_dep);
    		sbf.append(",");
    		sbf.append(AssetMake);
    		sbf.append(",'");
    		sbf.append(AssetModel);
    		sbf.append("','");
    		sbf.append(AssetSerialNo);
    		sbf.append("','");
    		sbf.append(AssetEngineNo);
    		sbf.append("',");
    		sbf.append(SupplierName);
    		sbf.append(",'");
    		sbf.append(AssetUser);
    		sbf.append("',");
    		sbf.append(AssetMaintenance);
    		sbf.append(",");
    		sbf.append(CostPrice);
    		sbf.append(",'");
    		sbf.append(dateConvert(depreciation_end_date));
    		sbf.append("',");
    		sbf.append(residualvalue);
    		sbf.append(",'");
    		sbf.append(AuthorizedBy);
    		sbf.append("','");
    		sbf.append(WhTax);
    		sbf.append("',");
    		sbf.append(whtaxamount);
    		sbf.append(",'");
    		sbf.append(PostingDate);
    		sbf.append("','");
//   		System.out.println(">>>>>> EffectiveDate 2 <<<<<<"+dateConvert(EffectiveDate));
    		sbf.append(dateConvert(EffectiveDate));
    		sbf.append("','");
    		sbf.append(PurchaseReason);
    		sbf.append("',");
    		sbf.append(location);
    		sbf.append(",");
    		sbf.append(VatableCost);
    		sbf.append(",");
    		sbf.append(vatamount);
    		sbf.append(",'");
    		sbf.append(require_depreciation);
    		sbf.append("','");
    		sbf.append(SubjectTOVat);
    		sbf.append("','");
    		sbf.append(who_to_remind);
    		sbf.append("','");
    		sbf.append(email_1);
    		sbf.append("','");
    		sbf.append(who_to_remind_2);
    		sbf.append("','");
    		sbf.append(email2);
    		sbf.append("',");
    		sbf.append(State);
    		sbf.append(",");
    		sbf.append(Driver);
    		sbf.append(",'");
    		sbf.append(spare_1);
    		sbf.append("','");
    		sbf.append(spare_2);
    		sbf.append("',");
    		sbf.append(UserID + ",");
    		sbf.append(province);
    		sbf.append(",'");
 //   		System.out.println(">>>>>> warrantyStartDate 2 <<<<<<"+dateConvert(warrantyStartDate));
    		sbf.append(dateConvert(warrantyStartDate));
    		sbf.append("',");
    		sbf.append(noOfMonths);
    		sbf.append(",'");
    		sbf.append(dateConvert(expiryDate));
    		sbf.append("','");
    		sbf.append(branchCode);
    		sbf.append("','");
    		sbf.append(deptCode);
    		sbf.append("','");
    		sbf.append(sectionCode);
    		sbf.append("','");
    		sbf.append(categoryCode);
    		sbf.append("','");
 //   		System.out.println("RAISE ENTRY IN AM_GROUP_ASSET_MAIN 2nd : " + raise_entry);
    		sbf.append("N");
    		sbf.append("',");
    		sbf.append(VatableCost);
    		sbf.append(",");
    		sbf.append((CostPrice)-(VatableCost));
    		sbf.append(",'");
    		sbf.append(partPAY);
    		sbf.append("','");
    		sbf.append(fullyPAID);
                    sbf.append("','");
                    sbf.append(AssetStatus);
                    sbf.append("','");
                    sbf.append(supervisor);
                    sbf.append("','");
                    sbf.append(lpo);
                    sbf.append("','");
                    sbf.append(barCode);
                    sbf.append("','");
                    sbf.append(require_redistribution);
                    sbf.append("','");
                    sbf.append(deferPay);
    		sbf.append("',");
    		sbf.append(VatableCost);
    		sbf.append(",'");
    		sbf.append("N");
    		sbf.append("','");
    		sbf.append(sbuCode);
    		sbf.append("',");
    		sbf.append(itemsCount);
                    sbf.append(",'");
    		sbf.append(invoiceNo);
                    sbf.append("','");
                    sbf.append(SystemIp);
    		sbf.append("')");
                    //ad.setPendingTrans(ad.setApprovalDataGroup(gid),"3");
//                    System.out.println("Query Used>>>>>>>>>> " + sbf.toString());
                    getStatement().executeUpdate(sbf.toString());
    			}
    			catch (Exception r)
    			{
    				System.out.println("INFO: Error creating AM_GROUP_ASSET_MAIN_Archive >>" + r);
    			}
    			finally
    			{
    				closeConnection(con, ps);
    			}

    		return gid;
    	}
    	public int insertGroupAssetRecord(String integrifyId,String Description,String RegistrationNo,String VendorAC,String Datepurchased,
    			String AssetMake,String AssetModel,String AssetSerialNo,String AssetEngineNo,String SupplierName,String AssetUser,
    			String AssetMaintenance,double CostPrice,double VatableCost,String AuthorizedBy,String WhTax,double whtaxamount,String PostingDate,String EffectiveDate,
    			String PurchaseReason,String SubjectTOVat,String AssetStatus,String State,String Driver,String UserID,String branchCode,
    			String sectionCode,String deptCode,String categoryCode,String barCode,String sbuCode,String lpo,String invoiceNo,String supervisor,
    			String posted,String assetId,String assetCode,String assettype,String branch_id,String dept_id,String section_id,String category_id,double vatamount,String residualvalue,int whtaxvalue,int noofitems,
    			String location,String memovalue,String memo, String spare1,String spare2,String multiple) throws Exception, Throwable {
 //   		System.out.println("INSIDE INSERT GROUP ASSET RECORD OF GROUP ASSET BEAN");
//    		System.out.println("insertGroupAssetRecord spare1: "+spare1+" insertGroupAssetRecord  spare2: "+spare2);
    		String[] budget = getBudgetInfo();
    		double[] bugdetvalues = getBudgetValues(branchCode,categoryCode);
    		int DONE = 1; // everything is oK
    		int BUDGETENFORCED = 1; // EF budget = Yes, CF = NO, ERROR_FLAG
    		int BUDGETENFORCEDCF = 2; // EF budget = Yes, CF = Yes, ERROR_FLAG
    		int ASSETPURCHASEBD = 3; // asset falls into no quarter purchase date
    		int result = 0;
    		long [] value = new long[2];

    		// older than bugdet
    		String Q = getQuarter(Datepurchased);
    		//System.out.println("BUDGET VALUE  =======  "  + budget[3]);
    		if(budget[3].equalsIgnoreCase("N")){
    			value[1]=createGroup(integrifyId,Description,RegistrationNo,VendorAC,Datepurchased,
    					AssetMake,AssetModel,AssetSerialNo,AssetEngineNo,SupplierName,AssetUser,AssetMaintenance,
    					CostPrice,VatableCost,AuthorizedBy,WhTax,whtaxamount,PostingDate,EffectiveDate,PurchaseReason,SubjectTOVat,AssetStatus,
    					State,Driver,UserID,branchCode,sectionCode,deptCode,categoryCode,barCode,sbuCode,lpo,invoiceNo,
    					supervisor,posted,assetId,assetCode,assettype,branch_id,dept_id,section_id,category_id,vatamount,residualvalue,whtaxvalue,noofitems,
    					location,memovalue,memo,spare1,spare2,multiple);
    			//value[0]= DONE;
    			result = DONE;
    		}
    		else if(budget[3].equalsIgnoreCase("Y")){
    		if (!Q.equalsIgnoreCase("NQ")) {
    			if (budget[3].equalsIgnoreCase("Y")
    					&& budget[4].equalsIgnoreCase("N")) {
    				if (chkBudgetAllocation(Q, bugdetvalues, false,CostPrice)) {
    					updateBudget(Q, budget,categoryCode,branchCode,CostPrice);
    					value[1]=createGroup(integrifyId,Description,RegistrationNo,VendorAC,Datepurchased,
    							AssetMake,AssetModel,AssetSerialNo,AssetEngineNo,SupplierName,AssetUser,AssetMaintenance,
    							CostPrice,VatableCost,AuthorizedBy,WhTax,whtaxamount,PostingDate,EffectiveDate,PurchaseReason,SubjectTOVat,AssetStatus,
    							State,Driver,UserID,branchCode,sectionCode,deptCode,categoryCode,barCode,sbuCode,lpo,invoiceNo,
    							supervisor,posted,assetId,assetCode,assettype,branch_id,dept_id,section_id,category_id,vatamount,residualvalue,whtaxvalue,noofitems,
    							location,memovalue,memo,spare1,spare2,multiple);
    					//value[0]= DONE;
    					result = DONE;
    				} else {
    					//value[0]=  BUDGETENFORCED;
    					result=  BUDGETENFORCED;
    				}

    			} else if (budget[3].equalsIgnoreCase("Y")
    					&& budget[4].equalsIgnoreCase("Y")) {
    				if (chkBudgetAllocation(Q, bugdetvalues, true,CostPrice)) {
    					updateBudget(Q, budget,categoryCode,branchCode,CostPrice);
    					value[1]=createGroup(integrifyId,Description,RegistrationNo,VendorAC,Datepurchased,
    							AssetMake,AssetModel,AssetSerialNo,AssetEngineNo,SupplierName,AssetUser,AssetMaintenance,
    							CostPrice,VatableCost,AuthorizedBy,WhTax,whtaxamount,PostingDate,EffectiveDate,PurchaseReason,SubjectTOVat,AssetStatus,
    							State,Driver,UserID,branchCode,sectionCode,deptCode,categoryCode,barCode,sbuCode,lpo,invoiceNo,
    							supervisor,posted,assetId,assetCode,assettype,branch_id,dept_id,section_id,category_id,vatamount,residualvalue,whtaxvalue,noofitems,
    							location,memovalue,memo, spare1,spare2,multiple);
    					//value[0]=  DONE;
    					result = DONE;
    				} else {
    					//value[0]=  BUDGETENFORCEDCF;
    					result=  BUDGETENFORCEDCF;
    				}

    			} else {
    				value[1]=createGroup(integrifyId,Description,RegistrationNo,VendorAC,Datepurchased,
    						AssetMake,AssetModel,AssetSerialNo,AssetEngineNo,SupplierName,AssetUser,AssetMaintenance,
    						CostPrice,VatableCost,AuthorizedBy,WhTax,whtaxamount,PostingDate,EffectiveDate,PurchaseReason,SubjectTOVat,AssetStatus,
    						State,Driver,UserID,branchCode,sectionCode,deptCode,categoryCode,barCode,sbuCode,lpo,invoiceNo,
    						supervisor,posted,assetId,assetCode,assettype,branch_id,dept_id,section_id,category_id,vatamount,residualvalue,whtaxvalue,noofitems,
    						location,memovalue,memo,spare1,spare2,multiple);
    				//value[0]= DONE;
    				result = DONE;
    			}
    		} else {
    			//value[1]=createGroup();
    			//value[0]= ASSETPURCHASEBD;
    			result= ASSETPURCHASEBD;
    		}
    	}
    	else{}
    		return result;

    	}
        public void updateVatableCostBalance(Double vat_cost_balance ,Double vat_cost,String groupId,String Count)
        {
  //      	System.out.println("vat_cost_balance: "+vat_cost_balance+" vat_cost:  "+vat_cost+"  groupId:  "+groupId+"  Count:  "+Count);
        	double vat_cost_difference = (vat_cost_balance - vat_cost);
           	String updateQry = "update am_group_asset_main set Vatable_Cost_Bal = ? ,pend_GrpAssets=?  where group_id = ? ";
        	Connection con = null;
            PreparedStatement ps = null;
            try
    	    {
            	con = getConnection();
                ps = con.prepareStatement(updateQry);
                ps.setDouble(1, vat_cost_difference);
                ps.setInt(2, Integer.parseInt(Count));
                ps.setLong(3, Long.parseLong(groupId));
                ps.executeUpdate();
    	    }
            catch(Exception ex) 
            {
                System.out.println("WARN: Error updateVatableCostBalance ->" + ex);
            } finally {
                closeConnection(con, ps);
            }
        }
        public boolean save(String integrifyId,String Description,String RegistrationNo,String VendorAC,String Datepurchased,
        		String AssetMake,String AssetModel,String AssetSerialNo,String AssetEngineNo,String SupplierName,String AssetUser,
        		String AssetMaintenance,double CostPrice,double VatableCost,String AuthorizedBy,String WhTax,double whtaxamount,String PostingDate,String EffectiveDate,
        		String PurchaseReason,String SubjectTOVat,String AssetStatus,String State,String Driver,String UserID,String branchCode,
        		String sectionCode,String deptCode,String categoryCode,String barCode,String sbuCode,String lpo,String invoiceNo,String supervisor,
        		String posted,String assetId,String assetCode,String assettype,String branch_id,String dept_id,String section_id,
        		String category_id,double vatamount,String residualvalue,int whtaxvalue,String groupid,int recno,String systemIp,String spare_1,String spare_2) throws Exception, Throwable
        {
        	//System.out.println("save spare_1: "+spare_1+" save  spare_2: "+spare_2);
				String location = "";
				//String vat_amount = "0.0";
				//String vatable_cost = "0.0";
				//String wh_tax_amount = "0";
				String province = "0";
				String noOfMonths = "0";
				String residual_value = "0";
				String warrantyStartDate = null;   
				String expiryDate = null;
				String memo = "N";
				String memoValue = "0";			
				String amountPTD = "0.0";
				String require_depreciation = "Y";
			    String require_redistribution = "N";
			    String who_to_remind = "";
			    String email_1 = "";
			    String who_to_remind_2 = "";
			    String email2 = "";
			    String raise_entry = "N";    
			 //   String spare_1 = "";
			   // String status = "";   
			    String user_id = "";
			    String multiple = "N";
			//    String spare_2 = "";
			    String partPAY = "N";
			    String fullyPAID = "Y";
			    String deferPay = "N";
			    int selectTax = 0;
			    String MacAddress = "";
			    String SystemIp = "";
			    String section = "";  
			    String strnewDateMonth = "";
			    String createQuery = "";
				int purchase_start_year = Integer.parseInt(Datepurchased.substring(0,4));
				//System.out.println("purchase start year: "+purchase_start_year+"  Date Purchased:  "+Datepurchased);
				int purchase_start_month = Integer.parseInt(Datepurchased.substring(5,7));
				//System.out.println("purchase start month: "+purchase_start_month+"  Date Purchased:  "+Datepurchased);
				int purchase_start_day = Integer.parseInt(Datepurchased.substring(8,10));
				//System.out.println("purchase start Day: "+purchase_start_day+"  Date Purchased:  "+Datepurchased);
					
				int newDateYear= purchase_start_year;
				//System.out.println("purchase start month: "+purchase_start_month+" Sum: "+purchase_start_month + 1);
				int newDateMonth = purchase_start_month + 1;
//				System.out.println("New month: "+newDateMonth);
				String newDateDay ="01";
				if(newDateMonth == 10){strnewDateMonth = "10";}
				if(newDateMonth == 11){strnewDateMonth = "11";}
				if(newDateMonth == 12){strnewDateMonth = "12";}
			  if (newDateMonth > 12)
			   { 
			     newDateMonth = 01;
			     strnewDateMonth = "01";
			     newDateYear = newDateYear + 1;
			   }
				
				if (newDateMonth < 10)
			   { 
					int lengthfld = String.valueOf(newDateMonth).length();
					if(newDateMonth == 7){strnewDateMonth = "0"+"7";}
					if(newDateMonth == 1){strnewDateMonth = "0"+"1";}
					if(newDateMonth == 2){strnewDateMonth = "0"+"2";}
					if(newDateMonth == 3){strnewDateMonth = "0"+"3";}
					if(newDateMonth == 4){strnewDateMonth = "0"+"4";}
					if(newDateMonth == 5){strnewDateMonth = "0"+"5";}
					if(newDateMonth == 6){strnewDateMonth = "0"+"6";}
					if(newDateMonth == 8){strnewDateMonth = "0"+"8";}
					if(newDateMonth == 9){strnewDateMonth = "0"+"9";}
			   }
				String depreciation_start_date = newDateDay+'-'+strnewDateMonth+'-'+newDateYear;
			    String rate = getDepreciationRate(categoryCode);

			      String vals = rate+","+depreciation_start_date;
//			      System.out.println("Values for Calculating Depreciation Date: "+vals);
			      String depreciation_end_date = getDepEndDate(vals);
			    
            String asset_id_new = getIdentity(branch_id,
            		dept_id, section_id, category_id);
                    int asset_Code = Integer.parseInt(new ApplicationHelper().getGeneratedId("AM_ASSET"));
                    assetCode = String.valueOf(asset_Code);
 //           System.out.println("New Asset_ID ::::::::: " + asset_id_new+" asset_Code: "+asset_Code);
            Connection con = null;
            PreparedStatement ps = null;
            boolean done = true;
           
            if (AssetMake == null || AssetMake.equals("")) {
            	AssetMake = "0";
            }
            if (AssetMaintenance == null || AssetMaintenance.equals("")) {
            	AssetMaintenance = "0";
            }
            if (SupplierName == null || SupplierName.equals("")) {
            	SupplierName = "0";
            }
            if (AssetUser == null || AssetUser.equals("")) {
            	AssetUser = "";
            }
            if (location == null || location.equals("")) {
                location = "0";
            }
            if (Driver == null || Driver.equals("")) {
            	Driver = "0";
            }
            if (State == null || State.equals("")) {
            	State = "0";
            }
            if (dept_id == null || dept_id.equals("")) {
            	dept_id = "0";
            }
 /*           if (vat_amount == null || vat_amount.equals("")) {
                vat_amount = "0.0";
            }
            if (vatable_cost == null || vatable_cost.equals("")) {
                vatable_cost = "0.0";
            }
            if (wh_tax_amount == null || wh_tax_amount.equals("")) {
                wh_tax_amount = "0";
            }*/
            if (branch_id == null || branch_id.equals("")) {
                branch_id = "0";
            }
     /*       if (province == null || province.equals("")) {
                province = "0";
            }*/
            if (category_id == null || category_id.equals("")) {
                category_id = "0";
            }
            
            if (residualvalue == null || residualvalue.equals("")) {
            	residualvalue = "0";
            }
            if (noOfMonths == null || noOfMonths.equals("")) {
                noOfMonths = "0";
            }
            if (warrantyStartDate == null || warrantyStartDate.equals("")) {
                warrantyStartDate = null;
            }
            if (expiryDate == null || expiryDate.equals("")) {
                expiryDate = null;
            }
     //       vat_amount = vat_amount.replaceAll(",", "");
     //       vatable_cost = vatable_cost.replaceAll(",", "");
     //       wh_tax_amount = wh_tax_amount.replaceAll(",", "");
     //       residual_value = residual_value.replaceAll(",", "");

//            String createQuery_old = "INSERT INTO AM_ASSET         " +
//                                 "(" +
//                                 "ASSET_ID, REGISTRATION_NO, BRANCH_ID, DEPT_ID," +
//                                 "SECTION_ID, CATEGORY_ID, [DESCRIPTION], VENDOR_AC," +
//                                 "DATE_PURCHASED, DEP_RATE, ASSET_MAKE, ASSET_MODEL," +
//                                 "ASSET_SERIAL_NO, ASSET_ENGINE_NO, SUPPLIER_NAME," +
    //
//                                 "ASSET_USER, ASSET_MAINTENANCE, ACCUM_DEP, MONTHLY_DEP," +
//                                 "COST_PRICE, NBV, DEP_END_DATE, RESIDUAL_VALUE," +
//                                 "AUTHORIZED_BY, POSTING_DATE, EFFECTIVE_DATE, PURCHASE_REASON," +
//                                 "USEFUL_LIFE, TOTAL_LIFE, LOCATION, REMAINING_LIFE," +
    //
//                                 "VATABLE_COST,VAT, WH_TAX, WH_TAX_AMOUNT, REQ_DEPRECIATION," +
//                                 "REQ_REDISTRIBUTION, SUBJECT_TO_VAT, WHO_TO_REM, EMAIL1," +
//                                 "WHO_TO_REM_2, EMAIL2, RAISE_ENTRY, DEP_YTD, [SECTION]," +
//                                 "STATE, DRIVER, SPARE_1, SPARE_2, ASSET_STATUS, [USER_ID]," +
//                                 "MULTIPLE,PROVINCE, WAR_START_DATE, WAR_MONTH," +
//                                 "BRANCH_CODE,CATEGORY_CODE " +
//                                 "WAR_EXPIRY_DATE, ) " +
    //
//                                 "VALUES" +
//                                 "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
//                                 "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            if(assettype.equalsIgnoreCase("C")){            
            createQuery = "INSERT INTO AM_ASSET         " +
            "(" +
            "ASSET_ID, REGISTRATION_NO, BRANCH_ID, DEPT_ID," +
            "SECTION_ID, CATEGORY_ID, [DESCRIPTION], VENDOR_AC," +
            "DATE_PURCHASED, DEP_RATE, ASSET_MAKE, ASSET_MODEL," +
            "ASSET_SERIAL_NO, ASSET_ENGINE_NO, SUPPLIER_NAME," +

            "ASSET_USER, ASSET_MAINTENANCE, ACCUM_DEP, MONTHLY_DEP," +
            "COST_PRICE, NBV, DEP_END_DATE, RESIDUAL_VALUE," +
            "AUTHORIZED_BY, POSTING_DATE, EFFECTIVE_DATE, PURCHASE_REASON," +
            "USEFUL_LIFE, TOTAL_LIFE, LOCATION, REMAINING_LIFE," +

            "VATABLE_COST,VAT, WH_TAX, WH_TAX_AMOUNT, REQ_DEPRECIATION," +
            "REQ_REDISTRIBUTION, SUBJECT_TO_VAT, WHO_TO_REM, EMAIL1," +
            "WHO_TO_REM_2, EMAIL2, RAISE_ENTRY, DEP_YTD, [SECTION]," +
            "STATE, DRIVER, SPARE_1, SPARE_2, ASSET_STATUS, [USER_ID]," +
            "MULTIPLE,PROVINCE, WAR_START_DATE, WAR_MONTH, " +
            "WAR_EXPIRY_DATE,LPO,BAR_CODE ,BRANCH_CODE,CATEGORY_CODE ," +
            "GROUP_ID,PART_PAY,FULLY_PAID,DEFER_PAY,SBU_CODE,SECTION_CODE,DEPT_CODE,system_ip,asset_code,memo,memoValue,INTEGRIFY,SUPERVISOR) " +

            "VALUES" +
            "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
            "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        }
            if(assettype.equalsIgnoreCase("U")){            
            createQuery = "INSERT INTO AM_ASSET_UNCAPITALIZED         " +
            "(" +
            "ASSET_ID, REGISTRATION_NO, BRANCH_ID, DEPT_ID," +
            "SECTION_ID, CATEGORY_ID, [DESCRIPTION], VENDOR_AC," +
            "DATE_PURCHASED, DEP_RATE, ASSET_MAKE, ASSET_MODEL," +
            "ASSET_SERIAL_NO, ASSET_ENGINE_NO, SUPPLIER_NAME," +

            "ASSET_USER, ASSET_MAINTENANCE, ACCUM_DEP, MONTHLY_DEP," +
            "COST_PRICE, NBV, DEP_END_DATE, RESIDUAL_VALUE," +
            "AUTHORIZED_BY, POSTING_DATE, EFFECTIVE_DATE, PURCHASE_REASON," +
            "USEFUL_LIFE, TOTAL_LIFE, LOCATION, REMAINING_LIFE," +

            "VATABLE_COST,VAT, WH_TAX, WH_TAX_AMOUNT, REQ_DEPRECIATION," +
            "REQ_REDISTRIBUTION, SUBJECT_TO_VAT, WHO_TO_REM, EMAIL1," +
            "WHO_TO_REM_2, EMAIL2, RAISE_ENTRY, DEP_YTD, [SECTION]," +
            "STATE, DRIVER, SPARE_1, SPARE_2, ASSET_STATUS, [USER_ID]," +
            "MULTIPLE,PROVINCE, WAR_START_DATE, WAR_MONTH, " +
            "WAR_EXPIRY_DATE,LPO,BAR_CODE ,BRANCH_CODE,CATEGORY_CODE ," +
            "GROUP_ID,PART_PAY,FULLY_PAID,DEFER_PAY,SBU_CODE,SECTION_CODE,DEPT_CODE,system_ip,asset_code,memo,memoValue,INTEGRIFY,SUPERVISOR) " +

            "VALUES" +
            "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
            "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            
        }
            
            String create_Archive_Query = "INSERT INTO AM_ASSET_ARCHIVE         " +
            "(" +
            "ASSET_ID, REGISTRATION_NO, BRANCH_ID, DEPT_ID," +
            "SECTION_ID, CATEGORY_ID, [DESCRIPTION], VENDOR_AC," +
            "DATE_PURCHASED, DEP_RATE, ASSET_MAKE, ASSET_MODEL," +
            "ASSET_SERIAL_NO, ASSET_ENGINE_NO, SUPPLIER_NAME," +

            "ASSET_USER, ASSET_MAINTENANCE, ACCUM_DEP, MONTHLY_DEP," +
            "COST_PRICE, NBV, DEP_END_DATE, RESIDUAL_VALUE," +
            "AUTHORIZED_BY, POSTING_DATE, EFFECTIVE_DATE, PURCHASE_REASON," +
            "USEFUL_LIFE, TOTAL_LIFE, LOCATION, REMAINING_LIFE," +

            "VATABLE_COST,VAT, WH_TAX, WH_TAX_AMOUNT, REQ_DEPRECIATION," +
            "REQ_REDISTRIBUTION, SUBJECT_TO_VAT, WHO_TO_REM, EMAIL1," +
            "WHO_TO_REM_2, EMAIL2, RAISE_ENTRY, DEP_YTD, [SECTION]," +
            "STATE, DRIVER, SPARE_1, SPARE_2, ASSET_STATUS, [USER_ID]," +
            "MULTIPLE,PROVINCE, WAR_START_DATE, WAR_MONTH, " +
            "WAR_EXPIRY_DATE,LPO,BAR_CODE ,BRANCH_CODE,CATEGORY_CODE ," +
            "GROUP_ID,PART_PAY,FULLY_PAID,DEFER_PAY,SBU_CODE,SECTION_CODE,DEPT_CODE,system_ip,asset_code ) " +

            "VALUES" +
            "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
            "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            /*
             *First Create Asset Records
             * and then determine if it
             * should be made available for fleet.
             */
               try 
               {
                //asset_costPrice = Double.parseDouble(vat_amount) + Double.parseDouble(vatable_cost);
 //           	System.out.println("Group Creation -1 ");
                con = getConnection();
                ps = con.prepareStatement(createQuery);
                ps.setString(1, asset_id_new);
                ps.setString(2, RegistrationNo);
 //               System.out.println("Group Creation branch_id: "+branch_id);
                ps.setInt(3, Integer.parseInt(branch_id));
 //               System.out.println("Group Creation dept_id: "+dept_id);
                ps.setInt(4, Integer.parseInt(dept_id));
 //               System.out.println("Group Creation section_id: "+section_id);
                ps.setInt(5, Integer.parseInt(section_id));
 //               System.out.println("Group Creation category_id: "+category_id);
                ps.setInt(6, Integer.parseInt(category_id));
                ps.setString(7, Description);
                ps.setString(8, VendorAC);
//                System.out.println("Group Creation -2 ");
                ps.setString(9, Datepurchased);
                ps.setString(10, rate);
                ps.setString(11, AssetMake);
                ps.setString(12, AssetModel);
                ps.setString(13, AssetSerialNo);
                ps.setString(14, AssetEngineNo);
//               System.out.println("Group Creation -3 ");
                ps.setInt(15, Integer.parseInt(SupplierName));
                ps.setString(16, AssetUser);
//                System.out.println("Group Creation0 ");
                ps.setInt(17, Integer.parseInt(AssetMaintenance));
                ps.setInt(18, 0);
                ps.setInt(19, 0);
                ps.setDouble(20, CostPrice);
                ps.setDouble(21, CostPrice);
//                System.out.println("Group Creation1 ");
                ps.setDate(22, dateConvert(depreciation_end_date));
                ps.setDouble(23, Double.parseDouble(residualvalue));
                ps.setString(24, AuthorizedBy);
                ps.setDate(25, dateConvert(new java.util.Date()));
                ps.setDate(26, dateConvert(depreciation_start_date));
                ps.setString(27, PurchaseReason);
                ps.setString(28, "0");          
 //               System.out.println("Group Creation2 ");
                ps.setString(29, computeTotalLife(getDepreciationRate(category_id)));
                ps.setInt(30, Integer.parseInt(location));
                ps.setString(31, computeTotalLife(getDepreciationRate(category_id)));
 //               System.out.println("Group Creation3 ");
                ps.setDouble(32, VatableCost);
                ps.setDouble(33, vatamount);
                ps.setString(34, WhTax);
                ps.setDouble(35, whtaxamount);
                ps.setString(36, require_depreciation);
                ps.setString(37, require_redistribution);
                ps.setString(38, SubjectTOVat);
                ps.setString(39, who_to_remind);
                ps.setString(40, email_1);
                ps.setString(41, who_to_remind_2);
                ps.setString(42, email2);
                ps.setString(43, "N");
                ps.setString(44, "0");
                ps.setString(45, section);
 //               System.out.println("Group Creation3 1 ");
                ps.setInt(46, Integer.parseInt(State));
                ps.setInt(47, Integer.parseInt(Driver));
                ps.setString(48, spare_1);
                ps.setString(49, spare_2);
                ps.setString(50, AssetStatus);
                ps.setString(51, user_id);
                ps.setString(52, multiple);
                ps.setString(53, province);
 //               System.out.println("Group Creation3 2 ");
                ps.setDate(54, dateConvert(warrantyStartDate));
//                System.out.println("Group Creation3 3 ");
                ps.setInt(55, Integer.parseInt(noOfMonths));
//                System.out.println("Group Creation3 4 ");
                ps.setDate(56, dateConvert(expiryDate));
//                System.out.println("Group Creation3 5 ");
                ps.setString(57,lpo);
                ps.setString(58,barCode);
                ps.setString(59,branchCode);
                ps.setString(60,categoryCode);
                ps.setString(61,groupid);
                /*ps.setString(62, partPAY);
                ps.setString(63, fullyPAID);
                ps.setString(64, deferPay);*/
                ps.setString(62, "N");
                ps.setString(63, "Y");
                ps.setString(64, "N");
                ps.setString(65,sbuCode);
                ps.setString(66,sectionCode);
                ps.setString(67,deptCode);
                ps.setString(68,systemIp);
                ps.setInt(69, asset_Code);
               // System.out.println("Group Creation4 "+assetCode);
                ps.setString(70,memo);
                ps.setString(71, memoValue);
                ps.setString(72, integrifyId);
                ps.setString(73, supervisor);
                //not right
                  
                boolean result = ps.execute();

                con = getConnection();
                ps = con.prepareStatement(create_Archive_Query);
                ps.setString(1, asset_id_new);
                ps.setString(2, RegistrationNo);
                ps.setInt(3, Integer.parseInt(branch_id));
                ps.setInt(4, Integer.parseInt(dept_id));
                ps.setInt(5, Integer.parseInt(section_id));
                ps.setInt(6, Integer.parseInt(category_id));
                ps.setString(7, Description);
                ps.setString(8, VendorAC);
                ps.setDate(9, dateConvert(Datepurchased));
                ps.setString(10, rate);
                ps.setString(11, AssetMake);
                ps.setString(12, AssetModel);
                ps.setString(13, AssetSerialNo);
                ps.setString(14, AssetEngineNo);
                ps.setInt(15, Integer.parseInt(SupplierName));
                ps.setString(16, AssetUser);
                ps.setInt(17, Integer.parseInt(AssetMaintenance));
                ps.setInt(18, 0);
                ps.setInt(19, 0);
                ps.setDouble(20, CostPrice);
                ps.setDouble(21, CostPrice);
                ps.setDate(22, dateConvert(depreciation_end_date));
                ps.setDouble(23, Double.parseDouble(residual_value));
                ps.setString(24, AuthorizedBy);
                ps.setDate(25, dateConvert(new java.util.Date()));
                ps.setDate(26, dateConvert(depreciation_start_date));
                ps.setString(27, PurchaseReason);
                ps.setString(28, "0");
                ps.setString(29, computeTotalLife(getDepreciationRate(category_id)));
                ps.setInt(30, Integer.parseInt(location));
                ps.setString(31, computeTotalLife(getDepreciationRate(category_id)));
                ps.setDouble(32, VatableCost);
                ps.setDouble(33, vatamount);
              //  System.out.println("Group Creation5 "+vatamount);
                ps.setString(34, WhTax);
                ps.setDouble(35, whtaxamount);
                ps.setString(36, require_depreciation);
                ps.setString(37, require_redistribution);
                ps.setString(38, SubjectTOVat);
                ps.setString(39, who_to_remind);
                ps.setString(40, email_1);
                ps.setString(41, who_to_remind_2);
                ps.setString(42, email2);
                ps.setString(43, "N");
                ps.setString(44, "0");
                ps.setString(45, section);
                ps.setInt(46, Integer.parseInt(State));
                ps.setInt(47, Integer.parseInt(Driver));
                ps.setString(48, spare_1);
                ps.setString(49, spare_2);
                ps.setString(50, AssetStatus);
                ps.setString(51, user_id);
                ps.setString(52, multiple);
                ps.setString(53, province);
                ps.setDate(54, dateConvert(warrantyStartDate));
                ps.setInt(55, Integer.parseInt(noOfMonths));
                ps.setDate(56, dateConvert(expiryDate));
                ps.setString(57,lpo);
                ps.setString(58,barCode);
                ps.setString(59,branchCode);
                ps.setString(60,categoryCode);
                ps.setString(61,groupid);
                /*ps.setString(62, partPAY);
                ps.setString(63, fullyPAID);
                ps.setString(64, deferPay);*/
                ps.setString(62, "N");
                ps.setString(63, "Y");
                ps.setString(64, "N");
                ps.setString(65,sbuCode);
                ps.setString(66,sectionCode);
                ps.setString(67,deptCode);
                ps.setString(68,systemIp);
                ps.setInt(69, asset_Code);
             //   System.out.println("=====================================================");
               // System.out.println("Result Of Insertion into Asset Table From group Asset : " + result);
               // System.out.println("=====================================================");
                 result = ps.execute();

                 
               insGrpToAm_Invoice_No(asset_id_new,lpo,invoiceNo,"Asset Creation",groupid);
               
         //       FleetHistoryManager fm = new FleetHistoryManager();
     /*           if (fm.isRequiredForFleet(asset_id))
                {
                	System.out.println(">>>>>>>>>>>>>>>>>> Asset is Required For Fleet <<<<<<<<<<<<<<<<<<<<<<<<<<<");
                   fm.copyAssetDataToFleet(asset_id);
                }*/
               // setAssetId(asset_id);
                          
                String page1 = "ASSET GROUP CREATION RAISE ENTRY";
                String flag= "";
          	  	String partPay="";
          	  String url = "";
          	  	String qryBranch =" SELECT branch_name from am_ad_branch where branch_id='"+branch_id+"'";
          	   	//String Branch = approvalRec.getCodeName(qryBranch);
          	   //	String subjectT= adGroup.subjectToVat(group_id);
          	   //	String whT= adGroup.whTax(group_id);
          	   	String Name =findObject(" SELECT full_name from am_gb_user where user_id='"+supervisor+"'");
    		//	System.out.println("Approved by Name in Save: "+Name+"  User Id: "+Integer.parseInt(supervisor));
          	   //	String IdInt =findObject(" SELECT User_id from am_gb_user where user_name='"+UserID+"'");
          	  if(assettype.equalsIgnoreCase("C")){              	   	
          	   	url = "DocumentHelp.jsp?np=groupAssetPosting&amp;id=" + groupid + "&pageDirect=Y";
               }
          	  if(assettype.equalsIgnoreCase("U")){              	   	
            	   	url = "DocumentHelp.jsp?np=groupAssetPostingBranch&amp;id=" + groupid + "&pageDirect=Y";
                 }          	  
          	   	boolean approval_level_val =checkApprovalStatus("27");
 //         	  System.out.println(">>>>>>>>>>>> groupid Into RaiseEntry <<<<<<<<<< "+groupid+"  asset_Code: "+asset_Code+"  assetCode: "+assetCode);
          	  	boolean status = updateCreatedAssetStatus(integrifyId,Description,RegistrationNo,VendorAC,Datepurchased,
          	  		AssetMake,AssetModel,AssetSerialNo,AssetEngineNo,SupplierName,AssetUser,
          	  	AssetMaintenance,CostPrice,VatableCost,AuthorizedBy,WhTax,whtaxamount,PostingDate,EffectiveDate,
          	  	PurchaseReason,SubjectTOVat,AssetStatus,State,Driver,UserID,branchCode,
          	  	sectionCode,deptCode,categoryCode,barCode,sbuCode,lpo,invoiceNo,supervisor,
          	  	posted,assetId,asset_Code,assettype,branch_id,dept_id,section_id,
          	  	category_id,vatamount,residualvalue,whtaxvalue,groupid,systemIp,
          	  	depreciation_end_date,depreciation_start_date,require_depreciation,require_redistribution,
          	  	who_to_remind,email_1,who_to_remind_2,email2,spare_1,spare_2,
          	  	partPAY,fullyPAID,deferPay,warrantyStartDate,expiryDate, noOfMonths,recno,asset_id_new);
          	  	
          	  	if ((!approval_level_val)&&(status))
    	      	  	{
          	  			//System.out.println(">>>>>>>>>>>> Inserting Into RaiseEntry <<<<<<<<<<");
          	  			String [] approvalResult=setApprovalDataGroup(Long.parseLong(groupid));
          	  			approvalResult[10]="A";
          	  			String trans_id = setGroupPendingTrans(approvalResult,"27",asset_Code);

                                    setPendingTransArchive(approvalResult,"27",Integer.parseInt(trans_id),asset_Code);

                                    String assetRaiseEntry =findObject(" SELECT raise_entry from am_gb_company ");
          	  			if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("Y")){
                                    insertApprovalx2
    	      	  		(groupid, approvalResult[5], page1, flag, partPay,Name, branchCode, SubjectTOVat,
    	      	  			WhTax, url,Integer.parseInt(trans_id),Integer.parseInt(assetCode));
                                    }
          	  		/*	String qry = upd_am_asset_status_RaiseEntry + groupid;
          	  			String qry2 = upd_am_grp_asset_RaiseEntry + groupid;
          	  			String qry3 = upd_am_grp_asset_main_RaiseEntry +  groupid;
                                    String qry4 = upd_am_asset_status_RaiseEntry_Archive + groupid;
          	  			String qry5 = upd_am_grp_asset_RaiseEntry_Archive + groupid;
          	  			String qry6 = upd_am_grp_asset_main_RaiseEntry_Archive +  groupid;*/
    	      	  		/*System.out.println("qry : " + qry);
    	  	  			System.out.println("qry2 : " + qry2 );
    	  	  			System.out.println("qry3 : " + qry3);*/
    	      	  	/*	updateStatusUtil(qry);
    	      	  		updateStatusUtil(qry2);
    	      	  		updateStatusUtil(qry3);
                                    updateStatusUtil(qry4);
    	      	  		updateStatusUtil(qry5);
    	      	  		updateStatusUtil(qry6);
    	      	  		*/
    	      	  	}
 //         	  System.out.println("====== Inserting into status ======"+status+" approval_level_val: "+approval_level_val);
    	      	if((status)&&(approval_level_val))
    		      	{
 //   		      	 System.out.println("====== Inserting into Approval ======"+groupid);
    		      	 changeGroupAssetStatus(groupid,"PENDING",assettype);
    		      	 String trans_id = setGroupPendingTrans(setApprovalDataGroup(Long.parseLong(groupid)),"27",Integer.parseInt(assetCode));
                             setPendingTransArchive(setApprovalDataGroup(Long.parseLong(groupid)),"27",Integer.parseInt(trans_id),Integer.parseInt(assetCode));
    		      	 //write a method to change status to pending
    		      	}
    	      	else{
    	//      		System.out.println("====== Inserting into Approval in else ======"+groupid);
   		      	 changeGroupAssetStatus(groupid,"ACTIVE",assettype);
   	//	      System.out.println("====== Inserting into Approval in else ======"+groupid);
		      //	 String trans_id = setGroupPendingTrans(setApprovalDataGroup(Long.parseLong(groupid)),"27",Integer.parseInt(assetCode));
             //    setPendingTransArchive(setApprovalDataGroup(Long.parseLong(groupid)),"27",Integer.parseInt(trans_id),Integer.parseInt(assetCode));    	      		
    	      	}
            } 
            catch (Exception ex)
            {
                done = false;
                System.out.println("WARN:Error creating asset in save->" + ex);
            }
            finally 
            {
                closeConnection(con, ps);
            }

            return done;

        }
        public void insGrpToAm_Invoice_No(String assetID,String lpo,String invoiceNo,String TransType,String grpID)
        {

            Connection Con2 = null;
            PreparedStatement Stat = null;
            ResultSet result = null;
            String found = null;

            String query="Insert into Am_Invoice_no (asset_id,lpo,invoice_no,trans_type,group_id) values (?,?,?,?,?)";

                 try
                 {                	 
                Con2 = getConnection();
                Stat = Con2.prepareStatement(query);
                Stat.setString(1, assetID);
                Stat.setString(2, lpo);
                Stat.setString(3, invoiceNo);
                Stat.setString(4, TransType);
                Stat.setString(5, grpID);
                Stat.execute();

            } catch (Exception ee2) {
                System.out.println("WARN:ERROR insGrpToAm_Invoice_No  --> " + ee2);
                ee2.printStackTrace();
            } finally {
                closeConnection(Con2, Stat, result);
            }

            }
        private boolean checkApprovalStatus(String code)
        {
    		// TODO Auto-generated method stub
    		boolean status = false;
    		String approval_status_qry = "select level from approval_level_setup where code ='"+code+"'";
    		Connection con = null;
    	    PreparedStatement ps = null;
    	    ResultSet rs = null;
    	    int level = 0;
    	    try
    	    {
    	    	con = getConnection();
    	    	ps = con.prepareStatement(approval_status_qry);
    	    	rs = ps.executeQuery();
    	    	if(rs.next())
    	    	{
    	    		level= rs.getInt(1);
    	    	}
    	    	if (level > 0)
    	    	{
    	    		status = true;
    	    	}
    	    }
    	    catch(Exception ex) 
    	    {
    	        System.out.println("WARN: Error checkApprovalStatus ->" + ex);
    	    }
    	    finally 
    	    {
    	        closeConnection(con, ps);
    	    }   
    	    return status;
    	}
    	public boolean updateCreatedAssetStatus(String integrifyId,String Description,String RegistrationNo,String VendorAC,String Datepurchased,
        		String AssetMake,String AssetModel,String AssetSerialNo,String AssetEngineNo,String SupplierName,String AssetUser,
        		String AssetMaintenance,double CostPrice,double VatableCost,String AuthorizedBy,String WhTax,double whtaxamount,String PostingDate,String EffectiveDate,
        		String PurchaseReason,String SubjectTOVat,String AssetStatus,String State,String Driver,String UserID,String branchCode,
        		String sectionCode,String deptCode,String categoryCode,String barCode,String sbuCode,String lpo,String invoiceNo,String supervisor,
        		String posted,String assetId,int assetCode,String assettype,String branch_id,String dept_id,String section_id,
        		String category_id,double vatamount,String residualvalue,int whtaxvalue,String groupid,String systemIp,
        		String depreciation_end_date,String depreciation_start_date,String require_depreciation,String require_redistribution,
        		String who_to_remind,String email_1,String who_to_remind_2,String email2,String spare_1,String spare_2,
        		String partPAY,String fullyPAID,String deferPay,String warrantyStartDate,String expiryDate, String noOfMonths,int recno,String new_asset_id)
        {
    		String location = "";
    		String province = "";
    		// TODO Auto-generated method stub
            if (AssetMake == null || AssetMake.equals("")) {
            	AssetMake = "0";
            }
            if (AssetMaintenance == null || AssetMaintenance.equals("")) {
            	AssetMaintenance = "0";
            }
            if (SupplierName == null || SupplierName.equals("")) {
            	SupplierName = "0";
            }
            if (AssetUser == null || AssetUser.equals("")) {
            	AssetUser = "";
            }
            if (location == null || location.equals("")) {
                location = "0";
            }
            if (province == null || province.equals("")) {
            	province = "0";
            }            
            if (Driver == null || Driver.equals("")) {
            	Driver = "0";
            }
            if (State == null || State.equals("")) {
            	State = "0";
            }
            if (dept_id == null || dept_id.equals("")) {
            	dept_id = "0";
            }
            if (branch_id == null || branch_id.equals("")) {
                branch_id = "0";
            }
            if (category_id == null || category_id.equals("")) {
                category_id = "0";
            }
            
            if (residualvalue == null || residualvalue.equals("")) {
            	residualvalue = "0";
            }
            if (noOfMonths == null || noOfMonths.equals("")) {
                noOfMonths = "0";
            }
            if (warrantyStartDate == null || warrantyStartDate.equals("")) {
                warrantyStartDate = null;
            }
            if (expiryDate == null || expiryDate.equals("")) {
                expiryDate = null;
            }    		
        	Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            boolean status = false;
            int chk_Process_flag=0;
            String process_flag ="N";
            String update_created_asset_qry = "";
            String chk_process_flag = "";
 //           System.out.println("Matanmi User ID: "+UserID);
          //  String location = "";   
 //           System.out.println("assetId: "+assetId+"  new_asset_id: "+new_asset_id+"  assetCode:  "+assetCode);
         if(assettype.equalsIgnoreCase("C")){             
            update_created_asset_qry ="update am_group_asset set process_flag= ?,asset_id=?,REGISTRATION_NO=?, " + 	  
            "BRANCH_ID=?, DEPT_ID=?,SECTION_ID=?, CATEGORY_ID=?,DESCRIPTION=?, VENDOR_AC=?," +
            "DATE_PURCHASED=?, DEP_RATE=?, ASSET_MAKE=?, ASSET_MODEL=?,"+ 
            "ASSET_SERIAL_NO=?, ASSET_ENGINE_NO=?, SUPPLIER_NAME=?," +
            "ASSET_USER=?, ASSET_MAINTENANCE=?, ACCUM_DEP=?," +
            "COST_PRICE=?, DEP_END_DATE=?, RESIDUAL_VALUE=?," +
            "AUTHORIZED_BY=?, POSTING_DATE=?, EFFECTIVE_DATE=?, PURCHASE_REASON=?," +
            " LOCATION=?, " +
            "VATABLE_COST=?,VAT=?, WH_TAX=?, WH_TAX_AMOUNT=?, REQ_DEPRECIATION=?," +
            "REQ_REDISTRIBUTION=?, SUBJECT_TO_VAT=?, WHO_TO_REM=?, EMAIL1=?," +
            "WHO_TO_REM_2=?, EMAIL2=?, RAISE_ENTRY=?, DEP_YTD=?, SECTION=?," +
            "STATE=?, DRIVER=?, SPARE_1=?, SPARE_2=?, [USER_ID]=?," +
            "PROVINCE=?, WAR_START_DATE=?, WAR_MONTH=?, " +
            "WAR_EXPIRY_DATE=?,LPO=?,BAR_CODE=? ,BRANCH_CODE=?,CATEGORY_CODE=? ," +
            "GROUP_ID=?,PART_PAY=?,FULLY_PAID=?,DEFER_PAY=?,SBU_CODE=?,[dept_code] = ?,[section_code] = ? "+
            "  where INTEGRIFY = ? AND RECCOUNT =?" ;
        }
         if(assettype.equalsIgnoreCase("U")){             
             update_created_asset_qry ="update AM_GROUP_ASSET_UNCAPITALIZED set process_flag= ?,asset_id=?,REGISTRATION_NO=?, " + 	  
             "BRANCH_ID=?, DEPT_ID=?,SECTION_ID=?, CATEGORY_ID=?,DESCRIPTION=?, VENDOR_AC=?," +
             "DATE_PURCHASED=?, DEP_RATE=?, ASSET_MAKE=?, ASSET_MODEL=?,"+ 
             "ASSET_SERIAL_NO=?, ASSET_ENGINE_NO=?, SUPPLIER_NAME=?," +
             "ASSET_USER=?, ASSET_MAINTENANCE=?, ACCUM_DEP=?," +
             "COST_PRICE=?, DEP_END_DATE=?, RESIDUAL_VALUE=?," +
             "AUTHORIZED_BY=?, POSTING_DATE=?, EFFECTIVE_DATE=?, PURCHASE_REASON=?," +
             " LOCATION=?, " +
             "VATABLE_COST=?,VAT=?, WH_TAX=?, WH_TAX_AMOUNT=?, REQ_DEPRECIATION=?," +
             "REQ_REDISTRIBUTION=?, SUBJECT_TO_VAT=?, WHO_TO_REM=?, EMAIL1=?," +
             "WHO_TO_REM_2=?, EMAIL2=?, RAISE_ENTRY=?, DEP_YTD=?, SECTION=?," +
             "STATE=?, DRIVER=?, SPARE_1=?, SPARE_2=?, [USER_ID]=?," +
             "PROVINCE=?, WAR_START_DATE=?, WAR_MONTH=?, " +
             "WAR_EXPIRY_DATE=?,LPO=?,BAR_CODE=? ,BRANCH_CODE=?,CATEGORY_CODE=? ," +
             "GROUP_ID=?,PART_PAY=?,FULLY_PAID=?,DEFER_PAY=?,SBU_CODE=?,[dept_code] = ?,[section_code] = ? "+
             "  where INTEGRIFY = ? AND RECCOUNT =?" ;
         }         
            String update_created_asset_ARCHIVE_qry ="update am_group_asset_ARCHIVE set process_flag= ?,asset_id=?,REGISTRATION_NO=?, " +
            "BRANCH_ID=?, DEPT_ID=?,SECTION_ID=?, CATEGORY_ID=?,DESCRIPTION=?, VENDOR_AC=?," +
            "DATE_PURCHASED=?, DEP_RATE=?, ASSET_MAKE=?, ASSET_MODEL=?,"+
            "ASSET_SERIAL_NO=?, ASSET_ENGINE_NO=?, SUPPLIER_NAME=?," +
            "ASSET_USER=?, ASSET_MAINTENANCE=?, ACCUM_DEP=?," +
            "COST_PRICE=?, DEP_END_DATE=?, RESIDUAL_VALUE=?," +
            "AUTHORIZED_BY=?, POSTING_DATE=?, EFFECTIVE_DATE=?, PURCHASE_REASON=?," +
            " LOCATION=?, " +
            "VATABLE_COST=?,VAT=?, WH_TAX=?, WH_TAX_AMOUNT=?, REQ_DEPRECIATION=?," +
            "REQ_REDISTRIBUTION=?, SUBJECT_TO_VAT=?, WHO_TO_REM=?, EMAIL1=?," +
            "WHO_TO_REM_2=?, EMAIL2=?, RAISE_ENTRY=?, DEP_YTD=?, SECTION=?," +
            "STATE=?, DRIVER=?, SPARE_1=?, SPARE_2=?, [USER_ID]=?," +
            "PROVINCE=?, WAR_START_DATE=?, WAR_MONTH=?, " +
            "WAR_EXPIRY_DATE=?,LPO=?,BAR_CODE=? ,BRANCH_CODE=?,CATEGORY_CODE=? ," +
            "GROUP_ID=?,PART_PAY=?,FULLY_PAID=?,DEFER_PAY=?,SBU_CODE=?,[dept_code] = ?,[section_code] = ? "+
            "  where INTEGRIFY = ? AND RECCOUNT =?" ;
         if(assettype.equalsIgnoreCase("C")){ 
            chk_process_flag ="select count(*) from am_group_asset WHERE process_flag='" 
            						+ process_flag + "'" +"  and Group_id ='"+groupid+"'";
         }
         if(assettype.equalsIgnoreCase("U")){ 
             chk_process_flag ="select count(*) from AM_GROUP_ASSET_UNCAPITALIZED WHERE process_flag='" 
             						+ process_flag + "'" +"  and Group_id ='"+groupid+"'";
          }         
            String update_created_asset_main_qry = "update am_group_asset_main set process_flag =? "+
           									" where group_id = ?";
            
            String update_created_asset_main_ARCHIVE_qry = "update am_group_asset_main_archive set process_flag =? "+
           									" where group_id = ?";
 //           System.out.println("chk_process_flag: "+chk_process_flag);
 //           System.out.println("update_created_asset_main_qry: "+update_created_asset_main_qry);
 //           System.out.println("update_created_asset_main_ARCHIVE_qry: "+update_created_asset_main_ARCHIVE_qry);
            try
    	    {
            	con = getConnection();
                ps = con.prepareStatement(update_created_asset_qry);
                ps.setString(1, "Y");
                ps.setString(2, new_asset_id);
//                System.out.println("Asset Id Inside update_created_asset_qry: "+new_asset_id);
                ps.setString(3, RegistrationNo);
                ps.setInt(4, Integer.parseInt(branch_id));
                ps.setInt(5, Integer.parseInt(dept_id));
                ps.setInt(6, Integer.parseInt(section_id));
                ps.setInt(7, Integer.parseInt(category_id));
                ps.setString(8, Description);
                ps.setString(9, VendorAC);
                ps.setString(10, Datepurchased);
                ps.setString(11, getDepreciationRate(categoryCode));
                ps.setString(12, AssetMake);
                ps.setString(13, AssetModel);
                ps.setString(14, AssetSerialNo);
                ps.setString(15, AssetEngineNo);
                ps.setInt(16, Integer.parseInt(SupplierName));
                ps.setString(17, AssetUser);
                ps.setInt(18, Integer.parseInt(AssetMaintenance));
                ps.setInt(19, 0);
                ps.setDouble(20, CostPrice);
                ps.setDate(21, dateConvert(depreciation_end_date));
                ps.setDouble(22, Double.parseDouble(residualvalue));
                ps.setString(23, AuthorizedBy);
                ps.setDate(24, dateConvert(new java.util.Date()));
                ps.setDate(25, dateConvert(depreciation_start_date));
                ps.setString(26, PurchaseReason);
                ps.setInt(27, Integer.parseInt(location));
                ps.setDouble(28, VatableCost);
                ps.setDouble(29, vatamount);
                ps.setString(30, WhTax);
                ps.setDouble(31, whtaxamount);
                ps.setString(32, require_depreciation);
                ps.setString(33, require_redistribution);
                ps.setString(34, SubjectTOVat);
                ps.setString(35, who_to_remind);
                ps.setString(36, email_1);
                ps.setString(37, who_to_remind_2);
                ps.setString(38, email2);
                ps.setString(39, "N");
                ps.setString(40, "0");
                ps.setString(41, section);
                ps.setInt(42, Integer.parseInt(State));
                ps.setInt(43, Integer.parseInt(Driver));
                ps.setString(44, spare_1);
                ps.setString(45, spare_2);
               // ps.setString(46, "ACTIVE");
                ps.setInt(46, Integer.parseInt(UserID));                
                ps.setString(47, province);
                ps.setDate(48, dateConvert(warrantyStartDate));
                ps.setInt(49, Integer.parseInt(noOfMonths));
                ps.setDate(50, dateConvert(expiryDate));
                ps.setString(51,lpo);
                ps.setString(52,barCode);
                ps.setString(53,branchCode);
                ps.setString(54,categoryCode);
                ps.setString(55,groupid);
                ps.setString(56, partPAY);
                ps.setString(57, fullyPAID);
                ps.setString(58, deferPay);
                ps.setString(59,sbuCode);
                ps.setString(60,deptCode);
                ps.setString(61,sectionCode);
                ps.setString(62, integrifyId);
                ps.setInt(63, recno);
                int result = ps.executeUpdate();

                con = getConnection();
                ps = con.prepareStatement(update_created_asset_ARCHIVE_qry);
                ps.setString(1, "Y");
                ps.setString(2, new_asset_id);
 //               System.out.println("Asset Id Inside update_created_asset_ARCHIVE_qry: "+new_asset_id);
                ps.setString(3, RegistrationNo);
                ps.setInt(4, Integer.parseInt(branch_id));
                ps.setInt(5, Integer.parseInt(dept_id));
                ps.setInt(6, Integer.parseInt(section_id));
                ps.setInt(7, Integer.parseInt(category_id));
                ps.setString(8, Description);
                ps.setString(9, VendorAC);
                ps.setString(10, Datepurchased);
                ps.setString(11, getDepreciationRate(categoryCode));
                ps.setString(12, AssetMake);
                ps.setString(13, AssetModel);
                ps.setString(14, AssetSerialNo);
                ps.setString(15, AssetEngineNo);
                ps.setInt(16, Integer.parseInt(SupplierName));
                ps.setString(17, AssetUser);
                ps.setInt(18, Integer.parseInt(AssetMaintenance));
                ps.setInt(19, 0);
                ps.setDouble(20, CostPrice);
                ps.setDate(21, dateConvert(depreciation_end_date));
                ps.setDouble(22, Double.parseDouble(residualvalue));
                ps.setString(23, AuthorizedBy);
                ps.setDate(24, dateConvert(new java.util.Date()));
                ps.setDate(25, dateConvert(depreciation_start_date));
                ps.setString(26, PurchaseReason);
                ps.setInt(27, Integer.parseInt(location));
                ps.setDouble(28, VatableCost);
                ps.setDouble(29, vatamount);
                ps.setString(30, WhTax);
                ps.setDouble(31, whtaxamount);
                ps.setString(32, require_depreciation);
                ps.setString(33, require_redistribution);
                ps.setString(34, SubjectTOVat);
                ps.setString(35, who_to_remind);
                ps.setString(36, email_1);
                ps.setString(37, who_to_remind_2);
                ps.setString(38, email2);
                ps.setString(39, "N");
                ps.setString(40, "0");
                ps.setString(41, section);
                ps.setInt(42, Integer.parseInt(State));
                ps.setInt(43, Integer.parseInt(Driver));
                ps.setString(44, spare_1);
                ps.setString(45, spare_2);
               // ps.setString(46, "ACTIVE");
                ps.setInt(46, Integer.parseInt(UserID));
                ps.setString(47, province);
                ps.setDate(48, dateConvert(warrantyStartDate));
                ps.setInt(49, Integer.parseInt(noOfMonths));
                ps.setDate(50, dateConvert(expiryDate));
                ps.setString(51,lpo);
                ps.setString(52,barCode);
                ps.setString(53,branchCode);
                ps.setString(54,categoryCode);
                ps.setString(55,groupid);
                ps.setString(56, partPAY);
                ps.setString(57, fullyPAID);
                ps.setString(58, deferPay);
                ps.setString(59,sbuCode);
                ps.setString(60,deptCode);
                ps.setString(61,sectionCode);
                ps.setString(62, integrifyId);
                ps.setInt(63, recno);
 //              System.out.println("RESULT AFTER UPDATING :::::::::::::::: " + result);
                /*
                 * chk if all the entries in am_group_asset have been updated
                 * update am_group_asset_main process_flag to Y
                 * chk if the approval level setup isn't zero
                 * call ad.setPendingtrans
                 */
 //               System.out.println("chk_process_flag qry :::::: " + chk_process_flag);
                ps = con.prepareStatement(chk_process_flag);
                rs = ps.executeQuery();
                if (rs.next()) 
                {
                	chk_Process_flag = rs.getInt(1);
    	            if(chk_Process_flag == 0)
    	            {
//    	            	System.out.println("Nothing to update in am_group_asset!!!!!!");
    	            	ps = con.prepareStatement(update_created_asset_main_qry);
    	            	ps.setString(1, "Y");
    	            	ps.setString(2, groupid);
    	            	ps.executeUpdate();

                        ps = con.prepareStatement(update_created_asset_main_ARCHIVE_qry);
    	            	ps.setString(1, "Y");
    	            	ps.setString(2, groupid);
    	            	ps.executeUpdate();
                            
    	            	status = true;
    	            }
                }
    	    }
            catch(Exception ex) 
            {
                System.out.println("WARN: Error updateCreatedAssetStatus ->" + ex);
            } finally {
                closeConnection(con, ps);
            }
            return status;
    	}
        public String setGroupPendingTrans(String[] a, String code,int assetCode){
        	String mtid = "";
            int transaction_level=0;
            Connection con;
            PreparedStatement ps;
            ResultSet rs;
     String pq =
    	 "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description," +
    	 "effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time," +
    	 "transaction_id,batch_id,transaction_level,asset_code) " +
    	 "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
     String tranLevelQuery = "select level from approval_level_setup where code ='"+code+"'";
            con = null;
            ps = null;
            rs = null;
            try
            {
                con = getConnection();
                 ps = con.prepareStatement(tranLevelQuery);
                  rs = ps.executeQuery();
                while(rs.next()){
                transaction_level = rs.getInt(1);
                }
                ps = con.prepareStatement(pq);

                SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");

                mtid =  new ApplicationHelper().getGeneratedId("am_asset_approval");

                ps.setString(1, (a[0]==null)?"":a[0]);
                ps.setString(2, (a[1]==null)?"":a[1]);
                ps.setString(3, (a[2]==null)?"":a[2]);
                ps.setDouble(4, (a[3]==null)?0:Double.parseDouble(a[3]));
                //System.out.println("Posting_Date b4 conversion in setPendingTrans : " +  a[4]);
                ps.setDate(5, (a[4])==null?null:dateConvert(a[4]));
                //System.out.println("Posting_Date after setPendingTrans : " +  a[4]);
                ps.setString(6, (a[5]==null)?"":a[5]);
               // System.out.println("effective_date b4 conversion in setPendingTrans : " +  a[6]);
                ps.setDate(7,(a[6])==null?null:dateConvert(a[6]));
                //System.out.println("effective_date after conversion in setPendingTrans : " +  a[6]);
                ps.setString(8, (a[7]==null)?"":a[7]);
 //               System.out.println("asset_status in setPendingTrans : " +  a[8]);
                ps.setString(9, (a[8]==null)?"":a[8]); //asset_status
                ps.setString(10, (a[9]==null)?"":a[9]);
                ps.setString(11, a[10]);
                ps.setString(12, timer.format(new java.util.Date()));
                ps.setString(13,mtid);
                ps.setString(14, mtid);
                ps.setInt(15, transaction_level);
                ps.setInt(16, assetCode);
                ps.execute();

            }
            catch(Exception er)
            {
                System.out.println(">>>GroupAssetBean:setGroupPendingTrans(>>>>>>" + er);

            }finally{
            closeConnection(con, ps);

            }
            return mtid;
        }
        public String[] setApprovalDataGroup(long id){

        	//String q ="select asset_id, asset_status,user_ID,supervisor,Cost_Price,Posting_Date,description,effective_date,BRANCH_CODE from am_asset where asset_id ='" +id+"'";
        	   String[] result= new String[12];
        	    Connection con = null;
        	        PreparedStatement ps = null;
        	        ResultSet rs = null;
        	        //int groupId = Integer.parseInt(id);
        	         String query ="select group_id,user_ID,supervisor,Cost_Price,Posting_Date," +
        	         		"		description,effective_date,BRANCH_CODE," +
        	         				"Asset_Status from am_group_asset_main where group_id =" +id ;
        	         //System.out.println("Query in setApprovalDataGroup : " + query);


        	        try {
        	            con = getConnection();
        	            ps = con.prepareStatement(query);
        	            rs = ps.executeQuery();
        	            while (rs.next()) {
        	                result[0] = rs.getString(1);
        	                result[1]= rs.getString(2);
        	                result[2] = rs.getString(3);
        	               result[3] = rs.getString(4);
        	               result[4] = formatDate(rs.getDate(5));
        	               //System.out.println("Before Conversion" + rs.getDate(5));
        	               //System.out.println("Posting_Date in setApprovalDataGroup : " +  result[4]);
        	               result[5] = rs.getString(6);
        	               result[6] = formatDate(rs.getDate(7));
        	              // System.out.println("Effective_Date in setApprovalDataGroup : " +  result[6]);
        	               result[7] = rs.getString(8);
        	               result[8] = rs.getString(9);//asset_status

        	            }

        	        } catch (Exception ex) {
        	            System.out.println("AssetRecordsBean : setApprovalData()WARN: Error setting approval data for group asset creation ->" + ex);
        	        } finally {
        	            closeConnection(con, ps);
        	        }
        	        	result[9] = "Group Asset Creation";
        	        	result[10] = "P";
        	//result[11] = timeInstance();
        	        return result;

        	}//setApprovalData()
        public boolean insertApprovalx2(String id, String description, String page, String flag, String partPay, String UserId, String Branch, String subjectToVat, String whTax, String url, int transID, int assetCode) {
            boolean done = true;
            flag = "Y";
            Connection con = null;
            PreparedStatement ps = null;
            String query = "INSERT INTO [am_raisentry_post](Id,Description,Page,Flag,partPay,UserId,"
                    + "Branch,subjectToVat,whTax,url,trans_id,entryPostFlag,asset_code)"
                    + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
            try {
                con = getConnection();
                ps = con.prepareStatement(query);
                ps.setString(1, id);
                ps.setString(2, description);
                ps.setString(3, page);
                ps.setString(4, flag);
                ps.setString(5, partPay);
                ps.setString(6, UserId);
                ps.setString(7, Branch);
                ps.setString(8, subjectToVat);
                ps.setString(9, whTax);
                ps.setString(10, url);
                ps.setInt(11, transID);
                ps.setString(12, "N");
                ps.setInt(13, assetCode);
                ps.execute();


            } catch (Exception ex) {
                done = false;
                System.out.println(
                        "WARNING:cannot insert am_raisentry_post->"
                        + ex.getMessage());
            } finally {
                closeConnection(con, ps);
            }
            return done;
        }
    	public void changeGroupAssetStatus(String id,String status,String assettype) 
    	{
 //   		System.out.println("changeGroupAssetStatus status: "+status+" Id: "+id);
    		// TODO Auto-generated method stub
    	//	System.out.println("changeGroupAssetStatus assettype: "+assettype+" Id: "+id);
    		Connection con = null;
    	    PreparedStatement ps = null;
    	    String query_r = "";
    	    if(assettype.equalsIgnoreCase("C")){    	    
    	    query_r ="update am_group_asset set asset_status=? " +
    		" where Group_id = '"+id+"'";
    	    }
    	    if(assettype.equalsIgnoreCase("U")){    	    
        	    query_r ="update AM_GROUP_ASSET_UNCAPITALIZED set asset_status=? " +
        		" where Group_id = '"+id+"'";
        	    }    	    
                String query_archive="update am_group_asset_archive set asset_status=? " +
    		" where Group_id = '"+id+"'";
      //          System.out.println("query_r:  "+query_r+" "+status);
       //         System.out.println("query_archive:  "+query_archive+" "+status);
    	    try 
    	    	{
    	    	con = getConnection();
    	    	ps = con.prepareStatement(query_r);
    	    	ps.setString(1,status);
    	       	int i =ps.executeUpdate();

                    ps = con.prepareStatement(query_archive);
    	    	ps.setString(1,status);
    	       	i =ps.executeUpdate();

    	        changeGroupAssetMainStatus(id,status);
    	        } 
    		catch (Exception ex)
    		    {
    		        System.out.println("GroupAssetToAssetBean: Error Updating am_group_asset " + ex);
    		    } 
    		finally 
    			{
    	            closeConnection(con, ps);
    	        }    		
    	}
    	public void changeGroupAssetMainStatus(String id, String status2)
    	{
 //   		System.out.println("changeGroupAssetMainStatus status2: "+status2+" Id: "+id);
    		// TODO Auto-generated method stub
    		String query_r ="update am_group_asset_main set asset_status=? " +
    		"where Group_id = '"+id+"'";

                    String query_archive ="update am_group_asset_main_archive set asset_status=? " +
    		"where Group_id = '"+id+"'";
       //        System.out.println("changeGroupAssetMainStatus query_r: "+query_r);  
     //          System.out.println("changeGroupAssetMainStatus query_archive: "+query_archive);    
    		Connection con = null;
    	    PreparedStatement ps = null;
    	    try 
    		{
    		con = getConnection();
    		ps = con.prepareStatement(query_r);
    		ps.setString(1,status2);
    	    int i =ps.executeUpdate();

                ps = con.prepareStatement(query_archive);
    		ps.setString(1,status2);
    	     i =ps.executeUpdate();

    	    } 
    	    catch (Exception ex)
    	    {
    	        System.out.println("GroupAssetToAssetBean: Error Updating am_group_asset_main : " + ex);
    	    } 
    	    finally 
    		{
    	        dbConnection.closeConnection(con, ps);
    	    }
    	}
        public void freeResource() {
            try {

                if (this.con != null) {
                    this.con.close();
                }
                this.con = null;
            } catch (Exception e) {
                System.out.println("WARN:Error closing Connection ->" +
                                   e.getMessage());
            }

        }
    	private long retrieveMaxGroupID()
    	{
    		// TODO Auto-generated method stub
    		Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            long maxNum=0;
    		String groupID_qry = "select MAX(group_id) from am_group_asset_main";
    		try
            {
                con = getConnection();
                ps = con.prepareStatement(groupID_qry);
                rs = ps.executeQuery();
                if (rs.next())
                {
                	 maxNum = rs.getLong(1);
                }
  //              System.out.println("Max GroupID retrieved : " + maxNum);
            }
    		catch (Exception e)
            {
                System.out.println("INFO:Error retrieving Maximum GroupID ->" +
                                   e.getMessage());
            }
    		finally
            {
                dbConnection.closeConnection(con, ps, rs);
            }
    		return maxNum;
    	}
    	public boolean newgroupassetinterface(String errormessage,int recno,String status,String assetid,String assetcode,String recintegrifyId) {

    		Connection con = null;
    		PreparedStatement ps = null;
    		boolean done = false;
    		String query = "UPDATE NEW_GROUP_ASSET_INTERFACE"
    				+ " SET ERROR_MESSAGE = '"+errormessage+"',POSTED='"+status+"',ASSET_ID = '"+assetid.trim()+"'," 
    				+ " ASSET_CODE = '"+assetcode.trim()+"' "
    				+ " WHERE ITEMCOUNT = '"+recno+"' AND INTEGRIFY_ID = '"+recintegrifyId+"' ";
    	//	System.out.println("query: "+query);
    		String query2 = "UPDATE NEW_GROUP_ASSET_INTERFACE"
    			+ " SET ERROR_MESSAGE = '"+errormessage+"',POSTED='"+status+"' " 
    			+ " WHERE ITEMCOUNT = '"+recno+"' AND INTEGRIFY_ID = '"+recintegrifyId+"' ";
    		//System.out.println("query2: "+query2);
    		try {
    			con = getConnection();
    			if(status.equalsIgnoreCase("Y")){
    			ps = con.prepareStatement(query);}
    			else{ps = con.prepareStatement(query2);}
    			done = (ps.executeUpdate() != -1);

    		} catch (Exception e) {
    			System.out.println("WARNING:Error executing Query ->"
    					+ e.getMessage());
    		} finally {
    			closeConnection(con, ps);
    		}
    		return done;

    	}  

    	 public String checkAssetAvalability(String assetID){
    	   //  System.out.println("\nttttttttttttttt "+assetID);
    	    String query = " SELECT transaction_id from am_asset_approval " +
    	                       " WHERE process_status='P' and asset_id = '"+assetID+"'";
    	//    System.out.println("query nttttttttttttttt "+query);
    	//	df = new com.magbel.util.DatetimeFormat();

    	String transactionId ="";
    	String process_status ="";
    	        Connection con = null;
    	        PreparedStatement ps = null;
    	        ResultSet rs = null;
    	        String Datefld = formatDate(new java.util.Date());
    	//        System.out.println("====Datefld== "+Datefld);
    	        String Month = Datefld.substring(3, 5);
    	        String Year = Datefld.substring(6, 10);
    	              try {
    	            con = getConnection();
    	            ps = con.prepareStatement(query);
    	            rs = ps.executeQuery();

    	            while (rs.next()) { 
    	               transactionId = rs.getString("transaction_id");
    	           //     System.out.println("\n>>>>> trans id " + transactionId);
    	               process_status =   transactionId;
    	            }
    	            if(transactionId.equalsIgnoreCase("")){
    	 String query1 = " SELECT asset_code from am_asset WHERE asset_id = '"+assetID+"'";
    	              int assetCode = Integer.parseInt(getCodeName(query1));
    	  String queryTest1 = getCodeName("select max(trans_id) from am_raisentry_post where  asset_code="+assetCode);
    	  String queryTest2 = getCodeName("select max(trans_id)  from am_raisentry_TRANSACTION where asset_code="+assetCode);
    	  String query2 =getCodeName("select asset_code from am_asset_improvement where asset_id = '"+assetID+"' and month(revalue_date) = '"+Month+"' and year(revalue_date) = '"+Year+"'");
    	  String query3 =getCodeName("select asset_code from am_asset_revaluation where asset_id = '"+assetID+"' and month(revalue_date) = '"+Month+"' and year(revalue_date) = '"+Year+"'");
    	  String query4 =getCodeName("select asset_code from am_AssetDisposal where asset_id = '"+assetID+"' and DISPOSAL_PERCENT = 100 ");
    	 String query5 =getCodeName("select count(*) from am_raisentry_post where asset_code = '"+assetCode+"'  and entryPostFlag = 'N' ");
    	  if(query2.equalsIgnoreCase("") && query3.equalsIgnoreCase("") && query4.equalsIgnoreCase("")){process_status="";}else {process_status="D";}   
    	  if(queryTest1.equalsIgnoreCase(queryTest2) && !queryTest1.equalsIgnoreCase("") && !queryTest2.equalsIgnoreCase("")){
    	      //check if posted succceffuly on am_raisentry_TRANSACTION
    	String queryTest3 = getCodeName("select iso  from am_raisentry_TRANSACTION where asset_code="+assetCode+" and trans_id="+queryTest1+" ");
    	 // System.out.println("--queryTest2--> "+queryTest2);    
    	if(process_status.equalsIgnoreCase("")){
    	if(queryTest3.equalsIgnoreCase("000")){process_status="";}else {process_status="N";}
    	}
    	//System.out.println("--process_status-1-> "+process_status);
    	  }
    	       else
    	            { 
    	             String queryTest4 = getCodeName("SELECT process_status from am_asset_approval WHERE process_status='A' and asset_id = '"+assetID+"'");
    	             if(queryTest4.equalsIgnoreCase("A"))
    	             { 
    	                 String queryTest5 = getCodeName("select max(trans_id) from am_raisentry_post where  asset_code="+assetCode);
    	              //   System.out.println("--queryTest1--> "+queryTest1);
    	                 String queryTest6 = getCodeName("select max(trans_id)  from am_raisentry_TRANSACTION where asset_code="+assetCode);
    	                 if(queryTest5.equalsIgnoreCase(queryTest6) && !queryTest5.equalsIgnoreCase("") && !queryTest6.equalsIgnoreCase(""))
    	                 {
    	      //check if posted succceffuly on am_raisentry_TRANSACTION
    	                 String queryTest7 = getCodeName("select iso  from am_raisentry_TRANSACTION where asset_code="+assetCode+" and trans_id="+queryTest5+" ");
    	 // System.out.println("--queryTest7--> "+queryTest7);
    	                 if(process_status.equalsIgnoreCase("")){
    	                 if(queryTest7.equalsIgnoreCase("000")){process_status="";}else {process_status="N";}
    	                 }
    	                 }//else {process_status="N";}
    	             }
    	   //          System.out.println("--process_status-1-> "+process_status);
    	            }
    	            }
    	      
    	        } catch (Exception e) {
    	            System.out.println(
    	                    "AssetManager: checkAssetAvalability(String assetID):INFO:->" +
    	                    e.getMessage());
    	        } finally {
    	            closeConnection(con, ps, rs);
    	        }
    	            return process_status;
    	    }//

    	    public String getCodeName(String query) {
    	        String result = "";
    	        Connection con = null;
    	        ResultSet rs = null;
    	        PreparedStatement ps = null; 
    	//System.out.println("====getCodeName query=====  "+query);
    	        try {
    	            con = getConnection();
    	            ps = con.prepareStatement(query);
    	            rs = ps.executeQuery();
    	            while (rs.next()) {
    	                result = rs.getString(1) == null ? "" : rs.getString(1);
    	            }
    	        } catch (Exception er) {
    	            System.out.println("Error in Query- getCodeName()... ->" + er);
    	            er.printStackTrace();
    	        } finally {
    	            closeConnection(con, ps);
    	        }
//    	        System.out.println("====getCodeName result=====  "+result);
    	        return result;
    	    }
/*    	    
    	    public boolean deleteinvoiceintegrify(String invoiceNo)
    	    {
    	    	//System.out.println("====findObject query=====  "+query);
    	    	boolean done = false;
    	        Connection Con2 = null;
    	        PreparedStatement Stat = null;
    	        ResultSet result = null;
    	        String query = ("Delete from  Am_Invoice_no WHERE invoice_no = '"+invoiceNo+"'");
    	        try {

    	        	Con2 = getConnection();
    				ps = Con2.prepareStatement(query);
    				done = (ps.executeUpdate() != -1);

    	        } catch (Exception ee2) {
    	            System.out.println("WARN:ERROR deleteObject --> " + ee2);
    	            ee2.printStackTrace();
    	        } finally {
    	            closeConnection(Con2, Stat);
    	        }

    	        return done;
    	    }    
    */
    
}
