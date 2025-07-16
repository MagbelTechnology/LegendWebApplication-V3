package legend;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletConfig;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;

import javax.servlet.http.HttpServletResponse;

import com.magbel.util.ApplicationHelper;
import com.magbel.util.DatetimeFormat;

import audit.*;
import legend.admin.handlers.CompanyHandler;
import legend.admin.objects.StockUsers;

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
public class StockUserAuditServlet extends HttpServlet {
    public StockUserAuditServlet() {
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

        //String loginId = request.getParameter("loginId");
			 int userID;
        String userId = (String) session.getAttribute("CurrentUser");
        if (userId == null) {
            userID = 0;
        } else {
            userID = Integer.parseInt(userId);
        }
		
        String userClass = (String)session.getAttribute("UserClass");
        
		String branchcode = (String)session.getAttribute("UserCenter");
		if(branchcode == null) { branchcode = "not set";	}

        DatetimeFormat dtf = new DatetimeFormat();

        String Id = request.getParameter("mtId");
		
		legend.admin.objects.StockUsers stusers = new  legend.admin.objects.StockUsers();		
		
       

        String buttSave = request.getParameter("buttSave");
       
        String compCode = request.getParameter("compCode");
        String buCode = request.getParameter("buCode");
        String undertaker = request.getParameter("undertaker");
        String createDate = request.getParameter("create_date");
        String userCode = request.getParameter("userCode");
        String utName = request.getParameter("userName");
        String utBranch = request.getParameter("userbaranch");
        String utaddress = request.getParameter("address");
        String status = request.getParameter("status");
        System.out.print("Identity Code: "+Id);
        stusers.setId(Id);
        stusers.setStockUserCode(userCode);
        stusers.setUtCode(undertaker);
        stusers.setDlIssueDate(createDate);
        stusers.setStockUserName(utName);
		stusers.setStockUserBranch(utBranch);
		stusers.setUtCode(undertaker);
		stusers.setStockUserAddress(utaddress);
		stusers.setCompCode(compCode);
		stusers.setStockUserStatus(status);
		stusers.setUserId(userId);
		System.out.println("buCode in StockUserAuditServlet: "+buCode);
		
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
			
		legend.admin.handlers.CompanyHandler  userhandle  = new legend.admin.handlers.CompanyHandler();
       // java.util.Vector v = new java.util.Vector();

             //System.out.print(driverId+"1");
        try{
        	 if (!userClass.equals("NULL") || userClass!=null){
            if(buttSave != null)
				{
                 if (Id.equals("")) 
					{
                	 String userCodeNo = (new ApplicationHelper()).getGeneratedId("ST_INVENTORY_USERS");
                	 System.out.println("userCodeNo in StockUserAuditServlet: "+userCodeNo);
                	 userCode = utBranch+userCodeNo;
                	 stusers.setStockUserCode(userCode);
                	 System.out.println("userCode in StockUserAuditServlet: "+userCode);
                	 if (userhandle.getUserByUserID(userCode)!= null) {
				
						out.print("<script>alert('The Stock Users code already exists .')</script>");
						out.print("<script>history.go(-1);</script>");
					} else {
						if (userhandle.createStockUser(stusers))
    						{
								//statusMessage = "Record saved successfully.";
								out.print("<script>alert('Record saved successfully.')</script>");
								out.print("<script>window.location = 'StockUsers.jsp?mtId="+(userhandle.getUserByUserID(userCode)).getStockUserCode()+"&PC=32'</script>");
							}
					}
					}
				 else if (!Id.equals(""))
					{
						//System.out.print(driverId+"3");
						//session.setAttribute("driveres", prop);
					 stusers.setId(Id);
						audit.select( 1, "SELECT * FROM  ST_INVENTORY_USERS  WHERE USER_CODE = '"+ Id +"'");
						boolean isupdt = userhandle.updateStockUser(stusers);
						audit.select( 2, "SELECT * FROM  ST_INVENTORY_USERS  WHERE USER_CODE = '"+ Id +"'");
						updtst = audit.logAuditTrail("ST_INVENTORY_USERS" ,  branchcode, userID, Id,hostName,ipAddress,macAddress);
						if(updtst == true)
							{
								//statusMessage = "Update on record is successfull";
								out.print("<script>alert('Update on record is successfull')</script>");
								out.print("<script>window.location = 'StockUsers.jsp?mtId="+Id+"&PC=32'</script>");
								//out.print("<script>window.location = 'manageBranchs.jsp?status=A'</script>");
							}
						else 
								{
								 //statusMessage = "No changes made on record";
								 out.print("<script>alert('No changes made on record')</script>");
								 out.print("<script>window.location = 'StockUsers.jsp?mtId="+Id+"&PC=32'</script>");
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
				out.print("<script>window.location = 'StockUsers.jsp'</script>");
				System.err.print(e.getMessage());
			}
    }
}
