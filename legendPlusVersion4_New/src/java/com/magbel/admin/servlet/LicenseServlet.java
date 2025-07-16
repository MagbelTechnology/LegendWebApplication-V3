/*
 * x500SecurityCheck.java
 *
 * Created on May 13, 2006, 3:47 PM

 * @author  Jejelowo.B.Festus
 * @Docs Integrated Accounting Software
 * @version 1.0
 * Description -->This is a servlet that performs
 		security check[validate] user information
 		and retrived assigned priviledges based on
 		the role of the user.
*@Modified by Bolanle M. Sule @ 8th Jan. 2008.
 */
package com.magbel.admin.servlet;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*; 
import java.util.*;
import com.magbel.util.License;

public class LicenseServlet extends HttpServlet {

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
    License license = new License();
    String licCode = request.getParameter("licCode").trim();
    String authorCode = request.getParameter("authorCode").trim();
 
	ServletContext sc = this.getServletContext();
	String directory = sc.getRealPath(File.separator);
	 
     try
     {  
    	 String file=directory+File.separator+"error.properties";
    	 System.out.println(directory+File.separator+"error.properties");
    	 if(license.modifyLicense(licCode, authorCode, file))
    		 {
    		 out.println("<script>alert('Application key changed');</script>");
				out.print("<script>window.location ='DocumentHelp.jsp'</script>");
    		 }
    	 else {
    		 out.println("<script>alert('Application key not changed');</script>");
    		 out.print("<script>window.location ='DocumentHelp.jsp'</script>");
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
 
}

  
  public String getServletInfo() {
    return "Security Controller Servlet";
  }

}
