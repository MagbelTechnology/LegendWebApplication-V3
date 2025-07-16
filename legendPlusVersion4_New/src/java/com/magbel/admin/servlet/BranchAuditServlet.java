/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.magbel.admin.servlet;

import audit.AuditTrailGen;
import com.magbel.util.CheckIntegerityContraint;
import java.io.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import com.magbel.admin.handlers.AdminHandler;
import com.magbel.admin.objects.Branch;

public class BranchAuditServlet extends HttpServlet
{

    public BranchAuditServlet()
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
        boolean updtstatus = false;
        AuditTrailGen audit = new AuditTrailGen();
        String loginId = (String)session.getAttribute("CurrentUser");
        int loginID;
        if(loginId == null)
        {
            loginID = 0;
        } else
        {
            loginID = Integer.parseInt(loginId);
        }
        String branchcode = (String)session.getAttribute("UserCenter");
        if(branchcode == null)
        {
            branchcode = "not set";
        }
        String branchId = request.getParameter("branchId");
        String branchStatus = request.getParameter("branchStatus");
        String buttSave = request.getParameter("buttSave");
        String acronym = request.getParameter("branchAcronym");
        if(acronym != null)
        {
            acronym = acronym.toUpperCase();
        }
        Branch branch = new Branch();
        String branchCode = request.getParameter("branchCode");
        String branchName = request.getParameter("branchName").toUpperCase();
        String branchAcronym = acronym.toUpperCase();
        String glPrefix = request.getParameter("glPrefix").toUpperCase();
        String branchAddress = request.getParameter("branchAddress").toUpperCase();
        String state = request.getParameter("branchState");
        String phoneNo = request.getParameter("branchPhone");
        String faxNo = request.getParameter("branchFax");
        String region = request.getParameter("branchRegion");
        String province = request.getParameter("branchProvince");
        String username = (String)session.getAttribute("CurrentUser");
        //Vickie - create a new field
        String emailAddress = request.getParameter("emailAddress").toUpperCase();
        //System.out.println("VIIIIICKKKKIEEEEE: Its getting the value from the field " + emailAddress);
       

        branch.setBranchAcronym(branchAcronym);
        branch.setBranchAddress(branchAddress);
        branch.setBranchCode(branchCode);
        branch.setBranchName(branchName);
        branch.setBranchStatus(branchStatus);
        branch.setFaxNo(faxNo);
        branch.setGlPrefix(glPrefix);
        branch.setPhoneNo(phoneNo);
        branch.setProvince(province);
        branch.setRegion(region);
        branch.setState(state);
        branch.setUsername(username);
        branch.setEmailAddress(emailAddress);
       
        AdminHandler admin = new AdminHandler();
        try
        {
            if(buttSave != null)
            {
                if(branchId.equals(""))
                {
                    if(admin.getBranchByBranchCode(branchCode) != null)
                    {
                        out.print("<script>alert('The branch code already exists .');</script>");
                        out.print("<script>history.go(-1);</script>");
                    } else
                    if(admin.createBranch(branch))
                    {
                        out.print("<script>alert('Record saved successfully.');</script>");
                        out.print((new StringBuilder("<script>window.location = 'DocumentHelp.jsp?np=branchSetup&branchId=")).append(admin.getBranchByBranchCode(branchCode).getBranchId()).append("&PC=8';</script>").toString());
                    }
                } else
                if(!branchId.equals(""))
                {
                    branch.setBranchId(branchId);
                    CheckIntegerityContraint intCont = new CheckIntegerityContraint();
                    if(intCont.checkReferenceConstraint("AM_GB_USERS", "BRANCH_CODE", branch.getBranchCode(), branchStatus))
                    {
                        out.print("<script>alert('This Branch Code is being referenced by other records it thus can" +
"not by closed.')</script>"
);
                        out.print((new StringBuilder("<script>window.location = 'DocumentHelp.jsp?np=branchSetup&branchId=")).append(branchId).append("&PC=8'</script>").toString());
                    } else
                    {
                        audit.select(1, (new StringBuilder("SELECT * FROM  AM_AD_BRANCH   WHERE branch_Id = '")).append(branchId).append("'").toString());
                        boolean isupdt = admin.updateBranch(branch);
                        audit.select(2, (new StringBuilder("SELECT * FROM  AM_AD_BRANCH  WHERE branch_Id = '")).append(branchId).append("'").toString());
                        updtst = audit.logAuditTrail("AM_AD_BRANCH ", branchcode, loginID, branchId,"","");
                        if(updtst)
                        {
                            out.print("<script>alert('Update on record is successfull')</script>");
                            out.print((new StringBuilder("<script>window.location = 'DocumentHelp.jsp?np=branchSetup&branchId=")).append(branchId).append("&PC=8'</script>").toString());
                        } else
                        {
                            out.print("<script>alert('No changes made on record')</script>");
                            out.print((new StringBuilder("<script>window.location = 'DocumentHelp.jsp?np=branchSetup&branchId=")).append(branchId).append("'</script>").toString());
                        }
                    }
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            out.print("<script>alert('Ensure unique record entry.')</script>");
            out.print((new StringBuilder("<script>window.location = 'DocumentHelp.jsp?np=branchSetup&branchId=")).append(branchId).append("'</script>").toString());
            System.err.print(e.getMessage());
        }
    }
}
