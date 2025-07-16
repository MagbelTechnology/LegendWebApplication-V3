package com.magbel.ia.servlet;

import audit.AuditTrailGen;
import com.magbel.ia.bus.SecurityServiceBus;
import com.magbel.ia.vao.SecurityClass;
import com.magbel.ia.vao.User;
import com.magbel.ia.util.*;
import java.io.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class SecClassAuditServlet extends HttpServlet
{

    public SecClassAuditServlet()
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
        AuditTrailGen audit = new AuditTrailGen();
        ApplicationHelper2 applHelper = new ApplicationHelper2();
        GenerateList genList = new GenerateList();
        String statusMessage = "";
        boolean updtst = false;
        User loginId = (User)session.getAttribute("CurrentUser");
        String loginID;
        if(loginId == null)
        {
            loginID = "Unkown";
        } else
        {
            loginID = loginId.getUserName().trim();
        }
        String branchcode = loginId.getBranch();
        if(branchcode == null)
        {
            branchcode = "not set";
        }
        String RowId = request.getParameter("classId").trim();
        String RecordId = request.getParameter("className").trim();
        String RecordValue = request.getParameter("classDesc").trim();
        String classId = request.getParameter("classId");
        SecurityClass sc = new SecurityClass();
        SecurityServiceBus sh = new SecurityServiceBus();
        String buttSave = request.getParameter("buttSave");
        String description = request.getParameter("classDesc");
        String className = request.getParameter("className");
        String isSupervisor = "N";
        if(request.getParameter("isSupervisor") != null)
        {
            isSupervisor = request.getParameter("isSupervisor");
        }
        String isStoreKeeper = "N";
        if(request.getParameter("isStoreKeeper") != null)
        {
        	isStoreKeeper = request.getParameter("isStoreKeeper");
        }
        String classStatus = request.getParameter("classStatus");
        String userId = ((User)session.getAttribute("CurrentUser")).getUserName();
        sc.setDescription(description);
        sc.setClassName(className);
        sc.setClassStatus(classStatus);
        sc.setIsSupervisor(isSupervisor);
        sc.setUserId(userId);
        sc.setIsStoreKeeper(isStoreKeeper);
        try
        {
            if(buttSave != null)
            {
                if(classId.equals(""))
                {
                    if(genList.findSecurityClassByName(className) == null)
                    {
                        if(applHelper.createSecurityClass(sc))
                        {
                            out.print("<script>alert('Record saved successfully.')</script>");
                            out.print("<script>window.location = 'DocumentHelp.jsp?np=admin/securityClasses&classId="+(genList.findSecurityClassByName(className).getClassId())+"&PC=10'</script>");
                        } else
                        {
                            System.out.println("Error saving record: New record \nfor 'security class'  with class name "+className+" could not be created");
                            out.print("<script>history.go(-1);</script>");
                        }
                    } else
                    {
                        out.print("<script>alert('Class Name "+ className+" exists.')</script>");
                        out.print("<script>window.history.back()</script>");
                    }
                } else
                if(!classId.equals(""))
                {
                    sc.setClassId(classId);
                    audit.select(1, "SELECT * FROM  MG_GB_CLASS  WHERE class_Id = '"+classId+"'");
                    boolean isupdt = applHelper.updateSecurityClass(sc);
                    audit.select(2, ("SELECT * FROM  MG_GB_CLASS  WHERE class_Id = '"+classId+"'"));
                    updtst = audit.logAuditTrail("MG_gb_class ", branchcode, loginID, RowId, RecordId, RecordValue);
                    if(updtst)
                    {
                        out.print("<script>alert('Update on record is successfull')</script>");
                        out.print("<script>window.location = 'DocumentHelp.jsp?np=admin/securityClasses&classId="+classId+"&PC=10'</script>");
                    } else
                    {
                        out.print("<script>alert('No changes made on record')</script>");
                        out.print("<script>window.location = 'DocumentHelp.jsp?np=admin/securityClasses&classId="+ classId+ "&PC=10'</script>");
                    }
                }
            }
        }
        catch(Throwable e)
        {
            e.printStackTrace();
            out.print("<script>alert('Ensure unique record entry.')</script>");
            System.err.print(e.getMessage());
            out.print("<script>window.location = 'DocumentHelp.jsp?np=admin/securityClasses&classId="+classId+"&PC=10'</script>");
        }
    }
}
