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
public class SectionSetupBean extends ConnectionClass {
    private String[] sections = new String[5];

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
    public String[][] selectSections(String con) throws Throwable {
        StringBuffer cq = new StringBuffer(100);
        cq.append("am_msp_count_sections"
                  + " '" + con + "'");
        ResultSet rc = getStatement().executeQuery(
                cq.toString());

        StringBuffer sq = new StringBuffer(100);
        sq.append("am_msp_select_sections"
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
    public boolean updateSections(String con) throws Throwable {
        StringBuffer iq = new StringBuffer(100);
        iq.append("am_msp_update_sections");
        iq.append("'");
        iq.append(con);
        iq.append("'");

        for (int i = 0; i < 5; i++) {
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

        return (getStatement().executeUpdate(
                iq.toString()) == -1);
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
        iq.append(getIdentity());
        iq.append("'");

        for (int i = 0; i < 5; i++) {
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

        return (getStatement().executeUpdate(
                iq.toString()) == -1);
    }
}
