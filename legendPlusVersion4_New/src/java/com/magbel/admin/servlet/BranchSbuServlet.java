/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.magbel.admin.servlet;

import java.io.IOException;
import java.io.PrintWriter;
 
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.magbel.admin.objects.Sbu_branch;
import com.magbel.admin.handlers.AdminHandler;

import audit.AuditTrailGen;

import com.magbel.util.CheckIntegerityContraint;

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
public class BranchSbuServlet extends HttpServlet {
	public BranchSbuServlet() {
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
			throws ServletException, IOException{

		response.setContentType("text/html");
		response.setDateHeader("Expires", -1);

		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();



		String code = request.getParameter("sbn_code");
		String name = request.getParameter("sbn_name");
		String contact = request.getParameter("sbn_contact");
		String status = request.getParameter("active");
		String mail = request.getParameter("sbn_mail");
		//String operation = request.getParameter("operation");

	    Sbu_branch bran = new Sbu_branch();
		bran.setSbucode(code);
		bran.setSbuname(name);
		bran.setSbucontact(contact);
		bran.setSbustatus(status);

		AdminHandler admin = new AdminHandler();

		//String message = admin.iscreatesave2(code,name,contact,status,mail);

		 //admin.CheckIfExist(code,name,contact,mail, status);
		//boolean message =



		 try {

						if(admin.iscreatesave2(code))
						{
							out.print("<script>alert('Record already exists ');</script>");
							out.print("<script>window.location = 'DocumentHelp.jsp?np=SbumanageBranches&status=ACTIVE'</script>");

						}
						else{


							admin.SaveSbuSetup(code,name,contact,status,mail);
							out.print("<script>alert('Record saved successfully.');</script>");
							out.print("<script>window.location = 'DocumentHelp.jsp?np=sbuSetup'</script>");

						}



			}
		 catch(Exception ex)
		 {
				ex.printStackTrace();
			}






	}
}