package com.magbel.ia.servlet;

import com.magbel.ia.bus.AccountInterfaceServiceBus;
import com.magbel.ia.vao.ImprestItems;
import com.magbel.ia.vao.User;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class ImprestItemActionServletOLD extends HttpServlet
{
    private AccountInterfaceServiceBus serviceBus;
    public ImprestItemActionServletOLD()
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
        String refNumber = request.getParameter("refno");
        String description = request.getParameter("description");
        String strAmount = request.getParameter("amount");
		String type = request.getParameter("type");
		String companyCode = request.getParameter("companyCode");
		String benaccount = request.getParameter("benaccount");
		ImprestItems imp =serviceBus.findImprestAvailable(benaccount);
		impamount = imp.getAmount();
        //double amount = strAmount == null ? 0.0D : Double.parseDouble(strAmount);
		double amount =0.0D;
		double _amount =0.0D;
if(id == null)
  {
//		if(serviceBus.findImprestAvailable(benaccount) == null)
		if(impamount == 0.00)
		     { 
				out.println("<script>");
				out.println("alert('Retiment Account cannot be Empty!')");
				out.println("</script>");
				out.print("<script>window.close();</script>");
			 }
			else{
							 if(strAmount.indexOf(",") > 0)
							{
								String amt=strAmount.replace(",","");
								_amount =Double.parseDouble(amt.trim()); 
								serviceBus.createImprestItem(glAccount, refNumber, description, _amount,type,companyCode);
								out.print("<script>window.close();</script>");
							}
							else
							{
									amount =Double.parseDouble(strAmount);
									serviceBus.createImprestItem(glAccount, refNumber, description, amount, type,companyCode);
									out.print("<script>window.close();</script>");
							}	
				}
   }       
        else{	
			if(strAmount.indexOf(",") > 0)
							{
								String amt=strAmount.replace(",","");
								_amount =Double.parseDouble(amt.trim()); 
								serviceBus.updateImprestItem(glAccount,description, _amount,type,id);
								out.print("<script>window.close();</script>");
							}
							else
							{
									amount =Double.parseDouble(strAmount);
									serviceBus.updateImprestItem(glAccount,description, amount,type,id);
									out.print("<script>window.close();</script>");
							}
            }
    }
    public String getServletInfo()
    {
        return "Imprest Item  Action Servlet";
    }
}
