package com.magbel.ia.vao;


public class SalesInvoice
{

    private String mtId;
    private String customerName;
    private String orderNo;
    private String itemDescription;
    private double amount;
    private int quantity;
    private double unitPrice;
    private String invoiceNo;
    private String accountNo;
    private String transactionDate;
    private String bank;
    private String chequeNo;
    private String expendhead;
    private String branchCode;
    private int userid;
    private String term;
    private String ledgerNo;
    private String beneficiaryNo;
    public SalesInvoice(String mtId, String customerName, String orderNo, String itemDescription, double amount, int quantity, double unitPrice, String invoiceNo, String accountNo, String transactionDate,String bank, String chequeNo,String expendhead, String branchCode,int userid, String term)
    {
        this.mtId = mtId;
        this.customerName = customerName;
        this.orderNo = orderNo;
        this.itemDescription = itemDescription;
        this.amount = amount;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.invoiceNo = invoiceNo;
        this.accountNo = accountNo;
        this.transactionDate = transactionDate;
        this.bank = bank;
        this.chequeNo = chequeNo;
        this.expendhead = expendhead;
        this.branchCode = branchCode;
        this.userid = userid;
        this.term = term;
    }
    public SalesInvoice(String mtId, String customerName, String orderNo, String itemDescription, double amount, int quantity, double unitPrice, String invoiceNo, String accountNo, String transactionDate,String bank, String chequeNo,String expendhead, String branchCode,int userid, String term, 
    		String ledgerNo, String beneficiaryNo)
    {
        this.mtId = mtId;
        this.customerName = customerName;
        this.orderNo = orderNo;
        this.itemDescription = itemDescription;
        this.amount = amount;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.invoiceNo = invoiceNo;
        this.accountNo = accountNo;
        this.transactionDate = transactionDate;
        this.bank = bank;
        this.chequeNo = chequeNo;
        this.expendhead = expendhead;
        this.branchCode = branchCode;
        this.userid = userid;
        this.term = term;
        this.ledgerNo = ledgerNo;
        this.beneficiaryNo = beneficiaryNo;
    }
    public SalesInvoice(String mtId, String customerName, String orderNo, String itemDescription, double amount, int quantity, double unitPrice, String invoiceNo, String accountNo, String transactionDate)
    {
        this.mtId = mtId;
        this.customerName = customerName;
        this.orderNo = orderNo;
        this.itemDescription = itemDescription;
        this.amount = amount;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.invoiceNo = invoiceNo;
        this.accountNo = accountNo;
        this.transactionDate = transactionDate;
    }

    public SalesInvoice()
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

    public String getCustomerName()
    {
        return customerName;
    }

    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }

    public String getOrderNo()
    {
        return orderNo;
    }

    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
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

    public String getTransactionDate()
    {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate)
    {
        this.transactionDate = transactionDate;
    }
    
    public String getBank()
    {
        return bank;
    }

    public void setBank(String bank)
    {
        this.bank = bank;
    } 
    public String getChequeNo()
    {
        return chequeNo;
    }

    public void setChequeNo(String chequeNo)
    {
        this.chequeNo = chequeNo;
    }  
    public String getExpendhead()
    {
        return expendhead;
    }

    public void setExpendhead(String expendhead)
    {
        this.expendhead = expendhead;
    } 
    public String getBranchCode()
    {
        return branchCode;
    }

    public void setBranchCode(String branchCode)
    {
        this.branchCode = branchCode;
    } 
    public String getTerm()
    {
        return term;
    }

    public void setTerm(String term)
    {
        this.term = term;
    }       
    public int getUserid()
    {
        return userid;
    }

    public void setUserid(int userid)
    {
        this.userid = userid;
    }       
    public String getLedgerNo()
    {
        return ledgerNo;
    }

    public void setLedgerNo(String ledgerNo)
    {
        this.ledgerNo = ledgerNo;
    }       
    public String getBeneficiaryNo()
    {
        return beneficiaryNo;
    }

    public void setBeneficiaryNo(String beneficiaryNo)
    {
        this.beneficiaryNo = beneficiaryNo;
    } 

}