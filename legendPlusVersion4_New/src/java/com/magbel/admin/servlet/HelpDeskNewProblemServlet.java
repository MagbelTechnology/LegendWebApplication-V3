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
import com.magbel.admin.handlers.MailSender;
import com.magbel.admin.mail.BulkMail;
import com.magbel.util.ApplicationHelper;

import javax.servlet.http.HttpSession;

public class HelpDeskNewProblemServlet extends HttpServlet {

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
    
    public HelpDeskNewProblemServlet() {
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



        boolean doneSave = false;




        String category = request.getParameter("category");
        String subcategory = request.getParameter("subcategory");
        String userId = request.getParameter("userid");
        String priority = request.getParameter("priority");
        String status = request.getParameter("status");
        String technician = request.getParameter("technicianId");
        String servicesAffected = request.getParameter("servicesAffected");
        String requesterName = request.getParameter("requesterName");
        String requesterContactNum = request.getParameter("requesterContNumber");
        String notifyEmail = request.getParameter("notifyEmail");
        String requestBranch = request.getParameter("requesterBranch");
        String requestDepartment = request.getParameter("requesterDept");
        String ReqSection = request.getParameter("requesterSection");
        String requestSubject = request.getParameter("requestSubject");
        String requestDescription = request.getParameter("requestDescription");
        String assetId = request.getParameter("asset");
       // String complaintType = request.getParameter("complaintType");
       // String incidentMode = request.getParameter("incidentMode");
       // String responseMode = request.getParameter("responseMode");          
        String operation = request.getParameter("operation");
        String mtid = request.getParameter("mtid");
        String companyCode = request.getParameter("comp_code");
       
        String requesterId = request.getParameter("requesterId");

        String currentUserName = "";
        String FileName = "HD_PROBLEM";
        String FieldName = "complaint_id";
        String pageType = request.getParameter("pageType");
        String pageName = request.getParameter("pageName");
        String SlaQuery = "SELECT  sla_ID   FROM   am_gb_sla where SLA_Code = "+priority+"";
        String SlaIdent = aprecords.getCodeName(SlaQuery);
        int Slaid = Integer.parseInt(SlaIdent);

        HttpSession session = request.getSession();
        com.magbel.admin.objects.User user = null;
        if (session.getAttribute("_user") != null) {
            user = (com.magbel.admin.objects.User) session.getAttribute("_user");

            currentUserName = user.getUserFullName();


        }

        if (operation != null && operation.equalsIgnoreCase("Save")) {
            String complaintId = "Procd/" + applHelper.getGeneratedId("HD_PROBLEM");
            String insertReqnTable = "";

            
            insertReqnTable = " insert into HD_PROBLEM (complaint_id,complain_category,complain_sub_category,"
                    + " asset_id,priority,create_date,status,user_id,"
                    + " requester_Name,requester_Contact_No,work_Station_IP,notify_Email,request_Branch,request_Department,"
                    + " Company_Code,request_Section,request_Subject,request_Description,"
                    + "create_time,technician,requester_id,nature,services_affected,sla_id) "
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
          
            try {
                con = mgDbCon.getConnection("helpDesk");

                pstmt = con.prepareStatement(insertReqnTable);
                pstmt.setString(1, complaintId);
                pstmt.setString(2, category);
                pstmt.setString(3, subcategory);
                pstmt.setString(4, assetId);
                pstmt.setString(5, priority);
                pstmt.setTimestamp(6, mgDbCon.getDateTime(new java.util.Date()));
                pstmt.setString(7, status);
                pstmt.setInt(8, Integer.parseInt(userId));
                pstmt.setString(9, requesterName);
                pstmt.setString(10, requesterContactNum);
                pstmt.setString(11, request.getRemoteAddr());
                pstmt.setString(12, notifyEmail);
                 pstmt.setString(13, requestBranch);
                pstmt.setString(14, requestDepartment);                
                pstmt.setString(15, companyCode);
                pstmt.setString(16, ReqSection);                
                pstmt.setString(17, requestSubject);
                pstmt.setString(18, requestDescription);
                pstmt.setString(19, timer.format(new java.util.Date()));
                if (technician != "") {
                    pstmt.setInt(20, Integer.parseInt(technician));                	
                }      
                if (technician == "") {
                	technician = "0";
                    pstmt.setInt(20, Integer.parseInt(technician));                	
                }                     
                //pstmt.setString(20, technician);
                pstmt.setString(21, requesterId);
                pstmt.setString(22, "normal");
                pstmt.setString(23, servicesAffected);
                pstmt.setInt(24, Slaid);
             doneSave = (pstmt.executeUpdate() == -1); //undo these
           
               // String incidentQuery = "SELECT  description   FROM   HD_COMPLAINT_TYPE where complaint_type_code = " + Integer.parseInt(complaintType);
              //  String incidentName = aprecords.getCodeName(incidentQuery);
 
                if (!doneSave) { 
               	 String mailnotify = aprecords.getmailaddresses(); 
            	 if(notifyEmail.equalsIgnoreCase("")){
                	 notifyEmail = mailnotify.substring(0, mailnotify.length()-1);
                	 } else {
                    	 notifyEmail = notifyEmail + ";" +mailnotify.substring(0, mailnotify.length()-1);
                     }

                	String url = getServletConfig().getServletContext().getRealPath("");
                	   System.out.println("<<<<<< NOT doneSave url >>>>>> "+url);
                	mails.SimpleMailWithAttachment(url, "PROBLEM", mail.getEmailMessageOriginal(status, category, "",pageType, technician, userId, requestDescription,complaintId,requestSubject,FileName,FieldName,"","","","","",""),notifyEmail,userId,"");											
                	 System.out.println("<<<<<< NOT doneSave 3 >>>>>> "+doneSave);
                    //insert image if available
                    String sessionId = request.getSession().getId();
                    if (aprecords.getProblemImage(sessionId) != null) {
                     //   aprecords.setProblemImage(aprecords.getProblemImage(sessionId), complaintId);
                        aprecords.setProblemImage(sessionId, userId, complaintId,pageName);
                    }

                    // to delete temporary images from image table
                    //genList.updateTable(" delete from am_ad_image where sessionId = '" + sessionId+"'");



                    //About to send email to recipients
                    String realPath = getServletConfig().getServletContext().getRealPath("/mailConfig/legend.properties");
                    FileInputStream fis = new FileInputStream(realPath);
                    ComplaintManager hdManager = new ComplaintManager();
//                    hdManager.sendRecipientMail(fis, hdManager.getRecipientEmail(reqnID), reqnID);

                    if (notifyEmail.contains(";")) {
                        String[] mailReciepents = notifyEmail.split(";");
         //               hdManager.sendRecipientsMail(fis, mailReciepents, complaintId, requestSubject, requestDescription, currentUserName);
                    } else {
                        String[] mailReciepents = new String[]{notifyEmail};
          //              hdManager.sendRecipientsMail(fis, mailReciepents, complaintId, requestSubject, requestDescription, currentUserName);
                    }


                    out.print("<script>alert('Problem saved successfully.');</script>");
                    out.print("<script>window.location='DocumentHelp.jsp?np=hdNewRequisitionProbleForm&reqnId='</script>");


                } else {
                    out.print("<script>alert('Problem  not save.');</script>");
                    out.print("<script>window.location='DocumentHelp.jsp?np=hdNewRequisitionProbleForm&reqnId='</script>");

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