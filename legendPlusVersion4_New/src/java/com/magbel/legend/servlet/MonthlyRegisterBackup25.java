package com.magbel.legend.servlet;

import com.magbel.util.ApplicationHelper;
import java.io.*;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import magma.net.dao.MagmaDBConnection;

public class MonthlyRegisterBackup25 extends HttpServlet
{

    private static final long serialVersionUID = 1L;
    private static final String CONTENT_TYPE = "text/xml";
    MagmaDBConnection mgDbCon;
    ApplicationHelper applHelper;
    Connection con;
    Connection cn;
    Statement stmt;
    ResultSet rs;
    Statement s;
    PreparedStatement ps;
    PreparedStatement ds;
    boolean done;

    public MonthlyRegisterBackup25()
    {
        mgDbCon = null;
        applHelper = null;
        con = null;
        cn = null;
        stmt = null;
        rs = null;
        s = null;
        ps = null;
        ds = null;
        done = false;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        signatureRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        signatureRequest(request, response);
    }

    private void signatureRequest(HttpServletRequest request, HttpServletResponse response)
        throws IOException
    {
        String pagecode;
        String RecInsert;
        int i;
        PrintWriter out;
        String query;
        String records = request.getParameter("record");
        pagecode = request.getParameter("pagecode");
        RecInsert = request.getParameter("RecInsert");
        System.out.println((new StringBuilder("====pagecode form JSP ====> ")).append(pagecode).toString());
        System.out.println((new StringBuilder("====RecInsert From JSP ====> ")).append(RecInsert).toString());
        if(RecInsert != "")
        System.out.println((new StringBuilder("====pagecode First ====> ")).append(pagecode).toString());
        System.out.println((new StringBuilder("====RecInsert firts ====> ")).append(RecInsert).toString());
        i = 0;
        System.out.println("About to Delete records from file AM_ASSET_MONTHLY");
        mgDbCon = new MagmaDBConnection();
        applHelper = new ApplicationHelper();
        out = response.getWriter();
        String Sapquery = "delete from AM_ASSET_MONTHLY where asset_id is not null ";
        cn = mgDbCon.getConnection("");
        System.out.println("Records Delete from file AM_ASSET_MONTHLY");
        try
        {
            ds = cn.prepareStatement(Sapquery);
            done = ds.executeUpdate() != -1;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        System.out.println((new StringBuilder("====pagecode Before If ====> ")).append(pagecode).toString());
        System.out.println((new StringBuilder("====RecInsert Before If ====> ")).append(RecInsert).toString());
        if(pagecode.trim() == "1" || RecInsert != "")
        RecInsert = "No";
        query = "INSERT INTO [AM_ASSET_MONTHLY] SELECT * FROM [am_asset]  where asset_id is not n" +
"ull"
;
        con = mgDbCon.getConnection("");
        System.out.println((new StringBuilder("About to Insert records into file AM_ASSET_MONTHLY")).append(query).toString());
        try
        {
            for(i = i; i < 3; i++)
            {
                System.out.println((new StringBuilder("====records Before If ====> ")).append(i).toString());
                if(i == 1)
                {
                    System.out.println((new StringBuilder("====i 1 ====> ")).append(i).toString());
                    System.out.println((new StringBuilder("====pagecode Inside If ====> ")).append(pagecode).toString());
                    System.out.println((new StringBuilder("====RecInsert Inside If ====> ")).append(RecInsert).toString());
                    ps = cn.prepareStatement(query);
                    done = ps.executeUpdate() != -1;
                    pagecode = "1";
                    RecInsert = "No";
                    System.out.println(" Insert records Done");
                    System.out.println((new StringBuilder("====i 2 ====> ")).append(i).toString());
                    i++;
                    pagecode = "1";
                }
            }

            pagecode = "1";
            RecInsert = "No";
            System.out.println((new StringBuilder("====pagecode  ====> ")).append(pagecode).toString());
            System.out.println((new StringBuilder("====RecInsert ====> ")).append(RecInsert).toString());
 
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        try
        {
            if(con != null)
            {
                con.close();
            }
            System.out.println((new StringBuilder("====pagecode Window ====> ")).append(pagecode).toString());
            System.out.println((new StringBuilder("====RecInsert Window ====> ")).append(RecInsert).toString());
            out.print((new StringBuilder("<script>window.location='DocumentHelp.jsp?np=depreciationProcessing&RecInsert=")).append(RecInsert).append("&pagecode=").append(pagecode).append("'</script>").toString());
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        try
        {
            if(con != null)
            {
                con.close();
            }
            System.out.println((new StringBuilder("====pagecode Window ====> ")).append(pagecode).toString());
            System.out.println((new StringBuilder("====RecInsert Window ====> ")).append(RecInsert).toString());
            out.print((new StringBuilder("<script>window.location='DocumentHelp.jsp?np=depreciationProcessing&RecInsert=")).append(RecInsert).append("&pagecode=").append(pagecode).append("'</script>").toString());
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        try
        {
            if(con != null)
            {
                con.close();
            }
            System.out.println((new StringBuilder("====pagecode Window ====> ")).append(pagecode).toString());
            System.out.println((new StringBuilder("====RecInsert Window ====> ")).append(RecInsert).toString());
            out.print((new StringBuilder("<script>window.location='DocumentHelp.jsp?np=depreciationProcessing&RecInsert=")).append(RecInsert).append("&pagecode=").append(pagecode).append("'</script>").toString());
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
