package magma.net.vao;


public class GroupAssets {
	private long gid;
	private int quantity;
	 private String vatable_cost = "0";
	private String branch_id = "";

	private String department_id = "";

	private String category_id = "";

	private String posting_date = null;
  
	private String make = "";

	private String location = "";

	private String supplied_by = "";

	private String description = "";

	private String vendor_account = "";

	private String cost_price = "0";

	private String vat_amount = "0";

	private String model = "";

	private String subject_to_vat = "";

	private String date_of_purchase = null;

	private String registration_no = "";

	private String raise_entry = "N";

	private String state = "";

	private String section_id = "";

	private String wh_tax_cb = "N";

	private String wh_tax_amount = "0";

	private String province = "";

	private String amountPTD;

	private String amountREM;
	private String user_id;

	private String partPAY = "N";

	private String fullyPAID = "N";

	/**
	 * 
	 */
	public GroupAssets() {

	}

	/**
	 * @param gid
	 * @param branch_id
	 * @param department_id
	 * @param category_id
	 * @param posting_date
	 * @param make
	 * @param location
	 * @param supplied_by
	 * @param description
	 * @param vendor_account
	 * @param cost_price
	 * @param vat_amount
	 * @param model
	 * @param subject_to_vat
	 * @param date_of_purchase
	 * @param registration_no
	 * @param raise_entry
	 * @param state
	 * @param section_id
	 * @param wh_tax_cb
	 * @param wh_tax_amount
	 * @param province
	 * @param amountPTD
	 * @param amountREM
	 * @param partPAY
	 * @param fullyPAID
	 */
	
	/**
	 * @return the amountPTD
	 */
	public String getAmountPTD() {
		return amountPTD;
	}

	/**
	 * @param amountPTD
	 *            the amountPTD to set
	 */
	public void setAmountPTD(String amountPTD) {
		this.amountPTD = amountPTD;
	}

	/**
	 * @return the amountREM
	 */
	public String getAmountREM() {
		return amountREM;
	}

	/**
	 * @param amountREM
	 *            the amountREM to set
	 */
	public void setAmountREM(String amountREM) {
		this.amountREM = amountREM;
	}

	/**
	 * @return the branch_id
	 */
	public String getBranch_id() {
		return branch_id;
	}

	/**
	 * @param branch_id
	 *            the branch_id to set
	 */
	public void setBranch_id(String branch_id) {
		this.branch_id = branch_id;
	}

	/**
	 * @return the category_id
	 */
	public String getCategory_id() {
		return category_id;
	}

	/**
	 * @param category_id
	 *            the category_id to set
	 */
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	/**
	 * @return the cost_price
	 */
	public String getCost_price() {
		return cost_price;
	}

	/**
	 * @param cost_price
	 *            the cost_price to set
	 */
	public void setCost_price(String cost_price) {
		this.cost_price = cost_price;
	}

	/**
	 * @return the date_of_purchase
	 */
	public String getDate_of_purchase() {
		return date_of_purchase;
	}

	/**
	 * @param date_of_purchase
	 *            the date_of_purchase to set
	 */
	public void setDate_of_purchase(String date_of_purchase) {
		this.date_of_purchase = date_of_purchase;
	}

	/**
	 * @return the department_id
	 */
	public String getDepartment_id() {
		return department_id;
	}

	/**
	 * @param department_id
	 *            the department_id to set
	 */
	public void setDepartment_id(String department_id) {
		this.department_id = department_id;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the fullyPAID
	 */
	public String getFullyPAID() {
		return fullyPAID;
	}

	/**
	 * @param fullyPAID
	 *            the fullyPAID to set
	 */
	public void setFullyPAID(String fullyPAID) {
		this.fullyPAID = fullyPAID;
	}

	/**
	 * @return the gid
	 */
	public long getGid() {
		return gid;
	}

	/**
	 * @param gid
	 *            the gid to set
	 */
	public void setGid(long gid) {
		this.gid = gid;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the make
	 */
	public String getMake() {
		return make;
	}

	/**
	 * @param make
	 *            the make to set
	 */
	public void setMake(String make) {
		this.make = make;
	}

	/**
	 * @return the model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * @param model
	 *            the model to set
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * @return the partPAY
	 */
	public String getPartPAY() {
		return partPAY;
	}

	/**
	 * @param partPAY
	 *            the partPAY to set
	 */
	public void setPartPAY(String partPAY) {
		this.partPAY = partPAY;
	}

	/**
	 * @return the posting_date
	 */
	public String getPosting_date() {
		return posting_date;
	}

	/**
	 * @param posting_date
	 *            the posting_date to set
	 */
	public void setPosting_date(String posting_date) {
		this.posting_date = posting_date;
	}

	/**
	 * @return the province
	 */
	public String getProvince() {
		return province;
	}

	/**
	 * @param province
	 *            the province to set
	 */
	public void setProvince(String province) {
		this.province = province;
	}

	/**
	 * @return the raise_entry
	 */
	public String getRaise_entry() {
		return raise_entry;
	}

	/**
	 * @param raise_entry
	 *            the raise_entry to set
	 */
	public void setRaise_entry(String raise_entry) {
		this.raise_entry = raise_entry;
	}

	/**
	 * @return the registration_no
	 */
	public String getRegistration_no() {
		return registration_no;
	}

	/**
	 * @param registration_no
	 *            the registration_no to set
	 */
	public void setRegistration_no(String registration_no) {
		this.registration_no = registration_no;
	}

	/**
	 * @return the section_id
	 */
	public String getSection_id() {
		return section_id;
	}

	/**
	 * @param section_id
	 *            the section_id to set
	 */
	public void setSection_id(String section_id) {
		this.section_id = section_id;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the subject_to_vat
	 */
	public String getSubject_to_vat() {
		return subject_to_vat;
	}

	/**
	 * @param subject_to_vat
	 *            the subject_to_vat to set
	 */
	public void setSubject_to_vat(String subject_to_vat) {
		this.subject_to_vat = subject_to_vat;
	}

	/**
	 * @return the supplied_by
	 */
	public String getSupplied_by() {
		return supplied_by;
	}

	/**
	 * @param supplied_by
	 *            the supplied_by to set
	 */
	public void setSupplied_by(String supplied_by) {
		this.supplied_by = supplied_by;
	}

	/**
	 * @return the vat_amount
	 */
	public String getVat_amount() {
		return vat_amount;
	}

	/**
	 * @param vat_amount
	 *            the vat_amount to set
	 */
	public void setVat_amount(String vat_amount) {
		this.vat_amount = vat_amount;
	}

	/**
	 * @return the vendor_account
	 */
	public String getVendor_account() {
		return vendor_account;
	}

	/**
	 * @param vendor_account
	 *            the vendor_account to set
	 */
	public void setVendor_account(String vendor_account) {
		this.vendor_account = vendor_account;
	}

	/**
	 * @return the wh_tax_amount
	 */
	public String getWh_tax_amount() {
		return wh_tax_amount;
	}

	/**
	 * @param wh_tax_amount
	 *            the wh_tax_amount to set
	 */
	public void setWh_tax_amount(String wh_tax_amount) {
		this.wh_tax_amount = wh_tax_amount;
	}

	/**
	 * @return the wh_tax_cb
	 */
	public String getWh_tax_cb() {
		return wh_tax_cb;
	}

	/**
	 * @param wh_tax_cb
	 *            the wh_tax_cb to set
	 */
	public void setWh_tax_cb(String wh_tax_cb) {
		this.wh_tax_cb = wh_tax_cb;
	}

	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the vatable_cost
	 */
	public String getVatable_cost() {
		return vatable_cost;
	}

	/**
	 * @param vatable_cost the vatable_cost to set
	 */
	public void setVatable_cost(String vatable_cost) {
		this.vatable_cost = vatable_cost;
	}

	/**
	 * @return the user_id
	 */
	public String getUser_id() {
		return user_id;
	}

	/**
	 * @param user_id the user_id to set
	 */
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

}
