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
public class CategoryClassesBean extends ConnectionClass {
    private String[] classes = new String[5];

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


    public CategoryClassesBean() throws Exception {
    }

    public void setClasses(String[] classes) {
        if (classes != null) {
            this.classes = classes;
        }
    }

    public String[] getClasses() {
        return classes;
    }

    /**
     * selectclasses
     *
     * @param con String
     * @return String[][]
     * @throws Throwable
     */
    public String[][] selectClasses(String con, String sta) throws Throwable {
        StringBuffer cq = new StringBuffer(100);
        cq.append("am_msp_count_cclasses"
                  + " '" + con + "','" + sta + "'");
        ResultSet rc = getStatement().executeQuery(
                cq.toString());

        StringBuffer sq = new StringBuffer(100);
        sq.append("am_msp_select_cclasses"
                  + " '" + con + "','" + sta + "'");
        ResultSet rv = getStatement().executeQuery(
                sq.toString());

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
     * updateclasses
     *
     * @param con String
     * @return boolean
     * @throws Throwable
     */
    public boolean updateClasses(String con) throws Throwable {
        StringBuffer iq = new StringBuffer(100);
        iq.append("am_msp_update_cclasses");
        iq.append("'");
        iq.append(con);
        iq.append("'");

        for (int i = 0; i < 4; i++) {
            switch (i) {
            case -1:
                iq.append(", ");
                iq.append(classes[i]);
                break;
            default:
                iq.append(",'");
                iq.append(classes[i]);
                iq.append("'");
            }
        }

        return (getStatement().executeUpdate(
                iq.toString()) == -1);
    }

    /**
     * insertclasses
     *
     * @return boolean
     * @throws Throwable
     */
    public boolean insertClasses() throws Throwable {
        StringBuffer iq = new StringBuffer(100);
        iq.append("am_msp_insert_cclasses");
        iq.append("'");
        iq.append(classes[0]);
        iq.append("'");

        for (int i = 1; i <= 3; i++) {
            switch (i) {
            case -1:
                iq.append(", ");
                iq.append(classes[i]);
                break;
            default:
                iq.append(",'");
                iq.append(classes[i]);
                iq.append("'");
            }
        }
        if (ai.reqInsertAudit() == true) {
            String rowid = ai.getRowId();
            int prevRow = Integer.parseInt(rowid) - 1;
            atg.select(1, "SELECT * FROM AM_GB_CLASS WHERE class_Id = (SELECT MIN(class_Id) FROM AM_GB_CLASS)");
            atg.captureAuditFields(classes);
            atg.logAuditTrail(ai);
        }
        return (getStatement().executeUpdate(
                iq.toString()) == -1);
    }
}
