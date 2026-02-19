package legend;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Date;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import magma.AssetRecordsBean;
import audit.AuditTrailGen;

/**
 * Servlet implementation class for Servlet: AssetInfoManagerServlet
 *
 */
public class AssetInfoManagerServlet extends jakarta.servlet.http.HttpServlet
		implements jakarta.servlet.Servlet {
	com.magbel.util.DatetimeFormat df;
	private AssetRecordsBean ad;
	/*
	 * (non-Java-doc)
	 *
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public AssetInfoManagerServlet() {
		super();
	}

	/*
	 * (non-Java-doc)
	 *
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request,
	 *      HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request,response);
	}

	/*
	 * (non-Java-doc)
	 *
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request,
	 *      HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		response.setContentType("text/html");
		response.setDateHeader("Expires", -1);

		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();

		// String type = request.getParameter("TYPE");
		String statusMessage = "";
		boolean updtst = false;

		// java.sql.Date dt = new java.sql.Date();
		AuditTrailGen audit = new AuditTrailGen();
		// java.text.SimpleDateFormat sdf = new
		// java.text.SimpleDateFormat("dd/mm/yyyy");
	    String userClass = (String) session.getAttribute("UserClass");
		int loginID;
		String loginId = (String) session.getAttribute("CurrentUser");
		String userId = (String) session.getAttribute("CurrentUser");
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

//		String processingDate = request.getParameter("processing_date");
//		String processingFrequency = request
//				.getParameter("processing_frequency");
//		String nextProcessingDate = request
//				.getParameter("next_processing_date");
		String defaultBranch = request.getParameter("default_branch");
		String branchName = request.getParameter("branch_name");
		String suspenseAcct = request.getParameter("suspense_acct");
		String lossDisposalAccount = request.getParameter("disposal_account");
		String groupAssetAcct = request.getParameter("groupAsset_Acct");
		String singleApproval = request.getParameter("singleApproval");
		String autoGenId = "N";
//		String thirdpartytransaction = "N";
//		String raiseEntry = "N";
		if (request.getParameter("autoid_generate")  != null) {
			autoGenId = request.getParameter("autoid_generate");
		}
//		System.out.println("Before thirdpartytransaction: "+thirdpartytransaction);
//		if (request.getParameter("thirdpartytransaction")  != null) {
//			thirdpartytransaction = request.getParameter("thirdpartytransaction");
//		}
//		if (request.getParameter("raiseEntry")  != null) {
//			raiseEntry = request.getParameter("raiseEntry");
//		}
//		System.out.println("After thirdpartytransaction: "+thirdpartytransaction+"   raiseEntry: "+raiseEntry);
		String residualValue = request.getParameter("residual_value");
		String depreciationMethod = request.getParameter("depreciation_method");
		String vatAccount = request.getParameter("vat_account");
		String whtAccount = request.getParameter("wht_account");
		String pLDisposalAccount = request.getParameter("pandl_account");
		String PLDStatus = "N";
		String selfCharge_Acct = request.getParameter("selfCharge_Acct"); 
		String recId = request.getParameter("recId");
		System.out.println("After recId: "+recId+"   cmdSave: "+cmdSave);
		if (request.getParameter("pandl_account_cent") != null) {
			PLDStatus = request.getParameter("pandl_account_cent");
		}


        String vatAcctStatus = "N";
		if (request.getParameter("vat_account_cent") != null) {
			vatAcctStatus = request.getParameter("vat_account_cent");
		}

		 com.magbel.util.ApplicationHelper appHelper = new com.magbel.util.ApplicationHelper();

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
		System.out.println("the value of part_pay_status is $$$$$$$$: " + request.getParameter("part_pay_status"));
		
        String LDAcctStatus = "N";
		if (request.getParameter("disposal_account_cent") != null) {
			LDAcctStatus = request.getParameter("disposal_account_cent");
		}

        String GAAcctStatus = "N";
        System.out.println("the value of group_acct_status is $$$$$$$$: " + request.getParameter("group_acct_status"));
		if (request.getParameter("group_acct_status") != null) {
			GAAcctStatus = request.getParameter("group_acct_status");
		}
		
        String whtAcctStatus = "N";
		if (request.getParameter("wht_account_cent") != null) {
			whtAcctStatus = request.getParameter("wht_account_cent");
		}


          String fedWhtAccountStatus = "N";

        if ( request.getParameter("fed_wht_account_cent")!= null) {
            System.out.println("the valu of fed_wht_account_cent " + request.getParameter("fed_wht_account_cent"));
			fedWhtAccountStatus = request.getParameter("fed_wht_account_cent");
        }

		String suspenseAcctStatus = "N";
		if (request.getParameter("disposal_account_cent") != null) {
			suspenseAcctStatus = request.getParameter("disposal_account_cent");
		} 

		String selfChargeStatus = "N";
		 System.out.println("the value of self_charges_status is $$$$$$$$: " + request.getParameter("self_charges_status"));
		if (request.getParameter("self_charges_status") != null) {
			selfChargeStatus = request.getParameter("self_charges_status");
		}

//		String sbuRequired = "N";
//		if (request.getParameter("sbu_required") != null) {
//			sbuRequired = request.getParameter("sbu_required");
//		}
		String sbuLevel = request.getParameter("sbu_level");
		String cc = request.getParameter("company_code");

        String lpo_r = request.getParameter("lpo_r");
        String bar_code_r = request.getParameter("bar_code_r");
        Double cp_threshold = Double.parseDouble( request.getParameter("cp_threshold"));
        String defer_account = request.getParameter("asset_defer");

        String asset_acq = request.getParameter("asset_acq");
        
         String trans_thresholds = request.getParameter("tran_threshold");
     //    System.out.println("trans_thresholds: "+trans_thresholds);
        double trans_thresholdd = Double.parseDouble(trans_thresholds);
        String part_pay = request.getParameter("part_pay");
    //    System.out.println("trans_thresholdd: "+trans_thresholdd);
        String supervisor = request.getParameter("supervisor");
        String numOfTransactionLevel = request.getParameter("numOfTransactionLevel");
        
        String fed_wh_tax_account = request.getParameter("fedWhtAccount");
//        String systemDate = request.getParameter("system_date");
//        String databaseName = request.getParameter("databaseName");
//        System.out.println("systemDate "+systemDate+"     databaseName: "+databaseName);
        String financialStartDate = request.getParameter("period_start_date");
      	String financialNoOfMonths = request.getParameter("period_number");
      	String financialEndDate = request.getParameter("period_end_date");      
	    String vatRate = request.getParameter("vat_rate");
	    String whtRate = request.getParameter("wht_rate");
	    String comp_delimiter = request.getParameter("comp_delimiter");
      	
		//System.out.println("Auto_id_generate "+autoGenId);
		legend.admin.objects.AssetManagerInfo ami = new legend.admin.objects.AssetManagerInfo();
		ami.setAutoGenId(autoGenId);
		ami.setBranchName(branchName);
		ami.setDefaultBranch(defaultBranch);
		ami.setDepreciationMethod(depreciationMethod);
//		ami.setNextProcessingDate(nextProcessingDate);
		ami.setPLDisposalAccount(pLDisposalAccount);
		ami.setPLDStatus(PLDStatus);
//		ami.setProcessingDate(processingDate);
//		ami.setProcessingFrequency(processingFrequency);
		ami.setResidualValue(residualValue);
//		ami.setSbuLevel(sbuLevel);
//		ami.setSbuRequired(sbuRequired);
		ami.setSuspenseAcct(suspenseAcct);
		ami.setSuspenseAcctStatus(suspenseAcctStatus);
		ami.setVatAccount(vatAccount);
		ami.setVatAcctStatus(vatAcctStatus);
		ami.setWhtAccount(whtAccount);
		ami.setWhtAcctStatus(whtAcctStatus);
        ami.setLpo_r(lpo_r);
        ami.setBar_code_r(bar_code_r);
        ami.setCp_threshold(cp_threshold);

        ami.setFedWhtAccount(fed_wh_tax_account);
        ami.setFedWhtAcctStatus(fedWhtAccountStatus);

        ami.setTrans_threshold(trans_thresholdd);
        ami.setDeferAccount(defer_account);
        ami.setAssetSuspenseAcct(asset_acq);
        ami.setPart_pay(part_pay);
        ami.setAsset_acq_status(asset_acq_status);
        ami.setAsset_defer_status(asset_defer_status);
        ami.setPart_pay_status(part_pay_status);
//        ami.setThirdpartytransaction(thirdpartytransaction);
//        ami.setRaiseEntry(raiseEntry);
//        ami.setSysDate(systemDate);
        ami.setLossDisposalAcct(lossDisposalAccount);
        ami.setLDAcctStatus(LDAcctStatus);
        ami.setGroupAssetAcct(groupAssetAcct);
        ami.setGAAStatus(GAAcctStatus);
        ami.setSelfChargeAcct(selfCharge_Acct);
        ami.setSelfChargeStatus(selfChargeStatus);
//        ami.setDatabaseName(databaseName);
      ami.setFinancialEndDate(financialEndDate);
      ami.setFinancialNoOfMonths(Integer.parseInt(financialNoOfMonths));
      ami.setFinancialStartDate(financialStartDate);
      ami.setVatRate(Double.parseDouble(vatRate));
      ami.setWhtRate(Double.parseDouble(whtRate));
      ami.setComp_delimiter(comp_delimiter);
      
        
        //System.out.println("in AssetInfoManagerServlet  $$$$$$$$$$$$$$$$$$$$$$$$$$$ ");
       // System.out.println("the value of cost threshold is $$$$$$$$$$$$$$$$$$$$$$$$$$$ " + cp_threshold);
       // System.out.println("the value of trans threshold is $$$$$$$$$$$$$$$$$$$$$$$$$$$ " + trans_thresholdd);
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
			
			legend.admin.handlers.SecurityHandler sechanle = new legend.admin.handlers.SecurityHandler();
			legend.admin.objects.User usr = sechanle.getUserByUserID(loginId);
			 String user_Name = usr.getUserName();
			 String branchuser_NameRestrict = usr.getBranchRestrict();
			 String User_Restrict = usr.getDeptRestrict();
			 String departCode = usr.getDeptCode();
			 String branch = usr.getBranch();
			
		try {
			df = new com.magbel.util.DatetimeFormat();
			ad = new AssetRecordsBean();
//			System.out.println("=====>userClass=>: " + userClass+"   ====recId: "+recId);
			if (recId.equals("null") || recId ==null){recId = "";}
			Date tranDate = new java.util.Date();
			String transDate = df.formatDate(tranDate);
			if (!userClass.equals("NULL") || userClass!=null){
			legend.admin.handlers.CompanyHandler ch = new legend.admin.handlers.CompanyHandler();
			java.util.ArrayList approvelist =ad.getApprovalsId(branch,departCode,user_Name);
//			System.out.println("cmdSave=>: " + cmdSave+"   ====recId: "+recId);
			if (cmdSave != null && recId !="") {
//				audit.select(1,
//						"SELECT * FROM  AM_GB_COMPANY ");
//				System.out.println("=====cmdSave=>: " + cmdSave+"   ====>recId=>: "+recId);
				boolean isupdt = ch.updateAssetManagerInfoTmp(ami,recId);
				out.println("<script>alert('Transaction has gone for Approval');</script>");
				out.println("<script>window.location = 'DocumentHelp.jsp?np=assetManagerInfo_Single'</script>");				
			}else {
				recId = ch.createCompanyAmiTmp(ami);
//				System.out.println("======>>>>>recId======>>>>: "+recId);
				if(!recId.equals("")) {boolean addon = ch.addOnCompanyDefaultsInfo(ami,recId);}
            	String[] pa = new String[12];
            	String recInteger = recId.substring(2);
 //           	System.out.println("======recInteger: "+recInteger);
				pa[0]=recId; pa[1]= userId; pa[2]=supervisor; pa[3]="0"; pa[4]= "";
				pa[5]= "Company Accounts Setup"; pa[6]= transDate; pa[7]= branchcode; pa[8]="PENDING"; pa[9]="Company Accounts Setup"; pa[10]="P";pa[11]=numOfTransactionLevel;
//				System.out.println("=====recId: "+recId+"     =====userId: "+userId);	
				if(userId.equals("")){userId = "0";}
				if(singleApproval.equalsIgnoreCase("Y")){
//				ad.setPendingTrans(ad.setApprovalData(venId),"1",Integer.parseInt(venId));
				ad.setPendingTransAdmin(pa,"91",Integer.parseInt(recId.substring(2)),"I");
//				ad.setPendingTransArchive(pa,"13",Integer.parseInt(recId),Integer.parseInt(recId));									
				}
				   if(singleApproval.equalsIgnoreCase("N")){
					   pa[8]="PENDING";
				  		String mtid = appHelper.getGeneratedId("am_asset_approval");
//				   		System.out.println("approvelist.size()#$$$$$$$$$$$ "+approvelist.size());
				   	 for(int j=0;j<approvelist.size();j++)
				     {  
					  	legend.admin.objects.User usrInfo = (legend.admin.objects.User)approvelist.get(j);   	 
						String supervisorId =  usrInfo.getUserId();
						String mailAddress = usrInfo.getEmail();
						String supervisorName = usrInfo.getUserName();
						String supervisorfullName = usrInfo.getUserFullName();
//						System.out.println("SupervisorId#$$$$$$$$$$$ "+supervisorId);
				  		ad.setPendingTransAdminMultiApp(pa,"91",Integer.parseInt(recId.substring(2)),"I",supervisorId,mtid);
				  	 }
				}				
				out.println("<script>alert('Record Successfully Inserted');</script>");
				out.println("<script>window.location = 'DocumentHelp.jsp?np=assetManagerInfo_Single'</script>");					
			}
//				audit.select(2,
//						"SELECT * FROM  AM_GB_COMPANY ");
//				updtst = audit.logAuditTrail("AM_GB_COMPANY", branchcode,
//						loginID, cc,hostName,ipAddress,macAddress);
//				if (updtst == true) {
//					// statusMessage = "Update on record is successful";
//					out
//							.println("<script>alert('Update on record is successful');</script>");
//					out
//							.println("<script>window.location = 'DocumentHelp.jsp?np=assetManagerInfo_Single'</script>");
//				} else {
//					// statusMessage = "No changes made on record";
//					out
//							.print("<script>alert('No changes made on record')</script>");
//					out
//							.print("<script>window.location = 'DocumentHelp.jsp?np=assetManagerInfo_Single'</script>");
//				}
			//}
		}
		} catch (Exception t) {

            System.err.println("########## ERROR OCCURED IN ASSETINFOMANAGERSERVLET##########");
			t.printStackTrace();
			// statusMessage = "Error! Record not saved.";
			out.println("<script>alert('Error! Record not saved.');</script>");
			out
					.println("<script>window.location = 'DocumentHelp.jsp?np=assetManagerInfo_Single'</script>");
		}
	}

}