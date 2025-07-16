
package com.magbel.admin.servlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.magbel.admin.handlers.AdminHandler;
import com.magbel.util.DatetimeFormat;
import audit.*;


public class CompanyAuditServlet extends HttpServlet {

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
        
        String passwordLimit = request.getParameter("password_limit");
        if(passwordLimit == null)passwordLimit="0";
       
        String minimumPassword = request.getParameter("minimum_password");
        String passwordExpiry = request.getParameter("password_expiry");
        String sessionTimeout = request.getParameter("session_timeout");
        
        int attempt_logon = Integer.parseInt(request.getParameter("log_on"));

        String keepUserLogAudit = "N";
        if (request.getParameter("LOG_USER") != null ) {
        	keepUserLogAudit = request.getParameter("LOG_USER");
        }
        String password_upper = request.getParameter("password_upper");
        String password_lower = request.getParameter("password_lower");
        String password_numeric = request.getParameter("password_numeric");
        String password_special = request.getParameter("password_special");
      
        String userId = (String) session.getAttribute("CurrentUser");
        com.magbel.admin.objects.Company company = new com.magbel.admin.objects.Company();
        company.setAcronym(acronym);
        company.setCompanyAddress(companyAddress);
        company.setCompanyCode(companyCode);
        company.setCompanyName(companyName);
           company.setMinimumPassword(Integer.parseInt(minimumPassword));
        company.setPasswordExpiry(Integer.parseInt(passwordExpiry));
       
        company.setSessionTimeout(Integer.parseInt(sessionTimeout));
        company.setUserId(userId);
       
        company.setLog_on(attempt_logon);
        
        company.setPassword_lower(password_lower);
        company.setPassword_numeric(password_numeric);
        company.setPassword_special(password_special);
        company.setPassword_upper(password_upper);
        company.setPasswordLimit(Integer.parseInt(passwordLimit));
        company.setLogUserAudit(keepUserLogAudit);

        AdminHandler admin = new AdminHandler();
		String roleid =admin.getPrivilegesRole("Company Profile");

        try {
          com.magbel.admin.handlers.CompanyHandler ch = new  com.magbel.admin.handlers.CompanyHandler();
            if (cmdSave != null || cmdNext != null) {
            	if(ch.getCompany()==null)
            	{
            		 if (ch.createCompany(company)) {
                      
                         out.println(
                                 "<script>alert('Update  is successfull');</script>");
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
                                             loginID, roleid,"","");

                if (updtst == true) {
                    //statusMessage = "Update on record is successfull";
                    out.println(
                            "<script>alert('Update on record is successfull');</script>");
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