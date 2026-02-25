package asset.report;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class Utilities
{
   
    Connection con;
    Statement stmt;
    PreparedStatement ps;
    CallableStatement cstmt;
    ResultSet rs;
    private String jndiName;

    public Utilities()
    {
        con = null;
        stmt = null;
        ps = null;
        cstmt = null;
        rs = null;
        jndiName = "legendPlus";
    }

    public void executeProcedure(String callableProc)
    {
        String finDatez[] = company_Details();
        String startDate = toCAL(finDatez[2]);
        String endDate = toCAL(finDatez[3]);
        try
        {
            con = getConnection();
            String CALL_PROC = callableProc;
            cstmt = con.prepareCall(CALL_PROC);
            cstmt.setString(1, startDate);
            cstmt.setString(2, endDate);
            int i = cstmt.executeUpdate();
        }
        catch(Exception Ex)
        {
            Ex.printStackTrace();
        }
        finally
        {
            try
            {
                if(con != null)
                {
                    con.close();
                }
            }
            catch(Exception Ex)
            {
                System.out.println("WARNING::Error closing connection @ Utilities  " + Ex);
            }
        }
    }

    public String toSQL(String dt)
    {
        String result[] = dt.substring(0, 10).split("/");
        return result[2] + "/" + result[1] + "/" + result[0];
    }

    public String toCAL(String dt)
    {
        String result[] = dt.substring(0, 10).split("-");
        return result[2] + "/" + result[1] + "/" + result[0];
    }

    public String getCurrentDate()
    {
        SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
        String curr_date = (new SimpleDateFormat("dd/MM/yyyy")).format(new Date());
        return curr_date;
    }

    public String[] company_Details()
    {
        String result[] = new String[4];
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        CallableStatement cstmt = null;
//        String qry = "SELECT company_name,company_address,financial_start_date,financial_end_date FROM" +
//" am_gb_company";
        String company_name = "";
        try
        {
//            con = getConnection();
//            ps = con.prepareStatement(qry);
	       	 con = getConnection();
	         stmt = con.createStatement();
		     cstmt = con.prepareCall("{CALL company_Details()}");
		//     cstmt.setString(1, userName);
		     rs = cstmt.executeQuery();

 //           for(rs = ps.executeQuery(); rs.next();)
 //           {
		     if (rs.next()) {
                result[0] = rs.getString(1);
                result[1] = rs.getString(2);
                result[2] = rs.getString(3);
                result[3] = rs.getString(4);
            }

        }
        catch(Exception e)
        {
            System.out.println("WARNING::ERROR GETCOMPANY  AM_GB_COMPANY " + e);
            e.printStackTrace();
        }
        finally
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
                System.out.println("WARNING::Error Closing Connection " + e);
            }
        }
        return result;
    }

    public String getReportName(String reportName)
    {
        String header = "";
        String _name[] = reportName.split("_");
        header = _name[0].equals("fixed") ? "fixed asset" : "fleet asset";
        if(header.equalsIgnoreCase("fixed asset") && _name[2].equalsIgnoreCase("mgt"))
        {
            return "Fixed Asset Management Report";
        }
        if(header.equalsIgnoreCase("fixed asset") && _name[2].equalsIgnoreCase("addition"))
        {
            return "Fixed Asset Addition Report";
        }
        if(header.equalsIgnoreCase("fixed asset") && _name[2].equalsIgnoreCase("list"))
        {
            return "Fixed Asset List Report";
        }
        if(header.equalsIgnoreCase("fixed asset") && _name[2].equalsIgnoreCase("detail"))
        {
            return "Fixed Asset Detail Report";
        }
        if(header.equalsIgnoreCase("fixed asset") && _name[2].equalsIgnoreCase("register"))
        {
            return "Fixed Asset Register Report";
        }
        if(header.equalsIgnoreCase("fixed asset") && _name[2].equalsIgnoreCase("mgt1"))
        {
            return "Fixed Asset Management Summary Report";
        }
        if(header.equalsIgnoreCase("fixed asset") && _name[2].equalsIgnoreCase("revalue"))
        {
            return "Fixed Asset Revaluation Report";
        }
        if(header.equalsIgnoreCase("fixed asset") && _name[2].equalsIgnoreCase("fullydep"))
        {
            return "Fully Depreciated Asset Report";
        }
        if(header.equalsIgnoreCase("fixed asset") && _name[2].equalsIgnoreCase("ledger"))
        {
            return "Ledger Extraction Report";
        }
        if(header.equalsIgnoreCase("fixed asset") && _name[2].equalsIgnoreCase("acquisition"))
        {
            return "Acquisition Budget Report";
        }
        if(header.equalsIgnoreCase("fixed asset") && _name[2].equalsIgnoreCase("acquivariance"))
        {
            return "Acquisition Budget Variance Report";
        }
        if(header.equalsIgnoreCase("fixed asset") && _name[2].equalsIgnoreCase("maintenance"))
        {
            return "Maintenance Budget Report";
        }
        if(header.equalsIgnoreCase("fixed asset") && _name[2].equalsIgnoreCase("maintvariance"))
        {
            return "Maintenance Budget Variance Report";
        }
        if(header.equalsIgnoreCase("fleet asset") && _name[2].equalsIgnoreCase("maintsummary"))
        {
            return "Fleet Asset Maintenance Summary Report";
        }
        if(header.equalsIgnoreCase("fleet asset") && _name[2].equalsIgnoreCase("maintenance"))
        {
            return "Fleet Asset Maintenance Report";
        }
        if(header.equalsIgnoreCase("fleet asset") && _name[2].equalsIgnoreCase("fuelsummary"))
        {
            return "Fleet Asset Fuel Summary Report";
        }
        if(header.equalsIgnoreCase("fleet asset") && _name[2].equalsIgnoreCase("fuel"))
        {
            return "Fleet Asset Fuel Report";
        }
        if(header.equalsIgnoreCase("fleet asset") && _name[2].equalsIgnoreCase("accidentsummary"))
        {
            return "Fleet Asset Accident Summary Report";
        }
        if(header.equalsIgnoreCase("fleet asset") && _name[2].equalsIgnoreCase("accident"))
        {
            return "Fleet Asset Accident Report";
        }
        if(header.equalsIgnoreCase("fleet asset") && _name[2].equalsIgnoreCase("licencesummary"))
        {
            return "Fleet Asset Licence Summary Report";
        }
        if(header.equalsIgnoreCase("fleet asset") && _name[2].equalsIgnoreCase("licence"))
        {
            return "Fleet Asset Licence Report";
        }
        if(header.equalsIgnoreCase("fleet asset") && _name[2].equalsIgnoreCase("insurancesummary"))
        {
            return "Fleet Asset Insurance Summary Report";
        }
        if(header.equalsIgnoreCase("fleet asset") && _name[2].equalsIgnoreCase("insurance"))
        {
            return "Fleet Asset Insurance Report";
        }
        if(header.equalsIgnoreCase("fleet asset") && _name[2].equalsIgnoreCase("detail"))
        {
            return "Fleet Asset Detail Report";
        }
        if(header.equalsIgnoreCase("fleet asset") && _name[2].equalsIgnoreCase("location"))
        {
            return "Fleet Asset Location Report";
        }
        if(header.equalsIgnoreCase("fleet asset") && _name[2].equalsIgnoreCase("allocation"))
        {
            return "Fleet Asset Allocation Report";
        }
        if(header.equalsIgnoreCase("fixed asset") && _name[2].equalsIgnoreCase("noticereminder"))
        {
            return "Asset Notice Reminder Report";
        }
        if(header.equalsIgnoreCase("fixed asset") && _name[2].equalsIgnoreCase("audittrail"))
        {
            return "Asset Audit Trail Report";
        }
        if(header.equalsIgnoreCase("fleet asset") && _name[2].equalsIgnoreCase("stolen"))
        {
            return "Asset Stolen Report";
        } else
        {
            return header + " " + _name[2] + " Report";
        }
    }
  
    public String getResources(String selected, String query)
    {
        Connection mcon = null;
        PreparedStatement mps = null;
        ResultSet mrs = null;
        String html = "";
        String id = "";
        try
        {
        	 	System.out.println("=======query in getResources ====== "+query);
            mcon = getConnection();
//            mps = mcon.prepareStatement(query);
            mps = mcon.prepareStatement(query.toString());
            //query_ = query_.replaceAll("\\s", "");
            if(query.contains("region")){
           // 	System.out.println("=======cost_price ======");
            	mps.setString(1, selected);

            }
            mrs = mps.executeQuery();
            for(mrs = mps.executeQuery(); mrs.next();)
            {
                id = mrs.getString(1);
                html = html + "<option value='" + id + "' " + (id.equals(selected) ? " selected " : "") + ">" + mrs.getString(2) + "</option> ";
            }

        }
        catch(Exception Ex)
        {
            Ex.printStackTrace();
        }
        finally
        {
            try
            {
                if(mps != null)
                {
                    mps.close();
                }
                if(mrs != null)
                {
                    mrs.close();
                }
                if(mcon != null)
                {
                    mcon.close();
                }
            }
            catch(Exception Ex)
            {
                Ex.printStackTrace();
            }
        }
        return html;
    }

    public String getColumnName(String query, String value)
    {
        String name = "";
        String queryString = "";
        queryString = query + value;
        if(value.equals("***"))
        {
            name = "***";
        } else
        {
            try
            {
                con = getConnection();
                ps = con.prepareStatement(queryString);
                rs = ps.executeQuery();
                do
                {
                    if(!rs.next())
                    {
                        break;
                    }
                    name = rs.getString(1);
                    if(name == null)
                    {
                        name = "";
                    }
                } while(true);
            }
            catch(Exception Ex)
            {
                System.out.println("Error In findSegment " + Ex);
            }
            finally
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
                catch(Exception Ex)
                {
                    Ex.printStackTrace();
                }
            }
        }
        return name;
    }
/*
    public Connection getConnection()
        throws Exception
    {
        Connection con = null;
        try
        {
            Context initContext = new InitialContext();
            Context envContext = (Context)initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource)envContext.lookup("jdbc/" + jndiName);
            con = ds.getConnection();
        }
        catch(Exception _e)
        {
            System.out.println("DataAccess::getConnection()!" + _e);
            _e.printStackTrace();
            con = null;
        }
        return con;
    }
*/
    public Connection getConnection() throws Exception {
        Connection con = null;

        try {
        	System.out.println("<=====Inside getConnection()====>");
            Context initContext = new InitialContext();
            String dsJndi = "java:/legendPlus";
            DataSource ds = (DataSource) initContext.lookup(
            		dsJndi);
            con = ds.getConnection();
//            con.close();
            System.out.println("<=====After getConnection()====>");
        } catch (Exception _e) {
            System.out.println("WARNING::DataAccess::getConnection()!" + _e);
            //con = null;
        }

        return con;
    }
    
    
    public static void main(String args[])
    {
        Utilities utilz = new Utilities();
    }
}