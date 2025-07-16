package magma;

import java.sql.ResultSet;

public class ListReclassBean extends legend.ConnectionClass {
    public ListReclassBean() throws Exception {
        super();
    }

    /**
     * getReclassList
     *
     * @return String[][]
     */
    public String[][] getReclassList() throws Exception {
        ResultSet rst = getStatement().executeQuery(
                "am_msp_select_asset_reclassification_list");
        ResultSet crs = getStatement().executeQuery(
                "am_msp_count_asset_reclassification_list");

        crs.next();
        int count = crs.getInt(1);

        if (count > 0) {
            String[][] a = new String[count][6];
            int i = 0;
            while (rst.next()) {
                for (int j = 0; j < 6; j++) {
                    a[i][j] = rst.getString(j + 1);
                }
                i++;
            }
            return a;
        }
        return null;
    }
}
