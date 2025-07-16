package legend;

import java.sql.*;
import magma.net.dao.MagmaDBConnection;
import audit.*;

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

  <pre>
    Modification:By Kareem Wasiu Remilekun.
    Reason : To Change All The Store Procedure to
       approprite Java SQL Statement,avoid
    duplicate data entry and
       free the resouce from exeeding the
     connection pool thereby shutting down
     the application
    Date	:Thursday 26th October,2006
 </pre>
 * @version 1.0
 */

public class FuelTypesBean extends ConnectionClass {

    private String[] fuels = new String[9];
    private String[] byFuelCode = new String[9];

    private AuditInfo ai = new AuditInfo();
    private AuditTrailGen atg = new AuditTrailGen();

    public final void setAuditInfo(AuditInfo AI) {
        this.ai = AI;
    }

    public final void setAuditInfo(String TableName, String BranchCode,
                                   int LoginId, String RowId, boolean

                                   ReqInsertAudit) {
        ai.setTableName(TableName);
        ai.setBranchCode(BranchCode);
        ai.setLoginId(LoginId);
        ai.setRowId(RowId);
        ai.setReqInsertAudit(ReqInsertAudit);
    }

    public final AuditInfo getAuditInfo() {
        return this.ai;
    }


    public FuelTypesBean() throws Exception {
    }

    public void setFuels(String[] fuels) {
        if (fuels != null) {
            this.fuels = fuels;
        }
    }

    public String[] getFuels() {
        return fuels;
    }

    public void setByFuelCode(String[] byFuelCode) {
        if (byFuelCode != null) {
            this.byFuelCode = byFuelCode;
        }
    }

    public String[] getByFuelCode() {
        return byFuelCode;
    }


    public String[][] selectFuels(String fuelId, String status) throws
            Throwable {

        Connection cnn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        MagmaDBConnection dbConn = new MagmaDBConnection();

        String param = "";
        String count = "";
        String select = "";

        if (fuelId.equalsIgnoreCase("0")) {

            count = "SELECT COUNT(*) FROM AM_AD_FUELTYPE " +
                    "WHERE FUEL_STATUS = ?";

            select = "SELECT * FROM AM_AD_FUELTYPE WHERE FUEL_STATUS = ? " +
                     " ORDER BY FUEL_DESC ASC";
            param = status;

        } else if (!fuelId.equalsIgnoreCase("0")) {

            count = "SELECT COUNT(*) FROM AM_AD_FUELTYPE " +
                    "WHERE FUEL_ID = ?";
            select = "SELECT * FROM AM_AD_FUELTYPE WHERE FUEL_ID = ?";
            param = fuelId;
        }

        else {}
        String[][] values = null;
        int j = 0;

        try {
            cnn = dbConn.getConnection("fixedasset");
            ps = cnn.prepareStatement(count);
            ps.setString(1, param);
            rs = ps.executeQuery();

            while (rs.next()) {
                j = rs.getInt(1);
            }

            ps = cnn.prepareStatement(select);
            ps.setString(1, param);
            rs = ps.executeQuery();

            values = new String[j][9];

            for (int x = 0; x < values.length; x++) {
                rs.next();
                for (int y = 0; y < 9; y++) {
                    values[x][y] = rs.getString(y + 1);
                }
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching fuels->" + e.getMessage());
        } finally {
            dbConn.closeConnection(cnn, ps, rs);
        }

        return values;
    }

    public String[][] selectByFuelCode(String fuelCode) throws Throwable {

        Connection cnn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        MagmaDBConnection dbConn = new MagmaDBConnection();

        String count = "";
        String select = "";

        count = "SELECT COUNT(*) FROM AM_AD_FUELTYPE " +
                "WHERE FUEL_CODE = ?";
        select = "SELECT * FROM AM_AD_FUELTYPE WHERE FUEL_CODE = ?";

        String[][] values = null;
        int j = 0;

        try {
            cnn = dbConn.getConnection("fixedasset");

            ps = cnn.prepareStatement(count);
            ps.setString(1, fuelCode);
            rs = ps.executeQuery();

            while (rs.next()) {
                j = rs.getInt(1);
            }

            ps = cnn.prepareStatement(select);
            ps.setString(1, fuelCode);

            rs = ps.executeQuery();

            values = new String[j][9];

            for (int x = 0; x < values.length; x++) {
                rs.next();
                for (int y = 0; y < 9; y++) {
                    values[x][y] = rs.getString(y + 1);
                }
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching fuels code->" +
                               e.getMessage());

        } finally {
            dbConn.closeConnection(cnn, ps, rs);
        }

        return values;
    }


    public boolean isUniqueCode(String fuelCode) throws Throwable {

        String query = "SELECT FUEl_CODE FROM AM_AD_FUELTYPE " +
                       "WHERE FUEL_CODE = ? ";
        Connection cnn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean confirm = false;
        MagmaDBConnection dbConn = new MagmaDBConnection();

        try {
            cnn = dbConn.getConnection("fixedasset");
            ps = cnn.prepareStatement(query);

            ps.setString(1, fuelCode);

            rs = ps.executeQuery();
            while (rs.next()) {
                confirm = true;
            }
        } catch (Exception e) {
            System.out.println("INFO:Error checking for duplicate code->" +
                               e.getMessage());

        } finally {
            dbConn.closeConnection(cnn, ps, rs);
        }

        return confirm;
    }

    public boolean updateFuels(String fuelId) throws Throwable {

        String query = "UPDATE AM_AD_FUELTYPE SET FUEL_CODE = ?,FUEL_DESC = ?," +
                       "FUEL_VOLUME = ?,FUEL_PRICE = ?,ACCOUNT_TYPE = ?,SUSPENSE_ACCOUNT = ?," +
                       "FUEL_STATUS = ?,USER_ID = ? WHERE FUEL_ID = ?";

        Connection con = null;
        PreparedStatement ps = null;
        int affectedRow = 0;

        MagmaDBConnection dbConn = new MagmaDBConnection();

        try {
            con = dbConn.getConnection("fixedasset");
            ps = con.prepareStatement(query);

            for (int i = 0; i < 8; i++) {
                ps.setString(i + 1, fuels[i]);
            }
            ps.setInt(9, Integer.parseInt(fuelId));

            affectedRow = ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("INFO:Error  updating Fuels->" + e.getMessage());

        } finally {
            dbConn.closeConnection(con, ps);
        }
        if (affectedRow > 0) {
            return true;
        } else {
            return false;
        }
    }


    public boolean insertFuels() throws Throwable {

        String query =
                "INSERT INTO AM_AD_FUELTYPE(FUEL_CODE,FUEL_DESC,FUEL_VOLUME," +
                "FUEL_PRICE,ACCOUNT_TYPE,SUSPENSE_ACCOUNT,FUEL_STATUS,USER_ID," +
                "CREATE_DATE) VALUES (?,?,?,?,?,?,?,?,getDate())";

        Connection con = null;
        PreparedStatement ps = null;
        int affectedRow = 0;

        MagmaDBConnection dbConn = new MagmaDBConnection();

        try {
            con = dbConn.getConnection("fixedasset");
            ps = con.prepareStatement(query);
            ps.setString(1, fuels[0]);
            for (int i = 1; i <= 7; i++) {
                ps.setString(i + 1, fuels[i]);
            }

            if (ai.reqInsertAudit() == true) {
                atg.select(1, "SELECT * FROM AM_AD_FUELTYPE WHERE fuel_Id = (SELECT MIN(fuel_Id) FROM AM_AD_FUEL)");
                atg.captureAuditFields(fuels);
                atg.logAuditTrail(ai);
            }
            affectedRow = ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("INFO:Error  inserting Fuels->" + e.getMessage());
        } finally {
            dbConn.closeConnection(con, ps);
        }
        if (affectedRow > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * selectreasons
     *
     * @param con String
     * @return String[][]
     * @throws Throwable
     */
    /* public String[][] selectReasons(String reasonId, String status) throws Throwable {
       StringBuffer cq = new StringBuffer(100);
     cq.append("am_msp_count_disposalReasons"
               +" '"+con+"','"+sta+"'");
     ResultSet rc = getStatement().executeQuery(
       cq.toString());

     StringBuffer sq = new StringBuffer(100);
     sq.append("am_msp_select_disposalReasons"
               +" '"+con+"','"+sta+"'");
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
     */

    /**
     * updatefuels
     *
     * @param con String
     * @return boolean
     * @throws Throwable
     */
    /* public boolean updateFuels(String con) throws Throwable {
       StringBuffer iq = new StringBuffer(100);
       iq.append("am_msp_update_fuels");
       iq.append("'");
       iq.append(con);
       iq.append("'");

       for(int i = 0; i < 8; i++){
         switch (i) {
           case -3:
             iq.append(", ");
             iq.append(fuels[i]);
             break;
           default:
             iq.append(",'");
             iq.append(fuels[i]);
             iq.append("'");
         }
       }

       return (getStatement().executeUpdate(
         iq.toString()) == -1);
     }
     */

    /**
     * insertfuels
     *
     * @return boolean
     * @throws Throwable
     */

    /* public boolean insertFuels() throws Throwable {
       StringBuffer iq = new StringBuffer(100);
       iq.append("am_msp_insert_fuels");
       iq.append("'");
       iq.append(fuels[0]);
       iq.append("'");

       for (int i = 1; i <= 7; i++) {
         switch (i) {
           case -3:
             iq.append(", ");
             iq.append(fuels[i]);
             break;
           default:
             iq.append(",'");
             iq.append(fuels[i]);
             iq.append("'");
         }
       }

       return (getStatement().executeUpdate(
           iq.toString()) == -1);
     }
     */

}
