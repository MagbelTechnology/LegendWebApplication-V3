package com.magbel.legend.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import magma.net.dao.MagmaDBConnection;
import com.magbel.util.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class AjaxRequisitionServlet
 */
public class VendorInfoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String CONTENT_TYPE = "text/xml";
    MagmaDBConnection mgDbCon = null;
    ApplicationHelper applHelper = null;
    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public VendorInfoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        assetDescriptionRequest(request, response);

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        assetDescriptionRequest(request, response);


    }

    private void assetDescriptionRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String vendorCode = request.getParameter("vendorCode");
        String description = "";
        String contactPerson ="";
        String contactAddress="";
        mgDbCon = new MagmaDBConnection();
        String userClass = (String) request.getSession().getAttribute("UserClass");

        String vendorQuery = "select Contact_Person,Contact_Address" +
                " from AM_AD_VENDOR where Vendor_Code ='" + vendorCode + "'";;


        con = mgDbCon.getConnection("");
        response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();
        System.out.println("<<<<<<vendorQuery in assetDescriptionRequest: "+vendorQuery);

        try {
        	if (!userClass.equals("NULL") || userClass!=null){
            stmt = con.createStatement();
            rs = stmt.executeQuery(vendorQuery);

            while (rs.next()) {
                
                contactPerson = rs.getString("Contact_Person");
                contactAddress = rs.getString("Contact_Address");
                System.out.println("<<<<<<contactPerson: "+contactPerson+"    contactAddress: "+contactAddress);
               // description = description +":"+rs.getString("Contact_Address");
            }
            if(contactPerson != null) description = contactPerson;
            else{
            contactPerson ="";
            description = contactPerson;
            }
            if(contactAddress != null) description = description + ":"+contactAddress;
            else{
            contactAddress="";
            description = description + ":"+contactAddress;
            }
            
            out.write(description);
        }
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
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }
}
