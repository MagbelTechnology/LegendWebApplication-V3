package legend;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import audit.AuditTrailGen;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.util.Cryptomanager; 

import legend.admin.handlers.SecurityHandler_07_11_2024; 
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
 * @author Rahman Oloritun
 * @version 1.0
 */

public class UserAuditServletVersion3 extends HttpServlet {
	public UserAuditServletVersion3() {
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
  
		legend.admin.objects.User user = new legend.admin.objects.User();
		legend.admin.handlers.SecurityHandler_07_11_2024 security = new legend.admin.handlers.SecurityHandler_07_11_2024();
		legend.admin.handlers.CompanyHandler company = new legend.admin.handlers.CompanyHandler();

		int min = company.getCompany().getMinimumPassword();
		int passexpiry = company.getCompany().getPasswordExpiry();

		AuditTrailGen audit = new AuditTrailGen();

		ApprovalRecords aprecords = new ApprovalRecords();
		
		int loginID;
		String loginId = (String) session.getAttribute("CurrentUser");
		String userClass = (String) session.getAttribute("UserClass");
		String password0 = request.getParameter("password");
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
		System.out.println("<<<<<<=====userId: "+userId+"    userClass: "+userClass);
		
		String password = new String();
		 String [] passwordDisplay = null;
		try {
			if(userId.equals(""))
			{
				//new password format 
                passwordDisplay =security.PasswordChange(request.getParameter("userName").toLowerCase(),request.getParameter("userName").toLowerCase());
                
                password   = cm.encrypt(passwordDisplay[0]);
                password0 = passwordDisplay[1];
//                System.out.println("password ============================ "+password+"  password0: "+password0);
			}//else
			//	password = cm.encrypt(request.getParameter("password"));
		} catch (Exception e) {
		}
		boolean tokenRequired =false;
		String buttSave = request.getParameter("buttSave");
		// String buttAssg = request.getParameter("buttAssg");
        String RowId = request.getParameter("userId");
        String RecordId = request.getParameter("userName");
		String userName = request.getParameter("userName");
		String fullname = request.getParameter("fullName");
		String legacyid = request.getParameter("legacyId");
		String userclass = request.getParameter("userClass");
		String passwords = password;
		String userBranch = request.getParameter("userBranch");
		String phoneNo = request.getParameter("phoneNo");
		String isSupervisor = request.getParameter("isSupervisor");
		String fleetAdmin = request.getParameter("fleetAdmin");
		String passwordMust = request.getParameter("passwordMust");
		String expiry = String.valueOf(passexpiry);
		String loginStatus = request.getParameter("loginStatus");
		String userStatus = request.getParameter("userStatus");
		String userid = (String) session.getAttribute("CurrentUser");
		String email = request.getParameter("email");
		String branchRestrict= request.getParameter("branchRestrict");
		String tokenRequire= request.getParameter("tokenrequire");
		if(tokenRequire==null){tokenRequire="N";}
		String expiry_Days = request.getParameter("expiryDays");
		if((expiry_Days.equals("")) || (expiry_Days.equals(null))){expiry_Days = "0";}
//		System.out.println("<<<<<<=====expiry_Days: "+expiry_Days);
//        int expiryDays = (request.getParameter("expiryDays")== null || request.getParameter("expiryDays")== "")?0:Integer.parseInt(request.getParameter("expiryDays"));
		int expiryDays = Integer.parseInt(expiry_Days);
//		System.out.println("<<<<<<=====expiryDays: "+expiryDays);
        String expiryDate = request.getParameter("expiryDate");

        String limit = request.getParameter("userLimit");
         String deptCode = request.getParameter("deptCode");
         String approveLevel = request.getParameter("approveLevel");
         String isStorekeeper= request.getParameter("is_StoreKeeper");
         String isStockAdministrator = request.getParameter("is_StockAdministrator"); 
         String buRestrict= request.getParameter("buRestrict");
         String undertaker= request.getParameter("undertaker");
         String regionCode= request.getParameter("region");
         String zoneCode= request.getParameter("zone");
         String regionRestrict= request.getParameter("regionRestrict");
         String zoneRestrict= request.getParameter("zoneRestrict");
         String isFacilityAdministrator= request.getParameter("is_FacilityAdministrator");
         String isStoreAdministrator= request.getParameter("is_StoreAdministrator");
         String existUser = aprecords.getCodeName("select USER_ID from am_gb_User where USER_NAME='"+userName+"'");
//		System.out.println("<<<<<<<<=======regionCode: "+regionCode+"   zoneCode: "+zoneCode);
		user.setUserName(userName);
		user.setUserFullName(fullname);
		user.setLegacySystemId(legacyid);
		user.setUserClass(userclass);
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

        user.setBranchRestrict(branchRestrict);
        user.setTokenRequire(tokenRequire);
        user.setExpiryDays(expiryDays);
        user.setExpiryDate(expiryDate);
		user.setDeptCode(deptCode);
		user.setApproveLevel(approveLevel);
		user.setIsStorekeeper(isStorekeeper);
        user.setIsStockAdministrator(isStockAdministrator);
        user.setDeptRestrict(buRestrict);
        user.setUnderTaker(undertaker);
        user.setRegionCode(regionCode);
        user.setZoneCode(zoneCode);
        user.setRegionRestrict(regionRestrict);
        user.setZoneRestrict(zoneRestrict);
        user.setIsFacilityAdministrator(isFacilityAdministrator);
        user.setIsStoreAdministrator(isStoreAdministrator);
//		System.out.println("UserAuditServlet tokenRequired: "+tokenRequired);
		try {
			if (!userClass.equals("NULL") || userClass!=null){
//			min = 2;
			System.out.println("password0 ============================ "+password0+"  Min: "+min);
			if (password0.length() >= min || password0 =="") {
				if (userId.equals("")) {
					if ((security.getUserByUserName(userName) == null) && (existUser.equals(""))){
						//if (security.createUser(user)) {            before limit introduction
							if (security.createManageUser2(user,limit)) {
							if (!userClass.equals("NULL") || userClass!=null){
							out.print("<script>alert('Record saved successfully.')</script>");
                            EmailSmsServiceBus mail = new EmailSmsServiceBus();
                            String message="Your password on legend application :"+passwordDisplay[1];
                            System.out.println("---------------------------------------------------------------"+message);
                            String newPassword = passwordDisplay[1];
                            System.out.println("My  password on legend application : "+newPassword);

                            mail.sendMail(email, "Password on Legend User", message);
							out.print("<script>window.location = 'DocumentHelp.jsp?np=manageUsers&status=ACTIVE'</script>");
                            
                            /* //the former 
                            .print("<script>window.location = 'DocumentHelp.jsp?np=systemUsers&userId="
											+ security.getUserByUserName(
													userName).getUserId()
											+ "&PC=11'</script>");
                            
                            */

							}else {
								out
								.print("<script>alert('You have No Right')</script>");
								out
								.print("<script>window.location = 'DocumentHelp.jsp?np=systemUsers&userId="
										+ userId + "&PC=11'</script>");
							}
								
						} else {
							System.out
									.println("Error saving record: New record \nfor 'user'  with user name  "
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
					System.out.println("<<<<<<userClass: "+userClass+"    userId ============================ "+userId);
					if (!userClass.equals("NULL") || userClass!=null){
					
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
					updtst = audit.logAuditTrail("AM_GB_USER ", branchcode,loginID, RowId, RecordId,"Update user profile");
					if (updtst == true) {
						// statusMessage = "Update on record is
						// successfull";
						out
								.print("<script>alert('Update on record is successful')</script>");
						out
								.print("<script>window.location = 'DocumentHelp.jsp?np=systemUsers&userId="
										+ userId + "&PC=11'</script>");

					} else {
						// statusMessage = "No changes made on record";
						out
								.print("<script>alert('No changes made on record')</script>");
						out
								.print("<script>window.location = 'DocumentHelp.jsp?np=systemUsers&userId="
										+ userId + "&PC=11'</script>");
					}
				}else {
						out
						.print("<script>alert('You have No Right')</script>");
						out
						.print("<script>window.location = 'DocumentHelp.jsp?np=systemUsers&userId="
								+ userId + "&PC=11'</script>");
					}
				}
			} else {
				out.print("<script>alert('Minimum user password length is "
						+ min + "')</script>");
				out.print("<script>window.history.back()</script>");
			}
		}else {
			out.print("<script>alert('Session Time Out')</script>");
			out.print("<script>window.location = 'DocumentHelp.jsp?np=systemUsers&userId="
					+ userId + "&PC=11'</script>");
		} 
		
		} catch (Throwable e) {
			e.printStackTrace();
			// statusMessage = "Ensure unique record entry";
			out.print("<script>alert('Ensure unique record entry. Or Session Time Out')</script>");
			System.err.print(e.getMessage());
			out.print("<script>window.location = 'DocumentHelp.jsp?np=systemUsers&userId="
					+ userId + "&PC=11'</script>");
		}
	}
}
