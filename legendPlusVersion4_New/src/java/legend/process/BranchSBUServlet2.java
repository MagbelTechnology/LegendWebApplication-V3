package legend.process;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import legend.admin.objects.Sbu_branch;
import legend.admin.handlers.AdminHandler;

import audit.AuditTrailGen;
import audit.*;

import com.magbel.util.CheckIntegerityContraint;

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
public class BranchSBUServlet2 extends HttpServlet {
	public BranchSBUServlet2() {
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
			throws ServletException, IOException{

		response.setContentType("text/html");
		response.setDateHeader("Expires", -1);

		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();

		AuditTrailGen  audit = new AuditTrailGen();
		boolean updtst = false;
		
		int loginID;
		 String loginId = (String)session.getAttribute("CurrentUser");
		 if(loginId == null) {  loginID = 0; }
			else { loginID = Integer.parseInt(loginId);	}

		String branchcode = (String)session.getAttribute("UserCenter");
		if(branchcode == null) { branchcode = "not set";	}

		
		
		String code = request.getParameter("sbn_code");
		//System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+code);
		String name = request.getParameter("sbn_name");
		String contact = request.getParameter("sbn_contact");
		String status = request.getParameter("active");
		String mail = request.getParameter("sbn_mail");
		//String operation = request.getParameter("operation");
		
	    Sbu_branch bran = new Sbu_branch();
		bran.setSbucode(code);
		bran.setSbuname(name);
		bran.setSbucontact(contact);
		bran.setSbustatus(status);
		
		AdminHandler admin = new AdminHandler();
		String roleid =admin.getPrivilegesRole("Manage SBU");
		//String save = admin.UpdateSbu(code,name,contact,status,mail);
		

		 try {		
			 		
						audit.select( 1, "SELECT * FROM  Sbu_SetUp   WHERE Sbu_code = '"+code+"'");
			 			String save = admin.UpdateSbu(code,name,contact,status,mail);
			 			if(save.equalsIgnoreCase("Success_update"))
			 			{
			 				audit.select( 2, "SELECT * FROM  Sbu_SetUp   WHERE Sbu_code = '"+code+"'");
			 				updtst = audit.logAuditTrail("Sbu_SetUp" , branchcode, loginID, roleid,"","");
			 				
			 				if(updtst) 
							{
								out.println("<script>alert('Records update Successful')");
				        		out.println("history.go(-1)");
				        		out.println("</script>");
							}else{		
								out.println("<script>alert('No changes made on Records')");
				        		out.println("history.go(-1)");
				        		out.println("</script>");
				        		
							}
			 			}
			 			/*	if( save.equalsIgnoreCase("Success_update")) 
						{
							out.println("<script>alert('Records update Successfully')");
			        		out.println("history.go(-1)");
			        		out.println("</script>");
						}else{		
							out.println("<script>alert('No changes made on Records')");
			        		out.println("history.go(-1)");
			        		out.println("</script>");
							
							//out.println("history.go(-1)");
							//out.println("</script>");
							//out.print("<script>window.location ='DocumentHelp.jsp?np=sbuSetup2'</script>");
							//out.print("<script>alert('Sbu  not saved.');</script>");
							//out.print("<script>window.location = 'DocumentHelp.jsp?np=sbuSetup'</script>");
							//out.println("history.go(-1)");
							 *}
						*/
			 		
				
			 	 			
			}
		 catch(Exception ex)
		 {
				ex.printStackTrace();
			}
			
			
		
		
	
	
	}
}				