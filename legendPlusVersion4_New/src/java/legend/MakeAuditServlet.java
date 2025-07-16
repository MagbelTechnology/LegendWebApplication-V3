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

import com.magbel.util.ApplicationHelper;

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
public class MakeAuditServlet extends HttpServlet {
	public MakeAuditServlet() {
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

		String userClass = (String)session.getAttribute("UserClass");
		
		String branchcode = (String) session.getAttribute("UserCenter");
		if (branchcode == null) {
			branchcode = "not set";
		}

		String buttSave = request.getParameter("buttSave");

		String makeId = request.getParameter("makeId");
		// if(makeId == null){makeId = "";}

		// String user = (String)session.getAttribute("CurrentUser");

		String assetMakeCode = request.getParameter("makeCode");
		String assetMake = encodeForHTML(request.getParameter("makeName"));
		String status = request.getParameter("makeStatus");
		String category = request.getParameter("makeCart");
		String userid = (String) session.getAttribute("CurrentUser");
		
		legend.admin.objects.AssetMake am =  new legend.admin.objects.AssetMake();
		am.setAssetMakeCode(assetMakeCode);
		am.setAssetMake(assetMake);
		am.setCategory(category);
		am.setStatus(status);
		am.setUserid(userid);

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
			if (!userClass.equals("NULL") || userClass!=null){
			legend.admin.handlers.CompanyHandler ch = new legend.admin.handlers.CompanyHandler();
			if (buttSave != null) {
				if (makeId.equals("")) {
                                    String assetMakeCodeNew =new ApplicationHelper().getGeneratedId("am_gb_assetMake");
                                    am.setAssetMakeCode(0+assetMakeCodeNew);
                                    if (ch.createAssetMake(am)) { 
						out
								.print("<script>alert('Record saved successfully.')</script>");
						out
						.print("<script>window.location = 'DocumentHelp.jsp?np=assetmakeSetup&makeId="
								+ ch.getAssetMakeByCode(am.getAssetMakeCode()).getAssetMakeId() + "&PC=61'</script>");
					} else {
						System.out
								.println("Error saving record: New record \nfor 'asset make'  with make "
										+ am.getAssetMake() + " could not be created");
						out
								.print("<script>window.location = 'DocumentHelp.jsp?np=assetmakeSetup'</script>");
					}
                                    /* old method
					if (ch.getAssetMakeByCode(assetMakeCode)!=null) {
						out
								.print("<script>alert('Asset Code is in Use.')</script>");
						out
								.print("<script>history.go(-1)</script>");
					}
					else{
					if (ch.createAssetMake(am)) {
						out
								.print("<script>alert('Record saved successfully.')</script>");
						out
						.print("<script>window.location = 'assetmakeSetup.jsp?makeId="
								+ ch.getAssetMakeByCode(assetMakeCode).getAssetMakeId() + "&PC=61'</script>");
					} else {
						System.out
								.println("Error saving record: New record \nfor 'asset make'  with make "
										+ am.getAssetMake() + " could not be created");
						out
								.print("<script>window.location = 'assetmakeSetup.jsp'</script>");
					}
					}
                                        */
				}

				if (!makeId.equals("")) {
					am.setAssetMakeId(makeId);
					audit.select(1,
							"SELECT * FROM  AM_GB_ASSETMAKE   WHERE assetmake_Id = '"
									+ makeId + "'");
					boolean isupdt = ch.updateAssetMake(am);
					audit.select(2,
							"SELECT * FROM  AM_GB_ASSETMAKE   WHERE assetmake_Id = '"
									+ makeId + "'");
					updtst = audit.logAuditTrail("AM_GB_ASSETMAKE", branchcode,
							loginID, makeId,hostName,ipAddress,macAddress);
					if (updtst == true) {
						out
								.print("<script>alert('Update on record is successful')</script>");
						out
								.print("<script>window.location = 'DocumentHelp.jsp?np=assetmakeSetup&makeId="
										+ makeId + "&PC=61'</script>");
						// out.print("<script>window.location =&
						// 'manageMakes.jsp?status=A'</script>");
					} else {
						// statusMessage = "No changes made on record";
						out
								.print("<script>alert('No changes made on record')</script>");
						out
						.print("<script>window.location = 'DocumentHelp.jsp?np=assetmakeSetup&makeId="
								+ makeId + "&PC=61'</script>");
					}
				}
			}
		}
		} catch (Throwable e) {
			e.printStackTrace();
			out.print("<script>alert('Ensure unique record entry.')</script>");
			out
					.print("<script>window.location = 'DocumentHelp.jsp?np=assetmakeSetup'</script>");
			System.err.print(e.getMessage());
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
