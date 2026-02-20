package legend.admin.handlers;

import audit.AuditTrailGen;

import com.magbel.legend.vao.PasswordHistory;
import com.magbel.util.ApplicationHelper;
import com.magbel.util.CryptManager;
import com.magbel.util.Cryptomanager;
import com.magbel.util.DataConnect;
import com.magbel.util.DatetimeFormat;
import com.magbel.util.HtmlUtility;
import com.mindcom.security.Licencer;
import com.magbel.legend.bus.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.security.Key;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.json.JSONException;
import org.json.JSONObject;

import legend.admin.objects.ClassPrivilege;
import legend.admin.objects.Privilege;
import legend.admin.objects.SecurityClass;
import legend.admin.objects.SecurityRerouteClass;
import legend.admin.objects.Staffs;
import legend.admin.objects.SupervisorDetailsClass;
import legend.admin.objects.User;
import legend.admin.objects.UserDisableClass;
import legend.admin.objects.UserEnableClass;
import ng.com.magbel.token.ParallexTokenClass;
import ng.com.magbel.token.TokenAuthentication;
import ng.com.magbel.token.TokenAuthenticationSoap;
//import ng.com.magbel.token.TokenAuthentication;
//import ng.com.magbel.token.TokenAuthenticationSoap;
import ng.com.magbel.token.TokenClass;
import ng.com.magbel.token.ZenithTokenClass;
public class SecurityHandler
{

    Connection con;
    Statement stmt;
    PreparedStatement ps;
    ResultSet rs;
    DataConnect dc;
    TokenClass tok;
    SimpleDateFormat sdf;
    final String space = "  ";
    final String comma = ",";
    Date date;
    DatetimeFormat df;
//    Cryptomanager cm;
    CryptManager cm;
    ApprovalRecords records;
  
    private static final String ALGO = "AES";
    
    public SecurityHandler()
    {
        con = null;
        stmt = null;
        ps = null;
        rs = null; 
        cm = null;
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        df = new DatetimeFormat();
        tok = new TokenClass();
        records = new ApprovalRecords();
    //    System.out.println((new StringBuilder()).append("USING_ ").append(getClass().getName()).toString());
    }

    public java.util.List getAllPrivileges() {
        java.util.List _list = new java.util.ArrayList();
        legend.admin.objects.Privilege privilege = null;
        String query = "SELECT role_uuid,role_name,role_wurl"
                + " ,menu_type,priority" + "  FROM am_ad_privileges order by role_name  ";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        CallableStatement cstmt = null;
        double averageWeight = 0;
        try {
            con = getConnection();
            
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                String role_uuid = rs.getString("role_uuid");
                String role_name = rs.getString("role_name");
                String role_wurl = rs.getString("role_wurl");
                String menu_type = rs.getString("menu_type");
                String priority = rs.getString("priority");
                privilege = new legend.admin.objects.Privilege(role_uuid,
                        role_name, role_wurl, menu_type, priority);
                _list.add(privilege);

            }
 //           closeConnection(con, ps);
        } catch (Exception ex) {
            System.out.println("WARN: Error fetching all asset ->" + ex);
        } finally {
            closeConnection(con, ps);
        }
        return _list;

    }
    public int tokenLogin(String userName, String token, String name) throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException
    {
    	boolean response = false;
//    	System.out.println("<<<<<=======userName in tokenLogin Method: "+userName+"    token: "+token);
    	// Kazeem
    	/*
        TokenAuthentication token = new TokenAuthentication();
        TokenAuthenticationSoap soap = token.getTokenAuthenticationSoap();
        Properties prop = new Properties();
        File file = new File("C:\\Property\\LegendPlus.properties");
        FileInputStream input = new FileInputStream(file);
        prop.load(input);

        String appId = prop.getProperty("AppID");
        String appKey = prop.getProperty("AppKey");
*/
//        if(password != null)
//        {
//            password = password.trim();
//        }
        String sub = "";        
//        if(userName != null)
//        {
//            sub = userName.substring(3);
//        }
//        System.out.println((new StringBuilder()).append("Password Supplied ==").append(password).append("   username is now==").append(userName));
//     //   boolean response = soap.authenticateToken(sub, password,"TestID","087f7101089b76ce079e3725276d91d1a30872ba");
//        boolean response = tok.callSoapWebService(userName, password);
//        System.out.println((new StringBuilder()).append("The response after the call to web service=====").append(response).toString());
//        return !response ? 0 : 1;
//        String full_Name = email(name);
//        System.out.println((new StringBuilder()).append("Password Supplied ==").append(password).append("   username is now==").append(userName).append(" after subsctring=").append(sub).toString());
//        //boolean response = soap.authenticateToken(userName, password,appId,appKey);
////        boolean response = TokenClass.callSoapWebService(userName, password);
//        response = TokenClass.callSoapWebService(full_Name, token, endpointUrl, digiPassEndPointUrl, digiPassName, username, digiPassResponseName );
//        System.out.println((new StringBuilder()).append("The response after the call to web service=====").append(response).toString());
//        return !response ? 0 : 1; 
        
        Properties prop = new Properties();
        File file = new File("C:\\Property\\LegendPlus.properties");
        FileInputStream input = new FileInputStream(file);
        prop.load(input);
   	  
        String endpointUrl = prop.getProperty("endPointUrl");
        String digiPassName =  prop.getProperty("digiPassName");
   	 String vascoName =  prop.getProperty("vascoName");
   	 String digiPassResponseName =  prop.getProperty("digiPassResponseName");
   	 String vascoResponseName = prop.getProperty("vascoResponseName");
//   	 String staffIdName = "staffId";
//   	 String username = "Username";
   	String staffIdName = prop.getProperty("staffIdName");
  	 String username = prop.getProperty("username");
        String vascoEndPointUrl = prop.getProperty("vascoEndPointUrl");
        String ThirdPartyLabel = prop.getProperty("ThirdPartyLabel");
        String ParallexApiUrl = prop.getProperty("ParallexApiUrl");
        
       // System.out.println("The url is : " + vascoEndPointUrl); 
        
        System.out.println((new StringBuilder()).append("Token Supplied ==").append(token).append("   username is now==").append(userName).append(" after subsctring=").append(sub).toString());
        //boolean response = soap.authenticateToken(userName, password,appId,appKey);
        if(ThirdPartyLabel.equalsIgnoreCase("INTEGRIFY")){
        response = TokenClass.callSoapWebService(userName, token, endpointUrl, vascoEndPointUrl, vascoName, staffIdName, vascoResponseName); 
//        System.out.println((new StringBuilder()).append("The response after the call to web service=====").append(response).toString());
        if(response == false){
        	 String digiPassEndPointUrl = prop.getProperty("digiPassEndPointUrl");
           //  System.out.println("The url is : " + digiPassEndPointUrl); 
//             SecurityHandler handler = new SecurityHandler();
            String str = email(name);
//            System.out.println("======str: " + str); 
            String[] arrOfStr = str.split("@");
            String full_Name = arrOfStr[0]; 
//            System.out.println("Token Full Name: " + full_Name); 
//            System.out.println("arrOfStr[1]: " + arrOfStr[1]); 
        	 response = TokenClass.callSoapWebService(full_Name, token, endpointUrl, digiPassEndPointUrl, digiPassName, username, digiPassResponseName );
             System.out.println((new StringBuilder()).append("The response after the call to second web service=====").append(response).toString());
             if(response == false){
//            	 System.out.println("Invalid Token!!!"); 
             }else{
            	 response = true;
             }
        }else{
        	response = true;
        }
        
    }
        if(ThirdPartyLabel.equalsIgnoreCase("K2")){
        	 String status = ParallexTokenClass.tokenValidation(userName,token);
        	 JSONObject json = new JSONObject(status);
		     String respCode = json.getString("ResponseCode");
		          
//		     System.out.println("The response code is : " + respCode);
		     
		     if(respCode.equals("00")){
		    	 response =true;
		     }
        	
        }
         
        if(ThirdPartyLabel.equalsIgnoreCase("ZENITH")){
            response = ZenithTokenClass.callSoapWebService(userName, token, endpointUrl, vascoEndPointUrl, vascoName, staffIdName, vascoResponseName); 
//            System.out.println((new StringBuilder()).append("The response after the call to web service=====").append(response).toString());
            if(response == false){
            	 String digiPassEndPointUrl = prop.getProperty("digiPassEndPointUrl");
               //  System.out.println("The url is : " + digiPassEndPointUrl); 
//                 SecurityHandler handler = new SecurityHandler();
                String str = email(name);
//                System.out.println("======str: " + str); 
                String[] arrOfStr = str.split("@");
                String full_Name = arrOfStr[0]; 
//                System.out.println("Token Full Name: " + full_Name); 
//                System.out.println("arrOfStr[1]: " + arrOfStr[1]); 
            	 response = ZenithTokenClass.callSoapWebService(full_Name, token, endpointUrl, digiPassEndPointUrl, digiPassName, username, digiPassResponseName );
                 System.out.println((new StringBuilder()).append("The response after the call to second web service=====").append(response).toString());
                 if(response == false){
//                	 System.out.println("Invalid Token!!!"); 
                 }else{
                	 response = true;
                 }
            }else{
            	response = true;
            }
            
        }
        
        return !response ? 0 : 1; 
          
    }

    public int tokenLogin(String userName, String password)
    {
    	// Kazeem 
        TokenAuthentication token = new TokenAuthentication();
        TokenAuthenticationSoap soap = token.getTokenAuthenticationSoap();
        if(password != null)
        {
            password = password.trim();
        }
        String sub = "";
        if(userName != null)
        {
            sub = userName.substring(3);
        }
//       System.out.println((new StringBuilder()).append("Password Supplied ==").append(password).append("   username is now==").append(userName).append(" after subsctring=").append(sub).toString());
        boolean response = soap.authenticateToken(sub, password,"TestID","087f7101089b76ce079e3725276d91d1a30872ba");
 //       System.out.println((new StringBuilder()).append("The response after the call to web service=====").append(response).toString());
        return !response ? 0 : 1;
    }

    
    public String email(String userName){
    	String response =null;
    	 Connection con;
         PreparedStatement ps;
         ResultSet rs;
       
         con = null;
         ps = null;
         rs = null;
         try
         {
        	 String query = "Select email from AM_GB_USER WHERE user_name =?";
             con = getConnection();
             ps = con.prepareStatement(query);
             ps.setString(1, userName);
             rs = ps.executeQuery();
             while(rs.next()){
            	 String email = rs.getString(1);
            	 String str = email;
	         		String[] arrOfStr = str.split("@fcmb.com");
	         		for (String fullName : arrOfStr){
//	         			System.out.println("The full name is : " + fullName);
	         			response = fullName;
	         		}
             }
             
         }catch(Exception e){
        	 e.getMessage();
         }finally {
             closeConnection(con, ps, rs);
         }
    	return response;
    }

    
    public legend.admin.objects.ClassPrivilege getClassPrivilege(
            String classid, String roleuuid) {
        legend.admin.objects.ClassPrivilege classprivilege = null;

        String query = "SELECT clss_uuid,role_uuid,role_view,role_addn,role_edit"
                + " FROM am_ad_class_privileges"
                + " WHERE clss_uuid=? AND role_uuid=?";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, classid);
            ps.setString(2, roleuuid);
            rs = ps.executeQuery();
            while (rs.next()) {
                String clss_uuid = rs.getString("clss_uuid");

                String role_uuid = rs.getString("role_uuid");

                String role_view = rs.getString("role_view");

                String role_addn = rs.getString("role_addn");

                String role_edit = rs.getString("role_edit");
                classprivilege = new legend.admin.objects.ClassPrivilege(
                        clss_uuid, role_uuid, role_view, role_addn, role_edit);
            }
 //           closeConnection(con, ps);
        } catch (Exception ex) {
            System.out.println("WARN: Error fetching Class Privileges ->" + ex);
        } finally {
            closeConnection(con, ps);
        }
        return classprivilege;

    }

    public boolean removeClassPrivilege(java.util.ArrayList list) {
        String query = "DELETE FROM am_ad_class_privileges"
                + " WHERE clss_uuid=? AND role_uuid=?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        legend.admin.objects.ClassPrivilege cp = null;
        int[] d = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            for (int i = 0; i < list.size(); i++) {
                cp = (legend.admin.objects.ClassPrivilege) list.get(i);

                ps.setString(1, cp.getClss_uuid());
                ps.setString(2, cp.getRole_uuid());

                ps.addBatch();
            }
            d = ps.executeBatch();
//            closeConnection(con, ps);
        } catch (Exception ex) {
            System.out.println("WARN: Error removing Class Privileges->" + ex);
        } finally {
            closeConnection(con, ps);
        }
        return (d.length > 0);   
    }

        public boolean insertClassPrivileges(java.util.ArrayList list) {
            String query = "INSERT INTO am_ad_class_privileges(clss_uuid,role_uuid"
                    + " ,role_view,role_addn ,role_edit)"
                    + " VALUES(?,?,?,?,?)";
            Connection con = null;
            PreparedStatement ps = null;
            legend.admin.objects.ClassPrivilege cp = null;
            int[] d = null;
            try {
                con = getConnection();
                ps = con.prepareStatement(query);

                for (int i = 0; i < list.size(); i++) {
                    cp = (legend.admin.objects.ClassPrivilege) list.get(i);
                    ps.setString(1, cp.getClss_uuid());
                    ps.setString(2, cp.getRole_uuid());
                    ps.setString(3, cp.getRole_view());
                    ps.setString(4, cp.getRole_addn());
                    ps.setString(5, cp.getRole_edit());
                    ps.addBatch();
                }
                d = ps.executeBatch();
//                closeConnection(con, ps);
            } catch (Exception ex) {
                System.out.println("WARN: Error fetching all asset ->" + ex);
            } finally {
                closeConnection(con, ps);
            }

            return (d.length > 0);
        }

    public legend.admin.objects.SecurityClass getSecurityClassById(String filter) {

        legend.admin.objects.SecurityClass sc = null;
        String query = "SELECT class_id ,class_desc,class_name ,is_supervisor"
                + "  ,class_status,user_id,create_date,fleet_admin,facility_admin,store_admin"
                + "  FROM am_gb_class WHERE  class_id= ?";

        Connection c = null;
        ResultSet rs = null;
        PreparedStatement s = null;

        try {
            c = getConnection();
            s = c.prepareStatement(query);
            s.setString(1, filter);
            rs = s.executeQuery();
            
            while (rs.next()) {
                String classId = rs.getString("class_id");
                String description = rs.getString("class_desc");

                String className = rs.getString("class_name");

                String isSupervisor = rs.getString("is_supervisor");

                String classStatus = rs.getString("class_status");

                String userId = rs.getString("user_id");

                String createDate = rs.getString("create_date");

                String fleetAdmin = rs.getString("fleet_admin");
                
                String facilityAdmin = rs.getString("facility_admin");
                
                String storeAdmin = rs.getString("store_admin");
                sc = new legend.admin.objects.SecurityClass(classId,
                        description, className, isSupervisor, classStatus,
                        userId, createDate, fleetAdmin,facilityAdmin,storeAdmin);

            }
//            closeConnection(c, s, rs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(c, s, rs);
        }
        return sc;

    }

    public legend.admin.objects.SecurityClass getSecurityClassByName(String filter) {

        legend.admin.objects.SecurityClass sc = null;
        String query = "SELECT class_id ,class_desc,class_name ,is_supervisor"
                + "  ,class_status,user_id,create_date,fleet_admin,facility_admin,store_admin"
                + "  FROM am_gb_class WHERE  class_name= ? ";

        Connection c = null;
        ResultSet rs = null;
        PreparedStatement s = null;

        try {
            c = getConnection();
            s = c.prepareStatement(query);
            s.setString(1, filter);
            rs = s.executeQuery();
            while (rs.next()) {
                String classId = rs.getString("class_id");
                String description = rs.getString("class_desc");

                String className = rs.getString("class_name");

                String isSupervisor = rs.getString("is_supervisor");

                String classStatus = rs.getString("class_status");

                String userId = rs.getString("user_id");

                String createDate = rs.getString("create_date");

                String fleetAdmin = rs.getString("fleet_admin");
                
                String facilityAdmin = rs.getString("facility_admin");
                
                String storeAdmin = rs.getString("store_admin");
                sc = new legend.admin.objects.SecurityClass(classId,
                        description, className, isSupervisor, classStatus,
                        userId, createDate, fleetAdmin,facilityAdmin,storeAdmin);

            }
//            closeConnection(c, s, rs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(c, s, rs);
        }
        return sc;

    }

    public boolean updateSecurityClass(legend.admin.objects.SecurityClass sc) {

        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;
        String query = "UPDATE am_gb_class SET class_desc = ?,class_name = ?"
                + "   ,is_supervisor = ?,class_status = ?"
                + "   ,fleet_admin = ?,facility_admin =?,store_admin=?" + "   WHERE class_id = ?";

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, sc.getDescription());
            ps.setString(2, sc.getClassName());
            ps.setString(3, sc.getIsSupervisor());
            ps.setString(4, sc.getClassStatus());
            ps.setString(5, sc.getFleetAdmin());
            ps.setString(6, sc.getFacilityAdmin());
            ps.setString(7, sc.getStoreAdmin());
            ps.setString(8, sc.getClassId());
            done = (ps.executeUpdate() != -1);
 //           closeConnection(con, ps);
        } catch (Exception e) {
            System.out.println(this.getClass().getName()
                    + " ERROR:Error Updating Security Class ->"
                    + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }

    public boolean createSecurityClass(legend.admin.objects.SecurityClass sc) {

        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;
        String query = "INSERT INTO am_gb_class(class_desc,class_name,is_supervisor,class_status"
                + "    ,user_id,create_date ,fleet_admin,class_id,facility_admin,store_admin)"
                + "    VALUES (?,?,?,?,?,?,?,?,?,?)";

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, sc.getDescription());
            ps.setString(2, sc.getClassName());
            ps.setString(3, sc.getIsSupervisor());
            ps.setString(4, sc.getClassStatus());
            ps.setString(5, sc.getUserId());
            ps.setDate(6, df.dateConvert(new java.util.Date()));
            ps.setString(7, sc.getFleetAdmin());
            //ps.setLong(8, System.currentTimeMillis());
            ps.setString(8, new ApplicationHelper().getGeneratedId("am_gb_class"));
            ps.setString(9, sc.getFacilityAdmin());
            ps.setString(10, sc.getStoreAdmin());

            done = (ps.executeUpdate() != -1);
 //           closeConnection(con, ps);
        } catch (Exception e) {
            System.out.println(this.getClass().getName()
                    + " ERROR:Error Updating Security Class ->"
                    + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }

    public java.util.List getSecurityClassByQuery(String filter) {
        java.util.List _list = new java.util.ArrayList();
        legend.admin.objects.SecurityClass sc = null;
        String query = "SELECT class_id ,class_desc,class_name ,is_supervisor"
                + "  ,class_status,user_id,create_date,fleet_admin,facility_admin,store_admin"
                + "  FROM am_gb_class WHERE CLASS_STATUS = ?";

//        query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        PreparedStatement s = null;

        try {
            c = getConnection();
            s = c.prepareStatement(query);
            s.setString(1, filter);
            rs = s.executeQuery();
            while (rs.next()) {
                String classId = rs.getString("class_id");
                String description = rs.getString("class_desc");

                String className = rs.getString("class_name");

                String isSupervisor = rs.getString("is_supervisor");

                String classStatus = rs.getString("class_status");

                String userId = rs.getString("user_id");

                String createDate = rs.getString("create_date");

                String fleetAdmin = rs.getString("fleet_admin");
                
                String facilityAdmin = rs.getString("facility_admin");
                
                String storeAdmin = rs.getString("store_admin");
                sc = new legend.admin.objects.SecurityClass(classId,
                        description, className, isSupervisor, classStatus,
                        userId, createDate, fleetAdmin,facilityAdmin,storeAdmin);

                _list.add(sc);
            }
//            closeConnection(c, s, rs);
        } catch (Exception e) {
            System.out.println("this error is generated in getSecurityClassByQuery(String filter) of securityHandler class");
            e.printStackTrace();

        } finally {
            closeConnection(c, s, rs);
        }
        return _list;

    } 

  
    public boolean updateClassPrivilege(java.util.ArrayList list) {
        String query = "UPDATE am_ad_class_privileges SET "
                + "role_view = ?,role_addn = ?,role_edit = ?"
                + " WHERE clss_uuid=? AND role_uuid=?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        legend.admin.objects.ClassPrivilege cp = null;
        int[] d = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);

            for (int i = 0; i < list.size(); i++) {
                cp = (legend.admin.objects.ClassPrivilege) list.get(i);
                ps.setString(1, cp.getRole_view());
                ps.setString(2, cp.getRole_addn());
                ps.setString(3, cp.getRole_edit());
                ps.setString(4, cp.getClss_uuid());
                ps.setString(5, cp.getRole_uuid());

                ps.addBatch();
            }
            d = ps.executeBatch();
//            closeConnection(con, ps);
        } catch (Exception ex) {
            System.out.println("WARN: Error fetching all asset ->" + ex);
        } finally {
            closeConnection(con, ps);
        }

        return (d.length > 0);
    }

    public java.util.List getAllUsers() {
        java.util.List _list = new java.util.ArrayList();
        legend.admin.objects.User user = null;
        String query = "SELECT User_Id, User_Name, Full_Name"
                + ", Legacy_Sys_Id, Class, Branch, Password"
                + ", Phone_No, is_Supervisor, Must_Change_Pwd"
                + ", Login_Status, User_Status, UserId, Create_Date"
                + ", Password_Expiry, Login_Date, Login_System, Fleet_Admin"
                + ", email, Branch,password_changed,region_code,zone_code,region_restriction,zone_restriction,Facility_Admin,Store_Admin" + " FROM AM_GB_USER";

        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String userId = rs.getString("User_Id");
                String userName = rs.getString("User_Name");
                String fullName = rs.getString("Full_Name");
                String legacySysId = rs.getString("Legacy_Sys_Id");
                String Class = rs.getString("Class");
                String branch = rs.getString("Branch");
                String password = rs.getString("Password");
                String phoneNo = rs.getString("Phone_No");
                String isSupervisor = rs.getString("Is_Supervisor");
                String mustChangePwd = rs.getString("Must_Change_Pwd");
                String loginStatus = rs.getString("Login_Status");
                String userStatus = rs.getString("User_Status");
                String user_Id = rs.getString("UserId");
                String createDate = rs.getString("Create_Date");
                String passwordExpiry = rs.getString("Password_Expiry");
                String loginDate = rs.getString("Login_Date");
                String loginSystem = rs.getString("Login_System");
                String fleetAdmin = rs.getString("Fleet_Admin");
                String email = rs.getString("email");
                String branchCode = rs.getString("Branch");
                String pwdChanged = rs.getString("password_changed");
                String regionCode = rs.getString("region_code");
                String zoneCode = rs.getString("zone_code");
                String regionRestrict = rs.getString("region_restriction");
                String zoneRestrict = rs.getString("zone_restriction");
                String facilityAdmin = rs.getString("Facility_Admin");
                String storeAdmin = rs.getString("Store_Admin");
                
                user = new legend.admin.objects.User();
                user.setUserId(userId);
                user.setUserName(userName);
                user.setUserFullName(fullName);
                user.setLegacySystemId(legacySysId);
                user.setUserClass(Class);
                user.setBranch(branch);
                user.setPassword(password);
                user.setPhoneNo(phoneNo);
                user.setIsSupervisor(isSupervisor);
                user.setMustChangePwd(mustChangePwd);
                user.setLoginStatus(loginStatus);
                user.setCreatedBy(user_Id);
                user.setCreateDate(createDate);
                user.setPwdExpiry(passwordExpiry);
                user.setLastLogindate(loginDate);
                user.setLoginSystem(loginSystem);
                user.setFleetAdmin(fleetAdmin);
                user.setEmail(email);
                user.setBranch(branchCode);
                user.setUserStatus(userStatus);
                user.setPwdChanged(pwdChanged);
                user.setRegionCode(regionCode);
                user.setZoneCode(zoneCode);
                user.setRegionRestrict(regionRestrict);
                user.setZoneRestrict(zoneRestrict);
                user.setIsFacilityAdministrator(facilityAdmin);
                user.setIsStoreAdministrator(storeAdmin);
                _list.add(user);
            }
 //           closeConnection(c, s, rs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(c, s, rs);
        }
        return _list;

    } 
    public java.util.ArrayList getUserByQuery(String filter)
    {
        ArrayList _list;
        String query;
        Connection c;
        ResultSet rs;
        PreparedStatement s;
        _list = new ArrayList();
        User user = null;
        query = "SELECT User_Id, User_Name, Full_Name, Legacy_Sys_Id, Class, Branch, Password, Ph" +
"one_No, is_Supervisor, Must_Change_Pwd, Login_Status, User_Status, UserId, Creat" +
"e_Date, Password_Expiry, Login_Date, Login_System, Fleet_Admin, email, Branch, p" +
"assword_changed,Expiry_Date,branch_restriction, approval_limit,approval_level,to" +
"ken_required,dept_restriction,UnderTaker,region_code,zone_code,region_restriction,zone_restriction,Facility_Admin,Store_Admin FROM AM_GB_USER WHERE User_Id IS NOT NULL "
;
        query += filter;
//        System.out.println("<<<<<<getUserByQuery query: "+query);
        c = null;
        rs = null;
        s = null;
        try {
            c = getConnection();
            s = c.prepareStatement(query);
            rs = s.executeQuery();
        while (rs.next()) {
            String userId = rs.getString("User_Id");
            String userName = rs.getString("User_Name");
            String fullName = rs.getString("Full_Name");
            String legacySysId = rs.getString("Legacy_Sys_Id");
            String Class = rs.getString("Class");
            String branch = rs.getString("Branch");
            String password = rs.getString("Password");
            String phoneNo = rs.getString("Phone_No");
            String isSupervisor = rs.getString("Is_Supervisor");
            String mustChangePwd = rs.getString("Must_Change_Pwd");
            String loginStatus = rs.getString("Login_Status");
            String userStatus = rs.getString("User_Status");
            String user_Id = rs.getString("UserId");
            String createDate = rs.getString("Create_Date");
            String passwordExpiry = rs.getString("Password_Expiry");
            String loginDate = rs.getString("Login_Date");
            String loginSystem = rs.getString("Login_System");
            String fleetAdmin = rs.getString("Fleet_Admin");
            String email = rs.getString("email");
            String branchCode = rs.getString("Branch");
            String pwdChanged = rs.getString("password_changed");
            String tokenRequired = rs.getString("token_required");
//            System.out.println((new StringBuilder()).append("####################################### Token Read from the DB here ====").append(tokenRequired).toString());
//            System.out.println((new StringBuilder()).append(" The sent tokenRequired ==== ").append(tokenRequired).toString());
            Date expiry_date = rs.getDate("Expiry_Date");
            String branch_restriction = rs.getString("branch_restriction");
            String apprvLimit = rs.getString("approval_limit");
            String apprvLevel = rs.getString("approval_level");
            String buRestrict = rs.getString("dept_restriction");
            String underTaker = rs.getString("UnderTaker");
            String regionCode = rs.getString("region_code");
            String zoneCode = rs.getString("zone_code");
            String regionRestrict = rs.getString("region_restriction");
            String zoneRestrict = rs.getString("zone_restriction");
            String facilityAdmin = rs.getString("Facility_Admin");
            String storeAdmin = rs.getString("Store_Admin");
            user = new User();
            user.setUserId(userId);
            user.setUserName(userName);
            user.setUserFullName(fullName);
            user.setLegacySystemId(legacySysId);
            user.setUserClass(Class);
            user.setBranch(branch);
            user.setPassword(password);
            user.setPhoneNo(phoneNo);
            user.setIsSupervisor(isSupervisor);
            user.setMustChangePwd(mustChangePwd);
            user.setLoginStatus(loginStatus);
            user.setUserStatus(userStatus);
            user.setCreatedBy(user_Id);
            user.setCreateDate(createDate);
            user.setPwdExpiry(passwordExpiry);
            user.setLastLogindate(loginDate);
            user.setLoginSystem(loginSystem);
            user.setFleetAdmin(fleetAdmin);
            user.setEmail(email);
            user.setBranch(branchCode);
            user.setPwdChanged(pwdChanged);
            user.setUnderTaker(underTaker);
            user.setRegionCode(regionCode);
            user.setZoneCode(zoneCode);
            user.setRegionRestrict(regionRestrict);
            user.setZoneRestrict(zoneRestrict);
            user.setIsFacilityAdministrator(facilityAdmin);
            user.setIsStoreAdministrator(storeAdmin);
            if(tokenRequired != null && tokenRequired.trim().equalsIgnoreCase("Y"))
            {
                user.setTokenRequired(true);
            } else
            {
                user.setTokenRequired(false);
            }
 //          System.out.println((new StringBuilder()).append("##################################### After setting the user the token of the us" +
//"er =="
//).append(user.isTokenRequired()).toString());
            user.setApprvLimit(apprvLimit);
            user.setBranchRestrict(branch_restriction);
            user.setApprvLevel(apprvLevel);
            user.setExpDate(expiry_date);
            user.setDeptRestrict(buRestrict);
            _list.add(user);
        }
//        closeConnection(c, s, rs);
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }
    
    public java.util.ArrayList getUserByQuery(String filter,String status)
    {
        ArrayList _list;
        String query;
        Connection c;
        ResultSet rs;
        PreparedStatement s;
        _list = new ArrayList();
        User user = null;
        query = "SELECT User_Id, User_Name, Full_Name, Legacy_Sys_Id, Class, Branch, Password, Ph" +
"one_No, is_Supervisor, Must_Change_Pwd, Login_Status, User_Status, UserId, Creat" +
"e_Date, Password_Expiry, Login_Date, Login_System, Fleet_Admin, email, Branch, p" +
"assword_changed,Expiry_Date,branch_restriction, approval_limit,approval_level,to" +
"ken_required,dept_restriction,UnderTaker,region_code,zone_code,region_restriction,zone_restriction,Facility_Admin,Store_Admin "+
" FROM AM_GB_USER WHERE User_Id IS NOT NULL AND USER_STATUS= ? ";
        query += filter;
//        System.out.println("<<<<<<getUserByQuery with two Parameter query: "+query);
        c = null;
        rs = null;
        s = null;
        try {
            c = getConnection();
            s = c.prepareStatement(query);
            s.setString(1, status);
            rs = s.executeQuery();
           
        while (rs.next()) {
            String userId = rs.getString("User_Id");
            String userName = rs.getString("User_Name");
            String fullName = rs.getString("Full_Name");
            String legacySysId = rs.getString("Legacy_Sys_Id");
            String Class = rs.getString("Class");
            String branch = rs.getString("Branch");
            String password = rs.getString("Password");
            String phoneNo = rs.getString("Phone_No");
            String isSupervisor = rs.getString("Is_Supervisor");
            String mustChangePwd = rs.getString("Must_Change_Pwd");
            String loginStatus = rs.getString("Login_Status");
            String userStatus = rs.getString("User_Status");
            String user_Id = rs.getString("UserId");
            String createDate = rs.getString("Create_Date");
            String passwordExpiry = rs.getString("Password_Expiry");
            String loginDate = rs.getString("Login_Date");
            String loginSystem = rs.getString("Login_System");
            String fleetAdmin = rs.getString("Fleet_Admin");
            String email = rs.getString("email");
            String branchCode = rs.getString("Branch");
            String pwdChanged = rs.getString("password_changed");
            String tokenRequired = rs.getString("token_required");
//            System.out.println((new StringBuilder()).append("####################################### Token Read from the DB here ====").append(tokenRequired).toString());
//            System.out.println((new StringBuilder()).append(" The sent tokenRequired ==== ").append(tokenRequired).toString());
            Date expiry_date = rs.getDate("Expiry_Date");
            String branch_restriction = rs.getString("branch_restriction");
            String apprvLimit = rs.getString("approval_limit");
            String apprvLevel = rs.getString("approval_level");
            String buRestrict = rs.getString("dept_restriction");
            String underTaker = rs.getString("UnderTaker");
            String regionCode = rs.getString("region_code");
            String zoneCode = rs.getString("zone_code");
            String regionRestrict = rs.getString("region_restriction");
            String zoneRestrict = rs.getString("zone_restriction");
            String facilityAdmin = rs.getString("Facility_Admin");
            String storeAdmin = rs.getString("Store_Admin");
            user = new User();
            user.setUserId(userId);
            user.setUserName(userName);
            user.setUserFullName(fullName);
            user.setLegacySystemId(legacySysId);
            user.setUserClass(Class);
            user.setBranch(branch);
            user.setPassword(password);
            user.setPhoneNo(phoneNo);
            user.setIsSupervisor(isSupervisor);
            user.setMustChangePwd(mustChangePwd);
            user.setLoginStatus(loginStatus);
            user.setUserStatus(userStatus);
            user.setCreatedBy(user_Id);
            user.setCreateDate(createDate);
            user.setPwdExpiry(passwordExpiry);
            user.setLastLogindate(loginDate);
            user.setLoginSystem(loginSystem);
            user.setFleetAdmin(fleetAdmin);
            user.setEmail(email);
            user.setBranch(branchCode);
            user.setPwdChanged(pwdChanged);
            user.setUnderTaker(underTaker);
            user.setRegionCode(regionCode);
            user.setZoneCode(zoneCode);
            user.setRegionRestrict(regionRestrict);
            user.setZoneRestrict(zoneRestrict);
            user.setIsFacilityAdministrator(facilityAdmin);
            user.setIsStoreAdministrator(storeAdmin);
            if(tokenRequired != null && tokenRequired.trim().equalsIgnoreCase("Y"))
            {
                user.setTokenRequired(true);
            } else
            {
                user.setTokenRequired(false);
            }
 //          System.out.println((new StringBuilder()).append("##################################### After setting the user the token of the us" +
//"er =="
//).append(user.isTokenRequired()).toString());
            user.setApprvLimit(apprvLimit);
            user.setBranchRestrict(branch_restriction);
            user.setApprvLevel(apprvLevel);
            user.setExpDate(expiry_date);
            user.setDeptRestrict(buRestrict);
            _list.add(user);
        }
//        closeConnection(c, s, rs);
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }


    public legend.admin.objects.User getUserByUserIDOld(String UserID)
    {
        User user;
        String expiryDate;
//        boolean tokenRequired;
        String query;
        Connection c;
        ResultSet rs;
        PreparedStatement s;
        user = null;
        expiryDate = "";
//        tokenRequired = false;
        query = "SELECT User_Id, User_Name, Full_Name, Legacy_Sys_Id, Class, Branch, Ph" +
"one_No, is_Supervisor, Must_Change_Pwd, Login_Status, User_Status, UserId, Creat" +
"e_Date, Password_Expiry, Login_Date, Login_System, Fleet_Admin, email, Branch,pa" +
"ssword_changed,branch_restriction, Expiry_Days,Expiry_Date,dept_code," +
"is_StockAdministrator,is_Storekeeper,Approval_Level,dept_restriction,UnderTaker,region_code,zone_code,region_restriction,zone_restriction,Facility_Admin,Store_Admin,token_required FROM AM_GB_USER WHERE User_Id = ?";
//System.out.println("getUserByUserID query: "+query);
        c = null;
        rs = null;
        s = null;
        try {

        c = getConnection();
        s = c.prepareStatement(query);
        s.setString(1, UserID);
        rs = s.executeQuery();
      
        while (rs.next()) {
            String userId = rs.getString("User_Id");
            String userName = rs.getString("User_Name");
            String fullName = rs.getString("Full_Name");
            String legacySysId = rs.getString("Legacy_Sys_Id");
            String Class = rs.getString("Class");
            String branch = rs.getString("Branch");
//            String password = rs.getString("Password");
            String phoneNo = rs.getString("Phone_No");
            String isSupervisor = rs.getString("Is_Supervisor");
            String mustChangePwd = rs.getString("Must_Change_Pwd");
            String loginStatus = rs.getString("Login_Status");
            String userStatus = rs.getString("User_Status");
            String user_Id = rs.getString("UserId");
            String createDate = rs.getString("Create_Date");
            String passwordExpiry = rs.getString("Password_Expiry");
            String loginDate = rs.getString("Login_Date");
            String loginSystem = rs.getString("Login_System");
            String fleetAdmin = rs.getString("Fleet_Admin");
            String email = rs.getString("email");
            String branchCode = rs.getString("Branch");
            String pwdChanged = rs.getString("password_changed");
            String branchRestrict = rs.getString("branch_restriction");
            int expiryDays = rs.getInt("Expiry_Days");
            String deptCode = rs.getString("dept_code");
//            String tokenRequire = rs.getString("token_required");
            String isStockAdministrator = rs.getString("is_StockAdministrator");
            String approveLevel = rs.getString("Approval_Level");
            String isStorekeeper = rs.getString("is_Storekeeper");
            String buRestrict = rs.getString("dept_restriction");
            String underTaker = rs.getString("UnderTaker");
            String regionCode = rs.getString("region_code");
            String zoneCode = rs.getString("zone_code");
            String regionRestrict = rs.getString("region_restriction");
            String zoneRestrict = rs.getString("zone_restriction");
            String facilityAdmin = rs.getString("Facility_Admin");
            String storeAdmin = rs.getString("Store_Admin");
            String tokenRequire = rs.getString("token_required");
//            if(tokenRequire == "Y")
//            {
//                tokenRequired = true;
//            }
//            if(tokenRequire == "N")
//            {
//                tokenRequired = false;
//            }
            if(rs.getDate("Expiry_Date") != null)
            { 
                expiryDate = rs.getDate("Expiry_Date").toString();
            }
            user = new User();
            user.setUserId(userId);
            user.setUserName(userName);
            user.setUserFullName(fullName);
            user.setLegacySystemId(legacySysId);
            user.setUserClass(Class);
            user.setBranch(branch);
//            user.setPassword(password);
            user.setPhoneNo(phoneNo);
            user.setIsSupervisor(isSupervisor);
            user.setMustChangePwd(mustChangePwd);
            user.setLoginStatus(loginStatus);
            user.setUserStatus(userStatus);
            user.setCreatedBy(user_Id);
            user.setCreateDate(createDate);
            user.setUserStatus(userStatus);
            user.setPwdExpiry(passwordExpiry);
            user.setLastLogindate(loginDate);
            user.setLoginSystem(loginSystem);
            user.setFleetAdmin(fleetAdmin);
            user.setEmail(email);
            user.setBranch(branchCode);
            user.setPwdChanged(pwdChanged);
            user.setBranchRestrict(branchRestrict);
            user.setExpiryDays(expiryDays);
            user.setExpiryDate(expiryDate);
            user.setDeptCode(deptCode);
//            user.setTokenRequire(tokenRequire);
            user.setIsStorekeeper(isStorekeeper);
            user.setApproveLevel(approveLevel);
            user.setIsStockAdministrator(isStockAdministrator);
            user.setDeptRestrict(buRestrict);
            user.setUnderTaker(underTaker);
            user.setRegionCode(regionCode);
            user.setZoneCode(zoneCode);
            user.setRegionRestrict(regionRestrict);
            user.setZoneRestrict(zoneRestrict);
            user.setIsFacilityAdministrator(facilityAdmin);
            user.setIsStoreAdministrator(storeAdmin);
            user.setTokenRequire(tokenRequire);
            
        }
 //       closeConnection(c, s, rs);
    }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(c, s, rs);
        }
        return user;
    }
    
    public User getUserByUserID(String userID) {

        String query =
            "SELECT User_Id, User_Name, Full_Name, Legacy_Sys_Id, Class, Branch, " +
            "Phone_No, Is_Supervisor, Must_Change_Pwd, Login_Status, User_Status, " +
            "UserId, Create_Date, Password_Expiry, Login_Date, Login_System, " +
            "Fleet_Admin, Email, password_changed, branch_restriction, Expiry_Days, " +
            "Expiry_Date, dept_code, is_StockAdministrator, is_Storekeeper, " +
            "Approval_Level, dept_restriction, UnderTaker, region_code, zone_code, " +
            "region_restriction, zone_restriction, Facility_Admin, Store_Admin, token_required " +
            "FROM AM_GB_USER WHERE User_Id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, userID);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    return null;
                }

                User user = mapUserFromResultSet(rs);
                return user;
            }

        } catch (SQLException e) {
            System.err.println("ERROR in getUserByUserID -> " + e.getMessage());
            return null;
        }
    }
    
    private User mapUserFromResultSet(ResultSet rs) throws SQLException {

        User user = new User();

        user.setUserId(rs.getString("User_Id"));
        user.setUserName(rs.getString("User_Name"));
        user.setUserFullName(rs.getString("Full_Name"));
        user.setLegacySystemId(rs.getString("Legacy_Sys_Id"));
        user.setUserClass(rs.getString("Class"));
        user.setBranch(rs.getString("Branch"));
        user.setPhoneNo(rs.getString("Phone_No"));
        user.setIsSupervisor(rs.getString("Is_Supervisor"));
        user.setMustChangePwd(rs.getString("Must_Change_Pwd"));
        user.setLoginStatus(rs.getString("Login_Status"));
        user.setUserStatus(rs.getString("User_Status"));
        user.setCreatedBy(rs.getString("UserId"));
        user.setCreateDate(rs.getString("Create_Date"));
        user.setPwdExpiry(rs.getString("Password_Expiry"));
        user.setLastLogindate(rs.getString("Login_Date"));
        user.setLoginSystem(rs.getString("Login_System"));
        user.setFleetAdmin(rs.getString("Fleet_Admin"));
        user.setEmail(rs.getString("Email"));
        user.setPwdChanged(rs.getString("password_changed"));
        user.setBranchRestrict(rs.getString("branch_restriction"));
        user.setExpiryDays(rs.getInt("Expiry_Days"));
        user.setDeptCode(rs.getString("dept_code"));
        user.setIsStockAdministrator(rs.getString("is_StockAdministrator"));
        user.setIsStorekeeper(rs.getString("is_Storekeeper"));
        user.setApproveLevel(rs.getString("Approval_Level"));
        user.setDeptRestrict(rs.getString("dept_restriction"));
        user.setUnderTaker(rs.getString("UnderTaker"));
        user.setRegionCode(rs.getString("region_code"));
        user.setZoneCode(rs.getString("zone_code"));
        user.setRegionRestrict(rs.getString("region_restriction"));
        user.setZoneRestrict(rs.getString("zone_restriction"));
        user.setIsFacilityAdministrator(rs.getString("Facility_Admin"));
        user.setIsStoreAdministrator(rs.getString("Store_Admin"));
        user.setTokenRequire(rs.getString("token_required"));

        Date expiry = rs.getDate("Expiry_Date");
        if (expiry != null) {
            user.setExpiryDate(expiry.toString());
        }

        return user;
    }



    public legend.admin.objects.User getUserByUserNameOld(String UserName)
    {
        User user;
        String query;
        Connection c;
        ResultSet rs;
        PreparedStatement s;
        user = null;
        query = "SELECT User_Id, User_Name, Full_Name, Legacy_Sys_Id, Class, Branch, Password, Ph" +
"one_No, is_Supervisor, Must_Change_Pwd, Login_Status, User_Status, UserId, Creat" +
"e_Date, Password_Expiry, Login_Date, Login_System, Fleet_Admin, email, Branch,pa" +
"ssword_changed,region_code,zone_code,region_restriction,zone_restriction,Facility_Admin,Store_Admin FROM AM_GB_USER WHERE User_Name = ?";
        c = null;
        rs = null;
        s = null;
        try {
        c = getConnection();
        String pwdChanged;
        s = c.prepareStatement(query);
        s.setString(1, UserName);
        rs = s.executeQuery();
        while (rs.next()) {
            String userId = rs.getString("User_Id");
            String userName = rs.getString("User_Name");
            String fullName = rs.getString("Full_Name");
            String legacySysId = rs.getString("Legacy_Sys_Id");
            String Class = rs.getString("Class");
            String branch = rs.getString("Branch");
            String password = rs.getString("Password");
            String phoneNo = rs.getString("Phone_No");
            String isSupervisor = rs.getString("Is_Supervisor");
            String mustChangePwd = rs.getString("Must_Change_Pwd");
            String loginStatus = rs.getString("Login_Status");
            String userStatus = rs.getString("User_Status");
            String user_Id = rs.getString("UserId");
            String createDate = rs.getString("Create_Date");
            String passwordExpiry = rs.getString("Password_Expiry");
            String loginDate = rs.getString("Login_Date");
            String loginSystem = rs.getString("Login_System");
            String fleetAdmin = rs.getString("Fleet_Admin");
            String email = rs.getString("email");
            String branchCode = rs.getString("Branch");
            pwdChanged = rs.getString("password_changed");
            String regionCode = rs.getString("region_code");
            String zoneCode = rs.getString("zone_code");
            String regionRestrict = rs.getString("region_restriction");
            String zoneRestrict = rs.getString("zone_restriction");
            String facilityAdmin = rs.getString("Facility_Admin");
            String storeAdmin = rs.getString("Store_Admin");
            user = new User();
            user.setUserId(userId);
            user.setUserName(userName);
            user.setUserFullName(fullName);
            user.setLegacySystemId(legacySysId);
            user.setUserClass(Class);
            user.setBranch(branch);
            user.setPassword(password);
            user.setPhoneNo(phoneNo);
            user.setIsSupervisor(isSupervisor);
            user.setMustChangePwd(mustChangePwd);
            user.setLoginStatus(loginStatus);
            user.setUserStatus(userStatus);
            user.setCreatedBy(user_Id);
            user.setCreateDate(createDate);
            user.setPwdExpiry(passwordExpiry);
            user.setLastLogindate(loginDate);
            user.setLoginSystem(loginSystem);
            user.setFleetAdmin(fleetAdmin);
            user.setEmail(email);
            user.setBranch(branchCode);
            user.setRegionCode(regionCode);
            user.setZoneCode(zoneCode);
            user.setRegionRestrict(regionRestrict);
            user.setZoneRestrict(zoneRestrict);
            user.setIsFacilityAdministrator(facilityAdmin);
            user.setIsStoreAdministrator(storeAdmin);
        }
//        closeConnection(c, s, rs);
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(c, s, rs);
        }
        return user;
    }
    
    public User getUserByUserName(String userName) {

        String query =
            "SELECT User_Id, User_Name, Full_Name, Legacy_Sys_Id, Class, Branch, Password, " +
            "Phone_No, Is_Supervisor, Must_Change_Pwd, Login_Status, User_Status, " +
            "UserId, Create_Date, Password_Expiry, Login_Date, Login_System, " +
            "Fleet_Admin, Email, password_changed, region_code, zone_code, " +
            "region_restriction, zone_restriction, Facility_Admin, Store_Admin " +
            "FROM AM_GB_USER WHERE User_Name = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, userName);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    return null; // user not found
                }

                User user = new User();

                user.setUserId(rs.getString("User_Id"));
                user.setUserName(rs.getString("User_Name"));
                user.setUserFullName(rs.getString("Full_Name"));
                user.setLegacySystemId(rs.getString("Legacy_Sys_Id"));
                user.setUserClass(rs.getString("Class"));
                user.setBranch(rs.getString("Branch"));
                user.setPassword(rs.getString("Password"));
                user.setPhoneNo(rs.getString("Phone_No"));
                user.setIsSupervisor(rs.getString("Is_Supervisor"));
                user.setMustChangePwd(rs.getString("Must_Change_Pwd"));
                user.setLoginStatus(rs.getString("Login_Status"));
                user.setUserStatus(rs.getString("User_Status"));
                user.setCreatedBy(rs.getString("UserId"));
                user.setCreateDate(rs.getString("Create_Date"));
                user.setPwdExpiry(rs.getString("Password_Expiry"));
                user.setLastLogindate(rs.getString("Login_Date"));
                user.setLoginSystem(rs.getString("Login_System"));
                user.setFleetAdmin(rs.getString("Fleet_Admin"));
                user.setEmail(rs.getString("Email"));
                user.setRegionCode(rs.getString("region_code"));
                user.setZoneCode(rs.getString("zone_code"));
                user.setRegionRestrict(rs.getString("region_restriction"));
                user.setZoneRestrict(rs.getString("zone_restriction"));
                user.setIsFacilityAdministrator(rs.getString("Facility_Admin"));
                user.setIsStoreAdministrator(rs.getString("Store_Admin"));

                return user;
            }

        } catch (SQLException e) {
            System.err.println("ERROR in getUserByUserName -> " + e.getMessage());
            return null;
        }
    }


    public boolean createUserOld(legend.admin.objects.User user)
    {
        Connection con;
        PreparedStatement ps;
        boolean done;
        con = null;
        ps = null;
        done = false;
        String query = "";
        try {
        if(user.getExpiryDate() == "" || user.getExpiryDate().equalsIgnoreCase("null") || user.getExpiryDate() == null || user.getExpiryDate().equalsIgnoreCase(""))
        {
             query = "INSERT INTO AM_GB_USER( User_Name, Full_Name, Legacy_Sys_Id, Class, Branch, Pass" +
"word, Phone_No, Is_Supervisor, Must_Change_Pwd, Login_Status, User_Status, UserI" +
"d, Create_Date, Password_Expiry,Fleet_Admin, Email,User_id,branch_restriction, E" +
"xpiry_Days,dept_restriction,dept_code,UnderTaker," +
"region_code,zone_code,region_restriction,zone_restriction,Facility_Admin,Store_Admin)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
;
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getUserFullName());
            ps.setString(3, user.getLegacySystemId());
            ps.setString(4, user.getUserClass());
            ps.setString(5, user.getBranch());
            ps.setString(6, user.getPassword());
            ps.setString(7, user.getPhoneNo());
            ps.setString(8, user.getIsSupervisor());
            ps.setString(9, user.getMustChangePwd());
            ps.setString(10, user.getLoginStatus());
            ps.setString(11, user.getUserStatus());
            ps.setString(12, user.getCreatedBy());
            ps.setDate(13, df.dateConvert(new Date()));
            ps.setDate(14, df.dateConvert(df.addDayToDate(new Date(), Integer.parseInt(user.getPwdExpiry()))));
            ps.setString(15, user.getFleetAdmin());
            ps.setString(16, user.getEmail());
            ps.setString(17, (new ApplicationHelper()).getGeneratedId("AM_GB_USER"));
            ps.setString(18, user.getBranchRestrict());
            ps.setInt(19, user.getExpiryDays());
            ps.setString(20, user.getDeptRestrict());
            ps.setString(21, user.getDeptCode());
            ps.setString(22, user.getUnderTaker());
            ps.setString(23, user.getRegionCode());
            ps.setString(24, user.getZoneCode());
            ps.setString(25, user.getRegionRestrict());
            ps.setString(26, user.getZoneRestrict());
            ps.setString(27, user.getIsFacilityAdministrator());
            ps.setString(28, user.getIsStoreAdministrator());
            done = ps.executeUpdate() != -1;
        } else
        {
             query = "INSERT INTO AM_GB_USER( User_Name, Full_Name, Legacy_Sys_Id, Class, Branch, Pass" +
"word, Phone_No, Is_Supervisor, Must_Change_Pwd, Login_Status, User_Status, UserI" +
"d, Create_Date, Password_Expiry,Fleet_Admin, Email,User_id,branch_restriction, E" +
"xpiry_Days,Expiry_Date,dept_restriction,dept_code,UnderTaker,region_code,zone_code," +
"region_restriction,zone_restriction,Facility_Admin,Store_Admin)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
;
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getUserFullName());
            ps.setString(3, user.getLegacySystemId());
            ps.setString(4, user.getUserClass());
            ps.setString(5, user.getBranch());
            ps.setString(6, user.getPassword());
            ps.setString(7, user.getPhoneNo());
            ps.setString(8, user.getIsSupervisor());
            ps.setString(9, user.getMustChangePwd());
            ps.setString(10, user.getLoginStatus());
            ps.setString(11, user.getUserStatus());
            ps.setString(12, user.getCreatedBy());
            ps.setDate(13, df.dateConvert(new Date()));
            ps.setDate(14, df.dateConvert(df.addDayToDate(new Date(), Integer.parseInt(user.getPwdExpiry()))));
            ps.setString(15, user.getFleetAdmin());
            ps.setString(16, user.getEmail());
            ps.setString(17, (new ApplicationHelper()).getGeneratedId("AM_GB_USER"));
            ps.setString(18, user.getBranchRestrict());
            ps.setInt(19, user.getExpiryDays());
            String temDate = user.getExpiryDate();
            ps.setDate(20, df.dateConvert(temDate));
            ps.setString(21, user.getDeptRestrict());
            ps.setString(22, user.getDeptCode());
            ps.setString(23, user.getUnderTaker());
            ps.setString(24, user.getRegionCode());
            ps.setString(25, user.getZoneCode());
            ps.setString(26, user.getRegionRestrict());
            ps.setString(27, user.getZoneRestrict());
            ps.setString(28, user.getIsFacilityAdministrator());
            ps.setString(29, user.getIsStoreAdministrator());
            done = ps.executeUpdate() != -1;
        }
 //       closeConnection(con, ps);
        }catch (Exception e) {
            System.out.println("WARNING:Error executing Query in createUser ->"
                    + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }
    
    public boolean createUser(legend.admin.objects.User user) {

        boolean hasExpiryDate = user.getExpiryDate() != null &&
                                !user.getExpiryDate().trim().isEmpty() &&
                                !"null".equalsIgnoreCase(user.getExpiryDate().trim());

        String query = hasExpiryDate
                ? "INSERT INTO AM_GB_USER (" +
                  "User_Name, Full_Name, Legacy_Sys_Id, Class, Branch, Password, Phone_No, " +
                  "Is_Supervisor, Must_Change_Pwd, Login_Status, User_Status, UserId, " +
                  "Create_Date, Password_Expiry, Fleet_Admin, Email, branch_restriction, " +
                  "Expiry_Days, Expiry_Date, dept_restriction, dept_code, UnderTaker, " +
                  "region_code, zone_code, region_restriction, zone_restriction, " +
                  "Facility_Admin, Store_Admin) " +
                  "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
                : "INSERT INTO AM_GB_USER (" +
                  "User_Name, Full_Name, Legacy_Sys_Id, Class, Branch, Password, Phone_No, " +
                  "Is_Supervisor, Must_Change_Pwd, Login_Status, User_Status, UserId, " +
                  "Create_Date, Password_Expiry, Fleet_Admin, Email, branch_restriction, " +
                  "Expiry_Days, dept_restriction, dept_code, UnderTaker, " +
                  "region_code, zone_code, region_restriction, zone_restriction, " +
                  "Facility_Admin, Store_Admin) " +
                  "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            int index = 1;

            String generatedId = new ApplicationHelper().getGeneratedId("AM_GB_USER");

            ps.setString(index++, user.getUserName());
            ps.setString(index++, user.getUserFullName());
            ps.setString(index++, user.getLegacySystemId());
            ps.setString(index++, user.getUserClass());
            ps.setString(index++, user.getBranch());
            ps.setString(index++, user.getPassword());
            ps.setString(index++, user.getPhoneNo());
            ps.setString(index++, user.getIsSupervisor());
            ps.setString(index++, user.getMustChangePwd());
            ps.setString(index++, user.getLoginStatus());
            ps.setString(index++, user.getUserStatus());
            ps.setString(index++, generatedId);
            ps.setDate(index++, df.dateConvert(new Date()));
            ps.setDate(index++, df.dateConvert(
                    df.addDayToDate(new Date(), Integer.parseInt(user.getPwdExpiry()))
            ));
            ps.setString(index++, user.getFleetAdmin());
            ps.setString(index++, user.getEmail());
            ps.setString(index++, user.getBranchRestrict());
            ps.setInt(index++, user.getExpiryDays());

            if (hasExpiryDate) {
                ps.setDate(index++, df.dateConvert(user.getExpiryDate()));
            }

            ps.setString(index++, user.getDeptRestrict());
            ps.setString(index++, user.getDeptCode());
            ps.setString(index++, user.getUnderTaker());
            ps.setString(index++, user.getRegionCode());
            ps.setString(index++, user.getZoneCode());
            ps.setString(index++, user.getRegionRestrict());
            ps.setString(index++, user.getZoneRestrict());
            ps.setString(index++, user.getIsFacilityAdministrator());
            ps.setString(index++, user.getIsStoreAdministrator());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("WARNING: Error executing createUser -> " + e.getMessage());
            return false;
        }
    }


    public boolean updateUserOld(legend.admin.objects.User user)
    {
        Connection con;
        PreparedStatement ps;
        boolean done;
        con = null;
        ps = null;
        done = false;
        String query = "";
        try {
        if(user.getExpiryDate() == "" || user.getExpiryDate().equalsIgnoreCase("null") || user.getExpiryDate().equalsIgnoreCase(""))
        {
             query = "UPDATE AM_GB_USER  SET  User_Name = ?, Full_Name = ? ,Legacy_Sys_Id = ?, Class =" +
" ?, Branch = ?, Password = ?, Phone_No = ?,Is_Supervisor = ?,Must_Change_Pwd = ?" +
", Login_Status = ?, User_Status = ?, Fleet_Admin = ?, Email = ?,branch_restricti" +
"on =?, Expiry_Days =? ,region_code =?,zone_code=?,region_restriction=?,zone_restriction=?,Facility_Admin = ?,Store_Admin = ? WHERE User_Id = ?"
;
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getUserFullName());
            ps.setString(3, user.getLegacySystemId());
            ps.setString(4, user.getUserClass());
            ps.setString(5, user.getBranch());
            ps.setString(6, user.getPassword());
            ps.setString(7, user.getPhoneNo());
            ps.setString(8, user.getIsSupervisor());
            ps.setString(9, user.getMustChangePwd());
            ps.setString(10, user.getLoginStatus());
            ps.setString(11, user.getUserStatus());
            ps.setString(12, user.getFleetAdmin());
            ps.setString(13, user.getEmail());
            ps.setString(14, user.getBranchRestrict());
            ps.setInt(15, user.getExpiryDays());
            ps.setString(16, user.getRegionCode());
            ps.setString(17, user.getZoneCode());
            ps.setString(18, user.getRegionRestrict());
            ps.setString(19, user.getZoneRestrict());
            ps.setString(20, user.getIsFacilityAdministrator());
            ps.setString(21, user.getIsStoreAdministrator());
            ps.setString(22, user.getUserId());

            done = ps.executeUpdate() != -1;
        } else
        {
             query = "UPDATE AM_GB_USER  SET  User_Name = ?, Full_Name = ? ,Legacy_Sys_Id = ?, Class =" +
" ?, Branch = ?, Password = ?, Phone_No = ?,Is_Supervisor = ?,Must_Change_Pwd = ?" +
", Login_Status = ?, User_Status = ?, Fleet_Admin = ?, Email = ?,branch_restricti" +
"on =?, Expiry_Days =?, Expiry_Date=?,region_code =?,zone_code=?,region_restriction=?,zone_restriction=?,Facility_Admin = ?,Store_Admin = ?  WHERE User_Id = ?"
;
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getUserFullName());
            ps.setString(3, user.getLegacySystemId());
            ps.setString(4, user.getUserClass());
            ps.setString(5, user.getBranch());
            ps.setString(6, user.getPassword());
            ps.setString(7, user.getPhoneNo());
            ps.setString(8, user.getIsSupervisor());
            ps.setString(9, user.getMustChangePwd());
            ps.setString(10, user.getLoginStatus());
            ps.setString(11, user.getUserStatus());
            ps.setString(12, user.getFleetAdmin());
            ps.setString(13, user.getEmail());
            ps.setString(14, user.getBranchRestrict());
            ps.setInt(15, user.getExpiryDays());
            ps.setDate(16, df.dateConvert(user.getExpiryDate()));
            ps.setString(17, user.getRegionCode());
            ps.setString(18, user.getZoneCode());
            ps.setString(19, user.getRegionRestrict());
            ps.setString(20, user.getZoneRestrict());
            ps.setString(21, user.getIsFacilityAdministrator());
            ps.setString(22, user.getIsStoreAdministrator());
            ps.setString(23, user.getUserId());

            done = ps.executeUpdate() != -1;
        }
        if(user.getExpiryDays() == 0)
        {
            updateExpiryDate(Integer.parseInt(user.getUserId()));
        }
//        closeConnection(con, ps);
        }catch (Exception e) {
            System.out.println("WARNING:Error executing Query in updateUser ->"
                    + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }
    
    public boolean updateUser(legend.admin.objects.User user) {

        boolean hasExpiryDate = user.getExpiryDate() != null &&
                                !user.getExpiryDate().trim().isEmpty() &&
                                !"null".equalsIgnoreCase(user.getExpiryDate().trim());

        String query = hasExpiryDate
                ? "UPDATE AM_GB_USER SET User_Name=?, Full_Name=?, Legacy_Sys_Id=?, Class=?, " +
                  "Branch=?, Password=?, Phone_No=?, Is_Supervisor=?, Must_Change_Pwd=?, " +
                  "Login_Status=?, User_Status=?, Fleet_Admin=?, Email=?, branch_restriction=?, " +
                  "Expiry_Days=?, Expiry_Date=?, region_code=?, zone_code=?, region_restriction=?, " +
                  "zone_restriction=?, Facility_Admin=?, Store_Admin=? WHERE User_Id=?"
                : "UPDATE AM_GB_USER SET User_Name=?, Full_Name=?, Legacy_Sys_Id=?, Class=?, " +
                  "Branch=?, Password=?, Phone_No=?, Is_Supervisor=?, Must_Change_Pwd=?, " +
                  "Login_Status=?, User_Status=?, Fleet_Admin=?, Email=?, branch_restriction=?, " +
                  "Expiry_Days=?, region_code=?, zone_code=?, region_restriction=?, " +
                  "zone_restriction=?, Facility_Admin=?, Store_Admin=? WHERE User_Id=?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            int index = 1;

            ps.setString(index++, user.getUserName());
            ps.setString(index++, user.getUserFullName());
            ps.setString(index++, user.getLegacySystemId());
            ps.setString(index++, user.getUserClass());
            ps.setString(index++, user.getBranch());
            ps.setString(index++, user.getPassword());
            ps.setString(index++, user.getPhoneNo());
            ps.setString(index++, user.getIsSupervisor());
            ps.setString(index++, user.getMustChangePwd());
            ps.setString(index++, user.getLoginStatus());
            ps.setString(index++, user.getUserStatus());
            ps.setString(index++, user.getFleetAdmin());
            ps.setString(index++, user.getEmail());
            ps.setString(index++, user.getBranchRestrict());
            ps.setInt(index++, user.getExpiryDays());

            if (hasExpiryDate) {
                ps.setDate(index++, df.dateConvert(user.getExpiryDate()));
            }

            ps.setString(index++, user.getRegionCode());
            ps.setString(index++, user.getZoneCode());
            ps.setString(index++, user.getRegionRestrict());
            ps.setString(index++, user.getZoneRestrict());
            ps.setString(index++, user.getIsFacilityAdministrator());
            ps.setString(index++, user.getIsStoreAdministrator());
            ps.setString(index++, user.getUserId());

            boolean updated = ps.executeUpdate() > 0;

            if (user.getExpiryDays() == 0) {
                updateExpiryDate(Integer.parseInt(user.getUserId()));
            }

            return updated;

        } catch (SQLException e) {
            System.err.println("WARNING: Error executing updateUser -> " + e.getMessage());
            return false;
        }
    }



    public boolean confirmPasswordOld(String iden, String pass)
    {
        String query;
        Connection con;
        cm = new CryptManager();
        PreparedStatement ps;
        ResultSet rs;
        boolean done;
        query = "SELECT PASSWORD FROM AM_GB_USER   WHERE USER_ID = ?";
        con = null;
        ps = null;
        rs = null;
        done = false;
        try {
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, iden);

        
        for(rs = ps.executeQuery(); rs.next();)
        {
            done = pass.equals(cm.decrypt(rs.getString("PASSWORD")));
        }
//        closeConnection(con, ps, rs);
        } catch (Exception e) {
            System.out.println("WARNING:Error executing Query in confirmPassword ->"
                    + e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return done;
    }
    
    public boolean confirmPassword(String userId, String pass) throws Exception {
        String query = "SELECT PASSWORD FROM AM_GB_USER WHERE USER_ID = ?";
        boolean isMatch = false;
        cm = new CryptManager();

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("PASSWORD");
                    isMatch = pass.equals(cm.decrypt(storedPassword));
                }
            }

        } catch (SQLException e) {
            System.err.println("WARNING: Error executing confirmPassword -> " + e.getMessage());
        }

        return isMatch;
    }


    public boolean updateLoginsOld(String userid, String ipaddress)
    {
        String query;
        Connection con;
        PreparedStatement ps;
        boolean done;
        query ="UPDATE AM_GB_USER  SET LOGIN_STATUS= 1 ,LOGIN_SYSTEM=?,login_date =? WHERE   USER_ID = ?";
        con = null;
        ps = null;
        done = false;
        try {
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, ipaddress);
        ps.setDate(2, df.dateConvert(new Date()));
        ps.setString(3, userid);
        done = ps.executeUpdate() != -1;
//        closeConnection(con, ps);
        }
        catch (Exception e) {
            System.out.println("WARNING:Error executing Query in updateLogins ->"
                    + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;
    }
    
    public boolean updateLogins(String userId, String ipAddress) {
        String query = "UPDATE AM_GB_USER SET LOGIN_STATUS = 1, LOGIN_SYSTEM = ?, login_date = ? WHERE USER_ID = ?";
        boolean done = false;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, ipAddress);
            ps.setDate(2, df.dateConvert(new Date())); 
            ps.setString(3, userId);

            done = ps.executeUpdate() > 0; 

        } catch (SQLException e) {
            System.err.println("WARNING: Error executing updateLogins -> " + e.getMessage());
        }

        return done;
    }


    public boolean changePasswordOld(String userid, String password, String expiry)
    {
        String query;
        Connection con;
        PreparedStatement ps;
        boolean done;
        query = "UPDATE am_gb_User SET Password =?,password_changed = ?,password_expiry =  ?,must" +
"_change_pwd=? WHERE   USER_ID = ?"
;
        con = null;
        ps = null;
        done = false;
        try {
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, password);
        ps.setString(2, "Y");
        ps.setDate(3, df.dateConvert(df.addDayToDate(new Date(), Integer.parseInt(expiry))));
        ps.setString(4, "N");
        ps.setString(5, userid);
        done = ps.executeUpdate() != -1;
//        closeConnection(con, ps);
    } catch (Exception e) {
        System.out.println("WARNING:Error executing Query in changePassword ->"
                + e.getMessage());
    } finally {
        closeConnection(con, ps);
    }
    return done;
}
    
    public boolean changePassword(String userId, String password, String expiry) {
        String query = "UPDATE AM_GB_USER SET Password = ?, password_changed = ?, password_expiry = ?, must_change_pwd = ? WHERE USER_ID = ?";
        boolean done = false;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, password);
            ps.setString(2, "Y");
            ps.setDate(3, df.dateConvert(df.addDayToDate(new Date(), Integer.parseInt(expiry))));

            ps.setString(4, "N");
            ps.setString(5, userId);

            done = ps.executeUpdate() > 0; 

        } catch (SQLException e) {
            System.err.println("WARNING: Error executing changePassword -> " + e.getMessage());
        }

        return done;
    }


    public boolean updateLoginsOld(String userid, String status, String ipaddress)
    {
        String query;
        Connection con;
        PreparedStatement ps;
        PreparedStatement ps1 = null;
        boolean done;
         query = "UPDATE AM_GB_USER  SET LOGIN_STATUS= ?,LOGIN_SYSTEM=?,login_date = GetDate()" + " "
         		+ "WHERE   USER_ID = ?";
     //   query = (new StringBuilder()).append("UPDATE AM_GB_USER  SET LOGIN_STATUS= ").append(status).append(",LOGIN_SYSTEM='").append(ipaddress).append("',").append("login_date = GetDate()").append(" WHERE   USER_ID = ").append(userid).toString();
         System.out.println("I AM INSIDE LOGINLOGOUT 4 TRUE = "+query);
 		String mtid =  records.getCodeName("SELECT MAX(mtid) FROM  gb_user_login where USER_ID = ? ",userid);
 		String querySessionOut = "UPDATE gb_user_login SET time_out = ? WHERE user_id =? AND MTID = ?";
         con = null;
        ps = null;
        done = false;
        try {
//        con = getConnection();
//        ps = con.prepareStatement(query);
//        done = ps.executeUpdate() != -1;
        
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, status);
        ps.setString(2, ipaddress);
        ps.setString(3, userid);
        done = ps.executeUpdate() != -1;
        
        ps1 = con.prepareStatement(querySessionOut);
		ps1.setString(1,df.getDateTime().substring(10));
		ps1.setString(2, userid);
		ps1.setString(3, mtid);
		done=( ps1.executeUpdate()!=-1);
		
//        closeConnection(con, ps);
    }
        catch (Exception e) {
            System.out.println("WARNING:Error executing Query in updateLogins ->"
                    + e.getMessage());
        } finally {
            closeConnection(con, ps);
            closeConnection(con, ps1);
        }
        return done;
    }
    
    public boolean updateLogins(String userId, String status, String ipAddress) {
        String updateUserQuery = "UPDATE AM_GB_USER SET LOGIN_STATUS = ?, LOGIN_SYSTEM = ?, login_date = GETDATE() WHERE USER_ID = ?";
        String querySessionOut = "UPDATE gb_user_login SET time_out = ? WHERE user_id = ? AND MTID = ?";

        boolean done = false;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(updateUserQuery);
             PreparedStatement ps1 = con.prepareStatement(querySessionOut)) {

            // Update AM_GB_USER
            ps.setString(1, status);
            ps.setString(2, ipAddress);
            ps.setString(3, userId);
            int rowsUpdated = ps.executeUpdate();

            // Get latest MTID for the user
            String mtid = records.getCodeName("SELECT MAX(mtid) FROM gb_user_login WHERE USER_ID = ?", userId);

            // Update GB_USER_LOGIN session
            ps1.setString(1, df.getDateTime().substring(10));
            ps1.setString(2, userId);
            ps1.setString(3, mtid);
            int sessionUpdated = ps1.executeUpdate();

            
            done = rowsUpdated > 0 && sessionUpdated > 0;

        } catch (SQLException e) {
            System.err.println("WARNING: Error executing updateLogins -> " + e.getMessage());
        }

        return done;
    }


    public boolean queryPexpiryOld(String userid)
    {
        String query;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        boolean done;
        query = "SELECT password_expiry FROM AM_GB_USER   "
                + "WHERE USER_ID = ?";
 //       query = (new StringBuilder()).append("SELECT password_expiry FROM AM_GB_USER   WHERE USER_ID = ").append(userid).toString();
        con = null;
        ps = null;
        rs = null;
        done = false;
        try {
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, userid);
        rs = ps.executeQuery();
        while (rs.next()) {
            String exDate = rs.getString("password_expiry");
            int diff = df.getDayDifferenceNoABS(exDate, sdf.format(new Date()));
            if(diff <= 0)
            {
                done = true;
            }
        }
//        closeConnection(con, ps, rs);
        } catch (Exception e) {
            System.out.println("WARNING:Error executing Query in queryPexpiry ->"
                    + e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return done;
    }
    
    public boolean queryPexpiry(String userId) {
        String query = "SELECT password_expiry FROM AM_GB_USER WHERE USER_ID = ?";
        boolean expired = false;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String exDate = rs.getString("password_expiry");
                    int diff = df.getDayDifferenceNoABS(exDate, sdf.format(new Date()));
                    expired = diff <= 0; 
                }
            }

        } catch (SQLException e) {
            System.err.println("WARNING: Error executing queryPexpiry -> " + e.getMessage());
        }

        return expired;
    }


//    private Connection getConnection()
//    {
//        Connection con = null;
//        dc = new DataConnect("legendPlus");
//        try
//        {
//            con = dc.getConnection();
//        }
//        catch(Exception e)
//        {
//            System.out.println((new StringBuilder()).append("WARNING: Error getting connection ->").append(e.getMessage()).toString());
//        }
//        return con;
//    }
//    
	private Connection getConnection() {
		Connection con = null;
		try {
//        	if(con==null){
                Context initContext = new InitialContext();
                String dsJndi = "java:/legendPlus";
                DataSource ds = (DataSource) initContext.lookup(
                		dsJndi);
                con = ds.getConnection();
//        	}
		} catch (Exception e) {
			System.out.println("WARNING: Error 1 getting connection ->"
					+ e.getMessage());
		}
		//finally {
//			closeConnection(con);
//		}
		return con;
	}


    private void closeConnection(Connection con, Statement s)
    {
        try
        {
            if(s != null)
            {
                s.close();
            }
            if(con != null)
            {
                con.close();
            }
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARNING: Error getting connection ->").append(e.getMessage()).toString());
        }
    }

    private void closeConnection(Connection con, PreparedStatement ps)
    {
        try
        {
            if(ps != null)
            {
                ps.close();
            }
            if(con != null)
            {
                con.close();
            }
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARNING: Error closing connection ->").append(e.getMessage()).toString());
        }
    }

    private void closeConnection(Connection con, Statement s, ResultSet rs)
    {
        try
        {
            if(rs != null)
            {
                rs.close();
            }
            if(s != null)
            {
                s.close();
            }
            if(con != null)
            {
                con.close();
            }
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARNING: Error closing connection ->").append(e.getMessage()).toString());
        }
    }

    private void closeConnection(Connection con, PreparedStatement ps, ResultSet rs)
    {
        try
        {
            if(rs != null)
            {
                rs.close();
            }
            if(ps != null)
            {
                ps.close();
            }
            if(con != null)
            {
                con.close();
            }
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARNING: Error closing connection ->").append(e.getMessage()).toString());
        }
    }

    private boolean executeQuery(String query)
    {
        Connection con;
        PreparedStatement ps;
        boolean done;
        con = null;
        ps = null;
        done = false;
        try {
        con = getConnection();
        ps = con.prepareStatement(query);
        done = ps.execute();
 //       closeConnection(con, ps);
        }
        catch (Exception e) {
            System.out.println("WARNING:Error executing Query in executeQuery ->"
                    + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;
    }

    public ArrayList getAUsers()
    {
        String filter = " AND Login_Status = '1'";
        ArrayList list = getUserByQuery(filter);
        return list;
    }

    public String getBranchRestrictedUser(String userId)
    {
        String branchRestrict;
        String query;
        Connection c;
        ResultSet rs;
        PreparedStatement s;
        branchRestrict = "";
        query = "SELECT branch_restriction "
                + " FROM AM_GB_USER WHERE User_Id = ?";
 //       query = (new StringBuilder()).append("SELECT branch_restriction  FROM AM_GB_USER WHERE User_Id = '").append(userId).append("'").toString();
        c = null;
        rs = null;
        s = null;
        try {
        c = getConnection();
        s = c.prepareStatement(query);
        s.setString(1, userId);
        rs = s.executeQuery();
        while (rs.next()) {
            branchRestrict = rs.getString("branch_restriction");
        }
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(c, s, rs);
        }

        return branchRestrict;
    }

    public boolean updateExpiryDate(int user_id)
    {
        Connection con;
        PreparedStatement ps;
        boolean done;
        con = null;
        ps = null;
        done = false;
        try {
        String query = "update am_gb_user set expiry_date = null where user_id = ?";
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setInt(1, user_id);
        done = ps.executeUpdate() != -1;
//        closeConnection(con, ps);
        }
        catch (Exception e) {
            System.out.println("WARNING:Error updateExpiryDate() Query ->"
                    + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;
    }

    public boolean createManageUser(legend.admin.objects.User user, String limit)
    {
        Connection con;
        PreparedStatement ps;
        boolean done;
        con = null;
        ps = null;
        done = false;
        String query = "";
        try {
        String userId = (new ApplicationHelper()).getGeneratedId("AM_GB_USER");
        if(user.getExpiryDate() == "" || user.getExpiryDate().equalsIgnoreCase("null") || user.getExpiryDate() == null || user.getExpiryDate().equalsIgnoreCase(""))
        {
             query = "INSERT INTO AM_GB_USER( User_Name, Full_Name, Legacy_Sys_Id, Class, Branch, Pass" +
"word, Phone_No, Is_Supervisor, Must_Change_Pwd, Login_Status, User_Status, UserI" +
"d, Create_Date, Password_Expiry,Fleet_Admin, Email,User_id,branch_restriction, E" +
"xpiry_Days,Approval_Limit,dept_restriction,dept_code,UnderTaker,region_code,zone_code," +
"region_restriction,zone_restriction,Facility_Admin,Store_Admin)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
;
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getUserFullName());
            ps.setString(3, user.getLegacySystemId());
            ps.setString(4, user.getUserClass());
            ps.setString(5, user.getBranch());
            ps.setString(6, user.getPassword());
            ps.setString(7, user.getPhoneNo());
            ps.setString(8, user.getIsSupervisor());
            ps.setString(9, user.getMustChangePwd());
            ps.setString(10, user.getLoginStatus());
            ps.setString(11, user.getUserStatus());
            ps.setString(12, user.getCreatedBy());
            ps.setDate(13, df.dateConvert(new Date()));
            ps.setDate(14, df.dateConvert(df.addDayToDate(new Date(), Integer.parseInt(user.getPwdExpiry()))));
            ps.setString(15, user.getFleetAdmin());
            ps.setString(16, user.getEmail());
            ps.setString(17, userId);
            ps.setString(18, user.getBranchRestrict());
            ps.setInt(19, user.getExpiryDays());
            ps.setString(20, limit);
            ps.setString(21, user.getDeptRestrict());
            ps.setString(22, user.getDeptCode());
            ps.setString(23, user.getUnderTaker());    
            ps.setString(24, user.getRegionCode());
            ps.setString(25, user.getZoneCode());
            ps.setString(26, user.getRegionRestrict());
            ps.setString(27, user.getZoneRestrict());
            ps.setString(28, user.getIsFacilityAdministrator());
            ps.setString(29, user.getIsStoreAdministrator());
            done = ps.executeUpdate() != -1;
        } else
        {
             query = "INSERT INTO AM_GB_USER( User_Name, Full_Name, Legacy_Sys_Id, Class, Branch, Pass" +
"word, Phone_No, Is_Supervisor, Must_Change_Pwd, Login_Status, User_Status, UserI" +
"d, Create_Date, Password_Expiry,Fleet_Admin, Email,User_id,branch_restriction, E" +
"xpiry_Days,Expiry_Date,Approval_Limit,dept_code,dept_restriction,UnderTaker,region_code,zone_code," +
"region_restriction,zone_restriction,Facility_Admin,Store_Admin)VALUES (?,?,?,?,?,?,?,?,?,?,?,?," +
"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
;
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getUserFullName());
            ps.setString(3, user.getLegacySystemId());
            ps.setString(4, user.getUserClass());
            ps.setString(5, user.getBranch());
            ps.setString(6, user.getPassword());
            ps.setString(7, user.getPhoneNo());
            ps.setString(8, user.getIsSupervisor());
            ps.setString(9, user.getMustChangePwd());
            ps.setString(10, user.getLoginStatus());
            ps.setString(11, user.getUserStatus());
            ps.setString(12, user.getCreatedBy());
            ps.setDate(13, df.dateConvert(new Date()));
            ps.setDate(14, df.dateConvert(df.addDayToDate(new Date(), Integer.parseInt(user.getPwdExpiry()))));
            ps.setString(15, user.getFleetAdmin());
            ps.setString(16, user.getEmail());
            ps.setString(17, userId);
            ps.setString(18, user.getBranchRestrict());
            ps.setInt(19, user.getExpiryDays());
            String temDate = user.getExpiryDate();
            ps.setDate(20, df.dateConvert(temDate));
            ps.setString(21, limit);
            ps.setString(22, user.getDeptCode());
            ps.setString(23, user.getDeptRestrict());
            ps.setString(24, user.getUnderTaker());   
            ps.setString(25, user.getRegionCode());
            ps.setString(26, user.getZoneCode());
            ps.setString(27, user.getRegionRestrict());
            ps.setString(28, user.getZoneRestrict());
            ps.setString(29, user.getIsFacilityAdministrator());
            ps.setString(30, user.getIsStoreAdministrator());
            done = ps.executeUpdate() != -1;
        }
//        closeConnection(con, ps);
        createPasswordHistory(userId, user.getPassword());
        }
        catch (Exception e) {
            System.out.println("WARNING:Error executing Query in createManageUser ->"
                    + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;


    }


    public boolean updateManageUser(legend.admin.objects.User user, String limit)
    {
        Connection con;
        PreparedStatement ps;
        boolean done;
        con = null;
        ps = null;
        done = false;
        String query = "";
        try {
        if(user.getExpiryDate() == "" || user.getExpiryDate().equalsIgnoreCase("null") || user.getExpiryDate().equalsIgnoreCase(""))
        {
             query = "UPDATE AM_GB_USER  SET  User_Name = ?, Full_Name = ? ,Legacy_Sys_Id = ?, Class =" +
" ?, Branch = ?, Password = ?, Phone_No = ?,Is_Supervisor = ?,Must_Change_Pwd = ?" +
", Login_Status = ?, User_Status = ?, Fleet_Admin = ?, Email = ?,branch_restricti" +
"on =?, Expiry_Days =?,Approval_Limit = ?,region_code=?,zone_code=?,region_restriction=?,zone_restriction=?,Facility_Admin = ?,Store_Admin = ? WHERE User_Id = ?"
;
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getUserFullName());
            ps.setString(3, user.getLegacySystemId());
            ps.setString(4, user.getUserClass());
            ps.setString(5, user.getBranch());
            ps.setString(6, user.getPassword());
            ps.setString(7, user.getPhoneNo());
            ps.setString(8, user.getIsSupervisor());
            ps.setString(9, user.getMustChangePwd());
            ps.setString(10, user.getLoginStatus());
            ps.setString(11, user.getUserStatus());
            ps.setString(12, user.getFleetAdmin());
            ps.setString(13, user.getEmail());
            ps.setString(14, user.getBranchRestrict());
            ps.setInt(15, user.getExpiryDays());
            ps.setString(16, limit);
            ps.setString(17, user.getRegionCode());
            ps.setString(18, user.getZoneCode());
            ps.setString(19, user.getRegionRestrict());
            ps.setString(20, user.getZoneRestrict());
            ps.setString(21, user.getIsFacilityAdministrator());
            ps.setString(22, user.getIsStoreAdministrator());
            ps.setString(23, user.getUserId());

            done = ps.executeUpdate() != -1;
        } else
        {
             query = "UPDATE AM_GB_USER  SET  User_Name = ?, Full_Name = ? ,Legacy_Sys_Id = ?, Class =" +
" ?, Branch = ?, Password = ?, Phone_No = ?,Is_Supervisor = ?,Must_Change_Pwd = ?" +
", Login_Status = ?, User_Status = ?, Fleet_Admin = ?, Email = ?,branch_restricti" +
"on =?, Expiry_Days =?, Expiry_Date=?,Approval_Limit = ?,dept_code=?,region_code=?,zone_code=?,region_restriction=?,zone_restriction=?" +
",Facility_Admin = ?,Store_Admin = ?  WHERE User_Id = ?"
;
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getUserFullName());
            ps.setString(3, user.getLegacySystemId());
            ps.setString(4, user.getUserClass());
            ps.setString(5, user.getBranch());
            ps.setString(6, user.getPassword());
            ps.setString(7, user.getPhoneNo());
            ps.setString(8, user.getIsSupervisor());
            ps.setString(9, user.getMustChangePwd());
            ps.setString(10, user.getLoginStatus());
            ps.setString(11, user.getUserStatus());
            ps.setString(12, user.getFleetAdmin());
            ps.setString(13, user.getEmail());
            ps.setString(14, user.getBranchRestrict());
            ps.setInt(15, user.getExpiryDays());
            ps.setDate(16, df.dateConvert(user.getExpiryDate()));
            ps.setString(17, limit);
            ps.setString(18, user.getDeptCode());
            ps.setString(19, user.getRegionCode());
            ps.setString(20, user.getZoneCode());
            ps.setString(21, user.getRegionRestrict());
            ps.setString(22, user.getZoneRestrict());
            ps.setString(22, user.getIsFacilityAdministrator());
            ps.setString(23, user.getIsStoreAdministrator());
            ps.setString(24, user.getUserId());

            done = ps.executeUpdate() != -1;
//            closeConnection(con, ps);
        }
        if(user.getExpiryDays() == 0)
        {
            updateExpiryDate(Integer.parseInt(user.getUserId()));
        }
    } catch (Exception e) {
        System.out.println("WARNING:Error executing Query in updateManageUser ->"
                + e.getMessage());
    } finally {
        closeConnection(con, ps);
    }
    return done;

}

    public void compareAuditValues(String role_view, String role_addn, String role_edit, String class_id, String role_id, String user_id, String branchCode,
            int loginId, String eff_date)
    {
        AuditTrailGen atg;
        String role_view1;
        String role_addn1;
        String role_edit1;
        String query;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        atg = new AuditTrailGen();
        role_view1 = "";
        role_addn1 = "";
        role_edit1 = "";
        query = "SELECT role_view, role_addn, role_edit FROM am_ad_class_privileges WHERE  clss_u" +
"uid = ? AND Role_uuid=?"
;  
        con = null;
        ps = null;
        rs = null;
        boolean done = false;
        try {
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setInt(1, Integer.parseInt(class_id));
        ps.setInt(2, Integer.parseInt(role_id));
//        System.out.println((new StringBuilder()).append("==============**=====class_id===============").append(class_id).toString());
//        System.out.println((new StringBuilder()).append("==============**=====role_id===============").append(role_id).toString());
        rs = ps.executeQuery();
//        System.out.println((new StringBuilder()).append("===================ResultSET===============").append(rs).toString());
        while (rs.next()) {
//            System.out.println((new StringBuilder()).append("===================WHILE INSIDE A ResultSET===============").append(rs).toString());
            role_view1 = rs.getString("role_view");
//            System.out.println((new StringBuilder()).append("===================role_view1===============").append(role_view1).toString());
            role_addn1 = rs.getString("role_addn");
//            System.out.println((new StringBuilder()).append("===================role_addn1===============").append(role_addn1).toString());
            role_edit1 = rs.getString("role_edit");
        }

//        System.out.println((new StringBuilder()).append("\n\n old values >> ").append(role_view1).append("\n\n new values >>").append(role_view).toString());
//        System.out.println((new StringBuilder()).append("\n\n old values >> ").append(role_addn1).append("\n\n new values >>").append(role_addn).toString());
//        System.out.println((new StringBuilder()).append("\n\n old values >> ").append(role_edit1).append("\n\n new values >>").append(role_edit).toString());
//        System.out.println((new StringBuilder()).append("\n\n role_id>> ").append(role_id).toString());
        if(!role_view1.equalsIgnoreCase(role_view))
        {
            atg.logAuditTrailSecurityComp("am_ad_class_privileges", branchCode, loginId, eff_date, role_id, role_view1, role_view, "role_view");
        }
        if(!role_addn1.equalsIgnoreCase(role_addn))
        {
            atg.logAuditTrailSecurityComp("am_ad_class_privileges", branchCode, loginId, eff_date, role_id, role_addn1, role_addn, "role_addn");
        }
        if(!role_edit1.equalsIgnoreCase(role_edit))
        {
            atg.logAuditTrailSecurityComp("am_ad_class_privileges", branchCode, loginId, eff_date, role_id, role_edit1, role_edit, "role_edit");
        }
//        System.out.println("===================logAuditTrailSecurityComp= within  compareAuditValues========" +
//"======"
//);
//        closeConnection(con, ps, rs);
    } catch (Exception e) {
        System.out.println("WARNING:Error executing Query User Full NamecompareAuditValues: ->"
                + e.getMessage());
    } finally {
        closeConnection(con, ps, rs);
    }
    //return UserFullName;
}
    
    public void compareAuditValues(String class_status, String default_class_id, String class_id, String user_id, String branchCode,
            int loginId, String eff_date, String description)
    {
        AuditTrailGen atg;
        String role_class_status;
        String role_default_Class_Id;
        String query;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        atg = new AuditTrailGen();
        role_class_status = "";
        role_default_Class_Id = "";
        query = "SELECT class_status, DefaultClass_id FROM am_gb_classDisable WHERE class_id=?"
;  
        con = null;
        ps = null;
        rs = null;
        boolean done = false;
        try {
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setInt(1, Integer.parseInt(class_id));
//        System.out.println((new StringBuilder()).append("==============**=====class_id===============").append(class_id).toString());
//        System.out.println((new StringBuilder()).append("==============**=====role_id===============").append(role_id).toString());
		rs = ps.executeQuery();
//        System.out.println((new StringBuilder()).append("===================ResultSET===============").append(rs).toString());
        while (rs.next()) {
//            System.out.println((new StringBuilder()).append("===================WHILE INSIDE A ResultSET===============").append(rs).toString());
            role_class_status = rs.getString("class_status");
//            System.out.println((new StringBuilder()).append("===================role_view1===============").append(role_view1).toString());
            role_default_Class_Id = rs.getString("DefaultClass_id");
//            System.out.println((new StringBuilder()).append("===================role_addn1===============").append(role_addn1).toString());
        }

//        System.out.println((new StringBuilder()).append("\n\n old values >> ").append(role_view1).append("\n\n new values >>").append(role_view).toString());
//        System.out.println((new StringBuilder()).append("\n\n old values >> ").append(role_addn1).append("\n\n new values >>").append(role_addn).toString());
//        System.out.println((new StringBuilder()).append("\n\n old values >> ").append(role_edit1).append("\n\n new values >>").append(role_edit).toString());
//        System.out.println((new StringBuilder()).append("\n\n role_id>> ").append(role_id).toString());
        if(!role_class_status.equalsIgnoreCase(class_status))
        {
            atg.logAuditTrailSecurityComp("am_gb_classDisable", branchCode, loginId, eff_date, class_id, role_class_status, class_status, "class_status", description);
        }
        if(!role_default_Class_Id.equalsIgnoreCase(default_class_id))
        {
            atg.logAuditTrailSecurityComp("am_gb_classDisable", branchCode, loginId, eff_date, class_id, role_default_Class_Id, default_class_id, "DefaultClass_id", description);
        }
//        System.out.println("===================logAuditTrailSecurityComp= within  compareAuditValues========" +
//"======"
//);
//        closeConnection(con, ps, rs);
    } catch (Exception e) {
        System.out.println("WARNING:Error executing Query User Full NamecompareAuditValues: ->"
                + e.getMessage());
    } finally {
        closeConnection(con, ps, rs);
    }
    //return UserFullName;
}    
    
    public ArrayList<UserDisableClass> getAllClassDisableDetails() {
		ArrayList<UserDisableClass> list = new ArrayList<UserDisableClass>();
		Connection con = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    try {
	    String query = "select class_id, class_desc, class_status,class_name, DefaultClass_id from am_gb_classDisable order by class_name";
	    con = getConnection();
	    ps = con.prepareStatement(query);
	    rs = ps.executeQuery();
	    while(rs.next()) {
	    	UserDisableClass userDisableClass = new UserDisableClass();

	    	userDisableClass.setClassId(rs.getString(1));
	    	userDisableClass.setClassDesc(rs.getString(2));
	    	userDisableClass.setClassStatus(rs.getString(3));
	    	userDisableClass.setClassName(rs.getString(4));
	    	userDisableClass.setDefaultClassId(rs.getString(5) != "NULL" ? rs.getString(5) : "NULL");
	    	
	    	list.add(userDisableClass);
	    	
	    }
	    }catch(Exception e){
	    	e.getMessage();
	    }

		
		
		return list;
	}
    
    public ArrayList<SecurityRerouteClass> getAllSecurityRerouteDetails(String superId) {
		ArrayList<SecurityRerouteClass> list = new ArrayList<SecurityRerouteClass>();
		Connection con = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    try {
	    String query = "select Transaction_Id,tran_type,asset_id,description from am_asset_approval where process_status='P' and super_id=?";
	    con = getConnection();
	    ps = con.prepareStatement(query);
		ps.setString(1, superId);
	    rs = ps.executeQuery();
	    while(rs.next()) {
	    	SecurityRerouteClass securityRerouteClass = new SecurityRerouteClass();

	    	securityRerouteClass.setTransaction_Id(rs.getInt(1));
	    	securityRerouteClass.setTran_type(rs.getString(2));
	    	securityRerouteClass.setAsset_Id(rs.getString(3));
	    	securityRerouteClass.setDescription(rs.getString(4) != "NULL" ? rs.getString(4) : "NULL");
	    	
	    	list.add(securityRerouteClass);
	    	
	    }
	    }catch(Exception e){
	    	e.getMessage();
	    }

		
		
		return list;
	}
    
    public ArrayList<SupervisorDetailsClass> getAllSupervisorDetails(String superId) {
  		ArrayList<SupervisorDetailsClass> list = new ArrayList<SupervisorDetailsClass>();
  		Connection con = null;
  	    PreparedStatement ps = null;
  	    ResultSet rs = null;
  	    try {
  	    String query = "select user_id, user_name+' - '+full_name AS Full_Detail from am_gb_user where "
  	    		+ "user_Name != ? and is_supervisor = 'Y' and User_Status='ACTIVE' order by user_name";
  	    con = getConnection();
  	    ps = con.prepareStatement(query);
  		ps.setString(1, superId);
  	    rs = ps.executeQuery();
  	    while(rs.next()) {
  	    	SupervisorDetailsClass supervisorDetailsClass = new SupervisorDetailsClass();

  	    	supervisorDetailsClass.setUser_Id(rs.getInt(1));
  	    	supervisorDetailsClass.setFull_Detail(rs.getString(2));
  	    	
  	    	list.add(supervisorDetailsClass);
  	    	
  	    }
  	    }catch(Exception e){
  	    	e.getMessage();
  	    }

  		
  		
  		return list;
  	}
    
    
    public ArrayList<UserEnableClass> getAllClassEnableDetails() {
		ArrayList<UserEnableClass> list = new ArrayList<UserEnableClass>();
		Connection con = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    try {
	    String query = "select distinct class_id, class_desc, class_name, class_status from am_gb_classEnable order by class_name ";
	    con = getConnection();
	    ps = con.prepareStatement(query);
	    rs = ps.executeQuery();
	    while(rs.next()) {  
	    	UserEnableClass userEnableClass = new UserEnableClass();

	    	userEnableClass.setClassId(rs.getString(1));
	    	userEnableClass.setClassDesc(rs.getString(2));
	    	userEnableClass.setClassName(rs.getString(3));
	    	userEnableClass.setClassStatus(rs.getString(4));
	    	
	    	list.add(userEnableClass);
	    	
	    }
	    }catch(Exception e){
	    	e.getMessage();
	    }

		
		
		return list;
	}

    
    

    public boolean updateClassPrivilege1(ArrayList list, int userid, String branchCode, int loginId, String eff_date)
    {
        for(int i = 0; i < list.size(); i++)
        {
            System.out.println();
            ClassPrivilege cp = (ClassPrivilege)list.get(i);
            updateClassPrivilege1(cp, userid, branchCode, loginId, eff_date);
        }
        System.out.println("Nos of rtecords updated = " + list.size());
 //       System.out.println((new StringBuilder()).append("Nos of rtecords updated = ").append(list.size()).toString());
        return true;
    }

    public void updateClassPrivilege1(ClassPrivilege cp, int userid, String branchCode, int loginId, String eff_date)
    {
        String query;
        Connection con;
        PreparedStatement ps;
        AuditTrailGen atg1 = new AuditTrailGen();
        query = "UPDATE am_ad_class_privileges SET role_view = ?,role_addn = ?,role_edit = ? WHER" +
"E clss_uuid=? AND Role_uuid=? "
;
        con = null;
        ps = null;
        ResultSet rs = null;
        int d[] = null;
        try {
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, cp.getRole_view());
        ps.setString(2, cp.getRole_addn());
        ps.setString(3, cp.getRole_edit());
        ps.setString(4, cp.getClss_uuid());
        ps.setString(5, cp.getRole_uuid());
        compareAuditValues(cp.getRole_view(), cp.getRole_addn(), cp.getRole_edit(), cp.getClss_uuid(), cp.getRole_uuid(), String.valueOf(userid), branchCode, loginId, eff_date);

//        System.out.println("\n\n >>>>>>>>>>>>>>>>>> ps.execute() " + ps.executeUpdate());
//        closeConnection(con, ps);
        }
        catch (Exception ex) {
        System.out.println("WARN: Error doing updateClassPrivilege ->" + ex);
    } finally {
        closeConnection(con, ps);
    }
}

    public String getUserLogonStatus(String userName)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet res;
        String result;
        con = null;
        ps = null;
        res = null;
        result = null;
        try {
        con = getConnection();
        String query = "select Login_Status from am_gb_User where user_name = ?";
        ps = con.prepareStatement("select Login_Status from am_gb_User where user_name = ?");
        ps.setString(1, userName);
        res = ps.executeQuery();
        if(res.next())
        {
            result = res.getString("Login_Status");
        }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(con, ps, res);
        }
        return result;
    }

    public boolean updateLoginAsAboveLimit(String user_name, String ip)
    {
        Connection con;
        PreparedStatement ps;
        boolean done;
        con = null;
        ps = null;
        done = false;
        try {
        String query = "update am_gb_user set login_status = 3, login_system =? where user_name = ?";
       
      	 String nameFilter = user_name;
       	nameFilter = nameFilter.replaceAll("\\s", "");
       	 String ipFilter = ip;
       	ipFilter = ipFilter.replaceAll("\\s", "");
       	
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, ipFilter);
        ps.setString(2, nameFilter);
        done = ps.executeUpdate() != -1;
    }
        catch (Exception e) {
            System.out.println("WARNING:Error updateExpiryDate() Query in updateLoginAsAboveLimit ->"
                    + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }

    public String getCompanyLoginAttempt()
    {
        Connection con;
        PreparedStatement ps;
        ResultSet res;
        String result;
        con = null;
        ps = null;
        res = null;
        result = null;
        try {
        con = getConnection();
        String query = "select attempt_logon from am_gb_company";
        ps = con.prepareStatement("select attempt_logon from am_gb_company");
        res = ps.executeQuery();
        if(res.next())
        {
            result = res.getString("attempt_logon");
        }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(con, ps, res);
        }
        return result;
    }

    public boolean updateManageUser(User user)
    {
        Connection con;
        PreparedStatement ps;
        boolean done;
        con = null;
        ps = null;
        done = false;
        String query = "";
        try {
         query = "UPDATE AM_GB_USER  SET  Password = ?, Must_Change_Pwd = ? ,Login_Status = ? WHER" +
"E User_Id = ?"
;
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, user.getPassword());
        ps.setString(2, user.getMustChangePwd());
        ps.setString(3, user.getLoginStatus());
        ps.setString(4, user.getUserId());
        done = ps.executeUpdate() != -1;
    } catch (Exception e) {
        System.out.println("WARNING:Error executing Query in updateManageUser ->" + e.getMessage());
        e.printStackTrace();
    } finally {
        closeConnection(con, ps);
    }
    return done;


}

    public String Name(String userName, String pagePassword)
    {
        String output = pagePassword.trim();
        String prefix = userName.trim().substring(0, 2);
        int length = userName.trim().length();
        int length2 = length - 2;
        String surrfix = userName.trim().substring(length2, length);
        String Password = (new StringBuilder()).append(surrfix).append(output).append(prefix).toString();
        return Password;
    }

    public boolean Name(String userName, String pagePassword, String databasePassword)
    {
        boolean result = false;
        String output = pagePassword.trim();
        String prefix = userName.trim().substring(0, 2);
        int length = userName.trim().length();
        int length2 = length - 2;
        String surrfix = userName.trim().substring(length2, length);
        String Password = (new StringBuilder()).append(surrfix).append(output).append(prefix).toString();
        
        if(Password.trim().equals(databasePassword.trim()))
        {
            result = true;
          
        }
        return result;
    }

    public java.util.ArrayList getUserByQuery(String filter, String Name, String pass)
    {
        java.util.ArrayList _list = new java.util.ArrayList();
        legend.admin.objects.User user = null;
        String query;
        Connection c;
        ResultSet rs;
        Statement s;
        _list = new ArrayList();
        query = "SELECT User_Id, lower(User_Name) User_Name, Full_Name, Legacy_Sys_Id, Class, Bra" +
"nch, Password, Phone_No, is_Supervisor, Must_Change_Pwd, Login_Status, User_Stat" +
"us, UserId, Create_Date, Password_Expiry, Login_Date, Login_System, Fleet_Admin," +
" email, Branch, password_changed,Expiry_Date,branch_restriction, approval_limit," +
"approval_level,token_required,region_code,zone_code,region_restriction,zone_restriction,Facility_Admin,Store_Admin FROM AM_GB_USER WHERE User_Id IS NOT NULL "+filter+""
;
        query = (new StringBuilder()).append(query).append(filter).toString();
       System.out.println("query in getUserByQuery with Three Parameter: "+query);
        c = null;
        rs = null;
        s = null;
        try {
        c = getConnection();
        s = c.createStatement();
        rs = s.executeQuery(query);
        while (rs.next()) {
            String userId = rs.getString("User_Id");
            String userName = rs.getString("User_Name");
            String password = rs.getString("Password");
            if(Name(userName, pass, cm.decrypt(password)))
            {
                String fullName = rs.getString("Full_Name");
                String legacySysId = rs.getString("Legacy_Sys_Id");
                String Class = rs.getString("Class");
                String branch = rs.getString("Branch");
                String phoneNo = rs.getString("Phone_No");
                String isSupervisor = rs.getString("Is_Supervisor");
                String mustChangePwd = rs.getString("Must_Change_Pwd");
                String loginStatus = rs.getString("Login_Status");
                String userStatus = rs.getString("User_Status");
                String user_Id = rs.getString("UserId");
                String createDate = rs.getString("Create_Date");
                String passwordExpiry = rs.getString("Password_Expiry");
                String loginDate = rs.getString("Login_Date");
                String loginSystem = rs.getString("Login_System");
                String fleetAdmin = rs.getString("Fleet_Admin");
                String email = rs.getString("email");
                String branchCode = rs.getString("Branch");
                String pwdChanged = rs.getString("password_changed");
                Date expiry_date = rs.getDate("Expiry_Date");
                String branch_restriction = rs.getString("branch_restriction");
                String apprvLimit = rs.getString("approval_limit");
                String apprvLevel = rs.getString("approval_level");
                String tokenRequired = rs.getString("token_required");
                String regionCode = rs.getString("region_code");
                String zoneCode = rs.getString("zone_code");
                String regionRestrict = rs.getString("region_restriction");
                String zoneRestrict = rs.getString("zone_restriction");
                String isFacilityAdministrator = rs.getString("Facility_Admin");
                String isStoreAdministrator = rs.getString("Store_Admin");
                System.out.println((new StringBuilder()).append("####################################### Token Read from the DB here ====").append(tokenRequired).toString());
                System.out.println((new StringBuilder()).append(" The sent tokenRequired ==== ").append(tokenRequired).toString());

                user = new legend.admin.objects.User();
                user.setUserId(userId);
                user.setUserName(userName);
                user.setUserFullName(fullName);
                user.setLegacySystemId(legacySysId);
                user.setUserClass(Class);
                user.setBranch(branch);
                user.setPassword(password);
                user.setPhoneNo(phoneNo);
                user.setIsSupervisor(isSupervisor);
                user.setMustChangePwd(mustChangePwd);
                user.setLoginStatus(loginStatus);
                user.setUserStatus(userStatus);
                user.setCreatedBy(user_Id);
                user.setCreateDate(createDate);
                user.setPwdExpiry(passwordExpiry);
                user.setLastLogindate(loginDate);
                user.setLoginSystem(loginSystem);
                user.setFleetAdmin(fleetAdmin);
                user.setEmail(email);
                user.setBranch(branchCode);
                user.setPwdChanged(pwdChanged);
                user.setApprvLimit(apprvLimit);
                user.setBranchRestrict(branch_restriction);
                user.setApprvLevel(apprvLevel);
                user.setExpDate(expiry_date);
                user.setRegionCode(regionCode);
                user.setRegionRestrict(regionRestrict);
                user.setZoneCode(zoneCode);
                user.setZoneRestrict(zoneRestrict);
                user.setIsFacilityAdministrator(isFacilityAdministrator);
                user.setIsStoreAdministrator(isStoreAdministrator);
                if(tokenRequired != null && tokenRequired.trim().equalsIgnoreCase("Y"))
                {
                    user.setTokenRequired(true);
                } else
                {
                    user.setTokenRequired(false);
                }
                System.out.println((new StringBuilder()).append("##################################### After setting the user the token of the us" +
"er =="
).append(user.isTokenRequired()).toString());
                _list.add(user);
            }
        }
//        closeConnection(c, s, rs);
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        closeConnection(c, s, rs);
    }
    return _list;

}

    public PasswordHistory getPasswordHistory(String userid)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        String query;
        PasswordHistory ph;
        con = null;
        ps = null;
        rs = null;

        	query = "select * from ad_password_history where userid = ?";
         ph = null;
         try {
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, userid);
        rs = ps.executeQuery();
        
        while (rs.next()) {
            String userId = rs.getString("USERID");
            int counter = rs.getInt("COUNTER");
            String password1 = rs.getString("PASSWORD1");
            String password2 = rs.getString("PASSWORD2");
            String password3 = rs.getString("PASSWORD3");
            String password4 = rs.getString("PASSWORD4");
            String password5 = rs.getString("PASSWORD5");
            String changeDate = rs.getString("CHANGEDATE");
            ph = new PasswordHistory(userId, counter, password1, password2, password3, password4, password5);
        }
//        closeConnection(con, ps, rs);
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(con, ps, rs);
        }


        return ph;
    }

    public boolean checkPassword(String userid, String password)
    {
        boolean result = false;
        boolean result1 = true;
        boolean result2 = true;
        boolean result3 = true;
        boolean result4 = true;
        boolean result5 = true;
        PasswordHistory values = getPasswordHistory(userid);
        if(password.equals(values.getPassword1()))
        {
            result1 = false;
        }
        if(password.equals(values.getPassword2()))
        {
            result2 = false;
        }
        if(password.equals(values.getPassword3()))
        {
            result3 = false;
        }
        if(password.equals(values.getPassword4()))
        {
            result4 = false;
        }
        if(password.equals(values.getPassword5()))
        {
            result5 = false;
        }
        if(result1 && result2 && result3 && result4 && result5)
        {
            result = true;
        }
        return result;
    }

    public boolean updatePasswordHistory(PasswordHistory pass, String password)
    {
        Connection con;
        PreparedStatement ps;
        boolean done;
        String query;
        int counter;
        con = null;
        ps = null;
        done = false;
        query = "";
        counter = 0;
        try {
        if(pass.getCounter() == 1)
        {
            counter = pass.getCounter() + 1;
            query = "UPDATE ad_password_history  SET  COUNTER = ?, PASSWORD1 = ?  ,CHANGEDATE = ? WHE" +
"RE USERID = ?"
;
        }
        if(pass.getCounter() == 2)
        {
            counter = pass.getCounter() + 1;
            query = "UPDATE ad_password_history  SET  COUNTER = ?, PASSWORD2 = ?  ,CHANGEDATE = ? WHE" +
"RE USERID = ?"
;
        }
        if(pass.getCounter() == 3)
        {
            counter = pass.getCounter() + 1;
            query = "UPDATE ad_password_history  SET  COUNTER = ?, PASSWORD3 = ?  ,CHANGEDATE = ? WHE" +
"RE USERID = ?"
;
        }
        if(pass.getCounter() == 4)
        {
            counter = pass.getCounter() + 1;
            query = "UPDATE ad_password_history  SET  COUNTER = ?, PASSWORD4 = ?  ,CHANGEDATE = ? WHE" +
"RE USERID = ?"
;
        }
        if(pass.getCounter() == 5)
        {
            counter = 1;
            query = "UPDATE ad_password_history  SET  COUNTER = ? , PASSWORD5 = ? ,CHANGEDATE = ? WHE" +
"RE USERID = ?"
;
        }
        con = getConnection();
        System.out.println((new StringBuilder()).append("--").append(query).append("--").toString());
        ps = con.prepareStatement(query);
        ps.setInt(1, counter);
        ps.setString(2, password);
        ps.setDate(3, df.dateConvert(new Date()));
        ps.setString(4, pass.getUserId());
        done = ps.executeUpdate() != -1;
//        closeConnection(con, ps);
        }
        catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->ad_password_history" + e.getMessage());
            e.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
        return done;


    }

    public boolean login(String location, String compName, String Apps, String LicfileName)
    {
        boolean result = false;
        try
        {
            File file = new File(location);
            Properties props = new Properties();
            InputStream in = new FileInputStream(file);
            Licencer lic = new Licencer();
            props.load(in);
            String strCompany = props.getProperty("comp");
            String app = props.getProperty("app");
            String license = props.getProperty("lic-code");
            String authcode = props.getProperty("author.code");
            license = license == null ? "" : license.replaceAll("-", "");
//            if(lic.isKeyOkay(license, strCompany, app, Long.parseLong(authcode)))
            if(lic.isKeyOkay(license, strCompany, app, Long.parseLong(authcode), LicfileName) && strCompany.equalsIgnoreCase(compName) && Apps.equalsIgnoreCase(app))
            {
                result = true;
            }
        } 
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    public boolean changePassword2(String userid, String password, String expiry)
    {
        boolean result = false;
        PasswordHistory values = getPasswordHistory(userid);
        if(checkPassword(userid, password) && changePassword(userid, password, expiry) && updatePasswordHistory(values, password))
        {
            result = true;
        }
        return result;
    }

    public boolean changePassword3(String userid, String password, String expiry)
    {
        boolean result = false;
        PasswordHistory values = getPasswordHistory(userid);
        if(checkPassword(userid, password) && changePassword(userid, password, expiry) && updatePasswordHistory(values, password))
        {
            result = true;
        }
        return result;
    }

    public boolean createPasswordHistory(String userid, String Password)
    {
        Connection con;
        PreparedStatement ps;
        boolean done;
        String query;
        con = null;
        ps = null;
        done = false;
/*        query = "INSERT INTO ad_password_history(USERID,COUNTER,PASSWORD1,PASSWORD2,PASSWORD3,PAS" +
"SWORD4,PASSWORD5)    VALUES (?,?,?,?,?,?,?)"
;*/
//        query = "INSERT INTO ad_password_history(USERID,COUNTER,PASSWORD)   VALUES (?,?,?,?,?,?,?)";
        query = "INSERT INTO ad_password_history(USERID,COUNTER,PASSWORD)   VALUES (?,?,?)";
        try {
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, userid);
        ps.setInt(2, 1);
        ps.setString(3, Password);
 /*       ps.setString(4, Password);
        ps.setString(5, Password);
        ps.setString(6, Password);
        ps.setString(7, Password);*/
        done = ps.executeUpdate() != -1;
//        closeConnection(con, ps);
        }
        catch (Exception e) {
            System.out.println(this.getClass().getName()
                    + " ERROR:Error inserting ad_password_history  ->");
            e.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }

    public boolean updatePasswordHistory2(int counter, String password, String userId)
    {
        Connection con;
        PreparedStatement ps;
        boolean done;
        int limitValue;
        int freq;
        con = null;
        ps = null;
        done = false;
        String query = "";
        int limit = 0;
        limitValue = 0;
        freq = 0;

        limit = getPasswordHistory3(userId);
        if(limit == counter)
        {
            limitValue = 1;
        } else
        {
            limitValue = limit + 1;
        }
        int frequency = getFreq(userId);
        System.out.println((new StringBuilder()).append("frequency  ").append(frequency).toString());
        freq = frequency + limitValue;
        if(frequency == counter)
        {
            freq = 1;
        }
        System.out.println((new StringBuilder()).append("freq  ").append(freq).toString());
        updateFreq(freq, userId);
        try {
         query = "UPDATE ad_password_history  SET  LIMIT = ?, PASSWORD = ?  ,CHANGEDATE = ? WHERE " +
"USERID = ? AND COUNTER = ?  "
;
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setInt(1, limitValue);
        ps.setString(2, password);
        ps.setTimestamp(3, getDateTime(new Date()));
        ps.setString(4, userId);
        ps.setInt(5, freq);
        done = ps.executeUpdate() != -1;
 //       closeConnection(con, ps);
    }
        catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->ad_password_history" + e.getMessage());
            e.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
        return done;


    }

    public boolean updateFreq(int freq, String userId)
    {
        Connection con;
        PreparedStatement ps;
        boolean done;
        con = null;
        ps = null;
        done = false;
        String query = "";
        try {
         query = "UPDATE ad_password_history  SET  freq = ?  WHERE USERID = ?  ";
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setInt(1, freq);
        ps.setString(2, userId);
        done = ps.executeUpdate() != -1;
//        closeConnection(con, ps);
        }
        catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->ad_password_history" + e.getMessage());
            e.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
        return done;


    }

    public java.util.ArrayList getAllPasswordHistory(String userid)
    {
    	java.util.ArrayList _list = new java.util.ArrayList();
        String query;
        Connection c;
        ResultSet rs;
        PreparedStatement s;
        PasswordHistory pass = null;
        query = "select * from ad_password_history where userid = ?";
        c = null;
        rs = null;
        s = null;
        try {
        c = getConnection();
        s = c.prepareStatement(query);
        s.setString(1, userid);
        rs = s.executeQuery();
        while (rs.next()) {
            String userId = rs.getString("USERID");
            int counter = rs.getInt("COUNTER");
            String password1 = rs.getString("PASSWORD");
            pass = new PasswordHistory();
            pass.setCounter(counter);
            pass.setPassword1(password1);
            pass.setUserId(userId);
            _list.add(pass);
        }
//        closeConnection(c, s, rs);
    }
    catch (Exception e) {
        e.printStackTrace();
    } finally {
        closeConnection(c, s, rs);
    }
    return _list;

}

    public boolean changePassword3(String userid, String password, String expiry, int limitCheck) {
        boolean result = false;
        boolean result2 = false;
        //fetch record from  ad password history based on userid
        java.util.ArrayList list = getAllPasswordHistory(userid);

        if ((list.size() != 0)) {
            for (int x = 0; x < list.size(); x++) {
                PasswordHistory pass = (PasswordHistory) list.get(x);
                if (password.equals(pass.getPassword1())) {
                    result = false;
                    break;

                } else {
                    result = true;
                }
            }
        } else {
            boolean a = createPasswordHistory(userid, password, list.size() + 1);
            changePassword(userid, password, expiry);
            result2 = true;
        }

        //check against last five password
        if (result) {
            //change password
            if (changePassword(userid, password, expiry)) {
                if (list.size() >= limitCheck) {
                    //update record from  ad password history based on userid
                    if (updatePasswordHistory2(list.size(), password, userid)) {
                        result2 = true;
                    }

                } else {
                    createPasswordHistory(userid, password, list.size() + 1);
                    result2 = true;
                }
            }
        }
        return result2;
    }

    public boolean createPasswordHistory(String userid, String Password, int counter)
    {
        Connection con;
        PreparedStatement ps;
        boolean done;
        String query;
        con = null;
        ps = null;
        done = false;
        query = "INSERT INTO ad_password_history(USERID,COUNTER,PASSWORD)    VALUES (?,?,?)";
        try {
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, userid);
        ps.setInt(2, counter);
        ps.setString(3, Password);
        done = ps.executeUpdate() != -1;
//        closeConnection(con, ps);
    }
    catch (Exception e) {
        System.out.println(this.getClass().getName()
                + " ERROR:Error inserting ad_password_history  ->");
        e.printStackTrace();
    } finally {
        closeConnection(con, ps);
    }
    return done;

}

    public PasswordHistory getPasswordHistory2(String userid)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        String query;
        PasswordHistory ph;
        con = null;
        ps = null;
        rs = null;
        query = "select USERID,max(COUNTER) COUNTER,PASSWORD,CHANGEDATE,LIMIT from ad_password_history where userid = ?";
        ph = null;
        try {
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, userid);
        rs = ps.executeQuery();
        for(rs = ps.executeQuery(); rs.next();)
        {
            String userId = rs.getString("USERID");
            int counter = rs.getInt("COUNTER");
            String password1 = rs.getString("PASSWORD");
            int limit = rs.getInt("LIMIT");
            ph = new PasswordHistory(userId, counter, password1, limit);
        }
 //       closeConnection(con, ps, rs);
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(con, ps, rs);
        }


        return ph;
    }

    public int getPasswordHistory3(String userid)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        int limit;
        String query;
        con = null;
        ps = null;
        rs = null;
        limit = 0;
        query = "select  max(counter) LIMIT from ad_password_history where userid = ?";

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, userid);
            rs = ps.executeQuery();
        while (rs.next()) {
            limit = rs.getInt("LIMIT");
        }
 //       closeConnection(con, ps, rs);
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(con, ps, rs);
        }
        return limit;
    }

    public int getFreq(String userid)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        int frequency;
        String query;
        con = null;
        ps = null;
        rs = null;
        frequency = 0;
        query = "select  freq from ad_password_history where userid = ?";
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, userid);
            rs = ps.executeQuery();
        while (rs.next()) {
            frequency = rs.getInt("freq");
        }
//        closeConnection(con, ps, rs);
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(con, ps, rs);
        }
        return frequency;
    }

    public Timestamp getDateTime(Date inputDate)
    {
        String strDate = null;
        try
        {
            if(inputDate == null)
            {
                strDate = sdf.format(new Date());
            } else
            {
                strDate = sdf.format(inputDate);
            }
        }
        catch(Exception er)
        {
            System.out.println((new StringBuilder()).append("WARN : Error getting datetime ->").append(er).toString());
        }
        return getDateTime(strDate);
    }

    public Timestamp getDateTime(String inputDate)
    {
        Timestamp inputTime = null;
        try
        {
            if(inputDate == null)
            {
                inputDate = sdf.format(new Date());
            }
            inputDate = inputDate.replaceAll("/", "-");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss.SSS");
            String strDate = dateFormat.format(new Date());
            String transInputDate = (new StringBuilder()).append(inputDate).append(strDate.substring(10, strDate.length())).toString();
            inputTime = new Timestamp(dateFormat.parse(transInputDate).getTime());
        }
        catch(Exception er)
        {
            System.out.println((new StringBuilder()).append("WARN : Error getting datetime ->").append(er).toString());
        }
        return inputTime;
    }

    public String[] PasswordChange(String userName, String pagePassword)
    {
    	int dbPasswordValue = Integer.valueOf( records.getCodeName("select minimum_password from am_gb_company"));
    	//System.out.println("<<<<<<<<<<< dbPasswordValue: " + dbPasswordValue);
    	String autoPass = String.valueOf(generateRandomPassword(dbPasswordValue));
    	//System.out.println("<<<<<<<<<<< autoPass: " + autoPass);
    	sdf = new SimpleDateFormat("dd-MM-yyyy");
        GregorianCalendar date = new GregorianCalendar();
        int sec = date.get(14);
        String output = pagePassword.trim();
        String prefix = userName.trim().substring(0, 2);
       // System.out.println("<<<<<<<<<<< prefix: " + prefix);
        int length = userName.trim().length();
       // System.out.println("<<<<<<<<<<< length: " + length);
        int length2 = length - 2; 
      //  System.out.println("<<<<<<<<<<< length2: " + length2);
        String surrfix = userName.trim().substring(length2, length);
      //  System.out.println("<<<<<<<<<<< surrfix: " + surrfix);
        String Password = (new StringBuilder()).append(surrfix).append(prefix).append(autoPass).append(surrfix).append(prefix).toString();
      //  System.out.println("<<<<<<<<<<< Password: " + Password);
       // String PasswordDisplay = (new StringBuilder()).append(prefix).append(sec).append(surrfix).toString();
       // System.out.println("<<<<<<<<<<< PasswordDisplay: " + PasswordDisplay);
        String passwordDisplay = (new StringBuilder()).append(prefix).append(autoPass).append(surrfix).toString();
      //  System.out.println("<<<<<<<<<<< password: " + passwordDisplay);
//        String display[] = {
//            Password, PasswordDisplay
//        };
        String display[] = {
        		Password, passwordDisplay
            };
        return display;
    }
    
    static char[] generateRandomPassword(int len)
    {
        // Using numeric values
        String numbers = "0123456789";
  
        // Using random method
        Random rndm_method = new Random();
  
        char[] password = new char[len];
  
        for (int i = 0; i < len; i++)
        {
            password[i] =
             numbers.charAt(rndm_method.nextInt(numbers.length()));
        }
        return password;
    }

    public boolean createManageUser2(User user, String limit,String  supervisorId) throws IOException
    {
        Connection con;
        PreparedStatement ps;
        boolean done;
        con = null;
        ps = null;
        done = false;
        String query = "";
        Properties prop = new Properties();
		File file = new File("C:\\Property\\LegendPlus.properties");
		FileInputStream input = new FileInputStream(file);
		prop.load(input);

		String bank = prop.getProperty("bank");
		System.out.println("bank: " + bank);
        try {
        String userId = (new ApplicationHelper()).getGeneratedId("AM_GB_USER");
        if(user.getExpiryDate() == "" || user.getExpiryDate().equalsIgnoreCase("null") || user.getExpiryDate() == null || user.getExpiryDate().equalsIgnoreCase(""))
        {
             query = "INSERT INTO AM_GB_USER( User_Name, Full_Name, Legacy_Sys_Id, Class, Branch, Pass" +
"word, Phone_No, Is_Supervisor, Must_Change_Pwd, Login_Status, User_Status, UserI" +
"d, Create_Date, Password_Expiry,Fleet_Admin, Email,User_id,branch_restriction, E" +
"xpiry_Days,Approval_Limit,dept_code,token_required,region_code,zone_code," +
"region_restriction,zone_restriction,Facility_Admin,Store_Admin,Supervisor_Id)VALUES (?,?,?,?,?,?,?,?,?,?,?" +
",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
;
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getUserFullName());
            ps.setString(3, user.getLegacySystemId());
            ps.setString(4, user.getUserClass());
            ps.setString(5, user.getBranch());
            ps.setString(6, user.getPassword());
            ps.setString(7, user.getPhoneNo());
            ps.setString(8, user.getIsSupervisor());
            ps.setString(9, user.getMustChangePwd());
            ps.setString(10, user.getLoginStatus());
            ps.setString(11, user.getUserStatus());
            ps.setString(12, user.getCreatedBy());
            ps.setDate(13, df.dateConvert(new Date()));
            if ("ZENITH".equals(bank)) {
                ps.setNull(14, java.sql.Types.DATE);
            } else {
                java.sql.Date expiryDate = df.dateConvert(
                    df.addDayToDate(new Date(), Integer.parseInt(user.getPwdExpiry()))
                );
                ps.setDate(14, expiryDate);
            }
            ps.setString(15, user.getFleetAdmin());
            ps.setString(16, user.getEmail());
            ps.setString(17, userId);
            System.out.println((new StringBuilder()).append("createManageUser2 tokenRequired: ").append(user.isTokenRequired()).toString());
            ps.setString(18, user.getBranchRestrict());
            ps.setInt(19, user.getExpiryDays());
            ps.setString(20, limit);
            ps.setString(21, user.getDeptCode());
            ps.setString(22, user.getTokenRequire());
            ps.setString(23, user.getRegionCode());
            ps.setString(24, user.getZoneCode());
            ps.setString(25, user.getRegionRestrict());
            ps.setString(26, user.getZoneRestrict());
            ps.setString(27, user.getIsFacilityAdministrator());
            ps.setString(28, user.getIsStoreAdministrator());
            ps.setString(29, supervisorId);
            done = ps.executeUpdate() != -1;
        } else
        {
             query = "INSERT INTO AM_GB_USER( User_Name, Full_Name, Legacy_Sys_Id, Class, Branch, Pass" +
"word, Phone_No, Is_Supervisor, Must_Change_Pwd, Login_Status, User_Status, UserI" +
"d, Create_Date, Password_Expiry,Fleet_Admin, Email,User_id,branch_restriction, E" +
"xpiry_Days,Expiry_Date,Approval_Limit,region_code,zone_code," +
"region_restriction,zone_restriction,Facility_Admin,Store_Admin,Supervisor_Id)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
"?,?,?,?,?,?,?,?,?,?,?)"
;
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getUserFullName());
            ps.setString(3, user.getLegacySystemId());
            ps.setString(4, user.getUserClass());
            ps.setString(5, user.getBranch());
            ps.setString(6, user.getPassword());
            ps.setString(7, user.getPhoneNo());
            ps.setString(8, user.getIsSupervisor());
            ps.setString(9, user.getMustChangePwd());
            ps.setString(10, user.getLoginStatus());
            ps.setString(11, user.getUserStatus());
            ps.setString(12, user.getCreatedBy());
            ps.setDate(13, df.dateConvert(new Date()));
            if ("ZENITH".equals(bank)) {
                ps.setNull(14, java.sql.Types.DATE);
            } else {
                java.sql.Date expiryDate = df.dateConvert(
                    df.addDayToDate(new Date(), Integer.parseInt(user.getPwdExpiry()))
                );
                ps.setDate(14, expiryDate);
            }
            ps.setString(15, user.getFleetAdmin());
            ps.setString(16, user.getEmail());
            ps.setString(17, userId);
            ps.setString(18, user.getBranchRestrict());
            ps.setInt(19, user.getExpiryDays());
            String temDate = user.getExpiryDate();
            ps.setDate(20, df.dateConvert(temDate));
            ps.setString(21, limit);
            ps.setString(22, user.getRegionCode());
            ps.setString(23, user.getZoneCode());
            ps.setString(24, user.getRegionRestrict());
            ps.setString(25, user.getZoneRestrict());
            ps.setString(26, user.getIsFacilityAdministrator());
            ps.setString(27, user.getIsStoreAdministrator());
            ps.setString(28, supervisorId);
            done = ps.executeUpdate() != -1;
        }

        createPasswordHistory(userId, user.getPassword());
 //       closeConnection(con, ps);
        }
        catch (Exception e) {
            System.out.println("WARNING:Error executing Query in createManageUser2 ->"
                    + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;


    }

    public boolean updateManageUser2(User user, String limit)
    {
        Connection con;
        PreparedStatement ps;
        boolean done;
        con = null;
        ps = null;
        done = false;
        String query = "";
        try {
        	System.out.println("=====>user.getExpiryDate(): "+user.getExpiryDate());
        if(user.getExpiryDate() == "" || user.getExpiryDate().equalsIgnoreCase("null") || user.getExpiryDate().equalsIgnoreCase(""))
        {
/*             query = "UPDATE AM_GB_USER  SET  User_Name = ?, Full_Name = ? ,Legacy_Sys_Id = ?, Class =" +
" ?, Branch = ?, Password = ?, Phone_No = ?,Is_Supervisor = ?,Must_Change_Pwd = ?" +
", Login_Status = ?, User_Status = ?, Fleet_Admin = ?, Email = ?,branch_restricti" +
"on =?, Expiry_Days =?,Approval_Limit = ? ,dept_code=?,token_required=?,region_code=?,zone_code=?,region_restriction=?,zone_restriction=?" +
",Facility_Admin = ?,Store_Admin = ? WHERE User_Id = ?";
*/             
            query = "UPDATE AM_GB_USER  SET  User_Name = ?, Full_Name = ? ,Legacy_Sys_Id = ?, Class =" +
" ?, Branch = ?, Phone_No = ?,Is_Supervisor = ?,Must_Change_Pwd = ?" +
", Login_Status = ?, User_Status = ?, Fleet_Admin = ?, Email = ?,branch_restricti" +
"on =?, Expiry_Days =?,Approval_Limit = ? ,dept_code=?,token_required=?,region_code=?,zone_code=?,region_restriction=?,zone_restriction=?" +
",Facility_Admin = ?,Store_Admin = ?, Expiry_Date=? WHERE User_Id = ?";
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getUserFullName());
            ps.setString(3, user.getLegacySystemId());
            ps.setString(4, user.getUserClass());
            ps.setString(5, user.getBranch());
//            ps.setString(6, user.getPassword());
            ps.setString(6, user.getPhoneNo());
            ps.setString(7, user.getIsSupervisor());
            ps.setString(8, user.getMustChangePwd());
            ps.setString(9, user.getLoginStatus());
            ps.setString(10, user.getUserStatus());
            ps.setString(11, user.getFleetAdmin());
            ps.setString(12, user.getEmail());
            ps.setString(13, user.getBranchRestrict());
            ps.setInt(14, user.getExpiryDays());
            ps.setString(15, limit);
            ps.setString(16, user.getDeptCode());
            ps.setString(17, user.getTokenRequire());
            ps.setString(18, user.getRegionCode());
            ps.setString(19, user.getZoneCode());
            ps.setString(20, user.getRegionRestrict());
            ps.setString(21, user.getZoneRestrict());
            ps.setString(22, user.getIsFacilityAdministrator());
            ps.setString(23, user.getIsStoreAdministrator());
            ps.setDate(24, df.dateConvert(user.getExpiryDate().equals("") ? null :  user.getExpiryDate()));
            ps.setString(25, user.getUserId());

            System.out.println((new StringBuilder()).append("updateManageUser2 tokenRequired: ").append(user.isTokenRequired()).toString());
            done = ps.executeUpdate() != -1;
        } else
        {
             query = "UPDATE AM_GB_USER  SET  User_Name = ?, Full_Name = ? ,Legacy_Sys_Id = ?, Class =" +
" ?, Branch = ?, Phone_No = ?,Is_Supervisor = ?,Must_Change_Pwd = ?" +
", Login_Status = ?, User_Status = ?, Fleet_Admin = ?, Email = ?,branch_restricti" +
"on =?, Expiry_Days =?, Approval_Limit = ? ,token_required=?,region_code=?,zone_code=?,region_restriction=?,zone_restriction=?" +
",Facility_Admin = ?,Store_Admin = ? ,dept_code=?, Expiry_Date=?  WHERE User_Id = ?";
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getUserFullName());
            ps.setString(3, user.getLegacySystemId());
            ps.setString(4, user.getUserClass());
            ps.setString(5, user.getBranch());
//            ps.setString(6, user.getPassword());
            ps.setString(6, user.getPhoneNo());
            ps.setString(7, user.getIsSupervisor());
            ps.setString(8, user.getMustChangePwd());
            ps.setString(9, user.getLoginStatus());
            ps.setString(10, user.getUserStatus());
            ps.setString(11, user.getFleetAdmin());
            ps.setString(12, user.getEmail());
            ps.setString(13, user.getBranchRestrict());
            ps.setInt(14, user.getExpiryDays());
            ps.setString(15, limit);
            ps.setString(16, user.getTokenRequire());
            ps.setString(17, user.getRegionCode());
            ps.setString(18, user.getZoneCode());
            ps.setString(19, user.getRegionRestrict());
            ps.setString(20, user.getZoneRestrict());
            ps.setString(21, user.getIsFacilityAdministrator());
            ps.setString(22, user.getIsStoreAdministrator());
//            System.out.println("=====>user.getDeptCode(): "+user.getDeptCode());
            ps.setString(23, user.getDeptCode());
            ps.setDate(24, df.dateConvert(user.getExpiryDate().equals("") ? null :  user.getExpiryDate()));
            ps.setString(25, user.getUserId());

            done = ps.executeUpdate() != -1;
        }
        if(user.getExpiryDays() == 0)
        {
            updateExpiryDate(Integer.parseInt(user.getUserId()));
        }
 //       closeConnection(con, ps);
        }
        catch (Exception e) {
            System.out.println("WARNING:Error executing Query in updateManageUser2 ->"
                    + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;
    }
    
    public java.util.ArrayList getAllUserByQuery()
    {
        ArrayList _list;
        String query;
        Connection c;
        ResultSet rs;
        Statement s;
        _list = new ArrayList();
        User user = null;
        query = "SELECT User_Id, User_Name, Full_Name, Legacy_Sys_Id, Class, Branch, Password, Ph" +
"one_No, is_Supervisor, Must_Change_Pwd, Login_Status, User_Status, UserId, Creat" +
"e_Date, Password_Expiry, Login_Date, Login_System, Fleet_Admin, email, Branch, p" +
"assword_changed,Expiry_Date,branch_restriction, approval_limit,approval_level,to" +
"ken_required,dept_restriction,UnderTaker,region_code,zone_code,region_restriction,zone_restriction,Facility_Admin,Store_Admin FROM AM_GB_USER WHERE USER_STATUS = 'ACTIVE' "
;
//        System.out.println("<<<<<<getAllUserByQuery query: "+query);
        c = null;
        rs = null;
        s = null;
        try {
        c = getConnection();
        s = c.createStatement();
        rs = s.executeQuery(query);
        while (rs.next()) {
            String userId = rs.getString("User_Id");
            String userName = rs.getString("User_Name");
            String fullName = rs.getString("Full_Name");
            String legacySysId = rs.getString("Legacy_Sys_Id");
            String Class = rs.getString("Class");
            String branch = rs.getString("Branch");
            String password = rs.getString("Password");
            String phoneNo = rs.getString("Phone_No");
            String isSupervisor = rs.getString("Is_Supervisor");
            String mustChangePwd = rs.getString("Must_Change_Pwd");
            String loginStatus = rs.getString("Login_Status");
            String userStatus = rs.getString("User_Status");
            String user_Id = rs.getString("UserId");
            String createDate = rs.getString("Create_Date");
            String passwordExpiry = rs.getString("Password_Expiry");
            String loginDate = rs.getString("Login_Date");
            String loginSystem = rs.getString("Login_System");
            String fleetAdmin = rs.getString("Fleet_Admin");
            String email = rs.getString("email");
            String branchCode = rs.getString("Branch");
            String pwdChanged = rs.getString("password_changed");
            String tokenRequired = rs.getString("token_required");
//            System.out.println((new StringBuilder()).append("####################################### Token Read from the DB here ====").append(tokenRequired).toString());
//            System.out.println((new StringBuilder()).append(" The sent tokenRequired ==== ").append(tokenRequired).toString());
            Date expiry_date = rs.getDate("Expiry_Date");
            String branch_restriction = rs.getString("branch_restriction");
            String apprvLimit = rs.getString("approval_limit");
            String apprvLevel = rs.getString("approval_level");
            String buRestrict = rs.getString("dept_restriction");
            String underTaker = rs.getString("UnderTaker");
            String regionCode = rs.getString("region_code");
            String zoneCode = rs.getString("zone_code");
            String regionRestrict = rs.getString("region_restriction");
            String zoneRestrict = rs.getString("zone_restriction");
            String facilityAdmin = rs.getString("Facility_Admin");
            String storeAdmin = rs.getString("Store_Admin");
            user = new User();
            user.setUserId(userId);
            user.setUserName(userName);
            user.setUserFullName(fullName);
            user.setLegacySystemId(legacySysId);
            user.setUserClass(Class);
            user.setBranch(branch);
            user.setPassword(password);
            user.setPhoneNo(phoneNo);
            user.setIsSupervisor(isSupervisor);
            user.setMustChangePwd(mustChangePwd);
            user.setLoginStatus(loginStatus);
            user.setUserStatus(userStatus);
            user.setCreatedBy(user_Id);
            user.setCreateDate(createDate);
            user.setPwdExpiry(passwordExpiry);
            user.setLastLogindate(loginDate);
            user.setLoginSystem(loginSystem);
            user.setFleetAdmin(fleetAdmin);
            user.setEmail(email);
            user.setBranch(branchCode);
            user.setPwdChanged(pwdChanged);
            user.setUnderTaker(underTaker);
            user.setRegionCode(regionCode);
            user.setZoneCode(zoneCode);
            user.setRegionRestrict(regionRestrict);
            user.setZoneRestrict(zoneRestrict);
            user.setIsFacilityAdministrator(facilityAdmin);
            user.setIsStoreAdministrator(storeAdmin);
            if(tokenRequired != null && tokenRequired.trim().equalsIgnoreCase("Y"))
            {
                user.setTokenRequired(true);
            } else
            {
                user.setTokenRequired(false);
            }
 //          System.out.println((new StringBuilder()).append("##################################### After setting the user the token of the us" +
//"er =="
//).append(user.isTokenRequired()).toString());
            user.setApprvLimit(apprvLimit);
            user.setBranchRestrict(branch_restriction);
            user.setApprvLevel(apprvLevel);
            user.setExpDate(expiry_date);
            user.setDeptRestrict(buRestrict);
            _list.add(user);
        }
 //       closeConnection(c, s, rs);
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }

    public java.util.ArrayList getAllUserByQuery(String loginUserId)
    {
        ArrayList _list;
        String query;
        Connection c;
        ResultSet rs;
        PreparedStatement s;
        _list = new ArrayList();
        User user = null;
        query = "SELECT User_Id, User_Name, Full_Name, Legacy_Sys_Id, Class, Branch, Password, Ph" +
"one_No, is_Supervisor, Must_Change_Pwd, Login_Status, User_Status, UserId, Creat" +
"e_Date, Password_Expiry, Login_Date, Login_System, Fleet_Admin, email, Branch, p" +
"assword_changed,Expiry_Date,branch_restriction, approval_limit,approval_level,to" +
"ken_required,dept_restriction,UnderTaker,region_code,zone_code,region_restriction,"+
"zone_restriction,Facility_Admin,Store_Admin FROM AM_GB_USER WHERE USER_STATUS = 'ACTIVE' AND User_Id!= ? ";
        System.out.println("<<<<<<getAllUserByQuery query: "+query);
        c = null;
        rs = null;
        s = null;
        try {
        c = getConnection();
        s = c.prepareStatement(query);
        s.setString(1, loginUserId);
        rs = s.executeQuery();
        while (rs.next()) {
            String userId = rs.getString("User_Id");
            String userName = rs.getString("User_Name");
            String fullName = rs.getString("Full_Name");
            String legacySysId = rs.getString("Legacy_Sys_Id");
            String Class = rs.getString("Class");
            String branch = rs.getString("Branch");
            String password = rs.getString("Password");
            String phoneNo = rs.getString("Phone_No");
            String isSupervisor = rs.getString("Is_Supervisor");
            String mustChangePwd = rs.getString("Must_Change_Pwd");
            String loginStatus = rs.getString("Login_Status");
            String userStatus = rs.getString("User_Status");
            String user_Id = rs.getString("UserId");
            String createDate = rs.getString("Create_Date");
            String passwordExpiry = rs.getString("Password_Expiry");
            String loginDate = rs.getString("Login_Date");
            String loginSystem = rs.getString("Login_System");
            String fleetAdmin = rs.getString("Fleet_Admin");
            String email = rs.getString("email");
            String branchCode = rs.getString("Branch");
            String pwdChanged = rs.getString("password_changed");
            String tokenRequired = rs.getString("token_required");
//            System.out.println((new StringBuilder()).append("####################################### Token Read from the DB here ====").append(tokenRequired).toString());
//            System.out.println((new StringBuilder()).append(" The sent tokenRequired ==== ").append(tokenRequired).toString());
            Date expiry_date = rs.getDate("Expiry_Date");
            String branch_restriction = rs.getString("branch_restriction");
            String apprvLimit = rs.getString("approval_limit");
            String apprvLevel = rs.getString("approval_level");
            String buRestrict = rs.getString("dept_restriction");
            String underTaker = rs.getString("UnderTaker");
            String regionCode = rs.getString("region_code");
            String zoneCode = rs.getString("zone_code");
            String regionRestrict = rs.getString("region_restriction");
            String zoneRestrict = rs.getString("zone_restriction");
            String facilityAdmin = rs.getString("Facility_Admin");
            String storeAdmin = rs.getString("Store_Admin");
            user = new User();
            user.setUserId(userId);
            user.setUserName(userName);
            user.setUserFullName(fullName);
            user.setLegacySystemId(legacySysId);
            user.setUserClass(Class);
            user.setBranch(branch);
            user.setPassword(password);
            user.setPhoneNo(phoneNo);
            user.setIsSupervisor(isSupervisor);
            user.setMustChangePwd(mustChangePwd);
            user.setLoginStatus(loginStatus);
            user.setUserStatus(userStatus);
            user.setCreatedBy(user_Id);
            user.setCreateDate(createDate);
            user.setPwdExpiry(passwordExpiry);
            user.setLastLogindate(loginDate);
            user.setLoginSystem(loginSystem);
            user.setFleetAdmin(fleetAdmin);
            user.setEmail(email);
            user.setBranch(branchCode);
            user.setPwdChanged(pwdChanged);
            user.setUnderTaker(underTaker);
            user.setRegionCode(regionCode);
            user.setZoneCode(zoneCode);
            user.setRegionRestrict(regionRestrict);
            user.setZoneRestrict(zoneRestrict);
            user.setIsFacilityAdministrator(facilityAdmin);
            user.setIsStoreAdministrator(storeAdmin);
            if(tokenRequired != null && tokenRequired.trim().equalsIgnoreCase("Y"))
            {
                user.setTokenRequired(true);
            } else
            {
                user.setTokenRequired(false);
            }
 //          System.out.println((new StringBuilder()).append("##################################### After setting the user the token of the us" +
//"er =="
//).append(user.isTokenRequired()).toString());
            user.setApprvLimit(apprvLimit);
            user.setBranchRestrict(branch_restriction);
            user.setApprvLevel(apprvLevel);
            user.setExpDate(expiry_date);
            user.setDeptRestrict(buRestrict);
            _list.add(user);
        }
//        closeConnection(c, s, rs);
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }

    public boolean createManageUserfromUpload(String userName,String fullname,String legacyid,String userclass,String enterpasswords,String passwords,String email,
			String userBranch,String phoneNo,String isSupervisor,String fleetAdmin,String passwordMust,String expiry,String loginStatus,String userStatus,String userid,String branchRestrict,String deptCode,String regionCode,String zoneCode,String limit)
    {
        Connection con;
        PreparedStatement ps;
        PreparedStatement ps1;
        boolean done;
        con = null;
        ps = null;
        done = false;
        String query = "";
        try {

        String userId = (new ApplicationHelper()).getGeneratedId("AM_GB_USER");

             query = "INSERT INTO AM_GB_USER( User_Name, Full_Name, Legacy_Sys_Id, Class, Branch, Pass" +
					"word, Phone_No, Is_Supervisor, Must_Change_Pwd, Login_Status, User_Status, UserI" +
					"d, Create_Date, Fleet_Admin, Email,User_id,branch_restriction, " +
					"Approval_Limit,dept_code,region_code,zone_code)VALUES (?,?,?,?,?,?,?,?,?,?,?" +
					",?,?,?,?,?,?,?,?,?,?)";
             
             String query2 = "delete from am_gb_User_Upload  where User_Name = '"+userName+"'";

            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, userName);
//            System.out.println("userName: "+userName);
            ps.setString(2, fullname);
            ps.setString(3, legacyid);
            ps.setString(4, userclass);
            ps.setString(5, userBranch);
            ps.setString(6, passwords);
            ps.setString(7, phoneNo);
            ps.setString(8, isSupervisor);
            ps.setString(9, passwordMust);
            ps.setString(10, loginStatus);
            ps.setString(11, userStatus);
            ps.setString(12, userid);
            ps.setDate(13, df.dateConvert(new Date()));
//            ps.setDate(14, df.dateConvert(df.addDayToDate(new Date(), Integer.parseInt(user.getPwdExpiry()))));
            ps.setString(14, fleetAdmin);
            ps.setString(15, email);
            ps.setString(16, userId);
//            System.out.println((new StringBuilder()).append("createManageUser2 tokenRequired: ").append(user.isTokenRequired()).toString());
            ps.setString(17, branchRestrict);
//            ps.setInt(19, user.getExpiryDays());
            ps.setString(18, limit);
            ps.setString(19, deptCode);
     //       ps.setString(22, user.getTokenRequire());
            ps.setString(20, regionCode);
            ps.setString(21, zoneCode);
 //           ps.setString(25, user.getRegionRestrict());
 //           ps.setString(26, user.getZoneRestrict());
//            ps.setString(27, user.getIsFacilityAdministrator());
//            ps.setString(28, user.getIsStoreAdministrator());
            done = ps.executeUpdate() != -1;

        createPasswordHistory(userId, passwords);

    	ps1 = con.prepareStatement(query2);
    	done = (ps1.executeUpdate() != -1);
  //  	  closeConnection(con, ps);
 //   	closeConnection(con, ps);
        }
        catch (Exception e) {
            System.out.println("WARNING:Error executing Query in createManageUser2 ->"
                    + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;


    }
    
    
    public boolean createManageUserfromUpload(String userName,String fullname,String legacyid,String userclass,String enterpasswords,String passwords,String email,
			String userBranch,String phoneNo,String isSupervisor,String fleetAdmin,String passwordMust,String expiry,String loginStatus,String userStatus,
			String userid,String branchRestrict,String deptCode,String regionCode,String zoneCode,String limit, String tokenRequired, String regionRestrict,
			String zoneRestrict)
    {
        Connection con;
        PreparedStatement ps;
        PreparedStatement ps1;
        boolean done;
        con = null;
        ps = null;
        done = false;
        String query = "";
        try {

        String userId = (new ApplicationHelper()).getGeneratedId("AM_GB_USER");

             query = "INSERT INTO AM_GB_USER( User_Name, Full_Name, Legacy_Sys_Id, Class, Branch, Pass" +
					"word, Phone_No, Is_Supervisor, Must_Change_Pwd, Login_Status, User_Status, UserI" +
					"d, Create_Date, Fleet_Admin, Email,User_id,branch_restriction, " +
					"Approval_Limit,dept_code,region_code,zone_code,"
					+ "token_required,REGION_RESTRICTION,ZONE_RESTRICTION)VALUES (?,?,?,?,?,?,?,?,?,?,?" +
					",?,?,?,?,?,?,?,?,?,?,?,?,?)";
             
             String query2 = "delete from am_gb_User_Upload  where User_Name = '"+userName+"'";

            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, userName);
//            System.out.println("userName: "+userName);
            ps.setString(2, fullname);
            ps.setString(3, legacyid);
            ps.setString(4, userclass);
            ps.setString(5, userBranch);
            ps.setString(6, passwords);
            ps.setString(7, phoneNo);
            ps.setString(8, isSupervisor);
            ps.setString(9, passwordMust);
            ps.setString(10, loginStatus);
            ps.setString(11, userStatus);
            ps.setString(12, userid);
            ps.setDate(13, df.dateConvert(new Date()));
//            ps.setDate(14, df.dateConvert(df.addDayToDate(new Date(), Integer.parseInt(user.getPwdExpiry()))));
            ps.setString(14, fleetAdmin);
            ps.setString(15, email);
            ps.setString(16, userId);
//            System.out.println((new StringBuilder()).append("createManageUser2 tokenRequired: ").append(user.isTokenRequired()).toString());
            ps.setString(17, branchRestrict);
//            ps.setInt(19, user.getExpiryDays());
            ps.setString(18, limit);
            ps.setString(19, deptCode);
     //       ps.setString(22, user.getTokenRequire());
            ps.setString(20, regionCode);
            ps.setString(21, zoneCode);
            ps.setString(22, tokenRequired);
            ps.setString(23, regionRestrict);
            ps.setString(24, zoneRestrict);
 //           ps.setString(25, user.getRegionRestrict());
 //           ps.setString(26, user.getZoneRestrict());
//            ps.setString(27, user.getIsFacilityAdministrator());
//            ps.setString(28, user.getIsStoreAdministrator());
            done = ps.executeUpdate() != -1;

        createPasswordHistory(userId, passwords);

    	ps1 = con.prepareStatement(query2);
    	done = (ps1.executeUpdate() != -1);
  //  	  closeConnection(con, ps);
 //   	closeConnection(con, ps);
        }
        catch (Exception e) {
            System.out.println("WARNING:Error executing Query in createManageUser2 ->"
                    + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;


    }
 

    public String getCompanyInfoOld(String compCode) throws SQLException {
        Connection con = null;
        //PreparedStatement ps = null;
         Statement stmt = null;
        ResultSet rs = null;

        String compName = "";
        CallableStatement cstmt = null;
//        String sql = "EXEC getCompanyInfo'" + compCode + "'";
//        System.out.println("\n\n calling getCompanyInfo() with this stored procedure " +sql);
        try {
        	 con = getConnection();
             stmt = con.createStatement();
//         cstmt = con.prepareCall(
//                 "{call getCompanyInfo('" + compCode + "')}");
         cstmt = con.prepareCall("{CALL getCompanyInfo(?)}");
         cstmt.setString(1, compCode);
         cstmt.execute();
         rs = cstmt.getResultSet();
         if (rs.next()) {
             compName = rs.getString(1);
 //            System.out.println("======Company Name:  "+compName);
         }      

        } catch (Exception ex) {

            System.out.println("Error occurred in getCompanyInfo() of SecurityHandler  >>" +ex);
        } finally {
        //closeConnection(con,stmt,rs);
        	rs.close();
        	cstmt.close();
        	con.close();
        }

        return compName;
    }
    
    public String getCompanyInfo(String compCode) throws SQLException {
        String compName = "";

 
        try (Connection con = getConnection();
             CallableStatement cstmt = con.prepareCall("{CALL getCompanyInfo(?)}")) {

            cstmt.setString(1, compCode);

            boolean hasResultSet = cstmt.execute();

            if (hasResultSet) {
                try (ResultSet rs = cstmt.getResultSet()) {
                    if (rs.next()) {
                        compName = rs.getString(1);
                    }
                }
            }

        } catch (SQLException ex) {
            System.err.println("Error in getCompanyInfo() >> " + ex.getMessage());
            throw ex; // propagate exception to caller
        }

        return compName;
    }


//    public String getCurrentDate_Month() throws SQLException {
//        Connection con = null;
//        //PreparedStatement ps = null;
//         Statement stmt = null;
//        ResultSet rs = null;
//        String dateMonth = "";
//        String logindate = "";
//        String month = "";
//        CallableStatement cstmt = null;
//        try {
//        	 con = getConnection();
//             stmt = con.createStatement();
//         cstmt = con.prepareCall(
//                 "{call getCurrentDate_Month()}");
//         cstmt.execute();
//         rs = cstmt.getResultSet();
//         if (rs.next()) {
//             logindate = rs.getString(1);
//             month = rs.getString(2);
//             dateMonth = logindate+"#"+month;
//          //   System.out.println("======logindate:  "+logindate+"   month: "+month+"   dateMonth: "+dateMonth);
//         }      
//
//        } catch (Exception ex) {
//
//            System.out.println("Error occurred in getCurrentDate_Month() of SecurityHandler  >>" +ex);
//        } finally {
//        //closeConnection(con,stmt,rs);
//        	rs.close();
//        	cstmt.close();
//        	con.close();
//        	
//        }
//
//        return dateMonth;
//    }
    
    
    public String getCurrentDate_Month() throws SQLException {

        String dateMonth = "";

        try (
            Connection con = getConnection();
            CallableStatement cstmt = con.prepareCall("{call getCurrentDate_Month()}");
            ResultSet rs = executeAndGetResultSet(cstmt)
        ) {

            if (rs.next()) {
                dateMonth = rs.getString(1) + "#" + rs.getString(2);
            }

        } catch (Exception ex) {
            throw new SQLException("Error in getCurrentDate_Month()", ex);
        }

        return dateMonth;
    }

    private ResultSet executeAndGetResultSet(CallableStatement cstmt) throws SQLException {
        cstmt.execute();
        return cstmt.getResultSet();
    }


//    public java.util.ArrayList getUserByQueryProc(String userNameFilter, String passwordfilter, String Name,String tokenfilter, String pass) throws SQLException
//    {
//        java.util.ArrayList _list = new java.util.ArrayList();
//        legend.admin.objects.User user = null;
//        String query;
//        Connection c;
//        ResultSet rs;
//        Statement s;
//        CallableStatement cstmt = null;
//        cm = new CryptManager();
//        _list = new ArrayList();
//
//        System.out.println("userNameFilter in getUserByQueryProc: "+userNameFilter+"  passwordfilter: "+passwordfilter+"  Name: "+Name+"  tokenfilter: "+tokenfilter+"  pass: "+pass);
//        c = null;
//        rs = null;
//        s = null;
//        try {
////        	Name = Cryptomanager.decrypt(Name);
////            pass = Cryptomanager.decrypt(pass);
////        	Name = cm.decrypt(Name);
////            pass = cm.decrypt(pass);
//        	 c = getConnection();
//             stmt = c.createStatement();
////             cstmt = c.prepareCall(		     
////		         "{call getUserByQuery(" + userNameFilter + ","+passwordfilter+","+tokenfilter+")}");
//           	 String nameFilter = userNameFilter;
//            	nameFilter = nameFilter.replaceAll("\\s", "");
//            	 String pwdFilter = passwordfilter;
//            	pwdFilter = pwdFilter.replaceAll("\\s", "");
//            	 String tokenFilter = tokenfilter;
//            	tokenFilter = tokenFilter.replaceAll("\\s", "");
//             cstmt = c.prepareCall("{CALL getUserByQuery(?,?,?)}");
//             cstmt.setString(1, nameFilter);
//             cstmt.setString(2, pwdFilter);
//             cstmt.setString(3, tokenFilter);
//		    // cstmt.execute();
//		     
////		     rs = cstmt.getResultSet();
//             System.out.println("userNameFilter<<<<<<" + userNameFilter);
//             System.out.println("passwordfilter<<<<<<" + passwordfilter);
//             System.out.println("tokenfilter<<<<<<" + tokenfilter);
//		     rs = cstmt.executeQuery();
//		     System.out.println("Trying to execute rs....");
//        while (rs.next()) {
//        	System.out.println("Inside rs....");
//            String userId = rs.getString(1);
//            String userName = rs.getString(2);
//            String password = rs.getString(7);
//            if(Name(userName, pass, cm.decrypt(password)))
//            {
//                String fullName = rs.getString(3);
//                String legacySysId = rs.getString(4);
//                String Class = rs.getString(5);
//                String branch = rs.getString(6);
//                String phoneNo = rs.getString(8);
//                String isSupervisor = rs.getString(9);
//                String mustChangePwd = rs.getString(10);
//                String loginStatus = rs.getString(11);
//                String userStatus = rs.getString(12);
//                String user_Id = rs.getString(13);
//                String createDate = rs.getString(14);
//                String passwordExpiry = rs.getString(15);
//                String loginDate = rs.getString(16);
//                String loginSystem = rs.getString(17);
//                String fleetAdmin = rs.getString(18);
//                String email = rs.getString(19);
//                String branchCode = rs.getString(20);
//                String pwdChanged = rs.getString(21);
//                Date expiry_date = rs.getDate(22);
//                String branch_restriction = rs.getString(23);
//                String apprvLimit = rs.getString(24);
//                String apprvLevel = rs.getString(25);
//                String tokenRequired = rs.getString(26);
//                String regionCode = rs.getString(27);
//                String zoneCode = rs.getString(28);
//                String regionRestrict = rs.getString(29);
//                String zoneRestrict = rs.getString(30);
//                String isFacilityAdministrator = rs.getString(31);
//                String isStoreAdministrator = rs.getString(32);
//                String deptCode = rs.getString(33);  
////                System.out.println((new StringBuilder()).append("####################################### Token Read from the DB here ====").append(tokenRequired).toString());
////                System.out.println((new StringBuilder()).append(" The sent tokenRequired ==== ").append(tokenRequired).toString());
//
//                user = new legend.admin.objects.User();
//                user.setUserId(userId);
//                user.setUserName(userName);
//                user.setUserFullName(fullName);
//                user.setLegacySystemId(legacySysId);
//                user.setUserClass(Class);
//                user.setBranch(branch);
//                user.setPassword(password);
//                user.setPhoneNo(phoneNo);
//                user.setIsSupervisor(isSupervisor);
//                user.setMustChangePwd(mustChangePwd);
//                user.setLoginStatus(loginStatus);
//                user.setUserStatus(userStatus);
//                user.setCreatedBy(user_Id);
//                user.setCreateDate(createDate);
//                user.setPwdExpiry(passwordExpiry);
//                user.setLastLogindate(loginDate);
//                user.setLoginSystem(loginSystem);
//                user.setFleetAdmin(fleetAdmin);
//                user.setEmail(email);
//                user.setBranch(branchCode);
//                user.setPwdChanged(pwdChanged);
//                user.setApprvLimit(apprvLimit);
//                user.setBranchRestrict(branch_restriction);
//                user.setApprvLevel(apprvLevel);
//                user.setExpDate(expiry_date);
//                user.setRegionCode(regionCode);
//                user.setRegionRestrict(regionRestrict);
//                user.setZoneCode(zoneCode);
//                user.setZoneRestrict(zoneRestrict);
//                user.setIsFacilityAdministrator(isFacilityAdministrator);
//                user.setIsStoreAdministrator(isStoreAdministrator);
//                user.setDeptCode(deptCode);
//                if(tokenRequired != null && tokenRequired.trim().equalsIgnoreCase("Y"))
//                {
//                    user.setTokenRequired(true);
//                } else
//                {
//                    user.setTokenRequired(false);
//                }
////                System.out.println((new StringBuilder()).append("##################################### After setting the user the token of the us" +
////"er =="
////).append(user.isTokenRequired()).toString());
//                _list.add(user);
//            }
//        }
////        closeConnection(con, stmt, rs);
////        closeConnection(c, s, rs);
//    } catch (Exception e) {
//        e.printStackTrace();
//    } finally {
//        //closeConnection(c, s, rs);
//    	rs.close();
//    	cstmt.close();
//    	c.close();
//    }
//    return _list;
//
//}
    
    public java.util.ArrayList getUserByQueryProcOld(String userNameFilter, String passwordfilter, String Name,String tokenfilter, String pass) throws SQLException
    {
        java.util.ArrayList _list = new java.util.ArrayList();
        legend.admin.objects.User user = null;
        String query;
        Connection c;
        ResultSet rs;
        Statement s;
        CallableStatement cstmt = null;
        cm = new CryptManager();
        _list = new ArrayList();

       // System.out.println("userNameFilter in getUserByQueryProc: "+userNameFilter+"  passwordfilter: "+passwordfilter+"  Name: "+Name+"  tokenfilter: "+tokenfilter+"  pass: "+pass);
        c = null;
        rs = null;
        s = null;
        try {
//        	Name = Cryptomanager.decrypt(Name);
//            pass = Cryptomanager.decrypt(pass);
//        	Name = cm.decrypt(Name);
//            pass = cm.decrypt(pass);
        	 c = getConnection();
             stmt = c.createStatement();
//             cstmt = c.prepareCall(		     
//		         "{call getUserByQuery(" + userNameFilter + ","+passwordfilter+","+tokenfilter+")}");
           	 String nameFilter = userNameFilter;
            	nameFilter = nameFilter.replaceAll("\\s", "");
            	
            	 String tokenFilter = tokenfilter;
            	tokenFilter = tokenFilter.replaceAll("\\s", "");
             cstmt = c.prepareCall("{CALL getUserByQueryNoPassword(?,?)}");
             cstmt.setString(1, nameFilter);
             cstmt.setString(2, tokenFilter);
		    // cstmt.execute();
		     
//		     rs = cstmt.getResultSet();
//             System.out.println("userNameFilter<<<<<<" + userNameFilter);
//            System.out.println("passwordfilter<<<<<<" + passwordfilter);
//             System.out.println("tokenfilter<<<<<<" + tokenfilter);
		     rs = cstmt.executeQuery();
//		     System.out.println("Trying to execute rs....");
        while (rs.next()) {
//        	System.out.println("Inside rs....");
            String userId = rs.getString(1);
            String userName = rs.getString(2);
            String password = rs.getString(7);
//            System.out.println("cm.decrypt(password): " + cm.decrypt(password));
            if(Name(userName, pass, cm.decrypt(password)))
            {
            	System.out.println("we are here....");
                String fullName = rs.getString(3);
                String legacySysId = rs.getString(4);
                String Class = rs.getString(5);
                String branch = rs.getString(6);
                String phoneNo = rs.getString(8);
                String isSupervisor = rs.getString(9);
                String mustChangePwd = rs.getString(10);
                String loginStatus = rs.getString(11);
                String userStatus = rs.getString(12);
                String user_Id = rs.getString(13);
                String createDate = rs.getString(14);
                String passwordExpiry = rs.getString(15);
                String loginDate = rs.getString(16);
                String loginSystem = rs.getString(17);
                String fleetAdmin = rs.getString(18);
                String email = rs.getString(19);
                String branchCode = rs.getString(20);
                String pwdChanged = rs.getString(21);
                Date expiry_date = rs.getDate(22);
                String branch_restriction = rs.getString(23);
                String apprvLimit = rs.getString(24);
                String apprvLevel = rs.getString(25);
                String tokenRequired = rs.getString(26);
                String regionCode = rs.getString(27);
                String zoneCode = rs.getString(28);
                String regionRestrict = rs.getString(29);
                String zoneRestrict = rs.getString(30);
                String isFacilityAdministrator = rs.getString(31);
                String isStoreAdministrator = rs.getString(32);
                //String deptCode = rs.getString(33);  
//                System.out.println((new StringBuilder()).append("####################################### Token Read from the DB here ====").append(tokenRequired).toString());
//                System.out.println((new StringBuilder()).append(" The sent tokenRequired ==== ").append(tokenRequired).toString());

               
                user = new legend.admin.objects.User();
                user.setUserId(userId);
                user.setUserName(userName);
                user.setUserFullName(fullName);
                user.setLegacySystemId(legacySysId);
                user.setUserClass(Class);
                user.setBranch(branch);
                user.setPassword(password);
                user.setPhoneNo(phoneNo);
                user.setIsSupervisor(isSupervisor);
                user.setMustChangePwd(mustChangePwd);
                user.setLoginStatus(loginStatus);
                user.setUserStatus(userStatus);
                user.setCreatedBy(user_Id);
                user.setCreateDate(createDate);
                user.setPwdExpiry(passwordExpiry);
                user.setLastLogindate(loginDate);
                user.setLoginSystem(loginSystem);
                user.setFleetAdmin(fleetAdmin);
                user.setEmail(email);
                user.setBranch(branchCode);
                user.setPwdChanged(pwdChanged);
                user.setApprvLimit(apprvLimit);
                user.setBranchRestrict(branch_restriction);
                user.setApprvLevel(apprvLevel);
                user.setExpDate(expiry_date);
                user.setRegionCode(regionCode);
                user.setRegionRestrict(regionRestrict);
                user.setZoneCode(zoneCode);
                user.setZoneRestrict(zoneRestrict);
                user.setIsFacilityAdministrator(isFacilityAdministrator);
                user.setIsStoreAdministrator(isStoreAdministrator);
                //user.setDeptCode(deptCode);
                if(tokenRequired != null && tokenRequired.trim().equalsIgnoreCase("Y"))
                {
                    user.setTokenRequired(true);
                } else
                {
                    user.setTokenRequired(false);
                }
//                System.out.println((new StringBuilder()).append("##################################### After setting the user the token of the us" +
//"er =="
//).append(user.isTokenRequired()).toString());
                _list.add(user);
            }
        }
//        System.out.println("_list...." + _list.size());
//        closeConnection(con, stmt, rs);
//        closeConnection(c, s, rs);
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        //closeConnection(c, s, rs);
    	rs.close();
    	cstmt.close();
    	c.close();
    }
    return _list;

}
    
    public java.util.ArrayList getUserByQueryProc(
            String userNameFilter, 
            String passwordFilter, 
            String Name, 
            String tokenFilter, 
            String pass) throws SQLException {
    	
    	java.util.ArrayList userList = new java.util.ArrayList();
        CryptManager cm = new CryptManager();

        // Clean input filters
        String cleanedUserName = userNameFilter != null ? userNameFilter.replaceAll("\\s", "") : "";
        String cleanedToken = tokenFilter != null ? tokenFilter.replaceAll("\\s", "") : "";

        String sql = "{CALL getUserByQueryNoPassword(?, ?)}";

        try (Connection con = getConnection();
             CallableStatement cstmt = con.prepareCall(sql)) {

            // Set stored procedure parameters
            cstmt.setString(1, cleanedUserName);
            cstmt.setString(2, cleanedToken);

            try (ResultSet rs = cstmt.executeQuery()) {

                while (rs.next()) {
                    String userName = rs.getString(2);
                    String encryptedPassword = rs.getString(7);

                    // Only add user if credentials match
                    if (Name(userName, pass, cm.decrypt(encryptedPassword))) {

                        legend.admin.objects.User user = new legend.admin.objects.User();
                        user.setUserId(rs.getString(1));
                        user.setUserName(userName);
                        user.setUserFullName(rs.getString(3));
                        user.setLegacySystemId(rs.getString(4));
                        user.setUserClass(rs.getString(5));
                        user.setBranch(rs.getString(6));
                        user.setPassword(encryptedPassword);
                        user.setPhoneNo(rs.getString(8));
                        user.setIsSupervisor(rs.getString(9));
                        user.setMustChangePwd(rs.getString(10));
                        user.setLoginStatus(rs.getString(11));
                        user.setUserStatus(rs.getString(12));
                        user.setCreatedBy(rs.getString(13));
                        user.setCreateDate(rs.getString(14));
                        user.setPwdExpiry(rs.getString(15));
                        user.setLastLogindate(rs.getString(16));
                        user.setLoginSystem(rs.getString(17));
                        user.setFleetAdmin(rs.getString(18));
                        user.setEmail(rs.getString(19));
                        user.setBranch(rs.getString(20));
                        user.setPwdChanged(rs.getString(21));
                        user.setExpDate(rs.getDate(22));
                        user.setBranchRestrict(rs.getString(23));
                        user.setApprvLimit(rs.getString(24));
                        user.setApprvLevel(rs.getString(25));
                        user.setTokenRequired("Y".equalsIgnoreCase(rs.getString(26)));
                        user.setRegionCode(rs.getString(27));
                        user.setZoneCode(rs.getString(28));
                        user.setRegionRestrict(rs.getString(29));
                        user.setZoneRestrict(rs.getString(30));
                        user.setIsFacilityAdministrator(rs.getString(31));
                        user.setIsStoreAdministrator(rs.getString(32));

                        userList.add(user);
                    }
                }

            }

        } catch (Exception e) {
            System.err.println("Error in getUserByQueryProc(): " + e.getMessage());
            e.printStackTrace();
            throw new SQLException("Failed to fetch users", e);
        }

        return userList;
    }

    

    public java.util.ArrayList getUserByQueryNoPasswordProc(String userNameFilter, String Name,String tokenfilter) throws SQLException
    {  
        java.util.ArrayList _list = new java.util.ArrayList();
        legend.admin.objects.User user = null;
        String query;
        Connection c;
        ResultSet rs;
        Statement s;
        CallableStatement cstmt = null;
        _list = new ArrayList();

//        System.out.println("userNameFilter in getUserByQueryNoPassword: "+userNameFilter+"  Name: "+Name+"  tokenfilter: "+tokenfilter);
        c = null;
        rs = null;
        s = null;  
        try {
        	 c = getConnection();
             stmt = c.createStatement();
//             cstmt = c.prepareCall(		     
//		         "{call getUserByQuery(" + userNameFilter + ","+passwordfilter+","+tokenfilter+")}");
           	 String nameFilter = userNameFilter;
            	nameFilter = nameFilter.replaceAll("\\s", "");
            	 String tokenFilter = tokenfilter;
            	tokenFilter = tokenFilter.replaceAll("\\s", "");
             cstmt = c.prepareCall("{CALL getUserByQueryNoPassword(?,?)}");
             cstmt.setString(1, nameFilter);
             cstmt.setString(2, tokenFilter);
		    // cstmt.execute();
		     
//		     rs = cstmt.getResultSet();
//             System.out.println("userNameFilter<<<<<<" + userNameFilter);
//             System.out.println("passwordfilter<<<<<<" + passwordfilter);
//             System.out.println("tokenfilter<<<<<<" + tokenfilter);
		     rs = cstmt.executeQuery();
//		     System.out.println("Trying to execute rs....");
        while (rs.next()) {
//        	System.out.println("Inside rs....");
            String userId = rs.getString(1);
            String userName = rs.getString(2);
            String password = rs.getString(7);
//            if(Name(userName, pass, cm.decrypt(password)))
//            {
                String fullName = rs.getString(3);
                String legacySysId = rs.getString(4);
                String Class = rs.getString(5);
                String branch = rs.getString(6);
                String phoneNo = rs.getString(8);
                String isSupervisor = rs.getString(9);
                String mustChangePwd = rs.getString(10);
                String loginStatus = rs.getString(11);
                String userStatus = rs.getString(12);
                String user_Id = rs.getString(13);
                String createDate = rs.getString(14);
                String passwordExpiry = rs.getString(15);
                String loginDate = rs.getString(16);
                String loginSystem = rs.getString(17);
                String fleetAdmin = rs.getString(18);
                String email = rs.getString(19);
                String branchCode = rs.getString(20);
                String pwdChanged = rs.getString(21);
                Date expiry_date = rs.getDate(22);
                String branch_restriction = rs.getString(23);
                String apprvLimit = rs.getString(24);
                String apprvLevel = rs.getString(25);
                String tokenRequired = rs.getString(26);
                String regionCode = rs.getString(27);
                String zoneCode = rs.getString(28);
                String regionRestrict = rs.getString(29);
                String zoneRestrict = rs.getString(30);
                String isFacilityAdministrator = rs.getString(31);
                String isStoreAdministrator = rs.getString(32);
//                System.out.println((new StringBuilder()).append("####################################### Token Read from the DB here ====").append(tokenRequired).toString());
//                System.out.println((new StringBuilder()).append(" The sent tokenRequired ==== ").append(tokenRequired).toString());

                user = new legend.admin.objects.User();
                user.setUserId(userId);
                user.setUserName(userName);
                user.setUserFullName(fullName);
                user.setLegacySystemId(legacySysId);
                user.setUserClass(Class);
                user.setBranch(branch);
                user.setPassword(password);
                user.setPhoneNo(phoneNo);
                user.setIsSupervisor(isSupervisor);
                user.setMustChangePwd(mustChangePwd);
                user.setLoginStatus(loginStatus);
                user.setUserStatus(userStatus);
                user.setCreatedBy(user_Id);
                user.setCreateDate(createDate);
                user.setPwdExpiry(passwordExpiry);
                user.setLastLogindate(loginDate);
                user.setLoginSystem(loginSystem);
                user.setFleetAdmin(fleetAdmin);
                user.setEmail(email);
                user.setBranch(branchCode);
                user.setPwdChanged(pwdChanged);
                user.setApprvLimit(apprvLimit);
                user.setBranchRestrict(branch_restriction);
                user.setApprvLevel(apprvLevel);
                user.setExpDate(expiry_date);
                user.setRegionCode(regionCode);
                user.setRegionRestrict(regionRestrict);
                user.setZoneCode(zoneCode);
                user.setZoneRestrict(zoneRestrict);
                user.setIsFacilityAdministrator(isFacilityAdministrator);
                user.setIsStoreAdministrator(isStoreAdministrator);
                if(tokenRequired != null && tokenRequired.trim().equalsIgnoreCase("Y"))
                {
                    user.setTokenRequired(true);
                } else
                {
                    user.setTokenRequired(false);
                }
//                System.out.println((new StringBuilder()).append("##################################### After setting the user the token of the us" +
//"er =="
//).append(user.isTokenRequired()).toString());
                _list.add(user);
//            }
        }
//        closeConnection(con, stmt, rs);
//        closeConnection(c, s, rs);
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        //closeConnection(c, s, rs);
    	rs.close();
    	cstmt.close();
    	c.close();
    }
    return _list;

}


    public String getTokenUserIdOld(String userName) throws SQLException {
        Connection con = null;
        //PreparedStatement ps = null;
         Statement stmt = null;
        ResultSet rs = null;

        String tokenUserId = "";
        CallableStatement cstmt = null;
//        String sql = "EXEC getCompanyInfo'" + compCode + "'";
//        System.out.println("\n\n calling getCompanyInfo() with this stored procedure " +sql);
        try {
        	 con = getConnection();
             stmt = con.createStatement();
//         cstmt = con.prepareCall(
//                 "{call getTokenUserId('" + userName + "')}");
         cstmt = con.prepareCall("{CALL getTokenUserId(?)}");
         cstmt.setString(1, userName);
         cstmt.execute();
         rs = cstmt.getResultSet();
         if (rs.next()) {
             tokenUserId = rs.getString(1);
 //            System.out.println("======tokenUserId Name:  "+tokenUserId);
         }      

        } catch (Exception ex) {

            System.out.println("Error occurred in getTokenUserId() of SecurityHandler  >>" +ex);
        } finally {
        //closeConnection(con,stmt,rs);
        	rs.close();
        	cstmt.close();
        	con.close();
        }

        return tokenUserId;
    }
    
    public String getTokenUserId(String userName) throws SQLException {
        String tokenUserId = "";
        String sql = "{CALL getTokenUserId(?)}";

        try (Connection con = getConnection();
             CallableStatement cstmt = con.prepareCall(sql)) {

            cstmt.setString(1, userName);

            try (ResultSet rs = cstmt.executeQuery()) {
                if (rs.next()) {
                    tokenUserId = rs.getString(1);
                }
            }

        } catch (SQLException ex) {
            System.err.println("Error in getTokenUserId() >> " + ex.getMessage());
            throw ex; 
        }

        return tokenUserId;
    }


    public String getUserMailOld(int userId) throws SQLException {
        Connection con = null;
        //PreparedStatement ps = null;
         Statement stmt = null;
        ResultSet rs = null;

        String email = "";
        CallableStatement cstmt = null;
//        String sql = "EXEC getCompanyInfo'" + compCode + "'";
//        System.out.println("\n\n calling getCompanyInfo() with this stored procedure " +sql);
        try {
        	 con = getConnection();
             stmt = con.createStatement();
//         cstmt = con.prepareCall(
//                 "{call getUserId(" + userId + ")}");
         cstmt = con.prepareCall("{CALL getUserId(?)}");
         cstmt.setInt(1, userId);
         cstmt.execute();
         rs = cstmt.getResultSet();
         if (rs.next()) {
             email = rs.getString(1);
 //            System.out.println("======Company Name:  "+compName);
         }      

        } catch (Exception ex) {

            System.out.println("Error occurred in getUserMail() of SecurityHandler  >>" +ex);
        } finally {
        //closeConnection(con,stmt,rs);
        	rs.close();
        	cstmt.close();
        	con.close();
        }

        return email;
    }
    
    
    public String getUserMail(int userId) throws SQLException {
        String email = "";
        String sql = "{CALL getUserId(?)}";

        try (Connection con = getConnection();
             CallableStatement cstmt = con.prepareCall(sql)) {

            cstmt.setInt(1, userId);

            try (ResultSet rs = cstmt.executeQuery()) {
                if (rs.next()) {
                    email = rs.getString(1);
                }
            }

        } catch (SQLException ex) {
            System.err.println("Error occurred in getUserMail() of SecurityHandler >> " + ex.getMessage());
            throw ex; // propagate exception
        }

        return email;
    }



//    public String getLoginStatus(String  userName) throws SQLException {
//        Connection con = null;
//        //PreparedStatement ps = null;
//         Statement stmt = null;
//        ResultSet rs = null;
// //       System.out.println("======userName Name:  "+userName);
//        String loginstatus = "";
//        CallableStatement cstmt = null;
//        try {
//        	 con = getConnection();
//             stmt = con.createStatement();
////         cstmt = con.prepareCall(
////                 "{call getLoginStatus(" + userName + ")}");
//         cstmt = con.prepareCall("{CALL getLoginStatus(?)}");
//         cstmt.setString(1, userName);
//         //cstmt.execute();
//         rs = cstmt.executeQuery();
//         if (rs.next()) {
//             loginstatus = rs.getString(1);
// //            System.out.println("======Company Name:  "+compName);
//         }      
//
//        } catch (Exception ex) {
//
//            System.out.println("Error occurred in getLoginStatus() of SecurityHandler  >>" +ex);
//        } finally {
//        //closeConnection(con,stmt,rs);
//        	rs.close();
//        	cstmt.close();
//        	con.close();
//        }
//
//        return loginstatus;
//    }
    
    public String getLoginStatus(String  userName) throws SQLException {

        String loginstatus = "";

        try (
                Connection con = getConnection();
                CallableStatement cstmt = con.prepareCall("{CALL getLoginStatus(?)}")
            ) {

                cstmt.setString(1, userName);

                try (ResultSet rs = cstmt.executeQuery()) {
                    if (rs.next()) {
                        loginstatus = rs.getString(1);
                    }
                }

            } catch (Exception ex) {
                throw new SQLException("Error in getLoginStatus()", ex);
            }

        return loginstatus;
    }

   

    

    public String getclassNameOld(String userClass,String param) throws SQLException {
        Connection con = null;
        //PreparedStatement ps = null;
         Statement stmt = null;
        ResultSet rs = null;
//        System.out.println("======userClass Name:  "+userClass+"     param: "+param);
        String className = "";
        CallableStatement cstmt = null;
        try {
        	 con = getConnection();
             stmt = con.createStatement();
//         cstmt = con.prepareCall(
//                 "{call getclassName(" + userClass + ")}");
         cstmt = con.prepareCall("{CALL getclassName(?,?)}");
         cstmt.setString(1, userClass);
         cstmt.setString(2, param);
         cstmt.execute();
         rs = cstmt.getResultSet();
         if (rs.next()) {
        	 className = rs.getString(1);
 //            System.out.println("======Company Name:  "+compName);
         }      

        } catch (Exception ex) {

            System.out.println("Error occurred in getclassName() of SecurityHandler  >>" +ex);
        } finally {
        //closeConnection(con,stmt,rs);
        	rs.close();
        	cstmt.close();
        	con.close();
        }

        return className;
    }
    
    public String getClassName(String userClass, String param) throws SQLException {
        String className = "";

        String sql = "{CALL getclassName(?, ?)}";

       
        try (Connection con = getConnection();
             CallableStatement cstmt = con.prepareCall(sql)) {

            cstmt.setString(1, userClass);
            cstmt.setString(2, param);

            
            try (ResultSet rs = cstmt.executeQuery()) {
                if (rs.next()) {
                    className = rs.getString(1);
                }
            }

        } catch (SQLException ex) {
            System.err.println("Error in getClassName() >> " + ex.getMessage());
            throw ex; 
        }

        return className;
    }


    public String getBranchCodeOld(int branchId) throws SQLException {
        Connection con = null;
        //PreparedStatement ps = null;
         Statement stmt = null;
        ResultSet rs = null;

        String branchCode = "";
        CallableStatement cstmt = null;
//        String sql = "EXEC getCompanyInfo'" + compCode + "'";
//        System.out.println("\n\n calling getCompanyInfo() with this stored procedure " +sql);
        try {
        	 con = getConnection();
             stmt = con.createStatement();
//         cstmt = con.prepareCall(
//                 "{call getUserId(" + userId + ")}");
         cstmt = con.prepareCall("{CALL getBranchCode(?)}");
         cstmt.setInt(1, branchId);
         cstmt.execute();
         rs = cstmt.getResultSet();
         if (rs.next()) {
        	 branchCode = rs.getString(1);
 //            System.out.println("======Company Name:  "+compName);
         }      

        } catch (Exception ex) {

            System.out.println("Error occurred in getBranchCode() of SecurityHandler  >>" +ex);
        } finally {
        //closeConnection(con,stmt,rs);
        	rs.close();
        	cstmt.close();
        	con.close();
        }

        return branchCode;
    }
    
    public String getBranchCode(int branchId) throws SQLException {
        String branchCode = "";

        String sql = "{CALL getBranchCode(?)}";

       
        try (Connection con = getConnection();
             CallableStatement cstmt = con.prepareCall(sql)) {

            cstmt.setInt(1, branchId);

            
            try (ResultSet rs = cstmt.executeQuery()) {
                if (rs.next()) {
                    branchCode = rs.getString(1);
                }
            }

        } catch (SQLException ex) {
            System.err.println("Error in getBranchCode() >> " + ex.getMessage());
            throw ex; 
        }

        return branchCode;
    }


    public String getExitclassOld(String pageName, String classId) throws SQLException {
        Connection con = null;
        //PreparedStatement ps = null;
         Statement stmt = null;
        ResultSet rs = null;
        String validPage = "";
        CallableStatement cstmt = null;
        try {
//        	 System.out.println("======page Name:  "+pageName+"    class Id: "+classId);
        	 con = getConnection();
             stmt = con.createStatement();
             pageName = pageName.replaceAll("\\s", "");
             classId = classId.replaceAll("\\s", "");
         cstmt = con.prepareCall("{CALL getExitclass(?,?)}");
         cstmt.setString(1, pageName);
         cstmt.setString(2, classId);
         cstmt.execute(); 
         rs = cstmt.getResultSet();
         if (rs.next()) {
        	 validPage = rs.getString(1);
        	 int valid = rs.getInt(1);
//             System.out.println("======valid Page: "+validPage+"    Valid: "+valid);
         }      

        } catch (Exception ex) {

            System.out.println("Error occurred in getExitclass() of SecurityHandler  >>" +ex);
        } finally {
        //closeConnection(con,stmt,rs);
        	rs.close();
        	cstmt.close();
        	con.close();
        }
//        System.out.println("======valid Page======: "+validPage);
        return validPage;
    }
    
    public String getExitClass(String pageName, String classId) throws SQLException {
        String validPage = "";

        String sql = "{CALL getExitclass(?, ?)}";

        
        try (Connection con = getConnection();
             CallableStatement cstmt = con.prepareCall(sql)) {

            pageName = pageName != null ? pageName.trim() : "";
            classId = classId != null ? classId.trim() : "";

            cstmt.setString(1, pageName);
            cstmt.setString(2, classId);

            try (ResultSet rs = cstmt.executeQuery()) {
                if (rs.next()) {
                    validPage = rs.getString(1);
                }
            }

        } catch (SQLException ex) {
            System.err.println("Error in getExitClass() >> " + ex.getMessage());
            throw ex; 
        }

        return validPage;
    }


    public String getClassIdOld(String  userName) throws SQLException {
        Connection con = null;
        //PreparedStatement ps = null;
         Statement stmt = null;
        ResultSet rs = null;
        String classId = "";
        CallableStatement cstmt = null;
        try {
        	 con = getConnection();
             stmt = con.createStatement();
         cstmt = con.prepareCall("{CALL getClassId(?)}");
         cstmt.setString(1, userName);
         rs = cstmt.executeQuery();
         if (rs.next()) {
             classId = rs.getString(1);
         }      

        } catch (Exception ex) {

            System.out.println("Error occurred in getClassId() of SecurityHandler  >>" +ex);
        } finally {
        //closeConnection(con,stmt,rs);
        	rs.close();
        	cstmt.close();
        	con.close();
        }

        return classId;
    }
    
    public String getClassId(String userName) throws SQLException {
        String classId = "";
        String sql = "{CALL getClassId(?)}";

       
        try (Connection con = getConnection();
             CallableStatement cstmt = con.prepareCall(sql)) {

            cstmt.setString(1, userName);

            try (ResultSet rs = cstmt.executeQuery()) {
                if (rs.next()) {
                    classId = rs.getString(1);
                }
            }

        } catch (SQLException ex) {
            System.err.println("Error in getClassId() >> " + ex.getMessage());
            throw ex; 
        }

        return classId;
    }


    public String getSubMenuExitOld(String pageName) throws SQLException {
        Connection con = null;
        //PreparedStatement ps = null;
         Statement stmt = null;
        ResultSet rs = null;
        String subClass = "";
        CallableStatement cstmt = null;
        try {
 //       	 System.out.println("======page Name:  "+pageName);
        	 con = getConnection();
             stmt = con.createStatement();
             pageName = pageName.replaceAll("\\s", "");
         cstmt = con.prepareCall("{CALL getSubMenuExit(?)}");
         cstmt.setString(1, pageName);
         cstmt.execute();
         rs = cstmt.getResultSet();
         if (rs.next()) {
        	 subClass = rs.getString(1);
 //            System.out.println("======subClass Page: "+subClass);
         }      

        } catch (Exception ex) {

            System.out.println("Error occurred in getSubMenuExit() of SecurityHandler  >>" +ex);
        } finally {
        //closeConnection(con,stmt,rs);
        	rs.close();
        	cstmt.close();
        	con.close();
        }
//        System.out.println("======subClass Page======: "+subClass);
        return subClass;
    }
    
    public String getSubMenuExit(String pageName) throws SQLException {
        String subClass = "";
        String sql = "{CALL getSubMenuExit(?)}";

        
        try (Connection con = getConnection();
             CallableStatement cstmt = con.prepareCall(sql)) {

            pageName = pageName != null ? pageName.trim() : "";
            cstmt.setString(1, pageName);

            try (ResultSet rs = cstmt.executeQuery()) {
                if (rs.next()) {
                    subClass = rs.getString(1);
                }
            }

        } catch (SQLException ex) {
            System.err.println("Error in getSubMenuExit() >> " + ex.getMessage());
            throw ex; 
        }

        return subClass;
    }


    public String getToDateOld() throws SQLException {
        Connection con = null;
        //PreparedStatement ps = null;
         Statement stmt = null;
        ResultSet rs = null;
        String toDate = "";
        CallableStatement cstmt = null;
        try {
        	 con = getConnection();
             stmt = con.createStatement();
         cstmt = con.prepareCall("{CALL getToDate()}");
//         cstmt.setString(1, userName);
         rs = cstmt.executeQuery();
         if (rs.next()) {
        	 toDate = rs.getString(1);
         }      

        } catch (Exception ex) {

            System.out.println("Error occurred in getToDate() of SecurityHandler  >>" +ex);
        } finally {
        //closeConnection(con,stmt,rs);
        	rs.close();
        	cstmt.close();
        	con.close();
        }

        return toDate;
    }
    
    public String getToDate() throws SQLException {
        String toDate = "";
        String sql = "{CALL getToDate()}";

        try (Connection con = getConnection();
             CallableStatement cstmt = con.prepareCall(sql);
             ResultSet rs = cstmt.executeQuery()) {

            if (rs.next()) {
                toDate = rs.getString(1);
            }

        } catch (SQLException ex) {
            System.err.println("Error in getToDate() >> " + ex.getMessage());
            throw ex; 
        }

        return toDate;
    }


    public String getFromDateOld() throws SQLException {
        Connection con = null;
        //PreparedStatement ps = null;
         Statement stmt = null;
        ResultSet rs = null;
        String fromDate = "";
        CallableStatement cstmt = null;
        try {
        	 con = getConnection();
             stmt = con.createStatement();
         cstmt = con.prepareCall("{CALL getFromDate()}");
//         cstmt.setString(1, userName);
         rs = cstmt.executeQuery();
         if (rs.next()) {
        	 fromDate = rs.getString(1);
         }      

        } catch (Exception ex) {

            System.out.println("Error occurred in getFromDate() of SecurityHandler  >>" +ex);
        } finally {
        //closeConnection(con,stmt,rs);
        	rs.close();
        	cstmt.close();
        	con.close();
        }

        return fromDate;
    }
    
    public String getFromDate() throws SQLException {
        String fromDate = "";
        String sql = "{CALL getFromDate()}";

        // Use try-with-resources to automatically close Connection, CallableStatement, and ResultSet
        try (Connection con = getConnection();
             CallableStatement cstmt = con.prepareCall(sql);
             ResultSet rs = cstmt.executeQuery()) {

            if (rs.next()) {
                fromDate = rs.getString(1);
            }

        } catch (SQLException ex) {
            System.err.println("Error in getFromDate() >> " + ex.getMessage());
            throw ex; // propagate exception to caller
        }

        return fromDate;
    }


    public String getOtherMailsOld(String mailCode) throws SQLException {
        Connection con = null;
        //PreparedStatement ps = null;
         Statement stmt = null;
        ResultSet rs = null;
//       System.out.println("=======mailCode: "+mailCode);
        String mailAddress = "";
        CallableStatement cstmt = null;
        try {
        	 con = getConnection();
             stmt = con.createStatement();
         cstmt = con.prepareCall("{CALL getOtherMails(?)}");
         cstmt.setString(1, mailCode);
         rs = cstmt.executeQuery();
         if (rs.next()) {
        	 mailAddress = rs.getString(1);
         }      

        } catch (Exception ex) {
  
            System.out.println("Error occurred in getOtherMails() of SecurityHandler  >>" +ex);
        } finally {
        //closeConnection(con,stmt,rs);
        	rs.close();
        	cstmt.close();
        	con.close();
        }

        return mailAddress;
    }
    
    public String getOtherMails(String mailCode) throws SQLException {
        String mailAddress = "";
        String sql = "{CALL getOtherMails(?)}";

        try (Connection con = getConnection();
             CallableStatement cstmt = con.prepareCall(sql)) {

            cstmt.setString(1, mailCode);

            try (ResultSet rs = cstmt.executeQuery()) {
                if (rs.next()) {
                    mailAddress = rs.getString(1);
                }
            }

        } catch (SQLException ex) {
            System.err.println("Error in getOtherMails() >> " + ex.getMessage());
            throw ex; 
        }

        return mailAddress;
    }


    public String createManageUser2Tmp(User user, String limit,String userId,String menuId)
    {
        Connection con;
        PreparedStatement ps;
        boolean done;
        con = null;
        ps = null;
        done = false;
        String query = "";
        String result = "";
        try {
        String tmpId = (new ApplicationHelper()).getGeneratedId("AM_GB_USERTMP");
        if(user.getExpiryDate() == "" || user.getExpiryDate().equalsIgnoreCase("null") || user.getExpiryDate() == null || user.getExpiryDate().equalsIgnoreCase(""))
        { 
             query = "INSERT INTO AM_GB_USERTMP( User_Name, Full_Name, Legacy_Sys_Id, Class, Branch, Pass" +
"word, Phone_No, Is_Supervisor, Must_Change_Pwd, Login_Status, User_Status, UserI" +
"d, Create_Date, Password_Expiry,Fleet_Admin, Email,User_id,branch_restriction, E" +
"xpiry_Days,Approval_Limit,dept_code,token_required,region_code,zone_code," +
"region_restriction,zone_restriction,Facility_Admin,Store_Admin,TMP_CREATE_DATE,RECORD_TYPE,TMPID,menuId)VALUES (?,?,?,?,?,?,?,?,?,?,?" +
",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
;
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getUserFullName());
            ps.setString(3, user.getLegacySystemId());
            ps.setString(4, user.getUserClass());
            ps.setString(5, user.getBranch());
            ps.setString(6, user.getPassword());
            ps.setString(7, user.getPhoneNo());
            ps.setString(8, user.getIsSupervisor());
            ps.setString(9, user.getMustChangePwd());
            ps.setString(10, user.getLoginStatus());
            ps.setString(11, user.getUserStatus());
            ps.setString(12, user.getCreatedBy());
            ps.setDate(13, df.dateConvert(new Date()));
            ps.setDate(14, df.dateConvert(df.addDayToDate(new Date(), Integer.parseInt(user.getPwdExpiry()))));
            ps.setString(15, user.getFleetAdmin());
            ps.setString(16, user.getEmail());
            ps.setString(17, userId);
            System.out.println((new StringBuilder()).append("createManageUser2Tmp tokenRequired: ").append(user.isTokenRequired()).toString());
            ps.setString(18, user.getBranchRestrict());
            ps.setInt(19, user.getExpiryDays());
            ps.setString(20, limit);
            ps.setString(21, user.getDeptCode());
            ps.setString(22, user.getTokenRequire());
            ps.setString(23, user.getRegionCode());
            ps.setString(24, user.getZoneCode());
            ps.setString(25, user.getRegionRestrict());
            ps.setString(26, user.getZoneRestrict());
            ps.setString(27, user.getIsFacilityAdministrator());
            ps.setString(28, user.getIsStoreAdministrator());
            ps.setDate(29, df.dateConvert(new java.util.Date()));
            ps.setString(30, "I");
            ps.setString(31, tmpId);
            ps.setString(32, menuId);
            done = ps.executeUpdate() != -1;
            if(done) result = tmpId;
        } else
        {
             query = "INSERT INTO AM_GB_USERTMP( User_Name, Full_Name, Legacy_Sys_Id, Class, Branch, Pass" +
"word, Phone_No, Is_Supervisor, Must_Change_Pwd, Login_Status, User_Status, UserI" +
"d, Create_Date, Password_Expiry,Fleet_Admin, Email,User_id,branch_restriction, E" +
"xpiry_Days,Expiry_Date,Approval_Limit,region_code,zone_code," +
"region_restriction,zone_restriction,Facility_Admin,Store_Admin,TMP_CREATE_DATE,RECORD_TYPE,TMPID,menuId)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
"?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
;
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getUserFullName());
            ps.setString(3, user.getLegacySystemId());
            ps.setString(4, user.getUserClass());
            ps.setString(5, user.getBranch());
            ps.setString(6, user.getPassword());
            ps.setString(7, user.getPhoneNo());
            ps.setString(8, user.getIsSupervisor());
            ps.setString(9, user.getMustChangePwd());
            ps.setString(10, user.getLoginStatus());
            ps.setString(11, user.getUserStatus());
            ps.setString(12, user.getCreatedBy());
            ps.setDate(13, df.dateConvert(new Date()));
            ps.setDate(14, df.dateConvert(df.addDayToDate(new Date(), Integer.parseInt(user.getPwdExpiry()))));
            ps.setString(15, user.getFleetAdmin());
            ps.setString(16, user.getEmail());
            ps.setString(17, userId);
            ps.setString(18, user.getBranchRestrict());
            ps.setInt(19, user.getExpiryDays());
            String temDate = user.getExpiryDate();
            ps.setDate(20, df.dateConvert(temDate));
            ps.setString(21, limit);
            ps.setString(22, user.getRegionCode());
            ps.setString(23, user.getZoneCode());
            ps.setString(24, user.getRegionRestrict());
            ps.setString(25, user.getZoneRestrict());
            ps.setString(26, user.getIsFacilityAdministrator());
            ps.setString(27, user.getIsStoreAdministrator());
            ps.setDate(28, df.dateConvert(new java.util.Date()));
            ps.setString(29, "I");
            ps.setString(30, tmpId);
            ps.setString(31, menuId);
            done = ps.executeUpdate() != -1;
            if(done) result = tmpId;
        }

        createPasswordHistory(userId, user.getPassword());
 //       closeConnection(con, ps);
        }
        catch (Exception e) {
            System.out.println("WARNING:Error executing Query in createManageUser2Tmp ->"
                    + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return result;


    }
    
    public legend.admin.objects.User getUserByUserTmpID(String UserID)
    {
        User user;
        String expiryDate;
//        boolean tokenRequired;
        String query;
        Connection c;
        ResultSet rs;
        PreparedStatement s;
        user = null;
        expiryDate = "";
//        tokenRequired = false;
        query = "SELECT User_Id, User_Name, Full_Name, Legacy_Sys_Id, Class, Branch, Ph" +
"one_No, is_Supervisor, Must_Change_Pwd, Login_Status, User_Status, UserId, Creat" +
"e_Date, Password_Expiry, Login_Date, Login_System, Fleet_Admin, email, Branch,pa" +
"ssword_changed,branch_restriction, Expiry_Days,Expiry_Date,dept_code,Approval_Level," +
"is_StockAdministrator,is_Storekeeper,Approval_Limit,dept_restriction,UnderTaker,region_code,zone_code,region_restriction,zone_restriction,Facility_Admin,Store_Admin,token_required FROM AM_GB_USERTMP WHERE TMPID = ?";
//System.out.println("getUserByUserID query: "+query);
        c = null;
        rs = null;
        s = null;
        try {

        c = getConnection();
        s = c.prepareStatement(query);
        s.setString(1, UserID);
        rs = s.executeQuery();
      
        while (rs.next()) {
            String userId = rs.getString("User_Id");
            String userName = rs.getString("User_Name");
            String fullName = rs.getString("Full_Name");
            String legacySysId = rs.getString("Legacy_Sys_Id");
            String Class = rs.getString("Class");
            String branch = rs.getString("Branch");
//            String password = rs.getString("Password");
            String phoneNo = rs.getString("Phone_No");
            String isSupervisor = rs.getString("Is_Supervisor");
            String mustChangePwd = rs.getString("Must_Change_Pwd");
            String loginStatus = rs.getString("Login_Status");
            String userStatus = rs.getString("User_Status");
            String user_Id = rs.getString("UserId");
            String createDate = rs.getString("Create_Date");
            String passwordExpiry = rs.getString("Password_Expiry");
            String loginDate = rs.getString("Login_Date");
            String loginSystem = rs.getString("Login_System");
            String fleetAdmin = rs.getString("Fleet_Admin");
            String email = rs.getString("email");
            String branchCode = rs.getString("Branch");
            String pwdChanged = rs.getString("password_changed");
            String branchRestrict = rs.getString("branch_restriction");
            int expiryDays = rs.getInt("Expiry_Days");
            String deptCode = rs.getString("dept_code");
//            String tokenRequire = rs.getString("token_required");
            String isStockAdministrator = rs.getString("is_StockAdministrator");
            String approveLevel = rs.getString("Approval_Level");
            String approveLimit = rs.getString("Approval_Limit");
            String isStorekeeper = rs.getString("is_Storekeeper");
            String buRestrict = rs.getString("dept_restriction");
            String underTaker = rs.getString("UnderTaker");
            String regionCode = rs.getString("region_code");
            String zoneCode = rs.getString("zone_code");
            String regionRestrict = rs.getString("region_restriction");
            String zoneRestrict = rs.getString("zone_restriction");
            String facilityAdmin = rs.getString("Facility_Admin");
            String storeAdmin = rs.getString("Store_Admin");
            String tokenRequire = rs.getString("token_required");
//            if(tokenRequire == "Y")
//            {
//                tokenRequired = true;
//            }
//            if(tokenRequire == "N")
//            {
//                tokenRequired = false;
//            }
            if(rs.getDate("Expiry_Date") != null)
            {
                expiryDate = rs.getDate("Expiry_Date").toString();
            }
            user = new User();
            user.setUserId(userId);
            user.setUserName(userName);
            user.setUserFullName(fullName);
            user.setLegacySystemId(legacySysId);
            user.setUserClass(Class);
            user.setBranch(branch);
//            user.setPassword(password);
            user.setPhoneNo(phoneNo);
            user.setIsSupervisor(isSupervisor);
            user.setMustChangePwd(mustChangePwd);
            user.setLoginStatus(loginStatus);
            user.setUserStatus(userStatus);
            user.setCreatedBy(user_Id);
            user.setCreateDate(createDate);
            user.setUserStatus(userStatus);
            user.setPwdExpiry(passwordExpiry);
            user.setLastLogindate(loginDate);
            user.setLoginSystem(loginSystem);
            user.setFleetAdmin(fleetAdmin);
            user.setEmail(email);
            user.setBranch(branchCode);
            user.setPwdChanged(pwdChanged);
            user.setBranchRestrict(branchRestrict);
            user.setExpiryDays(expiryDays);
            user.setExpiryDate(expiryDate);
            user.setDeptCode(deptCode);
//            user.setTokenRequire(tokenRequire);
            user.setIsStorekeeper(isStorekeeper);
            user.setApproveLevel(approveLevel);
            user.setApprvLimit(approveLimit);
            user.setIsStockAdministrator(isStockAdministrator);
            user.setDeptRestrict(buRestrict);
            user.setUnderTaker(underTaker);
            user.setRegionCode(regionCode);
            user.setZoneCode(zoneCode);
            user.setRegionRestrict(regionRestrict);
            user.setZoneRestrict(zoneRestrict);
            user.setIsFacilityAdministrator(facilityAdmin);
            user.setIsStoreAdministrator(storeAdmin);
            user.setTokenRequire(tokenRequire);
            
        }
 //       closeConnection(c, s, rs);
    }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(c, s, rs);
        }
        return user;
    }


    public boolean createManageUser2(User user, String limit)
    {
        Connection con;
        PreparedStatement ps;
        boolean done;
        con = null;
        ps = null;
        done = false;
        String query = "";
        try {
        String userId = (new ApplicationHelper()).getGeneratedId("AM_GB_USER");
        if(user.getExpiryDate() == "" || user.getExpiryDate().equalsIgnoreCase("null") || user.getExpiryDate() == null || user.getExpiryDate().equalsIgnoreCase(""))
        {
             query = "INSERT INTO AM_GB_USER( User_Name, Full_Name, Legacy_Sys_Id, Class, Branch, Pass" +
"word, Phone_No, Is_Supervisor, Must_Change_Pwd, Login_Status, User_Status, UserI" +
"d, Create_Date, Password_Expiry,Fleet_Admin, Email,User_id,branch_restriction, E" +
"xpiry_Days,Approval_Limit,dept_code,token_required,region_code,zone_code," +
"region_restriction,zone_restriction,Facility_Admin,Store_Admin)VALUES (?,?,?,?,?,?,?,?,?,?,?" +
",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
;
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getUserFullName());
            ps.setString(3, user.getLegacySystemId());
            ps.setString(4, user.getUserClass());
            ps.setString(5, user.getBranch());
            ps.setString(6, user.getPassword());
            ps.setString(7, user.getPhoneNo());
            ps.setString(8, user.getIsSupervisor());
            ps.setString(9, user.getMustChangePwd());
            ps.setString(10, user.getLoginStatus());
            ps.setString(11, user.getUserStatus());
            ps.setString(12, user.getCreatedBy());
            ps.setDate(13, df.dateConvert(new Date()));
            ps.setDate(14, df.dateConvert(df.addDayToDate(new Date(), Integer.parseInt(user.getPwdExpiry()))));
            ps.setString(15, user.getFleetAdmin());
            ps.setString(16, user.getEmail());
            ps.setString(17, userId);
            System.out.println((new StringBuilder()).append("createManageUser2 tokenRequired: ").append(user.isTokenRequired()).toString());
            ps.setString(18, user.getBranchRestrict());
            ps.setInt(19, user.getExpiryDays());
            ps.setString(20, limit);
            ps.setString(21, user.getDeptCode());
            ps.setString(22, user.getTokenRequire());
            ps.setString(23, user.getRegionCode());
            ps.setString(24, user.getZoneCode());
            ps.setString(25, user.getRegionRestrict());
            ps.setString(26, user.getZoneRestrict());
            ps.setString(27, user.getIsFacilityAdministrator());
            ps.setString(28, user.getIsStoreAdministrator());
            done = ps.executeUpdate() != -1;
        } else
        {
             query = "INSERT INTO AM_GB_USER( User_Name, Full_Name, Legacy_Sys_Id, Class, Branch, Pass" +
"word, Phone_No, Is_Supervisor, Must_Change_Pwd, Login_Status, User_Status, UserI" +
"d, Create_Date, Password_Expiry,Fleet_Admin, Email,User_id,branch_restriction, E" +
"xpiry_Days,Expiry_Date,Approval_Limit,region_code,zone_code," +
"region_restriction,zone_restriction,Facility_Admin,Store_Admin)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
"?,?,?,?,?,?,?,?,?,?)"
;
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getUserFullName());
            ps.setString(3, user.getLegacySystemId());
            ps.setString(4, user.getUserClass());
            ps.setString(5, user.getBranch());
            ps.setString(6, user.getPassword());
            ps.setString(7, user.getPhoneNo());
            ps.setString(8, user.getIsSupervisor());
            ps.setString(9, user.getMustChangePwd());
            ps.setString(10, user.getLoginStatus());
            ps.setString(11, user.getUserStatus());
            ps.setString(12, user.getCreatedBy());
            ps.setDate(13, df.dateConvert(new Date()));
            ps.setDate(14, df.dateConvert(df.addDayToDate(new Date(), Integer.parseInt(user.getPwdExpiry()))));
            ps.setString(15, user.getFleetAdmin());
            ps.setString(16, user.getEmail());
            ps.setString(17, userId);
            ps.setString(18, user.getBranchRestrict());
            ps.setInt(19, user.getExpiryDays());
            String temDate = user.getExpiryDate();
            ps.setDate(20, df.dateConvert(temDate));
            ps.setString(21, limit);
            ps.setString(22, user.getRegionCode());
            ps.setString(23, user.getZoneCode());
            ps.setString(24, user.getRegionRestrict());
            ps.setString(25, user.getZoneRestrict());
            ps.setString(26, user.getIsFacilityAdministrator());
            ps.setString(27, user.getIsStoreAdministrator());
            done = ps.executeUpdate() != -1;
        }

        createPasswordHistory(userId, user.getPassword());
 //       closeConnection(con, ps);
        }
        catch (Exception e) {
            System.out.println("WARNING:Error executing Query in createManageUser2 ->"
                    + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;


    }

    public String[] PasswordChange(String userName, String pagePassword,String space)
    {
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        GregorianCalendar date = new GregorianCalendar();
        int sec = date.get(14);
        String output = pagePassword.trim();
        String prefix = userName.trim().substring(0, 2);
        int length = userName.trim().length();
        int length2 = length - 2;
        String surrfix = userName.trim().substring(length2, length);
        String Password = (new StringBuilder()).append(surrfix).append(prefix).append(sec).append(surrfix).append(prefix).toString();
        String PasswordDisplay = (new StringBuilder()).append(prefix).append(sec).append(surrfix).toString();
        String display[] = {
            Password, PasswordDisplay
        };
        return display;
    }

    public java.util.ArrayList getUserByQueryTech(String filter) {
        java.util.ArrayList _list = new java.util.ArrayList();
        legend.admin.objects.User user = null;
        String query = "SELECT User_Id, User_Name, Full_Name" + ", Legacy_Sys_Id, Class, Branch, Password" + ", Phone_No, is_Supervisor, Must_Change_Pwd" + ", Login_Status, User_Status, UserId, Create_Date"
                + ", Password_Expiry, Login_Date, Login_System, Fleet_Admin"
                + ", email, Branch, password_changed,Expiry_Date,branch_restriction"
                + ", approval_limit,approval_level,dept_code,section_code,Organization" + " FROM AM_GB_USER WHERE User_Id IS NOT NULL ";
        query += filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String userId = rs.getString("User_Id");
                String userName = rs.getString("User_Name");
                String fullName = rs.getString("Full_Name");
                String legacySysId = rs.getString("Legacy_Sys_Id");
                String Class = rs.getString("Class");
                String branch = rs.getString("Branch");
                String password = rs.getString("Password");
                String phoneNo = rs.getString("Phone_No");
                String isSupervisor = rs.getString("Is_Supervisor");
                String mustChangePwd = rs.getString("Must_Change_Pwd");
                String loginStatus = rs.getString("Login_Status");
                String userStatus = rs.getString("User_Status");
                String user_Id = rs.getString("UserId");
                String createDate = rs.getString("Create_Date");
                String passwordExpiry = rs.getString("Password_Expiry");
                String loginDate = rs.getString("Login_Date");
                String loginSystem = rs.getString("Login_System");
                String fleetAdmin = rs.getString("Fleet_Admin");
                String email = rs.getString("email");
                String branchCode = rs.getString("Branch");
                String pwdChanged = rs.getString("password_changed");
                Date expiry_date = rs.getDate("Expiry_Date");
                String branch_restriction = rs.getString("branch_restriction");
                String apprvLimit = rs.getString("approval_limit");
                String apprvLevel = rs.getString("approval_level");
                String deptCode = rs.getString("dept_code");
                String sectionCode = rs.getString("section_code");
                String organization = rs.getString("Organization");
                user = new legend.admin.objects.User();
                user.setUserId(userId);
                user.setUserName(userName);
                user.setUserFullName(fullName);
                user.setLegacySystemId(legacySysId);
                user.setUserClass(Class);
                user.setBranch(branch);
                user.setPassword(password);
                user.setPhoneNo(phoneNo);
                user.setIsSupervisor(isSupervisor);
                user.setMustChangePwd(mustChangePwd);
                user.setLoginStatus(loginStatus);
                user.setUserStatus(userStatus);
                user.setCreatedBy(user_Id);
                user.setCreateDate(createDate);

                user.setPwdExpiry(passwordExpiry);
                user.setLastLogindate(loginDate);
                user.setLoginSystem(loginSystem);
                user.setFleetAdmin(fleetAdmin);
                user.setEmail(email);
                user.setBranch(branchCode);
                user.setPwdChanged(pwdChanged);
                user.setApprvLimit(apprvLimit);
                user.setBranchRestrict(branch_restriction);
                user.setApprvLevel(apprvLevel);
                user.setExpDate(expiry_date);
                user.setDeptCode(deptCode);
//                user.setSection(sectionCode);
//                user.setOrganization(organization);
                _list.add(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }
    public java.util.ArrayList getAssigneeByQueryTech(String filter) {
        java.util.ArrayList _list = new java.util.ArrayList();
        legend.admin.objects.User user = null;
        String query = "SELECT User_Id, User_Name, Full_Name" + ", Legacy_Sys_Id, Class, Branch, Password" + ", Phone_No, is_Supervisor, Must_Change_Pwd" + ", Login_Status, User_Status, UserId, Create_Date"
                + ", Password_Expiry, Login_Date, Login_System, Fleet_Admin"
                + ", email, Branch, password_changed,Expiry_Date,branch_restriction"
                + ", approval_limit,approval_level,dept_code,dept_code AS section_code, '' AS Organization " + " FROM AM_GB_USER WHERE User_Id IS NOT NULL ";
        query += filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;
        System.out.println("getAssigneeByQueryTech query: "+query);
        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String userId = rs.getString("User_Id");
                String userName = rs.getString("User_Name");
                String fullName = rs.getString("Full_Name");
                String legacySysId = rs.getString("Legacy_Sys_Id");
                String Class = rs.getString("Class");
                String branch = rs.getString("Branch");
                String password = rs.getString("Password");
                String phoneNo = rs.getString("Phone_No");
                String isSupervisor = rs.getString("Is_Supervisor");
                String mustChangePwd = rs.getString("Must_Change_Pwd");
                String loginStatus = rs.getString("Login_Status");
                String userStatus = rs.getString("User_Status");
                String user_Id = rs.getString("UserId");
                String createDate = rs.getString("Create_Date");
                String passwordExpiry = rs.getString("Password_Expiry");
                String loginDate = rs.getString("Login_Date");
                String loginSystem = rs.getString("Login_System");
                String fleetAdmin = rs.getString("Fleet_Admin");
                String email = rs.getString("email");
                String branchCode = rs.getString("Branch");
                String pwdChanged = rs.getString("password_changed");
                Date expiry_date = rs.getDate("Expiry_Date");
                String branch_restriction = rs.getString("branch_restriction");
                String apprvLimit = rs.getString("approval_limit");
                String apprvLevel = rs.getString("approval_level");
                String deptCode = rs.getString("dept_code");
                String sectionCode = rs.getString("section_code");
                String organization = rs.getString("Organization");
                user = new legend.admin.objects.User();
                user.setUserId(userId);
                user.setUserName(userName);
                user.setUserFullName(fullName);
                user.setLegacySystemId(legacySysId);
                user.setUserClass(Class);
                user.setBranch(branch);
                user.setPassword(password);
                user.setPhoneNo(phoneNo);
                user.setIsSupervisor(isSupervisor);
                user.setMustChangePwd(mustChangePwd);
                user.setLoginStatus(loginStatus);
                user.setUserStatus(userStatus);
                user.setCreatedBy(user_Id);
                user.setCreateDate(createDate);

                user.setPwdExpiry(passwordExpiry);
                user.setLastLogindate(loginDate);
                user.setLoginSystem(loginSystem);
                user.setFleetAdmin(fleetAdmin);
                user.setEmail(email);
                user.setBranch(branchCode);
                user.setPwdChanged(pwdChanged);
                user.setApprvLimit(apprvLimit);
                user.setBranchRestrict(branch_restriction);
                user.setApprvLevel(apprvLevel);
                user.setExpDate(expiry_date);
                user.setDeptCode(deptCode);
//                user.setSection(sectionCode);
//                user.setorganization(organization);
                _list.add(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }

    public java.util.ArrayList getServicesByQueryTech(String filter) {
        java.util.ArrayList _list = new java.util.ArrayList();
        legend.admin.objects.User user = null;
        String query = "SELECT Service_code, Service_name FROM HD_SERVICE WHERE Status = 'Active'";
        query += filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

       try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String userId = rs.getString("Service_code");
                String userName = rs.getString("Service_code");
                String fullName = rs.getString("Service_name");
              //  System.out.print("====fullName==== "+fullName);
                user = new legend.admin.objects.User();
                user.setUserId(userId);
                user.setUserName(userName);
                user.setUserFullName(fullName);
                _list.add(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }


    public java.util.ArrayList getPriorityByQuery(String filter) {
        java.util.ArrayList _list = new java.util.ArrayList();
        legend.admin.objects.User user = null;
        String query = "SELECT Dept_code, Dept_name FROM AM_AD_DEPARTMENT order By Dept_name ";
        query += filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

       try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String userId = rs.getString("Dept_code");
                String userName = rs.getString("Dept_code");
                String fullName = rs.getString("Dept_name");
              //  System.out.print("====fullName==== "+fullName);
                user = new legend.admin.objects.User();
                user.setUserId(userId);
                user.setUserName(userName);
                user.setUserFullName(fullName);
                _list.add(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }

    public ArrayList<Staffs> getStaffsList(String filter, String extraQuery, String status)
    {
    	//System.out.println("filter: " + filter);
        ArrayList<Staffs> _list =  new ArrayList<Staffs>();
        HtmlUtility html = new HtmlUtility();
        Connection c = null;
        ResultSet rs = null;
        PreparedStatement s = null;
        String query = "";
        
        try {
        
//        if(filter.equals("")) {
//        	query= "select StaffId, Full_Name, dept_code, branch_code from am_gb_Staff \r\n" + 
//        			extraQuery +
//        			"union select StaffId, Full_Name, dept_code, branch_code from am_gb_Staff"
//        			+extraQuery;
//        	 c = getConnection();
//           //  System.out.println("query: " + query);
//             s = c.prepareStatement(query);
//             s.setString(1, status);
//             s.setString(2, status);
//        }
        	
        	if(filter.equals("")) {
            	query= "select StaffId, Full_Name, dept_code, branch_code from am_gb_Staff \r\n" + 
            			"union select StaffId, Full_Name, dept_code, branch_code from am_gb_Staff";
            	 c = getConnection();
                 System.out.println("query: " + query);
                 s = c.prepareStatement(query);
            }
        
        if(!filter.equals("")) {
            query = "select StaffId, Full_Name, dept_code, branch_code from am_gb_Staff where StaffId like ? \r\n" + 
           		"union\r\n" + 
           		"select StaffId, Full_Name, dept_code, branch_code from am_gb_Staff where Full_Name like ? ";
            c = getConnection();
           System.out.println("query: " + query);
            s = c.prepareStatement(query);
            s.setString(1, "%"+filter+"%");
            s.setString(2,  "%"+filter+"%");
           }

            rs = s.executeQuery();
        while(rs.next()){
        	String staffId = rs.getString("StaffId");
        	String fullName = rs.getString("Full_Name");
        	String deptCode = rs.getString("dept_code");
        	String branchCode = rs.getString("branch_code");
        	
        	String deptName = html.findObject("select Dept_name from am_ad_department where dept_code = '"+deptCode+"'");
        	String branchName = html.findObject("select BRANCH_NAME from am_ad_branch where BRANCH_CODE = '"+branchCode+"'");
        	
           // System.out.println("staffId: "+ staffId + " fullName: " + fullName + " deptCode: " + deptCode + " branchCode: " + branchCode);
            Staffs staffDetails = new Staffs();
        	staffDetails.setStaffId(staffId);
        	staffDetails.setFullName(fullName);
        	staffDetails.setDeptName(deptName);
        	staffDetails.setBranchName(branchName);
        	
        	_list.add(staffDetails);
        
        }
        }catch(Exception e) {
        	e.getMessage();
        }
        
        
        return _list;
    }

    public static String decrypt(String strToDecrypt, String secret) {
		try {
		Key key = generateKey(secret);
		Cipher cipher = Cipher.getInstance(ALGO);
		cipher.init(Cipher.DECRYPT_MODE, key);
		return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
		} catch (Exception e) {
		System.out.println("Error while decrypting: " + e.toString());
		}
		return null;
		}


	private static Key generateKey(String secret) throws Exception {
		byte[] decoded = Base64.getDecoder().decode(secret.getBytes());
		Key key = new SecretKeySpec(decoded, ALGO);
		return key;
		}

		public static String decodeKey(String str) {
		byte[] decoded = Base64.getDecoder().decode(str.getBytes());
		return new String(decoded);
		}

		public static String encodeKey(String str) {
		byte[] encoded = Base64.getEncoder().encode(str.getBytes());
		return new String(encoded);
		}
    
}
