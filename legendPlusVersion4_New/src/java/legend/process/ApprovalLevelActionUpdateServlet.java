package legend.process;
import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
//import legend.admin.objects.Sbu_branch;
import legend.admin.objects.mail_setup;
import legend.admin.objects.Approval_Level;
import legend.admin.handlers.AdminHandler;
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
public class ApprovalLevelActionUpdateServlet extends HttpServlet {
	public ApprovalLevelActionUpdateServlet() {
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
 
		String userId="";
        String branch = "";
        
        AuditTrailGen  audit = new AuditTrailGen();
		boolean updtst = false;
		
		int loginID;
		 String loginId = (String)session.getAttribute("CurrentUser");
		 if(loginId == null) {  loginID = 0; }
			else { loginID = Integer.parseInt(loginId);	}

		String branchcode = (String)session.getAttribute("UserCenter");
		if(branchcode == null) { branchcode = "not set";	}
		//System.out.println("IYA MOSES..................WA BI BY E....");
		
		String code = request.getParameter("code");
		//System.out.println("Servlet code 1 "+code);
		//String name = request.getParameter("trans_type");
                String name = request.getParameter("trans_type").trim();
		//System.out.println("Servlet name  2"+  name);
		String contact = request.getParameter("mail_address");
		String level = request.getParameter("level");
		//System.out.println("Servlet level  3"+  level);
		String date = request.getParameter("create_date");
		//System.out.println("Servlet date  4"+  date);
	
		//String operation = request.getParameter("operation");
		
	    //Sbu_branch bran = new Sbu_branch();
		//mail_setup bran = new mail_setup();
		Approval_Level bran = new Approval_Level();
		bran.setCode(code);
		bran.setTrans_type(name);
		bran.setLevel(level);
		bran.setDate(date);
		//bran.setTrans_type(trans);
		//bran.setUserid(userid);
		//bran.setStatus(active);
		 legend.admin.objects.User user = null;
	   	 if(session.getAttribute("_user")!=null) 
	   	 { 
	   		 user =(legend.admin.objects.User)session.getAttribute("_user"); 
	   	     userId=user.getUserId(); 
	   	     branch=user.getBranch();
	   	     
	   	 }
		
		AdminHandler admin = new AdminHandler();
		String roleid =admin.getPrivilegesRole("Manage Approval Level List");
	

		 try {		
			 /*
			 audit.select(1,
						"SELECT * FROM  Approval_Level_setup  WHERE Code = '"
								+ code + "'");
				String save = admin.UpdateApproval_Level(code, name, level);
				audit.select(2,
						"SELECT * FROM  Approval_Level_setup  WHERE Code = '"
								+ code + "'");
				 updtst = audit.logAuditTrail("Approval_Level_setup",
							branchcode, loginID, code);
						if( save.equalsIgnoreCase("Success_update")) 
						{
						*/
                      audit.select(1,"SELECT * FROM  Approval_Level_setup  WHERE Code = '"+ code + "'");
				String save = admin.UpdateApproval_Level(code, name, level);
				audit.select(2,"SELECT * FROM  Approval_Level_setup  WHERE Code = '"+ code + "'");
				 updtst = audit.logAuditTrail("Approval_Level_setup",branchcode, loginID, roleid,"","");
							if(updtst == true) 
							{
								out.println("<script>alert('Records update Successfully')");
				        		out.println("history.go(-1)");
				        		out.println("</script>");
						
							}else{
								out.println("<script>alert('No changes made on Records')");
								out.println("history.go(-1)");
				        		out.println("</script>");
							}
							
							
						/*
							//out.println("history.go(-1)");
							//out.println("</script>");
							//out.print("<script>window.location ='DocumentHelp.jsp?np=sbuSetup2'</script>");
							//out.print("<script>alert('Sbu  not saved.');</script>");
							//out.print("<script>window.location = 'DocumentHelp.jsp?np=sbuSetup'</script>");
							//out.println("history.go(-1)");
						}
*/
			 	 			
			}
		 catch(Exception ex)
		 {
				ex.printStackTrace();
			}
			
			
		
		
	
	
	}
}				