package com.magbel.ia.servlet;

import com.magbel.ia.bus.GLAccountServiceBus;
import com.magbel.ia.vao.User;
import java.io.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class GLAccountLedgerActionServlet extends HttpServlet
{

    private GLAccountServiceBus serviceBus;

    public GLAccountLedgerActionServlet()
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
        if(loginId == null)
        {
            loginID = "Unkown";
        } else
        {
            loginID = loginId.getUserId();
        }
        String id = request.getParameter("id");
        String accountType = request.getParameter("accountType");
        String accountNo = request.getParameter("accountNo");
        String mask = "";
        String levelNo = request.getParameter("levelNo");
        String ledgerNo = request.getParameter("ledgerNo");
        String description = request.getParameter("description");
        String totalingLevel = request.getParameter("totalingLevel");
        String totalingLedgerNo = request.getParameter("totalingLedgerNo");
        String localTax = request.getParameter("localTax");
        String stateTax = request.getParameter("stateTax");
        String fedTax = request.getParameter("fedTax");
        String debitCredit = request.getParameter("debitCredit");
        String currency = request.getParameter("currency");
        String effectiveDate = request.getParameter("effectiveDate");
        String status = request.getParameter("status");
        String autoReplicate = request.getParameter("autoReplicate");
        String reconAccount = request.getParameter("reconAccount");
        String ledgerType = request.getParameter("ledgerType");
        String drNAllowed = request.getParameter("drNAllowed");
        String crNAllowed = request.getParameter("crNAllowed");
        String totalIndicator = request.getParameter("totalIndicator");
        String parentId = request.getParameter("parentId");
        String companyCode = request.getParameter("companyCode");
        String userId = loginID;
        System.out.println((new StringBuilder()).append("").append(id).toString());
        if(autoReplicate == null || autoReplicate.equals(""))
        {
            autoReplicate = "N";
        }
        if(reconAccount == null || reconAccount.equals(""))
        {
            reconAccount = "N";
        }
        if(levelNo.equals("100"))
        {
            totalIndicator = "GL";
        } else
        {
            totalIndicator = "TOT";
        }
        if(drNAllowed == null || drNAllowed.equals(""))
        {
            drNAllowed = "N";
        }
        if(crNAllowed == null || crNAllowed.equals(""))
        {
            crNAllowed = "N";
        }
        if(localTax == null || localTax.equals("")) {localTax = "N";}
        if(stateTax == null || stateTax.equals("")) {stateTax = "N";}
        if(fedTax == null || fedTax.equals("")) {fedTax = "N";}        
        if(id == null || id == "")
        {
            serviceBus.createGLAccountLedger(accountType, accountNo, mask, levelNo, ledgerNo, description, totalingLevel, totalingLedgerNo, localTax, stateTax, fedTax, debitCredit, currency, effectiveDate, status, autoReplicate, reconAccount, ledgerType, drNAllowed, crNAllowed, totalIndicator, userId, parentId,companyCode);
            out.println("<script>");
            out.println("alert('GLAccountLedger was successfully created!')");
            out.println("window.location='DocumentHelp.jsp?np=glAccountLedgerSetUp'");
            out.println("</script>");
        } else
        {
            serviceBus.updateGLAccountLedger(id, accountType, accountNo, mask, levelNo, ledgerNo, description, totalingLevel, totalingLedgerNo, localTax, stateTax, fedTax, debitCredit, currency, effectiveDate, status, autoReplicate, reconAccount, ledgerType, drNAllowed, crNAllowed, totalIndicator, userId, parentId,"","","");
            out.println("<script>");
            out.println("alert('GLAccountLedger was successfully updated!')");
            out.println("window.location='DocumentHelp.jsp?np=glAccountLedgerSetUp'");
            out.println("</script>");
        }
    }

    public String getServletInfo()
    {
        return "GLAccountLedger Action Servlet";
    }
}
