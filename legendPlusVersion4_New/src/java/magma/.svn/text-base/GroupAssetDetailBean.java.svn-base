package magma;

public class GroupAssetDetailBean extends legend.ConnectionClass {
    public GroupAssetDetailBean() throws Exception {
        super();
    }

    /**
     * getGroupAssets
     *
     * @return String[][]
     */
    public String[][] getGroupAssets() throws Exception {
        java.sql.ResultSet crs = getStatement().executeQuery(
                "am_msp_count_group_asset");
        crs.next();
        int c = crs.getInt(1);

        java.sql.ResultSet rst = getStatement().executeQuery(
                "am_msp_select_group_asset");

        String[][] a = new String[c][40];

        int i = 0;
        while (rst.next()) {
            for (int j = 0; j < 40; j++) {
                a[i][j] = rst.getString(j + 1);
            }
            i++;
        }

        return a;
    }

}
