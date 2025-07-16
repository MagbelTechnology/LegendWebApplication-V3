package com.magbel.legend.servlet;

 
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import magma.net.dao.MagmaDBConnection;
import com.magbel.util.*;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class DeleteFileServlet extends HttpServlet {
	  MagmaDBConnection mgDbCon = null;
	  ApplicationHelper applHelper = null;
	    Connection con = null;
	    Connection cn = null;
	    Statement stmt = null;
	    ResultSet rs = null;
	    Statement s = null; 
	    PreparedStatement ps = null;
	    PreparedStatement ds = null;
	  //  boolean rs = false;
	    boolean done = false;	  
	public DeleteFileServlet() {

	}

	/**
	 * Initializes the servlet.
	 * 
	 * @param config
	 *            ServletConfig
	 * @throws ServletException
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	/**
	 * Destroys the servlet.
	 */
	public void destroy() {

	}

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
        String records = request.getParameter("record");
        String pagecode = request.getParameter("pagecode");
        String RecInsert = request.getParameter("RecInsert");
    	System.out.println("====pagecode form JSP ====> "+pagecode);
    	System.out.println("====RecInsert From JSP ====> "+RecInsert);
      //  if(RecInsert==""){
      // 	  RecInsert = "No";
    	System.out.println("====pagecode First ====> "+pagecode);
    	System.out.println("====RecInsert firts ====> "+RecInsert);
        //String signature2 = request.getParameter("signature2");
        int i = 0;
        System.out.println("About to Delete records from file AM_ASSET_MONTHLY");
		mgDbCon = new MagmaDBConnection();
		applHelper = new ApplicationHelper();
        PrintWriter out = response.getWriter();
    	String Sapquery="delete from AM_ASSET_MONTHLY where asset_id is not null ";
        cn = mgDbCon.getConnection("legendPlus");  
    	// cn = mgDbCon.getConnection("fixedasset");
         System.out.println("Records Delete from file AM_ASSET_MONTHLY");
    //     String RecInsert = "";
        try { 
        	ds = cn.prepareStatement(Sapquery);
         	  done = ds.executeUpdate() != -1;            	   
        } catch (Exception e) {
            e.printStackTrace();
        } finally { 
           try {
            	if (cn != null) {
                    cn.close();
                } 
             /* if (ds != null) {
                	ds.close();
                }   
               if (rs != null) {
                    rs.close();
                }*/  
             //   con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            } 
       //     out.print("<script>window.location='DocumentHelp.jsp?np=depreciationProcessing&RecInsert="+RecInsert+"&pagecode="+pagecode+"'</script>");               
        }        

        }
}				
