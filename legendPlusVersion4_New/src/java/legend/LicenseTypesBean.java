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

public class LicenseTypesBean extends ConnectionClass {
    private String[] licenses = new String[8];
    private String[] byLicenseCode = new String[8];

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

    public LicenseTypesBean() throws Exception {
    }

    public void setLicenses(String[] licenses) {
        if (licenses != null) {
            this.licenses = licenses;
        }
    }

    public String[] getLicenses() {
        return licenses;
    }

    public void setByLicenseCode(String[] byLicenseCode) {
        if (byLicenseCode != null) {
            this.byLicenseCode = byLicenseCode;
        }
    }

    public String[] getByLicenseCode() {
        return byLicenseCode;
    }


    public String[][] selectLicenses(String licenseId, String status) throws
            Throwable {
        Connection cnn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        MagmaDBConnection dbConn = new MagmaDBConnection();

        String param = "";
        String count = "";
        String select = "";

        if (licenseId.equalsIgnoreCase("0")) {

            count =
                    "SELECT COUNT(*) FROM AM_AD_LICENSETYPE WHERE LICENSE_STATUS = ?";
            select =
                    "SELECT * FROM AM_AD_LICENSETYPE WHERE LICENSE_STATUS = ? " +
                    " ORDER BY LICENSE_NAME ASC ";
            param = status;

        } else if (!licenseId.equalsIgnoreCase("0")) {

            count =
                    "SELECT COUNT(*) FROM AM_AD_LICENSETYPE WHERE LICENSE_ID = ?";
            select = "SELECT * FROM AM_AD_LICENSETYPE WHERE LICENSE_ID = ?";
            param = licenseId;
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

            values = new String[j][8];

            for (int x = 0; x < values.length; x++) {
                rs.next();
                for (int y = 0; y < 8; y++) {
                    values[x][y] = rs.getString(y + 1);
                }
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching licenses->" + e.getMessage());

        } finally {
            dbConn.closeConnection(cnn, ps, rs);
        }
        return values;
    }

    public String[][] selectByLicenseCode(String licenseCode) throws Throwable {
        Connection cnn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        MagmaDBConnection dbConn = new MagmaDBConnection();

        String count = "";
        String select = "";

        count = "SELECT COUNT(*) FROM AM_AD_LICENSETYPE WHERE LICENSE_CODE = ?";
        select = "SELECT * FROM AM_AD_LICENSETYPE WHERE LICENSE_CODE = ?";

        String[][] values = null;
        int j = 0;

        try {
            cnn = dbConn.getConnection("fixedasset");
            ps = cnn.prepareStatement(count);
            ps.setString(1, licenseCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                j = rs.getInt(1);
            }

            ps = cnn.prepareStatement(select);
            ps.setString(1, licenseCode);

            rs = ps.executeQuery();

            values = new String[j][8];

            for (int x = 0; x < values.length; x++) {
                rs.next();
                for (int y = 0; y < 8; y++) {
                    values[x][y] = rs.getString(y + 1);
                }
            }
        } catch (Exception e) {
            System.out.println("INFO:Error fetching license code->" +
                               e.getMessage());
        } finally {
            dbConn.closeConnection(cnn, ps, rs);
        }
        return values;
    }


    public boolean isUniqueCode(String licenseCode) throws Throwable {
        String query = "SELECT LICENSE_CODE FROM AM_AD_LICENSETYPE " +
                       "WHERE LICENSE_CODE = ? ";
        Connection cnn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean confirm = false;

        MagmaDBConnection dbConn = new MagmaDBConnection();
        try {
            cnn = dbConn.getConnection("fixedasset");
            ps = cnn.prepareStatement(query);

            ps.setString(1, licenseCode);

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


    /**
     * updatelicenses
     *
     * @param con String
     * @return boolean
     * @throws Throwable
     */
    public boolean updateLicenses(String licenseId) throws Throwable {

        String query = "UPDATE AM_AD_LICENSETYPE SET LICENSE_CODE = ?," +
                       "LICENSE_NAME = ?,NOTIFY_DAYS = ?,EVERY_DAYS = ?,ACCOUNT_TYPE = ?," +
                       "SUSPENSE_ACCT = ?,LICENSE_STATUS = ?  WHERE LICENSE_ID = ?";

        Connection cnn = null;
        PreparedStatement ps = null;
        MagmaDBConnection dbConn = new MagmaDBConnection();
        int affectedRow = 0;

        try {
            cnn = dbConn.getConnection("fixedasset");
            ps = cnn.prepareStatement(query);

            for (int i = 0; i < 7; i++) {
                ps.setString(i + 1, licenses[i]);
            }
            ps.setInt(8, Integer.parseInt(licenseId));
            affectedRow = ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("INFO:Error updating Licenses->" + e.getMessage());

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
     * insertlicenses
     *
     * @return boolean
     * @throws Throwable
     */
    public boolean insertLicenses() throws Throwable {
        String query =
                "INSERT INTO AM_AD_LICENSETYPE(LICENSE_CODE,LICENSE_NAME," +
                "NOTIFY_DAYS,EVERY_DAYS,ACCOUNT_TYPE,SUSPENSE_ACCT,LICENSE_STATUS,USER_ID," +
                "CREATE_DATE) VALUES(?,?,?,?,?,?,?,?,getDate())";

        Connection cnn = null;
        PreparedStatement ps = null;
        MagmaDBConnection dbConn = new MagmaDBConnection();
        int affectedRow = 0;
        try {
            cnn = dbConn.getConnection("fixedasset");
            ps = cnn.prepareStatement(query);

            for (int i = 0; i < 8; i++) {
                ps.setString(i + 1, licenses[i]);
            }

            if (ai.reqInsertAudit() == true) {
                atg.select(1, "SELECT * FROM AM_AD_LICENSETYPE WHERE license_Id = (SELECT MIN(license_Id) FROM AM_AD_LICENSETYPE)");
                atg.captureAuditFields(licenses);
                atg.logAuditTrail(ai);
            }
            affectedRow = ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("INFO:Error inserting Licenses->" + e.getMessage());

        } finally {
            dbConn.closeConnection(cnn, ps);
        }
        if (affectedRow > 0) {
            return true;
        } else {
            return false;
        }

    }


}
