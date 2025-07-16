package com.magbel.ia.vao;

public class ImprestItems {
	private String mtId;
	private String glAccount;
	private String refNumber;
	private String createdate;
	private String description;
	private String companycode;
	private double amount;
	private String type;
	private String expglaccount;
	private String otheracct;
	private String orderNo;
	/**
	 * 
	 */
	public ImprestItems() {
		// TODO Auto-generated constructor stub
	}

	/**  
	 * @param refNumber  
	 * @param description
	 * @param amount
	 */
	public ImprestItems(String mtId,String glAccount, String refNumber, String description,
			double amount,String expglaccount,String orderNo) {
		this.mtId = mtId;
		this.glAccount=glAccount;
		this.refNumber = refNumber;
		this.description = description;
		this.amount = amount;
		this.expglaccount = expglaccount;
		this.orderNo = orderNo;
	}
	public ImprestItems(String mtId,String glAccount, String refNumber, String description,
			double amount, String type,String expglaccount,String orderNo) {
		this.mtId = mtId;
		this.glAccount=glAccount;
		this.refNumber = refNumber;
		this.description = description;
		this.amount = amount;
		this.type = type;
		this.expglaccount = expglaccount;
		this.orderNo = orderNo;
	}
	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the refNumber
	 */
	public String getRefNumber() {
		return refNumber;
	}

	/**
	 * @param refNumber
	 *            the refNumber to set
	 */
	public void setRefNumber(String refNumber) {
		this.refNumber = refNumber;
	}

	/**
	 * @return the mtId
	 */
	public String getMtId() {
		return mtId;
	}

	/**
	 * @param mtId
	 *            the mtId to set
	 */
	public void setMtId(String mtId) {
		this.mtId = mtId;
	}

	/**
	 * @return the glAccount
	 */
	public String getGlAccount() {
		return glAccount;
	}

	/**
	 * @param glAccount the glAccount to set
	 */
	public void setGlAccount(String glAccount) {
		this.glAccount = glAccount;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the createdate
	 */
	public String getCreatedate() {
		return createdate;
	}

	/**
	 * @param createdate
	 *            the createdate to set
	 */
	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}
	/**
	 * @return the companycode
	 */
	public String getCompanycode() {
		return companycode;
	}

	/**
	 * @param companycode
	 *            the companycode to set
	 */
	public void setCompanycode(String companycode) {
		this.companycode = companycode;
	}	
	/**
	 * @return the expglaccount
	 */
	public String getExpglaccount() {
		return expglaccount;
	}

	/**
	 * @param expglaccount
	 *            the expglaccount to set
	 */
	public void setExpglaccount(String expglaccount) {
		this.expglaccount = expglaccount;
	}

	public String getOtheracct() {
		return otheracct;
	}
	public void setOtheracct(String otheracct) {
		this.otheracct = otheracct;
	}

	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}	
	
}
