package com.magbel.ia.servlet;

import com.magbel.ia.util.ApplicationHelper;

import com.magbel.util.CheckIntegerityContraint;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletConfig;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServlet;

import audit.*;

import com.magbel.ia.vao.Company;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Bolanle M. Sule
 * @version 1.0
 */
public class CompanyAuditServlet extends HttpServlet {
    public CompanyAuditServlet() {
    }

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
    public void service(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {

        response.setContentType("text/html");
        response.setDateHeader("Expires", -1);

        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();

        //String type = request.getParameter("TYPE");
		String statusMessage = "";
		boolean updtst = false;
        //java.sql.Date dt = new java.sql.Date();
        AuditTrailGen  audit = new AuditTrailGen();
        //java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/mm/yyyy");

        String loginID;
		com.magbel.ia.vao.User loginId = (com.magbel.ia.vao.User) session.getAttribute("CurrentUser");
		if (loginId == null) {
			loginID = "Unkown";
		} else {
			loginID = loginId.getUserName();
		}

		String branchcode = loginId.getBranch();
		if (branchcode == null) {
			branchcode = "not set";
		}
		
		String RowId = request.getParameter("companyId");
		String RecordId = request.getParameter("companyCode");
		String RecordValue = request.getParameter("companyName");
		//String SessionId = request.getParameter("sessionTimeout");
		//String LoginID = request.getParameter("companyAddress");
		//String MacAddress = request.getParameter("status");

        
com.magbel.ia.bus.AdminServiceBus admin = new com.magbel.ia.bus.AdminServiceBus();
        
		com.magbel.ia.vao.Company company = new com.magbel.ia.vao.Company();

        String cmdSave = request.getParameter("cmdSave");
        String cmdNext = request.getParameter("cmdNext");
		
		String companyId = request.getParameter("companyId");
        String companyCode = request.getParameter("companyCode");
        String companyName = request.getParameter("companyName");
        String acronym = request.getParameter("acronym");
        String companyAddress = request.getParameter("companyAddress");
        String financialStartDate = request.getParameter("period_start_date");
		
		String financialNoOfMonths = request.getParameter("period_number");
		
		//int financialNoOfMonths = Integer.parseInt("strFNOM");
        //String strFNOM = request.getParameter("period_number");
		//strFNOM = strFNOM.trim();
		
        String financialEndDate = request.getParameter("period_end_date");
        String minimumPassword = request.getParameter("minimumPassword");
        String passwordExpiry = request.getParameter("passwordExpiry");
        String sessionTimeout = request.getParameter("sessionTimeout");
		String status = request.getParameter("status");
		String userId = request.getParameter("userId");
		String logUserAudit = request.getParameter("logUserAudit");
		String transWaitTime = request.getParameter("transWaitTime");
		String collectcashacct = request.getParameter("collectcashacct");
		String collectchqacct = request.getParameter("collectchqacct");
		String collecttrsacct = request.getParameter("collecttrsacct");
		String salarycontrol = request.getParameter("salaryControl");
//		System.out.println("<<<<<<Enforce Aquisition Budget: "+request.getParameter("enforce_acq_budget"));
        String enforceAcqBudget = request.getParameter("enforce_acq_budget");
        if (request.getParameter("enforce_acq_budget") != null ) {
        	enforceAcqBudget = request.getParameter("enforce_acq_budget");
        }
//        System.out.println("<<<<<<Enforce Budget Carry Forward: "+request.getParameter("require_quarterly_pm"));
        String quarterlySurplusCf = "N";
        if (request.getParameter("require_quarterly_pm") != null ) {
        	quarterlySurplusCf = request.getParameter("require_quarterly_pm");
        }
//        System.out.println("<<<<<<Enforce Budget Carry Forward After: "+quarterlySurplusCf);
		
		if((logUserAudit == null) || (logUserAudit.equals(""))){
		logUserAudit = "N";}
	
        
        
        //System.out.println("enforce_pm_budget "+enforcePmBudget);
        //String userId = (String) session.getAttribute("CurrentUser");
       
		company.setCompanyId(companyId);
        company.setAcronym(acronym);
        company.setCompanyAddress(companyAddress);
        company.setCompanyCode(companyCode);
        company.setCompanyName(companyName);
        company.setEnforceAcqBudget(enforceAcqBudget);
        company.setFinancialEndDate(financialEndDate);
       company.setFinancialNoOfMonths(Integer.parseInt(financialNoOfMonths));
        company.setFinancialStartDate(financialStartDate);
       company.setMinimumPassword(Integer.parseInt(minimumPassword));
       company.setPasswordExpiry(Integer.parseInt(passwordExpiry));
        company.setQuarterlySurplusCf(quarterlySurplusCf);
       company.setSessionTimeout(Integer.parseInt(sessionTimeout));
        company.setUserId(userId);
        company.setLogUserAudit(logUserAudit);
		company.setStatus(status);
		company.setTransWaitTime(Double.parseDouble(transWaitTime));
		company.setCollectcashacct(collectcashacct);
		company.setCollectchqacct(collectchqacct);
		company.setCollecttrsacct(collecttrsacct);
		company.setSalaryControl(salarycontrol);
	
/**if(financialNoOfMonths.equals("0")){
													//financialNoOfMonths = "";
									out.print("<script>alert('The financial month can not be 0 .');</script>");									   out.print("<script>history.go(-1);</script>");
									} **/
								
		
 try{
            
            if(cmdSave != null)
				{
				 if (companyId.equals("")) {
					System.out.println("COMPANY ID.........>>>>"+companyId);
					 if (admin.findCompanyByCode(companyCode) != null) {
					 System.out.print("COMPANY CODE IS >>"+companyCode);
							out.print("<script>alert('The Company already exists.');</script>");
							out.print("<script>history.go(-1);</script>");
									
						} else {

							if (admin.createCompany(company)) {
							
								out.print("<script>alert('Record saved successfully.');</script>");
	out.print("<script>window.location = 'DocumentHelp.jsp?np=admin/companyList&status=A'</script>");
												//+ admin.findInstrumentTypeByCode(code).getInstrumentId()+ "&PC=7';
												
							}
						}
					}
                 if (!companyId.equals("")) {
                	 
                	company.setCompanyId(companyId);
					CheckIntegerityContraint intCont = new CheckIntegerityContraint();
          if(intCont.checkReferenceConstraint("MG_GB_COMPANY","company_CODE",companyCode,status))
            {
             out.print("<script>alert('Company Code is being referenced,Integerity Constraint would be violated.')</script>");
			out.print("<script>window.location = 'DocumentHelp.jsp?np=admin/companyDefaults&companyId="+companyId+"&PC=1'</script>");
            }	
			
	 else{	
			audit.select( 1, "SELECT * FROM  MG_GB_COMPANY   WHERE company_Id = '"+ companyId + "'");
                    boolean isupdt = admin.updateCompany(company);
					
			audit.select( 2, "SELECT * FROM  MG_GB_COMPANY  WHERE company_Id = '"+ companyId + "'");
			
					 //updtst = audit.logAuditTrail("MG_GB_COMPANY" ,  branchcode, loginID, companyId);
			updtst = audit.logAuditTrail("MG_GB_COMPANY ", branchcode,loginID, RowId, RecordId, RecordValue);
		    //updtst = audit.updateLoginAudit(SessionId, LoginID, MacAddress); 
			
						if(updtst == true){
						
								out.print("<script>alert('Update on record is successfull')</script>");
								//out.print("<script>window.location = //'DocumentHelp.jsp?np=admin/companyDefaults&companyId="+companyId+"&PC=1'</script>");ssss

out.print("<script>window.location = 'DocumentHelp.jsp?np=admin/companyList&status=A'</script>");								
							}
							
						else 
							{
								
								 out.print("<script>alert('No changes made on record')</script>");
								 out.print("<script>window.location = 'DocumentHelp.jsp?np=admin/companyDefaults&companyId="+companyId+"&PC=1'</script>");
							}
									
					}
				}}
			} 
			catch(Throwable e)
			{
				
				out.print("<script>alert('Ensure unique record entry.')</script>");
				out.print("<script>window.location = 'DocumentHelp.jsp?np=admin/companyDefaults&companyId="+companyId+"&PC=1'</script>");
				e.printStackTrace();
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

}
