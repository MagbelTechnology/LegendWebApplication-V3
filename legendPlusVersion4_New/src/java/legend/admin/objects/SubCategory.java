/**
 * 
 */
package legend.admin.objects;

/**
 * @author Lekan Matanmi
 * 
 */
public class SubCategory {
	private String assetSubCategoryId;

	private String assetSubCategoryCode;

	private String assetSubCategory;

	private String status;

	private String category;

	private String userid;

	private String createDate;

	/**
	 * 
	 */
	public SubCategory() {
		// TODO Auto-generated constructor stub
	}

	public SubCategory(String assetSubCategoryId, String assetSubCategoryCode,
			String assetSubCategory, String status, String category, String userid,
			String createDate) {
		// TODO Auto-generated constructor stub

		this.assetSubCategoryId = assetSubCategoryId;
		this.assetSubCategoryCode = assetSubCategoryCode;
		this.assetSubCategory = assetSubCategory;
		this.status = status;
		this.category = category;
		this.userid = userid;
		this.createDate = createDate;

	}

	/**
	 * @param AssetSubCategoryId
	 *            the AssetSubCategoryId to set
	 */
	public void setAssetSubCategoryId(String assetSubCategoryId) {
		this.assetSubCategoryId = assetSubCategoryId;
	}

	/**
	 * @return the assetSubCategoryId
	 */
	public String getAssetSubCategoryId() {
		return assetSubCategoryId;
	}

	/**
	 * @param assetSubCategoryCode
	 *            the assetSubCategoryCode to set
	 */
	public void setAssetSubCategoryCode(String assetSubCategoryCode) {
		this.assetSubCategoryCode = assetSubCategoryCode;
	}

	/**
	 * @return the assetSubCategoryCode
	 */
	public String getAssetSubCategoryCode() {
		return assetSubCategoryCode;
	}

	/**
	 * @param assetSubCategory
	 *            the assetSubCategory to set
	 */
	public void setAssetSubCategory(String assetSubCategory) {
		this.assetSubCategory = assetSubCategory;
	}

	/**
	 * @return the assetMake
	 */
	public String getAssetSubCategory() {
		return assetSubCategory;
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
