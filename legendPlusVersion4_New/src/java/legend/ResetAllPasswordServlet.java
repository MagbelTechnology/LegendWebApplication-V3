package legend;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import audit.AuditTrailGen;

import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.util.CryptManager;
import com.magbel.util.Cryptomanager;

import java.util.Enumeration;

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

public class ResetAllPasswordServlet extends HttpServlet {
	public ResetAllPasswordServlet() {
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
		String loginUserId = request.getParameter("userid");
		String statusMessage = "";
		boolean updtst = false; 

		 String userClass = (String)session.getAttribute("UserClass");
		 
		legend.admin.objects.User user = new legend.admin.objects.User();
		legend.admin.handlers.SecurityHandler security = new legend.admin.handlers.SecurityHandler();
		legend.admin.handlers.CompanyHandler company = new legend.admin.handlers.CompanyHandler();
                legend.admin.handlers.AdminHandler admin = new legend.admin.handlers.AdminHandler();
		int min = company.getCompany().getMinimumPassword();
		int passexpiry = company.getCompany().getPasswordExpiry();

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
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
			}
			String macAddress = sb.toString();
			System.out.println(sb.toString());
			
		AuditTrailGen audit = new AuditTrailGen();
		  try
	        {  
			  if (!userClass.equals("NULL") || userClass!=null){
		     java.util.ArrayList list =security.getAllUserByQuery(loginUserId);
		     System.out.println("-->size>--> "+list.size());
		     for(int i=0;i<list.size();i++)
		     {
		    	 legend.admin.objects.User  userRecords = (legend.admin.objects.User)list.get(i);    	 
					String userId =  userRecords.getUserId();
					String userName =  userRecords.getUserName().toLowerCase();
					String password =  userRecords.getPassword();
					String userEmailId = userRecords.getEmail();

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

		//Cryptomanager cm = new Cryptomanager();
		CryptManager cm = new CryptManager();

//		String userId = request.getParameter("userId");
                
//                String userEmailId= request.getParameter("userEmailId");
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+userEmailId);

  //              String password = new String();
                 String [] passwordDisplay = null;
		try {
			//password = cm.encrypt(request.getParameter("userName").toLowerCase());
 //                        System.out.println("userName  = "+request.getParameter("userName").toLowerCase());
          //*changrd on 28-6-10               password = cm.encrypt(security.Name(request.getParameter("userName").toLowerCase(),request.getParameter("userName").toLowerCase()));
                passwordDisplay =security.PasswordChange(userName,userName);
                passwordDisplay =security.PasswordChange(userName,userName);                        
                     password   = cm.encrypt(passwordDisplay[0]);
 //                       System.out.println("<<<<<User NAme: "+userName+"    password ============================ "+password);
		} catch (Exception e) {
		}


//		String buttSave = request.getParameter("buttSave");
		// String buttAssg = request.getParameter("buttAssg");

//		String userName = request.getParameter("userName");

        //System.out.println("the value of user name is ============= "+request.getParameter("userName") );
        //System.out.println("the value of encrypted password is ============= "+password );
        //System.out.println("the value of userid is ============= "+userId );



		user.setPassword(password);

		//user.setPwdChanged("Y");
        user.setMustChangePwd("Y");
		user.setLoginStatus("0");

                String roleid =admin.getPrivilegesRole("Reset Password");

			 if (!userId.equals(""))
                                 {
					user.setUserId(userId);
					audit.select(1, "SELECT * FROM  AM_GB_USER  WHERE user_Id = '" + userId + "'");

                                        boolean isupdt = security.updateManageUser(user);
					audit.select(2, "SELECT * FROM  AM_GB_USER  WHERE user_Id = '" + userId + "'");

                                        updtst = audit.logAuditTrail("AM_GB_USER ", branchcode, loginID, roleid,hostName,ipAddress,macAddress);
					if (isupdt == true) {
                                                EmailSmsServiceBus mail = new EmailSmsServiceBus();
                                                String message="Your new password on legend application :"+passwordDisplay[1];
                              //                  System.out.println("---------------------------------------------------------------"+message);
                                                String newPassword = passwordDisplay[1];
                               //                 System.out.println("My new password on legend application : "+newPassword);

						mail.sendMail(userEmailId, "Password reset on Legend User", message);

					}

				}

		     }	
				out.print("<script>alert('Password reset successfull')</script>");
				out.print("<script>window.location = 'DocumentHelp.jsp?np=manageUserPassword&status=ACTIVE&amp;select=ACTIVE'</script>");		     
	        }
	        }
	        catch(Exception e)
	        {
	            e.printStackTrace();
				out.print("<script>alert('Error occured during password reset.')</script>");
				System.err.print(e.getMessage());	            
				out.print("<script>window.location = 'DocumentHelp.jsp?np=manageUserPassword&status=ACTIVE&amp;select=ACTIVE'</script>");
	        }	
		
	}
}
