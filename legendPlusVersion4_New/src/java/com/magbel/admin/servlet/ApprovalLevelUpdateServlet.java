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
import com.magbel.admin.handlers.AdminHandler;
import audit.*;
import com.magbel.util.CheckIntegerityContraint;

public class ApprovalLevelUpdateServlet extends HttpServlet {
	public ApprovalLevelUpdateServlet() {
	}

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

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
                String name = request.getParameter("trans_type").trim();
		String contact = request.getParameter("mail_address");
		String level = request.getParameter("level");
		String date = request.getParameter("create_date");

		Approval_Level bran = new Approval_Level();
		bran.setCode(code);
		bran.setTrans_type(name);
		bran.setLevel(level);
		bran.setDate(date);

		 com.magbel.admin.objects.User user = null;
	   	 if(session.getAttribute("_user")!=null)
	   	 {
	   		 user =(com.magbel.admin.objects.User)session.getAttribute("_user");
	   	     userId=user.getUserId();
	   	     branch=user.getBranch();

	   	 }

		AdminHandler admin = new AdminHandler();
		String roleid =admin.getPrivilegesRole("Manage Approval Level List");


		 try {
			 /*
			 audit.select(1,
						"SELECT * FROM  Approval_Level_setup  WHERE Code = '"
								+ code + "'");
				String save = admin.UpdateApproval_Level(code, name, level);
				audit.select(2,
						"SELECT * FROM  Approval_Level_setup  WHERE Code = '"
								+ code + "'");
				 updtst = audit.logAuditTrail("Approval_Level_setup",
							branchcode, loginID, code);
						if( save.equalsIgnoreCase("Success_update"))
						{
						*/
                      audit.select(1,"SELECT * FROM  Approval_Level_setup  WHERE Code = '"+ code + "'");
				String save = admin.UpdateApproval_Level(code, name, level);
				audit.select(2,"SELECT * FROM  Approval_Level_setup  WHERE Code = '"+ code + "'");
				 updtst = audit.logAuditTrail("Approval_Level_setup",branchcode, loginID, roleid,"","");
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
		 catch(Exception ex)
		 {
				ex.printStackTrace();
			}






	}
}