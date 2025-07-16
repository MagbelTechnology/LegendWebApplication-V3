package com.magbel.admin.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
//import legend.admin.objects.Sbu_branch;
import com.magbel.admin.objects.mail_setup;
import com.magbel.admin.handlers.AdminHandler;

import audit.*;

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
public class MailActionUpdateServlet extends HttpServlet {
	public MailActionUpdateServlet() {
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

		String userId="";
        String branch = "";

		AuditTrailGen  audit = new AuditTrailGen();
		boolean updtst = false;


        int loginID;
		 String loginId = (String)session.getAttribute("CurrentUser");
		 if(loginId == null) {  loginID = 0; }
			else { loginID = Integer.parseInt(loginId);	}

		String branchcode = (String)session.getAttribute("UserCenter");
		if(branchcode == null) { branchcode = "not set";	}


		String code = request.getParameter("mail_code");
		String mailheading = request.getParameter("mail_heading");
		String name = request.getParameter("mail_description");
		String contact = request.getParameter("mail_address");
		String date = request.getParameter("create_date");
		String trans= request.getParameter("transaction_type");
		String userid = request.getParameter("user_id");
		String active = request.getParameter("active");
		String category = request.getParameter("category");
		String subcategory = request.getParameter("subcategory");		
		String mailtype = request.getParameter("mail_type");
		String helptype = request.getParameter("help_type");
		mail_setup bran = new mail_setup();
		bran.setMailcode(name);
		bran.setmailheading(mailheading);
		bran.setMaildescription(name);
		bran.setMailaddress(contact);
		bran.setDate(date);
		bran.setTrans_type(trans);
		bran.setUserid(userid);
		bran.setStatus(active);
		bran.setcategory(category);
		bran.setmailtype(mailtype);
		bran.sethelptype(helptype);
		com.magbel.admin.objects.User user = null;
	   	 if(session.getAttribute("_user")!=null)
	   	 {
	   		 user =(com.magbel.admin.objects.User)session.getAttribute("_user");
	   	     userId=user.getUserId();
	   	     branch=user.getBranch();

	   	 }

		AdminHandler admin = new AdminHandler();


		 try {
 
			 audit.select(1,"SELECT * FROM  HD_mail_statement  WHERE mail_code = '"+ code + "'");
			 String save = admin.UpdateMailStatement(code,mailheading,name,contact,trans,active,category,mailtype,helptype,subcategory);			                                         
			 audit.select(2,
						"SELECT * FROM  HD_mail_statement  WHERE mail_code = '"+ code + "'");
			 updtst = audit.logAuditTrail("HD_mail_statement",
						branchcode, loginID, code,"","");
	 			if(save.equalsIgnoreCase("Success_update"))
	 			{
 
	 				if(updtst == true)
					{
						out.println("<script>alert('Records update Successfully')");
		        		out.println("history.go(-1)");
		        		out.println("</script>");

					}else{
						out.println("<script>alert('No changes made on Records')");
						out.println("history.go(-1)");
		        		out.println("</script>");
					}

     			}


			}
		 catch(Exception ex)
		 {
				ex.printStackTrace();
			}






	}
}				