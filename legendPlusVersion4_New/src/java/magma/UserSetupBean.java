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
public class UserSetupBean extends ConnectionClass {
    private String[] users = new String[13];

    public UserSetupBean() throws Exception {
    }

    public void setUsers(String[] users) {
        this.users = users;
    }

    public String[] getUsers() {
        return users;
    }

    /**
     * selectsections
     *
     * @param con String
     * @return String[][]
     * @throws Throwable
     */
    public String[][] selectUsers(String con) throws Throwable {
        StringBuffer cq = new StringBuffer(100);
        cq.append("am_msp_count_users"
                  + " '" + con + "'");
        ResultSet rc = getStatement().executeQuery(
                cq.toString());

        StringBuffer sq = new StringBuffer(100);
        sq.append("am_msp_select_users"
                  + " '" + con + "'");
        ResultSet rv = getStatement().executeQuery(
                sq.toString());

        rc.next();
        String[][] values = new String[rc.getInt(1)][13];

        for (int x = 0; x < values.length; x++) {
            rv.next();
            for (int y = 0; y < 13; y++) {
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
    public boolean updateUsers(String con) throws Throwable {
        StringBuffer iq = new StringBuffer(100);
        iq.append("am_msp_update_users");
        iq.append("'");
        iq.append(con);
        iq.append("'");

        for (int i = 0; i < 10; i++) {
            switch (i) {
            case -1:
                iq.append(", ");
                iq.append(users[i]);
                break;
            default:
                iq.append(",'");
                iq.append(users[i]);
                iq.append("' ");
            }
        }

        iq.append(",'");
        iq.append(getEncrypted(users[10]));
        iq.append("' ");

        return (getStatement().executeUpdate(
                iq.toString()) == -1);
    }

    /**
     * insertsections
     *
     * @return boolean
     * @throws Throwable
     */
    public boolean insertUsers() throws Throwable {
        StringBuffer iq = new StringBuffer(100);
        iq.append("am_msp_insert_users");
        iq.append("'");
        iq.append(getIdentity());
        iq.append("'");

        for (int i = 0; i < 10; i++) {
            switch (i) {
            case -1:
                iq.append(", ");
                iq.append(users[i]);
                break;
            default:
                iq.append(",'");
                iq.append(users[i]);
                iq.append("'");
            }
        }

        iq.append(",'");
        iq.append(getEncrypted(users[10]));
        iq.append("' ");

        return (getStatement().executeUpdate(
                iq.toString()) == -1);
    }
}
