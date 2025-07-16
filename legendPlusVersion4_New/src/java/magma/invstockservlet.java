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
import javax.servlet.ServletException;
import javax.servlet.http.*;

/**
 *
 * @author Matanmi
 */
public class invstockservlet extends HttpServlet {
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
        String param = request.getParameter("itemType");
 //       System.out.println("param in invstockservlet: "+param);
        String[] param_array = param.split("&");
        String itemType = param_array[0]; 
        String warehouseCode = param_array[1];
        response.setContentType("text/xml");
        PrintWriter out = response.getWriter();
        out.write("<message>");
        ResultSet rs= null;
        Connection con = null;
	     
	     PreparedStatement ps = null;
try
        {
           String query = "select ASSET_ID, DESCRIPTION from ST_STOCK where ITEM_CODE = '"+itemType+"' AND WAREHOUSE_CODE = '"+warehouseCode+"' AND ASSET_STATUS = 'ACTIVE' order by DESCRIPTION" ;
              con = (new DataConnect("fixedasset")).getConnection();
 //             System.out.println("invstockservlet query: "+query);
 //             System.out.println("invstockservlet param: "+param+"   itemType: "+itemType+"    warehouseCode: "+warehouseCode);
	      ps = con.prepareStatement(query);
         // ps.setString (1, itemType);
        //  ps.setString (2, warehouseCode);
	      rs = ps.executeQuery();
           while(rs.next()) {
            out.write("<make>");
                out.write("<id>");
                //out.write(rs.getString(1));
                out.write(rs.getString(1));
 //               System.out.println("Asset Id>>>>>>: "+rs.getString(1));
                out.write("</id>");
                out.write("<name>");
                out.write(rs.getString(2).replaceAll("&", "&amp;"));
                out.write("</name>");
             out.write("</make>");
 //              System.out.println("The value of name is LLLLLLLLLLLLLLLLLL"+rs.getString(2));
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
