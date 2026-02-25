package legend.admin.handlers;

import com.magbel.ia.vao.ModuleCodes;
import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.vao.SendMail;

import legend.admin.objects.AssetManagerInfo;
import legend.admin.objects.Branch;
import legend.admin.objects.Company;
import legend.admin.objects.Sbu_branch;
import legend.admin.objects.Section;
import legend.admin.objects.Sla;

import com.magbel.legend.vao.ViewAssetDetails;
import com.magbel.legend.vao.newAssetTransaction;

import legend.ConnectionClass;
import legend.admin.objects.equipmentElement;
import legend.admin.objects.opexAcctType;
import legend.admin.objects.vendorCriteria;
import magma.net.vao.FleetManatainanceRecord;

import com.magbel.util.ApplicationHelper;

import java.io.File;
import java.io.FileInputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;

import com.magbel.util.DataConnect;

import java.sql.SQLException;

import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import magma.DateManipulations;
import magma.asset.dto.Asset;
import magma.asset.dto.Improvement;
import magma.net.dao.MagmaDBConnection;
import magma.net.manager.AssetPaymentManager;
import magma.net.manager.FleetHistoryManager;
import magma.net.vao.BranchVisit;
import magma.net.vao.FMppmAllocation;
import magma.net.vao.Stock;

import com.magbel.util.HtmlUtility;

//import magma.net.manager.AssetPaymentManager;
//import magma.net.manager.FleetHistoryManager;
import magma.util.Codes;
/**
 * @author Rahman Oloritun
 * @Updated by Lekan Matanmi
 * @Entities company,AssetmanagerInfo,Driver,Location,categoryClasses, ASSETMAKE
 */
public class CompanyHandler {
	Connection con = null;

	private MagmaDBConnection dbConnection;

	Statement stmt = null;

	PreparedStatement ps = null;

	ResultSet rs = null;

	DataConnect dc;

	SimpleDateFormat sdf;

	final String space = "  ";

	final String comma = ",";
	
	ApplicationHelper applicationHelper;

	java.util.Date date;
	newAssetTransaction newassettrans;
	com.magbel.util.DatetimeFormat df;
	HtmlUtility  htmlUtil;

	public CompanyHandler() {

		sdf = new SimpleDateFormat("dd-MM-yyyy");
		df = new com.magbel.util.DatetimeFormat();
		newassettrans = new newAssetTransaction();
		applicationHelper = new ApplicationHelper();
//		System.out.println("USING_ " + this.getClass().getName());
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

	private String getDepreciationRate(String categoryCode) {

		String query = "SELECT DEP_RATE FROM AM_AD_CATEGORY WHERE CATEGORY_CODE = ?";
		String rate = "0.0";

		try (Connection con = getConnection();
			 PreparedStatement ps = con.prepareStatement(query)) {

			ps.setQueryTimeout(30);
			ps.setString(1, categoryCode);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					rate = rs.getString(1);
				}
			}

		} catch (Exception ex) {
			System.err.println("Error fetching DepreciationRate -> " + ex.getMessage());
		}

		return rate;
	}


//	public legend.admin.objects.Company getCompany() {
//		legend.admin.objects.Company company = null;
//		String query = "SELECT Company_Code, Company_Name, Acronym, Company_Address"
//				+ ",  Vat_Rate, Wht_Rate,  Financial_Start_Date, Financial_No_OfMonths"
//				+ ", Financial_End_Date,Minimum_Password, Password_Expiry, Session_Timeout,Proof_Session_timeout"
//				+ ", Enforce_Acq_Budget, Enforce_Pm_Budget, Enforce_Fuel_Allocation, Require_Quarterly_Pm"
//				+ ", Quarterly_Surplus_Cf,User_Id, Processing_Status, Trans_Wait_Time,loguseraudit "
//				+ ", password_upper,password_lower,password_numeric,password_special,password_limit FROM AM_GB_COMPANY";
//		Connection c = null;
//		ResultSet rs = null;
//		Statement s = null;
////		 System.out.println("getCompany query>>>>> :  "+query);
//		try {
//			c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
//			while (rs.next()) {
//				String companyCode = rs.getString("Company_Code");
//				String companyName = rs.getString("Company_Name");
//				String acronym = rs.getString("Acronym");
//				String companyAddress = rs.getString("Company_Address");
//				double vatRate = rs.getDouble("Vat_Rate");
//				double whtRate = rs.getDouble("Wht_Rate");//to do;get value for federal and state rate
//			String financialStartDate = sdf.format(rs
//						.getDate("Financial_Start_Date"));
//				int financialNoOfMonths = rs.getInt("Financial_No_OfMonths");
//				String financialEndDate = sdf.format(rs
//						.getDate("Financial_End_Date"));
//				int minimumPassword = rs.getInt("Minimum_Password");
//				int passwordExpiry = rs.getInt("Password_Expiry");
//				int sessionTimeout = rs.getInt("Session_Timeout");
//				int proofsessionTimeout = rs.getInt("Proof_Session_timeout");
//				String enforceAcqBudget = rs.getString("Enforce_Acq_Budget");
////				System.out.println("getCompany proofsessionTimeout>>> :  "+proofsessionTimeout+"   sessionTimeout: "+sessionTimeout);
//				String enforcePmBudget = rs.getString("Enforce_Pm_Budget");
//
//				String enforceFuelAllocation = rs
//						.getString("Enforce_Fuel_Allocation");
//
//				String requireQuarterlyPM = rs
//						.getString("Require_Quarterly_Pm");
//
//				String quarterlySurplusCf = rs
//						.getString("Quarterly_Surplus_Cf");
//
//				String userId = rs.getString("User_Id");
//				String processingStatus = rs.getString("Processing_Status");
//				String logUserAudit = rs.getString("loguseraudit");
//
//				double transWaitTime = rs.getDouble("Trans_Wait_Time");
//				String password_upper = rs.getString("password_upper");
//				String password_lower = rs.getString("password_lower");
//				String password_numeric = rs.getString("password_numeric");
//				String password_special = rs.getString("password_special");
//                int password_limit = rs.getInt("password_limit");
//				//to do give a condition for federal or state and use the value for whtRate
//
//				company = new legend.admin.objects.Company(companyCode,
//						companyName, acronym, companyAddress, vatRate, whtRate,
//						financialStartDate, financialNoOfMonths,
//						financialEndDate, minimumPassword, passwordExpiry,
//						sessionTimeout, enforceAcqBudget, enforcePmBudget,
//						enforceFuelAllocation, requireQuarterlyPM,
//						quarterlySurplusCf, userId, processingStatus,
//						transWaitTime);
//				        company.setLogUserAudit(logUserAudit);
//                        company.setPassword_lower(password_lower);
//						company.setPassword_numeric(password_numeric);
//						company.setPassword_special(password_special);
//						company.setPassword_upper(password_upper);
//                        company.setPasswordLimit(password_limit);
//                        company.setProofSessionTimeout(proofsessionTimeout);
//
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		finally {
//			closeConnection(c, s, rs);
//		}
//		return company;
//	}


	public legend.admin.objects.Company getCompany() {

		legend.admin.objects.Company company = null;

		String sql =
				"SELECT Company_Code, Company_Name, Acronym, Company_Address," +
						" Vat_Rate, Wht_Rate, Financial_Start_Date, Financial_No_OfMonths," +
						" Financial_End_Date, Minimum_Password, Password_Expiry," +
						" Session_Timeout, Proof_Session_timeout," +
						" Enforce_Acq_Budget, Enforce_Pm_Budget, Enforce_Fuel_Allocation," +
						" Require_Quarterly_Pm, Quarterly_Surplus_Cf, User_Id," +
						" Processing_Status, Trans_Wait_Time, loguseraudit," +
						" password_upper, password_lower, password_numeric," +
						" password_special, password_limit" +
						" FROM AM_GB_COMPANY";

		try (
				Connection con = getConnection();
				PreparedStatement ps = con.prepareStatement(sql)
		) {

			ps.setQueryTimeout(30); // Prevent long-held connections

			try (ResultSet rs = ps.executeQuery()) {

				if (rs.next()) {

					Date startDateObj = rs.getDate("Financial_Start_Date");
					Date endDateObj   = rs.getDate("Financial_End_Date");

					String financialStartDate =
							(startDateObj != null) ? sdf.format(startDateObj) : null;

					String financialEndDate =
							(endDateObj != null) ? sdf.format(endDateObj) : null;

					company = new legend.admin.objects.Company(
							rs.getString("Company_Code"),
							rs.getString("Company_Name"),
							rs.getString("Acronym"),
							rs.getString("Company_Address"),
							rs.getDouble("Vat_Rate"),
							rs.getDouble("Wht_Rate"),
							financialStartDate,
							rs.getInt("Financial_No_OfMonths"),
							financialEndDate,
							rs.getInt("Minimum_Password"),
							rs.getInt("Password_Expiry"),
							rs.getInt("Session_Timeout"),
							rs.getString("Enforce_Acq_Budget"),
							rs.getString("Enforce_Pm_Budget"),
							rs.getString("Enforce_Fuel_Allocation"),
							rs.getString("Require_Quarterly_Pm"),
							rs.getString("Quarterly_Surplus_Cf"),
							rs.getString("User_Id"),
							rs.getString("Processing_Status"),
							rs.getDouble("Trans_Wait_Time")
					);

					company.setLogUserAudit(rs.getString("loguseraudit"));
					company.setPassword_upper(rs.getString("password_upper"));
					company.setPassword_lower(rs.getString("password_lower"));
					company.setPassword_numeric(rs.getString("password_numeric"));
					company.setPassword_special(rs.getString("password_special"));
					company.setPasswordLimit(rs.getInt("password_limit"));
					company.setProofSessionTimeout(rs.getInt("Proof_Session_timeout"));
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error executing getCompany()", e);
		}

		return company;
	}

//		public legend.admin.objects.Company getCompanytmp() {
//			legend.admin.objects.Company company = null;
//			String query = "SELECT Company_Code, Company_Name, Acronym, Company_Address"
//					+ ",  Vat_Rate, Wht_Rate,  Financial_Start_Date, Financial_No_OfMonths"
//					+ ", Financial_End_Date,Minimum_Password, Password_Expiry, Session_Timeout,Proof_Session_timeout"
//					+ ", Enforce_Acq_Budget, Enforce_Pm_Budget, Enforce_Fuel_Allocation, Require_Quarterly_Pm"
//					+ ", Quarterly_Surplus_Cf,User_Id, Processing_Status, Trans_Wait_Time,loguseraudit "
//					+ ", password_upper,password_lower,password_numeric,password_special,password_limit FROM AM_GB_COMPANYTEMP";
//			Connection c = null;
//			ResultSet rs = null;
//			Statement s = null;
////			 System.out.println("getCompany query>>>>> :  "+query);
//			try {
//				c = getConnection();
//				s = c.createStatement();
//				rs = s.executeQuery(query);
//				while (rs.next()) {
//					String companyCode = rs.getString("Company_Code");
//					String companyName = rs.getString("Company_Name");
//					String acronym = rs.getString("Acronym");
//					String companyAddress = rs.getString("Company_Address");
//					double vatRate = rs.getDouble("Vat_Rate");
//					double whtRate = rs.getDouble("Wht_Rate");//to do;get value for federal and state rate
//				String financialStartDate = sdf.format(rs
//							.getDate("Financial_Start_Date"));
//					int financialNoOfMonths = rs.getInt("Financial_No_OfMonths");
//					String financialEndDate = sdf.format(rs
//							.getDate("Financial_End_Date"));
//					int minimumPassword = rs.getInt("Minimum_Password");
//					int passwordExpiry = rs.getInt("Password_Expiry");
//					int sessionTimeout = rs.getInt("Session_Timeout");
//					int proofsessionTimeout = rs.getInt("Proof_Session_timeout");
//					String enforceAcqBudget = rs.getString("Enforce_Acq_Budget");
////					System.out.println("getCompany proofsessionTimeout>>> :  "+proofsessionTimeout+"   sessionTimeout: "+sessionTimeout);
//					String enforcePmBudget = rs.getString("Enforce_Pm_Budget");
//
//					String enforceFuelAllocation = rs
//							.getString("Enforce_Fuel_Allocation");
//
//					String requireQuarterlyPM = rs
//							.getString("Require_Quarterly_Pm");
//
//					String quarterlySurplusCf = rs
//							.getString("Quarterly_Surplus_Cf");
//
//					String userId = rs.getString("User_Id");
//					String processingStatus = rs.getString("Processing_Status");
//					String logUserAudit = rs.getString("loguseraudit");
//
//					double transWaitTime = rs.getDouble("Trans_Wait_Time");
//					String password_upper = rs.getString("password_upper");
//					String password_lower = rs.getString("password_lower");
//					String password_numeric = rs.getString("password_numeric");
//					String password_special = rs.getString("password_special");
//	                int password_limit = rs.getInt("password_limit");
//					//to do give a condition for federal or state and use the value for whtRate
//
//					company = new legend.admin.objects.Company(companyCode,
//							companyName, acronym, companyAddress, vatRate, whtRate,
//							financialStartDate, financialNoOfMonths,
//							financialEndDate, minimumPassword, passwordExpiry,
//							sessionTimeout, enforceAcqBudget, enforcePmBudget,
//							enforceFuelAllocation, requireQuarterlyPM,
//							quarterlySurplusCf, userId, processingStatus,
//							transWaitTime);
//					        company.setLogUserAudit(logUserAudit);
//	                        company.setPassword_lower(password_lower);
//							company.setPassword_numeric(password_numeric);
//							company.setPassword_special(password_special);
//							company.setPassword_upper(password_upper);
//	                        company.setPasswordLimit(password_limit);
//	                        company.setProofSessionTimeout(proofsessionTimeout);
//
//				}
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//			finally {
//				closeConnection(c, s, rs);
//			}
//			return company;
//		}
private legend.admin.objects.Company mapCompanyFromResultSet(ResultSet rs) throws SQLException {

	Date startDateObj = rs.getDate("Financial_Start_Date");
	Date endDateObj   = rs.getDate("Financial_End_Date");

	String financialStartDate =
			(startDateObj != null) ? sdf.format(startDateObj) : null;

	String financialEndDate =
			(endDateObj != null) ? sdf.format(endDateObj) : null;

	legend.admin.objects.Company company =
			new legend.admin.objects.Company(
					rs.getString("Company_Code"),
					rs.getString("Company_Name"),
					rs.getString("Acronym"),
					rs.getString("Company_Address"),
					rs.getDouble("Vat_Rate"),
					rs.getDouble("Wht_Rate"),
					financialStartDate,
					rs.getInt("Financial_No_OfMonths"),
					financialEndDate,
					rs.getInt("Minimum_Password"),
					rs.getInt("Password_Expiry"),
					rs.getInt("Session_Timeout"),
					rs.getString("Enforce_Acq_Budget"),
					rs.getString("Enforce_Pm_Budget"),
					rs.getString("Enforce_Fuel_Allocation"),
					rs.getString("Require_Quarterly_Pm"),
					rs.getString("Quarterly_Surplus_Cf"),
					rs.getString("User_Id"),
					rs.getString("Processing_Status"),
					rs.getDouble("Trans_Wait_Time")
			);

	company.setLogUserAudit(rs.getString("loguseraudit"));
	company.setPassword_upper(rs.getString("password_upper"));
	company.setPassword_lower(rs.getString("password_lower"));
	company.setPassword_numeric(rs.getString("password_numeric"));
	company.setPassword_special(rs.getString("password_special"));
	company.setPasswordLimit(rs.getInt("password_limit"));
	company.setProofSessionTimeout(rs.getInt("Proof_Session_timeout"));

	return company;
}
	public legend.admin.objects.Company getCompanytmp() {

		legend.admin.objects.Company company = null;

		String sql =
				"SELECT Company_Code, Company_Name, Acronym, Company_Address," +
						" Vat_Rate, Wht_Rate, Financial_Start_Date, Financial_No_OfMonths," +
						" Financial_End_Date, Minimum_Password, Password_Expiry," +
						" Session_Timeout, Proof_Session_timeout," +
						" Enforce_Acq_Budget, Enforce_Pm_Budget, Enforce_Fuel_Allocation," +
						" Require_Quarterly_Pm, Quarterly_Surplus_Cf, User_Id," +
						" Processing_Status, Trans_Wait_Time, loguseraudit," +
						" password_upper, password_lower, password_numeric," +
						" password_special, password_limit" +
						" FROM AM_GB_COMPANYTEMP";

		try (
				Connection con = getConnection();
				PreparedStatement ps = con.prepareStatement(sql)
		) {

			ps.setQueryTimeout(30);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					company = mapCompanyFromResultSet(rs);
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error executing getCompanytmp()", e);
		}

		return company;
	}



//Ganiyu's code: the getCompanyFed() method that has both state and federal withholding tax rate

	public legend.admin.objects.Company getCompanyFed() {

		legend.admin.objects.Company company = null;

		String query =
				"SELECT Company_Code, Company_Name, Acronym, Company_Address" +
						", Vat_Rate, Wht_Rate, Financial_Start_Date, Financial_No_OfMonths" +
						", Financial_End_Date, Minimum_Password, Password_Expiry, Session_Timeout" +
						", Enforce_Acq_Budget, Enforce_Pm_Budget, Enforce_Fuel_Allocation, Require_Quarterly_Pm" +
						", Quarterly_Surplus_Cf, User_Id, Processing_Status, Trans_Wait_Time, loguseraudit" +
						", Fed_Wht_Rate, Attempt_Logon, component_delimiter " +
						"FROM AM_GB_COMPANY";

		try (
				Connection con = getConnection();
				PreparedStatement ps = con.prepareStatement(query)
		) {

			ps.setQueryTimeout(30);

			try (ResultSet rs = ps.executeQuery()) {

				if (rs.next()) {

					Date startDateObj = rs.getDate("Financial_Start_Date");
					Date endDateObj   = rs.getDate("Financial_End_Date");

					String financialStartDate =
							(startDateObj != null) ? sdf.format(startDateObj) : null;

					String financialEndDate =
							(endDateObj != null) ? sdf.format(endDateObj) : null;

					String compDelimiter = rs.getString("component_delimiter");
					if (compDelimiter == null) {
						compDelimiter = "";
					}

					company = new legend.admin.objects.Company(
							rs.getString("Company_Code"),
							rs.getString("Company_Name"),
							rs.getString("Acronym"),
							rs.getString("Company_Address"),
							rs.getDouble("Vat_Rate"),
							rs.getDouble("Wht_Rate"),
							rs.getDouble("Fed_Wht_Rate"),
							financialStartDate,
							rs.getInt("Financial_No_OfMonths"),
							financialEndDate,
							rs.getInt("Minimum_Password"),
							rs.getInt("Password_Expiry"),
							rs.getInt("Session_Timeout"),
							rs.getString("Enforce_Acq_Budget"),
							rs.getString("Enforce_Pm_Budget"),
							rs.getString("Enforce_Fuel_Allocation"),
							rs.getString("Require_Quarterly_Pm"),
							rs.getString("Quarterly_Surplus_Cf"),
							rs.getString("User_Id"),
							rs.getString("Processing_Status"),
							rs.getDouble("Trans_Wait_Time"),
							rs.getInt("Attempt_Logon")
					);

					company.setLogUserAudit(rs.getString("loguseraudit"));
					company.setComp_delimiter(compDelimiter);
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error executing getCompanyFed()", e);
		}

		return company;
	}

	public legend.admin.objects.Company getCompanyFed1() {

		legend.admin.objects.Company company = null;

		String query =
				"SELECT Company_Code, Company_Name, Acronym, Company_Address" +
						", Vat_Rate, Wht_Rate, Financial_Start_Date, Financial_No_OfMonths" +
						", Financial_End_Date, Minimum_Password, Password_Expiry, Session_Timeout" +
						", Enforce_Acq_Budget, Enforce_Pm_Budget, Enforce_Fuel_Allocation, Require_Quarterly_Pm" +
						", Quarterly_Surplus_Cf, User_Id, Processing_Status, Trans_Wait_Time, loguseraudit" +
						", Fed_Wht_Rate, Attempt_Logon, component_delimiter, password_upper, password_lower, password_numeric" +
						", Processing_Date, Processing_Frequency, Next_Processing_Date, Default_Branch, Branch_Name" +
						", Auto_Generate_Id, Depreciation_Method, LPO_Required, Barcode_Fld, THIRDPARTY_REQUIRE, raise_entry, databaseName" +
						", password_special, password_limit, Proof_Session_timeout FROM AM_GB_COMPANY";

		try (
				Connection con = getConnection();
				PreparedStatement ps = con.prepareStatement(query)
		) {

			ps.setQueryTimeout(30);

			try (ResultSet rs = ps.executeQuery()) {

				if (rs.next()) {

					Date startDateObj = rs.getDate("Financial_Start_Date");
					Date endDateObj   = rs.getDate("Financial_End_Date");

					String financialStartDate =
							(startDateObj != null) ? sdf.format(startDateObj) : null;

					String financialEndDate =
							(endDateObj != null) ? sdf.format(endDateObj) : null;

					String compDelimiter = rs.getString("component_delimiter");
					if (compDelimiter == null) {
						compDelimiter = "";
					}

					String proofSessionTimeout = rs.getString("Proof_Session_timeout");
					if (proofSessionTimeout == null || "null".equalsIgnoreCase(proofSessionTimeout)) {
						proofSessionTimeout = "0";
					}

					company = new legend.admin.objects.Company(
							rs.getString("Company_Code"),
							rs.getString("Company_Name"),
							rs.getString("Acronym"),
							rs.getString("Company_Address"),
							rs.getDouble("Vat_Rate"),
							rs.getDouble("Wht_Rate"),
							rs.getDouble("Fed_Wht_Rate"),
							financialStartDate,
							rs.getInt("Financial_No_OfMonths"),
							financialEndDate,
							rs.getInt("Minimum_Password"),
							rs.getInt("Password_Expiry"),
							rs.getInt("Session_Timeout"),
							rs.getString("Enforce_Acq_Budget"),
							rs.getString("Enforce_Pm_Budget"),
							rs.getString("Enforce_Fuel_Allocation"),
							rs.getString("Require_Quarterly_Pm"),
							rs.getString("Quarterly_Surplus_Cf"),
							rs.getString("User_Id"),
							rs.getString("Processing_Status"),
							rs.getDouble("Trans_Wait_Time"),
							rs.getInt("Attempt_Logon")
					);

					company.setLogUserAudit(rs.getString("loguseraudit"));
					company.setComp_delimiter(compDelimiter);
					company.setPassword_lower(rs.getString("password_lower"));
					company.setPassword_numeric(rs.getString("password_numeric"));
					company.setPassword_special(rs.getString("password_special"));
					company.setPassword_upper(rs.getString("password_upper"));
					company.setPasswordLimit(rs.getInt("password_limit"));
					company.setProofSessionTimeout(Integer.parseInt(proofSessionTimeout));
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error executing getCompanyFed1()", e);
		}

		return company;
	}

	public Company getCompanyFed2() {

		Company company = null;

		final String sql =
				"SELECT Company_Code, Company_Name, Acronym, Company_Address, " +
						"       Vat_Rate, Wht_Rate, Financial_Start_Date, Financial_No_OfMonths, " +
						"       Financial_End_Date, Minimum_Password, Password_Expiry, Session_Timeout, " +
						"       Enforce_Acq_Budget, Enforce_Pm_Budget, Enforce_Fuel_Allocation, Require_Quarterly_Pm, " +
						"       Quarterly_Surplus_Cf, User_Id, Processing_Status, Trans_Wait_Time, loguseraudit, " +
						"       Fed_Wht_Rate, Attempt_Logon, component_delimiter, password_upper, password_lower, password_numeric, " +
						"       Processing_Date, Processing_Frequency, Next_Processing_Date, Default_Branch, Branch_Name, " +
						"       Auto_Generate_Id, Depreciation_Method, LPO_Required, Barcode_Fld, THIRDPARTY_REQUIRE, raise_entry, databaseName, " +
						"       password_special, password_limit, Proof_Session_timeout, system_date " +
						"FROM AM_GB_COMPANY";

		try (Connection connection = getConnection();
			 PreparedStatement statement = connection.prepareStatement(
					 sql,
					 ResultSet.TYPE_FORWARD_ONLY,
					 ResultSet.CONCUR_READ_ONLY)) {

			statement.setQueryTimeout(30);

			try (ResultSet rs = statement.executeQuery()) {

				if (!rs.next()) {
					return null;
				}

				company = new Company(
						rs.getString("Company_Code"),
						rs.getString("Company_Name"),
						rs.getString("Acronym"),
						rs.getString("Company_Address"),
						getNullableDouble(rs, "Vat_Rate"),
						getNullableDouble(rs, "Wht_Rate"),
						getNullableDouble(rs, "Fed_Wht_Rate"),
						formatDate(rs.getDate("Financial_Start_Date")),
						rs.getInt("Financial_No_OfMonths"),
						formatDate(rs.getDate("Financial_End_Date")),
						rs.getInt("Minimum_Password"),
						rs.getInt("Password_Expiry"),
						rs.getInt("Session_Timeout"),
						rs.getString("Enforce_Acq_Budget"),
						rs.getString("Enforce_Pm_Budget"),
						rs.getString("Enforce_Fuel_Allocation"),
						rs.getString("Require_Quarterly_Pm"),
						rs.getString("Quarterly_Surplus_Cf"),
						rs.getString("User_Id"),
						rs.getString("Processing_Status"),
						getNullableDouble(rs, "Trans_Wait_Time"),
						rs.getInt("Attempt_Logon")
				);

				// Safe optional setters
				company.setLogUserAudit(rs.getString("loguseraudit"));
				company.setComp_delimiter(defaultString(rs.getString("component_delimiter")));
				company.setPassword_lower(rs.getString("password_lower"));
				company.setPassword_numeric(rs.getString("password_numeric"));
				company.setPassword_special(rs.getString("password_special"));
				company.setPassword_upper(rs.getString("password_upper"));
				company.setPasswordLimit(rs.getInt("password_limit"));

				company.setProofSessionTimeout(
						parseSafeInt(rs.getString("Proof_Session_timeout"), 0)
				);

				company.setThirdpartytransaction(rs.getString("THIRDPARTY_REQUIRE"));
				company.setRaiseEntry(rs.getString("raise_entry"));
				company.setDatabaseName(rs.getString("databaseName"));
				company.setSysDate(formatDate(rs.getDate("system_date")));
				company.setProcessingDate(formatDate(rs.getDate("Processing_Date")));
				company.setNextProcessingDate(formatDate(rs.getDate("Next_Processing_Date")));
				company.setProcessingFrequency(rs.getString("Processing_Frequency"));
			}

		} catch (Exception ex) {
			throw new RuntimeException(
					"Failed to load company configuration from AM_GB_COMPANY",
					ex
			);
		}

		return company;
	}
	private String defaultString(String value) {
		return (value == null) ? "" : value;
	}
	private int parseSafeInt(String value, int defaultValue) {
		try {
			return (value == null || "null".equalsIgnoreCase(value))
					? defaultValue
					: Integer.parseInt(value);
		} catch (NumberFormatException ex) {
			return defaultValue;
		}
	}

	private double getNullableDouble(ResultSet rs, String column) throws SQLException {
		double value = rs.getDouble(column);
		return rs.wasNull() ? 0.0 : value;
	}
	public AssetManagerInfo getAssetManagerInfo() {

		AssetManagerInfo assetManagerInfo = null;

		final String sql =
				"SELECT Processing_Date, Processing_Frequency, Next_Processing_Date, Default_Branch, " +
						"       Branch_Name, Suspense_Acct, Auto_Generate_Id, Residual_Value, " +
						"       Depreciation_Method, Vat_Account, Wht_Account, " +
						"       PL_Disposal_Account, PLD_Status, Vat_Acct_Status, Wht_Acct_Status, " +
						"       Suspense_Ac_Status, Sbu_Required, Sbu_Level, system_date, asset_acq_ac " +
						"FROM AM_GB_COMPANY";

		try (Connection connection = getConnection();
			 PreparedStatement statement = connection.prepareStatement(
					 sql,
					 ResultSet.TYPE_FORWARD_ONLY,
					 ResultSet.CONCUR_READ_ONLY)) {

			statement.setQueryTimeout(30);

			try (ResultSet resultSet = statement.executeQuery()) {

				if (resultSet.next()) {

					assetManagerInfo = new AssetManagerInfo(
							formatDate(resultSet.getDate("Processing_Date")),
							resultSet.getString("Processing_Frequency"),
							formatDate(resultSet.getDate("Next_Processing_Date")),
							resultSet.getString("Default_Branch"),
							resultSet.getString("Branch_Name"),
							resultSet.getString("Suspense_Acct"),
							resultSet.getString("Auto_Generate_Id"),
							resultSet.getString("Residual_Value"),
							resultSet.getString("Depreciation_Method"),
							resultSet.getString("Vat_Account"),
							resultSet.getString("Wht_Account"),
							resultSet.getString("PL_Disposal_Account"),
							resultSet.getString("PLD_Status"),
							resultSet.getString("Vat_Acct_Status"),
							resultSet.getString("Wht_Acct_Status"),
							resultSet.getString("Suspense_Ac_Status"),
							resultSet.getString("Sbu_Required"),
							resultSet.getString("Sbu_Level")
					);

					assetManagerInfo.setSysDate(
							formatDate(resultSet.getDate("system_date"))
					);

					assetManagerInfo.setAssetSuspenseAcct(
							resultSet.getString("asset_acq_ac")
					);
				}
			}

		} catch (Exception ex) {
			throw new RuntimeException(
					"Failed to retrieve Asset Manager configuration from AM_GB_COMPANY",
					ex
			);
		}

		return assetManagerInfo;
	}

	public legend.admin.objects.Company getAllCompanyField(String tempId) {

		legend.admin.objects.Company company = null;

		String query =
				"SELECT Company_Code, Company_Name, Acronym, Company_Address" +
						", Vat_Rate, Wht_Rate, Financial_Start_Date, Financial_No_OfMonths" +
						", Financial_End_Date, Minimum_Password, Password_Expiry, Session_Timeout" +
						", Enforce_Acq_Budget, Enforce_Pm_Budget, Enforce_Fuel_Allocation, Require_Quarterly_Pm" +
						", Quarterly_Surplus_Cf, User_Id, Processing_Status, Trans_Wait_Time, loguseraudit" +
						", Fed_Wht_Rate, Attempt_Logon, component_delimiter, password_upper, password_lower, password_numeric" +
						", Processing_Date, Processing_Frequency, Next_Processing_Date, Default_Branch" +
						", Branch_Name, Suspense_Acct, Auto_Generate_Id, Residual_Value" +
						", Depreciation_Method, Vat_Account, Wht_Account" +
						", PL_Disposal_Account, PLD_Status, Vat_Acct_Status, Wht_Acct_Status" +
						", Suspense_Ac_Status, Sbu_Required, Sbu_Level, system_date, asset_acq_ac, Proof_Session_timeout" +
						", password_special, password_limit, defer_account, Asset_acq_status, Asset_defer_status, Fed_Wht_Account" +
						", part_pay_acct, part_pay_status, raise_entry, THIRDPARTY_REQUIRE, Fed_wht_acct_status" +
						", loss_disposal_account, lossDisposal_act_status" +
						", group_asset_account, group_asset_act_status, selfChargeVAT, selfCharge_Vat_status" +
						", databaseName, RECORD_TYPE, Cost_Threshold, Trans_Threshold" +
						" FROM AM_GB_COMPANYTEMP WHERE TMPID = ?";

		try (
				Connection con = getConnection();
				PreparedStatement ps = con.prepareStatement(query)
		) {

			ps.setQueryTimeout(30);
			ps.setString(1, tempId);

			try (ResultSet rs = ps.executeQuery()) {

				if (rs.next()) {

					// ---- Null-safe date formatting ----
					String financialStartDate = formatDate(rs.getDate("Financial_Start_Date"));
					String financialEndDate   = formatDate(rs.getDate("Financial_End_Date"));
					String processingDate     = formatDate(rs.getDate("Processing_Date"));
					String nextProcessingDate = formatDate(rs.getDate("Next_Processing_Date"));
					String sysDate            = formatDate(rs.getDate("system_date"));

					String compDelimiter = rs.getString("component_delimiter");
					if (compDelimiter == null) compDelimiter = "";

					company = new legend.admin.objects.Company(
							rs.getString("Company_Code"),
							rs.getString("Company_Name"),
							rs.getString("Acronym"),
							rs.getString("Company_Address"),
							rs.getDouble("Vat_Rate"),
							rs.getDouble("Wht_Rate"),
							rs.getDouble("Fed_Wht_Rate"),
							financialStartDate,
							rs.getInt("Financial_No_OfMonths"),
							financialEndDate,
							rs.getInt("Minimum_Password"),
							rs.getInt("Password_Expiry"),
							rs.getInt("Session_Timeout"),
							rs.getString("Enforce_Acq_Budget"),
							rs.getString("Enforce_Pm_Budget"),
							rs.getString("Enforce_Fuel_Allocation"),
							rs.getString("Require_Quarterly_Pm"),
							rs.getString("Quarterly_Surplus_Cf"),
							rs.getString("User_Id"),
							rs.getString("Processing_Status"),
							rs.getDouble("Trans_Wait_Time"),
							rs.getInt("Attempt_Logon")
					);

					// ---- Set remaining fields (unchanged logic) ----
					company.setLogUserAudit(rs.getString("loguseraudit"));
					company.setComp_delimiter(compDelimiter);
					company.setPassword_lower(rs.getString("password_lower"));
					company.setPassword_numeric(rs.getString("password_numeric"));
					company.setPassword_special(rs.getString("password_special"));
					company.setPassword_upper(rs.getString("password_upper"));
					company.setPasswordLimit(rs.getInt("password_limit"));

					company.setProcessingDate(processingDate);
					company.setProcessingFrequency(rs.getString("Processing_Frequency"));
					company.setNextProcessingDate(nextProcessingDate);
					company.setDefaultBranch(rs.getString("Default_Branch"));
					company.setBranchName(rs.getString("Branch_Name"));
					company.setAssetSuspenseAcct(rs.getString("asset_acq_ac"));
					company.setAutoGenId(rs.getString("Auto_Generate_Id"));
					company.setResidualValue(rs.getString("Residual_Value"));
					company.setDepreciationMethod(rs.getString("Depreciation_Method"));
					company.setVatAccount(rs.getString("Vat_Account"));
					company.setWhtAccount(rs.getString("Wht_Account"));
					company.setPLDisposalAccount(rs.getString("PL_Disposal_Account"));
					company.setPLDStatus(rs.getString("PLD_Status"));
					company.setVatAcctStatus(rs.getString("Vat_Acct_Status"));
					company.setWhtAcctStatus(rs.getString("Wht_Acct_Status"));
					company.setSuspenseAcctStatus(rs.getString("Suspense_Ac_Status"));
					company.setSbuRequired(rs.getString("Sbu_Required"));
					company.setSbuLevel(rs.getString("Sbu_Level"));
					company.setDeferAccount(rs.getString("defer_account"));
					company.setSuspenseAcct(rs.getString("Suspense_Acct"));
					company.setAsset_acq_status(rs.getString("Asset_acq_status"));
					company.setAsset_defer_status(rs.getString("Asset_defer_status"));
					company.setFedWhtAccount(rs.getString("Fed_Wht_Account"));
					company.setPart_pay(rs.getString("part_pay_acct"));
					company.setPart_pay_status(rs.getString("part_pay_status"));
					company.setRaiseEntry(rs.getString("raise_entry"));
					company.setThirdpartytransaction(rs.getString("THIRDPARTY_REQUIRE"));
					company.setSysDate(sysDate);
					company.setRecordType(rs.getString("RECORD_TYPE"));
					company.setLossDisposalAcct(rs.getString("loss_disposal_account"));
					company.setLDAcctStatus(rs.getString("lossDisposal_act_status"));
					company.setGroupAssetAcct(rs.getString("group_asset_account"));
					company.setGAAStatus(rs.getString("group_asset_act_status"));
					company.setSelfChargeAcct(rs.getString("selfChargeVAT"));
					company.setSelfChargeStatus(rs.getString("selfCharge_Vat_status"));
					company.setFedWhtAcctStatus(rs.getString("Fed_wht_acct_status"));
					company.setProofSessionTimeout(rs.getInt("Proof_Session_timeout"));
					company.setDatabaseName(rs.getString("databaseName"));
					company.setCp_threshold(rs.getDouble("Cost_Threshold"));
					company.setTrans_threshold(rs.getDouble("Trans_Threshold"));
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error executing getAllCompanyField()", e);
		}

		return company;
	}


//	private Connection getConnection() {
//		Connection con = null;
//		try {
////        	if(con==null){
//                Context initContext = new InitialContext();
//                String dsJndi = "java:/legendPlus";
//                DataSource ds = (DataSource) initContext.lookup(
//                		dsJndi);
//                con = ds.getConnection();
////        	}
//		} catch (Exception e) {
//			System.out.println("WARNING: Error 1 getting connection ->"
//					+ e.getMessage());
//			e.printStackTrace();
//		}
//		//finally {
////			closeConnection(con);
////		}
//		return con;
//	}

private Connection getConnection() throws NamingException, SQLException {
    Context ctx = new InitialContext();
    DataSource ds = (DataSource) ctx.lookup("java:/legendPlus");
    return ds.getConnection();
}



	private Connection getFinacleConnection() {
		Connection con = null;
//		dc = new DataConnect("legendPlus");
		try {
/*			if(this.con==null){
				dc = new DataConnect("legendPlus");
				con = dc.getConnection();
				}*/
        	if(this.con==null){
                Context initContext = new InitialContext();
                String dsJndi = "java:/FinacleDataHouse";
                DataSource ds = (DataSource) initContext.lookup(
                		dsJndi);
                con = ds.getConnection();
        	}
		} catch (Exception e) {
			System.out.println("WARNING: Error 1 getting connection ->"
					+ e.getMessage());
			e.printStackTrace();
		}  //finally {
//			closeConnection(con);
//		}
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
			System.out.println("WARNING: Error 2 getting connection ->"
					+ e.getMessage());
					e.printStackTrace();
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
			System.out.println("Connection Closed.... ->");
		} catch (Exception e) {
			System.out.println("WARNING: Error 3 closing connection ->"
					+ e.getMessage());
			e.printStackTrace();
		}

	}

	private void closeConnection(Connection con) {
		try {
			if (con != null) {
				con.close();
			}
		} catch (Exception e) {
			System.out.println("WARNING: Error 4 closing connection ->"
					+ e.getMessage());
			e.printStackTrace();
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
			e.printStackTrace();
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
			System.out.println("WARNING: Error closing connection detail ->"
					+ e.getMessage());
			e.printStackTrace();
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
					e.printStackTrace();
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
					e.printStackTrace();
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}


	/**
	 * createCurrency
	 */
	public boolean createDefaultsCompany(legend.admin.objects.Company company) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "INSERT INTO AM_GB_COMPANY(Company_Code, Company_Name, Acronym, Company_Address"
				+ "Minimum_Password, Password_Expiry, Session_Timeout, Enforce_Acq_Budget, Enforce_Pm_Budget"
				+ ", Enforce_Fuel_Allocation, Require_Quarterly_Pm,Quarterly_Surplus_Cf,User_Id,loguseraudit,"
				+ "Attempt_Logon,component_delimiter,password_limit,Proof_Session_timeout,THIRDPARTY_REQUIRE,raise_entry,"
				+ "system_date,databaseName,sbu_required,next_processing_date,processing_date,Processing_frequency)"
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, company.getCompanyCode());
			ps.setString(2, company.getCompanyName());
			ps.setString(3, company.getAcronym());
			ps.setString(4, company.getCompanyAddress());
//			ps.setDouble(5, company.getVatRate());
//			ps.setDouble(6, company.getWhtRate());
//			ps.setDate(7, dateConvert(company.getFinancialStartDate()));
//			ps.setInt(8, company.getFinancialNoOfMonths());
//			ps.setDate(9, dateConvert(company.getFinancialEndDate()));
			ps.setInt(5, company.getMinimumPassword());
			ps.setInt(6, company.getPasswordExpiry());
			ps.setInt(7, company.getSessionTimeout());
			ps.setString(8, company.getEnforceAcqBudget());
			ps.setString(9, company.getEnforcePmBudget());
			ps.setString(10, company.getEnforceFuelAllocation());
			ps.setString(11, company.getRequireQuarterlyPM());
			ps.setString(12, company.getQuarterlySurplusCf());
			ps.setString(13, company.getUserId());
			ps.setString(14, company.getLogUserAudit());
			ps.setInt(15,company.getLog_on());
			ps.setString(16,company.getComp_delimiter());
            ps.setInt(17, company.getPasswordLimit());

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


//	/**
//	 * createCurrency
//	 */
//	public String createCompanyTmp(legend.admin.objects.Company company) {
//
//		Connection con = null;
//		PreparedStatement ps = null;
//		String result = "";
//		boolean done = false;
//		String query = "INSERT INTO AM_GB_COMPANYTEMP(Company_Code, Company_Name, Acronym, Company_Address"
//				+ ", Vat_Rate, Wht_Rate,Financial_Start_Date, Financial_No_OfMonths"
//				+ ", Financial_End_Date,Minimum_Password"
//				+ ", Password_Expiry, Session_Timeout, Enforce_Acq_Budget, Enforce_Pm_Budget"
//				+ ", Enforce_Fuel_Allocation, Require_Quarterly_Pm,Quarterly_Surplus_Cf,User_Id,"
//				+ "  loguseraudit,Attempt_Logon,component_delimiter,password_limit,TMP_CREATE_DATE,RECORD_TYPE,TMPID,PROCESSING_DATE,"
//				+ "  password_upper,password_lower,password_numeric,password_special,Proof_Session_timeout)"
//				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//
//		try {
//			String tmpId = (new ApplicationHelper()).getGeneratedId("am_gb_companytemp");
//			tmpId = "CP"+tmpId;
//			con = getConnection();
//			ps = con.prepareStatement(query);
//			ps.setString(1, company.getCompanyCode());
//			ps.setString(2, company.getCompanyName());
//			ps.setString(3, company.getAcronym());
//			ps.setString(4, company.getCompanyAddress());
//			ps.setDouble(5, company.getVatRate());
//			ps.setDouble(6, company.getWhtRate());
//			ps.setDate(7, dateConvert(company.getFinancialStartDate()));
//			ps.setInt(8, company.getFinancialNoOfMonths());
//			ps.setDate(9, dateConvert(company.getFinancialEndDate()));
//			ps.setInt(10, company.getMinimumPassword());
//			ps.setInt(11, company.getPasswordExpiry());
//			ps.setInt(12, company.getSessionTimeout());
//			ps.setString(13, company.getEnforceAcqBudget());
//			ps.setString(14, company.getEnforcePmBudget());
//			ps.setString(15, company.getEnforceFuelAllocation());
//			ps.setString(16, company.getRequireQuarterlyPM());
//			ps.setString(17, company.getQuarterlySurplusCf());
//			ps.setString(18, company.getUserId());
//			ps.setString(19, company.getLogUserAudit());
//			ps.setInt(20,company.getLog_on());
//			ps.setString(21,company.getComp_delimiter());
//            ps.setInt(22, company.getPasswordLimit());
//            ps.setDate(23, df.dateConvert(new java.util.Date()));
//            ps.setString(24, "A");
//            ps.setString(25, tmpId);
//            ps.setDate(26,dateConvert(company.getProcessingDate()));
//            ps.setString(27, company.getPassword_upper());
//            ps.setString(28, company.getPassword_lower());
//            ps.setString(29, company.getPassword_numeric());
//            ps.setString(30, company.getPassword_special());
//            ps.setInt(31, company.getProofSessionTimeout());
//
//
//			done = (ps.executeUpdate() != -1);
//	//		System.out.println("=========================checking if success"+done);
//			result = tmpId;
//		} catch (Exception e) {
//			System.out.println("WARNING:Error executing Query ->"
//					+ e.getMessage());
//		} finally {
//			closeConnection(con, ps);
//		}
//		return result;
//
//	}

	/**
	 * createCurrency
	 */
	public String createCompanyTmp(legend.admin.objects.Company company) {

		Connection con = null;
		PreparedStatement ps = null;
		String result = "";
		boolean done = false;
		String query = "INSERT INTO AM_GB_COMPANYTEMP(Company_Code, Company_Name, Acronym, Company_Address"
				+ ", Minimum_Password"
				+ ", Password_Expiry, Session_Timeout, Enforce_Acq_Budget, Enforce_Pm_Budget"
				+ ", Enforce_Fuel_Allocation, Require_Quarterly_Pm,Quarterly_Surplus_Cf,User_Id,"
				+ "  loguseraudit,Attempt_Logon,password_limit,TMP_CREATE_DATE,RECORD_TYPE,TMPID,PROCESSING_DATE,"
				+ "  password_upper,password_lower,password_numeric,password_special,Proof_Session_timeout,Processing_Frequency,"
				+ " Next_Processing_Date,THIRDPARTY_REQUIRE,RAISE_ENTRY,system_date,databaseName,Sbu_Required,Sbu_Level )"
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		try {
			String tmpId = (new ApplicationHelper()).getGeneratedId("am_gb_companytemp");
			tmpId = "CP"+tmpId;
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, company.getCompanyCode());
			ps.setString(2, company.getCompanyName());
			ps.setString(3, company.getAcronym());
			ps.setString(4, company.getCompanyAddress());
//			ps.setDouble(5, company.getVatRate());
//			ps.setDouble(6, company.getWhtRate());
//			System.out.println("=====company.getFinancialStartDate(): "+company.getFinancialStartDate());
//			ps.setDate(5, dateConvert(company.getFinancialStartDate()));
//			ps.setInt(6, company.getFinancialNoOfMonths());
//			System.out.println("=====company.getFinancialEndDate(): "+company.getFinancialEndDate());
//			ps.setDate(7, dateConvert(company.getFinancialEndDate()));
			ps.setInt(5, company.getMinimumPassword());
			ps.setInt(6, company.getPasswordExpiry());
			ps.setInt(7, company.getSessionTimeout());
			ps.setString(8, company.getEnforceAcqBudget());
			ps.setString(9, company.getEnforcePmBudget());
			ps.setString(10, company.getEnforceFuelAllocation());
			ps.setString(11, company.getRequireQuarterlyPM());
			ps.setString(12, company.getQuarterlySurplusCf());
			ps.setString(13, company.getUserId());
			ps.setString(14, company.getLogUserAudit());
			ps.setInt(15,company.getLog_on());
//			ps.setString(21,company.getComp_delimiter());
            ps.setInt(16, company.getPasswordLimit());
            ps.setDate(17, df.dateConvert(new java.util.Date()));
            ps.setString(18, "A");
            ps.setString(19, tmpId);
            System.out.println("=====company.getProcessingDate(): "+company.getProcessingDate()+"   dateConvert(company.getProcessingDate()): "+dateConvert(company.getProcessingDate()));
            ps.setDate(20,dateConvert(company.getProcessingDate()));
            ps.setString(21, company.getPassword_upper());
            ps.setString(22, company.getPassword_lower());
            ps.setString(23, company.getPassword_numeric());
            ps.setString(24, company.getPassword_special());
            ps.setInt(25, company.getProofSessionTimeout());
//    		ps.setDate(28, dateConvert(company.getProcessingDate()));
    		ps.setString(26, company.getProcessingFrequency());
    		System.out.println("=====company.getNextProcessingDate(): "+company.getNextProcessingDate());
    		ps.setDate(27, dateConvert(company.getNextProcessingDate()));
            ps.setString(28, company.getThirdpartytransaction());
            ps.setString(29, company.getRaiseEntry());
            ps.setDate(30, dateConvert(company.getSysDate()));
            ps.setString(31, company.getDatabaseName());
    		ps.setString(32, company.getSbuRequired());
    		ps.setString(33, company.getSbuLevel());

			done = (ps.executeUpdate() != -1);
	//		System.out.println("=========================checking if success"+done);
			result = tmpId;
		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return result;

	}

	public boolean updateCompany(legend.admin.objects.Company company) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "UPDATE AM_GB_COMPANY "
				+ " SET Company_Name = ?, Acronym = ?, Company_Address = ?,Vat_Rate = ?, Wht_Rate = ?"
				+ ", Financial_Start_Date = ?, Financial_No_OfMonths = ?"
				+ ", Financial_End_Date = ?, Minimum_Password = ?"
				+ ", Password_Expiry = ?, Session_Timeout = ?, Enforce_Acq_Budget = ?, Enforce_Pm_Budget = ?"
				+ ", Enforce_Fuel_Allocation = ?, Require_Quarterly_Pm = ?"
				+ ", Quarterly_Surplus_Cf = ?,loguseraudit=?,Attempt_Logon=?,component_delimiter=?,"
                + " password_upper=?,password_lower=?,password_numeric=?,password_special=?,password_limit=?,Proof_Session_timeout = ?  WHERE Company_Code=? ";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
//			ps = con.prepareStatement(query);

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
            ps.setInt(25, company.getProofSessionTimeout());
            ps.setString(26, company.getCompanyCode());
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
                 +"asset_acq_status=?, asset_defer_status=?, part_pay_status=?,THIRDPARTY_REQUIRE=?,RAISE_ENTRY=?";


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
            ps.setString(32, ami.getRaiseEntry());
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
			System.out.println("WARNING:Error executing Query in updateAssetManagerInfo ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}


	public boolean updateCompanyApprovalOld(legend.admin.objects.Company company) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "UPDATE AM_GB_COMPANY "
				+ " SET Company_Name = ?, Acronym = ?, Company_Address = ?,Vat_Rate = ?, Wht_Rate = ?"
				+ ", Financial_Start_Date = ?, Financial_No_OfMonths = ?"
				+ ", Financial_End_Date = ?, Minimum_Password = ?"
				+ ", Password_Expiry = ?, Session_Timeout = ?, Enforce_Acq_Budget = ?, Enforce_Pm_Budget = ?"
				+ ", Enforce_Fuel_Allocation = ?, Require_Quarterly_Pm = ?"
				+ ", Quarterly_Surplus_Cf = ?,loguseraudit=?,Attempt_Logon=?,component_delimiter=?,"
                + " password_upper=?,password_lower=?,password_numeric=?,password_special=?,password_limit=?,Proof_Session_timeout = ?  WHERE Company_Code=? ";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
//			ps = con.prepareStatement(query);

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
            ps.setInt(25, company.getProofSessionTimeout());
            ps.setString(26, company.getCompanyCode());
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

	public boolean updateCompanyApproval(legend.admin.objects.Company company) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "UPDATE AM_GB_COMPANY "
				+ " SET Company_Name = ?, Acronym = ?, Company_Address = ?,Proof_Session_timeout = ?, THIRDPARTY_REQUIRE = ?"
				+ ", raise_entry = ?, system_date = ?"
				+ ", databaseName = ?, Minimum_Password = ?"
				+ ", Password_Expiry = ?, Session_Timeout = ?, Enforce_Acq_Budget = ?, Enforce_Pm_Budget = ?"
				+ ", Enforce_Fuel_Allocation = ?, Require_Quarterly_Pm = ?"
				+ ", Quarterly_Surplus_Cf = ?,loguseraudit=?,Attempt_Logon=?,component_delimiter=?,"
                + " password_upper=?,password_lower=?,password_numeric=?,password_special=?,password_limit=?, "
				+ "sbu_level=?,sbu_required=?,next_processing_date=?,processing_date=?,Processing_frequency=?  WHERE Company_Code=? ";
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
//			ps = con.prepareStatement(query);

			ps.setString(1, company.getCompanyName());
			ps.setString(2, company.getAcronym());
			ps.setString(3, company.getCompanyAddress());
			ps.setInt(4, company.getProofSessionTimeout());
			ps.setString(5, company.getThirdpartytransaction());
			ps.setString(6, company.getRaiseEntry());
//			System.out.print("update getSysDate...>>>>>>>>>>>"+company.getSysDate());
			ps.setDate(7, dateConvert(company.getSysDate()));
//			System.out.print("update dateConvert(company.getSysDate())...>>>>>>>>>>>"+dateConvert(company.getSysDate()));
//			ps.setString(7, company.getSysDate());
			ps.setString(8, company.getDatabaseName());
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
//        System.out.print("update getComp_delimiter...>>>>>>>>>>>"+company.getComp_delimiter());
            ps.setString(20,company.getPassword_upper());
            ps.setString(21,company.getPassword_lower());
            ps.setString(22,company.getPassword_numeric());
            ps.setString(23,company.getPassword_special());
            ps.setInt(24, company.getPasswordLimit());
            ps.setString(25,company.getSbuLevel());
            ps.setString(26,company.getSbuRequired());
//            System.out.print("update getNextProcessingDate...>>>>>>>>>>>"+company.getNextProcessingDate());
//            ps.setString(26,company.getNextProcessingDate());
            ps.setDate(27, dateConvert(company.getNextProcessingDate()));
//            System.out.print("update getProcessingDate...>>>>>>>>>>>"+company.getProcessingDate());
//            ps.setString(27,company.getProcessingDate());
            ps.setDate(28, dateConvert(company.getProcessingDate()));
//            System.out.print("update getProcessingFrequency...>>>>>>>>>>>"+dateConvert(company.getProcessingDate()));
            ps.setString(29,company.getProcessingFrequency());
//            System.out.print("update getProcessingFrequency...>>>>>>>>>>>"+company.getProcessingFrequency());
            ps.setString(30, company.getCompanyCode());
//        	  System.out.print("update am_gb_company...>>>>>>>>>>>"+company.getCompanyCode());

            done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query in updateCompanyApproval ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}


	public boolean updateAssetManagerInfoApprovalOld(legend.admin.objects.Company company) {

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
                 +"asset_acq_status=?, asset_defer_status=?, part_pay_status=?,THIRDPARTY_REQUIRE=?,RAISE_ENTRY=?,system_date=?,"
 	       		 + " loss_disposal_account=?,lossDisposal_act_status=?, group_asset_account=?,group_asset_act_status=?,selfChargeVAT=?,"
 				 + " selfCharge_Vat_status=?,Proof_Session_timeout=?,databaseName=? WHERE Company_Code=? ";



  //      System.out.println("HERE ##################### " );

       // String query = "update am_gb_company set cost_threshold =?";
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			         //System.out.println("the query is############ " + query);
                     //ps.setDouble(1,ami.getCp_threshold());

			ps.setDate(1, dateConvert(company.getProcessingDate()));
			ps.setString(2, company.getProcessingFrequency());
			ps.setDate(3, dateConvert(company.getNextProcessingDate()));
			ps.setString(4, company.getDefaultBranch());
			ps.setString(5, company.getBranchName());
			ps.setString(6, company.getSuspenseAcct());
			ps.setString(7, company.getAutoGenId());
			ps.setString(8, company.getResidualValue());
			ps.setString(9, company.getDepreciationMethod());
			ps.setString(10, company.getVatAccount());
			ps.setString(11, company.getWhtAccount());
			ps.setString(12, company.getPLDisposalAccount());
			ps.setString(13, company.getPLDStatus());
			ps.setString(14, company.getVatAcctStatus());
			ps.setString(15, company.getWhtAcctStatus());
			ps.setString(16, company.getSuspenseAcctStatus());
			ps.setString(17, company.getSbuRequired());
			ps.setString(18, company.getSbuLevel());
			ps.setString(19, company.getAssetSuspenseAcct());
            ps.setString(20, company.getLpo_r());
            ps.setString(21, company.getBar_code_r());
            ps.setDouble(22, company.getCp_threshold());
            ps.setDouble(23, company.getTrans_threshold());
            ps.setString(24, company.getDeferAccount());
            ps.setString(25, company.getFedWhtAccount());
            ps.setString(26, company.getFedWhtAcctStatus());
            ps.setString(27, company.getPart_pay());
            ps.setString(28, company.getAsset_acq_status());
            ps.setString(29, company.getAsset_defer_status());
            ps.setString(30, company.getPart_pay_status());
            ps.setString(31, company.getThirdpartytransaction());
            ps.setString(32, company.getRaiseEntry());
            ps.setDate(33, dateConvert(company.getSysDate()));
            ps.setString(34, company.getLossDisposalAcct());
            ps.setString(35, company.getLDAcctStatus());
            ps.setString(36, company.getGroupAssetAcct());
            ps.setString(37, company.getGAAStatus());
            ps.setString(38, company.getSelfChargeAcct());
            ps.setString(39, company.getSelfChargeStatus());
            ps.setInt(40, company.getProofSessionTimeout());
            ps.setString(41, company.getDatabaseName());
            ps.setString(42, company.getCompanyCode());
    //        System.out.println("The content of ps is################### "+ ps.toString());
            int psResult = ps.executeUpdate();
	//		System.out.println("The content of ps is################### "+ ps.toString());
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
			System.out.println("WARNING:Error executing Query in updateAssetManagerInfo ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

	public boolean updateAssetManagerInfoApproval(legend.admin.objects.Company company) {

        Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;

        String query = "UPDATE AM_GB_COMPANY "
				+ "SET  Vat_Rate = ?, Wht_Rate = ?,Financial_Start_Date = ?, Financial_No_OfMonths = ?,"
				+ "Financial_End_Date = ?, Default_Branch = ?, "
				+ "Branch_Name = ?,Suspense_Acct = ? ,Auto_Generate_Id = ?, "
				 + "Residual_Value = ?, Depreciation_Method = ?, "
				+ "Vat_Account = ?, Wht_Account = ?,  PL_Disposal_Account = ?, PLD_Status = ?, "
				+ "Vat_Acct_Status = ?, Wht_Acct_Status = ?, "
				 + "Suspense_Ac_Status = ?, asset_acq_ac=?, LPO_Required=?, "
                 + "Barcode_Fld=?, Cost_Threshold=?, Trans_threshold=?, Defer_account=?, "
                 +"Fed_Wht_Account =?, Fed_Wht_Acct_Status = ?,part_pay_acct=?, "
                 +"asset_acq_status=?, asset_defer_status=?, part_pay_status=?,"
 	       		 + " loss_disposal_account=?,lossDisposal_act_status=?, group_asset_account=?,group_asset_act_status=?,selfChargeVAT=?,"
 				 + " selfCharge_Vat_status=? WHERE Company_Code=? ";



//        System.out.println("HERE ##################### " );

       // String query = "update am_gb_company set cost_threshold =?";
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
//			         System.out.println("the query is############===> " + query);
                     //ps.setDouble(1,ami.getCp_threshold());

			ps.setDouble(1, company.getVatRate());
			ps.setDouble(2, company.getWhtRate());
//			System.out.println("outcome OF dateConvert(company.getFinancialStartDate()) is ########## "+ dateConvert(company.getFinancialStartDate()));
			ps.setDate(3, dateConvert(company.getFinancialStartDate()));
			ps.setInt(4, company.getFinancialNoOfMonths());
//			System.out.println("outcome OF dateConvert(company.getFinancialEndDate()) is ########## "+ dateConvert(company.getFinancialEndDate()));
			ps.setDate(5, dateConvert(company.getFinancialEndDate()));
			ps.setString(6, company.getDefaultBranch());
			ps.setString(7, company.getBranchName());
			ps.setString(8, company.getSuspenseAcct());
			ps.setString(9, company.getAutoGenId());
			ps.setString(10, company.getResidualValue());
			ps.setString(11, company.getDepreciationMethod());
			ps.setString(12, company.getVatAccount());
			ps.setString(13, company.getWhtAccount());
			ps.setString(14, company.getPLDisposalAccount());
			ps.setString(15, company.getPLDStatus());
			ps.setString(16, company.getVatAcctStatus());
			ps.setString(17, company.getWhtAcctStatus());
			ps.setString(18, company.getSuspenseAcctStatus());
//			ps.setString(17, company.getSbuRequired());
//			ps.setString(17, company.getSbuLevel());
			ps.setString(19, company.getAssetSuspenseAcct());
            ps.setString(20, company.getLpo_r());
            ps.setString(21, company.getBar_code_r());
            ps.setDouble(22, company.getCp_threshold());
            ps.setDouble(23, company.getTrans_threshold());
            ps.setString(24, company.getDeferAccount());
            ps.setString(25, company.getFedWhtAccount());
            ps.setString(26, company.getFedWhtAcctStatus());
            ps.setString(27, company.getPart_pay());
            ps.setString(28, company.getAsset_acq_status());
            ps.setString(29, company.getAsset_defer_status());
            ps.setString(30, company.getPart_pay_status());
//            ps.setString(31, company.getThirdpartytransaction());
//            ps.setString(32, company.getRaiseEntry());
//            ps.setDate(33, dateConvert(company.getSysDate()));
            ps.setString(31, company.getLossDisposalAcct());
            ps.setString(32, company.getLDAcctStatus());
            ps.setString(33, company.getGroupAssetAcct());
            ps.setString(34, company.getGAAStatus());
            ps.setString(35, company.getSelfChargeAcct());
            ps.setString(36, company.getSelfChargeStatus());
 //           System.out.println("The content of company.getCompanyCode() is################### "+ company.getCompanyCode());
            ps.setString(37, company.getCompanyCode());
//            System.out.println("The content of ps is################### "+ ps.toString());
            int psResult = ps.executeUpdate();
//			System.out.println("The content of psResult is################### "+ ps.toString());
			done = (psResult != -1);
//            System.out.println("outcome of done is ########## "+ done);


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
			System.out.println("WARNING:Error executing Query in updateAssetManagerInfo ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}



	public boolean updateAllCompanyFields(legend.admin.objects.Company company) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "UPDATE AM_GB_COMPANY "
				+ " SET Company_Name = ?, Acronym = ?, Company_Address = ?,Vat_Rate = ?, Wht_Rate = ?"
				+ ", Financial_Start_Date = ?, Financial_No_OfMonths = ?"
				+ ", Financial_End_Date = ?, Minimum_Password = ?"
				+ ", Password_Expiry = ?, Session_Timeout = ?, Enforce_Acq_Budget = ?, Enforce_Pm_Budget = ?"
				+ ", Enforce_Fuel_Allocation = ?, Require_Quarterly_Pm = ?"
				+ ", Quarterly_Surplus_Cf = ?,loguseraudit=?,Attempt_Logon=?,component_delimiter=?,"
                + " password_upper=?,password_lower=?,password_numeric=?,password_special=?,password_limit=?,"
				+ " Processing_Date = ? ,Processing_Frequency = ?,Next_Processing_Date = ?, Default_Branch = ?,"
				+ " Branch_Name = ?,Suspense_Acct = ? ,Auto_Generate_Id = ?,Residual_Value = ?, Depreciation_Method = ?,"
				+ "	Vat_Account = ?, Wht_Account = ?,  PL_Disposal_Account = ?, PLD_Status = ?,Vat_Acct_Status = ?, Wht_Acct_Status = ?,"
				+ "	Suspense_Ac_Status = ?, Sbu_Required = ?,Sbu_Level = ?, asset_acq_ac=?, LPO_Required=?,"
				+ " Barcode_Fld=?, Cost_Threshold=?, Trans_threshold=?, Defer_account=?,Fed_Wht_Account =?, Fed_Wht_Acct_Status = ?,part_pay_acct=?,"
				+ " asset_acq_status=?, asset_defer_status=?, part_pay_status=?,THIRDPARTY_REQUIRE=?,RAISE_ENTRY=?,system_date=?,  "
				+ " loss_disposal_account=?,lossDisposal_act_status=?, group_asset_account=?,group_asset_act_status=?,selfChargeVAT=?,"
				+ " selfCharge_Vat_status=?,databaseName=?,Proof_Session_timeout=?  WHERE Company_Code=? ";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
//			ps = con.prepareStatement(query);
//			System.out.print("update query ...>>>>>>>>>>>"+query);
//			System.out.print("update getCompanyCode 1...>>>>>>>>>>>"+company.getCompanyCode());
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
            ps.setString(20,company.getPassword_upper());
            ps.setString(21,company.getPassword_lower());
            ps.setString(22,company.getPassword_numeric());
            ps.setString(23,company.getPassword_special());
            ps.setInt(24, company.getPasswordLimit());
			ps.setDate(25, dateConvert(company.getProcessingDate()));
			ps.setString(26, company.getProcessingFrequency());
			ps.setDate(27, dateConvert(company.getNextProcessingDate()));
			ps.setString(28, company.getDefaultBranch());
			ps.setString(29, company.getBranchName());
			ps.setString(30, company.getSuspenseAcct());
			ps.setString(31, company.getAutoGenId());
			ps.setString(32, company.getResidualValue());
			ps.setString(33, company.getDepreciationMethod());
			ps.setString(34, company.getVatAccount());
			ps.setString(35, company.getWhtAccount());
			ps.setString(36, company.getPLDisposalAccount());
			ps.setString(37, company.getPLDStatus());
			ps.setString(38, company.getVatAcctStatus());
			ps.setString(39, company.getWhtAcctStatus());
			ps.setString(40, company.getSuspenseAcctStatus());
			ps.setString(41, company.getSbuRequired());
			ps.setString(42, company.getSbuLevel());
			ps.setString(43, company.getAssetSuspenseAcct());
            ps.setString(44, company.getLpo_r());
            ps.setString(45,company.getBar_code_r());
            ps.setDouble(46,company.getCp_threshold());
            ps.setDouble(47, company.getTrans_threshold());
            ps.setString(48, company.getDeferAccount());
            ps.setString(49, company.getFedWhtAccount());
            ps.setString(50, company.getFedWhtAcctStatus());
            ps.setString(51, company.getPart_pay());
            ps.setString(52, company.getAsset_acq_status());
            ps.setString(53, company.getAsset_defer_status());
            ps.setString(54, company.getPart_pay_status());
            ps.setString(55, company.getThirdpartytransaction());
            ps.setString(56, company.getRaiseEntry());
            ps.setDate(57, dateConvert(company.getSysDate()));
            ps.setString(58, company.getLossDisposalAcct());
            ps.setString(59, company.getLDAcctStatus());
            ps.setString(60, company.getGroupAssetAcct());
            ps.setString(61, company.getGAAStatus());
            ps.setString(62, company.getSelfChargeAcct());
            ps.setString(63, company.getSelfChargeStatus());
            ps.setString(64, company.getDatabaseName());
            ps.setInt(65, company.getProofSessionTimeout());
            ps.setString(66, company.getCompanyCode());

            done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query in updateAllCompanyFields ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

	/**
	 * createCurrency
	 */
	public String createCompanyAmiTmp(legend.admin.objects.AssetManagerInfo ami) {

		Connection con = null;
		PreparedStatement ps = null;
		String result = "";
		boolean done = false;
		String query = "INSERT INTO AM_GB_COMPANYTEMP(Default_Branch, "
				+ "Branch_Name,Suspense_Acct,Auto_Generate_Id,Residual_Value,Depreciation_Method, "
				+ "Vat_Account,Wht_Account,PL_Disposal_Account,PLD_Status,Vat_Acct_Status, Wht_Acct_Status,"
				+ "Suspense_Ac_Status, asset_acq_ac, LPO_Required,"
				+ "Barcode_Fld,Cost_Threshold,Trans_threshold,Defer_account,Fed_Wht_Account, Fed_Wht_Acct_Status,part_pay_acct,"
				+ "asset_acq_status,asset_defer_status,part_pay_status,TMP_CREATE_DATE,RECORD_TYPE,"
				+ "loss_disposal_account,lossDisposal_act_status, group_asset_account,group_asset_act_status,selfChargeVAT,selfCharge_Vat_status,"
				+ "Vat_Rate, Wht_Rate,Financial_Start_Date, Financial_No_OfMonths,Financial_End_Date,component_delimiter, TMPID)"
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		try {
			String tmpId = (new ApplicationHelper()).getGeneratedId("am_gb_companytemp");
			tmpId = "CP"+tmpId;
//			System.out.println("=====tmpId in createCompanyAmiTmp: "+tmpId);
			con = getConnection();
			ps = con.prepareStatement(query);
//			ps.setDate(1, dateConvert(ami.getProcessingDate()));
//			ps.setString(2, ami.getProcessingFrequency());
//			ps.setDate(3, dateConvert(ami.getNextProcessingDate()));
//			System.out.println("=====getNextProcessingDate in createCompanyAmiTmp: "+dateConvert(ami.getNextProcessingDate()));
			ps.setString(1, ami.getDefaultBranch());
			ps.setString(2, ami.getBranchName());
			ps.setString(3, ami.getSuspenseAcct());
			ps.setString(4, ami.getAutoGenId());
			ps.setString(5, ami.getResidualValue());
			ps.setString(6, ami.getDepreciationMethod());
			ps.setString(7, ami.getVatAccount());
			ps.setString(8, ami.getWhtAccount());
			ps.setString(9, ami.getPLDisposalAccount());
			ps.setString(10, ami.getPLDStatus());
			ps.setString(11, ami.getVatAcctStatus());
			ps.setString(12, ami.getWhtAcctStatus());
			ps.setString(13, ami.getSuspenseAcctStatus());
//			ps.setString(14, ami.getSbuRequired());
//			ps.setString(15, ami.getSbuLevel());
			ps.setString(14, ami.getAssetSuspenseAcct());
            ps.setString(15, ami.getLpo_r());
            ps.setString(16, ami.getBar_code_r());
            ps.setDouble(17, ami.getCp_threshold());
            ps.setDouble(18, ami.getTrans_threshold());
            ps.setString(19, ami.getDeferAccount());
            ps.setString(20, ami.getFedWhtAccount());
            ps.setString(21, ami.getFedWhtAcctStatus());
            ps.setString(22, ami.getPart_pay());
            ps.setString(23, ami.getAsset_acq_status());
            ps.setString(24, ami.getAsset_defer_status());
            ps.setString(25, ami.getPart_pay_status());
//            ps.setString(31, ami.getThirdpartytransaction());
//            ps.setString(32, ami.getRaiseEntry());
            ps.setDate(26, df.dateConvert(new java.util.Date()));
            ps.setString(27, "B");
//            ps.setDate(35, dateConvert(ami.getSysDate()));
            ps.setString(28, ami.getLossDisposalAcct());
            ps.setString(29, ami.getLDAcctStatus());
            ps.setString(30, ami.getGroupAssetAcct());
            ps.setString(31, ami.getGAAStatus());
            ps.setString(32, ami.getSelfChargeAcct());
            ps.setString(33, ami.getSelfChargeStatus());
//            ps.setString(42, ami.getDatabaseName());
//            System.out.println("=====ami.getVatRate() in createCompanyAmiTmp: "+ami.getVatRate());
			ps.setDouble(34, ami.getVatRate());
//			System.out.println("=====ami.getWhtRate() in createCompanyAmiTmp: "+ami.getWhtRate());
			ps.setDouble(35, ami.getWhtRate());
//			System.out.println("=====dateConvert(ami.getFinancialStartDate()) in createCompanyAmiTmp: "+dateConvert(ami.getFinancialStartDate()));
			ps.setDate(36, dateConvert(ami.getFinancialStartDate()));
//			System.out.println("=====ami.getFinancialNoOfMonths() in createCompanyAmiTmp: "+ami.getFinancialNoOfMonths());
			ps.setInt(37, ami.getFinancialNoOfMonths());
//			System.out.println("=====dateConvert(ami.getFinancialEndDate()) in createCompanyAmiTmp: "+dateConvert(ami.getFinancialEndDate()));
			ps.setDate(38, dateConvert(ami.getFinancialEndDate()));
			System.out.println("=====ami.getComp_delimiter() Length in createCompanyAmiTmp: "+ami.getComp_delimiter().length());
			ps.setString(39, ami.getComp_delimiter());
//			System.out.println("=====tmpId in createCompanyAmiTmp: "+tmpId);
            ps.setString(40, tmpId);
			done = (ps.executeUpdate() != -1);
//			System.out.println("=========================checking if success"+done+"     tmpId:"+tmpId);
			result = tmpId;
		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query in createCompanyAmiTmp ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		System.out.println("=====result in createCompanyAmiTmp: "+result);
		return result;

	}
//
//	public boolean updateAssetManagerInfoTmp(legend.admin.objects.AssetManagerInfo ami,String recId) {
//
//        Connection con = null;
//		PreparedStatement ps = null;
//		boolean done = false;
//
//        String query = "UPDATE AM_GB_COMPANYTEMP "
//				+ "SET  Processing_Date = ? ,Processing_Frequency = ?, "
//				 + "Next_Processing_Date = ?, Default_Branch = ?, "
//				+ "Branch_Name = ?,Suspense_Acct = ? ,Auto_Generate_Id = ?, "
//				 + "Residual_Value = ?, Depreciation_Method = ?, "
//				+ "Vat_Account = ?, Wht_Account = ?,  PL_Disposal_Account = ?, PLD_Status = ?, "
//				+ "Vat_Acct_Status = ?, Wht_Acct_Status = ?, "
//				 + "Suspense_Ac_Status = ?, Sbu_Required = ?, "
//				 + "Sbu_Level = ?, asset_acq_ac=?, LPO_Required=?, "
//                 + "Barcode_Fld=?, Cost_Threshold=?, Trans_threshold=?, Defer_account=?, "
//                 +"Fed_Wht_Account =?, Fed_Wht_Acct_Status = ?,part_pay_acct=?, "
//                 +"asset_acq_status=?, asset_defer_status=?, part_pay_status=?,THIRDPARTY_REQUIRE=?,RAISE_ENTRY=?,RECORD_TYPE=?,system_date=?, "
//                 +"loss_disposal_account=?,lossDisposal_act_status=?, group_asset_account=?,group_asset_act_status=?,selfChargeVAT=?,"
//                 + "selfCharge_Vat_status=?,databaseName=?  where TMPID = ?";
//
//
//  //      System.out.println("HERE ##################### " );
//
//       // String query = "update am_gb_company set cost_threshold =?";
//		try {
//			con = getConnection();
//			ps = con.prepareStatement(query);
//			         //System.out.println("the query is############ " + query);
//                     //ps.setDouble(1,ami.getCp_threshold());
//
//			ps.setDate(1, dateConvert(ami.getProcessingDate()));
//			ps.setString(2, ami.getProcessingFrequency());
//			ps.setDate(3, dateConvert(ami.getNextProcessingDate()));
//			ps.setString(4, ami.getDefaultBranch());
//			ps.setString(5, ami.getBranchName());
//			ps.setString(6, ami.getSuspenseAcct());
//			ps.setString(7, ami.getAutoGenId());
//			ps.setString(8, ami.getResidualValue());
//			ps.setString(9, ami.getDepreciationMethod());
//			ps.setString(10, ami.getVatAccount());
//			ps.setString(11, ami.getWhtAccount());
//			ps.setString(12, ami.getPLDisposalAccount());
//			ps.setString(13, ami.getPLDStatus());
//			ps.setString(14, ami.getVatAcctStatus());
//			ps.setString(15, ami.getWhtAcctStatus());
//			ps.setString(16, ami.getSuspenseAcctStatus());
//			ps.setString(17, ami.getSbuRequired());
//			ps.setString(18, ami.getSbuLevel());
//			ps.setString(19, ami.getAssetSuspenseAcct());
//            ps.setString(20, ami.getLpo_r());
//            ps.setString(21,ami.getBar_code_r());
//            ps.setDouble(22,ami.getCp_threshold());
//            ps.setDouble(23, ami.getTrans_threshold());
//            ps.setString(24, ami.getDeferAccount());
//            ps.setString(25, ami.getFedWhtAccount());
//            ps.setString(26, ami.getFedWhtAcctStatus());
//            ps.setString(27, ami.getPart_pay());
//            ps.setString(28, ami.getAsset_acq_status());
//            ps.setString(29, ami.getAsset_defer_status());
//            ps.setString(30, ami.getPart_pay_status());
//            ps.setString(31, ami.getThirdpartytransaction());
//            ps.setString(32, ami.getRaiseEntry());
//            ps.setString(33, "I");
////            System.out.println("The content of getSysDate is####### "+ ami.getSysDate());
//            ps.setDate(34, dateConvert(ami.getSysDate()));
//            ps.setString(35, ami.getLossDisposalAcct());
//            ps.setString(36, ami.getLDAcctStatus());
//            ps.setString(37, ami.getGroupAssetAcct());
//            ps.setString(38, ami.getGAAStatus());
//            ps.setString(39, ami.getSelfChargeAcct());
//            ps.setString(40, ami.getSelfChargeStatus());
//            ps.setString(41, ami.getDatabaseName());
//            ps.setString(42, recId);
//    //        System.out.println("The content of ps is################### "+ ps.toString());
//            int psResult = ps.executeUpdate();
//	//		System.out.println("The content of ps is################### "+ ps.toString());
//			done = (psResult != -1);
//  //          System.out.println("outcome of update psResult is ########## "+ psResult);
//
//
//		}
//        catch(SQLException ex) {//start catch block
//					System.out.println("\n--- SQLException caught in updateAssetManagerInfoTmp METHOD of COMPANY HANDLER CLASS---\n");
//					while (ex != null) {//start while block
//								System.out.println("Message:   "
//														+ ex.getMessage ());
//								System.out.println("SQLState:  "
//														+ ex.getSQLState ());
//								System.out.println("ErrorCode: "
//														+ ex.getErrorCode ());
//								ex = ex.getNextException();
//								System.out.println("");
//							}//end while block
//     			}//end catch block
//
//        catch (Exception e) {
//
//            System.out.println("#######ERROR OCCURED IN updateAssetManagerInfo METHOD OF COMPANY HANDLER CLASS");
//			System.out.println("WARNING:Error executing Query ->"
//					+ e.getMessage());
//		} finally {
//			closeConnection(con, ps);
//		}
//		return done;
//
//	}
//
public boolean updateAssetManagerInfoTmp(legend.admin.objects.AssetManagerInfo ami,String recId) {

    Connection con = null;
	PreparedStatement ps = null;
	boolean done = false;

    String query = "UPDATE AM_GB_COMPANYTEMP "
			+ "SET  Default_Branch = ?, "
			+ "Branch_Name = ?,Suspense_Acct = ? ,Auto_Generate_Id = ?, "
			 + "Residual_Value = ?, Depreciation_Method = ?, "
			+ "Vat_Account = ?, Wht_Account = ?,  PL_Disposal_Account = ?, PLD_Status = ?, "
			+ "Vat_Acct_Status = ?, Wht_Acct_Status = ?, "
			 + "Suspense_Ac_Status = ?, "
			 + "Sbu_Level = ?, asset_acq_ac=?, LPO_Required=?, "
             + "Barcode_Fld=?, Cost_Threshold=?, Trans_threshold=?, Defer_account=?, "
             +"Fed_Wht_Account =?, Fed_Wht_Acct_Status = ?,part_pay_acct=?, "
             +"asset_acq_status=?, asset_defer_status=?, part_pay_status=?,RECORD_TYPE=?, "
             +"loss_disposal_account=?,lossDisposal_act_status=?, group_asset_account=?,group_asset_act_status=?,selfChargeVAT=?,"
             + "selfCharge_Vat_status=?  where TMPID = ?";


//      System.out.println("HERE ##################### " );

   // String query = "update am_gb_company set cost_threshold =?";
	try {
		con = getConnection();
		ps = con.prepareStatement(query);
		         //System.out.println("the query is############ " + query);
                 //ps.setDouble(1,ami.getCp_threshold());

//		ps.setDate(1, dateConvert(ami.getProcessingDate()));
//		ps.setString(2, ami.getProcessingFrequency());
//		ps.setDate(3, dateConvert(ami.getNextProcessingDate()));
		ps.setString(1, ami.getDefaultBranch());
		ps.setString(2, ami.getBranchName());
		ps.setString(3, ami.getSuspenseAcct());
		ps.setString(4, ami.getAutoGenId());
		ps.setString(5, ami.getResidualValue());
		ps.setString(6, ami.getDepreciationMethod());
		ps.setString(7, ami.getVatAccount());
		ps.setString(8, ami.getWhtAccount());
		ps.setString(9, ami.getPLDisposalAccount());
		ps.setString(10, ami.getPLDStatus());
		ps.setString(11, ami.getVatAcctStatus());
		ps.setString(12, ami.getWhtAcctStatus());
		ps.setString(13, ami.getSuspenseAcctStatus());
//		ps.setString(14, ami.getSbuRequired());
		ps.setString(15, ami.getSbuLevel());
		ps.setString(16, ami.getAssetSuspenseAcct());
        ps.setString(17, ami.getLpo_r());
        ps.setString(18,ami.getBar_code_r());
        ps.setDouble(19,ami.getCp_threshold());
        ps.setDouble(20, ami.getTrans_threshold());
        ps.setString(21, ami.getDeferAccount());
        ps.setString(22, ami.getFedWhtAccount());
        ps.setString(23, ami.getFedWhtAcctStatus());
        ps.setString(24, ami.getPart_pay());
        ps.setString(25, ami.getAsset_acq_status());
        ps.setString(26, ami.getAsset_defer_status());
        ps.setString(27, ami.getPart_pay_status());
//        ps.setString(31, ami.getThirdpartytransaction());
//        ps.setString(32, ami.getRaiseEntry());
        ps.setString(28, "I");
//        System.out.println("The content of getSysDate is####### "+ ami.getSysDate());
//        ps.setDate(34, dateConvert(ami.getSysDate()));
        ps.setString(29, ami.getLossDisposalAcct());
        ps.setString(30, ami.getLDAcctStatus());
        ps.setString(31, ami.getGroupAssetAcct());
        ps.setString(32, ami.getGAAStatus());
        ps.setString(33, ami.getSelfChargeAcct());
        ps.setString(34, ami.getSelfChargeStatus());
//        ps.setString(41, ami.getDatabaseName());
        ps.setString(35, recId);
//        System.out.println("The content of ps is################### "+ ps.toString());
        int psResult = ps.executeUpdate();
//		System.out.println("The content of ps is################### "+ ps.toString());
		done = (psResult != -1);
//          System.out.println("outcome of update psResult is ########## "+ psResult);


	}
    catch(SQLException ex) {//start catch block
				System.out.println("\n--- SQLException caught in updateAssetManagerInfoTmp METHOD of COMPANY HANDLER CLASS---\n");
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

	public java.util.ArrayList getDriverByQuery(String filter,String parameter) {
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
//		Statement s = null;
		PreparedStatement s = null;

		try {
			c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			s = c.prepareStatement(query.toString());
			s.setString(1, parameter);
			rs = s.executeQuery();
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
				+ "Create_date FROM am_ad_driver WHERE Driver_ID=? ";

		Connection c = null;
		ResultSet rs = null;
//		Statement s = null;
		PreparedStatement s = null;

		try {
			c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			s = c.prepareStatement(query.toString());
			s.setString(1, driverid);
			rs = s.executeQuery();
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
				+ "Create_date FROM am_ad_driver WHERE Driver_Code=? ";

		Connection c = null;
		ResultSet rs = null;
//		Statement s = null;
		PreparedStatement s = null;

		try {
			c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			s = c.prepareStatement(query.toString());
			s.setString(1, driverid);
			rs = s.executeQuery();
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

	public java.util.ArrayList getLocationByQuery(String filter,String statusparameter) {
		java.util.ArrayList _list = new java.util.ArrayList();
		legend.admin.objects.Locations location = null;

		String query = "SELECT Location_Id, Location_Code, Location"
				+ ", Status, User_Id, Create_Date"
				+ " FROM AM_GB_LOCATION WHERE Location_Id IS NOT NULL ";

		Connection c = null;
		ResultSet rs = null;
//		Statement s = null;
		PreparedStatement s = null;
		query = query + filter;
		try {
			c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);

			s = c.prepareStatement(query.toString());
			s.setString(1, statusparameter);
			rs = s.executeQuery();
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
				+ " FROM AM_GB_LOCATION WHERE Location_Id = ? ";

		Connection c = null;
		ResultSet rs = null;
//		Statement s = null;
		PreparedStatement s = null;
		try {
			c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			s = c.prepareStatement(query.toString());
			s.setString(1, LocID);
			rs = s.executeQuery();
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
				+ " FROM AM_GB_LOCATION WHERE Location_Code = ? ";

		Connection c = null;
		ResultSet rs = null;
//		Statement s = null;
		PreparedStatement s = null;
		try {
			c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			s = c.prepareStatement(query.toString());
			s.setString(1, LocCode);
			rs = s.executeQuery();
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

	public java.util.List getCategoryClassByQuery(String filter,String parameter) {
		java.util.List _list = new java.util.ArrayList();
		legend.admin.objects.CategoryClass catclass = null;

		String query = "SELECT Class_Id, Class_Code, Class_Name"
				+ ", Class_Status, User_Id, Create_Date"
				+ " FROM AM_AD_CATEGORY_CLASS WHERE Class_Id IS NOT NULL ";

		Connection c = null;
		ResultSet rs = null;
//		Statement s = null;
		PreparedStatement s = null;
		query += filter;
		try {
			c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);

			s = c.prepareStatement(query.toString());
			s.setString(1, parameter);
			rs = s.executeQuery();
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
				+ " FROM AM_AD_CATEGORY_CLASS WHERE Class_ID = ? ";

		Connection c = null;
		ResultSet rs = null;
//		Statement s = null;
		PreparedStatement s = null;
		try {
			c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);

			s = c.prepareStatement(query.toString());
			s.setString(1, CatClassID);
			rs = s.executeQuery();
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
				+ " FROM AM_AD_CATEGORY_CLASS WHERE Class_Code = ?";

		Connection c = null;
		ResultSet rs = null;
//		Statement s = null;
		PreparedStatement s = null;
		try {
			c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);

			s = c.prepareStatement(query.toString());
			s.setString(1, CatClassCode);
			rs = s.executeQuery();
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

	private legend.admin.objects.AssetMake getAnAssetMake(String filter, String parameter) {
		legend.admin.objects.AssetMake am = null;
		String query = "SELECT AssetMake_ID,AssetMake_Code,AssetMake"
				+ " ,status,Category_ID,user_id,create_date"
				+ " FROM am_gb_assetMake  ";

		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
//		Statement s = null;
		PreparedStatement s = null;

		try {
			c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);

			s = c.prepareStatement(query.toString());
			s.setString(1, parameter);
			rs = s.executeQuery();

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

	public java.util.List getAssetMakeByQuery(String filter,String statusParameter) {
		legend.admin.objects.AssetMake am = null;
		java.util.List<legend.admin.objects.AssetMake> _list = new java.util.ArrayList<legend.admin.objects.AssetMake>();
		String query = "SELECT AssetMake_ID,AssetMake_Code,AssetMake"
				+ " ,status,Category_ID,user_id,create_date"
				+ " FROM am_gb_assetMake  ";

		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
//		Statement s = null;
		PreparedStatement s = null;
		try {
			c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			s = c.prepareStatement(query.toString());
			s.setString(1, statusParameter);
			rs = s.executeQuery();
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
		String filter = " WHERE AssetMake_ID = ? ";
		legend.admin.objects.AssetMake am = getAnAssetMake(filter,id);
		return am;

	}

	public legend.admin.objects.AssetMake getAssetMakeByCode(String code) {
		String filter = " WHERE AssetMake_Code = ? ";
		legend.admin.objects.AssetMake am = getAnAssetMake(filter, code);
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

	public java.util.ArrayList getDisposableReasonsByQuery(String filter,String parameter) {
		java.util.ArrayList _list = new java.util.ArrayList();
		legend.admin.objects.DisposableReasons dispres = null;

		String query = "SELECT reason_id, reason_code, description, reason_status, user_id,"
				+ " create_date FROM am_ad_disposalReasons WHERE reason_id IS NOT NULL "+filter+"  ORDER BY DESCRIPTION ";

//		query += filter+"ORDER BY DESCRIPTION";

		try {
//			con = getConnection();
//			stmt = con.createStatement();
//			rs = stmt.executeQuery(query);

			con = getConnection();
			ps = con.prepareStatement(query.toString());
			ps.setString(1, parameter);
			rs = ps.executeQuery();

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
		String filter = " AND reason_status=?";
		java.util.ArrayList _list = getDisposableReasonsByQuery(filter,status);
		return _list;
	}

	public legend.admin.objects.DisposableReasons getDisposableReasonsByReasonId(
			String reasonId) {
		String filter = " AND reason_id=?";
		legend.admin.objects.DisposableReasons dispres = null;
		java.util.ArrayList _list = getDisposableReasonsByQuery(filter,reasonId);

		dispres = (legend.admin.objects.DisposableReasons) _list.get(0);
		return dispres;
	}

	public legend.admin.objects.DisposableReasons getDisposableReasonsByReasonCode(
			String reasonCode) {
		String filter = " AND reason_code=?";
		legend.admin.objects.DisposableReasons dispres = null;
		java.util.ArrayList _list = getDisposableReasonsByQuery(filter,reasonCode);
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
		String filter = " WHERE Driver_Status=? ";
		java.util.ArrayList _list = getDriverByQuery(filter,status);
		return _list;
	}

	public java.util.List getCategoryClassByStatus(String filter) {
		String filt = " and Class_Status = ? ";
		java.util.List _list = getCategoryClassByQuery(filt,filter);
		return _list;
	}


    public String getEmailStatus(String mailCode){
String  branch="";

            String query = "SELECT status FROM am_mail_statement where mail_code = '"+mailCode+"'";


            Connection c = null;
            ResultSet rs = null;
//            Statement s = null;
            PreparedStatement s = null;
            try
            {
                c = getConnection();
//                s = c.createStatement();

        		s = c.prepareStatement(query.toString());
        		s.setString(1, mailCode);

                for(rs = s.executeQuery(); rs.next();)
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
//public legend.admin.objects.AssetManagerInfo getAssetManagerInfoFed() {
//		legend.admin.objects.AssetManagerInfo ami = null;
//		String query = "SELECT  Suspense_Acct, Auto_Generate_Id, Residual_Value"
//				+ ", Depreciation_Method, Vat_Account, Wht_Account, Fed_Wht_Account"
//				+ ", PL_Disposal_Account, PLD_Status, Vat_Acct_Status, Wht_Acct_Status, Fed_wht_acct_status"
//				+ ", Suspense_Ac_Status,Sbu_Required, Sbu_Level,system_date,asset_acq_ac"
//				+", Cost_Threshold,defer_account,Trans_Threshold, part_pay_acct"
//		        + ", asset_acq_status, asset_defer_status, part_pay_status,loss_disposal_account,lossDisposal_act_status"
//		        + ", group_asset_account,group_asset_act_status,selfChargeVAT,selfCharge_Vat_status FROM AM_GB_COMPANY";
//
//		Connection c = null;
//		ResultSet rs = null;
//		Statement s = null;
//
//		try {
//			c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
//			while (rs.next()) {
//				String suspenseAcct = rs.getString("Suspense_Acct");
//				String depreciationMethod = rs.getString("Depreciation_Method");
//				String vatAccount = rs.getString("Vat_Account");
//				String whtAccount = rs.getString("Wht_Account");
//				String fedWhtAccount=rs.getString("Fed_Wht_Account");//ganiyu's code
//				String PLDisposalAccount = rs.getString("PL_Disposal_Account");
//				String PLDStatus = rs.getString("PLD_Status");
//				String vatAcctStatus = rs.getString("Vat_Acct_Status");
//				String whtAcctStatus = rs.getString("Wht_Acct_Status");
//				String suspenseAcctStatus = rs.getString("Suspense_Ac_Status");
//				String sbuRequired = rs.getString("Sbu_Required");
//				String sbuLevel = rs.getString("Sbu_Level");
//				String assetSuspenseAcct = rs.getString("asset_acq_ac");
//				String fedWhtAcctStatus = rs.getString("Fed_wht_acct_status");
//              double cp_threshold = rs.getDouble("Cost_Threshold");
//              String defer_account =rs.getString("defer_account");
//              double tr_threshold = rs.getDouble("Trans_Threshold");
//              String part_pay_acct = rs.getString("part_pay_acct");
//              String asset_acq_status=rs.getString("asset_acq_status");
//              String asset_defer_status=rs.getString("asset_defer_status");
//               String part_pay_status=rs.getString("part_pay_status");
//               String lossdisposalAcct=rs.getString("loss_disposal_account");
//               String lossDisposal_act_status=rs.getString("lossDisposal_act_status");
//               String groupAssetAcct=rs.getString("group_asset_account");
////               System.out.println("=====> Before update of the field groupAssetAcct: "+groupAssetAcct);
//               if(groupAssetAcct =="null" || groupAssetAcct == null){groupAssetAcct = "";}
//               String group_asset_act_status=rs.getString("group_asset_act_status");
//               String selfChargeVat=rs.getString("selfChargeVAT");
//               String selfCharge_Vat_status=rs.getString("selfCharge_Vat_status");
//				ami = new legend.admin.objects.AssetManagerInfo(processingDate,
//						processingFrequency, nextProcessingDate, defaultBranch,
//						branchName, suspenseAcct, autoGenId, residualValue,
//						depreciationMethod, vatAccount, whtAccount, fedWhtAccount,
//						PLDisposalAccount, PLDStatus, vatAcctStatus,
//						whtAcctStatus, fedWhtAcctStatus, suspenseAcctStatus, sbuRequired,
//						sbuLevel,lpo_r,bar_code_r,cp_threshold);
////				ami.setSysDate(sysDate);
//				ami.setAssetSuspenseAcct(assetSuspenseAcct);
//                ami.setDeferAccount(defer_account);
//                ami.setTrans_threshold(tr_threshold);
//                ami.setPart_pay(part_pay_acct);
//                ami.setAsset_acq_status(asset_acq_status);
//                ami.setAsset_defer_status(asset_defer_status);
//                ami.setPart_pay_status(part_pay_status);
////                ami.setThirdpartytransaction(thirdpartytransaction);
////                ami.setRaiseEntry(raiseEntry);
//                ami.setLossDisposalAcct(lossdisposalAcct);
//                ami.setLDAcctStatus(lossDisposal_act_status);
//                ami.setGroupAssetAcct(groupAssetAcct);
//                ami.setGAAStatus(group_asset_act_status);
//                ami.setSelfChargeAcct(selfChargeVat);
//                ami.setSelfChargeStatus(selfCharge_Vat_status);
////                ami.setDatabaseName(databaseName);
//
//			}
//
//		} catch (Exception e) {
//
//            System.out.println("######ERROR OCCURED IN getAssetManagerInfoFed() OF COMPANYHANDLER CLASS");
//			e.printStackTrace();
//		}
//
//		finally {
//			closeConnection(c, s, rs);
//		}
//		return ami;
//	}

public legend.admin.objects.AssetManagerInfo getAssetManagerInfoFed() {
	legend.admin.objects.AssetManagerInfo ami = null;
    String query = "SELECT  Processing_Date, Processing_Frequency, Next_Processing_Date, Default_Branch, Branch_Name,Suspense_Acct, Auto_Generate_Id, Residual_Value, Depreciation_Method, Vat_Account, Wht_Account, Fed_Wht_Account, PL_Disposal_Account, PLD_Status, Vat_Acct_Status, Wht_Acct_Status, Fed_wht_acct_status, Suspense_Ac_Status,Sbu_Required, Sbu_Level,system_date,asset_acq_ac, LPO_Required,Barcode_Fld,Cost_Threshold,defer_account,Trans_Threshold, part_pay_acct,THIRDPARTY_REQUIRE, asset_acq_status, asset_defer_status, part_pay_status,raise_entry,loss_disposal_account,lossDisposal_act_status, group_asset_account,group_asset_act_status,selfChargeVAT,selfCharge_Vat_status,databaseName FROM AM_GB_COMPANY";
    Connection c = null;
    ResultSet rs = null;
    Statement s = null;

    try {
       c = this.getConnection();
       s = c.createStatement();
       rs = s.executeQuery(query);

       while(rs.next()) {
          String processingDate = this.sdf.format(rs.getDate("Processing_Date"));
          String processingFrequency = rs.getString("Processing_Frequency");
          String nextProcessingDate = this.sdf.format(rs.getDate("Next_Processing_Date"));
          String defaultBranch = rs.getString("Default_Branch");
          String branchName = rs.getString("Branch_Name");
          String suspenseAcct = rs.getString("Suspense_Acct");
          String autoGenId = rs.getString("Auto_Generate_Id");
          String residualValue = rs.getString("Residual_Value");
          String depreciationMethod = rs.getString("Depreciation_Method");
          String vatAccount = rs.getString("Vat_Account");
          String whtAccount = rs.getString("Wht_Account");
          String fedWhtAccount = rs.getString("Fed_Wht_Account");
          String PLDisposalAccount = rs.getString("PL_Disposal_Account");
          String PLDStatus = rs.getString("PLD_Status");
          String vatAcctStatus = rs.getString("Vat_Acct_Status");
          String whtAcctStatus = rs.getString("Wht_Acct_Status");
          String suspenseAcctStatus = rs.getString("Suspense_Ac_Status");
          String sbuRequired = rs.getString("Sbu_Required");
          String sbuLevel = rs.getString("Sbu_Level");
          String sysDate = this.sdf.format(rs.getDate("system_date"));
          String assetSuspenseAcct = rs.getString("asset_acq_ac");
          String fedWhtAcctStatus = rs.getString("Fed_wht_acct_status");
          String lpo_r = rs.getString("LPO_Required");
          String bar_code_r = rs.getString("Barcode_Fld");
          double cp_threshold = rs.getDouble("Cost_Threshold");
          String defer_account = rs.getString("defer_account");
          double tr_threshold = rs.getDouble("Trans_Threshold");
          String part_pay_acct = rs.getString("part_pay_acct");
          String asset_acq_status = rs.getString("asset_acq_status");
          String asset_defer_status = rs.getString("asset_defer_status");
          String part_pay_status = rs.getString("part_pay_status");
          String thirdpartytransaction = rs.getString("THIRDPARTY_REQUIRE");
          String raiseEntry = rs.getString("RAISE_ENTRY");
          String lossdisposalAcct = rs.getString("loss_disposal_account");
          String lossDisposal_act_status = rs.getString("lossDisposal_act_status");
          String groupAssetAcct = rs.getString("group_asset_account");
          if (groupAssetAcct == "null" || groupAssetAcct == null) {
             groupAssetAcct = "";
          }

          String group_asset_act_status = rs.getString("group_asset_act_status");
          String selfChargeVat = rs.getString("selfChargeVAT");
          String selfCharge_Vat_status = rs.getString("selfCharge_Vat_status");
          String databaseName = rs.getString("databaseName");
          ami = new legend.admin.objects.AssetManagerInfo(processingDate, processingFrequency, nextProcessingDate, defaultBranch, branchName, suspenseAcct, autoGenId, residualValue, depreciationMethod, vatAccount, whtAccount, fedWhtAccount, PLDisposalAccount, PLDStatus, vatAcctStatus, whtAcctStatus, fedWhtAcctStatus, suspenseAcctStatus, sbuRequired, sbuLevel, lpo_r, bar_code_r, cp_threshold);
          ami.setSysDate(sysDate);
          ami.setAssetSuspenseAcct(assetSuspenseAcct);
          ami.setDeferAccount(defer_account);
          ami.setTrans_threshold(tr_threshold);
          ami.setPart_pay(part_pay_acct);
          ami.setAsset_acq_status(asset_acq_status);
          ami.setAsset_defer_status(asset_defer_status);
          ami.setPart_pay_status(part_pay_status);
          ami.setThirdpartytransaction(thirdpartytransaction);
          ami.setRaiseEntry(raiseEntry);
          ami.setLossDisposalAcct(lossdisposalAcct);
          ami.setLDAcctStatus(lossDisposal_act_status);
          ami.setGroupAssetAcct(groupAssetAcct);
          ami.setGAAStatus(group_asset_act_status);
          ami.setSelfChargeAcct(selfChargeVat);
          ami.setSelfChargeStatus(selfCharge_Vat_status);
          ami.setDatabaseName(databaseName);
       }
    } catch (Exception var51) {
       System.out.println("######ERROR OCCURED IN getAssetManagerInfoFed() OF COMPANYHANDLER CLASS");
       var51.printStackTrace();
    } finally {
       this.closeConnection(c, s, rs);
    }

    return ami;
 }

public legend.admin.objects.AssetManagerInfo getAssetManagerInfoFed2() {
	legend.admin.objects.AssetManagerInfo ami = null;
    String query = "SELECT  Processing_Date, Processing_Frequency, Next_Processing_Date, Default_Branch, Branch_Name,Suspense_Acct, Auto_Generate_Id, "
    		+ "Residual_Value, Depreciation_Method, Vat_Account, Wht_Account, Fed_Wht_Account, PL_Disposal_Account, PLD_Status, Vat_Acct_Status, "
    		+ "Wht_Acct_Status, Fed_wht_acct_status, Suspense_Ac_Status,Sbu_Required, Sbu_Level,system_date,asset_acq_ac, LPO_Required,Barcode_Fld,"
    		+ "Cost_Threshold,defer_account,Trans_Threshold, part_pay_acct,THIRDPARTY_REQUIRE, asset_acq_status, asset_defer_status, part_pay_status,"
    		+ "raise_entry,loss_disposal_account,lossDisposal_act_status, group_asset_account,group_asset_act_status,selfChargeVAT,selfCharge_Vat_status,"
    		+ "databaseName,Financial_Start_Date,Financial_No_OfMonths,Financial_End_Date,component_delimiter,Vat_Rate,Wht_Rate FROM AM_GB_COMPANY";
    Connection c = null;
    ResultSet rs = null;
    Statement s = null;

    try {
       c = this.getConnection();
       s = c.createStatement();
       rs = s.executeQuery(query);

       while(rs.next()) {
          String processingDate = this.sdf.format(rs.getDate("Processing_Date"));
          String processingFrequency = rs.getString("Processing_Frequency");
          String nextProcessingDate = this.sdf.format(rs.getDate("Next_Processing_Date"));
          String defaultBranch = rs.getString("Default_Branch");
          String branchName = rs.getString("Branch_Name");
          String suspenseAcct = rs.getString("Suspense_Acct");
          String autoGenId = rs.getString("Auto_Generate_Id");
          String residualValue = rs.getString("Residual_Value");
          String depreciationMethod = rs.getString("Depreciation_Method");
          String vatAccount = rs.getString("Vat_Account");
          String whtAccount = rs.getString("Wht_Account");
          String fedWhtAccount = rs.getString("Fed_Wht_Account");
          String PLDisposalAccount = rs.getString("PL_Disposal_Account");
          String PLDStatus = rs.getString("PLD_Status");
          String vatAcctStatus = rs.getString("Vat_Acct_Status");
          String whtAcctStatus = rs.getString("Wht_Acct_Status");
          String suspenseAcctStatus = rs.getString("Suspense_Ac_Status");
          String sbuRequired = rs.getString("Sbu_Required");
          String sbuLevel = rs.getString("Sbu_Level");
//          String sysDate = this.sdf.format(rs.getDate("system_date"));
          String assetSuspenseAcct = rs.getString("asset_acq_ac");
          String fedWhtAcctStatus = rs.getString("Fed_wht_acct_status");
          String lpo_r = rs.getString("LPO_Required");
          String bar_code_r = rs.getString("Barcode_Fld");
          double cp_threshold = rs.getDouble("Cost_Threshold");
          String defer_account = rs.getString("defer_account");
          double tr_threshold = rs.getDouble("Trans_Threshold");
          String part_pay_acct = rs.getString("part_pay_acct");
          String asset_acq_status = rs.getString("asset_acq_status");
          String asset_defer_status = rs.getString("asset_defer_status");
          String part_pay_status = rs.getString("part_pay_status");
			String financialStartDate = rs.getString("Financial_Start_Date");
			if(financialStartDate!=null) {
			financialStartDate = sdf.format(rs.getDate("Financial_Start_Date"));
			}

			int financialNoOfMonths = rs.getInt("Financial_No_OfMonths");

			String financialEndDate = rs.getString("Financial_End_Date");
			if(financialEndDate!=null) {
			 financialEndDate = sdf.format(rs.getDate("Financial_End_Date"));
			}
//          String thirdpartytransaction = rs.getString("THIRDPARTY_REQUIRE");
//          String raiseEntry = rs.getString("RAISE_ENTRY");
          String lossdisposalAcct = rs.getString("loss_disposal_account");
          String lossDisposal_act_status = rs.getString("lossDisposal_act_status");
          String groupAssetAcct = rs.getString("group_asset_account");
          if (groupAssetAcct == "null" || groupAssetAcct == null) {
             groupAssetAcct = "";
          }
          String comp_delimiter = rs.getString("component_delimiter");
			double vatRate = rs.getDouble("Vat_Rate");
			double whtRate = rs.getDouble("Wht_Rate");//to do;get value for federal and state rate

          String group_asset_act_status = rs.getString("group_asset_act_status");
          String selfChargeVat = rs.getString("selfChargeVAT");
          String selfCharge_Vat_status = rs.getString("selfCharge_Vat_status");
          ami = new legend.admin.objects.AssetManagerInfo(processingDate, processingFrequency, nextProcessingDate, defaultBranch, branchName, suspenseAcct, autoGenId, residualValue, depreciationMethod, vatAccount, whtAccount, fedWhtAccount, PLDisposalAccount, PLDStatus, vatAcctStatus, whtAcctStatus, fedWhtAcctStatus, suspenseAcctStatus, sbuRequired, sbuLevel, lpo_r, bar_code_r, cp_threshold);
          ami.setAssetSuspenseAcct(assetSuspenseAcct);
          ami.setDeferAccount(defer_account);
          ami.setTrans_threshold(tr_threshold);
          ami.setPart_pay(part_pay_acct);
          ami.setAsset_acq_status(asset_acq_status);
          ami.setAsset_defer_status(asset_defer_status);
          ami.setPart_pay_status(part_pay_status);
          ami.setLossDisposalAcct(lossdisposalAcct);
          ami.setLDAcctStatus(lossDisposal_act_status);
          ami.setGroupAssetAcct(groupAssetAcct);
          ami.setGAAStatus(group_asset_act_status);
          ami.setSelfChargeAcct(selfChargeVat);
          ami.setSelfChargeStatus(selfCharge_Vat_status);
          ami.setDefaultBranch(defaultBranch);
          ami.setBranchName(branchName);
          ami.setResidualValue(residualValue);
          ami.setDepreciationMethod(depreciationMethod);
          ami.setAutoGenId(autoGenId);
          ami.setFinancialStartDate(financialStartDate);
          ami.setFinancialNoOfMonths(financialNoOfMonths);
          ami.setFinancialEndDate(financialEndDate);
          ami.setVatRate(vatRate);
          ami.setWhtRate(whtRate);
          ami.setComp_delimiter(comp_delimiter);

       }
    } catch (Exception var51) {
       System.out.println("######ERROR OCCURED IN getAssetManagerInfoFed() OF COMPANYHANDLER CLASS");
       var51.printStackTrace();
    } finally {
       this.closeConnection(c, s, rs);
    }

    return ami;
 }


 public String[] getEmailStatusAndName(String tran_type){
String[]  branch= new String[2];

            String query = "SELECT status, mail_code FROM am_mail_statement where transaction_type = ?";
          //  System.out.println("getEmailStatusAndName query: "+query);
            Connection c = null;
            ResultSet rs = null;
//            Statement s = null;
            PreparedStatement s = null;

            try
            {
                c = getConnection();
//                s = c.createStatement();
        		s = c.prepareStatement(query.toString());
        		s.setString(1, tran_type);
                for(rs = s.executeQuery(); rs.next();)
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
 	String startDate = date.substring(0,8)+"01";
 	date = date+" 23:59:59.997";
// 	System.out.println("====Server Date-----> "+date+"     startDate: "+startDate);
 	String finacleTransId= null;
//		String query = " SELECT  finacle_Trans_Id from am_raisentry_transaction where transaction_date >'"+date+"' and iso<>'000' ";
		String query = " SELECT  finacle_Trans_Id,Trans_Id from am_raisentry_transaction where transaction_date between ? and ? and iso<>'000' ";
//		System.out.println("====Server query-----> "+query);
	Connection c = null;
	ResultSet rs = null;
//	Statement s = null;
	PreparedStatement s = null;

	try {
		    c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			s = c.prepareStatement(query.toString());
			s.setString(1, startDate);
			s.setString(2, date);
			rs = s.executeQuery();
			while (rs.next())
			   {
				String finacle_Trans_Id = rs.getString("finacle_Trans_Id");
				String Trans_Id = rs.getString("Trans_Id");
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setAssetId(Trans_Id);
				newTransaction.setTranType(finacle_Trans_Id);
				_list.add(newTransaction);
			   }
//			closeConnection(c, s, rs);
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
@SuppressWarnings("null")
public String getFinacleRecords(String finacleTransId)
 {
 	String date = String.valueOf(dateConvert(new java.util.Date()));
 	date = date.substring(0, 10);
 	String date1 =date.substring(0,2);
 	String date0 = date.substring(2,10);
// 	System.out.println("======date1: "+date1+"    date0: "+date0+"   date: "+date);
 	date = "20"+date.substring(2,10);
//	System.out.println("System Date in getFinacleRecords====> "+date+"====finacleTransId==>> "+finacleTransId);
 	String iso ="";
		String query = " SELECT  tran_particular_2 from fix_tb " +
				"where tran_particular_2=? and to_char(tran_date,'DD-MM-YYYY') >= ?";
//		System.out.println("Query on getFinacleRecords====> "+query);
	Connection c = null;
//	ConnectionClass connection = null;
	ResultSet rs = null;
	PreparedStatement ps = null;

	try {
		   c = getFinacleConnection();
        //connection = new ConnectionClass();
        //ps = connection.getPreparedStatementOracle(query);
//		    c = getConnectionFinacle();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			//rs = ps.executeQuery(query);
		   rs = c.prepareStatement(query).executeQuery();

			ps = c.prepareStatement(query.toString());
			ps.setString(1, finacleTransId);
			ps.setString(2, getOracleDateFormat(date));
			rs = ps.executeQuery();
			while (rs.next())
			   {
				 iso = "000";
			   }
//			System.out.println("================ISO Value: "+iso);
	 	}
				 catch (Exception e)
					{
						e.printStackTrace();
					}
					finally
					{
//						connection.freeResource();
						closeConnection(c, ps, rs);
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
public boolean updateSqlRecords( String iso,String finacleTransId,String TransId)
	{
		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String date = String.valueOf(dateConvert(new java.util.Date()));
	//	System.out.println("=====updateSqlRecords iso=====> "+iso);
	//	System.out.println("=====updateSqlRecords date=====> "+date);
	//	System.out.println("=====updateSqlRecords finacleTransId=====> "+finacleTransId);
	 	date = date.substring(0, 10);
		String query = "UPDATE am_raisentry_transaction SET iso=?   where transaction_date >='"+date+"' and finacle_Trans_Id=? and TRANS_ID = '"+TransId+"' ";
	//	System.out.println("=====updateSqlRecords query=====> "+query);
		try {
			con = getConnection();
			ps = con.prepareStatement(query);

			ps.setString(1, iso);
			ps.setString(2, finacleTransId);
			done = (ps.executeUpdate() != -1);
			closeConnection(con, ps);
		} catch (Exception e) {
			e.printStackTrace();
		//	closeConnection(con, ps);
		} finally {
	    	closeConnection(con, ps);
	    }

		return done;
	}
private Connection getConnectionFinacle() {
	Connection con = null;
	PreparedStatement ps = null;
	dc = new DataConnect("custom");
	try {
		con = dc.getConnection();
	} catch (Exception e) {
		System.out.println("WARNING: Error getting connection ->"
				+ e.getMessage());
	} finally {
    	closeConnection(con, ps);
    }
	return con;
}

public java.util.ArrayList getNewAssetSqlRecordsForCapitalised()
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
				"ASSET_CODE,ERROR_MESSAGE,ASSET_TYPE,Vat_Value,Wh_Tax_Value,QUANTITY,TRAN_TYPE,Location,Spare_1,Spare_2,Multiple,Memo,"+
				"MemoValue,IMPROV_USEFULLIFE, SUB_CATEGORY_CODE, SPARE_3,SPARE_4,SPARE_5,SPARE_6, "+
				"PROJECT_CODE,LOCATION "+
				"FROM NEW_ASSET_INTERFACE WHERE POSTED = 'N' AND ASSET_TYPE = 'C' AND TRAN_TYPE != 'P' AND INTEGRIFY_ID IS NOT NULL ";
//	Transaction transaction = null;
//     System.out.println("====query in getNewAssetSqlRecordsForCapitalised-----> "+query);
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
				String strsubcategoryCode = rs.getString("SUB_CATEGORY_CODE");
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
				double StrVatValue = rs.getDouble("Vat_Value");
				int Strnoofitems = rs.getInt("QUANTITY");
				String Strlocation = rs.getString("Location");
				String Strspare1 = rs.getString("Spare_1");
				String Strspare2 = rs.getString("Spare_2");
				String Strspare3 = rs.getString("Spare_3");
				String Strspare4 = rs.getString("Spare_4");
				String Strspare5 = rs.getString("Spare_5");
				String Strspare6 = rs.getString("Spare_6");
				String Strmultiple = rs.getString("Multiple");
				String Strmemo = rs.getString("Memo");
				String Strmemovalue = rs.getString("MemoValue");
				int Strusefullife = rs.getInt("IMPROV_USEFULLIFE");
				String projectCode = rs.getString("PROJECT_CODE");
				String location = rs.getString("LOCATION");
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
				newTransaction.setSubcategoryCode(strsubcategoryCode);
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
				newTransaction.setVatValue(StrVatValue);
				newTransaction.setTranType(Strtrantype);
				newTransaction.setNoofitems(Strnoofitems);
				newTransaction.setLocation(Strlocation);
				newTransaction.setSpare1(Strspare1);
				newTransaction.setSpare2(Strspare2);
				newTransaction.setSpare3(Strspare3);
				newTransaction.setSpare4(Strspare4);
				newTransaction.setSpare5(Strspare5);
				newTransaction.setSpare6(Strspare6);
				newTransaction.setMemo(Strmemo);
				newTransaction.setMemovalue(Strmemovalue);
				newTransaction.setMultiple(Strmultiple);
				newTransaction.setUsefullife(Strusefullife);
				newTransaction.setProjectCode(projectCode);
				newTransaction.setLocation(location);
				_list.add(newTransaction);
			   }
//			closeConnection(c, s, rs);
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
				"ASSET_CODE,ERROR_MESSAGE,ASSET_TYPE,Vat_Value,Wh_Tax_Value,ITEMCOUNT,Location,Spare_1,Spare_2,Multiple,Memo,MemoValue, "+
				"SUB_CATEGORY_CODE, SPARE_3,SPARE_4,SPARE_5,SPARE_6,  "+
				"PROJECT_CODE,LOCATION "+
				"FROM NEW_GROUP_ASSET_INTERFACE WHERE POSTED = 'N' AND TRAN_TYPE = 'N' AND INTEGRIFY_ID = ? ";
//	Transaction transaction = null;

	Connection c = null;
	ResultSet rs = null;
//	Statement s = null;
	PreparedStatement s = null;
	try {
		    c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			s = c.prepareStatement(query.toString());
			s.setString(1, integrifyId);
			rs = s.executeQuery();
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
				String strsubcategoryCode = rs.getString("SUB_CATEGORY_CODE");
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
				double StrvatValue = rs.getDouble("Vat_Value");
				int recno = rs.getInt("ITEMCOUNT");
				String Strlocation = rs.getString("Location");
				String Strspare1 = rs.getString("Spare_1");
				String Strspare2 = rs.getString("Spare_2");
				String Strspare3 = rs.getString("Spare_3");
				String Strspare4 = rs.getString("Spare_4");
				String Strspare5 = rs.getString("Spare_5");
				String Strspare6 = rs.getString("Spare_6");
				String projectCode = rs.getString("PROJECT_CODE");
				String location = rs.getString("LOCATION");
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
				newTransaction.setSubcategoryCode(strsubcategoryCode);
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
				newTransaction.setVatValue(StrvatValue);
				newTransaction.setNoofitems(recno);
				newTransaction.setLocation(Strlocation);
				newTransaction.setSpare1(Strspare1);
				newTransaction.setSpare2(Strspare2);
				newTransaction.setSpare3(Strspare3);
				newTransaction.setSpare4(Strspare4);
				newTransaction.setSpare5(Strspare5);
				newTransaction.setSpare6(Strspare6);
				newTransaction.setMemo(Strmemo);
				newTransaction.setMemovalue(Strmemovalue);
				newTransaction.setMultiple(Strmultiple);
				newTransaction.setProjectCode(projectCode);
				newTransaction.setLocation(location);

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

public boolean chkidExistsOld(String assetid,String assettype) {
    boolean exists = false;
    Connection con = null;
    String SQLC = "";
    String SQLU = "";
    PreparedStatement ps = null;
    ResultSet rs = null;
    if(assettype.equalsIgnoreCase("C")){
    	SQLC = "SELECT * FROM am_asset WHERE asset_id= ? ";
    }
    if(assettype.equalsIgnoreCase("U")){
       SQLU = "SELECT * FROM AM_ASSET_UNCAPITALIZED WHERE asset_id= ? ";
    }
    try {
        //con = dbConnection.getConnection("legendPlus");
    	con = getConnection();
    	if(assettype.equalsIgnoreCase("C")){
        ps = con.prepareStatement(SQLC);
        ps.setString(1, assetid);
    	}
    	if(assettype.equalsIgnoreCase("U")){
            ps = con.prepareStatement(SQLU);
            ps.setString(1, assetid);
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

public boolean chkidExists(String assetId, String assetType) {
    String query;
    if ("C".equalsIgnoreCase(assetType)) {
        query = "SELECT 1 FROM am_asset WHERE asset_id = ?";
    } else if ("U".equalsIgnoreCase(assetType)) {
        query = "SELECT 1 FROM am_asset_uncapitalized WHERE asset_id = ?";
    } else {
        // Unknown asset type
        return false;
    }

    try (Connection con = getConnection();
         PreparedStatement ps = con.prepareStatement(query)) {

        ps.setString(1, assetId);

        try (ResultSet rs = ps.executeQuery()) {
            return rs.next(); // true if record exists
        }

    } catch (Exception ex) {
        System.out.println("WARN: chkIdExists -> " + ex.getMessage());
        ex.printStackTrace();
        return false;
    }
}


private boolean rinsertAssetRecord(String integrifyId,String Description,String RegistrationNo,String VendorAC,String Datepurchased,
		String AssetMake,String AssetModel,String AssetSerialNo,String AssetEngineNo,String SupplierName,String AssetUser,
		String AssetMaintenance,double CostPrice,double VatableCost,String AuthorizedBy,String WhTax,double whtaxamount,String PostingDate,String EffectiveDate,
		String PurchaseReason,String SubjectTOVat,String AssetStatus,String State,String Driver,String UserID,String branchCode,
		String sectionCode,String deptCode,String categoryCode,String subcategoryCode,String barCode,String sbuCode,String lpo,String invoiceNo,String supervisor,
		String posted,String assetId,String assetCode,String assettype,String branch_id,String dept_id,String section_id,String category_id,String subcategory_id,double vatamount,String residualvalue,int whtaxvalue,
		String location,String memovalue,String memo, String spare1,String spare2, String spare3,String spare4, String spare5,String spare6,String multiple,String projectCode,String groupId,String delimiter) throws Exception, Throwable {

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
//	System.out.println("Branch Id:    "+branch_id+"  dept Id: "+dept_id+"  sectionId: "+section_id+"  categoryId:  "+category_id);
	String asset_id = getNewIdentity(branch_id,
    		dept_id, section_id, category_id,delimiter);
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
    if (subcategory_id == null || subcategory_id.equals("")) {
    	subcategory_id = "0";
    }
    if (subcategoryCode == null || subcategoryCode.equals("")) {
    	subcategoryCode = "0";
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
    if (projectCode == null || projectCode.equals("")) {
    	projectCode = "0";
    }
    if (groupId == null || groupId.equals("")) {
    	groupId = "0";
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
  //  System.out.println("Rate in rinsertAssetRecord : "+rate);
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
                         "system_ip,mac_address,asset_code,memo,memoValue,INTEGRIFY,SUB_CATEGORY_ID,SUB_CATEGORY_CODE,SPARE_3,SPARE_4,SPARE_5,SPARE_6,PROJECT_CODE,GROUP_ID) " +

                         "VALUES" +
                         "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                         "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

//For Uncapitalized Assets
    String createQueryU = "INSERT INTO AM_ASSET_UNCAPITALIZED      " +
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
    "system_ip,mac_address,asset_code,memo,memoValue,INTEGRIFY,SUB_CATEGORY_ID,SUB_CATEGORY_CODE,SPARE_3,SPARE_4,SPARE_5,SPARE_6,PROJECT_CODE,GROUP_ID) " +

    "VALUES" +
    "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
    "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";



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
      //  con = dbConnection.getConnection("legendPlus");
        if(assettype.equalsIgnoreCase("C")){
       // 	System.out.println("assettype in rinsertAssetRecord for C: "+assettype);
          ps = con.prepareStatement(createQueryC);
        }
        if(assettype.equalsIgnoreCase("P")){
       // 	System.out.println("assettype in rinsertAssetRecord for C: "+assettype);
        	require_depreciation = "N";
          ps = con.prepareStatement(createQueryC);
        }
        if(assettype.equalsIgnoreCase("U")){
        //	System.out.println("assettype in rinsertAssetRecord for U: "+assettype);
           ps = con.prepareStatement(createQueryU);
          // require_depreciation = "N";
        }
        AssetStatus = "APPROVED";
        System.out.println("assettype in rinsertAssetRecord: "+assettype);
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
        ps.setDouble(21, (CostPrice-10.00));
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
        ps.setInt(77, Integer.parseInt(subcategory_id));
        ps.setString(78, categoryCode);
        ps.setString(79, spare3);
        ps.setString(80, spare4);
        ps.setString(81, spare5);
        ps.setString(82, spare6);
        ps.setString(83, projectCode);
        ps.setString(84, groupId);
        ps.execute();
    //     System.out.println("createQueryC: "+createQueryC);
        System.out.println("Above to run rinsertAssetRecord2: ");
        rinsertAssetRecord2(integrifyId,Description,RegistrationNo,VendorAC,Datepurchased,
        		AssetMake,AssetModel,AssetSerialNo,AssetEngineNo,SupplierName,AssetUser,
        		AssetMaintenance,CostPrice,VatableCost,AuthorizedBy,WhTax,whtaxamount,PostingDate,EffectiveDate,
        		PurchaseReason,SubjectTOVat,AssetStatus,State,Driver,UserID,branchCode,require_depreciation,
        		sectionCode,deptCode,categoryCode,subcategoryCode,barCode,sbuCode,lpo,invoiceNo,supervisor,
        		posted,asset_id,asset_Code,assettype,branch_id,dept_id,section_id,depreciation_end_date,
        		section,spare1,spare2,status,partPAY,fullyPAID,
        		deferPay,SystemIp,MacAddress,who_to_remind,email_1,who_to_remind_2,email2,raise_entry,
        		noOfMonths,warrantyStartDate,expiryDate,category_id,subcategory_id,vatamount,residual_value,whtaxvalue,
        		location,memovalue,memo,spare1,spare2,spare3,spare4,spare5,spare6,multiple,groupId);


    } catch (Exception ex) {
        done = false;
        System.out.println("WARN:Error creating asset in rinsertAssetRecord->" + ex);
    } finally {
    	closeConnection(con, ps);
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
//if (quarter.equalsIgnoreCase("FIRST")) {
//result = values[0] -
//(values[1] +
//		CostPrice);
//} else if (quarter.equalsIgnoreCase("2ND")) {
//result = values[2] -
//(values[3] +
//		CostPrice);
//} else if (quarter.equalsIgnoreCase("3RD")) {
//result = values[4] -
//(values[5] +
//		CostPrice);
//} else if (quarter.equalsIgnoreCase("4TH")) {
//result = values[6] -
//(values[7] +
//		CostPrice);
//}
	result = values[8] -
	(values[9] +
	CostPrice);
}

if (result < 0) {
allocation = false;
}
return allocation;
}
public void updateBudget(String quarter, String[] bugdetinfo,String categoryCode,String branchCode,double CostPrice,String sbuCode,String subCategoryCode ) throws Exception {

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
                                   " SET Q1_ACTUAL = ((COALESCE(Q1_ACTUAL,0) + " +
                                   CostPrice +
                                   "),BALANCE_ALLOCATION = BALANCE_ALLOCATION - "+CostPrice+" WHERE BRANCH_ID='" +
                                   branchCode +
                                   "' AND CATEGORY_CODE='" + categoryCode +
                                   "' AND SBU_CODE = '" + sbuCode + "' AND SUB_CATEGORY_CODE = '" + subCategoryCode + "' AND ACC_START_DATE='" +
                                   dateConvert(bugdetinfo[0])
                                   + "' AND ACC_END_DATE='" +
                                   dateConvert(bugdetinfo[2]) +
                                   "'";
//            System.out.println(budgetUpdate1);
            stmt.executeUpdate(budgetUpdate1);
           // System.out.println("Updated 1st Quarter");
        } else if (quarter.equalsIgnoreCase("2ND")) {
            String budgetUpdate1 = "UPDATE AM_ACQUISITION_BUDGET "
                                   +
                                   " SET Q2_ACTUAL = ((COALESCE(Q2_ACTUAL,0) + " +
                                   CostPrice +
                                   "),BALANCE_ALLOCATION = BALANCE_ALLOCATION - "+CostPrice+" WHERE BRANCH_ID='" +
                                   branchCode +
                                   "' AND CATEGORY='" + categoryCode +
                                   "' AND SBU_CODE = '" + sbuCode + "' AND ACC_START_DATE='" +
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
                                   " SET Q3_ACTUAL =((COALESCE(Q3_ACTUAL,0) + " +
                                   CostPrice +
                                   "),BALANCE_ALLOCATION = BALANCE_ALLOCATION - "+CostPrice+" WHERE BRANCH_ID='" +
                                   branchCode +
                                   "' AND CATEGORY='" + categoryCode +
                                   "' AND SBU_CODE = '" + sbuCode + "' AND ACC_START_DATE='" +
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
                                   " SET Q4_ACTUAL = ((COALESCE(Q4_ACTUAL,0) + " +
                                   CostPrice +
                                   "),BALANCE_ALLOCATION = BALANCE_ALLOCATION - "+CostPrice+" WHERE BRANCH_ID='" +
                                   branchCode +
                                   "' AND CATEGORY='" + categoryCode +
                                   "' AND SBU_CODE = '" + sbuCode + "' AND ACC_START_DATE='" +
                                   dateConvert(bugdetinfo[0])
                                   + "' AND ACC_END_DATE='" +
                                   dateConvert(bugdetinfo[2]) +
                                   "'";
            //System.out.println(budgetUpdate1);

            stmt.executeUpdate(budgetUpdate1);

            //System.out.println("Updated 4th Quarter");
        }  else{
            String budgetUpdate1 = "UPDATE AM_ACQUISITION_BUDGET "
                    +
                    " SET TOTAL_ACTUAL = ((COALESCE(TOTAL_ACTUAL,0) + " +
                    CostPrice +
                    "),BALANCE_ALLOCATION = BALANCE_ALLOCATION - "+CostPrice+" WHERE BRANCH_ID='" +
                    branchCode +
                    "' AND CATEGORY='" + categoryCode +
                    "' AND SBU_CODE = '" + sbuCode + "' AND SUB_CATEGORY_CODE = '" + subCategoryCode + "' AND ACC_START_DATE='" +
                    dateConvert(bugdetinfo[0])
                    + "' AND ACC_END_DATE='" +
                    dateConvert(bugdetinfo[2]) +
                    "'";
System.out.println(budgetUpdate1);
stmt.executeUpdate(budgetUpdate1);
// System.out.println("Updated 1st Quarter");
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
		String sectionCode,String deptCode,String categoryCode,String subcategoryCode,String barCode,String sbuCode,String lpo,String invoiceNo,String supervisor,
		String posted,String assetId,String assetCode,String assettype,String branch_id,String dept_id,String section_id,String category_id,String sub_category_id,double vatamount,
		String residualvalue,int whtaxvalue,String location,String memovalue,String memo, String spare1,String spare2, String spare3,String spare4,
		String spare5,String spare6,String multiple,String projectCode,String groupId,String delimiter) throws Exception, Throwable {
//	System.out.println("integrifyId in insertAssetRecord>>>>>> "+integrifyId+"  sectionCode:  "+sectionCode);
//	System.out.println("insertAssetRecord Purchase Date>>>>>>  "+Datepurchased);
    String[] budget = getBudgetInfo();
    double[] bugdetvalues = getBudgetValues(branchCode,categoryCode,sbuCode,subcategoryCode);
    int DONE = 0; //everything is oK
    int BUDGETENFORCED = 1; //EF budget = Yes, CF = NO, ERROR_FLAG
    int BUDGETENFORCEDCF = 2; //EF budget = Yes, CF = Yes, ERROR_FLAG
    int ASSETPURCHASEBD = 3; //asset falls into no quarter purchase date older than bugdet
    String Q = getQuarter(Datepurchased);
//    System.out.println("=====>>bugdetvalues: "+bugdetvalues+"   budget: "+budget+"   Q: "+Q+"   budget[3]: "+budget[3]+"   budget[4]: "+budget[4]);
    if(budget[3].equalsIgnoreCase("N")){
		rinsertAssetRecord(integrifyId,Description,RegistrationNo,VendorAC,Datepurchased,
				AssetMake,AssetModel,AssetSerialNo,AssetEngineNo,SupplierName,AssetUser,AssetMaintenance,
				CostPrice,VatableCost,AuthorizedBy,WhTax,whtaxamount,PostingDate,EffectiveDate,PurchaseReason,SubjectTOVat,AssetStatus,
				State,Driver,UserID,branchCode,sectionCode,deptCode,categoryCode,subcategoryCode,barCode,sbuCode,lpo,invoiceNo,
				supervisor,posted,assetId,assetCode,assettype,branch_id,dept_id,section_id,category_id,sub_category_id,vatamount,residualvalue,whtaxvalue,
				location,memovalue,memo,spare1,spare2,spare3,spare4,spare5,spare6,multiple,projectCode,groupId,delimiter);
		return DONE;
	}
	else if(budget[3].equalsIgnoreCase("Y")){
    if (!Q.equalsIgnoreCase("NQ")) {
        if (budget[3].equalsIgnoreCase("Y") &&
            budget[4].equalsIgnoreCase("N")) {
            if (chkBudgetAllocation(Q, bugdetvalues, false,CostPrice)) {
                updateBudget(Q, budget,categoryCode,branchCode,CostPrice,sbuCode,subcategoryCode);
                rinsertAssetRecord(integrifyId,Description,RegistrationNo,VendorAC,Datepurchased,
        				AssetMake,AssetModel,AssetSerialNo,AssetEngineNo,SupplierName,AssetUser,AssetMaintenance,
        				CostPrice,VatableCost,AuthorizedBy,WhTax,whtaxamount,PostingDate,EffectiveDate,PurchaseReason,SubjectTOVat,AssetStatus,
        				State,Driver,UserID,branchCode,sectionCode,deptCode,categoryCode,subcategoryCode,barCode,sbuCode,lpo,invoiceNo,
        				supervisor,posted,assetId,assetCode,assettype,branch_id,dept_id,section_id,category_id,sub_category_id,vatamount,residualvalue,whtaxvalue,
        				location,memovalue,memo,spare1,spare2, spare3,spare4,spare5,spare6,multiple,projectCode,groupId,delimiter);
                return DONE;
            } else {
                return BUDGETENFORCED;
            }

        } else if (budget[3].equalsIgnoreCase("Y") &&
                   budget[4].equalsIgnoreCase("Y")) {
            if (chkBudgetAllocation(Q, bugdetvalues, true,CostPrice)) {
                updateBudget(Q, budget,categoryCode,branchCode,CostPrice,sbuCode,subcategoryCode);
                rinsertAssetRecord(integrifyId,Description,RegistrationNo,VendorAC,Datepurchased,
        				AssetMake,AssetModel,AssetSerialNo,AssetEngineNo,SupplierName,AssetUser,AssetMaintenance,
        				CostPrice,VatableCost,AuthorizedBy,WhTax,whtaxamount,PostingDate,EffectiveDate,PurchaseReason,SubjectTOVat,AssetStatus,
        				State,Driver,UserID,branchCode,sectionCode,deptCode,categoryCode,subcategoryCode,barCode,sbuCode,lpo,invoiceNo,
        				supervisor,posted,assetId,assetCode,assettype,branch_id,dept_id,section_id,category_id,sub_category_id,vatamount,residualvalue,whtaxvalue,
        				location,memovalue,memo,spare1,spare2, spare3,spare4,spare5,spare6,multiple,projectCode,groupId,delimiter);
                return DONE;
            } else {
                return BUDGETENFORCEDCF;
            }

        } //else {
//            rinsertAssetRecord(integrifyId,Description,RegistrationNo,VendorAC,Datepurchased,
//    				AssetMake,AssetModel,AssetSerialNo,AssetEngineNo,SupplierName,AssetUser,AssetMaintenance,
//    				CostPrice,VatableCost,AuthorizedBy,WhTax,whtaxamount,PostingDate,EffectiveDate,PurchaseReason,SubjectTOVat,AssetStatus,
//    				State,Driver,UserID,branchCode,sectionCode,deptCode,categoryCode,subcategoryCode,barCode,sbuCode,lpo,invoiceNo,
//    				supervisor,posted,assetId,assetCode,assettype,branch_id,dept_id,section_id,category_id,sub_category_id,vatamount,residualvalue,whtaxvalue,
//    				location,memovalue,memo,spare1,spare2, spare3,spare4,spare5,spare6,multiple,projectCode,groupId,delimiter);
//            return DONE;
//        }
    } else {
        if (budget[3].equalsIgnoreCase("Y") &&
                budget[4].equalsIgnoreCase("N")) {
                if (chkBudgetAllocation(Q, bugdetvalues, false,CostPrice)) {
                    updateBudget(Q, budget,categoryCode,branchCode,CostPrice,sbuCode,subcategoryCode);
                    rinsertAssetRecord(integrifyId,Description,RegistrationNo,VendorAC,Datepurchased,
            				AssetMake,AssetModel,AssetSerialNo,AssetEngineNo,SupplierName,AssetUser,AssetMaintenance,
            				CostPrice,VatableCost,AuthorizedBy,WhTax,whtaxamount,PostingDate,EffectiveDate,PurchaseReason,SubjectTOVat,AssetStatus,
            				State,Driver,UserID,branchCode,sectionCode,deptCode,categoryCode,subcategoryCode,barCode,sbuCode,lpo,invoiceNo,
            				supervisor,posted,assetId,assetCode,assettype,branch_id,dept_id,section_id,category_id,sub_category_id,vatamount,residualvalue,whtaxvalue,
            				location,memovalue,memo,spare1,spare2, spare3,spare4,spare5,spare6,multiple,projectCode,groupId,delimiter);
                    return DONE;
                } else {
                    return BUDGETENFORCED;
                }

            }
//        return ASSETPURCHASEBD;
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
   
    try(Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);) {
    	try(ResultSet rs = ps.executeQuery();) {
        while (rs.next()) {
            result[0] = sdf.format(rs.getDate("financial_start_date"));
           // System.out.println("financial_start_date: "+result[0]);
            result[1] = rs.getString("financial_no_ofmonths");
          //  System.out.println("financial_no_ofmonths: "+result[1]);
            result[2] = sdf.format(rs.getDate("financial_end_date"));
            result[3] = rs.getString("enforce_acq_budget");
            result[4] = rs.getString("quarterly_surplus_cf");
        }
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

public double[] getBudgetValues(String branchcode, String categorycode, String sbuCode,String subcategoryCode) {
    double[] result = new double[12];

    String query = " SELECT Q1_ALLOCATION,Q1_ACTUAL,Q2_ALLOCATION"
                   +
                   ",Q2_ACTUAL,Q3_ALLOCATION,Q3_ACTUAL,Q4_ALLOCATION,Q4_ACTUAL,BALANCE_ALLOCATION,TOTAL_ACTUAL"
                   + " FROM AM_ACQUISITION_BUDGET WHERE CATEGORY_CODE= ? AND "
                   + " BRANCH_ID=? AND SBU_CODE = ?  AND SUB_CATEGORY_CODE = ?";
//    System.out.println("=====Budget query: "+query);
    
    try(Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);) {
		ps.setString(1, categorycode);
		ps.setString(2, branchcode);
		ps.setString(3, sbuCode);
		ps.setString(4, subcategoryCode);
		try(ResultSet rs = ps.executeQuery();) {
        while (rs.next()) {
            result[0] = rs.getDouble("Q1_ALLOCATION");
            result[1] = rs.getDouble("Q1_ACTUAL");
            result[2] = rs.getDouble("Q2_ALLOCATION");
            result[3] = rs.getDouble("Q2_ACTUAL");
            result[4] = rs.getDouble("Q3_ALLOCATION");
            result[5] = rs.getDouble("Q3_ACTUAL");
            result[6] = rs.getDouble("Q4_ALLOCATION");
            result[7] = rs.getDouble("Q4_ACTUAL");
            result[8] = rs.getDouble("BALANCE_ALLOCATION");
            result[9] = rs.getDouble("TOTAL_ACTUAL");
            //result[4] = rs.getDouble("quarterly_surplus_cf");

        }
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
//    System.out.println("getting quarters");
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
           "WHERE BRANCH_ID = ? ";

     
   String branchcode = "0";
   try(Connection conn = getConnection();
	          PreparedStatement s = conn.prepareStatement(query);) {
		s.setString(1, BranchId);
		try (ResultSet rs = s.executeQuery()) {
       while (rs.next()) {

           branchcode = rs.getString(1);

       }
		}
   } catch (Exception ex) {
       ex.printStackTrace();
   } 

   return branchcode;

}
public String getDeptCode(String DeptId)
{
   String query =
          "SELECT DEPT_CODE  FROM am_ad_department  " +
          "WHERE DEPT_ID = ? ";

   
  String deptcode = "0";
  try(Connection conn = getConnection();
          PreparedStatement s = conn.prepareStatement(query);) {
		s.setString(1, DeptId);
		try (ResultSet rs = s.executeQuery()) {
      while (rs.next()) {

          deptcode = rs.getString(1);

      }
		}

  } catch (Exception ex) {
      ex.printStackTrace();
  } 

  return deptcode;

}
public String getSectionCode(String SectionId)
 {
     String query =
            "SELECT SECTION_CODE  FROM am_ad_section  " +
            "WHERE SECTION_ID = ? ";

      
    String sectioncode = "0";
    try(Connection conn = getConnection();
            PreparedStatement s = conn.prepareStatement(query);) {
		s.setString(1, SectionId);
		try (ResultSet rs = s.executeQuery()) {
        while (rs.next()) {

            sectioncode = rs.getString(1);

        }
		}
    } catch (Exception ex) {
        ex.printStackTrace();
    } 

    return sectioncode;

 }
 public String getCategoryCode(String categoryId)
     {
         String query =
             "SELECT CATEGORY_CODE  FROM am_ad_category  " +
            "WHERE category_id = ? ";

         
        String categorycode = "0";
        try(Connection conn = getConnection();
             PreparedStatement s = conn.prepareStatement(query);) {
    		s.setString(1, categoryId);
    		try (ResultSet rs = s.executeQuery()) {
            while (rs.next()) {

                categorycode = rs.getString(1);

            }
    		}

        } catch (Exception ex) {
            ex.printStackTrace();
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
    	String identity = "";
        Connection con = getConnection();
        Statement stmt = null;
  try
  {
        StringBuffer sb = new StringBuffer(100);

        String v1 = "", v2 = "", v3 = "", v4 = "", v5 = "", v6 = "", v7 = "",
                dl = "";
        int curr = 0;


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

            } catch (Exception ex) {
                System.out.println("WARN: Error fetching getIdentity ->" + ex);
            } finally {
               closeConnection(con, stmt);
            }
 //       closeConnection(con, ps);
        return identity;
    }


    public Statement getStatement() throws Exception {
//    	System.out.println("======this.con value: "+this.con);
        if (this.con!= null) {
            return this.con.createStatement();
        } else if(this.con== null) {
            return getConnection().createStatement();
        }
        else{
            return null;
        }
    }
    public java.sql.Date dateConvert(java.util.Date inputDate) {

        return this.getSQLFormatedDate(inputDate);

    }

//    public String findObject(String query)
//    {
////    	System.out.println("====findObject query=====  "+query);
//        Connection Con2 = null;
//        PreparedStatement Stat = null;
//        ResultSet result = null;
//        String found = null;
//
//        //String finder = "UNKNOWN";
//        String finder = "";
//
//       // double sequence = 0.00d;
//        try {
//
//        	Con2 = getConnection();
//            Stat = Con2.prepareStatement(query);
//            result = Stat.executeQuery();
//
//            while (result.next()) {
//                finder = result.getString(1);
//            }
//
//        } catch (Exception ee2) {
//            System.out.println("WARN:ERROR OBTAINING OBJ --> " + ee2);
//            ee2.printStackTrace();
//        } finally {
//            closeConnection(Con2, Stat, result);
//        }
//
//        return finder;
//    }

    public String findObject(String query) {
        String resultValue = null;

        try (
            Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                resultValue = rs.getString(1);
            }

        } catch (Exception e) {
            System.out.println("WARN: ERROR OBTAINING OBJ --> " + e.getMessage());
            e.printStackTrace();
        }

        return resultValue;
    }


//    public boolean deleteObject(String query)
//    {
//    	//System.out.println("====findObject query=====  "+query);
//    	boolean done = false;
//        Connection Con2 = null;
//        PreparedStatement Stat = null;
//        ResultSet result = null;
//
//        try {
//
//        	Con2 = getConnection();
//			ps = Con2.prepareStatement(query);
//			done = (ps.executeUpdate() != -1);
//
//        } catch (Exception ee2) {
//            System.out.println("WARN:ERROR deleteObject --> " + ee2);
//            ee2.printStackTrace();
//        } finally {
//            closeConnection(Con2, Stat);
//        }
//
//        return done;
//    }


    public boolean deleteObject(String query) {
        try (
            Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(query)
        ) {
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            System.out.println("WARN: ERROR deleteObject --> " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


	public boolean newassetinterfaceOld(String errormessage,String integrifyId,String status,String assetid,String assetcode) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "";
//		System.out.println("errormessage: "+errormessage+" integrifyId: "+integrifyId+" status: "+status+" assetid: "+assetid+" assetcode: "+assetcode);
		if(status.equalsIgnoreCase("Y")){
		query = "UPDATE NEW_ASSET_INTERFACE"
				+ " SET ERROR_MESSAGE = '"+errormessage+"',POSTED='"+status+"',ASSET_ID = '"+assetid.trim()+"',"
				+ " ASSET_CODE = '"+assetcode.trim()+"' "
				+ " WHERE INTEGRIFY_ID = '"+integrifyId+"'";
//		System.out.println("query: "+query);
		}
		if(!status.equalsIgnoreCase("Y")){
		query = "UPDATE NEW_ASSET_INTERFACE"
			+ " SET ERROR_MESSAGE = '"+errormessage+"',POSTED='"+status+"' "
			+ " WHERE INTEGRIFY_ID = '"+integrifyId+"'";
//		System.out.println("query2: "+query);
		}
		try {
			con = getConnection();
		//	if(status.equalsIgnoreCase("Y")){
			ps = con.prepareStatement(query);//}
			//else{ps = con.prepareStatement(query2);}
			done = (ps.executeUpdate() != -1);
//			closeConnection(con, ps);
		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
//		closeConnection(con, ps);
		return done;

	}

	public boolean newassetinterface(String errorMessage, String integrifyId, String status, String assetId, String assetCode) {
	    String queryWithAsset = "UPDATE NEW_ASSET_INTERFACE " +
	            "SET ERROR_MESSAGE = ?, POSTED = ?, ASSET_ID = ?, ASSET_CODE = ? " +
	            "WHERE INTEGRIFY_ID = ?";

	    String queryWithoutAsset = "UPDATE NEW_ASSET_INTERFACE " +
	            "SET ERROR_MESSAGE = ?, POSTED = ? " +
	            "WHERE INTEGRIFY_ID = ?";

	    boolean done = false;

	    try (Connection con = getConnection();
	         PreparedStatement ps = con.prepareStatement(status.equalsIgnoreCase("Y") ? queryWithAsset : queryWithoutAsset)) {

	        if (status.equalsIgnoreCase("Y")) {
	            ps.setString(1, errorMessage);
	            ps.setString(2, status);
	            ps.setString(3, assetId != null ? assetId.trim() : "");
	            ps.setString(4, assetCode != null ? assetCode.trim() : "");
	            ps.setString(5, integrifyId);
	        } else {
	            ps.setString(1, errorMessage);
	            ps.setString(2, status);
	            ps.setString(3, integrifyId);
	        }

	        done = ps.executeUpdate() > 0;

	    } catch (Exception e) {
	        System.out.println("WARNING: Error executing newAssetInterface -> " + e.getMessage());
	        e.printStackTrace();
	    }

	    return done;
	}


	public void insToAm_Invoice_No(String assetID,String lpo,String invoiceNo,String TransType,String integrifyid)
	    {

	        Connection Con2 = null;
	        PreparedStatement Stat = null;
	        ResultSet result = null;
	        String found = null;

	        String query="Insert into Am_Invoice_no (asset_id,lpo,invoice_no,trans_type,integrify_Id,CREATE_DATE) values (?,?,?,?,?,?)";

	             try
	             {
	            Con2 = getConnection();
	            Stat = Con2.prepareStatement(query);
	            Stat.setString(1, assetID);
	            Stat.setString(2, lpo);
	            Stat.setString(3, invoiceNo);
	            Stat.setString(4, TransType);
	            Stat.setString(5, integrifyid);
	            Stat.setString(6, String.valueOf(df.dateConvert(new java.util.Date())));
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
//    	System.out.println("====findObject query=====  "+query);
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
//        System.out.println("insertApprovalx Description: "+description);
        String query = "INSERT INTO am_raisentry_post(Id,Description,Page,Flag,partPay,"
                + "UserId,Branch,subjectToVat,whTax,url,trans_Id,asset_code,Posting_date,entryPostFlag,GroupIdStatus)"
                + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
            ps.setString(13, String.valueOf(df.dateConvert(new java.util.Date())));
            ps.setString(14, "N");
            ps.setString(15, "N");
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
                " description,effective_date,BRANCH_CODE,Asset_Status from am_asset where asset_id = ?";
                tranType = "Asset Creation";
            }
            if(assettype.equalsIgnoreCase("P")){
                query = "select asset_id,user_ID,supervisor,Cost_Price,Posting_Date," +
                " description,effective_date,BRANCH_CODE,Asset_Status from am_asset where asset_id = ?";
                tranType = "Asset Creation";
            }
             if(assettype.equalsIgnoreCase("U")){
                 query = "select asset_id,user_ID,supervisor,Cost_Price,Posting_Date," +
                 " description,effective_date,BRANCH_CODE,Asset_Status from AM_ASSET_UNCAPITALIZED where asset_id = ?";
                 tranType = "Asset Creation Uncapitalized";
             }

            try {
                con = getConnection();
                ps = con.prepareStatement(query);
                ps.setString(1, id);
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
 //               System.out.println("Query setApprovalData "+query);
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

    @SuppressWarnings("finally")
	public String setPendingTrans(String[] a, String code,int assetCode){
    	String resp="";
        int transaction_level=0;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
 String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description," +
         "effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time,transaction_id,batch_id," +
         "transaction_level,asset_code) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 String tranLevelQuery = "select level from approval_level_setup where code = ?";
// System.out.println("tranLevelQuery in setPendingTrans: "+tranLevelQuery);
        con = null;
        ps = null;
        rs = null;
        try
        {
            con = getConnection();


            /////////////To get transaction level
             ps = con.prepareStatement(tranLevelQuery);
             ps.setString(1, code);
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
            System.out.println("a[6] in setPendingTrans===: "+a[6]);
            String dd = a[6].substring(0,2);
            String mm = a[6].substring(3,5);
            String yyyy = a[6].substring(6,10);
            String effDate = yyyy+"-"+mm+"-"+dd;
            effDate = "2021-06-25";
            SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");

            String mtid =  new ApplicationHelper().getGeneratedId("am_asset_approval");
            ps.setString(1, (a[0]==null)?"":a[0]);
            ps.setString(2, (a[1]==null)?"":a[1]);
            ps.setString(3, (a[2]==null)?"":a[2]);
            ps.setDouble(4, (a[3]==null)?0:Double.parseDouble(a[3]));
            //ps.setDate(5, (a[4])==null?null:dbConnection.dateConvert(a[4]));
//            System.out.println("effDate in setPendingTrans====: "+effDate);
            ps.setTimestamp(5,getDateTime(new java.util.Date()));
            ps.setString(6, (a[5]==null)?"":a[5]);
            ps.setString(7,effDate);
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

            resp= mtid;
            System.out.println(">>>Reps in AssetRecordBeans:setPendingTrans(>>>>>>" + resp);
        }
        catch(Exception er)
        {
            System.out.println(">>>AssetRecordBeans:setPendingTrans(>>>>>>" + er);

        }finally{
        closeConnection(con, ps);

        	return resp;
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
   String tranLevelQuery = "select level from approval_level_setup where code =?";
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
               ps.setString(1, code);
                rs = ps.executeQuery();


              while(rs.next()){
              transaction_level = rs.getInt(1);

              }//if



              ////////////To set values for approval table

              ps = con.prepareStatement(pq);
//              System.out.println("a[6] in setPendingTransArchive===: "+a[6]);
              String dd = a[6].substring(0,2);
              String mm = a[6].substring(3,5);
              String yyyy = a[6].substring(6,10);
              String effDate = yyyy+"-"+mm+"-"+dd;
              effDate = "2021-06-25";
              SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");
//              System.out.println("effDate in setPendingTransArchive====: "+effDate);
              //String mtid =  new ApplicationHelper().getGeneratedId("am_asset_approval");
              ps.setString(1, (a[0]==null)?"":a[0]);
              ps.setString(2, (a[1]==null)?"":a[1]);
              ps.setString(3, (a[2]==null)?"":a[2]);
              ps.setDouble(4, (a[3]==null)?0:Double.parseDouble(a[3]));
              //ps.setDate(5, (a[4])==null?null:dbConnection.dateConvert(a[4]));
              ps.setTimestamp(5,getDateTime(new java.util.Date()));
              ps.setString(6, (a[5]==null)?"":a[5]);
              ps.setString(7,effDate);
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

    public void sendMailSupervisor(String id, String subject, String msgText1) {
        try {
            Properties prop = new Properties();
            try (FileInputStream in = new FileInputStream("C:\\Property\\Inventory.properties")) {
                prop.load(in);
            }

            String host = prop.getProperty("mail.smtp.host");
            String from = prop.getProperty("mail-user");
            String mailAuthenticator = prop.getProperty("mailAuthenticator");
            String to = userEmail(id);

            if (to == null || to.trim().isEmpty()) {
                System.out.println("No email address found for ID: " + id);
                return;
            }

            Session session;
            Message msg = new MimeMessage(Session.getDefaultInstance(prop));
            msg.setFrom(new InternetAddress(from));
            msg.setRecipients(Message.RecipientType.TO, new InternetAddress[]{ new InternetAddress(to.trim()) });
            msg.setSubject("Legend - " + subject);
            msg.setSentDate(new Date());
            msg.setText(msgText1);
            msg.saveChanges();

            if ("Y".equalsIgnoreCase(mailAuthenticator)) {
                final String user = prop.getProperty("mail-user");
                final String password = prop.getProperty("mail-password");
                String port = prop.getProperty("mail.smtp.port");
                String protocol = prop.getProperty("mail.smtp.ssl.protocols");

                Properties authProps = new Properties();
                authProps.put("mail.smtp.host", host);
                authProps.put("mail.smtp.port", port);
                authProps.put("mail.smtp.auth", "true");
                authProps.put("mail.smtp.starttls.enable", "true");
                authProps.put("mail.smtp.ssl.protocols", protocol);
                authProps.put("mail.smtp.ssl.trust", host);

                session = Session.getInstance(authProps, new jakarta.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user, password);
                    }
                });

                Transport.send(msg);
                System.out.println("Supervisor email sent (authenticated).");

            } else {
                String port = prop.getProperty("mail.smtp.port");

                Properties noAuthProps = new Properties();
                noAuthProps.put("mail.smtp.host", host);
                noAuthProps.put("mail.smtp.port", port);
                noAuthProps.put("mail.smtp.auth", "false");

                session = Session.getDefaultInstance(noAuthProps, null);
                Transport transport = session.getTransport("smtp");
                transport.connect();
                transport.sendMessage(msg, msg.getAllRecipients());
                transport.close();

                System.out.println("Supervisor email sent (no authentication).");
            }

        } catch (Exception ex) {
            System.out.println("Error in sendMailSupervisor");
            ex.printStackTrace();
        }
    }


    public String userEmail(String user_id) {

       
        String email = "";

        String FINDER_QUERY = "SELECT email from am_gb_user WHERE user_id = ? ";

        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(FINDER_QUERY)) {
          
            ps.setString(1, user_id);

            try(ResultSet rs = ps.executeQuery()){

            while (rs.next()) {
                email = rs.getString("email");
            }
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
       

        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {
          
                       ps.setString(1, tableName);
                       try(ResultSet rs = ps.executeQuery()){

            while (rs.next()) {

                mtid = rs.getString(1);

            }
                      }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(con, ps, rs);
        }

        return mtid;

    }

    public boolean updateNewAssetStatuxOld(String assetId,String tablename) throws Exception {

            String query = "update "+tablename+" SET  asset_status = 'APPROVED' where asset_id = ?";
             boolean done = true;
            Connection con = null;
            PreparedStatement ps = null;

            try {con = getConnection();
                ps = con.prepareStatement(query);
                ps.setString(1, assetId);
                rs = ps.executeQuery();
//                ps.execute();

            } catch (Exception ex) {
                done = false;
                System.out.println("AssetRecordsBean: updateNewAssetStatux(): WARN:Error updating asset->" + ex);
            } finally {
                closeConnection(con, ps);
            }
            return done;
        }
    

    public boolean updateNewAssetStatux(String assetId, String tablename) {

        String query = "UPDATE " + tablename +
                       " SET asset_status = 'APPROVED' " +
                       " WHERE asset_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, assetId);

            return ps.executeUpdate() > 0;

        } catch (Exception ex) {
            System.out.println("updateNewAssetStatus ERROR -> " + ex.getMessage());
            return false;
        }
    }

    public int getNumOfTransactionLevel(String levelCode){
       String query = "select level from approval_level_setup where code = ?";
       int result=0;
       try (Connection conn = getConnection();
               PreparedStatement ps = conn.prepareStatement(query);
              ) {
                ps.setString(1, levelCode);
               try(ResultSet rs = ps.executeQuery()){
                while (rs.next()) {
                    result = rs.getInt(1);

                }
               }
            } catch (Exception ex) {
                System.out.println("AssetRecordsBean: getNumOfTransactionLevel(String levelCode) WARN: Error fetching CategoryCode ->" + ex);
            } finally {
                closeConnection(con, ps);
            }
//           closeConnection(con, ps);
            return result;
       } //getNumOfTransactionLevel()

    public boolean updateNewApprovalAssetStatus(String groupID, int supervise) throws Exception {

            /*String query = "update am_asset_approval set  asset_status = ?, process_status=?, " +
            		"super_id =?, approval1 =? where asset_id =?";*/
            String query = "update am_asset_approval set  asset_status = ?, process_status=?, " +
    		"super_id =?, approval1 =? where asset_id =?";
             boolean done = true;
          

            try (Connection conn = getConnection();
                    PreparedStatement ps = conn.prepareStatement(query);) {
                ps.setString(1, "ACTIVE");
                ps.setString(2, "A");
                ps.setInt(3, supervise);
                ps.setInt(4, supervise);
                ps.setString(5,groupID);
                ps.execute();

            } catch (Exception ex) {
                done = false;
                System.out.println("AssetRecordsBean: updateNewApprovalAssetStatus: WARN:Error updating asset->" + ex);
            } 
 //           closeConnection(con, ps);
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
 //           closeConnection(con, ps);
            return result;
    }

    public String whTax(String id){
    String result="";
        Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

             String query =
                    "SELECT wh_tax FROM am_asset  " +
                    "WHERE asset_id = ? ";


            try {
                con = getConnection();
                ps = con.prepareStatement(query);
                ps.setString(1, id);
                rs = ps.executeQuery();
                while (rs.next()) {
                    result = rs.getString(1);
                }

            } catch (Exception ex) {
                System.out.println("WARN: Error fetching WHTAX ->" + ex);
            } finally {
               closeConnection(con, ps);
            }
 //           closeConnection(con, ps);
            return result;
    }

    public void updateAssetStatusChangeOld(String query_r){
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
//    	closeConnection(con, ps);
    }

    public void updateAssetStatusChange(String query) {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            int rowsUpdated = ps.executeUpdate();
            System.out.println("updateAssetStatusChange: Rows affected = " + rowsUpdated);

        } catch (Exception ex) {
        	 System.out.println("Error in updateAssetStatusChange" + ex);
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
 //           closeConnection(con, ps);
        } catch (Exception ex) {
            System.out.println("WARNING: cannot update am_asset+" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
//        closeConnection(con, ps);
    }

    public String MailMessage(String Mail_Code,String Transaction_Type)
	{
		String message="";
		String query="SELECT Mail_Description FROM am_mail_statement where Mail_Code=? and Transaction_Type= ?";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
            ps.setString(1, Mail_Code);
            ps.setString(2, Transaction_Type);
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
//		 closeConnection(con, ps);
	return 	message;
	}
    public String MailTo(String Mail_Code,String Transaction_Type)
  	{
  		String to="";
  		String query="SELECT mail_address FROM am_mail_statement where Mail_Code= ? and Transaction_Type= ?";

  		Connection con = null;
  		PreparedStatement ps = null;
  		ResultSet rs = null;
  		try {
  			con = getConnection();
  			ps = con.prepareStatement(query);
            ps.setString(1, Mail_Code);
            ps.setString(2, Transaction_Type);
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
 // 		 closeConnection(con, ps);
  	return 	to;
  	}

    public void sendMail(String email, String subject, String msgText1) {
        try {
            // Load mail properties
            Properties prop = new Properties();
            try (FileInputStream in = new FileInputStream("C:\\Property\\LegendPlus.properties")) {
                prop.load(in);
            }

            String host = prop.getProperty("mail.smtp.host");
            String from = prop.getProperty("mail-user");
            String mailAuthenticator = prop.getProperty("mailAuthenticator");

            String[] recipients = email.split(",");
            String to = recipients[0];
            System.out.println("Mail To: " + to + "     Mail from: " + from);

            Session session;
            Message msg = new MimeMessage(Session.getDefaultInstance(prop));
            msg.setFrom(new InternetAddress(from));
            msg.setRecipients(Message.RecipientType.TO, new InternetAddress[]{ new InternetAddress(to) });
            msg.setSubject("Legend - " + subject);
            msg.setSentDate(new Date());
            msg.setText(msgText1); // Plain text content

            // Add CCs (if any)
            for (int i = 1; i < recipients.length; i++) {
                InternetAddress addressCopy = new InternetAddress(recipients[i].trim());
                msg.addRecipient(Message.RecipientType.CC, addressCopy);
            }

            // Handle authenticated vs non-authenticated sending
            if ("Y".equalsIgnoreCase(mailAuthenticator)) {
                final String user = prop.getProperty("mail-user");
                final String password = prop.getProperty("mail-password");
                String port = prop.getProperty("mail.smtp.port");
                String protocol = prop.getProperty("mail.smtp.ssl.protocols");

                Properties authProps = new Properties();
                authProps.put("mail.smtp.host", host);
                authProps.put("mail.smtp.port", port);
                authProps.put("mail.smtp.auth", "true");
                authProps.put("mail.smtp.starttls.enable", "true");
                authProps.put("mail.smtp.ssl.protocols", protocol);
                authProps.put("mail.smtp.ssl.trust", host);

                session = Session.getInstance(authProps, new jakarta.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user, password);
                    }
                });

                Transport.send(msg);
                System.out.println("Email Sent (authenticated).");

            } else {
                String port = prop.getProperty("mail.smtp.port");
                Properties noAuthProps = new Properties();
                noAuthProps.put("mail.smtp.host", host);
                noAuthProps.put("mail.smtp.port", port);
                noAuthProps.put("mail.smtp.auth", "false");

                session = Session.getDefaultInstance(noAuthProps, null);
                Transport transport = session.getTransport("smtp");
                transport.connect();
                transport.sendMessage(msg, msg.getAllRecipients());
                transport.close();
                System.out.println("Email Sent (no authentication)." );
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


	String section = "";
    private void rinsertAssetRecord2(String integrifyId,String Description,String RegistrationNo,String VendorAC,String Datepurchased,
    		String make,String AssetModel,String AssetSerialNo,String AssetEngineNo,String supplied_by,String user,
    		String maintained_by,double CostPrice,double VatableCost,String AuthorizedBy,String WhTax,double whtaxamount,String PostingDate,String EffectiveDate,
    		String PurchaseReason,String SubjectTOVat,String AssetStatus,String state,String driver,String UserID,String branchCode,String reqdepreciation,
    		String sectionCode,String deptCode,String categoryCode,String subcategoryCode,String barCode,String sbuCode,String lpo,String invoiceNo,String supervisor,
    		String posted,String assetId,int assetCode,String assettype,String branch_id,String department_id,String section_id,String depreciation_end_date,
    		String section,String spare_1,String spare_2,String status,String partPAY,String fullyPAID,
    		String deferPay,String systemIp,String macAddress,String who_to_remind,String email_1,String who_to_remind_2,String email2,String raise_entry,
    		String noOfMonths,String warrantyStartDate,String expiryDate,String category_id,String subcategory_id,double vatamount,String residual_value,int whtaxvalue,
    		String location,String memovalue,String memo, String spare1,String spare2, String spare3,String spare4, String spare5,String spare6,String multiple,String groupId) throws Exception, Throwable {

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
    if (category_id == null || category_id.equals("")) {
        category_id = "0";
    }
    if (subcategory_id == null || subcategory_id.equals("")) {
        subcategory_id = "0";
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
    if (groupId == null || groupId.equals("")) {
    	groupId = "0";
    }

    String require_redistribution = "N";
    String amountPTD = "0.00";
    String province = "0";
//    System.out.println("Whholding Tax Rate before AM_ASSET_ARCHIVE: "+whtaxvalue+"  assetCode: "+assetCode);
    //vat_amount = vat_amount.replaceAll(",", "");
    //vatable_cost = vatable_cost.replaceAll(",", "");
    //wh_tax_amount = wh_tax_amount.replaceAll(",", "");
   // residual_value = residual_value.replaceAll(",", "");
   // amountPTD = amountPTD.replaceAll(",","");
    String createQuery = "INSERT INTO AM_ASSET_ARCHIVE        " +
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
                         "BRANCH_CODE,DEPT_CODE,SECTION_CODE,CATEGORY_CODE,AMOUNT_PTD," +
                         "AMOUNT_REM,PART_PAY,FULLY_PAID,BAR_CODE,SBU_CODE,LPO,supervisor,"+
                         "defer_pay,wht_percent,system_ip,mac_address,asset_code,SUB_CATEGORY_ID,SUB_CATEGORY_CODE, SPARE_3, SPARE_4, SPARE_5, SPARE_6,GROUP_ID) " +
                         "VALUES" +
                         "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                          "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
        ps.setDouble(21, (CostPrice-10.00));
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
        ps.setInt(74, Integer.parseInt(subcategory_id));
        ps.setString(75,subcategoryCode);
        ps.setString(76, spare3);
        ps.setString(77, spare4);
        ps.setString(78, spare5);
        ps.setString(79, spare6);
        ps.setString(80, groupId);

        ps.execute();
 //       closeConnection(con, ps);
    } catch (Exception ex) {
        done = false;
        System.out.println("WARN:Error inserting into  asset creation archive->" + ex);
    } finally {
        closeConnection(con, ps);
    }
//    closeConnection(con, ps);
   // return done;
}

    public Asset getAsset(String assetId) {
        String selectQuery = "SELECT * FROM AM_ASSET_MAIN WHERE ASSET_ID = ? ";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Asset _obj = null;

        try {
            con = getConnection();
            ps = con.prepareStatement(selectQuery);
            ps.setString(1, assetId);
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
//            closeConnection(con, ps, rs);
        } catch (Exception e) {
            System.out.println("INFO:Error fetching Asset BY ID ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
 //       closeConnection(con, ps, rs);
        return _obj;

    }

    //get Maintenance asset by ID
    public Improvement getMaintenanceAsset(String id) {

        String query =
                "SELECT REVALUE_ID,ASSET_ID,COST_INCREASE,REVALUE_REASON,REVALUE_DATE," +
                "USER_ID,RAISE_ENTRY,R_VENDOR_AC,COST_PRICE,VATABLE_COST,VAT_AMOUNT," +
                "WHT_AMOUNT,NBV,ACCUM_DEP,OLD_COST_PRICE,OLD_VATABLE_COST,OLD_VAT_AMOUNT," +
                "OLD_WHT_AMOUNT,OLD_NBV,OLD_ACCUM_DEP,EFFDATE FROM am_asset_improvement " +
                "WHERE ASSET_ID = ?";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        ArrayList list = new ArrayList();
        //declare DTO object
        Improvement improve = null;

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, id);
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
 //           closeConnection(con, ps, rs);
        } catch (Exception e) {
            System.out.println("INFO:Error fetching am_asset_improvement Asset by ID ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
 //       closeConnection(con, ps, rs);
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
     String tranLevelQuery = "select level from approval_level_setup where code = ?";
            con = null;
            ps = null;
            rs = null;
            try
            {
                con = getConnection();


                /////////////To get transaction level
                 ps = con.prepareStatement(tranLevelQuery);
                 ps.setString(1, code);
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
//                closeConnection(con, ps);
            }
            catch(Exception er)
            {
                System.out.println(">>>AssetRecordBeans:setPendingTrans(>>>>>>" + er);

            }finally{
            closeConnection(con, ps);

            }
 //           closeConnection(con, ps);
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
                 String vendorId,String vendorIdOld,String vendorAccOld,String description,String categoryCode,String subcategoryCode, String
                 branchCode,String lpo,String invoiceNo, double newCP,int assetCode,double newCostPrice,
                 double newVatAmt,double newWhtAmt,double newVatableCost,double newNbv, String integrifyId,int usefullife,String sbuCode) {
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
      "new_cost_price,asset_code,NEW_VATABLE_COST,NEW_NBV,NEW_VAT_AMOUNT,NEW_WHT_AMOUNT,INTEGRIFY,IMPROV_USEFULLIFE,IMPROVED,SBU_CODE)"+
     " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
     ps.setInt(40, usefullife);
     ps.setString(41, "Y");
     ps.setString(42, sbuCode);
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
     String warning = "WARNING:Error inserting am_asset_improvement Revaluation And am_asset_improvement->" +
            e.getMessage();
     System.out.println(warning);
     e.printStackTrace();
     } finally {
     closeConnection(con, ps);
     }
//     closeConnection(con, ps);
     return i;
     }
    public String[] raiseEntryInfo(String asset_id) {
        String[] result = new String[4];
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        String query = "select description, branch_id, subject_to_vat, wh_tax from am_asset "
                + "where asset_id = ?";
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, asset_id);
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
//        closeConnection(con, ps);
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
                 String vendorAccOld, String description,String categoryCode,String branchCode,String lpo,String invoiceNum,int assetCode,String integrify,int usefullife,String sbuCode) {

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
     "R_VENDOR_ID,OLD_VENDOR_ACC,OLD_VENDOR_ID,DESCRIPTION,CATEGORY_CODE,BRANCH_CODE,lpoNum,invoice_no,INTEGRIFY,asset_code,IMPROV_USEFULLIFE,IMPROVED,SBU_CODE)"+
     " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

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
     ps.setInt(40, usefullife);
     ps.setString(41, "Y");
     ps.setString(42, sbuCode);
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
 //    closeConnection(con, ps);
     return i;
     }
    public boolean insertApproval(String id, String description, String page, String flag,
            String partPay, String UserId, String Branch, String subjectToVat,
            String whTax, String url,String tranId,int assetCode) {
        boolean done = true;
        flag = "Y";
        Connection con = null;
        PreparedStatement ps = null;
        String query = "INSERT INTO am_raisentry_post(Id,Description,Page,Flag,partPay,"
                + "UserId,Branch,subjectToVat,whTax,url,trans_id,Posting_date,entryPostFlag,GroupIdStatus,asset_code)"
                + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
            ps.setString(12, String.valueOf(df.dateConvert(new java.util.Date())));
            ps.setString(13, "N");
            ps.setString(14, "N");
            ps.setInt(15, assetCode);
            ps.execute();

        } catch (Exception ex) {
            done = false;
            System.out.println(
                    "WARNING:cannot insert am_raisentry_post->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
 //       closeConnection(con, ps);
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
//        closeConnection(con, ps);
        return i;
    }


	public String createGroup(String integrifyId,String Description,String RegistrationNo,String VendorAC,String Datepurchased,
			String AssetMake,String AssetModel,String AssetSerialNo,String AssetEngineNo,String SupplierName,String AssetUser,
			String AssetMaintenance,double CostPrice,double VatableCost,String AuthorizedBy,String WhTax,double whtaxamount,String PostingDate,String EffectiveDate,
			String PurchaseReason,String SubjectTOVat,String AssetStatus,String State,String Driver,String UserID,String branchCode,
			String sectionCode,String deptCode,String categoryCode,String subcategoryCode,String barCode,String sbuCode,String lpo,String invoiceNo,String supervisor,
			String posted,String assetId,String assetCode,String assettype,String branch_id,String dept_id,String section_id,
			String category_id,String subcategory_id,double vatamount,String residualvalue,int whtaxvalue,int noofitems,
			String location,String memovalue,String memo, String spare1,String spare2, String spare3,String spare4, String spare5,String spare6,String multiple) throws Exception,Throwable
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

//		String integrify = newassettrans.getIntegrifyId();
//		System.out.println(">>>>>>>>> INSIDE CREATE GROUP IN GROUP ASSET BEAN <<<<<<<<<< ");
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
//	    String multiple = "N";
	    String spare_2 = "";
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
		String gid = createGroupMain(integrifyId,Description,RegistrationNo,VendorAC,Datepurchased,
				AssetMake,AssetModel,AssetSerialNo,AssetEngineNo,SupplierName,AssetUser,
				AssetMaintenance,CostPrice,VatableCost,AuthorizedBy,WhTax,whtaxamount,PostingDate,depreciation_start_date,
				PurchaseReason,SubjectTOVat,AssetStatus,State,Driver,UserID,branchCode,
				sectionCode,deptCode,categoryCode,barCode,sbuCode,lpo,invoiceNo,supervisor,
				posted,assetId,assetCode,assettype,branch_id,dept_id,section_id,category_id,vatamount,
				residualvalue,whtaxvalue,noofitems,warrantyStartDate,expiryDate,depreciation_end_date,
				SystemIp,require_depreciation,accum_dep,rate,require_redistribution);
		String groupexist = findObject("SELECT DISTINCT GROUP_ID FROM AM_GROUP_ASSET_MAIN WHERE GROUP_ID = '"+gid+"'");
//		System.out.println(">>>>>>>>>  GENERATED groupexist IN createGroupMain <<<<<<<<<< " + groupexist+"  itemsCount: "+itemsCount);
		try {
//			if(groupexist.equals("")){
		for (int x = 0; x < itemsCount; x++)
		{
			reccount = reccount +1;
//			try {

			//asset_id = new legend.AutoIDSetup().getIdentity(branch_id,department_id, section_id, category_id);
//			String asset_id = new ApplicationHelper().getGeneratedId("am_group_asset");
            String asset_id = new ApplicationHelper().getGeneratedId("AM_GROUP_ASSET_MAIN");
                        con = getConnection();

                        String query = "";
//		System.out.println(">>>>>>>>>  GENERATED ASSET_ID IN createGroupMain <<<<<<<<<< " + asset_id+"   assettype: "+assettype);
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
			+ "EMAIL2,	STATE,DRIVER,SPARE_1,SPARE_2,USER_ID, PROVINCE,"
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
				+ "EMAIL2,	STATE,DRIVER,SPARE_1,SPARE_2,USER_ID, PROVINCE,"
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
//		ps1.setLong(49, gid);
		ps1.setString(49, gid);
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
//        System.out.println("=====Result End=====?  "+result);
	archiveUpdate(asset_id,itemsCount,gid,integrifyId,Description,RegistrationNo,VendorAC,Datepurchased,
			AssetMake,AssetModel,AssetSerialNo,AssetEngineNo,SupplierName,AssetUser,
			AssetMaintenance,CostPrice,VatableCost,AuthorizedBy,WhTax,whtaxamount,PostingDate,EffectiveDate,
			PurchaseReason,SubjectTOVat,AssetStatus,State,Driver,UserID,branchCode,
			sectionCode,deptCode,categoryCode,barCode,sbuCode,lpo,invoiceNo,supervisor,
			posted,assetId,assetCode,assettype,branch_id,dept_id,section_id,category_id,
			vatamount,residualvalue,whtaxvalue,noofitems,depreciation_end_date,SystemIp,require_depreciation,
			accum_dep,rate,require_redistribution,reccount);
//	 System.out.println("=====Result End=====?  "+result);
			}
//		}
		}
                        catch (Exception r) {
				System.out.println("INFO: Error creating group aset >>" + r);
			} finally {
//				freeResource();
				 closeConnection(con, ps1);
			}
//        }
		//ad.updateGroupAssetStatus(Long.toString(gid));
//		freeResource();
		return gid;
	}



        public void archiveUpdate(String asset_id,int itemsCount,String gid,String integrifyId,String Description,String RegistrationNo,String VendorAC,String Datepurchased,
    			String AssetMake,String AssetModel,String AssetSerialNo,String AssetEngineNo,String SupplierName,String AssetUser,
    			String AssetMaintenance,double CostPrice,double VatableCost,String AuthorizedBy,String WhTax,double whtaxamount,String PostingDate,String EffectiveDate,
    			String PurchaseReason,String SubjectTOVat,String AssetStatus,String State,String Driver,String UserID,String branchCode,
    			String sectionCode,String deptCode,String categoryCode,String barCode,String sbuCode,String lpo,String invoiceNo,String supervisor,
    			String posted,String assetId,String assetCode,String assettype,String branch_id,String dept_id,String section_id,
    			String category_id,double vatamount,String residualvalue,int whtaxvalue,int recordNo,String depreciation_end_date,
    			String SystemIp,String require_depreciation,double accum_dep,String rate,String require_redistribution,int reccount)
        {
        	PreparedStatement ps = null;
            Codes code = new Codes();
            String amountPTD = "0.0";
    	    String multiple = "N";
    	    String spare_2 = "";
    	    String partPAY = "N";
    	    String fullyPAID = "Y";
    	    String deferPay = "N";
    	    String who_to_remind = "";
    	    String email_1 = "";
    	    String who_to_remind_2 = "";
    	    String email2 = "";
    	    String raise_entry = "N";
    	    String spare_1 = "";
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
			+ "EMAIL2,	STATE,DRIVER,SPARE_1,SPARE_2,USER_ID, PROVINCE,"
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
	//	ps.setLong(49, gid);
		ps.setString(49, gid);
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
//            closeConnection(con, ps);
        }


    	public String createGroupMain(String integrifyId,String Description,String RegistrationNo,String VendorAC,String Datepurchased,
    			String AssetMake,String AssetModel,String AssetSerialNo,String AssetEngineNo,String SupplierName,String AssetUser,
    			String AssetMaintenance,double CostPrice,double VatableCost,String AuthorizedBy,String WhTax,double whtaxamount,String PostingDate,String EffectiveDate,
    			String PurchaseReason,String SubjectTOVat,String AssetStatus,String State,String Driver,String UserID,String branchCode,
    			String sectionCode,String deptCode,String categoryCode,String barCode,String sbuCode,String lpo,String invoiceNo,String supervisor,
    			String posted,String assetId,String assetCode,String assettype,String branch_id,String dept_id,String section_id,String category_id,
    			double vatamount,String residualvalue,int whtaxvalue,int recordNo,String warrantyStartDate,String expiryDate,
    			String depreciation_end_date,String SystemIp,String require_depreciation,double accum_dep,String rate,String require_redistribution) throws Exception {
//    		System.out.println(">>>>>> INSIDE CREATE GROUP MAIN OF GROUP ASSET BEAN <<<<<<");
    		StringBuffer b = new StringBuffer(400);
    		String no_of_items = "";
    		String province = "";
    		String noOfMonths = "";
    	    String spare_2 = "";
    	    String partPAY = "N";
    	    String fullyPAID = "Y";
    	    String deferPay = "N";
    	    String who_to_remind = "";
    	    String email_1 = "";
    	    String who_to_remind_2 = "";
    	    String email2 = "";
    	    String raise_entry = "Y";
    	    String spare_1 = "";
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
    		String gid="";
			//String group_id = findObject("select max(mt_id) from IA_MTID_TABLE where mt_tablename='am_asset_approval'");
//26/05/2023			String group_id =  new ApplicationHelper().getGeneratedId("AM_GROUP_ASSET_MAIN");
//			 System.out.println("Before Generating gid >>>>>>>>>> "+group_id);
//		double grpid = Double.parseDouble(group_id);
		//gid = Long.parseLong(String.valueOf(grpid));
	//	gid = Long.parseLong(group_id);
//26/05/2023	gid = group_id;
    	String group_id = integrifyId;
		gid = integrifyId;
    		String query = "SET IDENTITY_INSERT am_group_asset_main ON   "
    			+ "INSERT INTO AM_GROUP_ASSET_MAIN(GROUP_ID,QUANTITY,"
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
    			+ "EMAIL2,	STATE,DRIVER,SPARE_1,SPARE_2,USER_ID, PROVINCE,"
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
//    				 System.out.println("After Query Used First >>>>>>>>>> ");
    				getStatement().executeUpdate(b.toString());
//    				String group_id = findObject("select max(mt_id) from IA_MTID_TABLE where mt_tablename='am_asset_approval'");
 //   				 System.out.println("Before Generating gid >>>>>>>>>> "+group_id);
//    			double grpid = Double.parseDouble(group_id);
    			//gid = Long.parseLong(String.valueOf(grpid));
//    			gid = Long.parseLong(group_id);
    				//long groupid = retrieveMaxGroupID();
 //   				 System.out.println("After Before Generating gid >>>>>>>>>> " );
    				//String groupid = findObject("select MAX(group_id) from am_group_asset_main");
    				System.out.println("group_Id in createGroupMain:  "+group_id+"    gid: "+gid);
                    String query_archive = "SET IDENTITY_INSERT am_group_asset_main ON   "
                + "INSERT INTO AM_GROUP_ASSET_MAIN_Archive(group_id,QUANTITY,"
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
    			+ "EMAIL2,	STATE,DRIVER,SPARE_1,SPARE_2,USER_ID, PROVINCE,"
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
//    		System.out.println("RAISE ENTRY IN AM_GROUP_ASSET_MAIN 2nd : " + raise_entry);
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
 //   			closeConnection(con, ps);
    		return gid;

    	}
    	public String insertGroupAssetRecord(String integrifyId,String Description,String RegistrationNo,String VendorAC,String Datepurchased,
    			String AssetMake,String AssetModel,String AssetSerialNo,String AssetEngineNo,String SupplierName,String AssetUser,
    			String AssetMaintenance,double CostPrice,double VatableCost,String AuthorizedBy,String WhTax,double whtaxamount,String PostingDate,String EffectiveDate,
    			String PurchaseReason,String SubjectTOVat,String AssetStatus,String State,String Driver,String UserID,String branchCode,
    			String sectionCode,String deptCode,String categoryCode,String subcategoryCode,String barCode,String sbuCode,String lpo,String invoiceNo,String supervisor,
    			String posted,String assetId,String assetCode,String assettype,String branch_id,String dept_id,String section_id,String category_id,String sub_category_id,double vatamount,String residualvalue,int whtaxvalue,int noofitems,
    			String location,String memovalue,String memo, String spare1,String spare2, String spare3,String spare4, String spare5,String spare6,String multiple, String page1, String approvedbyName,String branchName) throws Exception, Throwable {
//    		System.out.println("INSIDE INSERT GROUP ASSET RECORD OF GROUP ASSET BEAN");
    		String[] budget = getBudgetInfo();
//    		System.out.println("invoiceNo  =======  "  + invoiceNo+"  Description: "+Description+"   PostingDate: "+PostingDate);
    		String invoiceNoExist =findObject(" select count(*) from am_group_asset_main where INVOICE_NO = '"+invoiceNo+"' AND Description = '"+Description+"' AND Posting_Date = '"+PostingDate+"' ");
//    		System.out.println("invoiceNoExist  =======  "  + invoiceNoExist+"   budget[3]: "+budget[3]);
    		double[] bugdetvalues = getBudgetValues(branchCode,categoryCode,sbuCode,subcategoryCode);
    		int DONE = 1; // everything is oK
    		int BUDGETENFORCED = 1; // EF budget = Yes, CF = NO, ERROR_FLAG
    		int BUDGETENFORCEDCF = 2; // EF budget = Yes, CF = Yes, ERROR_FLAG
    		int ASSETPURCHASEBD = 3; // asset falls into no quarter purchase date
    		int result = 0;
    		String resultvalue = "";
    		String url = "";
    		String flag = "";
    		String partPay ="";
    		long [] value = new long[2];

    		// older than bugdet
    		String Q = getQuarter(Datepurchased);
//    		System.out.println("BUDGET VALUE  =======  "  + budget[3]);
//    		String invoiceExist = findObject("select cart_cr from am_group_asset_main where Invoice_No = '"+invoiceNo+"'");
    		if(budget[3].equalsIgnoreCase("N")){
//    		if(invoiceNoExist.equalsIgnoreCase("0")){
    			resultvalue=createGroup(integrifyId,Description,RegistrationNo,VendorAC,Datepurchased,
    					AssetMake,AssetModel,AssetSerialNo,AssetEngineNo,SupplierName,AssetUser,AssetMaintenance,
    					CostPrice,VatableCost,AuthorizedBy,WhTax,whtaxamount,PostingDate,EffectiveDate,PurchaseReason,SubjectTOVat,AssetStatus,
    					State,Driver,UserID,branchCode,sectionCode,deptCode,categoryCode,subcategoryCode,barCode,sbuCode,lpo,invoiceNo,
    					supervisor,posted,assetId,assetCode,assettype,branch_id,dept_id,section_id,category_id,sub_category_id,vatamount,residualvalue,whtaxvalue,noofitems,
    					location,memovalue,memo,spare1,spare2,spare3,spare4,spare5,spare6,multiple);
    			//value[0]= DONE;
//    			System.out.println("Return From createGroup in insertGroupAssetRecord with budget N:  "+value[1]+"  resultvalue: "+resultvalue);
    //			result = DONE;
    			assetId = resultvalue;
//	  			System.out.println(">>>>>>>>>>>> resultvalue <<<<<<<<<<: "+resultvalue+"  assetId: "+assetId);

//	  			System.out.println(">>>>>>>>>>>> Inserting Into RaiseEntry <<<<<<<<<<");
    			boolean approval_level_val =checkApprovalStatus("3");
//    			System.out.println(">>>>>>>>>>>> approval_level_val <<<<<<<<<<: "+approval_level_val);
    			if ((approval_level_val)&&(resultvalue!="")){
		    	if(assettype.equalsIgnoreCase("C")){
		    		url = "DocumentHelp.jsp?np=groupAssetPosting&amp;id="+assetId+"&pageDirect=Y";
		    	}
		    	if(assettype.equalsIgnoreCase("U")){
		    		url = "DocumentHelp.jsp?np=groupAssetPostingBranch&amp;id="+assetId+"&pageDirect=Y";
		    	}
	  			String [] approvalResult=setApprovalDataGroup(Long.parseLong(resultvalue));
	  			approvalResult[10]="A";
	  			String trans_id = setGroupPendingTrans(approvalResult,"3",0,Description);
//	  			System.out.println("<<<<<<<<<< Transaction Id >>>>>>>>>> "+trans_id);
                        setPendingTransArchive(approvalResult,"3",Integer.parseInt(trans_id),0);
//                        System.out.println(">>>>>>>>>>>> Transaction Id <<<<<<<<<< "+trans_id+"  <<<<<assetCode: "+assetCode);
                        if(assetCode ==null){assetCode = "0";}
                        if(assetCode.equalsIgnoreCase("")||(assetCode.equalsIgnoreCase(null))){assetCode = "0";}
                        String assetRaiseEntry =findObject(" SELECT raise_entry from am_gb_company ");
                        if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("Y")){
//				System.out.println("<<<<<<<<<< assetRaise Entry in Group Posting >>>>>>>>>> "+assetRaiseEntry+"  approvalResult[5]: "+approvalResult[5]);
                        insertApprovalx2(resultvalue, Description, page1, flag, partPay,branchName, branchCode, SubjectTOVat,
  	  			WhTax, url,Integer.parseInt(trans_id),Integer.parseInt(assetCode));
                        }
    		}
//    		}
    		}
    		else if(budget[3].equalsIgnoreCase("Y")){
//    			if(invoiceNoExist.equalsIgnoreCase("0")){
    		if (!Q.equalsIgnoreCase("NQ")) {
    			if (budget[3].equalsIgnoreCase("Y")
    					&& budget[4].equalsIgnoreCase("N")) {
    				if (chkBudgetAllocation(Q, bugdetvalues, false,CostPrice)) {
    					updateBudget(Q, budget,categoryCode,branchCode,CostPrice,sbuCode,subcategoryCode);
    					resultvalue=createGroup(integrifyId,Description,RegistrationNo,VendorAC,Datepurchased,
    							AssetMake,AssetModel,AssetSerialNo,AssetEngineNo,SupplierName,AssetUser,AssetMaintenance,
    							CostPrice,VatableCost,AuthorizedBy,WhTax,whtaxamount,PostingDate,EffectiveDate,PurchaseReason,SubjectTOVat,AssetStatus,
    							State,Driver,UserID,branchCode,sectionCode,deptCode,categoryCode,subcategoryCode,barCode,sbuCode,lpo,invoiceNo,
    							supervisor,posted,assetId,assetCode,assettype,branch_id,dept_id,section_id,category_id,sub_category_id,vatamount,residualvalue,whtaxvalue,noofitems,
    							location,memovalue,memo,spare1,spare2,spare3,spare4,spare5,spare6,multiple);
    					//value[0]= DONE;
  //  					System.out.println("Return From createGroup in insertGroupAssetRecord with Budget NQ:  "+value[1]);
    					result = DONE;
    				} else {
    					//value[0]=  BUDGETENFORCED;
    					result=  BUDGETENFORCED;
    				}
    				assetId = resultvalue;
        			boolean approval_level_val =checkApprovalStatus("3");
        			if ((!approval_level_val)&&(resultvalue!="")){
    		    	if(assettype.equalsIgnoreCase("C")){
    		    		url = "DocumentHelp.jsp?np=groupAssetPosting&amp;id="+assetId+"&pageDirect=Y";
    		    	}
    		    	if(assettype.equalsIgnoreCase("U")){
    		    		url = "DocumentHelp.jsp?np=groupAssetPostingBranch&amp;id="+assetId+"&pageDirect=Y";
    		    	}
    	  			String [] approvalResult=setApprovalDataGroup(Long.parseLong(resultvalue));
    	  			approvalResult[10]="A";
    	  			String trans_id = setGroupPendingTrans(approvalResult,"3",0,Description);

                            setPendingTransArchive(approvalResult,"3",Integer.parseInt(trans_id),0);

                            String assetRaiseEntry =findObject(" SELECT raise_entry from am_gb_company ");
    if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("Y")){
                            insertApprovalx2
      	  		(resultvalue, approvalResult[5], page1, flag, partPay,branchName, branchCode, SubjectTOVat,
      	  			WhTax, url,Integer.parseInt(trans_id),Integer.parseInt(assetCode));
                            }
        		}

    			} else if (budget[3].equalsIgnoreCase("Y")
    					&& budget[4].equalsIgnoreCase("Y")) {
    				if (chkBudgetAllocation(Q, bugdetvalues, true,CostPrice)) {
    					updateBudget(Q, budget,categoryCode,branchCode,CostPrice,sbuCode,subcategoryCode);
    					resultvalue=createGroup(integrifyId,Description,RegistrationNo,VendorAC,Datepurchased,
    							AssetMake,AssetModel,AssetSerialNo,AssetEngineNo,SupplierName,AssetUser,AssetMaintenance,
    							CostPrice,VatableCost,AuthorizedBy,WhTax,whtaxamount,PostingDate,EffectiveDate,PurchaseReason,SubjectTOVat,AssetStatus,
    							State,Driver,UserID,branchCode,sectionCode,deptCode,categoryCode,subcategoryCode,barCode,sbuCode,lpo,invoiceNo,
    							supervisor,posted,assetId,assetCode,assettype,branch_id,dept_id,section_id,category_id,sub_category_id,vatamount,residualvalue,whtaxvalue,noofitems,
    							location,memovalue,memo, spare1,spare2, spare3,spare4, spare5,spare6,multiple);
    					//value[0]=  DONE;
//    					System.out.println("Return From createGroup in insertGroupAssetRecord with Budget:  "+value[1]);
    					result = DONE;
    				} else {
    					//value[0]=  BUDGETENFORCEDCF;
    					result=  BUDGETENFORCEDCF;
    				}
    				assetId = resultvalue;
        			boolean approval_level_val =checkApprovalStatus("3");
        			if ((!approval_level_val)&&(resultvalue!="")){
    		    	if(assettype.equalsIgnoreCase("C")){
    		    		url = "DocumentHelp.jsp?np=groupAssetPosting&amp;id="+assetId+"&pageDirect=Y";
    		    	}
    		    	if(assettype.equalsIgnoreCase("U")){
    		    		url = "DocumentHelp.jsp?np=groupAssetPostingBranch&amp;id="+assetId+"&pageDirect=Y";
    		    	}
    	  			String [] approvalResult=setApprovalDataGroup(Long.parseLong(resultvalue));
    	  			approvalResult[10]="A";
    	  			String trans_id = setGroupPendingTrans(approvalResult,"3",0,Description);

                            setPendingTransArchive(approvalResult,"3",Integer.parseInt(trans_id),0);

                            String assetRaiseEntry =findObject(" SELECT raise_entry from am_gb_company ");
    if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("Y")){
                            insertApprovalx2
      	  		(resultvalue, approvalResult[5], page1, flag, partPay,branchName, branchCode, SubjectTOVat,
      	  			WhTax, url,Integer.parseInt(trans_id),Integer.parseInt(assetCode));
                            }
        		}
    			} else {
//    				resultvalue=createGroup(integrifyId,Description,RegistrationNo,VendorAC,Datepurchased,
//    						AssetMake,AssetModel,AssetSerialNo,AssetEngineNo,SupplierName,AssetUser,AssetMaintenance,
//    						CostPrice,VatableCost,AuthorizedBy,WhTax,whtaxamount,PostingDate,EffectiveDate,PurchaseReason,SubjectTOVat,AssetStatus,
//    						State,Driver,UserID,branchCode,sectionCode,deptCode,categoryCode,subcategoryCode,barCode,sbuCode,lpo,invoiceNo,
//    						supervisor,posted,assetId,assetCode,assettype,branch_id,dept_id,section_id,category_id,sub_category_id,vatamount,residualvalue,whtaxvalue,noofitems,
//    						location,memovalue,memo,spare1,spare2,spare3,spare4,spare5,spare6,multiple);
//    				//value[0]= DONE;
// //   				System.out.println("Return From createGroup in insertGroupAssetRecord with No Budget:  "+value[1]);
//    				result = DONE;
//    				assetId = resultvalue;
//        			boolean approval_level_val =checkApprovalStatus("27");
//        			if ((!approval_level_val)&&(resultvalue!="")){
//    		    	if(assettype.equalsIgnoreCase("C")){
//    		    		url = "DocumentHelp.jsp?np=groupAssetPosting&amp;id="+assetId+"&pageDirect=Y";
//    		    	}
//    		    	if(assettype.equalsIgnoreCase("U")){
//    		    		url = "DocumentHelp.jsp?np=groupAssetPostingBranch&amp;id="+assetId+"&pageDirect=Y";
//    		    	}
//    	  			String [] approvalResult=setApprovalDataGroup(Long.parseLong(resultvalue));
//    	  			approvalResult[10]="A";
//    	  			String trans_id = setGroupPendingTrans(approvalResult,"27",0);
//
//                            setPendingTransArchive(approvalResult,"27",Integer.parseInt(trans_id),0);
//
//                            String assetRaiseEntry =findObject(" SELECT raise_entry from am_gb_company ");
//    if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("Y")){
//                            insertApprovalx2
//      	  		(resultvalue, approvalResult[5], page1, flag, partPay,branchName, branchCode, SubjectTOVat,
//      	  			WhTax, url,Integer.parseInt(trans_id),Integer.parseInt(assetCode));
//                            }
//        		}
    			}
    		} else {
    			//value[1]=createGroup();
    			//value[0]= ASSETPURCHASEBD;
    			result= ASSETPURCHASEBD;
    		}
//    	}
    	}
    	else{}
 //   		closeConnection(con, ps);
    		return resultvalue;

    	}
        public void updateVatableCostBalance(Double vat_cost_balance ,Double vat_cost,String groupId,String Count)
        {
     //   	System.out.println("vat_cost_balance: "+vat_cost_balance+" vat_cost:  "+vat_cost+"  groupId:  "+groupId+"  Count:  "+Count);
        	double vat_cost_difference = (vat_cost_balance - vat_cost);
           	String updateQry = "update am_group_asset_main set Vatable_Cost_Bal = ? ,pend_GrpAssets=?  where group_id = ? ";
        
            try
            (Connection conn = getConnection();
        PreparedStatement Stat = conn.prepareStatement(updateQry);){
            
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
        		String sectionCode,String deptCode,String categoryCode,String subcategoryCode,String barCode,String sbuCode,String lpo,String invoiceNo,String supervisor,
        		String posted,String assetId,String assetCode,String assettype,String branch_id,String dept_id,String section_id,
        		String category_id,String sub_category_id,double vatamount,String residualvalue,int whtaxvalue,String groupid,int recno,
        		String systemIp,String spare_1,String spare_2,String spare_3,String spare_4,String spare_5,String spare_6,String projectCode,String delimiter) throws Exception, Throwable
        {
//				System.out.println("Save Groupid: "+groupid);
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
//			    String spare_1 = "";
			   // String status = "";
			    String user_id = "";
			    String multiple = "N";
//			    String spare_2 = "";
			    String partPAY = "N";
			    String fullyPAID = "Y";
			    String deferPay = "N";
			    int selectTax = 0;
			    String MacAddress = "";
			    String SystemIp = "";
			    String section = "";
			    String strnewDateMonth = "";
			    String createQuery = "";
//			    System.out.println("Date Purchased:  "+Datepurchased);
				int purchase_start_year = Integer.parseInt(Datepurchased.substring(0,4));
//				System.out.println("purchase start year: "+purchase_start_year+"  Date Purchased:  "+Datepurchased);
				int purchase_start_month = Integer.parseInt(Datepurchased.substring(5,7));
//				System.out.println("purchase start month: "+purchase_start_month+"  Date Purchased:  "+Datepurchased);
				int purchase_start_day = Integer.parseInt(Datepurchased.substring(8,10));
//				System.out.println("purchase start Day: "+purchase_start_day+"  Date Purchased:  "+Datepurchased);

				int newDateYear= purchase_start_year;
//				System.out.println("purchase start month: "+purchase_start_month+" Sum: "+purchase_start_month + 1);
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
//			      System.out.println("Values for branch_id: "+branch_id+"  dept_id: "+dept_id+"  section_id: "+section_id+"  category_id: "+category_id);
            String asset_id_new = getNewIdentity(branch_id,
            		dept_id, section_id, category_id,delimiter);
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
            if (category_id == null || category_id.equals("")) {
                category_id = "0";
            }
            if (sub_category_id == null || sub_category_id.equals("")) {
                sub_category_id = "0";
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
            if(assettype.equalsIgnoreCase("C")){
            createQuery = "INSERT INTO AM_ASSET         " +
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
            "MULTIPLE,PROVINCE, WAR_START_DATE, WAR_MONTH, " +
            "WAR_EXPIRY_DATE,LPO,BAR_CODE ,BRANCH_CODE,CATEGORY_CODE ," +
            "GROUP_ID,PART_PAY,FULLY_PAID,DEFER_PAY,SBU_CODE,SECTION_CODE," +
            "DEPT_CODE,system_ip,asset_code,memo,memoValue,INTEGRIFY,SUPERVISOR,SUB_CATEGORY_ID," +
            "SUB_CATEGORY_CODE,SPARE_3,SPARE_4,SPARE_5,SPARE_6,PROJECT_CODE) " +

            "VALUES" +
            "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
            "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        }
            if(assettype.equalsIgnoreCase("U")){
            createQuery = "INSERT INTO AM_ASSET_UNCAPITALIZED         " +
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
            "MULTIPLE,PROVINCE, WAR_START_DATE, WAR_MONTH, " +
            "WAR_EXPIRY_DATE,LPO,BAR_CODE ,BRANCH_CODE,CATEGORY_CODE ," +
            "GROUP_ID,PART_PAY,FULLY_PAID,DEFER_PAY,SBU_CODE,SECTION_CODE,DEPT_CODE," +
            "system_ip,asset_code,memo,memoValue,INTEGRIFY,SUPERVISOR,SUB_CATEGORY_ID," +
            "SUB_CATEGORY_CODE,SPARE_3,SPARE_4,SPARE_5,SPARE_6,PROJECT_CODE) " +

            "VALUES" +
            "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
            "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        }

            String create_Archive_Query = "INSERT INTO AM_ASSET_ARCHIVE         " +
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
            "MULTIPLE,PROVINCE, WAR_START_DATE, WAR_MONTH, " +
            "WAR_EXPIRY_DATE,LPO,BAR_CODE ,BRANCH_CODE,CATEGORY_CODE ," +
            "GROUP_ID,PART_PAY,FULLY_PAID,DEFER_PAY,SBU_CODE,SECTION_CODE,DEPT_CODE,"+
            "system_ip,asset_code,SUB_CATEGORY_ID,SUB_CATEGORY_CODE,SPARE_3, SPARE_4,SPARE_5, SPARE_6,PROJECT_CODE ) " +

            "VALUES" +
            "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
            "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            /*
             *First Create Asset Records
             * and then determine if it
             * should be made available for fleet.
             */
               try
               {
                //asset_costPrice = Double.parseDouble(vat_amount) + Double.parseDouble(vatable_cost);
//            	System.out.println("Group Creation -1 ");
                con = getConnection();
                AssetStatus = "APPROVED";
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
                ps.setInt(74, Integer.parseInt(sub_category_id));
                ps.setString(75,subcategoryCode);
                ps.setString(76, spare_3);
                ps.setString(77, spare_4);
                ps.setString(78, spare_5);
                ps.setString(79, spare_6);
                ps.setString(80, projectCode);
                //not right

                boolean result = ps.execute();

 //               con = getConnection();
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
                ps.setDouble(21, (CostPrice-10.00));
                ps.setDate(22, dateConvert(depreciation_end_date));
                ps.setDouble(23, Double.parseDouble(residual_value));
                ps.setString(24, AuthorizedBy);
                ps.setDate(25, dateConvert(new java.util.Date()));
                ps.setDate(26, dateConvert(depreciation_start_date));
                ps.setString(27, PurchaseReason);
                ps.setString(28, "0");
                ps.setString(29, computeTotalLife(getDepreciationRate(categoryCode)));
                ps.setInt(30, Integer.parseInt(location));
                ps.setString(31, computeTotalLife(getDepreciationRate(categoryCode)));
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
                ps.setInt(70, Integer.parseInt(sub_category_id));
                ps.setString(71,subcategoryCode);
                ps.setString(72, spare_3);
                ps.setString(73, spare_4);
                ps.setString(74, spare_5);
                ps.setString(75, spare_6);
                ps.setString(76, projectCode);
                if(result==false){
//                System.out.println("=====================================================");
//                System.out.println("Result Of Insertion into Asset Table From group Asset : " + result);
//                System.out.println("=============>integrifyId: "+integrifyId);
//                System.out.println("=====================================================");
                }
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
//    			System.out.println("Approved by Name in Save: "+Name+"  User Id: "+Integer.parseInt(supervisor));
          	   //	String IdInt =findObject(" SELECT User_id from am_gb_user where user_name='"+UserID+"'");
          	  if(assettype.equalsIgnoreCase("C")){
          	   	url = "DocumentHelp.jsp?np=groupAssetPosting&amp;id=" + groupid + "&pageDirect=Y";
               }
          	  if(assettype.equalsIgnoreCase("U")){
            	   	url = "DocumentHelp.jsp?np=groupAssetPostingBranch&amp;id=" + groupid + "&pageDirect=Y";
                 }
          	   	boolean approval_level_val =checkApprovalStatus("27");
//          	  System.out.println(">>>>>>>>>>>> groupid Into RaiseEntry <<<<<<<<<< "+groupid+"  asset_Code: "+asset_Code+"  assetCode: "+assetCode);
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
    	      	  	}
 //         	  System.out.println("====== Inserting into status ======"+status+" approval_level_val: "+approval_level_val);
    	      	if((status)&&(approval_level_val))
    		      	{
//    		      	 System.out.println("====== Inserting into Approval when approvl is required ======"+groupid);
    		      	 changeGroupAssetStatus(groupid,"PENDING",assettype);
    		      	 String trans_id = setGroupPendingTrans(setApprovalDataGroup(Long.parseLong(groupid)),"27",Integer.parseInt(assetCode),Description);
                             setPendingTransArchive(setApprovalDataGroup(Long.parseLong(groupid)),"27",Integer.parseInt(trans_id),Integer.parseInt(assetCode));
    		      	 //write a method to change status to pending
    		      	}
    	      	else{
 //   	      		System.out.println("====== Inserting into Approval in else ======"+groupid);
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

         
            String found = null;

            String query="Insert into Am_Invoice_no (asset_id,lpo,invoice_no,trans_type,group_id) values (?,?,?,?,?)";

                 try
                 (Connection conn = getConnection();
             PreparedStatement Stat = conn.prepareStatement(query);){
               
                Stat.setString(1, assetID);
                Stat.setString(2, lpo);
                Stat.setString(3, invoiceNo);
                Stat.setString(4, TransType);
                Stat.setString(5, grpID);
                Stat.execute();

            } catch (Exception ee2) {
                System.out.println("WARN:ERROR insGrpToAm_Invoice_No  --> " + ee2);
                ee2.printStackTrace();
            } 

            }
        private boolean checkApprovalStatus(String code)
        {
    		// TODO Auto-generated method stub
    		boolean status = false;
    		String approval_status_qry = "select level from approval_level_setup where code ='"+code+"'";
    		
    	    int level = 0;
    	    try
    	    (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(approval_status_qry);
             ResultSet rs = ps.executeQuery()){
    	    	
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
            "STATE=?, DRIVER=?, SPARE_1=?, SPARE_2=?, USER_ID=?," +
            "PROVINCE=?, WAR_START_DATE=?, WAR_MONTH=?, " +
            "WAR_EXPIRY_DATE=?,LPO=?,BAR_CODE=? ,BRANCH_CODE=?,CATEGORY_CODE=? ," +
            "GROUP_ID=?,PART_PAY=?,FULLY_PAID=?,DEFER_PAY=?,SBU_CODE=?,dept_code = ?,section_code = ? "+
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
             "STATE=?, DRIVER=?, SPARE_1=?, SPARE_2=?, USER_ID=?," +
             "PROVINCE=?, WAR_START_DATE=?, WAR_MONTH=?, " +
             "WAR_EXPIRY_DATE=?,LPO=?,BAR_CODE=? ,BRANCH_CODE=?,CATEGORY_CODE=? ," +
             "GROUP_ID=?,PART_PAY=?,FULLY_PAID=?,DEFER_PAY=?,SBU_CODE=?,dept_code = ?,section_code = ? "+
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
            "STATE=?, DRIVER=?, SPARE_1=?, SPARE_2=?, USER_ID=?," +
            "PROVINCE=?, WAR_START_DATE=?, WAR_MONTH=?, " +
            "WAR_EXPIRY_DATE=?,LPO=?,BAR_CODE=? ,BRANCH_CODE=?,CATEGORY_CODE=? ," +
            "GROUP_ID=?,PART_PAY=?,FULLY_PAID=?,DEFER_PAY=?,SBU_CODE=?,dept_code = ?,section_code = ? "+
            "  where INTEGRIFY = ? AND RECCOUNT =?" ;
         if(assettype.equalsIgnoreCase("C")){
            chk_process_flag ="select count(*) from am_group_asset WHERE process_flag=? and Group_id =?";
         }
         if(assettype.equalsIgnoreCase("U")){
             chk_process_flag ="select count(*) from AM_GROUP_ASSET_UNCAPITALIZED WHERE process_flag=? and Group_id =?";
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
            	ps.setString(1, "Y");
            	ps.setString(2, groupid);
                rs = ps.executeQuery();
                if (rs.next())
                {
                	chk_Process_flag = rs.getInt(1);
    	            if(chk_Process_flag == 0)
    	            {
//    	            	System.out.println("Nothing to update in am_group_asset!!!!!!");
    	            	ps = con.prepareStatement(update_created_asset_main_qry);
    	            	ps.setString(1, process_flag);
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
        public String setGroupPendingTransOld(String[] a, String code,int assetCode,String description){
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
     String tranLevelQuery = "select level from approval_level_setup where code =?";
            con = null;
            ps = null;
            rs = null;
            try
            {
                con = getConnection();
                 ps = con.prepareStatement(tranLevelQuery);
                 ps.setString(1, code);
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
 //               System.out.println("Posting_Date b4 conversion in setPendingTrans : " +  a[4]);
                ps.setDate(5, (a[4])==null?null:dateConvert(a[4]));
                //System.out.println("Posting_Date after setPendingTrans : " +  a[4]);
                //ps.setString(6, (a[5]==null)?"":a[5]);
                ps.setString(6,description);
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
//            System.out.println("MTID in setGroupPendingTrans: "+mtid);
            return mtid;
        }
        
        public String setGroupPendingTrans(String[] a,
                String code,
                int assetCode,
                String description) {

String insertSql = "INSERT INTO am_asset_approval (asset_id, user_id, super_id, amount, posting_date, description,"
		+ "effective_date, branchCode, asset_status, tran_type, process_status,"
		+ "tran_sent_time, transaction_id, batch_id, transaction_level, asset_code)"
		+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

String levelSql = "SELECT level FROM approval_level_setup WHERE code = ?";

String transactionId = new ApplicationHelper()
.getGeneratedId("am_asset_approval");

try (Connection con = getConnection()) {

con.setAutoCommit(false); 

int transactionLevel = 0;

// 1️⃣ Get approval level
try (PreparedStatement psLevel = con.prepareStatement(levelSql)) {

psLevel.setString(1, code);

try (ResultSet rs = psLevel.executeQuery()) {
if (rs.next()) {
 transactionLevel = rs.getInt("level");
}
}
}

// 2️⃣ Insert approval record
try (PreparedStatement psInsert = con.prepareStatement(insertSql)) {

SimpleDateFormat timer = new SimpleDateFormat("HH:mm:ss");

psInsert.setString(1, safe(a, 0));
psInsert.setString(2, safe(a, 1));
psInsert.setString(3, safe(a, 2));
psInsert.setDouble(4, parseDoubleSafe(a, 3));
psInsert.setDate(5, a[4] != null ? dateConvert(a[4]) : null);
psInsert.setString(6, description);
psInsert.setDate(7, a[6] != null ? dateConvert(a[6]) : null);
psInsert.setString(8, safe(a, 7));
psInsert.setString(9, safe(a, 8));
psInsert.setString(10, safe(a, 9));
psInsert.setString(11, safe(a, 10));
psInsert.setString(12, timer.format(new java.util.Date()));
psInsert.setString(13, transactionId);
psInsert.setString(14, transactionId);
psInsert.setInt(15, transactionLevel);
psInsert.setInt(16, assetCode);

psInsert.executeUpdate();
}

con.commit(); // ✅ Commit only if everything succeeded

} catch (Exception e) {
System.out.println("ERROR in setGroupPendingTrans -> " + e.getMessage());
return "";
}

return transactionId;
}

        private String safe(String[] arr, int index) {
            return (arr != null && arr.length > index && arr[index] != null)
                    ? arr[index]
                    : "";
        }

        private double parseDoubleSafe(String[] arr, int index) {
            try {
                return (arr != null && arr.length > index && arr[index] != null)
                        ? Double.parseDouble(arr[index])
                        : 0.0;
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }

        
        
        public String[] setApprovalDataGroup(long id){

        	//String q ="select asset_id, asset_status,user_ID,supervisor,Cost_Price,Posting_Date,description,effective_date,BRANCH_CODE from am_asset where asset_id ='" +id+"'";
        	   String[] result= new String[12];
        	  
        	       
        	        //int groupId = Integer.parseInt(id);
        	         String query ="select group_id,user_ID,supervisor,Cost_Price,Posting_Date," +
        	         		"		description,effective_date,BRANCH_CODE," +
        	         				"Asset_Status from am_group_asset_main where group_id =? "  ;
//        	         System.out.println("Query in setApprovalDataGroup : " + query);


        	        try(Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);) {
        	          
        	            ps.setLong(1, id);
        	            try (ResultSet rs = ps.executeQuery()) {
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
           
//            System.out.println("Inside insertApprovalx2 with Id: "+id);
            String query = "INSERT INTO am_raisentry_post(Id,Description,Page,Flag,partPay,UserId,"
                    + "Branch,subjectToVat,whTax,url,trans_id,Posting_date,entryPostFlag,GroupIdStatus,asset_code)"
                    + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);){
              
//                System.out.println("query in  insertApprovalx2: "+query);
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
                ps.setString(12, String.valueOf(df.dateConvert(new java.util.Date())));
                ps.setString(13, "N");
                ps.setString(14, "N");
                ps.setInt(15, assetCode);
                ps.execute();
 //               System.out.println("Saved insertApprovalx2: ");

            } catch (Exception ex) {
                done = false;
                System.out.println(
                        "WARNING:cannot insert am_raisentry_post->"
                        + ex.getMessage());
            } 
  //          System.out.println("done insertApprovalx2: "+done);
            return done;
        }
    	public void changeGroupAssetStatusOld(String id,String status,String assettype)
    	{
 //   		System.out.println("changeGroupAssetStatus status: "+status+" Id: "+id+"    assettype: "+assettype);
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
        		" where Group_id = ?";
        	    }
                String query_archive="update am_group_asset_archive set asset_status=? " +
    		" where Group_id = ?";
     //           System.out.println("query_r:  "+query_r+" "+status);
     //           System.out.println("query_archive:  "+query_archive+" "+status);
    	    try
    	    	{
    	    	con = getConnection();
    	    	ps = con.prepareStatement(query_r);
    	    	ps.setString(1,status);
    	    	ps.setString(2,id);
    	       	int i =ps.executeUpdate();

                    ps = con.prepareStatement(query_archive);
    	    	ps.setString(1,status);
    	    	ps.setString(2,id);
    	       	i =ps.executeUpdate();

    	        changeGroupAssetMainStatus(id,status);
    	        }
    		catch (Exception ex)
    		    {
    		        System.out.println("changeGroupAssetStatus in GroupAssetToAssetBean: Error Updating am_group_asset " + ex);
    		    }
    		finally
    			{
    	            closeConnection(con, ps);
    	        }
    	}
    	
    	public void changeGroupAssetStatus(String id, String status, String assetType) {

    	    String updateGroupAsset = null;

    	    if ("C".equalsIgnoreCase(assetType)) {
    	        updateGroupAsset = "UPDATE am_group_asset SET asset_status = ? WHERE group_id = ? ";
    	    } else if ("U".equalsIgnoreCase(assetType)) {
    	        updateGroupAsset = " UPDATE AM_GROUP_ASSET_UNCAPITALIZED SET asset_status = ? WHERE group_id = ? ";
    	    } else {
    	        throw new IllegalArgumentException("Invalid asset type: " + assetType);
    	    }

    	    String updateArchive = "UPDATE am_group_asset_archive SET asset_status = ? WHERE group_id = ?";

    	    String updateMain = "UPDATE am_group_asset_main SET asset_status = ? WHERE group_id = ?";

    	    String updateMainArchive = "UPDATE am_group_asset_main_archive SET asset_status = ? WHERE group_id = ? ";

    	    try (Connection con = getConnection()) {

    	        con.setAutoCommit(false); 

    	        try (PreparedStatement psGroup = con.prepareStatement(updateGroupAsset);
    	             PreparedStatement psArchive = con.prepareStatement(updateArchive);
    	             PreparedStatement psMain = con.prepareStatement(updateMain);
    	             PreparedStatement psMainArchive = con.prepareStatement(updateMainArchive)) {

    	            // 1️⃣ Update group table
    	            psGroup.setString(1, status);
    	            psGroup.setString(2, id);
    	            psGroup.executeUpdate();

    	            // 2️⃣ Update archive
    	            psArchive.setString(1, status);
    	            psArchive.setString(2, id);
    	            psArchive.executeUpdate();

    	            // 3️⃣ Update main
    	            psMain.setString(1, status);
    	            psMain.setString(2, id);
    	            psMain.executeUpdate();

    	            // 4️⃣ Update main archive
    	            psMainArchive.setString(1, status);
    	            psMainArchive.setString(2, id);
    	            psMainArchive.executeUpdate();

    	            con.commit(); // ✅ Commit all together

    	        } catch (Exception e) {
    	            con.rollback(); 
    	            throw e;
    	        }

    	    } catch (Exception ex) {
    	        System.out.println("ERROR updating group asset status -> " + ex.getMessage());
    	    }
    	}

    	public void changeGroupAssetMainStatusOld(String id, String status2)
    	{
 //   		System.out.println("changeGroupAssetMainStatus status2: "+status2+" Id: "+id);
    		// TODO Auto-generated method stub
    		String query_r ="update am_group_asset_main set asset_status=? " +
    		"where Group_id = ?";

                    String query_archive ="update am_group_asset_main_archive set asset_status=? " +
    		"where Group_id = ?";
//               System.out.println("changeGroupAssetMainStatus query_r: "+query_r);
//               System.out.println("changeGroupAssetMainStatus query_archive: "+query_archive);
    		Connection con = null;
    	    PreparedStatement ps = null;
    	    try
    		{
    		con = getConnection();
    		ps = con.prepareStatement(query_r);
    		ps.setString(1,status2);
    		ps.setString(2,id);
    	    int i =ps.executeUpdate();

                ps = con.prepareStatement(query_archive);
    		ps.setString(1,status2);
    		ps.setString(2,id);
    	     i =ps.executeUpdate();

    	    }
    	    catch (Exception ex)
    	    {
    	        System.out.println("changeGroupAssetMainStatus in GroupAssetToAssetBean: Error Updating am_group_asset_main : " + ex);
    	    }
    	    
    	}
    	
    	public void changeGroupAssetMainStatus(String id, String status) {

    	    String updateMain = "UPDATE am_group_asset_main SET asset_status = ? WHERE group_id = ?";

    	    String updateArchive = "UPDATE am_group_asset_main_archive SET asset_status = ? WHERE group_id = ? ";

    	    try (Connection con = getConnection()) {

    	        con.setAutoCommit(false); // Start transaction

    	        try (PreparedStatement psMain = con.prepareStatement(updateMain);
    	             PreparedStatement psArchive = con.prepareStatement(updateArchive)) {

    	            // Update main table
    	            psMain.setString(1, status);
    	            psMain.setString(2, id);
    	            psMain.executeUpdate();

    	            // Update archive table
    	            psArchive.setString(1, status);
    	            psArchive.setString(2, id);
    	            psArchive.executeUpdate();

    	            con.commit(); // Commit both together

    	        } catch (Exception e) {
    	            con.rollback(); // Rollback if anything fails
    	            throw e;
    	        }

    	    } catch (Exception ex) {
    	        System.out.println("ERROR updating group asset status -> " + ex.getMessage());
    	    }
    	}

        public void freeResource() {
            try {
            	if(this.con != null) {
            		this.con.close();
            	}
//                if (con.isClosed()){
//                    System.out.println("Connection Closed.");} else{System.out.println("Connection Is not Closed.");}
/*
                if (this.con != null) {
                    this.con.close();
                }*/
                this.con = null;
            } catch (Exception e) {
                System.out.println("WARN:Error closing Connection in CompanyHandler ->" +
                                   e.getMessage());
            }

        }
    	private long retrieveMaxGroupID()
    	{
    		// TODO Auto-generated method stub
    		
            long maxNum=0;
    		String groupID_qry = "select MAX(group_id) from am_group_asset_main";
    		try
            (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(groupID_qry);){
               try(ResultSet rs = ps.executeQuery();) {     
                if (rs.next())
                {
                	 maxNum = rs.getLong(1);
                }
  //              System.out.println("Max GroupID retrieved : " + maxNum);
            }
    		}
    		catch (Exception e)
            {
                System.out.println("INFO:Error retrieving Maximum GroupID ->" +
                                   e.getMessage());
            }
    		finally
            {
                //dbConnection.closeConnection(con, ps, rs);
    			closeConnection(con, ps, rs);
            }
    		return maxNum;
    	}
    	public boolean newgroupassetinterfaceOld(String errormessage,int recno,String status,String assetid,String assetcode,String recintegrifyId) {

    		
    		boolean done = false;
    		String query = "UPDATE NEW_GROUP_ASSET_INTERFACE"
    				+ " SET ERROR_MESSAGE = ?,POSTED=?,ASSET_ID = ?,"
    				+ " ASSET_CODE = ? "
    				+ " WHERE ITEMCOUNT = ? AND INTEGRIFY_ID = ? ";
    	//	System.out.println("query: "+query);
    		String query2 = "UPDATE NEW_GROUP_ASSET_INTERFACE"
    			+ " SET ERROR_MESSAGE = ?,POSTED=? "
    			+ " WHERE ITEMCOUNT = ? AND INTEGRIFY_ID = ? ";
    		//System.out.println("query2: "+query2);
    		try {
    			con = getConnection();
    			if(status.equalsIgnoreCase("Y")){
    			ps = con.prepareStatement(query);
    			ps.setString(1,errormessage);
    			ps.setString(2,status);
    			ps.setString(3,assetid.trim());
    			ps.setString(4,assetcode.trim());
    			ps.setInt(5,recno);
    			ps.setString(6,recintegrifyId);
    			}
    			else{ps = con.prepareStatement(query2);
    			ps.setString(1,errormessage);
    			ps.setString(2,status);
    			ps.setInt(3,recno);
    			ps.setString(4,recintegrifyId);
    			}
    			done = (ps.executeUpdate() != -1);

    		} catch (Exception e) {
    			System.out.println("WARNING:Error executing Query ->"
    					+ e.getMessage());
    		} finally {
    			closeConnection(con, ps);
    		}
    		return done;

    	}
    	
    	public boolean newgroupassetinterface(String errorMessage,
                int recNo,
                String status,
                String assetId,
                String assetCode,
                String recIntegrifyId) {

String queryWithAsset = "UPDATE NEW_GROUP_ASSET_INTERFACE SET ERROR_MESSAGE = ?,POSTED = ?,ASSET_ID = ?,ASSET_CODE = ? WHERE ITEMCOUNT = ? AND INTEGRIFY_ID = ? ";

String queryWithoutAsset = "UPDATE NEW_GROUP_ASSET_INTERFACE SET ERROR_MESSAGE = ?,POSTED = ? WHERE ITEMCOUNT = ? AND INTEGRIFY_ID = ?";

boolean isPosted = "Y".equalsIgnoreCase(status);

try (Connection con = getConnection();
PreparedStatement ps = con.prepareStatement(
isPosted ? queryWithAsset : queryWithoutAsset)) {

ps.setString(1, errorMessage);
ps.setString(2, status);

if (isPosted) {
ps.setString(3, assetId != null ? assetId.trim() : null);
ps.setString(4, assetCode != null ? assetCode.trim() : null);
ps.setInt(5, recNo);
ps.setString(6, recIntegrifyId);
} else {
ps.setInt(3, recNo);
ps.setString(4, recIntegrifyId);
}

return ps.executeUpdate() > 0;

} catch (Exception e) {
System.out.println("WARNING: Error executing NEW_GROUP_ASSET_INTERFACE update -> "
+ e.getMessage());
return false;
}
}


    	 public String checkAssetAvalability(String assetID){
    	   //  System.out.println("\nttttttttttttttt "+assetID);
    	    String query = " SELECT transaction_id from am_asset_approval " +
    	                       " WHERE (process_status='P' OR  process_status='WA') and asset_id = ?";
    	//    System.out.println("query nttttttttttttttt "+query);
    	//	df = new com.magbel.util.DatetimeFormat();

    	String transactionId ="";
    	String process_status ="";
    	       
    	        String Datefld = formatDate(new java.util.Date());
    	//        System.out.println("====Datefld== "+Datefld);
    	        String Month = Datefld.substring(3, 5);
    	        String Year = Datefld.substring(6, 10);
    	              try(Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);) {
    	           
    	            ps.setString(1,assetID);
    	            try (ResultSet rs = ps.executeQuery()) {
    	            while (rs.next()) {
    	               transactionId = rs.getString("transaction_id");
    	           //     System.out.println("\n>>>>> trans id " + transactionId);
    	               process_status =   transactionId;
    	            }
    	            }
    	            if(transactionId.equalsIgnoreCase("")){
    	 String query1 = " SELECT asset_code from am_asset WHERE asset_id = '"+assetID+"'";
    	              int assetCode = Integer.parseInt(getCodeName(query1));
    	  String queryTest1 = getCodeName("select max(trans_id) from am_raisentry_post where  asset_code="+assetCode);
    	  String queryTest2 = getCodeName("select max(trans_id)  from am_raisentry_TRANSACTION where asset_code="+assetCode);
    	  String query2 =getCodeName("select asset_code from am_asset_improvement where asset_id = '"+assetID+"' and month(revalue_date) = '"+Month+"' and year(revalue_date) = '"+Year+"' AND approval_status != 'REJECTED'");
    	  String query3 =getCodeName("select asset_code from am_asset_revaluation where asset_id = '"+assetID+"' and month(revalue_date) = '"+Month+"' and year(revalue_date) = '"+Year+"'");
    	  String query4 =getCodeName("select asset_code from am_AssetDisposal where asset_id = '"+assetID+"' and DISPOSAL_PERCENT = 100 AND disposal_status != 'R'");
    	 String query5 =getCodeName("select count(*) from am_raisentry_post where asset_code = '"+assetCode+"'  and entryPostFlag = 'N' ");
    	 String query6 =getCodeName("select asset_code from AM_GROUP_IMPROVEMENT where asset_id = '"+assetID+"' and month(revalue_date) = '"+Month+"' and year(revalue_date) = '"+Year+"' AND approval_status != 'REJECTED'");
    	  if(query2.equalsIgnoreCase("") && query3.equalsIgnoreCase("") && query4.equalsIgnoreCase("") && query6.equalsIgnoreCase("")){process_status="";}else {process_status="D";}
    	  if(queryTest1.equalsIgnoreCase(queryTest2) && !queryTest1.equalsIgnoreCase("") && !queryTest2.equalsIgnoreCase("")){
    	      //check if posted succceffuly on am_raisentry_TRANSACTION
    	String queryTest3 = getCodeName("select iso  from am_raisentry_TRANSACTION where asset_code="+assetCode+" and trans_id="+queryTest1+" ");
//    	  System.out.println("--queryTest2--> "+queryTest2);
//    	  System.out.println("--queryTest3--> "+queryTest3);
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
    	 
    	 
    	 public String checkAssetAvalability2(String assetId) {

    		    String processStatus = "";

    		    try (Connection con = getConnection()) {

    		        // 1️⃣ Check if asset is in pending approval
    		        if (hasPendingApproval(con, assetId)) {
    		            return "P";
    		        }

    		        int assetCode = getAssetCode(con, assetId);
    		        if (assetCode == 0) {
    		            return "";
    		        }

    		        // 2️⃣ Check disposal / improvement / revaluation
    		        if (hasCurrentMonthActivity(con, assetId)) {
    		            processStatus = "D";
    		        }

    		        // 3️⃣ Validate posting consistency
    		        if (isPostingMismatch(con, assetCode)) {
    		            if (processStatus.isEmpty()) {
    		                processStatus = "N";
    		            }
    		        }

    		    } catch (Exception e) {
    		        System.out.println("checkAssetAvailability ERROR -> " + e.getMessage());
    		    }

    		    return processStatus;
    		}

    	 private boolean hasPendingApproval(Connection con, String assetId) throws Exception {

    		    String sql = " SELECT 1 FROM am_asset_approval WHERE asset_id = ? AND process_status IN ('P','WA')";

    		    try (PreparedStatement ps = con.prepareStatement(sql)) {
    		        ps.setString(1, assetId);
    		        try (ResultSet rs = ps.executeQuery()) {
    		            return rs.next();
    		        }
    		    }
    		}
    	 
    	 private int getAssetCode(Connection con, String assetId) throws Exception {

    		    String sql = "SELECT asset_code FROM am_asset WHERE asset_id = ?";

    		    try (PreparedStatement ps = con.prepareStatement(sql)) {
    		        ps.setString(1, assetId);
    		        try (ResultSet rs = ps.executeQuery()) {
    		            if (rs.next()) {
    		                return rs.getInt("asset_code");
    		            }
    		        }
    		    }
    		    return 0;
    		}

    	 private boolean hasCurrentMonthActivity(Connection con, String assetId) throws Exception {

    		    String sql = " SELECT 1 FROM am_asset_improvement WHERE asset_id = ? AND MONTH(revalue_date) = MONTH(GETDATE()) "
    		    		+ "AND YEAR(revalue_date) = YEAR(GETDATE()) AND approval_status != 'REJECTED' ";

    		    try (PreparedStatement ps = con.prepareStatement(sql)) {
    		        ps.setString(1, assetId);
    		        try (ResultSet rs = ps.executeQuery()) {
    		            return rs.next();
    		        }
    		    }
    		}

    	 private boolean isPostingMismatch(Connection con, int assetCode) throws Exception {

    		    String sql = " SELECT (SELECT MAX(trans_id) FROM am_raisentry_post WHERE asset_code = ?) AS post_id, "
    		    		+ "(SELECT MAX(trans_id) FROM am_raisentry_TRANSACTION WHERE asset_code = ?) AS trans_id ";

    		    try (PreparedStatement ps = con.prepareStatement(sql)) {
    		        ps.setInt(1, assetCode);
    		        ps.setInt(2, assetCode);

    		        try (ResultSet rs = ps.executeQuery()) {
    		            if (rs.next()) {
    		                String postId = rs.getString("post_id");
    		                String transId = rs.getString("trans_id");

    		                if (postId != null && postId.equals(transId)) {

    		                    return !isIsoSuccessful(con, assetCode, postId);
    		                }
    		            }
    		        }
    		    }

    		    return false;
    		}
    	 
    	 private boolean isIsoSuccessful(Connection con, int assetCode, String transId) throws Exception {

    		    String sql = "SELECT iso FROM am_raisentry_TRANSACTION WHERE asset_code = ? AND trans_id = ? ";

    		    try (PreparedStatement ps = con.prepareStatement(sql)) {
    		        ps.setInt(1, assetCode);
    		        ps.setString(2, transId);

    		        try (ResultSet rs = ps.executeQuery()) {
    		            if (rs.next()) {
    		                return "000".equals(rs.getString("iso"));
    		            }
    		        }
    		    }

    		    return false;
    		}



//    	    public String getCodeName(String query) {
//    	        String result = "";
//    	        Connection con = null;
//    	        ResultSet rs = null;
//    	        PreparedStatement ps = null;
//    	//System.out.println("====getCodeName query=====  "+query);
//    	        try {
//    	            con = getConnection();
//    	            ps = con.prepareStatement(query);
//    	            rs = ps.executeQuery();
//    	            while (rs.next()) {
//    	                result = rs.getString(1) == null ? "" : rs.getString(1);
//    	            }
//    	        } catch (Exception er) {
//    	            System.out.println("Error in Query- getCodeName()... ->" + er);
//    	            er.printStackTrace();
//    	        } finally {
//    	            closeConnection(con, ps);
//    	        }
////    	        System.out.println("====getCodeName result=====  "+result);
//    	        return result;
//    	    }


    	    public String getCodeName(String query) {
    	        String resultValue = null;

    	        try (
    	            Connection con = getConnection();
    	            PreparedStatement ps = con.prepareStatement(query);
    	            ResultSet rs = ps.executeQuery()
    	        ) {
    	            if (rs.next()) {
    	                resultValue = rs.getString(1) == null ? "" : rs.getString(1);
    	            }

    	        } catch (Exception e) {
    	            System.out.println("WARN: Error in Query- getCodeName()... -> " + e.getMessage());
    	            e.printStackTrace();
    	        }

    	        return resultValue;
    	    }



    		public java.util.List getAssetSubCategoryByQuery(String filter, String statusparam) {
    			legend.admin.objects.SubCategory am = null;
    			java.util.List<legend.admin.objects.SubCategory> _list = new java.util.ArrayList<legend.admin.objects.SubCategory>();
    			String query = "SELECT SUB_CATEGORY_ID,SUB_CATEGORY_CODE,SUB_CATEGORY_NAME"
    					+ " ,SUB_CATEGORY_STATUS,Category_ID,user_id,create_date"
    					+ " FROM AM_AD_SUB_CATEGORY  ";

    			query = query + filter;
    			

    			try(Connection con = getConnection();
         PreparedStatement s = con.prepareStatement(query);) {
    	
    				s.setString(1, statusparam);
    				try (ResultSet rs = ps.executeQuery()) {
    				while (rs.next()) {

    					String assetSubCategoryId = rs.getString("SUB_CATEGORY_ID");
    					String assetSubCategoryCode = rs.getString("SUB_CATEGORY_CODE");
    					String assetSubCategory = rs.getString("SUB_CATEGORY_NAME");
    					String status = rs.getString("SUB_CATEGORY_STATUS");
    					String category = rs.getString("Category_ID");
    					String userid = rs.getString("user_id");
    					String createDate = sdf.format(rs.getDate("create_date"));
    					am = new legend.admin.objects.SubCategory(assetSubCategoryId,
    							assetSubCategoryCode, assetSubCategory, status, category, userid,
    							createDate);
    					_list.add(am);

    				}
    				}

    			} catch (Exception e) {
    				e.printStackTrace();
    			}

    			
    			return _list;

    		}

    		public legend.admin.objects.SubCategory getAssetSubCategoryByID(String id) {
    			String filter = " WHERE SUB_CATEGORY_ID = ?";
    			legend.admin.objects.SubCategory am = getAnAssetSubCategory(filter,id);
    			return am;

    		}

    		public legend.admin.objects.SubCategory getAssetSubCategoryByCode(String code) {
    			String filter = " WHERE SUB_CATEGORY_CODE = ?";
    			legend.admin.objects.SubCategory am = getAnAssetSubCategory(filter,code);
    			return am;

    		}

    		private legend.admin.objects.SubCategory getAnAssetSubCategory(String filter,String parameter) {
    			legend.admin.objects.SubCategory am = null;
    			String query = "SELECT SUB_CATEGORY_ID,SUB_CATEGORY_CODE,SUB_CATEGORY_NAME"
    					+ " ,SUB_CATEGORY_STATUS,Category_ID,user_id,create_date"
    					+ " FROM AM_AD_SUB_CATEGORY  ";

    			query = query + filter;
    			
    			try(Connection con = getConnection();
         PreparedStatement s = con.prepareStatement(query);) {
    			
    				s.setString(1, parameter);
    				try (ResultSet rs = ps.executeQuery()) {
        				while (rs.next()) {

    					String assetSubCategoryId = rs.getString("SUB_CATEGORY_ID");
    					String assetSubCategoryCode = rs.getString("SUB_CATEGORY_CODE");
    					String assetSubCategory = rs.getString("SUB_CATEGORY_NAME");
    					String status = rs.getString("SUB_CATEGORY_STATUS");
    					String category = rs.getString("Category_ID");
    					String userid = rs.getString("user_id");
    					String createDate = sdf.format(rs.getDate("create_date"));
    					am = new legend.admin.objects.SubCategory(assetSubCategoryId,
    							assetSubCategoryCode, assetSubCategory, status, category, userid,
    							createDate);

    				}
    				}

    			} catch (Exception e) {
    				e.printStackTrace();
    			}

    			
    			return am;

    		}

    		public boolean createAssetSubCategory(legend.admin.objects.SubCategory am) {

    			
    			boolean done = false;
    			String query = "INSERT INTO AM_AD_SUB_CATEGORY(SUB_CATEGORY_CODE,SUB_CATEGORY_NAME,"
    					+ "  SUB_CATEGORY_STATUS,Category_ID,user_id,create_date)"
    					+ " VALUES (?,?,?,?, ?,?)";
    	//		System.out.println("createAssetSubCategory Query: "+query);

    			try(Connection con = getConnection();
         PreparedStatement ps = con.prepareStatement(query);
         ResultSet rs = ps.executeQuery()) {
    				
    				ps.setString(1, am.getAssetSubCategoryCode());
    				ps.setString(2, am.getAssetSubCategory());
    				ps.setString(3, am.getStatus());
    				ps.setString(4, am.getCategory());
    				ps.setString(5, am.getUserid());
    				ps.setDate(6, dateConvert(new java.util.Date()));
    				done = (ps.executeUpdate() != -1);
    			} catch (Exception e) {
    				System.out.println(this.getClass().getName()
    						+ " ERROR:Error creating Sub-Category ->" + e.getMessage());
    			} finally {
    				closeConnection(con, ps);
    			}
    			return done;

    		}

    		public boolean updateAssetSubCategory(legend.admin.objects.SubCategory am) {

    			
    			boolean done = false;
    			String query = "UPDATE AM_AD_SUB_CATEGORY SET SUB_CATEGORY_CODE = ?,SUB_CATEGORY_NAME = ?,SUB_CATEGORY_STATUS =?,"
    					+ "  Category_ID = ?" + " WHERE sub_category_ID =?";
    			try (Connection con = getConnection();
         PreparedStatement ps = con.prepareStatement(query);){
    			
    				ps.setString(1, am.getAssetSubCategoryCode());
    				ps.setString(2, am.getAssetSubCategory());
    				ps.setString(3, am.getStatus());
    				ps.setString(4, am.getCategory());
    				// ps.setString(5, am.getUserid());
    				ps.setString(5, am.getAssetSubCategoryId());
    				done = (ps.executeUpdate() != -1);
    			} catch (Exception e) {
    				System.out.println(this.getClass().getName()
    						+ " ERROR:Error updating  Asset Sub Category ->" + e.getMessage());
    			} finally {
    				closeConnection(con, ps);
    			}
    			return done;

    		}

    		public java.util.ArrayList getStockUserByQuery(String filter) {
    			java.util.ArrayList _list = new java.util.ArrayList();
    			legend.admin.objects.StockUsers stockuser = null;
    			String query = "SELECT MTID, COMP_CODE, USER_CODE,BU_CODE,UTCODE, "
    					+ "USER_NAME, ADDRESS, STATUS,USER_ID,"
    					+ "CREATE_DATE FROM ST_INVENTORY_USERS WHERE MTID IS NOT NULL ";

    			query = query + filter;
    			

    			try(Connection con = getConnection();
         PreparedStatement ps = con.prepareStatement(query);
         ResultSet rs = ps.executeQuery()) {
    				while (rs.next()) {
    					String mtId = rs.getString("MTID");
    					String compCode = rs.getString("COMP_CODE");
    					String userBranch = rs.getString("BU_CODE");
    					String utCode = rs.getString("UTCODE");
    					String userCode = rs.getString("USER_CODE");
    					String userName = rs.getString("USER_NAME");
    					String address = rs.getString("ADDRESS");
    					String status = rs.getString("STATUS");
    					String userId = rs.getString("USER_ID");
    					String createD = sdf.format(rs.getDate("CREATE_DATE"));

    					stockuser = new legend.admin.objects.StockUsers();
    					stockuser.setId(mtId);
    					stockuser.setCompCode(compCode);
    					stockuser.setStockUserCode(userCode);
    					stockuser.setStockUserName(userName);
    					stockuser.setStockUserAddress(address);
    					stockuser.setStockUserStatus(status);
    					stockuser.setUserId(userId);
    					stockuser.setCreateDate(createD);
    					stockuser.setStockUserBranch(userBranch);
    					stockuser.setUtCode(utCode);
    					_list.add(stockuser);
    				}

    			} catch (Exception e) {
    				e.printStackTrace();
    			}

    			
    			return _list;

    		}

    		public legend.admin.objects.StockUsers getUserByUserID(String usercode) {
 //   			System.out.println("usercode in getUserByUserID: "+usercode);
    			legend.admin.objects.StockUsers stockuser = null;
    			String query = "SELECT * FROM ST_INVENTORY_USERS WHERE USER_CODE=? ";

    			

    			try (Connection con = getConnection();
         PreparedStatement ps = con.prepareStatement(query);
         ResultSet rs = ps.executeQuery()){
    				
    				ps.setString(1, usercode);
    				
    				while (rs.next()) {
    					String mtid = rs.getString("MTID");
    					String compCode = rs.getString("COMP_CODE");
    					String userBranch = rs.getString("BU_CODE");
    					String utCode = rs.getString("UTCODE");
    					String userCode = rs.getString("USER_CODE");
    					String userName = rs.getString("USER_NAME");
    					String createDate = sdf.format(rs.getDate("CREATE_DATE"));
    					String address = rs.getString("ADDRESS");
    					String status = rs.getString("STATUS");
    					String userId = rs.getString("User_id");
    					stockuser = new legend.admin.objects.StockUsers();
    					stockuser.setStockUserCode(userCode);
    					stockuser.setStockUserName(userName);
    					stockuser.setCompCode(compCode);
    					stockuser.setStockUserAddress(address);
    					stockuser.setCreateDate(createDate);
    					stockuser.setId(mtid);
    					stockuser.setStockUserStatus(status);
    					stockuser.setUserId(userId);
    					stockuser.setStockUserBranch(userBranch);
    					stockuser.setUtCode(utCode);

    				}

    			} catch (Exception e) {
    				e.printStackTrace();
    			}

    			
    			return stockuser;

    		}

    		public boolean createStockUser(legend.admin.objects.StockUsers ccode) {

    			Connection con = null;
    			PreparedStatement ps = null;
    			boolean done = false;
    			String query = "INSERT INTO ST_INVENTORY_USERS (COMP_CODE, BU_CODE,"
    					+ "UTCODE, USER_CODE, USER_NAME, ADDRESS,"
    					+ "STATUS, USER_ID, CREATE_DATE) "
    					+ " VALUES (?,?,?,?,?,?,?,?,?)";
    	//		System.out.println("query in createStockUser: "+query);
    			try {
    				con = getConnection();
    				ps = con.prepareStatement(query);
    				ps.setString(1, ccode.getCompCode());
    				ps.setString(2, ccode.getStockUserBranch());
    				ps.setString(3, ccode.getUtCode());
    				ps.setString(4, ccode.getStockUserCode());
    				ps.setString(5, ccode.getStockUserName());
    				ps.setString(6, ccode.getStockUserAddress());
    				ps.setString(7, ccode.getStockUserStatus());
    				ps.setString(8, ccode.getUserId());
    				ps.setDate(9, dateConvert(new java.util.Date()));
    				done = (ps.executeUpdate() != -1);

    			} catch (Exception e) {
    				System.out.println("WARNING:Error executing Query ->"
    						+ e.getMessage());
    			} finally {
    				closeConnection(con, ps);
    			}
    			return done;

    		}

    		public boolean updateStockUser(legend.admin.objects.StockUsers ccode) {

    			Connection con = null;
    			PreparedStatement ps = null;
    			boolean done = false;
    			String query = "UPDATE ST_INVENTORY_USERS SET"
    					+ " USER_NAME =?,ADDRESS =?,STATUS = ?"
    					+ " WHERE BU_CODE = ? AND UTCODE = ? AND USER_CODE = ?";
    	//		System.out.println("query in updateStockUser: "+query);
    			try {
    				con = getConnection();
    				ps = con.prepareStatement(query);
    				ps.setString(1, ccode.getStockUserName());
    				ps.setString(2, ccode.getStockUserAddress());
    				ps.setString(3, ccode.getStockUserStatus());
    				ps.setString(4, ccode.getStockUserBranch());
    				ps.setString(5, ccode.getUtCode());
    				ps.setString(6, ccode.getStockUserCode());
    				done = (ps.executeUpdate() != -1);

    			} catch (Exception e) {
    				System.out.println("WARNING:Error executing Query ->"
    						+ e.getMessage());
    			} finally {
    				closeConnection(con, ps);
    			}
    			return done;

    		}

    		public void sendAssetManagementMail(String usermails, String subject, String msgText1) {
    		    try {
    		        // Load mail properties
    		        Properties prop = new Properties();
    		        try (FileInputStream in = new FileInputStream("C:\\Property\\LegendPlus.properties")) {
    		            prop.load(in);
    		        }

    		        String host = prop.getProperty("mail.smtp.host");
    		        String from = prop.getProperty("mail-user");
    		        String mailAuthenticator = prop.getProperty("mailAuthenticator");

    		        String[] recipients = usermails.split(",");
    		        String to = recipients[0].trim();
    		        System.out.println("Mail To: " + to + "     Mail from: " + from);

    		        Session session;
    		        Message msg = new MimeMessage(Session.getDefaultInstance(prop));
    		        msg.setFrom(new InternetAddress(from));
    		        msg.setRecipients(Message.RecipientType.TO, new InternetAddress[]{ new InternetAddress(to) });
    		        msg.setSubject("Legend - " + subject);
    		        msg.setSentDate(new Date());
    		        msg.setText(msgText1); // Plain text content

    		        // Add CCs (if any)
    		        for (int i = 1; i < recipients.length; i++) {
    		            InternetAddress addressCopy = new InternetAddress(recipients[i].trim());
    		            msg.addRecipient(Message.RecipientType.CC, addressCopy);
    		        }

    		        // Handle authenticated vs non-authenticated sending
    		        if ("Y".equalsIgnoreCase(mailAuthenticator)) {
    		            final String user = prop.getProperty("mail-user");
    		            final String password = prop.getProperty("mail-password");
    		            String port = prop.getProperty("mail.smtp.port");
    		            String protocol = prop.getProperty("mail.smtp.ssl.protocols");

    		            Properties authProps = new Properties();
    		            authProps.put("mail.smtp.host", host);
    		            authProps.put("mail.smtp.port", port);
    		            authProps.put("mail.smtp.auth", "true");
    		            authProps.put("mail.smtp.starttls.enable", "true");
    		            authProps.put("mail.smtp.ssl.protocols", protocol);
    		            authProps.put("mail.smtp.ssl.trust", host);

    		            session = Session.getInstance(authProps, new jakarta.mail.Authenticator() {
    		                protected PasswordAuthentication getPasswordAuthentication() {
    		                    return new PasswordAuthentication(user, password);
    		                }
    		            });

    		            Transport.send(msg);
    		            System.out.println("Email Sent (authenticated).");

    		        } else {
    		            String port = prop.getProperty("mail.smtp.port");
    		            Properties noAuthProps = new Properties();
    		            noAuthProps.put("mail.smtp.host", host);
    		            noAuthProps.put("mail.smtp.port", port);
    		            noAuthProps.put("mail.smtp.auth", "false");

    		            session = Session.getDefaultInstance(noAuthProps, null);
    		            Transport transport = session.getTransport("smtp");
    		            transport.connect();
    		            transport.sendMessage(msg, msg.getAllRecipients());
    		            transport.close();
    		            System.out.println("Email Sent (no authentication).");
    		        }

    		    } catch (Exception ex) {
    		        ex.printStackTrace();
    		    }
    		}


public java.util.ArrayList getUsernotSignOutRecords(String sessionTimeOut)
 {

 	java.util.ArrayList list = new java.util.ArrayList();

		//String notSignOutquery = "select  user_id from gb_user_login  where time_out is null and datediff(minute, time_in, '"+df.getDateTime().substring(10)+"') / 60.0 > ("+sessionTimeOut+"/60.0)";
	//	String notSignOutquery = "select  user_id,time_in from gb_user_login  where time_out is null and datediff(minute, time_in, CONVERT(VARCHAR(8), GETDATE(), 108)) / 60.0 > ("+sessionTimeOut+"/60.0)";

		String notSignOutquery = "SELECT user_id,session_time,create_date,DATEDIFF(second, session_time, (SELECT CONVERT(VARCHAR, getdate(), 108))) AS difference"
				+ " from gb_user_login where time_out is null and DATEDIFF(second, session_time, (SELECT CONVERT(VARCHAR, getdate(), 108))) > 60 * ?";
//	 	System.out.println("<<<<<<notSignOutquery in getUsernotSignOutRecords: "+notSignOutquery+"   <<<<<<sessionTimeOut is : "+sessionTimeOut);
		
		try(Connection con = getConnection();
         PreparedStatement ps = con.prepareStatement(notSignOutquery);
         ResultSet rs = ps.executeQuery()){
		
		   // System.out.println("<<<<<<statement is : "+s);
	        ps.setString(1, sessionTimeOut);
			//rs = s.executeQuery(notSignOutquery);

//		    System.out.println("<<<<<<result set is : "+rs);
			while (rs.next())
			   {
				String notSignoutuserId = rs.getString("user_id");
//				System.out.println("<<<<<<notSignoutuserId is : "+notSignoutuserId);
				String timein = rs.getString("session_time");
//				System.out.println("<<<<<<timein is : "+timein);
				String createDate = rs.getString("create_date");
				String difference = rs.getString("difference");
				list.add(notSignoutuserId);
			   }
	 }
				 catch (Exception e)
					{
						e.getMessage();
					}

 	return list;
 }









//public boolean updateUsernotSignOutRecords( String userId,String mtid) throws SQLException
//{
//	Connection con = null;
//	PreparedStatement ps = null;
//	PreparedStatement ps1 = null;
//	boolean done = false;
//	//String date = String.valueOf(dateConvert(new java.util.Date()));
// 	//date = date.substring(0, 10);
////	System.out.println("======> userId in updateUsernotSignOutRecords: "+userId);
//	String query = "UPDATE am_gb_User SET login_status=0   where user_id = " + userId + "  and login_status != 0 ";
////	 System.out.println("======> query in updateUsernotSignOutRecords: "+query);
////	String mtid = htmlUtil.getCodeName("SELECT MAX(mtid) FROM  gb_user_login where USER_ID = " + userId + " ");
////	System.out.println("======> mtid in updateUsernotSignOutRecords: "+mtid);
//	String loginquery = "UPDATE gb_user_login SET time_out = session_time WHERE user_id =? AND MTID = ?";
////	System.out.println("query in updateUsernotSignOutRecords: "+query);
////	System.out.println("query in updateUsernotSignOutRecords: "+loginquery+"    <<<<<<mtid is : "+mtid);
//	try {
//		con = getConnection();
//		ps = con.prepareStatement(query);
////		ps.setString(1, userId);
//		done = (ps.executeUpdate() != -1);
//		ps1 = con.prepareStatement(query);
//		ps1.setString(1, userId);
//		ps1.setString(2, mtid);
//		done=( ps1.executeUpdate()!=-1);
//	} catch (Exception e) {
////		e.printStackTrace();
//		e.getMessage();
//	} finally {
//    	closeConnection(con, ps);
//    	closeConnection(con, ps1);
////		con.close();
//    }
//
////	closeConnection(con, ps);
//	return done;
//}

public boolean updateUsernotSignOutRecords(String userId, String mtid) throws SQLException {

    String updateUserQuery =
        "UPDATE am_gb_User SET login_status = 0 WHERE user_id = ? AND login_status != 0";

    String updateLoginQuery =
        "UPDATE gb_user_login SET time_out = session_time WHERE user_id = ? AND MTID = ?";

    try (Connection con = getConnection();
         PreparedStatement ps = con.prepareStatement(updateUserQuery);
         PreparedStatement ps1 = con.prepareStatement(updateLoginQuery)) {

        // First update
        ps.setString(1, userId);
        ps.executeUpdate();

        // Second update
        ps1.setString(1, userId);
        ps1.setString(2, mtid);
        ps1.executeUpdate();

        return true;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
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
    /*    try
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
        }*/
	} catch (Exception er) {
		System.out.println("Error in " + this.getClass().getName()
				+ "- getValue()... ->" + er.getMessage());
		er.printStackTrace();
	} finally {
    	closeConnection(con, ps);
    }

	return result;
}

public java.util.ArrayList getSqlAssetFullyDeprOld(String alertperiod)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
//	System.out.println("====alertperiod-----> "+alertperiod);
	date = date.substring(0, 10);
	String finacleTransId= null;
		String query = "SELECT (SELECT MONTH((SELECT CONVERT(VARCHAR(10),GETDATE(),102)))), (SELECT MONTH(coalesce((CONVERT(VARCHAR(10),(select ALERT_DATE from ASSET_ALERT_LOG WHERE ASSET_ALERT_LOG.ASSET_ID = AM_ASSET.ASSET_ID),102)),''))),  (select COUNT(*) from ASSET_ALERT_LOG WHERE ASSET_ALERT_LOG.ASSET_ID = AM_ASSET.ASSET_ID), DATEDIFF(MONTH, (SELECT CAST(GETDATE() AS DATE)),Dep_End_Date) AS DateDifference,Dep_End_Date, *FROM AM_ASSET " +
				"WHERE Dep_End_Date > (SELECT CAST(GETDATE() AS DATE)) AND DATEDIFF(MONTH, (SELECT CAST(GETDATE() AS DATE)),Dep_End_Date) < ("+alertperiod+"+1)  AND (Email1 !='' OR Email2 !='') "+
				"AND (select COUNT(*) from ASSET_ALERT_LOG WHERE ASSET_ALERT_LOG.ASSET_ID = AM_ASSET.ASSET_ID) < ("+alertperiod+"+1)" +
				"AND (SELECT MONTH((SELECT CONVERT(VARCHAR(10),GETDATE(),102)))) != (SELECT MONTH(coalesce((CONVERT(VARCHAR(10),(select DISTINCT ALERT_DATE from ASSET_ALERT_LOG WHERE ASSET_ALERT_LOG.ASSET_ID = AM_ASSET.ASSET_ID),102)),'')))";
	//	System.out.println("====query-----> "+query);
		Connection c = null;
	ResultSet rs = null;
	Statement s = null;

	try {
		    c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next())
			   {
				String StrassetId = rs.getString("ASSET_ID");
				String strDescription = rs.getString("DESCRIPTION");
				String strRegistrationNo = rs.getString("Registration_No");
				String strdatediff = rs.getString("DateDifference");
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
				String strsubcategoryCode = rs.getString("SUB_CATEGORY_CODE");
				String strbarCode = rs.getString("BAR_CODE");
				String strsbuCode = rs.getString("SBU_CODE");
				String strlpo = rs.getString("LPO");
				String StrassetCode = rs.getString("ASSET_CODE");
				int Strnoofitems = rs.getInt("QUANTITY");
				String Strlocation = rs.getString("Location");
				String Strspare1 = rs.getString("email1");
				String Strspare2 = rs.getString("email2");
				String Strspare3 = rs.getString("Spare_3");
				String Strspare4 = rs.getString("Spare_4");
				String Strspare5 = rs.getString("Spare_5");
				String Strspare6 = rs.getString("Spare_6");
				String Strmultiple = rs.getString("Multiple");
				String Strmemo = rs.getString("Memo");
				String Strmemovalue = rs.getString("MemoValue");
				int Strusefullife = rs.getInt("IMPROV_USEFULLIFE");
				String projectCode = rs.getString("PROJECT_CODE");
				String location = rs.getString("LOCATION");
				double strNBV = rs.getDouble("NBV");
//				System.out.println("====>>>StrassetId: "+StrassetId+"   strDescription: "+strDescription+"  Strspare1: "+Strspare1+"  Strspare2:"+Strspare2);
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setEffectiveDate(strEffectiveDate);
				newTransaction.setDescription(strDescription);
				newTransaction.setRegistrationNo(strRegistrationNo);
				newTransaction.setNbv(strNBV);
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
				newTransaction.setSubcategoryCode(strsubcategoryCode);
				newTransaction.setBarCode(strbarCode);
				newTransaction.setSbuCode(strsbuCode);
				newTransaction.setLpo(strlpo);
				newTransaction.setAssetId(StrassetId);
				newTransaction.setAssetCode(StrassetCode);
				newTransaction.setNoofitems(Strnoofitems);
				newTransaction.setLocation(Strlocation);
				newTransaction.setSpare1(Strspare1);
				newTransaction.setSpare2(Strspare2);
				newTransaction.setSpare3(Strspare3);
				newTransaction.setSpare4(Strspare4);
				newTransaction.setSpare5(Strspare5);
				newTransaction.setSpare6(Strspare6);
				newTransaction.setMemo(Strmemo);
				newTransaction.setMemovalue(Strmemovalue);
				newTransaction.setMultiple(Strmultiple);
				newTransaction.setUsefullife(Strusefullife);
				newTransaction.setProjectCode(projectCode);
				newTransaction.setLocation(location);
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


public List<newAssetTransaction> getSqlAssetFullyDepr(String alertperiod) {

    List<newAssetTransaction> list = new ArrayList<>();

    String query =
        "SELECT (SELECT MONTH(GETDATE())), " +
        "(SELECT MONTH(COALESCE(CONVERT(VARCHAR(10), (SELECT ALERT_DATE FROM ASSET_ALERT_LOG WHERE ASSET_ALERT_LOG.ASSET_ID = AM_ASSET.ASSET_ID), 102), ''))), " +
        "(SELECT COUNT(*) FROM ASSET_ALERT_LOG WHERE ASSET_ALERT_LOG.ASSET_ID = AM_ASSET.ASSET_ID), " +
        "DATEDIFF(MONTH, CAST(GETDATE() AS DATE), Dep_End_Date) AS DateDifference, " +
        "Dep_End_Date, * " +
        "FROM AM_ASSET " +
        "WHERE Dep_End_Date > CAST(GETDATE() AS DATE) " +
        "AND DATEDIFF(MONTH, CAST(GETDATE() AS DATE), Dep_End_Date) < (? + 1) " +
        "AND (Email1 != '' OR Email2 != '') " +
        "AND (SELECT COUNT(*) FROM ASSET_ALERT_LOG WHERE ASSET_ALERT_LOG.ASSET_ID = AM_ASSET.ASSET_ID) < (? + 1) " +
        "AND (SELECT MONTH(GETDATE())) != (SELECT MONTH(COALESCE(CONVERT(VARCHAR(10), (SELECT DISTINCT ALERT_DATE FROM ASSET_ALERT_LOG WHERE ASSET_ALERT_LOG.ASSET_ID = AM_ASSET.ASSET_ID), 102), '')))";

    try (Connection c = getConnection();
         PreparedStatement s = c.prepareStatement(query)) {

        int period = Integer.parseInt(alertperiod);
        s.setInt(1, period);
        s.setInt(2, period);

        try (ResultSet rs = s.executeQuery()) {

            while (rs.next()) {
                newAssetTransaction newTransaction = new newAssetTransaction();

                newTransaction.setAssetId(rs.getString("ASSET_ID"));
                newTransaction.setDescription(rs.getString("DESCRIPTION"));
                newTransaction.setRegistrationNo(rs.getString("Registration_No"));
                newTransaction.setNbv(rs.getDouble("NBV"));
                newTransaction.setDatepurchased(rs.getString("Date_purchased"));
                newTransaction.setAssetMake(rs.getString("Asset_Make"));
                newTransaction.setAssetModel(rs.getString("Asset_Model"));
                newTransaction.setAssetSerialNo(rs.getString("Asset_Serial_No"));
                newTransaction.setAssetEngineNo(rs.getString("Asset_Engine_No"));
                newTransaction.setSupplierName(rs.getString("Supplier_Name"));
                newTransaction.setAssetUser(rs.getString("Asset_User"));
                newTransaction.setAssetMaintenance(rs.getString("Asset_Maintenance"));
                newTransaction.setCostPrice(rs.getDouble("Cost_Price"));
                newTransaction.setAuthorizedBy(rs.getString("Authorized_By"));
                newTransaction.setWhTax(rs.getString("Wh_Tax"));
                newTransaction.setPostingDate(rs.getString("Posting_Date"));
                newTransaction.setEffectiveDate(rs.getString("Effective_Date"));
                newTransaction.setPurchaseReason(rs.getString("Purchase_Reason"));
                newTransaction.setSubjectTOVat(rs.getString("Subject_TO_Vat"));
                newTransaction.setAssetStatus(rs.getString("Asset_Status"));
                newTransaction.setState(rs.getString("State"));
                newTransaction.setDriver(rs.getString("Driver"));
                newTransaction.setUserID(rs.getString("User_ID"));
                newTransaction.setBranchCode(rs.getString("BRANCH_CODE"));
                newTransaction.setSectionCode(rs.getString("SECTION_CODE"));
                newTransaction.setDeptCode(rs.getString("DEPT_CODE"));
                newTransaction.setCategoryCode(rs.getString("CATEGORY_CODE"));
                newTransaction.setSubcategoryCode(rs.getString("SUB_CATEGORY_CODE"));
                newTransaction.setBarCode(rs.getString("BAR_CODE"));
                newTransaction.setSbuCode(rs.getString("SBU_CODE"));
                newTransaction.setLpo(rs.getString("LPO"));
                newTransaction.setAssetCode(rs.getString("ASSET_CODE"));
                newTransaction.setNoofitems(rs.getInt("QUANTITY"));
                newTransaction.setLocation(rs.getString("Location"));
                newTransaction.setSpare1(rs.getString("email1"));
                newTransaction.setSpare2(rs.getString("email2"));
                newTransaction.setSpare3(rs.getString("Spare_3"));
                newTransaction.setSpare4(rs.getString("Spare_4"));
                newTransaction.setSpare5(rs.getString("Spare_5"));
                newTransaction.setSpare6(rs.getString("Spare_6"));
                newTransaction.setMultiple(rs.getString("Multiple"));
                newTransaction.setMemo(rs.getString("Memo"));
                newTransaction.setMemovalue(rs.getString("MemoValue"));
                newTransaction.setUsefullife(rs.getInt("IMPROV_USEFULLIFE"));
                newTransaction.setProjectCode(rs.getString("PROJECT_CODE"));

                list.add(newTransaction);
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}

public java.util.ArrayList getNewAssetSqlRecordsForUnCapitalised()
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
				"ASSET_CODE,ERROR_MESSAGE,ASSET_TYPE,Vat_Value,Wh_Tax_Value,QUANTITY,TRAN_TYPE,Location,Spare_1,Spare_2,Multiple,Memo,"+
				"MemoValue,IMPROV_USEFULLIFE, SUB_CATEGORY_CODE, SPARE_3,SPARE_4,SPARE_5,SPARE_6, "+
				"PROJECT_CODE,LOCATION "+
				"FROM NEW_ASSET_INTERFACE WHERE POSTED = 'N' AND ASSET_TYPE = 'U' AND INTEGRIFY_ID IS NOT NULL ";
//	Transaction transaction = null;
//     System.out.println("====query in getNewAssetSqlRecords-----> "+query);
	

	try(Connection con = getConnection();
         PreparedStatement ps = con.prepareStatement(query);
         ResultSet rs = ps.executeQuery()) {
		
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
				String strsubcategoryCode = rs.getString("SUB_CATEGORY_CODE");
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
				double StrVatValue = rs.getDouble("Vat_Value");
				int Strnoofitems = rs.getInt("QUANTITY");
				String Strlocation = rs.getString("Location");
				String Strspare1 = rs.getString("Spare_1");
				String Strspare2 = rs.getString("Spare_2");
				String Strspare3 = rs.getString("Spare_3");
				String Strspare4 = rs.getString("Spare_4");
				String Strspare5 = rs.getString("Spare_5");
				String Strspare6 = rs.getString("Spare_6");
				String Strmultiple = rs.getString("Multiple");
				String Strmemo = rs.getString("Memo");
				String Strmemovalue = rs.getString("MemoValue");
				int Strusefullife = rs.getInt("IMPROV_USEFULLIFE");
				String projectCode = rs.getString("PROJECT_CODE");
				String location = rs.getString("LOCATION");
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
				newTransaction.setSubcategoryCode(strsubcategoryCode);
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
				newTransaction.setVatValue(StrVatValue);
				newTransaction.setTranType(Strtrantype);
				newTransaction.setNoofitems(Strnoofitems);
				newTransaction.setLocation(Strlocation);
				newTransaction.setSpare1(Strspare1);
				newTransaction.setSpare2(Strspare2);
				newTransaction.setSpare3(Strspare3);
				newTransaction.setSpare4(Strspare4);
				newTransaction.setSpare5(Strspare5);
				newTransaction.setSpare6(Strspare6);
				newTransaction.setMemo(Strmemo);
				newTransaction.setMemovalue(Strmemovalue);
				newTransaction.setMultiple(Strmultiple);
				newTransaction.setUsefullife(Strusefullife);
				newTransaction.setProjectCode(projectCode);
				newTransaction.setLocation(location);
				_list.add(newTransaction);
			   }
	 }
				 catch (Exception e)
					{
						e.printStackTrace();
					}
					
	return _list;
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
				"ASSET_CODE,ERROR_MESSAGE,ASSET_TYPE,Vat_Value,Wh_Tax_Value,QUANTITY,TRAN_TYPE,Location,Spare_1,Spare_2,Multiple,Memo,"+
				"MemoValue,IMPROV_USEFULLIFE, SUB_CATEGORY_CODE, SPARE_3,SPARE_4,SPARE_5,SPARE_6, "+
				"PROJECT_CODE,LOCATION "+
				"FROM NEW_ASSET_INTERFACE WHERE POSTED = 'N' AND ASSET_TYPE = 'U' ";
//	Transaction transaction = null;
//     System.out.println("====query in getNewAssetSqlRecords-----> "+query);
	

	try(Connection con = getConnection();
         PreparedStatement ps = con.prepareStatement(query);
         ResultSet rs = ps.executeQuery()) {
		  
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
				String strsubcategoryCode = rs.getString("SUB_CATEGORY_CODE");
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
				double StrVatValue = rs.getDouble("Vat_Value");
				int Strnoofitems = rs.getInt("QUANTITY");
				String Strlocation = rs.getString("Location");
				String Strspare1 = rs.getString("Spare_1");
				String Strspare2 = rs.getString("Spare_2");
				String Strspare3 = rs.getString("Spare_3");
				String Strspare4 = rs.getString("Spare_4");
				String Strspare5 = rs.getString("Spare_5");
				String Strspare6 = rs.getString("Spare_6");
				String Strmultiple = rs.getString("Multiple");
				String Strmemo = rs.getString("Memo");
				String Strmemovalue = rs.getString("MemoValue");
				int Strusefullife = rs.getInt("IMPROV_USEFULLIFE");
				String projectCode = rs.getString("PROJECT_CODE");
				String location = rs.getString("LOCATION");
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
				newTransaction.setSubcategoryCode(strsubcategoryCode);
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
				newTransaction.setVatValue(StrVatValue);
				newTransaction.setTranType(Strtrantype);
				newTransaction.setNoofitems(Strnoofitems);
				newTransaction.setLocation(Strlocation);
				newTransaction.setSpare1(Strspare1);
				newTransaction.setSpare2(Strspare2);
				newTransaction.setSpare3(Strspare3);
				newTransaction.setSpare4(Strspare4);
				newTransaction.setSpare5(Strspare5);
				newTransaction.setSpare6(Strspare6);
				newTransaction.setMemo(Strmemo);
				newTransaction.setMemovalue(Strmemovalue);
				newTransaction.setMultiple(Strmultiple);
				newTransaction.setUsefullife(Strusefullife);
				newTransaction.setProjectCode(projectCode);
				newTransaction.setLocation(location);
				_list.add(newTransaction);
			   }
	 }
				 catch (Exception e)
					{
						e.printStackTrace();
					}
					
	return _list;
}


public boolean assetalertNotificationsOld(String asset_Id, int assetCode,int alertNo) {
	Connection con = null;
	PreparedStatement ps = null;
	boolean done = false;
	String query = "INSERT INTO ASSET_ALERT_LOG(ASSET_ID, "
			+ "ASSET_CODE, ALERT_DATE, ALERT_NO) "
			+ " VALUES(?, ?, ?, ?)";

	try {
		con = getConnection();
		ps = con.prepareStatement(query);
		ps.setString(1, asset_Id);
		ps.setInt(2, assetCode);
		ps.setDate(3, dateConvert(new java.util.Date()));
		ps.setInt(4, alertNo);

		done = (ps.executeUpdate() != -1);

	} catch (Exception e) {
		System.out.println("WARNING:Error executing Query in assetalertNotifications ->"
				+ e.getMessage());
	} finally {
		closeConnection(con, ps);
	}
	return done;
}

public boolean assetalertNotifications(String assetId, int assetCode, int alertNo) {
    String query = "INSERT INTO ASSET_ALERT_LOG (ASSET_ID, ASSET_CODE, ALERT_DATE, ALERT_NO) "
                 + "VALUES (?, ?, ?, ?)";

    try (Connection con = getConnection();
         PreparedStatement ps = con.prepareStatement(query)) {

        ps.setString(1, assetId);
        ps.setInt(2, assetCode);
        ps.setDate(3, dateConvert(new java.util.Date()));
        ps.setInt(4, alertNo);

        int rowsAffected = ps.executeUpdate();
        return rowsAffected > 0;

    } catch (Exception e) {
    	System.out.println("Error executing assetalertNotifications for assetId=" + assetId + e);
        return false;
    }
}


public java.util.ArrayList getPPMRecords()
 {
 	java.util.ArrayList list = new java.util.ArrayList();

 	String durationquery = "SELECT  *FROM PPM_Schedule_View WHERE TRANSID+BRANCH_CODE+SUB_CATEGORY_CODE not in (SELECT TRANSID+BRANCH_CODE+SUB_CATEGORY_CODE FROM FM_PPM_LOG) ";
 //	System.out.println("<<<<<<<durationquery: "+durationquery);
 	Connection c = null;
	ResultSet rs = null;
	Statement s = null;
	try {
		    c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(durationquery);
			while (rs.next())
			   {
				String duration = rs.getString("DURATION");
				String transId = rs.getString("TRANSID");
				String branchCode = rs.getString("BRANCH_CODE");
				String subcatCode = rs.getString("SUB_CATEGORY_CODE");
				String description = rs.getString("DESCRIPTION");
				String categoryCode = rs.getString("CATEGORY_CODE");
				String vendorCode = rs.getString("VENDOR_CODE");
				String lastserviceDate = rs.getString("LASTSERVICE_DATE");
//				String q1Date = rs.getString("Q1_DATE");
				String q1DueDate = rs.getString("Q1_DUE_DATE");
				String q1Status = rs.getString("Q1_STATUS");
//				String q2Date = rs.getString("Q2_DATE");
				String q2DueDate = rs.getString("Q2_DUE_DATE");
				String q2Status = rs.getString("Q2_STATUS");
//				String q3Date = rs.getString("Q3_DATE");
				String q3DueDate = rs.getString("Q3_DUE_DATE");
				String q3Status = rs.getString("Q3_STATUS");
//				String q4Date = rs.getString("Q4_DATE");
				String q4DueDate = rs.getString("Q4_DUE_DATE");
				String q4Status = rs.getString("Q4_STATUS");
				String type = rs.getString("TYPE");
				String status = rs.getString("STATUS");
				FMppmAllocation ppmTrans = new FMppmAllocation();
				ppmTrans.setDuration(duration);
				ppmTrans.setTransId(transId);
				ppmTrans.setBranchCode(branchCode);
				ppmTrans.setCategoryCode(categoryCode);
				ppmTrans.setSubCategoryCode(subcatCode);
				ppmTrans.setVendorCode(vendorCode);
				ppmTrans.setDescription(description);
				ppmTrans.setFq_Due1(q1DueDate);
				ppmTrans.setFq_DueStatus1(q1Status);
//				ppmTrans.setSecondQuaterDate(q2Date);
				ppmTrans.setFq_Due2(q2DueDate);
				ppmTrans.setFq_DueStatus2(q2Status);
//				ppmTrans.setThirdQauterDate(q3Date);
				ppmTrans.setFq_Due3(q3DueDate);
				ppmTrans.setFq_DueStatus3(q3Status);
//				ppmTrans.setFourthQuaterDate(q4Date);
				ppmTrans.setFq_Due4(q4DueDate);
				ppmTrans.setFq_DueStatus4(q4Status);
				ppmTrans.setType(type);
				ppmTrans.setStatus(status);
				list.add(ppmTrans);
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

public boolean ppmlog(String transId,String branchCode, String subcatCode, String vendorCode, String description) {
	Connection con = null;
	PreparedStatement ps = null;
	boolean done = false;
	String query = "INSERT INTO FM_PPM_LOG(BRANCH_CODE,SUB_CATEGORY_CODE,VENDOR_CODE,DESCRIPTION,CREATE_DATE,TRANSID) "
			+ " VALUES(?, ?, ?, ?,?,?)";

	try {
		con = getConnection();
		ps = con.prepareStatement(query);
		ps.setString(1, branchCode);
		ps.setString(2, subcatCode);
		ps.setString(3, vendorCode);
		ps.setString(4, description);
		ps.setDate(5, dateConvert(new java.util.Date()));
		ps.setString(6, transId);
		done = (ps.executeUpdate() != -1);

	} catch (Exception e) {
		System.out.println("WARNING:Error executing Query in ppmlog ->"
				+ e.getMessage());
	} finally {
		closeConnection(con, ps);
	}
	return done;
}

public java.util.ArrayList getSendMailSqlRecordsOld()
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
		String query = " select *from MAILS_TO_SEND where status is null and MAIL_ADDRESS is NOT NULL and MAIL_ADDRESS != ''";

	Connection c = null;
	ResultSet rs = null;
	PreparedStatement s = null;

	try {
		    c = getConnection();
			s = c.prepareStatement(query);
			rs = s.executeQuery();
			while (rs.next())
			   {
				int strid = rs.getInt("ID");
				String straddress = rs.getString("MAIL_ADDRESS");
				String strheader = rs.getString("MAIL_HEADER");
				String strbody= rs.getString("MAIL_BODY");
				String strstatus = rs.getString("STATUS");
				SendMail mail = new SendMail();
				mail.setId(strid);
				mail.setAddress(straddress);
				mail.setHeader(strheader);
				mail.setBody(strbody);
				mail.setStatus(strstatus);
				_list.add(mail);
			   }
	 }
				 catch (Exception e)
					{
					 System.out.println("WARN: getSendMailSqlRecords failed --> " + e.getMessage());
					e.printStackTrace();
					}

	return _list;
}

public java.util.ArrayList<SendMail> getSendMailSqlRecords() {
    java.util.ArrayList<SendMail> mailList = new java.util.ArrayList<>();
    String query = "SELECT * FROM MAILS_TO_SEND WHERE STATUS IS NULL AND MAIL_ADDRESS IS NOT NULL AND MAIL_ADDRESS != ''";

    try (Connection con = getConnection();
         PreparedStatement ps = con.prepareStatement(query);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            SendMail mail = new SendMail();
            mail.setId(rs.getInt("ID"));
            mail.setAddress(rs.getString("MAIL_ADDRESS"));
            mail.setHeader(rs.getString("MAIL_HEADER"));
            mail.setBody(rs.getString("MAIL_BODY"));
            mail.setStatus(rs.getString("STATUS"));
            mailList.add(mail);
        }

    } catch (Exception e) {
    	System.out.println("WARN: getSendMailSqlRecords failed" + e);
    }

    return mailList;
}


public boolean updateSendMailRecordsOld( int id)
{
	Connection con = null;
	PreparedStatement ps = null;
	boolean done = false;
	String date = String.valueOf(dateConvert(new java.util.Date()));
 	//date = date.substring(0, 10);
	String query = "UPDATE MAILS_TO_SEND SET SENDDATE= '"+date+"',STATUS = 'SENT'   where ID ="+id+" ";
//	System.out.println("query in updateSendMailRecords: "+query);
	try {
		con = getConnection();
		ps = con.prepareStatement(query);
		done = (ps.executeUpdate() != -1);
	} catch (Exception e) {
		e.printStackTrace();
	}

	return done;
}

public boolean updateSendMailRecords(int id) {
    String query = "UPDATE MAILS_TO_SEND SET SENDDATE = ?, STATUS = 'SENT' WHERE ID = ?";

    try (Connection con = getConnection();
         PreparedStatement ps = con.prepareStatement(query)) {

        ps.setDate(1, dateConvert(new java.util.Date()));
        ps.setInt(2, id);

        int rowsAffected = ps.executeUpdate();
        return rowsAffected > 0;

    } catch (Exception e) {
    	System.out.println("Error updating mail record ID=" + id + e);
        return false;
    }
}

// +++++++++++++Without Authentication++++++++++++++++++++++++++++++++++++++
//public boolean sendRecordMail(String usermails, String subject,String msgText1)
//{
//	boolean sent = false;
////	System.out.println("Just called the sendApprovalEmail API");
//	try
//	{
//		Properties prop = new Properties();
//		File file = new File("C:\\Property\\LegendPlus.properties");
////   				System.out.print("Absolute Path:>>> "+file.getAbsolutePath());
////  				System.out.print("Able to load file ");
//		FileInputStream in = new FileInputStream(file);
//		prop.load(in);
////   				System.out.print("Able to load properties into prop");
//		String host = prop.getProperty("mail.smtp.host");
//		String from = prop.getProperty("mail-user");
//		Session session = Session.getDefaultInstance(prop,null);
//
//		boolean sessionDebug = true;
//		Properties props = System.getProperties();
//		props.put("mail.host",host);
//		props.put("mail.transport.protocols","smtp");
////		System.out.println("setting auth");
//		session = Session.getDefaultInstance(props,null);
//		session.setDebug(sessionDebug);
//
////   				System.out.println("From = "+from);
////   				System.out.println("point 1");
//
//		Message msg = new MimeMessage(session);
////		System.out.println("point 2");
//		msg.setFrom(new InternetAddress(from));
////   				System.out.println("point 3");
//		String to = usermails;
////   				System.out.println("To: "+to);
//		String []usermaillist = usermails.split(",");
//		int No = usermaillist.length;
//		System.out.println("=====usermails: "+usermails+"      No: "+No);
//		for(int j=0;j<No;j++){
//   					System.out.println("<<<<<<<<usermaillist[j]: "+usermaillist[j]);
//				InternetAddress[] address = { new InternetAddress(usermaillist[j]) };
//				msg.setRecipients(Message.RecipientType.TO,address);
//		}
//
////				InternetAddress[] address = { new InternetAddress(to) };
////		System.out.println("point 4");
//// 				msg.setRecipients(Message.RecipientType.TO,address);
//
//		 msg.setSubject(subject);
//
////		System.out.println("point 6");
//		msg.setSentDate(new Date());
////   				System.out.println("point 7");
//
//		String msgBody = msgText1;
////	    System.out.print("The mail body: "+msgBody);
//	  //  String s ="How are you   doing?".replaceAll("  +","  ");
////	    String[] str = msgText1.split(" ");
////	    System.out.println(str.length);
////	    for(String msgBody: str){
//	    	msg.setText(msgBody);
////	        System.out.println(msgBody);
////   			    }
//
//	    msg.saveChanges();
//
////		System.out.println("point 8");
//
//
//
////   				System.out.println("point 9");
//
////		System.out.println("point 10");
//
//Transport tr = session.getTransport("smtp");
////   	    System.out.println("point 11");
//tr.connect();
////System.out.println("point 12");
////Security.getProviders("smtp");
//
////   		System.out.println("point test");
////tr.sendMessage(msg, msg.getAllRecipients());
//tr.send(msg);
////System.out.println("point 13");
//tr.close();
//sent = true;
//
////System.out.println("point 14");
//	}
//	catch (Exception ex)
//	{
//		System.out.println("point 15 in sendRecordMail");
//		ex.printStackTrace();
//	}
//	return sent;
//
//}
//+++++++++++++End Without Authentication++++++++++++++++++++++++++++++++++++++


public boolean sendRecordMail(String usermails, String subject, String msgText1) {
    boolean sent = false;

    try {
        // Load mail properties
        Properties prop = new Properties();
        try (FileInputStream in = new FileInputStream("C:\\Property\\LegendPlus.properties")) {
            prop.load(in);
        }

        String host = prop.getProperty("mail.smtp.host");
        String from = prop.getProperty("mail-user");
        String mailAuthenticator = prop.getProperty("mailAuthenticator");
        String[] recipients = usermails.split(",");

        if (recipients.length == 0) {
            System.out.println("No recipients provided.");
            return false;
        }

        Session session;
        Message msg = new MimeMessage(Session.getDefaultInstance(prop));
        msg.setFrom(new InternetAddress(from));
        msg.setSubject("Legend - " + subject);
        msg.setSentDate(new Date());
        msg.setText(msgText1);

        // Set first email as TO
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients[0].trim()));

        // Add CC for the remaining
        for (int i = 1; i < recipients.length; i++) {
            msg.addRecipient(Message.RecipientType.CC, new InternetAddress(recipients[i].trim()));
        }

        // Authenticated
        if ("Y".equalsIgnoreCase(mailAuthenticator)) {
            final String user = prop.getProperty("mail-user");
            final String password = prop.getProperty("mail-password");
            String port = prop.getProperty("mail.smtp.port");
            String protocol = prop.getProperty("mail.smtp.ssl.protocols");

            Properties authProps = new Properties();
            authProps.put("mail.smtp.host", host);
            authProps.put("mail.smtp.port", port);
            authProps.put("mail.smtp.auth", "true");
            authProps.put("mail.smtp.starttls.enable", "true");
            authProps.put("mail.smtp.ssl.protocols", protocol);
            authProps.put("mail.smtp.ssl.trust", host);

            session = Session.getInstance(authProps, new jakarta.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(user, password);
                }
            });

            Transport.send(msg);
            System.out.println("Email sent (authenticated).");

        } else {
            // No authentication
            String port = prop.getProperty("mail.smtp.port");

            Properties noAuthProps = new Properties();
            noAuthProps.put("mail.smtp.host", host);
            noAuthProps.put("mail.smtp.port", port);
            noAuthProps.put("mail.smtp.auth", "false");

            session = Session.getDefaultInstance(noAuthProps, null);
            Transport transport = session.getTransport("smtp");
            transport.connect();
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();

            System.out.println("Email sent (no authentication).");
        }

        sent = true;

    } catch (Exception ex) {
        System.out.println("Error in sendRecordMail");
        ex.printStackTrace();
    }

    return sent;
}


public java.util.ArrayList getFleetTypeByQuery(String filter) {
	java.util.ArrayList _list = new java.util.ArrayList();
	legend.admin.objects.fleetType type = null;

	String query = "SELECT FT_MTID, FT_TYPE_CODE, DESCRIPTION"
			+ ", GL_ACCOUNT, USER_ID, CREATE_DATE"
			+ " FROM FT_PROCESSING_TYPE WHERE FT_MTID IS NOT NULL ";

	
	query = query + filter;
	try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery()){
		while (rs.next()) {
			String fleetTypeId = rs.getString("FT_MTID");
			String typeCode = rs.getString("FT_TYPE_CODE");
			String description = rs.getString("DESCRIPTION");
			String glaccount = rs.getString("GL_ACCOUNT");
			String userId = rs.getString("USER_ID");
			String createDate = rs.getString("CREATE_DATE");

			type = new legend.admin.objects.fleetType();
			type.setFleetTypeId(fleetTypeId);
			type.setFleetTypeCode(typeCode);
			type.setDescription(description);
			type.setUserId(userId);
			type.setCreateDate(createDate);
			type.setGlAccount(glaccount);
			_list.add(type);
		}

	} catch (Exception e) {
		e.printStackTrace();
	}

	
	return _list;

}

public legend.admin.objects.fleetType getFleetTypeByID(String TypeID) {
	legend.admin.objects.fleetType type = null;
	String query = "SELECT FT_MTID, FT_TYPE_CODE, DESCRIPTION"
			+ ", GL_ACCOUNT, USER_ID, CREATE_DATE"
			+ " FROM FT_PROCESSING_TYPE WHERE FT_MTID = ?";

	Connection c = null;
	ResultSet rs = null;
//	Statement s = null;
	PreparedStatement s = null;

	try {
		c = getConnection();
//		s = c.createStatement();
//		rs = s.executeQuery(query);
		s = c.prepareStatement(query.toString());
		s.setString(1, TypeID);
		rs = s.executeQuery();
		while (rs.next()) {
			String fleetTypeId = rs.getString("FT_MTID");
			String typeCode = rs.getString("FT_TYPE_CODE");
			String description = rs.getString("DESCRIPTION");
			String glaccount = rs.getString("GL_ACCOUNT");
			String userId = rs.getString("USER_ID");
			String createDate = rs.getString("CREATE_DATE");
			type = new legend.admin.objects.fleetType();
			type.setFleetTypeId(fleetTypeId);
			type.setFleetTypeCode(typeCode);
			type.setDescription(description);
			type.setUserId(userId);
			type.setCreateDate(createDate);
			type.setGlAccount(glaccount);
		}

	} catch (Exception e) {
		e.printStackTrace();
	}

	finally {
		closeConnection(c, s, rs);
	}
	return type;
}


public boolean createFleetType(legend.admin.objects.fleetType type) {

	Connection con = null;
	PreparedStatement ps = null;
	boolean done = false;
	String query = "INSERT INTO FT_PROCESSING_TYPE(FT_TYPE_CODE, DESCRIPTION"
			+ ", GL_ACCOUNT, USER_ID, CREATE_DATE,FT_MTID)" + " VALUES (?,?,?,?,?,?)";
		int mtid = Integer.parseInt(new ApplicationHelper().getGeneratedId("FT_PROCESSING_TYPE"));
	//	System.out.println("################ the mtid value is " + mtid);
	try {
		con = getConnection();
		ps = con.prepareStatement(query);
		// ps.setLong(1, System.currentTimeMillis());
		ps.setString(1, type.getFleetTypeCode());
		ps.setString(2, type.getDescription());
		ps.setString(3, type.getGlAccount());
		ps.setString(4, type.getUserId());
		ps.setDate(5, dateConvert(new java.util.Date()));
		ps.setInt(6,mtid );

		done = (ps.executeUpdate() != -1);
	} catch (Exception e) {
		System.out.println("WARNING:Error executing Query in createFleetType ->"
				+ e.getMessage());
	} finally {
		closeConnection(con, ps);
	}
	return done;

}


public boolean updateFleetType(legend.admin.objects.fleetType type) {

	Connection con = null;
	PreparedStatement ps = null;
	boolean done = false;
	String query = "UPDATE FT_PROCESSING_TYPE" + " SET FT_TYPE_CODE = ?"
			+ ",DESCRIPTION = ?,GL_ACCOUNT = ?" + " WHERE FT_MTID = ?";

	try {
		con = getConnection();
		ps = con.prepareStatement(query);
		ps.setString(1, type.getFleetTypeCode());
		ps.setString(2, type.getDescription());
		ps.setString(3, type.getGlAccount());
		ps.setString(4, type.getFleetTypeId());
		done = (ps.executeUpdate() != -1);

	} catch (Exception e) {
		System.out.println("WARNING:Error executing Query in updateFleetType ->"
				+ e.getMessage());
	} finally {
		closeConnection(con, ps);
	}
	return done;
}

public legend.admin.objects.fleetType getfleetTypeByTypeCode(String fleetTypeCode) {

	legend.admin.objects.fleetType type = null;
	String query = "SELECT FT_MTID,FT_TYPE_CODE, DESCRIPTION"
			+ ", GL_ACCOUNT, USER_ID, CREATE_DATE,FT_MTID"
			+ " FROM FT_PROCESSING_TYPE WHERE FT_TYPE_CODE = ?";

	Connection c = null;
	ResultSet rs = null;
//	Statement s = null;
	PreparedStatement s = null;

	try {
		c = getConnection();
//		s = c.createStatement();
//		rs = s.executeQuery(query);
		s = c.prepareStatement(query);
		s.setString(1, fleetTypeCode);
		rs = s.executeQuery();
		while (rs.next()) {
			String fleetTypeId = rs.getString("FT_MTID");
			String typeCode = rs.getString("FT_TYPE_CODE");
			String description = rs.getString("DESCRIPTION");
			String glaccount = rs.getString("GL_ACCOUNT");
			String userId = rs.getString("USER_ID");
			String createDate = rs.getString("CREATE_DATE");
			type = new legend.admin.objects.fleetType();
			type.setFleetTypeId(fleetTypeId);
			type.setFleetTypeCode(typeCode);
			type.setDescription(description);
			type.setUserId(userId);
			type.setCreateDate(createDate);
			type.setGlAccount(glaccount);

		}

	} catch (Exception e) {
		e.printStackTrace();
	}

	finally {
		closeConnection(c, s, rs);
	}
	return type;

}

public java.util.ArrayList getFleetMintenaceSqlRecords()
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
		String query = " select *from MAILS_TO_SEND where status is null ";

	Connection c = null;
	ResultSet rs = null;
	Statement s = null;

	try {
		    c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next())
			   {
				int strltId = rs.getInt("LT_ID");
				String strtransId = rs.getString("HIST_ID");
				String strType = rs.getString("TYPE");
				String strAssetId= rs.getString("ASSET_ID");
				double strCostPrice = rs.getDouble("COST_PRICE");
				String strregistrationNo = rs.getString("REGISTRATION_NO");
				String strrepairDate= rs.getString("REPAIRED_DATE");
				String strTechType= rs.getString("TECHNICIAN_TYPE");
				String strTechName= rs.getString("TECHNICIAN_NAME");
				double strMilleageBefore= rs.getDouble("MILLEAGE_BEFORE_MAINT");
				double strMilleageAfter= rs.getDouble("MILLEAGE_AFTER_MAINT");
				String strDetails= rs.getString("DETAILS");
				String strcomponent= rs.getString("COMPONENT_REPLACED");
				String strlastPmDate= rs.getString("LAST_PM_DATE");
				String strFirstNotDate = rs.getString("FIRST_NOT_DATE");
				String strNotificFreq= rs.getString("NOTIFICATION_FREQ");
				String strNextNotDate= rs.getString("NEXT_NOT_DATE");
				String strNextPMDate= rs.getString("NEXT_PM_DATE");
				String strMaintType= rs.getString("MAINTENANCE_TYPE");
				String strUserId= rs.getString("USER_ID");
				String strStatus= rs.getString("STATUS");
				String strInvoiceNo= rs.getString("INVOICE_NO");
				String strHistId = rs.getString("HIST_ID");
				double strVatAmt= rs.getDouble("VAT_AMT");
				double strWhtAmt= rs.getDouble("WHT_AMT");
				String strRaiseEntry = rs.getString("RAISED_ENTRY");
				String strReversal= rs.getString("REVERSAL");
				String strBranchId= rs.getString("branch_id");
				String strCreateDate= rs.getString("CREATE_DATE");
				int strAssetCode= rs.getInt("ASSET_CODE");
				String strProjectCode= rs.getString("PROJECT_CODE");

				FleetManatainanceRecord fleet = new FleetManatainanceRecord();
				fleet.setLtId(strltId);
				fleet.setHistId(strtransId);
				fleet.setType(strType);
				fleet.setAssetId(strAssetId);
				fleet.setCost(strCostPrice);
				fleet.setRegistrationNo(strregistrationNo);
				fleet.setDateOfRepair(strrepairDate);
				fleet.setTechnicianName(strTechName);
				fleet.setTechnicianType(strTechType);
				fleet.setDetails(strDetails);
				fleet.setMilleageBeforeMaintenance(strMilleageBefore);
				fleet.setMilleageAfterMaintenance(strMilleageAfter);
				fleet.setComponentReplaced(strcomponent);
				fleet.setLastPerformedDate(strlastPmDate);
				fleet.setNotificationFreq(strNotificFreq);
				fleet.setFirstNotificationDate(strFirstNotDate);
				fleet.setNextNotificationDate(strNextNotDate);
				fleet.setNextPerformedDate(strNextPMDate);
				fleet.setMaintenanceType(strMaintType);
				fleet.setUserId(strUserId);
				fleet.setInvoiceNo(strInvoiceNo);
				fleet.setHistId(strHistId);
				fleet.setVatAmt(strVatAmt);
				fleet.setWhtAmt(strWhtAmt);
				fleet.setBranchId(strBranchId);
				fleet.setAssetCode(strAssetCode);
				fleet.setProjectCode(strProjectCode);
				fleet.setRaiseEntry(strRaiseEntry);
				fleet.setReversal(strReversal);
				fleet.setStatus(strStatus);
				_list.add(fleet);
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


public java.util.ArrayList getMaintenanceEnvironmentSqlRecords()
{
	java.util.ArrayList _list = new java.util.ArrayList();
    String query;
	Connection c = null;
	ResultSet rs = null;
	Statement s = null;
    String date = String.valueOf(dateConvert(new Date()));
    date = date.substring(0, 10);
    String finacleTransId = null;
    query = " select ID,HIST_ID,BRANCH_ID,ITEM_TYPE,DESCRIPTION,USER_ID,VAT_AMT,WHT_AMT,'2' AS SUPERVISOR,COST_PRICE,INTEGRIFY_ID,"
    		+ "VAT_RATE,WHT_RATE,"
    		+ "STATUS from FM_SOCIALENVIRONMENT_SUMMARY WHERE STATUS = 'APPROVED' AND INTEGRIFY_ID IS NOT NULL AND ERROR_MESSAGE IS NULL ";
//    c = null;
//    rs = null;
//    s = null;
    try
    {
//		System.out.println("<<<<<<=====query in getMaintenanceEnvironmentSqlRecords: "+query);
	    c = getConnection();
		s = c.createStatement();
		rs = s.executeQuery(query);
		while (rs.next())
		   {
            int strid = rs.getInt("ID");
            String histId = rs.getString("HIST_ID");
            String itemType = rs.getString("ITEM_TYPE");
            String userId = rs.getString("USER_ID");
            String superId = rs.getString("SUPERVISOR");
            double costPrice = rs.getDouble("COST_PRICE");
            double vatAmt = rs.getDouble("VAT_AMT");
            double whtAmt = rs.getDouble("WHT_AMT");
            String strCost = rs.getString("COST_PRICE");
            String integrifyId = rs.getString("INTEGRIFY_ID");
            String status = rs.getString("STATUS");
            String branchId = rs.getString("BRANCH_ID");
            String description = rs.getString("DESCRIPTION");
            double vatRate = rs.getDouble("VAT_RATE");
            double whtRate = rs.getDouble("WHT_RATE");
            FleetManatainanceRecord record = new FleetManatainanceRecord();
            record.setId(String.valueOf(strid));
            record.setHistId(histId);
            record.setBranchId(branchId);
            record.setType(itemType);
            record.setCost(costPrice);
            record.setVatAmt(vatAmt);
            record.setWhtAmt(whtAmt);
            record.setIntegrifyId(integrifyId);
            record.setSuperId(superId);
            record.setUserId(userId);
            record.setDescription(description);
            record.setStatus(status);
            record.setVatRate(vatRate);
            record.setWhtRate(whtRate);
            _list.add(record);
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


public String[] setApprovalDataTranType(String id, String asset_id, String tranType, String supervisor, String userId, String amount){

//String q ="select asset_id, asset_status,user_ID,supervisor,Cost_Price,Posting_Date,description,effective_date,BRANCH_CODE from am_asset where asset_id ='" +id+"'";
 //String currentDate  = reArrangeDate(getCurrentDate1());

    String[] result= new String[12];
    String query = "";
    Connection con = null;
//        Statement ps = null;
        PreparedStatement s = null;
        ResultSet rs = null;

       query = "select asset_id,user_ID,supervisor,Cost_Price,Posting_Date, description,effective_date,BRANCH_CODE,Asset_Status from am_asset where asset_id =?";


        try {
            con = getConnection();
//            ps = con.createStatement();
//            rs = ps.executeQuery(query);
    		s = con.prepareStatement(query.toString());
    		s.setString(1, asset_id);
    		rs = s.executeQuery();
            while (rs.next()) {
         //       result[0] = rs.getString(1);
                result[0] = id;
                result[1] = userId;
                result[2] = supervisor;
                result[3] = amount;
                result[4] = rs.getString(5);
                result[5] = rs.getString(6);
                result[6] = rs.getString(7);
                result[7] = rs.getString(8);
                result[8] = rs.getString(9);

            }
//               System.out.println("Query setApprovalData "+query);
        } catch (Exception ex) {
            System.out.println("WARN: Error fetching setApprovalDataTranType ->" + ex);
        } finally {
            closeConnection(con, ps, rs);
        }
		result[9] = tranType;
		result[10] = "P";
        return result;

}//setApprovalData()

public String[] setApprovalDirectData(String id, String asset_id, String tranType, String supervisor, String userId, String amount, String branchCode,String description)
{
//    System.out.println("the $$$$$$$$$$$ id: "+id+"  asset_id: "+asset_id+"   tranType: "+tranType+"  supervisor: "+supervisor+"   userId: "+userId+"  amount: "+amount+"  branchCode: "+branchCode);
    String result[] = new String[12];
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try
    {
        result[0] = id;
        result[1] = userId;
        result[2] = supervisor;
        result[3] = amount;
//        System.out.println((new StringBuilder("<<<<===Id: ")).append(id).append("    userId: ").append(userId).append("  supervisor: ").append(supervisor).append("   amount: ").append(amount).toString());
        result[4] = tranType;
        result[5] = description;
        result[6] = String.valueOf(dateConvert(new Date()));
        result[7] = branchCode;
 //       System.out.println("<<<<===tranType: "+tranType+"    Date: "+dateConvert(new Date())+"  branchCode: "+branchCode);
        result[8] = "ACTIVE";
    }
    catch(Exception ex)
    {
        System.out.println("WARN: Error fetching setApprovalDirectData ->"+ex);
    }
    result[9] = tranType;
    result[10] = "A";
    return result;
}

public boolean updateESMSErrorMessageRecord(String integrifyId, String status)
{
    Connection con;
    PreparedStatement ps;
    boolean done;
    String query;
    con = null;
    ps = null;
    done = false;
    query = "UPDATE FM_SOCIALENVIRONMENT_SUMMARY SET ERROR_MESSAGE = ? WHERE INTEGRIFY_ID = ?";
//    System.out.println("<<<<===query in updateESMSErrorMessageRecord: "+query);
 //   System.out.println("<<<<===integrifyId: "+integrifyId+"    status: "+status);
    try
    {
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, status);
        ps.setString(2, integrifyId);
        done = ps.executeUpdate() != -1;
//        System.out.println("<<<<===done: "+done);

	} catch (Exception ex) {
	    System.out.println("WARNING:Error executing Query in updateESMSErrorMessageRecord ->" + ex);
	} finally {
	    closeConnection(con, ps);
	}
    return done;
}

public boolean updateESMSPostingRecord(String integrifyId, String status,String errorMessage)
{
    Connection con;
    PreparedStatement ps;
    boolean done;
    String query;
    con = null;
    ps = null;
    done = false;
    query = "UPDATE FM_SOCIALENVIRONMENT_SUMMARY SET STATUS = ?, ERROR_MESSAGE = ? WHERE INTEGRIFY_ID = ?";
    try
    {
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, status);
        ps.setString(2, errorMessage);
        ps.setString(3, integrifyId);
        done = ps.executeUpdate() != -1;

	} catch (Exception ex) {
	    System.out.println("WARNING:Error executing Query in updateESMSPostingRecord ->" + ex);
	} finally {
	    closeConnection(con, ps);
	}
    return done;
}

public String IsIntegrifyIdExist(String integrifyId)
{
    String result;
    String query;
   
    result = "";
    query = "SELECT INTEGRIFY_ID FROM AM_INVOICE_NO WHERE INTEGRIFY_ID = '"+integrifyId+"'";
   
    try
    (Connection con = getConnection();
             PreparedStatement s = con.prepareStatement(query);){
      
        for(rs = s.executeQuery(query); rs.next();)
        {
            result = rs.getString("INTEGRIFY_ID");
        }

//        closeConnection(c, s, rs);
    }
    catch(Exception e)
    {
        System.out.println("CompanyHandler:IsIntegrifyIdExist:Erorr getting Integrify status "+e);
    } 
    return result;
}

public boolean createIntegrify(String histId, String integrifyId, String tranType)
{
   
    boolean done;
    String query;
   
    done = false;
    query = "INSERT INTO AM_INVOICE_NO(ASSET_ID,INVOICE_NO,TRANS_TYPE,CREATE_DATE,INTEGRIFY_I" +
"D,LPO) VALUES (?,?,?,?,?,?)"
;
//    System.out.println("################ the histId value is "+histId+"  integrifyId: "+integrifyId+"  tranType: "+tranType);
    try
    (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query);){
       
        ps.setString(1, histId);
        ps.setString(2, integrifyId);
        ps.setString(3, tranType);
        ps.setDate(4, dateConvert(new Date()));
        ps.setString(5, integrifyId);
        ps.setString(6, "");
        done = ps.executeUpdate() != -1;
//        System.out.println("################ value of done"+done);
    }
    catch(Exception e)
    {
        System.out.println("WARNING:Error executing Query in createIntegrify ->"+e.getMessage());
    } 
    return done;
}

public ArrayList getvendorCriteriaByStatus(String status)
{
    String filter = " AND status=? ";
    ArrayList _list = getvendorCriteriaByQuery(filter,status);
    return _list;
}

public ArrayList getvendorCriteriaByQuery(String filter,String statusparam)
{
	PreparedStatement s = null;
    ArrayList _list;
    String query;
    _list = new ArrayList();
    query = "SELECT id, criteria_code, criteria, status,menu_type, user_id, create_date FROM " +
"VENDOR_ASSESSMENT_CRITERIA WHERE id IS NOT NULL "
;
    query = (new StringBuilder(String.valueOf(query))).append(filter).toString();
    System.out.println("========>>>>>>query in getvendorCriteriaByQuery: "+query+"      statusparam: "+statusparam);
    try
    {
        con = getConnection();
//        stmt = con.createStatement();
        s = con.prepareStatement(query.toString());
//        s.setString(1, statusparam);
        vendorCriteria dispres = new vendorCriteria();


//        con = getConnection();
//      ps = con.createStatement();
//      rs = ps.executeQuery(query);
//		s = con.prepareStatement(query.toString());
		s.setString(1, statusparam);
		rs = s.executeQuery();
      while (rs.next()) {
          String id = rs.getString("id");
          System.out.println("====>>>Id: "+id);
          String criteriaCode = rs.getString("criteria_code");
          String criteria = rs.getString("criteria");
          String status = rs.getString("status");
          String userId = rs.getString("user_id");
          String menuType = rs.getString("menu_type");
          String createDate = sdf.format(rs.getDate("create_date"));
    	  dispres = new vendorCriteria();
          dispres.setId(id);
          dispres.setCriteriaCode(criteriaCode);
          dispres.setCriteria(criteria);
          dispres.setStatus(status);
          dispres.setUserId(userId);
          dispres.setMenuType(menuType);
          dispres.setCreateDate(createDate);
          _list.add(dispres);
      }
    }
    catch(Exception ex)
    {
        ex.printStackTrace();
    } finally {
    	closeConnection(con, stmt, rs);
	}
    return _list;
}

public vendorCriteria getvendorCriteriaByCriteriaCode(String criteriaCode)
{
    String filter = " AND criteria_code=?";
    vendorCriteria dispres = null;
    ArrayList _list = getvendorCriteriaByQuery(filter,criteriaCode);
    if(_list != null && _list.size() > 0)
    {
        dispres = (vendorCriteria)_list.get(0);
    }
    return dispres;
}

public vendorCriteria getvendorCriteriaById(String reasonId)
{
    String filter = " AND Id= ? ";
    vendorCriteria dispres = null;
    ArrayList _list = getvendorCriteriaByQuery(filter,reasonId);
    dispres = (vendorCriteria)_list.get(0);
    return dispres;
}

public boolean createVendorCriteria(vendorCriteria crite)
{
    boolean done;
    String query;
   
    done = false;
    query = "INSERT INTO VENDOR_ASSESSMENT_CRITERIA(CRITERIA_CODE, CRITERIA, STATUS, USER_ID," +
" MENU_TYPE, CREATE_DATE)  VALUES(?, ?, ?, ?, ?, ?)"
;
    try
    (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query);){
       
        ps.setString(1, crite.getCriteriaCode());
        ps.setString(2, crite.getCriteria());
        ps.setString(3, crite.getStatus());
        ps.setString(4, crite.getUserId());
        ps.setString(5, crite.getMenuType());
        ps.setDate(6, dateConvert(crite.getCreateDate()));
        done = ps.executeUpdate() != -1;
    }
    catch(Exception e)
    {
        System.out.println("WARNING:Error executing Query in createVendorCriteria ->"+e.getMessage());
    } 
//    closeConnection(con, ps);
    return done;
}

public boolean updateVendorCriteria(vendorCriteria crite)
{
    Connection con;
    PreparedStatement ps;
    boolean done;
    String query;
    con = null;
    ps = null;
    done = false;
    query = "UPDATE VENDOR_ASSESSMENT_CRITERIA SET CRITERIA_CODE=?, CRITERIA=?, STATUS=?, USE" +
"R_ID=?, MENU_TYPE = ? WHERE ID=?"
;
    try
    {
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, crite.getCriteriaCode());
        ps.setString(2, crite.getCriteria());
        ps.setString(3, crite.getStatus());
        ps.setString(4, crite.getUserId());
        ps.setString(5, crite.getMenuType());
        ps.setString(6, crite.getId());
        done = ps.executeUpdate() != -1;
    }
    catch(Exception e)
    {
        e.printStackTrace();
        System.out.println("WARNING:Error executing Query in updateVendorCriteria ->"+e.getMessage());
    } finally {
    	closeConnection(con, ps);
	}
 //   closeConnection(con, ps);

    return done;
}


public java.util.ArrayList getFleetSqlRecords()
{
	java.util.ArrayList _list = new java.util.ArrayList();
    String query;
	
    String date = String.valueOf(dateConvert(new Date()));
    date = date.substring(0, 10);
    String finacleTransId = null;
    query = " select LT_ID,HIST_ID,BRANCH_ID,TYPE,DETAILS,USER_ID,VAT_AMT,WHT_AMT,'2' AS SUPERVISOR,COST_PRICE,INTEGRIFY_ID,STATUS from FT_MAINTENANCE_HISTORY "+
" WHERE STATUS ='APPROVED' AND INTEGRIFY_ID IS NOT NULL AND ERROR_MESSAGE IS NULL ";
   
    try
    (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query);
    		ResultSet rs = ps.executeQuery();){
//    	System.out.println("<<<<<<=====query in getFleetSqlRecords: "+query);
		while (rs.next())
		   {
            int strid = rs.getInt("LT_ID");
            String histId = rs.getString("HIST_ID");
            String itemType = rs.getString("TYPE");
            String userId = rs.getString("USER_ID");
            String superId = rs.getString("SUPERVISOR");
            double costPrice = rs.getDouble("COST_PRICE");
            double vatAmt = rs.getDouble("VAT_AMT");
            double whtAmt = rs.getDouble("WHT_AMT");
            String strCost = rs.getString("COST_PRICE");
            String integrifyId = rs.getString("INTEGRIFY_ID");
            String status = rs.getString("STATUS");
            String branchId = rs.getString("BRANCH_ID");
            String description = rs.getString("DETAILS");
            FleetManatainanceRecord record = new FleetManatainanceRecord();
            record.setId(String.valueOf(strid));
            record.setHistId(histId);
            record.setBranchId(branchId);
            record.setType(itemType);
            record.setCost(costPrice);
            record.setVatAmt(vatAmt);
            record.setWhtAmt(whtAmt);
            record.setIntegrifyId(integrifyId);
            record.setSuperId(superId);
            record.setUserId(userId);
            record.setDescription(description);
            record.setStatus(status);
            _list.add(record);
		   }
		}
	 catch (Exception e)
		{
			e.printStackTrace();
		}
		
//    closeConnection(c, s, rs);
return _list;

}

public boolean updateFTPostingRecord(String integrifyId, String status)
{
   
    boolean done;
    String query;
 
    done = false;
 //   System.out.println("=====STATUS in updateFTPostingRecord====: "+status+"    ======integrifyId: "+integrifyId);
    query = "UPDATE FT_MAINTENANCE_HISTORY SET STATUS = ? WHERE INTEGRIFY_ID = ?";
    try(Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query);){
      
        ps.setString(1, status);
        ps.setString(2, integrifyId);
        done = ps.executeUpdate() != -1;
	} catch (Exception ex) {
	    System.out.println("WARNING:Error executing Query in updateFTPostingRecord ->" + ex);
	} 
//    closeConnection(con, ps);
    return done;
}

public boolean updateFTErrorMessageRecord(String integrifyId, String status)
{
    Connection con;
    PreparedStatement ps;
    boolean done;
    String query;
    con = null;
    ps = null;
    done = false;
    query = "UPDATE FT_MAINTENANCE_HISTORY SET ERROR_MESSAGE = ? WHERE INTEGRIFY_ID = ?";
//    System.out.println("<<<<===integrifyId: "+integrifyId+"    status: "+status);
    try
    {
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, status);
        ps.setString(2, integrifyId);
        done = ps.executeUpdate() != -1;
//        System.out.println("<<<<===done: "+done);

	} catch (Exception ex) {
	    System.out.println("WARNING:Error executing Query in updateFTErrorMessageRecord ->" + ex);
	} finally {
	    closeConnection(con, ps);
	}
//    closeConnection(con, ps);
    return done;
}


public java.util.ArrayList getFMSqlRecords() throws NamingException
{
//	System.out.println("<<<<<<=====Inside query in getFMSqlRecords: ");
	java.util.ArrayList _list = new java.util.ArrayList();
    String query;
	Connection c = null;
	ResultSet rs = null;
	PreparedStatement s = null;
	Context initContext = new InitialContext();
	 String dsJndi = "java:/legendPlus";
     DataSource ds = (DataSource) initContext.lookup(
     		dsJndi);
    String date = String.valueOf(dateConvert(new Date()));
//    System.out.println("<<<<<<=====date in getFMSqlRecords: "+date);
    date = date.substring(0, 10);
    String finacleTransId = null;
//    System.out.println("<<<<<<date in getFMSqlRecords: "+date);
    query = " select LT_ID,HIST_ID,ERROR_MESSAGE,BRANCH_ID,TYPE,DETAILS,VAT_AMT,WHT_AMT,USER_ID,'2' AS SUPERVISOR,COST_PRICE,INTEGRIFY_ID,STATUS from FM_MAINTENANCE_HISTORY "+
" WHERE STATUS ='APPROVED' AND INTEGRIFY_ID IS NOT NULL AND ERROR_MESSAGE IS NULL ";
    try
    {
//    	System.out.println("<<<<<<=====query in getFMSqlRecords: "+query);
//    	System.out.println("<<<<<<=====Starting to call get connection");
    	//c = getConnection();
    	c = ds.getConnection();
		//s = c.createStatement();
 //   	System.out.println("<<<<<<=====get connection called...");
	    s = c.prepareStatement(query);
		//rs = s.executeQuery(query);
	    rs = s.executeQuery();
		while (rs.next())
		   {
            int strid = rs.getInt("LT_ID");
            String histId = rs.getString("HIST_ID");
//            System.out.println("<<<<<<=====histId in getFMSqlRecords: "+histId);
            String itemType = rs.getString("TYPE");
            String userId = rs.getString("USER_ID");
            String superId = rs.getString("SUPERVISOR");
            double costPrice = rs.getDouble("COST_PRICE");
            double vatAmt = rs.getDouble("VAT_AMT");
            double whtAmt = rs.getDouble("WHT_AMT");
            String strCost = rs.getString("COST_PRICE");
//            System.out.println("<<<<<<=====strCost in getFMSqlRecords: "+strCost);
            String integrifyId = rs.getString("INTEGRIFY_ID");
 //           System.out.println("<<<<<<=====integrifyId in getFMSqlRecords: "+integrifyId);
            String status = rs.getString("STATUS");
            String branchId = rs.getString("BRANCH_ID");
            String description = rs.getString("DETAILS");
            String errorMessage = rs.getString("ERROR_MESSAGE");
//            System.out.println("<<<<<<=====description in getFMSqlRecords: "+description);
            FleetManatainanceRecord record = new FleetManatainanceRecord();
            record.setId(String.valueOf(strid));
            record.setHistId(histId);
            record.setBranchId(branchId);
            record.setType(itemType);
            record.setCost(costPrice);
            record.setVatAmt(vatAmt);
            record.setWhtAmt(whtAmt);
            record.setIntegrifyId(integrifyId);
            record.setSuperId(superId);
            record.setUserId(userId);
            record.setDescription(description);
            record.setStatus(status);
            record.setIntegrifyId(integrifyId);
            record.setErrorMessage(errorMessage);
  //          System.out.println("<<<<<<=====status in getFMSqlRecords: "+status);
            _list.add(record);
		   }
	//	System.out.println("<<<<<<===== Done with getFMSqlRecords");
		}
	 catch (Exception e)
		{
			e.getMessage();
		}
		finally
		{
			closeConnection(c, s, rs);
		}
//    closeConnection(c, s, rs);
	return _list;

	}



public boolean updateFMPostingRecord(String integrifyId, String status)
{
    Connection con;
    PreparedStatement ps;
    boolean done;
    String query;
    con = null;
    ps = null;
    done = false;
    query = "UPDATE FM_MAINTENANCE_HISTORY SET STATUS = ? WHERE INTEGRIFY_ID = ?";
    try
    {
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, status);
        ps.setString(2, integrifyId);
        done = ps.executeUpdate() != -1;

	} catch (Exception ex) {
	    System.out.println("WARNING:Error executing Query in updateFMPostingRecord ->" + ex);
	} finally {
	    closeConnection(con, ps);
	}
//    closeConnection(con, ps);
    return done;
}


public boolean updateFMErrorMessageRecord(String integrifyId, String status)
{
    Connection con;
    PreparedStatement ps;
    boolean done;
    String query;
    con = null;
    ps = null;
    done = false;
    query = "UPDATE FM_MAINTENANCE_HISTORY SET ERROR_MESSAGE = ? WHERE INTEGRIFY_ID = ?";
//    System.out.println("<<<<===integrifyId: "+integrifyId+"    status: "+status);
    try
    {
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, status);
        ps.setString(2, integrifyId);
        done = ps.executeUpdate() != -1;
//        System.out.println("<<<<===done: "+done);

	} catch (Exception ex) {
	    System.out.println("WARNING:Error executing Query in updateFMErrorMessageRecord ->" + ex);
	} finally {
	    closeConnection(con, ps);
	}
//    closeConnection(con, ps);
    return done;
}


public boolean saveTest(String integrifyId,String Description,String RegistrationNo,String VendorAC,String Datepurchased,
		String AssetMake,String AssetModel,String AssetSerialNo,String AssetEngineNo,String SupplierName,String AssetUser,
		String AssetMaintenance,double CostPrice,double VatableCost,String AuthorizedBy,String WhTax,double whtaxamount,String PostingDate,String EffectiveDate,
		String PurchaseReason,String SubjectTOVat,String AssetStatus,String State,String Driver,String UserID,String branchCode,
		String sectionCode,String deptCode,String categoryCode,String subcategoryCode,String barCode,String sbuCode,String lpo,String invoiceNo,String supervisor,
		String posted,String assetId,String assetCode,String assettype,String branch_id,String dept_id,String section_id,
		String category_id,String sub_category_id,double vatamount,String residualvalue,int whtaxvalue,String groupid,int recno,
		String systemIp,String spare_1,String spare_2,String spare_3,String spare_4,String spare_5,String spare_6,String projectCode,String delimiter) throws Exception, Throwable
{

//		String integrifyId,String Description,String RegistrationNo,String VendorAC,String Datepurchased,
//		String AssetMake,String AssetModel,String AssetSerialNo,String AssetEngineNo,String SupplierName,String AssetUser,
//		String AssetMaintenance,double CostPrice,double VatableCost,String AuthorizedBy,String WhTax,double whtaxamount,String PostingDate,String EffectiveDate,
//		String PurchaseReason,String SubjectTOVat,String AssetStatus,String State,String Driver,String UserID,String branchCode,
//		String sectionCode,String deptCode,String categoryCode,String subcategoryCode,String barCode,String sbuCode,String lpo,String invoiceNo,String supervisor,
//		String posted,String assetId,String assetCode,String assettype,String branch_id,String dept_id,String section_id,String category_id,String subcategory_id,double vatamount,String residualvalue,int whtaxvalue,
//		String location,String memovalue,String memo, String spare1,String spare2, String spare3,String spare4, String spare5,String spare6,String multiple,String projectCode,String groupId) throws Exception, Throwable {

//	String location = "";
	String vat_amount = "0.0";
	String vatable_cost = "0.0";
	String wh_tax_amount = "0";
	String province = "0";
	String noOfMonths = "0";
	String residual_value = "0";
	String warrantyStartDate = null;
	String expiryDate = null;
	String memo = "N";
	String location = "0";
//	double costPrice =  newassettrans.getCostPrice();
	String amountPTD = "0.0";
//	String integrify = newassettrans.getIntegrifyId();
//	System.out.println("Branch Code:    "+branchCode+"  deptCode: "+deptCode+"  sectionCode: "+sectionCode+"  categoryCode:  "+categoryCode);
//	System.out.println("Branch Id:    "+branch_id+"  dept Id: "+dept_id+"  sectionId: "+section_id+"  categoryId:  "+category_id);
	String asset_id = getNewIdentity(branch_id,
    		dept_id, section_id, category_id,delimiter);
//	System.out.println("Asset Id=====:    "+asset_id);

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
//    if (location == null || location.equals("")) {
//        location = "0";
//    }
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
    if (sub_category_id == null || sub_category_id.equals("")) {
    	sub_category_id = "0";
    }
    if (subcategoryCode == null || subcategoryCode.equals("")) {
    	subcategoryCode = "0";
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

//    if (memo == null || memo.equals("")) {
//    	memo = "N";
//	}
    if (projectCode == null || projectCode.equals("")) {
    	projectCode = "0";
    }
    if (groupid == null || groupid.equals("")) {
    	groupid = "0";
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
//	System.out.println("newDateDay: "+newDateDay+"  newDateMonth: "+strnewDateMonth+"  newDateYear:  "+newDateYear);
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
    String status = "";
    String user_id = "";
//    String multiple = "N";
    String partPAY = "N";
    String fullyPAID = "Y";
    String deferPay = "N";
    int selectTax = 0;
    String MacAddress = "";
    String SystemIp = "";
    String section = "";
    String memovalue = "0";
    String multiple = "";
    String rate = getDepreciationRate(categoryCode);
  //  System.out.println("Rate in rinsertAssetRecord : "+rate);
//    System.out.println("Depreciation Date: "+depreciation_start_date+"  rate:  "+rate);
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
                         "system_ip,mac_address,asset_code,memo,memoValue,INTEGRIFY,SUB_CATEGORY_ID,SUB_CATEGORY_CODE,SPARE_3,SPARE_4,SPARE_5,SPARE_6,PROJECT_CODE,GROUP_ID) " +

                         "VALUES" +
                         "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                         "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

//For Uncapitalized Assets
    String createQueryU = "INSERT INTO AM_ASSET_UNCAPITALIZED      " +
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
    "system_ip,mac_address,asset_code,memo,memoValue,INTEGRIFY,SUB_CATEGORY_ID,SUB_CATEGORY_CODE,SPARE_3,SPARE_4,SPARE_5,SPARE_6,PROJECT_CODE,GROUP_ID) " +

    "VALUES" +
    "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
    "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";



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
      //  con = dbConnection.getConnection("legendPlus");
        if(assettype.equalsIgnoreCase("C")){
       // 	System.out.println("assettype in rinsertAssetRecord for C: "+assettype);
          ps = con.prepareStatement(createQueryC);
        }
//        if(assettype.equalsIgnoreCase("U")){
//        //	System.out.println("assettype in rinsertAssetRecord for U: "+assettype);
//           ps = con.prepareStatement(createQueryU);
//          // require_depreciation = "N";
//        }
//        System.out.println("assettype in rinsertAssetRecord: "+assettype);
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
        ps.setDouble(21, (CostPrice-10.00));
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
        ps.setString(48, spare_1);
        ps.setString(49, spare_2);
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
        ps.setInt(77, Integer.parseInt(sub_category_id));
        ps.setString(78, categoryCode);
        ps.setString(79, spare_3);
        ps.setString(80, spare_4);
        ps.setString(81, spare_5);
        ps.setString(82, spare_6);
        ps.setString(83, projectCode);
        ps.setString(84, groupid);
        ps.execute();
//         System.out.println("createQueryC: "+createQueryC);
        rinsertAssetRecord2(integrifyId,Description,RegistrationNo,VendorAC,Datepurchased,
        		AssetMake,AssetModel,AssetSerialNo,AssetEngineNo,SupplierName,AssetUser,
        		AssetMaintenance,CostPrice,VatableCost,AuthorizedBy,WhTax,whtaxamount,PostingDate,EffectiveDate,
        		PurchaseReason,SubjectTOVat,AssetStatus,State,Driver,UserID,branchCode,require_depreciation,
        		sectionCode,deptCode,categoryCode,subcategoryCode,barCode,sbuCode,lpo,invoiceNo,supervisor,
        		posted,asset_id,asset_Code,assettype,branch_id,dept_id,section_id,depreciation_end_date,
        		section,spare_1,spare_2,status,partPAY,fullyPAID,
        		deferPay,SystemIp,MacAddress,who_to_remind,email_1,who_to_remind_2,email2,raise_entry,
        		noOfMonths,warrantyStartDate,expiryDate,category_id,sub_category_id,vatamount,residual_value,whtaxvalue,
        		location,memovalue,memo,spare_1,spare_2,spare_3,spare_4,spare_5,spare_6,multiple,groupid);
//
//      insGrpToAm_Invoice_No(asset_id,lpo,invoiceNo,"Asset Creation",groupid);
//
//       String page1 = "ASSET GROUP CREATION RAISE ENTRY";
//       String flag= "";
// 	  	String partPay="";
// 	  String url = "";
// 	  	String qryBranch =" SELECT branch_name from am_ad_branch where branch_id='"+branch_id+"'";
// 	   	//String Branch = approvalRec.getCodeName(qryBranch);
// 	   //	String subjectT= adGroup.subjectToVat(group_id);
// 	   //	String whT= adGroup.whTax(group_id);
// 	   	String Name =findObject(" SELECT full_name from am_gb_user where user_id='"+supervisor+"'");
//	//	System.out.println("Approved by Name in Save: "+Name+"  User Id: "+Integer.parseInt(supervisor));
// 	   //	String IdInt =findObject(" SELECT User_id from am_gb_user where user_name='"+UserID+"'");
// 	  if(assettype.equalsIgnoreCase("C")){
// 	   	url = "DocumentHelp.jsp?np=groupAssetPosting&amp;id=" + groupid + "&pageDirect=Y";
//      }
// 	  if(assettype.equalsIgnoreCase("U")){
//   	   	url = "DocumentHelp.jsp?np=groupAssetPostingBranch&amp;id=" + groupid + "&pageDirect=Y";
//        }
// 	   	boolean approval_level_val =checkApprovalStatus("27");
//// 	  System.out.println(">>>>>>>>>>>> groupid Into RaiseEntry <<<<<<<<<< "+groupid+"  asset_Code: "+asset_Code+"  assetCode: "+assetCode);
// 	  	boolean status2 = updateCreatedAssetStatus(integrifyId,Description,RegistrationNo,VendorAC,Datepurchased,
// 	  		AssetMake,AssetModel,AssetSerialNo,AssetEngineNo,SupplierName,AssetUser,
// 	  	AssetMaintenance,CostPrice,VatableCost,AuthorizedBy,WhTax,whtaxamount,PostingDate,EffectiveDate,
// 	  	PurchaseReason,SubjectTOVat,AssetStatus,State,Driver,UserID,branchCode,
// 	  	sectionCode,deptCode,categoryCode,barCode,sbuCode,lpo,invoiceNo,supervisor,
// 	  	posted,assetId,asset_Code,assettype,branch_id,dept_id,section_id,
// 	  	category_id,vatamount,residualvalue,whtaxvalue,groupid,systemIp,
// 	  	depreciation_end_date,depreciation_start_date,require_depreciation,require_redistribution,
// 	  	who_to_remind,email_1,who_to_remind_2,email2,spare_1,spare_2,
// 	  	partPAY,fullyPAID,deferPay,warrantyStartDate,expiryDate, noOfMonths,recno,asset_id);
//
// 	  	if ((!approval_level_val)&&(status2))
//     	  	{
//     	  	}
////         	  System.out.println("====== Inserting into status ======"+status+" approval_level_val: "+approval_level_val);
//     	if((status2)&&(approval_level_val))
//	      	{
////   		      	 System.out.println("====== Inserting into Approval ======"+groupid);
//	      	 changeGroupAssetStatus(groupid,"PENDING",assettype);
//	      	 String trans_id = setGroupPendingTrans(setApprovalDataGroup(Long.parseLong(groupid)),"27",Integer.parseInt(assetCode));
//                    setPendingTransArchive(setApprovalDataGroup(Long.parseLong(groupid)),"27",Integer.parseInt(trans_id),Integer.parseInt(assetCode));
//	      	 //write a method to change status to pending
//	      	}
//     	else{
////      		System.out.println("====== Inserting into Approval in else ======"+groupid);
//	      	 changeGroupAssetStatus(groupid,"ACTIVE",assettype);
////	      System.out.println("====== Inserting into Approval in else ======"+groupid);
//     //	 String trans_id = setGroupPendingTrans(setApprovalDataGroup(Long.parseLong(groupid)),"27",Integer.parseInt(assetCode));
//    //    setPendingTransArchive(setApprovalDataGroup(Long.parseLong(groupid)),"27",Integer.parseInt(trans_id),Integer.parseInt(assetCode));
//     	}


    } catch (Exception ex) {
        done = false;
        System.out.println("WARN:Error creating asset in rinsertAssetRecord->" + ex);
    } finally {
    	closeConnection(con, ps, rs);
    }

    return done;
}

public String getNewIdentity(String bra, String dep, String sec, String cat,String delim)
{
	String curr = findObject("select cart_cr from am_ad_cart_identity where cart_id = "+cat+"");
//	System.out.println("====curr :"+curr);
    String query =
           "select acronym,'' AS group_acronym,'' AS region_acronym,'' AS branch_acronym,'' AS dept_acronym,'' AS section_acronym,'' AS category_acronym,0 AS cart_cr,0 AS sequ_cr from am_gb_company  " +
           " UNION "+
           "select '' AS acronym,group_acronym,'' AS region_acronym,'' AS branch_acronym,'' AS dept_acronym,'' AS section_acronym,'' AS category_acronym,0 AS cart_cr,0 AS sequ_cr  from am_ad_group"+
           " UNION "+
           "select '' AS acronym,'' AS group_acronym,region_acronym,'' AS branch_acronym,'' AS dept_acronym,'' AS section_acronym,'' AS category_acronym,0 AS cart_cr,0 AS sequ_cr from am_ad_region where region_id = (select region from am_ad_branch where branch_id = '"+bra+"')"+
           " UNION "+
           "select '' AS acronym,'' AS group_acronym,'' AS region_acronym,branch_acronym,'' AS dept_acronym,'' AS section_acronym,'' AS category_acronym,0 AS cart_cr,0 AS sequ_cr from am_ad_branch where branch_id = '"+bra+"'"+
           " UNION "+
           "select '' AS acronym,'' AS group_acronym,'' AS region_acronym,'' AS branch_acronym,dept_acronym,'' AS section_acronym,'' AS category_acronym,0 AS cart_cr,0 AS sequ_cr from am_ad_department where dept_id = '"+dep+"'"+
           " UNION "+
           "select '' AS acronym,'' AS group_acronym,'' AS region_acronym,'' AS branch_acronym,'' AS dept_acronym,section_acronym,'' AS category_acronym,0 AS cart_cr,0 AS sequ_cr  from am_ad_section where section_id = '"+sec+"'"+
           " UNION "+
           "select '' AS acronym,'' AS group_acronym,'' AS region_acronym,'' AS branch_acronym,'' AS dept_acronym,'' AS section_acronym,category_acronym,0 AS cart_cr,0 AS sequ_cr  from am_ad_category where category_id = '"+cat+"'"+
           " UNION "+
           "select '' AS acronym,'' AS group_acronym,'' AS region_acronym,'' AS branch_acronym,'' AS dept_acronym,'' AS section_acronym,'' AS category_acronym,cart_cr,0 AS sequ_cr  from am_ad_cart_identity where cart_id = '"+cat+"'"+
           " UNION "+
           "select '' AS acronym,'' AS group_acronym,'' AS region_acronym,'' AS branch_acronym,'' AS dept_acronym,'' AS section_acronym,'' AS category_acronym,0 AS cart_cr,sequ_cr  from am_ad_sequ_identity";
// System.out.println("========query in getNewIdentity: "+query);
// System.out.println("====Old curr :"+curr+"   cat: "+cat);
    int currValue = Integer.parseInt(curr) + 1;
//    System.out.println("====New currValue :"+currValue);
String query2 = "update am_ad_cart_identity set cart_cr = " + currValue +
" where cart_id = (select category_id from am_ad_category where category_id = " +
"'" + cat + "')";
String query3 = "update am_ad_sequ_identity set sequ_cr = " + currValue;
//System.out.println("====query2 in getNewIdentity :"+query2);
      Connection con = null;
      ResultSet rs = null;
      Statement stmt = null;
      PreparedStatement ps = null;
   String comp = "";
   String group = "";
   String region = "";
   String branch = "";
   String dept = "";
   String section = "";
   String category = "";
   String catrCr = "";
   String sequCr = "";
   String new_assetId = "";
   int count = 0;
   boolean done = false;
   try {
       con=getConnection();
       stmt = con.createStatement();
       rs = stmt.executeQuery(query);
       while (rs.next()) {
    	   count = count + 1;
 //   	   System.out.println("====Count :"+count);
    	   if(!rs.getString(1).equals("")){comp = rs.getString(1);}
           if(!rs.getString(2).equals("")){group = rs.getString(2);}
           if(!rs.getString(3).equals("")){region = rs.getString(3);}
           if(!rs.getString(4).equals("")){branch = rs.getString(4);}
           if(!rs.getString(5).equals("")){dept = rs.getString(5);}
           if(!rs.getString(6).equals("")){section = rs.getString(6);}
           if(!rs.getString(7).equals("")){category = rs.getString(7);}
           if(!rs.getString(8).equals("0")){catrCr = rs.getString(8);}
           if(!rs.getString(9).equals("0")){sequCr = rs.getString(9);}
       }
//       System.out.println("====comp: "+comp+"  group: "+group+"  region: "+region+"  branch: "+branch+"  dept: "+dept+"  section: "+section+"  category: "+category+"  catrCr: "+catrCr+"  sequCr: "+sequCr);
       new_assetId = comp+delim+branch+delim+category+delim+catrCr;
//       System.out.println("====New Asset Id: "+new_assetId);
       ps = con.prepareStatement(query2);
       done = (ps.executeUpdate() != -1);
//       System.out.println("====New done Id  & query2: "+done+"    query2: "+query2);
       ps = con.prepareStatement(query3);
       done = (ps.executeUpdate() != -1);
 //      System.out.println("====New done Id  & query3: "+done+"    query2: "+query3);
 //      System.out.println("====>>>CURR: "+findObject("select cart_cr from am_ad_cart_identity where cart_id = "+cat+""));
   } catch (Exception ex) {
       ex.printStackTrace();
   } finally {
       closeConnection(con,stmt,rs);
       closeConnection(con, ps);
   }
//   System.out.println("====new_assetId in getNewIdentity: "+new_assetId);
   return new_assetId;

}

public boolean newassetinterfaceProcessing(String integrifyId) {

	Connection con = null;
	PreparedStatement ps = null;
	boolean done = false;
	String query = "";
	query = "UPDATE NEW_ASSET_INTERFACE"
		+ " SET ERROR_MESSAGE = 'Process In Progress',POSTED='P' "
		+ " WHERE INTEGRIFY_ID = ?";
	try {
		con = getConnection();
		ps = con.prepareStatement(query);//}
		ps.setString(1, integrifyId);
		done = (ps.executeUpdate() != -1);
	} catch (Exception e) {
		System.out.println("WARNING:Error executing Query in newassetinterfaceProcessing ->"
				+ e.getMessage());
	} finally {
		closeConnection(con, ps);
	}
//	closeConnection(con, ps);
	return done;

}

public ArrayList getequipmentElementByStatus(String status)
{
    String filter = (new StringBuilder(" AND status='")).append(status).append("'").toString();
    ArrayList _list = getequipmentElementByQuery(filter);
    return _list;
}

public ArrayList getequipmentElementByQuery(String filter)
{
    ArrayList _list;
    String query;
    _list = new ArrayList();
    query = "SELECT id, description, status, user_id, create_date FROM " +
"FM_EQUIPMENT_ELEMENT WHERE id IS NOT NULL ";
    query = (new StringBuilder(String.valueOf(query))).append(filter).toString();
//    System.out.println("=====query in getequipmentElementByQuery: "+query);
    try
    {
        con = getConnection();
        stmt = con.createStatement();
        equipmentElement dispres = new equipmentElement();
        for(rs = stmt.executeQuery(query); rs.next(); _list.add(dispres))
        {
            String id = rs.getString("id");
            String description = rs.getString("description");
            String status = rs.getString("status");
            String userId = rs.getString("user_id");
            String createDate = sdf.format(rs.getDate("create_date"));
            dispres = new equipmentElement();
            dispres.setId(id);
            dispres.setDescription(description);
            dispres.setStatus(status);
            dispres.setUserId(userId);
            dispres.setCreateDate(createDate);
//            System.out.println("=====Id in createequipmentElement: "+id+"  description: "+description+"  status: "+status+"  userId: "+userId+"  createDate: "+createDate);
        }
    }
    catch(Exception ex)
    {
        ex.printStackTrace();
    } finally {
    	closeConnection(con, stmt, rs);
	}
    return _list;
}
/*
public equipmentElement getequipmentElementByCriteriaCode(String criteriaCode)
{
    String filter = (" AND criteria_code='"+criteriaCode+"'");
    equipmentElement dispres = null;
    ArrayList _list = getequipmentElementByQuery(filter);
    if(_list != null && _list.size() > 0)
    {
        dispres = (equipmentElement)_list.get(0);
    }
    return dispres;
}
*/
public equipmentElement getequipmentElementById(String reasonId)
{
    String filter = (" AND Id='"+reasonId+"'");
    equipmentElement dispres = null;
    ArrayList _list = getequipmentElementByQuery(filter);
    if(_list != null && _list.size() > 0)
    {
        dispres = (equipmentElement)_list.get(0);
    }
    return dispres;
}

public boolean createequipmentElement(equipmentElement equip,String Id)
{
    Connection con;
    PreparedStatement ps;
    boolean done;
    String query;
    con = null;
    ps = null;
    done = false;
    String elementId = (new ApplicationHelper()).getGeneratedId("FM_EQUIPMENT_ELEMENT");
    query = "SET IDENTITY_INSERT FM_EQUIPMENT_ELEMENT ON  INSERT INTO FM_EQUIPMENT_ELEMENT(ID, DESCRIPTION, STATUS, USER_ID," +
" CREATE_DATE)  VALUES(?, ?, ?, ?, ?)  SET IDENTITY_INSERT FM_EQUIPMENT_ELEMENT OFF ";

//    System.out.println("=====Id in createequipmentElement: "+Id);
    try
    {
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, elementId);
        ps.setString(2, equip.getDescription());
        ps.setString(3, equip.getStatus());
        ps.setString(4, equip.getUserId());
        ps.setDate(5, dateConvert(equip.getCreateDate()));
        done = ps.executeUpdate() != -1;
//        System.out.println("=====done in createequipmentElement: "+done);
    }
    catch(Exception e)
    {
        System.out.println("WARNING:Error executing Query in createequipmentElement ->"+e.getMessage());
    } finally {
    	closeConnection(con, ps);
	}
    return done;
}

public boolean updateequipmentElement(equipmentElement crite)
{
    Connection con;
    PreparedStatement ps;
    boolean done;
    String query;
    con = null;
    ps = null;
    done = false;
    query = "UPDATE FM_EQUIPMENT_ELEMENT SET DESCRIPTION=?, STATUS=?, USER_ID=? WHERE ID=?";
    try
    {
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, crite.getDescription());
        ps.setString(2, crite.getStatus());
        ps.setString(3, crite.getUserId());
        ps.setString(4, crite.getId());
        done = ps.executeUpdate() != -1;
    }
    catch(Exception e)
    {
        e.printStackTrace();
        System.out.println("WARNING:Error executing Query in updateequipmentElement ->"+e.getMessage());
    } finally {
    	closeConnection(con, ps);
	}

    return done;
}

public java.util.ArrayList getVendorTransactionSqlRecords()
{
	boolean done = false;
	PreparedStatement ps;
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	String updatequery = "UPDATE VENDOR_TRANSACTIONS SET LOCATION = 0 WHERE LOCATION = ?";
		String query = " SELECT PROJECT_CODE, SUM(coalesce(COST_PRICE,0)) AS AMOUNT FROM vendorTransactions GROUP BY PROJECT_CODE ";
//		System.out.println("====query in getVendorTransactionSqlRecords: "+query);
	Connection c = null;
	ResultSet rs = null;
	Statement s = null;

	try {
		    c = getConnection();

	        ps = c.prepareStatement(updatequery);
	        ps.setString(1, "null");
	        done = ps.executeUpdate() != -1;

			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next())
			   {
				String ProjectCode = rs.getString("PROJECT_CODE");
				double amount = rs.getDouble("AMOUNT");
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setProjectCode(ProjectCode);
				newTransaction.setCostPrice(amount);
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

public boolean updateProjectAmtUsed( String projectCode, double amount)
{
	Connection con = null;
	PreparedStatement ps = null;
	boolean done = false;
	String date = String.valueOf(dateConvert(new java.util.Date()));
 	//date = date.substring(0, 10);
	String query = "UPDATE ST_GL_PROJECT SET PROJECT_BALANCE = COST - ?,AMOUNT_UTILIZED= ? where CODE = ? ";
//	System.out.println("query in updateProjectAmtUsed: "+query);
	try {
		con = getConnection();
		ps = con.prepareStatement(query);
		ps.setDouble(1, amount);
		ps.setDouble(2, amount);
		ps.setString(3, projectCode);
		done = (ps.executeUpdate() != -1);
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
    	closeConnection(con, ps);
    }
	return done;
}

public boolean insertFTRecordsFrom_Am_Asset()
{
	Connection con = null;
	PreparedStatement ps = null;
	boolean done = false;
	String date = String.valueOf(dateConvert(new java.util.Date()));
 	//date = date.substring(0, 10);
	String query = "INSERT INTO FT_FLEET_MASTER (ASSET_CODE,ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID, CATEGORY_ID, "
			+ "DATE_PURCHASED, ASSET_MAKE,ASSET_USER,Asset_Maintenance,CREATE_DATE,"
			+ "EFFECTIVE_DATE, LOCATION,RAISE_ENTRY,DEP_YTD,STATUS,USER_ID,BRANCH_CODE,SECTION_CODE,DEPT_CODE,CATEGORY_CODE,DESCRIPTION) "
			+ "SELECT ASSET_CODE,ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID, CATEGORY_ID, DATE_PURCHASED, ASSET_MAKE,ASSET_USER,Asset_Maintenance,Posting_Date, "
			+ "EFFECTIVE_DATE, LOCATION,RAISE_ENTRY,DEP_YTD,ASSET_STATUS,USER_ID,BRANCH_CODE,SECTION_CODE,DEPT_CODE,CATEGORY_CODE,DESCRIPTION "
			+ "FROM FT_AM_ASSET_VIEW WHERE CATEGORY_CODE = '006' OR CATEGORY_CODE = '023' ";
//	System.out.println("query in updateProjectAmtUsed: "+query);
	try {
		con = getConnection();
		ps = con.prepareStatement(query);
		done = (ps.executeUpdate() != -1);
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
    	closeConnection(con, ps);
    }
	return done;
}

public boolean createMonthlyDeprChargesOld(ArrayList list)
{
    Connection con = null;
    PreparedStatement ps = null;
    //ResultSet rs = null;
    com.magbel.legend.vao.newAssetTransaction bd = null;
    int[] d = null;
    String query;

    query = "INSERT INTO MONTHLY_DEPRECIATIONCHARGE (ASSET_ID,Description,BRANCH_CODE,BRANCH_NAME,CATEGORY_CODE,Dept_Code,SBU_CODE,COST_PRICE,Monthly_Dep,YearDeprCharges,Accum_Dep,"+
    		"NBV,TOTAL_NBV,IMPROV_COST,IMPROV_ACCUMDEP,IMPROV_MONTHLYDEP,IMPROV_NBV,Total_Cost_Price,Date_purchased,EFFECTIVE_DATE,DEP_END_DATE,DEP_DATE,"+
    		"CALC_LIFESPAN,TOTAL_LIFE,IMPROV_TOTALLIFE,REMAINING_LIFE,DEP_RATE,CHARGEYEAR,Asset_Status) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    try
    {
		con = getConnection();
		ps = con.prepareStatement(query);
 //   	  System.out.println("===list.size() in createMonthlyDeprCharges: "+list.size());
        for (int i = 0; i < list.size(); i++) {
            bd = (com.magbel.legend.vao.newAssetTransaction) list.get(i);

            String assetId = bd.getAssetId();
            String description = bd.getDescription();
            String branchCode = bd.getBranchCode();
            String categoryCode = bd.getCategoryCode();
//            System.out.println("======>>>>assetId: "+assetId+"   ====>Description: "+description+"  branchCode: "+branchCode+"  categoryCode: "+categoryCode);
            String sbuCode = bd.getSbuCode();
            String branchName = bd.getBranchName();
            double monthlyDepr = bd.getMonthlyDep();
            String deptCode = bd.getDeptCode();
//            System.out.println("======>>>>sbuCode: "+sbuCode+"   ====>branchName: "+branchName+"  monthlyDepr: "+monthlyDepr+"  deptCode: "+deptCode);
            String effectiveDate = bd.getEffectiveDate();
            double costPrice = bd.getCostPrice();
            double totalnbv = bd.getTotalnbv();
            double accumDepr = bd.getAccumDep();
 //           System.out.println("======>>>>effectiveDate: "+effectiveDate+"   ====>costPrice: "+costPrice+"  totalnbv: "+totalnbv+"  accumDepr: "+accumDepr);
            double deprChargeToDate = bd.getDeprChargeToDate();
            double nbv = bd.getNbv();
            double yearDeprCharges = bd.getDeprChargeToDate();
            double improvCostPrice = bd.getImprovcostPrice();
 //           System.out.println("======>>>>deprChargeToDate: "+deprChargeToDate+"   ====>nbv: "+nbv+"  yearDeprCharges: "+yearDeprCharges+"  improvCostPrice: "+improvCostPrice);
            double improvaccumDepr = bd.getImprovaccumDep();
            double improvmonthldepr = bd.getImprovmonthlyDep();
            double totalCostPrice = bd.getTotalCost();
            double improvnbv = bd.getImprovnbv();
 //           System.out.println("======>>>>improvaccumDepr: "+improvaccumDepr+"   ====>improvmonthldepr: "+improvmonthldepr+"  totalCostPrice: "+totalCostPrice+"  improvnbv: "+improvnbv);
            String purchaseDate = bd.getDatepurchased();
            String depr_endDate = bd.getDependDate();
            String depDate = bd.getDepDate();
            double depRate = bd.getDepRate();
 //           System.out.println("======>>>>purchaseDate: "+purchaseDate+"   ====>depr_endDate: "+depr_endDate+"  depDate: "+depDate+"  depRate: "+depRate);
            int calcLifeSpan = bd.getCalcLifeSpan();
            int improvtotalLife = bd.getImprovtotallife();
            int remainLife = bd.getRemainLife();
            int totalLife = bd.getUsefullife();
            String chargeYear = bd.getChargeYear();
            String assetStatus = bd.getAssetStatus();
 //           System.out.println("======>>>>calcLifeSpan: "+calcLifeSpan+"   ====>improvtotalLife: "+improvtotalLife+"  remainLife: "+remainLife+"  totalLife: "+totalLife);
            ps.setString(1, assetId);
            ps.setString(2, description);
            ps.setString(3, branchCode);
//            System.out.println("=====assetId==>>>: "+assetId+"  description: "+description+"  branchCode: "+branchCode);
            ps.setString(4, branchName);
            ps.setString(5, categoryCode);
            ps.setString(6, deptCode);
//            System.out.println("=====branchName: "+branchName+"  categoryCode: "+categoryCode+"  deptCode: "+deptCode);
            ps.setString(7, sbuCode);
            ps.setDouble(8, costPrice);
            ps.setDouble(9, monthlyDepr);
//            System.out.println("=====sbuCode: "+sbuCode+"  costPrice: "+costPrice+"  monthlyDepr: "+monthlyDepr);
            ps.setDouble(10, yearDeprCharges);
            ps.setDouble(11, accumDepr);
            ps.setDouble(12, nbv);
//            System.out.println("=====yearDeprCharges: "+yearDeprCharges+"  accumDepr: "+accumDepr+"  nbv: "+nbv);
            ps.setDouble(13, totalnbv);
            ps.setDouble(14, improvCostPrice);
            ps.setDouble(15, improvaccumDepr);
 //           System.out.println("=====totalnbv: "+totalnbv+"  improvCostPrice: "+improvCostPrice+"  improvaccumDepr: "+improvaccumDepr);
            ps.setDouble(16, improvmonthldepr);
            ps.setDouble(17, improvnbv);
            ps.setDouble(18, totalCostPrice);
 //           System.out.println("=====improvmonthldepr: "+improvmonthldepr+"  improvnbv: "+improvnbv+"  totalCostPrice: "+totalCostPrice);
            ps.setString(19, purchaseDate);
            ps.setString(20, effectiveDate);
            ps.setString(21, depr_endDate);
 //           System.out.println("=====purchaseDate: "+purchaseDate+"  effectiveDate: "+effectiveDate+"  depr_endDate: "+depr_endDate);
            ps.setString(22, depDate);
            ps.setInt(23, calcLifeSpan);
            ps.setInt(24, totalLife);
//            System.out.println("=====depDate: "+depDate+"  calcLifeSpan: "+calcLifeSpan+"  totalLife: "+totalLife);
            ps.setInt(25, improvtotalLife);
            ps.setInt(26, remainLife);
            ps.setDouble(27, depRate);
            ps.setString(28, chargeYear);
            ps.setString(29, assetStatus);
 //           System.out.println("=====improvtotalLife: "+improvtotalLife+"  remainLife: "+remainLife+"  depRate: "+depRate);
            ps.addBatch();
        }
    d = ps.executeBatch();
    //System.out.println("Executed Successfully ");
} catch (Exception ex) {
    System.out.println("Error createMonthlyDeprCharges() of CompanyHandler -> " + ex);
} finally {
	closeConnection(con, ps);
}

return (d.length > 0);
}

public boolean createMonthlyDeprCharges(List<newAssetTransaction> list) {

    String query =
        "INSERT INTO MONTHLY_DEPRECIATIONCHARGE " +
        "(ASSET_ID, Description, BRANCH_CODE, BRANCH_NAME, CATEGORY_CODE, Dept_Code, SBU_CODE, COST_PRICE, Monthly_Dep, YearDeprCharges, Accum_Dep, " +
        "NBV, TOTAL_NBV, IMPROV_COST, IMPROV_ACCUMDEP, IMPROV_MONTHLYDEP, IMPROV_NBV, Total_Cost_Price, Date_purchased, EFFECTIVE_DATE, DEP_END_DATE, DEP_DATE, " +
        "CALC_LIFESPAN, TOTAL_LIFE, IMPROV_TOTALLIFE, REMAINING_LIFE, DEP_RATE, CHARGEYEAR, Asset_Status) " +
        "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    try (Connection con = getConnection();
         PreparedStatement ps = con.prepareStatement(query)) {

        for (newAssetTransaction bd : list) {

            ps.setString(1, bd.getAssetId());
            ps.setString(2, bd.getDescription());
            ps.setString(3, bd.getBranchCode());
            ps.setString(4, bd.getBranchName());
            ps.setString(5, bd.getCategoryCode());
            ps.setString(6, bd.getDeptCode());
            ps.setString(7, bd.getSbuCode());
            ps.setDouble(8, bd.getCostPrice());
            ps.setDouble(9, bd.getMonthlyDep());
            ps.setDouble(10, bd.getDeprChargeToDate());
            ps.setDouble(11, bd.getAccumDep());
            ps.setDouble(12, bd.getNbv());
            ps.setDouble(13, bd.getTotalnbv());
            ps.setDouble(14, bd.getImprovcostPrice());
            ps.setDouble(15, bd.getImprovaccumDep());
            ps.setDouble(16, bd.getImprovmonthlyDep());
            ps.setDouble(17, bd.getImprovnbv());
            ps.setDouble(18, bd.getTotalCost());
            ps.setString(19, bd.getDatepurchased());
            ps.setString(20, bd.getEffectiveDate());
            ps.setString(21, bd.getDependDate());
            ps.setString(22, bd.getDepDate());
            ps.setInt(23, bd.getCalcLifeSpan());
            ps.setInt(24, bd.getUsefullife());
            ps.setInt(25, bd.getImprovtotallife());
            ps.setInt(26, bd.getRemainLife());
            ps.setDouble(27, bd.getDepRate());
            ps.setString(28, bd.getChargeYear());
            ps.setString(29, bd.getAssetStatus());

            ps.addBatch();
        }

        int[] result = ps.executeBatch();
        return result.length > 0;

    } catch (Exception ex) {
        System.out.println("Error createMonthlyDeprCharges() -> " + ex.getMessage());
        return false;
    }
}


//public boolean monthlyDeprChargeComplete(String reportDate) {
//
//	Connection con = null;
//	PreparedStatement ps = null;
//	boolean done = false;
//	String query = "INSERT INTO monthly_Depr_Charge(REPORT_DATE,REPORT_GENERATE) VALUES (?,?)";
//
//	try {
//		con = getConnection();
//		ps = con.prepareStatement(query);
//
//		ps.setString(1, reportDate);
//		ps.setString(2, "Y");
//
//		done = (ps.executeUpdate() != -1);
//
//	} catch (Exception e) {
//		System.out.println("WARNING:Error executing Query for monthly_Depr_Charge ->"
//				+ e.getMessage());
//	} finally {
//		closeConnection(con, ps);
//	}
//	return done;
//
//}

public boolean monthlyDeprChargeComplete(String reportDate) {

    String query = "INSERT INTO monthly_Depr_Charge(REPORT_DATE, REPORT_GENERATE) VALUES (?, ?)";

    try (Connection con = getConnection();
         PreparedStatement ps = con.prepareStatement(query)) {

        ps.setString(1, reportDate);
        ps.setString(2, "Y");

        return ps.executeUpdate() > 0;

    } catch (Exception e) {
        System.out.println("WARNING: Error executing Query for monthly_Depr_Charge -> "
                + e.getMessage());
        return false;
    }
}

public java.util.ArrayList getDepreciationChargesExportRecordsOld(String query)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null;
	ResultSet rs = null;
	Statement s = null;
//	System.out.println("====>getDepreciationChargesExportRecords=====>: "+query);
	try {
		    c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
//			System.out.println("====>rs.next(): "+rs.next());
			while (rs.next())
			   {
				String strDescription = rs.getString("Description");
//				System.out.println("====>strDescription: "+strDescription);
				String StrassetId = rs.getString("ASSET_ID");
				String branchCode = rs.getString("BRANCH_CODE");
//				String branchName = rs.getString("BRANCH_NAME");
//				String category_name = rs.getString("category_name");
				String categoryCode = rs.getString("CATEGORY_CODE");
//				String deptName = rs.getString("Dept_name");
				String sbuCode = rs.getString("SBU_CODE");
				double costPrice = rs.getDouble("COST_PRICE");
				double monthlyDepr = rs.getDouble("Monthly_Dep");
				double depChargeToDate = rs.getDouble("YearDeprCharges");
				double accumDepr = rs.getDouble("Accum_Dep");
				double nbv = rs.getDouble("NBV");
				double totalnbv = rs.getDouble("TOTAL_NBV");
				double improvCostPrice = rs.getDouble("IMPROV_COST");
				double improvAccumDep = rs.getDouble("IMPROV_ACCUMDEP");
				double improvMonthDep = rs.getDouble("IMPROV_MONTHLYDEP");
				double improvNbv = rs.getDouble("IMPROV_NBV");
				double totalCostPrice = rs.getDouble("Total_Cost_Price");
				String purchaseDate = rs.getString("Date_purchased");
                String EffectiveDate = rs.getString("EFFECTIVE_DATE");
                String dep_endDate = rs.getString("DEP_END_DATE");
                String depDate = rs.getString("DEP_DATE");
                int calclifeSpan = rs.getInt("CALC_LIFESPAN");
                int totalLife = rs.getInt("TOTAL_LIFE");
                int improveTotalLife = rs.getInt("IMPROV_TOTALLIFE");
                int remainLife = rs.getInt("REMAINING_LIFE");
                double depRate = rs.getDouble("DEP_RATE");
                String chargeYear = rs.getString("CHARGEYEAR");
                String assetStatus = rs.getString("Asset_Status");
//                System.out.println("====>assetStatus: "+assetStatus);
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setAssetId(StrassetId);
				newTransaction.setBranchCode(branchCode);
				newTransaction.setCategoryCode(categoryCode);
				newTransaction.setSbuCode(sbuCode);
				newTransaction.setDescription(strDescription);
				newTransaction.setMonthlyDep(monthlyDepr);
				newTransaction.setEffectiveDate(EffectiveDate);
				newTransaction.setImprovmonthlyDep(improvMonthDep);
				newTransaction.setCostPrice(costPrice);
//				 System.out.println("====>costPrice: "+costPrice);
//				newTransaction.setPostingDate(depDate);
				newTransaction.setTotalnbv(totalnbv);
				newTransaction.setAccumDep(accumDepr);
				newTransaction.setDeprChargeToDate(depChargeToDate);
				newTransaction.setNbv(nbv);
//				System.out.println("====>nbv: "+nbv+"    depChargeToDate: "+depChargeToDate+"   totalnbv: "+totalnbv);
				newTransaction.setImprovcostPrice(improvCostPrice);
				newTransaction.setImprovaccumDep(improvAccumDep);
				newTransaction.setImprovmonthlyDep(improvMonthDep);
				newTransaction.setImprovnbv(improvNbv);
				newTransaction.setTotalCost(totalCostPrice);
				newTransaction.setDatepurchased(purchaseDate);
//				System.out.println("====>totalCostPrice: "+totalCostPrice);
//				newTransaction.setDeptCode(deptName);
				newTransaction.setDependDate(dep_endDate);
				newTransaction.setDepDate(depDate);
				newTransaction.setDepRate(depRate);
				newTransaction.setCalcLifeSpan(calclifeSpan);
				newTransaction.setUsefullife(totalLife);
//				System.out.println("====>calclifeSpan: "+calclifeSpan+"  totalLife: "+totalLife+"  depRate: "+depRate);
				newTransaction.setImprovtotallife(improveTotalLife);
				newTransaction.setRemainLife(remainLife);
//				System.out.println("====>improveTotalLife: "+improveTotalLife+"  remainLife: "+remainLife+"  chargeYear: "+chargeYear);
				newTransaction.setChargeYear(chargeYear);
//				System.out.println("====>chargeYear: "+chargeYear);
				newTransaction.setAssetStatus(assetStatus);
//				System.out.println("====>chargeYear: "+chargeYear+"  assetStatus: "+assetStatus);
//				System.out.println("====>Loop End=======>: ");
				_list.add(newTransaction);

			   }
//			System.out.println("====>getDepreciationChargesExportRecords Loaded=====>:");
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


public java.util.ArrayList getDepreciationChargesExportRecords(String query) {

	java.util.ArrayList list = new java.util.ArrayList();

    try (Connection c = getConnection();
         PreparedStatement ps = c.prepareStatement(query);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {

        	newAssetTransaction newTransaction = new newAssetTransaction();

            newTransaction.setAssetId(rs.getString("ASSET_ID"));
            newTransaction.setBranchCode(rs.getString("BRANCH_CODE"));
            newTransaction.setCategoryCode(rs.getString("CATEGORY_CODE"));
            newTransaction.setSbuCode(rs.getString("SBU_CODE"));
            newTransaction.setDescription(rs.getString("Description"));
            newTransaction.setMonthlyDep(rs.getDouble("Monthly_Dep"));
            newTransaction.setEffectiveDate(rs.getString("EFFECTIVE_DATE"));
            newTransaction.setCostPrice(rs.getDouble("COST_PRICE"));
            newTransaction.setTotalnbv(rs.getDouble("TOTAL_NBV"));
            newTransaction.setAccumDep(rs.getDouble("Accum_Dep"));
            newTransaction.setDeprChargeToDate(rs.getDouble("YearDeprCharges"));
            newTransaction.setNbv(rs.getDouble("NBV"));
            newTransaction.setImprovcostPrice(rs.getDouble("IMPROV_COST"));
            newTransaction.setImprovaccumDep(rs.getDouble("IMPROV_ACCUMDEP"));
            newTransaction.setImprovmonthlyDep(rs.getDouble("IMPROV_MONTHLYDEP"));
            newTransaction.setImprovnbv(rs.getDouble("IMPROV_NBV"));
            newTransaction.setTotalCost(rs.getDouble("Total_Cost_Price"));
            newTransaction.setDatepurchased(rs.getString("Date_purchased"));
            newTransaction.setDependDate(rs.getString("DEP_END_DATE"));
            newTransaction.setDepDate(rs.getString("DEP_DATE"));
            newTransaction.setDepRate(rs.getDouble("DEP_RATE"));
            newTransaction.setCalcLifeSpan(rs.getInt("CALC_LIFESPAN"));
            newTransaction.setUsefullife(rs.getInt("TOTAL_LIFE"));
            newTransaction.setImprovtotallife(rs.getInt("IMPROV_TOTALLIFE"));
            newTransaction.setRemainLife(rs.getInt("REMAINING_LIFE"));
            newTransaction.setChargeYear(rs.getString("CHARGEYEAR"));
            newTransaction.setAssetStatus(rs.getString("Asset_Status"));

            list.add(newTransaction);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}

public java.util.ArrayList getExpensesSqlRecords()
{
	java.util.ArrayList _list = new java.util.ArrayList();
    String query;
	
    String date = String.valueOf(dateConvert(new Date()));
    date = date.substring(0, 10);
    String finacleTransId = null;
 //   System.out.println("<<<<<<=====About to Execute query in getExpensesSqlRecords=====> ");
    query = " select LT_ID,HIST_ID,BRANCH_ID,ITEM_TYPE,DESCRIPTION,USER_ID,VAT_AMT,WHT_AMT,'2' AS SUPERVISOR,COST_PRICE,INTEGRIFY_ID,"
    		+ "VAT_RATE,WHT_RATE,STATUS,subjectToVat,subjectToWht from PR_EXPENSES_HISTORY WHERE STATUS = 'APPROVED' AND INTEGRIFY_ID IS NOT NULL AND ERROR_MESSAGE IS NULL ";
//    c = null;
//    rs = null;
//    s = null;
    try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()){
//		System.out.println("<<<<<<=====query in getExpensesSqlRecords: "+query);
	  
		while (rs.next())
		   {
            int strid = rs.getInt("LT_ID");
            String histId = rs.getString("HIST_ID");
            String itemType = rs.getString("ITEM_TYPE");
            String userId = rs.getString("USER_ID");
            String superId = rs.getString("SUPERVISOR");
            double costPrice = rs.getDouble("COST_PRICE");
            double vatRate = rs.getDouble("VAT_RATE");
            double whtRate = rs.getDouble("WHT_RATE");
            String strCost = rs.getString("COST_PRICE");
            String integrifyId = rs.getString("INTEGRIFY_ID");
            String status = rs.getString("STATUS");
            String branchId = rs.getString("BRANCH_ID");
            String description = rs.getString("DESCRIPTION");
            String subjectTOVat = rs.getString("subjectToVat");
            String subjectToWhTax = rs.getString("subjectToWht");
            FleetManatainanceRecord record = new FleetManatainanceRecord();
            record.setId(String.valueOf(strid));
            record.setHistId(histId);
            record.setBranchId(branchId);
            record.setType(itemType);
            record.setCost(costPrice);
            record.setVatRate(vatRate);
            record.setWhtRate(whtRate);
            record.setIntegrifyId(integrifyId);
            record.setSuperId(superId);
            record.setUserId(userId);
            record.setDescription(description);
            record.setStatus(status);
            record.setSubjectTOVat(subjectTOVat);
            record.setSubjectToWhTax(subjectToWhTax);
            _list.add(record);
		   }
		}
	 catch (Exception e)
		{
			e.printStackTrace();
		}
		
return _list;

}

public boolean updateExpensesMessageRecord(String integrifyId, String status)
{
    
    boolean done = false;
    String query;
   
    query = "UPDATE PR_EXPENSES_HISTORY SET ERROR_MESSAGE = ? WHERE INTEGRIFY_ID = ?";
//    System.out.println("<<<<===query in updateExpensesMessageRecord: "+query);
 //   System.out.println("<<<<===integrifyId: "+integrifyId+"    status: "+status);
    try(Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);){
        ps.setString(1, status);
        ps.setString(2, integrifyId);
        done = ps.executeUpdate() != -1;
//        System.out.println("<<<<===done: "+done);

	} catch (Exception ex) {
	    System.out.println("WARNING:Error executing Query in updateExpensesMessageRecord ->" + ex);
	} 
    return done;
}

public boolean updateExpensesPostingRecord(String integrifyId, String status,String errorMessage)
{
    Connection con;
    PreparedStatement ps;
    boolean done;
    String query;
    con = null;
    ps = null;
    done = false;
    query = "UPDATE PR_EXPENSES_HISTORY SET STATUS = ?, ERROR_MESSAGE = ? WHERE INTEGRIFY_ID = ?";
    try
    {
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, status);
        ps.setString(2, errorMessage);
        ps.setString(3, integrifyId);
        done = ps.executeUpdate() != -1;

	} catch (Exception ex) {
	    System.out.println("WARNING:Error executing Query in updateExpensesPostingRecord ->" + ex);
	} finally {
	    closeConnection(con, ps);
	}
    return done;
}

public java.util.ArrayList getNewWIPAssetSqlRecords()
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
		String query = " SELECT INTEGRIFY_ID,Description,Registration_No,Vendor_AC,Date_purchased,Asset_Make,Asset_Model," +
				"Asset_Serial_No,Asset_Engine_No,Supplier_Name,Asset_User,Asset_Maintenance,Cost_Price,Authorized_By," +
				"Wh_Tax,Posting_Date,Effective_Date,Purchase_Reason,Subject_TO_Vat,Asset_Status,State,Driver,User_ID," +
				"BRANCH_CODE,SECTION_CODE,DEPT_CODE,CATEGORY_CODE,BAR_CODE,SBU_CODE,LPO,INVOICE_NO,supervisor,POSTED,ASSET_ID," +
				"ASSET_CODE,ERROR_MESSAGE,ASSET_TYPE,Vat_Value,Wh_Tax_Value,QUANTITY,TRAN_TYPE,Location,Spare_1,Spare_2,Multiple,Memo,"+
				"MemoValue,IMPROV_USEFULLIFE, SUB_CATEGORY_CODE, SPARE_3,SPARE_4,SPARE_5,SPARE_6, "+
				"PROJECT_CODE,LOCATION "+
				"FROM NEW_ASSET_INTERFACE WHERE POSTED = 'N' AND ASSET_TYPE = 'C' AND TRAN_TYPE = 'P' AND INTEGRIFY_ID IS NOT NULL ";
//	Transaction transaction = null;
//   System.out.println("====query in getNewWIPAssetSqlRecords-----> "+query);

	try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()){
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
				String strsubcategoryCode = rs.getString("SUB_CATEGORY_CODE");
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
				double StrVatValue = rs.getDouble("Vat_Value");
				int Strnoofitems = rs.getInt("QUANTITY");
				String Strlocation = rs.getString("Location");
				String Strspare1 = rs.getString("Spare_1");
				String Strspare2 = rs.getString("Spare_2");
				String Strspare3 = rs.getString("Spare_3");
				String Strspare4 = rs.getString("Spare_4");
				String Strspare5 = rs.getString("Spare_5");
				String Strspare6 = rs.getString("Spare_6");
				String Strmultiple = rs.getString("Multiple");
				String Strmemo = rs.getString("Memo");
				String Strmemovalue = rs.getString("MemoValue");
				int Strusefullife = rs.getInt("IMPROV_USEFULLIFE");
				String projectCode = rs.getString("PROJECT_CODE");
				String location = rs.getString("LOCATION");
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
				newTransaction.setSubcategoryCode(strsubcategoryCode);
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
				newTransaction.setVatValue(StrVatValue);
				newTransaction.setTranType(Strtrantype);
				newTransaction.setNoofitems(Strnoofitems);
				newTransaction.setLocation(Strlocation);
				newTransaction.setSpare1(Strspare1);
				newTransaction.setSpare2(Strspare2);
				newTransaction.setSpare3(Strspare3);
				newTransaction.setSpare4(Strspare4);
				newTransaction.setSpare5(Strspare5);
				newTransaction.setSpare6(Strspare6);
				newTransaction.setMemo(Strmemo);
				newTransaction.setMemovalue(Strmemovalue);
				newTransaction.setMultiple(Strmultiple);
				newTransaction.setUsefullife(Strusefullife);
				newTransaction.setProjectCode(projectCode);
				newTransaction.setLocation(location);
				_list.add(newTransaction);
			   }
	 }
				 catch (Exception e)
					{
						e.printStackTrace();
					}
					
	return _list;
}


public java.util.ArrayList getStockSqlRecords()
{
	java.util.ArrayList _list = new java.util.ArrayList();
    String query;
    String date = String.valueOf(dateConvert(new Date()));
    date = date.substring(0, 10);
    String finacleTransId = null;
    query = " select LT_ID,HIST_ID,BRANCH_ID,TYPE,DETAILS,USER_ID,VAT_AMT,WHT_AMT,'2' AS SUPERVISOR,COST_PRICE,INTEGRIFY_ID,STATUS from ST_CREATION_HISTORY "+
" WHERE STATUS ='APPROVED' AND INTEGRIFY_ID IS NOT NULL AND ERROR_MESSAGE IS NULL ";
    
    try(Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()){
//    	System.out.println("<<<<<<=====query in getFleetSqlRecords: "+query);
		while (rs.next())
		   {
            int strid = rs.getInt("LT_ID");
            String histId = rs.getString("HIST_ID");
            String itemType = rs.getString("TYPE");
            String userId = rs.getString("USER_ID");
            String superId = rs.getString("SUPERVISOR");
            double costPrice = rs.getDouble("COST_PRICE");
            double vatAmt = rs.getDouble("VAT_AMT");
            double whtAmt = rs.getDouble("WHT_AMT");
            String strCost = rs.getString("COST_PRICE");
            String integrifyId = rs.getString("INTEGRIFY_ID");
            String status = rs.getString("STATUS");
            String branchId = rs.getString("BRANCH_ID");
            String description = rs.getString("DETAILS");
            FleetManatainanceRecord record = new FleetManatainanceRecord();
            record.setId(String.valueOf(strid));
            record.setHistId(histId);
            record.setBranchId(branchId);
            record.setType(itemType);
            record.setCost(costPrice);
            record.setVatAmt(vatAmt);
            record.setWhtAmt(whtAmt);
            record.setIntegrifyId(integrifyId);
            record.setSuperId(superId);
            record.setUserId(userId);
            record.setDescription(description);
            record.setStatus(status);
            _list.add(record);
		   }
		}
	 catch (Exception e)
		{
			e.printStackTrace();
		}
		
//    closeConnection(c, s, rs);
return _list;

}


public boolean updateStockPostingRecord(String integrifyId, String status)
{
   
    boolean done = false;
    String query; 
    query = "UPDATE ST_CREATION_HISTORY SET STATUS = ? WHERE INTEGRIFY_ID = ?";
    try(Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);){

        ps.setString(1, status);
        ps.setString(2, integrifyId);
        done = ps.executeUpdate() != -1;

	} catch (Exception ex) {
	    System.out.println("WARNING:Error executing Query in updateStockPostingRecord ->" + ex);
	} finally {
	    closeConnection(con, ps);
	}
//    closeConnection(con, ps);
    return done;
}

public boolean updateStockErrorMessageRecord(String integrifyId, String status)
{
    boolean done;
    String query;
    done = false;
    query = "UPDATE ST_CREATION_HISTORY SET ERROR_MESSAGE = ? WHERE INTEGRIFY_ID = ?";
//    System.out.println("<<<<===integrifyId: "+integrifyId+"    status: "+status);
    try(Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);) {
        ps.setString(1, status);
        ps.setString(2, integrifyId);
        done = ps.executeUpdate() != -1;
//        System.out.println("<<<<===done: "+done);

	} catch (Exception ex) {
	    System.out.println("WARNING:Error executing Query in updateStockErrorMessageRecord ->" + ex);
	} finally {
	    closeConnection(con, ps);
	}
//    closeConnection(con, ps);
    return done;
}


public boolean newassetinterface(String integrifyId,String status) {

	Connection con = null;
	PreparedStatement ps = null;
	boolean done = false;
	String query = "";
	System.out.println("=== status: "+status+"    First Update with Status");
	query = "UPDATE NEW_ASSET_INTERFACE"
			+ " SET POSTED='"+status+"'"
			+ " WHERE INTEGRIFY_ID = '"+integrifyId+"'";
	System.out.println("query with Pending Status: "+query);

	try {
		con = getConnection();
	//	if(status.equalsIgnoreCase("Y")){
		ps = con.prepareStatement(query);//}
		//else{ps = con.prepareStatement(query2);}
		done = (ps.executeUpdate() != -1);
//		closeConnection(con, ps);
	} catch (Exception e) {
		System.out.println("WARNING:Error executing Query ->"
				+ e.getMessage());
	} finally {
		closeConnection(con, ps);
	}
//	closeConnection(con, ps);
	return done;

}

public java.util.ArrayList getMobilesByStatus(String status) {
	String filter = " AND Status='" + status + "'";
	java.util.ArrayList _list = getMobilesByQuery(filter);
	return _list;
}

public java.util.ArrayList getMobilesByQuery(String filter) {
	java.util.ArrayList _list = new java.util.ArrayList();
	legend.admin.objects.Mobiles mobile = null;

	String query = "select distinct a.Id,b.User_id,a.USER_NAME,b.Full_Name,a.MAC_ADDRESS,a.Status,a.create_date, a.Staff_Enabled "
			+ " from AM_GB_REGMAC a, am_gb_user b where a.USER_NAME = b.User_Name ";

	query += filter+" ORDER BY USER_NAME";
//	System.out.println("====getMobilesByStatus query: "+query);
	try(Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
		

		while (rs.next()) {
			String mobileId = rs.getString("Id");
			String userName = rs.getString("USER_NAME");
			String fullName = rs.getString("Full_Name");
			String macAddress = rs.getString("MAC_ADDRESS");
			String mobileStatus = rs.getString("Status");
			String createDate = sdf.format(rs.getDate("create_date"));
			String staffEnabled = rs.getString("Staff_Enabled");

			mobile = new legend.admin.objects.Mobiles();
			mobile.setMobileUserId(mobileId);
			mobile.setUserName(userName);
			mobile.setFullName(fullName);
			mobile.setMobileStatus(mobileStatus);
			mobile.setCreateDate(createDate);
			mobile.setStaffEnabled(staffEnabled);
			mobile.setMacAddress(macAddress);


			_list.add(mobile);
		}
	} catch (Exception ex) {
		ex.printStackTrace();
	} finally {
		closeConnection(con, stmt, rs);
	}
	return _list;

}


public legend.admin.objects.Mobiles getMobilesByMobileId(
		String mobileId) {
	String filter = " AND a.Id='" + mobileId + "'";
	legend.admin.objects.Mobiles mobile = null;
	java.util.ArrayList _list = getMobilesByQuery(filter);

	mobile = (legend.admin.objects.Mobiles) _list.get(0);
	return mobile;
}

public legend.admin.objects.Mobiles getMobilesByUserName(
		String userName) {
	String filter = " AND a.User_Name='" + userName + "'";
	legend.admin.objects.Mobiles mobile = null;
	java.util.ArrayList _list = getMobilesByQuery(filter);
	if (_list != null && _list.size() > 0) {
		mobile = (legend.admin.objects.Mobiles) _list.get(0);
	}

	return mobile;
}

public boolean createMobiles(
		legend.admin.objects.Mobiles mobile) {
	
	boolean done = false;
	String query = "INSERT INTO AM_GB_REGMAC(USER_NAME, "
			+ "MAC_ADDRESS, STATUS, user_id, create_date, Staff_Enabled) "
			+ " VALUES(?, ?, ?, ?, ?, ?)";

	try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);){
	
		//ps.setLong(1, System.currentTimeMillis());
		ps.setString(1, mobile.getUserName());
		ps.setString(2, mobile.getMacAddress());
		ps.setString(3, mobile.getMobileStatus());
		ps.setString(4, mobile.getUserId());
		ps.setDate(5, dateConvert(mobile.getCreateDate()));
		ps.setString(6, mobile.getStaffEnabled());

		done = (ps.executeUpdate() != -1);

	} catch (Exception e) {
		System.out.println("WARNING:Error executing Query in createMobiles ->"
				+ e.getMessage());
	} 
	return done;
}

public boolean updateMobiles(
		legend.admin.objects.Mobiles mobile) {
	
	boolean done = false;
	String query = "UPDATE AM_GB_REGMAC SET MAC_ADDRESS=?, "
			+ "STATUS=?, "
			+ "Staff_Enabled=?"
			+ " WHERE USER_NAME=?";

	try(Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);) {
		
		ps.setString(1, mobile.getMacAddress());
		ps.setString(2, mobile.getMobileStatus());
		ps.setString(3, mobile.getStaffEnabled());
		ps.setString(4, mobile.getUserName());
		//ps.setDate(5, df.dateConvert(dispose.getCreateDate()));
		//ps.setString(5, mobile.getMobileId());

		done = (ps.executeUpdate() != -1);

	} catch (Exception e) {
		e.printStackTrace();
		System.out.println("WARNING:Error executing Query ->"
				+ e.getMessage());
	} 
	return done;
}

public boolean isUserExisting(String userName)
{
    boolean done = false;
    String FINDER_QUERY;
    
    FINDER_QUERY = "select User_Id from am_gb_user where User_Name = ?";
    try(Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(FINDER_QUERY);
             ResultSet rs = ps.executeQuery()){
		
        ps.setString(1, userName);

        while (rs.next()) {
            done = true;
        }

    } catch (Exception ex) {
        System.out.println("WARNING: cannot fetch [User from am_gb_user in isUserExisting ]->"
                + ex.getMessage());
    } finally {
        closeConnection(con, ps, rs);
    }

    return done;
}

public boolean fmppmserviceDue(String reqnId, String transId, String userId, String branchCode,
		String categoryCode, String subcatCode, String description, String compCode) {
    boolean done = true;

    System.out.println("fmppmserviceDue Description: "+description);
    String query = "INSERT INTO FM_PPM_AWAIT_REQUISITION(ReqnID,TRANSID,UserID,ReqnBranch,CATEGORY_CODE,SUB_CATEGORY_CODE,DESCRIPTION,"
            + "ReqnDate,ItemType,Status,Company_Code)"
            + " VALUES(?,?,?,?,?,?,?,?,?,?,?)";
    try(Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query);) {
      
        ps.setString(1, reqnId);
        ps.setString(2, transId);
        ps.setString(3, userId);
        ps.setString(4, branchCode);
        ps.setString(5, categoryCode);
        ps.setString(6, subcatCode);
        ps.setString(7, description);
        ps.setString(8, String.valueOf(df.dateConvert(new java.util.Date())));
//        System.out.println("fmppmserviceDue reqnId: "+reqnId+"  transId: "+transId+"  branchCode:"+branchCode);
        ps.setString(9, "X");
        ps.setString(10, "X");
        ps.setString(11, compCode);
        ps.execute();

    } catch (Exception ex) {
        done = false;
        System.out.println(
                "WARNING:cannot insert fm_ppm_Await_requisition->"
                + ex.getMessage());
    } 
    return done;
}

public String generateCode2(String moduleName, String branch_id,String department_id, String section_id)
{
	System.out.println("<<<<===generateCode2======");
StringBuffer generatedCode = new StringBuffer();
String prex="";
int start =0;
String result = "";
System.out.println("<<<<===generateCode3======");
//Codes code = this.findCodes("");
System.out.println("<<<<===generateCode4======");
ModuleCodes mcode = this.findModuleByName(moduleName);
System.out.println("<<<<===generateCode5======");
String temp = mcode.getModuleAbv();
start = mcode.getStartNo();
prex = mcode.getIs_prefix();
String moduleAbv = mcode.getModuleAbv();
System.out.println("<<<<===prex in generateCode: "+prex+"   temp: "+temp+"   start: "+start+"  moduleAbv: "+moduleAbv);
//if(prex.equalsIgnoreCase("Y"))
//{
//	temp+=code.getDelimiter()==null?"":code.getDelimiter();
//	generatedCode.insert(0, temp);
//}
//else
//{
//	generatedCode.append(temp);
//
//}
generatedCode.append(start);

notifyModule(moduleName);
System.out.println("<<<<===generateCode2: "+generatedCode.toString());
result = mcode.getModuleAbv() + Integer.toString(mcode.getStartNo());
//return generatedCode.toString();
return result;
}


public ModuleCodes findModuleByName(String moduleName) {
	String filter = "WHERE MODULE_NAME='" + moduleName + "'";
	ArrayList<ModuleCodes> _list = findModules(filter);
	ModuleCodes mcodes = _list.iterator().next();
	return mcodes;
}


public ArrayList<ModuleCodes> findModules(String filter) {

	String FIND_QUERY = "SELECT MODULE_NAME, MODULE_ABV, IS_PREFIX, START_NO FROM ST_MODULES_CODES  ";

	Connection con = null;
	ResultSet rs = null;
	PreparedStatement ps = null;

	ModuleCodes module = null;
	ArrayList<ModuleCodes> finder = new ArrayList<ModuleCodes>();

	FIND_QUERY += filter;

	try {

		con = getConnection();
		ps = con.prepareStatement(FIND_QUERY);
		rs = ps.executeQuery();
		while (rs.next()) {
			String moduleName = rs.getString("MODULE_NAME");
			String moduleAbv = rs.getString("MODULE_ABV");
			String is_prefix = rs.getString("IS_PREFIX");
			int startNo = rs.getInt("START_NO");

			module = new ModuleCodes(moduleName, moduleAbv, is_prefix,
					startNo);

			finder.add(module);

		}

	} catch (Exception ex) {
		ex.printStackTrace();
		System.out.println("WARN:Error Saving code setup " + ex);
	} finally {
		closeConnection(con, ps, rs);
	}
	return finder;
}


public Codes findCodes(String filter) {

	String FIND_QUERY = "SELECT PRIORITY1, PRIORITY2, PRIORITY3, "
			+ "PRIORITY4, PRIORITY5, PRIORITY6, "
			+ "DELIMITER FROM ST_CODEGEN_SETUP  ";

	Connection con = null;
	ResultSet rs = null;
	PreparedStatement ps = null;

	Codes code = null;
	// ArrayList<Codes> finder = new ArrayList<Codes>();
//	System.out.println("<<<<===findCodes======");
	FIND_QUERY += filter;

	try {

		con = getConnection();
		ps = con.prepareStatement(FIND_QUERY);
		rs = ps.executeQuery();
		while (rs.next()) {

			String priority1 = rs.getString("PRIORITY1");
			String priority2 = rs.getString("PRIORITY2");
			String priority3 = rs.getString("PRIORITY3");
			String priority4 = rs.getString("PRIORITY4");
			String priority5 = rs.getString("PRIORITY5");
			String priority6 = rs.getString("PRIORITY6");
			String delimiter = rs.getString("DELIMITER");

			code = new Codes(priority1, priority2, priority3, priority4,
					priority5, priority6, delimiter);

			// finder.add(code);

		}

	} catch (Exception ex) {
		ex.printStackTrace();
		System.out.println("WARN:Error Saving code setup " + ex);
	} finally {
		closeConnection(con, ps, rs);
	}
	System.out.println("<<<<===code in findCodes======"+code);
	return code;

}


public boolean notifyModule(String moduleName) {

	String UPDATE_QUERY = "UPDATE ST_MODULES_CODES SET "
			+ "START_NO=START_NO+? WHERE MODULE_NAME=?";

	Connection con = null;
	ResultSet rs = null;
	PreparedStatement ps = null;
	boolean done = false;

	try {

		con = getConnection();
		ps = con.prepareStatement(UPDATE_QUERY);
		ps.setInt(1, 1);
		ps.setString(2, moduleName);
		done = (ps.executeUpdate() == -1);

	} catch (Exception ex) {
		ex.printStackTrace();
		System.out.println("WARN:Error Saving code setup " + ex);
	} finally {
		closeConnection(con, ps, rs);
	}
	return done;
}

public java.util.ArrayList getSLASqlRecords()
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
		String query = "select distinct a.sla_ID,a.user_id,c.Full_Name,c.email,a.sla_name,a.sla_description,a.SLA_Start_Date,a.SLA_End_Date, DATEDIFF(day, (SELECT GETDATE()), a.SLA_End_Date) AS DATE_DIFFERENCE, ((DATEDIFF(day, (SELECT GETDATE()), a.SLA_End_Date)) - b.RESOLVE_DAY) AS REMAIN_DAYS,b.alertfreq,b.alertstart, ALERTSTARTDATE,s.RESOLVE_DAY,s.RESOLVE_HOUR,s.RESOLVE_MINUTE,s.name,STATUS from AM_GB_SLA a, SLA_RESPONSE b, am_gb_User c, SLA_ESCALATE s "+
	" WHERE a.sla_ID = b.criteria_ID and a.user_id = c.User_id and a.sla_ID = s.criteria_ID and a.STATUS IS NULL and b.ALERTSTARTDATE IS NOT NULL ";


//	System.out.println("query in getSLASqlRecords: "+query);
	try(Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
		   
			while (rs.next())
			   {
				String strid = rs.getString("sla_ID");
				String slaDescription = rs.getString("sla_description");
				String slaName = rs.getString("sla_Name");
				int dateDiff = rs.getInt("DATE_DIFFERENCE");
				int strremainDay = rs.getInt("REMAIN_DAYS");
				int stralertfreq = rs.getInt("alertfreq");
				int stralertstart = rs.getInt("alertstart");
				String alertStartDate = rs.getString("ALERTSTARTDATE");
				String strstatus = rs.getString("STATUS");
				String strstartDate = rs.getString("SLA_Start_Date");
				String strendDate = rs.getString("SLA_End_Date");
				String email = rs.getString("email");
				String userId = rs.getString("user_id");
				String userName = rs.getString("Full_Name");
				String resolveDay = rs.getString("RESOLVE_DAY");
				String resolveHour = rs.getString("RESOLVE_HOUR");
				String resolveMinutes = rs.getString("RESOLVE_MINUTE");
				String escalateName = rs.getString("NAME");
				Sla sla = new Sla();
				sla.setSla_ID(strid);
				sla.setSla_description(slaDescription);
				sla.setSla_name(slaName);
				sla.setDiffDate(dateDiff);
				sla.setRemainDay(strremainDay);
				sla.setAlertFreq(stralertfreq);
				sla.setAlertStart(stralertstart);
				sla.setSlaAlertStartDate(alertStartDate);
				sla.setSlaStartDate(strstartDate);
				sla.setSlaEndDate(strendDate);
				sla.setStatus(strstatus);
				sla.setSlaEmail(email);
				sla.setUserId(userId);
				sla.setSlaFullName(userName);
				sla.setRESOLVE_DAY(resolveDay);
				sla.setRESOLVE_HOUR(resolveHour);
				sla.setRESOLVE_MINUTE(resolveMinutes);
				sla.setSlaEscalateName(escalateName);
				_list.add(sla);
			   }
	 }
				 catch (Exception e)
					{
						e.printStackTrace();
					}
					
	return _list;
}

public boolean updateSlaRecords( String id)
{
	
	boolean done = false;
	String date = String.valueOf(dateConvert(new java.util.Date()));
 	//date = date.substring(0, 10);
	String query = "UPDATE am_gb_sla SET STATUS= 'P' WHERE SLA_ID = '"+id+"' AND STATUS IS NULL ";
//	System.out.println("query in updateSendMailRecords: "+query);
	try(Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);) {
		
		done = (ps.executeUpdate() != -1);
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
    	closeConnection(con, ps);
    }
	return done;
}


public void insertMailRecords(String createdby,String subject, String msgText1){
String query_r ="INSERT INTO MAILS_TO_SEND (MAIL_ADDRESS,MAIL_HEADER,MAIL_BODY) VALUES(?,?,?) ";
		

try(Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query_r);
             ResultSet rs = ps.executeQuery()) {
//    con = dbConnection.getConnection("legendPlus");

 //   System.out.println("insert Mail records beans================");

            ps.setString(1,createdby);
            ps.setString(2,subject);
            ps.setString(3,msgText1);
            ps.execute();
//           dbConnection.closeConnection(con, ps);
        } catch (Exception ex) {

            System.out.println("CompanyHand: InsertMails()>>>>>" + ex);
        } 

}//updateAssetStatus()

public boolean updateSlaJobRecords( String id,String nextalertDate)
{
	Connection con = null;
	PreparedStatement ps = null;
	PreparedStatement ps1 = null;
	boolean done = false;
	String date = String.valueOf(dateConvert(new java.util.Date()));
 	//date = date.substring(0, 10);
	String query = "UPDATE SLA_RESPONSE SET ALERTSTARTDATE= ? WHERE CRITERIA_ID = ? ";
	String query2 = "UPDATE AM_GB_SLA SET STATUS= 'P' WHERE SLA_ID = ? AND STATUS IS NULL ";
//	System.out.println("query in updateSendMailRecords: "+query);
	try {
		con = getConnection();
		ps = con.prepareStatement(query);
		ps.setString(1, nextalertDate);
		ps.setString(2, id);
		done = (ps.executeUpdate() != -1);
		ps1 = con.prepareStatement(query2);
		ps1.setString(1, id);
		done = (ps1.executeUpdate() != -1);
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
    	closeConnection(con, ps);
    	closeConnection(con, ps1);
    }
	return done;
}


public boolean raisentry_postDuplicate() throws SQLException {
    Connection con = null;
    //PreparedStatement ps = null;
     Statement stmt = null;
    ResultSet rs = null;
    String process = "";
    CallableStatement cstmt = null;
    boolean gotResults = false;
    try {
    	 con = getConnection();
         stmt = con.createStatement();
     cstmt = con.prepareCall("{CALL raisentry_postDuplicate()}");
//     cstmt.setString(1, userName);
//     rs = cstmt.executeQuery();
     gotResults = cstmt.execute();
//     if (rs.next()) {
//   // 	 toDate = rs.getString(1);
//    	 process = "Y";
//     }
     process = "Y";
    } catch (Exception ex) {

        System.out.println("Error occurred in raisentry_postDuplicate() of CompanyHandlers  >>" +ex);
    }  finally {
    	closeConnection(con, stmt);

    }

    return gotResults;
}


public boolean asset_approvalDuplicate() throws SQLException {
    Connection con = null;
    //PreparedStatement ps = null;
     Statement stmt = null;
    ResultSet rs = null;
    String process = "";
    CallableStatement cstmt = null;
    boolean gotResults = false;
    try {
    	 con = getConnection();
         stmt = con.createStatement();
     cstmt = con.prepareCall("{CALL asset_approvalDuplicate()}");
//     cstmt.setString(1, userName);
//     rs = cstmt.executeQuery();
      gotResults = cstmt.execute();
//     if (rs.next()) {
//   // 	 toDate = rs.getString(1);
//    	 process = "Y";
//     }
     process = "Y";
    } catch (Exception ex) {

        System.out.println("Error occurred in asset_approvalDuplicate() of CompanyHandlers  >>" +ex);
    }  finally {
    	closeConnection(con, stmt);

    }

    return gotResults;
}

public void insertSLAProcessingTransaction(String criteriaId,int alertFreq, int alertStart,int remainExecDays,String alertStartDate){
String query_r ="INSERT INTO SLA_PROCESSING (CRITERIA_ID,ALERTFREQ,ALERTSTART,REMAINEXECDAYS,ALERTSTARTDATE,CREATE_DATE) "
		+ "VALUES(?,?,?,?,?,?) ";
		Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

try {
//    con = dbConnection.getConnection("legendPlus");
    con = getConnection();

ps = con.prepareStatement(query_r);

//    System.out.println("insert Mail records beans================");

            ps.setString(1,criteriaId);
            ps.setInt(2,alertFreq);
            ps.setInt(3,alertStart);
            ps.setInt(4,remainExecDays);
            ps.setString(5,alertStartDate);
            ps.setString(6, String.valueOf(df.dateConvert(new java.util.Date())));
            ps.execute();
//           dbConnection.closeConnection(con, ps);
        } catch (Exception ex) {

            System.out.println("CompanyHand: insertSLAProcessingTransaction()>>>>>" + ex);
        } finally {
        	closeConnection(con, ps);
//            dbConnection.closeConnection(con, ps);
        }


}//updateAssetStatus()

public java.util.ArrayList getBranchVisitSqlRecords()
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
		String query = "SELECT *FROM FM_BRANCH_VISIT a, FM_BRANCH_VISIT_DETAILS b "+
	" WHERE a.GROUP_ID = b.GROUP_ID AND STATUS = 'DONE' ";

	Connection c = null;
	ResultSet rs = null;
	Statement s = null;
//	System.out.println("query in getSLASqlRecords: "+query);
	try {
		    c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next())
			   {
				String strid = rs.getString("ID");
				String branchCode = rs.getString("BRANCH_CODE");
				String inspectedBy = rs.getString("INSPECTED_BY");
				String dateInspect = rs.getString("INSPECT_DATE");
				String dueDate = rs.getString("DUEDATE");
				String groupId = rs.getString("GROUP_ID");
				String visitSummary = rs.getString("VISIT_SUMMARY");
				String strstatus = rs.getString("STATUS");
				String transDate = rs.getString("TRANS_DATE");
				String sNo = rs.getString("SNO");
				String element = rs.getString("ELEMENT");
				String condition = rs.getString("CONDITION");
				String remark = rs.getString("REMARK");
				String actionby = rs.getString("ACTION");

				BranchVisit brVisit = new BranchVisit();
				brVisit.setId(strid);
				brVisit.setBranchCode(branchCode);
				brVisit.setActionby(actionby);
				brVisit.setCondition(condition);
				brVisit.setDateInspect(dateInspect);
				brVisit.setDueDate(dueDate);
				brVisit.setElement(element);
				brVisit.setInspectedBy(inspectedBy);
				brVisit.setRemark(remark);
				brVisit.setSNo(sNo);
				brVisit.setVisitsummary(visitSummary);
				brVisit.setTransDate(transDate);
				brVisit.setStatus(strstatus);
				_list.add(brVisit);
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

public boolean updateBranchVisitJobRecords( String id,String nextalertDate)
{
	Connection con = null;
	PreparedStatement ps = null;
	boolean done = false;
	String date = String.valueOf(dateConvert(new java.util.Date()));
 	//date = date.substring(0, 10);
	String query = "UPDATE FM_BRANCH_VISIT SET STATUS= ? WHERE ID = ? ";
	try {
		con = getConnection();
		ps = con.prepareStatement(query);
		ps.setString(1, nextalertDate);
		ps.setString(2, id);
		done = (ps.executeUpdate() != -1);
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
    	closeConnection(con, ps);
    }
	return done;
}

public void insertStockTotalRecords(){
String query_r ="INSERT INTO ST_INVENTORY_TOTALS select ITEM_CODE, sum(QUANTITY) AS BALANCE,WAREHOUSE_CODE,USER_ID AS USERID,NULL AS BALANCE_LTD,NULL AS BALANCE_YTD,sum(QUANTITY) AS TMP_BALANCE,1 AS COMP_CODE,ASSET_STATUS AS STATUS,BRANCH_ID from st_stock "+
"GROUP BY WAREHOUSE_CODE,ITEM_CODE,BRANCH_ID,USER_ID,ASSET_STATUS";
		Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
String queryA = "update ST_DISTRIBUTION_ITEM set WAREHOUSE_CODE = '1' where WAREHOUSE_CODE = '' OR WAREHOUSE_CODE IS NULL ";
try {
//    con = dbConnection.getConnection("legendPlus");
    con = getConnection();

ps = con.prepareStatement(query_r);
            ps.execute();

ps1 = con.prepareStatement(queryA);
            ps1.execute();
//           dbConnection.closeConnection(con, ps);
        } catch (Exception ex) {

            System.out.println("CompanyHand: insertStockTotalRecords()>>>>>" + ex);
        } finally {
        	closeConnection(con, ps);
            closeConnection(con, ps1);
        }


}//updateAssetStatus()

public opexAcctType getOpexAcctTypeByID(String TypeID) {
	legend.admin.objects.opexAcctType type = null;
	String query = "SELECT MTID, CODE, CLASS"
			+ ", DESCRIPTION, USER_ID,CR_ACCOUNT,GL_ACCOUNT, CREATE_DATE,STATUS "
			+ " FROM AM_GB_OPEX WHERE MTID = ?";

	Connection c = null;
	ResultSet rs = null;
//	Statement s = null;
	PreparedStatement ps = null;

	try {
		c = getConnection();
//		s = c.createStatement();
//		rs = s.executeQuery(query);
		ps = c.prepareStatement(query.toString());
		ps.setString(1, TypeID);
		rs = ps.executeQuery();

		while (rs.next()) {
			String Id = rs.getString("MTID");
			String code = rs.getString("CODE");
			String classType = rs.getString("CLASS");
			String description = rs.getString("DESCRIPTION");
			String userId = rs.getString("USER_ID");
			String creditAcct = rs.getString("CR_ACCOUNT");
			String debitAcct = rs.getString("GL_ACCOUNT");
			String createDate = rs.getString("CREATE_DATE");
			String status = rs.getString("Status");
			type = new legend.admin.objects.opexAcctType();
			type.setTypeId(Id);
			type.setTypeCode(code);
			type.setTypeClass(classType);
			type.setDescription(description);
			type.setUserId(userId);
			type.setCreateDate(createDate);
			type.setDrAccount(debitAcct);
			type.setCrAccount(creditAcct);
			type.setStatus(status);
		}

	} catch (Exception e) {
		e.printStackTrace();
	}

	finally {
		closeConnection(c, ps, rs);
	}
	return type;
}

public java.util.ArrayList getOpexAcctTypeByQuery(String filter) {
	java.util.ArrayList _list = new java.util.ArrayList();
	opexAcctType type = null;

	String query = "SELECT MTID, CODE,CLASS, DESCRIPTION"
			+ ", GL_ACCOUNT,CR_ACCOUNT, USER_ID, CREATE_DATE,STATUS"
			+ " FROM AM_GB_OPEX WHERE MTID IS NOT NULL ";

	Connection c = null;
	ResultSet rs = null;
	Statement s = null;
	query = query + filter;
	try {
		c = getConnection();
		s = c.createStatement();
		rs = s.executeQuery(query);
		while (rs.next()) {
			String typeId = rs.getString("MTID");
			String typeCode = rs.getString("CODE");
			String typeClass = rs.getString("CLASS");
			String description = rs.getString("DESCRIPTION");
			String drAccount = rs.getString("GL_ACCOUNT");
			String crAccount = rs.getString("CR_ACCOUNT");
			String userId = rs.getString("USER_ID");
			String createDate = rs.getString("CREATE_DATE");
			String status = rs.getString("STATUS");

			type = new legend.admin.objects.opexAcctType();
			type.setTypeId(typeId);
			type.setTypeCode(typeCode);
			type.setTypeClass(typeClass);
			type.setDescription(description);
			type.setUserId(userId);
			type.setCreateDate(createDate);
			type.setDrAccount(drAccount);
			type.setCrAccount(crAccount);
			_list.add(type);
		}

	} catch (Exception e) {
		e.printStackTrace();
	}

	finally {
		closeConnection(c, s, rs);
	}
	return _list;

}

public boolean updateOpexAcctType(legend.admin.objects.opexAcctType type) {

	Connection con = null;
	PreparedStatement ps = null;
	boolean done = false;
	String query = "UPDATE AM_GB_OPEX" + " SET CLASS = ?"
			+ ",DESCRIPTION = ?,GL_ACCOUNT = ?,CR_ACCOUNT = ?" + " WHERE MTID = ?";
//	System.out.println("=====updateOpexAcctType: "+query);
	try {
		con = getConnection();
		ps = con.prepareStatement(query);
		ps.setString(1, type.getTypeClass());
		ps.setString(2, type.getDescription());
		ps.setString(3, type.getDrAccount());
		ps.setString(4, type.getCrAccount());
		ps.setString(5, type.getTypeId());
		done = (ps.executeUpdate() != -1);

	} catch (Exception e) {
		System.out.println("WARNING:Error executing Query in updateOpexAcctType ->"
				+ e.getMessage());
	} finally {
		closeConnection(con, ps);
	}
	return done;
}

public boolean createOpexAcctType(legend.admin.objects.opexAcctType type) {

	Connection con = null;
	PreparedStatement ps = null;
	boolean done = false;
	String query = "INSERT INTO AM_GB_OPEX(CODE, DESCRIPTION"
			+ ", GL_ACCOUNT, CR_ACCOUNT, USER_ID, CREATE_DATE,CLASS)" + " VALUES (?,?,?,?,?,?,?)";
//		int mtid = Integer.parseInt(new ApplicationHelper().getGeneratedId("AM_GB_OPEX"));
//		System.out.println("################ the mtid value is " + mtid);
	try {
		con = getConnection();
		ps = con.prepareStatement(query);
		// ps.setLong(1, System.currentTimeMillis());
		ps.setString(1, type.getTypeCode());
		ps.setString(2, type.getDescription());
		ps.setString(3, type.getDrAccount());
		ps.setString(4, type.getCrAccount());
		ps.setString(5, type.getUserId());
		ps.setDate(6, dateConvert(new java.util.Date()));
		ps.setString(7,type.getTypeClass());

		done = (ps.executeUpdate() != -1);
	} catch (Exception e) {
		System.out.println("WARNING:Error executing Query in createOpexAcctType ->"
				+ e.getMessage());
	} finally {
		closeConnection(con, ps);
	}
	return done;

}

public opexAcctType getOpexAcctTypeByTypeCode(String TypeCode) {

	legend.admin.objects.opexAcctType type = null;
	String query = "SELECT MTID,CODE, DESCRIPTION"
			+ ", GL_ACCOUNT,CR_ACCOUNT, USER_ID, CREATE_DATE,MTID,STATUS"
			+ " FROM AM_GB_OPEX WHERE CODE = ?";

	
	try(Connection conn = getConnection();
             PreparedStatement s = conn.prepareStatement(query);
             ResultSet rs = s.executeQuery()) {	
		s.setString(1, TypeCode);
		while (rs.next()) {
			String typeId = rs.getString("MTID");
			String typeCode = rs.getString("CODE");
			String description = rs.getString("DESCRIPTION");
			String glaccount = rs.getString("GL_ACCOUNT");
			String craccount = rs.getString("CR_ACCOUNT");
			String userId = rs.getString("USER_ID");
			String createDate = rs.getString("CREATE_DATE");
			String status = rs.getString("STATUS");
			type = new legend.admin.objects.opexAcctType();
			type.setTypeId(typeId);
			type.setTypeCode(typeCode);
			type.setDescription(description);
			type.setUserId(userId);
			type.setCreateDate(createDate);
			type.setDrAccount(glaccount);
			type.setCrAccount(craccount);
			type.setStatus(status);

		}

	} catch (Exception e) {
		e.printStackTrace();
	}

	
	return type;

}

public boolean dropObjectOld(String query)
{
//	System.out.println("====findObject query=====  "+query);
	boolean done = false;
    Connection Con2 = null;
    PreparedStatement Stat = null;
    ResultSet result = null;

    try {

    	Con2 = getConnection();
		ps = Con2.prepareStatement(query);
		done = (ps.executeUpdate() != -1);

    } catch (Exception ee2) {
        System.out.println("WARN:ERROR dropObject --> " + ee2);
        ee2.printStackTrace();
    } finally {
        closeConnection(Con2, Stat);
    }

    return done;
}

public boolean dropObject(String query) {
    boolean done = false;

    try (Connection con = getConnection();
         PreparedStatement ps = con.prepareStatement(query)) {

        done = ps.executeUpdate() > 0;

    } catch (Exception e) {
        System.out.println("WARN: ERROR dropObject -> " + e.getMessage());
        e.printStackTrace();
    }

    return done;
}


public boolean runDropTablesOld(String record) {

	Connection con = null;
	PreparedStatement ps = null;
	boolean done = false;
//	System.out.println("====runDropTables record=====  "+record);
	try {
		con = getConnection();
		ps = con.prepareStatement(record);
		done = (ps.executeUpdate() != -1);
	} catch (Exception e) {
		System.out.println("WARNING:Error executing Query in runDropTables ->"
				+ e.getMessage());
	} finally {
		closeConnection(con, ps);
	}
	return done;

}

public boolean runDropTables(String sql) {
    boolean done = false;

    if (sql == null || sql.trim().isEmpty()) {
        return false;
    }

    try (Connection con = getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        done = (ps.executeUpdate() != -1);

    } catch (Exception e) {
        System.out.println("WARNING: Error executing runDropTables -> " + e.getMessage());
        e.printStackTrace();
    }

    return done;
}


public java.util.ArrayList getDropTableRecordsOld()
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
		String query = " SELECT * FROM DROP_TABLE";
//	System.out.println("=======query====: "+query);
	Connection c = null;
	ResultSet rs = null;
	Statement s = null;

	try {
		    c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next())
			   {
				String strRecord = rs.getString("RECORD");
				int id = rs.getInt("ID");
//				System.out.println("=======record====: "+strRecord+"       Id: "+id);
				SendMail record = new SendMail();
				record.setId(id);
				record.setAddress(strRecord);
				_list.add(record);
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

public java.util.ArrayList<SendMail> getDropTableRecords() {
    java.util.ArrayList<SendMail> list = new java.util.ArrayList<>();

    String query = "SELECT * FROM DROP_TABLE";

    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

        while (rs.next()) {
            int id = rs.getInt("ID");
            String recordStr = rs.getString("RECORD");

            SendMail record = new SendMail();
            record.setId(id);
            record.setAddress(recordStr);

            list.add(record);
        }

    } catch (Exception e) {
        System.out.println("WARN: getDropTableRecords failed -> " + e.getMessage());
        e.printStackTrace();
    }

    return list;
}


public String createAssetDescriptions(
		legend.admin.objects.AssetDescription descript) {
	Connection con = null;
	PreparedStatement ps = null;
	boolean done = false;
	String result="";
	String query = "SET IDENTITY_INSERT AM_ASSET_DESCRIPTION ON INSERT INTO AM_ASSET_DESCRIPTION(ID,SUB_CATEGORY_CODE, "
			+ "DESCRIPTION, STATUS, USER_ID, CREATE_DATE) "
			+ " VALUES(?, ?, ?, ?, ?, ?) SET IDENTITY_INSERT AM_ASSET_DESCRIPTION OFF";

	try {
		con = getConnection();
		ps = con.prepareStatement(query);
		String stringid = new ApplicationHelper().getGeneratedId("AM_ASSET_DESCRIPTION");
		ps.setString(1, stringid);
		ps.setString(2, descript.getSubCategoryCode());
		ps.setString(3, descript.getDescription());
		ps.setString(4, descript.getDescriptionStatus());
		ps.setString(5, descript.getUserId());
		ps.setDate(6, dateConvert(descript.getCreateDate()));
		result = stringid;
		done = (ps.executeUpdate() != -1);
//		 System.out.println("======>result===>: "+result+"   done: "+done);
	} catch (Exception e) {
		System.out.println("WARNING:Error executing Query for Asset Descriptions ->"
				+ e.getMessage());
	} finally {
		closeConnection(con, ps);
	}
	return result;
}

public java.util.ArrayList getAssetDescriptionsByStatus(String status) {
	String filter = " AND STATUS=?";
	java.util.ArrayList _list = getAssetDescriptionsByQuery(filter,status);
	return _list;
}

public legend.admin.objects.AssetDescription getAssetDescriptionsById(
		String descriptId) {
	String filter = " AND ID=?";
	legend.admin.objects.AssetDescription descripts = null;
	java.util.ArrayList _list = getAssetDescriptionsByQuery(filter,descriptId);

//	descripts = (legend.admin.objects.AssetDescription) _list.get(0);
	if (_list != null && _list.size() > 0) {
		descripts = (legend.admin.objects.AssetDescription) _list.get(0);
	}
//	System.out.println("[[[[[[[[descripts]]]]]]: "+descripts);
	return descripts;
}

public legend.admin.objects.AssetDescription getAssetDescriptionsBySubCatCode(
		String subCatCode) {
	String filter = " AND SUB_CATEGORY_CODE=?";
	legend.admin.objects.AssetDescription descripts = null;
	java.util.ArrayList _list = getAssetDescriptionsByQuery(filter,subCatCode);
	if (_list != null && _list.size() > 0) {
		descripts = (legend.admin.objects.AssetDescription) _list.get(0);
	}
//	System.out.println("=======descripts====: "+descripts);
	return descripts;
}

	public java.util.ArrayList getAssetDescriptionsByQuery(String filter, String status) {

		java.util.ArrayList list = new java.util.ArrayList();

		StringBuilder query = new StringBuilder(
				"SELECT ID, SUB_CATEGORY_CODE, DESCRIPTION, STATUS, USER_ID, CREATE_DATE " +
						"FROM AM_ASSET_DESCRIPTION WHERE ID IS NOT NULL "
		);

		if (filter != null && !filter.trim().isEmpty()) {
			query.append(" ").append(filter).append(" ");
		}

		query.append(" ORDER BY DESCRIPTION");

		try (Connection con = getConnection();
			 PreparedStatement ps = con.prepareStatement(query.toString())) {

			// Only bind parameter if placeholder exists
			if (query.indexOf("?") != -1) {
				ps.setString(1, status);
			}

			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {

					legend.admin.objects.AssetDescription descripts =
							new legend.admin.objects.AssetDescription();

					descripts.setDescriptionId(rs.getString("ID"));
					descripts.setSubCategoryCode(rs.getString("SUB_CATEGORY_CODE"));
					descripts.setDescription(rs.getString("DESCRIPTION"));
					descripts.setDescriptionStatus(rs.getString("STATUS"));
					descripts.setUserId(rs.getString("USER_ID"));

					java.sql.Date createDate = rs.getDate("CREATE_DATE");
					if (createDate != null) {
						descripts.setCreateDate(sdf.format(createDate));
					}

					list.add(descripts);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return list;
	}



public legend.admin.objects.Staffs getStaffById(
		String staffId) {
	String filter = " AND STAFFID=?";
	legend.admin.objects.Staffs staff = null;
	java.util.ArrayList _list = getStaffListByQuery(filter,staffId);

//	descripts = (legend.admin.objects.AssetDescription) _list.get(0);
	//System.out.println("[[[[[[[[list size]]]]]]: "+_list.size());
	if (_list != null && _list.size() > 0) {
		staff = (legend.admin.objects.Staffs) _list.get(0);
	}
	//System.out.println("[[[[[[[[descripts]]]]]]: "+staff);
	return staff;
}

public java.util.ArrayList getStaffListByQuery(String filter, String status) {
	java.util.ArrayList _list = new java.util.ArrayList();
	legend.admin.objects.Staffs staffs = null;

	String query = "SELECT STAFFID, FULL_NAME, DEPT_CODE, BRANCH_CODE, STAFF_STATUS "
			+ "  FROM AM_GB_STAFF WHERE STAFFID IS NOT NULL ";

	query += filter+"ORDER BY FULL_NAME";
//	System.out.println("=======query====: "+query);
	PreparedStatement s = null;
	try {
		con = getConnection();
//		stmt = con.createStatement();
//		rs = stmt.executeQuery(query);
		s = con.prepareStatement(query.toString());
		s.setString(1, status);
		rs = s.executeQuery();

		while (rs.next()) {
			String staffId = rs.getString("STAFFID");
			String fullName = rs.getString("FULL_NAME");
			String deptCode = rs.getString("DEPT_CODE");
			String branchCode = rs.getString("BRANCH_CODE");
			String staffStatus = rs.getString("STAFF_STATUS");


			staffs = new legend.admin.objects.Staffs();
			staffs.setStaffId(staffId);
			staffs.setFullName(fullName);
			staffs.setDeptCode(deptCode);
			staffs.setBranchCode(branchCode);
			staffs.setStaffStatus(staffStatus);


			_list.add(staffs);
		}
	} catch (Exception ex) {
		ex.printStackTrace();
	} finally {
		closeConnection(con, stmt, rs);
	}
	return _list;

}


public boolean updateAssetDescription(
		legend.admin.objects.AssetDescription descript) {
	Connection con = null;
	PreparedStatement ps = null;
	boolean done = false;
	String query = "UPDATE AM_ASSET_DESCRIPTION SET SUB_CATEGORY_CODE=?, description=?, "
			+ "STATUS=? WHERE ID=?";

	try {
		con = getConnection();
		ps = con.prepareStatement(query);

		ps.setString(1, descript.getSubCategoryCode());
		ps.setString(2, descript.getDescription());
		ps.setString(3, descript.getDescriptionStatus());
		//ps.setDate(5, df.dateConvert(dispose.getCreateDate()));
		ps.setString(4, descript.getDescriptionId());

		done = (ps.executeUpdate() != -1);

	} catch (Exception e) {
		e.printStackTrace();
		System.out.println("WARNING:Error executing Update Query for Asset Descriptions ->"
				+ e.getMessage());
	} finally {
		closeConnection(con, ps);
	}
	return done;
}


public boolean updateStaffList(
		legend.admin.objects.Staffs staff) {
	Connection con = null;
	PreparedStatement ps = null;
	boolean done = false;
	String query = "UPDATE AM_GB_STAFF SET Full_Name=?, "
			+ "STAFF_STATUS=? WHERE STAFFID=?";

	try {
		con = getConnection();
		ps = con.prepareStatement(query);

		ps.setString(1, staff.getFullName());
		ps.setString(2, staff.getStaffStatus());
		ps.setString(3, staff.getStaffId());


		done = (ps.executeUpdate() != -1);

	} catch (Exception e) {
		e.printStackTrace();
		System.out.println("WARNING:Error executing Update Query for Staff List ->"
				+ e.getMessage());
	} finally {
		closeConnection(con, ps);
	}
	return done;
}

public boolean addOnAssetManagerInfo(legend.admin.objects.Company company,String recId) {
	Connection con = null;
	PreparedStatement ps = null;
	boolean done = false;
	String query = "UPDATE a SET  a.Processing_Date = b.Processing_Date ,a.Processing_Frequency = b.Processing_Frequency, a.Next_Processing_Date = b.Next_Processing_Date, a.Default_Branch = b.Default_Branch, "
			+ "a.Branch_Name = b.Branch_Name,a.Suspense_Acct = b.Suspense_Acct ,a.Auto_Generate_Id = b.Auto_Generate_Id, a.Residual_Value = b.Residual_Value, a.Depreciation_Method = b.Depreciation_Method, "
			+ "a.Vat_Account = b.Vat_Account, a.Wht_Account = b.Wht_Account,  a.PL_Disposal_Account = b.PL_Disposal_Account, a.PLD_Status = b.PLD_Status, a.Vat_Acct_Status = b.Vat_Acct_Status, a.Wht_Acct_Status = b.Wht_Acct_Status, "
			+ "a.Suspense_Ac_Status = b.Suspense_Ac_Status, a.Sbu_Required = b.Sbu_Required, a.Sbu_Level = b.Sbu_Level, a.asset_acq_ac = b.asset_acq_ac, a.LPO_Required=b.LPO_Required, a.Barcode_Fld=b.Barcode_Fld, "
			+ "a.Cost_Threshold=b.Cost_Threshold, a.Trans_threshold=b.Trans_threshold, a.Defer_account=b.Defer_account, a.Fed_Wht_Account =b.Fed_Wht_Account, a.Fed_Wht_Acct_Status = b.Fed_Wht_Acct_Status, "
			+ "a.part_pay_acct=b.part_pay_acct, a.asset_acq_status=b.asset_acq_status, a.asset_defer_status=b.asset_defer_status, a.part_pay_status=b.part_pay_status,a.THIRDPARTY_REQUIRE=b.THIRDPARTY_REQUIRE, "
			+ "a.RAISE_ENTRY=b.RAISE_ENTRY,a.system_date=b.system_date, a.loss_disposal_account=b.loss_disposal_account,a.lossDisposal_act_status=b.lossDisposal_act_status, "
			+ "a.group_asset_account=b.group_asset_account,a.group_asset_act_status=b.group_asset_act_status,a.selfChargeVAT=b.selfChargeVAT,a.selfCharge_Vat_status=b.selfCharge_Vat_status,a.databaseName=b.databaseName  "
			+ "from AM_GB_COMPANYTEMP a, AM_GB_COMPANY b where a.company_code = b.company_code and TMPID = ?";

	try {
		con = getConnection();
		ps = con.prepareStatement(query);

		ps.setString(1,recId);

		done = (ps.executeUpdate() != -1);

	} catch (Exception e) {
		e.printStackTrace();
		System.out.println("WARNING:Error executing Update Query for AddOnAssetManagerInfo ->"
				+ e.getMessage());
	} finally {
		closeConnection(con, ps);
	}
	return done;
}

public boolean addOnCompanyDefaultsInfo(legend.admin.objects.AssetManagerInfo ami,String recId) {
	Connection con = null;
	PreparedStatement ps = null;
	boolean done = false;
	String query = "update a SET a.Company_Code=b.Company_Code, a.Company_Name=b.Company_Name, a.Acronym=b.Acronym, a.Company_Address=b.Company_Address, a.Vat_Rate=b.Vat_Rate, a.Wht_Rate=b.Wht_Rate,"
			+ "a.Financial_Start_Date=b.Financial_Start_Date, a.Financial_No_OfMonths=b.Financial_No_OfMonths, a.Financial_End_Date=b.Financial_End_Date,a.Minimum_Password=b.Minimum_Password, "
			+ "a.Password_Expiry=b.Password_Expiry, a.Session_Timeout=b.Session_Timeout, a.Enforce_Acq_Budget=b.Enforce_Acq_Budget, a.Enforce_Pm_Budget=b.Enforce_Pm_Budget, "
			+ "a.Enforce_Fuel_Allocation=b.Enforce_Fuel_Allocation, a.Require_Quarterly_Pm=b.Require_Quarterly_Pm,a.Quarterly_Surplus_Cf=b.Quarterly_Surplus_Cf,a.User_Id=b.User_Id,a.loguseraudit=b.loguseraudit,"
			+ "a.Attempt_Logon=b.Attempt_Logon,a.component_delimiter=b.component_delimiter,a.password_limit=b.password_limit,a.PROCESSING_DATE=b.PROCESSING_DATE, a.password_upper=b.password_upper,"
			+ "a.password_lower=b.password_lower,a.password_numeric=b.password_numeric,a.password_special=b.password_special,a.Proof_Session_timeout=b.Proof_Session_timeout "
			+ "from AM_GB_COMPANYTEMP a, AM_GB_COMPANY b where TMPID = ?";
//	System.out.println("query in addOnCompanyDefaultsInfo: "+query);
	try {
		con = getConnection();
		ps = con.prepareStatement(query);

		ps.setString(1,recId);

		done = (ps.executeUpdate() != -1);

	} catch (Exception e) {
		e.printStackTrace();
		System.out.println("WARNING:Error executing Update Query for addOnCompanyDefaultsInfo ->"
				+ e.getMessage());
	} finally {
		closeConnection(con, ps);
	}
	return done;
}

public String getLegacyBranchRecords(String legacyTransId)
{
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String date1 =date.substring(0,2);
	String date0 = date.substring(2,10);
//	System.out.println("======date1: "+date1+"    date0: "+date0+"   date: "+date);
	date = "20"+date.substring(2,10);
//	System.out.println("System Date in getFinacleRecords====> "+date+"====finacleTransId==>> "+finacleTransId);
	String iso ="";
		String query = " SELECT  tran_particular_2 from fix_tb " +
				"where tran_particular_2=? and to_char(tran_date,'DD-MM-YYYY') >= ?";
//		System.out.println("Query on getFinacleRecords====> "+query);
	Connection c = null;
//	ConnectionClass connection = null;
	ResultSet rs = null;
	PreparedStatement ps = null;

	try {
		   c = getFinacleConnection();
       //connection = new ConnectionClass();
       //ps = connection.getPreparedStatementOracle(query);
//		    c = getConnectionFinacle();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			//rs = ps.executeQuery(query);
		   rs = c.prepareStatement(query).executeQuery();

			ps = c.prepareStatement(query.toString());
			ps.setString(1, legacyTransId);
			ps.setString(2, getOracleDateFormat(date));
			rs = ps.executeQuery();
			while (rs.next())
			   {
				 iso = "000";
			   }
//			System.out.println("================ISO Value: "+iso);
	 	}
				 catch (Exception e)
					{
						e.printStackTrace();
					}
					finally
					{
//						connection.freeResource();
						closeConnection(c, ps, rs);
					}
		 	return iso;
}

//public java.util.ArrayList getNewBranchRecordsFromLegacySystem()
//{
//	java.util.ArrayList _list = new java.util.ArrayList();
//	String date = String.valueOf(dateConvert(new java.util.Date()));
//	date = date.substring(0, 10);
//	String date1 =date.substring(0,2);
//	String date0 = date.substring(2,10);
////	System.out.println("======date1: "+date1+"    date0: "+date0+"   date: "+date);
//	date = "20"+date.substring(2,10);
//	String iso ="";
//		String query = " SELECT  * FROM ZENITHUBS.NEW_BRANCH_DETAILS " ;
////				"where to_char(tran_date,'DD-MM-YYYY') >= ?";
////		System.out.println("Query on getFinacleRecords====> "+query);
//	Connection c = null;
////	ConnectionClass connection = null;
//	ResultSet rs = null;
//	PreparedStatement ps = null;
//
//	try {
//		   c = getFinacleConnection();
//		   rs = c.prepareStatement(query).executeQuery();
//			ps = c.prepareStatement(query.toString());
////			ps.setString(1, getOracleDateFormat(date));
//			rs = ps.executeQuery();
//			while (rs.next())
//			   {
//				String branchCode = rs.getString("BRANCH_CODE");
//				String branchName = rs.getString("BRANCH_NAME");
//				String branchAddress = rs.getString("BRANCH_ADDR1");
//				Branch branch = new Branch();
//				branch.setBranchCode(branchCode);;
//				branch.setBranchAddress(branchAddress);
//				branch.setBranchName(branchName);
//				_list.add(branch);
//			   }
//	 	}
//				 catch (Exception e)
//					{
//						e.printStackTrace();
//					}
//					finally
//					{
////						connection.freeResource();
//						closeConnection(c, ps, rs);
//					}
//			return _list;
//}


public java.util.ArrayList<Branch> getNewBranchRecordsFromLegacySystemOld() {
    java.util.ArrayList<Branch> branchList = new java.util.ArrayList<>();

    String query = "SELECT * FROM ZENITHUBS.NEW_BRANCH_DETAILS";

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
        conn = getFinacleConnection();
        ps = conn.prepareStatement(query);
        rs = ps.executeQuery();

        while (rs.next()) {
            String branchCode = rs.getString("BRANCH_CODE");
            String branchName = rs.getString("BRANCH_NAME");
            String branchAddress = rs.getString("BRANCH_ADDR1");

            Branch branch = new Branch();
            branch.setBranchCode(branchCode);
            branch.setBranchName(branchName);
            branch.setBranchAddress(branchAddress);

            branchList.add(branch);
        }

    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        closeConnection(conn, ps, rs);
    }

    return branchList;
}

public java.util.ArrayList<Branch> getNewBranchRecordsFromLegacySystem() {
    java.util.ArrayList<Branch> branchList = new java.util.ArrayList<>();
    String query = "SELECT * FROM ZENITHUBS.NEW_BRANCH_DETAILS";

    try (Connection conn = getFinacleConnection();
         PreparedStatement ps = conn.prepareStatement(query);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Branch branch = new Branch();
            branch.setBranchCode(rs.getString("BRANCH_CODE"));
            branch.setBranchName(rs.getString("BRANCH_NAME"));
            branch.setBranchAddress(rs.getString("BRANCH_ADDR1"));
            branchList.add(branch);
        }

    } catch (Exception e) {
    	System.out.println("Error fetching branches from legacy system" + e);
    }

    return branchList;
}




public boolean InsertNewBranchOld(String branchCode, String branchName, String branchAddress,String stateId)
{
    Connection con;
    PreparedStatement ps;
    boolean done;
    String query;
    con = null;
    ps = null;
    done = false;
    query = "INSERT INTO AM_AD_BRANCH(BRANCH_ID,BRANCH_CODE,BRANCH_NAME,BRANCH_ACRONYM,GL_PREFIX,BRANCH_ADDRESS,STATE,REGION_CODE,BRANCH_STATUS,CREATE_DATE,EMAIL,PHONE_NO,USER_ID) "
    		+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)"
;
//    System.out.println("################ the histId value is "+histId+"  integrifyId: "+integrifyId+"  tranType: "+tranType);
    try
    {
    	String  groupid =  new ApplicationHelper().getGeneratedId("AM_AD_BRANCH");
        con = getConnection();
        ps = con.prepareStatement(query);
        String acronym = branchName.substring(1,3);
        String newAcronym = acronym;
        String email = getCodeName("select companyMail from am_gb_company");
        String number = getCodeName("select count(*)  from am_ad_branch where BRANCH_ACRONYM='"+acronym+"' ");
        if(!number.equals("")) {acronym = acronym+""+"1";}
        number = getCodeName("select count(*)  from am_ad_branch where BRANCH_ACRONYM='"+acronym+"' ");
        if(!number.equals("")) {acronym = acronym+""+"2";}
        ps.setString(1, groupid);
        ps.setString(2, branchCode);
        ps.setString(3, branchName);
        ps.setString(4, acronym);
        ps.setString(5, branchCode);
        ps.setString(6, branchAddress);
//        ps.setString(7, stateId);
        ps.setString(7, "1");
        ps.setString(8, "001");
        ps.setString(9, "ACTIVE");
        ps.setDate(10, dateConvert(new Date()));
        ps.setString(11, email);
        ps.setString(12, "012665944");
        ps.setString(13, "0");
        done = ps.executeUpdate() != -1;
//      System.out.println("################ Branch addition done: "+done);
    }
    catch(Exception e)
    {
        System.out.println("WARNING:Error executing in InsertNewBranch ->"+e.getMessage());
    } finally {
    	closeConnection(con, ps);
	}
    return done;
}

public boolean InsertNewBranch(String branchCode, String branchName, String branchAddress, String stateId) {
    boolean done = false;
    String query = "INSERT INTO AM_AD_BRANCH("
            + "BRANCH_ID, BRANCH_CODE, BRANCH_NAME, BRANCH_ACRONYM, GL_PREFIX, "
            + "BRANCH_ADDRESS, STATE, REGION_CODE, BRANCH_STATUS, CREATE_DATE, EMAIL, PHONE_NO, USER_ID) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection con = getConnection();
         PreparedStatement ps = con.prepareStatement(query)) {

        // Generate ID
        String branchId = new ApplicationHelper().getGeneratedId("AM_AD_BRANCH");

        // Generate acronym safely
        String acronym = branchName.length() >= 3 ? branchName.substring(0, 3).toUpperCase() : branchName.toUpperCase();
        int count = Integer.parseInt(getCodeName("SELECT COUNT(*) FROM am_ad_branch WHERE BRANCH_ACRONYM='" + acronym + "'"));
        if (count > 0) acronym += count;  // Append number if duplicate

        String email = getCodeName("SELECT companyMail FROM am_gb_company");

        ps.setString(1, branchId);
        ps.setString(2, branchCode);
        ps.setString(3, branchName);
        ps.setString(4, acronym);
        ps.setString(5, branchCode);
        ps.setString(6, branchAddress);
        ps.setString(7, stateId != null ? stateId : "1");
        ps.setString(8, "001");
        ps.setString(9, "ACTIVE");
        ps.setDate(10, dateConvert(new Date()));
        ps.setString(11, email != null ? email : "");
        ps.setString(12, "012665944");
        ps.setString(13, "0");

        done = ps.executeUpdate() > 0;

    } catch (Exception e) {
    	System.out.println("Error inserting new branch: " + branchCode + e);
    }

    return done;
}



	public List<newAssetTransaction> getLegacyTransactionRecords(
			String fromDate,
			String toDate,
			String bankingApp,
			String legacySysId) {

		List<newAssetTransaction> list = new ArrayList<>();

		DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("dd-MMM-yyyy");

		String formattedFromDate;
		String formattedToDate;

		try {
			LocalDate from = LocalDate.parse(fromDate, inputFormat);
			LocalDate to = LocalDate.parse(toDate, inputFormat);

			formattedFromDate = from.format(outputFormat);
			formattedToDate = to.format(outputFormat);

		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid date format. Expected dd-MM-yyyy", e);
		}

		String query;

		if ("FLEXCUBE".equalsIgnoreCase(bankingApp)) {

			query = "SELECT * FROM ZENITHUBS.TRANSACTION_DETAILS " +
					"WHERE POSTING_DATE BETWEEN ? AND ? " +
					"AND MAKER_ID = ?";

			try (Connection con = getFinacleConnection();
				 PreparedStatement ps = con.prepareStatement(query)) {

				ps.setQueryTimeout(60);
				ps.setString(1, formattedFromDate);
				ps.setString(2, formattedToDate);
				ps.setString(3, legacySysId);

				try (ResultSet rs = ps.executeQuery()) {

					while (rs.next()) {

						newAssetTransaction tx = new newAssetTransaction();

						tx.setAssetId(rs.getString("MAKER_ID"));
						tx.setBarCode(rs.getString("SERIAL_NO"));
						tx.setSbuCode(rs.getString("CHECKER_ID"));
						tx.setDescription(rs.getString("TRANSACTION_DESCRIPTION"));
						tx.setAssetUser(rs.getString("BATCH_NO"));
						tx.setVendorAC(rs.getString("ACCOUNT_NO"));
						tx.setAssetType(rs.getString("TRANSACTION_TYPE"));
						tx.setBranchCode(rs.getString("BRANCH_CODE"));
						tx.setPostingDate(rs.getString("POSTING_DATE"));

						String amount = rs.getString("AMOUNT");
						tx.setCostPrice(parseAmountSafely(amount));

						list.add(tx);
					}
				}

			} catch (SQLException e) {
				throw new RuntimeException("Error fetching FLEXCUBE transactions", e);
			}

		} else if ("FINACLE".equalsIgnoreCase(bankingApp)) {

			query = "SELECT * FROM CUSTOM.BULK_LGD " +
					"WHERE VALUE_DATE BETWEEN TO_DATE(?, 'DD-MON-YYYY') " +
					"AND TO_DATE(?, 'DD-MON-YYYY')";

			try (Connection con = getFinacleConnection();
				 PreparedStatement ps = con.prepareStatement(query)) {

				ps.setQueryTimeout(60);
				ps.setString(1, formattedFromDate);
				ps.setString(2, formattedToDate);

				try (ResultSet rs = ps.executeQuery()) {

					while (rs.next()) {

						newAssetTransaction tx = new newAssetTransaction();

						tx.setAssetId(rs.getString("ID"));
						tx.setBarCode(rs.getString("TRAN_ID"));
						tx.setSbuCode(rs.getString("SBU_CODE"));
						tx.setDebitAccount(rs.getString("DR_ACCT"));
						tx.setCreditAccount(rs.getString("CR_ACCT"));
						tx.setDebitAccountName(rs.getString("NARRATION"));
						tx.setCreditAccountName(rs.getString("NARRATION2"));
						tx.setTransDate(rs.getString("VALUE_DATE"));
						tx.setPostingDate(rs.getString("RCRE_TIME"));
						tx.setAssetStatus(rs.getString("PROCESSED_FLG"));
						tx.setUserID(rs.getString("FIN_USER"));
						tx.setResponse(rs.getString("REMARKS"));

						String amount = rs.getString("AMOUNT");
						tx.setCostPrice(parseAmountSafely(amount));

						list.add(tx);
					}
				}

			} catch (SQLException e) {
				throw new RuntimeException("Error fetching FINACLE transactions", e);
			}
		}

		return list;
	}
	private double parseAmountSafely(String amount) {
		try {
			return amount != null ? Double.parseDouble(amount) : 0.0;
		} catch (NumberFormatException e) {
			return 0.0;
		}
	}

public boolean InsertLegacyTransactionsOld(String makerId, String serialNo, String checkerId, String transDescription,String batchNo, String accountNo, double amount, String tranType, String branchCode, String postingDate,String bankingApp)

{
    Connection con;
    PreparedStatement ps;
    boolean done;
    String query ="";
    con = null;
    ps = null;
    done = false;
    if(bankingApp.equalsIgnoreCase("FLEXCUBE")) {
    	query = "INSERT INTO LEGACY_TRANSACTION(BATCH_NO,SERIAL_NO,ACCOUNT_NO,BRANCH_CODE,TRANSACTION_TYPE,AMOUNT,TRANSACTION_DESCRIPTION,MAKER_ID,CHECKER_ID,POSTING_DATE) "
    		+ "VALUES (?,?,?,?,?,?,?,?,?,?)";
//    	System.out.println("<<<<<<query: "+query);
    }
    if(bankingApp.equalsIgnoreCase("FINACLE")) {
    	query = "INSERT INTO LEGACY_TRANSACTION(BATCH_ID,SBU_CODE,ASSET_ID,DRACCOUNT_NO,CRACCOUNT_NO,AMOUNT,DR_DESCRIPTION,CR_DESCRIPTION,POSTING_DATE,TRANSACTION_DATE,FIN_USER,PROCESSED_FLAG,ERROR_MSG) "
    		+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
    	System.out.println("<<<<<<query: "+query);
    }
//    System.out.println("################ the histId value is "+histId+"  integrifyId: "+integrifyId+"  tranType: "+tranType);
//    System.out.println("<<<<<<query: "+query);
    try
    {
    	 if(bankingApp.equalsIgnoreCase("FLEXCUBE")) {
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, batchNo);
        ps.setString(2, serialNo);
        ps.setString(3, accountNo);
        ps.setString(4, branchCode);
        ps.setString(5, tranType);
        ps.setDouble(6, amount);
        ps.setString(7, transDescription);
        ps.setString(8, makerId);
        ps.setString(9, checkerId);
        ps.setString(10, postingDate);
        done = ps.executeUpdate() != -1;
//      System.out.println("################ Branch addition done: "+done);
    	 }
    	 if(bankingApp.equalsIgnoreCase("FINACLE")) {
//    	        con = getFinacleConnection();
    		 	con = getConnection();
    	        ps = con.prepareStatement(query);
    	        System.out.println("################ tranType: "+tranType+"      ###### postingDate: "+postingDate);
    	        String[] arrOfStr = tranType.split("&");
    	        tranType = arrOfStr[0];
    	        System.out.println("################ tranType: "+tranType);
    	        String processedFlag = arrOfStr[1];
    	        String reamrks = arrOfStr[2];
    	        System.out.println("################ processedFlag: "+processedFlag+"   reamrks: "+reamrks);
    	        String[] arrOfDate = postingDate.split("&");
    	        postingDate = arrOfDate[0];
    	        System.out.println("################ postingDate: "+postingDate);
    	        String transactionDate = arrOfDate[1];
    	        System.out.println("################ transactionDate: "+transactionDate);
    	        ps.setString(1, serialNo);
    	        System.out.println("################ serialNo: "+serialNo);
    	        ps.setString(2, checkerId);
    	        ps.setString(3, makerId);
    	        ps.setString(4, accountNo);
    	        ps.setString(5, branchCode);
    	        ps.setDouble(6, amount);
    	        System.out.println("################ amount: "+amount);
    	        ps.setString(7, transDescription);
    	        System.out.println("################ transDescription: "+transDescription);
    	        ps.setString(8, batchNo);
    	        ps.setString(9, postingDate);
    	        System.out.println("################ postingDate: "+postingDate);
    	        ps.setString(10, transactionDate);
    	        ps.setString(11, tranType);
    	        ps.setString(12, processedFlag);
    	        ps.setString(13, reamrks);
    	        System.out.println("################ processedFlag: "+processedFlag);
    	        done = ps.executeUpdate() != -1;
    	    	 }
    }
    catch(Exception e)
    {
        System.out.println("WARNING:Error executing Query in InsertLegacyTransactions ->"+e.getMessage());
    } finally {
    	closeConnection(con, ps);
	}
    return done;
}
	public boolean InsertLegacyTransactions(
			String makerId,
			String serialNo,
			String checkerId,
			String transDescription,
			String batchNo,
			String accountNo,
			double amount,
			String tranType,
			String branchCode,
			String postingDate,
			String bankingApp) {

		if (bankingApp == null) {
			throw new IllegalArgumentException("bankingApp cannot be null");
		}

		boolean done = false;
		String query;

		if ("FLEXCUBE".equalsIgnoreCase(bankingApp)) {

			query = "INSERT INTO LEGACY_TRANSACTION(" +
					"BATCH_NO,SERIAL_NO,ACCOUNT_NO,BRANCH_CODE," +
					"TRANSACTION_TYPE,AMOUNT,TRANSACTION_DESCRIPTION," +
					"MAKER_ID,CHECKER_ID,POSTING_DATE) " +
					"VALUES (?,?,?,?,?,?,?,?,?,?)";

			try (Connection con = getConnection();
				 PreparedStatement ps = con.prepareStatement(query)) {

				ps.setQueryTimeout(30);

				ps.setString(1, batchNo);
				ps.setString(2, serialNo);
				ps.setString(3, accountNo);
				ps.setString(4, branchCode);
				ps.setString(5, tranType);
				ps.setDouble(6, amount);
				ps.setString(7, transDescription);
				ps.setString(8, makerId);
				ps.setString(9, checkerId);
				ps.setString(10, postingDate);

				done = ps.executeUpdate() > 0;

			} catch (Exception e) {
				throw new RuntimeException("Error inserting FLEXCUBE legacy transaction", e);
			}

		} else if ("FINACLE".equalsIgnoreCase(bankingApp)) {

			query = "INSERT INTO LEGACY_TRANSACTION(" +
					"BATCH_ID,SBU_CODE,ASSET_ID,DRACCOUNT_NO,CRACCOUNT_NO," +
					"AMOUNT,DR_DESCRIPTION,CR_DESCRIPTION,POSTING_DATE," +
					"TRANSACTION_DATE,FIN_USER,PROCESSED_FLAG,ERROR_MSG) " +
					"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";

			try (Connection con = getConnection();
				 PreparedStatement ps = con.prepareStatement(query)) {

				ps.setQueryTimeout(30);

				// --- Safe parsing of tranType ---
				String actualTranType = null;
				String processedFlag = null;
				String remarks = null;

				String[] typeParts = safeSplit(tranType, 3);
				actualTranType = typeParts[0];
				processedFlag = typeParts[1];
				remarks = typeParts[2];

				// --- Safe parsing of postingDate ---
				String actualPostingDate = null;
				String transactionDate = null;

				String[] dateParts = safeSplit(postingDate, 2);
				actualPostingDate = dateParts[0];
				transactionDate = dateParts[1];

				ps.setString(1, serialNo);
				ps.setString(2, checkerId);
				ps.setString(3, makerId);
				ps.setString(4, accountNo);
				ps.setString(5, branchCode);
				ps.setDouble(6, amount);
				ps.setString(7, transDescription);
				ps.setString(8, batchNo);
				ps.setString(9, actualPostingDate);
				ps.setString(10, transactionDate);
				ps.setString(11, actualTranType);
				ps.setString(12, processedFlag);
				ps.setString(13, remarks);

				done = ps.executeUpdate() > 0;

			} catch (Exception e) {
				throw new RuntimeException("Error inserting FINACLE legacy transaction", e);
			}

		} else {
			throw new IllegalArgumentException("Unsupported bankingApp: " + bankingApp);
		}

		return done;
	}
	private String[] safeSplit(String input, int expectedParts) {

		String[] result = new String[expectedParts];

		if (input == null) {
			return result;
		}

		String[] parts = input.split("&");

		for (int i = 0; i < expectedParts; i++) {
			if (i < parts.length) {
				result[i] = parts[i];
			} else {
				result[i] = null;
			}
		}

		return result;
	}
public boolean InsertNewVendorOld(String branchCode, String branchName, String branchAddress,String stateId)
{
    Connection con;
    PreparedStatement ps;
    boolean done;
    String query;
    con = null;
    ps = null;
    done = false;
    query = "INSERT INTO am_ad_vendor(Vendor_ID,Vendor_Code,Vendor_Name,Contact_Person,Contact_Address,account_number,Vendor_Status,Create_date,RCNo) "
    		+ "VALUES(?,?,?,?,?,?,?,?,?)"
;
//    System.out.println("################ the histId value is "+histId+"  integrifyId: "+integrifyId+"  tranType: "+tranType);
    try
    {
    	String  groupid =  new ApplicationHelper().getGeneratedId("AM_AD_VENDOR");
        con = getConnection();
        ps = con.prepareStatement(query);
        String acronym = branchName.substring(1,3);
        String newAcronym = acronym;
        String vendorName = "FIXED ASSET TRANSIT - "+branchName;
        String contact = "BRANCH MANAGER";
        String accountNo = "120100015";
        ps.setString(1, groupid);
        ps.setString(2, branchCode);
        ps.setString(3, vendorName);
        ps.setString(4, contact);
        ps.setString(5, branchAddress);
        ps.setString(6, accountNo);
        ps.setString(7, "ACTIVE");
        ps.setDate(8, dateConvert(new Date()));
        ps.setString(9, "RC150224");
        done = ps.executeUpdate() != -1;
//      System.out.println("################ Vendor addition done: "+done);
    }
    catch(Exception e)
    {
        System.out.println("WARNING:Error executing InsertNewVendor ->"+e.getMessage());
    } finally {
    	closeConnection(con, ps);
	}
    return done;
}

	public boolean InsertNewVendor(String branchCode,
								   String branchName,
								   String branchAddress,
								   String stateId) {

		String query = "INSERT INTO am_ad_vendor(" +
				"Vendor_ID, Vendor_Code, Vendor_Name, Contact_Person, Contact_Address, " +
				"account_number, Vendor_Status, Create_date, RCNo) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (Connection con = getConnection();
			 PreparedStatement ps = con.prepareStatement(query)) {

			ps.setQueryTimeout(30);

			String vendorId = new ApplicationHelper().getGeneratedId("AM_AD_VENDOR");
			String vendorName = "FIXED ASSET TRANSIT - " + branchName;

			ps.setString(1, vendorId);
			ps.setString(2, branchCode);
			ps.setString(3, vendorName);
			ps.setString(4, "BRANCH MANAGER");
			ps.setString(5, branchAddress != null ? branchAddress : "");
			ps.setString(6, "120100015");
			ps.setString(7, "ACTIVE");
			ps.setDate(8, dateConvert(new Date()));
			ps.setString(9, "RC150224");

			return ps.executeUpdate() > 0;

		} catch (Exception e) {
			throw new RuntimeException("Error inserting vendor for branch: " + branchCode, e);
		}
	}
	public List<Branch> getNewBranchRecordsFromFinacleLegacySystem() {

		List<Branch> list = new ArrayList<>();

		String query = "SELECT * FROM CUSTOM.NEW_BRANCH_DETAILS";

		try (Connection con = getFinacleConnection();
			 PreparedStatement ps = con.prepareStatement(query)) {

			ps.setQueryTimeout(60);

			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {

					Branch branch = new Branch();
					branch.setBranchCode(rs.getString("SOL_ID"));
					branch.setBranchName(rs.getString("SOL_DESC"));

					String addr1 = rs.getString("ADDR_1");
					String addr2 = rs.getString("ADDR_2");
					branch.setBranchAddress(
							(addr1 != null ? addr1 : "") + " " +
									(addr2 != null ? addr2 : "")
					);

					branch.setState(rs.getString("STATE_CODE"));
					branch.setBranchStatus("ACTIVE");

					list.add(branch);
				}
			}

		} catch (SQLException e) {
			throw new RuntimeException("Error fetching new branch records from Finacle", e);
		}

		return list;
	}
	public boolean InsertNewBranch2(String branchCode,
									String branchName,
									String branchAddress,
									String stateId) {

		String query =
				"INSERT INTO AM_AD_BRANCH(" +
						"BRANCH_ID, BRANCH_CODE, BRANCH_NAME, BRANCH_ACRONYM, GL_PREFIX, " +
						"BRANCH_ADDRESS, STATE, REGION_CODE, ZONE_CODE, BRANCH_STATUS, " +
						"CREATE_DATE, EMAIL, PHONE_NO, USER_ID) " +
						"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		try (Connection con = getConnection();
			 PreparedStatement ps = con.prepareStatement(query)) {

			ps.setQueryTimeout(30);

			String branchId = new ApplicationHelper().getGeneratedId("AM_AD_BRANCH");

			String acronym = branchName.length() >= 3
					? branchName.substring(1, 3)
					: branchName;

			String email = getCodeName("select companyMail from am_gb_company");

			String number = getCodeName(
					"select count(*) from am_ad_branch where BRANCH_ACRONYM='" + acronym + "'");

			if (!number.equals("")) {
				acronym = acronym + "1";
			}

			number = getCodeName(
					"select count(*) from am_ad_branch where BRANCH_ACRONYM='" + acronym + "'");

			if (!number.equals("")) {
				acronym = acronym + "2";
			}

			stateId = getCodeName(
					"select STATE_ID from am_gb_states where STATE_ACRONYM='" + stateId + "'");

			ps.setString(1, branchId);
			ps.setString(2, branchCode);
			ps.setString(3, branchName);
			ps.setString(4, acronym);
			ps.setString(5, branchCode);
			ps.setString(6, branchAddress);
			ps.setString(7, stateId);
			ps.setString(8, "001");
			ps.setString(9, "001");
			ps.setString(10, "ACTIVE");
			ps.setDate(11, dateConvert(new Date()));
			ps.setString(12, email);
			ps.setString(13, "012665944");
			ps.setString(14, "0");

			return ps.executeUpdate() > 0;

		} catch (Exception e) {
			throw new RuntimeException("Error inserting branch: " + branchCode, e);
		}
	}

	public List<Sbu_branch> getNewSBURecordsFromFinacleLegacySystem() {

		final List<Sbu_branch> sbuList = new ArrayList<>();

		final String sql =
				"SELECT SECTORCODE, SECTORDESC " +
						"FROM CUSTOM.NEW_SBU_DETAILS";

		try (Connection connection = getFinacleConnection();
			 PreparedStatement statement = connection.prepareStatement(
					 sql,
					 ResultSet.TYPE_FORWARD_ONLY,
					 ResultSet.CONCUR_READ_ONLY)) {

			statement.setQueryTimeout(60);
			statement.setFetchSize(500); // important for large legacy datasets

			try (ResultSet resultSet = statement.executeQuery()) {

				while (resultSet.next()) {

					String sbuCode = resultSet.getString("SECTORCODE");
					String sbuName = resultSet.getString("SECTORDESC");

					Sbu_branch sbu = new Sbu_branch();
					sbu.setSbucode(sbuCode);
					sbu.setSbuname(sbuName);
					sbu.setMailcontact("info@fcmb.com"); // preserved logic
					sbu.setSbustatus("ACTIVE");          // preserved logic

					sbuList.add(sbu);
				}
			}

		} catch (Exception ex) {
			throw new RuntimeException(
					"Failed to fetch SBU records from Finacle legacy system", ex);
		}

		return sbuList;
	}


	public List<Section> getNewSectionRecordsFromFinacleLegacySystem() {

		final List<Section> sections = new ArrayList<>();

		final String sql =
				"SELECT SECTORCODE, SECTORDESC " +
						"FROM CUSTOM.NEW_SECTION_DETAILS";

		try (Connection connection = getFinacleConnection();
			 PreparedStatement statement = connection.prepareStatement(
					 sql,
					 ResultSet.TYPE_FORWARD_ONLY,
					 ResultSet.CONCUR_READ_ONLY)) {

			statement.setQueryTimeout(60);

			// Important for large legacy datasets (Oracle / Finacle style systems)
			statement.setFetchSize(500);

			try (ResultSet resultSet = statement.executeQuery()) {

				while (resultSet.next()) {

					Section section = new Section();

					String sectorCode = resultSet.getString("SECTORCODE");
					String sectorDesc = resultSet.getString("SECTORDESC");

					section.setSection_code(sectorCode);
					section.setSection_name(sectorDesc);
					section.setSection_status("ACTIVE");
					section.setSection_acronym(sectorCode);

					sections.add(section);
				}
			}

		} catch (Exception ex) {
			throw new RuntimeException(
					"Failed to fetch section records from Finacle legacy system", ex);
		}

		return sections;
	}
	public boolean insertNewSbu(String sbuCode,
								String sbuName,
								String sbuContact,
								String status) {

		final String sql =
				"INSERT INTO Sbu_SetUp(" +
						"sbu_code, sbu_name, sbu_contact, status, contact_email) " +
						"VALUES (?, ?, ?, ?, ?)";

		try (Connection connection = getConnection();
			 PreparedStatement statement = connection.prepareStatement(sql)) {

			// Ensure predictable transactional behavior
			if (connection.getAutoCommit()) {
				connection.setAutoCommit(false);
			}

			statement.setQueryTimeout(30);

			int index = 1;
			statement.setString(index++, sbuCode);
			statement.setString(index++, sbuName);
			statement.setString(index++, sbuName);     // preserved original logic
			statement.setString(index++, status);
			statement.setString(index, sbuContact);    // preserved original logic

			int rowsAffected = statement.executeUpdate();

			connection.commit();

			return rowsAffected > 0;

		} catch (SQLException | NamingException ex) {
			throw new RuntimeException(
					"Failed to insert SBU with code: " + sbuCode, ex);
		}
	}
	public boolean insertNewSection(String sectionCode,
									String sectionName,
									String sbuContact,
									String status) {

		final String sql =
				"INSERT INTO am_ad_section(" +
						"Section_Id, Section_Code, Section_Name, section_acronym, " +
						"Section_Status, user_Id, Create_Date) " +
						"VALUES (?, ?, ?, ?, ?, ?, ?)";

		
		final String generatedId = applicationHelper.getGeneratedId("am_ad_section");

		try (Connection connection = getConnection();
			 PreparedStatement statement = connection.prepareStatement(sql)) {

		
			if (connection.getAutoCommit()) {
				connection.setAutoCommit(false);
			}

			statement.setQueryTimeout(30);

			int index = 1;
			statement.setString(index++, generatedId);
			statement.setString(index++, sectionCode);
			statement.setString(index++, sectionName);
			statement.setString(index++, sectionCode); // acronym defaults to code
			statement.setString(index++, status);
			statement.setString(index++, "0"); // default system user
			statement.setDate(index, dateConvert(new Date()));

			int rowsAffected = statement.executeUpdate();

			connection.commit();

			return rowsAffected > 0;

		} catch (Exception ex) {
			throw new RuntimeException(
					"Failed to insert new Section with code: " + sectionCode, ex);
		}
	}
	public boolean APIgroupRecordProcess(String groupId, String invoiceNo) {

		boolean done = false;

		String QUERY_FOR_AM_ASSET =
				"INSERT INTO am_asset SELECT ... FROM am_asset_tmp WHERE TRANS_TYPE = 'C' AND group_id = ?";

		String QUERY_FOR_AM_ASSET_UNCAPITALIZE =
				"INSERT INTO am_asset SELECT ... FROM am_asset_tmp WHERE TRANS_TYPE = 'U' AND group_id = ?";

		String QUERY_FOR_AM_ASSET_ARCHIVE =
				"INSERT INTO AM_ASSET_ARCHIVE SELECT ... FROM am_asset_tmp WHERE group_id = ?";

		String QUERY_FOR_INVOICENO =
				"INSERT INTO AM_INVOICE_NO SELECT asset_id, lpo, ?, 'Asset Creation', ?, group_id, integrify FROM am_asset_tmp WHERE group_id = ?";

		try (Connection con = dbConnection.getConnection("legendPlus")) {

			con.setAutoCommit(false); // 🔥 CRITICAL

			try (
					PreparedStatement ps = con.prepareStatement(QUERY_FOR_AM_ASSET);
					PreparedStatement ps1 = con.prepareStatement(QUERY_FOR_AM_ASSET_UNCAPITALIZE);
					PreparedStatement ps2 = con.prepareStatement(QUERY_FOR_AM_ASSET_ARCHIVE);
					PreparedStatement ps3 = con.prepareStatement(QUERY_FOR_INVOICENO)
			) {

				ps.setQueryTimeout(60);
				ps1.setQueryTimeout(60);
				ps2.setQueryTimeout(60);
				ps3.setQueryTimeout(60);

				ps.setString(1, groupId);
				int r1 = ps.executeUpdate();

				ps1.setString(1, groupId);
				int r2 = ps1.executeUpdate();

				ps2.setString(1, groupId);
				int r3 = ps2.executeUpdate();

				ps3.setString(1, invoiceNo);
				ps3.setDate(2, df.dateConvert(new java.util.Date()));
				ps3.setString(3, groupId);
				int r4 = ps3.executeUpdate();

				con.commit();

				done = (r1 >= 0 && r2 >= 0 && r3 >= 0 && r4 >= 0);

			} catch (Exception e) {
				con.rollback(); // 🔥 CRITICAL
				throw e;
			}

		} catch (Exception ex) {
			System.err.println("ERROR: APIgroupRecordProcess failed -> " + ex.getMessage());
		}

		return done;
	}
}
