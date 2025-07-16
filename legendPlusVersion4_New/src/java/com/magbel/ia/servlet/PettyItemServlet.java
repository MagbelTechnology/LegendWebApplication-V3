package com.magbel.ia.servlet;

//import com.magbel.ia.bus.AccounNo;
import com.magbel.ia.bus.InvoiceServiceBus;
import com.magbel.util.HtmlUtility;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class PettyItemServlet extends HttpServlet
{

    private InvoiceServiceBus serviceBus;

    public PettyItemServlet()
    {
    }

    public void init(ServletConfig config)
        throws ServletException
    {
        super.init(config);
        serviceBus = new InvoiceServiceBus();
    }

    public void destroy()
    {
    }

    public void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
    	HtmlUtility apprecord = new HtmlUtility();
        response.setContentType("text/html");
        String mtid = "";
        javax.servlet.http.HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        String id = request.getParameter("id");
       // id = id.trim();
        String ident = request.getParameter("id");
        String mid = request.getParameter("mid");
        String itemDescription = request.getParameter("description");
        String strAmount = request.getParameter("amount");
        strAmount = strAmount.replaceAll(",", "");
        double amount = strAmount != null ? Double.parseDouble(strAmount) : 0.0D;
        String strQuantity = request.getParameter("quantity");
        strQuantity = strQuantity.replaceAll(",", "");
        double quantity = strQuantity != null ? Double.parseDouble(strQuantity) : 0.0D;
        String strUnitPrice = request.getParameter("unitPrice");
        strUnitPrice = strUnitPrice.replaceAll(",", "");
        double unitPrice = strUnitPrice != null ? Double.parseDouble(strUnitPrice) : 0.0D;
        String invoiceNo = request.getParameter("invoiceNo");
        String accountNo = request.getParameter("acctno");
        String branchCode = request.getParameter("branchCode");
        String customerName = request.getParameter("customerName");
        String orderNo = request.getParameter("orderNo");
        String userid = request.getParameter("userid");
        String term = request.getParameter("term");
        String companyCode = request.getParameter("companyCode");
        String projectCode = request.getParameter("projectCode");
        String BranchCode =apprecord.findObject("SELECT BRANCH_CODE FROM MG_AD_Branch WHERE BRANCH_CODE = '"+branchCode+"'");
        String requirebudget =apprecord.findObject("SELECT ENFORCE_ACQ_BUDGET FROM MG_gb_company WHERE COMPANY_CODE = '"+companyCode+"'");
    	String  balAmount = apprecord.getCodeName("SELECT BALANCE_ALLOCATION FROM ACQUISITION_BUDGET WHERE COMP_CODE = '"+companyCode+"' AND BRANCH_ID = '"+branchCode+"' AND CATEGORY = '"+accountNo+"'");
    	balAmount = balAmount.replace(",","");
		double  budgetBalance = ((balAmount != null)&&(!balAmount.equals(""))) ? Double.parseDouble(balAmount) : 0;
        String stroldAmount = request.getParameter("oldamount");
        if(stroldAmount==""){stroldAmount = "0";}
        stroldAmount = stroldAmount.replaceAll(",", "");
        double oldAmount = stroldAmount != null ? Double.parseDouble(stroldAmount) : 0.0D;
        
 //       System.out.println("id>>>>>>>>>>>>>==== "+id+"  <<<<<<<<invoiceNo: "+invoiceNo);
        try
        {
        	if(requirebudget.trim().equalsIgnoreCase("N")){
    //    		System.out.println("PettyItemServlet requirebudget: "+requirebudget);
        //	if(ident.equalsIgnoreCase(""))
        	if(id=="")	
            { 
                if(serviceBus.createPettyItem(itemDescription, amount, quantity, projectCode, unitPrice, invoiceNo, accountNo, BranchCode,companyCode,userid,term,customerName,orderNo))
                {
                    out.print("<script>alert('Record successfully saved.')</script>");
                    out.print("<script>window.close();</script>");
                    out.print("<script>window.document.location='DocumentHelp.jsp?np=newPettyCashDetails&id=="+id+"&invoiceNo="+invoiceNo+"'</script>");
                } else
                {
                    out.print("<script>alert('Record Not saved.')</script>");
                    out.print("<script>window.document.location='DocumentHelp.jsp?np=newPettyCashDetails&id=="+id+"&invoiceNo="+invoiceNo+"'</script>");
                }
            } else
            {
                serviceBus.updatePettyItem(id, itemDescription, amount, quantity, projectCode, unitPrice,accountNo,term);
                out.print("<script>alert('Record Updated successfully .')</script>");
               // out.print((new StringBuilder()).append("<script>window.location='DocumentHelp.jsp?np=newPettyCashDetails&id=").append(mid).append("'</script>").toString());
                out.print("<script>window.document.location='DocumentHelp.jsp?np=newPettyCashDetails&id=="+mid+"&invoiceNo="+invoiceNo+"'</script>");
            }
        }        	
        else{
        	double accountBalance = 0.0;
        	if(requirebudget.trim().equalsIgnoreCase("Y")){
        		accountBalance = budgetBalance - amount;
        		if(accountBalance == 0.0){accountBalance = 0.1;}
        	}
        //	System.out.println("PettyItemServlet requirebudget: "+requirebudget+"  accountBalance: "+accountBalance+"  budgetBalance:"+budgetBalance+"  amount: "+amount);
        	if((requirebudget.trim().equalsIgnoreCase("Y")) && (accountBalance > 0.0)){
        //	if(ident.equalsIgnoreCase(""))
        	if(id=="")	
            { 
                if(serviceBus.createPettyItem(itemDescription, amount, quantity, projectCode, unitPrice, invoiceNo, accountNo, BranchCode,companyCode,userid,term,customerName,orderNo))
                {
                	serviceBus.updateBudgetForCashVoucher(companyCode, branchCode, accountNo, amount);
                    out.print("<script>alert('Record successfully saved.')</script>");
                    out.print("<script>window.close();</script>");
                    out.print("<script>window.document.location='DocumentHelp.jsp?np=newPettyCashDetails&id=="+id+"&invoiceNo="+invoiceNo+"'</script>");
                } else
                {
                    out.print("<script>alert('Record Not saved.')</script>");
                }
            } else
            {
                serviceBus.updatePettyItem(id, itemDescription, amount, quantity, projectCode, unitPrice,accountNo,term);
            	serviceBus.updateBudgetAddForCashVoucher(companyCode, branchCode, accountNo, amount, oldAmount);
            	serviceBus.updateBudgetForCashVoucher(companyCode, branchCode, accountNo, amount);
                out.print("<script>alert('Record Updated successfully .')</script>");
               // out.print((new StringBuilder()).append("<script>window.location='DocumentHelp.jsp?np=newPettyCashDetails&id=").append(mid).append("'</script>").toString());
                out.print("<script>window.document.location='DocumentHelp.jsp?np=newPettyCashDetails&id=="+mid+"&invoiceNo="+invoiceNo+"'</script>");
            }
        }
        	else
            {
                out.print("<script>alert('No budget for this Expense .')</script>");
                out.print("<script>window.document.location='DocumentHelp.jsp?np=newPettyCashDetails&id=="+id+"&invoiceNo="+invoiceNo+"'</script>");
            }
        }
        }
        catch(NullPointerException e)
        {
            response.sendRedirect("sessionTimedOut.jsp");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public String getServletInfo()
    {
        return "Invoice Action Servlet";
    }
}