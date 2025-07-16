/*
 *
 *
 *
 */

package com.magbel.util;

/**
 *
 * @author  Oyindamola Davies
 */
/**
 *File Name            :DataPoolSource.java
 *Project              :apex
 *@version             :1.0
 *Creation Date        :Created on Nov 13, 2005, 7:58 AM
 *
 *@return              No return value
 *@exception          :No exceptions thrown
 *
 * Description         :An utility class for connecting to the databases.
 */
import java.io.*;
import java.sql.*;

public class DataPoolSource {


    public Connection connectionPool;


    public void setConnectionPool(Connection con) {
        connectionPool = con;
    }

    public Connection getConnectionPool() {
        return connectionPool;
    }

    /** Creates a new instance of DataPoolSource */
    public DataPoolSource() {

        try {

            Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");
            Connection eCon = DriverManager.getConnection(
                    "jdbc:microsoft:sqlserver://magbel-06:1433", "apexuser", "apex");
            setConnectionPool(eCon);

        } catch (Exception e) {
            System.out.println("stdout:error connecting to DBServer " +
                               e.toString());
        }

    }


    public synchronized void closeConnection(PreparedStatement ps,
                                             Connection con) {

        if (ps != null) {
            try {
                ps.close();
            } catch (Exception statementError) {

                System.out.println("Statement could not close:" +
                                   statementError);
            }
        }

        if (con != null) {
            try {

                con.close();
            } catch (SQLException errorConnect) {

                System.out.println("Connection could not close :" +
                                   errorConnect);
            }
        }
    }

    /**
     * Destroys resources created during the test case.
     *
     * @throws Exception DOCUMENT ME!
     */
    public synchronized void closeConnection(ResultSet rs, PreparedStatement ps,
                                             Connection con) {

        if (rs != null) {

            try {
                rs.close();
            } catch (Exception errorResult) {
                System.out.println("ResultSet could not close: " + errorResult);
            }
        }

        if (ps != null) {
            try {
                ps.close();
            } catch (Exception statementError) {

                System.out.println("Statement could not close:" +
                                   statementError);
            }
        }

        if (con != null) {
            try {

                con.close();
            } catch (SQLException errorConnect) {

                System.out.println("Connection could not close :" +
                                   errorConnect);
            }
        }
    }

    public static void main(String[] args) {
        DataPoolSource ds = new DataPoolSource();
        System.out.println("new Connection is " + ds.getConnectionPool());
    }

}
