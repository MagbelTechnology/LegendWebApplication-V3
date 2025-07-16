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

import java.util.*;
import java.net.InetAddress;
import com.magbel.admin.handlers.SecurityHandler;
import com.magbel.util.Cryptomanager;
import com.magbel.admin.handlers.CompanyHandler;
import com.magbel.admin.handlers.MenuHandler;
import com.magbel.admin.handlers.AdminHandler;
//import com.magbel.admin.handlers.Postilion;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import com.magbel.admin.objects.Company;
import com.magbel.admin.handlers.SytemsManager;
//import org.jboss.wsf.spi.deployment.SecurityHandler;

/**
 *
 * @author Developer - Vickie
 */
public class X500SecurityCheckCheckpoint extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {

            SecurityHandler sh = new SecurityHandler();
            Cryptomanager cm = new Cryptomanager();
            CompanyHandler ch = new CompanyHandler();
            MenuHandler menur = new MenuHandler();
            AdminHandler handler = new AdminHandler();
//            Postilion admin = new Postilion();
            Company comp = ch.getCompany();

            String buttConn = request.getParameter("buttConn");

            String workstationIp = request.getRemoteAddr();
            InetAddress address = InetAddress.getByName(workstationIp);
            String workstationName = address.getHostName();
            Calendar currentcal = Calendar.getInstance();
            int logonAttempt = Integer.parseInt(sh.getCompanyLoginAttempt());

            HttpSession session = request.getSession();
            String id = session.getId();

            String deployer = getServletContext().getRealPath("/error.properties");


                try {

                    //lisence
                    //the lisence is commentd out for now
                  //  if (sh.login(deployer)) {

                        java.util.List list = sh.getUserByQuery(" AND  USER_NAME='" +
                                request.getParameter("userName") + "' AND PASSWORD ='" + cm.encrypt(sh.Name(request.getParameter("userName"), request.getParameter("userPswd"))) + "'", request.getParameter("userName"), request.getParameter("userPswd"));

                        if (list != null && list.size() > 0) {
                            session.setAttribute("FAILED_LOGON_CTN", "0");

                            com.magbel.admin.objects.User user = (com.magbel.admin.objects.User) list.get(0);
                            if (user != null && user.getUserStatus().equalsIgnoreCase("ACTIVE")) //if(user != null) //the condition for access to system denial for inactive user goes here
                            {
                                Date d1 = new Date(currentcal.getTimeInMillis());
                                Date d2 = user.getExpDate();
                                //System.out.println("D2 >>>>>> " +  d2);
                                if (d2 == null) {
                                    //System.out.println("i entered here ");
                                    d2 = d1;
                                }//comparing d2 and d1


                                if ((d1.equals(d2)) || (d1.before(d2))) {
                                    //System.out.println("In here --------- 1");

                                   // System.out.println(">>0 the login status of user is "+ user.getLoginStatus());

                                    if (user.getLoginStatus().equals("3")) {
                                        out.print("<script>alert('You have exceded logon limits. Contact Administrator.')</script>");
                                        out.println("<script>window.location = 'index.jsp'</script>");
                                    }


                                    if (user.getLoginStatus().equals("0")) {

                                         session = request.getSession(true);
                                        session.setAttribute("connected", new String("true"));
                                        session.setMaxInactiveInterval(60 * comp.getSessionTimeout());

                                       SytemsManager sm = new SytemsManager();
                                        java.util.ArrayList functions = sm.findFunctionsBySecurityClass(user.getUserClass());
                                        java.util.List menus = menur.findMenus(user.getUserClass());
                                        java.util.ArrayList classFunctions = sm.findClassFunctionsById(user.getUserClass());
                                        //System.out.println(" User's Expiry Date >>>>>> " + user.getExpDate());
                                        session.setAttribute("classfunctions", classFunctions);
                                        session.setAttribute("Menus", menus);
                                        session.setAttribute("priviledges", functions);
                                        session.setAttribute("CurrentUser", user.getUserId());
                                        session.setAttribute("SignInName", user.getUserName());
                                        session.setAttribute("UserClass", user.getUserClass());
                                        session.setAttribute("UserCenter", user.getBranch());
                                        session.setAttribute("LastSignIn", user.getLastLogindate());
                                        session.setAttribute("SignInFrom", request.getRemoteAddr());
                                        session.setAttribute("IsSupervisor", user.getIsSupervisor());
                                        session.setAttribute("FleetAdmin", user.getFleetAdmin());
                                        session.setAttribute("_user", user);
                                        session.setAttribute("WorkstationName", workstationName);
                                        session.setAttribute("WorkstationIp", workstationIp);
                                        session.setAttribute("compCode", comp.getCompanyCode());

                                        String loguser = "N";

                                        if (comp.getLogUserAudit() != null) {
                                            loguser = comp.getLogUserAudit();
                                        }
                                        session.setAttribute("LoginAudit", loguser);
                                        Cookie cu = new Cookie("curr_user", user.getUserId());
                                        response.addCookie(cu);

                                        sh.updateLogins(user.getUserId(), "1", request.getRemoteAddr());

                                        session.setAttribute("FAILED_LOGON_CTN", "0");

                                        if (sh.queryPexpiry(user.getUserId())) {
                                            response.sendRedirect("changePassword1.jsp");

                                        } else if (user.getMustChangePwd().equals("Y")) {
                                            response.sendRedirect("changePassword1.jsp");
                                            //out.println("<script>window.location = 'changePassword.jsp'</script>");
                                        } else {
                                            response.sendRedirect("DocumentHelp.jsp");
                                           session.setAttribute("FAILED_LOGON_CTN", "0");
                                            handler.SaveLoginAudit(user.getUserId(), user.getBranch(), workstationName, workstationIp, id);
                                            System.out.println("-----------xx----------- Logging in --------");
                                            //out.println("<script>window.location = 'systemWebtop.jsp'</script>");
                                        }
                                    } else {
                                        out.print("<script>alert('User already connected to Internet Banking')</script>");
                                         out.println("<script>window.location = 'index.jsp'</script>");
                                    }

                                } else {
                                    System.out.println("In here --------- 2");
                                    out.print("<script>alert('Access Denied. Please contact your Administrator !!!!')</script>");
                                     out.println("<script>window.location = 'index.jsp'</script>");
                                }
                            } else {
                                out.print("<script>alert('You are not authorized to use Internet Banking')</script>");
                                 out.println("<script>window.location = 'index.jsp'</script>");
                            }//the else part of inactive user goes here
                        } else {
                            //for logon count logonAttempt
                            System.out.println("-----------xx----------- Logging in --hh----");
                            //System.out.println("<<<<<<<<<<<<<<<<< here 1");
                            if ((String) request.getSession().getAttribute("FAILED_LOGON_CTN") != null) {
                                //System.out.println("<<<<<<<<<<<<<<<<< here 2");
                                int logoncount = Integer.parseInt((String) request.getSession().getAttribute("FAILED_LOGON_CTN"));
                                logoncount += 1;
                                //System.out.println("<<<<<<<<<<<<<<<<< here logoncount " + logoncount);
                                if (logoncount == logonAttempt) {

                                    sh.updateLoginAsAboveLimit(request.getParameter("userName"), request.getRemoteAddr());
                                    out.print("<script>alert('You have exceded the allowed logon attempts limit.')</script>");
                                    out.println("<script>window.location = 'index.jsp'</script>");
                                } else {
                                    request.getSession().setAttribute("FAILED_LOGON_CTN", String.valueOf(logoncount));

                                }


                            }//if((String)session.getAttribute("FAILED_LOGON_CTN") != null)
                            else {

                                request.getSession().setAttribute("FAILED_LOGON_CTN", "1");
                                //out.print("<script>alert('Invalid user details. Try Again.')</script>");
                            }

                            //if(Integer.parseInt((String)session.getAttribute("FAILED_LOGON_CTN")) < 3)
                            out.print("<script>alert('Invalid User Name/Password details. Try Again.')</script>");
                            out.println("<script>window.location = 'index.jsp'</script>");

                        }
                        //License end
                        //uncomment the else part below to enforce license check
//                    } else {
//                        out.println("<script>alert('Application has expired or key is not valid');</script>");
//                        out.print("<script>window.location = 'index.jsp'</script>");
//                    }
                } catch (Throwable t) {
                    System.err.print(t.getMessage());
                    t.printStackTrace();
                    out.print("<script>alert('Error occured while processing your request. Try Again.')</script>");
                    out.println("<script>window.location = 'index.jsp'</script>");
                }//catch


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
