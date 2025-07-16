package com.magbel.ia.vao;

public class State {

	private String stateId;

	private String stateCode;

	private String stateName;

	private String stateStatus;

	private String userId;

	private String createDate;
	
	private String countryCode;

	public State() {

	}

	public State(String stateId, String stateCode, String stateName, String stateStatus,
				 String userId, String createDate, String countryCode) {

		this.stateId = stateId;
		this.stateCode = stateCode;
		this.stateName = stateName;
		this.stateStatus = stateStatus;
		this.userId = userId;
		this.createDate = createDate;
		this.countryCode = countryCode;

	}

	/**
	 * @param stateId
	 *            the stateId to set
	 */
	public void setStateId(String stateId) {
		this.stateId = stateId;
	}

	/**
	 * @return the stateId
	 */
	public String getStateId() {
		return stateId;
	}

	/**
	 * @param stateCode
	 *            the stateCode to set
	 */
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	/**
	 * @return the stateCode
	 */
	public String getStateCode() {
		return stateCode;
	}

	/**
	 * @param stateName
	 *            the stateName to set
	 */
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	/**
	 * @return the stateName
	 */
	public String getStateName() {
		return stateName;
	}

	/**
	 * @param stateStatus
	 *            the stateStatus to set
	 */
	public void setStateStatus(String stateStatus) {
		this.stateStatus = stateStatus;
	}

	/**
	 * @return the stateStatus
	 */
	public String getStateStatus() {
		return stateStatus;
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
	
	/**
	 * @param countryCode
	 *            the countryCode to set
	 */
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	/**
	 * @return the countryCode
	 */
	public String getCountryCode() {
		return countryCode;
	}
}
