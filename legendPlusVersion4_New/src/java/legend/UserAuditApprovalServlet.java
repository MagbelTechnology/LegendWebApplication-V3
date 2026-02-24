package legend;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.sql.Timestamp;
import java.util.Arrays;

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

import legend.admin.handlers.SecurityHandler; 
import magma.AssetRecordsBean;
import magma.net.dao.MagmaDBConnection;
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

public class UserAuditApprovalServlet extends HttpServlet {
	public UserAuditApprovalServlet() {
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

		MagmaDBConnection dbConnection = new MagmaDBConnection();
		
		String statusMessage = "";
		boolean updtst = false;
  
		legend.admin.objects.User user = new legend.admin.objects.User();
		legend.admin.handlers.SecurityHandler security = new legend.admin.handlers.SecurityHandler();
		legend.admin.handlers.CompanyHandler company = new legend.admin.handlers.CompanyHandler();
        String computerName = null;
        String remoteAddress = request.getRemoteAddr();
        InetAddress inetAddress = InetAddress.getByName(remoteAddress);
        System.out.println("inetAddress: " + inetAddress);
        computerName = inetAddress.getHostName();
        System.out.println("computerName: " + computerName);
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
			System.out.println("Current IP address : " + ip.getHostAddress());

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
		System.out.println("<<<<<<=====loginId: "+loginId+"    userClass: "+userClass);
		
		String password = new String();
		 String [] passwordDisplay = null;
		try {
			if(userId.equals("0"))
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
		Timestamp approveddate =  dbConnection.getDateTime(new java.util.Date());
		String buttSave = request.getParameter("buttSave");
		// String buttAssg = request.getParameter("buttAssg");
		String operation = request.getParameter("operation");
		String assetId = request.getParameter("assetId");
		int approvalCount =  Integer.parseInt(request.getParameter("approvalCount"));
		int tranLevel = Integer.parseInt(request.getParameter("tranLevel"));
//		int assetCode =Integer.parseInt(request.getParameter("assetCode"));
		String recId = request.getParameter("recId");
        String approvedBy = loginId;
        String RecordId  = request.getParameter("userName");
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
		System.out.println("<<<<<<<< userStatus : " + userStatus);
		String userid = (String) session.getAttribute("CurrentUser");
		String email = request.getParameter("email");
		String branchRestrict= request.getParameter("branchRestrict");
		if(branchRestrict==null){branchRestrict = "N";}
		String tokenRequire= request.getParameter("tokenrequire");
		if(tokenRequire==null){tokenRequire="N";}
		String expiry_Days = request.getParameter("expiryDays");
		String astatus = request.getParameter("astatus");
		 String singleApproval = request.getParameter("singleApproval");
		String rr = request.getParameter("reject_reason");
		int tranId = Integer.parseInt(request.getParameter("tranId"));
		if((expiry_Days.equals("")) || (expiry_Days.equals(null))){expiry_Days = "0";}
 //		System.out.println("<<<<<<=====expiry_Days: "+expiry_Days);
//        int expiryDays = (request.getParameter("expiryDays")== null || request.getParameter("expiryDays")== "")?0:Integer.parseInt(request.getParameter("expiryDays"));
		int expiryDays = Integer.parseInt(expiry_Days);
//		System.out.println("<<<<<<=====expiryDays: "+expiryDays);
        String expiryDate = request.getParameter("expiryDate");
        String alertmessage = "";
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
         if(regionRestrict==null){tokenRequire="N";}
         String zoneRestrict= request.getParameter("zoneRestrict");
         int assetCode = request.getParameter("assetCode") == null?0:Integer.parseInt(request.getParameter("assetCode"));
         if(zoneRestrict==null){zoneRestrict="N";}
         String isFacilityAdministrator= request.getParameter("is_FacilityAdministrator");
         String isStoreAdministrator= request.getParameter("is_StoreAdministrator");
         String existUser = aprecords.getCodeName("select USER_ID from am_gb_User where USER_NAME='"+userName+"'");
         String RowId = aprecords.getCodeName("select class from am_gb_UserTmp where TMPID='"+recId+"'");
         String initiatorId = aprecords.getCodeName("select userId from am_gb_UserTmp where TMPID='"+recId+"'");
         String menuId = aprecords.getCodeName("select menuId from am_gb_UserTmp where TMPID='"+recId+"'");
       //  String test = "select menuId from am_gb_UserTmp where TMPID='"+recId+"'";
		//System.out.println("<<<<<<<<=======menuId: "+menuId+"   Test: "+test);
//		System.out.println("<<<<<<<<=======regionCode: "+regionCode+"   zoneCode: "+zoneCode);
         userStatus = aprecords.getCodeName("select USER_STATUS from am_gb_UserTmp where TMPID='"+recId+"'");
         approvedBy = aprecords.getCodeName("select USER_Name from am_gb_User where USER_ID='"+approvedBy+"'");
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
		user.setCreatedBy(initiatorId);
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
			
			AssetRecordsBean arb = new AssetRecordsBean();
			if (!userClass.equals("NULL") || userClass!=null){
//			min = 2;
//			System.out.println("password0 ============================ "+password0+"  Min: "+min+"   userId: "+userId+"    password0.length(): "+password0.length()+"    astatus: "+astatus);
			if (password0.length() >= min || password0 =="") {
				
				String tableName = "am_gb_UserTmp";
                arb.deleteOtherSupervisorswithBatchId(String.valueOf(tranId),userid);
              System.out.println("Value of singleApproval is >>>>>> " + singleApproval);
              String transAvailable=arb.getCodeName("select count(*) from am_asset_approval WHERE asset_Id = '"+assetId+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
              System.out.println("Value of transAvailable is >>>>>> " + transAvailable);
              if (transAvailable == "0") {alertmessage = "Someone already attending to the Transaction";}
              if (singleApproval.equalsIgnoreCase("N")) {
              arb.setPendingMultiApprTransArchive(arb.setApprovalDataUploadGroup(Long.parseLong(recId),tableName),"75",Long.parseLong(assetId),assetCode,userid); 
              aprecords.updateRaiseEntry(assetId);
              }
              
				if (userId.equals("0") && astatus.equalsIgnoreCase("A")) {
					System.out.println("----------------astatus: "+astatus);
					if ((security.getUserByUserName(userName) == null) && (existUser.equals(""))){
						 //if (security.createUser(user)) {            before limit introduction
							if (security.createManageUser2(user, limit, userid)) {
							if (!userClass.equals("NULL") || userClass!=null){
							out.print("<script>alert('Record saved successfully.')</script>");
                            EmailSmsServiceBus mail = new EmailSmsServiceBus();
                            String message="Your password on legend application :"+passwordDisplay[1];
//                            System.out.println("---------------------------------------------------------------"+message);
                            String newPassword = passwordDisplay[1];
                            System.out.println("My  password on legend application : "+newPassword);
                            
   	   					 String q = "update am_asset_approval set process_status='A', asset_status='ACTIVE', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
   	   					 String r = "update am_gb_UserTmp set RECORD_TYPE='A', Approval_status='APPROVED' where TMPID=" + recId;
   	   					 arb.updateAssetStatusChange(q);
   	   					 arb.updateAssetStatusChange(r);
   	   					 
                            mail.sendMail(email, "Password on Legend User", message);
							out.print("<script>window.location = 'DocumentHelp.jsp?np=transactionForApprovalList'</script>");
                            
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
								.print("<script>window.location = 'DocumentHelp.jsp?np=systemUsersApproval&userId="
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
				} else if (!userId.equals("") && astatus.equalsIgnoreCase("A")) {
				System.out.println("<<<<<<userClass: "+userClass+"    userId ============================ "+userId);
					if (!userClass.equals("NULL") || userClass!=null){
					
					user.setUserId(userId);  
					audit.select(1,
							"SELECT * FROM  AM_GB_USER  WHERE user_Id = '"
									+ userId + "'");  
					//boolean isupdt = security.updateUser(user);   before limit introduction
					boolean isupdt = security.updateManageUser2(user,limit);
					System.out.println("---------------1------------------------isupdt: "+isupdt);
					audit.select(2,
							"SELECT * FROM  AM_GB_USER  WHERE user_Id = '"
									+ userId + "'");
					//updtst = audit.logAuditTrailActionPerformed("AM_GB_USER ", branchcode,
					String actPerformed = "Update user profile"+"$"+userName+"$"+approvedBy;
					 System.out.println("actPerformed : "+actPerformed);
					 int initiator = 0;

					 if (initiatorId != null && !initiatorId.trim().isEmpty()) {
					     initiator = Integer.parseInt(initiatorId);
					 }
//					updtst = audit.logAuditTrail("AM_GB_USER ", branchcode,Integer.parseInt(initiatorId), menuId, RecordId,actPerformed,hostName,ipAddress,macAddress);
						updtst = audit.logAuditTrail("AM_GB_USER ", branchcode,initiator, menuId, RecordId,actPerformed,hostName,ipAddress,macAddress);

					 System.out.println("---------------2------------------------isupdt: "+isupdt);
					if (updtst == true) {
						arb.deleteOtherSupervisorswithBatchId(String.valueOf(tranId),userid);
						 String q = "update am_asset_approval set process_status='A', asset_status='ACTIVE', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
						 String r = "update am_gb_UserTmp set RECORD_TYPE='A', Approval_status='APPROVED' where TMPID=" + recId;
						 arb.updateAssetStatusChange(q);
						 arb.updateAssetStatusChange(r);
						// statusMessage = "Update on record is
						// successfull";
						out
								.print("<script>alert('Update on record is successful')</script>");
						out
								.print("<script>window.location = 'DocumentHelp.jsp?np=transactionForApprovalList'</script>");

					} else {
						// statusMessage = "No changes made on record";
						out
								.print("<script>alert('No changes made on record')</script>");
//						out
//								.print("<script>window.location = 'DocumentHelp.jsp?np=systemUsersApproval&userId="
//										+ userId + "&PC=11'</script>");
						
						out
						.print("<script>window.location ='DocumentHelp.jsp?np=systemUsersApproval&operation=1&id=" +assetId +"&tranId="+tranId+
								"&transaction_level="+tranLevel+"&approval_level_count="+approvalCount+"&assetCode="+recId+
								"&recId="+recId+"&PC=31'</script>");
					}
				}else {
						out
						.print("<script>alert('You have No Right')</script>");
						out
						.print("<script>window.location = 'DocumentHelp.jsp?np=systemUsersApproval&userId="
								+ userId + "&PC=11'</script>");
					}
				}
			} else {
				out.print("<script>alert('Minimum user password length is "
						+ min + "')</script>");
				out.print("<script>window.history.back()</script>");
			}
			
			if (astatus.equalsIgnoreCase("R")) {
				 String q = "update am_asset_approval set process_status='R', asset_status='REJECTED', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
				 String r = "update am_gb_UserTmp set RECORD_TYPE='R', Approval_status='REJECTED' where TMPID=" + recId;
				 arb.updateAssetStatusChange(q);
				 arb.updateAssetStatusChange(r);
				out.print("<script>alert('Rejection Successful')</script>");
				out.print("<script>window.location = 'DocumentHelp.jsp?np=transactionForApprovalList'</script>");
			}					
		}else {
			out.print("<script>alert('Session Time Out')</script>");
			out.print("<script>window.location = 'DocumentHelp.jsp?np=systemUsersApproval&userId="
					+ userId + "&PC=11'</script>");
		} 

		} catch (Throwable e) {
			e.printStackTrace();
			// statusMessage = "Ensure unique record entry";
			out.print("<script>alert('Ensure unique record entry. Or Session Time Out')</script>");
			System.err.print(e.getMessage());
			out.print("<script>window.location = 'DocumentHelp.jsp?np=systemUsersApproval&userId="
					+ userId + "&PC=11'</script>");
		}
	}
}
