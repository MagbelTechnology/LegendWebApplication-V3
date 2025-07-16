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
 * @version 1.0
 */
public class RegionSetupBean extends ConnectionClass {
    private String[] regions = new String[8];
    private String[] reg = new String[8];

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


    public RegionSetupBean() throws Throwable {
    }

    public void setRegions(String[] regions) {
        if (regions != null) {
            this.regions = regions;
        }
    }

    public String[] getRegions() {
        return regions;
    }

    public void setReg(String[] reg) {
        if (reg != null) {
            this.reg = reg;
        }
    }

    public String[] getReg() {
        return reg;
    }


    /**
     * selectregions
     *
     * @param con String
     * @param sta String
     * @return String[][]
     * @throws Throwable
     */
    public String[][] selectRegions(String con, String sta) throws Throwable {
        StringBuffer cq = new StringBuffer(100);

        String statusQuery =
                "SELECT COUNT(*) FROM AM_AD_REGION WHERE REGION_STATUS = '" +
                sta + "'";
        String idQuery = "SELECT COUNT(*) FROM AM_AD_REGION WHERE REGION_ID = " +
                         con;

        String selQuerySatus =
                "SELECT * FROM AM_AD_REGION WHERE REGION_STATUS = '" + sta +
                "' " +
                "ORDER BY REGION_NAME ASC";

        String selQueryId = "SELECT * FROM AM_AD_REGION WHERE REGION_ID = " +
                            con;

        ResultSet rc = null;
        if (con.equals("0")) {
            rc = getStatement().executeQuery(statusQuery);
        } else if (con != "") {
            rc = getStatement().executeQuery(idQuery);
        } else {}

        ResultSet rv = null;

        if (con.equals("0")) {
            rv = getStatement().executeQuery(selQuerySatus);
        } else if (con != "") {
            rv = getStatement().executeQuery(selQueryId);
        } else {}

        rc.next();
        String[][] values = new String[rc.getInt(1)][8];

        for (int x = 0; x < values.length; x++) {
            rv.next();
            for (int y = 0; y < 8; y++) {
                values[x][y] = rv.getString(y + 1);
            }
        }

        return values;

    }

    /**
     * updateregions
     *
     * @param con String
     * @return boolean
     * @throws Throwable
     */
    public boolean updateRegions(String con) throws Throwable {
        StringBuffer iq = new StringBuffer(100);
        String str = "update am_ad_region set region_code = '" + regions[0] +
                     "',   " +
                     "region_acronym = '" + regions[1] + "',region_name = '" +
                     regions[2] + "',region_address = '" + regions[3] +
                     "',    " +
                     "region_phone = '" + regions[4] + "',region_fax = '" +
                     regions[5] + "',region_status = '" + regions[6] + "'    " +
                     "where  region_id = " + con;

        int ret = getStatement().executeUpdate(str);
        if (ret > 0) {
            return true;
        } else {
            return false;
        }
    }

    public String[][] selectRegionCode(String regionCode) throws Throwable {

        Connection mor = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        System.out.print("Action performed");

        MagmaDBConnection dbConn = new MagmaDBConnection();

        String count = "";
        String select = "";
        count = "SELECT COUNT(*) FROM am_ad_region " +
                "WHERE region_CODE = ?";

        select = "SELECT * FROM am_ad_region WHERE " +
                 "region_CODE = ?  ";

        String[][] values = new String[800][8];
        int j = 0;

        try {
            mor = dbConn.getConnection("legendPlus");

            ps = mor.prepareStatement(count);
            ps.setString(1, regionCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                j = rs.getInt(1);
            }

            ps = mor.prepareStatement(select);
            ps.setString(1, regionCode);

            rs = ps.executeQuery();

            values = new String[j][8];

            for (int x = 0; x < values.length; x++) {
                rs.next();
                for (int y = 0; y < 8; y++) {
                    values[x][y] = rs.getString(y + 1);
                }
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching region Code->" +
                               e.getMessage());
        } finally {
            dbConn.closeConnection(mor, ps, rs);
        }
        return values;
    }

    public boolean isUniqueCode(String regionCode) throws Throwable {
        String query = "SELECT region_CODE FROM am_ad_region " +
                       "WHERE region_CODE = ? ";
        Connection mor = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean confirm = false;

        MagmaDBConnection dbConn = new MagmaDBConnection();

        try {
            mor = dbConn.getConnection("legendPlus");
            ps = mor.prepareStatement(query);

            ps.setString(1, regionCode);

            rs = ps.executeQuery();
            while (rs.next()) {
                confirm = true;
            }

        } catch (Exception e) {
            System.out.println("INFO:Error checking for duplicate code->" +
                               e.getMessage());

        } finally {
            dbConn.closeConnection(mor, ps, rs);
        }

        return confirm;
    }


    /**
     * insertregions
     *
     * @return boolean
     * @throws Throwable
     */
    public boolean insertRegions() throws Throwable {
        StringBuffer iq = new StringBuffer(100);

        String str = "insert into am_ad_region(" +
                     "region_code,region_acronym,region_name," +
                     "region_address,region_phone,region_fax," +
                     "region_status,	[user_id]," +
                     "create_date	) values(";

        iq.append(str);
        iq.append("'");
        iq.append(regions[0]);
        iq.append("'");

        for (int i = 1; i <= 7; i++) {
            switch (i) {
            case -1:
                iq.append(", ");
                iq.append(regions[i]);
                break;
            default:
                iq.append(",'");
                iq.append(regions[i]);
                iq.append("'");
            }
        }

        iq.append(",getDate())");
        if (ai.reqInsertAudit() == true) {
            atg.select(1, "SELECT * FROM AM_AD_REGION WHERE region_Id = (SELECT MIN(region_Id) FROM AM_AD_REGION)");
            atg.captureAuditFields(regions);
            atg.logAuditTrail(ai);
        }
        boolean done = (getStatement().executeUpdate(iq.toString()) == -1);
        System.out.println("INFO:Return variable is " + done);
        return true;
    }
}
