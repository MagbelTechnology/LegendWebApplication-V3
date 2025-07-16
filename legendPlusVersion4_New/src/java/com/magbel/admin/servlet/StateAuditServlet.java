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
public class StateAuditServlet extends HttpServlet {
	public StateAuditServlet() {
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
		String stateId = request.getParameter("stateId");
		// if(stateId == null){stateId = "";}
		String stateCode = request.getParameter("stateCode");
		String stateName = request.getParameter("stateName");
		String stateStatus = request.getParameter("stateStatus");

		// String user = (String)session.getAttribute("CurrentUser");
		com.magbel.admin.objects.State state = new com.magbel.admin.objects.State();

		state.setStateCode(stateCode);
		state.setStateName(stateName);
		state.setStateStatus(stateStatus);
		state.setUserId(userId);

		com.magbel.admin.handlers.AdminHandler admin = new com.magbel.admin.handlers.AdminHandler();
                String roleid =admin.getPrivilegesRole("Manage States");
		try {

			if (buttSave != null) {
				if (stateId.equals("")) {
					if (admin.getStateByCode(stateCode) != null) {
						out
								.print("<script>alert('The state code already exists .');</script>");
						out.print("<script>history.go(-1);</script>");
					} else {

						if (admin.createState(state)) {
							out
									.print("<script>alert('Record saved successfully.');</script>");
							out
									.print("<script>window.location = 'DocumentHelp.jsp?np=statesSetup&stateId="
											+ admin.getStateByCode(stateCode)
													.getStateId() + "';</script>");
						}
					}

				}
				if (!stateId.equals("")) {
					state.setStateId(stateId);
					audit.select(1,
							"SELECT * FROM  AM_GB_STATES  WHERE state_Id = '"
									+ stateId + "'");
					updtst = admin.updateState(state);
					audit.select(2,
							"SELECT * FROM  AM_GB_STATES  WHERE state_Id = '"
									+ stateId + "'");
					audit.logAuditTrail("AM_GB_STATES ", branchcode, userID,
							roleid,"","");
					if (updtst == true) {
						// statusMessage = "Update on record is successfull";
						out
								.print("<script>alert('Update on record is successfull')</script>");
						out
								.print("<script>window.location = 'DocumentHelp.jsp?np=statesSetup&stateId="
										+ stateId + "';</script>");
						// out.print("<script>window.location =
						// 'manageStates.jsp?status=A'</script>");
					} else {
						// statusMessage = "No changes made on record";
						out
								.print("<script>alert('No changes made on record')</script>");
						out
								.print("<script>window.location = 'DocumentHelp.jsp?np=statesSetup&stateId="
										+ stateId + "';</script>");
					}
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
			// statusMessage = "Ensure unique record entry";
			out.print("<script>alert('Ensure unique record entry.')</script>");
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
