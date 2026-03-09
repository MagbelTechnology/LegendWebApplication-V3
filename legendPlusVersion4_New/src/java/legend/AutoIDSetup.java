package legend;

//import com.magbel.util.CurrentDateTime;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.magbel.legend.bus.ApprovalRecords;

import magma.net.dao.MagmaDBConnection;


public class AutoIDSetup extends legend.ConnectionClass {
	public ApprovalRecords approve;
    public AutoIDSetup() throws Exception {
    	approve = new ApprovalRecords(); 
    }

    /**
     * selectAutoIdentity
     *
     * @return String[]
     * @throws Exception
     */
    public String[] selectAutoIdentitiesOld() throws Exception {
        //StringBuffer sq = new StringBuffer(100);
        //sq.append("am_msp_select_auto_identities");
        //select * from dbo.am_ad_auto_identity

        MagmaDBConnection dbConnection = new MagmaDBConnection();

        String query = "select * from am_ad_auto_identity";
        String[] values = new String[11];
        try {
            Connection cnn = dbConnection.getConnection("legendPlus");
            PreparedStatement ps = cnn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            rs.next();
            for (int x = 0; x < values.length; x++) {
                values[x] = rs.getString(x + 1);
            }
        } catch (Exception e) {
            System.out.println("INFO:Error selectAutoIdentities() ->" +
                               e.getMessage());
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
    
    
    public String[] selectAutoIdentities() throws Exception {

        MagmaDBConnection dbConnection = new MagmaDBConnection();
        String query = "select * from am_ad_auto_identity";
        String[] values = new String[11];

        try (Connection cnn = dbConnection.getConnection("legendPlus");
             PreparedStatement ps = cnn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                for (int x = 0; x < values.length; x++) {
                    values[x] = rs.getString(x + 1);
                }
            }

        } catch (Exception e) {
            System.out.println("INFO:Error selectAutoIdentities() ->" + e.getMessage());
        }

        return values;
    }

    /**
     * selectCartIdentity
     *
     * @return String[][]
     * @throws Exception
     */
//    public String[][] selectCartIdentitiesOld() throws Exception {
//        StringBuffer cq = new StringBuffer(100);
//        cq.append("am_msp_count_cart_identities");
//        
//        ResultSet rc = getStatement().executeQuery(
//                cq.toString());
//
//        StringBuffer sq = new StringBuffer(100);
//        sq.append("am_msp_select_cart_identities");
//        ResultSet rv = getStatement().executeQuery(
//                sq.toString());
//
//        rc.next();
//        String[][] values = new String[rc.getInt(1)][5];
//
//        for (int x = 0; x < values.length; x++) {
//            rv.next();
//            for (int y = 0; y < values[x].length; y++) {
//                values[x][y] = rv.getString(y + 1);
//            }
//        }
//
//        return values;
//    }
    
    
    public String[][] selectCartIdentities() throws Exception {
        String countQuery = "am_msp_count_cart_identities";
        String selectQuery = "am_msp_select_cart_identities";

        MagmaDBConnection dbConn = new MagmaDBConnection();

        try (Connection conn = dbConn.getConnection("legendPlus");
             Statement stmtCount = conn.createStatement();
             ResultSet rc = stmtCount.executeQuery(countQuery)) {

            if (!rc.next()) {
                return new String[0][0];
            }

            int rowCount = rc.getInt(1);

            try (Statement stmtSelect = conn.createStatement();
                 ResultSet rv = stmtSelect.executeQuery(selectQuery)) {

                ResultSetMetaData meta = rv.getMetaData();
                int colCount = meta.getColumnCount();

                String[][] values = new String[rowCount][colCount];

                for (int x = 0; x < rowCount; x++) {
                    if (!rv.next()) {
                        break;
                    }
                    for (int y = 0; y < colCount; y++) {
                        values[x][y] = rv.getString(y + 1);
                    }
                }
                return values;
            }
        }
    }
    
    

    /**
     * selectCartIdentity
     *
     * @return String[][]
     * @throws Exception
     */

    //modified by Olabo
    public String[][] selectSequIdentitiesOld() throws Exception {
        //StringBuffer cq = new StringBuffer(100);lll
        //cq.append("am_msp_count_sequ_identities");

        MagmaDBConnection dbConn = new MagmaDBConnection();

        String queryCount = "select count(*) from am_ad_sequ_identity";
        String querySelect = "select * from am_ad_sequ_identity";
        String[][] values = null;
        try {
            Connection cnn = dbConn.getConnection("legendPlus");
            PreparedStatement ps = cnn.prepareStatement(queryCount);

            //ResultSet rc = getStatement().executeQuery(
            //cq.toString());
            int j = 0;
            ResultSet rc = ps.executeQuery();
            while (rc.next()) {
                j = rc.getInt(1);
            } //count number of rows

            //StringBuffer sq = new StringBuffer(100);
            //sq.append("am_msp_select_sequ_identities");

            ps = cnn.prepareStatement(querySelect);
            ResultSet rv = ps.executeQuery();
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
        } 

        return values;
    }
    
    
    
    public String[][] selectSequIdentities() throws Exception {

        MagmaDBConnection dbConn = new MagmaDBConnection();

        String queryCount = "select count(*) from am_ad_sequ_identity";
        String querySelect = "select * from am_ad_sequ_identity";

        String[][] values = new String[0][0];

        try (Connection cnn = dbConn.getConnection("legendPlus");
             PreparedStatement psCount = cnn.prepareStatement(queryCount);
             ResultSet rc = psCount.executeQuery()) {

            int rowCount = 0;

            if (rc.next()) {
                rowCount = rc.getInt(1);
            }

            values = new String[rowCount][4];

            try (PreparedStatement psSelect = cnn.prepareStatement(querySelect);
                 ResultSet rv = psSelect.executeQuery()) {

                for (int x = 0; x < rowCount; x++) {

                    if (!rv.next()) {
                        break; 
                    }

                    for (int y = 0; y < values[x].length; y++) {
                        values[x][y] = rv.getString(y + 1);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("INFO:Error selectSequIdentities() ->" + e.getMessage());
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
    public boolean processAutoIdentitiesOld(String a1, String a2, String a3,
                                         String a4, String a5, String a6,
                                         String a7, String dm, String fm,
                                         String us) throws Exception {
        //StringBuffer iq = new StringBuffer(100);

        // iq.append("am_msp_process_auto_identities" + " '" + a1 + "','" + a2 + "','" + a3 +
        // "','" + a4 + "','" + a5 + "','" + a6 + "','" + a7 + "','" + dm + "','" + fm + "','" + us + "'");
        boolean result = false;
       
        MagmaDBConnection dbConn = new MagmaDBConnection();

        String query = "insert into am_ad_auto_identity (priority1,priority2,priority3,priority4,priority5,priority6,priority7,delimiter,format,user_id,date_cr)" +
                       " values(?,?,?,?,?,?,?,?,?,?,?)";
        try {
            Connection cnn = dbConn.getConnection("legendPlus");
            PreparedStatement ps = cnn.prepareStatement(query);
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
        }

        return (result);
    }
    
    
    public boolean processAutoIdentities(String a1, String a2, String a3,
            String a4, String a5, String a6,
            String a7, String dm, String fm,
            String us) throws Exception {

		boolean result = false;
		MagmaDBConnection dbConn = new MagmaDBConnection();
		
		String query = "insert into am_ad_auto_identity " +
		"(priority1,priority2,priority3,priority4,priority5,priority6,priority7,delimiter,format,user_id,date_cr) " +
		"values(?,?,?,?,?,?,?,?,?,?,?)";
		
		try (Connection cnn = dbConn.getConnection("legendPlus");
		PreparedStatement ps = cnn.prepareStatement(query)) {
		
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
		
		// executeUpdate() is clearer for INSERT/UPDATE/DELETE
		result = ps.executeUpdate() > 0;
		
		} catch (Exception e) {
		System.out.println("INFO:Error processAutoIdentities() ->" + e.getMessage());
		 }
		
		return result;
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
    public boolean processCartIdentitiesOld(String[] iden, String[] cati,
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

                query = "insert into am_ad_cart_identity (cart_id,cart_st,cart_cr,user_id,date_cr) values (?,?,?,?,?)";
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

    public boolean processCartIdentities(String[] iden,
                                         String[] cati,
                                         String user) {

        if (iden == null || cati == null || iden.length != cati.length) {
            return false;
        }

        String query = "INSERT INTO am_ad_cart_identity " +
                "(cart_id, cart_st, cart_cr, user_id, date_cr) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = new MagmaDBConnection()
                .getConnection("legendPlus");
             PreparedStatement ps = con.prepareStatement(query)) {

            con.setAutoCommit(false);

            for (int i = 0; i < iden.length; i++) {

                ps.setInt(1, Integer.parseInt(iden[i]));
                ps.setInt(2, Integer.parseInt(cati[i]));
                ps.setInt(3, Integer.parseInt(cati[i])); // confirm if correct
                ps.setInt(4, Integer.parseInt(user));
                ps.setDate(5,
                        new java.sql.Date(System.currentTimeMillis()));

                ps.addBatch();
            }

            ps.executeBatch();
            con.commit();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * processAutoIdentity
     *
     * @return boolean
     * @throws Exception
     */
    public boolean resetStruIdentitiesOld() throws Exception {
        //StringBuffer iq = new StringBuffer(100);

        //iq.append("exec am_msp_reset_str_identities");

        MagmaDBConnection dbConn = new MagmaDBConnection();
        String query1 = "truncate table am_ad_auto_identity";
        String query2 = "truncate table am_ad_cart_identity";
        int x = 0;
        try {
            Connection cnn = dbConn.getConnection("legendPlus");
            PreparedStatement ps = cnn.prepareStatement(query1);
            x = ps.executeUpdate();
            ps = cnn.prepareStatement(query2);
            x = ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("INFO:Error resetStruIdentities() ->" +
                               e.getMessage());
        }
        return (x == -1);

        //return (getStatement().executeUpdate(
        //  iq.toString()) == -1);
    }

    
    public boolean resetStruIdentities() throws Exception {
        MagmaDBConnection dbConn = new MagmaDBConnection();
        String query1 = "truncate table am_ad_auto_identity";
        String query2 = "truncate table am_ad_cart_identity";
        boolean result = false;

        try (Connection cnn = dbConn.getConnection("legendPlus");
             PreparedStatement ps1 = cnn.prepareStatement(query1);
             PreparedStatement ps2 = cnn.prepareStatement(query2)) {

            // Execute first truncate
            ps1.executeUpdate();

            // Execute second truncate
            ps2.executeUpdate();

            result = true; 
        } catch (Exception e) {
            System.out.println("INFO:Error resetStruIdentities() ->" + e.getMessage());
        }

        return result;
    }
    
    /**
     * processAutoIdentity
     *
     * @return boolean
     * @throws Exception
     */
    public boolean resetSequIdentitiesOld() {
        // StringBuffer iq = new StringBuffer(100);
        // iq.append("exec am_msp_reset_seq_identities");


        boolean result = false;
        MagmaDBConnection dbConn = new MagmaDBConnection();
        String query = "truncate table am_ad_sequ_identity";
        try {
            Connection cnn = dbConn.getConnection("legendPlus");
            PreparedStatement ps = cnn.prepareStatement(query);
            result = ps.execute() ? false : true;
        } catch (Exception e) {
            System.out.println("INFO:Error resetSequIdentities() ->" +
                               e.getMessage());
        }
        //return (getStatement().executeUpdate(
        //  iq.toString()) == -1);
        return result;
    }
    
    
    public boolean resetSequIdentities() {
        boolean result = false;
        MagmaDBConnection dbConn = new MagmaDBConnection();
        String query = "truncate table am_ad_sequ_identity";

        try (Connection cnn = dbConn.getConnection("legendPlus");
             PreparedStatement ps = cnn.prepareStatement(query)) {

            result = ps.execute() ? false : true;

        } catch (Exception e) {
            System.out.println("INFO:Error resetSequIdentities() ->" + e.getMessage());
        }

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
    public boolean processSequIdentitiesOld(String cati, String user) {
        //StringBuffer iq = new StringBuffer(100);

        boolean result = false;
        MagmaDBConnection dbConn = new MagmaDBConnection();
        // iq.append("am_msp_process_sequ_identities" + " " + cati + ",'" + user + "'");

        String query =
                "insert into am_ad_sequ_identity (sequ_st,sequ_cr,user_id,date_cr) " +
                "values (?,?,?,?)";
        try {
            Connection cnn = dbConn.getConnection("legendPlus");
            PreparedStatement ps = cnn.prepareStatement(query);

            ps.setInt(1, Integer.parseInt(cati));
            ps.setInt(2, Integer.parseInt(cati));
            ps.setString(3, user);
            ps.setDate(4, dbConn.dateConvert(new java.util.Date()));
            result = ps.execute() ? false : true;
        } catch (Exception e) {
            System.out.println("INFO:Error processSequIdentities() ->" +
                               e.getMessage());
        }

        return (result);
    }
    
    
    public boolean processSequIdentities(String cati, String user) {
        boolean result = false;
        MagmaDBConnection dbConn = new MagmaDBConnection();
        String query =
            "insert into am_ad_sequ_identity (sequ_st,sequ_cr,user_id,date_cr) " +
            "values (?,?,?,?)";

        try (Connection cnn = dbConn.getConnection("legendPlus");
             PreparedStatement ps = cnn.prepareStatement(query)) {

            ps.setInt(1, Integer.parseInt(cati));
            ps.setInt(2, Integer.parseInt(cati));
            ps.setString(3, user);
            ps.setDate(4, dbConn.dateConvert(new java.util.Date()));

            result = ps.execute() ? false : true;

        } catch (Exception e) {
            System.out.println("INFO:Error processSequIdentities() ->" + e.getMessage());
        }

        return result;
    }
    

    /**
     * selectCartIdentity
     *
     * @return String[][]
     * @throws Exception
     */

    //modified by Olabo
    public boolean queryAssetsOld() {
        //StringBuffer cq = new StringBuffer(20);
        //cq.append("am_msp_query_assets");

        boolean result = false;
        MagmaDBConnection dbConn = new MagmaDBConnection();
        String query = "SELECT ASSET_ID FROM AM_ASSET";
        try {
            Connection cnn = dbConn.getConnection("legendPlus");
            PreparedStatement ps = cnn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result = true;
            }
        } catch (Exception e) {
            System.out.println("INFO:Error queryAssets() ->" +
                               e.getMessage());
        }

        return (result);

        //return getStatement().executeQuery(
        //         cq.toString()).next();
    }
    
    
    public boolean queryAssets() {
        boolean result = false;
        MagmaDBConnection dbConn = new MagmaDBConnection();
        String query = "SELECT ASSET_ID FROM AM_ASSET";

        try (Connection cnn = dbConn.getConnection("legendPlus");
             PreparedStatement ps = cnn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                result = true;
            }

        } catch (Exception e) {
            System.out.println("INFO:Error queryAssets() ->" + e.getMessage());
        }

        return result;
    }

    /**
     * selectCartIdentity
     *
     * @return String[][]
     * @throws Exception
     */
    //modified by Olabo
    public boolean queryAutoIdOld() throws Exception {
        //StringBuffer cq = new StringBuffer(20);
        //cq.append("am_msp_query_auto_id");

        MagmaDBConnection dbConn = new MagmaDBConnection();
        String query = "SELECT AUTO_GENERATE_ID FROM AM_GB_COMPANY";
        String result = new String();
        try {
            Connection cnn = dbConn.getConnection("legendPlus");
            PreparedStatement ps = cnn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result = rs.getString(1);
            }
        } catch (Exception e) {
            System.out.println("INFO:Error queryAutoId() ->" +
                               e.getMessage());
        }
        //ResultSet rs = getStatement().executeQuery(cq.toString());

        return (result.equals("Y"));
    }
    
    
    
    public boolean queryAutoId() throws Exception {
        MagmaDBConnection dbConn = new MagmaDBConnection();
        String query = "SELECT AUTO_GENERATE_ID FROM AM_GB_COMPANY";
        String result = "";

        try (Connection cnn = dbConn.getConnection("legendPlus");
             PreparedStatement ps = cnn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                result = rs.getString(1);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error queryAutoId() ->" + e.getMessage());
        }

        return "Y".equals(result);
    }

    
    
//    public String getIdentityOld(String bra, String dep, String sec, String cat) throws
//            Throwable {
////    	System.out.println("<<<<bra: "+bra+"   dep: "+dep+"  sec: "+sec+"   cat: "+cat);
//        StringBuffer sb = new StringBuffer(100);
//
//        String v1 = "", v2 = "", v3 = "", v4 = "", v5 = "", v6 = "", v7 = "",
//                dl = "";
//        int curr = 0;
//        String identity = "";
//
//        ResultSet rsa = getStatement().executeQuery(
//                "select * from am_ad_auto_identity");
//        ResultSet rsb = getStatement().executeQuery(
//                "select * from am_ad_cart_identity");
//
//        ResultSet rs1 = getStatement().executeQuery(
//                "select acronym from am_gb_company");
//        ResultSet rs2 = getStatement().executeQuery(
//                "select group_acronym from am_ad_group");
//        ResultSet rs3 = getStatement().executeQuery(
//                "select region_acronym from am_ad_region where region_code = (select region_code from am_ad_branch where branch_id = '" +
//                bra + "')");
//        ResultSet rs4 = getStatement().executeQuery(
//                "select branch_acronym from am_ad_branch where branch_id = '" +
//                bra + "'");
//        ResultSet rs5 = getStatement().executeQuery(
//                "select dept_acronym from am_ad_department where dept_id = '" +
//                dep + "'");
//        ResultSet rs6 = getStatement().executeQuery(
//                "select section_acronym from am_ad_section where section_id = '" +
//                sec + "'");
//        ResultSet rs7 = getStatement().executeQuery(
//                "select category_acronym from am_ad_category where category_id = '" +
//                cat + "'");
//        ResultSet rs8 = getStatement().executeQuery(
//                "select cart_cr from am_ad_cart_identity where cart_id = '" +
//                cat + "'");
//        ResultSet rs9 = getStatement().executeQuery(
//                "select sequ_cr from am_ad_sequ_identity");
//
//        if (rsa.next()) {
//            if (rsa.getString(1).equals("COMP")) {
//                if (rs1.next()) {
//                    v1 = rs1.getString(1);
//                } else {
//                    v1 = "";
//                }
//            } else if (rsa.getString(1).equals("GRPP")) {
//                if (rs2.next()) {
//                    v1 = rs2.getString(1);
//                } else {
//                    v1 = "";
//                }
//            } else if (rsa.getString(1).equals("REGN")) {
//                if (rs3.next()) {
//                    v1 = rs3.getString(1);
//                } else {
//                    v1 = "";
//                }
//            } else if (rsa.getString(1).equals("BRCH")) {
//                if (rs4.next()) {
//                    v1 = rs4.getString(1);
//                } else {
//                    v1 = "";
//                }
//            } else if (rsa.getString(1).equals("DEPT")) {
//                if (rs5.next()) {
//                    v1 = rs5.getString(1);
//                } else {
//                    v1 = "";
//                }
//            } else if (rsa.getString(1).equals("SECT")) {
//                if (rs6.next()) {
//                    v1 = rs6.getString(1);
//                } else {
//                    v1 = "";
//                }
//            } else if (rsa.getString(1).equals("CATG")) {
//                if (rs7.next()) {
//                    v1 = rs7.getString(1);
//                } else {
//                    v1 = "";
//                }
//            } else {
//                v1 = rsa.getString(1);
//            }
//
//            if (rsa.getString(2).equals("COMP")) {
//                if (rs1.next()) {
//                    v2 = rs1.getString(1);
//                } else {
//                    v2 = "";
//                }
//            } else if (rsa.getString(2).equals("GRPP")) {
//                if (rs2.next()) {
//                    v2 = rs2.getString(1);
//                } else {
//                    v2 = "";
//                }
//            } else if (rsa.getString(2).equals("REGN")) {
//                if (rs3.next()) {
//                    v2 = rs3.getString(1);
//                } else {
//                    v2 = "";
//                }
//            } else if (rsa.getString(2).equals("BRCH")) {
//                if (rs4.next()) {
//                    v2 = rs4.getString(1);
//                } else {
//                    v2 = "";
//                }
//            } else if (rsa.getString(2).equals("DEPT")) {
//                if (rs5.next()) {
//                    v2 = rs5.getString(1);
//                } else {
//                    v2 = "";
//                }
//            } else if (rsa.getString(2).equals("SECT")) {
//                if (rs6.next()) {
//                    v2 = rs6.getString(1);
//                } else {
//                    v2 = "";
//                }
//            } else if (rsa.getString(2).equals("CATG")) {
//                if (rs7.next()) {
//                    v2 = rs7.getString(1);
//                } else {
//                    v2 = "";
//                }
//            } else {
//                v2 = rsa.getString(2);
//            }
//
//            if (rsa.getString(3).equals("COMP")) {
//                if (rs1.next()) {
//                    v3 = rs1.getString(1);
//                } else {
//                    v3 = "";
//                }
//            } else if (rsa.getString(3).equals("GRPP")) {
//                if (rs2.next()) {
//                    v3 = rs2.getString(1);
//                } else {
//                    v3 = "";
//                }
//            } else if (rsa.getString(3).equals("REGN")) {
//                if (rs3.next()) {
//                    v3 = rs3.getString(1);
//                } else {
//                    v3 = "";
//                }
//            } else if (rsa.getString(3).equals("BRCH")) {
//                if (rs4.next()) {
//                    v3 = rs4.getString(1);
//                } else {
//                    v3 = "";
//                }
//            } else if (rsa.getString(3).equals("DEPT")) {
//                if (rs5.next()) {
//                    v3 = rs5.getString(1);
//                } else {
//                    v3 = "";
//                }
//            } else if (rsa.getString(3).equals("SECT")) {
//                if (rs6.next()) {
//                    v3 = rs6.getString(1);
//                } else {
//                    v3 = "";
//                }
//            } else if (rsa.getString(3).equals("CATG")) {
//                if (rs7.next()) {
//                    v3 = rs7.getString(1);
//                } else {
//                    v3 = "";
//                }
//            } else {
//                v3 = rsa.getString(3);
//            }
//
//            if (rsa.getString(4).equals("COMP")) {
//                if (rs1.next()) {
//                    v4 = rs1.getString(1);
//                } else {
//                    v4 = "";
//                }
//            } else if (rsa.getString(4).equals("GRPP")) {
//                if (rs2.next()) {
//                    v4 = rs2.getString(1);
//                } else {
//                    v4 = "";
//                }
//            } else if (rsa.getString(4).equals("REGN")) {
//                if (rs3.next()) {
//                    v4 = rs3.getString(1);
//                } else {
//                    v4 = "";
//                }
//            } else if (rsa.getString(4).equals("BRCH")) {
//                if (rs4.next()) {
//                    v4 = rs4.getString(1);
//                } else {
//                    v4 = "";
//                }
//            } else if (rsa.getString(4).equals("DEPT")) {
//                if (rs5.next()) {
//                    v4 = rs5.getString(1);
//                } else {
//                    v4 = "";
//                }
//            } else if (rsa.getString(4).equals("SECT")) {
//                if (rs6.next()) {
//                    v4 = rs6.getString(1);
//                } else {
//                    v4 = "";
//                }
//            } else if (rsa.getString(4).equals("CATG")) {
//                if (rs7.next()) {
//                    v4 = rs7.getString(1);
//                } else {
//                    v4 = "";
//                }
//            } else {
//                v4 = rsa.getString(4);
//            }
//
//            if (rsa.getString(5).equals("COMP")) {
//                if (rs1.next()) {
//                    v5 = rs1.getString(1);
//                } else {
//                    v5 = "";
//                }
//            } else if (rsa.getString(5).equals("GRPP")) {
//                if (rs2.next()) {
//                    v5 = rs2.getString(1);
//                } else {
//                    v5 = "";
//                }
//            } else if (rsa.getString(5).equals("REGN")) {
//                if (rs3.next()) {
//                    v5 = rs3.getString(1);
//                } else {
//                    v5 = "";
//                }
//            } else if (rsa.getString(5).equals("BRCH")) {
//                if (rs4.next()) {
//                    v5 = rs4.getString(1);
//                } else {
//                    v5 = "";
//                }
//            } else if (rsa.getString(5).equals("DEPT")) {
//                if (rs5.next()) {
//                    v5 = rs5.getString(1);
//                } else {
//                    v5 = "";
//                }
//            } else if (rsa.getString(5).equals("SECT")) {
//                if (rs6.next()) {
//                    v5 = rs6.getString(1);
//                } else {
//                    v5 = "";
//                }
//            } else if (rsa.getString(5).equals("CATG")) {
//                if (rs7.next()) {
//                    v5 = rs7.getString(1);
//                } else {
//                    v5 = "";
//                }
//            } else {
//                v5 = rsa.getString(5);
//            }
//
//            if (rsa.getString(6).equals("COMP")) {
//                if (rs1.next()) {
//                    v6 = rs1.getString(1);
//                } else {
//                    v6 = "";
//                }
//            } else if (rsa.getString(6).equals("GRPP")) {
//                if (rs2.next()) {
//                    v6 = rs2.getString(1);
//                } else {
//                    v6 = "";
//                }
//            } else if (rsa.getString(6).equals("REGN")) {
//                if (rs3.next()) {
//                    v6 = rs3.getString(1);
//                } else {
//                    v6 = "";
//                }
//            } else if (rsa.getString(6).equals("BRCH")) {
//                if (rs4.next()) {
//                    v6 = rs4.getString(1);
//                } else {
//                    v6 = "";
//                }
//            } else if (rsa.getString(6).equals("DEPT")) {
//                if (rs5.next()) {
//                    v6 = rs5.getString(1);
//                } else {
//                    v6 = "";
//                }
//            } else if (rsa.getString(6).equals("SECT")) {
//                if (rs6.next()) {
//                    v6 = rs6.getString(1);
//                } else {
//                    v6 = "";
//                }
//            } else if (rsa.getString(6).equals("CATG")) {
//                if (rs7.next()) {
//                    v6 = rs7.getString(1);
//                } else {
//                    v6 = "";
//                }
//            } else {
//                v6 = rsa.getString(6);
//            }
//
//            if (rsa.getString(7).equals("COMP")) {
//                if (rs1.next()) {
//                    v7 = rs1.getString(1);
//                } else {
//                    v7 = "";
//                }
//            } else if (rsa.getString(7).equals("GRPP")) {
//                if (rs2.next()) {
//                    v7 = rs2.getString(1);
//                } else {
//                    v7 = "";
//                }
//            } else if (rsa.getString(7).equals("REGN")) {
//                if (rs3.next()) {
//                    v7 = rs3.getString(1);
//                } else {
//                    v7 = "";
//                }
//            } else if (rsa.getString(7).equals("BRCH")) {
//                if (rs4.next()) {
//                    v7 = rs4.getString(1);
//                } else {
//                    v7 = "";
//                }
//            } else if (rsa.getString(7).equals("DEPT")) {
//                if (rs5.next()) {
//                    v7 = rs5.getString(1);
//                } else {
//                    v7 = "";
//                }
//            } else if (rsa.getString(7).equals("SECT")) {
//                if (rs6.next()) {
//                    v7 = rs6.getString(1);
//                } else {
//                    v7 = "";
//                }
//            } else if (rsa.getString(7).equals("CATG")) {
//                if (rs7.next()) {
//                    v7 = rs7.getString(1);
//                } else {
//                    v7 = "";
//                }
//            } else {
//                v7 = rsa.getString(7);
//            }
//
//            dl = rsa.getString(8);
//
//            if (!v1.equals("")) {
//                sb.append(v1 + dl);
//            }
//            if (!v2.equals("")) {
//                sb.append(v2 + dl);
//            }
//            if (!v3.equals("")) {
//                sb.append(v3 + dl);
//            }
//            if (!v4.equals("")) {
//                sb.append(v4 + dl);
//            }
//            if (!v5.equals("")) {
//                sb.append(v5 + dl);
//            }
//            if (!v6.equals("")) {
//                sb.append(v6 + dl);
//            }
//            if (!v7.equals("")) {
//                sb.append(v7 + dl);
//            }
//
//            rs8.next();
//            curr = rs8.getInt(1);
//            ++curr;
////            System.out.println("<<<<SB: "+sb.toString());
//            getStatement().executeUpdate(
//                    "update am_ad_cart_identity set cart_cr = " + curr +
//                    " where cart_id = (select category_id from am_ad_category where category_id = " +
//                    "'" + cat + "')");
//
//            identity = sb.toString() + (curr - 1);
//            
//        } else if (rsb.next()) {
//            rs9.next();
//            curr = rs9.getInt(1);
//            ++curr;
//
//            getStatement().executeUpdate(
//                    "update am_ad_sequ_identity set sequ_cr = " + curr);
//
//            identity = String.valueOf(curr - 1);
//        }
// //freeResource();
//// System.out.println("<<<<SBA: "+identity);
//        return identity;
//    }

    
    public String getIdentityOld(String bra, String dep, String sec, String cat) throws SQLException {
        StringBuilder sb = new StringBuilder(100);
        int curr = 0;
        String identity = "";
        MagmaDBConnection dbConnection = new MagmaDBConnection();

        try (Connection con = dbConnection.getConnection("legendPlus");
             Statement stmt = con.createStatement()) {

            // Load auto identity
            ResultSet rsa = stmt.executeQuery("SELECT * FROM am_ad_auto_identity");
            if (!rsa.next()) {
                // fallback: check cart identity
                ResultSet rsb = stmt.executeQuery("SELECT * FROM am_ad_cart_identity");
                if (rsb.next()) {
                    ResultSet rsSeq = stmt.executeQuery("SELECT sequ_cr FROM am_ad_sequ_identity");
                    if (rsSeq.next()) {
                        curr = rsSeq.getInt(1) + 1;
                        stmt.executeUpdate("UPDATE am_ad_sequ_identity SET sequ_cr = " + curr);
                        return String.valueOf(curr - 1);
                    }
                }
                return ""; // no identity found
            }

            // Load all acronym values once
            String company = getSingleValue(stmt, "SELECT acronym FROM am_gb_company");
            String group = getSingleValue(stmt, "SELECT group_acronym FROM am_ad_group");
            String region = getSingleValue(stmt, 
                "SELECT region_acronym FROM am_ad_region WHERE region_code = " +
                "(SELECT region_code FROM am_ad_branch WHERE branch_id = ?)", bra);
            String branch = getSingleValue(stmt, 
                "SELECT branch_acronym FROM am_ad_branch WHERE branch_id = ?", bra);
            String dept = getSingleValue(stmt, 
                "SELECT dept_acronym FROM am_ad_department WHERE dept_id = ?", dep);
            String section = getSingleValue(stmt, 
                "SELECT section_acronym FROM am_ad_section WHERE section_id = ?", sec);
            String category = getSingleValue(stmt, 
                "SELECT category_acronym FROM am_ad_category WHERE category_id = ?", cat);

            Map<String, String> acronymMap = Map.of(
                "COMP", company,
                "GRPP", group,
                "REGN", region,
                "BRCH", branch,
                "DEPT", dept,
                "SECT", section,
                "CATG", category
            );

            // Loop through 7 identity fields
            for (int i = 1; i <= 7; i++) {
                String key = rsa.getString(i);
                String val = acronymMap.getOrDefault(key, key != null ? key : "");
                if (!val.isEmpty()) {
                    sb.append(val).append(rsa.getString(8)); // delimiter
                }
            }

            // Update cart
            ResultSet rsCart = stmt.executeQuery(
                "SELECT cart_cr FROM am_ad_cart_identity WHERE cart_id = '" + cat + "'");
            if (rsCart.next()) {
                curr = rsCart.getInt(1) + 1;
                stmt.executeUpdate(
                    "UPDATE am_ad_cart_identity SET cart_cr = " + curr +
                    " WHERE cart_id = '" + cat + "'"
                );
            }

            identity = sb.toString() + (curr - 1);
        }

        return identity;
    }

    // Utility method for single value retrieval
    private String getSingleValue(Statement stmt, String sql) throws SQLException {
        try (ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getString(1) : "";
        }
    }

    // Overloaded for prepared statements with parameters
    private String getSingleValue(Statement stmt, String sql, String param) throws SQLException {
        try (PreparedStatement ps = stmt.getConnection().prepareStatement(sql)) {
            ps.setString(1, param);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString(1) : "";
            }
        }
    }
//    public String getIdentityforStock2Old(String bra, String dep, String sec, String cat) throws
//            Throwable {
////    	System.out.println("<<<<bra in getIdentityforStock: "+bra+"   dep: "+dep+"  sec: "+sec+"   cat: "+cat);
//        StringBuffer sb = new StringBuffer(100);
//
//        String v1 = "", v2 = "", v3 = "", v4 = "", v5 = "", v6 = "", v7 = "",
//                dl = "";
//        int curr = 0;
//        String identity = "";
//
//        ResultSet rsa = getStatement().executeQuery(
//                "select * from am_ad_auto_identity");
//        ResultSet rsb = getStatement().executeQuery(
//                "select * from am_ad_cart_identity");
//
//        ResultSet rs1 = getStatement().executeQuery(
//                "select acronym from am_gb_company");
//        ResultSet rs2 = getStatement().executeQuery(
//                "select group_acronym from am_ad_group");
//        ResultSet rs3 = getStatement().executeQuery(
//                "select region_acronym from am_ad_region where region_code = (select region_code from am_ad_branch where branch_id = '" +
//                bra + "')");
//        ResultSet rs4 = getStatement().executeQuery(
//                "select branch_acronym from am_ad_branch where branch_id = '" +
//                bra + "'");
//        ResultSet rs5 = getStatement().executeQuery(
//                "select dept_acronym from am_ad_department where dept_id = '" +
//                dep + "'");
//        ResultSet rs6 = getStatement().executeQuery(
//                "select section_acronym from am_ad_section where section_id = '" +
//                sec + "'");
//        ResultSet rs7 = getStatement().executeQuery(
//                "select category_acronym from ST_STOCK_CATEGORY where category_id = '" +
//                cat + "'");
//        ResultSet rs8 = getStatement().executeQuery(
//                "select cart_cr from ST_STOCK_CART_IDENTITY where cart_id = '" +
//                cat + "'");
//        ResultSet rs9 = getStatement().executeQuery(
//                "select sequ_cr from ST_STOCK_SEQU_IDENTITY");
////        System.out.println("<<<<>>>>>>in getIdentityforStock: ");
//        if (rsa.next()) {
//            if (rsa.getString(1).equals("COMP")) {
//                if (rs1.next()) {
//                    v1 = rs1.getString(1);
//                } else {
//                    v1 = "";
//                }
//            } else if (rsa.getString(1).equals("GRPP")) {
//                if (rs2.next()) {
//                    v1 = rs2.getString(1);
//                } else {
//                    v1 = "";
//                }
//            } else if (rsa.getString(1).equals("REGN")) {
//                if (rs3.next()) {
//                    v1 = rs3.getString(1);
//                } else {
//                    v1 = "";
//                }
//            } else if (rsa.getString(1).equals("BRCH")) {
//                if (rs4.next()) {
//                    v1 = rs4.getString(1);
//                } else {
//                    v1 = "";
//                }
//            } else if (rsa.getString(1).equals("DEPT")) {
//                if (rs5.next()) {
//                    v1 = rs5.getString(1);
//                } else {
//                    v1 = "";
//                }
//            } else if (rsa.getString(1).equals("SECT")) {
//                if (rs6.next()) {
//                    v1 = rs6.getString(1);
//                } else {
//                    v1 = "";
//                }
//            } else if (rsa.getString(1).equals("CATG")) {
//                if (rs7.next()) {
//                    v1 = rs7.getString(1);
//                } else {
//                    v1 = "";
//                }
//            } else {
//                v1 = rsa.getString(1);
//            }
//
//            if (rsa.getString(2).equals("COMP")) {
//                if (rs1.next()) {
//                    v2 = rs1.getString(1);
//                } else {
//                    v2 = "";
//                }
//            } else if (rsa.getString(2).equals("GRPP")) {
//                if (rs2.next()) {
//                    v2 = rs2.getString(1);
//                } else {
//                    v2 = "";
//                }
//            } else if (rsa.getString(2).equals("REGN")) {
//                if (rs3.next()) {
//                    v2 = rs3.getString(1);
//                } else {
//                    v2 = "";
//                }
//            } else if (rsa.getString(2).equals("BRCH")) {
//                if (rs4.next()) {
//                    v2 = rs4.getString(1);
//                } else {
//                    v2 = "";
//                }
//            } else if (rsa.getString(2).equals("DEPT")) {
//                if (rs5.next()) {
//                    v2 = rs5.getString(1);
//                } else {
//                    v2 = "";
//                }
//            } else if (rsa.getString(2).equals("SECT")) {
//                if (rs6.next()) {
//                    v2 = rs6.getString(1);
//                } else {
//                    v2 = "";
//                }
//            } else if (rsa.getString(2).equals("CATG")) {
//                if (rs7.next()) {
//                    v2 = rs7.getString(1);
//                } else {
//                    v2 = "";
//                }
//            } else {
//                v2 = rsa.getString(2);
//            }
//
//            if (rsa.getString(3).equals("COMP")) {
//                if (rs1.next()) {
//                    v3 = rs1.getString(1);
//                } else {
//                    v3 = "";
//                }
//            } else if (rsa.getString(3).equals("GRPP")) {
//                if (rs2.next()) {
//                    v3 = rs2.getString(1);
//                } else {
//                    v3 = "";
//                }
//            } else if (rsa.getString(3).equals("REGN")) {
//                if (rs3.next()) {
//                    v3 = rs3.getString(1);
//                } else {
//                    v3 = "";
//                }
//            } else if (rsa.getString(3).equals("BRCH")) {
//                if (rs4.next()) {
//                    v3 = rs4.getString(1);
//                } else {
//                    v3 = "";
//                }
//            } else if (rsa.getString(3).equals("DEPT")) {
//                if (rs5.next()) {
//                    v3 = rs5.getString(1);
//                } else {
//                    v3 = "";
//                }
//            } else if (rsa.getString(3).equals("SECT")) {
//                if (rs6.next()) {
//                    v3 = rs6.getString(1);
//                } else {
//                    v3 = "";
//                }
//            } else if (rsa.getString(3).equals("CATG")) {
//                if (rs7.next()) {
//                    v3 = rs7.getString(1);
//                } else {
//                    v3 = "";
//                }
//            } else {
//                v3 = rsa.getString(3);
//            }
//
//            if (rsa.getString(4).equals("COMP")) {
//                if (rs1.next()) {
//                    v4 = rs1.getString(1);
//                } else {
//                    v4 = "";
//                }
//            } else if (rsa.getString(4).equals("GRPP")) {
//                if (rs2.next()) {
//                    v4 = rs2.getString(1);
//                } else {
//                    v4 = "";
//                }
//            } else if (rsa.getString(4).equals("REGN")) {
//                if (rs3.next()) {
//                    v4 = rs3.getString(1);
//                } else {
//                    v4 = "";
//                }
//            } else if (rsa.getString(4).equals("BRCH")) {
//                if (rs4.next()) {
//                    v4 = rs4.getString(1);
//                } else {
//                    v4 = "";
//                }
//            } else if (rsa.getString(4).equals("DEPT")) {
//                if (rs5.next()) {
//                    v4 = rs5.getString(1);
//                } else {
//                    v4 = "";
//                }
//            } else if (rsa.getString(4).equals("SECT")) {
//                if (rs6.next()) {
//                    v4 = rs6.getString(1);
//                } else {
//                    v4 = "";
//                }
//            } else if (rsa.getString(4).equals("CATG")) {
//                if (rs7.next()) {
//                    v4 = rs7.getString(1);
//                } else {
//                    v4 = "";
//                }
//            } else {
//                v4 = rsa.getString(4);
//            }
//
//            if (rsa.getString(5).equals("COMP")) {
//                if (rs1.next()) {
//                    v5 = rs1.getString(1);
//                } else {
//                    v5 = "";
//                }
//            } else if (rsa.getString(5).equals("GRPP")) {
//                if (rs2.next()) {
//                    v5 = rs2.getString(1);
//                } else {
//                    v5 = "";
//                }
//            } else if (rsa.getString(5).equals("REGN")) {
//                if (rs3.next()) {
//                    v5 = rs3.getString(1);
//                } else {
//                    v5 = "";
//                }
//            } else if (rsa.getString(5).equals("BRCH")) {
//                if (rs4.next()) {
//                    v5 = rs4.getString(1);
//                } else {
//                    v5 = "";
//                }
//            } else if (rsa.getString(5).equals("DEPT")) {
//                if (rs5.next()) {
//                    v5 = rs5.getString(1);
//                } else {
//                    v5 = "";
//                }
//            } else if (rsa.getString(5).equals("SECT")) {
//                if (rs6.next()) {
//                    v5 = rs6.getString(1);
//                } else {
//                    v5 = "";
//                }
//            } else if (rsa.getString(5).equals("CATG")) {
//                if (rs7.next()) {
//                    v5 = rs7.getString(1);
//                } else {
//                    v5 = "";
//                }
//            } else {
//                v5 = rsa.getString(5);
//            }
//
//            if (rsa.getString(6).equals("COMP")) {
//                if (rs1.next()) {
//                    v6 = rs1.getString(1);
//                } else {
//                    v6 = "";
//                }
//            } else if (rsa.getString(6).equals("GRPP")) {
//                if (rs2.next()) {
//                    v6 = rs2.getString(1);
//                } else {
//                    v6 = "";
//                }
//            } else if (rsa.getString(6).equals("REGN")) {
//                if (rs3.next()) {
//                    v6 = rs3.getString(1);
//                } else {
//                    v6 = "";
//                }
//            } else if (rsa.getString(6).equals("BRCH")) {
//                if (rs4.next()) {
//                    v6 = rs4.getString(1);
//                } else {
//                    v6 = "";
//                }
//            } else if (rsa.getString(6).equals("DEPT")) {
//                if (rs5.next()) {
//                    v6 = rs5.getString(1);
//                } else {
//                    v6 = "";
//                }
//            } else if (rsa.getString(6).equals("SECT")) {
//                if (rs6.next()) {
//                    v6 = rs6.getString(1);
//                } else {
//                    v6 = "";
//                }
//            } else if (rsa.getString(6).equals("CATG")) {
//                if (rs7.next()) {
//                    v6 = rs7.getString(1);
//                } else {
//                    v6 = "";
//                }
//            } else {
//                v6 = rsa.getString(6);
//            }
//
//            if (rsa.getString(7).equals("COMP")) {
//                if (rs1.next()) {
//                    v7 = rs1.getString(1);
//                } else {
//                    v7 = "";
//                }
//            } else if (rsa.getString(7).equals("GRPP")) {
//                if (rs2.next()) {
//                    v7 = rs2.getString(1);
//                } else {
//                    v7 = "";
//                }
//            } else if (rsa.getString(7).equals("REGN")) {
//                if (rs3.next()) {
//                    v7 = rs3.getString(1);
//                } else {
//                    v7 = "";
//                }
//            } else if (rsa.getString(7).equals("BRCH")) {
//                if (rs4.next()) {
//                    v7 = rs4.getString(1);
//                } else {
//                    v7 = "";
//                }
//            } else if (rsa.getString(7).equals("DEPT")) {
//                if (rs5.next()) {
//                    v7 = rs5.getString(1);
//                } else {
//                    v7 = "";
//                }
//            } else if (rsa.getString(7).equals("SECT")) {
//                if (rs6.next()) {
//                    v7 = rs6.getString(1);
//                } else {
//                    v7 = "";
//                }
//            } else if (rsa.getString(7).equals("CATG")) {
//                if (rs7.next()) {
//                    v7 = rs7.getString(1);
//                } else {
//                    v7 = "";
//                }
//            } else {
//                v7 = rsa.getString(7);
//            }
//
//            dl = rsa.getString(8);
//
//            if (!v1.equals("")) {
//                sb.append(v1 + dl);
//            }
//            if (!v2.equals("")) {
//                sb.append(v2 + dl);
//            }
//            if (!v3.equals("")) {
//                sb.append(v3 + dl);
//            }
//            if (!v4.equals("")) {
//                sb.append(v4 + dl);
//            }
//            if (!v5.equals("")) {
//                sb.append(v5 + dl);
//            }
//            if (!v6.equals("")) {
//                sb.append(v6 + dl);
//            }
//            if (!v7.equals("")) {
//                sb.append(v7 + dl);
//            }
//
//            rs8.next();
//            curr = rs8.getInt(1);
//            ++curr;
////            System.out.println("<<<<SB: "+sb.toString());
//            getStatement().executeUpdate(
//                    "update ST_STOCK_CART_IDENTITY set cart_cr = " + curr +
//                    " where cart_id = (select category_id from ST_STOCK_CATEGORY where category_id = " +
//                    "'" + cat + "')");
//
//            identity = sb.toString() + (curr - 1);
//            
//        } else if (rsb.next()) {
//            rs9.next();
//            curr = rs9.getInt(1);
//            ++curr;
//
//            getStatement().executeUpdate(
//                    "update am_ad_sequ_identity set sequ_cr = " + curr);
//
//            identity = String.valueOf(curr - 1);
//        }
// freeResource();
//// System.out.println("<<<<SBA: "+identity);
//        return identity;
//    }
    
    public String getIdentityforStock2Old(String bra, String dep, String sec, String cat) throws SQLException {
        String identity = "";
        StringBuilder sb = new StringBuilder();
        int curr = 0;

        try (Connection conn = getConnection()) {

            // Fetch auto identity configuration
            String rsaQuery = "SELECT * FROM am_ad_auto_identity";
            try (Statement stmt = conn.createStatement();
                 ResultSet rsa = stmt.executeQuery(rsaQuery)) {

                if (!rsa.next()) {
                    return ""; // nothing to do
                }

                // Map code → value
                Map<String, String> codeMap = new HashMap<>();
                codeMap.put("COMP", getSingleValue(conn, "SELECT acronym FROM am_gb_company"));
                codeMap.put("GRPP", getSingleValue(conn, "SELECT group_acronym FROM am_ad_group"));
                codeMap.put("REGN",
                        getSingleValue(conn,
                            "SELECT region_acronym FROM am_ad_region WHERE region_code = " +
                            "(SELECT region_code FROM am_ad_branch WHERE branch_id = ?)",
                            bra));
                codeMap.put("BRCH", getSingleValue(conn, "SELECT branch_acronym FROM am_ad_branch WHERE branch_id = ?", bra));
                codeMap.put("DEPT", getSingleValue(conn, "SELECT dept_acronym FROM am_ad_department WHERE dept_id = ?", dep));
                codeMap.put("SECT", getSingleValue(conn, "SELECT section_acronym FROM am_ad_section WHERE section_id = ?", sec));
                codeMap.put("CATG", getSingleValue(conn, "SELECT category_acronym FROM ST_STOCK_CATEGORY WHERE category_id = ?", cat));

                String dl = rsa.getString(8);
                for (int i = 1; i <= 7; i++) {
                    String code = rsa.getString(i);
                    if (codeMap.containsKey(code)) {
                        sb.append(codeMap.get(code)).append(dl);
                    } else if (!code.isEmpty()) {
                        sb.append(code).append(dl);
                    }
                }

                // Increment cart counter
                curr = getSingleInt(conn, "SELECT cart_cr FROM ST_STOCK_CART_IDENTITY WHERE cart_id = ?", cat) + 1;
                try (PreparedStatement ps = conn.prepareStatement(
                        "UPDATE ST_STOCK_CART_IDENTITY SET cart_cr = ? WHERE cart_id = ?")) {
                    ps.setInt(1, curr);
                    ps.setString(2, cat);
                    ps.executeUpdate();
                }

                identity = sb.toString() + (curr - 1);
            }
        }

        return identity;
    }

    // Helper to get a single string
    private String getSingleValue(Connection conn, String sql, String... params) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) ps.setString(i + 1, params[i]);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString(1) : "";
            }
        }
    }

    // Helper to get a single int
    private int getSingleInt(Connection conn, String sql, String... params) throws SQLException {
        String val = getSingleValue(conn, sql, params);
        return val.isEmpty() ? 0 : Integer.parseInt(val);
    }
    
    public String getIdentityforStock2(String bra, String dep, String sec, String cat) throws Throwable {
        StringBuilder sb = new StringBuilder(100);
        String[] v = new String[7];
        String dl = "";
        int curr = 0;
        String identity = "";

        MagmaDBConnection dbConn = new MagmaDBConnection();

        try (Connection cnn = dbConn.getConnection("legendPlus")) {

            // Load all reference ResultSets safely
            try (PreparedStatement rsaPs = cnn.prepareStatement("select * from am_ad_auto_identity");
                 ResultSet rsa = rsaPs.executeQuery();
                 PreparedStatement rsbPs = cnn.prepareStatement("select * from am_ad_cart_identity");
                 ResultSet rsb = rsbPs.executeQuery();
                 PreparedStatement rs1Ps = cnn.prepareStatement("select acronym from am_gb_company");
                 ResultSet rs1 = rs1Ps.executeQuery();
                 PreparedStatement rs2Ps = cnn.prepareStatement("select group_acronym from am_ad_group");
                 ResultSet rs2 = rs2Ps.executeQuery();
                 PreparedStatement rs3Ps = cnn.prepareStatement(
                     "select region_acronym from am_ad_region where region_code = (select region_code from am_ad_branch where branch_id = ?)"
                 )) {

                try (PreparedStatement rs3Stmt = cnn.prepareStatement(
                         "select region_acronym from am_ad_region where region_code = (select region_code from am_ad_branch where branch_id = ?)"
                     )) {
                    rs3Stmt.setString(1, bra);
                    try (ResultSet rs3 = rs3Stmt.executeQuery()) {

                        try (PreparedStatement rs4Ps = cnn.prepareStatement(
                                 "select branch_acronym from am_ad_branch where branch_id = ?")) {
                            rs4Ps.setString(1, bra);
                            try (ResultSet rs4 = rs4Ps.executeQuery();
                                 PreparedStatement rs5Ps = cnn.prepareStatement(
                                     "select dept_acronym from am_ad_department where dept_id = ?")) {
                                rs5Ps.setString(1, dep);
                                try (ResultSet rs5 = rs5Ps.executeQuery();
                                     PreparedStatement rs6Ps = cnn.prepareStatement(
                                         "select section_acronym from am_ad_section where section_id = ?")) {
                                    rs6Ps.setString(1, sec);
                                    try (ResultSet rs6 = rs6Ps.executeQuery();
                                         PreparedStatement rs7Ps = cnn.prepareStatement(
                                             "select category_acronym from ST_STOCK_CATEGORY where category_id = ?")) {
                                        rs7Ps.setString(1, cat);
                                        try (ResultSet rs7 = rs7Ps.executeQuery();
                                             PreparedStatement rs8Ps = cnn.prepareStatement(
                                                 "select cart_cr from ST_STOCK_CART_IDENTITY where cart_id = ?")) {
                                            rs8Ps.setString(1, cat);
                                            try (ResultSet rs8 = rs8Ps.executeQuery();
                                                 PreparedStatement rs9Ps = cnn.prepareStatement(
                                                     "select sequ_cr from ST_STOCK_SEQU_IDENTITY");
                                                 ResultSet rs9 = rs9Ps.executeQuery()) {

                                                if (rsa.next()) {
                                                    dl = rsa.getString(8);

                                                    // Simplify repetitive logic using loop
                                                    for (int i = 0; i < 7; i++) {
                                                        String key = rsa.getString(i + 1);
                                                        switch (key) {
                                                            case "COMP": v[i] = rs1.next() ? rs1.getString(1) : ""; break;
                                                            case "GRPP": v[i] = rs2.next() ? rs2.getString(1) : ""; break;
                                                            case "REGN": v[i] = rs3.next() ? rs3.getString(1) : ""; break;
                                                            case "BRCH": v[i] = rs4.next() ? rs4.getString(1) : ""; break;
                                                            case "DEPT": v[i] = rs5.next() ? rs5.getString(1) : ""; break;
                                                            case "SECT": v[i] = rs6.next() ? rs6.getString(1) : ""; break;
                                                            case "CATG": v[i] = rs7.next() ? rs7.getString(1) : ""; break;
                                                            default: v[i] = key;
                                                        }
                                                        if (!v[i].isEmpty()) sb.append(v[i]).append(dl);
                                                    }

                                                    // Update cart identity
                                                    if (rs8.next()) {
                                                        curr = rs8.getInt(1) + 1;
                                                        try (PreparedStatement updatePs = cnn.prepareStatement(
                                                                 "update ST_STOCK_CART_IDENTITY set cart_cr = ? where cart_id = ?"
                                                             )) {
                                                            updatePs.setInt(1, curr);
                                                            updatePs.setString(2, cat);
                                                            updatePs.executeUpdate();
                                                        }
                                                    }

                                                    identity = sb.toString() + (curr - 1);

                                                } else if (rsb.next()) {
                                                    if (rs9.next()) {
                                                        curr = rs9.getInt(1) + 1;
                                                        try (PreparedStatement updatePs = cnn.prepareStatement(
                                                                 "update am_ad_sequ_identity set sequ_cr = ?"
                                                             )) {
                                                            updatePs.setInt(1, curr);
                                                            updatePs.executeUpdate();
                                                        }
                                                    }
                                                    identity = String.valueOf(curr - 1);
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return identity;
    }
    
    

//    public String getIdentityOld(String bra, String dep, String sec, String cat,String identity) throws
//    Throwable {
////    	magma.AssetRecordsBean bd = null;
////    for (int i = 0; i < list.size(); i++) {
////    	bd = (magma.AssetRecordsBean) list.get(i);
////    	 public String getIdentity(String bra, String dep, String sec, String cat) throws 	
////    	cat = bd.getCategory_id();
// //       String bra = bd.getNewbranch_id();
// //       String dep = bd.getNewdepartment_id(); 
////        String sec = bd.getNewsection_id();
//StringBuffer sb = new StringBuffer(100);
//
//String v1 = "", v2 = "", v3 = "", v4 = "", v5 = "", v6 = "", v7 = "",
//        dl = "";
//int curr = 0;
////String identity = "";
//
//ResultSet rsa = getStatement().executeQuery(
//        "select * from am_ad_auto_identity");
//ResultSet rsb = getStatement().executeQuery(
//        "select * from am_ad_cart_identity");
//
//ResultSet rs1 = getStatement().executeQuery(
//        "select acronym from am_gb_company");
//ResultSet rs2 = getStatement().executeQuery(
//        "select group_acronym from am_ad_group");
//ResultSet rs3 = getStatement().executeQuery(
//        "select region_acronym from am_ad_region where region_id = (select region_Code from am_ad_branch where branch_id = '" +
//        bra + "')");
//ResultSet rs4 = getStatement().executeQuery(
//        "select branch_acronym from am_ad_branch where branch_id = '" +
//        bra + "'");
//ResultSet rs5 = getStatement().executeQuery(
//        "select dept_acronym from am_ad_department where dept_id = '" +
//        dep + "'");
//ResultSet rs6 = getStatement().executeQuery(
//        "select section_acronym from am_ad_section where section_id = '" +
//        sec + "'");
//ResultSet rs7 = getStatement().executeQuery(
//        "select category_acronym from am_ad_category where category_id = '" +
//        cat + "'");
//ResultSet rs8 = getStatement().executeQuery(
//        "select cart_cr from am_ad_cart_identity where cart_id = '" +
//        cat + "'");
//ResultSet rs9 = getStatement().executeQuery(
//        "select sequ_cr from am_ad_sequ_identity");
//
//if (rsa.next()) {
//    if (rsa.getString(1).equals("COMP")) {
//        if (rs1.next()) {
//            v1 = rs1.getString(1);
//        } else {
//            v1 = "";
//        }
//    } else if (rsa.getString(1).equals("GRPP")) {
//        if (rs2.next()) {
//            v1 = rs2.getString(1);
//        } else {
//            v1 = "";
//        }
//    } else if (rsa.getString(1).equals("REGN")) {
//        if (rs3.next()) {
//            v1 = rs3.getString(1);
//        } else {
//            v1 = "";
//        }
//    } else if (rsa.getString(1).equals("BRCH")) {
//        if (rs4.next()) {
//            v1 = rs4.getString(1);
//        } else {
//            v1 = "";
//        }
//    } else if (rsa.getString(1).equals("DEPT")) {
//        if (rs5.next()) {
//            v1 = rs5.getString(1);
//        } else {
//            v1 = "";
//        }
//    } else if (rsa.getString(1).equals("SECT")) {
//        if (rs6.next()) {
//            v1 = rs6.getString(1);
//        } else {
//            v1 = "";
//        }
//    } else if (rsa.getString(1).equals("CATG")) {
//        if (rs7.next()) {
//            v1 = rs7.getString(1);
//        } else {
//            v1 = "";
//        }
//    } else {
//        v1 = rsa.getString(1);
//    }
//
//    if (rsa.getString(2).equals("COMP")) {
//        if (rs1.next()) {
//            v2 = rs1.getString(1);
//        } else {
//            v2 = "";
//        }
//    } else if (rsa.getString(2).equals("GRPP")) {
//        if (rs2.next()) {
//            v2 = rs2.getString(1);
//        } else {
//            v2 = "";
//        }
//    } else if (rsa.getString(2).equals("REGN")) {
//        if (rs3.next()) {
//            v2 = rs3.getString(1);
//        } else {
//            v2 = "";
//        }
//    } else if (rsa.getString(2).equals("BRCH")) {
//        if (rs4.next()) {
//            v2 = rs4.getString(1);
//        } else {
//            v2 = "";
//        }
//    } else if (rsa.getString(2).equals("DEPT")) {
//        if (rs5.next()) {
//            v2 = rs5.getString(1);
//        } else {
//            v2 = "";
//        }
//    } else if (rsa.getString(2).equals("SECT")) {
//        if (rs6.next()) {
//            v2 = rs6.getString(1);
//        } else {
//            v2 = "";
//        }
//    } else if (rsa.getString(2).equals("CATG")) {
//        if (rs7.next()) {
//            v2 = rs7.getString(1);
//        } else {
//            v2 = "";
//        }
//    } else {
//        v2 = rsa.getString(2);
//    }
//
//    if (rsa.getString(3).equals("COMP")) {
//        if (rs1.next()) {
//            v3 = rs1.getString(1);
//        } else {
//            v3 = "";
//        }
//    } else if (rsa.getString(3).equals("GRPP")) {
//        if (rs2.next()) {
//            v3 = rs2.getString(1);
//        } else {
//            v3 = "";
//        }
//    } else if (rsa.getString(3).equals("REGN")) {
//        if (rs3.next()) {
//            v3 = rs3.getString(1);
//        } else {
//            v3 = "";
//        }
//    } else if (rsa.getString(3).equals("BRCH")) {
//        if (rs4.next()) {
//            v3 = rs4.getString(1);
//        } else {
//            v3 = "";
//        }
//    } else if (rsa.getString(3).equals("DEPT")) {
//        if (rs5.next()) {
//            v3 = rs5.getString(1);
//        } else {
//            v3 = "";
//        }
//    } else if (rsa.getString(3).equals("SECT")) {
//        if (rs6.next()) {
//            v3 = rs6.getString(1);
//        } else {
//            v3 = "";
//        }
//    } else if (rsa.getString(3).equals("CATG")) {
//        if (rs7.next()) {
//            v3 = rs7.getString(1);
//        } else {
//            v3 = "";
//        }
//    } else {
//        v3 = rsa.getString(3);
//    }
//
//    if (rsa.getString(4).equals("COMP")) {
//        if (rs1.next()) {
//            v4 = rs1.getString(1);
//        } else {
//            v4 = "";
//        }
//    } else if (rsa.getString(4).equals("GRPP")) {
//        if (rs2.next()) {
//            v4 = rs2.getString(1);
//        } else {
//            v4 = "";
//        }
//    } else if (rsa.getString(4).equals("REGN")) {
//        if (rs3.next()) {
//            v4 = rs3.getString(1);
//        } else {
//            v4 = "";
//        }
//    } else if (rsa.getString(4).equals("BRCH")) {
//        if (rs4.next()) {
//            v4 = rs4.getString(1);
//        } else {
//            v4 = "";
//        }
//    } else if (rsa.getString(4).equals("DEPT")) {
//        if (rs5.next()) {
//            v4 = rs5.getString(1);
//        } else {
//            v4 = "";
//        }
//    } else if (rsa.getString(4).equals("SECT")) {
//        if (rs6.next()) {
//            v4 = rs6.getString(1);
//        } else {
//            v4 = "";
//        }
//    } else if (rsa.getString(4).equals("CATG")) {
//        if (rs7.next()) {
//            v4 = rs7.getString(1);
//        } else {
//            v4 = "";
//        }
//    } else {
//        v4 = rsa.getString(4);
//    }
//
//    if (rsa.getString(5).equals("COMP")) {
//        if (rs1.next()) {
//            v5 = rs1.getString(1);
//        } else {
//            v5 = "";
//        }
//    } else if (rsa.getString(5).equals("GRPP")) {
//        if (rs2.next()) {
//            v5 = rs2.getString(1);
//        } else {
//            v5 = "";
//        }
//    } else if (rsa.getString(5).equals("REGN")) {
//        if (rs3.next()) {
//            v5 = rs3.getString(1);
//        } else {
//            v5 = "";
//        }
//    } else if (rsa.getString(5).equals("BRCH")) {
//        if (rs4.next()) {
//            v5 = rs4.getString(1);
//        } else {
//            v5 = "";
//        }
//    } else if (rsa.getString(5).equals("DEPT")) {
//        if (rs5.next()) {
//            v5 = rs5.getString(1);
//        } else {
//            v5 = "";
//        }
//    } else if (rsa.getString(5).equals("SECT")) {
//        if (rs6.next()) {
//            v5 = rs6.getString(1);
//        } else {
//            v5 = "";
//        }
//    } else if (rsa.getString(5).equals("CATG")) {
//        if (rs7.next()) {
//            v5 = rs7.getString(1);
//        } else {
//            v5 = "";
//        }
//    } else {
//        v5 = rsa.getString(5);
//    }
//
//    if (rsa.getString(6).equals("COMP")) {
//        if (rs1.next()) {
//            v6 = rs1.getString(1);
//        } else {
//            v6 = "";
//        }
//    } else if (rsa.getString(6).equals("GRPP")) {
//        if (rs2.next()) {
//            v6 = rs2.getString(1);
//        } else {
//            v6 = "";
//        }
//    } else if (rsa.getString(6).equals("REGN")) {
//        if (rs3.next()) {
//            v6 = rs3.getString(1);
//        } else {
//            v6 = "";
//        }
//    } else if (rsa.getString(6).equals("BRCH")) {
//        if (rs4.next()) {
//            v6 = rs4.getString(1);
//        } else {
//            v6 = "";
//        }
//    } else if (rsa.getString(6).equals("DEPT")) {
//        if (rs5.next()) {
//            v6 = rs5.getString(1);
//        } else {
//            v6 = "";
//        }
//    } else if (rsa.getString(6).equals("SECT")) {
//        if (rs6.next()) {
//            v6 = rs6.getString(1);
//        } else {
//            v6 = "";
//        }
//    } else if (rsa.getString(6).equals("CATG")) {
//        if (rs7.next()) {
//            v6 = rs7.getString(1);
//        } else {
//            v6 = "";
//        }
//    } else {
//        v6 = rsa.getString(6);
//    }
//
//    if (rsa.getString(7).equals("COMP")) {
//        if (rs1.next()) {
//            v7 = rs1.getString(1);
//        } else {
//            v7 = "";
//        }
//    } else if (rsa.getString(7).equals("GRPP")) {
//        if (rs2.next()) {
//            v7 = rs2.getString(1);
//        } else {
//            v7 = "";
//        }
//    } else if (rsa.getString(7).equals("REGN")) {
//        if (rs3.next()) {
//            v7 = rs3.getString(1);
//        } else {
//            v7 = "";
//        }
//    } else if (rsa.getString(7).equals("BRCH")) {
//        if (rs4.next()) {
//            v7 = rs4.getString(1);
//        } else {
//            v7 = "";
//        }
//    } else if (rsa.getString(7).equals("DEPT")) {
//        if (rs5.next()) {
//            v7 = rs5.getString(1);
//        } else {
//            v7 = "";
//        }
//    } else if (rsa.getString(7).equals("SECT")) {
//        if (rs6.next()) {
//            v7 = rs6.getString(1);
//        } else {
//            v7 = "";
//        }
//    } else if (rsa.getString(7).equals("CATG")) {
//        if (rs7.next()) {
//            v7 = rs7.getString(1);
//        } else {
//            v7 = "";
//        }
//    } else {
//        v7 = rsa.getString(7);
//    }
//
//    dl = rsa.getString(8);
//
//    if (!v1.equals("")) {
//        sb.append(v1 + dl);
//    }
//    if (!v2.equals("")) {
//        sb.append(v2 + dl);
//    }
//    if (!v3.equals("")) {
//        sb.append(v3 + dl);
//    }
//    if (!v4.equals("")) {
//        sb.append(v4 + dl);
//    }
//    if (!v5.equals("")) {
//        sb.append(v5 + dl);
//    }
//    if (!v6.equals("")) {
//        sb.append(v6 + dl);
//    }
//    if (!v7.equals("")) {
//        sb.append(v7 + dl);
//    }
//
//    rs8.next();
//    curr = rs8.getInt(1);
//    ++curr;
//
//    getStatement().executeUpdate(
//            "update am_ad_cart_identity set cart_cr = " + curr +
//            " where cart_id = (select category_id from am_ad_category where category_id = " +
//            "'" + cat + "')");
//
//    identity = sb.toString() + (curr - 1);
//} else if (rsb.next()) {
//    rs9.next();
//    curr = rs9.getInt(1);
//    ++curr;
//
//    getStatement().executeUpdate(
//            "update am_ad_sequ_identity set sequ_cr = " + curr);
//
//    identity = String.valueOf(curr - 1);
//}
//freeResource();
//return identity;
////	return identity;
//}
    
    
    public String getIdentityOld(String bra, String dep, String sec, String cat,String identity) throws SQLException {
        StringBuilder sb = new StringBuilder();
        int curr = 0;
        MagmaDBConnection dbConn = new MagmaDBConnection();

        try (Connection con = dbConn.getConnection("legendPlus");
             Statement stmt = con.createStatement()) {

            // Load auto identity config
            ResultSet rsa = stmt.executeQuery("SELECT * FROM am_ad_auto_identity");
            if (!rsa.next()) {
                // fallback to sequential identity
                ResultSet rsb = stmt.executeQuery("SELECT * FROM am_ad_cart_identity");
                if (rsb.next()) {
                    ResultSet rsSeq = stmt.executeQuery("SELECT sequ_cr FROM am_ad_sequ_identity");
                    if (rsSeq.next()) {
                        curr = rsSeq.getInt(1) + 1;
                        stmt.executeUpdate("UPDATE am_ad_sequ_identity SET sequ_cr = " + curr);
                        return String.valueOf(curr - 1);
                    }
                }
                return "";
            }

            // Load all acronym values once
            String company = getSingleValue(stmt, "SELECT acronym FROM am_gb_company");
            String group = getSingleValue(stmt, "SELECT group_acronym FROM am_ad_group");
            String region = getSingleValue(stmt,
                    "SELECT region_acronym FROM am_ad_region WHERE region_id = (SELECT region_code FROM am_ad_branch WHERE branch_id = ?)", bra);
            String branch = getSingleValue(stmt,
                    "SELECT branch_acronym FROM am_ad_branch WHERE branch_id = ?", bra);
            String dept = getSingleValue(stmt,
                    "SELECT dept_acronym FROM am_ad_department WHERE dept_id = ?", dep);
            String section = getSingleValue(stmt,
                    "SELECT section_acronym FROM am_ad_section WHERE section_id = ?", sec);
            String category = getSingleValue(stmt,
                    "SELECT category_acronym FROM am_ad_category WHERE category_id = ?", cat);

            Map<String, String> acronymMap = Map.of(
                    "COMP", company,
                    "GRPP", group,
                    "REGN", region,
                    "BRCH", branch,
                    "DEPT", dept,
                    "SECT", section,
                    "CATG", category
            );

            String delimiter = rsa.getString(8);

            // Loop through 7 identity columns
            for (int i = 1; i <= 7; i++) {
                String key = rsa.getString(i);
                String val = acronymMap.getOrDefault(key, key != null ? key : "");
                if (!val.isEmpty()) {
                    sb.append(val).append(delimiter);
                }
            }

            // Update cart identity
            ResultSet rsCart = stmt.executeQuery(
                    "SELECT cart_cr FROM am_ad_cart_identity WHERE cart_id = '" + cat + "'");
            if (rsCart.next()) {
                curr = rsCart.getInt(1) + 1;
                stmt.executeUpdate(
                        "UPDATE am_ad_cart_identity SET cart_cr = " + curr +
                                " WHERE cart_id = '" + cat + "'"
                );
            }

            identity = sb.toString() + (curr - 1);
        }

        return identity;
    }

    
    
    public String getIdentity(String bra, String dep, String sec, String cat, String identity) throws Throwable {
        StringBuilder sb = new StringBuilder(100);
        String[] v = new String[7];
        String dl = "";
        int curr = 0;

        MagmaDBConnection dbConn = new MagmaDBConnection();

        try (Connection cnn = dbConn.getConnection("legendPlus")) {
            // Auto identity and cart identity
            try (PreparedStatement rsaPs = cnn.prepareStatement("select * from am_ad_auto_identity");
                 ResultSet rsa = rsaPs.executeQuery();
                 PreparedStatement rsbPs = cnn.prepareStatement("select * from am_ad_cart_identity");
                 ResultSet rsb = rsbPs.executeQuery();
                 PreparedStatement rs1Ps = cnn.prepareStatement("select acronym from am_gb_company");
                 ResultSet rs1 = rs1Ps.executeQuery();
                 PreparedStatement rs2Ps = cnn.prepareStatement("select group_acronym from am_ad_group");
                 ResultSet rs2 = rs2Ps.executeQuery();
                 PreparedStatement rs3Ps = cnn.prepareStatement(
                     "select region_acronym from am_ad_region where region_id = (select region_code from am_ad_branch where branch_id = ?)")) {

                rs3Ps.setString(1, bra);
                try (ResultSet rs3 = rs3Ps.executeQuery();
                     PreparedStatement rs4Ps = cnn.prepareStatement("select branch_acronym from am_ad_branch where branch_id = ?")) {
                    rs4Ps.setString(1, bra);
                    try (ResultSet rs4 = rs4Ps.executeQuery();
                         PreparedStatement rs5Ps = cnn.prepareStatement("select dept_acronym from am_ad_department where dept_id = ?")) {
                        rs5Ps.setString(1, dep);
                        try (ResultSet rs5 = rs5Ps.executeQuery();
                             PreparedStatement rs6Ps = cnn.prepareStatement("select section_acronym from am_ad_section where section_id = ?")) {
                            rs6Ps.setString(1, sec);
                            try (ResultSet rs6 = rs6Ps.executeQuery();
                                 PreparedStatement rs7Ps = cnn.prepareStatement("select category_acronym from am_ad_category where category_id = ?")) {
                                rs7Ps.setString(1, cat);
                                try (ResultSet rs7 = rs7Ps.executeQuery();
                                     PreparedStatement rs8Ps = cnn.prepareStatement("select cart_cr from am_ad_cart_identity where cart_id = ?")) {
                                    rs8Ps.setString(1, cat);
                                    try (ResultSet rs8 = rs8Ps.executeQuery();
                                         PreparedStatement rs9Ps = cnn.prepareStatement("select sequ_cr from am_ad_sequ_identity");
                                         ResultSet rs9 = rs9Ps.executeQuery()) {

                                        if (rsa.next()) {
                                            dl = rsa.getString(8);

                                            // Fill v1..v7 dynamically
                                            for (int i = 0; i < 7; i++) {
                                                String key = rsa.getString(i + 1);
                                                switch (key) {
                                                    case "COMP": v[i] = rs1.next() ? rs1.getString(1) : ""; break;
                                                    case "GRPP": v[i] = rs2.next() ? rs2.getString(1) : ""; break;
                                                    case "REGN": v[i] = rs3.next() ? rs3.getString(1) : ""; break;
                                                    case "BRCH": v[i] = rs4.next() ? rs4.getString(1) : ""; break;
                                                    case "DEPT": v[i] = rs5.next() ? rs5.getString(1) : ""; break;
                                                    case "SECT": v[i] = rs6.next() ? rs6.getString(1) : ""; break;
                                                    case "CATG": v[i] = rs7.next() ? rs7.getString(1) : ""; break;
                                                    default: v[i] = key;
                                                }
                                                if (!v[i].isEmpty()) sb.append(v[i]).append(dl);
                                            }

                                            // Update cart identity
                                            if (rs8.next()) {
                                                curr = rs8.getInt(1) + 1;
                                                try (PreparedStatement updatePs = cnn.prepareStatement(
                                                        "update am_ad_cart_identity set cart_cr = ? where cart_id = ?")) {
                                                    updatePs.setInt(1, curr);
                                                    updatePs.setString(2, cat);
                                                    updatePs.executeUpdate();
                                                }
                                            }
                                            identity = sb.toString() + (curr - 1);

                                        } else if (rsb.next()) {
                                            if (rs9.next()) {
                                                curr = rs9.getInt(1) + 1;
                                                try (PreparedStatement updatePs = cnn.prepareStatement(
                                                        "update am_ad_sequ_identity set sequ_cr = ?")) {
                                                    updatePs.setInt(1, curr);
                                                    updatePs.executeUpdate();
                                                }
                                            }
                                            identity = String.valueOf(curr - 1);
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return identity;
    }

    
 public String getCategoryIDOld(String catName) {
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
 
 
 
 
 public String getCategoryID(String catName) {
	    String value = "";
	    MagmaDBConnection dbConn = new MagmaDBConnection();

	    String query = "SELECT category_id FROM am_ad_category WHERE category_name = ?";
	    try (Connection cnn = dbConn.getConnection("legendPlus");
	         PreparedStatement ps = cnn.prepareStatement(query)) {

	        ps.setString(1, catName);
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) { // only expect one row
	                value = rs.getString(1);
	            }
	        }

	    } catch (Exception e) {
	        System.out.println("AutoIDSetup: INFO:Error getCategoryID() -> " + e.getMessage());
	    }

	    return value;
	}
 

public String getIdentityOldNew(String bra, String dep, String sec, String cat)
{
	System.out.println("====cat :"+cat);
	MagmaDBConnection dbConn = new MagmaDBConnection();
	String delim = approve.getCodeName("SELECT DELIMITER FROM am_ad_auto_identity ");
	String curr = approve.getCodeName("select cart_cr from am_ad_cart_identity where cart_id = "+cat+"");
	System.out.println("====curr :"+curr);
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
           "select '' AS acronym,'' AS group_acronym,'' AS region_acronym,'' AS branch_acronym,'' AS dept_acronym,'' AS section_acronym,'' AS category_acronym,cart_cr,0 AS sequ_cr  from am_ad_cart_identity where cart_id = '"+cat+"'"+
           " UNION "+
           "select '' AS acronym,'' AS group_acronym,'' AS region_acronym,'' AS branch_acronym,'' AS dept_acronym,'' AS section_acronym,'' AS category_acronym,0 AS cart_cr,sequ_cr  from am_ad_sequ_identity";
 //System.out.println("========query in getNewIdentity: "+query);  
    int currValue = Integer.parseInt(curr) + 1;
//    System.out.println("====New currValue :"+currValue);
String query2 = "update am_ad_cart_identity set cart_cr = " + currValue +
" where cart_id = (select category_id from am_ad_category where category_id = " +
"'" + cat + "')";
String query3 = "update am_ad_sequ_identity set sequ_cr = " + currValue;


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
       Connection cnn = dbConn.getConnection("legendPlus");
       PreparedStatement ps = cnn.prepareStatement(query);
       ResultSet rs = ps.executeQuery();
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
       ps = cnn.prepareStatement(query2);
       done = (ps.executeUpdate() != -1);
//       System.out.println("====New done Id  & query2: "+done+"    query2: "+query2);
       ps = cnn.prepareStatement(query3);
       done = (ps.executeUpdate() != -1);
 //      System.out.println("====New done Id  & query3: "+done+"    query2: "+query3);
 //      System.out.println("====>>>CURR: "+findObject("select cart_cr from am_ad_cart_identity where cart_id = "+cat+""));
   } catch (Exception ex) {
       ex.printStackTrace();
   }
//   System.out.println("====new_assetId in getNewIdentity: "+new_assetId);
   return new_assetId;

}



public String getIdentity2(String bra, String dep, String sec, String cat) {
    System.out.println("====cat :" + cat);
    MagmaDBConnection dbConn = new MagmaDBConnection();
    String delim = approve.getCodeName("SELECT DELIMITER FROM am_ad_auto_identity ");
    String curr = approve.getCodeName("select cart_cr from am_ad_cart_identity where cart_id = " + cat);
    System.out.println("====curr :" + curr);

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
           "select '' AS acronym,'' AS group_acronym,'' AS region_acronym,'' AS branch_acronym,'' AS dept_acronym,'' AS section_acronym,'' AS category_acronym,cart_cr,0 AS sequ_cr  from am_ad_cart_identity where cart_id = '"+cat+"'"+
           " UNION "+
           "select '' AS acronym,'' AS group_acronym,'' AS region_acronym,'' AS branch_acronym,'' AS dept_acronym,'' AS section_acronym,'' AS category_acronym,0 AS cart_cr,sequ_cr  from am_ad_sequ_identity";

    String new_assetId = "";
    try (Connection cnn = dbConn.getConnection("legendPlus");
         PreparedStatement ps = cnn.prepareStatement(query)) {
    	ps.setQueryTimeout(30);
      try(  ResultSet rs = ps.executeQuery();){

        String comp = "";
        String branch = "";
        String category = "";
        String catrCr = "";
        String sequCr = "";

        while (rs.next()) {
            if (!rs.getString(1).equals("")) comp = rs.getString(1);
            if (!rs.getString(4).equals("")) branch = rs.getString(4);
            if (!rs.getString(7).equals("")) category = rs.getString(7);
            if (!rs.getString(8).equals("0")) catrCr = rs.getString(8);
            if (!rs.getString(9).equals("0")) sequCr = rs.getString(9);
        }

        int currValue = Integer.parseInt(curr) + 1;
        new_assetId = comp + delim + branch + delim + category + delim + catrCr;

        // Update cart_cr
        String query2 = "update am_ad_cart_identity set cart_cr = " + currValue +
                        " where cart_id = '" + cat + "'";
        try (PreparedStatement ps2 = cnn.prepareStatement(query2)) {
        	ps2.setQueryTimeout(30);
            ps2.executeUpdate();
        }

        // Update sequ_cr
        String query3 = "update am_ad_sequ_identity set sequ_cr = " + currValue;
        try (PreparedStatement ps3 = cnn.prepareStatement(query3)) {
        	ps3.setQueryTimeout(30);
            ps3.executeUpdate();
        }
      }

    } catch (Exception ex) {
        ex.printStackTrace();
    }

    return new_assetId;
}

public String getIdentity(String bra, String dep, String sec, String cat) {
    String newAssetId = "";
    MagmaDBConnection dbConn = new MagmaDBConnection();

    try (Connection cnn = dbConn.getConnection("legendPlus")) {

        // Fetch delimiter and current cart_cr
        String delim = approve.getCodeName("SELECT DELIMITER FROM am_ad_auto_identity");
        String curr = approve.getCodeName("SELECT cart_cr FROM am_ad_cart_identity WHERE cart_id = ?", cat);

        // Fetch acronyms in a single query with JOINs (example, adjust column names)
        String query = "SELECT " +
                "(SELECT acronym FROM am_gb_company) AS comp, " +
                "(SELECT branch_acronym FROM am_ad_branch WHERE branch_id = ?) AS branch, " +
                "(SELECT category_acronym FROM am_ad_category WHERE category_id = ?) AS category";

        try (PreparedStatement ps = cnn.prepareStatement(query)) {
            ps.setString(1, bra);
            ps.setString(2, cat);
            ps.setQueryTimeout(30);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String comp = rs.getString("comp");
                    String branch = rs.getString("branch");
                    String category = rs.getString("category");

                    int currValue = Integer.parseInt(curr) + 1;
                    newAssetId = comp + delim + branch + delim + category + delim + currValue;

                    // Update cart_cr
                    try (PreparedStatement ps2 = cnn.prepareStatement(
                            "UPDATE am_ad_cart_identity SET cart_cr = ? WHERE cart_id = ?")) {
                        ps2.setInt(1, currValue);
                        ps2.setString(2, cat);
                        ps2.executeUpdate();
                    }

                    // Update sequ_cr
                    try (PreparedStatement ps3 = cnn.prepareStatement(
                            "UPDATE am_ad_sequ_identity SET sequ_cr = ?")) {
                        ps3.setInt(1, currValue);
                        ps3.executeUpdate();
                    }
                }
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return newAssetId;
}




public String getIdentity(Connection con,String bra, String dep, String sec, String cat) {
   // System.out.println("====cat :" + cat);
  
    String delim = approve.getCodeName(con,"SELECT DELIMITER FROM am_ad_auto_identity ");
    String curr = approve.getCodeName(con,"select cart_cr from am_ad_cart_identity where cart_id = " + cat);
   // System.out.println("====curr :" + curr);

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
           "select '' AS acronym,'' AS group_acronym,'' AS region_acronym,'' AS branch_acronym,'' AS dept_acronym,'' AS section_acronym,'' AS category_acronym,cart_cr,0 AS sequ_cr  from am_ad_cart_identity where cart_id = '"+cat+"'"+
           " UNION "+
           "select '' AS acronym,'' AS group_acronym,'' AS region_acronym,'' AS branch_acronym,'' AS dept_acronym,'' AS section_acronym,'' AS category_acronym,0 AS cart_cr,sequ_cr  from am_ad_sequ_identity";

    String new_assetId = "";
    try (
         PreparedStatement ps = con.prepareStatement(query)) {
    	//ps.setQueryTimeout(30);
      try(  ResultSet rs = ps.executeQuery();){

        String comp = "";
        String branch = "";
        String category = "";
        String catrCr = "";
        String sequCr = "";

        while (rs.next()) {
            if (!rs.getString(1).equals("")) comp = rs.getString(1);
            if (!rs.getString(4).equals("")) branch = rs.getString(4);
            if (!rs.getString(7).equals("")) category = rs.getString(7);
            if (!rs.getString(8).equals("0")) catrCr = rs.getString(8);
            if (!rs.getString(9).equals("0")) sequCr = rs.getString(9);
        }

        int currValue = Integer.parseInt(curr) + 1;
        new_assetId = comp + delim + branch + delim + category + delim + catrCr;

        // Update cart_cr
        String query2 = "update am_ad_cart_identity set cart_cr = " + currValue +
                        " where cart_id = '" + cat + "'";
        try (PreparedStatement ps2 = con.prepareStatement(query2)) {
        	
            ps2.executeUpdate();
        }

        // Update sequ_cr
        String query3 = "update am_ad_sequ_identity set sequ_cr = " + currValue;
        try (PreparedStatement ps3 = con.prepareStatement(query3)) {
        	
            ps3.executeUpdate();
        }
      }

    } catch (Exception ex) {
        ex.printStackTrace();
    }

    return new_assetId;
}

//public String getIdentity(Connection con, String branchId, String deptId, String sectionId, String categoryId) throws SQLException {
//    ApprovalRecords approve = new ApprovalRecords();
//
//    // 1️⃣ Get delimiter
//    String delim = approve.getCodeName(con, "SELECT DELIMITER FROM am_ad_auto_identity");
//
//    // 2️⃣ Get current cart_cr
//    int cartCr = Integer.parseInt(
//        approve.getCodeName(con, "SELECT cart_cr FROM am_ad_cart_identity WHERE cart_id = '"+categoryId+"'")
//    );
//
//    // 3️⃣ Fetch acronyms individually (single-row queries are faster)
//    String companyAcr = approve.getCodeName(con, "SELECT acronym FROM am_gb_company");
//    String branchAcr = approve.getCodeName(con, "SELECT branch_acronym FROM am_ad_branch WHERE branch_id = '"+branchId+"'");
//    String categoryAcr = approve.getCodeName(con, "SELECT category_acronym FROM am_ad_category WHERE category_id = '"+categoryId+"'");
//
//    // 4️⃣ Increment cart_cr
//    int newCartCr = cartCr + 1;
//
//    // 5️⃣ Build new asset ID
//    String newAssetId = String.join(delim, companyAcr, branchAcr, categoryAcr, String.valueOf(newCartCr));
//
//    // 6️⃣ Update cart_cr and sequ_cr in one shot
//    try (PreparedStatement psCart = con.prepareStatement(
//            "UPDATE am_ad_cart_identity SET cart_cr = ? WHERE cart_id = ?")) {
//        psCart.setInt(1, newCartCr);
//        psCart.setString(2, categoryId);
//        psCart.executeUpdate();
//    }
//
//    try (PreparedStatement psSeq = con.prepareStatement(
//            "UPDATE am_ad_sequ_identity SET sequ_cr = ?")) {
//        psSeq.setInt(1, newCartCr);
//        psSeq.executeUpdate();
//    }
//
//    return newAssetId;
//}



public List<String> generateAssetIds(
        Connection con,
        String branchId,
        String deptId,
        String sectionId,
        String categoryId,
        int count) throws SQLException {

    List<String> assetIds = new ArrayList<>();

    // 1️⃣ Get delimiter
    String delim = approve.getCodeName(con, "SELECT DELIMITER FROM am_ad_auto_identity");

    // 2️⃣ Fetch acronyms (company, branch, category) once
    String companyAcr = approve.getCodeName(con, "SELECT acronym FROM am_gb_company");
    String branchAcr = approve.getCodeName(con, "SELECT branch_acronym FROM am_ad_branch WHERE branch_id = ?");
    String categoryAcr = approve.getCodeName(con, "SELECT category_acronym FROM am_ad_category WHERE category_id = ?");

    try (PreparedStatement ps = con.prepareStatement(
            "SELECT branch_acronym FROM am_ad_branch WHERE branch_id = ?")) {
        ps.setString(1, branchId);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) branchAcr = rs.getString(1);
        }
    }

    try (PreparedStatement ps = con.prepareStatement(
            "SELECT category_acronym FROM am_ad_category WHERE category_id = ?")) {
        ps.setString(1, categoryId);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) categoryAcr = rs.getString(1);
        }
    }

    // 3️⃣ Get current cart_cr
    int currValue = 0;
    try (PreparedStatement ps = con.prepareStatement(
            "SELECT cart_cr FROM am_ad_cart_identity WHERE cart_id = ?")) {
        ps.setString(1, categoryId);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) currValue = rs.getInt(1);
        }
    }

    // 4️⃣ Generate all asset IDs in memory
    for (int i = 1; i <= count; i++) {
        currValue++;
        String assetId = String.join(delim,
                companyAcr,
                branchAcr,
                categoryAcr,
                String.valueOf(currValue));
        assetIds.add(assetId);
    }

    // 5️⃣ Update cart_cr and sequ_cr once
    try (PreparedStatement ps = con.prepareStatement(
            "UPDATE am_ad_cart_identity SET cart_cr = ? WHERE cart_id = ?")) {
        ps.setInt(1, currValue);
        ps.setString(2, categoryId);
        ps.executeUpdate();
    }

    try (PreparedStatement ps = con.prepareStatement(
            "UPDATE am_ad_sequ_identity SET sequ_cr = ?")) {
        ps.setInt(1, currValue);
        ps.executeUpdate();
    }

    return assetIds;
}

public String getBidIdentityOld(String bra, String dep, String sec, String cat)
{
	MagmaDBConnection dbConn = new MagmaDBConnection();
	String delim = approve.getCodeName("SELECT DELIMITER FROM am_ad_auto_identity ");
	String curr = approve.getCodeName("select bidupload_cart_cr from am_ad_cart_identity where cart_id = "+cat+"");
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
           "select '' AS acronym,'' AS group_acronym,'' AS region_acronym,'' AS branch_acronym,'' AS dept_acronym,'' AS section_acronym,'' AS category_acronym,bidupload_cart_cr ,0 AS sequ_cr  from am_ad_cart_identity where cart_id = '"+cat+"'"+
           " UNION "+
           "select '' AS acronym,'' AS group_acronym,'' AS region_acronym,'' AS branch_acronym,'' AS dept_acronym,'' AS section_acronym,'' AS category_acronym,0 AS cart_cr,sequ_cr  from am_ad_sequ_identity";
 //System.out.println("========query in getNewIdentity: "+query);  
    int currValue = Integer.parseInt(curr) + 1;
//    System.out.println("====New currValue :"+currValue);
String query2 = "update am_ad_cart_identity set bidupload_cart_cr = " + currValue +
" where cart_id = (select category_id from am_ad_category where category_id = " +
"'" + cat + "')";
String query3 = "update am_ad_sequ_identity set sequ_cr = " + currValue;


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
       Connection cnn = dbConn.getConnection("legendPlus");
       PreparedStatement ps = cnn.prepareStatement(query);
       ResultSet rs = ps.executeQuery();
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
       new_assetId = comp+delim+branch+delim+category+delim+"BID"+catrCr;
//       System.out.println("====New Asset Id: "+new_assetId);
       ps = cnn.prepareStatement(query2);
       done = (ps.executeUpdate() != -1);
//       System.out.println("====New done Id  & query2: "+done+"    query2: "+query2);
       ps = cnn.prepareStatement(query3);
       done = (ps.executeUpdate() != -1);
 //      System.out.println("====New done Id  & query3: "+done+"    query2: "+query3);
 //      System.out.println("====>>>CURR: "+findObject("select bidupload_cart_cr  from am_ad_cart_identity where cart_id = "+cat+""));
   } catch (Exception ex) {
       ex.printStackTrace();
   }
//   System.out.println("====new_assetId in getBidIdentity: "+new_assetId);
   return new_assetId;

}


public String getBidIdentity(String bra, String dep, String sec, String cat) {
    MagmaDBConnection dbConn = new MagmaDBConnection();
    String delim = approve.getCodeName("SELECT DELIMITER FROM am_ad_auto_identity ");
    String curr = approve.getCodeName("select bidupload_cart_cr from am_ad_cart_identity where cart_id = " + cat);

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
           "select '' AS acronym,'' AS group_acronym,'' AS region_acronym,'' AS branch_acronym,'' AS dept_acronym,'' AS section_acronym,'' AS category_acronym,bidupload_cart_cr ,0 AS sequ_cr  from am_ad_cart_identity where cart_id = '"+cat+"'"+
           " UNION "+
           "select '' AS acronym,'' AS group_acronym,'' AS region_acronym,'' AS branch_acronym,'' AS dept_acronym,'' AS section_acronym,'' AS category_acronym,0 AS cart_cr,sequ_cr  from am_ad_sequ_identity";

    int currValue = Integer.parseInt(curr) + 1;
    String query2 = "update am_ad_cart_identity set bidupload_cart_cr = " + currValue +
                    " where cart_id = '" + cat + "'";
    String query3 = "update am_ad_sequ_identity set sequ_cr = " + currValue;

    String comp = "", group = "", region = "", branch = "", dept = "", section = "", category = "", catrCr = "", sequCr = "";
    String new_assetId = "";
    int count = 0;

    try (Connection cnn = dbConn.getConnection("legendPlus");
         PreparedStatement ps = cnn.prepareStatement(query);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            count++;
            if (!rs.getString(1).equals("")) comp = rs.getString(1);
            if (!rs.getString(2).equals("")) group = rs.getString(2);
            if (!rs.getString(3).equals("")) region = rs.getString(3);
            if (!rs.getString(4).equals("")) branch = rs.getString(4);
            if (!rs.getString(5).equals("")) dept = rs.getString(5);
            if (!rs.getString(6).equals("")) section = rs.getString(6);
            if (!rs.getString(7).equals("")) category = rs.getString(7);
            if (!rs.getString(8).equals("0")) catrCr = rs.getString(8);
            if (!rs.getString(9).equals("0")) sequCr = rs.getString(9);
        }

        new_assetId = comp + delim + branch + delim + category + delim + "BID" + catrCr;

        // Update bidupload_cart_cr
        try (PreparedStatement ps2 = cnn.prepareStatement(query2)) {
            ps2.executeUpdate();
        }

        // Update sequ_cr
        try (PreparedStatement ps3 = cnn.prepareStatement(query3)) {
            ps3.executeUpdate();
        }

    } catch (Exception ex) {
        ex.printStackTrace();
    }

    return new_assetId;
}



public String getNewIdentityOld(String bra, String dep, String sec, String cat,String delim)
{
	MagmaDBConnection dbConn = new MagmaDBConnection();
	String curr = approve.getCodeName("select cart_cr from am_ad_cart_identity where cart_id = "+cat+"");
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
           "select '' AS acronym,'' AS group_acronym,'' AS region_acronym,'' AS branch_acronym,'' AS dept_acronym,'' AS section_acronym,'' AS category_acronym,cart_cr,0 AS sequ_cr  from am_ad_cart_identity where cart_id = '"+cat+"'"+
           " UNION "+
           "select '' AS acronym,'' AS group_acronym,'' AS region_acronym,'' AS branch_acronym,'' AS dept_acronym,'' AS section_acronym,'' AS category_acronym,0 AS cart_cr,sequ_cr  from am_ad_sequ_identity";
 //System.out.println("========query in getNewIdentity: "+query);  
    int currValue = Integer.parseInt(curr) + 1;
//    System.out.println("====New currValue :"+currValue);
String query2 = "update am_ad_cart_identity set cart_cr = " + currValue +
" where cart_id = (select category_id from am_ad_category where category_id = " +
"'" + cat + "')";
String query3 = "update am_ad_sequ_identity set sequ_cr = " + currValue;


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
       Connection cnn = dbConn.getConnection("legendPlus");
       PreparedStatement ps = cnn.prepareStatement(query);
       ResultSet rs = ps.executeQuery();
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
       ps = cnn.prepareStatement(query2);
       done = (ps.executeUpdate() != -1);
//       System.out.println("====New done Id  & query2: "+done+"    query2: "+query2);
       ps = cnn.prepareStatement(query3);
       done = (ps.executeUpdate() != -1);
 //      System.out.println("====New done Id  & query3: "+done+"    query2: "+query3);
 //      System.out.println("====>>>CURR: "+findObject("select cart_cr from am_ad_cart_identity where cart_id = "+cat+""));
   } catch (Exception ex) {
       ex.printStackTrace();
   }
//   System.out.println("====new_assetId in getNewIdentity: "+new_assetId);
   return new_assetId;

}


public String getNewIdentity(String bra, String dep, String sec, String cat, String delim) {
    MagmaDBConnection dbConn = new MagmaDBConnection();
    String curr = approve.getCodeName("select cart_cr from am_ad_cart_identity where cart_id = " + cat);

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
           "select '' AS acronym,'' AS group_acronym,'' AS region_acronym,'' AS branch_acronym,'' AS dept_acronym,'' AS section_acronym,'' AS category_acronym,cart_cr,0 AS sequ_cr  from am_ad_cart_identity where cart_id = '"+cat+"'"+
           " UNION "+
           "select '' AS acronym,'' AS group_acronym,'' AS region_acronym,'' AS branch_acronym,'' AS dept_acronym,'' AS section_acronym,'' AS category_acronym,0 AS cart_cr,sequ_cr  from am_ad_sequ_identity";

    int currValue = Integer.parseInt(curr) + 1;
    String query2 = "update am_ad_cart_identity set cart_cr = " + currValue +
                    " where cart_id = '" + cat + "'";
    String query3 = "update am_ad_sequ_identity set sequ_cr = " + currValue;

    String comp = "", group = "", region = "", branch = "", dept = "", section = "", category = "", catrCr = "", sequCr = "";
    String new_assetId = "";

    try (Connection cnn = dbConn.getConnection("legendPlus");
         PreparedStatement ps = cnn.prepareStatement(query);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            if (!rs.getString(1).equals("")) comp = rs.getString(1);
            if (!rs.getString(2).equals("")) group = rs.getString(2);
            if (!rs.getString(3).equals("")) region = rs.getString(3);
            if (!rs.getString(4).equals("")) branch = rs.getString(4);
            if (!rs.getString(5).equals("")) dept = rs.getString(5);
            if (!rs.getString(6).equals("")) section = rs.getString(6);
            if (!rs.getString(7).equals("")) category = rs.getString(7);
            if (!rs.getString(8).equals("0")) catrCr = rs.getString(8);
            if (!rs.getString(9).equals("0")) sequCr = rs.getString(9);
        }

        new_assetId = comp + delim + branch + delim + category + delim + catrCr;

        // Update cart_cr
        try (PreparedStatement ps2 = cnn.prepareStatement(query2)) {
            ps2.executeUpdate();
        }

        // Update sequ_cr
        try (PreparedStatement ps3 = cnn.prepareStatement(query3)) {
            ps3.executeUpdate();
        }

    } catch (Exception ex) {
        ex.printStackTrace();
    }

    return new_assetId;
}

public String getIdentityforStockOld(String bra, String dep, String sec, String cat)
{
	MagmaDBConnection dbConn = new MagmaDBConnection();
	 String delim = findObject("SELECT DELIMITER FROM am_ad_auto_identity ");
	String curr = findObject("select cart_cr from am_ad_cart_identity where cart_id = "+cat+"");
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
           "select '' AS acronym,'' AS group_acronym,'' AS region_acronym,'' AS branch_acronym,'' AS dept_acronym,'' AS section_acronym,'' AS category_acronym,cart_cr,0 AS sequ_cr  from am_ad_cart_identity where cart_id = '"+cat+"'"+
           " UNION "+
           "select '' AS acronym,'' AS group_acronym,'' AS region_acronym,'' AS branch_acronym,'' AS dept_acronym,'' AS section_acronym,'' AS category_acronym,0 AS cart_cr,sequ_cr  from am_ad_sequ_identity";
// System.out.println("========query in getNewIdentity: "+query);  
// System.out.println("====Old curr :"+curr+"   cat: "+cat);
    int currValue = Integer.parseInt(curr) + 1;
//    System.out.println("====New currValue :"+currValue);
String query2 = "update am_ad_cart_identity set cart_cr = " + currValue +
" where cart_id = (select category_id from am_ad_category where category_id = " +
"'" + cat + "')";
String query3 = "update am_ad_sequ_identity set sequ_cr = " + currValue;
//System.out.println("====query2 in getNewIdentity :"+query2);

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
       Connection cnn = dbConn.getConnection("legendPlus");
       PreparedStatement ps = cnn.prepareStatement(query);
       ResultSet rs = ps.executeQuery();
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
       ps = cnn.prepareStatement(query2);
       done = (ps.executeUpdate() != -1);
//       System.out.println("====New done Id  & query2: "+done+"    query2: "+query2);
       ps = cnn.prepareStatement(query3);
       done = (ps.executeUpdate() != -1);
 //      System.out.println("====New done Id  & query3: "+done+"    query2: "+query3);
 //      System.out.println("====>>>CURR: "+findObject("select cart_cr from am_ad_cart_identity where cart_id = "+cat+""));
   } catch (Exception ex) {
       ex.printStackTrace();
   }
//   System.out.println("====new_assetId in getNewIdentity: "+new_assetId);
   return new_assetId;

}



public String getIdentityforStock(String bra, String dep, String sec, String cat) {
    MagmaDBConnection dbConn = new MagmaDBConnection();
    String delim = findObject("SELECT DELIMITER FROM am_ad_auto_identity ");
    String curr = findObject("select cart_cr from am_ad_cart_identity where cart_id = " + cat);

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
           "select '' AS acronym,'' AS group_acronym,'' AS region_acronym,'' AS branch_acronym,'' AS dept_acronym,'' AS section_acronym,'' AS category_acronym,cart_cr,0 AS sequ_cr  from am_ad_cart_identity where cart_id = '"+cat+"'"+
           " UNION "+
           "select '' AS acronym,'' AS group_acronym,'' AS region_acronym,'' AS branch_acronym,'' AS dept_acronym,'' AS section_acronym,'' AS category_acronym,0 AS cart_cr,sequ_cr  from am_ad_sequ_identity";

    int currValue = Integer.parseInt(curr) + 1;
    String query2 = "update am_ad_cart_identity set cart_cr = " + currValue +
                    " where cart_id = '" + cat + "'";
    String query3 = "update am_ad_sequ_identity set sequ_cr = " + currValue;

    String comp = "", group = "", region = "", branch = "", dept = "", section = "", category = "", catrCr = "", sequCr = "";
    String new_assetId = "";

    try (Connection cnn = dbConn.getConnection("legendPlus");
         PreparedStatement ps = cnn.prepareStatement(query);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            if (!rs.getString(1).equals("")) comp = rs.getString(1);
            if (!rs.getString(2).equals("")) group = rs.getString(2);
            if (!rs.getString(3).equals("")) region = rs.getString(3);
            if (!rs.getString(4).equals("")) branch = rs.getString(4);
            if (!rs.getString(5).equals("")) dept = rs.getString(5);
            if (!rs.getString(6).equals("")) section = rs.getString(6);
            if (!rs.getString(7).equals("")) category = rs.getString(7);
            if (!rs.getString(8).equals("0")) catrCr = rs.getString(8);
            if (!rs.getString(9).equals("0")) sequCr = rs.getString(9);
        }

        new_assetId = comp + delim + branch + delim + category + delim + catrCr;

        // Update cart_cr safely
        try (PreparedStatement ps2 = cnn.prepareStatement(query2)) {
            ps2.executeUpdate();
        }

        // Update sequ_cr safely
        try (PreparedStatement ps3 = cnn.prepareStatement(query3)) {
            ps3.executeUpdate();
        }

    } catch (Exception ex) {
        ex.printStackTrace();
    }

    return new_assetId;
}

public String findObjectOld(String query)
{
	MagmaDBConnection dbConn = new MagmaDBConnection();
//	System.out.println("====findObject query=====  "+query);

    String found = null;

    //String finder = "UNKNOWN";
    String finder = "";

   // double sequence = 0.00d;
    try {

        Connection cnn = dbConn.getConnection("legendPlus");
        PreparedStatement ps = cnn.prepareStatement(query);
        ResultSet result = ps.executeQuery();

        while (result.next()) {
            finder = result.getString(1);
        }

    } catch (Exception ee2) {
        System.out.println("WARN:ERROR OBTAINING OBJ --> " + ee2);
        ee2.printStackTrace();
    }

    return finder;
}


public String findObject(String query) {
    MagmaDBConnection dbConn = new MagmaDBConnection();
    String finder = "";

    try (Connection cnn = dbConn.getConnection("legendPlus");
         PreparedStatement ps = cnn.prepareStatement(query);
         ResultSet result = ps.executeQuery()) {

        while (result.next()) {
            finder = result.getString(1);
        }

    } catch (Exception ee2) {
        System.out.println("WARN:ERROR OBTAINING OBJ --> " + ee2);
        ee2.printStackTrace();
    }

    return finder;
}

}
