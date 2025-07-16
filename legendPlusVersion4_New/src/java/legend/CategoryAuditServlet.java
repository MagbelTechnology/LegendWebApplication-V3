package legend;

import jakarta.servlet.http.HttpSession; 
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletConfig;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Date;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServlet;
import magma.AssetRecordsBean;
import audit.*;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class CategoryAuditServlet extends HttpServlet {
	com.magbel.util.DatetimeFormat df;
	private AssetRecordsBean ad;
	public CategoryAuditServlet() {
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

	/**
	 * Destroys the servlet.
	 */
	public void destroy() {

	}

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		response.setDateHeader("Expires", -1);

		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();

		// String type = request.getParameter("TYPE");
		StringBuffer sb = new StringBuffer();
		String statusMessage = "";
		boolean updtst = false;

		// java.sql.Date dt = new java.sql.Date();
		AuditTrailGen audit = new AuditTrailGen();
		// java.text.SimpleDateFormat sdf = new
		// java.text.SimpleDateFormat("dd/mm/yyyy");

		// String loginId = request.getParameter("loginId");
		int loginID;
		String loginId = (String) session.getAttribute("CurrentUser");
		if (loginId == null) {
			loginID = 0;
		} else {
			loginID = Integer.parseInt(loginId);
		}
		   String userClass = (String)session.getAttribute("UserClass");
		String branchcode = (String) session.getAttribute("UserCenter");
		
		legend.admin.handlers.SecurityHandler sechanle = new legend.admin.handlers.SecurityHandler();
		if (branchcode == null) {
			branchcode = "not set";
		}
		legend.admin.objects.User usr = sechanle.getUserByUserID(loginId);
		 String user_Name = usr.getUserName();
		 String branchuser_NameRestrict = usr.getBranchRestrict();
		 String User_Restrict = usr.getDeptRestrict();
		 String departCode = usr.getDeptCode();
		 String branch = usr.getBranch();
		 
		legend.admin.objects.Category cat = new legend.admin.objects.Category();
		legend.admin.handlers.AdminHandler admin = new legend.admin.handlers.AdminHandler();
		com.magbel.util.ApplicationHelper appHelper = new com.magbel.util.ApplicationHelper();
		RecalculateDep recal = null;
		try {
			recal = new RecalculateDep();
		} catch (Throwable e) {
		}

		String categoryId = request.getParameter("categoryId");

		String buttSave = request.getParameter("buttSave");
		String singleApproval = request.getParameter("singleApproval");
                String enforceBarcode = request.getParameter("enforceBarcode");
                  
		String recaldep = request.getParameter("rd");
		if (recaldep == null) {
			recaldep = "N";
		}

		String upexassets = request.getParameter("uea");
		if ( upexassets == null) {
			upexassets = "N";
		}

		String residualchange = request.getParameter("residualchange");
		if ( residualchange == null) {
			residualchange = "N";
		}
		
		String acronym = request.getParameter("categoryAcronym");
		if (acronym != null) {
			acronym = acronym.toUpperCase();
		}
		String reqFleet = request.getParameter("requiredForFleet");
		if (reqFleet == "" || reqFleet == null) {
			reqFleet = "N";
		}
                if (enforceBarcode == "" || enforceBarcode == null) {
			enforceBarcode = "N";
		}

		String categoryCode = request.getParameter("categoryCode");
		String categoryName = request.getParameter("categoryName");
		String categoryAcronym = acronym;
		String requiredforFleet = reqFleet;
		String categoryClass = request.getParameter("categoryClass");
		String pmCyclePeriod = (request.getParameter("pmCyclePeriod")
				.equals("") ? "0" : request.getParameter("pmCyclePeriod"));
		String mileage = (request.getParameter("mileage").equals("") ? "0"
				: request.getParameter("mileage"));
		String notifyMaintdays = (request.getParameter("notifyMaintDays")
				.equals("") ? "0" : request.getParameter("notifyMaintDays"));
		String notifyEveryDays = (request.getParameter("notifyEveryDays")
				.equals("") ? "0" : request.getParameter("notifyEveryDays"));
		String residualValue = request.getParameter("residualValue");
		String depRate = request.getParameter("depRate");
		String assetLedger = request.getParameter("assetLedger");
		String depLedger = request.getParameter("depLedger");
		String accumDepLedger = request.getParameter("accumDepLedger");
		String glAccount = request.getParameter("glAccount");
		String insuranceAcct = request.getParameter("insuranceAcct");
		String licenseLedger = request.getParameter("licenseAcct");
		String fuelLedger = request.getParameter("fuelAcct");
		String accidentLedger = request.getParameter("accidentAcct");
		String categoryStatus = request.getParameter("categoryStatus");
		String userId = (String) session.getAttribute("CurrentUser");
		String acctType = request.getParameter("acc_type");
		String currencyId = request.getParameter("currency");
        String categoryType = request.getParameter("categoryType");
        String supervisor = request.getParameter("supervisor");
        String numOfTransactionLevel = request.getParameter("numOfTransactionLevel");
//System.out.println("----------->--------------------------------->"+categoryType);
		cat.setAccidentLedger(accidentLedger);
		cat.setAcctType(acctType);
		cat.setAccumDepLedger(accumDepLedger);
		cat.setAssetLedger(assetLedger);
		cat.setCategoryAcronym(categoryAcronym);
		cat.setCategoryClass(categoryClass);
		cat.setCategoryCode(categoryCode);
		cat.setCategoryId(categoryId);
		cat.setCategoryName(categoryName);
		cat.setCategoryStatus(categoryStatus);
		cat.setDepLedger(depLedger);
		cat.setDepRate(depRate);
		cat.setFuelLedger(fuelLedger);
		cat.setGlAccount(glAccount);
		cat.setInsuranceAcct(insuranceAcct);
		cat.setLicenseLedger(licenseLedger);
		cat.setMileage(mileage);
		cat.setNotifyEveryDays(notifyEveryDays);
		cat.setNotifyMaintdays(notifyMaintdays);
		cat.setPmCyclePeriod(pmCyclePeriod);
		cat.setRequiredforFleet(requiredforFleet);
		cat.setResidualValue(residualValue);
		cat.setUserId(userId);
		cat.setCurrencyId(currencyId);
        cat.setEnforceBarcode(enforceBarcode);
        cat.setCategoryType(categoryType);
        cat.setUpexassets(upexassets);
        cat.setRecaldep(recaldep);
        cat.setResidualchange(residualchange);
        
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
        	        if(mac == null){
     	               String value = "";
     	               mac = value.getBytes();
        	        }
        			//StringBuilder sb = new StringBuilder();
        			for (int i = 0; i < mac.length; i++) {
        				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
        			}
        			String macAddress = sb.toString();
        			System.out.println(sb.toString());
        			
		try {
			ad = new AssetRecordsBean();
			df = new com.magbel.util.DatetimeFormat();
			
			java.util.ArrayList approvelist =ad.getApprovalsId(branch,departCode,user_Name);
			
			 if (!userClass.equals("NULL") || userClass!=null){
			/*
			 * for (int i = 0; i < prop.length; i += 1) {
			 * System.out.print(i+"-->"+prop[i]); }
			 */
					Date tranDate = new java.util.Date();
					System.out.println("=====tranDate: "+tranDate);
					String transDate = df.formatDate(tranDate);
					System.out.println("=====tranDate====>>>: "+tranDate);
					cat.setCreateDate(transDate);
			if (buttSave != null) {

				if (categoryId.equals("")) {
					if (admin.getCategoryByCode(categoryCode) == null) {
						if (admin.getCategoryByQuery(
								"WHERE category_acronym='" + categoryAcronym
										+ "'").size() <= 0) {
							categoryId = "0";
							cat.setCategoryId(categoryId);
							String recId = admin.createCategoryTmp(cat);
//							if (admin.createCategoryTmp(cat)) {
								if(!recId.equalsIgnoreCase("")) {
								// statusMessage = "Record saved successfully";
									
									statusMessage = "Record successfully  Sent for Approval";
									String[] pa = new String[12];

//									System.out.println("=====transDate: "+transDate);
									pa[0]=recId; pa[1]= userId; pa[2]=supervisor; pa[3]="0"; pa[4]= "";
									pa[5]= categoryName; pa[6]= transDate; pa[7]= recId; pa[8]="PENDING"; pa[9]="Category Creation"; pa[10]="P";pa[11]=numOfTransactionLevel;
									System.out.println("=====recId: "+recId+"     =====userId: "+userId);	
									if(userId.equals("")){userId = "0";}
									if(singleApproval.equalsIgnoreCase("Y")){
//									ad.setPendingTrans(ad.setApprovalData(venId),"1",Integer.parseInt(venId));
									ad.setPendingTransAdmin(pa,"76",Integer.parseInt(recId),"I");
		//							ad.setPendingTransArchive(pa,"76",Integer.parseInt(recId),Integer.parseInt(recId));									
									}
									   if(singleApproval.equalsIgnoreCase("N")){
										   pa[8]="PENDING";
									  		String mtid = appHelper.getGeneratedId("am_asset_approval");
//									   		System.out.println("approvelist.size()#$$$$$$$$$$$ "+approvelist.size());
									   	 for(int j=0;j<approvelist.size();j++)
									     {  
										  	legend.admin.objects.User usrInfo = (legend.admin.objects.User)approvelist.get(j);   	 
											String supervisorId =  usrInfo.getUserId();
											String mailAddress = usrInfo.getEmail();
											String supervisorName = usrInfo.getUserName();
											String supervisorfullName = usrInfo.getUserFullName();
//											System.out.println("SupervisorId#$$$$$$$$$$$ "+supervisorId);
									  		ad.setPendingTransAdminMultiApp(pa,"76",Integer.parseInt(recId),"I",supervisorId,mtid);
									  	 }
									}
								out.print("<script>alert('Record saved successfully.')</script>");
//								out.print("<script>window.location = 'DocumentHelp.jsp?np=categorySetup&categoryId="
//												+ admin.getCategoryByCode(
//														categoryCode)
//														.getCategoryId()
//												+ "&PC=14'</script>");
								out.print("<script>window.location = 'DocumentHelp.jsp?np=manageCategories&status=ACTIVE'</script>");
							}
						} else {
							// StringBuffer sb
							// statusMessage = "Category with Category Acronym"+
							out
									.print("<script>alert('Category with Category Acronym "
											+ categoryAcronym
											+ " Exists Already!')</script>");
							out.print("<script>window.history.back()</script>");
						}
					} else {
						out.print("<script>alert('Category with Category Code "
								+ categoryCode + " Exists Already!')</script>");
						out.print("<script>window.history.back()</script>");
					}
				} else if (!categoryId.equals("")) {
					
					String recId = admin.createCategoryTmp(cat);
					if(!recId.equalsIgnoreCase("")) {
					// statusMessage = "Record saved successfully";
						
						statusMessage = "Record successfully  Sent for Approval";
						String[] pa = new String[12];

//						System.out.println("=====transDate: "+transDate);
						pa[0]=recId; pa[1]= userId; pa[2]=supervisor; pa[3]="0"; pa[4]= "";
						pa[5]= categoryName; pa[6]= transDate; pa[7]= recId; pa[8]="PENDING"; pa[9]="Category Creation"; pa[10]="P";pa[11]=numOfTransactionLevel;
						System.out.println("=====recId: "+recId+"     =====userId: "+userId);	
						if(userId.equals("")){userId = "0";}
						if(singleApproval.equalsIgnoreCase("Y")){
//						ad.setPendingTrans(ad.setApprovalData(venId),"1",Integer.parseInt(venId));
						ad.setPendingTransAdmin(pa,"76",Integer.parseInt(recId),"I");
//						ad.setPendingTransArchive(pa,"75",Integer.parseInt(recId),Integer.parseInt(recId));									
						}
						   if(singleApproval.equalsIgnoreCase("N")){
							   pa[8]="PENDING";
						  		String mtid = appHelper.getGeneratedId("am_asset_approval");
//						   		System.out.println("approvelist.size()#$$$$$$$$$$$ "+approvelist.size());
						   	 for(int j=0;j<approvelist.size();j++)
						     {  
							  	legend.admin.objects.User usrInfo = (legend.admin.objects.User)approvelist.get(j);   	 
								String supervisorId =  usrInfo.getUserId();
								String mailAddress = usrInfo.getEmail();
								String supervisorName = usrInfo.getUserName();
								String supervisorfullName = usrInfo.getUserFullName();
//								System.out.println("SupervisorId#$$$$$$$$$$$ "+supervisorId);
						  		ad.setPendingTransAdminMultiApp(pa,"76",Integer.parseInt(recId),"I",supervisorId,mtid);
						  	 }
						}
						out.print("<script>alert('Update on record is successful')</script>");
//						out.print("<script>window.location = 'DocumentHelp.jsp?np=categorySetup&categoryId="
//										+ categoryId + "&PC=14'</script>");
						out.print("<script>window.location = 'DocumentHelp.jsp?np=manageCategories&status=ACTIVE'</script>");
						// out.print("<script>window.location =
						// 'manageBranchs.jsp?status=A'</script>");
					} else {
						// statusMessage = "No changes made on record";
						out
								.print("<script>alert('No changes made on record')</script>");
						out
								.print("<script>window.location = 'DocumentHelp.jsp?np=categorySetup&categoryId="
										+ categoryId + "&PC=14'</script>");
					}
				}
			}
		}
		} catch (Throwable e) {
			e.printStackTrace();
			out.print("<script>alert('Ensure unique record entry.')</script>");
			out
					.print("<script>window.location = 'DocumentHelp.jsp?np=categorySetup&categoryId="
							+ categoryId + "&PC=14'</script>");
			System.err.print(e.getMessage());
		}
	}
}
