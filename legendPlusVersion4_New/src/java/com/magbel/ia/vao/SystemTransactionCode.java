package com.magbel.ia.vao;

/**
 * <p>
 * Title:  System Transaction Code.java (IAS)
 * </p>
 * 
 * <p>
 * Description: System Transaction Code
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) Jan. 25th, 2008
 * </p>
 * 
 * <p>
 * Company: Magbel Technologies LTD
 * </p>
 * 
 * @author Bolanle M.Sule
 * @version 1.0
 */


public class SystemTransactionCode{

	private String sysId;
	private String code;
	private String description;
	private String drCr;
	private String createDate;
	private String status;
	private String cotCharge;
	
	public SystemTransactionCode(){}
	
	public SystemTransactionCode(String sysId, String code, String description, String drCr, 
	                             String createDate, String status, String cotCharge) {
		super();
		this.sysId = sysId;
		this.code = code;
		this.description = description;
		this.drCr = drCr;
		this.createDate = createDate;
		this.status = status;
		this.cotCharge = cotCharge;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCotCharge() {
		return cotCharge;
	}

	public void setCotCharge(String cotCharge) {
		this.cotCharge = cotCharge;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDrCr() {
		return drCr;
	}

	public void setDrCr(String drCr) {
		this.drCr = drCr;
	}

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}