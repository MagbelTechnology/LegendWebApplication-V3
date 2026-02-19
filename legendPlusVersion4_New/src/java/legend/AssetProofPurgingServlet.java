package legend;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import audit.AuditTrailGen;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.util.ApplicationHelper;
import com.magbel.util.Cryptomanager; 

import legend.admin.handlers.SecurityHandler; 
import magma.AssetRecordsBean;
import legend.purge.PurgeHandler;
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
 * Copyright: Copyright (c) 2025
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * 
 * @author Lekann Matanmi
 * @version 4.0
 */  

public class AssetProofPurgingServlet extends HttpServlet {
	com.magbel.util.DatetimeFormat df;
	private AssetRecordsBean ad;
	private PurgeHandler purge;
//	private ApplicationHelper appHelper;
	public AssetProofPurgingServlet() {
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

		String statusMessage = "";
		boolean updtst = false;
  
		 
			Properties prop = new Properties();
			File file = new File("C:\\Property\\LegendPlus.properties");
			FileInputStream input = new FileInputStream(file);
			prop.load(input);
			String singleApproval = prop.getProperty("singleApproval");
			
//		legend.purge.PurgeHandler purge = new legend.purge.PurgeHandler();
		legend.admin.handlers.CompanyHandler company = new legend.admin.handlers.CompanyHandler();
		com.magbel.util.ApplicationHelper appHelper = new com.magbel.util.ApplicationHelper();
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
			
		int min = company.getCompany().getMinimumPassword();
		int passexpiry = company.getCompany().getPasswordExpiry();

		AuditTrailGen audit = new AuditTrailGen();

		ApprovalRecords aprecords = new ApprovalRecords();
		legend.admin.handlers.SecurityHandler sechanle = new legend.admin.handlers.SecurityHandler();
		int loginID;
		String loginId = (String) session.getAttribute("CurrentUser");
		String userClass = (String) session.getAttribute("UserClass");
		String password0 = request.getParameter("password");
		if (loginId == null) {
			loginID = 0;
		} else {
			loginID = Integer.parseInt(loginId);
		}

		String branchcode = (String) session.getAttribute("UserCenter");
		if (branchcode == null) {
			branchcode = "not set";
		}
		legend.admin.objects.User usr = sechanle.getUserByUserID(loginId);
		 String user_Name = usr.getUserName();
		 String branchuser_NameRestrict = usr.getBranchRestrict();
		 String User_Restrict = usr.getDeptRestrict();
		 String departCode = usr.getDeptCode();
		 String branch = usr.getBranch();

		Cryptomanager cm = new Cryptomanager();
		String userId = request.getParameter("userId");
		System.out.println("<<<<<<=====userId: "+userId+"    userClass: "+userClass+"   loginId: "+loginId);
		if(userId==null) {userId = loginId;}
		String password = new String();
		 String [] passwordDisplay = null;
		try {

		} catch (Exception e) {
		}
		boolean tokenRequired =false;

//		String fullname = encodeForHTML(request.getParameter("fullName"));
		String fullname = "Asset Verification Purging";
		String archivingData = request.getParameter("archivingData");
		String sappingData = request.getParameter("sappingData");
//		System.out.println("<<<<<<=====expiry_Days: "+expiry_Days);
//        int expiryDays = (request.getParameter("expiryDays")== null || request.getParameter("expiryDays")== "")?0:Integer.parseInt(request.getParameter("expiryDays"));
//		System.out.println("<<<<<<=====expiryDays: "+expiryDays);
        String expiryDate = request.getParameter("expiryDate");
        
        String numOfTransactionLevel = request.getParameter("numOfTransactionLevel");
         String supervisor = request.getParameter("supervisor");
         String approveLevel = request.getParameter("approveLevel");
         int superId = (request.getParameter("supervisor")== null || request.getParameter("supervisor")== "")?0:Integer.parseInt(request.getParameter("supervisor"));
//         System.out.println("<<<<<<=====supervisor: "+supervisor+"    superId: "+superId);
         supervisor = Integer.toString(superId);
//         if(supervisor.equals("null") || (supervisor==null) || (supervisor=="null")){supervisor = "0";}
		try {
			ad = new AssetRecordsBean();
			df = new com.magbel.util.DatetimeFormat();
			purge = new PurgeHandler();
//			System.out.println("<<<<<<<<=======branch: "+branch+"   departCode: "+departCode+"   user_Name: "+user_Name);
			java.util.ArrayList approvelist =ad.getApprovalsId(branch,departCode,user_Name);
			if (!userClass.equals("NULL") || userClass!=null){
//			min = 2;
//			System.out.println("password0 ============================ "+password0+"  Min: "+min+"   userId: "+userId+"    menuURL: "+menuURL);
//				if (userId.equals("")) {
				Date tranDate = new java.util.Date();
				String action = "";
//				System.out.println("=====tranDate: "+tranDate);
				String transDate = df.formatDate(tranDate);
				String recId = (new ApplicationHelper()).getGeneratedId("Asset_ProofPurging_Temp");
//				System.out.println("=====recId: "+recId+"      User Id: "+userId+"   archivingData==>: "+archivingData);
					if(archivingData.equals("Y")) {action = "Archiving Verification Data ";					
					 recId = purge.createAssetProofPurgingTransaction(recId,action,userId);
					}
//					System.out.println("=====archivingData: "+archivingData+"      User Id: "+userId+"      action: "+action+"   recId: "+recId);
					if(sappingData.equals("Y")) {action = "Sapping Verification Data";
					recId = purge.createAssetProofPurgingTransaction(recId,action,userId);
					}
						
//								System.out.println("=====recId: "+recId+" New Users");
			                    if(!recId.equalsIgnoreCase(""))  
			                    {  
									statusMessage = "Record successfully Sent for Approval";
									String[] pa = new String[12];

//									System.out.println("=====transDate: "+transDate);
									pa[0]=recId; pa[1]= loginId; pa[2]=supervisor; pa[3]="0"; pa[4]= "";
									pa[5]= fullname; pa[6]= transDate; pa[7]= recId; pa[8]="PENDING"; pa[9]="Asset Proof Purging"; pa[10]="P";pa[11]=numOfTransactionLevel;
//									if(userId.equals("")){userId = "0";}
//									System.out.println("=====<<singleApproval>>>>>===: "+singleApproval);
									if(singleApproval.equalsIgnoreCase("Y")){
//									ad.setPendingTrans(ad.setApprovalData(venId),"1",Integer.parseInt(venId));
									ad.setPendingTransAdmin(pa,"88",Integer.parseInt(recId),"I");
//									System.out.println("=====<<2>>>>>===: ");
									ad.setPendingTransArchive(pa,"88",Integer.parseInt(recId),Integer.parseInt(recId));
//									System.out.println("=====<<3>>>>>===: ");
									 }
									   if(singleApproval.equalsIgnoreCase("N")){
										   pa[8]="PENDING";
//										   System.out.println("=====<<4>>>>>===: ");
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
									  		ad.setPendingTransAdminMultiApp(pa,"88",Integer.parseInt(recId),"I",supervisorId,mtid);
									  	 }
									}
//			                        statusMessage = "Record saved successfully";
			                        out.print("<script>alert('Asset Purging Record Submitted successfully and Sent for Approval.')</script>");
			                       // out.print((new StringBuilder("<script>window.location = 'DocumentHelp.jsp?np=companyVendors&vendorId=")).append(venId).append("&PC=16'</script>").toString());
			                        out.print("<script>window.location = 'DocumentHelp.jsp?np=AssetVerificationPurging'</script>");
			                    } else
			                    {
			                        System.out.println((new StringBuilder("Error saving record: New record\n for 'Purging' with Supervisor ")).append(recId).append(" could not be created").toString());
			                   //     out.print("<script>window.location = 'DocumentHelp.jsp?np=companyVendors'</script>");
			                        out.print("<script>window.location = 'DocumentHelp.jsp?np=AssetVerificationPurging'</script>");
			                    }
//						} 

		}else {
			out.print("<script>alert('Session Time Out')</script>");
			out.print("<script>window.location = 'DocumentHelp.jsp?np=AssetVerificationPurging&PC=11'</script>");
		} 
		
		} catch (Throwable e) {
			e.printStackTrace();
			// statusMessage = "Ensure unique record entry";
			out.print("<script>alert('Ensure unique record entry. Or Session Time Out')</script>");
			System.err.print(e.getMessage());
			out.print("<script>window.location = 'DocumentHelp.jsp?np=AssetVerificationPurging&PC=11'</script>");
		}
	}

}
