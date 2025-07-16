package legend;

import java.sql.*;

import magma.*;
import magma.net.manager.RaiseEntryManager;
import java.util.*;

/**
 *
 * <p>Title: Recalculate.java</p>
 *
 * <p>Description: Performs the depreciation reclculation processes</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Magbel Technologies LTD</p>
 *
 * @author Rahman Oloritun
 * @version 1.0
 */

public class RecalculateDep extends ConnectionClass {
    private double amount;
    RaiseEntryManager rem = new RaiseEntryManager();
    String option = null;
    String drNarration = "Adjustment entry iro Depreciation Rate Change";
    String crNarration = "Adjustment entry iro Depreciation Rate Change";
    // Connection con = getConnection();
    /**
     *
     * @throws Exception
     */
    public RecalculateDep() throws Exception {
        //  con.setAutoCommit(false);
    	freeResource();
    }

    /**
     *
     * @param oldrate String
     * @param newrate String
     * @param accumdep String
     * @return double
     */
    public double calculateDepValue(String oldrate, String newrate,
                                    String accumdep) {
        double oldr = Double.parseDouble(oldrate);
        double newr = Double.parseDouble(newrate);
        double accum = Double.parseDouble(accumdep);
        double newacc = 0.0;
        System.out.println("===oldr: "+oldr+"   newr: "+newr+"   accum: "+accum);
        if (newr == oldr) {
            newacc = 0.0;
            option = "E";
        } else if (newr > oldr) {
            newacc = (((newr - oldr) / oldr) + 1) * accum;
            amount = newacc - accum;
            option = "G";
        } else if (newr < oldr) {
            newacc = (1 - ((oldr - newr) / oldr)) * accum;
            amount = accum - newacc;
            option = "L";
        }
        return newacc;
    }

    /**
     *
     * @param catid String
     * @param newrate String
     * @param userid String
     * @param rd String
     * @param uea String
     * @throws Throwable
     */
    public void updateAsset(String catid, String oldrate, String newrate,
                            String userid,
                            String rd, String uea, String ccode) throws
            Throwable {

        if (uea.equalsIgnoreCase("Y") && !rd.equalsIgnoreCase("Y")) {
        	System.out.println("=====uea 1===:"+uea+"    rd: "+rd+"  catid: "+catid+"  oldrate: "+oldrate+"  newrate: "+newrate+"  ccode: "+ccode);
            updateAssetDep(catid, oldrate, newrate, userid, rd, uea, ccode);
            
        }
        if (!uea.equalsIgnoreCase("Y") && !rd.equalsIgnoreCase("Y")) {
        	System.out.println("=====uea 2===:"+uea+"    rd: "+rd+"  catid: "+catid+"  oldrate: "+oldrate+"  newrate: "+newrate+"  ccode: "+ccode);
            if (!recentDephistoryExist(newrate, catid)) {
                insertIntoDeprateHistory(catid, oldrate, newrate, userid, rd,
                                         uea, ccode);
            }
        }

        if (!uea.equalsIgnoreCase("Y") && rd.equalsIgnoreCase("Y")) {
        	System.out.println("=====uea 3===:"+uea+"    rd: "+rd+"  catid: "+catid+"  oldrate: "+oldrate+"  newrate: "+newrate+"  ccode: "+ccode);
            updateAssetRecalDep(catid, oldrate, newrate, userid, rd, uea, ccode);
        }
        if (uea.equalsIgnoreCase("Y") && rd.equalsIgnoreCase("Y")) {
        	System.out.println("=====uea 4===:"+uea+"    rd: "+rd+"  catid: "+catid+"  oldrate: "+oldrate+"  newrate: "+newrate+"  ccode: "+ccode);
            updateAssetRecalDep(catid, oldrate, newrate, userid, rd, uea, ccode);
        }

    }

    /**
     *
     * @param catid String
     * @param newrate String
     * @throws Throwable
     */


    public void updateAssetDep(String catid, String oldrate, String newrate,
                               String userid,
                               String rd, String uea, String ccode) throws
            Throwable {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
        if (!recentDephistoryExist(newrate, catid)) {
            insertIntoDeprateHistory(catid, oldrate, newrate, userid, rd, uea,
                                     ccode);
        } else {
        	System.out.println("=====uea B1===:"+uea+"    rd: "+rd+"  catid: "+catid+"  oldrate: "+oldrate+"  newrate: "+newrate+"  ccode: "+ccode);
            UpdateDeprateHistory(catid, oldrate, newrate, userid, rd, uea,
                                 ccode);
        }
        con = getConnection();
        String query =
                "SELECT Asset_id, Accum_dep,Cost_Price, useful_life, dep_rate FROM am_ASSET " +
                "WHERE Category_ID = " + catid +
                "  AND Asset_Status NOT IN ('C', 'D')";
//        ResultSet rs = getStatement().executeQuery(query);
		ps = con.prepareStatement(query);
		rs = ps.executeQuery();
        // getConnection().setAutoCommit(false);
        //getConnection().
        /*  String query2 = "SELECT DEP_RATE FROM AM_AD_CATEGORY " +
                          "WHERE CATEGORY_ID = " + catid;
          ResultSet rs2 = getStatement().executeQuery(query2);
          rs2.next();
          String oldrate = rs2.getString("dep_rate");*/
        while (rs.next()) {
//        	System.out.println("========ASSET ID: "+rs.getString("Asset_id")+"   newrate: "+newrate);
            query = "UPDATE am_ASSET SET dep_rate = " + newrate +
                    " WHERE Asset_id = '" + rs.getString("Asset_id") + "'";
//            getStatement().executeUpdate(query);
    		ps = con.prepareStatement(query);
    		int i = ps.executeUpdate();            
        }
        updateGAssetDep(catid, oldrate, newrate, userid, rd, uea, ccode);

//        freeResource();
    }

    public void updateGAssetDep(String catid, String oldrate, String newrate,
                                String userid,
                                String rd, String uea, String ccode) throws
            Throwable {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
        java.text.SimpleDateFormat sd = new java.text.SimpleDateFormat(
                "dd/MM/yyyy");
        con = getConnection();
        String query =
                "SELECT * FROM am_group_asset " +
                "WHERE Category_ID = " + catid;    
//        System.out.println("========query in updateGAssetDep: "+query);
//        ResultSet rs = getStatement().executeQuery(query);		
		ps = con.prepareStatement(query);
		rs = ps.executeQuery();
//        System.out.println("========newrate in updateGAssetDep: "+newrate+"    oldrate: "+oldrate+"    catid: "+catid);
        while (rs.next()) {
 //       	 System.out.println("========AssetId in updateGAssetDep: "+rs.getString("Asset_id"));
            String depend = getDepEndDate(newrate + "," +
                                          sd.format(rs.getDate(
                                                  "effective_date")));

            query = "UPDATE am_group_asset SET dep_rate = " + newrate +
                    ", DEP_END_DATE='" + depend + "'" +
                    " WHERE Asset_id = '" + rs.getString("Asset_id") + "'";
//            getStatement().executeUpdate(query);
    		ps = con.prepareStatement(query);
    		int i = ps.executeUpdate();
        }
//        freeResource();
    }

    /**
     *
     * @param catid String
     * @param newrate String
     * @throws Throwable
     */
    public void updateAssetRecalDep(String catid, String oldrate,
                                    String newrate, String userid,
                                    String rd, String uea, String ccode) throws
            Throwable {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ResultSet rx = null;
		ResultSet rxs = null;   
		ResultSet rsw = null; 
		Statement stmt = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		ResultSet rs4 = null;
		ResultSet rs5 = null;
		con = getConnection();
        if (!recentDephistoryExist(newrate, catid)) {

            insertIntoDeprateHistory(catid, oldrate, newrate, userid, rd,
                                     uea, ccode);
        } else {
            UpdateDeprateHistory(catid, oldrate, newrate, userid, rd, uea,
                                 ccode);
        }
        updateGAssetDep(catid, oldrate, newrate, userid, rd, uea, ccode);
        java.text.SimpleDateFormat sd = new java.text.SimpleDateFormat(
                "dd/MM/yyyy");
        java.text.SimpleDateFormat sd2 = new java.text.SimpleDateFormat(
                "MM-dd-yyyy");
        String xquery = "SELECT * FROM AM_AD_LEGACY_SYS_CONFIG";
		ps = con.prepareStatement(xquery);
		rxs = ps.executeQuery();
//       rxs = getStatement().executeQuery(xquery);
        rxs.next();
        // rem.getLegacySystemDate()
        String queryx = "SELECT effective_date FROM am_dep_rate_history WHERE mtid = (SELECT MAX(mtid) FROM am_dep_rate_history " +
                        " WHERE category_ID = " + catid + ")";
		ps = con.prepareStatement(queryx);
		rx = ps.executeQuery();
//        rx = getStatement().executeQuery(queryx);
        rx.next();

        String queryw = "SELECT next_processing_date FROM am_gb_company";
		ps = con.prepareStatement(queryw);
		rsw = ps.executeQuery();        
//        ResultSet rsw = getStatement().executeQuery(queryw);
        rsw.next();

        if (rx.getDate("effective_date").before(rsw.getDate(
                "next_processing_date")) ||
            rx.getDate("effective_date").equals(rsw.getDate(
                    "next_processing_date"))) {

            //RemoteMethods rm = new RemoteMethods();
            double newacc;
            double depytd = 0.0;
            double depytd2 = 0.0;
            /* String oldrate = rs2.getString("dep_rate");*/
//            Statement stmt = getStatement();
            con = getConnection();
            stmt = con.createStatement();
            String query = "SELECT * FROM am_ASSET " +
                           "WHERE Category_ID = " + catid +
                           "  AND Asset_Status NOT IN ('C', 'D')";

    		ps = con.prepareStatement(query);
    		rs = ps.executeQuery();
//             rs = getStatement().executeQuery(query);

            /*String queryg = "SELECT * FROM am_group_asset " +
                            "WHERE Category_ID = " + catid;
                         ResultSet rsg = getStatement().executeQuery(queryg);*/

            while (rs.next()) {

                newacc = calculateDepValue(oldrate, newrate,
                                           rs.getString("Accum_dep"));
                System.out.println("=====>newacc from calculateDepValue: "+newacc+"   oldrate: "+oldrate+"  newrate: "+newrate);
                if (newacc != 0.0) {
                    if (option.equals("G")) {
                        depytd = rs.getDouble("dep_ytd") + amount;
                    } else if (option.equals("L")) {
                        depytd = rs.getDouble("dep_ytd") - amount;
                    }
                    String depend = getDepEndDate(newrate + "," +
                                                  sd.format(rs.getDate(
                            "effective_date")));

                    query = "UPDATE am_ASSET SET dep_rate = " + newrate +
                            ", DEP_END_DATE='" + depend + "'," +
                            "total_life=" + totalInstallment(newrate) + "," +
                            "Accum_dep = " + newacc + "," +
                            "NBV = " + (rs.getDouble("Cost_Price") - newacc) +
                            "," +
                            "remaining_life =" +
                            ((totalInstallment(newrate) -
                              (double) rs.getInt("useful_life"))) +
                            ", dep_ytd = " + depytd +
                            "  WHERE Asset_id = '" + rs.getString("Asset_id") +
                            "'";
                    // System.out.println(query);
                    stmt.addBatch(query);

                    String pquery = "INSERT INTO AM_DEPRECIATION_TRAN" +
                                    "(ASSET_ID, BRANCH_ID, AMOUNT, TRAN_DATE, DEPT_ID," +
                                    "SECTION_ID,[USER_ID],ENTRY_DATE, NEWRATE_STATUS)" +
                                    " VALUES('" +
                                    rs.getString("Asset_id") + "'," +
                                    rs.getInt("branch_id") + "," +
                                    amount + ",'" +
                                    rxs.getString("process_date") + "'," +
                                    rs.getInt("dept_id") + "," +
                                    rs.getInt("section_id") + "," +
                                    userid + "," +
                                    "GETDATE(),'" +
                                    option + "')";
///                    System.out.println("pquery>>>>>: "+pquery);
                    stmt.addBatch(pquery);
                    stmt.executeBatch();
                } else {
                    query = "UPDATE am_ASSET SET dep_rate = " + newrate +
                            " WHERE Asset_id = '" + rs.getString("Asset_id") +
                            "'";
//                    getStatement().executeUpdate(query);
            		ps = con.prepareStatement(query);
            		int i = ps.executeUpdate();

                }
            } while (rs.next()) {

                newacc = calculateDepValue(oldrate, newrate,
                                           rs.getString("Accum_dep"));
                if (newacc != 0.0) {
                    if (option.equals("G")) {
                        depytd = rs.getDouble("dep_ytd") + amount;
                    } else if (option.equals("L")) {
                        depytd = rs.getDouble("dep_ytd") - amount;
                    }
                    String depend = getDepEndDate(newrate + "," +
                                                  sd.format(rs.getDate(
                            "effective_date")));

                    query = "UPDATE am_ASSET SET dep_rate = " + newrate +
                            ", DEP_END_DATE='" + depend + "'," +
                            "total_life=" + totalInstallment(newrate) + "," +
                            "Accum_dep = " + newacc + "," +
                            "NBV = " + (rs.getDouble("Cost_Price") - newacc) +
                            "," +
                            "remaining_life =" +
                            ((totalInstallment(newrate) -
                              (double) rs.getInt("useful_life"))) +
                            ", dep_ytd = " + depytd +
                            "  WHERE Asset_id = '" + rs.getString("Asset_id") +
                            "'";
                    // System.out.println(query);
                    stmt.addBatch(query);

                    String pquery = "INSERT INTO AM_DEPRECIATION_TRAN" +
                                    "(ASSET_ID, BRANCH_ID, AMOUNT, TRAN_DATE, DEPT_ID," +
                                    "SECTION_ID,[USER_ID],ENTRY_DATE, NEWRATE_STATUS)" +
                                    " VALUES('" +
                                    rs.getString("Asset_id") + "'," +
                                    rs.getInt("branch_id") + "," +
                                    amount + ",'" +
                                    rxs.getString("process_date") + "'," +
                                    rs.getInt("dept_id") + "," +
                                    rs.getInt("section_id") + "," +
                                    userid + "," +
                                    "GETDATE(),'" +
                                    option + "')";
                    //System.out.println(pquery);
                    stmt.addBatch(pquery);
                    stmt.executeBatch();
                } else {
                    query = "UPDATE am_ASSET SET dep_rate = " + newrate +
                            " WHERE Asset_id = '" + rs.getString("Asset_id") +
                            "'";
//                    getStatement().executeUpdate(query);
            		ps = con.prepareStatement(query);
            		int i = ps.executeUpdate();
//            		rs = ps.executeQuery();

                }
            }

            String query2 = "SELECT * FROM AM_AD_CATEGORY " +
                            "WHERE CATEGORY_ID = " + catid;
    		ps = con.prepareStatement(query2);
    		rs2 = ps.executeQuery();   
//            ResultSet rs2 = getStatement().executeQuery(query2);
            rs2.next();
            String query3 = "SELECT TRAN_CODE FROM AM_AD_TRAN_CODE " +
                            "WHERE DEBIT_CREDIT = 'CR' AND GEN_ACCT_TYPE='GL'";
    		ps = con.prepareStatement(query3);
    		rs3 = ps.executeQuery();  
//            ResultSet rs3 = getStatement().executeQuery(query3);
            rs3.next();
            String query4 = "SELECT TRAN_CODE FROM AM_AD_TRAN_CODE " +
                            "WHERE DEBIT_CREDIT = 'DR' AND GEN_ACCT_TYPE='GL'";
    		ps = con.prepareStatement(query4);
    		rs4 = ps.executeQuery();  
//            ResultSet rs4 = getStatement().executeQuery(query4);
            rs4.next();

            ResultSet sum = getSUMforLedgers(userid);
            // if(sum!=null)
            //{
            while (sum.next()) {
                String dep_ledger = rem.processGLAccount(rs2.getInt(
                        "CATEGORY_ID"), sum.getInt(1), "DEP");
                String accum_dep = rem.processGLAccount(rs2.getInt(
                        "CATEGORY_ID"), sum.getInt(1), "ACCUM_DEP");
                String batchid = "GL" + rem.getMaxNum(userid);
                String acctype, crtrancode, drtrancode;
                if (rxs.getString("req_accttype").equals("Y")) {
                    acctype = "4";
                } else {
                    acctype = "";
                }
                if (rxs.getString("req_trancode").equals("Y")) {
                    drtrancode = rs4.getString("TRAN_CODE");
                    crtrancode = rs3.getString("TRAN_CODE");
                } else {
                    drtrancode = "";
                    crtrancode = "";
                }

                if (option.equalsIgnoreCase("G")) {
                    rem.raiseEntry(dep_ledger, accum_dep, acctype, acctype,
                                   drtrancode,
                                   crtrancode,
                                   drNarration + " " + oldrate + " to " +
                                   newrate,
                                   crNarration + " " + oldrate + " to " +
                                   newrate
                                   , sum.getDouble(2), userid, batchid,
                                   sd.format(new java.util.Date()), "");
                } else if (option.equalsIgnoreCase("L")) {
                    rem.raiseEntry(accum_dep, dep_ledger, acctype, acctype,
                                   drtrancode,
                                   crtrancode,
                                   crNarration + " " + oldrate + " to " +
                                   newrate,
                                   drNarration + " " + oldrate + " to " +
                                   newrate,
                                   sum.getDouble(2), userid, batchid,
                                   sd.format(new java.util.Date()), "");
                }

            }
            String updquery = "UPDATE AM_DEPRECIATION_TRAN SET TRAN_STATUS='P'" +
                              "  WHERE USER_ID=" + userid +
                              " AND TRAN_STATUS='U' ";
//            getStatement().executeUpdate(updquery);
    		ps = con.prepareStatement(updquery);
    		int i = ps.executeUpdate();
    		
        }
        updateGAssetDep(catid, oldrate, newrate, userid, rd, uea, ccode);
        freeResource();
    }

    /**
     *
     * @param newrate String
     * @return int
     */
    public double totalInstallment(String newrate) {
        double newr = Double.parseDouble(newrate);
        return (100.0 / newr) * 12.0;
    }

    /**
     *
     * @param newrate String
     * @param catid String
     * @return boolean
     * @throws Throwable
     */
    public boolean recentDephistoryExist(String newrate, String catid) throws
            Throwable {
        String query = "SELECT * FROM am_dep_rate_history " +
                       " WHERE new_rate = " + newrate +
                       " AND recalculate_dep = 'N'" +
                       " AND mtid = (SELECT MAX(mtid) FROM am_dep_rate_history " +
                       " WHERE category_ID = " + catid + ")";

        ResultSet rs = getStatement().executeQuery(query);
        boolean result = rs.next();
        freeResource();
        return result;

    }

    /**
     *
     * @param newrate String
     * @param catid String
     * @return ResultSet
     * @throws Throwable
     */

    public ResultSet getRecentDephistoryRec(String newrate, String catid) throws
            Throwable {
        String query = "SELECT * FROM am_dep_rate_history " +
                       " WHERE new_rate = " + newrate +
                       " AND recalculate_dep = 'N'" +
                       " AND mtid = (SELECT MAX(mtid) FROM am_dep_rate_history " +
                       " WHERE category_ID = " + catid + ")";

        ResultSet rs = getStatement().executeQuery(query);
        // boolean result = rs.next();
        // freeResource();
        // return result;
        return rs;
    }

    /**
     *
     * @param catid String
     * @param oldrate String
     * @param newrate String
     * @param userid String
     * @param rd String
     * @param uea String
     * @return boolean
     * @throws Throwable
     */

    public boolean insertIntoDeprateHistory(String catid, String oldrate,
                                            String newrate, String userid,
                                            String rd, String uea, String ccode) throws
            Throwable {
        String query = "SELECT process_date FROM AM_AD_LEGACY_SYS_CONFIG";
        ResultSet rs = getStatement().executeQuery(query);
        boolean result = false;

        if (rs.next()) {
            query =
                    "INSERT INTO am_dep_rate_history (category_ID,[category_code],old_rate," +
                    " new_rate, effective_date, entry_date, userid, recalculate_dep, " +
                    "update_asset ) " +
                    " VALUES("
                    + catid + ",'"
                    + ccode + "',"
                    + oldrate + ","
                    + newrate + ",'"
                    + rs.getString("process_date")
                    + "', GetDate() ,"
                    + userid + ",'"
                    + rd + "','"
                    + uea + "')";
            result = getStatement().executeUpdate(query)!=-1;

        }
        freeResource();
        return result;
    }


    /**
     *
     * @param catid String
     * @param oldrate String
     * @param newrate String
     * @param userid String
     * @param rd String
     * @param uea String
     * @return boolean
     * @throws Throwable
     */
    public boolean UpdateDeprateHistory(String catid, String oldrate,
                                        String newrate, String userid,
                                        String rd, String uea, String ccode) throws
            Throwable {
        String query = "SELECT process_date FROM AM_AD_LEGACY_SYS_CONFIG";
        ResultSet rs = getStatement().executeQuery(query);
        boolean result = false;
		Connection con = null;
		PreparedStatement ps = null;
        //ResultSet rs2 = getRecentDephistoryRec(newrate, catid);
		con = getConnection();
        if (rs.next()
                /*&& rs2.getString("new_rate").equalsIgnoreCase(newrate)*/) {
            query = "UPDATE am_dep_rate_history SET "
                    + "category_ID =" + catid + ","
                    + "[category_code]='" + ccode + "',"
                    + "old_rate = " + oldrate + ","
                    + "new_rate = " + newrate + ","
                    + "effective_date = '" + rs.getString("process_date")
                    + "', entry_date = GetDate() ,"
                    + " userid = " + userid + ","
                    + "recalculate_dep = '" + rd + "',"
                    + "update_asset = '" + uea + "'"
                    +
                    " WHERE mtid = (SELECT MAX(mtid) FROM am_dep_rate_history " +
                    " WHERE category_ID = " + catid + " AND new_rate = " +
                    newrate + ")";
    		ps = con.prepareStatement(query);
    		result = ps.execute();
//            result = getStatement().execute(query);

        }
        freeResource();
        return result;
    }

    public String[] getRecentDephistoryRecord(String catid, String newrate) throws
            Throwable {
        String query = "SELECT * FROM am_dep_rate_history " +
                       " WHERE new_rate = " + newrate +
                       " AND recalculate_dep = 'N'" +
                       " AND mtid = (SELECT MAX(mtid) FROM am_dep_rate_history " +
                       " WHERE category_ID = " + catid + ")";

        ResultSet rs = getStatement().executeQuery(query);
        String[] result = new String[2];
        while (rs.next()) {
            result[0] = rs.getString("recalculate_dep");
            result[1] = rs.getString("update_asset");
        }
        // boolean result = rs.next();
        freeResource();
        return result;

    }

    public ResultSet getSUMforLedgers(String userid) throws Throwable {
        String[] compInfo = rem.getCompanyInfo();
        String sbuRequired = compInfo[4].trim();
        String sbuLevel = "";
//        System.out.println("======compInfo[5] whice is SBU LEVEL: "+compInfo[5]);
        if(compInfo[5]!=null) {sbuLevel = compInfo[5].trim();}
        //String sbuLevel = compInfo[5].trim();
        String query = "";
        int contAcct = 0;
        ResultSet result = null;
		Connection con = null;
		PreparedStatement ps = null;
		con = getConnection();
        if (sbuRequired.equalsIgnoreCase("Y")) {
            if (sbuLevel.equalsIgnoreCase("Department")) {
                query =
                        "SELECT DEPT_ID, SUM(amount)"
                        + " FROM AM_DEPRECIATION_TRAN "
                        + " WHERE USER_ID=" + userid + " AND TRAN_STATUS='U' "
                        + " GROUP BY DEPT_ID "
                        + " ORDER BY DEPT_ID";
        		ps = con.prepareStatement(query);
        		result = ps.executeQuery();
//                result = getStatement().executeQuery(query);

            }
            if (sbuLevel.equalsIgnoreCase("Sector/Units")) {
                query = "SELECT SECTION_ID, SUM(amount)"
                        + " FROM AM_DEPRECIATION_TRAN "
                        + " WHERE USER_ID=" + userid + " AND TRAN_STATUS='U' "
                        + " GROUP BY SECTION_ID "
                        + " ORDER BY SECTION_ID";
        		ps = con.prepareStatement(query);
        		result = ps.executeQuery();
//                result = getStatement().executeQuery(query);
            }
        } else {
            query = "SELECT BRANCH_ID, SUM(amount)"
                    + " FROM AM_DEPRECIATION_TRAN "
                    + " WHERE USER_ID=" + userid + " AND TRAN_STATUS='U' "
                    + " GROUP BY BRANCH_ID "
                    + " ORDER BY BRANCH_ID";
    		ps = con.prepareStatement(query);
    		result = ps.executeQuery();
//            result = getStatement().executeQuery(query);

        }
        //if(result.next())
        return result;
        //  else
        //  return null;

    }
  
    public static String getDepEndDate(String vals) {
        if (vals != null) {
            StringTokenizer st1 = new StringTokenizer(vals, ",");
            if (st1.countTokens() == 2) {
                String s1 = st1.nextToken();
                String s2 = st1.nextToken();

                Float rate = Float.parseFloat(s1);

                StringTokenizer st2 = new StringTokenizer(s2, "/");
                if (st2.countTokens() == 3) {
                    String day = st2.nextToken();
                    String month = st2.nextToken();
                    String year = st2.nextToken();

                    if ((year.length() == 4) && (day.length() > 0) &&
                        (day.length() < 3) &&
                        (month.length() > 0) && (month.length() < 3)) {
                        Calendar c = new GregorianCalendar(Integer.parseInt(
                                year),
                                Integer.parseInt(month) - 1,
                                Integer.parseInt(day) - 1);

                        int months = (int) (100 / rate * 12);
                        c.add(Calendar.MONTH, months);
                        //c.add(Calendar.DAY_OF_YEAR, -1 * Integer.parseInt(day));

                        int endDay = c.get(Calendar.DAY_OF_MONTH);
                        int endMonth = c.get(Calendar.MONTH) + 1;
                        int endYear = c.get(Calendar.YEAR);

                        return endMonth + "-" + endDay + "-" + endYear;
                    }
                }
            }
        }
        return "Error";
    }

    public void updateAssetResidual(String catid, String oldres,
                                    String newresvalue, String changed) throws
            Throwable {

        if (changed.equalsIgnoreCase("Y")) {
            if (!oldres.equalsIgnoreCase(newresvalue)) {
                String query = "UPDATE am_ASSET SET residual_value = " +
                               Double.parseDouble(newresvalue) +
                               "WHERE Category_ID = " + catid +
                               "  AND Asset_Status NOT IN ('C', 'D')";

                String query2 = "UPDATE am_group_asset SET residual_value = " +
                                Double.parseDouble(newresvalue) +
                                "WHERE Category_ID = " + catid +
                                "  AND Asset_Status NOT IN ('C', 'D')";

                Statement stmt = getStatement();
                stmt.addBatch(query);
                stmt.addBatch(query2);
                stmt.executeBatch();

                freeResource();
            }
        } else {

        }
    }
}
