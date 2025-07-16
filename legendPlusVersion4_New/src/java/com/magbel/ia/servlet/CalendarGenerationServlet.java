package com.magbel.ia.servlet;

//import com.magbel.ia.bus.SalesOrderServiceBus;

import com.magbel.ia.bus.SupervisorServiceBus;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.*;
import javax.servlet.http.*;

public class CalendarGenerationServlet extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html; charset=windows-1252";
    private SupervisorServiceBus serviceBus;
    
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        serviceBus = new SupervisorServiceBus();
    }
    public void destroy() {

    }
   
    public void service(HttpServletRequest request,HttpServletResponse response) 
                throws ServletException, IOException {
                
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        response.setContentType("text/html");
        
        //String id = request.getParameter("ID");
        String pymtCode = "";//request.getParameter("pymtCode");
        String financeEndYear = request.getParameter("period_end_date");
        String financeStartYear = request.getParameter("period_start_date");
        String startday = financeStartYear.substring(0, 2);
        String startmonth = financeStartYear.substring(3, 5);
        String startyear = financeStartYear.substring(6, 10);
        String startdate = startyear+"-"+startmonth+"-"+startday;
//        System.out.println("<<<<<<<startday: "+startday+"    <<<<<startmonth: "+startmonth+"  startyear: "+startyear+"    startdate: "+startdate);
        String endday = financeEndYear.substring(0, 2);
        String endmonth = financeEndYear.substring(3, 5);
        String endyear = financeEndYear.substring(6, 10);
        String enddate = endyear+"-"+endmonth+"-"+endday;
//        System.out.println("<<<<<<<endday: "+endday+"    <<<<<endmonth: "+endmonth+"  endyear: "+endyear+"    enddate: "+enddate);
//        System.out.println("<<<<<<<financeStartYear: "+financeStartYear+"    <<<<<financeEndYear: "+financeEndYear);
        String companyCode = request.getParameter("companyCode");
        String companyId = serviceBus.getCodeName("SELECT COMPANY_ID FROM MG_gb_company WHERE COMPANY_CODE='"+companyCode+"'");
        String pymtPg = "admin/companyDefaults";
        com.magbel.ia.vao.User user = null;
         try{
               if(session.getAttribute("CurrentUser")!=null){user =(com.magbel.ia.vao.User)session.getAttribute("CurrentUser");}
               int userId = Integer.parseInt(user.getUserId());//to be completed
                String branchId = user.getBranch();
                String branchcode = serviceBus.getCodeName("SELECT BRANCH_CODE FROM MG_AD_Branch WHERE BRANCH_ID='"+branchId+"'");

                                if(serviceBus.getNewYearCalendar(companyCode,startdate,enddate)){
                                   out.print("<script>alert('Record Succesfully Saved.')</script>");
                                   out.print("<script>window.document.location='DocumentHelp.jsp?np="+pymtPg+"&companyId="+companyId+"&PC=1'</script>");
                                    
                                }
                    }
                    
              catch(NullPointerException e){
                   response.sendRedirect("sessionTimeOut.jsp");
                }
              catch(Exception e){
                  e.printStackTrace();
              }
              
    }
    
    public String getServletInfo() {
            return "AR Action Servlet";
    }
}
