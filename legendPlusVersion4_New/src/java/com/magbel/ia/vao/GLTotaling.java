package com.magbel.ia.vao;

/**
 * <p>Title: GLTotaling.java</p>
 *
 * <p>Description: GLTotaling Description</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: Magbel Technologies LTD</p>
 *
 * @author Bolanle M. Sule
 * @version 1.0
 */

public class GLTotaling {

	private String id;
	private String accountType;
	private String ledgerNo;
	private String levelNo;
    private String description;
	private String effectiveDate;
	private String totalingLevel;
	private String totalingLedgerNo;
	private String ledgerType;
	private String debitCredit;
    private String status;
	private int userId;
	private String createDate;
    
    public GLTotaling(){
	}
    
	public GLTotaling(String id, String accountType, String ledgerNo, String levelNo, String description,
					  String effectiveDate, String totalingLevel,String totalingLedgerNo, String ledgerType, 
					  String debitCredit, String status, int userId, String createDate) {
		super();
		this.id = id;
		this.accountType = accountType;
		this.ledgerNo = ledgerNo;
		this.levelNo = levelNo;
		this.description = description;
		this.effectiveDate = effectiveDate;
		this.totalingLevel = totalingLevel;
		this.totalingLedgerNo = totalingLedgerNo;
		this.ledgerType = ledgerType;
		this.debitCredit = debitCredit;
		this.status = status;
		this.userId = userId;
		this.createDate = createDate;
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
	public String getLevelNo() {
		return levelNo;
	}
	public void setLevelNo(String levelNo) {
		this.levelNo = levelNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTotalingLevel() {
		return totalingLevel;
	}
	public void setTotalingLevel(String totalingLevel) {
		this.totalingLevel = totalingLevel;
	}   
	public String getTotalingLedgerNo() {
		return totalingLedgerNo;
	}
	public void setTotalingLedgerNo(String totalingLedgerNo) {
		this.totalingLedgerNo = totalingLedgerNo;
	} 

	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
}