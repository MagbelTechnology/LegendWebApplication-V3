package legend;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;

import javax.servlet.ServletException;

import com.magbel.util.ApplicationHelper;
import com.magbel.util.Cryptomanager;

import javax.servlet.ServletConfig;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;

import javax.servlet.http.HttpServletResponse;

import audit.*;

import com.magbel.util.HtmlUtility;

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
 * Disposal:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class PrivilegeServlet extends HttpServlet {
	HtmlUtility Utilrecords = null;
	public PrivilegeServlet() {
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
	public void service(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
		Utilrecords = new HtmlUtility();
        response.setContentType("text/html");
        response.setDateHeader("Expires", -1);

        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();

        // String type = request.getParameter("TYPE");

        // java.sql.Date dt = new java.sql.Date();
        AuditTrailGen  audit = new AuditTrailGen();
		String statusMessage = "";
		boolean updtst = false;
        // String loginId = request.getParameter("loginId");
			int loginID;
		 String loginId = (String)session.getAttribute("CurrentUser");
		 if(loginId == null) {  loginID = 0; }
			else { loginID = Integer.parseInt(loginId);	}
		
		 String userClass = (String)session.getAttribute("UserClass");
		 
		String branchcode = (String)session.getAttribute("UserCenter");
//		if(branchcode == null) { branchcode = "not set";	}

        Cryptomanager cm = new Cryptomanager();
        String RoleId = request.getParameter("RoleId");
      
       legend.admin.handlers.OtherHandler ch = new legend.admin.handlers.OtherHandler();
        String RoleName = request.getParameter("rolename");
        String buttSave = request.getParameter("operation");
        String buttDelete = request.getParameter("Delete");
    	 String RoleUrl = request.getParameter("roleurl");
    	 String MenuType = request.getParameter("menutype");
    	 String Priority =  request.getParameter("priority");
    	 String ParentId =  MenuType;
    	 String Level =  request.getParameter("level");    	 
/*
    	 System.out.println("RoleName?????=== "+RoleName);
    	 System.out.println("RoleUrl?????=== "+RoleUrl);
    	 System.out.println("MenuType?????=== "+MenuType);  
    	 System.out.println("Priority?????=== "+Priority);
    	 System.out.println("ParentId?????=== "+MenuType);
    	 System.out.println("Level?????=== "+Level);  
*/    	      	  
    	 String MenuIdQry = "Select role_name from am_ad_privileges where role_uuid = '"+MenuType+"'";
    	 String Menu_Type = ch.getCodeName(MenuIdQry);
 //   	 System.out.println("Menu_Type===>>>> "+Menu_Type);
    	 legend.admin.objects.PrivilegeAssign privilege = new legend.admin.objects.PrivilegeAssign();
    	 privilege.setRoleName(RoleName);
    	 privilege.setRoleUrld(RoleUrl);
    	 privilege.setMenuType(Menu_Type);
   // 	 System.out.println("Role Id=== "+RoleId);
    	// privilege.setPriority(Priority);
		 /*if (!RoleId.equals("")) 
			{
			 System.out.println("RoleId===>>>> "+RoleId);
    	 String ParenIdQry = "Select role_uuid from am_ad_privileges where menu_type = '"+MenuType+"'";    	 
    	 ParentId = ch.getCodeName(ParenIdQry);
    	 System.out.println("ParentId===>>>> "+ParentId);
    	 System.out.println("ParenIdQry===>>>> "+ParenIdQry);
			}*/
    	 privilege.setParentId(ParentId);
    	 privilege.setLevel(Level);
    //	 System.out.println("buttSave===>>>> "+buttSave);

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
            if(buttSave.equalsIgnoreCase("Delete"))
				{
				 //System.out.println("buttDelete?????=== "+buttSave);            	
				 if (!RoleId.equals("")) 
					{
					// System.out.println("buttDelete 2 ?????=== "+buttSave);
                     if (ch.deletePrivileges(RoleId))
						{
							// statusMessage = "Record saved successfully";
							out.print("<script>alert('Record Deleted successfully.')</script>");
							//out.print("<script>window.location = 'disposalReasons.jsp?reasonId="+ch.getDisposableReasonsByReasonCode(reasonCode).getReasonId()+"'</script>");
							out.print("<script>window.location = 'DocumentHelp.jsp?np=PrivilegeList'</script>");
						}
					 else
						{
						 System.out.println("Error deleting record: 'Privilege'  could not be created"); 
						out.print("<script>history.go(-1)</script>");
						}
					}
				}
            if(buttSave.equalsIgnoreCase("Save"))
				{
            //	System.out.println("buttSave?????=== "+buttSave);
				 if (RoleId.equals("")) 
					{					 
                     if (ch.createPrivileges(privilege))
						{
							// statusMessage = "Record saved successfully";
							out.print("<script>alert('Record saved successfully.')</script>");
							//out.print("<script>window.location = 'disposalReasons.jsp?reasonId="+ch.getDisposableReasonsByReasonCode(reasonCode).getReasonId()+"'</script>");
							out.print("<script>window.location = 'PrivilegesSetup.jsp?'</script>");
						}
					 else
						{
						 System.out.println("Error saving record: New record \nfor 'Privilege'  could not be created"); 
						out.print("<script>history.go(-1)</script>");
						}
				//	} 
					}
				 else if (!RoleId.equals(""))
					{ 
				//	 System.out.println("About Save Id=== "+RoleId);
					//	System.out.println("buttSave?????=== "+buttSave);
					 privilege.setRoleId(RoleId);
						audit.select( 1, "SELECT * FROM  am_ad_privileges   WHERE role_uuid = '"+RoleId+"'");
						boolean isupdt = ch.updatePrivilege(privilege);
						audit.select( 2, "SELECT * FROM  am_ad_privileges   WHERE role_uuid = '"+ RoleId +"'");
						updtst = audit.logAuditTrail("am_ad_privileges" ,  branchcode, loginID, RoleId,hostName,ipAddress,macAddress);
						   if(updtst == true)
							{
								// statusMessage = "Update on record is
								// successfull";  
								out.print("<script>alert('Update on record is successfull')</script>");
								out.print("<script>window.location = 'DocumentHelp.jsp?np=PrivilegesUpdate.jsp?roleId="+RoleId+"'</script>");
								// out.print("<script>window.location =
								// 'manageBranchs.jsp?status=A'</script>");
							}
						else 
								{
								// statusMessage = "No changes made on record";
								 out.print("<script>alert('No changes made on record')</script>");
								 out.print("<script>window.location = 'PrivilegesSetup.jsp?roleId="+RoleId+"'</script>");
								}
					}
				}
        }
			}
			catch(Throwable e)
			{
				e.printStackTrace();
				// statusMessage = "Ensure unique record entry";
				out.print("<script>alert('Ensure unique record entry.')</script>");
				out.print("<script>window.location = 'PrivilegesSetup.jsp?roleId="+RoleId+"'</script>");
				System.err.print(e.getMessage());
			}
    }
}
