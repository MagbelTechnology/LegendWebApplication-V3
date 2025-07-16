package com.magbel.ia.vao;

import java.io.Serializable;

public   class     PurchasesCustomerAccount  implements Serializable 
{
	 
	private static final long serialVersionUID = 1L;
	private  String  mtId;
	  private  String  customerNo;
	  private  String  accountNo;
	  private  String  customerAccountType = "Purchases";
	  private  String  customerAccountClass;
	  private  String  oldAccountNo;
	  private  String  accountName;
	  private  String  currency;
	  private  String  currencyCode;
	  private  double  accountBalance = 0.00d;
	  private  double  perctDiscount = 0.00d;
	  private  String  industry;
	  private  String  industryCode;
	  private  String  lastPurchTransDate;
	   private  double  purchasesMTD;
	    private  double  purchasesQTD;
	    private  double  purchasesYTD;
	    private  double  purchasesLTD;
	  private  String  status;
		private   String  createDate;
		private   String  effectiveDate;
	  private  int  userId = 0;
	  private  java.util.ArrayList  accountNos;
	  
	  
	  public  PurchasesCustomerAccount(){}


  public final double getAccountBalance() {
		return accountBalance;
	}
	
	
	public final void setAccountBalance(double accountBalance) {
		this.accountBalance = accountBalance;
	}
	
	
	public final String getAccountName() {
		return accountName;
	}
	
	
	public final void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
	
	public final String getAccountNo() {
		return accountNo;
	}
	
	
	public final void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	
	
	
	public final java.util.ArrayList getAccountNos() {
		return accountNos;
	}
	
	
	public final void setAccountNos(java.util.ArrayList accountNos) {
		this.accountNos = accountNos;
	}
	
	
	public final String getCreateDate() {
		return createDate;
	}
	
	
	public final void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	
	
	public final String getCurrency() {
		return currency;
	}
	
	
	public final void setCurrency(String currency) {
		this.currency = currency;
	}
	
	
	public final String getCurrencyCode() {
		return currencyCode;
	}
	
	
	public final void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	
	
	public final String getCustomerAccountClass() {
		return customerAccountClass;
	}
	
	
	public final void setCustomerAccountClass(String customerAccountClass) {
		this.customerAccountClass = customerAccountClass;
	}
	
	
	public final String getCustomerAccountType() {
		return customerAccountType;
	}
	
	
	public final void setCustomerAccountType(String customerAccountType) {
		this.customerAccountType = customerAccountType;
	}
	
	
	public final String getCustomerNo() {
		return customerNo;
	}
	
	
	public final void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	
	
	public final String getEffectiveDate() {
		return effectiveDate;
	}
	
	public final void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	
	public final String getIndustry() {
		return industry;
	}
	
	public final void setIndustry(String industry) {
		this.industry = industry;
	}
	
	
	public final String getIndustryCode() {
		return industryCode;
	}
	
	
	public final void setIndustryCode(String industryCode) {
		this.industryCode = industryCode;
	}
	
	
	public final String getLastPurchTransDate() {
		return lastPurchTransDate;
	}
	
	
	public final void setLastPurchTransDate(String lastPurchTransDate) {
		this.lastPurchTransDate = lastPurchTransDate;
	}
	
	
	public final String getMtId() {
		return mtId;
	}
	
	
	public final void setMtId(String mtId) {
		this.mtId = mtId;
	}
	
	
	public final String getOldAccountNo() {
		return oldAccountNo;
	}
	
	
	public final void setOldAccountNo(String oldAccountNo) {
		this.oldAccountNo = oldAccountNo;
	}
	
	
	public final double getPerctDiscount() {
		return perctDiscount;
	}
	
	
	public final void setPerctDiscount(double perctDiscount) {
		this.perctDiscount = perctDiscount;
	}
	
	
	public final double getPurchasesLTD() {
		return purchasesLTD;
	}
	
	
	public final void setPurchasesLTD(double purchasesLTD) {
		this.purchasesLTD = purchasesLTD;
	}
	
	
	public final double getPurchasesMTD() {
		return purchasesMTD;
	}
	
	
	public final void setPurchasesMTD(double purchasesMTD) {
		this.purchasesMTD = purchasesMTD;
	}
	
	
	public final double getPurchasesQTD() {
		return purchasesQTD;
	}
	
	
	public final void setPurchasesQTD(double purchasesQTD) {
		this.purchasesQTD = purchasesQTD;
	}
	
	
	public final double getPurchasesYTD() {
		return purchasesYTD;
	}
	
	
	
	public final void setPurchasesYTD(double purchasesYTD) {
		this.purchasesYTD = purchasesYTD;
	}
	
	public final String getStatus() {
		return status;
	}
	
	public final void setStatus(String status) {
		this.status = status;
	}
	
	public final int getUserId() {
		return userId;
	}
	
	public final void setUserId(int userId) {
		this.userId = userId;
	}





}