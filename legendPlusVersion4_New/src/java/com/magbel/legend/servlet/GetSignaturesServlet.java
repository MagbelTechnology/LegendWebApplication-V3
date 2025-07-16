package com.magbel.legend.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
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

public class GetSignaturesServlet extends HttpServlet {

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
    public GetSignaturesServlet() {
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
        String signature1 = request.getParameter("signature1");
        //String signature2 = request.getParameter("signature2");

        mgDbCon = new MagmaDBConnection();
        applHelper = new ApplicationHelper();

        String signatureQuery = " SELECT User_Code,User_Name  FROM FM_SIGNATURE WHERE STATUS='ACTIVE' AND USER_CODE != '"+signature1+"'";
        con = mgDbCon.getConnection("legendPlus");
        response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();
        out.write("<bSect>");

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(signatureQuery);
            String output = "<bSect>";
            while (rs.next()) {
                System.out.println("Here      hhhhhhhhhhh                 "+ rs.getString(1));
                out.write("<section>");
                out.write("<sectcode>");
                out.write(rs.getString(1));
                out.write("</sectcode>");
                out.write("<sectname>");
                out.write(rs.getString(2).replaceAll("&", "&amp;"));
                out.write("</sectname>");
                out.write("</section>");
                //output= output + "<section><sectcode>" + rs.getString(1)+"</sectcode><sectname>"+
                //rs.getString(2)+ "</sectname></section>"+ "\n";
            }
            out.write("</bSect>");
            output = output + "</bSect>";
            //System.out.println("output >>>>>>>> "+ "\n" + output);
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
