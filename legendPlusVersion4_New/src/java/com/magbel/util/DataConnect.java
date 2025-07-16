package com.magbel.util;

import java.sql.*;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.swing.JTextPane;

/**
 * <p>Title: DataConnect.java</p>
 *
 * <p>Description: a JNDI Look Database Connection Pool</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company:Ocular-Minds Group(TM) </p>
 *
 * @author Jejelowo Festus B.
 * @version 1.0
 */
public class DataConnect {
    private static String username = "";
    private static String password = "";
    private static String driver =
            "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static String url = "";
    private String jndiName = "RamsDB";
    JTextPane jTextPane1 = new JTextPane();

    public DataConnect() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
  
    public DataConnect(String dbId) {
        this.jndiName = dbId;
    }

    public Connection getConnection() throws Exception {
        Connection con = null;

        try {   
//        	System.out.println("========>con in getConnection of DataConnect: "+con);
        	if(con==null){
            Context initContext = new InitialContext();
            String dsJndi = "java:/legendPlus";
            DataSource ds = (DataSource) initContext.lookup(
            		dsJndi);
            con = ds.getConnection();
        	}
        } catch (Exception _e) {
            System.out.println("WARNING::DataAccess::getConnection()!" + _e);
            con = null;
        }

        return con;
    }

    public Connection getConnection(String dbUrl, String uname,
                                    String pass) {
        this.username = uname;
        this.password = pass;
        this.url = dbUrl;
        Connection con = null;
        try {

            Properties p = new Properties();
            Class.forName(this.driver);
            p.put("jdbc.drivers", this.driver);
            p.put("user", this.username.toString());
            p.put("password", this.password.toString());
            con = DriverManager.getConnection(this.url, p);
        } catch (Exception ex) {
            System.out.println("DataAccess::getConnection()!");
            ex.printStackTrace();
        }
        return con;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private void jbInit() throws Exception {
        jTextPane1.setText("jTextPane1");
    }

    public void closeOpenConnection(ResultSet rs, PreparedStatement ps,
                                    Connection con) {
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

        } catch (Exception error) {
            System.out.println("WARNING:Error closing connection->" +
                               error.getMessage());
        }
    }

    public void closeOpenConnection(PreparedStatement ps, Connection con) {
        try {
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }

        } catch (Exception error) {
            System.out.println("WARNING:Error closing connection->" +
                               error.getMessage());
        }
    }
}
