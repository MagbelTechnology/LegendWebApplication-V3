package com.magbel.ia.vao;

public class APInvoiceAccount {
private String id;
private String invoiceCode;
private String glAccount;
private double glAmount;
    public APInvoiceAccount(String id,String invoiceCode,String glAccount,double glAmount) {
    setId(id);
    setInvoiceCode(invoiceCode);
    setGlAccount(glAccount);
    setGlAmount(glAmount);
    
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setGlAccount(String glAccount) {
        this.glAccount = glAccount;
    }

    public String getGlAccount() {
        return glAccount;
    }

    public void setGlAmount(double glAmount) {
        this.glAmount = glAmount;
    }

    public double getGlAmount() {
        return glAmount;
    }
}
