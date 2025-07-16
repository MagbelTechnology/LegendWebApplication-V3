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

public class HelpDeskNewRequisitionServlet16_04_2012 extends HttpServlet {
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

    public HelpDeskNewRequisitionServlet16_04_2012() {
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
        com.magbel.util.DatetimeFormat df;
        timer = new SimpleDateFormat("kk:mm:ss");
        genList = new GenerateList();
        boolean done = false;
        mails= new com.magbel.admin.mail.EmailSmsServiceBus();
        df = new com.magbel.util.DatetimeFormat();
        mail= new BulkMail();


        boolean doneSave = false;
 
        String category = request.getParameter("category");
        String subcategory = request.getParameter("subcategory");
        String userId = request.getParameter("userid");
        String priority = request.getParameter("priority");
        String Comptype = request.getParameter("Comptype");        
        String issueMode = request.getParameter("incidentMode");   
        String requesterName = request.getParameter("requesterName");
        String requesterContactNum = request.getParameter("requesterContNumber");
        String requestDepartment = request.getParameter("requestDepartment");
        String notifymail = request.getParameter("notifymail");
        String requestSubject = request.getParameter("requestSubject");
        String requestDescription = request.getParameter("requestDescription"); 
        String operation = request.getParameter("operation");
        String companyCode = request.getParameter("comp_code");
        String requesterId = request.getParameter("requesterId");
        String pageType = request.getParameter("pageType");
        String pageName = request.getParameter("pageName"); 
        String FileName = "HD_COMPLAINT";
        String FieldName = "complaint_id";
        String returncode = request.getParameter("returncode");
        String technician = request.getParameter("technician");
   //     System.out.println("----notifymail---> "+notifymail);
    //    System.out.println("----req_subject1---> "+requestSubject);  
   //     System.out.println("----requesterId---> "+requesterId);
        String status = "001";//String status = request.getParameter("status");
     //   System.out.println("===technician===="+technician);
  	 // if(technician == null || technician.equalsIgnoreCase("")){
        String TechQuery = "SELECT User_id, User_Name   FROM   AM_GB_USER WHERE Full_Name = '"+technician+"'";
        technician = aprecords.getCodeName(TechQuery);
    //    }      

        String SlaQuery = "SELECT  criteria_ID   FROM   SLA_RESPONSE where Dept_Code = '"+requestDepartment+"' AND Cat_Code = '"+category+"' ";
        String SlaIdent = aprecords.getCodeName(SlaQuery);
        SlaIdent = (SlaIdent == null || SlaIdent.equals(""))?"0":SlaIdent;
        int Slaid = Integer.parseInt(SlaIdent);
        String currentUserName = "";
        status = "001";
        String TransactionType = request.getParameter("TransactionType");
        String responseMode = "001";
        String assetId = "0";
 //      System.out.println("===returncode HelpDeskNewRequisitionServlet=="+returncode);
//        System.out.println("===Slaid=="+Slaid);
        HttpSession session = request.getSession();
        com.magbel.admin.objects.User user = null;
        if (session.getAttribute("_user") != null) {
            user = (com.magbel.admin.objects.User) session.getAttribute("_user");
 
            currentUserName = user.getUserFullName();
        }

        if (operation != null && operation.equalsIgnoreCase("Save")) {
            String IssueQuery = "SELECT  description   FROM   HD_COMPLAINT_TYPE where complaint_type_code = "+Comptype+"";
            String IssueAcronym = aprecords.getCodeName(IssueQuery);
            String complaintId = IssueAcronym.substring(0, 3) + "/" + applHelper.getGeneratedId("HD_COMPLAINT");
            String insertReqnTable = "";

            insertReqnTable = " insert into HD_COMPLAINT (complaint_id,complain_category,complain_sub_category,"
                    + " asset_id,priority,create_dateTime,status,complaint_Type,incident_Mode,user_id,response_Mode,"
                    + " requester_Name,requester_Contact_No,work_Station_IP,notify_Email,request_Branch,request_Department,"
                    + " Company_Code,request_Section,request_Subject,request_Description,create_time,technician,requester_id,nature,sla_id,create_date) "
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
          
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
                pstmt.setString(8, Comptype);
                pstmt.setString(9, issueMode);
                userId = (userId == null || userId.equals(""))?"0":userId;
                pstmt.setInt(10, Integer.parseInt(userId));
                pstmt.setString(11, responseMode);
                pstmt.setString(12, requesterName);
                pstmt.setString(13, requesterContactNum);
                pstmt.setString(14, request.getRemoteAddr());
                pstmt.setString(15, notifymail); 
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
             doneSave = (pstmt.executeUpdate() == -1); //undo these
    //         System.out.println("<<<<<< INCIDENDE doneSave >>>>>> "+doneSave);
             	String SenderDeptQry = "SELECT  dept_code   FROM   AM_GB_USER where User_id = " + userId;
             	String SenderDept = aprecords.getCodeName(SenderDeptQry); 
                String incidentQuery = "SELECT  description   FROM   HD_COMPLAINT_TYPE where complaint_type_code = " + Comptype;
                String incidentName = aprecords.getCodeName(incidentQuery);
                String UnitHeadMailQuery = "SELECT  email   FROM   AM_AD_DEPARTMENT where Dept_code = " + category;
                String UnitHeadMail = aprecords.getCodeName(UnitHeadMailQuery);
//                String SenderQry = "SELECT  email   FROM   AM_GB_USER where User_id = " + userId;
//                String Sender = aprecords.getCodeName(SenderQry); 
                
                String SenderQry = "SELECT  email   FROM   AM_AD_DEPARTMENT where Dept_code = " + SenderDept;
                String Sender = aprecords.getCodeName(SenderQry);
                String StatusQry = "select status_description from hd_status where status_code= " + status;
                String Status = aprecords.getCodeName(StatusQry);
                String mailto = UnitHeadMail;
                //notifyEmail = notifyEmail +""+aprecords.userEmail(userId);
                String Subject = requestSubject + " " +complaintId +" "+Status;
                
                if (!doneSave) { 
                	 String url = getServletConfig().getServletContext().getRealPath("");
              //  	 System.out.println("<<<<<< NOT doneSave url >>>>>> "+url);
                	// String mailnotify = aprecords.getmailaddresses();  
                	// String mailnotify = Sender; 
                	 if(notifymail.equalsIgnoreCase("")){
                	 notifymail = Sender;
                	 } else { 
                    	 notifymail = notifymail + "," +Sender;
                     }       
                    // mails.SimpleMailWithAttachment(url, "INCIDENCE", mail.getEmailMessage(status, category, pageType, technician, userId, requestDescription,complaintId),notifyEmail,userId );
              		 System.out.println("TransactionType = "+TransactionType+" category = "+category+" SubCategory = "+category+" pageType = "+pageType+" technician = "+technician+" userId = "+userId+" requestDescription = "+requestDescription);
               		System.out.println("complaintId = "+complaintId+" Subject = "+requestSubject+" FileName = "+FileName+" FieldName = "+FieldName+" notifyEmail = "+notifymail+" userId = "+userId);
               		                 	  
             //   	mails.SimpleMailWithAttachment(url, requestSubject, mail.getEmailMessageOriginal(TransactionType, category, subcategory, pageType, technician, userId, requestDescription,complaintId,requestSubject,FileName,FieldName),Sender,userId);
                	mails.SimpleMailWithAttachment(url, Subject, mail.getEmailMessageOriginal(TransactionType, category, subcategory, pageType, technician, userId, requestDescription,complaintId,requestSubject,FileName,FieldName,Subject,status,"","","",""),notifymail,userId,mailto);
                    //insert image if available  
                    String sessionId = request.getSession().getId();
                    if (aprecords.getIncidentImage(sessionId) != null) {
                    	aprecords.setProblemImage(sessionId,userId,complaintId,pageName);
                      //  aprecords.setIncidentImage(aprecords.getIncidentImage(sessionId), complaintId);
                    }
 
                    // to delete temporary images from image table
                   // genList.updateTable(" delete from am_ad_image where sessionId = '" + sessionId+"'");



                    //About to send email to recipients
                    String realPath = getServletConfig().getServletContext().getRealPath("/mailConfig/legend.properties");
                    FileInputStream fis = new FileInputStream(realPath);
                    ComplaintManager hdManager = new ComplaintManager();
//                    hdManager.sendRecipientMail(fis, hdManager.getRecipientEmail(reqnID), reqnID);
/*
                    if (notifyEmail.contains(";")) {
                        String[] mailReciepents = notifyEmail.split(";");
                        hdManager.sendRecipientsMail(fis, mailReciepents, complaintId, requestSubject, requestDescription, currentUserName);
                    } else {
                        String[] mailReciepents = new String[]{notifyEmail};
                        hdManager.sendRecipientsMail(fis, mailReciepents, complaintId, requestSubject, requestDescription, currentUserName);
                    }
*/ 
                   
                    out.print("<script>alert('" + incidentName + " saved successfully.');</script>");
                    out.print("<script>window.location='DocumentHelp.jsp?np=hdNewRequisitionForm&reportId="+returncode+"&reqnId='</script>");

                } else {
                    out.print("<script>alert('" + incidentName + "  not save.');</script>");
                    out.print("<script>window.location='DocumentHelp.jsp?np=hdNewRequisitionForm&reportId="+returncode+"&reqnId='</script>");

                }
            } catch (Exception e) {
                System.out.println("Error occurred when saving Complaint requisition : " + e);
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