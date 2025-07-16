package com.magbel.ia.vao;

/**
 * <p>Title: TotalingLevel.java</p>
 *
 * <p>Description: TotalingLevel Description</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: Magbel Technologies LTD</p>
 *
 * @author Bolanle M. Sule
 * @version 1.0
 */


public class TotalingLevel {

		private String id;
		private String accountType;
		private String ledgerNo;
		private String position;
	    private String description;
		private String effectiveDate;
		private String totalingLevel;
		private String totalingLedgerNo;
		private String ledgerType;
		private String debitCredit;
	    private String status;
	    
	    
		public TotalingLevel(String id, String accountType, String ledgerNo, String position, 
		                     String description, String effectiveDate, String totalingLevel,
		                     String totalingLedgerNo, String ledgerType, String debitCredit, 
		                     String status) {
							 
			super();
			this.id = id;
			this.accountType = accountType;
			this.ledgerNo = ledgerNo;
			this.position = position;
			this.description = description;
			this.effectiveDate = effectiveDate;
			this.totalingLevel = totalingLevel;
			this.totalingLedgerNo = totalingLedgerNo;
			this.ledgerType = ledgerType;
			this.debitCredit = debitCredit;
			this.status = status;
		}
		
		public String getAccountType() {
			return accountType;
		}
		public void setAccountType(String accountType) {
			this.accountType = accountType;
		}
		public String getDebitCredit() {
			return debitCredit;
		}
		public void setDebitCredit(String debitCredit) {
			this.debitCredit = debitCredit;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getEffectiveDate() {
			return effectiveDate;
		}
		public void setEffectiveDate(String effectiveDate) {
			this.effectiveDate = effectiveDate;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getLedgerNo() {
			return ledgerNo;
		}
		public void setLedgerNo(String ledgerNo) {
			this.ledgerNo = ledgerNo;
		}
		public String getLedgerType() {
			return ledgerType;
		}
		public void setLedgerType(String ledgerType) {
			this.ledgerType = ledgerType;
		}
		public String getPosition() {
			return position;
		}
		public void setPosition(String position) {
			this.position = position;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getTotalingLedgerNo() {
			return totalingLedgerNo;
		}
		public void setTotalingLedgerNo(String totalingLedgerNo) {
			this.totalingLedgerNo = totalingLedgerNo;
		}
		public String getTotalingLevel() {
			return totalingLevel;
		}
		public void setTotalingLevel(String totalingLevel) {
			this.totalingLevel = totalingLevel;
		}

    }