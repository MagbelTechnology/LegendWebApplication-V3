package legend.admin.objects;

public class DisposableReasons {
	private String reasonId;
	private String reasonCode;
	private String description;
	private String reasonStatus;
	private String userId;
	private String createDate;

	
	public DisposableReasons(){
		
	}	


	/**
	 * @return the reasonId
	 */
	public String getReasonId() {
		return reasonId;
	}

	/**
	 * @param reasonId the reasonId to set
	 */
	public void setReasonId(String reasonId) {
		this.reasonId = reasonId;
	}

	/**
	 * @return the reasonCode
	 */
	public String getReasonCode() {
		return reasonCode;
	}


	/**
	 * @param reasonCode the reasonCode to set
	 */
	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}


	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}


	/**
	 * @return the reasonStatus
	 */
	public String getReasonStatus() {
		return reasonStatus;
	}


	/**
	 * @param reasonStatus the reasonStatus to set
	 */
	public void setReasonStatus(String reasonStatus) {
		this.reasonStatus = reasonStatus;
	}


	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}


	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}


	/**
	 * @return the createDate
	 */
	public String getCreateDate() {
		return createDate;
	}


	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}	

}
