package com.magbel.ia.servlet;

import com.magbel.ia.bus.AccountInterfaceServiceBus;
import com.magbel.ia.vao.ImprestItems;
import com.magbel.ia.vao.User;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class ImprestItemActionServlet extends HttpServlet
{
    private AccountInterfaceServiceBus serviceBus;
    public ImprestItemActionServlet()
    {
    }
    public void init(ServletConfig config)
        throws ServletException
    {
        super.init(config);
        serviceBus = new AccountInterfaceServiceBus();
    }
    public void destroy()
    {
    }
    public void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        response.setContentType("text/html");
        String mtid = "";
        double impamount = 0.00d;
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        User loginId = (User)session.getAttribute("CurrentUser");
        String id = request.getParameter("id");
        String glAccount = request.getParameter("glaccount");
        String expglaccount = request.getParameter("expglaccount");
        String Accounimprest = request.getParameter("imprestAccount");
 //       System.out.println("glAccount:  "+glAccount+"  Identification: "+id);
        String refNumber = request.getParameter("refno");
        String orderNo = request.getParameter("orderNo");
        String description = request.getParameter("description");
        String strAmount = request.getParameter("amount");
		String type = request.getParameter("type");
		String companyCode = request.getParameter("companyCode");
        //double amount = strAmount == null ? 0.0D : Double.parseDouble(strAmount);
		com.magbel.ia.vao.User user = null;
		String userId = "";
		String benaccount = request.getParameter("benaccount");
//		System.out.println("Beneficiary Account: "+benaccount);
		ImprestItems imp =serviceBus.findImprestAvailable(refNumber);
		impamount = imp.getAmount();
		String imprestAccount = imp.getExpglaccount();
		if(session.getAttribute("CurrentUser")!=null)
		{
			user =(com.magbel.ia.vao.User)session.getAttribute("CurrentUser");
		}
			userId = user.getUserId();//to be completed
			int userId_ = Integer.parseInt(user.getUserId());
			String branchCode = user.getBranch();
			int branchNo = Integer.parseInt(user.getBranch());
			
		double amount =0.0D;
		double _amount =0.0D;
if(id == null)
  {
//	System.out.println("Imprest Amount: "+impamount+"   imprestAccount: "+imprestAccount+" Length: "+imprestAccount.length()+"   Accounimprest: "+Accounimprest);
//		if(serviceBus.findTABSByTABSId(glAccount) == null)
			if((impamount == 0.00) || (!imprestAccount.equals(Accounimprest)))
		     {
				out.println("<script>");
				out.println("alert('Retirement Account cannot be Empty!')");
				out.println("</script>");
				out.print("<script>window.close();</script>");
			 }
			else{
							 if(strAmount.indexOf(",") > 0)
							{
								String amt=strAmount.replace(",","");
								_amount =Double.parseDouble(amt.trim()); 
								serviceBus.createImprestItem(Accounimprest, refNumber, description, _amount,type,companyCode, branchNo,expglaccount,orderNo);
								out.print("<script>window.close();</script>");
							}
							else
							{
									amount =Double.parseDouble(strAmount);
									serviceBus.createImprestItem(Accounimprest, refNumber, description, amount, type,companyCode, branchNo,expglaccount,orderNo);
									out.print("<script>window.close();</script>");
							}	
				}
   }       
        else{	
			if(strAmount.indexOf(",") > 0)
							{
								String amt=strAmount.replace(",","");
								_amount =Double.parseDouble(amt.trim()); 
								serviceBus.updateImprestItem(Accounimprest,description, _amount,type,id,expglaccount);
								out.print("<script>window.close();</script>");
							}
							else
							{
									amount =Double.parseDouble(strAmount);
									serviceBus.updateImprestItem(Accounimprest,description, amount,type,id,expglaccount);
									out.print("<script>window.close();</script>");
							}
            }
    }
    public String getServletInfo()
    {
        return "Imprest Item  Action Servlet";
    }
}
