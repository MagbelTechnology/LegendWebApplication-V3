package magma;

import java.sql.ResultSet;

public class ListDisposalsBean extends legend.ConnectionClass {
    public ListDisposalsBean() throws Exception {
        super();
    }

    /**
     * getDisposals
     *
     * @return String[][]
     */
    public String[][] getDisposals() throws Exception {
        ResultSet rst = getStatement().executeQuery(
                "am_msp_select_asset_disposal_list");
        ResultSet crs = getStatement().executeQuery(
                "am_msp_count_asset_disposal_list");
        crs.next();
        int count = crs.getInt(1);

        if (count > 0) {
            String[][] a = new String[count][8];
            int i = 0;
            while (rst.next()) {
                for (int j = 0; j < 8; j++) {
                    a[i][j] = rst.getString(j + 1);
                }
                i++;
            }
            return a;
        }
        return null;
    }
}
