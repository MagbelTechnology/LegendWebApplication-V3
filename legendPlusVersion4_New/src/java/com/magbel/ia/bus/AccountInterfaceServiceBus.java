
package com.magbel.ia.bus;

import com.magbel.ia.dao.PersistenceServiceDAO;
import com.magbel.ia.util.ApplicationHelper;
import com.magbel.ia.vao.*;
import com.magbel.util.DatetimeFormat;

import java.io.PrintStream;
import java.sql.*;
import java.text.SimpleDateFormat;

import com.magbel.ia.util.CodeGenerator;

import java.util.ArrayList;
import java.util.Iterator;
//import java.util.*;

public class AccountInterfaceServiceBus extends PersistenceServiceDAO
{

    SimpleDateFormat sdf;
    final String space = "  ";
    final String comma = ",";
    Date date;
    DatetimeFormat df;
    ApplicationHelper helper;
    public String id;
	public String auotoGenCode = "";
	CodeGenerator cg;

    public AccountInterfaceServiceBus()
    {
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        df = new DatetimeFormat();
        helper = new ApplicationHelper();
		 cg = new CodeGenerator();
    }

   
        public String createImprest(String refNumber, String beneficiary, String impAccNumber, String benAccNumber, String purpose, String expiryDate, String isRetired, 
            String userId, String supervisorId, String transDate, String effDate, String isCash, double amount, 
            String isposted,String companyCode,int branchNo,String ledgerNo,String orderNo)
    {
	    String isAutoGen = getCodeName("SELECT auto_generate_ID FROM MG_GB_COMPANY");
		
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        String id;
        CREATE_QUERY = "INSERT INTO IA_IMPRESTS(MTID, REF_NUMBER, BENEFICIARY, IMP_ACC_NUMBER, BEN_ACC_NUMBER, PURPOSE, EXPIRY_DATE, ISRETIRED, USERID, SUPERVISOR_ID, TRANS_DATE, EFF_DATE, ISCASH,AMOUNT,ISPOSTED,COMP_CODE,BRANCH_NO,LEDGER_CHQ_CASH,ORDER_NO)VALUES(?,?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?)";
        con = null;
        ps = null;
        done = false;
        id = helper.getGeneratedId("IA_IMPRESTS");
		
		refNumber = isAutoGen.trim().equalsIgnoreCase("Y") ? cg.generateCode2("IMPREST","","","") : refNumber;
        auotoGenCode = refNumber;  
        try
        {
            con = getConnection();
            ps = con.prepareStatement(CREATE_QUERY);
            ps.setString(1, id);
            ps.setString(2, refNumber);
            ps.setString(3, beneficiary);
            ps.setString(4, impAccNumber);
            ps.setString(5, benAccNumber);
            ps.setString(6, purpose);
            ps.setDate(7, dateConvert(expiryDate));
            ps.setString(8, isRetired);
            ps.setString(9, userId);
            ps.setString(10, supervisorId);
            ps.setDate(11, dateConvert(transDate));
            ps.setDate(12, dateConvert(effDate));
            ps.setString(13, isCash);
            ps.setDouble(14, amount);
            ps.setString(15, isposted);
			ps.setString(16,companyCode);
			ps.setInt(17,branchNo);
			ps.setString(18, ledgerNo);
			ps.setString(19, orderNo);
            done = ps.executeUpdate() != -1;
     //       System.out.println("INSERT TO TABLE IA_IMPRESTS: "+done);
        }
        catch(Exception er)
        {
            System.out.println((new StringBuilder("Error creating Imprest... ->")).append(er.getMessage()).toString());
            er.printStackTrace();
            
        }
        finally{
                                closeConnection(con,ps);
                        }
        return refNumber;
    }
    
    
    private ArrayList<Imprest> findImprest(String filter)
    {
        ArrayList<Imprest> iaList;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        String query;
        iaList = new ArrayList<Imprest>();
        Imprest impacc = null;
        con = null;
        ps = null;
        rs = null;
        query = "SELECT MTID, REF_NUMBER, BENEFICIARY, IMP_ACC_NUMBER, BEN_ACC_NUMBER, PURPOSE, EXPIRY_DATE, ISRETIRED, USERID, SUPERVISOR_ID, TRANS_DATE, EFF_DATE, ISCASH,AMOUNT, ISPOSTED,RETIRED_DATE,RETIRED_AMOUNT,LEDGER_CHQ_CASH,ORDER_NO FROM IA_IMPRESTS ";
        query = (new StringBuilder(String.valueOf(query))).append(filter).toString();
   //     System.out.println("findImprest query: "+query);
        try
        {
            con = getConnection();
            ps = con.prepareStatement(query);
            //Imprest impacc;
            for(rs = ps.executeQuery(); rs.next(); iaList.add(impacc))
            {
                String mtId = rs.getString("MTID");
                String refNumber = rs.getString("REF_NUMBER");
                String beneficiary = rs.getString("BENEFICIARY");
                String impAccNumber = rs.getString("IMP_ACC_NUMBER");
                String benAccNumber = rs.getString("BEN_ACC_NUMBER");
                String purpose = rs.getString("PURPOSE");
                String expiryDate = sdf.format(rs.getDate("EXPIRY_DATE"));
                String isRetired = rs.getString("ISRETIRED");
                String userId = rs.getString("USERID");
                String supervisorId = rs.getString("SUPERVISOR_ID");
                String transDate = sdf.format(rs.getDate("TRANS_DATE"));
                String effDate = sdf.format(rs.getDate("EFF_DATE"));
                String isCash = rs.getString("ISCASH");
                double amount = rs.getDouble("AMOUNT");
                String isPosted = rs.getString("ISPOSTED");
                String ledgerNo = rs.getString("LEDGER_CHQ_CASH");
                String orderNo = rs.getString("ORDER_NO");
                String retiredDate = "";
                if(rs.getString("RETIRED_DATE") != null)
                    retiredDate = sdf.format(rs.getDate("RETIRED_DATE"));
                impacc = new Imprest(mtId, refNumber, beneficiary, impAccNumber, benAccNumber, purpose, expiryDate, isRetired, userId, supervisorId, transDate, effDate, isCash, amount,orderNo);
                impacc.setIsPosted(isPosted);
                impacc.setRetiredDate(retiredDate);
                impacc.setLedgerNo(ledgerNo);                
            }

        }
        catch(Exception ex)
        {
            System.out.println((new StringBuilder("ERROR fetching Imprests findimprests ")).append(ex.getMessage()).toString());
            ex.printStackTrace();
            
        }
        finally{
                                closeConnection(con,ps);
                        }
        return iaList;
    }
    
    private ArrayList<Imprest> findImprestRetired(String filter)
    {  
        ArrayList<Imprest> iaList;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        String query;
        iaList = new ArrayList<Imprest>();
        Imprest impacc = null;
        con = null;
        ps = null;
        rs = null;
        query = "SELECT MTID, REF_NUMBER, BENEFICIARY, IMP_ACC_NUMBER, BEN_ACC_NUMBER, PURPOSE, EXPIRY_DATE, ISRETIRED, USERID, SUPERVISOR_ID, TRANS_DATE, EFF_DATE, ISCASH,AMOUNT, ISPOSTED,RETIRED_DATE,RETIRED_AMOUNT,LEDGER_CHQ_CASH,ORDER_NO FROM IA_PAYABLE_IMPRESTS ";
        query = (new StringBuilder(String.valueOf(query))).append(filter).toString();
  //      System.out.println("findImprestRetired query: "+query);
        try
        {
            con = getConnection();
            ps = con.prepareStatement(query);
            //Imprest impacc;
            for(rs = ps.executeQuery(); rs.next(); iaList.add(impacc))
            {
                String mtId = rs.getString("MTID");
                String refNumber = rs.getString("REF_NUMBER");
                String beneficiary = rs.getString("BENEFICIARY");
                String impAccNumber = rs.getString("IMP_ACC_NUMBER");
                String benAccNumber = rs.getString("BEN_ACC_NUMBER");
                String purpose = rs.getString("PURPOSE");
                String expiryDate = sdf.format(rs.getDate("EXPIRY_DATE"));
                String isRetired = rs.getString("ISRETIRED");
                String userId = rs.getString("USERID");
                String supervisorId = rs.getString("SUPERVISOR_ID");
                String transDate = sdf.format(rs.getDate("TRANS_DATE"));
                String effDate = sdf.format(rs.getDate("EFF_DATE"));
                String isCash = rs.getString("ISCASH");
                double amount = rs.getDouble("AMOUNT");
                String isPosted = rs.getString("ISPOSTED");
                String ledgerNo = rs.getString("LEDGER_CHQ_CASH");
                String orderNo = rs.getString("ORDER_NO");
                String retiredDate = "";
                if(rs.getString("RETIRED_DATE") != null)
                    retiredDate = sdf.format(rs.getDate("RETIRED_DATE"));
                impacc = new Imprest(mtId, refNumber, beneficiary, impAccNumber, benAccNumber, purpose, expiryDate, isRetired, userId, supervisorId, transDate, effDate, isCash, amount,orderNo);
                impacc.setIsPosted(isPosted);
                impacc.setRetiredDate(retiredDate);
                impacc.setLedgerNo(ledgerNo);                
            }

        }
        catch(Exception ex)
        {
            System.out.println((new StringBuilder("ERROR fetching Imprests findimprests ")).append(ex.getMessage()).toString());
            ex.printStackTrace();
            
        }
        finally{
                                closeConnection(con,ps);
                        }
        return iaList;
    }
        
    private ArrayList<Imprest> findPayableImprest(String filter)
    {
        ArrayList<Imprest> iaList;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        String query;
        iaList = new ArrayList<Imprest>();
        Imprest impacc = null;
        con = null;
        ps = null;
        rs = null;
        query = "SELECT a.MTID, a.REF_NUMBER, a.BENEFICIARY, a.IMP_ACC_NUMBER, a.BEN_ACC_NUMBER, a.PURPOSE, a.EXPIRY_DATE, a.ISRETIRED, a.USERID, a.SUPERVISOR_ID, a.TRANS_DATE, a.EFF_DATE, a.ISCASH,a.AMOUNT, a.ISPOSTED,a.RETIRED_DATE,a.RETIRED_AMOUNT,a.LEDGER_CHQ_CASH,a.ORDER_NO FROM IA_PAYABLE_IMPRESTS a, IA_CUSTOMER_ACCOUNT_TRAN_HIST b ";
        query = (new StringBuilder(String.valueOf(query))).append(filter).toString();
  //      System.out.println("<<<<<<findPayableImprest query: "+query);
        try
        {
            con = getConnection();
            ps = con.prepareStatement(query);
            //Imprest impacc;
            for(rs = ps.executeQuery(); rs.next(); iaList.add(impacc))
            {
                String mtId = rs.getString("MTID");
                String refNumber = rs.getString("REF_NUMBER");
                String beneficiary = rs.getString("BENEFICIARY");
                String impAccNumber = rs.getString("IMP_ACC_NUMBER");
                String benAccNumber = rs.getString("BEN_ACC_NUMBER");
                String purpose = rs.getString("PURPOSE");
                String expiryDate = sdf.format(rs.getDate("EXPIRY_DATE"));
                String isRetired = rs.getString("ISRETIRED");
                String userId = rs.getString("USERID");
                String supervisorId = rs.getString("SUPERVISOR_ID");
                String transDate = sdf.format(rs.getDate("TRANS_DATE"));
                String effDate = sdf.format(rs.getDate("EFF_DATE"));
                String isCash = rs.getString("ISCASH");
                double amount = rs.getDouble("AMOUNT");
                String isPosted = rs.getString("ISPOSTED");
                String ledgerNo = rs.getString("LEDGER_CHQ_CASH");
                String orderNo = rs.getString("ORDER_NO");
                String retiredDate = "";
                if(rs.getString("RETIRED_DATE") != null)
                    retiredDate = sdf.format(rs.getDate("RETIRED_DATE"));
                impacc = new Imprest(mtId, refNumber, beneficiary, impAccNumber, benAccNumber, purpose, expiryDate, isRetired, userId, supervisorId, transDate, effDate, isCash, amount,orderNo);
                impacc.setIsPosted(isPosted);
                impacc.setRetiredDate(retiredDate);
                impacc.setLedgerNo(ledgerNo);                
            }

        }
        catch(Exception ex)
        {
            System.out.println((new StringBuilder("ERROR fetching Payable Imprests findPayableimprests ")).append(ex.getMessage()).toString());
            ex.printStackTrace();
            
        }
        finally{
                                closeConnection(con,ps);
                        }
        return iaList;
    }


    public ArrayList<Imprest> findImprestByQuery(String filter2,String filter)
    {
        ArrayList<Imprest> iaList = new ArrayList<Imprest>();
        String criteria = (new StringBuilder(" WHERE COMP_CODE='"+filter2+"'AND MTID IS NOT NULL ")).append(filter).toString();
        iaList = findImprest(criteria);
        return iaList;
    }
 
    public ArrayList<Imprest> findPayableImprestByQuery(String filter2,String filter)
    {
        ArrayList<Imprest> iaList = new ArrayList<Imprest>();
        String criteria = (new StringBuilder(" WHERE a.COMP_CODE='"+filter2+"'AND a.MTID IS NOT NULL ")).append(filter).toString();
        iaList = findPayableImprest(criteria);
        return iaList;
    }


    public Imprest findImprestByRefNo(String refno)
    {
        Imprest imp = new Imprest();
        String criteria = (new StringBuilder(" WHERE REF_NUMBER = '")).append(refno).append("'").toString();
        imp = findImprest(criteria).get(0);
        return imp;
    }

	
	
	public Imprest findRefNumber(String refno)
	{
	   Imprest imp = null;
	   String filter = " WHERE REF_NUMBER = '"+refno+"'";
	   ArrayList<Imprest> list = findImprest(filter);
	   if(list.size() > 0)
	   {
		  imp = list.get(0);
	   }

	   return imp;
    }
	
    public Imprest findImprestByID(String id)
    {
        Imprest imp = new Imprest();
        String criteria = (new StringBuilder(" WHERE MTID = '")).append(id).append("'").toString();
        imp = findImprest(criteria).get(0);
        return imp;
    }	
	
    public Imprest findPayableImprestByID(String id)
    {
        Imprest imp = new Imprest();
        String criteria = (new StringBuilder(" WHERE MTID = '")).append(id).append("'").toString();
        imp = findPayableImprest(criteria).get(0);
        return imp;
    }	
	
    public Imprest findImprestRetiredByID(String id)
    {
        Imprest imp = new Imprest();
        String criteria = (new StringBuilder(" WHERE MTID = '")).append(id).append("'").toString();
        imp = findImprestRetired(criteria).get(0);
        return imp;
    }

    public boolean retireImprest(String isPosted, String id, double amount)
    {
        String UPDATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        UPDATE_QUERY = "UPDATE IA_IMPRESTS SET ISRETIRED=?,ISPOSTED=?, RETIRED_DATE=?, RETIRED_AMOUNT=? WHERE MTID=?";
        con = null;
        ps = null;
        done = false;
        try
        {
            con = getConnection();
            ps = con.prepareStatement(UPDATE_QUERY);
            ps.setString(1, "Y");
            ps.setString(2, isPosted);
            ps.setDate(3, dateConvert(new java.util.Date()));
            ps.setDouble(4, amount);
            ps.setString(5, id);
            done = ps.executeUpdate() != -1;
        }
        catch(Exception er)
        {
            System.out.println((new StringBuilder("Error UPDATING IA_IMPREST... ->")).append(er.getMessage()).toString());
            
        }
        finally{
                                closeConnection(con,ps);
                        }
        return done;
    }

    public boolean PayableretireImprest(String isPosted, String id, double amount)
    {
        String UPDATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        UPDATE_QUERY = "UPDATE IA_PAYABLE_IMPRESTS SET ISRETIRED=?,ISPOSTED=?, RETIRED_DATE=?, RETIRED_AMOUNT=? WHERE MTID=?";
        con = null;
        ps = null;
        done = false;
   //     System.out.println("UPDATE_QUERY Record Size>>>>>> "+UPDATE_QUERY+"    Identity Code: "+id);
        try
        {
            con = getConnection();
            ps = con.prepareStatement(UPDATE_QUERY);
            ps.setString(1, "Y");
            ps.setString(2, isPosted);
            ps.setDate(3, dateConvert(new java.util.Date()));
            ps.setDouble(4, amount);
            ps.setString(5, id);
            done = ps.executeUpdate() != -1;
        }
        catch(Exception er)
        {
            System.out.println((new StringBuilder("Error UPDATING IA_PAYABLE_IMPRESTS... ->")).append(er.getMessage()).toString());
            
        }
        finally{
                                closeConnection(con,ps);
                        }
        return done;
    }
 
    public boolean retireImprest(String id, double amount)
    {
        String UPDATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        UPDATE_QUERY = "UPDATE IA_IMPRESTS SET ISRETIRED=?, RETIRED_DATE=?, RETIRED_AMOUNT=? WHERE MTID=?";
        con = null;
        ps = null;
        done = false;
        try
        {
            con = getConnection();
            ps = con.prepareStatement(UPDATE_QUERY);
            ps.setString(1, "Y");
            ps.setDate(2, dateConvert(new java.util.Date()));
            ps.setDouble(3, amount);
            ps.setString(4, id);
            done = ps.executeUpdate() != -1;
        }
        catch(Exception er)
        {
            System.out.println((new StringBuilder("Error Retiring IA_IMPREST... ->")).append(er.getMessage()).toString());
            
        }
        finally{
                                closeConnection(con,ps);
                        }
        return done;
    }
    
    public boolean PayableretireImprest(String id, double amount)
    {
        String UPDATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        UPDATE_QUERY = "UPDATE IA_PAYABLE_IMPRESTS SET ISRETIRED=?, RETIRED_DATE=?, RETIRED_AMOUNT=? WHERE MTID=?";
        con = null;
        ps = null;
        done = false;
        try
        {
//        	System.out.println("updateImprestItemforPosting ID: "+id+"   Amount: "+amount+"  UPDATE_QUERY: "+UPDATE_QUERY);
            con = getConnection();
            ps = con.prepareStatement(UPDATE_QUERY);
            ps.setString(1, "Y");
            ps.setDate(2, dateConvert(new java.util.Date()));
            ps.setDouble(3, amount);
            ps.setString(4, id);
            done = ps.executeUpdate() != -1;
        }
        catch(Exception er)
        {
            System.out.println((new StringBuilder("Error Retiring IA_PAYABLE_IMPRESTS... ->")).append(er.getMessage()).toString());
            
        }
        finally{
                                closeConnection(con,ps);
                        }
        return done;
    }


    public boolean createImprestAccount(String code, String type, String glAccount, int retirePolicy, int grace,String companyCode)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        String id;
        CREATE_QUERY = "INSERT INTO IA_IMPREST_ACCOUNTS(MTID, CODE, TYPE, GL_ACCOUNT, RETIRE_POLICY, GRACE,CREATE_DATE,COMP_CODE) VALUES(?, ?, ?, ?, ?, ?, ?,? )";
        con = null;
        ps = null;
        done = false;
        id = helper.getGeneratedId("IA_IMPREST_ACCOUNTS");
        try
        {
            con = getConnection();
            ps = con.prepareStatement(CREATE_QUERY);
            ps.setString(1, id);
            ps.setString(2, code);
            ps.setString(3, type);
            ps.setString(4, glAccount);
            ps.setInt(5, retirePolicy);
            ps.setInt(6, grace);
            ps.setDate(7, dateConvert(new java.util.Date()));
			ps.setString(8,companyCode);
            done = ps.executeUpdate() != -1;
        }
        catch(Exception er)
        {
            System.out.println((new StringBuilder("Error creating Imprest Account... ->")).append(er.getMessage()).toString());
            er.printStackTrace();
           
        }
        finally{
                                closeConnection(con,ps);
                        }
        return done;
    }

    public boolean updateImprestAccount(String id, String type, String glAccount, int retirePolicy, int grace)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        CREATE_QUERY = "UPDATE IA_IMPREST_ACCOUNTS SET TYPE=?, GL_ACCOUNT=?, RETIRE_POLICY=?, GRACE=? WHERE MTID=? ";
        con = null;
        ps = null;
        done = false;
        try
        {
            con = getConnection();
            ps = con.prepareStatement(CREATE_QUERY);
            ps.setString(1, type);
            ps.setString(2, glAccount);
            ps.setInt(3, retirePolicy);
            ps.setInt(4, grace);
            ps.setString(5, id);
            done = ps.executeUpdate() != -1;
        }
        catch(Exception er)
        {
            System.out.println((new StringBuilder("Error updating Imprest Account... ->")).append(er.getMessage()).toString());
            er.printStackTrace();
            
        }
        finally{
                                closeConnection(con,ps);
                        }
        return done;
    }

    private ArrayList<ImprestAccount> findImprestAccount(String filter)
    {
        ArrayList<ImprestAccount> iaList;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        String query;
        iaList = new ArrayList<ImprestAccount>();
        ImprestAccount impacc = null;
        con = null;
        ps = null;
        rs = null;
        query = "SELECT MTID, CODE, TYPE, GL_ACCOUNT, RETIRE_POLICY, GRACE  FROM IA_IMPREST_ACCOUNTS ";
        query = (new StringBuilder(String.valueOf(query))).append(filter).toString();
        try
        {
            con = getConnection();
            ps = con.prepareStatement(query);
            //ImprestAccount impacc;
            for(rs = ps.executeQuery(); rs.next(); iaList.add(impacc))
            {
                String mtId = rs.getString("MTID");
                String code = rs.getString("CODE");
                String type = rs.getString("TYPE");
                String glAccount = rs.getString("GL_ACCOUNT");
                int retirePolicy = rs.getInt("RETIRE_POLICY");
                int grace = rs.getInt("GRACE");
                impacc = new ImprestAccount(mtId, code, type, glAccount, retirePolicy, grace);
            }

        }
        catch(Exception ex)
        {
            System.out.println((new StringBuilder("ERROR fetching Imprests findimprestaccount ")).append(ex.getMessage()).toString());
            ex.printStackTrace();
            
        }
        finally{
                                closeConnection(con,ps);
                        }
        return iaList;
    }

    public ArrayList<ImprestAccount> findImprestAccountByQuery(String filter2,String filter)
    {
        ArrayList<ImprestAccount> iaList = new ArrayList<ImprestAccount>();
        String criteria = (new StringBuilder(" WHERE COMP_CODE = '"+filter2+"' AND MTID IS NOT NULL ")).append(filter).toString();
        iaList = findImprestAccount(criteria);
        return iaList;
    }

    public ImprestAccount findImprestAccountByCode(String refno)
    {
        ImprestAccount imp = new ImprestAccount();
        String criteria = (new StringBuilder(" WHERE CODE = '")).append(refno).append("'").toString();
        imp = findImprestAccount(criteria).get(0);
        return imp;
    }

    public ImprestAccount findImprestAccountByID(String id)
    {
        ImprestAccount imp = new ImprestAccount();
        String criteria = (new StringBuilder(" WHERE MTID = '")).append(id).append("'").toString();
        imp = findImprestAccount(criteria).get(0);
        return imp;
    }

	
	public ImprestItems findTABSByTABSId(String glAccount)
	{
	   ImprestItems tabs = null;
	   String filter = " WHERE GL_ACOUNT = '"+glAccount+"'";
	   ArrayList<ImprestItems> list = findImprestItems(filter);
	   if(list.size() > 0)
	   {
		  tabs = list.get(0);
	   }

	   return tabs;
   }
	
	public ImprestItems findImprestAvailable(String refNo)
	{
	   ImprestItems tabs = null;
	   String filter = " WHERE REF_NUMBER = '"+refNo+"'";
	   ArrayList<ImprestItems> list = findImprestItemsAvailable(filter);
	   if(list.size() > 0)
	   {
		  tabs = list.get(0);
	   }

	   return tabs;
   }
	
	public ImprestItems findPayableImprestAvailable(String refNo)
	{
	   ImprestItems tabs = null;
	   String filter = " WHERE REF_NUMBER = '"+refNo+"'";
	   ArrayList<ImprestItems> list = findPayableImprestItemsAvailable(filter);
	   if(list.size() > 0)
	   {
		  tabs = list.get(0);
	   }

	   return tabs;
   }
		
    public boolean insertImprestItems(ArrayList list)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        int d[];
        CREATE_QUERY = "INSERT INTO IA_IMPREST_ITEMS(MTID, REF_NUMBER, GL_ACOUNT, DESCRIPTION, AMOUNT, CREATE_DATE,POSTED) VALUES(?, ?, ?, ?, ?, ?,?)";
        con = null;
        ps = null;
        d = (int[])null;
        try
        {
            con = getConnection();
            ps = con.prepareStatement(CREATE_QUERY);
            for(Iterator iter = list.iterator(); iter.hasNext(); ps.addBatch())
            {
                ImprestItems item = (ImprestItems)iter.next();
                String id = helper.getGeneratedId("IA_IMPREST_ITEMS");
                ps.setString(1, id);
                ps.setString(2, item.getRefNumber());
                ps.setString(3, item.getGlAccount());
                ps.setString(4, item.getDescription());
                ps.setDouble(5, item.getAmount());
                ps.setDate(6, dateConvert(new java.util.Date()));
                ps.setString(7,"N");
            }

            d = ps.executeBatch();
        }
        catch(Exception er)
        {
            System.out.println((new StringBuilder("Error creating Imprest ITEMS... ->")).append(er.getMessage()).toString());
            er.printStackTrace();
           
        }
        finally{
                                closeConnection(con,ps);
                        }
        return d.length > 0;
    }

    public boolean createImprestItem(String glAccount, String refNumber, String description, double amount,String type,String companyCode,int branchNo,String expglaccount,String orderNo)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean d;
        CREATE_QUERY = "INSERT INTO IA_IMPREST_ITEMS(MTID, REF_NUMBER, GL_ACOUNT, DESCRIPTION, AMOUNT,TYPE, CREATE_DATE,COMP_CODE,BRANCH_NO,POSTED,EXPENSE_ACCT,ORDER_NO) VALUES(?, ?, ?, ?, ?, ?, ?,?,?,?,?,?)";
        con = null;
        ps = null;
        d = false;
        try
        {
            con = getConnection();
            ps = con.prepareStatement(CREATE_QUERY);
            String id = helper.getGeneratedId("IA_IMPREST_ITEMS");
            ps.setString(1, id);
            ps.setString(2, refNumber);
            ps.setString(3, glAccount);
            ps.setString(4, description);
            ps.setDouble(5, amount);
			ps.setString(6,type);
            ps.setDate(7, dateConvert(new java.util.Date()));
			ps.setString(8,companyCode);
			ps.setInt(9, branchNo);
			ps.setString(10,"N");
			ps.setString(11, expglaccount);
			ps.setString(12, orderNo);
	//		System.out.print("Expense GL Account: "+expglaccount);;
            d = ps.executeUpdate() != -1;
        }
        catch(Exception er)
        {
            System.out.println((new StringBuilder("Error creating Imprest ITEM... ->")).append(er.getMessage()).toString());
            er.printStackTrace();
           
        }
        finally{
                                closeConnection(con,ps);
                        }
        return d;
    }
    public boolean createPayableImprestItem(String glAccount, String refNumber, String description, double amount,String type,String companyCode,int branchNo,String expglaccount,String orderNo)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean d;
        CREATE_QUERY = "INSERT INTO IA_PAYABLE_IMPREST_ITEMS(MTID, REF_NUMBER, GL_ACOUNT, DESCRIPTION, AMOUNT,TYPE, CREATE_DATE,COMP_CODE,BRANCH_NO,POSTED,EXPENSE_ACCT,ORDER_NO) VALUES(?, ?, ?, ?, ?, ?, ?,?,?,?,?,?)";
        con = null;
        ps = null;
        d = false;
        try
        {
            con = getConnection();
            ps = con.prepareStatement(CREATE_QUERY);
            String id = helper.getGeneratedId("IA_PAYABLE_IMPREST_ITEMS");
            ps.setString(1, id);
            ps.setString(2, refNumber);
            ps.setString(3, glAccount);
            ps.setString(4, description);
            ps.setDouble(5, amount);
			ps.setString(6,type);
            ps.setDate(7, dateConvert(new java.util.Date()));
			ps.setString(8,companyCode);
			ps.setInt(9, branchNo);
			ps.setString(10,"N");
			ps.setString(11, expglaccount);
			ps.setString(12, orderNo);
	//		System.out.print("Expense GL Account: "+expglaccount);;
            d = ps.executeUpdate() != -1;
        }
        catch(Exception er)
        {
            System.out.println((new StringBuilder("Error creating Payable Imprest ITEM... ->")).append(er.getMessage()).toString());
            er.printStackTrace();
           
        }
        finally{
                                closeConnection(con,ps);
                        }
        return d;
    }

    public boolean createImprestItem(String glAccount, String refNumber, String description, double amount,String type,String companyCode)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean d;
        CREATE_QUERY = "INSERT INTO IA_IMPREST_ITEMS(MTID, REF_NUMBER, GL_ACOUNT, DESCRIPTION, AMOUNT,TYPE, CREATE_DATE,COMP_CODE) VALUES(?, ?, ?, ?, ?, ?,?,?)";
        con = null;
        ps = null;
        d = false;
        try
        {
            con = getConnection();
            ps = con.prepareStatement(CREATE_QUERY);
            String id = helper.getGeneratedId("IA_IMPREST_ITEMS");
            ps.setString(1, id);
            ps.setString(2, refNumber);
            ps.setString(3, glAccount);
            ps.setString(4, description);
            ps.setDouble(5, amount);
			ps.setString(6,type);
            ps.setDate(7, dateConvert(new java.util.Date()));
			ps.setString(8,companyCode);
		//	ps.setInt(9, branchNo);
		//	ps.setString(9,"N");
            d = ps.executeUpdate() != -1;
        }
        catch(Exception er)
        {
            System.out.println((new StringBuilder("Error creating Imprest ITEM... ->")).append(er.getMessage()).toString());
            er.printStackTrace();
           
        }
        finally{
                                closeConnection(con,ps);
                        }
        return d;
    } 
    
    public boolean updateImprestItem(String glAccount,String description, double amount,String type,String id,String expglaccount)
    {
        String UPDATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean d;
        UPDATE_QUERY = "UPDATE IA_IMPREST_ITEMS SET GL_ACOUNT=?,EXPENSE_ACCT=?,DESCRIPTION=?,AMOUNT=?,CREATE_DATE=?,TYPE=? WHERE MTID=?";
        con = null;
        ps = null;
        d = false;
        try
        {
            con = getConnection();
            ps = con.prepareStatement(UPDATE_QUERY);
            ps.setString(1,glAccount);
            ps.setString(2,expglaccount);
            ps.setString(3, description);
            ps.setDouble(4, amount);
            ps.setDate(5, dateConvert(new java.util.Date()));
			ps.setString(6,type);
            ps.setString(7, id);
            d = ps.executeUpdate() != -1;
        }
        catch(Exception er)
        {
            System.out.println((new StringBuilder("Error updating Imprest ITEM... ->")).append(er.getMessage()).toString());
            er.printStackTrace();
           
        }
        finally{
                                closeConnection(con,ps);
                        }
        return d;
    }
    public boolean updatePayableImprestItem(String glAccount,String description, double amount,String type,String id,String expglaccount)
    {
        String UPDATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean d;
        UPDATE_QUERY = "UPDATE IA_PAYABLE_IMPREST_ITEMS SET GL_ACOUNT=?,EXPENSE_ACCT=?,DESCRIPTION=?,AMOUNT=?,CREATE_DATE=?,TYPE=? WHERE MTID=?";
        con = null;
        ps = null;
        d = false;
        try
        {
            con = getConnection();
            ps = con.prepareStatement(UPDATE_QUERY);
            ps.setString(1,glAccount);
            ps.setString(2,expglaccount);
            ps.setString(3, description);
            ps.setDouble(4, amount);
            ps.setDate(5, dateConvert(new java.util.Date()));
			ps.setString(6,type);
            ps.setString(7, id);
            d = ps.executeUpdate() != -1;
        }
        catch(Exception er)
        {
            System.out.println((new StringBuilder("Error updating Imprest ITEM... ->")).append(er.getMessage()).toString());
            er.printStackTrace();
           
        }
        finally{
                                closeConnection(con,ps);
                        }
        return d;
    }    
    public boolean updateImprestItem(String glAccount,String description, double amount,String type,String id)
    {
        String UPDATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean d;
        UPDATE_QUERY = "UPDATE IA_IMPREST_ITEMS SET GL_ACOUNT=?,EXPENSE_ACCT=?,DESCRIPTION=?,AMOUNT=?,CREATE_DATE=?,TYPE=? WHERE MTID=?";
        con = null;
        ps = null;
        d = false;
        try
        {
            con = getConnection();
            ps = con.prepareStatement(UPDATE_QUERY);
            ps.setString(1,glAccount);
            ps.setString(2, description);
            ps.setDouble(3, amount);
            ps.setDate(4, dateConvert(new java.util.Date()));
			ps.setString(5,type);
            ps.setString(6, id);
            d = ps.executeUpdate() != -1;
        }
        catch(Exception er)
        {
            System.out.println((new StringBuilder("Error updating Imprest ITEM... ->")).append(er.getMessage()).toString());
            er.printStackTrace();
           
        }
        finally{
                                closeConnection(con,ps);
                        }
        return d;
    }
        
    private ArrayList<ImprestItems> findImprestItems(String filter)
    {
        ArrayList<ImprestItems> iaList;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        String query;
        iaList = new ArrayList<ImprestItems>();
        ImprestItems impacc = null;
        con = null;
        ps = null;
        rs = null;
        query = "SELECT MTID, REF_NUMBER, GL_ACOUNT,EXPENSE_ACCT, DESCRIPTION, AMOUNT,TYPE, CREATE_DATE,ORDER_NO FROM IA_IMPREST_ITEMS";
        query = (new StringBuilder(String.valueOf(query))).append(filter).toString();
        try
        { //System.out.println("findImprestItems query: "+query);
            con = getConnection();
            ps = con.prepareStatement(query);
            //ImprestItems impacc;
            for(rs = ps.executeQuery(); rs.next(); iaList.add(impacc))
            {
                String mtId = rs.getString("MTID");
                String glAccount = rs.getString("GL_ACOUNT");
                String refNumber = rs.getString("REF_NUMBER");
                String description = rs.getString("DESCRIPTION");
                double amount = rs.getDouble("AMOUNT"); 
				String type = rs.getString("TYPE");
				String createdate = rs.getString("CREATE_DATE");
				String expglaccount = rs.getString("EXPENSE_ACCT");
				String orderNo = rs.getString("ORDER_NO");
                impacc = new ImprestItems(mtId, glAccount, refNumber, description, amount,type,orderNo);
                impacc.setCreatedate(createdate);
                impacc.setExpglaccount(expglaccount);
            }

        }
        catch(Exception ex)
        {
            System.out.println((new StringBuilder("ERROR fetching Imprests findimprestItems")).append(ex.getMessage()).toString());
            ex.printStackTrace();
            
        }
        finally{
                                closeConnection(con,ps);
                        }
        return iaList;
    }
    
private ArrayList<ImprestItems> findImprestRetiredItems(String filter)
{
    ArrayList<ImprestItems> iaList;
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    String query;
    iaList = new ArrayList<ImprestItems>();
    ImprestItems impacc = null;
    con = null;
    ps = null;
    rs = null;
    query = "SELECT MTID, REF_NUMBER, GL_ACOUNT,EXPENSE_ACCT, DESCRIPTION, AMOUNT,TYPE, CREATE_DATE,ORDER_NO FROM IA_PAYABLE_IMPREST_ITEMS";
    query = (new StringBuilder(String.valueOf(query))).append(filter).toString();
    try
    { //System.out.println("findImprestItems query: "+query);
        con = getConnection();
        ps = con.prepareStatement(query);
        //ImprestItems impacc;
        for(rs = ps.executeQuery(); rs.next(); iaList.add(impacc))
        {
            String mtId = rs.getString("MTID");
            String glAccount = rs.getString("GL_ACOUNT");
            String refNumber = rs.getString("REF_NUMBER");
            String description = rs.getString("DESCRIPTION");
            double amount = rs.getDouble("AMOUNT"); 
			String type = rs.getString("TYPE");
			String createdate = rs.getString("CREATE_DATE");
			String expglaccount = rs.getString("EXPENSE_ACCT");
			String orderNo = rs.getString("ORDER_NO");
            impacc = new ImprestItems(mtId, glAccount, refNumber, description, amount,type,orderNo);
            impacc.setCreatedate(createdate);
            impacc.setExpglaccount(expglaccount);
        }

    }
    catch(Exception ex)
    {
        System.out.println((new StringBuilder("ERROR fetching Imprests findimprestItems")).append(ex.getMessage()).toString());
        ex.printStackTrace();
        
    }
    finally{
                            closeConnection(con,ps);
                    }
    return iaList;
}
    
private ArrayList<ImprestItems> findImprestItemsAvailable(String filter)
{
    ArrayList<ImprestItems> iaList;
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    String query;
    iaList = new ArrayList<ImprestItems>();
    ImprestItems impacc = null;
    con = null;
    ps = null;
    rs = null;
    query = "SELECT MTID, BEN_ACC_NUMBER, IMP_ACC_NUMBER, AMOUNT,ORDER_NO FROM IA_IMPRESTS";
    query = (new StringBuilder(String.valueOf(query))).append(filter).toString();
    try
    { //System.out.println("findImprestItemsAvailable query: "+query);
        con = getConnection();
        ps = con.prepareStatement(query);
        //ImprestItems impacc;
        for(rs = ps.executeQuery(); rs.next(); iaList.add(impacc))
        {
            String mtId = rs.getString("MTID");
            String glAccount = rs.getString("BEN_ACC_NUMBER");
            String imprestAccount = rs.getString("IMP_ACC_NUMBER");
            double amount = rs.getDouble("AMOUNT"); 
            String orderNo = rs.getString("ORDER_NO");
            impacc = new ImprestItems(mtId, glAccount, "", "", amount,imprestAccount,orderNo);
        }

    }
    catch(Exception ex)
    {
        System.out.println((new StringBuilder("ERROR fetching Imprests findImprestItemsAvailable")).append(ex.getMessage()).toString());
        ex.printStackTrace();
        
    }
    finally{
                            closeConnection(con,ps);
                    }
    return iaList;
}
private ArrayList<ImprestItems> findPayableImprestItemsAvailable(String filter)
{
    ArrayList<ImprestItems> iaList;
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    String query;
    iaList = new ArrayList<ImprestItems>();
    ImprestItems impacc = null;
    con = null;
    ps = null;
    rs = null;
    query = "SELECT MTID, BEN_ACC_NUMBER, IMP_ACC_NUMBER, AMOUNT,ORDER_NO FROM IA_PAYABLE_IMPRESTS";
    query = (new StringBuilder(String.valueOf(query))).append(filter).toString();
    try
    {// System.out.println("findPayableImprestItemsAvailable query: "+query);
        con = getConnection();
        ps = con.prepareStatement(query);
        //ImprestItems impacc;
        for(rs = ps.executeQuery(); rs.next(); iaList.add(impacc))
        {
            String mtId = rs.getString("MTID");
            String glAccount = rs.getString("BEN_ACC_NUMBER");
            String imprestAccount = rs.getString("IMP_ACC_NUMBER");
            double amount = rs.getDouble("AMOUNT"); 
            String orderNo = rs.getString("ORDER_NO");
            impacc = new ImprestItems(mtId, glAccount, "", "", amount,imprestAccount,orderNo);
        }

    }
    catch(Exception ex)
    {
        System.out.println((new StringBuilder("ERROR fetching Imprests findPayableImprestItemsAvailable")).append(ex.getMessage()).toString());
        ex.printStackTrace();
        
    }
    finally{
                            closeConnection(con,ps);
                    }
    return iaList;
}
    public ArrayList<ImprestItems> findImprestItemsByQuery(String filter)
    {
        ArrayList<ImprestItems> iaList = new ArrayList<ImprestItems>();
        String criteria = (new StringBuilder(" WHERE MTID IS NOT NULL ")).append(filter).toString();
        iaList = findImprestItems(criteria);
        return iaList;
    }
    
    public ArrayList<ImprestItems> findImprestRetiredItemsByQuery(String filter)
    {
        ArrayList<ImprestItems> iaList = new ArrayList<ImprestItems>();
        String criteria = (new StringBuilder(" WHERE MTID IS NOT NULL ")).append(filter).toString();
        iaList = findImprestRetiredItems(criteria);
        return iaList;
    }

    public ImprestItems findImprestItemsByRef(String refno)
    {
        ImprestItems imp = new ImprestItems();
        String criteria = (new StringBuilder(" WHERE REF_NUMBER = '")).append(refno).append("'").toString();
        imp = findImprestItems(criteria).get(0);
        return imp;
    }
    public ImprestItems findImprestItemsById(String id)
    {
        ImprestItems imp = new ImprestItems();
        String criteria = (new StringBuilder(" WHERE MTID = '")).append(id).append("'").toString();
        imp = findImprestItems(criteria).get(0);
        return imp;
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
        System.out.println("Error in "+er.getClass().getName()+" - getCodeName()... ->"+er);
        er.printStackTrace();
     }finally{
        closeConnection(con,ps);
      }   
      return result;
     }
     
    public boolean updateImprestApproveStatus(String refNo, String approveStatus)
    {
        String updateQuery;
        Connection con;
        PreparedStatement ps;
        boolean done;
        updateQuery = "UPDATE IA_IMPRESTS SET APPROVE_STATUS=? WHERE REF_NUMBER=? ";
        con = null;
        ps = null;
        done = false;
        try
        {
            con = getConnection();
            ps = con.prepareStatement(updateQuery);
            ps.setString(1,approveStatus);
            ps.setString(2,refNo);
            done = ps.executeUpdate() != -1;
        }
        catch(Exception er)
        {
            System.out.println("Error updating Imprest Approve Status... ->"+er.getMessage());
            er.printStackTrace();
            
        }
        finally{
                                closeConnection(con,ps);
                        }
        return done;
    }
	
	
	
	
	public ArrayList<ImprestRef> findImprestRef(String filter)
    {
        ArrayList<ImprestRef> records = new ArrayList<ImprestRef>();
		String query  = "SELECT MTID, REF_NUMBER FROM IA_IMPRESTS WHERE MTID IS NOT NULL "+filter+" ORDER BY REF_NUMBER";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
        try
        {        
            con = getConnection();
            ps = con.prepareStatement(query);
            //Imprest impacc;
           rs = ps.executeQuery();
		   while(rs.next())
            {
                String mtId = rs.getString("MTID");
                String refNumber = rs.getString("REF_NUMBER");

                
               ImprestRef tabs = new ImprestRef(mtId, refNumber);
               records.add(tabs);
            }

        }
        catch(Exception ex)
        {
            System.out.println((new StringBuilder("ERROR fetching Imprests findimprests ")).append(ex.getMessage()).toString());
            ex.printStackTrace();
            
        }
        finally{
                                closeConnection(con,ps);
                        }
        return records;
    }


public ImprestRef findNumber(String refNumber)
{
	   ImprestRef tabs = null;
	   String filter = " AND REF_NUMBER = '"+refNumber+"'";
	   ArrayList<ImprestRef> list = findImprestRef(filter);
	   if(list.size() > 0){    
		  tabs = list.get(0);
	   }  

	   return tabs;
   }

public boolean updateCRBalances(String AcctNo, String BranchNo,double tranAmount) {
//	System.out.println();
    Connection con = null;
    PreparedStatement ps = null; 
    String UPDATE_QUERY = "UPDATE IA_GL_BALANCES SET current_bal = current_bal + "+tranAmount+",no_credits_ptd = no_credits_ptd + 1,amt_credits_ptd = amt_credits_ptd + "+tranAmount+" WHERE gl_acct_no = '"+AcctNo.trim()+"' AND Branch_Code = '"+BranchNo+"' ";
    String UPDATEProcess_QUERY = "UPDATE IA_GL_HISTORY SET Processed = 'Y' WHERE gl_acct_no = '"+AcctNo+"' AND Branch_NO = '"+BranchNo+"' ";
 //   System.out.println("updateCRBalances UPDATE_QUERY2: "+UPDATE_QUERY);
    boolean done;
    done = false;
    try {
    	con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);         
        ps.executeUpdate();          
        done = true;
    } catch (Exception ex) {
        System.out.println("WARNING: cannot update IA_GL_BALANCES IN updateCRBalances+" + ex.getMessage());
    } finally {
        closeConnection(con, ps);
    }
    return done;
}

public void updateDRBalances(String AcctNo, String BranchNo,double tranAmount) {
    Connection con = null;
    PreparedStatement ps = null;
    String UPDATE_QUERY = "UPDATE IA_GL_BALANCES SET current_bal = current_bal - "+tranAmount+",no_debits_ptd = no_debits_ptd + 1,amt_debits_ptd = amt_debits_ptd + "+tranAmount+" WHERE gl_acct_no = '"+AcctNo+"' AND Branch_code = '"+BranchNo+"' ";
//    System.out.println("updateDRBalances: "+UPDATE_QUERY);
    try {
   	   con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);                    
        ps.executeUpdate();
        
    } catch (Exception ex) {
        System.out.println("WARNING: cannot update IA_GL_BALANCES+" + ex.getMessage());
    } finally {
        closeConnection(con, ps);
    }

}

public void updateStudentCRBalances(String AcctNo, int BranchNo,double tranAmount) {
    Connection con = null;
    PreparedStatement ps = null;
    PreparedStatement ps1 = null;
    String UPDATE_QUERY1 = "UPDATE IA_CUSTOMER_ACCOUNT_DISPLAY SET ACCOUNT_BALANCE = ACCOUNT_BALANCE + "+tranAmount+",CLEAR_BALANCE = CLEAR_BALANCE + "+tranAmount+",TRANSACTION_COUNT = TRANSACTION_COUNT + 1 WHERE ACCOUNT_NO = '"+AcctNo+"' ";
  //   System.out.println("updateStudentCRBalances: "+UPDATE_QUERY1);
     String UPDATE_QUERY2 = "UPDATE IA_CUSTOMER_ACCOUNT_SETUP SET ACCOUNT_BALANCE = ACCOUNT_BALANCE + "+tranAmount+",TRANSACTION_COUNT = TRANSACTION_COUNT + 1 WHERE ACCOUNT_NO = '"+AcctNo+"' ";
 //    System.out.println("updateStudentCRBalances: "+UPDATE_QUERY2);
    try {
   	   con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY1);                    
        ps.executeUpdate();
        ps1 = con.prepareStatement(UPDATE_QUERY2);                    
        ps1.executeUpdate();
        
    } catch (Exception ex) {
        System.out.println("WARNING: cannot update IA_CUSTOMER_ACCOUNT_DISPLAY+" + ex.getMessage());
    } finally {
        closeConnection(con, ps);
        closeConnection(con, ps1);
    }

}
public void updateStudentDRBalances(String AcctNo, int BranchNo,double tranAmount) {
    Connection con = null;
    PreparedStatement ps = null;
    PreparedStatement ps1 = null;
    String UPDATE_QUERY1 = "UPDATE IA_CUSTOMER_ACCOUNT_DISPLAY SET ACCOUNT_BALANCE = ACCOUNT_BALANCE - "+tranAmount+",CLEAR_BALANCE = CLEAR_BALANCE - "+tranAmount+",TRANSACTION_COUNT = TRANSACTION_COUNT + 1 WHERE ACCOUNT_NO = '"+AcctNo+"' ";
//     System.out.println("updateStudentDRBalances UPDATE_QUERY1: "+UPDATE_QUERY1);
     String UPDATE_QUERY2 = "UPDATE IA_CUSTOMER_ACCOUNT_SETUP SET ACCOUNT_BALANCE = ACCOUNT_BALANCE - "+tranAmount+",TRANSACTION_COUNT = TRANSACTION_COUNT + 1 WHERE ACCOUNT_NO = '"+AcctNo+"' ";
 //    System.out.println("updateStudentDRBalances UPDATE_QUERY2: "+UPDATE_QUERY2); 
    try {
   	   con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY1);                    
        ps.executeUpdate();
        ps1 = con.prepareStatement(UPDATE_QUERY2);                    
        ps1.executeUpdate();
        
    } catch (Exception ex) {
        System.out.println("WARNING: cannot update IA_CUSTOMER_ACCOUNT_DISPLAY For DEBIT+" + ex.getMessage());
    } finally {
        closeConnection(con, ps);
        closeConnection(con, ps1);
    }

}
public boolean updateImprestItemforPosting(String id)
{
    String UPDATE_QUERY;
    Connection con;
    PreparedStatement ps;
    boolean d;
    UPDATE_QUERY = "UPDATE IA_IMPREST_ITEMS SET POSTED=? WHERE MTID=?";
    con = null;
    ps = null;
    d = false;
    try
    { 
 //   	System.out.println("updateImprestItemforPosting ID: "+id);
        con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);
        ps.setString(1,"Y");
        ps.setString(2, id);
        d = ps.executeUpdate() != -1;
    }
    catch(Exception er)
    {
        System.out.println((new StringBuilder("Error updating Imprest ITEM for Postings... ->")).append(er.getMessage()).toString());
        er.printStackTrace();
       
    }
    finally{
                            closeConnection(con,ps);
                    }
    return d;
}
public boolean PayableupdateImprestItemforPosting(String orderNo)
{
    String UPDATE_QUERY;
    Connection con;
    PreparedStatement ps;
    boolean d;
    UPDATE_QUERY = "UPDATE IA_PAYABLE_IMPREST_ITEMS SET POSTED=? WHERE ORDER_NO=?";
    con = null;
    ps = null;
    d = false;
    try
    { 
    //	System.out.println("updateImprestItemforPosting ID: "+id);
        con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);
        ps.setString(1,"Y");
        ps.setString(2, orderNo);
        d = ps.executeUpdate() != -1;
    }
    catch(Exception er)
    {
        System.out.println((new StringBuilder("Error updating Payable Imprest ITEM for Postings... ->")).append(er.getMessage()).toString());
        er.printStackTrace();
       
    }
    finally{
                            closeConnection(con,ps);
                    }
    return d;
}
public java.util.ArrayList<ImprestItems> getImprestItemsRecord()
{
	java.util.ArrayList<ImprestItems> _list = new java.util.ArrayList<ImprestItems>();
	String date = String.valueOf(df.dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String query = "SELECT  * from IA_IMPREST_ITEMS where POSTED = 'N' ";
//	Transaction transaction = null;

	Connection c = null;
	ResultSet rs = null;
	Statement s = null;
  
	try {
		    c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next())
			   {				
//				String transactionid = rs.getString("TRANSACTION_ID");
				String mtid = rs.getString("MTID");
				String companyCode = rs.getString("COMP_CODE");
				String refno = rs.getString("REF_NUMBER");
				String glaccount = rs.getString("GL_ACOUNT");
				String description = rs.getString("DESCRIPTION");
				double amount =  rs.getDouble("AMOUNT");
				String createdate = rs.getString("CREATE_DATE");
				String type = rs.getString("TYPE");
				String branchno = rs.getString("BRANCH_NO");
				String posted =rs.getString("POSTED");
				String expglaccount = rs.getString("EXPENSE_ACCT");
				ImprestItems ImprestItems = new ImprestItems();
				ImprestItems.setMtId(mtid);
				ImprestItems.setGlAccount(glaccount);
				ImprestItems.setRefNumber(refno);
				ImprestItems.setDescription(description);
				ImprestItems.setType(type);
				ImprestItems.setCreatedate(createdate);
				ImprestItems.setAmount(amount);
				ImprestItems.setCompanycode(companyCode);
				ImprestItems.setExpglaccount(expglaccount);
				ImprestItems.setOtheracct(expglaccount);
//				System.out.println("getImprestItemsRecord expglaccount:>>>>>>>>>> "+expglaccount+"  MTID: "+mtid+"     description: "+description);
				_list.add(ImprestItems);
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
	return _list;
}
public java.util.ArrayList<ImprestItems> getPayableImprestItemsRecord(String orderNo)
{
	java.util.ArrayList<ImprestItems> _list = new java.util.ArrayList<ImprestItems>();
	String date = String.valueOf(df.dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String query = "SELECT  * from IA_PAYABLE_IMPREST_ITEMS where POSTED = 'N' AND ORDER_NO = '"+orderNo+"' ";
//	Transaction transaction = null;

	Connection c = null;
	ResultSet rs = null;
	Statement s = null;

	try {
		    c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next())
			   {				
//				String transactionid = rs.getString("TRANSACTION_ID");
				String mtid = rs.getString("MTID");
				String companyCode = rs.getString("COMP_CODE");
				String refno = rs.getString("REF_NUMBER");
				String glaccount = rs.getString("GL_ACOUNT");
				String description = rs.getString("DESCRIPTION");
				double amount =  rs.getDouble("AMOUNT");
				String createdate = rs.getString("CREATE_DATE");
				String type = rs.getString("TYPE");
				String branchno = rs.getString("BRANCH_NO");
				String posted =rs.getString("POSTED");
				String expglaccount = rs.getString("EXPENSE_ACCT");
				ImprestItems ImprestItems = new ImprestItems();
				ImprestItems.setMtId(mtid);
				ImprestItems.setGlAccount(glaccount);
				ImprestItems.setRefNumber(refno);
				ImprestItems.setDescription(description);
				ImprestItems.setType(type);
				ImprestItems.setCreatedate(createdate);
				ImprestItems.setAmount(amount);
				ImprestItems.setCompanycode(companyCode);
				ImprestItems.setExpglaccount(expglaccount);
				ImprestItems.setOtheracct(expglaccount);
//				System.out.println("getImprestItemsRecord expglaccount:>>>>>>>>>> "+expglaccount+"  MTID: "+mtid+"     description: "+description);
				_list.add(ImprestItems);
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
	return _list;
}
public boolean createInventoryHistory(String Item_TypeCode,String Warehouse_Code,String user_id)
{

	Connection con = null;
	PreparedStatement ps = null;
	boolean done = false;
	String query = "INSERT INTO [IA_INVENTORY_HISTORY]([MTID], [ITEM_CODE],[TRANS_DESC], [QUANTITY],[WAREHOUSE_CODE]," +
			"[TRANS_DATE],[USERID],[BATCH_CODE])"
			+ "   VALUES (?,?,?,?,?,?,?,?)";

	try {
		con = getConnection();
		ps = con.prepareStatement(query);
		ps.setString(1, new ApplicationHelper().getGeneratedId("IA_INVENTORY_HISTORY"));
		ps.setString(2, Item_TypeCode);
		ps.setString(3, "NEW INVENTORY");
		ps.setString(4, "1");
		ps.setString(5, Warehouse_Code);
		ps.setDate(6, df.dateConvert(new java.util.Date()));
		ps.setString(7, user_id);
		ps.setString(8, "1");
		done = (ps.executeUpdate() != -1);

	} catch (Exception e) {
		System.out.println("WARNING:Error creating IA_INVENTORY_HISTORY ->"
				+ e.getMessage());
	} finally {
		closeConnection(con, ps);
	}
	return done;
}

public boolean createGLHistory(String drBranch, int drCurrCode, String drAcctType, String drAcctNo, String drTranCode, String drSBU, String drNarration, 
        double drAmount, String drReference, double drAcctExchRate, double drSysExchRate, 
        String crBranch, int crCurrCode, String crAcctType, String crAcctNo, String crTranCode, String crSBU, String crNarration, 
        double crAmount, double crAcctExchRate, double crSysExchRate, String effDate, 
        int userId,String companyCode,String Term,String OtherlegAcct,String receiptNo)
{
    String GLHistoryquery;
    Connection con;
    PreparedStatement ps;
    boolean done;
    String familyID;
//    System.out.println("drAcctNo: "+drAcctNo+"  OtherlegAcct: "+OtherlegAcct);
    GLHistoryquery = "INSERT INTO IA_GL_HISTORY (BRANCH_NO,ACCT_TYPE,GL_ACCT_NO,TRAN_CODE,SBU_CODE,DES" +
    "CRIPTION,AMT,REFERENCE,EFFECTIVE_DT,USERID,CREATE_DT,currency_id,sell_" +
    "act_exch_rate,sell_sys_exch_rate,MTID,sys_tran_code,orig_branch_no,COMP_CODE,OTHERACCTLEG,BRANCH_CODE,RECEIPT_NO)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
;       
    String isAutoGen = getCodeName("SELECT auto_generate_ID FROM IA_GB_COMPANY");
    con = null;
    ps = null;
    done = false;
    int i,j,k;
    try{  
    String glid = helper.getGeneratedId("IA_GL_HISTORY");
//      System.out.println("MTID=== "+pymtid);
//      familyID = glid;
    con = getConnection();
    done = con.getAutoCommit();
    con.setAutoCommit(false);
    done = false;
    ps = con.prepareStatement(GLHistoryquery);
    ps.setString(1, crBranch);
    ps.setString(2, crAcctType);
    ps.setString(3, crAcctNo);
    ps.setString(4, crTranCode);
    ps.setString(5, crSBU);
    ps.setString(6, crNarration);
    ps.setDouble(7, crAmount);
    ps.setString(8, drReference);
    ps.setDate(9, dateConvert(new java.util.Date()));
    ps.setInt(10, userId);
    ps.setDate(11, dateConvert(new java.util.Date()));
    ps.setInt(12, crCurrCode);
    ps.setDouble(13, crAcctExchRate);
    ps.setDouble(14, crSysExchRate);
    ps.setInt(15, Integer.parseInt(glid));
    ps.setInt(16, Integer.parseInt(crTranCode));
    ps.setString(17, crBranch);
    ps.setString(18, companyCode);
    ps.setString(19, OtherlegAcct);
    ps.setString(20, crBranch);
    ps.setString(21, receiptNo);
 //   ps.addBatch();
    //done = ps.executeBatch().length != -1;
  //  done = (ps.execute(GLHistoryquery));
    j = ps.executeUpdate();        
 
 if(j != -1)
 {
   con.commit();
   con.setAutoCommit(done);
   done = true;
  }
// System.out.println("done 2=== "+done);
    closeConnection(con, ps);
	}catch(Exception ex){
    System.out.println((new StringBuilder()).append("ERROR Creating Transaction Posting in createGLHistory.. ").append(ex.getMessage()).toString());
    ex.printStackTrace();
    done = false;
    closeConnection(con, ps);
	} finally{
    closeConnection(con, ps);
	}
    return done;
}   

public boolean createStudentHistory(String AppNo,String Branch, String AcctType, String AcctNo, String custType, String Narration, 
        double SumAmount, double AcctExchRate, double SysExchRate, String userId, String companyCode, String term, 
        double accountBalance, String tranCode, String familyid, String InventType,String receiptNo,String reference,String offsetAcct)
{
    Connection con;
    PreparedStatement ps;
    boolean done;
    String familyID;
    String StudentHistQuery = "INSERT INTO IA_CUSTOMER_ACCOUNT_TRAN_HIST (MTID,ACCOUNT_TYPE,CUST_ACCOUNT_NO,TRAN_CODE,"+
    "DESCRIPTION,AMOUNT,EFFECTIVE_DATE,USERID,CREATE_DATE,INV_ITEM_TYPE,BRANCH_NO,COMP_CODE,CUST_TYPE,BALANCE,POSTING_DATE,REFERENCE,OFFSET_ACCT_TYPE)" +
    "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; 
//     System.out.println("StudentHistQuery accountBalance=== "+accountBalance);
   // String isAutoGen = getCodeName("SELECT auto_generate_ID FROM IA_GB_COMPANY");
//    System.out.println("AcctNo: "+AcctNo+"  accountBalance: "+accountBalance);
    con = null;
    ps = null;
    done = false;
    int i;
    try{  
//    String glid = helper.getGeneratedId("IA_GL_HISTORY");
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
//   System.out.println("userId: "+userId);
    ps.setInt(8,Integer.parseInt(userId));        
    ps.setDate(9,dateConvert(new java.util.Date()));
    ps.setString(10,InventType);
	ps.setString(11,Branch);
	ps.setString(12,companyCode);
//	ps.setString(13,"ACTIVE");
	ps.setString(13,custType);
	double totalbal = SumAmount + accountBalance;
	ps.setDouble(14,totalbal);
    ps.setDate(15,dateConvert(new java.util.Date()));
  //  ps.setString(16, receiptNo);
    ps.setString(16, reference);
    ps.setString(17, offsetAcct);
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

}
