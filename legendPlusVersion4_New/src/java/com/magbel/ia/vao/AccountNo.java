package com.magbel.ia.vao;

public class AccountNo {

    private String mtId;
    private String accountNo;
    private String bankCode;
    private String accountType;
    private String status;
    private String accountName;
    private String transDate;
    private int userId;
    
    public AccountNo(String mtId,String accountNo,String bankCode,String accountType,
                     String status,String accountName,String transDate,int userId) {
    
    setMtId(mtId);
    setAccountNo(accountNo);
    setBankCode(bankCode);
    setAccountType(accountType);
    setStatus(status);
    setAccountName(accountName);
    setTransDate(transDate);
    setUserId(userId);
    }

    public void setMtId(String mtId) {
        this.mtId = mtId;
    }

    public String getMtId() {
        return mtId;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }
}
