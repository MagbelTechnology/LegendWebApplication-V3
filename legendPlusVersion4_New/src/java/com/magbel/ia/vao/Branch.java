/**
 * 
 */
package com.magbel.ia.vao;

/**
 * @author Rahman Oloritun
  * @update Bolanle M Sule.
 * @company magbel Technology Ltd.
 * @Table am_ad_branch
 * @version 1.00
 */
public class Branch {

	private String branchId;

	private String branchCode;

	private String branchName;

	private String branchAcronym;

	private String glPrefix;

	private String glSuffix;

	private String branchAddress;

	private String state;

	private String phoneNo;

	private String faxNo;

	private String region;

	private String province;

	private String branchStatus;

	private String username;

	private String create_date;
	
	private String companyCode;

	/**
	 * 
	 */

	public Branch() {
		// TODO Auto-generated constructor stub
	}

	public Branch(String branchId, String branchCode, String branchName,
			String branchAcronym, String glPrefix, String glSuffix,
			String branchAddress, String state, String phoneNo, String faxNo,
			String region, String province, String branchStatus,
			String username, String create_date, String companyCode) {

		this.branchId = branchId;
		this.branchCode = branchCode;
		this.branchName = branchName;
		this.branchAcronym =branchAcronym;
		this.glPrefix = glPrefix;
		this.glSuffix = glSuffix;
		this.branchAddress = branchAddress;
		this.state = state;
		this.phoneNo = phoneNo;
		this.faxNo=faxNo;
		this.region = region;
		this.province = province;
		this.branchStatus = branchStatus;
		this.username = username;
		this.create_date = create_date;
		this.companyCode = companyCode;
	}

	/**
	 * @param branchId
	 *            the branchId to set
	 */
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	/**
	 * @return the branchId
	 */
	public String getBranchId() {
		return branchId;
	}

	/**
	 * @param branchCode
	 *            the branchCode to set
	 */
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	/**
	 * @return the branchCode
	 */
	public String getBranchCode() {
		return branchCode;
	}

	/**
	 * @param branchName
	 *            the branchName to set
	 */
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	/**
	 * @return the branchName
	 */
	public String getBranchName() {
		return branchName;
	}

	/**
	 * @param branchAcronym
	 *            the branchAcronym to set
	 */
	public void setBranchAcronym(String branchAcronym) {
		this.branchAcronym = branchAcronym;
	}

	/**
	 * @return the branchAcronym
	 */
	public String getBranchAcronym() {
		return branchAcronym;
	}

	/**
	 * @param glPrefix
	 *            the glPrefix to set
	 */
	public void setGlPrefix(String glPrefix) {
		this.glPrefix = glPrefix;
	}

	/**
	 * @return the glPrefix
	 */
	public String getGlPrefix() {
		return glPrefix;
	}

	/**
	 * @param glSuffix
	 *            the glSuffix to set
	 */
	public void setGlSuffix(String glSuffix) {
		this.glSuffix = glSuffix;
	}

	/**
	 * @return the glSuffix
	 */
	public String getGlSuffix() {
		return glSuffix;
	}

	/**
	 * @param branchAddress
	 *            the branchAddress to set
	 */
	public void setBranchAddress(String branchAddress) {
		this.branchAddress = branchAddress;
	}

	/**
	 * @return the branchAddress
	 */
	public String getBranchAddress() {
		return branchAddress;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param phoneNo
	 *            the phoneNo to set
	 */
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	/**
	 * @return the phoneNo
	 */
	public String getPhoneNo() {
		return phoneNo;
	}

	/**
	 * @param faxNo
	 *            the faxNo to set
	 */
	public void setFaxNo(String faxNo) {
		this.faxNo = faxNo;
	}

	/**
	 * @return the faxNo
	 */
	public String getFaxNo() {
		return faxNo;
	}

	/**
	 * @param region
	 *            the region to set
	 */
	public void setRegion(String region) {
		this.region = region;
	}

	/**
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * @param province
	 *            the province to set
	 */
	public void setProvince(String province) {
		this.province = province;
	}

	/**
	 * @return the province
	 */
	public String getProvince() {
		return province;
	}

	/**
	 * @param branchStatus
	 *            the branchStatus to set
	 */
	public void setBranchStatus(String branchStatus) {
		this.branchStatus = branchStatus;
	}

	/**
	 * @return the branchStatus
	 */
	public String getBranchStatus() {
		return branchStatus;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param create_date
	 *            the create_date to set
	 */
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	/**
	 * @return the create_date
	 */
	public String getCreate_date() {
		return create_date;
	}
	
	
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getCompanyCode() {
		return companyCode;
	}

}
