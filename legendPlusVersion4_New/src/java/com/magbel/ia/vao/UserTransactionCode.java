package com.magbel.ia.vao;

/**
 * <p>
 * Title:  User Transaction Code.java (IAS)
 * </p>
 * 
 * <p>
 * Description: User Transaction Code
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) Jan. 26th, 2008
 * </p>
 * 
 * <p>
 * Company: Magbel Technologies LTD
 * </p>
 * 
 * @author Bolanle M.Sule
 * @version 1.0
 */

public class UserTransactionCode{

	private String uId;
	private String userTranCode;
	private String code;
	private String description;
	private String createDate;
	private String status;
	private String userId;
	private String companyCode;
	private String companyTranType;
	private String companyApprovalLevel;
	
	public UserTransactionCode(String id, String userTranCode, String code,
			String description, String createDate, String status,
			String userId, String companyCode, String companyTranType,
			String companyApprovalLevel) {
		super();
		uId = id;
		this.userTranCode = userTranCode;
		this.code = code;
		this.description = description;
		this.createDate = createDate;
		this.status = status;
		this.userId = userId;
		this.companyCode = companyCode;
		this.companyTranType = companyTranType;
		this.companyApprovalLevel = companyApprovalLevel;
	}

	public String getcompanyTranType() {
		return companyTranType;
	}

	public void setcompanyTranType(String companyTranType) {
		this.companyTranType = companyTranType;
	}

	public String getCompanyApprovalLevel() {
		return companyApprovalLevel;
	}

	public void setCompanyApprovalLevel(String companyApprovalLevel) {
		this.companyApprovalLevel = companyApprovalLevel;
	}

	public UserTransactionCode(){}
	
	public UserTransactionCode(String uId, String userTranCode, String code, String description,
                               String createDate, String status ,String userId, String companyCode) {
		super();
		this.uId = uId;
		this.userTranCode = userTranCode;
		this.code = code;
		this.description = description;
		this.createDate = createDate;
		this.status = status;
		this.userId = userId;
		this.companyCode = companyCode;
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

	public String getUId() {
		return uId;
	}

	public void setUId(String uId) {
		this.uId = uId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserTranCode() {
		return userTranCode;
	}

	public void setUserTranCode(String userTranCode) {
		this.userTranCode = userTranCode;
	}	

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getCompanyCode() {
		return companyCode;
	}
	
}