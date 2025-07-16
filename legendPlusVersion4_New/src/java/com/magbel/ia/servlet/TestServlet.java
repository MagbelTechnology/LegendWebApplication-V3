

package com.magbel.ia.servlet;

import com.magbel.ia.bus.AccountInterfaceServiceBus;
import com.magbel.ia.bus.SupervisorServiceBus;
import com.magbel.ia.vao.Imprest;
import com.magbel.ia.vao.ImprestRef;
import com.magbel.ia.vao.User;
import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class TestServlet extends HttpServlet
{

    private AccountInterfaceServiceBus serviceBus;
    private SupervisorServiceBus superv;
    public TestServlet()
    {
    }

    public void init(ServletConfig config)
        throws ServletException
    {
        super.init(config);
        serviceBus = new AccountInterfaceServiceBus();
        superv = new SupervisorServiceBus();
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
       // User loginId = (User)session.getAttribute("CurrentUser");
        String id = request.getParameter("id");
        String refNumber = request.getParameter("refno");
		String refno = request.getParameter("refno");
        String beneficiary = request.getParameter("beneficiary");
        String impAccNumber = request.getParameter("impaccount");
        String benAccNumber = request.getParameter("benacc");
        String purpose = request.getParameter("purpose");
        String expiryDate = request.getParameter("expirydate");
        String isRetired = request.getParameter("");
        String userId = "";//loginId.getUserId();
        String supervisorId = request.getParameter("supervisor");
        String transDate = request.getParameter("transdate");
        String effDate = request.getParameter("effdate");
        String isCash = request.getParameter("isCash");
        String strAmount = request.getParameter("amount");
        strAmount = strAmount.replaceAll(",","");
        String isPosted = request.getParameter("isposted");
        String companyCode = request.getParameter("companyCode");
        String strApproveOfficer = request.getParameter("approveOfficer");
        String strApproveStatus = request.getParameter("approveStatus");
        String cmdRetire = request.getParameter("btnRetire");
        String btnSave = request.getParameter("btnSave");
        
        com.magbel.ia.vao.User user = null;

           if(btnSave!=null)
		   {

               
              if (serviceBus.findNumber(refNumber)!=null)
					{
					out.println("<script>");
				    out.println("alert('Ref Code Already Exists...!')");
				    out.println("window.location='test.jsp'");
				    out.println("</script>");
					}
               else
                    {
                    out.println("<script>");
				    out.println("alert('Ref Code Not Exists...!')");
				    out.println("window.location='test.jsp'");
				    out.println("</script>");
                    }
       
          }
    }
    public String getServletInfo()
    {
        return "Imprest Action Servlet";
    }
}
