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
import com.magbel.admin.handlers.ApprovalRecords;
import com.magbel.admin.handlers.GenerateList;
import com.magbel.admin.handlers.ComplaintManager;
import com.magbel.admin.handlers.MailSender;
import com.magbel.admin.mail.BulkMail;
import com.magbel.util.ApplicationHelper;
import javax.servlet.http.HttpSession;

public class HelpDeskNewRequisitionGuestServlet04_06_2012 extends HttpServlet {

    Connection con = null;
    Connection cn = null;
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
    

    public HelpDeskNewRequisitionGuestServlet04_06_2012() {
        super();
    }

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        processRequisition(request, response);
    }
    
    private void processRequisition(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        mgDbCon = new MagmaDBConnection();
        applHelper = new ApplicationHelper();
        PrintWriter out = response.getWriter();
        mailSender = new MailSender();
        com.magbel.util.DatetimeFormat df;
        aprecords = new ApprovalRecords();
        timer = new SimpleDateFormat("kk:mm:ss");
        genList = new GenerateList();
        boolean done = false;
        mails= new com.magbel.admin.mail.EmailSmsServiceBus();        
        mail= new BulkMail();
        df = new com.magbel.util.DatetimeFormat();

        boolean doneSave = false;
        String category = request.getParameter("category");
        System.out.println("=====Category====  "+category);
        String subcategory = request.getParameter("subcategory");
        String userId = request.getParameter("userid");
        String priority = request.getParameter("priority");
        String status = "001";//request.getParameter("status");
        String assetId = request.getParameter("asset");
        String requesterName = request.getParameter("requesterName");
        String requesterContactNum = request.getParameter("requesterContNumber");
        String notifyEmail = request.getParameter("notifyEmail");
        String pageName = request.getParameter("pageName");
        String requestSubject = request.getParameter("requestSubject");
        String requestDescription = request.getParameter("requestDescription");
        String operation = request.getParameter("operation");
        String companyCode = request.getParameter("comp_code");
        String pageType = request.getParameter("pageType");
        String FileName = "HD_COMPLAINT";
        String FieldName = "complaint_id";
        String IssueCode = "001";
        String incidentMode = "001"; 
        String Comptype = "001";
        String responseMode = "001";
        String requesterId = request.getParameter("requesterId");
        String TransactionType = request.getParameter("TransactionType");
        //String technician = userId;
        String TechQuery = "SELECT UnitHaed_Id, UnitHead   FROM   AM_AD_DEPARTMENT WHERE Dept_code = '"+category+"'";
        String technician = aprecords.getCodeName(TechQuery);
        String currentUserName = "";
        String SlaQuery = "SELECT  criteria_ID   FROM   SLA_RESPONSE where Dept_Code = '"+category+"' AND Cat_Code = '"+subcategory+"' ";
        String SlaIdent = aprecords.getCodeName(SlaQuery);        
        SlaIdent = (SlaIdent == null || SlaIdent.equals(""))?"0":SlaIdent;
        int Slaid = Integer.parseInt(SlaIdent);
        String incidentQuery = "SELECT  description   FROM   HD_COMPLAINT_TYPE where complaint_type_code = " + Comptype;
        String incidentName = aprecords.getCodeName(incidentQuery);        
        String UnitHeadMailQuery = "SELECT  email   FROM   AM_AD_DEPARTMENT where Dept_code = " + category;
        String UnitHeadMail = aprecords.getCodeName(UnitHeadMailQuery);
        String StatusQry = "select status_description from hd_status where status_code= " + status;
        String Status = aprecords.getCodeName(StatusQry);
        HttpSession session = request.getSession();
        com.magbel.admin.objects.User user = null;
        
        response.setContentType("text/xml");
        if (session.getAttribute("_user") != null) {
            user = (com.magbel.admin.objects.User) session.getAttribute("_user");
            currentUserName = user.getUserFullName();
        }
System.out.println("=====operation=====> "+operation);
        if (operation != null && operation.equalsIgnoreCase("Open")) {
            String IssueQuery = "SELECT  description   FROM   HD_COMPLAINT_TYPE where complaint_type_code = "+IssueCode+"";
            String IssueAcronym = aprecords.getCodeName(IssueQuery);
            String complaintId = IssueAcronym.substring(0, 3) + "/" + applHelper.getGeneratedId("HD_COMPLAINT");
            String insertReqnTable = "";
            String Subject = requestSubject + " " +complaintId +" "+Status;
            insertReqnTable = " insert into HD_COMPLAINT (complaint_id,complain_category,complain_sub_category,"
                    + " asset_id,priority,create_dateTime,status,user_id,"
                    + " requester_Name,requester_Contact_No,work_Station_IP,notify_Email,"
                    + " Company_Code,request_Subject,request_Description,create_time,nature,Sla_id,complaint_Type,technician,incident_Mode,create_date) "
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
          
            try {

                con = mgDbCon.getConnection("legendPlus");

                pstmt = con.prepareStatement(insertReqnTable);
                pstmt.setString(1, complaintId);
                pstmt.setString(2, category);
                pstmt.setString(3, subcategory);
                pstmt.setString(4, assetId);
                pstmt.setString(5, priority);
                pstmt.setTimestamp(6, mgDbCon.getDateTime(new java.util.Date()));
                pstmt.setString(7, status);
                userId = (userId == null || userId.equals(""))?"0":userId;
                pstmt.setInt(8, Integer.parseInt(userId));
                pstmt.setString(9, requesterName);
                pstmt.setString(10, requesterContactNum);
                pstmt.setString(11, request.getRemoteAddr());
                pstmt.setString(12, notifyEmail);
                pstmt.setString(13, companyCode);
                pstmt.setString(14, requestSubject);
                pstmt.setString(15, requestDescription);
                pstmt.setString(16, timer.format(new java.util.Date()));
                pstmt.setString(17, "guest");
                pstmt.setInt(18, Slaid);
                pstmt.setString(19, "001");
                pstmt.setString(20, technician);
                pstmt.setString(21, incidentMode);
                pstmt.setDate(22, df.dateConvert(new java.util.Date()));
             doneSave = (pstmt.executeUpdate() == -1); //undo these

             String insertMailTable = "";
             insertMailTable = " insert into HD_COMPLAINT_MAIL (complaint_id,complain_category,complain_sub_category,"
                 + " asset_id,priority,create_dateTime,status,complaint_Type,incident_Mode,user_id,response_Mode,"
                 + " requester_Name,requester_Contact_No,work_Station_IP,notify_Email,request_Branch,request_Department,"
                 + " Company_Code,request_Section,request_Subject,request_Description,create_time,technician,requester_id,nature,sla_id,create_date,New_Mail_Issue_Status) "
                 + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
       
        // try {

             cn = mgDbCon.getConnection("legendPlus");

             pstmt = cn.prepareStatement(insertMailTable);
             pstmt.setString(1, complaintId);
             pstmt.setString(2, category);
             pstmt.setString(3, subcategory);
             pstmt.setString(4, assetId);
             pstmt.setString(5, priority);
             pstmt.setTimestamp(6, mgDbCon.getDateTime(new java.util.Date()));
             pstmt.setString(7, status);
             pstmt.setString(8, Comptype);
             pstmt.setString(9, incidentMode);
             userId = (userId == null || userId.equals(""))?"0":userId;
             pstmt.setInt(10, Integer.parseInt(userId));
             pstmt.setString(11, responseMode);
             pstmt.setString(12, requesterName);
             pstmt.setString(13, requesterContactNum);
             pstmt.setString(14, request.getRemoteAddr());
             pstmt.setString(15, notifyEmail); 
             pstmt.setString(16, user.getBranch());
             pstmt.setString(17, user.getdeptCode());
             pstmt.setString(18, companyCode);
             pstmt.setString(19, user.getSection());
             pstmt.setString(20, requestSubject);
             pstmt.setString(21, requestDescription);
             pstmt.setString(22, timer.format(new java.util.Date()));                 
             pstmt.setString(23, technician); 
             pstmt.setString(24, requesterId);
             pstmt.setString(25, "normal");  
             pstmt.setInt(26, Slaid); 
             pstmt.setDate(27, df.dateConvert(new java.util.Date()));
             pstmt.setString(28,"N");
          doneSave = (pstmt.executeUpdate() == -1); //undo these

    //         System.out.println("<<<<<< INCIDENDE doneSave >>>>>> "+doneSave);
//             	String SenderDeptQry = "SELECT  dept_code   FROM   AM_GB_USER where User_id = " + userId;
//             	String SenderDept = aprecords.getCodeName(SenderDeptQry); 
//                String incidentQuery = "SELECT  description   FROM   HD_COMPLAINT_TYPE where complaint_type_code = " + Comptype;
//                String incidentName = aprecords.getCodeName(incidentQuery);
//                String UnitHeadMailQuery = "SELECT  email   FROM   AM_AD_DEPARTMENT where Dept_code = " + category;
//                String UnitHeadMail = aprecords.getCodeName(UnitHeadMailQuery);
//                String SenderQry = "SELECT  email   FROM   AM_GB_USER where User_id = " + userId;
//                String Sender = aprecords.getCodeName(SenderQry); 
                
//                String SenderQry = "SELECT  email   FROM   AM_AD_DEPARTMENT where Dept_code = " + SenderDept;
//                String Sender = aprecords.getCodeName(SenderQry);
//                String StatusQry = "select status_description from hd_status where status_code= " + status;
//                String Status = aprecords.getCodeName(StatusQry);
//                String mailto = UnitHeadMail;
                //notifyEmail = notifyEmail +""+aprecords.userEmail(userId);
 //               String Subject = requestSubject + " " +complaintId +" "+Status;
                             
          System.out.println("=====operation=====> 1");
                if (!doneSave) {
               	 		String url = getServletConfig().getServletContext().getRealPath("");
                  //   	mails.SimpleMailWithAttachment(url, "INCIDENCE", mail.getEmailMessageOriginal(TransactionType, category, subcategory, pageType, technician, userId, requestDescription,complaintId,requestSubject,FileName,FieldName),notifyEmail,userId );
       
                    //insert image if available
                    String sessionId = request.getSession().getId();
                    if (aprecords.getIncidentImage(sessionId) != null) {
                    	aprecords.setProblemImage(sessionId,userId,complaintId,pageName);
                  //      aprecords.setIncidentImage(aprecords.getIncidentImage(sessionId), complaintId);
                    }
                    System.out.println("=====operation=====> 2");
                    // to delete temporary images from image table
                    genList.updateTable(" delete from am_ad_image where sessionId = '" + sessionId+"'");
  
                    //About to send email to recipients
                    System.out.println("loading properties file ...");
                    String realPath = getServletConfig().getServletContext().getRealPath("/mailConfig/legendPlus.properties");
                    FileInputStream fis = new FileInputStream(realPath);
                    System.out.println(" done loading properties file ...");
                    ComplaintManager hdManager = new ComplaintManager();
//                    hdManager.sendRecipientMail(fis, hdManager.getRecipientEmail(reqnID), reqnID);
   //                 String mailnotify = UnitHeadMail;  
                    System.out.println("=====operation=====> 3");
                    String mailto = UnitHeadMail;
                    System.out.println("fecthing mailto "+mailto);
                    String SenderQry = "SELECT  email   FROM   AM_GB_USER where User_id = " + userId;
                    String Sender = aprecords.getCodeName(SenderQry);
                    if(notifyEmail.equalsIgnoreCase("")){
                    	notifyEmail = Sender;
                	 } else {
                		 notifyEmail = notifyEmail + "," +Sender;
                     }                      
 //                  out.print("<script>alert('Request saved successfully.');</script>");  
                	// System.out.println("<<<<<< notifyEmail 0 >>>>>> "+notifyEmail);
                    // mails.SimpleMailWithAttachment(url, "INCIDENCE", mail.getEmailMessage(status, category, pageType, technician, userId, requestDescription,complaintId),notifyEmail,userId );
 //              	System.out.println("sending...");
       //         	mails.SimpleMailWithAttachment(url, Subject, mail.getEmailMessageOriginal(TransactionType, category, subcategory, pageType, technician, userId, requestDescription,complaintId,requestSubject,FileName,FieldName,Subject),notifyEmail,userId,mailto);
// 14-05-2012      	mails.SimpleMailWithAttachment(url, Subject, mail.getEmailMessageOriginal(TransactionType, category, subcategory, pageType, technician, userId, requestDescription,complaintId,requestSubject,FileName,FieldName,Subject,status,"","","",""),notifyEmail,userId,mailto);
                   // out.print("<mailer><error>00-completed</error></mailer>");
                //    out.print("<script>alert('Request saved successfully.');</script>");
                    System.out.println("=====incidentName=====> "+incidentName);  
                   // out.print("<script>alert('" + incidentName + " saved successfully.');</script>");
                    
                    out.print("<script>window.location='DocumentHelp.jsp?np=hdNewRequisitionFormGuest'</script>");
                    System.out.println("=====operation=====> 4");

                } else {  
                	//out.print("<mailer><error>50-Request not sent</error></mailer>");
                    out.print("<script>alert('" + incidentName + "  not save.');</script>");                	
                    //out.print("<script>alert('Request not save.');</script>");
                    out.print("<script>window.location='DocumentHelp.jsp?np=hdNewRequisitionFormGuest'</script>");

                }
            } catch (Exception e) {
            	//out.print("<mailer><error>99-failed</error></mailer>");
            	System.out.println("Error occurred when saving Complaint requisition : " + e);
            } finally {
                try {
                    if (con != null) {
                        con.close();
                    }
                    if (cn != null) {
                        cn.close();
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