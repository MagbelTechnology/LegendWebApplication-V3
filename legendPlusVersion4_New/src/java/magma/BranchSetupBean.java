package magma;

import java.sql.ResultSet;

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
public class BranchSetupBean extends ConnectionClass {
    private String[] branches = new String[10];

    public BranchSetupBean() throws Throwable {
    }

    public void setBranches(String[] branches) {
        if (branches != null) {
            this.branches = branches;
        }
    }

    public String[] getBranches() {
        return branches;
    }

    /**
     * selectbranches
     *
     * @param con String
     * @return String[][]
     * @throws Throwable
     */
    public String[][] selectBranches(String con) throws Throwable {
        StringBuffer cq = new StringBuffer(100);
        cq.append("am_msp_count_branches"
                  + " '" + con + "'");
        ResultSet rc = getStatement().executeQuery(
                cq.toString());

        StringBuffer sq = new StringBuffer(100);
        sq.append("am_msp_select_branches"
                  + " '" + con + "'");
        ResultSet rv = getStatement().executeQuery(
                sq.toString());

        rc.next();
        String[][] values = new String[rc.getInt(1)][10];

        for (int x = 0; x < values.length; x++) {
            rv.next();
            for (int y = 0; y < 10; y++) {
                values[x][y] = rv.getString(y + 1);
            }
        }

        return values;
    }

    /**
     * updatebranches
     *
     * @param con String
     * @return boolean
     * @throws Throwable
     */
    public boolean updateBranches(String con) throws Throwable {
        StringBuffer iq = new StringBuffer(100);
        iq.append("am_msp_update_branches");
        iq.append("'");
        iq.append(con);
        iq.append("'");

        for (int i = 0; i < 10; i++) {
            switch (i) {
            case -1:
                iq.append(", ");
                iq.append(branches[i]);
                break;
            default:
                iq.append(",'");
                iq.append(branches[i]);
                iq.append("'");
            }
        }

        return (getStatement().executeUpdate(
                iq.toString()) == -1);
    }

    /**
     * insertbranches
     *
     * @return boolean
     * @throws Throwable
     */
    public boolean insertBranches() throws Throwable {
        StringBuffer iq = new StringBuffer(100);
        iq.append("am_msp_insert_branches");
        iq.append("'");
        iq.append(getIdentity());
        iq.append("'");

        for (int i = 0; i < 10; i++) {
            switch (i) {
            case -1:
                iq.append(", ");
                iq.append(branches[i]);
                break;
            default:
                iq.append(",'");
                iq.append(branches[i]);
                iq.append("'");
            }
        }

        return (getStatement().executeUpdate(
                iq.toString()) == -1);
    }
}
