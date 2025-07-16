/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.magbel.admin.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.magbel.admin.handlers.SecurityHandler;
import com.magbel.admin.handlers.CompanyHandler;
import com.magbel.util.Cryptomanager;
import com.magbel.admin.handlers.ApprovalRecords;
import javax.servlet.http.HttpSession;


/**
 *
 * @author Developer - Ganiyu
 */
public class ChanngePasswordServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
      SecurityHandler   usb= new SecurityHandler();
CompanyHandler cdb = new CompanyHandler();
Cryptomanager cm = new Cryptomanager();
ApprovalRecords csm = new ApprovalRecords();

  HttpSession session = request.getSession();
if(session.getAttribute("_user")==null)
{
response.sendRedirect("sessionTimedOut.jsp");
}


		com.magbel.admin.objects.Company comp_param = cdb.getCompany();

	String password = new String();
	try{
           
		password = cm.encrypt(request.getParameter("newPassword"));
	}
	catch(Exception e){
            System.out.println(" >> the following error occur in ChangePasswordServlet: " +e);
        }



	try{
           
		com.magbel.admin.objects.User user = (com.magbel.admin.objects.User)session.getAttribute("_user");


			if(!request.getParameter("curPassword").equals(request.getParameter("newPassword")))
			if(comp_param.getMinimumPassword() <= request.getParameter("newPassword").length())
			{
			//get the new password format for validation
			String passWordNew1=cm.encrypt(usb.Name(String.valueOf(session.getAttribute("SignInName")),request.getParameter("curPassword")));

                      
                        if(usb.confirmPassword(user.getUserId(), passWordNew1))
				{
                          

              String passWordNew=cm.encrypt(usb.Name(String.valueOf(session.getAttribute("SignInName")),request.getParameter("newPassword")));

			 if(usb.changePassword3(user.getUserId(),passWordNew,String.valueOf(comp_param.getPasswordExpiry()),comp_param.getPasswordLimit()))
				{
                                 
						out.print("<script>alert('Password updated successfully.')</script>");
						out.println("<script>window.location = 'DocumentHelp.jsp'</script>");
					}
					 else{
                 
					      out.print("<script>alert('Password has been used before.')</script>");
                                              out.println("<script>window.location = 'changePassword1.jsp'</script>");
						  }
				}
				else{
					out.print("<script>alert('Wrong user current password supplied.')</script>");
                                         out.println("<script>window.location = 'changePassword1.jsp'</script>");
				}
			}
			else{
				out.print("<script>alert('Minimum user password length is "+comp_param.getMinimumPassword()+"')</script>");
                                 out.println("<script>window.location = 'changePassword1.jsp'</script>");
			}

			else
				out.print("<script>alert('Current password and new password must not be the same.')</script>");
                 out.println("<script>window.location = 'changePassword1.jsp'</script>");

	}catch(Exception e){e.printStackTrace();
		System.err.print(e.getMessage());
	}

        } finally { 
            out.close();
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
