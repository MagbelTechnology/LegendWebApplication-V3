package com.magbel.ia.servlet;

import audit.AuditTrailGen;
import budget.net.vao.CategoryMapp;
import budget.net.manager.BudgetManager;
import com.magbel.ia.vao.Branch;
import com.magbel.ia.vao.User;
import com.magbel.util.CheckIntegerityContraint;
import com.magbel.util.HtmlUtilily;
import java.io.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class CategoryAuditServlet extends HttpServlet
{
	HtmlUtilily htmlUtil;
    public CategoryAuditServlet()
    {
    	 htmlUtil = new HtmlUtilily();
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
        User loginId = (User)session.getAttribute("CurrentUser");
        String loginID;
        String userID = "";;
        if(loginId == null)
        {
            loginID = "Unkown";
        } else
        {
            loginID = loginId.getUserName();
            userID = loginId.getUserId();
        }
        String branchcode = loginId.getBranch();
        if(branchcode == null)
        {
            branchcode = "not set";
        }
        String RowId = request.getParameter("categoryId");
        String RecordId = request.getParameter("categoryCode");
        String RecordValue = request.getParameter("categoryName");
        String categoryId = request.getParameter("categoryId");
        String categoryStatus = request.getParameter("categoryStatus");
        String buttSave = request.getParameter("buttSave");
        String acronym = request.getParameter("categoryAcronym");
        String categorytype = request.getParameter("categorytype");  
        if(acronym != null)
        {
            acronym = acronym.toUpperCase();
        }
        CategoryMapp categoryMapp = new CategoryMapp();
        String categoryCode = request.getParameter("categoryCode");
        String categoryName = request.getParameter("categoryName");
        String categoryAcronym = acronym;
        String branchCode = request.getParameter("branchCode");
        String username = loginID;
        String companyCode = request.getParameter("companyCode");
        String branch = htmlUtil.getCodeName("SELECT branch_id FROM MG_AD_Branch WHERE COMPANY_CODE = '"+companyCode+"' AND BRANCH_CODE =  '"+branchCode+"' ");
        System.out.println("<<<<<<<categoryCode: "+categoryCode+"    categoryName: "+categoryName+"  categoryAcronym:"+categoryAcronym+"  branchCode: "+branchCode+"    categoryStatus: "+categoryStatus);
        categoryMapp.setCategoryId(categoryId);
        categoryMapp.setCategoryCode(categoryCode);
        categoryMapp.setCategoryAcronym(categoryAcronym);
        categoryMapp.setCategorytype(categorytype);
        categoryMapp.setCategoryName(categoryName);
        categoryMapp.setBranchCode(branchCode);
        categoryMapp.setuserId(userID);
        categoryMapp.setStatus(categoryStatus);
        categoryMapp.setCompanyCode(companyCode);
        BudgetManager budgt = new BudgetManager();
        try
        {
            if(buttSave != null)
            {
                if(categoryId.equals(""))
                {
                    if(budgt.findBranchByBranchCode(categoryCode) != null)
                    {
                        out.print("<script>alert('The category code already exists .');</script>");
                        out.print("<script>history.go(-1);</script>");
                    } else
                    if(budgt.createCategory(categoryMapp))
                    {
                        out.print("<script>alert('Record saved successfully.');</script>");
                        out.print((new StringBuilder()).append("<script>window.location = 'DocumentHelp.jsp?np=CategorySetup&categoryId=").append(budgt.findBranchByBranchCode(categoryCode).getCategoryId()).append("&PC=8';</script>").toString());
                    }
                } else
                if(!categoryId.equals(""))
                {
                	categoryMapp.setCategoryId(categoryId);
                    CheckIntegerityContraint intCont = new CheckIntegerityContraint();
                    if(intCont.checkReferenceConstraint("MG_GB_USER", "BRANCH", branch, categoryStatus))
                    {
                        out.print("<script>alert('This Category Code is being referenced by other records it thus can" +
"not by closed.')</script>"
);
                        out.print((new StringBuilder()).append("<script>window.location = 'DocumentHelp.jsp?np=CategorySetup&branchId=").append(categoryId).append("&PC=8'</script>").toString());
                    } else
                    {
                        audit.select(1, (new StringBuilder()).append("SELECT * FROM  IA_GL_CATEGORY   WHERE category_Id = '").append(categoryId).append("'").toString());
                        boolean isupdt = budgt.updateCategory(categoryMapp);
                        audit.select(2, (new StringBuilder()).append("SELECT * FROM  IA_GL_CATEGORY  WHERE category_Id = '").append(categoryId).append("'").toString());
                        updtst = audit.logAuditTrail("IA_GL_CATEGORY ", branchcode, loginID, RowId, RecordId, RecordValue);
                        if(updtst)
                        {
                            out.print("<script>alert('Update on record is successfull')</script>");
                            out.print((new StringBuilder()).append("<script>window.location = 'DocumentHelp.jsp?np=CategorySetup&categoryId=").append(categoryId).append("&PC=8'</script>").toString());
                        } else
                        {
                            out.print("<script>alert('No changes made on record')</script>");
                            out.print((new StringBuilder()).append("<script>window.location = 'DocumentHelp.jsp?np=CategorySetup&categoryId=").append(categoryId).append("&PC=8'</script>").toString());
                        }
                    }
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            out.print("<script>alert('Ensure unique record entry.')</script>");
            out.print((new StringBuilder()).append("<script>window.location = 'DocumentHelp.jsp?np=CategorySetup&categoryId=").append(categoryId).append("&PC=8'</script>").toString());
            System.err.print(e.getMessage());
        }
    }
}