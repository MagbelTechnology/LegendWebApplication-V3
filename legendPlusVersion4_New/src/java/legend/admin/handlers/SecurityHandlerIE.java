package legend.admin.handlers;

import audit.AuditTrailGen;

import com.magbel.legend.vao.PasswordHistory;
import com.magbel.util.ApplicationHelper;
import com.magbel.util.Cryptomanager;
import com.magbel.util.DataConnect;
import com.magbel.util.DatetimeFormat;
import com.mindcom.security.Licencer;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

import legend.admin.objects.ClassPrivilege;
import legend.admin.objects.Privilege;
import legend.admin.objects.SecurityClass;
import legend.admin.objects.User;
import ng.com.magbel.token.TokenAuthentication;
import ng.com.magbel.token.TokenAuthenticationSoap;

public class SecurityHandlerIE
{

    Connection con;
    Statement stmt;
    PreparedStatement ps;
    ResultSet rs;
    DataConnect dc;
    SimpleDateFormat sdf;
    final String space = "  ";
    final String comma = ",";
    Date date;
    DatetimeFormat df;
    Cryptomanager cm;

    public SecurityHandlerIE()
    {
        con = null;
        stmt = null;
        ps = null;
        rs = null;
        cm = null;
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        df = new DatetimeFormat();
  //      System.out.println((new StringBuilder()).append("USING_ ").append(getClass().getName()).toString());
    }

    public java.util.List getAllPrivileges() {
        java.util.List _list = new java.util.ArrayList();
        legend.admin.objects.Privilege privilege = null;
        String query = "SELECT role_uuid,role_name,role_wurl"
                + " ,menu_type,priority" + "  FROM am_ad_privileges order by role_name  ";
        System.out.println("query in getAllPrivileges:"+query);
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
    
    public int tokenLogin(String userName, String password)
    {
    	// Kazeem
        TokenAuthentication token = new TokenAuthentication();
        TokenAuthenticationSoap soap = token.getTokenAuthenticationSoap();
        if(password != null)
        {
            password = password.trim();
        }
        String sub = "";
        if(userName != null)
        {
            sub = userName.substring(3);
        }
        System.out.println((new StringBuilder()).append("Password Supplied ==").append(password).append("   username is now==").append(userName).append(" after subsctring=").append(sub).toString());
        boolean response = soap.authenticateToken(sub, password,"TestID","087f7101089b76ce079e3725276d91d1a30872ba");
        System.out.println((new StringBuilder()).append("The response after the call to web service=====").append(response).toString());
        return !response ? 0 : 1;
    }

    public legend.admin.objects.ClassPrivilege getClassPrivilege(
            String classid, String roleuuid) {
        legend.admin.objects.ClassPrivilege classprivilege = null;

        String query = "SELECT clss_uuid,role_uuid,role_view,role_addn,role_edit"
                + " FROM am_ad_class_privileges"
                + " WHERE clss_uuid=? AND role_uuid=?";
System.out.println("query in ClassPrivilege:"+query);
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
//System.out.println("query in getSecurityClassById: "+query);
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
        } finally {
            closeConnection(c, s, rs);
        }
        return sc;

    }

    public legend.admin.objects.SecurityClass getSecurityClassByName(String filter) {

        legend.admin.objects.SecurityClass sc = null;
        String query = "SELECT class_id ,class_desc,class_name ,is_supervisor"
                + "  ,class_status,user_id,create_date,fleet_admin"
                + "  FROM am_gb_class WHERE  class_name='" + filter + "'";
        System.out.println("query in getSecurityClassByName:"+query);
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
        } finally {
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
            ps.setString(8, new ApplicationHelper().getGeneratedId("am_gb_class"));

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

    public java.util.List getSecurityClassByQuery(String filter) {
        java.util.List _list = new java.util.ArrayList();
        legend.admin.objects.SecurityClass sc = null;
        String query = "SELECT class_id ,class_desc,class_name ,is_supervisor"
                + "  ,class_status,user_id,create_date,fleet_admin"
                + "  FROM am_gb_class ";
        System.out.println("query in getSecurityClassByQuery:"+query);
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

        } finally {
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
        } finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }
    public java.util.ArrayList getUserByQuery(String filter)
    {
        ArrayList _list;
        String query;
        Connection c;
        ResultSet rs;
        Statement s;
        _list = new ArrayList();
        User user = null;
        query = "SELECT User_Id, User_Name, Full_Name, Legacy_Sys_Id, Class, Branch, Password, Ph" +
"one_No, is_Supervisor, Must_Change_Pwd, Login_Status, User_Status, UserId, Creat" +
"e_Date, Password_Expiry, Login_Date, Login_System, Fleet_Admin, email, Branch, p" +
"assword_changed,Expiry_Date,branch_restriction, approval_limit,approval_level,to" +
"ken_required,dept_restriction,UnderTaker FROM AM_GB_USER WHERE User_Id IS NOT NULL"
;
        query += filter;
        c = null;
        rs = null;
        s = null;
//        System.out.println("getUserByQuery query for Active Users: "+query);
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
            String tokenRequired = rs.getString("token_required");
//            System.out.println((new StringBuilder()).append("####################################### Token Read from the DB here ====").append(tokenRequired).toString());
//            System.out.println((new StringBuilder()).append(" The sent tokenRequired ==== ").append(tokenRequired).toString());
            Date expiry_date = rs.getDate("Expiry_Date");
            String branch_restriction = rs.getString("branch_restriction");
            String apprvLimit = rs.getString("approval_limit");
            String apprvLevel = rs.getString("approval_level");
            String buRestrict = rs.getString("dept_restriction");
            String underTaker = rs.getString("UnderTaker");
            user = new User();
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
            user.setUnderTaker(underTaker);
            if(tokenRequired != null && tokenRequired.trim().equalsIgnoreCase("Y"))
            {
                user.setTokenRequired(true);
            } else
            {
                user.setTokenRequired(false);
            }
//            System.out.println((new StringBuilder()).append("##################################### After setting the user the token of the us" +
//"er =="
//).append(user.isTokenRequired()).toString());
            user.setApprvLimit(apprvLimit);
            user.setBranchRestrict(branch_restriction);
            user.setApprvLevel(apprvLevel);
            user.setExpDate(expiry_date);
            user.setDeptRestrict(buRestrict);
            _list.add(user);
        }
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }

    public legend.admin.objects.User getUserByUserID(String UserID)
    {
        User user;
        String expiryDate;
        boolean tokenRequired;
        String query;
        Connection c;
        ResultSet rs;
        Statement s;
        user = null;
        expiryDate = "";
        tokenRequired = false;
        query = "SELECT User_Id, User_Name, Full_Name, Legacy_Sys_Id, Class, Branch, Password, Ph" +
"one_No, is_Supervisor, Must_Change_Pwd, Login_Status, User_Status, UserId, Creat" +
"e_Date, Password_Expiry, Login_Date, Login_System, Fleet_Admin, email, Branch,pa" +
"ssword_changed,branch_restriction, Expiry_Days,Expiry_Date,dept_code,token_requi" +
"red,approve_level,is_StockAdministrator,is_Storekeeper,dept_restriction,UnderTaker FROM AM_GB_USER WHERE User_Id = '"
+ UserID + "'";

        c = null;
        rs = null;
        s = null;
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
            String deptCode = rs.getString("dept_code");
            String tokenRequire = rs.getString("token_required");
            String approveLevel = rs.getString("approve_level");
            String isStockAdministrator = rs.getString("is_StockAdministrator");
            String isStorekeeper = rs.getString("is_Storekeeper");
            String buRestrict = rs.getString("dept_restriction");
            String underTaker = rs.getString("UnderTaker");
            if(tokenRequire == "Y")
            {
                tokenRequired = true; 
            }
            if(tokenRequire == "N")
            {
                tokenRequired = false;
            }
            if(rs.getDate("Expiry_Date") != null)
            {
                expiryDate = rs.getDate("Expiry_Date").toString();
            }
            user = new User();
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
            user.setDeptCode(deptCode);
            user.setTokenRequire(tokenRequire);
            user.setApprvLevel(approveLevel);
            user.setIsStorekeeper(isStorekeeper);
            user.setIsStockAdministrator(isStockAdministrator); 
            user.setDeptRestrict(buRestrict);
            user.setUnderTaker(underTaker);
        }
    }  
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(c, s, rs);
        }
        return user;
    }

    public legend.admin.objects.User getUserByUserName(String UserName)
    {
        User user;
        String query;
        Connection c;
        ResultSet rs;
        Statement s;
        user = null;
        query = "SELECT User_Id, User_Name, Full_Name, Legacy_Sys_Id, Class, Branch, Password, Ph" +
"one_No, is_Supervisor, Must_Change_Pwd, Login_Status, User_Status, UserId, Creat" +
"e_Date, Password_Expiry, Login_Date, Login_System, Fleet_Admin, email, Branch,pa" +
"ssword_changed,approve_level,dept_code FROM AM_GB_USER WHERE User_Name = '"
+ UserName + "'";
        c = null;
        rs = null;
        s = null;
        try {
        c = getConnection();
        s = c.createStatement();
        String pwdChanged;
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
            pwdChanged = rs.getString("password_changed");
            String approveLevel = rs.getString("approve_level");
            String deptCode = rs.getString("dept_code");
            user = new User();
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
            user.setApprvLevel(approveLevel);
            user.setDeptCode(deptCode);
        }
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(c, s, rs);
        }
        return user;
    }

    public boolean createUser(legend.admin.objects.User user)
    {
        Connection con;
        PreparedStatement ps;
        boolean done;
        con = null;
        ps = null;
        done = false;
        String query = "";
        try {
        if(user.getExpiryDate() == "" || user.getExpiryDate().equalsIgnoreCase("null") || user.getExpiryDate() == null || user.getExpiryDate().equalsIgnoreCase(""))
        {
             query = "INSERT INTO AM_GB_USER( User_Name, Full_Name, Legacy_Sys_Id, Class, Branch, Pass" +
"word, Phone_No, Is_Supervisor, Must_Change_Pwd, Login_Status, User_Status, UserI" +
"d, Create_Date, Password_Expiry,Fleet_Admin, Email,User_id,branch_restriction, E" +
"xpiry_Days,approveLevel,dept_restriction,dept_code,UnderTaker)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
;
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
            ps.setDate(13, df.dateConvert(new Date()));
            ps.setDate(14, df.dateConvert(df.addDayToDate(new Date(), Integer.parseInt(user.getPwdExpiry()))));
            ps.setString(15, user.getFleetAdmin());
            ps.setString(16, user.getEmail());
            ps.setString(17, (new ApplicationHelper()).getGeneratedId("AM_GB_USER"));
            ps.setString(18, user.getBranchRestrict());
            ps.setInt(19, user.getExpiryDays());
            ps.setString(20, user.getApprvLevel());
            ps.setString(21, user.getDeptRestrict());
            ps.setString(22, user.getDeptCode());
            ps.setString(23, user.getUnderTaker());
            
            done = ps.executeUpdate() != -1;
        } else
        {
             query = "INSERT INTO AM_GB_USER( User_Name, Full_Name, Legacy_Sys_Id, Class, Branch, Pass" +
"word, Phone_No, Is_Supervisor, Must_Change_Pwd, Login_Status, User_Status, UserI" +
"d, Create_Date, Password_Expiry,Fleet_Admin, Email,User_id,branch_restriction, E" +
"xpiry_Days,Expiry_Date,approveLevel,dept_restriction,dept_code,UnderTaker)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
;
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
            ps.setDate(13, df.dateConvert(new Date()));
            ps.setDate(14, df.dateConvert(df.addDayToDate(new Date(), Integer.parseInt(user.getPwdExpiry()))));
            ps.setString(15, user.getFleetAdmin());
            ps.setString(16, user.getEmail());
            ps.setString(17, (new ApplicationHelper()).getGeneratedId("AM_GB_USER"));
            ps.setString(18, user.getBranchRestrict());
            ps.setInt(19, user.getExpiryDays());
            String temDate = user.getExpiryDate();
            ps.setDate(20, df.dateConvert(temDate));
            ps.setString(21, user.getApprvLevel());
            ps.setString(22, user.getDeptRestrict());
            ps.setString(23, user.getDeptCode());
            ps.setString(24, user.getUnderTaker());
            
            done = ps.executeUpdate() != -1;
        }
        }catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->"
                    + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }

    public boolean updateUser(legend.admin.objects.User user)
    {
        Connection con;
        PreparedStatement ps;
        boolean done;
        con = null;
        ps = null;
        done = false;
        String query = "";
        try {
        if(user.getExpiryDate() == "" || user.getExpiryDate().equalsIgnoreCase("null") || user.getExpiryDate().equalsIgnoreCase(""))
        {
             query = "UPDATE AM_GB_USER  SET  User_Name = ?, Full_Name = ? ,Legacy_Sys_Id = ?, Class =" +
" ?, Branch = ?, Password = ?, Phone_No = ?,Is_Supervisor = ?,Must_Change_Pwd = ?" +
", Login_Status = ?, User_Status = ?, Fleet_Admin = ?, Email = ?,branch_restricti" +
"on =?, Expiry_Days =?,approveLevel = ?,dept_restriction=?,dept_code=?,UnderTaker=?  WHERE User_Id = ?"
;
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
            ps.setString(16, user.getApprvLevel());
            ps.setString(17, user.getDeptRestrict());
            ps.setString(18, user.getDeptCode());
            ps.setString(19, user.getUnderTaker());
            ps.setString(20, user.getUserId());
          
            done = ps.executeUpdate() != -1;
        } else
        {
             query = "UPDATE AM_GB_USER  SET  User_Name = ?, Full_Name = ? ,Legacy_Sys_Id = ?, Class =" +
" ?, Branch = ?, Password = ?, Phone_No = ?,Is_Supervisor = ?,Must_Change_Pwd = ?" +
", Login_Status = ?, User_Status = ?, Fleet_Admin = ?, Email = ?,branch_restricti" +
"on =?, Expiry_Days =?, Expiry_Date=?,approveLevel = ?,dept_restriction=?,dept_code=?,UnderTaker=?  WHERE User_Id = ?"
;
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
            ps.setDate(16, df.dateConvert(user.getExpiryDate()));
            ps.setString(17, user.getApprvLevel());
            ps.setString(18, user.getDeptRestrict());
            ps.setString(19, user.getDeptCode());
            ps.setString(20, user.getUnderTaker());
            ps.setString(21, user.getUserId());
            done = ps.executeUpdate() != -1;
        }
        if(user.getExpiryDays() == 0)
        {
            updateExpiryDate(Integer.parseInt(user.getUserId()));
        }
        }catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->"
                    + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }


    public boolean confirmPassword(String iden, String pass)
    {
        String query;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        boolean done;
        query = (new StringBuilder()).append("SELECT PASSWORD FROM AM_GB_USER   WHERE USER_ID = ").append(iden).toString();
        con = null;
        ps = null;
        rs = null;
        done = false;
        try {
        con = getConnection();
        ps = con.prepareStatement(query);
        for(rs = ps.executeQuery(); rs.next();)
        {
            done = pass.equals(rs.getString("PASSWORD"));
        }
        } catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->"
                    + e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return done;
    }

    public boolean updateLogins(String userid, String ipaddress)
    {
        String query;
        Connection con;
        PreparedStatement ps;
        boolean done;
        query = (new StringBuilder()).append("UPDATE AM_GB_USER  SET LOGIN_STATUS= 1 ,LOGIN_SYSTEM='").append(ipaddress).append("',").append("login_date =?").append(" WHERE   USER_ID = ").append(userid).toString();
        con = null;
        ps = null;
        done = false;
        try {
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setDate(1, df.dateConvert(new Date()));
        done = ps.executeUpdate() != -1;
        }
        catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->"
                    + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;
    }

    public boolean changePassword(String userid, String password, String expiry)
    {
        String query;
        Connection con;
        PreparedStatement ps;
        boolean done;
        query = "UPDATE am_gb_User SET Password =?,password_changed = ?,password_expiry =  ?,must" +
"_change_pwd=? WHERE   USER_ID = ?"
;
        con = null;
        ps = null;
        done = false;
        try {
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, password);
        ps.setString(2, "Y");
        ps.setDate(3, df.dateConvert(df.addDayToDate(new Date(), Integer.parseInt(expiry))));
        ps.setString(4, "N");
        ps.setString(5, userid);
        done = ps.executeUpdate() != -1;
    } catch (Exception e) {
        System.out.println("WARNING:Error executing Query ->"
                + e.getMessage());
    } finally {
        closeConnection(con, ps);
    }
    return done;
}

    public boolean updateLogins(String userid, String status, String ipaddress)
    {
        String query;
        Connection con;
        PreparedStatement ps;
        boolean done;
         query = "UPDATE AM_GB_USER  SET LOGIN_STATUS= " + status
                + ",LOGIN_SYSTEM='" + ipaddress + "',"
                + "login_date = GetDate()" + " WHERE   USER_ID = " + userid;
     //   query = (new StringBuilder()).append("UPDATE AM_GB_USER  SET LOGIN_STATUS= ").append(status).append(",LOGIN_SYSTEM='").append(ipaddress).append("',").append("login_date = GetDate()").append(" WHERE   USER_ID = ").append(userid).toString();
        con = null;
        ps = null;
        done = false;
        try {
        con = getConnection();
        ps = con.prepareStatement(query);
        done = ps.executeUpdate() != -1;
    }
        catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->"
                    + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;
    }

    public boolean queryPexpiry(String userid)
    {
        String query;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        boolean done;
        query = "SELECT password_expiry FROM AM_GB_USER   "
                + "WHERE USER_ID = " + userid;
 //       query = (new StringBuilder()).append("SELECT password_expiry FROM AM_GB_USER   WHERE USER_ID = ").append(userid).toString();
        con = null;
        ps = null;
        rs = null;  
        done = false;
        try {
        con = getConnection();
        ps = con.prepareStatement(query);
        rs = ps.executeQuery();
        while (rs.next()) {
            String exDate = rs.getString("password_expiry");
            int diff = df.getDayDifferenceNoABS(exDate, sdf.format(new Date()));
            if(diff <= 0)
            {
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

    private Connection getConnection()
    {
        Connection con = null;
        dc = new DataConnect("FixedAssetPlus");
        try
        {
            con = dc.getConnection();
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARNING: Error getting connection ->").append(e.getMessage()).toString());
        }
        return con;
    }

    private void closeConnection(Connection con, Statement s)
    {
        try
        {
            if(s != null)
            {
                s.close();
            }
            if(con != null)
            {
                con.close();
            }
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARNING: Error getting connection ->").append(e.getMessage()).toString());
        }
    }

    private void closeConnection(Connection con, PreparedStatement ps)
    {
        try
        {
            if(ps != null)
            {
                ps.close();
            }
            if(con != null)
            {
                con.close();
            }
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARNING: Error closing connection ->").append(e.getMessage()).toString());
        }
    }

    private void closeConnection(Connection con, Statement s, ResultSet rs)
    {
        try
        {
            if(rs != null)
            {
                rs.close();
            }
            if(s != null)
            {
                s.close();
            }
            if(con != null)
            {
                con.close();
            }
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARNING: Error closing connection ->").append(e.getMessage()).toString());
        }
    }

    private void closeConnection(Connection con, PreparedStatement ps, ResultSet rs)
    {
        try
        {
            if(rs != null)
            {
                rs.close();
            }
            if(ps != null)
            {
                ps.close();
            }
            if(con != null)
            {
                con.close();
            }
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARNING: Error closing connection ->").append(e.getMessage()).toString());
        }
    }

    private boolean executeQuery(String query)
    {
        Connection con;
        PreparedStatement ps;
        boolean done;
        con = null;
        ps = null;
        done = false;
        try {
        con = getConnection();
        ps = con.prepareStatement(query);
        done = ps.execute();
        }
        catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->"
                    + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;
    }

    public ArrayList getAUsers()
    {
        String filter = " AND Login_Status = '1'";
        ArrayList list = getUserByQuery(filter);
        return list;
    }

    public String getBranchRestrictedUser(String userId)
    {
        String branchRestrict;
        String query;
        Connection c;
        ResultSet rs;
        Statement s;
        branchRestrict = "";
        query = "SELECT branch_restriction "
                + " FROM AM_GB_USER WHERE User_Id = '"
                + userId + "'";
 //       query = (new StringBuilder()).append("SELECT branch_restriction  FROM AM_GB_USER WHERE User_Id = '").append(userId).append("'").toString();
        c = null;
        rs = null;
        s = null;
        try {
        c = getConnection();
        s = c.createStatement();
        rs = s.executeQuery(query);
        while (rs.next()) {
            branchRestrict = rs.getString("branch_restriction");
        }
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(c, s, rs);
        }

        return branchRestrict;
    }

    public boolean updateExpiryDate(int user_id)
    {
        Connection con;
        PreparedStatement ps;
        boolean done;
        con = null;
        ps = null;
        done = false;
        try {
        String query = (new StringBuilder()).append("update am_gb_user set expiry_date = null where user_id = ").append(user_id).toString();
        con = getConnection();
        ps = con.prepareStatement(query);
        done = ps.executeUpdate() != -1;
        }
        catch (Exception e) {
            System.out.println("WARNING:Error updateExpiryDate() Query ->"
                    + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;
    }

    public boolean createManageUser(legend.admin.objects.User user, String limit)
    {
        Connection con;
        PreparedStatement ps;
        boolean done;
        con = null;
        ps = null;
        done = false;
        String query = "";
        try {
        String userId = (new ApplicationHelper()).getGeneratedId("AM_GB_USER");
        if(user.getExpiryDate() == "" || user.getExpiryDate().equalsIgnoreCase("null") || user.getExpiryDate() == null || user.getExpiryDate().equalsIgnoreCase(""))
        {
             query = "INSERT INTO AM_GB_USER( User_Name, Full_Name, Legacy_Sys_Id, Class, Branch, Pass" +
"word, Phone_No, Is_Supervisor, Must_Change_Pwd, Login_Status, User_Status, UserI" +
"d, Create_Date, Password_Expiry,Fleet_Admin, Email,User_id,branch_restriction, E" +
"xpiry_Days,Approval_Limit,dept_restriction,dept_code,UnderTaker)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
;
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
            ps.setDate(13, df.dateConvert(new Date()));
            ps.setDate(14, df.dateConvert(df.addDayToDate(new Date(), Integer.parseInt(user.getPwdExpiry()))));
            ps.setString(15, user.getFleetAdmin());
            ps.setString(16, user.getEmail());
            ps.setString(17, userId);
            ps.setString(18, user.getBranchRestrict());
            ps.setInt(19, user.getExpiryDays());
            ps.setString(20, limit);
            ps.setString(21, user.getDeptRestrict());
            ps.setString(22, user.getDeptCode());
            ps.setString(23, user.getUnderTaker());
            done = ps.executeUpdate() != -1;
        } else
        {
             query = "INSERT INTO AM_GB_USER( User_Name, Full_Name, Legacy_Sys_Id, Class, Branch, Pass" +
"word, Phone_No, Is_Supervisor, Must_Change_Pwd, Login_Status, User_Status, UserI" +
"d, Create_Date, Password_Expiry,Fleet_Admin, Email,User_id,branch_restriction, E" +
"xpiry_Days,Expiry_Date,Approval_Limit,dept_code,dept_restriction,UnderTaker)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?," +
"?,?,?,?,?,?,?,?,?,?,?)"
;
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
            ps.setDate(13, df.dateConvert(new Date()));
            ps.setDate(14, df.dateConvert(df.addDayToDate(new Date(), Integer.parseInt(user.getPwdExpiry()))));
            ps.setString(15, user.getFleetAdmin());
            ps.setString(16, user.getEmail());
            ps.setString(17, userId);
            ps.setString(18, user.getBranchRestrict());
            ps.setInt(19, user.getExpiryDays());
            String temDate = user.getExpiryDate();
            ps.setDate(20, df.dateConvert(temDate));
            ps.setString(21, limit);
            ps.setString(22, user.getDeptCode());
            ps.setString(23, user.getDeptRestrict());
            ps.setString(24, user.getUnderTaker());
            done = ps.executeUpdate() != -1;
        }
        createPasswordHistory(userId, user.getPassword());
        }
        catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->"
                    + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;


    }


    public boolean updateManageUser(legend.admin.objects.User user, String limit)
    {
        Connection con;
        PreparedStatement ps;
        boolean done;
        con = null;
        ps = null;
        done = false;
        String query = "";
        try {
        if(user.getExpiryDate() == "" || user.getExpiryDate().equalsIgnoreCase("null") || user.getExpiryDate().equalsIgnoreCase(""))
        {
             query = "UPDATE AM_GB_USER  SET  User_Name = ?, Full_Name = ? ,Legacy_Sys_Id = ?, Class =" +
" ?, Branch = ?, Password = ?, Phone_No = ?,Is_Supervisor = ?,Must_Change_Pwd = ?" +
", Login_Status = ?, User_Status = ?, Fleet_Admin = ?, Email = ?,branch_restricti" +
"on =?, Expiry_Days =?,Approval_Limit = ?,dept_restriction=?,dept_code=?,UnderTaker=? WHERE User_Id = ?"
;
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
            ps.setString(16, limit);
            ps.setString(17, user.getDeptRestrict());
            ps.setString(18, user.getDeptCode());
            ps.setString(19, user.getUnderTaker());
            ps.setString(20, user.getUserId());
            
            done = ps.executeUpdate() != -1;
        } else
        {
             query = "UPDATE AM_GB_USER  SET  User_Name = ?, Full_Name = ? ,Legacy_Sys_Id = ?, Class =" +
" ?, Branch = ?, Password = ?, Phone_No = ?,Is_Supervisor = ?,Must_Change_Pwd = ?" +
", Login_Status = ?, User_Status = ?, Fleet_Admin = ?, Email = ?,branch_restricti" +
"on =?, Expiry_Days =?, Expiry_Date=?,Approval_Limit = ?,dept_code=?,dept_restriction=?,UnderTaker=?  WHERE User_" +
"Id = ?"
;
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
            ps.setDate(16, df.dateConvert(user.getExpiryDate()));
            ps.setString(17, limit);
            ps.setString(18, user.getDeptCode());
            ps.setString(19, user.getDeptRestrict());
            ps.setString(20, user.getUnderTaker());
            ps.setString(21, user.getUserId());
            done = ps.executeUpdate() != -1;
        }
        if(user.getExpiryDays() == 0)
        {
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

    public void compareAuditValues(String role_view, String role_addn, String role_edit, String class_id, String role_id, String user_id, String branchCode,
            int loginId, String eff_date)
    {
        AuditTrailGen atg;
        String role_view1;
        String role_addn1;
        String role_edit1;
        String query;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        atg = new AuditTrailGen();
        role_view1 = "";
        role_addn1 = "";
        role_edit1 = "";
        query = "SELECT role_view, role_addn, role_edit FROM am_ad_class_privileges WHERE  clss_u" +
"uid = ? AND Role_uuid=?"
;
        con = null;
        ps = null;
        rs = null;
        boolean done = false;
        try {
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setInt(1, Integer.parseInt(class_id));
        ps.setInt(2, Integer.parseInt(role_id));
//        System.out.println((new StringBuilder()).append("==============**=====class_id===============").append(class_id).toString());
//        System.out.println((new StringBuilder()).append("==============**=====role_id===============").append(role_id).toString());
        rs = ps.executeQuery();
//        System.out.println((new StringBuilder()).append("===================ResultSET===============").append(rs).toString());
        while (rs.next()) {
//            System.out.println((new StringBuilder()).append("===================WHILE INSIDE A ResultSET===============").append(rs).toString());
            role_view1 = rs.getString("role_view");
//            System.out.println((new StringBuilder()).append("===================role_view1===============").append(role_view1).toString());
            role_addn1 = rs.getString("role_addn");
//            System.out.println((new StringBuilder()).append("===================role_addn1===============").append(role_addn1).toString());
            role_edit1 = rs.getString("role_edit");
        }

//        System.out.println((new StringBuilder()).append("\n\n old values >> ").append(role_view1).append("\n\n new values >>").append(role_view).toString());
//        System.out.println((new StringBuilder()).append("\n\n old values >> ").append(role_addn1).append("\n\n new values >>").append(role_addn).toString());
//        System.out.println((new StringBuilder()).append("\n\n old values >> ").append(role_edit1).append("\n\n new values >>").append(role_edit).toString());
//        System.out.println((new StringBuilder()).append("\n\n role_id>> ").append(role_id).toString());
        if(!role_view1.equalsIgnoreCase(role_view))
        {
            atg.logAuditTrailSecurityComp("am_ad_class_privileges", branchCode, loginId, eff_date, role_id, role_view1, role_view, "role_view");
        }
        if(!role_addn1.equalsIgnoreCase(role_addn))
        {
            atg.logAuditTrailSecurityComp("am_ad_class_privileges", branchCode, loginId, eff_date, role_id, role_addn1, role_addn, "role_addn");
        }
        if(!role_edit1.equalsIgnoreCase(role_edit))
        {
            atg.logAuditTrailSecurityComp("am_ad_class_privileges", branchCode, loginId, eff_date, role_id, role_edit1, role_edit, "role_edit");
        }
//        System.out.println("===================logAuditTrailSecurityComp= within  compareAuditValues========" +
//"======"
//);
    } catch (Exception e) {
        System.out.println("WARNING:Error executing Query User Full NamecompareAuditValues: ->"
                + e.getMessage());
    } finally {
        closeConnection(con, ps, rs);
    }
    //return UserFullName;
}

    public boolean updateClassPrivilege1(ArrayList list, int userid, String branchCode, int loginId, String eff_date)
    {
        for(int i = 0; i < list.size(); i++)
        {
            System.out.println();
            ClassPrivilege cp = (ClassPrivilege)list.get(i);
            updateClassPrivilege1(cp, userid, branchCode, loginId, eff_date);
        }
//        System.out.println("Nos of rtecords updated = " + list.size());
 //       System.out.println((new StringBuilder()).append("Nos of rtecords updated = ").append(list.size()).toString());
        return true;
    }

    public void updateClassPrivilege1(ClassPrivilege cp, int userid, String branchCode, int loginId, String eff_date)
    {
        String query;
        Connection con;
        PreparedStatement ps;
        AuditTrailGen atg1 = new AuditTrailGen();
        query = "UPDATE am_ad_class_privileges SET role_view = ?,role_addn = ?,role_edit = ? WHER" +
"E clss_uuid=? AND Role_uuid=? "
;
        con = null;
        ps = null;
        ResultSet rs = null;
        int d[] = null;
        try {
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, cp.getRole_view());
        ps.setString(2, cp.getRole_addn());
        ps.setString(3, cp.getRole_edit());
        ps.setString(4, cp.getClss_uuid());
        ps.setString(5, cp.getRole_uuid());
        compareAuditValues(cp.getRole_view(), cp.getRole_addn(), cp.getRole_edit(), cp.getClss_uuid(), cp.getRole_uuid(), String.valueOf(userid), branchCode, loginId, eff_date);

        System.out.println("\n\n >>>>>>>>>>>>>>>>>> ps.execute() " + ps.executeUpdate());
        }
        catch (Exception ex) {
        System.out.println("WARN: Error doing updateClassPrivilege ->" + ex);
    } finally {
        closeConnection(con, ps);
    }
}

    public String getUserLogonStatus(String userName)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet res;
        String result;
        con = null;
        ps = null;
        res = null;
        result = null;
        try {
        con = getConnection();
        String query = "select Login_Status from am_gb_User where user_name = ?";
        ps = con.prepareStatement("select Login_Status from am_gb_User where user_name = ?");
        ps.setString(1, userName);
        res = ps.executeQuery();
        if(res.next())
        {
            result = res.getString("Login_Status");
        }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(con, ps, res);
        }
        return result;
    }

    public boolean updateLoginAsAboveLimit(String user_name, String ip)
    {
        Connection con;
        PreparedStatement ps;
        boolean done;
        con = null;
        ps = null;
        done = false;
        try {
        String query = "update am_gb_user set login_status = 3, login_system ='" + ip + "' where user_name = '" + user_name + "'";
        con = getConnection();
        ps = con.prepareStatement(query);
        done = ps.executeUpdate() != -1;
    }
        catch (Exception e) {
            System.out.println("WARNING:Error updateExpiryDate() Query ->"
                    + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }

    public String getCompanyLoginAttempt()
    {
        Connection con;
        PreparedStatement ps;
        ResultSet res;
        String result;
        con = null;
        ps = null;
        res = null;
        result = null;
        try {
        con = getConnection();
        String query = "select attempt_logon from am_gb_company";
        ps = con.prepareStatement("select attempt_logon from am_gb_company");
        res = ps.executeQuery();
        if(res.next())
        {
            result = res.getString("attempt_logon");
        }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(con, ps, res);
        }
        return result;
    }

    public boolean updateManageUser(User user)
    {
        Connection con;
        PreparedStatement ps;
        boolean done;
        con = null;
        ps = null;
        done = false;
        String query = "";
        try {
         query = "UPDATE AM_GB_USER  SET  Password = ?, Must_Change_Pwd = ? ,Login_Status = ? WHER" +
"E User_Id = ?"
;
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, user.getPassword());
        ps.setString(2, user.getMustChangePwd());
        ps.setString(3, user.getLoginStatus());
        ps.setString(4, user.getUserId());
        done = ps.executeUpdate() != -1;
    } catch (Exception e) {
        System.out.println("WARNING:Error executing Query ->" + e.getMessage());
        e.printStackTrace();
    } finally {
        closeConnection(con, ps);
    }
    return done;


}

    public String Name(String userName, String pagePassword)
    {
        String output = pagePassword.trim();
        String prefix = userName.trim().substring(0, 2);
        int length = userName.trim().length();
        int length2 = length - 2;
        String surrfix = userName.trim().substring(length2, length);
        String Password = (new StringBuilder()).append(surrfix).append(output).append(prefix).toString();
        return Password;
    }

    public boolean Name(String userName, String pagePassword, String databasePassword)
    {
        boolean result = false;  
        String output = pagePassword.trim();
        String prefix = userName.trim().substring(0, 2);
        int length = userName.trim().length();
        int length2 = length - 2;
        String surrfix = userName.trim().substring(length2, length);
        String Password = (new StringBuilder()).append(surrfix).append(output).append(prefix).toString();
        if(Password.trim().equals(databasePassword.trim()))
        {
            result = true;
        }
        return result; 
    }

    public java.util.ArrayList getUserByQuery(String filter, String Name, String pass)
    {
        java.util.ArrayList _list = new java.util.ArrayList();
        legend.admin.objects.User user = null;
        String query;
        Connection c;
        ResultSet rs;
        Statement s;  
        _list = new ArrayList();
        query = "SELECT User_Id, lower(User_Name) User_Name, Full_Name, Legacy_Sys_Id, Class, Bra" +
"nch, Password, Phone_No, is_Supervisor, Must_Change_Pwd, Login_Status, User_Stat" +
"us, UserId, Create_Date, Password_Expiry, Login_Date, Login_System, Fleet_Admin,dept_code," +
" email, Branch, password_changed,Expiry_Date,branch_restriction,dept_restriction, approval_limit," +
"approval_level,token_required,UnderTaker FROM AM_GB_USER WHERE User_Id IS NOT NULL"
;
        query = (new StringBuilder()).append(query).append(filter).toString();
        c = null;
        rs = null;    
        s = null;
        System.out.println("getUserByQuery 2 query: "+query);
        try {
        c = getConnection();
        s = c.createStatement();
        rs = s.executeQuery(query);
        while (rs.next()) {
            String userId = rs.getString("User_Id");
            String userName = rs.getString("User_Name");
            String password = rs.getString("Password");
            if(Name(userName, pass, cm.decrypt(password)))
            {
                String fullName = rs.getString("Full_Name");
                String legacySysId = rs.getString("Legacy_Sys_Id");
                String Class = rs.getString("Class");
                String branch = rs.getString("Branch");
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
                String tokenRequired = rs.getString("token_required");
                String dept_restriction = rs.getString("dept_restriction");
                String deptCode = rs.getString("dept_code");
                String underTaker = rs.getString("UnderTaker");
//                System.out.println((new StringBuilder()).append("####################################### Token Read from the DB here ====").append(tokenRequired).toString());
//                System.out.println((new StringBuilder()).append(" The sent tokenRequired ==== ").append(tokenRequired).toString());

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
                user.setDeptRestrict(dept_restriction);
                user.setDeptCode(deptCode);
                user.setUnderTaker(underTaker);
                if(tokenRequired != null && tokenRequired.trim().equalsIgnoreCase("Y"))
                {
                    user.setTokenRequired(true);
                } else
                {
                    user.setTokenRequired(false);
                }
//                System.out.println((new StringBuilder()).append("##################################### After setting the user the token of the us" +
//"er =="
//).append(user.isTokenRequired()).toString());
                _list.add(user);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        closeConnection(c, s, rs);
    }
    return _list;

}

    public PasswordHistory getPasswordHistory(String userid)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        String query;
        PasswordHistory ph;
        con = null;
        ps = null;
        rs = null;

        	query = "select * from ad_password_history where userid = '" + userid + "'";
         ph = null;
         try {
        con = getConnection();
        ps = con.prepareStatement(query);
        rs = ps.executeQuery();
        while (rs.next()) {
            String userId = rs.getString("USERID");
            int counter = rs.getInt("COUNTER");
            String password1 = rs.getString("PASSWORD1");
            String password2 = rs.getString("PASSWORD2");
            String password3 = rs.getString("PASSWORD3");
            String password4 = rs.getString("PASSWORD4");
            String password5 = rs.getString("PASSWORD5");
            String changeDate = rs.getString("CHANGEDATE");
            ph = new PasswordHistory(userId, counter, password1, password2, password3, password4, password5);
        }
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(con, ps, rs);
        }


        return ph;
    }

    public boolean checkPassword(String userid, String password)
    {
        boolean result = false;
        boolean result1 = true;
        boolean result2 = true;
        boolean result3 = true;
        boolean result4 = true;
        boolean result5 = true;
        PasswordHistory values = getPasswordHistory(userid);
        if(password.equals(values.getPassword1()))
        {
            result1 = false;
        }
        if(password.equals(values.getPassword2()))
        {
            result2 = false;
        }
        if(password.equals(values.getPassword3()))
        {
            result3 = false;
        }
        if(password.equals(values.getPassword4()))
        {
            result4 = false;
        }
        if(password.equals(values.getPassword5()))
        {
            result5 = false;
        }
        if(result1 && result2 && result3 && result4 && result5)
        {
            result = true;
        }
        return result;
    }

    public boolean updatePasswordHistory(PasswordHistory pass, String password)
    {
        Connection con;
        PreparedStatement ps;
        boolean done;
        String query;
        int counter;
        con = null;
        ps = null;
        done = false;
        query = "";
        counter = 0;
        try {
        if(pass.getCounter() == 1)
        {
            counter = pass.getCounter() + 1;
            query = "UPDATE ad_password_history  SET  COUNTER = ?, PASSWORD1 = ?  ,CHANGEDATE = ? WHE" +
"RE USERID = ?"
;
        }
        if(pass.getCounter() == 2)
        {
            counter = pass.getCounter() + 1;
            query = "UPDATE ad_password_history  SET  COUNTER = ?, PASSWORD2 = ?  ,CHANGEDATE = ? WHE" +
"RE USERID = ?"
;
        }
        if(pass.getCounter() == 3)
        {
            counter = pass.getCounter() + 1;
            query = "UPDATE ad_password_history  SET  COUNTER = ?, PASSWORD3 = ?  ,CHANGEDATE = ? WHE" +
"RE USERID = ?"
;
        }
        if(pass.getCounter() == 4)
        {
            counter = pass.getCounter() + 1;
            query = "UPDATE ad_password_history  SET  COUNTER = ?, PASSWORD4 = ?  ,CHANGEDATE = ? WHE" +
"RE USERID = ?"
;
        }
        if(pass.getCounter() == 5)
        {
            counter = 1;
            query = "UPDATE ad_password_history  SET  COUNTER = ? , PASSWORD5 = ? ,CHANGEDATE = ? WHE" +
"RE USERID = ?"
;
        }
        con = getConnection();
        System.out.println((new StringBuilder()).append("--").append(query).append("--").toString());
        ps = con.prepareStatement(query);
        ps.setInt(1, counter);
        ps.setString(2, password);
        ps.setDate(3, df.dateConvert(new Date()));
        ps.setString(4, pass.getUserId());
        done = ps.executeUpdate() != -1;
        }
        catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->ad_password_history" + e.getMessage());
            e.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
        return done;


    }

    public boolean login(String location, String compName, String Apps, String LicfileName)
    {
        boolean result = false;
        try
        {
            File file = new File(location);
            Properties props = new Properties();
            InputStream in = new FileInputStream(file);
            Licencer lic = new Licencer();
            props.load(in);
            String strCompany = props.getProperty("comp");
            String app = props.getProperty("app");
            String license = props.getProperty("lic-code");
            String authcode = props.getProperty("author.code");
            license = license == null ? "" : license.replaceAll("-", "");
//            if(lic.isKeyOkay(license, strCompany, app, Long.parseLong(authcode)))
            if(lic.isKeyOkay(license, strCompany, app, Long.parseLong(authcode), LicfileName) && strCompany.equalsIgnoreCase(compName) && Apps.equalsIgnoreCase(app))
            {
                result = true;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    public boolean changePassword2(String userid, String password, String expiry)
    {
        boolean result = false;
        PasswordHistory values = getPasswordHistory(userid);
        if(checkPassword(userid, password) && changePassword(userid, password, expiry) && updatePasswordHistory(values, password))
        {
            result = true;
        }
        return result;
    }

    public boolean changePassword3(String userid, String password, String expiry)
    {
        boolean result = false;
        PasswordHistory values = getPasswordHistory(userid);
        if(checkPassword(userid, password) && changePassword(userid, password, expiry) && updatePasswordHistory(values, password))
        {
            result = true;
        }
        return result;
    }

    public boolean createPasswordHistory(String userid, String Password)
    {
        Connection con;
        PreparedStatement ps;
        boolean done;
        String query;
        con = null;
        ps = null;
        done = false;
        query = "INSERT INTO ad_password_history(USERID,COUNTER,PASSWORD)    VALUES (?,?,?)"
;
        try {
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, userid);
        ps.setInt(2, 1);
        ps.setString(3, Password);
 //       ps.setString(4, Password);
  //      ps.setString(5, Password);
  //      ps.setString(6, Password);
 //       ps.setString(7, Password);
        done = ps.executeUpdate() != -1;
        }
        catch (Exception e) {
            System.out.println(this.getClass().getName()
                    + " ERROR:Error inserting ad_password_history  ->");
            e.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }

    public boolean updatePasswordHistory2(int counter, String password, String userId)
    {
        Connection con;
        PreparedStatement ps;
        boolean done;
        int limitValue;
        int freq;
        con = null;
        ps = null;
        done = false;
        String query = "";
        int limit = 0;
        limitValue = 0;
        freq = 0;

        limit = getPasswordHistory3(userId);
        if(limit == counter)
        {
            limitValue = 1;
        } else
        {
            limitValue = limit + 1;
        }
        int frequency = getFreq(userId);
        System.out.println((new StringBuilder()).append("frequency  ").append(frequency).toString());
        freq = frequency + limitValue;
        if(frequency == counter)
        {
            freq = 1;
        }
        System.out.println((new StringBuilder()).append("freq  ").append(freq).toString());
        updateFreq(freq, userId);
        try {
         query = "UPDATE ad_password_history  SET  LIMIT = ?, PASSWORD = ?  ,CHANGEDATE = ? WHERE " +
"USERID = ? AND COUNTER = ?  "
;
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setInt(1, limitValue);
        ps.setString(2, password);
        ps.setTimestamp(3, getDateTime(new Date()));
        ps.setString(4, userId);
        ps.setInt(5, freq);
        done = ps.executeUpdate() != -1;
    }
        catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->ad_password_history" + e.getMessage());
            e.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
        return done;


    }

    public boolean updateFreq(int freq, String userId)
    {
        Connection con;
        PreparedStatement ps;
        boolean done;
        con = null;
        ps = null;
        done = false;
        String query = "";
        try {
         query = "UPDATE ad_password_history  SET  freq = ?  WHERE USERID = ?  ";
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setInt(1, freq);
        ps.setString(2, userId);
        done = ps.executeUpdate() != -1;
        }
        catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->ad_password_history" + e.getMessage());
            e.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
        return done;


    }

    public java.util.ArrayList getAllPasswordHistory(String userid)
    {
    	java.util.ArrayList _list = new java.util.ArrayList();
        String query;
        Connection c;
        ResultSet rs;
        Statement s;
        PasswordHistory pass = null;
        query = (new StringBuilder()).append("select * from ad_password_history where userid = '").append(userid).append("' ").toString();
        c = null;
        rs = null;
        s = null;
        try {
        c = getConnection();
        s = c.createStatement();
        rs = s.executeQuery(query);
        while (rs.next()) {
            String userId = rs.getString("USERID");
            int counter = rs.getInt("COUNTER");
            String password1 = rs.getString("PASSWORD");
            pass = new PasswordHistory();
            pass.setCounter(counter);
            pass.setPassword1(password1);
            pass.setUserId(userId);
            _list.add(pass);
        }
    }
    catch (Exception e) {
        e.printStackTrace();
    } finally {
        closeConnection(c, s, rs);
    }
    return _list;

}

    public boolean changePassword3(String userid, String password, String expiry, int limitCheck) {
        boolean result = false;
        boolean result2 = false;
        //fetch record from  ad password history based on userid
        java.util.ArrayList list = getAllPasswordHistory(userid);

        if ((list.size() != 0)) {
            for (int x = 0; x < list.size(); x++) {
                PasswordHistory pass = (PasswordHistory) list.get(x);
                if (password.equals(pass.getPassword1())) {
                    result = false;
                    break;

                } else {
                    result = true;
                }
            }
        } else {
            boolean a = createPasswordHistory(userid, password, list.size() + 1);
            changePassword(userid, password, expiry);
            result2 = true;
        }

        //check against last five password
        if (result) {
            //change password
            if (changePassword(userid, password, expiry)) {
                if (list.size() >= limitCheck) {
                    //update record from  ad password history based on userid
                    if (updatePasswordHistory2(list.size(), password, userid)) {
                        result2 = true;
                    }

                } else {
                    createPasswordHistory(userid, password, list.size() + 1);
                    result2 = true;
                }
            }
        }
        return result2;
    }

    public boolean createPasswordHistory(String userid, String Password, int counter)
    {
        Connection con;
        PreparedStatement ps;
        boolean done;
        String query;
        con = null;
        ps = null;
        done = false;
        query = "INSERT INTO ad_password_history(USERID,COUNTER,PASSWORD)    VALUES (?,?,?)";
        try {
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, userid);
        ps.setInt(2, counter);
        ps.setString(3, Password);
        done = ps.executeUpdate() != -1;
    }
    catch (Exception e) {
        System.out.println(this.getClass().getName()
                + " ERROR:Error inserting ad_password_history  ->");
        e.printStackTrace();
    } finally {
        closeConnection(con, ps);
    }
    return done;

}

    public PasswordHistory getPasswordHistory2(String userid)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        String query;
        PasswordHistory ph;
        con = null;
        ps = null;
        rs = null;
        query = "select USERID,max(COUNTER) COUNTER,PASSWORD,CHANGEDATE,LIMIT from ad_password_history where userid = '" + userid + "'";
        ph = null;
        try {
        con = getConnection();
        ps = con.prepareStatement(query);
        for(rs = ps.executeQuery(); rs.next();)
        {
            String userId = rs.getString("USERID");
            int counter = rs.getInt("COUNTER");
            String password1 = rs.getString("PASSWORD");
            int limit = rs.getInt("LIMIT");
            ph = new PasswordHistory(userId, counter, password1, limit);
        }
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(con, ps, rs);
        }


        return ph;
    }

    public int getPasswordHistory3(String userid)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        int limit;
        String query;
        con = null;
        ps = null;
        rs = null;
        limit = 0;
        query = "select  max(counter) LIMIT from ad_password_history where userid = '" + userid + "'";

        try {
        con = getConnection();
        ps = con.prepareStatement(query);
        rs = ps.executeQuery();
        while (rs.next()) {
            limit = rs.getInt("LIMIT");
        }
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(con, ps, rs);
        }
        return limit;
    }

    public int getFreq(String userid)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        int frequency;
        String query;
        con = null;
        ps = null;
        rs = null;
        frequency = 0;
        query = "select  freq from ad_password_history where userid = '" + userid + "'";
        try {
        con = getConnection();
        ps = con.prepareStatement(query);
        rs = ps.executeQuery();
        while (rs.next()) {
            frequency = rs.getInt("freq");
        }
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(con, ps, rs);
        }
        return frequency;
    }

    public Timestamp getDateTime(Date inputDate)
    {
        String strDate = null;
        try
        {
            if(inputDate == null)
            {
                strDate = sdf.format(new Date());
            } else
            {
                strDate = sdf.format(inputDate);
            }
        }
        catch(Exception er)
        {
            System.out.println((new StringBuilder()).append("WARN : Error getting datetime ->").append(er).toString());
        }
        return getDateTime(strDate);
    }

    public Timestamp getDateTime(String inputDate)
    {
        Timestamp inputTime = null;
        try
        {
            if(inputDate == null)
            {
                inputDate = sdf.format(new Date());
            }
            inputDate = inputDate.replaceAll("/", "-");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss.SSS");
            String strDate = dateFormat.format(new Date());
            String transInputDate = (new StringBuilder()).append(inputDate).append(strDate.substring(10, strDate.length())).toString();
            inputTime = new Timestamp(dateFormat.parse(transInputDate).getTime());
        }
        catch(Exception er)
        {
            System.out.println((new StringBuilder()).append("WARN : Error getting datetime ->").append(er).toString());
        }
        return inputTime;
    }

    public String[] PasswordChange(String userName, String pagePassword)
    {
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        GregorianCalendar date = new GregorianCalendar();
        int sec = date.get(14);
        String output = pagePassword.trim();
        String prefix = userName.trim().substring(0, 2);
        int length = userName.trim().length();
        int length2 = length - 2;
        String surrfix = userName.trim().substring(length2, length);
        String Password = (new StringBuilder()).append(surrfix).append(prefix).append(sec).append(surrfix).append(prefix).toString();
        String PasswordDisplay = (new StringBuilder()).append(prefix).append(sec).append(surrfix).toString();
        String display[] = {
            Password, PasswordDisplay
        };
        return display;
    }

    public boolean createManageUser2(User user, String limit)
    {
        Connection con;
        PreparedStatement ps;
        boolean done;
        con = null;
        ps = null;
        done = false;
        String query = "";
        try {
        String userId = (new ApplicationHelper()).getGeneratedId("AM_GB_USER");
        if(user.getExpiryDate() == "" || user.getExpiryDate().equalsIgnoreCase("null") || user.getExpiryDate() == null || user.getExpiryDate().equalsIgnoreCase(""))
        {
             query = "INSERT INTO AM_GB_USER( User_Name, Full_Name, Legacy_Sys_Id, Class, Branch, Pass" +
"word, Phone_No, Is_Supervisor, Must_Change_Pwd, Login_Status, User_Status, UserI" +
"d, Create_Date, Password_Expiry,Fleet_Admin, Email,User_id,branch_restriction, E" +
"xpiry_Days,Approval_Limit,dept_code,token_required,approve_level,is_storekeeper,is_StockAdministrator,dept_restriction,UnderTaker)VALUES (?,?,?,?,?,?,?,?,?,?,?,?" +
",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
;
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
            ps.setDate(13, df.dateConvert(new Date()));
            ps.setDate(14, df.dateConvert(df.addDayToDate(new Date(), Integer.parseInt(user.getPwdExpiry()))));
            ps.setString(15, user.getFleetAdmin());
            ps.setString(16, user.getEmail());
            ps.setString(17, userId);
        //    System.out.println((new StringBuilder()).append("createManageUser2 tokenRequired: ").append(user.isTokenRequired()).toString());
            ps.setString(18, user.getBranchRestrict());
            ps.setInt(19, user.getExpiryDays());
            ps.setString(20, limit);
            ps.setString(21, user.getDeptCode());
            ps.setString(22, user.getTokenRequire());
            ps.setString(23, user.getApproveLevel());
            ps.setString(24, user.getIsStorekeeper());
            ps.setString(25, user.getIsStockAdministrator());
            ps.setString(26, user.getDeptRestrict());
            ps.setString(27, user.getUnderTaker());
            done = ps.executeUpdate() != -1;
        } else
        {
             query = "INSERT INTO AM_GB_USER( User_Name, Full_Name, Legacy_Sys_Id, Class, Branch, Pass" +
"word, Phone_No, Is_Supervisor, Must_Change_Pwd, Login_Status, User_Status, UserI" +
"d, Create_Date, Password_Expiry,Fleet_Admin, Email,User_id,branch_restriction, E" +
"xpiry_Days,Expiry_Date,Approval_Limit,Is_Supervisor,approve_level,is_storekeeper,is_StockAdministrator,dept_restriction,dept_code,UnderTaker)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
"?,?,?,?,?,?,?,?,?,?)"
;
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
            ps.setDate(13, df.dateConvert(new Date()));
            ps.setDate(14, df.dateConvert(df.addDayToDate(new Date(), Integer.parseInt(user.getPwdExpiry()))));
            ps.setString(15, user.getFleetAdmin());
            ps.setString(16, user.getEmail());
            ps.setString(17, userId);
            ps.setString(18, user.getBranchRestrict());
            ps.setInt(19, user.getExpiryDays());
            String temDate = user.getExpiryDate();
            ps.setDate(20, df.dateConvert(temDate));
            ps.setString(21, limit);
            ps.setString(22, user.getIsSupervisor());
            ps.setString(23, user.getApproveLevel());
            ps.setString(24, user.getIsStorekeeper());
            ps.setString(25, user.getIsStockAdministrator());
            ps.setString(26, user.getDeptRestrict());
            ps.setString(27, user.getDeptCode());
            ps.setString(28, user.getUnderTaker());
            done = ps.executeUpdate() != -1;
        }

        createPasswordHistory(userId, user.getPassword());
        }
        catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->"
                    + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;


    }

    public boolean updateManageUser2(User user, String limit)
    {
        Connection con;
        PreparedStatement ps;
        boolean done;
        con = null;
        ps = null;
        done = false;
        String query = "";
        try {
        if(user.getExpiryDate() == "" || user.getExpiryDate().equalsIgnoreCase("null") || user.getExpiryDate().equalsIgnoreCase(""))
        {
             query = "UPDATE AM_GB_USER  SET  User_Name = ?, Full_Name = ? ,Legacy_Sys_Id = ?, Class =" +
" ?, Branch = ?, Password = ?, Phone_No = ?,Is_Supervisor = ?,Must_Change_Pwd = ?" +
", Login_Status = ?, User_Status = ?, Fleet_Admin = ?, Email = ?,branch_restricti" +
"on =?, Expiry_Days =?,Approval_Limit = ? ,dept_code=?,token_required=?, is_storekeeper=?,is_StockAdministrator=?,dept_restriction=?,UnderTaker=? WHERE Use" +
"r_Id = ?"
;
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
            ps.setString(16, limit);
            ps.setString(17, user.getDeptCode());
            ps.setString(18, user.getTokenRequire());
            ps.setString(19, user.getIsStorekeeper());
            ps.setString(20, user.getIsStockAdministrator());
            ps.setString(21, user.getDeptRestrict());
            ps.setString(22, user.getUnderTaker());
            ps.setString(23, user.getUserId());
     //       System.out.println((new StringBuilder()).append("updateManageUser2 tokenRequired: ").append(user.isTokenRequired()).toString());
            done = ps.executeUpdate() != -1;
        } else
        {
             query = "UPDATE AM_GB_USER  SET  User_Name = ?, Full_Name = ? ,Legacy_Sys_Id = ?, Class =" +
" ?, Branch = ?, Password = ?, Phone_No = ?,Is_Supervisor = ?,Must_Change_Pwd = ?" +
", Login_Status = ?, User_Status = ?, Fleet_Admin = ?, Email = ?,branch_restricti" +
"on =?, Expiry_Days =?, Expiry_Date=?,Approval_Limit = ? ,token_required=?,dept_restriction=?,dept_code=?,UnderTaker=?  WHERE" +
" User_Id = ?"
;
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
            ps.setDate(16, df.dateConvert(user.getExpiryDate()));
            ps.setString(17, limit);
            ps.setString(18, user.getTokenRequire());
            ps.setString(19, user.getDeptRestrict());
            ps.setString(20, user.getDeptCode());
            ps.setString(21, user.getUnderTaker());
            ps.setString(22, user.getUserId());
            
            done = ps.executeUpdate() != -1;
        }
        if(user.getExpiryDays() == 0)
        {
            updateExpiryDate(Integer.parseInt(user.getUserId()));
        }  
        }
        catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->"
                    + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;


    }
}
