package legend;

import java.sql.ResultSet;
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
public class CompanyDriversBean extends ConnectionClass {
    private String[] drivers = new String[19];

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


    public CompanyDriversBean() throws Exception {
    }

    public void setDrivers(String[] drivers) {
        if (drivers != null) {
            this.drivers = drivers;
        }
    }

    public String[] getDrivers() {
        return drivers;
    }

    /**
     * selectdrivers
     *
     * @param con String
     * @param sta String
     * @return String[][]
     * @throws Throwable
     */
    public String[][] selectDrivers(String con, String sta) throws Throwable {
        StringBuffer cq = new StringBuffer(100);
        cq.append("am_msp_count_drivers"
                  + " '" + con + "','" + sta + "'");
        ResultSet rc = getStatement().executeQuery(
                cq.toString());

        StringBuffer sq = new StringBuffer(100);
        sq.append("am_msp_select_drivers"
                  + " '" + con + "','" + sta + "'");
        ResultSet rv = getStatement().executeQuery(
                sq.toString());

        rc.next();
        String[][] values = new String[rc.getInt(1)][19];

        for (int x = 0; x < values.length; x++) {
            rv.next();
            for (int y = 0; y < 19; y++) {
                values[x][y] = rv.getString(y + 1);
            }
        }

        return values;
    }

    /**
     * selectdrivers
     *
     * @param dep String
     * @param sta String
     * @return String[][]
     * @throws Throwable
     */
    public String[][] selectDriversDepart(String dep, String sta) throws
            Throwable {
        StringBuffer cq = new StringBuffer(100);
        cq.append("am_msp_count_drivers_depart'" + dep + "', '" + sta + "'");
        ResultSet rc = getStatement().executeQuery(
                cq.toString());

        StringBuffer sq = new StringBuffer(100);
        sq.append("am_msp_select_drivers_depart'" + dep + "', '" + sta + "'");
        ResultSet rv = getStatement().executeQuery(
                sq.toString());

        rc.next();
        String[][] values = new String[rc.getInt(1)][19];

        for (int x = 0; x < values.length; x++) {
            rv.next();
            for (int y = 0; y < 19; y++) {
                values[x][y] = rv.getString(y + 1);
            }
        }

        return values;
    }

    /**
     * selectdrivers
     *
     * @param bra String
     * @param sta String
     * @return String[][]
     * @throws Throwable
     */
    public String[][] selectDriversBranch(String bra, String sta) throws
            Throwable {
        StringBuffer cq = new StringBuffer(100);
        cq.append("am_msp_count_drivers_branch'" + bra + "', '" + sta + "'");
        ResultSet rc = getStatement().executeQuery(
                cq.toString());

        StringBuffer sq = new StringBuffer(100);
        sq.append("am_msp_select_drivers_branch'" + bra + "', '" + sta + "'");
        ResultSet rv = getStatement().executeQuery(
                sq.toString());

        rc.next();
        String[][] values = new String[rc.getInt(1)][19];

        for (int x = 0; x < values.length; x++) {
            rv.next();
            for (int y = 0; y < 19; y++) {
                values[x][y] = rv.getString(y + 1);
            }
        }

        return values;
    }

    /**
     * selectdrivers
     *
     * @param dep String
     * @param bra String
     * @param sta String
     * @return String[][]
     * @throws Throwable
     */
    public String[][] selectDriversDepartBranch(String dep, String bra,
                                                String sta) throws Throwable {
        StringBuffer cq = new StringBuffer(100);
        cq.append("am_msp_count_drivers_depart_branch"
                  + " '" + dep + "','" + bra + "', '" + sta + "'");
        ResultSet rc = getStatement().executeQuery(
                cq.toString());

        StringBuffer sq = new StringBuffer(100);
        sq.append("am_msp_select_drivers_depart_branch"
                  + " '" + dep + "','" + bra + "', '" + sta + "'");
        ResultSet rv = getStatement().executeQuery(
                sq.toString());

        rc.next();
        String[][] values = new String[rc.getInt(1)][19];

        for (int x = 0; x < values.length; x++) {
            rv.next();
            for (int y = 0; y < 19; y++) {
                values[x][y] = rv.getString(y + 1);
            }
        }

        return values;
    }

    /**
     * updatedrivers
     *
     * @param cn String
     * @param dc String
     * @param dn String
     * @param di String
     * @param de String
     * @param ln String
     * @param fn String
     * @param on String
     * @param db String
     * @param dd String
     * @param ca String
     * @param ds String
     * @param dp String
     * @param df String
     * @param em String
     * @param st String
     * @param pr String
     * @param cu String
     * @return boolean
     * @throws Throwable
     */
    public boolean updateDrivers(
            String cn, String dc, String dn, java.sql.Date di, java.sql.Date de,
            String ln, String fn, String on, String db, String dd, String ca,
            String ds, String dp, String df, String em, String st, String pr,
            String cu) throws Throwable {
        StringBuffer iq = new StringBuffer(100);
        iq.append("am_msp_update_drivers");
        iq.append("'" + cn + "',");
        iq.append("'" + dc + "',");
        iq.append("'" + dn + "',");
        iq.append("'" + di + "',");
        iq.append("'" + de + "',");
        iq.append("'" + ln + "',");
        iq.append("'" + fn + "',");
        iq.append("'" + on + "',");
        iq.append("'" + db + "',");
        iq.append("'" + dd + "',");
        iq.append("'" + ca + "',");
        iq.append("'" + ds + "',");
        iq.append("'" + dp + "',");
        iq.append("'" + df + "',");
        iq.append("'" + em + "',");
        iq.append("'" + st + "',");
        iq.append("'" + pr + "',");
        iq.append("'" + cu + "' ");

        int ret = getStatement().executeUpdate(
                iq.toString());
        if (ret > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * insertdrivers
     *
     * @return boolean
     * @throws Throwable
     * @param dc String
     * @param dn String
     * @param di String
     * @param de String
     * @param ln String
     * @param fn String
     * @param on String
     * @param db String
     * @param dd String
     * @param ca String
     * @param ds String
     * @param dp String
     * @param df String
     * @param em String
     * @param st String
     * @param pr String
     * @param cu String
     */
    public boolean insertDrivers(String dc, String dn, java.sql.Date di,
                                 java.sql.Date de, String ln, String fn,
                                 String on, String db, String dd, String ca,
                                 String ds, String dp, String df, String em,
                                 String st, String pr, String cu) throws
            Throwable {
        StringBuffer iq = new StringBuffer(100);
        iq.append("am_msp_insert_drivers");
        iq.append("'" + dc + "',");
        iq.append("'" + dn + "',");
        iq.append("'" + di + "',");
        iq.append("'" + de + "',");
        iq.append("'" + ln + "',");
        iq.append("'" + fn + "',");
        iq.append("'" + on + "',");
        iq.append("'" + db + "',");
        iq.append("'" + dd + "',");
        iq.append("'" + ca + "',");
        iq.append("'" + ds + "',");
        iq.append("'" + dp + "',");
        iq.append("'" + df + "',");
        iq.append("'" + em + "',");
        iq.append("'" + st + "',");
        iq.append("'" + pr + "',");
        iq.append("'" + cu + "' ");

        if (ai.reqInsertAudit() == true) {
            atg.select(1, "SELECT * FROM AM_AD_DRIVER WHERE driver_Id = (SELECT MIN(driver_Id) FROM AM_AD_DRIVER)");
            atg.captureAuditFields(drivers);
            atg.logAuditTrail(ai);
        }
        return (getStatement().executeUpdate(
                iq.toString()) == -1);
    }
}
