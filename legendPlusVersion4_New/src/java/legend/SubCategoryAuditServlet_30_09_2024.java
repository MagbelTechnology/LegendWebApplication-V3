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

import com.magbel.util.ApplicationHelper;

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
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class SubCategoryAuditServlet extends HttpServlet {
	public SubCategoryAuditServlet() {
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

		// String type = request.getParameter("TYPE");
		String statusMessage = "";
		boolean updtst = false;

		// java.sql.Date dt = new java.sql.Date();
		AuditTrailGen audit = new AuditTrailGen();
		// java.text.SimpleDateFormat sdf = new
		// java.text.SimpleDateFormat("dd/mm/yyyy");

		// String loginId = request.getParameter("loginId");
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

		String buttSave = request.getParameter("buttSave");

		String subcategoryId = request.getParameter("subcategoryId");
		// if(makeId == null){makeId = "";}

		// String user = (String)session.getAttribute("CurrentUser");

		String assetSubcategoryCode = request.getParameter("subcategoryCode");
		String assetSubcategory = request.getParameter("subcategoryName");
		String subcategoryStatus = request.getParameter("subcategoryStatus");
		String category = request.getParameter("subcategoryCart");
		String userid = (String) session.getAttribute("CurrentUser");
		System.out.println("assetSubcategoryCode: " + assetSubcategoryCode+"  assetSubcategory: "+assetSubcategory+"    subcategoryStatus: "+subcategoryStatus+"  category: "+category+"  userid: "+userid);
		legend.admin.objects.SubCategory am =  new legend.admin.objects.SubCategory();
		am.setAssetSubCategoryCode(assetSubcategoryCode);
		am.setAssetSubCategory(assetSubcategory);
		am.setCategory(category);
		am.setStatus(subcategoryStatus);
		am.setUserid(userid);

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
			
		try {
			 if (!userClass.equals("NULL") || userClass!=null){
			legend.admin.handlers.CompanyHandler ch = new legend.admin.handlers.CompanyHandler();
			if (buttSave != null) {
				if (subcategoryId.equals("")) {
                                    String assetSubCategoryCodeNew =new ApplicationHelper().getGeneratedId("AM_AD_SUB_CATEGORY");
                                    am.setAssetSubCategoryCode(0+assetSubCategoryCodeNew);
                                    if (ch.createAssetSubCategory(am)) { 
						out
								.print("<script>alert('Record saved successfully.')</script>");
						out
						.print("<script>window.location = 'DocumentHelp.jsp?np=subCategorySetup&subcategoryId="
								+ ch.getAssetSubCategoryByCode(am.getAssetSubCategoryCode()).getAssetSubCategoryId() + "&PC=61'</script>");
					} else {
						System.out
								.println("Error saving record: New record \nfor 'asset sub Category'  with subcategory "
										+ am.getAssetSubCategory() + " could not be created");
						out
								.print("<script>window.location = 'DocumentHelp.jsp?np=subCategorySetup&subcategoryId="
								+ ch.getAssetSubCategoryByCode(am.getAssetSubCategoryCode()).getAssetSubCategoryId() + "&PC=61'</script>");
						//.print("<script>window.location = 'subCategorySetup.jsp'</script>");
					}
				}

				if (!subcategoryId.equals("")) {
					am.setAssetSubCategoryId(subcategoryId);
					audit.select(1,
							"SELECT * FROM  am_ad_sub_category   WHERE sub_category_ID = '"
									+ subcategoryId + "'");
					boolean isupdt = ch.updateAssetSubCategory(am);
					audit.select(2,
							"SELECT * FROM  am_ad_sub_category  WHERE sub_category_ID = '"
									+ subcategoryId + "'");
					updtst = audit.logAuditTrail("am_ad_sub_category", branchcode,
							loginID, subcategoryId,hostName,ipAddress,macAddress);
					if (updtst == true) {
						out
								.print("<script>alert('Update on record is successful')</script>");
						out
								.print("<script>window.location = 'DocumentHelp.jsp?np=subCategorySetup&subcategoryId="
								+ ch.getAssetSubCategoryByCode(am.getAssetSubCategoryCode()).getAssetSubCategoryId() + "&PC=61'</script>");
					} else {
						out
								.print("<script>alert('No changes made on record')</script>");
						out
						.print("<script>window.location = 'DocumentHelp.jsp?np=subCategorySetup&subcategoryId="
								+ ch.getAssetSubCategoryByCode(am.getAssetSubCategoryCode()).getAssetSubCategoryId() + "&PC=61'</script>");
					}
				}
			}
		}
		} catch (Throwable e) {
			e.printStackTrace();
			out.print("<script>alert('Ensure unique record entry.')</script>");
			out
					.print("<script>window.location = 'DocumentHelp.jsp?np=subCategorySetup'</script>");
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
