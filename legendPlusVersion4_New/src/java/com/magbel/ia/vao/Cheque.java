package com.magbel.ia.vao;

public class Cheque {

	private String id;
	private String chequeno;
	private String type;
	private String	currency;
	private String vendorCode;
	private String transDate;
	private String chequeDate;
	private String period;

	public Cheque() {

	}
	public Cheque(String id,String type, String currency,String vendorCode,
				  String transDate,String chequeDate,String period,String chequeno ) {

	this.id = id;
	this.type = type;
	this.currency = currency;
	this.vendorCode = vendorCode;
	this.transDate = transDate;
	this.chequeDate = chequeDate;
	this.period = period;
	this.chequeno = chequeno;

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getVendorCode() {
		return vendorCode;
	}

	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}
	public String getTransDate() {
		return transDate;
	}

	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}
	public String getChequeDate() {
		return chequeDate;
	}

	public void setChequeDate(String chequeDate) {
		this.chequeDate = chequeDate;
	}
	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}
	/**
	 * @return the chequeno
	 */
	public String getChequeno() {
		return chequeno;
	}
	/**
	 * @param chequeno the chequeno to set
	 */
	public void setChequeno(String chequeno) {
		this.chequeno = chequeno;
	}
}


