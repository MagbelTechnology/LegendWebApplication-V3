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
import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.GenerateList;
import com.magbel.legend.bus.ComplaintManager;
import com.magbel.admin.handlers.MailSender;
import com.magbel.util.ApplicationHelper;
import javax.servlet.http.HttpSession;

public class HelpDeskNewChangeServlet extends HttpServlet {

    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    MagmaDBConnection mgDbCon = null;
    ApplicationHelper applHelper = null;
    MailSender mailSender = null;
    ApprovalRecords aprecords = null;
    SimpleDateFormat timer = null;
    GenerateList genList = null;

    public HelpDeskNewChangeServlet() {
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




        String category = request.getParameter("category");
        String subcategory = request.getParameter("subcategory");
        String userId = request.getParameter("userid");
        String priority = request.getParameter("priority");
        String status = request.getParameter("status");
        String technician = request.getParameter("technician");
        String servicesAffected = request.getParameter("servicesAffected");
        String requesterName = request.getParameter("requesterName");
        String requesterContactNum = request.getParameter("requesterContNumber");
        String notifyEmail = request.getParameter("notifyEmail");
        String requestBranch = request.getParameter("requesterBranch");
        String requestDepartment = request.getParameter("requesterDept");
        String ReqSection = request.getParameter("requesterSection");
        String requestTitle = request.getParameter("requesttitle");
        String requestChange = request.getParameter("requestchange");
        String requestDescription = request.getParameter("requestDescription");
        String assetId = request.getParameter("asset");
        String changeType = request.getParameter("changeType");
        String scheduleStartTime = request.getParameter("scheduleStartTime");
        String scheduleEndTime = request.getParameter("scheduleEndTime");
        
       
        String operation = request.getParameter("operation");
        String mtid = request.getParameter("mtid");
        String companyCode = request.getParameter("comp_code");
       
        String requesterId = request.getParameter("requesterId");

        String currentUserName = "";
        HttpSession session = request.getSession();
        legend.admin.objects.User user = null;
        if (session.getAttribute("_user") != null) {
            user = (legend.admin.objects.User) session.getAttribute("_user");

            currentUserName = user.getUserFullName();


        }
  
        if (operation != null && operation.equalsIgnoreCase("Save")) {
            String ChangeId = "Incd/" + applHelper.getGeneratedId("HD_CHANGE");
            String insertReqnTable = "";
          		 

            insertReqnTable = " insert into HD_CHANGES (Change_id,Category_Code,sub_category_Code,"
                    + " priority,create_date,status,user_id,"
                    + " requester_Name,requester_Contact_No,work_Station_IP,notify_Email,request_Branch,request_Department,"
                    + " Company_Code,request_Section,Change_Title,Change_Description,"
                    + "create_time,technician,requesterId,services_affected"
                    + ",change_type,schedule_start_time,schedule_end_time,image) "
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
          
            try {

                con = mgDbCon.getConnection("helpDesk");

                pstmt = con.prepareStatement(insertReqnTable);
                pstmt.setString(1, ChangeId);
                pstmt.setString(2, category);
                pstmt.setString(3, subcategory);
             //   pstmt.setString(4, assetId);
                pstmt.setString(4, priority);
                pstmt.setTimestamp(5, mgDbCon.getDateTime(new java.util.Date()));
                pstmt.setString(6, status);
                pstmt.setInt(7, Integer.parseInt(userId));
                pstmt.setString(8, requesterName);
                pstmt.setString(9, requesterContactNum);
                pstmt.setString(10, request.getRemoteAddr());
                pstmt.setString(11, notifyEmail);                
                 pstmt.setString(12, requestBranch);
                pstmt.setString(13, requestDepartment);
                pstmt.setString(14, companyCode);
                pstmt.setString(15, ReqSection);
                pstmt.setString(16, requestTitle);
                pstmt.setString(17, requestDescription);
                pstmt.setString(18, timer.format(new java.util.Date()));
                pstmt.setString(19, technician);
                pstmt.setString(20, requesterId);
             //   pstmt.setString(22, "Change");
                pstmt.setString(21, servicesAffected);
                pstmt.setString(22, changeType);
                pstmt.setString(23, scheduleStartTime);
                pstmt.setString(24, scheduleEndTime);
                
             doneSave = (pstmt.executeUpdate() == -1); //undo these

               // String incidentQuery = "SELECT  description   FROM   HD_COMPLAINT_TYPE where complaint_type_code = " + Integer.parseInt(complaintType);
              //  String incidentName = aprecords.getCodeName(incidentQuery);

                if (!doneSave) {

                    //insert image if available
//                    String sessionId = request.getSession().getId();
//                    if (aprecords.getIncidentImage(sessionId) != null) {
//                        aprecords.setChangeImage(aprecords.getChangeImage(sessionId), ChangeId);
//                    }
//
//                    // to delete temporary images from image table
//                    genList.updateTable(" delete from am_ad_image where sessionId = '" + sessionId+"'");



                    //About to send email to recipients
                    String realPath = getServletConfig().getServletContext().getRealPath("/mailConfig/legend.properties");
                    FileInputStream fis = new FileInputStream(realPath);
                    ComplaintManager hdManager = new ComplaintManager();
//                    hdManager.sendRecipientMail(fis, hdManager.getRecipientEmail(reqnID), reqnID);

                    if (notifyEmail.contains(";")) {
                        String[] mailReciepents = notifyEmail.split(";");
                        hdManager.sendRecipientsMail(fis, mailReciepents, ChangeId, requestTitle, requestDescription, currentUserName);
                    } else {
                        String[] mailReciepents = new String[]{notifyEmail};
                        hdManager.sendRecipientsMail(fis, mailReciepents, ChangeId, requestTitle, requestDescription, currentUserName);
                    }


                    out.print("<script>alert('Problem saved successfully.');</script>");
                    out.print("<script>window.location='DocumentHelp.jsp?np=hdNewRequisitionChangeForm&reqnId='</script>");


                } else {
                    out.print("<script>alert('Problem  not save.');</script>");
                    out.print("<script>window.location='DocumentHelp.jsp?np=hdNewRequisitionChangeForm&reqnId='</script>");

                }
            } catch (Exception e) {
                System.out.println("Error occurred when saving Problem requisition : " + e);
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