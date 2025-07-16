package legend.admin.handlers;


import java.sql.*;
import java.text.SimpleDateFormat;

import com.magbel.util.DataConnect;

public class FuelTypeHandler {
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
    public FuelTypeHandler() {

        sdf = new SimpleDateFormat("dd-MM-yyyy");
        df = new com.magbel.util.DatetimeFormat();
        System.out.println("USING_ " + this.getClass().getName());
    }

    public java.util.ArrayList getAllFuelTypes() {
        java.util.ArrayList _list = new java.util.ArrayList();
        legend.admin.objects.FuelType fueltype = null;
        String query = "SELECT Fuel_ID,Fuel_Code,Fuel_Desc"
                       + ",Fuel_Volume,fuel_Price"
                       + ",account_type ,suspense_account,Fuel_Status"
                       + ",User_ID ,Create_Date"
                       + " FROM am_ad_fuelType";

        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c= getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String fuelId = rs.getString("Fuel_ID");
                String fuelCode = rs.getString("Fuel_Code");
                String description = rs.getString("Fuel_Desc");
                String volume = rs.getString("Fuel_Volume");
                String fuelPrice = rs.getString("fuel_Price");
                String acccountTpye = rs.getString("account_type");
                String suspenseAccount = rs.getString("suspense_account");
                String fuelStatus = rs.getString("Fuel_Status");
                String userId = rs.getString("User_ID");
                String createDate = sdf.format(rs.getDate("Create_Date"));
                fueltype = new legend.admin.objects.FuelType();
                fueltype.setFuelId(fuelId);
				fueltype.setFuelCode(fuelCode);
				fueltype.setDescription(description);
				fueltype.setVolume(volume);
				fueltype.setFuelPrice(fuelPrice);
				fueltype.setAcccountTpye(acccountTpye);
				fueltype.setSuspenseAccount(suspenseAccount);
				fueltype.setFuelStatus(fuelStatus);
				fueltype.setUserId(userId);
				fueltype.setCreateDate(createDate);
                _list.add(fueltype);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }

    public java.util.ArrayList getFuelTypeByQuery(String filter) {
        java.util.ArrayList _list = new java.util.ArrayList();
        legend.admin.objects.FuelType fueltype = null;
        String query = "SELECT Fuel_ID,Fuel_Code,Fuel_Desc"
                       + ",Fuel_Volume,fuel_Price"
                       + ",account_type ,suspense_account,Fuel_Status"
                       + ",User_ID ,Create_Date"
                       + " FROM am_ad_fuelType WHERE Fuel_ID IS NOT NULL ";


        query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String fuelId = rs.getString("Fuel_ID");
                String fuelCode = rs.getString("Fuel_Code");
                String description = rs.getString("Fuel_Desc");
                String volume = rs.getString("Fuel_Volume");
                String fuelPrice = rs.getString("fuel_Price");
                String acccountTpye = rs.getString("account_type");
                String suspenseAccount = rs.getString("suspense_account");
                String fuelStatus = rs.getString("Fuel_Status");
                String userId = rs.getString("User_ID");
                String createDate = sdf.format(rs.getDate("Create_Date"));
                fueltype = new legend.admin.objects.FuelType();
                fueltype.setFuelId(fuelId);
				fueltype.setFuelCode(fuelCode);
				fueltype.setDescription(description);
				fueltype.setVolume(volume);
				fueltype.setFuelPrice(fuelPrice);
				fueltype.setAcccountTpye(acccountTpye);
				fueltype.setSuspenseAccount(suspenseAccount);
				fueltype.setFuelStatus(fuelStatus);
				fueltype.setUserId(userId);
				fueltype.setCreateDate(createDate);
                _list.add(fueltype);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }


    public legend.admin.objects.FuelType getFuelTypeByFuelID(String fuelid) {
        legend.admin.objects.FuelType fueltype = null;
        String query = "SELECT Fuel_ID,Fuel_Code,Fuel_Desc"
                       + ",Fuel_Volume,fuel_Price"
                       + ",account_type ,suspense_account,Fuel_Status"
                       + ",User_ID ,Create_Date"
                       + " FROM am_ad_fuelType WHERE Fuel_ID=" + fuelid;

        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String fuelId = rs.getString("Fuel_ID");
                String fuelCode = rs.getString("Fuel_Code");
                String description = rs.getString("Fuel_Desc");
                String volume = rs.getString("Fuel_Volume");
                String fuelPrice = rs.getString("fuel_Price");
                String acccountTpye = rs.getString("account_type");
                String suspenseAccount = rs.getString("suspense_account");
                String fuelStatus = rs.getString("Fuel_Status");
                String userId = rs.getString("User_ID");
                String createDate = sdf.format(rs.getDate("Create_Date"));
                fueltype = new legend.admin.objects.FuelType();
                fueltype.setFuelId(fuelId);
				fueltype.setFuelCode(fuelCode);
				fueltype.setDescription(description);
				fueltype.setVolume(volume);
				fueltype.setFuelPrice(fuelPrice);
				fueltype.setAcccountTpye(acccountTpye);
				fueltype.setSuspenseAccount(suspenseAccount);
				fueltype.setFuelStatus(fuelStatus);
				fueltype.setUserId(userId);
				fueltype.setCreateDate(createDate);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(c, s, rs);
        }
        return fueltype;

    }


    public legend.admin.objects.FuelType getFuelTypeByFuelCode(String fuelcode) {
        
        legend.admin.objects.FuelType fueltype = null;
        String query = "SELECT Fuel_ID,Fuel_Code,Fuel_Desc"
                       + ",Fuel_Volume,fuel_Price"
                       + ",account_type ,suspense_account,Fuel_Status"
                       + ",User_ID ,Create_Date"
                       + " FROM am_ad_fuelType WHERE Fuel_Code='" + fuelcode+"'";


        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String fuelId = rs.getString("Fuel_ID");
                String fuelCode = rs.getString("Fuel_Code");
                String description = rs.getString("Fuel_Desc");
                String volume = rs.getString("Fuel_Volume");
                String fuelPrice = rs.getString("fuel_Price");
                String acccountTpye = rs.getString("account_type");
                String suspenseAccount = rs.getString("suspense_account");
                String fuelStatus = rs.getString("Fuel_Status");
                String userId = rs.getString("User_ID");
                String createDate = sdf.format(rs.getDate("Create_Date"));
                fueltype = new legend.admin.objects.FuelType();
                fueltype.setFuelId(fuelId);
				fueltype.setFuelCode(fuelCode);
				fueltype.setDescription(description);
				fueltype.setVolume(volume);
				fueltype.setFuelPrice(fuelPrice);
				fueltype.setAcccountTpye(acccountTpye);
				fueltype.setSuspenseAccount(suspenseAccount);
				fueltype.setFuelStatus(fuelStatus);
				fueltype.setUserId(userId);
				fueltype.setCreateDate(createDate);
             
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(c, s, rs);
        }
        return fueltype;

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


    public boolean createFuelType(legend.admin.objects.FuelType fueltype) {

        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;
        String query = "INSERT INTO am_ad_fuelType"
                       + "(Fuel_Code,Fuel_Desc"
                       + ",Fuel_Volume,fuel_Price"
                       + ",account_type ,suspense_account,Fuel_Status"
                       + ",User_ID ,Create_Date,Fuel_ID)"
                       + " VALUES (?,?,?,?,?,?,?,?,?,?)";

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, fueltype.getFuelCode());
            ps.setString(2, fueltype.getDescription());
            ps.setString(3, fueltype.getVolume());
            ps.setString(4, fueltype.getFuelPrice());
            ps.setString(5, fueltype.getAcccountTpye());
            ps.setString(6, fueltype.getSuspenseAccount());
            ps.setString(7, fueltype.getFuelStatus());
            ps.setString(8, fueltype.getUserId());
            ps.setDate(9, df.dateConvert(new java.util.Date()));
           ps.setLong(10, System.currentTimeMillis());
           done = (ps.executeUpdate()!=-1);

        } catch (Exception e) {
            System.out.println("WARNING:Error Creating Fuel Type ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }

    public boolean updateFuelType(legend.admin.objects.FuelType fueltype) {

        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;

        String query = "UPDATE am_ad_fuelType"
                       +" SET Fuel_Code = ?,Fuel_Desc = ?,"
                       +"  Fuel_Volume = ?,fuel_Price = ?,account_type = ?,"
                       +"  suspense_account = ?,Fuel_Status = ?"
                       +" WHERE Fuel_ID =?";

        try {
            con = getConnection();
            ps = con.prepareStatement(query);

           
            ps.setString(1, fueltype.getFuelCode());
            ps.setString(2, fueltype.getDescription());
            ps.setString(3, fueltype.getVolume());
            ps.setString(4, fueltype.getFuelPrice());
            ps.setString(5, fueltype.getAcccountTpye());
            ps.setString(6, fueltype.getSuspenseAccount());
            ps.setString(7, fueltype.getFuelStatus());
            ps.setString(8, fueltype.getFuelId());
            done = (ps.executeUpdate()!=-1);

        } catch (Exception e) {
            System.out.println("WARNING:Error Updating Fuel Types ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }


}
