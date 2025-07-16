package com.magbel.ia.bus;

import com.magbel.ia.dao.PersistenceServiceDAO;
import com.magbel.ia.vao.APInvoice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.magbel.ia.util.ApplicationHelper;
import com.magbel.ia.vao.APInvoiceAccount;
import com.magbel.ia.vao.AccountNo;
import com.magbel.ia.vao.GLAccount;
import com.magbel.ia.vao.GLHistory;
import com.magbel.ia.vao.PaymentHistory;
import com.magbel.ia.vao.PurchasesPayment;
import com.magbel.ia.vao.SalesPayment;
import com.magbel.util.HtmlUtilily;

import java.sql.SQLException;
import java.util.ArrayList;

public class APServiceBus extends PersistenceServiceDAO{
    public String id;
  ApplicationHelper helper;
    public String pymtCode;
    HtmlUtilily apprecord = new HtmlUtilily();
    public APServiceBus() {
        helper = new ApplicationHelper();
    }
  
    public boolean createAPInvoice (String invoiceCode,String vendorCode,String description,
                                double amount,String invoiceDate,String period,String dueDate,
                                double discount,double percentageDiscount,double amountPaid,double amountOwing,
                                String glAccount, double glAmount,int userId){

    String CREATE_QUERY = "INSERT INTO IA_AP_INVOICES "+
                      "(INVOICECODE,VENDORCODE,DESCRIPTION,AMOUNT,INVOICEDATE, "+
                      "PERIOD,DUEDATE,DISCOUNT,PERCDISCOUNT,AMOUNT_PAID,AMOUNT_OWING,GL_ACCOUNT,GL_AMOUNT,MTID,USERID) "+
                              " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        
               
            Connection con = null;
            PreparedStatement ps = null;
            id = helper.getGeneratedId("IA_AP_INVOICES");
            boolean done = false;
            try{

                    con = getConnection();
                    ps = con.prepareStatement(CREATE_QUERY);
                    ps.setString(1,invoiceCode);
                    ps.setString(2,vendorCode);
                    ps.setString(3,description);
                    ps.setDouble(4,amount);
                    ps.setDate(5,dateConvert(invoiceDate));
                    ps.setString(6,period);
                    ps.setDate(7,dateConvert(dueDate));
                    ps.setDouble(8,discount);
                    ps.setDouble(9,percentageDiscount);
                    ps.setDouble(10,amountPaid);
                    ps.setDouble(11,amountOwing);
                    ps.setString(12,glAccount);
                    ps.setDouble(13,glAmount);
                    ps.setString(14,id);
                    ps.setInt(15,userId);
                    
                done = (ps.executeUpdate() != -1);

            }catch(Exception er){
                    System.out.println("Error creating APInvoice... ->"+er.getMessage());
            }finally{
                    closeConnection(con,ps);
            }
          return done;
    }
    /**
    * Deletes a Invoice
    * deleteInvoice
    *
    * @param id String
    */
    public void deleteAPInvoice(String id){
            String DELETE_QUERY = "DELETE FROM IA_AP_INVOICES  WHERE MTID = ?";
        Connection con = null;
            PreparedStatement ps = null;
            try{

                    con = getConnection();
                    ps = con.prepareStatement(DELETE_QUERY);
                    ps.setString(1, id);

                    ps.execute();


            }catch(Exception er){
                    System.out.println("Error Deleting APInvoice... ->"+er.getMessage());
            }finally{
                    closeConnection(con,ps);
            }

    }

    public boolean updateAPInvoice(String id,String invoiceCode,String vendorCode,String description,
                                double amount,String invoiceDate,String period,String dueDate,
                                double discount,double percentageDiscount,double amountPaid,double amountOwing,
                                String glAccount, double glAmount){

            String UPDATE_QUERY =  "UPDATE IA_AP_INVOICES SET VENDORCODE=?,GL_ACCOUNT=?,"+
                                   "DESCRIPTION=?,AMOUNT=?,INVOICEDATE=?,PERIOD=?,DUEDATE=?,DISCOUNT=?,"+
                                   "PERCDISCOUNT=?,GL_AMOUNT=?,AMOUNT_PAID=?,AMOUNT_OWING=? WHERE INVOICECODE=?";
            Connection con = null;
            PreparedStatement ps = null;
            boolean done = false;

            try{
                    con = getConnection();
                    ps = con.prepareStatement(UPDATE_QUERY);
                    ps.setString(1, vendorCode);
                    ps.setString(2, glAccount);
                    ps.setString(3, description);
                    ps.setDouble(4, amount);
                    ps.setDate(5, dateConvert(invoiceDate));
                    ps.setString(6, period);
                    ps.setDate(7, dateConvert(dueDate));
                    ps.setDouble(8, discount);
                    ps.setDouble(9, percentageDiscount);
                    ps.setDouble(10,glAmount);
                    ps.setDouble(11,amountPaid);
                    ps.setDouble(12,amountOwing);
                    ps.setString(13, invoiceCode);
                    
                done = (ps.executeUpdate() != -1);

            }catch(Exception er){
                    System.out.println("Error UPDATING APInvoice... ->"+er.getMessage());
            }finally{
                    closeConnection(con,ps);
            }
    return done;
    }
    public ArrayList<APInvoice> findAllAPInvoice(){

            ArrayList<APInvoice> records = new ArrayList<APInvoice>();
            String SELECT_QUERY = "SELECT MTID,INVOICECODE,VENDORCODE,DESCRIPTION,"+
                                  "AMOUNT,INVOICEDATE,PERIOD,DUEDATE,DISCOUNT,"+
                                  "PERCDISCOUNT,AMOUNT_PAID,AMOUNT_OWING,GL_ACCOUNT,GL_AMOUNT,USERID "+
                                  "FROM IA_AP_INVOICES";
                                  
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try{  

                    con = getConnection();
                    ps = con.prepareStatement(SELECT_QUERY);

                    rs = ps.executeQuery();

                    while(rs.next()){

                            String invoiceCode = rs.getString("INVOICECODE");
                            String vendorCode = rs.getString("VENDORCODE");
                            String description = rs.getString("DESCRIPTION");
                            double amount  = rs.getDouble("AMOUNT");
                            String invoiceDate  = formatDate(rs.getDate("INVOICEDATE"));
                            String period = rs.getString("PERIOD");
                            String dueDate = formatDate(rs.getDate("DUEDATE"));
                            double discount = rs.getDouble("DISCOUNT");
                            double percDiscount = rs.getDouble("PERCDISCOUNT");
                            double amountPaid = rs.getDouble("AMOUNT_PAID");
                            double amountOwing = rs.getDouble("AMOUNT_OWING");
                            String glAccount = rs.getString("GL_ACCOUNT");
                            double glAmount = rs.getDouble("GL_AMOUNT");
                            String id = rs.getString("MTID");
                            int userId = rs.getInt("USERID");
                            
                            APInvoice invoice = new APInvoice(id,invoiceCode,vendorCode,description,
                                                  amount,invoiceDate,period,dueDate,discount,percDiscount,
                                                  amountPaid,amountOwing,glAccount,glAmount,userId);
                            records.add(invoice);
                    }

            }catch(Exception er){
                    System.out.println("Error finding All APInvoice...->"+er.getMessage());
            }finally{
                    closeConnection(con,ps,rs);
            }

            return records ;
    }
    
    public APInvoice findAPInvoiceById(String invoiceCode){
           
        String FIND_QUERY = "SELECT MTID,INVOICECODE,VENDORCODE,DESCRIPTION,"+
                              "AMOUNT,INVOICEDATE,PERIOD,DUEDATE,DISCOUNT,"+
                              "PERCDISCOUNT,AMOUNT_PAID,AMOUNT_OWING,GL_ACCOUNT,GL_AMOUNT,USERID "+
                              "FROM IA_AP_INVOICES WHERE INVOICECODE=?";

        Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            APInvoice invoice = null;

            try{
                    con = getConnection();
                    ps = con.prepareStatement(FIND_QUERY);
                    ps.setString(1,invoiceCode);
                    rs = ps.executeQuery();
                    while(rs.next()){

            String id = rs.getString("MTID");
            String vendorCode = rs.getString("VENDORCODE");
            String description = rs.getString("DESCRIPTION");
            double amount  = rs.getDouble("AMOUNT");
            String invoiceDate  = formatDate(rs.getDate("INVOICEDATE"));
            String period = rs.getString("PERIOD");
            String dueDate = formatDate(rs.getDate("DUEDATE"));
            double discount = rs.getDouble("DISCOUNT");
            double percDiscount = rs.getDouble("PERCDISCOUNT");
            double amountPaid = rs.getDouble("AMOUNT_PAID");
            double amountOwing = rs.getDouble("AMOUNT_OWING");
            String glAccount = rs.getString("GL_ACCOUNT");
            double glAmount = rs.getDouble("GL_AMOUNT");
            int userId = rs.getInt("USERID");
            
            invoice = new APInvoice(id,invoiceCode,vendorCode,description,
                                  amount,invoiceDate,period,dueDate,discount,percDiscount,
                                  amountPaid,amountOwing,glAccount,glAmount,userId);
            
        }

            }catch(Exception er){
                    System.out.println("Error finding APInvoiceByID ->"+er);
            }finally{
                    closeConnection(con,ps,rs);
            }


            return invoice;
    }
    
    //APInvoice GL Account DETAIL
     public boolean createAPInvoiceAcctDetail (String invoiceCode,String glAccount, double glAmount){

     String CREATE_QUERY = "INSERT INTO IA_AP_INV_ACCT_DETAIL "+
                       "(INVOICECODE,GL_ACCOUNT,GL_AMOUNT,MTID) "+
                               " VALUES (?,?,?,?)";
         
                
             Connection con = null;
             PreparedStatement ps = null;
             id = helper.getGeneratedId("IA_AP_INV_ACCT_DETAIL");
             boolean done = false;
             try{

                     con = getConnection();
                     ps = con.prepareStatement(CREATE_QUERY);
                     ps.setString(1,invoiceCode);
                     ps.setString(2,glAccount);
                     ps.setDouble(3,glAmount);
                     ps.setString(4,id);
                     
                 done = (ps.executeUpdate() != -1);

             }catch(Exception er){
                     System.out.println("Error creating APInvoiceAccountDetail... ->"+er.getMessage());
             }finally{
                     closeConnection(con,ps);
             }
           return done;
     }
     
     public void deleteAPInvoiceAcctDetail(String id){
             String DELETE_QUERY = "DELETE FROM IA_AP_INV_ACCT_DETAIL  WHERE MTID = ?";
         Connection con = null;
             PreparedStatement ps = null;
             try{

                     con = getConnection();
                     ps = con.prepareStatement(DELETE_QUERY);
                     ps.setString(1, id);

                     ps.execute();


             }catch(Exception er){
                     System.out.println("Error Deleting APInvoiceAccountDetail... ->"+er.getMessage());
             }finally{
                     closeConnection(con,ps);
             }

     }

     public boolean updateAPInvoiceAcctDetail(String id,String invoiceCode,String glAccount, double glAmount){

             String UPDATE_QUERY =  "UPDATE IA_AP_INV_ACCT_DETAIL SET GL_ACCOUNT=?,"+
                                    "GL_AMOUNT=? WHERE MTID=? AND INVOICECODE=?";
             Connection con = null;
             PreparedStatement ps = null;
             boolean done = false;

             try{
                     con = getConnection();
                     ps = con.prepareStatement(UPDATE_QUERY);
                     ps.setString(1,glAccount);
                     ps.setDouble(2,glAmount);
                     ps.setString(3,id);
                     ps.setString(4,invoiceCode);
                     
                 done = (ps.executeUpdate() != -1);

             }catch(Exception er){
                     System.out.println("Error UPDATING APInvoiceAccountDetail... ->"+er.getMessage());
             }finally{
                     closeConnection(con,ps);
             }
     return done;
     }
     public ArrayList<APInvoiceAccount> findAllAPInvoiceAcctDetail(String invCode){

             ArrayList<APInvoiceAccount> records = new ArrayList<APInvoiceAccount>();
             String SELECT_QUERY = "SELECT MTID,INVOICECODE,GL_ACCOUNT,GL_AMOUNT "+
                                   "FROM IA_AP_INV_ACCT_DETAIL WHERE INVOICECODE =?";
                                   
             Connection con = null;
             PreparedStatement ps = null;
             ResultSet rs = null;

             try{
                     con = getConnection();
                     ps = con.prepareStatement(SELECT_QUERY);
                     ps.setString(1,invCode);
                     rs = ps.executeQuery();
                     while(rs.next()){
                             //String invoiceCode = rs.getString("INVOICECODE");
                             String glAccount = rs.getString("GL_ACCOUNT");
                             double glAmount = rs.getDouble("GL_AMOUNT");
                             String id = rs.getString("MTID");
                              APInvoiceAccount invoice = new APInvoiceAccount(id,invCode,glAccount,glAmount);
                             records.add(invoice);
                     }

             }catch(Exception er){
                     System.out.println("Error finding All APInvoiceAccountDetail...->"+er.getMessage());
             }finally{
                     closeConnection(con,ps,rs);
             }

             return records ;
     }
     
     public APInvoiceAccount findAPInvoiceAcctDetailById(String id,String invoiceCode){
            
         String FIND_QUERY = "SELECT INVOICECODE,GL_ACCOUNT,GL_AMOUNT "+
                             "FROM IA_AP_INV_ACCT_DETAIL WHERE MTID=? AND INVOICECODE=?";

         Connection con = null;
             PreparedStatement ps = null;
             ResultSet rs = null;
             APInvoiceAccount invoice = null;

             try{
                     con = getConnection();
                     ps = con.prepareStatement(FIND_QUERY);
                     ps.setString(1,id);
                     ps.setString(2,invoiceCode);
                     rs = ps.executeQuery();
                     while(rs.next()){

             //String invoiceCode = rs.getString("INVOICECODE");
             String glAccount = rs.getString("GL_ACCOUNT");
             double glAmount = rs.getDouble("GL_AMOUNT");
             
             invoice = new APInvoiceAccount(id,invoiceCode,glAccount,glAmount);
             
         }

             }catch(Exception er){
                     System.out.println("Error finding APInvoiceAccountDetailByID ->"+er);
             }finally{
                     closeConnection(con,ps,rs);
             }
          return invoice;
     }
    
  public boolean createPurchasePayment(String orderCode,String chequeNo,String bankCode,
                                      double amountOwing,double amountPaid,int userId,String pymtType,
                                      String accountNo,String customerCode,String bankerCode,String depositor,String tellerNo,String projectCode,
                                      String drAcctNo,String drAcctType,String drTranCode,String drNarration,double drAmount,
                                      String effDate,String crAcctNo,String crAcctType,String crTranCode,String crNarration,double crAmount,String companyCode) 
	{

     String pymtQuery = "INSERT INTO IA_PURCHASES_PAYMENTS (PAYMENT_CODE,PORDER_CODE,CHEQUE_NO,BANK_CODE,"+
                        "TRANS_DATE,AMOUNT_OWING,AMOUNT_PAID,USERID,PAYMENT_TYPE,MTID,ACCOUNT_NO,VENDOR_CODE,"+
                        "BANKER_CODE,DEPOSITOR,TELLER_NO,PROJECT_CODE,COMP_CODE) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    
     String pymtHistQuery = "INSERT INTO IA_PAYMENT_HISTORY (ACCT_TYPE,CUST_ACCT_NO,TRAN_CODE,"+
                            "DESCRIPTION,AMT,EFFECTIVE_DT,USERID,CREATE_DT,FAMILY_ID,PAYMENT_TYPE,COMP_CODE)" +
                            "VALUES (?,?,?,?,?,?,?,?,?,?,?)"; 
                                    
      String isAutoGen = getCodeName("SELECT auto_generate_ID FROM IA_GB_COMPANY");
        Connection con = null;
        PreparedStatement ps = null;
        boolean autoCommit = false;
        boolean done = false;
        String id = helper.getGeneratedId("IA_PURCHASES_PAYMENTS");
        pymtCode = id;
        //pymtCode = isAutoGen.equalsIgnoreCase("Y") ? id : pymtCode;
        //pymtCode==familyID==id
        int i,j,k;
        try 
        {
         con = getConnection();
         autoCommit = con.getAutoCommit();
         con.setAutoCommit(false);
         ps = con.prepareStatement(pymtQuery);
         ps.setString(1,pymtCode);
         ps.setString(2,orderCode);
         ps.setString(3,chequeNo);
         ps.setString(4,bankCode);
         ps.setDate(5,dateConvert(new java.util.Date()));
         ps.setDouble(6,amountOwing);
         ps.setDouble(7,amountPaid);
         ps.setInt(8,userId);
         ps.setString(9,pymtType);
         ps.setString(10,id);
         ps.setString(11,accountNo);
         ps.setString(12,customerCode);
         ps.setString(13,bankerCode);
         ps.setString(14,depositor);
         ps.setString(15,tellerNo);
         ps.setString(16,projectCode);
		 ps.setString(17,companyCode);
         i = ps.executeUpdate(); //add payment Details
         ps = con.prepareStatement(pymtHistQuery);
         ps.setString(1,drAcctType);
         ps.setString(2,drAcctNo);
         ps.setString(3,drTranCode);
         ps.setString(4,drNarration);
         ps.setDouble(5,drAmount);
         ps.setDate(6,dateConvert(effDate));
         ps.setInt(7,userId);
         ps.setDate(8,dateConvert(new java.util.Date()));
         ps.setString(9,pymtCode);
         ps.setString(10,pymtType);
		 ps.setString(11,companyCode);
         ps.addBatch(); //add first batch
         ps.setString(1,crAcctType);
         ps.setString(2,crAcctNo);
         ps.setString(3,crTranCode);
         ps.setString(4,crNarration);
         ps.setDouble(5,crAmount);
         ps.setDate(6,dateConvert(effDate));
         ps.setInt(7,userId);
         ps.setDate(8,dateConvert(new java.util.Date()));
         ps.setString(9,pymtCode);
         ps.setString(10,pymtType);
		 ps.setString(11,companyCode);
         ps.addBatch(); //add second batch
         j = (ps.executeBatch()).length;
         if((i != -1) && (j != -1))
         {
           con.commit();
           con.setAutoCommit(autoCommit);
           done = true;
          }
                            
         }catch (Exception er){
             System.out.println("ERROR Creating Purchases Payment " + er.getMessage());
             er.printStackTrace();
              try{
                  con.rollback();
                  done = false;
              }
              catch(SQLException ex){
                  System.out.println("Error In Transaction....Rollback ->"+er.getMessage());
              
              }
          }finally {
             closeConnection(con, ps);
          }
           return done;
    }
            
    public java.util.ArrayList<PurchasesPayment> findPurchaseOrdersPymtByQuery(String filter2,String filter) {
            java.util.ArrayList<PurchasesPayment> soList = new java.util.ArrayList<PurchasesPayment>();
            String criteria = " WHERE COMP_CODE='"+filter2+"' AND MTID IS NOT NULL " + filter;
            soList = findPurchaseOrdersPymt(criteria);
            return soList;
    }
    private java.util.ArrayList<PurchasesPayment> findPurchaseOrdersPymt(String filter) {
                    java.util.ArrayList<PurchasesPayment> soList = new java.util.ArrayList<PurchasesPayment>();

                    PurchasesPayment porder = null;

                    Connection con = null;
                    PreparedStatement ps = null;
                    ResultSet rs = null;

                    String query = "SELECT MTID,PORDER_CODE,PAYMENT_CODE,VENDOR_CODE,CHEQUE_NO,"+
                                   "BANK_CODE,AMOUNT,TRANS_DATE,AMOUNT_OWING,"+
                                   "AMOUNT_PAID,ACCOUNT_NO,USERID,PAYMENT_TYPE,BANKER_CODE,DEPOSITOR,TELLER_NO,PROJECT_CODE "+
                                   "FROM IA_PURCHASES_PAYMENTS ";
                   
                    String orderBy = " ORDER BY PORDER_CODE,PAYMENT_CODE DESC";                
                   
                    query += filter;
                    query += orderBy;
                    
                    try {

                            con = getConnection();
                            ps = con.prepareStatement(query);

                            rs = ps.executeQuery();

                            while (rs.next()) {
                                String id = rs.getString("MTID");
                                    String orderNo = rs.getString("PORDER_CODE");
                                    String pymtCode = rs.getString("PAYMENT_CODE");
                                    String customerCode = rs.getString("VENDOR_CODE");
                                    String chequeNo = rs.getString("CHEQUE_NO");
                                    String bankCode = rs.getString("BANK_CODE");
                                    //String amount = rs.getString("AMOUNT");
                                    String transDate = formatDate(rs.getDate("TRANS_DATE"));
                                    double amountOwing = rs.getDouble("AMOUNT_OWING");
                                    double amountPaid = rs.getDouble("AMOUNT_PAID");
                                    String accountNo = rs.getString("ACCOUNT_NO");
                                    int userId = rs.getInt("USERID");
                                    String pymtType = rs.getString("PAYMENT_TYPE");
                                    String bankerCode = rs.getString("BANKER_CODE");
                                    String depositor = rs.getString("DEPOSITOR");
                                    String tellerNo = rs.getString("TELLER_NO");
                                    String projectCode = rs.getString("PROJECT_CODE");
                                   
                                    porder = new PurchasesPayment(id,pymtCode,orderNo,chequeNo,bankCode,transDate,
                                                              amountOwing,amountPaid,userId,pymtType,accountNo,customerCode,bankerCode,depositor,tellerNo,projectCode);
                                    soList.add(porder);

                            }
                    }catch (Exception ex) {
                                            System.out.println("ERROR Querying Purchases Payment " + ex.getMessage());
                                            ex.printStackTrace();
                                    } finally {
                                            closeConnection(con, ps,rs);
                                    }

                    return soList;
            } 
    public PurchasesPayment findPurchasePaymentById(String pymtCode){
           
        String FIND_QUERY = "SELECT PAYMENT_CODE,PORDER_CODE,CHEQUE_NO,BANK_CODE,TRANS_DATE,AMOUNT_OWING,"+
                            "AMOUNT_PAID,USERID,PAYMENT_TYPE,MTID,ACCOUNT_NO,VENDOR_CODE,BANKER_CODE,DEPOSITOR,TELLER_NO,PROJECT_CODE "+
                            "FROM IA_PURCHASES_PAYMENTS WHERE PAYMENT_CODE=? ";

        Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            PurchasesPayment purchasesPymt = null;

            try{
                    con = getConnection();
                    ps = con.prepareStatement(FIND_QUERY);
                    ps.setString(1,pymtCode);
                    rs = ps.executeQuery();
                    while(rs.next()){

             //String pymtCode = rs.getString("PAYMENT_CODE");
              String customerCode = rs.getString("VENDOR_CODE");
             String orderCode = rs.getString("PORDER_CODE");
             String chequeNo = rs.getString("CHEQUE_NO");
             String bankCode = rs.getString("BANK_CODE");
             String transDate = formatDate(rs.getDate("TRANS_DATE"));
              double  amountOwing = rs.getDouble("AMOUNT_OWING");
            double amountPaid = rs.getDouble("AMOUNT_PAID");
            int userId = rs.getInt("USERID");
            String pymtType = rs.getString("PAYMENT_TYPE");
            String mtId = rs.getString("MTID");
            String accountNo = rs.getString("ACCOUNT_NO");
            String bankerCode = rs.getString("BANKER_CODE");
            String depositor = rs.getString("DEPOSITOR");
            String tellerNo = rs.getString("TELLER_NO");
            String projectCode = rs.getString("PROJECT_CODE");
              purchasesPymt = new PurchasesPayment(mtId,pymtCode,orderCode,chequeNo,bankCode,transDate,amountOwing,
                                           amountPaid,userId,pymtType,accountNo,customerCode,bankerCode,depositor,tellerNo,projectCode);
              
            
           }

            }catch(Exception er){
                    System.out.println("Error finding findPurchasePaymentById ->"+er);
            }finally{
                    closeConnection(con,ps,rs);
            }
         return purchasesPymt;
    }
    
    public ArrayList<AccountNo> findBankAccount(String filter) {
                    java.util.ArrayList<AccountNo> acctList = new java.util.ArrayList<AccountNo>();

                    AccountNo acctno = null;
                    Connection con = null;
                    PreparedStatement ps = null;
                    ResultSet rs = null;

                    String query = "SELECT ACCOUNT_NO,BANK_CODE,ACCOUNT_TYPE,ACCOUNT_NAME "+
                                   "FROM IA_ACCOUNT";
                                                    
                    query += filter;
                              
                    try {
                            con = getConnection();
                            ps = con.prepareStatement(query);

                            rs = ps.executeQuery();

                            while (rs.next()) {
                                    String id = "";//rs.getString("MTID");
                                    String accountNo = rs.getString("ACCOUNT_NO");
                                    String bankCode = rs.getString("BANK_CODE");
                                    String accountType = rs.getString("ACCOUNT_TYPE");
                                    String accountName = rs.getString("ACCOUNT_NAME");
                                    int userId = 0;//rs.getInt("USERID");
                                    String transDate = "";//formatDate(rs.getDate("TRANS_DATE"));                                  
                                    acctno = new AccountNo(id,accountNo,bankCode,accountType,"",accountName,transDate,userId);
                                    acctList.add(acctno);

                            }
                    }catch (Exception ex) {
                                            System.out.println("ERROR Querying Account No. " + ex.getMessage());
                                            ex.printStackTrace();
                                    } finally {
                                            closeConnection(con, ps,rs);
                                    }
                  return acctList;
        }
             
    public String getCodeName(String query)
    {
     String result = "";
     Connection con = null;
     ResultSet rs = null; 
     PreparedStatement ps = null;
     
     try
     {
      con = getConnection();
      ps = con.prepareStatement(query);
      rs = ps.executeQuery();
      while(rs.next())
      {
       result = rs.getString(1) == null ? "" : rs.getString(1);
      }
     }
     catch(Exception er)
     {
        System.out.println("Error in APServiceBus- getCodeName()... ->"+er);
        er.printStackTrace();
     }finally{
        closeConnection(con,ps);
      }   
      return result;
     }
    public boolean isRecordExisting(String query){

             boolean exists = false;
             String updateQuery = query;
             Connection con = null;
             PreparedStatement ps = null;
             ResultSet rs = null;

             try{

                     con = getConnection();
                     ps = con.prepareStatement(updateQuery);

                    // ps.setString(1,batchCode);
                     rs = ps.executeQuery();

                     while(rs.next()){
                             int counted = rs.getInt(1);
                             if(counted > 0){
                             exists = true;
                      }
                     }

             }
             catch(Exception er){
                     System.out.println("Error in isRecordExisting()... ->"+er);
                     er.printStackTrace();
             }finally{
                     closeConnection(con,ps);
             }

             return exists;
     } 
    
public boolean createPurchasePayment(String orderCode,String chequeNo,String bankCode,
                                   double amountOwing,double amountPaid,int userId,String pymtType,
                                   String accountNo,String customerCode,String bankerCode,String depositor,String tellerNo,String projectCode,
                                   String drAcctNo,String drAcctType,String drTranCode,String drNarration,double drAmount,
                                   String effDate,String crAcctNo,String crAcctType,String crTranCode,String crNarration,double crAmount,String companyCode,String FamilyId) 
	{

  String pymtQuery = "INSERT INTO IA_PURCHASES_PAYMENTS (PAYMENT_CODE,PORDER_CODE,CHEQUE_NO,BANK_CODE,"+
                     "TRANS_DATE,AMOUNT_OWING,AMOUNT_PAID,USERID,PAYMENT_TYPE,MTID,ACCOUNT_NO,VENDOR_CODE,"+
                     "BANKER_CODE,DEPOSITOR,TELLER_NO,PROJECT_CODE,COMP_CODE) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                 
  String pymtHistQuery = "INSERT INTO IA_PAYMENT_HISTORY (MTID,ACCT_TYPE,CUST_ACCT_NO,TRAN_CODE,"+
                         "DESCRIPTION,AMT,EFFECTIVE_DT,USERID,CREATE_DT,FAMILY_ID,PAYMENT_TYPE,BRANCH_NO,COMP_CODE)" +
                         "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)"; 
                                 
 String GLHistoryquery = "INSERT INTO IA_GL_HISTORY (BRANCH_NO,ACCT_TYPE,GL_ACCT_NO,TRAN_CODE,SBU_CODE,DES" +
  "CRIPTION,AMT,REFERENCE,EFFECTIVE_DT,USERID,CREATE_DT,FAMILY_ID,currency_id,sell_" +
  "act_exch_rate,sell_sys_exch_rate,MTID,sys_tran_code,orig_branch_no,Processed,COMP_CODE)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
  ;
    
   String isAutoGen = getCodeName("SELECT auto_generate_ID FROM IA_GB_COMPANY");
     Connection con = null;
     PreparedStatement ps = null;
     boolean autoCommit = false;
     boolean done = false;
     String id = helper.getGeneratedId("IA_PURCHASES_PAYMENTS");
     String pymtid = helper.getGeneratedId("IA_PURCHASES_PAYMENTS");
     String glid = helper.getGeneratedId("IA_GL_HISTORY");
     pymtCode = id;
     int crCurrCode = 0;
     double crAcctExchRate =0.0;
     double crSysExchRate = 0.0;
     String crSBU = "1";
     String branch_code =apprecord.getCodeName("SELECT Branch_Code FROM IA_CUSTOMER_ACCOUNT_DISPLAY WHERE ACCOUNT_NO = '"+accountNo+"'");
     String crBranch =apprecord.getCodeName("SELECT Branch_id FROM MG_AD_Branch WHERE BRANCH_CODE = '"+branch_code+"'");
     //pymtCode = isAutoGen.equalsIgnoreCase("Y") ? id : pymtCode;
     //pymtCode==familyID==id
     int i,j,k;
     try 
     {
      con = getConnection();
      autoCommit = con.getAutoCommit();
      con.setAutoCommit(false);
      ps = con.prepareStatement(pymtQuery);
      ps.setString(1,pymtCode);
      ps.setString(2,orderCode);
      ps.setString(3,chequeNo);
      ps.setString(4,bankCode);
      ps.setDate(5,dateConvert(new java.util.Date()));
      ps.setDouble(6,amountOwing);
      ps.setDouble(7,amountPaid);
      ps.setInt(8,userId);
      ps.setString(9,pymtType);
      ps.setString(10,id);
      ps.setString(11,crAcctNo);
      ps.setString(12,customerCode);
      ps.setString(13,bankerCode);
      ps.setString(14,depositor);
      ps.setString(15,tellerNo);
      ps.setString(16,projectCode);
		 ps.setString(17,companyCode);
      i = ps.executeUpdate(); //add payment Details
      ps = con.prepareStatement(pymtHistQuery);
      ps.setString(1,pymtid);
      ps.setString(2,crAcctType);
      ps.setString(3,crAcctNo);
      ps.setString(4,crTranCode);
      ps.setString(5,crNarration);
      ps.setDouble(6,drAmount);
      ps.setDate(7,dateConvert(effDate));
      ps.setInt(8,userId);
      ps.setDate(9,dateConvert(new java.util.Date()));
      ps.setString(10,FamilyId);
      ps.setString(11,pymtType);
      ps.setString(12,crBranch);
		 ps.setString(13,companyCode);
		 i = ps.executeUpdate();
      ps = con.prepareStatement(GLHistoryquery);
      //ps.addBatch(); //add first batch
      ps.setString(1,branch_code);   
      ps.setString(2,drAcctType);  
      ps.setString(3,drAcctNo);   
      ps.setString(4,drTranCode);  
      ps.setString(5, crSBU);  
      ps.setString(6,drNarration);  
      ps.setDouble(7,drAmount); 
      ps.setString(8, orderCode);  
      ps.setDate(9,dateConvert(effDate));  
      ps.setInt(10,userId);    
      ps.setDate(11,dateConvert(new java.util.Date()));   
      ps.setString(12,glid);  
		 ps.setInt(13, crCurrCode);  
      ps.setDouble(14, crAcctExchRate); 
      ps.setDouble(15, crSysExchRate); 
      ps.setString(16, glid);         
      ps.setInt(17, Integer.parseInt(crTranCode));  
      ps.setString(18, branch_code);
      ps.setString(19, "N");
      ps.setString(20,companyCode);	        	        
		 j = ps.executeUpdate();
		 // ps.addBatch(); //add second batch
      //j = (ps.executeBatch()).length;
      if((i != -1) && (j != -1))
      {
        con.commit();
        con.setAutoCommit(autoCommit);
        done = true;
       }
                         
      }catch (Exception er){
          System.out.println("ERROR Creating Purchases Payment " + er.getMessage());
          er.printStackTrace();
           try{
               con.rollback();
               done = false;
           }
           catch(SQLException ex){
               System.out.println("Error In Transaction....Rollback ->"+er.getMessage());
           
           }
       }finally {
          closeConnection(con, ps);
       }
        return done;
 }    
    
public boolean createVoucherPayment(String orderCode,String chequeNo,String bankCode,
        double amountOwing,double amountPaid,int userId,String pymtType,
        String accountNo,String customerCode,String bankerCode,String depositor,String tellerNo,String projectCode,
        String drAcctNo,String drAcctType,String drTranCode,String drNarration,double drAmount,
        String effDate,String crAcctNo,String crAcctType,String crTranCode,String crNarration,
        double crAmount,String companyCode,String FamilyId,String branchCode,String invoiceNo,String customerAcct,double accountBalance,String orderNo) 
{
String GLHistoryDebitquery = "INSERT INTO IA_GL_HISTORY (BRANCH_NO,ACCT_TYPE,GL_ACCT_NO,TRAN_CODE,SBU_CODE,DES" +
"CRIPTION,AMT,REFERENCE,EFFECTIVE_DT,USERID,CREATE_DT,FAMILY_ID,currency_id,sell_" +
"act_exch_rate,sell_sys_exch_rate,MTID,sys_tran_code,orig_branch_no,Processed,COMP_CODE,OTHERACCTLEG,BRANCH_CODE,RECEIPT_NO)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
;
String GLHistoryCreditquery = "INSERT INTO IA_GL_HISTORY (BRANCH_NO,ACCT_TYPE,GL_ACCT_NO,TRAN_CODE,SBU_CODE,DES" +
"CRIPTION,AMT,REFERENCE,EFFECTIVE_DT,USERID,CREATE_DT,FAMILY_ID,currency_id,sell_" +
"act_exch_rate,sell_sys_exch_rate,MTID,sys_tran_code,orig_branch_no,Processed,COMP_CODE,OTHERACCTLEG,BRANCH_CODE,RECEIPT_NO)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
;
String StudentDRHistQuery = "INSERT INTO IA_CUSTOMER_ACCOUNT_TRAN_HIST (MTID,ACCOUNT_TYPE,CUST_ACCOUNT_NO,TRAN_CODE,"+
"DESCRIPTION,AMOUNT,EFFECTIVE_DATE,USERID,CREATE_DATE,INV_ITEM_TYPE,BRANCH_CODE,COMP_CODE,CUST_TYPE,BALANCE,POSTING_DATE,REFERENCE,SYS_TRAN_CODE,FAMILY_ID)" +
"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";   

String isAutoGen = getCodeName("SELECT auto_generate_ID FROM IA_GB_COMPANY");
Connection con = null;
PreparedStatement ps = null;
boolean autoCommit = false;
boolean done = false;
//String id = helper.getGeneratedId("IA_PURCHASES_PAYMENTS");
//String pymtid = helper.getGeneratedId("IA_PURCHASES_PAYMENTS");
String glid = helper.getGeneratedId("IA_GL_HISTORY");
if(orderNo==""){orderNo =glid;}
String pymtid = helper.getGeneratedId("IA_CUSTOMER_ACCOUNT_TRAN_HIST");
pymtCode = id;
int crCurrCode = 0;
double crAcctExchRate =0.0;
double crSysExchRate = 0.0;
String crSBU = "1";
//String branch_code =apprecord.getCodeName("SELECT Branch_Code FROM IA_CUSTOMER_ACCOUNT_DISPLAY WHERE ACCOUNT_NO = '"+accountNo+"'");
//String crBranch =apprecord.getCodeName("SELECT Branch_id FROM MG_AD_Branch WHERE BRANCH_CODE = '"+branchCode+"'");
//pymtCode = isAutoGen.equalsIgnoreCase("Y") ? id : pymtCode;
//pymtCode==familyID==id
int i,j,l;
try 
{
con = getConnection();
autoCommit = con.getAutoCommit();
con.setAutoCommit(false);
ps = con.prepareStatement(GLHistoryDebitquery);
//ps.addBatch(); //add first batch
ps.setString(1,branchCode);   
ps.setString(2,drAcctType);  
ps.setString(3,drAcctNo);   
ps.setString(4,drTranCode);  
ps.setString(5, crSBU);  
ps.setString(6,drNarration);  
System.out.println("drNarration: "+drNarration);
ps.setDouble(7,drAmount); 
ps.setString(8, orderCode);  
ps.setDate(9,dateConvert(effDate));  
ps.setInt(10,userId);    
ps.setDate(11,dateConvert(effDate));   
ps.setString(12,orderNo);  
ps.setInt(13, crCurrCode);  
ps.setDouble(14, crAcctExchRate); 
ps.setDouble(15, crSysExchRate); 
ps.setString(16, glid);         
ps.setInt(17, Integer.parseInt(drTranCode));  
ps.setString(18, branchCode);
ps.setString(19, "N");
ps.setString(20,companyCode);
ps.setString(21,crAcctNo); 
ps.setString(22, branchCode);
ps.setString(23, invoiceNo);
i = ps.executeUpdate();
ps = con.prepareStatement(GLHistoryCreditquery);
//ps.addBatch(); //add first batch
ps.setString(1,branchCode);   
ps.setString(2,crAcctType);  
ps.setString(3,crAcctNo);   
ps.setString(4,crTranCode);  
ps.setString(5, crSBU);  
ps.setString(6,crNarration);  
System.out.println("crNarration: "+crNarration);
ps.setDouble(7,crAmount); 
ps.setString(8, orderCode);  
ps.setDate(9,dateConvert(effDate));  
ps.setInt(10,userId);    
ps.setDate(11,dateConvert(effDate));   
ps.setString(12,orderNo);  
ps.setInt(13, crCurrCode);  
ps.setDouble(14, crAcctExchRate);   
ps.setDouble(15, crSysExchRate); 
ps.setString(16, glid);         
ps.setInt(17, Integer.parseInt(crTranCode));  
ps.setString(18, branchCode);
ps.setString(19, "N");
ps.setString(20,companyCode);
ps.setString(21,drAcctNo); 
ps.setString(22, branchCode);
ps.setString(23, invoiceNo);
j = ps.executeUpdate();
//ps.addBatch(); //add second batch
//j = (ps.executeBatch()).length;
if(customerAcct!=""){
ps = con.prepareStatement(StudentDRHistQuery);
ps.setString(1, pymtid);
ps.setString(2,drAcctType);
ps.setString(3,customerAcct);
ps.setString(4,drTranCode);
ps.setString(5,drNarration);
ps.setDouble(6,drAmount);
ps.setDate(7,dateConvert(effDate));
ps.setInt(8,userId);        
ps.setDate(9,dateConvert(effDate));
ps.setString(10,pymtType);
ps.setString(11,branchCode);
ps.setString(12,companyCode);
ps.setString(13,pymtType);
double totalbal = accountBalance - drAmount;
ps.setDouble(14,totalbal);
ps.setDate(15,dateConvert(effDate));
ps.setString(16, glid);
ps.setString(17,drTranCode);
ps.setString(18,orderNo); 
l = ps.executeUpdate();
}
if(j != -1)
{
con.commit();
con.setAutoCommit(autoCommit);
done = true;
}

}catch (Exception er){
System.out.println("ERROR Creating Purchases Payment " + er.getMessage());
er.printStackTrace();
try{
con.rollback();
done = false;
}
catch(SQLException ex){
System.out.println("Error In Transaction....Rollback ->"+er.getMessage());

}
}finally {
closeConnection(con, ps);
}
return done;
}
public boolean createImprestTransaction(String orderCode,String chequeNo,String bankCode,
        double amountOwing,double amountPaid,int userId,String pymtType,
        String accountNo,String customerCode,String bankerCode,String depositor,String tellerNo,String projectCode,
        String drAcctNo,String drAcctType,String drTranCode,String drNarration,double drAmount,
        String effDate,String crAcctNo,String crAcctType,String crTranCode,String crNarration,
        double crAmount,String companyCode,String FamilyId,String branchCode,String invoiceNo) 
{
String GLHistoryDebitquery = "INSERT INTO IA_GL_HISTORY (BRANCH_NO,ACCT_TYPE,GL_ACCT_NO,TRAN_CODE,SBU_CODE,DES" +
"CRIPTION,AMT,REFERENCE,EFFECTIVE_DT,USERID,CREATE_DT,FAMILY_ID,currency_id,sell_" +
"act_exch_rate,sell_sys_exch_rate,MTID,sys_tran_code,orig_branch_no,Processed,COMP_CODE,OTHERACCTLEG,BRANCH_CODE,RECEIPT_NO)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
;
String GLHistoryCreditquery = "INSERT INTO IA_GL_HISTORY (BRANCH_NO,ACCT_TYPE,GL_ACCT_NO,TRAN_CODE,SBU_CODE,DES" +
"CRIPTION,AMT,REFERENCE,EFFECTIVE_DT,USERID,CREATE_DT,FAMILY_ID,currency_id,sell_" +
"act_exch_rate,sell_sys_exch_rate,MTID,sys_tran_code,orig_branch_no,Processed,COMP_CODE,OTHERACCTLEG,BRANCH_CODE,RECEIPT_NO)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
;

String isAutoGen = getCodeName("SELECT auto_generate_ID FROM IA_GB_COMPANY");
Connection con = null;
PreparedStatement ps = null;
boolean autoCommit = false;
boolean done = false;
//String id = helper.getGeneratedId("IA_PURCHASES_PAYMENTS");
//String pymtid = helper.getGeneratedId("IA_PURCHASES_PAYMENTS");
String glid = helper.getGeneratedId("IA_GL_HISTORY");
pymtCode = id;
int crCurrCode = 0;
double crAcctExchRate =0.0;
double crSysExchRate = 0.0;
String crSBU = "1";
//String branch_code =apprecord.getCodeName("SELECT Branch_Code FROM IA_CUSTOMER_ACCOUNT_DISPLAY WHERE ACCOUNT_NO = '"+accountNo+"'");
//String crBranch =apprecord.getCodeName("SELECT Branch_id FROM MG_AD_Branch WHERE BRANCH_CODE = '"+branchCode+"'");
//pymtCode = isAutoGen.equalsIgnoreCase("Y") ? id : pymtCode;
//pymtCode==familyID==id
int i,j;
try 
{
con = getConnection();
autoCommit = con.getAutoCommit();
con.setAutoCommit(false);
ps = con.prepareStatement(GLHistoryDebitquery);
//ps.addBatch(); //add first batch
ps.setString(1,branchCode);   
ps.setString(2,drAcctType);  
ps.setString(3,drAcctNo);   
ps.setString(4,drTranCode);  
ps.setString(5, crSBU);  
ps.setString(6,drNarration);  
System.out.println("drNarration: "+drNarration);
ps.setDouble(7,drAmount); 
ps.setString(8, orderCode);  
ps.setDate(9,dateConvert(effDate));  
ps.setInt(10,userId);    
ps.setDate(11,dateConvert(effDate));   
ps.setString(12,glid);  
ps.setInt(13, crCurrCode);  
ps.setDouble(14, crAcctExchRate); 
ps.setDouble(15, crSysExchRate); 
ps.setString(16, glid);         
ps.setInt(17, Integer.parseInt(drTranCode));  
ps.setString(18, branchCode);
ps.setString(19, "N");
ps.setString(20,companyCode);
ps.setString(21,crAcctNo); 
ps.setString(22, branchCode);
ps.setString(23, invoiceNo);
i = ps.executeUpdate();
ps = con.prepareStatement(GLHistoryCreditquery);
//ps.addBatch(); //add first batch
ps.setString(1,branchCode);   
ps.setString(2,crAcctType);  
ps.setString(3,crAcctNo);   
ps.setString(4,crTranCode);  
ps.setString(5, crSBU);  
ps.setString(6,crNarration);  
System.out.println("crNarration: "+crNarration);
ps.setDouble(7,crAmount); 
ps.setString(8, orderCode);  
ps.setDate(9,dateConvert(effDate));  
ps.setInt(10,userId);    
ps.setDate(11,dateConvert(effDate));   
ps.setString(12,glid);  
ps.setInt(13, crCurrCode);  
ps.setDouble(14, crAcctExchRate);   
ps.setDouble(15, crSysExchRate); 
ps.setString(16, glid);         
ps.setInt(17, Integer.parseInt(crTranCode));  
ps.setString(18, branchCode);
ps.setString(19, "N");
ps.setString(20,companyCode);
ps.setString(21,drAcctNo); 
ps.setString(22, branchCode);
ps.setString(23, invoiceNo);
j = ps.executeUpdate();
//ps.addBatch(); //add second batch
//j = (ps.executeBatch()).length;
if(j != -1)
{
con.commit();
con.setAutoCommit(autoCommit);
done = true;
}

}catch (Exception er){
System.out.println("ERROR Creating Payable Imprest Payment " + er.getMessage());
er.printStackTrace();
try{
con.rollback();
done = false;
}
catch(SQLException ex){
System.out.println("Error In Transaction....Rollback ->"+er.getMessage());

}
}finally {
closeConnection(con, ps);
}
return done;
}          
}
