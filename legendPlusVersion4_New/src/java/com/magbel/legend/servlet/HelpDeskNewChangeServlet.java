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

public class HelpDeskNewChangeServlet extends HttpServlet {

    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    MagmaDBConnection mgDbCon = null;
    ApplicationHelper applHelper = null;
    MailSender mailSender = null;
    ApprovalRecords aprecords = null;
    SimpleDateFormat timer = null;
    GenerateList genList = null;

    public HelpDeskNewChangeServlet() {
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
        String notifyEmail =  request.getParameter("notifyEmail");
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
       
        String requesterId = request.getParameter("requesterId");
        
       
        for (Part part : request.getParts()) {
//          System.out.println("<<<<<<<<=====servicesAffected: "+servicesAffected);
//          System.out.println("<<<<<<<<=====operation Immediately: "+operation);

          String currentUserName = "";
          HttpSession session = request.getSession();
          legend.admin.objects.User user = null;
          if (session.getAttribute("_user") != null) {
              user = (legend.admin.objects.User) session.getAttribute("_user");

              currentUserName = user.getUserFullName();


          }
//          System.out.println("<<<<<<<<=====operation: "+operation);
          if (operation != null && operation.equalsIgnoreCase("Save")) {
              String ChangeId = "Incd-" + applHelper.getGeneratedId("HD_CHANGES");
              String insertReqnTable = "";
            		 

              insertReqnTable = " insert into HD_CHANGES (Change_id,Category_Code,sub_category_Code,"
                      + " priority,create_date,status,user_id,"
                      + " requester_Name,requester_Contact_No,work_Station_IP,notify_Email,request_Branch,request_Department,"
                      + " Company_Code,request_Section,Change_Title,Change_Description,"
                      + "create_time,technician,requesterId,services_affected"
                      + ",change_type,Schedule_Start_Date,Schedule_End_Date) "
                      + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            
              try {

                  con = mgDbCon.getConnection("legendPlus");
//                  System.out.println("<<<<<<<<=====ChangeId: "+ChangeId);
                  pstmt = con.prepareStatement(insertReqnTable);
                  pstmt.setString(1, ChangeId);
                  pstmt.setString(2, category);
                  pstmt.setString(3, subcategory);
               //   pstmt.setString(4, assetId);
                  pstmt.setString(4, priority);
                  pstmt.setTimestamp(5, mgDbCon.getDateTime(new java.util.Date()));
                  pstmt.setString(6, status);
                  pstmt.setInt(7, Integer.parseInt(userId));
                  pstmt.setString(8, requesterName);
                  pstmt.setString(9, requesterContactNum);
                  pstmt.setString(10, request.getRemoteAddr());
                  pstmt.setString(11, notifyEmail);                
                   pstmt.setString(12, requestBranch);
                  pstmt.setString(13, requestDepartment);
                  pstmt.setString(14, companyCode);
                  pstmt.setString(15, ReqSection);
                  pstmt.setString(16, requestTitle);
                  pstmt.setString(17, requestDescription);
                  pstmt.setString(18, timer.format(new java.util.Date()));
                  pstmt.setString(19, technician);
                  pstmt.setString(20, requesterId);
               //   pstmt.setString(22, "Change");
                  pstmt.setString(21, servicesAffected);
                  pstmt.setString(22, changeType);
                  pstmt.setString(23, scheduleStartTime);
                  pstmt.setString(24, scheduleEndTime);
                  done = true;
               doneSave = (pstmt.executeUpdate() == -1); //undo these

                 // String incidentQuery = "SELECT  description   FROM   HD_COMPLAINT_TYPE where complaint_type_code = " + Integer.parseInt(complaintType);
                //  String incidentName = aprecords.getCodeName(incidentQuery);

  			 File newFile = null;

  			 System.out.println("The doneSave is ==: " + doneSave);
  			 System.out.println("The done is ==: " + done);
  			 if (done) {
  				if(ChangeId!=""){
  					 String fileName = "";
  		//++++++++++Image Attachement Start +++++++++++++++++++++++++++++++++
  			           // List formItems = upload.parseRequest(request);
  			           
  			               
  			      
  			                		 
  					 				String name = part.getSubmittedFileName();
  			                		 
  			                		 System.out.println("The name is =>: " + name);
  			                	
  			                     fileName = (new File(part.getName())).getName();
//  			                    System.out.println("The file name is =>: " + fileName);
//  			                    System.out.println("The slaID is =>: " + slaID);
  			                    String filePath = uploadPath + fileName.toString();
//  			                    System.out.println("The file path is ==: " + filePath);
//  			                    System.out.println("<===== slaID for New ID is =>: " + slaID);
//  			                    slaID = "419";
  			                    String newPath = uploadPath + "CHANGES" + ChangeId + ".pdf";
//  			                    System.out.println("The new file path is >=: " + newPath);
  			                    
  			                    File oldFile = new File(filePath);
  			                
//  			                    newFile = new File(newPath);
//  			                    
//  		                         part.write(newFile.toString());
  			                    
  			                  newFile = new File(newPath);
 			                    
 			                    oldFile.renameTo(newFile);
 			                    
 		                      part.write(newPath);
  			                
  			                	 
  			                
  			            
  	                    if(fileName!=""){     
  			            String path = "CHG" + ChangeId + ".pdf";
//  			            System.out.println("Path is : " +path);
  			            Path yourFile = Paths.get(newFile.toString());
  			            System.out.println("Your File Path is : " + yourFile);
  			            Files.move(yourFile, yourFile.resolveSibling(path));
//  			            System.out.println("Upload has been done successfully!");
  			        //  ++++++++++Image Attachement End +++++++++++++++++++++++++++++++++	
  	                    } 
  				} 
  			 
                //About to send email to recipients
                String realPath = getServletConfig().getServletContext().getRealPath("/Property/legendPlus.properties");
                FileInputStream fis = new FileInputStream(realPath);
                ComplaintManager hdManager = new ComplaintManager();
//                hdManager.sendRecipientMail(fis, hdManager.getRecipientEmail(reqnID), reqnID);

                if (notifyEmail.contains(";")) {
                    String[] mailReciepents = notifyEmail.split(";");
                    hdManager.sendRecipientsMail(fis, mailReciepents, ChangeId, requestTitle, requestDescription, currentUserName);
                } else {
                    String[] mailReciepents = new String[]{notifyEmail};
                    hdManager.sendRecipientsMail(fis, mailReciepents, ChangeId, requestTitle, requestDescription, currentUserName);
                }


                out.print("<script>alert('Change saved successfully.');</script>");
                out.print("<script>window.location='DocumentHelp.jsp?np=hdNewRequisitionChangeForm&reqnId='</script>");


            } 
            else {
                out.print("<script>alert('Change  not save.');</script>");
                out.print("<script>window.location='DocumentHelp.jsp?np=hdNewRequisitionChangeForm&reqnId='</script>");

            }				
  				
              } catch (Exception e) {
                  System.out.println("Error occurred when saving Change requisition : " + e);
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
}