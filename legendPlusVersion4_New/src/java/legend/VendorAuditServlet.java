package legend;

import audit.AuditTrailGen;

import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Date;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import legend.admin.handlers.VendorHandler;
import legend.admin.objects.Vendor;
import magma.AssetRecordsBean;

public class VendorAuditServlet extends HttpServlet
{
	private AssetRecordsBean ad;
	com.magbel.util.DatetimeFormat df;
    public VendorAuditServlet()
    {
    }

    public void init(ServletConfig config)
        throws ServletException
    {
        super.init(config);
    }

    public void destroy()
    {
    }

    public void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        response.setContentType("text/html");
        response.setDateHeader("Expires", -1L);
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        String statusMessage = "";
        boolean updtst = false;
        AuditTrailGen audit = new AuditTrailGen();
        String loginId = (String)session.getAttribute("CurrentUser");
        int loginID = Integer.parseInt(loginId);
        String vendorId = request.getParameter("vendorId");
        VendorHandler vh = new VendorHandler();
        String branchcode = request.getParameter("branchId");
        String buttSave = request.getParameter("buttSave");
        String vendorBranchId = request.getParameter("vendorBranchId");
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
        String userId = (String)session.getAttribute("CurrentUser");
        String accountType = request.getParameter("accountType");
        String vendorProvince = request.getParameter("vendorProvince");
        String vendorCategory = request.getParameter("vendorcategory");
        String serviceType = request.getParameter("serviceType");
        String supervisor = request.getParameter("supervisor");
        String VendorCreateDate = request.getParameter("VendorCreateDate");
        String numOfTransactionLevel = request.getParameter("numOfTransactionLevel");
        String rcNo = request.getParameter("RCNo");
        String tin = request.getParameter("TIN");
        String singleApproval = request.getParameter("singleApproval");
        
        com.magbel.util.ApplicationHelper appHelper = new com.magbel.util.ApplicationHelper();
        legend.admin.handlers.SecurityHandler_07_11_2024 sechanle = new legend.admin.handlers.SecurityHandler_07_11_2024();
        Vendor vendor = new Vendor();
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
        vendor.setVendorBranchId(Integer.parseInt(vendorBranchId));
        vendor.setVendorServiceType(serviceType);
        vendor.setCreatedate(VendorCreateDate);
        vendor.setTin(tin.toUpperCase());
        vendor.setRcNo(rcNo.toUpperCase());
        String computerName = null;
        String remoteAddress = request.getRemoteAddr();
        InetAddress inetAddress = InetAddress.getByName(remoteAddress);
        computerName = inetAddress.getHostName();
        if(computerName.equalsIgnoreCase("localhost"))
        {
            computerName = InetAddress.getLocalHost().getCanonicalHostName();
        }
        String hostName = "";
        if(hostName.equals(request.getRemoteAddr()))
        {
            InetAddress addr = InetAddress.getByName(request.getRemoteAddr());
            hostName = addr.getHostName();
        }
        if(InetAddress.getLocalHost().getHostAddress().equals(request.getRemoteAddr()))
        {
            hostName = "Local Host";
        }
        InetAddress ip = InetAddress.getLocalHost();
        String ipAddress = ip.getHostAddress();
        NetworkInterface network = NetworkInterface.getByInetAddress(ip);
        byte mac[] = network.getHardwareAddress();
        if(mac == null){
            String value = "";
            mac = value.getBytes();
     }
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < mac.length; i++)
        {
            sb.append(String.format("%02X%s", new Object[] {
                Byte.valueOf(mac[i]), i >= mac.length - 1 ? "" : "-"
            }));
        }

        String macAddress = sb.toString();
        System.out.println(sb.toString());
        
		legend.admin.objects.User usr = sechanle.getUserByUserID(loginId);
		 String user_Name = usr.getUserName();
		 String branchuser_NameRestrict = usr.getBranchRestrict();
		 String User_Restrict = usr.getDeptRestrict();
		 String departCode = usr.getDeptCode();
		 String branch = usr.getBranch();
		 
        try
        {
			ad = new AssetRecordsBean();
			df = new com.magbel.util.DatetimeFormat();
			java.util.ArrayList approvelist =ad.getApprovalsId(branch,departCode,user_Name);
            if(buttSave != null)
            {
/*                if(vendorId.equals(""))
                {  */
					Date tranDate = new java.util.Date();
					System.out.println("=====tranDate: "+tranDate);
					String transDate = df.formatDate(tranDate);
					 vendor.setCreatedate(transDate);
                    String venId = vh.createVendorTmp(vendor);
                    System.out.println("=====venId: "+venId+"   numOfTransactionLevel: "+numOfTransactionLevel);
                    if(!venId.equalsIgnoreCase(""))  
                    {  
						statusMessage = "Record successfully  Sent for Approval";
						String[] pa = new String[12];

//						System.out.println("=====transDate: "+transDate);
						pa[0]=venId; pa[1]= userId; pa[2]=supervisor; pa[3]="0"; pa[4]= "";
						pa[5]= vendorName; pa[6]= transDate; pa[7]= venId; pa[8]="ACTIVE"; pa[9]="Vendor Creation"; pa[10]="P";pa[11]=numOfTransactionLevel;
//						System.out.println("=====venId: "+venId+"     =====vendorCode: "+vendorCode);	
						if(vendorCode.equals("")){vendorCode = "0";}
//						ad.setPendingTrans(ad.setApprovalData(venId),"1",Integer.parseInt(venId));
						if(singleApproval.equalsIgnoreCase("Y")){
						ad.setPendingTransAdmin(pa,"72",Integer.parseInt(venId),"I");
						ad.setPendingTransArchive(pa,"72",Integer.parseInt(venId),Integer.parseInt(venId));
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
						  		ad.setPendingTransAdminMultiApp(pa,"72",Integer.parseInt(venId),"I",supervisorId,mtid);
						  	 }
						}						
//                        statusMessage = "Record saved successfully";
                        out.print("<script>alert('Record Submitted successfully and Sent for Approval.')</script>");
                       // out.print((new StringBuilder("<script>window.location = 'DocumentHelp.jsp?np=companyVendors&vendorId=")).append(venId).append("&PC=16'</script>").toString());
                        out.print("<script>window.location = 'DocumentHelp.jsp?np=manageVendors&status=ACTIVE'</script>");
                    } else
                    {
                        System.out.println((new StringBuilder("Error saving record: New record\n for 'vendor' with vendor name ")).append(vendorId).append(" could not be created").toString());
                   //     out.print("<script>window.location = 'DocumentHelp.jsp?np=companyVendors'</script>");
                        out.print("<script>window.location = 'DocumentHelp.jsp?np=manageVendors&status=ACTIVE'</script>");
                    }
/*                } else
                if(!vendorId.equals(""))
                {
                    vendor.setVendorId(vendorId);
                    audit.select(1, (new StringBuilder("SELECT * FROM   AM_AD_VENDOR   WHERE vendor_Id = '")).append(vendorId).append("'").toString());
//                    boolean isupdt = vh.updateVendor(vendor);
                    audit.select(2, (new StringBuilder("SELECT * FROM  AM_AD_VENDOR   WHERE vendor_Id = '")).append(vendorId).append("'").toString());
                    updtst = audit.logAuditTrail("AM_AD_VENDOR", branchcode, loginID, vendorId, hostName, ipAddress, macAddress);
                    if(updtst)
                    {
                    	boolean isupdt = vh.updateVendorTmp(vendor);
//                    	boolean delete =  vh.deleteVendorRec(vendorId);
						String[] pa = new String[11];
						Date tranDate = new java.util.Date();
//						System.out.println("=====tranDate: "+tranDate);
						String transDate = df.formatDate(tranDate);
//						System.out.println("=====transDate: "+transDate);
						pa[0]=vendorCode; pa[1]= userId; pa[2]=supervisor; pa[3]="0"; pa[4]= "";
						pa[5]= vendorName; pa[6]= transDate; pa[7]= vendorCode; pa[8]="ACTIVE"; pa[9]="Vendor Creation"; pa[10]="P";
							
//						ad.setPendingTrans(ad.setApprovalData(isupdt),"1",Integer.parseInt(vendorCode));
						ad.setPendingTransAdmin(pa,"72",Integer.parseInt(vendorCode),"U");
						ad.setPendingTransArchive(pa,"72",Integer.parseInt(vendorCode),Integer.parseInt(vendorCode));
						
                        statusMessage = "Record Successfully Sent for Approval";
                        out.print("<script>alert('Update on record is successfull and Sent for Approval')</script>");
                        //out.print((new StringBuilder("<script>window.location = 'DocumentHelp.jsp?np=companyVendors&vendorId=")).append(vendorId).append("&PC=16'</script>").toString());
                        out.print("<script>window.location = 'DocumentHelp.jsp?np=manageVendors&status=ACTIVE'</script>");
                    } else
                    {
                        out.print("<script>alert('No changes made on record')</script>");
                        //out.print((new StringBuilder("<script>window.location = 'DocumentHelp.jsp?np=companyVendors&vendorId=")).append(vendorId).append("&PC=16'</script>").toString());
                        out.print("<script>window.location = 'DocumentHelp.jsp?np=manageVendors&status=ACTIVE'</script>");
                    }
                }  */
            }
        }
        catch(Throwable e)
        {
            e.printStackTrace();
            statusMessage = "Ensure unique record entry";
            out.print("<script>alert('Ensure unique record entry.')</script>");
            out.print("<script>window.location = 'DocumentHelp.jsp?np=manageVendors&status=ACTIVE'</script>");
           // out.print((new StringBuilder("<script>window.location = 'DocumentHelp.jsp?np=companyVendors&vendorId=")).append(vendorId).append("&PC=16'</script>").toString());
            System.err.print(e.getMessage());
        }
    }
}
