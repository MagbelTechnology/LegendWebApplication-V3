package com.magbel.ia.vao;

/**
 * <p>
 * Title: Company.java
 * </p>
 * 
 * <p>
 * Description: Company
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: Magbel Technologies LTD
 * </p>
 * 
 * @author Lanre O
 * @version 1.0
 */

public class ApprovalRemark {

	    private String Id =""; 
	    private String  supervisorID =""; 
	    private String  DateRequisitioned =""; 
	    private String Remark  =""; 
	    private String  status =""; 
	    private String ApprovalLevel  =""; 
	    private String  RemarkDate =""; 
	    private String  IPAddress=""; 

	public ApprovalRemark() {
	}

	public ApprovalRemark(String id, String supervisorID,
			String dateRequisitioned, String remark, String status,
			String approvalLevel, String remarkDate, String address) {
		super();
		this.Id = id;
		this.supervisorID = supervisorID;
		this.DateRequisitioned = dateRequisitioned;
		this.Remark = remark;
		this.status = status;
		this.ApprovalLevel = approvalLevel;
		this.RemarkDate = remarkDate;
		this.IPAddress = address;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getSupervisorID() {
		return supervisorID;
	}

	public void setSupervisorID(String supervisorID) {
		this.supervisorID = supervisorID;
	}

	public String getDateRequisitioned() {
		return DateRequisitioned;
	}

	public void setDateRequisitioned(String dateRequisitioned) {
		DateRequisitioned = dateRequisitioned;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getApprovalLevel() {
		return ApprovalLevel;
	}

	public void setApprovalLevel(String approvalLevel) {
		ApprovalLevel = approvalLevel;
	}

	public String getRemarkDate() {
		return RemarkDate;
	}

	public void setRemarkDate(String remarkDate) {
		RemarkDate = remarkDate;
	}

	public String getIPAddress() {
		return IPAddress;
	}

	public void setIPAddress(String address) {
		IPAddress = address;
	}

	
	
}
