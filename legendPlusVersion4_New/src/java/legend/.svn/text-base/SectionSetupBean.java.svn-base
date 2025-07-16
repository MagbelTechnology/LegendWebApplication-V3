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
public class SectionSetupBean extends ConnectionClass {
    private String[] sections = new String[7];

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


    public SectionSetupBean() throws Throwable {
    }

    public void setSections(String[] sections) {
        if (sections != null) {
            this.sections = sections;
        }
    }

    public String[] getSections() {
        return sections;
    }

    /**
     * selectsections
     *
     * @param con String
     * @return String[][]
     * @throws Throwable
     */
    public String[][] selectSections(String con, String sta) throws Throwable {
        StringBuffer cq = new StringBuffer(100);
        cq.append("am_msp_count_sections"
                  + " '" + con + "','" + sta + "'");
        ResultSet rc = getStatement().executeQuery(
                cq.toString());

        StringBuffer sq = new StringBuffer(100);
        sq.append("am_msp_select_sections"
                  + " '" + con + "','" + sta + "'");
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
     * updatesections
     *
     * @param con String
     * @return boolean
     * @throws Throwable
     */
    public boolean updateSections(String con) throws Throwable {
        StringBuffer iq = new StringBuffer(100);
        iq.append("am_msp_update_sections");
        iq.append("'");
        iq.append(con);
        iq.append("'");

        for (int i = 0; i < 7; i++) {
            switch (i) {
            case -1:
                iq.append(", ");
                iq.append(sections[i]);
                break;
            default:
                iq.append(",'");
                iq.append(sections[i]);
                iq.append("'");
            }
        }

        int ret = getStatement().executeUpdate(iq.toString());
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
    public boolean insertSections() throws Throwable {
        StringBuffer iq = new StringBuffer(100);
        iq.append("am_msp_insert_sections");
        iq.append("'");
        iq.append(sections[0]);
        iq.append("'");

        for (int i = 1; i <= 6; i++) {
            switch (i) {
            case -1:
                iq.append(", ");
                iq.append(sections[i]);
                break;
            default:
                iq.append(",'");
                iq.append(sections[i]);
                iq.append("'");
            }
        }
        if (ai.reqInsertAudit() == true) {
            atg.select(1, "SELECT * FROM AM_AD_SECTION WHERE section_Id = (SELECT MIN(section_Id) FROM AM_AD_SECTION)");
            atg.captureAuditFields(sections);
            atg.logAuditTrail(ai);
        }
        return (getStatement().executeUpdate(
                iq.toString()) == -1);
    }
}
