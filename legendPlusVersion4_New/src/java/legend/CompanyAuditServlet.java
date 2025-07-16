package legend;

import audit.AuditTrailGen;
import com.magbel.util.ApplicationHelper;
import com.magbel.util.DatetimeFormat;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Date;
import legend.admin.handlers.AdminHandler;
import legend.admin.handlers.CompanyHandler;
import legend.admin.handlers.SecurityHandler;
import legend.admin.objects.Company;
import legend.admin.objects.User;
import magma.AssetRecordsBean;

public class CompanyAuditServlet extends HttpServlet {
   DatetimeFormat dtf = new DatetimeFormat();
   DatetimeFormat df;
   private AssetRecordsBean ad;

   public void init(ServletConfig config) throws ServletException {
      super.init(config);
   }

   public void destroy() {
   }

   public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      response.setContentType("text/html");
      response.setDateHeader("Expires", -1L);
      HttpSession session = request.getSession();
      PrintWriter out = response.getWriter();
      String statusMessage = "";
      boolean updtst = false;
      String recId = "";
      new AuditTrailGen();
      ApplicationHelper appHelper = new ApplicationHelper();
      int loginID;
      String loginId = (String)session.getAttribute("CurrentUser");
//      if (loginId == null) {
//         boolean var10 = false;
//      } else {
//         int var81 = Integer.parseInt(loginId);
//      }
      
      if (loginId == null) {
          loginID = 0;
      } else {
          loginID = Integer.parseInt(loginId);
      }

      String branchcode = (String)session.getAttribute("UserCenter");
      if (branchcode == null) {
         branchcode = "";
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
      String singleApproval = request.getParameter("singleApproval");
      String supervisor = request.getParameter("supervisor");
      String numOfTransactionLevel = request.getParameter("numOfTransactionLevel");
      String compRecId = request.getParameter("compRecId");
      if (passwordLimit == null) {
         passwordLimit = "0";
      }

      if (comp_delimiter == null) {
         comp_delimiter = "";
      }

      String financialStartDate = request.getParameter("period_start_date");
      String financialNoOfMonths = request.getParameter("period_number");
      String financialEndDate = request.getParameter("period_end_date");
      String minimumPassword = request.getParameter("minimum_password");
      String passwordExpiry = request.getParameter("password_expiry");
      String sessionTimeout = request.getParameter("session_timeout");
      String proofsessionTimeout = request.getParameter("proof_session_timeout");
      System.out.println("======>>>>>>>proofsessionTimeout: " + proofsessionTimeout);
      if (proofsessionTimeout == null || proofsessionTimeout == "null") {
         proofsessionTimeout = "0";
      }

      String enforceAcqBudget = "N";
      int attempt_logon = Integer.parseInt(request.getParameter("log_on"));
      if (request.getParameter("enforce_acq_budget") != null) {
         enforceAcqBudget = request.getParameter("enforce_acq_budget");
      }

      String enforcePmBudget = "N";
      if (request.getParameter("enforce_pm_budget") != null) {
         enforcePmBudget = request.getParameter("enforce_pm_budget");
      }

      String enforceFuelAllocation = "N";
      if (request.getParameter("enforce_fuel_allocation") != null) {
         enforceFuelAllocation = request.getParameter("enforce_fuel_allocation");
      }

      String requireQuarterlyPM = "N";
      if (request.getParameter("require_quarterly_pm") != null) {
         requireQuarterlyPM = request.getParameter("require_quarterly_pm");
      }

      String quarterlySurplusCf = "N";
      if (request.getParameter("quarterly_surplus_cf") != null) {
         quarterlySurplusCf = request.getParameter("quarterly_surplus_cf");
      }

      String keepUserLogAudit = "N";
      if (request.getParameter("LOG_USER") != null) {
         keepUserLogAudit = request.getParameter("LOG_USER");
      }

      String password_upper = request.getParameter("password_upper");
      String password_lower = request.getParameter("password_lower");
      System.out.println("password_lower " + password_lower);
      String password_numeric = request.getParameter("password_numeric");
      String password_special = request.getParameter("password_special");
      String userClass = (String)session.getAttribute("UserClass");
      System.out.println("enforce_pm_budget " + enforcePmBudget);
      String userId = (String)session.getAttribute("CurrentUser");
      Company company = new Company();
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
      System.out.println("passwordLimit " + passwordLimit);
      company.setPasswordLimit(Integer.parseInt(passwordLimit));
      company.setLogUserAudit(keepUserLogAudit);
      company.setProofSessionTimeout(Integer.parseInt(proofsessionTimeout));
      AdminHandler admin = new AdminHandler();
      String roleid = admin.getPrivilegesRole("Company Profile");
      String computerName = null;
      String remoteAddress = request.getRemoteAddr();
      InetAddress inetAddress = InetAddress.getByName(remoteAddress);
      System.out.println("inetAddress: " + inetAddress);
      computerName = inetAddress.getHostName();
      System.out.println("computerName: " + computerName);
      if (computerName.equalsIgnoreCase("localhost")) {
         computerName = InetAddress.getLocalHost().getCanonicalHostName();
      }

      String hostName = "";
      InetAddress ip;
      if (hostName.equals(request.getRemoteAddr())) {
         ip = InetAddress.getByName(request.getRemoteAddr());
         hostName = ip.getHostName();
      }

      if (InetAddress.getLocalHost().getHostAddress().equals(request.getRemoteAddr())) {
         hostName = "Local Host";
      }

      ip = InetAddress.getLocalHost();
      String ipAddress = ip.getHostAddress();
      System.out.println("Current IP address : " + ip.getHostAddress());
      NetworkInterface network = NetworkInterface.getByInetAddress(ip);
      byte[] mac = network.getHardwareAddress();
      if (mac == null) {
         String value = "";
         mac = value.getBytes();
      }

      StringBuilder sb = new StringBuilder();

      for(int i = 0; i < mac.length; ++i) {
         sb.append(String.format("%02X%s", mac[i], i < mac.length - 1 ? "-" : ""));
      }

      SecurityHandler sechanle = new SecurityHandler();
      String macAddress = sb.toString();
      System.out.println(sb.toString());
      User usr = sechanle.getUserByUserID(loginId);
      String user_Name = usr.getUserName();
      String branchuser_NameRestrict = usr.getBranchRestrict();
      String User_Restrict = usr.getDeptRestrict();
      String departCode = usr.getDeptCode();
      String branch = usr.getBranch();

      try {
         this.df = new DatetimeFormat();
         this.ad = new AssetRecordsBean();
         if (!userClass.equals("NULL") || userClass != null) {
            CompanyHandler ch = new CompanyHandler();
            System.out.println("=====branch: " + branch + "  ====departCode: " + departCode + "   ==user_Name " + user_Name);
            ArrayList approvelist = this.ad.getApprovalsId(branch, departCode, user_Name);
            Date tranDate = new Date();
            String transDate = this.df.formatDate(tranDate);
            System.out.println("=====tranDate: " + tranDate + "  ====cmdSave: " + cmdSave + "   ==cmdNext " + cmdNext);
            if (cmdSave != null || cmdNext != null) {
               String[] pa;
               String mtid="";
               String supervisorId="";
               String mailAddress;
               String supervisorName;
               if (ch.getCompany() == null && cmdSave != null) {
                  recId = ch.createCompanyTmp(company);
                  System.out.println("=====>>>>>>>>recId First: " + recId);
                  if (!recId.equalsIgnoreCase("")) {
                     statusMessage = "Record successfully  Sent for Approval";
                     pa = new String[]{recId, userId, supervisor, "0", "", companyName, transDate, recId, "PENDING", "Company Profile", "P", numOfTransactionLevel};
                     if (userId.equals("")) {
                        userId = "0";
                     }

                     if (singleApproval.equalsIgnoreCase("Y")) {
                        this.ad.setPendingTransAdmin(pa, "85", Integer.parseInt(recId), "I");
                     }

                     if (singleApproval.equalsIgnoreCase("N")) {
                        pa[8] = "PENDING";
                        mtid = appHelper.getGeneratedId("am_asset_approval");

                        for(int j = 0; j < approvelist.size(); ++j) {
                           User usrInfo = (User)approvelist.get(j);
                            supervisorId = usrInfo.getUserId();
                           supervisorId = usrInfo.getEmail();
                           mailAddress = usrInfo.getUserName();
                           supervisorName = usrInfo.getUserFullName();
                           this.ad.setPendingTransAdminMultiApp(pa, "85", Integer.parseInt(recId), "I", supervisorId, mtid);
                        }
                     }

                     if (cmdNext != null) {
                    	// System.out.println("=====>>>>> We are here 1: ");
                    	 out.println("<script>alert('Transaction has gone for Approval ');</script>");
                        out.println("<script>window.location = 'DocumentHelp.jsp?np=assetManagerInfo&cc=" + recId + "'</script>");
                     }

                     if (cmdSave != null) {
                    	// System.out.println("=====>>>>> We are here 2: ");
                    	 out.println("<script>alert('Transaction has gone for Approval ');</script>");
                        out.println("<script>window.location = 'DocumentHelp.jsp?np=companyDefaults&cc=" + recId + "'</script>");
                     }
                  }
               } else {
                  if (cmdSave != null) {
                     recId = ch.createCompanyTmp(company);
                     ch.addOnAssetManagerInfo(company, recId);
                  }

                  System.out.println("=====>>>>>recId under cmdSave: " + recId);
                  if (!recId.equalsIgnoreCase("")) {
                     statusMessage = "Record successfully  Sent for Approval";
                     pa = new String[12];
                     mtid = recId.substring(2);
                     pa[0] = recId;
                     pa[1] = userId;
                     pa[2] = supervisor;
                     pa[3] = "0";
                     pa[4] = "";
                     pa[5] = companyName;
                     pa[6] = transDate;
                     pa[7] = branchcode;
                     pa[8] = "PENDING";
                     pa[9] = "Company Profile";
                     pa[10] = "P";
                     pa[11] = numOfTransactionLevel;
                     if (userId.equals("")) {
                        userId = "0";
                     }

                     if (singleApproval.equals("Y")) {
                        this.ad.setPendingTransAdmin(pa, "85", Integer.parseInt(recId.substring(2)), "I");
                     }

                     if (singleApproval.equals("N")) {
                        pa[8] = "PENDING";
                         mtid = appHelper.getGeneratedId("am_asset_approval");

                        for(int j = 0; j < approvelist.size(); ++j) {
                           User usrInfo = (User)approvelist.get(j);
                           supervisorId = usrInfo.getUserId();
                           mailAddress = usrInfo.getEmail();
                           supervisorName = usrInfo.getUserName();
                           String supervisorfullName = usrInfo.getUserFullName();
                           this.ad.setPendingTransAdminMultiApp(pa, "85", Integer.parseInt(recId.substring(2)), "I", supervisorId, mtid);
                        }
                     }

                     if (cmdNext != null) {
                    	// System.out.println("=====>>>>> We are here 1A: ");
                    	 out.println("<script>alert('Transaction has gone for Approval ');</script>");
                        out.println("<script>window.location = 'DocumentHelp.jsp?np=assetManagerInfo&cc=" + recId + "'</script>");
                     }

                     if (cmdSave != null) {
                    	// System.out.println("=====>>>>> We are here 2A: ");
                        out.println("<script>alert('Transaction has gone for Approval ');</script>");
                        out.println("<script>window.location = 'DocumentHelp.jsp?np=companyDefaults&cc=" + recId + "'</script>");
                     }
                  } else {
                     statusMessage = "No changes made on record";
                     if (cmdNext != null) {
                    	// System.out.println("=====>>>>> We are here 1C: ");
                        out.println("<script>window.location = 'DocumentHelp.jsp?np=assetManagerInfo&cc=" + compRecId + "'</script>");
                     }

                     if (cmdSave != null) {
                    	// System.out.println("=====>>>>> We are here 2C: ");
                        out.println("<script>window.location = 'DocumentHelp.jsp?np=companyDefaults&cc=" + compRecId + "'</script>");
                     }
                  }
               }
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
         out.println("<script>alert('Error! Record not saved.');</script>");
         out.println("<script>window.location = 'DocumentHelp.jsp?np=companyDefaults'</script>");
      }

   }

   public String getServletInfo() {
      return "Company Audit Servlet";
   }
}
