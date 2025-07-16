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
import com.magbel.admin.handlers.ApprovalRecords;
import com.magbel.admin.handlers.GenerateList;
//import com.magbel.admin.handlers.ComplaintManager;
import com.magbel.admin.handlers.MailSender;
import com.magbel.admin.mail.BulkMail;

import com.magbel.util.ApplicationHelper;
import jakarta.servlet.http.HttpSession;

public class HelpDeskNewAnnounceServlet extends HttpServlet {

    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    MagmaDBConnection mgDbCon = null;
    ApplicationHelper applHelper = null;
    MailSender mailSender = null;
    ApprovalRecords aprecords = null;
    SimpleDateFormat timer = null; 
    GenerateList genList = null;
    com.magbel.admin.mail.EmailSmsServiceBus mails = null;
    BulkMail mail=null;

	  com.magbel.util.DatetimeFormat df;
    public HelpDeskNewAnnounceServlet() {
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
        df = new   com.magbel.util.DatetimeFormat();
        applHelper = new ApplicationHelper();
        PrintWriter out = response.getWriter();
        mailSender = new MailSender();
        aprecords = new ApprovalRecords();
        timer = new SimpleDateFormat("kk:mm:ss");
        genList = new GenerateList();
        boolean done = false;  
        mails= new com.magbel.admin.mail.EmailSmsServiceBus();
        mail= new BulkMail();

        boolean doneSave = false;
        String Announcetitle = request.getParameter("Announcetitle");
        String Announcecontents = request.getParameter("Announcecontents");
        String userId = request.getParameter("userid");
        String Sendmail = request.getParameter("Sendmail");
        String Restrict = request.getParameter("Restrict");
        String status = request.getParameter("status");
        String emailto = request.getParameter("emailto");
  //      System.out.println("===In Servlet Sendmail======"+Sendmail);
        String emailcopy = request.getParameter("emailcopy");
        String scheduleStartTime = request.getParameter("scheduleStartTime");
        String DDMM = scheduleStartTime.substring(0, 5);
    //    System.out.println("======scheduleStartTime===>>>>> "+scheduleStartTime);
        String scheduleEndTime = request.getParameter("scheduleEndTime");
        String operation = request.getParameter("operation");
        String mtid = request.getParameter("mtid");
        String companyCode = request.getParameter("comp_code");
        String pageType = request.getParameter("pageType");
        String pageName = request.getParameter("pageName");
        String FileName = "HD_ANNOUNCEMENT";
        String FieldName = "Announce_id";
        String category = "000";
        String technician = userId;
        String currentUserName = "";
        HttpSession session = request.getSession();
        com.magbel.admin.objects.User user = null;
        if (session.getAttribute("_user") != null) {
            user = (com.magbel.admin.objects.User) session.getAttribute("_user");

            currentUserName = user.getUserFullName();

        }
		String mailCheck = "";
        if (Sendmail == null || Sendmail == "") {
        	 mailCheck = "N";
	        } else {
        	 mailCheck = "Y";
        }	  

        if (operation != null && operation.equalsIgnoreCase("Save")) {
            String AnnounceId = "Incd/" + applHelper.getGeneratedId("HD_ANNOUNCEMENT");
            String insertReqnTable = "";   
         
            insertReqnTable = " insert into HD_ANNOUNCEMENT (Announce_id,Announce_Title,"
                    + " Announce_Content,SEND_MAIL,Email_TO,Email_COPY,create_date,user_id,"
                    + " work_Station_IP,create_time,Schedule_Start_Date,Schedule_End_Date,Company_Code,Announcement_limit) "
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
       
            try {
            	
                con = mgDbCon.getConnection("legendPlus");

                pstmt = con.prepareStatement(insertReqnTable);
                pstmt.setString(1, AnnounceId);
                pstmt.setString(2, Announcetitle);
                pstmt.setString(3, Announcecontents);
                pstmt.setString(4, mailCheck);
                pstmt.setString(5, emailto);
                pstmt.setString(6, emailcopy);          
        //        System.out.println("<<<<mgDbCon.getDateTime(new java.util.Date()) >>>>>"+mgDbCon.getDateTime(new java.util.Date()));
                 
                System.out.println("======DDMM+df.AddYear()===== "+DDMM+df.AddYear());
                pstmt.setTimestamp(7, mgDbCon.getDateTime(new java.util.Date()));
                pstmt.setString(8, userId);
                pstmt.setString(9, request.getRemoteAddr());
                pstmt.setString(10, timer.format(new java.util.Date()));  
                pstmt.setDate(11, df.dateConvert(scheduleStartTime));
                if (scheduleEndTime.equals(null) || scheduleEndTime == ""){                 	                
                	scheduleEndTime = DDMM+df.AddYear();
                }  
            //    System.out.println("======DDMM scheduleEndTime 1 ===== "+scheduleEndTime);
          //      System.out.println("======df.dateConvert(scheduleEndTime) ===== "+df.dateConvert(scheduleEndTime));
                	 pstmt.setDate(12, df.dateConvert(scheduleEndTime));     
                pstmt.setString(13, companyCode); 
                pstmt.setString(14, Restrict);
             doneSave = (pstmt.executeUpdate() == -1); //undo these         	
                if (!doneSave) {
                		String url = getServletConfig().getServletContext().getRealPath("");
                         
                	if (mailCheck.equalsIgnoreCase("Y")){
                     	mails.SimpleMailWithAttachment(url, Announcetitle, mail.getEmailMessageAnnouce(userId, Announcecontents,AnnounceId,Announcetitle,emailcopy,Announcetitle),emailto,userId,emailcopy);      
                	} 

                    String sessionId = request.getSession().getId();
                   // if (aprecords.getIncidentImage(sessionId) != null) {
                    //    aprecords.setIncidentImage(aprecords.getIncidentImage(sessionId), taskId);
                    //}

                    // to delete temporary images from image table
                  //  genList.updateTable(" delete from am_ad_image where sessionId = '" + sessionId+"'");



                    //About to send email to recipients
                    String realPath = getServletConfig().getServletContext().getRealPath("/legendPlus/legendPlus.properties");
                    //FileInputStream fis = new FileInputStream(realPath);
                   // ComplaintManager hdManager = new ComplaintManager();
   

                    out.print("<script>alert('Task saved successfully.');</script>");
                    out.print("<script>window.location='DocumentHelp.jsp?np=hdNewAnnouncementForm&reqnId='</script>");

                } else {
                    out.print("<script>alert('Task  not save.');</script>");
                    out.print("<script>window.location='DocumentHelp.jsp?np=hdNewAnnouncementForm&reqnId='</script>");

                }
            } catch (Exception e) {
            	e.printStackTrace();
                System.out.println("Error occurred when saving Announcement : " + e);
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