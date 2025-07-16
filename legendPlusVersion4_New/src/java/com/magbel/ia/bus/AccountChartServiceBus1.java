package com.magbel.ia.bus;

import com.magbel.ia.dao.PersistenceServiceDAO;
import com.magbel.ia.util.ApplicationHelper;
import com.magbel.ia.vao.GLAccount;
import com.magbel.ia.vao.GLHistory;
import com.magbel.ia.vao.PaymentHistory;
import com.magbel.ia.vao.SalesOrder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;

public class AccountChartServiceBus1 extends PersistenceServiceDAO{
    public String id;
    ApplicationHelper helper;
    public String pymtCode;
    public AccountChartServiceBus1() {
        helper = new ApplicationHelper();
    }
    public java.util.ArrayList<GLAccount> findGLAccount(String filter) 
    {
      java.util.ArrayList<GLAccount> list = new java.util.ArrayList<GLAccount>();
      GLAccount glacct = null;
      Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      String query = "SELECT LEDGER_NO,GL_ACCT_NO,DESCRIPTION FROM ia_gl_acct_ledger";
      query += filter;
      
       try 
       {
        con = getConnection();
        ps = con.prepareStatement(query);
        rs = ps.executeQuery();
         while (rs.next()) 
         {
         String ledgerNo = rs.getString("LEDGER_NO");
         String glAcctNo = rs.getString("GL_ACCT_NO");
         String glAcctDesc = rs.getString("DESCRIPTION");
         GLAccount glAcct = new GLAccount(ledgerNo,glAcctNo,glAcctDesc);
         list.add(glAcct);
         }
       }
       catch (Exception ex) 
       {
        System.out.println("ERROR finding GL Account.. " + ex.getMessage());
        ex.printStackTrace();
       } 
       finally 
       {
        closeConnection(con, ps,rs);
       }
       return list;
     }
     
  public boolean createGL2GLPosting(String drBranch,int drCurrCode,String drAcctType,String drAcctNo,String drTranCode,
                                    String drSBU,String drNarration,double drAmount,String drReference,double drAcctExchRate,
                                    double drSysExchRate,String crBranch,int crCurrCode,String crAcctType,String crAcctNo,String crTranCode,String crSBU,
                                    String crNarration,double crAmount,double crAcctExchRate,double crSysExchRate,String effDate,int userId) 
  {

     String query = "INSERT INTO IA_GL_HISTORY (BRANCH_NO,ACCT_TYPE,GL_ACCT_NO,TRAN_CODE,SBU_CODE,"+
                   "DESCRIPTION,AMT,REFERENCE,EFFECTIVE_DT,USERID,CREATE_DT,FAMILY_ID,currency_id,"+
                   "sell_act_exch_rate,sell_sys_exch_rate,mtid)"+
                   "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                
     String isAutoGen = getCodeName("SELECT auto_generate_ID FROM IA_GB_COMPANY");
     Connection con = null;
     PreparedStatement ps = null;
     boolean done = false;
     String id = helper.getGeneratedId("IA_GL_HISTORY");
     String familyID = id;//equivalent 2 familyID
        System.out.println("Family Id  >> "+familyID);                
     try 
     {  
       con = getConnection();
       ps = con.prepareStatement(query);
       ps.setString(1,drBranch);
       ps.setString(2,drAcctType);
       ps.setString(3,drAcctNo);
       ps.setString(4,drTranCode);
       ps.setString(5,drSBU);
       ps.setString(6,drNarration);
       ps.setDouble(7,drAmount);
       ps.setString(8,drReference);
       ps.setDate(9,dateConvert(effDate));
       ps.setInt(10,userId);
       ps.setDate(11,dateConvert(new java.util.Date()));
       ps.setString(12,familyID);
       ps.setInt(13,drCurrCode);
       ps.setDouble(14,drAcctExchRate);
       ps.setDouble(15,drSysExchRate);
       ps.addBatch(); //add first batch
       ps.setString(1,crBranch);
       ps.setString(2,crAcctType);
       ps.setString(3,crAcctNo);
       ps.setString(4,crTranCode);
       ps.setString(5,crSBU);
       ps.setString(6,crNarration);
       ps.setDouble(7,drAmount);
       ps.setString(8,drReference);
       ps.setDate(9,dateConvert(effDate));
       ps.setInt(10,userId);
       ps.setDate(11,dateConvert(new java.util.Date()));
       ps.setString(12,familyID);
       ps.setInt(13,crCurrCode);
       ps.setDouble(14,crAcctExchRate);
       ps.setDouble(15,crSysExchRate);
       ps.setString(16,id);
       ps.addBatch(); //add second batch
      done = (ps.executeBatch().length != -1);
      } 
      catch (Exception ex) 
      {
       System.out.println("ERROR Creating GL2GL Posting.. " + ex.getMessage());
       ex.printStackTrace();
       done = false;
      }finally 
       {
        closeConnection(con, ps);
       }
       return done;
      }
    public java.util.ArrayList<GLHistory> findGLHistory(String filter) 
    {
      java.util.ArrayList<GLHistory> list = new java.util.ArrayList<GLHistory>();
      GLAccount glacct = null;
      Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      String query = "SELECT BRANCH_NO,ACCT_TYPE,GL_ACCT_NO,TRAN_CODE,SBU_CODE,DESCRIPTION,AMT,"+
                     "REFERENCE,EFFECTIVE_DT,USERID,CREATE_DT,FAMILY_ID,CURRENCY_ID,sell_act_exch_rate,sell_sys_exch_rate "+
                     "FROM IA_GL_HISTORY WHERE MTID IS NOT NULL ";
      query += filter;
      
       try 
       {
        con = getConnection();
        ps = con.prepareStatement(query);
        rs = ps.executeQuery();
         while (rs.next()) 
         {
          String branchCode = rs.getString("BRANCH_NO");
          String acctType = rs.getString("ACCT_TYPE");
          String acctNo = rs.getString("GL_ACCT_NO");
          String tranCode = rs.getString("TRAN_CODE");
          String sbu = rs.getString("SBU_CODE");
          String narration = rs.getString("DESCRIPTION");
          double amount = rs.getDouble("AMT");
          String reference = rs.getString("REFERENCE");
          String effDate = formatDate(rs.getDate("EFFECTIVE_DT"));
          int userId = rs.getInt("USERID");
          String transDate = formatDate(rs.getDate("CREATE_DT"));
          String familyCode = rs.getString("FAMILY_ID");
          String currCode = rs.getString("CURRENCY_ID");
          double acctExchRate = rs.getDouble("sell_act_exch_rate");
          double sysExchRate = rs.getDouble("sell_sys_exch_rate");
                    
         GLHistory hist = new GLHistory(branchCode,acctType,acctNo,tranCode,sbu,narration,amount,reference,effDate,
                                       userId,transDate,familyCode,currCode,acctExchRate,sysExchRate);
         list.add(hist);
         }
       }
       catch (Exception ex) 
       {
        System.out.println("ERROR finding GL2GLPost... " + ex.getMessage());
        ex.printStackTrace();
       } 
       finally 
       {
        closeConnection(con, ps,rs);
       }
       return list;
     }
    public java.util.ArrayList<PaymentHistory> findPaymentHistory(String filter) 
    {
      java.util.ArrayList<PaymentHistory> list = new java.util.ArrayList<PaymentHistory>();
      GLAccount glacct = null;
      Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      String query = "SELECT BRANCH_NO,ACCT_TYPE,GL_ACCT_NO,TRAN_CODE,SBU_CODE,DESCRIPTION,AMT,"+
                     "EFFECTIVE_DT,USERID,CREATE_DT,FAMILY_ID,CURRENCY_ID,sell_act_exch_rate,sell_sys_exch_rate, "+
                     "PAYMENT_TYPE FROM IA_PAYMENT_HISTORY WHERE MTID IS NOT NULL ";
      query += filter;
      
       try 
       {
        con = getConnection();
        ps = con.prepareStatement(query);
        rs = ps.executeQuery();
         while (rs.next()) 
         {
          String branchCode = rs.getString("BRANCH_NO");
          String acctType = rs.getString("ACCT_TYPE");
          String acctNo = rs.getString("CUST_ACCT_NO");
          String tranCode = rs.getString("TRAN_CODE");
          String sbu = rs.getString("SBU_CODE");
          String narration = rs.getString("DESCRIPTION");
          double amount = rs.getDouble("AMT");
          String reference = "";//rs.getString("REFERENCE");
          String effDate = formatDate(rs.getDate("EFFECTIVE_DT"));
          int userId = rs.getInt("USERID");
          String transDate = formatDate(rs.getDate("CREATE_DT"));
          String familyCode = rs.getString("FAMILY_ID");
          String currCode = rs.getString("CURRENCY_ID");
          double acctExchRate = rs.getDouble("sell_act_exch_rate");
          double sysExchRate = rs.getDouble("sell_sys_exch_rate");
          String pymtType = rs.getString("PAYMENT_TYPE");
                    
         PaymentHistory hist = new PaymentHistory(currCode,acctType,acctNo,tranCode,narration,amount,reference,
                                   acctExchRate,sysExchRate,branchCode,effDate,userId,familyCode,pymtType);
         list.add(hist);
         }
       }
       catch (Exception ex) 
       {
        System.out.println("ERROR finding PaymentHistory... " + ex.getMessage());
        ex.printStackTrace();
       } 
       finally 
       {
        closeConnection(con, ps,rs);
       }
       return list;
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
        System.out.println("Error in AccountChartServiceBus- getCodeName()... ->"+er);
        er.printStackTrace();
     }finally{
        closeConnection(con,ps);
      }   
      return result;
     }
    public String getAccountTranType(String tranCode)
    {
     String result = "";
     Connection con = null;
     ResultSet rs = null; 
     PreparedStatement ps = null;
     String query = "SELECT DEBIT_CREDIT FROM IA_AD_TRAN_CODE WHERE TRAN_CODE=?";
     try
     {
      con = getConnection();
      ps = con.prepareStatement(query);
      ps.setString(1,tranCode);
      rs = ps.executeQuery();
      while(rs.next())
      {
       result = rs.getString(1) == null ? "" : rs.getString(1);
      }
     }
     catch(Exception er)
     {
        System.out.println("Error in AccountChartServiceBus- get Transaction Type... ->"+er);
        er.printStackTrace();
     }finally{
        closeConnection(con,ps);
      }   
      return result;
     }
}
