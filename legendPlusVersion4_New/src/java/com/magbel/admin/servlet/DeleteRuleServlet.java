package com.magbel.admin.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.magbel.admin.objects.lost_password;
import com.magbel.admin.objects.mail_setup;
import com.magbel.admin.handlers.AdminHandler;
import com.magbel.admin.handlers.ApprovalRecords;
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
public class DeleteRuleServlet extends HttpServlet {
	public DeleteRuleServlet() {
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
		
		 String userId="";
                 String branch = "";

		

		String slaId = request.getParameter("slaId");
		String rule_name = request.getParameter("rule_name");
		System.out.println("-----slaId ---- "+slaId); 
		System.out.println("-----rule_name ---- "+rule_name);
		
		AdminHandler admin = new AdminHandler();
		
		 com.magbel.admin.objects.User user = null;

		 try {		
			  			admin.deleteRule(slaId);
				//			out.print("<script>alert('We shall get back to you for the new Password.');</script>");
							out.print("<script>window.location = 'DocumentHelp.jsp?np=slajsp'</script>");
										 	 			
			}
		 catch(Exception ex)
		 {
				ex.printStackTrace();
			}
			
			
		
		
	
	
	}
}				