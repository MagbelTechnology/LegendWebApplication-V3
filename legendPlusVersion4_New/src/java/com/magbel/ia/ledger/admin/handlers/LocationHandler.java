package legend.admin.handlers;


import java.sql.*;
import java.text.SimpleDateFormat;
import  legend.admin.objects.Locations;
import com.magbel.util.DataConnect;





public class LocationHandler {
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
	
    public LocationHandler() {

        sdf = new SimpleDateFormat("dd-MM-yyyy");
        df = new com.magbel.util.DatetimeFormat();
        System.out.println("USING_ " + this.getClass().getName());
    }

    public java.util.List getAllLocations() {
        java.util.List _list = new java.util.ArrayList();
        legend.admin.objects.Locations  location = null;
        String query = "SELECT Location_Id, Location_Code, Location"
                       + ", Status, User_Id, Create_Date"
                      + " FROM [AM_GB_LOCATION]";

        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String locationId = rs.getString("Location_Id");
                String locationCode = rs.getString("Location_Code");                
				String locate = rs.getString("Location");
				String status = rs.getString("Status");
				String userId = rs.getString("User_Id");
                String createDate = rs.getString("Create_Date");
				
                location = new legend.admin.objects.Locations();
                location.setLocationId(locationId);
                location.setLocationCode(locationCode);
                location.setLocation(locate);
                location.setUserId(userId);
				location.setCreateDate(createDate);
				location.setStatus(status);
				_list.add(location);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }

    public java.util.ArrayList getLocationByQuery(String filter) {
        java.util.ArrayList _list = new java.util.ArrayList();
        legend.admin.objects.Locations  location = null;
		
        String query = "SELECT Location_Id, Location_Code, Location"
                       + ", Status, User_Id, Create_Date"
                      + " FROM [AM_GB_LOCATION] WHERE Location_Code IS NOT NULL";

        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String locationId = rs.getString("Location_Id");
                String locationCode = rs.getString("Location_Code");                
				String locate = rs.getString("Location");
				String status = rs.getString("Status");
				String userId = rs.getString("User_Id");
                String createDate = rs.getString("Create_Date");
				
                location = new legend.admin.objects.Locations();
                location.setLocationId(locationId);
                location.setLocationCode(locationCode);
                location.setLocation(locate);
                location.setUserId(userId);
				location.setCreateDate(createDate);
				location.setStatus(status);
				_list.add(location);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }

    public legend.admin.objects.Locations getLocationByLocID(String LocID) {
        legend.admin.objects.Locations  location = null;
        String query = "SELECT Location_Id, Location_Code, Location"
                       + ", Status, User_Id, Create_Date"
                      + " FROM [AM_GB_LOCATION] WHERE Location_Id = '" + LocID+"'";

        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
           c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String locationId = rs.getString("Location_Id");
                String locationCode = rs.getString("Location_Code");                
				String locate = rs.getString("Location");
				String status = rs.getString("Status");
				String userId = rs.getString("User_Id");
                String createDate = rs.getString("Create_Date");
				
                location = new legend.admin.objects.Locations();
                location.setLocationId(locationId);
                location.setLocationCode(locationCode);
                location.setLocation(locate);
                location.setUserId(userId);
				location.setCreateDate(createDate);
				
				
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(c, s, rs);
        }
        return location;
    }
	
	

    public boolean isUniqueCode(String LocCode) {
	  
	   boolean unique = false;
        
        String query = "SELECT Location_Code"
                        + " FROM [AM_GB_LOCATION] WHERE Location_Code = '" +LocCode+"'";

        Connection c = null;
        ResultSet rs = null;
        Statement s = null;
		//PreparedStatement s = null;

        try {
            con = getConnection();
            s = con.createStatement();
            rs = s.executeQuery(query);
			int x = 0;
            while (rs.next()) {
			    x++;
                String locationCode = rs.getString("Location_Code");
                }
            if (x == 1) unique = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(c, s, rs);
        }
        return unique;

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
    public boolean createLocation(legend.admin.objects.Locations  location)
	{

        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;
        String query =
                "INSERT INTO [AM_GB_LOCATION](Location_Id, Location_Code, Location"
                       + ", Status, User_Id, Create_Date)"
                         + " VALUES (?,?,?,?,?, getDate())";

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setLong(1, System.currentTimeMillis());
            ps.setString(2, location.getLocationCode());
            ps.setString(3, location.getLocation());
            ps.setString(4, location.getStatus());
            ps.setString(5, location.getUserId());
           
			
            done = ps.execute();

        } catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }

    public boolean updateLocation(legend.admin.objects.Locations location) {

        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;
        String query = "UPDATE [AM_GB_LOCATION]"
                       + " SET [Location_Id] = ?"
                       + ",[Location] = ?,[Status] = ?"
                      + " WHERE Location_Code = ?";

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
           ps.setString(1, location.getLocationId());
            ps.setString(2, location.getLocation());
            ps.setString(3, location.getStatus());
           
			ps.setString(4, location.getLocationCode());
            done = ps.execute();

        } catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }


  public legend.admin.objects.Locations getLocationByLocCode(String LocCode) {
        
        legend.admin.objects.Locations location = null;
        String query = "SELECT Location_Id, Location_Code, Location"
                       + ", Status, User_Id, Create_Date"
                       + " FROM [AM_GB_LOCATION] WHERE Location_Code = '" +LocCode+"'";

        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String locationId = rs.getString("Location_Id");
                String locationCode = rs.getString("Location_Code");                
				String locate = rs.getString("Location");
				String locationStatus = rs.getString("Status");
				String userId = rs.getString("User_Id");
                String createDate = rs.getString("Create_Date");
				
                location = new legend.admin.objects.Locations();
                location.setLocationId(locationId);
                location.setLocationCode(locationCode);
                location.setLocation(locate);
                location.setStatus(locationStatus);
                location.setUserId(userId);
                location.setCreateDate(createDate);
                		
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(c, s, rs);
        }
        return location;

    }	
	
	
	
	

	 public boolean exist(String LocCode) {
        
        boolean itexist = false;
        String query = "SELECT  Location_Code, "
                       + " FROM [AM_GB_LOCATION] WHERE Location_Code = '" +LocCode+"'";

        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            con = getConnection();
            s = con.createStatement();
            rs = s.executeQuery(query);
			
            if( rs == null)
				{ itexist = false;	} 
			else 
				{ itexist = true;	}
        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(c, s, rs);
        }
        return itexist;

    }	
	
	
}
