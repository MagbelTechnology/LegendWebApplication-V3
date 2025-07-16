
package com.magbel.ia.vao;

public class Country{

	private String cId;
	private String code;
	private String description;
	private String homeCountry;
	private String status;
	private String userId;
	private String createDate;
	private String effectiveDate;

	public Country(){}

	public Country(String cId, String code, String description, String homeCountry, String status,
				   String userId, String createDate, String effectiveDate) {

		super();
		this.cId = cId;
		this.code = code;
		this.description = description;
		this.homeCountry = homeCountry;
		this.status = status;
		this.userId = userId;
		this.createDate = createDate;
		this.effectiveDate = effectiveDate;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getHomeCountry() {
		return homeCountry;
	}

	public void setHomeCountry(String homeCountry) {
		this.homeCountry = homeCountry;
	}

	public String getCId() {
		return cId;
	}

	public void setCId(String cId) {
		this.cId = cId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
}
