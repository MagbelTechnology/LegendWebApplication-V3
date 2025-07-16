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

public class HelpTaskUpdateServlet extends HttpServlet {
	
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    MagmaDBConnection mgDbCon = null;
    ApplicationHelper applHelper = null;
    MailSender mailSender = null;
    ApprovalRecords aprecords = null;
    SimpleDateFormat timer = null;
    GenerateList genList = null;

    public HelpTaskUpdateServlet() {
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
        String userId = request.getParameter("userid");
        String status = request.getParameter("status");
        String Task_Title = request.getParameter("Task_Title");
        //String reqnID = request.getParameter("reqnId");
        String Task_Topic = request.getParameter("Task_Topic");
        String Task_Content = request.getParameter("Task_Content");
        String Task_Comments = request.getParameter("Task_Comments");
        String companyCode = request.getParameter("comp_code");
        String Task_id = request.getParameter("Task_id");
        String Email_Me_Before = request.getParameter("Email_Me_Before");
        String ScheduleStarteDate = request.getParameter("scheduleStartTime");
        String ScheduleEndDate = request.getParameter("scheduleEndTime");
        String ActualStartDate = request.getParameter("actualStartDate");
        String ActualEndDate = request.getParameter("actualEndDate");
        String technician = request.getParameter("technician_Id");
        String operation = request.getParameter("operation");   
        String returncode = request.getParameter("returncode");
        complaintResponse.setTask_Title(Task_Title);
        complaintResponse.setTask_Topic(Task_Topic);
        complaintResponse.setTask_Content(Task_Content);
        complaintResponse.setTask_Comments(Task_Comments);
        complaintResponse.setTask_id(Task_id);
        complaintResponse.setEmailMeBefore(Email_Me_Before);
        complaintResponse.setStatus(status);
        complaintResponse.setScheduleStarteDate(ScheduleStarteDate);
        complaintResponse.setScheduleEndDate(ScheduleEndDate);
        complaintResponse.setActualStartDate(ActualStartDate);
        complaintResponse.setActualEndDate(ActualEndDate);
        complaintResponse.setTechnician(technician);
    
        String currentUserName = "";
        HttpSession session = request.getSession();
        com.magbel.admin.objects.User user = null;
        if (session.getAttribute("_user") != null) {
            user = (com.magbel.admin.objects.User) session.getAttribute("_user");

            currentUserName = user.getUserFullName();
        }
        
        if (operation != null && operation.equalsIgnoreCase("Save")) {
            //String complaintId = "Incd/" + applHelper.getGeneratedId("HD_TASK");
            String insertReqnTable = "";
        
            try {

                con = mgDbCon.getConnection("legendPlus");
             
                doneSave = true;          

             if (doneSave) {
                        ProblemManager hdManager = new ProblemManager();
                     hdManager.updateTask(complaintResponse);                  

                    //About to send email to recipients
                    String realPath = getServletConfig().getServletContext().getRealPath("/legendPlus/legendPlus.properties");
                    FileInputStream fis = new FileInputStream(realPath);
                   
                    out.print("<script>alert('Response saved successfully.');</script>");
                    if (returncode != null && returncode.equalsIgnoreCase("DB")) {
                    out.print("<script>window.location='DocumentHelp.jsp?'</script>");
                    } else {
                        out.print("<script>window.location='DocumentHelp.jsp?np=hdTaskList'</script>");
                    }
                    
                } else {  
                    out.print("<script>alert('Response  not save.');</script>");
                    if (returncode != null && returncode.equalsIgnoreCase("DB")) {
                        out.print("<script>window.location='DocumentHelp.jsp?'</script>");
                        } else {                    
                    out.print("<script>window.location='DocumentHelp.jsp?np=hdTaskList'</script>");
                        }
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