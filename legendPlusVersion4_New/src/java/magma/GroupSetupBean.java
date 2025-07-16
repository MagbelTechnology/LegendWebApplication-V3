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
public class GroupSetupBean extends ConnectionClass {
    private String[] groups = new String[7];

    public GroupSetupBean() throws Throwable {
    }

    public void setGroups(String[] groups) {
        if (groups != null) {
            this.groups = groups;
        }
    }

    public String[] getGroups() {
        return groups;
    }

    /**
     * selectgroups
     *
     * @param con String
     * @return String[][]
     * @throws Throwable
     */
    public String[][] selectGroups(String con) throws Throwable {
        StringBuffer cq = new StringBuffer(100);
        cq.append("am_msp_count_groups"
                  + " '" + con + "'");
        ResultSet rc = getStatement().executeQuery(
                cq.toString());

        StringBuffer sq = new StringBuffer(100);
        sq.append("am_msp_select_groups"
                  + " '" + con + "'");
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
     * updategroups
     *
     * @param con String
     * @return boolean
     * @throws Throwable
     */
    public boolean updateGroups(String con) throws Throwable {
        StringBuffer iq = new StringBuffer(100);
        iq.append("am_msp_update_groups");
        iq.append("'");
        iq.append(con);
        iq.append("'");

        for (int i = 0; i < 7; i++) {
            switch (i) {
            case -1:
                iq.append(", ");
                iq.append(groups[i]);
                break;
            default:
                iq.append(",'");
                iq.append(groups[i]);
                iq.append("'");
            }
        }

        return (getStatement().executeUpdate(
                iq.toString()) == -1);
    }

    /**
     * insertgroups
     *
     * @return boolean
     * @throws Throwable
     */
    public boolean insertGroups() throws Throwable {
        StringBuffer iq = new StringBuffer(100);
        iq.append("am_msp_insert_groups");
        iq.append("'");
        iq.append(getIdentity());
        iq.append("'");

        for (int i = 0; i < 7; i++) {
            switch (i) {
            case -1:
                iq.append(", ");
                iq.append(groups[i]);
                break;
            default:
                iq.append(",'");
                iq.append(groups[i]);
                iq.append("'");
            }
        }

        return (getStatement().executeUpdate(
                iq.toString()) == -1);
    }
}
