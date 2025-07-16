package legend;

//import com.magbel.util.CurrentDateTime;
import java.sql.*;
import java.util.ArrayList;

import com.magbel.legend.bus.ApprovalRecords;

import magma.net.dao.MagmaDBConnection;


public class AutoIDStockSetup extends ConnectionClass {
	public ApprovalRecords approve;
    public AutoIDStockSetup() throws Exception {
    	approve = new ApprovalRecords(); 
    }

    /**
     * selectAutoIdentity
     *
     * @return String[]
     * @throws Exception
     */
    public String[] selectAutoIdentities() throws Exception {
        //StringBuffer sq = new StringBuffer(100);
        //sq.append("am_msp_select_auto_identities");
        //select * from dbo.am_ad_auto_identity
        Connection cnn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        MagmaDBConnection dbConn = new MagmaDBConnection();

        String query = "select * from am_ad_auto_identity";
        String[] values = new String[11];
        try {
            cnn = dbConn.getConnection("legendPlus");
            ps = cnn.prepareStatement(query);
            rs = ps.executeQuery();
            rs.next();
            for (int x = 0; x < values.length; x++) {
                values[x] = rs.getString(x + 1);
            }
        } catch (Exception e) {
            System.out.println("INFO:Error selectAutoIdentities() ->" +
                               e.getMessage());
        } finally {
            dbConn.closeConnection(cnn, ps, rs);
        }
        // ResultSet rv = getStatement().executeQuery(
        //  sq.toString());

        // String[] values = new String[11];

        // rv.next();
        // for (int x = 0; x < values.length; x++) {
        //     values[x] = rv.getString(x + 1);
        // }

        return values;
    }

    /**
     * selectCartIdentity
     *
     * @return String[][]
     * @throws Exception
     */
    public String[][] selectCartIdentities() throws Exception {
        StringBuffer cq = new StringBuffer(100);
        cq.append("am_msp_count_cart_identities");
        ResultSet rc = getStatement().executeQuery(
                cq.toString());

        StringBuffer sq = new StringBuffer(100);
        sq.append("am_msp_select_cart_identities");
        ResultSet rv = getStatement().executeQuery(
                sq.toString());

        rc.next();
        String[][] values = new String[rc.getInt(1)][5];

        for (int x = 0; x < values.length; x++) {
            rv.next();
            for (int y = 0; y < values[x].length; y++) {
                values[x][y] = rv.getString(y + 1);
            }
        }

        return values;
    }

    /**
     * selectCartIdentity
     *
     * @return String[][]
     * @throws Exception
     */

    //modified by Olabo
    public String[][] selectSequIdentities() throws Exception {
        //StringBuffer cq = new StringBuffer(100);lll
        //cq.append("am_msp_count_sequ_identities");
        Connection cnn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ResultSet rc = null;
        ResultSet rv = null;
        MagmaDBConnection dbConn = new MagmaDBConnection();

        String queryCount = "select count(*) from am_ad_sequ_identity";
        String querySelect = "select * from am_ad_sequ_identity";
        String[][] values = null;
        try {
            cnn = dbConn.getConnection("legendPlus");
            ps = cnn.prepareStatement(queryCount);

            //ResultSet rc = getStatement().executeQuery(
            //cq.toString());
            int j = 0;
            rc = ps.executeQuery();
            while (rc.next()) {
                j = rc.getInt(1);
            } //count number of rows

            //StringBuffer sq = new StringBuffer(100);
            //sq.append("am_msp_select_sequ_identities");

            ps = cnn.prepareStatement(querySelect);
            rv = ps.executeQuery();
            //ResultSet rv = getStatement().executeQuery(
            //sq.toString());

            //rc.next();
            values = new String[j][4];

            for (int x = 0; x < values.length; x++) {
                rv.next();
                for (int y = 0; y < values[x].length; y++) {
                    values[x][y] = rv.getString(y + 1);
                }
            }
        } catch (Exception e) {
            System.out.println("INFO:Error selectSequIdentities() ->" +
                               e.getMessage());
        } finally {
            dbConn.closeConnection(cnn, ps, rs);
        }

        return values;
    }

    /**
     * processAutoIdentity
     *
     * @param a1 String[]
     * @param a2 String[]
     * @param a3 String[]
     * @param a4 String[]
     * @param a5 String[]
     * @param a6 String[]
     * @param a7 String[]
     * @param dm String[]
     * @param fm String
     * @param us String
     * @return boolean
     * @throws Exception
     */
    public boolean processAutoIdentities(String a1, String a2, String a3,
                                         String a4, String a5, String a6,
                                         String a7, String dm, String fm,
                                         String us) throws Exception {
        //StringBuffer iq = new StringBuffer(100);

        // iq.append("am_msp_process_auto_identities" + " '" + a1 + "','" + a2 + "','" + a3 +
        // "','" + a4 + "','" + a5 + "','" + a6 + "','" + a7 + "','" + dm + "','" + fm + "','" + us + "'");
        boolean result = false;
        Connection cnn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        MagmaDBConnection dbConn = new MagmaDBConnection();

        String query = "insert into am_ad_auto_identity (priority1,priority2,priority3,priority4,priority5,priority6,priority7,delimiter,format,user_id,date_cr)" +
                       " values(?,?,?,?,?,?,?,?,?,?,?)";
        try {
            cnn = dbConn.getConnection("legendPlus");
            ps = cnn.prepareStatement(query);
            ps.setString(1, a1);
            ps.setString(2, a2);
            ps.setString(3, a3);
            ps.setString(4, a4);
            ps.setString(5, a5);
            ps.setString(6, a6);
            ps.setString(7, a7);
            ps.setString(8, dm);
            ps.setString(9, fm);
            ps.setString(10, us);
            ps.setDate(11, dbConn.dateConvert(new java.util.Date()));
            result = ps.execute() ? false : true;
        } catch (Exception e) {
            System.out.println("INFO:Error processAutoIdentities() ->" +
                               e.getMessage());
        } finally {
            dbConn.closeConnection(cnn, ps, rs);
        }
        return (result);
    }

    /**
     * processAutoIdentity
     *
     * @param iden String[]
     * @param cati String[]
     * @param user String
     * @return boolean
     * @throws Exception
     */
    public boolean processCartIdentities(String[] iden, String[] cati,
                                         String user) throws Exception {
        //StringBuffer iq = new StringBuffer(100);

        Connection cnn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        MagmaDBConnection dbConn = new MagmaDBConnection();
        String query = "";
        try {
            cnn = dbConn.getConnection("legendPlus");
            for (int i = 0; i < iden.length; i++) {
                //iq.append("exec am_msp_process_cart_identities" + " '" + iden[i] + "'," + cati[i] + ",'" + user + "'");

                query = "insert into st_ad_cart_identity (cart_id,cart_st,cart_cr,user_id,date_cr) values (?,?,?,?,?)";
                // "+Integer.parseInt(iden[i])+","+Integer.parseInt(cati[i])+","+Integer.parseInt(user)+
                ps = cnn.prepareStatement(query);
                ps.setInt(1, Integer.parseInt(iden[i]));
                ps.setInt(2, Integer.parseInt(cati[i]));
                ps.setInt(3, Integer.parseInt(cati[i]));
                ps.setInt(4, Integer.parseInt(user));
                ps.setDate(5, dbConn.dateConvert(new java.util.Date()));
                int x = ps.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println("INFO:Error processCartIdentities() ->" +
                               e.getMessage());
        } finally {
            dbConn.closeConnection(cnn, ps, rs);
        }
        return (ps.executeUpdate() == -1);
        //return (getStatement().executeUpdate(
        //   iq.toString()) == -1);
    }

    /**
     * processAutoIdentity
     *
     * @return boolean
     * @throws Exception
     */
    public boolean resetStruIdentities() throws Exception {
        //StringBuffer iq = new StringBuffer(100);

        //iq.append("exec am_msp_reset_str_identities");
        Connection cnn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        MagmaDBConnection dbConn = new MagmaDBConnection();
        String query1 = "truncate table am_ad_auto_identity";
        String query2 = "truncate table st_ad_cart_identity";
        int x = 0;
        try {
            cnn = dbConn.getConnection("legendPlus");
            ps = cnn.prepareStatement(query1);
            x = ps.executeUpdate();
            ps = cnn.prepareStatement(query2);
            x = ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("INFO:Error resetStruIdentities() ->" +
                               e.getMessage());
        } finally {
            dbConn.closeConnection(cnn, ps, rs);
        }
        return (x == -1);

        //return (getStatement().executeUpdate(
        //  iq.toString()) == -1);
    }

    /**
     * processAutoIdentity
     *
     * @return boolean
     * @throws Exception
     */
    public boolean resetSequIdentities() {
        // StringBuffer iq = new StringBuffer(100);
        // iq.append("exec am_msp_reset_seq_identities");

        Connection cnn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean result = false;
        MagmaDBConnection dbConn = new MagmaDBConnection();
        String query = "truncate table am_ad_sequ_identity";
        try {
            cnn = dbConn.getConnection("legendPlus");
            ps = cnn.prepareStatement(query);
            result = ps.execute() ? false : true;
        } catch (Exception e) {
            System.out.println("INFO:Error resetSequIdentities() ->" +
                               e.getMessage());
        } finally {
            dbConn.closeConnection(cnn, ps, rs);
        }
        //return (getStatement().executeUpdate(
        //  iq.toString()) == -1);
        return result;
    }

    /**
     * processAutoIdentity
     *
     * @param cati String[]
     * @param user String
     * @return boolean
     * @throws Exception
     */
    //modified by Olabo
    public boolean processSequIdentities(String cati, String user) {
        //StringBuffer iq = new StringBuffer(100);
        Connection cnn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean result = false;
        MagmaDBConnection dbConn = new MagmaDBConnection();
        // iq.append("am_msp_process_sequ_identities" + " " + cati + ",'" + user + "'");

        String query =
                "insert into am_ad_sequ_identity (sequ_st,sequ_cr,user_id,date_cr) " +
                "values (?,?,?,?)";
        try {
            cnn = dbConn.getConnection("legendPlus");
            ps = cnn.prepareStatement(query);

            ps.setInt(1, Integer.parseInt(cati));
            ps.setInt(2, Integer.parseInt(cati));
            ps.setString(3, user);
            ps.setDate(4, dbConn.dateConvert(new java.util.Date()));
            result = ps.execute() ? false : true;
        } catch (Exception e) {
            System.out.println("INFO:Error processSequIdentities() ->" +
                               e.getMessage());
        } finally {
            dbConn.closeConnection(cnn, ps, rs);
        }

        return (result);
    }

    /**
     * selectCartIdentity
     *
     * @return String[][]
     * @throws Exception
     */

    //modified by Olabo
    public boolean queryAssets() {
        //StringBuffer cq = new StringBuffer(20);
        //cq.append("am_msp_query_assets");
        Connection cnn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean result = false;
        MagmaDBConnection dbConn = new MagmaDBConnection();
        String query = "SELECT ASSET_ID FROM AM_ASSET";
        try {
            cnn = dbConn.getConnection("legendPlus");
            ps = cnn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                result = true;
            }
        } catch (Exception e) {
            System.out.println("INFO:Error queryAssets() ->" +
                               e.getMessage());
        } finally {
            dbConn.closeConnection(cnn, ps, rs);
        }

        return (result);

        //return getStatement().executeQuery(
        //         cq.toString()).next();
    }

    /**
     * selectCartIdentity
     *
     * @return String[][]
     * @throws Exception
     */
    //modified by Olabo
    public boolean queryAutoId() throws Exception {
        //StringBuffer cq = new StringBuffer(20);
        //cq.append("am_msp_query_auto_id");
        Connection cnn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        MagmaDBConnection dbConn = new MagmaDBConnection();
        String query = "SELECT AUTO_GENERATE_ID FROM AM_GB_COMPANY";
        String result = new String();
        try {
            cnn = dbConn.getConnection("legendPlus");
            ps = cnn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                result = rs.getString(1);
            }
        } catch (Exception e) {
            System.out.println("INFO:Error queryAutoId() ->" +
                               e.getMessage());
        } finally {
            dbConn.closeConnection(cnn, ps, rs);
        }
        //ResultSet rs = getStatement().executeQuery(cq.toString());

        return (result.equals("Y"));
    }

    public String getIdentityOld(String bra, String dep, String sec, String cat) throws
            Throwable {
    	System.out.println("<<<<bra: "+bra+"   dep: "+dep+"  sec: "+sec+"   cat: "+cat);
        StringBuffer sb = new StringBuffer(100);

        String v1 = "", v2 = "", v3 = "", v4 = "", v5 = "", v6 = "", v7 = "",
                dl = "";
        int curr = 0;
        String identity = "";

        ResultSet rsa = getStatement().executeQuery(
                "select * from am_ad_auto_identity");
        ResultSet rsb = getStatement().executeQuery(
                "select * from st_ad_cart_identity");

        ResultSet rs1 = getStatement().executeQuery(
                "select acronym from am_gb_company");
        ResultSet rs2 = getStatement().executeQuery(
                "select group_acronym from am_ad_group");
        ResultSet rs3 = getStatement().executeQuery(
                "select region_acronym from am_ad_region where region_code = (select region_code from am_ad_branch where branch_id = '" +
                bra + "')");
        ResultSet rs4 = getStatement().executeQuery(
                "select branch_acronym from am_ad_branch where branch_id = '" +
                bra + "'");
        ResultSet rs5 = getStatement().executeQuery(
                "select dept_acronym from am_ad_department where dept_id = '" +
                dep + "'");
        ResultSet rs6 = getStatement().executeQuery(
                "select section_acronym from am_ad_section where section_id = '" +
                sec + "'");
        ResultSet rs7 = getStatement().executeQuery(
                "select category_acronym from am_ad_category where category_id = '" +
                cat + "'");
        ResultSet rs8 = getStatement().executeQuery(
                "select cart_cr from st_ad_cart_identity where cart_id = '" +
                cat + "'");
        ResultSet rs9 = getStatement().executeQuery(
                "select sequ_cr from am_ad_sequ_identity");

        if (rsa.next()) {
            if (rsa.getString(1).equals("COMP")) {
                if (rs1.next()) {
                    v1 = rs1.getString(1);
                } else {
                    v1 = "";
                }
            } else if (rsa.getString(1).equals("GRPP")) {
                if (rs2.next()) {
                    v1 = rs2.getString(1);
                } else {
                    v1 = "";
                }
            } else if (rsa.getString(1).equals("REGN")) {
                if (rs3.next()) {
                    v1 = rs3.getString(1);
                } else {
                    v1 = "";
                }
            } else if (rsa.getString(1).equals("BRCH")) {
                if (rs4.next()) {
                    v1 = rs4.getString(1);
                } else {
                    v1 = "";
                }
            } else if (rsa.getString(1).equals("DEPT")) {
                if (rs5.next()) {
                    v1 = rs5.getString(1);
                } else {
                    v1 = "";
                }
            } else if (rsa.getString(1).equals("SECT")) {
                if (rs6.next()) {
                    v1 = rs6.getString(1);
                } else {
                    v1 = "";
                }
            } else if (rsa.getString(1).equals("CATG")) {
                if (rs7.next()) {
                    v1 = rs7.getString(1);
                } else {
                    v1 = "";
                }
            } else {
                v1 = rsa.getString(1);
            }

            if (rsa.getString(2).equals("COMP")) {
                if (rs1.next()) {
                    v2 = rs1.getString(1);
                } else {
                    v2 = "";
                }
            } else if (rsa.getString(2).equals("GRPP")) {
                if (rs2.next()) {
                    v2 = rs2.getString(1);
                } else {
                    v2 = "";
                }
            } else if (rsa.getString(2).equals("REGN")) {
                if (rs3.next()) {
                    v2 = rs3.getString(1);
                } else {
                    v2 = "";
                }
            } else if (rsa.getString(2).equals("BRCH")) {
                if (rs4.next()) {
                    v2 = rs4.getString(1);
                } else {
                    v2 = "";
                }
            } else if (rsa.getString(2).equals("DEPT")) {
                if (rs5.next()) {
                    v2 = rs5.getString(1);
                } else {
                    v2 = "";
                }
            } else if (rsa.getString(2).equals("SECT")) {
                if (rs6.next()) {
                    v2 = rs6.getString(1);
                } else {
                    v2 = "";
                }
            } else if (rsa.getString(2).equals("CATG")) {
                if (rs7.next()) {
                    v2 = rs7.getString(1);
                } else {
                    v2 = "";
                }
            } else {
                v2 = rsa.getString(2);
            }

            if (rsa.getString(3).equals("COMP")) {
                if (rs1.next()) {
                    v3 = rs1.getString(1);
                } else {
                    v3 = "";
                }
            } else if (rsa.getString(3).equals("GRPP")) {
                if (rs2.next()) {
                    v3 = rs2.getString(1);
                } else {
                    v3 = "";
                }
            } else if (rsa.getString(3).equals("REGN")) {
                if (rs3.next()) {
                    v3 = rs3.getString(1);
                } else {
                    v3 = "";
                }
            } else if (rsa.getString(3).equals("BRCH")) {
                if (rs4.next()) {
                    v3 = rs4.getString(1);
                } else {
                    v3 = "";
                }
            } else if (rsa.getString(3).equals("DEPT")) {
                if (rs5.next()) {
                    v3 = rs5.getString(1);
                } else {
                    v3 = "";
                }
            } else if (rsa.getString(3).equals("SECT")) {
                if (rs6.next()) {
                    v3 = rs6.getString(1);
                } else {
                    v3 = "";
                }
            } else if (rsa.getString(3).equals("CATG")) {
                if (rs7.next()) {
                    v3 = rs7.getString(1);
                } else {
                    v3 = "";
                }
            } else {
                v3 = rsa.getString(3);
            }

            if (rsa.getString(4).equals("COMP")) {
                if (rs1.next()) {
                    v4 = rs1.getString(1);
                } else {
                    v4 = "";
                }
            } else if (rsa.getString(4).equals("GRPP")) {
                if (rs2.next()) {
                    v4 = rs2.getString(1);
                } else {
                    v4 = "";
                }
            } else if (rsa.getString(4).equals("REGN")) {
                if (rs3.next()) {
                    v4 = rs3.getString(1);
                } else {
                    v4 = "";
                }
            } else if (rsa.getString(4).equals("BRCH")) {
                if (rs4.next()) {
                    v4 = rs4.getString(1);
                } else {
                    v4 = "";
                }
            } else if (rsa.getString(4).equals("DEPT")) {
                if (rs5.next()) {
                    v4 = rs5.getString(1);
                } else {
                    v4 = "";
                }
            } else if (rsa.getString(4).equals("SECT")) {
                if (rs6.next()) {
                    v4 = rs6.getString(1);
                } else {
                    v4 = "";
                }
            } else if (rsa.getString(4).equals("CATG")) {
                if (rs7.next()) {
                    v4 = rs7.getString(1);
                } else {
                    v4 = "";
                }
            } else {
                v4 = rsa.getString(4);
            }

            if (rsa.getString(5).equals("COMP")) {
                if (rs1.next()) {
                    v5 = rs1.getString(1);
                } else {
                    v5 = "";
                }
            } else if (rsa.getString(5).equals("GRPP")) {
                if (rs2.next()) {
                    v5 = rs2.getString(1);
                } else {
                    v5 = "";
                }
            } else if (rsa.getString(5).equals("REGN")) {
                if (rs3.next()) {
                    v5 = rs3.getString(1);
                } else {
                    v5 = "";
                }
            } else if (rsa.getString(5).equals("BRCH")) {
                if (rs4.next()) {
                    v5 = rs4.getString(1);
                } else {
                    v5 = "";
                }
            } else if (rsa.getString(5).equals("DEPT")) {
                if (rs5.next()) {
                    v5 = rs5.getString(1);
                } else {
                    v5 = "";
                }
            } else if (rsa.getString(5).equals("SECT")) {
                if (rs6.next()) {
                    v5 = rs6.getString(1);
                } else {
                    v5 = "";
                }
            } else if (rsa.getString(5).equals("CATG")) {
                if (rs7.next()) {
                    v5 = rs7.getString(1);
                } else {
                    v5 = "";
                }
            } else {
                v5 = rsa.getString(5);
            }

            if (rsa.getString(6).equals("COMP")) {
                if (rs1.next()) {
                    v6 = rs1.getString(1);
                } else {
                    v6 = "";
                }
            } else if (rsa.getString(6).equals("GRPP")) {
                if (rs2.next()) {
                    v6 = rs2.getString(1);
                } else {
                    v6 = "";
                }
            } else if (rsa.getString(6).equals("REGN")) {
                if (rs3.next()) {
                    v6 = rs3.getString(1);
                } else {
                    v6 = "";
                }
            } else if (rsa.getString(6).equals("BRCH")) {
                if (rs4.next()) {
                    v6 = rs4.getString(1);
                } else {
                    v6 = "";
                }
            } else if (rsa.getString(6).equals("DEPT")) {
                if (rs5.next()) {
                    v6 = rs5.getString(1);
                } else {
                    v6 = "";
                }
            } else if (rsa.getString(6).equals("SECT")) {
                if (rs6.next()) {
                    v6 = rs6.getString(1);
                } else {
                    v6 = "";
                }
            } else if (rsa.getString(6).equals("CATG")) {
                if (rs7.next()) {
                    v6 = rs7.getString(1);
                } else {
                    v6 = "";
                }
            } else {
                v6 = rsa.getString(6);
            }

            if (rsa.getString(7).equals("COMP")) {
                if (rs1.next()) {
                    v7 = rs1.getString(1);
                } else {
                    v7 = "";
                }
            } else if (rsa.getString(7).equals("GRPP")) {
                if (rs2.next()) {
                    v7 = rs2.getString(1);
                } else {
                    v7 = "";
                }
            } else if (rsa.getString(7).equals("REGN")) {
                if (rs3.next()) {
                    v7 = rs3.getString(1);
                } else {
                    v7 = "";
                }
            } else if (rsa.getString(7).equals("BRCH")) {
                if (rs4.next()) {
                    v7 = rs4.getString(1);
                } else {
                    v7 = "";
                }
            } else if (rsa.getString(7).equals("DEPT")) {
                if (rs5.next()) {
                    v7 = rs5.getString(1);
                } else {
                    v7 = "";
                }
            } else if (rsa.getString(7).equals("SECT")) {
                if (rs6.next()) {
                    v7 = rs6.getString(1);
                } else {
                    v7 = "";
                }
            } else if (rsa.getString(7).equals("CATG")) {
                if (rs7.next()) {
                    v7 = rs7.getString(1);
                } else {
                    v7 = "";
                }
            } else {
                v7 = rsa.getString(7);
            }

            dl = rsa.getString(8);

            if (!v1.equals("")) {
                sb.append(v1 + dl);
            }
            if (!v2.equals("")) {
                sb.append(v2 + dl);
            }
            if (!v3.equals("")) {
                sb.append(v3 + dl);
            }
            if (!v4.equals("")) {
                sb.append(v4 + dl);
            }
            if (!v5.equals("")) {
                sb.append(v5 + dl);
            }
            if (!v6.equals("")) {
                sb.append(v6 + dl);
            }
            if (!v7.equals("")) {
                sb.append(v7 + dl);
            }

            rs8.next();
            curr = rs8.getInt(1);
            ++curr;
            System.out.println("<<<<SB: "+sb.toString());
            getStatement().executeUpdate(
                    "update st_ad_cart_identity set cart_cr = " + curr +
                    " where cart_id = (select category_id from am_ad_category where category_id = " +
                    "'" + cat + "')");

            identity = sb.toString() + (curr - 1);
            
        } else if (rsb.next()) {
            rs9.next();
            curr = rs9.getInt(1);
            ++curr;

            getStatement().executeUpdate(
                    "update am_ad_sequ_identity set sequ_cr = " + curr);

            identity = String.valueOf(curr - 1);
        }
 //freeResource();
 System.out.println("<<<<SBA: "+identity);
        return identity;
    }


    public String getIdentityforStock(String bra, String dep, String sec, String cat) throws
            Throwable {
    	System.out.println("<<<<bra: "+bra+"   dep: "+dep+"  sec: "+sec+"   cat: "+cat);
        StringBuffer sb = new StringBuffer(100);

        String v1 = "", v2 = "", v3 = "", v4 = "", v5 = "", v6 = "", v7 = "",
                dl = "";
        int curr = 0;
        String identity = "";

        ResultSet rsa = getStatement().executeQuery(
                "select * from am_ad_auto_identity");
        ResultSet rsb = getStatement().executeQuery(
                "select * from st_ad_cart_identity");

        ResultSet rs1 = getStatement().executeQuery(
                "select acronym from am_gb_company");
        ResultSet rs2 = getStatement().executeQuery(
                "select group_acronym from am_ad_group");
        ResultSet rs3 = getStatement().executeQuery(
                "select region_acronym from am_ad_region where region_code = (select region_code from am_ad_branch where branch_id = '" +
                bra + "')");
        ResultSet rs4 = getStatement().executeQuery(
                "select branch_acronym from am_ad_branch where branch_id = '" +
                bra + "'");
        ResultSet rs5 = getStatement().executeQuery(
                "select dept_acronym from am_ad_department where dept_id = '" +
                dep + "'");
        ResultSet rs6 = getStatement().executeQuery(
                "select section_acronym from am_ad_section where section_id = '" +
                sec + "'");
        ResultSet rs7 = getStatement().executeQuery(
                "select category_acronym from ST_STOCK_CATEGORY where category_id = '" +
                cat + "'");
        ResultSet rs8 = getStatement().executeQuery(
                "select cart_cr from st_ad_cart_identity where cart_id = '" +
                cat + "'");
        ResultSet rs9 = getStatement().executeQuery(
                "select sequ_cr from am_ad_sequ_identity");

        if (rsa.next()) {
            if (rsa.getString(1).equals("COMP")) {
                if (rs1.next()) {
                    v1 = rs1.getString(1);
                } else {
                    v1 = "";
                }
            } else if (rsa.getString(1).equals("GRPP")) {
                if (rs2.next()) {
                    v1 = rs2.getString(1);
                } else {
                    v1 = "";
                }
            } else if (rsa.getString(1).equals("REGN")) {
                if (rs3.next()) {
                    v1 = rs3.getString(1);
                } else {
                    v1 = "";
                }
            } else if (rsa.getString(1).equals("BRCH")) {
                if (rs4.next()) {
                    v1 = rs4.getString(1);
                } else {
                    v1 = "";
                }
            } else if (rsa.getString(1).equals("DEPT")) {
                if (rs5.next()) {
                    v1 = rs5.getString(1);
                } else {
                    v1 = "";
                }
            } else if (rsa.getString(1).equals("SECT")) {
                if (rs6.next()) {
                    v1 = rs6.getString(1);
                } else {
                    v1 = "";
                }
            } else if (rsa.getString(1).equals("CATG")) {
                if (rs7.next()) {
                    v1 = rs7.getString(1);
                } else {
                    v1 = "";
                }
            } else {
                v1 = rsa.getString(1);
            }

            if (rsa.getString(2).equals("COMP")) {
                if (rs1.next()) {
                    v2 = rs1.getString(1);
                } else {
                    v2 = "";
                }
            } else if (rsa.getString(2).equals("GRPP")) {
                if (rs2.next()) {
                    v2 = rs2.getString(1);
                } else {
                    v2 = "";
                }
            } else if (rsa.getString(2).equals("REGN")) {
                if (rs3.next()) {
                    v2 = rs3.getString(1);
                } else {
                    v2 = "";
                }
            } else if (rsa.getString(2).equals("BRCH")) {
                if (rs4.next()) {
                    v2 = rs4.getString(1);
                } else {
                    v2 = "";
                }
            } else if (rsa.getString(2).equals("DEPT")) {
                if (rs5.next()) {
                    v2 = rs5.getString(1);
                } else {
                    v2 = "";
                }
            } else if (rsa.getString(2).equals("SECT")) {
                if (rs6.next()) {
                    v2 = rs6.getString(1);
                } else {
                    v2 = "";
                }
            } else if (rsa.getString(2).equals("CATG")) {
                if (rs7.next()) {
                    v2 = rs7.getString(1);
                } else {
                    v2 = "";
                }
            } else {
                v2 = rsa.getString(2);
            }

            if (rsa.getString(3).equals("COMP")) {
                if (rs1.next()) {
                    v3 = rs1.getString(1);
                } else {
                    v3 = "";
                }
            } else if (rsa.getString(3).equals("GRPP")) {
                if (rs2.next()) {
                    v3 = rs2.getString(1);
                } else {
                    v3 = "";
                }
            } else if (rsa.getString(3).equals("REGN")) {
                if (rs3.next()) {
                    v3 = rs3.getString(1);
                } else {
                    v3 = "";
                }
            } else if (rsa.getString(3).equals("BRCH")) {
                if (rs4.next()) {
                    v3 = rs4.getString(1);
                } else {
                    v3 = "";
                }
            } else if (rsa.getString(3).equals("DEPT")) {
                if (rs5.next()) {
                    v3 = rs5.getString(1);
                } else {
                    v3 = "";
                }
            } else if (rsa.getString(3).equals("SECT")) {
                if (rs6.next()) {
                    v3 = rs6.getString(1);
                } else {
                    v3 = "";
                }
            } else if (rsa.getString(3).equals("CATG")) {
                if (rs7.next()) {
                    v3 = rs7.getString(1);
                } else {
                    v3 = "";
                }
            } else {
                v3 = rsa.getString(3);
            }

            if (rsa.getString(4).equals("COMP")) {
                if (rs1.next()) {
                    v4 = rs1.getString(1);
                } else {
                    v4 = "";
                }
            } else if (rsa.getString(4).equals("GRPP")) {
                if (rs2.next()) {
                    v4 = rs2.getString(1);
                } else {
                    v4 = "";
                }
            } else if (rsa.getString(4).equals("REGN")) {
                if (rs3.next()) {
                    v4 = rs3.getString(1);
                } else {
                    v4 = "";
                }
            } else if (rsa.getString(4).equals("BRCH")) {
                if (rs4.next()) {
                    v4 = rs4.getString(1);
                } else {
                    v4 = "";
                }
            } else if (rsa.getString(4).equals("DEPT")) {
                if (rs5.next()) {
                    v4 = rs5.getString(1);
                } else {
                    v4 = "";
                }
            } else if (rsa.getString(4).equals("SECT")) {
                if (rs6.next()) {
                    v4 = rs6.getString(1);
                } else {
                    v4 = "";
                }
            } else if (rsa.getString(4).equals("CATG")) {
                if (rs7.next()) {
                    v4 = rs7.getString(1);
                } else {
                    v4 = "";
                }
            } else {
                v4 = rsa.getString(4);
            }

            if (rsa.getString(5).equals("COMP")) {
                if (rs1.next()) {
                    v5 = rs1.getString(1);
                } else {
                    v5 = "";
                }
            } else if (rsa.getString(5).equals("GRPP")) {
                if (rs2.next()) {
                    v5 = rs2.getString(1);
                } else {
                    v5 = "";
                }
            } else if (rsa.getString(5).equals("REGN")) {
                if (rs3.next()) {
                    v5 = rs3.getString(1);
                } else {
                    v5 = "";
                }
            } else if (rsa.getString(5).equals("BRCH")) {
                if (rs4.next()) {
                    v5 = rs4.getString(1);
                } else {
                    v5 = "";
                }
            } else if (rsa.getString(5).equals("DEPT")) {
                if (rs5.next()) {
                    v5 = rs5.getString(1);
                } else {
                    v5 = "";
                }
            } else if (rsa.getString(5).equals("SECT")) {
                if (rs6.next()) {
                    v5 = rs6.getString(1);
                } else {
                    v5 = "";
                }
            } else if (rsa.getString(5).equals("CATG")) {
                if (rs7.next()) {
                    v5 = rs7.getString(1);
                } else {
                    v5 = "";
                }
            } else {
                v5 = rsa.getString(5);
            }

            if (rsa.getString(6).equals("COMP")) {
                if (rs1.next()) {
                    v6 = rs1.getString(1);
                } else {
                    v6 = "";
                }
            } else if (rsa.getString(6).equals("GRPP")) {
                if (rs2.next()) {
                    v6 = rs2.getString(1);
                } else {
                    v6 = "";
                }
            } else if (rsa.getString(6).equals("REGN")) {
                if (rs3.next()) {
                    v6 = rs3.getString(1);
                } else {
                    v6 = "";
                }
            } else if (rsa.getString(6).equals("BRCH")) {
                if (rs4.next()) {
                    v6 = rs4.getString(1);
                } else {
                    v6 = "";
                }
            } else if (rsa.getString(6).equals("DEPT")) {
                if (rs5.next()) {
                    v6 = rs5.getString(1);
                } else {
                    v6 = "";
                }
            } else if (rsa.getString(6).equals("SECT")) {
                if (rs6.next()) {
                    v6 = rs6.getString(1);
                } else {
                    v6 = "";
                }
            } else if (rsa.getString(6).equals("CATG")) {
                if (rs7.next()) {
                    v6 = rs7.getString(1);
                } else {
                    v6 = "";
                }
            } else {
                v6 = rsa.getString(6);
            }

            if (rsa.getString(7).equals("COMP")) {
                if (rs1.next()) {
                    v7 = rs1.getString(1);
                } else {
                    v7 = "";
                }
            } else if (rsa.getString(7).equals("GRPP")) {
                if (rs2.next()) {
                    v7 = rs2.getString(1);
                } else {
                    v7 = "";
                }
            } else if (rsa.getString(7).equals("REGN")) {
                if (rs3.next()) {
                    v7 = rs3.getString(1);
                } else {
                    v7 = "";
                }
            } else if (rsa.getString(7).equals("BRCH")) {
                if (rs4.next()) {
                    v7 = rs4.getString(1);
                } else {
                    v7 = "";
                }
            } else if (rsa.getString(7).equals("DEPT")) {
                if (rs5.next()) {
                    v7 = rs5.getString(1);
                } else {
                    v7 = "";
                }
            } else if (rsa.getString(7).equals("SECT")) {
                if (rs6.next()) {
                    v7 = rs6.getString(1);
                } else {
                    v7 = "";
                }
            } else if (rsa.getString(7).equals("CATG")) {
                if (rs7.next()) {
                    v7 = rs7.getString(1);
                } else {
                    v7 = "";
                }
            } else {
                v7 = rsa.getString(7);
            }

            dl = rsa.getString(8);

            if (!v1.equals("")) {
                sb.append(v1 + dl);
            }
            if (!v2.equals("")) {
                sb.append(v2 + dl);
            }
            if (!v3.equals("")) {
                sb.append(v3 + dl);
            }
            if (!v4.equals("")) {
                sb.append(v4 + dl);
            }
            if (!v5.equals("")) {
                sb.append(v5 + dl);
            }
            if (!v6.equals("")) {
                sb.append(v6 + dl);
            }
            if (!v7.equals("")) {
                sb.append(v7 + dl);
            }

            rs8.next();
            curr = rs8.getInt(1);
            ++curr;
            System.out.println("<<<<SB: "+sb.toString());
            getStatement().executeUpdate(
                    "update st_ad_cart_identity set cart_cr = " + curr +
                    " where cart_id = (select category_id from ST_STOCK_CATEGORY where category_id = " +
                    "'" + cat + "')");

            identity = sb.toString() + (curr - 1);
            
        } else if (rsb.next()) {
            rs9.next();
            curr = rs9.getInt(1);
            ++curr;

            getStatement().executeUpdate(
                    "update am_ad_sequ_identity set sequ_cr = " + curr);

            identity = String.valueOf(curr - 1);
        }
 freeResource();
 System.out.println("<<<<SBA: "+identity);
        return identity;
    }


    public String getIdentityOld(String bra, String dep, String sec, String cat,String identity) throws
    Throwable {
//    	magma.AssetRecordsBean bd = null;
//    for (int i = 0; i < list.size(); i++) {
//    	bd = (magma.AssetRecordsBean) list.get(i);
//    	 public String getIdentity(String bra, String dep, String sec, String cat) throws 	
//    	cat = bd.getCategory_id();
 //       String bra = bd.getNewbranch_id();
 //       String dep = bd.getNewdepartment_id(); 
//        String sec = bd.getNewsection_id();
StringBuffer sb = new StringBuffer(100);

String v1 = "", v2 = "", v3 = "", v4 = "", v5 = "", v6 = "", v7 = "",
        dl = "";
int curr = 0;
//String identity = "";

ResultSet rsa = getStatement().executeQuery(
        "select * from am_ad_auto_identity");
ResultSet rsb = getStatement().executeQuery(
        "select * from st_ad_cart_identity");

ResultSet rs1 = getStatement().executeQuery(
        "select acronym from am_gb_company");
ResultSet rs2 = getStatement().executeQuery(
        "select group_acronym from am_ad_group");
ResultSet rs3 = getStatement().executeQuery(
        "select region_acronym from am_ad_region where region_id = (select region_Code from am_ad_branch where branch_id = '" +
        bra + "')");
ResultSet rs4 = getStatement().executeQuery(
        "select branch_acronym from am_ad_branch where branch_id = '" +
        bra + "'");
ResultSet rs5 = getStatement().executeQuery(
        "select dept_acronym from am_ad_department where dept_id = '" +
        dep + "'");
ResultSet rs6 = getStatement().executeQuery(
        "select section_acronym from am_ad_section where section_id = '" +
        sec + "'");
ResultSet rs7 = getStatement().executeQuery(
        "select category_acronym from am_ad_category where category_id = '" +
        cat + "'");
ResultSet rs8 = getStatement().executeQuery(
        "select cart_cr from st_ad_cart_identity where cart_id = '" +
        cat + "'");
ResultSet rs9 = getStatement().executeQuery(
        "select sequ_cr from am_ad_sequ_identity");

if (rsa.next()) {
    if (rsa.getString(1).equals("COMP")) {
        if (rs1.next()) {
            v1 = rs1.getString(1);
        } else {
            v1 = "";
        }
    } else if (rsa.getString(1).equals("GRPP")) {
        if (rs2.next()) {
            v1 = rs2.getString(1);
        } else {
            v1 = "";
        }
    } else if (rsa.getString(1).equals("REGN")) {
        if (rs3.next()) {
            v1 = rs3.getString(1);
        } else {
            v1 = "";
        }
    } else if (rsa.getString(1).equals("BRCH")) {
        if (rs4.next()) {
            v1 = rs4.getString(1);
        } else {
            v1 = "";
        }
    } else if (rsa.getString(1).equals("DEPT")) {
        if (rs5.next()) {
            v1 = rs5.getString(1);
        } else {
            v1 = "";
        }
    } else if (rsa.getString(1).equals("SECT")) {
        if (rs6.next()) {
            v1 = rs6.getString(1);
        } else {
            v1 = "";
        }
    } else if (rsa.getString(1).equals("CATG")) {
        if (rs7.next()) {
            v1 = rs7.getString(1);
        } else {
            v1 = "";
        }
    } else {
        v1 = rsa.getString(1);
    }

    if (rsa.getString(2).equals("COMP")) {
        if (rs1.next()) {
            v2 = rs1.getString(1);
        } else {
            v2 = "";
        }
    } else if (rsa.getString(2).equals("GRPP")) {
        if (rs2.next()) {
            v2 = rs2.getString(1);
        } else {
            v2 = "";
        }
    } else if (rsa.getString(2).equals("REGN")) {
        if (rs3.next()) {
            v2 = rs3.getString(1);
        } else {
            v2 = "";
        }
    } else if (rsa.getString(2).equals("BRCH")) {
        if (rs4.next()) {
            v2 = rs4.getString(1);
        } else {
            v2 = "";
        }
    } else if (rsa.getString(2).equals("DEPT")) {
        if (rs5.next()) {
            v2 = rs5.getString(1);
        } else {
            v2 = "";
        }
    } else if (rsa.getString(2).equals("SECT")) {
        if (rs6.next()) {
            v2 = rs6.getString(1);
        } else {
            v2 = "";
        }
    } else if (rsa.getString(2).equals("CATG")) {
        if (rs7.next()) {
            v2 = rs7.getString(1);
        } else {
            v2 = "";
        }
    } else {
        v2 = rsa.getString(2);
    }

    if (rsa.getString(3).equals("COMP")) {
        if (rs1.next()) {
            v3 = rs1.getString(1);
        } else {
            v3 = "";
        }
    } else if (rsa.getString(3).equals("GRPP")) {
        if (rs2.next()) {
            v3 = rs2.getString(1);
        } else {
            v3 = "";
        }
    } else if (rsa.getString(3).equals("REGN")) {
        if (rs3.next()) {
            v3 = rs3.getString(1);
        } else {
            v3 = "";
        }
    } else if (rsa.getString(3).equals("BRCH")) {
        if (rs4.next()) {
            v3 = rs4.getString(1);
        } else {
            v3 = "";
        }
    } else if (rsa.getString(3).equals("DEPT")) {
        if (rs5.next()) {
            v3 = rs5.getString(1);
        } else {
            v3 = "";
        }
    } else if (rsa.getString(3).equals("SECT")) {
        if (rs6.next()) {
            v3 = rs6.getString(1);
        } else {
            v3 = "";
        }
    } else if (rsa.getString(3).equals("CATG")) {
        if (rs7.next()) {
            v3 = rs7.getString(1);
        } else {
            v3 = "";
        }
    } else {
        v3 = rsa.getString(3);
    }

    if (rsa.getString(4).equals("COMP")) {
        if (rs1.next()) {
            v4 = rs1.getString(1);
        } else {
            v4 = "";
        }
    } else if (rsa.getString(4).equals("GRPP")) {
        if (rs2.next()) {
            v4 = rs2.getString(1);
        } else {
            v4 = "";
        }
    } else if (rsa.getString(4).equals("REGN")) {
        if (rs3.next()) {
            v4 = rs3.getString(1);
        } else {
            v4 = "";
        }
    } else if (rsa.getString(4).equals("BRCH")) {
        if (rs4.next()) {
            v4 = rs4.getString(1);
        } else {
            v4 = "";
        }
    } else if (rsa.getString(4).equals("DEPT")) {
        if (rs5.next()) {
            v4 = rs5.getString(1);
        } else {
            v4 = "";
        }
    } else if (rsa.getString(4).equals("SECT")) {
        if (rs6.next()) {
            v4 = rs6.getString(1);
        } else {
            v4 = "";
        }
    } else if (rsa.getString(4).equals("CATG")) {
        if (rs7.next()) {
            v4 = rs7.getString(1);
        } else {
            v4 = "";
        }
    } else {
        v4 = rsa.getString(4);
    }

    if (rsa.getString(5).equals("COMP")) {
        if (rs1.next()) {
            v5 = rs1.getString(1);
        } else {
            v5 = "";
        }
    } else if (rsa.getString(5).equals("GRPP")) {
        if (rs2.next()) {
            v5 = rs2.getString(1);
        } else {
            v5 = "";
        }
    } else if (rsa.getString(5).equals("REGN")) {
        if (rs3.next()) {
            v5 = rs3.getString(1);
        } else {
            v5 = "";
        }
    } else if (rsa.getString(5).equals("BRCH")) {
        if (rs4.next()) {
            v5 = rs4.getString(1);
        } else {
            v5 = "";
        }
    } else if (rsa.getString(5).equals("DEPT")) {
        if (rs5.next()) {
            v5 = rs5.getString(1);
        } else {
            v5 = "";
        }
    } else if (rsa.getString(5).equals("SECT")) {
        if (rs6.next()) {
            v5 = rs6.getString(1);
        } else {
            v5 = "";
        }
    } else if (rsa.getString(5).equals("CATG")) {
        if (rs7.next()) {
            v5 = rs7.getString(1);
        } else {
            v5 = "";
        }
    } else {
        v5 = rsa.getString(5);
    }

    if (rsa.getString(6).equals("COMP")) {
        if (rs1.next()) {
            v6 = rs1.getString(1);
        } else {
            v6 = "";
        }
    } else if (rsa.getString(6).equals("GRPP")) {
        if (rs2.next()) {
            v6 = rs2.getString(1);
        } else {
            v6 = "";
        }
    } else if (rsa.getString(6).equals("REGN")) {
        if (rs3.next()) {
            v6 = rs3.getString(1);
        } else {
            v6 = "";
        }
    } else if (rsa.getString(6).equals("BRCH")) {
        if (rs4.next()) {
            v6 = rs4.getString(1);
        } else {
            v6 = "";
        }
    } else if (rsa.getString(6).equals("DEPT")) {
        if (rs5.next()) {
            v6 = rs5.getString(1);
        } else {
            v6 = "";
        }
    } else if (rsa.getString(6).equals("SECT")) {
        if (rs6.next()) {
            v6 = rs6.getString(1);
        } else {
            v6 = "";
        }
    } else if (rsa.getString(6).equals("CATG")) {
        if (rs7.next()) {
            v6 = rs7.getString(1);
        } else {
            v6 = "";
        }
    } else {
        v6 = rsa.getString(6);
    }

    if (rsa.getString(7).equals("COMP")) {
        if (rs1.next()) {
            v7 = rs1.getString(1);
        } else {
            v7 = "";
        }
    } else if (rsa.getString(7).equals("GRPP")) {
        if (rs2.next()) {
            v7 = rs2.getString(1);
        } else {
            v7 = "";
        }
    } else if (rsa.getString(7).equals("REGN")) {
        if (rs3.next()) {
            v7 = rs3.getString(1);
        } else {
            v7 = "";
        }
    } else if (rsa.getString(7).equals("BRCH")) {
        if (rs4.next()) {
            v7 = rs4.getString(1);
        } else {
            v7 = "";
        }
    } else if (rsa.getString(7).equals("DEPT")) {
        if (rs5.next()) {
            v7 = rs5.getString(1);
        } else {
            v7 = "";
        }
    } else if (rsa.getString(7).equals("SECT")) {
        if (rs6.next()) {
            v7 = rs6.getString(1);
        } else {
            v7 = "";
        }
    } else if (rsa.getString(7).equals("CATG")) {
        if (rs7.next()) {
            v7 = rs7.getString(1);
        } else {
            v7 = "";
        }
    } else {
        v7 = rsa.getString(7);
    }

    dl = rsa.getString(8);

    if (!v1.equals("")) {
        sb.append(v1 + dl);
    }
    if (!v2.equals("")) {
        sb.append(v2 + dl);
    }
    if (!v3.equals("")) {
        sb.append(v3 + dl);
    }
    if (!v4.equals("")) {
        sb.append(v4 + dl);
    }
    if (!v5.equals("")) {
        sb.append(v5 + dl);
    }
    if (!v6.equals("")) {
        sb.append(v6 + dl);
    }
    if (!v7.equals("")) {
        sb.append(v7 + dl);
    }

    rs8.next();
    curr = rs8.getInt(1);
    ++curr;

    getStatement().executeUpdate(
            "update st_ad_cart_identity set cart_cr = " + curr +
            " where cart_id = (select category_id from am_ad_category where category_id = " +
            "'" + cat + "')");

    identity = sb.toString() + (curr - 1);
} else if (rsb.next()) {
    rs9.next();
    curr = rs9.getInt(1);
    ++curr;

    getStatement().executeUpdate(
            "update am_ad_sequ_identity set sequ_cr = " + curr);

    identity = String.valueOf(curr - 1);
}
freeResource();
return identity;
//	return identity;
}

    
 public String getCategoryID(String catName) {
             Connection cnn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        MagmaDBConnection dbConn = new MagmaDBConnection();

        String query = "select category_id from am_ad_category where category_name='"+catName+"'";
        String value = "";
        try {
            cnn = dbConn.getConnection("legendPlus");
            ps = cnn.prepareStatement(query);
            rs = ps.executeQuery();
            while(rs.next()){
           value= rs.getString(1);
            }

        } catch (Exception e) {
            System.out.println("AutoIDSetup: INFO:Error getCategoryID() ->" + e.getMessage());
        } finally {
            dbConn.closeConnection(cnn, ps, rs);
        }

        return value;
    }

public String getIdentity(String bra, String dep, String sec, String cat)
{
	MagmaDBConnection dbConn = new MagmaDBConnection();
	String delim = approve.getCodeName("SELECT DELIMITER FROM am_ad_auto_identity ");
	String curr = approve.getCodeName("select cart_cr from st_ad_cart_identity where cart_id = "+cat+"");
//	System.out.println("====curr :"+curr);
    String query =
           "select acronym,'' AS group_acronym,'' AS region_acronym,'' AS branch_acronym,'' AS dept_acronym,'' AS section_acronym,'' AS category_acronym,0 AS cart_cr,0 AS sequ_cr from am_gb_company  " +
           " UNION "+
           "select '' AS acronym,group_acronym,'' AS region_acronym,'' AS branch_acronym,'' AS dept_acronym,'' AS section_acronym,'' AS category_acronym,0 AS cart_cr,0 AS sequ_cr  from am_ad_group"+
           " UNION "+
           "select '' AS acronym,'' AS group_acronym,region_acronym,'' AS branch_acronym,'' AS dept_acronym,'' AS section_acronym,'' AS category_acronym,0 AS cart_cr,0 AS sequ_cr from am_ad_region where region_id = (select region from am_ad_branch where branch_id = '"+bra+"')"+
           " UNION "+
           "select '' AS acronym,'' AS group_acronym,'' AS region_acronym,branch_acronym,'' AS dept_acronym,'' AS section_acronym,'' AS category_acronym,0 AS cart_cr,0 AS sequ_cr from am_ad_branch where branch_id = '"+bra+"'"+  
           " UNION "+  
           "select '' AS acronym,'' AS group_acronym,'' AS region_acronym,'' AS branch_acronym,dept_acronym,'' AS section_acronym,'' AS category_acronym,0 AS cart_cr,0 AS sequ_cr from am_ad_department where dept_id = '"+dep+"'"+  
           " UNION "+
           "select '' AS acronym,'' AS group_acronym,'' AS region_acronym,'' AS branch_acronym,'' AS dept_acronym,section_acronym,'' AS category_acronym,0 AS cart_cr,0 AS sequ_cr  from am_ad_section where section_id = '"+sec+"'"+
           " UNION "+
           "select '' AS acronym,'' AS group_acronym,'' AS region_acronym,'' AS branch_acronym,'' AS dept_acronym,'' AS section_acronym,category_acronym,0 AS cart_cr,0 AS sequ_cr  from am_ad_category where category_id = '"+cat+"'"+
           " UNION "+
           "select '' AS acronym,'' AS group_acronym,'' AS region_acronym,'' AS branch_acronym,'' AS dept_acronym,'' AS section_acronym,'' AS category_acronym,cart_cr,0 AS sequ_cr  from st_ad_cart_identity where cart_id = '"+cat+"'"+
           " UNION "+
           "select '' AS acronym,'' AS group_acronym,'' AS region_acronym,'' AS branch_acronym,'' AS dept_acronym,'' AS section_acronym,'' AS category_acronym,0 AS cart_cr,sequ_cr  from am_ad_sequ_identity";
 //System.out.println("========query in getNewIdentity: "+query);  
    int currValue = Integer.parseInt(curr) + 1;
//    System.out.println("====New currValue :"+currValue);
String query2 = "update st_ad_cart_identity set cart_cr = " + currValue +
" where cart_id = (select category_id from am_ad_category where category_id = " +
"'" + cat + "')";
String query3 = "update am_ad_sequ_identity set sequ_cr = " + currValue;

      Connection con = null;
      ResultSet rs = null;
      Statement stmt = null;
      PreparedStatement ps = null;
   String comp = "";
   String group = "";
   String region = "";
   String branch = "";
   String dept = "";
   String section = "";
   String category = "";
   String catrCr = "";
   String sequCr = "";
   String new_assetId = "";
   int count = 0;
   boolean done = false;
   try {
       con=getConnection();
       stmt = con.createStatement();
       rs = stmt.executeQuery(query);
       while (rs.next()) {
    	   count = count + 1;
 //   	   System.out.println("====Count :"+count);
    	   if(!rs.getString(1).equals("")){comp = rs.getString(1);}
           if(!rs.getString(2).equals("")){group = rs.getString(2);}
           if(!rs.getString(3).equals("")){region = rs.getString(3);}
           if(!rs.getString(4).equals("")){branch = rs.getString(4);}
           if(!rs.getString(5).equals("")){dept = rs.getString(5);}
           if(!rs.getString(6).equals("")){section = rs.getString(6);}
           if(!rs.getString(7).equals("")){category = rs.getString(7);}
           if(!rs.getString(8).equals("0")){catrCr = rs.getString(8);}
           if(!rs.getString(9).equals("0")){sequCr = rs.getString(9);}
       }
//       System.out.println("====comp: "+comp+"  group: "+group+"  region: "+region+"  branch: "+branch+"  dept: "+dept+"  section: "+section+"  category: "+category+"  catrCr: "+catrCr+"  sequCr: "+sequCr);
       new_assetId = comp+delim+branch+delim+category+delim+catrCr;
//       System.out.println("====New Asset Id: "+new_assetId);
       ps = con.prepareStatement(query2);
       done = (ps.executeUpdate() != -1);
//       System.out.println("====New done Id  & query2: "+done+"    query2: "+query2);
       ps = con.prepareStatement(query3);
       done = (ps.executeUpdate() != -1);
 //      System.out.println("====New done Id  & query3: "+done+"    query2: "+query3);
 //      System.out.println("====>>>CURR: "+findObject("select cart_cr from st_ad_cart_identity where cart_id = "+cat+""));
   } catch (Exception ex) {
       ex.printStackTrace();
   } finally {
	   dbConn.closeConnection(con,stmt,rs);
	   dbConn.closeConnection(con, ps);
       
   }
   System.out.println("====new_assetId in getNewIdentity: "+new_assetId);
   return new_assetId;

}



}
