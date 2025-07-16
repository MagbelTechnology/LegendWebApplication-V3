/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package magma;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.magbel.util.DataConnect;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

/**
 *
 * @author Matanmi
 */
public class descriptservlet extends HttpServlet {
    private static final String CONTENT_TYPE = "text/xml";
    //@todo set DTD
    private static final String DOC_TYPE = null;

    //Initialize global variables
    public void init() throws ServletException {
    }

    //Process the HTTP Get request
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        String asset_Id = request.getParameter("asset_Id");
        if(asset_Id == null)
        {
        	asset_Id = "0";
        }
        response.setContentType("text/xml");
        PrintWriter out = response.getWriter();
        out.write("<message>");
ResultSet rs= null;
Connection con = null;
	     
	     PreparedStatement ps = null;
try
        {
           String query = "SELECT DESCRIPTION,DESCRIPTION FROM AM_ASSET WHERE ASSET_ID = ? " ;
//           System.out.println("<<<<<barCodeservlet query: "+query+"    asset_Id: "+asset_Id);
              con = (new DataConnect("legendPlus")).getConnection();
	      ps = con.prepareStatement(query);
          ps.setString(1, asset_Id);
	      rs = ps.executeQuery();
           while(rs.next()) {
//        	   System.out.println("About to start Looping");
            out.write("<narrate>");
                out.write("<id>");
                //out.write(rs.getString(1));
                out.write(rs.getString(1));
                out.write("</id>");
                out.write("<description>");
                out.write(rs.getString(2).replaceAll("&", "&amp;"));
                out.write("</name>");
             out.write("</description>");
 //              System.out.println("The Description Value is LLLLLLLLLLLLLLLLLL"+rs.getString(2));
           }





            /*
            for( rs = (new ConnectionClass()).getStatement().executeQuery((new StringBuilder()).append("select ATTACH_ID, SBU_NAME from AM_SBU_ATTACHEMENT where ATTACH_ID = ").append(branch_id).toString()); rs.next(); out.write("</make>"))
            {
                System.out.println("==============foud the record ========================");
                out.write("<make>");
                out.write("<id>");
                out.write(rs.getString(1));
                out.write("</id>");
                out.write("<name>");
                out.write(rs.getString(2));
                out.write("</name>");
            }
            */

//rs.close();
        }
        catch(SQLException ex) { }
        catch(Exception ex) { }
        finally{
       //out.close();
            closeConnection(con,ps,rs);
        }
        out.write("</message>");
        if(DOC_TYPE != null)
        {
            out.println(DOC_TYPE);
        }
    }

    public void destroy()
    {
    }
private void closeConnection(Connection con, PreparedStatement ps,
                                 ResultSet rs) {
        try {
//System.out.println("=============inside closeConnection==========");
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }

        } catch (Exception e) {
            System.out.println("WANR: Error closing connection >>" + e);
        }
    }
    
}
