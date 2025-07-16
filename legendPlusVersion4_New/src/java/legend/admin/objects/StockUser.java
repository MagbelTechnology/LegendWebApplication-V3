package legend.admin.objects;

public class StockUser {
	
	private String mtId;
	private String compCode;
	private String userCode;
	private String userName;
	private String address;
	private String userId;
	private String createDate;
	private String status;
	
	public StockUser()
	{
		
	}


	public void setMtId(String mtId) {
		this.mtId = mtId;
	}


	public String getMtId() {
		return mtId;
	}


	public void setCompCode(String compCode) {
		this.compCode = compCode;
	}


	public String getCompCode() {
		return compCode;
	}


	/**
	 * @param userCode the userCode to set
	 */
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}


	/**
	 * @return the userCode
	 */
	public String getUserCode() {
		return userCode;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}


	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}



	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}


	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}


	/**
	 * @param userId the userId to set
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
	 * @param createDate the createDate to set
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


	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}


	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

}
