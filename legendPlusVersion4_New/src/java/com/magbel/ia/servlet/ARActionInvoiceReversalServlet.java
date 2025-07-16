package com.magbel.ia.servlet;

//import com.magbel.ia.bus.SalesOrderServiceBus;

import com.magbel.ia.bus.ARServiceBus;
import com.magbel.ia.bus.ApprovalRecords;
import com.magbel.ia.util.ApplicationHelper;
import com.magbel.ia.vao.User;
import com.magbel.util.HtmlUtilily;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.*;
import javax.servlet.http.*;

public class ARActionInvoiceReversalServlet extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html; charset=windows-1252";
    private ARServiceBus serviceBus;
    ApprovalRecords Apprecord;
    ApplicationHelper helper;
    HtmlUtilily htmlUtil;
    
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        serviceBus = new ARServiceBus();
        Apprecord = new ApprovalRecords();    
        helper = new ApplicationHelper();
        htmlUtil = new HtmlUtilily();
    }
    public void destroy() {

    }
   
    public void service(HttpServletRequest request,HttpServletResponse response) 
                throws ServletException, IOException {
                
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        response.setContentType("text/html");
        String pymtCode = "";//request.getParameter("pymtCode");
        String customerCode = request.getParameter("customerCode");
        String orderCode = request.getParameter("orderCode");
        String accountNo = request.getParameter("accountNo");
        String chequeNo = request.getParameter("chequeNo");
        String bankCode = request.getParameter("bankCode");
//        String strAmountPaid = request.getParameter("amountPaid");
        String strAmountPaid = request.getParameter("reverseAmount");
        strAmountPaid = strAmountPaid.replaceAll(",","");
        double amountPaid=Double.parseDouble(strAmountPaid);//(strAmountPaid!=null && !strAmountPaid.equals(""))?Double.parseDouble(strAmountPaid):0;
        double amountOwing = 0;
        String tellerNo = request.getParameter("tellerNo");
        String pymtType = request.getParameter("pymtType");
        String bankerCode = request.getParameter("bankerCode");
        String depositor = request.getParameter("depositor");
        String projectCode = request.getParameter("projectCode");
        String term = request.getParameter("Semester");
        String BankAcct = request.getParameter("Bank");
        String others = request.getParameter("others");
        String batch = request.getParameter("batch");
        String program = request.getParameter("program");
        String Session = request.getParameter("Session");
        String courses = request.getParameter("courses");
        String menuPage = request.getParameter("menuPage");
        String customername = request.getParameter("customername");
        projectCode = (projectCode==null) ? "" : projectCode;
        String pymtPg = "";
        
        String drAcctNo = request.getParameter("drAcctNo");
        String drAcctType = request.getParameter("drAcctType");
        String drTranCode = request.getParameter("drTranCode");
        String branchcode = request.getParameter("branchcode");
        String drNarration = request.getParameter("drNarration");
        String drReference = request.getParameter("drReference");
        double drAmount = amountPaid;//Double.parseDouble(strDrAmount);
         String crAcctNo = request.getParameter("crAcctNo");
         String crAcctType = request.getParameter("crAcctType");
         String crTranCode = request.getParameter("crTranCode");
         String crNarration = request.getParameter("crNarration");
         double crAmount = amountPaid;//Double.parseDouble(strCrAmount);
         String effDate = request.getParameter("effDate");
        String companyCode = request.getParameter("companyCode");
        String familyid = request.getParameter("familyId");
        String memberNo = request.getParameter("MemberNo");
        String transactionDate = request.getParameter("transactionDate");
        familyid = familyid+"R";
        User user = null;
        if(session.getAttribute("CurrentUser") != null)
        {
            user = (User)session.getAttribute("CurrentUser");
        }
        int userId = Integer.parseInt(user.getUserId());  
        String UserId = user.getUserId();  
        if((branchcode==null)||(branchcode=="")){branchcode=user.getBranch();}        
        if(pymtType.equals("CHEQUE")){
            pymtPg = "OtherReceivableByCheque";
        }
        else if(pymtType.equals("CASH")){
            pymtPg = "OtherReceivableByCash";
        } 
        else{
            pymtPg = "salesOrderBankPymtDetail";
        }
        
        
         String addChequeBtn = request.getParameter("addChequeBtn");
         String addCashBtn = request.getParameter("addCashBtn");
        String addBankBtn = request.getParameter("addBankBtn");
//        String receiptNo = helper.getGenerateReceiptNo(branchcode);
        String BranchAcronym =htmlUtil.findObject("SELECT BRANCH_ACRONYM FROM MG_AD_Branch WHERE BRANCH_CODE = '"+branchcode+"'");
    //    String branchcode =htmlUtil.findObject("SELECT BRANCH_CODE FROM MG_AD_Branch WHERE BRANCH_ID = '"+school+"'");
        String CompanyAcronym =htmlUtil.findObject("SELECT ACRONYM FROM MG_gb_company WHERE COMPANY_CODE = '"+companyCode+"'");
//        System.out.println("receiptNo==== "+receiptNo);
 //       receiptNo = CompanyAcronym+"/"+BranchAcronym+"/"+receiptNo;        
 //       System.out.println("PaymentFees receiptNo=== "+receiptNo);
        String payername = customername;
         try{  
                 if(addChequeBtn != null){
                                if(serviceBus.createSalesPaymentReversal(orderCode,chequeNo,bankCode,amountOwing,amountPaid,userId,pymtType,accountNo,customerCode,bankerCode,depositor,
                                    tellerNo,projectCode,drAcctNo,drAcctType,drTranCode,drNarration,drAmount,effDate,crAcctNo,crAcctType,crTranCode,crNarration,crAmount,branchcode,companyCode,payername,menuPage,familyid,transactionDate)){
                                	Apprecord.updateCRBalances(crAcctNo,branchcode,crAmount);
                                	Apprecord.updateDRBalances(drAcctNo,branchcode,drAmount);
                                   pymtCode = serviceBus.pymtCode;
                                   out.print("<script>alert('Record Succesfully Saved.')</script>");
                                  // out.print("<script>window.document.location='DocumentHelp.jsp?np="+pymtPg+"&orderCode="+orderCode+"&pymtCode="+pymtCode+"'</script>");
                                   out.print("<script>window.document.location='DocumentHelp.jsp?np="+pymtPg+"&customername="+customername+"&batch="+batch+"&program="+program+"&Session="+Session+"&courses="+courses+"&Semester="+term+"&Narration="+drNarration+"&school="+branchcode+"&depositslip="+drReference+"&crNarration="+crNarration+"&Bank="+BankAcct+"&AmountPaid="+drAmount+"&others="+others+"&itemTypeCode="+pymtType+"&memberNo="+memberNo+"'</script>");
                                }
                           }
                        if(addCashBtn != null){
                                       if(serviceBus.createSalesPaymentReversal(orderCode,chequeNo,bankCode,amountOwing,amountPaid,userId,pymtType,accountNo,customerCode,bankerCode,depositor,
                                                                       tellerNo,projectCode,drAcctNo,drAcctType,drTranCode,drNarration,drAmount,effDate,crAcctNo,crAcctType,crTranCode,
                                                                       crNarration,crAmount,branchcode,companyCode,payername,menuPage,familyid,transactionDate)){
	                                       	Apprecord.updateCRBalances(crAcctNo,branchcode,crAmount);
	                                    	Apprecord.updateDRBalances(drAcctNo,branchcode,drAmount);
                                           pymtCode = serviceBus.pymtCode;
                                           out.print("<script>alert('Record Succesfully Saved.')</script>");
                                           //out.print("<script>window.document.location='DocumentHelp.jsp?np="+pymtPg+"&orderCode="+orderCode+"&pymtCode="+pymtCode+"'</script>");
                                           out.print("<script>window.document.location='DocumentHelp.jsp?np="+pymtPg+"&customername="+customername+"&batch="+batch+"&program="+program+"&Session="+Session+"&courses="+courses+"&Semester="+term+"&Narration="+drNarration+"&school="+branchcode+"&depositslip="+drReference+"&crNarration="+crNarration+"&Bank="+BankAcct+"&AmountPaid="+drAmount+"&others="+others+"&itemTypeCode="+pymtType+"&memberNo="+memberNo+"'</script>");
                                           
                                       }
                                  }
                        if(addBankBtn != null){
                                       if(serviceBus.createSalesPaymentReversal(orderCode,tellerNo,bankCode,amountOwing,amountPaid,userId,pymtType,accountNo,customerCode,bankerCode,depositor,
                                                                       tellerNo,projectCode,drAcctNo,drAcctType,drTranCode,drNarration,drAmount,effDate,crAcctNo,crAcctType,crTranCode,
                                                                       crNarration,crAmount,branchcode,companyCode,payername,menuPage,familyid,transactionDate)){
	                                       	Apprecord.updateCRBalances(crAcctNo,branchcode,crAmount);
	                                    	Apprecord.updateDRBalances(drAcctNo,branchcode,drAmount);                                    	   
                                           pymtCode = serviceBus.pymtCode;
                                           out.print("<script>alert('Record Succesfully Saved.')</script>");
                                         //  out.print("<script>window.document.location='DocumentHelp.jsp?np="+pymtPg+"&orderCode="+orderCode+"&pymtCode="+pymtCode+"'</script>");
                                           out.print("<script>window.document.location='DocumentHelp.jsp?np="+pymtPg+"&customername="+customername+"&batch="+batch+"&program="+program+"&Session="+Session+"&courses="+courses+"&Semester="+term+"&Narration="+drNarration+"&school="+branchcode+"&depositslip="+drReference+"&crNarration="+crNarration+"&Bank="+BankAcct+"&AmountPaid="+drAmount+"&others="+others+"&itemTypeCode="+pymtType+"&memberNo="+memberNo+"'</script>");
                                           
                                       }
                                  }
                    }
                    
              catch(NullPointerException e){
                   response.sendRedirect("sessionTimeOut.jsp");
                }
              catch(Exception e){
                  e.printStackTrace();
              }
              
    }
    
    public String getServletInfo() {
            return "AR Action Servlet";
    }
}
