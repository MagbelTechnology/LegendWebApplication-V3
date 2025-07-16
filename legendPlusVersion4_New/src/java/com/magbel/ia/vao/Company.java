package com.magbel.ia.vao;

/**
 * <p>
 * Title: Company.java
 * </p>
 * 
 * <p>
 * Description: Company
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: Magbel Technologies LTD
 * </p>
 * 
 * @author Bolanle M.Sule
 * @version 1.0
 */

public class Company {

	private String companyId = "";

	private String companyCode = "";

	private String companyName = "";

	private String acronym = "";

	private String companyAddress = "";

	private String financialStartDate = ""; // smalldatetime

	private int financialNoOfMonths = 0;

	private String financialEndDate = ""; // smalldatetime

	private int minimumPassword = 4;

	private int passwordExpiry = 1;

	private int sessionTimeout = 1;

	private String enforceAcqBudget = "N";

	private String requireQuarterlyPm = "N";
	
	private String quarterlySurplusCf = "N";
	
	private String enforceFuelAllocation = "N";

	private String enforcePmBudget = "N";
	
	private String userId;

	private String status;

	private double transWaitTime = 0;
	
	private String logUserAudit;
	
	private double vatRate = 0.0;
	
	private double whtRate = 0.0;
	
	private String collectcashacct = "";
	
	private String collectchqacct = "";
	
	private String collecttrsacct = "";
	
	private String suspenseacct = "";
	
	private String salaryControl = "";
	
	public Company() {
	}

	public Company(String companyId,String companyCode, String companyName, String acronym,
			       String companyAddress,String financialStartDate,
			       int financialNoOfMonths,String financialEndDate,
			       int minimumPassword, int passwordExpiry,int sessionTimeout,
			       String enforceAcqBudget,String quarterlySurplusCf,
			       String userId, String status, double transWaitTime,String logUserAudit) {
			 
			
		this.companyId = companyId;
		this.companyCode = companyCode;
		this.companyName = companyName;
		this.acronym = acronym;
		this.companyAddress = companyAddress;
		this.financialStartDate = financialStartDate;
		this.financialNoOfMonths = financialNoOfMonths;
		this.financialEndDate = financialEndDate;
		this.minimumPassword = minimumPassword;
		this.passwordExpiry = passwordExpiry;
		this.sessionTimeout = sessionTimeout;
		this.enforceAcqBudget = enforceAcqBudget;
		this.quarterlySurplusCf = quarterlySurplusCf;
		this.userId = userId;
		this.status = status;
		this.transWaitTime = transWaitTime;
		this.logUserAudit = logUserAudit;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getEnforceAcqBudget() {
		return enforceAcqBudget;
	}

	public void setEnforceAcqBudget(String enforceAcqBudget) {
		this.enforceAcqBudget = enforceAcqBudget;
	}

	public String getFinancialEndDate() {
		return financialEndDate;
	}

	public void setFinancialEndDate(String financialEndDate) {
		this.financialEndDate = financialEndDate;
	}

	public int getFinancialNoOfMonths() {
		return financialNoOfMonths;
	}

	public void setFinancialNoOfMonths(int financialNoOfMonths) {
		this.financialNoOfMonths = financialNoOfMonths;
	}

	public String getFinancialStartDate() {
		return financialStartDate;
	}

	public void setFinancialStartDate(String financialStartDate) {
		this.financialStartDate = financialStartDate;
	}

	public String getLogUserAudit() {
		return logUserAudit;
	}

	public void setLogUserAudit(String logUserAudit) {
		this.logUserAudit = logUserAudit;
	}

	public int getMinimumPassword() {
		return minimumPassword;
	}

	public void setMinimumPassword(int minimumPassword) {
		this.minimumPassword = minimumPassword;
	}

	public int getPasswordExpiry() {
		return passwordExpiry;
	}

	public void setPasswordExpiry(int passwordExpiry) {
		this.passwordExpiry = passwordExpiry;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getQuarterlySurplusCf() {
		return quarterlySurplusCf;
	}

	public void setQuarterlySurplusCf(String quarterlySurplusCf) {
		this.quarterlySurplusCf = quarterlySurplusCf;
	}

	public int getSessionTimeout() {
		return sessionTimeout;
	}

	public void setSessionTimeout(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	public double getTransWaitTime() {
		return transWaitTime;
	}

	public void setTransWaitTime(double transWaitTime) {
		this.transWaitTime = transWaitTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRequireQuarterlyPm() {
		return requireQuarterlyPm;
	}

	public void setRequireQuarterlyPm(String requireQuarterlyPm) {
		this.requireQuarterlyPm = requireQuarterlyPm;
	}
	
	public double getVatRate() {
		return vatRate;
	}

	public void setVatRate(double vatRate) {
		this.vatRate = vatRate;
	}
	
	public double getWhtRate() {
		return whtRate;
	}

	public void setWhtRate(double whtRate) {
		this.whtRate = whtRate;
	}
	
	public String getEnforceFuelAllocation() {
		return enforceFuelAllocation;
	}

	public void setEnforceFuelAllocation(String enforceFuelAllocation) {
		this.enforceFuelAllocation = enforceFuelAllocation;
	}
	
	public String getEnforcePmBudget() {
		return enforcePmBudget;
	}

	public void setEnforcePmBudget(String enforcePmBudget) {
		this.enforcePmBudget = enforcePmBudget;
	}
	public String getCollectcashacct() {
		return collectcashacct;
	}

	public void setCollectcashacct(String collectcashacct) {
		this.collectcashacct = collectcashacct;
	}
	public String getCollectchqacct() {
		return collectchqacct;
	}

	public void setCollectchqacct(String collectchqacct) {
		this.collectchqacct = collectchqacct;
	}
	public String getCollecttrsacct() {
		return collecttrsacct;
	}

	public void setCollecttrsacct(String collecttrsacct) {
		this.collecttrsacct = collecttrsacct;
	}
		
	public String getSuspenseacct() {
		return suspenseacct;
	}

	public void setSuspenseacct(String suspenseacct) {
		this.suspenseacct = suspenseacct;
	}	
	
	public String getSalaryControl() {
		return salaryControl;
	}
	
	public void setSalaryControl(String salaryControl) {
		this.salaryControl = salaryControl;
	}		
	
}
