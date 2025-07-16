package legend.bean;

import java.io.PrintStream;
import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import legend.util.ConnectionClass;
import java.sql.Connection;
public class NoticeAlert
{

    ConnectionClass db;

    public NoticeAlert()
    {
    }

    public ResultSet listNoticeAlert()
    {
        Connection con = null;
        Statement ps = null;
        ResultSet rs = null;
        try
        {
        	con = getConnection();
            String query = "SELECT * FROM AM_NOTICEREMINDER";
            Statement don = con.createStatement();
            rs = don.executeQuery(query);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARING: Could not list notice alert ").append(e.getMessage()).toString());
        }
        return rs;
    }

    public ResultSet getNoticeAlert(String noticerm_id)
    {
        Connection con = null;
        Statement ps = null;
        ResultSet rs = null;
        try
        {
        	con = getConnection();
            String query = (new StringBuilder()).append("SELECT * FROM AM_NOTICEREMINDER WHERE NOTICERM_ID = '").append(noticerm_id).append("'").toString();
            Statement don = con.createStatement();
            rs = don.executeQuery(query);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARING: Could not get notice alert ").append(e.getMessage()).toString());
        }
        return rs;
    }

    public ResultSet listCategories()
    {
        Connection con = null;
        Statement ps = null;
        ResultSet rs = null;
        try
        {
        	con = getConnection();
            String query = "SELECT CATEGORY FROM AM_NOTICEREMINDER WHERE NOTICE_STATUS = 'A'";
            Statement don = con.createStatement();
            rs = don.executeQuery(query);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARING: Could not list category ").append(e.getMessage()).toString());
        }
        return rs;
    }

    public ResultSet listCatego()
    {
        Connection con = null;
        Statement ps = null;
        ResultSet rs = null;
        try
        {
        	con = getConnection();
            String query = "SELECT CATEGORY FROM AM_AD_CATEGORY WHERE NOTICE_STATUS = 'A'";
            Statement don = con.createStatement();
            rs = don.executeQuery(query);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARING: Could not list category ").append(e.getMessage()).toString());
        }
        return rs;
    }
    
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

}
