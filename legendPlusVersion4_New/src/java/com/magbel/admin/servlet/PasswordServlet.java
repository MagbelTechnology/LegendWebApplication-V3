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

import audit.AuditTrailGen;

import com.magbel.admin.mail.EmailSmsServiceBus;
import com.magbel.util.Cryptomanager;
import java.util.Enumeration;
import com.magbel.admin.dao.MagmaDBConnection;
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
 * Copyright: Copyright (c) 2009
 * </p>
 *
 * <p>
 * Company:
 * </p>
 *
 * @author Ganiyu Shefiu
 * @version 1.0
 */

public class PasswordServlet extends HttpServlet {
    com.magbel.admin.mail.EmailSmsServiceBus mails = null;
	public PasswordServlet() {
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

		String statusMessage = "";
		boolean updtst = false;

		com.magbel.admin.objects.User user = new com.magbel.admin.objects.User();
		com.magbel.admin.handlers.SecurityHandler security = new com.magbel.admin.handlers.SecurityHandler();
		com.magbel.admin.handlers.CompanyHandler company = new com.magbel.admin.handlers.CompanyHandler();
                com.magbel.admin.handlers.AdminHandler admin = new com.magbel.admin.handlers.AdminHandler();
		int min = company.getCompany().getMinimumPassword();
		int passexpiry = company.getCompany().getPasswordExpiry();

		AuditTrailGen audit = new AuditTrailGen();

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

		Cryptomanager cm = new Cryptomanager();

		String userId = request.getParameter("userId");

                String userEmailId= request.getParameter("userEmailId");
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+userEmailId);

                String password = new String();
                 String [] passwordDisplay = null;
		try {
			//password = cm.encrypt(request.getParameter("userName").toLowerCase());
                         System.out.println("userName  = "+request.getParameter("userName").toLowerCase());
          //*changrd on 28-6-10               password = cm.encrypt(security.Name(request.getParameter("userName").toLowerCase(),request.getParameter("userName").toLowerCase()));
                passwordDisplay =security.PasswordChange(request.getParameter("userName").toLowerCase(),request.getParameter("userName").toLowerCase());
                System.out.println(">>>>>>>>>>>>>>>>>>>>passwordDisplay<<<<<<<"+passwordDisplay);
                     password   = cm.encrypt(passwordDisplay[0]);
                        System.out.println("password ============================ "+password);
		} catch (Exception e) { 
		}
 

		String buttSave = request.getParameter("buttSave");
		// String buttAssg = request.getParameter("buttAssg");

		String userName = request.getParameter("userName");

        System.out.println("the value of user name is ============= "+request.getParameter("userName") );
        System.out.println("the value of encrypted password is ============= "+password );
        System.out.println("the value of userid is ============= "+userId );



		user.setPassword(password);

		//user.setPwdChanged("Y");
        user.setMustChangePwd("Y");
		user.setLoginStatus("0");
		System.out.println("====== Before roleid === ");
                String roleid =admin.getPrivilegesRole("Reset Password");
System.out.println("======roleid === "+roleid);

		try {

				 if (!userId.equals(""))
                                 {
					user.setUserId(userId);
					audit.select(1, "SELECT * FROM  AM_GB_USER  WHERE user_Id = '" + userId + "'");
System.out.println("=====After audit.select 1 ====");
                                        boolean isupdt = security.updateManageUser(user);
					audit.select(2, "SELECT * FROM  AM_GB_USER  WHERE user_Id = '" + userId + "'");
					System.out.println("=====After audit.select 2 ====");
                                        updtst = audit.logAuditTrail("AM_GB_USER ", branchcode, loginID, roleid,"","");
                                        System.out.println("=====After audit.select 3 ====");
                       if (isupdt == true) {
                               EmailSmsServiceBus mail = new EmailSmsServiceBus();
                               String message="Your new password on Oriental HelpDesk Solution: "+passwordDisplay[1];
                               System.out.println("---------------------------------------------------------------"+message);
                        String url = getServletConfig().getServletContext().getRealPath("");
						mail.sendMail(userEmailId, "Password reset on Oriental HelpDesk Solution", message,url,userId);
						out.print("<script>alert('Password reset successfull')</script>");
						out.print("<script>window.location = 'DocumentHelp.jsp?np=manageUserPassword&status=ACTIVE&amp;select=ACTIVE'</script>");

					}
                                        else {

						out.print("<script>alert('Password reset not successful')</script>");
						out.print("<script>window.location = 'DocumentHelp.jsp?np=manageUserPassword&status=ACTIVE&amp;select=ACTIVE'</script>");
					}
				}


		} catch (Throwable e) {
			e.printStackTrace();

			out.print("<script>alert('Error occured during password reset.')</script>");
			System.err.print(e.getMessage());
			out.print("<script>window.location = 'DocumentHelp.jsp?np=manageUserPassword&status=ACTIVE&amp;select=ACTIVE'</script>");
		}
	}
}
