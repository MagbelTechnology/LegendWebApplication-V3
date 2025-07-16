/*
 * x500SecurityCheck.java
 *
 * Created on May 13, 2006, 3:47 PM

 * @author  Jejelowo.B.Festus
 * @update  Bolanle M. Sule.
 * @Docs Integrated Accounting Software
 * @version 1.0
 * Description -->This is a servlet that performs
 		security check[validate] user information
 		and retrived assigned priviledges based on
 		the role of the user.
 */

package com.magbel.ia.servlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.magbel.menu.handlers.MenuHandler;
import com.magbel.menu.objects.Menu;
import com.magbel.ia.vao.User;


public class x500SecurityCheck extends HttpServlet {

  /** Initializes the servlet.
   */

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }

  /** Destroys the servlet.
   */
  public void destroy() {

  }

  /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
   * @param request servlet request
   * @param response servlet response
   */
  public void service(HttpServletRequest request, HttpServletResponse response) throws
      ServletException, IOException {

	response.setContentType("text/html");

    HttpSession session = request.getSession();
    PrintWriter out = response.getWriter();
    
    com.magbel.ia.bus.AdminServiceBus  adminHandler = new com.magbel.ia.bus.AdminServiceBus();
   com.magbel.ia.bus.SecurityServiceBus sm = new  com.magbel.ia.bus.SecurityServiceBus();
   com.magbel.ia.vao.User user1 = new com.magbel.ia.vao.User();
   MenuHandler menu = new MenuHandler();
    int len = adminHandler.findCompany().getSessionTimeout();
    String username = request.getParameter("username").trim();
    String password = request.getParameter("password").trim();
	String comC = request.getParameter("companyCode");
	String result = request.getParameter("result");
	
	
    String userid = "username"; //request.getParameter("userid");
	String actionType = "test";//request.getParameter("actionType");
	String loginid = "5";//request.getParameter("loginid");
	String branch = "005";//request.getParameter("branch");
	String userStatus = "A";//request.getParameter("userStatus");
	String userFullName = "Bolanle"; //request.getParameter("userFullName");
     //com.magbel.util.Cryptomanager cm = new  com.magbel.util.Cryptomanager();   
	 
	// String companyCode;
		//com.magbel.ia.vao.User loginId = (com.magbel.ia.vao.User) session.getAttribute("CurrentUser");
		//if (loginId == null) {
			//companyCode = "Unkown";} else {companyCode = loginId.getCompanyCode();}
			
	//		System.out.println("<<<<<<<<<RESULT: "+result);
	if(!result.equalsIgnoreCase("false")){
    try
    {
		//comC = ((com.magbel.ia.vao.User)session.getAttribute("CurrentUser")).getCompanyCode();
		
       if(sm.isValidUser(username,com.magbel.util.Cryptomanager.encrypt(password)))
	   {
	   
           com.magbel.ia.vao.User user = sm.findUserByUserName(username);
				
			if(sm.confirmCompany(username,comC))
			{ 

    	     com.magbel.ia.vao.User user2 = sm.findUserByCompanyCode(comC,username);
								  
    	  if(user.getLoginStatus().equals("0"))
		    {   
    		
    			session = request.getSession(true);
    			//session.setMaxInactiveInterval(60* len);
    			session.setAttribute("CurrentUser",user);		
    			session.setAttribute("userid",user.getUserId());	
    			session.setAttribute("branch",user.getBranch());
                session.setAttribute("brachRestrict", user.getBranchRestrict());
                session.setAttribute("userRestrict", user.getUserRestrict());
				//session.setAttribute("company",user.getCompanyCode());
    			java.util.List<Menu> functions = menu.findMenus(user.getUserClass());
    			session.setAttribute("ClassFunction",functions);
    			Cookie cu = new Cookie("currUser",user.getUserId());
    			response.addCookie(cu);
    			if(sm.queryPexpiry(user.getUserId())||user.getMustChangePwd().equals("Y"))
				{
    				 out.print("<script>window.location = 'admin/changePassword1.jsp'</script>");	
    						 //out.close();
							 
    					}
						else
						{
    					sm.updateLogins(user.getUserId(),"1", request.getRemoteAddr());
						sm.notifyLoginStatus(userid,actionType,loginid,branch,userStatus,userFullName);
    			        response.sendRedirect(response.encodeURL("DocumentHelp.jsp?username="+username+""));
    		            }

    		
    		}
			else
			{
    	     out.print("<script>alert('User already Login,Please Logout properly.')</script>");
    		 out.print("<script>window.location = 'index.jsp'</script>");				
    	    }
		}
		  else 
		    {
			 response.sendRedirect("loginError2.jsp");
    	    }
    	}
		else 
		{
    		response.sendRedirect("loginError.jsp");
    	}
    }
	catch(NullPointerException ne)
	  {
    	response.sendRedirect("sessionTimedOut.jsp");
      }
	catch(Exception ne)
	{
    	response.sendRedirect("sessionTimedOut.jsp");
    }
  }else{
	     out.print("<script>alert('Application has expired or key is not valid.')</script>");
		 out.print("<script>window.location = 'index.jsp'</script>");
  }
    

   // response.sendRedirect("DocumentHelp.jsp");

}

  /** Returns a short description of the servlet.
   */
  public String getServletInfo() 
  {
    return "Security Controller Servlet";
  }

}
