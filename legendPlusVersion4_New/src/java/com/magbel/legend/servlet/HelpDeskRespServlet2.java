/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magbel.legend.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import magma.net.dao.MagmaDBConnection;
import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.GenerateList;
import com.magbel.legend.bus.ComplaintManager;
import com.magbel.legend.mail.MailSender;
import com.magbel.legend.mail.BulkMail;
import legend.admin.objects.ComplaintRequisition;
import com.magbel.util.ApplicationHelper;
import jakarta.servlet.http.HttpSession;

public class HelpDeskRespServlet2 extends HttpServlet {

    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    MagmaDBConnection mgDbCon = null;
    ApplicationHelper applHelper = null;
    MailSender mailSender = null;
    ApprovalRecords aprecords = null;
    SimpleDateFormat timer = null;
    GenerateList genList = null;
    com.magbel.legend.mail.EmailSmsServiceBus mails = null;
    BulkMail mail=null;
	private String UpdateTime;

    public HelpDeskRespServlet2() {
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
        com.magbel.util.DatetimeFormat df;
        aprecords = new ApprovalRecords();
        timer = new SimpleDateFormat("kk:mm:ss");
        genList = new GenerateList();
        mails= new com.magbel.legend.mail.EmailSmsServiceBus();
        df = new com.magbel.util.DatetimeFormat();
        mail= new BulkMail();

        
        boolean done = false;
        ComplaintRequisition complaintRequisition = new ComplaintRequisition();

        boolean doneSave = false;
       //for inserrt to hd response
        String category = request.getParameter("category");
        String subcategory = request.getParameter("subcategory");
        String userId = request.getParameter("userid");
        String priority = request.getParameter("priority");
        userId = (userId == null || userId.equals(""))?"0":userId;
        String status = request.getParameter("status");
       // String requesterName = request.getParameter("requesterName");
       // String requesterContactNum = request.getParameter("requesterContNumber");
        String notifyEmail = request.getParameter("notifymail");
       // String receipientId = request.getParameter("requesterId");
        String complaintType = request.getParameter("Comptype");
        String incidentMode = request.getParameter("incidentMode");
        String requestDescription = request.getParameter("requestDesc");        
        String requestDescriptionNew = request.getParameter("requestDescriptionNew");
        String operation = request.getParameter("operation");
        String returncode = request.getParameter("returncode");
        String companyCode = request.getParameter("comp_code");
        String complaintId = request.getParameter("reqnId");
        String requestSubject = request.getParameter("requestSubject");
         //for update of hd complaint
        //subject,description,technician,asset,
        if (category == null || category.equals("")){
        	category = request.getParameter("CatCode");
        }                
        if (subcategory == null || subcategory.equals("")){
        	subcategory = request.getParameter("SubCatCode");
        }          
        String technician = request.getParameter("technicianId");
        technician = (technician == null || technician.equals(""))?"0":technician;
        //int tech = Integer.parseInt(technician);       
        String assetId = request.getParameter("asset"); 
        String pageType = request.getParameter("pageType");
        String pageName = request.getParameter("pageName");
        String FileName = "HD_COMPLAINT";
        String FieldName = "complaint_id";
        String TransactionType = request.getParameter("TransactionType");
        String SlaQuery = "SELECT  criteria_ID   FROM   SLA_RESPONSE where Dept_Code = '"+category+"' AND Cat_Code = '"+subcategory+"' ";
        String SlaIdent = aprecords.getCodeName(SlaQuery);   
      //  System.out.println("=======SlaQuery>>>>> "+SlaQuery);
        SlaIdent = (SlaIdent == null || SlaIdent.equals(""))?"0":SlaIdent;
        int Slaid = Integer.parseInt(SlaIdent);

        complaintRequisition.setCategory(category);
        complaintRequisition.setSubCategory(subcategory);
        complaintRequisition.setPriority(priority);
        complaintRequisition.setComplainType(complaintType);
        complaintRequisition.setIncidentMode(incidentMode);
        complaintRequisition.setTechnician(technician);
        complaintRequisition.setAssetId(assetId);
        complaintRequisition.setRequestSubject(requestSubject);
        complaintRequisition.setRequestDescription(requestDescription);
        complaintRequisition.setRequestDescriptionNew(requestDescriptionNew);
        complaintRequisition.setSlaid(Slaid);
          
        complaintRequisition.setReqnID(complaintId);
        String SenderDeptQry = "SELECT  dept_code   FROM   AM_GB_USER where User_id = " + userId;
        String SenderDept = aprecords.getCodeName(SenderDeptQry);         
        String UnitHeadMailQuery = "SELECT  email   FROM   AM_AD_DEPARTMENT where Dept_code = '" + category +"' ";
        String UnitHeadMail = aprecords.getCodeName(UnitHeadMailQuery);
//        String SenderQry = "SELECT  email   FROM   AM_GB_USER where User_id = " + userId;
//        String Sender = aprecords.getCodeName(SenderQry); 
        String statuscode = status;
        String IdQry = "SELECT  notify_email   FROM   HD_COMPLAINT where complaint_Id = '"+complaintId+"'";
        String OldNotification = aprecords.getCodeName(IdQry); 
        String OldStatusQry = "SELECT  status   FROM   HD_COMPLAINT where complaint_Id = '"+complaintId+"'";
        String OldStatus = aprecords.getCodeName(OldStatusQry); 
        String SenderQry = "SELECT  email   FROM   AM_AD_DEPARTMENT where Dept_code = '"+SenderDept+"'";
        String Sender = aprecords.getCodeName(SenderQry);
        String StatusQry = "select status_description from hd_status where status_code= '"+status+"'";
        String Status = aprecords.getCodeName(StatusQry);
//      	System.out.println("=====status====>>> "+status+"=====statuscode====>>>>> "+statuscode);
       	Timestamp UpdateDate =  mgDbCon.getDateTime(new java.util.Date());
       	UpdateTime = timer.format(new java.util.Date());
//       	System.out.println("=====UpdateDate====>>> "+UpdateDate+"=====UpdateTime====>>>>> "+UpdateTime);
        String mailto = UnitHeadMail; 
        String Change = "";
        //notifyEmail = notifyEmail +""+aprecords.userEmail(userId);
       // String Subject = requestSubject + " " +complaintId;
        String SubjectQry = "select request_Subject, request_Description from HD_COMPLAINT where complaint_id = '"+complaintId+"'";
        String requestSubjectold = aprecords.getCodeName(SubjectQry);
        String DescriptQry = "select request_Description, request_Subject from HD_COMPLAINT where complaint_id = '"+complaintId+"'";
        String requestDescriptionold = aprecords.getCodeName(DescriptQry);
//        System.out.println("=====requestSubjectold ===== "+requestSubjectold);
//        System.out.println("=====requestDescriptionold ===== "+requestDescriptionold);
//        System.out.println("=====statuscode ===== "+statuscode);
//        System.out.println("=====OldStatus ===== "+OldStatus);
        if(statuscode.equalsIgnoreCase(OldStatus)){
        	Change = "EDITED";
        	Status = Change;
        }
 //       System.out.println("=====Change 01 ===== "+Change);
        String Subject = requestSubject + " " +complaintId +" "+Status;
   	 if(notifyEmail.equalsIgnoreCase("")){
   		notifyEmail = Sender+ "," +OldNotification;
    	 } else { 
    		 notifyEmail = notifyEmail + "," +Sender + "," +OldNotification;
         }  
        String currentUserName = "";
        HttpSession session = request.getSession();
        legend.admin.objects.User user = null;
        if (session.getAttribute("_user") != null) {
            user = (legend.admin.objects.User) session.getAttribute("_user");

            currentUserName = user.getUserFullName();

    
        }
//        System.out.println("=====operation ===== "+operation);
        operation = "Save";
        if (operation != null && operation.equalsIgnoreCase("Save")) {
            //String complaintId = "Incd/" + applHelper.getGeneratedId("HD_COMPLAINT");
            String insertReqnTable = "";

//            System.out.println("About To Save ");

            insertReqnTable = " insert into HD_RESPONSE (complaint_id, create_date,status,user_id,"
                    + " workStationIP,notify_Email,"
                    + " response,create_time,recipient_Id,complain_category,complain_sub_category"
                    + ",priority,complaint_Type,incident_Mode,asset_id) "
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//            System.out.println("=====insertReqnTable ===== "+insertReqnTable);
            try {   
                con = mgDbCon.getConnection("legendPlus");

                pstmt = con.prepareStatement(insertReqnTable);    
                pstmt.setString(1, complaintId);
                pstmt.setTimestamp(2, mgDbCon.getDateTime(new java.util.Date()));
                pstmt.setString(3, status);
                pstmt.setInt(4, Integer.parseInt(userId));
                pstmt.setString(5, request.getRemoteAddr());
                pstmt.setString(6, notifyEmail);    
                pstmt.setString(7, requestDescriptionNew);
                pstmt.setString(8, timer.format(new java.util.Date())); 
                pstmt.setString(9, technician);                   	
                pstmt.setString(10, category);
                pstmt.setString(11, subcategory);  
                pstmt.setString(12, priority);
                pstmt.setString(13, complaintType);
                pstmt.setString(14, incidentMode);  
                pstmt.setString(15, assetId);   
                                           
             doneSave = (pstmt.executeUpdate() == -1); //undo these
               // String incidentQuery = "SELECT  description   FROM   HD_COMPLAINT_TYPE where complaint_type_code = " + Integer.parseInt(complaintType);
               // String incidentName = aprecords.getCodeName(incidentQuery); 
//             System.out.println("=====Done ===== ");             
                if (!doneSave) {    
//                	  System.out.println("=====Subject 01 ===== "+Subject);
                    ComplaintManager hdManager = new ComplaintManager();
                    hdManager.updateCompRequisition(complaintRequisition,statuscode,priority,complaintType,UpdateDate);
                	 String url = getServletConfig().getServletContext().getRealPath("");
                     //mails.SimpleMailWithAttachment(url, "TESTING", mail.getEmailMessage(status, category, pageType, technician, userId, requestDescription,complaintId),notifyEmail ,userId);              	 
                //   	mails.SimpleMailWithAttachment(url, "INCIDENT", mail.getEmailMessageOriginal(status, category, subcategory, pageType, technician, userId, requestDescription,complaintId,requestSubject,FileName,FieldName),notifyEmail,userId );
                   	mails.SimpleMailWithAttachment(url, Subject, mail.getEmailMessageOriginal(TransactionType, category, subcategory, pageType, technician, userId, requestDescription,complaintId,requestSubject,FileName,FieldName,Subject,status,requestDescriptionNew,Change,requestSubjectold,requestDescriptionold),notifyEmail,userId,mailto);                   	
  //        mail.sendMail(  "Subject",mail.getEmailMessage(status, category, pageType, technician, userId, requestDescription,complaintId),  url,"", "");
/*     25/01/2012               ComplaintManager hdManager = new ComplaintManager();
                     hdManager.updateCompRequisition(complaintRequisition,statuscode,priority,complaintType,UpdateDate);  */

                    //insert image if available 
                    String sessionId = request.getSession().getId();
                    if (aprecords.getIncidentImage(sessionId) != null) {
                    	aprecords.setProblemImage(sessionId,userId,complaintId,pageName);
                      //  aprecords.setIncidentImage(aprecords.getIncidentImage(sessionId), complaintId);
                    }
 

                    // to delete temporary images from image table
                   // genList.updateTable(" delete from am_ad_image where sessionId = '" + sessionId+"'");
                 
                     

    
                    //About to send email to recipients
                    String realPath = getServletConfig().getServletContext().getRealPath("/Property/legend.properties");
                    FileInputStream fis = new FileInputStream(realPath);
                   
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

                    out.print("<script>alert('Response saved successfully.');</script>");
                //    out.print("<script>window.location='DocumentHelp.jsp?np=hdComplaintDashBoard'</script>");
                    out.print("<script>window.location='DocumentHelp.jsp?np=hdComplaintDashBoard&query="+returncode+"'</script>");

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