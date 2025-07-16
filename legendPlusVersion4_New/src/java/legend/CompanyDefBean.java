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
public class CompanyDefBean extends legend.ConnectionClass {

//String[] company_information;
    Connection cnn = null;
    PreparedStatement prepstmt = null;
    Statement stmt = null;
    ResultSet rs = null;
    MagmaDBConnection dbConn = new MagmaDBConnection();

    public CompanyDefBean() throws Exception {
        super();
    }


    public String[] selectCompanyParameters() throws Exception {
        // StringBuffer sq = new StringBuffer(100);
        String selectstr =
                "select minimum_password,password_expiry,session_timeout from am_gb_company";
        
        PreparedStatement ps = null;
        String[] values = new String[3];
        try {
            cnn = getConnection();
            //stmt = cnn.createStatement();
            ps = cnn.prepareStatement(selectstr);
            rs = ps.executeQuery();
            //ResultSet rs = getStatement(str);
             rs.next();
             for (int x = 0; x < values.length; x++) {
                 values[x] = rs.getString(x + 1);
                 
             }
        } catch (Exception e) {
            e.printStackTrace();
        } 
    
        finally {
                    dbConn.closeConnection(cnn, prepstmt, rs);
                }

        return values;
    }



    public String[] getCompanyDefs() throws Exception {
        String selectstr =
                "select Company_name,Company_Address,  " +
                "minimum_password,password_expiry,session_timeout," +
                "user_Id from am_gb_company";
        PreparedStatement ps = null;
        ps = getConnection().prepareStatement(selectstr);
        ResultSet rs = ps.executeQuery();
        String[] company_info = new String[6];
        if (rs.next()) {
            for (int k = 0; k < 6; k++) {
                company_info[k] = rs.getString(k + 1);
            }
        }
        freeResource();
        //company_information = company_info;
        return company_info;
    }



    public boolean updateCompanyDef( String cn, String ra,
                                    String mp,
                                    String px, String st, String cu) {

        int res = 0;
        String updtstr = "UPDATE AM_GB_COMPANY SET COMPANY_NAME = '" + cn
                         +"', COMPANY_ADDRESS = '" + ra + "', " +
                        "MINIMUM_PASSWORD = '" + mp + "', " +
                         "PASSWORD_EXPIRY = '" + px + "',SESSION_TIMEOUT = '" +
                         st + "',USER_ID = '" + cu + "' ";

        try {

            cnn = dbConn.getConnection("fixedasset");
            prepstmt = cnn.prepareStatement(updtstr);
            res = prepstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbConn.closeConnection(cnn, prepstmt, rs);
        }
        if (res > 0) {
            return true;
        } else {
            return false;
        }

    }


}
