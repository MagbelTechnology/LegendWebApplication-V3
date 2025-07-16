/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magbel.admin.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.magbel.admin.dao.MagmaDBConnection;
import com.magbel.admin.handlers.ApprovalRecords;
import com.magbel.admin.handlers.GenerateList;
import com.magbel.admin.handlers.ComplaintManager;
import com.magbel.admin.handlers.ProblemManager;
import com.magbel.admin.handlers.MailSender;
import com.magbel.admin.objects.ComplaintRequisition;
import com.magbel.admin.objects.ComplaintResponse;
import com.magbel.util.ApplicationHelper;
import javax.servlet.http.HttpSession;

public class HelpSolutionUpdateServlet extends HttpServlet {

    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    MagmaDBConnection mgDbCon = null;
    ApplicationHelper applHelper = null;
    MailSender mailSender = null;
    ApprovalRecords aprecords = null;
    SimpleDateFormat timer = null;
    GenerateList genList = null;

    public HelpSolutionUpdateServlet() {
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
        boolean done = false;
        ComplaintResponse complaintResponse = new ComplaintResponse();

        boolean doneSave = false;
       //for inserrt to hd response
        String userId = request.getParameter("userid");
        String status = request.getParameter("status");
        String Solution_Title = request.getParameter("Solution_Title");
        //String reqnID = request.getParameter("reqnId");
        String Solution_Topic = request.getParameter("Solution_Topic");
        String Solution_Content = request.getParameter("Solution_Content");
        String Solution_Keywords = request.getParameter("Solution_Keywords");
        String Solution_Comments = request.getParameter("Solution_Comments");
        String companyCode = request.getParameter("comp_code");
        String Solution_id = request.getParameter("Solution_id");
        String operation = request.getParameter("operation");   
        complaintResponse.setSolution_Title(Solution_Title);
        complaintResponse.setSolution_Topic(Solution_Topic);
        complaintResponse.setSolution_Content(Solution_Content);
        complaintResponse.setSolution_Keywords(Solution_Keywords);
        complaintResponse.setSolution_Comments(Solution_Comments);
        complaintResponse.setSolution_id(Solution_id);
        //complaintResponse.setReqnID(reqnID);
        complaintResponse.setStatus(status);
        String currentUserName = "";
        HttpSession session = request.getSession();
        com.magbel.admin.objects.User user = null;
        if (session.getAttribute("_user") != null) {
            user = (com.magbel.admin.objects.User) session.getAttribute("_user");

            currentUserName = user.getUserFullName();
        }

        if (operation != null && operation.equalsIgnoreCase("Save")) {
            //String complaintId = "Incd/" + applHelper.getGeneratedId("HD_COMPLAINT");
            String insertReqnTable = "";
        
            try {

                con = mgDbCon.getConnection("helpDesk");
             
                doneSave = true;          

             if (doneSave) {
                        ProblemManager hdManager = new ProblemManager();
                     hdManager.updateSolution(complaintResponse);                  

                    //About to send email to recipients
                    String realPath = getServletConfig().getServletContext().getRealPath("/mailConfig/legend.properties");
                    FileInputStream fis = new FileInputStream(realPath);
                   
                    out.print("<script>alert('Response saved successfully.');</script>");
                    out.print("<script>window.location='DocumentHelp.jsp?np=hdSolutionList'</script>");

                } else {
                    out.print("<script>alert('Response  not save.');</script>");
                    out.print("<script>window.location='DocumentHelp.jsp?np=hdSolutionList'</script>");

                }
            } catch (Exception e) {
                System.out.println("Error occurred when saving complaint response : " + e);
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