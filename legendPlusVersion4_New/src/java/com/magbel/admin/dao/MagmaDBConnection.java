package com.magbel.admin.dao;

import javax.naming.*;
import javax.sql.DataSource;
import java.sql.*;
import java.text.SimpleDateFormat;

import com.magbel.util.DataConnect;
import com.magbel.util.DatetimeFormat;

/**
 * <p>Title: MagmaDBConnection.java</p>
 *
 * <p>Description: File Description</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Magbel Technologies LTD</p>
 *
 * @author Jejelowo.B.Festus
 * @version 1.0
 */
public class MagmaDBConnection implements eConnection {

    private String jndiName;
    private DatetimeFormat dateFormat;
    private SimpleDateFormat sdf;

    public MagmaDBConnection() {
        dateFormat = new DatetimeFormat();
        sdf = new SimpleDateFormat("dd-MM-yyyy");
    }

    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }

    public String getJndiName() {
        return jndiName;
    }


    /**
     * closeConnection
     *
     * @param con Connection
     * @param ps PreparedStatement
     * @todo Implement this magma.net.dao.eConnection method
     */
    public void closeConnection(Connection con, PreparedStatement ps) {
        try {
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (Exception e) {
            System.out.println("WARNING:Error closing Connection ->" +
                               e.getMessage());
        }

    }
    public void closeConnection(Connection con, Statement ps) {
           try {
               if (ps != null) {
                   ps.close();
               }
               if (con != null) {
                   con.close();
               }
           } catch (Exception e) {
               System.out.println("WARNING:Error closing Connection ->" +
                                  e.getMessage());
           }

    }


    /**
     * closeConnection
     *
     * @param con Connection
     * @param ps PreparedStatement
     * @param rs ResultSet
     * @todo Implement this magma.net.dao.eConnection method
     */
    public void closeConnection(Connection con, PreparedStatement ps,
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
            System.out.println("WARNING:Error closing Connection ->" +
                               e.getMessage());
        }

    }
    public void closeConnection(Connection con, Statement ps,
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
               System.out.println("WARNING:Error closing Connection ->" +
                                  e.getMessage());
           }

       }

    /**
     * getConnection
     *
     * @return Connection
     * @todo Implement this magma.net.dao.eConnection method
     */
/* SQL Connection    
    public Connection getConnection(String jndi) {
        Connection con = null;
        try {
            Context initContext = new InitialContext();
            DataSource ds = (DataSource) initContext.lookup("java:comp/env/jdbc/" + jndi);
            con = ds.getConnection();

        } catch (Exception e) {
            System.out.println("WARNING:Error closing Connection ->" +
                               e.getMessage());
        }

        return con;
    }
*/     
    public Connection getConnection(String jndi) {
        Connection con = null;
        try {
         //   Context initContext = new InitialContext();
         //   DataSource ds = (DataSource) initContext.lookup("java:comp/env/jdbc/" + jndi);
            con = new DataConnect(jndi).getConnection();
       // System.out.println("===I am Out of DataConnect  ===");
        } catch (Exception e) {
            System.out.println("WARNING:Error getting Connection ->" +
                               e.getMessage());
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

	public static void main(String[] args){
		MagmaDBConnection m = new MagmaDBConnection();
                System.out.println("Todays Date >>>> " + m.getDateTime(new java.util.Date()));
    }

}
