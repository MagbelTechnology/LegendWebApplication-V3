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
import com.magbel.admin.mail.BulkMail;
import com.magbel.admin.objects.ComplaintRequisition;
import com.magbel.admin.objects.ComplaintResponse;
import com.magbel.util.ApplicationHelper;
import javax.servlet.http.HttpSession;

public class HelpProblemRespServlet2 extends HttpServlet {
 
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
    
    public HelpProblemRespServlet2() {
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
        ComplaintResponse complaintResponse = new ComplaintResponse();

        boolean doneSave = false;
       //for inserrt to hd response
        String userId = request.getParameter("userid");
        String status = request.getParameter("status");
       // String requesterName = request.getParameter("requesterName");
       // String requesterContactNum = request.getParameter("requesterContNumber");
        String notifyEmail = request.getParameter("notifyEmail");
       // String receipientId = request.getParameter("requesterId");
        String servicesAffected = request.getParameter("servicesAffected");
        String requestDescription = request.getParameter("requestDescriptionNew");
        String operation = request.getParameter("operation");
        String companyCode = request.getParameter("comp_code");
        String complaintId = request.getParameter("reqnId");
        String requestSubject = request.getParameter("requestSubject");
 
//        System.out.print("=======status===== "+status);   
//       System.out.print("=======requestDescription===== "+requestDescription);
//       System.out.print("=======complaintId===== "+complaintId);   
        //for update of hd complaint
        //subject,description,technician,asset,
        String category = request.getParameter("category");
        String subcategory = request.getParameter("subcategory");
        String priority = request.getParameter("priority");
        String complaintType = request.getParameter("complaintType");
        String incidentMode = request.getParameter("incidentMode");
        String technician = request.getParameter("technicianId");
        String assetId = request.getParameter("asset");
        String pageType = request.getParameter("pageType");
        String SlaQuery = "SELECT  sla_ID   FROM   am_gb_sla where SLA_Code = "+priority+"";
        String SlaIdent = aprecords.getCodeName(SlaQuery);
        int Slaid = Integer.parseInt(SlaIdent);
       // System.out.println("<<<<Slaid>>>> "+Slaid);
        complaintResponse.setCategory(category);
        complaintResponse.setSubCategory(subcategory);
        complaintResponse.setPriority(priority);
        complaintResponse.setComplainType(complaintType);
        complaintResponse.setIncidentMode(incidentMode);
        complaintResponse.setTechnician(technician);
        complaintResponse.setservicesAffected(servicesAffected);
        complaintResponse.setAssetId(assetId);
        complaintResponse.setRequestSubject(requestSubject);
        complaintResponse.setRequestDescription(requestDescription);
        complaintResponse.setReqnID(complaintId);
        complaintResponse.setStatus(status);
        complaintResponse.setSlaid(Slaid);
        String currentUserName = "";
        String FileName = "HD_PROBLEM";
        String FieldName = "complaint_id";
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
                    + " workStationIP,notify_Email,"
                    + " request_Description,create_time,recipient_Id,complain_category,complain_sub_category"
                    + ",priority,complaint_Type,incident_Mode,asset_id) "
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
          
            try {

                con = mgDbCon.getConnection("helpDesk");

                pstmt = con.prepareStatement(insertReqnTable);
                
                pstmt.setString(1, complaintId);
                pstmt.setTimestamp(2, mgDbCon.getDateTime(new java.util.Date()));
                pstmt.setString(3, status);
                pstmt.setInt(4, Integer.parseInt(userId));
//                pstmt.setString(5, requesterName);
//                pstmt.setString(6, requesterContactNum);
                pstmt.setString(5, request.getRemoteAddr());
                pstmt.setString(6, notifyEmail);
                pstmt.setString(7, requestDescription);
                pstmt.setString(8, timer.format(new java.util.Date()));
                pstmt.setInt(9, Integer.parseInt(technician));
                pstmt.setString(10, category);
                pstmt.setString(11, subcategory);
                pstmt.setString(12, priority);
                pstmt.setString(13, complaintType);
                pstmt.setString(14, incidentMode);
                pstmt.setString(15, assetId);

             doneSave = (pstmt.executeUpdate() == -1); //undo these

               // String incidentQuery = "SELECT  description   FROM   HD_COMPLAINT_TYPE where complaint_type_code = " + Integer.parseInt(complaintType);
               // String incidentName = aprecords.getCodeName(incidentQuery);             
//             System.out.println("I Am in the Servlet");
             
                
             if (!doneSave) {
                 String url = getServletConfig().getServletContext().getRealPath("");
//                 System.out.println("-----url---- "+url);
//                 System.out.println("Mail Parameters 1 status "+status+" category "+category+" pageType "+pageType+" technician "+technician);
//                 System.out.println("Mail Parameters 2 technician "+technician+" userId "+userId+" requestDescription "+requestDescription+" complaintId "+complaintId);
//                 System.out.println("Mail>>>>>"+mail.getEmailMessage(status, category, pageType, technician, userId, requestDescription,complaintId));
//                 mails.SimpleMailWithAttachment(url, "INCIDENCE", mail.getEmailMessage(status, category, pageType, technician, userId, requestDescription,complaintId),notifyEmail,userId );            
                        ProblemManager hdManager = new ProblemManager();
                     hdManager.updateProblemRes(complaintResponse);
  //                	mails.SimpleMailWithAttachment(url, "PROBLEM", mail.getEmailMessageOriginal(status, category, pageType, technician, userId, requestDescription,complaintId,requestSubject,FileName,FieldName),notifyEmail,userId );
                    //insert image if available
//                    String sessionId = request.getSession().getId();
//                    if (aprecords.getIncidentImage(sessionId) != null) {
//                        aprecords.setIncidentImage(aprecords.getIncidentImage(sessionId), complaintId);
//                    } 
  
                    // to delete temporary images from image table
                   // genList.updateTable(" delete from am_ad_image where sessionId = '" + sessionId+"'");
                 
                     


                    //About to send email to recipients
                    String realPath = getServletConfig().getServletContext().getRealPath("/mailConfig/legend.properties");
                    FileInputStream fis = new FileInputStream(realPath);
                   
//                    hdManager.sendRecipientMail(fis, hdManager.getRecipientEmail(reqnID), reqnID);

                    if (notifyEmail.contains(";")) {
                        String[] mailReciepents = notifyEmail.split(";");
                  //      hdManager.sendRecipientsMail(fis, mailReciepents, complaintId, requestSubject, requestDescription, currentUserName);
                    } else {
                        String[] mailReciepents = new String[]{notifyEmail};
                    //    hdManager.sendRecipientsMail(fis, mailReciepents, complaintId, requestSubject, requestDescription, currentUserName);
                    }


                    out.print("<script>alert('Response saved successfully.');</script>");
                    out.print("<script>window.location='DocumentHelp.jsp?np=hdProblemList'</script>");


                } else {
                    out.print("<script>alert('Response  not save.');</script>");
                    out.print("<script>window.location='DocumentHelp.jsp?np=hdProblemList'</script>");

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