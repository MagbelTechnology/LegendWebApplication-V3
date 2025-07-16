package legend.purge;

public class PurgeHandler extends legend.ConnectionClass {
    public PurgeHandler() throws Exception {

        System.out.println("[INFO_]-- Using " +
                           this.getClass().getName().toString());
    }

    public java.util.ArrayList getPurgeInfo() {
        java.util.ArrayList _list = new java.util.ArrayList();
        legend.purge.object.PurgeandArchivebean pab = null;
        String query = "SELECT Record_Index,Table_Name,Description"
                       + ",no_of_days,archive"
                       + " FROM am_gb_purgingandarchiving";
        try {
            java.sql.ResultSet rs = getStatement().executeQuery(query);
            while (rs.next()) {
                String index = rs.getString("Record_Index");
                String table = rs.getString("Table_Name");
                String desc = rs.getString("Description");
                int nodays = rs.getInt("no_of_days");
                String archive = rs.getString("archive");

                pab = new legend.purge.object.PurgeandArchivebean(index, table,
                        desc, nodays, archive);

                _list.add(pab);
            }

        } catch (Exception ex) {
            System.out.println("[INFO_]-- Error retriving purging information " +
                               ex.getMessage());
        } finally {
            freeResource();
        }
        return _list;
    }

    public boolean setPurgeInfo(String recindex, int no_days, String archive) {
        boolean done = false;
        String usql = "UPDATE am_gb_purgingandarchiving SET no_of_days = ?"
                      + " ,archive = ? WHERE Record_Index =? ";
        try {
            java.sql.PreparedStatement ps = getConnection().prepareStatement(
                    usql);
            ps.setInt(1, no_days);
            ps.setString(2, archive);
            ps.setString(3, recindex);
            done = (ps.executeUpdate() != -1);
        } catch (Exception ex) {
            System.out.println("[INFO_]-- Error Updating purging information " +
                               ex.getMessage());
        } finally {
            freeResource();
        }
        return done;

    }
}
