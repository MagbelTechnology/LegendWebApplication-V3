package legend.admin.handlers;


import java.sql.*;
import java.text.SimpleDateFormat;

import com.magbel.util.DataConnect;

public class InsuranceHandler {
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
    public InsuranceHandler() {

        sdf = new SimpleDateFormat("dd-MM-yyyy");
        df = new com.magbel.util.DatetimeFormat();
        System.out.println("USING_ " + this.getClass().getName());
    }

    public java.util.ArrayList getAllInsurance() {
        java.util.ArrayList _list = new java.util.ArrayList();
        legend.admin.objects.Insurance insurance = null;
        String query = "SELECT Insurance_ID, Insurance_Code, Insurance_Name, Contact_Person"
                       + ",Contact_Address, insurance_state, Insurance_Phone, Insurance_fax"
                       + ",Insurance_email, Notify_Days, Every_Days, account_type"
					   + ",account_number, Insurance_Status, insurance_province, User_ID, Create_date"
                       + " FROM am_ad_Insurance";

					   
					   
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String insid = rs.getString("Insurance_ID");
                String inscode = rs.getString("Insurance_Code");
                String insname = rs.getString("Insurance_Name");
                String contperson = rs.getString("Contact_Person");
                String contaddress = rs.getString("Contact_Address");
                String insstate = rs.getString("insurance_state");
                String insphone = rs.getString("Insurance_Phone");
				String insfax = rs.getString("Insurance_fax");
				String insemail = rs.getString("Insurance_email");
				String notifydays = rs.getString("Notify_Days");
				String everyday= rs.getString("Every_Days");
				String acctype = rs.getString("account_type");
				String accnumber = rs.getString("account_number");
				String insstatus = rs.getString("Insurance_Status");
				String insprovince = rs.getString("insurance_province");
				String userid = rs.getString("User_ID");
				String createdt = sdf.format(rs.getDate("Create_date"));
				
                insurance = new legend.admin.objects.Insurance();
				insurance.setInsuranceId(insid);
				insurance.setInsuranceCode(inscode);
                insurance.setInsuranceName(insname);
                insurance.setContactPerson(contperson);
                insurance.setContactAddress(contaddress);
                insurance.setInsuranceState(insstate);
                insurance.setInsurancePhone(insphone);
                insurance.setInsuranceFax(insfax);
                insurance.setInsuranceEmail(insemail);
                insurance.setNotifydays(notifydays);
				insurance.setEveryDays(everyday);
				insurance.setAccountType(acctype);
				insurance.setAccountNumber(accnumber);
				insurance.setInsuranceStatus(insstatus);
				insurance.setInsuranceProvince(insprovince);
				insurance.setUserId(userid);
				insurance.setCreateDate(createdt);
                _list.add(insurance);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }

    public java.util.List getInsuranceByQuery(String filter) {
        java.util.List _list = new java.util.ArrayList();
        legend.admin.objects.Insurance insurance = null;
        String query = "SELECT Insurance_ID, Insurance_Code, Insurance_Name, Contact_Person"
                       + ",Contact_Address, insurance_state, Insurance_Phone, Insurance_fax"
                       + ",Insurance_email, Notify_Days, Every_Days, account_type"
					   + ",account_number, Insurance_Status, insurance_province, User_ID, Create_date"
                       + " FROM am_ad_Insurance";

        query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String insid = rs.getString("Insurance_ID");
                String inscode = rs.getString("Insurance_Code");
                String insname = rs.getString("Insurance_Name");
                String contperson = rs.getString("Contact_Person");
                String contaddress = rs.getString("Contact_Address");
                String insstate = rs.getString("insurance_state");
                String insphone = rs.getString("Insurance_Phone");
				String insfax = rs.getString("Insurance_fax");
				String insemail = rs.getString("Insurance_email");
				String notifydays = rs.getString("Notify_Days");
				String everyday= rs.getString("Every_Days");
				String acctype = rs.getString("account_type");
				String accnumber = rs.getString("account_number");
				String insstatus = rs.getString("Insurance_Status");
				String insprovince = rs.getString("insurance_province");
				String userid = rs.getString("User_ID");
				String createdt = sdf.format(rs.getDate("Create_date"));
				
                insurance = new legend.admin.objects.Insurance();
				insurance.setInsuranceId(insid);
				insurance.setInsuranceCode(inscode);
                insurance.setInsuranceName(insname);
                insurance.setContactPerson(contperson);
                insurance.setContactAddress(contaddress);
                insurance.setInsuranceState(insstate);
                insurance.setInsurancePhone(insphone);
                insurance.setInsuranceFax(insfax);
                insurance.setInsuranceEmail(insemail);
                insurance.setNotifydays(notifydays);
				insurance.setEveryDays(everyday);
				insurance.setAccountType(acctype);
				insurance.setAccountNumber(accnumber);
				insurance.setInsuranceStatus(insstatus);
				insurance.setInsuranceProvince(insprovince);
				insurance.setUserId(userid);
				insurance.setCreateDate(createdt);
                _list.add(insurance);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }

    public legend.admin.objects.Insurance getInsuranceByID(String curryid) {
        legend.admin.objects.Insurance insurance = null;
        String query = "SELECT Insurance_ID, Insurance_Code, Insurance_Name, Contact_Person"
                       + ",Contact_Address, insurance_state, Insurance_Phone, Insurance_fax"
                       + ",Insurance_email, Notify_Days, Every_Days, account_type"
					   + ",account_number, Insurance_Status, insurance_province, User_ID, Create_date"
                       + " FROM am_ad_Insurance WHERE Insurance_ID="+curryid;
					   
        //query = query+filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String insid = rs.getString("Insurance_ID");
                String inscode = rs.getString("Insurance_Code");
                String insname = rs.getString("Insurance_Name");
                String contperson = rs.getString("Contact_Person");
                String contaddress = rs.getString("Contact_Address");
                String insstate = rs.getString("insurance_state");
                String insphone = rs.getString("Insurance_Phone");
				String insfax = rs.getString("Insurance_fax");
				String insemail = rs.getString("Insurance_email");
				String notifydays = rs.getString("Notify_Days");
				String everyday= rs.getString("Every_Days");
				String acctype = rs.getString("account_type");
				String accnumber = rs.getString("account_number");
				String insstatus = rs.getString("Insurance_Status");
				String insprovince = rs.getString("insurance_province");
				String userid = rs.getString("User_ID");
				String createdt = sdf.format(rs.getDate("Create_date"));
				
                insurance = new legend.admin.objects.Insurance();
                insurance.setInsuranceId(insid);
				insurance.setInsuranceCode(inscode);
                insurance.setInsuranceName(insname);
                insurance.setContactPerson(contperson);
                insurance.setContactAddress(contaddress);
                insurance.setInsuranceState(insstate);
                insurance.setInsurancePhone(insphone);
                insurance.setInsuranceFax(insfax);
                insurance.setInsuranceEmail(insemail);
                insurance.setNotifydays(notifydays);
				insurance.setEveryDays(everyday);
				insurance.setAccountType(acctype);
				insurance.setAccountNumber(accnumber);
				insurance.setInsuranceStatus(insstatus);
				insurance.setInsuranceProvince(insprovince);
				insurance.setUserId(userid);
				insurance.setCreateDate(createdt);
               
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(c, s, rs);
        }
        return insurance;

    }
    public legend.admin.objects.Insurance getInsuranceByCode(String curryid) {
        legend.admin.objects.Insurance insurance = null;
        String query = "SELECT Insurance_ID, Insurance_Code, Insurance_Name, Contact_Person"
                       + ",Contact_Address, insurance_state, Insurance_Phone, Insurance_fax"
                       + ",Insurance_email, Notify_Days, Every_Days, account_type"
					   + ",account_number, Insurance_Status, insurance_province, User_ID, Create_date"
                       + " FROM am_ad_Insurance WHERE Insurance_Code='"+curryid+"'";
					   
       
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
           c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String insid = rs.getString("Insurance_ID");
                String inscode = rs.getString("Insurance_Code");
                String insname = rs.getString("Insurance_Name");
                String contperson = rs.getString("Contact_Person");
                String contaddress = rs.getString("Contact_Address");
                String insstate = rs.getString("insurance_state");
                String insphone = rs.getString("Insurance_Phone");
				String insfax = rs.getString("Insurance_fax");
				String insemail = rs.getString("Insurance_email");
				String notifydays = rs.getString("Notify_Days");
				String everyday= rs.getString("Every_Days");
				String acctype = rs.getString("account_type");
				String accnumber = rs.getString("account_number");
				String insstatus = rs.getString("Insurance_Status");
				String insprovince = rs.getString("insurance_province");
				String userid = rs.getString("User_ID");
				String createdt = sdf.format(rs.getDate("Create_date"));
				
                insurance = new legend.admin.objects.Insurance();
                insurance.setInsuranceId(insid);
				insurance.setInsuranceCode(inscode);
                insurance.setInsuranceName(insname);
                insurance.setContactPerson(contperson);
                insurance.setContactAddress(contaddress);
                insurance.setInsuranceState(insstate);
                insurance.setInsurancePhone(insphone);
                insurance.setInsuranceFax(insfax);
                insurance.setInsuranceEmail(insemail);
                insurance.setNotifydays(notifydays);
				insurance.setEveryDays(everyday);
				insurance.setAccountType(acctype);
				insurance.setAccountNumber(accnumber);
				insurance.setInsuranceStatus(insstatus);
				insurance.setInsuranceProvince(insprovince);
				insurance.setUserId(userid);
				insurance.setCreateDate(createdt);
               
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(c, s, rs);
        }
        return insurance;

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
    public boolean createInsurance(legend.admin.objects.Insurance ccode) {

        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;
        String query =
                "INSERT INTO am_ad_Insurance  ( Insurance_Code, Insurance_Name"
                + ",Contact_Person,Contact_Address, insurance_state, Insurance_Phone"
                + ",Insurance_fax ,Insurance_email, Notify_Days, Every_Days, account_type"
				+ ",account_number, Insurance_Status, insurance_province, User_ID, Create_date,Insurance_ID) "
                + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, ccode.getInsuranceCode());
            ps.setString(2, ccode.getInsuranceName());
            ps.setString(3, ccode.getContactPerson());
            ps.setString(4, ccode.getContactAddress());
            ps.setString(5, ccode.getInsuranceState());
            ps.setString(6, ccode.getInsurancePhone());
            ps.setString(7, ccode.getInsuranceFax());
			ps.setString(8, ccode.getInsuranceEmail());
			ps.setString(9, ccode.getNotifydays());
			ps.setString(10, ccode.getEveryDays());
			ps.setString(11, ccode.getAccountType());
			ps.setString(12, ccode.getAccountNumber());
			ps.setString(13, ccode.getInsuranceStatus());
			ps.setString(14, ccode.getInsuranceProvince());
			ps.setString(15, ccode.getUserId());
			ps.setDate(16, df.dateConvert(new java.util.Date()));
			ps.setLong(17, System.currentTimeMillis());
			
			  done = (ps.executeUpdate()!=-1);

        } catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }

    public boolean updateInsurance(legend.admin.objects.Insurance ccode) {

        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;
        String query = "UPDATE am_ad_Insurance"
                       + " SET  Insurance_Code=?, Insurance_Name=?"
                + ",Contact_Person=?,Contact_Address=?, insurance_state=?, Insurance_Phone=?"
                + ",Insurance_fax=? ,Insurance_email=?, Notify_Days=?, Every_Days=?, account_type=?"
				+ ",account_number=?, Insurance_Status=?, insurance_province=? " 
                 +" WHERE Insurance_ID=?" ;

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
           
            ps.setString(1, ccode.getInsuranceCode());
            ps.setString(2, ccode.getInsuranceName());
            ps.setString(3, ccode.getContactPerson());
            ps.setString(4, ccode.getContactAddress());
            ps.setString(5, ccode.getInsuranceState());
            ps.setString(6, ccode.getInsurancePhone());
            ps.setString(7, ccode.getInsuranceFax());
			ps.setString(8, ccode.getInsuranceEmail());
			ps.setString(9, ccode.getNotifydays());
			ps.setString(10, ccode.getEveryDays());
			ps.setString(11, ccode.getAccountType());
			ps.setString(12, ccode.getAccountNumber());
			ps.setString(13, ccode.getInsuranceStatus());
			ps.setString(14, ccode.getInsuranceProvince());
			ps.setString(15, ccode.getInsuranceId());
			
			  done = (ps.executeUpdate()!=-1);

        } catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }
    public java.util.ArrayList getInsuranceByStatus(String status){
    	java.util.ArrayList _list = new java.util.ArrayList();
    	String filter = " WHERE Insurance_Status='" + status + "'";
    	_list = (java.util.ArrayList)getInsuranceByQuery(filter);
    	return _list;
    } 
   
}
