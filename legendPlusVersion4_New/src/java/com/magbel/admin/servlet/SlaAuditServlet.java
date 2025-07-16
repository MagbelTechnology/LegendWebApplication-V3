/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.magbel.admin.servlet;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletConfig;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServlet;
import audit.*;
import com.magbel.admin.handlers.AdminHandler;
import com.magbel.admin.objects.State;
import com.magbel.util.HtmlComboRecord;
import com.magbel.admin.handlers.ApprovalRecords;

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

		// String type = request.getParameter("TYPE");
		String statusMessage = "";
		boolean updtst = false;
		// java.sql.Date dt = new java.sql.Date();
		AuditTrailGen audit = new AuditTrailGen();
		// java.text.SimpleDateFormat sdf = new
		// java.text.SimpleDateFormat("dd/mm/yyyy");
		HtmlComboRecord htmlCombo = new HtmlComboRecord();
		com.magbel.admin.handlers.ApprovalRecords aprecords = new com.magbel.admin.handlers.ApprovalRecords();
		// String loginId = request.getParameter("loginId");
		int userID;
		String userId = (String) session.getAttribute("CurrentUser");
		if (userId == null) {
			userID = 0;
		} else {
			userID = Integer.parseInt(userId);
		}

		String branchcode = (String) session.getAttribute("UserCenter");
		if (branchcode == null) {
			branchcode = "not set";
		}   
		String buttSave = request.getParameter("buttSave");
		//String sla_ID = request.getParameter("sla_ID");
		String sla_ID = request.getParameter("slaId");		
       // System.out.println("------------------sla_ID ->" + sla_ID); 
		String sla_name = request.getParameter("sla_name"); 
		// System.out.println("------sla_name -> " + sla_name); 
		String sla_description = request.getParameter("sla_description"); 
		String DeptCode = request.getParameter("DeptCode"); 
		String responseDay = request.getParameter("day"); 
		String responseHour = request.getParameter("hour"); 
		String responseMinutew = request.getParameter("minutes"); 
		String resolveDay = request.getParameter("day2");     
		String resolveHour = request.getParameter("hour2");  
		String resolveMinutes = request.getParameter("minutes2");
		String CategoryName = request.getParameter("Category_Name"); 
		String create = request.getParameter("create"); 
		String Status = request.getParameter("Status"); 
//		System.out.println("------Status----> "+Status);
		if(Status.equalsIgnoreCase("ACTIVE")){Status = "Y";}
		if(Status.equalsIgnoreCase("CLOSED")){Status = "N";}
//		System.out.println("------Status- After ---> "+Status);
	//	System.out.println("------Category_Code----> "+CategoryName);
	//	System.out.println("------DeptCode----> "+DeptCode);
	//	System.out.println("------create----> "+create);
		String constraintResponse = request.getParameter("constraint");
		 constraintResponse = "A";
		//String fld = htmlCombo.getGeneratedId3(DeptCode);
		//System.out.println("------------------fld of DeptCode ->" + fld); 
		String escaleteResponseDay = request.getParameter("day3"); 
		String escaleteResponseHour = request.getParameter("hour3"); 
		String escaleteResponseMinutes = request.getParameter("minutes3");
		String escaleteResolveDay = request.getParameter("day7"); 
		String escaleteResolveHour = request.getParameter("hour7"); 
		String escaleteResolveMinutes = request.getParameter("minutes7");
		String Dept_Code = aprecords.getCodeName("select Dept_code from AM_AD_DEPARTMENT  where Dept_name='"+DeptCode+"'");
		String CatCode = aprecords.getCodeName("select sub_category_code from HD_COMPLAIN_SUBCATEGORY  where sub_category_name='"+CategoryName+"'");
		//DeptCode = Dept_Code;
//		System.out.println("----Depart_name of DeptCode ->" + DeptCode);
//		System.out.println("------CategoryCode----> "+CatCode);
		String constraintEscaleteResponse = request.getParameter("radiobutton1");
                //System.out.println("------------------radiobutton1 ->" + constraintEscaleteResponse); 
		String constraintEscaleteResolve = request.getParameter("radiobutton7");
	//	System.out.println("------------------radiobutton7 ->" + constraintEscaleteResolve); 
		  
		//String nameEscaleteResponse = request.getParameter("escalate1field");
		String nameEscaleteResolve2 = request.getParameter("escalate1field7");
		System.out.println("------------------escalate1field7 ->" + nameEscaleteResolve2); 
		com.magbel.admin.objects.Sla sla = new com.magbel.admin.objects.Sla();
		com.magbel.admin.objects.RuleConstraints subValues = new com.magbel.admin.objects.RuleConstraints();
		String id=""; 
		sla.setSla_ID(sla_ID);
		sla.setSla_name(sla_name);
		sla.setSla_description(sla_description);
		sla.setDeptCode(DeptCode);
		sla.setCatCode(CatCode);
		sla.setUserId(userId);
		sla.setStatus(Status);
		com.magbel.admin.handlers.AdminHandler admin = new com.magbel.admin.handlers.AdminHandler();
                String roleid =admin.getPrivilegesRole("Manage Sla");
		try {
   
			if (buttSave != null) {
				if (create.equals("Y")) {
					if (admin.getSlaById(sla_ID) != null) {
						out.print("<script>alert('The sla id already exists .');</script>");
						out.print("<script>history.go(-1);</script>");
					} else {
					   id=admin.createSlaNew(sla);
				//	   System.out.println("<<<<<<<<< id >>>>>>>> "+id);
						//id=admin.createSlaNew1(id,sla_name, sla_description,userID, Priority);
						if (id!="" || id.equalsIgnoreCase("")) 
						{
							  System.out.println("------------------id ->" + id); 
							subValues.setCriteria_ID(id);
							subValues.setCONSTRAINT(constraintEscaleteResolve);
							//subValues.setNAME(nameEscaleteResponse);
							subValues.setRESOLVE_DAY(escaleteResolveDay);
							subValues.setRESOLVE_HOUR(escaleteResolveHour);
							subValues.setRESOLVE_MINUTE(escaleteResolveMinutes);
							subValues.setRESPONSE_DAY(escaleteResponseDay);
							subValues.setRESPONSE_HOUR(escaleteResponseHour);
							subValues.setRESPONSE_MINUTE(escaleteResponseMinutes);
							//subValues.setCONSTRAINT2(constraintEscaleteResponse);
							//admin.createEscalate(subValues,nameEscaleteResolve2,DeptCode,CatCode);
							
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

				}
				if (!sla_ID.equals("")) {
					sla.setSla_ID(sla_ID);
					int idSla =  Integer.parseInt(sla_ID);
					audit.select(1,
							"SELECT * FROM  AM_GB_SLA  WHERE sla_ID = '"
									+ sla_ID + "'");
					updtst = admin.updateSla(sla);
					admin.createEscalate(subValues,nameEscaleteResolve2,DeptCode,CatCode,idSla);
					admin.createResponse(subValues,DeptCode,CatCode,idSla);
					
					audit.select(2,
							"SELECT * FROM AM_GB_SLA  WHERE sla_ID = '"
									+ sla_ID + "'");
					audit.logAuditTrail("AM_GB_SLA ", branchcode, userID,
							roleid,"","");
					if (updtst == true) {
					//	 System.out.println("----------id update---1--------->"+sla.getSla_ID());
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
				//		subValues.setCONSTRAINT2(constraintEscaleteResponse);				
					 System.out.println("-------------gg-------------->"+admin.updateEscalate(subValues,nameEscaleteResolve2,DeptCode,CatCode)); 
						// System.out.println("----------id update----2-------->"+id);
						subValues.setCriteria_ID(id);
						subValues.setCONSTRAINT(constraintResponse);
						subValues.setNAME("");
						subValues.setRESOLVE_DAY(resolveDay);  
						subValues.setRESOLVE_HOUR(resolveHour);
						subValues.setRESOLVE_MINUTE(resolveMinutes);
						subValues.setRESPONSE_DAY(responseDay);
						subValues.setRESPONSE_HOUR(responseHour);
						subValues.setRESPONSE_MINUTE(responseMinutew);
						
					 System.out.println("-------------ggs-------------->"+admin.updateResponse(subValues,DeptCode,CatCode));
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
