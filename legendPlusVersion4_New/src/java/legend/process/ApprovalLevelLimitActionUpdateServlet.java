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
import legend.admin.objects.Aproval_limit;
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
public class ApprovalLevelLimitActionUpdateServlet extends HttpServlet {
	public ApprovalLevelLimitActionUpdateServlet() {
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
 
		
		
		String code = request.getParameter("code");
		//System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+code);
		String name = request.getParameter("min_amount");
		String contact = request.getParameter("max_amount");
		String desc = request.getParameter("description");
		//String date = request.getParameter("create_date");
	
		String min = name.replace(",","");
		String max = contact.replace(",","");
		//String operation = request.getParameter("operation");
		
		double min_amount = Double.parseDouble(min);
		double max_amount = Double.parseDouble(max);
		
	    //Sbu_branch bran = new Sbu_branch();
		//mail_setup bran = new mail_setup();
		Aproval_limit bran = new Aproval_limit();
		bran.setCode(code);
		bran.setMinAmt(min_amount);
		bran.setMaxAmt(max_amount);
		bran.setDesc(desc);
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
		String roleid =admin.getPrivilegesRole("Manage Approval Limit");
		

		 try {	
			 
			 audit.select(1,
						"SELECT * FROM  Approval_Limit  WHERE Level_Code = '"
								+ code + "'");
			 String save = admin.UpdateApproval_Limit(code,min_amount,max_amount,desc);
			 audit.select(2,
						"SELECT * FROM  Approval_Limit  WHERE Level_Code = '"
								+ code + "'");
			 updtst = audit.logAuditTrail("Approval_Limit",
						branchcode, loginID, roleid,"","");
			 
						if( save.equalsIgnoreCase("Success_update")) 
						{
							out.println("<script>alert('Records updated Successfully')");
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
						}
				
			 	 			
			}
		 catch(Exception ex)
		 {
				ex.printStackTrace();
			}
			
			
		
		
	
	
	}
}				