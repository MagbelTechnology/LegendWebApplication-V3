/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.magbel.admin.servlet;

import com.magbel.util.CheckIntegerityContraint;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletConfig;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServlet;
import audit.*;

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
public class SectionAuditServlet extends HttpServlet {
	public SectionAuditServlet() {
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

		com.magbel.admin.objects.Section section = new com.magbel.admin.objects.Section();
		com.magbel.admin.handlers.AdminHandler admin = new com.magbel.admin.handlers.AdminHandler();

		String sectionId = request.getParameter("sectionId");
		String sectionStatus = request.getParameter("sectionStatus");
		String buttSave = request.getParameter("buttSave");

		String acronym = request.getParameter("sectionAcronym");

		if (acronym != null) {
			acronym = acronym.toUpperCase();
		}

		String section_code = request.getParameter("sectionCode");
		String section_name = request.getParameter("sectionName");
		String section_acronym = acronym;
		String userid = (String) session.getAttribute("CurrentUser");
		section.setSection_code(section_code);
		section.setSection_name(section_name);
		section.setSection_code(section_code);
		section.setUserid(userid);
		section.setSection_status(sectionStatus);
		section.setSection_acronym(section_acronym);
		try {

			if (buttSave != null) {

					if (sectionId.equals("")) {
						if (admin.getSectionByCode(section_code) != null) {
							out
									.print("<script>alert('The section code already exists .');</script>");
							out.print("<script>history.go(-1);</script>");
						} else {

							if (admin.createSection(section)) {
								out
										.print("<script>alert('Record saved successfully.');</script>");
								out
										.print("<script>window.location = 'DocumentHelp.jsp?np=sectionSetup&sectionId="
												+admin.getSectionByCode(section_code).getSection_id() + "';</script>");
							}
						}
					} else if (!sectionId.equals("")) {
						section.setSection_id(sectionId);
						CheckIntegerityContraint intCont = new CheckIntegerityContraint();
						if (intCont.checkReferenceConstraint("AM_GB_USERS",
								"SECTION_CODE", section_code, sectionStatus)) {
							out
									.print("<script>alert('Section Code is used by other records.')</script>");
							out
									.print("<script>window.location = 'DocumentHelp.jsp?np=sectionSetup&sectionId="
											+ sectionId + "'</script>");
						} else {
							// System.out.print(sectionId+"3");
							audit.select(1,
									"SELECT * FROM  AM_AD_SECTION   WHERE section_Id = '"
											+ sectionId + "'");
							boolean isupdt = admin.updateSection(section);
							audit.select(2,
									"SELECT * FROM  AM_AD_SECTION   WHERE section_Id = '"
											+ sectionId + "'");
							updtst = audit.logAuditTrail("AM_AD_SECTION",
									branchcode, loginID, sectionId,"","");
							if (updtst == true) {
								// statusMessage = "Update on record is
								// successfull";
								out
										.print("<script>alert('Update on record is successfull')</script>");
								out
										.print("<script>window.location = 'DocumentHelp.jsp?np=sectionSetup&sectionId="
												+ sectionId + "'</script>");
								// out.print("<script>window.location =
								// 'manageBranchs.jsp?status=A'</script>");
							} else {
								// statusMessage = "No changes made on record";
								out
										.print("<script>alert('No changes made on record')</script>");
								out
										.print("<script>window.location = 'DocumentHelp.jsp?np=sectionSetup&sectionId="
												+ sectionId + "'</script>");
							}
						}

					}}

		} catch (Throwable e) {
			e.printStackTrace();
			// statusMessage = "Ensure unique record entry";
			out.print("<script>alert('Ensure unique record entry.')</script>");
			// out.print("<script>window.location =
			// 'sectionSetup.jsp?sectionId="+sectionId+"'</script>");
			System.err.print(e.getMessage());
		}


}

	}

