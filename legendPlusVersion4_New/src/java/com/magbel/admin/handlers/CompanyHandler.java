package com.magbel.admin.handlers;

import com.magbel.admin.objects.Company;
import com.magbel.admin.objects.Locations;
import com.magbel.util.DataConnect;
import com.magbel.util.DatetimeFormat;
import com.magbel.util.ApplicationHelper;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CompanyHandler {

	private final SimpleDateFormat sdf;
	private final DatetimeFormat df;

	public CompanyHandler() {
		this.sdf = new SimpleDateFormat("dd-MM-yyyy");
		this.df = new DatetimeFormat();
	}

	private Connection getConnection() throws Exception {
		DataConnect dc = new DataConnect("helpDesk");
		return dc.getConnection();
	}

	public Company getCompany() {
		String query = "SELECT Company_Code, Company_Name, Acronym, Company_Address, Minimum_Psw, Psw_Expiry, Session_Timeout, User_Id, Trans_Wait_Time, loguseraudit, Psw_upper, Psw_lower, NumericPass, SpecPassword, Pass_Limit, attempted_login FROM AM_GB_COMPANY";
		try (Connection c = getConnection();
			 Statement s = c.createStatement();
			 ResultSet rs = s.executeQuery(query)) {

			if (rs.next()) {
				Company company = new Company();
				company.setCompanyName(rs.getString("Company_Name"));
				company.setCompanyCode(rs.getString("Company_Code"));
				company.setAcronym(rs.getString("Acronym"));
				company.setCompanyAddress(rs.getString("Company_Address"));
				company.setMinimumPassword(rs.getInt("Minimum_Psw"));
				company.setPasswordExpiry(rs.getInt("Psw_Expiry"));
				company.setSessionTimeout(rs.getInt("Session_Timeout"));
				company.setUserId(rs.getString("User_Id"));
				company.setTransWaitTime(rs.getDouble("Trans_Wait_Time"));
				company.setLogUserAudit(rs.getString("loguseraudit"));
				company.setPassword_upper(rs.getString("Psw_upper"));
				company.setPassword_lower(rs.getString("Psw_lower"));
				company.setPassword_numeric(rs.getString("NumericPass"));
				company.setPassword_special(rs.getString("SpecPassword"));
				company.setPasswordLimit(rs.getInt("Pass_Limit"));
				company.setLog_on(rs.getInt("attempted_login"));
				return company;
			}
		} catch (Exception e) {
			// Use a proper logging framework here
			System.err.println("Error retrieving company: " + e.getMessage());
		}
		return null;
	}

	public boolean createCompany(Company company) {
		String query = "INSERT INTO AM_GB_COMPANY(Company_Code, Company_Name, Acronym, Company_Address, Minimum_Psw, Psw_Expiry, Session_Timeout, User_Id, loguseraudit, Attempt_Logon, Pass_Limit, Psw_upper, Psw_lower, NumericPass, SpecPassword) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try (Connection con = getConnection();
			 PreparedStatement ps = con.prepareStatement(query)) {

			ps.setString(1, company.getCompanyCode());
			ps.setString(2, company.getCompanyName());
			ps.setString(3, company.getAcronym());
			ps.setString(4, company.getCompanyAddress());
			ps.setInt(5, company.getMinimumPassword());
			ps.setInt(6, company.getPasswordExpiry());
			ps.setInt(7, company.getSessionTimeout());
			ps.setString(8, company.getUserId());
			ps.setString(9, company.getLogUserAudit());
			ps.setInt(10, company.getLog_on());
			ps.setInt(11, company.getPasswordLimit());
			ps.setString(12, company.getPassword_upper());
			ps.setString(13, company.getPassword_lower());
			ps.setString(14, company.getPassword_numeric());
			ps.setString(15, company.getPassword_special());

			return ps.executeUpdate() != -1;
		} catch (Exception e) {
			System.err.println("Error creating company: " + e.getMessage());
			return false;
		}
	}

	public boolean updateCompany(Company company) {
		String query = "UPDATE AM_GB_COMPANY SET Company_Name = ?, Acronym = ?, Company_Address = ?, Minimum_Psw = ?, Psw_Expiry = ?, Session_Timeout = ?, loguseraudit = ?, attempted_login = ?, Psw_upper = ?, Psw_lower = ?, NumericPass = ?, SpecPassword = ?, Pass_Limit = ?, user_id = ? WHERE Company_Code = ?";
		try (Connection con = getConnection();
			 PreparedStatement ps = con.prepareStatement(query)) {

			ps.setString(1, company.getCompanyName());
			ps.setString(2, company.getAcronym());
			ps.setString(3, company.getCompanyAddress());
			ps.setInt(4, company.getMinimumPassword());
			ps.setInt(5, company.getPasswordExpiry());
			ps.setInt(6, company.getSessionTimeout());
			ps.setString(7, company.getLogUserAudit());
			ps.setInt(8, company.getLog_on());
			ps.setString(9, company.getPassword_upper());
			ps.setString(10, company.getPassword_lower());
			ps.setString(11, company.getPassword_numeric());
			ps.setString(12, company.getPassword_special());
			ps.setInt(13, company.getPasswordLimit());
			ps.setString(14, company.getUserId());
			ps.setString(15, company.getCompanyCode());

			return ps.executeUpdate() != -1;
		} catch (Exception e) {
			System.err.println("Error updating company: " + e.getMessage());
			return false;
		}
	}

	public Company getCompanyFed1() {
		String query = "SELECT Company_Code, Company_Name, Acronym, Company_Address, Vat_Rate, Wht_Rate, Financial_Start_Date, Financial_No_OfMonths, Financial_End_Date, Minimum_Psw, Psw_Expiry, Session_Timeout, Enforce_Acq_Budget, Enforce_Pm_Budget, Enforce_Fuel_Allocation, Require_Quarterly_Pm, Quarterly_Surplus_Cf, User_Id, Processing_Status, Trans_Wait_Time, loguseraudit, Fed_Wht_Rate, Attempt_Logon, component_delimiter, Psw_upper, Psw_lower, NumericPass, SpecPassword, Pass_Limit FROM AM_GB_COMPANY";
		try (Connection c = getConnection();
			 Statement s = c.createStatement();
			 ResultSet rs = s.executeQuery(query)) {

			if (rs.next()) {
				Company company = new Company(
						rs.getString("Company_Code"),
						rs.getString("Company_Name"),
						rs.getString("Acronym"),
						rs.getString("Company_Address"),
						rs.getDouble("Vat_Rate"),
						rs.getDouble("Wht_Rate"),
						rs.getDouble("Fed_Wht_Rate"),
						sdf.format(rs.getDate("Financial_Start_Date")),
						rs.getInt("Financial_No_OfMonths"),
						sdf.format(rs.getDate("Financial_End_Date")),
						rs.getInt("Minimum_Psw"),
						rs.getInt("Psw_Expiry"),
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
				company.setComp_delimiter(rs.getString("component_delimiter") != null ? rs.getString("component_delimiter") : "");
				company.setPassword_upper(rs.getString("Psw_upper"));
				company.setPassword_lower(rs.getString("Psw_lower"));
				company.setPassword_numeric(rs.getString("NumericPass"));
				company.setPassword_special(rs.getString("SpecPassword"));
				company.setPasswordLimit(rs.getInt("Pass_Limit"));
				return company;
			}
		} catch (Exception e) {
			System.err.println("Error retrieving company (Fed1): " + e.getMessage());
		}
		return null;
	}

	public Locations getLocationByLocID(String locID) {
		String query = "SELECT Location_Id, Location_Code, Location, Status, User_Id, Create_Date FROM AM_GB_LOCATION WHERE Location_Id = ?";
		try (Connection c = getConnection();
			 PreparedStatement ps = c.prepareStatement(query)) {

			ps.setString(1, locID);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					Locations location = new Locations();
					location.setLocationId(rs.getString("Location_Id"));
					location.setLocationCode(rs.getString("Location_Code"));
					location.setLocation(rs.getString("Location"));
					location.setStatus(rs.getString("Status"));
					location.setUserId(rs.getString("User_Id"));
					location.setCreateDate(rs.getString("Create_Date"));
					return location;
				}
			}
		} catch (Exception e) {
			System.err.println("Error retrieving location by ID: " + e.getMessage());
		}
		return null;
	}

	public boolean createLocation(Locations location) {
		String query = "INSERT INTO AM_GB_LOCATION(Location_Code, Location, Status, User_Id, Create_Date, Location_Id) VALUES (?,?,?,?,?,?)";
		try (Connection con = getConnection();
			 PreparedStatement ps = con.prepareStatement(query)) {

			int mtid = Integer.parseInt(new ApplicationHelper().getGeneratedId("AM_GB_LOCATION"));
			ps.setString(1, location.getLocationCode());
			ps.setString(2, location.getLocation());
			ps.setString(3, location.getStatus());
			ps.setString(4, location.getUserId());
			ps.setDate(5, df.dateConvert(new Date()));
			ps.setInt(6, mtid);

			return ps.executeUpdate() != -1;
		} catch (Exception e) {
			System.err.println("Error creating location: " + e.getMessage());
			return false;
		}
	}

	public boolean updateLocation(Locations location) {
		String query = "UPDATE AM_GB_LOCATION SET Location_Code = ?, Location = ?, Status = ? WHERE Location_Id = ?";
		try (Connection con = getConnection();
			 PreparedStatement ps = con.prepareStatement(query)) {

			ps.setString(1, location.getLocationCode());
			ps.setString(2, location.getLocation());
			ps.setString(3, location.getStatus());
			ps.setString(4, location.getLocationId());

			return ps.executeUpdate() != -1;
		} catch (Exception e) {
			System.err.println("Error updating location: " + e.getMessage());
			return false;
		}
	}

	public Locations getLocationByLocCode(String locCode) {
		String query = "SELECT Location_Id, Location_Code, Location, Status, User_Id, Create_Date FROM AM_GB_LOCATION WHERE Location_Code = ?";
		try (Connection c = getConnection();
			 PreparedStatement ps = c.prepareStatement(query)) {

			ps.setString(1, locCode);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					Locations location = new Locations();
					location.setLocationId(rs.getString("Location_Id"));
					location.setLocationCode(rs.getString("Location_Code"));
					location.setLocation(rs.getString("Location"));
					location.setStatus(rs.getString("Status"));
					location.setUserId(rs.getString("User_Id"));
					location.setCreateDate(rs.getString("Create_Date"));
					return location;
				}
			}
		} catch (Exception e) {
			System.err.println("Error retrieving location by code: " + e.getMessage());
		}
		return null;
	}

	public ArrayList<Locations> getLocationByQuery(String filter) {
		ArrayList<Locations> list = new ArrayList<>();
		String query = "SELECT Location_Id, Location_Code, Location, Status, User_Id, Create_Date FROM AM_GB_LOCATION WHERE Location_Id IS NOT NULL " + filter;
		try (Connection c = getConnection();
			 Statement s = c.createStatement();
			 ResultSet rs = s.executeQuery(query)) {

			while (rs.next()) {
				Locations location = new Locations();
				location.setLocationId(rs.getString("Location_Id"));
				location.setLocationCode(rs.getString("Location_Code"));
				location.setLocation(rs.getString("Location"));
				location.setStatus(rs.getString("Status"));
				location.setUserId(rs.getString("User_Id"));
				location.setCreateDate(rs.getString("Create_Date"));
				list.add(location);
			}
		} catch (Exception e) {
			System.err.println("Error retrieving locations by query: " + e.getMessage());
		}
		return list;
	}
}
