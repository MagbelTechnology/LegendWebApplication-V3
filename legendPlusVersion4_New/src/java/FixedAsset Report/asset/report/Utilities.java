package asset.report;

import java.text.DateFormat;
import java.util.*;
import java.io.*;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.PrintStream;
import java.io.File;

import java.sql.*;
import java.util.Properties;
import java.io.FileInputStream;
import javax.naming.*;
import javax.sql.DataSource;
import javax.swing.*;


public class Utilities
{
  Connection con = null;
  Statement  stmt = null;
  PreparedStatement ps = null;
  CallableStatement  cstmt = null;
  ResultSet rs = null;


  private String jndiName = "FixedAsset";

  public Utilities()
  {
  }

  public void executeProcedure(String callableProc)
  {

   String[] finDatez = company_Details();
   String startDate = toCAL(finDatez[2]);
   String endDate = toCAL(finDatez[3]);

   try
   {
    con = getConnection();
    String CALL_PROC = callableProc;
    //"{call dbo.am_msp_variance_budget_acquisition(?,?)}"
    cstmt = con.prepareCall(CALL_PROC);
    cstmt.setString(1,startDate);
    cstmt.setString(2,endDate);
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
	  // if (cs != null) cs.close();
		 if (con != null) con.close();
	  }
    catch(Exception Ex)
    {
		 System.out.println("WARNING::Error closing connection @ Utilities  "+Ex);
	  }
	 }
  }

  public String toSQL(String dt)
  {
  //year,mth,day to SQL
  String [] result = dt.substring(0,10).split("/");
  return result[2] + "/" + result[1] + "/"  + result[0];
  }

  public String toCAL(String dt)
  {
   //year,mth,day to CAL
   String [] result = dt.substring(0,10).split("-");
   return result[2] + "/" + result[1] + "/"  + result[0];
  }

  public String getCurrentDate()
  {
   java.text.SimpleDateFormat sd  = new java.text.SimpleDateFormat("dd/MM/yyyy");
   String curr_date = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new Date());
   return curr_date;
  }


  public String[] company_Details()
  {

   String[] result = new String[4];

    Connection con = null;
    Statement  stmt = null;
    ResultSet rs = null;
    PreparedStatement ps = null;
    String qry = "SELECT company_name,company_address,financial_start_date,financial_end_date FROM am_gb_company";
    String company_name = "";
    try
    {
     con = getConnection();
     ps = con.prepareStatement(qry);
     rs = ps.executeQuery();
    while(rs.next())
    {
     result[0] = rs.getString(1);
     result[1] = rs.getString(2);
     result[2] = rs.getString(3);
     result[3] = rs.getString(4);
    }

   }
   catch(Exception e)
   {
    System.out.println("WARNING::ERROR GETCOMPANY  AM_GB_COMPANY "+e);
    e.printStackTrace();
   }
   finally
   {
   try
   {
   if(ps != null){ps.close();}
   if(con != null){con.close();}
   }
   catch(Exception e)
   {
    System.out.println("WARNING::Error Closing Connection "+e);
   }
   }
   return result;
  }
  public String getReportName(String reportName)
  {
    String header = "";
    String[] _name = reportName.split("_");
    header = _name[0].equals("fixed")?"fixed asset":"fleet asset";

    if(header.equalsIgnoreCase("fixed asset")&&_name[2].equalsIgnoreCase("mgt"))
    {
     return "Fixed Asset Management Report";
    }
    if(header.equalsIgnoreCase("fixed asset")&&_name[2].equalsIgnoreCase("addition"))
    {
     return "Fixed Asset Addition Report";
    }
    if(header.equalsIgnoreCase("fixed asset")&&_name[2].equalsIgnoreCase("list"))
    {
     return "Fixed Asset List Report";
    }
    if(header.equalsIgnoreCase("fixed asset")&&_name[2].equalsIgnoreCase("detail"))
    {
     return "Fixed Asset Detail Report";
    }
    if(header.equalsIgnoreCase("fixed asset")&&_name[2].equalsIgnoreCase("register"))
    {
     return "Fixed Asset Register Report";
    }
    else if(header.equalsIgnoreCase("fixed asset")&&_name[2].equalsIgnoreCase("mgt1"))
    {
     return "Fixed Asset Management Summary Report";
    }
    else if(header.equalsIgnoreCase("fixed asset")&&_name[2].equalsIgnoreCase("revalue"))
    {
     return "Fixed Asset Revaluation Report";
    }
    else if(header.equalsIgnoreCase("fixed asset")&&_name[2].equalsIgnoreCase("fullydep"))
    {
     return "Fully Depreciated Asset Report";
    }
    else if(header.equalsIgnoreCase("fixed asset")&&_name[2].equalsIgnoreCase("ledger"))
    {
     return "Ledger Extraction Report";
    }
    else if(header.equalsIgnoreCase("fixed asset")&&_name[2].equalsIgnoreCase("acquisition"))
    {
     return "Acquisition Budget Report";
    }
    else if(header.equalsIgnoreCase("fixed asset")&&_name[2].equalsIgnoreCase("acquivariance"))
    {
     return "Acquisition Budget Variance Report";
    }
    else if(header.equalsIgnoreCase("fixed asset")&&_name[2].equalsIgnoreCase("maintenance"))
    {
     return "Maintenance Budget Report";
    }
    else if(header.equalsIgnoreCase("fixed asset")&&_name[2].equalsIgnoreCase("maintvariance"))
    {
     return "Maintenance Budget Variance Report";
    }
    else if(header.equalsIgnoreCase("fleet asset")&&_name[2].equalsIgnoreCase("maintsummary"))
    {
     return "Fleet Asset Maintenance Summary Report";
    }
    else if(header.equalsIgnoreCase("fleet asset")&&_name[2].equalsIgnoreCase("maintenance"))
    {
     return "Fleet Asset Maintenance Report";
    }
    else if(header.equalsIgnoreCase("fleet asset")&&_name[2].equalsIgnoreCase("fuelsummary"))
    {
     return "Fleet Asset Fuel Summary Report";
    }
    else if(header.equalsIgnoreCase("fleet asset")&&_name[2].equalsIgnoreCase("fuel"))
    {
     return "Fleet Asset Fuel Report";
    }
    else if(header.equalsIgnoreCase("fleet asset")&&_name[2].equalsIgnoreCase("accidentsummary"))
    {
     return "Fleet Asset Accident Summary Report";
    }
    else if(header.equalsIgnoreCase("fleet asset")&&_name[2].equalsIgnoreCase("accident"))
    {
     return "Fleet Asset Accident Report";
    }
    else if(header.equalsIgnoreCase("fleet asset")&&_name[2].equalsIgnoreCase("licencesummary"))
    {
     return "Fleet Asset Licence Summary Report";
    }
    else if(header.equalsIgnoreCase("fleet asset")&&_name[2].equalsIgnoreCase("licence"))
    {
     return "Fleet Asset Licence Report";
    }
    else if(header.equalsIgnoreCase("fleet asset")&&_name[2].equalsIgnoreCase("insurancesummary"))
    {
     return "Fleet Asset Insurance Summary Report";
    }
    else if(header.equalsIgnoreCase("fleet asset")&&_name[2].equalsIgnoreCase("insurance"))
    {
     return "Fleet Asset Insurance Report";
    }
    else if(header.equalsIgnoreCase("fleet asset")&&_name[2].equalsIgnoreCase("detail"))
    {
     return "Fleet Asset Detail Report";
    }
    else if(header.equalsIgnoreCase("fleet asset")&&_name[2].equalsIgnoreCase("location"))
    {
     return "Fleet Asset Location Report";
    }
    else if(header.equalsIgnoreCase("fleet asset")&&_name[2].equalsIgnoreCase("allocation"))
    {
     return "Fleet Asset Allocation Report";
    }
    else if(header.equalsIgnoreCase("fixed asset")&&_name[2].equalsIgnoreCase("noticereminder"))
    {
     return "Asset Notice Reminder Report";
    }
    else if(header.equalsIgnoreCase("fixed asset")&&_name[2].equalsIgnoreCase("audittrail"))
    {
     return "Asset Audit Trail Report";
    }
    else if(header.equalsIgnoreCase("fleet asset")&&_name[2].equalsIgnoreCase("stolen"))
    {
     return "Asset Stolen Report";
    }
   /* if(_name[0].equalsIgnoreCase("fixed")&&_name[2].equalsIgnoreCase("mgt1"))
    {
     return "FIXED ASSET MANAGEMENT SUMMARY "+_name[2].toUpperCase()+" REPORT";
    }*/
    else
    {
     return header+" "+_name[2]+" Report";
    }

  }
  public String getResources(String selected, String query)
  {
   Connection mcon;
   PreparedStatement mps;
   ResultSet mrs;
   String html;
   mcon = null;
   mps = null;
   mrs = null;
   html = "";
   String id = "";
   try
   {
    mcon = getConnection();
    mps = mcon.prepareStatement(query);
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
    if(mps != null)mps.close();
    if(mrs != null)mrs.close();
    if(mcon != null)mcon.close();
   }
   catch(Exception Ex)
   {
    Ex.printStackTrace();
   }
   }
   //System.out.println("inside bean : "+html);
   return html;
  }

 public String getColumnName(String query,String value)
 {
  String name = "";
  String queryString = "";
  queryString = query + value;
  if(value.equals("***"))
  {
   name = "***";
  }
  else
  {
  try
  {
   con = getConnection();
   ps = con.prepareStatement(queryString);
   rs = ps.executeQuery();
   while(rs.next())
   {
    name = rs.getString(1);
		if(name == null) name = "";
   }
  }
  catch(Exception Ex)
  {
   System.out.println("Error In findSegment "+Ex);
  }
  finally
  {
   try
   {
    if(ps != null)ps.close();
    if(rs != null)rs.close();
    if(con != null)con.close();
   }
   catch(Exception Ex)
   {
    Ex.printStackTrace();
   }
  }
  }
   return name;
 }

  //connect to rdbms

  public Connection getConnection() throws Exception
    {
        Connection con = null;
        try{
          Context initContext = new InitialContext();
          Context envContext  = (Context)initContext.lookup("java:/comp/env");
          DataSource ds = (DataSource)envContext.lookup("jdbc/"+this.jndiName);
          //System.out.println
          con = ds.getConnection();

        } catch (Exception _e) {
            System.out.println("DataAccess::getConnection()!"+_e);
            _e.printStackTrace();
            con = null;
           }
        return con;
    }



   public static void main(String[] args)
   {
     Utilities utilz = new Utilities();
    //System.out.println(utilz.getColumnName("select region_name from am_ad_region where region_id = ","***"));
    //utilz.executeProcedure("{call dbo.am_msp_variance_budget_acquisition(?,?)}");
    //System.out.println(utilz.getResources("","select branch_id,branch_name from am_ad_branch"));

   }

 }