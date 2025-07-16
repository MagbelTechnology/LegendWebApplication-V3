package magma;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.*;
import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import legend.ConnectionClass;
import magma.net.dao.MagmaDBConnection;

import com.magbel.util.NumberToWordConverter;
public class RemoteMethods extends RemoteScripting {
	private static MagmaDBConnection dbConnection;
	
    private static ConnectionClass conClass;
    

    public void log(String msg) {
        System.out.println("RemoteScripting: " + msg);
    }

    public static String getDepRate(String Category) {
    	dbConnection = new MagmaDBConnection();
    	Connection con = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
        String query = "select dep_rate from am_ad_category  " +
                       "where category_id='" + Category + "'";
        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
//            conClass = new ConnectionClass();
//            rs = con.getStatement().executeQuery(query);
//            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
            dbConnection.closeConnection(con, ps, rs);
        } catch (SQLException ex) {
        } catch (Exception ex) {
            System.out.println("WARN : RemoteMethod >> error getting depRate " +
                               ex);
        } finally {
 //           conClass.freeResource();
        	dbConnection.closeConnection(con, ps, rs);
        }
        return "";
    }

    public static String getResidualValue(String Category)
    {

    	dbConnection = new MagmaDBConnection();
    	Connection con = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
        String query = "select residual_value from am_ad_category " +
                       "where category_id=" + Category;

        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
//            conClass = new ConnectionClass();
//            rs = conClass.getStatement().executeQuery(query);
            if (rs.next()) {
                return rs.getString(1);
            }
            dbConnection.closeConnection(con, ps, rs);
        } catch (SQLException ex) {
        } catch (Exception ex) {
            System.out.println(
                    "WARN : RemoteMethods >> Could not getResidualValue " + ex);
        } finally {
 //           conClass.freeResource();
        	dbConnection.closeConnection(con, ps, rs);
        }
        return "";
    }

    public static String getVendorAC(String vendor) {

    	dbConnection = new MagmaDBConnection();
    	Connection con = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
        String query = "SELECT ACCOUNT_NUMBER FROM AM_AD_VENDOR " +
                       "WHERE VENDOR_ID='" + vendor + "'";
        //System.out.println("Magma RemoteScripting: " + vendor); 
        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
//            conClass = new ConnectionClass();
//            rs = conClass.getStatement().executeQuery(query);
            if (rs.next()) {
                return rs.getString(1);
            }
            dbConnection.closeConnection(con, ps, rs);
        } catch (SQLException ex) {

        } catch (Exception exr) {
            System.out.println("WAR: RemoteMethod >>Error getting Vendor Acc " +
                               exr);
        } finally {
 //           conClass.freeResource();
            dbConnection.closeConnection(con, ps, rs);
        }
        return "";

    }


    public static String addMonthToDate(String vals) {

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
                "dd-MM-yyyy");
        java.util.Calendar calendarDate = null;

        StringTokenizer st1 = new StringTokenizer(vals, ",");

        String date = st1.nextToken();
        String strMonth = st1.nextToken();

        int month = new Integer(strMonth).intValue();
        String added = null;
        if (date == null) {
            added = null;
        } else {

            try {
                date.replace("/", "-");
                Date dDate = sdf.parse(date);
                calendarDate = new java.util.GregorianCalendar();
                calendarDate.setTime(dDate);
                calendarDate.add(java.util.Calendar.MONTH, month);
                dDate = calendarDate.getTime();

                long toAdd = (long) (1 * 24 * 60 * 60 * 1000);
                dDate.setTime(dDate.getTime() - toAdd);
                added = sdf.format(dDate);

                added = sdf.format(dDate);
            } catch (Exception er) {
                System.out.println("WARN:Error adding date ->" + er);
            }

        }
        return added;
    }

    public static String getDepEndDate(String vals) {

        String endDate = "ERROR";

        if (vals != null) {

            StringTokenizer st1 = new StringTokenizer(vals, ",");
            if (st1.countTokens() == 2) {

                String s1 = st1.nextToken();
                String s2 = st1.nextToken();

                Float rate = Float.parseFloat(s1);
                if (s2 != null) {
                    s2 = s2.replaceAll("/", "-");
                }

                StringTokenizer st2 = new StringTokenizer(s2, "-");
                if (st2.countTokens() == 3) {
                    String day = st2.nextToken();
                    String month = st2.nextToken();
                    String year = st2.nextToken();

                    if ((year.length() == 4) && (day.length() > 0) &&
                        (day.length() < 3) && (month.length() > 0) &&
                        (month.length() < 3)) {
                        Calendar c = new GregorianCalendar(Integer.parseInt(
                                year),
                                Integer.parseInt(month) - 1,
                                Integer.parseInt(day) - 1);

                        int months = (int) (100 / rate * 12);
                        c.add(Calendar.MONTH, months);
                        // c.add(Calendar.DAY_OF_YEAR, -1*Integer.parseInt(day));

                        int endDay = c.get(Calendar.DAY_OF_MONTH);
                        int endMonth = c.get(Calendar.MONTH) + 1;
                        int endYear = c.get(Calendar.YEAR);

                        endDate = endDay + "-" + endMonth + "-" + endYear;
                    }

                }
            }
        }

        return endDate;
    }

    public static String getAssetID(String s) {
        StringTokenizer st = new StringTokenizer(s, ",");
        if (st.countTokens() == 4) {
            try {
                String id = new legend.AutoIDSetup().getIdentity(st.nextToken(),
                        st.nextToken(), st.nextToken(), st.nextToken());
                return id;
            } catch (Throwable ex) {
                return "error";
            }
        }
        return "";
    }
 public static String getEnforceBarCode(String Category)
    {

	 	dbConnection = new MagmaDBConnection();
	 	Connection con = null;
	 	PreparedStatement ps = null;
	 	ResultSet rs = null;
        String query = "select enforceBarcode from am_ad_category " +
                       "where category_id=" + Category;
//        System.out.println("query in RemoteMethods >>>>> " + query);
        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
//            conClass = new ConnectionClass();
//            rs = conClass.getStatement().executeQuery(query);
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException ex) {
        } catch (Exception ex) {
            System.out.println(
                    "WARN : RemoteMethods >> Could not getEnforceBarCode " + ex);
        } finally {
//            conClass.freeResource();
        	dbConnection.closeConnection(con, ps, rs);
        }
        return "";
    }
 
 public static String getsubcategory(String Category)
 {

	 	Connection con = null;
	 	PreparedStatement ps = null;
	 	ResultSet rs = null;
     String query = "select SUB_CATEGORY_CODE from am_ad_sub_category " +
                    "where SUB_CATEGORY_ID=" + Category;
//     System.out.println("query in RemoteMethods in getsubcategory >>>>> " + query);
     try {
//         conClass = new ConnectionClass();
//         rs = conClass.getStatement().executeQuery(query);
         con = dbConnection.getConnection("legendPlus");
         ps = con.prepareStatement(query);
         rs = ps.executeQuery();
         if (rs.next()) {
             return rs.getString(1);
         }
         dbConnection.closeConnection(con, ps, rs);
     } catch (SQLException ex) {
     } catch (Exception ex) {
         System.out.println(
                 "WARN : RemoteMethods >> Could not getsubcategory " + ex);
     } finally {
//         conClass.freeResource();
    	 dbConnection.closeConnection(con, ps, rs);
     }
     return "";
 }


 public static String getAmountInWords(String amount){

 //NumberToWordConverter numberToWordConverter = new NumberToWordConverter();
 return (NumberToWordConverter.wordValue(amount));
 }
}
