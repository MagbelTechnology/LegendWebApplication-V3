package com.magbel.ia.vao;

import java.io.Serializable;

public class CustomerAccount implements Serializable 
{
	 
	private static final long serialVersionUID = 1L;
	private  String  mtId;
	  private  String  customerNo;
	  private  String  accountNo;
	  private  String  customerType;
	  private  String  accountType;
	   private  String  accountTypeCode;
	  private  String  accountClass;
	  private  String  oldAccountNo;
	  private  String  accountName;
	  private  String  currency;
	  private  String  currencyCode;
	  private  double  accountBalance = 0.00d;
	  private  double  perctDiscount = 0.00d;
	  private  String  industry;
	   private  String  industryCode;
	  private  String  lastTransDate;
	   private  double  mTD  = 0.00d;
	   private  double  qTD  = 0.00d;
	  private  double  yTD  = 0.00d;
	    private  double  lYTD  = 0.00d;
	    private  String  status;
		private   String  createDate;
		private   String  effectiveDate;
	  private  int  userId = 0;
	  private  java.util.ArrayList  accountNos;
	  
	  
	  public  CustomerAccount(){}
	  
	
public final double getAccountBalance() {
	return accountBalance;
}

public final void setAccountBalance(double  AccountBalance) {
	this.accountBalance =  AccountBalance;
}

public final String getAccountName() {
	return accountName;
}

public final void setAccountName(String  AccountName) {
	this.accountName =   AccountName;
}

public final String getAccountNo() {
	return accountNo;
}

public final void setAccountNo(String   AccountNo) {
	this.accountNo =   AccountNo;
}

public final String getCurrency() {
	return currency;
}

public final void setCurrency(String   Currency) {
	this.currency =   Currency;
}

public final String getAccountType() {
	return  accountType;
}

public final void setAccountType(String   AccountType) {
	this.accountType =   AccountType;
}


public final String getAccountTypeCode() {
	return accountTypeCode;
}

public final void setAccountTypeCode(String   AccountTypeCode) {
	this.accountTypeCode =   AccountTypeCode;
}


public final String getCustomerNo() {
	return customerNo;
}

public final void setCustomerNo(String   CustomerNo) {
	this.customerNo =   CustomerNo;
}

public final String getIndustry() {
	return industry;
}

public final void setIndustry(String   Industry) {
	this.industry =   Industry;
}



public final String getIndustryCode() {
	return industryCode;
}

public final void setIndustryCode(String   IndustryCode) {
	this.industryCode =   IndustryCode;
}


public final String getCurrencyCode() {
	return currencyCode;
}

public final void setCurrencyCode(String   CurrencyCode) {
	this.currencyCode =   CurrencyCode;
}


public final String getLastTransDate() {
	return lastTransDate;
}

public final void setLastTransDate(String   LastTransDate) {
	this.lastTransDate =   LastTransDate;
}



public final String getMtId() {
	return mtId;
}

public final void setMtId(String   MtId) {
	this.mtId =   MtId;
}

public final String getOldAccountNo() {
	return oldAccountNo;
}

public final void setOldAccountNo(String  OldAccountNo) {
	this.oldAccountNo =   OldAccountNo;
}

public final double getPerctDiscount() {
	return perctDiscount;
}

public final void setPerctDiscount(double   PerctDiscount) {
	this.perctDiscount =   PerctDiscount;
}


public final double getLTD() {
	return lYTD;
}

public final void setLTD(double   LYTD) {
	this.lYTD =   LYTD;
}

public final double getMTD() {
	return mTD;
}

public final void setMTD(double   MTD) {
	this.mTD =   MTD;
}

public final double getQTD() {
	return qTD;
}

public final void setQTD(double   QTD) {
	this.qTD =    QTD;
}

public final double getYTD() {
	return yTD;
}

public final void setYTD(double   YTD) {
	this.yTD =   YTD;
}

public final String getStatus() {
	return status;
}

public final void setStatus(String   Status) {
	this.status =   Status;
}



public final String getCreateDate() {
	return createDate;
}


public final void setCreateDate(String createDate) {
	this.createDate = createDate;
}


public final String getEffectiveDate() {
	return effectiveDate;
}


public final void setEffectiveDate(String EffectiveDate) {
	this.effectiveDate = EffectiveDate;
}


public final int getUserId() {
	return userId;
}


public final void setUserId(int   UserId) {
	this.userId =   UserId;
}

public final String getAccountClass() {
	return this.accountClass;
}

public final void setAccountClass(String AccountClass) {
	this.accountClass = AccountClass;
}	  
	  


public final String getCustomerType() {
	return customerType;
}

public final void setCustomerType(String   CustomerType) {
	this.customerType =   CustomerType;
}




}
