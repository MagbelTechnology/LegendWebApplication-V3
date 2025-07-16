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
public class SectionAuditServlet extends HttpServlet {
	public SectionAuditServlet() {
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

		// java.sql.Date dt = new java.sql.Date();
		AuditTrailGen audit = new AuditTrailGen();
		// java.text.SimpleDateFormat sdf = new
		// java.text.SimpleDateFormat("dd/mm/yyyy");
		String statusMessage = "";
		boolean updtst = false;
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

		legend.admin.objects.Section section = new legend.admin.objects.Section();
		legend.admin.handlers.AdminHandler admin = new legend.admin.handlers.AdminHandler();

		String sectionId = request.getParameter("sectionId");
		String sectionStatus = request.getParameter("sectionStatus");
		String buttSave = request.getParameter("buttSave");

		String acronym = request.getParameter("sectionAcronym");

		if (acronym != null) {
			acronym = acronym.toUpperCase();
		}
		String userClass = (String)session.getAttribute("UserClass");
		String section_code = encodeForHTML(request.getParameter("sectionCode"));
		String section_name = encodeForHTML(request.getParameter("sectionName"));
		String section_acronym = acronym;
		String userid = (String) session.getAttribute("CurrentUser");
		section.setSection_code(section_code);
		section.setSection_name(section_name);
		section.setSection_code(section_code);
		section.setUserid(userid);
		section.setSection_status(sectionStatus);
		section.setSection_acronym(section_acronym);
		
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
			
		try {
			 if (!userClass.equals("NULL") || userClass!=null){
			if (buttSave != null) {
				
					if (sectionId.equals("")) {
						if (admin.getSectionByCode(section_code) != null) {
							out
									.print("<script>alert('The section code already exists .');</script>");
							out.print("<script>history.go(-1);</script>");
						} else {

							if (admin.createSection(section)) {
								out
										.print("<script>alert('Record saved successfully.');</script>");
								out
										.print("<script>window.location = 'DocumentHelp.jsp?np=sectionSetup&sectionId="
												+admin.getSectionByCode(section_code).getSection_id() + "';</script>");
							}
						}
					} else if (!sectionId.equals("")) {
						section.setSection_id(sectionId);
						CheckIntegerityContraint intCont = new CheckIntegerityContraint();
						if (intCont.checkReferenceConstraint("AM_GB_USERS",
								"SECTION_CODE", section_code, sectionStatus)) {
							out
									.print("<script>alert('Section Code is used by other records.')</script>");
							out
									.print("<script>window.location = 'DocumentHelp.jsp?np=sectionSetup&sectionId="
											+ sectionId + "'</script>");
						} else {
							// System.out.print(sectionId+"3");
							audit.select(1,
									"SELECT * FROM  AM_AD_SECTION   WHERE section_Id = '"
											+ sectionId + "'");
							boolean isupdt = admin.updateSection(section);
							audit.select(2,
									"SELECT * FROM  AM_AD_SECTION   WHERE section_Id = '"
											+ sectionId + "'");
							updtst = audit.logAuditTrail("AM_AD_SECTION",
									branchcode, loginID, sectionId,hostName,ipAddress,macAddress);
							if (updtst == true) {
								// statusMessage = "Update on record is
								// successfull";
								out
										.print("<script>alert('Update on record is successful')</script>");
								out
										.print("<script>window.location = 'DocumentHelp.jsp?np=sectionSetup&sectionId="
												+ sectionId + "'</script>");
								// out.print("<script>window.location =
								// 'manageBranchs.jsp?status=A'</script>");
							} else {
								// statusMessage = "No changes made on record";
								out
										.print("<script>alert('No changes made on record')</script>");
								out
										.print("<script>window.location = 'DocumentHelp.jsp?np=sectionSetup&sectionId="
												+ sectionId + "'</script>");
							}
						}
					
					}}
		}
		} catch (Throwable e) {
			e.printStackTrace();
			// statusMessage = "Ensure unique record entry";
			out.print("<script>alert('Ensure unique record entry.')</script>");
			// out.print("<script>window.location =
			// 'sectionSetup.jsp?sectionId="+sectionId+"'</script>");
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

