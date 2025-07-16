package  com.magbel.ia.bus;


import java.sql.*;
import java.text.SimpleDateFormat;
import  com.magbel.ia.vao.CustomerAccountSetup;
//import com.magbel.ia.vao.MandatoryField;
//import com.magbel.ia.vao.ErrorCode;
import com.magbel.ia.dao.PersistenceServiceDAO;
import com.magbel.ia.util.ApplicationHelper;
import  java.util.ArrayList;



public class CustomerAccountSetupHandler extends PersistenceServiceDAO //com.magbel.ia.dao.Config{
  {
    SimpleDateFormat sdf;
    final String space = "  ";
    final String comma = ",";
    java.util.Date dat;
   private  ApplicationHelper helper;
			
	
    public   CustomerAccountSetupHandler()
		{
        dat = new java.util.Date();
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        String date  = dat.toString();
		helper = new ApplicationHelper();
       // System.out.println("USING_ " + this.getClass().getName()+"\n Software written by" 
                             //  	+"  Ogey Bolaji. L @ " +date );
		//System.out.println(" All rights reserved ");
		}
		
	
		
      public java.util.ArrayList   getAllCustomerAccountSetups() {
        java.util.ArrayList  records = new java.util.ArrayList();
        com.magbel.ia.vao.CustomerAccountSetup    customerAccountSetup = null;
		
		 Connection con = null;
    Statement stmt = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
		try {
            
			con = getConnection();
			
        String SELECT_QUERY  = " SELECT * FROM   IA_CUSTOMER_ACCOUNT_SETUP  ORDER BY MTID ";
		stmt = con.createStatement();
		
        rs = stmt.executeQuery(SELECT_QUERY);
		
        while (rs.next())
    		{
                
                String mtId = rs.getString("MTID");
                String customerCode = rs.getString("CUSTOMER_CODE");
				String  customerClass  =  rs.getString("CUSTOMER_CLASS");
				String customerType = rs.getString("CUSTOMER_TYPE");
				String accountType = rs.getString("ACCOUNT_TYPE");
				String accountTypeCode = rs.getString("ACCOUNT_TYPE_CODE");
				String accountNo = rs.getString("ACCOUNT_NO");
                String  oldAccountNo = rs.getString("OLD_ACCOUNT_NO");
                String  accountName = rs.getString("ACCOUNT_NAME");
                String  currencyCode = rs.getString("CURRENCY_CODE");
				String  currency = rs.getString("CURRENCY");
				double  accountBalance  =  rs.getDouble("ACCOUNT_BALANCE");
				double  perctDiscount  =  rs.getDouble("PERCT_DISCOUNT");
                String  industryCode  =  rs.getString("INDUSTRY_CODE");
				String  industry  =  rs.getString("INDUSTRY");
                String  lastTranDate  =  rs.getString("LAST_TRAN_DATE");
                String   witholding  =  rs.getString("WITHOLDING");
				String   witholdingType  =  rs.getString("WITHOLDING_TYPE");
				String   vat  =  rs.getString("VAT");
				String   status   =   rs.getString("STATUS");
                String   createDate   =   formatDate(rs.getDate("CREATE_DATE"));
                String   effectiveDate  =  formatDate(rs.getDate("EFFECTIVE_DATE"));
                int userId   =  rs.getInt("USERID");
				String  accountOfficer =  rs.getString("ACCOUNT_OFFICER");
				int  transactionCount  =  rs.getInt("TRANSACTION_COUNT");
           
				
                 customerAccountSetup  = new  com.magbel.ia.vao.CustomerAccountSetup();
				
				  customerAccountSetup.setMtId(mtId);
				   customerAccountSetup.setCustomerNo(customerCode);
				   customerAccountSetup.setCustomerClass(customerClass);
				   customerAccountSetup.setCustomerType(customerType);
				   customerAccountSetup.setAccountType(accountType);
				   customerAccountSetup.setAccountTypeCode(accountTypeCode);
				   customerAccountSetup.setAccountNo(accountNo);
				    customerAccountSetup.setOldAccountNo(oldAccountNo);
				    customerAccountSetup.setAccountName(accountName);
				     customerAccountSetup.setCurrencyCode(currencyCode);
					  customerAccountSetup.setCurrency(currency);
					   customerAccountSetup.setAccountBalance(accountBalance);
					    customerAccountSetup.setPerctDiscount(perctDiscount);
					     customerAccountSetup.setIndustryCode(industryCode);
						  customerAccountSetup.setIndustry(industry);
						   customerAccountSetup.setLastTransDate(lastTranDate);
				 customerAccountSetup.setWitholding(witholding);
				 customerAccountSetup.setWitholdingType(witholdingType);
				 customerAccountSetup.setVat(vat);
				 customerAccountSetup.setStatus(status);
				 customerAccountSetup.setCreateDate(createDate);
				 customerAccountSetup.setEffectiveDate(effectiveDate);
				 customerAccountSetup.setUserId(userId);
				 customerAccountSetup.setAccountOfficer(accountOfficer);
				  customerAccountSetup.setTransactionCount(transactionCount);
				 
                records.add(customerAccountSetup);
		    }
   } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(con, stmt, rs);
        }
        return  records;
}





  public java.util.ArrayList   getCustomerAccountSetupByCustomerCode(String  CustomerCode) {
        java.util.ArrayList  records = new java.util.ArrayList();
        com.magbel.ia.vao.CustomerAccountSetup   customerAccountSetup = null;
		
		 Connection con = null;
    Statement stmt = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
		try {
            
			con = getConnection();
			 
        String SELECT_QUERY  = " SELECT * FROM   IA_CUSTOMER_ACCOUNT_SETUP  WHERE  CUSTOMER_CODE = '"
		                      +CustomerCode+"'";
		stmt = con.createStatement();
		
        rs = stmt.executeQuery(SELECT_QUERY);
		
        while (rs.next())
    		{
                
                
                String mtId = rs.getString("MTID");
                String customerCode = rs.getString("CUSTOMER_CODE");
			   String  customerClass  =  rs.getString("CUSTOMER_CLASS");
				String customerType = rs.getString("CUSTOMER_TYPE");
				String accountType = rs.getString("ACCOUNT_TYPE");
				String accountTypeCode = rs.getString("ACCOUNT_TYPE_CODE");
				String accountNo = rs.getString("ACCOUNT_NO");
                String  oldAccountNo = rs.getString("OLD_ACCOUNT_NO");
                String  accountName = rs.getString("ACCOUNT_NAME");
                String  currencyCode = rs.getString("CURRENCY_CODE");
				String  currency = rs.getString("CURRENCY");
				double  accountBalance  =  rs.getDouble("ACCOUNT_BALANCE");
				double  perctDiscount  =  rs.getDouble("PERCT_DISCOUNT");
                String  industryCode  =  rs.getString("INDUSTRY_CODE");
				String  industry  =  rs.getString("INDUSTRY");
                String  lastTranDate  =  rs.getString("LAST_TRAN_DATE");
                String   witholding  =  rs.getString("WITHOLDING");
				String   witholdingType  =  rs.getString("WITHOLDING_TYPE");
				String   vat  =  rs.getString("VAT");
				String   status   =   rs.getString("STATUS");
                String   createDate   =   formatDate(rs.getDate("CREATE_DATE"));
                String   effectiveDate  =  formatDate(rs.getDate("EFFECTIVE_DATE"));
                int userId   =  rs.getInt("USERID");
				String  accountOfficer =  rs.getString("ACCOUNT_OFFICER");
				int  transactionCount  =  rs.getInt("TRANSACTION_COUNT");
				
           
				
                 customerAccountSetup  = new  com.magbel.ia.vao.CustomerAccountSetup();
				
				  customerAccountSetup.setMtId(mtId);
				   customerAccountSetup.setCustomerNo(customerCode);
				   customerAccountSetup.setCustomerClass(customerClass);
				    customerAccountSetup.setCustomerType(customerType);
				   customerAccountSetup.setAccountType(accountType);
				   customerAccountSetup.setAccountTypeCode(accountTypeCode);
				   customerAccountSetup.setAccountNo(accountNo);
				    customerAccountSetup.setOldAccountNo(oldAccountNo);
				    customerAccountSetup.setAccountName(accountName);
				     customerAccountSetup.setCurrencyCode(currencyCode);
					  customerAccountSetup.setCurrency(currency);
					   customerAccountSetup.setAccountBalance(accountBalance);
					    customerAccountSetup.setPerctDiscount(perctDiscount);
					     customerAccountSetup.setIndustryCode(industryCode);
						  customerAccountSetup.setIndustry(industry);
						   customerAccountSetup.setLastTransDate(lastTranDate);
				 customerAccountSetup.setWitholding(witholding);
				 customerAccountSetup.setWitholdingType(witholdingType);
				 customerAccountSetup.setVat(vat);
				 customerAccountSetup.setStatus(status);
				 customerAccountSetup.setCreateDate(createDate);
				 customerAccountSetup.setEffectiveDate(effectiveDate);
				 customerAccountSetup.setUserId(userId);
				 customerAccountSetup.setAccountOfficer(accountOfficer);
				  customerAccountSetup.setTransactionCount(transactionCount);					   
				 
                records.add(customerAccountSetup);
		    }
   } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(con, stmt, rs);
        }
        return  records;
}



public java.util.ArrayList   getCustomerAccountSetupByStatus(String  Status) {
        java.util.ArrayList  records = new java.util.ArrayList();
        com.magbel.ia.vao.CustomerAccountSetup    customerAccountSetup = null;
		
		 Connection con = null;
    Statement stmt = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
		try {
            
			con = getConnection();
				 
        String SELECT_QUERY  = " SELECT * FROM   IA_CUSTOMER_ACCOUNT_SETUP  WHERE  STATUS = '"
		                      +Status+"'";
		stmt = con.createStatement();
		
        rs = stmt.executeQuery(SELECT_QUERY);
		
        while (rs.next())
    		{
                
                
                String mtId = rs.getString("MTID");
                String customerCode = rs.getString("CUSTOMER_CODE");
			   String  customerClass  =  rs.getString("CUSTOMER_CLASS");
				String customerType = rs.getString("CUSTOMER_TYPE");
				String accountType = rs.getString("ACCOUNT_TYPE");
				String accountTypeCode = rs.getString("ACCOUNT_TYPE_CODE");
				String accountNo = rs.getString("ACCOUNT_NO");
                String  oldAccountNo = rs.getString("OLD_ACCOUNT_NO");
                String  accountName = rs.getString("ACCOUNT_NAME");
                String  currencyCode = rs.getString("CURRENCY_CODE");
				String  currency = rs.getString("CURRENCY");
				double  accountBalance  =  rs.getDouble("ACCOUNT_BALANCE");
				double  perctDiscount  =  rs.getDouble("PERCT_DISCOUNT");
                String  industryCode  =  rs.getString("INDUSTRY_CODE");
				String  industry  =  rs.getString("INDUSTRY");
                String  lastTranDate  =  rs.getString("LAST_TRAN_DATE");
                String   witholding  =  rs.getString("WITHOLDING");
				String   witholdingType  =  rs.getString("WITHOLDING_TYPE");
				String   vat  =  rs.getString("VAT");
				String   status   =   rs.getString("STATUS");
                String   createDate   =   formatDate(rs.getDate("CREATE_DATE"));
                String   effectiveDate  =  formatDate(rs.getDate("EFFECTIVE_DATE"));
                int userId   =  rs.getInt("USERID");
				String  accountOfficer =  rs.getString("ACCOUNT_OFFICER");
				int  transactionCount  =  rs.getInt("TRANSACTION_COUNT");
           
				
                 customerAccountSetup  = new  com.magbel.ia.vao.CustomerAccountSetup();
				
				  customerAccountSetup.setMtId(mtId);
				   customerAccountSetup.setCustomerNo(customerCode);
				   customerAccountSetup.setCustomerClass(customerClass);
				    customerAccountSetup.setCustomerType(customerType);
				   customerAccountSetup.setAccountType(accountType);
				   customerAccountSetup.setAccountTypeCode(accountTypeCode);
				   customerAccountSetup.setAccountNo(accountNo);
				    customerAccountSetup.setOldAccountNo(oldAccountNo);
				    customerAccountSetup.setAccountName(accountName);
				     customerAccountSetup.setCurrencyCode(currencyCode);
					  customerAccountSetup.setCurrency(currency);
					   customerAccountSetup.setAccountBalance(accountBalance);
					    customerAccountSetup.setPerctDiscount(perctDiscount);
					     customerAccountSetup.setIndustryCode(industryCode);
						  customerAccountSetup.setIndustry(industry);
						   customerAccountSetup.setLastTransDate(lastTranDate);
				  customerAccountSetup.setWitholding(witholding);
				 customerAccountSetup.setWitholdingType(witholdingType);
				 customerAccountSetup.setVat(vat);
				 customerAccountSetup.setStatus(status);
				 customerAccountSetup.setCreateDate(createDate);
				 customerAccountSetup.setEffectiveDate(effectiveDate);
				 customerAccountSetup.setUserId(userId);
				 customerAccountSetup.setAccountOfficer(accountOfficer);
				  customerAccountSetup.setTransactionCount(transactionCount);						   
								   
				 
                records.add(customerAccountSetup);
		    }
   } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(con, stmt, rs);
        }
        return  records;
}







     public   CustomerAccountSetup   getCustomerAccountSetupById(String MtId) {
        //java.util.ArrayList  records = new java.util.ArrayList();
        com.magbel.ia.vao.CustomerAccountSetup      customerAccountSetup = null;
   Connection con = null;
    Statement stmt = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
	
		try {
           
			con = getConnection();
		 
        String SELECT_QUERY  = " SELECT * FROM  IA_CUSTOMER_ACCOUNT_SETUP  WHERE MTID = '"+MtId+"'";
		stmt = con.createStatement();
		
        rs = stmt.executeQuery(SELECT_QUERY);
		
        while (rs.next())
    		{
                   
               
                String mtId = rs.getString("MTID");
                String customerCode = rs.getString("CUSTOMER_CODE");
			    String  customerClass  =  rs.getString("CUSTOMER_CLASS");
				String customerType = rs.getString("CUSTOMER_TYPE");
				String accountType = rs.getString("ACCOUNT_TYPE");
				String accountTypeCode = rs.getString("ACCOUNT_TYPE_CODE");
				String accountNo = rs.getString("ACCOUNT_NO");
                String  oldAccountNo = rs.getString("OLD_ACCOUNT_NO");
                String  accountName = rs.getString("ACCOUNT_NAME");
                String  currencyCode = rs.getString("CURRENCY_CODE");
				String  currency = rs.getString("CURRENCY");
				double  accountBalance  =  rs.getDouble("ACCOUNT_BALANCE");
				double  perctDiscount  =  rs.getDouble("PERCT_DISCOUNT");
                String  industryCode  =  rs.getString("INDUSTRY_CODE");
				String  industry  =  rs.getString("INDUSTRY");
                String  lastTranDate  =  rs.getString("LAST_TRAN_DATE");
                String   witholding  =  rs.getString("WITHOLDING");
				String   witholdingType  =  rs.getString("WITHOLDING_TYPE");
				String   vat  =  rs.getString("VAT");
				String   status   =   rs.getString("STATUS");
                String   createDate   =   formatDate(rs.getDate("CREATE_DATE"));
                String   effectiveDate  =  formatDate(rs.getDate("EFFECTIVE_DATE"));
                int userId   =  rs.getInt("USERID");
				String  accountOfficer =  rs.getString("ACCOUNT_OFFICER");
				int  transactionCount  =  rs.getInt("TRANSACTION_COUNT");
           
				
                 customerAccountSetup  = new  com.magbel.ia.vao.CustomerAccountSetup();
				
				  customerAccountSetup.setMtId(mtId);
				   customerAccountSetup.setCustomerNo(customerCode);
				   customerAccountSetup.setCustomerClass(customerClass);
				    customerAccountSetup.setCustomerType(customerType);
				   customerAccountSetup.setAccountType(accountType);
				   customerAccountSetup.setAccountTypeCode(accountTypeCode);
				   customerAccountSetup.setAccountNo(accountNo);
				    customerAccountSetup.setOldAccountNo(oldAccountNo);
				    customerAccountSetup.setAccountName(accountName);
				     customerAccountSetup.setCurrencyCode(currencyCode);
					  customerAccountSetup.setCurrency(currency);
					   customerAccountSetup.setAccountBalance(accountBalance);
					    customerAccountSetup.setPerctDiscount(perctDiscount);
					     customerAccountSetup.setIndustryCode(industryCode);
						  customerAccountSetup.setIndustry(industry);
						   customerAccountSetup.setLastTransDate(lastTranDate);
				  customerAccountSetup.setWitholding(witholding);
				 customerAccountSetup.setWitholdingType(witholdingType);
				 customerAccountSetup.setVat(vat);
				 customerAccountSetup.setStatus(status);
				 customerAccountSetup.setCreateDate(createDate);
				 customerAccountSetup.setEffectiveDate(effectiveDate);
				 customerAccountSetup.setUserId(userId);
				  customerAccountSetup.setAccountOfficer(accountOfficer);
				  customerAccountSetup.setTransactionCount(transactionCount);
			}
   } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(con, stmt, rs);
        }
        return     customerAccountSetup;
}





 public   CustomerAccountSetup   getCustomerAccountSetupByAccountNo(String AccountNo) {
        //java.util.ArrayList  records = new java.util.ArrayList();
        com.magbel.ia.vao.CustomerAccountSetup      customerAccountSetup = null;
   Connection con = null;
    Statement stmt = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
	
		try {
           
			con = getConnection();
			 
        String SELECT_QUERY  = " SELECT * FROM  IA_CUSTOMER_ACCOUNT_SETUP  WHERE ACCOUNT_NO = '"
		             +AccountNo+"'";
		stmt = con.createStatement();
		
        rs = stmt.executeQuery(SELECT_QUERY);
		
        while (rs.next())
    		{
                   
              
                String mtId = rs.getString("MTID");
                String customerCode = rs.getString("CUSTOMER_CODE");
				String  customerClass  =  rs.getString("CUSTOMER_CLASS");
				String customerType = rs.getString("CUSTOMER_TYPE");
				String accountType = rs.getString("ACCOUNT_TYPE");
				String accountTypeCode = rs.getString("ACCOUNT_TYPE_CODE");
				String accountNo = rs.getString("ACCOUNT_NO");
                String  oldAccountNo = rs.getString("OLD_ACCOUNT_NO");
                String  accountName = rs.getString("ACCOUNT_NAME");
                String  currencyCode = rs.getString("CURRENCY_CODE");
				String  currency = rs.getString("CURRENCY");
				double  accountBalance  =  rs.getDouble("ACCOUNT_BALANCE");
				double  perctDiscount  =  rs.getDouble("PERCT_DISCOUNT");
                String  industryCode  =  rs.getString("INDUSTRY_CODE");
				String  industry  =  rs.getString("INDUSTRY");
                String  lastTranDate  =  rs.getString("LAST_TRAN_DATE");
                String   witholding  =  rs.getString("WITHOLDING");
				String   witholdingType  =  rs.getString("WITHOLDING_TYPE");
				String   vat  =  rs.getString("VAT");
				String   status   =   rs.getString("STATUS");
                String   createDate   =   formatDate(rs.getDate("CREATE_DATE"));
                String   effectiveDate  =  formatDate(rs.getDate("EFFECTIVE_DATE"));
                int userId   =  rs.getInt("USERID");
				String  accountOfficer =  rs.getString("ACCOUNT_OFFICER");
				int  transactionCount  =  rs.getInt("TRANSACTION_COUNT");
           
				
                 customerAccountSetup  = new  com.magbel.ia.vao.CustomerAccountSetup();
				
				  customerAccountSetup.setMtId(mtId);
				   customerAccountSetup.setCustomerNo(customerCode);
				   customerAccountSetup.setCustomerClass(customerClass);
				    customerAccountSetup.setCustomerType(customerType);
				   customerAccountSetup.setAccountType(accountType);
				   customerAccountSetup.setAccountTypeCode(accountTypeCode);
				   customerAccountSetup.setAccountNo(accountNo);
				    customerAccountSetup.setOldAccountNo(oldAccountNo);
				    customerAccountSetup.setAccountName(accountName);
				     customerAccountSetup.setCurrencyCode(currencyCode);
					  customerAccountSetup.setCurrency(currency);
					   customerAccountSetup.setAccountBalance(accountBalance);
					    customerAccountSetup.setPerctDiscount(perctDiscount);
					     customerAccountSetup.setIndustryCode(industryCode);
						  customerAccountSetup.setIndustry(industry);
						   customerAccountSetup.setLastTransDate(lastTranDate);
				  customerAccountSetup.setWitholding(witholding);
				 customerAccountSetup.setWitholdingType(witholdingType);
				 customerAccountSetup.setVat(vat);
				 customerAccountSetup.setStatus(status);
				 customerAccountSetup.setCreateDate(createDate);
				 customerAccountSetup.setEffectiveDate(effectiveDate);
				 customerAccountSetup.setUserId(userId);
				  customerAccountSetup.setAccountOfficer(accountOfficer);
				  customerAccountSetup.setTransactionCount(transactionCount);
				
				
			}
   } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(con, stmt, rs);
        }
        return     customerAccountSetup;
}



 public java.util.ArrayList  getCustomerAccountSetupsByQuery(String query){
        java.util.ArrayList records = new java.util.ArrayList();
        com.magbel.ia.vao.CustomerAccountSetup    customerAccountSetup = null;
		 String SELECT_QUERY  =  query;
		
        Connection con = null;
        ResultSet rs = null;
        Statement stmt = null;

        try {
           con = getConnection();
		
            stmt = con.createStatement();
            rs = stmt.executeQuery(SELECT_QUERY);
              while (rs.next())
    		{
                
                String mtId = rs.getString("MTID");
                String customerCode = rs.getString("CUSTOMER_CODE");
				String  customerClass  =  rs.getString("CUSTOMER_Class");
				String customerType = rs.getString("CUSTOMER_TYPE");
				String accountType = rs.getString("ACCOUNT_TYPE");
				String accountTypeCode = rs.getString("ACCOUNT_TYPE_CODE");
				String accountNo = rs.getString("ACCOUNT_NO");
                String  oldAccountNo = rs.getString("OLD_ACCOUNT_NO");
                String  accountName = rs.getString("ACCOUNT_NAME");
                String  currencyCode = rs.getString("CURRENCY_CODE");
				String  currency = rs.getString("CURRENCY");
				double  accountBalance  =  rs.getDouble("ACCOUNT_BALANCE");
				double  perctDiscount  =  rs.getDouble("PERCT_DISCOUNT");
                String  industryCode  =  rs.getString("INDUSTRY_CODE");
				String  industry  =  rs.getString("INDUSTRY");
                String  lastTranDate  =  rs.getString("LAST_TRAN_DATE");
               String   witholding  =  rs.getString("WITHOLDING");
				String   witholdingType  =  rs.getString("WITHOLDING_TYPE");
				String   vat  =  rs.getString("VAT");
				String   status   =   rs.getString("STATUS");
                String   createDate   =   formatDate(rs.getDate("CREATE_DATE"));
                String   effectiveDate  =  formatDate(rs.getDate("EFFECTIVE_DATE"));
                int userId   =  rs.getInt("USERID");
				String  accountOfficer =  rs.getString("ACCOUNT_OFFICER");
				int  transactionCount  =  rs.getInt("TRANSACTION_COUNT");
           
				
                 customerAccountSetup  = new  com.magbel.ia.vao.CustomerAccountSetup();
				
				  customerAccountSetup.setMtId(mtId);
				   customerAccountSetup.setCustomerNo(customerCode);
				   customerAccountSetup.setCustomerClass(customerClass);
				    customerAccountSetup.setCustomerType(customerType);
				   customerAccountSetup.setAccountType(accountType);
				   customerAccountSetup.setAccountTypeCode(accountTypeCode);
				   customerAccountSetup.setAccountNo(accountNo);
				    customerAccountSetup.setOldAccountNo(oldAccountNo);
				    customerAccountSetup.setAccountName(accountName);
				     customerAccountSetup.setCurrencyCode(currencyCode);
					  customerAccountSetup.setCurrency(currency);
					   customerAccountSetup.setAccountBalance(accountBalance);
					    customerAccountSetup.setPerctDiscount(perctDiscount);
					     customerAccountSetup.setIndustryCode(industryCode);
						  customerAccountSetup.setIndustry(industry);
						   customerAccountSetup.setLastTransDate(lastTranDate);
				 customerAccountSetup.setWitholding(witholding);
				 customerAccountSetup.setWitholdingType(witholdingType);
				 customerAccountSetup.setVat(vat);
				 customerAccountSetup.setStatus(status);
				 customerAccountSetup.setCreateDate(createDate);
				 customerAccountSetup.setEffectiveDate(effectiveDate);
				 customerAccountSetup.setUserId(userId);
				 customerAccountSetup.setAccountOfficer(accountOfficer);
				  customerAccountSetup.setTransactionCount(transactionCount);
				  
                records.add(customerAccountSetup);

				}

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(con, stmt, rs);
        }
       return records;
    }
	
	
	
 public java.util.ArrayList   getCustomerAccountSetupByQuery(String filter) {
        java.util.ArrayList records = new java.util.ArrayList();
        com.magbel.ia.vao.CustomerAccountSetup    customerAccountSetup = null;
		 String SELECT_QUERY  = null;
		if(filter != null)
		   { SELECT_QUERY = "SELECT * FROM  IA_CUSTOMER_ACCOUNT_SETUP " + filter;	}
		else { SELECT_QUERY = "SELECT * FROM   IA_CUSTOMER_ACCOUNT_SETUP ";  }

        Connection con = null;
        ResultSet rs = null;
        Statement stmt = null;

        try {
           con = getConnection();
		
            stmt = con.createStatement();
            rs = stmt.executeQuery(SELECT_QUERY);
              while (rs.next())
    		{
                
                String mtId = rs.getString("MTID");
                String customerCode = rs.getString("CUSTOMER_CODE");
				String  customerClass  =  rs.getString("CUSTOMER_CLASS");
				String customerType = rs.getString("CUSTOMER_TYPE");
				String accountType = rs.getString("ACCOUNT_TYPE");
				String accountTypeCode = rs.getString("ACCOUNT_TYPE_CODE");
				String accountNo = rs.getString("ACCOUNT_NO");
                String  oldAccountNo = rs.getString("OLD_ACCOUNT_NO");
                String  accountName = rs.getString("ACCOUNT_NAME");
                String  currencyCode = rs.getString("CURRENCY_CODE");
				String  currency = rs.getString("CURRENCY");
				double  accountBalance  =  rs.getDouble("ACCOUNT_BALANCE");
				double  perctDiscount  =  rs.getDouble("PERCT_DISCOUNT");
                String  industryCode  =  rs.getString("INDUSTRY_CODE");
				String  industry  =  rs.getString("INDUSTRY");
                String  lastTranDate  =  rs.getString("LAST_TRAN_DATE");
              String   witholding  =  rs.getString("WITHOLDING");
				String   witholdingType  =  rs.getString("WITHOLDING_TYPE");
				String   vat  =  rs.getString("VAT");
				String   status   =   rs.getString("STATUS");
                String   createDate   =   formatDate(rs.getDate("CREATE_DATE"));
                String   effectiveDate  =  formatDate(rs.getDate("EFFECTIVE_DATE"));
                int userId   =  rs.getInt("USERID");
				String  accountOfficer =  rs.getString("ACCOUNT_OFFICER");
				int  transactionCount  =  rs.getInt("TRANSACTION_COUNT");
           
				
                 customerAccountSetup  = new  com.magbel.ia.vao.CustomerAccountSetup();
				
				  customerAccountSetup.setMtId(mtId);
				   customerAccountSetup.setCustomerNo(customerCode);
				   customerAccountSetup.setCustomerClass(customerClass);
				    customerAccountSetup.setCustomerType(customerType);
				   customerAccountSetup.setAccountType(accountType);
				   customerAccountSetup.setAccountTypeCode(accountTypeCode);
				   customerAccountSetup.setAccountNo(accountNo);
				    customerAccountSetup.setOldAccountNo(oldAccountNo);
				    customerAccountSetup.setAccountName(accountName);
				     customerAccountSetup.setCurrencyCode(currencyCode);
					  customerAccountSetup.setCurrency(currency);
					   customerAccountSetup.setAccountBalance(accountBalance);
					    customerAccountSetup.setPerctDiscount(perctDiscount);
					     customerAccountSetup.setIndustryCode(industryCode);
						  customerAccountSetup.setIndustry(industry);
						   customerAccountSetup.setLastTransDate(lastTranDate);
				 customerAccountSetup.setWitholding(witholding);
				 customerAccountSetup.setWitholdingType(witholdingType);
				 customerAccountSetup.setVat(vat);
				 customerAccountSetup.setStatus(status);
				 customerAccountSetup.setCreateDate(createDate);
				 customerAccountSetup.setEffectiveDate(effectiveDate);
				 customerAccountSetup.setUserId(userId);
				 customerAccountSetup.setAccountOfficer(accountOfficer);
				  customerAccountSetup.setTransactionCount(transactionCount);
				
                records.add(customerAccountSetup);

				}

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(con, stmt, rs);
        }
       return records;
    }



	
    public final boolean createCustomerAccountSetup(CustomerAccountSetup    customerAccountSetup)
	{
	   Connection con = null;
        ResultSet rs = null;
        Statement stmt = null;
		PreparedStatement ps = null;
		boolean  successfull  =  false;
		System.out.println("Creating  Customer Account Setup");
	  try{
	          		
		String INSERT_QUERY	 = "INSERT INTO   IA_CUSTOMER_ACCOUNT_SETUP (MTID, CUSTOMER_CODE, CUSTOMER_CLASS, "
		                    +" CUSTOMER_TYPE,  ACCOUNT_TYPE,  ACCOUNT_TYPE_CODE,  ACCOUNT_NO, "
							+" OLD_ACCOUNT_NO, ACCOUNT_NAME, CURRENCY_CODE,  CURRENCY, ACCOUNT_BALANCE, "
					   +" PERCT_DISCOUNT, INDUSTRY_CODE,  INDUSTRY,  LAST_TRAN_DATE, "
					   +" ACCOUNT_OFFICER,  TRANSACTION_COUNT, WITHOLDING,  WITHOLDING_TYPE,  VAT,   "
					   +"    STATUS, CREATE_DATE, EFFECTIVE_DATE, USERID "
                        +"  )  VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
					   
			
		   String mtId = helper.getGeneratedId("IA_CUSTOMER_ACCOUNT_SETUP");
		   String   customerCode  =   customerAccountSetup.getCustomerNo();
		    String   customerClass  =   customerAccountSetup.getCustomerClass();
		     String   customerType  =   customerAccountSetup.getCustomerType();
		   String   accountType  =   customerAccountSetup.getAccountType();
		   String   accountTypeCode  =   customerAccountSetup.getAccountTypeCode();
		   String  accountNo  =    customerAccountSetup.getAccountNo();
		   String  oldAccountNo  =   customerAccountSetup.getOldAccountNo();
                   String   accountName  =   customerAccountSetup.getAccountName();
                   String  currencyCode  =    customerAccountSetup.getCurrencyCode();
				   String  currency  =    customerAccountSetup.getCurrency();
                   double  accountBalance =   customerAccountSetup.getAccountBalance();
                   double  perctDiscount  =   customerAccountSetup.getPerctDiscount();
                   String  industryCode  =   customerAccountSetup.getIndustryCode();
				   String  industry  =   customerAccountSetup.getIndustry();
                   String  lastTransDate  =   customerAccountSetup.getLastTransDate();
                   String  witholding  =   customerAccountSetup.getWitholding();
                   String  witholdingType  =   customerAccountSetup.getWitholdingType();
                   String  vat  =   customerAccountSetup.getVat();
				 
                   String  status    =   customerAccountSetup.getStatus();
                   String  createDate =  formatDate(new java.util.Date());
                   String  effectiveDate  =   customerAccountSetup.getEffectiveDate();
				    if((effectiveDate == "") || (effectiveDate == null))
				      {   effectiveDate =  createDate;  }
                  int  userId =   customerAccountSetup.getUserId();
				  String  accountOfficer  =  customerAccountSetup.getAccountOfficer();
				  int  transactionCount  =  customerAccountSetup.getTransactionCount();
                    
          	con = getConnection();
			
		ps = con.prepareStatement(INSERT_QUERY);
		
		ps.setString(1, mtId);
		ps.setString(2, customerCode);
		ps.setString(3, customerClass);
		ps.setString(4, customerType);
		ps.setString(5, accountType);
		ps.setString(6, accountTypeCode);
		ps.setString(7, accountNo);
		ps.setString(8, oldAccountNo);
		ps.setString(9, accountName);
		ps.setString(10, currencyCode);
		ps.setString(11, currency);
		ps.setDouble(12, accountBalance);
		ps.setDouble(13, perctDiscount);
		ps.setString(14, industryCode);
		ps.setString(15, industry);
		ps.setDate(16, dateConvert(lastTransDate));
		ps.setString(17, accountOfficer);
		ps.setInt(18,  transactionCount);
		ps.setString(19, witholding);
         ps.setString(20, witholdingType);
        ps.setString(21, vat);
		ps.setString(22, status);
         ps.setDate(23, dateConvert(createDate));
        ps.setDate(24, dateConvert(effectiveDate));
		ps.setInt(25, userId);
		
		
		successfull =  ps.execute() ;
						
	   } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(con, ps, rs);
        }
       return  successfull;
    }
	
	
	
	public final boolean updateCustomerAccountSetup(CustomerAccountSetup     customerAccountSetup)
	{
	  Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
		boolean successfull =  false;
		System.out.println("Updating  Customer Account Setup");
	  try{
	          	
		String UPDATE_QUERY = " UPDATE   IA_CUSTOMER_ACCOUNT_SETUP  SET  CUSTOMER_CODE = ?, CUSTOMER_CLASS = ?, "
		          +" CUSTOMER_TYPE = ?,   ACCOUNT_TYPE = ?,  ACCOUNT_TYPE_CODE = ?,"
					+" ACCOUNT_NO = ?,   OLD_ACCOUNT_NO = ?,  ACCOUNT_NAME = ?,"
					+"  CURRENCY_CODE = ?, CURRENCY = ?,  ACCOUNT_BALANCE = ?, PERCT_DISCOUNT = ?, "
                     +" INDUSTRY_CODE = ?,   INDUSTRY = ?, LAST_TRAN_DATE = ?, "
                     +"    ACCOUNT_OFFICER = ?,  TRANSACTION_COUNT = ? ,WITHOLDING = ?,  "
					 +"  WITHOLDING_TYPE = ?,  VAT = ?, STATUS = ?  WHERE   MTID = ?  ";
                    
		
                   String  mtId =    customerAccountSetup.getMtId();
					String   customerCode  =   customerAccountSetup.getCustomerNo();
					String   customerClass  =   customerAccountSetup.getCustomerClass();
					 String   customerType  =   customerAccountSetup.getCustomerType();
					String   accountType  =   customerAccountSetup.getAccountType();
					String   accountTypeCode  =   customerAccountSetup.getAccountTypeCode();
					String  accountNo  =    customerAccountSetup.getAccountNo();
					String  oldAccountNo  =   customerAccountSetup.getOldAccountNo();
                   String   accountName  =   customerAccountSetup.getAccountName();
                   String  currencyCode  =    customerAccountSetup.getCurrencyCode();
				   String  currency  =    customerAccountSetup.getCurrency();
                   double  accountBalance =   customerAccountSetup.getAccountBalance();
                   double  perctDiscount  =   customerAccountSetup.getPerctDiscount();
                   String  industryCode  =   customerAccountSetup.getIndustryCode();
				   String  industry  =   customerAccountSetup.getIndustry();
                   String  lastTransDate  =   customerAccountSetup.getLastTransDate();
                   String  witholding  =   customerAccountSetup.getWitholding();
                   String  witholdingType  =   customerAccountSetup.getWitholdingType();
                   String  vat  =   customerAccountSetup.getVat();
                   String  status    =   customerAccountSetup.getStatus();
                   String  createDate =    customerAccountSetup.getCreateDate();
                   String  effectiveDate  =   customerAccountSetup.getEffectiveDate();
				     if((effectiveDate == "") || (effectiveDate == null))
				      {   effectiveDate =  createDate;  }
				     int  userId =   customerAccountSetup.getUserId();
					  String  accountOfficer  =  customerAccountSetup.getAccountOfficer();
				  int  transactionCount  =  customerAccountSetup.getTransactionCount();
		
		con = getConnection();
			
		ps = con.prepareStatement(UPDATE_QUERY);
		
        
		ps.setString(1, customerCode);
		ps.setString(2, customerClass);
		ps.setString(3, customerType);
		ps.setString(4, accountType);
		ps.setString(5, accountTypeCode);
		ps.setString(6, accountNo);
		ps.setString(7, oldAccountNo);
		ps.setString(8, accountName);
		ps.setString(9, currencyCode);
		ps.setString(10, currency);
		ps.setDouble(11, accountBalance);
		ps.setDouble(12, perctDiscount);
		ps.setString(13, industryCode);
		ps.setString(14, industry);
		ps.setDate(15, dateConvert(lastTransDate));
		ps.setString(16, accountOfficer);
		ps.setInt(17,  transactionCount);
		ps.setString(18, witholding);
        ps.setString(19, witholdingType);
         ps.setString(20, vat);
		 ps.setString(21, status);
		 
         ps.setString(22, mtId);
				
		
			successfull = (ps.executeUpdate()!= -1 );
						
	   } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(con, ps, rs);
        }
       return  successfull;
    }
	
	

public void closeConnection(Connection c, Statement st, ResultSet rs)
{
  	c = null;  st = null;  rs = null;
	
}
	

public void closeConnection(Connection c, PreparedStatement pst, ResultSet rs)
{
  	c = null;  pst = null;  rs = null;
	
}







public boolean  isMtIdExisting(String MtId) {
        
         
		 String  SELECT_QUERY = "SELECT count(MTID)  FROM   IA_CUSTOMER_ACCOUNT_SETUP  WHERE MTID = '"+MtId+"'";	
		        
        Connection con = null;
        ResultSet rs = null;
        Statement stmt = null;
		boolean exist = false;
         int icfId = 0;
		 int count = 0;
        try {
            con = getConnection();
			
            stmt = con.createStatement();
            rs = stmt.executeQuery(SELECT_QUERY);
			
			while (rs.next())
    		{
               count = rs.getInt("MTID");
			 }
			if(count > 0)
			  {  exist = true;	}
					    
        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(con, stmt, rs);
        }
        return  exist;
}











public boolean  isAccountNoExisting(String AccountNo) {
        
         
		 String  SELECT_QUERY = "SELECT  ACCOUNT_NO  FROM   IA_CUSTOMER_ACCOUNT_SETUP  WHERE ACCOUNT_NO = '"
		                       +AccountNo+"'";	
		        
        Connection con = null;
        ResultSet rs = null;
        Statement stmt = null;
		boolean exist = false;
         String acctNo = null;
		 int count = 0;
        try {
            con = getConnection();
		
            stmt = con.createStatement();
          rs = stmt.executeQuery(SELECT_QUERY);
			
			while (rs.next())
    		{
              acctNo = rs.getString("ACCOUNT_NO");
			 }
			 
			if(acctNo != null || acctNo != "")
			  {  exist = true;	}
					    
        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(con, stmt, rs);
        }
        return  exist;
}






public java.util.ArrayList   getCustomerAccountSetupByCustomerClass(String  CustomerClass) {
        java.util.ArrayList  records = new java.util.ArrayList();
        com.magbel.ia.vao.CustomerAccountSetup    customerAccountSetup = null;
		
		 Connection con = null;
    Statement stmt = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
		try {
            
			con = getConnection();
				 
        String SELECT_QUERY  = " SELECT * FROM   IA_CUSTOMER_ACCOUNT_SETUP  WHERE  CUSTOMER_CLASS = '"
		                      +CustomerClass+"'";
		stmt = con.createStatement();
		
        rs = stmt.executeQuery(SELECT_QUERY);
		
        while (rs.next())
    		{
                
                
                String mtId = rs.getString("MTID");
                String customerCode = rs.getString("CUSTOMER_CODE");
			    String  customerClass  =  rs.getString("CUSTOMER_CLASS");
			   String  customerType  =  rs.getString("CUSTOMER_TYPE");
				String accountType = rs.getString("ACCOUNT_TYPE");
				String accountTypeCode = rs.getString("ACCOUNT_TYPE_CODE");
				String accountNo = rs.getString("ACCOUNT_NO");
                String  oldAccountNo = rs.getString("OLD_ACCOUNT_NO");
                String  accountName = rs.getString("ACCOUNT_NAME");
                String  currencyCode = rs.getString("CURRENCY_CODE");
				String  currency = rs.getString("CURRENCY");
				double  accountBalance  =  rs.getDouble("ACCOUNT_BALANCE");
				double  perctDiscount  =  rs.getDouble("PERCT_DISCOUNT");
                String  industryCode  =  rs.getString("INDUSTRY_CODE");
				String  industry  =  rs.getString("INDUSTRY");
                String  lastTranDate  =  rs.getString("LAST_TRAN_DATE");
             String   witholding  =  rs.getString("WITHOLDING");
				String   witholdingType  =  rs.getString("WITHOLDING_TYPE");
				String   vat  =  rs.getString("VAT");
				String   status   =   rs.getString("STATUS");
                String   createDate   =   formatDate(rs.getDate("CREATE_DATE"));
                String   effectiveDate  =  formatDate(rs.getDate("EFFECTIVE_DATE"));
                int userId   =  rs.getInt("USERID");
           
				
                 customerAccountSetup  = new  com.magbel.ia.vao.CustomerAccountSetup();
				
				  customerAccountSetup.setMtId(mtId);
				   customerAccountSetup.setCustomerNo(customerCode);
				    customerAccountSetup.setCustomerClass(customerClass);
				    customerAccountSetup.setCustomerType(customerType);
				   customerAccountSetup.setAccountType(accountType);
				   customerAccountSetup.setAccountTypeCode(accountTypeCode);
				   customerAccountSetup.setAccountNo(accountNo);
				    customerAccountSetup.setOldAccountNo(oldAccountNo);
				    customerAccountSetup.setAccountName(accountName);
				     customerAccountSetup.setCurrencyCode(currencyCode);
					  customerAccountSetup.setCurrency(currency);
					   customerAccountSetup.setAccountBalance(accountBalance);
					    customerAccountSetup.setPerctDiscount(perctDiscount);
					     customerAccountSetup.setIndustryCode(industryCode);
						  customerAccountSetup.setIndustry(industry);
						   customerAccountSetup.setLastTransDate(lastTranDate);
				 customerAccountSetup.setWitholding(witholding);
				 customerAccountSetup.setWitholdingType(witholdingType);
				 customerAccountSetup.setVat(vat);
				 customerAccountSetup.setStatus(status);
				 customerAccountSetup.setCreateDate(createDate);
				 customerAccountSetup.setEffectiveDate(effectiveDate);
				 customerAccountSetup.setUserId(userId);
				// customerAccount.set
		
	   
								   
				 
                records.add(customerAccountSetup);
		    }
   } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(con, stmt, rs);
        }
        return  records;
}





public  boolean   isUniqueCode(String   CustomerAccountCode)
{

 boolean exists = false;
  boolean  unique = false;
   Connection con = null;
        ResultSet rs = null;
        Statement stmt = null;
		PreparedStatement ps = null;
		
  try {
  
  String SELECT_QUERY  = "SELECT count(CUSTOMER_ACCOUNT_CODE)  FROM   IA_CUSTOMER_ACCOUNT_SETUP "
							+ "  WHERE  CUSTOMER_ACCOUNT_CODE = ?";
  
  
	      con = getConnection();
			ps = con.prepareStatement(SELECT_QUERY);
	
		
		ps.setString(1, CustomerAccountCode);
		
		rs = ps.executeQuery();
		
			while(rs.next()){
					int counted = rs.getInt(1);
				if(counted > 0){
				unique = false;
			 }   else {  unique = true; }
			}
	    
	
		
   } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(con, stmt, rs);
        }
        return     unique;
}

			
}