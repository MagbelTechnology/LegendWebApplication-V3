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
 * @version 1.0
 */
public class CompanyVendorsBean extends ConnectionClass {
    private String[] vendors = new String[17];

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

    public CompanyVendorsBean() throws Exception {
    }

    public void setVendors(String[] vendors) {
        if (vendors != null) {
            this.vendors = vendors;
        }
    }

    public String[] getVendors() {
        return vendors;
    }

    /**
     * selectvendors
     *
     * @param con String
     * @param sta String
     * @return String[][]
     * @throws Throwable
     */
    public String[][] selectVendors(String con, String sta) throws Throwable {
        StringBuffer cq = new StringBuffer(100);
        cq.append("am_msp_count_vendors"
                  + " '" + con + "','" + sta + "'");
        ResultSet rc = getStatement().executeQuery(
                cq.toString());

        StringBuffer sq = new StringBuffer(100);
        sq.append("am_msp_select_vendors"
                  + " '" + con + "','" + sta + "'");
        ResultSet rv = getStatement().executeQuery(
                sq.toString());

        rc.next();
        String[][] values = new String[rc.getInt(1)][17];

        for (int x = 0; x < values.length; x++) {
            rv.next();
            for (int y = 0; y < 17; y++) {
                values[x][y] = rv.getString(y + 1);
            }
        }

        return values;
    }

    /**
     * updatevendors
     *
     * @param con String
     * @return boolean
     * @throws Throwable
     */
    public boolean updateVendors(String con) throws Throwable {
        StringBuffer iq = new StringBuffer(100);
        iq.append("am_msp_update_vendors");
        iq.append("'");
        iq.append(con);
        iq.append("'");

        for (int i = 0; i < 15; i++) {
            switch (i) {
            case -1:
                iq.append(", ");
                iq.append(vendors[i]);
                break;
            default:
                iq.append(",'");
                iq.append(vendors[i]);
                iq.append("'");
            }
        }

        return (getStatement().executeUpdate(
                iq.toString()) == -1);
    }

    /**
     * insertvendors
     *
     * @return boolean
     * @throws Throwable
     */
    public boolean insertVendors() throws Throwable {
        StringBuffer iq = new StringBuffer(100);
        iq.append("am_msp_insert_vendors");
        iq.append("'");
        iq.append(vendors[0]);
        iq.append("'");

        for (int i = 1; i <= 14; i++) {
            switch (i) {
            case -1:
                iq.append(", ");
                iq.append(vendors[i]);
                break;
            default:
                iq.append(",'");
                iq.append(vendors[i]);
                iq.append("'");
            }
        }
        if (ai.reqInsertAudit() == true) {
            atg.select(1, "SELECT * FROM AM_AD_VENDOR WHERE vendor_Id = (SELECT MIN(vendor_id) FROM AM_AD_VENDOR)");
            atg.captureAuditFields(vendors);
            atg.logAuditTrail(ai);
        }
        return (getStatement().executeUpdate(
                iq.toString()) == -1);
    }

    public boolean requireAccttype() throws Throwable {
        ResultSet rs = getStatement().executeQuery(
                "select req_accttype from am_ad_legacy_sys_config");
        rs.next();

        return rs.getString(1).equalsIgnoreCase("Y");
    }

    public String[][] selectAccttypes() throws Throwable {
        StringBuffer cq = new StringBuffer(100);
        cq.append("select count(*) from am_ad_account_type");
        ResultSet rc = getStatement().executeQuery(
                cq.toString());

        StringBuffer sq = new StringBuffer(100);
        sq.append("select * from am_ad_account_type");
        ResultSet rv = getStatement().executeQuery(
                sq.toString());

        rc.next();
        String[][] values = new String[rc.getInt(1)][2];

        for (int x = 0; x < values.length; x++) {
            rv.next();
            for (int y = 0; y < 2; y++) {
                values[x][y] = rv.getString(y + 1);
            }
        }

        return values;
    }
}
