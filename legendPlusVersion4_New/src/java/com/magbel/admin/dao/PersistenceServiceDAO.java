package com.magbel.admin.dao;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.magbel.util.DataConnect;
import com.magbel.util.DatetimeFormat;

public class PersistenceServiceDAO implements ConnectionDAO {

    private final DatetimeFormat dateFormat = new DatetimeFormat();

    // Thread-safe formatter
    private static final ThreadLocal<SimpleDateFormat> DATE_FORMAT =
            ThreadLocal.withInitial(() -> new SimpleDateFormat("dd-MM-yyyy"));

    /* =========================================================
       QUERY EXECUTION
       ========================================================= */

    public void executeQuery(String query) {
        executeQueryString(query, "helpDesk");
    }

    public void executeQueryString(String query, String jndiName) {

        try (Connection connection = getConnection(jndiName);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.execute();

        } catch (SQLException ex) {
            throw new RuntimeException(
                    "Failed executing query on datasource: " + jndiName,
                    ex
            );
        }
    }

    /* =========================================================
       CONNECTION MANAGEMENT
       ========================================================= */

    public Connection getConnection() {
        return getConnection("helpDesk");
    }

    public Connection getConnection(String jndiName) {
        try {
            return new DataConnect(jndiName).getConnection();
        } catch (Exception ex) {
            throw new RuntimeException(
                    "Failed to obtain connection for datasource: " + jndiName,
                    ex
            );
        }
    }

    @Override
    public void closeConnection(Connection con, PreparedStatement ps, ResultSet rs) {

    }

    @Override
    public void closeConnection(Connection con, PreparedStatement ps) {

    }

    /* =========================================================
       DATE UTILITIES
       ========================================================= */

    public java.sql.Date dateConvert(String strDate) {

        try {
            if (strDate == null) {
                return new java.sql.Date(System.currentTimeMillis());
            }

            strDate = strDate.replace("/", "-");
            Date parsed = DATE_FORMAT.get().parse(strDate);

            return dateFormat.getSQLFormatedDate(parsed);

        } catch (ParseException ex) {
            throw new IllegalArgumentException(
                    "Invalid date format (expected dd-MM-yyyy): " + strDate,
                    ex
            );
        }
    }

    public java.sql.Date dateConvert(Date inputDate) {
        if (inputDate == null) {
            inputDate = new Date();
        }
        return dateFormat.getSQLFormatedDate(inputDate);
    }

    public java.sql.Date dateConvert(long longDate) {
        return dateConvert(new Date(longDate));
    }

    public String formatDate(Date date) {
        if (date == null) {
            date = new Date();
        }
        return DATE_FORMAT.get().format(date);
    }

    public Timestamp getDateTime(String inputDate) {

        try {
            if (inputDate == null) {
                inputDate = formatDate(new Date());
            }

            inputDate = inputDate.replace("/", "-");

            SimpleDateFormat dateTimeFormat =
                    new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");

            String now = dateTimeFormat.format(new Date());
            String merged = inputDate + now.substring(10);

            return new Timestamp(dateTimeFormat.parse(merged).getTime());

        } catch (Exception ex) {
            throw new IllegalArgumentException(
                    "Error generating timestamp from date: " + inputDate,
                    ex
            );
        }
    }

    public Timestamp getDateTime(Date inputDate) {
        return getDateTime(formatDate(inputDate));
    }
}