package legend;

import jakarta.servlet.http.HttpSession; 
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletConfig;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.sql.Timestamp;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServlet;

import com.magbel.legend.bus.ApprovalRecords;

import magma.AssetRecordsBean;
import magma.net.dao.MagmaDBConnection;
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
public class CategoryAuditApprovalServlet_17_11_2025 extends HttpServlet {
	public CategoryAuditApprovalServlet_17_11_2025() {
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
		MagmaDBConnection dbConnection = new MagmaDBConnection();
		// String type = request.getParameter("TYPE");
		StringBuffer sb = new StringBuffer();
		String statusMessage = "";
		boolean updtst = false;

		// java.sql.Date dt = new java.sql.Date();
		AuditTrailGen audit = new AuditTrailGen();
		// java.text.SimpleDateFormat sdf = new
		// java.text.SimpleDateFormat("dd/mm/yyyy");
		ApprovalRecords aprecords = new ApprovalRecords();
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
		if (branchcode == null) {
			branchcode = "not set";
		}

		legend.admin.objects.Category cat = new legend.admin.objects.Category();
		legend.admin.handlers.AdminHandler admin = new legend.admin.handlers.AdminHandler();
		RecalculateDep recal = null;
		try {
			recal = new RecalculateDep();
		} catch (Throwable e) {
		}

		String categoryId = request.getParameter("categoryId");

		String buttSave = request.getParameter("buttSave");
		String assetId = request.getParameter("assetId");
                String enforceBarcode = request.getParameter("enforceBarcode");
                  
		String recaldep = request.getParameter("rd");
		System.out.println("====recaldep: "+recaldep);
		if (recaldep == null) {
			recaldep = "N";
		}
		
		String upexassets = request.getParameter("upexassets");
		System.out.println("====upexassets: "+upexassets);
		if ( upexassets == null) {
			upexassets = "N";
		}
		String userid = (String) session.getAttribute("CurrentUser");
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
        Timestamp approveddate =  dbConnection.getDateTime(new java.util.Date());
		String categoryCode = request.getParameter("categoryCode");
		String categoryName = request.getParameter("categoryName");
		 String singleApproval = request.getParameter("singleApproval");
		String categoryAcronym = acronym;
		String requiredforFleet = reqFleet;
		 String alertmessage = "";
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
        String astatus = request.getParameter("astatus");
		String rr = request.getParameter("reject_reason");
		String recId = request.getParameter("recId");
		
		 int noOfImprovementMnths = Integer.parseInt(request.getParameter("noOfImprvMnth")); 
		 System.out.println("---------noOfImprovementMnths-----------> "+noOfImprovementMnths);
		int tranId = Integer.parseInt(request.getParameter("tranId"));
		 int assetCode = request.getParameter("assetCode") == null?0:Integer.parseInt(request.getParameter("assetCode"));
		String initiatorId = aprecords.getCodeName("select TMPID from am_ad_categoryTmp where TMPID=?",recId);
		System.out.println("---------initiatorId-----------> "+initiatorId);
		recaldep = aprecords.getCodeName("select recaldep from am_ad_categoryTmp where TMPID=?",recId);
		upexassets = aprecords.getCodeName("select upexassets from am_ad_categoryTmp where TMPID=?",recId);
		System.out.println("---------recaldep-----------> "+recaldep+"   -----upexassets----> "+upexassets);
		cat.setAccidentLedger(accidentLedger);
		cat.setAcctType(acctType);
		cat.setAccumDepLedger(accumDepLedger);
		cat.setAssetLedger(assetLedger);
		cat.setCategoryAcronym(categoryAcronym);
		cat.setCategoryClass(categoryClass);
		cat.setCategoryCode(categoryCode);
		//cat.setCategoryId(categoryId);
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
                cat.setNoOfImproveMnth(noOfImprovementMnths);

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
			AssetRecordsBean arb = new AssetRecordsBean();
			 if (!userClass.equals("NULL") || userClass!=null){
			/*
			 * for (int i = 0; i < prop.length; i += 1) {
			 * System.out.print(i+"-->"+prop[i]); }
			 */
					String tableName = "am_gb_UserTmp";
	                arb.deleteOtherSupervisorswithBatchId(String.valueOf(tranId),userid);
	              System.out.println("Value of singleApproval is >>>>>> " + singleApproval);
	              String transAvailable=arb.getCodeName("select count(*) from am_asset_approval WHERE asset_Id = '"+assetId+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
	              System.out.println("Value of transAvailable is >>>>>> " + transAvailable);
	              if (transAvailable == "0") {alertmessage = "Someone already attending to the Transaction";}
	              if (singleApproval.equalsIgnoreCase("N")) {
	              arb.setPendingMultiApprTransArchive(arb.setApprovalDataUploadGroup(Long.parseLong(recId),tableName),"75",Long.parseLong(assetId),assetCode,userid); 
//	              aprecords.updateRaiseEntry(assetId);
	              }
	              String oldRate=arb.getCodeName("select dep_rate from am_ad_category WHERE Category_Id = '"+categoryId+"'");
				 System.out.println("====buttSave=====: "+buttSave+"   categoryId: "+categoryId);
			if (buttSave != null) {
				if ((categoryId.equals("")  || categoryId.equals("0"))  && astatus.equalsIgnoreCase("A")) {
					if (admin.getCategoryByCode(categoryCode) == null) {
						if (admin.getCategoryByQuery(
								"WHERE category_acronym='" + categoryAcronym
										+ "'").size() <= 0) {
							if (admin.createCategory(cat)) {
//								System.out.println("====createCategory=====");
		   	   					 String q = "update am_asset_approval set process_status='A', asset_status='Category Creation', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
		   	   					 String r = "update am_ad_categoryTmp set RECORD_TYPE='A', Approval_status='APPROVED' where TMPID=" + recId;
		   	   					 arb.updateAssetStatusChange(q);
		   	   					 arb.updateAssetStatusChange(r);
								// statusMessage = "Record saved successfully";
								out.print("<script>alert('Record saved successfully.')</script>");
//								out.print("<script>window.location = 'DocumentHelp.jsp?np=categorySetupApproval&categoryId="
//												+ admin.getCategoryByCode(
//														categoryCode)
//														.getCategoryId()
//												+ "&PC=14'</script>");
								out.print("<script>window.location = 'DocumentHelp.jsp?np=transactionForApprovalList'</script>");
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
				} else if (!categoryId.equals("") && astatus.equalsIgnoreCase("A")) {
					cat.setCategoryId(categoryId);
					audit.select(1,
							"SELECT * FROM  AM_AD_CATEGORY  WHERE category_Id = '"
									+ categoryId + "'");
					System.out.println("====YES 1=====");
					boolean isupdt = admin.updateCategory(cat);
					System.out.println("====updateCategory====="+isupdt);
					audit.select(2,
							"SELECT * FROM  AM_AD_CATEGORY  WHERE category_Id = '"
									+ categoryId + "'");
					System.out.println("====YES 2=====");
					System.out.println("====Integer.parseInt(initiatorId)=====" + Integer.parseInt(initiatorId));
					updtst = audit.logAuditTrail("AM_AD_CATEGORYTMP", branchcode,
							Integer.parseInt(initiatorId), categoryId,hostName,ipAddress,macAddress);
					System.out.println("====updtst=====" + updtst);
					System.out.println("====YES 3=====");
					System.out.println("====recaldep: "+recaldep+"    upexassets: "+upexassets);
					recal.updateAsset(categoryId, oldRate, depRate, userId,
							recaldep, upexassets, categoryCode);
					System.out.println("====YES 4=====");
					if (updtst == true) {
						
						
  	   					 String q = "update am_asset_approval set process_status='A', asset_status='Catgeory Creation', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
  	   					 String r = "update am_ad_categoryTmp set RECORD_TYPE='A', Approval_status='APPROVED' where TMPID=" + recId;
  	   					 arb.updateAssetStatusChange(q);
  	   					 arb.updateAssetStatusChange(r);
  	   					 
						out.print("<script>alert('Update on record is successful')</script>");
						out.print("<script>window.location = 'DocumentHelp.jsp?np=transactionForApprovalList'</script>");
						// out.print("<script>window.location =
						// 'manageBranchs.jsp?status=A'</script>");
					} else {
						// statusMessage = "No changes made on record";
						out.print("<script>alert('No changes made on record')</script>");
						out.print("<script>window.location = 'DocumentHelp.jsp?np=transactionForApprovalList'</script>");
					}
				}
			
			}
			System.out.println("====>astatus: "+astatus);
			if (astatus.equalsIgnoreCase("R")) {
				 String q = "update am_asset_approval set process_status='R', asset_status='Catgeory Creation', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
				 String r = "update am_ad_categoryTmp set RECORD_TYPE='R', Approval_status='REJECTED' where TMPID=" + recId;
				 arb.updateAssetStatusChange(q);
				 arb.updateAssetStatusChange(r);
				out.print("<script>alert('Rejection Successful')</script>");
				out.print("<script>window.location = 'DocumentHelp.jsp?np=transactionForApprovalList'</script>");
			}	
		}
		} catch (Throwable e) {
			e.printStackTrace();
			out.print("<script>alert('Ensure unique record entry.')</script>");
			out.print("<script>window.location = 'DocumentHelp.jsp?np=categorySetupApproval&categoryId="
							+ categoryId + "&PC=14'</script>");
			System.err.print(e.getMessage());
		}
	}
}
