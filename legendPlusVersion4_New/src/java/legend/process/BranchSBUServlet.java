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
public class BranchSBUServlet extends HttpServlet {
	public BranchSBUServlet() {
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

		
		
		String code = encodeForHTML(request.getParameter("sbn_code"));
		String name = encodeForHTML(request.getParameter("sbn_name"));
		String contact = encodeForHTML(request.getParameter("sbn_contact"));
		String status = request.getParameter("active");
		String mail = request.getParameter("sbn_mail");
		//String operation = request.getParameter("operation");
		
	    Sbu_branch bran = new Sbu_branch();
		bran.setSbucode(code);
		bran.setSbuname(name);
		bran.setSbucontact(contact);
		bran.setSbustatus(status);
		
		AdminHandler admin = new AdminHandler();
		
		//String message = admin.iscreatesave2(code,name,contact,status,mail);
		
		 //admin.CheckIfExist(code,name,contact,mail, status);
		//boolean message = 
		
	

		 try {		
			 
						if(admin.iscreatesave2(code)) 
						{
							out.print("<script>alert('Record already exists ');</script>");
							out.print("<script>window.location = 'DocumentHelp.jsp?np=SbumanageBranches&status=ACTIVE'</script>");
							
						}
						else{
							
							
							admin.SaveSbuSetup(code,name,contact,status,mail);
							out.print("<script>alert('Record saved successfully.');</script>");
							out.print("<script>window.location = 'DocumentHelp.jsp?np=sbuSetup'</script>");
							
						}
						
				
			 	 			
			}
		 catch(Exception ex)
		 {
				ex.printStackTrace();
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