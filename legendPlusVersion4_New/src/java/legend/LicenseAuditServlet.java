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
public class LicenseAuditServlet extends HttpServlet {
	public LicenseAuditServlet() {
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

		AuditTrailGen audit = new AuditTrailGen();

		String statusMessage = "";
		boolean updtst = false;

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

		String licenseId = request.getParameter("licenseId");

		String buttSave = request.getParameter("buttSave");

		String licenseCode = request.getParameter("licenseCode");
		String licenseName = request.getParameter("licenseName");
		String notifyDays = request.getParameter("notifyDays");
		String everyDays = request.getParameter("everyDays");
		String accountType = request.getParameter("accountType");
		String suspenseAcct = request.getParameter("suspenseAcct");
		String licenseStatus = request.getParameter("licenseStatus");
		String userId = (String) session.getAttribute("CurrentUser");

		legend.admin.objects.LicenseType licensetype = new legend.admin.objects.LicenseType();

		// licensehandle.setLicenseId(licenseId);
		licensetype.setLicenseCode(licenseCode);
		licensetype.setLicenseName(licenseName);
		licensetype.setNotifyDays(notifyDays);
		licensetype.setEveryDays(everyDays);
		licensetype.setAccountType(accountType);
		licensetype.setSuspenseAcct(suspenseAcct);
		licensetype.setLicenseStatus(licenseStatus);
		licensetype.setUserId(userId);

		// java.sql.Date effectiveDate = new
		// java.sql.Date(sdf.parse(dt.textDate()).getTime());
		legend.admin.handlers.LicenseTypeHandler licensehandle = new legend.admin.handlers.LicenseTypeHandler();

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
			
		// System.out.print(licenseId+"1");
		try {
			 if (!userClass.equals("NULL") || userClass!=null){
			if (buttSave != null) {
				if (licenseId.equals("")) {
					if (licensehandle
							.getLicenseTypeByLicenseCode(licenseCode)!= null) {
						out.print("<script>alert('The Licence code already exists .')</script>");
						out.print("<script>history.go(-1);</script>");
					} else {
					if (licensehandle.createLicenseType(licensetype)) {
						// statusMessage = "Record saved successfully";
						out
								.print("<script>alert('Record saved successfully.')</script>");
						out
								.print("<script>window.location = 'licenseTypes.jsp?licenseId="
										+ (licensehandle
												.getLicenseTypeByLicenseCode(licenseCode))
												.getLicenseId()
										+ "'</script>");
					} else {
						System.out
								.println("Error saving record: New record \nfor 'license'  with license name  "
										+ licensetype.getLicenseName()
										+ " could not be created");
						out
								.print("<script>history.go(-1)</script>");
					}
					}
				} else if (!licenseId.equals("")) {
					licensetype.setLicenseId(licenseId);
					
					audit.select(1,
							"SELECT * FROM  AM_AD_LICENSETYPE   WHERE license_Id = '"
									+ licenseId + "'");
					boolean isupdt = licensehandle
							.updateLicenseType(licensetype);
					audit.select(2,
							"SELECT * FROM  AM_AD_LICENSETYPE   WHERE license_Id = '"
									+ licenseId + "'");
					updtst = audit.logAuditTrail("AM_AD_LICENSETYPE ",
							branchcode, Integer.parseInt(userId), licenseId,hostName,ipAddress,macAddress);
					if (updtst == true) {
						// statusMessage = "Update on record is successfull";
						out
								.print("<script>alert('Update on record is successfull')</script>");
						out
								.print("<script>window.location = 'licenseTypes.jsp?licenseId="
										+ licenseId + "'</script>");
						// out.print("<script>window.location =
						// 'manageBranchs.jsp?status=A'</script>");
					} else {
						// statusMessage = "No changes made on record";
						out
								.print("<script>alert('No changes made on record')</script>");
						out
						.print("<script>window.location = 'licenseTypes.jsp?licenseId="
								+ licenseId + "'</script>");
					}
				}
			}
		}
		} catch (Throwable e) {
			e.printStackTrace();
			// statusMessage = "Ensure unique record entry";
			out.print("<script>alert('Ensure unique record entry.')</script>");
			out.print("<script>history.back()</script>");
			System.err.print(e.getMessage());
		}
	}
}
