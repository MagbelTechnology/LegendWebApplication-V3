package legend;

import java.sql.ResultSet;

import audit.AuditInfo;
import audit.AuditTrailGen;

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
    Modification:By Kareem Wasiu Remilekun.
    Reason : To Change All The Store Procedure to
       approprite Java SQL Statement,avoid
    duplicate data entry and
       free the resource from exeeding the
     connection pool thereby shutting down
    the application
    Date	:Thursday 26th October,2006
 </pre>
 * @version 1.0
 */

public class ActiveUserBean extends ConnectionClass {
    private String[] ausers = new String[5];

    private AuditInfo ai;
    private AuditTrailGen atg;

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

    public ActiveUserBean() throws Throwable {
    }

    public void setAusers(String[] ausers) {
        this.ausers = ausers;
    }

    public String[] getAusers() {
        return ausers;
    }

    /**
     * selectsections
     *
     * @param con String
     * @return String[][]
     * @throws Throwable
     */
    public String[][] selectAusers(String con, String sta) throws Throwable {
        String countQuery = "SELECT COUNT(*) FROM AM_GB_USER  " +
                            "WHERE LOGIN_STATUS = '1'";
        String selectQuery = "SELECT USER_ID, USER_NAME, FULL_NAME," +
                             "PHONE_NO, LOGIN_SYSTEM    " +
                             "FROM AM_GB_USER    " +
                             "WHERE LOGIN_STATUS = '1'";

        ResultSet rc = getStatement().executeQuery(countQuery);
        ResultSet rv = getStatement().executeQuery(selectQuery);

        rc.next();
        String[][] values = new String[rc.getInt(1)][5];

        for (int x = 0; x < values.length; x++) {
            rv.next();
            for (int y = 0; y < 5; y++) {
                values[x][y] = rv.getString(y + 1);
            }
        }

        //freeResource();
        return values;
    }

    /**
     * updatesections
     *
     * @param con String
     * @return boolean
     * @throws Throwable
     */
    public boolean logoutUsers(String con) throws Throwable {

        String query =
                "UPDATE AM_GB_USER SET LOGIN_STATUS = '0' WHERE USER_ID=" + con;
        boolean done = (getStatement().executeUpdate(query) == -1);
        freeResource();
        return done;
    }
}
