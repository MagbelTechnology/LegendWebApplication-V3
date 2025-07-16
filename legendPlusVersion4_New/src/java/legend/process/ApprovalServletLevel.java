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
import legend.admin.objects.Aproval_limit;
import legend.admin.objects.Approval_Level;
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
public class ApprovalServletLevel extends HttpServlet {
	public ApprovalServletLevel() {
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
		String userId ="";
		String branch="";
		
		
		String code = request.getParameter("code");
		String name = request.getParameter("trans_type");
		String contact = request.getParameter("level");
		String date = request.getParameter("create_date");
		String userid = request.getParameter("user_id");
		
		//String operation = request.getParameter("operation");
		
		//String amount_min = name.replace("," , "");
		//String amount_max= name.replace("," , "");
		
		//int convert = Integer.parseInt(code);
		//double min = Double.parseDouble(amount_min);
		//double max = Double.parseDouble(amount_max);
		
	   // Sbu_branch bran = new Sbu_branch();
		Approval_Level approve = new Approval_Level();
		approve.setCode(code);
		approve.setTrans_type(name);
		approve.setLevel(contact);
		approve.setDate(date);
		approve.setUserid(userid);
		
		
		
		 //admin.CheckIfExist(code,name,contact,mail, status);
		//boolean message = 
		 legend.admin.objects.User user = null;
	   	 if(session.getAttribute("_user")!=null) 
	   	 { 
	   		 user =(legend.admin.objects.User)session.getAttribute("_user"); 
	   	     userId=user.getUserId(); 
	   	     branch=user.getBranch();
	   	     
	   	 }
		
	
	   	 AdminHandler admin = new AdminHandler();
		
		//String message = admin.isApproval_Level_Existing(code);
		

		 try {		
			 
						if(admin.isApproval_Level_Existing(code)) 
						{
							out.print("<script>alert('Record already exists');</script>");
							out.print("<script>window.location = 'DocumentHelp.jsp?np=ApprovalLevelView&status=ACTIVE'</script>");
							
						}
						else{
							
							
							admin.SaveApproval_level(code, name, contact, date,userId);
							out.print("<script>alert('Record saved successfully.');</script>");
							out.print("<script>window.location = 'DocumentHelp.jsp?np=aprovalLevel'</script>");
							
						}
						
				
			 	 			
			}
		 catch(Exception ex)
		 {
				ex.printStackTrace();
			}
			
			
		
		
	
	
	}
}				