
package com.magbel.ia.servlet;

import com.magbel.ia.bus.AccountInterfaceServiceBus;
import com.magbel.ia.vao.ImprestAccount;
import com.magbel.ia.vao.User;
import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class ImprestAccountActionServlet extends HttpServlet
{

    private AccountInterfaceServiceBus serviceBus;

    public ImprestAccountActionServlet()
    {
    }

    public void init(ServletConfig config)
        throws ServletException
    {
        super.init(config);
        serviceBus = new AccountInterfaceServiceBus();
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
        java.io.PrintWriter out = response.getWriter();
        User loginId = (User)session.getAttribute("CurrentUser");
        String id = request.getParameter("id");
        String code = request.getParameter("code");
        String type = request.getParameter("type");
        String glAccount = request.getParameter("glAccount");
        String strRetire = request.getParameter("retirePolicy");
        int retirePolicy = strRetire == null ? 0 : Integer.parseInt(strRetire);
        String strGrace = request.getParameter("gracedays");
        int grace = strGrace == null ? 0 : Integer.parseInt(strGrace);
		String companyCode = request.getParameter("companyCode");
        if(id == null)
        {
            serviceBus.createImprestAccount(code, type, glAccount, retirePolicy, grace,companyCode);
            mtid = serviceBus.findImprestAccountByCode(code).getMtId();
			out.print("<script>window.location='DocumentHelp.jsp?np=ImprestControlRecords'</script>");
        } else
        {
            serviceBus.updateImprestAccount(id, type, glAccount, retirePolicy, grace);
            mtid = id;
        }
        response.sendRedirect((new StringBuilder("DocumentHelp.jsp?np=imprestControlDetails&id=")).append(mtid).toString());
    }

    public String getServletInfo()
    {
        return "Imprest Accounts Action Servlet";
    }
}
