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
public class DepartSetupBean extends ConnectionClass {
    private String[] departs = new String[7];

    private AuditInfo ai = new AuditInfo();
    private AuditTrailGen atg = new AuditTrailGen();


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


    public DepartSetupBean() throws Throwable {
    }

    public void setDeparts(String[] departs) {
        if (departs != null) {
            this.departs = departs;
        }
    }

    public String[] getDeparts() {
        return departs;
    }

    /**
     * selectdeparts
     *
     * @param con String
     * @param sta String
     * @return String[][]
     * @throws Throwable
     */
    public String[][] selectDeparts(String con, String sta) throws Throwable {
        StringBuffer cq = new StringBuffer(100);
        cq.append("am_msp_count_departs"
                  + " " + con + ",'" + sta + "'");
        ResultSet rc = getStatement().executeQuery(
                cq.toString());

        StringBuffer sq = new StringBuffer(100);
        sq.append("am_msp_select_departs"
                  + " " + con + ",'" + sta + "'");
        ResultSet rv = getStatement().executeQuery(
                sq.toString());

        rc.next();
        String[][] values = new String[rc.getInt(1)][7];

        for (int x = 0; x < values.length; x++) {
            rv.next();
            for (int y = 0; y < 7; y++) {
                values[x][y] = rv.getString(y + 1);
            }
        }

        return values;
    }

    /**
     * updatedeparts
     *
     * @param con String
     * @return boolean
     * @throws Throwable
     */
    public boolean updateDeparts(String con) throws Throwable {
        StringBuffer iq = new StringBuffer(100);
        iq.append("am_msp_update_departs");
        iq.append("'");
        iq.append(con);
        iq.append("'");

        for (int i = 0; i < 7; i++) {
            switch (i) {
            case -1:
                iq.append(", ");
                iq.append(departs[i]);
                break;
            default:
                iq.append(",'");
                iq.append(departs[i]);
                iq.append("'");
            }
        }

        int ret = getStatement().executeUpdate(
                iq.toString());
        if (ret > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * insertdeparts
     *
     * @return boolean
     * @throws Throwable
     */
    public boolean insertDeparts() throws Throwable {
        StringBuffer iq = new StringBuffer(100);
        iq.append("am_msp_insert_departs");
        iq.append("'");
        iq.append(departs[0]);
        iq.append("'");

        for (int i = 1; i <= 6; i++) {
            switch (i) {
            case -1:
                iq.append(", ");
                iq.append(departs[i]);
                break;
            default:
                iq.append(",'");
                iq.append(departs[i]);
                iq.append("'");
            }
        }
        if (ai.reqInsertAudit() == true) {
            atg.select(1, "SELECT * FROM AM_AD_DEPARTMENT WHERE dept_Id = (SELECT MIN(dept_Id) FROM AM_AD_DEPARTMENT)");
            atg.captureAuditFields(departs);
            atg.logAuditTrail(ai);
        }
        return (getStatement().executeUpdate(
                iq.toString()) == -1);
    }
}
