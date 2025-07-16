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
public class StateSetupBean extends ConnectionClass {
    private String[] states = new String[5];

    public StateSetupBean() throws Throwable {
    }

    public void setStates(String[] states) {
        if (states != null) {
            this.states = states;
        }
    }

    public String[] getStates() {
        return states;
    }

    /**
     * selectStates
     *
     * @param con String
     * @return String[][]
     * @throws Throwable
     */
    public String[][] selectStates(String con) throws Throwable {
        StringBuffer cq = new StringBuffer(100);
        cq.append("am_msp_count_states"
                  + " '" + con + "'");
        ResultSet rc = getStatement().executeQuery(
                cq.toString());

        StringBuffer sq = new StringBuffer(100);
        sq.append("am_msp_select_states"
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
     * updateStates
     *
     * @param con String
     * @return boolean
     * @throws Throwable
     */
    public boolean updateStates(String con) throws Throwable {
        StringBuffer iq = new StringBuffer(100);
        iq.append("am_msp_update_states");
        iq.append("'");
        iq.append(con);
        iq.append("'");

        for (int i = 0; i < 4; i++) {
            switch (i) {
            case -1:
                iq.append(", ");
                iq.append(states[i]);
                break;
            default:
                iq.append(",'");
                iq.append(states[i]);
                iq.append("'");
            }
        }

        return (getStatement().executeUpdate(
                iq.toString()) == -1);
    }

    /**
     * insertStates
     *
     * @return boolean
     * @throws Throwable
     */
    public boolean insertStates() throws Throwable {
        StringBuffer iq = new StringBuffer(100);
        iq.append("am_msp_insert_states");
        iq.append("'");
        iq.append(getIdentity());
        iq.append("'");

        for (int i = 0; i < 4; i++) {
            switch (i) {
            case -1:
                iq.append(", ");
                iq.append(states[i]);
                break;
            default:
                iq.append(",'");
                iq.append(states[i]);
                iq.append("'");
            }
        }

        return (getStatement().executeUpdate(
                iq.toString()) == -1);
    }
}
