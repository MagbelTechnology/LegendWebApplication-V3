package com.magbel.ia.servlet;

//import com.magbel.ia.bus.SalesOrderServiceBus;

import com.magbel.ia.bus.APServiceBus;
import com.magbel.ia.bus.ApprovalRecords;
import com.magbel.util.HtmlUtilily;
import  com.magbel.ia.bus.InvoiceServiceBus;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.*;
import javax.servlet.http.*;

public class APVoucherActionReversalServlet extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html; charset=windows-1252";
    private APServiceBus serviceBus;
    ApprovalRecords Apprecord;
    HtmlUtilily htmlUtil;
    InvoiceServiceBus ais;
    
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        serviceBus = new APServiceBus();
        Apprecord = new ApprovalRecords();
        htmlUtil = new HtmlUtilily();
        ais = new InvoiceServiceBus();
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
        orderCode = orderCode+"R";
        String accountNo = request.getParameter("accountNo");
//        System.out.println("accountNo=== "+accountNo);
        String chequeNo = request.getParameter("chequeNo");
        String bankCode = request.getParameter("bankCode");
        String invoiceNo = request.getParameter("invoiceNo");        
 //       System.out.println("bankCode=== "+bankCode);
        String strAmountPaid = request.getParameter("Amount");
        strAmountPaid = strAmountPaid.replaceAll(",","");
        double amountPaid=Double.parseDouble(strAmountPaid);//(strAmountPaid!=null && !strAmountPaid.equals(""))?Double.parseDouble(strAmountPaid):0;
        String strAmountOwing = request.getParameter("Amount");
        strAmountOwing = strAmountOwing.replaceAll(",","");
        double amountOwing = Double.parseDouble(strAmountOwing);//(strAmountOwing!=null && !strAmountOwing.equals(""))?Double.parseDouble(strAmountOwing):0;
        String tellerNo = request.getParameter("tellerNo");
        String pymtType = request.getParameter("pymtType");
        String bankerCode = request.getParameter("bankerCode");
        String depositor = request.getParameter("depositor");
        String projectCode = request.getParameter("projectCode");
        projectCode = (projectCode==null) ? "" : projectCode;
        String pymtPg = "";
        String orderNo = request.getParameter("orderNo");
        orderNo = orderNo+"R";
        String drAcctNo = request.getParameter("drAcctNo");
//        System.out.println("drAcctNo=== "+drAcctNo);
        String branchCode = request.getParameter("branchCode");
        String drAcctType = request.getParameter("drAcctType");
        String drTranCode = request.getParameter("drTranCode");
       // String drSBU = request.getParameter("drSBU");
        String drNarration = request.getParameter("drNarration");
        //String strDrAmount = request.getParameter("drAmount");
        //strDrAmount = strDrAmount.replaceAll(",","");
        double drAmount = amountPaid;//Double.parseDouble(strDrAmount);
        String description = request.getParameter("description");
        String crAcctNo = request.getParameter("crAcctNo");
        String customerAcct = request.getParameter("customerAcct");
//         String crAcctNo = htmlUtil.getCodeName("SELECT ACCOUNT_NO FROM IA_BANK WHERE STATUS = 'ACTIVE' AND ACRONYM = '"+bankCode+"'");         
 //        System.out.println("crAcctNo=== "+crAcctNo);
         String crAcctType = request.getParameter("crAcctType");
         String crTranCode = request.getParameter("crTranCode");
         //String crSBU = request.getParameter("crSBU");
         String crNarration = request.getParameter("crNarration");
         //String strCrAmount = request.getParameter("crAmount");
         //strCrAmount = strCrAmount.replaceAll(",","");
         double crAmount = amountPaid;//Double.parseDouble(strCrAmount);
         String transactionDate = request.getParameter("transactionDate");
		 String companyCode = request.getParameter("companyCode");
//		 System.out.println("effDate=== "+effDate);
//		 System.out.println("companyCode=== "+companyCode);
		 String familyid = Apprecord.getId("IA_FAMILY_ID");
		 String pagetype = request.getParameter("pagetype");
//		 System.out.println("pagetype=== "+pagetype);
         drNarration = description+" "+invoiceNo;
         crNarration = description+" "+invoiceNo;
	 if(!pagetype.equalsIgnoreCase("pettycash")){
//		 System.out.println("Payment Voucher=== "+pagetype);
        if(pymtType.equals("CHEQUE")){
            //pymtPg = "purchaseOrderChequePymtDetail";
            pymtPg = "invoiceVoucherItemList";
        }
        else if(pymtType.equals("CASH")){
            //pymtPg = "purchaseOrderCashPymtDetail";
            pymtPg = "invoiceVoucherItemList";
        }
        else{
            //pymtPg = "purchaseOrderBankPymtDetail";
            pymtPg = "invoiceVoucherItemList";
        }
    }
        else{ 
   		 System.out.println("Pety cash Voucher=== "+pagetype);  
//   		crAcctNo = request.getParameter("crAcctNo");
        if(pymtType.equals("CHEQUE")){
            //pymtPg = "purchaseOrderChequePymtDetail";
            pymtPg = "PettyCashItemList";
        }
        else if(pymtType.equals("CASH")){
            //pymtPg = "purchaseOrderCashPymtDetail";
            pymtPg = "PettyCashItemList";
        }
        else{
            //pymtPg = "purchaseOrderBankPymtDetail";
            pymtPg = "PettyCashItemList";
        }   
        }
        String filter = " AND INVOICE_NO ='"+invoiceNo+"'";
        //java.util.ArrayList list = ais.findInvoiceItemByQuery(filter);
        java.util.ArrayList list = null;
//        System.out.println("APActionServlet pagetype=== "+pagetype);
        if(!pagetype.equalsIgnoreCase("pettycash")){
        	list = ais.findVoucherItemByQuery(filter);
        }else{
        	list = ais.findPettyCashItemByQuery(filter);
        }
        int noofrows = list.size();
        boolean done3 = false;
 //       System.out.println("noofrows=== "+noofrows+" orderCode: "+orderCode);
         String reversalBtn = request.getParameter("reversalBtn");
         String addCashBtn = request.getParameter("addCashBtn");
        String addBankBtn = request.getParameter("addBankBtn");
    	String  balAmount = htmlUtil.getCodeName("SELECT ACCOUNT_BALANCE FROM IA_CUSTOMER_ACCOUNT_DISPLAY WHERE STATUS = 'ACTIVE' AND ACCOUNT_NO = '"+customerAcct+"'");
    	balAmount = balAmount.replace(",","");
		double  accountBalance = ((balAmount != null)&&(!balAmount.equals(""))) ? Double.parseDouble(balAmount) : 0;
        
        com.magbel.ia.vao.User user = null;
         try{
               if(session.getAttribute("CurrentUser")!=null){user =(com.magbel.ia.vao.User)session.getAttribute("CurrentUser");}
               int userId = Integer.parseInt(user.getUserId());//to be completed
                        
         if(reversalBtn != null){
                        if(serviceBus.createVoucherPayment(orderCode,chequeNo,bankCode,amountOwing,amountPaid,userId,pymtType,accountNo,
                                                            customerCode,bankerCode,depositor,tellerNo,projectCode,drAcctNo,drAcctType,crTranCode,drNarration,
                                                            drAmount,transactionDate,crAcctNo,crAcctType,drTranCode,crNarration,crAmount,companyCode,familyid,branchCode,invoiceNo,customerAcct,accountBalance,orderNo)){
                        	boolean done1 = Apprecord.updateCRBalances(drAcctNo,branchCode,drAmount);
                        	if(done1 == true){Apprecord.updateCustomerCRBalances(customerAcct,branchCode,drAmount,accountBalance);}
                        	if(done1==true){
                        	Apprecord.updateDRBalances(crAcctNo,branchCode,crAmount);                                	
                        	if(!pagetype.equalsIgnoreCase("pettycash")){
                        		Apprecord.updatePVSUMMARY(invoiceNo,orderNo);
                        	}
                        	else{                             	
                        	Apprecord.updatePCSUMMARY(invoiceNo,orderNo);
                        	}
                        	}
                        	for(int i=0; i<noofrows; i++) {	
                        		com.magbel.ia.vao.SalesInvoiceItem ta=(com.magbel.ia.vao.SalesInvoiceItem)list.get(i);
                        		String invoiceno = ta.getInvoiceNo();
                        		String crItemdescription = ta.getItemDescription();
                        		String drItemdescription = ta.getItemDescription();
                        		String creditAccount = ta.getAccountNo();
                        		String SchoolCode = ta.getBranchCode();
                        		String itemCompanyCode = ta.getCompanyCode();
                        		double itemAmount = ta.getAmount();
                        		double critemAmount = ta.getAmount();
                        		double dritemAmount = ta.getAmount();
                        		boolean done4 = serviceBus.createVoucherPayment(orderCode,chequeNo,bankCode,itemAmount,amountPaid,userId,pymtType,accountNo,
                                        customerCode,bankerCode,depositor,tellerNo,projectCode,drAcctNo,drAcctType,drTranCode,drItemdescription,
                                        dritemAmount,transactionDate,creditAccount,crAcctType,crTranCode,crItemdescription,critemAmount,itemCompanyCode,familyid,SchoolCode,invoiceNo,"",accountBalance,orderNo);
                        		if(done4 == true){done3 = Apprecord.updateCRBalances(drAcctNo,SchoolCode,dritemAmount);}
                            	if(done3==true){Apprecord.updateDRBalances(creditAccount,SchoolCode,critemAmount);}
                        	//	System.out.println("invoiceno=== "+invoiceno+" debitAccount: "+debitAccount);
                        	}                                	
                           pymtCode = serviceBus.pymtCode;
                           out.print("<script>alert('Reversal Record Succesfully Saved.')</script>");
                            out.print("<script>window.document.location='DocumentHelp.jsp?np="+pymtPg+"&orderCode="+orderCode+"&pymtCode="+pymtCode+"'</script>");
                            
                        }
                           }
                        if(addCashBtn != null){
                                       if(serviceBus.createPurchasePayment(orderCode,chequeNo,bankCode,amountOwing,amountPaid,userId,pymtType,
                                                                      accountNo,customerCode,bankerCode,depositor,tellerNo,projectCode,drAcctNo,drAcctType,drTranCode,
                                                                      drNarration,drAmount,transactionDate,crAcctNo,crAcctType,crTranCode,crNarration,crAmount,companyCode,familyid)){
                                           pymtCode = serviceBus.pymtCode;
                                           out.print("<script>alert('Reversal Record Succesfully Saved.')</script>");
                                           out.print("<script>window.document.location='DocumentHelp.jsp?np="+pymtPg+"&orderCode="+orderCode+"&pymtCode="+pymtCode+"'</script>");
                                           
                                       }
                                  }
                          /*     }
                               
                             }*/
                        if(addBankBtn != null)
						{
                                       if(serviceBus.createPurchasePayment(orderCode,chequeNo,bankCode,amountOwing,amountPaid,userId,pymtType,accountNo,
                                                                 customerCode,bankerCode,depositor,tellerNo,projectCode,drAcctNo,drAcctType,drTranCode,drNarration,drAmount,
                                                                 transactionDate,crAcctNo,crAcctType,crTranCode,crNarration,crAmount,companyCode,familyid)){
                                           pymtCode = serviceBus.pymtCode;
                                           out.print("<script>alert('Reversal Record Succesfully Saved.')</script>");
                                           out.print("<script>window.document.location='DocumentHelp.jsp?np="+pymtPg+"&orderCode="+orderCode+"&pymtCode="+pymtCode+"'</script>");
                                           
                                       }
                                  }
                    }
                    
              catch(NullPointerException e){
                   response.sendRedirect("sessionTimedOut.jsp");
                }
              catch(Exception e){
                  e.printStackTrace();
              }
              
    }
    
    public String getServletInfo() {
            return "AP Action Servlet";
    }
}
