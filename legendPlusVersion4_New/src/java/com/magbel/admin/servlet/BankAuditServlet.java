package com.magbel.admin.servlet;

import com.magbel.util.CheckIntegerityContraint;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletConfig;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServlet;

import com.magbel.admin.objects.Bank;
import com.magbel.admin.handlers.AdminHandler;
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
		String bankCode = request.getParameter("Org_Code");
		String bankName = request.getParameter("Org_Name");
		String address = request.getParameter("address");
		String phone = request.getParameter("phone");
		String email = request.getParameter("email");
		String Fax = request.getParameter("Fax_No");
		String Status = request.getParameter("Status");
		String CreateDate = request.getParameter("CreateDate");
		String Acronym = request.getParameter("Acronym");
		String Domain = request.getParameter("Domain");
		String user_id = request.getParameter("userid");
		String contactPerson = request.getParameter("ContactPerson");			
		String buttSave = request.getParameter("buttSave");			
		//String user_id = (String) session.getAttribute("CurrentUser");
		
		bank.setAddress(address);
		bank.setBankCode(bankCode);
		bank.setBankName(bankName);
		bank.setEmail(email);
		bank.setPhone(phone);
		bank.setFax(Fax);
		bank.setStatus(Status);
		bank.setAcronym(Acronym);
		bank.setDomain(Domain);
		bank.setUser_id(user_id);
		bank.setUser_id(user_id);	
		bank.setorganContact(contactPerson);
		try {
			
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
								"SELECT * FROM  AM_AD_ORGANIZATION  WHERE ORG_CODE = '"
										+ bankCode + "'");
						boolean isupdt = admin.updateBank(bank);
						audit.select(2,
								"SELECT * FROM  AM_AD_ORGANIZATION  WHERE ORG_CODE = '"
								+ bankCode + "'");
						updtst = audit.logAuditTrail("AM_AD_ORGANIZATION",
								branchcode, loginID, bankCode,"","");
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
