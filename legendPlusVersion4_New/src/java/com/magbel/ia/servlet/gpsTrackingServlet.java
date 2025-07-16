package com.magbel.ia.servlet;

import audit.AuditTrailGen;
import com.magbel.ia.bus.AdminServiceBus;
import com.magbel.ia.vao.Project;
//import com.magbel.ia.vao.User;
import java.io.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class gpsTrackingServlet extends HttpServlet
{

    public gpsTrackingServlet()
    {
    }

    public void init(ServletConfig config)
        throws ServletException
    {
        super.init(config);
    }

    public void destroy()
    {
    }

    public void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        response.setContentType("text/html");
        response.setDateHeader("Expires", -1L);
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        String statusMessage = "";
        boolean updtst = false;
        AuditTrailGen audit = new AuditTrailGen();

        String buttTrack = request.getParameter("buttTrack");

        try
        {
        //	System.out.println("<<<<<<<<buttTrack: "+buttTrack);
            if(buttTrack != null)
            {
                out.print("<script>window.location = 'http://192.64.85.57/tramigo/'</script>");
            }
        }
        catch(Throwable e)
        {
            e.printStackTrace();
            out.print("<script>alert('Ensure unique record entry.')</script>");
            System.err.print(e.getMessage());
            out.print("<script>history.go(-1);</script>");
        }
    }

    public String getServletInfo()
    {
        return "GPS Tracking Servlet";
    }
}
