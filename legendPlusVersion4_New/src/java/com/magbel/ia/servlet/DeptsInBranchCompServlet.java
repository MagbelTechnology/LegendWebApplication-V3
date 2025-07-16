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


public class DeptsInBranchCompServlet extends HttpServlet 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DeptsInBranchCompServlet()
    {
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response)    throws ServletException, IOException
	{
		DeptsInBranchComp(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)    throws ServletException, IOException
	{
		DeptsInBranchComp(request, response);
	}
	
	private void DeptsInBranchComp(HttpServletRequest request,	HttpServletResponse response) throws ServletException, IOException
	{
		PrintWriter out = response.getWriter();
		String noofdepts = request.getParameter("size");
        String branchId = request.getParameter("bid");
        String branchcode = request.getParameter("bcode");
        int nodept = Integer.parseInt(noofdepts);

        
        HttpSession session = request.getSession();
		User loginId = (User) session.getAttribute("CurrentUser");
		String compCode = loginId.getCompanyCode();
		
		GenerateList genList = new  GenerateList();
		ApplicationHelper2 applHelper = new ApplicationHelper2();
		
		BranchDept ibdept = null;
		
        ArrayList ilist = new ArrayList();
        ArrayList ulist = new ArrayList();
        ArrayList dlist = new ArrayList();
        
        for(int i = 0; i < nodept; i++)
        {
            String assign = request.getParameter("assign" + i);
            String deptid  = request.getParameter("did" + i);
            String glprefix = request.getParameter("glprefix"+ i);
            String glsuffix = request.getParameter("glsuffix"+ i);
            String deptcode  = request.getParameter("dcode"+ i);
            BranchDept bdept = genList.findDeptInBranchComp(branchId, deptid, compCode);
            if(assign == null)
            {
                assign = "N";
            }
            if(assign.equalsIgnoreCase("Y"))
            {
            	ibdept = new BranchDept();
            	
                ibdept.setBranchCode(branchcode);
                ibdept.setBranchId(branchId);
                ibdept.setDeptId(deptid);
                ibdept.setDeptCode(deptcode);
                ibdept.setGl_prefix(glprefix);
                ibdept.setGl_suffix(glsuffix);
                ibdept.setCompCode(compCode);
                if(bdept != null)
                {
                    ulist.add(ibdept);
                } else
                {
                    ilist.add(ibdept);
                }
                continue;
            }
            if(bdept != null)
            {
            	ibdept = new BranchDept();
                ibdept.setBranchCode(branchcode);
                ibdept.setBranchId(branchId);
                ibdept.setDeptId(deptid);
                ibdept.setDeptCode(deptcode);
                ibdept.setGl_prefix(glprefix);
                ibdept.setGl_suffix(glsuffix);
                ibdept.setCompCode(compCode);
                dlist.add(ibdept);
            }
        }
        
        boolean ok = false;
        if(!ilist.isEmpty() && applHelper.insertDeptForBranchComp(ilist))
        {
            out.print("<script>alert('Departments Created For Branch!');</script>");
            ok = true;
        }
        if(!ulist.isEmpty() && applHelper.updateDeptForBranchComp(ulist))
        {
            out.print("<script>alert('Departments Updated Sucessfully!');</script>");
            ok = true;
        }
        if(!dlist.isEmpty() && applHelper.removeDeptFromBranchComp(dlist))
        {
            out.print("<script>alert('Departments Closed Sucessfully!');</script>");
            ok = true;
        }
        if(ok)
        {
            out.print("<script>window.location='DocumentHelp.jsp?np=admin/branch_dept&bid="+branchId+"&bcode="+branchcode+"'</script>");
        } else
        {
            out.print("<script>alert('Error Updating Record!');</script>");
            out.print("<script>history.back();</script>");
        }
	}
}
