/**
 * 
 */
package com.magbel.ia.vao;

/**
 * @author Rahman O. Oloritun
 * 
 */
public class Imprest {

	private String mtId;

	private String refNumber;

	private String beneficiary;

	private String impAccNumber; // Imprest Account Number

	private String benAccNumber; // benficiary Account Number

	private String purpose;

	private String expiryDate;

	private String isRetired;

	private String userId;

	private String supervisorId;

	private String transDate; // Transaction Date

	private String effDate; // Application Date

	private String isCash;

	private String isPosted;

	private String retiredDate;

	private double amount;
	
	private double retire_Amount;
	
	private String ledgerNo;
	
	private String orderNo;

	/**
	 * @param mtId
	 * @param refNumber
	 * @param beneficiary
	 * @param impAccNumber
	 * @param benAccNumber
	 * @param purpose
	 * @param expiryDate
	 * @param isRetired
	 * @param userId
	 * @param supervisorId
	 * @param transDate
	 * @param effDate
	 * @param isCash
	 */
	public Imprest(String mtId, String refNumber, String beneficiary,
			String impAccNumber, String benAccNumber, String purpose,
			String expiryDate, String isRetired, String userId,
			String supervisorId, String transDate, String effDate,
			String isCash, double amount,String orderNo) {
		this.mtId = mtId;
		this.refNumber = refNumber;
		this.beneficiary = beneficiary;
		this.impAccNumber = impAccNumber;
		this.benAccNumber = benAccNumber;
		this.purpose = purpose;
		this.expiryDate = expiryDate;
		this.isRetired = isRetired;
		this.userId = userId;
		this.supervisorId = supervisorId;
		this.transDate = transDate;
		this.effDate = effDate;
		this.isCash = isCash;
		this.amount = amount;
		this.orderNo = orderNo;
	}

	/**
	 * 
	 */
	public Imprest() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the benAccNumber
	 */
	public String getBenAccNumber() {
		return benAccNumber;
	}

	/**
	 * @param benAccNumber
	 *            the benAccNumber to set
	 */
	public void setBenAccNumber(String benAccNumber) {
		this.benAccNumber = benAccNumber;
	}

	/**
	 * @return the beneficiary
	 */
	public String getBeneficiary() {
		return beneficiary;
	}

	/**
	 * @param beneficiary
	 *            the beneficiary to set
	 */
	public void setBeneficiary(String beneficiary) {
		this.beneficiary = beneficiary;
	}

	/**
	 * @return the effDate
	 */
	public String getEffDate() {
		return effDate;
	}

	/**
	 * @param effDate
	 *            the effDate to set
	 */
	public void setEffDate(String effDate) {
		this.effDate = effDate;
	}

	/**
	 * @return the expiryDate
	 */
	public String getExpiryDate() {
		return expiryDate;
	}

	/**
	 * @param expiryDate
	 *            the expiryDate to set
	 */
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	/**
	 * @return the impAccNumber
	 */
	public String getImpAccNumber() {
		return impAccNumber;
	}

	/**
	 * @param impAccNumber
	 *            the impAccNumber to set
	 */
	public void setImpAccNumber(String impAccNumber) {
		this.impAccNumber = impAccNumber;
	}

	/**
	 * @return the isCash
	 */
	public String getIsCash() {
		return isCash;
	}

	/**
	 * @param isCash
	 *            the isCash to set
	 */
	public void setIsCash(String isCash) {
		this.isCash = isCash;
	}

	/**
	 * @return the isRetired
	 */
	public String getIsRetired() {
		return isRetired;
	}

	/**
	 * @param isRetired
	 *            the isRetired to set
	 */
	public void setIsRetired(String isRetired) {
		this.isRetired = isRetired;
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
	 * @return the purpose
	 */
	public String getPurpose() {
		return purpose;
	}

	/**
	 * @param purpose
	 *            the purpose to set
	 */
	public void setPurpose(String purpose) {
		this.purpose = purpose;
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
	 * @return the supervisorId
	 */
	public String getSupervisorId() {
		return supervisorId;
	}

	/**
	 * @param supervisorId
	 *            the supervisorId to set
	 */
	public void setSupervisorId(String supervisorId) {
		this.supervisorId = supervisorId;
	}

	/**
	 * @return the transDate
	 */
	public String getTransDate() {
		return transDate;
	}

	/**
	 * @param transDate
	 *            the transDate to set
	 */
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
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
	 * @return the isPosted
	 */
	public String getIsPosted() {
		return isPosted;
	}

	/**
	 * @param isPosted
	 *            the isPosted to set
	 */
	public void setIsPosted(String isPosted) {
		this.isPosted = isPosted;
	}

	/**
	 * @return the retiredDate
	 */
	public String getRetiredDate() {
		return retiredDate;
	}

	/**
	 * @param retiredDate
	 *            the retiredDate to set
	 */
	public void setRetiredDate(String retiredDate) {
		this.retiredDate = retiredDate;
	}

	/**
	 * @return the retire_Amount
	 */
	public double getRetire_Amount() {
		return retire_Amount;
	}

	/**
	 * @param retire_Amount the retire_Amount to set
	 */
	public void setRetire_Amount(double retire_Amount) {
		this.retire_Amount = retire_Amount;
	}

	/**
	 * @return the ledgerNo
	 */
	public String getLedgerNo() {
		return ledgerNo;
	}

	/**orderNo
	 * @param ledgerNo
	 *            the ledgerNo to set
	 */
	public void setLedgerNo(String ledgerNo) {
		this.ledgerNo = ledgerNo;
	}
	
	/**
	 * @return the orderNo
	 */
	public String getOrderNo() {
		return orderNo;
	}	

	/**
	 * @param orderNo
	 *            the orderNo to set
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}	

}
