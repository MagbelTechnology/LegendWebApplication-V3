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

import com.magbel.admin.objects.mail_setup;
import com.magbel.admin.objects.Approval_Level;
import com.magbel.admin.objects.Aproval_limit;
import com.magbel.admin.handlers.AdminHandler;
import audit.*;

import com.magbel.util.CheckIntegerityContraint;

public class ApprovalServletLimitUpdate extends HttpServlet {
	public ApprovalServletLimitUpdate() {
	}

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



		String code = request.getParameter("code");

		String name = request.getParameter("min_amount");
		String contact = request.getParameter("max_amount");
		String desc = request.getParameter("description");

		String min = name.replace(",","");
		String max = contact.replace(",","");

		double min_amount = Double.parseDouble(min);
		double max_amount = Double.parseDouble(max);

		Aproval_limit bran = new Aproval_limit();
		bran.setCode(code);
		bran.setMinAmt(min_amount);
		bran.setMaxAmt(max_amount);
		bran.setDesc(desc);

		 com.magbel.admin.objects.User user = null;
	   	 if(session.getAttribute("_user")!=null)
	   	 {
	   		 user =(com.magbel.admin.objects.User)session.getAttribute("_user");
	   	     userId=user.getUserId();
	   	     branch=user.getBranch();

	   	 }

		AdminHandler admin = new AdminHandler();
		String roleid =admin.getPrivilegesRole("Manage Approval Limit");


		 try {

			 audit.select(1,
						"SELECT * FROM  Approval_Limit  WHERE Level_Code = '"
								+ code + "'");
			 String save = admin.UpdateApproval_Limit(code,min_amount,max_amount,desc);
			 audit.select(2,
						"SELECT * FROM  Approval_Limit  WHERE Level_Code = '"
								+ code + "'");
			 updtst = audit.logAuditTrail("Approval_Limit",
						branchcode, loginID, roleid,"","");

						if( save.equalsIgnoreCase("Success_update"))
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
		 catch(Exception ex)
		 {
				ex.printStackTrace();
			}






	}
}