package com.magbel.util;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import magma.net.vao.Category;
import com.magbel.legend.bus.Report;

public class HtmlUtility {

    private static final int QUERY_TIMEOUT_SECONDS = 30;

    private final DatetimeFormat df;

    public HtmlUtility() {
        df = new DatetimeFormat();
    }

    /* =========================================================
       SAFE CONNECTION HELPER
       ========================================================= */
    private Connection getLegendConnection() throws SQLException {
        return new DataConnect("legendPlus").getConnection();
    }

    private Connection getOtherConnection() throws SQLException {
        return new DataConnect("otherDataSource").getOtherConnection();
    }

    /* =========================================================
       SAFE findObject()
       ========================================================= */

    public String findObject(String query) {

        String result = "UNKNOWN";

        try (Connection con = getLegendConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setQueryTimeout(QUERY_TIMEOUT_SECONDS);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = rs.getString(1);
                }
            }

        } catch (Exception e) {
            System.err.println("ERROR findObject(): " + e.getMessage());
        }

        return result == null ? "" : result;
    }

    public String findObject(String query, String filter) {

        String result = "UNKNOWN";

        try (Connection con = getLegendConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setQueryTimeout(QUERY_TIMEOUT_SECONDS);
            ps.setString(1, filter);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = rs.getString(1);
                }
            }

        } catch (Exception e) {
            System.err.println("ERROR findObject(filter): " + e.getMessage());
        }

        return result == null ? "" : result;
    }

    /* =========================================================
       SAFE getCodeName()
       ========================================================= */

    public String getCodeName(String query) {

        String result = "";

        try (Connection con = getLegendConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setQueryTimeout(QUERY_TIMEOUT_SECONDS);

            if (query.trim().toUpperCase().startsWith("UPDATE")) {
                ps.executeUpdate();
                return "";
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = rs.getString(1);
                }
            }

        } catch (Exception e) {
            System.err.println("ERROR getCodeName(): " + e.getMessage());
        }

        return result == null ? "" : result;
    }

    public String getCodeName(String query, String param) {

        String result = "";

        try (Connection con = getLegendConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setQueryTimeout(QUERY_TIMEOUT_SECONDS);
            ps.setString(1, param);

            if (query.trim().toUpperCase().startsWith("UPDATE")) {
                ps.executeUpdate();
                return "";
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = rs.getString(1);
                }
            }

        } catch (Exception e) {
            System.err.println("ERROR getCodeName(param): " + e.getMessage());
        }

        return result == null ? "" : result;
    }

    /* =========================================================
       SAFE INSERT METHODS
       ========================================================= */

    public void insToAm_Invoice_No(String assetID, String lpo,
                                   String invoiceNo, String transType) {

        String sql = "INSERT INTO Am_Invoice_no " +
                "(asset_id,lpo,invoice_no,trans_type,create_date) " +
                "VALUES (?,?,?,?,?)";

        try (Connection con = getLegendConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setQueryTimeout(QUERY_TIMEOUT_SECONDS);

            ps.setString(1, assetID);
            ps.setString(2, lpo);
            ps.setString(3, invoiceNo);
            ps.setString(4, transType);
            ps.setString(5, String.valueOf(df.dateConvert(new Date())));

            ps.executeUpdate();

        } catch (Exception e) {
            System.err.println("ERROR insToAm_Invoice_No(): " + e.getMessage());
        }
    }

    public void insGrpToAm_Invoice_No(String assetID, String lpo,
                                      String invoiceNo, String transType,
                                      String grpID) {

        String sql = "INSERT INTO Am_Invoice_no " +
                "(asset_id,lpo,invoice_no,trans_type,group_id,create_date) " +
                "VALUES (?,?,?,?,?,?)";

        try (Connection con = getLegendConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setQueryTimeout(QUERY_TIMEOUT_SECONDS);

            ps.setString(1, assetID);
            ps.setString(2, lpo);
            ps.setString(3, invoiceNo);
            ps.setString(4, transType);
            ps.setString(5, grpID);
            ps.setString(6, String.valueOf(df.dateConvert(new Date())));

            ps.executeUpdate();

        } catch (Exception e) {
            System.err.println("ERROR insGrpToAm_Invoice_No(): " + e.getMessage());
        }
    }

    /* =========================================================
       SAFE findCategoryItems()
       ========================================================= */

    public ArrayList<Category> findCategoryItems(String categoryId,
                                                 String categoryCode,
                                                 String status) {

        ArrayList<Category> list = new ArrayList<>();

        String sql = "SELECT itemCode,isInventory,itemName " +
                "FROM am_ad_categoryItems " +
                "WHERE category_id=? AND category_code=? AND status=?";

        try (Connection con = getLegendConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setQueryTimeout(QUERY_TIMEOUT_SECONDS);

            ps.setString(1, categoryId);
            ps.setString(2, categoryCode);
            ps.setString(3, status);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Category c = new Category();
                    c.setCode(rs.getString("itemCode"));
                    c.setInventoryItem(rs.getString("isInventory"));
                    c.setName(rs.getString("itemName"));
                    list.add(c);
                }
            }

        } catch (Exception e) {
            System.err.println("ERROR findCategoryItems(): " + e.getMessage());
        }

        return list;
    }

    /* =========================================================
       SAFE updateAssetStatusChange()
       ========================================================= */

    public void updateAssetStatusChange(String query) {

        try (Connection con = getLegendConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setQueryTimeout(QUERY_TIMEOUT_SECONDS);
            ps.executeUpdate();

        } catch (Exception e) {
            System.err.println("ERROR updateAssetStatusChange(): " + e.getMessage());
        }
    }

    public void updateAssetStatusChange(String query,
                                        Timestamp date,
                                        int tranId) {

        try (Connection con = getLegendConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setQueryTimeout(QUERY_TIMEOUT_SECONDS);
            ps.setTimestamp(1, date);
            ps.setInt(2, tranId);
            ps.executeUpdate();

        } catch (Exception e) {
            System.err.println("ERROR updateAssetStatusChange(): " + e.getMessage());
        }
    }

}