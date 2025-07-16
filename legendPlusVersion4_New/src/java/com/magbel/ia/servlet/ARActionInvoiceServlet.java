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

public class ARActionInvoiceServlet extends HttpServlet {
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
        
        //String id = request.getParameter("ID");
        String pymtCode = "";//request.getParameter("pymtCode");
        String customerCode = request.getParameter("customerCode");
        String orderCode = request.getParameter("orderCode");
        String accountNo = request.getParameter("accountNo");
        String chequeNo = request.getParameter("chequeNo");
        String bankCode = request.getParameter("bankCode");
        String strAmountPaid = request.getParameter("amountPaid");
        strAmountPaid = strAmountPaid.replaceAll(",","");
        double amountPaid=Double.parseDouble(strAmountPaid);//(strAmountPaid!=null && !strAmountPaid.equals(""))?Double.parseDouble(strAmountPaid):0;
//        String strAmountOwing = request.getParameter("amountOwing");
//        strAmountOwing = strAmountOwing.replaceAll(",","");
//        double amountOwing = Double.parseDouble(strAmountOwing);//(strAmountOwing!=null && !strAmountOwing.equals(""))?Double.parseDouble(strAmountOwing):0;
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
        //String strDrAmount = request.getParameter("drAmount");
        //strDrAmount = strDrAmount.replaceAll(",","");
        double drAmount = amountPaid;//Double.parseDouble(strDrAmount);
         String crAcctNo = request.getParameter("crAcctNo");
         String crAcctType = request.getParameter("crAcctType");
         String crTranCode = request.getParameter("crTranCode");
         //String crSBU = request.getParameter("crSBU");
         String crNarration = request.getParameter("crNarration");
         //String strCrAmount = request.getParameter("crAmount");
         //strCrAmount = strCrAmount.replaceAll(",","");
         double crAmount = amountPaid;//Double.parseDouble(strCrAmount);
         String effDate = request.getParameter("effDate");
         String transactionDate = request.getParameter("transactionDate");
        String companyCode = request.getParameter("companyCode");
        User user = null;
        if(session.getAttribute("CurrentUser") != null)
        {
            user = (User)session.getAttribute("CurrentUser");
        }
       // System.out.println("user==== "+user);
//        System.out.println("addApplicationFormBtn==== "+addApplicationFormBtn);
        int userId = Integer.parseInt(user.getUserId());  
        String UserId = user.getUserId();  
        if((branchcode==null)||(branchcode=="")){branchcode=user.getBranch();}        
        if(pymtType.equals("CHEQUE")){
        //    pymtPg = "OtherReceivableByCheque";
            pymtPg = "SearchPaymentOtherReceivableChqList";
        }
        else if(pymtType.equals("CASH")){
           // pymtPg = "OtherReceivableByCash";
            pymtPg = "SearchPaymentOtherReceivableCashList";
        } 
        else{
         //   pymtPg = "OtherReceivableByTransfer";
            pymtPg = "SearchPaymentOtherReceivableTransferList";
            
        }
        
        
         String addChequeBtn = request.getParameter("addChequeBtn");
         String addCashBtn = request.getParameter("addCashBtn");
        String addBankBtn = request.getParameter("addBankBtn");
        String receiptNo = helper.getGenerateReceiptNo(branchcode);
        String BranchAcronym =htmlUtil.findObject("SELECT BRANCH_ACRONYM FROM MG_AD_Branch WHERE BRANCH_CODE = '"+branchcode+"'");
    //    String branchcode =htmlUtil.findObject("SELECT BRANCH_CODE FROM MG_AD_Branch WHERE BRANCH_ID = '"+school+"'");
        String CompanyAcronym =htmlUtil.findObject("SELECT ACRONYM FROM MG_gb_company WHERE COMPANY_CODE = '"+companyCode+"'");
//        System.out.println("receiptNo==== "+receiptNo);
        receiptNo = CompanyAcronym+"/"+BranchAcronym+"/"+receiptNo;        
        String familyid = Apprecord.getId("IA_FAMILY_ID"); 
//        System.out.println("PaymentFees receiptNo=== "+receiptNo);
        String payername = customername;
//        String custcode = serviceBus.getCodeName("SELECT CUSTOMER_NAME FROM IA_CUSTOMER WHERE CUSTOMER_CODE='"+customerCode+"'");
//        System.out.println("Customer Code when Blank: "+customername);
//        if(custcode.equalsIgnoreCase("")){payername = customername;}
//        crNarration = Studentname+" "+receiptNo+" "+courses;
//        drNarration = Studentname+" "+receiptNo+" "+courses;
        
//        com.magbel.ia.vao.User user = null;
         try{
//               if(session.getAttribute("CurrentUser")!=null){user =(com.magbel.ia.vao.User)session.getAttribute("CurrentUser");}
//               int userId = Integer.parseInt(user.getUserId());//to be completed
                         
                 if(addChequeBtn != null){
                                if(serviceBus.createSalesPayment(orderCode,chequeNo,bankCode,amountOwing,amountPaid,userId,pymtType,accountNo,customerCode,bankerCode,depositor,
                                    tellerNo,projectCode,drAcctNo,drAcctType,drTranCode,drNarration,drAmount,effDate,crAcctNo,crAcctType,crTranCode,crNarration,crAmount,branchcode,companyCode,payername,menuPage,transactionDate)){
                                	Apprecord.updateCRBalances(drAcctNo,branchcode,drAmount);
                                	Apprecord.updateDRBalances(crAcctNo,branchcode,crAmount);
                                   pymtCode = serviceBus.pymtCode;
                                   out.print("<script>alert('Record Succesfully Saved.')</script>");
                                  // out.print("<script>window.document.location='DocumentHelp.jsp?np="+pymtPg+"&orderCode="+orderCode+"&pymtCode="+pymtCode+"'</script>");
                                   out.print("<script>window.document.location='DocumentHelp.jsp?np="+pymtPg+"&customername="+customername+"&batch="+batch+"&program="+program+"&Session="+Session+"&courses="+courses+"&Semester="+term+"&Narration="+drNarration+"&school="+branchcode+"&depositslip="+drReference+"&crNarration="+crNarration+"&Bank="+BankAcct+"&AmountPaid="+drAmount+"&others="+others+"&itemTypeCode="+pymtType+"&receiptNo="+receiptNo+"'</script>");
                                }
                           }
                        if(addCashBtn != null){
                                       if(serviceBus.createSalesPayment(orderCode,chequeNo,bankCode,amountOwing,amountPaid,userId,pymtType,accountNo,customerCode,bankerCode,depositor,
                                                                       tellerNo,projectCode,drAcctNo,drAcctType,drTranCode,drNarration,drAmount,effDate,crAcctNo,crAcctType,crTranCode,
                                                                       crNarration,crAmount,branchcode,companyCode,payername,menuPage,transactionDate)){
	                                       	Apprecord.updateCRBalances(drAcctNo,branchcode,drAmount);
	                                    	Apprecord.updateDRBalances(crAcctNo,branchcode,crAmount);
                                           pymtCode = serviceBus.pymtCode;
                                           out.print("<script>alert('Record Succesfully Saved.')</script>");
                                           //out.print("<script>window.document.location='DocumentHelp.jsp?np="+pymtPg+"&orderCode="+orderCode+"&pymtCode="+pymtCode+"'</script>");
                                           out.print("<script>window.document.location='DocumentHelp.jsp?np="+pymtPg+"&customername="+customername+"&batch="+batch+"&program="+program+"&Session="+Session+"&courses="+courses+"&Semester="+term+"&Narration="+drNarration+"&school="+branchcode+"&depositslip="+drReference+"&crNarration="+crNarration+"&Bank="+BankAcct+"&AmountPaid="+drAmount+"&others="+others+"&itemTypeCode="+pymtType+"&receiptNo="+receiptNo+"'</script>");
                                           
                                       }
                                  }
                        if(addBankBtn != null){
                                       if(serviceBus.createSalesPayment(orderCode,tellerNo,bankCode,amountOwing,amountPaid,userId,pymtType,accountNo,customerCode,bankerCode,depositor,
                                                                       tellerNo,projectCode,drAcctNo,drAcctType,drTranCode,drNarration,drAmount,effDate,crAcctNo,crAcctType,crTranCode,
                                                                       crNarration,crAmount,branchcode,companyCode,payername,menuPage,transactionDate)){
	                                       	Apprecord.updateCRBalances(drAcctNo,branchcode,drAmount);
	                                    	Apprecord.updateDRBalances(crAcctNo,branchcode,crAmount);                                    	   
                                           pymtCode = serviceBus.pymtCode;
                                           out.print("<script>alert('Record Succesfully Saved.')</script>");
                                         //  out.print("<script>window.document.location='DocumentHelp.jsp?np="+pymtPg+"&orderCode="+orderCode+"&pymtCode="+pymtCode+"'</script>");
                                           out.print("<script>window.document.location='DocumentHelp.jsp?np="+pymtPg+"&customername="+customername+"&batch="+batch+"&program="+program+"&Session="+Session+"&courses="+courses+"&Semester="+term+"&Narration="+drNarration+"&school="+branchcode+"&depositslip="+drReference+"&crNarration="+crNarration+"&Bank="+BankAcct+"&AmountPaid="+drAmount+"&others="+others+"&itemTypeCode="+pymtType+"&receiptNo="+receiptNo+"'</script>");
                                           
                                       }
                                  }
                             /*  }
                               
                             } */    
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
