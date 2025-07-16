package com.magbel.ia.vao;


public class SalesInvoiceItem
{

    private String mtId;
    private String itemDescription;
    private double amount;
    private int quantity;
    private double unitPrice;
    private String invoiceNo; 
    private String accountNo; 
    private String branchCode; 
    private String companyCode; 
    private String term; 
    public SalesInvoiceItem(String mtId, String itemDescription, double amount, int quantity, double unitPrice, String invoiceNo, String term)
    {
        this.mtId = mtId;
        this.itemDescription = itemDescription;
        this.amount = amount;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.invoiceNo = invoiceNo;
        this.term = term;
    }
    public SalesInvoiceItem(String mtId, String itemDescription, double amount, int quantity, double unitPrice, String invoiceNo,String accountNo, String branchCode, String companyCode,String term)
    {
        this.mtId = mtId;
        this.itemDescription = itemDescription;
        this.amount = amount;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.invoiceNo = invoiceNo;
        this.accountNo = accountNo;
        this.branchCode = branchCode;
        this.companyCode = companyCode;
        this.term = term;
    }

    public SalesInvoiceItem(String mtId, String itemDescription, double amount, int quantity, double unitPrice, String invoiceNo)
    {
        this.mtId = mtId;
        this.itemDescription = itemDescription;
        this.amount = amount;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.invoiceNo = invoiceNo;
        this.accountNo = accountNo;
        this.branchCode = branchCode;
        this.companyCode = companyCode;
        this.term = term;
    }
    
    public SalesInvoiceItem()
    {
    }

    public String getMtId()
    {
        return mtId;
    }

    public void setMtId(String mtId)
    {
        this.mtId = mtId;
    }

    public String getItemDescription()
    {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription)
    {
        this.itemDescription = itemDescription;
    }

    public double getAmount()
    {
        return amount;
    }

    public void setAmount(double amount)
    {
        this.amount = amount;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public double getUnitPrice()
    {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice)
    {
        this.unitPrice = unitPrice;
    }
    public String getInvoiceNo()
    {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo)
    {
        this.invoiceNo = invoiceNo;
    }   
    
    public String getAccountNo()
    {
        return accountNo;
    }

    public void setAccountNo(String accountNo)
    {
        this.accountNo = accountNo;
    }
    
    public String getBranchCode()
    {
        return branchCode;
    }

    public void setBranchCode(String branchCode)
    {
        this.branchCode = branchCode;
    }

    public String getCompanyCode()
    {
        return companyCode;
    }

    public void setCompanyCode(String companyCode)
    {
        this.companyCode = companyCode;
    }
    
    public String getTerm()
    {
        return term;
    }

    public void setTerm(String term)
    {
        this.term = term;
    }    
    
}