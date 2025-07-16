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
import com.magbel.admin.objects.Approval_Level;
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
public class ApprovalLevelServlet extends HttpServlet {
	public ApprovalLevelServlet() {
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
		String userId ="";
		String branch="";


		String code = request.getParameter("code");
		String name = request.getParameter("trans_type");
		String contact = request.getParameter("level");
		String date = request.getParameter("create_date");
		String userid = request.getParameter("user_id");

		Approval_Level approve = new Approval_Level();
		approve.setCode(code);
		approve.setTrans_type(name);
		approve.setLevel(contact);
		approve.setDate(date);
		approve.setUserid(userid);

		 com.magbel.admin.objects.User user = null;
	   	 if(session.getAttribute("_user")!=null)
	   	 {
	   		 user =(com.magbel.admin.objects.User)session.getAttribute("_user");
	   	     userId=user.getUserId();
	   	     branch=user.getBranch();

	   	 }


	   	 AdminHandler admin = new AdminHandler();

		 try {

						if(admin.isApproval_Level_Existing(code))
						{
							out.print("<script>alert('Record already exists');</script>");
							out.print("<script>window.location = 'DocumentHelp.jsp?np=ApprovalLevelView&status=ACTIVE'</script>");

						}
						else{


							admin.SaveApproval_level(code, name, contact, date,userId);
							out.print("<script>alert('Record saved successfully.');</script>");
							out.print("<script>window.location = 'DocumentHelp.jsp?np=aprovalLevel'</script>");

						}



			}
		 catch(Exception ex)
		 {
				ex.printStackTrace();
			}






	}
}