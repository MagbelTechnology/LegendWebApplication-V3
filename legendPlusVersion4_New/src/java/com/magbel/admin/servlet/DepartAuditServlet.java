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

import com.magbel.admin.objects.Department;
import com.magbel.admin.handlers.AdminHandler;
import com.magbel.admin.handlers.ApprovalRecords;

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

public class DepartAuditServlet extends HttpServlet {
    ApprovalRecords aprecords = null;
	public DepartAuditServlet() {
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
		   aprecords = new ApprovalRecords();
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

		Department dept = new Department();
		AdminHandler admin = new AdminHandler();
		String deptId = request.getParameter("deptId");

		String deptStatus = request.getParameter("deptStatus");

		String buttSave = request.getParameter("buttSave");

		String acronym = request.getParameter("deptAcronym");

		if (acronym != null) {
			acronym = acronym.toUpperCase();
		}

		String dept_code = request.getParameter("deptCode");

		String dept_name = request.getParameter("deptName");
 
		String dept_acronym = acronym;

		String dept_status = request.getParameter("deptStatus");
		String UnitHeadId = request.getParameter("UnitHead");
		String UnitMail = request.getParameter("UnitMail");
	//	System.out.println("====UnitHeadId===> "+UnitHeadId);
		
        String UserQuery = "SELECT Full_Name, User_Name FROM   AM_GB_USER WHERE User_id = "+Integer.parseInt(UnitHeadId)+"";
    //    System.out.println("====UserQuery1===> "+UserQuery);
        String UnitHead = aprecords.getCodeName(UserQuery);
        String UserMailQuery = "SELECT email, User_Name FROM   AM_GB_USER WHERE User_id = "+Integer.parseInt(UnitHeadId)+"";
    //    System.out.println("====UserQuery2===> "+UserQuery);
        String email = aprecords.getCodeName(UserMailQuery);
    //    System.out.println("====UserQuery3===> "+UserQuery);
		String user_id = (String) session.getAttribute("CurrentUser");
		dept.setDept_code(dept_code);
		dept.setDept_name(dept_name);   
		dept.setDept_acronym(dept_acronym);
		dept.setDept_status(dept_status); 
		dept.setUser_id(user_id);
		dept.setUnitHeadId(UnitHeadId);
		dept.setUnitHead(UnitHead);
		dept.setemail(email); 
		dept.setUnitMail(UnitMail);
		 System.out.print("Matanmi > "+UnitHead+" 1 "+"== UnitHeadId== "+UnitHeadId);
		try {

			if (buttSave != null) {

				if (deptId.equals("")) {
					if (admin.getDeptByDeptCode(dept_code) != null) {
						out.print("<script>alert('The Department code already exists .')</script>");
						out.print("<script>history.go(-1);</script>");
					} else {
 
						if (admin.createDepartment(dept)) {
							out.print("<script>alert('Record saved successfully.')</script>");
							out.print("<script>window.location = 'DocumentHelp.jsp?np=departSetup&deptId="
											+ admin.getDeptByDeptCode(dept_code).getDept_id()
											+ "&PC=9'</script>");
						}
				}
					}else if (!deptId.equals("")) {
						dept.setDept_id(deptId);
					CheckIntegerityContraint intCont = new CheckIntegerityContraint();
					if (intCont.checkReferenceConstraint("AM_GB_USERS",
							"DEPT_CODE", dept_code, deptStatus)) {
						out
								.print("<script>alert('Departmnet Code is being referenced,Integerity Constraint would be violated.')</script>");
						out
								.print("<script>window.location = 'DocumentHelp.jsp?np=departSetup&deptId="
										+ deptId + "&PC=9'</script>");
					} else { 
						// System.out.print(deptId+"3");
						audit.select(1,
								"SELECT * FROM  AM_AD_DEPARTMENT  WHERE dept_code = '"
										+ dept_code + "'");
						boolean isupdt = admin.updateDepartment(dept);  
						audit.select(2,
								"SELECT * FROM  AM_AD_DEPARTMENT  WHERE dept_Id = '"
										+ deptId + "'");
						updtst = audit.logAuditTrail("AM_AD_DEPARTMENT",
								branchcode, loginID, deptId,"","");
	//					System.out.print("===updtst=== "+updtst);
						if (updtst == true) {
							out
									.print("<script>alert('Update on record is successfull')</script>");
							out
									.print("<script>window.location = 'DocumentHelp.jsp?np=departSetup&deptId="
											+ deptId + "&PC=9'</script>");

						} else {

							out
									.print("<script>alert('No changes made on record')</script>");
							out
									.print("<script>window.location = 'DocumentHelp.jsp?np=departSetup&deptId="
											+ deptId + "&PC=9'</script>");
						}
					}
				}

			} else {
				// statusMessage = "Error saving! Select a branch.";
				out
						.print("<script>alert('Error saving! Select a branch.')</script>");
				out
						.print("<script>window.location = 'DocumentHelp.jsp?np=departSetup'</script>");
			}

		} catch (Throwable e) {
			e.printStackTrace();
			// statusMessage = "Ensure unique record entry.";
			out.print("<script>alert('Ensure unique record entry.')</script>");
			out.print("<script>window.location = 'DocumentHelp.jsp?np=departSetup&deptId="
					+ deptId + "&PC=9'</script>");
			System.err.print(e.getMessage());
		}
	}
}
