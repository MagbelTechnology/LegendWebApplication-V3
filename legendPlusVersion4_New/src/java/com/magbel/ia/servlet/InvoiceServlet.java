package com.magbel.ia.servlet;

import com.magbel.ia.bus.InvoiceServiceBus;
import com.magbel.ia.util.ApplicationHelper;
import com.magbel.ia.vao.PettyInvoice;
import com.magbel.util.HtmlUtility;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class InvoiceServlet extends HttpServlet
{

    private InvoiceServiceBus serviceBus;
    PettyInvoice imp;
    ApplicationHelper helper;

    public InvoiceServlet()
    {
    }

    public void init(ServletConfig config)
        throws ServletException
    {
        super.init(config);
        serviceBus = new InvoiceServiceBus();
        imp = new PettyInvoice();
        helper = new ApplicationHelper();
    }

    public void destroy()
    {
    }

    public void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {  
        response.setContentType("text/html");
        String mtid = "";   
        HtmlUtility apprecord = new HtmlUtility();
        javax.servlet.http.HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        String id = request.getParameter("id");
        String orderNo = request.getParameter("orderNo");
        String isCash = request.getParameter("isCash");
        String customerName = request.getParameter("customerName");
        String customerCode = request.getParameter("customerName");
        String description = request.getParameter("description");
        String ExpendHead = request.getParameter("ExpendHead");
        String strAmount = request.getParameter("amount");
        strAmount = strAmount.replaceAll(",", "");
        double amount = strAmount != null ? Double.parseDouble(strAmount) : 0.0D;
        String strQuantity = request.getParameter("quantity");
        strQuantity = strQuantity.replaceAll(",", "");
        double quantity = strQuantity != null ? Double.parseDouble(strQuantity) : 0.0D;
        double unitPrice = amount/quantity;
        String invoiceNo = request.getParameter("invoiceNo");
 //       String strUnitPrice = request.getParameter("unitPrice");
 //       strUnitPrice = strUnitPrice.replaceAll(",", "");
       // double unitPrice = strUnitPrice != null ? Double.parseDouble(strUnitPrice) : 0.0D;
    //    String invoiceNo = request.getParameter("invoiceNo");
        String CustomerNo = request.getParameter("customerCode");
        String transactionDate = request.getParameter("transactionDate");
        String companyCode = request.getParameter("companyCode");
        String branchCode = request.getParameter("branchCode");
        String schoolcode = request.getParameter("schoolcode");        
        String userid = request.getParameter("userid");
        String accountNo = request.getParameter("acctno");
        String Bank = request.getParameter("Bank");
        String chequeNo = request.getParameter("chequeNo");
        String term = request.getParameter("term");
        String ledgerNo = request.getParameter("ledgerNo");
        String projectCode = "";
     //   System.out.println("branchCode====> "+branchCode+"  School Code: "+schoolcode);        
        if(branchCode==null){branchCode=schoolcode;}
     //   System.out.println("branchCode====> "+branchCode+"  School Code: "+schoolcode); 
        if(id == null){invoiceNo = helper.getGeneratePVC(branchCode);}
      //  String invoiceNo = branchCode;
        String BranchAcronym =apprecord.findObject("SELECT BRANCH_ACRONYM FROM MG_AD_Branch WHERE BRANCH_CODE = '"+branchCode+"'");
       // System.out.println("BranchAcronym====> "+BranchAcronym);
        String CompanyAcronym =apprecord.findObject("SELECT ACRONYM FROM MG_gb_company WHERE COMPANY_CODE = '"+companyCode+"'");
        String branchNo =apprecord.findObject("SELECT Branch_id FROM MG_AD_Branch WHERE BRANCH_CODE = '"+branchCode+"'");
        invoiceNo = CompanyAcronym+"/"+BranchAcronym+"/"+invoiceNo;
        // System.out.println("isCash==== "+isCash);
        description = description+" "+invoiceNo;
        //description = description+" "+invoiceNo;
        String isposted = "N";
        String isRetired = "N";
        customerName =apprecord.findObject("SELECT CUSTOMER_NAME FROM IA_CUSTOMER WHERE CUSTOMER_CODE = '"+customerName+"'");
        try
        {  
            if(id == null)
            {
                serviceBus.createPaymentVoucher(customerName, orderNo, description, amount, quantity, projectCode, unitPrice, invoiceNo, accountNo, transactionDate, companyCode,branchCode,Bank,chequeNo,ExpendHead,userid,term,ledgerNo,customerCode);
                serviceBus.createPayableImprst(invoiceNo, customerName, accountNo, customerCode, description, "", isRetired, 
                userid, "", "", "", isCash, amount,isposted, companyCode, Integer.parseInt(branchNo),ledgerNo,orderNo);
                
                out.print("<script>alert('Record successfully saved.')</script>");
                String mid = serviceBus.getCodeName("select max(mtid) from IA_PV_SUMMARY");
                //out.print((new StringBuilder()).append("<script>window.location='DocumentHelp.jsp?np=newSalesInvoiceDetails&id="+mid).append(mid).append("'</script>").toString());
                out.print("<script>window.location='DocumentHelp.jsp?np=newSalesInvoiceDetails&id="+mid+"&invoiceNo="+invoiceNo+"&customerCode="+customerCode+"'</script>");
       //         out.println("<html><b><font color ='red'>No Transactions to Export to CSV!</font></b><br><a href ='DocumentHelp.jsp?np=ExportToCSVTrunc&CHECK_TYPE="+cType+"&clr_session="+clrSession+"&valueDate="+clearDate+"&ZONE_TYPE="+Zone+"'>Back</html>");
            } else
            {
                serviceBus.updatePaymentVoucher(id, customerName, orderNo, description, amount, quantity, projectCode, unitPrice, invoiceNo, accountNo, transactionDate,Bank,chequeNo,ExpendHead,ledgerNo);
                out.print("<script>alert('Record Updated successfully .')</script>");
                out.print("<script>window.location='DocumentHelp.jsp?np=newSalesInvoiceDetails&customerCode="+customerCode+"'</script>");
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