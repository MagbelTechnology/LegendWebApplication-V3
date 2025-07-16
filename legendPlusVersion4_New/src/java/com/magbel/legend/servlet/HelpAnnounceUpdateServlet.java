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
import com.magbel.legend.mail.BulkMail;
import legend.admin.objects.ComplaintRequisition;
import legend.admin.objects.ComplaintResponse;
import com.magbel.util.ApplicationHelper;
import jakarta.servlet.http.HttpSession;

public class HelpAnnounceUpdateServlet extends HttpServlet {
	
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

    public HelpAnnounceUpdateServlet() {
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
        mails= new com.magbel.admin.mail.EmailSmsServiceBus();        
        mail= new BulkMail();
        df = new   com.magbel.util.DatetimeFormat();

        ComplaintResponse complaintResponse = new ComplaintResponse();

        boolean doneSave = false;
        String userId = request.getParameter("userid");
        String status = request.getParameter("status");
        String Announcetitle = request.getParameter("Announcetitle");
        String Announcecontents = request.getParameter("Announcecontents");
        String companyCode = request.getParameter("comp_code");
        String Announce_id = request.getParameter("Announce_id");
        String Sendmail = request.getParameter("Sendmail");
        String RestrictAnnouncement = request.getParameter("Restrict");
        String emailto = request.getParameter("emailto");
        String emailcopy = request.getParameter("emailcopy");
        String scheduleStartTime = request.getParameter("scheduleStartTime");
        String scheduleEndTime = request.getParameter("scheduleEndTime");
        String operation = request.getParameter("operation");  
        String returncode = request.getParameter("returncode");
        System.out.print("===== Sendmail =====> "+Sendmail);
        String pageType = request.getParameter("pageType");
        String pageName = request.getParameter("pageName");
        String FileName = "HD_ANNOUNCEMENT";
        String FieldName = "Announce_id";
        String technician = userId;   
        String category = "000";
        String scheduleEndTimeNULL = df.AddYear();
        complaintResponse.setAnnounce_Title(Announcetitle);
        complaintResponse.setAnnounce_Content(Announcecontents);
        complaintResponse.setAnnounce_id(Announce_id);
        System.out.println("-------RestrictAnnouncement-----? 0 "+RestrictAnnouncement);
		String mailCheck = "";
        if (Sendmail == null || Sendmail == "") {
        	 mailCheck = "N";
	        } else {
        	 mailCheck = "Y";
        }	  
        complaintResponse.setSend_Mail(Sendmail); 
        complaintResponse.setRestrictAnnouncement(RestrictAnnouncement);
        complaintResponse.setScheduleStarteDate(scheduleStartTime);
        if (scheduleEndTime.equalsIgnoreCase("NULL")){ 
        	complaintResponse.setScheduleEndDate(scheduleEndTimeNULL); 
         }   
        
        if (!scheduleEndTime.equalsIgnoreCase("NULL")){ 
     	   complaintResponse.setScheduleEndDate(scheduleEndTime);     	   
         }
     
        complaintResponse.setEmail_To(emailto);
        complaintResponse.setEmail_Copy(emailcopy);
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
            	 System.out.print("===== doneSave =====> "+doneSave);
            		 String url = getServletConfig().getServletContext().getRealPath("");
            //		 System.out.print("===== mailCheck =====>2 "+mailCheck);
            	 if (mailCheck.equalsIgnoreCase("Y")){
           // 		 System.out.print("===== mailCheck =====>3 "+mailCheck);
         		//	mails.SimpleMailWithAttachment(url, "ANNOUNCEMENT", mail.getEmailMessageAnnouce(userId, Announcecontents,Announce_id,Announcetitle,emailcopy),emailto,userId );
            			mails.SimpleMailWithAttachment(url, Announcetitle, mail.getEmailMessageAnnouce(userId, Announcecontents,Announce_id,Announcetitle,emailcopy,Announcetitle),emailto,userId,emailcopy );
            	 }   
                        ProblemManager hdManager = new ProblemManager();
                     hdManager.updateAnnouncements(complaintResponse);                  

                    //About to send email to recipients
                    String realPath = getServletConfig().getServletContext().getRealPath("/legendPlus/legendPlus.properties");
                    FileInputStream fis = new FileInputStream(realPath);
                   
                    out.print("<script>alert('Response saved successfully.');</script>");
                    if (returncode != null && returncode.equalsIgnoreCase("DB")) {
                        out.print("<script>window.location='DocumentHelp.jsp?'</script>");
                        } else {                    
                    out.print("<script>window.location='DocumentHelp.jsp?np=hdAnnouncementList'</script>");
                        }
                } else {
                    out.print("<script>alert('Response  not save.');</script>");
                    if (returncode != null && returncode.equalsIgnoreCase("DB")) {
                        out.print("<script>window.location='DocumentHelp.jsp?'</script>");
                        } else {                                        
                    out.print("<script>window.location='DocumentHelp.jsp?np=hdAnnouncementList'</script>");
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