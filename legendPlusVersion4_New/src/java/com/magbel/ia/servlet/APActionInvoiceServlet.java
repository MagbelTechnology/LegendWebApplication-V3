package com.magbel.ia.servlet;

//import com.magbel.ia.bus.SalesOrderServiceBus;

import com.magbel.ia.bus.APServiceBus;
import com.magbel.ia.bus.AccountInterfaceServiceBus;
import com.magbel.ia.bus.ApprovalRecords;
import com.magbel.util.HtmlUtilily;
import  com.magbel.ia.bus.InvoiceServiceBus;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.*;
import javax.servlet.http.*;
  
public class APActionInvoiceServlet extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html; charset=windows-1252";
    private APServiceBus serviceBus;
    private AccountInterfaceServiceBus acctserviceBus;
    ApprovalRecords Apprecord;
    HtmlUtilily htmlUtil;
    InvoiceServiceBus ais;
    
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        serviceBus = new APServiceBus();
        Apprecord = new ApprovalRecords();
        htmlUtil = new HtmlUtilily();
        ais = new InvoiceServiceBus();
        acctserviceBus = new AccountInterfaceServiceBus();
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
        String customerAccount = request.getParameter("customerAccount");
        String customerName = request.getParameter("customerName");
//        System.out.println("customerName=== "+customerName+"   customerCode: "+customerCode);
        String customerAccountNo = htmlUtil.getCodeName("SELECT ACCOUNT_NO FROM IA_CUSTOMER_ACCOUNT_DISPLAY WHERE CUSTOMER_CODE = '"+customerAccount+"'");
        String drAcctNo = request.getParameter("drAcctNo");
        String crAcctNo = request.getParameter("crAcctNo");
//        System.out.println("drAcctNo=== "+drAcctNo+"   crAcctNo: "+crAcctNo);
        String branchCode = request.getParameter("branchCode");
        String drAcctType = request.getParameter("drAcctType");
        String drTranCode = request.getParameter("drTranCode");
        
        String drNarration = request.getParameter("drNarration");
        //String strDrAmount = request.getParameter("drAmount");
        //strDrAmount = strDrAmount.replaceAll(",","");
        double drAmount = amountPaid;//Double.parseDouble(strDrAmount);
        String description = request.getParameter("description");
 //        String crAcctNo = htmlUtil.getCodeName("SELECT ACCOUNT_NO FROM IA_BANK WHERE STATUS = 'ACTIVE' AND ACRONYM = '"+bankCode+"'");         
 //        System.out.println("crAcctNo=== "+crAcctNo);
         String crAcctType = request.getParameter("crAcctType");
         String crTranCode = request.getParameter("crTranCode");
         //String crSBU = request.getParameter("crSBU");
         String crNarration = request.getParameter("crNarration");
         //String strCrAmount = request.getParameter("crAmount");
         //strCrAmount = strCrAmount.replaceAll(",","");
         double crAmount = amountPaid;//Double.parseDouble(strCrAmount);
         String effDate = request.getParameter("effDate");
		 String companyCode = request.getParameter("companyCode");
//		 System.out.println("effDate=== "+effDate);
//		 System.out.println("companyCode=== "+companyCode);
		 String familyid = Apprecord.getId("IA_FAMILY_ID");
		 String pagetype = request.getParameter("pagetype");
		 String ledgerNo = request.getParameter("ledgerNo");
		 String branchNo = htmlUtil.getCodeName("SELECT BRANCH_ID FROM MG_AD_Branch WHERE BRANCH_CODE = '"+branchCode+"'");
//		 System.out.println("pagetype=== "+pagetype+"    ledgerNo: "+ledgerNo+"    accountNo: "+accountNo+"    drAcctNo: "+drAcctNo+"    crAcctNo: "+crAcctNo);
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
        else if(pymtType.equals("TRANSFER")){
            //pymtPg = "purchaseOrderCashPymtDetail";
            pymtPg = "invoiceVoucherItemList";
        }        
        else{
            //pymtPg = "purchaseOrderBankPymtDetail";
            pymtPg = "invoiceVoucherItemList";
        }
    }
        else{ 
//   		 System.out.println("Pety cash Voucher=== "+pagetype);  
   		crAcctNo = request.getParameter("crAcctNo");
        if(pymtType.equals("CHEQUE")){
            //pymtPg = "purchaseOrderChequePymtDetail";
            pymtPg = "PettyCashItemList";
        }
        else if(pymtType.equals("CASH")){
            //pymtPg = "purchaseOrderCashPymtDetail";
            pymtPg = "PettyCashItemList";
        }
        else if(pymtType.equals("TRANSFER")){
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
//        System.out.println("APActionInvoiceServlet pagetype=== "+pagetype);
        if(!pagetype.equalsIgnoreCase("pettycash")){
        	list = ais.findInvoiceItemByQuery(filter);
        }else{
        	list = ais.findPettyCashItemByQuery(filter);
        }  
        int noofrows = list.size();
        boolean done3 = false;
 //       System.out.println("noofrows=== "+noofrows+" orderCode: "+orderCode);
         String addChequeBtn = request.getParameter("addChequeBtn");
         String addCashBtn = request.getParameter("addCashBtn");
        String addBankBtn = request.getParameter("addBankBtn");
        com.magbel.ia.vao.User user = null;
//        System.out.println("APActionInvoiceServlet addChequeBtn=== "+addChequeBtn);
         try{
               if(session.getAttribute("CurrentUser")!=null){user =(com.magbel.ia.vao.User)session.getAttribute("CurrentUser");}
               int userId = Integer.parseInt(user.getUserId());//to be completed
                        
                 if(addChequeBtn != null){
                                if(serviceBus.createVoucherPayment(orderCode,chequeNo,bankCode,amountOwing,amountPaid,userId,pymtType,accountNo,
                                                                    customerCode,bankerCode,depositor,tellerNo,projectCode,drAcctNo,drAcctType,drTranCode,drNarration,
                                                                    drAmount,effDate,crAcctNo,crAcctType,crTranCode,crNarration,crAmount,companyCode,familyid,branchCode,invoiceNo,"",0,"")){
                                	boolean done1 = Apprecord.updateCRBalances(crAcctNo,branchCode,crAmount);
                                	if(done1==true){
//                                		System.out.println("done1=== "+done1);
//                                		System.out.println("<<<<<<<< About to Update Customer Record 1 >>>>>>>>>>>>>");
                                	Apprecord.updateDRBalances(drAcctNo,branchCode,drAmount);  
   //                             	System.out.println("<<<<<<<< Customer Account No >>>>>>>>>>>>>: "+customerAccount+"   branchNo: "+branchNo+"     crAmount: "+crAmount);
   //                             	acctserviceBus.updateStudentCRBalances(customerAccountNo, Integer.parseInt(branchNo), crAmount);
                                	if(!pagetype.equalsIgnoreCase("pettycash")){
//                                		System.out.println("pagetype=== "+pagetype);
//                                		System.out.println("invoiceNo=== "+invoiceNo);
                                		Apprecord.updatePVSUMMARY(invoiceNo);
                                	}
                                	else{ 
 //                               		System.out.println("pagetype=== "+pagetype);
 //                               		System.out.println("invoiceNo=== "+invoiceNo);                                	
                                	Apprecord.updatePCSUMMARY(invoiceNo);
                                	}
                                	}
                                	for(int i=0; i<noofrows; i++) {	
                                		com.magbel.ia.vao.PettyInvoiceItem ta=(com.magbel.ia.vao.PettyInvoiceItem)list.get(i);
                                		String invoiceno = ta.getInvoiceNo();
//                                		System.out.println("invoiceno=== "+invoiceno+" for: "+i);
                                		String crItemdescription = ta.getItemDescription();
                                		String drItemdescription = ta.getItemDescription();
                                		String debitAccount = ta.getAccountNo();
                                		String SchoolCode = ta.getBranchCode();
                                		String itemCompanyCode = ta.getCompanyCode();
                                		double itemAmount = ta.getAmount();
                                		double critemAmount = ta.getAmount();
                                		double dritemAmount = ta.getAmount();  
                                		boolean done4 = serviceBus.createVoucherPayment(orderCode,chequeNo,bankCode,itemAmount,amountPaid,userId,pymtType,accountNo,
                                                customerCode,bankerCode,depositor,tellerNo,projectCode,debitAccount,drAcctType,drTranCode,drItemdescription,
                                                dritemAmount,effDate,crAcctNo,crAcctType,crTranCode,crItemdescription,critemAmount,itemCompanyCode,familyid,SchoolCode,invoiceNo,"",0,"");
                                		if(done4 == true){done3 = Apprecord.updateCRBalances(crAcctNo,SchoolCode,critemAmount);}
                                    	if(done3==true){
 //                                   		System.out.println("<<<<<<<< About to Update Customer Record 2 >>>>>>>>>>>>>");
                                    		Apprecord.updateDRBalances(debitAccount,SchoolCode,dritemAmount);
                                    		acctserviceBus.updateStudentCRBalances(customerAccountNo, Integer.parseInt(branchNo), itemAmount);
                                    	}
                                	//	System.out.println("invoiceno=== "+invoiceno+" debitAccount: "+debitAccount);
                                	}                                	
                                   pymtCode = serviceBus.pymtCode;
                                   out.print("<script>alert('Record Succesfully Saved.')</script>");
                                    out.print("<script>window.document.location='DocumentHelp.jsp?np="+pymtPg+"&orderCode="+orderCode+"&pymtCode="+pymtCode+"'</script>");
                                    
                                }
                           }
                     /*   }  
                        
                      }*/
                        if(addCashBtn != null){
                                       if(serviceBus.createPurchasePayment(orderCode,chequeNo,bankCode,amountOwing,amountPaid,userId,pymtType,
                                                                      accountNo,customerCode,bankerCode,depositor,tellerNo,projectCode,drAcctNo,drAcctType,drTranCode,
                                                                      drNarration,drAmount,effDate,crAcctNo,crAcctType,crTranCode,crNarration,crAmount,companyCode,familyid)){
                                           pymtCode = serviceBus.pymtCode;
                                           out.print("<script>alert('Record Succesfully Saved.')</script>");
                                           out.print("<script>window.document.location='DocumentHelp.jsp?np="+pymtPg+"&orderCode="+orderCode+"&pymtCode="+pymtCode+"'</script>");
                                           
                                       }
                                  }
                          /*     }
                               
                             }*/
                        if(addBankBtn != null)
						{
                                       if(serviceBus.createPurchasePayment(orderCode,chequeNo,bankCode,amountOwing,amountPaid,userId,pymtType,accountNo,
                                                                 customerCode,bankerCode,depositor,tellerNo,projectCode,drAcctNo,drAcctType,drTranCode,drNarration,drAmount,
                                                                 effDate,crAcctNo,crAcctType,crTranCode,crNarration,crAmount,companyCode,familyid)){
                                           pymtCode = serviceBus.pymtCode;
                                           out.print("<script>alert('Record Succesfully Saved.')</script>");
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
