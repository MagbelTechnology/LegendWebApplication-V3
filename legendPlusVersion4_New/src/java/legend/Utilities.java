package legend;

/**
 * <p>Title: Magma.net System</p>
 *
 * <p>Description: Fixed Assets Manager</p>
 *
 * <p>Copyright: Copyright (c) 2006. All rights reserved.</p>
 *
 * <p>Company: Magbel Technologies Limited.</p>
 *
 * @author Charles Ayoola Ayodele-Peters.
 * @version 1.0
 */

import java.sql.*;
import java.util.StringTokenizer;

public class Utilities {
    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
    PreparedStatement ps = null;

    public Utilities() {
    }

    public static String getProcess_Date() {
        String[] getMonth = tokenize(getPay_Roll_Sys_Info()[0].substring(0, 10),
                                     "-");
        if (getMonth[1].equals("01")) {
            return "January " + getMonth[0];
        } else
        if (getMonth[1].equals("02")) {
            return "February " + getMonth[0];
        } else
        if (getMonth[1].equals("03")) {
            return "March " + getMonth[0];
        } else
        if (getMonth[1].equals("04")) {
            return "April " + getMonth[0];
        } else
        if (getMonth[1].equals("05")) {
            return "May " + getMonth[0];
        } else
        if (getMonth[1].equals("06")) {
            return "June " + getMonth[0];
        } else
        if (getMonth[1].equals("07")) {
            return "July " + getMonth[0];
        } else
        if (getMonth[1].equals("08")) {
            return "August " + getMonth[0];
        } else
        if (getMonth[1].equals("09")) {
            return "September " + getMonth[0];
        } else
        if (getMonth[1].equals("10")) {
            return "October " + getMonth[0];
        } else
        if (getMonth[1].equals("11")) {
            return "November " + getMonth[0];
        } else {
            return "December " + getMonth[0];
        }

    }

    public static String[] tokenize(String dt, String delimiter) {
        String[] tokens = new String[3];
        StringTokenizer st = new StringTokenizer(dt, delimiter);
        int i = 0;
        while (st.hasMoreTokens()) {
            tokens[i++] = st.nextToken();
        }

        return tokens;
    }

    public String toSQL(String dt) {
        //year,mth,day to SQL
        String[] result = tokenize(dt.substring(0, 10), "-");

        // System.out.println("Year : in toSQL : " + result[2] + " " + result[1] + " " + result[0]);

        return result[2] + "/" + result[1] + "/" + result[0];
    }

    public static String[] getPay_Roll_Sys_Info() {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        String qry =
                "SELECT CURR_DATE,H_PEND,MONTH_NO,RUN_DAYS FROM AP_PR_SYSTEM_INFO";
        String[] result = new String[4];
        try {
            con = getConnection();
            ps = con.prepareStatement(qry);
            rs = ps.executeQuery();
            while (rs.next()) {
                result[0] = rs.getString(1); //curr_date
                result[1] = rs.getString(2); //h_pend
                result[2] = rs.getString(3); //month_no
                result[3] = rs.getString(4); //run_days
            }

        } catch (Exception e) {
            System.out.println("WARNING::ERROR GETTING PROCESS DATE " + e);
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

    public String[] company_Details() {

        String[] result = new String[2];

        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        String qry = "SELECT company_name,company_address FROM am_gb_company";
        String company_name = "";
        try {
            con = this.getConnection();
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
    public static Connection getConnection() throws ClassNotFoundException,
            SQLException {
        //Change these settings according to your local configuration
        String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        String connectString =
                "jdbc:sqlserver://charles:1433;database=fixedasset";
        String user = "magbel-staff";
        String password = "magbel";
        /*
         * connection to sqlserver 2005
         * Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
             this.conn = DriverManager.getConnection("jdbc:sqlserver://charles:1433;database=fixedasset;user=magbel-staff;password=magbel");
         *
         */

        Class.forName(driver);
        Connection conn = DriverManager.getConnection(connectString, user,
                password);
        return conn;
    }

    //this method process licence expiration and notification

    public void processLicenceAlert() {

    }
}
