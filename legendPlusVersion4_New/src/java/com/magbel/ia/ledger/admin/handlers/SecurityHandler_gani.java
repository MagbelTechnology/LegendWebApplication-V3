package legend.admin.handlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import com.magbel.util.DataConnect;
import com.magbel.util.ApplicationHelper;
import java.util.Date;

/**
 * @author Rahman Oloritun
 * @Entities user,security privileges, userclass
 */
public class SecurityHandler_gani{

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

	public SecurityHandler_gani() {
		// TODO Auto-generated constructor stub
		sdf = new SimpleDateFormat("dd-MM-yyyy");
		df = new com.magbel.util.DatetimeFormat();
		System.out.println("USING_ " + this.getClass().getName());
	}

	
	public java.util.List getAllPrivileges() {
		java.util.List _list = new java.util.ArrayList();
		legend.admin.objects.Privilege privilege = null;
		String query = "SELECT role_uuid,role_name,role_wurl"
				+ " ,menu_type,priority" + "  FROM am_ad_privileges";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				String role_uuid = rs.getString("role_uuid");
				String role_name = rs.getString("role_name");
				String role_wurl = rs.getString("role_wurl");
				String menu_type = rs.getString("menu_type");
				String priority = rs.getString("priority");
				privilege = new legend.admin.objects.Privilege(role_uuid,
						role_name, role_wurl, menu_type, priority);
				_list.add(privilege);

			}
		} catch (Exception ex) {
			System.out.println("WARN: Error fetching all asset ->" + ex);
		} finally {
			closeConnection(con, ps);
		}
		return _list;

	}

	public legend.admin.objects.ClassPrivilege getClassPrivilege(
			String classid, String roleuuid) {
		legend.admin.objects.ClassPrivilege classprivilege = null;

		String query = "SELECT clss_uuid,role_uuid,role_view,role_addn,role_edit"
				+ " FROM am_ad_class_privileges"
				+ " WHERE clss_uuid=? AND role_uuid=?";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, classid);
			ps.setString(2, roleuuid);
			rs = ps.executeQuery();
			while (rs.next()) {
				String clss_uuid = rs.getString("clss_uuid");

				String role_uuid = rs.getString("role_uuid");

				String role_view = rs.getString("role_view");

				String role_addn = rs.getString("role_addn");

				String role_edit = rs.getString("role_edit");
				classprivilege = new legend.admin.objects.ClassPrivilege(
						clss_uuid, role_uuid, role_view, role_addn, role_edit);
			}
		} catch (Exception ex) {
			System.out.println("WARN: Error fetching Class Privileges ->" + ex);
		} finally {
			closeConnection(con, ps);
		}
		return classprivilege;

	}

	public boolean removeClassPrivilege(java.util.ArrayList list) {
		String query = "DELETE FROM am_ad_class_privileges"
				+ " WHERE clss_uuid=? AND role_uuid=?";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		legend.admin.objects.ClassPrivilege cp = null;
		int[] d = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			for (int i = 0; i < list.size(); i++) {
				cp = (legend.admin.objects.ClassPrivilege) list.get(i);

				ps.setString(1, cp.getClss_uuid());
				ps.setString(2, cp.getRole_uuid());

				ps.addBatch();
			}
			d = ps.executeBatch();

		} catch (Exception ex) {
			System.out.println("WARN: Error removing Class Privileges->" + ex);
		} finally {
			closeConnection(con, ps);
		}
		return (d.length > 0);
	}

	public boolean insertClassPrivileges(java.util.ArrayList list) {
		String query = "INSERT INTO am_ad_class_privileges(clss_uuid,role_uuid"
				+ " ,role_view,role_addn ,role_edit)"
				+ " VALUES(?,?,?,?,?)";
		Connection con = null;
		PreparedStatement ps = null;
		legend.admin.objects.ClassPrivilege cp = null;
		int[] d = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(query);

			for (int i = 0; i < list.size(); i++) {
				cp = (legend.admin.objects.ClassPrivilege) list.get(i);
				ps.setString(1, cp.getClss_uuid());
				ps.setString(2, cp.getRole_uuid());
				ps.setString(3, cp.getRole_view());
				ps.setString(4, cp.getRole_addn());
				ps.setString(5, cp.getRole_edit());
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

	public legend.admin.objects.SecurityClass getSecurityClassById(String filter) {

		legend.admin.objects.SecurityClass sc = null;
		String query = "SELECT class_id ,class_desc,class_name ,is_supervisor"
				+ "  ,class_status,user_id,create_date,fleet_admin"
				+ "  FROM am_gb_class WHERE  class_id=" + filter;

		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String classId = rs.getString("class_id");
				String description = rs.getString("class_desc");

				String className = rs.getString("class_name");

				String isSupervisor = rs.getString("is_supervisor");

				String classStatus = rs.getString("class_status");

				String userId = rs.getString("user_id");

				String createDate = rs.getString("create_date");

				String fleetAdmin = rs.getString("fleet_admin");
				sc = new legend.admin.objects.SecurityClass(classId,
						description, className, isSupervisor, classStatus,
						userId, createDate, fleetAdmin);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return sc;

	}

	public legend.admin.objects.SecurityClass getSecurityClassByName(String filter) {

		legend.admin.objects.SecurityClass sc = null;
		String query = "SELECT class_id ,class_desc,class_name ,is_supervisor"
				+ "  ,class_status,user_id,create_date,fleet_admin"
				+ "  FROM am_gb_class WHERE  class_name='" + filter+"'";

		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String classId = rs.getString("class_id");
				String description = rs.getString("class_desc");

				String className = rs.getString("class_name");

				String isSupervisor = rs.getString("is_supervisor");

				String classStatus = rs.getString("class_status");

				String userId = rs.getString("user_id");

				String createDate = rs.getString("create_date");

				String fleetAdmin = rs.getString("fleet_admin");
				sc = new legend.admin.objects.SecurityClass(classId,
						description, className, isSupervisor, classStatus,
						userId, createDate, fleetAdmin);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return sc;

	}

	public boolean updateSecurityClass(legend.admin.objects.SecurityClass sc) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "UPDATE am_gb_class SET class_desc = ?,class_name = ?"
				+ "   ,is_supervisor = ?,class_status = ?"
				+ "   ,fleet_admin = ?" + "   WHERE class_id = ?";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, sc.getDescription());
			ps.setString(2, sc.getClassName());
			ps.setString(3, sc.getIsSupervisor());
			ps.setString(4, sc.getClassStatus());
			ps.setString(5, sc.getFleetAdmin());
			ps.setString(6, sc.getClassId());
			done=( ps.executeUpdate()!=-1);

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " ERROR:Error Updating Security Class ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

	public boolean createSecurityClass(legend.admin.objects.SecurityClass sc) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "INSERT INTO am_gb_class(class_desc,class_name,is_supervisor,class_status"
				+ "    ,user_id,create_date ,fleet_admin,class_id)"
				+ "    VALUES (?,?,?,?,?,?,?,?)";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, sc.getDescription());
			ps.setString(2, sc.getClassName());
			ps.setString(3, sc.getIsSupervisor());
			ps.setString(4, sc.getClassStatus());
			ps.setString(5, sc.getUserId());
			ps.setDate(6, df.dateConvert(new java.util.Date()));
			ps.setString(7, sc.getFleetAdmin());
			//ps.setLong(8, System.currentTimeMillis());
			ps.setString(8, new ApplicationHelper()
					.getGeneratedId("am_gb_class"));
			
			done=( ps.executeUpdate()!=-1);

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " ERROR:Error Updating Security Class ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

	public java.util.List getSecurityClassByQuery(String filter) {
		java.util.List _list = new java.util.ArrayList();
		legend.admin.objects.SecurityClass sc = null;
		String query = "SELECT class_id ,class_desc,class_name ,is_supervisor"
				+ "  ,class_status,user_id,create_date,fleet_admin"
				+ "  FROM am_gb_class ";

		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String classId = rs.getString("class_id");
				String description = rs.getString("class_desc");

				String className = rs.getString("class_name");

				String isSupervisor = rs.getString("is_supervisor");

				String classStatus = rs.getString("class_status");

				String userId = rs.getString("user_id");

				String createDate = rs.getString("create_date");

				String fleetAdmin = rs.getString("fleet_admin");
				sc = new legend.admin.objects.SecurityClass(classId,
						description, className, isSupervisor, classStatus,
						userId, createDate, fleetAdmin);

				_list.add(sc);
			}

		} catch (Exception e) {
		          System.out.println("this error is generated in getSecurityClassByQuery(String filter) of securityHandler class");
            e.printStackTrace();

        }

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}

	public boolean updateClassPrivilege(java.util.ArrayList list) {
		String query = "UPDATE am_ad_class_privileges SET "
				+ "role_view = ?,role_addn = ?,role_edit = ?"
				+ " WHERE clss_uuid=? AND role_uuid=?";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		legend.admin.objects.ClassPrivilege cp = null;
		int[] d = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(query);

			for (int i = 0; i < list.size(); i++) {
				cp = (legend.admin.objects.ClassPrivilege) list.get(i);
				ps.setString(1, cp.getRole_view());
				ps.setString(2, cp.getRole_addn());
				ps.setString(3, cp.getRole_edit());
				ps.setString(4, cp.getClss_uuid());
				ps.setString(5, cp.getRole_uuid());

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

	public java.util.List getAllUsers() {
		java.util.List _list = new java.util.ArrayList();
		legend.admin.objects.User user = null;
		String query = "SELECT User_Id, User_Name, Full_Name"
				+ ", Legacy_Sys_Id, Class, Branch, Password"
				+ ", Phone_No, is_Supervisor, Must_Change_Pwd"
				+ ", Login_Status, User_Status, UserId, Create_Date"
				+ ", Password_Expiry, Login_Date, Login_System, Fleet_Admin"
				+ ", email, Branch,password_changed" + " FROM AM_GB_USER";

		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String userId = rs.getString("User_Id");
				String userName = rs.getString("User_Name");
				String fullName = rs.getString("Full_Name");
				String legacySysId = rs.getString("Legacy_Sys_Id");
				String Class = rs.getString("Class");
				String branch = rs.getString("Branch");
				String password = rs.getString("Password");
				String phoneNo = rs.getString("Phone_No");
				String isSupervisor = rs.getString("Is_Supervisor");
				String mustChangePwd = rs.getString("Must_Change_Pwd");
				String loginStatus = rs.getString("Login_Status");
				String userStatus = rs.getString("User_Status");
				String user_Id = rs.getString("UserId");
				String createDate = rs.getString("Create_Date");
				String passwordExpiry = rs.getString("Password_Expiry");
				String loginDate = rs.getString("Login_Date");
				String loginSystem = rs.getString("Login_System");
				String fleetAdmin = rs.getString("Fleet_Admin");
				String email = rs.getString("email");
				String branchCode = rs.getString("Branch");
				String pwdChanged = rs.getString("password_changed");
				
				user = new legend.admin.objects.User();
				user.setUserId(userId);
				user.setUserName(userName);
				user.setUserFullName(fullName);
				user.setLegacySystemId(legacySysId);
				user.setUserClass(Class);
				user.setBranch(branch);
				user.setPassword(password);
				user.setPhoneNo(phoneNo);
				user.setIsSupervisor(isSupervisor);
				user.setMustChangePwd(mustChangePwd);
				user.setLoginStatus(loginStatus);
				user.setCreatedBy(user_Id);
				user.setCreateDate(createDate);
				user.setPwdExpiry(passwordExpiry);
				user.setLastLogindate(loginDate);
				user.setLoginSystem(loginSystem);
				user.setFleetAdmin(fleetAdmin);
				user.setEmail(email);
				user.setBranch(branchCode);
				user.setUserStatus(userStatus);
				user.setPwdChanged(pwdChanged);
				_list.add(user);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}

	public java.util.ArrayList getUserByQuery(String filter) {
		java.util.ArrayList _list = new java.util.ArrayList();
		legend.admin.objects.User user = null;
		String query = "SELECT User_Id, User_Name, Full_Name"
				+ ", Legacy_Sys_Id, Class, Branch, Password"
				+ ", Phone_No, is_Supervisor, Must_Change_Pwd"
				+ ", Login_Status, User_Status, UserId, Create_Date"
				+ ", Password_Expiry, Login_Date, Login_System, Fleet_Admin"
				+ ", email, Branch, password_changed,Expiry_Date,branch_restriction"
                                + ", approval_limit,approval_level"
				+ " FROM AM_GB_USER WHERE User_Id IS NOT NULL";
		query += filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String userId = rs.getString("User_Id");
				String userName = rs.getString("User_Name");
				String fullName = rs.getString("Full_Name");
				String legacySysId = rs.getString("Legacy_Sys_Id");
				String Class = rs.getString("Class");
				String branch = rs.getString("Branch");
				String password = rs.getString("Password");
				String phoneNo = rs.getString("Phone_No");
				String isSupervisor = rs.getString("Is_Supervisor");
				String mustChangePwd = rs.getString("Must_Change_Pwd");
				String loginStatus = rs.getString("Login_Status");
				String userStatus = rs.getString("User_Status");
				String user_Id = rs.getString("UserId");
				String createDate = rs.getString("Create_Date");
				String passwordExpiry = rs.getString("Password_Expiry");
				String loginDate = rs.getString("Login_Date");
				String loginSystem = rs.getString("Login_System");
				String fleetAdmin = rs.getString("Fleet_Admin");
				String email = rs.getString("email");
				String branchCode = rs.getString("Branch");
				String pwdChanged = rs.getString("password_changed");
                                Date expiry_date = rs.getDate("Expiry_Date");
                                String branch_restriction = rs.getString("branch_restriction");
                                String apprvLimit = rs.getString("approval_limit");
                                String apprvLevel = rs.getString("approval_level");
				
				user = new legend.admin.objects.User();
				user.setUserId(userId);
				user.setUserName(userName);
				user.setUserFullName(fullName);
				user.setLegacySystemId(legacySysId);
				user.setUserClass(Class);
				user.setBranch(branch);
				user.setPassword(password);
				user.setPhoneNo(phoneNo);
				user.setIsSupervisor(isSupervisor);
				user.setMustChangePwd(mustChangePwd);
				user.setLoginStatus(loginStatus);
				user.setUserStatus(userStatus);
				user.setCreatedBy(user_Id);
				user.setCreateDate(createDate);

				user.setPwdExpiry(passwordExpiry);
				user.setLastLogindate(loginDate);
				user.setLoginSystem(loginSystem);
				user.setFleetAdmin(fleetAdmin);
				user.setEmail(email);
				user.setBranch(branchCode);
				user.setPwdChanged(pwdChanged);
                                user.setApprvLimit(apprvLimit);
                                user.setBranchRestrict(branch_restriction);
                                user.setApprvLevel(apprvLevel);
                                user.setExpDate(expiry_date);
				_list.add(user);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}

	public legend.admin.objects.User getUserByUserID(String UserID) {
		legend.admin.objects.User user = null;
    String expiryDate= "";
		String query = "SELECT User_Id, User_Name, Full_Name"
				+ ", Legacy_Sys_Id, Class, Branch, Password"
				+ ", Phone_No, is_Supervisor, Must_Change_Pwd"
				+ ", Login_Status, User_Status, UserId, Create_Date"
				+ ", Password_Expiry, Login_Date, Login_System, Fleet_Admin"
				+ ", email, Branch,password_changed,branch_restriction, Expiry_Days,Expiry_Date" + " FROM AM_GB_USER WHERE User_Id = '"
				+ UserID + "'";

		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String userId = rs.getString("User_Id");
				String userName = rs.getString("User_Name");
				String fullName = rs.getString("Full_Name");
				String legacySysId = rs.getString("Legacy_Sys_Id");
				String Class = rs.getString("Class");
				String branch = rs.getString("Branch");
				String password = rs.getString("Password");
				String phoneNo = rs.getString("Phone_No");
				String isSupervisor = rs.getString("Is_Supervisor");
				String mustChangePwd = rs.getString("Must_Change_Pwd");
				String loginStatus = rs.getString("Login_Status");
				String userStatus = rs.getString("User_Status");
				String user_Id = rs.getString("UserId");
				String createDate = rs.getString("Create_Date");
				String passwordExpiry = rs.getString("Password_Expiry");
				String loginDate = rs.getString("Login_Date");
				String loginSystem = rs.getString("Login_System");
				String fleetAdmin = rs.getString("Fleet_Admin");
				String email = rs.getString("email");
				String branchCode = rs.getString("Branch");
				String pwdChanged = rs.getString("password_changed");


                String branchRestrict = rs.getString("branch_restriction");
                int expiryDays = rs.getInt("Expiry_Days");
                //String branch_posting_restriction = rs.getString("branch_posting_restriction");
             if(rs.getDate("Expiry_Date") ==null){}
             else{
                 expiryDate = rs.getDate("Expiry_Date").toString();
             }
				user = new legend.admin.objects.User();
				user.setUserId(userId);
				user.setUserName(userName);
				user.setUserFullName(fullName);
				user.setLegacySystemId(legacySysId);
				user.setUserClass(Class);
				user.setBranch(branch);
				user.setPassword(password);
				user.setPhoneNo(phoneNo);
				user.setIsSupervisor(isSupervisor);
				user.setMustChangePwd(mustChangePwd);
				user.setLoginStatus(loginStatus);
				user.setUserStatus(userStatus);
				user.setCreatedBy(user_Id);
				user.setCreateDate(createDate);
				user.setUserStatus(userStatus);
				user.setPwdExpiry(passwordExpiry);
				user.setLastLogindate(loginDate);
				user.setLoginSystem(loginSystem);
				user.setFleetAdmin(fleetAdmin);
				user.setEmail(email);
				user.setBranch(branchCode);
				user.setPwdChanged(pwdChanged);
                user.setBranchRestrict(branchRestrict);
                user.setExpiryDays(expiryDays);
                user.setExpiryDate(expiryDate);
                //user.setPostingRestricted(branch_posting_restriction);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return user;
	}

	public legend.admin.objects.User getUserByUserName(String UserName) {
		legend.admin.objects.User user = null;
		String query = "SELECT User_Id, User_Name, Full_Name"
				+ ", Legacy_Sys_Id, Class, Branch, Password"
				+ ", Phone_No, is_Supervisor, Must_Change_Pwd"
				+ ", Login_Status, User_Status, UserId, Create_Date"
				+ ", Password_Expiry, Login_Date, Login_System, Fleet_Admin"
				+ ", email, Branch,password_changed" + " FROM AM_GB_USER WHERE User_Name = '"
				+ UserName + "'";

		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String userId = rs.getString("User_Id");
				String userName = rs.getString("User_Name");
				String fullName = rs.getString("Full_Name");
				String legacySysId = rs.getString("Legacy_Sys_Id");
				String Class = rs.getString("Class");
				String branch = rs.getString("Branch");
				String password = rs.getString("Password");
				String phoneNo = rs.getString("Phone_No");
				String isSupervisor = rs.getString("Is_Supervisor");
				String mustChangePwd = rs.getString("Must_Change_Pwd");
				String loginStatus = rs.getString("Login_Status");
				String userStatus = rs.getString("User_Status");
				String user_Id = rs.getString("UserId");
				String createDate = rs.getString("Create_Date");
				String passwordExpiry = rs.getString("Password_Expiry");
				String loginDate = rs.getString("Login_Date");
				String loginSystem = rs.getString("Login_System");
				String fleetAdmin = rs.getString("Fleet_Admin");
				String email = rs.getString("email");
				String branchCode = rs.getString("Branch");
				String pwdChanged = rs.getString("password_changed");

				user = new legend.admin.objects.User();
				user.setUserId(userId);
				user.setUserName(userName);
				user.setUserFullName(fullName);
				user.setLegacySystemId(legacySysId);
				user.setUserClass(Class);
				user.setBranch(branch);
				user.setPassword(password);
				user.setPhoneNo(phoneNo);
				user.setIsSupervisor(isSupervisor);
				user.setMustChangePwd(mustChangePwd);
				user.setLoginStatus(loginStatus);
				user.setUserStatus(userStatus);
				user.setCreatedBy(user_Id);
				user.setCreateDate(createDate);

				user.setPwdExpiry(passwordExpiry);
				user.setLastLogindate(loginDate);
				user.setLoginSystem(loginSystem);
				user.setFleetAdmin(fleetAdmin);
				user.setEmail(email);
				user.setBranch(branchCode);
				user.setPwdChanged(pwdChanged);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return user;
	}

	public boolean createUser(legend.admin.objects.User user) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
        String query ="";

/*
		String query = "INSERT INTO AM_GB_USER( User_Name, Full_Name, Legacy_Sys_Id"
				+ ", Class, Branch, Password, Phone_No, Is_Supervisor, Must_Change_Pwd"
				+ ", Login_Status, User_Status, UserId, Create_Date, Password_Expiry"
				+ ",Fleet_Admin, Email,User_id,branch_restriction, Expiry_Days,Expiry_Date)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
*/
		try {
            

            if(user.getExpiryDate()== "" || user.getExpiryDate().equalsIgnoreCase("null") || user.getExpiryDate()==null || user.getExpiryDate().equalsIgnoreCase(""))
            {

                 query = "INSERT INTO AM_GB_USER( User_Name, Full_Name, Legacy_Sys_Id"
				+ ", Class, Branch, Password, Phone_No, Is_Supervisor, Must_Change_Pwd"
				+ ", Login_Status, User_Status, UserId, Create_Date, Password_Expiry"
				+ ",Fleet_Admin, Email,User_id,branch_restriction, Expiry_Days)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            con = getConnection();
			ps = con.prepareStatement(query);

			ps.setString(1, user.getUserName());
			ps.setString(2, user.getUserFullName());
			ps.setString(3, user.getLegacySystemId());
			ps.setString(4, user.getUserClass());
			ps.setString(5, user.getBranch());
			ps.setString(6, user.getPassword());
			ps.setString(7, user.getPhoneNo());
			ps.setString(8, user.getIsSupervisor());
			ps.setString(9, user.getMustChangePwd());
			ps.setString(10, user.getLoginStatus());
			ps.setString(11, user.getUserStatus());
			ps.setString(12, user.getCreatedBy());
			ps.setDate(13, df.dateConvert(new java.util.Date()));
			ps.setDate(14, df.dateConvert(df.addDayToDate(new java.util.Date(), Integer.parseInt(user.getPwdExpiry()))));
			ps.setString(15, user.getFleetAdmin());
			ps.setString(16, user.getEmail());
			//ps.setLong(17, System.currentTimeMillis());
			ps.setString(17, new ApplicationHelper()
					.getGeneratedId("AM_GB_USER"));

            ps.setString(18, user.getBranchRestrict());
            ps.setInt(19, user.getExpiryDays());

            /*
            String temDate= user.getExpiryDate();

            if(temDate == ""||temDate.equalsIgnoreCase("null")){
            ps.setDate(20, null);
            }else{

            ps.setDate(20,df.dateConvert(temDate));
            }
            */

			done = (ps.executeUpdate() != -1);

            }//if
            else{
            query = "INSERT INTO AM_GB_USER( User_Name, Full_Name, Legacy_Sys_Id"
				+ ", Class, Branch, Password, Phone_No, Is_Supervisor, Must_Change_Pwd"
				+ ", Login_Status, User_Status, UserId, Create_Date, Password_Expiry"
				+ ",Fleet_Admin, Email,User_id,branch_restriction, Expiry_Days,Expiry_Date)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

             con = getConnection();
			ps = con.prepareStatement(query);
			

			ps.setString(1, user.getUserName());
			ps.setString(2, user.getUserFullName());
			ps.setString(3, user.getLegacySystemId());
			ps.setString(4, user.getUserClass());
			ps.setString(5, user.getBranch());
			ps.setString(6, user.getPassword());
			ps.setString(7, user.getPhoneNo());
			ps.setString(8, user.getIsSupervisor());
			ps.setString(9, user.getMustChangePwd());
			ps.setString(10, user.getLoginStatus());
			ps.setString(11, user.getUserStatus());
			ps.setString(12, user.getCreatedBy());
			ps.setDate(13, df.dateConvert(new java.util.Date()));
			ps.setDate(14, df.dateConvert(df.addDayToDate(new java.util.Date(), Integer.parseInt(user.getPwdExpiry()))));
			ps.setString(15, user.getFleetAdmin());
			ps.setString(16, user.getEmail());
			//ps.setLong(17, System.currentTimeMillis());
			ps.setString(17, new ApplicationHelper()
					.getGeneratedId("AM_GB_USER"));

            ps.setString(18, user.getBranchRestrict());
            ps.setInt(19, user.getExpiryDays());
           
            String temDate= user.getExpiryDate();

            

            ps.setDate(20,df.dateConvert(temDate));
           
            
			done = (ps.executeUpdate() != -1);


            }//else

		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

	public boolean updateUser(legend.admin.objects.User user) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query ="";
        /*
        String query = "UPDATE AM_GB_USER  SET  User_Name = ?, Full_Name = ?"
				+ " ,Legacy_Sys_Id = ?, Class = ?, Branch = ?, Password = ?, Phone_No = ?"
				+ ",Is_Supervisor = ?,Must_Change_Pwd = ?, Login_Status = ?, User_Status = ?"
				+ ", Fleet_Admin = ?, Email = ?,branch_restriction =?, Expiry_Days =?, Expiry_Date=? " + " WHERE User_Id = ?";
        */
		try {
            if(user.getExpiryDate()== "" || user.getExpiryDate().equalsIgnoreCase("null") ||user.getExpiryDate().equalsIgnoreCase("")  ){

                query = "UPDATE AM_GB_USER  SET  User_Name = ?, Full_Name = ?"
				+ " ,Legacy_Sys_Id = ?, Class = ?, Branch = ?, Password = ?, Phone_No = ?"
				+ ",Is_Supervisor = ?,Must_Change_Pwd = ?, Login_Status = ?, User_Status = ?"
				+ ", Fleet_Admin = ?, Email = ?,branch_restriction =?, Expiry_Days =? " + " WHERE User_Id = ?";
            con = getConnection();
			ps = con.prepareStatement(query);

			ps.setString(1, user.getUserName());
			ps.setString(2, user.getUserFullName());
			ps.setString(3, user.getLegacySystemId());
			ps.setString(4, user.getUserClass());
			ps.setString(5, user.getBranch());
			ps.setString(6, user.getPassword());
			ps.setString(7, user.getPhoneNo());
			ps.setString(8, user.getIsSupervisor());
			ps.setString(9, user.getMustChangePwd());
			ps.setString(10, user.getLoginStatus());

			ps.setString(11, user.getUserStatus());

			ps.setString(12, user.getFleetAdmin());
			ps.setString(13, user.getEmail());

            ps.setString(14, user.getBranchRestrict());
            ps.setInt(15, user.getExpiryDays());
            //ps.setDate(16,df.dateConvert(user.getExpiryDate()));


            ps.setString(16, user.getUserId());

			done=( ps.executeUpdate()!=-1);


            }else{
                query = "UPDATE AM_GB_USER  SET  User_Name = ?, Full_Name = ?"
				+ " ,Legacy_Sys_Id = ?, Class = ?, Branch = ?, Password = ?, Phone_No = ?"
				+ ",Is_Supervisor = ?,Must_Change_Pwd = ?, Login_Status = ?, User_Status = ?"
				+ ", Fleet_Admin = ?, Email = ?,branch_restriction =?, Expiry_Days =?, Expiry_Date=? " + " WHERE User_Id = ?";
			con = getConnection();
			ps = con.prepareStatement(query);

			ps.setString(1, user.getUserName());
			ps.setString(2, user.getUserFullName());
			ps.setString(3, user.getLegacySystemId());
			ps.setString(4, user.getUserClass());
			ps.setString(5, user.getBranch());
			ps.setString(6, user.getPassword());
			ps.setString(7, user.getPhoneNo());
			ps.setString(8, user.getIsSupervisor());
			ps.setString(9, user.getMustChangePwd());
			ps.setString(10, user.getLoginStatus());

			ps.setString(11, user.getUserStatus());

			ps.setString(12, user.getFleetAdmin());
			ps.setString(13, user.getEmail());

            ps.setString(14, user.getBranchRestrict());
            ps.setInt(15, user.getExpiryDays());
            ps.setDate(16,df.dateConvert(user.getExpiryDate()));
			
            
            ps.setString(17, user.getUserId());
			done=( ps.executeUpdate()!=-1);
            }//else

            //TO RESET EXPIRY DATE  TO NULL IF EXPIRY DAY IS 0
            if(user.getExpiryDays() == 0){
         updateExpiryDate(Integer.parseInt(user.getUserId()));
        }

        } catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

	public boolean confirmPassword(String iden, String pass) {

		String query = "SELECT PASSWORD FROM AM_GB_USER   "
				+ "WHERE USER_ID = " + iden;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean done = false;
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				done = (pass.equals(rs.getString("PASSWORD")));
			}

		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps, rs);
		}
		return done;
	}

	public boolean updateLogins(String userid, String ipaddress) {

		String query = "UPDATE AM_GB_USER  SET LOGIN_STATUS= 1 ,LOGIN_SYSTEM='"
				+ ipaddress + "'," + "login_date =?"
				+ " WHERE   USER_ID = " + userid;
		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setDate(1, df.dateConvert(new java.util.Date()));
			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;
	}

	public boolean changePassword(String userid, String password, String expiry) {

		String query = "UPDATE am_gb_User"
				+ " SET Password =?,password_changed = ?,password_expiry =  ?,must_change_pwd=?"
				+ " WHERE   USER_ID = ?";
		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, password);
			ps.setString(2, "Y");
			ps.setDate(3, df.dateConvert(df.addDayToDate(new java.util.Date(), Integer.parseInt(expiry))));
			ps.setString(4, "N");
			ps.setString(5, userid);
			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;
	}

	public boolean updateLogins(String userid, String status, String ipaddress) {

		String query = "UPDATE AM_GB_USER  SET LOGIN_STATUS= " + status
				+ ",LOGIN_SYSTEM='" + ipaddress + "',"
				+ "login_date = GetDate()" + " WHERE   USER_ID = " + userid;
		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;
	}

	public boolean queryPexpiry(String userid) {
		String query = "SELECT password_expiry FROM AM_GB_USER   "
				+ "WHERE USER_ID = " + userid;

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean done = false;
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				String exDate = rs.getString("password_expiry");
                //System.out.println("the value of exDate from security handler is /////" +exDate);
				int diff = df.getDayDifferenceNoABS(exDate, sdf.format(new java.util.Date()));

                //System.out.println("/////////the value of date difference is ///////"+ diff);
                if ( diff <= 0) {
					done = true;
				}

			}

		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps, rs);
		}
		return done;
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
	public java.util.ArrayList getAUsers(){
		   String filter = " AND Login_Status = '1'";
		   java.util.ArrayList list = getUserByQuery(filter);
		   return list;
		}

    public String getBranchRestrictedUser(String userId){
        String branchRestrict = "";
		String query = "SELECT branch_restriction "
                + " FROM AM_GB_USER WHERE User_Id = '"
				+ userId + "'";

		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
                branchRestrict = rs.getString("branch_restriction");
             }

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}

        return branchRestrict;
    }




    public boolean updateExpiryDate(int user_id) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;

		try {

              String  query = "update am_gb_user set expiry_date = null where user_id = "+user_id;
			con = getConnection();
			ps = con.prepareStatement(query);


			done=( ps.executeUpdate()!=-1);

		} catch (Exception e) {
			System.out.println("WARNING:Error updateExpiryDate() Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}


 //modification lanre
    public boolean createManageUser(legend.admin.objects.User user,String limit) {


		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
        String query ="";

/*
		String query = "INSERT INTO AM_GB_USER( User_Name, Full_Name, Legacy_Sys_Id"
				+ ", Class, Branch, Password, Phone_No, Is_Supervisor, Must_Change_Pwd"
				+ ", Login_Status, User_Status, UserId, Create_Date, Password_Expiry"
				+ ",Fleet_Admin, Email,User_id,branch_restriction, Expiry_Days,Expiry_Date)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
*/
		try {


            if(user.getExpiryDate()== "" || user.getExpiryDate().equalsIgnoreCase("null") || user.getExpiryDate()==null || user.getExpiryDate().equalsIgnoreCase(""))
            {

                 query = "INSERT INTO AM_GB_USER( User_Name, Full_Name, Legacy_Sys_Id"
				+ ", Class, Branch, Password, Phone_No, Is_Supervisor, Must_Change_Pwd"
				+ ", Login_Status, User_Status, UserId, Create_Date, Password_Expiry"
				+ ",Fleet_Admin, Email,User_id,branch_restriction, Expiry_Days,Approval_Limit)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            con = getConnection();
			ps = con.prepareStatement(query);

			ps.setString(1, user.getUserName());
			ps.setString(2, user.getUserFullName());
			ps.setString(3, user.getLegacySystemId());
			ps.setString(4, user.getUserClass());
			ps.setString(5, user.getBranch());
			ps.setString(6, user.getPassword());
			ps.setString(7, user.getPhoneNo());
			ps.setString(8, user.getIsSupervisor());
			ps.setString(9, user.getMustChangePwd());
			ps.setString(10, user.getLoginStatus());
			ps.setString(11, user.getUserStatus());
			ps.setString(12, user.getCreatedBy());
			ps.setDate(13, df.dateConvert(new java.util.Date()));
			ps.setDate(14, df.dateConvert(df.addDayToDate(new java.util.Date(), Integer.parseInt(user.getPwdExpiry()))));
			ps.setString(15, user.getFleetAdmin());
			ps.setString(16, user.getEmail());
			//ps.setLong(17, System.currentTimeMillis());
			ps.setString(17, new ApplicationHelper()
					.getGeneratedId("AM_GB_USER"));

            ps.setString(18, user.getBranchRestrict());
            ps.setInt(19, user.getExpiryDays());
            ps.setString(20, limit);
            //--ps.setString(21, user.getPostingRestricted());
            /*
            String temDate= user.getExpiryDate();

            if(temDate == ""||temDate.equalsIgnoreCase("null")){
            ps.setDate(20, null);
            }else{

            ps.setDate(20,df.dateConvert(temDate));
            }
            */

			done = (ps.executeUpdate() != -1);

            }//if
            else{
            query = "INSERT INTO AM_GB_USER( User_Name, Full_Name, Legacy_Sys_Id"
				+ ", Class, Branch, Password, Phone_No, Is_Supervisor, Must_Change_Pwd"
				+ ", Login_Status, User_Status, UserId, Create_Date, Password_Expiry"
				+ ",Fleet_Admin, Email,User_id,branch_restriction, Expiry_Days,Expiry_Date,Approval_Limit)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

             con = getConnection();
			ps = con.prepareStatement(query);


			ps.setString(1, user.getUserName());
			ps.setString(2, user.getUserFullName());
			ps.setString(3, user.getLegacySystemId());
			ps.setString(4, user.getUserClass());
			ps.setString(5, user.getBranch());
			ps.setString(6, user.getPassword());
			ps.setString(7, user.getPhoneNo());
			ps.setString(8, user.getIsSupervisor());
			ps.setString(9, user.getMustChangePwd());
			ps.setString(10, user.getLoginStatus());
			ps.setString(11, user.getUserStatus());
			ps.setString(12, user.getCreatedBy());
			ps.setDate(13, df.dateConvert(new java.util.Date()));
			ps.setDate(14, df.dateConvert(df.addDayToDate(new java.util.Date(), Integer.parseInt(user.getPwdExpiry()))));
			ps.setString(15, user.getFleetAdmin());
			ps.setString(16, user.getEmail());
			//ps.setLong(17, System.currentTimeMillis());
			ps.setString(17, new ApplicationHelper()
					.getGeneratedId("AM_GB_USER"));

            ps.setString(18, user.getBranchRestrict());
            ps.setInt(19, user.getExpiryDays());

            String temDate= user.getExpiryDate();



            ps.setDate(20,df.dateConvert(temDate));

            ps.setString(21, limit);
            //--ps.setString(22, user.getPostingRestricted());
			done = (ps.executeUpdate() != -1);


            }//else

		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;


	}

	public boolean updateManageUser(legend.admin.objects.User user,String limit) {


		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query ="";
        /*
        String query = "UPDATE AM_GB_USER  SET  User_Name = ?, Full_Name = ?"
				+ " ,Legacy_Sys_Id = ?, Class = ?, Branch = ?, Password = ?, Phone_No = ?"
				+ ",Is_Supervisor = ?,Must_Change_Pwd = ?, Login_Status = ?, User_Status = ?"
				+ ", Fleet_Admin = ?, Email = ?,branch_restriction =?, Expiry_Days =?, Expiry_Date=? " + " WHERE User_Id = ?";
        */
		try {
            if(user.getExpiryDate()== "" || user.getExpiryDate().equalsIgnoreCase("null") ||user.getExpiryDate().equalsIgnoreCase("")  ){

                query = "UPDATE AM_GB_USER  SET  User_Name = ?, Full_Name = ?"
				+ " ,Legacy_Sys_Id = ?, Class = ?, Branch = ?, Password = ?, Phone_No = ?"
				+ ",Is_Supervisor = ?,Must_Change_Pwd = ?, Login_Status = ?, User_Status = ?"
				+ ", Fleet_Admin = ?, Email = ?,branch_restriction =?, Expiry_Days =?,Approval_Limit = ? " + " WHERE User_Id = ?";
            con = getConnection();
			ps = con.prepareStatement(query);

			ps.setString(1, user.getUserName());
			ps.setString(2, user.getUserFullName());
			ps.setString(3, user.getLegacySystemId());
			ps.setString(4, user.getUserClass());
			ps.setString(5, user.getBranch());
			ps.setString(6, user.getPassword());
			ps.setString(7, user.getPhoneNo());
			ps.setString(8, user.getIsSupervisor());
			ps.setString(9, user.getMustChangePwd());
			ps.setString(10, user.getLoginStatus());

			ps.setString(11, user.getUserStatus());

			ps.setString(12, user.getFleetAdmin());
			ps.setString(13, user.getEmail());

            ps.setString(14, user.getBranchRestrict());
            ps.setInt(15, user.getExpiryDays());
            //ps.setDate(16,df.dateConvert(user.getExpiryDate()));

             ps.setString(16, limit);
             //-- ps.setString(17, user.getPostingRestricted());
            ps.setString(17, user.getUserId());

           

			done=( ps.executeUpdate()!=-1);


            }else{
                query = "UPDATE AM_GB_USER  SET  User_Name = ?, Full_Name = ?"
				+ " ,Legacy_Sys_Id = ?, Class = ?, Branch = ?, Password = ?, Phone_No = ?"
				+ ",Is_Supervisor = ?,Must_Change_Pwd = ?, Login_Status = ?, User_Status = ?"
				+ ", Fleet_Admin = ?, Email = ?,branch_restriction =?, Expiry_Days =?, Expiry_Date=?,Approval_Limit = ? " + " WHERE User_Id = ?";
			con = getConnection();
			ps = con.prepareStatement(query);

			ps.setString(1, user.getUserName());
			ps.setString(2, user.getUserFullName());
			ps.setString(3, user.getLegacySystemId());
			ps.setString(4, user.getUserClass());
			ps.setString(5, user.getBranch());
			ps.setString(6, user.getPassword());
			ps.setString(7, user.getPhoneNo());
			ps.setString(8, user.getIsSupervisor());
			ps.setString(9, user.getMustChangePwd());
			ps.setString(10, user.getLoginStatus());

			ps.setString(11, user.getUserStatus());

			ps.setString(12, user.getFleetAdmin());
			ps.setString(13, user.getEmail());

            ps.setString(14, user.getBranchRestrict());
            ps.setInt(15, user.getExpiryDays());
            ps.setDate(16,df.dateConvert(user.getExpiryDate()));

            ps.setString(17, limit);
            //--ps.setString(18, user.getPostingRestricted());
            ps.setString(18, user.getUserId());

            
            
			done=( ps.executeUpdate()!=-1);
            }//else

            //TO RESET EXPIRY DATE  TO NULL IF EXPIRY DAY IS 0
            if(user.getExpiryDays() == 0){
         updateExpiryDate(Integer.parseInt(user.getUserId()));
        }

        } catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;


	}


}
