/**
 * 
 */
package legend.admin.handlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import com.magbel.util.DataConnect;

import  legend.admin.objects.*;
import legend.admin.objects.User;
import audit.*;

//import the ApplicationHelper class
 import com.magbel.util.ApplicationHelper;
import com.magbel.util.HtmlUtility;
import com.magbel.util.DatetimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import legend.admin.objects.Branch;

 //Declaration




public class AdminHandler {

	/**
	 * @Entities Department,Branch,Section,States,Category,Region,Province
	 */
	AdminHandler handler;
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
     ApplicationHelper  help ;
     // AdminHandler handler;
     HtmlUtility  htmlUtil;
      ApplicationHelper apph;


	public AdminHandler() {

		sdf = new SimpleDateFormat("dd-MM-yyyy");
		df = new com.magbel.util.DatetimeFormat();
//		System.out.println("USING_ " + this.getClass().getName());
       help = new ApplicationHelper();
       htmlUtil = new HtmlUtility();
	}


    /*
    public boolean createBranch(legend.admin.objects.Branch branch) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "INSERT INTO am_ad_branch(BRANCH_CODE,BRANCH_NAME"
				+ ",BRANCH_ACRONYM,GL_PREFIX,BRANCH_ADDRESS"
				+ " ,STATE,PHONE_NO,FAX_NO,REGION,PROVINCE"
				+ " ,BRANCH_STATUS,USER_ID,CREATE_DATE,GL_SUFFIX,BRANCH_ID) "
				+ " VALUES(?,?,? ,?,?,?,?,?,?,?,?,?,?,?,?)";

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
			ps.setLong(15,System.currentTimeMillis()); 
			done=( ps.executeUpdate()!=-1);

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " ERROR:Error Creating Branch ->" + e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}
*/
	public boolean createCategory(legend.admin.objects.Category category) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "INSERT INTO am_ad_category"
				+ "(category_code,category_name"
				+ ",category_acronym,Required_for_fleet"
				+ ",Category_Class ,PM_Cycle_Period,mileage"
				+ ",Notify_Maint_Days ,notify_every_days,residual_value"
				+ ",Dep_rate ,Asset_Ledger,Dep_ledger"
				+ ",Accum_Dep_ledger ,gl_account,insurance_acct"
				+ ",license_ledger ,fuel_ledger,accident_ledger"
				+ ",Category_Status ,user_id,create_date"
				+ ",acct_type ,currency_Id, category_id,enforceBarcode,category_Type)"
		+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			//ps.setLong(1, System.currentTimeMillis());
			ps.setString(1, category.getCategoryCode());
			ps.setString(2, category.getCategoryName()); 
			ps.setString(3, category.getCategoryAcronym());
			ps.setString(4, category.getRequiredforFleet());
			ps.setString(5, category.getCategoryClass());
			ps.setString(6, category.getPmCyclePeriod());
			ps.setString(7, category.getMileage());
			ps.setString(8, category.getNotifyMaintdays());
			ps.setString(9, category.getNotifyEveryDays());
			ps.setString(10, category.getResidualValue());
			ps.setString(11, category.getDepRate());
			ps.setString(12, category.getAssetLedger());
			ps.setString(13, category.getDepLedger());
			ps.setString(14, category.getAccumDepLedger());
			ps.setString(15, category.getGlAccount());
			ps.setString(16, category.getInsuranceAcct());
			ps.setString(17, category.getLicenseLedger());
			ps.setString(18, category.getFuelLedger());
			ps.setString(19, category.getAccidentLedger());
			ps.setString(20, category.getCategoryStatus());
			ps.setString(21, category.getUserId());
			ps.setDate(22, df.dateConvert(new java.util.Date()));
			ps.setString(23, category.getAcctType());
			ps.setString(24, category.getCurrencyId());
			ps.setLong(25, Integer.parseInt(new ApplicationHelper().getGeneratedId("am_ad_category")));
			
                        ps.setString(26, category.getEnforceBarcode());
                        ps.setString(27, category.getCategoryType());
			done=( ps.executeUpdate()!=-1);

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " ERROR:Error creating category ->" + e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

	public boolean createDepartment(legend.admin.objects.Department dept) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "INSERT INTO am_ad_department(Dept_code,Dept_name"
				+ " ,Dept_acronym,Dept_Status,user_id ,CREATE_DATE,Dept_ID)"
				+ " VALUES (?,?,?,?,?,?,?)";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, dept.getDept_code());
			ps.setString(2, dept.getDept_name());
			ps.setString(3, dept.getDept_acronym());
			ps.setString(4, dept.getDept_status());
			ps.setString(5, dept.getUser_id());
			ps.setDate(6, df.dateConvert(new java.util.Date()));
			ps.setLong(7, Integer.parseInt(new ApplicationHelper().getGeneratedId("am_ad_department")));
		done=( ps.executeUpdate()!=-1);

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " ERROR:Error creating Department ->" + e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

	public boolean createSection(legend.admin.objects.Section section) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "INSERT INTO am_ad_section(Section_ID,Section_Code,Section_Name"
				+ "  ,section_acronym,Section_Status,User_ID,Create_Date)"
				+ "  VALUES (?,?,?,?,?,?,?)";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setLong(1, Integer.parseInt(new ApplicationHelper().getGeneratedId("am_ad_section")));
			ps.setString(2, section.getSection_code());
			ps.setString(3, section.getSection_name());
			ps.setString(4, section.getSection_acronym());
			ps.setString(5, section.getSection_status());
			ps.setString(6, section.getUserid());
			ps.setDate(7, df.dateConvert(new java.util.Date()));

			done=( ps.executeUpdate()!=-1);

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " ERROR:Error creating Section ->" + e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

        /*

	public boolean createState(legend.admin.objects.State state) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "INSERT INTO am_gb_states(state_code,state_name"
				+ "  ,state_status,user_id,create_date,state_id)"
				+ "   VALUES (?,?,?,?,?))";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, state.getStateCode());
			ps.setString(2, state.getStateName());
			ps.setString(3, state.getStateStatus());
			ps.setString(4, state.getUserId());
			ps.setDate(5, df.dateConvert(new java.util.Date()));
			ps.setLong(6, Integer.parseInt(new ApplicationHelper().getGeneratedId("am_gb_states")));
			done=( ps.executeUpdate()!=-1);

		} catch (Exception e) {
			System.out.println("WARNING:Error creating State ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}
*/
	public boolean createRegion(legend.admin.objects.Region region) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "INSERT INTO AM_AD_REGION( Region_Code, Region_Name"
				+ ", Region_Acronym, Region_Address"
				+ ",Region_Phone , Region_Fax, Region_Status, User_Id,Create_Date,Region_iD) "
				+ " VALUES (?,?,?,?,?,?,?,?,?,?)";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			//ps.setLong(1, System.currentTimeMillis());
			ps.setString(1, region.getRegionCode());
			ps.setString(2, region.getRegionName());
			ps.setString(3, region.getRegionAcronym());
			ps.setString(4, region.getRegionAddress());
			ps.setString(5, region.getRegionPhone());
			ps.setString(6, region.getRegionFax());
			ps.setString(7, region.getRegionStatus());
			ps.setString(8, region.getUserId());
			ps.setDate(9, df.dateConvert(new java.util.Date()));
			ps.setLong(10, Integer.parseInt(new ApplicationHelper().getGeneratedId("AM_AD_REGION")));
			
			done=( ps.executeUpdate()!=-1);
		} catch (Exception e) {
			System.out.println("WARNING:Error creating Region ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

	public boolean createProvince(legend.admin.objects.Province ccode) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "INSERT INTO am_gb_Province"
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
			ps.setLong(6, Integer.parseInt(new ApplicationHelper().getGeneratedId("am_gb_Province")));
			done=( ps.executeUpdate()!=-1);

		} catch (Exception e) {
			System.out.println("WARNING:Error creating Province ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

    /*
	public boolean updateBranch(legend.admin.objects.Branch branch) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "UPDATE am_ad_branch SET BRANCH_CODE =?,BRANCH_NAME = ?,BRANCH_ACRONYM = ?"
				+ " ,GL_PREFIX = ?,BRANCH_ADDRESS = ?,STATE = ?"
				+ " ,PHONE_NO = ?,FAX_NO = ?,REGION = ?,PROVINCE = ?"
				+ " ,BRANCH_STATUS = ?,GL_SUFFIX = ?"
				+ " WHERE BRANCH_ID =?";

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
			ps.setString(13, branch.getBranchId());
			done=( ps.executeUpdate()!=-1);

		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}
*/
	public boolean updateCategory(legend.admin.objects.Category category) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;

		String query = "UPDATE am_ad_category"
				+ " SET category_code = ?,category_name = ?,category_acronym = ?"
				+ " , Required_for_fleet = ?,Category_Class = ?,PM_Cycle_Period = ?"
				+ " , mileage = ?,Notify_Maint_Days = ?,notify_every_days = ?"
				+ " , residual_value = ?,Dep_rate = ?,Asset_Ledger = ?"
				+ " , Dep_ledger = ?,Accum_Dep_ledger = ?,gl_account = ?"
				+ " , insurance_acct = ?,license_ledger = ?,fuel_ledger = ?"
				+ " , accident_ledger = ?,Category_Status = ?,user_id = ?"
				+ " , create_date = ?,acct_type = ?,currency_Id = ?,enforceBarcode=?,category_Type=?,maxNo_Dep_Improve=?"
				+ " WHERE category_ID =?";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);

			ps.setString(1, category.getCategoryCode());
			ps.setString(2, category.getCategoryName());
			ps.setString(3, category.getCategoryAcronym());
			ps.setString(4, category.getRequiredforFleet());
			ps.setString(5, category.getCategoryClass());
			ps.setString(6, category.getPmCyclePeriod());
			ps.setString(7, category.getMileage());
			ps.setString(8, category.getNotifyMaintdays());
			ps.setString(9, category.getNotifyEveryDays());
			ps.setString(10, category.getResidualValue());
			ps.setString(11, category.getDepRate());
			ps.setString(12, category.getAssetLedger());
			ps.setString(13, category.getDepLedger());
			ps.setString(14, category.getAccumDepLedger());
			ps.setString(15, category.getGlAccount());
			ps.setString(16, category.getInsuranceAcct());
			ps.setString(17, category.getLicenseLedger());
			ps.setString(18, category.getFuelLedger());
			ps.setString(19, category.getAccidentLedger());
			ps.setString(20, category.getCategoryStatus());
			ps.setString(21, category.getUserId());
			ps.setDate(22, df.dateConvert(new java.util.Date()));
			ps.setString(23, category.getAcctType());
			ps.setString(24, category.getCurrencyId());
                        ps.setString(25, category.getEnforceBarcode());
                    //    System.out.println("----------->--------------------------------->"+category.getCategoryType());
			ps.setString(26, category.getCategoryType());
			ps.setInt(27, category.getNoOfImproveMnth());
			//System.out.println("-----------getCategoryId>---------->"+category.getCategoryId());
			ps.setString(28, category.getCategoryId());
			//ps.setInt(28, category.getNoOfImproveMnth());
		//	System.out.println("----------->"+query);
			done=( ps.executeUpdate()!=-1);

		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}


	public boolean updateDepartment(legend.admin.objects.Department dept) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "UPDATE am_ad_department SET Dept_code = ?,Dept_name = ?"
				+ " ,Dept_acronym = ?,Dept_Status = ?"
				+ "  WHERE Dept_ID=?";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, dept.getDept_code());
			ps.setString(2, dept.getDept_name());
			ps.setString(3, dept.getDept_acronym());
			ps.setString(4, dept.getDept_status());
			ps.setString(5, dept.getDept_id());

			done=( ps.executeUpdate()!=-1);

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " ERROR:Error Updating Query ->" + e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

	public boolean updateSection(legend.admin.objects.Section section) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "UPDATE am_ad_section SET Section_Code = ?,Section_Name = ?"
				+ "  ,section_acronym = ?,Section_Status = ?"
				+ "  WHERE Section_ID = ?";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, section.getSection_code());
			ps.setString(2, section.getSection_name());
			ps.setString(3, section.getSection_acronym());
			ps.setString(4, section.getSection_status());
			ps.setString(5, section.getSection_id());

			done=( ps.executeUpdate()!=-1);

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " ERROR:Error Updating Section ->" + e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

	public boolean updateState(legend.admin.objects.State state) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "UPDATE am_gb_states SET state_code = ?,state_name = ?"
				+ "   ,state_status = ?" + "   WHERE state_ID=?";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, state.getStateCode());
			ps.setString(2, state.getStateName());
			ps.setString(3, state.getStateStatus());
			ps.setString(4, state.getStateId());

			done=( ps.executeUpdate()!=-1);

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " ERROR:Error Updating State ->" + e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

	public boolean updateRegion(legend.admin.objects.Region region) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
                /*
		String query = "UPDATE AM_AD_REGION"
				+ " SET Region_Name = ?, Region_Acronym = ?"
				+ ",Region_Address = ?,Region_Phone = ?, Region_Fax = ?"
				+ ",Region_Status = ?, User_Id = ?"
				// + ",Create_Date = ?"
				+ " WHERE Region_Code = ?";
*/
                String query = "Update AM_AD_REGION SET Region_Name=?,Region_Acronym=?,Region_Address=?," +
                        "Region_Phone=?,Region_Fax=?,Region_Status=? WHERE Region_Code=?";
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
			//ps.setString(7, region.getUserId());
			ps.setString(7, region.getRegionCode());
			done=( ps.executeUpdate()!=-1);

		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}
	

	public boolean updateZone(legend.admin.objects.Zone zone) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
                String query = "Update AM_AD_ZONE SET Zone_Name=?,Zone_Acronym=?,Zone_Address=?," +
                        "Zone_Phone=?,Zone_Fax=?,Zone_Status=? WHERE Zone_Code=?";
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, zone.getZoneName());
			ps.setString(2, zone.getZoneAcronym());
			ps.setString(3, zone.getZoneAddress());
			ps.setString(4, zone.getZonePhone());
			ps.setString(5, zone.getZoneFax());
			// ps.setDate(6,df.dateConvert(new java.util.Date()));
			ps.setString(6, zone.getZoneStatus());
			//ps.setString(7, region.getUserId());
			ps.setString(7, zone.getZoneCode());
			done=( ps.executeUpdate()!=-1);

		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query for updateZone ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}



	public boolean updateProvince(legend.admin.objects.Province ccode) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "UPDATE am_gb_Province" + " SET Province_Code = ?"
				+ ",Province = ?,Status = ?" + "  WHERE Province_ID = ?";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			// ps.setString(1, ccode.getProvinceId());
			ps.setString(1, ccode.getProvinceCode());
			ps.setString(2, ccode.getProvince());
			ps.setString(3, ccode.getStatus());
			ps.setString(4, ccode.getProvinceId());
			done=( ps.executeUpdate()!=-1);

		} catch (Exception e) {
			System.out.println("WARNING:Error Updating Province ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

	public java.util.List getBranchesByQuery(String filter,String status) {
		java.util.List _list = new java.util.ArrayList();
		legend.admin.objects.Branch branch = null;
		String query = "SELECT BRANCH_ID,BRANCH_CODE"
				+ " ,BRANCH_NAME,BRANCH_ACRONYM,GL_PREFIX"
				+ ",BRANCH_ADDRESS,STATE,PHONE_NO"
				+ ",FAX_NO,REGION_CODE,ZONE_CODE,PROVINCE"
				+ ",BRANCH_STATUS,USER_ID,GL_SUFFIX"
				+ ",CREATE_DATE  FROM am_ad_branch";

		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
//		Statement s = null;
		PreparedStatement s = null;
//		System.out.println("query in getBranchesByQuery: " + query);
//		System.out.println("Status in getBranchesByQuery: " + status);
		try {
			c = getConnection();
//			s = c.createStatement();
			 s = c.prepareStatement(query.toString());
			s.setString(1, status);
			rs = s.executeQuery();
			while (rs.next()) {
				String branchId = rs.getString("BRANCH_ID");
				String branchCode = rs.getString("BRANCH_CODE");
				String branchName = rs.getString("BRANCH_NAME");
				String branchAcronym =rs.getString("BRANCH_ACRONYM");
				String glPrefix = rs.getString("GL_PREFIX");
				String branchAddress = rs.getString("BRANCH_ADDRESS");
				String state = rs.getString("STATE");
				String phoneNo = rs.getString("PHONE_NO");
				String faxNo = rs.getString("FAX_NO");
				String province = rs.getString("PROVINCE");
				String region = rs.getString("REGION_CODE");
				String zoneCode = rs.getString("ZONE_CODE");
				String branchStatus = rs.getString("BRANCH_STATUS");
				String username = rs.getString("USER_ID");
				String glSuffix = rs.getString("GL_SUFFIX");
				String createDate = rs.getString("CREATE_DATE");
				branch = new legend.admin.objects.Branch(branchId, branchCode,
						branchName, branchAcronym, glPrefix, glSuffix,
						branchAddress, state, phoneNo, faxNo, region,zoneCode, province,
						branchStatus, username, createDate);

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



    /*
	private legend.admin.objects.Branch getABranch(String filter) {
		legend.admin.objects.Branch branch = null;
		String query = "SELECT BRANCH_ID,BRANCH_CODE"
				+ " ,BRANCH_NAME,BRANCH_ACRONYM,GL_PREFIX"
				+ ",BRANCH_ADDRESS,STATE,PHONE_NO"
				+ ",FAX_NO,REGION,PROVINCE"
				+ ",BRANCH_STATUS,USER_ID,GL_SUFFIX"
				+ ",CREATE_DATE  FROM am_ad_branch ";

		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c= getConnection();
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
				branch = new legend.admin.objects.Branch(branchId, branchCode,
						branchName, branchAcronym, glPrefix, glSuffix,
						branchAddress, state, phoneNo, faxNo, region, province,
						branchStatus, username, createDate);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return branch;

	}

     */
    
	public legend.admin.objects.Branch getBranchByBranchID(String branchid) {
		//String filter = " WHERE BRANCH_ID = " + branchid;
		legend.admin.objects.Branch branch = getABranch(branchid);
		return branch;

	}

	public legend.admin.objects.Branch getBranchByBranchCode(String branchcode) {
//		String filter = " WHERE BRANCH_CODE = '" + branchcode + "'";
		legend.admin.objects.Branch branch = getABranchCode(branchcode);
		return branch;

	}

	public java.util.List getDeparmentsByQuery(String filter,String status) {
		java.util.List _list = new java.util.ArrayList();
		legend.admin.objects.Department dept = null;
		String query = "SELECT Dept_ID,Dept_code,Dept_name"
				+ "  ,Dept_acronym,Dept_Status"
				+ " ,user_id,CREATE_DATE" + " FROM am_ad_department ";

		query = query + filter;
//		System.out.println("query in getDeparmentsByQuery "+query);
		Connection c = null;
		ResultSet rs = null;
//		Statement s = null;
		PreparedStatement s = null;
		try {
			c = getConnection();
//			s = c.createStatement();
			s = c.prepareStatement(query.toString());
	          if(query.contains("DEPT_STATUS")){
	        	  s.setString(1, status);
	          }	  
			rs = s.executeQuery();
			while (rs.next()) {
				String dept_id = rs.getString("Dept_ID");
				String dept_code = rs.getString("Dept_code");
				String dept_name = rs.getString("Dept_name");
				String dept_acronym = rs.getString("Dept_acronym");
				String dept_status = rs.getString("Dept_Status");
				String user_id = rs.getString("user_id");
				dept = new legend.admin.objects.Department(dept_id, dept_code,
						dept_name, dept_acronym, dept_status, user_id);

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

	public java.util.List getDeparmentsByQuery(String filter) {
		java.util.List _list = new java.util.ArrayList();
		legend.admin.objects.Department dept = null;
		String query = "SELECT Dept_ID,Dept_code,Dept_name"
				+ "  ,Dept_acronym,Dept_Status"
				+ " ,user_id,CREATE_DATE" + " FROM am_ad_department ";

		query = query + filter;
//		System.out.println("query in getDeparmentsByQuery "+query);
		Connection c = null;
		ResultSet rs = null;
//		Statement s = null;
		PreparedStatement s = null;
		try {
			c = getConnection();
//			s = c.createStatement();
			s = c.prepareStatement(query.toString());
			rs = s.executeQuery();
			while (rs.next()) {
				String dept_id = rs.getString("Dept_ID");
				String dept_code = rs.getString("Dept_code");
				String dept_name = rs.getString("Dept_name");
				String dept_acronym = rs.getString("Dept_acronym");
				String dept_status = rs.getString("Dept_Status");
				String user_id = rs.getString("user_id");
				dept = new legend.admin.objects.Department(dept_id, dept_code,
						dept_name, dept_acronym, dept_status, user_id);

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


	public java.util.List getDeparmentsByQuery(String filter,String status,String deptName) {
		java.util.List _list = new java.util.ArrayList();
		legend.admin.objects.Department dept = null;
		String query = "SELECT Dept_ID,Dept_code,Dept_name"
				+ "  ,Dept_acronym,Dept_Status"
				+ " ,user_id,CREATE_DATE" + " FROM am_ad_department ";

		query = query + filter;
//		System.out.println("query in getDeparmentsByQuery "+query);
		Connection c = null;
		ResultSet rs = null;
//		Statement s = null;
		PreparedStatement s = null;
		try {
			c = getConnection();
//			s = c.createStatement();
			s = c.prepareStatement(query.toString());
//			System.out.println("query.contains DEPT_STATUS in getDeparmentsByQuery "+query.contains("DEPT_STATUS"));
//			System.out.println("query.contains Dept_Name in getDeparmentsByQuery "+query.contains("Dept_Name"));
	          if(query.contains("DEPT_STATUS") && !query.contains("Dept_Name")){
	        	  s.setString(1, status);
	          }	  
	          if(query.contains("DEPT_STATUS") && query.contains("Dept_Name")){
	        	  deptName = "%"+deptName+"%";
	        	  s.setString(1, status);
	        	  s.setString(2, deptName);
	          }	  
			rs = s.executeQuery();
			while (rs.next()) {
				String dept_id = rs.getString("Dept_ID");
				String dept_code = rs.getString("Dept_code");
				String dept_name = rs.getString("Dept_name");
				String dept_acronym = rs.getString("Dept_acronym");
				String dept_status = rs.getString("Dept_Status");
				String user_id = rs.getString("user_id");
				dept = new legend.admin.objects.Department(dept_id, dept_code,
						dept_name, dept_acronym, dept_status, user_id);

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

	private legend.admin.objects.Department getADepartment(String filter, String deptCode) {
		legend.admin.objects.Department dept = null;
		String query = "SELECT Dept_ID,Dept_code,Dept_name"
				+ "  ,Dept_acronym,Dept_Status"
				+ " ,user_id,CREATE_DATE" + " FROM am_ad_department ";

		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
//		Statement s = null;
		PreparedStatement s = null;
		try {
			c = getConnection();
//			s = c.createStatement();
			s = c.prepareStatement(query.toString());
			s.setString(1, deptCode);
			rs = s.executeQuery();
			while (rs.next()) {
				String dept_id = rs.getString("Dept_ID");
				String dept_code = rs.getString("Dept_code");
				String dept_name = rs.getString("Dept_name");
				String dept_acronym = rs.getString("Dept_acronym");
				String dept_status = rs.getString("Dept_Status");
				String user_id = rs.getString("user_id");
				dept = new legend.admin.objects.Department(dept_id, dept_code,
						dept_name, dept_acronym, dept_status, user_id);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return dept;

	}

	public legend.admin.objects.Department getDeptByDeptID(String deptid) {
		String filter = " WHERE Dept_ID=? AND Dept_Status = 'ACTIVE'";
		legend.admin.objects.Department dept = getADepartment(filter,deptid);
		return dept;

	}

	public legend.admin.objects.Department getDeptByDeptCode(String deptcode) {
		String filter = " WHERE Dept_Code=? AND Dept_Status = 'ACTIVE'";
		legend.admin.objects.Department dept = getADepartment(filter,deptcode);
		return dept;

	}

	public java.util.List getSectionByQuery(String filter) {
		java.util.List _list = new java.util.ArrayList();
		legend.admin.objects.Section section = null;
		String query = "SELECT Section_ID,Section_Code,Section_Name"
				+ " ,section_acronym,Section_Status"
				+ " ,user_id,CREATE_DATE" + "  FROM am_ad_section ";

		query = query + filter;
//		System.out.println(">>>>query in getSectionByQuery: "+query);
		Connection c = null;
		ResultSet rs = null;
//		Statement s = null;
		PreparedStatement s = null;

		try {
			c = getConnection();
//			s = c.createStatement();
			s = c.prepareStatement(query);
//			s.setString(1, filter);
			rs = s.executeQuery();
			
			while (rs.next()) {
				String section_id = rs.getString("Section_ID");
				String section_code = rs.getString("Section_Code");
				String section_acromyn = rs.getString("section_acronym");
				String section_name = rs.getString("Section_Name");
				String section_status = rs.getString("Section_Status");
				String userid = rs.getString("user_id");
				section = new legend.admin.objects.Section(section_id,
						section_code, section_acromyn, section_name,
						section_status, userid);

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

	private legend.admin.objects.Section getASection(String filter) {
		legend.admin.objects.Section section = null;
		String query = "SELECT Section_ID,Section_Code,Section_Name"
				+ " ,section_acronym,Section_Status"
				+ ",user_id,CREATE_DATE" + "  FROM am_ad_section ";

		query = query + filter+" AND Section_Status = 'ACTIVE'";
//		System.out.println(">>>>query in getASection: "+query);
		Connection c = null;
		ResultSet rs = null;
//		Statement s = null;
		PreparedStatement s = null;
		try {
			c = getConnection();
//			s = c.createStatement();
			s = c.prepareStatement(query);
//			s.setString(1, filter);
			rs = s.executeQuery();
			while (rs.next()) {
				String section_id = rs.getString("Section_ID");
				String section_code = rs.getString("Section_Code");
				String section_acromyn = rs.getString("section_acronym");
				String section_name = rs.getString("Section_Name");
				String section_status = rs.getString("Section_Status");
				String userid = rs.getString("user_id");
				section = new legend.admin.objects.Section(section_id,
						section_code, section_acromyn, section_name,
						section_status, userid);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return section;

	}

	public legend.admin.objects.Section getSectionByID(String sectionid) {
		String filter = " WHERE Section_ID=" + sectionid;
		legend.admin.objects.Section section = getASection(filter);
		return section;

	}

	public legend.admin.objects.Section getSectionByCode(String sectioncode) {
		String filter = " WHERE Section_Code='" + sectioncode + "'";
		legend.admin.objects.Section section = getASection(filter);
		return section;

	}

	public java.util.List getStatesByQuery(String filter) {
		java.util.List _list = new java.util.ArrayList();
		legend.admin.objects.State state = null;
		String query = "SELECT state_ID,state_code,state_name"
				+ "  ,state_status,user_id,create_date"
				+ " FROM am_gb_states ";

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
				state = new legend.admin.objects.State(stateId, stateCode,
						stateName, stateStatus, userId, createDate);

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

	private legend.admin.objects.State getAState(String filter) {
		java.util.ArrayList _list = new java.util.ArrayList();
		legend.admin.objects.State state = null;
		String query = "SELECT state_ID,state_code,state_name"
				+ "  ,state_status,user_id,create_date"
				+ " FROM am_gb_states ";

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
				state = new legend.admin.objects.State(stateId, stateCode,
						stateName, stateStatus, userId, createDate);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return state;

	}

	public legend.admin.objects.State getStateByID(String stateid) {
		String filter = " WHERE state_ID=" + stateid;
		legend.admin.objects.State state = getAState(filter);
		return state;

	}
/*
	private legend.admin.objects.Region getARegion(String filter)
    {
        Connection c = null;
		//ResultSet rs = null;
		Statement s = null;
        PreparedStatement ps = null;

		legend.admin.objects.Region region = null;
		String query = "SELECT Region_Id, Region_Code, Region_Name"
				+ ", Region_Acronym, Region_Address"
				+ ",Region_Phone , Region_Fax, Region_Status, User_Id, Create_Date"
				+ " FROM AM_AD_REGION";

		query = query + filter;
		

		try {

           c = getConnection();
	        ps = con.prepareStatement(query);


	            rs = ps.executeQuery();








			//c = getConnection();
			//s = c.createStatement();
			//rs = s.executeQuery(query);
			while (rs.next()) {
                System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
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

				region = new legend.admin.objects.Region();
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

	public legend.admin.objects.Region getRegionByID(String RegID) {
		legend.admin.objects.Region region = null;

		String filter = "WHERE Region_Id = '" + RegID + "'";
		region = getARegion(filter);
		return region;

	}
    */
//NEW CATEGORY FROM LANRE
    private legend.admin.objects.Region getARegion(String filter) {
		legend.admin.objects.Region region = null;
		String query = "SELECT Region_Id, Region_Code, Region_Name"
				+ ", Region_Acronym, Region_Address"
				+ ",Region_Phone , Region_Fax, Region_Status, User_Id, Create_Date"
				+ " FROM AM_AD_REGION";

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

				region = new legend.admin.objects.Region();
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

	public legend.admin.objects.Region getRegionByID(String RegID) {
		legend.admin.objects.Region region = null;

		String filter = " WHERE Region_Id = "+ RegID ;
		region = getARegion(filter);
		return region;

	}


	public legend.admin.objects.Zone getZoneByID(String RegID) {
		legend.admin.objects.Zone zone = null;

		String filter = " WHERE Zone_Id = "+ RegID ;
		zone = getAZone(filter);
		return zone;

	}




	public legend.admin.objects.Region getRegionByCode(String Regcode) {
		legend.admin.objects.Region region = null;

		String filter = "WHERE  Region_Code = '" + Regcode + "'";
		region = getARegion(filter);
		return region;

	}

	public legend.admin.objects.State getStateByCode(String statecode) {
		String filter = " WHERE state_code='" + statecode + "'";
		legend.admin.objects.State state = getAState(filter);
		return state;

	}

	public legend.admin.objects.Zone getZoneByCode(String Zonecode) {
		legend.admin.objects.Zone zone = null;
		String filter = "WHERE  Zone_Code = '" + Zonecode + "'";
		zone = getAZone(filter);
		return zone;

	}	

	public java.util.List getCategoryByQuery(String filter) {
		java.util.List _list = new java.util.ArrayList();
		legend.admin.objects.Category category = null;
		String query = "SELECT category_ID,category_code,category_name"
				+ ",category_acronym,Required_for_fleet"
				+ ",Category_Class ,PM_Cycle_Period,mileage"
				+ ",Notify_Maint_Days ,notify_every_days,residual_value"
				+ ",Dep_rate ,Asset_Ledger,Dep_ledger"
				+ ",Accum_Dep_ledger ,gl_account,insurance_acct"
				+ ",license_ledger ,fuel_ledger,accident_ledger"
				+ ",Category_Status ,user_id,create_date"
				+ ",acct_type ,currency_Id,enforceBarcode" + " FROM am_ad_category  ";

		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String categoryId = rs.getString("category_ID");
				String categoryCode = rs.getString("category_code");
				String categoryName = rs.getString("category_name");
				String categoryAcronym = rs.getString("category_acronym");
				String requiredforFleet = rs.getString("Required_for_fleet");
				String categoryClass = rs.getString("Category_Class");
				String pmCyclePeriod = rs.getString("PM_Cycle_Period");
				String mileage = rs.getString("mileage");
				String notifyMaintdays = rs.getString("Notify_Maint_Days");
				String notifyEveryDays = rs.getString("notify_every_days");
				String residualValue = rs.getString("residual_value");
				String depRate = rs.getString("Dep_rate");
				String assetLedger = rs.getString("Asset_Ledger");
				String depLedger = rs.getString("Dep_ledger");
				String accumDepLedger = rs.getString("Accum_Dep_ledger");
				String glAccount = rs.getString("gl_account");
				String insuranceAcct = rs.getString("insurance_acct");
				String licenseLedger = rs.getString("license_ledger");
				String fuelLedger = rs.getString("fuel_ledger");
				String accidentLedger = rs.getString("accident_ledger");
				String categoryStatus = rs.getString("Category_Status");
				String userId = rs.getString("user_id");
				String createDate = sdf.format(rs.getDate("create_date"));
				String acctType = rs.getString("acct_type");
				String currencyId = rs.getString("currency_Id");
                                String enforeBarcode = rs.getString("enforceBarcode");
                               // int noOfImproveMnths = rs.getInt("maxNo_Dep_Improve");
				category = new legend.admin.objects.Category(categoryId,
						categoryCode, categoryName, categoryAcronym,
						requiredforFleet, categoryClass, pmCyclePeriod,
						mileage, notifyMaintdays, notifyEveryDays,
						residualValue, depRate, assetLedger, depLedger,
						accumDepLedger, glAccount, insuranceAcct,
						licenseLedger, fuelLedger, accidentLedger,
						categoryStatus, userId, createDate, acctType,
						currencyId,enforeBarcode);
				_list.add(category);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}

	private legend.admin.objects.Category getACategory(String filter) {
		legend.admin.objects.Category category = null;
		String query = "SELECT category_ID,category_code,category_name"
				+ ",category_acronym,Required_for_fleet"
				+ ",Category_Class ,PM_Cycle_Period,mileage"
				+ ",Notify_Maint_Days ,notify_every_days,residual_value"
				+ ",Dep_rate ,Asset_Ledger,Dep_ledger"
				+ ",Accum_Dep_ledger ,gl_account,insurance_acct"
				+ ",license_ledger ,fuel_ledger,accident_ledger"
				+ ",Category_Status ,user_id,create_date"
				+ ",acct_type ,currency_Id,enforceBarcode" + " FROM am_ad_category ";

		query = query + filter+" AND Category_Status = 'ACTIVE'";
	//	System.out.println("<<<<getACategory query: "+query);
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String categoryId = rs.getString("category_ID");
				String categoryCode = rs.getString("category_code");
				String categoryName = rs.getString("category_name");
				String categoryAcronym = rs.getString("category_acronym");
				String requiredforFleet = rs.getString("Required_for_fleet");
				String categoryClass = rs.getString("Category_Class");
				String pmCyclePeriod = rs.getString("PM_Cycle_Period");
				String mileage = rs.getString("mileage");
				String notifyMaintdays = rs.getString("Notify_Maint_Days");
				String notifyEveryDays = rs.getString("notify_every_days");
				String residualValue = rs.getString("residual_value");
				String depRate = rs.getString("Dep_rate");
				String assetLedger = rs.getString("Asset_Ledger");
				String depLedger = rs.getString("Dep_ledger");
				String accumDepLedger = rs.getString("Accum_Dep_ledger");
				String glAccount = rs.getString("gl_account");
				String insuranceAcct = rs.getString("insurance_acct");
				String licenseLedger = rs.getString("license_ledger");
				String fuelLedger = rs.getString("fuel_ledger");
				String accidentLedger = rs.getString("accident_ledger");
				String categoryStatus = rs.getString("Category_Status");
				String userId = rs.getString("user_id");
				String createDate = sdf.format(rs.getDate("create_date"));
				String acctType = rs.getString("acct_type");
				String currencyId = rs.getString("currency_Id");
                                String enforeBarcode = rs.getString("enforceBarcode");
				category = new legend.admin.objects.Category(categoryId,
						categoryCode, categoryName, categoryAcronym,
						requiredforFleet, categoryClass, pmCyclePeriod,
						mileage, notifyMaintdays, notifyEveryDays,
						residualValue, depRate, assetLedger, depLedger,
						accumDepLedger, glAccount, insuranceAcct,
						licenseLedger, fuelLedger, accidentLedger,
						categoryStatus, userId, createDate, acctType,
						currencyId,enforeBarcode);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return category;

	}

	private legend.admin.objects.SubCategory getASubCategory(String filter) {
		legend.admin.objects.SubCategory subcategory = null;
		String query = "SELECT sub_category_ID,sub_category_code,sub_category_name"
				+ ",sub_Category_Class,sub_Category_Status"
				+ ",Category_ID,user_id"
				+ ",create_date" + " FROM am_ad_sub_category ";

		query = query + filter+" AND sub_Category_Status = 'ACTIVE'";
		//System.out.println("getASubCategory query: "+query);
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {  
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String subcategoryId = rs.getString("sub_category_ID");
//				System.out.println("<<<<getASubCategory subcategoryId: "+subcategoryId);
				String subcategoryCode = rs.getString("sub_category_code");
				String subcategoryName = rs.getString("sub_category_name");
				String subcategoryClass = rs.getString("sub_Category_Class");
				String categoryId = rs.getString("Category_ID");
				String subcategoryStatus = rs.getString("sub_Category_Status");
				String userId = rs.getString("user_id");
				String createDate = sdf.format(rs.getDate("create_date"));
				subcategory = new legend.admin.objects.SubCategory(subcategoryId,
						subcategoryCode, subcategoryName,categoryId, 
						subcategoryStatus, userId, createDate);				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return subcategory;

	}

	public legend.admin.objects.Category getCategoryByID(String categoryid) {
		String filter = " WHERE category_ID=" + categoryid;
		legend.admin.objects.Category category = getACategory(filter);
		return category;

	}

	public legend.admin.objects.Category getCategoryByCode(String categorycode) {
		String filter = " WHERE category_code='" + categorycode + "'";
		legend.admin.objects.Category category = getACategory(filter);
		return category;

	}

	public legend.admin.objects.SubCategory getSubCategoryByCode(String subcategorycode) {
		String filter = " WHERE sub_category_code='" + subcategorycode + "'";
		legend.admin.objects.SubCategory subcategory = getASubCategory(filter);
		return subcategory;

	}
	
	public java.util.List getRegionByQuery(String filter) {
		java.util.List _list = new java.util.ArrayList();
		legend.admin.objects.Region region = null;
		String query = "SELECT Region_Id, Region_Code, Region_Name"
				+ ", Region_Acronym, Region_Address"
				+ ",Region_Phone , Region_Fax, Region_Status, User_Id, Create_Date"
				+ " FROM AM_AD_REGION ";

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

				region = new legend.admin.objects.Region(regionId, regionCode,
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

	public java.util.List getAllDeptInBranch(String branchid) {
		java.util.List _list = new java.util.ArrayList();
		legend.admin.objects.BranchDept dept = null;

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

				dept = new legend.admin.objects.BranchDept();
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
			closeConnection(con, ps,rs);
		}
		return _list;

	}

	public java.util.List getAllSectionInDept(String branchid, String deptId) {
		legend.admin.objects.DeptSection dept = null;
		java.util.ArrayList _list = new java.util.ArrayList();
		String query = "SELECT branchCode,deptCode,gl_prefix,sectionCode"
				+ ",gl_suffix,branchId,deptId,sectionId,mtid"
				+ " FROM sbu_dept_section WHERE branchId='"
				+ branchid
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

				dept = new legend.admin.objects.DeptSection();
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

	public java.util.List getProvinceByQuery(String filter) {
		java.util.List _list = new java.util.ArrayList();
		legend.admin.objects.Province province = null;
		String query = "SELECT Province_ID, Province_Code, Province"
				+ ",Status, User_id, create_date"
				+ " FROM am_gb_Province ";

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
				String createdt = df.formatDate(rs.getDate("create_date"));

				province = new legend.admin.objects.Province();
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

	private legend.admin.objects.Province getAProvince(String filter) {
		legend.admin.objects.Province province = null;
		String query = "SELECT Province_ID, Province_Code, Province"
				+ ",Status, User_id, create_date"
				+ " FROM am_gb_Province ";

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
				String createdt = df.formatDate(rs.getDate("create_date"));

				province = new legend.admin.objects.Province();
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
	public legend.admin.objects.Province getProvinceByID(String provinceid) {
		String filter = " WHERE Province_ID=" + provinceid;
		legend.admin.objects.Province province = getAProvince(filter);
		return province;

	}
	public legend.admin.objects.Province getProvinceByCode(String provincecode) {
		String filter = " WHERE Province_Code='" + provincecode+"'";
		legend.admin.objects.Province province = getAProvince(filter);
		return province;

	}
	
	public boolean removeDeptFromBranch(java.util.ArrayList list) {
		String query = "DELETE FROM sbu_branch_dept" + " WHERE branchId=?"
				+ " AND deptId=?";
		String query2 = "DELETE FROM sbu_dept_section"
				+ " WHERE branchId=?" + " AND deptId=?";

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;
		legend.admin.objects.BranchDept bd = null;
		int[] d = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps2 = con.prepareStatement(query2);
			for (int i = 0; i < list.size(); i++) {
				bd = (legend.admin.objects.BranchDept) list.get(i);

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

	public legend.admin.objects.BranchDept getDeptInBranch(String branchid,
			String deptId) {
		legend.admin.objects.BranchDept dept = null;

		String query = "SELECT branchCode,deptCode,gl_prefix"
				+ ",gl_suffix,branchId,deptId,mtid"
				+ " FROM sbu_branch_dept WHERE branchId='" + branchid
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

				dept = new legend.admin.objects.BranchDept();
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

	public legend.admin.objects.DeptSection getSectionInDept(String branchid,
			String deptId, String sectionId) {
		legend.admin.objects.DeptSection dept = null;

		String query = "SELECT branchCode,deptCode,gl_prefix,sectionCode"
				+ ",gl_suffix,branchId,deptId,sectionId,mtid"
				+ " FROM sbu_dept_section WHERE branchId=? AND deptId=? AND sectionId=?";
		//System.out.println("<<<<query in getSectionInDept: "+query);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
      	  	ps.setString(1, branchid);
      	  	ps.setString(2, deptId);
      	  	ps.setString(3, sectionId);
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

				dept = new legend.admin.objects.DeptSection();
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
			System.out.println("WARN: Error fetching all Section ->" + ex);
		} finally {
			closeConnection(con, ps);
		}
		return dept;

	}

	public boolean removeSectionsFromDept(java.util.ArrayList list) {
		String query = "DELETE FROM sbu_dept_section" + " WHERE branchId=?"
				+ " AND deptId=?" + " AND sectionId=?";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		legend.admin.objects.DeptSection bd = null;
		int[] d = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			for (int i = 0; i < list.size(); i++) {
				bd = (legend.admin.objects.DeptSection) list.get(i);

				ps.setString(1, bd.getBranchId());
				ps.setString(2, bd.getDeptId());
				ps.setString(3, bd.getSectionId());

				ps.addBatch();
			}
			d = ps.executeBatch();

		} catch (Exception ex) {
			System.out.println("WARN: Error removing Section From Department ->" + ex);
		} finally {
			closeConnection(con, ps);
		}

		return (d.length > 0);
	}

    /*
	public boolean insertDeptForBranch(java.util.ArrayList list) {

		String query = "INSERT INTO sbu_branch_dept(branchCode"
				+ ",deptCode,branchId" + ",deptId,gl_prefix"
				+ ",gl_suffix,mtid)" + " VALUES(?,?,?,?,?,?,?)";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		legend.admin.objects.BranchDept bd = null;
		int[] d = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(query);

			for (int i = 0; i < list.size(); i++) {
				bd = (legend.admin.objects.BranchDept) list.get(i);
				ps.setString(1, bd.getBranchCode());
				ps.setString(2, bd.getDeptCode());
				ps.setString(3, bd.getBranchId());
				ps.setString(4, bd.getDeptId());
				ps.setString(5, bd.getGl_prefix());
				ps.setString(6, bd.getGl_suffix());
				ps.setLong(7, System.currentTimeMillis());
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
*/
	public boolean updateDeptForBranch(java.util.ArrayList list) {
		String query = "UPDATE sbu_branch_dept SET branchCode = ?"
				+ ",deptCode = ?,gl_prefix = ?,gl_suffix = ?"
				+ " WHERE branchId=?" + " AND deptId=?";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		legend.admin.objects.BranchDept bd = null;
		int[] d = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(query);

			for (int i = 0; i < list.size(); i++) {
				bd = (legend.admin.objects.BranchDept) list.get(i);
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
		String query = "INSERT INTO sbu_dept_section(branchCode"
				+ ",deptCode,sectionCode,branchId"
				+ ",deptId,sectionId,gl_prefix" + ",gl_suffix)"
				+ " VALUES(?,?,?,?,?,?,?,?) ";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		legend.admin.objects.DeptSection bd = null;
		int[] d = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(query);

			for (int i = 0; i < list.size(); i++) {
				bd = (legend.admin.objects.DeptSection) list.get(i);
				ps.setString(1, bd.getBranchCode());
				ps.setString(2, bd.getDeptCode());
				ps.setString(3, bd.getSectionCode());
				ps.setString(4, bd.getBranchId());
				ps.setString(5, bd.getDeptId());
				ps.setString(6, bd.getSectionId());
				ps.setString(7, bd.getGl_prefix());
				ps.setString(8, bd.getGl_suffix());
//				System.out.println("<<<<getBranchCode: "+bd.getBranchCode()+"  getDeptCode: "+bd.getDeptCode()+"  getSectionCode: "+bd.getSectionCode());
//				System.out.println("<<<<<currentTimeMillis: "+System.currentTimeMillis());
//				ps.setLong(9, System.currentTimeMillis());
				ps.addBatch();
			}
			d = ps.executeBatch();
//			System.out.println("<<<<d: "+d);
		} catch (Exception ex) {
			System.out.println("WARN: Error fetching all asset ->" + ex);
		} finally {
			closeConnection(con, ps);
		}

		return (d.length > 0);
	}

	public boolean updateSectionForDept(java.util.ArrayList list) {
		String query = "UPDATE sbu_dept_section SET branchCode = ?"
				+ ",deptCode = ?,sectionCode=?,gl_prefix = ?"
				+ ",gl_suffix = ?" + " WHERE branchId=?"
				+ " AND deptId=?" + " AND sectionId=?";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		legend.admin.objects.DeptSection bd = null;
		int[] d = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			for (int i = 0; i < list.size(); i++) {
				bd = (legend.admin.objects.DeptSection) list.get(i);

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

//	private Connection getConnection() {
//		Connection con = null;
//		dc = new DataConnect("legendPlus");
//              
//		try { 
//			con = dc.getConnection();
//		} catch (Exception e) {
//			System.out.println("WARNING: Error getting connection ->"
//					+ e.getMessage());
//		}
//		return con;
//	}
	
	private Connection getConnection() {
		Connection con = null;
		try {
//        	if(con==null){
                Context initContext = new InitialContext();
                String dsJndi = "java:/legendPlus";
                DataSource ds = (DataSource) initContext.lookup(
                		dsJndi);
                con = ds.getConnection();
//        	}
		} catch (Exception e) {
			System.out.println("WARNING: Error 1 getting connection in AdminHandler ->"
					+ e.getMessage());
		}
		//finally {
//			closeConnection(con);
//		}
		return con;
	}

	
//
//	public Connection getConnection() {
//		System.out.println("About to refreshConnection connection in AdminHandler");
//		Connection con = null;
//		try {
//                Context initContext = new InitialContext();
//                String dsJndi = "java:/legendPlus";
//                DataSource ds = (DataSource) initContext.lookup(
//                		dsJndi);
//                con = ds.getConnection();
//		} catch (Exception e) {
//			System.out.println("WARNING: Error getting connection in ConnectionClass ->"
//					+ e.getMessage());
//		}
//		//finally {
////			closeConnection(con);
////		}
//		return con;
//	}
//	
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
	   public String getProperties(String sele, String[] iden, String[] vals) {
	        String html = new String();
	        if (sele == null) {
	            sele = " ";
	        }
	        for (int i = 0; i < iden.length; i++) {
	            html = html + "<option value='" + iden[i] + "' " +
	                   (iden[i].equalsIgnoreCase(sele) ? " selected " : "") + ">" +
	                   vals[i] + "</option> ";
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
	                html = html + "<option value='" + vals[i][0] + "' " +
	                       (vals[i][0].equalsIgnoreCase(sele) ? " selected " : "") +
	                       ">" + vals[i][2] + "</option> ";
	            }

	        }

	        return html;
	    }
	   public java.util.ArrayList getStateByStatus(String status){
			String filter = " WHERE state_status='" + status +"'";
			java.util.ArrayList _list = (java.util.ArrayList)getStatesByQuery(filter);
			return _list;
		} 

	public java.util.ArrayList getCategoryByStatus(String categorystatus){
			String filter = " WHERE Category_Status='" + categorystatus + "'";
			java.util.ArrayList category = (java.util.ArrayList) getCategoryByQuery(filter);
			return category;
		}
	public java.util.ArrayList getProvinceByStatus(String status){
			String filter = " WHERE Status='" + status + "'";
			java.util.ArrayList _list = (java.util.ArrayList) getProvinceByQuery(filter);
			return _list;
		}
	public java.util.ArrayList getBranchByBranchStatus(String status) {
			java.util.ArrayList _list = null;
			String filter = " WHERE BRANCH_STATUS =?";
			_list = (java.util.ArrayList) getBranchesByQuery(filter,status);
			return _list;
		}
		public java.util.ArrayList getDeptByDeptStatus(String status) {
			java.util.ArrayList _list = null;
			String filter = " WHERE Dept_Status=?";
			_list = (java.util.ArrayList)getDeparmentsByQuery(filter, status);
			return _list;

        }





public String createsave2(String sbcode, String sbname, String sbcontact, String sbstatus, String contactmail)
	{
					 Connection c = null;
					 Statement s = null;
					 ResultSet  r = null;
					 int rowcount;
					String message ="No Duplicate Found";
						try
						{
									c = getConnection();
									s = c.createStatement();
//									System.out.println("established connection...");

									r = s.executeQuery("select * from Sbu_SetUp where [Sbu_code] = '"+sbcode+"' AND [Sbu_name] ='"+sbname+"' AND [Sbu_contact]='"+sbcontact+"' AND [Status]='"+sbstatus+"' AND [Contact_email]='"+contactmail+"'");
									if(r.next())
									{
										message = "Duplicates Found";
//										System.out.println("FOUND >>>>>>>>>>>>>>>>>>>"+message);
									}


						}catch(Exception ex){ex.printStackTrace();}
						finally
						{
							closeConnection(con, s,r);
						}
						return message;
	}

public boolean SaveSbuSetup(String sbcode, String sbname, String sbcontact, String sbstatus, String contactmail)
	{
					boolean done = false;
					 Connection con = null;
					 Statement s = null;
					 ResultSet  r = null;
					 PreparedStatement ps = null;
					 String query = "INSERT INTO Sbu_SetUp"
							+ "(Sbu_code, Sbu_name"
							+ ",Sbu_contact, Status,Contact_email)"
							+ " VALUES (?,?,?,?,?)";

					//int rowcount;
					//String message = "";
						try
						{
									con = getConnection();
//									System.out.println("established connection...");
									ps = con.prepareStatement(query);
									ps.setString(1, sbcode);
									ps.setString(2, sbname);
									ps.setString(3, sbcontact);
									ps.setString(4, sbstatus);
									ps.setString(5, contactmail);
									done=(ps.executeUpdate()!=-1);
//									System.out.println("output "+done);

						}catch(Exception ex){ex.printStackTrace();}
						finally
						{
							closeConnection(con, ps);
						}
						return done;
	}

public String UpdateSbu(String sbucode,String sbuname, String sbucontact, String status, String contactmail)
		{
					System.out.print(sbucode);
					System.out.print(sbuname);
					System.out.print(sbucontact);
					System.out.print(status);



					String mess ="";
					Connection con = null;
					Statement s = null;
					PreparedStatement ps = null;
					ResultSet rs = null;


						try
						{
								con = getConnection();

								    String updatequery = "UPDATE Sbu_SetUp SET Sbu_name =?,Sbu_contact =?,Status = ?,Contact_email= ? WHERE Sbu_code=?";
									ps = con.prepareStatement(updatequery);
									ps.setString(1,sbuname);
//									System.out.print("wetin dey happen self"+sbuname);
									ps.setString(2,sbucontact);
//									System.out.print("wetin dey happen self"+sbucontact);
									ps.setString(3,status);
//									System.out.print("wetin dey happen self"+status);
									ps.setString(4,contactmail);
									ps.setString(5,sbucode);

//									System.out.print("wetin dey happen self"+sbucode);
//									System.out.print("wetin dey happen self checkpoint 1");
									int cnt=ps.executeUpdate();
									mess = "Success_update";
//									System.out.print("wetin dey happen self checkpoint 2"+mess);
									if(cnt>0)
									{
										mess = "Success_update";
//										System.out.println("*********************"+mess);
									}


						}catch(Exception ex)
						{
								ex.printStackTrace();
						}
						finally
						{
							closeConnection(con, ps);
						}

						return mess;

		}
public String UpdateMailStatement(String mailcode,String maildescription, String mailaddress, String createdate, String transactiontype, String userid, String status)
		{
					System.out.print(mailcode);
					System.out.print(maildescription);
					System.out.print(mailaddress);
					System.out.print(createdate);

                    handler = new AdminHandler();

					String mess ="";
					Connection con = null;
					Statement s = null;
					PreparedStatement ps = null;
					ResultSet rs = null;


						try
						{
								con = getConnection();

								    String updatequery = "UPDATE am_mail_statement SET Mail_description=?, Mail_address = ?,Create_date= ? , Transaction_type=?, User_id =?, Status = ? WHERE Mail_code=?";
									ps = con.prepareStatement(updatequery);
									ps.setString(1,maildescription);
//									System.out.print("wetin dey happen self"+maildescription);
									ps.setString(2,mailaddress);
//									System.out.print("wetin dey happen self"+mailaddress);
									ps.setString(3,createdate);
//									System.out.print("wetin dey happen self"+createdate);
									ps.setString(4,transactiontype);
									ps.setString(5,userid);
									ps.setString(6,status);
									ps.setString(7,mailcode);

//									System.out.print("wetin dey happen self"+mailcode);
//									System.out.print("wetin dey happen self checkpoint 1");
									int cnt=ps.executeUpdate();
									mess = "Success_update";
//									System.out.print("wetin dey happen self checkpoint 2"+mess);
									if(cnt>0)
									{
										mess = "Success_update";
//										System.out.println("*********************"+mess);
									}


						}catch(Exception ex)
						{
								ex.printStackTrace();
						}
						finally
						{
							closeConnection(con, ps);
						}

						return mess;

		}



public java.util.List getsbuByQuery(String filter) {
		java.util.List _list = new java.util.ArrayList();
		legend.admin.objects.Sbu_branch sbu = null;
		String query = "SELECT DISTINCT Sbu_code,Sbu_code+'-'+Sbu_name as Sbu_name,Sbu_contact"
				+ "  ,Status "
				+ " ,Contact_email" + " FROM Sbu_SetUp ";

		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String code = rs.getString("Sbu_code");
				String name = rs.getString("Sbu_name");
				String contact = rs.getString("Sbu_contact");
				String status = rs.getString("Status");
				String email= rs.getString("Contact_email");
				//String user_id = rs.getString("user_id");
				sbu = new legend.admin.objects.Sbu_branch(code, name,
						contact, status, email);

				_list.add(sbu);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}

/*
public legend.admin.objects.AssignSbu getClassPrivilegeSbu(String sbuName) {
	legend.admin.objects.AssignSbu classprivilege = null;

	String query = "Select MTID,ATTACH_ID,SBU_NAME,GL_PREFIX,GL_SUFIX,CREATE_DATE,CREATE_USER,SBU_CODE from AM_SBU_ATTACHEMENT WHERE SBU_NAME = ?";
	//String query1 = "SELECT Sbu_code,Sbu_name,Sbu_contact,Status,Contact_email  FROM Sbu_SetUp WHERE Sbu_name= ?";

	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	//ApplicationHelper help = new ApplicationHelper();
	try {
		con = getConnection();
		ps = con.prepareStatement(query);

		ps.setString(1, sbuName);
		//ps.setString(2, roleuuid);
		rs = ps.executeQuery();
		while (rs.next()) {
		//	 String id = help.getGeneratedId("AM_SBU_ATTACHEMENT");


			String id = rs.getString("MTID");
			String branchName = rs.getString("ATTACH_ID");

			String branchAcronym = rs.getString("SBU_NAME");

			String glPrefix = rs.getString("GL_PREFIX");

			String userid = rs.getString("GL_SUFIX");

			String date = rs.getString("CREATE_DATE");

			String createuser = rs.getString("CREATE_USER");

			String sbucode = rs.getString("SBU_CODE");


			//System.out.println(branchCode);


			 classprivilege = new legend.admin.objects.AssignSbu();
			System.out.println("11");

			classprivilege.setMitd(id);
			classprivilege.setAttachid(branchName);
			classprivilege.setSbuname(branchAcronym);
			classprivilege.setGlprifix(glPrefix);
			classprivilege.setGlsurfix(userid);
			classprivilege.setCreatedate(date);
			classprivilege.setCreateuser(createuser);
			classprivilege.setSbucode(sbucode);


		}
	} catch (Exception ex) {
		System.out.println("WARN: Error fetching Class Privileges ->" + ex);
	} finally {
		closeConnection(con, ps);
	}
	return classprivilege;
}

*/

public legend.admin.objects.AssignSbu getSbuPrivilege(String classid) {
	legend.admin.objects.AssignSbu classprivilege = null;

	String query = "SELECT SBU_NAME,GL_PREFIX,GL_SUFIX"
			+ " FROM AM_SBU_ATTACHEMENT"
			+ " WHERE SBU_NAME=? ";

	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	try {
		con = getConnection();
		ps = con.prepareStatement(query);
		ps.setString(1, classid);
		//ps.setString(2, roleuuid);
		rs = ps.executeQuery();
		while (rs.next()) {
			String clss_uuid = rs.getString("SBU_NAME");

			String role_uuid = rs.getString("GL_PREFIX");

			String role_view = rs.getString("GL_SUFIX");

			//String role_addn = rs.getString("role_addn");

			//String role_edit = rs.getString("role_edit");
			//classprivilege = new legend.admin.objects.AssignSbu(
					//clss_uuid, role_uuid, role_view);
		}
	} catch (Exception ex) {
		System.out.println("WARN: Error fetching Class Privileges ->" + ex);
	} finally {
		closeConnection(con, ps);
	}
	return classprivilege;
}


public boolean insertAssignSbuPrivileges2(java.util.ArrayList list) {



	 String query = "INSERT INTO AM_SBU_ATTACHEMENT"
				+ "(MTID, ATTACH_ID"
				+ ",SBU_NAME,GL_PREFIX,GL_SUFIX,CREATE_DATE,CREATE_USER,SBU_CODE)"
				+ " VALUES (?,?,?,?,?,?,?,?)";




			Connection con = null;
			PreparedStatement ps = null;
			ResultSet rs = null;



	        legend.admin.objects.AssignSbu cp = null;
	        legend.admin.objects.User user = null;
		    ApplicationHelper help = new ApplicationHelper();


			int[] d = null;
			try {
				con = getConnection();
				ps = con.prepareStatement(query);
	                        String id = help.getGeneratedId("AM_SBU_ATTACHEMENT");
				for (int i = 0; i < list.size(); i++) {
					cp = (legend.admin.objects.AssignSbu) list.get(i);

					user = new legend.admin.objects.User();
                   // int branchID = getBranchIDforSBU(cp.getAttachid());
				ps.setString(1, id);
//				System.out.println("james bon1");
				ps.setString(2,cp.getAttachid());
//				System.out.println("james bon2");
				ps.setString(3, cp.getSbuname());
//				System.out.println("james bon3");
				ps.setString(4, cp.getGlprifix());
//				System.out.println("james bon4");
				ps.setString(5, cp.getGlsurfix());
//				System.out.println("james bon5");
				ps.setDate(6, df.dateConvert(new java.util.Date()));
//				System.out.println("james bon6");
				ps.setString(7, user.getUserId());
//				System.out.println("james bon7");
				ps.setString(8, cp.getSbucode());
//				System.out.println("james bon8");
				
                //ps.setInt(9, branchID);
                    //System.out.println("the value of branch id from am_sbu_attach is.........." + branchID);
                ps.addBatch();
//				System.out.println("james bon9");
				
                //int branchID = getBranchIDforSBU(cp.getAttachid());

                }
				d = ps.executeBatch();
//				System.out.println("james bon10");
			} catch (Exception ex) {
				System.out.println("WARN: Error fetching all asset ->" + ex);
			} finally {
				closeConnection(con, ps);
			}
			return (d.length > 0);
		}


public boolean updateAvaliableSbu(java.util.ArrayList list) {
	String query = "UPDATE AM_SBU_ATTACHEMENT SET ATTACH_ID=?"
		+",SBU_NAME= ?,GL_PREFIX = ?,GL_SUFIX = ?,CREATE_DATE =?,CREATE_USER =?,SBU_CODE=?"
			+ " WHERE SBU_NAME=?";
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	legend.admin.objects.AssignSbu bd = null;
	int[] d = null;
	try {
		con = getConnection();
		ps = con.prepareStatement(query);

		for (int i = 0; i < list.size(); i++) {
			bd = (legend.admin.objects.AssignSbu) list.get(i);
//			System.out.println("*********b4************");
/*			System.out.println(bd.getAttachid());
			System.out.println(bd.getSbuname());
			System.out.println(bd.getGlprifix());
			System.out.println(bd.getGlsurfix());

			System.out.println(bd.getCreateuser());
			System.out.println(bd.getSbucode());
			System.out.println(bd.getMitd());
			System.out.println("*********f4*************");
			*/
			ps.setString(1, bd.getAttachid());
			ps.setString(2, bd.getSbuname());
			ps.setString(3, bd.getGlprifix());
			ps.setString(4, bd.getGlsurfix());
			ps.setDate(5, df.dateConvert(new java.util.Date()));
			ps.setString(6, bd.getCreateuser());
			ps.setString(7, bd.getSbucode());
			ps.setString(8, bd.getSbuname());

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
public boolean updateAvaliableSbu(java.util.ArrayList list,int userid, String branchCode,int loginId,String eff_date) {
	String query = "UPDATE AM_SBU_ATTACHEMENT SET ATTACH_ID=?"
		+",SBU_NAME= ?,GL_PREFIX = ?,GL_SUFIX = ?,CREATE_DATE =?,CREATE_USER =?,SBU_CODE=?"
			+ " WHERE SBU_NAME=?";
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	legend.admin.objects.AssignSbu bd = null;
	int[] d = null;
	try {
		con = getConnection();
		ps = con.prepareStatement(query);

		for (int i = 0; i < list.size(); i++) {
			bd = (legend.admin.objects.AssignSbu) list.get(i);
			//System.out.println("*********b4************");
//			System.out.println(bd.getAttachid());
//			System.out.println(bd.getSbuname());
//			System.out.println(bd.getGlprifix());
//			System.out.println(bd.getGlsurfix());
//
//			System.out.println(bd.getCreateuser());
//			System.out.println(bd.getSbucode());
//			System.out.println(bd.getMitd());
			//System.out.println("*********f4*************");
			ps.setString(1, bd.getAttachid());
			ps.setString(2, bd.getSbuname());
			ps.setString(3, bd.getGlprifix());
			ps.setString(4, bd.getGlsurfix());
			ps.setDate(5, df.dateConvert(new java.util.Date()));
			ps.setString(6, bd.getCreateuser());
			ps.setString(7, bd.getSbucode());
			ps.setString(8, bd.getSbuname());

			//compareAuditValues(bd.getGl_prefix(),bd.getGl_suffix(),bd.getBranchId(),bd.getDeptCode(),String.valueOf(userid),branchCode,loginId,eff_date);
			compareAuditValuesSbu(bd.getGlprifix(),bd.getGlsurfix(),bd.getAttachid(),bd.getSbucode(),String.valueOf(userid),branchCode,loginId,eff_date);

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

/*
public boolean removeAvaliableSbu(java.util.ArrayList list) {
	String query = "DELETE FROM AM_SBU_ATTACHEMENT" + " WHERE Sbu_Code=?"
			+ " AND Sbu_name=?";
	String query2 = "DELETE FROM Sbu_SetUp"
			+ " WHERE Sbu_Code=?" + " AND Sbu_name=?";

	Connection con = null;
	PreparedStatement ps = null;
	PreparedStatement ps2 = null;
	ResultSet rs = null;
	legend.admin.objects.AssignSbu bd = null;
	int[] d = null;
	try {
		con = getConnection();
		ps = con.prepareStatement(query);
		ps2 = con.prepareStatement(query2);
		for (int i = 0; i < list.size(); i++) {
			bd = (legend.admin.objects.AssignSbu) list.get(i);

			ps.setString(1, bd.getSbucode());
			ps.setString(2, bd.getSbuname());

			ps.addBatch();
			ps2.setString(1, bd.getSbucode());
			ps2.setString(2, bd.getSbuname());
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


*/

public String getBranchesByQuery2()
	{
		String cv = "";

		String query = "SELECT Sbu_code,Sbu_name,Sbu_contact, Status, Contact_email  FROM Sbu_SetUp" ;


		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while(rs.next())
			{

				cv = rs.getString("Sbu_code");

			}

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " Error Selecting branches ->" + e.getMessage());
//			System.out.print("i don no waiting dey happenn o!");
		}

		finally {
			closeConnection(c, s, rs);
		}
		return cv;

	}


public java.util.ArrayList getBranchesByQuery2(String filter)
	{
		java.util.ArrayList _list = new java.util.ArrayList();
		legend.admin.objects.Sbu_branch branch = null;
		String query = "SELECT Sbu_code,Sbu_name,Sbu_contact, Status, Contact_email FROM Sbu_SetUp"  + filter;


		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while(rs.next())
			{
//				System.out.println("i dey here o! 333333333");
				String branchCode = rs.getString("Sbu_code");
				String branchName = rs.getString("Sbu_name");
				String branchAcronym = rs.getString("Sbu_contact");
				String glPrefix = rs.getString("Status");
				String email = rs.getString("Contact_email");
//				System.out.print("joshua !");
				branch = new legend.admin.objects.Sbu_branch(branchCode, branchName, branchAcronym, glPrefix,email);

				_list.add(branch);

			}

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " Error Selecting branches ->" + e.getMessage());
		//	System.out.print("i don no waiting dey happenn o!");
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}


public java.util.ArrayList getBranchesBysbucode(String filter)
	{
		java.util.ArrayList _list = new java.util.ArrayList();
		legend.admin.objects.Sbu_branch branch = null;
		String query = "SELECT Sbu_code,Sbu_name,Sbu_code ,Contact_email FROM Sbu_SetUP where Sbu_code ='"+filter+"'";


		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while(rs.next())
			{
//				System.out.println("i dey here o! 333333333");
				String branchCode = rs.getString("Sbu_code");
				String branchName = rs.getString("Sbu_name");
				String branchAcronym = rs.getString("Sbu_contact");
				String glPrefix = rs.getString("Status");
				String mail = rs.getString("Contact_email");
//				System.out.print("joshua !");
				branch = new legend.admin.objects.Sbu_branch(branchCode, branchName, branchAcronym, glPrefix,mail);

				_list.add(branch);

			}

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " Error Selecting branches ->" + e.getMessage());
//			System.out.print("i don no waiting dey happenn o!");
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}
public java.util.ArrayList getApprovalByQuery2(String filter)
	{
		java.util.ArrayList _list = new java.util.ArrayList();
		legend.admin.objects.Aproval_limit branch = null;
		String query = "Select * from Approval_Limit"+filter;
		//System.out.println(""+query);


		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while(rs.next())
			{
//				System.out.println("i dey here o! 333333333");
				String branchCode = rs.getString("Level_Code");
//				System.out.println(branchCode);
				double branchName = rs.getDouble("Min_Amount");
//				System.out.println(branchName);
				double branchAcronym = rs.getDouble("Max_Amount");
//				System.out.println(branchAcronym);
				String glPrefix = rs.getString("Description");
//				System.out.println(glPrefix);

//				System.out.print("joshua  na u bi dis...!");

				Aproval_limit condition = new Aproval_limit(branchCode,branchName,branchAcronym,glPrefix);
				//condition.setCode(branchCode);
				//condition.setMinAmt(branchName);
				//condition.setMaxAmt(branchAcronym);
				//condition.setDesc(glPrefix);

//				System.out.println("Flash me d object >>>>>>>>>>>>>"+condition);
				_list.add(condition);
//				System.out.println("e dey work na .....JOSHUA");

			}

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " Error Selecting branches ->" + e.getMessage());
			System.out.print("i don no waiting dey happenn o!");
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}
public java.util.ArrayList getApprovalByQuery2()
	{
		java.util.ArrayList _list = new java.util.ArrayList();
		legend.admin.objects.Aproval_limit branch = null;
		String query = "Select * from Approval_Limit ";
		//System.out.println(""+query);


		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while(rs.next())
			{
//				System.out.println("i dey here o! 333333333");
				String branchCode = rs.getString("Level_Code");
//				System.out.println(branchCode);
				double branchName = rs.getDouble("Min_Amount");
//				System.out.println(branchName);
				double branchAcronym = rs.getDouble("Max_Amount");
//				System.out.println(branchAcronym);
				String glPrefix = rs.getString("Description");
//				System.out.println(glPrefix);

//				System.out.print("joshua  na u bi dis...!");

				Aproval_limit condition = new Aproval_limit(branchCode,branchName,branchAcronym,glPrefix);
				//condition.setCode(branchCode);
				//condition.setMinAmt(branchName);
				//condition.setMaxAmt(branchAcronym);
				//condition.setDesc(glPrefix);

//				System.out.println("Flash me d object >>>>>>>>>>>>>"+condition);
				_list.add(condition);
//				System.out.println("e dey work na .....JOSHUA");

			}

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " Error Selecting branches ->" + e.getMessage());
//			System.out.print("i don no waiting dey happenn o!");
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}
    public String Approval_Duplicate(String sbcode, double  min, double max, String des)
		{
						 Connection c = null;
						 Statement s = null;
						 ResultSet  r = null;
						 int rowcount;
						String message ="No Duplicate Found";
							try
							{
										c = getConnection();
										s = c.createStatement();
										r = s.executeQuery("select * from Approval_Limit where [Level_Code] = '"+sbcode+"' AND [Min_Amount] ='"+min+"' AND [Max_Amount]='"+max+"' AND [Description]='"+des+"'");
								//		System.out.println("i don't know if it is working parker .....");
										if(r.next())
										{
								//			System.out.println("i don't know if it is working parker 2 .....");
											message = "Duplicates Found";
									//		System.out.println("FOUND >>>>>>>>>>>>>>>>>>>"+message);
											
										}else{
											message ="Message can not be display....";
										}
										
								
							}catch(Exception ex){ex.printStackTrace();}
							finally
							{
								closeConnection(con, s,r);
							}
							return message;
		}
        public String Approval_Level_Duplicate(String code, String trans_type, String level, String date, String userid)
		{
						 Connection c = null;
						 Statement s = null;
						 ResultSet  r = null;
						 int rowcount;
						String message ="No Duplicate Found";
							try
							{
										c = getConnection();
										s = c.createStatement();
						//				System.out.println("established connection...");

										r = s.executeQuery("select * from Approval_Level_setup where [Code] = '"+code+"' AND [Transaction_type] ='"+trans_type+"' AND [Level]='"+level+"' AND [Date]='"+date+"' AND [User_id]='"+userid+"'");
										if(r.next())
										{
											message = "Duplicates Found";
										//	System.out.println("FOUND >>>>>>>>>>>>>>>>>>>"+message);
										}
										
								
							}catch(Exception ex){ex.printStackTrace();}
							finally
							{
								closeConnection(con, s,r);
							}
							return message;
		}
        public boolean isApprovalExisting(String code) {
 	boolean done=false;
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;


    String FINDER_QUERY = "SELECT Level_Code,Min_Amount,Max_Amount,Description from Approval_Limit WHERE Level_Code = ?";

    try {
        con = getConnection();
        ps = con.prepareStatement(FINDER_QUERY);
        ps.setString(1, code);
       // ps.setString(2,page);
        rs = ps.executeQuery();
   //     System.out.println("Yes o...............!");

        while (rs.next())
        {
           done=true;
        }

    } catch (Exception ex) {
        System.out.println("WARNING: cannot fetch [am_raisentry_post_josh]->" +
                ex.getMessage());
    } finally {
        closeConnection(con, ps, rs);
    }

    return done;

}

  /*
        public boolean SaveApproval_level(String code, String trans_type, String  level, String date,String userid)
{
				boolean done = false;
				 Connection con = null;
				 Statement s = null;
				 ResultSet  r = null;
				 PreparedStatement ps = null;
				 String query = "INSERT INTO Approval_Level_setup"
						+ "(code, Transaction_type"
						+ ",Level,Date,User_id)"
						+ " VALUES (?,?,?,?,?)";

				//int rowcount;
				//String message = "";
					try
					{
								con = getConnection();
								System.out.println("established connection...");
								ps = con.prepareStatement(query);
								ps.setString(1, code);
								ps.setString(2, trans_type);
								ps.setString(3, level);
								ps.setDate(4, df.dateConvert(new java.util.Date()));
								ps.setString(5,userid);

								done=(ps.executeUpdate()!=-1);
								System.out.println("output "+done);

					}catch(Exception ex){ex.printStackTrace();}
					finally
					{
						closeConnection(con, ps);
					}
					return done;
}

*/
        public boolean isApproval_Level_Existing(String codee) {
 	boolean done=false;
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;


    String FINDER_QUERY = "SELECT Code,Transaction_type,Level from Approval_Level_setup where Code = ?";

    try {
        con = getConnection();
        ps = con.prepareStatement(FINDER_QUERY);
        ps.setString(1, codee);
       // ps.setString(2,Transaction_Type);
       // ps.setString(3,Transaction_Type);
        rs = ps.executeQuery();
 //       System.out.println("Yes o...............!");

        while (rs.next())
        {
           done=true;
        }

    } catch (Exception ex) {
        System.out.println("WARNING: cannot fetch [am_raisentry_post_josh]->" +
                ex.getMessage());
    } finally {
        closeConnection(con, ps, rs);
    }

    return done;

}
public String UpdateApproval_Limit(String code,double min_amount, double max_amount,  String describ)
		{
					System.out.print(code);
					System.out.print(min_amount);
					System.out.print(max_amount);
					System.out.print(describ);



					String mess ="";
					Connection con = null;
					Statement s = null;
					PreparedStatement ps = null;
					ResultSet rs = null;


						try
						{
								con = getConnection();

								    String updatequery = "UPDATE Approval_Limit SET Min_Amount=?, Max_Amount=?,Description=? WHERE Level_Code=?";
									ps = con.prepareStatement(updatequery);
									ps.setDouble(1, min_amount);
//									System.out.print("wetin dey happen self"+min_amount);
									ps.setDouble(2,max_amount);
//									System.out.print("wetin dey happen self"+max_amount);
									//ps.setDate(4, df.dateConvert(new java.util.Date()));
									//System.out.print("wetin dey happen self"+createdate);
									ps.setString(3,describ);
									ps.setString(4,code);

//									System.out.print("wetin dey happen self____"+code);
//									System.out.print("wetin dey happen self checkpoint 1");
									int cnt=ps.executeUpdate();
									mess = "Success_update";
//									System.out.print("wetin dey happen self checkpoint 2"+mess);
									if(cnt>0)
									{
										mess = "Success_update";
//										System.out.println("*********************"+mess);
									}


						}catch(Exception ex)
						{
								ex.printStackTrace();
						}
						finally
						{
							closeConnection(con, ps);
						}

						return mess;

		}
public java.util.ArrayList getApprovalLevelByQueryGprifix()
{
	java.util.ArrayList _list = new java.util.ArrayList();
	legend.admin.objects.AssignSbu branch = null;
	String query = "Select * from AM_SBU_ATTACHEMENT ";
	//System.out.println(""+query);


	Connection c = null;
	ResultSet rs = null;
	Statement s = null;

	try {
		c = getConnection();
		s = c.createStatement();
		rs = s.executeQuery(query);
		while(rs.next())
		{
			 String id = help.getGeneratedId("AM_SBU_ATTACHEMENT");
//			System.out.println("i dey here o! 333333333");
			String branchCode = rs.getString("MTID");
//			System.out.println(branchCode);
			String branchName = rs.getString("ATTACH_ID");
//			System.out.println(branchName);
			String branchAcronym = rs.getString("SBU_NAME");
//			System.out.println(branchAcronym);
			String glPrefix = rs.getString("GL_PREFIX");
//			System.out.println(glPrefix);
			String userid = rs.getString("GL_SUFIX");
//			System.out.println(userid);
			String date = rs.getString("CREATE_DATE");
//			System.out.println(userid);
			String createuser = rs.getString("CREATE_USER");
//			System.out.println(userid);
			String sbucode = rs.getString("SBU_CODE");
//			System.out.println(userid);
			int y = 6;
			int u =0;
			int t =(y < u)? 6 : 9;

//			System.out.print("joshua  na u bi dis...!");

			AssignSbu condition = new AssignSbu();
			condition.setSbuname(branchAcronym);
			condition.setGlprifix(glPrefix);
			condition.setGlsurfix(userid);
			//condition.setDesc(glPrefix);

//			System.out.println("Flash me d object >>>>>>>>>>>>>"+condition);
			_list.add(condition);
//			System.out.println("e dey work na .....JOSHUA");

		}

	} catch (Exception e) {
		System.out.println(this.getClass().getName()
				+ " Error Selecting branches ->" + e.getMessage());
		System.out.print("i don no waiting dey happenn o!");
	}

	finally {
		closeConnection(c, s, rs);
	}
	return _list;

}
public java.util.ArrayList getApprovalLevelByQuery(String filter)
	{
		java.util.ArrayList _list = new java.util.ArrayList();
		legend.admin.objects.Aproval_limit branch = null;
		String query = "Select * from Approval_Level_setup "+filter;
		//System.out.println(""+query);


		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while(rs.next())
			{
	//			System.out.println("i dey here o! 333333333");
				String branchCode = rs.getString("Code");
	//			System.out.println(branchCode);
				String branchName = rs.getString("Transaction_type");
	//			System.out.println(branchName);
				String branchAcronym = rs.getString("Level");
	//			System.out.println(branchAcronym);
				String glPrefix = rs.getString("Date");
	//			System.out.println(glPrefix);
				String userid = rs.getString("User_id");
	//			System.out.println(userid);

	//			System.out.print("joshua  na u bi dis...!");

				Approval_Level condition = new Approval_Level(branchCode,branchName,branchAcronym,glPrefix,userid);
				//condition.setCode(branchCode);
				//condition.setMinAmt(branchName);
				//condition.setMaxAmt(branchAcronym);
				//condition.setDesc(glPrefix);

//				System.out.println("Flash me d object >>>>>>>>>>>>>"+condition);
				_list.add(condition);
//				System.out.println("e dey work na .....JOSHUA");

			}

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " Error Selecting branches ->" + e.getMessage());
			System.out.print("i don no waiting dey happenn o!");
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}

public java.util.ArrayList getApprovalLevelByQuery()
	{
		java.util.ArrayList _list = new java.util.ArrayList();
		legend.admin.objects.Aproval_limit branch = null;
		String query = "Select * from Approval_Level_setup order by Transaction_type ";
		//System.out.println(""+query);


		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while(rs.next())
			{
	//			System.out.println("i dey here o! 333333333");
				String branchCode = rs.getString("Code");
	//			System.out.println(branchCode);
				String branchName = rs.getString("Transaction_type");
	//			System.out.println(branchName);
				String branchAcronym = rs.getString("Level");
	//			System.out.println(branchAcronym);
				String glPrefix = rs.getString("Date");
	//			System.out.println(glPrefix);
				String userid = rs.getString("User_id");
	//			System.out.println(userid);

	//			System.out.print("joshua  na u bi dis...!");

				Approval_Level condition = new Approval_Level(branchCode,branchName,branchAcronym,glPrefix,userid);
				//condition.setCode(branchCode);
				//condition.setMinAmt(branchName);
				//condition.setMaxAmt(branchAcronym);
				//condition.setDesc(glPrefix);

	//			System.out.println("Flash me d object >>>>>>>>>>>>>"+condition);
				_list.add(condition);
	//			System.out.println("e dey work na .....JOSHUA");

			}

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " Error Selecting branches ->" + e.getMessage());
			//System.out.print("i don no waiting dey happenn o!");
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}

public java.util.ArrayList getBranchesByMail(String filter)
	{
		java.util.ArrayList _list = new java.util.ArrayList();
		legend.admin.objects.mail_setup branch = null;
		String query = "SELECT Mail_code,Mail_description,Mail_address,Creation_date, Transaction_type," +
                        "User_id,Status FROM am_mail_statement "+ filter;


		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while(rs.next())
			{
//				System.out.println("i dey here o! jjoshua");
				String branchCode = rs.getString("Mail_code");
				String branchName = rs.getString("Mail_description");
				String branchAcronym = rs.getString("Mail_address");
				String glPrefix = rs.getString("Creation_date");
//				System.out.println("coming to mama ...."+glPrefix );
				String email = rs.getString("Transaction_type");
				String userid = rs.getString("User_id");
				String status= rs.getString("Status");
//				System.out.print("joshua !");
				branch = new legend.admin.objects.mail_setup(branchCode, branchName, branchAcronym, glPrefix,email,userid,status);

				_list.add(branch);

			}

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " Error Selecting branches ->" + e.getMessage());
//			System.out.print("i don no waiting dey happenn o! i want to know");
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}



public String CheckMailSetup(String mailcode, String description, String address, String date, String trans_type, String userid, String status)
	{
					 Connection c = null;
					 Statement s = null;
					 ResultSet  r = null;
					 int rowcount;
					String message ="No Duplicate Found";
						try
{
    c = getConnection();
    s = c.createStatement();
    //System.out.println("established connection...");

    r = s.executeQuery("select * from am_mail_statement_view where [Mail_code]='"+mailcode+"' AND [Mail_description]" +
            "='"+description+"' AND [Mail_address]='"+address+"' AND [Create_date]='"+date+"' AND" +
            " [Transaction_type]='"+trans_type+"' AND [User_id]='"+userid+"' AND [Status] ='"+status+"'");
    if(r.next())
    {
            message = "Duplicates Found";
            //System.out.println("FOUND >>>>>>>>>>>>>>>>>>>"+message);
    }


                }catch(Exception ex){ex.printStackTrace();}
                finally
                {
                        closeConnection(con, s,r);
                }
                return message;
	}


/*
public boolean SaveMailSetup(String mailcode, String description, String address, String date, String trans_type, String userid, String status)
	{
					boolean done = false;
					 Connection con = null;
					 Statement s = null;
					 ResultSet  r = null;
					 PreparedStatement ps = null;
					 String query = "INSERT INTO am_mail_statement"
							+ "(Mail_code, Mail_description"
							+ ",Mail_address,Creation_date,Transaction_type,User_id,Status)"
							+ " VALUES (?,?,?,?,?,?,?)";

					//int rowcount;
					//String message = "";
						try
						{
									con = getConnection();
									System.out.println("established connection...");
									ps = con.prepareStatement(query);
									ps.setString(1, mailcode);
									ps.setString(2, description);
									ps.setString(3, address);
									ps.setDate(4, df.dateConvert(new java.util.Date()));
									ps.setString(5,trans_type );
									ps.setString(6, userid);
									ps.setString(7, status);
									done=(ps.executeUpdate()!=-1);
									System.out.println("output "+done);

						}catch(Exception ex){ex.printStackTrace();}
						finally
						{
							closeConnection(con, ps);
						}
						return done;
	}


*/

/*
public String UpdateMailStatement(String mailcode,String maildescription, String mailaddress, String createdate, String transactiontype, String userid, String status)
		{
					System.out.print(mailcode);
					System.out.print(maildescription);
					System.out.print(mailaddress);
					System.out.print(createdate);



					String mess ="";
					Connection con = null;
					Statement s = null;
					PreparedStatement ps = null;
					ResultSet rs = null;


						try
						{
								con = getConnection();

								    String updatequery = "UPDATE am_mail_statement_view SET Mail_description=?, Mail_address = ?,Create_date= ? , Transaction_type=?, User_id =?, Status = ? WHERE Mail_code=?";
									ps = con.prepareStatement(updatequery);
									ps.setString(1,maildescription);
									System.out.print("wetin dey happen self"+maildescription);
									ps.setString(2,mailaddress);
									System.out.print("wetin dey happen self"+mailaddress);
									ps.setString(3,createdate);
									System.out.print("wetin dey happen self"+createdate);
									ps.setString(4,transactiontype);
									ps.setString(5,userid);
									ps.setString(6,status);
									ps.setString(7,mailcode);

									System.out.print("wetin dey happen self"+mailcode);
									System.out.print("wetin dey happen self checkpoint 1");
									int cnt=ps.executeUpdate();
									mess = "Success_update";
									System.out.print("wetin dey happen self checkpoint 2"+mess);
									if(cnt>0)
									{
										mess = "Success_update";
										System.out.println("*********************"+mess);
									}


						}catch(Exception ex)
						{
								ex.printStackTrace();
						}
						finally
						{
							closeConnection(con, ps);
						}

						return mess;

		}

*/

public boolean iscreatesave2(String codee) {
	 	boolean done=false;
	    Connection con = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;


	    String FINDER_QUERY = "SELECT Sbu_code,Sbu_name,Sbu_contact,Status from Sbu_SetUp where Sbu_code = ?";

	    try {
	        con = getConnection();
	        ps = con.prepareStatement(FINDER_QUERY);
	        ps.setString(1, codee);
	       // ps.setString(2,Transaction_Type);
	       // ps.setString(3,Transaction_Type);
	        rs = ps.executeQuery();
	//        System.out.println("Yes o...............!");

	        while (rs.next())
	        {
	           done=true;
	           //System.out.println("records already exist "+done);

	        }

	    } catch (Exception ex) {
	        System.out.println("WARNING: cannot fetch [am_raisentry_post_josh]->" +
	                ex.getMessage());
	    } finally {
	        closeConnection(con, ps, rs);
	    }

	    return done;

	}

public boolean isCheckMailSetup(String codee) {
	 	boolean done=false;
	    Connection con = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;


	    String FINDER_QUERY = "SELECT Mail_code,Mail_description,Mail_address, Creation_date,Transaction_type,User_id,Status from am_mail_statement where Mail_code = ?";

	    try {
	        con = getConnection();
	        ps = con.prepareStatement(FINDER_QUERY);
	        ps.setString(1, codee);
	       // ps.setString(2,Transaction_Type);
	       // ps.setString(3,Transaction_Type);
	        rs = ps.executeQuery();
	//        System.out.println("Yes o...............!");

	        while (rs.next())
	        {
	           done=true;
	        }

	    } catch (Exception ex) {
	        System.out.println("WARNING: cannot fetch [am_raisentry_post_josh]->" +
	                ex.getMessage());
	    } finally {
	        closeConnection(con, ps, rs);
	    }

	    return done;

	}

/*
public boolean SaveApproval(String code, double min_amount, double max_amount, String des)
		{
						boolean done = false;
						 Connection con = null;
						 Statement s = null;
						 ResultSet  r = null;
						 PreparedStatement ps = null;
						 String query = "INSERT INTO Approval_Limit"
								+ "(Level_code, Min_Amount"
								+ ",Max_Amount,Description)"
								+ " VALUES (?,?,?,?)";

						//int rowcount;
						//String message = "";
							try
							{
										con = getConnection();
										System.out.println("established connection...");
										ps = con.prepareStatement(query);
										ps.setString(1, code);
										ps.setDouble(2, min_amount);
										ps.setDouble(3, max_amount);
										ps.setString(4, des);

										done=(ps.executeUpdate()!=-1);
										System.out.println("output "+done);

							}catch(Exception ex){ex.printStackTrace();}
							finally
							{
								closeConnection(con, ps);
							}
							return done;
		}

*/

public String UpdateApproval_Level(String code,String trans_type, String level_id)
		{
					System.out.print(code);
					System.out.print(trans_type);
					System.out.print(level_id);
					//System.out.print(createdate);



					String mess ="";
					Connection con = null;
					Statement s = null;
					PreparedStatement ps = null;
					ResultSet rs = null;


						try
						{
								con = getConnection();

								    String updatequery = "UPDATE Approval_Level_setup SET Transaction_type=?,Level=? WHERE Code=?";
									ps = con.prepareStatement(updatequery);
									ps.setString(1,trans_type);
//									System.out.print("wetin dey happen self"+trans_type);
									ps.setString(2, level_id);
//									System.out.print("wetin dey happen self today "+level_id);
									//ps.setDate(4, df.dateConvert(new java.util.Date()));
									//System.out.print("wetin dey happen self"+createdate);
									ps.setString(3,code);
									//ps.setString(5,code);

//									System.out.print("wetin dey happen self"+code);
//									System.out.print("wetin dey happen self checkpoint 1");
									int cnt=ps.executeUpdate();
									mess = "Success_update";
//									System.out.print("wetin dey happen self checkpoint 2"+mess);
									if(cnt>0)
									{
										mess = "Success_update";
										//System.out.println("*********************"+mess);
									}


						}catch(Exception ex)
						{
								ex.printStackTrace();
						}
						finally
						{
							closeConnection(con, ps);
						}

						return mess;

		}

/*
public String UpdateMailStatement(String mailcode,String maildescription, String mailaddress, String transactiontype, String status)
		{
					System.out.print(mailcode);
					System.out.print(maildescription);
					System.out.print(mailaddress);
					//System.out.print(createdate);
                    handler = new AdminHandler();


					String mess ="";
					Connection con = null;
					Statement s = null;
					PreparedStatement ps = null;
					ResultSet rs = null;


						try{
								con = getConnection();

								    String updatequery = "UPDATE am_mail_statement SET Mail_description=?, Mail_address = ?, Transaction_type=?, Status = ? WHERE Mail_code=?";
									ps = con.prepareStatement(updatequery);
									ps.setString(1,maildescription);
									System.out.print("wetin dey happen self"+maildescription);
									ps.setString(2,mailaddress);
									System.out.print("wetin dey happen self"+mailaddress);
                                    ps.setString(3,handler.getTransactCode(transactiontype));
									System.out.print("wetin dey happen self"+transactiontype);
									ps.setString(4,status);
									ps.setString(5,mailcode);

									System.out.print("wetin dey happen self"+mailcode);
									System.out.print("wetin dey happen self checkpoint 1");
									int cnt=ps.executeUpdate();
									mess = "Success_update";
									System.out.print("wetin dey happen self checkpoint 2"+mess);
									if(cnt>0)
									{
										mess = "Success_update";
										System.out.println("*********************"+mess);
									}


						}catch(Exception ex)
						{
								ex.printStackTrace();
						}
						finally
						{
							closeConnection(con, ps);
						}

						return mess;

		}


*/
public String getTransactCode(String Trasn_Id){
    String query =
           "SELECT DESCRIPTION  FROM Am_Transaction_Type  " +
           "WHERE TRANS_CODE = '" + Trasn_Id + "' ";

      Connection con = null;
      ResultSet rs = null;
      Statement stmt = null;
   String branchcode = "0";
   try {
       con= getConnection();
       stmt = con.createStatement();
       rs = stmt.executeQuery(query);
       while (rs.next()) {

           branchcode = rs.getString(1);

       }

   } catch (Exception ex) {
       ex.printStackTrace();
   } finally {
    closeConnection(con, stmt,rs);
   }
   return branchcode;
}


public boolean SaveMailSetup(String mailcode, String description, String address, String date, String trans_type, String userid, String status)
	{

					boolean done = false;
					 Connection con = null;
					 Statement s = null;
					 ResultSet  r = null;
					 PreparedStatement ps = null;

					 handler = new AdminHandler();


					 String query = "INSERT INTO am_mail_statement"
							+ "(Mail_code, Mail_description"
							+ ",Mail_address,creation_date,Transaction_type,User_id,Status)"
							+ " VALUES (?,?,?,?,?,?,?)";

					//int rowcount;
					//String message = "";
						try
						{
							 String id = help.getGeneratedId("am_mail_statement");
									con = getConnection();
									//System.out.println("established connection...");
									ps = con.prepareStatement(query);
									ps.setString(1, id);
									ps.setString(2, description.toUpperCase());
									ps.setString(3, address);
									ps.setDate(4, df.dateConvert(new java.util.Date()));
									ps.setString(5,handler.getTransactCode(trans_type));
									ps.setString(6, userid);
									ps.setString(7, status);
									done=(ps.executeUpdate()!=-1);
									//System.out.println("output "+done);

						}catch(Exception ex){ex.printStackTrace();}
						finally
						{
							closeConnection(con, ps);
						}
						return done;
	}


public boolean SaveApproval(String code, double min_amount, double max_amount, String des)
		{
						boolean done = false;
						 Connection con = null;
						 Statement s = null;
						 ResultSet  r = null;
						 PreparedStatement ps = null;
						 String query = "INSERT INTO Approval_Limit"
								+ "(Level_code, Min_Amount"
								+ ",Max_Amount,Description)"
								+ " VALUES (?,?,?,?)";

						//int rowcount;
						//String message = "";
							try
							{
										String id = help.getGeneratedId("Approval_Limit");
										con = getConnection();
										//System.out.println("established connection...");
										ps = con.prepareStatement(query);
										ps.setString(1, id);
										ps.setDouble(2, min_amount);
										ps.setDouble(3, max_amount);
										ps.setString(4, des.toUpperCase());

										done=(ps.executeUpdate()!=-1);
										//System.out.println("output "+done);

							}catch(Exception ex){ex.printStackTrace();}
							finally
							{
								closeConnection(con, ps);
							}
							return done;
		}


public boolean SaveApproval_level(String code, String trans_type, String  level, String date,String userid)
{
				boolean done = false;
				 Connection con = null;
				 Statement s = null;
				 ResultSet  r = null;
				 PreparedStatement ps = null;
				 String query = "INSERT INTO Approval_Level_setup"
						+ "(code, Transaction_type"
						+ ",Level,Date,User_id)"
						+ " VALUES (?,?,?,?,?)";

				//int rowcount;
				//String message = "";
					try
					{
						 		String id = help.getGeneratedId("Approval_Level_setup");
								con = getConnection();
								//System.out.println("established connection...");
								ps = con.prepareStatement(query);
								ps.setString(1, id);
								ps.setString(2, trans_type);
								ps.setString(3, level);
								ps.setDate(4, df.dateConvert(new java.util.Date()));
								ps.setString(5,userid);

								done=(ps.executeUpdate()!=-1);
								//System.out.println("output "+done);

					}catch(Exception ex){ex.printStackTrace();}
					finally
					{
						closeConnection(con, ps);
					}
					return done;
}

public String checkBoxCheck(String sbu_code, String attach_id,String sbu_name)
	{
	 	String result ="N";
	    Connection con = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    String FINDER_QUERY = " select distinct sbu_code from am_sbu_attachement where sbu_code in (select sbu_code from sbu_setup where sbu_code=?  and attach_id=?) and sbu_name in (select sbu_name from sbu_setup where sbu_name=?) ";
	    try {
	        con = getConnection();
	        ps = con.prepareStatement(FINDER_QUERY);
	        ps.setString(1, sbu_code);
	        ps.setString(2, attach_id);
	        ps.setString(3, sbu_name);

	        rs = ps.executeQuery();
	        while (rs.next())
	        {
	        	result ="Y";
	        }

	    } catch (Exception ex) {
	        System.out.println("error generated" +
	                ex.getMessage());
	    } finally {
	        closeConnection(con, ps, rs);
	    }

	    return result;

	}







public String UpdateMailStatement(String mailcode,String maildescription, String mailaddress, String transactiontype, String status)
		{
					System.out.print(mailcode);
					System.out.print(maildescription);
					System.out.print(mailaddress);
					//System.out.print(createdate);

					handler = new AdminHandler();


					String mess ="";
					Connection con = null;
					Statement s = null;
					PreparedStatement ps = null;
					ResultSet rs = null;


						try
						{
								con = getConnection();

								    String updatequery = "UPDATE am_mail_statement SET Mail_description=?, Mail_address = ?, Transaction_type=?, Status = ? WHERE Mail_code=?";
									ps = con.prepareStatement(updatequery);
									ps.setString(1,maildescription);
									System.out.print("wetin dey happen self"+maildescription);
									ps.setString(2,mailaddress);
									System.out.print("wetin dey happen self"+mailaddress);
									ps.setString(3,handler.getTransactCode(transactiontype));
									System.out.print("wetin dey happen self"+transactiontype);
									ps.setString(4,status);
									ps.setString(5,mailcode);

									System.out.print("wetin dey happen self"+mailcode);
									System.out.print("wetin dey happen self checkpoint 1");
									int cnt=ps.executeUpdate();
									mess = "Success_update";
									System.out.print("wetin dey happen self checkpoint 2"+mess);
									if(cnt>0)
									{
										mess = "Success_update";
										//System.out.println("*********************"+mess);
									}


						}catch(Exception ex)
						{
								ex.printStackTrace();
						}
						finally
						{
							closeConnection(con, ps);
						}

						return mess;

		}



public legend.admin.objects.AssignSbu getClassPrivilegeSbu(String attched_id) {
	legend.admin.objects.AssignSbu classprivilege = null;

	String query = "Select MTID,Attach_id,SBU_NAME,GL_PREFIX,GL_SUFIX,CREATE_DATE,CREATE_USER,SBU_CODE from AM_SBU_ATTACHEMENT WHERE Attach_id =?";

	//String query1 = "SELECT Sbu_code,Sbu_name,Sbu_contact,Status,Contact_email  FROM Sbu_SetUp WHERE Sbu_name= ?";

	//String query = "Select a.sbu_code from am_sbu_attachement a,sbu_setup b where a.code=b.sbu_code and a.sbuname=b.sbu_name";
	/*String query = "SELECT MTID,ATTACH_ID,SBU_NAME"
		+ ",GL_PREFIX,GL_SUFIX,CREATE_DATE,CREATE_USER,SBU_CODE"
		+ " FROM AM_SBU_ATTACHEMENT WHERE ATTACH_ID='" + attched_id
		+ "' AND SBU_CODE='" + sbucode + "'";
		*/
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	//ApplicationHelper help = new ApplicationHelper();
	try {
		con = getConnection();
		ps = con.prepareStatement(query);

		ps.setString(1, attched_id);
		//ps.setString(2,mtid);
		//ps.setString(2, sbuname);
		//ps.setString(2, roleuuid);
		rs = ps.executeQuery();
		while (rs.next()) {
		//	 String id = help.getGeneratedId("AM_SBU_ATTACHEMENT");




			String branchAcronym = rs.getString("SBU_NAME");

			String glPrefix = rs.getString("GL_PREFIX");

			String userid = rs.getString("GL_SUFIX");

			String date = rs.getString("CREATE_DATE");

			String createuser = rs.getString("CREATE_USER");

			String  sbbb  = rs.getString("SBU_CODE");

			String branchName = rs.getString("ATTACH_ID");

			String id = rs.getString("MTID");




			//System.out.println(branchCode);


			 classprivilege = new legend.admin.objects.AssignSbu();
			//System.out.println("11");

			classprivilege.setMitd(id);
			classprivilege.setAttachid(branchName);
			classprivilege.setSbuname(branchAcronym);
			classprivilege.setGlprifix(glPrefix);
			classprivilege.setGlsurfix(userid);
			classprivilege.setCreatedate(date);
			classprivilege.setCreateuser(createuser);
			classprivilege.setSbucode(sbbb);


		}
	} catch (Exception ex) {
		System.out.println("WARN: Error fetching Class Privileges ->" + ex);
	} finally {
		closeConnection(con, ps);
	}
	return classprivilege;
}

public legend.admin.objects.AssignSbu getClassPrivilegeSbu(String attched_id,String sbucode) {
	legend.admin.objects.AssignSbu classprivilege = null;

	String query = "Select MTID,Attach_id,SBU_NAME,GL_PREFIX,GL_SUFIX,CREATE_DATE,CREATE_USER,SBU_CODE from AM_SBU_ATTACHEMENT WHERE Attach_id =? AND SBU_CODE =?";

	//String query1 = "SELECT Sbu_code,Sbu_name,Sbu_contact,Status,Contact_email  FROM Sbu_SetUp WHERE Sbu_name= ?";

	//String query = "Select a.sbu_code from am_sbu_attachement a,sbu_setup b where a.code=b.sbu_code and a.sbuname=b.sbu_name";
	/*String query = "SELECT MTID,ATTACH_ID,SBU_NAME"
		+ ",GL_PREFIX,GL_SUFIX,CREATE_DATE,CREATE_USER,SBU_CODE"
		+ " FROM AM_SBU_ATTACHEMENT WHERE ATTACH_ID='" + attched_id
		+ "' AND SBU_CODE='" + sbucode + "'";
		*/
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	//ApplicationHelper help = new ApplicationHelper();
	try {
		con = getConnection();
		ps = con.prepareStatement(query);

		ps.setString(1, attched_id);
		ps.setString(2, sbucode);
		//System.out.println("==========coming in from d method===attched_id======================"+attched_id);
		//ps.setString(2,mtid);
		//ps.setString(2, sbuname);
		//ps.setString(2, roleuuid);
		rs = ps.executeQuery();
		while (rs.next()) {
		//	 String id = help.getGeneratedId("AM_SBU_ATTACHEMENT");
			String id = rs.getString("MTID");
//			System.out.println("=================MTID===================="+id);
			String sbuname = rs.getString("SBU_NAME");
//			System.out.println("=================SBU_NAME===================="+sbuname);


			String glPrefix = rs.getString("GL_PREFIX");
//			System.out.println("=================GL_PREFIX===================="+glPrefix);
			String glsufix = rs.getString("GL_SUFIX");
//			System.out.println("=================GL_SUFIX===================="+glsufix);
			String date = rs.getString("CREATE_DATE");
//			System.out.println("=================CREATE_DATE===================="+date);
			String createuser = rs.getString("CREATE_USER");
//			System.out.println("=================CREATE_USER===================="+createuser);
			String  sbbbcode  = rs.getString("SBU_CODE");
//			System.out.println("=================SBU_CODE===================="+sbbbcode);
			String attachid = rs.getString("ATTACH_ID");
//			System.out.println("=================ATTACH_ID===================="+attachid);

			classprivilege = new legend.admin.objects.AssignSbu();









			classprivilege.setMitd(id);
			classprivilege.setSbuname(sbuname);

			classprivilege.setGlprifix(glPrefix);
			classprivilege.setGlsurfix(glsufix);
			classprivilege.setCreatedate(date);
			classprivilege.setCreateuser(createuser);
			classprivilege.setAttachid(attachid);
			classprivilege.setSbucode(sbbbcode);
			classprivilege.setCreateuser(createuser);



		}
	} catch (Exception ex) {
		System.out.println("WARN: Error fetching Class Privileges ->" + ex);
	} finally {
		closeConnection(con, ps);
	}
	return classprivilege;
}
//victorial

public boolean createBranch(Branch branch)
      {
          boolean done;
          Connection con = null;
          PreparedStatement ps = null;
          done = false;
          String query = "INSERT INTO am_ad_branch(BRANCH_ID,BRANCH_CODE,BRANCH_NAME,BRANCH_ACRONYM,GL_PREFIX,BRANCH" +
  "_ADDRESS ,STATE,PHONE_NO,FAX_NO,REGION,PROVINCE ,BRANCH_STATUS,USER_ID,CREATE_DA" +
  "TE,GL_SUFFIX,EMAIL,Uncapitalized_account,REGION_CODE,ZONE_CODE)  VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
  ;

          try
          {
             apph = new ApplicationHelper();
             String stringid = apph.getGeneratedId("am_ad_branch");
              con = getConnection();
              ps = con.prepareStatement(query);
              ps.setString(1, stringid);
              ps.setString(2, branch.getBranchCode());
              ps.setString(3, branch.getBranchName());
              ps.setString(4, branch.getBranchAcronym());
              ps.setString(5, branch.getGlPrefix());
              ps.setString(6, branch.getBranchAddress());
              ps.setString(7, branch.getState());
              ps.setString(8, branch.getPhoneNo());
              ps.setString(9, branch.getFaxNo());
              ps.setString(10, branch.getRegion());
              ps.setString(11, branch.getProvince());
              ps.setString(12, branch.getBranchStatus());
              ps.setString(13, branch.getUsername());
              ps.setDate(14, df.dateConvert(new Date()));
              ps.setString(15, branch.getGlSuffix());
              ps.setString(16, branch.getEmailAddress());
              ps.setString(17, branch.getUnClassified());
              ps.setString(18, branch.getRegion());
              ps.setString(19, branch.getZoneCode());
              done = ps.executeUpdate() != -1;
          }
          catch(Exception e)
          {
              System.out.println(getClass().getName() + " ERROR:Error Creating Branch ->" + e.getMessage());
              e.printStackTrace();
          }
          finally
          {
              closeConnection(con, ps);
          }
          return done;
    }


    private Branch getABranch(String branchid)
        {
            Branch branch;
            branch = null;
            /*
            String query = "SELECT BRANCH_ID,BRANCH_CODE ,BRANCH_NAME,BRANCH_ACRONYM,GL_PREFIX,BRANCH_ADDRES" +
    "S,STATE,PHONE_NO,FAX_NO,REGION,PROVINCE,BRANCH_STATUS,USER_ID,GL_SUFFIX,CREATE_D" +
    "ATE,EMAIL,LOCATION  FROM am_ad_branch "
    ;
            */

            String query = "SELECT BRANCH_ID,BRANCH_CODE ,BRANCH_NAME,BRANCH_ACRONYM,GL_PREFIX,BRANCH_ADDRES" +
    "S,STATE,PHONE_NO,FAX_NO,REGION_CODE,PROVINCE,BRANCH_STATUS,USER_ID,GL_SUFFIX,CREATE_D" +
    "ATE,EMAIL,ZONE_CODE FROM am_ad_branch WHERE BRANCH_ID=? AND BRANCH_STATUS = 'ACTIVE'" ;
 //           query = query + "WHERE BRANCH_ID=? AND BRANCH_STATUS = 'ACTIVE'";
        //    System.out.println("filter in getABranch: "+query);
            Connection c = null;
            ResultSet rs = null;
            PreparedStatement s = null;
            try
            {
                c = getConnection();
                s = c.prepareStatement(query);
                s.setString(1, branchid);
                rs = s.executeQuery();
                
                while (rs.next()) 
                {
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
                    String region = rs.getString("REGION_CODE");
                    String zoneCode = rs.getString("ZONE_CODE");
                    String branchStatus = rs.getString("BRANCH_STATUS");
                    String username = rs.getString("USER_ID");
                    String glSuffix = rs.getString("GL_SUFFIX");
                    String createDate = rs.getString("CREATE_DATE");
                    String emailAddress = rs.getString("EMAIL");
                    //int location = rs.getInt("LOCATION");
                    branch = new Branch(branchId, branchCode, branchName, branchAcronym, glPrefix, glSuffix, branchAddress, state, phoneNo, faxNo, region,zoneCode, province, branchStatus, username, createDate, emailAddress);
                }

            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                closeConnection(c, s, rs);
            }
            return branch;
    }


    private Branch getABranchCode(String branchcode)
        {
            Branch branch;
            branch = null;
            /*
            String query = "SELECT BRANCH_ID,BRANCH_CODE ,BRANCH_NAME,BRANCH_ACRONYM,GL_PREFIX,BRANCH_ADDRES" +
    "S,STATE,PHONE_NO,FAX_NO,REGION,PROVINCE,BRANCH_STATUS,USER_ID,GL_SUFFIX,CREATE_D" +
    "ATE,EMAIL,LOCATION  FROM am_ad_branch "
    ;
            */

            String query = "SELECT BRANCH_ID,BRANCH_CODE ,BRANCH_NAME,BRANCH_ACRONYM,GL_PREFIX,BRANCH_ADDRES" +
    "S,STATE,PHONE_NO,FAX_NO,REGION_CODE,PROVINCE,BRANCH_STATUS,USER_ID,GL_SUFFIX,CREATE_D" +
    "ATE,EMAIL,ZONE_CODE FROM am_ad_branch WHERE BRANCH_CODE=? AND BRANCH_STATUS = 'ACTIVE' " ;
 //           query = query + "WHERE BRANCH_CODE=? AND BRANCH_STATUS = 'ACTIVE'";
//            System.out.println("filter in getABranch: "+query);
            Connection c = null;
            ResultSet rs = null;
            PreparedStatement s = null;
            try
            {
                c = getConnection();
                s = c.prepareStatement(query);
                s.setString(1, branchcode);
                rs = s.executeQuery();
                
                while (rs.next()) 
                {
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
                    String region = rs.getString("REGION_CODE");
                    String zoneCode = rs.getString("ZONE_CODE");
                    String branchStatus = rs.getString("BRANCH_STATUS");
                    String username = rs.getString("USER_ID");
                    String glSuffix = rs.getString("GL_SUFFIX");
                    String createDate = rs.getString("CREATE_DATE");
                    String emailAddress = rs.getString("EMAIL");
                    //int location = rs.getInt("LOCATION");
                    branch = new Branch(branchId, branchCode, branchName, branchAcronym, glPrefix, glSuffix, branchAddress, state, phoneNo, faxNo, region,zoneCode, province, branchStatus, username, createDate, emailAddress);
                }

            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                closeConnection(c, s, rs);
            }
            return branch;
    }


public boolean updateBranch(Branch branch)
        {
            boolean done;
            Connection con = null;
            PreparedStatement ps = null;
            done = false;
            String query = "UPDATE am_ad_branch SET BRANCH_CODE =?,BRANCH_NAME = ?,BRANCH_ACRONYM = ? ,GL_PR" +
    "EFIX = ?,BRANCH_ADDRESS = ?,STATE = ? ,PHONE_NO = ?,FAX_NO = ?,REGION_CODE = ?,PROVIN" +
    "CE = ? ,BRANCH_STATUS = ?,GL_SUFFIX = ?, EMAIL = ?,Uncapitalized_account=?,ZONE_CODE = ? WHERE BRANCH_ID =?"
    ;
            try
            {
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
                ps.setString(13, branch.getEmailAddress());
                ps.setString(14, branch.getUnClassified());
                ps.setString(15, branch.getZoneCode());
                ps.setString(16, branch.getBranchId());
                done = ps.executeUpdate() != -1;
            }
            catch(Exception e)
            {
                System.out.println("WARNING:Error executing Query ->" + e.getMessage());
                e.printStackTrace();
            }
            finally
            {
                closeConnection(con, ps);
            }
            return done;

    }


    public boolean insertDeptForBranch(java.util.ArrayList list) {

		String query = "INSERT INTO sbu_branch_dept(branchCode"
				+ ",deptCode,branchId" + ",deptId,gl_prefix"
				+ ",gl_suffix)" + " VALUES(?,?,?,?,?,?)";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		legend.admin.objects.BranchDept bd = null;
		int[] d = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(query);

			for (int i = 0; i < list.size(); i++) {
				bd = (legend.admin.objects.BranchDept) list.get(i);
				ps.setString(1, bd.getBranchCode());
				ps.setString(2, bd.getDeptCode());
				ps.setString(3, bd.getBranchId());
				ps.setString(4, bd.getDeptId());
				ps.setString(5, bd.getGl_prefix());
				ps.setString(6, bd.getGl_suffix());
				ps.addBatch();
			}
			d = ps.executeBatch();
		} catch (Exception ex) {
			System.out.println("AdminHandler: insertDeptForBranch(): WARN: Error fetching all asset ->" + ex);
		} finally {
			closeConnection(con, ps);
		}
		return (d.length > 0);
	}

    public boolean removeAvaliableSbu(java.util.ArrayList list) {
	String query = "DELETE FROM AM_SBU_ATTACHEMENT" + " WHERE Sbu_Code=?"
			+ " AND Sbu_name=? AND ATTACH_ID=?";
	String query2 = "DELETE FROM Sbu_SetUp"
			+ " WHERE Sbu_Code=?" + " AND Sbu_name=? ";

	Connection con = null;
	PreparedStatement ps = null;
	PreparedStatement ps2 = null;
	ResultSet rs = null;
	legend.admin.objects.AssignSbu bd = null;
	int[] d = null;
	try {
		con = getConnection();
		ps = con.prepareStatement(query);
		ps2 = con.prepareStatement(query2);
		for (int i = 0; i < list.size(); i++) {
			bd = (legend.admin.objects.AssignSbu) list.get(i);

			ps.setString(1, bd.getSbucode());
			ps.setString(2, bd.getSbuname());
			ps.setString(3, bd.getAttachid());

			ps.addBatch();
			ps2.setString(1, bd.getSbucode());
			ps2.setString(2, bd.getSbuname());
			ps2.addBatch();

		}

		d = ps.executeBatch();
		ps2.addBatch();
	} catch (Exception ex) {
		System.out.println("WARN: Error fetching all asset - from am_sbu_attachement>" + ex);
	} finally {
		closeConnection(con, ps);
	}

	return (d.length > 0);
}

/*
    public int getBranchIDforSBU(String branchCode){
int  branch=0;

            String query = "SELECT BRANCH_ID FROM am_ad_branch where branch_code = '"+branchCode+"'";


            Connection c = null;
            ResultSet rs = null;
            Statement s = null;
            try
            {
                c = getConnection();
                s = c.createStatement();
                for(rs = s.executeQuery(query); rs.next();)
                {
                   branch = rs.getInt("BRANCH_ID");


                }

            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                closeConnection(c, s, rs);
            }
            return branch;
}//getBranchIDforSBU(String brachCode)

*/

    public String createResignation(Resignation resign) {

		Connection con = null;
		PreparedStatement ps = null;
		String result = null;
		String query = "INSERT INTO am_ad_Resignation(Staff_Name,Department"
				+ "  ,Date_of_Resumption,Section_Unit,Exit_Type,Exit_Reason,User_id,Create_date)"
				+ "   VALUES (?,?,?,?,?,?,?,?)";
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, resign.getStaffname());
			ps.setString(2, resign.getDepartment());
			ps.setString(3, resign.getDate_of_resumption());
			ps.setString(4, resign.getSection_unit());
			ps.setString(5, resign.getExit_type());
			ps.setString(6, resign.getExit_reason());
			ps.setInt(7,resign.getUserId());
			ps.setDate(8, df.dateConvert(new java.util.Date()));

			int res = ps.executeUpdate();
			if (res > 0) {
				result = "SUCCESS";
			} else {
				result = "FAILED";
			}

		} catch (Exception e) {
			System.out.println("WARNING:Error creating State ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return result;

	}
    public java.util.ArrayList getResignationList()
	{
		java.util.ArrayList _list = new java.util.ArrayList();
		legend.admin.objects.Resignation resign = null;
		String query = "Select * from am_ad_Resignation";
		//System.out.println(""+query);


		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while(rs.next())
			{
				int staffid = rs.getInt("Staff_ID");
				String staffname = rs.getString("Staff_Name");
				String department = rs.getString("Department");
				String date_of_Resumption =rs.getString("Date_of_Resumption");
				String section_Unit = rs.getString("Section_Unit");
				String exit_Type = rs.getString("Exit_Type");
				String exit_Reason = rs.getString("Exit_Reason");

				resign = new Resignation();
				resign.setStaffId(staffid);
				resign.setStaffname(staffname);
				resign.setDepartment(department);
				resign.setDate_of_resumption(date_of_Resumption);
				resign.setSection_unit(section_Unit);
				resign.setExit_type(exit_Type);
				resign.setExit_reason(exit_Reason);
				_list.add(resign);

			}

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " Error Selecting Resignation ->" + e.getMessage());
			System.out.print("Error viewing Resigination!");
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}
    public java.util.ArrayList getResignationList(String filter)
	{
		java.util.ArrayList _list = new java.util.ArrayList();
		legend.admin.objects.Resignation resign = null;
		String query = "Select * from am_ad_Resignation"+filter;
		//System.out.println(""+query);


		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while(rs.next())
			{
				int staffid = rs.getInt("Staff_ID");
				String staffname = rs.getString("Staff_Name");
				String department = rs.getString("Department");
				String date_of_Resumption =rs.getString("Date_of_Resumption");
				String section_Unit = rs.getString("Section_Unit");
				String exit_Type = rs.getString("Exit_Type");
				String exit_Reason = rs.getString("Exit_Reason");

				resign = new Resignation();
				resign.setStaffId(staffid);
				resign.setStaffname(staffname);
				resign.setDepartment(department);
				resign.setDate_of_resumption(date_of_Resumption);
				resign.setSection_unit(section_Unit);
				resign.setExit_type(exit_Type);
				resign.setExit_reason(exit_Reason);
				_list.add(resign);

			}

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " Error Selecting Resignation ->" + e.getMessage());
			System.out.print("Error viewing Resigination!");
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}
    public String updateResignation(Resignation resign) {
    	//System.out.println("Resignation  enter 1");
		Connection con = null;
		PreparedStatement ps = null;
		String result = null;

		//String query = "UPDATE am_ad_Resignation SET Staff_Name=?,Department=?,Date_of_Resumption=?,Section_Unit=?,Exit_Type=?,Exit_Reason=?,User_id=? WHERE Staff_ID =?";

		String query = "UPDATE am_ad_Resignation SET Staff_Name = ?"
			+ ",Department = ?,Date_of_Resumption=?,Section_Unit = ?"
			+ ",Exit_Type = ?,Exit_Reason=?,User_id=?" + " WHERE Staff_ID=?";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			//System.out.println("Resignation  enter 2");
			ps.setString(1, resign.getStaffname());
			//System.out.println("Resignation  enter 3"+resign.getStaffname());
			ps.setString(2, resign.getDepartment());
			//System.out.println("Resignation  enter 4"+resign.getDepartment());
			ps.setString(3, resign.getDate_of_resumption());
			//System.out.println("Resignation  enter 5"+resign.getDate_of_resumption());
			ps.setString(4, resign.getSection_unit());
			//System.out.println("Resignation  enter 6"+resign.getSection_unit());
			ps.setString(5, resign.getExit_type());
			//System.out.println("Resignation  enter 7"+resign.getExit_type());
			ps.setString(6, resign.getExit_reason());
			//System.out.println("Resignation  enter 8"+resign.getExit_reason());
			ps.setInt(7,resign.getUserId());
			//System.out.println("Resignation  enter 9"+resign.getUserId());
			ps.setInt(8, resign.getStaffId());
			//System.out.println("Resignation  enter 10"+resign.getStaffId());

			int res = ps.executeUpdate();

			//System.out.println("SUCCESSFUL UPDATED 55"+res);
			if(res> 0) {
				result = "SUCCESS";
				//System.out.println("SUCCESSFUL UPDATED 2");
			}else {
				result = "FAILED";
				//System.out.println("SUCCESSFUL UPDATED 3");
			}

			//System.out.println(query);
		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " ERROR:Error Updating State ->" + e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return result;

	}
    public String createLeave(Leave leave) {

		Connection con = null;
		PreparedStatement ps = null;
		String result = null;
		String query = "INSERT INTO am_ad_leave(Staff_Name,Department"
				+ "  ,Section_Unit,Last_Leave_Date,Effective_Date,Leave_Days,User_id,Create_date)"
				+ "   VALUES (?,?,?,?,?,?,?,?)";
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, leave.getStaffname());
			ps.setString(2, leave.getDepartment());
			ps.setString(3, leave.getSection_unit());
			ps.setString(4, leave.getLast_Leave_Date());
			ps.setString(5, leave.getEffective_Date());
			ps.setString(6, leave.getLeave_Days());
			ps.setInt(7,leave.getUserId());
			ps.setDate(8, df.dateConvert(new java.util.Date()));

			int res = ps.executeUpdate();
			if (res > 0) {
				result = "SUCCESS";
			} else {
				result = "FAILED";
			}

		} catch (Exception e) {
			System.out.println("WARNING:Error creating State ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return result;

	}
    public java.util.ArrayList getLeaveList()
	{
		java.util.ArrayList _list = new java.util.ArrayList();
		legend.admin.objects.Leave resign = null;
		String query = "Select * from am_ad_leave";
		//System.out.println(""+query);


		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while(rs.next())
			{
				int staffid = rs.getInt("Staff_ID");
				String staffname = rs.getString("Staff_Name");
				String department = rs.getString("Department");
				String Section_Unit =rs.getString("Section_Unit");
				String Last_Leave_Date = rs.getString("Last_Leave_Date");
				String Effective_Date = rs.getString("Effective_Date");
				String Leave_Days = rs.getString("Leave_Days");
				int User_id = rs.getInt("User_id");


				resign = new Leave();
				resign.setStaffId(staffid);
				resign.setStaffname(staffname);
				resign.setDepartment(department);
				resign.setSection_unit(Section_Unit);
				resign.setLast_Leave_Date(Last_Leave_Date);
				resign.setEffective_Date(Effective_Date);
				resign.setLeave_Days(Leave_Days);
				resign.setUserId(User_id);
				_list.add(resign);

			}

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " Error Selecting Resignation ->" + e.getMessage());
			System.out.print("Error viewing Resigination!");
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}
    public java.util.ArrayList getLeaveList(String filter)
	{
		java.util.ArrayList _list = new java.util.ArrayList();
		legend.admin.objects.Leave resign = null;
		String query = "Select * from am_ad_leave"+filter;
		//System.out.println(""+query);


		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while(rs.next())
			{
				int staffid = rs.getInt("Staff_ID");
				String staffname = rs.getString("Staff_Name");
				String department = rs.getString("Department");
				String Section_Unit =rs.getString("Section_Unit");
				String Last_Leave_Date = rs.getString("Last_Leave_Date");
				String Effective_Date = rs.getString("Effective_Date");
				String Leave_Days = rs.getString("Leave_Days");
				int User_id = rs.getInt("User_id");


				resign = new Leave();
				resign.setStaffId(staffid);
				resign.setStaffname(staffname);
				resign.setDepartment(department);
				resign.setSection_unit(Section_Unit);
				resign.setLast_Leave_Date(Last_Leave_Date);
				resign.setEffective_Date(Effective_Date);
				resign.setLeave_Days(Leave_Days);
				resign.setUserId(User_id);
				_list.add(resign);

			}

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " Error Selecting Resignation ->" + e.getMessage());
			System.out.print("Error viewing Resigination!");
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}
    public String updateLeave(Leave resign) {
    	//System.out.println("Resignation  enter 1");
		Connection con = null;
		PreparedStatement ps = null;
		String result = null;

		//String query = "UPDATE am_ad_Resignation SET Staff_Name=?,Department=?,Date_of_Resumption=?,Section_Unit=?,Exit_Type=?,Exit_Reason=?,User_id=? WHERE Staff_ID =?";

		String query = "UPDATE am_ad_leave SET Staff_Name = ?"
			+ ",Department = ?,Section_Unit=?,Last_Leave_Date = ?"
			+ ",Effective_Date = ?,Leave_Days=?,User_id=?" + " WHERE Staff_ID=?";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			//System.out.println("Resignation  enter 2");
			ps.setString(1, resign.getStaffname());
			//System.out.println("Resignation  enter 3"+resign.getStaffname());
			ps.setString(2, resign.getDepartment());
			//System.out.println("Resignation  enter 4"+resign.getDepartment());
			ps.setString(3, resign.getSection_unit());
			//System.out.println("Resignation  enter 5"+resign.getSection_unit());
			ps.setString(4, resign.getSection_unit());
			//System.out.println("Resignation  enter 6"+resign.getSection_unit());
			ps.setString(5, resign.getLast_Leave_Date());
			//System.out.println("Resignation  enter 7"+resign.getLast_Leave_Date());
			ps.setString(6, resign.getEffective_Date());
			//System.out.println("Resignation  enter 8"+resign.getEffective_Date());
			ps.setInt(7,resign.getUserId());
			//System.out.println("Resignation  enter 9"+resign.getUserId());
			ps.setInt(8, resign.getStaffId());
			//System.out.println("Resignation  enter 10"+resign.getStaffId());



			int res = ps.executeUpdate();

			//System.out.println("SUCCESSFUL UPDATED 55"+res);
			if(res> 0) {
				result = "SUCCESS";
				//System.out.println("SUCCESSFUL UPDATED 2");
			}else {
				result = "FAILED";
				//System.out.println("SUCCESSFUL UPDATED 3");
			}

			//System.out.println(query);
		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " ERROR:Error Updating State ->" + e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return result;

	}
	public boolean updateClassPrivilege1(java.util.ArrayList<ClassPrivilege> list,int userid, String branchCode,int loginId,String eff_date) {
		/**
		 * edited by Joshua
		 */

			for (int i = 0; i < list.size(); i++) {
				System.out.println();
				ClassPrivilege cp = (ClassPrivilege) list.get(i);
				//System.out
				updateClassPrivilege1(cp,userid,branchCode,loginId,eff_date);

			}
			System.out.println("Nos of rtecords updated = "+list.size());
		return true;
	}

	public void updateClassPrivilege1(legend.admin.objects.ClassPrivilege cp,int userid,String branchCode,int loginId,String eff_date) {
		AuditTrailGen atg1 = new AuditTrailGen();
		//String query = "UPDATE am_ad_class_privileges "+
				//"SET role_view = ?,role_addn = ?,role_edit = ?"
			//+ " WHERE clss_uuid=? AND Role_uuid=? ";
		//AND user_id=?

		String query = "UPDATE am_ad_class_privileges "+
		"SET role_view = ?,role_addn = ?,role_edit = ?"
	+ " WHERE clss_uuid=? AND Role_uuid=? ";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int[] d = null;
		try {

			con = getConnection();
			ps = con.prepareStatement(query);

				ps.setString(1, cp.getRole_view());
				ps.setString(2, cp.getRole_addn());
				ps.setString(3, cp.getRole_edit());
				ps.setString(4, cp.getClss_uuid());
				ps.setString(5, cp.getRole_uuid());
				//ps.setInt(6, userid);

				//this is to congfirm changed fields
				//compareAuditValues(cp.getRole_view(),cp.getRole_addn(),cp.getRole_edit(),cp.getClss_uuid(),cp.getRole_uuid(),String.valueOf(userid),branchCode,loginId,eff_date);

				//System.out.println("\n\n >>>>>>>>>>>>>>>>>> query "+ query);
				//System.out.println("\n\n >>>>>>>>>>>>>>>>>> cp.getRole_view()"+ cp.getRole_view());
				//System.out.println("\n\n >>>>>>>>>>>>>>>>>> cp.getRole_addn() "+ cp.getRole_addn());
				//System.out.println("\n\n >>>>>>>>>>>>>>>>>> cp.getRole_edit() "+ cp.getRole_edit());
				//System.out.println("\n\n >>>>>>>>>>>>>>>>>> cp.getClss_uuid() "+ cp.getClss_uuid());
				//System.out.println("\n\n >>>>>>>>>>>>>>>>>> cp.getRole_uuid() "+ cp.getRole_uuid());

				System.out.println("\n\n >>>>>>>>>>>>>>>>>> ps.execute() "+ ps.executeUpdate());

		}catch (Exception ex) {
			System.out.println("WARN: Error doing updateClassPrivilege ->" + ex);
		} finally {
			closeConnection(con, ps);
		}
	}
	public void compareAuditValues(String  gl_prefix, String gl_suffix, String branch_id, String dept_code, String user_id, String branchCode,int loginId,String eff_date)
	{
		AuditTrailGen atg = new AuditTrailGen();
		String gl_prefix1 = "";
		String gl_suffix1 = "";
		//String role_edit1 = "";
		//String query = "SELECT role_view, role_addn, role_edit FROM am_ad_class_privileges WHERE  clss_uuid = ? AND Role_uuid=?";
		String query = "SELECT gl_prefix,gl_suffix FROM sbu_branch_dept WHERE  branchId=? AND deptCode=?";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean done = false;
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			//ps.setInt(1, Integer.parseInt(user_id));
			ps.setInt(1, Integer.parseInt(branch_id));
			ps.setInt(2, Integer.parseInt(dept_code));
			//System.out.println("==============**=====branch_id==============="+branch_id);
			//System.out.println("==============**=====dept_Code==============="+dept_code);
			rs = ps.executeQuery();
			//System.out.println("===================ResultSET==============="+rs);
			while (rs.next())
			{
				//System.out.println("===================WHILE INSIDE A ResultSET==============="+rs);
				gl_prefix1 = rs.getString("gl_prefix");
				//System.out.println("===================gl_prefix1==============="+gl_prefix1);
				gl_suffix1 = rs.getString("gl_suffix");
				//System.out.println("===================gl_suffix1==============="+gl_suffix1);
				//role_edit1 = rs.getString("role_edit");
				//System.out.println("===================role_edit1==============="+role_edit1);
			}
			//System.out.println("\n\n old values >> "+ gl_prefix1 + "\n\n new values >>" + gl_prefix);
			//System.out.println("\n\n old values >> "+ gl_suffix1 + "\n\n new values >>" + gl_suffix);
			//System.out.println("\n\n old values >> "+ role_edit1 + "\n\n new values >>" + role_edit);
			//System.out.println("\n\n role_id>> "+ role_id);




			if(!gl_prefix1.equalsIgnoreCase(gl_prefix)){atg.logAuditTrailSecurityComp_Dept("sbu_branch_dept",  branchCode,loginId, eff_date, dept_code,gl_prefix1,gl_prefix,"gl_prefix");}
			if(!gl_suffix1.equalsIgnoreCase(gl_suffix)){atg.logAuditTrailSecurityComp_Dept("sbu_branch_dept",  branchCode,loginId, eff_date, dept_code,gl_suffix1,gl_suffix,"gl_suffix");}
			//if(!role_edit1.equalsIgnoreCase(role_edit)){atg.logAuditTrailSecurityComp("am_ad_class_privileges",  branchCode,loginId, eff_date, role_id,role_edit1,role_edit,"role_edit");}

			//System.out.println("===================logAuditTrailSecurityComp= within  compareAuditValues==============");

		}catch (Exception e) {
			System.out.println("WARNING:Error executing Query User Full NamecompareAuditValues: ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps, rs);
		}
		//return UserFullName;
	}
	public void compareAuditValuesSbu(String  gl_prefix, String gl_suffix, String attach_id, String sbu_code, String user_id, String branchCode,int loginId,String eff_date)
	{

		//System.out.println("==============**=====IN SIDE compareAuditValuesSbu With Attached Id =======**========"+attach_id+"and ******sbu_code********"+sbu_code);
		AuditTrailGen atg = new AuditTrailGen(); 
		String gl_prefix1 = "";
		String gl_suffix1 = "";
		//String role_edit1 = "";
		//String query = "SELECT role_view, role_addn, role_edit FROM am_ad_class_privileges WHERE  clss_uuid = ? AND Role_uuid=?";
		String query = "SELECT GL_PREFIX,GL_SUFIX FROM AM_SBU_ATTACHEMENT WHERE  ATTACH_ID=? AND SBU_CODE=?";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean done = false;
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			//System.out.println("==============**==ps connection============="+ps);
			//ps.setInt(1, Integer.parseInt(user_id));
			ps.setInt(1, Integer.parseInt(attach_id));
			ps.setString(2, sbu_code);
			//System.out.println("==============**=====attach_id==============="+attach_id);
			//System.out.println("==============**=====sbu_code==============="+sbu_code);
			rs = ps.executeQuery();
			//System.out.println("===================ResultSET==============="+rs);
			while (rs.next())
			{
				//System.out.println("===================WHILE INSIDE A ResultSET==============="+rs);
				gl_prefix1 = rs.getString("GL_PREFIX");
				//System.out.println("===================gl_prefix1==============="+gl_prefix1);
				gl_suffix1 = rs.getString("GL_SUFIX");
				//System.out.println("===================gl_suffix1==============="+gl_suffix1);
				//role_edit1 = rs.getString("role_edit");
				//System.out.println("===================role_edit1==============="+role_edit1);
			}
			//System.out.println("\n\n old values >> "+ gl_prefix1 + "\n\n new values >>" + gl_prefix);
			//System.out.println("\n\n old values >> "+ gl_suffix1 + "\n\n new values >>" + gl_suffix);
			//System.out.println("\n\n old values >> "+ role_edit1 + "\n\n new values >>" + role_edit);
			//System.out.println("\n\n role_id>> "+ role_id);




			if(gl_prefix1.equalsIgnoreCase(gl_prefix)){atg.logAuditTrailSecurityComp_Sbu("AM_SBU_ATTACHEMENT",  branchCode,loginId, eff_date, sbu_code,gl_prefix1,gl_prefix,"GL_PREFIX");}
			if(gl_suffix1.equalsIgnoreCase(gl_suffix)){atg.logAuditTrailSecurityComp_Sbu("AM_SBU_ATTACHEMENT",  branchCode,loginId, eff_date, sbu_code,gl_suffix1,gl_suffix,"GL_SUFFIX");}
			//if(!role_edit1.equalsIgnoreCase(role_edit)){atg.logAuditTrailSecurityComp("am_ad_class_privileges",  branchCode,loginId, eff_date, role_id,role_edit1,role_edit,"role_edit");}

			//System.out.println("===================compareAuditValuesSbu==============");

		}catch (Exception e) {
			System.out.println("WARNING:Error executing Query User Full NamecompareAuditValues: ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps, rs);
		}
		//return UserFullName;
	}
	public boolean updateDeptForBranch(java.util.ArrayList list,int userid, String branchCode,int loginId,String eff_date) {
		String query = "UPDATE sbu_branch_dept SET branchCode = ?"
				+ ",deptCode = ?,gl_prefix = ?,gl_suffix = ?"
				+ " WHERE branchId=?" + " AND deptId=?";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		legend.admin.objects.BranchDept bd = null;
		int[] d = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(query);

			for (int i = 0; i < list.size(); i++) {
				bd = (legend.admin.objects.BranchDept) list.get(i);
				ps.setString(1, bd.getBranchCode());
				ps.setString(2, bd.getDeptCode());
				ps.setString(3, bd.getGl_prefix());
				ps.setString(4, bd.getGl_suffix());
				ps.setString(5, bd.getBranchId());
				ps.setString(6, bd.getDeptId());

				ps.addBatch();

				//compareAuditValues(cp.getRole_view(),cp.getRole_addn(),cp.getRole_edit(),cp.getClss_uuid(),cp.getRole_uuid(),String.valueOf(userid),branchCode,loginId,eff_date);
				compareAuditValues(bd.getGl_prefix(),bd.getGl_suffix(),bd.getBranchId(),bd.getDeptCode(),String.valueOf(userid),branchCode,loginId,eff_date);
			}
			d = ps.executeBatch();

		} catch (Exception ex) {
			System.out.println("WARN: Error fetching all asset ->" + ex);
		} finally {
			closeConnection(con, ps);
		}

		return (d.length > 0);
	}

 public void SaveLoginAudit(String userid, String branchcode, String workstname, String ip, String sessionid)
	{

					 boolean done = false;
					 Connection con = null;
					 Statement s = null;
					 ResultSet  r = null;
					 PreparedStatement ps = null;
					 DatetimeFormat format = new DatetimeFormat();

					 String query = "INSERT INTO gb_user_login"
							+ "(create_date, user_id"
							+ ",branch_code,mtid,time_in,workstation_name,System_ip,session_id,session_time)"
							+ " VALUES (?,?,?,?,?,?,?,?,?)";
						try
						{
							 	apph = new ApplicationHelper();
							 	String stringid = apph.getGeneratedId("gb_user_login");

							 	    con = getConnection();
									ps = con.prepareStatement(query);
									String createDate = htmlUtil.getCodeName("SELECT GETDATE() ");
//									System.out.println("<<<<<<<createDate: "+createDate);
									//ps.setDate(1,  df.dateConvert(new java.util.Date()));
									ps.setString(1, createDate);
									ps.setString(2, userid);
									ps.setString(3, branchcode);
									ps.setString(4, stringid);
									//ps.setString(5,String.valueOf(df.dateConvert(new java.util.Date()).getHours())+df.dateConvert(new java.util.Date()).getMinutes()+df.dateConvert(new java.util.Date()).getSeconds());
									ps.setString(5,df.getDateTime().substring(10));
									ps.setString(6, workstname);
									ps.setString(7, ip);
									ps.setString(8,sessionid);
									ps.setString(9,df.getDateTime().substring(10));
									done=(ps.executeUpdate()!=-1);

						}catch(Exception ex){ex.printStackTrace();}
						finally
						{
							closeConnection(con, ps);
						}					//return done

	}



public void updateLoginAudit(String userid) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		boolean done2 = false;
//	  	String sessionTimeOut = htmlUtil.getCodeName("select session_timeout from am_gb_company");
//		String queryTimeOut = "update gb_user_login set time_out = '"+df.getDateTime().substring(10)+"' where time_out is null and datediff(minute, time_in, '"+df.getDateTime().substring(10)+"') / 60.0 > "+sessionTimeOut+"";
//		System.out.print("<<<<<sessionTimeOut: "+sessionTimeOut+"   queryTimeOut: "+queryTimeOut);
		
		String mtid = htmlUtil.getCodeName("SELECT MAX(mtid) FROM  gb_user_login where USER_ID = ? ",userid);
		String query = "UPDATE gb_user_login SET time_out = ? WHERE user_id =? AND MTID = ?";
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			System.out.print("<<<<<getDateTime(): "+df.getDateTime().substring(10)+"   mtid: "+mtid+"   userid: "+userid);
			ps.setString(1,df.getDateTime().substring(10));
			ps.setString(2, userid);
			ps.setString(3, mtid);
			done=( ps.executeUpdate()!=-1);
//			ps = con.prepareStatement(queryTimeOut);
//			done=( ps.executeUpdate()!=-1);
		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " ERROR:Error Updating gb_user_login timeout ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
}

 
public boolean createState(legend.admin.objects.State state) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "INSERT INTO am_gb_states(state_code,state_name"
				+ "  ,state_status,user_id,create_date,state_id)"
				+ "   VALUES (?,?,?,?,?,?)";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, state.getStateCode());
			ps.setString(2, state.getStateName());
			ps.setString(3, state.getStateStatus());
			ps.setString(4, state.getUserId());
			ps.setDate(5, df.dateConvert(new java.util.Date()));
			ps.setLong(6, Integer.parseInt(new ApplicationHelper().getGeneratedId("am_gb_states")));
			done=( ps.executeUpdate()!=-1);

		} catch (Exception e) {
			System.out.println("WARNING:Error creating State ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}


public String getPrivilegesRole(String role_name) {


	String query = "Select role_uuid from  am_ad_privileges where role_name=?";
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	String role_uuid = null;

	try {
		con = getConnection();
		ps = con.prepareStatement(query);
		ps.setString(1,role_name);
		rs = ps.executeQuery();

		while (rs.next()) {
		role_uuid = rs.getString("role_uuid");
		}
	} catch (Exception ex) {
		System.out.println("WARN: Error fetching role_uuid  getPrivilegesRole ->" + ex);
	} finally {
		closeConnection(con, ps);
	}
	return role_uuid;

}

private Branch getABranch2(String branchid)
{
    Branch branch;
    branch = null;
    /*
    String query = "SELECT BRANCH_ID,BRANCH_CODE ,BRANCH_NAME,BRANCH_ACRONYM,GL_PREFIX,BRANCH_ADDRES" +
"S,STATE,PHONE_NO,FAX_NO,REGION,PROVINCE,BRANCH_STATUS,USER_ID,GL_SUFFIX,CREATE_D" +
"ATE,EMAIL,LOCATION  FROM am_ad_branch "
;
    */

    String query = "SELECT BRANCH_ID,BRANCH_CODE ,BRANCH_NAME,BRANCH_ACRONYM,GL_PREFIX,BRANCH_ADDRES" +
"S,STATE,PHONE_NO,FAX_NO,REGION_CODE,PROVINCE,BRANCH_STATUS,USER_ID,GL_SUFFIX,CREATE_D" +
"ATE,EMAIL,Uncapitalized_account,ZONE_CODE FROM am_ad_branch " ;
    query = query+" WHERE BRANCH_ID = ?";
    Connection c = null;
    ResultSet rs = null;
    PreparedStatement s = null;
    try
    {
        c = getConnection();
        s = c.prepareStatement(query);
        s.setString(1, branchid);
        rs = s.executeQuery();
        while (rs.next()) 
        {
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
            String region = rs.getString("REGION_CODE");
            String zoneCode = rs.getString("ZONE_CODE");
            String branchStatus = rs.getString("BRANCH_STATUS");
            String username = rs.getString("USER_ID");
            String glSuffix = rs.getString("GL_SUFFIX");
            String createDate = rs.getString("CREATE_DATE");
            String emailAddress = rs.getString("EMAIL");
            String unclassified = rs.getString("Uncapitalized_account");

            //int location = rs.getInt("LOCATION");
            branch = new Branch(branchId, branchCode, branchName, branchAcronym, glPrefix, glSuffix, branchAddress, state, phoneNo, faxNo, region, province, branchStatus, username, createDate, emailAddress,unclassified,zoneCode);
        }

    }
    catch(Exception e)
    {
        e.printStackTrace();
    }
    finally
    {
        closeConnection(c, s, rs);
    }
    return branch;
}

public legend.admin.objects.Branch getBranchByBranchID2(String branchid) {
//	String filter = " WHERE BRANCH_ID = " + branchid;
	legend.admin.objects.Branch branch = getABranch2(branchid);
	return branch;

}
private legend.admin.objects.Category getACategory2(String filter) {
	legend.admin.objects.Category category = null;
	String query = "SELECT category_ID,category_code,category_name"
			+ ",category_acronym,Required_for_fleet"
			+ ",Category_Class ,PM_Cycle_Period,mileage"
			+ ",Notify_Maint_Days ,notify_every_days,residual_value"
			+ ",Dep_rate ,Asset_Ledger,Dep_ledger"
			+ ",Accum_Dep_ledger ,gl_account,insurance_acct"
			+ ",license_ledger ,fuel_ledger,accident_ledger"
			+ ",Category_Status ,user_id,create_date"
			+ ",acct_type ,currency_Id,enforceBarcode,category_type,maxNo_Dep_Improve" + " FROM am_ad_category ";

	query = query + filter;
	Connection c = null;
	ResultSet rs = null;
	Statement s = null;

	try {
		c = getConnection();
		s = c.createStatement();
		rs = s.executeQuery(query);
		while (rs.next()) {
			String categoryId = rs.getString("category_ID");
			String categoryCode = rs.getString("category_code");
			String categoryName = rs.getString("category_name");
			String categoryAcronym = rs.getString("category_acronym");
			String requiredforFleet = rs.getString("Required_for_fleet");
			String categoryClass = rs.getString("Category_Class");
			String pmCyclePeriod = rs.getString("PM_Cycle_Period");
			String mileage = rs.getString("mileage");
			String notifyMaintdays = rs.getString("Notify_Maint_Days");
			String notifyEveryDays = rs.getString("notify_every_days");
			String residualValue = rs.getString("residual_value");
			String depRate = rs.getString("Dep_rate");
			String assetLedger = rs.getString("Asset_Ledger");
			String depLedger = rs.getString("Dep_ledger");
			String accumDepLedger = rs.getString("Accum_Dep_ledger");
			String glAccount = rs.getString("gl_account");
			String insuranceAcct = rs.getString("insurance_acct");
			String licenseLedger = rs.getString("license_ledger");
			String fuelLedger = rs.getString("fuel_ledger");
			String accidentLedger = rs.getString("accident_ledger");
			String categoryStatus = rs.getString("Category_Status");
			String userId = rs.getString("user_id");
			String createDate = sdf.format(rs.getDate("create_date"));
			String acctType = rs.getString("acct_type");
			String currencyId = rs.getString("currency_Id");
                            String enforeBarcode = rs.getString("enforceBarcode");
                            String categorytype = rs.getString("category_type");
                            int maxNoDepImprove = rs.getInt("maxNo_Dep_Improve");
			category = new legend.admin.objects.Category(categoryId,
					categoryCode, categoryName, categoryAcronym,
					requiredforFleet, categoryClass, pmCyclePeriod,
					mileage, notifyMaintdays, notifyEveryDays,
					residualValue, depRate, assetLedger, depLedger,
					accumDepLedger, glAccount, insuranceAcct,
					licenseLedger, fuelLedger, accidentLedger,
					categoryStatus, userId, createDate, acctType,
					currencyId,enforeBarcode,categorytype,maxNoDepImprove);

		}

	} catch (Exception e) {
		e.printStackTrace();
	}

	finally {
		closeConnection(c, s, rs);
	}
	return category;

}
public legend.admin.objects.Category getCategoryByID2(String categoryid) {
	String filter = " WHERE category_ID=" + categoryid;
	legend.admin.objects.Category category = getACategory2(filter);
	return category;

}

///----------------
public legend.admin.objects.Category getCategoryByID2Tmp(String recId) {
	String filter = " WHERE TMPID=" + recId;
	legend.admin.objects.Category category = getACategory2Tmp(filter);
	return category;

}

public legend.admin.objects.Bank getBankByCode(String bankCode) {
	String filter = " WHERE bankCode='" + bankCode + "'";
	legend.admin.objects.Bank bank = getABank(filter);
	return bank;

}

private legend.admin.objects.Bank getABank(String filter) {
	legend.admin.objects.Bank bank = null;
	String query = "SELECT bankCode,bankName,address,phone,email,user_id,CREATE_DATE FROM am_ad_bank ";

	query = query + filter;
	Connection c = null;
	ResultSet rs = null;
	Statement s = null;

	try {
		c = getConnection();
		s = c.createStatement();
		rs = s.executeQuery(query);
		while (rs.next()) {
			String bankCode = rs.getString("bankCode");
			String bankName = rs.getString("bankName");
			String address = rs.getString("address");
			String phone = rs.getString("phone");
			String email = rs.getString("email");
			String user_id = rs.getString("user_id");
			Date createDate = rs.getDate("CREATE_DATE"); 
			bank = new legend.admin.objects.Bank(bankCode, bankName,
					address, phone, email, user_id,createDate);
		}

	} catch (Exception e) {
		e.printStackTrace();
	}

	finally {
		closeConnection(c, s, rs);
	}
	return bank;

}
public java.util.List getBanksByQuery(String filter) {
	java.util.List _list = new java.util.ArrayList();
	legend.admin.objects.Bank bank = null;
	String query = "SELECT bankCode,bankName,address,phone,email,user_id,CREATE_DATE FROM am_ad_bank ";

	query = query + filter;
	Connection c = null;
	ResultSet rs = null;
	Statement s = null;

	try {
		c = getConnection();
		s = c.createStatement();
		rs = s.executeQuery(query);
		while (rs.next()) {
			String bankCode = rs.getString("bankCode");
			String bankName = rs.getString("bankName");
			String address = rs.getString("address");
			String phone = rs.getString("phone");
			String email = rs.getString("email");
			String user_id = rs.getString("user_id");
			Date createDate = rs.getDate("CREATE_DATE"); 
			bank = new legend.admin.objects.Bank(bankCode, bankName,
					address, phone, email, user_id,createDate);
			_list.add(bank);
		}

	} catch (Exception e) {
		e.printStackTrace();
	}

	finally {
		closeConnection(c, s, rs);
	}
	return _list;

}
public boolean createBank(legend.admin.objects.Bank bank) {

	Connection con = null;
	PreparedStatement ps = null;
	boolean done = false;
	String query = "INSERT INTO am_ad_Bank(bankCode,bankName,address,phone,email,user_id,CREATE_DATE,mtid)"
			+ " VALUES (?,?,?,?,?,?,?,?)";

	try {
		con = getConnection();
		ps = con.prepareStatement(query);
		 
		ps.setString(1,bank.getBankCode());
		ps.setString(2,bank.getBankName());
		ps.setString(3,bank.getAddress());
		ps.setString(4, bank.getPhone());
		ps.setString(5, bank.getEmail());
		ps.setString(6, bank.getUser_id());
		ps.setDate(7, df.dateConvert(new java.util.Date()));
		ps.setString(8,  new ApplicationHelper().getGeneratedId("am_ad_bank"));
	done=( ps.executeUpdate()!=-1);

	} catch (Exception e) {
		System.out.println(this.getClass().getName()
				+ " ERROR:Error creating am_ad_bank ->" + e.getMessage());
		e.printStackTrace();
	} finally {
		closeConnection(con, ps);
	}
	return done;

}

public boolean updateBank(legend.admin.objects.Bank bank) {

	Connection con = null;
	PreparedStatement ps = null;
	boolean done = false;
	String query = "UPDATE am_ad_bank SET bankName=?,address=?,phone=?,email=?  WHERE bankCode=?";

	try {
		con = getConnection();
		ps = con.prepareStatement(query);
		ps.setString(1, bank.getBankName());
		ps.setString(2, bank.getAddress());
		ps.setString(3, bank.getPhone());
		ps.setString(4, bank.getEmail());
		ps.setString(5, bank.getBankCode());

		done=( ps.executeUpdate()!=-1);

	} catch (Exception e) {
		System.out.println(this.getClass().getName()
				+ " ERROR:Error Updating Query ->" + e.getMessage());
		e.printStackTrace();
	} finally {
		closeConnection(con, ps);
	}
	return done;

}
public java.util.ArrayList getStockApprovalByQuery2(String filter)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	legend.admin.objects.Aproval_limit branch = null;
	String query = "Select * from ST_APPROVAL_LEVEL"+filter;
	//System.out.println(""+query);


	Connection c = null;
	ResultSet rs = null;
	Statement s = null;

	try {
		c = getConnection();
		s = c.createStatement();
		rs = s.executeQuery(query);
		while(rs.next())
		{
//			System.out.println("i dey here o! 333333333");
			String branchCode = rs.getString("Level_Code");
//			System.out.println(branchCode);
			double branchName = rs.getDouble("Min_Amount");
//			System.out.println(branchName);
			double branchAcronym = rs.getDouble("Max_Amount");
//			System.out.println(branchAcronym);
			String glPrefix = rs.getString("Description");
//			System.out.println(glPrefix);

//			System.out.print("joshua  na u bi dis...!");

			Aproval_limit condition = new Aproval_limit(branchCode,branchName,branchAcronym,glPrefix);
			//condition.setCode(branchCode);
			//condition.setMinAmt(branchName);
			//condition.setMaxAmt(branchAcronym);
			//condition.setDesc(glPrefix);

//			System.out.println("Flash me d object >>>>>>>>>>>>>"+condition);
			_list.add(condition);
//			System.out.println("e dey work na .....JOSHUA");

		}

	} catch (Exception e) {
		System.out.println(this.getClass().getName()
				+ " Error Selecting branches ->" + e.getMessage());
	//	System.out.print("i don no waiting dey happenn o!");
	}

	finally {
		closeConnection(c, s, rs);
	}
	return _list;

}


public java.util.List getBranchManagerByQuery(String filter) {
	java.util.List _list = new java.util.ArrayList();
	BranchManager branch = null;
	String query = "SELECT MTID,BRANCH_CODE,MANAGER_NAME"
			+ ",EMAIL_ADDRESS,USER_ID,STATUS"
			+ ",CREATE_DATE  FROM am_branch_Manager ";
	query = query + filter;
//	System.out.println("<<<<<<query: "+query);
	Connection c = null;
	ResultSet rs = null;
	Statement s = null;

	try {
		c = getConnection();
		s = c.createStatement();
		rs = s.executeQuery(query);
		while (rs.next()) {
			String mtid = rs.getString("MTID");
			String branchCode = rs.getString("BRANCH_CODE");
	//		String branchName = rs.getString("BRANCH_NAME");
			String managerName =rs.getString("MANAGER_NAME");
			String emailAddress = rs.getString("EMAIL_ADDRESS");
			String status = rs.getString("STATUS");
			String userId = rs.getString("USER_ID");
			String createDate = rs.getString("CREATE_DATE");
			branch = new legend.admin.objects.BranchManager(mtid, branchCode, managerName, emailAddress,status,createDate,userId);

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


public BranchManager getBranchMgrByBranchCode(String branchcode) {
	String filter = " WHERE BRANCH_CODE = '" + branchcode + "'";
	legend.admin.objects.BranchManager branch = getABranchManager(filter);
	return branch;

}

public legend.admin.objects.BranchManager getBranchByBranchManagerID2(String mtid) {
	String filter = " WHERE MTID = " + mtid;
	legend.admin.objects.BranchManager branch = getABranchManager2(filter);
	return branch;

}

public legend.admin.objects.BranchManager getBranchByBranchManagerID3(String mtid) {
	String filter = " WHERE MTID = " + mtid;
	legend.admin.objects.BranchManager branch = getABranchManager2(filter);
	return branch;

}

private BranchManager getABranchManager(String filter)
    {
        BranchManager branch;
        branch = null;
        String query = "SELECT MTID,BRANCH_CODE,MANAGER_NAME,EMAIL_ADDRESS,STATUS,USER_ID,CREATE_DATE FROM am_branch_Manager " ;
        query = query + filter+" AND STATUS = 'ACTIVE'";
//        System.out.println("filter in getABranch: "+query);
        Connection c = null;
//        ResultSet rs = null;
//        Statement s = null;
	    PreparedStatement s = null;
	    ResultSet rs = null;
        try
        {
    	    c = getConnection();
    	    s = c.prepareStatement(query);
    	    rs = s.executeQuery();  	    
            while (rs.next()) 
            {
                String mtid = rs.getString("MTID");
                String branchCode = rs.getString("BRANCH_CODE");
                String managerName = rs.getString("MANAGER_NAME");
                String status = rs.getString("STATUS");
                String userId = rs.getString("USER_ID");
                String createDate = rs.getString("CREATE_DATE");
                String emailAddress = rs.getString("EMAIL_ADDRESS");
                //int location = rs.getInt("LOCATION");
                branch = new BranchManager(mtid, branchCode, managerName, emailAddress, status, createDate, userId);
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            closeConnection(c, s, rs);
        }
        return branch;
}

private BranchManager getABranchManager2(String filter)
    {
        BranchManager branch;
        branch = null;
        String query = "SELECT MTID,BRANCH_CODE,MANAGER_NAME,EMAIL_ADDRESS,STATUS,USER_ID,CREATE_DATE FROM am_branch_Manager " ;
        query = query + filter+" AND STATUS = 'ACTIVE'";
//        System.out.println("filter in getABranch: "+query);
        Connection c = null;
//        ResultSet rs = null;
//        Statement s = null;
	    PreparedStatement s = null;
	    ResultSet rs = null;
        try
        {
//            c = getConnection();
//            s = c.createStatement();
    	    c = getConnection();
    	    s = c.prepareStatement(query);
    	    rs = s.executeQuery();  
            while (rs.next()) 
            {
                String mtid = rs.getString("MTID");
                String branchCode = rs.getString("BRANCH_CODE");
                String managerName = rs.getString("MANAGER_NAME");
                String status = rs.getString("STATUS");
                String userId = rs.getString("USER_ID");
                String createDate = rs.getString("CREATE_DATE");
                String emailAddress = rs.getString("EMAIL_ADDRESS");
                //int location = rs.getInt("LOCATION");
                branch = new BranchManager(mtid, branchCode, managerName, emailAddress, status, createDate, userId);
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            closeConnection(c, s, rs);
        }
        return branch;
}



public boolean createBranchManager(BranchManager branch)
      {
          boolean done;
          Connection con = null;
          PreparedStatement ps = null;
          done = false;
          String query = "INSERT INTO am_branch_Manager(MTID,BRANCH_CODE,MANAGER_NAME,EMAIL_ADDRESS,STATUS,USER_ID,CREATE_DATE)  VALUES(?,?,?,?,?,?,?)" ;
          try
          {
             apph = new ApplicationHelper();
             String stringid = apph.getGeneratedId("am_branch_Manager");
              con = getConnection();
              ps = con.prepareStatement(query);
              ps.setString(1, stringid);
              ps.setString(2, branch.getBranchCode());
              ps.setString(3, branch.getManagerName());
              ps.setString(4, branch.getEmailAddress());
              ps.setString(5, branch.getStatus());
              ps.setString(6, branch.getUserId());
              ps.setDate(7, df.dateConvert(new Date()));
              done = ps.executeUpdate() != -1;
          }
          catch(Exception e)
          {
              System.out.println(getClass().getName() + " ERROR:Error Creating Branch Manager ->" + e.getMessage());
              e.printStackTrace();
          }
          finally
          {
              closeConnection(con, ps);
          }
          return done;
    }

public boolean updateBranchManager(BranchManager branch)
        {
            boolean done;
            Connection con = null;
            PreparedStatement ps = null;
            done = false;
            String query = "UPDATE am_branch_Manager SET BRANCH_CODE =?,MANAGER_NAME = ? ,STATUS = ?, EMAIL_ADDRESS = ? WHERE MTID =?";
            try
            {
                con = getConnection();
                ps = con.prepareStatement(query);
                ps.setString(1, branch.getBranchCode());
                ps.setString(2, branch.getManagerName());
                ps.setString(3, branch.getStatus());
                ps.setString(4, branch.getEmailAddress());
                ps.setString(5, branch.getMtid());
                done = ps.executeUpdate() != -1;
            }
            catch(Exception e)
            {
                System.out.println("WARNING:Error executing Query to Update am_branch_Manager ->" + e.getMessage());
                e.printStackTrace();
            }
            finally
            {
                closeConnection(con, ps);
            }
            return done;

    }


public java.util.List getRegionZoneByQuery(String filter) {
		java.util.List _list = new java.util.ArrayList();
		Zone zone = null;
		String query = "SELECT Zone_Id,Zone_code,Zone_code+'-'+Zone_name as Zone_name,Zone_acronym"
				+ ",Zone_Address,Zone_Phone,Zone_Fax,Zone_Status,User_Id,Create_Date" + " FROM am_ad_Zone ";

		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String zoneId = rs.getString("Zone_Id");
				String zoneCode = rs.getString("Zone_code");
				String zoneName = rs.getString("Zone_name");
				String zoneAcronym = rs.getString("Zone_acronym");
				String zoneAddress = rs.getString("Zone_Address");
				String zonePhone = rs.getString("Zone_Phone");
				String zoneFax = rs.getString("Zone_Fax");
				String userId = rs.getString("User_Id");
				String createDate = rs.getString("Create_Date");
				String zoneStatus= rs.getString("Zone_Status");
				//String user_id = rs.getString("user_id");
				zone = new legend.admin.objects.Zone(zoneId, zoneCode, zoneName, zoneAcronym, 
						zoneAddress, zonePhone, zoneFax, zoneStatus, userId, createDate);

				_list.add(zone);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}

public legend.admin.objects.RegionZone getZoneInRegion(String regionid,
		String zoneId) {
	legend.admin.objects.RegionZone zone = null;

	String query = "SELECT regionCode,zoneCode"
			+ ",regionId,zoneId,mtid"
			+ " FROM REGION_ZONE WHERE regionId='" + regionid
			+ "' AND zoneId='" + zoneId + "'";

	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	try {
		con = getConnection();
		ps = con.prepareStatement(query);
		rs = ps.executeQuery();
		while (rs.next()) {
			String regioncode = rs.getString("regionCode");
			String zonecode = rs.getString("zoneCode");
			String regionId = rs.getString("regionId");
			String zoneid = rs.getString("zoneId");
			String mtid = rs.getString("mtid");
			zone = new legend.admin.objects.RegionZone();
			zone.setRegionCode(regioncode);
			zone.setRegionId(regionId);
			zone.setZoneId(zoneid);
			zone.setZoneCode(zonecode);
			zone.setMtid(mtid);

			// _list.add(dept);
		}
	} catch (Exception ex) {
		System.out.println("WARN: Error fetching all Zone Assigned ->" + ex);
	} finally {
		closeConnection(con, ps);
	}
	return zone;

}

public boolean insertZoneForRegion(java.util.ArrayList list) {

	String query = "INSERT INTO REGION_ZONE(regionCode"
			+ ",zoneCode,regionId" + ",zoneId"
			+ ",mtid)" + " VALUES(?,?,?,?,?)";
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	legend.admin.objects.RegionZone bd = null;
	int[] d = null;
	try {
		con = getConnection();
		ps = con.prepareStatement(query);
        apph = new ApplicationHelper();
        String mtId = apph.getGeneratedId("REGION_ZONE");
        
		for (int i = 0; i < list.size(); i++) {
			bd = (legend.admin.objects.RegionZone) list.get(i);
			ps.setString(1, bd.getRegionCode());
			ps.setString(2, bd.getZoneCode());
			ps.setString(3, bd.getRegionId());
			ps.setString(4, bd.getZoneId());
			ps.setString(5, mtId);
			ps.addBatch();
		}
		d = ps.executeBatch();
	} catch (Exception ex) {
		System.out.println("AdminHandler: insertZoneForRegion(): WARN: Error fetching all region ->" + ex);
	} finally {
		closeConnection(con, ps);
	}
	return (d.length > 0);
}

public boolean updateZoneForRegion(java.util.ArrayList list) {
	String query = "UPDATE REGION_ZONE SET regionCode = ?"
			+ ",zoneCode = ?"
			+ " WHERE regionId=?" + " AND zoneId=?";
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	legend.admin.objects.RegionZone bd = null;
	int[] d = null;
	try {
		con = getConnection();
		ps = con.prepareStatement(query);

		for (int i = 0; i < list.size(); i++) {
			bd = (legend.admin.objects.RegionZone) list.get(i);
			ps.setString(1, bd.getRegionCode());
			ps.setString(2, bd.getZoneCode());
			ps.setString(3, bd.getRegionId());
			ps.setString(4, bd.getZoneId());

			ps.addBatch();
		}
		d = ps.executeBatch();

	} catch (Exception ex) {
		System.out.println("WARN: Error fetching all Zones ->" + ex);
	} finally {
		closeConnection(con, ps);
	}

	return (d.length > 0);
}

public java.util.List getSlaByQuery(String filter,String deptCode) {
    java.util.List _list = new java.util.ArrayList();
    legend.admin.objects.Sla sla = null;
    String query = "SELECT sla_ID,sla_name ,sla_description,Dept_Code,user_id,create_date,SLA_Start_Date,SLA_End_Date,STATUS "
    		+ "FROM am_gb_sla where Dept_Code = '"+deptCode+"' ";
    query = query + filter;
//    System.out.println("=====getSlaByQuery in getSlaByQuery: "+query);
    Connection c = null;
    ResultSet rs = null;
    Statement s = null;

    try {
        c = getConnection();
        s = c.createStatement();
        rs = s.executeQuery(query); 
        while (rs.next()) {
            String sla_ID = rs.getString("sla_ID");
            String sla_name = rs.getString("sla_name");
            String sla_description = rs.getString("sla_description");
            String DeptCode = rs.getString("Dept_Code");
           // String CatCode = rs.getString("Category_Code");
            String userId = rs.getString("user_id");
            String createDate = rs.getString("create_date");
            String slaStartDate = rs.getString("SLA_Start_Date");
            String slaEndDate = rs.getString("SLA_End_Date");
            String Status = rs.getString("STATUS");
            sla = new legend.admin.objects.Sla(sla_ID,DeptCode, sla_name,
            		sla_description, userId, createDate,Status,slaStartDate,slaEndDate);

            _list.add(sla);
        }

    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        closeConnection(c, s, rs);
    }
    return _list;

}

public Sla getSlaByID(String slaId)
{
    String filter = (new StringBuilder(" WHERE sla_ID= ")).append(slaId).toString();
    Sla sla = getSla(filter);
    return sla;
}

private legend.admin.objects.Sla getSla(String filter) {
    java.util.ArrayList _list = new java.util.ArrayList();
    legend.admin.objects.Sla sla = null;
    String query = "SELECT sla_ID,sla_name ,sla_description,Dept_Code,user_id,create_date,STATUS,SLA_Start_Date,SLA_End_Date FROM am_gb_sla ";
    query = query + filter;
//    System.out.println("======query in getSla:"+query);
    Connection c = null;
    ResultSet rs = null;
    Statement s = null;  

    try {
        c = getConnection(); 
        s = c.createStatement();
        rs = s.executeQuery(query);
        while (rs.next()) {
            String sla_ID = rs.getString("sla_ID");
            String sla_name = rs.getString("sla_name");
            String sla_description = rs.getString("sla_description");
            String DeptCode = rs.getString("Dept_Code");
     //       String CatCode = rs.getString("Category_Code");
            String userId = rs.getString("user_id");
            String createDate = rs.getString("create_date");
            String Status = rs.getString("STATUS");
            String slaStartDate = rs.getString("SLA_Start_Date");
            String slaEndDate = rs.getString("SLA_End_Date");
            sla = new legend.admin.objects.Sla(sla_ID,DeptCode,  sla_name,
            		sla_description, userId, createDate,Status,slaStartDate,slaEndDate);

        }

    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        closeConnection(c, s, rs);
    }
    return sla;

}


public Sla getSlaById(String slaId)
{
    String filter = (new StringBuilder(" WHERE sla_ID='")).append(slaId).append("'").toString();
    Sla sla = getSla(filter);
    return sla;
}

public boolean createSla(Sla sla)
{
    Connection con;
    PreparedStatement ps;
    boolean done;
    String query;
    con = null;
    ps = null;
    done = false;
    query = "INSERT INTO am_gb_sla(sla_ID,sla_name ,sla_description,user_id,create_date,Dept_" +
"Code,Category_Code,SLA_Start_Date,SLA_End_Date,RemainExecDays)   VALUES (?,?,?,?,?,?,?,?,?,?)"
;
//    System.out.println((new StringBuilder("I am in createSla ")).append(sla.getSla_ID()).toString());
    try
    {
    	String dateDiff = htmlUtil.getCodeName("DATEDIFF(day, (SELECT GETDATE()), '"+sla.getSlaEndDate()+"')");
//    	System.out.println("=====>>>>dateDiff: "+dateDiff);
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, sla.getSla_ID());
        ps.setString(2, sla.getSla_name());
        ps.setString(3, sla.getSla_description());
        ps.setString(4, sla.getUserId());
        ps.setDate(5, df.dateConvert(new Date()));
        ps.setString(6, sla.getDeptCode());
        ps.setString(7, sla.getCatCode());
        ps.setString(8, sla.getSlaStartDate());
        ps.setString(9, sla.getSlaEndDate());
        ps.setString(10, dateDiff); 
        done = ps.executeUpdate() != -1;

    }
    catch(Exception e)
    {
        e.printStackTrace();
        System.out.println((new StringBuilder("WARNING:Error creating Sla ->")).append(e.getMessage()).toString());
	} finally {
		closeConnection(con, ps);
	}
	return done;
}

public boolean updateSla(Sla sla)
{
    Connection con;
    PreparedStatement ps;
    boolean done;
    String query;
    con = null;
    ps = null;
    done = false;
    query = "UPDATE am_gb_sla SET sla_name = ?, sla_description = ?  ,Dept_code = ?,SLA_Start_Date = ?,SLA_End_Date = ? WHERE s" +
"la_ID=?"
;
    try
    {
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, sla.getSla_name());
        ps.setString(2, sla.getSla_description());
        ps.setString(3, sla.getDeptCode());
        ps.setString(4, sla.getSlaStartDate());
        ps.setString(5, sla.getSlaEndDate());
        ps.setString(6, sla.getSla_ID());
        done = ps.executeUpdate() != -1;
    }
    catch(Exception e)
    {
        System.out.println((new StringBuilder(String.valueOf(getClass().getName()))).append(" ERROR:Error Updating Sla ->").append(e.getMessage()).toString());
    }
 finally {
	closeConnection(con, ps);
}
return done;
}

//
//public boolean createCriteriaRule(String slaId, String crt, String desc, String userId)
//{
//    Connection con;
//    PreparedStatement ps;
//    boolean done;
//    String query;
//    con = null;
//    ps = null;
//    done = false;
//    query = "INSERT INTO am_criteria_rules(criteria_ID,rules_NAME,criteria_DESCRIPTION, USER_" +
//"ID,CREATE_DATE)   VALUES (?,?,?,?,?)"
//;
//    System.out.println((new StringBuilder("slaId>>>>> ")).append(slaId).append(" =====desc== ").append(desc).append(" ===crt==== ").append(crt).toString());
//    try
//    {
//        con = getConnection();
//        ps = con.prepareStatement(query);
// //       String id = (new ApplicationHelper()).getGeneratedId("AM_GB_SLA");
// //       ps.setString(1, id);
//        ps.setString(1, slaId);
//        ps.setString(2, crt);
//        ps.setString(3, desc);
//        ps.setString(4, userId);
//        ps.setDate(5, df.dateConvert(new Date()));
//        done = ps.executeUpdate() != -1;
//    }
//    catch(Exception e)
//    {
//        e.printStackTrace();
//        System.out.println((new StringBuilder("WARNING:Error creating am_criteria_rules ->")).append(e.getMessage()).toString());
//    }
//    finally {
//    	closeConnection(con, ps);
//    }
//    return done;
//}

public boolean createCriteriaRule(String slaId,String crt,String desc,String userId) {

    Connection con = null;
    PreparedStatement ps = null; 
    boolean done = false;
    String query = "INSERT INTO am_criteria_rules(criteria_ID,rules_NAME,criteria_DESCRIPTION, USER_ID,CREATE_DATE)   VALUES (?,?,?,?,?)";
    try {
        con = getConnection();
        ps = con.prepareStatement(query); 
//       String id=new ApplicationHelper().getSelectId("AM_GB_SLA");
       String id=new ApplicationHelper().getGeneratedId2("AM_GB_SLA");
        ps.setString(1, id);   
        ps.setString(2, crt);
        ps.setString(3, desc);
        ps.setString(4, userId); 
//        System.out.println("======>>>>Id: "+id+"   <<<<<<<<slaId>>>>> "+slaId+" =====desc== "+desc+" ===crt==== "+crt);
        ps.setDate(5, df.dateConvert(new java.util.Date())); 
        done = (ps.executeUpdate() != -1);

    } catch (Exception e) {e.printStackTrace();
        System.out.println("WARNING:Error creating am_criteria_rules ->" + e.getMessage());
    } finally {
        closeConnection(con, ps);
    }
    return done;

}        

public ArrayList getRulesRecords(String filter)
{
    ArrayList _list;
    String query;
    Connection c;
    ResultSet rs;
    Statement s;
    _list = new ArrayList();
    Rules trlc = null;
    query = " SELECT * FROM am_criteria_rules  ";
    query = (new StringBuilder(String.valueOf(query))).append(filter).toString();
    c = null;
    rs = null;
    s = null;
//    System.out.println((new StringBuilder("KKKKKqueryKKKKK>>> ")).append(query).toString());
    try
    {
        c = getConnection();
        s = c.createStatement();
      
        for(rs = s.executeQuery(query); rs.next(); _list.add(trlc))
        {
            String Id = rs.getString("criteria_ID");
            String name = rs.getString("rules_NAME");
            String desc = rs.getString("criteria_DESCRIPTION");
            String userId = rs.getString("USER_ID");
            trlc = new Rules(Id, name, desc, userId);
        }
    }
    catch(Exception e)
    {
        e.printStackTrace();
    }
    finally {
    	 closeConnection(c, s, rs);
    }
    return _list;
}

public boolean createResponse(RuleConstraints constrsint, String DeptCode, String CatCode, int sla_ID)
{
    Connection con;
    PreparedStatement ps;
    boolean done;
    String query;
    con = null;
    ps = null;
    done = false;
    query = "INSERT INTO SLA_RESPONSE(criteria_ID,ITEM_NAME,RESPONSE_DAY,RESPONSE_HOUR,RESPON" +
"SE_MINUTE,RESOLVE_DAY,RESOLVE_HOUR,RESOLVE_MINUTE,CONSTRAINTS,DEPT_CODE,CAT_CODE,ALERTFREQ,ALERTSTART,ALERTSTARTDATE" +
")   VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
;
    try
    {
        con = getConnection();
        ps = con.prepareStatement(query);
        String id = (new ApplicationHelper()).getGeneratedId("SLA_RESPONSE");
        ps.setInt(1, sla_ID);
        ps.setString(2, constrsint.getNAME());
        ps.setString(3, constrsint.getRESPONSE_DAY());
        ps.setString(4, constrsint.getRESPONSE_HOUR());
        ps.setString(5, constrsint.getRESPONSE_MINUTE());
        ps.setString(6, constrsint.getRESOLVE_DAY());
        ps.setString(7, constrsint.getRESOLVE_HOUR());
        ps.setString(8, constrsint.getRESOLVE_MINUTE());
        ps.setString(9, constrsint.getCONSTRAINT() != null ? constrsint.getCONSTRAINT() : "");
        ps.setString(10, DeptCode);
        ps.setString(11, CatCode);
        ps.setInt(12, constrsint.getAlertFreq());
        ps.setString(13, constrsint.getAlertStart());
        ps.setString(14, constrsint.getAlertStartDate());
        
        done = ps.executeUpdate() != -1;
    }
    catch(Exception e)
    {
        e.printStackTrace();
        System.out.println((new StringBuilder("WARNING:Error creating SLA_ESCALATE ->")).append(e.getMessage()).toString());
    }
    finally {
    	closeConnection(con, ps);
   }
    return done;
}

public boolean createEscalate(RuleConstraints constrsint)
{
    Connection con;
    PreparedStatement ps;
    boolean done;
    String query;
    con = null;
    ps = null;
    done = false;
    query = "INSERT INTO SLA_ESCALATE(criteria_ID,NAME,RESPONSE_DAY,RESPONSE_HOUR,RESPONSE_MI" +
"NUTE,RESOLVE_DAY,RESOLVE_HOUR,RESOLVE_MINUTE,CONSTRAINTS,NAME2)   VALUES (?,?,?," +
"?,?,?,?,?,?,?)"
;
    try
    {
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, constrsint.getCriteria_ID());
        ps.setString(2, constrsint.getNAME());
        ps.setString(3, constrsint.getRESPONSE_DAY());
        ps.setString(4, constrsint.getRESPONSE_HOUR());
        ps.setString(5, constrsint.getRESPONSE_MINUTE());
        ps.setString(6, constrsint.getRESOLVE_DAY());
        ps.setString(7, constrsint.getRESOLVE_HOUR());
        ps.setString(8, constrsint.getRESOLVE_MINUTE());
        ps.setString(9, constrsint.getCONSTRAINT());
        done = ps.executeUpdate() != -1;
    }
    catch(Exception e)
    {
        e.printStackTrace();
        System.out.println((new StringBuilder("WARNING:Error creating SLA_ESCALATE ->")).append(e.getMessage()).toString());
    }
    finally {
    	closeConnection(con, ps);
   }
    return done;
}

public RuleConstraints getResponseByQuery(String filter)
{
    RuleConstraints values;
    String query;
    Connection c;
    ResultSet rs;
    Statement s;
    values = null;
    query = "SELECT *  FROM SLA_RESPONSE";
    query = (new StringBuilder(String.valueOf(query))).append(filter).toString();
//    System.out.println("=======query in getResponseByQuery: "+query);
    c = null;
    rs = null;
    s = null;
    try
    {
        c = getConnection();
        s = c.createStatement();
        for(rs = s.executeQuery(query); rs.next();)
        {
            String Id = String.valueOf(rs.getString("criteria_ID"));
            String itemName = rs.getString("ITEM_NAME");
            String resDay = rs.getString("RESPONSE_DAY");
            String resHour = rs.getString("RESPONSE_HOUR");
            String resMinute = rs.getString("RESPONSE_MINUTE");
            String resovDay = rs.getString("RESOLVE_DAY");
            String resovHour = rs.getString("RESOLVE_HOUR");
            String resovMinute = rs.getString("RESOLVE_MINUTE");
            String contraint = rs.getString("CONSTRAINTS");
            int alertFreq = rs.getInt("ALERTFREQ");
            String alertStart = rs.getString("ALERTSTART");
            values = new RuleConstraints(Id, itemName, resDay, resHour, resMinute, resovDay, resovHour, resovMinute, contraint,alertFreq,alertStart);
            
        }
    }
    catch(Exception e)
    {
        System.out.println((new StringBuilder(String.valueOf(getClass().getName()))).append(" ERROR:Error Selecting SLA_RESPONSE ->").append(e.getMessage()).toString());
    }
    finally {
    	closeConnection(c, s, rs);
   }
    return values;
}

public RuleConstraints getEscalateByQuery(String filter)
{
    RuleConstraints values;
    String query;
    Connection c;
    ResultSet rs;
    Statement s;
    values = null;
    query = "SELECT *  FROM SLA_ESCALATE";
    query = (new StringBuilder(String.valueOf(query))).append(filter).toString();
    c = null;
    rs = null;
    s = null;
    try
    {
        c = getConnection();
        s = c.createStatement();
        String contraint2;
        for(rs = s.executeQuery(query); rs.next(); values.setCONSTRAINT2(contraint2))
        {
            String Id = rs.getString("criteria_ID");
            String itemName = rs.getString("NAME");
            String resDay = rs.getString("RESPONSE_DAY");
            String resHour = rs.getString("RESPONSE_HOUR");
            String resMinute = rs.getString("RESPONSE_MINUTE");
            String resovDay = rs.getString("RESOLVE_DAY");
            String resovHour = rs.getString("RESOLVE_HOUR");
            String resovMinute = rs.getString("RESOLVE_MINUTE");
            String contraint = rs.getString("CONSTRAINTS");
            String itemName2 = rs.getString("NAME2");
            contraint2 = rs.getString("CONSTRAINTS2");
            values = new RuleConstraints(Id, itemName, resDay, resHour, resMinute, resovDay, resovHour, resovMinute, contraint);
            values.setItemName2(itemName2);
        }
    }
    catch(Exception e)
    {
        System.out.println((new StringBuilder(String.valueOf(getClass().getName()))).append(" ERROR:Error Selecting SLA_ESCALATE ->").append(e.getMessage()).toString());
    }
    finally {
    	closeConnection(c, s, rs);
   }
    return values;
}

public boolean updateResponse(RuleConstraints values, String DeptCode, String CatCode)
{
    Connection con;
    PreparedStatement ps;
    boolean done;
    String query;
    con = null;
    ps = null;
    done = false;
    query = "update SLA_RESPONSE set ITEM_NAME=? ,RESPONSE_DAY=?,RESPONSE_HOUR=?,RESPONSE_MIN" +
"UTE=?,RESOLVE_DAY=?,RESOLVE_HOUR=?,RESOLVE_MINUTE=?,CONSTRAINTS=?,ALERTFREQ=?,ALERTSTART=?,ALERTSTARTDATE=?  WHERE Dept_Co" +
"de=? AND Cat_Code=? AND criteria_ID=? "
;
    try
    {
//    	System.out.println("=======values.getAlertFreq()===>: "+values.getAlertFreq()+" ======>>>>>values.getAlertStart(): "+values.getAlertStart()+"  =====values.getCriteria_ID(): "+values.getCriteria_ID());
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, values.getNAME());
        ps.setString(2, values.getRESPONSE_DAY());
        ps.setString(3, values.getRESPONSE_HOUR());
        ps.setString(4, values.getRESPONSE_MINUTE());
        ps.setString(5, values.getRESOLVE_DAY());
        ps.setString(6, values.getRESOLVE_HOUR());
        ps.setString(7, values.getRESOLVE_MINUTE());
        ps.setString(8, values.getCONSTRAINT() != null ? values.getCONSTRAINT() : "");
        ps.setInt(9, values.getAlertFreq());
        ps.setString(10, values.getAlertStart());
        //System.out.println("------getAlertStartDate------->"+values.getAlertStartDate());
        ps.setString(11, values.getAlertStartDate());
        ps.setString(12, DeptCode);
        ps.setString(13, CatCode);
        ps.setInt(14, Integer.parseInt(values.getCriteria_ID()));
        done = ps.executeUpdate() != -1;
    }
    catch(Exception e)
    {
        e.printStackTrace();
        System.out.println((new StringBuilder("WARNING:Error UPDATING SLA_RESPONSE ->")).append(e.getMessage()).toString());
    }
    finally {
    	closeConnection(con, ps);
   }
    return done;
}

public boolean updateEscalate(RuleConstraints values)
{
    Connection con;
    PreparedStatement ps;
    boolean done;
    String query;
    con = null;
    ps = null;
    done = false;
    query = "update SLA_ESCALATE set NAME=? ,RESPONSE_DAY=?,RESPONSE_HOUR=?,RESPONSE_MINUTE=?" +
",RESOLVE_DAY=?,RESOLVE_HOUR=?,RESOLVE_MINUTE=?,CONSTRAINTS=?,ALERTSTARTDATE=? WHERE criteria_ID=?" +
" "
;
    try
    {
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, values.getNAME());
        ps.setString(2, values.getRESPONSE_DAY());
        ps.setString(3, values.getRESPONSE_HOUR());
        ps.setString(4, values.getRESPONSE_MINUTE());
        ps.setString(5, values.getRESOLVE_DAY());
        ps.setString(6, values.getRESOLVE_HOUR());
        ps.setString(7, values.getRESOLVE_MINUTE());
        ps.setString(8, values.getCONSTRAINT() != null ? values.getCONSTRAINT() : "");
        ps.setString(9, values.getAlertStartDate());
        ps.setString(10, values.getCriteria_ID());
        done = ps.executeUpdate() != -1;
    }
    catch(Exception e)
    {
        //System.out.println("-----------updateEscalate---------------->");
        e.printStackTrace();
        System.out.println((new StringBuilder("WARNING:Error UPDATING SLA_ESCALATE ->")).append(e.getMessage()).toString());
    }
    finally {
    	closeConnection(con, ps);
   }
    return done;
}

public String createSlaNew(Sla sla)
{
    Connection con;
    PreparedStatement ps;
    String id;
    String query;
    con = null;
    ps = null;
//    boolean done = false;
    id = "";
    query = "INSERT INTO am_gb_sla(sla_ID,sla_name ,sla_description,user_id,create_date,Dept_" +
"Code)   VALUES (?,?,?,?,?,?)"
;
    try
    {
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, sla.getSla_ID());
        ps.setString(2, sla.getSla_name());
        ps.setString(3, sla.getSla_description());
        ps.setString(4, sla.getUserId());
        ps.setDate(5, df.dateConvert(new Date()));
        ps.setString(6, sla.getDeptCode());
        
        id = (new ApplicationHelper().getGeneratedId("AM_GB_SLA"));
//        System.out.println("=====sla.getSla_description(): "+sla.getSla_description());
 //       id = (new ApplicationHelper()).UpdateGeneratedId("AM_GB_SLA");
        boolean done = ps.executeUpdate() != -1;
    }
    catch(Exception e)
    {
        e.printStackTrace();
        System.out.println((new StringBuilder("WARNING:Error creating Sla ->")).append(e.getMessage()).toString());
    }
    finally {
    	closeConnection(con, ps);
   }
    return id;
}

public boolean updateEscalate(RuleConstraints values, String name2, String DeptCode, String CatCode)
{
    Connection con;
    PreparedStatement ps;
    boolean done;
    String query;
    con = null;
    ps = null;
    done = false;
    query = "update SLA_ESCALATE set NAME=? ,RESPONSE_DAY=?,RESPONSE_HOUR=?,RESPONSE_MINUTE=?" +
",RESOLVE_DAY=?,RESOLVE_HOUR=?,RESOLVE_MINUTE=?,CONSTRAINTS=?,NAME2=?,CONSTRAINTS" +
"2=? WHERE Dept_Code=? AND Cat_Code=? AND criteria_ID=? "
;
    try
    {
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, name2);
        ps.setString(2, values.getRESPONSE_DAY());
        ps.setString(3, values.getRESPONSE_HOUR());
        ps.setString(4, values.getRESPONSE_MINUTE());
        ps.setString(5, values.getRESOLVE_DAY());
        ps.setString(6, values.getRESOLVE_HOUR());
        ps.setString(7, values.getRESOLVE_MINUTE());
        ps.setString(8, values.getCONSTRAINT() != null ? values.getCONSTRAINT() : "");
        ps.setString(9, name2);
        ps.setString(10, values.getCONSTRAINT2() != null ? values.getCONSTRAINT2() : "");
        ps.setString(11, DeptCode);
        ps.setString(12, CatCode);
        ps.setInt(13, Integer.parseInt(values.getCriteria_ID()));
        done = ps.executeUpdate() != -1;
    }
    catch(Exception e)
    {
        e.printStackTrace();
        System.out.println((new StringBuilder("WARNING:Error UPDATING SLA_ESCALATE ->")).append(e.getMessage()).toString());
    }
    finally {
    	closeConnection(con, ps);
   }
    return done;
}

public boolean createEscalate(RuleConstraints constrsint, String name2, String DeptCode, String CatCode, int sla_ID)
{
    Connection con;
    PreparedStatement ps;
    boolean done;
    String query;
    con = null;
    ps = null;
    done = false;
    query = "INSERT INTO SLA_ESCALATE(criteria_ID,NAME,RESPONSE_DAY,RESPONSE_HOUR,RESPONSE_MI" +
"NUTE,RESOLVE_DAY,RESOLVE_HOUR,RESOLVE_MINUTE,CONSTRAINTS,NAME2,CONSTRAINTS2,DEPT" +
"_CODE,CAT_CODE)   VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)"
;
    try
    {
        con = getConnection();
        ps = con.prepareStatement(query);
        String id = (new ApplicationHelper()).getGeneratedId("SLA_ESCALATE");
        ps.setInt(1, sla_ID);
        ps.setString(2, name2);
        ps.setString(3, constrsint.getRESPONSE_DAY());
        ps.setString(4, constrsint.getRESPONSE_HOUR());
        ps.setString(5, constrsint.getRESPONSE_MINUTE());
        ps.setString(6, constrsint.getRESOLVE_DAY());
        ps.setString(7, constrsint.getRESOLVE_HOUR());
        ps.setString(8, constrsint.getRESOLVE_MINUTE());
        ps.setString(9, constrsint.getCONSTRAINT() != null ? constrsint.getCONSTRAINT() : "");
        ps.setString(10, name2);
        ps.setString(11, constrsint.getCONSTRAINT2() != null ? constrsint.getCONSTRAINT2() : "");
        ps.setString(12, DeptCode);
        ps.setString(13, CatCode);
        done = ps.executeUpdate() != -1;
    }
    catch(Exception e)
    {
        //System.out.println("--------------------------->");
        e.printStackTrace();
        System.out.println((new StringBuilder("WARNING:Error creating SLA_ESCALATE ->")).append(e.getMessage()).toString());
    }
    finally {
    	closeConnection(con, ps);
   }
    return done;
}


public ArrayList getRecords(String column, String table)
{
    ArrayList _list;
    String query;
    Connection c;
    ResultSet rs;
    Statement s;
    _list = new ArrayList();
    TableColumn tc = null;
    query = (new StringBuilder("SELECT ")).append(column).append(" FROM ").append(table).append("  ").toString();
//    System.out.println("========query with 2 Parameters: "+query);
    c = null;
    rs = null;
    s = null;
    try
    {
        c = getConnection();
        s = c.createStatement();
        for(rs = s.executeQuery(query); rs.next(); _list.add(tc))
        {
            String columnvalue = rs.getString(1);
            tc = new TableColumn();
            tc.setColumnName(columnvalue);
        }
    }
    catch(Exception e)
    {
        e.printStackTrace();
    }
    finally {
    	closeConnection(c, s);
   }
    return _list;
}


public ArrayList getRecordsCategory(String column, String table, String DeptCode)
{
    ArrayList _list;
    String query;
    Connection c;
    ResultSet rs;
    Statement s;
    _list = new ArrayList();
    TableColumn tc = null;
    String code = "";
  //  query = "SELECT ")).append(column).append(" FROM ").append(table).append("  ").toString();
    if(column.equalsIgnoreCase("sub_category_name")){code = "sub_category_code";}
    if(column.equalsIgnoreCase("Region_Name")){code = "Region_Code";}
    query = "SELECT "+column+","+code+" FROM "+table+" where "+column+" is not null";
//    System.out.println("========query with 3 Parameters: "+query);
    c = null;
    rs = null;
    s = null;
    try
    {
        c = getConnection();
        s = c.createStatement();
        for(rs = s.executeQuery(query); rs.next(); _list.add(tc))
        {
            String columnvalue = rs.getString(1);
            String columId = rs.getString(2);
            tc = new TableColumn();
            tc.setColumnName(columnvalue);
            tc.setColumId(columId);
//            System.out.print("=======columnvalue: "+columnvalue+"      columId: "+columId);
        }
    }
    catch(Exception e)
    {
        e.printStackTrace();
    }
    finally {
    	closeConnection(c, s);
   }
    return _list;
}


public ArrayList getUserRecords()
{
    ArrayList _list;
    String query;
    Connection c;
    ResultSet rs;
    Statement s;
    _list = new ArrayList();
    User tc = null;
    query = "SELECT user_name,full_name FROM am_gb_user where User_Status = 'ACTIVE' order by full_name  ";
    c = null;
    rs = null;
    s = null;
    try
    {
        c = getConnection();
        s = c.createStatement();
        for(rs = s.executeQuery(query); rs.next(); _list.add(tc))
        {
            String user_name = rs.getString(1);
            String full_name = rs.getString(2);
            tc = new User();
            tc.setUserName(user_name);
            tc.setUserFullName(full_name);
        }

    }
    catch(Exception e)
    {
        e.printStackTrace();
    }
    closeConnection(c, s, rs);

    return _list;
}


public boolean createComplaintCategory(legend.admin.objects.ComplaintCategory comCat) {

    Connection con = null;
    PreparedStatement ps = null;
    boolean done = false;
    String query = "INSERT INTO HD_COMPLAIN_CATEGORY(category_code,CATEGORY_DESCRIPTION,status,user_id,create_date,complain_id)" + "   VALUES (?,?,?,?,?,?)";

    try {
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, comCat.getcomplaintCode());
        ps.setString(2, comCat.getcomplaintName());
        ps.setString(3, comCat.getcomplaintStatus());
        ps.setString(4, comCat.getUserId());
        ps.setDate(5, df.dateConvert(new java.util.Date()));
        ps.setLong(6, Integer.parseInt(new ApplicationHelper().getGeneratedId("HD_COMPLAIN_CATEGORY")));
        done = (ps.executeUpdate() != -1);

    } catch (Exception e) {
        System.out.println("WARNING:Error creating Complaint Category ->" + e.getMessage());
    } finally {
        closeConnection(con, ps);
    }
    return done;

}



  public java.util.List getComplaintCategoryByQuery(String filter) {
    java.util.List _list = new java.util.ArrayList();
    legend.admin.objects.ComplaintCategory comCat = null;
    String query = "SELECT complain_id,category_code,CATEGORY_DESCRIPTION ,status,user_id,create_date" + " FROM HD_COMPLAIN_CATEGORY ";

    query = query + filter;
    Connection c = null;
    ResultSet rs = null;
    Statement s = null;

    try {
        c = getConnection();
        s = c.createStatement();
        rs = s.executeQuery(query);
        while (rs.next()) {
            String complainId = rs.getString("complain_id");
            String complainCode = rs.getString("category_code");
            String complainName = rs.getString("CATEGORY_DESCRIPTION");
            String complainStatus = rs.getString("status");
            String userId = rs.getString("user_id");
            String createDate = rs.getString("create_date");
            comCat = new legend.admin.objects.ComplaintCategory(complainId, complainCode,
                    complainName, complainStatus, userId, createDate);

            _list.add(comCat);
        }

    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        closeConnection(c, s, rs);
    }
    return _list;

}

private legend.admin.objects.ComplaintCategory getComplaint(String filter) {
    java.util.ArrayList _list = new java.util.ArrayList();
    legend.admin.objects.ComplaintCategory comCat = null;
    String query = "SELECT complain_id,category_code,CATEGORY_DESCRIPTION ,status,user_id,create_date" + " FROM HD_COMPLAIN_CATEGORY ";

    query = query + filter;
    Connection c = null;
    ResultSet rs = null;
    Statement s = null;

    try {
        c = getConnection();
        s = c.createStatement();
        rs = s.executeQuery(query);
        while (rs.next()) {
            String complainId = rs.getString("complain_id");
            String complainCode = rs.getString("category_code");
            String complainName = rs.getString("CATEGORY_DESCRIPTION");
            String complainStatus = rs.getString("status");
            String userId = rs.getString("user_id");
            String createDate = rs.getString("create_date");
            comCat = new legend.admin.objects.ComplaintCategory(complainId, complainCode,
                    complainName, complainStatus, userId, createDate);

        }

    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        closeConnection(c, s, rs);
    }
    return comCat;

}  

public legend.admin.objects.ComplaintCategory getComplaintCategoryByID(String comid) {
    String filter = " WHERE complain_id =" + comid;
    legend.admin.objects.ComplaintCategory complaint = getComplaint(filter);
    return complaint;
}

    public legend.admin.objects.ComplaintCategory getCategoryCodeByCode(String catCode) {
    String filter = " WHERE category_code='" + catCode + "'";
    legend.admin.objects.ComplaintCategory comCategory = getComplaint(filter);
    return comCategory;

}

    public boolean updateComplaintCategory(legend.admin.objects.ComplaintCategory comCat) {

    Connection con = null;
    PreparedStatement ps = null;
    boolean done = false;
    String query = "UPDATE HD_COMPLAIN_CATEGORY SET category_code = ?,CATEGORY_DESCRIPTION = ? ,status = ?" + "   WHERE complain_id=?";

    try {
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, comCat.getcomplaintCode());
        ps.setString(2, comCat.getcomplaintName());
        ps.setString(3, comCat.getcomplaintStatus());
        ps.setString(4, comCat.getcomplaintId());

        done = (ps.executeUpdate() != -1);

    } catch (Exception e) {
        System.out.println(this.getClass().getName() + " ERROR:Error Updating Complaint Category ->" + e.getMessage());
    } finally {
        closeConnection(con, ps);
    }
    return done;

}


    public boolean SaveLostPassword(String UserName, String email, String date) {

        boolean done = false;
        Connection con = null;
        Statement s = null;  
        ResultSet r = null;
        PreparedStatement ps = null;

        handler = new AdminHandler();  
//        System.out.println("=====About to Insert into SaveLostPassword====");
        String query = "INSERT INTO am_gb_LostPassword (ID,User_Name,email,Create_Date) VALUES (?,?,?,?)";

        try {
            String id = help.getGeneratedId("am_gb_LostPassword");
            con = getConnection();
//            System.out.println("=====id===Inserted === "+id);
            ps = con.prepareStatement(query);
            ps.setString(1, id.trim());
            ps.setString(2, UserName);
            ps.setString(3, email);
            ps.setDate(4, df.dateConvert(new java.util.Date()));
            done = (ps.executeUpdate() != -1);


        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
        return done;
    }

    public boolean deleteRule(String slaId, String rule_name) {
        boolean done = true;
        //Connection conn = null;
        con = getConnection();
        try {  
     //       con = getConnection("helpDesk");
//        	System.out.println("<<<<<<slaId deleteRule>>>>> "+slaId);
            if (!slaId.equals("")) { 
                PreparedStatement ps = con.prepareStatement("Delete from am_criteria_rules where criteria_ID = '"+slaId+"' and criteria_DESCRIPTION = '"+rule_name+"' ");
                ps.execute();
                ps.close();

            }
            if (!slaId.equals("")) { 
                PreparedStatement ps = con.prepareStatement("Delete from SLA_ESCALATE where criteria_ID = '"+slaId+"'");
                ps.execute();
                ps.close();

            }    
            if (!slaId.equals("")) { 
                PreparedStatement ps = con.prepareStatement("Delete from SLA_RESPONSE where criteria_ID = '"+slaId+"'");
                ps.execute();
                ps.close();

            }
            con.close();
        } catch (Exception e) {
            System.out.println(">> Error occued in Criteria Rule " + e);
            done = false;
        }
        return done;
    }


    public boolean createCategory(Category_Definition category, String userId)
    {
        Connection con;
        PreparedStatement ps;
        boolean done;
        String query;
        con = null;
        ps = null;
        done = false;
        query = "INSERT INTO HD_COMPLAIN_CATEGORY(CATEGORY_CODE,CATEGORY_NAME ,CATEGORY_DESCRIPTI" +
"ON,user_id,create_date,status)   VALUES (?,?,?,?,?,?)"
;
        try
        {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, (new ApplicationHelper()).getGeneratedId("Category_Definition"));
            ps.setString(2, category.getCATEGORY_NAME());
            ps.setString(3, category.getCATEGORY_DESCRIPTION());
            ps.setString(4, userId);
            ps.setDate(5, df.dateConvert(new Date()));
            ps.setString(6, category.getstatus());
            done = ps.executeUpdate() != -1;
//            System.out.println("<<<<<<done createCategory>>>>> "+done);
        }
        
        catch(Exception e)
        {
            System.out.println("--------------------------->");
            e.printStackTrace();
            System.out.println((new StringBuilder("WARNING:Error creating Category_Definition ->")).append(e.getMessage()).toString());
        }
        finally {
        	closeConnection(con, ps);
       }
        return done;
    }


    public boolean createSubCategory(Category_Sub_Definition category, String userId)
    {
        Connection con;
        PreparedStatement ps;
        boolean done;
        String query;
        con = null;
        ps = null;
        done = false;
        query = "INSERT INTO HD_COMPLAIN_SUBCATEGORY(SUB_COMPLAIN_ID,SUB_CATEGORY_CODE,CATEGORY_CODE,SUB_CATEGORY" +
"_NAME ,SUB_CATEGORY_DESC,user_id,create_date,status)   VALUES (?,?,?,?,?,?,?,?)"
;
        try
        {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, (new ApplicationHelper()).getGeneratedId("HD_COMPLAIN_SUBCATEGORY"));
            ps.setString(2, category.getSUB_CATEGORY_ID());
            ps.setString(3, category.getCATEGORY_ID());
            ps.setString(4, category.getSUB_CATEGORY_NAME());
            ps.setString(5, category.getSUB_CATEGORY_DESCRIPTION());
            ps.setString(6, userId);
            ps.setDate(7, df.dateConvert(new Date()));
            ps.setString(8, category.getStatus());
            done = ps.executeUpdate() != -1;
//            System.out.println("--subcategoryId in getSubCategoryByQuery2----> "+category.getSUB_CATEGORY_ID()+"  --name-> "+category.getSUB_CATEGORY_NAME()+"  --desc-> "+category.getSUB_CATEGORY_DESCRIPTION()+"  --done-> "+done);
        } 
        catch(Exception e)
        {
            System.out.println("--------------------------->");
            e.printStackTrace();
            System.out.println((new StringBuilder("WARNING:Error creating Sub_Category_Definition ->")).append(e.getMessage()).toString());
        }
        finally {
        	closeConnection(con, ps);
       }
        return done;
    }

    public List getSubCategoryByQuery2(String filter)
    {
        List _list;
        String query;
        Connection c;
        ResultSet rs;
        Statement s;
        _list = new ArrayList();
        Category_Sub_Definition category = null;
        query = "SELECT SUB_CATEGORY_CODE,CATEGORY_CODE,SUB_CATEGORY_NAME,SUB_CATEGORY_DESC,STATU" +
"S,user_id,create_date FROM HD_COMPLAIN_SUBCATEGORY  "
;
        query = (new StringBuilder(String.valueOf(query))).append(filter).toString();
        c = null;
        rs = null;
        s = null;
        try
        {
            c = getConnection();
            s = c.createStatement();
 //           Category_Sub_Definition category;
            for(rs = s.executeQuery(query); rs.next(); _list.add(category))
            {
                String subcategoryId = rs.getString("SUB_CATEGORY_CODE");
                String categoryId = rs.getString("CATEGORY_CODE");
                String name = rs.getString("SUB_CATEGORY_NAME");
                String desc = rs.getString("SUB_CATEGORY_DESC");
                String status = rs.getString("STATUS");
                String user_id = rs.getString("user_id");
                String create_date = rs.getString("create_date");
                category = new Category_Sub_Definition(categoryId,subcategoryId, name, desc, status, user_id, create_date);
                
            }
        }
        catch(Exception e)
        {
            System.out.println("--------------------------->");
            e.printStackTrace();
            System.out.println((new StringBuilder("WARNING:Error getting getSubCategoryByQuery2 ->")).append(e.getMessage()).toString());
        }
        finally {
        	closeConnection(c, s);
       }
        return _list;
    }

    public boolean updateCategory(Category_Definition category, String userId)
    {
        Connection con;
        PreparedStatement ps;
        boolean done;
        String query;
        con = null;
        ps = null;
        done = false;
        query = "update HD_COMPLAIN_CATEGORY set CATEGORY_NAME=? ,CATEGORY_DESCRIPTION=?,create_d" +
"ate=?,STATUS=? WHERE CATEGORY_CODE=? "
;
        try
        {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, category.getCATEGORY_NAME());
            ps.setString(2, category.getCATEGORY_DESCRIPTION());
            ps.setDate(3, df.dateConvert(new Date()));
            ps.setString(4, category.getstatus());
            ps.setString(5, category.getCATEGORY_ID());
        }
        catch(Exception e)
        {
            System.out.println("--------------------------->");
            e.printStackTrace();
            System.out.println((new StringBuilder("WARNING:Error UPDATING Category_Definition ->")).append(e.getMessage()).toString());
        }
        finally {
        	closeConnection(con, ps);
       }
        return done;
    }


    public boolean createItemCategory(Item_Definition item, String userId)
    {
        Connection con;
        PreparedStatement ps;
        boolean done;
        String query;
        con = null;
        ps = null;
        done = false;
        query = "INSERT INTO Item_Definition(ITEM_ID,SUB_CATEGORY_ID,NAME,DESCRIPTION,IS_DELETED," +
"USER_ID,CREATE_DATE,TECH_ID)   VALUES (?,?,?,?,?,?,?,?)"
;
        try
        {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, (new ApplicationHelper()).getGeneratedId("Item_Definition"));
            ps.setString(2, item.getSUB_CATEGORY_ID());
            ps.setString(3, item.getNAME());
            ps.setString(4, item.getDESCRIPTION());
            ps.setString(5, item.getIS_DELETED());
            ps.setString(6, userId);
            ps.setDate(7, df.dateConvert(new Date()));
            ps.setString(8, item.getTech_id());
            done = ps.executeUpdate() != -1;
        }
        catch(Exception e)
        {
            System.out.println("--------------------------->");
            e.printStackTrace();
            System.out.println((new StringBuilder("WARNING:Error creating Item_Definition ->")).append(e.getMessage()).toString());
        }
        finally {
        	closeConnection(con, ps);
       }
        return done;
    }

    public List getItemByQuery2(String filter)
    {
        List _list;
        String query;
        Connection c;
        ResultSet rs;
        Statement s;
        _list = new ArrayList();
        Item_Definition item = null;
        query = "SELECT ITEM_ID,SUB_CATEGORY_ID,NAME,DESCRIPTION,IS_DELETED,USER_ID,CREATE_DATE,T" +
"ECH_ID FROM Item_Definition  "
;
        query = (new StringBuilder(String.valueOf(query))).append(filter).toString();
        c = null;
        rs = null;
        s = null;
        try
        {
            c = getConnection();
            s = c.createStatement();
//            Item_Definition item;
            for(rs = s.executeQuery(query); rs.next(); _list.add(item))
            {
                String itemId = rs.getString("ITEM_ID");
                String subcategoryId = rs.getString("SUB_CATEGORY_ID");
                String name = rs.getString("NAME");
                String desc = rs.getString("DESCRIPTION");
                String categoryIsDeleted = rs.getString("IS_DELETED");
                String user_id = rs.getString("user_id");
                String create_date = rs.getString("create_date");
                String tech_id = rs.getString("tech_id");
                item = new Item_Definition(itemId, subcategoryId, name, desc, categoryIsDeleted, tech_id, user_id, create_date);
            }


        }
        catch(Exception e)
        {
            System.out.println("--------------------------->");
            e.printStackTrace();
            System.out.println((new StringBuilder("WARNING:Error getting getItemByQuery2 ->")).append(e.getMessage()).toString());
        }
        finally {
        	closeConnection(c, s);
       }
        return _list;
    }


    public List getCategoryByQuery2(String filter)
    {
        List _list;
        String query;
        Connection c;
        ResultSet rs;
        Statement s;
        _list = new ArrayList();
        Category_Definition category = null;
        query = "SELECT Dept_code,Dept_name AS DeptName,Dept_name as DeptName,Dept_Status,user_id,create_date FROM AM_AD_" +
"DEPARTMENT "+filter+" ORDER BY Dept_name "
;
//        query = (new StringBuilder(String.valueOf(query))).append(filter).toString();
        //System.out.println("====query in getCategoryByQuery2: "+query);
        c = null;
        rs = null;
        s = null;
        try
        {
            c = getConnection();
            s = c.createStatement();
//            Category_Definition category;
            for(rs = s.executeQuery(query); rs.next(); _list.add(category))
            {
                String categoryId = rs.getString("Dept_code");
                String categoryName = rs.getString("DeptName");
                String categoryDesc = rs.getString("DeptName");
                String status = rs.getString("Dept_Status");
                String user_id = rs.getString("user_id");
                String create_date = rs.getString("create_date");
                category = new Category_Definition(categoryId, categoryName, categoryDesc, status, user_id, create_date);
            }
        }
        catch(Exception e)
        {
            System.out.println("--------------------------->");
            e.printStackTrace();
            System.out.println((new StringBuilder("WARNING:Error getting getCategoryByQuery2 ->")).append(e.getMessage()).toString());
        }
        finally {
        	closeConnection(c, s,rs);
       }
        return _list;
    }

    public List getCategoryByQuery3(String filter)
    {
        List _list;
        String query;
        Connection c;
        ResultSet rs;
        Statement s;
        _list = new ArrayList();
        Category_Sub_Definition category = null;
        query = "SELECT Dept_code,Dept_name AS DeptName,Dept_name as DeptName,Dept_Status,user_id,create_date FROM AM_AD_" +
"DEPARTMENT "+filter+" ORDER BY Dept_name "
;
//        query = (new StringBuilder(String.valueOf(query))).append(filter).toString();
        //System.out.println("====query in getCategoryByQuery2: "+query);
        c = null;
        rs = null;
        s = null;
        try
        {
            c = getConnection();
            s = c.createStatement();
//            Category_Definition category;
            for(rs = s.executeQuery(query); rs.next(); _list.add(category))
            {
                String categoryId = rs.getString("Dept_code");
                String categoryName = rs.getString("DeptName");
                String categoryDesc = rs.getString("DeptName");
                String status = rs.getString("Dept_Status");
                String user_id = rs.getString("user_id");
                String create_date = rs.getString("create_date");
                category = new Category_Sub_Definition(categoryId, categoryName, categoryDesc, status, user_id, create_date);
            }
        }
        catch(Exception e)
        {
            System.out.println("--------------------------->");
            e.printStackTrace();
            System.out.println((new StringBuilder("WARNING:Error getting getCategoryByQuery2 ->")).append(e.getMessage()).toString());
        }
        finally {
        	closeConnection(c, s,rs);
       }
        return _list;
    }



    public boolean updateSubCategory(legend.admin.objects.Category_Sub_Definition Category_Sub_Definition) {

        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;
        //System.out.println("====Category_Sub_Definition.getSUB_CATEGORY_ID()===== "+Category_Sub_Definition.getSUB_CATEGORY_ID());
        //System.out.println("====Category_Sub_Definition.getSUB_CATEGORY_NAME()===== "+Category_Sub_Definition.getSUB_CATEGORY_NAME());
        String query = "UPDATE HD_COMPLAIN_SUBCATEGORY" + " SET sub_category_name = ?,sub_category_desc = ? , status = ?  WHERE sub_category_code =?";
        //System.out.println("====updateSubCategory query===== "+query);
        try {
            con = getConnection();
            ps = con.prepareStatement(query);        
            ps.setString(1, Category_Sub_Definition.getSUB_CATEGORY_NAME());
            ps.setString(2, Category_Sub_Definition.getSUB_CATEGORY_DESCRIPTION());
            ps.setString(3, Category_Sub_Definition.getStatus());
            ps.setString(4, Category_Sub_Definition.getSUB_CATEGORY_ID());
            //	System.out.println("----------->"+query);
            done = (ps.executeUpdate() != -1);

        } catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->" + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }

    public String createCategoryTmp(legend.admin.objects.Category category) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String result = "";
		String query = "INSERT INTO am_ad_categoryTmp"
				+ "(category_code,category_name"
				+ ",category_acronym,Required_for_fleet"
				+ ",Category_Class ,PM_Cycle_Period,mileage"
				+ ",Notify_Maint_Days ,notify_every_days,residual_value"
				+ ",Dep_rate ,Asset_Ledger,Dep_ledger"
				+ ",Accum_Dep_ledger ,gl_account,insurance_acct"
				+ ",license_ledger ,fuel_ledger,accident_ledger"
				+ ",Category_Status ,user_id,create_date"
				+ ",acct_type ,currency_Id, category_id,enforceBarcode,category_Type,TMP_CREATE_DATE,RECORD_TYPE,TMPID,"
				+ "upexassets,recaldep,residualchange,maxNo_Dep_Improve)"
		+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			String tmpId = (new ApplicationHelper()).getGeneratedId("am_ad_categoryTmp");
			//ps.setLong(1, System.currentTimeMillis());
			ps.setString(1, category.getCategoryCode());
			ps.setString(2, category.getCategoryName()); 
			ps.setString(3, category.getCategoryAcronym());
			ps.setString(4, category.getRequiredforFleet());
			ps.setString(5, category.getCategoryClass());
			ps.setString(6, category.getPmCyclePeriod());
			ps.setString(7, category.getMileage());
			ps.setString(8, category.getNotifyMaintdays());
			ps.setString(9, category.getNotifyEveryDays());
			ps.setString(10, category.getResidualValue());
			ps.setString(11, category.getDepRate());
			ps.setString(12, category.getAssetLedger());
			ps.setString(13, category.getDepLedger());
			ps.setString(14, category.getAccumDepLedger());
			ps.setString(15, category.getGlAccount());
			ps.setString(16, category.getInsuranceAcct());
			ps.setString(17, category.getLicenseLedger());
			ps.setString(18, category.getFuelLedger());
			ps.setString(19, category.getAccidentLedger());
			ps.setString(20, category.getCategoryStatus());
			ps.setString(21, category.getUserId());
//			System.out.println("Date Conversion: "+df.dateConvert(new java.util.Date()));
			ps.setDate(22, df.dateConvert(new java.util.Date()));
			ps.setString(23, category.getAcctType());
			ps.setString(24, category.getCurrencyId());
			ps.setLong(25, Integer.parseInt(category.getCategoryId()));
            ps.setString(26, category.getEnforceBarcode());
            ps.setString(27, category.getCategoryType());
            ps.setDate(28, df.dateConvert(new java.util.Date()));
            ps.setString(29, "I");
            ps.setString(30, tmpId);
            ps.setString(31, category.getUpexassets());
            ps.setString(32, category.getRecaldep());
            ps.setString(33, category.getResidualchange());
            ps.setInt(34, category.getNoOfImproveMnth());
			done=( ps.executeUpdate()!=-1);
			if(done) result = tmpId;

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " ERROR:Error creating category Temp -> " + e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return result;

	}  
	
	private legend.admin.objects.Category getACategory2Tmp(String filter) {
		legend.admin.objects.Category category = null;
		String query = "SELECT category_ID,category_code,category_name"
				+ ",category_acronym,Required_for_fleet"
				+ ",Category_Class ,PM_Cycle_Period,mileage"
				+ ",Notify_Maint_Days ,notify_every_days,residual_value"
				+ ",Dep_rate ,Asset_Ledger,Dep_ledger"
				+ ",Accum_Dep_ledger ,gl_account,insurance_acct"
				+ ",license_ledger ,fuel_ledger,accident_ledger"
				+ ",Category_Status ,user_id,create_date,upexassets,recaldep,residualchange"
				+ ",acct_type ,currency_Id,enforceBarcode,category_type,maxNo_Dep_Improve" + " FROM am_ad_categoryTmp ";

		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;
		//System.out.println("===> query in getACategory2Tmp: "+query);
		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String categoryId = rs.getString("category_ID");
				String categoryCode = rs.getString("category_code");
				String categoryName = rs.getString("category_name");
				String categoryAcronym = rs.getString("category_acronym");
				String requiredforFleet = rs.getString("Required_for_fleet");
				String categoryClass = rs.getString("Category_Class");
				String pmCyclePeriod = rs.getString("PM_Cycle_Period");
				String mileage = rs.getString("mileage");
				String notifyMaintdays = rs.getString("Notify_Maint_Days");
				String notifyEveryDays = rs.getString("notify_every_days");
				String residualValue = rs.getString("residual_value");
				String depRate = rs.getString("Dep_rate");
				String assetLedger = rs.getString("Asset_Ledger");
				String depLedger = rs.getString("Dep_ledger");
				String accumDepLedger = rs.getString("Accum_Dep_ledger");
				String glAccount = rs.getString("gl_account");
				String insuranceAcct = rs.getString("insurance_acct");
				String licenseLedger = rs.getString("license_ledger");
				String fuelLedger = rs.getString("fuel_ledger");
				String accidentLedger = rs.getString("accident_ledger");
				String categoryStatus = rs.getString("Category_Status");
				String userId = rs.getString("user_id");
				String createDate = sdf.format(rs.getDate("create_date"));
				String acctType = rs.getString("acct_type");
				String currencyId = rs.getString("currency_Id");
                String enforeBarcode = rs.getString("enforceBarcode");
                String categorytype = rs.getString("category_type");
                String upexassets = rs.getString("upexassets");
                String recaldep = rs.getString("recaldep");
                String residualchange = rs.getString("residualchange");
                int maxNoDepImprove = rs.getInt("maxNo_Dep_Improve");
                System.out.println("===> maxNoDepImprove: "+maxNoDepImprove);
                
				category = new legend.admin.objects.Category(categoryId,
						categoryCode, categoryName, categoryAcronym,
						requiredforFleet, categoryClass, pmCyclePeriod,
						mileage, notifyMaintdays, notifyEveryDays,
						residualValue, depRate, assetLedger, depLedger,
						accumDepLedger, glAccount, insuranceAcct,
						licenseLedger, fuelLedger, accidentLedger,
						categoryStatus, userId, createDate, acctType,
						currencyId,enforeBarcode,categorytype,upexassets,recaldep,residualchange,maxNoDepImprove);
//						category.setUpexassets(upexassets);
//						category.setRecaldep(recaldep);
//						category.setResidualchange(residualchange);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return category;

	}
	
	public java.util.List getZoneByQuery(String filter) {
		java.util.List _list = new java.util.ArrayList();
		Zone zone = null;
		String query = "SELECT Zone_Id, Zone_Code, Zone_Name,Zone_Acronym, Zone_Address,"
				+ "Zone_Phone , Zone_Fax, Zone_Status, User_Id, Create_Date FROM am_ad_Zone ";

		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String zoneId = rs.getString("Zone_Id");
				String zoneCode = rs.getString("Zone_Code");
				String zoneName = rs.getString("Zone_Name");
				String zoneAcronym = rs.getString("Zone_Acronym");
				String zoneAddress = rs.getString("Zone_Address");
				String zonePhone = rs.getString("Zone_Phone");
				String zoneFax = rs.getString("Zone_Fax");
				String zoneStatus = rs.getString("Zone_Status");
				String userId = rs.getString("User_Id");
				String createDate = rs.getString("Create_Date");

				zone = new legend.admin.objects.Zone(zoneId, zoneCode,
						zoneName, zoneAcronym, zoneAddress, zonePhone,
						zoneFax, zoneStatus, userId, createDate);
				_list.add(zone);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}
    private legend.admin.objects.Zone getAZone(String filter) {
		legend.admin.objects.Zone zone = null;
		String query = "SELECT Zone_Id, Zone_Code, Zone_Name"
				+ ", Zone_Acronym, Zone_Address"
				+ ",Zone_Phone , Zone_Fax, Zone_Status, User_Id, Create_Date"
				+ " FROM AM_AD_ZONE ";

		query = query + filter;
//		System.out.println("======query in getAZone: "+query);
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String zoneId = rs.getString("Zone_Id");
				String zoneCode = rs.getString("Zone_Code");
				String zoneName = rs.getString("Zone_Name");
				String zoneAcronym = rs.getString("Zone_Acronym");
				String zoneAddress = rs.getString("Zone_Address");
				String zonePhone = rs.getString("Zone_Phone");
				String zoneFax = rs.getString("Zone_Fax");
				String zoneStatus = rs.getString("Zone_Status");
				String userId = rs.getString("User_Id");
				String createDate = rs.getString("Create_Date");

				zone = new legend.admin.objects.Zone();
				zone.setZoneId(zoneId);
				zone.setZoneCode(zoneCode);
				zone.setZoneName(zoneName);
				zone.setZoneAcronym(zoneAcronym);
				zone.setZoneAddress(zoneAddress);
				zone.setZoneStatus(zoneStatus);
				zone.setZonePhone(zonePhone);
				zone.setZoneFax(zoneFax);
				zone.setUserId(userId);
				zone.setCreateDate(createDate);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return zone;

	}

	public boolean createZone(legend.admin.objects.Zone zone) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "INSERT INTO AM_AD_ZONE( Zone_Code, Zone_Name"
				+ ", Zone_Acronym, Zone_Address"
				+ ",Zone_Phone , Zone_Fax, Zone_Status, User_Id,Create_Date,Zone_iD) "
				+ " VALUES (?,?,?,?,?,?,?,?,?,?)";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			//ps.setLong(1, System.currentTimeMillis());
			ps.setString(1, zone.getZoneCode());
			ps.setString(2, zone.getZoneName());
			ps.setString(3, zone.getZoneAcronym());
			ps.setString(4, zone.getZoneAddress());
			ps.setString(5, zone.getZonePhone());
			ps.setString(6, zone.getZoneFax());
			ps.setString(7, zone.getZoneStatus());
			ps.setString(8, zone.getUserId());
			ps.setDate(9, df.dateConvert(new java.util.Date()));
			ps.setLong(10, Integer.parseInt(new ApplicationHelper().getGeneratedId("AM_AD_ZONE")));
			
			done=( ps.executeUpdate()!=-1);
		} catch (Exception e) {
			System.out.println("WARNING:Error creating Zone ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}


public void updateLoginSession(String userName) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		boolean done2 = false;		
		String userid = htmlUtil.getCodeName("SELECT USER_ID FROM  am_gb_user where USER_NAME = ? ",userName);
		String mtid = htmlUtil.getCodeName("SELECT MAX(mtid) FROM  gb_user_login where USER_ID = ? ",userid);
		String query = "UPDATE gb_user_login SET session_time = ? WHERE user_id =? AND MTID = ?";
		try {
			con = getConnection(); 
			ps = con.prepareStatement(query);
//			System.out.print("<<<<<getDateTime(): "+df.getDateTime().substring(10)+"   mtid: "+mtid+"   userid: "+userid);
			ps.setString(1,df.getDateTime().substring(10));
			ps.setString(2, userid);
			ps.setString(3, mtid);
			done=( ps.executeUpdate()!=-1);
		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " ERROR:Error Updating updateLoginSession session_time ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
}

public void updateTimeOutSession(String userName) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		boolean done2 = false;		
		String userid = htmlUtil.getCodeName("SELECT USER_ID FROM  am_gb_user where USER_NAME = ? ",userName);
		String mtid = htmlUtil.getCodeName("SELECT MAX(mtid) FROM  gb_user_login where USER_ID = ? ",userid);
		String query = "UPDATE gb_user_login SET time_out = ? WHERE user_id =? AND MTID = ?";
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
//			System.out.print("<<<<<getDateTime(): "+df.getDateTime().substring(10)+"   mtid: "+mtid+"   userid: "+userid);
			ps.setString(1,df.getDateTime().substring(10));
			ps.setString(2, userid);
			ps.setInt(3, Integer.parseInt(mtid)); 
			done=( ps.executeUpdate()!=-1);
		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " ERROR:Error Updating updateTimeOutSession time_out ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
}



}
