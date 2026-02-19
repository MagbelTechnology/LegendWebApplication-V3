package legend;

import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Date;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import legend.admin.handlers.AdminHandler;
import magma.AssetRecordsBean;

import com.magbel.util.DatetimeFormat;

import audit.*;

//import legend.AutoIDSetup;

//import java.sql.

public class CompanyAuditServlet extends HttpServlet {

    DatetimeFormat dtf = new DatetimeFormat();
    com.magbel.util.DatetimeFormat df;
    private AssetRecordsBean ad;
    
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
        String recId = "";
        //java.sql.Date dt = new java.sql.Date();
        AuditTrailGen audit = new AuditTrailGen();
        //java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/mm/yyyy");
        com.magbel.util.ApplicationHelper appHelper = new com.magbel.util.ApplicationHelper();
        
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
//        String vatRate = request.getParameter("vat_rate");
//        String whtRate = request.getParameter("wht_rate");
//        String comp_delimiter = request.getParameter("comp_delimiter");
        String passwordLimit = request.getParameter("password_limit");
    	String singleApproval = request.getParameter("singleApproval");
    	String supervisor = request.getParameter("supervisor");
    	String numOfTransactionLevel = request.getParameter("numOfTransactionLevel");
    	String compRecId = request.getParameter("compRecId");
        if(passwordLimit == null)passwordLimit="0";

//        if (comp_delimiter == null)
//        {
//        	comp_delimiter="";
//        }
        
//        String financialStartDate = request.getParameter("period_start_date");
//        String financialNoOfMonths = request.getParameter("period_number");
//        String financialEndDate = request.getParameter("period_end_date");
//        System.out.println("======>>>>>>>financialStartDate: "+financialStartDate+"    financialNoOfMonths: "+financialNoOfMonths+"  financialEndDate: "+financialEndDate);

        String minimumPassword = request.getParameter("minimum_password");
        String passwordExpiry = request.getParameter("password_expiry");
        String sessionTimeout = request.getParameter("session_timeout");
        String proofsessionTimeout = request.getParameter("proof_session_timeout");
      //  System.out.println("======>>>>>>>proofsessionTimeout: "+proofsessionTimeout+"    branchcode: "+branchcode);
        if(proofsessionTimeout == null || proofsessionTimeout =="null") {proofsessionTimeout = "0";}
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
        System.out.println("password_lower "+password_lower);
        String password_numeric = request.getParameter("password_numeric");
        String password_special = request.getParameter("password_special");
        String userClass = (String)session.getAttribute("UserClass");
        
        String systemDate = request.getParameter("system_date");
        String databaseName = request.getParameter("databaseName");
		String processingDate = request.getParameter("processing_date");
		String processingFrequency = request.getParameter("processing_frequency");
		String nextProcessingDate = request.getParameter("next_processing_date"); 
	//	System.out.println("========>nextProcessingDate: "+nextProcessingDate+"     processingDate: "+processingDate+"    processingFrequency: "+processingFrequency);
		//System.out.println("From Front End thirdpartytransaction: "+request.getParameter("thirdpartytransaction"));
		String thirdpartytransaction = "N"; // default
		String[] values = request.getParameterValues("thirdpartytransaction");
		if (values != null) {
		    for (String val : values) {
		        if ("Y".equals(val)) {
		            thirdpartytransaction = "Y";
		            break;
		        }
		    }
		}
	//	System.out.println("thirdpartytransaction: "+thirdpartytransaction);
		String raiseEntry = "N";  // default value
		String[] raiseEntryValues = request.getParameterValues("raiseEntry");
		if (raiseEntryValues != null) {
		    for (String val : raiseEntryValues) {
		        if ("Y".equals(val)) {
		            raiseEntry = "Y";
		            break;
		        }
		    }
		}
	//	System.out.println("raiseEntry: "+raiseEntry);
		
		String sbuRequired = "N";
		if (request.getParameter("sbu_required") != null) {
			sbuRequired = request.getParameter("sbu_required");
		}
		String sbuLevel = request.getParameter("sbu_level");
    //    System.out.println("enforce_pm_budget "+enforcePmBudget);
        String userId = (String) session.getAttribute("CurrentUser");
        legend.admin.objects.Company company = new legend.admin.objects.Company();
        company.setAcronym(acronym);
        company.setCompanyAddress(companyAddress);
        company.setCompanyCode(companyCode);
        company.setCompanyName(companyName);
        company.setEnforceAcqBudget(enforceAcqBudget);
        company.setEnforceFuelAllocation(enforceFuelAllocation);
        company.setEnforcePmBudget(enforcePmBudget);
//        company.setFinancialEndDate(financialEndDate);
//        company.setFinancialNoOfMonths(Integer.parseInt(financialNoOfMonths));
//        company.setFinancialStartDate(financialStartDate);
        company.setMinimumPassword(Integer.parseInt(minimumPassword));
        company.setPasswordExpiry(Integer.parseInt(passwordExpiry));
        company.setQuarterlySurplusCf(quarterlySurplusCf);
        company.setRequireQuarterlyPM(requireQuarterlyPM);
        company.setSessionTimeout(Integer.parseInt(sessionTimeout));
        company.setUserId(userId);
//        company.setVatRate(Double.parseDouble(vatRate));
//        company.setWhtRate(Double.parseDouble(whtRate));
        company.setLog_on(attempt_logon);
//        company.setComp_delimiter(comp_delimiter);
        company.setPassword_lower(password_lower);
        company.setPassword_numeric(password_numeric);
        company.setPassword_special(password_special);
        company.setPassword_upper(password_upper);
        System.out.println("passwordLimit "+passwordLimit);
        company.setPasswordLimit(Integer.parseInt(passwordLimit));
        company.setLogUserAudit(keepUserLogAudit);
        company.setProofSessionTimeout(Integer.parseInt(proofsessionTimeout));
        company.setThirdpartytransaction(thirdpartytransaction);
        company.setRaiseEntry(raiseEntry);
        company.setSysDate(systemDate);
        company.setDatabaseName(databaseName);
        company.setSbuRequired(sbuRequired);
        company.setSbuLevel(sbuLevel);
        company.setNextProcessingDate(nextProcessingDate);
        company.setProcessingDate(processingDate);
        company.setProcessingFrequency(processingFrequency);
      
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
			legend.admin.handlers.SecurityHandler sechanle = new legend.admin.handlers.SecurityHandler();
			
			String macAddress = sb.toString();
			System.out.println(sb.toString());
			
			legend.admin.objects.User usr = sechanle.getUserByUserID(loginId);
			 String user_Name = usr.getUserName();
			 String branchuser_NameRestrict = usr.getBranchRestrict();
			 String User_Restrict = usr.getDeptRestrict();
			 String departCode = usr.getDeptCode();
			 String branch = usr.getBranch();
			
        try {
			df = new com.magbel.util.DatetimeFormat();
			ad = new AssetRecordsBean();
			
        	 if (!userClass.equals("NULL") || userClass!=null){
        		     			
          legend.admin.handlers.CompanyHandler ch = new  legend.admin.handlers.CompanyHandler();
          System.out.println("=====branch: "+branch+"  ====departCode: "+departCode+"   ==user_Name "+user_Name);
          java.util.ArrayList approvelist =ad.getApprovalsId(branch,departCode,user_Name);
          
        		  
			Date tranDate = new java.util.Date();
			String transDate = df.formatDate(tranDate);
			System.out.println("=====tranDate: "+tranDate+"  ====cmdSave: "+cmdSave+"   ==cmdNext "+cmdNext);
            if (cmdSave != null || cmdNext != null) {
//            if (cmdSave != null) {
//            	System.out.println("=====ch.getCompany(): "+ch.getCompany());
            	if(ch.getCompany()==null && cmdSave != null)
            	{
            		recId = ch.createCompanyTmp(company);
            		System.out.println("=====>>>>>>>>recId First: "+recId);
//            		 if (ch.createCompanyTmp(company)) {
            		 if (!recId.equalsIgnoreCase("")) {          			 
                         //statusMessage = "Update on record is successfull";
//                         out.println("<script>alert('Update  is successfull');</script>");
							statusMessage = "Record successfully  Sent for Approval";
							String[] pa = new String[12];

//							System.out.println("=====transDate: "+transDate);
							pa[0]=recId; pa[1]= userId; pa[2]=supervisor; pa[3]="0"; pa[4]= "";
							pa[5]= companyName; pa[6]= transDate; pa[7]= branchcode; pa[8]="PENDING"; pa[9]="Company Profile"; pa[10]="P";pa[11]=numOfTransactionLevel;
//							System.out.println("=====recId: "+recId+"     =====userId: "+userId);	
							if(userId.equals("")){userId = "0";}
							if(singleApproval.equalsIgnoreCase("Y")){
//							ad.setPendingTrans(ad.setApprovalData(venId),"1",Integer.parseInt(venId));
							ad.setPendingTransAdmin(pa,"14",Integer.parseInt(recId),"I");
//							ad.setPendingTransArchive(pa,"14",Integer.parseInt(recId),Integer.parseInt(recId));									
							}
							   if(singleApproval.equalsIgnoreCase("N")){
								   pa[8]="PENDING";
							  		String mtid = appHelper.getGeneratedId("am_asset_approval");
//							   		System.out.println("approvelist.size()#$$$$$$$$$$$ "+approvelist.size());
							   	 for(int j=0;j<approvelist.size();j++)
							     {  
								  	legend.admin.objects.User usrInfo = (legend.admin.objects.User)approvelist.get(j);   	 
									String supervisorId =  usrInfo.getUserId();
									String mailAddress = usrInfo.getEmail();
									String supervisorName = usrInfo.getUserName();
									String supervisorfullName = usrInfo.getUserFullName();
//									System.out.println("SupervisorId#$$$$$$$$$$$ "+supervisorId);
							  		ad.setPendingTransAdminMultiApp(pa,"14",Integer.parseInt(recId),"I",supervisorId,mtid);
							  	 }
							}
                         if(cmdNext != null){
                         out.println(
                                 "<script>window.location = 'DocumentHelp.jsp?np=assetManagerInfo&cc=" +
                                		 recId + "'</script>");}
                         if(cmdSave != null){
                             out.println(
                                     "<script>window.location = 'DocumentHelp.jsp?np=companyDefaults_Single&cc=" +
                                    		 recId + "'</script>");}
                     }
            	}else {
            	if (cmdSave != null) {recId = ch.createCompanyTmp(company);}
            	//boolean addon = ch.addOnAssetManagerInfo(company,recId);}
            	System.out.println("=====>>>>>recId under cmdSave: "+recId);
//                audit.select(1,
//                        "SELECT * FROM  AM_GB_COMPANY  WHERE company_code = '" +
//                        companyCode + "'");
//                boolean isupdt = ch.updateCompanyTmp(company);
//                audit.select(2,
//                        "SELECT * FROM  AM_GB_COMPANY  WHERE company_code = '" +
//                        companyCode + "'");
//                /*
//                updtst = audit.logAuditTrail("AM_GB_COMPANY", branchcode,
//                                             loginID, companyCode);
//                */
//                updtst = audit.logAuditTrail("AM_GB_COMPANY", branchcode,
//                                             loginID, roleid,hostName,ipAddress,macAddress);
//
//                if (updtst == true) {
                if (!recId.equalsIgnoreCase("")) {  
                	statusMessage = "Record successfully  Sent for Approval";
                	String[] pa = new String[12];
                	String recInteger = recId.substring(2);
//                	System.out.println("======recInteger: "+recInteger);
					pa[0]=recId; pa[1]= userId; pa[2]=supervisor; pa[3]="0"; pa[4]= "";
					pa[5]= companyName; pa[6]= transDate; pa[7]= branchcode; pa[8]="PENDING"; pa[9]="Company Profile"; pa[10]="P";pa[11]=numOfTransactionLevel;
					System.out.println("=====recId: "+recId+"     =====userId: "+userId+"     singleApproval: "+singleApproval+"     transDate: "+transDate);	
					if(userId.equals("")){userId = "0";}
					if(singleApproval.equals("Y")){
//					ad.setPendingTrans(ad.setApprovalData(venId),"1",Integer.parseInt(venId));
					ad.setPendingTransAdmin(pa,"14",Integer.parseInt(recId.substring(2)),"I");
//					ad.setPendingTransArchive(pa,"14",Integer.parseInt(recId),Integer.parseInt(recId));									
					}
					   if(singleApproval.equals("N")){
						   pa[8]="PENDING";
					  		String mtid = appHelper.getGeneratedId("am_asset_approval");
//					   		System.out.println("approvelist.size()#$$$$$$$$$$$ "+approvelist.size());
					   	 for(int j=0;j<approvelist.size();j++)
					     { 
						  	legend.admin.objects.User usrInfo = (legend.admin.objects.User)approvelist.get(j);   	 
							String supervisorId =  usrInfo.getUserId();
							String mailAddress = usrInfo.getEmail();
							String supervisorName = usrInfo.getUserName();
							String supervisorfullName = usrInfo.getUserFullName();
//							System.out.println("SupervisorId#$$$$$$$$$$$ "+supervisorId);
					  		ad.setPendingTransAdminMultiApp(pa,"92",Integer.parseInt(recId.substring(2)),"I",supervisorId,mtid);
					  	 }
					}  
                	
                    //statusMessage = "Update on record is successfull";
 //                   out.println("<script>alert('Update on record is successfull');</script>");
                    if(cmdNext != null){                    	
                        out.println(
                                "<script>window.location = 'DocumentHelp.jsp?np=assetManagerInfo&cc=" +
                                		recId + "'</script>");}
                        if(cmdSave != null){
                        	out.println("<script>alert('Transaction has gone for Approval ');</script>");
                            out.println(
                                    "<script>window.location = 'DocumentHelp.jsp?np=companyDefaults_Single&cc=" +
                                    		recId + "'</script>");}
                } else {
                    statusMessage = "No changes made on record";
                    recId = compRecId;
//                    out.print(
//                            "<script>alert('No changes made on record')</script>");
                    if(cmdNext != null){
                        out.println(
                                "<script>window.location = 'DocumentHelp.jsp?np=assetManagerInfo&cc=" +
                                		recId + "'</script>");}
                        if(cmdSave != null){
                            out.println(
                                    "<script>window.location = 'DocumentHelp.jsp?np=companyDefaults_Single&cc=" +
                                    		recId + "'</script>");}
                }

            	}
            }
        }
        } catch (Exception t) {
            t.printStackTrace();
            //statusMessage = "Error! Record not saved.";
            out.println("<script>alert('Error! Record not saved.');</script>");
            out.println(
                    "<script>window.location = 'DocumentHelp.jsp?np=companyDefaults_Single'</script>");
        }


    }

    public String getServletInfo() {
        return "Company Audit Servlet";
    }


}
