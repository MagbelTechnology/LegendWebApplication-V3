/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magbel.admin.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import magma.net.dao.MagmaDBConnection;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.GenerateList;
import com.magbel.legend.bus.ComplaintManager;
import com.magbel.legend.mail.MailSender;
import com.magbel.admin.mail.BulkMail;
import com.magbel.util.ApplicationHelper;

import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class HelpDeskNewRequisitionServlet extends HttpServlet {
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
    com.magbel.legend.mail.EmailSmsServiceBus mails = null;
    BulkMail mail=null;

    public HelpDeskNewRequisitionServlet() {
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
//        PrintWriter out = response.getWriter();
        mailSender = new MailSender();
        aprecords = new ApprovalRecords();
        com.magbel.util.DatetimeFormat df;
        timer = new SimpleDateFormat("kk:mm:ss");
        genList = new GenerateList();
        boolean done = false;
        mails= new com.magbel.legend.mail.EmailSmsServiceBus();
        df = new com.magbel.util.DatetimeFormat();
        mail= new BulkMail();


        boolean doneSave = false;
 

		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		
        if(!ServletFileUpload.isMultipartContent(request))
        {
            response.getWriter().println("Does not support!");
            return;
        }
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(0x300000);
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setFileSizeMax(0xa00000L);
        upload.setSizeMax(0x3200000L);
        Properties prop = new Properties();
        File file = new File("C:\\Property\\LegendPlus.properties");
        FileInputStream input = new FileInputStream(file);
        prop.load(input);
        String UPLOAD_FOLDER = prop.getProperty("imagesUrl");
//        System.out.println((new StringBuilder("The url is : ")).append(UPLOAD_FOLDER).toString());
        String uploadPath = UPLOAD_FOLDER;
        
        String previousPage = request.getParameter("previousPage");
//        String assetId = request.getParameter("assetId");
     
        
        File uploadDir = new File(uploadPath);
        if(!uploadDir.exists())
        {
            uploadDir.mkdir();
        }  

        String category = "";
        String subcategory = "";
        String userId = "";
        String priority = "";
        String Comptype = "";
        String issueMode = "";
        String requesterName = "";
        String requesterContactNum = "";
        String requestDepartment = "";
        String notifymail = "";
        String requestSubject = "";
        String requestDescription = "";
        String operation = "";
        String companyCode = "";
        String requesterId = "";
        String pageType = "";
        String pageName = "";
        String FileName = "HD_COMPLAINT";
        String FieldName = "complaint_id";
        String returncode = "";
        String technician = "";
        

        List formItems = null;
				try {
					formItems = upload.parseRequest(request);
				} catch (FileUploadException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
              for(Iterator iter = formItems.iterator(); iter.hasNext();)
              {
                  FileItem item = (FileItem)iter.next();
                  
                 if(item.isFormField())
                  {
                
                  		 String fieldName = item.getFieldName();
//  						 System.out.println("Field Name : " + fieldName);
                           if(fieldName.equals("category")) {category=item.getString();}     
                           if(fieldName.equals("userid")) {userId=item.getString(); }                   
                           if(fieldName.equals("subcategory")) {subcategory=item.getString();}
                           if(fieldName.equals("priority")) {priority =item.getString();}                     
                           if(fieldName.equals("Comptype")) {Comptype =item.getString();} 
                           if(fieldName.equals("incidentMode")) {issueMode =item.getString();} 
                           if(fieldName.equals("requesterName")) {requesterName =item.getString();} 
                           if(fieldName.equals("requesterContNumber")) {requesterContactNum =item.getString();} 
                           if(fieldName.equals("notifymail")) {notifymail =item.getString();} 
                           if(fieldName.equals("requestSubject")) {requestSubject =item.getString();} 
                           if(fieldName.equals("requestDescription")) {requestDescription =item.getString();} 
                           if(fieldName.equals("operation")) {operation =item.getString();} 
                           if(fieldName.equals("comp_code")) {companyCode =item.getString();} 
                           if(fieldName.equals("requesterId")) {requesterId =item.getString();} 
                           if(fieldName.equals("pageType")) {pageType =item.getString();} 
                           if(fieldName.equals("pageName")) {pageName =item.getString();} 
                           if(fieldName.equals("returncode")) {returncode =item.getString();} 
                           if(fieldName.equals("technician")) {technician =item.getString();} 
                  }
              }

        String status = "001";//String status = request.getParameter("status");
        String TechQuery = "SELECT User_id, User_Name   FROM   AM_GB_USER WHERE Full_Name = '"+technician+"'";
        technician = aprecords.getCodeName(TechQuery);  
        String subcategoryQry = "SELECT sub_category_code   FROM   HD_COMPLAIN_SUBCATEGORY WHERE sub_category_desc = '"+subcategory+"'";
        subcategory = aprecords.getCodeName(subcategoryQry);
        
        String SlaQuery = "SELECT  criteria_ID   FROM   SLA_RESPONSE where Dept_Code = '"+requestDepartment+"' AND Cat_Code = '"+category+"' ";
        String SlaIdent = aprecords.getCodeName(SlaQuery);
        SlaIdent = (SlaIdent == null || SlaIdent.equals(""))?"0":SlaIdent;
        int Slaid = Integer.parseInt(SlaIdent);
        String currentUserName = "";
        status = "001";
        String TransactionType = request.getParameter("TransactionType");
        String responseMode = "001";
        String assetId = "0";
        legend.admin.objects.User user = null;
        if (session.getAttribute("_user") != null) {
            user = (legend.admin.objects.User) session.getAttribute("_user");
 
            currentUserName = user.getUserFullName();
        }
        operation = "Save";
        if (operation != null && operation.equalsIgnoreCase("Save")) {
            String IssueQuery = "SELECT  description   FROM   HD_COMPLAINT_TYPE where complaint_type_code = "+Comptype+"";
//            System.out.println("===IssueQuery== "+IssueQuery);
            String IssueAcronym = aprecords.getCodeName(IssueQuery);
//            System.out.println("===IssueAcronym== "+IssueAcronym);
            String complaintId = IssueAcronym.substring(0, 3) + "-" + applHelper.getGeneratedId("HD_COMPLAINT");
            String insertReqnTable = "";
//            System.out.println("===complaintId== "+complaintId);
            
            insertReqnTable = " insert into HD_COMPLAINT (complaint_id,complain_category,complain_sub_category,"
                    + " asset_id,priority,create_dateTime,status,complaint_Type,incident_Mode,user_id,response_Mode,"
                    + " requester_Name,requester_Contact_No,work_Station_IP,notify_Email,request_Branch,request_Department,"
                    + " Company_Code,request_Subject,request_Description,create_time,technician,requester_id,nature,sla_id,create_date) "
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
          
            try {
//            	System.out.println("===insertReqnTable== "+insertReqnTable);
                con = mgDbCon.getConnection("legendPlus");
                pstmt = con.prepareStatement(insertReqnTable);
                pstmt.setString(1, complaintId);
//                System.out.println("===complaintId---: "+complaintId);
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
                pstmt.setString(17, user.getDeptCode());
                pstmt.setString(18, companyCode);
//                pstmt.setString(19, user.getSection());
                pstmt.setString(19, requestSubject);
                pstmt.setString(20, requestDescription);
                pstmt.setString(21, timer.format(new java.util.Date()));                 
                pstmt.setString(22, technician);
                pstmt.setString(23, requesterId);
                pstmt.setString(24, "normal"); 
                pstmt.setInt(25, Slaid); 
                pstmt.setDate(26, df.dateConvert(new java.util.Date()));
             doneSave = (pstmt.executeUpdate() == -1); //undo these

             String insertMailTable = "";
             insertMailTable = " insert into HD_COMPLAINT_MAIL (complaint_id,complain_category,complain_sub_category,"
                 + " asset_id,priority,create_dateTime,status,complaint_Type,incident_Mode,user_id,response_Mode,"
                 + " requester_Name,requester_Contact_No,work_Station_IP,notify_Email,request_Branch,request_Department,"
                 + " Company_Code,request_Subject,request_Description,create_time,technician,requester_id,nature,sla_id,create_date,New_Mail_Issue_Status) "
                 + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
       
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
             pstmt.setString(9, issueMode);
             userId = (userId == null || userId.equals(""))?"0":userId;
             pstmt.setInt(10, Integer.parseInt(userId));
             pstmt.setString(11, responseMode);
             pstmt.setString(12, requesterName);
             pstmt.setString(13, requesterContactNum);
             pstmt.setString(14, request.getRemoteAddr());
             pstmt.setString(15, notifymail); 
             pstmt.setString(16, user.getBranch());
             pstmt.setString(17, user.getDeptCode());
             pstmt.setString(18, companyCode);
//             pstmt.setString(19, user.getSection());
             pstmt.setString(19, requestSubject);
             pstmt.setString(20, requestDescription);
             pstmt.setString(21, timer.format(new java.util.Date()));                 
             pstmt.setString(22, technician); 
             pstmt.setString(23, requesterId);
             pstmt.setString(24, "normal");  
             pstmt.setInt(25, Slaid); 
             pstmt.setDate(26, df.dateConvert(new java.util.Date()));
             pstmt.setString(27,"N");
          doneSave = (pstmt.executeUpdate() == -1); //undo these
             
    //         System.out.println("<<<<<< INCIDENDE doneSave >>>>>> "+doneSave);
             	String SenderDeptQry = "SELECT  dept_code   FROM   AM_GB_USER where User_id = " + userId;
             	String SenderDept = aprecords.getCodeName(SenderDeptQry); 
                String incidentQuery = "SELECT  description   FROM   HD_COMPLAINT_TYPE where complaint_type_code = '"+Comptype+"'";
                String incidentName = aprecords.getCodeName(incidentQuery);
                String UnitHeadMailQuery = "SELECT  email   FROM   AM_AD_DEPARTMENT where Dept_code = '"+category+"'";
                String UnitHeadMail = aprecords.getCodeName(UnitHeadMailQuery);
//                String SenderQry = "SELECT  email   FROM   AM_GB_USER where User_id = " + userId;
//                String Sender = aprecords.getCodeName(SenderQry); 
                
                String SenderQry = "SELECT  email   FROM   AM_AD_DEPARTMENT where Dept_code = '"+SenderDept+"' ";
                String Sender = aprecords.getCodeName(SenderQry);
                String StatusQry = "select status_description from hd_status where status_code= '"+status+"' ";
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
//              		 System.out.println("TransactionType = "+TransactionType+" category = "+category+" SubCategory = "+category+" pageType = "+pageType+" technician = "+technician+" userId = "+userId+" requestDescription = "+requestDescription);
//               		System.out.println("complaintId = "+complaintId+" Subject = "+requestSubject+" FileName = "+FileName+" FieldName = "+FieldName+" notifyEmail = "+notifymail+" userId = "+userId);
               		                 	  
             //   	mails.SimpleMailWithAttachment(url, requestSubject, mail.getEmailMessageOriginal(TransactionType, category, subcategory, pageType, technician, userId, requestDescription,complaintId,requestSubject,FileName,FieldName),Sender,userId);
//  16/04/2012     	mails.SimpleMailWithAttachment(url, Subject, mail.getEmailMessageOriginal(TransactionType, category, subcategory, pageType, technician, userId, requestDescription,complaintId,requestSubject,FileName,FieldName,Subject,status,"","","",""),notifymail,userId,mailto);
                    //insert image if available  
//                    String sessionId = request.getSession().getId();
//                    System.out.println("<<<<<< sessionId >>>>>> "+sessionId);
//                    if (aprecords.getIncidentImage(sessionId) != null) {
//                    	aprecords.setProblemImage(sessionId,userId,complaintId,pageName);
//                      //  aprecords.setIncidentImage(aprecords.getIncidentImage(sessionId), complaintId);
//                    }
 
                    File newFile = null;
				            for(Iterator iter = formItems.iterator(); iter.hasNext();)
				            {
				                FileItem item = (FileItem)iter.next();
				                if(!item.isFormField())
				                {
				                	 String fieldName = item.getFieldName();
				                	 if(fieldName.equals("file")) {
				                		 
				                		 String name = item.getName().toString();
				                		 String fileName = (new File(item.getName())).getName();
				                		 String filePath = uploadPath + fileName.toString();
				                		 String newPath = uploadPath + "COMPLAINT_ID" + complaintId + ".pdf";
				                    File oldFile = new File(filePath);
				                
				                    newFile = new File(newPath);
				                    
			                         item.write(newFile);
				                }
				                	 
				                }
				            }
				            
				            String path = "WCOMPLAINT_ID_" + complaintId + ".pdf";
				            System.out.println("Path is : " +path);
				            Path yourFile = Paths.get(newFile.toString());
				            System.out.println("Your File Path is : " + yourFile);
				            Files.move(yourFile, yourFile.resolveSibling(path));
				            System.out.println("Upload has been done successfully!");
                    //About to send email to recipients
                    String realPath = getServletConfig().getServletContext().getRealPath("/mailConfig/legend.properties");
                    FileInputStream fis = new FileInputStream(realPath);
                    ComplaintManager hdManager = new ComplaintManager();

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