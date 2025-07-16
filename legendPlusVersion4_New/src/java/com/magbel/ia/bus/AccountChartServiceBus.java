package com.magbel.ia.bus;

import com.magbel.ia.dao.PersistenceServiceDAO;
import com.magbel.ia.util.ApplicationHelper;
import com.magbel.ia.vao.*;
import com.magbel.util.HtmlUtilily;

import java.io.PrintStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class AccountChartServiceBus extends PersistenceServiceDAO
{

    public String id;
    ApplicationHelper helper;
    HtmlUtilily htmlUtil;
    public String pymtCode;

    public AccountChartServiceBus()
    {
        helper = new ApplicationHelper();
        htmlUtil = new HtmlUtilily();
    }

    public ArrayList<GLAccount> findGLAccount(String filter)
    {
        ArrayList<GLAccount> list;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        String query;
        list = new ArrayList<GLAccount>();
        GLAccount glacct = null;
        con = null;
        ps = null;
        rs = null;
        try {
        query = "SELECT LEDGER_NO,GL_ACCT_NO,DESCRIPTION FROM ia_gl_acct_ledger";
        query = (new StringBuilder()).append(query).append(filter).toString();
        con = getConnection();
        ps = con.prepareStatement(query);
        GLAccount glAcct;
//        System.out.println("findGLAccount query: "+query);
        for(rs = ps.executeQuery(); rs.next(); list.add(glAcct))
        {
            String ledgerNo = rs.getString("LEDGER_NO");
            String glAcctNo = rs.getString("GL_ACCT_NO");
            String glAcctDesc = rs.getString("DESCRIPTION");
            glAcct = new GLAccount(ledgerNo, glAcctNo, glAcctDesc);
        }

        closeConnection(con, ps, rs);
	} catch(Exception ex){
        System.out.println((new StringBuilder()).append("ERROR finding GL Account.. ").append(ex.getMessage()).toString());
        ex.printStackTrace();
        closeConnection(con, ps, rs);
	} finally {
        closeConnection(con, ps, rs);
	}
        return list;
    }

    public boolean createGL2GLPosting(String drBranch, int drCurrCode, String drAcctType, String drAcctNo, String drTranCode, String drSBU, String drNarration, 
            double drAmount, String drReference, double drAcctExchRate, double drSysExchRate, 
            String crBranch, int crCurrCode, String crAcctType, String crAcctNo, String crTranCode, String crSBU, String crNarration, 
            double crAmount, double crAcctExchRate, double crSysExchRate, String effDate, 
            int userId,String companyCode, String transDate)
    {
        String query;
        Connection con;
        PreparedStatement ps;
        boolean done;
        String familyID;
        String branchCode  =  htmlUtil.getCodeName("SELECT BRANCH_CODE FROM MG_AD_Branch WHERE BRANCH_ID = "+drBranch+"");
        query = "INSERT INTO IA_GL_HISTORY (BRANCH_NO,ACCT_TYPE,GL_ACCT_NO,TRAN_CODE,SBU_CODE,DES" +
"CRIPTION,AMT,REFERENCE,EFFECTIVE_DT,USERID,CREATE_DT,FAMILY_ID,currency_id,sell_" +
"act_exch_rate,sell_sys_exch_rate,MTID,sys_tran_code,orig_branch_no,BRANCH_CODE,COMP_CODE) " +
"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String isAutoGen = htmlUtil.getCodeName("SELECT auto_generate_ID FROM IA_GB_COMPANY");
        con = null;
        ps = null;
        done = false;
        try{  
        String id = helper.getGeneratedId("IA_GL_HISTORY");
        familyID = id;
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, drBranch);
        ps.setString(2, drAcctType);
        ps.setString(3, drAcctNo);
        ps.setString(4, drTranCode);
        ps.setString(5, drSBU);
        ps.setString(6, drNarration);
        ps.setDouble(7, drAmount);
        ps.setString(8, drReference);
        ps.setDate(9, dateConvert(effDate));
        ps.setInt(10, userId);
        ps.setDate(11, dateConvert(transDate));
        ps.setString(12, familyID);
        ps.setInt(13, drCurrCode);
        ps.setDouble(14, drAcctExchRate);
        ps.setDouble(15, drSysExchRate);
        ps.setString(16, id);
        ps.setInt(17, Integer.parseInt(drTranCode));
        ps.setString(18, drBranch);
        ps.setString(19, branchCode);
        ps.setString(20, companyCode);
        ps.addBatch();
        ps.setString(1, crBranch);
        ps.setString(2, crAcctType);
        ps.setString(3, crAcctNo);
        ps.setString(4, crTranCode);
        ps.setString(5, crSBU);
        ps.setString(6, crNarration);
        ps.setDouble(7, drAmount);
        ps.setString(8, drReference);
        ps.setDate(9, dateConvert(effDate));
        ps.setInt(10, userId);
        ps.setDate(11, dateConvert(transDate));
        ps.setString(12, familyID);
        ps.setInt(13, crCurrCode);
        ps.setDouble(14, crAcctExchRate);
        ps.setDouble(15, crSysExchRate);
        ps.setString(16, id);
        ps.setInt(17, Integer.parseInt(crTranCode));
        ps.setString(18, crBranch); 
        ps.setString(19, branchCode);
        ps.setString(20, companyCode);
        ps.addBatch();
        done = ps.executeBatch().length != -1;
        closeConnection(con, ps);
    	}catch(Exception ex){
        System.out.println((new StringBuilder()).append("ERROR Creating GL2GL Posting.. ").append(ex.getMessage()).toString());
        ex.printStackTrace();
        done = false;
        closeConnection(con, ps);
    	} finally{
        closeConnection(con, ps);
    	}
        return done;
    } 

    public ArrayList<GLHistory> findGLHistory(String filter)
    {
        ArrayList<GLHistory> list;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        String query;
        list = new ArrayList<GLHistory>();
        GLAccount glacct = null;
        con = null;
        ps = null;
        rs = null;
        try{
        query = "SELECT BRANCH_NO,ACCT_TYPE,GL_ACCT_NO,TRAN_CODE,SBU_CODE,DESCRIPTION,AMT,REFEREN" +
"CE,EFFECTIVE_DT,USERID,CREATE_DT,FAMILY_ID,CURRENCY_ID,sell_act_exch_rate,sell_s" +
"ys_exch_rate FROM IA_GL_HISTORY WHERE MTID IS NOT NULL "
;
        query = (new StringBuilder()).append(query).append(filter).toString();
        con = getConnection();
        ps = con.prepareStatement(query);
        GLHistory hist;
        for(rs = ps.executeQuery(); rs.next(); list.add(hist))
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
            hist = new GLHistory(branchCode, acctType, acctNo, tranCode, sbu, narration, amount, reference, effDate, userId, transDate, familyCode, currCode, acctExchRate, sysExchRate);
        }

        closeConnection(con, ps, rs);
	}catch(Exception ex){
        System.out.println((new StringBuilder()).append("ERROR finding GL2GLPost... ").append(ex.getMessage()).toString());
        ex.printStackTrace();
        closeConnection(con, ps, rs);
	}finally {
        closeConnection(con, ps, rs);
}
        return list;
    }

    public ArrayList<PaymentHistory> findPaymentHistory(String filter)
    {
        ArrayList<PaymentHistory> list;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        String query;
        list = new ArrayList<PaymentHistory>();
        GLAccount glacct = null;
        con = null;
        ps = null;
        rs = null;
        try{
        query = "SELECT BRANCH_NO,ACCT_TYPE,GL_ACCT_NO,TRAN_CODE,SBU_CODE,DESCRIPTION,AMT,EFFECTI" +
"VE_DT,USERID,CREATE_DT,FAMILY_ID,CURRENCY_ID,sell_act_exch_rate,sell_sys_exch_ra" +
"te, PAYMENT_TYPE FROM IA_PAYMENT_HISTORY WHERE MTID IS NOT NULL "
;
        query = (new StringBuilder()).append(query).append(filter).toString();
        con = getConnection();
        ps = con.prepareStatement(query);
        PaymentHistory hist;
        for(rs = ps.executeQuery(); rs.next(); list.add(hist))
        {
            String branchCode = rs.getString("BRANCH_NO");
            String acctType = rs.getString("ACCT_TYPE");
            String acctNo = rs.getString("CUST_ACCT_NO");
            String tranCode = rs.getString("TRAN_CODE");
            String sbu = rs.getString("SBU_CODE");
            String narration = rs.getString("DESCRIPTION");
            double amount = rs.getDouble("AMT");
            String reference = "";
            String effDate = formatDate(rs.getDate("EFFECTIVE_DT"));
            int userId = rs.getInt("USERID");
            String transDate = formatDate(rs.getDate("CREATE_DT"));
            String familyCode = rs.getString("FAMILY_ID");
            String currCode = rs.getString("CURRENCY_ID");
            double acctExchRate = rs.getDouble("sell_act_exch_rate");
            double sysExchRate = rs.getDouble("sell_sys_exch_rate");
            String pymtType = rs.getString("PAYMENT_TYPE");
            hist = new PaymentHistory(currCode, acctType, acctNo, tranCode, narration, amount, reference, acctExchRate, sysExchRate, branchCode, effDate, userId, familyCode, pymtType);
        }

        closeConnection(con, ps, rs);
	}catch(Exception ex){
        System.out.println((new StringBuilder()).append("ERROR finding PaymentHistory... ").append(ex.getMessage()).toString());
        ex.printStackTrace();
        closeConnection(con, ps, rs);
	}finally {
        closeConnection(con, ps, rs);
	}
        return list;
    }

    public String getCodeName(String query)
    {
        String result;
        Connection con;
        PreparedStatement ps;
        result = "";
        con = null;
   //     ResultSet rs = null;
        ps = null;
        try{
        con = getConnection();
        ps = con.prepareStatement(query);
        for(ResultSet rs = ps.executeQuery(); rs.next();)
        {
            result = rs.getString(1) != null ? rs.getString(1) : "";
        }

        closeConnection(con, ps);
	}catch(Exception er){
        System.out.println((new StringBuilder()).append("Error in AccountChartServiceBus- getCodeName()... ->").append(er).toString());
        er.printStackTrace();
        closeConnection(con, ps);
	}finally {
        closeConnection(con, ps);
	}
        return result;
    }

    public String getAccountTranType(String tranCode)
    {
        String result;
        Connection con;
        PreparedStatement ps;
        String query;
        result = "";
        con = null;
   //     ResultSet rs = null;
        ps = null;
        try{
        query = "SELECT DEBIT_CREDIT FROM IA_AD_TRAN_CODE WHERE TRAN_CODE=?";
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, tranCode);
        for(ResultSet rs = ps.executeQuery(); rs.next();)
        {
            result = rs.getString(1) != null ? rs.getString(1) : "";
        }

        closeConnection(con, ps);
	}catch(Exception er){
        System.out.println((new StringBuilder()).append("Error in AccountChartServiceBus- get Transaction Type... ->").append(er).toString());
        er.printStackTrace();
        closeConnection(con, ps);
    }finally{
        closeConnection(con, ps);
    }
        return result;
    }

    public ArrayList<Project> findGlProject(String filter)
    {
        ArrayList<Project> list;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        String query;
        list = new ArrayList<Project>();
        Project glacct = null;
        con = null;
        ps = null;
        rs = null;
        try{
        query = "SELECT * FROM IA_GL_PROJECT";
        query = (new StringBuilder()).append(query).append(filter).toString();
        con = getConnection();
        ps = con.prepareStatement(query);
        Project glAcct;
        for(rs = ps.executeQuery(); rs.next(); list.add(glAcct))
        {
            String id = rs.getString("MTID");
            String code = rs.getString("CODE");
            String desc = rs.getString("DESCRIPTION");
            String cCode = rs.getString("CUSTOMER_CODE");
            String startDt = rs.getString("START_DATE");
            String endDt = rs.getString("END_DATE");
            String cost = rs.getString("COST");
            String capital = rs.getString("CAPITAL");
            String other = rs.getString("OTHERS");
            String transDt = rs.getString("TRANS_DATE");
            String status = rs.getString("STATUS");
            glAcct = new Project(id, code, desc, cCode, startDt, endDt, cost, capital, other, transDt, status);
        }
  
        closeConnection(con, ps, rs);
	}catch(Exception ex){
        System.out.println((new StringBuilder()).append("ERROR finding GL Account.. ").append(ex.getMessage()).toString());
        ex.printStackTrace();
        closeConnection(con, ps, rs);
    }finally {
        closeConnection(con, ps, rs);
    }
        return list;
    }

    public ArrayList<GLAccount> findNoteAccount(String filter)
    {
        ArrayList<GLAccount> list;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        String query;
        list = new ArrayList<GLAccount>();
        GLAccount glacct = null;
        con = null;
        ps = null;
        rs = null;
        try {
        query = "SELECT ledger_no,description,LEVEL_NO FROM ia_gl_Totaling_Account WHERE LEVEL_NO='3' AND COMP_CODE = '001'";
        query = (new StringBuilder()).append(query).append(filter).toString();
        con = getConnection();
        ps = con.prepareStatement(query);
        GLAccount glAcct;
        for(rs = ps.executeQuery(); rs.next(); list.add(glAcct))
        {
            String levelNo = rs.getString("LEVEL_NO");
            String ledgerNo = rs.getString("ledger_no");
            String glAcctDesc = rs.getString("DESCRIPTION");
            glAcct = new GLAccount(ledgerNo, levelNo, glAcctDesc);
        }

        closeConnection(con, ps, rs);
	} catch(Exception ex){
        System.out.println((new StringBuilder()).append("ERROR finding GL Totalling Account.. ").append(ex.getMessage()).toString());
        ex.printStackTrace();
        closeConnection(con, ps, rs);
	} finally {
        closeConnection(con, ps, rs);
	}
        return list;
    }   
    public boolean insertBalancesForCloseDay(String branchCode,String companyCode,String termenddate)
    {
        Connection con;
        PreparedStatement ps;
        PreparedStatement ps2;
        PreparedStatement ps3;
        boolean done;
        boolean done2;
        boolean done3;
        String query;
        con = null;
        ps = null;
        ps2 = null;
        ps3 = null;
        done = false;
        String closedate = formatDate(dateConvert(new Date()));
        String closeDay = termenddate.substring(0,2);
        String closeMonth = termenddate.substring(3,5);
        String closeYear = termenddate.substring(6,10);
  //      System.out.println("closedate: "+closedate+" closeDay: "+closeDay+" closeMonth: "+closeMonth+" closeYear: "+closeYear);
        query = "INSERT INTO IA_GL_MONTHLY_BAL SELECT gl_acct_no,Branch_Code,period_end_dt,user_id,create_dt,current_bal," +
        		"end_bal,avg_bal,last_year_end_bal,budget_amt,begin_bal,begin_bal_last_per,no_debits_ptd,no_credits_ptd," +
        		"amt_debits_ptd,amt_credits_ptd,avg_bal_ytd,crncy_id,no_debits_qtd,no_credits_qtd,amt_debits_qtd," +
        		"amt_credits_qtd,avg_bal_qtd,ptd_bal,qtd_bal,tran_code,amt_dr_today,no_dr_today,amt_dr_mtd,no_dr_mtd," +
        		"avg_balance_mtd,COMP_CODE,FIRST_QRT_BAL,SECOND_QRT_BAL,THIRD_QRT_BAL,FOURTH_QRT_BAL,"+
        		"'"+termenddate+"','"+closeDay+"','"+closeMonth+"','"+closeYear+"' FROM IA_GL_BALANCES WHERE BRANCH_CODE = '"+branchCode+"' AND COMP_CODE = '"+companyCode+"'";
   //     System.out.println("insertBalancesForCloseDay query: "+query);
        try
        { 
            con = getConnection();
            ps = con.prepareStatement(query);
           // ps.setString(1, schoolCode);
            done = ps.executeUpdate() != -1;        
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder(String.valueOf(getClass().getName()))).append(" ERROR:Error inserting Monthly Balances in Monthly Table->").append(e.getMessage()).toString());
            e.printStackTrace();
        }
        finally {  
			   closeConnection(con, ps);  
		}
        return done;
    }    

    public String DeleteSessionRecords(String schoolCode,String companyCode)
    {
        String result;
        Connection con;
        PreparedStatement ps;
        result = "";
        boolean done;
        boolean done2;
        boolean done3;        
        con = null;
   //     ResultSet rs = null;
        ps = null;
        try{
       
        String query = "DELETE FROM IA_STUDENT_PAYMENT  WHERE BRANCH_CODE = '"+schoolCode+"' AND COMP_CODE = '"+companyCode+"' ";
        String query2 = "DELETE FROM IA_NEW_CUSTOMER  WHERE SCHOOL_CODE = '"+schoolCode+"' AND COMP_CODE = '"+companyCode+"' ";        
        String query3 = "DELETE FROM IA_STUDENT_SCORED  WHERE SCHOOL = '"+schoolCode+"' AND COMP_CODE = '"+companyCode+"' ";
        String query4 = "DELETE FROM IA_STUDENT_RESULT  WHERE SCHOOL = '"+schoolCode+"' AND COMP_CODE = '"+companyCode+"' ";
        con = getConnection();
        ps = con.prepareStatement(query);
        done = ps.executeUpdate() != -1; 
        closeConnection(con, ps);
        ps = con.prepareStatement(query2);
        done = ps.executeUpdate() != -1;  
        closeConnection(con, ps);
        ps = con.prepareStatement(query3);
        done = ps.executeUpdate() != -1; 
        closeConnection(con, ps);
        ps = con.prepareStatement(query4);
        done = ps.executeUpdate() != -1;         
        closeConnection(con, ps);
	}catch(Exception er){
        System.out.println((new StringBuilder()).append("Error in Deleting Records... ->").append(er).toString());
        er.printStackTrace();
        closeConnection(con, ps);
    }finally{
        closeConnection(con, ps);
    }
        return result;
    }   
    public boolean updateQrtBalance(String companyCode, String branchCode,String qtr) {
   	 String query = "";
   	String query1 = "";
   	String query2 = "";
 //  	 System.out.println("QUARTER NAME in updateQrtBalance: "+qtr);
   	 if(qtr.equalsIgnoreCase("FIRST")){
   		 query = "UPDATE IA_GL_BALANCES SET FIRST_QRT_BAL = coalesce(FIRST_QRT_BAL,0) + CURRENT_BAL WHERE COMP_CODE = '"+companyCode+"' AND BRANCH_CODE = '"+branchCode+"'";   
         query1 = "UPDATE IA_CUSTOMER_ACCOUNT_DISPLAY SET FIRST_QRT_BAL = coalesce(FIRST_QRT_BAL,0) + ACCOUNT_BALANCE WHERE STATUS = 'ACTIVE'  AND COMP_CODE = '"+companyCode+"' "; 
         query2 = "UPDATE IA_CUSTOMER_ACCOUNT_SETUP SET FIRST_QRT_BAL = coalesce(FIRST_QRT_BAL,0) + ACCOUNT_BALANCE WHERE STATUS = 'ACTIVE'  AND COMP_CODE = '"+companyCode+"' ";   		 
   	 }
   	 if(qtr.equalsIgnoreCase("SECOND")){
   		 query = "UPDATE IA_GL_BALANCES SET SECOND_QRT_BAL = coalesce(SECOND_QRT_BAL,0) + CURRENT_BAL WHERE COMP_CODE = '"+companyCode+"' AND BRANCH_CODE = '"+branchCode+"'";
         query1 = "UPDATE IA_CUSTOMER_ACCOUNT_DISPLAY SET SECOND_QRT_BAL = coalesce(SECOND_QRT_BAL,0) + ACCOUNT_BALANCE WHERE STATUS = 'ACTIVE'  AND COMP_CODE = '"+companyCode+"' "; 
         query2 = "UPDATE IA_CUSTOMER_ACCOUNT_SETUP SET SECOND_QRT_BAL = coalesce(SECOND_QRT_BAL,0) + ACCOUNT_BALANCE WHERE STATUS = 'ACTIVE'  AND COMP_CODE = '"+companyCode+"' ";   		 
   	 }
   	 if(qtr.equalsIgnoreCase("THIRD")){
   		 query = "UPDATE IA_GL_BALANCES SET THIRD_QRT_BAL = coalesce(THIRD_QRT_BAL,0) + CURRENT_BAL WHERE COMP_CODE = '"+companyCode+"' AND BRANCH_CODE = '"+branchCode+"'";
         query1 = "UPDATE IA_CUSTOMER_ACCOUNT_DISPLAY SET THIRD_QRT_BAL = coalesce(THIRD_QRT_BAL,0) + ACCOUNT_BALANCE WHERE STATUS = 'ACTIVE'  AND COMP_CODE = '"+companyCode+"' "; 
         query2 = "UPDATE IA_CUSTOMER_ACCOUNT_SETUP SET THIRD_QRT_BAL = coalesce(THIRD_QRT_BAL,0) + ACCOUNT_BALANCE WHERE STATUS = 'ACTIVE'  AND COMP_CODE = '"+companyCode+"' ";   		 
   	 }
   	 if(qtr.equalsIgnoreCase("FOURTH")){
   		 query = "UPDATE IA_GL_BALANCES SET FOURTH_QRT_BAL = coalesce(FOURTH_QRT_BAL,0) + CURRENT_BAL WHERE COMP_CODE = '"+companyCode+"' AND BRANCH_CODE = '"+branchCode+"'";
         query1 = "UPDATE IA_CUSTOMER_ACCOUNT_DISPLAY SET FOURTH_QRT_BAL = coalesce(FOURTH_QRT_BAL,0) + ACCOUNT_BALANCE WHERE STATUS = 'ACTIVE'  AND COMP_CODE = '"+companyCode+"' "; 
         query2 = "UPDATE IA_CUSTOMER_ACCOUNT_SETUP SET FOURTH_QRT_BAL = coalesce(FOURTH_QRT_BAL,0) + ACCOUNT_BALANCE WHERE STATUS = 'ACTIVE'  AND COMP_CODE = '"+companyCode+"' ";   		 
   	 }  
          Connection con = null;
          PreparedStatement ps = null;
          PreparedStatement ps1 = null;
          PreparedStatement ps2 = null;
          boolean done = false;  
          boolean done1 = false;  
          boolean done2 = false;  
 //         System.out.println("updateQrtBalance query: "+query);
//          System.out.println("updateQrtBalance query1: "+query1);
//          System.out.println("updateQrtBalance query2: "+query2);
          try { 
                  con = getConnection();
                  ps = con.prepareStatement(query);   
                  done = (ps.executeUpdate() != -1);      
                  ps1 = con.prepareStatement(query1);   
                  done1 = (ps1.executeUpdate() != -1);       
                  ps2 = con.prepareStatement(query2);   
                  done2 = (ps2.executeUpdate() != -1);       
          } catch (Exception ex) { 
                  done = true;
                  done1 = true;
                  done2 = true;
                  System.out.println("ERROR Updating Balances per Quarter " + ex.getMessage());
                  ex.printStackTrace();
          } finally {
                  closeConnection(con, ps);
                  closeConnection(con, ps1);
                  closeConnection(con, ps2);
          }
          return done;
   }
    public boolean createMonthCloseRecord(String companyCode,String branchCode, String Month, String qtr,
            int userId,String termenddate,int lastcustomertotal)
    {
        String closemonthquery;
        Connection con;
        PreparedStatement ps;
        boolean done;
//        String familyID;
          
        closemonthquery = "INSERT INTO MG_GB_GL_CLOSED (MTID,COMP_CODE,BRANCH_CODE,QUARTER," +
"MONTH,USERID,CLOSE_DATE,CREATE_DATE,LAST_CUSTOMER_ACTIVE)VALUES (?,?,?,?,?,?,?,?,?)"
;       
        con = null;
        ps = null;
        done = false;
        int j;
        String closedate = formatDate(dateConvert(new Date()));
        String closeDay = closedate.substring(0,2);
        String closeMonth = closedate.substring(3,5);
        String closeYear = closedate.substring(6,10);
        try{  
        String glid = helper.getGeneratedId("MG_GB_GL_CLOSED");
        con = getConnection();
        done = con.getAutoCommit();
        con.setAutoCommit(false);
        done = false;
        ps = con.prepareStatement(closemonthquery);
        ps.setInt(1, Integer.parseInt(glid));
        ps.setString(2, companyCode);
        ps.setString(3, branchCode);
        ps.setString(4, qtr);
        ps.setString(5, closeMonth);
        ps.setInt(6, userId);
        ps.setDate(7, dateConvert(termenddate));
        ps.setDate(8, dateConvert(new Date())); 
        ps.setInt(9, lastcustomertotal);
        j = ps.executeUpdate();        
     
     if(j != -1)
     {
       con.commit();
       con.setAutoCommit(done);
       done = true;
      }
        closeConnection(con, ps);
    	}catch(Exception ex){
        System.out.println((new StringBuilder()).append("ERROR Entering Record in Close table.. ").append(ex.getMessage()).toString());
        ex.printStackTrace();
        done = false;
        closeConnection(con, ps);
    	} finally{
        closeConnection(con, ps);
    	}
        return done;
    }   
    public boolean UpdateCloseBranch(String companyCode, String branchCode)
    {
        String query;
        Connection con;
        PreparedStatement ps;
        boolean done;        
//        System.out.println("adminNo:   "+adminNo+"   markscore:  "+markscore);
        String UpdateTotal = "UPDATE MG_AD_BRANCH SET CLOSE_STATUS = 'CLOSED' WHERE COMPANY_CODE = '"+companyCode+"' AND BRANCH_CODE = '"+branchCode+"' ";
     //   System.out.println("UpdateCloseSchool UpdateTotal:   "+UpdateTotal);
        con = null;
        ps = null;
        PreparedStatement ps1 = null;
        done = false;
        
        try{          
        con = getConnection();
            ps1 = con.prepareStatement(UpdateTotal);
            ps1.executeUpdate();    
            done = true;     
        closeConnection(con, ps);
    	}catch(Exception ex){
        System.out.println((new StringBuilder()).append("ERROR Updating GL Status.. ").append(ex.getMessage()).toString());
        ex.printStackTrace();
        done = false;
        closeConnection(con, ps);
    	} finally{
        closeConnection(con, ps); 
    	}
        return done;
    }     
    
    public boolean createYearlyCalendar(String schoolCode,String companyCode,String termenddate)
    {
        Connection con;
        PreparedStatement ps;
        PreparedStatement ps2;
        PreparedStatement ps3;
        boolean done;
        boolean done2;
        boolean done3;
        String query;
        con = null;
        ps = null;
        ps2 = null;
        ps3 = null;
        done = false;
        String currentdate = formatDate(dateConvert(new Date()));
        String currentYear = termenddate.substring(7,11);
        String closedate = formatDate(dateConvert(new Date()));
        String closeDay = termenddate.substring(0,2);
        String closeMonth = termenddate.substring(4,6);
        String closeYear = termenddate.substring(7,11);
        String startdate = currentYear+'-'+"01"+'-'+"01";
        String endDate = currentYear+'-'+"12"+'-'+"31";
//        System.out.println("closedate: "+closedate+" closeDay: "+closeDay+" closeMonth: "+closeMonth+" closeYear: "+closeYear);
        query = "drop table MG_GB_CALENDAR " +
        		"declare @start_date datetime, @end_date datetime, @cur_date datetime " +
        		"set @start_date = '"+startdate+"' " +
        		"set @end_date = '"+endDate+"' " +
        		"set @cur_date = @start_date " +
        		"create table MG_GB_CALENDAR " +
        		"(weekday varchar(10), date int,month varchar(10),year int, Public_Holiday varchar(15), " +
        		"Process_Status char(1),Calendar_Date datetime,GL_Closing_Status char(1),CloseDate datetime) " +
        		"while @cur_date <= @end_date " +
        		"begin " +
        		"insert into MG_GB_CALENDAR " +
        		"select datename(dw, @cur_date), datepart(day, @cur_date), datename(month, @cur_date), datepart(year, @cur_date),'' AS Public_Holiday, 'N' AS Process_Status, @cur_date AS Calendar_Date,'N' AS GL_Closing_Status," +
        		"(--how to find last sunday of a given month or date " +
        		"SELECT DATEADD(dd, -DATEPART(WEEKDAY, DATEADD(dd, -DAY(DATEADD(mm, 1, @cur_date)), DATEADD(mm, 1, @cur_date))) + 1, " +
        		"DATEADD(dd, -DAY(DATEADD(mm, 1, @cur_date)), DATEADD(mm, 1, @cur_date)))) AS CloseDate " +
        		"set @cur_date = dateadd(dd, 1, @cur_date) " +
        		"update MG_GB_CALENDAR set Public_Holiday = 'Yes' where weekday = 'Saturday' or weekday = 'Sunday' or (month = 'December' and (date = '25' or date = '26')) or (month = 'January' and date = '1') " +
        		"UPDATE MG_GB_CALENDAR set GL_Closing_Status = 'Y' WHERE CALENDAR_DATE  = CloseDate " +
        		"end ";
//        System.out.println("createYearlyCalendar query: "+query);
        try
        {
            con = getConnection();
            ps = con.prepareStatement(query);
           // ps.setString(1, schoolCode);
            done = ps.executeUpdate() != -1;        
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder(String.valueOf(getClass().getName()))).append(" ERROR:Error creating calendar table and records->").append(e.getMessage()).toString());
            e.printStackTrace();
        }
        finally {  
			   closeConnection(con, ps);  
		}
        return done;
    }    

    public java.util.ArrayList<Result> getBranchList()
    {
    	java.util.ArrayList<Result> list2 = new java.util.ArrayList<Result>();
    	String query = " SELECT  * from MG_AD_Branch where CLOSE_STATUS ='ACTIVE' ";
    	Connection c = null;
    	ResultSet rs = null;
    	Statement s = null;
    	try {
    		    c = getConnection();  
    			s = c.createStatement();
    			rs = s.executeQuery(query);
    			while (rs.next())
    			   {				
    				String branch = rs.getString("BRANCH_CODE");
    				String Description = rs.getString("BRANCH_NAME");
 //   				String positionbyarm = rs.getString("POSITION_BY_ARM");
    				Result Result = new Result();
    				Result.setBranch(branch);
    				Result.setDescription(Description);
//    				Result.setPositionbyarm(positionbyarm);
    				list2.add(Result);
    			   }
    	 }
    				 catch (Exception e)
    					{
    						e.printStackTrace();
    					}
    					finally
    					{
    						closeConnection(c, s, rs);
    					}
    	return list2;
    }
    
    public boolean databasebackup(String backupfile,String databaseName)
    {
        Connection con;
        PreparedStatement ps;
        boolean done;
        boolean done2;
        boolean done3;
        String query;
        con = null;   
        ps = null;
        done = false;
        query = "USE "+databaseName+" "+
				"BACKUP DATABASE "+databaseName+"  "+
				"TO DISK = '"+backupfile+"' "+  
				"WITH FORMAT, "+  
				"NAME = 'Full Backup of "+databaseName+" ' ";
 //       System.out.println("databasebackup query: "+query);
        try
        { 
            con = getConnection();
            ps = con.prepareStatement(query);
            done = ps.executeUpdate() != -1;        
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder(String.valueOf(getClass().getName()))).append(" ERROR:Error backing up the IAS database->").append(e.getMessage()).toString());
            e.printStackTrace();
        }
        finally {  
			   closeConnection(con, ps);  
		}
        return done;
    }    
    
    public boolean UpdateStartBranch(String companyCode, String branchCode)
    {
        String query;
        Connection con;
//        PreparedStatement ps;
        boolean done;        
//        System.out.println("adminNo:   "+adminNo+"   markscore:  "+markscore);
        String UpdateTotal = "UPDATE MG_AD_BRANCH SET CLOSE_STATUS = 'ACTIVE' WHERE COMPANY_CODE = '"+companyCode+"' AND BRANCH_CODE = '"+branchCode+"' ";
     //   System.out.println("UpdateCloseSchool UpdateTotal:   "+UpdateTotal);
        con = null;
        PreparedStatement ps1 = null;
        done = false;
        
        try{          
        con = getConnection();
            ps1 = con.prepareStatement(UpdateTotal);
            ps1.executeUpdate();    
            done = true;     
        closeConnection(con, ps1);
    	}catch(Exception ex){
        System.out.println((new StringBuilder()).append("ERROR Updating Branch in GL Status.. ").append(ex.getMessage()).toString());
        ex.printStackTrace();
        done = false;
        closeConnection(con, ps1);
    	} finally{
        closeConnection(con, ps1); 
    	}
        return done;
    }     
    public java.util.ArrayList<Result> getBranchClosedList()
    {
    	java.util.ArrayList<Result> list2 = new java.util.ArrayList<Result>();
    	String query = " SELECT  * from MG_AD_Branch where CLOSE_STATUS ='CLOSED' ";
    	Connection c = null;
    	ResultSet rs = null;
    	Statement s = null;
    	try {
    		    c = getConnection();  
    			s = c.createStatement();
    			rs = s.executeQuery(query);
    			while (rs.next())
    			   {				
    				String branch = rs.getString("BRANCH_CODE");
    				String Description = rs.getString("BRANCH_NAME");
 //   				String positionbyarm = rs.getString("POSITION_BY_ARM");
    				Result Result = new Result();
    				Result.setBranch(branch);
    				Result.setDescription(Description);
//    				Result.setPositionbyarm(positionbyarm);
    				list2.add(Result);
    			   }
    	 }
    				 catch (Exception e)
    					{
    						e.printStackTrace();
    					}
    					finally
    					{
    						closeConnection(c, s, rs);
    					}
    	return list2;
    }
    public boolean createMonthStartRecord(String companyCode,String branchCode, String Month, String qtr,
            int userId,String termenddate,int lastcustomertotal)
    {
        String closemonthquery;
        Connection con;
        PreparedStatement ps;
        boolean done;
//        String familyID;
          
        closemonthquery = "INSERT INTO MG_GB_GL_START (MTID,COMP_CODE,BRANCH_CODE,QUARTER," +
"MONTH,USERID,START_DATE,CREATE_DATE,LAST_CUSTOMER_ACTIVE,STATUS)VALUES (?,?,?,?,?,?,?,?,?,?)"
;       
        con = null;
        ps = null;
        done = false;
        int j;
        String startdate = formatDate(dateConvert(new Date()));
        String startDay = startdate.substring(0,2);
        String startMonth = startdate.substring(3,5);
        String startYear = startdate.substring(6,10);
        try{  
        String glid = helper.getGeneratedId("MG_GB_GL_START");
        con = getConnection();
        done = con.getAutoCommit();
        con.setAutoCommit(false);
        done = false;
        ps = con.prepareStatement(closemonthquery);
        ps.setInt(1, Integer.parseInt(glid));
        ps.setString(2, companyCode);
        ps.setString(3, branchCode);
        ps.setString(4, qtr);
        ps.setString(5, startMonth);
        ps.setInt(6, userId);
        ps.setDate(7, dateConvert(termenddate));
        ps.setDate(8, dateConvert(new Date())); 
        ps.setInt(9, lastcustomertotal);
        ps.setString(10, "ACTIVE");
        j = ps.executeUpdate();        
     
     if(j != -1)
     {
       con.commit();
       con.setAutoCommit(done);
       done = true;
      }
        closeConnection(con, ps);
    	}catch(Exception ex){
        System.out.println((new StringBuilder()).append("ERROR Start Term Record in createBranchStartRecord.. ").append(ex.getMessage()).toString());
        ex.printStackTrace();
        done = false;
        closeConnection(con, ps);
    	} finally{
        closeConnection(con, ps);
    	}
        return done;
    }
    
    public boolean UpdateComanyInfo(String companyCode,String yearend,String processstartDate, int rundays)
    {
        String query;
        Connection con;
        PreparedStatement ps;
        boolean done;        
        String UpdateTotal = "UPDATE MG_GB_COMPANY SET PERFORM_YEAR_END = '"+yearend+"', PROCESSING_DATE = '"+processstartDate+"' WHERE COMPANY_CODE = '"+companyCode+"' ";
//        System.out.println("<<<<UpdateTotal in UpdateComanyInfo: "+UpdateTotal);
        con = null;
        ps = null;
        done = false;
        
        try{          
        con = getConnection();
            ps = con.prepareStatement(UpdateTotal);
            ps.executeUpdate();    
            done = true;     
        closeConnection(con, ps);
    	}catch(Exception ex){
        System.out.println((new StringBuilder()).append("ERROR Updating Company Info in GL Status for Starting.. ").append(ex.getMessage()).toString());
        ex.printStackTrace();
        done = false;
        closeConnection(con, ps);
    	} finally{
        closeConnection(con, ps); 
    	}
        return done;
    } 
    public boolean UpdateComanyInfoforClose(String companyCode,String yearend)
    {
        String query;
        Connection con;
        PreparedStatement ps;
        boolean done;        
        String UpdateTotal = "UPDATE MG_GB_COMPANY SET PERFORM_YEAR_END = '"+yearend+"' WHERE COMPANY_CODE = '"+companyCode+"' ";
        con = null;
        ps = null;
        done = false;
        
        try{          
        con = getConnection();
            ps = con.prepareStatement(UpdateTotal);
            ps.executeUpdate();    
            done = true;     
        closeConnection(con, ps);
    	}catch(Exception ex){
        System.out.println((new StringBuilder()).append("ERROR Updating Company Info in GL Status for Closing.. ").append(ex.getMessage()).toString());
        ex.printStackTrace();
        done = false;
        closeConnection(con, ps);
    	} finally{
        closeConnection(con, ps); 
    	}
        return done;
    } 

    public boolean insertBalancesForCloseYear(String branchCode,String companyCode,String termenddate)
    {
        Connection con;
        PreparedStatement ps;
        PreparedStatement ps2;
        PreparedStatement ps3;
        boolean done;
        boolean done2;
        boolean done3;
        String query;
        con = null;
        ps = null;
        ps2 = null;
        ps3 = null;
        done = false;
        String closedate = formatDate(dateConvert(new Date()));
        String closeDay = termenddate.substring(0,2);
        String closeMonth = termenddate.substring(3,5);
        String closeYear = termenddate.substring(6,10);
 //       System.out.println("closedate: "+closedate+" closeDay: "+closeDay+" closeMonth: "+closeMonth+" closeYear: "+closeYear);
        query = "INSERT INTO IA_GL_YEARLY_BALANCES SELECT gl_acct_no,Branch_Code,period_end_dt,user_id,create_dt,current_bal," +
        		"end_bal,avg_bal,last_year_end_bal,budget_amt,begin_bal,begin_bal_last_per,no_debits_ptd,no_credits_ptd," +
        		"amt_debits_ptd,amt_credits_ptd,avg_bal_ytd,crncy_id,no_debits_qtd,no_credits_qtd,amt_debits_qtd," +
        		"amt_credits_qtd,avg_bal_qtd,ptd_bal,qtd_bal,tran_code,amt_dr_today,no_dr_today,amt_dr_mtd,no_dr_mtd," +
        		"avg_balance_mtd,COMP_CODE,FIRST_QRT_BAL,SECOND_QRT_BAL,THIRD_QRT_BAL,FOURTH_QRT_BAL,"+
        		"'"+termenddate+"','"+closeDay+"','"+closeMonth+"','"+closeYear+"' FROM IA_GL_BALANCES WHERE BRANCH_CODE = '"+branchCode+"' AND COMP_CODE = '"+companyCode+"'";
//        System.out.println("insertBalancesForCloseYear query: "+query);
        try
        { 
            con = getConnection();
            ps = con.prepareStatement(query);
           // ps.setString(1, schoolCode);
            done = ps.executeUpdate() != -1;        
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder(String.valueOf(getClass().getName()))).append(" ERROR:Error inserting GL balances in Yerly Table->").append(e.getMessage()).toString());
            e.printStackTrace();
        }
        finally {  
			   closeConnection(con, ps);  
		}
        return done;
    }    
    public java.util.ArrayList<Header> getheaderList()
    {
    	java.util.ArrayList<Header> list2 = new java.util.ArrayList<Header>();
    	String query = " SELECT  * from IA_PAY_CUM_HEADER ";
    	Connection c = null;
    	ResultSet rs = null;
    	Statement s = null;
    	try {
    		    c = getConnection();  
    			s = c.createStatement();
    			rs = s.executeQuery(query);
    			while (rs.next())
    			   {				
    				String pay1 = rs.getString("PAY1");
    				String pay2 = rs.getString("PAY2");
    				String pay3 = rs.getString("PAY3");
    				String pay4 = rs.getString("PAY4");
    				String pay5 = rs.getString("PAY5");
    				String pay6 = rs.getString("PAY6");
    				String pay7 = rs.getString("PAY7");
    				String pay8 = rs.getString("PAY8");
    				String message = rs.getString("MESSAGE");
    				Header Header = new Header();
    				Header.setPay1(pay1);
    				Header.setPay2(pay2);
    				Header.setPay3(pay3);
    				Header.setPay4(pay4);
    				Header.setPay5(pay5);
    				Header.setPay6(pay6);
    				Header.setPay7(pay7);
    				Header.setPay8(pay8);
    				Header.setMessage(message);
    				list2.add(Header);
    			   }
    	 }
    				 catch (Exception e)
    					{
    						e.printStackTrace();
    					}
    					finally
    					{
    						closeConnection(c, s, rs);
    					}
    	return list2;
    }    
}
