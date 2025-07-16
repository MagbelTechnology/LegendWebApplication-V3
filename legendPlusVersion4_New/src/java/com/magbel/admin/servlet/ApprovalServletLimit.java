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
import com.magbel.admin.objects.Aproval_limit;
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
public class ApprovalServletLimit extends HttpServlet {
	public ApprovalServletLimit() {
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
			throws ServletException, IOException{

		response.setContentType("text/html");
		response.setDateHeader("Expires", -1);

		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();

		String code = request.getParameter("code");
		String name = request.getParameter("min_amount");
		String contact = request.getParameter("max_amount");
		String desc = request.getParameter("description");
		String amount_min = name.replace("," , "");
		String amount_max= contact.replace("," , "");

		//int convert = Integer.parseInt(code);
		double min = Double.parseDouble(amount_min);
		double max = Double.parseDouble(amount_max);


	    Aproval_limit approve = new Aproval_limit();
		approve.setCode(code);
		approve.setMinAmt(min);
		approve.setMaxAmt(max );
		approve.setDesc(desc);

		AdminHandler admin = new AdminHandler();

		String message = admin.Approval_Duplicate(code,min,max,desc);

		 try {

						if(admin.isApprovalExisting(code))
						{
							out.print("<script>alert('Record already exists');</script>");
							out.print("<script>window.location = 'DocumentHelp.jsp?np=ApprovalListView&status=ACTIVE'</script>");

						}
						else{


							admin.SaveApproval(code, min, max, desc);
							out.print("<script>alert('Record saved successfully.');</script>");
							out.print("<script>window.location = 'DocumentHelp.jsp?np=ApprovalListView&status=ACTIVE'</script>");

						}



			}
		 catch(Exception ex)
		 {
				ex.printStackTrace();
			}

	}
}