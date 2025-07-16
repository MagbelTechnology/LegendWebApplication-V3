package com.magbel.ia.vao;

public class Suffix {

	private String suffixId;

	private String suffixCode;

	private String description;

	private String chargeCode;

	private String status;

	private String createDate;

	public Suffix() {

	}

	public Suffix(String suffixId, String suffixCode, String description,
			      String chargeCode, String status, String createDate) {

		this.suffixId = suffixId;
		this.suffixCode = suffixCode;
		this.description = description;
		this.chargeCode = chargeCode;
		this.status = status;
		this.createDate = createDate;

		}


	public String getChargeCode() {
		return chargeCode;
	}

	public void setChargeCode(String chargeCode) {
		this.chargeCode = chargeCode;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSuffixCode() {
		return suffixCode;
	}

	public void setSuffixCode(String suffixCode) {
		this.suffixCode = suffixCode;
	}

	public String getSuffixId() {
		return suffixId;
	}

	public void setSuffixId(String suffixId) {
		this.suffixId = suffixId;
	}

	

}