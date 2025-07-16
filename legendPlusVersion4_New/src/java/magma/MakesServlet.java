package magma;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import magma.net.dao.MagmaDBConnection;

/**
 * <p>Title: Magma.net System</p>
 *
 * <p>Description: Fixed Assets Manager</p>
 *
 * <p>Copyright: Copyright (c) 2006. All rights reserved.</p>
 *
 * <p>Company: Magbel Technologies Limited.</p>
 *
 * @author Charles Ayoola Ayodele-Peters.
 * @version 1.0
 */


public class MakesServlet extends HttpServlet {
	
    private static final String CONTENT_TYPE = "text/xml";
    //@todo set DTD
    private static final String DOC_TYPE = null;
    
	private MagmaDBConnection dbConnection = null;

    //Initialize global variables
    public void init() throws ServletException {
    }

    //Process the HTTP Get request
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        //Selected Category
        String cat = request.getParameter("cat");
        if (cat == null) {
            cat = "0";
        }

        response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();
        out.write("<message>");
        //out.println("<?xml version=\"1.0\"?>");

        Connection con = null;
    	Statement stmt = null;
    	ResultSet rs = null;
       
        try {
            //rs = new legend.ConnectionClass().getStatement().executeQuery("select assetmake_id, assetmake from am_gb_assetmake where category_id = " + cat);
        	dbConnection = new MagmaDBConnection();
			con = dbConnection.getConnection("legendPlus");
			stmt = con.createStatement();
			rs = stmt.executeQuery("select assetmake_id, assetmake from am_gb_assetmake where category_id = " + cat+" and  STATUS = 'ACTIVE'");
            while (rs.next()) {
                out.write("<make>");
                out.write("<id>");
                out.write(rs.getString(1));
                out.write("</id>");
                out.write("<name>");
                out.write(rs.getString(2));
                out.write("</name>");
                out.write("</make>");
            }
   
        out.write("</message>");

        if (DOC_TYPE != null) {
            out.println(DOC_TYPE);
        }
         
        
        }
        catch (Exception ex) 
        {
        	ex.printStackTrace();
         }
        finally
	    {
	        	  try 
	        	{
				  if(con !=null) {
					 con.close();
				  }
				  if(stmt !=null) {
					  stmt.close();
					  }
				  if(rs !=null) {
						 rs.close();
					  }
	        	 }
	        	  catch(Exception ex){
	              	ex.printStackTrace();
	              }
	         
	    }
   
        
        
    }

    //Clean up resources
    public void destroy() {
    }
}
