package legend;

import com.magbel.util.CheckIntegerityContraint;

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
public class ZoneAuditServlet extends HttpServlet {
    public ZoneAuditServlet() {
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
     	int loginID;
		 String loginId = (String)session.getAttribute("CurrentUser");
		 if(loginId == null) {  loginID = 0; }
			else { loginID = Integer.parseInt(loginId);	}
		
		
		String branchcode = (String)session.getAttribute("UserCenter");
		if(branchcode == null) { branchcode = "not set";	}

        String buttSave = request.getParameter("buttSave");
legend.admin.handlers.AdminHandler admin = new legend.admin.handlers.AdminHandler();
        String acronym = request.getParameter("zoneAcronym");
        if(acronym != null){
            acronym = acronym.toUpperCase();
        }
legend.admin.objects.Zone zone = new legend.admin.objects.Zone();
	
        String zoneId = request.getParameter("zoneId");
        //if(zoneId == null){zoneId = "";}
		
		//String zoneStatus = request.getParameter("zoneStatus");
        //String user = (String)session.getAttribute("CurrentUser");
		
		String zoneCode = request.getParameter("zoneCode");
		String zoneName = request.getParameter("zoneName");
		String zoneAcronym  = acronym;
		String zoneAddress  = request.getParameter("zoneAddress");
		String zonePhone = request.getParameter("zonePhone");
		String zoneFax = request.getParameter("zoneFax");
		String zoneStatus = request.getParameter("zoneStatus");
		String userId =(String)session.getAttribute("CurrentUser");
		                      
       zone.setZoneCode(zoneCode);
       zone.setZoneName(zoneName);
       zone.setZoneAcronym(zoneAcronym);
       zone.setZoneAddress(zoneAddress);
       zone.setZonePhone(zonePhone);
       zone.setZoneFax(zoneFax);
       zone.setZoneStatus(zoneStatus);
       zone.setUserId(userId);
       
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
			
        try{
        	if (!userClass.equals("NULL") || userClass!=null){
            if(buttSave != null)
				{
				 if(zoneId.equals(""))
					{
                    //System.out.print(zoneId+"3");
					 if (admin.getZoneByCode(zoneCode) != null) {
							out.print("<script>alert('The ZOne code already exists .');</script>");
							out.print("<script>history.go(-1);</script>");
						} else {

							if (admin.createZone(zone)) {
								out.print("<script>alert('Record saved successfully.');</script>");
								out.print("<script>window.location = 'DocumentHelp.jsp?np=zoneSetup&zoneId="
												+ admin.getZoneByCode(zoneCode).getZoneId()
												+ "&PC=5';</script>");
							}
						}
					}
                 if(!zoneId.equals(""))
					{
                	 
                	 zone.setZoneId(zoneId);
					CheckIntegerityContraint intCont = new CheckIntegerityContraint();
          if(intCont.checkReferenceConstraint("AM_AD_BRANCH","ZONE_CODE",zoneCode,zoneStatus))
            {
             out.print("<script>alert('Zone Code is being referenced,Integerity Constraint would be violated.')</script>");
			out.print("<script>window.location = 'DocumentHelp.jsp?np=zoneSetup&zoneId="+zoneId+"&PC=5'</script>");
            }
	 else{	
                    //System.out.print(zoneId+"2");
					audit.select( 1, "SELECT * FROM  AM_AD_ZONE   WHERE zone_Id = '"+ zoneId +"'");
                    boolean isupdt = admin.updateZone(zone);
					audit.select( 2, "SELECT * FROM  AM_AD_ZONE  WHERE zone_Id = '"+ zoneId +"'");
					 updtst = audit.logAuditTrail("AM_AD_ZONE" ,  branchcode, loginID, zoneId,hostName,ipAddress,macAddress);
                        if(updtst == true)
							{
								//statusMessage = "Update on record is successful";
								out.print("<script>alert('Update on record is successful')</script>");
								out.print("<script>window.location = 'DocumentHelp.jsp?np=zoneSetup&zoneId="+zoneId+"&PC=5'</script>");
							//out.print("<script>window.location = 'manageZone.jsp?status=A'</script>");
							}
						else 
							{
								//statusMessage = "No changes made on record";
								 out.print("<script>alert('No changes made on record')</script>");
								out.print("<script>window.location = 'zoneSetup.jsp?zoneId="+zoneId+"&PC=5'</script>");
							}
					}
				}}
        }
			}  
			catch(Throwable e)
			{
				
				//statusMessage = "Ensure unique record entry";
				out.print("<script>alert('Ensure unique record entry.')</script>");
				out.print("<script>window.location = 'DocumentHelp.jsp?np=zoneSetup&zoneId="+zoneId+"&PC=5'</script>");
				e.printStackTrace();
				System.err.print(e.getMessage());
			}
    }
    /**
     * Returns a short description of the servlet.
     *
     * @return String
     */
    public String getServletInfo() {
        return "Zone Audit Servlet";
    }

}
