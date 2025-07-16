package legend;

import java.sql.ResultSet;

import audit.AuditInfo;
import audit.AuditTrailGen;
import com.magbel.util.DatetimeFormat;

/**
 * <p>Title: Magma.net System</p>
 *
 * <p>Description: Fixed Assets Manager</p>
 *
 * <p>Copyright: Copyright (c) 2006. All rights reserved.</p>
 *
 * <p>Company: Magbel Technologies Limited.</p>
 *
 * @author Charles Ayoola Ayodele-Peters.
 <pre>
   Modification: by Jejelowo .B.Festus:
   Reason : To Change All The Store Procedure to
       approprite Java SQL Statement.
   Date	: Friday 27th November,2006
 </pre>
 * @version 1.0
 */
public class UserSetupBean extends ConnectionClass {
    private String[] users = new String[15];
    private String start_record = "0";
    private String rec_count = "2";

    private AuditInfo ai = new AuditInfo();
    private AuditTrailGen atg = new AuditTrailGen();
    DatetimeFormat df = new DatetimeFormat();

    public final void setAuditInfo(AuditInfo AI) {
        this.ai = AI;
    }

    public final void setAuditInfo(String TableName, String BranchCode,
                                   int LoginId, String RowId, boolean

                                   ReqInsertAudit) {
        ai.setTableName(TableName);
        ai.setBranchCode(BranchCode);
        ai.setLoginId(LoginId);
        ai.setRowId(RowId);
        ai.setReqInsertAudit(ReqInsertAudit);
    }

    public final AuditInfo getAuditInfo() {
        return this.ai;
    }


    public UserSetupBean() throws Exception {
    }

    public void setUsers(String[] users) {
        this.users = users;
    }

    public void setStart_record(String start_record) {
        this.start_record = start_record;
    }

    public void setRec_count(String rec_count) {
        this.rec_count = rec_count;
    }

    public String[] getUsers() {
        return users;
    }

    public String getStart_record() {
        return start_record;
    }

    public String getRec_count() {
        return rec_count;
    }

    /**
     * selectsections
     *
     * @param con String
     * @param sta String
     * @return String[][]
     * @throws Throwable
     */
    public String[][] selectUsers(String con, String sta) throws Throwable {
        StringBuffer cq = new StringBuffer(100);
        String countQuery = "";
        String selectQuery = "";
        if (con.equals("0")) {
            countQuery = "SELECT count(*) FROM AM_GB_USER   " +
                         "WHERE USER_STATUS = '" + sta + "'";
        } else if (!con.equals("0")) {
            countQuery = "SELECT count(*) FROM AM_GB_USER   " +
                         "WHERE USER_ID =" + con;
        }
        if (con.equals("0")) {
            selectQuery = "SELECT * FROM AM_GB_USER   " +
                          "WHERE USER_STATUS = '" + sta + "'  " +
                          "ORDER BY USER_NAME ASC";
        } else if (!con.equals("0")) {
            selectQuery = "SELECT * FROM AM_GB_USER   " +
                          "WHERE USER_ID =" + con;

        }

        ResultSet rc = getStatement().executeQuery(countQuery);
        ResultSet rv = getStatement().executeQuery(selectQuery);

        rc.next();
        String[][] values = new String[rc.getInt(1)][20];

        for (int x = 0; x < values.length; x++) {
            rv.next();
            for (int y = 0; y < 20; y++) {
                values[x][y] = rv.getString(y + 1);
            }
        }
        freeResource();
        return values;
    }

    /**
     * selectsections
     *
     * @param cls String
     * @return String[][]
     * @throws Throwable
     */
    public String[][] selectUsersClass(String cls, String sta) throws Throwable {

        String countQuery = "SELECT COUNT(*) FROM AM_GB_USER  " +
                            "WHERE CLASS = '" + cls + "' AND USER_STATUS = '" +
                            sta + "'";
        String selectQuery = "SELECT * FROM AM_GB_USER   " +
                             "WHERE CLASS = '" + cls + "' AND USER_STATUS = '" +
                             sta + "'";

        ResultSet rc = getStatement().executeQuery(countQuery);
        ResultSet rv = getStatement().executeQuery(selectQuery);

        rc.next();
        String[][] values = new String[rc.getInt(1)][20];

        for (int x = 0; x < values.length; x++) {
            rv.next();
            for (int y = 0; y < 20; y++) {
                values[x][y] = rv.getString(y + 1);
            }
        }
        freeResource();
        return values;
    }

    /**
     * selectsections
     *
     * @param bra String
     * @return String[][]
     * @throws Throwable
     */
    public String[][] selectUsersBranch(String bra, String sta) throws
            Throwable {

        String countQuery = "SELECT count(*) FROM AM_GB_USER " +
                            "WHERE BRANCH = " + bra + " AND USER_STATUS = '" +
                            sta + "' ";
        String selectQuery = "SELECT * FROM AM_GB_USER   " +
                             "WHERE BRANCH = " + bra + " AND USER_STATUS = '" +
                             sta + "'";

        ResultSet rc = getStatement().executeQuery(countQuery);
        ResultSet rv = getStatement().executeQuery(selectQuery);

        rc.next();
        String[][] values = new String[rc.getInt(1)][20];

        for (int x = 0; x < values.length; x++) {
            rv.next();
            for (int y = 0; y < 20; y++) {
                values[x][y] = rv.getString(y + 1);
            }
        }
        freeResource();
        return values;
    }

    /**
     * selectsections
     *
     * @param cls String
     * @param bra String
     * @return String[][]
     * @throws Throwable
     */
    public String[][] selectUsersClassBranch(String cls, String bra, String sta) throws
            Throwable {

        String countQuery = "SELECT * FROM AM_GB_USER  " +
                            "WHERE CLASS = '" + cls + "' AND BRANCH = " + bra +
                            " AND USER_STATUS = '" + sta + "'";
        String selectQuery = "SELECT * FROM AM_GB_USER  " +
                             "WHERE CLASS = '" + cls + "' AND BRANCH = " +
                             bra + " AND USER_STATUS = '" + sta + "'";

        ResultSet rc = getStatement().executeQuery(countQuery);
        ResultSet rv = getStatement().executeQuery(selectQuery);

        rc.next();
        String[][] values = new String[rc.getInt(1)][20];

        for (int x = 0; x < values.length; x++) {
            rv.next();
            for (int y = 0; y < 20; y++) {
                values[x][y] = rv.getString(y + 1);
            }
        }
        freeResource();
        return values;
    }

    /**
     * updatesections
     *
     * @param con String
     * @return boolean
     * @throws Throwable
     */
    public boolean updateUsers(String con) throws Throwable {

        String query = "UPDATE AM_GB_USER SET USER_NAME = '" + users[0] + "'," +
                       "FULL_NAME = '" + users[1] + "', LEGACY_SYS_ID = '" +
                       users[2] + "'," +
                       "BRANCH = " + users[5] + ", CLASS = " + users[3] + ", " +
                       "PASSWORD = '" + users[4] + "', PHONE_NO = '" + users[6] +
                       "'," +
                       "IS_SUPERVISOR = '" + users[7] + "', FLEET_ADMIN = '" +
                       users[8] + "'," +
                       "MUST_CHANGE_PWD = '" + users[9] + "', USER_STATUS = '" +
                       users[12] + "'," +
                       "LOGIN_STATUS = " + users[11] + " , EMAIL = '" +
                       users[14] + "'  " +
                       "WHERE USER_ID = " + con;
        //System.out.println(query);

        int ret = getStatement().executeUpdate(query);
        if (ret > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * insertsections
     *
     * @return boolean
     * @throws Throwable
     */
    public boolean insertUsers() throws Throwable {

        DatetimeFormat gDate = new DatetimeFormat();
        String query = "INSERT INTO AM_GB_USER(" +
                       "USER_NAME, FULL_NAME, LEGACY_SYS_ID," +
                       "BRANCH, CLASS, PASSWORD, PHONE_NO," +
                       "IS_SUPERVISOR, FLEET_ADMIN, MUST_CHANGE_PWD," +
                       "PASSWORD_EXPIRY, LOGIN_STATUS, USER_STATUS," +
                       "USER_ID, CREATE_DATE, EMAIL, password_changed ) VALUES(" +
                       "'" + users[0] + "', '" + users[1] + "','" + users[2] +
                       "', " + users[5] + "," +
                       users[3] + ", '" + users[4] + "', '" + users[6] + "'," +
                       "'" + users[7] + "', '" + users[8] + "', '" +
                       users[9] + "', DATEADD(day, " + users[10] +
                       ", getDate()) ,'" + users[11] + "'," +
                       "'" + users[12] + "', " + users[13] + ", getDate(),'" +
                       users[14] + "','N')";

        //System.out.println(query);
        if (ai.reqInsertAudit() == true) {
            atg.select(1,
                    "SELECT * FROM AM_GB_USER WHERE user_Id = (SELECT MIN(user_Id) FROM AM_GB_USER)");
            atg.captureAuditFields(users);
            atg.logAuditTrail(ai);
        }
        boolean done = (getStatement().executeUpdate(query) != -1);
        //System.out.println(done);
        freeResource();
        return done;
    }

    /**
     * queryLogins
     *
     * @param name String[]
     * @param pass String
     * @return boolean
     * @throws Throwable
     */
    public String[] queryLogins(String name, String pass) throws Throwable {

        String query = "SELECT USER_ID, USER_NAME, CLASS, BRANCH, LOGIN_DATE," +
                       "LOGIN_SYSTEM, LOGIN_STATUS, PASSWORD_CHANGED,  " +
                       "IS_SUPERVISOR, MUST_CHANGE_PWD, FLEET_ADMIN " +
                       "FROM AM_GB_USER  " +
                       "WHERE USER_STATUS = 'A' " +
                       "AND USER_NAME = '" + name + "' " +
                       "AND PASSWORD = '" + pass + "'";

        ResultSet rs = getStatement().executeQuery(query);
        String[] logins = new String[11];
        if (rs.next()) {
            for (int i = 0; i < 11; i++) {
                logins[i] = rs.getString(i + 1);
            }
        }
        freeResource();
        return logins;
    }

    /**
     * updatesections
     *
     * @param users String
     * @return boolean
     * @throws Throwable
     */
    public boolean updatePassword(String[] users) throws Throwable {
        StringBuffer iq = new StringBuffer(100);

        String query = "UPDATE AM_GB_USER " +
                       "SET PASSWORD = '" + users[1] +
                       "', PASSWORD_EXPIRY = getDate() + " + users[2] + ", " +
                       "PASSWORD_CHANGED = '" + users[3] + "' WHERE USER_ID = " +
                       users[0];

        boolean result = (getStatement().executeUpdate(query) != -1);
        freeResource();
        return result;
    }

    /**
     * queryLogins
     *
     * @param iden String[]
     * @param pass String
     * @return boolean
     * @throws Throwable
     */
    public boolean confirmPassword(String iden, String pass) throws Throwable {

        String query = "SELECT PASSWORD FROM AM_GB_USER   " +
                       "WHERE USER_ID = " + iden;

        ResultSet rs = getStatement().executeQuery(query);
        rs.next();
        boolean result = pass.equals(rs.getString(1));
        freeResource();
        return result;
    }

    /**
     * queryLogins
     *
     * @param iden String[]
     * @param pass String
     * @return boolean
     * @throws Throwable
     */
    public boolean updateLogins(String userid, String ipaddress) throws
            Throwable {

        String query = "UPDATE AM_GB_USER  SET LOGIN_STATUS= 1 ,LOGIN_SYSTEM='" +
                       ipaddress + "'," +
                       "login_date = GetDate()" +
                       " WHERE   USER_ID = " + userid;

        boolean result = getStatement().execute(query);
        freeResource();
        return result;
    }

    public boolean updateLogins(String userid, String status, String ipaddress) throws
            Throwable {

        String query = "UPDATE AM_GB_USER  SET LOGIN_STATUS= " + status +
                       ",LOGIN_SYSTEM='" + ipaddress + "'," +
                       "login_date = GetDate()" +
                       " WHERE   USER_ID = " + userid;

        boolean result = getStatement().execute(query);
        freeResource();
        return result;
    }

    /**
     * Author: Rahman Oloritun,
     * purpose: to enforce unique usernames
     * date: Friday, November 10 2006
     * @param user_name
     * @return boolean
     */
    public boolean userExist(String user_name) throws Throwable {
        String query = "SELECT * FROM AM_GB_USER WHERE [USER_NAME]='" +
                       user_name + "'";
        ResultSet result = getStatement().executeQuery(query);
        boolean retval = result.next();
        freeResource();
        return retval;
    }

    /**
     * Author: Rahman Oloritun,
     * purpose: to get the internal identifier of a partcular user name
     * date: Friday, November 10 2006
     */
    public int getUserId(String user_name) throws Throwable {
        String query = "SELECT USER_ID FROM AM_GB_USER WHERE [USER_NAME]='" +
                       user_name + "'";
        ResultSet result = getStatement().executeQuery(query);
        int userid = 0;
        if (result.next()) {
            userid = result.getInt("USER_ID");
        }
        freeResource();
        return userid;
    }

    /**
     * Author: Rahman Oloritun,
     * purpose: to check if password has expired and is due for a change
     * date: Friday, November 10 2006
     */
    public boolean queryPexpiry(String userid) throws Throwable {
        String query =
                "SELECT DATEDIFF(day, GetDate(), password_expiry) AS term FROM AM_GB_USER   " +
                "WHERE USER_ID = " + userid;
        boolean result = true;
        //CurrentDateTime cd =  new CurrentDateTime();
        ResultSet rs = getStatement().executeQuery(query);
        if (rs.next()) {
            //System.out.println(rs.getInt("term"));
            if (rs.getInt("term") <= 0) {
                result = false;
            }
        }
        freeResource();
        return result;

    }
}
