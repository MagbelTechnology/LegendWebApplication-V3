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
public class MakeSetupBean extends ConnectionClass {
    private String[] makes = new String[6];
    private String[] maker = new String[6];

    private AuditInfo ai = new AuditInfo();
    private AuditTrailGen atg = new AuditTrailGen();

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


    public MakeSetupBean() throws Throwable {
    }

    public void setMakes(String[] makes) {
        if (makes != null) {
            this.makes = makes;
        }
    }

    public String[] getMakes() {
        return makes;
    }

    public void setMaker(String[] maker) {
        if (maker != null) {
            this.maker = maker;
        }
    }

    public String[] getMaker() {
        return maker;
    }

    /**
     * selectMakes
     *
     * @param con String
     * @param sta String
     * @return String[][]
     * @throws Throwable
     */
    public String[][] selectMakes(String con, String sta) throws Throwable {
        StringBuffer cq = new StringBuffer(100);

        String statusQuery =
                "SELECT COUNT(*) FROM AM_GB_ASSETMAKE WHERE STATUS = '" + sta +
                "'";
        String idQuery =
                "SELECT COUNT(*) FROM AM_GB_ASSETMAKE WHERE ASSETMAKE_ID = " +
                con;

        String selQuerySatus =
                "SELECT ASSETMAKE_ID, ASSETMAKE_CODE, ASSETMAKE, STATUS," +
                "CATEGORY_NAME, AM_GB_ASSETMAKE.USER_ID, AM_GB_ASSETMAKE.CREATE_DATE   " +
                "FROM AM_GB_ASSETMAKE JOIN AM_AD_CATEGORY ON    " +
                "AM_GB_ASSETMAKE.CATEGORY_ID=AM_AD_CATEGORY.CATEGORY_ID	WHERE STATUS = '" +
                sta + "' " +
                "ORDER BY ASSETMAKE ASC";

        String selQueryId =
                "SELECT * FROM AM_GB_ASSETMAKE WHERE ASSETMAKE_ID = " + con;

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
        String[][] values = new String[rc.getInt(1)][6];

        for (int x = 0; x < values.length; x++) {
            rv.next();
            for (int y = 0; y < 6; y++) {
                values[x][y] = rv.getString(y + 1);
            }
        }

        return values;
    }


    /**
     * updateMakes
     *
     * @param con String
     * @return boolean
     * @throws Throwable
     */
    public boolean updateMakes(String con) throws Throwable {
        StringBuffer iq = new StringBuffer(100);
        String str = "update am_gb_assetmake set assetmake_code = '" + makes[0] +
                     "', " +
                     "assetmake = '" + makes[1] + "',category_id = '" + makes[2] +
                     "', " +
                     "status = '" + makes[3] + "' where assetmake_id = " + con;

        int ret = getStatement().executeUpdate(str);
        if (ret > 0) {
            return true;
        } else {
            return false;
        }

    }

    public String[][] selectMakeCode(String makeCode) throws Throwable {

        Connection mor = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        System.out.println("Action Performed");

        MagmaDBConnection dbConn = new MagmaDBConnection();

        String count = "";
        String select = "";
        count = "SELECT COUNT(*) FROM AM_GB_ASSETMAKE " +
                "WHERE ASSETMAKE_CODE = ?";

        select = "SELECT * FROM AM_GB_ASSETMAKE WHERE " +
                 "ASSETMAKE_CODE = ?  ";

        String[][] values = new String[600][6];
        int j = 0;

        try {
            mor = dbConn.getConnection("fixedasset");

            ps = mor.prepareStatement(count);
            ps.setString(1, makeCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                j = rs.getInt(1);
            }

            ps = mor.prepareStatement(select);
            ps.setString(1, makeCode);

            rs = ps.executeQuery();

            values = new String[j][6];

            for (int x = 0; x < values.length; x++) {
                rs.next();
                for (int y = 0; y < 6; y++) {
                    values[x][y] = rs.getString(y + 1);
                }
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching Make Code->" + e.getMessage());
        } finally {
            dbConn.closeConnection(mor, ps, rs);
        }
        return values;
    }

    public boolean isUniqueCode(String makeCode) throws Throwable {
        String query = "SELECT ASSETMAKE_CODE FROM AM_GB_ASSETMAKE " +
                       "WHERE ASSETMAKE_CODE = ? ";
        Connection mor = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean confirm = false;

        MagmaDBConnection dbConn = new MagmaDBConnection();

        try {
            mor = dbConn.getConnection("fixedasset");
            ps = mor.prepareStatement(query);

            ps.setString(1, makeCode);

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


    /**
     * insertMakes
     *
     * @return boolean
     * @throws Throwable
     */
    public boolean insertMakes() throws Throwable {
        StringBuffer iq = new StringBuffer(100);
        String str = "INSERT INTO AM_GB_ASSETMAKE(" +
                     "ASSETMAKE_CODE,ASSETMAKE,CATEGORY_ID,STATUS,[USER_ID],CREATE_DATE ) VALUES(";

        iq.append(str);
        iq.append("'");
        iq.append(makes[0]);
        iq.append("'");

        for (int i = 1; i <= 4; i++) {
            switch (i) {
            case -1:
                iq.append(", ");
                iq.append(makes[i]);
                break;
            default:
                iq.append(",'");
                iq.append(makes[i]);
                iq.append("'");
            }
        }
        iq.append(",getDate())");

        if (ai.reqInsertAudit() == true) {
            atg.select(1, "SELECT * FROM AM_GB_ASSETMAKE WHERE make_Id = (SELECT MIN(make_Id) FROM AM_GB_ASSETMAKE)");
            atg.captureAuditFields(makes);
            atg.logAuditTrail(ai);
        }
        boolean done = (getStatement().executeUpdate(iq.toString()) == -1);
        System.out.println("INFO:Return variable is " + done);
        return true;
    }
}

