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
public class ClassSetupBean extends ConnectionClass {
    private String[] classes = new String[8];
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


    public ClassSetupBean() throws Exception {
    }

    public void setClasses(String[] classes) {
        this.classes = classes;
    }

    public String[] getClasses() {
        return classes;
    }

    /**
     * selectAutoIdentity
     *
     * @return String[]
     * @throws Exception
     * @param cls String
     */
    public boolean isClassSupervisor(String cls) throws Exception {
        StringBuffer sq = new StringBuffer(100);
        sq.append("select is_supervisor from am_gb_class " +
                  " where class_id = " + cls + " and class_status = 'A'");
        ResultSet rv = getStatement().executeQuery(
                sq.toString());

        rv.next();

        return (rv.getString(1).equals("Y"));
    }

    /**
     * selectAutoIdentity
     *
     * @return String[]
     * @throws Exception
     * @param cls String
     */
    public boolean isFleetAdmin(String cls) throws Exception {
        StringBuffer sq = new StringBuffer(100);
        sq.append("select fleet_admin from am_gb_class " +
                  " where class_id = " + cls + " and class_status = 'A'");
        ResultSet rv = getStatement().executeQuery(
                sq.toString());

        rv.next();

        return (rv.getString(1).equals("Y"));
    }

    /**
     * selectsections
     *
     * @param con String
     * @param sta String
     * @return String[][]
     * @throws Throwable
     */
    public String[][] selectClasses(String con, String sta) throws Throwable {
        StringBuffer cq = new StringBuffer(100);
        StringBuffer sq = new StringBuffer(100);
        /*cq.append("am_msp_count_classes"
                  + " '" + con + "','" + sta + "'");*/
        /*sq.append("am_msp_select_classes"
                  + " '" + con + "','" + sta + "'");*/
        if (con.equals("0")) {
            cq.append("select count(*) from am_gb_class " +
                      " where class_status = '" + sta + "'");
            sq.append("select * from am_gb_class " +
                      " where class_status = '" + sta + "'");
        } else if (!con.equals("0")) {
            cq.append("select count(*) from am_gb_class " +
                      " where class_id = " + con);
            sq.append("select * from am_gb_class " +
                      " where class_id = " + con);

        } else {

        }
        ResultSet rc = getStatement().executeQuery(
                cq.toString());

        ResultSet rv = getStatement().executeQuery(
                sq.toString());

        rc.next();
        String[][] values = new String[rc.getInt(1)][8];

        for (int x = 0; x < values.length; x++) {
            rv.next();
            for (int y = 0; y < 8; y++) {
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
    public boolean updateClasses(String con) throws Throwable {
        String iq = "update am_gb_class set " +
                    "class_name = '" + classes[0] + "'," +
                    "class_desc = '" + classes[1] + "'," +
                    "is_supervisor = '" + classes[2] + "'," +
                    "fleet_admin = '" + classes[3] + "'," +
                    "class_status = '" + classes[4] + "'" +
                    " where " +
                    "class_id =" + con;

        /*StringBuffer iq = new StringBuffer(100);
                iq.append("am_msp_update_classes");
                iq.append("'");
                iq.append(con);
                iq.append("'");

                for (int i = 0; i < 6; i++) {
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
                }*/
        int ret = getStatement().executeUpdate(
                iq.toString());
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
    public boolean insertClasses() throws Throwable {

        String iq = "insert into am_gb_class(" +
                    "class_name, class_desc,is_supervisor," +
                    "fleet_admin,class_status,user_id," +
                    "create_date )" +
                    " values('" +
                    classes[0] + "','" +
                    classes[1] + "','" +
                    classes[2] + "','" +
                    classes[3] + "','" +
                    classes[4] + "'," +
                    classes[5] + "," +
                    "GetDate())";
        if (ai.reqInsertAudit() == true) {
            atg.select(1, "SELECT * FROM AM_GB_CLASS WHERE class_Id = (SELECT MIN(class_Id) FROM AM_GB_CLASS)");
            atg.captureAuditFields(classes);
            atg.logAuditTrail(ai);
        }

        return (getStatement().executeUpdate(
                iq.toString()) != -1);
    }

    /**
     * getInsertedId
     *
     * @return String
     * @throws Throwable
     * @param name String
     */
    public String getClassID(String name) throws Throwable {
        StringBuffer sq = new StringBuffer(100);
        sq.append("select * from am_gb_class where class_name = '" + name + "'");
        ResultSet rv = getStatement().executeQuery(sq.toString());

        rv.next();
        return rv.getString(1);
    }

    public boolean classExist(String name) throws Throwable {
        StringBuffer sq = new StringBuffer(100);
        sq.append("select * from am_gb_class where class_name = '" + name + "'");
        ResultSet rv = getStatement().executeQuery(sq.toString());

        return (rv.next());

    }

}
