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
public class SClassAuditServlet extends HttpServlet {
	public SClassAuditServlet() {
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

		// java.sql.Date dt = new java.sql.Date();
		AuditTrailGen audit = new AuditTrailGen();
		// java.text.SimpleDateFormat sdf = new
		// java.text.SimpleDateFormat("dd/mm/yyyy");
		String statusMessage = "";
		boolean updtst = false;
		// String loginId = request.getParameter("loginId");
		int loginID;
		String loginId = (String) session.getAttribute("CurrentUser");
		if (loginId == null) {
			loginID = 0;
		} else {
			loginID = Integer.parseInt(loginId);
		}

		String branchcode = (String) session.getAttribute("UserCenter");
		if (branchcode == null) {
			branchcode = "not set";
		}

		String classId = request.getParameter("classId");

		com.magbel.admin.objects.SecurityClass sc = new com.magbel.admin.objects.SecurityClass();
		com.magbel.admin.handlers.SecurityHandler sh = new com.magbel.admin.handlers.SecurityHandler();
                AdminHandler admin = new AdminHandler();
		String roleid =admin.getPrivilegesRole("Manage Security Class");
		String buttSave = request.getParameter("buttSave");
		// String buttAssg = request.getParameter("buttAssg");

		String description = request.getParameter("classDesc");
		String className = request.getParameter("className");
		String isSupervisor ="N";// request.getParameter("isSupervisor");
              //  System.out.println(">>-=------------------>>>"+isSupervisor);
		String classStatus = request.getParameter("classStatus");
		String userId = (String) session.getAttribute("CurrentUser");
		String fleetAdmin = "N";//request.getParameter("fleetAdmin");
                fleetAdmin = fleetAdmin == null?"N":fleetAdmin.trim();
                   // System.out.println(">>-=------------------>>>"+fleetAdmin);
		sc.setDescription(description);
		sc.setClassName(className);
		sc.setClassStatus(classStatus);
		sc.setIsSupervisor(isSupervisor);
		sc.setFleetAdmin(fleetAdmin);
		sc.setUserId(userId);
		try {

			if (buttSave != null) {

				if (classId.equals("")) {
					if (sh.getSecurityClassByName(className) == null) {
						if (sh.createSecurityClass(sc)) {

							out
									.print("<script>alert('Record saved successfully.')</script>");
							out
									.print("<script>window.location = 'DocumentHelp.jsp?np=securityClasses&classId="
											+ sh.getSecurityClassByName(
													className).getClassId()
											+ "&PC=10'</script>");
						} else {
							System.out
									.println("Error saving record: New record \nfor 'security class'  with class name "
											+ className
											+ " could not be created");
							out.print("<script>history.go(-1);</script>");
						}
					} else {

						out.print("<script>alert('Class Name " + className
								+ " exists.')</script>");
						out.print("<script>window.history.back()</script>");
					}
				} else if (!classId.equals("")) {
					sc.setClassId(classId);
					audit.select(1,
							"SELECT * FROM  AM_GB_CLASS  WHERE class_Id = '"
									+ classId + "'");
					boolean isupdt = sh.updateSecurityClass(sc);
					audit.select(2,
							"SELECT * FROM  AM_GB_CLASS  WHERE class_Id = '"
									+ classId + "'");
					updtst = audit.logAuditTrail("AM_GB_CLASS", branchcode,
							loginID, roleid,"","");
					if (updtst == true) {
						// statusMessage = "Update on record is successfull";
						out
								.print("<script>alert('Update on record is successfull')</script>");
						out
								.print("<script>window.location = 'DocumentHelp.jsp?np=securityClasses&classId="
										+ classId + "&PC=10'</script>");
						// out.print("<script>window.location =
						// 'manageBranchs.jsp?status=A'</script>");
					} else {
						// statusMessage = "No changes made on record";
						out
								.print("<script>alert('No changes made on record')</script>");
						out
								.print("<script>window.location = 'DocumentHelp.jsp?np=securityClasses&classId="
										+ classId + "&PC=10'</script>");
					}
				} 
			}
		} catch (Throwable e) {
			e.printStackTrace();
			// statusMessage = "Ensure unique record entry";
			out.print("<script>alert('Ensure unique record entry.')</script>");
			System.err.print(e.getMessage());
			out.print("<script>window.location = 'DocumentHelp.jsp?np=securityClasses&classId="
					+ classId + "&PC=10'</script>");
		}
	}
}
