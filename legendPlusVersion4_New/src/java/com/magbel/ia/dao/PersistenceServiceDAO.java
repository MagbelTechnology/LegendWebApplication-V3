package com.magbel.ia.dao;

import com.magbel.util.DataConnect;
import com.magbel.util.DatetimeFormat;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

// Referenced classes of package com.magbel.ia.dao:
//            ConnectionDAO

public class PersistenceServiceDAO
    implements ConnectionDAO
{

    DatetimeFormat dateFormat;
    SimpleDateFormat sdf;

    public PersistenceServiceDAO()   
    {
        dateFormat = new DatetimeFormat();
        sdf = new SimpleDateFormat("dd-MM-yyyy");
    }

    public void executeQuery(String query)
    {
        { 
        	System.out.println("=====query in executeQuery===?? "+query);
            PreparedStatement ps = null;
            Connection con = null;
            try
            {
                con = getConnection("legendPlus");
                ps = con.prepareStatement(query);
                ps.execute();
            }  
            catch(Exception error)
            {
                closeConnection(con, ps);
            }
            finally
            {
                closeConnection(con, ps);
            }
            closeConnection(con, ps);
        }
    }

    public void executeQueryString(String query, String jndiName)
    {
        PreparedStatement ps;
        Connection con;
        ps = null;
        con = null;
        try {        
        con = getConnection(jndiName);
        ps = con.prepareStatement(query);
        ps.execute();
        closeConnection(con, ps);
    } catch (Exception ex) {
        System.out.println((new StringBuilder()).append("WARN:com.magbel.ia.dao.PersistenceService:").append(ex).toString());
        closeConnection(con, ps);
    } finally {
        closeConnection(con, ps);
    }
    }

    public void closeConnection(Connection con, PreparedStatement ps)
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
        catch(Exception ex)
        {
            System.out.println((new StringBuilder()).append("WARNING:Error closing Connection ->").append(ex).toString());
        }
    }

    public void closeConnection(Connection con, PreparedStatement ps, ResultSet rs)
    {
        try
        {
            if(ps != null)
            {
                ps.close();
            }
            if(rs != null)
            {
                rs.close();
            }
            if(con != null)
            {
                con.close();
            }
        }
        catch(Exception ex)
        {
            System.out.println((new StringBuilder()).append("WARNING:Error closing Connection ->").append(ex).toString());
        }
    }

    public void closeConnection(Connection con, Statement ps, ResultSet rs)
    {
        try
        {
            if(ps != null)
            {
                ps.close();
            }
            if(rs != null)
            {
                rs.close();
            }
            if(con != null)
            {
                con.close();
            }
        }
        catch(Exception ex)
        {
            System.out.println((new StringBuilder()).append("WARNING:Error closing Connection ->").append(ex).toString());
        }
    }

    public Connection getConnection()
    {
        Connection con = null; 
        try
        {
            con = (new DataConnect("legendPlus")).getConnection();
        }
        catch(Exception conError)
        {
            System.out.println((new StringBuilder()).append("WARNING:Error getting connection - >").append(conError).toString());
        }
        return con;
    }

    public Connection getConnection(String jndiName)
    {
        Connection con = null;
        try
        {
            con = (new DataConnect(jndiName)).getConnection();
        }
        catch(Exception conError)
        {
            System.out.println((new StringBuilder()).append("WARNING:Error getting connection - >").append(conError).toString());
        }
        return con;
    }

    public java.sql.Date dateConvert(String strDate)
    {
        SimpleDateFormat formata = new SimpleDateFormat("dd-MM-yyyy");
        if(strDate == null)
        {
            strDate = formata.format(new Date());
        }
        strDate = strDate.replaceAll("/", "-");
        Date inputDate = null;
        try
        {
            inputDate = formata.parse(strDate);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARNING: Error formating Date:").append(e.getMessage()).toString());
        }
        return dateFormat.getSQLFormatedDate(inputDate);
    }

    public java.sql.Date dateConvert(Date inputDate)
    {
        return dateFormat.getSQLFormatedDate(inputDate);
    }

    public java.sql.Date dateConvert(long longDate)
    {
        Date inputDate = new Date();
        inputDate.setTime(longDate);
        return dateFormat.getSQLFormatedDate(inputDate);
    }

    public String formatDate(Date date)
    {
        String formated = "";
        if(date == null)
        {
            date = new Date();
        }
        try
        {
            sdf = new SimpleDateFormat("dd-MM-yyyy");
            formated = sdf.format(date);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARNING:Error formating Date ->").append(e.getMessage()).toString());
        }
        return formated;
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
}