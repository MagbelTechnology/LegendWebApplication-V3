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
public class CategorySetupBean extends ConnectionClass {
    private String[] categories = new String[18];

    public CategorySetupBean() throws Exception {
    }

    public void setCategories(String[] categories) {
        if (categories != null) {
            this.categories = categories;
        }
    }

    public String[] getCategories() {
        return categories;
    }

    /**
     * selectcategories
     *
     * @param con String
     * @return String[][]
     * @throws Throwable
     */
    public String[][] selectCategories(String con) throws Throwable {
        StringBuffer cq = new StringBuffer(100);
        cq.append("am_msp_count_categories"
                  + " '" + con + "'");
        ResultSet rc = getStatement().executeQuery(
                cq.toString());

        StringBuffer sq = new StringBuffer(100);
        sq.append("am_msp_select_categories"
                  + " '" + con + "'");
        ResultSet rv = getStatement().executeQuery(
                sq.toString());

        rc.next();
        String[][] values = new String[rc.getInt(1)][18];

        for (int x = 0; x < values.length; x++) {
            rv.next();
            for (int y = 0; y < 18; y++) {
                values[x][y] = rv.getString(y + 1);
            }
        }

        return values;
    }

    /**
     * updatecategories
     *
     * @param con String
     * @return boolean
     * @throws Throwable
     */
    public boolean updateCategories(String con) throws Throwable {
        StringBuffer iq = new StringBuffer(100);
        iq.append("am_msp_update_categories");
        iq.append("'");
        iq.append(con);
        iq.append("'");

        for (int i = 0; i < 16; i++) {
            switch (i) {
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                iq.append(", ");
                iq.append(categories[i]);
                break;
            default:
                iq.append(",'");
                iq.append(categories[i]);
                iq.append("'");
            }
        }

        return (getStatement().executeUpdate(
                iq.toString()) == -1);
    }

    /**
     * insertcategories
     *
     * @return boolean
     * @throws Throwable
     */
    public boolean insertCategories() throws Throwable {
        StringBuffer iq = new StringBuffer(100);
        iq.append("am_msp_insert_categories");
        iq.append("'");
        iq.append(getIdentity());
        iq.append("'");

        for (int i = 0; i < 16; i++) {
            switch (i) {
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                iq.append(", ");
                iq.append(categories[i]);
                break;
            default:
                iq.append(",'");
                iq.append(categories[i]);
                iq.append("'");
            }
        }

        return (getStatement().executeUpdate(
                iq.toString()) == -1);
    }

}
