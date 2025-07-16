package legend;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

import jakarta.servlet.ServletException;

import com.magbel.util.Cryptomanager;

import jakarta.servlet.ServletConfig;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;

import jakarta.servlet.http.HttpServletResponse;

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
 * Disposal:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class AssetDescriptionAuditServlet extends HttpServlet {
	public AssetDescriptionAuditServlet() {
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

        response.setContentType("text/html");
        response.setDateHeader("Expires", -1);

        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();

        // String type = request.getParameter("TYPE");

        // java.sql.Date dt = new java.sql.Date();
        AuditTrailGen  audit = new AuditTrailGen();
        // java.text.SimpleDateFormat sdf = new
		// java.text.SimpleDateFormat("dd/mm/yyyy");
		String statusMessage = "";
		boolean updtst = false;
        // String loginId = request.getParameter("loginId");
			int loginID;
		 String loginId = (String)session.getAttribute("CurrentUser");
		 if(loginId == null) {  loginID = 0; }
			else { loginID = Integer.parseInt(loginId);	}
		
		  String userClass = (String)session.getAttribute("UserClass");
		
		String branchcode = (String)session.getAttribute("UserCenter");
		if(branchcode == null) { branchcode = "not set";	}

        Cryptomanager cm = new Cryptomanager();

        String descriptId = request.getParameter("descriptId");

       legend.admin.handlers.CompanyHandler ch = new legend.admin.handlers.CompanyHandler();

        String buttSave = request.getParameter("buttSave");;
    	 String sbuCatCode = request.getParameter("descriptCode");
    	 String description = encodeForHTML(request.getParameter("assetDesc"));
    	 String descriptStatus =  request.getParameter("assetStatus");
    	 String userId =  (String)session.getAttribute("CurrentUser");
  //  	 System.out.println("======>descriptId: "+descriptId+"   description: "+description+"   sbuCatCode: "+sbuCatCode+"   descriptStatus: "+descriptStatus);
    	 legend.admin.objects.AssetDescription descripts = new legend.admin.objects.AssetDescription();
    	 	 descripts.setDescriptionId(descriptId) ;
             descripts.setSubCategoryCode(sbuCatCode) ;
             descripts.setDescription(description);
             descripts.setDescriptionStatus(descriptStatus);
             descripts.setUserId(userId);

             String computerName = null;
             String remoteAddress = request.getRemoteAddr();
             InetAddress inetAddress = InetAddress.getByName(remoteAddress);
  //           System.out.println("inetAddress: " + inetAddress);
             computerName = inetAddress.getHostName();
  //           System.out.println("computerName: " + computerName);
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
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < mac.length; i++) {
					sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
				}
				String macAddress = sb.toString();
	//			System.out.println(sb.toString());
				
	//	        System.out.println("======>hostName: "+hostName+"   ipAddress: "+ipAddress);
        try{
        	if (!userClass.equals("NULL") || userClass!=null){
            if(buttSave != null)
				{
				 if (descriptId.equals("")) 
					{
					 
					 System.out.println("<<< descriptId: " + descriptId);
					 boolean assetDescriptionDetails = ch.getAssetDescriptionsById(descriptId) != null;
					 System.out.println("<<< assetDescriptionDetails: " + assetDescriptionDetails);
					 
					 if(ch.getAssetDescriptionsById(descriptId)!=null)
					 {
						 out.print("<script>alert('Asset Description is in Use')</script>");
						 out.print("<script>history.go(-1)</script>");
					 }
					 else
					 {
						 String result = ch.createAssetDescriptions(descripts);
	//					 System.out.println("======>result: "+result);
                     if (!result.equals(""))
						{
							// statusMessage = "Record saved successfully";
							out.print("<script>alert('Record saved successfully.')</script>");
							out.print("<script>window.location = 'DocumentHelp.jsp?np=assetDescriptions&descriptId="+result+"'</script>");
						}
					 else
						{
						 System.out.println("Error saving record: New record \nfor 'Asset Description'  with Description Id "+result+" could not be created"); 
						out.print("<script>history.go(-1)</script>");
						}
					} 
					}
				 else if (!descriptId.equals(""))
					{

					 descripts.setDescriptionId(descriptId);
						audit.select( 1, "SELECT * FROM  AM_ASSET_DESCRIPTION   WHERE Id = '"+ descriptId+"'");
						boolean isupdt = ch.updateAssetDescription(descripts);
						audit.select( 2, "SELECT * FROM  AM_ASSET_DESCRIPTION   WHERE Id = '"+ descriptId +"'");
						updtst = audit.logAuditTrail("AM_ASSET_DESCRIPTION" ,  branchcode, loginID, descriptId,hostName,ipAddress,macAddress);
						
						   if(updtst == true)
							{
								// statusMessage = "Update on record is
								// successfull";
								out.print("<script>alert('Update on record is successfull')</script>");
								out.print("<script>window.location = 'DocumentHelp.jsp?np=assetDescriptions&descriptId="+descriptId+"'</script>");
								// out.print("<script>window.location =
								// 'manageBranchs.jsp?status=A'</script>");
							}
						else 
								{
								// statusMessage = "No changes made on record";
								 out.print("<script>alert('No changes made on record')</script>");
								 out.print("<script>window.location = 'DocumentHelp.jsp?np=assetDescriptions&descriptId="+descriptId+"'</script>");
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
				out.print("<script>window.location = 'DocumentHelp.jsp?np=assetDescriptions&descriptId="+descriptId+"'</script>");
				System.err.print(e.getMessage());
			}
    }
	
	private String encodeForHTML(String s) {
		StringBuilder out = new StringBuilder(Math.max(16, s.length()));
	    for (int i = 0; i < s.length(); i++) {
	        char c = s.charAt(i);
	        if (c > 127 || c == '"' || c == '\'' || c == '<' || c == '>' || c == '&') {
	            out.append("&#");
	            out.append((int) c);
	            out.append(';');
	        } else {
	            out.append(c);
	        }
	    }
	    return out.toString();
}
}
