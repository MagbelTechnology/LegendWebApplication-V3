/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magbel.legend.servlet;

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

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import magma.net.dao.MagmaDBConnection;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.GenerateList;
import com.magbel.legend.bus.ComplaintManager;
import com.magbel.legend.mail.MailSender;
import com.magbel.legend.mail.BulkMail;
import com.magbel.util.ApplicationHelper;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

@MultipartConfig(fileSizeThreshold = 1024 * 1024,
maxFileSize = 1024 * 1024 * 5, 
maxRequestSize = 1024 * 1024 * 5 * 5)
public class HelpDeskNewRequisitionServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
        PrintWriter out = response.getWriter();
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

//        DiskFileItemFactory factory = new DiskFileItemFactory();
//        factory.setSizeThreshold(0x300000);
//        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
//        ServletFileUpload upload = new ServletFileUpload(factory);
//        upload.setFileSizeMax(0xa00000L);
//        upload.setSizeMax(0x3200000L);
        Properties prop = new Properties();
//        File file = new File("C:\\Property\\LegendPlus.properties");
//        FileInputStream input = new FileInputStream(file);
        File file = new File(getServletConfig().getServletContext().getRealPath("/Property/legendPlus.properties"));
        FileInputStream input = new FileInputStream(file);
        
        prop.load(input);
        String UPLOAD_FOLDER = prop.getProperty("imagesUrl");
        System.out.println((new StringBuilder("The url is : ")).append(UPLOAD_FOLDER).toString());
        String uploadPath = UPLOAD_FOLDER;

        File uploadDir = new File(uploadPath);
        if(!uploadDir.exists())
        {
            uploadDir.mkdir();
        }
        
        
        String category =  request.getParameter("category");
        String subcategory =  request.getParameter("subcategory");
        String userId =  request.getParameter("userid");
        String priority =  request.getParameter("priority");
        String status =  request.getParameter("status");
        String technician =  request.getParameter("technician");
        String servicesAffected =  request.getParameter("servicesAffected");
        String requesterName =  request.getParameter("requesterName");
        String requesterContactNum =  request.getParameter("requesterContNumber");
        String notifyEmail =  request.getParameter("notifymail");
        String requestBranch =  request.getParameter("requesterBranch");
        String requestDepartment =  request.getParameter("requesterDept");
        String ReqSection =  request.getParameter("requesterSection");
        String requestTitle =  request.getParameter("requesttitle");
        String requestChange =  request.getParameter("requestchange");
        String requestDescription =  request.getParameter("requestDescription");
        String assetId =  request.getParameter("asset");
        String changeType =  request.getParameter("changeType");
        String scheduleStartTime =  request.getParameter("scheduleStartTime");
        String scheduleEndTime =  request.getParameter("scheduleEndTime");
        String operation =  request.getParameter("operation");
        String mtid =  request.getParameter("mtid");
        String companyCode =  request.getParameter("comp_code");
        String Comptype = request.getParameter("Comptype");
        String issueMode = request.getParameter("incidentMode");
        String requesterId = request.getParameter("requesterId");
        String notifymail = request.getParameter("notifymail");
        String requestSubject = request.getParameter("requestSubject");
        String pageType = request.getParameter("pageType");
        String pageName = request.getParameter("pageName");
        String returncode = request.getParameter("returncode");
        
        
        for (Part part : request.getParts()) {
        	 String FileName = "HD_COMPLAINT";
             String FieldName = "complaint_id";
//             returncode = request.getParameter("returncode");
//             technician = request.getParameter("technician");
             System.out.println("----notifymail---> "+notifymail);
             System.out.println("----req_subject1---> "+requestSubject);  
             System.out.println("----requesterId---> "+requesterId);
//             String status = "001";//String status = request.getParameter("status");
             System.out.println("===technician===="+technician);
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
             assetId = "0";
      //      System.out.println("===returncode HelpDeskNewRequisitionServlet=="+returncode);
//             System.out.println("===Slaid=="+Slaid);
             HttpSession session = request.getSession();
             legend.admin.objects.User user = null;
             if (session.getAttribute("_user") != null) {
                 user = (legend.admin.objects.User) session.getAttribute("_user");
      
                 currentUserName = user.getUserFullName();
             }
             operation = "Save";
             if (operation != null && operation.equalsIgnoreCase("Save")) {
                 String IssueQuery = "SELECT  description   FROM   HD_COMPLAINT_TYPE where complaint_type_code = '"+Comptype+"'";
                 System.out.println("----IssueQuery---> "+IssueQuery);
                 String IssueAcronym = aprecords.getCodeName(IssueQuery);
                 System.out.println("===IssueAcronym=="+IssueAcronym);
                 String complaintId = IssueAcronym.substring(0, 3) + "-" + applHelper.getGeneratedId("HD_COMPLAINT");
                 String insertReqnTable = "";

                 insertReqnTable = " insert into HD_COMPLAINT (complaint_id,complain_category,complain_sub_category,"
                         + " asset_id,priority,create_dateTime,status,complaint_Type,incident_Mode,user_id,response_Mode,"
                         + " requester_Name,requester_Contact_No,work_Station_IP,notify_Email,request_Branch,request_Department,"
                         + " Company_Code,request_Section,request_Subject,request_Description,create_time,technician,requester_id,nature,sla_id,create_date) "
                         + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
             
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
                     pstmt.setString(19, user.getDeptCode());
                     pstmt.setString(20, requestSubject);
                     pstmt.setString(21, requestDescription);
                     pstmt.setString(22, timer.format(new java.util.Date()));                 
                     pstmt.setString(23, technician);
                     pstmt.setString(24, requesterId);
                     pstmt.setString(25, "normal");   
                     pstmt.setInt(26, Slaid); 
                     pstmt.setDate(27, df.dateConvert(new java.util.Date()));
                  doneSave = (pstmt.executeUpdate() == -1); //undo these
                  done = true;
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
                  pstmt.setString(19, user.getDeptCode());
                  pstmt.setString(20, requestSubject);
                  pstmt.setString(21, requestDescription);
                  pstmt.setString(22, timer.format(new java.util.Date()));                 
                  pstmt.setString(23, technician); 
                  pstmt.setString(24, requesterId);
                  pstmt.setString(25, "normal");  
                  pstmt.setInt(26, Slaid); 
                  pstmt.setDate(27, df.dateConvert(new java.util.Date()));
                  pstmt.setString(28,"N");
                  done = true;
               doneSave = (pstmt.executeUpdate() == -1); //undo these
                  
         //         System.out.println("<<<<<< INCIDENDE doneSave >>>>>> "+doneSave);
                  	String SenderDeptQry = "SELECT  dept_code   FROM   AM_GB_USER where User_id = " + userId;
                  	String SenderDept = aprecords.getCodeName(SenderDeptQry); 
                     String incidentQuery = "SELECT  description   FROM   HD_COMPLAINT_TYPE where complaint_type_code = '"+Comptype+"'";
                     String incidentName = aprecords.getCodeName(incidentQuery);
                     String UnitHeadMailQuery = "SELECT  email   FROM   AM_AD_DEPARTMENT where Dept_code = '"+category+"'" ;
                     String UnitHeadMail = aprecords.getCodeName(UnitHeadMailQuery);
//                     String SenderQry = "SELECT  email   FROM   AM_GB_USER where User_id = " + userId;
//                     String Sender = aprecords.getCodeName(SenderQry); 
                     
                     String SenderQry = "SELECT  email   FROM   AM_AD_DEPARTMENT where Dept_code = '"+SenderDept+"'";
                     String Sender = aprecords.getCodeName(SenderQry);
                     String StatusQry = "select status_description from hd_status where status_code= '"+status+"'";
                     String Status = aprecords.getCodeName(StatusQry);
                     String mailto = UnitHeadMail;
                     //notifyEmail = notifyEmail +""+aprecords.userEmail(userId);
                     String Subject = requestSubject + " " +complaintId +" "+Status;
                     
//                     if (!doneSave) { 
//                     	 String url = getServletConfig().getServletContext().getRealPath("");
//                   //  	 System.out.println("<<<<<< NOT doneSave url >>>>>> "+url);
//                     	// String mailnotify = aprecords.getmailaddresses();  
//                     	// String mailnotify = Sender; 
//                     	 if(notifymail.equalsIgnoreCase("")){
//                     	 notifymail = Sender;
//                     	 } else { 
//                         	 notifymail = notifymail + "," +Sender;
//                          }       
//                         // mails.SimpleMailWithAttachment(url, "INCIDENCE", mail.getEmailMessage(status, category, pageType, technician, userId, requestDescription,complaintId),notifyEmail,userId );
////                   		 System.out.println("TransactionType = "+TransactionType+" category = "+category+" SubCategory = "+category+" pageType = "+pageType+" technician = "+technician+" userId = "+userId+" requestDescription = "+requestDescription);
////                    		System.out.println("complaintId = "+complaintId+" Subject = "+requestSubject+" FileName = "+FileName+" FieldName = "+FieldName+" notifyEmail = "+notifymail+" userId = "+userId);
//                    		                 	  
//                  //   	mails.SimpleMailWithAttachment(url, requestSubject, mail.getEmailMessageOriginal(TransactionType, category, subcategory, pageType, technician, userId, requestDescription,complaintId,requestSubject,FileName,FieldName),Sender,userId);
     ////  16/04/2012     	mails.SimpleMailWithAttachment(url, Subject, mail.getEmailMessageOriginal(TransactionType, category, subcategory, pageType, technician, userId, requestDescription,complaintId,requestSubject,FileName,FieldName,Subject,status,"","","",""),notifymail,userId,mailto);
//                         //insert image if available  
//                         String sessionId = request.getSession().getId();
//                         if (aprecords.getIncidentImage(sessionId) != null) {
//                         	aprecords.setProblemImage(sessionId,userId,complaintId,pageName);
//                           //  aprecords.setIncidentImage(aprecords.getIncidentImage(sessionId), complaintId);
//                         }
     // 
//                         // to delete temporary images from image table
//                        // genList.updateTable(" delete from am_ad_image where sessionId = '" + sessionId+"'");
     //
     //
     //
//                         //About to send email to recipients
//                         String realPath = getServletConfig().getServletContext().getRealPath("/legendPlus/legendPlus.properties");
//                         FileInputStream fis = new FileInputStream(realPath);
//                         ComplaintManager hdManager = new ComplaintManager();
////                         hdManager.sendRecipientMail(fis, hdManager.getRecipientEmail(reqnID), reqnID);
     ///*
//                         if (notifyEmail.contains(";")) {
//                             String[] mailReciepents = notifyEmail.split(";");
//                             hdManager.sendRecipientsMail(fis, mailReciepents, complaintId, requestSubject, requestDescription, currentUserName);
//                         } else {
//                             String[] mailReciepents = new String[]{notifyEmail};
//                             hdManager.sendRecipientsMail(fis, mailReciepents, complaintId, requestSubject, requestDescription, currentUserName);
//                         }
     //*/ 
//                     
//                         out.print("<script>alert('" + incidentName + " saved successfully.');</script>");
//                         out.print("<script>window.location='DocumentHelp.jsp?np=hdNewRequisitionForm&reportId="+returncode+"&reqnId='</script>");
     //
//                     } 
//                     
                	 File newFile = null;

           			 System.out.println("The doneSave is ==: " + doneSave);
           			 System.out.println("The done is ==: " + done);
           			 if (done) {
           				if(complaintId!=""){
           					 String fileName = "";
           		//++++++++++Image Attachement Start +++++++++++++++++++++++++++++++++
           			           // List formItems = upload.parseRequest(request);
           			        
           	
           			                		 
           			                	
           			                		String name = part.getSubmittedFileName();
           		
           			                		 
           			                		 System.out.println("The name is =>: " + name);
           			                	
           			                     fileName = (new File(part.getName())).getName();
           			                    System.out.println("The file name is =>: " + fileName);
//           			                    System.out.println("The slaID is =>: " + slaID);
           			                    String filePath = uploadPath + fileName.toString();
//           			                    System.out.println("The file path is ==: " + filePath);
//           			                    System.out.println("<===== slaID for New ID is =>: " + slaID);
//           			                    slaID = "419";
           			                    String newPath = uploadPath + "INCIDENT" + complaintId + ".pdf";
//           			                    System.out.println("The new file path is >=: " + newPath);
           			                    
           			                    File oldFile = new File(filePath);
           			                
//           			                    newFile = new File(newPath);
//           			                    
//           		                      part.write(newFile.toString());
           			                    
           			                 newFile = new File(newPath);
        			                    
        			                    oldFile.renameTo(newFile);
        			                    
        		                      part.write(newPath);
           			            
           			            
           	                    if(fileName!=""){     
           			            String path = "ICD" + complaintId + ".pdf";
           			            System.out.println("Path is : " +path);
           			            Path yourFile = Paths.get(newFile.toString());
           			            System.out.println("Your File Path is : " + yourFile);
           			            Files.move(yourFile, yourFile.resolveSibling(path));
           			            System.out.println("Upload has been done successfully!");
           			        //  ++++++++++Image Attachement End +++++++++++++++++++++++++++++++++	
           	                    } 
           				} 
           			 
                         //About to send email to recipients
                         String realPath = getServletConfig().getServletContext().getRealPath("/Property/legendPlus.properties");
                         FileInputStream fis = new FileInputStream(realPath);
                         ComplaintManager hdManager = new ComplaintManager();
//                         hdManager.sendRecipientMail(fis, hdManager.getRecipientEmail(reqnID), reqnID);

                         if (notifyEmail.contains(";")) {
                             String[] mailReciepents = notifyEmail.split(";");
                             hdManager.sendRecipientsMail(fis, mailReciepents, complaintId, requestTitle, requestDescription, currentUserName);
                         } else {
                             String[] mailReciepents = new String[]{notifyEmail};
                             hdManager.sendRecipientsMail(fis, mailReciepents, complaintId, requestTitle, requestDescription, currentUserName);
                         }
                         out.print("<script>alert('" + incidentName + " saved successfully.');</script>");
                         out.print("<script>window.location='DocumentHelp.jsp?np=hdNewRequisitionForm&reqnId='</script>");
                     }
           			 
                        else {
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
    
 
}