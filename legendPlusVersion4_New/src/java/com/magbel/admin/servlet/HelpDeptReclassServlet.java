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
import com.magbel.admin.objects.ComplaintRequisition;
import com.magbel.util.ApplicationHelper;
import javax.servlet.http.HttpSession;

public class HelpDeptReclassServlet extends HttpServlet {

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

    public HelpDeptReclassServlet() {
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
        mails= new com.magbel.admin.mail.EmailSmsServiceBus();
        
        mail= new BulkMail();

        
        boolean done = false;
        ComplaintRequisition complaintRequisition = new ComplaintRequisition();

        boolean doneSave = false;
       //for inserrt to hd response
        String operation = request.getParameter("operation");
        String complaintId = request.getParameter("reqnId");
        String category = request.getParameter("category");
        String subcategory = request.getParameter("subcategory");
        String pageType = request.getParameter("pageType");
        String FileName = "HD_COMPLAINT";
		String status = request.getParameter("status");
        String technician = request.getParameter("technicianId");
        technician = (technician == null || technician.equals(""))?"0":technician;
        String requestDescription = request.getParameter("requestDescriptionNew");   
        String userId = request.getParameter("userid");
        String requestSubject = request.getParameter("requestSubject");
        String notifyEmail = request.getParameter("notifyEmail");
        String FieldName = "complaint_id";
   //     System.out.println("==status== "+status+" ===technician "+technician+" ===requestDescription "+requestDescription);
   //     System.out.println("==userId== "+userId+" ===requestSubject "+requestSubject+" ===notifyEmail "+notifyEmail);
        complaintRequisition.setCategory(category);
        complaintRequisition.setSubCategory(subcategory); 
  //      System.out.println("======complaintId=====> "+complaintId);
        complaintRequisition.setReqnID(complaintId);
        String SenderDeptQry = "SELECT  dept_code   FROM   AM_GB_USER where User_id = " + userId;
        String SenderDept = aprecords.getCodeName(SenderDeptQry); 
        String UnitHeadMailQuery = "SELECT  email   FROM   AM_AD_DEPARTMENT where Dept_code = " + category;
        String UnitHeadMail = aprecords.getCodeName(UnitHeadMailQuery);
        String FromCategoryQuery = "SELECT  complain_category   FROM   HD_COMPLAINT WHERE complaint_id =  + '"+complaintId+"'";
        String FromCategory = aprecords.getCodeName(FromCategoryQuery);
   //     System.out.println("======FromCategory=====> "+FromCategory);
        String FromUnitHeadMailQuery = "SELECT  email   FROM   AM_AD_DEPARTMENT where Dept_code = " + FromCategory;
        String FromUnitHeadMail = aprecords.getCodeName(FromUnitHeadMailQuery);
   //     System.out.println("======FromUnitHeadMail=====> "+FromUnitHeadMail);
    //    String SenderQry = "SELECT  email   FROM   AM_GB_USER where User_id = " + userId;
    //    String Sender = aprecords.getCodeName(SenderQry); 
        String SenderQry = "SELECT  email   FROM   AM_AD_DEPARTMENT where Dept_code = " + SenderDept;
        String Sender = aprecords.getCodeName(SenderQry);
        String TransactionType = "006";
        String mailto = UnitHeadMail; 
        String Subject = "Re-Assigning of Issue" + " " +complaintId;
    	String Change = "RE-ASSIGNED";
        String CatQry = "select complain_category from HD_COMPLAINT where complaint_id = '"+complaintId+"'";
        String oldCategory = aprecords.getCodeName(CatQry);
        String SubCatQry = "select complain_sub_category from HD_COMPLAINT where complaint_id = '"+complaintId+"'";
        String OldSubCategory = aprecords.getCodeName(SubCatQry);
   //     System.out.println("======oldCategory=====> "+oldCategory+" ===OldSubCategory=== "+OldSubCategory);
        String SubjectQry = "select Dept_name from AM_AD_DEPARTMENT where Dept_code = '"+oldCategory+"'";
        String Categoryold = aprecords.getCodeName(SubjectQry);
        String DescriptQry = "select sub_category_name from HD_COMPLAIN_SUBCATEGORY where sub_category_code = '"+OldSubCategory+"'";
        String SubCategoryold = aprecords.getCodeName(DescriptQry); 
        String NewSubjectQry = "select Dept_name from AM_AD_DEPARTMENT where Dept_code = '"+category+"'";
        String CategoryNew = aprecords.getCodeName(NewSubjectQry);
        String NewDescriptQry = "select sub_category_name from HD_COMPLAIN_SUBCATEGORY where sub_category_code = '"+subcategory+"'";
        String SubCategoryNew = aprecords.getCodeName(NewDescriptQry); 
   //     System.out.println("======oldCategory=====> "+oldCategory+" ===OldSubCategory=== "+OldSubCategory);
        notifyEmail = (notifyEmail == null || notifyEmail.equals(""))?"":notifyEmail;
  //      System.out.println("======notifyEmail=====> "+notifyEmail);
   	 if(notifyEmail.equalsIgnoreCase("") || notifyEmail==null){  
   		notifyEmail = Sender + "," +FromUnitHeadMail;
    	 } else { 
    		 notifyEmail = notifyEmail + "," +Sender + "," +FromUnitHeadMail;
         }     // System.out.println("======notifyEmail=====> "+notifyEmail);
        String currentUserName = "";
        HttpSession session = request.getSession();
        com.magbel.admin.objects.User user = null;
        if (session.getAttribute("_user") != null) {
            user = (com.magbel.admin.objects.User) session.getAttribute("_user");

            currentUserName = user.getUserFullName();


        }
        System.out.println("======operation=====> "+operation);
        String insertReqnTable = "";
        if (operation != null && operation.equalsIgnoreCase("Re-Assign")) {

            insertReqnTable = " insert into HD_RESPONSE (complaint_id, create_date,status,user_id,"
                    + " workStationIP,notify_Email,"
                    + " response,create_time,recipient_Id,complain_category,complain_sub_category)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?)";                        
            try {
            	  
                con = mgDbCon.getConnection("helpDesk");

                pstmt = con.prepareStatement(insertReqnTable);    
                pstmt.setString(1, complaintId);
                pstmt.setTimestamp(2, mgDbCon.getDateTime(new java.util.Date()));
                pstmt.setString(3, status);
                pstmt.setInt(4, Integer.parseInt(userId));
                pstmt.setString(5, request.getRemoteAddr());
                pstmt.setString(6, notifyEmail);    
                pstmt.setString(7, "Re-Assigned");
                pstmt.setString(8, timer.format(new java.util.Date())); 
                pstmt.setString(9, technician);                   	
                pstmt.setString(10, category);
                pstmt.setString(11, subcategory);  
          //      pstmt.setString(12, priority);
        //        pstmt.setString(13, complaintType);
         //       pstmt.setString(14, incidentMode);  
           //     pstmt.setString(15, assetId);  
                                           
             doneSave = (pstmt.executeUpdate() == -1); //undo these    
                    	
                ComplaintManager hdManager = new ComplaintManager();
                hdManager.updateDeptCompRequisition(complaintRequisition);
                if (!doneSave) { 
                	 String url = getServletConfig().getServletContext().getRealPath("");
                     //mails.SimpleMailWithAttachment(url, "TESTING", mail.getEmailMessage(status, category, pageType, technician, userId, requestDescription,complaintId),notifyEmail ,userId);
                  // 	mails.SimpleMailWithAttachment(url, "Department Reallocation", mail.getEmailMessageOriginal(status, category, subcategory, pageType, technician, userId, requestDescription,complaintId,requestSubject,FileName,FieldName),notifyEmail,userId );
                //  	mails.SimpleMailWithAttachment(url, Subject, mail.getEmailMessageOriginal(TransactionType, category, subcategory, pageType, technician, userId, requestDescription,complaintId,requestSubject,FileName,FieldName,Subject),notifyEmail,userId,mailto);
                	 status = "006";
                	mails.SimpleMailWithAttachment(url, Subject, mail.getEmailMessageOriginal(TransactionType, category, subcategory, pageType, technician, userId, SubCategoryNew,complaintId,CategoryNew,FileName,FieldName,Subject,status,requestDescription,Change,Categoryold,SubCategoryold),notifyEmail,userId,mailto);
                    String realPath = getServletConfig().getServletContext().getRealPath("/mailConfig/legend.properties");
                    FileInputStream fis = new FileInputStream(realPath);
                   
                    out.print("<script>alert('Response saved successfully.');</script>");
                    out.print("<script>window.location='DocumentHelp.jsp?np=hdDepartmentReallocate'</script>");

   
                } else {
                    out.print("<script>alert('Response  not save.');</script>");
                    out.print("<script>window.location='DocumentHelp.jsp?np=hdDepartmentReallocate'</script>");

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