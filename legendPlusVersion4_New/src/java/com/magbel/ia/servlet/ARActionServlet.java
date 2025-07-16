package com.magbel.ia.servlet;

//import com.magbel.ia.bus.SalesOrderServiceBus;

import com.magbel.ia.bus.ARServiceBus;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.*;
import javax.servlet.http.*;

public class ARActionServlet extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html; charset=windows-1252";
    private ARServiceBus serviceBus;
    
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        serviceBus = new ARServiceBus();
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
        
        String strAmount = request.getParameter("amount");
        strAmount = strAmount.replaceAll(",","");
        double amount=Double.parseDouble(strAmount);//(strAmount!=null && !strAmount.equals(""))?Double.parseDouble(strAmount):0;
        String strAmountPaid = request.getParameter("amountPaid");
        strAmountPaid = strAmountPaid.replaceAll(",","");
        double amountPaid=Double.parseDouble(strAmountPaid);//(strAmountPaid!=null && !strAmountPaid.equals(""))?Double.parseDouble(strAmountPaid):0;
        String strAmountOwing = request.getParameter("amountOwing");
        strAmountOwing = strAmountOwing.replaceAll(",","");
        double amountOwing = Double.parseDouble(strAmountOwing);//(strAmountOwing!=null && !strAmountOwing.equals(""))?Double.parseDouble(strAmountOwing):0;
        String tellerNo = request.getParameter("tellerNo");
        String pymtType = request.getParameter("pymtType");
        String bankerCode = request.getParameter("bankerCode");
        String depositor = request.getParameter("depositor");
        String projectCode = request.getParameter("projectCode");
        projectCode = (projectCode==null) ? "" : projectCode;
        String pymtPg = "";
        String payername = "";
        String custcode = serviceBus.getCodeName("SELECT CUSTOMER_NAME FROM IA_CUSTOMER WHERE CUSTOMER_CODE='"+customerCode+"'");
        System.out.println("Customer Code when Blank: "+custcode);
        if(custcode==""){payername = customerCode;}
        //Debit
        // String drBranch = request.getParameter("drBranch");
        // String intDrCurrCode = request.getParameter("drCurrCode");
        // intDrCurrCode = serviceBus.getCodeName("SELECT CURRENCY_ID FROM IA_GB_CURRENCY_CODE WHERE ISO_CODE='"+intDrCurrCode+"'");
        //intDrCurrCode = ((intDrCurrCode!=null) && (!intDrCurrCode.equals("")))?intDrCurrCode:"0";
        // int drCurrCode = Integer.parseInt(intDrCurrCode);
        String drAcctNo = request.getParameter("drAcctNo");
        String drAcctType = request.getParameter("drAcctType");
        String drTranCode = request.getParameter("drTranCode");
        // String drSBU = request.getParameter("drSBU");
        String drNarration = request.getParameter("drNarration");
        //String strDrAmount = request.getParameter("drAmount");
        //strDrAmount = strDrAmount.replaceAll(",","");
        double drAmount = amountPaid;//Double.parseDouble(strDrAmount);
        //String strDrAcctExchRate = request.getParameter("drExchRate");
        //double drAcctExchRate = Double.parseDouble(strDrAcctExchRate);
        //String strDrSysExchRate = "0";//request.getParameter("drSysExchRate");
        //double drSysExchRate = Double.parseDouble(strDrSysExchRate);
        
        //Credit
         //String crBranch = request.getParameter("crBranch");
         //String intCrCurrCode = request.getParameter("crCurrCode");
         //intCrCurrCode = serviceBus.getCodeName("SELECT CURRENCY_ID FROM IA_GB_CURRENCY_CODE WHERE ISO_CODE='"+intCrCurrCode+"'");
         //intCrCurrCode = ((intCrCurrCode!=null) && (!intCrCurrCode.equals("")))?intCrCurrCode:"0";
         //int crCurrCode = Integer.parseInt(intCrCurrCode);
         String crAcctNo = request.getParameter("crAcctNo");
         String crAcctType = request.getParameter("crAcctType");
         String crTranCode = request.getParameter("crTranCode");
         //String crSBU = request.getParameter("crSBU");
         String crNarration = request.getParameter("crNarration");
         //String strCrAmount = request.getParameter("crAmount");
         //strCrAmount = strCrAmount.replaceAll(",","");
         double crAmount = amountPaid;//Double.parseDouble(strCrAmount);
         //String strCrAcctExchRate = request.getParameter("crExchRate");
         //double crAcctExchRate = Double.parseDouble(strCrAcctExchRate);
         //String strCrSysExchRate = "0";//request.getParameter("crSysExchRate");
         //double crSysExchRate = Double.parseDouble(strCrSysExchRate);
         String effDate = request.getParameter("effDate");
        String companyCode = request.getParameter("companyCode");
        
        if(pymtType.equals("CHEQUE")){
            pymtPg = "salesOrderChequePymtDetail";
        }
        else if(pymtType.equals("CASH")){
            pymtPg = "salesOrderCashPymtDetail";
        }
        else{
            pymtPg = "salesOrderBankPymtDetail";
        }
        
        
         String addChequeBtn = request.getParameter("addChequeBtn");
         String addCashBtn = request.getParameter("addCashBtn");
        String addBankBtn = request.getParameter("addBankBtn");
        com.magbel.ia.vao.User user = null;
         try{
               if(session.getAttribute("CurrentUser")!=null){user =(com.magbel.ia.vao.User)session.getAttribute("CurrentUser");}
               int userId = Integer.parseInt(user.getUserId());//to be completed
                String branchId = user.getBranch();
                String branchcode = serviceBus.getCodeName("SELECT BRANCH_CODE FROM MG_AD_Branch WHERE BRANCH_ID='"+branchId+"'");
                 if(addChequeBtn != null){
                    /*    if(pymtCode.equals("")||pymtCode == null){
                            out.print("<script>alert('Payment no. cannot be empty')</script>");
                            out.print("<script>history.go(-1);</script>");
                        }
                        else{
                            if(serviceBus.isRecordExisting("SELECT COUNT(PAYMENT_CODE) FROM IA_SALES_PAYMENTS WHERE PAYMENT_CODE = '"+pymtCode+"' AND SORDER_CODE='"+orderCode+"'")){
                                out.print("<script>alert('Payment Already Exist')</script>");
                                out.print("<script>history.go(-1);</script>");
                            }
                            else{*/
                                if(serviceBus.createSalesPayment(orderCode,chequeNo,bankCode,amountOwing,amountPaid,userId,pymtType,accountNo,customerCode,bankerCode,depositor,
                                    tellerNo,projectCode,drAcctNo,drAcctType,drTranCode,drNarration,drAmount,effDate,crAcctNo,crAcctType,crTranCode,crNarration,crAmount,companyCode,payername,branchcode,amount)){
                                   pymtCode = serviceBus.pymtCode;
                                   out.print("<script>alert('Record Succesfully Saved.')</script>");
                                   out.print("<script>window.document.location='DocumentHelp.jsp?np="+pymtPg+"&orderCode="+orderCode+"&pymtCode="+pymtCode+"'</script>");
                                    
                                }
                           }
                     /*   }
                        
                      }*/
                        if(addCashBtn != null){
                              /* if(pymtCode.equals("")||pymtCode == null){
                                   out.print("<script>alert('Payment no. cannot be empty')</script>");
                                   out.print("<script>history.go(-1);</script>");
                               }
                               else{
                                   if(serviceBus.isRecordExisting("SELECT COUNT(PAYMENT_CODE) FROM IA_SALES_PAYMENTS WHERE PAYMENT_CODE = '"+pymtCode+"' AND SORDER_CODE='"+orderCode+"'")){
                                       out.print("<script>alert('Payment Already Exist')</script>");
                                       out.print("<script>history.go(-1);</script>");
                                   }
                                   else{*/
                                       if(serviceBus.createSalesPayment(orderCode,chequeNo,bankCode,amountOwing,amountPaid,userId,pymtType,accountNo,customerCode,bankerCode,depositor,
                                                                       tellerNo,projectCode,drAcctNo,drAcctType,drTranCode,drNarration,drAmount,effDate,crAcctNo,crAcctType,crTranCode,
                                                                       crNarration,crAmount,companyCode,payername,branchcode,amount)){
                                           pymtCode = serviceBus.pymtCode;
                                           out.print("<script>alert('Record Succesfully Saved.')</script>");
                                           out.print("<script>window.document.location='DocumentHelp.jsp?np="+pymtPg+"&orderCode="+orderCode+"&pymtCode="+pymtCode+"'</script>");
                                           
                                       }
                                  }
                          /*     }
                               
                             }*/
                        if(addBankBtn != null){
                             /*  if(pymtCode.equals("")||pymtCode == null){
                                   out.print("<script>alert('Payment no. cannot be empty')</script>");
                                   out.print("<script>history.go(-1);</script>");
                               }
                               else{
                                   if(serviceBus.isRecordExisting("SELECT COUNT(PAYMENT_CODE) FROM IA_SALES_PAYMENTS WHERE PAYMENT_CODE = '"+pymtCode+"' AND SORDER_CODE='"+orderCode+"'")){
                                       out.print("<script>alert('Payment Already Exist')</script>");
                                       out.print("<script>history.go(-1);</script>");
                                   }
                                   else{*/
                                       if(serviceBus.createSalesPayment(orderCode,tellerNo,bankCode,amountOwing,amountPaid,userId,pymtType,accountNo,customerCode,bankerCode,depositor,
                                                                       tellerNo,projectCode,drAcctNo,drAcctType,drTranCode,drNarration,drAmount,effDate,crAcctNo,crAcctType,crTranCode,
                                                                       crNarration,crAmount,companyCode,payername,branchcode,amount)){
                                           pymtCode = serviceBus.pymtCode;
                                           out.print("<script>alert('Record Succesfully Saved.')</script>");
                                           out.print("<script>window.document.location='DocumentHelp.jsp?np="+pymtPg+"&orderCode="+orderCode+"&pymtCode="+pymtCode+"'</script>");
                                           
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
