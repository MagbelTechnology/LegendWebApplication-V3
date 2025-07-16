package legend.admin.handlers;


import java.sql.*;
import java.text.SimpleDateFormat;

import com.magbel.util.DataConnect;

public class LicenseTypeHandler {
    Connection con = null;
    Statement stmt = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    DataConnect dc;
    SimpleDateFormat sdf;
    final String space = "  ";
    final String comma = ",";
    java.util.Date date;
    com.magbel.util.DatetimeFormat df;
    public LicenseTypeHandler() {

        sdf = new SimpleDateFormat("dd-MM-yyyy");
        df = new com.magbel.util.DatetimeFormat();
        System.out.println("USING_ " + this.getClass().getName());
    }

    public java.util.List getAllLicenseTypes() {
        java.util.List _list = new java.util.ArrayList();
        legend.admin.objects.LicenseType licensetype = null;
        String query = "SELECT license_id,license_code,license_name"
                       + ",notify_days,every_days"
                       + ",account_type ,suspense_acct,license_status"
                       + ",user_id ,create_date"
                       + " FROM am_ad_licenseType";

        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String licenseId = rs.getString("license_id");
                String licenseCode = rs.getString("license_code");
                String licenseName = rs.getString("license_name");
                String notifyDays = rs.getString("notify_days");
                String everyDays = rs.getString("every_days");
                String accountType = rs.getString("account_type");
                String suspenseAcct = rs.getString("suspense_acct");
                String licenseStatus = rs.getString("license_status");
                String userId = rs.getString("user_id");
                String createDate = sdf.format(rs.getDate("create_date"));
                licensetype = new legend.admin.objects.LicenseType();
                licensetype.setLicenseId(licenseId);
                licensetype.setLicenseCode(licenseCode);
                licensetype.setLicenseName(licenseName);
                licensetype.setNotifyDays(notifyDays);
                licensetype.setEveryDays(everyDays);
                licensetype.setAccountType(accountType);
                licensetype.setSuspenseAcct(suspenseAcct);
                licensetype.setLicenseStatus(licenseStatus);
                licensetype.setUserId(userId);
                licensetype.setCreateDate(createDate);
                _list.add(licensetype);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }

    public java.util.List getLicenseTypeByQuery(String filter) {
        java.util.List _list = new java.util.ArrayList();
        legend.admin.objects.LicenseType licensetype = null;
        String query = "SELECT license_id,license_code,license_name"
                       + ",notify_days,every_days"
                       + ",account_type ,suspense_acct,license_status"
                       + ",user_id ,create_date"
                       + " FROM am_ad_licenseType WHERE license_id IS NOT NULL ";


        query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String licenseId = rs.getString("license_id");
                String licenseCode = rs.getString("license_code");
                String licenseName = rs.getString("license_name");
                String notifyDays = rs.getString("notify_days");
                String everyDays = rs.getString("every_days");
                String accountType = rs.getString("account_type");
                String suspenseAcct = rs.getString("suspense_acct");
                String licenseStatus = rs.getString("license_status");
                String userId = rs.getString("user_id");
                String createDate = sdf.format(rs.getDate("create_date"));
                licensetype = new legend.admin.objects.LicenseType();
                licensetype.setLicenseId(licenseId);
                licensetype.setLicenseCode(licenseCode);
                licensetype.setLicenseName(licenseName);
                licensetype.setNotifyDays(notifyDays);
                licensetype.setEveryDays(everyDays);
                licensetype.setAccountType(accountType);
                licensetype.setSuspenseAcct(suspenseAcct);
                licensetype.setLicenseStatus(licenseStatus);
                licensetype.setUserId(userId);
                licensetype.setCreateDate(createDate);
                _list.add(licensetype);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }

    public legend.admin.objects.LicenseType getLicenseTypeByLicenseID(String liceid) {
        legend.admin.objects.LicenseType licensetype = null;
        String query = "SELECT license_id,license_code,license_name"
                       + ",notify_days,every_days"
                       + ",account_type ,suspense_acct,license_status"
                       + ",user_id ,create_date"
                       + " FROM am_ad_licenseType WHERE license_id=" + liceid;

        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String licenseId = rs.getString("license_id");
                String licenseCode = rs.getString("license_code");
                String licenseName = rs.getString("license_name");
                String notifyDays = rs.getString("notify_days");
                String everyDays = rs.getString("every_days");
                String accountType = rs.getString("account_type");
                String suspenseAcct = rs.getString("suspense_acct");
                String licenseStatus = rs.getString("license_status");
                String userId = rs.getString("user_id");
                String createDate = sdf.format(rs.getDate("create_date"));
                licensetype = new legend.admin.objects.LicenseType();
                licensetype.setLicenseId(licenseId);
                licensetype.setLicenseCode(licenseCode);
                licensetype.setLicenseName(licenseName);
                licensetype.setNotifyDays(notifyDays);
                licensetype.setEveryDays(everyDays);
                licensetype.setAccountType(accountType);
                licensetype.setSuspenseAcct(suspenseAcct);
                licensetype.setLicenseStatus(licenseStatus);
                licensetype.setUserId(userId);
                licensetype.setCreateDate(createDate);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(c, s, rs);
        }
        return licensetype;

    }

    public legend.admin.objects.LicenseType getLicenseTypeByLicenseCode(String licecode) {
        
        legend.admin.objects.LicenseType licensetype = null;
        String query = "SELECT license_id,license_code,license_name"
                       + ",notify_days,every_days"
                       + ",account_type ,suspense_acct,license_status"
                       + ",user_id ,create_date"
                       + " FROM am_ad_licenseType WHERE license_code='" + licecode+"'";


        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String licenseId = rs.getString("license_id");
                String licenseCode = rs.getString("license_code");
                String licenseName = rs.getString("license_name");
                String notifyDays = rs.getString("notify_days");
                String everyDays = rs.getString("every_days");
                String accountType = rs.getString("account_type");
                String suspenseAcct = rs.getString("suspense_acct");
                String licenseStatus = rs.getString("license_status");
                String userId = rs.getString("user_id");
                String createDate = sdf.format(rs.getDate("create_date"));
                licensetype = new legend.admin.objects.LicenseType();
                licensetype.setLicenseId(licenseId);
                licensetype.setLicenseCode(licenseCode);
                licensetype.setLicenseName(licenseName);
                licensetype.setNotifyDays(notifyDays);
                licensetype.setEveryDays(everyDays);
                licensetype.setAccountType(accountType);
                licensetype.setSuspenseAcct(suspenseAcct);
                licensetype.setLicenseStatus(licenseStatus);
                licensetype.setUserId(userId);
                licensetype.setCreateDate(createDate);
               
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(c, s, rs);
        }
        return licensetype;

    }


    private Connection getConnection() {
        Connection con = null;
        dc = new DataConnect("fixedasset");
        try {
            con = dc.getConnection();
        } catch (Exception e) {
            System.out.println("WARNING: Error getting connection ->" +
                               e.getMessage());
        }
        return con;
    }

    private void closeConnection(Connection con, Statement s) {
        try {
            if (s != null) {
                s.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (Exception e) {
            System.out.println("WARNING: Error getting connection ->" +
                               e.getMessage());
        }

    }

    private void closeConnection(Connection con, PreparedStatement ps) {
        try {
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (Exception e) {
            System.out.println("WARNING: Error closing connection ->" +
                               e.getMessage());
        }

    }

    /**
     *
     * @param con Connection
     * @param s Statement
     * @param rs ResultSet
     */
    private void closeConnection(Connection con, Statement s, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (s != null) {
                s.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (Exception e) {
            System.out.println("WARNING: Error closing connection ->" +
                               e.getMessage());
        }
    }

    /**
     *
     * @param con Connection
     * @param ps PreparedStatement
     * @param rs ResultSet
     */
    private void closeConnection(Connection con, PreparedStatement ps,
                                 ResultSet rs) {
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
        } catch (Exception e) {
            System.out.println("WARNING: Error closing connection ->" +
                               e.getMessage());
        }
    }

    private boolean executeQuery(String query) {
        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            done = ps.execute();

        } catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;
    }


    public boolean createLicenseType(legend.admin.objects.LicenseType licensetype) {

        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;
        String query = "INSERT INTO am_ad_licenseType"
                       + "(license_code,license_name"
                       + ",notify_days,every_days"
                       + ",account_type ,suspense_acct,license_status"
                       + ",user_id ,create_date,license_id)"
                       + " VALUES (?,?,?,?,?,?,?,?,?,?)";

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            
            ps.setString(1, licensetype.getLicenseCode());
            ps.setString(2, licensetype.getLicenseName());
            ps.setString(3, licensetype.getNotifyDays());
            ps.setString(4, licensetype.getEveryDays());
            ps.setString(5, licensetype.getAccountType());
            ps.setString(6, licensetype.getSuspenseAcct());
            ps.setString(7, licensetype.getLicenseStatus());
            ps.setString(8, licensetype.getUserId());
            ps.setDate(9, df.dateConvert(new java.util.Date()));
           ps.setLong(10, System.currentTimeMillis());
           done = (ps.executeUpdate() != -1);

        } catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }

    public boolean updateLicenseType(legend.admin.objects.LicenseType licensetype) {

        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;

        String query = "UPDATE am_ad_licenseType SET "
                       +"license_code = ?,license_name = ?"
                       +" , notify_days = ?,every_days = ?,account_type = ?"
                       +" , suspense_acct = ?,license_status = ?"
                        +" WHERE license_id =?";

        try {
            con = getConnection();
            ps = con.prepareStatement(query);

            ps.setString(1, licensetype.getLicenseCode());
            ps.setString(2, licensetype.getLicenseName());
            ps.setString(3, licensetype.getNotifyDays());
            ps.setString(4, licensetype.getEveryDays());
            ps.setString(5, licensetype.getAccountType());
            ps.setString(6, licensetype.getSuspenseAcct());
            ps.setString(7, licensetype.getLicenseStatus());
           // ps.setString(8, licensetype.getUserId());
            //ps.setDate(9, df.dateConvert(new java.util.Date()));
            ps.setString(8, licensetype.getLicenseId());
            done = (ps.executeUpdate() != -1);
        } catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }


}
