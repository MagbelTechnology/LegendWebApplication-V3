/**
 * 
 */
package com.magbel.ia.legend.admin.objects;

/**
 * @author Rahman Oloritun
 * 
 */
public class AssetMake {
	private String assetMakeId;

	private String assetMakeCode;

	private String assetMake;

	private String status;

	private String category;

	private String userid;

	private String createDate;

	/**
	 * 
	 */
	public AssetMake() {
		// TODO Auto-generated constructor stub
	}

	public AssetMake(String assetMakeId, String assetMakeCode,
			String assetMake, String status, String category, String userid,
			String createDate) {
		// TODO Auto-generated constructor stub

		this.assetMakeId = assetMakeId;
		this.assetMakeCode = assetMakeCode;
		this.assetMake = assetMake;
		this.status = status;
		this.category = category;
		this.userid = userid;
		this.createDate = createDate;

	}

	/**
	 * @param assetMakeId
	 *            the assetMakeId to set
	 */
	public void setAssetMakeId(String assetMakeId) {
		this.assetMakeId = assetMakeId;
	}

	/**
	 * @return the assetMakeId
	 */
	public String getAssetMakeId() {
		return assetMakeId;
	}

	/**
	 * @param assetMakeCode
	 *            the assetMakeCode to set
	 */
	public void setAssetMakeCode(String assetMakeCode) {
		this.assetMakeCode = assetMakeCode;
	}

	/**
	 * @return the assetMakeCode
	 */
	public String getAssetMakeCode() {
		return assetMakeCode;
	}

	/**
	 * @param assetMake
	 *            the assetMake to set
	 */
	public void setAssetMake(String assetMake) {
		this.assetMake = assetMake;
	}

	/**
	 * @return the assetMake
	 */
	public String getAssetMake() {
		return assetMake;
	}

	/**
	 * @param status
	 *            the status to set
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

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param userid
	 *            the userid to set
	 */
	public void setUserid(String userid) {
		this.userid = userid;
	}

	/**
	 * @return the userid
	 */
	public String getUserid() {
		return userid;
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
