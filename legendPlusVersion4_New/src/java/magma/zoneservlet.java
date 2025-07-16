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
 * @author Olabo
 */
public class zoneservlet extends HttpServlet {
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
        String regionCode = request.getParameter("regionCode");
        if(regionCode == null)
        {
        	regionCode = "0";
        }
        int region_code = Integer.parseInt(regionCode);
        response.setContentType("text/xml");
        PrintWriter out = response.getWriter();
        out.write("<message>");
ResultSet rs= null;
Connection con = null;
	     
	     PreparedStatement ps = null;
try
        {
            //System.out.println("this is the doGet() of the sbuservlet the value of branch _id is ============= " +branch_id+" ================");
    //System.out.println("The value of branchID is LLLLLLLLLLLLLLLLLL"+branchID);
        // String query = "select ATTACH_ID, SBU_NAME from AM_SBU_ATTACHEMENT where ATTACH_ID = ? order by sbu_name" ;
           String query = "select ZONE_CODE, ZONE_CODE+' - '+ZONE_NAME from AM_AD_ZONE where REGION_CODE = ? order by ZONE_NAME" ;
              con = (new DataConnect("legendPlus")).getConnection();
	      ps = con.prepareStatement(query);
          ps.setInt(1, region_code);
	      rs = ps.executeQuery();
           while(rs.next()) {
            out.write("<make>");
                out.write("<id>");
                //out.write(rs.getString(1));
                out.write(rs.getString(1));
                out.write("</id>");
                out.write("<name>");
                out.write(rs.getString(2).replaceAll("&", "&amp;"));
                out.write("</name>");
             out.write("</make>");
               System.out.println("The value of name is LLLLLLLLLLLLLLLLLL"+rs.getString(2));
           }


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
