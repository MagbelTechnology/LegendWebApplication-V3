package asset.report;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Utilities {

    private static final String LEGEND_DS = "java:/legendPlus";

    /* =========================================================
       CONNECTION PROVIDER (THREAD SAFE)
       ========================================================= */
    public Connection getConnection() throws SQLException {
        try {
            InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup(LEGEND_DS);
            return ds.getConnection();
        } catch (Exception e) {
            throw new SQLException("Unable to obtain connection", e);
        }
    }

    /* =========================================================
       EXECUTE STORED PROCEDURE
       ========================================================= */
    public void executeProcedure(String callableProc) {

        String[] finDates = companyDetails();
        String startDate = toCAL(finDates[2]);
        String endDate = toCAL(finDates[3]);

        try (Connection con = getConnection();
             CallableStatement cstmt = con.prepareCall(callableProc)) {

            cstmt.setString(1, startDate);
            cstmt.setString(2, endDate);
            cstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* =========================================================
       DATE UTILITIES
       ========================================================= */
    public String toSQL(String dt) {
        String[] result = dt.substring(0, 10).split("/");
        return result[2] + "/" + result[1] + "/" + result[0];
    }

    public String toCAL(String dt) {
        String[] result = dt.substring(0, 10).split("-");
        return result[2] + "/" + result[1] + "/" + result[0];
    }

    public String getCurrentDate() {
        return new SimpleDateFormat("dd/MM/yyyy").format(new Date());
    }

    /* =========================================================
       COMPANY DETAILS
       ========================================================= */
    public String[] companyDetails() {

        String[] result = new String[4];

        try (Connection con = getConnection();
             CallableStatement cstmt = con.prepareCall("{CALL company_Details()}");
             ResultSet rs = cstmt.executeQuery()) {

            if (rs.next()) {
                result[0] = rs.getString(1);
                result[1] = rs.getString(2);
                result[2] = rs.getString(3);
                result[3] = rs.getString(4);
            }

        } catch (Exception e) {
            System.out.println("WARNING::ERROR GETCOMPANY AM_GB_COMPANY " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    /* =========================================================
       REPORT NAME RESOLVER (CLEANED)
       ========================================================= */
    public String getReportName(String reportName) {

        if (reportName == null || !reportName.contains("_")) {
            return "Unknown Report";
        }

        String[] parts = reportName.split("_");
        if (parts.length < 3) {
            return "Unknown Report";
        }

        String assetType = parts[0].equalsIgnoreCase("fixed")
                ? "Fixed Asset"
                : "Fleet Asset";

        String key = parts[2].toLowerCase();

        Map<String, String> reportMap = new HashMap<>();

        // Fixed Asset Reports
        reportMap.put("mgt", "Management Report");
        reportMap.put("addition", "Addition Report");
        reportMap.put("list", "List Report");
        reportMap.put("detail", "Detail Report");
        reportMap.put("register", "Register Report");
        reportMap.put("mgt1", "Management Summary Report");
        reportMap.put("revalue", "Revaluation Report");
        reportMap.put("fullydep", "Fully Depreciated Asset Report");
        reportMap.put("ledger", "Ledger Extraction Report");
        reportMap.put("acquisition", "Acquisition Budget Report");
        reportMap.put("acquivariance", "Acquisition Budget Variance Report");
        reportMap.put("maintenance", "Maintenance Budget Report");
        reportMap.put("maintvariance", "Maintenance Budget Variance Report");
        reportMap.put("noticereminder", "Asset Notice Reminder Report");
        reportMap.put("audittrail", "Asset Audit Trail Report");

        // Fleet Reports
        reportMap.put("maintsummary", "Maintenance Summary Report");
        reportMap.put("fuelsummary", "Fuel Summary Report");
        reportMap.put("fuel", "Fuel Report");
        reportMap.put("accidentsummary", "Accident Summary Report");
        reportMap.put("accident", "Accident Report");
        reportMap.put("licencesummary", "Licence Summary Report");
        reportMap.put("licence", "Licence Report");
        reportMap.put("insurancesummary", "Insurance Summary Report");
        reportMap.put("insurance", "Insurance Report");
        reportMap.put("location", "Location Report");
        reportMap.put("allocation", "Allocation Report");
        reportMap.put("stolen", "Stolen Report");

        String description = reportMap.getOrDefault(key, key + " Report");

        return assetType + " " + description;
    }

    /* =========================================================
       HTML RESOURCE BUILDER
       ========================================================= */
    public String getResources(String selected, String query) {

        StringBuilder html = new StringBuilder();

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            if (query.toLowerCase().contains("region")) {
                ps.setString(1, selected);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String id = rs.getString(1);
                    String name = rs.getString(2);

                    html.append("<option value='")
                            .append(id)
                            .append("' ")
                            .append(id.equals(selected) ? "selected" : "")
                            .append(">")
                            .append(name)
                            .append("</option>");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return html.toString();
    }

    
    
    
    /* =========================================================
       COLUMN NAME FETCHER (SAFE)
       ========================================================= */
    public String getColumnName(String baseQuery, String value) {

        if ("***".equals(value)) {
            return "***";
        }

        String name = "";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(baseQuery + value);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                name = rs.getString(1);
                if (name == null) {
                    name = "";
                }
            }

        } catch (Exception e) {
            System.out.println("Error In getColumnName " + e.getMessage());
        }

        return name;
    }

}