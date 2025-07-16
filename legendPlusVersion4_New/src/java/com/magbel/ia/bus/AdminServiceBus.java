/**
 *
 */
package com.magbel.ia.bus;

import com.magbel.ia.dao.ConnectManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import com.magbel.ia.util.ApplicationHelper;
import com.magbel.ia.vao.Branch;
import com.magbel.ia.vao.BranchDept;
import com.magbel.ia.vao.Camp;
import com.magbel.ia.vao.Company;
import com.magbel.ia.vao.Country;
import com.magbel.ia.vao.CurrencyCode;
import com.magbel.ia.vao.Department;
import com.magbel.ia.vao.DeptSection;
import com.magbel.ia.vao.ExchangeRate;
import com.magbel.ia.vao.NoteMapping;
import com.magbel.ia.vao.Project;
import com.magbel.ia.vao.Province;
import com.magbel.ia.vao.Region;
import com.magbel.ia.vao.Section;
import com.magbel.ia.vao.State;
import com.magbel.ia.vao.Suffix;
import com.magbel.ia.vao.SystemTransactionCode;
import com.magbel.ia.vao.UserTransactionCode;
import com.magbel.ia.dao.PersistenceServiceDAO;

import java.util.ArrayList;
import java.util.Vector;

/**
 * 2008_IAS
 * @author Bolanle M. Sule
 * @version 1.0
 */
public class
//ConnectManager

AdminServiceBus extends PersistenceServiceDAO {

	/**
	 * @Entities Department,Branch,Section,States,Company
	 *           Region,Province,Locations,Currency, Exchange Rate
	 */

	SimpleDateFormat sdf;

	final String space = "";

	final String comma = ",";

	java.util.Date date;

	com.magbel.util.DatetimeFormat df;

	public AdminServiceBus() {

		sdf = new SimpleDateFormat("dd-MM-yyyy");
		df = new com.magbel.util.DatetimeFormat();
		//System.out.println("USING_ " + this.getClass().getName());
	}

	/**
	 *
	 * @param branch
	 * @return
	 */
	public boolean createBranch(com.magbel.ia.vao.Branch branch) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "INSERT INTO MG_AD_Branch(BRANCH_CODE,BRANCH_NAME"
				+ ",BRANCH_ACRONYM,GL_PREFIX,BRANCH_ADDRESS"
				+ " ,STATE,PHONE_NO,FAX_NO,REGION,PROVINCE"
				+ " ,BRANCH_STATUS,USER_ID,CREATE_DATE,GL_SUFFIX,COMPANY_CODE,BRANCH_ID) "
				+ " VALUES(?,?,? ,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, branch.getBranchCode());
			ps.setString(2, branch.getBranchName());
			ps.setString(3, branch.getBranchAcronym());
			ps.setString(4, branch.getGlPrefix());
			ps.setString(5, branch.getBranchAddress());
			ps.setString(6, branch.getState());
			ps.setString(7, branch.getPhoneNo());
			ps.setString(8, branch.getFaxNo());
			ps.setString(9, branch.getRegion());
			ps.setString(10, branch.getProvince());
			ps.setString(11, branch.getBranchStatus());
			ps.setString(12, branch.getUsername());
			ps.setDate(13, df.dateConvert(new java.util.Date()));
			ps.setString(14, branch.getGlSuffix());
			ps.setString(15, branch.getCompanyCode());
			ps.setString(16, new ApplicationHelper()
					.getGeneratedId("MG_AD_Branch"));
			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " ERROR:Error Creating Branch ->" + e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

	/**
	 *
	 * @param dept
	 * @return
	 */
	public boolean createDepartment(com.magbel.ia.vao.Department dept) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "INSERT INTO mg_ad_department(Dept_code,Dept_name"
				+ " ,Dept_acronym,Dept_Status,user_id ,CREATE_DATE,COMPANY_CODE,Dept_ID)"
				+ " VALUES (?,?,?,?,?,?,?,?)";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, dept.getDept_code());
			ps.setString(2, dept.getDept_name());
			ps.setString(3, dept.getDept_acronym());
			ps.setString(4, dept.getDept_status());
			ps.setString(5, dept.getUser_id());
			ps.setDate(6, df.dateConvert(new java.util.Date()));
			ps.setString(7, dept.getCompanyCode());
			ps.setString(8, new ApplicationHelper()
					.getGeneratedId("mg_ad_department"));
			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " ERROR:Error creating Department ->" + e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

	/**
	 *
	 * @param section
	 * @return
	 */
	public boolean createSection(com.magbel.ia.vao.Section section) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "INSERT INTO MG_AD_SECTION(Section_ID,Section_Code,Section_Name"
				+ "  ,section_acronym,Section_Status,User_ID,Create_Date,COMPANY_CODE)"
				+ "  VALUES (?,?,?,?,?,?,?,?)";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, new ApplicationHelper()
					.getGeneratedId("MG_AD_SECTION"));
			ps.setString(2, section.getSection_code());
			ps.setString(3, section.getSection_name());
			ps.setString(4, section.getSection_acronym());
			ps.setString(5, section.getSection_status());
			ps.setString(6, section.getUserid());
			ps.setDate(7, df.dateConvert(new java.util.Date()));
			ps.setString(8, section.getCompanyCode());

			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " ERROR:Error creating Section ->" + e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}
	
	public boolean createProject(com.magbel.ia.vao.Project project) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "INSERT INTO IA_GL_PROJECT(MTID,CODE,DESCRIPTION"
				+ "  ,START_DATE,END_DATE,COST,CAPITAL,OTHER,TRANS_DT,STATUS)"
				+ "  VALUES (?,?,?,?,?,?,?,?,?,?)";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, new ApplicationHelper().getGeneratedId("IA_GL_PROJECT"));
			ps.setString(2, project.getCode());
			ps.setString(3, project.getDesc());
			ps.setDate(4, df.dateConvert(project.getStartDt()));
			ps.setDate(5, df.dateConvert(project.getEndDt()));
			String stroldAmount = project.getCost();
	        if(stroldAmount==""){stroldAmount = "0";}
	        stroldAmount = stroldAmount.replaceAll(",", "");
	        double Cost = stroldAmount != null ? Double.parseDouble(stroldAmount) : 0.0D;
			ps.setDouble(6, Cost);
			ps.setString(7, project.getCapital());
			ps.setString(8, project.getOther());
			ps.setString(9, project.getTransDt());
			ps.setString(10, project.getStatus());
			
			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " ERROR:Error creating Project ->" + e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}
	
	public boolean createCountry(com.magbel.ia.vao.Country country) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "INSERT INTO IA_COUNTRY(COUNTRY_ID,CODE"
				+ "  ,DESCRIPTION,HOME_COUNTRY,STATUS,USERID,CREATE_DATE,EFFECTIVE_DATE)"
				+ "   VALUES (?,?,?,?,?,?,?,?)";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, new ApplicationHelper()
					.getGeneratedId("IA_COUNTRY"));
			ps.setString(2, country.getCode());
			ps.setString(3, country.getDescription());
			ps.setString(4, country.getHomeCountry());
			ps.setString(5, country.getStatus());
			ps.setString(6, country.getUserId());
			ps.setDate(7, df.dateConvert(new java.util.Date()));
			ps.setDate(8, df.dateConvert(new java.util.Date()));
			
			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println("WARNING:Error creating Country.... ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

	
	public boolean createState(com.magbel.ia.vao.State state) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "INSERT INTO MG_GB_STATE(state_code,state_name"
				+ "  ,state_status,user_id,create_date,country_code,state_id)"
				+ "   VALUES (?,?,?,?,?,?,?)";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, state.getStateCode());
			ps.setString(2, state.getStateName());
			ps.setString(3, state.getStateStatus());
			ps.setString(4, state.getUserId());
			ps.setDate(5, df.dateConvert(new java.util.Date()));
			ps.setString(6, state.getCountryCode());
			ps.setString(7, new ApplicationHelper()
					.getGeneratedId("MG_GB_STATE"));
			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println("WARNING:Error creating State ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

	public boolean createRegion(com.magbel.ia.vao.Region region) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "INSERT INTO MG_AD_REGION( Region_Code, Region_Name"
				+ ", Region_Acronym, Region_Address"
				+ ",Region_Phone , Region_Fax, Region_Status, User_Id,Create_Date,Region_iD) "
				+ " VALUES (?,?,?,?,?,?,?,?,?,?)";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			// ps.setLong(1, System.currentTimeMillis());
			ps.setString(1, region.getRegionCode());
			ps.setString(2, region.getRegionName());
			ps.setString(3, region.getRegionAcronym());
			ps.setString(4, region.getRegionAddress());
			ps.setString(5, region.getRegionPhone());
			ps.setString(6, region.getRegionFax());
			ps.setString(7, region.getRegionStatus());
			ps.setString(8, region.getUserId());
			ps.setDate(9, df.dateConvert(new java.util.Date()));
			ps.setString(10, new ApplicationHelper()
					.getGeneratedId("MG_AD_REGION"));

			done = (ps.executeUpdate() != -1);
		} catch (Exception e) {
			System.out.println("WARNING:Error creating Region ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

	public boolean createProvince(com.magbel.ia.vao.Province ccode) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "INSERT INTO MG_GB_Province"
				+ "(Province_Code, Province"
				+ ",Status, User_id, create_date,Province_ID)"
				+ " VALUES (?,?,?,?,?,?)";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, ccode.getProvinceCode());
			ps.setString(2, ccode.getProvince());
			ps.setString(3, ccode.getStatus());
			ps.setString(4, ccode.getUserId());
			ps.setDate(5, df.dateConvert(ccode.getCreateDate()));
			ps.setString(6, new ApplicationHelper()
					.getGeneratedId("MG_GB_Province"));
			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println("WARNING:Error creating Province ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}
	
	public boolean createSuffix(com.magbel.ia.vao.Suffix suffix) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "INSERT INTO MG_GB_SUFFIX(Suffix_Code,description"
				+ "  ,Charge_Code,Status,Create_date,Suffix_Id)"
				+ "   VALUES (?,?,?,?,?,?)";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, suffix.getSuffixCode());
			ps.setString(2, suffix.getDescription());
			ps.setString(3, suffix.getChargeCode());
			ps.setString(4, suffix.getStatus());
			ps.setDate(5, df.dateConvert(new java.util.Date()));
			ps.setString(6, new ApplicationHelper()
					.getGeneratedId("MG_GB_SUFFIX"));
			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println("WARNING:Error creating Suffix ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}
	
	public boolean createCamp(com.magbel.ia.vao.Camp cm) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "INSERT INTO MG_CAMP( code, id, status)"
				+ " VALUES (?,?,?)";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			// ps.setLong(1, System.currentTimeMillis());
			ps.setString(1, cm.getCode());
			ps.setString(2, new ApplicationHelper()
					.getGeneratedId("MG_CAMP"));
			ps.setString(3, cm.getStatus());

			done = (ps.executeUpdate() != -1);
		} catch (Exception e) {
			System.out.println("WARNING:Error creating Camp ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}
	
	
	public boolean createSystemTransactionCode(com.magbel.ia.vao.SystemTransactionCode stc) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "INSERT INTO IA_SYSTEM_TRANSACTION_CODE(MTID,CODE"
				+ "  ,DESCRIPTION,Dr_Cr,Create_date,STATUS,Cot_Charge)"
				+ "   VALUES (?,?,?,?,?,?,?)";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, new ApplicationHelper()
					.getGeneratedId("IA_SYSTEM_TRANSACTION_CODE"));
			ps.setString(2, stc.getCode());
			ps.setString(3, stc.getDescription());
			ps.setString(4, stc.getDrCr());
			ps.setDate(5, df.dateConvert(new java.util.Date()));

			ps.setString(6, stc.getStatus());
			ps.setString(7, stc.getCotCharge());
			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println("WARNING:Error creating SystemTransactionCode ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}
	
	
	public boolean createUserTransactionCode(com.magbel.ia.vao.UserTransactionCode utc) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "INSERT INTO IA_USER_TRANSACTION_CODE(MTID,USER_TRAN_CODE"
				+ "  ,DESCRIPTION,Create_date,STATUS,CODE,USER_ID,COMPANY_CODE)"
				+ "   VALUES (?,?,?,?,?,?,?,?)";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, new ApplicationHelper()
					.getGeneratedId("IA_USER_TRANSACTION_CODE"));
			ps.setString(2, utc.getUserTranCode());
			ps.setString(3, utc.getDescription());
			ps.setDate(4, df.dateConvert(new java.util.Date()));
			ps.setString(5, utc.getStatus());
			ps.setString(6, utc.getCode());
			ps.setString(7, utc.getUserId());
			ps.setString(8, utc.getCompanyCode());
			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println("WARNING:Error creating UserTransactionCode ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}
	
	

	public boolean updateBranch(com.magbel.ia.vao.Branch branch) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "UPDATE MG_AD_Branch SET BRANCH_CODE =?,BRANCH_NAME = ?,BRANCH_ACRONYM = ?"
				+ " ,GL_PREFIX = ?,BRANCH_ADDRESS = ?,STATE = ?"
				+ " ,PHONE_NO = ?,FAX_NO = ?,REGION = ?,PROVINCE = ?"
				+ " ,BRANCH_STATUS = ?,GL_SUFFIX = ?,COMPANY_CODE=?" + " WHERE BRANCH_ID =?";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, branch.getBranchCode());
			ps.setString(2, branch.getBranchName());
			ps.setString(3, branch.getBranchAcronym());
			ps.setString(4, branch.getGlPrefix());
			ps.setString(5, branch.getBranchAddress());
			ps.setString(6, branch.getState());
			ps.setString(7, branch.getPhoneNo());
			ps.setString(8, branch.getFaxNo());
			ps.setString(9, branch.getRegion());
			ps.setString(10, branch.getProvince());
			ps.setString(11, branch.getBranchStatus());
			ps.setString(12, branch.getGlSuffix());
			ps.setString(13, branch.getCompanyCode());
			ps.setString(14, branch.getBranchId());
			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

	public boolean updateDepartment(com.magbel.ia.vao.Department dept) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "UPDATE mg_ad_department SET Dept_code = ?,Dept_name = ?"
				+ " ,Dept_acronym = ?,Dept_Status = ?,COMPANY_CODE=? " + "  WHERE Dept_ID=?";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, dept.getDept_code());
			ps.setString(2, dept.getDept_name());
			ps.setString(3, dept.getDept_acronym());
			ps.setString(4, dept.getDept_status());
			ps.setString(5, dept.getCompanyCode());
			ps.setString(6, dept.getDept_id());

			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " ERROR:Error Updating Query ->" + e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

	public boolean updateSection(com.magbel.ia.vao.Section section) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "UPDATE MG_AD_SECTION SET Section_Code = ?,Section_Name = ?"
				+ "  ,section_acronym = ?,Section_Status = ?,COMPANY_CODE=? "
				+ "  WHERE Section_ID = ?";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, section.getSection_code());
			ps.setString(2, section.getSection_name());
			ps.setString(3, section.getSection_acronym());
			ps.setString(4, section.getSection_status());
			ps.setString(5, section.getCompanyCode());
			ps.setString(6, section.getSection_id());

			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " ERROR:Error Updating Section ->" + e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}
	
		public boolean updateProject(com.magbel.ia.vao.Project project) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "UPDATE IA_GL_PROJECT SET CODE = ?,DESCRIPTION = ?,START_DATE=?"
				+ "  ,END_DATE = ?,COST = ?,CAPITAL=?,OTHER=?,TRANS_DT =?,STATUS = ? WHERE MTID = ?";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, project.getCode());
			ps.setString(2, project.getDesc());
			ps.setDate(3, df.dateConvert(project.getStartDt()));
			ps.setDate(4, df.dateConvert(project.getEndDt()));
			String stroldAmount = project.getCost();
	        if(stroldAmount==""){stroldAmount = "0";}
	        stroldAmount = stroldAmount.replaceAll(",", "");
	        double Cost = stroldAmount != null ? Double.parseDouble(stroldAmount) : 0.0D;
			ps.setDouble(5, Cost);
			ps.setString(6, project.getCapital());
			ps.setString(7, project.getOther());
			ps.setString(8, project.getTransDt());
			ps.setString(9, project.getStatus());
			ps.setString(10, project.getId());

			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " ERROR:Error Updating Project ->" + e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

	public boolean updateState(com.magbel.ia.vao.State state) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "UPDATE MG_GB_STATE SET state_code = ?,state_name = ?"
				+ "   ,state_status = ?,country_code = ?" + "   WHERE state_ID=?";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, state.getStateCode());
			ps.setString(2, state.getStateName());
			ps.setString(3, state.getStateStatus());
			ps.setString(4, state.getCountryCode());
			ps.setString(5, state.getStateId());

			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " ERROR:Error Updating State ->" + e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}
	
	public boolean updateCountry(com.magbel.ia.vao.Country country) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "UPDATE IA_COUNTRY SET code = ?,description = ?"
				+ "   ,HOME_COUNTRY = ?,status = ?,EFFECTIVE_DATE=?" + "   WHERE COUNTRY_ID=?";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, country.getCode());
			ps.setString(2, country.getDescription());
			ps.setString(3, country.getHomeCountry());
			ps.setString(4, country.getStatus());
			ps.setString(5, country.getEffectiveDate());
			ps.setString(6, country.getCId());

			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " ERROR:Error Updating Country .... ->" + e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}


	public boolean updateRegion(com.magbel.ia.vao.Region region) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "UPDATE MG_AD_REGION"
				+ " SET Region_Name = ?, Region_Acronym = ?"
				+ ",Region_Address = ?,Region_Phone = ?, Region_Fax = ?"
				+ ",Region_Status = ?, User_Id = ?,Region_Code = ?"
				// + ",Create_Date = ?"
				+ " WHERE Region_ID = ?";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, region.getRegionName());
			ps.setString(2, region.getRegionAcronym());
			ps.setString(3, region.getRegionAddress());
			ps.setString(4, region.getRegionPhone());
			ps.setString(5, region.getRegionFax());
			// ps.setDate(6,df.dateConvert(new java.util.Date()));
			ps.setString(6, region.getRegionStatus());
			ps.setString(7, region.getUserId());
			ps.setString(8, region.getRegionCode());
			ps.setString(9, region.getRegionId());
			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}
	
	

	public boolean updateProvince(com.magbel.ia.vao.Province ccode) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "UPDATE MG_GB_Province" + " SET Province_Code = ?"
				+ ",Province = ?,Status = ?" + "  WHERE Province_ID = ?";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			// ps.setString(1, ccode.getProvinceId());
			ps.setString(1, ccode.getProvinceCode());
			ps.setString(2, ccode.getProvince());
			ps.setString(3, ccode.getStatus());
			ps.setString(4, ccode.getProvinceId());
			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println("WARNING:Error Updating Province ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}
	
	
	public boolean updateSuffix(com.magbel.ia.vao.Suffix suffix) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "UPDATE MG_GB_SUFFIX SET Suffix_Code = ?, description = ?"
				+ "   ,status = ?, Charge_Code=? " + "   WHERE suffix_ID=?";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, suffix.getSuffixCode());
			ps.setString(2, suffix.getDescription());
			ps.setString(3, suffix.getStatus());
			ps.setString(4, suffix.getChargeCode());
			ps.setString(5, suffix.getSuffixId());

			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " ERROR:Error Updating Suffix ->" + e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}
	
	public boolean updateCamp(com.magbel.ia.vao.Camp cm) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "UPDATE MG_CAMP"
				+ " SET code = ?, STATUS=? "
				+ " WHERE ID = ?";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, cm.getCode());
			ps.setString(2, cm.getStatus());
			ps.setString(3, cm.getId());
			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query(updateCamp) ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

	
	
	public boolean updateSystemTransactionCode(com.magbel.ia.vao.SystemTransactionCode stc) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "UPDATE IA_SYSTEM_TRANSACTION_CODE SET Code = ?, description = ?"
				+ "  , Dr_Cr=?, status = ?, Cot_Charge=? " + "   WHERE MTID=?";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, stc.getCode());
			ps.setString(2, stc.getDescription());
			ps.setString(3, stc.getDrCr());
			ps.setString(4, stc.getStatus());
			ps.setString(5, stc.getCotCharge());
			ps.setString(6, stc.getSysId());

			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " ERROR:Error Updating SystemTransactionCode ->" + e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}
	
	
	public boolean updateUserTransactionCode(com.magbel.ia.vao.UserTransactionCode utc) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "UPDATE IA_USER_TRANSACTION_CODE SET User_Tran_Code = ?, description = ?"
				+ "  , status = ?, Code=? ,COMPANY_CODE = ? " + "   WHERE MTID=?";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, utc.getUserTranCode());
			ps.setString(2, utc.getDescription());
			ps.setString(3, utc.getStatus());
			ps.setString(4, utc.getCode());
			ps.setString(5, utc.getCompanyCode());
			ps.setString(6, utc.getUId());

			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " ERROR:Error Updating UserTransactionCode ->" + e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}


	public java.util.List<Branch> findBranchesByQuery(String filter) {
		java.util.List<Branch> _list = new java.util.ArrayList<Branch>();
		com.magbel.ia.vao.Branch branch = null;
		String query = "SELECT BRANCH_ID,BRANCH_CODE"
				+ " ,BRANCH_NAME,BRANCH_ACRONYM,GL_PREFIX"
				+ ",BRANCH_ADDRESS,STATE,PHONE_NO" + ",FAX_NO,REGION,PROVINCE"
				+ ",BRANCH_STATUS,USER_ID,GL_SUFFIX"
				+ ",CREATE_DATE,COMPANY_CODE  FROM MG_AD_Branch ";

		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String branchId = rs.getString("BRANCH_ID");
				String branchCode = rs.getString("BRANCH_CODE");
				String branchName = rs.getString("BRANCH_NAME");
				String branchAcronym = rs.getString("BRANCH_ACRONYM");
				String glPrefix = rs.getString("GL_PREFIX");
				String branchAddress = rs.getString("BRANCH_ADDRESS");
				String state = rs.getString("STATE");
				String phoneNo = rs.getString("PHONE_NO");
				String faxNo = rs.getString("FAX_NO");
				String province = rs.getString("PROVINCE");
				String region = rs.getString("REGION");
				String branchStatus = rs.getString("BRANCH_STATUS");
				String username = rs.getString("USER_ID");
				String glSuffix = rs.getString("GL_SUFFIX");
				String createDate = rs.getString("CREATE_DATE");
				String companyCode = rs.getString("COMPANY_CODE");
				branch = new com.magbel.ia.vao.Branch(branchId, branchCode,
						branchName, branchAcronym, glPrefix, glSuffix,
						branchAddress, state, phoneNo, faxNo, region, province,
						branchStatus, username, createDate, companyCode);

				_list.add(branch);
			}

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " ERROR:Error Selecting branches ->" + e.getMessage());
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}

	private com.magbel.ia.vao.Branch findABranch(String filter) {
		com.magbel.ia.vao.Branch branch = null;
		String query = "SELECT BRANCH_ID,BRANCH_CODE"
				+ " ,BRANCH_NAME,BRANCH_ACRONYM,GL_PREFIX"
				+ ",BRANCH_ADDRESS,STATE,PHONE_NO" + ",FAX_NO,REGION,PROVINCE"
				+ ",BRANCH_STATUS,USER_ID,GL_SUFFIX"
				+ ",CREATE_DATE,COMPANY_CODE  FROM MG_AD_Branch ";

		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String branchId = rs.getString("BRANCH_ID");
				String branchCode = rs.getString("BRANCH_CODE");
				String branchName = rs.getString("BRANCH_NAME");
				String branchAcronym = rs.getString("BRANCH_ACRONYM");
				String glPrefix = rs.getString("GL_PREFIX");
				String branchAddress = rs.getString("BRANCH_ADDRESS");
				String state = rs.getString("STATE");
				String phoneNo = rs.getString("PHONE_NO");
				String faxNo = rs.getString("FAX_NO");
				String province = rs.getString("PROVINCE");
				String region = rs.getString("REGION");
				String branchStatus = rs.getString("BRANCH_STATUS");
				String username = rs.getString("USER_ID");
				String glSuffix = rs.getString("GL_SUFFIX");
				String createDate = rs.getString("CREATE_DATE");
				String companyCode = rs.getString("COMPANY_CODE");
				branch = new com.magbel.ia.vao.Branch(branchId, branchCode,
						branchName, branchAcronym, glPrefix, glSuffix,
						branchAddress, state, phoneNo, faxNo, region, province,
						branchStatus, username, createDate, companyCode);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return branch;

	}

	public com.magbel.ia.vao.Branch findBranchByBranchID(String branchid) {
		String filter = " WHERE BRANCH_ID = '" + branchid+"'";
		com.magbel.ia.vao.Branch branch = findABranch(filter);
		return branch;

	}

	public com.magbel.ia.vao.Branch findBranchByBranchCode(String branchcode) {
		String filter = " WHERE BRANCH_CODE = '" + branchcode + "'";
		com.magbel.ia.vao.Branch branch = findABranch(filter);
		return branch;

	}

	public java.util.List<Department> findDeparmentsByQuery(String filter) {
		java.util.List<Department> _list = new java.util.ArrayList<Department>();
		com.magbel.ia.vao.Department dept = null;
		String query = "SELECT Dept_ID,Dept_code,Dept_name"
				+ "  ,Dept_acronym,Dept_Status" + " ,user_id,CREATE_DATE,COMPANY_CODE"
				+ " FROM mg_ad_department ";

		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String dept_id = rs.getString("Dept_ID");
				String dept_code = rs.getString("Dept_code");
				String dept_name = rs.getString("Dept_name");
				String dept_acronym = rs.getString("Dept_acronym");
				String dept_status = rs.getString("Dept_Status");
				String user_id = rs.getString("user_id");
				String companyCode = rs.getString("COMPANY_CODE");
				dept = new com.magbel.ia.vao.Department(dept_id, dept_code,
						dept_name, dept_acronym, dept_status, user_id, companyCode);

				_list.add(dept);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}

	private com.magbel.ia.vao.Department findADepartment(String filter) {
		com.magbel.ia.vao.Department dept = null;
		String query = "SELECT Dept_ID,Dept_code,Dept_name"
				+ "  ,Dept_acronym,Dept_Status" + " ,user_id,CREATE_DATE,COMPANY_CODE"
				+ " FROM mg_ad_department ";

		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String dept_id = rs.getString("Dept_ID");
				String dept_code = rs.getString("Dept_code");
				String dept_name = rs.getString("Dept_name");
				String dept_acronym = rs.getString("Dept_acronym");
				String dept_status = rs.getString("Dept_Status");
				String user_id = rs.getString("user_id");
				String companyCode = rs.getString("COMPANY_CODE");
				dept = new com.magbel.ia.vao.Department(dept_id, dept_code,
						dept_name, dept_acronym, dept_status, user_id, companyCode);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return dept;

	}

	public com.magbel.ia.vao.Department findDeptByDeptID(String deptid) {
		String filter = " WHERE Dept_ID='" + deptid+"'";
		com.magbel.ia.vao.Department dept = findADepartment(filter);
		return dept;

	}

	public com.magbel.ia.vao.Department findDeptByDeptCode(String deptcode) {
		String filter = " WHERE Dept_Code='" + deptcode + "'";
		com.magbel.ia.vao.Department dept = findADepartment(filter);
		return dept;

	}
	

	public java.util.List<Section> findSectionByQuery(String filter) {
		java.util.List<Section> _list = new java.util.ArrayList<Section>();
		com.magbel.ia.vao.Section section = null;
		String query = "SELECT Section_ID,Section_Code,Section_Name"
				+ " ,section_acronym,Section_Status" + " ,user_id,CREATE_DATE,COMPANY_CODE"
				+ "  FROM MG_AD_SECTION ";

		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String section_id = rs.getString("Section_ID");
				String section_code = rs.getString("Section_Code");
				String section_acromyn = rs.getString("section_acronym");
				String section_name = rs.getString("Section_Name");
				String section_status = rs.getString("Section_Status");
				String userid = rs.getString("user_id");
				String companyCode = rs.getString("COMPANY_CODE");
				section = new com.magbel.ia.vao.Section(section_id,
						section_code, section_acromyn, section_name,
						section_status, userid, companyCode);

				_list.add(section);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}

	private com.magbel.ia.vao.Section findASection(String filter) {
		com.magbel.ia.vao.Section section = null;
		String query = "SELECT Section_ID,Section_Code,Section_Name"
				+ " ,section_acronym,Section_Status" + ",user_id,CREATE_DATE,COMPANY_CODE"
				+ "  FROM MG_AD_SECTION ";

		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String section_id = rs.getString("Section_ID");
				String section_code = rs.getString("Section_Code");
				String section_acromyn = rs.getString("section_acronym");
				String section_name = rs.getString("Section_Name");
				String section_status = rs.getString("Section_Status");
				String userid = rs.getString("user_id");
				String companyCode = rs.getString("COMPANY_CODE");
				section = new com.magbel.ia.vao.Section(section_id,
						section_code, section_acromyn, section_name,
						section_status, userid, companyCode);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return section;

	}

	public com.magbel.ia.vao.Section findSectionByID(String sectionid) {
		String filter = " WHERE Section_ID='" + sectionid+"'";
		com.magbel.ia.vao.Section section = findASection(filter);
		return section;

	}

	public com.magbel.ia.vao.Section findSectionByCode(String sectioncode) {
		String filter = " WHERE Section_Code='" + sectioncode + "'";
		com.magbel.ia.vao.Section section = findASection(filter);
		return section;

	}
	
	public java.util.List<Project> findProjectsByQuery(String filter) {
		java.util.List<Project> _list = new java.util.ArrayList<Project>();
		com.magbel.ia.vao.Project project = null;
		String query = "SELECT MTID, CODE, DESCRIPTION, START_DATE, END_DATE, COST, CAPITAL,  "+
                      "OTHER ,TRANS_DT" + "  ,STATUS FROM IA_GL_PROJECT"; 
		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String id = rs.getString("MTID");
				String code = rs.getString("CODE");
				String desc = rs.getString("DESCRIPTION");
				String startDt = sdf.format(rs.getDate("START_DATE"));
				String endDt = sdf.format(rs.getDate("END_DATE"));
				//double cost = rs.getDouble("COST");
				String cost = String.valueOf(rs.getDouble("COST"));
				String capital = rs.getString("CAPITAL");
				String other = rs.getString("OTHER");
				String transDt = rs.getString("TRANS_DT");
				String status = rs.getString("STATUS");

	project = new com.magbel.ia.vao.Project(id,code,desc,"",startDt,endDt,cost,capital,other,transDt,status);

				_list.add(project);
			}

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " ERROR:Error Selecting projects ->" + e.getMessage());
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}

	private com.magbel.ia.vao.Project findAProject(String filter) {
		com.magbel.ia.vao.Project project = null;
		String query = "SELECT MTID, CODE, DESCRIPTION, START_DATE, END_DATE, COST, CAPITAL,  "+
                      "OTHER ,TRANS_DT" + "  ,STATUS FROM IA_GL_PROJECT";

		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String id = rs.getString("MTID");
				String code = rs.getString("CODE");
				String desc = rs.getString("DESCRIPTION");
				String startDt = rs.getString("START_DATE");
				String endDt = rs.getString("END_DATE");
				//double cost = rs.getDouble("COST");
				String cost = String.valueOf(rs.getDouble("COST"));
				String capital = rs.getString("CAPITAL");
				String other = rs.getString("OTHER");
				String transDt = rs.getString("TRANS_DT");
				String status = rs.getString("STATUS");
				
	project = new com.magbel.ia.vao.Project(id,code,desc,"",startDt,endDt,cost,capital,other,transDt,status);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return project;

	}

	public com.magbel.ia.vao.Project findProjectByID(String id) {
		String filter = " WHERE MTID = '" +id+"'";
		com.magbel.ia.vao.Project project = findAProject(filter);
		return project;
	}

	public com.magbel.ia.vao.Project findProjectByProjectCode(String code) {
		String filter = " WHERE CODE = '" + code + "'";
		com.magbel.ia.vao.Project project = findAProject(filter);
		return project;

	}

	public java.util.List<State> findStatesByQuery(String filter) {
		java.util.List<State> _list = new java.util.ArrayList<State>();
		com.magbel.ia.vao.State state = null;
		String query = "SELECT state_ID,state_code,state_name"
				+ "  ,state_status,user_id,create_date,country_code" + " FROM MG_GB_STATE ";

		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String stateId = rs.getString("state_ID");
				String stateCode = rs.getString("state_code");
				String stateName = rs.getString("state_name");
				String stateStatus = rs.getString("state_status");
				String userId = rs.getString("user_id");
				String createDate = rs.getString("create_date");
				String countryCode = rs.getString("country_code");
				state = new com.magbel.ia.vao.State(stateId, stateCode,
						stateName, stateStatus, userId, createDate, countryCode);

				_list.add(state);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}

	private com.magbel.ia.vao.State findAState(String filter) {
		// java.util.ArrayList _list = new java.util.ArrayList();
		com.magbel.ia.vao.State state = null;
		String query = "SELECT state_ID,state_code,state_name"
				+ "  ,state_status,user_id,create_date,country_code" + " FROM MG_GB_STATE ";

		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String stateId = rs.getString("state_ID");
				String stateCode = rs.getString("state_code");
				String stateName = rs.getString("state_name");
				String stateStatus = rs.getString("state_status");
				String userId = rs.getString("user_id");
				String createDate = rs.getString("create_date");
				String countryCode = rs.getString("country_code");
				state = new com.magbel.ia.vao.State(stateId, stateCode,
						stateName, stateStatus, userId, createDate, countryCode);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return state;

	}

	public com.magbel.ia.vao.State findStateByID(String stateid) {
		String filter = " WHERE state_ID='" + stateid+"'";
		com.magbel.ia.vao.State state = findAState(filter);
		return state;

	}
	
	public com.magbel.ia.vao.State findStateByCode(String statecode) {
		String filter = " WHERE state_code='" + statecode + "'";
		com.magbel.ia.vao.State state = findAState(filter);
		return state;

	}


	private com.magbel.ia.vao.Region findARegion(String filter) {
		com.magbel.ia.vao.Region region = null;
		String query = "SELECT Region_Id, Region_Code, Region_Name"
				+ ", Region_Acronym, Region_Address"
				+ ",Region_Phone , Region_Fax, Region_Status, User_Id, Create_Date"
				+ " FROM MG_AD_REGION";

		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String regionId = rs.getString("Region_Id");
				String regionCode = rs.getString("Region_Code");
				String regionName = rs.getString("Region_Name");
				String regionAcronym = rs.getString("Region_Acronym");
				String regionAddress = rs.getString("Region_Address");
				String regionPhone = rs.getString("Region_Phone");
				String regionFax = rs.getString("Region_Fax");
				String regionStatus = rs.getString("Region_Status");
				String userId = rs.getString("User_Id");
				String createDate = rs.getString("Create_Date");

				region = new com.magbel.ia.vao.Region();
				region.setRegionId(regionId);
				region.setRegionCode(regionCode);
				region.setRegionName(regionName);
				region.setRegionAcronym(regionAcronym);
				region.setRegionAddress(regionAddress);
				region.setRegionStatus(regionStatus);
				region.setRegionPhone(regionPhone);
				region.setRegionFax(regionFax);
				region.setUserId(userId);
				region.setCreateDate(createDate);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return region;

	}

	public com.magbel.ia.vao.Region findRegionByID(String RegID) {
		com.magbel.ia.vao.Region region = null;

		String filter = " WHERE Region_Id = '" + RegID + "'";
		region = findARegion(filter);
		return region;

	}

	public com.magbel.ia.vao.Region findRegionByCode(String Regcode) {
		com.magbel.ia.vao.Region region = null;

		String filter = " WHERE  Region_Code = '" + Regcode + "'";
		region = findARegion(filter);
		return region;

	}

	
	public java.util.List<Region> findRegionByQuery(String filter) {
		java.util.List<Region> _list = new java.util.ArrayList<Region>();
		com.magbel.ia.vao.Region region = null;
		String query = "SELECT Region_Id, Region_Code, Region_Name"
				+ ", Region_Acronym, Region_Address"
				+ ",Region_Phone , Region_Fax, Region_Status, User_Id, Create_Date"
				+ " FROM MG_AD_REGION ";

		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String regionId = rs.getString("Region_Id");
				String regionCode = rs.getString("Region_Code");
				String regionName = rs.getString("Region_Name");
				String regionAcronym = rs.getString("Region_Acronym");
				String regionAddress = rs.getString("Region_Address");
				String regionPhone = rs.getString("Region_Phone");
				String regionFax = rs.getString("Region_Fax");
				String regionStatus = rs.getString("Region_Status");
				String userId = rs.getString("User_Id");
				String createDate = rs.getString("Create_Date");

				region = new com.magbel.ia.vao.Region(regionId, regionCode,
						regionName, regionAcronym, regionAddress, regionPhone,
						regionFax, regionStatus, userId, createDate);
				_list.add(region);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}

	public java.util.List<BranchDept> findAllDeptInBranch(String branchid) {
		java.util.List<BranchDept> _list = new java.util.ArrayList<BranchDept>();
		com.magbel.ia.vao.BranchDept dept = null;

		String query = "SELECT branchCode,deptCode,gl_prefix"
				+ ",gl_suffix,branchId,deptId,mtid"
				+ " FROM sbu_branch_dept WHERE branchId='" + branchid + "'";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				String branchcode = rs.getString("branchCode");
				String deptcode = rs.getString("deptCode");
				String glprefix = rs.getString("gl_prefix");
				String glsuffix = rs.getString("gl_suffix");
				String branchId = rs.getString("branchId");
				String deptid = rs.getString("branchId");
				String mtid = rs.getString("mtid");

				dept = new com.magbel.ia.vao.BranchDept();
				dept.setBranchCode(branchcode);
				dept.setBranchId(branchId);
				dept.setDeptId(deptid);
				dept.setDeptCode(deptcode);
				dept.setGl_prefix(glprefix);
				dept.setGl_suffix(glsuffix);
				dept.setMtid(mtid);

				_list.add(dept);
			}
		} catch (Exception ex) {
			System.out.println("WARN: Error fetching all asset ->" + ex);
		} finally {
			closeConnection(con, ps, rs);
		}
		return _list;

	}

	public java.util.List<DeptSection> findAllSectionInDept(String branchid, String deptId) {
		com.magbel.ia.vao.DeptSection dept = null;
		java.util.ArrayList<DeptSection> _list = new java.util.ArrayList<DeptSection>();
		String query = "SELECT branchCode,deptCode,gl_prefix,sectionCode"
				+ ",gl_suffix,branchId,deptId,sectionId,mtid"
				+ " FROM sbu_dept_section WHERE branchId='" + branchid
				+ "' AND deptId='" + deptId + "'";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				String branchcode = rs.getString("branchCode");
				String deptcode = rs.getString("deptCode");
				String glprefix = rs.getString("gl_prefix");
				String glsuffix = rs.getString("gl_suffix");
				String branchId = rs.getString("branchId");
				String deptid = rs.getString("branchId");
				String mtid = rs.getString("mtid");
				String sectioncode = rs.getString("sectionCode");
				String sectiondi = rs.getString("sectionId");

				dept = new com.magbel.ia.vao.DeptSection();
				dept.setBranchCode(branchcode);
				dept.setBranchId(branchId);
				dept.setDeptId(deptid);
				dept.setDeptCode(deptcode);
				dept.setGl_prefix(glprefix);
				dept.setGl_suffix(glsuffix);
				dept.setMtid(mtid);
				dept.setSectionCode(sectioncode);
				dept.setSectionId(sectiondi);
				_list.add(dept);
			}
		} catch (Exception ex) {
			System.out.println("WARN: Error fetching all asset ->" + ex);
		} finally {
			closeConnection(con, ps);
		}
		return _list;

	}

	public java.util.List<Province> findProvinceByQuery(String filter) {
		java.util.List<Province> _list = new java.util.ArrayList<Province>();
		com.magbel.ia.vao.Province province = null;
		String query = "SELECT Province_ID, Province_Code, Province"
				+ ",Status, User_id, create_date" + " FROM MG_GB_Province ";

		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String provinceid = rs.getString("Province_ID");
				String provincecode = rs.getString("Province_Code");
				String provincename = rs.getString("Province");
				String status = rs.getString("Status");
				String userid = rs.getString("User_id");
				String createdt = sdf.format(rs.getDate("create_date"));

				province = new com.magbel.ia.vao.Province();
				province.setProvinceId(provinceid);
				province.setProvinceCode(provincecode);
				province.setProvince(provincename);
				province.setStatus(status);
				province.setUserId(userid);
				province.setCreateDate(createdt);
				_list.add(province);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}

	private com.magbel.ia.vao.Province findAProvince(String filter) {
		com.magbel.ia.vao.Province province = null;
		String query = "SELECT Province_ID, Province_Code, Province"
				+ ",Status, User_id, create_date" + " FROM MG_GB_Province ";

		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String provinceid = rs.getString("Province_ID");
				String provincecode = rs.getString("Province_Code");
				String provincename = rs.getString("Province");
				String status = rs.getString("Status");
				String userid = rs.getString("User_id");
				String createdt = sdf.format(rs.getDate("create_date"));

				province = new com.magbel.ia.vao.Province();
				province.setProvinceId(provinceid);
				province.setProvinceCode(provincecode);
				province.setProvince(provincename);
				province.setStatus(status);
				province.setUserId(userid);
				province.setCreateDate(createdt);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return province;

	}

	public com.magbel.ia.vao.Province findProvinceByID(String provinceid) {
		String filter = " WHERE Province_ID='" + provinceid+"'";
		com.magbel.ia.vao.Province province = findAProvince(filter);
		return province;

	}

	public com.magbel.ia.vao.Province findProvinceByCode(String provincecode) {
		String filter = " WHERE Province_Code='" + provincecode + "'";
		com.magbel.ia.vao.Province province = findAProvince(filter);
		return province;

	}

	public boolean removeDeptFromBranch(java.util.ArrayList list) {
		String query = "DELETE FROM ia_sbu_branch_dept" + " WHERE branchId=?"
				+ " AND deptId=?";
		String query2 = "DELETE FROM sbu_dept_section" + " WHERE branchId=?"
				+ " AND deptId=?";

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		// ResultSet rs = null;
		com.magbel.ia.vao.BranchDept bd = null;
		int[] d = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps2 = con.prepareStatement(query2);
			for (int i = 0; i < list.size(); i++) {
				bd = (com.magbel.ia.vao.BranchDept) list.get(i);

				ps.setString(1, bd.getBranchId());
				ps.setString(2, bd.getDeptId());

				ps.addBatch();
				ps2.setString(1, bd.getBranchId());
				ps2.setString(2, bd.getDeptId());
				ps2.addBatch();

			}

			d = ps.executeBatch();
		
			ps2.addBatch();
		} catch (Exception ex) {
			System.out.println("WARN: Error fetching all asset ->" + ex);
		} finally {
			closeConnection(con, ps);
		}

		return (d.length > 0);
	}

	public com.magbel.ia.vao.BranchDept findDeptInBranch(String branchid,
			String deptId) {
		com.magbel.ia.vao.BranchDept dept = null;

		String query = "SELECT branchCode,deptCode,gl_prefix"
				+ ",gl_suffix,branchId,deptId,mtid"
				+ " FROM ia_sbu_branch_dept WHERE branchId='" + branchid
				+ "' AND deptId='" + deptId + "'";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				String branchcode = rs.getString("branchCode");
				String deptcode = rs.getString("deptCode");
				String glprefix = rs.getString("gl_prefix");
				String glsuffix = rs.getString("gl_suffix");
				String branchId = rs.getString("branchId");
				String deptid = rs.getString("branchId");
				String mtid = rs.getString("mtid");

				dept = new com.magbel.ia.vao.BranchDept();
				dept.setBranchCode(branchcode);
				dept.setBranchId(branchId);
				dept.setDeptId(deptid);
				dept.setDeptCode(deptcode);
				dept.setGl_prefix(glprefix);
				dept.setGl_suffix(glsuffix);
				dept.setMtid(mtid);

				// _list.add(dept);
			}
		} catch (Exception ex) {
			System.out.println("WARN: Error fetching all asset ->" + ex);
		} finally {
			closeConnection(con, ps);
		}
		return dept;

	}

	public com.magbel.ia.vao.DeptSection findSectionInDept(String branchid,
			String deptId, String sectionId) {
		com.magbel.ia.vao.DeptSection dept = null;

		String query = "SELECT branchCode,deptCode,gl_prefix,sectionCode"
				+ ",gl_suffix,branchId,deptId,sectionId,mtid"
				+ " FROM ia_sbu_dept_section WHERE branchId='" + branchid
				+ "' AND deptId='" + deptId + "'" + " AND sectionId='"
				+ sectionId + "'";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				String branchcode = rs.getString("branchCode");
				String deptcode = rs.getString("deptCode");
				String glprefix = rs.getString("gl_prefix");
				String glsuffix = rs.getString("gl_suffix");
				String branchId = rs.getString("branchId");
				String deptid = rs.getString("branchId");
				String mtid = rs.getString("mtid");
				String sectioncode = rs.getString("sectionCode");
				String sectiondi = rs.getString("sectionId");

				dept = new com.magbel.ia.vao.DeptSection();
				dept.setBranchCode(branchcode);
				dept.setBranchId(branchId);
				dept.setDeptId(deptid);
				dept.setDeptCode(deptcode);
				dept.setGl_prefix(glprefix);
				dept.setGl_suffix(glsuffix);
				dept.setMtid(mtid);
				dept.setSectionCode(sectioncode);
				dept.setSectionId(sectiondi);
				// _list.add(dept);
			}
		} catch (Exception ex) {
			System.out.println("WARN: Error fetching all asset ->" + ex);
		} finally {
			closeConnection(con, ps);
		}
		return dept;

	}

	public boolean removeSectionsFromDept(java.util.ArrayList list) {
		String query = "DELETE FROM ia_sbu_dept_section" + " WHERE branchId=?"
				+ " AND deptId=?" + " AND sectionId=?";
		Connection con = null;
		PreparedStatement ps = null;
		// ResultSet rs = null;
		com.magbel.ia.vao.DeptSection bd = null;
		int[] d = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			for (int i = 0; i < list.size(); i++) {
				bd = (com.magbel.ia.vao.DeptSection) list.get(i);

				ps.setString(1, bd.getBranchId());
				ps.setString(2, bd.getDeptId());
				ps.setString(3, bd.getSectionId());

				ps.addBatch();
			}
			d = ps.executeBatch();

		} catch (Exception ex) {
			System.out
					.println("WARN: Error removing Section From Department ->"
							+ ex);
		} finally {
			closeConnection(con, ps);
		}

		return (d.length > 0);
	}

	public boolean insertDeptForBranch(java.util.ArrayList list) {

		String query = "INSERT INTO ia_sbu_branch_dept(branchCode"
				+ ",deptCode,branchId" + ",deptId,gl_prefix"
				+ ",gl_suffix,mtid)" + " VALUES(?,?,?,?,?,?,?)";
		Connection con = null;
		PreparedStatement ps = null;
		// ResultSet rs = null;
		com.magbel.ia.vao.BranchDept bd = null;
		int[] d = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(query);

			for (int i = 0; i < list.size(); i++) {
				bd = (com.magbel.ia.vao.BranchDept) list.get(i);
				ps.setString(1, bd.getBranchCode());
				ps.setString(2, bd.getDeptCode());
				ps.setString(3, bd.getBranchId());
				ps.setString(4, bd.getDeptId());
				ps.setString(5, bd.getGl_prefix());
				ps.setString(6, bd.getGl_suffix());
				ps.setString(7, new ApplicationHelper()
						.getGeneratedId("ia_sbu_branch_dept"));
				ps.addBatch();
			}
			d = ps.executeBatch();
		} catch (Exception ex) {
			System.out.println("WARN: Error fetching all asset ->" + ex);
		} finally {
			closeConnection(con, ps);
		}
		return (d.length > 0);
	}

	public boolean updateDeptForBranch(java.util.ArrayList list) {
		String query = "UPDATE ia_sbu_branch_dept SET branchCode = ?"
				+ ",deptCode = ?,gl_prefix = ?,gl_suffix = ?"
				+ " WHERE branchId=?" + " AND deptId=?";
		Connection con = null;
		PreparedStatement ps = null;
		// ResultSet rs = null;
		com.magbel.ia.vao.BranchDept bd = null;
		int[] d = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(query);

			for (int i = 0; i < list.size(); i++) {
				bd = (com.magbel.ia.vao.BranchDept) list.get(i);
				ps.setString(1, bd.getBranchCode());
				ps.setString(2, bd.getDeptCode());
				ps.setString(3, bd.getGl_prefix());
				ps.setString(4, bd.getGl_suffix());
				ps.setString(5, bd.getBranchId());
				ps.setString(6, bd.getDeptId());

				ps.addBatch();
			}
			d = ps.executeBatch();

		} catch (Exception ex) {
			System.out.println("WARN: Error fetching all asset ->" + ex);
		} finally {
			closeConnection(con, ps);
		}

		return (d.length > 0);
	}

	public boolean insertSectionForDept(java.util.ArrayList list) {
		String query = "INSERT INTO ia_sbu_dept_section(branchCode"
				+ ",deptCode,sectionCode,branchId"
				+ ",deptId,sectionId,gl_prefix" + ",gl_suffix,mtid)"
				+ " VALUES(?,?,?,?,?,?,?,?,?)";
		Connection con = null;
		PreparedStatement ps = null;
		// ResultSet rs = null;
		com.magbel.ia.vao.DeptSection bd = null;
		int[] d = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(query);

			for (int i = 0; i < list.size(); i++) {
				bd = (com.magbel.ia.vao.DeptSection) list.get(i);
				ps.setString(1, bd.getBranchCode());
				ps.setString(2, bd.getDeptCode());
				ps.setString(3, bd.getSectionCode());
				ps.setString(4, bd.getBranchId());
				ps.setString(5, bd.getDeptId());
				ps.setString(6, bd.getSectionId());
				ps.setString(7, bd.getGl_prefix());
				ps.setString(8, bd.getGl_suffix());
				ps.setString(9, new ApplicationHelper()
						.getGeneratedId("ia_sbu_dept_section"));
				ps.addBatch();
			}
			d = ps.executeBatch();

		} catch (Exception ex) {
			System.out.println("WARN: Error fetching all asset ->" + ex);
		} finally {
			closeConnection(con, ps);
		}

		return (d.length > 0);
	}

	public boolean updateSectionForDept(java.util.ArrayList list) {
		String query = "UPDATE ia_sbu_dept_section SET branchCode = ?"
				+ ",deptCode = ?,sectionCode=?,gl_prefix = ?"
				+ ",gl_suffix = ?" + " WHERE branchId=?" + " AND deptId=?"
				+ " AND sectionId=?";
		Connection con = null;
		PreparedStatement ps = null;
		// ResultSet rs = null;
		com.magbel.ia.vao.DeptSection bd = null;
		int[] d = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			for (int i = 0; i < list.size(); i++) {
				bd = (com.magbel.ia.vao.DeptSection) list.get(i);

				ps.setString(1, bd.getBranchCode());
				ps.setString(2, bd.getDeptCode());
				ps.setString(3, bd.getSectionCode());
				ps.setString(4, bd.getGl_prefix());
				ps.setString(5, bd.getGl_suffix());
				ps.setString(6, bd.getBranchId());
				ps.setString(7, bd.getDeptId());
				ps.setString(8, bd.getSectionId());

				ps.addBatch();
			}
			d = ps.executeBatch();

		} catch (Exception ex) {
			System.out.println("WARN: Error updating SectionFOrDept ->" + ex);
		} finally {
			closeConnection(con, ps);
		}

		return (d.length > 0);
	}

	public String getProperties(String sele, String[] iden, String[] vals) {
		String html = new String();
		if (sele == null) {
			sele = " ";
		}
		for (int i = 0; i < iden.length; i++) {
			html = html + "<option value='" + iden[i] + "' "
					+ (iden[i].equalsIgnoreCase(sele) ? " selected " : "")
					+ ">" + vals[i] + "</option> ";
		}

		return html;
	}

	public String getProperties(String sele, String[][] vals) {
		String html = new String();
		if (sele == null) {
			sele = " ";
		}

		if (vals != null) {
			for (int i = 0; i < vals.length; i++) {
				html = html
						+ "<option value='"
						+ vals[i][0]
						+ "' "
						+ (vals[i][0].equalsIgnoreCase(sele) ? " selected "
								: "") + ">" + vals[i][2] + "</option> ";
			}

		}

		return html;
	}

	public java.util.ArrayList<State> findStateByStatus(String status) {
		String filter = " WHERE state_status='" + status + "'";
		java.util.ArrayList<State> _list = (java.util.ArrayList<State>) findStatesByQuery(filter);
		return _list;
	}

	public java.util.ArrayList<Province> findProvinceByStatus(String status) {
		String filter = " WHERE Status='" + status + "'";
		java.util.ArrayList<Province> _list = (java.util.ArrayList<Province>) findProvinceByQuery(filter);
		return _list;
	}

	public java.util.ArrayList<Branch> findBranchByBranchStatus(String status) {
		java.util.ArrayList<Branch> _list = null;
		String filter = " WHERE BRANCH_STATUS ='" + status + "'";
		_list = (java.util.ArrayList<Branch>) findBranchesByQuery(filter);
		return _list;
	}

	public java.util.ArrayList<Department> findDeptByDeptStatus(String status) {
		java.util.ArrayList<Department> _list = null;
		String filter = " WHERE Dept_Status='" + status + "'";
		_list = (java.util.ArrayList<Department>) findDeparmentsByQuery(filter);
		return _list;
	}

	public java.util.ArrayList<ExchangeRate> findAllExchangeRate() {
		java.util.ArrayList<ExchangeRate> _list = new java.util.ArrayList<ExchangeRate>();
		com.magbel.ia.vao.ExchangeRate exchange = null;
		String query = "SELECT MTID,currency_id,create_dt"
				+ ",effective_dt,exchg_rate" + ",user_id ,status,method"
				+ " FROM ia_GB_EXCH_RATE";

		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String currid = rs.getString("currency_id");
				String rate = rs.getString("exchg_rate");
				String createdt = sdf.format(rs.getDate("create_dt"));
				String effdate = sdf.format(rs.getDate("effective_dt"));
				String userid = rs.getString("user_id");
				String mtid = rs.getString("MTID");
				String status = rs.getString("status");
				String method = rs.getString("method");
				exchange = new com.magbel.ia.vao.ExchangeRate();
				exchange.setCreate_dt(createdt);
				exchange.setCurrency_id(currid);
				exchange.setEffective_dt(effdate);
				exchange.setExchg_rate(rate);
				exchange.setMTID(mtid);
				exchange.setStatus(status);
				exchange.setUser_id(userid);
				exchange.setMethod(method);
				_list.add(exchange);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}

	public java.util.ArrayList<ExchangeRate> findExchangeRateByQuery(String filter) {
		java.util.ArrayList<ExchangeRate> _list = new java.util.ArrayList<ExchangeRate>();
		com.magbel.ia.vao.ExchangeRate exchange = null;
		String query = "SELECT MTID,currency_id,create_dt"
				+ ",effective_dt,exchg_rate" + ",user_id ,status,method"
				+ " FROM ia_GB_EXCH_RATE WHERE currency_id IS NOT NULL ";

		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String currid = rs.getString("currency_id");
				String rate = rs.getString("exchg_rate");
				String createdt = sdf.format(rs.getDate("create_dt"));
				String effdate = sdf.format(rs.getDate("effective_dt"));
				String userid = rs.getString("user_id");
				String mtid = rs.getString("MTID");
				String status = rs.getString("status");
				String method = rs.getString("method");
				exchange = new com.magbel.ia.vao.ExchangeRate();
				exchange.setCreate_dt(createdt);
				exchange.setCurrency_id(currid);
				exchange.setEffective_dt(effdate);
				exchange.setExchg_rate(rate);
				exchange.setMTID(mtid);
				exchange.setStatus(status);
				exchange.setMethod(method);
				exchange.setUser_id(userid);
				_list.add(exchange);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}

	public com.magbel.ia.vao.ExchangeRate findExchangeRateByCurrID(String curryid) {
		// java.util.ArrayList _list = new java.util.ArrayList();
		com.magbel.ia.vao.ExchangeRate exchange = null;
		String query = "SELECT MTID,currency_id,create_dt"
				+ ",effective_dt,exchg_rate" + ",user_id ,status,method"
				+ " FROM ia_GB_EXCH_RATE WHERE currency_id=" + curryid;

		// query = query+filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String currid = rs.getString("currency_id");
				String rate = rs.getString("exchg_rate");
				String createdt = sdf.format(rs.getDate("create_dt"));
				String effdate = sdf.format(rs.getDate("effective_dt"));
				String userid = rs.getString("user_id");
				String mtid = rs.getString("MTID");
				String status = rs.getString("status");
				String method = rs.getString("method");
				exchange = new com.magbel.ia.vao.ExchangeRate();
				exchange.setCreate_dt(createdt);
				exchange.setCurrency_id(currid);
				exchange.setEffective_dt(effdate);
				exchange.setExchg_rate(rate);
				exchange.setMTID(mtid);
				exchange.setStatus(status);
				exchange.setUser_id(userid);
				exchange.setMethod(method);
				// _list.add(exchange);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return exchange;

	}

	public com.magbel.ia.vao.ExchangeRate findExchangeRateByMTID(String curryid) {
		// java.util.ArrayList _list = new java.util.ArrayList();
		com.magbel.ia.vao.ExchangeRate exchange = null;
		String query = "SELECT MTID,currency_id,create_dt"
				+ ",effective_dt,exchg_rate" + ",user_id ,status,method"
				+ " FROM ia_GB_EXCH_RATE WHERE MTID='" + curryid+"'";

		// query = query+filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String currid = rs.getString("currency_id");
				String rate = rs.getString("exchg_rate");
				String createdt = sdf.format(rs.getDate("create_dt"));
				String effdate = sdf.format(rs.getDate("effective_dt"));
				String userid = rs.getString("user_id");
				String mtid = rs.getString("MTID");
				String status = rs.getString("status");
				String method = rs.getString("method");
				exchange = new com.magbel.ia.vao.ExchangeRate();
				exchange.setCreate_dt(createdt);
				exchange.setCurrency_id(currid);
				exchange.setEffective_dt(effdate);
				exchange.setExchg_rate(rate);
				exchange.setMTID(mtid);
				exchange.setStatus(status);
				exchange.setUser_id(userid);
				exchange.setMethod(method);
				// _list.add(exchange);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return exchange;

	}

	public java.util.ArrayList<CurrencyCode> findAllCurrency() {
		java.util.ArrayList<CurrencyCode> _list = new java.util.ArrayList<CurrencyCode>();
		com.magbel.ia.vao.CurrencyCode curcode = null;
		String query = "SELECT currency_id,iso_code,currency_symbol"
				+ " ,description,status,create_dt,local_currency"
				+ " ,user_id,mtid" + " FROM MG_GB_CURRENCY_CODE";

		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String currid = rs.getString("currency_id");
				String isocode = rs.getString("iso_code");
				String currsymbol = rs.getString("currency_symbol");
				String description = rs.getString("description");
				String createdt = sdf.format(rs.getDate("create_dt"));
				String localcurr = rs.getString("local_currency");
				String userid = rs.getString("user_id");
				String mtid = rs.getString("mtid");
				String status = rs.getString("status");

				curcode = new com.magbel.ia.vao.CurrencyCode(currid, isocode,
						currsymbol, description, status);
				curcode.setCreate_dt(createdt);
				curcode.setMtid(mtid);
				curcode.setLocal_currency(localcurr);
				curcode.setUser_id(userid);
				_list.add(curcode);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}

	public java.util.ArrayList<CurrencyCode> findCurrencyByQuery(String filter) {
		java.util.ArrayList<CurrencyCode> _list = new java.util.ArrayList<CurrencyCode>();
		com.magbel.ia.vao.CurrencyCode curcode = null;
		String query = "SELECT currency_id,iso_code,currency_symbol"
				+ " ,description,status,create_dt,local_currency"
				+ " ,user_id,mtid"
				+ " FROM MG_GB_CURRENCY_CODE WHERE currency_id IS NOT NULL ";
		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String currid = rs.getString("currency_id");
				String isocode = rs.getString("iso_code");
				String currsymbol = rs.getString("currency_symbol");
				String description = rs.getString("description");
				String createdt = sdf.format(rs.getDate("create_dt"));
				String localcurr = rs.getString("local_currency");
				String userid = rs.getString("user_id");
				String mtid = rs.getString("mtid");
				String status = rs.getString("status");

				curcode = new com.magbel.ia.vao.CurrencyCode(currid, isocode,
						currsymbol, description, status);
				curcode.setCreate_dt(createdt);
				curcode.setMtid(mtid);
				curcode.setLocal_currency(localcurr);
				curcode.setUser_id(userid);
				_list.add(curcode);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}

	public com.magbel.ia.vao.CurrencyCode findCurrencyByCurrID(String currencyid) {
		// java.util.ArrayList _list = new java.util.ArrayList();
		com.magbel.ia.vao.CurrencyCode curcode = null;
		String query = "SELECT currency_id,iso_code,currency_symbol"
				+ " ,description,status,create_dt,local_currency"
				+ " ,user_id,mtid"
				+ " FROM MG_GB_CURRENCY_CODE WHERE currency_id=" + currencyid;
		// query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String currid = rs.getString("currency_id");
				String isocode = rs.getString("iso_code");
				String currsymbol = rs.getString("currency_symbol");
				String description = rs.getString("description");
				String createdt = sdf.format(rs.getDate("create_dt"));
				String localcurr = rs.getString("local_currency");
				String userid = rs.getString("user_id");
				String mtid = rs.getString("mtid");
				String status = rs.getString("status");

				curcode = new com.magbel.ia.vao.CurrencyCode(currid, isocode,
						currsymbol, description, status);
				curcode.setCreate_dt(createdt);
				curcode.setMtid(mtid);
				curcode.setLocal_currency(localcurr);
				curcode.setUser_id(userid);
				// _list.add(curcode);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return curcode;

	}

	public com.magbel.ia.vao.CurrencyCode findLocalCurrency() {
		// java.util.ArrayList _list = new java.util.ArrayList();
		com.magbel.ia.vao.CurrencyCode curcode = null;
		String query = "SELECT currency_id,iso_code,currency_symbol"
				+ " ,description,status,create_dt,local_currency"
				+ " ,user_id,mtid"
				+ " FROM MG_GB_CURRENCY_CODE WHERE local_currency='Y'";
		// query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String currid = rs.getString("currency_id");
				String isocode = rs.getString("iso_code");
				String currsymbol = rs.getString("currency_symbol");
				String description = rs.getString("description");
				String createdt = sdf.format(rs.getDate("create_dt"));
				String localcurr = rs.getString("local_currency");
				String userid = rs.getString("user_id");
				String mtid = rs.getString("mtid");
				String status = rs.getString("status");

				curcode = new com.magbel.ia.vao.CurrencyCode(currid, isocode,
						currsymbol, description, status);
				curcode.setCreate_dt(createdt);
				curcode.setMtid(mtid);
				curcode.setLocal_currency(localcurr);
				curcode.setUser_id(userid);
				// _list.add(curcode);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return curcode;

	}

	public com.magbel.ia.vao.CurrencyCode findCurrencyByMTID(String mtidy) {
		// java.util.ArrayList _list = new java.util.ArrayList();
		com.magbel.ia.vao.CurrencyCode curcode = null;
		String query = "SELECT currency_id,iso_code,currency_symbol"
				+ " ,description,status,create_dt,local_currency"
				+ " ,user_id,mtid" + " FROM MG_GB_CURRENCY_CODE WHERE mtid='"
				+ mtidy+"'";
		// query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String currid = rs.getString("currency_id");
				String isocode = rs.getString("iso_code");
				String currsymbol = rs.getString("currency_symbol");
				String description = rs.getString("description");
				String createdt = sdf.format(rs.getDate("create_dt"));
				String localcurr = rs.getString("local_currency");
				String userid = rs.getString("user_id");
				String mtid = rs.getString("mtid");
				String status = rs.getString("status");

				curcode = new com.magbel.ia.vao.CurrencyCode(currid, isocode,
						currsymbol, description, status);
				curcode.setCreate_dt(createdt);
				curcode.setMtid(mtid);
				curcode.setLocal_currency(localcurr);
				curcode.setUser_id(userid);
				// _list.add(curcode);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return curcode;

	}

	public boolean CurrencyIDExists(String mtidy) {
		// java.util.ArrayList _list = new java.util.ArrayList();
		// com.magbel.ia.vao.CurrencyCode curcode = null;
		String query = "SELECT currency_id,iso_code,currency_symbol"
				+ " ,description,status,create_dt,local_currency"
				+ " ,user_id,mtid"
				+ " FROM MG_GB_CURRENCY_CODE WHERE currency_id=" + mtidy;
		// query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;
		boolean exists = false;
		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			exists = rs.next();

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return exists;

	}

	public boolean ExCurrencyIDExists(String mtidy) {
		// java.util.ArrayList _list = new java.util.ArrayList();
		// com.magbel.ia.vao.CurrencyCode curcode = null;
		String query = "SELECT *" + " FROM ia_GB_EXCH_RATE WHERE currency_id="
				+ mtidy;
		// query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;
		boolean exists = false;
		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			exists = rs.next();

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return exists;

	}

	/**
	 * createCurrency
	 */
	public boolean createCurrency(com.magbel.ia.vao.CurrencyCode ccode) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "INSERT INTO MG_GB_CURRENCY_CODE(currency_id,iso_code"
				+ ",currency_symbol ,description,status,create_dt,"
				+ "local_currency ,user_id,MTID) "
				+ " VALUES (?,?,?,?,?,?,?,?,?)";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, ccode.getCurrency_id());
			ps.setString(2, ccode.getIso_code());
			ps.setString(3, ccode.getCurrency_symbol());
			ps.setString(4, ccode.getDescription());
			ps.setString(5, ccode.getStatus());
			ps.setDate(6, df.dateConvert(new java.util.Date()));
			ps.setString(7, ccode.getLocal_currency());
			ps.setString(8, ccode.getUser_id());
			ps.setString(9, new ApplicationHelper()
					.getGeneratedId("MG_GB_CURRENCY_CODE"));
			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

	public boolean updateCurrency(com.magbel.ia.vao.CurrencyCode ccode) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "UPDATE MG_GB_CURRENCY_CODE"
				+ " SET currency_id = ?,iso_code = ?"
				+ ",currency_symbol = ?,description = ?" + ",status = ?"
				+ ",local_currency = ?" + " WHERE currency_id = ?";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, ccode.getCurrency_id());
			ps.setString(2, ccode.getIso_code());
			ps.setString(3, ccode.getCurrency_symbol());
			ps.setString(4, ccode.getDescription());
			ps.setString(5, ccode.getStatus());
			// ps.setDate(6,df.dateConvert(new java.util.Date()));
			ps.setString(6, ccode.getLocal_currency());
			ps.setString(7, ccode.getCurrency_id());
			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

	public boolean createExchangeRate(com.magbel.ia.vao.ExchangeRate ccode) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "INSERT INTO ia_GB_EXCH_RATE"
				+ "(currency_id,create_dt,effective_dt"
				+ ",exchg_rate,user_id,status,method,MTID)"
				+ " VALUES (?,?,?,?,?,?,?,?)";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, ccode.getCurrency_id());
			ps.setDate(2, df.dateConvert(new java.util.Date()));
			ps.setDate(3, df.dateConvert(ccode.getEffective_dt()));
			ps.setString(4, ccode.getExchg_rate());
			ps.setString(5, ccode.getUser_id());
			ps.setString(6, ccode.getStatus());
			ps.setString(7, ccode.getMethod());
			ps.setString(8, new ApplicationHelper()
					.getGeneratedId("ia_GB_EXCH_RATE"));

			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

	public boolean updateExchangeRate(com.magbel.ia.vao.ExchangeRate ccode,
			boolean dirty) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		if (dirty) {
			logExchangeRate(ccode.getCurrency_id());
		}
		String query = "UPDATE ia_GB_EXCH_RATE"
				+ " SET effective_dt = ?,exchg_rate = ?,status = ?"
				+ ",method = ?  WHERE currency_id =?";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);

			// ps.setDate(1,df.dateConvert(new java.util.Date()));
			ps.setDate(1, df.dateConvert(ccode.getEffective_dt()));
			ps.setString(2, ccode.getExchg_rate());
			// ps.setString(4,ccode.getUser_id());
			ps.setString(3, ccode.getStatus());
			ps.setString(4, ccode.getMethod());
			ps.setString(5, ccode.getCurrency_id());

			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

	public boolean logExchangeRate(String cid) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		com.magbel.ia.vao.ExchangeRate ccode = new com.magbel.ia.vao.ExchangeRate();
		ccode = this.findExchangeRateByCurrID(cid);
		String query = "INSERT INTO ia_EXCH_RATE_HISTORY"
				+ "(currency_id,create_dt,effective_dt"
				+ ",exchg_rate,user_id,status,mtid)"
				+ " VALUES (?,?,?,?,?,?,?)";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, ccode.getCurrency_id());
			ps.setDate(2, df.dateConvert(new java.util.Date()));
			ps.setDate(3, df.dateConvert(ccode.getEffective_dt()));
			ps.setString(4, ccode.getExchg_rate());
			ps.setString(5, ccode.getUser_id());
			ps.setString(6, ccode.getStatus());
			ps.setString(7, ccode.getMTID());

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
	 *
	 * @param company
	 * @return
	 */
	public boolean createCompany(com.magbel.ia.vao.Company company) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "INSERT INTO MG_GB_COMPANY(Company_Code, Company_Name, Acronym, Company_Address"
				+ ",Financial_Start_Date, Financial_No_OfMonths"
				+ ", Financial_End_Date,Minimum_Password"
				+ ", Password_Expiry, Session_Timeout, Enforce_Acq_Budget, "
				+ "Quarterly_Surplus_Cf,UserId,Trans_Wait_time,log_user_audit,Status,Company_Id,"
				+ "COLLECTION1_ACCT,COLLECTION2_ACCT,COLLECTION3_ACCT,suspense_acct,SALARY_CONTROL)"
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, company.getCompanyCode());
			ps.setString(2, company.getCompanyName());
			ps.setString(3, company.getAcronym());
			ps.setString(4, company.getCompanyAddress());
			ps.setDate(5, df.dateConvert(company.getFinancialStartDate()));
			ps.setInt(6, company.getFinancialNoOfMonths());
			ps.setDate(7, df.dateConvert(company.getFinancialEndDate()));
			ps.setInt(8, company.getMinimumPassword());
			ps.setInt(9, company.getPasswordExpiry());
			ps.setInt(10, company.getSessionTimeout());
			ps.setString(11, company.getEnforceAcqBudget());
			ps.setString(12, company.getQuarterlySurplusCf());
			ps.setString(13, company.getUserId());
			ps.setDouble(14, company.getTransWaitTime());
			ps.setString(15, company.getLogUserAudit());
			ps.setString(16, company.getStatus());
			ps.setString(17, new ApplicationHelper()
					.getGeneratedId("MG_GB_COMPANY"));
			ps.setString(18, company.getCollectcashacct());
			ps.setString(19, company.getCollectchqacct());
			ps.setString(20, company.getCollecttrsacct());
			ps.setString(21, company.getSuspenseacct());
			ps.setString(22, company.getSalaryControl());
			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}
/**
 *
 * @param company
 * @return
 */
	public boolean updateCompany(com.magbel.ia.vao.Company company) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "UPDATE MG_GB_COMPANY"
				+ " SET  Company_Code = ?,Company_Name = ?, Acronym = ?, Company_Address = ?"
				+ ", Financial_Start_Date = ?, Financial_No_OfMonths = ?"
				+ ", Financial_End_Date = ?, Minimum_Password = ?"
				+ ", Password_Expiry = ?, Session_Timeout = ?, Enforce_Acq_Budget = ?"
				+ ", Quarterly_Surplus_Cf = ?,Trans_Wait_time=?,Log_User_Audit=?,Status=?" 
				+ ",  COLLECTION1_ACCT=?,COLLECTION2_ACCT=?,COLLECTION3_ACCT=?,enforce_pm_budget=?,enforce_fuel_allocation=?,require_quarterly_pm=?,suspense_acct=?,SALARY_CONTROL = ? WHERE Company_ID=?";
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps = con.prepareStatement(query);
			ps.setString(1, company.getCompanyCode());
			ps.setString(2, company.getCompanyName());
			ps.setString(3, company.getAcronym());
			ps.setString(4, company.getCompanyAddress());
			ps.setDate(5, df.dateConvert(company.getFinancialStartDate()));
			ps.setInt(6, company.getFinancialNoOfMonths());
			ps.setDate(7, df.dateConvert(company.getFinancialEndDate()));
			ps.setInt(8, company.getMinimumPassword());
			ps.setInt(9, company.getPasswordExpiry());
			ps.setInt(10, company.getSessionTimeout());
			ps.setString(11, company.getEnforceAcqBudget());
			ps.setString(12, company.getQuarterlySurplusCf());
			ps.setDouble(13, company.getTransWaitTime());
			ps.setString(14, company.getLogUserAudit());
			ps.setString(15, company.getStatus());
			ps.setString(16, company.getCollectcashacct());
			ps.setString(17, company.getCollectchqacct());
			ps.setString(18, company.getCollecttrsacct());
			ps.setString(19, company.getEnforcePmBudget());
			ps.setString(20, company.getEnforceFuelAllocation());
			ps.setString(21, company.getRequireQuarterlyPm());
			ps.setString(22, company.getSuspenseacct());
			ps.setString(23, company.getSalaryControl());
			ps.setString(24, company.getCompanyId());

			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}
	
	public java.util.List<Company> findCompanyByQuery(String filter) {
		java.util.List<Company> _list = new java.util.ArrayList<Company>();
		com.magbel.ia.vao.Company company = null;
		String query = "SELECT Company_Id,Company_Code, Company_Name, Acronym, Company_Address"
				+ ",  Financial_Start_Date, Financial_No_OfMonths"
				+ ", Financial_End_Date,Minimum_Password, Password_Expiry, Session_Timeout"
				+ ", Enforce_Acq_Budget,suspense_acct"
				+ ", Quarterly_Surplus_Cf,UserId, Status, Trans_Wait_Time,log_user_audit, COLLECTION1_ACCT,COLLECTION2_ACCT,COLLECTION3_ACCT,"
				+ " SALARY_CONTROL FROM MG_GB_COMPANY";
				
		
		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String companyId = rs.getString("Company_Id");
				String companyCode = rs.getString("Company_Code");
				String companyName = rs.getString("Company_Name");
				String acronym = rs.getString("Acronym");
				String companyAddress = rs.getString("Company_Address");
				String financialStartDate = sdf.format(rs.getDate("Financial_Start_Date"));
				int financialNoOfMonths = rs.getInt("Financial_No_OfMonths");
				String financialEndDate = sdf.format(rs.getDate("Financial_End_Date"));
				int minimumPassword = rs.getInt("Minimum_Password");
				int passwordExpiry = rs.getInt("Password_Expiry");
				int sessionTimeout = rs.getInt("Session_Timeout");
				String enforceAcqBudget = rs.getString("Enforce_Acq_Budget");
				String quarterlySurplusCf = rs.getString("Quarterly_Surplus_Cf");
				String userId = rs.getString("UserId");
				String status = rs.getString("Status");
				double transWaitTime = rs.getDouble("Trans_Wait_Time");
				String logUserAudit = rs.getString("log_user_audit");
				String collectcashacct = rs.getString("COLLECTION1_ACCT");
				String collectchqacct = rs.getString("COLLECTION2_ACCT");
				String collecttrsacct = rs.getString("COLLECTION3_ACCT");
				String suspenseacct = rs.getString("suspense_acct");
				String salaryControl = rs.getString("SALARY_CONTROL");
			company = new com.magbel.ia.vao.Company(companyId,companyCode,companyName, acronym, companyAddress,
													financialStartDate, financialNoOfMonths,
													financialEndDate, minimumPassword, passwordExpiry,
													sessionTimeout, enforceAcqBudget,
													quarterlySurplusCf, userId, status,
													transWaitTime,logUserAudit);
			company.setCollectcashacct(collectcashacct);
			company.setCollectchqacct(collectchqacct);
			company.setCollecttrsacct(collecttrsacct);
			company.setSuspenseacct(suspenseacct);
			company.setSalaryControl(salaryControl);
			_list.add(company);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}

	private com.magbel.ia.vao.Company findACompany(String filter) {
		com.magbel.ia.vao.Company company = null;
		String query = "SELECT Company_Id,Company_Code, Company_Name, Acronym, Company_Address"
				+ ",  Financial_Start_Date, Financial_No_OfMonths,enforce_fuel_allocation"
				+ ", Financial_End_Date,Minimum_Password, Password_Expiry, Session_Timeout,suspense_acct"
				+ ", Enforce_Acq_Budget,enforce_pm_budget,Quarterly_Surplus_Cf,require_quarterly_pm"
				+ ", UserId, Status, Trans_Wait_Time,log_user_audit,COLLECTION1_ACCT,COLLECTION2_ACCT,COLLECTION3_ACCT,SALARY_CONTROL"
				+ " FROM MG_GB_COMPANY";

		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;
		
		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String companyId = rs.getString("Company_Id");
				String companyCode = rs.getString("Company_Code");
				String companyName = rs.getString("Company_Name");
				String acronym = rs.getString("Acronym");
				String companyAddress = rs.getString("Company_Address");
				String financialStartDate = sdf.format(rs.getDate("Financial_Start_Date"));
				int financialNoOfMonths = rs.getInt("Financial_No_OfMonths");
				String financialEndDate = sdf.format(rs.getDate("Financial_End_Date"));
				int minimumPassword = rs.getInt("Minimum_Password");
				int passwordExpiry = rs.getInt("Password_Expiry");
				int sessionTimeout = rs.getInt("Session_Timeout");
				String enforcepmbudget = rs.getString("enforce_pm_budget");
				if(enforcepmbudget==null){enforcepmbudget = "N";}
				String enforcefuelallocation = rs.getString("enforce_fuel_allocation");
				if(enforcefuelallocation==null){enforcefuelallocation = "N";}
				String enforceAcqBudget = rs.getString("Enforce_Acq_Budget");
//				System.out.println("<<<<<<<<What is enforceAcqBudget: "+enforceAcqBudget);
				if(enforceAcqBudget==null){enforceAcqBudget = "N";}
//				System.out.println("<<<<<<<<What Next is enforceAcqBudget: "+enforceAcqBudget);
				String quarterlySurplusCf = rs.getString("require_quarterly_pm");
				if(quarterlySurplusCf==null){quarterlySurplusCf = "N";}
				String userId = rs.getString("UserId");
				String status = rs.getString("Status");
				double transWaitTime = rs.getDouble("Trans_Wait_Time");
				String logUserAudit = rs.getString("log_user_audit");
				String collectcashacct = rs.getString("COLLECTION1_ACCT");
				String collectchqacct = rs.getString("COLLECTION2_ACCT");
				String collecttrsacct = rs.getString("COLLECTION3_ACCT");
				String suspenseacct = rs.getString("suspense_acct");
				String salaryControl = rs.getString("SALARY_CONTROL");
			company = new com.magbel.ia.vao.Company(companyId,companyCode,companyName, acronym, companyAddress,
													financialStartDate, financialNoOfMonths,
													financialEndDate, minimumPassword, passwordExpiry,
													sessionTimeout, enforceAcqBudget,
													quarterlySurplusCf, userId, status,
													transWaitTime,logUserAudit);
													company.setEnforceFuelAllocation(enforcefuelallocation);
													company.setEnforcePmBudget(enforcepmbudget);
													company.setCollectcashacct(collectcashacct);
													company.setCollectchqacct(collectchqacct);
													company.setCollecttrsacct(collecttrsacct);
													company.setSuspenseacct(suspenseacct);
													company.setSalaryControl(salaryControl);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return company;

	}
	
	
	public com.magbel.ia.vao.Company findCompany() {
		com.magbel.ia.vao.Company company = null;
		String query = "SELECT Company_Id,Company_Code, Company_Name, Acronym, Company_Address"
				+ ",  Financial_Start_Date, Financial_No_OfMonths,suspense_acct"
				+ ", Financial_End_Date,Minimum_Password, Password_Expiry, Session_Timeout"
				+ ", Enforce_Acq_Budget,Enforce_Acq_Budget,Quarterly_Surplus_Cf"
				+ ", UserId, Status, Trans_Wait_Time,log_user_audit,vat_rate,wht_rate"
				+ ", require_quarterly_pm,enforce_fuel_allocation,enforce_pm_budget,COLLECTION1_ACCT,COLLECTION2_ACCT,COLLECTION3_ACCT,SALARY_CONTROL"
				+ " FROM MG_GB_COMPANY";
		
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			
			while (rs.next()) {
				String companyId = rs.getString("Company_Id");
				String companyCode = rs.getString("Company_Code");
				String companyName = rs.getString("Company_Name");
				String acronym = rs.getString("Acronym");
				String companyAddress = rs.getString("Company_Address");
				String financialStartDate = sdf.format(rs.getDate("Financial_Start_Date"));
				int financialNoOfMonths = rs.getInt("Financial_No_OfMonths");
				String financialEndDate = sdf.format(rs.getDate("Financial_End_Date"));
				int minimumPassword = rs.getInt("Minimum_Password");
				int passwordExpiry = rs.getInt("Password_Expiry");
				int sessionTimeout = rs.getInt("Session_Timeout");
				String enforceAcqBudget = rs.getString("Enforce_Acq_Budget");
				String quarterlySurplusCf = rs.getString("Quarterly_Surplus_Cf");
				String userId = rs.getString("UserId");
				String status = rs.getString("Status");
				double transWaitTime = rs.getDouble("Trans_Wait_Time");
				String logUserAudit = rs.getString("log_user_audit");
				double vatrate = rs.getDouble("vat_rate");
				double whtrate = rs.getDouble("wht_rate");
				String requireQuarterlyPm = rs.getString("require_quarterly_pm");
				String enforceFuelAllocation = rs.getString("enforce_fuel_allocation");
				String enforcePmBudget = rs.getString("enforce_pm_budget");
				String collectcashacct = rs.getString("COLLECTION1_ACCT"); 
				String collectchqacct = rs.getString("COLLECTION2_ACCT"); 
				String collecttrsacct = rs.getString("COLLECTION3_ACCT");
				String suspenseacct = rs.getString("suspense_acct");
				String salaryControl = rs.getString("SALARY_CONTROL");
			company = new com.magbel.ia.vao.Company(companyId,companyCode,companyName, acronym, companyAddress,
													financialStartDate, financialNoOfMonths,
													financialEndDate, minimumPassword, passwordExpiry,
													sessionTimeout, enforceAcqBudget,
													quarterlySurplusCf, userId, status,
													transWaitTime,logUserAudit);
													company.setVatRate(vatrate);
													company.setWhtRate(whtrate);
													company.setRequireQuarterlyPm(requireQuarterlyPm);
													company.setEnforceFuelAllocation(enforceFuelAllocation);
													company.setEnforcePmBudget(enforcePmBudget);
													company.setCollectcashacct(collectcashacct);
													company.setCollectchqacct(collectchqacct);
													company.setCollecttrsacct(collecttrsacct);
													company.setSuspenseacct(suspenseacct);
													company.setSalaryControl(salaryControl);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return company;
	}


	public com.magbel.ia.vao.Company findCompanyByID(String companyId) {
		String filter = " WHERE company_ID='" + companyId+"'";
		com.magbel.ia.vao.Company company = findACompany(filter);
		return company;

	}

	public com.magbel.ia.vao.Company findCompanyByCode(String companyCode) {
		String filter = " WHERE Company_Code='" + companyCode + "'";
		com.magbel.ia.vao.Company company = findACompany(filter);
		return company;

	}
	
	
	public java.util.List<Suffix> findSuffixByQuery(String filter) {
		java.util.List<Suffix> _list = new java.util.ArrayList<Suffix>();
		com.magbel.ia.vao.Suffix suffix = null;
		String query = "SELECT suffix_ID,Suffix_code,description"
				+ "  ,charge_code,status,create_date" + " FROM MG_GB_SUFFIX ";

		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String suffixId = rs.getString("Suffix_ID");
				String suffixCode = rs.getString("Suffix_code");
				String description = rs.getString("Description");
				String chargeCode = rs.getString("Charge_Code");
				String status = rs.getString("Status");
				String createDate = rs.getString("Create_Date");
				suffix = new com.magbel.ia.vao.Suffix(suffixId, suffixCode,
						description, chargeCode, status, createDate);

				_list.add(suffix);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}

	private com.magbel.ia.vao.Suffix findASuffix(String filter) {
		// java.util.ArrayList _list = new java.util.ArrayList();
		com.magbel.ia.vao.Suffix suffix = null;
		String query = "SELECT Suffix_ID,Suffix_Code,Description"
				+ "  ,Charge_Code,Status,Create_date" + " FROM MG_GB_SUFFIX ";
		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String suffixId = rs.getString("Suffix_ID");
				String suffixCode = rs.getString("Suffix_code");
				String description = rs.getString("Description");
				String chargeCode = rs.getString("Charge_Code");
				String status = rs.getString("Status");
				String createDate = rs.getString("Create_date");
				suffix = new com.magbel.ia.vao.Suffix(suffixId, suffixCode,
						description, chargeCode, status, createDate);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return suffix;

	}

	public com.magbel.ia.vao.Suffix findSuffixByID(String suffixid) {
		String filter = " WHERE suffix_ID='" + suffixid+"'";
		com.magbel.ia.vao.Suffix suffix = findASuffix(filter);
		return suffix;

	}
	
	public com.magbel.ia.vao.Suffix findSuffixByCode(String suffixCode) {
		String filter = " WHERE suffix_Code='" + suffixCode + "'";
		com.magbel.ia.vao.Suffix suffix = findASuffix(filter);
		return suffix;

	}

	
		public java.util.List<SystemTransactionCode> findSystemTransactionCodeByQuery(String filter) {
		java.util.List<SystemTransactionCode> _list = new java.util.ArrayList<SystemTransactionCode>();
		com.magbel.ia.vao.SystemTransactionCode stc = null;
		String query = "SELECT MTID, Code, Description"
				+ "  , Dr_Cr, Create_Date, Status, Cot_Charge" + " FROM IA_SYSTEM_TRANSACTION_CODE ";

		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String sysId = rs.getString("MTID");
				String code = rs.getString("Code");
				String description = rs.getString("Description");
				String drCr = rs.getString("Dr_Cr");
				String createDate = rs.getString("Create_Date");
				String status = rs.getString("Status");
				String cotCharge = rs.getString("Cot_Charge");
				stc = new com.magbel.ia.vao.SystemTransactionCode(sysId, code,
						description, drCr, createDate, status, cotCharge);

				_list.add(stc);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}

	private com.magbel.ia.vao.SystemTransactionCode findASystemTransactionCode(String filter) {
		// java.util.ArrayList _list = new java.util.ArrayList();
		com.magbel.ia.vao.SystemTransactionCode stc = null;
		String query = "SELECT MTID,Code,Description"
				+ "  ,Dr_Cr,Create_Date,Status,Cot_Charge" + " FROM IA_SYSTEM_TRANSACTION_CODE ";

		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String sysId = rs.getString("MTID");
				String code = rs.getString("Code");
				String description = rs.getString("Description");
				String drCr = rs.getString("Dr_Cr");
				String createDate = rs.getString("Create_Date");
				String status = rs.getString("Status");
				String cotCharge = rs.getString("Cot_Charge");
				stc = new com.magbel.ia.vao.SystemTransactionCode(sysId, code,
						description, drCr, createDate, status, cotCharge);
						
//			System.out.println("MTID>>>>>>>....."+sysId);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return stc;

	}

	public com.magbel.ia.vao.SystemTransactionCode findSystemTransactionCodeByID(String sysId) {
		String filter = " WHERE MTID='" + sysId +"'";
		com.magbel.ia.vao.SystemTransactionCode stc = findASystemTransactionCode(filter);
		return stc;

	}
	
	public com.magbel.ia.vao.SystemTransactionCode findSystemTransactionCodeByCode(String code) {
		String filter = " WHERE Code='" + code + "'";
		com.magbel.ia.vao.SystemTransactionCode stc = findASystemTransactionCode(filter);
		return stc;

	}
	
	
		public java.util.List<UserTransactionCode> findUserTransactionCodeByQuery(String filter) {
		java.util.List<UserTransactionCode> _list = new java.util.ArrayList<UserTransactionCode>();
		com.magbel.ia.vao.UserTransactionCode utc = null;
		String query = "SELECT MTID, User_Tran_Code, Description, Create_Date, Status,"
				     + " Code, USER_ID, COMPANY_CODE" + " FROM IA_USER_TRANSACTION_CODE ";

		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String uId = rs.getString("MTID");
				String userTranCode = rs.getString("User_Tran_Code");
				String description = rs.getString("Description");
				String createDate = rs.getString("Create_Date");
				String status = rs.getString("Status");
				String code = rs.getString("Code");
				String userId = rs.getString("USER_ID");
				String companyCode = rs.getString("COMPANY_CODE");
				utc =new com.magbel.ia.vao.UserTransactionCode(uId, userTranCode,code,description,
                               createDate, status, userId, companyCode);
				

				_list.add(utc);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}

	private com.magbel.ia.vao.UserTransactionCode findAUserTransactionCode(String filter) {
		// java.util.ArrayList _list = new java.util.ArrayList();
		com.magbel.ia.vao.UserTransactionCode utc = null;
		String query = "SELECT MTID,User_Tran_Code,Description,Create_Date,Status,"
				     + " Code,USER_ID,COMPANY_CODE" + " FROM IA_USER_TRANSACTION_CODE ";

		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String uId = rs.getString("MTID");
				String userTranCode = rs.getString("User_Tran_Code");
				String description = rs.getString("Description");
				String createDate = rs.getString("Create_Date");
				String status = rs.getString("Status");
				String code = rs.getString("Code");
				String userId = rs.getString("USER_ID");
				String companyCode = rs.getString("COMPANY_CODE");
				
				utc = new com.magbel.ia.vao.UserTransactionCode(uId, userTranCode,code,description,
                               createDate, status, userId, companyCode) ;
						
//		System.out.println("MTID>>>>>>>....."+uId);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return utc;

	}

	public com.magbel.ia.vao.UserTransactionCode findUserTransactionCodeByID(String uId) {
		String filter = " WHERE MTID='" + uId +"'";
		com.magbel.ia.vao.UserTransactionCode utc = findAUserTransactionCode(filter);
		return utc;

	}
	
	public com.magbel.ia.vao.UserTransactionCode findUserTransactionCodeByCode(String userTranCode) {
		String filter = " WHERE User_Tran_Code='" + userTranCode + "'";
		com.magbel.ia.vao.UserTransactionCode utc = findAUserTransactionCode(filter);
		return utc;

	}
	
	
	public java.util.List<Country> findCountryByQuery(String filter) {
		java.util.List<Country> _list = new java.util.ArrayList<Country>();
		com.magbel.ia.vao.Country country = null;
		String query = "SELECT COUNTRY_ID,code,description"
				+ "  ,home_country,status,userid,create_date,effective_date" + " FROM IA_COUNTRY ";

		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String cId = rs.getString("COUNTRY_ID");
				String code = rs.getString("code");
				String description = rs.getString("description");
				String homeCountry = rs.getString("home_country");
				String status = rs.getString("status");
				String userId = rs.getString("userid");
				String createDate = rs.getString("create_date");
				String effectiveDate = rs.getString("effective_date");
				country = new com.magbel.ia.vao.Country(cId, code,
						description, homeCountry, status, userId, createDate, effectiveDate);

				_list.add(country);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}

	private com.magbel.ia.vao.Country findACountry(String filter) {
		// java.util.ArrayList _list = new java.util.ArrayList();
		com.magbel.ia.vao.Country country = null;
		String query = "SELECT COUNTRY_ID,code,description"
				+ "  ,home_country,status,userid,create_date,effective_date" + " FROM IA_COUNTRY ";

		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String cId = rs.getString("COUNTRY_ID");
				String code = rs.getString("code");
				String description = rs.getString("description");
				String homeCountry = rs.getString("home_country");
				String status = rs.getString("status");
				String userId = rs.getString("userid");
				String createDate = rs.getString("create_date");
				String effectiveDate = rs.getString("effective_date");
				country = new com.magbel.ia.vao.Country(cId, code,
						description, homeCountry, status, userId, createDate,effectiveDate);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return country;

	}

	public com.magbel.ia.vao.Country findCountryByID(String cId) {
		String filter = " WHERE COUNTRY_ID='" + cId + "'";
		com.magbel.ia.vao.Country country = findACountry(filter);
		return country;

	}
	
	public com.magbel.ia.vao.Country findCountryByCode(String code) {
		String filter = " WHERE CODE='" + code + "'";
		com.magbel.ia.vao.Country country = findACountry(filter);
		return country;

	}

	
	

    public String[] getStatusCodes() {

            String query = "SELECT STATUS_CODE FROM AM_GB_STATUS ORDER BY STATUS_CODE";

            Connection c = null;
            ResultSet rs = null;
            PreparedStatement ps = null;
            ArrayList<String> _list = new ArrayList<String>();
            String[] list = new String[1];

            try {
             c = getConnection();
             ps = c.prepareStatement(query);
             rs = ps.executeQuery();
             while (rs.next()) {
             _list.add(rs.getString("STATUS_CODE"));

             }
            list = new String[_list.size()];
            for(int i = 0; i < list.length; i++)
            {
             list[i] = _list.get(i);

            }

             }

            catch (Exception e) {
                    e.printStackTrace();
            }

            finally {
                    closeConnection(c, ps, rs);
            }

            return list;

    }
    public String[] getStatusNames() {

            String query = "SELECT STATUS_NAME FROM AM_GB_STATUS ORDER BY STATUS_CODE";

            Connection c = null;
            ResultSet rs = null;
            PreparedStatement ps = null;
            ArrayList<String> _list = new ArrayList<String>();
            String[] list = new String[1];

            try {
                    c = getConnection();
                     ps = c.prepareStatement(query);
                     rs = ps.executeQuery();
                    while (rs.next()) {
                    _list.add(rs.getString("STATUS_NAME"));
                     }
                list = new String[_list.size()];
                for(int i = 0; i < list.length; i++)
                {
                 list[i] = _list.get(i);
                }

            }

            catch (Exception e) {
                    e.printStackTrace();
            }

            finally {
                    closeConnection(c, ps, rs);
            }

            return list;
    }
	

	
	private com.magbel.ia.vao.Camp findACamp(String filter) {
		com.magbel.ia.vao.Camp cm = null;
		String query = "SELECT Id, Code,Status"
				+ " FROM MG_CAMP";

		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String id = rs.getString("Id");
				String code = rs.getString("Code");
				String status = rs.getString("Status");
				

				cm = new com.magbel.ia.vao.Camp(id,code,status);
				cm.setId(id);
				cm.setCode(code);
				cm.setStatus(status);
			
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return cm;

	}

	public com.magbel.ia.vao.Camp findCampByID(String id) {
		com.magbel.ia.vao.Camp cm = null;

		String filter = " WHERE Id = '" + id + "'";
		cm = findACamp(filter);
		return cm;

	}

	public com.magbel.ia.vao.Camp findCampByCode(String code) {
		com.magbel.ia.vao.Camp cm = null;

		String filter = " WHERE  Code = '" + code + "'";
		cm = findACamp(filter);
		return cm;

	}

	
	public java.util.List<Camp> findCampByQuery(String filter) {
		java.util.List<Camp> _list = new java.util.ArrayList<Camp>();
		com.magbel.ia.vao.Camp cm = null;
		String query = "SELECT Id, Code,Status"
				+ " FROM MG_CAMP ";

		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String id = rs.getString("Id");
				String code = rs.getString("Code");
				String status = rs.getString("Status");
				

				cm = new com.magbel.ia.vao.Camp(id, code,status);
				_list.add(cm);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}

	public java.util.List<NoteMapping> findReportNotesByQuery(String filter) {
		java.util.List<NoteMapping> _list = new java.util.ArrayList<NoteMapping>();
		com.magbel.ia.vao.NoteMapping note = null;
		String query = "SELECT ID,Comp_Code"
				+ " ,NOTE_CODE,NOTE_DESCRIPTION,NOTE_NO"
				+ ",LEDGER_NO,REPORT_NAME,NOTE_NAME,STATUS,USER_ID"
				+ ",CREATE_DATE  FROM IA_GL_BALANCE_NOTES ";

		query = query + filter+"ORDER BY NOTE_CODE" ;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String noteId = rs.getString("ID");
				String companyCode = rs.getString("Comp_Code");
				String notecode = rs.getString("NOTE_CODE");
				String notedescription = rs.getString("NOTE_DESCRIPTION");
				String noteno = rs.getString("NOTE_NO");
				String ledgerNo = rs.getString("LEDGER_NO");
				String reportname = rs.getString("REPORT_NAME");
				String noteName = rs.getString("NOTE_NAME");
				String status = rs.getString("STATUS");
				String userid = rs.getString("USER_ID");
				String createDate = rs.getString("CREATE_DATE");
				note = new com.magbel.ia.vao.NoteMapping(noteId, companyCode,
						notecode, notedescription, noteno, ledgerNo,
						reportname, noteName, status, userid, createDate);

				_list.add(note);
			}

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " ERROR:Error Selecting branches ->" + e.getMessage());
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}
	public com.magbel.ia.vao.NoteMapping findNoteByNoteID(String noteid) {
		String filter = " WHERE ID = '" + noteid+"'";
		com.magbel.ia.vao.NoteMapping note = findANote(filter);
		return note;

	}
	private com.magbel.ia.vao.NoteMapping findANote(String filter) {
		com.magbel.ia.vao.NoteMapping note = null;
		String query =  "SELECT ID,Comp_Code"
			+ " ,NOTE_CODE,NOTE_DESCRIPTION,NOTE_NO"
			+ ",LEDGER_NO,REPORT_NAME,NOTE_NAME,STATUS,USER_ID"
			+ ",CREATE_DATE  FROM IA_GL_BALANCE_NOTES ";

		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String noteId = rs.getString("ID");
				String companyCode = rs.getString("Comp_Code");
				String notecode = rs.getString("NOTE_CODE");
				String notedescription = rs.getString("NOTE_DESCRIPTION");
				String noteno = rs.getString("NOTE_NO");
				String ledgerNo = rs.getString("LEDGER_NO");
				String reportname = rs.getString("REPORT_NAME");
				String noteName = rs.getString("NOTE_NAME");
				String status = rs.getString("STATUS");
				String userid = rs.getString("USER_ID");
				String createDate = rs.getString("CREATE_DATE");
				note = new com.magbel.ia.vao.NoteMapping(noteId, companyCode,
						notecode, notedescription, noteno, ledgerNo,
						reportname,noteName, status, userid, createDate);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return note;

	}
	public com.magbel.ia.vao.NoteMapping findNoteByNoteCode(String notecode) {
		String filter = " WHERE NOTE_CODE = '" + notecode + "'";
		com.magbel.ia.vao.NoteMapping note = findANote(filter);
		return note;

	}	
	public boolean createNoteMapping(com.magbel.ia.vao.NoteMapping note) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "INSERT INTO IA_GL_BALANCE_NOTES(ID,Comp_Code"
				+ ",NOTE_CODE,NOTE_DESCRIPTION,NOTE_NO"
				+ " ,LEDGER_NO,REPORT_NAME,NOTE_NAME,STATUS"
				+ " ,USER_ID,CREATE_DATE) "
				+ " VALUES(?,?,?,?,?,?,?,?,?,?,?)";

		try {
			con = getConnection();
		//	String noteid = helper.getGeneratedId("IA_GL_BALANCE_NOTES");
			String noteid = new ApplicationHelper().getGeneratedId("IA_GL_BALANCE_NOTES");
			ps = con.prepareStatement(query);
			ps.setString(1,noteid);
			ps.setString(2, note.getCompanyCode());
			ps.setString(3, note.getNoteCode());
			ps.setString(4, note.getNotedescription());
			ps.setString(5, note.getNoteNo());
			ps.setString(6, note.getLedgerNo());
			ps.setString(7, note.getReportName());
			ps.setString(8, note.getNoteName());
			ps.setString(9, note.getStatus());
			ps.setString(10, note.getUserId());
			ps.setDate(11, df.dateConvert(new java.util.Date()));
			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " ERROR:Error Creating Report Note ->" + e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}	
	
	public boolean updateNoteMapping(com.magbel.ia.vao.NoteMapping note) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "UPDATE IA_GL_BALANCE_NOTES SET NOTE_DESCRIPTION =?,NOTE_NO = ?,REPORT_NAME = ?"
				+ " ,NOTE_NAME = ?,LEDGER_NO = ? WHERE ID =?";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, note.getNotedescription());
			ps.setString(2, note.getNoteNo());
			ps.setString(3, note.getReportName());
			ps.setString(4, note.getNoteName());
			ps.setString(5, note.getLedgerNo());
			ps.setString(6, note.getNoteId());
			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println("WARNING:Error executing updateNoteMapping Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}		
}
