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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class MonthlyRegisterBackupCopy extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String CONTENT_TYPE = "text/xml";
    MagmaDBConnection mgDbCon = null;
    ApplicationHelper applHelper = null;
    Connection con = null;
    Connection cn = null;
    Statement stmt = null;
    boolean rs = false;
    boolean done = false;
     
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MonthlyRegisterBackupCopy() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        signatureRequest(request, response);

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        signatureRequest(request, response);
    }

    private void signatureRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
      //  String signature1 = request.getParameter("signature1");
        //String signature2 = request.getParameter("signature2");
System.out.println("About to Delete records from file AM_ASSET_MONTHLY");
        mgDbCon = new MagmaDBConnection();
        applHelper = new ApplicationHelper();

    	String Sapquery="delete from AM_ASSET_MONTHLY where asset_id is not null ";
        cn = mgDbCon.getConnection("fixedasset");
    //    response.setContentType(CONTENT_TYPE);
     //   PrintWriter out = response.getWriter();
    //    out.write("<bSect>");
 //       String Query = "Delete from "+filename+" where MTID = '"+MTID+"'";
  //      try {  
  //      	System.out.println("<<<<<<MTID to delete Cheque from Outward_Cheque>>>>> "+MTID);
  //      	System.out.println("<<<<<<MTID to delete Outward Cheque Outward_Cheque Query >>>>> "+Query);
      //      con = getConnection();
       //         ps = con.prepareStatement(Query);
         //       done = ps.executeUpdate() != -1;
                
        try {
         //   stmt = cn.prepareStatement(Sapquery);
        //   rs = stmt.executeQuery(Sapquery);
        	 stmt = cn.prepareStatement(Sapquery);
      rs= stmt.execute(Sapquery);
            String output = "<bSect>";
        } catch (SQLException e) {
            e.printStackTrace();
        } finally { 
            try {
                if (cn != null) {
                    cn.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
     /*           if (rs != null) {
                    rs.close();
                }  */
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    	String query="INSERT INTO AM_ASSET_MONTHLY SELECT * FROM am_asset ";
        con = mgDbCon.getConnection("fixedasset");
        System.out.println("About to Insert records into file AM_ASSET_MONTHLY");
        try {
            stmt = con.createStatement();
           // rs = stmt.executeQuery(query);
            rs= stmt.execute(Sapquery);
            String output = "<bSect>";
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            /*    if (rs != null) {
                    rs.close();
                }*/
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }
}
