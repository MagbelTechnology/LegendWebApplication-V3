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
       free the resource from exeeding the
     connection pool thereby shutting down
     the application
    Date	:Thursday 26th October,2006
 </pre>
 * @version 1.0
 */

public class DisposalReasonsBean extends ConnectionClass {
    private String[] reasons = new String[5];
    private String[] byReasonCode = new String[5];

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

    public DisposalReasonsBean() throws Exception {
    }

    public void setReasons(String[] reasons) {
        if (reasons != null) {
            this.reasons = reasons;
        }
    }

    public String[] getReasons() {
        return reasons;
    }

    public void setByReasonCode(String[] byReasonCode) {
        if (byReasonCode != null) {
            this.byReasonCode = byReasonCode;
        }
    }

    public String[] getByReasonCode() {
        return byReasonCode;
    }


    public String[][] selectReasons(String reasonId, String status) throws
            Throwable {
        Connection cnn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        MagmaDBConnection dbConn = new MagmaDBConnection();

        String param = "";
        String count = "";
        String select = "";
        if (reasonId.equalsIgnoreCase("0")) {

            count = "SELECT COUNT(*) FROM AM_AD_DISPOSALREASONS " +
                    "WHERE REASON_STATUS = ?";

            select = "SELECT * FROM AM_AD_DISPOSALREASONS WHERE " +
                     "REASON_STATUS = ? ORDER BY DESCRIPTION ASC ";
            param = status;

        } else if (!reasonId.equalsIgnoreCase("0")) {

            count = "SELECT COUNT(*)FROM AM_AD_DISPOSALREASONS " +
                    "WHERE REASON_ID = ?";

            select = "SELECT * FROM AM_AD_DISPOSALREASONS WHERE " +
                     "REASON_ID = ?";
            param = reasonId;
        } else {}

        String[][] values = new String[500][5];
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

            values = new String[j][5];

            for (int x = 0; x < values.length; x++) {
                rs.next();
                for (int y = 0; y < 5; y++) {
                    values[x][y] = rs.getString(y + 1);
                }
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching Reasons->" + e.getMessage());

        } finally {
            dbConn.closeConnection(cnn, ps, rs);
        }

        return values;
    }

    public String[][] selectReasonCode(String reasonCode) throws Throwable {

        Connection cnn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        System.out.print("Action performed");

        MagmaDBConnection dbConn = new MagmaDBConnection();

        String count = "";
        String select = "";
        count = "SELECT COUNT(*) FROM AM_AD_DISPOSALREASONS " +
                "WHERE REASON_CODE = ?";

        select = "SELECT * FROM AM_AD_DISPOSALREASONS WHERE " +
                 "REASON_CODE = ?  ";

        String[][] values = new String[500][5];
        int j = 0;

        try {
            cnn = dbConn.getConnection("fixedasset");

            ps = cnn.prepareStatement(count);
            ps.setString(1, reasonCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                j = rs.getInt(1);
            }

            ps = cnn.prepareStatement(select);
            ps.setString(1, reasonCode);

            rs = ps.executeQuery();

            values = new String[j][5];

            for (int x = 0; x < values.length; x++) {
                rs.next();
                for (int y = 0; y < 5; y++) {
                    values[x][y] = rs.getString(y + 1);
                }
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching Reasons Code->" +
                               e.getMessage());
        } finally {
            dbConn.closeConnection(cnn, ps, rs);
        }
        return values;
    }

    public boolean isUniqueCode(String reasonCode) throws Throwable {
        String query = "SELECT REASON_CODE FROM AM_AD_DISPOSALREASONS " +
                       "WHERE REASON_CODE = ? ";
        Connection cnn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean confirm = false;

        MagmaDBConnection dbConn = new MagmaDBConnection();

        try {
            cnn = dbConn.getConnection("fixedasset");
            ps = cnn.prepareStatement(query);

            ps.setString(1, reasonCode);

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

    public boolean updateReasons(String reasonId) {

        String query = "UPDATE AM_AD_DISPOSALREASONS SET " +
                       "REASON_CODE = ?,DESCRIPTION = ? ,REASON_STATUS = ? " +
                       "WHERE REASON_ID = ?";

        Connection cnn = null;
        PreparedStatement ps = null;
        MagmaDBConnection dbConn = new MagmaDBConnection();
        int affectedRow = 0;

        try {

            cnn = dbConn.getConnection("fixedasset");
            ps = cnn.prepareStatement(query);

            for (int i = 0; i < 4; i++) {
                ps.setString(i + 1, reasons[i]);
            }
            ps.setInt(4, Integer.parseInt(reasonId));

            affectedRow = ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("INFO:Error updating update Reasons->" +
                               e.getMessage());

        } finally {
            dbConn.closeConnection(cnn, ps);
        }
        if (affectedRow > 0) {
            return true;
        } else {
            return false;
        }
    }


    public boolean insertReasons() throws Throwable {
        String query =
                "INSERT INTO AM_AD_DISPOSALREASONS(REASON_CODE,DESCRIPTION," +
                "REASON_STATUS,USER_ID,CREATE_DATE) VALUES( ?,?,?,?,getDate())";

        Connection cnn = null;
        PreparedStatement ps = null;
        MagmaDBConnection dbConn = new MagmaDBConnection();
        int affectedRow = 0;
        try {

            cnn = dbConn.getConnection("fixedasset");
            ps = cnn.prepareStatement(query);

            for (int i = 0; i < 4; i++) {
                System.out.println("Input Data - [" + i + "] = " + reasons[i]);
                ps.setString(i + 1, reasons[i]);
            }

            if (ai.reqInsertAudit() == true) {
                atg.select(1, "SELECT * FROM AM_AD_DISPOSALREASONS WHERE reason_Id = (SELECT reason_Id FROM AM_AD_DISPOSALREASONS)");
                atg.captureAuditFields(reasons);
                atg.logAuditTrail(ai);
            }

            affectedRow = ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("INFO:Error inserting Reasons->" + e.getMessage());
            e.printStackTrace();

        } finally {
            dbConn.closeConnection(cnn, ps);
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
     * updatereasons
     *
     * @param con String
     * @return boolean
     * @throws Throwable
     */
    /* public boolean updateReasons(String con) throws Throwable {
       StringBuffer iq = new StringBuffer(100);
       iq.append("am_msp_update_disposalReasons");
       iq.append("'");
       iq.append(con);
       iq.append("'");

       for(int i = 0; i < 4; i++){
         switch (i) {
           case -1:
             iq.append(", ");
             iq.append(reasons[i]);
             break;
           default:
             iq.append(",'");
             iq.append(reasons[i]);
             iq.append("'");
         }
       }

       return (getStatement().executeUpdate(
         iq.toString()) == -1);
     }

     /**
      * insertreasons
      *
      * @return boolean
      * @throws Throwable
      */
     /*public boolean insertReasons() throws Throwable {
       StringBuffer iq = new StringBuffer(100);
       iq.append("am_msp_insert_disposalReasons");
       iq.append("'");
       iq.append(reasons[0]);
       iq.append("'");

       for (int i = 1; i <= 3; i++) {
         switch (i) {
           case -1:
             iq.append(", ");
             iq.append(reasons[i]);
             break;
           default:
             iq.append(",'");
             iq.append(reasons[i]);
             iq.append("'");
         }
       }

       return (getStatement().executeUpdate(
           iq.toString()) == -1);
        }
      */



}
