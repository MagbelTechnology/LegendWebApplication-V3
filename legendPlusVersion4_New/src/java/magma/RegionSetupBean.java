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
public class RegionSetupBean extends ConnectionClass {
    private String[] regions = new String[7];

    public RegionSetupBean() throws Throwable {
    }

    public void setRegions(String[] regions) {
        if (regions != null) {
            this.regions = regions;
        }
    }

    public String[] getRegions() {
        return regions;
    }

    /**
     * selectregions
     *
     * @param con String
     * @return String[][]
     * @throws Throwable
     */
    public String[][] selectRegions(String con) throws Throwable {
        StringBuffer cq = new StringBuffer(100);
        cq.append("am_msp_count_regions"
                  + " '" + con + "'");
        ResultSet rc = getStatement().executeQuery(
                cq.toString());

        StringBuffer sq = new StringBuffer(100);
        sq.append("am_msp_select_regions"
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
     * updateregions
     *
     * @param con String
     * @return boolean
     * @throws Throwable
     */
    public boolean updateRegions(String con) throws Throwable {
        StringBuffer iq = new StringBuffer(100);
        iq.append("am_msp_update_regions");
        iq.append("'");
        iq.append(con);
        iq.append("'");

        for (int i = 0; i < 7; i++) {
            switch (i) {
            case -1:
                iq.append(", ");
                iq.append(regions[i]);
                break;
            default:
                iq.append(",'");
                iq.append(regions[i]);
                iq.append("'");
            }
        }

        return (getStatement().executeUpdate(
                iq.toString()) == -1);
    }

    /**
     * insertregions
     *
     * @return boolean
     * @throws Throwable
     */
    public boolean insertRegions() throws Throwable {
        StringBuffer iq = new StringBuffer(100);
        iq.append("am_msp_insert_regions");
        iq.append("'");
        iq.append(getIdentity());
        iq.append("'");

        for (int i = 0; i < 7; i++) {
            switch (i) {
            case -1:
                iq.append(", ");
                iq.append(regions[i]);
                break;
            default:
                iq.append(",'");
                iq.append(regions[i]);
                iq.append("'");
            }
        }

        return (getStatement().executeUpdate(
                iq.toString()) == -1);
    }
}
