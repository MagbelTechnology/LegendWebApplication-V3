package legend;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.magbel.legend.bus.ApprovalRecords;

import legend.objects.PersistenceServiceDAO;
import magma.AssetRecordsBean;

public class ConnectionClass {

    private static final String LEGEND_DS = "java:/legendPlus";
    private static final String FINACLE_DS = "java:/FinacleDataHouse";
    private static final String VASCO_DS = "java:/vascoDS";

    /* =========================================================
       CONNECTION PROVIDERS (THREAD SAFE)
       ========================================================= */

    public Connection getConnection() throws SQLException {
        return lookupDataSource(LEGEND_DS).getConnection();
    }

    public Connection getOracleConnection() throws SQLException {
        return lookupDataSource(FINACLE_DS).getConnection();
    }

    public Connection getConnectionOracleVasco() throws SQLException {
        return lookupDataSource(VASCO_DS).getConnection();
    }

    private DataSource lookupDataSource(String jndiName) throws SQLException {
        try {
            InitialContext ctx = new InitialContext();
            return (DataSource) ctx.lookup(jndiName);
        } catch (NamingException e) {
            throw new SQLException("Unable to lookup datasource: " + jndiName, e);
        }
    }

    /* =========================================================
       SAFE RESOURCE CLOSERS
       ========================================================= */

    public void closeQuietly(Connection con) {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (Exception ignored) {}
    }

    public void closeQuietly(Connection con, PreparedStatement ps) {
        closeQuietly(ps);
        closeQuietly(con);
    }

    public void closeQuietly(Connection con, PreparedStatement ps, ResultSet rs) {
        closeQuietly(rs);
        closeQuietly(ps);
        closeQuietly(con);
    }

    public void closeQuietly(Statement st) {
        try {
            if (st != null) {
                st.close();
            }
        } catch (Exception ignored) {}
    }

    public void closeQuietly(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (Exception ignored) {}
    }

    /* =========================================================
       UTILITY METHODS (UNCHANGED BEHAVIOR)
       ========================================================= */

    public String getIdentity() {
        return UUID.randomUUID().toString();
    }

    public String getEncrypted(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(input.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(md.digest());
    }

    public String encodeHtmlText(String text) {
        if (text == null) return null;

        StringBuilder encoded = new StringBuilder();
        for (char c : text.toCharArray()) {
            switch (c) {
                case '<': encoded.append("&lt;"); break;
                case '>': encoded.append("&gt;"); break;
                case '&': encoded.append("&amp;"); break;
                case '"': encoded.append("&quot;"); break;
                case ' ': encoded.append("&nbsp;"); break;
                default: encoded.append(c);
            }
        }
        return encoded.toString();
    }

    public String filterFieldValue(String value) {
        if (value == null) return null;
        return value.replace("'", "''");
    }

    public String addMonthToDate(String date, int month) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date parsedDate = sdf.parse(date);

        Calendar cal = Calendar.getInstance();
        cal.setTime(parsedDate);
        cal.add(Calendar.MONTH, month);

        return sdf.format(cal.getTime());
    }

    public String formatDate(Date date) {
        if (date == null) return null;
        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }

    public String getMonthPartOfDate(String date) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date parsedDate = sdf.parse(date);

        Calendar cal = Calendar.getInstance();
        cal.setTime(parsedDate);

        return new SimpleDateFormat("MMMM").format(cal.getTime());
    }

    public String getYearPartOfDate(String date) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date parsedDate = sdf.parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(parsedDate);

        return String.valueOf(cal.get(Calendar.YEAR));
    }

    public String getCurrentDate() {
        return new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    }

    /* =========================================================
       SAFE EXAMPLE: VAT RATE (Pattern to follow)
       ========================================================= */

    public String getVatRate() {
        String query = "SELECT VAT_RATE FROM AM_GB_COMPANY";
        String vat = "0";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                vat = rs.getString(1) != null ? rs.getString(1) : "0";
            }

        } catch (Exception e) {
            System.out.println("WARN: Error fetching VAT rate -> " + e.getMessage());
        }

        return vat;
    }
    
    public String[][] getBranchesForComboExcluding(String s) throws Exception {

        String query =
            "SELECT BRANCH_ID, BRANCH_CODE, BRANCH_NAME " +
            "FROM AM_AD_BRANCH " +
            "WHERE BRANCH_NAME <> ? " +
            "ORDER BY BRANCH_NAME";

        List<String[]> list = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, s);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    String[] row = new String[3];
                    row[0] = rs.getString("BRANCH_ID");
                    row[1] = rs.getString("BRANCH_CODE");
                    row[2] = rs.getString("BRANCH_NAME");

                    list.add(row);
                }
            }
        }

        if (list.isEmpty()) {
            return null;
        }

        return list.toArray(new String[list.size()][3]);
    }

    
    public String[][] getDepartmentsForComboExcluding(String s) throws Exception {

        String query =
            "SELECT DEPT_ID, DEPT_CODE, DEPT_NAME " +
            "FROM AM_AD_DEPARTMENT " +
            "WHERE LOWER(DEPT_NAME) <> LOWER(?)";

        List<String[]> list = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, s);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    String[] row = new String[3];
                    row[0] = rs.getString("DEPT_ID");
                    row[1] = rs.getString("DEPT_CODE");
                    row[2] = rs.getString("DEPT_NAME");
                    list.add(row);
                }
            }
        }

        if (list.isEmpty()) {
            return null;
        }

        return list.toArray(new String[list.size()][3]);
    }

    
    public String[][] getCategoriesForCombo() {

        String query = "SELECT CATEGORY_ID, CATEGORY_CODE, CATEGORY_NAME " +
                       "FROM AM_AD_CATEGORY " +
                       "WHERE CATEGORY_STATUS = 'ACTIVE' " +
                       "ORDER BY CATEGORY_NAME";

        List<String[]> list = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new String[]{
                        rs.getString("CATEGORY_ID"),
                        rs.getString("CATEGORY_CODE"),
                        rs.getString("CATEGORY_NAME")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list.toArray(new String[0][0]);
    }
    
    public String[][] getCategoriesForComboExcluding(String excludeName) {

        String query = "SELECT CATEGORY_ID, CATEGORY_CODE, CATEGORY_NAME " +
                       "FROM AM_AD_CATEGORY " +
                       "WHERE CATEGORY_NAME <> ? " +
                       "ORDER BY CATEGORY_NAME";

        List<String[]> list = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, excludeName);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new String[]{
                            rs.getString("CATEGORY_ID"),
                            rs.getString("CATEGORY_CODE"),
                            rs.getString("CATEGORY_NAME")
                    });
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list.toArray(new String[0][0]);
    }

    
    public String[][] getMaintenanceVendorsForCombo() {

        String query =
                "SELECT VENDOR_ID, VENDOR_CODE, VENDOR_NAME " +
                "FROM AM_AD_VENDOR WHERE VENDOR_STATUS = 'ACTIVE' " +
                "UNION " +
                "SELECT MAINTAINANCE_ID, REGISTRATION_NO, TECH_NAME " +
                "FROM AM_AD_MAINTENANCEREPS WHERE MAINTENACE_STATUS = 'ACTIVE'";

        List<String[]> list = new ArrayList<>();

        // Add default
        list.add(new String[]{"0", "0", "None"});

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new String[]{
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3)
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list.toArray(new String[0][0]);
    }

    
    public String[][] getSuppliersForCombo() {

        String query =
                "SELECT VENDOR_ID, VENDOR_CODE, VENDOR_NAME " +
                "FROM AM_AD_VENDOR WHERE VENDOR_STATUS = 'A'";

        List<String[]> list = new ArrayList<>();
        list.add(new String[]{"0", "0", "None"});

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new String[]{
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3)
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list.toArray(new String[0][0]);
    }

    
    public String[][] getDisposalReasonsForCombo() {

        String query =
                "SELECT REASON_ID, REASON_CODE, DESCRIPTION " +
                "FROM AM_AD_DISPOSALREASONS " +
                "WHERE REASON_STATUS = 'ACTIVE' " +
                "ORDER BY DESCRIPTION";

        List<String[]> list = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new String[]{
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3)
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list.toArray(new String[0][0]);
    }

    
    public String[][] getStatesForCombo() {

        List<String[]> list = new ArrayList<>();
        list.add(new String[]{"0", "0", "None"});

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("EXEC am_msp_select_states 0, 'A'");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new String[]{
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3)
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list.toArray(new String[0][0]);
    }

    
    public String[][] getDriversForCombo() {

        String query =
                "SELECT DRIVER_ID, DRIVER_CODE, " +
                "driver_lastname, driver_firstname, driver_othername " +
                "FROM am_ad_driver " +
                "WHERE driver_status = 'A' " +
                "ORDER BY driver_lastname";

        List<String[]> list = new ArrayList<>();
        list.add(new String[]{"0", "0", "None"});

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String fullName = rs.getString(3) + ", " +
                                  rs.getString(4) + " " +
                                  rs.getString(5);

                list.add(new String[]{
                        rs.getString(1),
                        rs.getString(2),
                        fullName
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list.toArray(new String[0][0]);
    }

    
    public String[][] getLocationsForCombo() {

        String query = "SELECT LOCATION_ID, LOCATION_CODE, LOCATION " +
                       "FROM AM_GB_LOCATION " +
                       "WHERE STATUS = 'ACTIVE' " +
                       "ORDER BY LOCATION";

        List<String[]> list = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new String[]{
                        rs.getString("LOCATION_ID"),
                        rs.getString("LOCATION_CODE"),
                        rs.getString("LOCATION")
                });
            }

        } catch (Exception e) {
            e.printStackTrace(); 
        }

        return list.toArray(new String[0][0]);
    }

    public String[][] getSectionsForCombo() {
        String query = "SELECT * " +
                       "FROM AM_AD_SECTION " +
                       "WHERE SECTION_STATUS = 'ACTIVE' " +
                       "ORDER BY SECTION_NAME ASC";
        
        String countQuery = "SELECT COUNT(*) FROM AM_AD_SECTION " +
                            "WHERE SECTION_STATUS = 'ACTIVE'";
        
        try (Connection conn = getConnection();
             PreparedStatement countPs = conn.prepareStatement(countQuery);
             ResultSet countRs = countPs.executeQuery()) {
            
            if (!countRs.next()) {
                return new String[0][0]; // no sections
            }

            int count = countRs.getInt(1);
            if (count == 0) {
                return new String[0][0];
            }

            String[][] sections = new String[count][3];

            try (PreparedStatement ps = conn.prepareStatement(query);
                 ResultSet rs = ps.executeQuery()) {

                int i = 0;
                while (rs.next()) {
                    sections[i][0] = rs.getString(1);
                    sections[i][1] = rs.getString(2);
                    sections[i][2] = rs.getString(3);
                    i++;
                }
            }

            return sections;

        } catch (Exception e) {
            System.out.println("WARN: Error fetching sections -> " + e.getMessage());
            e.printStackTrace();
            return new String[0][0]; // safe fallback
        }
    }
    
    public String[][] getSectionsForComboExcluding(String excludeName) {
        String query = "SELECT SECTION_ID, SECTION_CODE, SECTION_NAME " +
                       "FROM AM_AD_SECTION " +
                       "WHERE LOWER(SECTION_NAME) != LOWER(?) " +
                       "ORDER BY SECTION_NAME ASC";

        String countQuery = "SELECT COUNT(*) FROM AM_AD_SECTION " +
                            "WHERE LOWER(SECTION_NAME) != LOWER(?)";

        try (Connection conn = getConnection();
             PreparedStatement countPs = conn.prepareStatement(countQuery)) {

            countPs.setString(1, excludeName);
            try (ResultSet countRs = countPs.executeQuery()) {
                if (!countRs.next() || countRs.getInt(1) == 0) {
                    return new String[0][0]; // no sections
                }

                int count = countRs.getInt(1);
                String[][] sections = new String[count][3];

                try (PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setString(1, excludeName);
                    try (ResultSet rs = ps.executeQuery()) {
                        int i = 0;
                        while (rs.next()) {
                            sections[i][0] = rs.getString(1);
                            sections[i][1] = rs.getString(2);
                            sections[i][2] = rs.getString(3);
                            i++;
                        }
                    }
                }
                return sections;
            }

        } catch (Exception e) {
            System.out.println("WARN: Error fetching sections excluding '" + excludeName + "' -> " + e.getMessage());
            e.printStackTrace();
            return new String[0][0]; // safe fallback
        }
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
    
    

}