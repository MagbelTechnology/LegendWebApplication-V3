package com.magbel.admin.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
public class MailActionServlet extends HttpServlet {
	public MailActionServlet() {
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

		

		String mailcode = request.getParameter("mail_code");
		String mailheading = request.getParameter("mailheading");
		String maildesc = request.getParameter("mail_description");
		String address = request.getParameter("mail_address");
		String date = request.getParameter("create_date");
		String trans_type = request.getParameter("transaction_type");
		String userid = request.getParameter("user_id");
		String active = request.getParameter("active");
		String category = request.getParameter("category");
		String Subcategory = request.getParameter("subcategory");
		String mailtype = request.getParameter("mail_type");
		String helptype = request.getParameter("help_type");
		//String operation = request.getParameter("operation");
		//System.out.print("-----category---- "+category);
		//System.out.print("-----mailtype---- "+mailtype);
		//System.out.print("-----helptype---- "+helptype);
                mail_setup bran = new  mail_setup();
                String start_yy = category.substring(0,2);	
		bran.setMailcode(mailcode);
		bran.setmailheading(mailheading);
		bran.setMaildescription(maildesc);
		bran.setMailaddress(address);
		bran.setDate(date);
		bran.setTrans_type(trans_type);
		bran.setUserid(userid);
		bran.setStatus(active);
		bran.setcategory(category);
		bran.setmailtype(mailtype);
		bran.sethelptype(helptype);
		
		AdminHandler admin = new AdminHandler();

		//String message = admin.CheckMailSetup(mailcode,maildesc ,address,date,trans_type,userid,active);
		
		 com.magbel.admin.objects.User user = null;
	   	 if(session.getAttribute("_user")!=null) 
	   	 { 
	   		 user =(com.magbel.admin.objects.User)session.getAttribute("_user");
	   	     userId=user.getUserId(); 
	   	     branch=user.getBranch();
	   	     
	   	 }
		
	

		 try {		
			 
						if(admin.isCheckMailSetup(mailcode))
						{
							out.print("<script>alert('Record already exists');</script>");
							out.print("<script>window.location = 'DocumentHelp.jsp?np=managemailsetup'</script>");
							
						}
						else{
							
							
							admin.SaveMailSetup( mailcode,mailheading,maildesc,address,date,trans_type, userId,active,category,mailtype,helptype,Subcategory);
							out.print("<script>alert('Record saved successfully.');</script>");
							out.print("<script>window.location = 'DocumentHelp.jsp?np=mailsetup'</script>");
							
						}
						
				
			 	 			
			}
		 catch(Exception ex)
		 {
				ex.printStackTrace();
			}
			
			
		
		
	
	
	}
}				