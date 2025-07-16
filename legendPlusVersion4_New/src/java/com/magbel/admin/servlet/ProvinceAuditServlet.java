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


public class ProvinceAuditServlet extends HttpServlet {
	public ProvinceAuditServlet() {
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

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		response.setDateHeader("Expires", -1);

		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();

		AuditTrailGen audit = new AuditTrailGen();

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

		String buttSave = request.getParameter("buttSave");

		String provinceId = request.getParameter("provinceId");
		String provinceCode = request.getParameter("provinceCode");
		String province = request.getParameter("provinceName");
		String status = request.getParameter("provinceStatus");
		String userId = (String) session.getAttribute("CurrentUser");

		com.magbel.admin.objects.Province prov = new com.magbel.admin.objects.Province();
		prov.setProvince(province);
		prov.setProvinceCode(provinceCode);
		prov.setStatus(status);
		prov.setUserId(userId);
com.magbel.admin.handlers.AdminHandler admin = new com.magbel.admin.handlers.AdminHandler();
		try {

			if (buttSave != null) {
				if (provinceId.equals("")) {
					if (admin.getProvinceByCode(provinceCode) != null) {
						out.print("<script>alert('The Province code already exists .');</script>");
						out.print("<script>history.go(-1);</script>");
					} else {

						if (admin.createProvince(prov)) {
							out.print("<script>alert('Record saved successfully.');</script>");
							out.print("<script>window.location = 'DocumentHelp.jsp?np=provinceSetup&provinceId="
											+ admin.getProvinceByCode(provinceCode).getProvinceId()
											+ "&PC=60';</script>");
						}
					}
				}
				if (!provinceId.equals("")) {
					prov.setProvinceId(provinceId);
					audit.select(1,
							"SELECT * FROM  AM_GB_PROVINCE   WHERE province_Id = '"
									+ provinceId + "'");
					boolean isupdt = admin.updateProvince(prov);
					audit.select(2,
							"SELECT * FROM  AM_GB_PROVINCE  WHERE province_Id = '"
									+ provinceId + "'");
					updtst = audit.logAuditTrail("AM_GB_PROVINCE", branchcode,
							loginID, provinceId,"","");
					if (updtst == true) {

						out
								.print("<script>alert('Update on record is successfull')</script>");
						out
								.print("<script>window.location = 'DocumentHelp.jsp?np=provinceSetup&provinceId="
										+ provinceId + "&PC=60'</script>");

					} else {

						out
								.print("<script>alert('No changes made on record')</script>");
						out
								.print("<script>window.location = 'DocumentHelp.jsp?np=provinceSetup&provinceId="
										+ provinceId + "&PC=60'</script>");
					}
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
			// statusMessage = "Ensure unique record entry";
			out.print("<script>alert('Ensure unique record entry.')</script>");
			out.print("<script>history.go(-1);</script>");
			System.err.print(e.getMessage());
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
