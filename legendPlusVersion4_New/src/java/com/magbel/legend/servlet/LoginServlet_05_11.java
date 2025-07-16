package com.magbel.legend.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.util.Cryptomanager;

import magma.AssetRecordsBean;


public class LoginServlet_05_11 extends HttpServlet {
   private static final long serialVersionUID = 1L;

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  try {
	   HttpSession session = request.getSession();
	   Properties prop = new Properties();
	   PrintWriter out = response.getWriter();
	   File file = new File("C:\\Property\\LegendPlus.properties");
	   FileInputStream input = new FileInputStream(file);
	   prop.load(input);
	   
	   response.setContentType("application/json");
       response.setCharacterEncoding("UTF-8");

	   String ThirdPartyLabel = prop.getProperty("ThirdPartyLabel");
	   String PasswordValidation = prop.getProperty("PasswordValidation");
	   System.out.println("<<<<<<<PasswordValidation: " + PasswordValidation);	
	   String loginImage = prop.getProperty("loginImage");
	   
	   String json = request.getReader().lines().collect(Collectors.joining());
	    JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
	    String uName= jsonObject.get("userName").getAsString();
	    String uPassword = jsonObject.get("userPswd").getAsString();
	    String uToken= jsonObject.get("token").getAsString();
	    //System.out.println("=======>>>>>uPassword: "+uPassword+"     uName: "+uName+"     uToken: "+uToken);
	    
	    String jsonResponse="";
	   
	   String getUserName = request.getParameter("passuserName");
		String getpassPassword = request.getParameter("passPassword");
//		System.out.println("=======>>>>>passuserName: "+getUserName+"     passPassword: "+getpassPassword);
//		String applicationType = "AND A.ROLE_UUID != '450'";
		String applicationType = "";
		legend.admin.handlers.CompanyHandler ch = new legend.admin.handlers.CompanyHandler();
		legend.admin.handlers.SecurityHandler sh = new legend.admin.handlers.SecurityHandler();
		legend.admin.handlers.AdminHandler handler = new legend.admin.handlers.AdminHandler();
		com.magbel.menu.handlers.MenuHandler menur = new com.magbel.menu.handlers.MenuHandler();
		com.magbel.util.HtmlUtility htmlUtil = new com.magbel.util.HtmlUtility();
		legend.admin.objects.Company comp = ch.getCompany();
		com.magbel.util.Cryptomanager cm = new com.magbel.util.Cryptomanager();
		EmailSmsServiceBus mail = new EmailSmsServiceBus(); 
		 AssetRecordsBean arb = new AssetRecordsBean();

		 String CompCode = "1";
		 String Apps = "LEGEND";
			String compname = sh.getCompanyInfo(CompCode);	
		//System.out.println("<<<<<<<compname: "+compname);	
		 String id = session.getId();
		 String tokenfld = "";
		 	String deployer = getServletContext().getRealPath(
		 			"/error.properties");
		 	String LicfileName = getServletContext()
		 			.getRealPath("/license.txt");
		 	//String buttConn = request.getParameter("buttConn");
		 	String buttConn = jsonObject.get("buttConn").getAsString();
			String workstationIp = request.getRemoteAddr();
			InetAddress address = InetAddress.getByName(workstationIp);
			String workstationName = address.getHostName();
		 	Calendar currentcal = Calendar.getInstance();
		 	String np = request.getParameter("np");
		 	//String np = "np";
			String urlconcat = "";
			String newurlConnect = request.getRequestURL()+""+request.getQueryString();
			String []connect = newurlConnect.split("=");
			int ConnectNo = connect.length;
			String loginSuccessful = "N";
			 String userId = "";
			 int logonAttempt = Integer.parseInt(sh.getCompanyLoginAttempt());
			
			if(ConnectNo!=1){
				connect[0] ="http://localhost:8080/legendPlus/DocumentHelp.jsp?np";
				//urlconcat = connect[0]+"="+connect[1]+"="+connect[2]+"="+connect[3]+"="+connect[4]+"="+connect[5]+"="+connect[6]+"="+connect[7]+"="+connect[8];
				urlconcat = connect[0]+"="+connect[1]+"="+connect[2]+"="+connect[3]+"="+connect[4];

				}else{urlconcat = "http://localhost:8080/legendPlus/systemConnect.jsp";}

			if (buttConn != null) {
				try {
					//lisence
					// if(sh.login(deployer))
					boolean result = false;
//				System.out.printlnln("<<<<<<<getUserName: " + getUserName);	
//				System.out.printlnln("<<<<<<<compname: " + compname+"    deployer: "+deployer+"    Apps: "+Apps+"     LicfileName: "+LicfileName);
					 result = sh.login(deployer, compname, Apps,
							LicfileName);					
					//System.out.printlnln(">>>>>>>>>>>>>>>>>>>>License Result:  "+ result);
				//String getUser = request.getParameter("userName");
				String getUser = uName;
			//if(PasswordValidation.equals("N")){getUserName=request.getParameter("userName"); getpassPassword = "";getUser = request.getParameter("userName");}
				if(PasswordValidation.equals("N")){getUserName=uName; getpassPassword = "";getUser = uName;}
				//System.out.printlnln("<<<<<<<getUserName>>>>>>>>>>>>>>: " + getUserName);
			String tokenRequired = htmlUtil.getCodeName("SELECT Token_required FROM am_gb_User WHERE User_Name = ? ",getUser);
			//System.out.printlnln("<<<<<<<tokenRequired>>>>>>>>>>>>>>: " + tokenRequired);
		if(result){
					if (sh.login(deployer, compname, Apps, LicfileName) && (getUserName == "" || getUserName==null) && (PasswordValidation.equals("Y"))) {
						//String logonAttempt = sh.getCompanyLoginAttempt();
						//==============================================

						//System.out.printlnln("<<<<<<<getUserName 1: " + getUserName);	
						legend.admin.objects.User user = null;
						//System.out.printlnln("<<<<<<<getUserName 2: " + getUserName);	
						tokenfld = "Y";
//						java.util.List list_token = sh
//							
//									.getUserByQueryProc(
//											" "
//													+ (request
//															.getParameter("userName")).toLowerCase()
//													+ ""," "
//													+ Cryptomanager.encrypt(sh.Name(
//															(request.getParameter("userName")).toLowerCase(),
//															request.getParameter("userPswd")))
//													+ "", Cryptomanager.encrypt((request
//													.getParameter("userName")).toLowerCase().toString()),tokenfld,
//													Cryptomanager.encrypt(request.getParameter("userPswd")).toString());
						
						java.util.List list_token = sh
								
								.getUserByQueryProc(
										" "
												+ (uName).toLowerCase()
												+ ""," "
												+ Cryptomanager.encrypt(sh.Name(
														(uName).toLowerCase(),
														uPassword))
												+ "", Cryptomanager.encrypt((uName).toLowerCase().toString()),tokenfld,
												Cryptomanager.encrypt(uPassword).toString());
											
						System.out.println("Token: " + list_token);
						System.out.println("Token Size: " + list_token.size());
						if (list_token != null && list_token.size() > 0) {
						
//						String Token_UserId = sh.getTokenUserId(request.getParameter("userName"));
						String Token_UserId = "";
						/*
						if(ThirdPartyLabel.equalsIgnoreCase("INTEGRIFY")){
						Token_UserId = sh.getTokenUserId(request.getParameter("userName"));
						}
						if(ThirdPartyLabel.equalsIgnoreCase("K2")){
						Token_UserId = request.getParameter("userName");
						}	
						if(ThirdPartyLabel.equalsIgnoreCase("ZENITH")){
						Token_UserId = sh.getTokenUserId(request.getParameter("userName"));
						}		
						*/
						
						//Token_UserId = sh.getTokenUserId(request.getParameter("userName"));
						Token_UserId = sh.getTokenUserId(uName);	
//						System.out.println("User Id for Token Users: " + Token_UserId);					
							

//							if (sh.tokenLogin((Token_UserId).toLowerCase(),
//									request.getParameter("token"),request.getParameter("userName")) == 1) {
//								user = (legend.admin.objects.User) list_token
//										.get(0);
//								session.setAttribute("FAILED_LOGON_CTN", "0");
//							} else {
//								session.setAttribute("FAILED_LOGON_CTN", "1");
//								out.println("<script>alert('Token Required:Try Again.')</script>");
//								out.printlnln("<script>window.location = 'systemConnect.jsp'</script>");
//							}
						
						if (sh.tokenLogin((Token_UserId).toLowerCase(),
								uToken, uName) == 1) {
							user = (legend.admin.objects.User) list_token
									.get(0);
							session.setAttribute("FAILED_LOGON_CTN", "0");
						} else {
							session.setAttribute("FAILED_LOGON_CTN", "1");
							out.println("<script type='text/javascript'>alert('Token Required:Try Again.')</script>");
							out.println("<script type='text/javascript'>window.location = 'systemConnect.jsp'</script>");
						}
						}
						
						if ((user == null && list_token.size() < 1) || (user != null && !user.isTokenRequired())) {


//							if ((request.getParameter("token") != null)||(request.getParameter("token") != "")) {
//
//								String token = request.getParameter("token")
//										.trim();
////							    System.out.println("Token====: " + token);						
//								if (!token.trim().equalsIgnoreCase("")) {
//									session.setAttribute("FAILED_LOGON_CTN", "1");
//									out.println("<script>alert('Token Required: Invalid User Name/Password details. Try Again.')</script>");
//									out.printlnln("<script>window.location = 'systemConnect.jsp'</script>");
//									
//								}
//									
//
//							}
							
							if ((uToken != null)||(uToken != "")) {

								String token = uToken
										.trim();
//							    System.out.println("Token====: " + token);						
								if (!token.trim().equalsIgnoreCase("")) {
									session.setAttribute("FAILED_LOGON_CTN", "1");
									out.println("<script type='text/javascript'>alert('Token Required: Invalid User Name/Password details. Try Again.')</script>");
									out.println("<script type='text/javascript'>window.location = 'systemConnect.jsp'</script>");
									
								}
							}
							 tokenfld = "N";
//							java.util.List list = sh									
//									.getUserByQueryProc(
//											" "
//													+ (request
//															.getParameter("userName")).toLowerCase()
//													+ ""," "
//													+ Cryptomanager.encrypt(sh.Name(
//															(request.getParameter("userName")).toLowerCase(),
//															request.getParameter("userPswd")))
//													+ "", Cryptomanager.encrypt((request
//													.getParameter("userName")).toLowerCase().toString()),tokenfld,
//													Cryptomanager.encrypt(request.getParameter("userPswd")).toString());
							 
							 java.util.List list = sh									
										.getUserByQueryProc(
												" "
														+ (uName).toLowerCase()
														+ ""," "
														+ Cryptomanager.encrypt(sh.Name(
																(uName).toLowerCase(),
																uPassword))
														+ "", Cryptomanager.encrypt((uName).toLowerCase().toString()),tokenfld,
														Cryptomanager.encrypt(uPassword).toString());
										System.out.println("<<<<<<<< List Size: " + list.size());	
							if (list != null && list.size() > 0)
								user = (legend.admin.objects.User) list.get(0);
						} else {
							session.setAttribute("FAILED_LOGON_CTN", "1");
							out.println("<script type='text/javascript'>alert('Invalid User Name/Password details. Try Again.')</script>");
							out.println("<script type='text/javascript'>window.location = 'systemConnect.jsp'</script>");

						}

						if (user != null) {
							session.setAttribute("FAILED_LOGON_CTN", "0");

							//System.out.printlnln("############################################TEST TEST ############################################");						

							if (user != null
									&& user.getUserStatus().equalsIgnoreCase(
											"ACTIVE")) //if(user != null) //the condition for access to system denial for inactive user goes here
							{

								System.out
										.println("#################################################################################################"
												+ "##################### For this particular user token required=="
												+ user.isTokenRequired());

								Date d1 = new Date(currentcal.getTimeInMillis());
								Date d2 = user.getExpDate();
								//System.out.println("D2 >>>>>> " +  d2);
								if (d2 == null) {
									//System.out.printlnln("i entered here ");
									d2 = d1;
								}//comparing d2 and d1

								if ((d1.equals(d2)) || (d1.before(d2))) {
									System.out.println("In here --------- 1");

									System.out.println("<<<<<<user.getLoginStatus(): "+user.getLoginStatus());
									
									if (user.getLoginStatus().equals("3")) {
										out.println("<script type='text/javascript'>alert('You have exceded logon limits. Contact Administrator.')</script>");
										out.println("<script type='text/javascript'>window.location = 'systemConnect.jsp'</script>");
									}
									System.out.println("Login Status Test:  "
											+ user.getLoginStatus());
									
									

									if (user.getLoginStatus().equals("0")) {
										session = request.getSession(true);
										session.setAttribute("connected",
												new String("true"));
										session.setMaxInactiveInterval(60 * comp
												.getSessionTimeout());

										magma.net.manager.SytemsManager sm = new magma.net.manager.SytemsManager();
										java.util.ArrayList functions = sm
												.findFunctionsBySecurityClass(user
														.getUserClass());
										java.util.List menus = menur
												.findMenus(user.getUserClass(),applicationType);
										java.util.ArrayList classFunctions = sm
												.findClassFunctionsById(user
														.getUserClass());
										session.setAttribute("classfunctions",
												classFunctions);
										session.setAttribute("Menus", menus);
										session.setAttribute("priviledges",
												functions);
										session.setAttribute("CurrentUser",
												user.getUserId());
										session.setAttribute("SignInName",
												user.getUserName());
										session.setAttribute("UserClass",
												user.getUserClass());
										session.setAttribute("UserCenter",
												user.getBranch());
										session.setAttribute("LastSignIn",
												user.getLastLogindate());
										session.setAttribute("SignInFrom",
												request.getRemoteAddr());
										session.setAttribute("IsSupervisor",
												user.getIsSupervisor());
										session.setAttribute("FleetAdmin",
												user.getFleetAdmin());
										session.setAttribute("_user", user);
										session.setAttribute("WorkstationName",
												workstationName);
										session.setAttribute("WorkstationIp",
												workstationIp);
										session.setAttribute("LoginStatus",
												user.getLoginStatus());
			//							System.out
			//									.println("-------------------");
										//admin.start();
			//							System.out
			//									.println("------------------");
										String loguser = "N";

										if (comp.getLogUserAudit() != null) {
											loguser = comp.getLogUserAudit();
										}

										session.setAttribute("LoginAudit",
												loguser);
								//		Cookie cu = new Cookie("curr_user",
								//				user.getUserId());
								//		response.addCookie(cu);
										sh.updateLogins(user.getUserId(), "1",
												request.getRemoteAddr());
										
										System.out.println("We are here");

										session.setAttribute(
												"FAILED_LOGON_CTN", "0");
										
										System.out.println("We are here 2");

										if (sh.queryPexpiry(user.getUserId())) {
											response.sendRedirect("changePassword1.jsp");
											//System.out.printlnln("JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ here i am");
											//out.printlnln("<script>window.location = 'changePassword.jsp'</script>");
										} else if (user.getMustChangePwd()
												.equals("Y")) {
											response.sendRedirect("changePassword1.jsp");
											//out.printlnln("<script>window.location = 'changePassword.jsp'</script>");
										} else {
			//							System.out.println("<<<<=======Browser Parameter: "+np+"  newurl: "+newurl);
											//response.sendRedirect(urlfromMail);
											if(np==null){
											System.out.println("<<<<=======Browser Parameter with null: "+np);
											//response.sendRedirect("DocumentHelp.jsp");
											
										   out.println("DocumentHelp.jsp");
										   out.flush();
									        
									        // jsonResponse = new Gson().toJson(new ResponseData(true, "DocumentHelp.jsp"));
										
										}
											//response.sendRedirect(newurl);
											if(np!=null){//System.out.println("<<<<=======Browser Parameter without null: "+np);
											response.sendRedirect(urlconcat);
											}
											session.setAttribute(
													"FAILED_LOGON_CTN", "0");
											handler.SaveLoginAudit(
													user.getUserId(),
													user.getBranch(),
													workstationName,
													workstationIp, id);
			//								System.out
			//										.println("-----------xx----------- Logging in --------");

									 loginSuccessful = "Y";
									  userId = user.getUserId();

										
											//out.printlnln("<script>window.location = 'systemWebtop.jsp'</script>");
										}
									} else {
										if (user.getLoginStatus().equals("4")) {
											out.println("<script type='text/javascript'>alert('You can not Logon to Legend by this Time')</script>");
											out.println("<script type='text/javascript'>window.location = 'systemConnect.jsp'</script>");
										} else {
											out.println("<script type='text/javascript'>alert('User already connected to Legend')</script>");
											out.println("<script type='text/javascript'>window.location = 'systemConnect.jsp'</script>");
										}
									}

								} else {
				//					System.out.printlnln("In here --------- 2");
									out.println("<script type='text/javascript'>alert('Access Denied. Please contact your Administrator !!!!')</script>");
									out.println("<script type='text/javascript'>window.location = 'systemConnect.jsp'</script>");
								}
							} else {
								out.println("<script type='text/javascript'>alert('You are not authorized to use Legend')</script>");
								out.println("<script type='text/javascript'>window.location = 'systemConnect.jsp'</script>");
							}//the else part of inactive user goes here
						} else {
							//for logon count logonAttempt
			//				System.out
			//						.println("-----------xx----------- Logging in --hh----");
							//System.out.printlnln("<<<<<<<<<<<<<<<<< here 1");
							if ((String) session
									.getAttribute("FAILED_LOGON_CTN") != null) {
								//System.out.printlnln("<<<<<<<<<<<<<<<<< here 2");
								int logoncount = Integer
										.parseInt((String) session
												.getAttribute("FAILED_LOGON_CTN"));
								logoncount += 1;
								//System.out.printlnln("<<<<<<<<<<<<<<<<< here logoncount " + logoncount);
								if (logoncount == logonAttempt) {

//									sh.updateLoginAsAboveLimit(
//											request.getParameter("userName"),
//											request.getRemoteAddr());
									sh.updateLoginAsAboveLimit(
											uName,
											request.getRemoteAddr());
									out.println("<script type='text/javascript'>alert('You have exceded the allowed logon attempts limit.')</script>");
									out.println("<script type='text/javascript'>window.location = 'systemConnect.jsp'</script>");
								} else {
									session.setAttribute("FAILED_LOGON_CTN",
											String.valueOf(logoncount));

								}

							}//if((String)session.getAttribute("FAILED_LOGON_CTN") != null)

							else {

								session.setAttribute("FAILED_LOGON_CTN", "1");
								//out.println("<script>alert('Invalid user details. Try Again.')</script>");
							}

							//if(Integer.parseInt((String)session.getAttribute("FAILED_LOGON_CTN")) < 3)	
							out.println("<script type='text/javascript'>alert('Invalid User Name/Password details. Try Again.')</script>");
							out.println("<script type='text/javascript'>window.location = 'systemConnect.jsp'</script>");
						}
						//License end
					} 
					
					if((getUserName != "") && (PasswordValidation.equals("N"))){
			//			System.out.printlnln("<<<<<<<<<<<<<<<<< here PasswordValidation For Token " + PasswordValidation);
			//			System.out.printlnln("<<<<<<<<<<<<<<<<< here tokenRequired " + tokenRequired);
					if(tokenRequired.equals("Y")){
						//String logonAttempt = sh.getCompanyLoginAttempt();
						//==============================================
						

						legend.admin.objects.User user = null;
//						System.out.printlnln("<<<<<<<<<<<<<<<<< here ThirdPartyLabel " + ThirdPartyLabel);
						tokenfld = "Y";
//						System.out.printlnln("<<<<<<<<<<<<<<<<< here userName " + request.getParameter("userName"));
//						java.util.List list_token = sh
//							
//									.getUserByQueryNoPasswordProc(
//											" "
//													+ (request
//															.getParameter("userName")).toLowerCase()
//													+ ""," "
//													+ (request
//													.getParameter("userName")).toLowerCase(),tokenfld);
						
						java.util.List list_token = sh
								
								.getUserByQueryNoPasswordProc(
										" "
												+ (uName).toLowerCase()
												+ ""," "
												+ (uName).toLowerCase(),tokenfld);
											
//						System.out.println("Token: " + list_token);
//						System.out.println("Token Size: " + list_token.size());
						if (list_token != null && list_token.size() > 0) {
						
//						String Token_UserId = sh.getTokenUserId(request.getParameter("userName"));

						String Token_UserId = "";
						/*
						if(ThirdPartyLabel.equalsIgnoreCase("INTEGRIFY")){
						Token_UserId = sh.getTokenUserId(request.getParameter("userName"));
						}
						if(ThirdPartyLabel.equalsIgnoreCase("K2")){
						Token_UserId = request.getParameter("userName");
						}	
						if(ThirdPartyLabel.equalsIgnoreCase("ZENITH")){
						Token_UserId = sh.getTokenUserId(request.getParameter("userName"));
						}	
						*/
						
						//Token_UserId = sh.getTokenUserId(request.getParameter("userName"));
						
						Token_UserId = sh.getTokenUserId(uName);
						
//						System.out.println("User Id for Token Users: " + Token_UserId);					
							
//							System.out.println("User Id for Token Users: " + Token_UserId);

//							if (sh.tokenLogin((Token_UserId).toLowerCase(),
//									request.getParameter("token"),request.getParameter("userName")) == 1) {
//								user = (legend.admin.objects.User) list_token
//										.get(0);
//								session.setAttribute("FAILED_LOGON_CTN", "0");
//							} else {
//								session.setAttribute("FAILED_LOGON_CTN", "1");
//								out.println("<script>alert('Token Required:Try Again.')</script>");
//								out.printlnln("<script>window.location = 'systemConnect.jsp'</script>");
//							}
						
						if (sh.tokenLogin((Token_UserId).toLowerCase(),
								uToken,uName) == 1) {
							user = (legend.admin.objects.User) list_token
									.get(0);
							session.setAttribute("FAILED_LOGON_CTN", "0");
						} else {
							session.setAttribute("FAILED_LOGON_CTN", "1");
							out.println("<script type='text/javascript'>alert('Token Required:Try Again.')</script>");
							out.println("<script type='text/javascript'>window.location = 'systemConnect.jsp'</script>");
						}
						}
						
//						if ((user == null && list_token.size() < 1) || (user != null && !user.isTokenRequired())) {
//
//
//							if ((request.getParameter("token") != null)||(request.getParameter("token") != "")) {
//
//								String token = request.getParameter("token")
//										.trim();
//							    System.out.println("Token====: " + token);						
//								if (!token.trim().equalsIgnoreCase("")) {
//									session.setAttribute("FAILED_LOGON_CTN", "1");
//									out.println("<script>alert('Token Required: Invalid User Name/Password details. Try Again.')</script>");
//									out.printlnln("<script>window.location = 'systemConnect.jsp'</script>");
//									
//								}
//									
//
//							}
						
						if ((user == null && list_token.size() < 1) || (user != null && !user.isTokenRequired())) {


							if ((uToken != null)||(uToken != "")) {

								String token = uToken
										.trim();
							    System.out.println("Token====: " + token);						
								if (!token.trim().equalsIgnoreCase("")) {
									session.setAttribute("FAILED_LOGON_CTN", "1");
									out.println("<script type='text/javascript'>alert('Token Required: Invalid User Name/Password details. Try Again.')</script>");
									out.println("<script type='text/javascript'>window.location = 'systemConnect.jsp'</script>");
									
								}
									

							}
							 tokenfld = "N";
//							java.util.List list = sh									
//									.getUserByQueryNoPasswordProc(
//											" "
//													+ (request
//															.getParameter("userName")).toLowerCase()
//													+ ""," "
//													+ (request
//													.getParameter("userName")).toLowerCase(),tokenfld);
							 
							 java.util.List list = sh									
										.getUserByQueryNoPasswordProc(
												" "
														+ (uName).toLowerCase()
														+ ""," "
														+ (uName).toLowerCase(),tokenfld);
											
							if (list != null && list.size() > 0)
								user = (legend.admin.objects.User) list.get(0);
						} else {
							session.setAttribute("FAILED_LOGON_CTN", "1");
							response.getWriter().write("<script type='text/javascript'>alert('Invalid User Name/Password details. Try Again.')</script>");
							response.getWriter().write("<script type='text/javascript'>window.location = 'systemConnect.jsp'</script>");

						}

						if (user != null) {
							session.setAttribute("FAILED_LOGON_CTN", "0");

							//System.out.printlnln("############################################TEST TEST ############################################");						

							if (user != null
									&& user.getUserStatus().equalsIgnoreCase(
											"ACTIVE")) //if(user != null) //the condition for access to system denial for inactive user goes here
							{

								System.out
										.println("#################################################################################################"
												+ "##################### For this particular user token required=="
												+ user.isTokenRequired());

								Date d1 = new Date(currentcal.getTimeInMillis());
								Date d2 = user.getExpDate();
								//System.out.printlnln("D2 >>>>>> " +  d2);
								if (d2 == null) {
									System.out.println("i entered here ");
									d2 = d1;
								}//comparing d2 and d1

								if ((d1.equals(d2)) || (d1.before(d2))) {
									System.out.println("In here --------- 1");

									if (user.getLoginStatus().equals("3")) {
										response.getWriter().write("<script type='text/javascript'>alert('You have exceded logon limits. Contact Administrator.')</script>");
										response.getWriter().write("<script type='text/javascript'>window.location = 'systemConnect.jsp'</script>");
									}
									System.out.println("Login Status Test:  "
											+ user.getLoginStatus());

									if (user.getLoginStatus().equals("0")) {
										session = request.getSession(true);
										session.setAttribute("connected",
												new String("true"));
										session.setMaxInactiveInterval(60 * comp
												.getSessionTimeout());

										magma.net.manager.SytemsManager sm = new magma.net.manager.SytemsManager();
										java.util.ArrayList functions = sm
												.findFunctionsBySecurityClass(user
														.getUserClass());
										java.util.List menus = menur
												.findMenus(user.getUserClass(),applicationType);
										java.util.ArrayList classFunctions = sm
												.findClassFunctionsById(user
														.getUserClass());
										session.setAttribute("classfunctions",
												classFunctions);
										session.setAttribute("Menus", menus);
										session.setAttribute("priviledges",
												functions);
										session.setAttribute("CurrentUser",
												user.getUserId());
										session.setAttribute("SignInName",
												user.getUserName());
										session.setAttribute("UserClass",
												user.getUserClass());
										session.setAttribute("UserCenter",
												user.getBranch());
										session.setAttribute("LastSignIn",
												user.getLastLogindate());
										session.setAttribute("SignInFrom",
												request.getRemoteAddr());
										session.setAttribute("IsSupervisor",
												user.getIsSupervisor());
										session.setAttribute("FleetAdmin",
												user.getFleetAdmin());
										session.setAttribute("_user", user);
										session.setAttribute("WorkstationName",
												workstationName);
										session.setAttribute("WorkstationIp",
												workstationIp);
										session.setAttribute("LoginStatus",
												user.getLoginStatus());
										System.out
												.println("-------------------");
										//admin.start();
			//							System.out
			//									.println("------------------");
										String loguser = "N";

										if (comp.getLogUserAudit() != null) {
											loguser = comp.getLogUserAudit();
										}

										session.setAttribute("LoginAudit",
												loguser);
								//		Cookie cu = new Cookie("curr_user",
								//				user.getUserId());
								//		response.addCookie(cu);
										System.out.println("<<<<<<<user.getUserId()==: "+user.getUserId());
										
										sh.updateLogins(user.getUserId(), "1",
												request.getRemoteAddr());

										session.setAttribute(
												"FAILED_LOGON_CTN", "0");

										if (sh.queryPexpiry(user.getUserId())) {
											response.sendRedirect("changePassword1.jsp");
											//System.out.printlnln("JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ here i am");
											//out.printlnln("<script>window.location = 'changePassword.jsp'</script>");
										} else if (user.getMustChangePwd()
												.equals("Y")) {
											response.sendRedirect("changePassword1.jsp");
											//out.printlnln("<script>window.location = 'changePassword.jsp'</script>");
										} else {
									//	System.out.println("<<<<=======Browser Parameter: "+np+"  newurl: "+newurl);
											//response.sendRedirect(urlfromMail);
											if(np==null){
											//System.out.println("<<<<=======Browser Parameter with null: "+np);
											//response.sendRedirect("DocumentHelp.jsp");
												
											
											response.getWriter().write("DocumentHelp.jsp");
										}
											//response.sendRedirect(newurl);
											if(np!=null){//System.out.println("<<<<=======Browser Parameter without null: "+np);
											response.sendRedirect(urlconcat);
											}
											session.setAttribute(
													"FAILED_LOGON_CTN", "0");
											handler.SaveLoginAudit(
													user.getUserId(),
													user.getBranch(),
													workstationName,
													workstationIp, id);
			//								System.out
			//										.println("-----------xx----------- Logging in --------");

									 loginSuccessful = "Y";
									  userId = user.getUserId();

										
											//out.printlnln("<script>window.location = 'systemWebtop.jsp'</script>");
										}
									} else {
										if (user.getLoginStatus().equals("4")) {
											response.getWriter().write("<script type='text/javascript'>alert('You can not Logon to Legend by this Time')</script>");
											response.getWriter().write("<script type='text/javascript'>window.location = 'systemConnect.jsp'</script>");
										} else {
											response.getWriter().write("<script type='text/javascript'>alert('User already connected to Legend')</script>");
											response.getWriter().write("<script type='text/javascript'>window.location = 'systemConnect.jsp'</script>");
										}
									}

								} else {
				//					System.out.printlnln("In here --------- 2");
									response.getWriter().write("<script type='text/javascript'>alert('Access Denied. Please contact your Administrator !!!!')</script>");
									response.getWriter().write("<script type='text/javascript'>window.location = 'systemConnect.jsp'</script>");
								}
							} else {
								out.println("<script type='text/javascript'>alert('You are not authorized to use Legend')</script>");
								out.println("<script type='text/javascript'>window.location = 'systemConnect.jsp'</script>");
							}//the else part of inactive user goes here
						} else {
							//for logon count logonAttempt
			//				System.out
			//						.println("-----------xx----------- Logging in --hh----");
							//System.out.printlnln("<<<<<<<<<<<<<<<<< here 1");
							if ((String) session
									.getAttribute("FAILED_LOGON_CTN") != null) {
								//System.out.printlnln("<<<<<<<<<<<<<<<<< here 2");
								int logoncount = Integer
										.parseInt((String) session
												.getAttribute("FAILED_LOGON_CTN"));
								logoncount += 1;
								//System.out.printlnln("<<<<<<<<<<<<<<<<< here logoncount " + logoncount);
								if (logoncount == logonAttempt) {

//									sh.updateLoginAsAboveLimit(
//											request.getParameter("userName"),
//											request.getRemoteAddr());
//									out.println("<script>alert('You have exceded the allowed logon attempts limit.')</script>");
//									out.printlnln("<script>window.location = 'systemConnect.jsp'</script>");
									
									sh.updateLoginAsAboveLimit(
											uName,
											request.getRemoteAddr());
									out.println("<script type='text/javascript'>alert('You have exceded the allowed logon attempts limit.')</script>");
									out.println("<script type='text/javascript'>window.location = 'systemConnect.jsp'</script>");
								} else {
									session.setAttribute("FAILED_LOGON_CTN",
											String.valueOf(logoncount));

								}

							}//if((String)session.getAttribute("FAILED_LOGON_CTN") != null)

							else {

								session.setAttribute("FAILED_LOGON_CTN", "1");
								//out.println("<script>alert('Invalid user details. Try Again.')</script>");
							}

							//if(Integer.parseInt((String)session.getAttribute("FAILED_LOGON_CTN")) < 3)	
							out.println("<script type='text/javascript'>alert('Invalid User Name/Password details. Try Again.')</script>");
							out.println("<script type='text/javascript'>window.location = 'systemConnect.jsp'</script>");
						}
						//.........End of Login without Password......
//					System.out.printlnln("<<<<<<<<<<<<<<<<< here getUserName " + getUserName);
					}
					 else {
						out.println("<script type='text/javascript'>alert('Invalid User Name/Token details. Try Again.')</script>");
						out.println("<script type='text/javascript'>window.location = 'systemConnect.jsp'</script>");
						}
					}
					}
					 else {
						out.println("<script type='text/javascript'>alert('Application has expired or key is not valid');</script>");
						out.println("<script type='text/javascript'>window.location = 'systemConnect.jsp'</script>");
					}
					
				} catch (Throwable t) {
					System.err.print(t.getMessage());
					t.printStackTrace();
					out.println("<script type='text/javascript'>alert('Error occured while processing your request. Try Again.')</script>");
					out.println("<script type='text/javascript'>window.location = 'systemConnect.jsp'</script>");
				}//catch
				
			} ///else{out.println("<script>alert('You are not authorized to use Legend')</script>");}
		
	  }catch(Exception e) {
		  e.getMessage();
	  }

   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      this.doPost(request, response);
   }
   
   
  
   
}