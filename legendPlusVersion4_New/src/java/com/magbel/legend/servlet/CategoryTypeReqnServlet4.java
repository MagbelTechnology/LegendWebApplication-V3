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
public class CategoryTypeReqnServlet4 extends HttpServlet {

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
    public CategoryTypeReqnServlet4() {
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
        String assetId = request.getParameter("assetId");
        String description = "";

        mgDbCon = new MagmaDBConnection();


        String assetDescription = "select description from am_asset where asset_id= '" + assetId+"'";


        con = mgDbCon.getConnection("legendPlus");
        response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();
		String userClass = (String) request.getSession().getAttribute("UserClass");

        try {
        	 if (!userClass.equals("NULL") || userClass!=null){
            stmt = con.createStatement();
            rs = stmt.executeQuery(assetDescription);

            while (rs.next()) {
                description = rs.getString("Description");
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
