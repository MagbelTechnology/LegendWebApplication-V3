package com.magbel.ia.servlet;
import java.io.IOException;
import java.io.PrintWriter;
import com.magbel.ia.vao.*;
import com.magbel.ia.util.*;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.magbel.ia.vao.DeptSection;


public class SectionsInDeptCompServlet extends HttpServlet 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SectionsInDeptCompServlet()
    {
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response)    throws ServletException, IOException
	{
		SectionsInDeptComp(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)    throws ServletException, IOException
	{
		SectionsInDeptComp(request, response);
	}
	
	private void SectionsInDeptComp(HttpServletRequest request,	HttpServletResponse response) throws ServletException, IOException
	{
		PrintWriter out = response.getWriter();
        String noofdepts = request.getParameter("size");
        String branchId = request.getParameter("bid");
        String branchcode = request.getParameter("bcode");
        String deptid = request.getParameter("dept");
        String deptcode = request.getParameter("dcode");
        int nodept = Integer.parseInt(noofdepts);
        
        HttpSession session = request.getSession();
		User loginId = (User) session.getAttribute("CurrentUser");
		String compCode = loginId.getCompanyCode();
		
		GenerateList genList = new  GenerateList();
		ApplicationHelper2 applHelper = new ApplicationHelper2();
		
		DeptSection deptSect = null;
		
        ArrayList ilist = new ArrayList();
        ArrayList ulist = new ArrayList();
        ArrayList dlist = new ArrayList();
        
        for(int i = 0; i < nodept; i++)
        {
            String assign = request.getParameter("assign" + i);
            String secttid = request.getParameter("sectid" + i);
            String glprefix = request.getParameter("glprefix"+ i);
            String glsuffix = request.getParameter("glsuffix"+ i);
            String sectcode = request.getParameter("sectcode"+ i);
            DeptSection sdept = genList.findSectionInDeptComp(branchId, deptid, secttid,compCode);
            if(assign == null)
            {
                assign = "N";
            }
            if(assign.equalsIgnoreCase("Y"))
            {
                deptSect = new DeptSection();
                deptSect.setBranchCode(branchcode);
                deptSect.setBranchId(branchId);
                deptSect.setDeptId(deptid);
                deptSect.setDeptCode(deptcode);
                deptSect.setGl_prefix(glprefix);
                deptSect.setGl_suffix(glsuffix);
                deptSect.setSectionCode(sectcode);
                deptSect.setSectionId(secttid);
                deptSect.setCompCode(compCode);
                
                if(sdept != null)
                {
                    ulist.add(deptSect);
                } else
                {
                    ilist.add(deptSect);
                }
                continue;
            }
            if(sdept != null)
            {
                deptSect = new DeptSection();
                deptSect.setBranchCode(branchcode);
                deptSect.setBranchId(branchId);
                deptSect.setDeptId(deptid);
                deptSect.setDeptCode(deptcode);
                deptSect.setGl_prefix(glprefix);
                deptSect.setGl_suffix(glsuffix);
                deptSect.setSectionCode(sectcode);
                deptSect.setSectionId(secttid);
                deptSect.setCompCode(compCode);
                dlist.add(deptSect);
            }
        }
        
        boolean ok = false;
        if(!ilist.isEmpty() && applHelper.InsertSectionForDeptComp(ilist))
        {
            out.print("<script>alert('Sections Created For Deparment!');</script>");
            ok = true;
        }
        if(!ulist.isEmpty() && applHelper.updateSectionForDeptComp(ulist))
        {
            out.print("<script>alert('Sections Updated Sucessfully!');</script>");
            ok = true;
        }
        if(!dlist.isEmpty() && applHelper.removeSectionsFromDeptComp(dlist))
        {
            out.print("<script>alert('Sections Closed Sucessfully!');</script>");
            ok = true;
        }
        if(ok)
        {
            out.print("<script>window.location='DocumentHelp.jsp?np=admin/dept_section&bid="+branchId+"&bcode="+branchcode+"&dept="+deptid+"&dcode="+deptcode+"'</script>");
        } else
        {
            out.print("<script>alert('Error Updating Record!');</script>");
            out.print("<script>history.back();</script>");
        }
	}
}
