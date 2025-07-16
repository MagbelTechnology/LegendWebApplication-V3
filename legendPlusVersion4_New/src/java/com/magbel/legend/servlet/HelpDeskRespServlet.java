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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import magma.net.dao.MagmaDBConnection;
import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.GenerateList;
import com.magbel.legend.bus.ComplaintManager;
import com.magbel.legend.mail.MailSender;
import com.magbel.util.ApplicationHelper;
import javax.servlet.http.HttpSession;

public class HelpDeskRespServlet extends HttpServlet {

    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    MagmaDBConnection mgDbCon = null;
    ApplicationHelper applHelper = null;
    MailSender mailSender = null;
    ApprovalRecords aprecords = null;
    SimpleDateFormat timer = null;
    GenerateList genList = null;

    public HelpDeskRespServlet() {
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
       
        String userId = request.getParameter("userid");
        String status = request.getParameter("status");
        String requesterName = request.getParameter("requesterName");
        String requesterContactNum = request.getParameter("requesterContNumber");
        String notifyEmail = request.getParameter("notifyEmail");
        String receipientId = request.getParameter("requesterId");
        String requestDescription = request.getParameter("requestDescriptionNew");
        String operation = request.getParameter("operation");
        String companyCode = request.getParameter("comp_code");
        String complaintId = request.getParameter("reqnId");
        String requestSubject = request.getParameter("requestSubject");
        
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



            insertReqnTable = " insert into HD_RESPONSE (complaint_id, create_date,status,user_id,"
                    + " requester_Name,requester_Contact_No,workStationIP,notify_Email,"
                    + " request_Description,create_time,recipient_Id) "
                    + " values (?,?,?,?,?,?,?,?,?,?,?)";
          
            try {

                con = mgDbCon.getConnection("helpDesk");

                pstmt = con.prepareStatement(insertReqnTable);
                
                pstmt.setString(1, complaintId);
                pstmt.setTimestamp(2, mgDbCon.getDateTime(new java.util.Date()));
                pstmt.setString(3, status);
                pstmt.setInt(4, Integer.parseInt(userId));
                pstmt.setString(5, requesterName);
                pstmt.setString(6, requesterContactNum);
                pstmt.setString(7, request.getRemoteAddr());
                pstmt.setString(8, notifyEmail);
                pstmt.setString(9, requestDescription);
                pstmt.setString(10, timer.format(new java.util.Date()));
                pstmt.setInt(11, Integer.parseInt(receipientId));
                

             doneSave = (pstmt.executeUpdate() == -1); //undo these

               // String incidentQuery = "SELECT  description   FROM   HD_COMPLAINT_TYPE where complaint_type_code = " + Integer.parseInt(complaintType);
               // String incidentName = aprecords.getCodeName(incidentQuery);

                if (!doneSave) {

                    //insert image if available
                    String sessionId = request.getSession().getId();
                    if (aprecords.getIncidentImage(sessionId) != null) {
                        aprecords.setIncidentImage(aprecords.getIncidentImage(sessionId), complaintId);
                    }

                    // to delete temporary images from image table
                   // genList.updateTable(" delete from am_ad_image where sessionId = '" + sessionId+"'");

                    //About to send email to recipients
                    String realPath = getServletConfig().getServletContext().getRealPath("/legendPlus/legend.properties");
                    FileInputStream fis = new FileInputStream(realPath);
                    ComplaintManager hdManager = new ComplaintManager();
//                    hdManager.sendRecipientMail(fis, hdManager.getRecipientEmail(reqnID), reqnID);

                    if (notifyEmail.contains(";")) {
                        String[] mailReciepents = notifyEmail.split(";");
                        hdManager.sendRecipientsMail(fis, mailReciepents, complaintId, requestSubject, requestDescription, currentUserName);
                    } else {
                        String[] mailReciepents = new String[]{notifyEmail};
                        System.out.println("=====About to enter sendRecipientsMail === ");
                        hdManager.sendRecipientsMail(fis, mailReciepents, complaintId, requestSubject, requestDescription, currentUserName);
                    }


                    out.print("<script>alert('Response saved successfully.');</script>");
                    out.print("<script>window.location='DocumentHelp.jsp?np=hdComplaintDashBoard'</script>");


                } else {
                    out.print("<script>alert('Response  not save.');</script>");
                    out.print("<script>window.location='DocumentHelp.jsp?np=hdComplaintDashBoard'</script>");

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