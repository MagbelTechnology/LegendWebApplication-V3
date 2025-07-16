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

public class InsuranceSetupBean extends ConnectionClass {


    private String[] insurances = new String[17];
    private String[] byInsuranceCode = new String[17];

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


    public InsuranceSetupBean() throws Exception {
    }

    public void setInsurances(String[] insurances) {
        if (insurances != null) {
            this.insurances = insurances;
        }
    }

    public String[] getInsurances() {
        return insurances;
    }

    public void setByInsuranceCode(String[] byInsuranceCode) {
        if (byInsuranceCode != null) {
            this.byInsuranceCode = byInsuranceCode;
        }
    }

    public String[] getByInsuranceCode() {
        return byInsuranceCode;
    }

    public String[][] selectInsurances(String insuranceId, String status) throws
            Throwable {
        Connection cnn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        MagmaDBConnection dbConn = new MagmaDBConnection();

        String param = "";
        String count = "";
        String select = "";
        if (insuranceId.equalsIgnoreCase("0")) {

            count =
                    "SELECT COUNT(*) FROM AM_AD_INSURANCE WHERE INSURANCE_STATUS = ?";

            select =
                    "SELECT * FROM AM_AD_INSURANCE	WHERE INSURANCE_STATUS = ? " +
                    "ORDER BY INSURANCE_NAME ASC";
            param = status;

        } else if (!insuranceId.equalsIgnoreCase("0")) {

            count =
                    "select count(*) from am_ad_insurance where insurance_id = ?";
            select = "SELECT * FROM AM_AD_INSURANCE WHERE INSURANCE_ID = ?";
            param = insuranceId;
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

            values = new String[j][17];

            for (int x = 0; x < values.length; x++) {
                rs.next();
                for (int y = 0; y < 17; y++) {
                    values[x][y] = rs.getString(y + 1);
                }
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching insurances->" +
                               e.getMessage());

        } finally {
            dbConn.closeConnection(cnn, ps, rs);
        }
        return values;
    }

    public String[][] selectByInsuranceCode(String insuranceCode) throws
            Throwable {
        Connection cnn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        MagmaDBConnection dbConn = new MagmaDBConnection();
        String count = "";
        String select = "";

        count = "SELECT COUNT(*) FROM AM_AD_INSURANCE WHERE INSURANCE_CODE = ?";
        select = "SELECT * FROM AM_AD_INSURANCE WHERE INSURANCE_CODE = ?";

        String[][] values = null;
        int j = 0;

        try {
            cnn = dbConn.getConnection("fixedasset");

            ps = cnn.prepareStatement(count);
            ps.setString(1, insuranceCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                j = rs.getInt(1);
            }
            ps = cnn.prepareStatement(select);
            ps.setString(1, insuranceCode);
            rs = ps.executeQuery();
            values = new String[j][17];

            for (int x = 0; x < values.length; x++) {
                rs.next();
                for (int y = 0; y < 17; y++) {
                    values[x][y] = rs.getString(y + 1);
                }
            }
        } catch (Exception e) {
            System.out.println("INFO:Error fetching insurance code->" +
                               e.getMessage());

        } finally {
            dbConn.closeConnection(cnn, ps, rs);
        }
        return values;
    }


    public boolean isUniqueCode(String insuranceCode) throws Throwable {
        String query = "SELECT INSURANCE_CODE FROM AM_AD_INSURANCE " +
                       "WHERE INSURANCE_CODE = ? ";
        Connection cnn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean confirm = false;

        MagmaDBConnection dbConn = new MagmaDBConnection();

        try {
            cnn = dbConn.getConnection("fixedasset");
            ps = cnn.prepareStatement(query);

            ps.setString(1, insuranceCode);

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


    public boolean updateInsurances(String insuranceId) throws Throwable {

        String query =
                "UPDATE AM_AD_INSURANCE SET INSURANCE_CODE = ?,INSURANCE_NAME = ?," +
                "CONTACT_PERSON = ?,CONTACT_ADDRESS = ?,INSURANCE_STATE = ?,INSURANCE_PHONE = ? ," +
                "INSURANCE_FAX = ?,INSURANCE_EMAIL = ?,NOTIFY_DAYS = ?,EVERY_DAYS = ?, " +
                "ACCOUNT_TYPE = ?,ACCOUNT_NUMBER = ?,INSURANCE_STATUS = ?,INSURANCE_PROVINCE = ? " +
                "WHERE INSURANCE_ID = ?";

        Connection cnn = null;
        PreparedStatement ps = null;
        MagmaDBConnection dbConn = new MagmaDBConnection();

        int affectedRow = 0;

        try {

            cnn = dbConn.getConnection("fixedasset");
            ps = cnn.prepareStatement(query);

            for (int i = 0; i < 15; i++) {
                ps.setString(i + 1, insurances[i]);
            }

            ps.setInt(15, Integer.parseInt(insuranceId));
            affectedRow = ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("INFO:Error updating Insurances->" +
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


    public boolean insertInsurances() throws Throwable {
        String query =
                "INSERT INTO AM_AD_INSURANCE(INSURANCE_CODE,INSURANCE_NAME," +
                "CONTACT_PERSON,CONTACT_ADDRESS,INSURANCE_STATE,INSURANCE_PHONE,INSURANCE_FAX," +
                "INSURANCE_EMAIL,NOTIFY_DAYS,EVERY_DAYS,ACCOUNT_TYPE,ACCOUNT_NUMBER," +
                "INSURANCE_STATUS,INSURANCE_PROVINCE,USER_ID,CREATE_DATE)" +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,getDate())";

        Connection cnn = null;
        PreparedStatement ps = null;
        MagmaDBConnection dbConn = new MagmaDBConnection();
        int affectedRow = 0;

        try {
            cnn = dbConn.getConnection("fixedasset");
            ps = cnn.prepareStatement(query);

            for (int i = 0; i < 15; i++) {
                ps.setString(i + 1, insurances[i]);
            }

            if (ai.reqInsertAudit() == true) {
                atg.select(1, "SELECT * FROM AM_AD_INSURANCE WHERE insurance_Id = (SELECT MIN(insurance_Id) FROM AM_AD_INSURANCE)");
                atg.captureAuditFields(insurances);
                atg.logAuditTrail(ai);
            }
            affectedRow = ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("INFO:Error insert Groups->" + e.getMessage());

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
     * updateinsurances
     *
     * @param con String
     * @return boolean
     * @throws Throwable
     */

    /*public boolean updateInsurances(String con) throws Throwable {
      StringBuffer iq = new StringBuffer(100);
      iq.append("am_msp_update_insurances");
      iq.append("'");
      iq.append(con);
      iq.append("'");

      for(int i = 0; i < 15; i++){
        switch (i) {
          case 8:
          case 9:
            iq.append(", ");
            iq.append(insurances[i]);
            break;
          default:
            iq.append(",'");
            iq.append(insurances[i]);
            iq.append("'");
        }
      }

      return (getStatement().executeUpdate(
        iq.toString()) == -1);
       }
     */

    /**
     * insertinsurances
     *
     * @return boolean
     * @throws Throwable
     */
    /* public boolean insertInsurances() throws Throwable {
       StringBuffer iq = new StringBuffer(100);
       iq.append("am_msp_insert_insurances");
       iq.append("'");
       iq.append(Integer.parseInt(insurances[0]));
       iq.append("'");

       for (int i = 1; i <= 14; i++) {
         switch (i) {
           case 8:
           case 9:
             iq.append(", ");
             iq.append(insurances[i]);
             break;
           default:
             iq.append(",'");
             iq.append(insurances[i]);
             iq.append("'");
         }
       }

       return (getStatement().executeUpdate(
           iq.toString()) == -1);
     }
     */

}
