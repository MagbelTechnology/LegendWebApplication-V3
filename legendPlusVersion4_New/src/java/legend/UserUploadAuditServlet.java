package legend;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import audit.AuditTrailGen;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.util.CryptManager;
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

public class UserUploadAuditServlet extends HttpServlet {
	public UserUploadAuditServlet() {
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
		 
//		legend.admin.objects.User user = new legend.admin.objects.User();
		legend.admin.handlers.SecurityHandler security = new legend.admin.handlers.SecurityHandler();
		legend.admin.handlers.CompanyHandler company = new legend.admin.handlers.CompanyHandler();

		int min = company.getCompany().getMinimumPassword();
		int passexpiry = company.getCompany().getPasswordExpiry();

		AuditTrailGen audit = new AuditTrailGen();

		ApprovalRecords aprecords = new ApprovalRecords();
		String userClass = (String)request.getSession().getAttribute("UserClass");
		int loginID;
		String loginId = (String) session.getAttribute("CurrentUser");
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

		CryptManager cm = new CryptManager();
		String userId = loginId;

		String password = new String();
		 String [] passwordDisplay = null;
	/*
		 try {
			if(userId=="")
			{

			}//else
			//	password = cm.encrypt(request.getParameter("password"));
		} catch (Exception e) {
		}
		*/
		 if (!userClass.equals("NULL") || userClass!=null){
		boolean tokenRequired =false;
		String ColQuery ="select *from am_gb_User_Upload where USER_NAME not in (select USER_NAME from am_gb_User)";
		 Report rep = new Report();
		  java.util.ArrayList list =rep.getUploadUserSqlRecords(ColQuery,userId);
//			try {		

//				  System.out.println("list.size() ============================ "+list.size());
				  int listValue = list.size();
				for(int k=0;k<listValue;k++)
		     {
			System.out.println("K ============== "+k+"   list.size() =========== "+list.size());	
			legend.admin.objects.User  usr = (legend.admin.objects.User)list.get(k);    	 					
//			String buttSave = request.getParameter("buttSave");
			// String buttAssg = request.getParameter("buttAssg");
//	       String RowId = request.getParameter("userId");
//	       String RecordId = usr.getUserName();
			String userName = usr.getUserName();
			String fullname = usr.getUserFullName();
			String legacyid = usr.getLegacySystemId();
			String userclass = usr.getUserClass();
			String enterpasswords = usr.getPassword();
			String userBranch = usr.getBranch();
			String phoneNo = usr.getPhoneNo();
			String isSupervisor = usr.getIsSupervisor();
			String fleetAdmin = usr.getFleetAdmin();
			String passwordMust = usr.getMustChangePwd();
			String expiry = usr.getExpiryDate();
			String loginStatus = usr.getLoginStatus();
			String userStatus = usr.getUserStatus();
			String userid = userId;
			String email = usr.getEmail();
			String branchRestrict= usr.getBranchRestrict();
//			String tokenRequire= request.getParameter("tokenrequire");
	       int expiryDays = usr.getExpiryDays(); 
//	       String expiryDate = request.getParameter("expiryDate");
	       String limit = usr.getApprvLimit();
	        String deptCode = usr.getDeptCode();
//	        String approveLevel = request.getParameter("approveLevel");
//	        String isStorekeeper= request.getParameter("is_StoreKeeper");
//	        String isStockAdministrator = request.getParameter("is_StockAdministrator"); 
//	        String buRestrict= request.getParameter("buRestrict");
//	        String undertaker= request.getParameter("undertaker");
	        String token_Required = usr.getTokenRequire();
	        String regionCode= usr.getRegionCode();
	        String zoneCode= usr.getZoneCode();
	        String regionRestriction = usr.getRegionRestrict();
	        String zoneRestriction = usr.getZoneRestrict();
	        String deptRestriction = usr.getDeptRestrict();
	        String facilityAdmin = usr.getIsFacilityAdministrator();
	        String storeAdmin = usr.getIsStoreAdministrator();
//	        String regionRestrict= request.getParameter("regionRestrict");
//	        String zoneRestrict= request.getParameter("zoneRestrict");
//	        String isFacilityAdministrator= request.getParameter("is_FacilityAdministrator");
//	        String isStoreAdministrator= request.getParameter("is_StoreAdministrator");
//	        String existUser = aprecords.getCodeName("select USER_ID from am_gb_User where USER_NAME='"+userName+"'");
//			System.out.println("<<<<<<<<=======regionCode: "+regionCode+"   zoneCode: "+zoneCode);
	        usr.setUserName(userName);
	        usr.setUserFullName(fullname);
	        usr.setLegacySystemId(legacyid);
			usr.setUserClass(userclass);
//			usr.setPassword(passwords);
			usr.setBranch(userBranch);
			usr.setPhoneNo(phoneNo);
			usr.setIsSupervisor(isSupervisor);
			usr.setFleetAdmin(fleetAdmin);
			usr.setMustChangePwd(passwordMust);
			usr.setPwdExpiry(expiry);
			usr.setLoginStatus(loginStatus);
			usr.setUserStatus(userStatus);
			usr.setCreatedBy(userid);
			usr.setEmail(email);
			usr.setBranchRestrict(branchRestrict);
			usr.setTokenRequire(token_Required);
//	       user.setExpiryDays(expiryDays);
//	       user.setExpiryDate(expiryDate);
			usr.setDeptCode(deptCode);
//			user.setApproveLevel(approveLevel);
//			user.setIsStorekeeper(isStorekeeper);
//	       usr.setIsStockAdministrator(isStockAdministrator);
//	       usr.setDeptRestrict(buRestrict);
//	       user.setUnderTaker(undertaker);
			usr.setDeptRestrict(deptRestriction);
			usr.setRegionCode(regionCode);
			usr.setZoneCode(zoneCode);		
	       usr.setRegionRestrict(regionRestriction);
	       usr.setZoneRestrict(zoneRestriction);
	       usr.setIsFacilityAdministrator(facilityAdmin);
	       usr.setIsStoreAdministrator(storeAdmin);
			
			list.add(usr);  
			
//			System.out.println("UserAuditServlet tokenRequired: "+tokenRequired);
			
			
			
			//new password format 
	       /*
           passwordDisplay =security.PasswordChange(userName.toLowerCase(),userName.toLowerCase());
           
           password   = cm.encrypt(passwordDisplay[0]);
           password0 = passwordDisplay[1];
           */
            passwordDisplay =security.PasswordChange(userName.toLowerCase(),userName.toLowerCase());
            
            try {
				password   = cm.encrypt(passwordDisplay[0]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            password0 = passwordDisplay[1];

 //          String newpassword = cm.encrypt(password);
//			try{
	       limit = "5";
	       
           System.out.println("password ============================ "+password+"  userName: "+userName+"  enterpasswords: "+enterpasswords);
								if (security.createManageUserfromUpload(userName,fullname,legacyid,userclass,enterpasswords,password,email,
										userBranch,phoneNo,isSupervisor,fleetAdmin,passwordMust,expiry,loginStatus,userStatus,userid,branchRestrict,
										deptCode,regionCode,zoneCode,limit,token_Required,regionRestriction,zoneRestriction)) {
//								out.print("<script>alert('Record saved successfully.')</script>");
	                           EmailSmsServiceBus mail = new EmailSmsServiceBus();
	                           String message="Your password on legend application :"+passwordDisplay[1];
	                           System.out.println("---------------------------------------------------------------"+message);
	                           String newPassword = passwordDisplay[1];
	                           System.out.println("My  password on legend application : "+newPassword);

	                           mail.sendMail(email, "Password on Legend User", message);
								

							} 
								 System.out.println("K LISt ============= "+k+"  listValue: "+listValue);
	//			} catch (Exception e) {
	//			}
			//					 System.out.println("=======>>>>K: "+k);
		     }
				out.print("<script>alert('Upload Done Successfully!!!')</script>");
				out.print("<script>window.location = 'DocumentHelp.jsp?np=manageUsers&status=ACTIVE'</script>");	
		/*	}  catch (Throwable e) {
				e.printStackTrace();
				// statusMessage = "Ensure unique record entry";
				out.print("<script>alert('Ensure unique record entry.')</script>");
				System.err.print(e.getMessage());
				out.print("<script>window.location = 'DocumentHelp.jsp?np=systemUsers&userId="
						+ userId + "&PC=11'</script>");
			}	*/	 
	}
	}
	}
