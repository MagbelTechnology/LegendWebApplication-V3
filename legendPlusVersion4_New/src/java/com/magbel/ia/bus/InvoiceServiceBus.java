package com.magbel.ia.bus;

import com.magbel.ia.dao.PersistenceServiceDAO;
import com.magbel.ia.util.ApplicationHelper;
import com.magbel.ia.util.CodeGenerator;
import com.magbel.ia.vao.PettyInvoice;
import com.magbel.ia.vao.PettyInvoiceItem;
import com.magbel.ia.vao.SalesInvoice;
import com.magbel.ia.vao.SalesInvoiceItem;
import com.magbel.util.DatetimeFormat;

import java.io.PrintStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class InvoiceServiceBus extends PersistenceServiceDAO
{

    SimpleDateFormat sdf;
    final String space = "  ";
    final String comma = ",";
    Date date;
    DatetimeFormat df;
    ApplicationHelper helper;
    public String id;
    public String auotoGenCode;
    CodeGenerator cg;

    public InvoiceServiceBus()
    {
        auotoGenCode = "";
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        df = new DatetimeFormat();
        helper = new ApplicationHelper();
        cg = new CodeGenerator();
    }

    public boolean createInvoice(String customerName, String orderNo, String itemDescription, double amount, double quantity, 
            double unitPrice, String invoiceNo, String accountNo, String transactionDate, String companyCode)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        String isAutoGen = getCodeName("SELECT auto_generate_ID FROM MG_GB_COMPANY");
        CREATE_QUERY = "INSERT INTO IA_SALES_INVOICE(MTID,CUSTOMER_NAME,ORDER_NO,ITEM_DESCRIPTION,AMOUNT,QUAN" +
"TITY,UNIT_PRICE,INVOICE_NO,ACCOUNT_NO,TRANSACTION_DATE,COMP_CODE)VALUES(?,?,?,?," +
"?,?,?,?,?,?,?)"
;
        con = null;
        ps = null;
        done = false;
        String id = helper.getGeneratedId("IA_SALES_INVOICE");
        invoiceNo = isAutoGen.trim().equalsIgnoreCase("Y") ? cg.generateCode2("INVOICE", "", "", "") : invoiceNo;
        auotoGenCode = invoiceNo;
        try {
        con = getConnection();
        ps = con.prepareStatement(CREATE_QUERY);
        ps.setString(1, id);
        ps.setString(2, customerName);
        ps.setString(3, orderNo);
        ps.setString(4, itemDescription);
        ps.setDouble(5, amount);
        ps.setDouble(6, quantity);
        ps.setDouble(7, unitPrice);
        ps.setString(8, invoiceNo);
        ps.setString(9, accountNo);
        ps.setDate(10, dateConvert(transactionDate));
        ps.setString(11, companyCode);
        done = ps.executeUpdate() != -1;
        closeConnection(con, ps);
    	} catch (Exception ex) {
    		System.out.println("Error creating INVOICE... ->" + ex.getMessage());
	
    	}
    	finally {
        closeConnection(con, ps);
    	}
        return done;
    }
    private ArrayList findInvoice(String filter)
    {
        ArrayList iaList;
        Connection con;
        PreparedStatement ps;
        String query;
        iaList = new ArrayList();
        SalesInvoice impacc = null;
        con = null;
        ps = null;
        ResultSet rs = null;
        query = "SELECT MTID,CUSTOMER_NAME,ORDER_NO,ITEM_DESCRIPTION,AMOUNT,QUANTITY,UNIT_PRICE,I" +
"NVOICE_NO,ACCOUNT_NO,TRANSACTION_DATE FROM IA_SALES_INVOICE "
;
        query = (new StringBuilder(String.valueOf(query))).append(filter).toString();
        try{        
        con = getConnection();
        ps = con.prepareStatement(query);
        rs = ps.executeQuery();
//        System.out.println("findInvoice query:"+query);
       	 while(rs.next()){
            String mtId = rs.getString("MTID");
            String customerName = rs.getString("CUSTOMER_NAME");
            String orderNo = rs.getString("ORDER_NO");
            String itemDescription = rs.getString("ITEM_DESCRIPTION");
            double amount = rs.getDouble("AMOUNT");
            int quantity = rs.getInt("QUANTITY");
            double unitPrice = rs.getDouble("UNIT_PRICE");
            String invoiceNo = rs.getString("INVOICE_NO");
            String accountNo = rs.getString("ACCOUNT_NO");
            String transactionDate = "";
            if(rs.getString("TRANSACTION_DATE") != null)
            {
                transactionDate = sdf.format(rs.getDate("TRANSACTION_DATE"));
            }
            System.out.println("findInvoice query: "+query);
            impacc = new SalesInvoice(mtId, customerName, orderNo, itemDescription, amount, quantity, unitPrice, invoiceNo, accountNo, transactionDate);
            iaList.add(impacc);
       	 }
    }catch(Exception ex){
        System.out.println("ERROR fetching Invoice"+ex.getMessage());
}finally{
        closeConnection(con,ps);
}

    return iaList ;
}        

        
    private ArrayList findPettyInvoice(String filter)
    {
        ArrayList iaList;
        Connection con;
        PreparedStatement ps;
        String query;
        iaList = new ArrayList();
        PettyInvoice impacc = null;
        con = null;
        ps = null;
        ResultSet rs = null;
        System.out.println("findInvoice filter : "+filter);
        query = "SELECT MTID,CUSTOMER_NAME,BENEFICIARYN_NO,ORDER_NO,ITEM_DESCRIPTION,TOTAL_AMOUNT,TOTAL_QUANTITY,UNIT_PRICE,I" +
"NVOICE_NO,ACCOUNT_NO,TRANSACTION_DATE,BANK,CHEQUE_NO,EXPEND_HEAD,BRANCH_CODE,USERID,TERM,LEDGER_ACCT FROM IA_PV_SUMMARY ";
        query = (new StringBuilder(String.valueOf(query))).append(filter).toString();       
 //       System.out.println(" findInvoice query:  "+query);
        try{
        con = getConnection();
        ps = con.prepareStatement(query);
        rs = ps.executeQuery();
  //      PettyInvoice impacc;
        while(rs.next())
        {
     //   for( rs = ps.executeQuery(); rs.next(); iaList.add(impacc))
     //   {
            String mtId = rs.getString("MTID");
            String customerName = rs.getString("CUSTOMER_NAME");
            String customerCode = rs.getString("BENEFICIARYN_NO");
            String orderNo = rs.getString("ORDER_NO");
            String itemDescription = rs.getString("ITEM_DESCRIPTION");
            double amount = rs.getDouble("TOTAL_AMOUNT");
            int quantity = rs.getInt("TOTAL_QUANTITY");
            double unitPrice = rs.getDouble("UNIT_PRICE");
            String invoiceNo = rs.getString("INVOICE_NO");
            String accountNo = rs.getString("ACCOUNT_NO");
            String bank = rs.getString("BANK");
            String chequeNo = rs.getString("CHEQUE_NO");
            String expendhead = rs.getString("EXPEND_HEAD");
            String branchcode = rs.getString("BRANCH_CODE");
            int userid = rs.getInt("USERID");
            String term = rs.getString("TERM");
            String ledgerNo = rs.getString("LEDGER_ACCT");
            String transactionDate = "";  
            
            if(rs.getString("TRANSACTION_DATE") != null)
            {
                transactionDate = sdf.format(rs.getDate("TRANSACTION_DATE"));
            }
            impacc = new PettyInvoice(mtId, customerName, orderNo, itemDescription, amount, quantity, unitPrice, invoiceNo, accountNo, transactionDate,bank,chequeNo,expendhead,branchcode,userid,term);
            impacc.setLedgerNo(ledgerNo);
            impacc.setCustomerCode(customerCode);
            iaList.add(impacc);
        }
        }catch(Exception er){
		        System.out.println("ERROR fetching Invoice"+er.getMessage());
		}finally{
		        closeConnection(con,ps);
		}
		
		    return iaList ;
		}        

    public ArrayList findInvoiceByQuery(String filter)
    {
        ArrayList iaList = new ArrayList();
        String criteria = (new StringBuilder(" WHERE MTID IS NOT NULL ")).append(filter).toString();
        iaList = findInvoice(criteria);
        return iaList;
    }
    
    public ArrayList findPettyInvoiceByQuery(String filter)
    {
        ArrayList iaList = new ArrayList();
        String criteria = (new StringBuilder(" WHERE MTID IS NOT NULL ")).append(filter).toString();
        iaList = findPettyInvoice(criteria);
        return iaList;
    }
    public SalesInvoice findImprestByRefNo(String invoiceNo)
    {
        SalesInvoice imp = new SalesInvoice();
        String criteria = (new StringBuilder(" WHERE ORDER_NO = '")).append(invoiceNo).append("'").toString();
        imp = (SalesInvoice)findInvoice(criteria).get(0);
        return imp;
    }

    public SalesInvoice findInvoiceByID(String id)
    {
        SalesInvoice imp = new SalesInvoice();
        String criteria = (new StringBuilder(" WHERE MTID = '")).append(id).append("'").toString();
        System.out.println("====criteria in findInvoiceByID: "+criteria);
        imp = (SalesInvoice)findInvoice(criteria).get(0);
        return imp;
    }

    public SalesInvoice findVoucherInvoiceByID(String id)
    {
        SalesInvoice imp = new SalesInvoice();
        String criteria = "";
//        System.out.println("findInvoiceByID ID: "+id);
        
        if(id != null || !id.equals("")){
      //  	System.out.println("findInvoiceByID ID 2: "+id);
         criteria = (new StringBuilder(" WHERE INVOICE_NO = '")).append(id).append("'").toString();
        }
  //      System.out.println(" findInvoiceByID criteria:  "+criteria);
        imp = (SalesInvoice)findVoucherInvoice(criteria).get(0);
        return imp;
    }
    
    public PettyInvoice findImprestPettyByRefNo(String invoiceNo)
    {
        PettyInvoice imp = new PettyInvoice();
        String criteria = (new StringBuilder(" WHERE ORDER_NO = '")).append(invoiceNo).append("'").toString();
        imp = (PettyInvoice)findPettyInvoice(criteria).get(0);
        return imp;
    }

    public PettyInvoice findInvoicePettyByID(String id)
    {
        PettyInvoice imp = new PettyInvoice();
        String criteria = "";        
        if(id != null || !id.equals("")){
         criteria = (new StringBuilder(" WHERE INVOICE_NO = '")).append(id).append("'").toString();
        }
        imp = (PettyInvoice)findPettyInvoice(criteria).get(0);
        return imp;
    }
    public PettyInvoice findInvoiceByIDforSummary(String id)
    {
        PettyInvoice imp = new PettyInvoice();
        String criteria = "";
   //     System.out.println("findInvoiceByID ID: "+id);
        
        if(id != null || !id.equals("")){
    //    	System.out.println("findInvoiceByID ID 2: "+id);
         criteria = (new StringBuilder(" WHERE MTID = '")).append(id).append("'").toString();
        }
   //     System.out.println(" findInvoiceByID criteria:  "+criteria);
        imp = (PettyInvoice)findPettyInvoice(criteria).get(0);
        return imp;
    }
    public PettyInvoice findPettyByIDforSummary(String id)
    {
        PettyInvoice imp = new PettyInvoice();
        String criteria = "";
     //   System.out.println("findInvoiceByID ID: "+id);
        id = id.trim();
        if(id != null || !id.equals("")){
       // 	System.out.println("findInvoiceByID ID 2: "+id);
         criteria = (new StringBuilder(" WHERE MTID = '")).append(id).append("'").toString();
        }
      //  System.out.println(" findInvoiceByID criteria:  "+criteria);
        imp = (PettyInvoice)findPettycash(criteria).get(0);
        return imp;
    }
    
    public boolean updateInvoice(String mtId, String customerName, String orderNo, String itemDescription, double amount, double quantity, double unitPrice, String invoiceNo, String accountNo, String transactionDate)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        CREATE_QUERY = "UPDATE IA_SALES_INVOICE SET CUSTOMER_NAME=?, ORDER_NO=?, ITEM_DESCRIPTION=?, AMO" +
"UNT=?,QUANTITY=?,UNIT_PRICE=?,INVOICE_NO=?,ACCOUNT_NO = ?,TRANSACTION_DATE = ? W" +
"HERE MTID=? "
;
        con = null;
        ps = null;
        done = false;
        try{
        con = getConnection();
        ps = con.prepareStatement(CREATE_QUERY);
        ps.setString(1, customerName);
        ps.setString(2, orderNo);
        ps.setString(3, itemDescription);
        ps.setDouble(4, amount);
        ps.setDouble(5, quantity);
        ps.setDouble(6, unitPrice);
        ps.setString(7, invoiceNo);
        ps.setString(8, accountNo);
        ps.setDate(9, dateConvert(transactionDate));
        ps.setString(10, mtId);
        done = ps.executeUpdate() != -1;
        closeConnection(con, ps);
		} catch (Exception ex) {
			System.out.println("Error updating Invoice... ->" + ex.getMessage());
	
		}
		finally {
	    closeConnection(con, ps);
		}
	    return done;
	}        
 
    public String getCodeName(String query)
    {
        String result;
        Connection con;
        PreparedStatement ps;
        result = "";
        con = null;
        ResultSet rs = null;
        ps = null;
        try{
        con = getConnection();
        ps = con.prepareStatement(query);
        for(rs = ps.executeQuery(); rs.next();)
        {
            result = rs.getString(1) != null ? rs.getString(1) : "";
        }
		} catch (Exception ex) {
			System.out.println("Error in" + ex.getMessage());
	
		}
		finally {
	    closeConnection(con, ps);
		}
	    return result;
	}        

    public boolean createInvoiceVoucherItem(String itemDescription, double amount, double quantity, String projectCode, double unitPrice, 
            String invoiceNo,String AccounNo,String branchCode,String companyCode,String userid,String chequeNo, String term,String customerName,String orderNo)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        String id;
        CREATE_QUERY = "INSERT INTO IA_PV_DETAIL(mtid,ITEM_DESCRIPTION,amount,quantity,PROJECT_CODE,UNIT_PRICE,inv" +
"oice_No,ACCOUNT_NO,BRANCH_CODE,COMP_CODE,USERID,CHEQUE_NO, CUSTOMER_NAME,ORDER_NO,TERM)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        con = null;
        ps = null;  
        done = false;
        id = helper.getGeneratedId("IA_PV_DETAIL");
        try{
        con = getConnection();
        ps = con.prepareStatement(CREATE_QUERY);
        ps.setString(1, id);
        ps.setString(2, itemDescription);
        ps.setDouble(3, amount);
        ps.setDouble(4, quantity);
        ps.setString(5, projectCode);
        ps.setDouble(6, unitPrice);
        ps.setString(7, invoiceNo);
        ps.setString(8, AccounNo);
        ps.setString(9, branchCode);
        ps.setString(10, companyCode);
        ps.setInt(11, Integer.parseInt(userid));
        ps.setString(12, chequeNo);
        ps.setString(13, customerName);
        ps.setString(14, orderNo);
        ps.setString(15, term);
        done = ps.executeUpdate() != -1;
        closeConnection(con, ps);
		} catch (Exception ex) {
			System.out.println("Error creating PAYMENT ITEM in createInvoiceItem... ->" + ex.getMessage());
	
		}
		finally {
	    closeConnection(con, ps);
		}
	    return done;
	}           

    private ArrayList findPettyInvoiceItem(String filter)
    {
        ArrayList iaList;
        Connection con;
        PreparedStatement ps;
        String query;
        iaList = new ArrayList();
     //   PettyInvoiceItem impacc = null;
        con = null;
        ps = null;
        ResultSet rs = null; 
        query = "SELECT mtid,ITEM_DESCRIPTION,amount,quantity,UNIT_PRICE,INVOICE_NO,ACCOUNT_NO,BRANCH_CODE,COMP_CODE,TERM FROM IA_PV_DETAIL ";
        query = (new StringBuilder(String.valueOf(query))).append(filter).toString();
//        System.out.print("findInvoiceItem query: "+query);
        try{
        con = getConnection();
        ps = con.prepareStatement(query);
        PettyInvoiceItem impacc;
        for(rs = ps.executeQuery(); rs.next(); iaList.add(impacc))
        { 
            String mtId = rs.getString("mtid");
            String itemDescription = rs.getString("ITEM_DESCRIPTION");
            double amount = rs.getDouble("AMOUNT");
            int quantity = rs.getInt("QUANTITY");
            double unitPrice = rs.getDouble("UNIT_PRICE");
            String invoiceNo = rs.getString("INVOICE_NO"); 
            String accountNo = rs.getString("ACCOUNT_NO"); 
            String branchCode = rs.getString("BRANCH_CODE");
            String companyCode = rs.getString("COMP_CODE"); 
            String term = rs.getString("TERM"); 
            impacc = new PettyInvoiceItem(mtId, itemDescription, amount, quantity, unitPrice, invoiceNo,accountNo,branchCode,companyCode,term);
        }

        closeConnection(con, ps);
		} catch (Exception ex) {
			System.out.println("ERROR fetching Payment Item in findInvoiceItem " + ex.getMessage());
	
		}
		finally {
	    closeConnection(con, ps);
		}
		return iaList;
	}        

    public ArrayList findPettyInvoiceItemByQuery(String filter)
    {
        ArrayList iaList = new ArrayList();
        String criteria = (new StringBuilder(" WHERE MTID IS NOT NULL ")).append(filter).toString();
        iaList = findPettyInvoiceItem(criteria);
        return iaList;
    }

    public ArrayList findPettyInvoiceItemByMtId(String filter)
    {
        ArrayList iaList = new ArrayList();
        String criteria = (new StringBuilder(" WHERE MTID = '")).append(filter).append("'").toString();
        iaList = findPettyInvoiceItem(criteria);
        return iaList;
    }

    public PettyInvoiceItem findPettyInvoiceItemByID(String id)
    {
        PettyInvoiceItem imp = new PettyInvoiceItem();
        String criteria = (new StringBuilder(" WHERE MTID = '")).append(id).append("'").toString();
        imp = (PettyInvoiceItem)findPettyInvoiceItem(criteria).get(0);
        return imp;
    }

    public boolean updateInvoiceVoucherItem(String mtId, String itemDescription, double amount, double quantity, String projectCode, double unitPrice,String invoiceNo,String pageName)
    {
    	//System.out.println("pageName: "+pageName);
        String CREATE_QUERY = "";
        Connection con;
        PreparedStatement ps;
        boolean done;
        if(pageName.equalsIgnoreCase("PettyInvoiceItemUpdate")){
     //   	System.out.println("pageName: "+pageName+"  mtId: "+mtId+"  itemDescription: "+itemDescription+"  amount: "+amount+"  quantity: "+quantity+"  unitPrice: "+unitPrice+"  invoiceNo: "+chequeNo);
        CREATE_QUERY = "UPDATE IA_PV_DETAIL SET  ITEM_DESCRIPTION=?, AMOUNT=?,QUANTITY=?,UNIT_PRICE=?,INVOICE_NO=?,PROJECT_CODE=? WHERE MTID=? ";
        }  
        if(pageName.equalsIgnoreCase("SalesSalesSummaryUpdate")){
 //       	System.out.println("pageName: "+pageName+"  mtId: "+mtId+"  itemDescription: "+itemDescription+"  amount: "+amount+"  quantity: "+quantity+"  unitPrice: "+unitPrice+"  invoiceNo: "+chequeNo);
        CREATE_QUERY = "UPDATE IA_PV_SUMMARY SET  ITEM_DESCRIPTION=?, TOTAL_AMOUNT=?,TOTAL_QUANTITY=?,UNIT_PRICE=?,INVOICE_NO=?, PROJECT_CODE=? WHERE MTID=? ";
        }        
        con = null;
        ps = null; 
        done = false;
   //     System.out.println("CREATE_QUERY: "+CREATE_QUERY);
        try {
        con = getConnection();
        ps = con.prepareStatement(CREATE_QUERY);
        ps.setString(1, itemDescription);
        ps.setDouble(2, amount);
        ps.setDouble(3, quantity);
        ps.setDouble(4, unitPrice);
        ps.setString(5, invoiceNo);
        ps.setString(6, projectCode);
        ps.setString(7, mtId);
        done = ps.executeUpdate() != -1;
        closeConnection(con, ps);
	} catch (Exception ex) {
		System.out.println("Error updating Invoice Item... ->" + ex.getMessage());

	}
	finally {
    closeConnection(con, ps);
	}
    return done;
}        
  

    public boolean createPaymentVoucher(String customerName, String orderNo, String itemDescription, double amount, double quantity, String projectCode,
            double unitPrice, String invoiceNo, String accountNo, String transactionDate, String companyCode,String branchCode,String Bank, String chequeNo,String ExpendHead,String userid,String term,String ledgerNo,String customserCode)
    {
        String CREATE_QUERY; 
        Connection con;
        PreparedStatement ps;
        boolean done;
        String isAutoGen = getCodeName("SELECT auto_generate_ID FROM MG_GB_COMPANY");
        CREATE_QUERY = "INSERT INTO IA_PV_SUMMARY(MTID,CUSTOMER_NAME,ORDER_NO,ITEM_DESCRIPTION,TOTAL_AMOUNT,TOTAL_QUANTITY,PROJECT_CODE" +
",UNIT_PRICE,INVOICE_NO,TRANSACTION_DATE,COMP_CODE,BRANCH_CODE,ACCOUNT_NO,BANK,CHEQUE_NO,EXPEND_HEAD,USERID,TERM,LEDGER_ACCT,BENEFICIARYN_NO)VALUES(?,?,?,?," +
"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//        System.out.println("CREATE_QUERY in createPaymentVoucher:  "+CREATE_QUERY);
        con = null;
        ps = null;
        done = false;
        String id = helper.getGeneratedId("IA_PV_SUMMARY");
       // invoiceNo = isAutoGen.trim().equalsIgnoreCase("Y") ? cg.generateCode2("PAYMENT VOUCHER", "", "", "") : invoiceNo;
    //    auotoGenCode = invoiceNo;
//         System.out.println("userid==== "+userid);
//         System.out.println("ExpendHead==== "+ExpendHead); 
        try {
        con = getConnection();
        ps = con.prepareStatement(CREATE_QUERY);
        ps.setString(1, id);
        ps.setString(2, customerName);
        ps.setString(3, orderNo);
        ps.setString(4, itemDescription);
        ps.setDouble(5, amount);
        ps.setDouble(6, quantity);
        ps.setString(7, projectCode);
        ps.setDouble(8, unitPrice);
        ps.setString(9, invoiceNo);
      //  ps.setString(8, accountNo);
        ps.setDate(10, dateConvert(transactionDate));
        ps.setString(11, companyCode);
        ps.setString(12, branchCode);
        ps.setString(13, ledgerNo);
        ps.setString(14, Bank);
        ps.setString(15, chequeNo);
        ps.setString(16, ExpendHead);
        ps.setInt(17, Integer.parseInt(userid));
        ps.setString(18, term);
        ps.setString(19, accountNo);
        ps.setString(20, customserCode);
        done = ps.executeUpdate() != -1;
        closeConnection(con, ps);
    	} catch (Exception ex) {
    		System.out.println("Error creating PVS... ->" + ex.getMessage());
	
    	}
    	finally {
        closeConnection(con, ps);
    	}
        return done;
    }
    public boolean updatePaymentVoucher(String mtId, String customerName, String orderNo, String itemDescription, double amount, double quantity, String projectCode, double unitPrice, String invoiceNo, String accountNo, String transactionDate,String Bank, String chequeNo,String ExpendHead,String ledgerNo)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        CREATE_QUERY = "UPDATE IA_PV_SUMMARY SET CUSTOMER_NAME=?, ORDER_NO=?, LEDGER_ACCT=?,ITEM_DESCRIPTION=?, AMO" +
"UNT=?,QUANTITY=?,UNIT_PRICE=?,INVOICE_NO=?,ACCOUNT_NO = ?,TRANSACTION_DATE = ?,BANK = ?,CHEQUE_NO = ?,EXPEND_HEAD = ?,PROJECT_CODE =? W" +
"HERE MTID=? "
;
        con = null;
        ps = null;
        done = false;
        try{
        con = getConnection();
        ps = con.prepareStatement(CREATE_QUERY);
        ps.setString(1, customerName);
        ps.setString(2, orderNo);
        ps.setString(3, ledgerNo);
        ps.setString(4, itemDescription);
        ps.setDouble(5, amount);
        ps.setDouble(6, quantity);
        ps.setDouble(7, unitPrice);
        ps.setString(8, invoiceNo);
        ps.setString(9, accountNo);
        ps.setDate(10, dateConvert(transactionDate));        
        ps.setString(11, Bank);
        ps.setString(12, chequeNo);        
        ps.setString(13, ExpendHead);
        ps.setString(14, projectCode);
        ps.setString(15, mtId);
        done = ps.executeUpdate() != -1;
        closeConnection(con, ps);
		} catch (Exception ex) {
			System.out.println("Error updating Invoice... ->" + ex.getMessage());
	
		}
		finally {
	    closeConnection(con, ps);
		}
	    return done;
	}        
    public PettyInvoiceItem findPettyPaymentCategoryItemByID(String id)
    {
        PettyInvoiceItem imp = new PettyInvoiceItem();
        String criteria = (new StringBuilder(" WHERE MTID = '")).append(id).append("'").toString();
        imp = (PettyInvoiceItem)findPettyInvoiceItem(criteria).get(0);
        return imp;
    }

    public ArrayList findPettyCashByQuery(String filter)
    {
        ArrayList iaList = new ArrayList();
        String criteria = (new StringBuilder(" WHERE MTID IS NOT NULL ")).append(filter).toString();
        iaList = findPettycash(criteria);
        return iaList;
    }
    private ArrayList findPettycash(String filter)
    {
        ArrayList iaList;
        Connection con;
        PreparedStatement ps;
        String query;
        iaList = new ArrayList();
        PettyInvoice impacc = null;
        con = null;
        ps = null;
        ResultSet rs = null;
        System.out.println("findPettyCash filter : "+filter);
        query = "SELECT MTID,CUSTOMER_NAME,ORDER_NO,ITEM_DESCRIPTION,TOTAL_AMOUNT,TOTAL_QUANTITY,UNIT_PRICE,I" +
"NVOICE_NO,ACCOUNT_NO,TRANSACTION_DATE,BANK,CHEQUE_NO,EXPEND_HEAD,BRANCH_CODE,USERID,TERM,BENEFICIARYN_NO FROM IA_PC_SUMMARY ";
        query = (new StringBuilder(String.valueOf(query))).append(filter).toString();       
//        System.out.println(" findPettycash query:  "+query);
        try{
        con = getConnection();
        ps = con.prepareStatement(query);
        rs = ps.executeQuery();
  //      PettyInvoice impacc;
        while(rs.next())
        {
     //   for( rs = ps.executeQuery(); rs.next(); iaList.add(impacc))
     //   {
            String mtId = rs.getString("MTID");
            String customerName = rs.getString("CUSTOMER_NAME");
            String orderNo = rs.getString("ORDER_NO");
            String itemDescription = rs.getString("ITEM_DESCRIPTION");
            double amount = rs.getDouble("TOTAL_AMOUNT");
            int quantity = rs.getInt("TOTAL_QUANTITY");
            double unitPrice = rs.getDouble("UNIT_PRICE");
            String invoiceNo = rs.getString("INVOICE_NO");
            String accountNo = rs.getString("ACCOUNT_NO");
            String bank = rs.getString("BANK");
            String chequeNo = rs.getString("CHEQUE_NO");
            String expendhead = rs.getString("EXPEND_HEAD");
            String branchcode = rs.getString("BRANCH_CODE");
            int userid = rs.getInt("USERID");
            String term = rs.getString("TERM");
            String customerCode = rs.getString("BENEFICIARYN_NO");
            String transactionDate = "";
            
            if(rs.getString("TRANSACTION_DATE") != null)
            {
                transactionDate = sdf.format(rs.getDate("TRANSACTION_DATE"));
            }
            impacc = new PettyInvoice(mtId, customerName, orderNo, itemDescription, amount, quantity, unitPrice, invoiceNo, accountNo, transactionDate,bank,chequeNo,expendhead,branchcode,userid,term);
            impacc.setCustomerCode(customerCode);
            iaList.add(impacc);
        }
        }catch(Exception er){
		        System.out.println("ERROR fetching Petty Cash"+er.getMessage());
		}finally{
		        closeConnection(con,ps);
		}
		
		    return iaList ;
		}        

    public boolean createPettyCashVoucher(String customerName, String orderNo, String itemDescription, double amount, double quantity, String projectCode,
            double unitPrice, String invoiceNo, String accountNo, String transactionDate, String companyCode,String branchCode,String otherleg, String chequeNo,String ExpendHead,String userid, String term,String customerCode)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        String isAutoGen = getCodeName("SELECT auto_generate_ID FROM MG_GB_COMPANY");
        CREATE_QUERY = "INSERT INTO IA_PC_SUMMARY(MTID,CUSTOMER_NAME,ORDER_NO,ITEM_DESCRIPTION,TOTAL_AMOUNT,TOTAL_QUANTITY" +
",UNIT_PRICE,INVOICE_NO,TRANSACTION_DATE,COMP_CODE,BRANCH_CODE,ACCOUNT_NO,LEDGER_ACCT,CHEQUE_NO,EXPEND_HEAD,USERID,TERM,BENEFICIARYN_NO,PROJECT_CODE)VALUES(?,?,?,?," +
"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//        System.out.println("CREATE_QUERY in createPettyCashVoucher:  "+CREATE_QUERY);
        con = null;
        ps = null;
        done = false;
        String id = helper.getGeneratedId("IA_PC_SUMMARY");
       // invoiceNo = isAutoGen.trim().equalsIgnoreCase("Y") ? cg.generateCode2("PAYMENT VOUCHER", "", "", "") : invoiceNo;
    //    auotoGenCode = invoiceNo;
        try {
        con = getConnection();
        ps = con.prepareStatement(CREATE_QUERY);
        ps.setString(1, id);
        ps.setString(2, customerName);
        ps.setString(3, orderNo);
        ps.setString(4, itemDescription);
        ps.setDouble(5, amount);
        ps.setDouble(6, quantity);
        ps.setDouble(7, unitPrice);
        ps.setString(8, invoiceNo);
      //  ps.setString(8, accountNo);
        ps.setDate(9, dateConvert(transactionDate));
        ps.setString(10, companyCode);
        ps.setString(11, branchCode);
        ps.setString(12, accountNo);
        ps.setString(13, otherleg);
        ps.setString(14, chequeNo);
        ps.setString(15, ExpendHead);
        ps.setInt(16, Integer.parseInt(userid));
        ps.setString(17, term);
        ps.setString(18, customerCode);
        ps.setString(19, projectCode);
        done = ps.executeUpdate() != -1;
        closeConnection(con, ps);
    	} catch (Exception ex) {
    		System.out.println("Error creating PCV... ->" + ex.getMessage());
	
    	}
    	finally {
        closeConnection(con, ps);
    	}
        return done;
    }
    public boolean updatePettyCashVoucher(String mtId, String customerName, String orderNo, String itemDescription, double amount, double quantity, String projectCode, double unitPrice, String invoiceNo, String accountNo, String transactionDate,String Bank, String chequeNo,String ExpendHead)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        CREATE_QUERY = "UPDATE IA_PC_SUMMARY SET ITEM_DESCRIPTION=?, TOTAL_AMOUNT=?,TOTAL_QUANTITY=?,UNIT_PRICE=?,PROJECT_CODE=? WHERE MTID=?" ;
        con = null;
        ps = null;
        done = false;
        try{
        con = getConnection();
        ps = con.prepareStatement(CREATE_QUERY);
        ps.setString(1, itemDescription);
        ps.setDouble(2, amount);
        ps.setDouble(3, quantity);
        ps.setDouble(4, unitPrice);
        ps.setString(5, projectCode);
        ps.setString(6, mtId);
        done = ps.executeUpdate() != -1;
        closeConnection(con, ps);
		} catch (Exception ex) {
			System.out.println("Error updating Petty Cash... ->" + ex.getMessage());
	
		}
		finally {
	    closeConnection(con, ps);
		}
	    return done;
	}        
 
    public boolean createPettyItem(String itemDescription, double amount, double quantity, String projectCode, double unitPrice, 
            String invoiceNo,String AccounNo,String branchCode,String companyCode,String userid,String term,String customerName,String orderNo)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        String id;
        CREATE_QUERY = "INSERT INTO IA_PC_DETAIL(mtid,ITEM_DESCRIPTION,amount,quantity,UNIT_PRICE,inv" +
"oice_No,ACCOUNT_NO,BRANCH_CODE,COMP_CODE,USERID,TERM,CUSTOMER_NAME,ORDER_NO,PROJECT_CODE)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      //  System.out.println("createPettyItem CREATE_QUERY: "+CREATE_QUERY);
        con = null;
        ps = null;  
        done = false;
        id = helper.getGeneratedId("IA_PC_DETAIL");
        try{
        con = getConnection();
        ps = con.prepareStatement(CREATE_QUERY);
        ps.setString(1, id);
        ps.setString(2, itemDescription);
        ps.setDouble(3, amount);
        ps.setDouble(4, quantity);
        ps.setDouble(5, unitPrice);
        ps.setString(6, invoiceNo);
        ps.setString(7, AccounNo);
        ps.setString(8, branchCode);
        ps.setString(9, companyCode);
        ps.setInt(10, Integer.parseInt(userid));
        ps.setString(11, term);
        ps.setString(12, customerName);
        ps.setString(13, orderNo);
        ps.setString(14, projectCode);
        done = ps.executeUpdate() != -1;
      // System.out.println("createPettyItem Inserted: ");
        closeConnection(con, ps);
		} catch (Exception ex) {
			System.out.println("Error creating Petty ITEM... ->" + ex.getMessage());
	
		}
		finally {
	    closeConnection(con, ps);
		}
	    return done;
	}           
    public boolean updatePettyItem(String mtId, String itemDescription, double amount, double quantity, String projectCode, double unitPrice,String accountNo,String term)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        CREATE_QUERY = "UPDATE IA_PC_DETAIL SET  ITEM_DESCRIPTION=?, AMOUNT=?,QUANTITY=?,UNIT_PRICE=?,TERM = ?,PROJECT_CODE = ? WHERE MTID=? ";
        con = null;
        ps = null;
        done = false;
        try {
        con = getConnection();
        ps = con.prepareStatement(CREATE_QUERY);
        ps.setString(1, itemDescription);
        ps.setDouble(2, amount);
        ps.setDouble(3, quantity);
        ps.setDouble(4, unitPrice);
        ps.setString(5, term);
        ps.setString(6, projectCode);
        ps.setString(6, mtId);
        done = ps.executeUpdate() != -1;
        closeConnection(con, ps);
	} catch (Exception ex) {
		System.out.println("Error updating Petty Item... ->" + ex.getMessage());

	}
	finally {
    closeConnection(con, ps);
	}
    return done;
}        
    public PettyInvoice findPettyByID(String id)
    {
        PettyInvoice imp = new PettyInvoice();
        String criteria = "";
//        System.out.println("findInvoiceByID ID: "+id);
 //       if(id == "="){id = "";}
        if(id != null || !id.equals("")){
//        	System.out.println("findPettyByID ID 2: "+id);
         criteria = (new StringBuilder(" WHERE INVOICE_NO = '")).append(id).append("'").toString();
         imp = (PettyInvoice)findPetty(criteria).get(0);
        }
        else{
//        System.out.println(" findPettyByID criteria:  "+criteria);
        imp = (PettyInvoice)findPetty(criteria).get(0);
        }
        return imp;
    }
    private ArrayList findPetty(String filter)
    {
        ArrayList iaList;
        Connection con;
        PreparedStatement ps;
        String query;
        iaList = new ArrayList();
        PettyInvoice impacc = null;
        con = null;
        ps = null;
        ResultSet rs = null;
//        System.out.println("findPetty filter : "+filter);
        query = "SELECT MTID,CUSTOMER_NAME,ORDER_NO,ITEM_DESCRIPTION,TOTAL_AMOUNT,TOTAL_QUANTITY,UNIT_PRICE,I" +
"NVOICE_NO,ACCOUNT_NO,TRANSACTION_DATE,BANK,CHEQUE_NO,EXPEND_HEAD,BRANCH_CODE,USERID,TERM,LEDGER_ACCT FROM IA_PC_SUMMARY ";
        query = (new StringBuilder(String.valueOf(query))).append(filter).toString();       
        System.out.println(" findPetty query:  "+query);
        try{
        con = getConnection();
        ps = con.prepareStatement(query);
        rs = ps.executeQuery();
  //      PettyInvoice impacc;
        while(rs.next())
        {
     //   for( rs = ps.executeQuery(); rs.next(); iaList.add(impacc))
     //   {
            String mtId = rs.getString("MTID");
            String customerName = rs.getString("CUSTOMER_NAME");
            String orderNo = rs.getString("ORDER_NO");
            String itemDescription = rs.getString("ITEM_DESCRIPTION");
            double amount = rs.getDouble("TOTAL_AMOUNT");
            int quantity = rs.getInt("TOTAL_QUANTITY");
            double unitPrice = rs.getDouble("UNIT_PRICE");
            String invoiceNo = rs.getString("INVOICE_NO");
            String accountNo = rs.getString("ACCOUNT_NO");
            String bank = rs.getString("BANK");
            String chequeNo = rs.getString("CHEQUE_NO");
            String expendhead = rs.getString("EXPEND_HEAD");
            String branchcode = rs.getString("BRANCH_CODE");
            int userid = rs.getInt("USERID");
            String term = rs.getString("TERM");
            String ledgerNo = rs.getString("LEDGER_ACCT"); 
            String transactionDate = "";
            
            if(rs.getString("TRANSACTION_DATE") != null)
            {
                transactionDate = sdf.format(rs.getDate("TRANSACTION_DATE"));
            } 
            impacc = new PettyInvoice(mtId, customerName, orderNo, itemDescription, amount, quantity, unitPrice, invoiceNo, accountNo, transactionDate,bank,chequeNo,expendhead,branchcode,userid,term);
            impacc.setLedgerNo(ledgerNo);
            iaList.add(impacc);
        }
        }catch(Exception er){
		        System.out.println("ERROR fetching Petty"+er.getMessage());
		}finally{
		        closeConnection(con,ps);
		}
		
		    return iaList ;
		}        
    public ArrayList findPettyCashItemByQuery(String filter)
    {
        ArrayList iaList = new ArrayList();
        String criteria = (new StringBuilder(" WHERE MTID IS NOT NULL ")).append(filter).toString();
        iaList = findPettyItem(criteria);
        return iaList;
    }

    private ArrayList findPettyItem(String filter)
    {
        ArrayList iaList;
        Connection con;
        PreparedStatement ps;
        String query;
        iaList = new ArrayList();
     //   PettyInvoiceItem impacc = null;
        con = null;
        ps = null;
        ResultSet rs = null; 
        query = "SELECT mtid,ITEM_DESCRIPTION,amount,quantity,UNIT_PRICE,INVOICE_NO,ACCOUNT_NO,BRANCH_CODE,COMP_CODE,TERM FROM IA_PC_DETAIL ";
        query = (new StringBuilder(String.valueOf(query))).append(filter).toString();
//        System.out.print("findPettyItem query: "+query);
        try{
        con = getConnection();
        ps = con.prepareStatement(query);
        PettyInvoiceItem impacc;
        for(rs = ps.executeQuery(); rs.next(); iaList.add(impacc))
        {
            String mtId = rs.getString("mtid");
            String itemDescription = rs.getString("ITEM_DESCRIPTION");
            double amount = rs.getDouble("AMOUNT");
            int quantity = rs.getInt("QUANTITY");
            double unitPrice = rs.getDouble("UNIT_PRICE");
            String invoiceNo = rs.getString("INVOICE_NO"); 
            String accountNo = rs.getString("ACCOUNT_NO"); 
            String branchCode = rs.getString("BRANCH_CODE");
            String companyCode = rs.getString("COMP_CODE"); 
            String term = rs.getString("TERM"); 
            impacc = new PettyInvoiceItem(mtId, itemDescription, amount, quantity, unitPrice, invoiceNo,accountNo,branchCode,companyCode,term);
        }

        closeConnection(con, ps);
		} catch (Exception ex) {
			System.out.println("ERROR fetching Petty Cash Item in findPettyItem " + ex.getMessage());
	
		}
		finally {
	    closeConnection(con, ps);
		}
		return iaList;
	}        

    public boolean updateBudgetForCashVoucher(String companyCode, String schoolCode, String categoryCode, double amount)
    {
        String BUDGET_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        BUDGET_QUERY = "UPDATE ACQUISITION_BUDGET SET BALANCE_ALLOCATION = BALANCE_ALLOCATION - ? WHERE COMP_CODE = ?, BRANCH_ID=? AND CATEGORY = ?" ;
        con = null;
        ps = null;
        done = false;
        try{
        con = getConnection();
        ps = con.prepareStatement(BUDGET_QUERY);
        ps.setDouble(1, amount);
        ps.setString(2, companyCode);
        ps.setString(5, schoolCode);
        ps.setString(5, categoryCode);
        done = ps.executeUpdate() != -1;
        closeConnection(con, ps);
		} catch (Exception ex) {
			System.out.println("Error updating Budget Balance... ->" + ex.getMessage());
	
		}
		finally {
	    closeConnection(con, ps);
		}
	    return done;
	}        
    public boolean updateBudgetAddForCashVoucher(String companyCode, String schoolCode, String categoryCode, double amount,  double oldAmount)
    {
        String BUDGET_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        BUDGET_QUERY = "UPDATE AM_ACQUISITION_BUDGET SET BALANCE_ALLOCATION = BALANCE_ALLOCATION + ? WHERE COMP_CODE = ?, BRANCH_ID=? AND CATEGORY = ?" ;
        con = null;
        ps = null;
        done = false;
        try{
        con = getConnection();
        ps = con.prepareStatement(BUDGET_QUERY);
        ps.setDouble(1, oldAmount);
        ps.setString(2, companyCode);
        ps.setString(5, schoolCode);
        ps.setString(5, categoryCode);
        done = ps.executeUpdate() != -1;
        closeConnection(con, ps);
		} catch (Exception ex) {
			System.out.println("Error updating Budget Balance... ->" + ex.getMessage());
	
		}
		finally {
	    closeConnection(con, ps);
		}
	    return done;
	}       
    
    public boolean createPayableImprst(String refNumber, String beneficiary, String impAccNumber, String benAccNumber, String purpose, String expiryDate, String isRetired, 
            String userId, String supervisorId, String transDate, String effDate, String isCash, double amount, 
            String isposted,String companyCode,int branchNo,String ledgerNo,String orderNo)
    {
	    String isAutoGen = getCodeName("SELECT auto_generate_ID FROM MG_GB_COMPANY");
		
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        String id;
        CREATE_QUERY = "INSERT INTO IA_PAYABLE_IMPRESTS(MTID, REF_NUMBER, BENEFICIARY, IMP_ACC_NUMBER, BEN_ACC_NUMBER, PURPOSE, EXPIRY_DATE, ISRETIRED, USERID, TRANS_DATE, EFF_DATE, ISCASH,AMOUNT,ISPOSTED,COMP_CODE,BRANCH_NO,LEDGER_CHQ_CASH,ORDER_NO)VALUES(?,?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?)";
        con = null;
        ps = null;
        done = false;
        id = helper.getGeneratedId("IA_PAYABLE_IMPRESTS");
		
//		refNumber = isAutoGen.trim().equalsIgnoreCase("Y") ? cg.generateCode2("PAYABLE_IMPRESTS","","","") : refNumber;
 //       auotoGenCode = refNumber;  
        try
        {
            con = getConnection();
            ps = con.prepareStatement(CREATE_QUERY);
            ps.setString(1, id);
            ps.setString(2, refNumber);
            ps.setString(3, beneficiary);
            ps.setString(4, ledgerNo);
            ps.setString(5, benAccNumber);
            ps.setString(6, purpose);
            ps.setDate(7, dateConvert(new java.util.Date()));
            ps.setString(8, isRetired);
            ps.setString(9, userId);
//            ps.setString(10, supervisorId);
            ps.setDate(10, dateConvert(new java.util.Date()));
            ps.setDate(11, dateConvert(new java.util.Date()));
            ps.setString(12, isCash);
            ps.setDouble(13, amount);
            ps.setString(14, isposted);
			ps.setString(15,companyCode);
			ps.setInt(16,branchNo);
			ps.setString(17, impAccNumber);
			ps.setString(18, orderNo);
            done = ps.executeUpdate() != -1;
     //       System.out.println("INSERT TO TABLE IA_PAYABLE_IMPRESTS: "+done);
        }
        catch(Exception er)
        {
            System.out.println((new StringBuilder("Error creating Payable Imprest... ->")).append(er.getMessage()).toString());
            er.printStackTrace();
            
        }
        finally{
                                closeConnection(con,ps);
                        }
        return done;
    }

    public ArrayList findInvoiceItemByQuery(String filter)
    {
        ArrayList iaList = new ArrayList();
        String criteria = (new StringBuilder(" WHERE MTID IS NOT NULL ")).append(filter).toString();
        iaList = findInvoiceItem(criteria);
        return iaList;
    }

    public ArrayList findVoucherItemByQuery(String filter)
    {
        ArrayList iaList = new ArrayList();
        String criteria = (new StringBuilder(" WHERE MTID IS NOT NULL ")).append(filter).toString();
        iaList = findVoucherInvoiceItem(criteria);
        return iaList;
    }


    public ArrayList findInvoiceItemByMtId(String filter)
    {
        ArrayList iaList = new ArrayList();
        String criteria = (new StringBuilder(" WHERE MTID = '")).append(filter).append("'").toString();
        iaList = findInvoiceItem(criteria);
        return iaList;
    }

    public SalesInvoiceItem findInvoiceItemByID(String id)
    {
        SalesInvoiceItem imp = new SalesInvoiceItem();
        String criteria = (new StringBuilder(" WHERE MTID = '")).append(id).append("'").toString();
        imp = (SalesInvoiceItem)findInvoiceItem(criteria).get(0);
        return imp;
    }

    private ArrayList findInvoiceItem(String filter)
    {
        ArrayList iaList;
        Connection con;
        PreparedStatement ps;
        String query;
        iaList = new ArrayList();
        SalesInvoiceItem impacc = null;
        con = null;
        ps = null;
        ResultSet rs = null;
        query = "SELECT mtid,description,amount,quantity,unitprice,invoiceNo FROM IA_SALES_INVOIC" +
"E_ITEM "
;  
        query = (new StringBuilder(String.valueOf(query))).append(filter).toString();
//        System.out.println("<<<<<findInvoiceItem: "+query);
        try { 
        con = getConnection();
        ps = con.prepareStatement(query);
        rs = ps.executeQuery();
//        SalesInvoiceItem impacc;
       	while(rs.next())
        {
            String mtId = rs.getString("mtid");
            String itemDescription = rs.getString("DESCRIPTION");
            double amount = rs.getDouble("AMOUNT");
            int quantity = rs.getInt("QUANTITY");
            double unitPrice = rs.getDouble("UNITPRICE");
            String invoiceNo = rs.getString("invoiceNo");
            impacc = new SalesInvoiceItem(mtId, itemDescription, amount, quantity, unitPrice, invoiceNo);
            iaList.add(impacc);
        }
        }catch(Exception er){
            System.out.println("ERROR fetching Invoice Item "+er.getMessage());
    }finally{
            closeConnection(con,ps);
    }

        return iaList ;
    }        

    private ArrayList findVoucherInvoice(String filter)
    {
        ArrayList iaList;
        Connection con;
        PreparedStatement ps;
        String query;
        iaList = new ArrayList();
        SalesInvoice impacc = null;
        con = null;
        ps = null;
        ResultSet rs = null;
        System.out.println("findInvoice filter : "+filter);
        query = "SELECT MTID,CUSTOMER_NAME,ORDER_NO,ITEM_DESCRIPTION,TOTAL_AMOUNT,TOTAL_QUANTITY,UNIT_PRICE,I" +
"NVOICE_NO,ACCOUNT_NO,TRANSACTION_DATE,BANK,CHEQUE_NO,EXPEND_HEAD,BRANCH_CODE,USERID,TERM,LEDGER_ACCT,BENEFICIARYN_NO FROM IA_PV_SUMMARY ";
        query = (new StringBuilder(String.valueOf(query))).append(filter).toString();       
//        System.out.println(" findVoucherInvoice query:  "+query);
        try{
        con = getConnection();
        ps = con.prepareStatement(query);
        rs = ps.executeQuery();
  //      SalesInvoice impacc;
        while(rs.next())
        {
     //   for( rs = ps.executeQuery(); rs.next(); iaList.add(impacc))
     //   {
            String mtId = rs.getString("MTID");
            String customerName = rs.getString("CUSTOMER_NAME");
            String orderNo = rs.getString("ORDER_NO");
            String itemDescription = rs.getString("ITEM_DESCRIPTION");
            double amount = rs.getDouble("TOTAL_AMOUNT");
            int quantity = rs.getInt("TOTAL_QUANTITY");
            double unitPrice = rs.getDouble("UNIT_PRICE");
            String invoiceNo = rs.getString("INVOICE_NO");
            String accountNo = rs.getString("ACCOUNT_NO");
            String bank = rs.getString("BANK");
            String chequeNo = rs.getString("CHEQUE_NO");
            String expendhead = rs.getString("EXPEND_HEAD");
            String branchcode = rs.getString("BRANCH_CODE");
            int userid = rs.getInt("USERID");
            String term = rs.getString("TERM");
            String ledgeracct = rs.getString("LEDGER_ACCT");
            String beneficiaryNo = rs.getString("BENEFICIARYN_NO");
            String transactionDate = "";  
            
            if(rs.getString("TRANSACTION_DATE") != null)
            {
                transactionDate = sdf.format(rs.getDate("TRANSACTION_DATE"));
            }
            impacc = new SalesInvoice(mtId, customerName, orderNo, itemDescription, amount, quantity, unitPrice, invoiceNo, accountNo, transactionDate,bank,chequeNo,expendhead,branchcode,userid,term,ledgeracct,beneficiaryNo);
            iaList.add(impacc);
        }
        }catch(Exception er){
		        System.out.println("ERROR fetching Invoice"+er.getMessage());
		}finally{
		        closeConnection(con,ps);
		}
		
		    return iaList ;
		}        


    private ArrayList findVoucherInvoiceItem(String filter)
    {
        ArrayList iaList;
        Connection con;
        PreparedStatement ps;
        String query;
        iaList = new ArrayList();
     //   SalesInvoiceItem impacc = null;
        con = null;
        ps = null;
        ResultSet rs = null; 
        query = "SELECT mtid,ITEM_DESCRIPTION,amount,quantity,UNIT_PRICE,INVOICE_NO,ACCOUNT_NO,BRANCH_CODE,COMP_CODE,TERM FROM IA_PV_DETAIL ";
        query = (new StringBuilder(String.valueOf(query))).append(filter).toString();
//        System.out.print("findVoucherInvoiceItem query: "+query);
        try{
        con = getConnection();
        ps = con.prepareStatement(query);
        SalesInvoiceItem impacc;
        for(rs = ps.executeQuery(); rs.next(); iaList.add(impacc))
        { 
            String mtId = rs.getString("mtid");
            String itemDescription = rs.getString("ITEM_DESCRIPTION");
            double amount = rs.getDouble("AMOUNT");
            int quantity = rs.getInt("QUANTITY");
            double unitPrice = rs.getDouble("UNIT_PRICE");
            String invoiceNo = rs.getString("INVOICE_NO"); 
            String accountNo = rs.getString("ACCOUNT_NO"); 
            String branchCode = rs.getString("BRANCH_CODE");
            String companyCode = rs.getString("COMP_CODE"); 
            String term = rs.getString("TERM"); 
            impacc = new SalesInvoiceItem(mtId, itemDescription, amount, quantity, unitPrice, invoiceNo,accountNo,branchCode,companyCode,term);
        }

        closeConnection(con, ps);
		} catch (Exception ex) {
			System.out.println("ERROR fetching Payment Item in findVoucherInvoiceItem " + ex.getMessage());
	
		}
		finally {
	    closeConnection(con, ps);
		}
		return iaList;
	}        


    public boolean createInvoiceItem(String itemDescription, double amount, double quantity, double unitPrice, 
            String invoiceNo)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        String id;
        CREATE_QUERY = "INSERT INTO IA_SALES_INVOICE_ITEM(mtid,description,amount,quantity,unitprice,inv" +
"oiceNo)VALUES(?,?,?,?,?,?)"
;
        con = null;
        ps = null;
        done = false;
        id = helper.getGeneratedId("IA_SALES_INVOICE_ITEM");
        try {
        con = getConnection();
        ps = con.prepareStatement(CREATE_QUERY);
        ps.setString(1, id);
        ps.setString(2, itemDescription);
        ps.setDouble(3, amount);
        ps.setDouble(4, quantity);
        ps.setDouble(5, unitPrice);
        ps.setString(6, invoiceNo);
        done = ps.executeUpdate() != -1;
        closeConnection(con, ps);
	} catch (Exception ex) {
		System.out.println("Error creating INVOICE ITEM... ->" + ex.getMessage());
	}
	finally {
    closeConnection(con, ps);
	}
    return done;
}        

    public boolean updateInvoiceItem(String mtId, String itemDescription, double amount, double quantity, double unitPrice)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        CREATE_QUERY = "UPDATE IA_SALES_INVOICE_ITEM SET  DESCRIPTION=?, AMOUNT=?,QUANTITY=?,UNITPRICE=?" +
" WHERE MTID=? "
;
        con = null;
        ps = null;
        done = false;
        try {
        con = getConnection();
        ps = con.prepareStatement(CREATE_QUERY);
        ps.setString(1, itemDescription);
        ps.setDouble(2, amount);
        ps.setDouble(3, quantity);
        ps.setDouble(4, unitPrice);
        ps.setString(5, mtId);
        done = ps.executeUpdate() != -1;
        closeConnection(con, ps);
	} catch (Exception ex) {
		System.out.println("Error updating Invoice Item... ->" + ex.getMessage());

	}
	finally {
    closeConnection(con, ps);
	}
    return done;
} 
    
}