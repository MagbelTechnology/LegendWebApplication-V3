
package com.magbel.legend.servlet;

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
import legend.admin.objects.ComplaintCategory;


public class ComplaintCategoryServlet extends HttpServlet {
	public ComplaintCategoryServlet() {
	}


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

		int userID;
		String userId = (String) session.getAttribute("CurrentUser");
		if (userId == null) {
			userID = 0;
		} else {
			userID = Integer.parseInt(userId);
		}

		String branchcode = (String) session.getAttribute("UserCenter");
		if (branchcode == null) {
			branchcode = "not set";
		}

		String buttSave = request.getParameter("buttSave");
		String complaintId = request.getParameter("complaintId");
		
		String complaintCode = request.getParameter("complaintCode");
		String complaintName = request.getParameter("complaintName");
		String complaintStatus = request.getParameter("complaintStatus");
		
        String loginId = (String)session.getAttribute("CurrentUser");
        int loginID = Integer.parseInt(loginId);
        String computerName = null;
        String remoteAddress = request.getRemoteAddr();
        InetAddress inetAddress = InetAddress.getByName(remoteAddress);
        computerName = inetAddress.getHostName();
        if(computerName.equalsIgnoreCase("localhost"))
        {
            computerName = InetAddress.getLocalHost().getCanonicalHostName();
        }
        String hostName = "";
        if(hostName.equals(request.getRemoteAddr()))
        {
            InetAddress addr = InetAddress.getByName(request.getRemoteAddr());
            hostName = addr.getHostName();
        }
        if(InetAddress.getLocalHost().getHostAddress().equals(request.getRemoteAddr()))
        {
            hostName = "Local Host";
        }
        InetAddress ip = InetAddress.getLocalHost();
        String ipAddress = ip.getHostAddress();
        NetworkInterface network = NetworkInterface.getByInetAddress(ip);
        byte mac[] = network.getHardwareAddress();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < mac.length; i++)
        {
            sb.append(String.format("%02X%s", new Object[] {
                Byte.valueOf(mac[i]), i >= mac.length - 1 ? "" : "-"
            }));
        }
        
        String macAddress = sb.toString();
        System.out.println(sb.toString());
        
		legend.admin.objects.ComplaintCategory complaintCategory = new legend.admin.objects.ComplaintCategory();

		complaintCategory.setcomplaintCode(complaintCode);
		complaintCategory.setcomplaintName(complaintName);
		complaintCategory.setcomplaintStatus(complaintStatus);
		complaintCategory.setUserId(userId);

		legend.admin.handlers.AdminHandler admin = new legend.admin.handlers.AdminHandler();
                String roleid =admin.getPrivilegesRole("Complaint Category");
		try {

			if (buttSave != null) {
				if (complaintId.equals("")) {
					if (admin.getStateByCode(complaintCode) != null) {
						out
								.print("<script>alert('The Complaint code already exists .');</script>");
						out.print("<script>history.go(-1);</script>");
					} else {

						if (admin.createComplaintCategory(complaintCategory)) {
							out
									.print("<script>alert('Record saved successfully.');</script>");
							out
									.print("<script>window.location = 'DocumentHelp.jsp?np=hdCategoryComplaintSetup&complaintId="
											+ admin.getCategoryCodeByCode(complaintCode).getcomplaintId() + "';</script>");
						}
					}

				}
				if (!complaintId.equals("")) {
					complaintCategory.setcomplaintId(complaintId);
					audit.select(1,
							"SELECT * FROM  HD_COMPLAIN_CATEGORY  WHERE complain_id = '"
									+ complaintId + "'");
					updtst = admin.updateComplaintCategory(complaintCategory);
					audit.select(2,
							"SELECT * FROM  HD_COMPLAIN_CATEGORY  WHERE complain_id = '"
									+ complaintId + "'");
//					audit.logAuditTrail("HD_COMPLAIN_CATEGORY ", branchcode, userID,
//							roleid);
					updtst = audit.logAuditTrail("AM_GB_SLA", branchcode, loginID, roleid, hostName, ipAddress, macAddress);
					if (updtst == true) {
				
						out
								.print("<script>alert('Update on record is successfull')</script>");
						out
								.print("<script>window.location = 'DocumentHelp.jsp?np=hdCategoryComplaintSetup&complaintId="
										+ complaintId + "';</script>");
	
					} else {
					
						out
								.print("<script>alert('No changes made on record')</script>");
						out
								.print("<script>window.location = 'DocumentHelp.jsp?np=hdCategoryComplaintSetup&complaintId="
										+ complaintId + "';</script>");
					}
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
	
			out.print("<script>alert('Ensure unique record entry.')</script>");
			System.err.print(e.getMessage());
			out.print("<script>history.go(-1);</script>");

		}
	}

	public String getServletInfo() {
		return "Company Audit Servlet";
	}

}
