package com.magbel.ia.bus;

import com.magbel.ia.dao.PersistenceServiceDAO;
import com.magbel.ia.util.ApplicationHelper;
import com.magbel.ia.util.CodeGenerator;
import com.magbel.ia.vao.SalesInvoice;
import com.magbel.ia.vao.SalesInvoiceItem;
import com.magbel.util.DatetimeFormat;
import java.io.PrintStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class InvoiceServiceBusOld extends PersistenceServiceDAO
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

    public InvoiceServiceBusOld()
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
        CREATE_QUERY = "INSERT INTO IA_SALES_INVOICE(CUSTOMER_NAME,ORDER_NO,ITEM_DESCRIPTION,AMOUNT,QUAN" +
"TITY,UNIT_PRICE,INVOICE_NO,ACCOUNT_NO,TRANSACTION_DATE,COMP_CODE)VALUES(?,?,?,?," +
"?,?,?,?,?,?)"
;
        con = null;
        ps = null;
        done = false;
        String id = helper.getGeneratedId("IA_SALES_INVOICE");
        invoiceNo = isAutoGen.trim().equalsIgnoreCase("Y") ? cg.generateCode2("INVOICE", "", "", "") : invoiceNo;
        auotoGenCode = invoiceNo;
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
        ps.setString(10, companyCode);
        done = ps.executeUpdate() != -1;
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_282;
        Exception er;
        er;
        System.out.println((new StringBuilder("Error creating INVOICE... ->")).append(er.getMessage()).toString());
        er.printStackTrace();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_282;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
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
        con = getConnection();
        ps = con.prepareStatement(query);
        SalesInvoice impacc;
        for(ResultSet rs = ps.executeQuery(); rs.next(); iaList.add(impacc))
        {
            String mtId = rs.getString("MTID");
            String customerName = rs.getString("CUSTOMER_NAME");
            String orderNo = rs.getString("ORDER_NO");
            String itemDescription = rs.getString("ITEM_DESCRIPTION");
            double amount = rs.getDouble("AMOUNT");
            double quantity = rs.getDouble("QUANTITY");
            double unitPrice = rs.getDouble("UNIT_PRICE");
            String invoiceNo = rs.getString("INVOICE_NO");
            String accountNo = rs.getString("ACCOUNT_NO");
            String transactionDate = "";
            if(rs.getString("TRANSACTION_DATE") != null)
            {
                transactionDate = sdf.format(rs.getDate("TRANSACTION_DATE"));
            }
            impacc = new SalesInvoice(mtId, customerName, orderNo, itemDescription, amount, quantity, unitPrice, invoiceNo, accountNo, transactionDate);
        }

        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_315;
        Exception ex;
        ex;
        System.out.println((new StringBuilder("ERROR fetching Invoice ")).append(ex.getMessage()).toString());
        ex.printStackTrace();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_315;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
        return iaList;
    }

    public ArrayList findInvoiceByQuery(String filter)
    {
        ArrayList iaList = new ArrayList();
        String criteria = (new StringBuilder(" WHERE MTID IS NOT NULL ")).append(filter).toString();
        iaList = findInvoice(criteria);
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
        imp = (SalesInvoice)findInvoice(criteria).get(0);
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
        break MISSING_BLOCK_LABEL_222;
        Exception er;
        er;
        System.out.println((new StringBuilder("Error updating Invoice... ->")).append(er.getMessage()).toString());
        er.printStackTrace();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_222;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
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
        con = getConnection();
        ps = con.prepareStatement(query);
        for(ResultSet rs = ps.executeQuery(); rs.next();)
        {
            result = rs.getString(1) != null ? rs.getString(1) : "";
        }

        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_153;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error in ").append(er.getClass().getName()).append(" - getCodeName()... ->").append(er).toString());
        er.printStackTrace();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_153;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
        return result;
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
        break MISSING_BLOCK_LABEL_186;
        Exception er;
        er;
        System.out.println((new StringBuilder("Error creating INVOICE ITEM... ->")).append(er.getMessage()).toString());
        er.printStackTrace();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_186;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
        return done;
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
        con = getConnection();
        ps = con.prepareStatement(query);
        SalesInvoiceItem impacc;
        for(ResultSet rs = ps.executeQuery(); rs.next(); iaList.add(impacc))
        {
            String mtId = rs.getString("mtid");
            String itemDescription = rs.getString("DESCRIPTION");
            double amount = rs.getDouble("AMOUNT");
            double quantity = rs.getDouble("QUANTITY");
            double unitPrice = rs.getDouble("UNITPRICE");
            String invoiceNo = rs.getString("invoiceNo");
            impacc = new SalesInvoiceItem(mtId, itemDescription, amount, quantity, unitPrice, invoiceNo);
        }

        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_240;
        Exception ex;
        ex;
        System.out.println((new StringBuilder("ERROR fetching Invoice Item ")).append(ex.getMessage()).toString());
        ex.printStackTrace();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_240;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
        return iaList;
    }

    public ArrayList findInvoiceItemByQuery(String filter)
    {
        ArrayList iaList = new ArrayList();
        String criteria = (new StringBuilder(" WHERE MTID IS NOT NULL ")).append(filter).toString();
        iaList = findInvoiceItem(criteria);
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
        con = getConnection();
        ps = con.prepareStatement(CREATE_QUERY);
        ps.setString(1, itemDescription);
        ps.setDouble(2, amount);
        ps.setDouble(3, quantity);
        ps.setDouble(4, unitPrice);
        ps.setString(5, mtId);
        done = ps.executeUpdate() != -1;
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_163;
        Exception er;
        er;
        System.out.println((new StringBuilder("Error updating Invoice Item... ->")).append(er.getMessage()).toString());
        er.printStackTrace();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_163;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
        return done;
    }
}