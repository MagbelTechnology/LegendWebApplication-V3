package legend.objects;
import java.sql.*;
import com.magbel.util.DataConnect;
import com.magbel.util.DatetimeFormat;
import java.text.SimpleDateFormat;

/**
 * <p>Title: PersistenceServiceDAO.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: MagBel Technology LTD.</p>
 *
 * @author Jejelowo B. Festus
 * @version 1.0
 */
public class PersistenceServiceDAO implements ConnectionDAO{

    DatetimeFormat dateFormat;
    SimpleDateFormat sdf;

    public PersistenceServiceDAO() {

        dateFormat = new DatetimeFormat();
        sdf = new SimpleDateFormat("dd-MM-yyyy");
    }

    public void executeQuery(String query) {

        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = this.getConnection("tabs");
            ps = con.prepareStatement(query);
            ps.execute();
        } catch (Exception error) {
			System.out.println("WARN:com.magbel.ia.dao.PersistenceService:"+error);
        } finally {
            closeConnection(con, ps);
        }
    }

    public void executeQueryString(String query,String jndiName) {

	        PreparedStatement ps = null;
	        Connection con = null;
	        try {
	            con = this.getConnection(jndiName);
	            ps = con.prepareStatement(query);
	            ps.execute();
	        } catch (Exception error) {
				System.out.println("WARN:com.magbel.ia.dao.PersistenceService:"+error);

	        } finally {
	            closeConnection(con, ps);
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

    /**
     * closeConnection
     *
     * @param con Connection
     * @param rs ResultSet
     * @param ps PreparedStatement
     * @todo Implement this com.magbel.ia.dao.ConnectionDAO method
     */
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

    public Connection getConnection() {
	        Connection con = null;
	        try {
	            con = new DataConnect("tabs").getConnection();
	        } catch (Exception conError) {
	            System.out.println("WARNING:Error getting connection - >" +
	                               conError);
	        }
	        return con;
    }


    public Connection getConnection(String jndiName) {
        Connection con = null;
        try {
            con = new DataConnect(jndiName).getConnection();
        } catch (Exception conError) {
            System.out.println("WARNING:Error getting connection - >" +
                               conError);
        }
        return con;
    }

    public java.sql.Date dateConvert(String strDate) {
       
      java.text.SimpleDateFormat formata = new java.text.SimpleDateFormat(
              "dd-MM-yyyy");
      if (strDate == null) {
          strDate = formata.format(new java.util.Date());
      }
      strDate = strDate.replaceAll("/", "-");

      java.util.Date inputDate = null;
      try {
          inputDate = formata.parse(strDate);
      } catch (Exception e) {
          System.out.println("WARNING: Error formating Date:" + e.getMessage());
      }
      return dateFormat.getSQLFormatedDate(inputDate);

  }

  public java.sql.Date dateConvert(java.util.Date inputDate) {
      return dateFormat.getSQLFormatedDate(inputDate);
  }

  public java.sql.Date dateConvert(long longDate) {

      java.util.Date inputDate = new java.util.Date();
      inputDate.setTime(longDate);

      return dateFormat.getSQLFormatedDate(inputDate);

  }

  public String formatDate(java.util.Date date) {
      String formated = "";

      if (date == null) {
          date = new java.util.Date();
      }
      try {
          sdf = new SimpleDateFormat("dd-MM-yyyy");
          formated = sdf.format(date);
      } catch (Exception e) {
          System.out.println("WARNING:Error formating Date ->" + e.getMessage());
      }

      return formated;
  }
  
  public java.sql.Timestamp getDateTime(String inputDate){

  		java.sql.Timestamp inputTime = null;

  		try{

  			if(inputDate == null){inputDate = sdf.format(new java.util.Date());}
  			inputDate = inputDate.replaceAll("/","-");

  			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss.SSS");
  			String strDate = dateFormat.format(new java.util.Date());
  			String transInputDate = inputDate + strDate.substring(10,strDate.length());

  			inputTime = new java.sql.Timestamp((dateFormat.parse(transInputDate)).getTime());

  		}catch(Exception er){
  			System.out.println("WARN : Error getting datetime ->"+er);
  		}

  			return inputTime;
  		}

  		public java.sql.Timestamp getDateTime(java.util.Date inputDate){

  			String strDate = null;
  			try{
  			if(inputDate == null){
  				strDate = sdf.format(new java.util.Date());
  				}else{
  					strDate =  sdf.format(inputDate);
  				}
  			}catch(Exception er){
  				System.out.println("WARN : Error getting datetime ->"+er);
  			}

  			return getDateTime(strDate);
  	}


}
