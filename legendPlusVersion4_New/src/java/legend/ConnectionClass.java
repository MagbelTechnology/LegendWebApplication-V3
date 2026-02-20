package legend;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.UUID;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;

//import java.sql.Date;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.magbel.legend.bus.ApprovalRecords;

import legend.objects.PersistenceServiceDAO;
import magma.AssetRecordsBean;

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
 <pre>
   Modification: by Jejelowo .B.Festus:
   Reason : To Change All The Store Procedure to
       approprite Java SQL Statement.
   Date	: Friday 27th November,2006
 </pre>
 * @version 1.0
 */

public class ConnectionClass {    

    private Connection conn = null;
    private String jndiName = "legendPlus";
    private String jndiNameOracle = "FinacleDataHouse";
    private String jndiVasco = "vascoDS";
    private int record_start = 0;
    private int record_count = 2;
    private Context ic = null;
    private ApprovalRecords approv;
    public ConnectionClass() {
        conn = null;
        jndiName = "legendPlus";
        jndiNameOracle = "FinacleDataHouse";
        jndiVasco = "vascoDS";
        record_start = 0;
        record_count = 2;
        ic = null;

       try {  
		ic = new InitialContext();
		approv = new ApprovalRecords();
	} catch (NamingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
       // DataSource ds = (DataSource) ic.lookup("java:comp/env/jdbc/" +
                                             //  this.jndiName);
       // conn = ds.getConnection();
        //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        //this.conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;database=legend;user=sa;password=oddeseyus");
    }

    public void refreshConnection() throws Exception {
//        if (this.conn!=null) 
//        	freeResource(); 
    	this.conn = null;
//    	System.out.println("Inside refreshConnection this.conn==: "+this.conn);
    	if (this.conn==null){
        ic = new InitialContext();
        String dsJndi = "java:/legendPlus";
        DataSource ds = (DataSource) ic.lookup(dsJndi);
        this.conn = ds.getConnection();
    	}
    } 
   
    public void refreshConnectionOracle()throws Exception{
//        if (this.conn!=null) 
//        	freeResource();  
         
        ic = new InitialContext();
        String dsJndi = "java:/FinacleDataHouse";
        DataSource ds = (DataSource) ic.lookup(dsJndi);
        this.conn = ds.getConnection();

    	
//    if (this.conn!=null) freeResource();
//        //DataSource ds = (DataSource) ic.lookup("java:comp/env/jdbc/" +
//                                               //this.jndiNameOracle);
//      DataSource ds = (DataSource) ic.lookup("java:/"+this.jndiNameOracle);
//        this.conn = ds.getConnection();
    }
       
    public void refreshConnectionOracleVasco()throws Exception{
    if (this.conn!=null) freeResource();
        //DataSource ds = (DataSource) ic.lookup("java:comp/env/jdbc/" +
                                               //this.jndiNameOracle);
      DataSource ds = (DataSource) ic.lookup("java:/"+this.jndiVasco);
        this.conn = ds.getConnection();
    }
    public PreparedStatement getPreparedStatementOracle(String sql)throws Exception{
         getConnectionOracle();
        PreparedStatement ps = conn.prepareStatement(sql);

    return ps;  
    }
    
	public Connection getOracleConnection() {
		Connection con = null;
//		dc = new DataConnect("legendPlus");
		try {
/*			if(this.con==null){ 
				dc = new DataConnect("legendPlus"); 
				con = dc.getConnection();
				}*/
        	if(con==null){
                Context initContext = new InitialContext();
                String dsJndi = "java:/FinacleDataHouse";
                DataSource ds = (DataSource) initContext.lookup(
                		dsJndi);
                con = ds.getConnection();
        	}
		} catch (Exception e) {
			System.out.println("WARNING: Error getting connection in Connection Class ->"
					+ e.getMessage());
		}  //finally {
//			closeConnection(con);
//		}
		return con;
	}
    
    
    /**
     * getConnection
     *
     * @return Connection
     * @throws Exception
     */
//    public Connection getConnection() throws Exception {
//    	System.out.println("About to refreshConnection connection");
// //           refreshConnection();
//
////        return conn;
//		Connection con = null;
////		dc = new DataConnect("legendPlus");
//		try {
//
//        	if(con==null){
//                Context initContext = new InitialContext();
//                String dsJndi = "java:/legendPlus";
//                DataSource ds = (DataSource) initContext.lookup(
//                		dsJndi);
//                con = ds.getConnection();
//        	}
//		} catch (Exception e) {
//			System.out.println("WARNING: Error getting connection in Connection Class ->"
//					+ e.getMessage());
//		}  //finally {
////			closeConnection(con);
////		}
//		return con;    	
//    }

	public Connection getConnection() {
//		System.out.println("About to refreshConnection connection in ConnectionClass");
		Connection con = null;
		try {
                Context initContext = new InitialContext();
                String dsJndi = "java:/legendPlus";
                DataSource ds = (DataSource) initContext.lookup(
                		dsJndi);
                con = ds.getConnection();
		} catch (Exception e) {
			System.out.println("WARNING: Error getting connection in ConnectionClass ->"
					+ e.getMessage());
		}
		//finally {
//			closeConnection(con);
//		}
		return con;
	}
     public Connection getConnectionOracle() throws Exception {

            refreshConnectionOracle();

        return conn;
    }

     
     public Connection getConnectionOracleVasco() throws Exception {

         refreshConnectionOracleVasco();

     return conn;
 }     
    /**
     * getConnection
     *
     * @return Statement
     * @throws Exception
     */
    public Statement getStatement() throws Exception {
//    	System.out.println("====this.conn in getStatement(); "+this.conn);
        if (this.conn== null) {
        	return getConnection().createStatement(); 
        } 
        else if(this.conn!= null) {
        	 return this.conn.createStatement();
        }
        else{
        	return this.conn.createStatement();
        }
    }
 public Statement getStatementOracle() throws Exception {
        if (this.conn!= null) {
            return this.conn.createStatement();
        } else if(this.conn== null) {
            return getConnectionOracle().createStatement();
        }
        else{
            return null;
        }
    }
    /**
     * freeResource
     *
     * @param con Connection
     * @param ps PreparedStatement
     * @param rs ResultSet
     * @todo Implement this magma.net.dao.eConnection method
     */
    public void freeResource() {
        try {
        	// Closing the connection
//        	 System.out.println("Connection Value: "+this.conn);
        	if(this.conn != null) {
//        		System.out.println("About to Close Connection: "+conn);
        		this.conn.close();}
            if (this.conn.isClosed()){ 
 //             System.out.println("Connection Closed.");
            	} 
            else{//System.out.println("Connection Is not Closed.");
            }
            if (this.conn != null) {
                this.conn.close();
            }
      //      this.conn = null;
        } catch (Exception e) {  
//            System.out.println("WARN:Error closing Connection in ConnectionClass ->" +
//                               e.getMessage());
        }

    }

    /**
     * freeResource
     *
     * @param con Connection
     * @param ps PreparedStatement
     * @todo Implement this magma.net.dao.eConnection method
     */
    public void freeResource(Connection con, PreparedStatement ps) {
        try {
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (Exception e) {
            System.out.println("WARNING:Error closing Connection in FreeResource 1 ->" +
                               e.getMessage());
        }

    }
    
    public void freeResource(Connection con, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (Exception e) {
            System.out.println("WARNING:Error closing Connection in FreeResource 2  ->" +
                               e.getMessage());
        }

    }

    /**
     * freeResource
     *
     * @param con Connection
     * @param ps PreparedStatement
     * @param rs ResultSet
     * @todo Implement this magma.net.dao.eConnection method
     */
    public void freeResource(Connection con, PreparedStatement ps,
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
            System.out.println("WARNING:Error closing Connection in FreeResource 3  ->" +
                               e.getMessage());
        }

    }

    /**
     * getIdentity
     *
     * @return String
     */
    public String getIdentity() {
        return UUID.randomUUID().toString();
    }

    /**
     * getProperties
     *
     * @param sele String
     * @param vals String[][]
     * @return String
     */
    public String getProperties(String sele, String[][] vals) {
        String html = new String();
        if (sele == null) {
            sele = " ";
        }

        if (vals != null) {
            for (int i = 0; i < vals.length; i++) {
                html = html + "<option value='" + vals[i][0] + "' " +
                       (vals[i][0].equalsIgnoreCase(sele) ? " selected " : "") +
                       ">" + vals[i][2] + "</option> ";
            }

        }

        return html;
    }

    /**
     * getProperties
     *
     * @param sele String
     * @param vals String[][]
     * @return String
     */
    public String getUserprops(String sele, String[][] vals) {
        String html = new String();
        if (sele == null) {
            sele = " ";
        }
        for (int i = 0; i < vals.length; i++) {
            html = html + "<option value='" + vals[i][0] + "' " +
                   (vals[i][0].equalsIgnoreCase(sele) ? " selected " : "") +
                   ">" + vals[i][1] + "</option> ";
        }

        return html;
    }

    /**
     * getProperties
     *
     * @param sele String
     * @param iden String[]
     * @param vals String[]
     * @return String
     */
    public String getProperties(String sele, String[] iden, String[] vals) {
        String html = new String();
        if (sele == null) {
            sele = " ";
        }
        for (int i = 0; i < iden.length; i++) {
            html = html + "<option value='" + iden[i] + "' " +
                   (iden[i].equalsIgnoreCase(sele) ? " selected " : "") + ">" +
                   vals[i] + "</option> ";
        }

        return html;
    }

    /**
     * getEncrypted
     *
     * @param input String
     * @throws Throwable
     * @return String
     */
    public String getEncrypted(String input) throws Throwable {

        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(input.toString().getBytes("UTF-16"));


        return Base64.getEncoder().encodeToString(md.digest());
    }


    public String encodeHtmlText(String text) {
        if (text == null) {
            return null;
        }
        int length = text.length();
        StringBuffer encodedText = new StringBuffer(2 * length);
        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);
            if (c == '<') {
                encodedText.append("&lt;");
            } else if (c == '>') {
                encodedText.append("&gt;");
            } else if (c == '&') {
                encodedText.append("&amp;");
            } else if (c == '"') {
                encodedText.append("&quot;");
            } else if (c == ' ') {
                encodedText.append("&nbsp;");
            } else {
                encodedText.append(c);
            }
        }
        return encodedText.toString();
    };

    public String filterFieldValue(String value) {
        if (value == null) {
            return null;
        }
        int length = value.length();
        StringBuffer filteredValue = new StringBuffer((int) (length * 1.1));
        for (int i = 0; i < length; i++) {
            char c = value.charAt(i);
            if (c == '\'') {
                filteredValue.append("''");
            } else {
                filteredValue.append(c);
            }
        }
        return filteredValue.toString();
    };

    public String addMonthToDate(String date, int month) throws Throwable {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
                "dd/MM/yyyy");
        java.util.Calendar calendarDate = null;
        String added = null;
        if (date == null) {
            added = null;
        } else {
            Date dDate = sdf.parse(date);
            calendarDate = new java.util.GregorianCalendar();
            calendarDate.setTime(dDate);
            calendarDate.add(java.util.Calendar.MONTH, month);
            dDate = calendarDate.getTime();
            added = sdf.format(dDate);
        }
        return added;
    }

    public void setRecord_start(String record_start) {
        this.record_start = Integer.parseInt(record_start);
    }

    public void setRecord_count(String record_count) {
        this.record_count = Integer.parseInt(record_count);
    }

    public String getRecord_start() {
        return String.valueOf(record_start);
    }

    public String getRecord_count() {
        return String.valueOf(record_count);
    }

    public String[][] getBranchesForCombo() throws Exception {
    	Connection conn = null;
    	ResultSet rs = null;
    	ResultSet rsc = null;
    	try{
    		
    	
        String query = "SELECT * FROM AM_AD_BRANCH	" +
                       "WHERE BRANCH_STATUS = 'ACTIVE'  " +
                       "ORDER BY BRANCH_NAME ASC";
        String counterQuery = "SELECT count(*) FROM AM_AD_BRANCH	" +
                              "WHERE BRANCH_STATUS = 'ACTIVE'  ";

        
        //rs = getStatement().executeQuery(query);
        conn = getConnection();
        rs = conn.prepareStatement(query).executeQuery();
        rsc = conn.prepareStatement(counterQuery).executeQuery();
        //rsc = getStatement().executeQuery(counterQuery);
        rsc.next();
        int count = rsc.getInt(1);
        if (count > 0) {

            String[][] a = new String[count][12];
            int i = 0;
            while (rs.next()) {
                for (int j = 0; j < 12; j++) {
                    a[i][j] = rs.getString(j + 1);
                }
                i++;
            }
//            conn.close();
            System.out.println("About to close connection!!!");
            return a;
            
        }
    	}catch(Exception e){
    		e.getMessage();
    	}finally{
    		//closeConnection(conn);
    		 System.out.println("Close connection!!!");
//    		  freeResource(conn, rs);
//    		  freeResource(conn);
    		 conn.close();
    	}
      
        return null;
    }

    public String[][] getBranchesForComboExcluding(String s) throws Exception {
    	Connection conn = null;
    	ResultSet rs = null;
    	ResultSet rsc = null;
    	try{
        String query = "SELECT BRANCH_ID, BRANCH_CODE, BRANCH_NAME " +
                       "FROM AM_AD_BRANCH " +
                       "WHERE LOWER(BRANCH_NAME) != LOWER('" + s + "')";

        String countQuery = "SELECT count(*) FROM AM_AD_BRANCH " +
                            "WHERE LOWER(BRANCH_NAME) != LOWER('" + s + "')";

//        ResultSet rs = getStatement().executeQuery(query);
//        ResultSet rsc = getStatement().executeQuery(countQuery);
        conn = getConnection();
        rs = conn.prepareStatement(query).executeQuery();
        rsc = conn.prepareStatement(countQuery).executeQuery();
        rsc.next();
        int count = rsc.getInt(1);
        if (count > 0) {

            String[][] a = new String[count][12];
            int i = 0;
            while (rs.next()) {
                for (int j = 0; j < 12; j++) {
                    a[i][j] = rs.getString(j + 1);
                }
                i++;
            }
           // freeResource();
            return a;
        }
    	} catch(Exception e){
    		e.getMessage();
    	}finally{
    	conn.close();
    	}
       // freeResource();
        return null;

    }

    public String[][] getDepartmentsForCombo() throws Exception {
    	Connection conn = null;
    	ResultSet rs = null;
    	ResultSet rsc = null;
    	try{

        String query = "SELECT * FROM AM_AD_DEPARTMENT  " +
                       "WHERE DEPT_STATUS = 'ACTIVE' ORDER BY DEPT_NAME ASC";

        String countQuery = "SELECT COUNT(*) FROM AM_AD_DEPARTMENT  " +
                            "WHERE DEPT_STATUS = 'ACTIVE' ";

//        ResultSet rs = getStatement().executeQuery(query);
//        ResultSet rsc = getStatement().executeQuery(countQuery);
        conn = getConnection();
        rs = conn.prepareStatement(query).executeQuery();
        rsc = conn.prepareStatement(countQuery).executeQuery();
        rsc.next();
        int count = rsc.getInt(1);
        if (count > 0) {

            String[][] a = new String[count][8];
            int i = 0;
            while (rs.next()) {
                for (int j = 0; j < 8; j++) {
                    a[i][j] = rs.getString(j + 1);
                }
                i++;
            }
            return a;
        }
    	} catch(Exception e){
    		e.getMessage();
    	}finally{
    		conn.close();
    	}
        return null;
    }

    public String[][] getDepartmentsForComboExcluding(String s) throws
            Exception {
    	Connection conn = null;
    	ResultSet rs = null;
    	ResultSet rsc = null;
    	try{
        String query = "SELECT DEPT_ID, DEPT_CODE, DEPT_NAME " +
                       "FROM AM_AD_DEPARTMENT " +
                       "WHERE LOWER(DEPT_NAME) != LOWER('" + s + "')";

        String countQuery = "SELECT count(*) FROM AM_AD_DEPARTMENT " +
                            "WHERE LOWER(DEPT_NAME) != LOWER('" + s + "')";

//        ResultSet rs = getStatement().executeQuery(query);
//        ResultSet rsc = getStatement().executeQuery(countQuery);
        conn = getConnection();
        rs = conn.prepareStatement(query).executeQuery();
        rsc = conn.prepareStatement(countQuery).executeQuery();
        rsc.next();
        int count = rsc.getInt(1);
        if (count > 0) {

            String[][] a = new String[count][8];
            int i = 0;
            while (rs.next()) {
                for (int j = 0; j < 8; j++) {
                    a[i][j] = rs.getString(j + 1);
                }
                i++;
            }
            //freeResource();
            return a;
        }
    	}catch(Exception e){
    		e.getMessage();
    	}
    	finally{
    		conn.close();
    	}
        //freeResource();
        return null;

    }

    public String[][] getCategoriesForCombo() throws Exception {
    	Connection conn = null;
    	ResultSet rs = null;
    	ResultSet rsc = null;
    	try{
        String query = "SELECT * FROM AM_AD_CATEGORY  " +
                       "WHERE CATEGORY_STATUS = 'ACTIVE'  " +
                       "ORDER BY CATEGORY_NAME ASC";

        String countQuery = "SELECT count(*) FROM AM_AD_CATEGORY  " +
                            "WHERE CATEGORY_STATUS = 'ACTIVE'  ";

//        ResultSet rs = getStatement().executeQuery(query);
//        ResultSet rsc = getStatement().executeQuery(countQuery);
        conn = getConnection();
        rs = conn.prepareStatement(query).executeQuery();
        rsc = conn.prepareStatement(countQuery).executeQuery();
        rsc.next();
        int count = rsc.getInt(1);
        if (count > 0) {

            String[][] a = new String[count][16];
            int i = 0;
            while (rs.next()) {
                for (int j = 0; j < 16; j++) {
                    a[i][j] = rs.getString(j + 1);
                }
                i++;
            }
           // freeResource();
            return a;
        }
    	} catch(Exception e){
    		e.getMessage();
    	}finally{
    		conn.close();
    	}
        //freeResource();
        return null;
    }

    public String[][] getCategoriesForComboExcluding(String s) throws Exception {
    	Connection conn = null;
    	ResultSet rs = null;
    	ResultSet rsc = null;
    	try{
        String query = "SELECT CATEGORY_ID, CATEGORY_CODE, CATEGORY_NAME " +
                       "FROM AM_AD_CATEGORY " +
                       "WHERE LOWER(CATEGORY_NAME) != LOWER('" + s + "')";
        String countQuery = "SELECT count(*) FROM AM_AD_CATEGORY " +
                            "WHERE LOWER(CATEGORY_NAME) != LOWER('" + s + "')";

//        ResultSet rs = getStatement().executeQuery(query);
//        ResultSet rsc = getStatement().executeQuery(countQuery);

        conn = getConnection();
              rs = conn.prepareStatement(query).executeQuery();
              rsc = conn.prepareStatement(countQuery).executeQuery();
        rsc.next();
        int count = rsc.getInt(1);
        if (count > 0) {

            String[][] a = new String[count][16];
            int i = 0;
            while (rs.next()) {
                for (int j = 0; j < 16; j++) {
                    a[i][j] = rs.getString(j + 1);
                }
                i++;
            }
            //freeResource();
            return a;
        }
    	}catch(Exception e){
    		e.getMessage();
    	}finally{
    		conn.close();
    	}
        //freeResource();
        return null;

    }

    public String[][] getMaintenanceVendorsForCombo() throws Exception {
    	Connection conn = null;
    	ResultSet rs = null;
    	ResultSet rsc = null;
    	try{
        String query =
                "SELECT	VENDOR_ID AS MAINT_ID,VENDOR_CODE AS MAINT_CODE," +
                "VENDOR_NAME AS MAINT_NAME    " +
                "FROM AM_AD_VENDOR   " +
                "WHERE VENDOR_STATUS = 'ACTIVE'   " +
                "UNION   " +
                "SELECT  " +
                "MAINTAINANCE_ID AS MAINT_ID,REGISTRATION_NO AS MAINT_CODE," +
                "TECH_NAME AS MAINT_NAME    " +
                "FROM AM_AD_MAINTENANCEREPS  " +
                "WHERE MAINTENACE_STATUS = 'ACTIVE'";

        String countQuery = "SELECT COUNT(*) FROM AM_AD_MAINTENANCEREPS " +
                            "WHERE MAINTENACE_STATUS = 'ACTIVE'";

//        ResultSet rs = getStatement().executeQuery(query);
//        ResultSet rsc = getStatement().executeQuery(countQuery);
        conn = getConnection();
        rs = conn.prepareStatement(query).executeQuery();
        rsc = conn.prepareStatement(countQuery).executeQuery();
        rsc.next();
        int count = rsc.getInt(1);
        if (count > 0) {

            String[][] a = new String[count + 1][3];
            int i = 0;
            a[i][0] = "0";
            a[i][1] = "0";
            a[i++][2] = "None";

            while (rs.next()) {
                for (int j = 0; j < 3; j++) {
                    a[i][j] = rs.getString(j + 1);
                }
                i++;
            }
            //freeResource();
            return a;
        }
    	}catch(Exception e){
    		e.getMessage();
    	}finally{
    		conn.close();
    	}
        //freeResource();
        return null;
    }

    public String[][] getSuppliersForCombo() throws Exception {
    	Connection conn = null;
    	ResultSet rs = null;
    	ResultSet rsc = null;
    	try{
        String query =
                "SELECT VENDOR_ID AS MAINT_ID,VENDOR_CODE AS MAINT_CODE," +
                "VENDOR_NAME AS MAINT_NAME    " +
                "FROM AM_AD_VENDOR WHERE VENDOR_STATUS = 'A'";

        String countQuery =
                "select count(*) FROM AM_AD_VENDOR WHERE VENDOR_STATUS = 'A'";

//        ResultSet rs = getStatement().executeQuery(query);
//        ResultSet rsc = getStatement().executeQuery(countQuery);
        conn = getConnection();
        rs = conn.prepareStatement(query).executeQuery();
        rsc = conn.prepareStatement(countQuery).executeQuery();
        rsc.next();
        int count = rsc.getInt(1);
        if (count > 0) {

            String[][] a = new String[count + 1][3];
            a[0][0] = "0";
            a[0][1] = "0";
            a[0][2] = "None";

            int i = 1;
            while (rs.next()) {
                for (int j = 0; j < 3; j++) {
                    a[i][j] = rs.getString(j + 1);
                }
                i++;
            }
           // freeResource();
            return a;
        }
    	}catch(Exception e){
    		e.getMessage();
    	}finally{
    		conn.close();
    	}
        //freeResource();
        return null;
    }

    public String[][] getDisposalReasonsForCombo() throws Exception {
    	Connection conn = null;
    	ResultSet rs = null;
    	ResultSet rsc = null;
    	try{
        String query = "SELECT * FROM AM_AD_DISPOSALREASONS   " +
                       "WHERE REASON_STATUS = 'ACTIVE'   " +
                       "ORDER BY DESCRIPTION ASC    ";
        String countQuery = "SELECT count(*) FROM AM_AD_DISPOSALREASONS   " +
                            "WHERE REASON_STATUS = 'ACTIVE'";

//        ResultSet rs = getStatement().executeQuery(query);
//        ResultSet rsc = getStatement().executeQuery(countQuery);
        conn = getConnection();
        rs = conn.prepareStatement(query).executeQuery();
        rsc = conn.prepareStatement(countQuery).executeQuery();
        rsc.next();
        int count = rsc.getInt(1);
        if (count > 0) {

            String[][] a = new String[count][3];
            int i = 0;
            while (rs.next()) {
                a[i][0] = rs.getString(1);
                a[i][1] = rs.getString(2);
                a[i][2] = rs.getString(3);
                i++;
            }
           // freeResource();
            return a;
        }
    	}catch(Exception e){
    		e.getMessage();
    	}finally{
    		conn.close();
    	}
        //freeResource();
        return null;
    }

    public String[][] getAssetMakeForCombo() throws Exception {
    	Connection conn = null;
    	ResultSet rs = null;
    	ResultSet rsc = null;
    	try{
        String query = "SELECT * FROM AM_GB_ASSETMAKE	";
        String countQuery = "SELECT COUNT(*) FROM AM_GB_ASSETMAKE	";

//        ResultSet rs = getStatement().executeQuery(query);
//        ResultSet rsc = getStatement().executeQuery(countQuery);
        conn = getConnection();
        rs = conn.prepareStatement(query).executeQuery();
        rsc = conn.prepareStatement(countQuery).executeQuery();
        rsc.next();
        int count = rsc.getInt(1);
        if (count > 0) {

            String[][] a = new String[count][3];
            int i = 0;
            while (rs.next()) {
                a[i][0] = rs.getString(1);
                a[i][1] = rs.getString(2);
                a[i][2] = rs.getString(3);
                i++;
            }
            return a;
        }
    	}catch(Exception e){
    		e.getMessage();
    	}finally{
    		conn.close();
    	}
        return null;
    }

    public String[][] getStatesForCombo() throws Exception {
    	Connection conn = null;
    	ResultSet rs = null;
    	ResultSet rsc = null;
    	try{

    		  conn = getConnection();
    		        rs = conn.prepareStatement("am_msp_select_states 0, 'A'").executeQuery();
    		        rsc = conn.prepareStatement("am_msp_count_states 0, 'A'").executeQuery();
        
//    		        ResultSet rs = getStatement().executeQuery(
//                "am_msp_select_states 0, 'A'");
//        ResultSet rsc = getStatement().executeQuery(
//                "am_msp_count_states 0, 'A'");
        rsc.next();
        int count = rsc.getInt(1);
        if (count > 0) {

            String[][] a = new String[count + 1][3];
            a[0][0] = "0";
            a[0][1] = "0";
            a[0][2] = "None";

            int i = 1;
            while (rs.next()) {
                a[i][0] = rs.getString(1);
                a[i][1] = rs.getString(2);
                a[i][2] = rs.getString(3);
                i++;
            }
            //freeResource();
            return a;
        }
    	}catch(Exception e){
    		e.getMessage();
    	}finally{
    		conn.close();
    	}
       // freeResource();
        return null;
    }

    public String[][] getDriversForCombo() throws Exception {
    	Connection conn = null;
    	ResultSet rs = null;
    	ResultSet rsc = null;
    	try{
    		conn = getConnection();
            rs = conn.prepareStatement("SELECT DRIVER_ID, DRIVER_CODE, driver_lastname, driver_firstname, driver_othername from am_ad_driver where driver_status = 'A' order by driver_lastname").executeQuery();
            rsc = conn.prepareStatement("select count(*) from am_ad_driver where driver_status = 'ACTIVE'").executeQuery();
    	
//            ResultSet rs = getStatement().executeQuery("SELECT DRIVER_ID, DRIVER_CODE, driver_lastname, driver_firstname, driver_othername from am_ad_driver where driver_status = 'A' order by driver_lastname");
//        ResultSet rsc = getStatement().executeQuery(
//                "select count(*) from am_ad_driver where driver_status = 'ACTIVE'");
        rsc.next();
        int count = rsc.getInt(1);
        if (count > 0) {

            String[][] a = new String[count + 1][3];
            a[0][0] = "0";
            a[0][1] = "0";
            a[0][2] = "None";
            int i = 1;
            while (rs.next()) {
                a[i][0] = rs.getString(1);
                a[i][1] = rs.getString(2);
                a[i][2] = rs.getString(3) + ", " + rs.getString(4) + " " +
                          rs.getString(5);
                i++;
            }
            //freeResource();
            return a;
        }
    	}catch(Exception e){
    		e.getMessage();
    	}finally{
    		conn.close();
    	}
        return null;
    }

    public String[][] getLocationsForCombo() throws Exception {
    	Connection conn = null;
    	ResultSet rs = null;
    	ResultSet rsc = null;

    	try{

        String query = "SELECT LOCATION_ID, LOCATION_CODE, LOCATION " +
                       "FROM AM_GB_LOCATION " +
                       "WHERE STATUS = 'ACTIVE' ORDER BY LOCATION";

        String countQuery =
                "SELECT COUNT(*) FROM AM_GB_LOCATION WHERE STATUS = 'ACTIVE'";

//        ResultSet rs = getStatement().executeQuery(query);
//        ResultSet rsc = getStatement().executeQuery(countQuery);
        	conn = getConnection();
              rs = conn.prepareStatement(query).executeQuery();
              rsc = conn.prepareStatement(countQuery).executeQuery();
        rsc.next();
        int count = rsc.getInt(1);
        if (count > 0) {

            String[][] a = new String[count][3];
            int i = 0;
            while (rs.next()) {
                a[i][0] = rs.getString(1);
                a[i][1] = rs.getString(2);
                a[i][2] = rs.getString(3);
                i++;
            }
            //freeResource();
            return a;
        }
    	}
    	catch(Exception e){
    		e.getMessage();
    	}finally{
    		conn.close();
    	}
        return null;
    }


    public String[][] getSectionsForComboOld() throws Exception {
    	Connection conn = null;
    	ResultSet rs = null;
    	ResultSet rsc = null;
    	
    	try{
    	
        String query = "SELECT * FROM AM_AD_SECTION  " +
                       "WHERE SECTION_STATUS = 'ACTIVE'  " +
                       "ORDER BY SECTION_NAME ASC";

        String countQuery = "SELECT count(*) FROM AM_AD_SECTION  " +
                            "WHERE SECTION_STATUS = 'ACTIVE'  ";

//        rs = getStatement().executeQuery(query);
//        rsc = getStatement().executeQuery(countQuery);
        
        conn = getConnection();
        rs = conn.prepareStatement(query).executeQuery();
        rsc = conn.prepareStatement(countQuery).executeQuery();
        rsc.next();
        int count = rsc.getInt(1);
        if (count > 0) {

            String[][] a = new String[count][3];
            int i = 0;
            while (rs.next()) {
                a[i][0] = rs.getString(1);
                a[i][1] = rs.getString(2);
                a[i][2] = rs.getString(3);
                i++;
            }
            freeResource();
            return a;

        }
        }catch(Exception e){
        	e.getMessage();
        }finally{
        	conn.close();
        }
        return null;
    }
    
    public String[][] getSectionsForCombo() {
        String query = "SELECT * " +
                       "FROM AM_AD_SECTION " +
                       "WHERE SECTION_STATUS = 'ACTIVE' " +
                       "ORDER BY SECTION_NAME ASC";
        
        String countQuery = "SELECT COUNT(*) FROM AM_AD_SECTION " +
                            "WHERE SECTION_STATUS = 'ACTIVE'";
        
        try (Connection conn = getConnection();
             PreparedStatement countPs = conn.prepareStatement(countQuery);
             ResultSet countRs = countPs.executeQuery()) {
            
            if (!countRs.next()) {
                return new String[0][0]; // no sections
            }

            int count = countRs.getInt(1);
            if (count == 0) {
                return new String[0][0];
            }

            String[][] sections = new String[count][3];

            try (PreparedStatement ps = conn.prepareStatement(query);
                 ResultSet rs = ps.executeQuery()) {

                int i = 0;
                while (rs.next()) {
                    sections[i][0] = rs.getString(1);
                    sections[i][1] = rs.getString(2);
                    sections[i][2] = rs.getString(3);
                    i++;
                }
            }

            return sections;

        } catch (Exception e) {
            System.out.println("WARN: Error fetching sections -> " + e.getMessage());
            e.printStackTrace();
            return new String[0][0]; // safe fallback
        }
    }

    public String[][] getSectionsForComboExcludingOld(String s) throws Exception {
    	Connection conn = null;
    	ResultSet rs = null;
    	ResultSet rsc = null;

    	try{
        String query = "SELECT SECTION_ID, SECTION_CODE, SECTION_NAME " +
                       "FROM AM_AD_SECTION " +
                       "WHERE LOWER(SECTION_NAME) != LOWER('" + s + "')";
        String countQuery = "SELECT COUNT(*) FROM AM_AD_SECTION " +
                            "WHERE LOWER(SECTION_NAME) != LOWER('" + s + "')";

//        ResultSet rs = getStatement().executeQuery(query);
//        ResultSet rsc = getStatement().executeQuery(countQuery);
        conn = getConnection();
        rs = conn.prepareStatement(query).executeQuery();
        rsc = conn.prepareStatement(countQuery).executeQuery();
        rsc.next();
        int count = rsc.getInt(1);
        if (count > 0) {

            String[][] a = new String[count][3];
            int i = 0;
            while (rs.next()) {
                a[i][0] = rs.getString(1);
                a[i][1] = rs.getString(2);
                a[i][2] = rs.getString(3);
                i++;
            }
           // freeResource();
            return a;
        }
    	}
    	catch(Exception e){
    		e.getMessage();
    	}
    	finally{
    		conn.close();
    	}
        return null;

    }
    
    public String[][] getSectionsForComboExcluding(String excludeName) {
        String query = "SELECT SECTION_ID, SECTION_CODE, SECTION_NAME " +
                       "FROM AM_AD_SECTION " +
                       "WHERE LOWER(SECTION_NAME) != LOWER(?) " +
                       "ORDER BY SECTION_NAME ASC";

        String countQuery = "SELECT COUNT(*) FROM AM_AD_SECTION " +
                            "WHERE LOWER(SECTION_NAME) != LOWER(?)";

        try (Connection conn = getConnection();
             PreparedStatement countPs = conn.prepareStatement(countQuery)) {

            countPs.setString(1, excludeName);
            try (ResultSet countRs = countPs.executeQuery()) {
                if (!countRs.next() || countRs.getInt(1) == 0) {
                    return new String[0][0]; // no sections
                }

                int count = countRs.getInt(1);
                String[][] sections = new String[count][3];

                try (PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setString(1, excludeName);
                    try (ResultSet rs = ps.executeQuery()) {
                        int i = 0;
                        while (rs.next()) {
                            sections[i][0] = rs.getString(1);
                            sections[i][1] = rs.getString(2);
                            sections[i][2] = rs.getString(3);
                            i++;
                        }
                    }
                }
                return sections;
            }

        } catch (Exception e) {
            System.out.println("WARN: Error fetching sections excluding '" + excludeName + "' -> " + e.getMessage());
            e.printStackTrace();
            return new String[0][0]; // safe fallback
        }
    }

    public String[][] getStatusForCombo() throws Exception {
        String[][] a = new String[4][3];

        a[0][0] = "A";
        a[0][1] = "";
        a[0][2] = "Active";

        a[1][0] = "C";
        a[1][1] = "";
        a[1][2] = "Closed";

        a[2][0] = "Z";
        a[2][1] = "";
        a[2][2] = "Distribution";

        a[3][0] = "S";
        a[3][1] = "";
        a[3][2] = "Stolen";

        return a;
    }

    public String getVatRateOld() throws Exception {
    	Connection conn = null;
    	ResultSet rs = null;
    	
    	conn = getConnection();
        rs = conn.prepareStatement("SELECT VAT_RATE FROM AM_GB_COMPANY").executeQuery();
        
//        ResultSet rs = getStatement().executeQuery(
//                "SELECT VAT_RATE FROM AM_GB_COMPANY");
        
        String vat="0";
        if (rs.next()) {
           vat=rs.getString(1);
        }
        conn.close();
        //freeResource();
        return vat;
    }

    public String getVatRate() {
        String vat = "0";

        String query = "SELECT VAT_RATE FROM AM_GB_COMPANY";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                vat = rs.getString(1) != null ? rs.getString(1) : "0";
            }

        } catch (Exception ex) {
            System.out.println("WARN: Error fetching VAT rate -> " + ex.getMessage());
            ex.printStackTrace();
        }

        return vat;
    }


     public String getWhRateOld() throws Exception {
    	 Connection conn = null;
     	ResultSet rs = null;
     	
    	conn = getConnection();
        rs = conn.prepareStatement("SELECT WHT_RATE FROM AM_GB_COMPANY").executeQuery();
//        ResultSet rs = getStatement().executeQuery(
//                "SELECT WHT_RATE FROM AM_GB_COMPANY");
        String vat="0";
        if (rs.next()) {
           vat= rs.getString(1);
	        }
        conn.close();
        //freeResource();
        return vat;
    }
     
     public String getWhRate() {
    	    String rate = "0";
    	    String query = "SELECT WHT_RATE FROM AM_GB_COMPANY";

    	    try (Connection conn = getConnection();
    	         PreparedStatement ps = conn.prepareStatement(query);
    	         ResultSet rs = ps.executeQuery()) {

    	        if (rs.next()) {
    	            rate = rs.getString(1) != null ? rs.getString(1) : "0";
    	        }

    	    } catch (Exception e) {
    	        System.out.println("WARN: Error fetching WHT_RATE -> " + e.getMessage());
    	        e.printStackTrace();
    	    }

    	    return rate;
    	}


   public String getFedWhRateOld() throws Exception {
	   Connection conn = null;
    	ResultSet rs = null;
    	
   	conn = getConnection();
       rs = conn.prepareStatement("SELECT FED_WHT_RATE FROM AM_GB_COMPANY").executeQuery();
       
//        ResultSet rs = getStatement().executeQuery(
//                "SELECT FED_WHT_RATE FROM AM_GB_COMPANY");
        String vat="0";
        if (rs.next()) {
           vat= rs.getString(1);
	        }
        conn.close();
       // freeResource();
        return vat;
    }

   public String getFedWhRate() {
	    String rate = "0";
	    String query = "SELECT FED_WHT_RATE FROM AM_GB_COMPANY";

	    try (Connection conn = getConnection();
	         PreparedStatement ps = conn.prepareStatement(query);
	         ResultSet rs = ps.executeQuery()) {

	        if (rs.next()) {
	            rate = rs.getString(1) != null ? rs.getString(1) : "0";
	        }

	    } catch (Exception e) {
	        System.out.println("WARN: Error fetching FED_WHT_RATE -> " + e.getMessage());
	        e.printStackTrace();
	    }

	    return rate;
	}


   public String getProcessingStatusOld() throws Exception {
	   Connection conn = null;
   	ResultSet rs = null;
   	
  	conn = getConnection();
      rs = conn.prepareStatement("SELECT PROCESSING_STATUS FROM AM_GB_COMPANY").executeQuery();
      
//        ResultSet rs = getStatement().executeQuery(
//                "SELECT PROCESSING_STATUS FROM AM_GB_COMPANY");
        String ps="0";
        if (rs.next()) {
           ps=rs.getString("processing_status");
	        }
        conn.close();
        //freeResource();
        return ps;
    }
   
   public String getProcessingStatus() {
	    String status = "0"; 
	    String query = "SELECT PROCESSING_STATUS FROM AM_GB_COMPANY";

	    try (Connection conn = getConnection();
	         PreparedStatement ps = conn.prepareStatement(query);
	         ResultSet rs = ps.executeQuery()) {

	        if (rs.next()) {
	            status = rs.getString("PROCESSING_STATUS") != null ? rs.getString("PROCESSING_STATUS") : "0";
	        }

	    } catch (Exception ex) {
	        System.out.println("WARN: Error fetching processing status -> " + ex.getMessage());
	        ex.printStackTrace();
	    }

	    return status;
	}

   public String getLegacyExportStatusOld() throws Exception {
	   Connection conn = null;
   	ResultSet rs = null;
   	
  	conn = getConnection();
      rs = conn.prepareStatement("SELECT COUNT(*) AS LEGACYEXPORTSTATUS FROM FINACLE_EXT ").executeQuery();
      
//        ResultSet rs = getStatement().executeQuery(
//                "SELECT PROCESSING_STATUS FROM AM_GB_COMPANY");
        String ps="0";
        if (rs.next()) {
           ps=rs.getString("LEGACYEXPORTSTATUS");
	        }
        conn.close();
        //freeResource();
        return ps;
    }
   
   public String getLegacyExportStatus() {
	    String status = "0"; // default value
	    String query = "SELECT COUNT(*) AS LEGACYEXPORTSTATUS FROM FINACLE_EXT";

	    try (Connection conn = getConnection();
	         PreparedStatement ps = conn.prepareStatement(query);
	         ResultSet rs = ps.executeQuery()) {

	        if (rs.next()) {
	            status = rs.getString("LEGACYEXPORTSTATUS") != null ? rs.getString("LEGACYEXPORTSTATUS") : "0";
	        }

	    } catch (Exception ex) {
	        System.out.println("WARN: Error fetching legacy export status -> " + ex.getMessage());
	        ex.printStackTrace();
	    }

	    return status;
	}
   
public boolean populateFinacleTemp(int ps_status,String ThirdPartyLabel)throws Exception{

    boolean suc = false;
    Connection conn = null;
    PreparedStatement statement = null;
String current_date= getCurrentDate();
String narration = null;
Date processing_date =null;
PersistenceServiceDAO psdao = new PersistenceServiceDAO();
 conn = getConnection();
 statement = conn.prepareStatement("select processing_date from am_gb_company");
 ResultSet re = statement.executeQuery();
//ResultSet re = getStatement().executeQuery("select processing_date from am_gb_company");

if(re.next()){
processing_date = re.getDate(1);//.getString(1);
 }

freeResource();
String describe = "DEPRECIATON FOR THE MONTH OF ";


//001**DEPTN NOVEMBER 2024**FAS

 //System.out.println("the value of processing date is " + formatDate(processing_date) );
narration = describe + getMonthPartOfDate(formatDate(processing_date)).toUpperCase() + " " + getYearPartOfDate(formatDate(processing_date));
//System.out.println("i am herejjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj" + narration);
//    System.out.println("the current date" + current_date);
if(ThirdPartyLabel.equals("ZENITH")) {describe = "DEPTN ";
String transCode = "**FAS";
narration = describe + getMonthPartOfDate(formatDate(processing_date)).toUpperCase() + " " + getYearPartOfDate(formatDate(processing_date)+transCode);
}

if(ps_status == 1){
 
  
try{
	AssetRecordsBean arb = new AssetRecordsBean();
    getStatement().executeUpdate("Insert into finacle_temp(dr_acct,cr_acct,amount,value_date,narration,narration2) select dr_acct,cr_acct,amount,value_date,narration,narration2 from finacle_ext");
//    System.out.println("i am herekkkkkkkkkkkkkkkkkk");
//getStatement().executeUpdate("TRUNCATE table finacle_ext insert into finacle_ext(type,dr_acct,cr_acct,amount,value_date) select c.category_name,d.country_prefix + d.dr_prefix + b.branch_code + c.Asset_Ledger,d.country_prefix + d.cr_prefix + b.branch_code + c.Dep_ledger,sum(a.monthly_dep),d.processing_date from am_ad_branch b,am_ad_category c,am_asset a,am_gb_company d where a.category_code = c.category_code And a.branch_code = b.branch_code and c.Asset_Ledger <> '" +" "+"' group by c.category_name,b.branch_code,c.Asset_Ledger,c.Dep_ledger,d.processing_date,d.country_prefix,d.dr_prefix,d.cr_prefix order by b.branch_code asc");
    /*
getStatement().executeUpdate("TRUNCATE table finacle_ext insert into finacle_ext(type,dr_acct," +
        "cr_acct,amount,value_date) select c.category_name,d.country_prefix + d.dr_prefix + b.branch_code +" +
        " c.Asset_Ledger,d.country_prefix + d.cr_prefix + b.branch_code + c.Dep_ledger,sum(a.monthly_dep)," +
        "d.processing_date from am_ad_branch b,am_ad_category c,am_asset a,am_gb_company d where " +
        "a.category_code = c.category_code And a.branch_code = b.branch_code and a.asset_status ='active' " +
        "and a.cost_price > d.cost_threshold and a.dep_rate > 0 and c.Asset_Ledger <> '" +" "+"' " +
        "group by c.category_name,b.branch_code,c.Asset_Ledger,c.Dep_ledger,d.processing_date,d.country_prefix," +
        "d.dr_prefix,d.cr_prefix order by b.branch_code asc");
*/

     String script = approv.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where THIRDPARTY = '"+ThirdPartyLabel+"' AND type = 'DEPRECIATION'");
//    getStatement().executeUpdate(script);
     arb.updateAssetStatusChange(script);

//if(ThirdPartyLabel.equalsIgnoreCase("K2")){  
//getStatement().executeUpdate("TRUNCATE table finacle_ext insert into finacle_ext(type,SBU_CODE,dr_acct,cr_acct,amount,value_date) " +
//        "select c.category_name,a.SBU_CODE,substring(b.BRANCH_CODE,3,3)+d.country_prefix+c.Dep_ledger," +
//        "substring(b.BRANCH_CODE,3,3)+d.country_prefix+c.Accum_Dep_ledger, " +
//        "sum(a.monthly_dep)+SUM(coalesce(a.IMPROV_MONTHLYDEP,0)),d.processing_date from " +
//        "am_ad_branch b,am_ad_category c,am_asset a,am_gb_company d where a.category_code = c.category_code " +
//        "And a.branch_code = b.branch_code and a.asset_status ='active' and a.cost_price > d.cost_threshold and " +
//        "a.dep_rate > 0 and c.Accum_Dep_ledger <> '" +" "+"' and a.Req_Redistribution = 'N' group by c.category_name,a.SBU_CODE,b.branch_code," +
//        "c.Accum_Dep_ledger,c.Dep_ledger,d.processing_date,d.country_prefix,d.dr_prefix,d.cr_prefix " +
//        "UNION " +        
//		"select c.category_name,a.SBU_CODE,substring(b.BRANCH_CODE,3,3)+d.country_prefix+e.DIST_EXP_ACCT, " +
//		"substring(b.BRANCH_CODE,3,3)+d.country_prefix+e.DIST_ACCUM_ACCT,sum(a.monthly_dep*e.VALUE_ASSIGNED/100)+SUM(coalesce(a.IMPROV_MONTHLYDEP,0)*e.VALUE_ASSIGNED/100),  " +
//		"d.processing_date from am_ad_branch b,am_ad_category c,am_asset a,am_gb_company d, AM_DEPR_DIST e " +
//		"where a.category_code = c.category_code And a.branch_code = b.branch_code and a.asset_status ='active' " +
//		"and a.cost_price > d.cost_threshold and a.dep_rate > 0 and c.Accum_Dep_ledger <> '" +" "+"' and a.Asset_id = e.ASSET_ID " +
//		" and a.Req_Redistribution = 'N' group by c.category_name,a.SBU_CODE,b.branch_code,e.DIST_EXP_ACCT,e.DIST_ACCUM_ACCT,d.processing_date,d.country_prefix, " +
//		"d.dr_prefix,d.cr_prefix ");
//}else{
//
//getStatement().executeUpdate("TRUNCATE table finacle_ext insert into finacle_ext(type,SBU_CODE,dr_acct,cr_acct,amount,value_date) " +
//        "select c.category_name,a.SBU_CODE,d.country_prefix + d.dr_prefix + b.branch_code + c.Dep_ledger,d.country_prefix + " +
//        "d.cr_prefix + b.branch_code + c.Accum_Dep_ledger,sum(a.monthly_dep)+SUM(coalesce(a.IMPROV_MONTHLYDEP,0)),d.processing_date from " +
//        "am_ad_branch b,am_ad_category c,am_asset a,am_gb_company d where a.category_code = c.category_code " +
//        "And a.branch_code = b.branch_code and a.asset_status ='active' and a.cost_price > d.cost_threshold and " +
//        "a.dep_rate > 0 and c.Accum_Dep_ledger <> '" +" "+"' and a.Req_Redistribution = 'N' group by c.category_name,a.SBU_CODE,b.branch_code," +
//        "c.Accum_Dep_ledger,c.Dep_ledger,d.processing_date,d.country_prefix,d.dr_prefix,d.cr_prefix " +
//        "UNION " +        
//		"select c.category_name,a.SBU_CODE,d.country_prefix + d.dr_prefix + b.branch_code + e.DIST_EXP_ACCT,d.country_prefix + " +
//		"d.cr_prefix + b.branch_code + e.DIST_ACCUM_ACCT,sum(a.monthly_dep*e.VALUE_ASSIGNED/100)+SUM(coalesce(a.IMPROV_MONTHLYDEP,0)*e.VALUE_ASSIGNED/100), " +
//		"d.processing_date from am_ad_branch b,am_ad_category c,am_asset a,am_gb_company d, AM_DEPR_DIST e " +
//		"where a.category_code = c.category_code And a.branch_code = b.branch_code and a.asset_status ='active' " +
//		"and a.cost_price > d.cost_threshold and a.dep_rate > 0 and c.Accum_Dep_ledger <> '" +" "+"' and a.Asset_id = e.ASSET_ID " +
//		" and a.Req_Redistribution = 'N' group by c.category_name,a.SBU_CODE,b.branch_code,e.DIST_EXP_ACCT,e.DIST_ACCUM_ACCT,d.processing_date,d.country_prefix, " +
//		"d.dr_prefix,d.cr_prefix ");
//}


String query ="update finacle_ext set narration=?,system_date=?,narration2=?";
//System.out.println("THE UPDATE WAS SUCCESSFUL FOR query " + query);
    Connection con = getConnection();
PreparedStatement ps = con.prepareStatement(query);
ps.setString(1, narration);
ps.setDate(2, psdao.dateConvert(current_date));
ps.setString(3, narration);
int i =ps.executeUpdate();
if(i != -1){System.out.println("THE UPDATE WAS SUCCESSFUL");}
//getStatement().executeUpdate("update finacle_ext set narration='" + narration + "',system_date='"+current_date+"',narration2='" + narration + "'");
}catch(Exception e){e.getMessage();
}finally{freeResource();}


suc = true;
freeResource();
}else{suc = false;}//if

return suc;
}


public String formatDate(Date date){
String output = null;
    try {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    output = sdf.format(date);
       }catch(Exception e){e.printStackTrace();}
return output;
}


public String getMonthPartOfDate(String date) {
String output = null;
    try {SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
Date myDate = sdf.parse(date);
java.util.Calendar cal = java.util.Calendar.getInstance();
cal.setTime(myDate);
int month = cal.get(Calendar.MONTH);

String[] monthName = new String[]{"January","February","March","April","May","June","July","August","September","October","November","December"};
 output = monthName[month];
}catch(Exception e){e.printStackTrace();}
return output;

}

public String getYearPartOfDate(String date) {
String output = null;
    try {SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
Date myDate = sdf.parse(date);
java.util.Calendar cal = java.util.Calendar.getInstance();
cal.setTime(myDate);
output = Integer.toString(cal.get(Calendar.YEAR));


}catch(Exception e){e.printStackTrace();}
return output;

}


public String getCurrentDate(){
   String output = null;
PersistenceServiceDAO psdao = null;
 try {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
Date date = new Date();
   output = sdf.format(date);//psdao.dateConvert(date.toString());
    // System.out.println("the pre output is " + sdf.toPattern());
}catch(Exception e){e.printStackTrace();}

return output;


    }



  /*  public String getWhRate() throws Exception {
        ResultSet rs = getStatement().executeQuery(
                "SELECT WHT_RATE,FED_WHT_RATE FROM AM_GB_COMPANY");
        String vat="0";

        if (rs.next()) {
           vat= vat + rs.getInt("WHT_RATE");
	vat= vat+"_"+rs.getString("FED_WHT_RATE");
        }

        freeResource();
        return vat;

    }

*/





	
	
	/*
	//Ganiyu's Code
	public String getWhRateValues() throws Exception{
		ResultSet rs1 = getStatement().executeQuery("SELECT WHT_RATE,FED_WHT_RATE FROM AM_GB_COMPANY");
		String rv ="";
		if(rs1.next()){
		rv = rv + rs1.getDouble(1);
		rv = rv + "_" + rs1.getDouble(2);
		}
		freeResource();
		return rv;

	}//getWhRateValues


	*/
    /**
     * getYesNoForCombo
     *
     * @return String[][]
     */
    public String[][] getYesNoForCombo() {
        String[][] a = new String[2][3];

        a[0][0] = "Y";
        a[0][1] = "";
        a[0][2] = "Yes";

        a[1][0] = "N";
        a[1][1] = "";
        a[1][2] = "No";

        return a;
    }


//Ganiyu's Code
public String[][] getFederalStateNoneForCombo(){
String[][] a= new String[3][3];
a[0][0]="N";
a[0][1]="";
a[0][2]="None";

a[1][0]="S";
a[1][1]="";
a[1][2]="State";

a[2][0]="F";
a[2][1]="";
a[2][2]="Federal";

return a;


}


public String[][] getApproveRejectPendingForCombo(){
String[][] a= new String[3][3];
a[0][0]="P";
a[0][1]="";
a[0][2]="Pending";

a[1][0]="R";
a[1][1]="";
a[1][2]="Reject";

a[2][0]="A";
a[2][1]="";
a[2][2]="Approve";

return a;


}

    public String[][] getBranchesForComboOld(String code,String branchRestrict) throws Exception {
    	Connection conn = null;
    	ResultSet rs = null;
    	ResultSet rsc = null;
    	try{
         String query = "";
         if(branchRestrict.equalsIgnoreCase("N")){
          query = "SELECT * FROM AM_AD_BRANCH	" +
                       "WHERE BRANCH_STATUS = 'ACTIVE'  " +
                       "ORDER BY BRANCH_NAME ASC";
         }else{
          query = "SELECT * FROM AM_AD_BRANCH	" +
                       "WHERE BRANCH_STATUS = 'ACTIVE' AND BRANCH_ID = '"+code+"' " +
                       "ORDER BY BRANCH_NAME ASC";
         }
        String counterQuery = "SELECT count(*) FROM AM_AD_BRANCH	" +
                              "WHERE BRANCH_STATUS = 'ACTIVE'  ";

//        ResultSet rs = getStatement().executeQuery(query);
//        ResultSet rsc = getStatement().executeQuery(counterQuery);
        conn = getConnection();
        rs = conn.prepareStatement(query).executeQuery();
        rsc = conn.prepareStatement(counterQuery).executeQuery();
        rsc.next();
        int count = rsc.getInt(1);
        if (count > 0) {

            String[][] a = new String[count][12];
            int i = 0;
            while (rs.next()) {
                for (int j = 0; j < 12; j++) {
                    a[i][j] = rs.getString(j + 1);
                }
                i++;
            }
           // freeResource();
            return a;
        }
    	}catch(Exception e){
    		e.getMessage();
    	}finally{
    		conn.close();
    	}
        //freeResource();
        return null;
    }
    
    public String[][] getBranchesForCombo(String code, String branchRestrict) throws Exception {
        String[][] result = null;

        String queryAll = "SELECT * FROM AM_AD_BRANCH WHERE BRANCH_STATUS = 'ACTIVE' ORDER BY BRANCH_NAME ASC";
        String queryRestricted = "SELECT * FROM AM_AD_BRANCH WHERE BRANCH_STATUS = 'ACTIVE' AND BRANCH_ID = ? ORDER BY BRANCH_NAME ASC";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(branchRestrict.equalsIgnoreCase("N") ? queryAll : queryRestricted)) {

            if (!branchRestrict.equalsIgnoreCase("N")) {
                ps.setString(1, code);
            }

            try (ResultSet rs = ps.executeQuery()) {
               
                rs.last();
                int rowCount = rs.getRow();
                rs.beforeFirst(); 

                if (rowCount > 0) {
                    int columnCount = rs.getMetaData().getColumnCount();
                    result = new String[rowCount][columnCount];
                    int i = 0;
                    while (rs.next()) {
                        for (int j = 0; j < columnCount; j++) {
                            result[i][j] = rs.getString(j + 1);
                        }
                        i++;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("WARN: Error fetching branches -> " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }
}
