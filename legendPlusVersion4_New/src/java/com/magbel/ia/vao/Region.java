package com.magbel.ia.vao;

public class Region {

	private String regionId;

	private String regionCode;

	private String regionName;

	private String regionAcronym;

	private String regionAddress;

	private String regionPhone;

	private String regionFax;

	private String regionStatus;

	private String userId;

	private String createDate;

	public Region() {
		// TODO Auto-generated constructor stub
	}

	public Region(String regionId, String regionCode, String regionName,
			String regionAcronym, String regionAddress, String regionPhone,
			String regionFax, String regionStatus, String userId,
			String createDate) {

		this.regionId = regionId;
		this.regionCode = regionCode;
		this.regionName = regionName;
		this.regionAcronym = regionAcronym;
		this.regionAddress = regionAddress;
		this.regionPhone = regionPhone;
		this.regionFax = regionFax;
		this.regionStatus = regionStatus;
		this.userId = userId;
		this.createDate = createDate;

	}

	/**
	 * @param regionId
	 *            the regionId to set
	 */
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	/**
	 * @return the regionId
	 */
	public String getRegionId() {
		return regionId;
	}

	/**
	 * @param regionCode
	 *            the regionCode to set
	 */
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	/**
	 * @return the regionCode
	 */
	public String getRegionCode() {
		return regionCode;
	}

	/**
	 * @param regionName
	 *            the regionName to set
	 */
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	/**
	 * @return the regionName
	 */
	public String getRegionName() {
		return regionName;
	}

	/**
	 * @param regionAcronym
	 *            the regionAcronym to set
	 */
	public void setRegionAcronym(String regionAcronym) {
		this.regionAcronym = regionAcronym;
	}

	/**
	 * @return the regionAcronym
	 */
	public String getRegionAcronym() {
		return regionAcronym;
	}

	/**
	 * @param regionAddress
	 *            the regionAddress to set
	 */
	public void setRegionAddress(String regionAddress) {
		this.regionAddress = regionAddress;
	}

	/**
	 * @return the regionAddress
	 */
	public String getRegionAddress() {
		return regionAddress;
	}

	/**
	 * @param regionPhone
	 *            the regionPhone to set
	 */
	public void setRegionPhone(String regionPhone) {
		this.regionPhone = regionPhone;
	}

	/**
	 * @return the regionPhone
	 */
	public String getRegionPhone() {
		return regionPhone;
	}

	/**
	 * @param regionFax
	 *            the regionFax to set
	 */
	public void setRegionFax(String regionFax) {
		this.regionFax = regionFax;
	}

	/**
	 * @return the regionFax
	 */
	public String getRegionFax() {
		return regionFax;
	}

	/**
	 * @param regionStatus
	 *            the regionStatus to set
	 */
	public void setRegionStatus(String regionStatus) {
		this.regionStatus = regionStatus;
	}

	/**
	 * @return the regionStatus
	 */
	public String getRegionStatus() {
		return regionStatus;
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
