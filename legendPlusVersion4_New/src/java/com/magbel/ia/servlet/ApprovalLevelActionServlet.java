package com.magbel.ia.servlet;

import com.magbel.ia.bus.SetUpServiceBus;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class ApprovalLevelActionServlet extends HttpServlet
{

    private SetUpServiceBus serviceBus;

    public ApprovalLevelActionServlet()
    {
    }

    public void init(ServletConfig config)
        throws ServletException
    {
        super.init(config);
        serviceBus = new SetUpServiceBus();
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
        String code = request.getParameter("code");
        String strMinAmt = request.getParameter("minAmt");
        double minAmt = strMinAmt == null ? 0.0D : Double.parseDouble(strMinAmt);
        String strMaxAmt = request.getParameter("maxAmt");
        double maxAmt = strMaxAmt == null ? 0.0D : Double.parseDouble(strMaxAmt);
        String desc = request.getParameter("desc");
        String strAdjMin = request.getParameter("adjMin");
        int adjMin = strAdjMin == null ? 0 : Integer.parseInt(strAdjMin);
        String strAdjMax = request.getParameter("adjMax");
        int adjMax = strAdjMax == null ? 0 : Integer.parseInt(strAdjMax);
        String strAdjCon = request.getParameter("adjCon");
        int adjCon = strAdjCon == null ? 0 : Integer.parseInt(strAdjCon);
        String strConcur = request.getParameter("concur");
        int concur = strConcur == null ? 0 : Integer.parseInt(strConcur);
        if(id == null)
        {
            if(serviceBus.findApprovalLevelByApprovalLevelId(code) != null)
            {
                out.println("<script>");
                out.println("alert('ApprovalLevel Code Already Exists...!')");
                out.println("window.location='DocumentHelp.jsp?np=approvalRecords'");
                out.println("</script>");
            } else
            {
                serviceBus.createApprovalLevel(code, minAmt, maxAmt, desc, adjMin, adjMax, adjCon, concur);
                out.println("<script>");
                out.println("alert('ApprovalLevel was successfully created!')");
                out.println("window.location='DocumentHelp.jsp?np=approvalRecords'");
                out.println("</script>");
            }
        } else
        {
            serviceBus.updateApprovalLevel(id, code, minAmt, maxAmt, desc, adjMin, adjMax, adjCon, concur);
            out.println("<script>");
            out.println("alert('ApprovalLevel was successfully updated!')");
            out.println("window.location='DocumentHelp.jsp?np=approvalRecords'");
            out.println("</script>");
        }
    }

    public String getServletInfo()
    {
        return "ApprovalLevel Action Servlet";
    }
}
