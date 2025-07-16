package com.magbel.crms.vao;

public class ImprestAccount {

	private String mtId;

	private String code;

	private String type;

	private String glAccount;

	private int retirePolicy;

	private int grace;

	private double balance;

	/**
	 * @param mtId
	 * @param code
	 * @param type
	 * @param glAccount
	 * @param retirePolicy
	 * @param grace
	 */
	public ImprestAccount(String mtId, String code, String type,
			String glAccount, int retirePolicy, int grace) {
		this.mtId = mtId;
		this.code = code;
		this.type = type;
		this.glAccount = glAccount;
		this.retirePolicy = retirePolicy;
		this.grace = grace;
	}

	/**
	 * 
	 */
	public ImprestAccount() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the balance
	 */
	public double getBalance() {
		return balance;
	}

	/**
	 * @param balance
	 *            the balance to set
	 */
	public void setBalance(double balance) {
		this.balance = balance;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the glAccount
	 */
	public String getGlAccount() {
		return glAccount;
	}

	/**
	 * @param glAccount
	 *            the glAccount to set
	 */
	public void setGlAccount(String glAccount) {
		this.glAccount = glAccount;
	}

	/**
	 * @return the grace
	 */
	public int getGrace() {
		return grace;
	}

	/**
	 * @param grace
	 *            the grace to set
	 */
	public void setGrace(int grace) {
		this.grace = grace;
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
	 * @return the retirePolicy
	 */
	public int getRetirePolicy() {
		return retirePolicy;
	}

	/**
	 * @param retirePolicy
	 *            the retirePolicy to set
	 */
	public void setRetirePolicy(int retirePolicy) {
		this.retirePolicy = retirePolicy;
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

}
