package asset.report;

import java.sql.*;
import java.util.Properties;
import java.io.FileInputStream;
import javax.naming.*;
import javax.sql.DataSource;
import javax.swing.*;
/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class DataConnect {
    private static String username = "";
    private static String password = "";
    private static String driver = "";
    private static String url = "";
    private String jndiName = "ePostmanager";
  JTextPane jTextPane1 = new JTextPane();

    public DataConnect() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    }
    public DataConnect(String dbId) {
      this.jndiName = dbId;
      
   }
    
    public Connection getConnection() throws Exception
    {
        Connection con = null;
        try{
          Context initContext = new InitialContext();
          Context envContext  = (Context)initContext.lookup("java:/comp/env");  
          DataSource ds = (DataSource)envContext.lookup("jdbc/"+this.jndiName); 
          //System.out.println
          con = ds.getConnection();
         
        } catch (Exception _e) {
            System.out.println("DataAccess::getConnection()!"+_e);
            _e.printStackTrace();
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
    
    public Connection getConnection(String host) throws Exception
    {
     Connection con = null;
     if(host.equalsIgnoreCase("ePostmanager"))
     {
      //System.out.println("epostmanager....");
      try
      {  
       Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");
       con = DriverManager.getConnection("jdbc:microsoft:sqlserver://localhost:1433","epost","epost");
      }
      catch (Exception _e)
      {
       System.out.println("DataAccess::getConnection()!"+_e);
       _e.printStackTrace();
       con = null;
      }
      return con;
     }
     else if(host.equalsIgnoreCase("intercon"))
     {
      try
      {
       Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
       con = DriverManager.getConnection("jdbc:odbc:INTERCON", "INTERFACE", "INTERFACE");
      }
      catch (Exception _e)
      {
       System.out.println("DataAccess::getConnection()!"+_e);
       _e.printStackTrace();
       con = null;
      }
      return con;
     }
     else if(host.equalsIgnoreCase("ora-gateway"))
     {
      try
      {
       Class.forName("oracle.jdbc.driver.OracleDriver");
       con = DriverManager.getConnection("jdbc:oracle:thin:@194.50.1.107:1521:fcrlive", "fcrlive", "fcrlive");
      }
      catch (Exception _e)
      {
       System.out.println("DataAccess::getConnection()!"+_e);
       _e.printStackTrace();
       con = null;
      }
      return con;  
     }
     else
     {
      System.out.println("equity........");
      try
      {
       Class.forName("com.sybase.jdbc2.jdbc.SybDataSource");
       con = DriverManager.getConnection("jdbc:sybase:Tds:magbelserver:5000/banking", "sa", "magbel123");
      }
      catch (Exception _e)
      {
       System.out.println("DataAccess::getConnection()!"+_e);
       _e.printStackTrace();
       con = null;
      }
      return con; 
     }
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
}
