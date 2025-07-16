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
public class CurrencyServlet extends HttpServlet {
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
        int countryCode = request.getParameter("CountryCode")== null?0:Integer.parseInt(request.getParameter("CountryCode"));
      //  System.out.println(">>>>>>>>> the country code is >>>>>>>>>> " + countryCode);
       response.setContentType("text/html");
           response.setHeader("Cache-Control", "no-cache");

        PrintWriter out = response.getWriter();


try
        {

           out.write(getCurrencyData(countryCode));


        }

        catch(Exception ex) { }
        finally{

        }
        //out.write("</message>");
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


private String getCurrencyData(int currencyCode){

String values= "";

ResultSet rs= null;
Connection con = null;

	     PreparedStatement ps = null;

    try {

        String query = "select Iso_Code, Currency_code,Currency_symbol,Currency_Description from am_gb_country where country_code = ?" ;
              con = (new DataConnect("fixedasset")).getConnection();
	      ps = con.prepareStatement(query);
          ps.setInt(1, currencyCode);
	      rs = ps.executeQuery();
           while(rs.next()) {

           values =  rs.getString(1)+ ":"+rs.getString(2)+":"+rs.getString(3)+":"+rs.getString(4);
//System.out.println(">>>>>>>>> the values is >>>>>>>>>> " + values);
           }

    } catch (Exception e) {

    System.out.println("WANR: Error closing connection >>" + e);

    }


return values;
}




}
