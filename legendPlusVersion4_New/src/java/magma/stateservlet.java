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
import magma.net.dao.MagmaDBConnection;

/**
 *
 * @author Olabo
 */
public class stateservlet extends HttpServlet {
    private static final String CONTENT_TYPE = "text/xml";
    //@todo set DTD
    private static final String DOC_TYPE = null;
     private MagmaDBConnection dbConnection;

    //Initialize global variables
    public void init() throws ServletException {
    }

    //Process the HTTP Get request
    //@Override
    public void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
      String branchid = request.getParameter("branch_id");

        if(branchid == null)
        {
            branchid = "0";
        }
        int branchID = Integer.parseInt(branchid);
        response.setContentType("text/xml");
       
           response.setHeader("Cache-Control", "no-cache");
      //  System.out.println("=================the branch id is "+ branchID);
           PrintWriter out = response.getWriter();
           out.write("<message>");

           String query = "SELECT STATE" + " FROM am_ad_branch  " + " WHERE branch_id= " + branchID;
       // System.out.println(" the first query is ============================= " +query);
           Connection con = null;
           PreparedStatement ps = null;
           ResultSet rs = null;
           PreparedStatement ps2 = null;
        ResultSet rs2 = null;
           try {

               con = (new DataConnect("legendPlus")).getConnection();
               ps = con.prepareStatement(query);
               rs = ps.executeQuery();
               System.out.println("================here 1");
               while (rs.next()) {
               
                   String iq ="SELECT [state_name]+'-'+[state_Code]"
                          +" FROM [am_gb_states]"
                          +" WHERE   [state_ID]= "+ rs.getInt(1); //+Integer.parseInt(rs.getInt(1));
                  // System.out.println("==================== the query is " +iq);


                  
                  // System.out.println("==================== branch id is "+Integer.parseInt(rs.getString(1)));

                    
                   ps2 = con.prepareStatement(iq);
               rs2 = ps2.executeQuery();
               if(rs2.next()){
                   out.write("<state>");
                   out.write("<id>");
                   out.write(rs.getString(1));
                   out.write("</id>");
                   out.write("<name>");
                   out.write(rs2.getString(1));
                   out.write("</name>");
                   out.write("</state>");
               }
            
               }

           }

           catch (Exception e) {
               String warning = "WARNING:Error Fetching state from stateservlet " +
                                " ->" + e.getMessage();
               System.out.println(warning);
           } finally {
               closeConnection(con, ps, rs);
           }
           out.write("</message>");

           if (DOC_TYPE != null) {
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
