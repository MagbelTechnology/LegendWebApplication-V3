package magma;

import java.sql.ResultSet;

public class ListRevalueBean extends legend.ConnectionClass {
    public ListRevalueBean() throws Exception {
        super();
    }

    /**
     * getRevalueList
     *
     * @return String[][]
     */
    public String[][] getRevalueList() throws Exception {
        ResultSet rst = getStatement().executeQuery(
                "am_msp_select_asset_revalue_list");
        ResultSet crs = getStatement().executeQuery(
                "am_msp_count_asset_revalue_list");
        crs.next();
        int count = crs.getInt(1);

        if (count > 0) {
            String[][] a = new String[count][6];
            int i = 0;
            while (rst.next()) {
                a[i][0] = rst.getString(1);
                a[i][1] = rst.getString(2);
                a[i][2] = rst.getString(3);
                a[i][3] = rst.getString(4);
                a[i][4] = rst.getString(5);
                a[i][5] = rst.getString(6);
                i++;
            }
            return a;
        }

        return null;
    }
}
