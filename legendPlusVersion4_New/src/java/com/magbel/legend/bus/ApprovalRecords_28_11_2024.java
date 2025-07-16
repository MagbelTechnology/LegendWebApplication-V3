package com.magbel.legend.bus;

import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.legend.vao.Approval;
import com.magbel.util.CurrencyNumberformat;
import com.magbel.util.DatetimeFormat;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import legend.admin.handlers.CompanyHandler;
import magma.AssetRecordsBean;
import magma.asset.manager.AssetManager;
import magma.net.dao.MagmaDBConnection;
import magma.util.Codes;

// Referenced classes of package com.magbel.legend.bus:
//            ApprovalManager

public class ApprovalRecords_28_11_2024 extends MagmaDBConnection
{

    private CurrencyNumberformat formata;
    private SimpleDateFormat sdf;
    private DatetimeFormat df;
    ArrayList Alist;
    private String qryCheck;
    private ApprovalManager appManager;
    private MagmaDBConnection dbConnection;
    AssetManager asset_manager;
    CompanyHandler comp;
  
    public ApprovalRecords_28_11_2024()
    {
        Alist = new ArrayList();
        qryCheck = "";
        asset_manager = new AssetManager();
        comp = new CompanyHandler();
        dbConnection = new MagmaDBConnection();
    }

    public ArrayList findApproval(String assetId, String filter)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        ArrayList collection;
        String FINDER_QUERY;
        con = null;
        ps = null;
        rs = null;
        Approval app = null;
        collection = new ArrayList();
        FINDER_QUERY = (new StringBuilder("SELECT  Id,Description,Page,Flag,partPay,UserId,Branch,subjectToVat,whTax,operat" +
"ion1,exitPage,url from am_raisentry_post WHERE ID = ? AND FLAG=?"
)).append(filter).toString();
        try
        {
            con = getConnection("legendPlus");
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
    
    public ArrayList findApproval(String filter)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        ArrayList collection;
        String FINDER_QUERY;
        con = null;
        ps = null;
        rs = null;
        Approval app = null;
        collection = new ArrayList();
        FINDER_QUERY = (new StringBuilder("SELECT Id,Description,Page,Flag,partPay,UserId,Branch,subjectToVat,whTax,operati" +
"on1,exitPage,url from am_raisentry_post WHERE  FLAG=? "
)).append(filter).toString();
        try
        {
            con = getConnection("legendPlus");
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
            }
        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch [am_raisentry_post]->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return collection;

    }

    public boolean isApprovalExisting(String assetId, String page)
    {
        boolean done;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        String FINDER_QUERY;
        done = false;
        con = null;
        ps = null;
        rs = null;
        FINDER_QUERY = "SELECT Id,Description,Page,Flag,partPay from am_raisentry_post WHERE ID = ? and " +
"page=?"
;
        try
        {
            con = getConnection("legendPlus");
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

    public boolean isApprovalExisting(String assetId)
    {
        boolean done;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        String FINDER_QUERY;
        done = false;
        con = null;
        ps = null;
        rs = null;
        FINDER_QUERY = "SELECT Id,Description,Page,Flag,partPay from am_raisentry_post WHERE ID = ?";
        try
        {
            con = getConnection("legendPlus");
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

    public void updateApproval(String assetid)
    {
        Connection con;
        PreparedStatement ps;
        String NOTIFY_QUERY;
        con = null;
        ps = null;
        NOTIFY_QUERY = "UPDATE am_raisentry_post SET flag = ? WHERE ID = ?  ";
        try
        {
            con = getConnection("legendPlus");
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

    public void updateRaiseEntry(String assetid)
    {
        Connection con;
        PreparedStatement ps;
        String NOTIFY_QUERY;
        con = null;
        ps = null;
        NOTIFY_QUERY = "UPDATE am_asset SET raise_entry = ? WHERE ASSET_ID = ?  ";
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(NOTIFY_QUERY);
            ps.setString(1, "Y");
            ps.setString(2, assetid);
            ps.executeUpdate();

        } catch (Exception ex) {
            System.out.println("WARNING: cannot update am_asset+" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
    }

    public void updateRaiseAsseg2Entry(String assetid)
    {
        Connection con;
        PreparedStatement ps;
        String NOTIFY_QUERY;
        con = null;
        ps = null;
        NOTIFY_QUERY = "UPDATE am_asset2 SET raise_entry = ? WHERE ASSET_ID = ?  ";
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(NOTIFY_QUERY);
            ps.setString(1, "Y");
            ps.setString(2, assetid);
            ps.executeUpdate();

        } catch (Exception ex) {
            System.out.println("WARNING: cannot update am_asset+" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
    }

    public boolean insertApproval(String id, String description, String page, String flag, String partPay, String UserId, String Branch, 
            String subjectToVat, String whTax, String url)
    {
        boolean done;
        Connection con;
        PreparedStatement ps;
        String query;
        done = true;
        flag = "Y";
        con = null;
        ps = null;
        query = "INSERT INTO [am_raisentry_post](Id,Description,Page,Flag,partPay,UserId,Branch,s" +
"ubjectToVat,whTax,url) VALUES(?,?,?,?,?,?,?,?,?,?)"
;
        try
        {
            con = getConnection("legendPlus");
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

    public String getCodeName(String query)
    {
        String result;
        Connection con;
        PreparedStatement ps;
        result = "";
        con = null;
        ResultSet rs = null;
        ps = null;
        try 
        { 
//        	System.out.println("====getCodeName query=====  "+query);
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);

            rs = ps.executeQuery();

  //          System.out.println("====getCodeName query=====  "+query);
            while (rs.next()) {
                result = rs.getString(1) == null ? "" : rs.getString(1);

            }
        } catch (Exception er) {
            System.out.println("Error in Query- getCodeName()... ->" + er);
            er.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
  //      System.out.println("====getCodeName result=====  "+result);
        return result;
    }

    public String getCodeName(String query,String param)
    {
        String result;
        Connection con;
        PreparedStatement ps;
        result = "";
        con = null;
        ResultSet rs = null;
        ps = null;
        try
        {      
//        	System.out.println("====getCodeName query=====  "+query); 
//            con = getConnection("legendPlus");
//            ps = con.prepareStatement(query);
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);    
            	  ps.setString(1, param);
            rs = ps.executeQuery();

//            System.out.println("====getCodeName param=====  "+param);
            while (rs.next()) {
                result = rs.getString(1) == null ? "" : rs.getString(1);

            }
        } catch (Exception er) {
            System.out.println("Error in Query- getCodeName()... ->" + er);
            er.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
  //      System.out.println("====getCodeName result=====  "+result);
        return result;
    }

    public String getCodeName(String query,String param1,String param2,String param3)
    {
        String result;
        Connection con;
        PreparedStatement ps;
        result = "";
        con = null;
        ResultSet rs = null;
        ps = null;
        try
        { 
            con = getConnection("legendPlus");
          ps = con.prepareStatement(query);    
       	  ps.setString(1, param1);
    	  ps.setString(2, param2);
    	  ps.setString(3, param3);
          rs = ps.executeQuery();

  //          System.out.println("====getCodeName query=====  "+query);
            while (rs.next()) {
                result = rs.getString(1) == null ? "" : rs.getString(1);

            }
        } catch (Exception er) {
            System.out.println("Error in Query- getCodeName()... ->" + er);
            er.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
  //      System.out.println("====getCodeName result=====  "+result);
        return result;
    }

    public String getCodeName(String query,String param1,String param2)
    {
        String result;
        Connection con;
        PreparedStatement ps;
        result = "";
        con = null;
        ResultSet rs = null;
        ps = null;
        try
        { 
            con = getConnection("legendPlus");
          ps = con.prepareStatement(query);    
       	  ps.setString(1, param1);
    	  ps.setString(2, param2);
          rs = ps.executeQuery();

  //          System.out.println("====getCodeName query=====  "+query);
            while (rs.next()) {
                result = rs.getString(1) == null ? "" : rs.getString(1);

            }
        } catch (Exception er) {
            System.out.println("Error in Query- getCodeName()... ->" + er);
            er.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
  //      System.out.println("====getCodeName result=====  "+result);
        return result;
    }


    public double getValue(String query)
    {
        double result;
        Connection con;
        PreparedStatement ps;
        result = 0.0D;
        con = null;
        ResultSet rs = null;
        ps = null;
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();


            while (rs.next()) {
                result = rs.getDouble(1);

            }
        } catch (Exception er) {
            System.out.println("Error in Query- getValue()... ->" + er);
            er.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
        return result;
    }

    public boolean insertBlob(String assetId, String userId, String pageName, String fileName)
    {
        boolean done = true;
        Connection conn = null;
        try
        {
            conn = getConnection("legendPlus");
            if(!fileName.equals(""))
            {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO am_ad_image VALUES(?,?,?,?,?)");
                ps.setString(1, assetId);
                ps.setString(2, userId);
                ps.setDate(3, dateConvert(new Date()));
                FileInputStream fis = new FileInputStream(fileName);
                ps.setBinaryStream(4, fis, fis.available());
                ps.setString(5, pageName);
                ps.execute();
                ps.close();
            }
            conn.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            done = false;
        }
        return done;
    }

    public boolean isImageExisting(String assetId, String pageName)
    {
        boolean done;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        String FINDER_QUERY;
        done = false;
        con = null;
        ps = null;
        rs = null;
        FINDER_QUERY = "select image from am_ad_image where assetId=? and pageName=?";
        System.out.println("");
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);
            ps.setString(1, assetId);
            ps.setString(2, pageName);
            rs = ps.executeQuery();

            while (rs.next()) {
                done = true;
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch [am_ad_image]->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return done;
    }

    public byte[] retrieveImageData(String pathname)
    {
        ArrayList imageArray = new ArrayList();
        byte imageData[] = null;
        File file = new File(pathname);
        BufferedInputStream bis;
        try
        {
            bis = new BufferedInputStream(new FileInputStream(file));
        }
        catch(FileNotFoundException fe)
        {
            System.out.println("INFO: <<CheckImageDAO:retrieveImageData>>\n Image file does not Exist.");
            return null;
        }
        try
        {
            int data;
            while((data = bis.read()) != -1) 
            {
                imageArray.add(new Byte((byte)data));
            }
            imageData = new byte[imageArray.size()];
            for(int i = 0; i < imageData.length; i++)
            {
                imageData[i] = ((Byte)imageArray.get(i)).byteValue();
            }

        }
        catch(Exception ioe)
        {
            System.out.println("INFORMATION, <<CheckImageDAO:retrieveImageData>> Could not retrieve image.");
            System.out.println(ioe);
        }
        return imageData;
    }

    public void getImageData(String assetId, String pageName)
    {
        Connection conn = null;
        conn = getConnection("legendPlus");
        try
        {
            String query = (new StringBuilder("select image from am_ad_image where assetId='")).append(assetId).append("' and pageName='").append(pageName).append("'").toString();
            Statement state = conn.createStatement();
            ResultSet rs = state.executeQuery(query);
            if(rs.next())
            {
                byte fileBytes[] = rs.getBytes(1);
                OutputStream targetFile = new FileOutputStream("d://filepath//new.JPG");
                targetFile.write(fileBytes);
                targetFile.close();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public String[] raiseEntryInfo(String asset_id)
    {
        String result[];
        Connection con;
        PreparedStatement ps;
        String query;
        result = new String[4];
        con = null;
        ResultSet rs = null;
        ps = null;
        query = (new StringBuilder("select description, branch_id, subject_to_vat, wh_tax from am_asset where asset_" +
"id = '"
)).append(asset_id).append(" '").toString();
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();


            while (rs.next()) {
                result[0] = rs.getString(1) == null ? "" : rs.getString(1);
                result[1] = rs.getString(2) == null ? "" : rs.getString(2);
                result[2] = rs.getString(3) == null ? "" : rs.getString(3);
                result[3] = rs.getString(4) == null ? "" : rs.getString(4);


            }
        } catch (Exception er) {
            System.out.println("Error in Query- raiseEntryInfo() in class ApprovalRecords... ->" + er);
            er.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
        return result;
    }

    public String[] UncapitalizedraiseEntryInfo(String asset_id)
    {
        String result[];
        Connection con;
        PreparedStatement ps;
        String query;
        result = new String[4];
        con = null;
        ResultSet rs = null;
        ps = null;
        query = (new StringBuilder("select description, branch_id, subject_to_vat, wh_tax from AM_ASSET_UNCAPITALIZE" +
"D where asset_id = '"
)).append(asset_id).append(" '").toString();
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();


            while (rs.next()) {
                result[0] = rs.getString(1) == null ? "" : rs.getString(1);
                result[1] = rs.getString(2) == null ? "" : rs.getString(2);
                result[2] = rs.getString(3) == null ? "" : rs.getString(3);
                result[3] = rs.getString(4) == null ? "" : rs.getString(4);


            }
        } catch (Exception er) {
            System.out.println("Error in Query- UncapitalizedraiseEntryInfo() in class ApprovalRecords... ->" + er);
            er.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
        return result;
    }

    public String userToEmail(String assetId)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        String user_id;
        String FINDER_QUERY;
        con = null;
        ps = null;
        rs = null;
        user_id = "";
        FINDER_QUERY = "SELECT user_Id from am_asset_approval WHERE asset_Id = ? ";
        try
        {
            con = getConnection("legendPlus");
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

    public String userEmail(String user_id)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        String email;
        String FINDER_QUERY;
        con = null;
        ps = null;
        rs = null;
        email = "";
        FINDER_QUERY = "SELECT email from am_gb_user WHERE user_id = ? ";
        try
        { 
            con = getConnection("legendPlus");
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
        System.out.println(">>>>>The user id is " + user_id + " the emial is " + email);
        return email;
    }

    public boolean deleteRaiseEntry(String assetid, String page1)
    {
        Connection con;
        PreparedStatement ps;
        String NOTIFY_QUERY;
        con = null;
        ps = null;
        NOTIFY_QUERY = "delete from  am_raisentry_post  WHERE ID = ? and page=? ";
        boolean done;
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(NOTIFY_QUERY);
            ps.setString(1, assetid);
            ps.setString(2, page1);
            ps.executeUpdate();
            done = true;
        } catch (Exception ex) {
            done = false;
            System.out.println("WARNING: cannot delete am_raisentry_post+" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;
    }

    public void updateRaiseEntry(String assetid, String status)
    {
        Connection con;
        PreparedStatement ps;
        String NOTIFY_QUERY;
        con = null;
        ps = null;
        NOTIFY_QUERY = "UPDATE am_asset SET raise_entry = ? WHERE ASSET_ID = ?  ";
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(NOTIFY_QUERY);
            ps.setString(1, status);
            ps.setString(2, assetid);
            ps.executeUpdate();

        } catch (Exception ex) {
            System.out.println("WARNING: cannot update am_asset+" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
    }

    public void updateAssetStatus(String assetid, String status)
    {
        Connection con;
        PreparedStatement ps;
        String NOTIFY_QUERY;
        con = null;
        ps = null;
        NOTIFY_QUERY = " update am_asset_approval set process_status=?,approval_level_count=?,approval1=" +
"?,approval2=?,approval3=?,approval4=?,approval5=? where asset_id=? "
;
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(NOTIFY_QUERY);
            ps.setString(1, status);
            ps.setInt(2, 0);
            ps.setInt(3, 0);
            ps.setInt(4, 0);
            ps.setInt(5, 0);
            ps.setInt(6, 0);
            ps.setInt(7, 0);
            ps.setString(8, assetid);
            ps.executeUpdate();

        } catch (Exception ex) {
            System.out.println("WARNING: cannot update am_asset_approval+" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
    }

    public ArrayList findApprovalInitiator_old(String filter)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        ArrayList collection;
        String FINDER_QUERY;
        con = null;
        ps = null;
        rs = null;
        Approval app = null;
        collection = new ArrayList();
        FINDER_QUERY = (new StringBuilder("select Id,Description,Page,Flag,partPay,UserId,Branch,subjectToVat,whTax,operati" +
"on1,exitPage,url,trans_id from am_raisentry_post where (id in (select asset_id f" +
"rom am_raisentry_transaction where iso <> '000') or id not in (select asset_id f" +
"rom am_raisentry_transaction)) "
)).append(filter).toString();
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);
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
                int tranID = rs.getInt("trans_id");
                app = new Approval(id, Description, Page, Flag, partPay, UserId, Branch, subjectToVat, whTax, operation1, exitPage, url);
                app.setAssetCreator(getTransactionInitiatorByTranID(tranID, UserId,id));
                collection.add(app);
            }
        } catch (Exception ex) {
            System.out.println("findApprovalInitiator(): WARNING: cannot fetch [ApprovalRecords]->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return collection;
    }

    public ArrayList findApprovalInitiator(String filter)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        ArrayList collection;
        String FINDER_QUERY;
        con = null;
        ps = null;
        rs = null;
        Approval app = null;
        collection = new ArrayList();
        FINDER_QUERY = (new StringBuilder("select Id,Description,Page,Flag,partPay,UserId,Branch,subjectToVat,whTax,operati" +
"on1,exitPage,url,trans_id from am_raisentry_post where (id in (select asset_id f" +
"rom am_raisentry_transaction where iso <> '000') or id not in (select asset_id f" +
"rom am_raisentry_transaction) or id in (select id from am_raisentry_post where G" +
"roupIdStatus = 'N')) "
)).append(filter).toString();
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);
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
                int tranID = rs.getInt("trans_id");
                app = new Approval(id, Description, Page, Flag, partPay, UserId, Branch, subjectToVat, whTax, operation1, exitPage, url);
                app.setAssetCreator(getTransactionInitiatorByTranID(tranID, UserId,id));
                collection.add(app);
            }
        } catch (Exception ex) {
            System.out.println("findApprovalInitiator(): WARNING: cannot fetch [ApprovalRecords]->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return collection;
    }

    public ArrayList findApprovalInitiator3(String Filter)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        ArrayList collection;
        String Finder_qry;
        con = null;
        ps = null;
        rs = null;
        Approval app = null;
        collection = new ArrayList();
        Finder_qry = "select Id,p.Description,Page,Flag,partPay,UserId,Branch, a.cost_price,a.branch_i" +
"d,subjectToVat,whTax,operation1,exitPage,url,trans_id from am_raisentry_post p, " +
"am_asset a  where (id in (select asset_id from am_raisentry_transaction where is" +
"o <> '000') or id not in (select asset_id from am_raisentry_transaction) or id i" +
"n (select id from am_raisentry_post where GroupIdStatus = 'N')) "
;
        Finder_qry = (new StringBuilder(String.valueOf(Finder_qry))).append(Filter).toString();
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(Finder_qry);
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
                int tranID = rs.getInt("trans_id");
                app = new Approval(id, Description, Page, Flag, partPay, UserId, Branch, subjectToVat, whTax, operation1, exitPage, url);
                app.setAssetCreator(getTransactionInitiatorByTranID(tranID, UserId,id));
                collection.add(app);
            }

        } catch (Exception ex) {
            System.out.println("findApprovalInitiator3(): WARNING: cannot fetch [ApprovalRecords]->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return collection;
    }

    public ArrayList findApprovalInitiator4(String Filter)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        ArrayList collection;
        String Finder_qry;
        con = null;
        ps = null;
        rs = null;
        Approval app = null;
        collection = new ArrayList();
        Finder_qry = " select distinct Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,br" +
"anch_id,subjectToVat,whTax,operation1,exitPage,url,trans_id  from am_raisentry_p" +
"ost p, dbo.am_group_asset_main m  where GroupIdStatus = 'N' and  id in ( select " +
"convert(varchar ,group_id) from dbo.am_group_asset_main) and id = convert(varcha" +
"r ,group_id)UNION ALL  select distinct Id,p.Description,Page,Flag,partPay,UserId" +
",Branch, cost_price,branch_id, subjectToVat,whTax,operation1,exitPage,url,trans_" +
"id  from am_raisentry_post p, am_asset a  where id in (select asset_id from am_r" +
"aisentry_transaction where iso <> '000')  AND ID = ASSET_ID  UNION ALL  select d" +
"istinct Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, " +
"subjectToVat,whTax,operation1,exitPage,url,trans_id  from am_raisentry_post p, a" +
"m_asset a  where  id not in (select asset_id from am_raisentry_transaction)  AND" +
" ID = ASSET_ID  UNION ALL  select distinct Id,p.Description,Page,Flag,partPay,Us" +
"erId,Branch, cost_price,branch_id, subjectToVat,whTax,operation1,exitPage,url,tr" +
"ans_id   from am_raisentry_post p, am_asset a   where ID = a.OLD_ASSET_ID  and a" +
".asset_id not in (select asset_id from am_raisentry_transaction) "
;
        Finder_qry = (new StringBuilder(String.valueOf(Finder_qry))).append(Filter).toString();
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(Finder_qry);
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
                int tranID = rs.getInt("trans_id");
                app = new Approval(id, Description, Page, Flag, partPay, UserId, Branch, subjectToVat, whTax, operation1, exitPage, url);
                app.setAssetCreator(getTransactionInitiatorByTranID(tranID, UserId,id));
                collection.add(app);
            }
        } catch (Exception ex) {
            System.out.println("findApprovalInitiator4(): WARNING: cannot fetch [ApprovalRecords]->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return collection;
    }

    public ArrayList findApprovalInitiator2(String filter)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        ArrayList collection;
        String FINDER_QUERY;
        con = null;
        ps = null;
        rs = null;
        Approval app = null;
        collection = new ArrayList();
        FINDER_QUERY = (new StringBuilder("select Id,Description,Page,Flag,partPay,UserId,Branch,subjectToVat,whTax,operati" +
"on1,exitPage,url,trans_id from am_raisentry_post where  entrypostflag != 'Y' "
)).append(filter).toString();
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);
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
                int tranID = rs.getInt("trans_id");
                app = new Approval(id, Description, Page, Flag, partPay, UserId, Branch, subjectToVat, whTax, operation1, exitPage, url);
                app.setAssetCreator(getTransactionInitiatorByTranID(tranID, UserId,id));
                collection.add(app);
            }

        } catch (Exception ex) {
            System.out.println("findApprovalInitiator2(): WARNING: cannot fetch [ApprovalRecords]->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return collection;
    }
    
    public String getTransactionInitiatorByTranID(int tranIDs, String initiatorID, String Id) {
        //System.out.println("tranIDs >>>>>>> " + tranIDs);
        // System.out.println("initiatorID >>>>>>> " + initiatorID);
        String userID = "";
        int user_id = 0;
    //    System.out.println("ApprovalRecords: tranIDs assetCreator-> "+tranIDs);
        if (tranIDs != 0) {
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            String FINDER_QUERY = "SELECT user_id from am_asset_approval WHERE transaction_id = ? and Asset_Id = ?";
            try {
                con = getConnection("legendPlus");
                ps = con.prepareStatement(FINDER_QUERY);
                ps.setInt(1, tranIDs);
                ps.setString(2, Id);
  //              System.out.println("ApprovalRecords:Inside loop-> ");
                rs = ps.executeQuery();

                while (rs.next()) {
                    //userID = Integer.toString(rs.getInt("user_id"));
                    user_id = rs.getInt("user_id");
                    userID = rs.getString("user_Id");
                }
                String query = " SELECT full_name from am_gb_user where user_id=?";
                userID = getCodeName(query,userID);
 //               System.out.println("ApprovalRecords: Initiator assetCreator-> "+user_id);
            } catch (Exception ex) {
                System.out.println("ApprovalRecords: WARNING: cannot fetch assetCreator->"
                        + ex.getMessage());
            } finally {
                closeConnection(con, ps, rs);
            }
        } else {
            userID = initiatorID;

        }

        return userID;

    }    

    public boolean insertApproval(String id, String description, String page, String flag, String partPay, String UserId, String Branch, 
            String subjectToVat, String whTax, String url, int transID)
    {
        boolean done;
        Connection con;
        PreparedStatement ps;
        String query;
        done = true;
        flag = "Y";
        con = null;
        ps = null;
        query = "INSERT INTO [am_raisentry_post](Id,Description,Page,Flag,partPay,UserId,Branch,s" +
"ubjectToVat,whTax,url,trans_id) VALUES(?,?,?,?,?,?,?,?,?,?,?)"
;
        try
        {
            con = getConnection("legendPlus");
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
            ps.setInt(11, transID);
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

    public ArrayList findPostedEntry_old(String filter)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        ArrayList collection;
        String FINDER_QUERY;
        con = null;
        ps = null;
        rs = null;
        Approval app = null;
        collection = new ArrayList();
        FINDER_QUERY = (new StringBuilder("select Id,Description,Page,Flag,partPay,UserId,Branch,subjectToVat,whTax,operati" +
"on1,exitPage,url,trans_id from am_raisentry_post where(id in (select asset_id fr" +
"om am_raisentry_transaction where iso = '000'))"
)).append(filter).toString();
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);
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
                int tranID = rs.getInt("trans_id");
                app = new Approval(id, Description, Page, Flag, partPay, UserId, Branch, subjectToVat, whTax, operation1, exitPage, url);
                app.setAssetCreator(getTransactionInitiatorByTranID(tranID, UserId,id));
                collection.add(app);
        }

    } catch (Exception ex) {
        System.out.println("findPostedEntry_old(): WARNING: cannot fetch [ApprovalRecords]->"
                + ex.getMessage());
    } finally {
        closeConnection(con, ps, rs);
    }
        return collection;
    }

    public ArrayList findPostedEntry(String filter)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        ArrayList collection;
        String FINDER_QUERY;
        con = null;
        ps = null;
        rs = null;
        Approval app = null;
        collection = new ArrayList();
        FINDER_QUERY = (new StringBuilder("select Id,p.Description,Page,Flag,partPay,UserId,Branch,a.cost_price,a.branch_id" +
",subjectToVat,whTax,operation1,exitPage,url,trans_id from am_raisentry_post p, a" +
"m_asset a where(id in (select asset_id from am_raisentry_transaction where iso =" +
" '000' or id in (select id from am_raisentry_post where GroupIdStatus = 'Y'))) "
)).append(filter).toString();
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);
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
                int tranID = rs.getInt("trans_id");
                app = new Approval(id, Description, Page, Flag, partPay, UserId, Branch, subjectToVat, whTax, operation1, exitPage, url);
                app.setAssetCreator(getTransactionInitiatorByTranID(tranID, UserId,id));
                collection.add(app);
            }

        } catch (Exception ex) {
            System.out.println("findPostedEntry(): WARNING: cannot fetch [am_raisentry_post]->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return collection;
    }

    public ArrayList findPostedEntry2(String filter)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        ArrayList collection;
        String FINDER_QUERY;
        con = null;
        ps = null;
        rs = null;
        Approval app = null;
        collection = new ArrayList();
        FINDER_QUERY = (new StringBuilder("select Id,Description,Page,Flag,partPay,UserId,Branch,subjectToVat,whTax,operati" +
"on1,exitPage,url,trans_id from am_raisentry_post where  entrypostflag = 'Y'"
)).append(filter).toString();
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);

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
                int tranID = rs.getInt("trans_id");
                app = new Approval(id, Description, Page, Flag, partPay, UserId, Branch, subjectToVat, whTax, operation1, exitPage, url);
                app.setAssetCreator(getTransactionInitiatorByTranID(tranID, UserId,id));
                collection.add(app);
            }

        } catch (Exception ex) {
            System.out.println("findPostedEntry2(): WARNING: cannot fetch [am_raisentry_post]->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return collection;
    }

    public void updateRaiseEntryPost(String id, String page1, String transactionIdCost, String transactionIdVendor, String transactionIdWitholding)
    {
        String assetStatus;
        Connection con;
        PreparedStatement ps;
//        assetStatus = getCodeName((new StringBuilder("select asset_status from am_asset where asset_id='")).append(id).append("'").toString());
        String param = "select asset_status from am_asset where asset_id= ?";
        assetStatus = getCodeName(param,id);
        if(assetStatus == "")
        {
//        	assetStatus = getCodeName((new StringBuilder("select asset_status from am_asset_uncapitalized where asset_id='")).append(id).append("'").toString());
        	param = "select asset_status from am_asset_uncapitalized where asset_id= ?";
            assetStatus = getCodeName(param,id);
        }
        con = null;
        ps = null;
        String query = "";
        try
        {
            AssetRecordsBean arb = new AssetRecordsBean();
            if(transactionIdCost.equalsIgnoreCase("000") && transactionIdVendor.equalsIgnoreCase("000") && transactionIdWitholding.equalsIgnoreCase("000") && assetStatus.equalsIgnoreCase("APPROVED"))
            {
                query = "update am_raisentry_post set entrypostflag='Y' where id=? and page=? ";
                arb.updateNewAssetStatus(id);
                arb.updateUncapitalizedNewAssetStatus(id);
                appManager = new ApprovalManager();
                appManager.infoFromApproval(id, page1);
                sendMailAfterPosting(id);
                sendMailAfterPostingToBranch(id);
            } else
            {
                query = "update am_raisentry_post set entrypostflag='N' where id=? and page=? ";
                String del_am_invoice_no_qry = (new StringBuilder("Delete from am_invoice_no where asset_id='")).append(id).append("'").append(" and trans_type='Asset Creation'").toString();
                updateUtil(del_am_invoice_no_qry);
            }
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            ps.setString(1, id);
            ps.setString(2, page1);
            ps.execute();
        } catch (Exception ex) {

            System.out.println("WARNING:cannot update am_raisentry_post->");
            ex.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
    }

    public void NewAssetRaiseEntryPost(String id, String page1, String transactionIdCost, String transactionIdVendor, String transactionIdWitholding)
    {
        String assetStatus;
        Connection con;
        PreparedStatement ps;
        String param = "select asset_status from am_asset where asset_id= ?";
        assetStatus = getCodeName(param,id);
//        assetStatus = getCodeName((new StringBuilder("select asset_status from am_asset where asset_id='")).append(id).append("'").toString());
        if(assetStatus == "")
        {
            param = "select asset_status from am_asset_uncapitalized where asset_id= ?";
            assetStatus = getCodeName(param,id);
//            assetStatus = getCodeName((new StringBuilder("select asset_status from am_asset_uncapitalized where asset_id='")).append(id).append("'").toString());
        }
      //  System.out.println((new StringBuilder(">>>>>>>>>>> the status of asset is ")).append(assetStatus).append("  id: ").append(id).append("  page1: ").append(page1).toString());
        con = null;
        ps = null;
        String query = "";
    //    System.out.println((new StringBuilder(">>>>>>>>>>> transactionIdCost is ")).append(transactionIdCost).append("  transactionIdVendor: ").append(transactionIdVendor).append("  transactionIdWitholding: ").append(transactionIdWitholding).toString());
        try
        {
            AssetRecordsBean arb = new AssetRecordsBean();
            if(transactionIdCost.equalsIgnoreCase("000") && transactionIdVendor.equalsIgnoreCase("000") && transactionIdWitholding.equalsIgnoreCase("000") && assetStatus.equalsIgnoreCase("ACTIVE"))
            {
                query = "update am_raisentry_post set entrypostflag='Y' where id=? and page=? ";
//                System.out.println((new StringBuilder("here in >>>>>>>>>NewAssetRaiseEntryPost--id--")).append(id).toString());
                arb.updateNewAssetStatus(id);
                arb.updateUncapitalizedNewAssetStatus(id);
                appManager = new ApprovalManager();
                appManager.infoFromApproval(id, page1);
                sendMailAfterPosting(id);
                sendMailAfterPostingToBranch(id);
            } else
            {
                query = "update am_raisentry_post set entrypostflag='N' where id=? and page=? ";
                String del_am_invoice_no_qry = (new StringBuilder("Delete from am_invoice_no where asset_id='")).append(id).append("'").append(" and trans_type='Asset Creation'").toString();
                updateUtil(del_am_invoice_no_qry);
            }
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            ps.setString(1, id);
            ps.setString(2, page1);
            ps.execute();

        } catch (Exception ex) {

            System.out.println("WARNING:cannot update am_raisentry_post->");
            ex.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
    }

    public void updateRaiseEntryPost(String id, String page1, String transactionIdCost, String transactionIdAccum, String transactionIdDisposal, String transactionIdProfit, String dispopercent)
    {
        Connection con;
        Connection con1;
        PreparedStatement ps;
        PreparedStatement ps1;
        double Percentage;
        String check;
        con = null;
        con1 = null;
        ps = null;
        ps1 = null;
        String query = "";
        Percentage = Double.parseDouble(dispopercent);
        String param = "select disposal_status from am_assetDisposal where asset_id= ?";
        check = getCodeName(param,id);
//        check = getCodeName((new StringBuilder("select  disposal_status from am_assetDisposal  where Asset_id ='")).append(id).append("'").toString());
        try
        {
            if(transactionIdCost.equalsIgnoreCase("000") && transactionIdAccum.equalsIgnoreCase("000") && transactionIdDisposal.equalsIgnoreCase("000") && transactionIdProfit.equalsIgnoreCase("000"))
            {
                int result = updateUtil((new StringBuilder("update am_assetDisposal set email_sent ='Y', disposal_status='P' where asset_id=" +
"'"
)).append(id).append("'").toString());
                String query2 = (new StringBuilder("update am_asset set asset_status=?,date_disposed=?,Cost_Price = Cost_Price - Cos" +
"t_Price*"
)).append(Percentage).append("/100, Accum_Dep= Accum_Dep - Accum_Dep*").append(Percentage).append("/100, NBV = NBV - NBV*").append(Percentage).append("/100, Monthly_Dep = Monthly_Dep - Monthly_Dep*").append(Percentage).append("/100 where asset_id=?").toString();
                String query02 = "update am_asset set asset_status=?,date_disposed=? where asset_id=?";
                con = getConnection("legendPlus");
                if(Percentage < 100D)
                {
                    ps = con.prepareStatement(query2);
                    ps.setString(1, "ACTIVE");
                } else
                {
                    ps = con.prepareStatement(query02);
                    ps.setString(1, "Disposed");
                }
                ps.setDate(2, dateConvert(new Date()));
                ps.setString(3, id);
                ps.execute();
                appManager = new ApprovalManager();
                appManager.infoFromApproval(id, page1);
                query = (new StringBuilder("update am_raisentry_post set entrypostflag='Y' where id='")).append(id).append("' and page='").append(page1).append("' ").toString();
                int result3 = updateUtil(query);
                if(check.equalsIgnoreCase("PD"))
                {
                    query = "update am_raisentry_post set entrypostflag='N' where id=? and page=? ";
                    con1 = getConnection("legendPlus");
                    ps1 = con.prepareStatement(query);
                    ps.setString(1, id);
                    ps.setString(2, page1);
                    ps.execute();
                }
            }
        } catch (Exception ex) {

            System.out.println("WARNING:cannot update am_raisentry_post->");
            ex.printStackTrace();
        } finally {
            closeConnection(con, ps);
            closeConnection(con1, ps1);
        }

    }

    public void updateRaiseEntryPost(String id, String page1, String transactionIdCost, String transactionIdAccum)
    {
        Connection con;
        PreparedStatement ps;
        String query;
        String param = "select asset_status from am_asset where asset_id= ?";
        String assetStatus = getCodeName(param,id);
//        String assetStatus = getCodeName((new StringBuilder("select asset_status from am_asset where asset_id='")).append(id).append("'").toString());
        if(assetStatus == "")
        {
            param = "select asset_status from am_asset_uncapitalized where asset_id= ?";
            assetStatus = getCodeName(param,id);
//            assetStatus = getCodeName((new StringBuilder("select asset_status from am_asset_uncapitalized where asset_id='")).append(id).append("'").toString());
        }
        con = null;
        ps = null;
        query = "";
        try
        {
            AssetRecordsBean arb = new AssetRecordsBean();
            if(transactionIdCost.equalsIgnoreCase("000") && assetStatus.equalsIgnoreCase("APPROVED"))
            {
                query = "update am_raisentry_post set entrypostflag='Y' where id=? and page=? ";
                arb.updateNewAssetStatus(id);
                arb.updateUncapitalizedNewAssetStatus(id);
                appManager = new ApprovalManager();
                appManager.infoFromApproval(id, page1);
                sendMailAfterPosting(id);
            } else
            {
                query = "update am_raisentry_post set entrypostflag='N' where id=? and page=? ";
            }
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder("Error in updateRaiseEntryPost() ")).append(e).toString());
        }
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            ps.setString(1, id);
            ps.setString(2, page1);
            ps.execute();
        } catch (Exception ex) {

            System.out.println("WARNING:cannot update am_raisentry_post->");
            ex.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
    }

    public void updateRaiseEntryPost(String id, String page1, String transactionIdCost)
    {
        Connection con;
        PreparedStatement ps;
        String query;
        con = null;
        ps = null;
        query = "";
        if(transactionIdCost.equalsIgnoreCase("000"))
        {
            query = "update am_raisentry_post set entrypostflag='Y' where id=? and page=? ";
        } else
        {
            query = "update am_raisentry_post set entrypostflag='N' where id=? and page=? ";
        }
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            ps.setString(1, id);
            ps.setString(2, page1);
            ps.execute();
        } catch (Exception ex) {

            System.out.println("WARNING:cannot update am_raisentry_post->");
            ex.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
    }

    public void sendMailAfterPosting(String asset_id)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        String asset_status;
        String FINDER_QUERY;
        con = null;
        ps = null;
        rs = null;
        asset_status = "";
        int user_id = 0;
        FINDER_QUERY = "SELECT  asset_status,user_id from am_asset WHERE asset_id = ?";
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);
            ps.setString(1, asset_id);
            rs = ps.executeQuery();

            while (rs.next()) {
                asset_status = rs.getString("asset_status");
                user_id = rs.getInt("user_id");
            }

            if(asset_status.equalsIgnoreCase("ACTIVE"))
            {
                AssetRecordsBean ad = new AssetRecordsBean();
                String vendor_name = ad.vendorName(asset_id);
//                String assetDescription = getCodeName((new StringBuilder("select description from am_asset where asset_id='")).append(asset_id).append("'").toString());
                String param = "select description from am_asset where asset_id= ?";
                String assetDescription = getCodeName(param,asset_id);
                EmailSmsServiceBus mail = new EmailSmsServiceBus();
                String subjectr = "Payment for new asset";
                String msgText11 = (new StringBuilder(String.valueOf(vendor_name))).append(" has been paid for asset with ").append(assetDescription).append(" ID: ").append(asset_id).append(".").toString();
                mail.sendMailUser(asset_id, subjectr, msgText11);
            }
        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch [sendMailAfterPosting]->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
    }

    public boolean insertApproval2(String id, String description, String page, String flag, String partPay, String UserId, String Branch, 
            String subjectToVat, String whTax, String url, int transID)
    {
        boolean done;
        Connection con;
        PreparedStatement ps;
        String query;
        done = true;
        flag = "Y";
        con = null;
        ps = null;
        query = "INSERT INTO [am_raisentry_post](Id,Description,Page,Flag,partPay,UserId,Branch,s" +
"ubjectToVat,whTax,url,trans_id,entryPostFlag,GroupIdStatus,Posting_Date) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
;
        try
        {
            con = getConnection("legendPlus");
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
            ps.setInt(11, transID);
            ps.setString(12, "N");
            ps.setString(13, "N");
            Timestamp approveddate =  dbConnection.getDateTime(new java.util.Date());
 //           System.out.println("<<<<<approveddate: "+approveddate);
            ps.setTimestamp(14, approveddate);
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

    public void updateAssetPendingTrans(String Approv[], String code, String assetID)
    {
        int transaction_level;
        String update_Approval_Qry;
        String tranLevelQuery;
        Connection con;
        PreparedStatement ps;
        SimpleDateFormat timer;
        transaction_level = 0;
        update_Approval_Qry = "update am_asset_approval set user_id=?,super_id=?,amount=?,posting_date=?,descri" +
"ption=?,effective_date=?,branchCode=?,asset_status=?,tran_type=?,process_status=" +
"?,tran_sent_time=?,transaction_level=?,reject_reason=?,approval1=?,approval2=?,a" +
"pproval3=?,approval4=?,approval5=?,approval_level_count=? where asset_status='RE" +
"JECTED' and asset_id=?  "
;
        tranLevelQuery = (new StringBuilder("select level from approval_level_setup where code ='")).append(code).append("'").toString();
        con = null;
        ps = null;
        ResultSet rs = null;
        timer = new SimpleDateFormat("kk:mm:ss");
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(tranLevelQuery);
            rs = ps.executeQuery();
            while (rs.next()) {
                transaction_level = rs.getInt(1);
            }
            ps = con.prepareStatement(update_Approval_Qry);
            ps.setString(1, Approv[1] != null ? Approv[1] : "");
            ps.setString(2, Approv[2] != null ? Approv[2] : "");
            ps.setDouble(3, Approv[3] != null ? Double.parseDouble(Approv[3]) : 0.0D);
            ps.setDate(4, Approv[4] != null ? dateConvert(Approv[4]) : null);
            ps.setString(5, Approv[5] != null ? Approv[5] : "");
            ps.setDate(6, Approv[6] != null ? dateConvert(Approv[6]) : null);
            ps.setString(7, Approv[7] != null ? Approv[7] : "");
            ps.setString(8, Approv[8] != null ? Approv[8] : "");
            ps.setString(9, Approv[9] != null ? Approv[9] : "");
            ps.setString(10, Approv[10]);
            ps.setString(11, timer.format(new Date()));
            ps.setInt(12, transaction_level);
            ps.setString(13, "");
            ps.setString(14, "");
            ps.setString(15, "");
            ps.setString(16, "");
            ps.setString(17, "");
            ps.setString(18, "");
            ps.setInt(19, 0);
            ps.setString(20, assetID);
            ps.executeUpdate();
        } catch (Exception ex) {
            System.out.println("WARN:GroupAssetToAssetBean -Error Updating am_asset_approval->" + ex);
        } finally {
            closeConnection(con, ps);
        }
    }

    public void updateAssetStatus(String assetid, String status, String status1)
    {
        Connection con;
        PreparedStatement ps;
        String NOTIFY_QUERY;
        con = null;
        ps = null;
        NOTIFY_QUERY = " update am_asset_approval set process_status=?,approval_level_count=?,approval1=" +
"?,approval2=?,approval3=?,approval4=?,approval5=?,asset_status=? where asset_id=" +
"? "
;
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(NOTIFY_QUERY);
            ps.setString(1, status);
            ps.setInt(2, 0);
            ps.setInt(3, 0);
            ps.setInt(4, 0);
            ps.setInt(5, 0);
            ps.setInt(6, 0);
            ps.setInt(7, 0);
            ps.setString(8, status1);
            ps.setString(9, assetid);
            ps.executeUpdate();
        } catch (Exception ex) {
            System.out.println("WARNING: cannot update am_asset_approval+" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
    }

    public void sendEmailUpdateAssetStatus(String asset_id)
    {
        try
        {
            AssetRecordsBean arb = new AssetRecordsBean();
            String q2 = (new StringBuilder("update am_AssetDisposal set disposal_status='D' where asset_id = '")).append(asset_id).append("'").toString();
            arb.updateAssetStatusChange(q2);
            String q3 = (new StringBuilder("update am_asset set Asset_Status='Disposed' where asset_id = '")).append(asset_id).append("'").toString();
            arb.updateAssetStatusChange(q3);
            CompanyHandler compHandler = new CompanyHandler();
            String mailSetUp[] = compHandler.getEmailStatusAndName("asset disposal");
            String Status1 = mailSetUp[0];
            String mail_code = mailSetUp[1];
            Status1 = Status1.trim();
            if(Status1.equalsIgnoreCase("Active"))
            {
                String transaction_type = "Asset Disposal";
                String subject = "Asset Disposal";
                Codes message = new Codes();
                EmailSmsServiceBus mail = new EmailSmsServiceBus();
                String to = message.MailTo(mail_code, transaction_type);
                String msgText1 = message.MailMessage(mail_code, transaction_type);
                mail.sendMail(to, subject, msgText1);
            }
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder("Error occurred in sendEmailUpdateAssetStatus(): of ApprovalRecords >>")).append(e).toString());
        }
    }

    public int updateUtil(String query)
    {
        int result;
        Connection con;
        PreparedStatement ps;
        result = 0;
        con = null; 
        ps = null;
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            result = ps.executeUpdate();

        } catch (Exception ex) {

            System.out.println("WARNING:cannot update updateUtil()->");
            ex.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
        return result;
    }

    public String[] getPartPaymentData(String query,String assetID, int tranID)
    {
        String a[];
//        String query;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        a = new String[3];
//        query = "select amount, effective_date, description from am_asset_approval where asset_id" +
//" = ? and transaction_id = ?"
;
//		System.out.println("====getPartPaymentData query=====  "+query+" assetID=====  "+assetID+" tranID=====  "+tranID);
        con = null;
        ps = null;
        rs = null;
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query.toString());
            if(query.contains("asset_id") && !query.contains("transaction_id")){
           // 	System.out.println("=======cost_price ======");
          	  ps.setString(1, assetID);
            } 
            if(query.contains("asset_id") && query.contains("transaction_id")){
            ps.setString(1, assetID);
            ps.setInt(2, tranID);
            } 
            if(query.contains("GROUP_ID")){
//            	System.out.println("=======GROUP_ID ======");
          	  ps.setString(1, assetID);
            } 
            rs = ps.executeQuery();
            

            while (rs.next()) {
            	if(query.contains("asset_id") && !query.contains("transaction_id")){
                a[0] = rs.getString(1);
                a[1] = rs.getString(2);
                a[2] = rs.getString(3);
            	 } 
            	if(query.contains("asset_id") && query.contains("transaction_id")){
                    a[0] = rs.getString(1);
                    a[1] = formatDate(rs.getDate(2));
                    a[2] = rs.getString(3);
                	 }       
            	if(query.contains("GROUP_ID") && !query.contains("transaction_id")){
                    a[0] = rs.getString(1);
                    a[1] = rs.getString(2);
                    a[2] = rs.getString(3);
                	 }    
            }

        } catch (Exception e) {

            System.out.println("Error occured in getPartPaymentData(): " + e);
        } finally {
            closeConnection(con, ps, rs);

        }
        return a;
    }

    public String[] getPartPaymentData(String query,String assetID)
    {
        String a[];
//        String query;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        a = new String[3];
//        query = "select amount, effective_date, description from am_asset_approval where asset_id" +
//" = ? and transaction_id = ?"
;
        con = null;
        ps = null;
        rs = null;
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query.toString());
            ps.setString(1, assetID);
            rs = ps.executeQuery();
            

            while (rs.next()) {
                a[0] = rs.getString(1);
                a[1] = rs.getString(2);
                a[2] = rs.getString(3);             	
            }

        } catch (Exception e) {

            System.out.println("Error occured in getPartPaymentData(): " + e);
        } finally {
            closeConnection(con, ps, rs);

        }
        return a;
    }

    public ArrayList findPostedEntry(String Filter,String tranId)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        ArrayList collection;
        String Finder_qry;
        con = null;
        ps = null;
        rs = null;
        Approval app = null;
        collection = new ArrayList();
        String Finder_qry_old = (new StringBuilder(" select Id,p.Description,Page,p.posting_date AS approved_date,b.posting_date as initiated_date,Flag,partPay,UserId,Branch, cost_price,branch_id, " +
"subjectToVat,whTax,operation1,exitPage,url,trans_id  from am_raisentry_post p, d" +
"bo.am_group_asset_main m  where GroupIdStatus = 'Y'  and id in ( select convert(" +
"varchar ,group_id) from dbo.am_group_asset_main) and id = convert(varchar ,group" +
"_id) "
)).append(Filter).append(" UNION ").append(" select Id,p.Description,Page,p.posting_date AS approved_date,b.posting_date as initiated_date,Flag,partPay,UserId,Branch, cost_price,branch_id,").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id ").append(" from am_raisentry_post p, am_asset a ").append(" where id in (select asset_id from am_raisentry_transaction where iso = '000') ").append(" AND ID = ASSET_ID ").append(Filter).toString();
        Finder_qry = (new StringBuilder(" select Id,p.Description,Page,p.posting_date AS approved_date,b.posting_date as initiated_date,Flag,partPay,UserId,Branch, cost_price,branch_id, " +
"subjectToVat,whTax,operation1,exitPage,url,trans_id,m.asset_code  from am_raisen" +
"try_post p, dbo.am_group_asset_main m,am_asset_approval b   where GroupIdStatus = 'Y'  and id in ( se" +
"lect convert(varchar ,group_id) from dbo.am_group_asset_main) and id = convert(v" +
"archar ,group_id)  and convert(varchar ,P.TRANS_ID) = b.BATCH_ID  "
)).append(Filter).append(" UNION ").append(" select Id,p.Description,Page,p.posting_date AS approved_date,b.posting_date as initiated_date,Flag,partPay,UserId,Branch, cost_price,branch_id,").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id,a.asset_code ").append(" from am_raisentry_post p, am_asset a,am_asset_approval b  ").append(" where entryPostFlag = 'Y' and id in ( select asset_id from am_raisentry_transaction where convert(varchar ,p.trans_id) = " +
"trans_id and iso = '000') "
).append(" AND ID = a.ASSET_ID and convert(varchar ,p.TRANS_ID) = b.BATCH_ID   ").append(Filter).toString();
//        Finder_qry = (new StringBuilder(String.valueOf(Finder_qry))).append(" ").append(ordering).toString();
//        System.out.println("<<<<<<findPostedEntry Finder_qry: "+Finder_qry);
        try
        { 
//            con = getConnection("legendPlus");
//            ps = con.prepareStatement(Finder_qry);
//
//            rs = ps.executeQuery();

            con = getConnection("legendPlus");
            ps = con.prepareStatement(Finder_qry.toString());
            if(Filter.contains("TRANS_ID")){
           // 	System.out.println("=======cost_price ======");
          	  ps.setString(1, tranId);
          	  ps.setString(2, tranId);
            }       
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
                int tranID = rs.getInt("trans_id");
                int assetCode = rs.getInt("asset_code");
                String approveddate = rs.getString("approved_date");
                String initiateDate = rs.getString("initiated_date");
                app = new Approval(id, Description, Page, Flag, partPay, UserId, Branch, subjectToVat, whTax, operation1, exitPage, url);
                app.setTranID(tranID);
                app.setAssetCreator(getTransactionInitiatorByTranID(tranID, UserId,id));
                app.setPosting_date(initiateDate);
                app.setApproved_date(approveddate);
                collection.add(app);
            }

        } catch (Exception ex) {
            System.out.println("findPostedEntry(,): WARNING: cannot fetch [ApprovalRecords]->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return collection;
    }


    public ArrayList findPostedEntry(String Filter,String apprvLimit_min,String apprvLimit_max,String branch_id,String fromDate,String toDate,String User_Name,String Id,String ordering)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        ArrayList collection;
        String Finder_qry;
        con = null;
        ps = null;
        rs = null;
        Approval app = null;
        collection = new ArrayList();
        String Finder_qry_old = (new StringBuilder(" select Id,p.Description,Page,p.posting_date AS approved_date,b.posting_date as initiated_date,Flag,partPay,UserId,Branch, cost_price,branch_id, " +
"subjectToVat,whTax,operation1,exitPage,url,trans_id  from am_raisentry_post p, d" +
"bo.am_group_asset_main m  where GroupIdStatus = 'Y'  and id in ( select convert(" +
"varchar ,group_id) from dbo.am_group_asset_main) and id = convert(varchar ,group" +
"_id) "
)).append(Filter).append(" UNION ").append(" select Id,p.Description,Page,p.posting_date AS approved_date,b.posting_date as initiated_date,Flag,partPay,UserId,Branch, cost_price,branch_id,").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id ").append(" from am_raisentry_post p, am_asset a ").append(" where id in (select asset_id from am_raisentry_transaction where iso = '000') ").append(" AND ID = ASSET_ID ").append(Filter).toString();
        Finder_qry = (new StringBuilder(" select Id,p.Description,Page,p.posting_date AS approved_date,b.posting_date as initiated_date,Flag,partPay,UserId,Branch, cost_price,branch_id, " +
"subjectToVat,whTax,operation1,exitPage,url,trans_id,m.asset_code  from am_raisen" +
"try_post p, dbo.am_group_asset_main m,am_asset_approval b   where GroupIdStatus = 'Y'  and id in ( se" +
"lect convert(varchar ,group_id) from dbo.am_group_asset_main) and id = convert(v" +
"archar ,group_id)  and convert(varchar ,P.TRANS_ID) = b.BATCH_ID  "
)).append(Filter).append(" UNION ").append(" select Id,p.Description,Page,p.posting_date AS approved_date,b.posting_date as initiated_date,Flag,partPay,UserId,Branch, cost_price,branch_id,").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id,a.asset_code ").append(" from am_raisentry_post p, am_asset a,am_asset_approval b  ").append(" where entryPostFlag = 'Y' and id in ( select asset_id from am_raisentry_transaction where convert(varchar ,p.trans_id) = " +
"trans_id and iso = '000') "
).append(" AND ID = a.ASSET_ID and convert(varchar ,p.TRANS_ID) = b.BATCH_ID   ").append(Filter).toString();
        Finder_qry = (new StringBuilder(String.valueOf(Finder_qry))).append(" ").append(ordering).toString();
        
       Finder_qry = "select Id,p.Description,Page,p.posting_date AS approved_date,b.posting_date as initiated_date,Flag,partPay,UserId,Branch, cost_price,branch_id, subjectToVat,whTax,operation1,exitPage,url,trans_id,m.asset_code  from am_raisentry_post p, dbo.am_group_asset_main m,am_asset_approval b   where GroupIdStatus = 'Y'  and id in ( select convert(varchar ,group_id) from dbo.am_group_asset_main) and id = convert(varchar ,group_id)  and convert(varchar ,P.TRANS_ID) = b.BATCH_ID "+Filter+" "+
        "UNION "+
        "select Id,p.Description,Page,p.posting_date AS approved_date,b.posting_date as initiated_date,Flag,partPay,UserId,Branch, cost_price,'0' AS branch_id, subjectToVat,whTax,operation1,exitPage,url,trans_id,'0' AS asset_code  from am_raisentry_post p, dbo.GROUP_ASSET_UPLOAD m,am_asset_approval b  where GroupIdStatus = 'Y' and id in ( select convert(varchar ,group_id) from dbo.GROUP_ASSET_UPLOAD) and id = convert(varchar ,group_id) and convert(varchar ,P.ID) = b.BATCH_ID "+Filter+ " "+
       "UNION "+
        "select Id,p.Description,Page,p.posting_date AS approved_date,b.posting_date as initiated_date,Flag,partPay,UserId,Branch, cost_price,branch_id, subjectToVat,whTax,operation1,exitPage,url,trans_id,a.asset_code  from am_raisentry_post p, am_asset a,am_asset_approval b   where entryPostFlag = 'Y' and id in ( select asset_id from am_raisentry_transaction where convert(varchar ,p.trans_id) = trans_id and iso = '000')  AND ID = a.ASSET_ID and convert(varchar ,p.TRANS_ID) = b.BATCH_ID "+Filter+" order by page, userid ";
//        System.out.println("<<<<<<findPostedEntry Finder_qry: "+Finder_qry);
        try
        {  
//            con = getConnection("legendPlus");
//            ps = con.prepareStatement(Finder_qry);
//
//            rs = ps.executeQuery();
//            
            con = getConnection("legendPlus");
            ps = con.prepareStatement(Finder_qry.toString());
            //query_ = query_.replaceAll("\\s", "");
            if(Filter.contains("cost_price") && Filter.contains("branch_id") && Filter.contains("p.posting_DATE")){
//            	System.out.println("=======cost_price, BRANCH_ID, p.posting_DATE ======");
          	  ps.setString(1, apprvLimit_min);
          	  ps.setString(2, apprvLimit_max);
          	  ps.setString(3, branch_id);
          	  ps.setString(4, fromDate);
          	  ps.setString(5, toDate);
          	  ps.setString(6, apprvLimit_min);
          	  ps.setString(7, apprvLimit_max);
          	  ps.setString(8, branch_id);
          	  ps.setString(9, fromDate);
          	  ps.setString(10, toDate);    
            }
            if(Filter.contains("cost_price")){
//            	System.out.println("=======cost_price ======");
          	  ps.setString(1, apprvLimit_min);
          	  ps.setString(2, apprvLimit_max);
//          	  ps.setString(3, fromDate);
//          	  ps.setString(4, toDate);
//          	  ps.setString(5, ordering);
            }            
            if(Filter.contains("cost_price") && Filter.contains("p.posting_DATE")){
//            	System.out.println("=======cost_price, p.posting_DATE ======cost_price: "+Filter.contains("cost_price")+"   POSTING_DATE: "+Filter.contains("p.posting_DATE")+"   USERID: "+Filter.contains("USERID")+"  ID: "+Filter.contains("id")+"  BRANCH_ID: "+Filter.contains("BRANCH_ID"));
            	  ps.setString(1, apprvLimit_min);
            	  ps.setString(2, apprvLimit_max);
            	  ps.setString(3, fromDate);
            	  ps.setString(4, toDate);
            	  ps.setString(5, apprvLimit_min);
            	  ps.setString(6, apprvLimit_max);
            	  ps.setString(7, fromDate);
            	  ps.setString(8, toDate);
            	  ps.setString(9, apprvLimit_min);
            	  ps.setString(10, apprvLimit_max);
            	  ps.setString(11, fromDate);
            	  ps.setString(12, toDate);
              }
            if(Filter.contains("cost_price") && Filter.contains("id") && Filter.contains("p.posting_DATE")){
//            	System.out.println("=======cost_price, p.posting_DATE and ID ======");
        	  ps.setString(1, apprvLimit_min);
        	  ps.setString(2, apprvLimit_max);
        	  ps.setString(3, Id);
        	  ps.setString(4, fromDate);
        	  ps.setString(5, toDate);
        	  ps.setString(6, apprvLimit_min);
        	  ps.setString(7, apprvLimit_max);
        	  ps.setString(8, Id);
        	  ps.setString(9, fromDate);
        	  ps.setString(10, toDate);          	  
            }      
            if(Filter.contains("cost_price") && Filter.contains("p.posting_DATE") && Filter.contains("USERID")){
//            	System.out.println("=======cost_price, p.posting_DATE and USERID  ======");
          	  ps.setString(1, apprvLimit_min);
          	  ps.setString(2, apprvLimit_max);
          	  ps.setString(3, User_Name);
          	  ps.setString(4, fromDate);
          	  ps.setString(5, toDate);
          	  ps.setString(6, apprvLimit_min);
          	  ps.setString(7, apprvLimit_max);
          	  ps.setString(8, User_Name);
          	  ps.setString(9, fromDate);
          	  ps.setString(10, toDate);      
              }  
            if(Filter.contains("cost_price") && Filter.contains("BRANCH_ID") && Filter.contains("p.posting_DATE") && Filter.contains("USERID")){
//            	System.out.println("=======cost_price, BRANCH_ID, POSTING_DATE and USERID ======");
          	  ps.setString(1, apprvLimit_min);
          	  ps.setString(2, apprvLimit_max);
          	  ps.setString(3, branch_id);
          	  ps.setString(4, User_Name);
          	  ps.setString(5, fromDate);
          	  ps.setString(6, toDate);
            } 
            if(Filter.contains("cost_price") && Filter.contains("p.posting_DATE") && Filter.contains("USERID") && Filter.contains("id")){
           // 	System.out.println("=======cost_price, POSTING_DATE, USERID and ID ======");
          	  ps.setString(1, apprvLimit_min);
          	  ps.setString(2, apprvLimit_max);
          	  ps.setString(3, User_Name);
          	  ps.setString(4, Id);
          	  ps.setString(5, fromDate);
          	  ps.setString(6, toDate);
            }
            if(Filter.contains("cost_price") && Filter.contains("BRANCH_ID") && Filter.contains("p.posting_DATE") && Filter.contains("ID")){
//            	System.out.println("=======cost_price, BRANCH_ID, POSTING_DATE and ID ======");
          	  ps.setString(1, apprvLimit_min);
          	  ps.setString(2, apprvLimit_max);
          	  ps.setString(3, branch_id);
          	  ps.setString(4, Id);
          	  ps.setString(5, fromDate);
          	  ps.setString(6, toDate);  
            }
            if(Filter.contains("cost_price") && Filter.contains("BRANCH_ID") && Filter.contains("USERID") && Filter.contains("id") && Filter.contains("p.posting_DATE")){
//            	System.out.println("=======cost_price, BRANCH_ID, POSTING_DATE, USERID and ID ======");
            	  ps.setString(1, apprvLimit_min);
            	  ps.setString(2, apprvLimit_max);
            	  ps.setString(3, branch_id);
            	  ps.setString(4, User_Name);
            	  ps.setString(5, Id);
            	  ps.setString(6, fromDate);
            	  ps.setString(7, toDate);
              }
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
                int tranID = rs.getInt("trans_id");
                int assetCode = rs.getInt("asset_code");
                String approveddate = rs.getString("approved_date");
                String initiateDate = rs.getString("initiated_date");
                app = new Approval(id, Description, Page, Flag, partPay, UserId, Branch, subjectToVat, whTax, operation1, exitPage, url);
                app.setTranID(tranID);
                app.setAssetCreator(getTransactionInitiatorByTranID(tranID, UserId,id));
                app.setPosting_date(initiateDate);
                app.setApproved_date(approveddate);
                collection.add(app);
            }

        } catch (Exception ex) {
            System.out.println("findPostedEntry(,): WARNING: cannot fetch [ApprovalRecords]->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return collection;
    }

    public void triggerQuery()
    {
        String queryA;
        String queryB;
        int counter;
        String query1A = " IF OBJECT_ID ('Legend_Security1','TR') IS NOT NULL  DROP TRIGGER Legend_Securit" +
"y1  "
;
        String query1B = " CREATE TRIGGER Legend_Security1  ON AM_ASSET_ARCHIVE with ENCRYPTION  AFTER INS" +
"ERT, UPDATE, DELETE   AS RAISERROR ('You Can not Modify the record', 16, 10) "
;
        String query2A = " IF OBJECT_ID ('Legend_Security2','TR') IS NOT NULL  DROP TRIGGER Legend_Securit" +
"y2   "
;
        String query2B = " CREATE TRIGGER Legend_Security2  ON am_gb_User with ENCRYPTION  AFTER INSERT, U" +
"PDATE, DELETE   AS RAISERROR ('You Can not Modify the record', 16, 10) "
;
        String query3A = " IF OBJECT_ID ('Legend_Security3','TR') IS NOT NULL  DROP TRIGGER Legend_Securit" +
"y3   "
;
        String query3B = " CREATE TRIGGER Legend_Security3  ON am_ad_update_audit with ENCRYPTION  AFTER I" +
"NSERT, UPDATE, DELETE   AS RAISERROR ('You Can not Modify the record', 16, 10) "
;
        String query4A = " IF OBJECT_ID ('Legend_Security4','TR') IS NOT NULL  DROP TRIGGER Legend_Securit" +
"y4   "
;
        String query4B = " CREATE TRIGGER Legend_Security4  ON am_ad_privileges with ENCRYPTION  AFTER INS" +
"ERT, UPDATE, DELETE   AS RAISERROR ('You Can not Modify the record', 16, 10) "
;
        String query5A = " IF OBJECT_ID ('Legend_Security5','TR') IS NOT NULL  DROP TRIGGER Legend_Securit" +
"y5   "
;
        String query5B = " CREATE TRIGGER Legend_Security5  ON am_AssetDisposal with ENCRYPTION  AFTER INS" +
"ERT, UPDATE, DELETE   AS RAISERROR ('You Can not Modify the record', 16, 10) "
;
        String query6A = " IF OBJECT_ID ('Legend_Security6','TR') IS NOT NULL  DROP TRIGGER Legend_Securit" +
"y6   "
;
        String query6B = " CREATE TRIGGER Legend_Security6  ON am_assetRevalue with ENCRYPTION  AFTER INSE" +
"RT, UPDATE, DELETE   AS RAISERROR ('You Can not Modify the record', 16, 10) "
;
        String query7A = " IF OBJECT_ID ('Legend_Security7','TR') IS NOT NULL  DROP TRIGGER Legend_Securit" +
"y7   "
;
        String query7B = " CREATE TRIGGER Legend_Security7  ON am_assetTransfer with ENCRYPTION  AFTER INS" +
"ERT, UPDATE, DELETE   AS RAISERROR ('You Can not Modify the record', 16, 10) "
;
        String query8A = " IF OBJECT_ID ('Legend_Security8','TR') IS NOT NULL  DROP TRIGGER Legend_Securit" +
"y8   "
;
        String query8B = " CREATE TRIGGER Legend_Security8  ON am_ad_category with ENCRYPTION  AFTER INSER" +
"T, UPDATE, DELETE   AS RAISERROR ('You Can not Modify the record', 16, 10) "
;
        String query9A = " IF OBJECT_ID ('Legend_Security9','TR') IS NOT NULL  DROP TRIGGER Legend_Securit" +
"y9   "
;
        String query9B = " CREATE TRIGGER Legend_Security9  ON am_gb_company with ENCRYPTION  AFTER INSERT" +
", UPDATE, DELETE   AS RAISERROR ('You Can not Modify the record', 16, 10) "
;
        String query10A = " IF OBJECT_ID ('Legend_Security10','TR') IS NOT NULL  DROP TRIGGER Legend_Securi" +
"ty10   "
;
        String query10B = " CREATE TRIGGER Legend_Security10  ON am_asset_approval with ENCRYPTION  AFTER I" +
"NSERT, UPDATE, DELETE   AS RAISERROR ('You Can not Modify the record', 16, 10) "
;
        String query11A = " IF OBJECT_ID ('Legend_Security10A','TR') IS NOT NULL  DROP TRIGGER Legend_Secur" +
"ity10A   "
;
        String query11B = " CREATE TRIGGER Legend_Security10A  ON am_asset_approval_archive with ENCRYPTION" +
"  AFTER INSERT, UPDATE, DELETE   AS RAISERROR ('You Can not Modify the record', " +
"16, 10) "
;
        String query12A = " IF OBJECT_ID ('Legend_Security11','TR') IS NOT NULL  DROP TRIGGER Legend_Securi" +
"ty11   "
;
        String query12B = " CREATE TRIGGER Legend_Security11  ON am_assetReclassification with ENCRYPTION  " +
"AFTER INSERT, UPDATE, DELETE   AS RAISERROR ('You Can not Modify the record', 16" +
", 10) "
;
        String query13A = " IF OBJECT_ID ('Legend_Security12','TR') IS NOT NULL  DROP TRIGGER Legend_Securi" +
"ty12   "
;
        String query13B = " CREATE TRIGGER Legend_Security12  ON am_approval_remark with ENCRYPTION  AFTER " +
"INSERT, UPDATE, DELETE   AS RAISERROR ('You Can not Modify the record', 16, 10) "
;
        String query14A = " IF OBJECT_ID ('Legend_Security13','TR') IS NOT NULL  DROP TRIGGER Legend_Securi" +
"ty13   "
;
        String query14B = " CREATE TRIGGER Legend_Security13  ON am_ad_class_privileges with ENCRYPTION  AF" +
"TER INSERT, UPDATE, DELETE   AS RAISERROR ('You Can not Modify the record', 16, " +
"10)   "
;
        queryA = "";
        queryB = "";
        counter = 1;
        counter = 1;
        for (counter = 1; counter <= 2; counter++) {
        Connection con;
        PreparedStatement ps;
        PreparedStatement ps2;
        con = null;
        ps = null;
        ps2 = null;
        queryA = (new StringBuilder("query")).append(String.valueOf(counter)).append("A").toString();
        queryB = (new StringBuilder("query")).append(String.valueOf(counter)).append("B").toString();
        try
        {
            con = getConnection("legendPlus");
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

    public String triggerQuery(String inQuery)
    {
        String outQuery = "";
        if(inQuery.equalsIgnoreCase("query1A"))
        {
            outQuery = " IF OBJECT_ID ('Legend_Security1','TR') IS NOT NULL  DROP TRIGGER Legend_Securit" +
"y1   "
;
        }
        if(inQuery.equalsIgnoreCase("query1B"))
        {
            outQuery = " CREATE TRIGGER Legend_Security1  ON am_ad_privileges with ENCRYPTION  AFTER INS" +
"ERT, UPDATE, DELETE   AS RAISERROR ('You Can not Modify the record', 16, 10) "
;
        }
        if(inQuery.equalsIgnoreCase("query2A"))
        {
            outQuery = " IF OBJECT_ID ('Legend_Security2','TR') IS NOT NULL  DROP TRIGGER Legend_Securit" +
"y2   "
;
        }
        if(inQuery.equalsIgnoreCase("query2B"))
        {
            outQuery = " CREATE TRIGGER Legend_Security2  ON am_gb_User with ENCRYPTION  AFTER INSERT, U" +
"PDATE, DELETE   AS RAISERROR ('You Can not Modify the record', 16, 10) "
;
        }
        return outQuery;
    }

    public String userToEmailTransInitiator(int tranId)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        String user_id;
        String FINDER_QUERY;
        con = null;
        ps = null;
        rs = null;
        user_id = "";
        FINDER_QUERY = "SELECT user_Id from am_asset_approval WHERE transaction_Id = ? ";
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);
            ps.setInt(1, tranId);
            rs = ps.executeQuery();

            while (rs.next()) {
                user_id = rs.getString("user_Id");
            }

            System.out.println((new StringBuilder("\n\n the user id is")).append(user_id).toString());
        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch user id ->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return user_id;
    }

    public void sendMailAfterPostingToBranch(String asset_id)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        String asset_status;
        String branch_code;
        String FINDER_QUERY;
        con = null;
        ps = null;
        rs = null;
        asset_status = "";
        branch_code = "";
        FINDER_QUERY = "SELECT  asset_status,branch_code from am_asset WHERE asset_id = ?";
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);
            ps.setString(1, asset_id);
            rs = ps.executeQuery();

            while (rs.next()) {
                asset_status = rs.getString("asset_status");
                branch_code = rs.getString("branch_code");
            }

            if(asset_status.equalsIgnoreCase("ACTIVE"))
            {
                AssetRecordsBean ad = new AssetRecordsBean();
//                String assetDescription = getCodeName((new StringBuilder("select description from am_asset where asset_id='")).append(asset_id).append("'").toString());
                String param = "select description from am_asset where asset_id= ?";
                String assetDescription = getCodeName(param,asset_id);
//                String branchEmail = getCodeName((new StringBuilder("select email from am_ad_branch where branch_code='")).append(branch_code).append("'").toString());
                param = "select email from am_ad_branch where branch_code= ?";
                String branchEmail = getCodeName(param,branch_code);                
                EmailSmsServiceBus mail = new EmailSmsServiceBus();
                String subjectr = "Payment for new asset";
                String msgText11 = (new StringBuilder("Your branch has been debited for asset ")).append(assetDescription).append(" with asset ID: ").append(asset_id).toString();
                mail.sendMail(branchEmail, subjectr, msgText11);
            }


        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch [sendMailAfterPostingToBranch]->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
    }

    public void updateRaiseEntryPostEmail(String id, String page1, String transactionIdCost, String transactionIdVendor, String transactionIdWitholding, String transaction)
    {
        String lpoNum;
        String invNum;
        Connection con;
        PreparedStatement ps;
//        String assetStatus = getCodeName((new StringBuilder("select asset_status from am_asset where asset_id='")).append(id).append("'").toString());
        String param = "select asset_status from am_asset where asset_id= ?";
        String assetStatus = getCodeName(param,id);
//        lpoNum = getCodeName((new StringBuilder("select lpoNum from am_asset_improvement where asset_id='")).append(id).append("'").toString());
        param = "select lpoNum from am_asset_improvement where asset_id= ?";
        lpoNum = getCodeName(param,id);
//        invNum = getCodeName((new StringBuilder("select invoice_no from am_asset_improvement where asset_id='")).append(id).append("'").toString());
        param = "select invoice_no from am_asset_improvement where asset_id= ?";
        invNum = getCodeName(param,id);
        con = null;
        ps = null;
        String query = "";
        try
        {
            AssetRecordsBean arb = new AssetRecordsBean();
            if(transactionIdCost.equalsIgnoreCase("000"))
            {
                query = "update am_raisentry_post set entrypostflag='Y' where id=? and page=? ";
                arb.updateNewAssetStatus(id);
                appManager = new ApprovalManager();
                appManager.infoFromApproval(id, page1);
                sendMailAfterPosting(id);
                sendMailAfterPostingToBranch(id, transaction);
            } else
            {
                query = "update am_raisentry_post set entrypostflag='N' where id=? and page=? ";
                String del_am_invoice_no_qry = (new StringBuilder("Delete from am_invoice_no where asset_id='")).append(id).append("'").append("and lpo='").append(lpoNum).append("' and invoice_no='").append(invNum).append("'").toString();
                updateUtil(del_am_invoice_no_qry);
            }
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            ps.setString(1, id);
            ps.setString(2, page1);
            ps.execute();

        } catch (Exception ex) {

            System.out.println("WARNING:cannot update am_raisentry_post->");
            ex.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
    }

    public void updateRaiseEntryPostEmail(String id, String page1, String transactionIdCost, String transactionIdVendor, String transactionIdWitholding, String transaction, int tranId, 
            int usefullife)
    {
        String lpoNum;
        String invNum;
        String newimprovement;
        Connection con;
        PreparedStatement ps;
        String query;
//        String assetStatus = getCodeName((new StringBuilder("select asset_status from am_asset where asset_id='")).append(id).append("'").toString());
        String param = "select asset_status from am_asset where asset_id= ?";
        String assetStatus = getCodeName(param,id);
//        lpoNum = getCodeName((new StringBuilder("select lpoNum from am_asset_improvement where asset_id='")).append(id).append("'").toString());
        param = "select lpoNum from am_asset_improvement where asset_id= ?";
        lpoNum = getCodeName(param,id);
//        invNum = getCodeName((new StringBuilder("select invoice_no from am_asset_improvement where asset_id='")).append(id).append("'").toString());
        param = "select invoice_no from am_asset_improvement where asset_id= ?";
        invNum = getCodeName(param,id);
//        newimprovement = getCodeName((new StringBuilder("select IMPROVED from am_asset_improvement where asset_id='")).append(id).append("' and Revalue_id = ").append(tranId).append(" ").toString());
        param = "select IMPROVED from am_asset_improvement where asset_id= ?";
        newimprovement = getCodeName(param,id);
//        System.out.println((new StringBuilder(">>>>>>>>>>> the status of asset is ")).append(assetStatus).append("  Transaction Id: ").append(tranId).append("  Transaction: ").append(transaction).append("  usefullife: ").append(usefullife).toString());
        con = null;
        ps = null;
        query = "";
        try
        {
            AssetRecordsBean arb = new AssetRecordsBean();
//            System.out.println((new StringBuilder(">>>>>>>>>>> transactionIdCost is ")).append(transactionIdCost).append("  transactionIdVendor Id: ").append(transactionIdVendor).append("  transactionIdWitholding: ").append(transactionIdWitholding).append("  Transaction: ").append(transaction).toString());
            if(transactionIdCost.equalsIgnoreCase("000") && transactionIdVendor.equalsIgnoreCase("000") && transactionIdWitholding.equalsIgnoreCase("000"))
            {
                if(transaction.equalsIgnoreCase("improvement"))
                {
//                    System.out.println((new StringBuilder("Improvement useful life in updateRaiseEntryPostEmail: ")).append(usefullife).toString());
//                    System.out.println((new StringBuilder("I Am Inside the Update with Useful life: ")).append(usefullife).toString());
                    asset_manager.updateAssetMaintenance(id, tranId, usefullife, newimprovement);
                    query = "update am_raisentry_post set entrypostflag='Y',GroupIdStatus = 'Y' where id=? an" +
"d page=? and trans_id=?"
;
//                    System.out.println((new StringBuilder(">>>>>>>>>>> the query 1 is ")).append(query).append(" Asset Id: ").append(id).append(" Page: ").append(page1).append("  Transaction Id: ").append(tranId).toString());
                    arb.updateNewAssetStatus(id);
                    appManager = new ApprovalManager();
                    appManager.infoFromApproval(id, page1);
//                    System.out.println(">>>>>>>>>>> About to send Mail on the Transaction");
                    sendMailAfterPosting(id);
                    sendMailAfterPostingToBranch(id, transaction);
 //                   System.out.println(">>>>>>>>>>> Mail sent on the Transaction");
                }
            } else
            {
                query = "update am_raisentry_post set entrypostflag='N', GroupIdStatus = 'N' where id=? a" +
"nd page=? and trans_id=?"
;
                String del_am_invoice_no_qry = (new StringBuilder("Delete from am_invoice_no where asset_id='")).append(id).append("'").append("and lpo='").append(lpoNum).append("' and invoice_no='").append(invNum).append("'").toString();
                updateUtil(del_am_invoice_no_qry);
            }
//            System.out.println((new StringBuilder(">>>>>>>>>>> the query 2 is ")).append(query).append("   ID: ").append(id).append("    Page1: ").append(page1).append("     TranId: ").append(tranId).toString());
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            ps.setString(1, id);
            ps.setString(2, page1);
            ps.setInt(3, tranId);
            ps.execute();
        } catch (Exception ex) {

            System.out.println("WARNING:cannot update am_raisentry_post->");
            ex.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
    }

    public void updateRevaluationRaiseEntryPostEmail(String id, String page1, String transactionIdCost, String transactionIdVendor, String transactionIdWitholding, String transaction, int tranId)
    {
        String lpoNum;
        String invNum;
        Connection con;
        PreparedStatement ps;
//        String assetStatus = getCodeName((new StringBuilder("select asset_status from am_asset where asset_id='")).append(id).append("'").toString());
        String param = "select asset_status from am_asset where asset_id= ?";
        String assetStatus = getCodeName(param,id);
//        lpoNum = getCodeName((new StringBuilder("select lpoNum from am_asset_revaluation where asset_id='")).append(id).append("'").toString());
        param = "select lpoNum from am_asset_revaluation where asset_id= ?";
        lpoNum = getCodeName(param,id);
//        invNum = getCodeName((new StringBuilder("select invoice_no from am_asset_revaluation where asset_id='")).append(id).append("'").toString());
        param = "select invoice_no from am_asset_revaluation where asset_id= ?";
        invNum = getCodeName(param,id);
        con = null;
        ps = null;
        String query = "";
        try
        {
            AssetRecordsBean arb = new AssetRecordsBean();
            if(transactionIdCost.equalsIgnoreCase("000"))
            {
                if(transaction.equalsIgnoreCase("improvement"))
                {
                    asset_manager.updateAssetRevaluation(id, tranId);
                }
                query = "update am_raisentry_post set entrypostflag='Y' where id=? and page=? and trans_i" +
"d=?"
;
                arb.updateNewAssetStatus(id);
                appManager = new ApprovalManager();
                appManager.infoFromApproval(id, page1);
                sendMailAfterPosting(id);
                sendMailAfterPostingToBranch(id, transaction);
            } else
            {
                query = "update am_raisentry_post set entrypostflag='N' where id=? and page=? and trans_i" +
"d=?"
;
                String del_am_invoice_no_qry = (new StringBuilder("Delete from am_invoice_no where asset_id='")).append(id).append("'").append("and lpo='").append(lpoNum).append("' and invoice_no='").append(invNum).append("'").toString();
                updateUtil(del_am_invoice_no_qry);
            }
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            ps.setString(1, id);
            ps.setString(2, page1);
            ps.setInt(3, tranId);
            ps.execute();

        } catch (Exception ex) {

            System.out.println("WARNING:updateRevaluationRaiseEntryPostEmail cannot update am_raisentry_post->");
            ex.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
    }

    public void sendMailAfterPostingToBranch(String asset_id, String transaction)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        String asset_status;
        String branch_code;
        String FINDER_QUERY;
        con = null;
        ps = null;
        rs = null;
        asset_status = "";
        branch_code = "";
        FINDER_QUERY = "SELECT  asset_status,branch_code from am_asset WHERE asset_id = ?";
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);
            ps.setString(1, asset_id);
            rs = ps.executeQuery();

            while (rs.next()) {
                asset_status = rs.getString("asset_status");
                branch_code = rs.getString("branch_code");
            }

            if(asset_status.equalsIgnoreCase("ACTIVE"))
            {
                AssetRecordsBean ad = new AssetRecordsBean();
//                String assetDescription = getCodeName((new StringBuilder("select description from am_asset where asset_id='")).append(asset_id).append("'").toString());
                String param = "select description from am_asset where asset_id= ?";
                String assetDescription = getCodeName(param,asset_id);
//                String branchEmail = getCodeName((new StringBuilder("select email from am_ad_branch where branch_code='")).append(branch_code).append("'").toString());
                param = "select email from am_ad_branch where branch_code= ?";
                String branchEmail = getCodeName(param,branch_code);
                EmailSmsServiceBus mail = new EmailSmsServiceBus();
                String subjectr = "Payment for new asset";
                String msgText11 = (new StringBuilder(" Your branch has been debited for asset ")).append(assetDescription).append(" with asset ID: ").append(asset_id).toString();
                mail.sendMail(branchEmail, subjectr, msgText11);
                if(transaction.equalsIgnoreCase("transfer"))
                {
//                    String oldAssetID = getCodeName((new StringBuilder("select old_asset_id from am_asset where asset_id='")).append(asset_id).append("'").toString());
                    param = "select old_asset_id from am_asset where asset_id= ?";
                    String oldAssetID = getCodeName(param,asset_id);
//                    String oldBranchCode = getCodeName((new StringBuilder("select old_branch_code from am_assetTransfer where asset_id='")).append(oldAssetID).append("'").toString());
                    param = "select old_branch_code from am_assetTransfer where asset_id= ?";
                    String oldBranchCode = getCodeName(param,oldAssetID);
            //        String branchEmailLosing = getCodeName((new StringBuilder("select email from am_ad_branch where branch_code='")).append(oldBranchCode).append("'").toString());
                    param = "select email from am_ad_branch where branch_code= ?";
                    String branchEmailLosing = getCodeName(param,oldBranchCode);
                    msgText11 = (new StringBuilder("Your branch has been credited for asset ")).append(assetDescription).append(" with asset ID: ").append(oldAssetID).toString();
                    mail.sendMail(branchEmailLosing, subjectr, msgText11);
                }
                if(transaction.equalsIgnoreCase("WIP"))
                {
//                    String oldAssetID = getCodeName((new StringBuilder("select old_asset_id from am_asset where asset_id='")).append(asset_id).append("'").toString());
                    param = "select old_asset_id from am_asset where asset_id= ?";
                    String oldAssetID = getCodeName(param,asset_id);
   //                 String oldBranchCode = getCodeName((new StringBuilder("select old_branch_code from am_Wip_Reclassification where asset_id='")).append(oldAssetID).append("'").toString());
                    param = "select old_branch_code from am_Wip_Reclassification where asset_id= ?";
                    String oldBranchCode = getCodeName(param,oldAssetID);
//                    String branchEmailLosing = getCodeName((new StringBuilder("select email from am_ad_branch where branch_code='")).append(oldBranchCode).append("'").toString());
                    param = "select email from am_ad_branch where branch_code = ?";
                    String branchEmailLosing = getCodeName(param,oldBranchCode);
                    msgText11 = (new StringBuilder("Your branch has been credited for asset ")).append(assetDescription).append(" with asset ID: ").append(oldAssetID).toString();
                    mail.sendMail(branchEmailLosing, subjectr, msgText11);
                }
            }


        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch [sendMailAfterPostingToBranch]->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
    }

    public void updateRaiseEntryPostTransfer(String id, String page1, String transactionIdCost, String transactionIdAccum, String transaction)
    {
        String newAssetId;
        Connection con;
        PreparedStatement ps;
        String query;
//        String assetStatus = getCodeName((new StringBuilder("select asset_status from am_asset where asset_id='")).append(id).append("'").toString());
        String param = "select asset_status from am_asset where asset_id= ?";
        String assetStatus = getCodeName(param,id);
//        newAssetId = getCodeName((new StringBuilder("select new_asset_id from AM_ASSETTRANSFER where asset_id='")).append(id).append("'").toString());
        param = "select new_asset_id from AM_ASSETTRANSFER where asset_id= ?";
        newAssetId = getCodeName(param,id);
        con = null;
        ps = null;
        query = "";
        try 
        {
            AssetRecordsBean arb = new AssetRecordsBean();
            if(transactionIdCost.equalsIgnoreCase("000"))
            {
//                System.out.println("\n here in updateRaiseEntryPostTransfer>>>>>>>>>>>>>>>>");
                if(page1.equalsIgnoreCase("ASSET TRANSFER RAISE ENTRY"))
                {
                    query = "update am_raisentry_post set entrypostflag='Y' where id=? and page=? ";
                    asset_manager.updateAssetTransfer(id);
                    String change_id_query2 = (new StringBuilder("update am_asset set old_asset_id ='")).append(id).append("', asset_id ='").append(newAssetId).append("' where asset_id ='").append(id).append("'").toString();
                    arb.updateAssetStatusChange(change_id_query2);
                    appManager = new ApprovalManager();
                    appManager.infoFromApproval(id, page1);
                } else
                {
                    appManager = new ApprovalManager();
                    appManager.infoFromApproval(id, page1);
                }
                arb.updateNewAssetStatus(id);
                sendMailAfterPostingToBranch(id, transaction);
            } else
            {
                query = "update am_raisentry_post set entrypostflag='N' where id=? and page=? ";
            }
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder("Error in updateRaiseEntryPost() ")).append(e).toString());
        }
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            ps.setString(1, newAssetId);
            ps.setString(2, page1);
            ps.execute();

        } catch (Exception ex) {

            System.out.println("WARNING:cannot update updateRaiseEntryPostTransfer->");
            ex.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
    }

    public void updateAssetStatus2(int mtid, String status, String status1)
    {
        Connection con;
        PreparedStatement ps;
        String NOTIFY_QUERY;
        con = null;
        ps = null;
        NOTIFY_QUERY = " update am_asset_approval set process_status=?,approval_level_count=?,approval1=" +
"?,approval2=?,approval3=?,approval4=?,approval5=?,asset_status=? where transacti" +
"on_id=? "
;
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(NOTIFY_QUERY);
            ps.setString(1, status);
            ps.setInt(2, 0);
            ps.setInt(3, 0);
            ps.setInt(4, 0);
            ps.setInt(5, 0);
            ps.setInt(6, 0);
            ps.setInt(7, 0);
            ps.setString(8, status1);
            ps.setInt(9, mtid);
            ps.executeUpdate();

        } catch (Exception ex) {
            System.out.println("WARNING: cannot update am_asset_approval in updateAssetStatus2 +" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
    }

    public void updateAssetStatus3(int mtid, String status, String status1, String rejectReason)
    {
        Connection con;
        PreparedStatement ps;
        String NOTIFY_QUERY;
        con = null;
        ps = null;
        NOTIFY_QUERY = " update am_asset_approval set process_status=?,approval_level_count=?,approval1=" +
"?,approval2=?,approval3=?,approval4=?,approval5=?,asset_status=?,reject_reason=?" +
" where transaction_id=? "
;
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(NOTIFY_QUERY);
            ps.setString(1, status);
            ps.setInt(2, 0);
            ps.setInt(3, 0);
            ps.setInt(4, 0);
            ps.setInt(5, 0);
            ps.setInt(6, 0);
            ps.setInt(7, 0);
            ps.setString(8, status1);
            ps.setString(9, rejectReason);
            ps.setInt(10, mtid);
            ps.executeUpdate();

        } catch (Exception ex) {
            System.out.println("WARNING: cannot update am_asset_approval in updateAssetStatus3 +" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
    }

    public int getTranIdForRejetPost(String pageName, String asset_id)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        int tranId;
        String FINDER_QUERY;
        con = null;
        ps = null;
        rs = null;
        tranId = 0;
        FINDER_QUERY = "SELECT  trans_id from am_raisentry_post WHERE id = ? and page=? and entryPostFla" +
"g=?"
;
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);
            ps.setString(1, asset_id);
            ps.setString(2, pageName);
            ps.setString(3, "N");
            rs = ps.executeQuery();

            while (rs.next()) {
                tranId = rs.getInt(1);
            }


        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch getTranIdForRejetPost->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return tranId;
    }

    public String[] UncapitalizedraiseEntryInfoRepost(String asset_id)
    {
        String result[];
        Connection con;
        PreparedStatement ps;
        String query;
        result = new String[4];
        con = null;
        ResultSet rs = null;
        ps = null;
        query = (new StringBuilder("select description, branch_id, subject_to_vat, wh_tax from AM_ASSET_UNCAPITALIZE" +
"D where old_asset_id = '"
)).append(asset_id).append(" '").toString();
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                result[0] = rs.getString(1) != null ? rs.getString(1) : "";
                result[1] = rs.getString(2) != null ? rs.getString(2) : "";
                result[2] = rs.getString(3) != null ? rs.getString(3) : "";
                result[3] = rs.getString(4) != null ? rs.getString(4) : "";
            }

        } catch (Exception er) {
            System.out.println("Error in Query- raiseEntryInfoRepost()in class ApprovalRecords... ->" + er);
            er.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
        return result;
    }

    public String[] raiseEntryInfoRepost(String asset_id)
    {
        String result[];
        Connection con;
        PreparedStatement ps;
        String query;
        result = new String[4];
        con = null;
        ResultSet rs = null;
        ps = null;
        query = (new StringBuilder("select description, branch_id, subject_to_vat, wh_tax from am_asset where old_as" +
"set_id = '"
)).append(asset_id).append(" '").toString();
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                result[0] = rs.getString(1) != null ? rs.getString(1) : "";
                result[1] = rs.getString(2) != null ? rs.getString(2) : "";
                result[2] = rs.getString(3) != null ? rs.getString(3) : "";
                result[3] = rs.getString(4) != null ? rs.getString(4) : "";
            }

        } catch (Exception er) {
            System.out.println("Error in Query- raiseEntryInfoRepost()in class ApprovalRecords... ->" + er);
            er.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
        return result;
    }

    public void reverseAssetReclassification_19_05_2010(String old_id, String new_id)
    {
        int old_category;
        double old_depr_rate;
        double old_accum_dep;
        String old_categorCode;
        double oldNBV;
        String updateQuery2;
        String query1;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        old_category = 0;
        old_depr_rate = 0.0D;
        old_accum_dep = 0.0D;
        int total_life = 0;
        int remaining_life = 0;
        old_categorCode = "";
        double recalc_difference = 0.0D;
        String recalculate_depreciation = "";
        oldNBV = 0.0D;
        int i = 0;
        updateQuery2 = (new StringBuilder(" UPDATE AM_ASSET  SET   CATEGORY_ID = ?, DEP_RATE = ?,CATEGORY_CODE=?,ACCUM_DEP=" +
"?, nbv=?, asset_id=?   WHERE ASSET_ID = '"
)).append(new_id).append("'").toString();
        query1 = (new StringBuilder("select old_category_id,old_depr_rate,old_accum_dep,old_category_code,recalc_depr" +
",old_nbv from AM_ASSETRECLASSIFICATION where asset_id = '"
)).append(old_id).append("'").toString();
        con = null;
        ps = null;
        rs = null;
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query1);
            rs = ps.executeQuery();

            while (rs.next()) {
                old_category = rs.getInt(1);
                old_depr_rate = rs.getDouble(2);
                old_accum_dep = rs.getDouble(3);
                old_categorCode = rs.getString(4);
                recalculate_depreciation = rs.getString(5);
                oldNBV = rs.getDouble(6);
            }

            ps = con.prepareStatement(updateQuery2);
            ps.setInt(1, old_category);
            ps.setDouble(2, old_depr_rate);
            ps.setString(3, old_categorCode);
            ps.setDouble(4, old_accum_dep);
            ps.setDouble(5, oldNBV);
            ps.setString(6, old_id);
            ps.execute();
                    }
        catch(Exception e)
        {
            String warning = (new StringBuilder("WARNING:AssetReclassificationBean class: reverseAssetReclassification(): Error r" +
"eclassifying asset ->"
)).append(e.getMessage()).toString();
            System.out.println(warning);
            e.printStackTrace();
        
		} finally {
		    closeConnection(con, ps, rs);
		}
    }

    public String[] reclassRepostInfo(String asset_id) {
        String[] result = new String[4];
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        String query = "select nbv, category_code, branch_code, Monthly_Dep from am_asset where asset_id = '" + asset_id + " '";
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();


            while (rs.next()) {
                result[0] = rs.getString(1) == null ? "0" : rs.getString(1);
                result[1] = rs.getString(2) == null ? "" : rs.getString(2);
                result[2] = rs.getString(3) == null ? "" : rs.getString(3);
                result[3] = rs.getString(4) == null ? "0" : rs.getString(4);


            }
        } catch (Exception er) {
            System.out.println("Error in Query- reclassRepostInfo()in class ApprovalRecords... ->" + er);
            er.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
        return result;
    }

    public int updateReq_Depreciation()
    {
        Connection con;
        PreparedStatement ps;
        String NOTIFY_QUERY;
        int output;
        con = null;
        ps = null;
        NOTIFY_QUERY = " UPDATE am_asset SET Req_Redistribution = 'N' ";
        output = 0;
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(NOTIFY_QUERY);
            output = ps.executeUpdate();

        } catch (Exception ex) {
            System.out.println("WARNING: cannot update am_asset in updateReq_Depreciation +" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return output;
    }

    public void reverseAssetReclassification(String old_id, String new_id)
    {
        int old_category;
        double old_depr_rate;
        double old_accum_dep;
        int old_total_life;
        int old_remaining_life;
        double old_monthly_depreciation;
        String old_categorCode;
        double oldNBV;
        String updateQuery2;
        String query1;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        old_category = 0;
        old_depr_rate = 0.0D;
        old_accum_dep = 0.0D;
        old_total_life = 0;
        old_remaining_life = 0;
        old_monthly_depreciation = 0.0D;
        old_categorCode = "";
        double recalc_difference = 0.0D;
        String recalculate_depreciation = "";
        oldNBV = 0.0D;
        int i = 0;
        updateQuery2 = (new StringBuilder(" UPDATE AM_ASSET  SET   CATEGORY_ID = ?, DEP_RATE = ?,CATEGORY_CODE=?,ACCUM_DEP=" +
"?, nbv=?, asset_id=?,  Total_Life=?,Remaining_Life=?,Monthly_Dep=? WHERE ASSET_I" +
"D = '"
)).append(new_id).append("'").toString();
        query1 = (new StringBuilder("select old_category_id,old_depr_rate,old_accum_dep,old_category_code, recalc_dep" +
"r,old_nbv,old_total_life,old_remaining_life,Monthly_Dep from AM_ASSETRECLASSIFIC" +
"ATION where asset_id = '"
)).append(old_id).append("'").toString();
        con = null;
        ps = null;
        rs = null;
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query1);
            rs = ps.executeQuery();

            while (rs.next()) {
                old_category = rs.getInt(1);
                old_depr_rate = rs.getDouble(2);
                old_accum_dep = rs.getDouble(3);
                old_categorCode = rs.getString(4);
                recalculate_depreciation = rs.getString(5);
                oldNBV = rs.getDouble(6);
                old_total_life = rs.getInt(7);
                old_remaining_life = rs.getInt(8);
                old_monthly_depreciation = rs.getDouble(9);
            }

            ps = con.prepareStatement(updateQuery2);
            ps.setInt(1, old_category);
            ps.setDouble(2, old_depr_rate);
            ps.setString(3, old_categorCode);
            ps.setDouble(4, old_accum_dep);
            ps.setDouble(5, oldNBV);
            ps.setString(6, old_id);
            ps.setInt(7, old_total_life);
            ps.setInt(8, old_remaining_life);
            ps.setDouble(9, old_monthly_depreciation);
            ps.execute();

        } catch (Exception e) {
            String warning = "WARNING:AssetReclassificationBean class: reverseAssetReclassification(): Error reclassifying asset ->"
                    + e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
            closeConnection(con, ps, rs);
        }
    }

    public boolean deleteQuery(String delelteQuery)
    {
        Connection con;
        PreparedStatement ps;
        con = null;
        ps = null;
        boolean done;
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(delelteQuery);
            ps.executeUpdate();
            done = true;
        } catch (Exception ex) {
            done = false;
            System.out.println("WARNING: cannot delete deleteQuery+" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;
    }

    public boolean insertApproval(String id, String description, String page, String flag, String partPay, String UserId, String Branch, 
            String subjectToVat, String whTax, String url, String tranId)
    {
        boolean done;
        Connection con;
        PreparedStatement ps;
        String query;
        done = true;
        flag = "Y";
        con = null;
        ps = null;
        query = "INSERT INTO [am_raisentry_post](Id,Description,Page,Flag,partPay,UserId,Branch,s" +
"ubjectToVat,whTax,url,trans_id) VALUES(?,?,?,?,?,?,?,?,?,?,?)"
;
        try
        {
            con = getConnection("legendPlus");
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
            ps.setString(11, tranId);
            ps.execute();
        }
        catch(Exception ex)
        {
            done = false;
            System.out.println("WARNING:cannot insert am_raisentry_post in insertApproval->"
            +ex.getMessage());
        }
        finally{
        closeConnection(con, ps);
        }
        return done;
    }

    public boolean deleteRaiseEntry(String assetid, String page1, int tranId)
    {
        Connection con;
        PreparedStatement ps;
        String NOTIFY_QUERY;
        con = null;
        ps = null;
        NOTIFY_QUERY = "delete from  am_raisentry_post  WHERE ID = ? and page=? and trans_id=?";
        boolean done;
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(NOTIFY_QUERY);
            ps.setString(1, assetid);
            ps.setString(2, page1);
            ps.setInt(3, tranId);
            ps.executeUpdate();
            done = true;
        } catch (Exception ex) {
            done = false;
            System.out.println("WARNING: cannot delete am_raisentry_post+" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;
    }

public String[] raiseEntryInfoUnclassified(String asset_id)
{
    String result[];
    Connection con;
    PreparedStatement ps;
    String query;
    result = new String[4];
    con = null;
    ResultSet rs = null;
    ps = null;
    query = (new StringBuilder("select description, branch_id, subject_to_vat, wh_tax from AM_ASSET_UNCAPITALIZED where asset_" +
"id = '"
)).append(asset_id).append(" '").toString();
    try
    {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(query);
        rs = ps.executeQuery();


        while (rs.next()) {
            result[0] = rs.getString(1) == null ? "" : rs.getString(1);
            result[1] = rs.getString(2) == null ? "" : rs.getString(2);
            result[2] = rs.getString(3) == null ? "" : rs.getString(3);
            result[3] = rs.getString(4) == null ? "" : rs.getString(4);

        }
    } catch (Exception er) {
        System.out.println("Error in Query- raiseEntryInfoUnclassified() in class ApprovalRecords... ->" + er);
        er.printStackTrace();
    } finally {
        closeConnection(con, ps);
    }
    return result;
}

    public ArrayList findApprovalInitiator5(String Filter,String branch_ID_Filter,String apprvLimit_min,String apprvLimit_max,String User_Name,String assetId,String FromDate,String ToDate, String ordering)
    {      
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        ArrayList collection;
        String Finder_qry;
        con = null;
        ps = null;
        rs = null;
        Approval app = null;
        collection = new ArrayList();
        String Finder_qry_OLD = (new StringBuilder(" select Id,p.Description,Page,p.posting_date AS approved_date,b.posting_date as initiated_date,Flag,partPay,UserId,Branch, cost_price,branch_id, " +
"subjectToVat,whTax,operation1,exitPage,url,trans_id  from am_raisentry_post p, d" +
"bo.am_group_asset_main m  where GroupIdStatus = 'N'  and id in ( select convert(" +
"varchar ,group_id) from dbo.am_group_asset_main) and id = convert(varchar ,group" +
"_id) "
)).append(Filter).append(" UNION ").append(" select Id,p.Description,Page,p.posting_date AS approved_date,b.posting_date as initiated_date,Flag,partPay,UserId,Branch, cost_price,branch_id,").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id ").append(" from am_raisentry_post p, am_asset a ").append(" where id in (select asset_id from am_raisentry_transaction where iso <> '000') ").append(" AND ID = ASSET_ID ").append(" AND p.entryPostFlag='N' ").append(Filter).append(" UNION ").append(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, ").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id ").append(" from am_raisentry_post p, am_asset a ").append(" where  id not in (select asset_id from am_raisentry_transaction) ").append(" AND ID = ASSET_ID ").append(" AND p.entryPostFlag='N' ").append(Filter).append(" UNION ").append(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id,").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id  ").append(" from am_raisentry_post p, am_asset a  ").append(" where ID = a.OLD_ASSET_ID ").append(" AND p.entryPostFlag='N' ").append(" and a.asset_id not in (select asset_id from am_raisentry_transaction) ").append(Filter).toString();
        String Finder_qry_old2 = (new StringBuilder(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, " +
"subjectToVat,whTax,operation1,exitPage,url,trans_id  from am_raisentry_post p, d" +
"bo.am_group_asset_main m  where GroupIdStatus = 'N'  and id in ( select convert(" +
"varchar ,group_id) from dbo.am_group_asset_main) and id = convert(varchar ,group" +
"_id) "
)).append(Filter).append(" UNION ").append(" select Id,p.Description,Page,Flag,p.posting_date AS approved_date,b.posting_date as initiated_date,partPay,UserId,Branch, cost_price,branch_id,").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id ").append(" from am_raisentry_post p, am_asset a ").append(" where id in (select asset_id from am_raisentry_transaction where iso <> '000') ").append(" AND ID = ASSET_ID ").append(" AND p.entryPostFlag='N' ").append(Filter).append(" UNION ").append(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, ").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id ").append(" from am_raisentry_post p, am_asset a ").append(" where  id not in (select asset_id from am_raisentry_transaction) ").append(" AND ID = ASSET_ID ").append(" AND p.entryPostFlag='N' ").append(Filter).append(" UNION ").append(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id,").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id  ").append(" from am_raisentry_post p, am_asset a  ").append(" where ID = a.OLD_ASSET_ID ").append(" AND p.entryPostFlag='N' ").append(" and a.asset_id not in (select asset_id from am_raisentry_transaction) ").append(Filter).append("UNION ").append(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, ").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id ").append(" from am_raisentry_post p, am_asset a ").append(" where p.page='Asset Improvement Raise Entry' AND p.entryPostFlag='N'").append(" and p.id=a.asset_id ").append(Filter).append("UNION ").append(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, ").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id ").append(" from am_raisentry_post p, am_asset a ").append(" where p.page='ASSET PART PAYMENT ENTRY' AND p.entryPostFlag='N'").append(" and p.id=a.asset_id ").append(Filter).append("UNION ").append(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, ").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id ").append(" from am_raisentry_post p, am_asset a ").append(" where p.page='ASSET DISPOSAL RAISE ENTRY' AND p.entryPostFlag='N'").append(" and p.id=a.asset_id ").append(Filter).append("UNION ").append(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, ").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id ").append(" from am_raisentry_post p, am_asset a ").append(" where p.page='ACCELERATED DEPRECIATION RAISE ENTRY' AND p.entryPostFlag='N'").append(" and p.id=a.asset_id ").append(Filter).toString();

String Finder_qry_Last = " select Id,p.Description,Page,p.posting_date AS approved_date,b.posting_date as initiated_date,Flag,partPay,UserId,Branch, cost_price,branch_id, " +
"subjectToVat,whTax,operation1,exitPage,url,trans_id,p.asset_code,p.posting_date  from am_raisen" +
"try_post p, dbo.am_group_asset_main m, am_asset_approval b  where GroupIdStatus = 'N'  and id in ( se" +
"lect convert(varchar ,group_id) from dbo.am_group_asset_main) and id in ( select" +
" convert(varchar ,group_id) from AM_GROUP_ASSET)  and id = convert(varchar ,group_id) and convert(varchar ,P.TRANS_ID) = b.BATCH_ID  "+Filter
+" UNION "
+" select Id,p.Description,Page,p.posting_date AS approved_date,b.posting_date as initiated_date,Flag,partPay,UserId,Branch, cost_price,branch_id," 
+" subjectToVat,whTax,operation1,exitPage,url,trans_id,a.asset_code,p.posting_date " 
+" from am_raisentry_post p, am_asset a, am_asset_approval b " 
+" where ID = a.ASSET_ID AND p.entryPostFlag='N' and convert(varchar ,P.TRANS_ID) = b.BATCH_ID  "+ Filter 
+" UNION "
+" select Id,p.Description,Page,p.posting_date AS approved_date,b.posting_date as initiated_date,Flag,partPay,UserId,Branch, cost_price,branch_id, " 
+" subjectToVat,whTax,operation1,exitPage,url,trans_id,a.asset_code,p.posting_date " 
+" from am_raisentry_post p, am_asset a, am_asset_approval b " 
+" where ID = a.ASSET_ID  AND p.entryPostFlag='N' and convert(varchar ,P.TRANS_ID) = b.BATCH_ID "+ Filter 
+" UNION " 
+" select Id,p.Description,Page,p.posting_date AS approved_date,b.posting_date as initiated_date,Flag,partPay,UserId,Branch, cost_price,branch_id," 
+" subjectToVat,whTax,operation1,exitPage,url,trans_id,a.asset_code,p.posting_date  " 
+" from am_raisentry_post p, am_asset a, am_asset_approval b  " 
+" where ID = a.ASSET_ID AND p.entryPostFlag='N' and convert(varchar ,P.TRANS_ID) = b.BATCH_ID "+ Filter
+" UNION "
+" select Id,p.Description,Page,p.posting_date AS approved_date,b.posting_date as initiated_date,Flag,partPay,UserId,Branch, cost_price,branch_id, "
+" subjectToVat,whTax,operation1,exitPage,url,trans_id,a.asset_code,p.posting_date " 
+" from am_raisentry_post p, am_asset a, am_asset_approval b "
+" where p.page='Asset Improvement Raise Entry' AND p.entryPostFlag='N'"
+" and p.id=a.asset_id and convert(varchar ,P.TRANS_ID) = b.BATCH_ID "+ Filter
+" UNION "
+" select Id,p.Description,Page,p.posting_date AS approved_date,b.posting_date as initiated_date,Flag,partPay,UserId,Branch, cost_price,branch_id, "
+" subjectToVat,whTax,operation1,exitPage,url,trans_id,a.asset_code,p.posting_date "
+" from am_raisentry_post p, am_asset a, am_asset_approval b "
+" where p.page='ASSET PART PAYMENT ENTRY' AND p.entryPostFlag='N'"
+" and p.id=a.asset_id and convert(varchar ,P.TRANS_ID) = b.BATCH_ID "+Filter
+" UNION "
+" select Id,p.Description,Page,p.posting_date AS approved_date,b.posting_date as initiated_date,Flag,partPay,UserId,Branch, cost_price,branch_id, "
+" subjectToVat,whTax,operation1,exitPage,url,trans_id,a.asset_code,p.posting_date "
+" from am_raisentry_post p, am_asset a, am_asset_approval b "
+" where p.page='WIP RECLASSIFICATION' AND p.entryPostFlag='N'"
+" and p.asset_code=a.asset_code and convert(varchar ,P.TRANS_ID) = b.BATCH_ID "+Filter
+" UNION "
+" select Id,p.Description,Page,p.posting_date AS approved_date,b.posting_date as initiated_date,Flag,partPay,UserId,Branch, cost_price,branch_id, "
+" subjectToVat,whTax,operation1,exitPage,url,trans_id,a.asset_code,p.posting_date "
+" from am_raisentry_post p, am_asset a, am_asset_approval b "
+" where p.page='ASSET RECLASSIFICATION RAISE ENTRY' AND p.entryPostFlag='N'"
+" and p.asset_code=a.asset_code and convert(varchar ,P.TRANS_ID) = b.BATCH_ID "+Filter
+" UNION "
+" select Id,p.Description,Page,p.posting_date AS approved_date,b.posting_date as initiated_date,Flag,partPay,UserId,Branch, cost_price,branch_id, "
+" subjectToVat,whTax,operation1,exitPage,url,trans_id,a.asset_code,p.posting_date "
+" from am_raisentry_post p, am_asset a, am_asset_approval b "
+" where p.page='ASSET DISPOSAL RAISE ENTRY' AND p.entryPostFlag='N'"
+" and p.id=a.asset_id and convert(varchar ,P.TRANS_ID) = b.BATCH_ID "+Filter
+" UNION "
+" select Id,p.Description,Page,p.posting_date AS approved_date,b.posting_date as initiated_date,Flag,partPay,UserId,Branch, cost_price,branch_id, "
+" subjectToVat,whTax,operation1,exitPage,url,trans_id,a.asset_code,p.posting_date "
+" from am_raisentry_post p, am_asset2 a, am_asset_approval b "
+" where p.page='ASSET2 DISPOSAL RAISE ENTRY' AND p.entryPostFlag='N'"
+" and p.id=a.asset_id and convert(varchar ,P.TRANS_ID) = b.BATCH_ID "+Filter
+" UNION "
+" select Id,p.Description,Page,p.posting_date AS approved_date,b.posting_date as initiated_date,Flag,partPay,UserId,Branch, cost_price,branch_id, "
+" subjectToVat,whTax,operation1,exitPage,url,trans_id,a.asset_code,p.posting_date "
+" from am_raisentry_post p, am_asset a, am_asset_approval b "
+" where p.page='ASSET TRANSFER RAISE ENTRY' AND p.entryPostFlag='N' "
+" and p.asset_code=a.asset_code and convert(varchar ,P.TRANS_ID) = b.BATCH_ID "+Filter
+" UNION "
+" select distinct Id,p.Description,Page,p.posting_date AS approved_date,b.posting_date as initiated_date,Flag,partPay,UserId,Branch,  0.00 AS cost_price,'' as branch_Id, "
+" subjectToVat,whTax,operation1,exitPage,url,trans_id,p.asset_code,p.posting_date  from am_raisentry_post p, "
+" am_gb_bulkTransfer m, am_asset_approval b  where GroupIdStatus = 'N'  and id in ( select convert(varchar ,batch_id) "
+" from am_gb_bulkTransfer) and m.batch_id in ( select convert(varchar ,batch_id) from am_gb_bulkTransfer) "
+" and m.batch_id = convert(varchar ,b.batch_id) and P.ID = b.BATCH_ID "//+Filter
+" UNION "
+" select Id,p.Description,Page,p.posting_date AS approved_date,b.posting_date as initiated_date,Flag,partPay,UserId,Branch, cost_price,branch_id, "
+" subjectToVat,whTax,operation1,exitPage,url,trans_id,a.asset_code,p.posting_date "
+" from am_raisentry_post p, am_asset a, am_asset_approval b "
+" where p.page='ACCELERATED DEPRECIATION RAISE ENTRY' AND p.entryPostFlag='N'"
+" and p.id=a.asset_id and convert(varchar ,P.TRANS_ID) = b.BATCH_ID "+Filter
+" UNION "
+" select Id,p.Description,Page,p.posting_date AS approved_date,b.posting_date as initiated_date,Flag,partPay,UserId,Branch,  0.00 AS cost_price,branch_Id, " 
+" subjectToVat,whTax,operation1,exitPage,url,trans_id,p.asset_code,p.posting_date  from am_raisentry_post p," 
+" dbo.GROUP_IMPROVEMENT m, am_asset_approval b  where GroupIdStatus = 'N'  and id in ( select convert(varchar ,REVALUE_ID)" 
+" from dbo.GROUP_IMPROVEMENT) and id in ( select" 
+" convert(varchar ,REVALUE_ID) from GROUP_IMPROVEMENT)  and id = convert(varchar ,REVALUE_ID) and P.ID = b.BATCH_ID "+Filter
+" UNION "  //**************
+" select Id,p.Description,Page,p.posting_date AS approved_date,b.posting_date as initiated_date,Flag,partPay,UserId,m.Branch,  0.00 AS cost_price,m.branch_Id, " 
+" subjectToVat,whTax,operation1,exitPage,url,trans_id,p.asset_code,p.posting_date  from am_raisentry_post p," 
+" dbo.GROUP_ASSET_UPLOAD m, am_asset_approval b  where GroupIdStatus = 'N'  and id in ( select convert(varchar ,GROUP_ID) " 
+" from dbo.GROUP_ASSET_UPLOAD) and id in ( select" 
+" convert(varchar ,GROUP_ID) from GROUP_ASSET_UPLOAD)  and id = convert(varchar ,GROUP_ID) and P.ID = b.BATCH_ID AND"
+ " Page != 'ASSET GROUP CREATION RAISE ENTRY' "+Filter
+" UNION " //*****************
+" select DISTINCT Id,p.Description,Page,p.posting_date AS approved_date,b.posting_date as initiated_date,Flag,partPay,UserId,Branch, cost_price,'' AS branch_id," 
+" subjectToVat,whTax,operation1,exitPage,url,trans_id,'0' as asset_code,p.posting_date  " 
+" from am_raisentry_post p, FT_MAINTENANCE_HISTORY a, am_asset_approval b " 
+" where p.ID = a.HIST_ID AND p.entryPostFlag='N' AND a.STATUS = 'APPROVED' and P.ID = b.BATCH_ID "+ Filter 
+" UNION "
+" select DISTINCT Id,p.Description,Page,p.posting_date AS approved_date,b.posting_date as initiated_date,Flag,partPay,UserId,Branch, cost_price,'' AS branch_id," 
+" subjectToVat,whTax,operation1,exitPage,url,trans_id,'0' as asset_code,p.posting_date  " 
+" from am_raisentry_post p, FT_LICENCE_HISTORY a, am_asset_approval b " 
+" where p.ID = a.HIST_ID AND p.entryPostFlag='N' AND a.STATUS = 'APPROVED' and P.ID = b.BATCH_ID "+ Filter 
+" UNION "
+" select DISTINCT Id,p.Description,Page,p.posting_date AS approved_date,b.posting_date as initiated_date,Flag,partPay,UserId,Branch, cost_price,'' AS branch_id," 
+" subjectToVat,whTax,operation1,exitPage,url,trans_id,'0' as asset_code,p.posting_date  " 
+" from am_raisentry_post p, FT_FUEL_HISTORY a, am_asset_approval b " 
+" where p.ID = a.HIST_ID AND p.entryPostFlag='N' AND a.STATUS = 'APPROVED' and P.ID = b.BATCH_ID "+ Filter 
+" UNION "
+" select Id,p.Description,Page,p.posting_date AS approved_date,b.posting_date as initiated_date,Flag,partPay,UserId,p.Branch,m.cost_price,m.branch_Id, " 
+" subjectToVat,whTax,operation1,exitPage,url,trans_id,p.asset_code,p.posting_date  from am_raisentry_post p," 
+" dbo.FT_GROUP_DUE_PERIOD m, am_asset_approval b  where GroupIdStatus = 'N'  and id in ( select convert(varchar ,GROUP_ID)" 
+" from dbo.FT_GROUP_DUE_PERIOD) and id in ( select" 
+" convert(varchar ,GROUP_ID) from FT_GROUP_DUE_PERIOD)  and id = convert(varchar ,GROUP_ID) and P.ID = b.BATCH_ID  "+Filter
+" UNION " 
+" select Id,p.Description,Page,p.posting_date AS approved_date,b.posting_date as initiated_date,Flag,partPay,UserId,Branch, cost_price,branch_id, "
+" subjectToVat,whTax,operation1,exitPage,url,trans_id,p.asset_code,p.posting_date  from am_raisentry_post p, dbo.am_group_Stock_main m, am_asset_approval b "
+" where GroupIdStatus = 'N'  and id in ( select convert(varchar ,group_id) from dbo.am_group_stock_main) and id in ( select "
+" convert(varchar ,group_id) from AM_GROUP_STOCK)  and id = convert(varchar ,group_id) and convert(varchar ,P.TRANS_ID) = b.BATCH_ID "+ Filter
+" UNION "
+" select Id,p.Description,Page,p.posting_date AS approved_date,b.posting_date as initiated_date,Flag,partPay,UserId,Branch, cost_price,branch_id, " 
+" subjectToVat,whTax,operation1,exitPage,url,trans_id,a.asset_code,p.posting_date " 
+" from am_raisentry_post p, FT_MAINTENANCE_HISTORY a, am_asset_approval b " 
+" where ID = a.HIST_ID  AND p.entryPostFlag='N' and P.ID = b.ASSET_ID "+ Filter
+" UNION "
+" select Id,p.Description,Page,p.posting_date AS approved_date,b.posting_date as initiated_date,Flag,partPay,UserId,Branch, cost_price,branch_id, " 
+" subjectToVat,whTax,operation1,exitPage,url,trans_id,a.asset_code,p.posting_date " 
+" from am_raisentry_post p, FM_MAINTENANCE_HISTORY a, am_asset_approval b " 
+" where ID = a.HIST_ID  AND p.entryPostFlag='N' and P.ID = b.ASSET_ID "+ Filter
+" UNION "
+" select DISTINCT Id,p.Description,Page,p.posting_date AS approved_date,m.TRANSFER_DATE as initiated_date,Flag,partPay,UserId,Branch, cost_price,newbranch_id, " 
+" subjectToVat,whTax,operation1,exitPage,url,trans_id,p.asset_code,p.posting_date  from am_raisentry_post p, dbo.am_gb_bulkStocktransfer m  " 
+" where GroupIdStatus = 'N'  and id in ( select convert(varchar ,Batch_id) from dbo.am_gb_bulkStocktransfer) " 
+" and id in ( select convert(varchar ,Batch_id) from am_gb_bulkStocktransfer)  and id = convert(varchar ,Batch_id) " 
+Filter
+" UNION "
+" select p.Id,p.Description,Page,p.posting_date AS approved_date,b.posting_date as initiated_date,Flag,partPay,UserId,Branch, a.cost_price,0 AS branch_id, "
+" subjectToVat,whTax,operation1,exitPage,url,trans_id,'' AS asset_code,p.posting_date "
+" from am_raisentry_post p, FM_PAYMENT_TRANS_VIEW a, am_asset_approval b "
+" where p.ID = a.ReqnID  AND p.entryPostFlag='N' and P.ID = b.ASSET_ID and b.tran_type = 'Facility Mgt Work Completion' "
+Filter
+" UNION "
+" select p.Id,p.Description,Page,p.posting_date AS approved_date,b.posting_date as initiated_date,Flag,partPay,UserId,Branch, cost_price,branch_id, " 
+" subjectToVat,whTax,operation1,exitPage,url,trans_id,'0' AS asset_code,p.posting_date " 
+" from am_raisentry_post p, FM_SOCIALENVIRONMENT_SUMMARY a, am_asset_approval b " 
+" where p.ID = a.HIST_ID  AND p.entryPostFlag='N' and p.ID = b.ASSET_ID "+ Filter;
Finder_qry_Last = Finder_qry_Last + " " + ordering;
        Finder_qry ="SELECT *FROM raiseentryTransactionView WHERE ID IS NOT NULL AND INITIATED_DATE IS NOT NULL " + Filter+ " " + ordering;        
 //       System.out.println("\n\n Finder_qry in findApprovalInitiator5():>>>>>  " + Finder_qry);
        try 
        {
//            con = getConnection("legendPlus"); 
//            ps = con.prepareStatement(Finder_qry);
//            rs = ps.executeQuery();

            con = getConnection("legendPlus");
            ps = con.prepareStatement(Finder_qry.toString());  
          if(Filter.contains("POSTING_DATE")){  
//        	  System.out.println("<========getAssetByQuery=======>0 FromDate: "+FromDate+"     ToDate: "+ToDate);
        	  ps.setString(1, FromDate);
        	  ps.setString(2, ToDate);
          }     
          if(Filter.contains("USERID") && Filter.contains("id") && Filter.contains("POSTING_DATE")){  
//        	  System.out.println("<========getAssetByQuery=======>0 FromDate: "+FromDate+"     ToDate: "+ToDate);
        	  ps.setString(1, User_Name);
        	  ps.setString(2, assetId);
        	  ps.setString(3, FromDate);
        	  ps.setString(4, ToDate);
          } 
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
                int tranID = rs.getInt("trans_id");
                int assetCode = rs.getInt("asset_code");      
                String approvedDate = rs.getString("approved_date");
                String initiateDate = rs.getString("initiated_date");
                app = new Approval(id, Description, Page, Flag, partPay, UserId, Branch, subjectToVat, whTax, operation1, exitPage, url);
                app.setTranID(tranID);
//                System.out.println("findApprovalInitiator5 tranID:   "+tranID+"  UserId: "+UserId);
                app.setAssetCreator(getTransactionInitiatorByTranID(tranID,UserId, id));
//                System.out.println("getTransactionInitiatorByTranID Done: ");
                app.setAssetCode(assetCode);
                app.setPosting_date(initiateDate);
                app.setApproved_date(approvedDate);
                collection.add(app);
            }


        } catch (Exception ex) {
            System.out.println("findApprovalInitiator5(): WARNING: cannot fetch [ApprovalRecords]->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }  
        return collection;
    }

    public ArrayList findApprovalInitiator5Opex(String Filter,String apprvLimit_min,String apprvLimit_max,String branch_id,String fromDate,String toDate,String User_Name,String Id)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        ArrayList collection;
        String Finder_qry;
        con = null;
        ps = null; 
        rs = null;
        Approval app = null;
        collection = new ArrayList();
        Finder_qry ="SELECT *FROM raiseentryTransactionViewOpex WHERE ID IS NOT NULL AND INITIATED_DATE IS NOT NULL"+Filter;        
//        System.out.println("\n\n Finder_qry in findApprovalInitiator5Opex():>>>>>  " + Finder_qry);
//        System.out.println("\n\n Filter in findApprovalInitiator5Opex():>>>>>  " + Filter);
        try 
        {  
            con = getConnection("legendPlus");
            ps = con.prepareStatement(Finder_qry.toString());
            //query_ = query_.replaceAll("\\s", "");
            if(Filter.contains("cost_price") && Filter.contains("BRANCH_ID") && Filter.contains("POSTING_DATE")){
           // 	System.out.println("=======cost_price, BRANCH_ID, POSTING_DATE ======");
          	  ps.setString(1, apprvLimit_min);
          	  ps.setString(2, apprvLimit_max);
          	  ps.setString(3, branch_id);
          	  ps.setString(4, fromDate); 
          	  ps.setString(5, toDate);
//          	  ps.setString(5, ordering);
            }
            if(Filter.contains("cost_price")){
           // 	System.out.println("=======cost_price ======");
          	  ps.setString(1, apprvLimit_min);
          	  ps.setString(2, apprvLimit_max);
//          	  ps.setString(3, fromDate);
//          	  ps.setString(4, toDate);
//          	  ps.setString(5, ordering);
            }            
            if(Filter.contains("cost_price") && Filter.contains("POSTING_DATE")){
            //	System.out.println("=======cost_price, POSTING_DATE ======cost_price: "+Filter.contains("cost_price")+"   POSTING_DATE: "+Filter.contains("POSTING_DATE")+"   USERID: "+Filter.contains("USERID")+"  ID: "+Filter.contains("id")+"  BRANCH_ID: "+Filter.contains("BRANCH_ID"));
            	  ps.setString(1, apprvLimit_min);
            	  ps.setString(2, apprvLimit_max);
            	  ps.setString(3, fromDate);
            	  ps.setString(4, toDate);
//            	  ps.setString(5, ordering); 
              }
            if(Filter.contains("cost_price") && Filter.contains("id") && Filter.contains("POSTING_DATE")){
            //	System.out.println("=======cost_price, POSTING_DATE and ID ======");
          	  ps.setString(1, apprvLimit_min);
          	  ps.setString(2, apprvLimit_max);
          	  ps.setString(3, Id);
          	  ps.setString(4, fromDate);
          	  ps.setString(5, toDate);
            }      
            if(Filter.contains("cost_price") && Filter.contains("POSTING_DATE") && Filter.contains("USERID")){
            //	System.out.println("=======cost_price, POSTING_DATE and USERID  ======");
            	  ps.setString(1, apprvLimit_min);
            	  ps.setString(2, apprvLimit_max);
            	  ps.setString(3, User_Name);
            	  ps.setString(4, fromDate);
            	  ps.setString(5, toDate);
              }  
            if(Filter.contains("cost_price") && Filter.contains("BRANCH_ID") && Filter.contains("POSTING_DATE") && Filter.contains("USERID")){
            //	System.out.println("=======cost_price, BRANCH_ID, POSTING_DATE and USERID ======");
          	  ps.setString(1, apprvLimit_min);
          	  ps.setString(2, apprvLimit_max);
          	  ps.setString(3, branch_id);
          	  ps.setString(4, User_Name);
          	  ps.setString(5, fromDate);
          	  ps.setString(6, toDate);
            } 
            if(Filter.contains("cost_price") && Filter.contains("POSTING_DATE") && Filter.contains("USERID") && Filter.contains("id")){
           // 	System.out.println("=======cost_price, POSTING_DATE, USERID and ID ======");
          	  ps.setString(1, apprvLimit_min);
          	  ps.setString(2, apprvLimit_max);
          	  ps.setString(3, User_Name);
          	  ps.setString(4, Id);
          	  ps.setString(5, fromDate);
          	  ps.setString(6, toDate);
            }
            if(Filter.contains("cost_price") && Filter.contains("BRANCH_ID") && Filter.contains("POSTING_DATE") && Filter.contains("ID")){
            //	System.out.println("=======cost_price, BRANCH_ID, POSTING_DATE and ID ======");
          	  ps.setString(1, apprvLimit_min);
          	  ps.setString(2, apprvLimit_max);
          	  ps.setString(3, branch_id);
          	  ps.setString(4, Id);
          	  ps.setString(5, fromDate);
          	  ps.setString(6, toDate);  
            }
            if(Filter.contains("cost_price") && Filter.contains("BRANCH_ID") && Filter.contains("USERID") && Filter.contains("id") && Filter.contains("POSTING_DATE")){
            //	System.out.println("=======cost_price, BRANCH_ID, POSTING_DATE, USERID and ID ======");
            	  ps.setString(1, apprvLimit_min);
            	  ps.setString(2, apprvLimit_max);
            	  ps.setString(3, branch_id);
            	  ps.setString(4, User_Name);
            	  ps.setString(5, Id);
            	  ps.setString(6, fromDate);
            	  ps.setString(7, toDate);
              }
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
                int tranID = rs.getInt("trans_id");
                int assetCode = rs.getInt("asset_code");      
                String approvedDate = rs.getString("approved_date");
                String initiateDate = rs.getString("initiated_date");
                app = new Approval(id, Description, Page, Flag, partPay, UserId, Branch, subjectToVat, whTax, operation1, exitPage, url);
                app.setTranID(tranID);
//                System.out.println("findApprovalInitiator5 tranID:   "+tranID+"  UserId: "+UserId);
                app.setAssetCreator(getTransactionInitiatorByTranID(tranID, UserId,id));
//                System.out.println("getTransactionInitiatorByTranID Done: ");
                app.setAssetCode(assetCode);
                app.setPosting_date(initiateDate);
                app.setApproved_date(approvedDate);
                collection.add(app);
            }


        } catch (Exception ex) {
            System.out.println("findApprovalInitiator5Opex(): WARNING: cannot fetch [ApprovalRecords]->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }  
        return collection;
    }


    public boolean insertApprovalx(String id, String description, String page, String flag, String partPay, String UserId, String Branch, 
            String subjectToVat, String whTax, String url, int assetCode)
    {
        boolean done;
        Connection con;
        PreparedStatement ps;
        String query;
        done = true;
        flag = "Y";
        con = null;
        ps = null;
        query = "INSERT INTO [am_raisentry_post](Id,Description,Page,Flag,partPay,UserId,Branch,s" +
"ubjectToVat,whTax,url,asset_code,POSTING_DATE) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)"
;
        try
        {
            con = getConnection("legendPlus");
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
            ps.setInt(11, assetCode);
//            System.out.println("<<<<<getDate: "+new java.util.Date());
            Timestamp approveddate =  dbConnection.getDateTime(new java.util.Date());
//            System.out.println("<<<<<approveddate: "+approveddate);
            ps.setTimestamp(12, approveddate);
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

    public boolean insertApprovalx(String id, String description, String page, String flag, String partPay, String UserId, String Branch, 
            String subjectToVat, String whTax, String url, int transID, int assetCode)
    {
        boolean done;
        Connection con;
        PreparedStatement ps;
        String query;
        done = true;
        flag = "Y";
        con = null;
        ps = null;
        query = "INSERT INTO [am_raisentry_post](Id,Description,Page,Flag,partPay,UserId,Branch,s" +
"ubjectToVat,whTax,url,trans_id,asset_code,Posting_date,entryPostFlag,GroupIdStatus) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
;
//        System.out.println("<<<<<<<<<<<assetCode in insertApprovalx: "+assetCode);
        try
        {
            con = getConnection("legendPlus");
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
            ps.setInt(11, transID);
            ps.setInt(12, assetCode);
         //   ps.setString(13, String.valueOf(df.dateConvert(new java.util.Date())));
        //    ps.setTimestamp(13, dbConnection.getDateTime(new java.util.Date()));
 //           System.out.println("<<<<<getDate: "+new java.util.Date());
            Timestamp approveddate =  dbConnection.getDateTime(new java.util.Date());
  //          System.out.println("<<<<<approveddate: "+approveddate);
            ps.setTimestamp(13, approveddate);
            ps.setString(14, "N");
            ps.setString(15, "N");
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

    public boolean insertApproval2x(String id, String description, String page, String flag, String partPay, String UserId, String Branch, 
            String subjectToVat, String whTax, String url, int transID, int assetCode)
    {
        boolean done;
        Connection con;
        PreparedStatement ps;
        String query;
        done = true;
        flag = "Y";
        con = null;
        ps = null;
        query = "INSERT INTO [am_raisentry_post](Id,Description,Page,Flag,partPay,UserId,Branch,s" +
"ubjectToVat,whTax,url,trans_id,entryPostFlag,asset_code,Posting_date) VALUES(?,?,?,?,?,?,?,?,?," +
"?,?,?,?,?)"
;
        try
        {
            con = getConnection("legendPlus");
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
            ps.setInt(11, transID);
            ps.setString(12, "N");
            ps.setInt(13, assetCode); 
          //  ps.setString(14, String.valueOf(df.dateConvert(new java.util.Date())));
//            System.out.println("<<<<<getDate: "+new java.util.Date());
            Timestamp approveddate =  dbConnection.getDateTime(new java.util.Date());
 //           System.out.println("<<<<<approveddate: "+approveddate);
            ps.setTimestamp(14, approveddate);
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

    public boolean insertApprovalx(String id, String description, String page, String flag, String partPay, String UserId, String Branch, 
            String subjectToVat, String whTax, String url, String tranId, int assetCode)
    {
        boolean done;
        Connection con;
        PreparedStatement ps;
        String query;
        done = true;
        flag = "Y";
        con = null;
        ps = null;
        query = "INSERT INTO [am_raisentry_post](Id,Description,Page,Flag,partPay,UserId,Branch,s" +
"ubjectToVat,whTax,url,trans_id,asset_code,Posting_date,entryPostFlag,GroupIdStatus) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
;
        try
        {
            con = getConnection("legendPlus");
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
            ps.setString(11, tranId);
            ps.setInt(12, assetCode);
   //         ps.setString(13, String.valueOf(df.dateConvert(new java.util.Date())));
//            System.out.println("<<<<<getDate: "+new java.util.Date());
            Timestamp approveddate =  dbConnection.getDateTime(new java.util.Date());
  //          System.out.println("<<<<<approveddate: "+approveddate);
            ps.setTimestamp(13, approveddate);
            ps.setString(14, "N");
            ps.setString(15, "N");
            ps.execute();

        } catch (Exception ex) {
            done = false;
            System.out.println(
                    "WARNING:cannot insert am_raisentry_post in insertApprovalx ->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;
    }

    public ArrayList findApprovalInitiator6(String Filter,String branch_ID_Filter,String apprvLimit_min,String apprvLimit_max,String User_Name,String assetId,String FromDate,String ToDate, String ordering)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        ArrayList collection;
        String Finder_qry;
        con = null;
        ps = null;
        rs = null;
        Approval app = null;
        collection = new ArrayList();
String Finder_qryOld = (new StringBuilder(" select Id,p.Description,Page,p.posting_date AS approved_date,b.posting_date as initiated_date,Flag,partPay,UserId,Branch, cost_price,branch_id, " +
"subjectToVat,whTax,operation1,exitPage,url,trans_id,p.asset_code  from am_raisen" +
"try_post p, dbo.AM_GROUP_ASSET_UNCAPITALIZED m, am_asset_approval b   where GroupIdStatus = 'N'  and id in ( se" +
"lect convert(varchar ,m.group_id) from dbo.AM_GROUP_ASSET_UNCAPITALIZED) and id = convert" +
"(varchar ,m.group_id) and convert(varchar ,P.TRANS_ID) = b.BATCH_ID "
)).append(Filter).append(" UNION ").append(" select Id,p.Description,Page,p.posting_date AS approved_date,b.posting_date as initiated_date,Flag,partPay,UserId,Branch, cost_price,branch_id,").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id,a.asset_code ").append(" from am_raisentry_post p, AM_ASSET_UNCAPITALIZED a, am_asset_approval b ").append(" where ID = a.ASSET_ID ").append(" AND p.entryPostFlag='N' and convert(varchar ,P.TRANS_ID) = b.BATCH_ID ").append(Filter).append(" UNION ").append(" select Id,p.Description,Page,p.posting_date AS approved_date,b.posting_date as initiated_date,Flag,partPay,UserId,Branch, cost_price,branch_id, ").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id,a.asset_code ").append(" from am_raisentry_post p, AM_ASSET_UNCAPITALIZED a, am_asset_approval b ").append(" where ID = a.ASSET_ID ").append(" AND p.entryPostFlag='N' and convert(varchar ,P.TRANS_ID) = b.BATCH_ID ").append(Filter).append("UNION ").append(" select Id,p.Description,Page,p.posting_date AS approved_date,b.posting_date as initiated_date,Flag,partPay,UserId,Branch, cost_price,branch_id, ").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id,a.asset_code ").append(" from am_raisentry_post p, AM_ASSET_UNCAPITALIZED a, am_asset_approval b ").append(" where p.page='UNCAPITALISED ASSET TRANSFER RAISE ENTRY' AND p.entryPostFlag='N'" +
" "
).append(" and p.asset_code=a.asset_code and convert(varchar ,P.TRANS_ID) = b.BATCH_ID ").append(Filter).toString();
Finder_qryOld = (new StringBuilder(String.valueOf(Finder_qryOld))).append(" ").append(ordering).toString();
        
        Finder_qry = "SELECT *FROM UncapitalizedRaiseEntry WHERE ID IS NOT NULL "+Filter;
//        System.out.println("findApprovalInitiator6 "+Finder_qry+"   ====apprvLimit_min: "+apprvLimit_min+"      apprvLimit_max: "+apprvLimit_max+"   FromDate: "+FromDate+"   ToDate: "+ToDate+"    ordering: "+ordering); 
        try
        {
//            con = getConnection("legendPlus");
//            ps = con.prepareStatement(Finder_qry);
//            rs = ps.executeQuery();
        	
            con = getConnection("legendPlus");
            ps = con.prepareStatement(Finder_qry.toString());
          if(Filter.contains("POSTING_DATE")){  
//        	  System.out.println("<========getAssetByQuery=======>0 FromDate: "+FromDate+"     ToDate: "+ToDate);
        	  ps.setString(1, FromDate);
        	  ps.setString(2, ToDate);
          }     
          if(Filter.contains("USERID") && Filter.contains("id") && Filter.contains("POSTING_DATE")){  
 //       	  System.out.println("<========getAssetByQuery=======>1 FromDate: "+FromDate+"     ToDate: "+ToDate);
        	  ps.setString(1, User_Name);
        	  ps.setString(2, assetId);
        	  ps.setString(3, FromDate);
        	  ps.setString(4, ToDate);
          } 
          if(Filter.contains("cost_price") && Filter.contains("approved_date") ){  
//        	  System.out.println("<========getAssetByQuery=======>2 apprvLimit_min: "+apprvLimit_min+"     apprvLimit_max: "+apprvLimit_max);
        	  ps.setString(1, apprvLimit_min);
        	  ps.setString(2, apprvLimit_max);
        	  ps.setString(3, FromDate);
        	  ps.setString(4, ToDate);
          } 
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
                int tranID = rs.getInt("trans_id");
                int assetCode = rs.getInt("asset_code");
                String approvedDate = rs.getString("approved_date");
                String initiateDate = rs.getString("initiated_date");                
                app = new Approval(id, Description, Page, Flag, partPay, UserId, Branch, subjectToVat, whTax, operation1, exitPage, url);
                app.setTranID(tranID);
                app.setAssetCode(assetCode);
                app.setPosting_date(initiateDate);
                app.setApproved_date(approvedDate);
                app.setAssetCreator(getTransactionInitiatorByTranID(tranID, UserId,id));
                collection.add(app);
            }

        } catch (Exception ex) {
            System.out.println("findApprovalInitiator6(): WARNING: cannot fetch [ApprovalRecords]->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return collection;
    }

    public ArrayList findApprovalInitiator7(String Filter, String ordering)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        ArrayList collection;
        String Finder_qry;
        con = null;
        ps = null;
        rs = null;
        Approval app = null;
        collection = new ArrayList();
        String Finder_qry_OLD = (new StringBuilder(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, " +
"subjectToVat,whTax,operation1,exitPage,url,trans_id  from am_raisentry_post p, d" +
"bo.am_group_asset_main m   where GroupIdStatus = 'N'  and id in ( select convert" +
"(varchar ,group_id) from dbo.am_group_asset_main) and id = convert(varchar ,grou" +
"p_id) "
)).append(Filter).append(" UNION ").append(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id,").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id ").append(" from am_raisentry_post p, am_asset a ").append(" where id in (select asset_id from am_raisentry_transaction where iso <> '000') ").append(" AND ID = ASSET_ID ").append(Filter).append(" UNION ").append(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, ").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id ").append(" from am_raisentry_post p, am_asset a ").append(" where  id not in (select asset_id from am_raisentry_transaction) ").append(" AND ID = ASSET_ID ").append(Filter).append(" UNION ").append(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id,").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id  ").append(" from am_raisentry_post p, am_asset a  ").append(" where ID = a.OLD_ASSET_ID ").append(" and a.asset_id not in (select asset_id from am_raisentry_transaction) ").append(Filter).toString();
        String Finder_qry_old2 = (new StringBuilder(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, " +
"subjectToVat,whTax,operation1,exitPage,url,trans_id  from am_raisentry_post p, d" +
"bo.am_group_asset_main m  where GroupIdStatus = 'N'  and id in ( select convert(" +
"varchar ,group_id) from dbo.am_group_asset_main) and id = convert(varchar ,group" +
"_id) "
)).append(Filter).append(" UNION ").append(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id,").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id ").append(" from am_raisentry_post p, am_asset a ").append(" where id in (select asset_id from am_raisentry_transaction where iso <> '000') ").append(" AND ID = ASSET_ID ").append(Filter).append(" UNION ").append(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, ").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id ").append(" from am_raisentry_post p, am_asset a ").append(" where  id not in (select asset_id from am_raisentry_transaction) ").append(" AND ID = ASSET_ID ").append(Filter).append(" UNION ").append(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id,").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id  ").append(" from am_raisentry_post p, am_asset a  ").append(" where ID = a.OLD_ASSET_ID ").append(" and a.asset_id not in (select asset_id from am_raisentry_transaction) ").append(Filter).append("UNION ").append(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, ").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id ").append(" from am_raisentry_post p, am_asset a ").append(" where p.page='Asset Improvement Raise Entry' AND p.entryPostFlag='N'").append(" and p.id=a.asset_id ").append(Filter).append("UNION ").append(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, ").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id ").append(" from am_raisentry_post p, am_asset a ").append(" where p.page='ASSET PART PAYMENT ENTRY' AND p.entryPostFlag='N'").append(" and p.id=a.asset_id ").append(Filter).append("UNION ").append(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, ").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id ").append(" from am_raisentry_post p, am_asset a ").append(" where p.page='ASSET DISPOSAL RAISE ENTRY' AND p.entryPostFlag='N'").append(" and p.id=a.asset_id ").append(Filter).toString();
        Finder_qry = (new StringBuilder(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, " +
"subjectToVat,whTax,operation1,exitPage,url,trans_id  from am_raisentry_post p, d" +
"bo.am_group_asset_main m  where GroupIdStatus = 'N'  and id in ( select convert(" +
"varchar ,group_id) from dbo.am_group_asset_main) and id in ( select convert(varc" +
"har ,group_id) from AM_GROUP_ASSET)  and id = convert(varchar ,group_id) "
)).append(Filter).append(" UNION ").append(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id,").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id ").append(" from am_raisentry_post p, am_asset a ").append(" where id in (select asset_id from am_raisentry_transaction where iso <> '000') ").append(" AND ID = ASSET_ID ").append(Filter).append(" UNION ").append(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, ").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id ").append(" from am_raisentry_post p, am_asset a ").append(" where  id not in (select asset_id from am_raisentry_transaction) ").append(" AND ID = ASSET_ID ").append(Filter).append(" UNION ").append(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id,").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id  ").append(" from am_raisentry_post p, am_asset a  ").append(" where ID = a.OLD_ASSET_ID ").append(" and a.asset_id not in (select asset_id from am_raisentry_transaction) ").append(Filter).append("UNION ").append(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, ").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id ").append(" from am_raisentry_post p, am_asset a ").append(" where p.page='Asset Improvement Raise Entry' AND p.entryPostFlag='N'").append(" and p.id=a.asset_id ").append(Filter).append("UNION ").append(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, ").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id ").append(" from am_raisentry_post p, am_asset a ").append(" where p.page='ASSET PART PAYMENT ENTRY' AND p.entryPostFlag='N'").append(" and p.id=a.asset_id ").append(Filter).append("UNION ").append(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, ").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id ").append(" from am_raisentry_post p, am_asset a ").append(" where p.page='ASSET DISPOSAL RAISE ENTRY' AND p.entryPostFlag='N'").append(" and p.id=a.asset_id ").append(Filter).toString();
        Finder_qry = (new StringBuilder(String.valueOf(Finder_qry))).append(" ").append(ordering).toString();
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(Finder_qry);
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
                int tranID = rs.getInt("trans_id");
                app = new Approval(id, Description, Page, Flag, partPay, UserId, Branch, subjectToVat, whTax, operation1, exitPage, url);
                app.setTranID(tranID);
                app.setAssetCreator(getTransactionInitiatorByTranID(tranID, UserId,id));
                collection.add(app);
            }

        } catch (Exception ex) {
            System.out.println("findApprovalInitiator7(): WARNING: cannot fetch [ApprovalRecords]->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return collection;
    }

    public ArrayList findPostedEntry2(String Filter, String ordering)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        ArrayList collection;
        String Finder_qry;
        con = null;
        ps = null;
        rs = null;
        Approval app = null;
        collection = new ArrayList();
        Finder_qry = (new StringBuilder(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, " +
"subjectToVat,whTax,operation1,exitPage,url,trans_id  from am_raisentry_post p, d" +
"bo.am_group_asset_main m  where GroupIdStatus = 'Y'  and id in ( select convert(" +
"varchar ,group_id) from dbo.am_group_asset_main) and id in ( select convert(varc" +
"har ,group_id) from AM_GROUP_ASSET)  and id = convert(varchar ,group_id) "
)).append(Filter).append(" UNION ").append(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id,").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id ").append(" from am_raisentry_post p, am_asset a ").append(" where id in (select asset_id from am_raisentry_transaction where iso = '000') ").append(" AND ID = ASSET_ID ").append(Filter).toString();
        Finder_qry = (new StringBuilder(String.valueOf(Finder_qry))).append(" ").append(ordering).toString();
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(Finder_qry);
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
                int tranID = rs.getInt("trans_id");
                app = new Approval(id, Description, Page, Flag, partPay, UserId, Branch, subjectToVat, whTax, operation1, exitPage, url);
                app.setAssetCreator(getTransactionInitiatorByTranID(tranID, UserId,id));
                collection.add(app);
            }

        } catch (Exception ex) {
            System.out.println("findPostedEntry2(,): WARNING: cannot fetch [ApprovalRecords] in findPostedEntry2->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return collection;
    }

    public ArrayList findPostedEntryBranch(String Filter,String apprvLimit_min,String apprvLimit_max,String branch_id,String fromDate,String toDate,String User_Name,String Id,String ordering)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        ArrayList collection;
        String Finder_qry;
        con = null;
        ps = null;
        rs = null;
        Approval app = null;
        collection = new ArrayList();
        Finder_qry = (new StringBuilder(" select Id,p.Description,Page,p.posting_date AS approved_date,b.posting_date as initiated_date,Flag,partPay,UserId,Branch, cost_price,branch_id, " +
"subjectToVat,whTax,operation1,exitPage,url,trans_id  from am_raisentry_post p, d" +
"bo.am_group_asset_main m, am_asset_approval b  where GroupIdStatus = 'Y'  and id in ( select convert(" +
"varchar ,group_id) from dbo.am_group_asset_main) and id in ( select convert(varc" +
"har ,group_id) from AM_ASSET_UNCAPITALIZED)  and id = convert(varchar ,group_id) and convert(varchar ,P.TRANS_ID) = b.BATCH_ID " +
" "
)).append(Filter).append(" UNION ").append(" select Id,p.Description,Page,p.posting_date AS approved_date,b.posting_date as initiated_date,Flag,partPay,UserId,Branch, cost_price,branch_id,").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id ").append(" from am_raisentry_post p, AM_ASSET_UNCAPITALIZED a, am_asset_approval b ").append(" where id in (select asset_id from am_raisentry_transaction where iso = '000') ").append(" AND ID = a.ASSET_ID and convert(varchar ,P.TRANS_ID) = b.BATCH_ID ").append(Filter).toString();
        Finder_qry = (new StringBuilder(String.valueOf(Finder_qry))).append(" ").append(ordering).toString();
//        System.out.println("findPostedEntryBranch "+Finder_qry); 
        try
        {
//            con = getConnection("legendPlus");
//            ps = con.prepareStatement(Finder_qry);
//            int tranID;
//            rs = ps.executeQuery();

            con = getConnection("legendPlus");
            ps = con.prepareStatement(Finder_qry.toString());
            //query_ = query_.replaceAll("\\s", "");
            if(Filter.contains("cost_price") && Filter.contains("branch_id") && Filter.contains("p.posting_DATE")){
//            	System.out.println("=======cost_price, BRANCH_ID, p.posting_DATE ======");
          	  ps.setString(1, apprvLimit_min);
          	  ps.setString(2, apprvLimit_max);
          	  ps.setString(3, branch_id);
          	  ps.setString(4, fromDate);
          	  ps.setString(5, toDate);
          	  ps.setString(6, fromDate);
          	  ps.setString(7, toDate);
          	  ps.setString(86, apprvLimit_min);
          	  ps.setString(9, apprvLimit_max);
          	  ps.setString(10, branch_id);
          	  ps.setString(11, fromDate);
          	  ps.setString(12, toDate);    
          	  ps.setString(13, fromDate);
          	  ps.setString(14, toDate);
            }
            if(Filter.contains("cost_price")){
           // 	System.out.println("=======cost_price ======");
          	  ps.setString(1, apprvLimit_min);
          	  ps.setString(2, apprvLimit_max);
//          	  ps.setString(3, fromDate);
//          	  ps.setString(4, toDate);
//          	  ps.setString(5, ordering);
            }            
            if(Filter.contains("cost_price") && Filter.contains("p.posting_DATE")){
//            	System.out.println("=======cost_price, p.posting_DATE ======cost_price: "+Filter.contains("cost_price")+"   POSTING_DATE: "+Filter.contains("p.posting_DATE")+"   USERID: "+Filter.contains("USERID")+"  ID: "+Filter.contains("id")+"  BRANCH_ID: "+Filter.contains("BRANCH_ID"));
            	  ps.setString(1, apprvLimit_min);
            	  ps.setString(2, apprvLimit_max);
            	  ps.setString(3, fromDate);
            	  ps.setString(4, toDate);
            	  ps.setString(5, fromDate);
            	  ps.setString(6, toDate);
            	  ps.setString(7, apprvLimit_min);
            	  ps.setString(8, apprvLimit_max);
            	  ps.setString(9, fromDate);
            	  ps.setString(10, toDate);
            	  ps.setString(11, fromDate);
            	  ps.setString(12, toDate);
              }
            if(Filter.contains("cost_price") && Filter.contains("id") && Filter.contains("p.posting_DATE")){
//            	System.out.println("=======cost_price, p.posting_DATE and ID ======");
        	  ps.setString(1, apprvLimit_min);
        	  ps.setString(2, apprvLimit_max);
        	  ps.setString(3, Id);
        	  ps.setString(4, fromDate);
        	  ps.setString(5, toDate);
        	  ps.setString(6, fromDate);
        	  ps.setString(7, toDate);
        	  ps.setString(8, apprvLimit_min);
        	  ps.setString(9, apprvLimit_max);
        	  ps.setString(10, Id);
        	  ps.setString(11, fromDate);
        	  ps.setString(12, toDate);  
        	  ps.setString(13, fromDate);
        	  ps.setString(14, toDate);
            }      
            if(Filter.contains("cost_price") && Filter.contains("p.posting_DATE") && Filter.contains("USERID")){
//            	System.out.println("=======cost_price, p.posting_DATE and USERID  ======");
          	  ps.setString(1, apprvLimit_min);
          	  ps.setString(2, apprvLimit_max);
          	  ps.setString(3, User_Name);
          	  ps.setString(4, fromDate);
          	  ps.setString(5, toDate);
          	  ps.setString(6, fromDate);
          	  ps.setString(7, toDate);
          	  ps.setString(8, apprvLimit_min);
          	  ps.setString(9, apprvLimit_max);
          	  ps.setString(10, User_Name);
          	  ps.setString(11, fromDate);
          	  ps.setString(12, toDate); 
          	  ps.setString(13, fromDate);
          	  ps.setString(14, toDate);
              }  
            if(Filter.contains("cost_price") && Filter.contains("BRANCH_ID") && Filter.contains("p.posting_DATE") && Filter.contains("USERID")){
            //	System.out.println("=======cost_price, BRANCH_ID, POSTING_DATE and USERID ======");
          	  ps.setString(1, apprvLimit_min);
          	  ps.setString(2, apprvLimit_max);
          	  ps.setString(3, branch_id);
          	  ps.setString(4, User_Name);
          	  ps.setString(5, fromDate);
          	  ps.setString(6, toDate);
            } 
            if(Filter.contains("cost_price") && Filter.contains("p.posting_DATE") && Filter.contains("USERID") && Filter.contains("id")){
           // 	System.out.println("=======cost_price, POSTING_DATE, USERID and ID ======");
          	  ps.setString(1, apprvLimit_min);
          	  ps.setString(2, apprvLimit_max);
          	  ps.setString(3, User_Name);
          	  ps.setString(4, Id);
          	  ps.setString(5, fromDate);
          	  ps.setString(6, toDate);
            }
            if(Filter.contains("cost_price") && Filter.contains("BRANCH_ID") && Filter.contains("p.posting_DATE") && Filter.contains("ID")){
            //	System.out.println("=======cost_price, BRANCH_ID, POSTING_DATE and ID ======");
          	  ps.setString(1, apprvLimit_min);
          	  ps.setString(2, apprvLimit_max);
          	  ps.setString(3, branch_id);
          	  ps.setString(4, Id);
          	  ps.setString(5, fromDate);
          	  ps.setString(6, toDate);  
            }
            if(Filter.contains("cost_price") && Filter.contains("BRANCH_ID") && Filter.contains("USERID") && Filter.contains("id") && Filter.contains("p.posting_DATE")){
            //	System.out.println("=======cost_price, BRANCH_ID, POSTING_DATE, USERID and ID ======");
            	  ps.setString(1, apprvLimit_min);
            	  ps.setString(2, apprvLimit_max);
            	  ps.setString(3, branch_id);
            	  ps.setString(4, User_Name);
            	  ps.setString(5, Id);
            	  ps.setString(6, fromDate);
            	  ps.setString(7, toDate);
              }
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
                int tranID = rs.getInt("trans_id");
                String approvedDate = rs.getString("approved_date");
                String initiateDate = rs.getString("initiated_date");   
                app = new Approval(id, Description, Page, Flag, partPay, UserId, Branch, subjectToVat, whTax, operation1, exitPage, url);
                app.setAssetCreator(getTransactionInitiatorByTranID(tranID, UserId,id));
                collection.add(app);
                app.setTranID(tranID);
                app.setPosting_date(initiateDate);
                app.setApproved_date(approvedDate);
            }

        } catch (Exception ex) {
            System.out.println("findPostedEntryBranch(,): WARNING: cannot fetch [ApprovalRecords] in findPostedEntryBranch->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return collection;
    }

    public ArrayList findApprovalInitiator8(String Filter, String ordering)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        ArrayList collection;
        String Finder_qry;
        con = null;
        ps = null;
        rs = null;
        Approval app = null;
        collection = new ArrayList();
        Finder_qry = (new StringBuilder(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, " +
"subjectToVat,whTax,operation1,exitPage,url,trans_id  from am_raisentry_post p, d" +
"bo.am_group_asset_main m  where GroupIdStatus = 'N'  and id in ( select convert(" +
"varchar ,group_id) from dbo.am_group_asset_main) and id = convert(varchar ,group" +
"_id) "
)).append(Filter).append(" UNION ").append(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id,").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id ").append(" from am_raisentry_post p, am_asset a ").append(" where id in (select asset_id from am_raisentry_transaction where iso <> '000') ").append(" AND ID = ASSET_ID ").append(Filter).append(" UNION ").append(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, ").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id ").append(" from am_raisentry_post p, am_asset a ").append(" where  id not in (select asset_id from am_raisentry_transaction) ").append(" AND ID = ASSET_ID ").append(Filter).append(" UNION ").append(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id,").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id  ").append(" from am_raisentry_post p, am_asset a  ").append(" where ID = a.OLD_ASSET_ID ").append(" and a.asset_id not in (select asset_id from am_raisentry_transaction) ").append(Filter).append("UNION ").append(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, ").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id ").append(" from am_raisentry_post p, am_asset a ").append(" where p.page='Asset Improvement Raise Entry' AND p.entryPostFlag='N'").append(" and p.id=a.asset_id ").append(Filter).append("UNION ").append(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, ").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id ").append(" from am_raisentry_post p, am_asset a ").append(" where p.page='ASSET PART PAYMENT ENTRY' AND p.entryPostFlag='N'").append(" and p.id=a.asset_id ").append(Filter).append("UNION ").append(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, ").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id ").append(" from am_raisentry_post p, am_asset a ").append(" where p.page='ASSET DISPOSAL RAISE ENTRY' AND p.entryPostFlag='N'").append(" and p.id=a.asset_id ").append(Filter).toString();
        Finder_qry = (new StringBuilder(String.valueOf(Finder_qry))).append(" ").append(ordering).toString();
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(Finder_qry);
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
                int tranID = rs.getInt("trans_id");
                app = new Approval(id, Description, Page, Flag, partPay, UserId, Branch, subjectToVat, whTax, operation1, exitPage, url);
                app.setTranID(tranID);
                app.setAssetCreator(getTransactionInitiatorByTranID(tranID, UserId,id));
                collection.add(app);
            }

        } catch (Exception ex) {
            System.out.println("findApprovalInitiator8(): WARNING: cannot fetch [ApprovalRecords]->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps, rs); 
        }
        return collection;
    }

    public boolean insertApprovalx2(String id, String description, String page, String flag, String partPay, String UserId, String Branch, 
            String subjectToVat, String whTax, String url, int transID, int assetCode)
    {
        boolean done;
        Connection con;
        PreparedStatement ps;
        String query; 
        done = true;
        flag = "Y";
        con = null;
        ps = null;
        query = "INSERT INTO [am_raisentry_post](Id,Description,Page,Flag,partPay,UserId,Branch,s" +
"ubjectToVat,whTax,url,trans_id,entryPostFlag,asset_code,POSTING_DATE,GroupIdStatus) VALUES(?,?,?,?,?,?,?,?," +
"?,?,?,?,?,?,?)"
;
        System.out.println("url in insertApprovalx2: "+url);
        try
        {
            con = getConnection("legendPlus");
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
            ps.setInt(11, transID);
            ps.setString(12, "N");
            ps.setInt(13, assetCode);
 //           System.out.println("<<<<<getDate: "+new java.util.Date());
            Timestamp approveddate =  dbConnection.getDateTime(new java.util.Date());
 //           System.out.println("<<<<<approveddate: "+approveddate);
            ps.setTimestamp(14, approveddate);
            ps.setString(15, "N");
            ps.execute();

        } catch (Exception ex) {
            done = false;
            System.out.println(
                    "WARNING:cannot insert am_raisentry_post in insertApprovalx2->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;
    }

    public void updateRaiseEntryBranch(String assetid, String status)
    {
        Connection con;
        PreparedStatement ps;
        String NOTIFY_QUERY;
        con = null;
        ps = null;
        NOTIFY_QUERY = "UPDATE am_asset_uncapitalized SET raise_entry = ? WHERE ASSET_ID = ?  ";
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(NOTIFY_QUERY);
            ps.setString(1, status);
            ps.setString(2, assetid);
            ps.executeUpdate();

        } catch (Exception ex) {
            System.out.println("WARNING: cannot update am_asset in updateRaiseEntryBranch +" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
    }

    public String retrieveArray(String query)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        String apprvLevel;
        String apprvLevelLimit;
        String image;
        con = null;
        ps = null;
        rs = null;
        apprvLevel = "";
        apprvLevelLimit = "";
        image = "";
        try
        {
            con = getConnection("legendPlus");
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
    public boolean clearTransactionEntry(String assetid, String page1, int tranId,String thirdPartyLabel)
    {
        Connection con;
        PreparedStatement ps;
        con = null;
        ps = null;
        boolean done;
        try
        {  
            String delTransactionEntry = (new StringBuilder("delete from  am_Raisentry_Transaction  WHERE   page1='")).append(page1).append("' and trans_id=").append(tranId).toString();
            deleteQuery(delTransactionEntry);
            if(page1 != null && !page1.equalsIgnoreCase("null"))
            {
                if(page1.equalsIgnoreCase("ASSET CREATION RAISE ENTRY") && page1.equalsIgnoreCase("Y"))
                {
                    deleteQuery((new StringBuilder("delete from am_asset where asset_id='")).append(assetid).append("'").toString());
                    deleteQuery((new StringBuilder("delete from am_asset_archive where asset_id='")).append(assetid).append("'").toString());
                    deleteQuery((new StringBuilder("delete from am_asset_approval where transaction_id=")).append(tranId).toString());
                }
                if(page1.equalsIgnoreCase("ASSET CREATION RAISE ENTRY") && page1.equalsIgnoreCase("N"))
                {
                 updateAssetStatus(assetid);
                }
                if(page1.equalsIgnoreCase("ASSET DISPOSAL RAISE ENTRY"))
                {
                    deleteQuery((new StringBuilder("delete from am_assetDisposal where asset_id='")).append(assetid).append("'").toString());
                    deleteQuery((new StringBuilder("delete from am_asset_approval where transaction_id=")).append(tranId).toString());
                }
                if(page1.equalsIgnoreCase("ASSET IMPROVEMENT RAISE ENTRY") && page1.equalsIgnoreCase("Y"))
                {
                	/*
                    deleteQuery((new StringBuilder("delete from am_asset_improvement where asset_id='")).append(assetid).append("'").toString());
                    deleteQuery((new StringBuilder("delete from am_raisentry_post where id='")).append(assetid).append("'").toString());
                    deleteQuery((new StringBuilder("delete from am_asset_approval where transaction_id=")).append(tranId).toString());
                    */
                    deleteQuery("delete from am_asset_improvement where Revalue_ID='" + tranId + "'");
                    deleteQuery("delete from am_raisentry_post where Trans_id='" + tranId + "'");
                    deleteQuery("delete from am_asset_approval where transaction_id=" + tranId);
                }  
                if(page1.equalsIgnoreCase("ASSET IMPROVEMENT RAISE ENTRY") && page1.equalsIgnoreCase("N"))
                {
                	updateAssetImprovement(tranId);
                }                
                page1.equalsIgnoreCase("WIP RECLASSIFICATION");
                page1.equalsIgnoreCase("ASSET RECLASSIFICATION RAISE ENTRY");
            }
            done = true;
        } catch (Exception ex) {
            done = false;
            System.out.println("WARNING: cannot delete clearTransactionEntry " + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;
    }

    public String userToEmail2(String groupId)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        String user_id;
        String FINDER_QUERY;
        con = null;
        ps = null;
        rs = null;
        user_id = "";
        FINDER_QUERY = (new StringBuilder("SELECT user_Id from am_group_asset WHERE group_Id = '")).append(groupId).append("' ").toString();
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);
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

    public void updateAssetWip(String asset_code)
    {
        int New_dept_id;
        int New_branch_id;
        String New_Asset_user;
        int New_Section;
        java.sql.Date effDate;
        String NEW_BRANCH_CODE;
        String NEW_SECTION_CODE;
        String NEW_DEPT_CODE;
        String asset_id;
        String new_asset_id;
        int new_cat_id;
        String new_cat_code;
        String selectQuery;
        String updateQuery;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        int w = 0;
        New_dept_id = 0;
        New_branch_id = 0;
        New_Asset_user = "";
        New_Section = 0;
        effDate = null;
        NEW_BRANCH_CODE = "";
        NEW_SECTION_CODE = "";
        NEW_DEPT_CODE = "";
        asset_id = "";
        new_asset_id = "";
        new_cat_id = 0;
        new_cat_code = "";
        selectQuery = (new StringBuilder(" select New_dept_id,New_branch_id,New_Asset_user,New_Section,  effDate,NEW_BRANC" +
"H_CODE,NEW_SECTION_CODE,NEW_DEPT_CODE,  asset_id,new_asset_id,new_cat_id,new_cat" +
"_code  from am_wip_reclassification where asset_code ='"
)).append(asset_code).append("' ").toString();
        updateQuery = (new StringBuilder(" update am_asset  set DEPT_ID=?, BRANCH_ID=?, section_id=?, asset_user=?,  BRANC" +
"H_CODE=?, DEPT_CODE=?, SECTION_CODE=?,CATEGORY_ID=?,CATEGORY_CODE=?,Effective_Da" +
"te=?,  asset_id=?, old_asset_id=?  where asset_code ='"
)).append(asset_code).append("' ").toString();
        con = null;
        ps = null;
        rs = null;
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {
                New_dept_id = rs.getInt(1);
                New_branch_id = rs.getInt(2);
                New_Asset_user = rs.getString(3);
                New_Section = rs.getInt(4);
                effDate = rs.getDate(5);
                NEW_BRANCH_CODE = rs.getString(6);
                NEW_SECTION_CODE = rs.getString(7);
                NEW_DEPT_CODE = rs.getString(8);
                asset_id = rs.getString(9);
                new_asset_id = rs.getString(10);
                new_cat_id = rs.getInt(11);
                new_cat_code = rs.getString(12);
            }

            ps = con.prepareStatement(updateQuery);
            ps.setInt(1, New_dept_id);
            ps.setInt(2, New_branch_id);
            ps.setInt(3, New_Section);
            ps.setString(4, New_Asset_user);
            ps.setString(5, NEW_BRANCH_CODE);
            ps.setString(6, NEW_DEPT_CODE);
            ps.setString(7, NEW_SECTION_CODE);
            ps.setInt(8, new_cat_id);
            ps.setString(9, new_cat_code);
            ps.setDate(10, effDate);
            ps.setString(11, new_asset_id);
            ps.setString(12, asset_id);
            w = ps.executeUpdate();

        } catch (Exception e) {
            String warning = " >>--------- Error updateWIP  information ->";
            e.printStackTrace();
        } finally {
            closeConnection(con, ps, rs);
        }
    }

    public String getGeneratedTransId()
    {
        return comp.getGeneratedTransId();
    }

    public String getFinacleRecords(String finacleTransId)
    {
        String iso;
        String query;
        Connection c;
        ResultSet rs;
        Statement s;
        String date = String.valueOf(df.dateConvert(new Date()));
        date = date.substring(0, 10);
        iso = "";
        query = (new StringBuilder(" SELECT  iso from finalceTable where transaction_date >'")).append(date).append("' and finacle_Trans_Id='").append(finacleTransId).append("' ").toString();
        c = null;
        rs = null;
        s = null;
        try
        {
            c = getConnection("FinacleDataHouse");
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                iso = rs.getString("iso");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(c, s, rs);
        }
        return iso;
    }

    public ArrayList getSqlRecords()
    {
        ArrayList _list;
        String query;
        Connection c;
        ResultSet rs;
        Statement s;
        _list = new ArrayList();
        String date = String.valueOf(df.dateConvert(new Date()));
        date = date.substring(0, 10);
        String finacleTransId = null;
        query = (new StringBuilder(" SELECT  finacle_Trans_Id from am_raisentry_transaction where transaction_date >" +
"'"
)).append(date).append("' and iso<>'000' ").toString();
        c = null;
        rs = null;
        s = null;
        try
        {
            c = getConnection("legendPlus");
            s = c.createStatement();
            String finacle_Trans_Id;
            rs = s.executeQuery(query);
            while (rs.next()) {
                finacle_Trans_Id = rs.getString("finacle_Trans_Id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(c, s, rs);
        }
        return _list;
    }

    public boolean updateSqlRecords(String iso, String finacleTransId)
    {
        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;
        String date = String.valueOf(df.dateConvert(new Date()));
        date = date.substring(0, 10);
        String query = (new StringBuilder("UPDATE am_raisentry_transaction SET iso=?   where transaction_date >'")).append(date).append("' and finacle_Trans_Id=? ").toString();
        try
        { 
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            ps.setString(1, iso);
            ps.setString(2, finacleTransId);
            done = ps.executeUpdate() != -1;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            closeConnection(con, ps);
        }
        return done;
    }

    public void updateRaiseEntryPostforGroup(String id, String page1, String transactionIdCost, String transactionIdVendor, String transactionIdWitholding)
    {
        String assetStatus;
        Connection con;
        PreparedStatement ps;
//        assetStatus = getCodeName((new StringBuilder("select asset_status from am_group_asset_main where GROUP_ID='")).append(id).append("'").toString());
        String param = "select asset_status from am_group_asset_main where asset_id= ?";
        assetStatus = getCodeName(param,id);
        con = null;
        ps = null;
        String query = "";
        try
        {
            AssetRecordsBean arb = new AssetRecordsBean();
            if(transactionIdCost.equalsIgnoreCase("000") && assetStatus.equalsIgnoreCase("APPROVED"))
            {
                query = "update am_raisentry_post set entrypostflag='Y' where id=? and page=? ";
//                System.out.println((new StringBuilder("here in >>>>>>>>>updateRaiseEntryPost--id--")).append(id).toString());
                arb.updateNewAssetStatus(id);
                arb.updateUncapitalizedNewAssetStatus(id);
                appManager = new ApprovalManager();
                appManager.infoFromApproval(id, page1);
                sendMailAfterPosting(id);
                sendMailAfterPostingToBranch(id);
            } else
            {
                query = "update am_raisentry_post set entrypostflag='N' where id=? and page=? ";
                String del_am_invoice_no_qry = (new StringBuilder("Delete from am_invoice_no where asset_id='")).append(id).append("'").append(" and trans_type='Asset Creation'").toString();
                updateUtil(del_am_invoice_no_qry);
            }
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            ps.setString(1, id);
            ps.setString(2, page1);
            ps.execute();

        } catch (Exception ex) {

            System.out.println("WARNING:cannot update am_raisentry_post->");
            ex.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
    }

    public void updateRaiseEntryAccelerated(String id, String page1, String TransactionIdISO, String transactionIdacceleratedCost, String transactionIdRemainlife, String transactionIdUsefullife, int tranId)
    {
        Connection con;
        PreparedStatement ps;
        double acceleratedCost;
        int usefulife;
        int remainlife;
        String check;
        con = null;
        ps = null;
        String query = "";
        acceleratedCost = Double.parseDouble(transactionIdacceleratedCost);
        usefulife = Integer.parseInt(transactionIdUsefullife);
        remainlife = Integer.parseInt(transactionIdRemainlife);
//        check = getCodeName((new StringBuilder("select  Accelerated_status from am_AcceleratedDepreciation  where Asset_id ='")).append(id).append("'").toString());
        String param = "select Accelerated_status from am_AcceleratedDepreciation where asset_id= ?";
        check = getCodeName(param,id);
        try
        {
            if(TransactionIdISO.equalsIgnoreCase("000") && check.equalsIgnoreCase("N"))
            {
                int result = updateUtil((new StringBuilder("update am_AcceleratedDepreciation set Raise_Entry ='Y', Accelerated_status='P' w" +
"here asset_id='"
)).append(id).append("'").toString());
                String query2 = (new StringBuilder("update am_asset set asset_status=?,date_disposed=?,NBV = NBV - ")).append(acceleratedCost).append(", Accum_Dep= Accum_Dep + ").append(acceleratedCost).append(", USEFUL_LIFE = ").append(usefulife).append(", REMAINING_LIFE = ").append(remainlife).append(" where asset_id=?").toString();
                con = getConnection("legendPlus");
                ps = con.prepareStatement(query2);
                ps.setString(1, "ACTIVE");
                ps.setDate(2, dateConvert(new Date()));
                ps.setString(3, id);
                ps.execute();
                appManager = new ApprovalManager();
                appManager.infoFromApproval(id, page1);
                query = (new StringBuilder("update am_raisentry_post set entrypostflag='Y' where id='")).append(id).append("' and page='").append(page1).append("'").toString();
                int i = updateUtil(query);
            }

        } catch (Exception ex) {

            System.out.println("WARNING:cannot update am_raisentry_post in updateRaiseEntryAccelerated->");
            ex.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
    }

    public ArrayList findApprovalInventory(String Filter, String ordering)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        ArrayList collection;
        String Finder_qry;
        con = null;
        ps = null;
        rs = null;
        Approval app = null;
        collection = new ArrayList();
Finder_qry = " select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, " +
"subjectToVat,whTax,operation1,exitPage,url,trans_id,p.asset_code  from am_raisen" +
"try_post p, dbo.am_group_asset_main m  where GroupIdStatus = 'N'  and id in ( se" +
"lect convert(varchar ,group_id) from dbo.am_group_asset_main) and id in ( select" +
" convert(varchar ,group_id) from AM_GROUP_ASSET)  and id = convert(varchar ,group_id) "+Filter
+" UNION "
+" select Id,a.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id," 
+" subjectToVat,whTax,operation1,exitPage,url,trans_id,a.asset_code " 
+" from am_raisentry_post p, ST_STOCK a " 
+" where ID = ASSET_ID AND p.entryPostFlag='N' "+ Filter 
+" UNION "
+" select Id,a.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, " 
+" subjectToVat,whTax,operation1,exitPage,url,trans_id,a.asset_code " 
+" from am_raisentry_post p, ST_STOCK a " 
+" where ID = ASSET_ID  AND p.entryPostFlag='N' "+ Filter 
+" UNION " 
+" select Id,a.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id," 
+" subjectToVat,whTax,operation1,exitPage,url,trans_id,a.asset_code  " 
+" from am_raisentry_post p, ST_STOCK a  " 
+" where ID = a.ASSET_ID AND p.entryPostFlag='N' "+ Filter
+" UNION "
+" select Id,a.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, "
+" subjectToVat,whTax,operation1,exitPage,url,trans_id,a.asset_code "
+" from am_raisentry_post p, ST_STOCK a "
+" where p.page='ASSET DISPOSAL RAISE ENTRY' AND p.entryPostFlag='N'"
+" and p.id=a.asset_id "+Filter;
        Finder_qry = Finder_qry + " " + ordering;
//        System.out.println("\n\n Finder_qry in findApprovalInventory():>>>>>  " + Finder_qry);
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(Finder_qry);
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
                String batchId = rs.getString("trans_id");
//                System.out.println("<<<<<batchId Value========: "+batchId);
                Integer x = Integer.valueOf(batchId);
 //               System.out.println("<<<<<<<<<<<<<<X Value========: "+x);
                int tranID = rs.getInt("trans_id");
                
                int assetCode = rs.getInt("asset_code");
                app = new Approval(id, Description, Page, Flag, partPay, UserId, Branch, subjectToVat, whTax, operation1, exitPage, url);
                app.setTranID(tranID);
//                System.out.println("findApprovalInitiator5 tranID:   "+tranID+"  UserId: "+UserId);
                app.setAssetCreator(getTransactionInitiatorByTranID(tranID, UserId,id));
//                System.out.println("getTransactionInitiatorByTranID Done: ");
                app.setAssetCode(assetCode);
                collection.add(app);
            }


        } catch (Exception ex) {
            System.out.println("findApprovalInventory(): WARNING: cannot fetch [ApprovalRecords]->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }  
        return collection;
    }

    public ArrayList findApprovalInitiatorforFleet(String Filter, String ordering)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        ArrayList collection;
        String Finder_qry;
        con = null;
        ps = null;
        rs = null;
        Approval app = null;
        collection = new ArrayList();
        String Finder_qry_OLD = (new StringBuilder(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, " +
"subjectToVat,whTax,operation1,exitPage,url,trans_id  from am_raisentry_post p, d" +
"bo.am_group_asset_main m  where GroupIdStatus = 'N'  and id in ( select convert(" +
"varchar ,group_id) from dbo.am_group_asset_main) and id = convert(varchar ,group" +
"_id) "
)).append(Filter).append(" UNION ").append(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id,").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id ").append(" from am_raisentry_post p, am_asset a ").append(" where id in (select asset_id from am_raisentry_transaction where iso <> '000') ").append(" AND ID = ASSET_ID ").append(" AND p.entryPostFlag='N' ").append(Filter).append(" UNION ").append(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, ").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id ").append(" from am_raisentry_post p, am_asset a ").append(" where  id not in (select asset_id from am_raisentry_transaction) ").append(" AND ID = ASSET_ID ").append(" AND p.entryPostFlag='N' ").append(Filter).append(" UNION ").append(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id,").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id  ").append(" from am_raisentry_post p, am_asset a  ").append(" where ID = a.OLD_ASSET_ID ").append(" AND p.entryPostFlag='N' ").append(" and a.asset_id not in (select asset_id from am_raisentry_transaction) ").append(Filter).toString();
        String Finder_qry_old2 = (new StringBuilder(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, " +
"subjectToVat,whTax,operation1,exitPage,url,trans_id  from am_raisentry_post p, d" +
"bo.am_group_asset_main m  where GroupIdStatus = 'N'  and id in ( select convert(" +
"varchar ,group_id) from dbo.am_group_asset_main) and id = convert(varchar ,group" +
"_id) "
)).append(Filter).append(" UNION ").append(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id,").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id ").append(" from am_raisentry_post p, am_asset a ").append(" where id in (select asset_id from am_raisentry_transaction where iso <> '000') ").append(" AND ID = ASSET_ID ").append(" AND p.entryPostFlag='N' ").append(Filter).append(" UNION ").append(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, ").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id ").append(" from am_raisentry_post p, am_asset a ").append(" where  id not in (select asset_id from am_raisentry_transaction) ").append(" AND ID = ASSET_ID ").append(" AND p.entryPostFlag='N' ").append(Filter).append(" UNION ").append(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id,").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id  ").append(" from am_raisentry_post p, am_asset a  ").append(" where ID = a.OLD_ASSET_ID ").append(" AND p.entryPostFlag='N' ").append(" and a.asset_id not in (select asset_id from am_raisentry_transaction) ").append(Filter).append("UNION ").append(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, ").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id ").append(" from am_raisentry_post p, am_asset a ").append(" where p.page='Asset Improvement Raise Entry' AND p.entryPostFlag='N'").append(" and p.id=a.asset_id ").append(Filter).append("UNION ").append(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, ").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id ").append(" from am_raisentry_post p, am_asset a ").append(" where p.page='ASSET PART PAYMENT ENTRY' AND p.entryPostFlag='N'").append(" and p.id=a.asset_id ").append(Filter).append("UNION ").append(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, ").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id ").append(" from am_raisentry_post p, am_asset a ").append(" where p.page='ASSET DISPOSAL RAISE ENTRY' AND p.entryPostFlag='N'").append(" and p.id=a.asset_id ").append(Filter).append("UNION ").append(" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, ").append(" subjectToVat,whTax,operation1,exitPage,url,trans_id ").append(" from am_raisentry_post p, am_asset a ").append(" where p.page='ACCELERATED DEPRECIATION RAISE ENTRY' AND p.entryPostFlag='N'").append(" and p.id=a.asset_id ").append(Filter).toString();

Finder_qry = " select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, " +
"subjectToVat,whTax,operation1,exitPage,url,trans_id,p.asset_code  from am_raisen" +
"try_post p, dbo.am_group_asset_main m  where GroupIdStatus = 'N'  and id in ( se" +
"lect convert(varchar ,group_id) from dbo.am_group_asset_main) and id in ( select" +
" convert(varchar ,group_id) from AM_GROUP_ASSET)  and id = convert(varchar ,group_id) "+Filter
+" UNION "
+" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id," 
+" subjectToVat,whTax,operation1,exitPage,url,trans_id,a.asset_code " 
+" from am_raisentry_post p, am_asset a " 
+" where ID = ASSET_ID AND p.entryPostFlag='N' "+ Filter 
+" UNION "
+" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, " 
+" subjectToVat,whTax,operation1,exitPage,url,trans_id,a.asset_code " 
+" from am_raisentry_post p, am_asset a " 
+" where ID = ASSET_ID  AND p.entryPostFlag='N' "+ Filter 
+" UNION " 
+" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id," 
+" subjectToVat,whTax,operation1,exitPage,url,trans_id,a.asset_code  " 
+" from am_raisentry_post p, am_asset a  " 
+" where ID = a.ASSET_ID AND p.entryPostFlag='N' "+ Filter
+" UNION "
+" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, "
+" subjectToVat,whTax,operation1,exitPage,url,trans_id,a.asset_code " 
+" from am_raisentry_post p, am_asset a "
+" where p.page='Asset Improvement Raise Entry' AND p.entryPostFlag='N'"
+" and p.id=a.asset_id "+ Filter
+" UNION "
+" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, "
+" subjectToVat,whTax,operation1,exitPage,url,trans_id,a.asset_code "
+" from am_raisentry_post p, am_asset a "
+" where p.page='ASSET PART PAYMENT ENTRY' AND p.entryPostFlag='N'"
+" and p.id=a.asset_id "+Filter
+" UNION "
+" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, "
+" subjectToVat,whTax,operation1,exitPage,url,trans_id,a.asset_code "
+" from am_raisentry_post p, am_asset a "
+" where p.page='WIP RECLASSIFICATION' AND p.entryPostFlag='N'"
+" and p.asset_code=a.asset_code "+Filter
+" UNION "
+" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, "
+" subjectToVat,whTax,operation1,exitPage,url,trans_id,a.asset_code "
+" from am_raisentry_post p, am_asset a "
+" where p.page='ASSET RECLASSIFICATION RAISE ENTRY' AND p.entryPostFlag='N'"
+" and p.asset_code=a.asset_code "+Filter
+" UNION "
+" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, "
+" subjectToVat,whTax,operation1,exitPage,url,trans_id,a.asset_code "
+" from am_raisentry_post p, am_asset a "
+" where p.page='ASSET DISPOSAL RAISE ENTRY' AND p.entryPostFlag='N'"
+" and p.id=a.asset_id "+Filter
+" UNION "
+" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, "
+" subjectToVat,whTax,operation1,exitPage,url,trans_id,a.asset_code "
+" from am_raisentry_post p, am_asset a "
+" where p.page='ASSET TRANSFER RAISE ENTRY' AND p.entryPostFlag='N' "
+" and p.asset_code=a.asset_code "+Filter
+" UNION "
+" select Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,branch_id, "
+" subjectToVat,whTax,operation1,exitPage,url,trans_id,a.asset_code "
+" from am_raisentry_post p, am_asset a "
+" where p.page='ACCELERATED DEPRECIATION RAISE ENTRY' AND p.entryPostFlag='N'"
+" and p.id=a.asset_id "+Filter
+" UNION "
+" select DISTINCT Id,p.Description,Page,Flag,partPay,UserId,Branch, cost_price,newbranch_id AS branch_id, " 
+" subjectToVat,whTax,operation1,exitPage,url,trans_id,p.asset_code  from am_raisentry_post p, dbo.am_gb_bulkStocktransfer m  " 
+" where GroupIdStatus = 'N'  and id in ( select convert(varchar ,Batch_id) from dbo.am_gb_bulkStocktransfer) " 
+" and id in ( select convert(varchar ,Batch_id) from am_gb_bulkStocktransfer)  and id = convert(varchar ,Batch_id) " 
+Filter;

        Finder_qry = Finder_qry + " " + ordering;
//        System.out.println("\n\n Finder_qry in findApprovalInitiatorforFleet():>>>>>  " + Finder_qry);
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(Finder_qry);
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
                int tranID = rs.getInt("trans_id");
                int assetCode = rs.getInt("asset_code");
                app = new Approval(id, Description, Page, Flag, partPay, UserId, Branch, subjectToVat, whTax, operation1, exitPage, url);
                app.setTranID(tranID);
//                System.out.println("findApprovalInitiator5 tranID:   "+tranID+"  UserId: "+UserId);
                app.setAssetCreator(getTransactionInitiatorByTranID(tranID, UserId,id));
//                System.out.println("getTransactionInitiatorByTranID Done: ");
                app.setAssetCode(assetCode);
                collection.add(app);
            }


        } catch (Exception ex) {
            System.out.println("findApprovalInitiatorforFleet(): WARNING: cannot fetch [ApprovalRecords]->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }  
        return collection;
    }

    public boolean insertApprovalxFT(String id, String description, String page, String flag, String partPay, String UserId, String Branch, 
            String subjectToVat, String whTax, String url, int transID, int assetCode)
    {
        boolean done;
        Connection con;
        PreparedStatement ps;
        String query;
        done = true;
        flag = "Y";
        con = null;
        ps = null;
        query = "INSERT INTO [am_raisentry_post](Id,Description,Page,Flag,partPay,UserId,Branch,s" +
"ubjectToVat,whTax,url,trans_id,asset_code,entryPostFlag,GroupIdStatus,Posting_Date) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
;
        try
        {
            con = getConnection("legendPlus");
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
            ps.setInt(11, transID);
            ps.setInt(12, assetCode);
            ps.setString(13, "N");
            ps.setString(14, "N");
 //           System.out.println("<<<<<getDate: "+new java.util.Date());
            Timestamp approveddate =  dbConnection.getDateTime(new java.util.Date());
 //           System.out.println("<<<<<approveddate: "+approveddate);
            ps.setTimestamp(15, approveddate);
    
            ps.execute();

        } catch (Exception ex) {
            done = false;
            System.out.println(
                    "WARNING:cannot insert am_raisentry_post for FT->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;
    }
    
    public boolean databasebackup(String backupfile,String databaseName,String userId,String deprecYear, String deprecMonth)
    {
        Connection con;
        PreparedStatement ps;
        PreparedStatement ps2;
        boolean done;
        boolean done2;
        boolean done3;
        String query;
        con = null;   
        ps = null;
        ps2 = null;
        done = false;
        done2 = false;
        Timestamp backupDate =  dbConnection.getDateTime(new java.util.Date());
        query = "USE "+databaseName+" "+
				"BACKUP DATABASE "+databaseName+"  "+
				"TO DISK = '"+backupfile+"' "+  
				"WITH FORMAT, "+  
				"NAME = 'Full Backup of "+databaseName+" ' ";
        String backupquery = "insert into DATABASE_BACKUP_HISTORY(BACKUP_DONE,DONE_BY,MONTH,YEAR,CREATE_DATE) "+
        		 "values('Y','"+userId+"','"+deprecMonth+"','"+deprecYear+"','"+backupDate+"')";
//        System.out.println("databasebackup query: "+query);
        try
        { 
        	con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            done = ps.executeUpdate() != -1;
            done = true;
            System.out.println("databasebackup done: "+done);
            if(done){
            ps2 = con.prepareStatement(backupquery);
            done2 = ps2.executeUpdate() != -1; 
            System.out.println("databasebackup done2: "+done2);
            }
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder(String.valueOf(getClass().getName()))).append(" ERROR:Error backing up the database->").append(e.getMessage()).toString());
            e.printStackTrace();
        }
        finally {  
			   closeConnection(con, ps);  
			   closeConnection(con, ps2); 
		}
        return done2;
    }    
    
    public String getCodeDelete(String query)
    {
        String result;
        Connection con;
        PreparedStatement ps;
        result = "";
        con = null;
        int rs = 0;
        ps = null;
        try
        {
 //       	System.out.println("====getCodeDelete query=====  "+query);
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeUpdate();

  //          System.out.println("====getCodeDelete query=====  "+query);
        } catch (Exception er) {
            System.out.println("Error in Query- getCodeDelete()... ->" + er);
            er.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
  //      System.out.println("====getCodeName result=====  "+result);
        return result;
    }


    public String retrieveArray(String query,String Id)
    {
        Connection con;
//        PreparedStatement ps;
        PreparedStatement ps = null;
        ResultSet rs;
        String apprvLevel;
        String apprvLevelLimit;
        String image;
        String narration = "";
        con = null;
        ps = null;
        rs = null;
        apprvLevel = "";
        apprvLevelLimit = "";
        image = "";
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query.toString());
			 if(!Id.equals("")) {
	        	  ps.setString(1, Id);
	          }
//            ps.execute();
            rs = ps.executeQuery(); 
//            System.out.println("=======>>>>I: "+Id+"       query: "+query);
            while (rs.next()) {
                apprvLevel = rs.getString(1);
                apprvLevelLimit = rs.getString(2);
                image = rs.getString(3);
                narration = rs.getString(4);
            }

        } catch (Exception ee) {
            System.out.println("WARN:ApprovalRecords.retrieveArray:->" + ee);
        } finally {
            closeConnection(con, ps, rs);
        }
        return (apprvLevel + ":" + apprvLevelLimit + ":" + image + ":" + narration);
    }
    public void updateAssetStatus(String assetid)
    {
        Connection con;
        PreparedStatement ps;
        String NOTIFY_QUERY;
        con = null;
        ps = null;
        NOTIFY_QUERY = "UPDATE am_asset SET Asset_Status = ? WHERE ASSET_ID = ?  ";
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(NOTIFY_QUERY);
            ps.setString(1, "Rejected");
            ps.setString(2, assetid);
            ps.executeUpdate();

        } catch (Exception ex) {
            System.out.println("WARNING: cannot update am_asset Asset_Status+" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
    }
    public void updateAssetImprovement(int tranId)
    {
        Connection con;
        PreparedStatement ps;
        String NOTIFY_QUERY;
        con = null;
        ps = null;
        NOTIFY_QUERY = "UPDATE am_asset_improvement SET approval_status = ? WHERE revalue_id = ?  ";
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(NOTIFY_QUERY);
            ps.setString(1, "Rejected");
            ps.setInt(2, tranId);
            ps.executeUpdate();

        } catch (Exception ex) {
            System.out.println("WARNING: cannot update am_asset_improvement approval_status+" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
    }    

    public Blob getIncidentImage(String sessionId) {

        Connection con = null;
        PreparedStatement ps = null;
        String getBlobQuery = "select image from am_ad_image where sessionId = ?";
        Blob incBlob = null;
        ResultSet rs = null;
        try {
            con = getConnection("legendPlus");
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
            con = getConnection("legendPlus");
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
    public boolean setProblemImage(String Session, String userId, String complaintId, String pageName) {

        Connection con = null;
        PreparedStatement ps = null;
        String setBlobQuery = "UPDATE am_ad_image SET Id = ?, PageName = ?  where sessionId = '"+Session+"' and id = '"+userId+"' ";
        boolean doneUpdate = false;

        try {
            con = getConnection("legendPlus");
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


    public String getResponse(String reqnId)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        String response;
        String FINDER_QUERY;
        con = null;
        ps = null;
        rs = null;
        response = "";
        FINDER_QUERY = "SELECT response from HD_RESPONSE WHERE complaint_id = ? ";
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);
            ps.setString(1, reqnId);
            for(rs = ps.executeQuery(); rs.next();)
            {
                response = rs.getString("response");
            }
        } catch (Exception ex) {
        	System.out.println((new StringBuilder("WARNING: cannot fetch Response in getResponse->")).append(ex.getMessage()).toString());
        } finally {
            closeConnection(con, ps);
        }
        return response;

    }

    public String getImageFileType(String reqnId, String pageName)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        String fileType;
        String FINDER_QUERY;
        con = null;
        ps = null;
        rs = null;
        fileType = "";
        FINDER_QUERY = "SELECT File_Type from am_ad_image WHERE AssetId = ? ";
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);
            ps.setString(1, reqnId);
            for(rs = ps.executeQuery(); rs.next();)
            {
                fileType = rs.getString("File_Type");
            }
        } catch (Exception ex) {
        	System.out.println((new StringBuilder("WARNING: cannot fetch File Type in getImageFileType->")).append(ex.getMessage()).toString());
        } finally {
        	closeConnection(con, ps, rs);
        }
        return fileType;
        
    }    
    

    public Blob getChangeImage(String sessionId)
    {
        Connection con;
        PreparedStatement ps;
        String getBlobQuery;
        Blob incBlob;
        con = null;
        ps = null;
        getBlobQuery = "select image from am_ad_image where sessionId = ?";
        incBlob = null;
        ResultSet rs = null;
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(getBlobQuery);
            ps.setString(1, sessionId);
            for(rs = ps.executeQuery(); rs.next();)
            {
                incBlob = rs.getBlob("image");
            }

        } catch (Exception ex) {
        	System.out.println((new StringBuilder("WARNING: cannot get image getChangeImage+")).append(ex.getMessage()).toString());
        } finally {
        	closeConnection(con, ps, rs);
        }
        return incBlob;
        
    }    

    public boolean setChangeImage(Blob incBlob, String ChangeId) {

        Connection con = null;
        PreparedStatement ps = null;
        String setBlobQuery = "UPDATE HD_CHANGE SET image = ? WHERE Change_id = ?  ";
        boolean doneUpdate = false;

        try {
            con = getConnection("legendPlus");
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
    
    public String getmailaddresses() {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String[] email = null;
        String mailaddresses = "";
        ArrayList list = new ArrayList();
        String FINDER_QUERY = "SELECT email from am_gb_user WHERE is_helpdesk_manager = 'Y' ";
        try {
            con = getConnection("legendPlus");
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

    public boolean setIncidentImage(Blob incBlob, String complaintId) {

        Connection con = null;
        PreparedStatement ps = null;
        String setBlobQuery = "UPDATE HD_COMPLAINT SET image = ? WHERE complaint_id = ?  ";
        boolean doneUpdate = false;

        try {
            con = getConnection("legendPlus");
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

    public boolean insertIntoGroupImage(String UserId, String page,String id)
    {
        boolean done;
        Connection con;
        PreparedStatement ps;
        String query;
        done = true;
        con = null;
        ps = null;
        query = "insert into am_group_image(user_Id,create_Date,pageName,group_id) VALUES(?,?,?,?)";
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            ps.setString(1, UserId);
//            ps.setString(2, description);
            ps.setDate(2, dateConvert(new Date()));
            ps.setString(3, page);
            ps.setString(4, id);
            ps.execute();

        } catch (Exception ex) {
            done = false;
            System.out.println(
                    "WARNING:cannot insert am_group_image->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;
    }

public void updatePostingRecordScript(String query,String batchId,String status)
{
	Connection con = null;
    PreparedStatement ps = null;
    try 
    	{
    	con = dbConnection.getConnection("legendPlus");
    	ps = con.prepareStatement(query);
    	ps.setString(1,status);
    	ps.setString(2,batchId);
       	int i =ps.executeUpdate();
        } 
	catch (Exception ex)
	    {
	        System.out.println("AssetRecordBean: Error Runing Update Script with Two Parameters " + ex);
	    } 
	finally 
		{
            dbConnection.closeConnection(con, ps);
        }

}


public void updateBatchPostingWithBatchId(String tableName,String columnName, String column2, String batchId, String groupId)
{
	Connection con = null;
    PreparedStatement ps = null;
    String query_r ="update " +tableName+ " set "+columnName+"=? where "+column2+" = ?  " ;
    System.out.println("query_r updateBatchPostingWithBatchId>>>> "+query_r);
    try 
    	{
    	con = dbConnection.getConnection("legendPlus");
    	ps = con.prepareStatement(query_r);
    	ps.setString(1,batchId);
    	ps.setString(2,groupId);
       	int i =ps.executeUpdate();
        } 
	catch (Exception ex)
	    {
	        System.out.println("AssetRecordBean: Error Updating Batch table " + ex);
	    } 
	finally 
		{
            dbConnection.closeConnection(con, ps);
        }

}

public void updatePostingRecordWithBatchId(String batchId,String tableName,String columnName)
{
	Connection con = null;
    PreparedStatement ps = null;
    String query_r ="update a SET "+columnName+"=? from " +tableName+ " a, AM_GB_POSTING_EXCEPTION b where a.ID != b.ID and b.BATCH_NO = ?" ;
    System.out.println("query_r updatePostingRecordWithBatchId>>>> "+query_r);
    try 
    	{
    	con = dbConnection.getConnection("legendPlus");
    	ps = con.prepareStatement(query_r);
    	ps.setString(1,"Posted");
    	ps.setString(2,batchId);
       	int i =ps.executeUpdate();
        } 
	catch (Exception ex)
	    {
	        System.out.println("AssetRecordBean: Error Updating Batch table " + ex);
	    } 
	finally 
		{
            dbConnection.closeConnection(con, ps);
        }

}

public void updateAssetWithBatchId(String tableName,String columnName, String whereFld, String batchId,String status)
{
	Connection con = null;
    PreparedStatement ps = null;
    String query_r ="update " +tableName+ " set "+columnName+"=? where "+whereFld+" = ? " ;
    System.out.println("query_r updateAssetWithBatchId>>>> "+query_r);
    try 
    	{
    	con = dbConnection.getConnection("legendPlus");
    	ps = con.prepareStatement(query_r);
    	ps.setString(1,status);
    	ps.setString(2,batchId);
       	int i =ps.executeUpdate();
        } 
	catch (Exception ex)
	    {
	        System.out.println("AssetRecordBean: Error in updateAssetWithBatchId " + ex);
	    } 
	finally 
		{
            dbConnection.closeConnection(con, ps);
        }

}

public void updatePostWithBatchId(String tableName,String columnName1,String columnName2, String whereFld, String batchId,String status1,String status2)
{
	Connection con = null;
    PreparedStatement ps = null;
    String query_r ="update " +tableName+ " set "+columnName1+"=?, "+columnName2+"=? where "+whereFld+" = ? " ;
    System.out.println("query_r updateAssetWithBatchId>>>> "+query_r);
    try 
    	{
    	con = dbConnection.getConnection("legendPlus");
    	ps = con.prepareStatement(query_r);
    	ps.setString(1,status1);
    	ps.setString(2,status2);
    	ps.setString(3,batchId);
       	int i =ps.executeUpdate();
        } 
	catch (Exception ex)
	    {
	        System.out.println("AssetRecordBean: Error in updateAssetWithBatchId " + ex);
	    } 
	finally 
		{
            dbConnection.closeConnection(con, ps);
        }

}

public void updatePostWithBatchId(String tableName,String columnName1,String columnName2,String columnName3, String whereFld, String batchId,String status1,String status2,String status3)
{
	Connection con = null;
    PreparedStatement ps = null;
    String query_r ="update " +tableName+ " set "+columnName1+"=?, "+columnName2+"=?, "+columnName3+"=? where "+whereFld+" = ? " ;
    System.out.println("query_r updateAssetWithBatchId>>>> "+query_r);
    try 
    	{
    	con = dbConnection.getConnection("legendPlus");
    	ps = con.prepareStatement(query_r);
    	ps.setString(1,status1);
    	ps.setString(2,status2);
    	ps.setString(3,status3);
    	ps.setString(4,batchId);
       	int i =ps.executeUpdate();
        } 
	catch (Exception ex)
	    {
	        System.out.println("AssetRecordBean: Error in updateAssetWithBatchId " + ex);
	    } 
	finally 
		{
            dbConnection.closeConnection(con, ps);
        }

}
public void insertBatchTransactions(int id, String groupId, String dateField,String accountNo,double costPrice,String transType, String description, String transCode, String currency, String branchCode, String UserId, String purposeCode, 
        String maker, String batchNo, String checker,String status)

{
    boolean done;
    Connection con;
    PreparedStatement ps;
    String query;
    done = true;
    con = null;
    ps = null;
    query = "INSERT INTO AM_GB_BATCH_POSTING(ID,GROUP_ID,ID_GROUP_ID,DATE_FIELD,ACCOUNT_NO,COST_PRICE,"
    		+ "TRANSTYPE,DESCRIPTION,TRANSCODE,CURRENCY,BRANCH_CODE,PURPOSE_CODE,MAKER,"
    		+ "BATCH_NO,CHECKER,STATUS,USER_ID) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
;
    try
    {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(query);
        String idGroupId = id+"-"+groupId;
        ps.setInt(1, id);
        ps.setString(2, groupId);
        ps.setString(3, idGroupId);
        ps.setString(4, dateField);
        ps.setString(5, accountNo);
        ps.setDouble(6, costPrice);
        ps.setString(7, transType);
        ps.setString(8, description);
        ps.setString(9, transCode);
        ps.setString(10, currency);
        ps.setString(11, branchCode);
        ps.setString(12, purposeCode);
        ps.setString(13, maker);
        ps.setString(14, batchNo);
        ps.setString(15, checker);
        ps.setString(16, status);
        ps.setString(17, UserId);
        ps.execute();

    } catch (Exception ex) {
        done = false;
        System.out.println(
                "WARNING:cannot insert AM_GB_BATCH_POSTING->"
                + ex.getMessage());
    } finally {
        closeConnection(con, ps);
    }
//    return done;
}

public boolean deleteBatachPostingException(String groupId)
{
    Connection con;
    PreparedStatement ps;
    String NOTIFY_QUERY;
    con = null;
    ps = null;
    NOTIFY_QUERY = "delete from  AM_GB_POSTING_EXCEPTION  WHERE GROUP_ID = ?";
    boolean done;
//    System.out.println("NOTIFY_QUERY=======: " + NOTIFY_QUERY +"   groupId====: " + groupId);
    try
    {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(NOTIFY_QUERY);
        ps.setString(1, groupId);
        ps.executeUpdate();
        done = true;
    } catch (Exception ex) {
        done = false;
        System.out.println("WARNING: cannot delete AM_GB_POSTING_EXCEPTION+" + ex.getMessage());
    } finally {
        closeConnection(con, ps);
    }
    return done;
}

public void updateAssetWithNoWhereClause(String tableName,String columnName,String status)
{
	Connection con = null;
    PreparedStatement ps = null;
    String query_r ="update " +tableName+ " set "+columnName+"=? " ;
//    System.out.println("query_r updateAssetWithBatchId>>>> "+query_r);
    try 
    	{
    	con = dbConnection.getConnection("legendPlus");
    	ps = con.prepareStatement(query_r);
    	ps.setString(1,status);
       	int i =ps.executeUpdate();
        } 
	catch (Exception ex)
	    {
	        System.out.println("AssetRecordBean: Error in updateAssetWithBatchId " + ex);
	    } 
	finally 
		{
            dbConnection.closeConnection(con, ps);
        }

}

}
