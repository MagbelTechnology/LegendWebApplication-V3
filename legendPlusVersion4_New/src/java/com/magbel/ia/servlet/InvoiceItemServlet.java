package com.magbel.ia.servlet;

//import com.magbel.ia.bus.AccounNo;
import com.magbel.ia.bus.InvoiceServiceBus;
import com.magbel.util.HtmlUtility;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class InvoiceItemServlet extends HttpServlet
{

    private InvoiceServiceBus serviceBus;

    public InvoiceItemServlet()
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
        System.out.println("<<<<<<<<<ID in InvoiceItemServlet>>>>>>>>>>> "+id);
        if(id == null){id="";}
        id = id.trim();
        String ident = request.getParameter("id");
        if(ident==null){ident="";}
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
        String userid = request.getParameter("userid");
        String companyCode = request.getParameter("companyCode");
        String pageName = request.getParameter("PageName");
        String chequeNo = request.getParameter("chequeNo");
        String customerName = request.getParameter("customerName");
        String orderNo = request.getParameter("orderNo");
        String term = request.getParameter("term");
//        String BranchCode =apprecord.findObject("SELECT BRANCH_CODE FROM MG_AD_Branch WHERE BRANCH_ID = '"+branchCode+"'");
        System.out.println("<<<<<<<<<Ident>>>>>>>>>>> "+ident+"   invoiceNo: "+invoiceNo);
        try
        {
        	
            if(ident.equalsIgnoreCase(""))
            { 
            	
                if(serviceBus.createInvoiceItem(itemDescription, amount, quantity, unitPrice, invoiceNo))
                {  
                    out.print("<script>alert('Record successfully saved.')</script>");
                    out.print("<script>window.close();</script>");
                    out.print("<script>window.document.location='DocumentHelp.jsp?np=newSalesInvoiceDetails&id=="+id+"&invoiceNo="+invoiceNo+"'</script>");
                } else
                {  
                    out.print("<script>alert('Record Not saved.')</script>");
                }
            } else
            {  
                serviceBus.updateInvoiceItem(id, itemDescription, amount, quantity, unitPrice);
                out.print("<script>alert('Record Updated successfully .')</script>");
                //out.print((new StringBuilder()).append("<script>window.location='DocumentHelp.jsp?np=newSalesInvoiceDetails&id=").append(mid).append("'</script>").toString());
                out.print("<script>window.document.location='DocumentHelp.jsp?np=newSalesInvoiceDetails&id=="+mid+"&invoiceNo="+invoiceNo+"'</script>");
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