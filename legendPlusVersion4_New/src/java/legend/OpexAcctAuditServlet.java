package legend;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletConfig;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServlet;
import audit.*;
import legend.admin.handlers.CompanyHandler;
import legend.admin.objects.Locations;
import legend.admin.objects.fleetType;
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
public class OpexAcctAuditServlet extends HttpServlet {
    public OpexAcctAuditServlet() {
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

        //java.sql.Date dt = new java.sql.Date();
        AuditTrailGen  audit = new AuditTrailGen();
        //java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/mm/yyyy");

		String statusMessage = "";
		boolean updtst = false;
		
        //String loginId = request.getParameter("loginId");
		int userID;
		 String userId = (String)session.getAttribute("CurrentUser");
		 if(userId == null) {  userID = 0; }
			else { userID = Integer.parseInt(userId);	}
		
		 String userClass = (String)session.getAttribute("UserClass");
		String branchcode = (String)session.getAttribute("UserCenter");
		if(branchcode == null) { branchcode = "Branch not set";	}
				
		String fleetTypeId = request.getParameter("acctTypeId");
		System.out.println("======fleetTypeId====: "+fleetTypeId);
		if(fleetTypeId == null) { fleetTypeId = "Id not set"; 	}
		
		
        String buttSave = request.getParameter("buttSave");
		 //if(locationId == null){locationId = "";}
        //String user = (String)session.getAttribute("CurrentUser");
       
         String fleetTypeCode =  request.getParameter("fleetTypeCode");
          String description =  request.getParameter("description");
          String glAccount =  request.getParameter("glAccount");
          String crAccount =  request.getParameter("crAccount");
          String typeClass =  request.getParameter("opexClass");
		  		            //            (String)session.getAttribute("CurrentUser")
		legend.admin.objects.opexAcctType  type = new  legend.admin.objects.opexAcctType();					
        
		//type.setFleetTypeId(fleetTypeId);	
		type.setTypeCode(fleetTypeCode);
		type.setTypeClass(typeClass);
		type.setDescription(description);
		type.setDrAccount(glAccount);
		type.setCrAccount(crAccount);
		type.setUserId(userId);
		//type.setCreateDate(
		      
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
			
        //java.sql.Date effectiveDate = new java.sql.Date(sdf.parse(dt.textDate()).getTime());
       legend.admin.handlers.CompanyHandler   locationhandle = new   legend.admin.handlers.CompanyHandler();
        //System.out.print(locationId+"1");
        try{
        	 if (!userClass.equals("NULL") || userClass!=null){
            if(buttSave != null)
				{
            	System.out.print("====fleetTypeId: "+fleetTypeId+"  1");
                 if(fleetTypeId.equals(""))
					{
                	 if (locationhandle.getOpexAcctTypeByTypeCode(fleetTypeCode)!=null) {
 						out.print("<script>alert('The Fleet Type with code already exists .')</script>");
 						out.print("<script>history.go(-1);</script>");
 					} else {
						if(locationhandle.createOpexAcctType(type))
							{
								//statusMessage = "Record saved successfully";
								out.print("<script>alert('Record saved successfully.')</script>");
								out.print("<script>window.location = 'DocumentHelp.jsp?np=OpexAccountMaintenance&acctTypeId="+(locationhandle.getOpexAcctTypeByTypeCode(fleetTypeCode)).getTypeId()+"&PC=62;'</script>");
							}
						else
						   	 {
								System.out.println("Error saving record: New record \nfor 'Description'  with Fleet Type name "+type.getDescription()+" could not be created"); 
								out.print("<script>history.back()</script>");
							}}
					}
                 if(!fleetTypeId.equals(""))
					{
                	 type.setTypeId(fleetTypeId);	
						System.out.print(fleetTypeId+"   SELECT * FROM  AM_GB_OPEX   WHERE MTID = '"+ fleetTypeId +"' ");
						audit.select( 1, "SELECT * FROM  AM_GB_OPEX   WHERE MTID = '"+ fleetTypeId +"'");
						boolean isupdt = locationhandle.updateOpexAcctType(type);
						audit.select( 2, "SELECT * FROM  AM_GB_OPEX  WHERE MTID = '"+ fleetTypeId +"'");
						updtst = audit.logAuditTrail("AM_GB_OPEX" ,  branchcode, userID, fleetTypeId,hostName,ipAddress,macAddress);
                        if(updtst == true)
							{
								//statusMessage = "Update on record is successfull";
								out.print("<script>alert('Update on record is successful')</script>");
								out.print("<script>window.location = 'DocumentHelp.jsp?np=OpexAccountMaintenance&acctTypeId="+fleetTypeId+"&PC=62'</script>");
								//out.print("<script>window.location = 'manageLocations.jsp?status=A'</script>");
							}
						else 
								{
								//statusMessage = "No changes made on record";
								 out.print("<script>alert('No changes made on record')</script>");
								 out.print("<script>window.location = 'OpexAccountMaintenance.jsp?acctTypeId="+fleetTypeId+"&PC=62'</script>");
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
			out.print("<script>history.back()</script>");
            System.err.print(e.getMessage());
        }
    }
    /**
     * Returns a short description of the servlet.
     *
     * @return String
     */
    public String getServletInfo() {
        return "Company Audit Servlet";
    }

}
