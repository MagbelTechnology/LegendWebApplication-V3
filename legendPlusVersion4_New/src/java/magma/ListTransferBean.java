package magma;

import java.sql.ResultSet;

public class ListTransferBean extends legend.ConnectionClass {
    public ListTransferBean() throws Exception {
        super();
    }

    /**
     * getTransferBean
     *
     * @return String[][]
     */
    public String[][] getTransferBean() throws Exception {
        ResultSet crs = getStatement().executeQuery(
                "am_msp_count_asset_transfer_list");
        crs.next();
        int count = crs.getInt(1);
        if (count > 0) {
            ResultSet rst = getStatement().executeQuery(
                    "am_msp_select_asset_transfer_list");
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
