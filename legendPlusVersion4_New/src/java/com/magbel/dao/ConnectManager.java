
package com.magbel.dao;

import com.magbel.util.DataConnect;

import java.sql.*;


public class ConnectManager 
{
  private String jndiName = "legendPlus";
  public ConnectManager()
  {
  }
  //connect to rdbms
  public Connection getConnection() throws ClassNotFoundException, SQLException
	{
	 //Change these settings according to your local configuration
	 String driver = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
	 String connectString = "jdbc:microsoft:sqlserver://localhost:1433";
	 String user = "legend";
	 String password = "legend";
  
   Class.forName(driver);
	 Connection conn = DriverManager.getConnection(connectString, user, password);
	 return conn;
	}
    public Connection getOracleConnection() throws ClassNotFoundException, SQLException
          {
           //Change these settings according to your local configuration
           String driver = "oracle.jdbc.driver.OracleDriver";
           String connectString = "jdbc:oracle:thin:@192.168.1.6:1521:orcl";
           String user = "legend";
           String password = "legend";
    
     Class.forName(driver);
           Connection conn = DriverManager.getConnection(connectString, user, password);
           return conn;
          }      
  /*
  public Connection getConnection() throws Exception
    {
        Connection con = null;
        try{
          Context initContext = new InitialContext();
          Context envContext  = (Context)initContext.lookup("java:/comp/env");  
          DataSource ds = (DataSource)envContext.lookup("jdbc/"+this.jndiName); 
          con = ds.getConnection();
         
        } catch (Exception _e) {
            System.out.println("DataAccess::getConnection()!"+_e);
            _e.printStackTrace();
            con = null;
           }
        return con;
    }
    */
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
    public void closeConnection(Connection con, PreparedStatement ps,
                                ResultSet rs) {
        try {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (Exception ex) {
            System.out.println("WARNING:Error closing Connection ->" + ex);
        }
    }
    public void closeConnection(Connection con, PreparedStatement ps) {

        try {
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (Exception ex) {
            System.out.println("WARNING:Error closing Connection ->" + ex);
        }
    }
    public void closeConnection(Connection con, Statement ps,
                                        ResultSet rs) {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                    if (rs != null) {
                        rs.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } catch (Exception ex) {
                    System.out.println("WARNING:Error closing Connection ->" + ex);
                }
    }

    
    public static void main(String[] args) {
    
   /* 
    try{
        ConnectManager cman = new ConnectManager();
            Connection cn = cman.getOracleConnection();
            System.out.println("connection:"+cn);
    }
    catch(Exception e){e.printStackTrace();}   
    */
    }
    
}