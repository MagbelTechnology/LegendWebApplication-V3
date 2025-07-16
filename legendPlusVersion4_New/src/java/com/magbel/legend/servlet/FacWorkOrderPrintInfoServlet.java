package com.magbel.legend.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import magma.net.dao.MagmaDBConnection;

import com.magbel.util.*;

import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AjaxRequisitionServlet
 */
public class FacWorkOrderPrintInfoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String CONTENT_TYPE = "text/xml";
    MagmaDBConnection mgDbCon = null;
    ApplicationHelper applHelper = null;
    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
     PreparedStatement pstmt = null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FacWorkOrderPrintInfoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        printInfoRequest(request, response);

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        printInfoRequest(request, response);


    }

    private void printInfoRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int userId = request.getParameter("userid")==null?0:Integer.parseInt(request.getParameter("userid"));
        String workOrderCode = request.getParameter("workOrderCode");
        String isPrinted =request.getParameter("isPrinted");
        String sysIp = request.getRemoteAddr();
        String userClass = (String) request.getSession().getAttribute("UserClass");
        boolean done = false;
        
        if (!userClass.equals("NULL") || userClass!=null){
        	
        if(isPrinted.equalsIgnoreCase("") ||isPrinted.equalsIgnoreCase("N")){
        mgDbCon = new MagmaDBConnection();


        String printQuery = "insert into fm_work_order_print (Work_order_code,Print_Date,Printed_By,WorkStationIP) values(?,?,?,?)" ;


        con = mgDbCon.getConnection("legendPlus");
        response.setContentType(CONTENT_TYPE);
       // PrintWriter out = response.getWriter();


        try {
            pstmt = con.prepareStatement(printQuery);
            pstmt.setString(1,workOrderCode);
           pstmt.setTimestamp(2, mgDbCon.getDateTime(new java.util.Date()));
           pstmt.setInt(3,userId);
           pstmt.setString(4,sysIp);
           done = (pstmt.executeUpdate() == -1);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }//for if
    }
    }
}
