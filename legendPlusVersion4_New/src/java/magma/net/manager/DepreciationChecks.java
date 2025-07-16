package magma.net.manager;

import java.sql.*;
import java.util.*;

import magma.net.dao.MagmaDBConnection;
import magma.net.vao.Asset;
import com.magbel.util.DatetimeFormat;
import magma.net.vao.ProcesingInfo;

public class DepreciationChecks extends MagmaDBConnection {

    private long procid = 0;
    private DatetimeFormat dateFormat;
    java.text.SimpleDateFormat sdf;
    public DepreciationChecks() {
        sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");
        //dbConnection = new MagmaDBConnection();
        dateFormat = new DatetimeFormat();
        procid = (long)new java.util.Date().getTime();
        System.out.println(
                "Perform Prelimenary Depreciation Process Check Process ID -->> " +
                procid);
    }

    public boolean runChecks() {
        boolean test = false;
        clearErrors();
        clearDepreciationTemp();
        /*
        test = expenseAccumGLsexist();
        test = prefixCheck();
        test = findAssetWithoutValidBranches();
        test = findAssetWithoutValidCategory();
        test = isNotDistAssetDistributed();
        */
        return (expenseAccumGLsexist() ||  expenseAccumGLsexist()||findAssetWithoutValidBranches()
                ||findAssetWithoutValidCategory()|| isNotDistAssetDistributed());
    }

    public boolean expenseAccumGLsexist() {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        StringBuffer notin = new StringBuffer();
        boolean exists = false;

        String sql = "Select * From am_ad_category WHERE Category_Status = 'ACTIVE'"
                     + " AND (Dep_ledger IS NULL OR Dep_ledger='') OR "
                     + " (Accum_Dep_ledger IS NULL OR Accum_Dep_ledger='')";
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                String catid = rs.getString("category_code");
                String C = rs.getString("category_name");
                String DepGL = rs.getString("Dep_ledger");
                String AccumGL = rs.getString("Accum_Dep_ledger");
                exists = true;
                logError(String.valueOf(procid),
                         "The Category with Category Code " + catid + " Category Name " + C +
                         " Has Gl Accounts Missing. PLEASE CORRECT to enable you continue with Depreciation processing.");

            }

        } catch (Exception ex) {
            System.out.println(
                    "WARNING: Error Checking Category ->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        //System.out.println("GLcheck=====>" + notin.toString());
        closeConnection(con, ps, rs);
        return exists;
    }

    public boolean prefixCheck() {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        ResultSet rs2 = null;
        PreparedStatement ps2 = null;
        StringBuffer notin = new StringBuffer();
        String sburequired = getCompanyInfo()[4];
        String query = "";
        String sbuLevel = "";
        boolean exists = false;
        String prefix_req = getCompanyInfo()[8];
        if (prefix_req.equalsIgnoreCase("Y")) {
            String sqlBranches = "SELECT DISTINCT BRANCH_ID " +
                                 " FROM AM_ASSET  WHERE asset_status='ACTIVE' AND BRANCH_ID IN " +
                                 "(SELECT BRANCH_ID FROM am_ad_branch) ";

            if (sburequired.equalsIgnoreCase("Y")) {
                sbuLevel = getCompanyInfo()[5];

                if (sbuLevel.equalsIgnoreCase("Department")) {
                    query =
                            "SELECT S.BRANCHCODE ,D.DEPT_NAME,S.DEPTCODE, D.DEPT_NAME FROM sbu_branch_dept S, AM_AD_DEPARTMENT D "
                            +
                            " WHERE S.DEPTID = D.DEPT_ID AND S.BRANCHID=? AND (S.GL_PREFIX IS NULL OR "
                            + " S.GL_PREFIX='')";
                }
                if (sbuLevel.equalsIgnoreCase("Sector/Units")) {
                    query =
                            "SELECT A.BRANCHCODE,B.SECTION_NAME, C.DEPT_NAME FROM sbu_dept_section A,"
                            +
                            " AM_AD_SECTION B, AM_AD_DEPARTMENT C WHERE A.SECTIONID=B.SECTION_ID"
                            +
                            " AND A.DEPTID = C.DEPT_ID AND A.BRANCHID=? AND (A.GL_PREFIX IS NULL OR "
                            + " A.GL_PREFIX='')";
                }
            } else {
                query =
                        "SELECT  BRANCH_CODE, BRANCH_NAME,BRANCH_CODE FROM AM_AD_BRANCH "
                        + " WHERE BRANCH_ID=? AND (GL_PREFIX IS NULL OR "
                        + " GL_PREFIX='')";
            }
            try {
                con = getConnection("legendPlus");
                ps = con.prepareStatement(sqlBranches);
                rs = ps.executeQuery();

                while (rs.next()) {
                    String bid = rs.getString(1);
                    ps2 = con.prepareStatement(query);
                    ps2.setString(1, bid);
                    rs2 = ps2.executeQuery();
                    while (rs2.next()) {
                        String field0 = rs2.getString(1);
                        String field1 = rs2.getString(2);
                        String field2 = rs2.getString(3);
                        exists = true;
                        logError(String.valueOf(procid),
                                  "There is an error in Branch Code " + field0 + " with ["+field1+"] "+
                                 sbuLevel + "  missing GL Prefixes PLEASE CORRECT to enable you continue with Depreciation processing.");
                    }
                }

            } catch (Exception ex) {
                System.out.println(
                        "WARNING: Error Check Prefixes ->" +
                        ex.getMessage());
            } finally {
                closeConnection(con, ps, rs);
            }
        } else {
            exists = true;
        }
        //System.out.println("prefixchk=====>" + notin.toString());
        closeConnection(con, ps, rs);
        return exists;
    }

    public String[] getCompanyInfo() {
        String[] result = new String[9];

        String query =
                "SELECT a.vat_rate,a.wht_rate,a.vat_account,a.wht_account,a.sbu_required" +
                ",a.sbu_level,a.pl_disposal_account,a.suspense_acct,b.req_prefix" +
                " FROM am_gb_company a,AM_AD_LEGACY_SYS_CONFIG b ";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                result[0] = rs.getString("vat_rate");
                result[1] = rs.getString("wht_rate");
                result[2] = rs.getString("vat_account");
                result[3] = rs.getString("wht_account");
                result[4] = rs.getString("sbu_required");
                result[5] = rs.getString("sbu_level");
                result[6] = rs.getString("pl_disposal_account");
                result[7] = rs.getString("suspense_acct");
                result[8] = rs.getString("req_prefix");
            }
        } catch (Exception e) {
            String warning = "WARNING:Error Fetching Company Details" +
                             " ->" + e.getMessage();
            System.out.println(warning);
        } finally {
            closeConnection(con, ps, rs);
        }
        closeConnection(con, ps, rs);
        return result;
    }

    public boolean checkMonthlyDep(int month, int year) {

        String sql =
                "select *  from monthly_depreciation_processing WHERE datepart(month,DEP_DATE)=" +
                month + " AND datepart(year,DEP_DATE)=" + year;
        boolean exists = false;
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
             con = getConnection("legendPlus");
             stmt = con.createStatement();
             rs = stmt.executeQuery(sql);
             exists = rs.next();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        closeConnection(con, stmt, rs);
        return exists;

    }

    private ArrayList findDistributedAsset() {

        String selectQuery =
                "SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID," +
                "CATEGORY_ID, DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
                "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
                "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
                "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION , " +
                "NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE   " +
                "FROM AM_ASSET  WHERE ASSET_ID IS NOT NULL AND req_redistribution='Y'";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList list = new ArrayList();

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String branchId = rs.getString("BRANCH_ID");
                String departmentId = rs.getString("DEPT_ID");
                String section = rs.getString("SECTION");
                String category = rs.getString("CATEGORY_ID");
                String description = rs.getString("DESCRIPTION");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                double depreciationRate = rs.getDouble("DEP_RATE");
                String assetMake = rs.getString("ASSET_MAKE");
                String assetUser = rs.getString("ASSET_USER");
                String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
                double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
                double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
                double cost = rs.getDouble("COST_PRICE");
                String depreciationEndDate = formatDate(rs.getDate(
                        "DEP_END_DATE"));
                double residualValue = rs.getDouble("RESIDUAL_VALUE");
                String postingDate = rs.getString("POSTING_DATE");
                String entryRaised = rs.getString("RAISE_ENTRY");
                double depreciationYearToDate = rs.getDouble("DEP_YTD");
                double nbv = rs.getDouble("NBV");
                int remainingLife = rs.getInt("REMAINING_LIFE");
                int totalLife = rs.getInt("TOTAL_LIFE");
                java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");

                Asset aset = new Asset(id, registrationNo, branchId,
                                       departmentId, section, category,
                                       description,
                                       datePurchased, depreciationRate,
                                       assetMake,
                                       assetUser, assetMaintenance,
                                       accumulatedDepreciation,
                                       monthlyDepreciation, cost,
                                       depreciationEndDate,
                                       residualValue, postingDate, entryRaised,
                                       depreciationYearToDate);
                aset.setNbv(nbv);
                aset.setRemainLife(remainingLife);
                aset.setTotalLife(totalLife);
                aset.setEffectiveDate(effectiveDate);
                list.add(aset);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching ALL Asset ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        closeConnection(con, ps, rs);
        return list;

    }

    public boolean findAssetWithoutValidBranches() {

        String selectQuery = "SELECT ASSET_ID,DESCRIPTION " +
                             " FROM AM_ASSET  WHERE asset_status='ACTIVE' AND BRANCH_ID NOT IN " +
                             "(SELECT BRANCH_ID FROM am_ad_branch WHERE BRANCH_STATUS='ACTIVE')";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        boolean exists = false;
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {
                String id = rs.getString("ASSET_ID");
                String desc = rs.getString("DESCRIPTION");
                logError(String.valueOf(procid),
                         "There is an error with Asset ["+desc+"] with asset Id " + id +
                         ". It does NOT BELONG TO A VALID BRANCH! PLEASE CORRECT to enable you continue with Depreciation processing.");
                exists = true;
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching ALL Asset ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        closeConnection(con, ps, rs);
        return exists;

    }

    public boolean findAssetWithoutValidCategory() {

        String selectQuery = "SELECT ASSET_ID,DESCRIPTION " +
                             " FROM AM_ASSET  WHERE asset_status='ACTIVE' AND CATEGORY_ID NOT IN " +
                             "(SELECT CATEGORY_ID FROM am_ad_category WHERE Category_Status = 'ACTIVE' )";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        boolean exists = false;
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {
                String id = rs.getString("ASSET_ID");
                String desc = rs.getString("DESCRIPTION");
                logError(String.valueOf(procid),
                         "There is an error with Asset ["+desc+"] with asset " + id +
                         " .It does NOT BELONG TO A VALID CATEGORY! PLEASE CORRECT to enable you continue with Depreciation processing..");
                exists = true;
            }

        } catch (Exception e) {
            System.out.println(
                    "INFO:Error check Assets without valid category ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        closeConnection(con, ps, rs);
        return exists;

    }

    public void logError(String processid, String description) {
        Connection con = null;
        PreparedStatement ps = null;
        String LOGERROR = "INSERT INTO am_depCheckErrorLog"
                          + " (ProcessID,ErrorDescription"
                          + " ,errordate)"
                          + " VALUES (?,?,?)";

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(LOGERROR);
            ps.setString(1, processid);
            ps.setString(2, description);
            ps.setDate(3, dateFormat.dateConvert(sdf.format(new java.util.Date())));
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("INFO:Error Logging Depreciation Error-> " +
                               e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        closeConnection(con, ps);
    }

    public boolean isNotDistAssetDistributed() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean exists = false;

        ArrayList list = new ArrayList();
        list = findDistributedAsset();
        String FINDER_QUERY = "SELECT DIST_ID,TYPE,STATUS,DIST_EXP_ACCT," +
                              "DIST_ACCUM_ACCT,VALUE_ASSIGNED,SEQUENCE_NO    " +
                              "FROM AM_DEPR_DIST  WHERE ASSET_ID = ?";

        try {
            con = getConnection("legendPlus");

            for (Iterator it = list.iterator(); it.hasNext(); ) {
                ps = con.prepareStatement(FINDER_QUERY);
                Asset asset = (Asset) it.next();
                ps.setString(1, asset.getId());
                rs = ps.executeQuery();
                boolean error = false;
                if (rs.next()) {
                    while (rs.next()) {
                        String desc = "";

                        String id = rs.getString("DIST_ID");
                        String type = rs.getString("TYPE");
                        String status = rs.getString("STATUS");
                        String expenseAccount = rs.getString("DIST_EXP_ACCT");
                        String accumAccount = rs.getString("DIST_ACCUM_ACCT");
                        double amount = rs.getDouble("VALUE_ASSIGNED");
                        String seq = rs.getString("SEQUENCE_NO");

                        if (expenseAccount.equalsIgnoreCase(null)) {
                            desc +=
                                    "Distributed Expense Account Number Is Empty.\n ";
                            error = true;
                        }
                        if (accumAccount.equalsIgnoreCase(null)) {
                            desc +=
                                    " Distributed Accumulated Depreciation Account Number Is Empty ";
                            error = true;
                        }
                        if (error) {
                            exists = true;
                            logError(String.valueOf(procid),
                                       "There is an error with Asset ["+asset.getDescription()+"] with asset Id " + asset.getId() +
                                     " Distributed ID " +
                                     id + " AND SEQUENCE NO " + seq +
                                     " has the errors " + desc +" PLEASE CORRECT to enable you continue with Depreciation processing..");
                        }
                    }
                } else {
                    logError(String.valueOf(procid),
                             "There is an error with Asset ["+asset.getDescription()+"] with asset Id " + asset.getId() +
                             " has no Distribution Entries PLEASE CORRECT to enable you continue with Depreciation processing..");
                    exists = true;
                }
            }
        } catch (Exception ex) {
            System.out.println(
                    "WARNING: Error findDistributionByAssetId ->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        closeConnection(con, ps, rs);
        return exists;

    }

    public ArrayList getDepreciationError() {

        String selectQuery =
                "SELECT ProcessID,ErrorDescription" +
                ",errordate,mtid" +
                " FROM am_depCheckErrorLog";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList list = new ArrayList();

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                String proid = rs.getString("ProcessID");
                String error = rs.getString("ErrorDescription");
                String errordate = rs.getString("errordate");
                String mtid = rs.getString("mtid");
                magma.net.dao.DepreciationError deper = new magma.net.dao.
                        DepreciationError();
                deper.setErrorDate(errordate);
                deper.setErrorDescription(error);
                deper.setProcessid(proid);
                deper.setMtid(mtid);
                list.add(deper);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching ALL Asset ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        closeConnection(con, ps, rs);
        return list;

    }

    public void clearErrors() {

        Connection con = null;
        PreparedStatement ps = null;
        String query = "DELETE FROM  am_depCheckErrorLog";
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            // ps.setString(1, processid);
            ps.execute();
        } catch (Exception e) {
            System.out.println(
                    "INFO:Error Clearing Depreciation errors table-> " +
                    e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        closeConnection(con, ps);
    }

    public void clearDepreciationTemp() {

        Connection con = null;
        PreparedStatement ps = null;
        String query = "DELETE FROM  depreciation_entry_temp";
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            // ps.setString(1, processid);
            ps.execute();
        } catch (Exception e) {
            System.out.println(
                    "INFO:Error Clearing Error depreciation temp entries-> " +
                    e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        closeConnection(con, ps);
    }

    public String[] getLegacySystemDetail() {
        String SELECT_QUERY = "SELECT app_name,version,client_name,req_accttype," +
        		" req_trancode, req_prefix, process_date FROM AM_AD_LEGACY_SYS_CONFIG ";
        String[] result = new String[7];

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(SELECT_QUERY);
            rs = ps.executeQuery();
            while (rs.next()) {
                result[0] = rs.getString("app_name");
                result[1] = rs.getString("version");
                result[2] = rs.getString("client_name");
                result[3] = rs.getString("req_accttype");
                result[4] = rs.getString("req_trancode");
                result[5] = rs.getString("req_prefix");
                result[6] = formatDate(rs.getDate("process_date"));
            }
        } catch (Exception e) {
            e.getMessage();
        } finally {
            closeConnection(con, ps, rs);
        }
        closeConnection(con, ps, rs);
        return result;
    }

    public boolean insertTempDepreciationEntry() {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        String sburequired = getCompanyInfo()[4];
        String query = "";
        String sbuLevel = "";
        boolean exists = false;
        if (sburequired.equalsIgnoreCase("Y")) {
            sbuLevel = getCompanyInfo()[5];
//            System.out.println("======>sbuLevel: "+sbuLevel);
            if (sbuLevel.equalsIgnoreCase("Department")) {
 //           	System.out.println("======>1<======: ");
                query = " Insert into depreciation_entry_temp"
                        + " select distinct a.branch_id,b.branchcode,"
                        + " a.category_id, c.acct_type,"
                        + " b.gl_prefix + c.dep_ledger ,"
                        + " b.gl_prefix + c.accum_dep_ledger ,"
                        + " c.currency_id, 0 Amount,'N',"
                        +
                        " Cast(a.branch_id as varchar(5))+Cast(a.dept_id as varchar(5))"
                        +
                        " from am_asset a, sbu_branch_dept b,am_ad_category c "
                        +
                        " where a.branch_id = b.branchid AND a.dept_id = b.deptid "
                        + " and a.category_id = c.category_id "
                        + " and a.req_depreciation = 'Y' "
                        + " and a.asset_status = 'ACTIVE'"
                        +
                        " and a.effective_date < (select next_processing_date from am_gb_company)"
                        +
                        " and a.NBV != c.residual_value and a.req_redistribution != 'Y'";

            }
            if (sbuLevel.equalsIgnoreCase("Sector/Units")) {
//            	System.out.println("======>2<======: ");
                query =
                        "Insert into depreciation_entry_temp"
                        + " select distinct a.branch_id,b.branchcode,"
                        + " a.category_id,  c.acct_type,"
                        + " b.gl_prefix + c.dep_ledger ,"
                        + " b.gl_prefix + c.accum_dep_ledger,"
                        +
                        " c.currency_id,    0 Amount,'N',Cast(a.branch_id as varchar(5))+"
                        + "Cast(a.dept_id as varchar(5))+"
                        + "Cast(a.section_id as varchar(5))"
                        +
                        " from am_asset a, sbu_dept_section b,am_ad_category c"
                        +
                        " where a.branch_id = b.branchid AND a.dept_id = b.deptid"
                        + " and a.section_id = b.sectionid "
                        + " and a.category_id = c.category_id "
                        +
                        " and a.req_depreciation = 'Y' and a.asset_status = 'ACTIVE'"
                        +
                        " and a.effective_date < (select next_processing_date from am_gb_company)"
                        +
                        " and a.NBV != c.residual_value and a.req_redistribution != 'Y'";

            }
        } else {
//        	System.out.println("======>3<======: ");
            query =
                    "Insert into depreciation_entry_temp "
                    + " select distinct a.branch_id,b.branch_code,"
                    + "a.category_id,c.acct_type,"
                    + "b.gl_prefix + c.dep_ledger ,"
                    + "b.gl_prefix + c.accum_dep_ledger ,"
                    +
                    "c.currency_id,0 Amount,'N',Cast(a.branch_id as varchar(5))"
                    + " from am_asset a, am_ad_branch b,am_ad_category c"
                    + " where a.branch_id = b.branch_id"
                    + " and a.category_id = c.category_id"
                    +
                    " and a.req_depreciation = 'Y' and a.asset_status = 'ACTIVE'"
                    +
                    " and a.effective_date < (select next_processing_date from am_gb_company)"
                    +
                    " and a.NBV != c.residual_value and a.req_redistribution != 'Y'";

        }
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            exists = ps.execute();
 //           System.out.println("======>exists<======: "+exists);
        } catch (Exception ex) {
            System.out.println(
                    "WARNING: Error Inserting Asset into temp dep entry ->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        //System.out.println("prefixchk=====>" + notin.toString());
        closeConnection(con, ps, rs);
        return exists;
    }

    public void updateDepTemp(Asset aset, double amount,int m) {
        Connection con = null;
        PreparedStatement ps = null;
        String sburequired = getCompanyInfo()[4];
        String sbuLevel = "";
        String SLIPT_QUERY = "UPDATE depreciation_entry_temp"
                             +
                             " SET Amount = Amount + ?  WHERE tblid = ? ";
        if(aset.getReq_distribution().equalsIgnoreCase("Y")){
            updateDepTempDistributed( aset, amount,m);
        }
        else{
            try {
                con = getConnection("legendPlus");
                ps = con.prepareStatement(SLIPT_QUERY);
                ps.setDouble(1, amount);
                if (sburequired.equalsIgnoreCase("Y")) {
                    sbuLevel = getCompanyInfo()[5];

                    if (sbuLevel.equalsIgnoreCase("Department")) {
                        ps.setString(2,
                                     aset.getBranchId() + aset.getDepartmentId());
                    }
                    if (sbuLevel.equalsIgnoreCase("Sector/Units")) {
                        ps.setString(2,
                                     aset.getBranchId() + aset.getDepartmentId() +
                                     aset.getSection());
                    }
                } else {
                    ps.setString(2, aset.getBranchId());
                }

                ps.executeUpdate();

            } catch (Exception ex) {
                System.out.println(
                        "WARNING: Error updating temp dep entries ->" +
                        ex.getMessage());
            } finally {
                closeConnection(con, ps);
            }
            closeConnection(con, ps);
        }
    }

    public void TransfertoEntrytable(String userid) {
        long batchid = (long) System.currentTimeMillis();

        String query = "SELECT branch_id ,branchcode,category_id"
                       + ",acct_type,Debit_Acct_No"
                       + ",Credit_Acct_No,currency_id,Amount,tblid"
                       + " FROM depreciation_entry_temp";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
                "dd-MM-yyyy");
        java.text.SimpleDateFormat sdf2 = new java.text.SimpleDateFormat(
                "MMMM, yyyy");
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        DepreciationProcessingManager dpm = new DepreciationProcessingManager();
        try {
            //String[][] users = new String[1][20];
            legend.admin.objects.User users = new legend.admin.handlers.SecurityHandler_07_11_2024().getUserByUserID(userid);
            

            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            ProcesingInfo pinfo = dpm.getProcessingInfo();
            while (rs.next()) {
                String branchcode = rs.getString("branchcode");
                String catId = rs.getString("category_id");
                String accttype = rs.getString("acct_type");
                String drno = rs.getString("Debit_Acct_No");
                String crno = rs.getString("Credit_Acct_No");
                String currid = rs.getString("currency_id");
                double amount = rs.getDouble("Amount");
                String tblid = rs.getString("tblid");
                dpm = new DepreciationProcessingManager();
                String narration = "Being Depreciation For " +
                                   sdf2.format(pinfo.getProcessingDate());
                dpm.logRaisedEntryTransaction(drno, accttype, "",
                                              narration, crno, accttype,
                                              "", narration,
                                              String.valueOf(amount), userid,
                                              "0",
                                              users.getLegacySystemId(), "DP" + batchid, "",
                                              currid,
                                              "");

            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("INFO:Error Transfering NON distributed asset to Entry Proper->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        closeConnection(con, ps, rs);
    }

    public void archiveEntrytable() {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        String query = "INSERT INTO am_entry_table_archive "
                       + " SELECT *  FROM am_entry_table";
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            ps.execute();

        } catch (Exception ex) {
            System.out.println(
                    "WARNING: Error Archiving entries table->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps,rs);
        }
        //System.out.println("prefixchk=====>" + notin.toString());
        closeConnection(con, ps, rs);
        //return exists;
    }

    public void clearEntrytable() {

        Connection con = null;
        PreparedStatement ps = null;
        String query = "DELETE FROM  am_entry_table";
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            // ps.setString(1, processid);
            ps.execute();
        } catch (Exception e) {
            System.out.println(
                    "INFO:Error Clearing Error entries-> " +
                    e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        closeConnection(con, ps);
    }

    public void insertTempDistributedAssets() {
        Connection con = null;
        PreparedStatement ps = null;
        String query = "Insert into depreciation_entry_temp"
                       + "(Debit_Acct_No, branch_id,branchcode, category_id,"
                       + "acct_type, Credit_Acct_No,"
                       + "currency_id, Amount, dep_dist,tblid)"
                       + " select distinct d.DIST_EXP_ACCT,"
                       + " a.branch_id, b.branch_code,"
                       + "a.category_id, c.acct_type,"
                       + "d.DIST_ACCUM_ACCT, c.currency_id,0, 'Y',"
                       + "0"
                       + " from am_asset a, am_ad_branch b, am_ad_category c,"
                       + " AM_DEPR_DIST d "
                       + " where a.branch_id = b.branch_id"
                       + " and a.category_id = c.category_id"
                       + " and a.asset_id = d.asset_id"
                       + " and a.req_depreciation = 'Y'"
                       + " and a.asset_status = 'ACTIVE'"
                       + " and a.effective_date < "
                       + " (select next_processing_date from"
                       + "  am_gb_company)"
                       + " and a.NBV != c.residual_value"
                       + " and a.req_redistribution = 'Y'";

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("INFO:Error inserting dist Asset into temp dep entry-> " +
                               e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        closeConnection(con, ps);
    }

    public void updateDepTempDistributed(Asset aset, double amount,int m) {
        Connection con = null;
        PreparedStatement ps = null;
        String SLIPT_QUERY = "UPDATE depreciation_entry_temp"
                             +
                             " SET Amount = Amount + ?  WHERE Debit_Acct_No = ? AND dep_dist='Y'";
        magma.net.vao.DistributionDetail dd = null;
        DepreciationProcessingManager dpm = new DepreciationProcessingManager();
        java.util.ArrayList _list = dpm.findDistributionByAssetId(aset.getId());
        try {

            con = getConnection("legendPlus");
            for (int i = 0; i < _list.size(); i++) {
                dd = (magma.net.vao.DistributionDetail) _list.get(i);
                ps = con.prepareStatement(SLIPT_QUERY);
                double ramount = 0;
                if (dd.getType().equalsIgnoreCase("PERCENTAGE")) {
                    ramount = dd.getAmount() / 100.0 * amount;
                } else {
                    if (i < _list.size()-1) {
                        ramount = dd.getAmount()*m;
                        amount = amount - ramount;
                    } else {
                        ramount = amount;
                    }
                }
                ps.setDouble(1, ramount);
                ps.setString(2, dd.getExpenseAccount());
                ps.executeUpdate();
            }
        } catch (Exception ex) {
            System.out.println(
                    "WARNING: Error temp depreciation table with distributed asset ->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        closeConnection(con, ps);
    }

    public ArrayList getUploadError(String tranType) {

        String selectQuery =
                "SELECT TRANSACTION_TYPE,ERRORDESCRIPTION" +
                ",ERRORDATE,MTID" +
                " FROM am_uploadCheckErrorLog where TRANSACTION_TYPE like '%"+tranType+"%' ";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList list = new ArrayList();

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                String transactiontype = rs.getString("TRANSACTION_TYPE");
                String error = rs.getString("ERRORDESCRIPTION");
                String errordate = rs.getString("ERRORDATE");
                String mtid = rs.getString("MTID");
                magma.net.dao.UploadError deper = new magma.net.dao.
                        UploadError();
                deper.setErrorDate(errordate);
                deper.setErrorDescription(error);
                deper.setTransactiontype(transactiontype);
                deper.setMtid(mtid);
                list.add(deper);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching ALL UploadAsset ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        closeConnection(con, ps, rs);
        return list;

    }

    public ArrayList getBatchPostingError(String tranType, String groupid, String userid) {

        String selectQuery =
                "SELECT ID,TRANSACTION_TYPE,BATCH_NO,SEQUENCE_NO,ERROR_CODE,ACCOUNT_NO,ERROR_MSG" +
                " FROM AM_GB_POSTING_EXCEPTION where TRANSACTION_TYPE like '%"+tranType+"%' and GROUP_ID = ? and USER_ID = ?";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList list = new ArrayList();

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            ps.setString(1, groupid);
            ps.setString(2, userid);
            rs = ps.executeQuery();

            while (rs.next()) {

                String transactiontype = rs.getString("TRANSACTION_TYPE");
                String errorMessage = rs.getString("ERROR_MSG");
                String batchNo = rs.getString("BATCH_NO");
                String sequenceNo = rs.getString("SEQUENCE_NO");
                String accuntNo = rs.getString("ACCOUNT_NO");
                String errorCode = rs.getString("ERROR_CODE");
                String mtid = rs.getString("ID");
                magma.net.dao.UploadError deper = new magma.net.dao.
                        UploadError();
                deper.setBatchNo(batchNo);
                deper.setErrorDescription(errorMessage);
                deper.setSequenceNo(sequenceNo);
                deper.setErrorCode(errorCode);
                deper.setAccountNo(accuntNo);
                deper.setTransactiontype(transactiontype);
                deper.setMtid(mtid);
                list.add(deper);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching ALL UploadAsset ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        closeConnection(con, ps, rs);
        return list;

    }

}
