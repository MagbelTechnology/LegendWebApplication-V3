package com.magbel.ia.servlet;

import audit.AuditTrailGen;
import com.magbel.ia.bus.AdminServiceBus;
import com.magbel.ia.vao.Project;
import com.magbel.ia.vao.User;
import java.io.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class ProjectAuditServlet extends HttpServlet
{

    public ProjectAuditServlet()
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
        User loginId = (User)session.getAttribute("CurrentUser");
        String loginID;
        if(loginId == null)
        {
            loginID = "Unkown";
        } else
        {
            loginID = loginId.getUserName();
        }
        String branchcode = loginId.getBranch();
        if(branchcode == null)
        {
            branchcode = "not set";
        }
        String RowId = request.getParameter("id");
        String RecordId = request.getParameter("code");
        String RecordValue = request.getParameter("desc");
        String buttSave = request.getParameter("buttSave");
        String id = request.getParameter("id");
        String code = request.getParameter("code");
        String desc = request.getParameter("desc");
        String startDt = request.getParameter("startDt");
        String endDt = request.getParameter("endDt");
        String strCost = request.getParameter("cost");
        double cost = strCost == null ? 0.0D : Double.parseDouble(strCost);
        String capital = request.getParameter("capital");
        String other = request.getParameter("other");
        String transDt = request.getParameter("transDt");
        String status = request.getParameter("status");
        Project project = new Project();
        project.setId(id);
        project.setCode(code);
        project.setDesc(desc);
        project.setStartDt(startDt);
        project.setEndDt(endDt);
        project.setCost(strCost);
        project.setCapital(capital);
        project.setOther(other);
        project.setTransDt(transDt);
        project.setStatus(status);
        AdminServiceBus admin = new AdminServiceBus();
        try
        {
            if(buttSave != null)
            {
                if(id.equals(""))
                {
                    if(admin.findProjectByProjectCode(code) != null)
                    {
                        out.print("<script>alert('The Project Code already exists .');</script>");
                        out.print("<script>history.go(-1);</script>");
                    } else
                    if(admin.createProject(project))
                    {
                        out.print("<script>alert('Record saved successfully.');</script>");
                        out.print("<script>window.location = 'DocumentHelp.jsp?np=admin/manageProject&status=A'</sc" +
"ript>"
);
                    }
                }
                if(!id.equals(""))
                {
                    project.setId(id);
                    audit.select(1, (new StringBuilder()).append("SELECT * FROM IA_GB_PROJECT WHERE MTID ='").append(id).append("'").toString());
                    admin.updateProject(project);
                    audit.select(2, (new StringBuilder()).append("SELECT * FROM  IA_GB_PROJECT WHERE MTID ='").append(id).append("'").toString());
                    updtst = audit.logAuditTrail("IA_GB_PROJECT ", code, Integer.parseInt(loginID), RowId, RecordId, RecordValue);
                    if(updtst)
                    {
                        out.print("<script>alert('No changes made on record')</script>");
                        out.print("<script>window.location = 'DocumentHelp.jsp?np=admin/manageProject&status=A'</sc" +
"ript>"
);
                    } else
                    {
                        out.print("<script>alert('Update on record is successfull')</script>");
                        out.print((new StringBuilder()).append("<script>window.location = 'DocumentHelp.jsp?np=admin/projectSetup&id=").append(id).append("&PC=2';</script>").toString());
                    }
                }
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
        return "Project Audit Servlet";
    }
}
