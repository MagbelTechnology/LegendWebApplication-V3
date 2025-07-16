/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.magbel.ia.servlet;

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
public class Paymenttypeservlet extends HttpServlet {
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
        String Paymenttype = request.getParameter("isCash");
//        System.out.print("Paymenttypeservlet Paymenttype: "+Paymenttype);
 /*       if(stateCode == null)
        {
        	stateCode = "0";
        }*/  
       // int stateID = Integer.parseInt(stateCode);
        response.setContentType("text/xml");
        PrintWriter out = response.getWriter();
        out.write("<message>");
ResultSet rs= null;
Connection con = null;
	     
	     PreparedStatement ps = null;
try
        {
           String query = "SELECT LEDGER_NO,DESCRIPTION FROM IA_GL_ACCT_LEDGER "+Paymenttype+" " ;
//           System.out.println("Paymenttypeservlet query: "+query);
              con = (new DataConnect("ias")).getConnection();
	      ps = con.prepareStatement(query);
	      rs = ps.executeQuery();
           while(rs.next()) {
            out.write("<make>");
                out.write("<id>");
                out.write(rs.getString(1));
                out.write("</id>");
                out.write("<name>");
                out.write(rs.getString(2).replaceAll("&", "&amp;"));
                out.write("</name>");
             out.write("</make>");
               System.out.println("The value of name is Field: "+rs.getString(2));
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
