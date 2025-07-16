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

import com.magbel.util.Cryptomanager;
import com.magbel.admin.handlers.SecurityHandler;

public class UserAuditServlet extends HttpServlet {
	public UserAuditServlet() {
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

		String password = new String();
		try {
			if(userId=="")
			{

				 password = cm.encrypt(security.Name(request.getParameter("userName"),request.getParameter("password")));


			}else
				password = cm.encrypt(request.getParameter("password"));
		} catch (Exception e) {
		}

		String buttSave = request.getParameter("buttSave");
		String userName = request.getParameter("userName");
		String fullname = request.getParameter("fullName");
		String legacyid = "";//request.getParameter("legacyId");
		String userclass = request.getParameter("userClass");
		String userorgan = request.getParameter("userOrgan");
		String passwords = password;
		String userBranch = request.getParameter("userBranch");
		String phoneNo = request.getParameter("phoneNo");
		String isSupervisor = "N";//request.getParameter("isSupervisor");
		String fleetAdmin = "N";//request.getParameter("fleetAdmin");
		String passwordMust = request.getParameter("passwordMust");
		String expiry = String.valueOf(passexpiry);
		String loginStatus = request.getParameter("loginStatus");
		String userStatus = request.getParameter("userStatus");
		String userid = (String) session.getAttribute("CurrentUser");
		String email = request.getParameter("email");
		String branchRestrict= request.getParameter("branchRestrict");
		branchRestrict = (branchRestrict == null || branchRestrict.equals(""))?"N":branchRestrict;
		String Technician= request.getParameter("Technician");
		Technician = (Technician == null || Technician.equals(""))?"N":Technician;
//		System.out.print("+++++ Technician+++++ "+Technician);
        int expiryDays = (request.getParameter("expiryDays")== null || request.getParameter("expiryDays")== "")?0:Integer.parseInt(request.getParameter("expiryDays"));
        String expiryDate = request.getParameter("expiryDate");

        String limit = request.getParameter("userLimit");
         String deptCode = request.getParameter("dept_Code"); 
System.out.println("<<<<<<<<deptCode>>>>>> "+deptCode);



		user.setUserName(userName);
		user.setUserFullName(fullname);
		user.setLegacySystemId(legacyid);
		user.setUserClass(userclass);
		user.setorganization(userorgan);
		user.setPassword(passwords);
		user.setBranch(userBranch);
		user.setPhoneNo(phoneNo);
		user.setIsSupervisor(isSupervisor);
		user.setFleetAdmin(fleetAdmin);
		user.setMustChangePwd(passwordMust);
		user.setPwdExpiry(expiry);
		user.setLoginStatus(loginStatus);
		user.setUserStatus(userStatus);
		user.setCreatedBy(userid);
		user.setEmail(email);
		user.setorganization(userorgan);
        user.setBranchRestrict(branchRestrict);
        user.setTechnician(Technician);
         
        user.setExpiryDays(expiryDays);
        user.setExpiryDate(expiryDate);
		user.setdeptCode(deptCode);
		try {

			if (request.getParameter("password").length() >= min) {
				if (userId.equals("")) {
					if (security.getUserByUserName(userName) == null) {
						//if (security.createUser(user)) {            before limit introduction
							if (security.createManageUser2(user,limit)) {
							out.print("<script>alert('Record saved successfully.')</script>");
							out.print("<script>window.location = 'DocumentHelp.jsp?np=manageUsers&status=ACTIVE'</script>");
 
						
						} else {
							System.out.println("Error saving record: New record \nfor 'user'  with user name  "
											+ userName
											+ " could not be created");
							out.print("<script>window.history.back()</script>");
						}
					} else {
						out.print("<script>alert('Username ["
								+ request.getParameter("userName").trim()
								+ "] Exists Already.')</script>");
						out.print("<script>window.history.back()</script>");

					}
				} else if (!userId.equals("")) {
					user.setUserId(userId);
					audit.select(1,
							"SELECT * FROM  AM_GB_USER  WHERE user_Id = '"
									+ userId + "'");
					//boolean isupdt = security.updateUser(user);   before limit introduction
					boolean isupdt = security.updateManageUser2(user,limit);
					audit.select(2,
							"SELECT * FROM  AM_GB_USER  WHERE user_Id = '"
									+ userId + "'");
					//updtst = audit.logAuditTrailActionPerformed("AM_GB_USER ", branchcode,
					updtst = audit.logAuditTrail("AM_GB_USER ", branchcode,loginID, userId, "Update user profile","","");
					if (updtst == true) {

						out.print("<script>alert('Update on record is successfull')</script>");
						out.print("<script>window.location = 'DocumentHelp.jsp?np=systemUsers&userId="
										+ userId + "&PC=11'</script>");

					} else {
					out.print("<script>alert('No changes made on record')</script>");
						out.print("<script>window.location = 'DocumentHelp.jsp?np=systemUsers&userId="
										+ userId + "&PC=11'</script>");
					}
				}
			} else {
				out.print("<script>alert('Minimum user password length is "
						+ min + "')</script>");
				out.print("<script>window.history.back()</script>");
			}

		} catch (Throwable e) {
			e.printStackTrace();

			out.print("<script>alert('Ensure unique record entry.')</script>");
			System.err.print(e.getMessage());
			out.print("<script>window.location = 'DocumentHelp.jsp?np=systemUsers&userId="
					+ userId + "&PC=11'</script>");
		}
	}
}
