/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.magbel.legend.servlet;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletConfig;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import audit.*;
import legend.admin.handlers.AdminHandler;
import legend.admin.objects.State;

import com.magbel.util.HtmlComboRecord;
import com.magbel.legend.bus.ApprovalRecords;

/**
 * <p>
 * Title:
 * </p>
 *
 * <p>
 * Description:
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 *
 * <p>
 * Company:
 * </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class SlaAuditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final int THRESHOLD_SIZE = 0x300000;
    private static final int MAX_FILE_SIZE = 0xa00000;
    private static final int REQUEST_SIZE = 0x3200000;
	public SlaAuditServlet() {
	}

	/**
	 * Initializes the servlet.
	 *
	 * @param config
	 *            ServletConfig
	 * @throws ServletException
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	/**
	 * Destroys the servlet.
	 */
	public void destroy() {

	}

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 *
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		response.setDateHeader("Expires", -1);

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
        System.out.println((new StringBuilder("The url is : ")).append(UPLOAD_FOLDER).toString());
        String uploadPath = UPLOAD_FOLDER;
        
        String previousPage = request.getParameter("previousPage");
//        String assetId = request.getParameter("assetId");
     
        
        File uploadDir = new File(uploadPath);
        if(!uploadDir.exists())
        {
            uploadDir.mkdir();
        }  

		// String type = request.getParameter("TYPE");
		String statusMessage = "";
		boolean updtst = false;
		 String buttSave = "";
		 String Status = "";
		 String sla_name = "";
		 String sla_ID = "";
		 String sla_description =  "";
		String DeptCode = "";
		String responseDay = ""; 
		String responseHour = "";
		String responseMinutew = ""; 
		String resolveDay = "";     
		String resolveHour = "";
		String resolveMinutes = "";
		String CategoryName = ""; 
		String create = ""; 
		String constraintResponse = "";
		String escaleteResponseDay = "";
		String escaleteResponseHour = "";
		String escaleteResponseMinutes = "";
		String escaleteResolveDay = "";
		String escaleteResolveHour = ""; 
		String escaleteResolveMinutes = "";
		String slaStartDate = "";	
		String slaEndDate = "";
		String nameEscaleteResolve2 = "";
		String constraintEscaleteResponse = "";
		String constraintEscaleteResolve = "";
		String alertFreq = "";
		String alertStart = "";
		// java.sql.Date dt = new java.sql.Date();
		AuditTrailGen audit = new AuditTrailGen();
		// java.text.SimpleDateFormat sdf = new
		// java.text.SimpleDateFormat("dd/mm/yyyy");
		HtmlComboRecord htmlCombo = new HtmlComboRecord();
		com.magbel.legend.bus.ApprovalRecords aprecords = new com.magbel.legend.bus.ApprovalRecords();
		// String loginId = request.getParameter("loginId");
		int userID;
		String userId = (String) session.getAttribute("CurrentUser");
		if (userId == null) {
			userID = 0;
		} else {
			userID = Integer.parseInt(userId);
		}

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
                           if(fieldName.equals("buttSave")) {buttSave=item.getString();}     
                           if(fieldName.equals("Status")) {Status=item.getString(); }                   
                           if(fieldName.equals("sla_name")) {sla_name=item.getString();}
                           if(fieldName.equals("slaId")) {sla_ID =item.getString();}                     
                           if(fieldName.equals("sla_description")) {sla_description =item.getString();} 
                           if(fieldName.equals("DeptCode")) {DeptCode =item.getString();} 
                           if(fieldName.equals("day")) {responseDay =item.getString();} 
                           if(fieldName.equals("hour")) {responseHour =item.getString();} 
                           if(fieldName.equals("minutes")) {responseMinutew =item.getString();} 
                           if(fieldName.equals("day2")) {resolveDay =item.getString();} 
                           if(fieldName.equals("hour2")) {resolveHour =item.getString();} 
                           if(fieldName.equals("minutes2")) {resolveMinutes =item.getString();} 
                           if(fieldName.equals("Category_Name")) {CategoryName =item.getString();} 
                           if(fieldName.equals("create")) {create =item.getString();} 
                           if(fieldName.equals("constraint")) {constraintResponse =item.getString();} 
                           if(fieldName.equals("day3")) {escaleteResponseDay =item.getString();} 
                           if(fieldName.equals("hour3")) {escaleteResponseHour =item.getString();} 
                           if(fieldName.equals("minutes3")) {escaleteResponseMinutes =item.getString();} 
                           if(fieldName.equals("day7")) {escaleteResolveDay =item.getString();} 
                           if(fieldName.equals("hour7")) {escaleteResolveHour =item.getString();} 
                           if(fieldName.equals("minutes7")) {escaleteResolveMinutes =item.getString();} 
                           if(fieldName.equals("slaStartDate")) {slaStartDate =item.getString();} 
                           if(fieldName.equals("escalate1field7")) {nameEscaleteResolve2 =item.getString();} 
                           if(fieldName.equals("radiobutton1")) {constraintEscaleteResponse =item.getString();} 
                           if(fieldName.equals("radiobutton7")) {constraintEscaleteResolve =item.getString();} 
                           if(fieldName.equals("alertFreq")) {alertFreq =item.getString();} 
                           if(fieldName.equals("alertStart")) {alertStart =item.getString();} 
 //                          System.out.println("=======resolveDay===>: "+resolveDay+" ======>>>>>resolveHour: "+resolveHour+"  =====resolveMinutes: "+resolveMinutes);
                           if(fieldName.equals("slaEndDate")) {
                        	   slaEndDate=item.getString();
                               System.out.println("slaEndDate is =>: " + slaEndDate);                        
                           }  
                  }
                 
                 //item.write(newFilePath);
              }
		
		String branchcode = (String) session.getAttribute("UserCenter");
		if (branchcode == null) {
			branchcode = "not set";
		}   

		if(Status.equalsIgnoreCase("ACTIVE")){Status = "Y";}
		if(Status.equalsIgnoreCase("CLOSED")){Status = "N";}

		 constraintResponse = "A";

        String dd = slaStartDate.substring(0,2);
        String mm = slaStartDate.substring(3,5);
        String yyyy = slaStartDate.substring(6,10);
        slaStartDate = yyyy+"-"+mm+"-"+dd;
//		String slaEndDate = request.getParameter("slaEndDate");
        String sdd = slaEndDate.substring(0,2);
        String smm = slaEndDate.substring(3,5);
        String syyyy = slaEndDate.substring(6,10);
        slaEndDate = syyyy+"-"+smm+"-"+sdd;
		String Dept_Code = aprecords.getCodeName("select Dept_code from AM_AD_DEPARTMENT  where Dept_name='"+DeptCode+"'");
		String CatCode = aprecords.getCodeName("select sub_category_code from HD_COMPLAIN_SUBCATEGORY  where sub_category_name='"+CategoryName+"'");
		String slaID = "";
//		System.out.println("------------------escalate1field7 ->" + nameEscaleteResolve2); 
		
        String loginId = (String)session.getAttribute("CurrentUser");
        int loginID = Integer.parseInt(loginId);
        String computerName = null;
        String remoteAddress = request.getRemoteAddr();
        InetAddress inetAddress = InetAddress.getByName(remoteAddress);
        computerName = inetAddress.getHostName();
        if(computerName.equalsIgnoreCase("localhost"))
        {
            computerName = InetAddress.getLocalHost().getCanonicalHostName();
        }
        String hostName = "";
        if(hostName.equals(request.getRemoteAddr()))
        {
            InetAddress addr = InetAddress.getByName(request.getRemoteAddr());
            hostName = addr.getHostName();
        }
        if(InetAddress.getLocalHost().getHostAddress().equals(request.getRemoteAddr()))
        {
            hostName = "Local Host";
        }
        InetAddress ip = InetAddress.getLocalHost();
        String ipAddress = ip.getHostAddress();
        NetworkInterface network = NetworkInterface.getByInetAddress(ip);
        byte mac[] = network.getHardwareAddress();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < mac.length; i++)
        {
            sb.append(String.format("%02X%s", new Object[] {
                Byte.valueOf(mac[i]), i >= mac.length - 1 ? "" : "-"
            }));
        }

        String macAddress = sb.toString();
//        System.out.println(sb.toString());
//        System.out.println("======>>>>>alertFreq: "+alertFreq+"  =====alertStart: "+alertStart);
		legend.admin.objects.Sla sla = new legend.admin.objects.Sla();
		legend.admin.objects.RuleConstraints subValues = new legend.admin.objects.RuleConstraints();
		String id=""; 
		sla.setSla_ID(sla_ID);
		sla.setSla_name(sla_name);
		sla.setSla_description(sla_description);
		sla.setDeptCode(DeptCode);
		sla.setCatCode(CatCode);
		sla.setUserId(userId);
		sla.setStatus(Status);
		sla.setSlaStartDate(slaStartDate);
		sla.setSlaEndDate(slaEndDate);
		 if((alertStart==null)||(alertStart.equals(""))){alertStart = "0";}
		System.out.println("======>>>>>sla_ID: "+sla_ID+"     alertStart: "+alertStart+"   resolveHour: "+resolveHour+"    resolveMinutes: "+resolveMinutes);
		String alertStartDate = aprecords.getCodeName(" (SELECT DATEADD(day, "+alertStart+", '"+slaEndDate+"')) ");
		System.out.println("======>>>>>alertStartDate: "+alertStartDate+"   alertStart: "+alertStart+"      alertStartDate substring: "+alertStartDate.substring(0,10));
		alertStartDate = alertStartDate.substring(0,10) +" "+resolveHour+":"+resolveMinutes+":"+"00";
		legend.admin.handlers.AdminHandler admin = new legend.admin.handlers.AdminHandler();
                String roleid =admin.getPrivilegesRole("Manage Sla");

		try {
   
			if (buttSave != null) {
				if (create.equals("Y")) {
					if (admin.getSlaById(sla_ID) != null) {
						out.print("<script>alert('The sla id already exists .');</script>");
						out.print("<script>history.go(-1);</script>");
					} else {
					   id=admin.createSlaNew(sla);
					   slaID = id; 
					   int idSla =  Integer.parseInt(sla_ID);
//					   System.out.println("<<<<<<<<< id >>>>>>>> "+id+"     slaID: "+slaID);
						//id=admin.createSlaNew1(id,sla_name, sla_description,userID, Priority);
						if (id!="" || id.equalsIgnoreCase("")) 
						{
//							  System.out.println("------------------id ->" + id+"   sla_ID: "+sla_ID); 
							subValues.setCriteria_ID(id);
							subValues.setCONSTRAINT(constraintEscaleteResolve);
							//subValues.setNAME(nameEscaleteResponse);
							subValues.setRESOLVE_DAY(escaleteResolveDay);
							subValues.setRESOLVE_HOUR(escaleteResolveHour);
							subValues.setRESOLVE_MINUTE(escaleteResolveMinutes);
							subValues.setRESPONSE_DAY(escaleteResponseDay);
							subValues.setRESPONSE_HOUR(escaleteResponseHour);
							subValues.setRESPONSE_MINUTE(escaleteResponseMinutes);
							subValues.setCONSTRAINT2(constraintEscaleteResponse);
							//admin.createEscalate(subValues,nameEscaleteResolve2,DeptCode,CatCode);
							admin.createEscalate(subValues,nameEscaleteResolve2,DeptCode,CatCode,idSla);
							
							subValues.setCriteria_ID(id);
							subValues.setCONSTRAINT(constraintResponse);
							subValues.setNAME("");
							subValues.setRESOLVE_DAY(resolveDay);
							subValues.setRESOLVE_HOUR(resolveHour);
							subValues.setRESOLVE_MINUTE(resolveMinutes);
							subValues.setRESPONSE_DAY(responseDay);
							subValues.setRESPONSE_HOUR(responseHour);
							subValues.setRESPONSE_MINUTE(responseMinutew);
							//admin.createResponse(subValues,DeptCode,CatCode);
							subValues.setAlertFreq(Integer.parseInt(alertFreq));
							subValues.setAlertStart(alertStart);
//							alertStartDate = alertStartDate +":"+resolveHour+":"+resolveMinutes+":"+"00";
							subValues.setAlertStartDate(alertStartDate);
							
							admin.createResponse(subValues,DeptCode,CatCode,idSla);
							
							out.print("<script>alert('Record saved successfully.');</script>");
							//out.print("<script>window.location = 'DocumentHelp.jsp?np=slaSetup&sla_ID="+ admin.getSlaById(sla_ID).getSla_ID() + "';</script>");
							out.print("<script>window.location = 'DocumentHelp.jsp?np=manageSla&Status=Y';</script>");
							 
						}
					/*	if (admin.createSla(sla)) 
						{
							
							out.print("<script>alert('Record saved successfully.');</script>");
							out.print("<script>window.location = 'DocumentHelp.jsp?np=slaSetup&sla_ID="
											+ admin.getSlaById(sla_ID).getSla_ID() + "';</script>");
						}*/
					}
					String slaId = aprecords.getCodeName("select SLA_ID from AM_GB_SLA  where SLA_ID='"+slaID+"'");
							File newFile = null;

							boolean resp = false;
							

							if(slaId!=""){
					//++++++++++Image Attachement Start +++++++++++++++++++++++++++++++++
						           // List formItems = upload.parseRequest(request);
						            for(Iterator iter = formItems.iterator(); iter.hasNext();)
						            {
						                FileItem item = (FileItem)iter.next();
						                if(!item.isFormField())
						                {
						                	 String fieldName = item.getFieldName();
						                	 
//						                	 System.out.println("The fieldName is ==: " + fieldName);
						                	 
						                	 if(fieldName.equals("file")) {
						                		 
						                		 String name = item.getName().toString();
						                		 
//						                		 System.out.println("The name is =>: " + name);
						                	
						                    String fileName = (new File(item.getName())).getName();
//						                    System.out.println("The file name is =>: " + fileName);
//						                    System.out.println("The slaID is =>: " + slaID);
						                    String filePath = uploadPath + fileName.toString();
//						                    System.out.println("The file path is ==: " + filePath);
//						                    System.out.println("<===== slaID for New ID is =>: " + slaID);
//						                    slaID = "419";
						                    String newPath = uploadPath + "SLA" + slaID + ".pdf";
//						                    System.out.println("The new file path is >=: " + newPath);
						                    
						                   
						                    File oldFile = new File(filePath);
						                
						                    newFile = new File(newPath);
						                    
					                         item.write(newFile);
						                }
						                	 
						                }
						            }
						            
						            String path = "SLA" + slaID + ".pdf";
						            System.out.println("Path is : " +path);
						            Path yourFile = Paths.get(newFile.toString());
						            System.out.println("Your File Path is : " + yourFile);
						            Files.move(yourFile, yourFile.resolveSibling(path));
						            System.out.println("Upload has been done successfully!");
						        //  ++++++++++Image Attachement End +++++++++++++++++++++++++++++++++	   	            
							}
				}
				if (!sla_ID.equals("")) {
					sla.setSla_ID(sla_ID);
					 slaID = sla_ID;
					int idSla =  Integer.parseInt(sla_ID);
					audit.select(1,
							"SELECT * FROM  AM_GB_SLA  WHERE sla_ID = '"
									+ sla_ID + "'");
					updtst = admin.updateSla(sla);
//					admin.createEscalate(subValues,nameEscaleteResolve2,DeptCode,CatCode,idSla);
//					admin.createResponse(subValues,DeptCode,CatCode,idSla);
					
					audit.select(2,
							"SELECT * FROM AM_GB_SLA  WHERE sla_ID = '"
									+ sla_ID + "'");
					
//					audit.logAuditTrail("AM_GB_SLA ", branchcode, userID,
//							roleid);
					updtst = audit.logAuditTrail("AM_GB_SLA", branchcode, loginID, roleid, hostName, ipAddress, macAddress);
//					if (updtst == true) {
//						 System.out.println("----------id update---1--------->"+sla.getSla_ID());
						 id=sla.getSla_ID();
						subValues.setCriteria_ID(id);
						subValues.setCONSTRAINT(constraintEscaleteResolve);
				//		subValues.setNAME(nameEscaleteResponse);
						subValues.setRESOLVE_DAY(escaleteResolveDay);
						subValues.setRESOLVE_HOUR(escaleteResolveHour);
						subValues.setRESOLVE_MINUTE(escaleteResolveMinutes);
						subValues.setRESPONSE_DAY(escaleteResponseDay);
						subValues.setRESPONSE_HOUR(escaleteResponseHour);
						subValues.setRESPONSE_MINUTE(escaleteResponseMinutes);
						audit.select(1,"SELECT * FROM  SLA_ESCALATE  WHERE criteria_ID = '"+ sla_ID + "'");
						updtst = admin.updateSla(sla);
						updtst= admin.updateEscalate(subValues,nameEscaleteResolve2,DeptCode,CatCode);
//						System.out.println("----------id update----2-------->"+id+"   sla_ID: "+sla_ID);
						audit.select(2,"SELECT * FROM SLA_ESCALATE  WHERE criteria_ID = '"+ sla_ID + "'");

						updtst = audit.logAuditTrail("SLA_ESCALATE", branchcode, loginID, roleid, hostName, ipAddress, macAddress);

						subValues.setCriteria_ID(id);
						subValues.setCONSTRAINT(constraintResponse);
						subValues.setNAME("");
						subValues.setRESOLVE_DAY(resolveDay);  
						subValues.setRESOLVE_HOUR(resolveHour);
						subValues.setRESOLVE_MINUTE(resolveMinutes);
						subValues.setRESPONSE_DAY(responseDay);
						subValues.setRESPONSE_HOUR(responseHour);
						subValues.setRESPONSE_MINUTE(responseMinutew);
						subValues.setAlertFreq(Integer.parseInt(alertFreq));
						subValues.setAlertStart(alertStart);
//						System.out.println("<<<<=======alertStartDate===>: "+alertStartDate);
						
//						System.out.println("=======alertStartDate===>: "+alertStartDate+" ======>>>>>resolveHour: "+resolveHour+"  =====resolveMinutes: "+resolveMinutes);													
						subValues.setAlertStartDate(alertStartDate);
						System.out.println("=======Id===>: "+id+" ======>>>>>alertFreq: "+alertFreq+"  =====alertStart: "+alertStart);
//						System.out.println("----------id update----2-------->"+id+"   sla_ID: "+sla_ID);
						audit.select(1,"SELECT * FROM  SLA_RESPONSE  WHERE criteria_ID = '"+ sla_ID + "'");
						updtst = admin.updateResponse(subValues,DeptCode,CatCode);
						 
						audit.select(2,"SELECT * FROM SLA_RESPONSE  WHERE criteria_ID = '"+ sla_ID + "'");
//						System.out.println("----------id update----2-------->");
						updtst = audit.logAuditTrail("SLA_RESPONSE", branchcode, loginID, roleid, hostName, ipAddress, macAddress);
						
					 String slaId = aprecords.getCodeName("select SLA_ID from AM_GB_SLA  where SLA_ID='"+slaID+"'");
					 File newFile = null;
					 if(slaId!=""){
						//++++++++++Image Attachement Start +++++++++++++++++++++++++++++++++
//							            List formItems = upload.parseRequest(request);
							            for(Iterator iter = formItems.iterator(); iter.hasNext();)
							            {
							                FileItem item = (FileItem)iter.next();
							                if(!item.isFormField())
							                {
							                	
							                	 String fieldName = item.getFieldName();
							                	 
							                	 if(fieldName.equals("file")) {
							                	
							                    String fileName = (new File(item.getName())).getName();
//							                    System.out.println("The file name is =>: " + fileName);
//							                    System.out.println("The slaID is =>: " + slaID);
//							                    slaID = "419";
							                    String filePath = uploadPath + "SLA" + fileName.toString();
//							                    System.out.println("The file path is ==: " + filePath);
//							                    System.out.println("<===== slaID Old ID is =>: " + slaID);
							                    String newPath = uploadPath + "SLA" + slaID +".pdf";
//							                    System.out.println("The new file path is >=: " + newPath);
							                    
							                   
							                    File oldFile = new File(filePath);
							                    
							                
							                    newFile = new File(newPath);
							                    
						                         item.write(newFile);
							                    
							                	 }
							                } 
							            }
							            String path = "SLA"+slaID + ".pdf";
							            System.out.println("Path is : " +path);
							            Path yourFile = Paths.get(newFile.toString());
							            System.out.println("Your File Path is : " + yourFile);
							            Files.move(yourFile, yourFile.resolveSibling(path));
							            System.out.println("Upload has been done successfully!");
							          //++++++++++Image Attachement End +++++++++++++++++++++++++++++++++	   	            
					 			}
					 if (updtst == true) {
						// statusMessage = "Update on record is successfull";
						out.print("<script>alert('Update on record is successfull')</script>");
						//out.print("<script>window.location = 'DocumentHelp.jsp?np=slaSetup&sla_ID="+ sla_ID + "';</script>");
					//	System.out.println("---------ggs sla_ID--------->"+sla_ID);
						out.print("<script>window.location = 'DocumentHelp.jsp?np=slajsp&slaId="+ admin.getSlaById(sla_ID).getSla_ID() + "&slaDeptCode="+ admin.getSlaById(sla_ID).getDeptCode() + "&Status="+ admin.getSlaById(sla_ID).getStatus()+ "&PC=2';</script>");
														 	
						// out.print("<script>window.location =
						// 'manageStates.jsp?status=A'</script>");
					   
					} else {
						// statusMessage = "No changes made on record";
						out.print("<script>alert('No changes made on record')</script>");
						out.print("<script>window.location = 'DocumentHelp.jsp?np=slaSetup&sla_ID="+ sla_ID  + "&Status="+ admin.getSlaById(sla_ID).getStatus()+ "&slaPriority="+ admin.getSlaById(sla_ID).getDeptCode()+ "';</script>");
					}
				}
				
			}
		} catch (Throwable e) {
			e.printStackTrace();
			// statusMessage = "Ensure unique record entry";
		//	out.print("<script>alert('Ensure unique record entry.')</script>");
			System.err.print(e.getMessage());
			out.print("<script>history.go(-1);</script>");

		}
	}

	/**
	 * Returns a short description of the servlet.
	 *
	 * @return String
	 */
	public String getServletInfo() {
		return "Company Audit Servlet";
	}

}
