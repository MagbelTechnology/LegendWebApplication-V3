package com.magbel.ia.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.magbel.ia.bus.GLAccountServiceBus;
import com.magbel.ia.dao.PersistenceServiceDAO;

public class GLTotalingActionServlet extends HttpServlet
{

    private GLAccountServiceBus serviceBus;

    public GLTotalingActionServlet()
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
        javax.servlet.http.HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        String id = request.getParameter("id");
        String accountType = request.getParameter("accountType");
        String ledgerNo = request.getParameter("ledgerNo");
        String levelNo = request.getParameter("levelNo");
        String description = request.getParameter("description");
        String effectiveDate = request.getParameter("effectiveDate");
        String totalingLevel = request.getParameter("totalingLevel");
        String totalingLedgerNo = request.getParameter("totalingLedgerNo");
        String ledgerType = request.getParameter("ledgerType");
        String debitCredit = request.getParameter("debitCredit");
        String status = request.getParameter("status");
        int userId = 0;
        String createDate = request.getParameter("createDate");
        com.magbel.ia.vao.User loginId = (com.magbel.ia.vao.User) session.getAttribute("CurrentUser");
        String companyCode = loginId.getCompanyCode();
        if(id == null || id == "")
        {
            PersistenceServiceDAO cd = new PersistenceServiceDAO();
            createDate = cd.formatDate(new Date());
            if(serviceBus.findGLTotalingByLedgerNo(ledgerNo) != null)
            {
                out.println("<script>");
                out.println("alert('Ledger No Already Exists...!')");
                out.println("window.location='totalingAccountSetUp.jsp'");
                out.println("</script>");
            } else
            {
                serviceBus.createGLTotaling(accountType, ledgerNo, levelNo, description, effectiveDate, totalingLevel, totalingLedgerNo, ledgerType, debitCredit, status, userId, createDate,companyCode);
                out.println("<script>");
                out.println("alert('GLTotaling was successfully created!')");
                out.println("window.location='DocumentHelp.jsp?np=totalingAccountSetUp'");
                out.println("</script>");
            }
        } else
        {
            serviceBus.updateGLTotaling(id, accountType, ledgerNo, levelNo, description, effectiveDate, totalingLevel, totalingLedgerNo, ledgerType, debitCredit, status, userId, createDate);
            out.println("<script>");
            out.println("alert('GLTotaling was successfully updated!')");
            out.println("window.location='DocumentHelp.jsp?np=totalingAccountSetUp'");
            out.println("</script>");
        }
    }

    public String getServletInfo()
    {
        return "GLTotaling Action Servlet";
    }
}
