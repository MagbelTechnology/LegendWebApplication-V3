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
//import com.magbel.admin.handlers.ComplaintManager;
import com.magbel.admin.handlers.MailSender;
import com.magbel.util.ApplicationHelper;
import javax.servlet.http.HttpSession;

public class HelpDeskNewSolutionServlet extends HttpServlet {

    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    MagmaDBConnection mgDbCon = null;
    ApplicationHelper applHelper = null;
    MailSender mailSender = null;
    ApprovalRecords aprecords = null;
    SimpleDateFormat timer = null;
    GenerateList genList = null;

    public HelpDeskNewSolutionServlet() {
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


        boolean doneSave = false;




        String title = request.getParameter("title");
        String topic = request.getParameter("topic");
        String userId = request.getParameter("userid");
        String contents = request.getParameter("contents");
        String keywords = request.getParameter("keywords");
        String comments = request.getParameter("comments");
        String companyCode = request.getParameter("comp_code");
        String pageName = request.getParameter("pageName");
        String operation = request.getParameter("operation");
       // String mtid = request.getParameter("mtid");
       // String companyCode = request.getParameter("comp_code");
        System.out.println("===In Servlet 1======");
        String currentUserName = "";
        HttpSession session = request.getSession();
        com.magbel.admin.objects.User user = null;
        if (session.getAttribute("_user") != null) {
            user = (com.magbel.admin.objects.User) session.getAttribute("_user");

            currentUserName = user.getUserFullName();


        }

        if (operation != null && operation.equalsIgnoreCase("Save")) {
            String solutionId = "Incd/" + applHelper.getGeneratedId("HD_SOLUTION");
            String insertReqnTable = "";

System.out.println("===In Servlet 2======");

            insertReqnTable = " insert into HD_SOLUTION (solution_id,Solution_title,Solution_topic,"
                    + " Solution_Content,Solution_Keywords,Solution_Comments,create_date,user_id,"
                    + " work_Station_IP,create_time,Company_Code) "
                    + " values (?,?,?,?,?,?,?,?,?,?,?)";
          
            try {

                con = mgDbCon.getConnection("helpDesk");

                pstmt = con.prepareStatement(insertReqnTable);
                pstmt.setString(1, solutionId);
                pstmt.setString(2, title);
                pstmt.setString(3, topic);
                pstmt.setString(4, contents);
                pstmt.setString(5, keywords);
                pstmt.setString(6, comments);
                pstmt.setTimestamp(7, mgDbCon.getDateTime(new java.util.Date()));
                pstmt.setString(8, userId);
                 pstmt.setString(9, request.getRemoteAddr());
                 pstmt.setString(10, timer.format(new java.util.Date()));
                 pstmt.setString(11, companyCode);
                
                  
             doneSave = (pstmt.executeUpdate() == -1); //undo these

                if (!doneSave) {

                    //insert image if available
                    String sessionId = request.getSession().getId();
                    if (aprecords.getIncidentImage(sessionId) != null) {                    	                    	
                    	aprecords.setProblemImage(sessionId,userId,solutionId,pageName);
                 //       aprecords.setIncidentImage(aprecords.getIncidentImage(sessionId), solutionId);
                    }

                    // to delete temporary images from image table
                   // genList.updateTable(" delete from am_ad_image where sessionId = '" + sessionId+"'");



                    //About to send email to recipients
                    String realPath = getServletConfig().getServletContext().getRealPath("/mailConfig/legend.properties");
                    //FileInputStream fis = new FileInputStream(realPath);
                   // ComplaintManager hdManager = new ComplaintManager();
   


                    out.print("<script>alert('Solution saved successfully.');</script>");
                    out.print("<script>window.location='DocumentHelp.jsp?np=hdNewRequisitionSolutionForm&reqnId='</script>");


                } else {
                    out.print("<script>alert('Solution  not save.');</script>");
                    out.print("<script>window.location='DocumentHelp.jsp?np=hdNewRequisitionSolutionForm&reqnId='</script>");

                }
            } catch (Exception e) {
                System.out.println("Error occurred when saving Solution : " + e);
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