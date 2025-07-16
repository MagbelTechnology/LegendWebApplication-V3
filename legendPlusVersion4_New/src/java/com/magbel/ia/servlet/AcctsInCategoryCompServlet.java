package com.magbel.ia.servlet;
import java.io.IOException;
import java.io.PrintWriter;

import budget.net.vao.*;

import com.magbel.ia.util.*;

import budget.net.manager.*;

import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.magbel.ia.vao.DeptSection;
import com.magbel.ia.vao.User;


public class AcctsInCategoryCompServlet extends HttpServlet 
{
	/** 
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AcctsInCategoryCompServlet()
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
        String categoryId = request.getParameter("bid");
        String categorycode = request.getParameter("bcode");
        String branchcode = request.getParameter("branchcode");
        int nodept = Integer.parseInt(noofdepts);

        HttpSession session = request.getSession();
		User loginId = (User) session.getAttribute("CurrentUser");
		String compCode = loginId.getCompanyCode();
		
		BudgetManager genList = new  BudgetManager();
		
		CategoryAcct catldgr = null;
		
        ArrayList ilist = new ArrayList();
        ArrayList ulist = new ArrayList();
        ArrayList dlist = new ArrayList();
        
        for(int i = 0; i < nodept; i++)
        {
            String assign = request.getParameter("assign" + i);
            String lgrid  = request.getParameter("did" + i);
            String ledgerNo = request.getParameter("bcode" + i);
            String catclass = request.getParameter("categoryclass"+ i);
            String accounttype = request.getParameter("accounttype"+ i);
            System.out.println("<<<<categoryId: "+categoryId+"  ASSIGN: "+assign+"  lgrid: "+lgrid+"  compCode: "+compCode+"  ledgerNo: "+ledgerNo);
            String lgrcode  = request.getParameter("dcode"+ i);
            CategoryAcct bdept = genList.findAcctInCategoryComp(categoryId, lgrid, compCode);
//            System.out.println("<<<<bdept: "+bdept);
            if(assign == null)
            {
                assign = "N";
            }
            if(assign.equalsIgnoreCase("Y"))
            {
            	catldgr = new CategoryAcct();
            	
                catldgr.setCategoryCode(categorycode);
                catldgr.setCategoryId(categoryId);
                catldgr.setLedgerId(lgrid);
                catldgr.setLedgerCode(lgrcode);
                catldgr.setBranchCode(branchcode);
                catldgr.setCompanyCode(compCode);
                if(bdept != null)
                {
                    ulist.add(catldgr);
                } else
                {
                    ilist.add(catldgr);
                }
                continue;
            }
//            else{
            if(bdept != null)
            {
            	catldgr = new CategoryAcct();
                catldgr.setCategoryCode(categorycode);
                catldgr.setCategoryId(categoryId);
                catldgr.setLedgerId(lgrid);
                catldgr.setLedgerCode(lgrcode);
                catldgr.setBranchCode(branchcode);
                catldgr.setCompanyCode(compCode);
                dlist.add(catldgr);
            }
//            }
        }
        
        boolean ok = false;
//    	System.out.println("<<<<<<DLIST: "+dlist.isEmpty()+"     ILIST: "+ilist.isEmpty()+"    ULIST: "+ulist.isEmpty());
        if(!ilist.isEmpty() && genList.insertLedgerForCategoryComp(ilist))
        {
            out.print("<script>alert('Ledgers Created For Category!');</script>");
            ok = true;
        }
        if(!ulist.isEmpty() && genList.updateLedgerForCategoryComp(ulist))
        {
            out.print("<script>alert('Ledgers Updated Sucessfully!');</script>");
            ok = true;
        }
        if(!dlist.isEmpty() && genList.removeLedgerFromCategoryComp(dlist))
        {
            out.print("<script>alert('Ledgers Closed Sucessfully!');</script>");
            ok = true;
        }
        if(ok)
        {
            out.print("<script>window.location='DocumentHelp.jsp?np=category_account&bid="+categoryId+"&branchcode="+branchcode+"&bcode="+categorycode+"'</script>");
        } else
        {
            out.print("<script>alert('Error Updating Record!');</script>");
            out.print("<script>history.back();</script>");
        }
	}
}
