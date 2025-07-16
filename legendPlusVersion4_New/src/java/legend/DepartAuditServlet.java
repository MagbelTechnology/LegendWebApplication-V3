package legend;

import com.magbel.util.CheckIntegerityContraint;

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
import legend.admin.objects.Department;
import legend.admin.handlers.AdminHandler;
import audit.*;

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

public class DepartAuditServlet extends HttpServlet {

	public DepartAuditServlet() {
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

		Department dept = new Department();
		AdminHandler admin = new AdminHandler();
		String deptId = encodeForHTML(request.getParameter("deptId"));

		String deptStatus = request.getParameter("deptStatus");

		String buttSave = request.getParameter("buttSave");

		String acronym = encodeForHTML(request.getParameter("deptAcronym"));

		if (acronym != null) {
			acronym = acronym.toUpperCase();
		}

		String dept_code = encodeForHTML(request.getParameter("deptCode"));

		String dept_name = encodeForHTML(request.getParameter("deptName"));

		String dept_acronym = acronym;

		String dept_status = request.getParameter("deptStatus");

		String user_id = (String) session.getAttribute("CurrentUser");
		dept.setDept_code(dept_code);
		dept.setDept_name(dept_name);
		dept.setDept_acronym(dept_acronym);
		dept.setDept_status(dept_status);
		dept.setUser_id(user_id);
		// System.out.print(deptId+"1");
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
			
		try {
			
			if (buttSave != null) {

				if (deptId.equals("")) {
					if (admin.getDeptByDeptCode(dept_code) != null) {
						out.print("<script>alert('The Department code already exists .')</script>");
						out.print("<script>history.go(-1);</script>");
					} else {

						if (admin.createDepartment(dept)) {	
							String deptId_Result = admin.getDeptByDeptCode(dept_code).getDept_id();
							System.out.println("<<<< deptId_Result: " + deptId_Result);
							out.print("<script>alert('Record saved successfully.')</script>");
							out.print("<script>window.location = 'DocumentHelp.jsp?np=departSetup?deptId="
											+ admin.getDeptByDeptCode(dept_code).getDept_id()
											+ "&PC=9'</script>");
						}
				}
					}else if (!deptId.equals("")) {
						dept.setDept_id(deptId);
					CheckIntegerityContraint intCont = new CheckIntegerityContraint();
					if (intCont.checkReferenceConstraint("AM_GB_USERS",
							"DEPT_CODE", dept_code, deptStatus)) {
						out
								.print("<script>alert('Departmnet Code is being referenced,Integerity Constraint would be violated.')</script>");
						out
								.print("<script>window.location = 'DocumentHelp.jsp?np=departSetup&deptId="
										+ deptId + "&PC=9'</script>");
					} else {
						// System.out.print(deptId+"3");
						audit.select(1,
								"SELECT * FROM  AM_AD_DEPARTMENT  WHERE dept_Id = '"
										+ deptId + "'");
						boolean isupdt = admin.updateDepartment(dept);
						audit.select(2,
								"SELECT * FROM  AM_AD_DEPARTMENT  WHERE dept_Id = '"
										+ deptId + "'");
						updtst = audit.logAuditTrail("AM_AD_DEPARTMENT",
								branchcode, loginID, deptId,hostName,ipAddress,macAddress);
						if (updtst == true) {  
							out
									.print("<script>alert('Update on record is successfull')</script>");
							out
									.print("<script>window.location = 'DocumentHelp.jsp?np=departSetup&deptId="
											+ deptId + "&PC=9'</script>");
							
						} else {
							
							out
									.print("<script>alert('No changes made on record')</script>");
							out
									.print("<script>window.location = 'DocumentHelp.jsp?np=departSetup&deptId="
											+ deptId + "&PC=9'</script>");
						}
					}
				}

			} else {
				// statusMessage = "Error saving! Select a branch.";
				out
						.print("<script>alert('Error saving! Invalid Department.')</script>");
				out
						.print("<script>window.location = 'DocumentHelp.jsp?np=departSetup'</script>");
			}

		} catch (Throwable e) {
			e.printStackTrace();
			// statusMessage = "Ensure unique record entry.";
			out.print("<script>alert('Ensure unique record entry.')</script>");
			out.print("<script>window.location = 'DocumentHelp.jsp?np=departSetup?deptId="
					+ deptId + "&PC=9'</script>");
			System.err.print(e.getMessage());
		}
	}
	
	private String encodeForHTML(String s) {
		StringBuilder out = new StringBuilder(Math.max(16, s.length()));
	    for (int i = 0; i < s.length(); i++) {
	        char c = s.charAt(i);
	        if (c > 127 || c == '"' || c == '\'' || c == '<' || c == '>' || c == '&') {
	            out.append("&#");
	            out.append((int) c);
	            out.append(';');
	        } else {
	            out.append(c);
	        }
	    }
	    return out.toString();
}
}