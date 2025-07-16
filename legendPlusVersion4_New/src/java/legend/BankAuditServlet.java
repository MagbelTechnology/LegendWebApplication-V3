package legend;

import com.magbel.util.CheckIntegerityContraint;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletConfig;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServlet;

import legend.admin.objects.Bank;
import legend.admin.handlers.AdminHandler;
import audit.*;

 

public class BankAuditServlet extends HttpServlet {

	public BankAuditServlet() {
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
 
	public void destroy() {

	} 
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		response.setDateHeader("Expires", -1);

		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
 

		String statusMessage = "";
		boolean updtst = false;
 
		AuditTrailGen audit = new AuditTrailGen(); 
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

		Bank bank = new Bank();
		AdminHandler admin = new AdminHandler();
		String mtid = request.getParameter("mtid");
		String bankCode = request.getParameter("bankCode");
		String bankName = request.getParameter("bankName");
		String address = request.getParameter("address");
		String phone = request.getParameter("phone");
		String email = request.getParameter("email");
 
		String buttSave = request.getParameter("buttSave");

		String user_id = (String) session.getAttribute("CurrentUser");
		String userClass = (String) session.getAttribute("UserClass");
		bank.setAddress(address);
		bank.setBankCode(bankCode);
		bank.setBankName(bankName);
		bank.setEmail(email);
		bank.setPhone(phone); 
		
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
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
			}
			String macAddress = sb.toString();
			System.out.println(sb.toString());
		
		try {
			if (!userClass.equals("NULL") || userClass!=null){
			if (buttSave != null) {

				if (mtid.equals("")||mtid==null) {
				if (admin.getBankByCode(bankCode) != null) {
					out.print("<script>alert('The Bank code already exists .')</script>");
					out.print("<script>history.go(-1);</script>");
				} else {

					if (admin.createBank(bank)) {							
						out.print("<script>alert('Record saved successfully.')</script>");
						out.print("<script>window.location = 'DocumentHelp.jsp?np=bankSetup&bankCode="
										+ bankCode
										+ "&PC=9'</script>");
					}
			  } 
				}
				
				  else   {
						// System.out.print(deptId+"3");
						audit.select(1,
								"SELECT * FROM  AM_AD_BANK  WHERE BANKCODE = '"
										+ bankCode + "'");
						boolean isupdt = admin.updateBank(bank);
						audit.select(2,
								"SELECT * FROM  AM_AD_BANK  WHERE BANKCODE = '"
								+ bankCode + "'");
						updtst = audit.logAuditTrail("AM_AD_BANK",
								branchcode, loginID, bankCode,hostName,ipAddress,macAddress);
						
						if (updtst == true) {
							
							out.print("<script>alert('Update on record is successfull')</script>");
							
							out.print("<script>window.location = 'DocumentHelp.jsp?np=bankSetup&bankCode="
											+ bankCode + "&PC=9'</script>");
							
						} else {
							
							
							out.print("<script>alert('No changes made on record')</script>");
							out.print("<script>window.location = 'DocumentHelp.jsp?np=bankSetup&bankCode="
									+ bankCode + "&PC=9'</script>");
						}
					}

			}  
		}
		} catch (Throwable e) {
			e.printStackTrace();
			// statusMessage = "Ensure unique record entry.";
			out.print("<script>alert('Ensure unique record entry.')</script>");
			out.print("<script>window.location = 'DocumentHelp.jsp?np=bankSetup&bankCode="
					+ bankCode + "&PC=9'</script>");
			System.err.print(e.getMessage());
		}
	}
}
