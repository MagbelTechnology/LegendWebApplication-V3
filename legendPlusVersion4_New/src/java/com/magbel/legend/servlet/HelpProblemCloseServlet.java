/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magbel.legend.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import magma.net.dao.MagmaDBConnection;
import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.GenerateList;
import com.magbel.legend.bus.ComplaintManager;
import com.magbel.legend.bus.ProblemManager;
import com.magbel.legend.mail.MailSender;
import legend.admin.objects.ComplaintRequisition;
import legend.admin.objects.ComplaintResponse;
import com.magbel.util.ApplicationHelper;
import jakarta.servlet.http.HttpSession;

public class HelpProblemCloseServlet extends HttpServlet {
	
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    MagmaDBConnection mgDbCon = null;
    ApplicationHelper applHelper = null;
    MailSender mailSender = null;
    ApprovalRecords aprecords = null;
    SimpleDateFormat timer = null;
    GenerateList genList = null;

    public HelpProblemCloseServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        processRequisition(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        processRequisition(request, response);
    }

    private void processRequisition(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        mgDbCon = new MagmaDBConnection();
        applHelper = new ApplicationHelper();
        PrintWriter out = response.getWriter();
        mailSender = new MailSender();
        aprecords = new ApprovalRecords();
        timer = new SimpleDateFormat("kk:mm:ss");
        genList = new GenerateList();
        //boolean done = false;
        String returncode = request.getParameter("returncode");
        ComplaintResponse complaintResponse = new ComplaintResponse();

            System.out.println("returncode"+returncode);
        String operation = "Return"; 
        if (operation != null && operation.equalsIgnoreCase("Return")) {

            try {

                con = mgDbCon.getConnection("legendPlus");
             
          //      doneSave = true;          

             if (returncode != null && returncode.equalsIgnoreCase("DB")) {
                     out.print("<script>window.location='DocumentHelp.jsp?'</script>");

                } else {
                    out.print("<script>window.location='DocumentHelp.jsp?np=hdProblemList'</script>");

                }
            } catch (Exception e) {
                System.out.println("Error occurred when closing Window : " + e);
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

        }



    }
}