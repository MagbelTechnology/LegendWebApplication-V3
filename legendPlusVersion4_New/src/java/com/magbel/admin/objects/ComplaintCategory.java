package com.magbel.admin.objects;

public class ComplaintCategory {

	private String complaintId;

	private String complaintCode;

	private String complaintName;

	private String complaintStatus;

	private String userId;

	private String createDate;

	public ComplaintCategory() {

	}

	public ComplaintCategory(String complaintId, String complaintCode, String complaintName,
			String complaintStatus, String userId, String createDate) {

		this.complaintId = complaintId;
		this.complaintCode = complaintCode;
		this.complaintName = complaintName;
		this.complaintStatus = complaintStatus;
		this.userId = userId;
		this.createDate = createDate;

	}

	/**
	 * @param complaintId
	 *            the complaintId to set
	 */
	public void setcomplaintId(String complaintId) {
		this.complaintId = complaintId;
	}

	/**
	 * @return the complaintId
	 */
	public String getcomplaintId() {
		return complaintId;
	}

	/**
	 * @param complaintCode
	 *            the complaintCode to set
	 */
	public void setcomplaintCode(String complaintCode) {
		this.complaintCode = complaintCode;
	}

	/**
	 * @return the complaintCode
	 */
	public String getcomplaintCode() {
		return complaintCode;
	}

	/**
	 * @param complaintName
	 *            the complaintName to set
	 */
	public void setcomplaintName(String complaintName) {
		this.complaintName = complaintName;
	}

	/**
	 * @return the complaintName
	 */
	public String getcomplaintName() {
		return complaintName;
	}

	/**
	 * @param complaintStatus
	 *            the complaintStatus to set
	 */
	public void setcomplaintStatus(String complaintStatus) {
		this.complaintStatus = complaintStatus;
	}

	/**
	 * @return the complaintStatus
	 */
	public String getcomplaintStatus() {
		return complaintStatus;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param createDate
	 *            the createDate to set
	 */
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the createDate
	 */
	public String getCreateDate() {
		return createDate;
	}
}
