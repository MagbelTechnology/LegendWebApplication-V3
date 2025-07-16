package com.magbel.ia.bus;

import com.magbel.ia.dao.PersistenceServiceDAO;
import com.magbel.ia.util.ApplicationHelper;

//import com.magbel.ia.vao.Account;
import com.magbel.ia.vao.AccountNo;
import com.magbel.ia.vao.SalesOrder;
import com.magbel.ia.vao.SalesOrderItem;
import com.magbel.ia.vao.SalesPayment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ARServiceBus extends PersistenceServiceDAO{
    
    ApplicationHelper helper;
    public String pymtCode;
    public ARServiceBus() {
        helper = new ApplicationHelper();
    }
    
    public boolean createSalesPayment(String orderCode,String chequeNo,String bankCode,
                                      double amountOwing,double amountPaid,int userId,String pymtType,
                                      String accountNo,String customerCode,String bankerCode,String depositor,
                                      String tellerNo,String projectCode,String drAcctNo,String drAcctType,String drTranCode,
                                      String drNarration,double drAmount,String effDate,String crAcctNo,String crAcctType,
                                      String crTranCode,String crNarration,double crAmount,String companyCode,String payername,String branchcode,double amount) {

                String pymtQuery = "INSERT INTO IA_SALES_PAYMENTS (PAYMENT_CODE,SORDER_CODE,CHEQUE_NO,BANK_CODE,"+
                                   "TRANS_DATE,AMOUNT_OWING,AMOUNT_PAID,USERID,PAYMENT_TYPE,MTID,ACCOUNT_NO,CUSTOMER_CODE,"+
                                   "BANKER_CODE,DEPOSITOR,TELLER_NO,PROJECT_CODE,COMP_CODE,PAYER_NAME,BRANCH_CODE,AMOUNT) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    
                String pymtHistQuery = "INSERT INTO IA_PAYMENT_HISTORY (ACCT_TYPE,CUST_ACCT_NO,TRAN_CODE,"+
                                       "DESCRIPTION,AMT,EFFECTIVE_DT,USERID,CREATE_DT,FAMILY_ID,PAYMENT_TYPE,COMP_CODE,BRANCH_CODE)" +
                                       "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)"; 
                                       
                    //String isAutoGen = getCodeName("SELECT auto_generate_ID FROM IA_GB_COMPANY");
                    Connection con = null;
                    PreparedStatement ps = null;
                    PreparedStatement ps1 = null;
                    boolean autoCommit = false;
                    boolean done = false;
                    String id = helper.getGeneratedId("IA_SALES_PAYMENTS");
                    pymtCode = id;
                    //pymtCode = isAutoGen.equalsIgnoreCase("Y") ? id : pymtCode;
                    //pymtCode==familyID==id
                     int i,j,k;
                    
                    try {
                        con = getConnection();
                        autoCommit = con.getAutoCommit();
                        con.setAutoCommit(false);
                        ps = con.prepareStatement(pymtQuery);
                        ps.setString(1,pymtCode);
                        ps.setString(2,orderCode);
                        //ps.setString(3,customerCode);
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
						ps.setString(18,payername); 
						ps.setString(19,branchcode);
						ps.setDouble(20, amount);
                        i = ps.executeUpdate(); //add payment Details
                        
                        ps1 = con.prepareStatement(pymtHistQuery);
                        ps1.setString(1,drAcctType);
                        ps1.setString(2,drAcctNo);
                        ps1.setString(3,drTranCode);
                        ps1.setString(4,drNarration);
//                        ps1.setDouble(5,drAmount);
                        ps1.setString(5,Double.toString(drAmount));
                        ps1.setDate(6,dateConvert(effDate));
    //                    ps1.setInt(7,userId);
                        ps1.setString(7,Integer.toString(userId));
                        ps1.setDate(8,dateConvert(new java.util.Date()));
                        ps1.setString(9,pymtCode);
                        ps1.setString(10,pymtType);
						ps1.setString(11,companyCode);
						ps1.setString(12, branchcode);
                        ps1.addBatch(); //add first batch
                        //Second Batch Start
                        ps1.setString(1,crAcctType);
                        ps1.setString(2,crAcctNo);
                        ps1.setString(3,crTranCode);
                        ps1.setString(4,crNarration);
//                        ps1.setDouble(5,crAmount);
                        ps1.setString(5,Double.toString(crAmount));                        
                        ps1.setDate(6,dateConvert(effDate));
//                        ps1.setInt(7,userId);
                        ps1.setString(7,Integer.toString(userId));
                        ps1.setDate(8,dateConvert(new java.util.Date()));
                        ps1.setString(9,pymtCode);
                        ps1.setString(10,pymtType); 
						ps1.setString(11,companyCode);
						ps1.setString(12, branchcode);
                        ps1.addBatch(); //add second batch
                        j = (ps1.executeBatch()).length;
                        if((i != -1) && (j != -1))
                        {
                          con.commit();
                          con.setAutoCommit(autoCommit);
                          done = true;
                         }
                                           
                        }catch (Exception er){
                            System.out.println("ERROR Creating Sales Payment " + er.getMessage());
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
                            closeConnection(con, ps1);
                         }
                          return done;
                }
                                    
    public boolean updateCustomerBalance(String orderCode,double amountOwing,double amountPaid,int userId) {

                    String query = "";
                    String CREATE_QUERY = "INSERT INTO IA_SALES_CUSTOMER () "+
                                   "VALUES ()";
                
                     String UPDATE_QUERY = "UPDATE IA_SALES_CUSTOMER";
                    //String isAutoGen = getCodeName("SELECT auto_generate_ID FROM IA_GB_COMPANY");
                    
                    String customerCode = getCodeName("SELECT CUSTOMER_CODE FROM IA_SALES_ORDER WHERE SORDER_CODE='"+orderCode+"'");
                    Connection con = null;
                    PreparedStatement ps = null;
                   boolean done = false;
                   if(this.isRecordExisting("SELECT CUSTOMER_CODE FROM IA_SALES_CUSTOMER")){
                       try {
                               con = getConnection();
                               ps = con.prepareStatement(UPDATE_QUERY);
                               //ps.setString(1,pymtCode);
                               done = (ps.executeUpdate() != -1);
                               
                       } catch (Exception ex) {
                               System.out.println("ERROR Updating updateCustomerBalance().. " + ex.getMessage());
                               ex.printStackTrace();
                               done = false;
                       } finally {
                               closeConnection(con, ps);
                       }
                   }
                   else{
                       try {
                               con = getConnection();
                               ps = con.prepareStatement(CREATE_QUERY);
                               //ps.setString(1,pymtCode);
                               done = (ps.executeUpdate() != -1);
                               
                       } catch (Exception ex) {
                               System.out.println("ERROR Updating updateCustomerBalance().. " + ex.getMessage());
                               ex.printStackTrace();
                               done = false;
                       } finally {
                               closeConnection(con, ps);
                       }
                   }
                    
                    
                    return done;
            } 
            
    public boolean updateGLAccounts(String drAcct,String crAcct,double amount,String narration,String effDate,int userId) {
                   
                    boolean done = false;
                   
                    return done;
            }         
            
    public java.util.ArrayList<SalesPayment> findSalesOrdersPymtByQuery(String filter2,String filter) {
            java.util.ArrayList<SalesPayment> soList = new java.util.ArrayList<SalesPayment>();
            String criteria = " WHERE COMP_CODE = '"+filter2+"' AND MTID IS NOT NULL " + filter;
            soList = findSalesOrdersPymt(criteria);
            return soList;
    }
    private java.util.ArrayList<SalesPayment> findSalesOrdersPymt(String filter) {
                    java.util.ArrayList<SalesPayment> soList = new java.util.ArrayList<SalesPayment>();

                    SalesPayment sorder = null;

                    Connection con = null;
                    PreparedStatement ps = null;
                    ResultSet rs = null;

                    String query = "SELECT MTID,SORDER_CODE,PAYMENT_CODE,CUSTOMER_CODE,CHEQUE_NO,"+
                                   "BANK_CODE,AMOUNT,TRANS_DATE,AMOUNT_OWING,FAMILY_ID,MENU_OPTION,PAYER_NAME,"+
                                   "AMOUNT_PAID,ACCOUNT_NO,USERID,PAYMENT_TYPE,BANKER_CODE,DEPOSITOR,TELLER_NO,PROJECT_CODE "+
                                   "FROM IA_SALES_PAYMENTS ";
                   
                    String orderBy = " ORDER BY SORDER_CODE,PAYMENT_CODE DESC";                
                   
                    query += filter;
                    query += orderBy;
                    
                    try {

                            con = getConnection();
                            ps = con.prepareStatement(query);

                            rs = ps.executeQuery();

                            while (rs.next()) {
                                String id = rs.getString("MTID");
                                    String orderNo = rs.getString("SORDER_CODE");
                                    String pymtCode = rs.getString("PAYMENT_CODE");
                                    String customerCode = rs.getString("CUSTOMER_CODE");
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
                                    String familyId = rs.getString("FAMILY_ID");
                                    String payerName = rs.getString("PAYER_NAME");
                                    String menuPage = rs.getString("MENU_OPTION");
                                    sorder = new SalesPayment(id,pymtCode,orderNo,chequeNo,bankCode,transDate,
                                                              amountOwing,amountPaid,userId,pymtType,accountNo,customerCode,bankerCode,depositor,
                                                              tellerNo,projectCode,familyId,payerName,menuPage);
                                    soList.add(sorder);

                            }
                    }catch (Exception ex) {
                                            System.out.println("ERROR Querying Sales Payment " + ex.getMessage());
                                            ex.printStackTrace();
                                    } finally {
                                            closeConnection(con, ps,rs);
                                    }

                    return soList;
            }
    public SalesPayment findSalesPaymentById(String pymtCode){
           
        String FIND_QUERY = "SELECT PAYMENT_CODE,SORDER_CODE,CHEQUE_NO,BANK_CODE,TRANS_DATE,AMOUNT_OWING,FAMILY_ID,MENU_OPTION,PAYER_NAME,"+
                            "AMOUNT_PAID,USERID,PAYMENT_TYPE,MTID,ACCOUNT_NO,CUSTOMER_CODE,BANKER_CODE,DEPOSITOR,TELLER_NO,PROJECT_CODE "+
                            "FROM IA_SALES_PAYMENTS WHERE PAYMENT_CODE=? ";

        Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            SalesPayment salesPymt = null;

            try{
                    con = getConnection();
                    ps = con.prepareStatement(FIND_QUERY);
                    ps.setString(1,pymtCode);
                    rs = ps.executeQuery();
                    while(rs.next()){

             //String pymtCode = rs.getString("PAYMENT_CODE");
              String customerCode = rs.getString("CUSTOMER_CODE");
             String orderCode = rs.getString("SORDER_CODE");
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
            String familyId = rs.getString("FAMILY_ID");
            String menuPage = rs.getString("MENU_OPTION");
            String payerName = rs.getString("PAYER_NAME");
              salesPymt = new SalesPayment(mtId,pymtCode,orderCode,chequeNo,bankCode,transDate,amountOwing,
                                           amountPaid,userId,pymtType,accountNo,customerCode,bankerCode,depositor,
                                           tellerNo,projectCode,familyId,payerName,menuPage);
              
            
           }

            }catch(Exception er){
                    System.out.println("Error finding findSalesPaymentById ->"+er);
            }finally{
                    closeConnection(con,ps,rs);
            }
         return salesPymt;
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
        System.out.println("Error in ARServiceBus- getCodeName()... ->"+er);
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

             }catch(Exception er){
                     System.out.println("Error in isRecordExisting()... ->"+er);
                     er.printStackTrace();
             }finally{
                     closeConnection(con,ps);
             }

             return exists;
     } 
    public boolean createSalesPayment(String orderCode,String chequeNo,String bankCode,
            double amountOwing,double amountPaid,int userId,String pymtType,
            String accountNo,String customerCode,String bankerCode,String depositor,
            String tellerNo,String projectCode,String drAcctNo,String drAcctType,String drTranCode,
            String drNarration,double drAmount,String effDate,String crAcctNo,String crAcctType,
            String crTranCode,String crNarration,double crAmount,String branchcode,String companyCode,String payername,String menuPage,String transactionDate) {

String pymtQuery = "INSERT INTO IA_SALES_PAYMENTS (PAYMENT_CODE,SORDER_CODE,CHEQUE_NO,BANK_CODE,"+
         "TRANS_DATE,AMOUNT_OWING,AMOUNT_PAID,USERID,PAYMENT_TYPE,MTID,ACCOUNT_NO,CUSTOMER_CODE,"+
         "BANKER_CODE,DEPOSITOR,TELLER_NO,PROJECT_CODE,BRANCH_CODE,COMP_CODE,PAYER_NAME,MENU_OPTION,FAMILY_ID) "+
         "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

String pymtHistQuery = "INSERT INTO IA_PAYMENT_HISTORY (ACCT_TYPE,CUST_ACCT_NO,TRAN_CODE,"+
             "DESCRIPTION,AMT,EFFECTIVE_DT,USERID,CREATE_DT,FAMILY_ID,PAYMENT_TYPE,BRANCH_CODE,COMP_CODE)" +
             "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)"; 

String GLCRHistoryquery = "INSERT INTO IA_GL_HISTORY (BRANCH_NO,ACCT_TYPE,GL_ACCT_NO,TRAN_CODE,SBU_CODE,DES" +
"CRIPTION,AMT,REFERENCE,EFFECTIVE_DT,USERID,CREATE_DT,FAMILY_ID,currency_id,sell_" +
"act_exch_rate,sell_sys_exch_rate,MTID,sys_tran_code,orig_branch_no,Processed,COMP_CODE,OTHERACCTLEG)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
;       
/*
String GLDRHistoryquery = "INSERT INTO IA_GL_HISTORY (BRANCH_NO,ACCT_TYPE,GL_ACCT_NO,TRAN_CODE,SBU_CODE,DES" +
"CRIPTION,AMT,REFERENCE,EFFECTIVE_DT,USERID,CREATE_DT,FAMILY_ID,currency_id,sell_" +
"act_exch_rate,sell_sys_exch_rate,MTID,sys_tran_code,orig_branch_no,Processed,COMP_CODE,OTHERACCTLEG)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
;        */
//System.out.println("createSalesPayment pymtQuery:  "+pymtQuery);
//.out.println("createSalesPayment pymtHistQuery:  "+pymtHistQuery);
//System.out.println("createSalesPayment GLCRHistoryquery:  "+GLCRHistoryquery);
//System.out.println("createSalesPayment GLDRHistoryquery:  "+GLDRHistoryquery);
//String isAutoGen = getCodeName("SELECT auto_generate_ID FROM IA_GB_COMPANY");
Connection con = null;
PreparedStatement ps = null;
PreparedStatement ps1 = null;
PreparedStatement ps2 = null;
boolean autoCommit = false;
boolean done = false;
String id = helper.getGeneratedId("IA_SALES_PAYMENTS");
pymtCode = id;
String glid = helper.getGeneratedId("IA_GL_HISTORY");
//pymtCode = isAutoGen.equalsIgnoreCase("Y") ? id : pymtCode;
//pymtCode==familyID==id
int crCurrCode = 1;
double crAcctExchRate = 0.00;
double crSysExchRate = 0.00;
String crSBU = "1";
int drCurrCode = 1;
double drAcctExchRate = 0.00;
double drSysExchRate = 0.00;
String drSBU = "1";                    
int i,j,k,l;

try {
con = getConnection();
//autoCommit = con.getAutoCommit();
//con.setAutoCommit(false);
ps = con.prepareStatement(pymtQuery);
ps.setString(1,pymtCode);
ps.setString(2,orderCode);
//ps.setString(3,customerCode);
ps.setString(3,chequeNo);
ps.setString(4,bankCode);
ps.setDate(5,dateConvert(transactionDate));
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
ps.setString(17,branchcode);
ps.setString(18,companyCode);
ps.setString(19,payername);
ps.setString(20,menuPage);
ps.setString(21,glid);
i = ps.executeUpdate(); //add payment Details
ps1 = con.prepareStatement(pymtHistQuery);
ps1.setString(1,drAcctType);
ps1.setString(2,drAcctNo);
ps1.setString(3,drTranCode);
ps1.setString(4,drNarration);
ps1.setDouble(5,drAmount);
ps1.setDate(6,dateConvert(effDate));
ps1.setInt(7,userId);
ps1.setDate(8,dateConvert(transactionDate));
ps1.setString(9,glid);
ps1.setString(10,pymtType);
ps1.setString(11,branchcode);
ps1.setString(12,companyCode);
ps1.addBatch(); //add first batch
ps1.setString(1,crAcctType);
ps1.setString(2,crAcctNo);
ps1.setString(3,crTranCode);
ps1.setString(4,crNarration);
ps1.setDouble(5,crAmount);
ps1.setDate(6,dateConvert(effDate));
ps1.setInt(7,userId);
ps1.setDate(8,dateConvert(transactionDate));
ps1.setString(9,glid);
ps1.setString(10,pymtType);
ps1.setString(11,branchcode);
ps1.setString(12,companyCode);
ps1.addBatch(); //add second batch
j = (ps1.executeBatch()).length;

ps2 = con.prepareStatement(GLCRHistoryquery);
ps2.setString(1,branchcode);   
ps2.setString(2,crAcctType);  
ps2.setString(3,crAcctNo);   
ps2.setString(4,crTranCode);  
ps2.setString(5, crSBU);  
ps2.setString(6,crNarration);  
ps2.setDouble(7,crAmount); 
ps2.setString(8, orderCode);  
ps2.setDate(9,dateConvert(effDate));  
ps2.setInt(10,userId);    
ps2.setDate(11,dateConvert(transactionDate));   
ps2.setString(12,glid);  
ps2.setInt(13, crCurrCode);  
ps2.setDouble(14, crAcctExchRate); 
ps2.setDouble(15, crSysExchRate); 
ps2.setString(16, glid);         
ps2.setInt(17, Integer.parseInt(crTranCode));  
ps2.setString(18, branchcode);
ps2.setString(19, "N");
ps2.setString(20,companyCode);	    
ps2.setString(21,drAcctNo);	
ps2.addBatch(); //add Third batch
ps2.setString(1,branchcode);  
ps2.setString(2,drAcctType);  
ps2.setString(3,drAcctNo);   
ps2.setString(4,drTranCode);  
ps2.setString(5, drSBU);
ps2.setString(6,drNarration);  
ps2.setDouble(7,drAmount); 
ps2.setString(8, orderCode); 
ps2.setDate(9,dateConvert(effDate));  
ps2.setInt(10,userId); 
ps2.setDate(11,dateConvert(transactionDate));   
ps2.setString(12,glid);  
ps2.setInt(13, drCurrCode); 
ps2.setDouble(14, drAcctExchRate); 
ps2.setDouble(15, drSysExchRate); 
ps2.setString(16, glid);     
ps2.setInt(17, Integer.parseInt(drTranCode)); 
ps2.setString(18, branchcode);
ps2.setString(19, "N");
ps2.setString(20,companyCode);	
ps2.setString(21,crAcctNo);	
ps2.addBatch(); //add Fourth batch
k = (ps2.executeBatch()).length;
//System.out.println("<<<<<<i: " +i+"  <<<<<<<J: "+j+"<<<<<<K: "+k);

if((i != -1) && (j != -1) && (k != -1))
{
//con.commit();
//con.setAutoCommit(autoCommit);
done = true;
} 
                 
}catch (Exception er){
  System.out.println("ERROR Creating Sales Payment " + er.getMessage());
  er.printStackTrace();
/*   try{
       con.rollback();
       done = false;
   }
   catch(SQLException ex){
       System.out.println("Error In Transaction....Rollback ->"+er.getMessage());
   
   } */
}finally {
  closeConnection(con, ps);
  closeConnection(con, ps1);
  closeConnection(con, ps2);
}
return done;
}  
    
    public boolean createStudentHistory(String AppNo,String Branch, String AcctType, String AcctNo, String custType, String Narration, 
            double SumAmount, double AcctExchRate, double SysExchRate, String userId, String companyCode, String term, 
            double accountBalance, String tranCode, String familyid, String InventType,String receiptNo,String reference)
    {
        Connection con;
        PreparedStatement ps;
        boolean done;
        String familyID;
        String StudentHistQuery = "INSERT INTO IA_CUSTOMER_ACCOUNT_TRAN_HIST (MTID,ACCOUNT_TYPE,CUST_ACCOUNT_NO,TRAN_CODE,"+
        "DESCRIPTION,AMOUNT,EFFECTIVE_DATE,USERID,CREATE_DATE,FAMILY_ID,INV_ITEM_TYPE,BRANCH_CODE,COMP_CODE,TERM,STATUS,CUST_TYPE,BALANCE,POSTING_DATE,RECEIPT_NO,REFERENCE,ADMIN_NO)" +
        "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; 
   //     System.out.println("StudentHistQuery accountBalance=== "+accountBalance);
       // String isAutoGen = getCodeName("SELECT auto_generate_ID FROM IA_GB_COMPANY");
        con = null;
        ps = null;
        done = false;
        int i;
        try{  
//        String glid = helper.getGeneratedId("IA_GL_HISTORY");
        String pymtid = helper.getGeneratedId("IA_CUSTOMER_ACCOUNT_TRAN_HIST");
        con = getConnection();
        ps = con.prepareStatement(StudentHistQuery);
        ps.setString(1, pymtid);
        ps.setString(2,AcctType);
        ps.setString(3,AcctNo);
        ps.setString(4,tranCode);
        ps.setString(5,Narration);
        ps.setDouble(6,SumAmount);
        ps.setDate(7,dateConvert(new java.util.Date()));
//       System.out.println("userId: "+userId);
        ps.setInt(8,Integer.parseInt(userId));        
        ps.setDate(9,dateConvert(new java.util.Date()));
        ps.setString(10,familyid);
        ps.setString(11,InventType);
		ps.setString(12,Branch);
		ps.setString(13,companyCode);
		ps.setString(14,term);
		ps.setString(15,"ACTIVE");
		ps.setString(16,custType);
		double totalbal = SumAmount + accountBalance;
		ps.setDouble(17,totalbal);
        ps.setDate(18,dateConvert(new java.util.Date()));
        ps.setString(19, receiptNo);
        ps.setString(20, reference);
        ps.setString(21,AppNo);
		i = ps.executeUpdate();
        closeConnection(con, ps);
    	}catch(Exception ex){
        System.out.println((new StringBuilder()).append("ERROR Creating Transaction Posting in createStudentHistory.. ").append(ex.getMessage()).toString());
        ex.printStackTrace();
 //       done = false;
        closeConnection(con, ps);
    	} finally{
        closeConnection(con, ps);
    	}
        return done;
    }   
    
    public void updateCustomerDRBalances(String AcctNo, String BranchNo,double tranAmount) {
        Connection con = null;
        PreparedStatement ps = null;
   //     String BranchNo = HtmlUtilily.getCodeName("Select branch_code from MG_AD_Branch where branch_id = "+BranchId+"");
        String UPDATE_QUERY = "UPDATE IA_CUSTOMER_ACCOUNT_DISPLAY SET ACCOUNT_BALANCE= ACCOUNT_BALANCE - "+tranAmount+", CLEAR_BALANCE = CLEAR_BALANCE - "+tranAmount+",no_debits_ptd = no_debits_ptd + 1,amt_debits_ptd = amt_debits_ptd + "+tranAmount+" WHERE ACCOUNT_NO = '"+AcctNo+"' AND Branch_Code = '"+BranchNo+"' ";
//        String UPDATEProcess_QUERY = "UPDATE IA_GL_HISTORY SET Processed = 'Y' WHERE gl_acct_no = '"+AcctNo+"' AND Branch_code = "+BranchNo+" AND Processed = 'N' ";
  //      System.out.println("updateCustomerDRBalances UPDATE_QUERY: "+UPDATE_QUERY);
        try {
           // con = getConnection("eschool");
       	   con = getConnection();
            ps = con.prepareStatement(UPDATE_QUERY);                    
            ps.executeUpdate();
//            ps = con.prepareStatement(UPDATEProcess_QUERY); 
            ps.executeUpdate();
            
        } catch (Exception ex) {
            System.out.println("WARNING: cannot update IA_CUSTOMER_ACCOUNT_DISPLAY+" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
    }
    
    public boolean updateCustomerCRBalances(String AcctNo, String BranchNo,double tranAmount) {
        //String BranchNo = HtmlUtilily.getCodeName("Select branch_code from MG_AD_Branch where branch_id = "+BranchId+"");
        Connection con = null;
        PreparedStatement ps = null;
        String UPDATE_QUERY = "UPDATE IA_CUSTOMER_ACCOUNT_DISPLAY SET ACCOUNT_BALANCE= ACCOUNT_BALANCE + "+tranAmount+", CLEAR_BALANCE = CLEAR_BALANCE + "+tranAmount+",no_credits_ptd = no_credits_ptd + 1,amt_credits_ptd = amt_credits_ptd + "+tranAmount+" WHERE ACCOUNT_NO = '"+AcctNo+"' AND Branch_Code = '"+BranchNo+"' ";
 //       String UPDATEProcess_QUERY = "UPDATE IA_GL_HISTORY SET Processed = 'Y' WHERE gl_acct_no = '"+AcctNo+"' AND Branch_Code = "+BranchNo+" ";
   //     System.out.println("updateCustomerCRBalances UPDATE_QUERY: "+UPDATE_QUERY);
    //    System.out.println("tranAmount: "+tranAmount);
        boolean done;
        done = false;
        try {
           // con = getConnection("eschool");
        	con = getConnection();
            ps = con.prepareStatement(UPDATE_QUERY);         
            ps.executeUpdate();
 //           ps = con.prepareStatement(UPDATEProcess_QUERY);
 //           ps.executeUpdate();            
            done = true;
        } catch (Exception ex) {
            System.out.println("WARNING: cannot update IA_CUSTOMER_ACCOUNT_DISPLAY+" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;
    }
    public boolean createTithePayment(String orderCode,String chequeNo,String bankCode,
            double amountOwing,double amountPaid,int userId,String pymtType,
            String accountNo,String customerCode,String bankerCode,String depositor,
            String tellerNo,String projectCode,String drAcctNo,String drAcctType,String drTranCode,
            String drNarration,double drAmount,String effDate,String crAcctNo,String crAcctType,
            String crTranCode,String crNarration,double crAmount,String branchcode,
            String companyCode,String payername,double accountBalance,String gldrTranCode,
            String glcrTranCode,String menuPage, String customerAcct,String transactionDate) {

String pymtQuery = "INSERT INTO IA_SALES_PAYMENTS (PAYMENT_CODE,SORDER_CODE,CHEQUE_NO,BANK_CODE,"+
         "TRANS_DATE,AMOUNT_OWING,AMOUNT_PAID,USERID,PAYMENT_TYPE,MTID,ACCOUNT_NO,CUSTOMER_CODE,"+
         "BANKER_CODE,DEPOSITOR,TELLER_NO,PROJECT_CODE,BRANCH_CODE,COMP_CODE,PAYER_NAME,AMOUNT,FAMILY_ID,MENU_OPTION)"+
         "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

String pymtHistQuery = "INSERT INTO IA_PAYMENT_HISTORY (ACCT_TYPE,CUST_ACCT_NO,TRAN_CODE,"+
             "DESCRIPTION,AMT,SYS_TRAN_CODE,EFFECTIVE_DT,USERID,CREATE_DT,FAMILY_ID,PAYMENT_TYPE,BRANCH_CODE,COMP_CODE)" +
             "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)"; 

String GLCRHistoryquery = "INSERT INTO IA_GL_HISTORY (BRANCH_NO,ACCT_TYPE,GL_ACCT_NO,TRAN_CODE,SBU_CODE,DES" +
"CRIPTION,AMT,REFERENCE,EFFECTIVE_DT,USERID,CREATE_DT,FAMILY_ID,currency_id,sell_" +
"act_exch_rate,sell_sys_exch_rate,MTID,sys_tran_code,orig_branch_no,Processed,COMP_CODE,OTHERACCTLEG)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
;       

String StudentDRHistQuery = "INSERT INTO IA_CUSTOMER_ACCOUNT_TRAN_HIST (MTID,ACCOUNT_TYPE,CUST_ACCOUNT_NO,TRAN_CODE,"+
"DESCRIPTION,AMOUNT,EFFECTIVE_DATE,USERID,CREATE_DATE,INV_ITEM_TYPE,BRANCH_CODE,COMP_CODE,CUST_TYPE,BALANCE,POSTING_DATE,REFERENCE,SYS_TRAN_CODE,FAMILY_ID)" +
"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";   

String GLDRHistoryquery = "INSERT INTO IA_GL_HISTORY (BRANCH_NO,ACCT_TYPE,GL_ACCT_NO,TRAN_CODE,SBU_CODE,DES" +
"CRIPTION,AMT,REFERENCE,EFFECTIVE_DT,USERID,CREATE_DT,FAMILY_ID,currency_id,sell_" +
"act_exch_rate,sell_sys_exch_rate,MTID,sys_tran_code,orig_branch_no,Processed,COMP_CODE,OTHERACCTLEG)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
;       

Connection con = null;
PreparedStatement ps = null;
PreparedStatement ps1 = null;
PreparedStatement ps2 = null;
PreparedStatement ps3 = null;
PreparedStatement ps4 = null;
boolean autoCommit = false;
boolean done = false;
String id = helper.getGeneratedId("IA_SALES_PAYMENTS");
pymtCode = id;
String glid = helper.getGeneratedId("IA_GL_HISTORY");
//pymtCode = isAutoGen.equalsIgnoreCase("Y") ? id : pymtCode;
String pymtid = helper.getGeneratedId("IA_CUSTOMER_ACCOUNT_TRAN_HIST");
//pymtCode==familyID==id
System.out.println("<<<<<<glcrTranCode: "+glcrTranCode+"    gldrTranCode: "+gldrTranCode);
int crCurrCode = 1;
double crAcctExchRate = 0.00;
double crSysExchRate = 0.00;
String crSBU = "1";
int drCurrCode = 1;
double drAcctExchRate = 0.00;
double drSysExchRate = 0.00;
String drSBU = "1";                    
int i,j,k,l,m;
//drNarration = payername;
try {
con = getConnection();
autoCommit = con.getAutoCommit();
con.setAutoCommit(false);
ps = con.prepareStatement(pymtQuery);
ps.setString(1,pymtCode);
ps.setString(2,orderCode);
//ps.setString(3,customerCode);
ps.setString(3,chequeNo);
ps.setString(4,bankCode);
ps.setDate(5,dateConvert(transactionDate));
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
ps.setString(17,branchcode);
ps.setString(18,companyCode);
ps.setString(19,payername);
ps.setDouble(20,amountPaid);
ps.setString(21,glid); 
ps.setString(22,menuPage); 
i = ps.executeUpdate(); //add payment Details
ps1 = con.prepareStatement(pymtHistQuery);
ps1.setString(1,drAcctType);
ps1.setString(2,drAcctNo);
ps1.setString(3,gldrTranCode);
ps1.setString(4,drNarration);
ps1.setDouble(5,drAmount);
ps1.setString(6,drTranCode);
ps1.setDate(7,dateConvert(effDate));
ps1.setInt(8,userId);
ps1.setDate(9,dateConvert(transactionDate));
ps1.setString(10,glid);
ps1.setString(11,pymtType);
ps1.setString(12,branchcode);
ps1.setString(13,companyCode);
ps1.addBatch(); //add first batch
ps1.setString(1,crAcctType);
ps1.setString(2,crAcctNo);
ps1.setString(3,glcrTranCode);
ps1.setString(4,crNarration);
ps1.setDouble(5,crAmount);
ps1.setString(6,crTranCode);
ps1.setDate(7,dateConvert(effDate));
ps1.setInt(8,userId);
ps1.setDate(9,dateConvert(transactionDate));
ps1.setString(10,glid);
ps1.setString(11,pymtType);
ps1.setString(12,branchcode);
ps1.setString(13,companyCode);
ps1.addBatch(); //add second batch
j = (ps1.executeBatch()).length;

ps2 = con.prepareStatement(GLDRHistoryquery);
//ps.addBatch(); //add first batch
ps2.setString(1,branchcode);   
ps2.setString(2,drAcctType);  
ps2.setString(3,drAcctNo);   
ps2.setString(4,gldrTranCode);  
ps2.setString(5, drSBU);  
ps2.setString(6,drNarration);  
ps2.setDouble(7,drAmount); 
ps2.setString(8, orderCode);  
ps2.setDate(9,dateConvert(effDate));  
ps2.setInt(10,userId);    
ps2.setDate(11,dateConvert(transactionDate));   
ps2.setString(12,glid);  
ps2.setDouble(13, drAcctExchRate); 
ps2.setDouble(14, drSysExchRate); 
ps2.setDouble(15, drSysExchRate); 
ps2.setString(16, glid);         
ps2.setInt(17, Integer.parseInt(drTranCode));  
ps2.setString(18, branchcode);
ps2.setString(19, "N");
ps2.setString(20,companyCode);	   
ps2.setString(21,crAcctNo);	 
k = ps2.executeUpdate();

ps3 = con.prepareStatement(StudentDRHistQuery);
ps3.setString(1, pymtid);
ps3.setString(2,crAcctType);
ps3.setString(3,customerAcct);
ps3.setString(4,glcrTranCode);
ps3.setString(5,crNarration);
ps3.setDouble(6,crAmount);
ps3.setDate(7,dateConvert(transactionDate));
ps3.setInt(8,userId);        
ps3.setDate(9,dateConvert(transactionDate));
ps3.setString(10,pymtType);
ps3.setString(11,branchcode);
ps3.setString(12,companyCode);
ps3.setString(13,pymtType);
double totalbal = crAmount + accountBalance;
ps3.setDouble(14,totalbal);
ps3.setDate(15,dateConvert(transactionDate));
ps3.setString(16, glid);
ps3.setString(17,crTranCode);
ps3.setString(18,glid); 
l = ps3.executeUpdate();

ps4 = con.prepareStatement(GLCRHistoryquery);
//ps.addBatch(); //add first batch
ps4.setString(1,branchcode);   
ps4.setString(2,crAcctType);  
ps4.setString(3,crAcctNo);   
ps4.setString(4,glcrTranCode);  
ps4.setString(5, drSBU);  
ps4.setString(6,crNarration);  
ps4.setDouble(7,drAmount); 
ps4.setString(8, orderCode);  
ps4.setDate(9,dateConvert(effDate));  
ps4.setInt(10,userId);    
ps4.setDate(11,dateConvert(new java.util.Date()));   
ps4.setString(12,glid);  
ps4.setDouble(13, crAcctExchRate); 
ps4.setDouble(14, crSysExchRate); 
ps4.setDouble(15, crSysExchRate); 
ps4.setString(16, glid);         
ps4.setInt(17, Integer.parseInt(crTranCode));  
ps4.setString(18, branchcode);
ps4.setString(19, "N");
ps4.setString(20,companyCode);	   
ps4.setString(21,drAcctNo);	 
m = ps4.executeUpdate();

if((i != -1) && (j != -1) && (k != -1) && (l != -1) && (m != -1))
{
con.commit();
con.setAutoCommit(autoCommit);
done = true;
}
                 
}catch (Exception er){
  System.out.println("ERROR Creating Sales Payment " + er.getMessage());
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
  closeConnection(con, ps1);
  closeConnection(con, ps2);
  closeConnection(con, ps3);
  closeConnection(con, ps4);
}
return done;
}  
    public SalesPayment findPaymentById(String mtid){
        
        String FIND_QUERY = "SELECT PAYMENT_CODE,SORDER_CODE,CHEQUE_NO,BANK_CODE,TRANS_DATE,AMOUNT_OWING,FAMILY_ID,MENU_OPTION,PAYER_NAME,"+
                            "AMOUNT_PAID,USERID,PAYMENT_TYPE,MTID,ACCOUNT_NO,CUSTOMER_CODE,BANKER_CODE,DEPOSITOR,TELLER_NO,PROJECT_CODE "+
                            "FROM IA_SALES_PAYMENTS WHERE MTID = ? ";

        Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            SalesPayment salesPymt = null;

            try{   
                    con = getConnection();
                    ps = con.prepareStatement(FIND_QUERY);
                    ps.setString(1,mtid);
                    rs = ps.executeQuery();
                    while(rs.next()){

             //String pymtCode = rs.getString("PAYMENT_CODE");
              String customerCode = rs.getString("CUSTOMER_CODE");
             String orderCode = rs.getString("SORDER_CODE");
             String chequeNo = rs.getString("CHEQUE_NO");
             String bankCode = rs.getString("BANK_CODE");
             String transDate = formatDate(rs.getDate("TRANS_DATE"));
             double amountOwing = rs.getDouble("AMOUNT_OWING");
            double amountPaid = rs.getDouble("AMOUNT_PAID");
            int userId = rs.getInt("USERID");
            String pymtType = rs.getString("PAYMENT_TYPE");
            String mtId = rs.getString("MTID");
            String accountNo = rs.getString("ACCOUNT_NO");
            String bankerCode = rs.getString("BANKER_CODE");
            String depositor = rs.getString("DEPOSITOR");
            String tellerNo = rs.getString("TELLER_NO");
            String projectCode = rs.getString("PROJECT_CODE");
            String familyId = rs.getString("FAMILY_ID");
            String menuPage = rs.getString("MENU_OPTION");
            String payerName = rs.getString("PAYER_NAME");
              salesPymt = new SalesPayment(mtId,pymtCode,orderCode,chequeNo,bankCode,transDate,amountOwing,
                                           amountPaid,userId,pymtType,accountNo,customerCode,bankerCode,depositor,
                                           tellerNo,projectCode,familyId,payerName,menuPage);
              
            
           }

            }catch(Exception er){
                    System.out.println("Error finding findPaymentById ->"+er);
            }finally{
                    closeConnection(con,ps,rs);
            }
         return salesPymt;
    }    
    
    public ArrayList<SalesPayment> findPaymentLists(String filter) {
                    java.util.ArrayList<SalesPayment> transList = new java.util.ArrayList<SalesPayment>();

                    SalesPayment salesPymt = null;
                    Connection con = null;
                    PreparedStatement ps = null;
                    ResultSet rs = null;

                    String FIND_QUERY = "SELECT PAYMENT_CODE,SORDER_CODE,CHEQUE_NO,BANK_CODE,TRANS_DATE,AMOUNT_OWING,FAMILY_ID,MENU_OPTION,PAYER_NAME,"+
                            "AMOUNT_PAID,USERID,PAYMENT_TYPE,MTID,ACCOUNT_NO,CUSTOMER_CODE,BANKER_CODE,DEPOSITOR,TELLER_NO,PROJECT_CODE "+
                            "FROM IA_SALES_PAYMENTS ";
                                                    
                    FIND_QUERY += filter;
                              System.out.println("findPaymentLists FIND_QUERY:"+FIND_QUERY);
                    try {
                            con = getConnection();
                            ps = con.prepareStatement(FIND_QUERY);

                            rs = ps.executeQuery();

                            while (rs.next()) {
                                    String customerCode = rs.getString("CUSTOMER_CODE");
                                    String orderCode = rs.getString("SORDER_CODE");
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
                                    String familyId = rs.getString("FAMILY_ID");
                                    String menuPage = rs.getString("MENU_OPTION");
                                    String payerName = rs.getString("PAYER_NAME");
                                     salesPymt = new SalesPayment(mtId,pymtCode,orderCode,chequeNo,bankCode,transDate,amountOwing,
                                                                  amountPaid,userId,pymtType,accountNo,customerCode,bankerCode,depositor,
                                                                  tellerNo,projectCode,familyId,payerName,menuPage);  
                                     transList.add(salesPymt);
                            }
                    }catch (Exception ex) {
                                            System.out.println("ERROR Querying Payment Transactions. " + ex.getMessage());
                                            ex.printStackTrace();
                                    } finally {
                                            closeConnection(con, ps,rs);
                                    }
                  return transList;
        }
    public boolean createTitheReverser(String orderCode,String chequeNo,String bankCode,
            double amountOwing,double amountPaid,int userId,String pymtType,
            String accountNo,String customerCode,String bankerCode,String depositor,
            String tellerNo,String projectCode,String drAcctNo,String drAcctType,String drTranCode,
            String drNarration,double drAmount,String effDate,String crAcctNo,String crAcctType,
            String crTranCode,String crNarration,double crAmount,String branchcode,String companyCode,
            String payername,double accountBalance,String gldrTranCode,String glcrTranCode,String familyId,
            String menuPage, String customerAcct) {

String pymtQuery = "INSERT INTO IA_SALES_PAYMENTS (PAYMENT_CODE,SORDER_CODE,CHEQUE_NO,BANK_CODE,"+
         "TRANS_DATE,AMOUNT_OWING,AMOUNT_PAID,USERID,PAYMENT_TYPE,MTID,ACCOUNT_NO,CUSTOMER_CODE,"+
         "BANKER_CODE,DEPOSITOR,TELLER_NO,PROJECT_CODE,BRANCH_CODE,COMP_CODE,PAYER_NAME,AMOUNT,FAMILY_ID,MENU_OPTION)"+
         "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

String pymtHistQuery = "INSERT INTO IA_PAYMENT_HISTORY (ACCT_TYPE,CUST_ACCT_NO,TRAN_CODE,"+
             "DESCRIPTION,AMT,SYS_TRAN_CODE,EFFECTIVE_DT,USERID,CREATE_DT,FAMILY_ID,PAYMENT_TYPE,BRANCH_CODE,COMP_CODE,MTID)" +
             "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; 

String GLCRHistoryquery = "INSERT INTO IA_GL_HISTORY (BRANCH_NO,ACCT_TYPE,GL_ACCT_NO,TRAN_CODE,SBU_CODE,DES" +
"CRIPTION,AMT,REFERENCE,EFFECTIVE_DT,USERID,CREATE_DT,FAMILY_ID,currency_id,sell_" +
"act_exch_rate,sell_sys_exch_rate,MTID,sys_tran_code,orig_branch_no,Processed,COMP_CODE,OTHERACCTLEG)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
;       

String StudentDRHistQuery = "INSERT INTO IA_CUSTOMER_ACCOUNT_TRAN_HIST (MTID,ACCOUNT_TYPE,CUST_ACCOUNT_NO,TRAN_CODE,"+
"DESCRIPTION,AMOUNT,EFFECTIVE_DATE,USERID,CREATE_DATE,INV_ITEM_TYPE,BRANCH_CODE,COMP_CODE,CUST_TYPE,BALANCE,POSTING_DATE,REFERENCE,SYS_TRAN_CODE,FAMILY_ID)" +
"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";   

String GLDRHistoryquery = "INSERT INTO IA_GL_HISTORY (BRANCH_NO,ACCT_TYPE,GL_ACCT_NO,TRAN_CODE,SBU_CODE,DES" +
"CRIPTION,AMT,REFERENCE,EFFECTIVE_DT,USERID,CREATE_DT,FAMILY_ID,currency_id,sell_" +
"act_exch_rate,sell_sys_exch_rate,MTID,sys_tran_code,orig_branch_no,Processed,COMP_CODE,OTHERACCTLEG)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
;       

Connection con = null;
PreparedStatement ps = null;
PreparedStatement ps1 = null;
PreparedStatement ps2 = null;
PreparedStatement ps3 = null;
PreparedStatement ps4 = null;
boolean autoCommit = false;
boolean done = false;
String id = helper.getGeneratedId("IA_SALES_PAYMENTS");
pymtCode = id;
String glid = helper.getGeneratedId("IA_GL_HISTORY");
String pymthist = helper.getGeneratedId("IA_PAYMENT_HISTORY");
String pymtid = helper.getGeneratedId("IA_CUSTOMER_ACCOUNT_TRAN_HIST");
//pymtCode==familyID==id
System.out.println("<<<<<<glcrTranCode: "+glcrTranCode+"    gldrTranCode: "+gldrTranCode);
int crCurrCode = 1;
double crAcctExchRate = 0.00;
double crSysExchRate = 0.00;
String crSBU = "1";
int drCurrCode = 1;
double drAcctExchRate = 0.00;
double drSysExchRate = 0.00;
String drSBU = "1";                    
int i,j,k,l,m;
//drNarration = payername;
try {
con = getConnection();
autoCommit = con.getAutoCommit();
con.setAutoCommit(false);
ps = con.prepareStatement(pymtQuery);
ps.setString(1,pymtCode);
ps.setString(2,orderCode);
//ps.setString(3,customerCode);
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
ps.setString(17,branchcode);
ps.setString(18,companyCode);
ps.setString(19,payername);
ps.setDouble(20,amountPaid);
ps.setString(21,familyId); 
ps.setString(22,menuPage); 
i = ps.executeUpdate(); //add payment Details
ps1 = con.prepareStatement(pymtHistQuery);
ps1.setString(1,drAcctType);
ps1.setString(2,drAcctNo);
ps1.setString(3,glcrTranCode);
ps1.setString(4,drNarration);
ps1.setDouble(5,drAmount);
ps1.setString(6,crTranCode);
ps1.setDate(7,dateConvert(effDate));
ps1.setInt(8,userId);
ps1.setDate(9,dateConvert(new java.util.Date()));
ps1.setString(10,familyId);
ps1.setString(11,pymtType);
ps1.setString(12,branchcode);
ps1.setString(13,companyCode);
ps1.setString(14,pymthist);
ps1.addBatch(); //add first batch
ps1.setString(1,crAcctType);
ps1.setString(2,crAcctNo);
ps1.setString(3,gldrTranCode);
ps1.setString(4,crNarration);
ps1.setDouble(5,crAmount);
ps1.setString(6,drTranCode);
ps1.setDate(7,dateConvert(effDate));
ps1.setInt(8,userId);
ps1.setDate(9,dateConvert(new java.util.Date()));
ps1.setString(10,familyId);
ps1.setString(11,pymtType);
ps1.setString(12,branchcode);
ps1.setString(13,companyCode);
ps1.setString(14,pymthist);
ps1.addBatch(); //add second batch
j = (ps1.executeBatch()).length;

ps2 = con.prepareStatement(GLDRHistoryquery);
//ps.addBatch(); //add first batch
ps2.setString(1,branchcode);   
ps2.setString(2,drAcctType);  
ps2.setString(3,drAcctNo);   
ps2.setString(4,glcrTranCode);  
ps2.setString(5, drSBU);  
ps2.setString(6,drNarration);  
ps2.setDouble(7,drAmount); 
ps2.setString(8, orderCode);  
ps2.setDate(9,dateConvert(effDate));  
ps2.setInt(10,userId);    
ps2.setDate(11,dateConvert(new java.util.Date()));   
ps2.setString(12,familyId);  
ps2.setDouble(13, drAcctExchRate); 
ps2.setDouble(14, drSysExchRate); 
ps2.setDouble(15, drSysExchRate); 
ps2.setString(16, glid);         
ps2.setInt(17, Integer.parseInt(crTranCode));  
ps2.setString(18, branchcode);
ps2.setString(19, "N");
ps2.setString(20,companyCode);	   
ps2.setString(21,crAcctNo);	 
k = ps2.executeUpdate();

ps3 = con.prepareStatement(StudentDRHistQuery);
ps3.setString(1, pymtid);
ps3.setString(2,crAcctType);
ps3.setString(3,customerAcct);
ps3.setString(4,gldrTranCode);
ps3.setString(5,crNarration);
ps3.setDouble(6,crAmount);
ps3.setDate(7,dateConvert(new java.util.Date()));
ps3.setInt(8,userId);        
ps3.setDate(9,dateConvert(new java.util.Date()));
ps3.setString(10,pymtType);
ps3.setString(11,branchcode);
ps3.setString(12,companyCode);
ps3.setString(13,pymtType);
double totalbal = crAmount + accountBalance;
ps3.setDouble(14,totalbal);
ps3.setDate(15,dateConvert(new java.util.Date()));
ps3.setString(16, glid);
ps3.setString(17,drTranCode);
ps3.setString(18,familyId); 
l = ps3.executeUpdate();

ps4 = con.prepareStatement(GLCRHistoryquery);
//ps.addBatch(); //add first batch
ps4.setString(1,branchcode);   
ps4.setString(2,crAcctType);  
ps4.setString(3,crAcctNo);   
ps4.setString(4,gldrTranCode);  
ps4.setString(5, drSBU);  
ps4.setString(6,crNarration);  
ps4.setDouble(7,drAmount); 
ps4.setString(8, orderCode);  
ps4.setDate(9,dateConvert(effDate));  
ps4.setInt(10,userId);    
ps4.setDate(11,dateConvert(new java.util.Date()));   
ps4.setString(12,familyId);  
ps4.setDouble(13, crAcctExchRate);   
ps4.setDouble(14, crSysExchRate); 
ps4.setDouble(15, crSysExchRate); 
ps4.setString(16, glid);         
ps4.setInt(17, Integer.parseInt(drTranCode));  
ps4.setString(18, branchcode);
ps4.setString(19, "N");
ps4.setString(20,companyCode);	   
ps4.setString(21,drAcctNo);	 
m = ps4.executeUpdate();

if((i != -1) && (j != -1) && (k != -1) && (l != -1) && (m != -1))
{
con.commit();
con.setAutoCommit(autoCommit);
done = true;
}
                 
}catch (Exception er){
  System.out.println("ERROR Creating Sales Payment " + er.getMessage());
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
  closeConnection(con, ps1);
  closeConnection(con, ps2);
  closeConnection(con, ps3);
  closeConnection(con, ps4);
}
return done;
}       

    public boolean createSalesPaymentReversal(String orderCode,String chequeNo,String bankCode,
            double amountOwing,double amountPaid,int userId,String pymtType,
            String accountNo,String customerCode,String bankerCode,String depositor,
            String tellerNo,String projectCode,String drAcctNo,String drAcctType,String drTranCode,
            String drNarration,double drAmount,String effDate,String crAcctNo,String crAcctType,
            String crTranCode,String crNarration,double crAmount,String branchcode,String companyCode,String payername,String menuPage,String familyid,String transactionDate) {

String pymtQuery = "INSERT INTO IA_SALES_PAYMENTS (PAYMENT_CODE,SORDER_CODE,CHEQUE_NO,BANK_CODE,"+
         "TRANS_DATE,AMOUNT_OWING,AMOUNT_PAID,USERID,PAYMENT_TYPE,MTID,ACCOUNT_NO,CUSTOMER_CODE,"+
         "BANKER_CODE,DEPOSITOR,TELLER_NO,PROJECT_CODE,BRANCH_CODE,COMP_CODE,PAYER_NAME,MENU_OPTION,FAMILY_ID) "+
         "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

String pymtHistQuery = "INSERT INTO IA_PAYMENT_HISTORY (ACCT_TYPE,CUST_ACCT_NO,TRAN_CODE,"+
             "DESCRIPTION,AMT,EFFECTIVE_DT,USERID,CREATE_DT,FAMILY_ID,PAYMENT_TYPE,BRANCH_CODE,COMP_CODE)" +
             "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)"; 

String GLCRHistoryquery = "INSERT INTO IA_GL_HISTORY (BRANCH_NO,ACCT_TYPE,GL_ACCT_NO,TRAN_CODE,SBU_CODE,DES" +
"CRIPTION,AMT,REFERENCE,EFFECTIVE_DT,USERID,CREATE_DT,FAMILY_ID,currency_id,sell_" +
"act_exch_rate,sell_sys_exch_rate,MTID,sys_tran_code,orig_branch_no,Processed,COMP_CODE)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
;       

String GLDRHistoryquery = "INSERT INTO IA_GL_HISTORY (BRANCH_NO,ACCT_TYPE,GL_ACCT_NO,TRAN_CODE,SBU_CODE,DES" +
"CRIPTION,AMT,REFERENCE,EFFECTIVE_DT,USERID,CREATE_DT,FAMILY_ID,currency_id,sell_" +
"act_exch_rate,sell_sys_exch_rate,MTID,sys_tran_code,orig_branch_no,Processed,COMP_CODE)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
;        
//System.out.println("createSalesPayment pymtQuery:  "+pymtQuery);
//.out.println("createSalesPayment pymtHistQuery:  "+pymtHistQuery);
//System.out.println("createSalesPayment GLCRHistoryquery:  "+GLCRHistoryquery);
//System.out.println("createSalesPayment GLDRHistoryquery:  "+GLDRHistoryquery);
//String isAutoGen = getCodeName("SELECT auto_generate_ID FROM IA_GB_COMPANY");
Connection con = null;
PreparedStatement ps = null;
boolean autoCommit = false;
boolean done = false;
String id = helper.getGeneratedId("IA_SALES_PAYMENTS");
pymtCode = id;
String glid = helper.getGeneratedId("IA_GL_HISTORY");
//pymtCode = isAutoGen.equalsIgnoreCase("Y") ? id : pymtCode;
//pymtCode==familyID==id
int crCurrCode = 1;
double crAcctExchRate = 0.00;
double crSysExchRate = 0.00;
String crSBU = "1";
int drCurrCode = 1;
double drAcctExchRate = 0.00;
double drSysExchRate = 0.00;
String drSBU = "1";                    
int i,j,k;

try {
con = getConnection();
autoCommit = con.getAutoCommit();
con.setAutoCommit(false);
ps = con.prepareStatement(pymtQuery);
ps.setString(1,pymtCode);
ps.setString(2,orderCode);
//ps.setString(3,customerCode);
ps.setString(3,chequeNo);
ps.setString(4,bankCode);
ps.setDate(5,dateConvert(transactionDate));
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
ps.setString(17,branchcode);
ps.setString(18,companyCode);
ps.setString(19,payername);
ps.setString(20,menuPage);
ps.setString(21,familyid);
i = ps.executeUpdate(); //add payment Details
ps = con.prepareStatement(pymtHistQuery);
ps.setString(1,drAcctType);
ps.setString(2,drAcctNo);
ps.setString(3,crTranCode);
ps.setString(4,drNarration);
ps.setDouble(5,drAmount);
ps.setDate(6,dateConvert(effDate));
ps.setInt(7,userId);
ps.setDate(8,dateConvert(transactionDate));
ps.setString(9,familyid);
ps.setString(10,pymtType);
ps.setString(11,branchcode);
ps.setString(12,companyCode);
ps.addBatch(); //add first batch
ps.setString(1,crAcctType);
ps.setString(2,crAcctNo);
ps.setString(3,drTranCode);
ps.setString(4,crNarration);
ps.setDouble(5,crAmount);
ps.setDate(6,dateConvert(effDate));
ps.setInt(7,userId);
ps.setDate(8,dateConvert(transactionDate));
ps.setString(9,familyid);
ps.setString(10,pymtType);
ps.setString(11,branchcode);
ps.setString(12,companyCode);
ps.addBatch(); //add second batch
j = (ps.executeBatch()).length;

ps = con.prepareStatement(GLCRHistoryquery);
//ps.addBatch(); //add first batch
ps.setString(1,branchcode);   
ps.setString(2,crAcctType);  
ps.setString(3,crAcctNo);   
ps.setString(4,drTranCode);  
ps.setString(5, crSBU);  
ps.setString(6,crNarration);  
ps.setDouble(7,crAmount); 
ps.setString(8, orderCode);  
ps.setDate(9,dateConvert(effDate));  
ps.setInt(10,userId);    
ps.setDate(11,dateConvert(transactionDate));   
ps.setString(12,familyid);  
ps.setInt(13, crCurrCode);  
ps.setDouble(14, crAcctExchRate); 
ps.setDouble(15, crSysExchRate); 
ps.setString(16, glid);         
ps.setInt(17, Integer.parseInt(drTranCode));  
ps.setString(18, branchcode);
ps.setString(19, "N");
ps.setString(20,companyCode);	        	        
k = ps.executeUpdate();

ps = con.prepareStatement(GLDRHistoryquery);
//ps.addBatch(); //add first batch
ps.setString(1,branchcode);   
ps.setString(2,drAcctType);  
ps.setString(3,drAcctNo);   
ps.setString(4,crTranCode);  
ps.setString(5, drSBU);  
ps.setString(6,drNarration);  
ps.setDouble(7,drAmount); 
ps.setString(8, orderCode);  
ps.setDate(9,dateConvert(effDate));  
ps.setInt(10,userId);    
ps.setDate(11,dateConvert(transactionDate));   
ps.setString(12,familyid);  
ps.setInt(13, drCurrCode);  
ps.setDouble(14, drAcctExchRate); 
ps.setDouble(15, drSysExchRate); 
ps.setString(16, glid);         
ps.setInt(17, Integer.parseInt(crTranCode));  
ps.setString(18, branchcode);
ps.setString(19, "N");
ps.setString(20,companyCode);	        	        
k = ps.executeUpdate();

if((i != -1) && (j != -1) && (k != -1))
{
con.commit();
con.setAutoCommit(autoCommit);
done = true;
}
                 
}catch (Exception er){
  System.out.println("ERROR Creating Sales Payment " + er.getMessage());
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
