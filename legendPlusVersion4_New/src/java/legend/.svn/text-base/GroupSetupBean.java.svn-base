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


public class GroupSetupBean extends ConnectionClass {
    private String[] groups = new String[8];
    private String[] byGroupCode = new String[8];

    private AuditInfo ai = new AuditInfo();
    private AuditTrailGen atg = new AuditTrailGen();


    public final void setAuditInfo(AuditInfo AI) {
        this.ai = AI;
    }


    public final void setAuditInfo(String TableName, String BranchCode,
                                   int LoginId, String RowId,
                                   boolean ReqInsertAudit) {
        ai.setTableName(TableName);
        ai.setBranchCode(BranchCode);
        ai.setLoginId(LoginId);
        ai.setRowId(RowId);
        ai.setReqInsertAudit(ReqInsertAudit);
    }


    public final AuditInfo getAuditInfo() {
        return this.ai;
    }


    public GroupSetupBean() throws Throwable {
    }

    public void setGroups(String[] groups) {
        if (groups != null) {
            this.groups = groups;
        }
    }

    public String[] getGroups() {
        return groups;
    }


    public void setByGroupCode(String[] byGroupCode) {
        if (groups != null) {
            this.byGroupCode = byGroupCode;
        }
    }

    public String[] getByGroupCode() {
        return byGroupCode;
    }


    public String[][] selectGroups(String groupId, String status) throws
            Throwable {

        Connection cnn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        MagmaDBConnection dbConn = new MagmaDBConnection();

        String param = "";
        String count = "";
        String select = "";
        if (groupId.equalsIgnoreCase("0")) {

            count = "SELECT COUNT(*) FROM AM_AD_GROUP WHERE GROUP_STATUS = ? ";

            select = "SELECT * FROM AM_AD_GROUP WHERE GROUP_STATUS = ? " +
                     "ORDER BY GROUP_NAME ASC";
            param = status;

        } else if (!groupId.equalsIgnoreCase("0")) {

            count = "SELECT COUNT(*) FROM AM_AD_GROUP WHERE GROUP_ID = ? ";

            select = "SELECT * FROM AM_AD_GROUP WHERE GROUP_ID = ?";
            param = groupId;
        } else {}

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

            values = new String[j][8];

            for (int x = 0; x < values.length; x++) {
                rs.next();
                for (int y = 0; y < 8; y++) {
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

    public String[][] selectByGroupCode(String groupCode) throws Throwable {

        Connection cnn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        MagmaDBConnection dbConn = new MagmaDBConnection();

        String param = "";
        String count = "";
        String select = "";

        count = "SELECT COUNT(*) FROM AM_AD_GROUP WHERE GROUP_CODE = ? ";

        select = "SELECT * FROM AM_AD_GROUP WHERE GROUP_CODE = ?";

        String[][] values = null;
        int j = 0;

        try {
            cnn = dbConn.getConnection("fixedasset");

            ps = cnn.prepareStatement(count);
            ps.setString(1, groupCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                j = rs.getInt(1);
            }
            ps = cnn.prepareStatement(select);
            ps.setString(1, groupCode);
            rs = ps.executeQuery();

            values = new String[j][8];

            for (int x = 0; x < values.length; x++) {
                rs.next();
                for (int y = 0; y < 8; y++) {
                    values[x][y] = rs.getString(y + 1);
                }
            }
        } catch (Exception e) {
            System.out.println("INFO:Error fetching group Code->" +
                               e.getMessage());
        } finally {
            dbConn.closeConnection(cnn, ps, rs);
        }

        return values;
    }


    public boolean isUniqueGroup(String groupCode, String groupAcronym) throws
            Throwable {
        String query = "SELECT GROUP_CODE ,GROUP_ACRONYM FROM AM_AD_GROUP " +
                       "WHERE GROUP_CODE = ? OR GROUP_ACRONYM = ?";
        Connection cnn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean confirm = false;
        MagmaDBConnection dbConn = new MagmaDBConnection();

        try {
            cnn = dbConn.getConnection("fixedasset");
            ps = cnn.prepareStatement(query);

            ps.setString(1, groupCode);
            ps.setString(2, groupAcronym);
            rs = ps.executeQuery();
            while (rs.next()) {
                confirm = true;
            }

        } catch (Exception e) {
            System.out.println("INFO:Error checking for duplicate Group->" +
                               e.getMessage());

        } finally {
            dbConn.closeConnection(cnn, ps, rs);
        }

        return confirm;
    }

    public boolean updateGroups(String groupId, String[] groups) throws
            Throwable {

        String query =
                "UPDATE AM_AD_GROUP SET GROUP_CODE = ?,GROUP_ACRONYM = ?," +
                "GROUP_NAME = ?,GROUP_ADDRESS = ?,GROUP_PHONE = ?,GROUP_FAX = ?," +
                " GROUP_STATUS = ? WHERE GROUP_ID = ?";

        Connection cnn = null;
        PreparedStatement ps = null;
        int affectedRow = 0;
        MagmaDBConnection dbConn = new MagmaDBConnection();

        try {
            cnn = dbConn.getConnection("fixedasset");
            ps = cnn.prepareStatement(query);

            for (int i = 0; i < 8; i++) {
                ps.setString(i + 1, groups[i]);
            }
            ps.setInt(8, Integer.parseInt(groupId));
            affectedRow = ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("INFO:Error updating Groups->" + e.getMessage());

        } finally {
            dbConn.closeConnection(cnn, ps);
        }
        if (affectedRow > 0) {
            return true;
        } else {
            return false;
        }

    }

    public boolean insertGroups(String[] groups) throws Throwable {
        String query = "INSERT INTO AM_AD_GROUP(GROUP_CODE,GROUP_ACRONYM,GROUP_NAME,GROUP_ADDRESS,GROUP_PHONE," +
                       "GROUP_FAX,GROUP_STATUS,USER_ID,CREATE_DATE)VALUES(?,?,?,?,?,?,?,?,getDate())";
        Connection cnn = null;
        PreparedStatement ps = null;
        int affectedRow = 0;
        MagmaDBConnection dbConn = new MagmaDBConnection();

        try {
            cnn = dbConn.getConnection("fixedasset");
            ps = cnn.prepareStatement(query);

            for (int i = 0; i < 8; i++) {
                ps.setString(i + 1, groups[i]);
            }

            if (ai.reqInsertAudit() == true) {
                atg.select(1, "SELECT * FROM AM_AD_GROUP WHERE group_Id = (SELECT MIN(group_Id) FROM AM_AD_GROUP)");
                atg.captureAuditFields(groups);
                atg.logAuditTrail(ai);
            }
            affectedRow = ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("INFO:Error inserting Groups->" + e.getMessage());

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
     * updategroups
     *
     * @param con String
     * @return boolean
     * @throws Throwable
     */
    /*public boolean updateGroups(String con, String[] groups) throws Throwable {
      StringBuffer iq = new StringBuffer(100);
      iq.append("am_msp_update_groups");
      iq.append("'");
      iq.append(con);
      iq.append("'");

      for(int i = 0; i < 8; i++){
        switch (i) {
          case -1:
            iq.append(", ");
            iq.append(groups[i]);
            break;
          default:
            iq.append(",'");
            iq.append(groups[i]);
            iq.append("'");
        }
      }

      return (getStatement().executeUpdate(
        iq.toString()) == -1);
       }
     */

    /**
     * insertgroups
     *
     * @return boolean
     * @throws Throwable
     */
    /* public boolean insertGroups(String[] groups) throws Throwable {
       StringBuffer iq = new StringBuffer(100);
       iq.append("am_msp_insert_groups");
       iq.append("'");
       iq.append(groups[0]);
       iq.append("'");

       for (int i = 1; i <= 7; i++) {
         switch (i) {
           case -1:
             iq.append(", ");
             iq.append(Integer.parseInt(groups[i]));
             break;
           default:
             iq.append(",'");
             iq.append(groups[i]);
             iq.append("'");
         }
       }

       return (getStatement().executeUpdate(
           iq.toString()) == -1);
     }
     */



}