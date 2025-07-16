package com.magbel.ia.vao;

public class SalesPayment {

    private String mtId;
    private String pymtCode;
    private String orderCode;
    private String customerCode;
    private String chequeNo;
    private String bankCode;
    private String bankerCode;
    //private double amount;
    private String transDate;
    private double amountOwing;
    private double amountPaid;
    private String accountNo;
    private String glAccount;
    private int userId;
    private String pymtType;
    private String depositor;
    private String tellerNo;
    private String projectCode;
    private String familyId;
    private String payerName;
    private String menuPage;
    public SalesPayment() {
    
    }
    public SalesPayment(String mtId,String pymtCode,String orderCode,String chequeNo,
                        String bankCode,String transDate,double amountOwing,double amountPaid,
                        int userId,String pymtType,String accountNo,String customerCode,String bankerCode,
                        String depositor,String tellerNo,String projectCode,String familyId,String payerName,String menuPage) {
     
     setMtId(mtId);
     setPymtCode(pymtCode);
     setOrderCode(orderCode);
     setChequeNo(chequeNo);
     setBankCode(bankCode);
     setTransDate(transDate);
     setAmountOwing(amountOwing);
     setAmountPaid(amountPaid);
     setUserId(userId);
     setPymtType(pymtType);
     setAccountNo(accountNo);
     setCustomerCode(customerCode);
     setBankerCode(bankerCode);
     setDepositor(depositor);
     setTellerNo(tellerNo);
     setProjectCode(projectCode);
     setFamilyId(familyId);
     setPayerName(payerName);
     setMenuPage(menuPage);
    }

    public void setMtId(String mtId) {
        this.mtId = mtId;
    }

    public String getMtId() {
        return mtId;
    }

    public void setPymtCode(String pymtCode) {
        this.pymtCode = pymtCode;
    }

    public String getPymtCode() {
        return pymtCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerCode() {
        return customerCode;
    }

   
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setAmountOwing(double amountOwing) {
        this.amountOwing = amountOwing;
    }

    public double getAmountOwing() {
        return amountOwing;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setGlAccount(String glAccount) {
        this.glAccount = glAccount;
    }

    public String getGlAccount() {
        return glAccount;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setPymtType(String pymtType) {
        this.pymtType = pymtType;
    }

    public String getPymtType() {
        return pymtType;
    }

    public void setChequeNo(String chequeNo) {
        this.chequeNo = chequeNo;
    }

    public String getChequeNo() {
        return chequeNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setBankerCode(String bankerCode) {
        this.bankerCode = bankerCode;
    }

    public String getBankerCode() {
        return bankerCode;
    }

    public void setDepositor(String depositor) {
        this.depositor = depositor;
    }

    public String getDepositor() {
        return depositor;
    }

    public void setTellerNo(String tellerNo) {
        this.tellerNo = tellerNo;
    }

    public String getTellerNo() {
        return tellerNo;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public String getFamilyId() {
        return familyId;
    } 

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getPayerName() {
        return payerName;
    }     

    public void setMenuPage(String menuPage) {
        this.menuPage = menuPage;
    }

    public String getMenuPage() {
        return menuPage;
    }         
}
