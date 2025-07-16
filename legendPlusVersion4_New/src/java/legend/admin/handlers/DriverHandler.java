package legend.admin.handlers;


import java.sql.*;
import java.text.SimpleDateFormat;

import com.magbel.util.DataConnect;

public class DriverHandler {
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
    public DriverHandler() {

        sdf = new SimpleDateFormat("dd-MM-yyyy");
        df = new com.magbel.util.DatetimeFormat();
        System.out.println("USING_ " + this.getClass().getName());
    }

    public java.util.ArrayList getAllDrivers() {
        java.util.ArrayList _list = new java.util.ArrayList();
        legend.admin.objects.Driver driver = null;
        String query ="SELECT [Driver_ID], [Driver_Code], [Driver_License], [dl_issue_date]," 
                		+ "[dl_expiry_date], [Driver_LastName], [Driver_FirstName], [Driver_OtherName],"
						+ "[Driver_Branch], [Driver_Dept], [Contact_Address], [Driver_State], [Driver_Phone],"
						+ "[Driver_Fax], [Driver_email], [Driver_Status], [driver_province], [User_id],"
						+ "[Create_date] FROM [am_ad_driver]";				   

        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String driverId = rs.getString("Driver_ID");
                String code = rs.getString("Driver_Code");
				String license = rs.getString("Driver_License");
                String issueD = sdf.format(rs.getDate("dl_issue_date"));
                String expD = sdf.format(rs.getDate("dl_expiry_date"));
                String lastN = rs.getString("Driver_LastName");
                String firstN = rs.getString("Driver_FirstName");
				String otherN = rs.getString("Driver_OtherName");
                String branch = rs.getString("Driver_Branch");
                String dept = rs.getString("Driver_Dept");
				String address = rs.getString("Contact_Address");
				String state = rs.getString("Driver_State");
				String phone = rs.getString("Driver_Phone");
				String fax = rs.getString("Driver_Fax");
				String email = rs.getString("Driver_email");
				String status = rs.getString("Driver_Status");
				String province = rs.getString("driver_province");
				String userId = rs.getString("User_id");
				String createD = sdf.format(rs.getDate("Create_date"));
				
                driver = new legend.admin.objects.Driver();
                driver.setDriverId(driverId);
                driver.setDriverCode(code);
                driver.setDriverLicense(license);
				driver.setDlIssueDate(issueD);
                driver.setDlExpiryDate(expD);
                driver.setDriverLastName(lastN);
                driver.setDriverFirstname(firstN);
                driver.setDriverOtherName(otherN);
                driver.setDriverBranch(branch);
				driver.setDriverDept(dept);
				driver.setContatcAddress(address);
				driver.setDriverState(state);
				driver.setDriverPhone(phone);
				driver.setDriverFax(fax);
				driver.setDriverEmail(email);
				driver.setDriverStatus(status);
				driver.setDriverProvince(province);
				driver.setUserId(userId);
				driver.setCreateDate(createD);
                _list.add(driver);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }

    public java.util.ArrayList getDriverByQuery(String filter) {
        java.util.ArrayList _list = new java.util.ArrayList();
        legend.admin.objects.Driver driver = null;
        String query = "SELECT [Driver_ID], [Driver_Code], [Driver_License], [dl_issue_date]," 
                		+ "[dl_expiry_date], [Driver_LastName], [Driver_FirstName], [Driver_OtherName],"
						+ "[Driver_Branch], [Driver_Dept], [Contact_Address], [Driver_State], [Driver_Phone],"
						+ "[Driver_Fax], [Driver_email], [Driver_Status], [driver_province], [User_id],"
						+ "[Create_date] FROM [am_ad_driver] ";				   

        query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
           c = getConnection();
            s =c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String driverId = rs.getString("Driver_ID");
                String code = rs.getString("Driver_code");
				String license = rs.getString("Driver_License");
                String issueD = sdf.format(rs.getDate("dl_issue_date"));
                String expD = sdf.format(rs.getDate("dl_expiry_date"));
                String lastN = rs.getString("Driver_LastName");
                String firstN = rs.getString("Driver_FirstName");
				String otherN = rs.getString("Driver_OtherName");
                String branch = rs.getString("Driver_Branch");
                String dept = rs.getString("Driver_Dept");
				String address = rs.getString("Contact_Address");
				String state = rs.getString("Driver_State");
				String phone = rs.getString("Driver_Phone");
				String fax = rs.getString("Driver_Fax");
				String email = rs.getString("Driver_email");
				String status = rs.getString("Driver_Status");
				String province = rs.getString("driver_province");
				String userId = rs.getString("User_id");
				String createD = sdf.format(rs.getDate("Create_date"));
				
                driver = new legend.admin.objects.Driver();
                driver.setDriverId(driverId);
                driver.setDriverCode(code);
                driver.setDriverLicense(license);
				driver.setDlIssueDate(issueD);
                driver.setDlExpiryDate(expD);
                driver.setDriverLastName(lastN);
                driver.setDriverFirstname(firstN);
                driver.setDriverOtherName(otherN);
                driver.setDriverBranch(branch);
				driver.setDriverDept(dept);
				driver.setContatcAddress(address);
				driver.setDriverState(state);
				driver.setDriverPhone(phone);
				driver.setDriverFax(fax);
				driver.setDriverEmail(email);
				driver.setDriverStatus(status);
				driver.setDriverProvince(province);
				driver.setUserId(userId);
				driver.setCreateDate(createD);
                _list.add(driver);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }

    public legend.admin.objects.Driver getDriverByDriverID(String driverid) {
       
        legend.admin.objects.Driver driver = null;
        String query = "SELECT [Driver_ID], [Driver_Code], [Driver_License], [dl_issue_date]," 
                		+ "[dl_expiry_date], [Driver_LastName], [Driver_FirstName], [Driver_OtherName],"
						+ "[Driver_Branch], [Driver_Dept], [Contact_Address], [Driver_State], [Driver_Phone],"
						+ "[Driver_Fax], [Driver_email], [Driver_Status], [driver_province], [User_id],"
						+ "[Create_date] FROM [am_ad_driver] WHERE [Driver_ID]="+driverid;				   

      
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
           c = getConnection();
            s =c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String driverId = rs.getString("Driver_ID");
                String code = rs.getString("Driver_code");
				String license = rs.getString("Driver_License");
                String issueD = sdf.format(rs.getDate("dl_issue_date"));
                String expD = sdf.format(rs.getDate("dl_expiry_date"));
                String lastN = rs.getString("Driver_LastName");
                String firstN = rs.getString("Driver_FirstName");
				String otherN = rs.getString("Driver_OtherName");
                String branch = rs.getString("Driver_Branch");
                String dept = rs.getString("Driver_Dept");
				String address = rs.getString("Contact_Address");
				String state = rs.getString("Driver_State");
				String phone = rs.getString("Driver_Phone");
				String fax = rs.getString("Driver_Fax");
				String email = rs.getString("Driver_email");
				String status = rs.getString("Driver_Status");
				String province = rs.getString("driver_province");
				String userId = rs.getString("User_id");
				String createD = sdf.format(rs.getDate("Create_date"));
				
                driver = new legend.admin.objects.Driver();
                driver.setDriverId(driverId);
                driver.setDriverCode(code);
                driver.setDriverLicense(license);
				driver.setDlIssueDate(issueD);
                driver.setDlExpiryDate(expD);
                driver.setDriverLastName(lastN);
                driver.setDriverFirstname(firstN);
                driver.setDriverOtherName(otherN);
                driver.setDriverBranch(branch);
				driver.setDriverDept(dept);
				driver.setContatcAddress(address);
				driver.setDriverState(state);
				driver.setDriverPhone(phone);
				driver.setDriverFax(fax);
				driver.setDriverEmail(email);
				driver.setDriverStatus(status);
				driver.setDriverProvince(province);
				driver.setUserId(userId);
				driver.setCreateDate(createD);
               
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(c, s, rs);
        }
        return driver;

    }

    public legend.admin.objects.Driver getDriverByDriverCode(String driverid) {
        
        legend.admin.objects.Driver driver = null;
        String query = "SELECT [Driver_ID], [Driver_Code], [Driver_License], [dl_issue_date]," 
                		+ "[dl_expiry_date], [Driver_LastName], [Driver_FirstName], [Driver_OtherName],"
						+ "[Driver_Branch], [Driver_Dept], [Contact_Address], [Driver_State], [Driver_Phone],"
						+ "[Driver_Fax], [Driver_email], [Driver_Status], [driver_province], [User_id],"
						+ "[Create_date] FROM [am_ad_driver] WHERE [Driver_Code]='"+driverid+"'";				   

      
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
           c = getConnection();
            s =c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String driverId = rs.getString("Driver_ID");
                String code = rs.getString("Driver_code");
				String license = rs.getString("Driver_License");
                String issueD = sdf.format(rs.getDate("dl_issue_date"));
                String expD = sdf.format(rs.getDate("dl_expiry_date"));
                String lastN = rs.getString("Driver_LastName");
                String firstN = rs.getString("Driver_FirstName");
				String otherN = rs.getString("Driver_OtherName");
                String branch = rs.getString("Driver_Branch");
                String dept = rs.getString("Driver_Dept");
				String address = rs.getString("Contact_Address");
				String state = rs.getString("Driver_State");
				String phone = rs.getString("Driver_Phone");
				String fax = rs.getString("Driver_Fax");
				String email = rs.getString("Driver_email");
				String status = rs.getString("Driver_Status");
				String province = rs.getString("driver_province");
				String userId = rs.getString("User_id");
				String createD = sdf.format(rs.getDate("Create_date"));
				
                driver = new legend.admin.objects.Driver();
                driver.setDriverId(driverId);
                driver.setDriverCode(code);
                driver.setDriverLicense(license);
				driver.setDlIssueDate(issueD);
                driver.setDlExpiryDate(expD);
                driver.setDriverLastName(lastN);
                driver.setDriverFirstname(firstN);
                driver.setDriverOtherName(otherN);
                driver.setDriverBranch(branch);
				driver.setDriverDept(dept);
				driver.setContatcAddress(address);
				driver.setDriverState(state);
				driver.setDriverPhone(phone);
				driver.setDriverFax(fax);
				driver.setDriverEmail(email);
				driver.setDriverStatus(status);
				driver.setDriverProvince(province);
				driver.setUserId(userId);
				driver.setCreateDate(createD);
               
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(c, s, rs);
        }
        return driver;

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
	

    /**
     * createCurrency
     */
    public boolean createDriver(legend.admin.objects.Driver ccode) {

        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;
        String query =
                "INSERT INTO [am_ad_driver]([Driver_Code], [Driver_License], [dl_issue_date]," 
                		+ "[dl_expiry_date], [Driver_LastName], [Driver_FirstName], [Driver_OtherName],"
						+ "[Driver_Branch], [Driver_Dept], [Contact_Address], [Driver_State], [Driver_Phone],"
						+ "[Driver_Fax], [Driver_email], [Driver_Status], [driver_province], [User_id],"
						+ "[Create_date],[Driver_Code],[Driver_ID]) "		
						+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
           ps.setString(1, ccode.getDriverCode());
            ps.setString(2, ccode.getDriverLicense());
            ps.setDate(3, df.dateConvert(ccode.getDlIssueDate()));
            ps.setDate(4, df.dateConvert(ccode.getDlExpiryDate()));
            ps.setString(5, ccode.getDriverLastName());
            ps.setString(6, ccode.getDriverFirstname());
            ps.setString(7, ccode.getDriverOtherName());
			ps.setString(8, ccode.getDriverBranch());
			ps.setString(9, ccode.getDriverDept());
			ps.setString(10, ccode.getContatcAddress());
			ps.setString(11, ccode.getDriverState());
			ps.setString(12, ccode.getDriverPhone());
	        ps.setString(13, ccode.getDriverFax());		
			ps.setString(14, ccode.getDriverEmail());
			ps.setString(15, ccode.getDriverStatus());
			ps.setString(16, ccode.getDriverProvince());
			ps.setString(17, ccode.getUserId());
			ps.setDate(19, df.dateConvert(new java.util.Date()));
			ps.setLong(20, System.currentTimeMillis());
			
            done = ps.execute();

        } catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }

    public boolean updateDriver(legend.admin.objects.Driver ccode) {

        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;
        String query = "UPDATE [am_ad_driver]"
                       + " SET [Driver_Code] = ?,[Driver_License],[dl_issue_date] = ?"
                       + ",[dl_expiry_date] = ?,[Driver_LastName] =?,[Driver_FirstName] =?,[Driver_OtherName] = ?"
                       + ",[Driver_Branch] =?, [Driver_Dept] = ?, [Contact_Address] = ?, [Driver_State] = ?"
                       + ",[Driver_Phone] = ?,[Driver_Fax] = ?, [Driver_email] = ?, [Driver_Status] = ?"
					   + ",[driver_province] = ?"
                       + " WHERE [Driver_ID] = ?";

        try {
            con = getConnection();
            ps.setString(1, ccode.getDriverCode());
            ps.setString(2, ccode.getDriverLicense());
            ps.setDate(3, df.dateConvert(ccode.getDlIssueDate()));
            ps.setDate(4, df.dateConvert(ccode.getDlExpiryDate()));
            ps.setString(5, ccode.getDriverLastName());
            ps.setString(6, ccode.getDriverFirstname());
            ps.setString(7, ccode.getDriverOtherName());
			ps.setString(8, ccode.getDriverBranch());
			ps.setString(9, ccode.getDriverDept());
			ps.setString(10, ccode.getContatcAddress());
			ps.setString(11, ccode.getDriverState());
			ps.setString(12, ccode.getDriverPhone());
	        ps.setString(13, ccode.getDriverFax());		
			ps.setString(14, ccode.getDriverEmail());
			ps.setString(15, ccode.getDriverStatus());
			ps.setString(16, ccode.getDriverProvince());
			ps.setString(17, ccode.getDriverId());
            done = ps.execute();

        } catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }

    
}
