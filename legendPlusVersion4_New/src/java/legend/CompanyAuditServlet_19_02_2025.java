package legend;

import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import legend.admin.handlers.AdminHandler;

import com.magbel.util.DatetimeFormat;

import audit.*;

//import legend.AutoIDSetup;

//import java.sql.

public class CompanyAuditServlet_19_02_2025 extends HttpServlet {

    DatetimeFormat dtf = new DatetimeFormat();

    /**
     * Initializes the servlet.
     *
     * @param config ServletConfig
     * @throws ServletException
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    /** Destroys the servlet.
     */
    public void destroy() {

    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException
     * @throws IOException
     */
    public void service(HttpServletRequest request,
                        HttpServletResponse response) throws
            ServletException, IOException {

        response.setContentType("text/html");
        response.setDateHeader("Expires", -1);

        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();

        //String type = request.getParameter("TYPE");
        String statusMessage = "";
        boolean updtst = false;

        //java.sql.Date dt = new java.sql.Date();
        AuditTrailGen audit = new AuditTrailGen();
        //java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/mm/yyyy");

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

        String cmdSave = request.getParameter("cmdSave");
        String cmdNext = request.getParameter("cmdNext");
        String companyCode = request.getParameter("company_code");
        String companyName = request.getParameter("company_name");
        String acronym = request.getParameter("short_name");
        String companyAddress = request.getParameter("registered_address");
        String vatRate = request.getParameter("vat_rate");
        String whtRate = request.getParameter("wht_rate");
        String comp_delimiter = request.getParameter("comp_delimiter");
        String passwordLimit = request.getParameter("password_limit");
        if(passwordLimit == null)passwordLimit="0";

        if (comp_delimiter == null)
        {
        	comp_delimiter="";
        }

        String financialStartDate = request.getParameter("period_start_date");
        String financialNoOfMonths = request.getParameter("period_number");
        String financialEndDate = request.getParameter("period_end_date");

        String minimumPassword = request.getParameter("minimum_password");
        String passwordExpiry = request.getParameter("password_expiry");
        String sessionTimeout = request.getParameter("session_timeout");
        String enforceAcqBudget = "N";
        int attempt_logon = Integer.parseInt(request.getParameter("log_on"));

        if (request.getParameter("enforce_acq_budget") != null ) {
        	enforceAcqBudget = request.getParameter("enforce_acq_budget");
        }
        String enforcePmBudget = "N";
        if (request.getParameter("enforce_pm_budget") != null ) {
        	enforcePmBudget = request.getParameter("enforce_pm_budget");
        }
        String enforceFuelAllocation = "N";
        if (request.getParameter("enforce_fuel_allocation") != null ) {
        	enforceFuelAllocation = request.getParameter("enforce_fuel_allocation");
        }
        String requireQuarterlyPM = "N";
        if (request.getParameter("require_quarterly_pm") != null ) {
        	requireQuarterlyPM = request.getParameter("require_quarterly_pm");
        }
        String quarterlySurplusCf = "N";
        if (request.getParameter("quarterly_surplus_cf") != null ) {
        	quarterlySurplusCf = request.getParameter("quarterly_surplus_cf");
        }


        String keepUserLogAudit = "N";
        if (request.getParameter("LOG_USER") != null ) {
        	keepUserLogAudit = request.getParameter("LOG_USER");
        }


        String password_upper = request.getParameter("password_upper");
        String password_lower = request.getParameter("password_lower");
        String password_numeric = request.getParameter("password_numeric");
        String password_special = request.getParameter("password_special");
        String userClass = (String)session.getAttribute("UserClass");

        System.out.println("enforce_pm_budget "+enforcePmBudget);
        String userId = (String) session.getAttribute("CurrentUser");
        legend.admin.objects.Company company = new legend.admin.objects.Company();
        company.setAcronym(acronym);
        company.setCompanyAddress(companyAddress);
        company.setCompanyCode(companyCode);
        company.setCompanyName(companyName);
        company.setEnforceAcqBudget(enforceAcqBudget);
        company.setEnforceFuelAllocation(enforceFuelAllocation);
        company.setEnforcePmBudget(enforcePmBudget);
        company.setFinancialEndDate(financialEndDate);
        company.setFinancialNoOfMonths(Integer.parseInt(financialNoOfMonths));
        company.setFinancialStartDate(financialStartDate);
        company.setMinimumPassword(Integer.parseInt(minimumPassword));
        company.setPasswordExpiry(Integer.parseInt(passwordExpiry));
        company.setQuarterlySurplusCf(quarterlySurplusCf);
        company.setRequireQuarterlyPM(requireQuarterlyPM);
        company.setSessionTimeout(Integer.parseInt(sessionTimeout));
        company.setUserId(userId);
        company.setVatRate(Double.parseDouble(vatRate));
        company.setWhtRate(Double.parseDouble(whtRate));
        company.setLog_on(attempt_logon);
        company.setComp_delimiter(comp_delimiter);
        company.setPassword_lower(password_lower);
        company.setPassword_numeric(password_numeric);
        company.setPassword_special(password_special);
        company.setPassword_upper(password_upper);
        company.setPasswordLimit(Integer.parseInt(passwordLimit));
        company.setLogUserAudit(keepUserLogAudit);

        AdminHandler admin = new AdminHandler();
		String roleid =admin.getPrivilegesRole("Company Profile");

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
			//System.out.println("Network address : " + network.getHardwareAddress());
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
          legend.admin.handlers.CompanyHandler ch = new  legend.admin.handlers.CompanyHandler();
            if (cmdSave != null || cmdNext != null) {
            	if(ch.getCompany()==null)
            	{
            		 if (ch.createCompany(company)) {
                         //statusMessage = "Update on record is successfull";
//                         out.println("<script>alert('Update  is successfull');</script>");
                         if(cmdNext != null){
                         out.println(
                                 "<script>window.location = 'DocumentHelp.jsp?np=assetManagerInfo.jsp?cc=" +
                                 companyCode + "'</script>");}
                         if(cmdSave != null){
                             out.println(
                                     "<script>window.location = 'DocumentHelp.jsp?np=companyDefaults.jsp?cc=" +
                                     companyCode + "'</script>");}
                     }
            	}
                audit.select(1,
                        "SELECT * FROM  AM_GB_COMPANY  WHERE company_code = '" +
                        companyCode + "'");
                boolean isupdt = ch.updateCompany(company);
                audit.select(2,
                        "SELECT * FROM  AM_GB_COMPANY  WHERE company_code = '" +
                        companyCode + "'");
                /*
                updtst = audit.logAuditTrail("AM_GB_COMPANY", branchcode,
                                             loginID, companyCode);
                */
                updtst = audit.logAuditTrail("AM_GB_COMPANY", branchcode,
                                             loginID, roleid,hostName,ipAddress,macAddress);

                if (updtst == true) {
                    //statusMessage = "Update on record is successfull";
 //                   out.println("<script>alert('Update on record is successfull');</script>");
                    if(cmdNext != null){
                        out.println(
                                "<script>window.location = 'DocumentHelp.jsp?np=assetManagerInfo.jsp?cc=" +
                                companyCode + "'</script>");}
                        if(cmdSave != null){
                            out.println(
                                    "<script>window.location = 'DocumentHelp.jsp?np=companyDefaults.jsp?cc=" +
                                    companyCode + "'</script>");}
                } else {
                    //statusMessage = "No changes made on record";
                    out.print(
                            "<script>alert('No changes made on record')</script>");
                    if(cmdNext != null){
                        out.println(
                                "<script>window.location = 'DocumentHelp.jsp?np=assetManagerInfo.jsp?cc=" +
                                companyCode + "'</script>");}
                        if(cmdSave != null){
                            out.println(
                                    "<script>window.location = 'DocumentHelp.jsp?np=companyDefaults.jsp?cc=" +
                                    companyCode + "'</script>");}
                }
            }
        }
        	 out.println(
                     "<script>window.location = 'sessionTimeOut.jsp'</script>");
        } catch (Exception t) {
            t.printStackTrace();
            //statusMessage = "Error! Record not saved.";
            out.println("<script>alert('Error! Record not saved.');</script>");
            out.println(
                    "<script>window.location = 'DocumentHelp.jsp?np=companyDefaults.jsp'</script>");
        }


    }

    public String getServletInfo() {
        return "Company Audit Servlet";
    }


}
