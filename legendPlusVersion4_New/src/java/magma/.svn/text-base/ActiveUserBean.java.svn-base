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
public class ActiveUserBean extends ConnectionClass {
    private String[] ausers = new String[5];

    public ActiveUserBean() throws Throwable {
    }

    public void setAusers(String[] ausers) {
        this.ausers = ausers;
    }

    public String[] getAusers() {
        return ausers;
    }

    /**
     * selectsections
     *
     * @param con String
     * @return String[][]
     * @throws Throwable
     */
    public String[][] selectAusers(String con) throws Throwable {
        StringBuffer cq = new StringBuffer(100);
        cq.append("am_msp_count_ausers"
                  + " '" + con + "'");
        ResultSet rc = getStatement().executeQuery(
                cq.toString());

        StringBuffer sq = new StringBuffer(100);
        sq.append("am_msp_select_ausers"
                  + " '" + con + "'");
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
     * updatesections
     *
     * @param con String
     * @return boolean
     * @throws Throwable
     */
    public boolean logoutUsers(String con) throws Throwable {
        StringBuffer iq = new StringBuffer(100);
        iq.append("am_msp_logout_users");
        iq.append("'");
        iq.append(con);
        iq.append("'");

        return (getStatement().executeUpdate(
                iq.toString()) == -1);
    }
}
