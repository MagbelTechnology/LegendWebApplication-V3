package legend;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.sql.Timestamp;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.util.Cryptomanager;
import com.magbel.util.HtmlUtility;

import audit.AuditTrailGen;
import magma.AssetRecordsBean;
import magma.net.dao.MagmaDBConnection;

public class SupervisorRerouteApprovalServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		MagmaDBConnection dbConnection = new MagmaDBConnection();
		HtmlUtility html = new HtmlUtility();
		AuditTrailGen audit = new AuditTrailGen();
		ApprovalRecords aprecords = new ApprovalRecords();
		HttpSession session = request.getSession();
		legend.admin.handlers.SecurityHandler security = new legend.admin.handlers.SecurityHandler();
		legend.admin.handlers.CompanyHandler company = new legend.admin.handlers.CompanyHandler();

		
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
			System.out.println("Current IP address : " + ip.getHostAddress());

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

        
		Timestamp approveddate =  dbConnection.getDateTime(new java.util.Date());
		
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

		Cryptomanager cm = new Cryptomanager();
		String userId = request.getParameter("userId");
//		System.out.println("<<<<<<=====loginId: "+loginId+"    userClass: "+userClass);
		
		String password = new String();
		 String [] passwordDisplay = null;
		
		boolean tokenRequired =false;
		
		String approvalId = request.getParameter("id");
		String oldSupervisor = request.getParameter("supervisor_name");
		 String newSupervisor = request.getParameter("new_supervisor");
		 String astatus = request.getParameter("astatus");
		 String rr = request.getParameter("reject_reason");
		 String assetCode = request.getParameter("assetCode");
		 int tranId = Integer.parseInt(request.getParameter("tranId"));
		// System.out.println("tranId : " + tranId);
		 String buttSave = request.getParameter("buttSave");
		 String RecordId  = request.getParameter("new_supervisor");
		 String approvedBy  = request.getParameter("approved_by");
//		 int id  = Integer.parseInt(request.getParameter("id"));
		 String id  = request.getParameter("id");
		 String userid = (String) session.getAttribute("CurrentUser");
		 String [] status = request.getParameterValues("status");
	    	
		 String RowId = html.getCodeName("select Class from am_gb_User where User_id=?",userId);
		 
		 //System.out.println("<<<<<<< RowId: " + RowId + " Reason: " + reason + " Confirm:" + confirm + 
		//		 " userId: " + userId + " Transaction Level: " + transaction_level);
//		  System.out.print("=======astatus: "+astatus+"   =====rr: "+rr+"    tranId: "+tranId+"  userId:"+userId+"  buttSave: "+buttSave);
       
		 try {
			 
			 AssetRecordsBean arb = new AssetRecordsBean();
			 //System.out.println("<<<<<<=====We are here");
				if (buttSave != null && astatus.equalsIgnoreCase("A")) {
					
					// System.out.println("<<<<<<=====We are here 2");
					String actPerformed = "Supervisor Reroute From: " + oldSupervisor + "$"+newSupervisor+"$"+approvedBy;
					//boolean updtst = audit.logAuditTrail("AM_SUPERVISOR_REROUTE", branchcode, Integer.parseInt(userId), RowId, RecordId,actPerformed,hostName,ipAddress,macAddress);	
//					 System.out.println("<<<<<<< RowId: " + RowId + " branchcode: " + branchcode + " RecordId:" + RecordId + 
//							 " actPerformed: " + actPerformed + " uId: " + Integer.parseInt(userId));
				//	System.out.println("<<<<<<=====status length: "+status.length);
					for(String selectedItem : status) {
//			    		System.out.println("<<<<< status length: " + status.length);
						

			    		String asset_id = request.getParameter("asset_id"+selectedItem);
			        	String tran_type = request.getParameter("tran_type"+selectedItem);
			        	String description = request.getParameter("description"+selectedItem);
			        	String tran_id = request.getParameter("transaction_Id"+selectedItem);
			        	
			    	//	int cheeckbox_Length = status[i].length();
			    		String assetId = asset_id;
			    		String tran_Id = tran_id;
			    		
			    		arb.deleteOtherSupervisors(id, userid);
//			    		System.out.println("<<<<<<=====assetId: "+assetId);
//			    		System.out.println("<<<<<<=====new supervisor: "+newSupervisor+"    approvalId: "+approvalId+"   Id: "+id);
					//String q = "update am_asset_approval set asset_status='APPROVED', super_id = ?, DATE_APPROVED = ?  where asset_id=?";
					//String a = "update am_supervisor_reroute set Status='APPROVED', Approval_Date=? where batch_id=?";
			    //		System.out.println("<<<<<<=====Query SupervisorRerouteApprovalServlet in Approved: "+"update am_asset_approval set super_id='" + newSupervisor + "' where asset_id= '" + assetId+"' and tran_type='Supervisor Reroute'");
	   			//	 String q = "update am_asset_approval set super_id='" + newSupervisor + "' where asset_id= '" + assetId+"' and process_status = 'P' ";
			    		String q = "update am_asset_approval set super_id='" + newSupervisor + "' where transaction_id= '" + tran_id+"' and process_status = 'P' ";
	   				String p = "update am_asset_approval set process_status='A', asset_status='APPROVED', Date_Approved='" + approveddate + "'  where asset_id='" + approvalId+"'and tran_type='Supervisor Reroute'";
	   				String r = "update am_supervisor_reroute set Status='APPROVED', Approval_Date='" + approveddate + "' where batch_id= '"+id+"' ";
	   				arb.updateAssetStatusChange(q);
	   				arb.updateAssetStatusChange(p);
	   				arb.updateAssetStatusChange(r);
//	   				System.out.println("<<<<<<=====q: "+q);
//	   				System.out.println("<<<<<<=====p: "+p);
//	   				System.out.println("<<<<<<=====r: "+r);
				//	html.updateAssetStatusChange(q, newSupervisor, Integer.valueOf(assetId));
				//	 html.updateAssetStatusChange(a, approveddate, id);	
					 
	   				out.print("<script>alert('Record Successfully Approved')</script>");
					out.print("<script>window.location = 'DocumentHelp.jsp?np=transactionForApprovalList'</script>");
					}	
				}
					
				if (astatus.equalsIgnoreCase("R")) {
//					 String q = "update am_asset_approval set process_status='R', asset_status='REJECTED', reject_reason=?,DATE_APPROVED = ? where transaction_id=?";
//					 String r = "update am_supervisor_reroute set Status='REJECTED', Approval_Date=? where batch_id=?";
//					 html.updateAssetStatusChange(q,rr, approveddate, tranId);
//					 html.updateAssetStatusChange(r, approveddate, id);	
//					System.out.println("<<<<<<=====Query SupervisorRerouteApprovalServlet Reject in : "+"update am_asset_approval set process_status='R', asset_status='REJECTED' where asset_id='" + approvalId+"'");
					arb.deleteOtherSupervisors(id, userid);	
					String p = "update am_asset_approval set process_status='R', asset_status='REJECTED' where asset_id='" + approvalId+"'";
		   				String r = "update am_supervisor_reroute set Status='REJECTED', Approval_Date='" + approveddate + "' where batch_id='"+id+"'";
		   				
		   				arb.updateAssetStatusChange(p);
		   				arb.updateAssetStatusChange(r);
					out.print("<script>alert('Rejection Successfull')</script>");
					out.print("<script>window.location = 'DocumentHelp.jsp?np=transactionForApprovalList'</script>");
				
				}
		 }catch(Exception e) {
			 e.getMessage();
		 }
	
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doPost(request, response);
    }

}
