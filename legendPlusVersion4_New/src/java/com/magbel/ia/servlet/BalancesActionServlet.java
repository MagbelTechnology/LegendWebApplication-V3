package com.magbel.ia.servlet;

import com.magbel.ia.bus.GLAccountServiceBus;
import com.magbel.ia.vao.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class BalancesActionServlet extends HttpServlet
{

    private GLAccountServiceBus serviceBus;

    public BalancesActionServlet()
    {
    }  

    public void init(ServletConfig config)
        throws ServletException
    {
        super.init(config);
        serviceBus = new GLAccountServiceBus();
    }

    public void destroy()
    {
    }  

    public void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        response.setContentType("text/html");
        String mtid = "";
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        User loginId = (User)session.getAttribute("CurrentUser");
        String loginID;
        String companyCODE;
        if(loginId == null)
        {
            loginID = "Unkown";
            companyCODE = "Unkown";
        } else
        {
            loginID = loginId.getUserId();
            companyCODE = loginId.getCompanyCode();
        }
        String buttSave = request.getParameter("buttSave");
        String branchCode = request.getParameter("branchCode");
        String userId = loginID;
        String companyCode = companyCODE;
        if(buttSave != null)
        {
            serviceBus.createBalances(branchCode, userId, companyCode);
            out.println("<script>");
            out.println("alert('Balances Created!....')");
            out.println("window.location='DocumentHelp.jsp?np=newBalances'");
            out.println("</script>");
        }
    }

    public String getServletInfo()
    {
        return "Balances Action Servlet";
    }
}
