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

import jakarta.servlet.http.HttpServletResponse;

import com.magbel.util.DatetimeFormat;

import audit.*;
import legend.admin.handlers.CompanyHandler;
import legend.admin.objects.Driver;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class DriverAuditServlet extends HttpServlet {
    public DriverAuditServlet() {
    }

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
    public void service(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {

        response.setContentType("text/html");
        response.setDateHeader("Expires", -1);

        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();

        //String type = request.getParameter("TYPE");
		
		String statusMessage = "";
		boolean updtst = false;

        //java.sql.Date dt = new java.sql.Date();
        AuditTrailGen  audit = new AuditTrailGen();
        //java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/mm/yyyy");
        String userClass = (String)session.getAttribute("UserClass");
        //String loginId = request.getParameter("loginId");
			 int userID;
        String userId = (String) session.getAttribute("CurrentUser");
        if (userId == null) {
            userID = 0;
        } else {
            userID = Integer.parseInt(userId);
        }
		
		
		String branchcode = (String)session.getAttribute("UserCenter");
		if(branchcode == null) { branchcode = "not set";	}

        DatetimeFormat dtf = new DatetimeFormat();

        String driverId = request.getParameter("driverId");
		
		legend.admin.objects.Driver driver = new  legend.admin.objects.Driver();		
		
       

        String buttSave = request.getParameter("buttSave");
       

        String driverCode = request.getParameter("driverCode");
        String driverLicense = request.getParameter("driverLicense");
        String issueDate = request.getParameter("dl_issue_date");
		
		String expiryDate = request.getParameter("dl_expiry_date");
        
        String lastName = request.getParameter("lastName");
        String firstName = request.getParameter("firstName");
        String otherName = request.getParameter("otherName");
        String driverBranch = request.getParameter("driverBranch");
        String driverDepart = request.getParameter("driverDepart");
        String contactAddress = request.getParameter("contactAddress");
        String driverState = request.getParameter("driverState");
        String driverPhone = request.getParameter("driverPhone");
        String driverFax = request.getParameter("driverFax");
        String driverEmail = request.getParameter("driverEmail");
        String driverStatus = request.getParameter("driverStatus");
        String driverProvince = request.getParameter("driverProvince");
        String currentUser = (String)session.getAttribute("CurrentUser");

		
		driver.setDriverCode(driverCode);
		driver.setDriverLicense(driverLicense);
		driver.setDlIssueDate(issueDate);
		driver.setDlExpiryDate(expiryDate);
		driver.setDriverLastName(lastName);
		driver.setDriverFirstname(firstName);
		driver.setDriverOtherName(otherName);
		driver.setDriverBranch(driverBranch);
		driver.setDriverDept(driverDepart);
		driver.setContatcAddress(contactAddress);
		driver.setDriverState(driverState);
		driver.setDriverPhone(driverPhone);
		driver.setDriverFax(driverFax);
		driver.setDriverEmail(driverEmail);
		driver.setDriverStatus(driverStatus);
		driver.setDriverProvince(driverProvince);
		driver.setUserId(currentUser);
		
		legend.admin.handlers.CompanyHandler  drivehandle  = new legend.admin.handlers.CompanyHandler();
       // java.util.Vector v = new java.util.Vector();
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
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
			}
			String macAddress = sb.toString();
			System.out.println(sb.toString());
			
             //System.out.print(driverId+"1");
        try{
        	 if (!userClass.equals("NULL") || userClass!=null){
            if(buttSave != null)
				{
                 if (driverId.equals("")) 
					{
                	 if (drivehandle.getDriverByDriverCode(driverCode)!= null) {
				
						out.print("<script>alert('The Driver code already exists .')</script>");
						out.print("<script>history.go(-1);</script>");
					} else {
						if (drivehandle.createDriver(driver))
    						{
								//statusMessage = "Record saved successfully.";
								out.print("<script>alert('Record saved successfully.')</script>");
								out.print("<script>window.location = 'DocumentHelp.jsp?np=companyDrivers&driverId="+(drivehandle.getDriverByDriverCode(driverCode)).getDriverId()+"&PC=32'</script>");
							}
					}
					}
				 else if (!driverId.equals(""))
					{
						//System.out.print(driverId+"3");
						//session.setAttribute("driveres", prop);
					 driver.setDriverId(driverId);
						audit.select( 1, "SELECT * FROM  AM_AD_DRIVER  WHERE driver_Id = '"+ driverId +"'");
						boolean isupdt = drivehandle.updateDriver(driver);
						audit.select( 2, "SELECT * FROM  AM_AD_DRIVER  WHERE driver_Id = '"+ driverId +"'");
						updtst = audit.logAuditTrail("AM_AD_DRIVER" ,  branchcode, userID, driverId,hostName,ipAddress,macAddress);
						if(updtst == true)
							{
								//statusMessage = "Update on record is successfull";
								out.print("<script>alert('Update on record is successful')</script>");
								out.print("<script>window.location = 'DocumentHelp.jsp?np=companyDrivers&driverId="+driverId+"&PC=32'</script>");
								//out.print("<script>window.location = 'manageBranchs.jsp?status=A'</script>");
							}
						else 
								{
								 //statusMessage = "No changes made on record";
								 out.print("<script>alert('No changes made on record')</script>");
								 out.print("<script>window.location = 'DocumentHelp.jsp?np=companyDrivers&driverId="+driverId+"&PC=32'</script>");
								}
					}
				
			}
        }
        }
		catch(Throwable e)
	    	{
				e.printStackTrace();
				//statusMessage = "Ensure unique record entry";
				out.print("<script>alert('Ensure unique record entry.')</script>");
				out.print("<script>window.location = 'DocumentHelp.jsp?np=companyDrivers'</script>");
				System.err.print(e.getMessage());
			}
    }
}
