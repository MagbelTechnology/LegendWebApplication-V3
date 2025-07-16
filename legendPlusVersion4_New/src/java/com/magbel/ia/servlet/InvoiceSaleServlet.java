package com.magbel.ia.servlet;

import com.magbel.ia.bus.InvoiceServiceBus;
import com.magbel.ia.vao.SalesInvoice;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class InvoiceSaleServlet extends HttpServlet
{

    private InvoiceServiceBus serviceBus;
    SalesInvoice imp;

    public InvoiceSaleServlet()
    {
    }
 
    public void init(ServletConfig config)
        throws ServletException
    {
        super.init(config);
        serviceBus = new InvoiceServiceBus();
        imp = new SalesInvoice();
    }

    public void destroy()
    {
    }

    public void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        response.setContentType("text/html");
        String mtid = "";
        javax.servlet.http.HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        String id = request.getParameter("id");
        String orderNo = request.getParameter("orderNo");
        String customerName = request.getParameter("customerName");
        String description = request.getParameter("description");
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
        String accountNo = request.getParameter("customerCode");
        String transactionDate = request.getParameter("transactionDate");
        String companyCode = request.getParameter("companyCode");
        try
        {
            if(id == null)
            {
                serviceBus.createInvoice(customerName, orderNo, description, amount, quantity, unitPrice, invoiceNo, accountNo, transactionDate, companyCode);
                out.print("<script>alert('Record successfully saved.')</script>");
                String mid = serviceBus.getCodeName("select max(mtid) from ia_sales_invoice");
                out.print((new StringBuilder()).append("<script>window.location='DocumentHelp.jsp?np=newSalesInvoiceDetails&id=").append(mid).append("'</script>").toString());
            } else
            {
                serviceBus.updateInvoice(id, customerName, orderNo, description, amount, quantity, unitPrice, invoiceNo, accountNo, transactionDate);
                out.print("<script>alert('Record Updated successfully .')</script>");
                out.print("<script>window.location='DocumentHelp.jsp?np=SalesnvoiceItemList'</script>");
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