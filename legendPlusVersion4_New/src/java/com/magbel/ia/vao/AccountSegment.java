
package com.magbel.ia.vao;

public class AccountSegment{

	private String id;
	private String position;
	private String totalingLevel;
	private String accountType;
	private String code;
	private String description;
	private String level;
	private String ledgerNo;
	private String type;
	private String callerType;
	private String incomeTax;
	private String balanceType;
	private String normalBalance;
	private String status;
	private String effectiveDate;


	public AccountSegment(String id,String position, String totalingLevel, String accountType, String code,
				           String description, String level, String ledgerNo, String type,
				           String callerType, String incomeTax, String balanceType, String normalBalance,
				           String status, String effectiveDate){

		setId(id);
		setPosition(position);
		setTotalingLevel(totalingLevel);
		setAccountType(accountType);
		setCode(code);
		setDescription(description);
		setLevel(level);

		setledgerNo(ledgerNo);
		setType(type);
		setCallerType(callerType);
		setIncomeTax(incomeTax);
		setBalanceType(balanceType);
		setNormalBalance(normalBalance);
		setStatus(status);
		setEffectiveDate(effectiveDate);

	}

	public void setPosition(String position){
		this.position = position;
	}
	public void setTotalingLevel(String totalingLevel){
		this.totalingLevel = totalingLevel;
	}
	public void setAccountType(String accountType){
		this.accountType = accountType;
	}
	public void setCode(String code){
		this.code = code;
	}
	public void setDescription(String description){
		this.description = description;
	}
	public void setLevel(String level){
		this.level = level;
	}
	public void setledgerNo(String ledgerNo){
		this.ledgerNo = ledgerNo;
	}
	public void setType(String type){
		this.type = type;
	}
	public void setCallerType(String callerType){
		this.callerType = callerType;
	}
	public void setIncomeTax(String incomeTax){
		this.incomeTax = incomeTax;
	}
	public void setBalanceType(String balanceType){
		this.balanceType = balanceType;
	}
	public void setNormalBalance(String normalBalance){
		this.normalBalance = normalBalance;
	}
	public void setStatus(String status){
		this.status = status;
	}
	public void setEffectiveDate(String effectiveDate){
		this.effectiveDate = effectiveDate;
	}

	public String getPosition(){
		return this.position;
	}
	public String getTotalingLevel(){
		return this.totalingLevel;
	}
	public String getAccountType(){
		return this.accountType;
	}
	public String getCode(){
		return this.code;
	}
	public String getDescription(){
		return this.description;
	}
	public String getLevel(){
		return this.level;
	}
	public String getledgerNo(){
		return this.ledgerNo;
	}
	public String getType(){
		return this.type;
	}
	public String getCallerType(){
		return this.callerType;
	}
	public String getIncomeTax(){
		return this.incomeTax;
	}
	public String getBalanceType(){
		return this.balanceType;
	}
	public String getNormalBalance(){
		return this.normalBalance;
	}
	public String getStatus(){
		return this.status;
	}
	public String getEffectiveDate(){
		return this.effectiveDate;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the ledgerNo
	 */
	public String getLedgerNo() {
		return ledgerNo;
	}

	/**
	 * @param ledgerNo the ledgerNo to set
	 */
	public void setLedgerNo(String ledgerNo) {
		this.ledgerNo = ledgerNo;
	}
}