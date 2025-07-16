package com.magbel.admin.handlers;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;
import com.magbel.util.DatetimeFormat;
import com.magbel.util.CurrencyNumberformat;
import com.magbel.admin.dao.MagmaDBConnection;
import com.magbel.admin.objects.Approval;
import java.sql.Blob;

//import magma.util.Codes;
public class ApprovalRecords extends MagmaDBConnection {

    private CurrencyNumberformat formata;
    private SimpleDateFormat sdf;
    private DatetimeFormat df;
    ArrayList Alist = new ArrayList();
    private String qryCheck = "";

    public ApprovalRecords() {
        super();
    }
 
    public ArrayList findApproval(String assetId, String filter) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Approval app = null;
        ArrayList collection = new ArrayList();
        String FINDER_QUERY = "SELECT  Id,Description,Page,Flag,partPay,UserId,Branch,subjectToVat,whTax,operation1,exitPage,url from am_raisentry_post WHERE ID = ? AND FLAG=?" + filter;

        try {
            con = getConnection("helpDesk");
            ps = con.prepareStatement(FINDER_QUERY);
            ps.setString(1, assetId);
            ps.setString(2, "Y");
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("Id");
                String Description = rs.getString("Description");
                String Page = rs.getString("Page");
                String Flag = rs.getString("Flag");
                String partPay = rs.getString("partPay");
                String UserId = rs.getString("UserId");
                String Branch = rs.getString("Branch");
                String subjectToVat = rs.getString("subjectToTax");
                String whTax = rs.getString("whTax");
                String operation1 = rs.getString("operation1");
                String exitPage = rs.getString("exitPage");
                String url = rs.getString("url");

                app = new Approval(id, Description, Page, Flag, partPay, UserId, Branch, subjectToVat, whTax, operation1, exitPage, url);
                collection.add(app);
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch [am_raisentry_post]->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return collection;

    }

    public ArrayList findApproval(String filter) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Approval app = null;
        ArrayList collection = new ArrayList();
        String FINDER_QUERY = "SELECT Id,Description,Page,Flag,partPay,UserId,Branch,subjectToVat,whTax,operation1,exitPage,url from am_raisentry_post WHERE  FLAG=? " + filter;

        try {
            con = getConnection("helpDesk");
            ps = con.prepareStatement(FINDER_QUERY);

            ps.setString(1, "Y");
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("Id");
                String Description = rs.getString("Description");
                String Page = rs.getString("Page");
                String Flag = rs.getString("Flag");
                String partPay = rs.getString("partPay");
                String UserId = rs.getString("UserId");
                String Branch = rs.getString("Branch");
                String subjectToVat = rs.getString("subjectToVat");
                String whTax = rs.getString("whTax");
                String operation1 = rs.getString("operation1");
                String exitPage = rs.getString("exitPage");
                String url = rs.getString("url");

                app = new Approval(id, Description, Page, Flag, partPay, UserId, Branch, subjectToVat, whTax, operation1, exitPage, url);
                // app.setAssetCreator(assetCreator(id));
                collection.add(app);
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch [am_raisentry_post]->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return collection;

    }

    public boolean isApprovalExisting(String assetId, String page) {
        boolean done = false;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        String FINDER_QUERY = "SELECT Id,Description,Page,Flag,partPay from am_raisentry_post WHERE ID = ? and page=?";

        try {
            con = getConnection("helpDesk");
            ps = con.prepareStatement(FINDER_QUERY);
            ps.setString(1, assetId);
            ps.setString(2, page);
            rs = ps.executeQuery();

            while (rs.next()) {
                done = true;
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch [am_raisentry_post]->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return done;

    }

    public boolean isApprovalExisting(String assetId) {
        boolean done = false;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        String FINDER_QUERY = "SELECT Id,Description,Page,Flag,partPay from am_raisentry_post WHERE ID = ?";

        try {
            con = getConnection("helpDesk");
            ps = con.prepareStatement(FINDER_QUERY);
            ps.setString(1, assetId);

            rs = ps.executeQuery();

            while (rs.next()) {
                done = true;
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch [am_raisentry_post]->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return done;

    }

    public void updateApproval(String assetid) {

        Connection con = null;
        PreparedStatement ps = null;
        String NOTIFY_QUERY = "UPDATE am_raisentry_post SET flag = ? WHERE ID = ?  ";

        try {
            con = getConnection("helpDesk");
            ps = con.prepareStatement(NOTIFY_QUERY);
            ps.setString(1, "N");
            ps.setString(2, assetid);
            ps.executeUpdate();

        } catch (Exception ex) {
            System.out.println("WARNING: cannot update am_raisentry_post+" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }

    }

    public boolean insertApproval(String id, String description, String page, String flag,
            String partPay, String UserId, String Branch, String subjectToVat, String whTax, String url) {
        boolean done = true;
        flag = "Y";
        Connection con = null;
        PreparedStatement ps = null;
        String query = "INSERT INTO [am_raisentry_post](Id,Description,Page,Flag,partPay,"
                + "UserId,Branch,subjectToVat,whTax,url)"
                + " VALUES(?,?,?,?,?,?,?,?,?,?)";
        try {
            con = getConnection("helpDesk");
            ps = con.prepareStatement(query);
            ps.setString(1, id);
            ps.setString(2, description);
            ps.setString(3, page);
            ps.setString(4, flag);
            ps.setString(5, partPay);
            ps.setString(6, UserId);
            ps.setString(7, Branch);
            ps.setString(8, subjectToVat);
            ps.setString(9, whTax);
            ps.setString(10, url);
            ps.execute();

        } catch (Exception ex) {
            done = false;
            System.out.println(
                    "WARNING:cannot insert am_raisentry_post->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;  
    }

    public String getCodeName(String query) {
        String result = "";
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
System.out.println("######query##### "+query);   
        try { 
            con = getConnection("helpDesk");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            //System.out.println("===getCodeName in ApproalRecords query=="+query);
            while (rs.next()) {
                result = rs.getString(1) == null ? "" : rs.getString(1);

            }
        } catch (Exception er) {
            System.out.println("Error in Query- getCodeName()... ->" + er);
            er.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
        return result;
    }
/*
    public String getservicesAffected(String query) {
        String result = "";
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = getConnection("helpDesk");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                result = rs.getString(1) == null ? "" : rs.getString(1);
            }
        } catch (Exception er) {
            System.out.println("Error in Query- servicesAffected()... ->" + er);
            er.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
        return result;
    }

*/
    
    public String userEmail(String user_id) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String email = "";  

        String FINDER_QUERY = "SELECT email from am_gb_user WHERE user_id = ? ";

        try {
            con = getConnection("helpDesk");
            ps = con.prepareStatement(FINDER_QUERY);
            ps.setString(1, user_id);

            rs = ps.executeQuery();

            while (rs.next()) {
                email = rs.getString("email");
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch email->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
       // System.out.println(">>>>>The user id is " + user_id + " the emial is " + email);
        return email;

    } 
    
    public String getmailaddresses() {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String[] email = null;
        String mailaddresses = "";
        ArrayList list = new ArrayList();
        String FINDER_QUERY = "SELECT email from am_gb_user WHERE is_helpdesk_manager = 'Y' ";
        try {
            con = getConnection("helpDesk");
            ps = con.prepareStatement(FINDER_QUERY);
            rs = ps.executeQuery();

            while (rs.next()) {
                list.add(rs.getString(1));
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch email->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        } 
        email = new String[list.size()];
        for(int x=0; x<list.size(); x++){
        	//email[x] = (String)list.get(x)+";";
        	mailaddresses = mailaddresses+(String)list.get(x)+";";
        }
        
        System.out.println(">>>>>The emials are: " + mailaddresses);
        return mailaddresses;

    }

    public void triggerQuery() {
        String query1A =
                " IF OBJECT_ID ('Legend_Security1','TR') IS NOT NULL "
                + " DROP TRIGGER Legend_Security1  ";

        String query1B =
                " CREATE TRIGGER Legend_Security1 "
                + " ON AM_ASSET_ARCHIVE with ENCRYPTION "
                + " AFTER INSERT, UPDATE, DELETE  "
                + " AS RAISERROR ('You Can not Modify the record', 16, 10) ";

        String query2A =
                " IF OBJECT_ID ('Legend_Security2','TR') IS NOT NULL "
                + " DROP TRIGGER Legend_Security2   ";

        String query2B =
                " CREATE TRIGGER Legend_Security2 "
                + " ON am_gb_User with ENCRYPTION "
                + " AFTER INSERT, UPDATE, DELETE  "
                + " AS RAISERROR ('You Can not Modify the record', 16, 10) ";

        String query3A =
                " IF OBJECT_ID ('Legend_Security3','TR') IS NOT NULL "
                + " DROP TRIGGER Legend_Security3   ";
        String query3B =
                " CREATE TRIGGER Legend_Security3 "
                + " ON am_ad_update_audit with ENCRYPTION "
                + " AFTER INSERT, UPDATE, DELETE  "
                + " AS RAISERROR ('You Can not Modify the record', 16, 10) ";
        String query4A =
                " IF OBJECT_ID ('Legend_Security4','TR') IS NOT NULL "
                + " DROP TRIGGER Legend_Security4   ";

        String query4B =
                " CREATE TRIGGER Legend_Security4 "
                + " ON am_ad_privileges with ENCRYPTION "
                + " AFTER INSERT, UPDATE, DELETE  "
                + " AS RAISERROR ('You Can not Modify the record', 16, 10) ";
        String query5A =
                " IF OBJECT_ID ('Legend_Security5','TR') IS NOT NULL "
                + " DROP TRIGGER Legend_Security5   ";

        String query5B =
                " CREATE TRIGGER Legend_Security5 "
                + " ON am_AssetDisposal with ENCRYPTION "
                + " AFTER INSERT, UPDATE, DELETE  "
                + " AS RAISERROR ('You Can not Modify the record', 16, 10) ";
        String query6A =
                " IF OBJECT_ID ('Legend_Security6','TR') IS NOT NULL "
                + " DROP TRIGGER Legend_Security6   ";
        String query6B =
                " CREATE TRIGGER Legend_Security6 "
                + " ON am_assetRevalue with ENCRYPTION "
                + " AFTER INSERT, UPDATE, DELETE  "
                + " AS RAISERROR ('You Can not Modify the record', 16, 10) ";

        String query7A =
                " IF OBJECT_ID ('Legend_Security7','TR') IS NOT NULL "
                + " DROP TRIGGER Legend_Security7   ";
        String query7B =
                " CREATE TRIGGER Legend_Security7 "
                + " ON am_assetTransfer with ENCRYPTION "
                + " AFTER INSERT, UPDATE, DELETE  "
                + " AS RAISERROR ('You Can not Modify the record', 16, 10) ";

        String query8A =
                " IF OBJECT_ID ('Legend_Security8','TR') IS NOT NULL "
                + " DROP TRIGGER Legend_Security8   ";

        String query8B =
                " CREATE TRIGGER Legend_Security8 "
                + " ON am_ad_category with ENCRYPTION "
                + " AFTER INSERT, UPDATE, DELETE  "
                + " AS RAISERROR ('You Can not Modify the record', 16, 10) ";
        String query9A =
                " IF OBJECT_ID ('Legend_Security9','TR') IS NOT NULL "
                + " DROP TRIGGER Legend_Security9   ";

        String query9B =
                " CREATE TRIGGER Legend_Security9 "
                + " ON am_gb_company with ENCRYPTION "
                + " AFTER INSERT, UPDATE, DELETE  "
                + " AS RAISERROR ('You Can not Modify the record', 16, 10) ";
        String query10A =
                " IF OBJECT_ID ('Legend_Security10','TR') IS NOT NULL "
                + " DROP TRIGGER Legend_Security10   ";

        String query10B =
                " CREATE TRIGGER Legend_Security10 "
                + " ON am_asset_approval with ENCRYPTION "
                + " AFTER INSERT, UPDATE, DELETE  "
                + " AS RAISERROR ('You Can not Modify the record', 16, 10) ";
        String query11A =
                " IF OBJECT_ID ('Legend_Security10A','TR') IS NOT NULL "
                + " DROP TRIGGER Legend_Security10A   ";
        String query11B =
                " CREATE TRIGGER Legend_Security10A "
                + " ON am_asset_approval_archive with ENCRYPTION "
                + " AFTER INSERT, UPDATE, DELETE  "
                + " AS RAISERROR ('You Can not Modify the record', 16, 10) ";

        String query12A =
                " IF OBJECT_ID ('Legend_Security11','TR') IS NOT NULL "
                + " DROP TRIGGER Legend_Security11   ";

        String query12B =
                " CREATE TRIGGER Legend_Security11 "
                + " ON am_assetReclassification with ENCRYPTION "
                + " AFTER INSERT, UPDATE, DELETE  "
                + " AS RAISERROR ('You Can not Modify the record', 16, 10) ";
        String query13A =
                " IF OBJECT_ID ('Legend_Security12','TR') IS NOT NULL "
                + " DROP TRIGGER Legend_Security12   ";

        String query13B =
                " CREATE TRIGGER Legend_Security12 "
                + " ON am_approval_remark with ENCRYPTION "
                + " AFTER INSERT, UPDATE, DELETE  "
                + " AS RAISERROR ('You Can not Modify the record', 16, 10) ";
        String query14A =
                " IF OBJECT_ID ('Legend_Security13','TR') IS NOT NULL "
                + " DROP TRIGGER Legend_Security13   ";
        String query14B =
                " CREATE TRIGGER Legend_Security13 "
                + " ON am_ad_class_privileges with ENCRYPTION "
                + " AFTER INSERT, UPDATE, DELETE  "
                + " AS RAISERROR ('You Can not Modify the record', 16, 10)   ";

        String queryA = "";
        String queryB = "";

        int counter = 1;
        for (counter = 1; counter <= 2; counter++) {
            Connection con = null;

            PreparedStatement ps = null;
            PreparedStatement ps2 = null;


            queryA = "query" + String.valueOf(counter) + "A";
            queryB = "query" + String.valueOf(counter) + "B";

            try {
                con = getConnection("helpDesk");

                ps = con.prepareStatement(triggerQuery(queryA));
                ps2 = con.prepareStatement(triggerQuery(queryB));


                ps.addBatch();
                ps2.addBatch();


                ps.executeBatch();
                ps2.executeBatch();


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeConnection(con, ps);
                closeConnection(con, ps2);

            }

        }
    }

    public String triggerQuery(String inQuery) {
        String outQuery = "";

        if (inQuery.equalsIgnoreCase("query1A")) {
            outQuery =
                    " IF OBJECT_ID ('Legend_Security1','TR') IS NOT NULL "
                    + " DROP TRIGGER Legend_Security1   ";
        }

        if (inQuery.equalsIgnoreCase("query1B")) {
            outQuery =
                    " CREATE TRIGGER Legend_Security1 "
                    + " ON am_ad_privileges with ENCRYPTION "
                    + " AFTER INSERT, UPDATE, DELETE  "
                    + " AS RAISERROR ('You Can not Modify the record', 16, 10) ";
        }


        if (inQuery.equalsIgnoreCase("query2A")) {
            outQuery =
                    " IF OBJECT_ID ('Legend_Security2','TR') IS NOT NULL "
                    + " DROP TRIGGER Legend_Security2   ";
        }

        if (inQuery.equalsIgnoreCase("query2B")) {
            outQuery =
                    " CREATE TRIGGER Legend_Security2 "
                    + " ON am_gb_User with ENCRYPTION "
                    + " AFTER INSERT, UPDATE, DELETE  "
                    + " AS RAISERROR ('You Can not Modify the record', 16, 10) ";
        }


        return outQuery;
    }

    public String userToEmail(String assetId) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String user_id = "";

        String FINDER_QUERY = "SELECT user_Id from am_asset WHERE asset_Id = ? ";

        try {
            con = getConnection("helpDesk");
            ps = con.prepareStatement(FINDER_QUERY);
            ps.setString(1, assetId);

            rs = ps.executeQuery();

            while (rs.next()) {
                user_id = rs.getString("user_Id");
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch user id ->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return user_id;

    }

    public String userToEmailTransInitiator(int tranId) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String user_id = "";

        String FINDER_QUERY = "SELECT user_Id from am_asset_approval WHERE transaction_Id = ? ";

        try {
            con = getConnection("helpDesk");
            ps = con.prepareStatement(FINDER_QUERY);
            ps.setInt(1, tranId);

            rs = ps.executeQuery();

            while (rs.next()) {
                user_id = rs.getString("user_Id");
            }
          //  System.out.println("\n\n the user id is" + user_id);


        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch user id ->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return user_id;

    }

    public String retrieveArray(String query) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String apprvLevel = "";
        String apprvLevelLimit = "";
        String image = "";
        try {
            con = getConnection("helpDesk");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                apprvLevel = rs.getString(1);
                apprvLevelLimit = rs.getString(2);
                image = rs.getString(3);
            }
        } catch (Exception ee) {
            System.out.println("WARN:ApprovalRecords.retrieveArray:->" + ee);
        } finally {
            closeConnection(con, ps, rs);
        }
        return (apprvLevel + ":" + apprvLevelLimit + ":" + image);
    }
    public boolean deleteBlob(String sessionId, String userId, String fileName) {
        boolean done = true;
        Connection conn = null;
        try { 
            conn = getConnection("helpDesk");
            if (!fileName.equals("")) { 
                PreparedStatement ps = conn.prepareStatement("Delete from am_ad_image where sessionId = '"+sessionId+"' and id = '"+userId+"'");
                ps.execute();
                ps.close();
 
            }
            conn.close();
        } catch (Exception e) {
            System.out.println(">> Error occued in deleteBlob method of ApprovalRecords " + e);
            done = false;
        }
        return done;
    }
   
       

    public boolean insertBlob(String sessionId, String userId, String fileName,String pageName, String fileType, String filename) {
        boolean done = true;
        Connection conn = null;
        try { 
        	//System.out.println("About to Insert"+sessionId);
            conn = getConnection("helpDesk");
            if (!fileName.equals("")) {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO am_ad_image VALUES(?,?,?,?,?,?,?,?)");
                ps.setString(1, userId);
                ps.setString(2, sessionId);
                ps.setString(3, userId);
                ps.setDate(4, dateConvert(new java.util.Date()));
                FileInputStream fis = new FileInputStream(fileName);
                ps.setBinaryStream(5, fis, fis.available());
                ps.setString(6, pageName);
                ps.setString(7, fileType);
                ps.setString(8, filename);
                ps.execute();
                ps.close();

            }
            conn.close();
        } catch (Exception e) {
            System.out.println(">> Error occued in insertBlob method of ApprovalRecords " + e);
            done = false;
        }
        return done;
    }

    public boolean setIncidentImage(Blob incBlob, String complaintId) {

        Connection con = null;
        PreparedStatement ps = null;
        String setBlobQuery = "UPDATE HD_COMPLAINT SET image = ? WHERE complaint_id = ?  ";
        boolean doneUpdate = false;

        try {
            con = getConnection("helpDesk");
            ps = con.prepareStatement(setBlobQuery);

            ps.setBlob(1, incBlob);
            ps.setString(2, complaintId);
            doneUpdate = ps.executeUpdate() == -1;


        } catch (Exception ex) {
            System.out.println("WARNING: cannot update setIncidentImage+" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return doneUpdate;
    }

/*
    public boolean setProblemImage(Blob incBlob, String complaintId) {

        Connection con = null;
        PreparedStatement ps = null;
        String setBlobQuery = "UPDATE HD_PROBLEM SET image = ? WHERE complaint_id = ?  ";
        boolean doneUpdate = false;

        try {
            con = getConnection("helpDesk");
            ps = con.prepareStatement(setBlobQuery);

            ps.setBlob(1, incBlob);
            ps.setString(2, complaintId);
            doneUpdate = ps.executeUpdate() == -1;


        } catch (Exception ex) {
            System.out.println("WARNING: cannot update setProblemImage+" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return doneUpdate;
    }*/
    public boolean setProblemImage(String Session, String userId, String complaintId, String pageName) {

        Connection con = null;
        PreparedStatement ps = null;
        String setBlobQuery = "UPDATE am_ad_image SET Id = ?, PageName = ?  where sessionId = '"+Session+"' and id = '"+userId+"' ";
        boolean doneUpdate = false;

        try {
            con = getConnection("helpDesk");
            ps = con.prepareStatement(setBlobQuery);

            ps.setString(1, complaintId);
            ps.setString(2, pageName);
            doneUpdate = ps.executeUpdate() == -1;


        } catch (Exception ex) {
            System.out.println("WARNING: cannot update setProblemImage+" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return doneUpdate;
    }



    public boolean setChangeImage(Blob incBlob, String ChangeId) {

        Connection con = null;
        PreparedStatement ps = null;
        String setBlobQuery = "UPDATE HD_CHANGE SET image = ? WHERE Change_id = ?  ";
        boolean doneUpdate = false;

        try {
            con = getConnection("helpDesk");
            ps = con.prepareStatement(setBlobQuery);

            ps.setBlob(1, incBlob);
            ps.setString(2, ChangeId);
            doneUpdate = ps.executeUpdate() == -1;


        } catch (Exception ex) {
            System.out.println("WARNING: cannot update setChangeImage+" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return doneUpdate;
    }

    
    public Blob getChangeImage(String sessionId) {

        Connection con = null;
        PreparedStatement ps = null;
        String getBlobQuery = "select image from am_ad_image where sessionId = ?";
        Blob incBlob = null;
        ResultSet rs = null;
        try {
            con = getConnection("helpDesk");
            ps = con.prepareStatement(getBlobQuery);
            ps.setString(1, sessionId);
            rs = ps.executeQuery();

            while (rs.next()) {
                incBlob = rs.getBlob("image");
            }


        } catch (Exception ex) {
            System.out.println("WARNING: cannot get image getChangeImage+" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return incBlob;
    }

    public Blob getIncidentImage(String sessionId) {

        Connection con = null;
        PreparedStatement ps = null;
        String getBlobQuery = "select image from am_ad_image where sessionId = ?";
        Blob incBlob = null;
        ResultSet rs = null;
        try {
            con = getConnection("helpDesk");
            ps = con.prepareStatement(getBlobQuery);
            ps.setString(1, sessionId);
            rs = ps.executeQuery();

            while (rs.next()) {
                incBlob = rs.getBlob("image");
            }


        } catch (Exception ex) {
            System.out.println("WARNING: cannot get image getIncidentImage+" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return incBlob;
    }
    public Blob getProblemImage(String sessionId) {

        Connection con = null;
        PreparedStatement ps = null;
        String getBlobQuery = "select image from am_ad_image where sessionId = ?";
        Blob incBlob = null;
        ResultSet rs = null;
        try {
            con = getConnection("helpDesk");
            ps = con.prepareStatement(getBlobQuery);
            ps.setString(1, sessionId);
            rs = ps.executeQuery();

            while (rs.next()) {
                incBlob = rs.getBlob("image");
            }


        } catch (Exception ex) {
            System.out.println("WARNING: cannot get image getProblemImage+" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return incBlob;
    }
    public Blob getGeneratedId3(String sessionId) {

        Connection con = null;
        PreparedStatement ps = null;
        String getBlobQuery = "select image from am_ad_image where sessionId = ?";
        Blob incBlob = null;
        ResultSet rs = null;
        try {
            con = getConnection("helpDesk");
            ps = con.prepareStatement(getBlobQuery);
            ps.setString(1, sessionId);
            rs = ps.executeQuery();

            while (rs.next()) {
                incBlob = rs.getBlob("image");
            }


        } catch (Exception ex) {
            System.out.println("WARNING: cannot get image getProblemImage+" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return incBlob;
    }

    
    public String getImageFileType(String reqnId,String pageName) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String fileType = "";

        String FINDER_QUERY = "SELECT File_Type from am_ad_image WHERE Id = ? ";

        try {
            con = getConnection("helpDesk");
            ps = con.prepareStatement(FINDER_QUERY);
            ps.setString(1, reqnId);

            rs = ps.executeQuery();

            while (rs.next()) {
                fileType = rs.getString("File_Type");
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch File Type->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
      //  System.out.println(">>>>>The reqn id is " + reqnId + " the File Type is " + fileType);
        return fileType;

    }
    
    public String getResponse(String reqnId) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String response = "";

        String FINDER_QUERY = "SELECT response from HD_RESPONSE WHERE complaint_id = ? ";

        try {
            con = getConnection("helpDesk");
            ps = con.prepareStatement(FINDER_QUERY);
            ps.setString(1, reqnId);

            rs = ps.executeQuery();

            while (rs.next()) {
            	response = rs.getString("response");
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch Response->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
     //   System.out.println(">>>>>The reqn id is " + reqnId + " the File Type is " + response);
        return response;

    }

    public boolean RecordAvailable(String FINDER_QUERY) {
        boolean done = false;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;  
  try {
            con = getConnection("helpDesk");
            ps = con.prepareStatement(FINDER_QUERY);
            rs = ps.executeQuery();

            while (rs.next()) {
                done = true;
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch helpDesk in RecordAvailable ->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return done;

    }

    
    
}
