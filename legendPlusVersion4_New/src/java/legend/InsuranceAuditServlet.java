package legend;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

import jakarta.servlet.ServletException;

import com.magbel.util.Cryptomanager;

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
public class InsuranceAuditServlet extends HttpServlet {
	public InsuranceAuditServlet() {
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
		legend.admin.objects.Insurance insurance = new legend.admin.objects.Insurance();
		legend.admin.handlers.InsuranceHandler insuranceHandler = new legend.admin.handlers.InsuranceHandler();
		String statusMessage;
		boolean updtst = false;

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
		String userClass = (String)session.getAttribute("UserClass");
		
		Cryptomanager cm = new Cryptomanager();

		String insuranceId = request.getParameter("insuranceId");

		String buttSave = request.getParameter("buttSave");
		String insuranceCode = request.getParameter("insuranceCode");
		String insuranceName = request.getParameter("insuranceName");
		String contactPerson = request.getParameter("contactPerson");
		String contactAddress = request.getParameter("contactAddress");
		String insuranceState = request.getParameter("insuranceState");
		String insurancePhone = request.getParameter("insurancePhone");
		String insuranceFax = request.getParameter("insuranceFax");
		String insuranceEmail = request.getParameter("insuranceEmail");
		String notifydays = request.getParameter("notifyDays");
		String everyDays = request.getParameter("everyDays");
		String accountType = request.getParameter("accountType");
		String accountNumber = request.getParameter("accountNumber");
		String insuranceStatus = request.getParameter("insuranceStatus");
		String insuranceProvince = request.getParameter("insuranceProvince");
		String userId = (String) session.getAttribute("CurrentUser");
		
		insurance.setInsuranceCode(insuranceCode);
		insurance.setInsuranceName(insuranceName);
		insurance.setContactPerson(contactPerson);
		insurance.setContactAddress(contactAddress);
		insurance.setInsuranceState(insuranceState);
		insurance.setInsurancePhone(insurancePhone);
		insurance.setInsuranceFax(insuranceFax);
		insurance.setInsuranceEmail(insuranceEmail);
		insurance.setNotifydays(notifydays);
		insurance.setEveryDays(everyDays);
		insurance.setAccountType(accountType);
		insurance.setAccountNumber(accountNumber);
		insurance.setInsuranceStatus(insuranceStatus);
		insurance.setInsuranceProvince(insuranceProvince);
		insurance.setUserId(userId);
		
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
			if (buttSave != null) {
				if (insuranceId.equals("")) {
					if(insuranceHandler.getInsuranceByCode(insuranceCode)!=null)
					{
						out.print("<script>alert('The Insurance with code already exists .');</script>");
						out.print("<script>history.go(-1);</script>");
					} else {
					if (insuranceHandler.createInsurance(insurance)) {
						
						out
								.print("<script>alert('Record saved successfully.')</script>");
						out
								.print("<script>window.location = 'insuranceCompanies.jsp?insuranceId="
										+ insuranceHandler.getInsuranceByCode(insuranceCode).getInsuranceId()+ "'</script>");
					} else {
						System.out
								.println("Error saving record: New record \nfor 'insurance'  with insurance code "
										+ insuranceCode + " could not be created");
						out
								.print("<script>window.location = 'insuranceCompanies.jsp'</script>");
					}
					}
				} else if (!insuranceId.equals("")) {
					insurance.setInsuranceId(insuranceId);
					audit.select(1,
							"SELECT * FROM  AM_AD_INSURANCE   WHERE insurance_Id = '"
									+ insuranceId + "'");
					boolean isupdt = insuranceHandler.updateInsurance(insurance);
					audit.select(2,
							"SELECT * FROM  AM_AD_INSURANCE   WHERE insurance_Id = '"
									+ insuranceId + "'");
					updtst = audit.logAuditTrail("AM_AD_INSURANCE", branchcode,
							loginID, insuranceId,hostName,ipAddress,macAddress);
					if (updtst == true) {
						// statusMessage = "Update on record is successfull";
						out
								.print("<script>alert('Update on record is successfull')</script>");
						out
								.print("<script>window.location = 'insuranceCompanies.jsp?insuranceId="
										+ insuranceId + "'</script>");
						// out.print("<script>window.location =
						// 'manageBranchs.jsp?status=A'</script>");
					} else {
						// statusMessage = "No changes made on record";
						out
								.print("<script>alert('No changes made on record')</script>");
						out
								.print("<script>window.location = 'insuranceCompanies.jsp?insuranceId="
										+ insuranceId + "'</script>");
					}
				}
			}
		}
		} catch (Throwable e) {
			e.printStackTrace();
			statusMessage = "Ensure unique record entry.";
			out.print("<script>alert('Ensure unique record entry.')</script>");
			out
					.print("<script>window.location = 'insuranceCompanies.jsp?insuranceId="
							+ insuranceId + "'</script>");
			System.err.print(e.getMessage());
		}
	}
}
