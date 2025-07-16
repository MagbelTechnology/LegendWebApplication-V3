package com.magbel.ia.bus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import com.magbel.ia.dao.PersistenceServiceDAO;
import com.magbel.ia.util.ApplicationHelper;

/*
 * @Entities user,security privileges, userclass
 */

public class SecurityServiceBus extends PersistenceServiceDAO {

	SimpleDateFormat sdf;

	final String space = "  ";

	final String comma = ",";

	java.util.Date date;

	com.magbel.util.DatetimeFormat df;
	ApplicationHelper helper;
	/**
	 * constructor
	 *
	 */

	public SecurityServiceBus() {
		// TODO Auto-generated constructor stub
		sdf = new SimpleDateFormat("dd-MM-yyyy");
		df = new com.magbel.util.DatetimeFormat();
		//System.out.println("[INFO] USING_ " + this.getClass().getName());
		helper = new ApplicationHelper();
	}

	/**
	 * <p>
	 * Description:Returns all security privileges
	 * </p>
	 *
	 * @return java.util.List
	 */
	public java.util.List findAllPrivileges() {
		java.util.List _list = new java.util.ArrayList();
		com.magbel.ia.vao.Privilege privilege = null;
		String query = "SELECT role_uuid,role_name,role_wurl"
				+ " ,menu_type,priority" + "  FROM mg_ad_privileges order by role_name";

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
				privilege = new com.magbel.ia.vao.Privilege(role_uuid,
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

	/**
	Listing of GL Interface
	*/


		public java.util.List findAllGlInterface() {
			java.util.List _list = new java.util.ArrayList();
			com.magbel.ia.vao.GlInterface glInterface = null;
			String query = "SELECT MTID,INTER_CODE,INTER_NAME,LEDGER_NO"
					+ " ,USER_ID,CREATE_DT FROM IA_GL_INTERFACE";

			Connection con = null;
			PreparedStatement ps = null;
			ResultSet rs = null;

			try {
				con = getConnection();
				ps = con.prepareStatement(query);
				rs = ps.executeQuery();
				while (rs.next()) {
					String id = rs.getString("MTID");
					String interCode = rs.getString("INTER_CODE");
					String interName = rs.getString("INTER_NAME");
					String ledgeNo = rs.getString("LEDGER_NO");
					String userId = rs.getString("USER_ID");
					String createDt = rs.getString("CREATE_DT");
					glInterface = new com.magbel.ia.vao.GlInterface(id,
							interCode, interName, ledgeNo, userId, createDt);
					_list.add(glInterface);

				}
			} catch (Exception ex) {
				System.out.println("WARN: Error fetching all GL Interface ->" + ex);
			} finally {
				closeConnection(con, ps);
			}
			return _list;

	}

	/**
	 *
	 * <p>
	 * Description:Returns a security class with it privilege
	 * </p>
	 *
	 * @param classid
	 * @param roleuuid
	 * @return
	 */
	public com.magbel.ia.vao.ClassPrivilege findClassPrivilege(String classid,
			String roleuuid) {
		com.magbel.ia.vao.ClassPrivilege classprivilege = null;

		String query = "SELECT clss_uuid,role_uuid,role_view,role_addn,role_edit"
				+ " FROM mg_ad_class_privileges"
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
				classprivilege = new com.magbel.ia.vao.ClassPrivilege(
						clss_uuid, role_uuid, role_view, role_addn, role_edit);
			}
		} catch (Exception ex) {
			System.out.println("WARN: Error fetching Class Privileges ->" + ex);
		} finally {
			closeConnection(con, ps);
		}
		return classprivilege;

	}

	/**
	 * <p>
	 * Description:De-assigns privileges from a security class
	 * </p>
	 *
	 * @param list
	 * @return
	 */
	public boolean removeClassPrivilege(java.util.ArrayList list) {
		String query = "DELETE FROM mg_ad_class_privileges"
				+ " WHERE clss_uuid=? AND role_uuid=?";
		Connection con = null;
		PreparedStatement ps = null;
		// ResultSet rs = null;
		com.magbel.ia.vao.ClassPrivilege cp = null;
		int[] d = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			for (int i = 0; i < list.size(); i++) {
				cp = (com.magbel.ia.vao.ClassPrivilege) list.get(i);

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

	/**
	 * <p>
	 * Description:Assigns security privileges to Security Class
	 * </p>
	 *
	 * @param list
	 * @return
	 */
	public boolean insertClassPrivileges(java.util.ArrayList list) {
		String query = "INSERT INTO mg_ad_class_privileges(clss_uuid,role_uuid"
				+ " ,role_view,role_addn ,role_edit)" + " VALUES(?,?,?,?,?)";
		Connection con = null;
		PreparedStatement ps = null;
		com.magbel.ia.vao.ClassPrivilege cp = null;
		int[] d = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(query);

			for (int i = 0; i < list.size(); i++) {
				cp = (com.magbel.ia.vao.ClassPrivilege) list.get(i);
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

	/**
	 * <p>
	 * Description:Gets Security Class by classId
	 * </p>
	 *
	 * @param filter
	 * @return
	 */
	public com.magbel.ia.vao.SecurityClass findSecurityClassById(String filter) {

		com.magbel.ia.vao.SecurityClass sc = null;
		String query = "SELECT class_id ,class_desc,class_name ,is_supervisor"
				+ "  ,class_status,user_id,create_date"
				+ "  FROM MG_GB_CLASS WHERE  class_id='" + filter+"'";

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

				//String fleetAdmin = rs.getString("fleet_admin");
				sc = new com.magbel.ia.vao.SecurityClass(classId, description,
						className, isSupervisor, classStatus, userId,
						createDate);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return sc;

	}

	/**
	 * <p>
	 * Description:Gets Security Class by class_name
	 * </p>
	 *
	 * @param filter
	 * @return
	 */
	public com.magbel.ia.vao.SecurityClass findSecurityClassByName(String filter) {

		com.magbel.ia.vao.SecurityClass sc = null;
		String query = "SELECT class_id ,class_desc,class_name ,is_supervisor"
				+ "  ,class_status,user_id,create_date"
				+ "  FROM MG_GB_CLASS WHERE  class_name='" + filter + "'";

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

				//String fleetAdmin = rs.getString("fleet_admin");
				sc = new com.magbel.ia.vao.SecurityClass(classId, description,
						className, isSupervisor, classStatus, userId,
						createDate);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return sc;

	}

	/**
	 *
	 * <p>
	 * Description: Updates Security Class
	 * </p>
	 *
	 * @param sc
	 * @return
	 */
	public boolean updateSecurityClass(com.magbel.ia.vao.SecurityClass sc) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "UPDATE MG_GB_CLASS SET class_desc = ?,class_name = ?"
				+ "   ,is_supervisor = ?,class_status = ?"
				    + "   WHERE class_id = ?";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, sc.getDescription());
			ps.setString(2, sc.getClassName());
			ps.setString(3, sc.getIsSupervisor());
			ps.setString(4, sc.getClassStatus());
			//ps.setString(5, sc.getFleetAdmin());
			ps.setString(5, sc.getClassId());
			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " ERROR:Error Updating Security Class ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

	/**
	 *
	 * <p>
	 * Description: Updates Gl Interface
	 * </p>
	 *
	 * @param sc
	 * @return
	 *
	public boolean updateGlInterface(interCode,interName,ledgeNo,id) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "UPDATE IA_GL_INTERFACE SET INTER_CODE = ?,INTER_NAME = ?     "+ 
				"   ,LEDGER_NO = ?   WHERE MTID = ?";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, gl.getInterCode());
			ps.setString(2, gl.getInterName());
			ps.setString(3, gl.getLedgeNo());

			ps.setString(4, gl.getId());
			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " ERROR:Error Updating Gl Interface ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;
	}


	 *
	 * <p>
	 * Description: Inserts new Security Class record
	 * </p>
	 *
	 * @param sc
	 * @return
	 */


	 public boolean updateGlInterface(String interCode,String interName,String ledgeNo,String id){

	 		Connection con = null;
	 		PreparedStatement ps = null;
			boolean done = false;
	 		String UPDATE_QUERY =  "UPDATE IA_GL_INTERFACE SET INTER_CODE = ?,INTER_NAME = ?"
									+ "   ,LEDGER_NO = ?   WHERE MTID = ?";

	 		try{

	 			con = getConnection();
	 			ps = con.prepareStatement(UPDATE_QUERY);

	 			ps.setString(1, interCode);
	 			ps.setString(2, interName);
	 			ps.setString(3, ledgeNo);

	 			ps.setString(4, id);
			 	done = (ps.executeUpdate() != -1);

	 		}catch(Exception e){
	 			System.out.println("Error UPDATING Gl Interface... ->"+e.getMessage());
	 		}finally{
	 			closeConnection(con,ps);
	 		}
			return done;
	 	}



	public java.util.List findGlInterfaceByQuery(String filter) {
		java.util.List _list = new java.util.ArrayList();
		com.magbel.ia.vao.GlInterface gl = null;
		String query = "SELECT MTID,INTER_CODE,INTER_NAME,LEDGER_NO "
				+ "  ,USER_ID ,CREATE_DT FROM IA_GL_INTERFACE ";

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
				String interCode = rs.getString("INTER_CODE");
				String interName = rs.getString("INTER_NAME");
				String ledgeNo = rs.getString("LEDGER_NO");
				String userId = rs.getString("USER_ID");
				String createDt = rs.getString("CREATE_DT");

				gl = new com.magbel.ia.vao.GlInterface(id, interCode,
						interName, ledgeNo, userId, createDt);

				_list.add(gl);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}/**
		public com.magbel.ia.vao.GlInterface findGlInterfaceByGlInterfaceID(String id) {
			com.magbel.ia.vao.GlInterface gl = null;
			String query = "SELECT ACCOUNTNO FROM ia_GB_ACCOUNT WHERE MTID = '" + id + "'";

			Connection c = null;
			ResultSet rs = null;
			Statement s = null;

			try {
				c = getConnection();
				s = c.createStatement();
				rs = s.executeQuery(query);
				while (rs.next()) {
					String id = rs.getString("MTID");
					String no = rs.getString("ACCOUNTNO");

					gl = new com.magbel.ia.vao.GlInterface();
					gl.setId(id);
					user.setPwdChanged(pwdChanged);
	                                user.setApproveLevel(approveLevel);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			finally {
				closeConnection(c, s, rs);
			}
			return user;
		}

*/
	public boolean createSecurityClass(com.magbel.ia.vao.SecurityClass sc) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "INSERT INTO MG_GB_CLASS(class_desc,class_name,is_supervisor,class_status"
				+ "    ,user_id,create_date ,class_id)"
				+ "    VALUES (?,?,?,?,?,?,?)";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, sc.getDescription());
			ps.setString(2, sc.getClassName());
			ps.setString(3, sc.getIsSupervisor());
			ps.setString(4, sc.getClassStatus());
			ps.setString(5, sc.getUserId());
			ps.setDate(6, df.dateConvert(new java.util.Date()));
			//ps.setString(7, sc.getFleetAdmin());
			ps.setString(7, new ApplicationHelper()
					.getGeneratedId("MG_GB_CLASS"));
			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " ERROR:Error Updating Security Class ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

	/**
	 * <p>
	 * Description:Gets Security Class by Query condition
	 * </p>
	 *
	 * @param filter
	 * @return
	 */
	public java.util.List findSecurityClassByQuery(String filter) {
		java.util.List _list = new java.util.ArrayList();
		com.magbel.ia.vao.SecurityClass sc = null;
		String query = "SELECT class_id ,class_desc,class_name ,is_supervisor"
				+ "  ,class_status,user_id,create_date"
				+ "  FROM MG_GB_CLASS ";

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

				//String fleetAdmin = rs.getString("fleet_admin");
				sc = new com.magbel.ia.vao.SecurityClass(classId, description,
						className, isSupervisor, classStatus, userId,
						createDate);

				_list.add(sc);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}

	/**
	 * <p>
	 * Description:Update Privileges assigned to a class
	 * </p>
	 *
	 * @param list
	 * @return
	 */
	public boolean updateClassPrivilege(java.util.ArrayList list) {
		String query = "UPDATE mg_ad_class_privileges SET "
				+ "role_view = ?,role_addn = ?,role_edit = ?"
				+ " WHERE clss_uuid=? AND role_uuid=?";
		Connection con = null;
		PreparedStatement ps = null;
		// ResultSet rs = null;
		com.magbel.ia.vao.ClassPrivilege cp = null;
		int[] d = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(query);

			for (int i = 0; i < list.size(); i++) {
				cp = (com.magbel.ia.vao.ClassPrivilege) list.get(i);
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

	/**
	 *
	 * @return
	 */
	public java.util.List findAllUsers() {
		java.util.List _list = new java.util.ArrayList();
		com.magbel.ia.vao.User user = null;
		String query = "SELECT User_Id, User_Name, Full_Name"
				+ ", Legacy_Sys_Id, Class, Branch, Password"
				+ ", Phone_No, is_Supervisor, Must_Change_Pwd"
				+ ", Login_Status, User_Status, UserId, Create_Date"
				+ ", Password_Expiry, Login_Date, Login_System"
				+ ", email, Branch, password_changed, COMPANY_CODE,branch_restriction,User_Restrict  " 
				+ "  FROM MG_GB_USER";

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
				//String fleetAdmin = rs.getString("Fleet_Admin");
				String email = rs.getString("email");
				String branchCode = rs.getString("Branch");
				String pwdChanged = rs.getString("password_changed");
				String companyCode = rs.getString("COMPANY_CODE");
                String branchRestrict = rs.getString("branch_restriction");
				String userRestrict = rs.getString("User_Restrict");
				
				user = new com.magbel.ia.vao.User();
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
				//user.setFleetAdmin(fleetAdmin);
				user.setEmail(email);
				user.setBranch(branchCode);
				user.setUserStatus(userStatus);
				user.setPwdChanged(pwdChanged);
				user.setCompanyCode(companyCode);
				user.setBranchRestrict(branchRestrict);
				user.setUserRestrict(userRestrict);
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

	/**
	 * @param filter
	 * @return
	 */
	public java.util.ArrayList findUserByQuery(String filter) {
		java.util.ArrayList _list = new java.util.ArrayList();
		com.magbel.ia.vao.User user = null;
		String query = "SELECT User_Id, User_Name, Full_Name"
				+ ", Legacy_Sys_Id, Class, Branch, Password"
				+ ", Phone_No, is_Supervisor, Must_Change_Pwd"
				+ ", Login_Status, User_Status, UserId, Create_Date"
				+ ", Password_Expiry, Login_Date, Login_System"
				+ ", email, Branch, password_changed, COMPANY_CODE,branch_restriction,User_Restrict  "
				+ " FROM MG_GB_USER WHERE User_Id IS NOT NULL ";
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
				//String fleetAdmin = rs.getString("Fleet_Admin");
				String email = rs.getString("email");
				String branchCode = rs.getString("Branch");
				String pwdChanged = rs.getString("password_changed");
				String companyCode = rs.getString("COMPANY_CODE");
                String branchRestrict = rs.getString("branch_restriction");
				String userRestrict = rs.getString("User_Restrict");

				user = new com.magbel.ia.vao.User();
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
				//user.setFleetAdmin(fleetAdmin);
				user.setEmail(email);
				user.setBranch(branchCode);
				user.setPwdChanged(pwdChanged);
				user.setCompanyCode(companyCode);
				user.setBranchRestrict(branchRestrict);
				user.setUserRestrict(userRestrict);
				
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

	/**
	 *
	 * @param UserID
	 * @return
	 */
	public com.magbel.ia.vao.User findUserByUserID(String UserID) {
		com.magbel.ia.vao.User user = null;
		String query = "SELECT User_Id, User_Name, Full_Name"
				+ ", Legacy_Sys_Id, Class, Branch, Password"
				+ ", Phone_No, is_Supervisor, Must_Change_Pwd"
				+ ", Login_Status, User_Status, UserId, Create_Date"
				+ ", Password_Expiry, Login_Date, Login_System"
				+ ", email, Branch,password_changed,approve_level,COMPANY_CODE,branch_restriction,User_Restrict "
				+ " FROM MG_GB_USER WHERE User_Id = '" + UserID + "'";

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
				//String fleetAdmin = rs.getString("Fleet_Admin");
				String email = rs.getString("email");
				String branchCode = rs.getString("Branch");
				String pwdChanged = rs.getString("password_changed");
                                String approveLevel = rs.getString("approve_level");
				String companyCode = rs.getString("COMPANY_CODE");
                String branchRestrict = rs.getString("branch_restriction");
				String userRestrict = rs.getString("User_Restrict");

				user = new com.magbel.ia.vao.User();
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
				//user.setFleetAdmin(fleetAdmin);
				user.setEmail(email);
				user.setBranch(branchCode);
				user.setPwdChanged(pwdChanged);
                                user.setApproveLevel(approveLevel);
				
				user.setCompanyCode(companyCode);
				user.setBranchRestrict(branchRestrict);
				user.setUserRestrict(userRestrict);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return user;
	}

	/**
	 *
	 * @param UserName
	 * @return
	 */
	public com.magbel.ia.vao.User findUserByUserName(String UserName) {
		com.magbel.ia.vao.User user = null;
		String query = "SELECT User_Id, User_Name, Full_Name"
				+ ", Legacy_Sys_Id, Class, Branch, Password"
				+ ", Phone_No, is_Supervisor, Must_Change_Pwd"
				+ ", Login_Status, User_Status, UserId, Create_Date"
				+ ", Password_Expiry, Login_Date, Login_System"
				+ ", email, Branch,password_changed,approve_level,COMPANY_CODE,branch_restriction,User_Restrict "
				+ " FROM MG_GB_USER WHERE User_Name = '" + UserName + "'";

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
				//String fleetAdmin = rs.getString("Fleet_Admin");
				String email = rs.getString("email");
				String branchCode = rs.getString("Branch");
				String pwdChanged = rs.getString("password_changed");
                String approveLevel = rs.getString("approve_level");
				String companyCode = rs.getString("COMPANY_CODE");
                String branchRestrict = rs.getString("branch_restriction");
				String userRestrict = rs.getString("User_Restrict");

				user = new com.magbel.ia.vao.User();
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
				//user.setFleetAdmin(fleetAdmin);
				user.setEmail(email);
				user.setBranch(branchCode);
				user.setPwdChanged(pwdChanged);
                user.setApproveLevel(approveLevel);
				user.setCompanyCode(companyCode);
				user.setBranchRestrict(branchRestrict);
				user.setUserRestrict(userRestrict);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return user;
	}
	
	
	/**
	 *
	 * @param Company Code
	 * @return
	 */
	public com.magbel.ia.vao.User findUserByCompanyCode(String companyCode, String userId2) {
		com.magbel.ia.vao.User user = null;
		String query = "SELECT User_Id, User_Name, Full_Name"
				+ ", Legacy_Sys_Id, Class, Branch, Password"
				+ ", Phone_No, is_Supervisor, Must_Change_Pwd"
				+ ", Login_Status, User_Status, UserId, Create_Date"
				+ ", Password_Expiry, Login_Date, Login_System"
				+ ", email, Branch,password_changed,approve_level,COMPANY_CODE,branch_restriction,User_Restrict  "
				+ " FROM MG_GB_USER WHERE Company_Code ='" + companyCode + "' AND USER_ID='"+userId2+"' ";

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
				//String fleetAdmin = rs.getString("Fleet_Admin");
				String email = rs.getString("email");
				String branchCode = rs.getString("Branch");
				String pwdChanged = rs.getString("password_changed");
                                String approveLevel = rs.getString("approve_level");
				 companyCode = rs.getString("COMPANY_CODE");
                String branchRestrict = rs.getString("branch_restriction");
				String userRestrict = rs.getString("User_Restrict");
				user = new com.magbel.ia.vao.User();
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
				//user.setFleetAdmin(fleetAdmin);
				user.setEmail(email);
				user.setBranch(branchCode);
				user.setPwdChanged(pwdChanged);
                                user.setApproveLevel(approveLevel);
				user.setCompanyCode(companyCode);
				user.setBranchRestrict(branchRestrict);
				user.setUserRestrict(userRestrict);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return user;
	}

	
	
	/**
	 *
	 * @param user
	 * @return
	 */
	public boolean createUser(com.magbel.ia.vao.User user) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;

		String query = "INSERT INTO MG_GB_USER( User_Name, Full_Name, Legacy_Sys_Id"
				+ ", Class, Branch, Password, Phone_No, Is_Supervisor, Must_Change_Pwd"
				+ ", Login_Status, User_Status, UserId, Create_Date, Password_Expiry"
				+ ", Email,approve_level,COMPANY_CODE,user_id,branch_restriction,User_Restrict)"
				+ "  VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				

		try {
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
			ps.setDate(14, df.dateConvert(df.addDayToDate(new java.util.Date(),
					Integer.parseInt(user.getPwdExpiry()))));
			//ps.setString(15, user.getFleetAdmin());
			ps.setString(15, user.getEmail());
                        ps.setString(16,user.getApproveLevel());
			ps.setString(17, user.getCompanyCode());	
			ps.setString(18, new ApplicationHelper()
					.getGeneratedId("MG_GB_USER"));
			ps.setString(19, user.getBranchRestrict());
			ps.setString(20, user.getUserRestrict());
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
	 * @param user
	 * @return
	 */
	public boolean updateUser(com.magbel.ia.vao.User user) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "UPDATE MG_GB_USER  SET  User_Name = ?, Full_Name = ?"
				+ " ,Legacy_Sys_Id = ?, Class = ?, Branch = ?, Password = ?, Phone_No = ?"
				+ ",Is_Supervisor = ?,Must_Change_Pwd = ?, Login_Status = ?, User_Status = ?"
				+ ", Email = ?,approve_level=?, COMPANY_CODE=?,branch_restriction=?,User_Restrict=? "
				+ "  WHERE User_Id = ?";

		try {
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

		//	ps.setString(12, user.getFleetAdmin());
			ps.setString(12, user.getEmail());
                        ps.setString(13,user.getApproveLevel());
			
			ps.setString(14, user.getCompanyCode());
			ps.setString(15, user.getBranchRestrict());
			ps.setString(16, user.getUserRestrict());
			ps.setString(17, user.getUserId());
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
	 * @param iden  Company
	 * @param pass
	 * @return
	 */
	public boolean confirmCompany(String iden, String pass) {

		String query = "SELECT COMPANY_CODE FROM MG_GB_USER   "
				+ "WHERE USER_NAME = '" + iden+"'";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean done = false;
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				done = (pass.equals(rs.getString("COMPANY_CODE")));
			}

		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps, rs);
		}
		return done;
	}
	

	/**
	 *
	 * @param iden
	 * @param pass
	 * @return
	 */
	public boolean confirmPassword(String iden, String pass) {

		String query = "SELECT PASSWORD FROM MG_GB_USER   "
				+ "WHERE USER_ID = '" + iden+"'";
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

	/**
	 *
	 * @param userid
	 * @param ipaddress
	 * @return
	 */
	public boolean updateLogins(String userid, String ipaddress) {

		String query = "UPDATE MG_GB_USER  SET LOGIN_STATUS= 1 ,LOGIN_SYSTEM='"
				+ ipaddress + "'," + "login_date =?" + " WHERE   USER_ID = "
				+ userid;
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

	/**
	 *
	 * @param userid
	 * @param password
	 * @param expiry
	 * @return
	 */
	/** public boolean changePassword(String userid, String password, String expiry) {

		String query = "UPDATE MG_GB_USER"
				+ " SET Password =?,password_changed = ?,password_expiry =  ?"
				+ " WHERE   USER_ID = ?";
		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, password);
			ps.setString(2, "Y");
			ps.setDate(3, df.dateConvert(df.addDayToDate(new java.util.Date(),
					Integer.parseInt(expiry))));
			// ps.setInt(3, Integer.parseInt(expiry));
			ps.setString(4, userid);
			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;
	}           ------------------------------------------------------------------------***/
	
	public boolean changePassword(String userid, String password, String passwordMust, String expiry) {

		String query = "UPDATE MG_GB_USER"
				+ " SET Password =?,password_changed = ?,must_change_pwd=?,password_expiry =  ? "
				+ " WHERE   USER_ID = ?";
		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, password);
			ps.setString(2, "Y");
			ps.setString(3, "N");
			ps.setDate(4, df.dateConvert(df.addDayToDate(new java.util.Date(),
					Integer.parseInt(expiry))));
			// ps.setInt(3, Integer.parseInt(expiry));
			
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
	

	/**
	 *
	 * @param userid
	 * @param status
	 * @param ipaddress
	 * @return
	 */
	public boolean updateLogins(String userid, String status, String ipaddress) {

		String query = "UPDATE MG_GB_USER  SET LOGIN_STATUS= " + status
				+ ",LOGIN_SYSTEM='" + ipaddress + "',"
				+ "login_date = ?" + " WHERE   USER_ID = '" + userid+"'";
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

	/**
	 *
	 * @param userid
	 * @return
	 */
	public boolean queryPexpiry(String userid) {
		String query = "SELECT password_expiry FROM MG_GB_USER   "
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
				java.util.Date exDate = rs.getDate("password_expiry");
				done = (new java.util.Date().after(exDate));

			}

		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps, rs);
		}
		return done;
	}

	/**
	 *
	 * @return
	 */
	public java.util.ArrayList findLoggedUsers(String search) {
		String filter = " AND Login_Status = '1' " +search;
		java.util.ArrayList list = findUserByQuery(filter);
		return list;
	}

	/**
	 *
	 * @return
	 */
	public java.util.ArrayList<com.magbel.ia.vao.Function> findAllFunctions() {

		java.util.ArrayList<com.magbel.ia.vao.Function> finder = new java.util.ArrayList<com.magbel.ia.vao.Function>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(this.getFunctionQuery(""));
			rs = ps.executeQuery();

			while (rs.next()) {

				String id = rs.getString(1);
				String code = rs.getString(2);
				String url = rs.getString(3);
				String description = rs.getString(4);
				String type = rs.getString(5);
				com.magbel.ia.vao.Function function = new com.magbel.ia.vao.Function(
						id, code, url, description, type);
				finder.add(function);
			}

		} catch (Exception er) {
			System.out.println("WARN:Error connecting to DB \n" + er);
		} finally {
			closeConnection(con, ps, rs);
		}
		return finder;
	}

	/**
	 *
	 * @param classid
	 * @return
	 */
	public java.util.ArrayList<com.magbel.ia.vao.Function> findFunctionsBySecurityClass(
			String classid) {
		java.util.ArrayList<com.magbel.ia.vao.Function> finder = new java.util.ArrayList<com.magbel.ia.vao.Function>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(this.getClassFunctionQuery(classid));
			rs = ps.executeQuery();

			while (rs.next()) {

				String id = rs.getString(1);
				String code = rs.getString(2);
				String url = rs.getString(3);
				String description = rs.getString(4);
				String type = rs.getString(5);
				com.magbel.ia.vao.Function function = new com.magbel.ia.vao.Function(
						id, code, url, description, type);
				finder.add(function);
			}

		} catch (Exception er) {
			String message = "WARN: Can not select user's available\n"
					+ " functions due to the following:\n";
			System.out.println(message + er);
		} finally {
			closeConnection(con, ps, rs);
		}

		return finder;

	}

	/*
	 *
	 */
	private String getFunctionQuery(String filter) {
		String SELCT_QUERY = "SELECT ROLE_UUID,'000' as CODE, ROLE_WURL,"
				+ "ROLE_NAME,MENU_TYPE  "
				+ "FROM mg_ad_privileges ORDER BY PRIORITY";
		return SELCT_QUERY;
	}

	/**
	 *
	 * @param classid
	 * @return
	 */
	public java.util.ArrayList findClassFunctionsById(String classid) {
		java.util.ArrayList finder = new java.util.ArrayList();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String SELECT_QUERY = "SELECT CLSS_UUID,ROLE_UUID,ROLE_ADDN,"
				+ "ROLE_EDIT FROM mg_ad_class_privileges  "
				+ "WHERE CLSS_UUID = ?  ";
		try {
			con = getConnection();
			ps = con.prepareStatement(SELECT_QUERY);
			ps.setString(1, classid);
			rs = ps.executeQuery();

			while (rs.next()) {

				String classCode = rs.getString(1);
				String functionCode = rs.getString(2);
				boolean addable = ((rs.getString(3).equalsIgnoreCase("Y")) ? true
						: false);
				boolean editable = ((rs.getString(4).equalsIgnoreCase("Y")) ? true
						: false);
				com.magbel.ia.vao.ClassFunction cFunction = new com.magbel.ia.vao.ClassFunction(
						classCode, functionCode, editable, addable);

				finder.add(cFunction);
			}

		} catch (Exception er) {
			String message = "WARN: Can not select user's priviledges\n"
					+ " functions due to the following:\n";
			System.out.println(message + er);
		} finally {
			closeConnection(con, ps, rs);
		}
		return finder;

	}

	/**
	 *
	 * @param objectid
	 * @param classFunctions
	 * @return
	 */
	public boolean isEditable(String objectid,
			java.util.ArrayList classFunctions) {
		boolean editable = false;
		for (int x = 0; x < classFunctions.size(); x++) {
			com.magbel.ia.vao.ClassFunction cf = (com.magbel.ia.vao.ClassFunction) classFunctions
					.get(x);
			if (cf.getFunctionCode().equals(objectid)) {
				editable = cf.isEditable();
				break;
			}
		}

		return editable;

	}

	/**
	 *
	 * @param objectid
	 * @param classFunctions
	 * @return
	 */
	public boolean isAddable(String objectid, java.util.ArrayList classFunctions) {
		boolean addable = false;
		for (int x = 0; x < classFunctions.size(); x++) {
			com.magbel.ia.vao.ClassFunction cf = (com.magbel.ia.vao.ClassFunction) classFunctions
					.get(x);
			if (cf.getFunctionCode().equals(objectid)) {
				addable = cf.isAddable();
				break;
			}
		}

		return addable;
	}

	/**
	 *
	 * @param classCode
	 * @return
	 */
	private String getClassFunctionQuery(String classCode) {
		String CLASS_FUNCTION_QUERY = "SELECT A.ROLE_UUID,'000' AS CODE ,A.ROLE_WURL,"
				+ "A.ROLE_NAME,A.MENU_TYPE "
				+ "FROM mg_ad_class_privileges B,"
				+ "mg_ad_privileges A   "
				+ "WHERE B.ROLE_UUID = A.ROLE_UUID   "
				+ "AND B.ROLE_VIEW = 'Y' "
				+ "AND B.CLSS_UUID = '"
				+ classCode
				+ "' ORDER BY A.PRIORITY";

		return CLASS_FUNCTION_QUERY;
	}

	public boolean isValidUser(String iden, String pass) {

		String query = "SELECT PASSWORD FROM MG_GB_USER   "
				+ "WHERE USER_NAME = '" + iden+"'";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isValid = false;
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				isValid = (pass.equals(rs.getString("PASSWORD")));
			}

		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps, rs);
		}
		return isValid;
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


	
	public String notifyLoginStatus(String userid,String actionType,
									String loginid,String branch,String userStatus,String userFullName){

		final String COMPLETE_LOGOUT = "logout";
		final String SUCCESS_LOGIN = "login";
		String loginAudit = "UPDATE MG_GB_USER SET LOGIN_DATE = ? WHERE USER_ID = ?";
		String logoutAuditTrail = "UPDATE MG_USER_LOGIN SET LOGOUT_TM = ? "+
								  "WHERE MTID = ?";
		String loginAuditTrail = "INSERT INTO MG_USER_LOGIN("+
				 "MTID,USER_ID,LOG_DT,LOGIN_TM,LOGOUT_TM,STATUS,NAME,BRANCH_CODE) VALUES(?,?,?,?,?,?,?,?)";

		Connection con = null;
		PreparedStatement ps = null;
		String id = null;
		SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");

		try {

			con = getConnection();

			if(actionType.equalsIgnoreCase(SUCCESS_LOGIN)){

				id = helper.getGeneratedId2("MG_USER_LOGIN");
				ps = con.prepareStatement(loginAuditTrail);

				ps.setString(1, id);
				ps.setString(2, userid);
				ps.setDate(3,dateConvert(new java.util.Date()));
				ps.setString(4,timer.format(new java.util.Date()));
				ps.setString(5,timer.format(new java.util.Date()));
				ps.setString(6, userStatus);
				ps.setString(7, userFullName);
				ps.setString(8, branch);

			    ps.execute();

			    ps = con.prepareStatement(loginAudit);
			    ps.setString(1,timer.format(new java.util.Date()));
				ps.setString(2,userid);

				ps.execute();

			}else{

				id = loginid;
				ps = con.prepareStatement(logoutAuditTrail);
				ps.setString(1,timer.format(new java.util.Date()));
				ps.setString(2,id);
				ps.execute();
			}


		} catch (Exception er) {
			System.out.println("WARN:Error notifying LoginStatus ->"+ er.getMessage());
		} finally {
			closeConnection(con, ps);
		}

		return id;

	}
	
	
	public void getUserDetails(String userId) {
	
	//System.out.println("Inside getUserDetails(String userId)" );
	
	String query = "SELECT User_ID,User_Name,Full_Name,User_Status,BRANCH,Login_Date"
				+ " FROM MG_GB_USER WHERE User_Name = '" + userId + "'";

		Connection c = null;
		ResultSet rs = null;
		Statement s = null;
		SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			
			
			while (rs.next()) {
				
				String userid = rs.getString("User_Id");
				String username = rs.getString("User_Name");
				String userFullName = rs.getString("Full_Name");
				String userStatus = rs.getString("User_Status");
				String branch = rs.getString("Branch");
				String lastLogindate = rs.getString("Login_Date");
				
				//System.out.println(userid);
				//System.out.println(username);
				//System.out.println(userFullName);
				//System.out.println(userStatus);
				//System.out.println( branch);
				//System.out.println(lastLogindate);
				
				notifyLogin(userid,username, branch,userStatus, userFullName, lastLogindate);
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		
	}
	
	
	public void notifyLogin(String userid,String username,String branch,
								String userStatus,String userFullName,String lastLogindate){

		
		String loginAuditTrail = "INSERT INTO MG_USER_LOGIN(MTID, User_ID,STATUS,NAME "
				+", BRANCH_CODE,LOG_DT,LOGIN_TM,LOGOUT_TM) VALUES(?,?,?,?,?,?,?,?)";

		Connection con = null;
		PreparedStatement ps = null;
		
		SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");

		try {

			con = getConnection();

			
				ps = con.prepareStatement(loginAuditTrail);

				ps.setString(1, userid);
				ps.setString(2, username);
				ps.setString(3,userStatus);
				ps.setString(4,userFullName);
				ps.setString(5, branch);
				ps.setDate(6,dateConvert(new java.util.Date()));
				ps.setString(7,timer.format(new java.util.Date()));
				ps.setString(8,timer.format(new java.util.Date()));

			    ps.execute(); 
				

		} catch (Exception er) {
			System.out.println("WARN:Error notifying LoginStatus ->"+ er.getMessage());
		} finally {
			closeConnection(con, ps);
		}

		

	}

	public String getCodeName(String query)
    {
     String result = "";
     Connection con = null;
     ResultSet rs = null; 
     PreparedStatement ps = null;
     
     try
     {
      con = getConnection();
      ps = con.prepareStatement(query);
      rs = ps.executeQuery();
      while(rs.next())
      {
       result = rs.getString(1) == null ? "" : rs.getString(1);
      }
     }
     catch(Exception er)
     {
        System.out.println("Error in getCodeName()... ->"+er);
        er.printStackTrace();
     }finally{
        closeConnection(con,ps);
      }   
      return result;
     }

	
	
	
}
