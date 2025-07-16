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
public class DepartSetupBean extends ConnectionClass {
    private String[] departs = new String[6];

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
     * @return String[][]
     * @throws Throwable
     */
    public String[][] selectDeparts(String con) throws Throwable {
        StringBuffer cq = new StringBuffer(100);
        cq.append("am_msp_count_departs"
                  + " '" + con + "'");
        ResultSet rc = getStatement().executeQuery(
                cq.toString());

        StringBuffer sq = new StringBuffer(100);
        sq.append("am_msp_select_departs"
                  + " '" + con + "'");
        ResultSet rv = getStatement().executeQuery(
                sq.toString());

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

        for (int i = 0; i < 6; i++) {
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

        return (getStatement().executeUpdate(
                iq.toString()) == -1);
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
        iq.append(getIdentity());
        iq.append("'");

        for (int i = 0; i < 6; i++) {
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

        return (getStatement().executeUpdate(
                iq.toString()) == -1);
    }
}
