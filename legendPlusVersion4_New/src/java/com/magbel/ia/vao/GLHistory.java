package com.magbel.ia.vao;

public class GLHistory {

    private String branchCode;
    private String acctType;
    private String acctNo;
    private String tranCode;
    private String sbu;
    private String narration;
    private double amount;
    private String reference;
    private String effDate;
    private int userId;
    private String transDate;
    private String familyCode;
    private String currCode;
    private double acctExchRate;
    private double sysExchRate;
    
    public GLHistory(String branchCode,String acctType,String acctNo,String tranCode,String sbu,String narration,
                     double amount,String reference,String effDate,int userId,String transDate,String familyCode,String currCode,
                     double acctExchRate,double sysExchRate) {
    
    setBranchCode(branchCode);
    setAcctType(acctType);
    setAcctNo(acctNo);
    setTranCode(tranCode);
    setSbu(sbu);
    setNarration(narration);
    setAmount(amount);
    setReference(reference);
    setEffDate(effDate);
    setUserId(userId);
    setTransDate(transDate);
    setFamilyCode(familyCode);
    setCurrCode(currCode);
    setAcctExchRate(acctExchRate);
    setSysExchRate(sysExchRate);
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setAcctType(String acctType) {
        this.acctType = acctType;
    }

    public String getAcctType() {
        return acctType;
    }

    public void setTranCode(String tranCode) {
        this.tranCode = tranCode;
    }

    public String getTranCode() {
        return tranCode;
    }

    public void setSbu(String sbu) {
        this.sbu = sbu;
    }

    public String getSbu() {
        return sbu;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getNarration() {
        return narration;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getReference() {
        return reference;
    }

    public void setEffDate(String effDate) {
        this.effDate = effDate;
    }

    public String getEffDate() {
        return effDate;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setFamilyCode(String familyCode) {
        this.familyCode = familyCode;
    }

    public String getFamilyCode() {
        return familyCode;
    }

    public void setCurrCode(String currCode) {
        this.currCode = currCode;
    }

    public String getCurrCode() {
        return currCode;
    }

    public void setAcctExchRate(double acctExchRate) {
        this.acctExchRate = acctExchRate;
    }

    public double getAcctExchRate() {
        return acctExchRate;
    }

    public void setSysExchRate(double sysExchRate) {
        this.sysExchRate = sysExchRate;
    }

    public double getSysExchRate() {
        return sysExchRate;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public String getAcctNo() {
        return acctNo;
    }
}
