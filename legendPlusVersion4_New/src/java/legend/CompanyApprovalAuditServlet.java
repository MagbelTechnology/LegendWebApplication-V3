package legend;

import audit.AuditTrailGen;
import com.magbel.legend.bus.ApprovalRecords;
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
import java.sql.Timestamp;
import java.util.Date;
import legend.admin.handlers.AdminHandler;
import legend.admin.handlers.CompanyHandler;
import legend.admin.objects.Company;
import magma.AssetRecordsBean;
import magma.net.dao.MagmaDBConnection;

public class CompanyApprovalAuditServlet extends HttpServlet {
   DatetimeFormat dtf = new DatetimeFormat();
   private ApprovalRecords records;

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
      MagmaDBConnection dbConnection = new MagmaDBConnection();
      String statusMessage = "";
      boolean updtst = false;
      this.records = new ApprovalRecords();
      AuditTrailGen audit = new AuditTrailGen();
      new ApprovalRecords();
      String loginId = (String)session.getAttribute("CurrentUser");
      int loginID;
      if (loginId == null) {
         loginID = 0;
      } else {
         loginID = Integer.parseInt(loginId);
      }

      String branchcode = (String)session.getAttribute("UserCenter");
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
      if (passwordLimit == null) {
         passwordLimit = "0";
      }

      if (comp_delimiter == null) {
         comp_delimiter = "";
      }

      Timestamp approveddate = dbConnection.getDateTime(new Date());
      String assetId = request.getParameter("tempId");
      String financialStartDate = request.getParameter("period_start_date");
      String financialNoOfMonths = request.getParameter("period_number");
      String financialEndDate = request.getParameter("period_end_date");
      String minimumPassword = request.getParameter("minimum_password");
      String passwordExpiry = request.getParameter("password_expiry");
      String sessionTimeout = request.getParameter("session_timeout");
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

      int tranId = Integer.parseInt(request.getParameter("tranId"));
      if (request.getParameter("assetCode") == null) {
         boolean var10000 = false;
      } else {
         Integer.parseInt(request.getParameter("assetCode"));
      }

      String password_upper = request.getParameter("password_upper");
      String password_lower = request.getParameter("password_lower");
      String password_numeric = request.getParameter("password_numeric");
      String password_special = request.getParameter("password_special");
      String userClass = (String)session.getAttribute("UserClass");
      String processingDate = request.getParameter("processing_date");
      String processingFrequency = request.getParameter("processing_frequency");
      String nextProcessingDate = request.getParameter("next_processing_date");
      String defaultBranch = request.getParameter("default_branch");
      String branchName = request.getParameter("branch_name");
      String suspenseAcct = request.getParameter("suspenseAccount");
      String autoGenId = "N";
      String thirdpartytransaction = "N";
      String raiseEntry = "N";
      if (request.getParameter("autoid_generate") != null) {
         autoGenId = request.getParameter("autoid_generate");
      }

      System.out.println("Before thirdpartytransaction: " + thirdpartytransaction);
      if (request.getParameter("thirdpartytransaction") != null) {
         thirdpartytransaction = request.getParameter("thirdpartytransaction");
      }

      if (request.getParameter("raiseEntry") != null) {
         raiseEntry = request.getParameter("raiseEntry");
      }

      System.out.println("After thirdpartytransaction: " + thirdpartytransaction + "   raiseEntry: " + raiseEntry);
      String residualValue = request.getParameter("residual_value");
      String depreciationMethod = request.getParameter("depreciation_method");
      String vatAccount = request.getParameter("vat_account");
      String whtAccount = request.getParameter("wht_account");
      String pLDisposalAccount = request.getParameter("pandl_account");
      String PLDStatus = "N";
      String recId = request.getParameter("recId");
      String recType = request.getParameter("recordType");
      System.out.println("After recId: " + recId + "   cmdSave: " + cmdSave);
      if (request.getParameter("pandl_account_cent") != null) {
         PLDStatus = request.getParameter("pandl_account_cent");
      }

      String vatAcctStatus = "N";
      if (request.getParameter("vat_account_cent") != null) {
         vatAcctStatus = request.getParameter("vat_account_cent");
      }

      String asset_acq_status = "N";
      if (request.getParameter("asset_acq_status") != null) {
         asset_acq_status = request.getParameter("asset_acq_status");
      }

      String asset_defer_status = "N";
      if (request.getParameter("asset_defer_status") != null) {
         asset_defer_status = request.getParameter("asset_defer_status");
      }

      String part_pay_status = "N";
      if (request.getParameter("part_pay_status") != null) {
         part_pay_status = request.getParameter("part_pay_status");
      }

      String whtAcctStatus = "N";
      if (request.getParameter("wht_account_cent") != null) {
         whtAcctStatus = request.getParameter("wht_account_cent");
      }

      String fedWhtAccountStatus = "N";
      if (request.getParameter("fed_wht_account_cent") != null) {
         System.out.println("the valu of fed_wht_account_cent " + request.getParameter("fed_wht_account_cent"));
         fedWhtAccountStatus = request.getParameter("fed_wht_account_cent");
      }

      String suspenseAcctStatus = "N";
      if (request.getParameter("suspenseAcctStatus") != null) {
         suspenseAcctStatus = request.getParameter("suspenseAcctStatus");
      }

      String disposal_account_cent = "N";
      if (request.getParameter("disposal_account_cent") != null) {
         disposal_account_cent = request.getParameter("disposal_account_cent");
      }

      String sbuRequired = "N";
      if (request.getParameter("sbu_required") != null) {
         sbuRequired = request.getParameter("sbu_required");
      }

      String sbuLevel = request.getParameter("sbu_level");
      String cc = request.getParameter("company_code");
      String lpo_r = request.getParameter("lpo_r");
      String bar_code_r = request.getParameter("bar_code_r");
      Double cp_threshold = Double.parseDouble(request.getParameter("cp_threshold"));
      String defer_account = request.getParameter("asset_defer");
      String asset_acq = request.getParameter("asset_acq");
      String trans_thresholds = request.getParameter("tran_threshold");
      double trans_thresholdd = Double.parseDouble(trans_thresholds);
      String part_pay = request.getParameter("part_pay");
      System.out.println("enforce_pm_budget " + enforcePmBudget);
      String userId = (String)session.getAttribute("CurrentUser");
      String system_date = request.getParameter("system_date");
      String fed_wh_tax_account = request.getParameter("fedWhtAccount");
      String proofSessionTimeout = request.getParameter("proof_session_timeout");
      String loss_disposal_account = request.getParameter("loss_disposal_account");
      String group_Asset_Account = request.getParameter("group_Asset_Account");
      String self_Charge_Acct = request.getParameter("self_Charge_Acct");
      String databaseName = request.getParameter("databaseName");
      String userid = (String)session.getAttribute("CurrentUser");
      String groupAssetAccountStatus = "N";
      if (request.getParameter("groupAssetAccountStatus") != null) {
         groupAssetAccountStatus = request.getParameter("groupAssetAccountStatus");
      }

      String selfChargeAcctStatus = "N";
      if (request.getParameter("selfChargeAcctStatus") != null) {
         selfChargeAcctStatus = request.getParameter("selfChargeAcctStatus");
      }

      String singleApproval = request.getParameter("singleApproval");
      String rr = request.getParameter("reject_reason");
      String astatus = request.getParameter("astatus");
      System.out.println("========astatus: " + astatus);
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
      company.setPasswordLimit(Integer.parseInt(passwordLimit));
      company.setLogUserAudit(keepUserLogAudit);
      company.setAutoGenId(autoGenId);
      company.setBranchName(branchName);
      company.setDefaultBranch(defaultBranch);
      company.setDepreciationMethod(depreciationMethod);
      company.setNextProcessingDate(nextProcessingDate);
      company.setPLDisposalAccount(pLDisposalAccount);
      company.setPLDStatus(PLDStatus);
      company.setProcessingDate(processingDate);
      company.setProcessingFrequency(processingFrequency);
      company.setResidualValue(residualValue);
      company.setSbuLevel(sbuLevel);
      company.setSbuRequired(sbuRequired);
      company.setSuspenseAcct(suspenseAcct);
      company.setSuspenseAcctStatus(suspenseAcctStatus);
      company.setVatAccount(vatAccount);
      company.setVatAcctStatus(vatAcctStatus);
      company.setWhtAccount(whtAccount);
      company.setWhtAcctStatus(whtAcctStatus);
      company.setLpo_r(lpo_r);
      company.setBar_code_r(bar_code_r);
      company.setCp_threshold(cp_threshold);
      company.setFedWhtAccount(fed_wh_tax_account);
      company.setFedWhtAcctStatus(fedWhtAccountStatus);
      company.setTrans_threshold(trans_thresholdd);
      company.setDeferAccount(defer_account);
      company.setAssetSuspenseAcct(asset_acq);
      company.setPart_pay(part_pay);
      company.setAsset_acq_status(asset_acq_status);
      company.setAsset_defer_status(asset_defer_status);
      company.setPart_pay_status(part_pay_status);
      company.setThirdpartytransaction(thirdpartytransaction);
      company.setRaiseEntry(raiseEntry);
      company.setSysDate(system_date);
      company.setProofSessionTimeout(Integer.parseInt(proofSessionTimeout));
      company.setLossDisposalAcct(loss_disposal_account);
      company.setLDAcctStatus(disposal_account_cent);
      company.setGroupAssetAcct(group_Asset_Account);
      company.setGAAStatus(groupAssetAccountStatus);
      company.setSelfChargeAcct(self_Charge_Acct);
      company.setSelfChargeStatus(selfChargeAcctStatus);
      company.setDatabaseName(databaseName);
      
      
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

      String macAddress = sb.toString();
      System.out.println(sb.toString());

      try {
         AssetRecordsBean arb = new AssetRecordsBean();
         String tableName = "am_gb_companytemp";
         if (!userClass.equals("NULL") || userClass != null) {
            singleApproval.equalsIgnoreCase("N");
            CompanyHandler ch = new CompanyHandler();
            String q;
            if (cmdSave != null && astatus.equalsIgnoreCase("A")) {
               arb.deleteOtherSupervisorswithBatchId(String.valueOf(tranId), userid);
               if (ch.getCompany() == null && ch.createCompany(company) && cmdSave != null) {
                  out.println("<script>window.location = 'DocumentHelp.jsp?np=transactionForApprovalList'</script>");
               }

               audit.select(1, "SELECT * FROM  AM_GB_COMPANY  WHERE company_code = '" + companyCode + "'");
               System.out.println("Record Type in CompanyApprovalAuditServlet : " + recType);
               boolean isupdt = false;
               if (recType.equals("I")) {
                  isupdt = ch.updateAllCompanyFields(company);
               }

               if (recType.equals("A")) {
                  isupdt = ch.updateCompanyApproval(company);
               }

               if (recType.equals("B")) {
                  isupdt = ch.updateAssetManagerInfoApproval(company);
               }

               audit.select(2, "SELECT * FROM  AM_GB_COMPANY  WHERE company_code = '" + companyCode + "'");
               updtst = audit.logAuditTrail("AM_GB_COMPANY", branchcode, loginID, roleid, hostName, ipAddress, macAddress);
               if (updtst && isupdt) {
                  q = "update am_asset_approval set process_status='A', asset_status='APPROVED', reject_reason='" + rr + "',DATE_APPROVED = '" + approveddate + "' where transaction_id=" + tranId;
                  String r = "update am_gb_companytemp set RECORD_TYPE='C', Approval_status='APPROVED' where TMPID='" + recId + "'";
                  arb.updateAssetStatusChange(q);
                  arb.updateAssetStatusChange(r);
                  if (cmdNext != null) {
                	  out.println("<script>alert('Record Update Successful');</script>");
                     out.println("<script>window.location = 'DocumentHelp.jsp?np=transactionForApprovalList'</script>");
                  }

                  if (cmdSave != null) {
                	  out.println("<script>alert('Record Update Successful');</script>");
                     out.println("<script>window.location = 'DocumentHelp.jsp?np=transactionForApprovalList'</script>");
                  }
               } else {
                  out.print("<script>alert('No changes made on record')</script>");
                  if (cmdNext != null) {
                     out.println("<script>window.location = 'DocumentHelp.jsp?np=transactionForApprovalList'</script>");
                  }

                  if (cmdSave != null) {
                     out.println("<script>window.location = 'DocumentHelp.jsp?np=transactionForApprovalList'</script>");
                  }
               }
            }

            if (astatus.equalsIgnoreCase("R")) {
               q = "update am_asset_approval set process_status='R', asset_status='REJECTED', reject_reason='" + rr + "',DATE_APPROVED = '" + approveddate + "' where transaction_id=" + tranId;
             String r = "update am_gb_companytemp set RECORD_TYPE='R', Approval_status='REJECTED' where TMPID='" + recId + "'";
               arb.updateAssetStatusChange(q);
               arb.updateAssetStatusChange(r);
               out.print("<script>alert('Rejection Successful')</script>");
               out.print("<script>window.location = 'DocumentHelp.jsp?np=transactionForApprovalList'</script>");
            }

            if (astatus.equalsIgnoreCase("")) {
               out.print("<script>window.location = 'DocumentHelp.jsp?np=transactionForApprovalList'</script>");
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
         out.println("<script>alert('Error! Record not saved.');</script>");
         out.println("<script>window.location = 'DocumentHelp.jsp?np=transactionForApprovalList'</script>");
      }

   }

   public String getServletInfo() {
      return "Company Audit Servlet Approval";
   }
}
