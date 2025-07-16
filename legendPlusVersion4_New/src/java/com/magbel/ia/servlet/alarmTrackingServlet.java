package com.magbel.ia.servlet;

import audit.AuditTrailGen;

import com.magbel.ia.bus.AdminServiceBus;
import com.magbel.ia.vao.Project;

//import com.magbel.ia.vao.User;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class alarmTrackingServlet extends HttpServlet
{

    public alarmTrackingServlet()
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
            	sendAlarm();
            	out.print("<script>window.location='DocumentHelp.jsp?np=alarmTracking'</script>");
             //   out.print("<script>window.location = 'http://192.64.85.57/tramigo/'</script>");
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

public void sendAlarm()
{
	//System.out.println("Just called the sendEmail API");
	try
	{
		Properties prop = new Properties();
		File file = new File("C:\\Property\\Inventory.properties");
		FileInputStream in = new FileInputStream(file);
		prop.load(in);
		System.out.print("Able to load Alarm properties into prop for set off");
		String url = prop.getProperty("host.urloff");
		String USER_AGENT = "SetOff";
		//System.out.println("<<<<<<<urlAlarm: "+url);
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);
		int responseCode = con.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);

	} 
	catch (Exception ex) 
	{
		System.out.println("point of Alarm Off");
		ex.printStackTrace();
	}

}		
    
}
