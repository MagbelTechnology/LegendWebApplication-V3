package legend;

import jakarta.servlet.http.HttpServlet;
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
public class VendorAuditApprovalServlet extends HttpServlet {
	public VendorAuditApprovalServlet() {
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
		String statusMessage = "";
		boolean updtst = false;
		// java.sql.Date dt = new java.sql.Date();
		AuditTrailGen audit = new AuditTrailGen();
		// java.text.SimpleDateFormat sdf = new
		// java.text.SimpleDateFormat("dd/mm/yyyy");
		ApprovalRecords aprecords = new ApprovalRecords();
		String loginId = (String) session.getAttribute("CurrentUser");
		int loginID = Integer.parseInt(loginId);

		String vendorId = request.getParameter("vendorId");
		System.out.print("=======vendorId: "+vendorId);
		legend.admin.handlers.VendorHandler vh = new legend.admin.handlers.VendorHandler();
		 Timestamp approveddate =  dbConnection.getDateTime(new java.util.Date());
		String branchcode = request.getParameter("branchId");
		String buttSave = request.getParameter("buttSave");
		String rejectBtn = request.getParameter("rejectBtn");
		String alertmessage = "";
		String assetId = request.getParameter("assetId");
		String vendorCode = request.getParameter("vendorCode");
		String vendorName = request.getParameter("vendorName");
		String contactPerson = request.getParameter("contactPerson");
		String contactAddress = request.getParameter("contactAddress");
		String vendorPhone = request.getParameter("vendorPhone");
		String vendorFax = request.getParameter("vendorFax");
		String vendorEmail = request.getParameter("vendorEmail");
		String vendorState = request.getParameter("vendorState");
		String aquisitionVendor = request.getParameter("acquisitionVendor");
		String maintenanceVendor = request.getParameter("maintenanceVendor");
		String accountNumber = request.getParameter("accountNumber");
		String vendorStatus = request.getParameter("vendorStatus");
		String userId = (String) session.getAttribute("CurrentUser");
		String accountType = request.getParameter("accountType");
		String vendorProvince = request.getParameter("vendorProvince");
		String vendorCategory = request.getParameter("vendorcategory");
		String vendorServiceType = request.getParameter("serviceType");
		String VendorCreateDate = request.getParameter("VendorCreateDate");
		String astatus = request.getParameter("astatus");
		String singleApproval = request.getParameter("singleApproval");
		String rr = request.getParameter("reject_reason");
		int tranId = Integer.parseInt(request.getParameter("tranId"));
        String rcNo = request.getParameter("RCNo");
        String tin = request.getParameter("TIN");
        vendorStatus = aprecords.getCodeName("select VENDOR_STATUS from am_ad_vendortmp where VENDOR_ID='"+vendorId+"'");
        // vendorID = request.getParameter("vendorID");
		System.out.print("=======astatus: "+astatus+"   =====rr: "+rr+"    tranId: "+tranId+"   buttSave: "+buttSave+"   rejectBtn: "+rejectBtn+"  rcNo: "+rcNo+"    tin: "+tin);
       // if(request.getParameter("vendorID") == null){
		String dd = VendorCreateDate.substring(0, 2);
		System.out.println("======>dd: "+dd);
		String mm = VendorCreateDate.substring(3, 5);
		System.out.println("======>mm: "+mm);
		String yyyy = VendorCreateDate.substring(6, 10);
		VendorCreateDate = yyyy+"/"+mm+"/"+dd;
		System.out.println("======>VendorCreateDate: "+VendorCreateDate);
		int assetCode = request.getParameter("assetCode") == null?0:Integer.parseInt(request.getParameter("assetCode"));
       // vendorID =
        //}

		legend.admin.objects.Vendor vendor = new legend.admin.objects.Vendor();

		vendor.setAccountNumber(accountNumber);
		vendor.setAccountType(accountType);
		vendor.setAquisitionVendor(aquisitionVendor);
		vendor.setContactAddress(contactAddress);
		vendor.setContactPerson(contactPerson);
		vendor.setMaintenanceVendor(maintenanceVendor);
		vendor.setUserId(userId);
		vendor.setVendorCode(vendorCode);
		vendor.setVendorEmail(vendorEmail);
		vendor.setVendorFax(vendorFax);
		vendor.setVendorName(vendorName.toUpperCase());
		vendor.setVendorPhone(vendorPhone);
		vendor.setVendorProvince(vendorProvince);
		vendor.setVendorState(vendorState);
		vendor.setVendorStatus(vendorStatus);
		vendor.setVendorCategory(vendorCategory);
		vendor.setVendorServiceType(vendorServiceType);
		
		vendor.setCreatedate(VendorCreateDate);
        vendor.setTin(tin.toUpperCase());
        vendor.setRcNo(rcNo.toUpperCase());
        
        String computerName = null;
        String remoteAddress = request.getRemoteAddr();
        InetAddress inetAddress = InetAddress.getByName(remoteAddress);
//        System.out.println("inetAddress: " + inetAddress);
        computerName = inetAddress.getHostName();
//        System.out.println("computerName: " + computerName);
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
//			System.out.println("Current IP address : " + ip.getHostAddress());

			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
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
			AssetRecordsBean arb = new AssetRecordsBean();
			
			String tableName = "am_ad_vendorTmp";
            arb.deleteOtherSupervisorswithBatchId(String.valueOf(tranId),loginId);
          System.out.println("Value of singleApproval is >>>>>> " + singleApproval);
          String transAvailable=arb.getCodeName("select count(*) from am_asset_approval WHERE asset_Id = '"+assetId+"' and super_Id = '"+loginId+"'  and asset_status = 'PENDING' ");
          System.out.println("Value of transAvailable is >>>>>> " + transAvailable);
          if (transAvailable == "0") {alertmessage = "Someone already attending to the Transaction";}
          if (singleApproval.equalsIgnoreCase("N")) {
          arb.setPendingMultiApprTransArchive(arb.setApprovalDataUploadGroup(Long.parseLong(vendorId),tableName),"72",Integer.parseInt(assetId),assetCode,loginId); 
          aprecords.updateRaiseEntry(assetId);
          }
          
//			System.out.print("=====buttSave: "+buttSave+" ===astatus:  "+astatus);
			if (buttSave != null && astatus.equalsIgnoreCase("A")) {
				System.out.print(vendorId+" ===1  "+vendorCode+"   astatus: "+astatus);
				if (vendorCode.equals("")) {
//					 System.out.print(vendorId+" ===2  "+vendorCode+"   vendorId ");
					//--if (vh.getVendorByVendorCode(vendorCode) != null) {
						//--out
						//--		.print("<script>alert('Vendor Code is in Use.')</script>");
						//--out.print("<script>history.back()</script>");
					//--} else {

                        String venId=vh.createVendor(vendor);
	   					 String q = "update am_asset_approval set process_status='A', asset_status='ACTIVE', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
	   					 String r = "update am_ad_vendorTmp set RECTYPE='A', Vendor_status='APPROVED' where RECTYPE IS NULL AND VENDOR_ID=" + vendorId;
	   					 arb.updateAssetStatusChange(q);
	   					 arb.updateAssetStatusChange(r);
						if (!venId.equalsIgnoreCase("")) {
							statusMessage = "Record Approved successfully";
							out
									.print("<script>alert('Record saved successfully.')</script>");
							out
							.print("<script>window.location = 'DocumentHelp.jsp?np=transactionForApprovalList'</script>");
                                                              //  + vendorId + "&PC=16'</script>");
                                                    //System.out.println(">>>>>>>>>>>>>>>>>>>>> vh.getVendorByVendorCode(vendorCode).getVendorId() " +venId);

						} else {
							System.out
									.println("Error saving record: New record\n for 'vendor' with vendor name "
											+ vendorId
											+ " could not be created");
							out
									.print("<script>window.location = 'DocumentHelp.jsp?np=transactionForApprovalList'</script>");
						}
					//--}
				} else if (!vendorCode.equals("")) {
					System.out.print(vendorId+" ===3  "+vendorCode+"   vendorId ");
					// session.setAttribute("vendores", prop);
					vendor.setVendorId(vendorId);  
//					boolean delete =  vh.deleteVendorRec(vendorCode);
//					audit.select(1,
//							"SELECT * FROM   AM_AD_VENDOR   WHERE vendor_Id = '"
//									+ vendorId + "'");
					boolean isupdt =vh.updateVendorFromVendorTMP(vendor);
//					String venId=vh.createVendorFromTmp(vendor);
//					System.out.println("=====isupdt: "+isupdt+"   vendorCode: "+vendorCode);
					 String q = "update am_asset_approval set process_status='A', asset_status='ACTIVE', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
					 String r = "update am_ad_vendorTmp set RECTYPE='A', Vendor_status='APPROVED' where RECTYPE IS NULL AND VENDOR_CODE=" + vendorCode;
					 arb.updateAssetStatusChange(q);
					 arb.updateAssetStatusChange(r);
//					boolean isapprove = vh.VendorRecApproval(vendorCode);
//					audit.select(2,
//							"SELECT * FROM  AM_AD_VENDOR   WHERE vendor_Id = '"
//									+ vendorId + "'");
//					updtst = audit.logAuditTrail("AM_AD_VENDOR", branchcode,
//							loginID, vendorId);
//					updtst = audit.logAuditTrail("AM_AD_VENDOR", branchcode,
//							loginID, vendorId,hostName,ipAddress,macAddress);
//					if (updtst == true) {
						statusMessage = "Approval is successfull";
						out
								.print("<script>alert('Record Successfully Approved')</script>");
						out
								.print("<script>window.location = 'DocumentHelp.jsp?np=transactionForApprovalList'</script>");
			/*		} else {
						// statusMessage = "No changes made on record";
						out
								.print("<script>alert('No changes made on record')</script>");
						out
								.print("<script>window.location = 'DocumentHelp.jsp?np=manageVendors&status=ACTIVE'</script>");
					}  */
				}
			}
			if (astatus.equalsIgnoreCase("R")) {
				
				 String q = "update am_asset_approval set process_status='R', asset_status='REJECTED', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
				 String r = "update am_ad_vendorTmp set RECTYPE='R', Vendor_status='REJECTED' where VENDOR_CODE=" + vendorCode;
				 arb.updateAssetStatusChange(q);
				 arb.updateAssetStatusChange(r);
				out.print("<script>alert('Rejection Successfull')</script>");
				out.print("<script>window.location = 'DocumentHelp.jsp?np=transactionForApprovalList'</script>");
			}
		} catch (Throwable e) {
			e.printStackTrace();
			statusMessage = "Ensure unique record entry";
			out.print("<script>alert('Ensure unique record entry.')</script>");
			out.print("<script>window.location = 'DocumentHelp.jsp?np=transactionForApprovalList'</script>");
			System.err.print(e.getMessage());
		}
	}
}
