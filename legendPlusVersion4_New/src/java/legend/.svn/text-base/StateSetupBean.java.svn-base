package legend;

import java.sql.*;
import magma.net.dao.MagmaDBConnection;
import audit.*;

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
 * @version 1.0
 */
public class StateSetupBean extends ConnectionClass {
    private String[] states = new String[5];
    private String[] stateCodes = new String[5];

    private AuditInfo ai;
    private AuditTrailGen atg;


    public final void setAuditInfo(AuditInfo AI) {
        this.ai = AI;
    }


    public final void setAuditInfo(String TableName, String BranchCode,
                                   int LoginId, String RowId,
                                   boolean ReqInsertAudit) {
        ai.setTableName(TableName);
        ai.setBranchCode(BranchCode);
        ai.setLoginId(LoginId);
        ai.setRowId(RowId);
        ai.setReqInsertAudit(ReqInsertAudit);
    }


    public final AuditInfo getAuditInfo() {
        return this.ai;
    }


    public StateSetupBean() throws Throwable {
    }

    public void setStates(String[] states) {
        if (states != null) {
            this.states = states;
        }
    }

    public String[] getStates() {
        return states;
    }

    public void setStateCodes(String[] stateCodes) {
        if (stateCodes != null) {
            this.stateCodes = stateCodes;
        }
    }

    public String[] getStateCodes() {
        return stateCodes;
    }

    /**
     * selectStates
     *
     * @param con String
     * @param sta String
     * @return String[][]
     * @throws Throwable
     */

    public String[][] selectStates(String con, String sta) throws Throwable {
        StringBuffer cq = new StringBuffer(100);

        String statusQuery =
                "SELECT COUNT(*) FROM AM_GB_STATES WHERE STATE_STATUS = '" +
                sta + "'";
        String idQuery = "SELECT COUNT(*) FROM AM_GB_STATES WHERE STATE_ID = " +
                         con;

        String selQuerySatus =
                "SELECT * FROM AM_GB_STATES WHERE STATE_STATUS = '" + sta +
                "' " +
                "ORDER BY STATE_NAME ASC";

        String selQueryId = "SELECT * FROM AM_GB_STATES WHERE STATE_ID = " +
                            con;

        ResultSet rc = null;
        if (con.equals("0")) {
            rc = getStatement().executeQuery(statusQuery);
        } else if (con != "") {
            rc = getStatement().executeQuery(idQuery);
        } else {}

        ResultSet rv = null;
        if (con.equals("0")) {
            rv = getStatement().executeQuery(selQuerySatus);
        } else if (con != "") {
            rv = getStatement().executeQuery(selQueryId);
        } else {}

        rc.next();
        String[][] values = new String[rc.getInt(1)][5];

        for (int x = 0; x < values.length; x++) {
            rv.next();
            for (int y = 0; y < 5; y++) {
                values[x][y] = rv.getString(y + 1);
            }
        }

        return values;
    }

    /**
     * updateStates
     *
     * @param con String
     * @return boolean
     * @throws Throwable
     */
    public boolean updateState(String con) throws Throwable {
        StringBuffer iq = new StringBuffer(100);
        String str = "UPDATE AM_GB_STATES SET STATE_CODE = '" + states[0] +
                     "',  " +
                     "STATE_NAME = '" + states[1] + "', STATE_STATUS = '" +
                     states[2] + "' WHERE STATE_ID = " + con;

        int ret = getStatement().executeUpdate(str);
        if (ret > 0) {
            return true;
        } else {
            return false;
        }
    }


    public String[][] selectStateCodes(String stateCode) throws Throwable {

        Connection mor = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        System.out.print("Action Performed");

        MagmaDBConnection dbConn = new MagmaDBConnection();

        String count = "";
        String select = "";
        count = "SELECT COUNT(*) FROM AM_GB_STATES  " +
                "WHERE STATE_CODE = ?";

        select = "SELECT * FROM AM_GB_STATES WHERE   " +
                 "STATE_CODE = ?    ";

        String[][] values = new String[50][5];
        int j = 0;
        try {
            mor = dbConn.getConnection("fixedasset");
            ps = mor.prepareStatement(count);
            ps.setString(1, stateCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                j = rs.getInt(1);
            }

            ps = mor.prepareStatement(select);
            ps.setString(1, stateCode);

            rs = ps.executeQuery();

            values = new String[j][5];

            for (int x = 0; x < values.length; x++) {
                rs.next();
                for (int y = 0; y < 5; y++) {
                    values[x][y] = rs.getString(y + 1);
                }
            }
        } catch (Exception e) {
            System.out.println("INFO: Error fetching State Code ->" +
                               e.getMessage());
        } finally {
            dbConn.closeConnection(mor, ps, rs);
        }
        return values;
    }

    public boolean isUniqueCode(String stateCode) throws Throwable {
        String query =
                "SELECT STATE_CODE FROM AM_GB_STATES WHERE STATE_CODE = ? ";

        Connection mor = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean confirm = false;

        MagmaDBConnection dbConn = new MagmaDBConnection();
        try {
            mor = dbConn.getConnection("fixedasset");
            ps = mor.prepareStatement(query);

            ps.setString(1, stateCode);

            rs = ps.executeQuery();
            while (rs.next()) {
                confirm = true;
            }

        } catch (Exception e) {
            System.out.println("INFO:Error checking for duplicate code->" +
                               e.getMessage());

        } finally {
            dbConn.closeConnection(mor, ps, rs);
        }

        return confirm;
    }


    /*
      public boolean updateStates(String con) throws Throwable {
        StringBuffer iq = new StringBuffer(100);
        iq.append("am_msp_update_states");
        iq.append("'");
        iq.append(con);
        iq.append("'");

        for(int i = 0; i < 4; i++){
          switch (i) {
            case -1:
              iq.append(", ");
              iq.append(states[i]);
              break;
            default:
              iq.append(",'");
              iq.append(states[i]);
              iq.append("'");
          }
        }

        return (getStatement().executeUpdate(
          iq.toString()) == -1);
      }

     */

    /**
     * insertStates
     *
     * @return boolean
     * @throws Throwable
     */
    public boolean insertStates() throws Throwable {
        StringBuffer iq = new StringBuffer(100);
        String str = "INSERT INTO AM_GB_STATES( " +
                     "STATE_CODE,STATE_NAME,STATE_STATUS,[USER_ID],CREATE_DATE " +
                     ") 	VALUES(";

        iq.append(str);
        iq.append("'");
        iq.append(states[0]);
        iq.append("'");

        for (int i = 1; i <= 3; i++) {
            switch (i) {
            case -1:
                iq.append(", ");
                iq.append(states[i]);
                break;
            default:
                iq.append(",'");
                iq.append(states[i]);
                iq.append("'");
            }
        }
        iq.append(",getDate())");
        if (ai.reqInsertAudit() == true) {
            atg.select(1, "SELECT * FROM AM_GB_STATES WHERE state_Id = (SELECT MIN(state_Id) FROM AM_GB_STATES)");
            atg.captureAuditFields(states);
            atg.logAuditTrail(ai);
        }
        boolean done = (getStatement().executeUpdate(iq.toString()) == -1);
        System.out.println("INFO:Return variable is " + done);
        return true;
    }
}

