package com.magbel.admin.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.magbel.admin.objects.lost_password;
import com.magbel.admin.objects.mail_setup;
import com.magbel.admin.handlers.AdminHandler;

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
public class LostPasswordServletOld extends HttpServlet {
	public LostPasswordServletOld() {
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

		

		String UserName = request.getParameter("UserName");
		String email = request.getParameter("email");
		String date = request.getParameter("create_date");
		lost_password bran = new  lost_password();
		System.out.println("-----UserName Servlet----- "+UserName);
		System.out.println("-----email Servlet----- "+email);
		bran.setUserName(UserName);
		bran.setemail(email);
		bran.setDate(date);
		
		AdminHandler admin = new AdminHandler();
		
		 com.magbel.admin.objects.User user = null;
	   	 if(session.getAttribute("_user")!=null) 
	   	 { 
	   		 user =(com.magbel.admin.objects.User)session.getAttribute("_user");
	   	     userId=user.getUserId(); 
	   	     branch=user.getBranch();
	   	     
	   	 }
		
	
 
		 try {		
			 
						if(admin.isCheckUserId(UserName,email))
						{
							admin.SaveLostPassword( UserName,email,date);
							out.print("<script>alert('We shall get back to you for the new Password.');</script>");
							out.print("<script>window.location = 'DocumentHelp.jsp?np=LostPassword'</script>");
							
						}
						else{													
			
							out.print("<script>alert('You have not supplied correct information.');</script>");
							out.print("<script>window.location = 'DocumentHelp.jsp?np=LostPassword'</script>");
							
						}
						
				
			 	 			
			}
		 catch(Exception ex)
		 {
				ex.printStackTrace();
			}
			
			
		
		
	
	
	}
}				