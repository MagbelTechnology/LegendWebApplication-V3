package legend;

import java.sql.*;
import magma.net.dao.MagmaDBConnection;

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
public class LocationSetupBean extends ConnectionClass {
  private String[] locations = new String[5];
  private String[] byLocationCode = new String[5];

  public LocationSetupBean() throws Throwable {
  }

  public void setLocations(String[] locations) {
     if(locations != null){
       this.locations = locations;
     }
  }

  public String[] getLocations() {
    return locations;
  }

    public void setByLocations(String[] byLocationCode) {
       if(byLocationCode != null){
         this.byLocationCode = byLocationCode;
       }
    }

    public String[] getByLocations() {
      return byLocationCode;
  }

    /**
     * selectLocations
     *
     * @param con String
     * @param sta String
     * @return String[][]
     * @throws Throwable
     */
    public String[][] selectLocations(String con, String sta) throws Throwable {
    StringBuffer cq = new StringBuffer(100);

	String statusQuery = "SELECT COUNT(*) FROM AM_gb_location WHERE STATUS = '"+sta+"'";
	String idQuery = "SELECT COUNT(*) FROM AM_gb_location WHERE LOCATION_ID = "+con;

	String selQuerySatus = "SELECT * FROM AM_gb_location WHERE STATUS = '"+sta+"' "+
							"ORDER BY LOCATION ASC";

	String selQueryId = "SELECT * FROM AM_GB_LOCATION WHERE LOCATION_ID = "+con;


	   ResultSet rc = null;
	   if(con.equals("0")){
		rc = getStatement().executeQuery(statusQuery);
	}else if(con != ""){
		rc = getStatement().executeQuery(idQuery);
	}else{}


	   ResultSet rv = null;
	   if(con.equals("0")){
			rv = getStatement().executeQuery(selQuerySatus);
		}else if(con != ""){
			rv = getStatement().executeQuery(selQueryId);
	}else{}

	   rc.next();
	String[][] values = new String[rc.getInt(1)][5];


   for (int x = 0; x < values.length; x++) {
	  rv.next();
	  for (int y = 0; y < 5; y++) {
		values[x][y] = rv.getString(y + 1);
	  }
	}

	return values;
  }

    /*
    cq.append("am_msp_count_locations"
              +" '"+con+"','"+sta+"'");
    ResultSet rc = getStatement().executeQuery(
      cq.toString());

    StringBuffer sq = new StringBuffer(100);
    sq.append("am_msp_select_locations"
              +" '"+con+"','"+sta+"'");
    ResultSet rv = getStatement().executeQuery(
      sq.toString());

    rc.next();
    String[][] values = new String[rc.getInt(1)][5];

    for (int x = 0; x < values.length; x++) {
      rv.next();
      for (int y = 0; y < 5; y++) {
        values[x][y] = rv.getString(y + 1);
      }
    }

    return values;
  }
*/
  /**
   * updateLocations
   *
   * @param con String
   * @return boolean
   * @throws Throwable
   */
  public boolean updateLocations(String con) throws Throwable {
    StringBuffer iq = new StringBuffer(100);
    String str = "UPDATE AM_GB_LOCATION SET LOCATION_CODE = '"+locations[0]+"',"+
				"LOCATION = '"+locations[1]+"', STATUS = '"+locations[2]+"' WHERE LOCATION_ID = "+con;


    boolean done = (getStatement().executeUpdate(str) == -1);
    return true;
  }


  public String[][] selectLocationCode(String locationCode) throws Throwable {

  		Connection cnn = null;
  		PreparedStatement ps = null;
  		ResultSet rs = null;
  		System.out.print("Action performed");

  		MagmaDBConnection dbConn = new MagmaDBConnection();

             String count = "";
             String select = "";
             count = "SELECT COUNT(*) FROM AM_GB_LOCATION "+
  						"WHERE LOCATION_CODE = ?";

             select = "SELECT * FROM AM_GB_LOCATION WHERE "+
  						"LOCATION_CODE = ?  ";

  						String[][] values = new String[500][5];
                    int j = 0;

  		 try {
                    cnn = dbConn.getConnection("fixedasset");

  			ps = cnn.prepareStatement(count);
  			ps.setString(1,locationCode);
  			rs = ps.executeQuery();
              while(rs.next()){j = rs.getInt(1);}

  			ps = cnn.prepareStatement(select);
  			ps.setString(1,locationCode);

  			rs = ps.executeQuery();


  			values = new String[j][5];

  			for (int x = 0; x < values.length; x++) {
  			rs.next();
  				for (int y = 0; y < 5; y++) {
  				values[x][y] = rs.getString(y + 1);
  				}
  			}


          }
  		catch (Exception e) {
           System.out.println("INFO:Error fetching location Code->" +e.getMessage());
          }
  		finally {
           dbConn.closeConnection(cnn, ps,rs);
  		}
  		return values;
      }

     public boolean isUniqueCode(String locationCode) throws Throwable {
  	 String query = "SELECT LOCATION_CODE FROM AM_GB_LOCATION "+
  							"WHERE LOCATION_CODE = ? ";
  		Connection cnn = null;
  		PreparedStatement ps = null;
  		ResultSet rs = null;
  		boolean confirm = false;

  		MagmaDBConnection dbConn = new MagmaDBConnection();

  	try {
  		cnn = dbConn.getConnection("fixedasset");
  		ps = cnn.prepareStatement(query);


  			ps.setString(1,locationCode);

  			rs = ps.executeQuery();
  			while (rs.next()){
  				confirm = true;
  			}

  		}
  		catch (Exception e) {
           System.out.println("INFO:Error checking for duplicate code->" +e.getMessage());

          }
  		finally {
              dbConn.closeConnection(cnn, ps,rs);
         }

  		  return confirm;
	}

  /**
   * insertLocations
   *
   * @return boolean
   * @throws Throwable
   */
  public boolean insertLocations() throws Throwable {
    StringBuffer iq = new StringBuffer(100);
    String str = "insert into AM_GB_LOCATION("+
			"location_code,location,status,[user_id],create_date) values(";

    iq.append(str);
    iq.append("'");
    iq.append(locations[0]);
    iq.append("'");

    for (int i = 1; i <= 3; i++) {
      switch (i) {
        case -1:
          iq.append(", ");
          iq.append(locations[i]);
          break;
        default:
          iq.append(",'");
          iq.append(locations[i]);
          iq.append("'");
      }
    }
    iq.append(",getDate())");

	boolean done = (getStatement().executeUpdate(iq.toString()) == -1 );
    System.out.println("INFO:Return variable is "+done);
    return true;
  }

}
