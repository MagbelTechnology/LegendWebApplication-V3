package com.magbel.ia.servlet;

import com.magbel.ia.bus.InvoiceServiceBus;
import com.magbel.ia.util.ApplicationHelper;
import com.magbel.ia.vao.SalesInvoice;
import com.magbel.ia.vao.User;
import com.magbel.util.HtmlUtility;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class PettyCashServlet extends HttpServlet
{

    private InvoiceServiceBus serviceBus;
    SalesInvoice imp;
    ApplicationHelper helper;

    public PettyCashServlet()
    {
    }

    public void init(ServletConfig config)
        throws ServletException
    {
        super.init(config);
        serviceBus = new InvoiceServiceBus();
        imp = new SalesInvoice();
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
        //id = id.trim();
        String ident = request.getParameter("id");
        String mid = request.getParameter("mid");
        String orderNo = request.getParameter("orderNo");
        String customerName = request.getParameter("customerName");
        String customerCode = request.getParameter("customerName");
        customerName =apprecord.findObject("SELECT CUSTOMER_NAME FROM IA_CUSTOMER WHERE CUSTOMER_CODE = '"+customerName+"'");
        String description = request.getParameter("description");
        String ExpendHead = request.getParameter("ExpendHead");
        String strAmount = request.getParameter("amount");
        strAmount = strAmount.replaceAll(",", "");
        double amount = strAmount != null ? Double.parseDouble(strAmount) : 0.0D;
        String strQuantity = request.getParameter("quantity");
        strQuantity = strQuantity.replaceAll(",", "");
        double quantity = strQuantity != null ? Double.parseDouble(strQuantity) : 0.0D;
        double unitPrice = amount/quantity;
        String stroldAmount = request.getParameter("oldamount");
 //       System.out.println("PettyCashServlet Old Amount: "+stroldAmount);
        if(stroldAmount==""){stroldAmount = "0";}
        stroldAmount = stroldAmount.replaceAll(",", "");
        double oldAmount = stroldAmount != null ? Double.parseDouble(stroldAmount) : 0.0D;
        String CustomerNo = request.getParameter("customerCode");
        String transactionDate = request.getParameter("transactionDate");
        String companyCode = request.getParameter("companyCode");
        String branchCode = request.getParameter("branchCode");
        String userid = request.getParameter("userid");
        String accountNo = request.getParameter("acctno");
        String Bank = request.getParameter("Bank");
        String chequeNo = request.getParameter("chequeNo");
        String pageName = request.getParameter("PageName");
        String invoiceNo = request.getParameter("invoiceNo");
        String customerAccount = request.getParameter("customerAccount");
        String projectCode = request.getParameter("projectCode");
        String customerAccountNo = apprecord.findObject("SELECT ACCOUNT_NO FROM IA_CUSTOMER_ACCOUNT_DISPLAY WHERE CUSTOMER_CODE = '"+customerAccount+"'");
        String term = request.getParameter("term");
        User user = null;
        if(session.getAttribute("CurrentUser") != null)
        {
            user = (User)session.getAttribute("CurrentUser");
        }
       // System.out.println("user==== "+user);
//        System.out.println("addApplicationFormBtn==== "+addApplicationFormBtn);
        int userId = Integer.parseInt(user.getUserId());  
        String UserId = user.getUserId();  
        if((branchCode==null)||(branchCode=="")){branchCode=user.getBranch();}
//        System.out.println("branchCode PettyCashServlet==== "+branchCode);
        
      //  String invoiceNo = branchCode;
        String BranchAcronym =apprecord.findObject("SELECT BRANCH_ACRONYM FROM MG_AD_Branch WHERE BRANCH_CODE = '"+branchCode+"'");
//        System.out.println("BranchAcronym==== "+BranchAcronym);
        String CompanyAcronym =apprecord.findObject("SELECT ACRONYM FROM MG_gb_company WHERE COMPANY_CODE = '"+companyCode+"'");
        String requirebudget =apprecord.findObject("SELECT ENFORCE_ACQ_BUDGET FROM MG_gb_company WHERE COMPANY_CODE = '"+companyCode+"'");
        if(requirebudget==null){requirebudget = "N";}
    	String  balAmount = apprecord.getCodeName("SELECT BALANCE_ALLOCATION FROM ACQUISITION_BUDGET WHERE COMP_CODE = '"+companyCode+"' AND BRANCH_ID = '"+branchCode+"' AND CATEGORY = '"+accountNo+"'");
    	balAmount = balAmount.replace(",","");
		double  budgetBalance = ((balAmount != null)&&(!balAmount.equals(""))) ? Double.parseDouble(balAmount) : 0;
 /*       if(ident.equalsIgnoreCase("")){
        	invoiceNo = helper.getGeneratePVC(branchCode);
        	invoiceNo = CompanyAcronym+"/"+BranchAcronym+"/"+invoiceNo;
        }*/
//         System.out.println("invoiceNo==== "+invoiceNo+"    requirebudget: "+requirebudget);
        try
        {
        	if(requirebudget.trim().equalsIgnoreCase("N")){
//        		System.out.println("PettyCashServlet requirebudget: "+requirebudget);
                if(ident.equalsIgnoreCase("")){
                	invoiceNo = helper.getGeneratePVC(branchCode);
                	invoiceNo = CompanyAcronym+"/"+BranchAcronym+"/"+invoiceNo;
                }
        	if(ident.equalsIgnoreCase(""))
            {
                serviceBus.createPettyCashVoucher(customerName, orderNo, description, amount, quantity,projectCode, unitPrice, invoiceNo, accountNo, transactionDate, companyCode,branchCode,Bank,chequeNo,ExpendHead,userid,term,customerCode);                                                
                out.print("<script>alert('Record successfully saved.')</script>");
                mid = serviceBus.getCodeName("select max(mtid) from IA_PC_SUMMARY");
                //out.print((new StringBuilder()).append("<script>window.location='DocumentHelp.jsp?np=newSalesInvoiceDetails&id="+mid).append(mid).append("'</script>").toString());
                out.print("<script>window.location='DocumentHelp.jsp?np=newPettyCashDetails&id="+invoiceNo+"&invoiceNo="+invoiceNo+"'</script>");
       //         out.println("<html><b><font color ='red'>No Transactions to Export to CSV!</font></b><br><a href ='DocumentHelp.jsp?np=ExportToCSVTrunc&CHECK_TYPE="+cType+"&clr_session="+clrSession+"&valueDate="+clearDate+"&ZONE_TYPE="+Zone+"'>Back</html>");
            } else
            { 
                serviceBus.updatePettyCashVoucher(id, customerName, orderNo, description, amount, quantity,projectCode, unitPrice, invoiceNo, accountNo, transactionDate,Bank,chequeNo,ExpendHead);
                out.print("<script>alert('Record Updated successfully .')</script>");
                //out.print("<script>window.location='DocumentHelp.jsp?np=newPettyCashDetails'</script>");
                out.print("<script>window.document.location='DocumentHelp.jsp?np=newPettyCashDetails&id="+invoiceNo+"&invoiceNo="+invoiceNo+"'</script>");                
            }
        }
        	else{
        	double accountBalance = 0.0;
        	if((requirebudget.equalsIgnoreCase("Y")) || (accountBalance > 0.0)){
        		accountBalance = budgetBalance - amount;
        		if(accountBalance == 0.0){accountBalance = 0.1;}
        	}
        //	System.out.println("PettyCashServlet requirebudget: "+requirebudget+"  accountBalance: "+accountBalance+"  budgetBalance:"+budgetBalance+"  amount: "+amount);
        	if((requirebudget.trim().equalsIgnoreCase("Y")) || (accountBalance > 0.0)){
                if(ident.equalsIgnoreCase("")){
                	invoiceNo = helper.getGeneratePVC(branchCode);
                	invoiceNo = CompanyAcronym+"/"+BranchAcronym+"/"+invoiceNo;
                }
        	if(ident.equalsIgnoreCase(""))
            {
                serviceBus.createPettyCashVoucher(customerName, orderNo, description, amount, quantity, projectCode, unitPrice, invoiceNo, accountNo, transactionDate, companyCode,branchCode,Bank,chequeNo,ExpendHead,userid,term,customerCode);
                serviceBus.updateBudgetForCashVoucher(companyCode, branchCode, accountNo, amount);
                out.print("<script>alert('Record successfully saved.')</script>");
                mid = serviceBus.getCodeName("select max(mtid) from IA_PC_SUMMARY");
                //out.print((new StringBuilder()).append("<script>window.location='DocumentHelp.jsp?np=newSalesInvoiceDetails&id="+mid).append(mid).append("'</script>").toString());
                out.print("<script>window.location='DocumentHelp.jsp?np=newPettyCashDetails&id="+invoiceNo+"&invoiceNo="+invoiceNo+"'</script>");
       //         out.println("<html><b><font color ='red'>No Transactions to Export to CSV!</font></b><br><a href ='DocumentHelp.jsp?np=ExportToCSVTrunc&CHECK_TYPE="+cType+"&clr_session="+clrSession+"&valueDate="+clearDate+"&ZONE_TYPE="+Zone+"'>Back</html>");
            } else
            { 
            	
                serviceBus.updatePettyCashVoucher(id, customerName, orderNo, description, amount, quantity, projectCode, unitPrice, invoiceNo, accountNo, transactionDate,Bank,chequeNo,ExpendHead);
            	serviceBus.updateBudgetAddForCashVoucher(companyCode, branchCode, accountNo, amount, oldAmount);
            	serviceBus.updateBudgetForCashVoucher(companyCode, branchCode, accountNo, amount);
                out.print("<script>alert('Record Updated successfully .')</script>");
                //out.print("<script>window.location='DocumentHelp.jsp?np=newPettyCashDetails'</script>");
                out.print("<script>window.document.location='DocumentHelp.jsp?np=newPettyCashDetails&id="+invoiceNo+"&invoiceNo="+invoiceNo+"'</script>");                
            }
        }
        	 else
             { 
                 out.print("<script>alert('No budget for this Expense .')</script>");
                 out.print("<script>window.document.location='DocumentHelp.jsp?np=newPettyCashDetails&id="+invoiceNo+"&invoiceNo="+invoiceNo+"'</script>");                
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