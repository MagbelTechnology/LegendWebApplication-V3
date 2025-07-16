package com.magbel.admin.dao;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.UUID;
import java.text.SimpleDateFormat;
import java.util.Calendar;
//import java.sql.Date;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import com.magbel.admin.dao.PersistenceServiceDAO;

import sun.misc.BASE64Encoder;


public class ConnectionClass {

    private Connection conn = null;
    private String jndiName = "legendPlus";
    private String jndiNameOracle = "FinacleDataHouse";
    private int record_start = 0;
    private int record_count = 2;
    private Context ic = null;

    public ConnectionClass() throws Exception {
         //if (conn!=null) freeResource();

       ic = new InitialContext();
      
    }

    public void refreshConnection() throws Exception {
        if (conn!=null) freeResource();
        DataSource ds = (DataSource) ic.lookup("java:comp/env/jdbc/" +
                                               this.jndiName);
        conn = ds.getConnection();
    }

    public void refreshConnectionOracle()throws Exception{
    if (conn!=null) freeResource();
        //DataSource ds = (DataSource) ic.lookup("java:comp/env/jdbc/" +
                                               //this.jndiNameOracle);
      DataSource ds = (DataSource) ic.lookup("java:/"+this.jndiNameOracle);
        conn = ds.getConnection();
    }

    public PreparedStatement getPreparedStatementOracle(String sql)throws Exception{
         getConnectionOracle();
        PreparedStatement ps = conn.prepareStatement(sql);

    return ps;
    }
    /**
     * getConnection
     *
     * @return Connection
     * @throws Exception
     */
    public Connection getConnection() throws Exception {

            refreshConnection();

        return conn;
    }

     public Connection getConnectionOracle() throws Exception {

            refreshConnectionOracle();

        return conn;
    }

    /**
     * getConnection
     *
     * @return Statement
     * @throws Exception
     */
    public Statement getStatement() throws Exception {
        if (conn!= null) {
            return conn.createStatement();
        } else if(conn== null) {
            return getConnection().createStatement();
        }
        else{
            return null;
        }
    }
 public Statement getStatementOracle() throws Exception {
        if (conn!= null) {
            return conn.createStatement();
        } else if(conn== null) {
            return getConnectionOracle().createStatement();
        }
        else{
            return null;
        }
    }
    /**
     * freeResource
     *
     * @param con Connection
     * @param ps PreparedStatement
     * @param rs ResultSet
     * @todo Implement this magma.net.dao.eConnection method
     */
    public void freeResource() {
        try {

            if (this.conn != null) {
                this.conn.close();
            }
            this.conn = null;
        } catch (Exception e) {
            System.out.println("WARN:Error closing Connection ->" +
                               e.getMessage());
        }

    }

    /**
     * freeResource
     *
     * @param con Connection
     * @param ps PreparedStatement
     * @todo Implement this magma.net.dao.eConnection method
     */
    public void freeResource(Connection con, PreparedStatement ps) {
        try {
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (Exception e) {
            System.out.println("WARNING:Error closing Connection ->" +
                               e.getMessage());
        }

    }

    /**
     * freeResource
     *
     * @param con Connection
     * @param ps PreparedStatement
     * @param rs ResultSet
     * @todo Implement this magma.net.dao.eConnection method
     */
    public void freeResource(Connection con, PreparedStatement ps,
                             ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (Exception e) {
            System.out.println("WARNING:Error closing Connection ->" +
                               e.getMessage());
        }

    }

    /**
     * getIdentity
     *
     * @return String
     */
    public String getIdentity() {
        return UUID.randomUUID().toString();
    }

    /**
     * getProperties
     *
     * @param sele String
     * @param vals String[][]
     * @return String
     */
    public String getProperties(String sele, String[][] vals) {
        String html = new String();
        if (sele == null) {
            sele = " ";
        }

        if (vals != null) {
            for (int i = 0; i < vals.length; i++) {
                html = html + "<option value='" + vals[i][0] + "' " +
                       (vals[i][0].equalsIgnoreCase(sele) ? " selected " : "") +
                       ">" + vals[i][2] + "</option> ";
            }

        }

        return html;
    }

    /**
     * getProperties
     *
     * @param sele String
     * @param vals String[][]
     * @return String
     */
    public String getUserprops(String sele, String[][] vals) {
        String html = new String();
        if (sele == null) {
            sele = " ";
        }
        for (int i = 0; i < vals.length; i++) {
            html = html + "<option value='" + vals[i][0] + "' " +
                   (vals[i][0].equalsIgnoreCase(sele) ? " selected " : "") +
                   ">" + vals[i][1] + "</option> ";
        }

        return html;
    }

    /**
     * getProperties
     *
     * @param sele String
     * @param iden String[]
     * @param vals String[]
     * @return String
     */
    public String getProperties(String sele, String[] iden, String[] vals) {
        String html = new String();
        if (sele == null) {
            sele = " ";
        }
        for (int i = 0; i < iden.length; i++) {
            html = html + "<option value='" + iden[i] + "' " +
                   (iden[i].equalsIgnoreCase(sele) ? " selected " : "") + ">" +
                   vals[i] + "</option> ";
        }

        return html;
    }

    /**
     * getEncrypted
     *
     * @param input String
     * @throws Throwable
     * @return String
     */
    public String getEncrypted(String input) throws Throwable {

        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(input.toString().getBytes("UTF-16"));

        return new BASE64Encoder().encode(md.digest());
    }


    public String encodeHtmlText(String text) {
        if (text == null) {
            return null;
        }
        int length = text.length();
        StringBuffer encodedText = new StringBuffer(2 * length);
        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);
            if (c == '<') {
                encodedText.append("&lt;");
            } else if (c == '>') {
                encodedText.append("&gt;");
            } else if (c == '&') {
                encodedText.append("&amp;");
            } else if (c == '"') {
                encodedText.append("&quot;");
            } else if (c == ' ') {
                encodedText.append("&nbsp;");
            } else {
                encodedText.append(c);
            }
        }
        return encodedText.toString();
    };

    public String filterFieldValue(String value) {
        if (value == null) {
            return null;
        }
        int length = value.length();
        StringBuffer filteredValue = new StringBuffer((int) (length * 1.1));
        for (int i = 0; i < length; i++) {
            char c = value.charAt(i);
            if (c == '\'') {
                filteredValue.append("''");
            } else {
                filteredValue.append(c);
            }
        }
        return filteredValue.toString();
    };

    public String addMonthToDate(String date, int month) throws Throwable {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
                "dd/MM/yyyy");
        java.util.Calendar calendarDate = null;
        String added = null;
        if (date == null) {
            added = null;
        } else {
            Date dDate = sdf.parse(date);
            calendarDate = new java.util.GregorianCalendar();
            calendarDate.setTime(dDate);
            calendarDate.add(java.util.Calendar.MONTH, month);
            dDate = calendarDate.getTime();
            added = sdf.format(dDate);
        }
        return added;
    }

    public void setRecord_start(String record_start) {
        this.record_start = Integer.parseInt(record_start);
    }

    public void setRecord_count(String record_count) {
        this.record_count = Integer.parseInt(record_count);
    }

    public String getRecord_start() {
        return String.valueOf(record_start);
    }

    public String getRecord_count() {
        return String.valueOf(record_count);
    }

    public String[][] getBranchesForCombo() throws Exception {

        String query = "SELECT * FROM AM_AD_BRANCH	" +
                       "WHERE BRANCH_STATUS = 'ACTIVE'  " +
                       "ORDER BY BRANCH_NAME ASC";
        String counterQuery = "SELECT count(*) FROM AM_AD_BRANCH	" +
                              "WHERE BRANCH_STATUS = 'ACTIVE'  ";

        ResultSet rs = getStatement().executeQuery(query);
        ResultSet rsc = getStatement().executeQuery(counterQuery);
        rsc.next();
        int count = rsc.getInt(1);
        if (count > 0) {

            String[][] a = new String[count][12];
            int i = 0;
            while (rs.next()) {
                for (int j = 0; j < 12; j++) {
                    a[i][j] = rs.getString(j + 1);
                }
                i++;
            }
            freeResource();
            return a;
        }
        freeResource();
        return null;
    }

    public String[][] getBranchesForComboExcluding(String s) throws Exception {

        String query = "SELECT BRANCH_ID, BRANCH_CODE, BRANCH_NAME " +
                       "FROM AM_AD_BRANCH " +
                       "WHERE LOWER(BRANCH_NAME) != LOWER('" + s + "')";

        String countQuery = "SELECT count(*) FROM AM_AD_BRANCH " +
                            "WHERE LOWER(BRANCH_NAME) != LOWER('" + s + "')";

        ResultSet rs = getStatement().executeQuery(query);
        ResultSet rsc = getStatement().executeQuery(countQuery);
        rsc.next();
        int count = rsc.getInt(1);
        if (count > 0) {

            String[][] a = new String[count][12];
            int i = 0;
            while (rs.next()) {
                for (int j = 0; j < 12; j++) {
                    a[i][j] = rs.getString(j + 1);
                }
                i++;
            }
            freeResource();
            return a;
        }
        freeResource();
        return null;

    }

    public String[][] getDepartmentsForCombo() throws Exception {

        String query = "SELECT * FROM AM_AD_DEPARTMENT  " +
                       "WHERE DEPT_STATUS = 'ACTIVE' ORDER BY DEPT_NAME ASC";

        String countQuery = "SELECT COUNT(*) FROM AM_AD_DEPARTMENT  " +
                            "WHERE DEPT_STATUS = 'ACTIVE' ";

        ResultSet rs = getStatement().executeQuery(query);
        ResultSet rsc = getStatement().executeQuery(countQuery);
        rsc.next();
        int count = rsc.getInt(1);
        if (count > 0) {

            String[][] a = new String[count][8];
            int i = 0;
            while (rs.next()) {
                for (int j = 0; j < 8; j++) {
                    a[i][j] = rs.getString(j + 1);
                }
                i++;
            }
            return a;
        }
        return null;
    }

    public String[][] getDepartmentsForComboExcluding(String s) throws
            Exception {

        String query = "SELECT DEPT_ID, DEPT_CODE, DEPT_NAME " +
                       "FROM AM_AD_DEPARTMENT " +
                       "WHERE LOWER(DEPT_NAME) != LOWER('" + s + "')";

        String countQuery = "SELECT count(*) FROM AM_AD_DEPARTMENT " +
                            "WHERE LOWER(DEPT_NAME) != LOWER('" + s + "')";

        ResultSet rs = getStatement().executeQuery(query);
        ResultSet rsc = getStatement().executeQuery(countQuery);
        rsc.next();
        int count = rsc.getInt(1);
        if (count > 0) {

            String[][] a = new String[count][8];
            int i = 0;
            while (rs.next()) {
                for (int j = 0; j < 8; j++) {
                    a[i][j] = rs.getString(j + 1);
                }
                i++;
            }
            freeResource();
            return a;
        }
        freeResource();
        return null;

    }

    public String[][] getCategoriesForCombo() throws Exception {

        String query = "SELECT * FROM AM_AD_CATEGORY  " +
                       "WHERE CATEGORY_STATUS = 'ACTIVE'  " +
                       "ORDER BY CATEGORY_NAME ASC";

        String countQuery = "SELECT count(*) FROM AM_AD_CATEGORY  " +
                            "WHERE CATEGORY_STATUS = 'ACTIVE'  ";

        ResultSet rs = getStatement().executeQuery(query);
        ResultSet rsc = getStatement().executeQuery(countQuery);
        rsc.next();
        int count = rsc.getInt(1);
        if (count > 0) {

            String[][] a = new String[count][16];
            int i = 0;
            while (rs.next()) {
                for (int j = 0; j < 16; j++) {
                    a[i][j] = rs.getString(j + 1);
                }
                i++;
            }
            freeResource();
            return a;
        }
        freeResource();
        return null;
    }

    public String[][] getCategoriesForComboExcluding(String s) throws Exception {

        String query = "SELECT CATEGORY_ID, CATEGORY_CODE, CATEGORY_NAME " +
                       "FROM AM_AD_CATEGORY " +
                       "WHERE LOWER(CATEGORY_NAME) != LOWER('" + s + "')";
        String countQuery = "SELECT count(*) FROM AM_AD_CATEGORY " +
                            "WHERE LOWER(CATEGORY_NAME) != LOWER('" + s + "')";

        ResultSet rs = getStatement().executeQuery(query);
        ResultSet rsc = getStatement().executeQuery(countQuery);
        rsc.next();
        int count = rsc.getInt(1);
        if (count > 0) {

            String[][] a = new String[count][16];
            int i = 0;
            while (rs.next()) {
                for (int j = 0; j < 16; j++) {
                    a[i][j] = rs.getString(j + 1);
                }
                i++;
            }
            freeResource();
            return a;
        }
        freeResource();
        return null;

    }

    public String[][] getMaintenanceVendorsForCombo() throws Exception {

        String query =
                "SELECT	VENDOR_ID AS MAINT_ID,VENDOR_CODE AS MAINT_CODE," +
                "VENDOR_NAME AS MAINT_NAME    " +
                "FROM AM_AD_VENDOR   " +
                "WHERE VENDOR_STATUS = 'ACTIVE'   " +
                "UNION   " +
                "SELECT  " +
                "MAINTAINANCE_ID AS MAINT_ID,REGISTRATION_NO AS MAINT_CODE," +
                "TECH_NAME AS MAINT_NAME    " +
                "FROM AM_AD_MAINTENANCEREPS  " +
                "WHERE MAINTENACE_STATUS = 'ACTIVE'";

        String countQuery = "SELECT COUNT(*) FROM AM_AD_MAINTENANCEREPS " +
                            "WHERE MAINTENACE_STATUS = 'ACTIVE'";

        ResultSet rs = getStatement().executeQuery(query);
        ResultSet rsc = getStatement().executeQuery(countQuery);
        rsc.next();
        int count = rsc.getInt(1);
        if (count > 0) {

            String[][] a = new String[count + 1][3];
            int i = 0;
            a[i][0] = "0";
            a[i][1] = "0";
            a[i++][2] = "None";

            while (rs.next()) {
                for (int j = 0; j < 3; j++) {
                    a[i][j] = rs.getString(j + 1);
                }
                i++;
            }
            freeResource();
            return a;
        }
        freeResource();
        return null;
    }

    public String[][] getSuppliersForCombo() throws Exception {

        String query =
                "SELECT VENDOR_ID AS MAINT_ID,VENDOR_CODE AS MAINT_CODE," +
                "VENDOR_NAME AS MAINT_NAME    " +
                "FROM AM_AD_VENDOR WHERE VENDOR_STATUS = 'A'";

        String countQuery =
                "select count(*) FROM AM_AD_VENDOR WHERE VENDOR_STATUS = 'A'";

        ResultSet rs = getStatement().executeQuery(query);
        ResultSet rsc = getStatement().executeQuery(countQuery);
        rsc.next();
        int count = rsc.getInt(1);
        if (count > 0) {

            String[][] a = new String[count + 1][3];
            a[0][0] = "0";
            a[0][1] = "0";
            a[0][2] = "None";

            int i = 1;
            while (rs.next()) {
                for (int j = 0; j < 3; j++) {
                    a[i][j] = rs.getString(j + 1);
                }
                i++;
            }
            freeResource();
            return a;
        }
        freeResource();
        return null;
    }

    public String[][] getDisposalReasonsForCombo() throws Exception {

        String query = "SELECT * FROM AM_AD_DISPOSALREASONS   " +
                       "WHERE REASON_STATUS = 'ACTIVE'   " +
                       "ORDER BY DESCRIPTION ASC    ";
        String countQuery = "SELECT count(*) FROM AM_AD_DISPOSALREASONS   " +
                            "WHERE REASON_STATUS = 'ACTIVE'";

        ResultSet rs = getStatement().executeQuery(query);
        ResultSet rsc = getStatement().executeQuery(countQuery);
        rsc.next();
        int count = rsc.getInt(1);
        if (count > 0) {

            String[][] a = new String[count][3];
            int i = 0;
            while (rs.next()) {
                a[i][0] = rs.getString(1);
                a[i][1] = rs.getString(2);
                a[i][2] = rs.getString(3);
                i++;
            }
            freeResource();
            return a;
        }
        freeResource();
        return null;
    }

    public String[][] getAssetMakeForCombo() throws Exception {
        String query = "SELECT * FROM AM_GB_ASSETMAKE	";
        String countQuery = "SELECT COUNT(*) FROM AM_GB_ASSETMAKE	";

        ResultSet rs = getStatement().executeQuery(query);
        ResultSet rsc = getStatement().executeQuery(countQuery);
        rsc.next();
        int count = rsc.getInt(1);
        if (count > 0) {

            String[][] a = new String[count][3];
            int i = 0;
            while (rs.next()) {
                a[i][0] = rs.getString(1);
                a[i][1] = rs.getString(2);
                a[i][2] = rs.getString(3);
                i++;
            }
            return a;
        }
        return null;
    }

    public String[][] getStatesForCombo() throws Exception {
        ResultSet rs = getStatement().executeQuery(
                "am_msp_select_states 0, 'A'");
        ResultSet rsc = getStatement().executeQuery(
                "am_msp_count_states 0, 'A'");
        rsc.next();
        int count = rsc.getInt(1);
        if (count > 0) {

            String[][] a = new String[count + 1][3];
            a[0][0] = "0";
            a[0][1] = "0";
            a[0][2] = "None";

            int i = 1;
            while (rs.next()) {
                a[i][0] = rs.getString(1);
                a[i][1] = rs.getString(2);
                a[i][2] = rs.getString(3);
                i++;
            }
            freeResource();
            return a;
        }
        freeResource();
        return null;
    }

    public String[][] getDriversForCombo() throws Exception {
        ResultSet rs = getStatement().executeQuery("SELECT DRIVER_ID, DRIVER_CODE, driver_lastname, driver_firstname, driver_othername from am_ad_driver where driver_status = 'A' order by driver_lastname");
        ResultSet rsc = getStatement().executeQuery(
                "select count(*) from am_ad_driver where driver_status = 'ACTIVE'");
        rsc.next();
        int count = rsc.getInt(1);
        if (count > 0) {

            String[][] a = new String[count + 1][3];
            a[0][0] = "0";
            a[0][1] = "0";
            a[0][2] = "None";
            int i = 1;
            while (rs.next()) {
                a[i][0] = rs.getString(1);
                a[i][1] = rs.getString(2);
                a[i][2] = rs.getString(3) + ", " + rs.getString(4) + " " +
                          rs.getString(5);
                i++;
            }
            freeResource();
            return a;
        }
        return null;
    }

    public String[][] getLocationsForCombo() throws Exception {

        String query = "SELECT LOCATION_ID, LOCATION_CODE, LOCATION " +
                       "FROM AM_GB_LOCATION " +
                       "WHERE STATUS = 'ACTIVE' ORDER BY LOCATION";

        String countQuery =
                "SELECT COUNT(*) FROM AM_GB_LOCATION WHERE STATUS = 'ACTIVE'";

        ResultSet rs = getStatement().executeQuery(query);
        ResultSet rsc = getStatement().executeQuery(countQuery);
        rsc.next();
        int count = rsc.getInt(1);
        if (count > 0) {

            String[][] a = new String[count][3];
            int i = 0;
            while (rs.next()) {
                a[i][0] = rs.getString(1);
                a[i][1] = rs.getString(2);
                a[i][2] = rs.getString(3);
                i++;
            }
            freeResource();
            return a;
        }
        return null;
    }


    public String[][] getSectionsForCombo() throws Exception {

        String query = "SELECT * FROM AM_AD_SECTION  " +
                       "WHERE SECTION_STATUS = 'ACTIVE'  " +
                       "ORDER BY SECTION_NAME ASC";

        String countQuery = "SELECT count(*) FROM AM_AD_SECTION  " +
                            "WHERE SECTION_STATUS = 'ACTIVE'  ";

        ResultSet rs = getStatement().executeQuery(query);
        ResultSet rsc = getStatement().executeQuery(countQuery);
        rsc.next();
        int count = rsc.getInt(1);
        if (count > 0) {

            String[][] a = new String[count][3];
            int i = 0;
            while (rs.next()) {
                a[i][0] = rs.getString(1);
                a[i][1] = rs.getString(2);
                a[i][2] = rs.getString(3);
                i++;
            }
            freeResource();
            return a;

        }
        return null;
    }

    public String[][] getSectionsForComboExcluding(String s) throws Exception {

        String query = "SELECT SECTION_ID, SECTION_CODE, SECTION_NAME " +
                       "FROM AM_AD_SECTION " +
                       "WHERE LOWER(SECTION_NAME) != LOWER('" + s + "')";
        String countQuery = "SELECT COUNT(*) FROM AM_AD_SECTION " +
                            "WHERE LOWER(SECTION_NAME) != LOWER('" + s + "')";

        ResultSet rs = getStatement().executeQuery(query);
        ResultSet rsc = getStatement().executeQuery(countQuery);
        rsc.next();
        int count = rsc.getInt(1);
        if (count > 0) {

            String[][] a = new String[count][3];
            int i = 0;
            while (rs.next()) {
                a[i][0] = rs.getString(1);
                a[i][1] = rs.getString(2);
                a[i][2] = rs.getString(3);
                i++;
            }
            freeResource();
            return a;
        }
        return null;

    }

    public String[][] getStatusForCombo() throws Exception {
        String[][] a = new String[4][3];

        a[0][0] = "A";
        a[0][1] = "";
        a[0][2] = "Active";

        a[1][0] = "C";
        a[1][1] = "";
        a[1][2] = "Closed";

        a[2][0] = "Z";
        a[2][1] = "";
        a[2][2] = "Distribution";

        a[3][0] = "S";
        a[3][1] = "";
        a[3][2] = "Stolen";

        return a;
    }

    public String getVatRate() throws Exception {
        ResultSet rs = getStatement().executeQuery(
                "SELECT VAT_RATE FROM AM_GB_COMPANY");
        String vat="0";
        if (rs.next()) {
           vat=rs.getString(1);
        }
        freeResource();
        return vat;
    }



     public String getWhRate() throws Exception {
        ResultSet rs = getStatement().executeQuery(
                "SELECT WHT_RATE FROM AM_GB_COMPANY");
        String vat="0";
        if (rs.next()) {
           vat= rs.getString(1);
	        }
        freeResource();
        return vat;
    }


   public String getFedWhRate() throws Exception {
        ResultSet rs = getStatement().executeQuery(
                "SELECT FED_WHT_RATE FROM AM_GB_COMPANY");
        String vat="0";
        if (rs.next()) {
           vat= rs.getString(1);
	        }
        freeResource();
        return vat;
    }



   public String getProcessingStatus() throws Exception {
        ResultSet rs = getStatement().executeQuery(
                "SELECT PROCESSING_STATUS FROM AM_GB_COMPANY");
        String ps="0";
        if (rs.next()) {
           ps=rs.getString("processing_status");
	        }
        freeResource();
        return ps;
    }

public boolean populateFinacleTemp(int ps_status)throws Exception{

    boolean suc = false;
String current_date= getCurrentDate();
String narration = null;
Date processing_date =null;
PersistenceServiceDAO psdao = new PersistenceServiceDAO();

ResultSet re = getStatement().executeQuery("select processing_date from am_gb_company");

if(re.next()){
processing_date = re.getDate(1);//.getString(1);
 }

freeResource();

 //System.out.println("the value of processing date is " + formatDate(processing_date) );
narration = "DEPRECIATON FOR THE MONTH OF " + getMonthPartOfDate(formatDate(processing_date)).toUpperCase() + " " + getYearPartOfDate(formatDate(processing_date));

if(ps_status == 1){
 

try{
    getStatement().executeUpdate("Insert into finacle_temp(dr_acct,cr_acct,amount,value_date,narration,narration2) select dr_acct,cr_acct,amount,value_date,narration,narration2 from finacle_ext");

getStatement().executeUpdate("TRUNCATE table finacle_ext insert into finacle_ext(type,dr_acct,cr_acct,amount,value_date) " +
        "select c.category_name,d.country_prefix + d.dr_prefix + b.branch_code + c.Dep_ledger,d.country_prefix + " +
        "d.cr_prefix + b.branch_code + c.Accum_Dep_ledger,sum(a.monthly_dep),d.processing_date from " +
        "am_ad_branch b,am_ad_category c,am_asset a,am_gb_company d where a.category_code = c.category_code " +
        "And a.branch_code = b.branch_code and a.asset_status ='active' and a.cost_price > d.cost_threshold and " +
        "a.dep_rate > 0 and c.Accum_Dep_ledger <> '" +" "+"' group by c.category_name,b.branch_code," +
        "c.Accum_Dep_ledger,c.Dep_ledger,d.processing_date,d.country_prefix,d.dr_prefix,d.cr_prefix order " +
        "by b.branch_code asc");


String query ="update finacle_ext set narration=?,system_date=?,narration2=?";
    Connection con = getConnection();
PreparedStatement ps = con.prepareStatement(query);
ps.setString(1, narration);
ps.setDate(2, psdao.dateConvert(current_date));
ps.setString(3, narration);
int i =ps.executeUpdate();
//if(i != -1){System.out.println("THE UPDATE WAS SUCCESSFUL");}
//getStatement().executeUpdate("update finacle_ext set narration='" + narration + "',system_date='"+current_date+"',narration2='" + narration + "'");
}catch(Exception e){e.printStackTrace();
}finally{freeResource();}


suc = true;
freeResource();
}else{suc = false;}//if

return suc;
}


public String formatDate(Date date){
String output = null;
    try {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    output = sdf.format(date);
       }catch(Exception e){e.printStackTrace();}
return output;
}


public String getMonthPartOfDate(String date) {
String output = null;
    try {SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
Date myDate = sdf.parse(date);
java.util.Calendar cal = java.util.Calendar.getInstance();
cal.setTime(myDate);
int month = cal.get(Calendar.MONTH);

String[] monthName = new String[]{"January","February","March","April","May","June","July","August","September","October","November","December"};
 output = monthName[month];
}catch(Exception e){e.printStackTrace();}
return output;

}

public String getYearPartOfDate(String date) {
String output = null;
    try {SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
Date myDate = sdf.parse(date);
java.util.Calendar cal = java.util.Calendar.getInstance();
cal.setTime(myDate);
output = Integer.toString(cal.get(Calendar.YEAR));


}catch(Exception e){e.printStackTrace();}
return output;

}


public String getCurrentDate(){
   String output = null;
PersistenceServiceDAO psdao = null;
 try {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
Date date = new Date();
   output = sdf.format(date);//psdao.dateConvert(date.toString());
    // System.out.println("the pre output is " + sdf.toPattern());
}catch(Exception e){e.printStackTrace();}

return output;


    }

    public String[][] getYesNoForCombo() {
        String[][] a = new String[2][3];

        a[0][0] = "Y";
        a[0][1] = "";
        a[0][2] = "Yes";

        a[1][0] = "N";
        a[1][1] = "";
        a[1][2] = "No";

        return a;
    }


//Ganiyu's Code
public String[][] getFederalStateNoneForCombo(){
String[][] a= new String[3][3];
a[0][0]="N";
a[0][1]="";
a[0][2]="None";

a[1][0]="S";
a[1][1]="";
a[1][2]="State";

a[2][0]="F";
a[2][1]="";
a[2][2]="Federal";

return a;


}


public String[][] getApproveRejectPendingForCombo(){
String[][] a= new String[3][3];
a[0][0]="P";
a[0][1]="";
a[0][2]="Pending";

a[1][0]="R";
a[1][1]="";
a[1][2]="Reject";

a[2][0]="A";
a[2][1]="";
a[2][2]="Approve";

return a;


}

    public String[][] getBranchesForCombo(String code,String branchRestrict) throws Exception {
         String query = "";
         if(branchRestrict.equalsIgnoreCase("N")){
          query = "SELECT * FROM AM_AD_BRANCH	" +
                       "WHERE BRANCH_STATUS = 'ACTIVE'  " +
                       "ORDER BY BRANCH_NAME ASC";
         }else{
          query = "SELECT * FROM AM_AD_BRANCH	" +
                       "WHERE BRANCH_STATUS = 'ACTIVE' AND BRANCH_ID = '"+code+"' " +
                       "ORDER BY BRANCH_NAME ASC";
         }
        String counterQuery = "SELECT count(*) FROM AM_AD_BRANCH	" +
                              "WHERE BRANCH_STATUS = 'ACTIVE'  ";

        ResultSet rs = getStatement().executeQuery(query);
        ResultSet rsc = getStatement().executeQuery(counterQuery);
        rsc.next();
        int count = rsc.getInt(1);
        if (count > 0) {

            String[][] a = new String[count][12];
            int i = 0;
            while (rs.next()) {
                for (int j = 0; j < 12; j++) {
                    a[i][j] = rs.getString(j + 1);
                }
                i++;
            }
            freeResource();
            return a;
        }
        freeResource();
        return null;
    }
}
