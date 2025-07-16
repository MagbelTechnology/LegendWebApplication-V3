package com.magbel.ia.vao;

public class PaymentHistory {

  private String currCode;
  private String acctType;
  private String acctNo;
  private String tranCode;
  private String sbu;
  private String narration;
  private double amount;
  private String reference;
  private double acctExchRate;
  private double sysExchRate;
  private String branchCode;
  private String effDate;
  private int userId; 
  private String familyCode;
  private String pymtType;
              
    public PaymentHistory(String currCode,String acctType,String acctNo,String tranCode,String narration,
                          double amount,String reference,double acctExchRate,double sysExchRate,String branchCode,
                          String effDate,int userId,String familyCode,String pymtType) {
    setCurrCode(currCode);
    setAcctType(acctType);
    setAcctNo(acctNo);
    setTranCode(tranCode);
    setNarration(narration);
    setAmount(amount);
    setReference(reference);
    setAcctExchRate(acctExchRate);
    setSysExchRate(sysExchRate);
    setBranchCode(branchCode);
    setEffDate(effDate);
    setUserId(userId);
    setFamilyCode(familyCode);
    setPymtType(pymtType);
    
    }

    public void setCurrCode(String currCode) {
        this.currCode = currCode;
    }

    public String getCurrCode() {
        return currCode;
    }

    public void setAcctType(String acctType) {
        this.acctType = acctType;
    }

    public String getAcctType() {
        return acctType;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public String getAcctNo() {
        return acctNo;
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

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchCode() {
        return branchCode;
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

    public void setFamilyCode(String familyCode) {
        this.familyCode = familyCode;
    }

    public String getFamilyCode() {
        return familyCode;
    }

    public void setPymtType(String pymtType) {
        this.pymtType = pymtType;
    }

    public String getPymtType() {
        return pymtType;
    }
}
