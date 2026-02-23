package com.magbel.util;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HtmlUtilily {

    private static final int QUERY_TIMEOUT = 30;

    public HtmlUtilily() {
    }

    private Connection getConnection() throws SQLException {
        return new DataConnect("ias").getConnection();
    }

    /* =========================================================
       SAFE getResources()
       ========================================================= */

    public String getResources(String selected, String query) {

        if (selected == null) {
            selected = "";
        }

        StringBuilder html = new StringBuilder();

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setQueryTimeout(QUERY_TIMEOUT);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    String id = rs.getString(1);
                    String name = rs.getString(2);

                    html.append("<option ")
                            .append(id != null && id.equals(selected) ? "selected " : "")
                            .append("value='")
                            .append(id)
                            .append("'>")
                            .append(name)
                            .append("</option>");
                }
            }

        } catch (Exception e) {
            System.err.println("HtmlUtilily.getResources ERROR: " + e.getMessage());
        }

        return html.toString();
    }

    /* =========================================================
       SAFE findObject()
       ========================================================= */

    public String findObject(String query) {

        String result = "UNKNOWN";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setQueryTimeout(QUERY_TIMEOUT);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = rs.getString(1);
                }
            }

        } catch (Exception e) {
            System.err.println("HtmlUtilily.findObject ERROR: " + e.getMessage());
        }

        return result == null ? "" : result;
    }

    /* =========================================================
       SAFE findintObject()
       ========================================================= */

    public double findintObject(String query) {

        double result = 0.0;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setQueryTimeout(QUERY_TIMEOUT);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = rs.getDouble(1);
                }
            }

        } catch (Exception e) {
            System.err.println("HtmlUtilily.findintObject ERROR: " + e.getMessage());
        }

        return result;
    }

    /* =========================================================
       SAFE getCodeName()
       ========================================================= */

    public String getCodeName(String query) {

        String result = "";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setQueryTimeout(QUERY_TIMEOUT);

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
            System.err.println("HtmlUtilily.getCodeName ERROR: " + e.getMessage());
        }

        return result == null ? "" : result;
    }

    /* =========================================================
       SAFE getResources2()
       ========================================================= */

    public String getResources2(String selected, String query) {

        if (selected == null) {
            selected = "";
        }

        StringBuilder html = new StringBuilder();

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setQueryTimeout(QUERY_TIMEOUT);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    String id = rs.getString(2);

                    html.append("<option ")
                            .append(id != null && id.equals(selected) ? "selected " : "")
                            .append(" value='")
                            .append(id)
                            .append("'>")
                            .append(rs.getString(2))
                            .append("</option>");
                }
            }

        } catch (Exception e) {
            System.err.println("HtmlUtilily.getResources2 ERROR: " + e.getMessage());
        }

        return html.toString();
    }

    /* =========================================================
       UNIQUE GENERATOR (unchanged)
       ========================================================= */

    public String getUnique(String epostId) {
        return epostId +
                new SimpleDateFormat("yyyyMMddHHmmSSSS").format(new Date()) +
                removeNdot(String.valueOf(Math.random() * 10000000D));
    }

    private String removeNdot(String value) {

        if (value.contains(".")) {
            String decimal = value.substring(value.indexOf('.') + 1);
            if (decimal.length() == 1) {
                decimal += "0";
            }
            return value.substring(0, value.indexOf('.')) + decimal;
        }
        return value + "00";
    }
}