package legend.asset_report;

import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class Utilities {
    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
    PreparedStatement ps = null;
    private String jndiName = "ePostmanager";

    public Utilities() {
    }

    public String[] company_Details() {

        String[] result = new String[2];

        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        String qry = "SELECT company_name,company_address FROM am_gb_company";
        String company_name = "";
        try {
            con = getConnection();
            ps = con.prepareStatement(qry);
            rs = ps.executeQuery();
            while (rs.next()) {
                result[0] = rs.getString(1);
                result[1] = rs.getString(2);

            }

        } catch (Exception e) {
            System.out.println("WARNING::ERROR GETCOMPANY  AM_GB_COMPANY " + e);
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                System.out.println("WARNING::Error Closing Connection " + e);
            }
        }
        return result;
    }

    public String getResources(String selected, String query) {
        Connection mcon;
        PreparedStatement mps;
        ResultSet mrs;
        String html;
        mcon = null;
        mps = null;
        mrs = null;
        html = "";
        String id = "";
        try {
            mcon = getConnection();
            mps = mcon.prepareStatement(query);
            for (mrs = mps.executeQuery(); mrs.next(); ) {
                id = mrs.getString(1);
                html = html + "<option value='" + id + "' " +
                       (id.equals(selected) ? " selected " : "") + ">" +
                       mrs.getString(2) + "</option> ";
            }
        } catch (Exception Ex) {
            Ex.printStackTrace();
        } finally {
            try {
                if (mps != null) {
                    mps.close();
                }
                if (mrs != null) {
                    mrs.close();
                }
                if (mcon != null) {
                    mcon.close();
                }
            } catch (Exception Ex) {
                Ex.printStackTrace();
            }
        }
        return html;
    }

    //connect to rdbms
    public Connection getConnection() throws Exception {
        Connection con = null;
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/" +
                    this.jndiName);
            //System.out.println
            con = ds.getConnection();

        } catch (Exception _e) {
            System.out.println("DataAccess::getConnection()!" + _e);
            _e.printStackTrace();
            con = null;
        }
        return con;
    }

    /*
     * public static Connection getConnection() throws ClassNotFoundException, SQLException
      {
     //Change these settings according to your local configuration
     String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
     String connectString = "jdbc:sqlserver://charles:1433;database=fixedasset";
     String user = "magbel-staff";
     String password = "magbel";

       -- connection to sqlserver 2005
       -- Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
       --this.conn = DriverManager.getConnection("jdbc:sqlserver://charles:1433;database=fixedasset;user=magbel-staff;password=magbel");


     Class.forName(driver);
     Connection conn = DriverManager.getConnection(connectString, user, password);
     return conn;
      }
     */
}
