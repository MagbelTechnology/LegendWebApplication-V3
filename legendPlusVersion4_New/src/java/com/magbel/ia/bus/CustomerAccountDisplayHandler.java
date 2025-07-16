package  com.magbel.ia.bus;


import java.sql.*;
import java.text.SimpleDateFormat;
import  com.magbel.ia.vao.CustomerAccountDisplay;
//import com.magbel.ia.vao.MandatoryField;
//import com.magbel.ia.vao.ErrorCode;
import com.magbel.ia.dao.PersistenceServiceDAO;
import com.magbel.ia.util.ApplicationHelper;
import  java.util.ArrayList;



public class CustomerAccountDisplayHandler extends PersistenceServiceDAO //com.magbel.ia.dao.Config{
  {
    SimpleDateFormat sdf;
    final String space = "  ";
    final String comma = ",";
    java.util.Date dat;
   private  ApplicationHelper helper;
			
	
    public   CustomerAccountDisplayHandler()
		{
        dat = new java.util.Date();
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        String date  = dat.toString();
		helper = new ApplicationHelper();
       // System.out.println("USING_ " + this.getClass().getName()+"\n Software written by" 
                             //  	+"  Ogey Bolaji. L @ " +date );
		//System.out.println(" All rights reserved ");
		}
		
	
		
      public java.util.ArrayList   getAllCustomerAccountDisplays() {
        java.util.ArrayList  records = new java.util.ArrayList();
        com.magbel.ia.vao.CustomerAccountDisplay    customerAccountDisplay = null;
		
		 Connection con = null;
    Statement stmt = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
		try {
            
			con = getConnection();
			
        String SELECT_QUERY  = " SELECT * FROM   IA_CUSTOMER_ACCOUNT_DISPLAY  ORDER BY MTID ";
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
				double  witholdingAmount  =   rs.getDouble("WITHOLDING_AMOUNT");
				double   vat  =   rs.getDouble("VAT");
                double  mTD  =   rs.getDouble("MTD");
				double   qTD  =   rs.getDouble("QTD");
               double   yTD   =   rs.getDouble("YTD");
				double   lTD   =   rs.getDouble("LTD");
				String   status   =   rs.getString("STATUS");
                String   createDate   =   formatDate(rs.getDate("CREATE_DATE"));
                String   effectiveDate  =  formatDate(rs.getDate("EFFECTIVE_DATE"));
                int userId   =  rs.getInt("USERID");
				String  accountOfficer =  rs.getString("ACCOUNT_OFFICER");
				int  transactionCount  =  rs.getInt("TRANSACTION_COUNT");
           
				
                 customerAccountDisplay  = new  com.magbel.ia.vao.CustomerAccountDisplay();
				
				  customerAccountDisplay.setMtId(mtId);
				   customerAccountDisplay.setCustomerNo(customerCode);
				   customerAccountDisplay.setCustomerClass(customerClass);
				   customerAccountDisplay.setCustomerType(customerType);
				   customerAccountDisplay.setAccountType(accountType);
				   customerAccountDisplay.setAccountTypeCode(accountTypeCode);
				   customerAccountDisplay.setAccountNo(accountNo);
				    customerAccountDisplay.setOldAccountNo(oldAccountNo);
				    customerAccountDisplay.setAccountName(accountName);
				     customerAccountDisplay.setCurrencyCode(currencyCode);
					  customerAccountDisplay.setCurrency(currency);
					   customerAccountDisplay.setAccountBalance(accountBalance);
					    customerAccountDisplay.setPerctDiscount(perctDiscount);
					     customerAccountDisplay.setIndustryCode(industryCode);
						  customerAccountDisplay.setIndustry(industry);
						   customerAccountDisplay.setLastTransDate(lastTranDate);
				customerAccountDisplay.setWitholdingAmount(witholdingAmount);
				 customerAccountDisplay.setVat(vat);
				 customerAccountDisplay.setMTD(mTD);
				 customerAccountDisplay.setQTD(qTD);
				 customerAccountDisplay.setYTD(yTD);
				 customerAccountDisplay.setLTD(lTD);
				 customerAccountDisplay.setStatus(status);
				 customerAccountDisplay.setCreateDate(createDate);
				 customerAccountDisplay.setEffectiveDate(effectiveDate);
				 customerAccountDisplay.setUserId(userId);
				 customerAccountDisplay.setAccountOfficer(accountOfficer);
				  customerAccountDisplay.setTransactionCount(transactionCount);
				 
                records.add(customerAccountDisplay);
		    }
   } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(con, stmt, rs);
        }
        return  records;
}





  public java.util.ArrayList   getCustomerAccountDisplayByCustomerCode(String  CustomerCode) {
        java.util.ArrayList  records = new java.util.ArrayList();
        com.magbel.ia.vao.CustomerAccountDisplay   customerAccountDisplay = null;
		
		 Connection con = null;
    Statement stmt = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
		try {
            
			con = getConnection();
			 
        String SELECT_QUERY  = " SELECT * FROM   IA_CUSTOMER_ACCOUNT_DISPLAY  WHERE  CUSTOMER_CODE = '"
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
				double  witholdingAmount  =   rs.getDouble("WITHOLDING_AMOUNT");
				double   vat  =   rs.getDouble("VAT");
                double  mTD  =   rs.getDouble("MTD");
				double   qTD  =   rs.getDouble("QTD");
               double   yTD   =   rs.getDouble("YTD");
				double   lTD   =   rs.getDouble("LTD");
				String   status   =   rs.getString("STATUS");
                String   createDate   =   formatDate(rs.getDate("CREATE_DATE"));
                String   effectiveDate  =  formatDate(rs.getDate("EFFECTIVE_DATE"));
                int userId   =  rs.getInt("USERID");
				String  accountOfficer =  rs.getString("ACCOUNT_OFFICER");
				int  transactionCount  =  rs.getInt("TRANSACTION_COUNT");
				
           
				
                 customerAccountDisplay  = new  com.magbel.ia.vao.CustomerAccountDisplay();
				
				  customerAccountDisplay.setMtId(mtId);
				   customerAccountDisplay.setCustomerNo(customerCode);
				   customerAccountDisplay.setCustomerClass(customerClass);
				    customerAccountDisplay.setCustomerType(customerType);
				   customerAccountDisplay.setAccountType(accountType);
				   customerAccountDisplay.setAccountTypeCode(accountTypeCode);
				   customerAccountDisplay.setAccountNo(accountNo);
				    customerAccountDisplay.setOldAccountNo(oldAccountNo);
				    customerAccountDisplay.setAccountName(accountName);
				     customerAccountDisplay.setCurrencyCode(currencyCode);
					  customerAccountDisplay.setCurrency(currency);
					   customerAccountDisplay.setAccountBalance(accountBalance);
					    customerAccountDisplay.setPerctDiscount(perctDiscount);
					     customerAccountDisplay.setIndustryCode(industryCode);
						  customerAccountDisplay.setIndustry(industry);
						   customerAccountDisplay.setLastTransDate(lastTranDate);
						   customerAccountDisplay.setWitholdingAmount(witholdingAmount);
				 customerAccountDisplay.setVat(vat);
				 customerAccountDisplay.setMTD(mTD);
				 customerAccountDisplay.setQTD(qTD);
				 customerAccountDisplay.setYTD(yTD);
				 customerAccountDisplay.setLTD(lTD);
				 customerAccountDisplay.setStatus(status);
				 customerAccountDisplay.setCreateDate(createDate);
				 customerAccountDisplay.setEffectiveDate(effectiveDate);
				 customerAccountDisplay.setUserId(userId);
				 customerAccountDisplay.setAccountOfficer(accountOfficer);
				  customerAccountDisplay.setTransactionCount(transactionCount);					   
				 
                records.add(customerAccountDisplay);
		    }
   } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(con, stmt, rs);
        }
        return  records;
}



public java.util.ArrayList   getCustomerAccountDisplayByStatus(String  Status) {
        java.util.ArrayList  records = new java.util.ArrayList();
        com.magbel.ia.vao.CustomerAccountDisplay    customerAccountDisplay = null;
		
		 Connection con = null;
    Statement stmt = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
		try {
            
			con = getConnection();
				 
        String SELECT_QUERY  = " SELECT * FROM   IA_CUSTOMER_ACCOUNT_DISPLAY  WHERE  STATUS = '"
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
				double  witholdingAmount  =   rs.getDouble("WITHOLDING_AMOUNT");
				double   vat  =   rs.getDouble("VAT");
                double  mTD  =   rs.getDouble("MTD");
				double   qTD  =   rs.getDouble("QTD");
               double   yTD   =   rs.getDouble("YTD");
				double   lTD   =   rs.getDouble("LTD");
				String   status   =   rs.getString("STATUS");
                String   createDate   =   formatDate(rs.getDate("CREATE_DATE"));
                String   effectiveDate  =  formatDate(rs.getDate("EFFECTIVE_DATE"));
                int userId   =  rs.getInt("USERID");
				String  accountOfficer =  rs.getString("ACCOUNT_OFFICER");
				int  transactionCount  =  rs.getInt("TRANSACTION_COUNT");
           
				
                 customerAccountDisplay  = new  com.magbel.ia.vao.CustomerAccountDisplay();
				
				  customerAccountDisplay.setMtId(mtId);
				   customerAccountDisplay.setCustomerNo(customerCode);
				   customerAccountDisplay.setCustomerClass(customerClass);
				    customerAccountDisplay.setCustomerType(customerType);
				   customerAccountDisplay.setAccountType(accountType);
				   customerAccountDisplay.setAccountTypeCode(accountTypeCode);
				   customerAccountDisplay.setAccountNo(accountNo);
				    customerAccountDisplay.setOldAccountNo(oldAccountNo);
				    customerAccountDisplay.setAccountName(accountName);
				     customerAccountDisplay.setCurrencyCode(currencyCode);
					  customerAccountDisplay.setCurrency(currency);
					   customerAccountDisplay.setAccountBalance(accountBalance);
					    customerAccountDisplay.setPerctDiscount(perctDiscount);
					     customerAccountDisplay.setIndustryCode(industryCode);
						  customerAccountDisplay.setIndustry(industry);
						   customerAccountDisplay.setLastTransDate(lastTranDate);
						   customerAccountDisplay.setWitholdingAmount(witholdingAmount);
				 customerAccountDisplay.setVat(vat);
				 customerAccountDisplay.setMTD(mTD);
				 customerAccountDisplay.setQTD(qTD);
				 customerAccountDisplay.setYTD(yTD);
				 customerAccountDisplay.setLTD(lTD);
				 customerAccountDisplay.setStatus(status);
				 customerAccountDisplay.setCreateDate(createDate);
				 customerAccountDisplay.setEffectiveDate(effectiveDate);
				 customerAccountDisplay.setUserId(userId);
				 customerAccountDisplay.setAccountOfficer(accountOfficer);
				  customerAccountDisplay.setTransactionCount(transactionCount);						   
								   
				 
                records.add(customerAccountDisplay);
		    }
   } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(con, stmt, rs);
        }
        return  records;
}







     public   CustomerAccountDisplay   getCustomerAccountDisplayById(String MtId) {
        //java.util.ArrayList  records = new java.util.ArrayList();
        com.magbel.ia.vao.CustomerAccountDisplay      customerAccountDisplay = null;
   Connection con = null;
    Statement stmt = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
	
		try {
           
			con = getConnection();
		 
        String SELECT_QUERY  = " SELECT * FROM  IA_CUSTOMER_ACCOUNT_DISPLAY  WHERE MTID = '"+MtId+"'";
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
				 double  witholdingAmount  =   rs.getDouble("WITHOLDING_AMOUNT");
				double   vat  =   rs.getDouble("VAT");
                double  mTD  =   rs.getDouble("MTD");
				double   qTD  =   rs.getDouble("QTD");
               double   yTD   =   rs.getDouble("YTD");
				double   lTD   =   rs.getDouble("LTD");
				String   status   =   rs.getString("STATUS");
                String   createDate   =   formatDate(rs.getDate("CREATE_DATE"));
                String   effectiveDate  =  formatDate(rs.getDate("EFFECTIVE_DATE"));
                int userId   =  rs.getInt("USERID");
				String  accountOfficer =  rs.getString("ACCOUNT_OFFICER");
				int  transactionCount  =  rs.getInt("TRANSACTION_COUNT");
           
				
                 customerAccountDisplay  = new  com.magbel.ia.vao.CustomerAccountDisplay();
				
				  customerAccountDisplay.setMtId(mtId);
				   customerAccountDisplay.setCustomerNo(customerCode);
				   customerAccountDisplay.setCustomerClass(customerClass);
				    customerAccountDisplay.setCustomerType(customerType);
				   customerAccountDisplay.setAccountType(accountType);
				   customerAccountDisplay.setAccountTypeCode(accountTypeCode);
				   customerAccountDisplay.setAccountNo(accountNo);
				    customerAccountDisplay.setOldAccountNo(oldAccountNo);
				    customerAccountDisplay.setAccountName(accountName);
				     customerAccountDisplay.setCurrencyCode(currencyCode);
					  customerAccountDisplay.setCurrency(currency);
					   customerAccountDisplay.setAccountBalance(accountBalance);
					    customerAccountDisplay.setPerctDiscount(perctDiscount);
					     customerAccountDisplay.setIndustryCode(industryCode);
						  customerAccountDisplay.setIndustry(industry);
						   customerAccountDisplay.setLastTransDate(lastTranDate);
						   customerAccountDisplay.setWitholdingAmount(witholdingAmount);
				 customerAccountDisplay.setVat(vat);
				 customerAccountDisplay.setMTD(mTD);
				 customerAccountDisplay.setQTD(qTD);
				 customerAccountDisplay.setYTD(yTD);
				 customerAccountDisplay.setLTD(lTD);
				 customerAccountDisplay.setStatus(status);
				 customerAccountDisplay.setCreateDate(createDate);
				 customerAccountDisplay.setEffectiveDate(effectiveDate);
				 customerAccountDisplay.setUserId(userId);
				  customerAccountDisplay.setAccountOfficer(accountOfficer);
				  customerAccountDisplay.setTransactionCount(transactionCount);
			}
   } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(con, stmt, rs);
        }
        return     customerAccountDisplay;
}





 public   CustomerAccountDisplay   getCustomerAccountDisplayByAccountNo(String AccountNo) {
        //java.util.ArrayList  records = new java.util.ArrayList();
        com.magbel.ia.vao.CustomerAccountDisplay      customerAccountDisplay = null;
   Connection con = null;
    Statement stmt = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
	
		try {
           
			con = getConnection();
			 
        String SELECT_QUERY  = " SELECT * FROM  IA_CUSTOMER_ACCOUNT_DISPLAY  WHERE ACCOUNT_NO = '"
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
				String  accountOfficer =  rs.getString("ACCOUNT_OFFICER");
				int  transactionCount  =  rs.getInt("TRANSACTION_COUNT");
				double  witholdingAmount  =   rs.getDouble("WITHOLDING_AMOUNT");
				double   vat  =   rs.getDouble("VAT");
                double  mTD  =   rs.getDouble("MTD");
				double   qTD  =   rs.getDouble("QTD");
               double   yTD   =   rs.getDouble("YTD");
				double   lTD   =   rs.getDouble("LTD");
				String   status   =   rs.getString("STATUS");
                String   createDate   =   formatDate(rs.getDate("CREATE_DATE"));
                String   effectiveDate  =  formatDate(rs.getDate("EFFECTIVE_DATE"));
                int userId   =  rs.getInt("USERID");
				
           
				
                 customerAccountDisplay  = new  com.magbel.ia.vao.CustomerAccountDisplay();
				
				  customerAccountDisplay.setMtId(mtId);
				   customerAccountDisplay.setCustomerNo(customerCode);
				   customerAccountDisplay.setCustomerClass(customerClass);
				    customerAccountDisplay.setCustomerType(customerType);
				   customerAccountDisplay.setAccountType(accountType);
				   customerAccountDisplay.setAccountTypeCode(accountTypeCode);
				   customerAccountDisplay.setAccountNo(accountNo);
				    customerAccountDisplay.setOldAccountNo(oldAccountNo);
				    customerAccountDisplay.setAccountName(accountName);
				     customerAccountDisplay.setCurrencyCode(currencyCode);
					  customerAccountDisplay.setCurrency(currency);
					   customerAccountDisplay.setAccountBalance(accountBalance);
					    customerAccountDisplay.setPerctDiscount(perctDiscount);
					     customerAccountDisplay.setIndustryCode(industryCode);
						  customerAccountDisplay.setIndustry(industry);
						   customerAccountDisplay.setLastTransDate(lastTranDate);
						   customerAccountDisplay.setWitholdingAmount(witholdingAmount);
				 customerAccountDisplay.setVat(vat);
				 customerAccountDisplay.setMTD(mTD);
				 customerAccountDisplay.setQTD(qTD);
				 customerAccountDisplay.setYTD(yTD);
				 customerAccountDisplay.setLTD(lTD);
				 customerAccountDisplay.setStatus(status);
				 customerAccountDisplay.setCreateDate(createDate);
				 customerAccountDisplay.setEffectiveDate(effectiveDate);
				 customerAccountDisplay.setUserId(userId);
				  customerAccountDisplay.setAccountOfficer(accountOfficer);
				  customerAccountDisplay.setTransactionCount(transactionCount);
				
				
			}
   } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(con, stmt, rs);
        }
        return     customerAccountDisplay;
}



 public java.util.ArrayList  getCustomerAccountDisplaysByQuery(String query){
        java.util.ArrayList records = new java.util.ArrayList();
        com.magbel.ia.vao.CustomerAccountDisplay    customerAccountDisplay = null;
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
				double  witholdingAmount  =   rs.getDouble("WITHOLDING_AMOUNT");
				double   vat  =   rs.getDouble("VAT");
                double  mTD  =   rs.getDouble("MTD");
				double   qTD  =   rs.getDouble("QTD");
               double   yTD   =   rs.getDouble("YTD");
				double   lTD   =   rs.getDouble("LTD");
				String   status   =   rs.getString("STATUS");
                String   createDate   =   formatDate(rs.getDate("CREATE_DATE"));
                String   effectiveDate  =  formatDate(rs.getDate("EFFECTIVE_DATE"));
                int userId   =  rs.getInt("USERID");
				String  accountOfficer =  rs.getString("ACCOUNT_OFFICER");
				int  transactionCount  =  rs.getInt("TRANSACTION_COUNT");
           
				
                 customerAccountDisplay  = new  com.magbel.ia.vao.CustomerAccountDisplay();
				
				  customerAccountDisplay.setMtId(mtId);
				   customerAccountDisplay.setCustomerNo(customerCode);
				   customerAccountDisplay.setCustomerClass(customerClass);
				    customerAccountDisplay.setCustomerType(customerType);
				   customerAccountDisplay.setAccountType(accountType);
				   customerAccountDisplay.setAccountTypeCode(accountTypeCode);
				   customerAccountDisplay.setAccountNo(accountNo);
				    customerAccountDisplay.setOldAccountNo(oldAccountNo);
				    customerAccountDisplay.setAccountName(accountName);
				     customerAccountDisplay.setCurrencyCode(currencyCode);
					  customerAccountDisplay.setCurrency(currency);
					   customerAccountDisplay.setAccountBalance(accountBalance);
					    customerAccountDisplay.setPerctDiscount(perctDiscount);
					     customerAccountDisplay.setIndustryCode(industryCode);
						  customerAccountDisplay.setIndustry(industry);
						   customerAccountDisplay.setLastTransDate(lastTranDate);
						   customerAccountDisplay.setWitholdingAmount(witholdingAmount);
				 customerAccountDisplay.setVat(vat);
				 customerAccountDisplay.setMTD(mTD);
				 customerAccountDisplay.setQTD(qTD);
				 customerAccountDisplay.setYTD(yTD);
				 customerAccountDisplay.setLTD(lTD);
				 customerAccountDisplay.setStatus(status);
				 customerAccountDisplay.setCreateDate(createDate);
				 customerAccountDisplay.setEffectiveDate(effectiveDate);
				 customerAccountDisplay.setUserId(userId);
				 customerAccountDisplay.setAccountOfficer(accountOfficer);
				  customerAccountDisplay.setTransactionCount(transactionCount);
				  
                records.add(customerAccountDisplay);

				}

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(con, stmt, rs);
        }
       return records;
    }
	
	
	
 public java.util.ArrayList   getCustomerAccountDisplayByQuery(String filter) {
        java.util.ArrayList records = new java.util.ArrayList();
        com.magbel.ia.vao.CustomerAccountDisplay    customerAccountDisplay = null;
		 String SELECT_QUERY  = null;
		if(filter != null)
		   { SELECT_QUERY = "SELECT * FROM  IA_CUSTOMER_ACCOUNT_DISPLAY " + filter;	}
		else { SELECT_QUERY = "SELECT * FROM   IA_CUSTOMER_ACCOUNT_DISPLAY ";  }

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
				 double  witholdingAmount  =   rs.getDouble("WITHOLDING_AMOUNT");
				double   vat  =   rs.getDouble("VAT");
                double  mTD  =   rs.getDouble("MTD");
				double   qTD  =   rs.getDouble("QTD");
               double   yTD   =   rs.getDouble("YTD");
				double   lTD   =   rs.getDouble("LTD");
				String   status   =   rs.getString("STATUS");
                String   createDate   =   formatDate(rs.getDate("CREATE_DATE"));
                String   effectiveDate  =  formatDate(rs.getDate("EFFECTIVE_DATE"));
                int userId   =  rs.getInt("USERID");
				String  accountOfficer =  rs.getString("ACCOUNT_OFFICER");
				int  transactionCount  =  rs.getInt("TRANSACTION_COUNT");
           
				
                 customerAccountDisplay  = new  com.magbel.ia.vao.CustomerAccountDisplay();
				
				  customerAccountDisplay.setMtId(mtId);
				   customerAccountDisplay.setCustomerNo(customerCode);
				   customerAccountDisplay.setCustomerClass(customerClass);
				    customerAccountDisplay.setCustomerType(customerType);
				   customerAccountDisplay.setAccountType(accountType);
				   customerAccountDisplay.setAccountTypeCode(accountTypeCode);
				   customerAccountDisplay.setAccountNo(accountNo);
				    customerAccountDisplay.setOldAccountNo(oldAccountNo);
				    customerAccountDisplay.setAccountName(accountName);
				     customerAccountDisplay.setCurrencyCode(currencyCode);
					  customerAccountDisplay.setCurrency(currency);
					   customerAccountDisplay.setAccountBalance(accountBalance);
					    customerAccountDisplay.setPerctDiscount(perctDiscount);
					     customerAccountDisplay.setIndustryCode(industryCode);
						  customerAccountDisplay.setIndustry(industry);
						   customerAccountDisplay.setLastTransDate(lastTranDate);
						   customerAccountDisplay.setWitholdingAmount(witholdingAmount);
				 customerAccountDisplay.setVat(vat);
				 customerAccountDisplay.setMTD(mTD);
				 customerAccountDisplay.setQTD(qTD);
				 customerAccountDisplay.setYTD(yTD);
				 customerAccountDisplay.setLTD(lTD);
				 customerAccountDisplay.setStatus(status);
				 customerAccountDisplay.setCreateDate(createDate);
				 customerAccountDisplay.setEffectiveDate(effectiveDate);
				 customerAccountDisplay.setUserId(userId);
				 customerAccountDisplay.setAccountOfficer(accountOfficer);
				  customerAccountDisplay.setTransactionCount(transactionCount);
				
                records.add(customerAccountDisplay);

				}

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(con, stmt, rs);
        }
       return records;
    }



	
    public final boolean createCustomerAccountDisplay(CustomerAccountDisplay    customerAccountDisplay)
	{
	   Connection con = null;
        ResultSet rs = null;
        Statement stmt = null;
		PreparedStatement ps = null;
		boolean  successfull  =  false;
		System.out.println("Creating  Customer Account  Display");
	  try{
	          		
		String INSERT_QUERY	 = "INSERT INTO   IA_CUSTOMER_ACCOUNT_DISPLAY (MTID, CUSTOMER_CODE, CUSTOMER_CLASS, "
		                    +" CUSTOMER_TYPE,  ACCOUNT_TYPE,  ACCOUNT_TYPE_CODE,  ACCOUNT_NO, "
							+" OLD_ACCOUNT_NO, ACCOUNT_NAME, CURRENCY_CODE,  CURRENCY, ACCOUNT_BALANCE, "
					   +" PERCT_DISCOUNT, INDUSTRY_CODE,  INDUSTRY,  LAST_TRAN_DATE, "
					   +"  ACCOUNT_OFFICER,  TRANSACTION_COUNT ,  "
					   +"    WITHOLDING_AMOUNT, VAT, MTD ,  QTD ,  YTD, LTD ,  "
					   +"  STATUS, CREATE_DATE,  EFFECTIVE_DATE, USERID "
                        +"  )  VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
					   
			
		   String mtId = helper.getGeneratedId("IA_CUSTOMER_ACCOUNT_DISPLAY");
		   String   customerCode  =   customerAccountDisplay.getCustomerNo();
		    String   customerClass  =   customerAccountDisplay.getCustomerClass();
		     String   customerType  =   customerAccountDisplay.getCustomerType();
		   String   accountType  =   customerAccountDisplay.getAccountType();
		   String   accountTypeCode  =   customerAccountDisplay.getAccountTypeCode();
		   String  accountNo  =    customerAccountDisplay.getAccountNo();
		   String  oldAccountNo  =   customerAccountDisplay.getOldAccountNo();
                   String   accountName  =   customerAccountDisplay.getAccountName();
                   String  currencyCode  =    customerAccountDisplay.getCurrencyCode();
				   String  currency  =    customerAccountDisplay.getCurrency();
                   double  accountBalance =   customerAccountDisplay.getAccountBalance();
                   double  perctDiscount  =   customerAccountDisplay.getPerctDiscount();
                   String  industryCode  =   customerAccountDisplay.getIndustryCode();
				   String  industry  =   customerAccountDisplay.getIndustry();
                   String  lastTransDate  =   customerAccountDisplay.getLastTransDate();
				   double  witholdingAmount  =   customerAccountDisplay.getWitholdingAmount();
                   double  vat  =   customerAccountDisplay.getVat();
                   double  mTD  =   customerAccountDisplay.getMTD();
                   double  qTD  =   customerAccountDisplay.getQTD();
                   double  yTD  =   customerAccountDisplay.getYTD();
				   double  lTD  =   customerAccountDisplay.getLTD();
                   String  status    =   customerAccountDisplay.getStatus();
                   String  createDate =  formatDate(new java.util.Date());
                   String  effectiveDate  =   customerAccountDisplay.getEffectiveDate();
				    if((effectiveDate == "") || (effectiveDate == null))
				      {   effectiveDate =  createDate;  }
                  int  userId =   customerAccountDisplay.getUserId();
				  String  accountOfficer  =  customerAccountDisplay.getAccountOfficer();
				  int  transactionCount  =  customerAccountDisplay.getTransactionCount();
                    
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
		ps.setDouble(19, witholdingAmount);
         ps.setDouble(20, vat);		
		ps.setDouble(21, mTD);
         ps.setDouble(22, qTD);
         ps.setDouble(23, yTD);
	    ps.setDouble(24, lTD);
         ps.setString(25, status);
         ps.setDate(26, dateConvert(createDate));
         ps.setDate(27, dateConvert(effectiveDate));
	    ps.setInt(28, userId);
				
		
		successfull =  ps.execute();
						
	   } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(con, ps, rs);
        }
       return  successfull;
    }
	
	
	
	public final boolean updateCustomerAccountDisplay(CustomerAccountDisplay     customerAccountDisplay)
	{
	  Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
		boolean successfull =  false;
		System.out.println("Updating  Customer Account");
	  try{
	          	
		String UPDATE_QUERY = " UPDATE   IA_CUSTOMER_ACCOUNT_DISPLAY  SET  CUSTOMER_CODE = ?, "
							+" CUSTOMER_CLASS = ?, CUSTOMER_TYPE = ?,"
							+"    ACCOUNT_TYPE = ?,  ACCOUNT_TYPE_CODE = ?,"
							+" ACCOUNT_NO = ?,   OLD_ACCOUNT_NO = ?,  ACCOUNT_NAME = ?,"
							+"  CURRENCY_CODE = ?, CURRENCY = ?,  ACCOUNT_BALANCE = ?, PERCT_DISCOUNT = ?, "
							+" INDUSTRY_CODE = ?,   INDUSTRY = ?, LAST_TRAN_DATE = ?, "
							+"  ACCOUNT_OFFICER = ?,  TRANSACTION_COUNT = ?,  WITHOLDING_AMOUNT = ?, "
							+"  VAT = ?, MTD = ?,  QTD = ?,  YTD = ?, LTD = ?, "
							+"   STATUS = ?    WHERE   MTID = ? ";
		
                   String  mtId =    customerAccountDisplay.getMtId();
					String   customerCode  =   customerAccountDisplay.getCustomerNo();
					String   customerClass  =   customerAccountDisplay.getCustomerClass();
					 String   customerType  =   customerAccountDisplay.getCustomerType();
					String   accountType  =   customerAccountDisplay.getAccountType();
					String   accountTypeCode  =   customerAccountDisplay.getAccountTypeCode();
					String  accountNo  =    customerAccountDisplay.getAccountNo();
					String  oldAccountNo  =   customerAccountDisplay.getOldAccountNo();
                   String   accountName  =   customerAccountDisplay.getAccountName();
                   String  currencyCode  =    customerAccountDisplay.getCurrencyCode();
				   String  currency  =    customerAccountDisplay.getCurrency();
                   double  accountBalance =   customerAccountDisplay.getAccountBalance();
                   double  perctDiscount  =   customerAccountDisplay.getPerctDiscount();
                   String  industryCode  =   customerAccountDisplay.getIndustryCode();
				   String  industry  =   customerAccountDisplay.getIndustry();
                   String  lastTransDate  =   customerAccountDisplay.getLastTransDate();
				    double  witholdingAmount  =   customerAccountDisplay.getWitholdingAmount();
                   double  vat  =   customerAccountDisplay.getVat();
                   double  mTD  =   customerAccountDisplay.getMTD();
                   double  qTD  =   customerAccountDisplay.getQTD();
                   double  yTD  =   customerAccountDisplay.getYTD();
				     double  lTD  =   customerAccountDisplay.getLTD();
                   String  status    =   customerAccountDisplay.getStatus();
                   String  createDate =    customerAccountDisplay.getCreateDate();
                   String  effectiveDate  =   customerAccountDisplay.getEffectiveDate();
				     if((effectiveDate == "") || (effectiveDate == null))
				      {   effectiveDate =  createDate;  }
				     int  userId =   customerAccountDisplay.getUserId();
					  String  accountOfficer  =  customerAccountDisplay.getAccountOfficer();
				  int  transactionCount  =  customerAccountDisplay.getTransactionCount();
		
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
		ps.setDouble(18, witholdingAmount);
         ps.setDouble(19, vat);
		  ps.setDouble(20, mTD);
                 ps.setDouble(21, qTD);
                ps.setDouble(22, yTD);
				ps.setDouble(23, lTD);
                ps.setString(24, status);
         		
                ps.setString(25, mtId);
				
		
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
        
         
		 String  SELECT_QUERY = "SELECT count(MTID)  FROM   IA_CUSTOMER_ACCOUNT_DISPLAY  WHERE MTID = '"+MtId+"'";	
		        
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
        
         
		 String  SELECT_QUERY = "SELECT  ACCOUNT_NO  FROM   IA_CUSTOMER_ACCOUNT  WHERE ACCOUNT_NO = '"
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






public java.util.ArrayList   getCustomerAccountDisplayByCustomerClass(String  CustomerClass) {
        java.util.ArrayList  records = new java.util.ArrayList();
        com.magbel.ia.vao.CustomerAccountDisplay    customerAccountDisplay = null;
		
		 Connection con = null;
    Statement stmt = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
		try {
            
			con = getConnection();
				 
        String SELECT_QUERY  = " SELECT * FROM   IA_CUSTOMER_ACCOUNT  WHERE  CUSTOMER_CLASS = '"
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
				double  witholdingAmount  =   rs.getDouble("WITHOLDING_AMOUNT");
				double   vat  =   rs.getDouble("VAT");
                double  mTD  =   rs.getDouble("MTD");
				double   qTD  =   rs.getDouble("QTD");
               double   yTD   =   rs.getDouble("YTD");
				double   lTD   =   rs.getDouble("LTD");
				String   status   =   rs.getString("STATUS");
                String   createDate   =   formatDate(rs.getDate("CREATE_DATE"));
                String   effectiveDate  =  formatDate(rs.getDate("EFFECTIVE_DATE"));
                int userId   =  rs.getInt("USERID");
           
				
                 customerAccountDisplay  = new  com.magbel.ia.vao.CustomerAccountDisplay();
				
				  customerAccountDisplay.setMtId(mtId);
				   customerAccountDisplay.setCustomerNo(customerCode);
				    customerAccountDisplay.setCustomerClass(customerClass);
				    customerAccountDisplay.setCustomerType(customerType);
				   customerAccountDisplay.setAccountType(accountType);
				   customerAccountDisplay.setAccountTypeCode(accountTypeCode);
				   customerAccountDisplay.setAccountNo(accountNo);
				    customerAccountDisplay.setOldAccountNo(oldAccountNo);
				    customerAccountDisplay.setAccountName(accountName);
				     customerAccountDisplay.setCurrencyCode(currencyCode);
					  customerAccountDisplay.setCurrency(currency);
					   customerAccountDisplay.setAccountBalance(accountBalance);
					    customerAccountDisplay.setPerctDiscount(perctDiscount);
					     customerAccountDisplay.setIndustryCode(industryCode);
						  customerAccountDisplay.setIndustry(industry);
						   customerAccountDisplay.setLastTransDate(lastTranDate);
						   customerAccountDisplay.setWitholdingAmount(witholdingAmount);
				 customerAccountDisplay.setVat(vat);
				 customerAccountDisplay.setMTD(mTD);
				 customerAccountDisplay.setQTD(qTD);
				 customerAccountDisplay.setYTD(yTD);
				 customerAccountDisplay.setLTD(lTD);
				 customerAccountDisplay.setStatus(status);
				 customerAccountDisplay.setCreateDate(createDate);
				 customerAccountDisplay.setEffectiveDate(effectiveDate);
				 customerAccountDisplay.setUserId(userId);
				// customerAccount.set
		
	   
								   
				 
                records.add(customerAccountDisplay);
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
  
  String SELECT_QUERY  = "SELECT count(CUSTOMER_ACCOUNT_CODE)  FROM   IA_CUSTOMER_ACCOUNT "
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