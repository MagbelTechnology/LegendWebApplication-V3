package com.magbel.ia.servlet;

import com.magbel.ia.bus.ARServiceBus;

import com.magbel.ia.bus.AccountChartServiceBus;
import com.magbel.ia.bus.InventoryServiceBus;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;

import javax.servlet.*;
import javax.servlet.http.*;
import com.magbel.ia.bus.PurchaseOrderServiceBus;
import com.magbel.ia.bus.SupervisorServiceBus;
import com.magbel.ia.util.CodeGenerator;
import com.magbel.ia.vao.AccountNo;
import com.magbel.ia.vao.InventoryTotal;
import com.magbel.ia.vao.User;
import com.magbel.ia.vao.WareHouse;

public class ItemCallBack extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
       
    }
    PurchaseOrderServiceBus pos = new PurchaseOrderServiceBus();
    CodeGenerator  codegen = new CodeGenerator();
    ARServiceBus ars = new ARServiceBus();
    ArrayList list = new ArrayList();
    SupervisorServiceBus superv = new SupervisorServiceBus();
    InventoryServiceBus inv = new InventoryServiceBus();
    AccountChartServiceBus acctChart = new AccountChartServiceBus();
    int userId = 0;
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
      String itemCode = request.getParameter("ITEMCODE");
      String item_Code = request.getParameter("itemCode");
      String strItemTotAmount = request.getParameter("ITEMAMOUNT");
      double itemTotAmount = ((strItemTotAmount != null) && (!strItemTotAmount.equals("")))? Double.parseDouble(strItemTotAmount):0;
      String warehouseCode = request.getParameter("WAREHOUSECODE");
      String warehouse = request.getParameter("warehouse");
      String orderCode = request.getParameter("PORDERCODE");
      String batchCode = request.getParameter("BATCHCODE");
      String operation = request.getParameter("OPT").trim();
      String paymentOption = request.getParameter("PAYMENTOPTION");
      String bankCode = request.getParameter("BANKCODE");
      String moduleCode = request.getParameter("MODULECODE");
      String transType = request.getParameter("TRANSTYPE");
      String ID = request.getParameter("ID");
      String acctTranCode = request.getParameter("ACCTTRANCODE");
      String acctTypeCode = request.getParameter("ACCTTYPECODE");
      String currCode = request.getParameter("CURRCODE");
      String branchCode = request.getParameter("BRANCHCODE");
      String glAcctNo = request.getParameter("GLACCTNO");
      String userId_ = request.getParameter("USERID");
      String benAcctNo = request.getParameter("BENACCTNO"); 
      String transactionCode = request.getParameter("transactionCode");
      String wareHouseCode2 = request.getParameter("WAREHOUSECODE2");
      String itemCode2 = request.getParameter("ITEMCODE2");
      String salesAcct = request.getParameter("salesAcct");
      String branchWarehouseCode = request.getParameter("BRANCHWAREHOUSECODE");
      String invCode = request.getParameter("INVCODE");
 //     System.out.println("ItemCallBack operation: "+operation+"     itemCode: "+itemCode+"  item_Code: "+item_Code);  
      //userId = Integer.parseInt(userId_); 
      userId = ((userId_!=null) && (!userId_.equals(""))) ? Integer.parseInt(userId_) : 0;
        
     /* try{
         userId = ((userId_!=null) && (!userId_.equals(""))) ? Integer.parseInt(userId_) : 0;
      }
      catch(NullPointerException e){
         response.sendRedirect("sessionTimedOut.jsp");
      }
      catch(Exception e){
        e.printStackTrace();
      }*/      
      
      //HttpSession session = request.getSession(true);
 
       if(operation.equals("1"))
         {
          if ((itemCode != null)||(itemCode != "")) {
//        	  System.out.println("ItemCallBack itemCode: "+itemCode+"   warehouseCode: "+warehouseCode);  
              String unitPrice   = pos.getCodeName("SELECT STANDARD_COST FROM ST_INVENTORY_ITEMS WHERE ITEM_CODE='"+itemCode+"'");
              String qtyquery =  "SELECT BALANCE FROM ST_INVENTORY_TOTALS WHERE ITEM_CODE='"+itemCode+"' AND WAREHOUSE_CODE='"+warehouseCode+"'";
            //  System.out.println("qtyquery in ItemCallBack: "+qtyquery);
              String strAvailableQuant = pos.getCodeName(qtyquery);
              String availableQuant = strAvailableQuant.equalsIgnoreCase("") ? "0" : strAvailableQuant;
              String reqApproval   = "N";//pos.getCodeName("SELECT REQ_APPROVAL FROM ST_INVENTORY_ITEMS WHERE ITEM_CODE='"+itemCode+"'");
//              System.out.println("ItemCallBack unitPrice: "+unitPrice+"   strAvailableQuant: "+strAvailableQuant+"   availableQuant: "+availableQuant);
              String result = unitPrice +"#"+availableQuant+"#"+reqApproval;
 //             System.out.println("ItemCallBack result: "+result);  
              processRequest(request, response, result);
          }
         } 
     
       //process purchase order delivery items  
         if(operation.equals("2")){
            if (((orderCode != null)||(orderCode != ""))&&((batchCode != null)||(batchCode != ""))) {
            boolean  result   = true;////new PurchaseOrderServiceBus().createPurchaseOrderItemDetail(orderCode,batchCode);
            processPurchaseOrderDeliveryItemRequest(request, response, result);
           }
        }
        
        //getCodeName() method
         if(operation.equals("3")){
            //String codeGen = ""; 
            String  result   = pos.getCodeName("SELECT AUTO_GENERATE_ID FROM IA_GB_COMPANY");//new PurchaseOrderServiceBus().createPurchaseOrderItemDetail(orderCode,batchCode);
           /* if(result.equalsIgnoreCase("Y")){
             result+="#";
             result+=codeGen = codegen.generateCode(moduleCode,"","","");
            }*/
            processGetCodeNameRequest(request, response, result);
           
         } 
        if(operation.equals("4")){
           if (((paymentOption != null)||(paymentOption != ""))){
            processPaymentModeRequest(request, response, paymentOption);
         }
        }
       if(operation.equals("5")){
           if (((bankCode != null)&&(!bankCode.equals("")))){
              list = ars.findBankAccount(" WHERE BANK_CODE='"+bankCode+"'");
              processBankAccountRequest(request, response, list);
          
          }
       } 
       if(operation.equals("6")){
           if (((itemCode != null)&&(!itemCode.equals("")))){
                list = superv.getCurrentApprovingOfficer(userId,itemCode,itemTotAmount);
                processApproveOfficerRequest(request, response, list);
             
        }   
      }
      if(operation.equals("7")){
            if (((itemCode != null)&&(!itemCode.equals("")))){
                 boolean x = superv.isItemRequireApproval(itemCode);
                 list = superv.getCurrentApprovingOfficer(userId,itemCode,itemTotAmount);
                 if((x) && (list.size() > 0)){
                  processApproveOfficerRequest(request, response, list);
                 }
                 else{
                  processItemNotRequireApprovalRequest(request,response,x); 
                 }
              
         }   
        }
        if(operation.equals("8")){
              if(((itemCode != null)&&(!itemCode.equals("")))){
                list = superv.getNextApprovingOfficer(userId,ID,transType,itemCode,itemTotAmount);
                processApproveOfficerRequest(request, response, list);
            }   
          }
        if(operation.equals("9")){
              if (((warehouseCode != null)&&(!warehouseCode.equals("")))){
                 list = inv.findAllInventoryTotalItemByQuery(" AND a.WAREHOUSE_CODE='"+warehouseCode+"'");
                 processInventoryTotalItemRequest(request, response, list);
            }   
          }   
         if(operation.equals("10")){
              if(((acctTypeCode != null)&&(!acctTypeCode.equals("")))){
                 String result = acctChart.getCodeName("SELECT ACCT_SERIAL FROM ST_AD_ACCOUNT_TYPE WHERE ACCT_SERIAL='"+acctTypeCode+"'");
                 processAccountTranCodeRequest(request, response, result);
            }   
          }
        if(operation.equals("11")){
              if(((acctTranCode != null)&&(!acctTranCode.equals("")))){
                 String result = acctChart.getCodeName("SELECT TRAN_CODE FROM ST_AD_TRAN_CODE WHERE TRAN_CODE='"+acctTranCode.trim()+"'");
                 processAccountTranCodeRequest(request, response, result);
            }   
          }
        if(operation.equals("12")){
              if(((currCode != null)&&(!currCode.equals("")))){
                 //get currency ID using ISO==CurrCode
                 String currId = acctChart.getCodeName("SELECT CURRENCY_ID FROM ST_GB_CURRENCY_CODE WHERE ISO_CODE='"+currCode.trim()+"'");
                 String result = acctChart.getCodeName("SELECT EXCHG_RATE FROM ST_GB_EXCH_RATE WHERE CURRENCY_ID='"+currId.trim()+"'");
                 processExchaneRateRequest(request, response, result);
            }   
          }
        if(operation.equals("13")){
              if(((glAcctNo != null)&&(!glAcctNo.equals("")))){
                 String result = acctChart.getCodeName("SELECT DESCRIPTION FROM ST_GL_ACCOUNT WHERE GL_ACCT_NO='"+glAcctNo.trim()+"'");
                 processValidateGLAccountRequest(request, response, result);
            }   
        }
        if(operation.equals("14")){ //this method is 4 imprest accounting
        itemCode="";
        
                  list = superv.getCurrentApprovingOfficer(userId,itemCode,itemTotAmount);
                   if(list.size() > 0){
                    processApproveOfficerRequest(request,response,list);
                   }
                   else{
                    processItemNotRequireApprovalRequest(request,response,false); 
                   }
                
         
          } 
        
        if(operation.equals("15")){
              itemCode="";
                 list = superv.getNextApprovingOfficer(userId,ID,transType,itemCode,itemTotAmount);
                processApproveOfficerRequest(request, response, list);
           
          }
        if(operation.equals("16"))
          {
           if ((benAcctNo != null)||(benAcctNo != "")) {
               String result   = pos.getCodeName("SELECT ACCOUNT_NO FROM ST_CUSTOMER_ACCOUNT_SETUP WHERE CUSTOMER_CLASS='300' AND ACCOUNT_NO='"+benAcctNo+"'");
               processRequest(request, response, result);
           }
          }
        if(operation.equals("17") && (transactionCode != null || transactionCode != ""))
        {
            String tranCodeType = pos.getCodeName((new StringBuilder("select tran_code_type from ST_GB_TC where tran_code='")).append(transactionCode).append("'").toString());
            String debitCreditIndicator = pos.getCodeName((new StringBuilder("select debit_credit from ST_GB_TC where tran_code='")).append(transactionCode).append("'").toString());
            String contraTc = pos.getCodeName((new StringBuilder("select gl_acct_contra_tc from ST_GB_TC where tran_code='")).append(transactionCode).append("'").toString());
            String result = (new StringBuilder(String.valueOf(tranCodeType))).append("#").append(debitCreditIndicator).append("#").append(contraTc).toString();
            processUserTransaction(request, response, result);
        }
        if(operation.equals("18") && (itemCode2 != null || itemCode2 != "") && (wareHouseCode2 != null || wareHouseCode2 != ""))
        {
            String balance = pos.getCodeName((new StringBuilder("select balance from  ST_INVENTORY_TOTALS where ITEM_CODE = '")).append(itemCode2).append("' AND  WAREHOUSE_CODE = '").append(wareHouseCode2).append("'").toString());
            String result = balance;
            processBalanceTransaction(request, response, result);
        }
        if(operation.equals("19") && (salesAcct != null || salesAcct != ""))
        {
            String account = pos.getCodeName((new StringBuilder("select description from  ia_gl_acct_ledger where ledger_no = '")).append(salesAcct).append("'").toString());
            String result = account;
            processAccountTransaction(request, response, result);
        }
        if(operation.equals("20") && branchWarehouseCode != null && !branchWarehouseCode.equals(""))
        {
            list = inv.findAllBranchWarehouseCode((new StringBuilder(" and b.branch_code='")).append(branchWarehouseCode).append("'").toString());
            processBranchWarehouseCodeRequest(request, response, list);
        }
        if(operation.equals("21") && warehouseCode != null && !warehouseCode.equals(""))
        {
            list = inv.findInventoryItemByWarehouse((new StringBuilder(" AND a.WAREHOUSE_CODE='")).append(warehouseCode).append("'").toString());
            processInventoryItemByWarehouseRequest(request, response, list);
        }
        
        if(operation.equals("22")) 
        {
         if ((invCode != null)||(invCode != "")) {
        	 list   = inv.findWarehouseByInventoryItem("  and  item_code='" + invCode +"' and a.WAREHOUSE_CODE='"+warehouse+"' ");
        	 processWarehouseByInventoryItemRequest(request, response, list);
         }
        } 
        
        if(operation.equals("23"))
          {
           if ((itemCode != null)||(itemCode != "")) {
//        	  System.out.println("ItemCallBack itemCode: "+itemCode+"   warehouseCode: "+warehouseCode);  
//               String unitPrice   = pos.getCodeName("SELECT STANDARD_COST FROM ST_INVENTORY_RFID_APPROVED WHERE ITEM_CODE='"+itemCode+"'");
               String qtyquery =  "SELECT GROUP_ID+'#'+CAST(QUANTITY AS VARCHAR(12)) FROM ST_INVENTORY_RFID_APPROVED WHERE RFID_TAG='"+itemCode+"'";
//               System.out.println("qtyquery in ItemCallBack: "+qtyquery);
               String result = pos.getCodeName(qtyquery);
//               System.out.println("ItemCallBack result: "+result);  
               processRequest(request, response, result);
           }
          }         
    }
    private void processPurchaseOrderDeliveryItemRequest(HttpServletRequest request,
                                            HttpServletResponse response,
                                            boolean result) throws
             IOException, ServletException {
         //StringBuffer sb = new StringBuffer();
         response.setContentType("text/xml");
         response.setHeader("Cache-Control", "no-cache");
        String x = "";
        x = "false";
         //sb.append(unitPrice);
         if(result){
           x = "true";    
         }
         response.getWriter().write(x);
     }
    private void processRequest(HttpServletRequest request,
                                            HttpServletResponse response,
                                            String result) throws
             IOException, ServletException {
         //StringBuffer sb = new StringBuffer();
         response.setContentType("text/xml");
         response.setHeader("Cache-Control", "no-cache");

         //sb.append(unitPrice);
         response.getWriter().write(result);

     }
    private void processGetCodeNameRequest(HttpServletRequest request,
                                            HttpServletResponse response,
                                            String result) throws
             IOException, ServletException {
         //StringBuffer sb = new StringBuffer();
         response.setContentType("text/xml");
         response.setHeader("Cache-Control", "no-cache");

         //sb.append(unitPrice);
         response.getWriter().write(result);

     }
     
    private void processPaymentModeRequest(HttpServletRequest request,
                                            HttpServletResponse response,
                                            String optValue) throws
             IOException, ServletException {
         StringBuffer sb = new StringBuffer();
          response.setContentType("text/xml");
          response.setHeader("Cache-Control", "no-cache");

              sb.append("<OPTIONS>\n");
              sb.append("<VALUE>Cash</VALUE>\n");
              sb.append("<NAME>Cash</NAME>\n");
              sb.append("</OPTIONS>\n");
              sb.append("<OPTIONS>\n"); 
              sb.append("<VALUE>Cheque</VALUE>\n");
              sb.append("<NAME>Cheque</NAME>\n");
              sb.append("</OPTIONS>\n");
         
           //System.out.println(sb.toString());
          response.getWriter().write(sb.toString());

     }
 
    private void processBankAccountRequest(HttpServletRequest request,
                                            HttpServletResponse response,
                                            ArrayList list) throws
          IOException, ServletException {
          StringBuffer sb = new StringBuffer();
          response.setContentType("text/xml");
          response.setHeader("Cache-Control", "no-cache");
         sb.append("<TAG-HEAD>\n");
         for (int x = 0; x < list.size(); x++) {

             AccountNo acc = (AccountNo)list.get(x);
             sb.append("<ACCTOPTIONS>\n");
             sb.append("<ACCTCODE>" + acc.getAccountNo() + "</ACCTCODE>\n");
             sb.append("<ACCTNAME>" + acc.getAccountName() + "</ACCTNAME>\n");
             sb.append("</ACCTOPTIONS>\n");
         }
         sb.append("</TAG-HEAD>\n");
          response.getWriter().write(sb.toString());

     }
     
    private void processApproveOfficerRequest(HttpServletRequest request,
                                            HttpServletResponse response,
                                            ArrayList list) throws
          IOException, ServletException {
          StringBuffer sb = new StringBuffer();
          response.setContentType("text/xml");
          response.setHeader("Cache-Control", "no-cache");
         sb.append("<TAG-HEAD>\n");
         for (int x = 0; x < list.size(); x++) {

             User acc = (User)list.get(x);
             sb.append("<APPROPTIONS>\n");
             sb.append("<USERID>" + acc.getUserId() + "</USERID>\n");
             sb.append("<FULLNAME>" + acc.getUserFullName() + "</FULLNAME>\n");
             sb.append("</APPROPTIONS>\n");
         }
         sb.append("</TAG-HEAD>\n");
         
          response.getWriter().write(sb.toString());

     }
    
    private void processInventoryTotalItemRequest(HttpServletRequest request,
                                            HttpServletResponse response,
                                            ArrayList list) throws
          IOException, ServletException {
          StringBuffer sb = new StringBuffer();
          response.setContentType("text/xml");
          response.setHeader("Cache-Control", "no-cache");
         sb.append("<TAG-HEAD>\n");
         for (int x = 0; x < list.size(); x++) {
             InventoryTotal acc = (InventoryTotal)list.get(x);
             sb.append("<ITEMOPTIONS>\n");
             sb.append("<ITEMCODE>" + acc.getItemCode() + "</ITEMCODE>\n");
             sb.append("<ITEMNAME>" + acc.getDesc() + "</ITEMNAME>\n");
             sb.append("</ITEMOPTIONS>\n");
         }
         sb.append("</TAG-HEAD>\n");
         
          response.getWriter().write(sb.toString());

     }
    
    private void processWarehouseByInventoryItemRequest(HttpServletRequest request, HttpServletResponse response, ArrayList list)
    throws IOException, ServletException
{
    StringBuffer sb = new StringBuffer();
    StringBuffer sb2;
    response.setContentType("text/xml");
    response.setHeader("Cache-Control", "no-cache");
    sb.append("<WHOUSE>\n");
    for(int x = 0; x < list.size(); x++)
    {
        InventoryTotal acc = (InventoryTotal)list.get(x);
        sb.append("<WAREHOUSE>\n");
        sb.append("<WAREHOUSECODE>" + acc.getItemCode() + "</WAREHOUSECODE>\n");
        sb.append("<WAREHOUSENAME>" + acc.getDesc() + "</WAREHOUSENAME>\n");
        sb.append("<BALANCE>" + acc.getItemBalance() + "</BALANCE>\n");
        sb.append("</WAREHOUSE>\n");
    }

    sb.append("</WHOUSE>\n");
    sb2 = sb;
    System.out.println("Result >>>>>>> " + sb2.toString());
    response.getWriter().write(sb.toString());
}
    private void processItemNotRequireApprovalRequest(HttpServletRequest request,
                                            HttpServletResponse response,
                                            boolean result) throws
          IOException, ServletException {
         StringBuffer sb = new StringBuffer();
         response.setContentType("text/xml");
         response.setHeader("Cache-Control", "no-cache");
         sb.append("<TAG-HEAD>\n");
         sb.append("</TAG-HEAD>\n");         
         response.getWriter().write(sb.toString());

     }
    private void processAccountTranCodeRequest(HttpServletRequest request,
                                            HttpServletResponse response,
                                            String result) throws
          IOException, ServletException {
          //StringBuffer sb = new StringBuffer();
          response.setContentType("text/xml");
          response.setHeader("Cache-Control", "no-cache");
          if((result != null)&&(!result.equals(""))) {
             
          }else{
              result = "";
          }
          response.getWriter().write(result);

     }
    private void processExchaneRateRequest(HttpServletRequest request,
                                            HttpServletResponse response,
                                            String result) throws
          IOException, ServletException {
          //StringBuffer sb = new StringBuffer();
          response.setContentType("text/xml");
          response.setHeader("Cache-Control", "no-cache");
          if((result != null)&&(!result.equals(""))) {
             
          }else{
              result = "";
          }
          response.getWriter().write(result);
    }
    private void processValidateGLAccountRequest(HttpServletRequest request,
                                            HttpServletResponse response,
                                            String result) throws
          IOException, ServletException {
          //StringBuffer sb = new StringBuffer();
          response.setContentType("text/xml");
          response.setHeader("Cache-Control", "no-cache");
          if((result != null)&&(!result.equals(""))) {
             
          }else{
              result = "";
          }
          response.getWriter().write(result);

     } 
    private void processBranchWarehouseCodeRequest(HttpServletRequest request, HttpServletResponse response, ArrayList list)
    throws IOException, ServletException
{
    StringBuffer sb = new StringBuffer();
    response.setContentType("text/xml");
    response.setHeader("Cache-Control", "no-cache");
    sb.append("<TAG-HEAD>\n");
    for(int x = 0; x < list.size(); x++)
    {
        WareHouse ac = (WareHouse)list.get(x);
        sb.append("<WAREHOUSEOPTIONS>\n");
        sb.append((new StringBuilder("<WAREHOUSECODE>")).append(ac.getCode()).append("</WAREHOUSECODE>\n").toString());
        sb.append((new StringBuilder("<WAREHOUSENAME>")).append(ac.getName()).append("</WAREHOUSENAME>\n").toString());
        sb.append("</WAREHOUSEOPTIONS>\n");
    }

    sb.append("</TAG-HEAD>\n");
    response.getWriter().write(sb.toString());
}
    private void processInventoryItemByWarehouseRequest(HttpServletRequest request, HttpServletResponse response, ArrayList list)
    throws IOException, ServletException
{
    StringBuffer sb = new StringBuffer();
    response.setContentType("text/xml");
    response.setHeader("Cache-Control", "no-cache");
    sb.append("<HEAD>\n");
    for(int x = 0; x < list.size(); x++)
    {
        InventoryTotal acc = (InventoryTotal)list.get(x);
        sb.append("<ITEM>\n");
        sb.append((new StringBuilder("<ITEMCD>")).append(acc.getItemCode()).append("</ITEMCD>\n").toString());
        sb.append((new StringBuilder("<ITEMNM>")).append(acc.getDesc()).append("</ITEMNM>\n").toString());
        sb.append("</ITEM>\n");
    }

    sb.append("</HEAD>\n");
    response.getWriter().write(sb.toString());
}
    private void processUserTransaction(HttpServletRequest request, HttpServletResponse response, String result)
    throws IOException, ServletException
{
    response.setContentType("text/xml");
    response.setHeader("Cache-Control", "no-cache");
    response.getWriter().write(result);
}

    private void processBalanceTransaction(HttpServletRequest request, HttpServletResponse response, String result)
        throws IOException, ServletException
    {
        response.setContentType("text/xml");
        response.setHeader("Cache-Control", "no-cache");
        response.getWriter().write(result);
    }
    private void processAccountTransaction(HttpServletRequest request, HttpServletResponse response, String result)
    throws IOException, ServletException
{
    response.setContentType("text/xml");
    response.setHeader("Cache-Control", "no-cache");
    response.getWriter().write(result);
}
}
