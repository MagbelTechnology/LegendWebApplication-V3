package legend;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

import jakarta.servlet.ServletException;

import com.magbel.util.Cryptomanager;

import jakarta.servlet.ServletConfig;

import com.magbel.util.DatetimeFormat;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;

import jakarta.servlet.http.HttpServletResponse;
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
public class FuelAuditServlet extends HttpServlet {
    public FuelAuditServlet() {
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
    public void service(HttpServletRequest request,
                        HttpServletResponse response) throws
            ServletException, IOException {

        response.setContentType("text/html");
        response.setDateHeader("Expires", -1);

        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();

        //String type = request.getParameter("TYPE");
        String statusMessage = "";
        boolean updtst = false;

        //java.sql.Date dt = new java.sql.Date();
        AuditTrailGen audit = new AuditTrailGen();
        //java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/mm/yyyy");

        //String loginId = request.getParameter("loginId");
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
legend.admin.handlers.FuelTypeHandler fuelHandler = new legend.admin.handlers.FuelTypeHandler();
        String fuelId = request.getParameter("fuelId");

        String buttSave = request.getParameter("buttSave");
        
    	 String fuelCode= request.getParameter("fuelCode");
    	 String description=request.getParameter("fuelDesc");
    	 String volume= request.getParameter("fuelVolume");
    	 String fuelPrice= request.getParameter("fuelPrice");
    	 String acccountTpye= request.getParameter("accountType");
    	 String suspenseAccount= request.getParameter("suspenseAcct");
    	 String fuelStatus=request.getParameter("fuelStatus");
    	 String userId=(String) session.getAttribute("CurrentUser");
    	legend.admin.objects.FuelType fueltype = new legend.admin.objects.FuelType();
    	fueltype.setFuelCode(fuelCode);
    	fueltype.setFuelPrice(fuelPrice);
    	fueltype.setVolume(volume);
    	fueltype.setDescription(description);
    	fueltype.setAcccountTpye(acccountTpye);
    	fueltype.setSuspenseAccount(suspenseAccount);
    	fueltype.setUserId(userId);
    	fueltype.setFuelStatus(fuelStatus);

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
			
        try {
        	 if (!userClass.equals("NULL") || userClass!=null){
            
            if (buttSave != null) {
                if (fuelId.equals("")) {
                	if (fuelHandler.getFuelTypeByFuelCode(fuelCode)!= null) {
						out.print("<script>alert('The Fuel code already exists .')</script>");
						out.print("<script>history.go(-1);</script>");
					} else {

						if (fuelHandler.createFuelType(fueltype)) {							
							out.print("<script>alert('Record saved successfully.')</script>");
							 out.print(
		                                "<script>window.location = 'fuelTypes.jsp?fuelId=" +
		                                fuelHandler.getFuelTypeByFuelCode(fuelCode).getFuelId() + "&PC=31'</script>");
						}
				}

                } else if (!fuelId.equals("")) {
                    fueltype.setFuelId(fuelId);
                    audit.select(1,
                            "SELECT * FROM  AM_AD_FUELTYPE  WHERE fuel_Id = '" +
                                 fuelId + "'");
                    boolean isupdt = fuelHandler.updateFuelType(fueltype);
                    audit.select(2,
                            "SELECT * FROM  AM_AD_FUELTYPE  WHERE fuel_Id = '" +
                                 fuelId + "'");
                    updtst = audit.logAuditTrail("AM_AD_FUELTYPE", branchcode,
                                                 loginID, fuelId,hostName,ipAddress,macAddress);
                    if (updtst == true) {
                        // statusMessage = "Update on record is successfull";
                        out.print(
                                "<script>alert('Update on record is successfull')</script>");
                        out.print(
                                "<script>window.location = 'fuelTypes.jsp?fuelId=" +
                                fuelId + "&PC=31'</script>");
                        //out.print("<script>window.location = 'manageBranchs.jsp?status=A'</script>");
                    } else {
                        //statusMessage = "No changes made on record";
                        out.print(
                                "<script>alert('No changes made on record')</script>");
                        out.print(
                                    "<script>window.location = 'fuelTypes.jsp?fuelId=" +
                                fuelId + "&PC=31'</script>");
                    }
                }
            }
        }
        } catch (Throwable e) {
            e.printStackTrace();
            //statusMessage = "Ensure unique record entry.";
            out.print("<script>alert('Ensure unique record entry.')</script>");
            out.print(
            "<script>history.back();</script>");
            System.err.print(e.getMessage());
        }
    }
}
