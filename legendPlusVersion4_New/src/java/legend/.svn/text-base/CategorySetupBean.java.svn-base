package legend;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import audit.AuditInfo;
import audit.AuditTrailGen;

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
   Modification: by Jejelowo .B.Festus:
   Reason : To Change All The Store Procedure to
       approprite Java SQL Statement.
   Date	: Friday 27th November,2006
 </pre>
 * @version 1.0
 */
public class CategorySetupBean extends ConnectionClass {
    private String[] categories = new String[25];

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


    public CategorySetupBean() throws Exception {
    }

    public void setCategories(String[] categories) {
        if (categories != null) {
            this.categories = categories;
        }
    }

    public String[] getCategories() {
        return categories;
    }

    /**
     * selectcategories
     *
     * @param con String
     * @return String[][]
     * @throws Throwable

     modified by Jejelowo .B. Festus
     */
    public String[][] selectCategories(String con, String sta) throws Throwable {

        /* String countQuery = "SELECT COUNT(*) FROM AM_AD_CATEGORY "+
              "WHERE CATEGORY_STATUS = '"+sta+"'";
          String selectQuery = "SELECT * FROM AM_AD_CATEGORY  "+
            "WHERE CATEGORY_STATUS = '"+sta+"'  "+
            "ORDER BY CATEGORY_NAME ASC    ";*/
        String queryCount = "";
        String querySelect = "";

        if (con.equalsIgnoreCase("0")) {
            queryCount =
                    "select count(*) from am_ad_category where category_status = '" +
                    sta + "'";
            querySelect = "select CATEGORY_ID,[CATEGORY_CODE], CATEGORY_NAME," +
                          "		CATEGORY_ACRONYM,REQUIRED_FOR_FLEET," +
                          "		CATEGORY_CLASS, PM_CYCLE_PERIOD," +
                          "		MILEAGE, NOTIFY_MAINT_DAYS," +
                          "		NOTIFY_EVERY_DAYS, DEP_RATE," +
                          "		RESIDUAL_VALUE, ASSET_LEDGER," +
                          "		DEP_LEDGER, ACCUM_DEP_LEDGER," +
                          "		GL_ACCOUNT, INSURANCE_ACCT," +
                          "		LICENSE_LEDGER, FUEL_LEDGER," +
                          "		ACCIDENT_LEDGER, CATEGORY_STATUS,[user_id], " +
                          "              create_date, acct_type, currency_Id" +
                          " from am_ad_category where category_status = '" +
                          sta + "' order by category_name asc";

        } else if (!con.equalsIgnoreCase("0")) {
            queryCount =
                    "select count(*) from am_ad_category where category_id = " +
                    con;
            querySelect = "select CATEGORY_ID,[CATEGORY_CODE], CATEGORY_NAME," +
                          "		CATEGORY_ACRONYM,REQUIRED_FOR_FLEET," +
                          "		CATEGORY_CLASS, PM_CYCLE_PERIOD," +
                          "		MILEAGE, NOTIFY_MAINT_DAYS," +
                          "		NOTIFY_EVERY_DAYS, DEP_RATE," +
                          "		RESIDUAL_VALUE, ASSET_LEDGER," +
                          "		DEP_LEDGER, ACCUM_DEP_LEDGER," +
                          "		GL_ACCOUNT, INSURANCE_ACCT," +
                          "		LICENSE_LEDGER, FUEL_LEDGER," +
                          "		ACCIDENT_LEDGER, CATEGORY_STATUS,[user_id], " +
                          "              create_date, acct_type,currency_Id" +
                          " from am_ad_category where category_id = " + con;
        } else {
        }

        ResultSet rc = getStatement().executeQuery(queryCount);
        ResultSet rv = getStatement().executeQuery(querySelect);

        rc.next();
        String[][] values = new String[rc.getInt(1)][25];

        for (int x = 0; x < values.length; x++) {
            rv.next();
            for (int y = 0; y < 25; y++) {
                values[x][y] = rv.getString(y + 1);
            }
        }

        return values;
    }

    /**
     * updatecategories
     *
     * @param con String
     * @return boolean
     * @throws Throwable
     *
     */
    public boolean updateCategories(String con) throws Throwable {

        String query = "UPDATE AM_AD_CATEGORY " +
                       " 	SET  " +
                       "		[CATEGORY_CODE] = ?, CATEGORY_NAME = ?," +
                       "		CATEGORY_ACRONYM =  ?,REQUIRED_FOR_FLEET = ?," +
                       "		CATEGORY_CLASS = ?, PM_CYCLE_PERIOD = ?," +
                       "		MILEAGE = ?, NOTIFY_MAINT_DAYS = ?," +
                       "		NOTIFY_EVERY_DAYS = ?, RESIDUAL_VALUE = ?," +
                       "		DEP_RATE = ?, ASSET_LEDGER = ?," +
                       "		DEP_LEDGER = ?, ACCUM_DEP_LEDGER = ?," +
                       "		GL_ACCOUNT = ?, INSURANCE_ACCT = ?," +
                       "		LICENSE_LEDGER = ?, FUEL_LEDGER = ?," +
                       "		ACCIDENT_LEDGER = ?, CATEGORY_STATUS = ?, " +
                       "              acct_type = ?, currency_Id=? " +
                       "	WHERE  CATEGORY_ID = ? ";
        PreparedStatement ps = getConnection().prepareStatement(query);
        ps.setString(1, categories[0]);
        ps.setString(2, categories[1]);
        ps.setString(3, categories[2]);
        ps.setString(4, categories[3]);
        ps.setString(5, categories[4]);
        ps.setString(6, categories[5]);
        ps.setString(7, categories[6]);
        ps.setString(8, categories[7]);
        ps.setString(9, categories[8]);
        ps.setString(10, categories[9]);
        ps.setString(11, categories[10]);
        ps.setString(12, categories[11]);
        ps.setString(13, categories[12]);
        ps.setString(14, categories[13]);
        ps.setString(15, categories[14]);
        ps.setString(16, categories[15]);
        ps.setString(17, categories[16]);
        ps.setString(18, categories[17]);
        ps.setString(19, categories[18]);
        ps.setString(20, categories[19]);
        ps.setString(21, categories[21]);
        System.out.println("CUUR" + categories[24]);
        ps.setString(22, categories[24]);
        ps.setString(23, con);
        int ret = ps.executeUpdate();
        if (ret > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * insertcategories
     *
     * @return boolean
     * @throws Throwable
     */
    public boolean insertCategories() throws Throwable {
        java.text.SimpleDateFormat sd2 = new java.text.SimpleDateFormat(
                "MM-dd-yyyy");
        String query = "INSERT INTO AM_AD_CATEGORY(" +
                       "[CATEGORY_CODE], CATEGORY_NAME, CATEGORY_ACRONYM," +
                       "REQUIRED_FOR_FLEET, CATEGORY_CLASS, PM_CYCLE_PERIOD," +
                       "MILEAGE, NOTIFY_MAINT_DAYS, NOTIFY_EVERY_DAYS," +
                       "RESIDUAL_VALUE, DEP_RATE, ASSET_LEDGER," +
                       "DEP_LEDGER, ACCUM_DEP_LEDGER, GL_ACCOUNT," +
                       "INSURANCE_ACCT, LICENSE_LEDGER, FUEL_LEDGER," +
                       "ACCIDENT_LEDGER, CATEGORY_STATUS, [USER_ID]," +
                       "CREATE_DATE, acct_type, currency_Id)" +
                       "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = getConnection().prepareStatement(query);
        ps.setString(1, categories[0]);
        ps.setString(2, categories[1]);
        ps.setString(3, categories[2]);
        ps.setString(4, categories[3]);
        ps.setString(5, categories[4]);
        ps.setString(6, categories[5]);
        ps.setString(7, categories[6]);
        ps.setString(8, categories[7]);
        ps.setString(9, categories[8]);
        ps.setString(10, categories[9]);
        ps.setString(11, categories[10]);
        ps.setString(12, categories[11]);
        ps.setString(13, categories[12]);
        ps.setString(14, categories[13]);
        ps.setString(15, categories[14]);
        ps.setString(16, categories[15]);
        ps.setString(17, categories[16]);
        ps.setString(18, categories[17]);
        ps.setString(19, categories[18]);
        ps.setString(20, categories[19]);
        ps.setString(21, categories[20]);
        ps.setString(22, sd2.format(new java.util.Date()));
        ps.setString(23, categories[21]);
        ps.setString(24, categories[24]);
        if (ai.reqInsertAudit() == true) {
            atg.select(1, "SELECT * FROM am_ad_category WHERE category_Id = (SELECT MIN(category_Id) FROM AM_AD_CATEGORY)");
            atg.captureAuditFields(categories);
            atg.logAuditTrail(ai);
        }
        return (ps.executeUpdate() != -1);

    }

    /**
     * Rahman oloritun
     * @param code String
     * @return boolean
     * @throws Throwable
     */
    public boolean catcodeExists(String code) throws Throwable {
        String query = "Select * from AM_AD_CATEGORY WHERE [CATEGORY_CODE]='" +
                       code + "'";

        ResultSet rs = getStatement().executeQuery(query);

        boolean result = rs.next();
        freeResource();
        return result;
    }

    public int getCategoryID(String code) throws Throwable {
        String query =
                "Select Category_ID from AM_AD_CATEGORY WHERE [CATEGORY_CODE]='" +
                code + "'";

        ResultSet rs = getStatement().executeQuery(query);
        int result = 0;
        if (rs.next()) {
            result = rs.getInt("Category_ID");
        }

        return result;
    }

    public boolean acronymExists(String acronym) throws Throwable {
        String query = "Select * from AM_AD_CATEGORY WHERE CATEGORY_ACRONYM='" +
                       acronym + "'";

        ResultSet rs = getStatement().executeQuery(query);

        boolean result = rs.next();
        freeResource();
        return result;
    }
}
