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
//import com.magbel.admin.handlers.ComplaintManager;
import com.magbel.legend.mail.MailSender;
import com.magbel.util.ApplicationHelper;
import jakarta.servlet.http.HttpSession;

public class HelpDeskNewTaskServlet extends HttpServlet {
	  com.magbel.util.DatetimeFormat df;
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    MagmaDBConnection mgDbCon = null;
    ApplicationHelper applHelper = null;
    MailSender mailSender = null;
    ApprovalRecords aprecords = null;
    SimpleDateFormat timer = null;
    GenerateList genList = null;

    public HelpDeskNewTaskServlet() {
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
        df = new   com.magbel.util.DatetimeFormat();
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
        String status = request.getParameter("status");
        String emailmebefore = request.getParameter("emailmebefore");
        String comments = request.getParameter("comments");
        String scheduleStartTime = request.getParameter("scheduleStartTime");
        String scheduleEndTime = request.getParameter("scheduleEndTime");
        String actualStartDate = request.getParameter("actualStartDate");
        String actualEndDate = request.getParameter("actualEndDate");
        String operation = request.getParameter("operation");
        String companyCode = request.getParameter("comp_code");
        String Technician = request.getParameter("technicianId");
       // System.out.print("====request.getParameter== "+Technician);
        String pageType = request.getParameter("pageType");
        String pageName = request.getParameter("pageName");
        String FileName = "HD_COMPLAINT";
        String FieldName = "complaint_id";

        String currentUserName = "";
        HttpSession session = request.getSession();
        legend.admin.objects.User user = null;
        if (session.getAttribute("_user") != null) {
            user = (legend.admin.objects.User) session.getAttribute("_user");

            currentUserName = user.getUserFullName();


        }

        if (operation != null && operation.equalsIgnoreCase("Save")) {
            String taskId = "Incd/" + applHelper.getGeneratedId("HD_TASK");
            String insertReqnTable = "";

            //System.out.println("===In Servlet 2======");

            insertReqnTable = " insert into HD_TASK (Task_id,Task_title,Task_topic,"
                    + " Task_Content,Task_Comments,create_date,user_id,"
                    + " work_Station_IP,create_time,Schedule_Start_Date,Schedule_End_Date,Actual_Start_Date,Actual_End_Date,status,Email_Me_Before,Technician,Company_Code) "
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
          
            try {
  
                con = mgDbCon.getConnection("legendPlus");

                pstmt = con.prepareStatement(insertReqnTable);
                pstmt.setString(1, taskId);
                pstmt.setString(2, title);
                pstmt.setString(3, topic);
                pstmt.setString(4, contents);
                pstmt.setString(5, comments);
                pstmt.setTimestamp(6, mgDbCon.getDateTime(new java.util.Date()));
                pstmt.setString(7, userId);
                 pstmt.setString(8, request.getRemoteAddr());
                 pstmt.setString(9, timer.format(new java.util.Date()));
                 pstmt.setDate(10, df.dateConvert(scheduleStartTime));
                 pstmt.setDate(11, df.dateConvert(scheduleEndTime));
                 pstmt.setDate(12, df.dateConvert(actualStartDate));
                 pstmt.setDate(13, df.dateConvert(actualEndDate));
                 pstmt.setString(14, status);
                 pstmt.setString(15, emailmebefore);
                 if (Technician != "") {
                     pstmt.setInt(16, Integer.parseInt(Technician));                	
                 }      
                 if (Technician == "") {
                 	Technician = "0";
                     pstmt.setInt(16, Integer.parseInt(Technician));                	
                 }     
              //   pstmt.setString(16, Technician);
                 pstmt.setString(17, companyCode);
             doneSave = (pstmt.executeUpdate() == -1); //undo these

                if (!doneSave) {

                    //Update image if available
                    String sessionId = request.getSession().getId();
                    if (aprecords.getIncidentImage(sessionId) != null) {
                    	aprecords.setProblemImage(sessionId,userId,taskId,pageName);
                        //aprecords.setIncidentImage(aprecords.getIncidentImage(sessionId), taskId);
                    }

                    // to delete temporary images from image table
                    genList.updateTable(" delete from am_ad_image where sessionId = '" + sessionId+"'");



                    //About to send email to recipients
                    String realPath = getServletConfig().getServletContext().getRealPath("/legendPlus/legendPlus.properties");
                    //FileInputStream fis = new FileInputStream(realPath);
                   // ComplaintManager hdManager = new ComplaintManager();
   

                    out.print("<script>alert('Task saved successfully.');</script>");
                    out.print("<script>window.location='DocumentHelp.jsp?np=hdNewTaskForm&reqnId='</script>");

                } else {
                    out.print("<script>alert('Task  not save.');</script>");
                    out.print("<script>window.location='DocumentHelp.jsp?np=hdNewTaskForm&reqnId='</script>");

                }
            } catch (Exception e) {
                System.out.println("Error occurred when saving Task : " + e);
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