package com.magbel.ia.vao;


/**
 * <p>Title: filestartDate.java</p>
 *
 * <p>Description: File Description</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: Magbel Technologies LTD</p>
 *
 * @author Bolanle M. Sule
 * @version 1.0
 */

public class LedgerType {

	private String id;
	private String ledgerCode;
	private String position;
	private String ledgerType;
	private String startNo;
    private String parentId;
	private String child;
	private String debitCredit;
    

    public LedgerType(String id, String ledgerCode, String position, String ledgerType, String startNo, 
	                  String parentId, String child, String debitCredit) {
		super();
		this.id = id;
		this.ledgerCode = ledgerCode;
		this.position = position;
		this.ledgerType = ledgerType;
		this.startNo = startNo;
		this.parentId = parentId;
		this.child = child;
		this.debitCredit = debitCredit;
	}


	public String getDebitCredit() {
		return debitCredit;
	}


	public void setDebitCredit(String debitCredit) {
		this.debitCredit = debitCredit;
	}


	public String getLedgerType() {
		return ledgerType;
	}


	public void setLedgerType(String ledgerType) {
		this.ledgerType = ledgerType;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getLedgerCode() {
		return ledgerCode;
	}


	public void setLedgerCode(String ledgerCode) {
		this.ledgerCode = ledgerCode;
	}


	public String getParentId() {
		return parentId;
	}


	public void setParentId(String parentId) {
		this.parentId = parentId;
	}


	public String getPosition() {
		return position;
	}


	public void setPosition(String position) {
		this.position = position;
	}


	public String getStartNo() {
		return startNo;
	}


	public void setStartNo(String startNo) {
		this.startNo = startNo;
	}


	public String getChild() {
		return child;
	}


	public void setChild(String child) {
		this.child = child;
	}	
}
