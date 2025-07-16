package com.magbel.ia.servlet;

import com.magbel.ia.bus.ReconServiceBus;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

public class ProjectActionServlet extends HttpServlet
{

    private ReconServiceBus serviceBus;

    public ProjectActionServlet()
    {
    }

    public void init(ServletConfig config)
        throws ServletException
    {
        super.init(config);
        serviceBus = new ReconServiceBus();
    }

    public void destroy()
    {
    } 

    public void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        response.setContentType("text/html");
        String mtid = "";
        jakarta.servlet.http.HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        String id = request.getParameter("id");
        String code = request.getParameter("code");
        String desc = request.getParameter("desc");
        String cCode = request.getParameter("cCode");
        String startDt = request.getParameter("startDt");
        String endDt = request.getParameter("endDt");
        String cost = request.getParameter("cost");
        String capital = request.getParameter("capital");
        String onlineNumber = request.getParameter("onlineNumber");
        String projectAcct = request.getParameter("projectAcct");
        String projectSponsor = request.getParameter("projectSponsor");
        String projectOwner = request.getParameter("projectOwner");
        String projectManager = request.getParameter("projectManager");
        String projectFundBal = request.getParameter("projectFundBal");
        String projectAmtUtilized = request.getParameter("projectAmtUtilized");
        String other = request.getParameter("other");
        String transDt = request.getParameter("transDt");
        String status = request.getParameter("status");
        String branchId = request.getParameter("branch_id");
        String departmentId = request.getParameter("department_id");
        String sbuCode = request.getParameter("sbu_code");
        
        if(id == null)
        {  
            if(serviceBus.findProjectByProjectId(code) != null)
            {
                out.println("<script>");
                out.println("alert('Project Code Already Exists...!')");
                out.println("window.location='DocumentHelp.jsp?np=projectRecords'");
                out.println("</script>");
            } else
            { System.out.println("<<<<<<<<<<<<About to Create the Project");
                serviceBus.createProject(code, desc, cCode, startDt, endDt, cost, capital, other, transDt, status,branchId,departmentId,sbuCode,
                		onlineNumber,projectAcct,projectSponsor,projectOwner,projectManager,projectFundBal,projectAmtUtilized);
                out.println("<script>");
                out.println("alert('Project was successfully created!')");
                out.println("window.location='DocumentHelp.jsp?np=projectRecords'");
                out.println("</script>");
            }
        } else
        {
            serviceBus.updateProject(id, code, desc, cCode, startDt, endDt, cost, capital, other, transDt, status,branchId,departmentId,sbuCode,
            		onlineNumber,projectAcct,projectSponsor,projectOwner,projectManager,projectFundBal,projectAmtUtilized);
            out.println("<script>");
            out.println("alert('Project was successfully updated!')");
            out.println("window.location='DocumentHelp.jsp?np=projectRecords'");
            out.println("</script>");
        }
    }

    public String getServletInfo()
    {
        return "Project Action Servlet";
    }
}
