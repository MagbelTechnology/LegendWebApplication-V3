package legend;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletConfig;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServlet;
import audit.*;
import legend.admin.handlers.AdminHandler;
import legend.admin.objects.State;

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
public class StateAuditServlet extends HttpServlet {
	public StateAuditServlet() {
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

		// String type = request.getParameter("TYPE");
		String statusMessage = "";
		boolean updtst = false;
		// java.sql.Date dt = new java.sql.Date();
		AuditTrailGen audit = new AuditTrailGen();
		// java.text.SimpleDateFormat sdf = new
		// java.text.SimpleDateFormat("dd/mm/yyyy");

		// String loginId = request.getParameter("loginId");
		int userID;
		String userId = (String) session.getAttribute("CurrentUser");
		if (userId == null) {
			userID = 0;
		} else {
			userID = Integer.parseInt(userId);
		}
		String userClass = (String)session.getAttribute("UserClass");
		String branchcode = (String) session.getAttribute("UserCenter");
		if (branchcode == null) {
			branchcode = "not set";
		}

		String buttSave = request.getParameter("buttSave");
		String stateId = request.getParameter("stateId");
		// if(stateId == null){stateId = "";}
		String stateCode = request.getParameter("stateCode");
		String stateName = request.getParameter("stateName");
		String stateStatus = request.getParameter("stateStatus");

		// String user = (String)session.getAttribute("CurrentUser");
		legend.admin.objects.State state = new legend.admin.objects.State();

		state.setStateCode(stateCode);
		state.setStateName(stateName);
		state.setStateStatus(stateStatus);
		state.setUserId(userId);

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
			
		legend.admin.handlers.AdminHandler admin = new legend.admin.handlers.AdminHandler();
                String roleid =admin.getPrivilegesRole("Manage States");
		try {
			 if (!userClass.equals("NULL") || userClass!=null){
			if (buttSave != null) {
				if (stateId.equals("")) {
					if (admin.getStateByCode(stateCode) != null) {
						out
								.print("<script>alert('The state code already exists .');</script>");
						out.print("<script>history.go(-1);</script>");
					} else {

						if (admin.createState(state)) {
							out
									.print("<script>alert('Record saved successfully.');</script>");
							out
									.print("<script>window.location = 'DocumentHelp.jsp?np=statesSetup&stateId="
											+ admin.getStateByCode(stateCode)
													.getStateId() + "';</script>");
						}
					}

				}
				if (!stateId.equals("")) {
					state.setStateId(stateId);
					audit.select(1,
							"SELECT * FROM  AM_GB_STATES  WHERE state_Id = '"
									+ stateId + "'");
					updtst = admin.updateState(state);
					audit.select(2,
							"SELECT * FROM  AM_GB_STATES  WHERE state_Id = '"
									+ stateId + "'");
					audit.logAuditTrail("AM_GB_STATES ", branchcode, userID,
							roleid,hostName,ipAddress,macAddress);
					if (updtst == true) {
						// statusMessage = "Update on record is successful";
						out
								.print("<script>alert('Update on record is successful')</script>");
						out
								.print("<script>window.location = 'DocumentHelp.jsp?np=statesSetup&stateId="
										+ stateId + "';</script>");
						// out.print("<script>window.location =
						// 'manageStates.jsp?status=A'</script>");
					} else {
						// statusMessage = "No changes made on record";
						out
								.print("<script>alert('No changes made on record')</script>");
						out
								.print("<script>window.location = 'DocumentHelp.jsp?np=statesSetup&stateId="
										+ stateId + "';</script>");
					}
				}
			} 
		}
		} catch (Throwable e) {
			e.printStackTrace();
			// statusMessage = "Ensure unique record entry";
			out.print("<script>alert('Ensure unique record entry.')</script>");
			System.err.print(e.getMessage());
			out.print("<script>history.go(-1);</script>");
			
		}
	}

	/**
	 * Returns a short description of the servlet.
	 * 
	 * @return String
	 */
	public String getServletInfo() {
		return "Company Audit Servlet";
	}

}
