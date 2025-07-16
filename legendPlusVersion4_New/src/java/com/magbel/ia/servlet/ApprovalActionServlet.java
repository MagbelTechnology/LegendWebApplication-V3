package com.magbel.ia.servlet;

import com.magbel.ia.bus.ParameterServiceBus;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class ApprovalActionServlet extends HttpServlet
{

    private ParameterServiceBus serviceBus;

    public ApprovalActionServlet()
    {
    }

    public void init(ServletConfig config)
        throws ServletException
    {
        super.init(config);
        serviceBus = new ParameterServiceBus();
    }

    public void destroy()
    {
    }

    public void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        response.setContentType("text/html");
        javax.servlet.http.HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        String id = request.getParameter("id");
        String code = request.getParameter("code");
        String strMin = request.getParameter("min");
        double min = strMin == null ? 0.0D : Double.parseDouble(strMin);
        String strMax = request.getParameter("max");
        double max = strMax != null && !strMax.equals("") ? Double.parseDouble(strMax) : 0.0D;
        String desc = request.getParameter("desc");
        if(id == null)
        {
            if(serviceBus.isApprovalExisting(id))
            {
                out.println("<script>");
                out.println("alert('Approval already exist for the indicated Bank!')");
                out.println("window.location='DocumentHelp.jsp?np=approvalRecords'");
                out.println("</script>");
            }
            if(code.equals("") || code == null)
            {
                out.print("<script>alert('Level Code. cannot be empty')</script>");
                out.print("<script>history.go(-1);</script>");
            }
            if(serviceBus.isApprovalExisting((new StringBuilder()).append("SELECT COUNT(MTID) FROM IA_APPROVAL WHERE MTID='").append(id).append("'").toString()))
            {
                out.print("<script>alert('Approval Already Exist')</script>");
                out.print("<script>history.go(-1);</script>");
            }
            if(serviceBus.isApprovalLevelExisting((new StringBuilder()).append("SELECT COUNT(CODE) FROM IA_APPROVAL WHERE CODE = '").append(code).append("'").toString()))
            {
                out.print("<script>alert('Approval Level  Already Exist')</script>");
                out.print("<script>history.go(-1);</script>");
            }
            serviceBus.createApproval(code, min, max, desc);
            out.print("<script>alert('Record Succesfully Saved.')</script>");
            out.print("<script>window.document.location='DocumentHelp.jsp?np=approvalRecords'</script>");
        } else
        {
            serviceBus.updateApproval(id, code, min, max, desc);
            out.print("<script>alert('Record Succesfully Updated.');</script>");
            out.print("<script>window.location='DocumentHelp.jsp?np=approvalRecords'</script>");
        }
    }
}
