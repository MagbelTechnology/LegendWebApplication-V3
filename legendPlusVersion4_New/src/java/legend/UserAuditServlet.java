package legend;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Arrays;
import java.util.Date;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import audit.AuditTrailGen;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.util.Cryptomanager; 

import legend.admin.handlers.SecurityHandler_07_11_2024; 
import magma.AssetRecordsBean;
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

public class UserAuditServlet extends HttpServlet {
	com.magbel.util.DatetimeFormat df;
	private AssetRecordsBean ad;
	public UserAuditServlet() {
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
		legend.admin.handlers.SecurityHandler security = new legend.admin.handlers.SecurityHandler();
		legend.admin.handlers.CompanyHandler company = new legend.admin.handlers.CompanyHandler();
		com.magbel.util.ApplicationHelper appHelper = new com.magbel.util.ApplicationHelper();
        String computerName = null;
        String remoteAddress = request.getRemoteAddr();
        InetAddress inetAddress = InetAddress.getByName(remoteAddress);
//        System.out.println("inetAddress: " + inetAddress);
        computerName = inetAddress.getHostName();
//        System.out.println("computerName: " + computerName);
        if (computerName.equalsIgnoreCase("localhost")) {
            computerName = java.net.InetAddress.getLocalHost().getCanonicalHostName();
        } 
       String hostName = "";
       
       if (hostName.equals(request.getRemoteAddr())) {
           InetAddress addr = InetAddress.getByName(request.getRemoteAddr());
           hostName = addr.getHostName();
           
	        }
	
	        if (InetAddress.getLocalHost().getHostAddress().equals(request.getRemoteAddr())) {
	                hostName = "Local Host";
	        }
	        
	        InetAddress ip;
			ip = InetAddress.getLocalHost();
			String ipAddress = ip.getHostAddress();
//			System.out.println("Current IP address : " + ip.getHostAddress());

			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
	        byte[] mac = network.getHardwareAddress();
	        if(mac == null){
	               String value = "";
	               mac = value.getBytes();
	        }
	        if(mac == null){
	               String value = "";
	               mac = value.getBytes();
	        }
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
			}
			String macAddress = sb.toString();
			System.out.println(sb.toString());
			
		int min = company.getCompany().getMinimumPassword();
		int passexpiry = company.getCompany().getPasswordExpiry();

		AuditTrailGen audit = new AuditTrailGen();

		ApprovalRecords aprecords = new ApprovalRecords();
		legend.admin.handlers.SecurityHandler sechanle = new legend.admin.handlers.SecurityHandler();
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
		legend.admin.objects.User usr = sechanle.getUserByUserID(loginId);
		 String user_Name = usr.getUserName();
		 String branchuser_NameRestrict = usr.getBranchRestrict();
		 String User_Restrict = usr.getDeptRestrict();
		 String departCode = usr.getDeptCode();
		 String branch = usr.getBranch();

		Cryptomanager cm = new Cryptomanager();
		String userId = request.getParameter("userId");
		System.out.println("<<<<<<=====userId: "+userId+"    userClass: "+userClass+"   loginId: "+loginId);
		
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
		String menuURL = request.getParameter("menuURL");
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
        String singleApproval = request.getParameter("singleApproval");
        
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
         String supervisor = request.getParameter("supervisor");
         String numOfTransactionLevel = request.getParameter("numOfTransactionLevel");
         String existUser = aprecords.getCodeName("select USER_ID from am_gb_User where USER_NAME='"+userName+"'");
         String existUserId = aprecords.getCodeName("select USER_ID from am_gb_User where USER_ID='"+RowId+"'");
         System.out.println("select role_uuid from am_ad_privileges where role_wurl LIKE '"+menuURL+"'");
         String menuId = aprecords.getCodeName("select role_uuid from am_ad_privileges where role_wurl LIKE '"+menuURL+"'");
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
			ad = new AssetRecordsBean();
			df = new com.magbel.util.DatetimeFormat();
//			System.out.println("<<<<<<<<=======branch: "+branch+"   departCode: "+departCode+"   user_Name: "+user_Name);
			java.util.ArrayList approvelist =ad.getApprovalsId(branch,departCode,user_Name);
			if (!userClass.equals("NULL") || userClass!=null){
//			min = 2;
//			System.out.println("password0 ============================ "+password0+"  Min: "+min+"   userId: "+userId+"    menuURL: "+menuURL);
			if (password0.length() >= min || password0 =="") {
//				if (userId.equals("")) {
				Date tranDate = new java.util.Date();
//				System.out.println("=====tranDate: "+tranDate);
				String transDate = df.formatDate(tranDate);
				 user.setCreateDate(transDate);
//				 System.out.println("=====security.getUserByUserName(userName): "+security.getUserByUserName(userName)+"     existUser: "+existUser+"   existUserId: "+existUserId+"   RowId: "+RowId);
					if ((security.getUserByUserName(userName) == null) && (existUser.equals(""))){
						//if (security.createUser(user)) {            before limit introduction
//							if (security.createManageUser2Tmp(user,limit)) {
						String recId = security.createManageUser2Tmp(user,limit,userId,menuId); 
//								System.out.println("=====recId: "+recId+" New Users");
			                    if(!recId.equalsIgnoreCase(""))  
			                    {  
									statusMessage = "Record successfully  Sent for Approval";
									String[] pa = new String[12];

//									System.out.println("=====transDate: "+transDate);
									pa[0]=recId; pa[1]= loginId; pa[2]=supervisor; pa[3]="0"; pa[4]= "";
									pa[5]= fullname; pa[6]= transDate; pa[7]= recId; pa[8]="PENDING"; pa[9]="User Creation"; pa[10]="P";pa[11]=numOfTransactionLevel;
									System.out.println("=====recId: "+recId+"     =====userId: "+userId+"     =====userid: "+userid);	
									if(userId.equals("")){userId = "0";}
									if(singleApproval.equalsIgnoreCase("Y")){
//									ad.setPendingTrans(ad.setApprovalData(venId),"1",Integer.parseInt(venId));
									ad.setPendingTransAdmin(pa,"75",Integer.parseInt(recId),"I");
									ad.setPendingTransArchive(pa,"75",Integer.parseInt(recId),Integer.parseInt(recId));
									 }
									   if(singleApproval.equalsIgnoreCase("N")){
										   pa[8]="PENDING";
									  		String mtid = appHelper.getGeneratedId("am_asset_approval");
//									   		System.out.println("approvelist.size()#$$$$$$$$$$$ "+approvelist.size());
									   	 for(int j=0;j<approvelist.size();j++)
									     {   
										  	legend.admin.objects.User usrInfo = (legend.admin.objects.User)approvelist.get(j);   	 
											String supervisorId =  usrInfo.getUserId();
											String mailAddress = usrInfo.getEmail();
											String supervisorName = usrInfo.getUserName();
											String supervisorfullName = usrInfo.getUserFullName();
//											System.out.println("SupervisorId#$$$$$$$$$$$ "+supervisorId);
									  		ad.setPendingTransAdminMultiApp(pa,"75",Integer.parseInt(recId),"I",supervisorId,mtid);
									  	 }
									}
//			                        statusMessage = "Record saved successfully";
			                        out.print("<script>alert('User Creation Record Submitted successfully and Sent for Approval.')</script>");
			                       // out.print((new StringBuilder("<script>window.location = 'DocumentHelp.jsp?np=companyVendors&vendorId=")).append(venId).append("&PC=16'</script>").toString());
			                        out.print("<script>window.location = 'DocumentHelp.jsp?np=manageUsers&status=ACTIVE'</script>");
			                    } else
			                    {
			                        System.out.println((new StringBuilder("Error saving record: New record\n for 'vendor' with User Name ")).append(recId).append(" could not be created").toString());
			                   //     out.print("<script>window.location = 'DocumentHelp.jsp?np=companyVendors'</script>");
			                        out.print("<script>window.location = 'DocumentHelp.jsp?np=manageUsers&status=ACTIVE'</script>");
			                    }
						} else {
							
							String recId = security.createManageUser2Tmp(user,limit,userId,menuId); 
//							System.out.println("=====recId for Update: "+recId+"Already Existing Staff");
				                    if(!recId.equalsIgnoreCase("") && !RowId.equalsIgnoreCase(""))  
				                    {  
										statusMessage = "User Update Record successfully  Sent for Approval";
										String[] pa = new String[12];

//										System.out.println("=====transDate: "+transDate);
										pa[0]=recId; pa[1]= loginId; pa[2]=supervisor; pa[3]="0"; pa[4]= "";
										pa[5]= fullname; pa[6]= transDate; pa[7]= recId; pa[8]="PENDING"; pa[9]="User Creation"; pa[10]="P";pa[11]=numOfTransactionLevel;
//										System.out.println("=====recId: "+recId+"     =====userId: "+userId);	
										if(userId.equals("")){userId = "0";}
//										ad.setPendingTrans(ad.setApprovalData(venId),"1",Integer.parseInt(venId));
										if(singleApproval.equalsIgnoreCase("Y")){
										ad.setPendingTransAdmin(pa,"75",Integer.parseInt(recId),"I");
//										ad.setPendingTransArchive(pa,"75",Integer.parseInt(recId),Integer.parseInt(recId));
										  }
										   if(singleApproval.equalsIgnoreCase("N")){
											   pa[8]="PENDING";
										  		String mtid =  appHelper.getGeneratedId("am_asset_approval");
//										   		System.out.println("approvelist.size()#$$$$$$$$$$$ "+approvelist.size());
										   	 for(int j=0;j<approvelist.size();j++)
										     {  
											  	legend.admin.objects.User usrInfo = (legend.admin.objects.User)approvelist.get(j);   	 
												String supervisorId =  usrInfo.getUserId();
												String mailAddress = usrInfo.getEmail();
												String supervisorName = usrInfo.getUserName();
												String supervisorfullName = usrInfo.getUserFullName();
//												System.out.println("SupervisorId#$$$$$$$$$$$ "+supervisorId);
										  		ad.setPendingTransAdminMultiApp(pa,"75",Integer.parseInt(recId),"I",supervisorId,mtid);
										  	 }
										}
//				                        statusMessage = "Record saved successfully";
				                        out.print("<script>alert('User Update Record Submitted successfully and Sent for Approval.')</script>");
				                       // out.print((new StringBuilder("<script>window.location = 'DocumentHelp.jsp?np=companyVendors&vendorId=")).append(venId).append("&PC=16'</script>").toString());
				                        out.print("<script>window.location = 'DocumentHelp.jsp?np=manageUsers&status=ACTIVE'</script>");
				                    }else{
				                        out.print("<script>alert('User Already Exist.')</script>");
				                       // out.print((new StringBuilder("<script>window.location = 'DocumentHelp.jsp?np=companyVendors&vendorId=")).append(venId).append("&PC=16'</script>").toString());
				                        out.print("<script>window.location = 'DocumentHelp.jsp?np=systemUsers&PC=11'</script>");

//										System.out.println("Error saving record: New record \nfor 'user'  with user name  "
//														+ userName
//														+ " could not be created");
//										out.print("<script>window.history.back()</script>");
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
