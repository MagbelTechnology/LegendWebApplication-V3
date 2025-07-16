/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package magma;

import com.magbel.util.ApplicationHelper;
import java.sql.*;
import java.util.*;

import magma.net.dao.MagmaDBConnection;
import magma.net.manager.FleetHistoryManager;
import magma.net.manager.AssetPaymentManager;
import com.magbel.util.DatetimeFormat;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import magma.util.Codes;
/**
 *
 * @author Olabo
 */
public class ApprovalBean extends legend.ConnectionClass {
        private MagmaDBConnection dbConnection;
         private DatetimeFormat dateFormat;
        java.text.SimpleDateFormat sdf;
        Codes code;
        private double mini_approval=0;
        private double max_approval =0;
        private String levle_code ="";

    public ApprovalBean() throws Exception {

        //super();
        try {
            freeResource();
            sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");
            dbConnection = new MagmaDBConnection();
            dateFormat = new DatetimeFormat();
            code = new Codes();
        } catch (Exception ex) {
        }
    }

    /**
     * @return the mini_approval
     */
    public double getMini_approval() {
        return mini_approval;
    }

    /**
     * @return the max_approval
     */
    public double getMax_approval() {
        return max_approval;
    }
  
    /**
     * @return the levle_code
     */
    public String getLevle_code() {
        return levle_code;
    }

public void getApprovalDetail(String levelCode){
String query ="select Min_Amount,Max_Amount from approval_limit where level_Code = '"+levelCode+"'";

  Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try {
                con = dbConnection.getConnection("fixedasset");
                ps = con.prepareStatement(query);
                rs = ps.executeQuery();

                if (rs.next()) {

                    mini_approval = rs.getDouble(1);
                    max_approval = rs.getDouble(2);

                }


            }catch (Exception ex) {
                System.out.println("ApprovalBean Class: getApprovalDetaial(): WARN: Error fetching approval details ->" + ex);
            } finally {
                dbConnection.closeConnection(con, ps, rs);
            }



}

public String getApprovalLevel(String userId){
String query ="select approval_level from am_gb_user where user_id = '"+userId+"'";


  Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try {
                con = dbConnection.getConnection("fixedasset");
                ps = con.prepareStatement(query);
                rs = ps.executeQuery();

                if (rs.next()) {

                    levle_code = rs.getString(1);
                    //max_approval = rs.getDouble(2);

                }
            

            }catch (Exception ex) {
                System.out.println("ApprovalBean Class: getApprovalLevel(): WARN: Error fetching approval details ->" + ex);
            } finally {
                dbConnection.closeConnection(con, ps, rs);
            }

return levle_code;

}



public String getApprovalLimit(String userId){
    int ApprovalLimit = Integer.parseInt(userId);
String query ="select approval_limit from am_gb_user where user_id = "+ApprovalLimit;


  Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try {
                con = dbConnection.getConnection("fixedasset");
                ps = con.prepareStatement(query);
                rs = ps.executeQuery();

                if (rs.next()) {

                    levle_code = rs.getString(1);
                    //max_approval = rs.getDouble(2);

                }


            }catch (Exception ex) {
                System.out.println("ApprovalBean Class: getApprovalLimit(): WARN: Error fetching approval details ->" + ex);
            } finally {
                dbConnection.closeConnection(con, ps, rs);
            }

return levle_code;

}

}
