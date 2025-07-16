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
public class PaymentCategory {

	private String mtid;

	private String companyCode;

	private String schoolcode;

	private String itemcode;

	private String status;

	private String itemtype;

	private String description;

	private double amount;

	private String salesacct;

	private String incomesuspenseacct;
	
	private double dicountpercent;

	private double discountamt;
	
	private String username;
	
	private String create_date;
	
	private int userid;

	private int priority;
	
	private String parentid;
	
	private String adminNo;
	
    private String classes;
    
    private String branchbalance;
	/**
	 * 
	 */

	public PaymentCategory() {
		// TODO Auto-generated constructor stub
	}

	public PaymentCategory(String mtid, String schoolcode, String itemcode,
			String status, String itemtype, String description,
			double amount, String salesacct, double dicountpercent, double discountamt,
			String username, String create_date, String companyCode,int userid,String incomesuspenseacct) {

		this.mtid = mtid;
		this.schoolcode = schoolcode;
		this.itemcode = itemcode;
		this.status =status;
		this.itemtype = itemtype;
		this.description = description;
		this.amount = amount;
		this.salesacct = salesacct;
		this.incomesuspenseacct = incomesuspenseacct;
		this.dicountpercent = dicountpercent;
		this.discountamt=discountamt;
		this.username = username;
		this.create_date = create_date;
		this.companyCode = companyCode;
		this.userid = userid;
	}
	public PaymentCategory(String mtid, String schoolcode, String parentid,
			String status, String itemtype, String description,
			double amount, String salesacct, double dicountpercent, double discountamt,
			String create_date, String companyCode,int userid, int priority,String incomesuspenseacct) {

		this.mtid = mtid;
		this.schoolcode = schoolcode;
		this.parentid = parentid;
		this.status =status;
		this.itemtype = itemtype;
		this.description = description;
		this.amount = amount;
		this.salesacct = salesacct;
		this.incomesuspenseacct = incomesuspenseacct;
		this.dicountpercent = dicountpercent;
		this.discountamt=discountamt;
		this.username = username;
		this.create_date = create_date;
		this.companyCode = companyCode;
		this.userid = userid;
		this.priority = priority;
	}

	/**
	 * @param branchId
	 *            the branchId to set
	 */
	public void setMtid(String mtid) {
		this.mtid = mtid;
	}

	/**
	 * @return the branchId
	 */
	public String getMtid() {
		return mtid;
	}

	/**
	 * @param schoolcode
	 *            the schoolcode to set
	 */
	public void setSchoolcode(String schoolcode) {
		this.schoolcode = schoolcode;
	}

	/**
	 * @return the schoolcode
	 */
	public String getSchoolcode() {
		return schoolcode;
	}

	/**
	 * @param itemcode
	 *            the itemcode to set
	 */
	public void setItemcode(String itemcode) {
		this.itemcode = itemcode;
	}

	/**
	 * @return the itemcode
	 */
	public String getItemcode() {
		return itemcode;
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
	 * @param itemtype
	 *            the itemtype to set
	 */
	public void setItemtype(String itemtype) {
		this.itemtype = itemtype;
	}

	/**
	 * @return the glPrefix
	 */
	public String getItemtype() {
		return itemtype;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}

	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @param salesacct
	 *            the salesacct to set
	 */
	public void setSalesacct(String salesacct) {
		this.salesacct = salesacct;
	}

	/**
	 * @return the salesacct
	 */
	public String getSalesacct() {
		return salesacct;
	}
	
	/**
	 * @param incomesuspenseacct
	 *            the incomesuspenseacct to set
	 */
	public void setIncomesuspenseacct(String incomesuspenseacct) {
		this.incomesuspenseacct = incomesuspenseacct;
	}

	/**
	 * @return the incomesuspenseacct
	 */
	public String getIncomesuspenseacct() {
		return incomesuspenseacct;
	}
	
	/**
	 * @param dicountpercent
	 *            the dicountpercent to set
	 */
	public void setDicountpercent(double dicountpercent) {
		this.dicountpercent = dicountpercent;
	}

	/**
	 * @return the dicountpercent
	 */
	public double getDicountpercent() {
		return dicountpercent;
	}

	/**
	 * @param discountamt
	 *            the discountamt to set
	 */
	public void setDiscountamt(double discountamt) {
		this.discountamt = discountamt;
	}

	/**
	 * @return the discountamt
	 */
	public double getDiscountamt() {
		return discountamt;
	}

	/**
	 * @param userid
	 *            the userid to set
	 */
	public void setUserid(int userid) {
		this.userid = userid;
	}

	/**
	 * @return the userid
	 */
	public int getUserid() {
		return userid;
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
    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
	public void setParentid(String parentid){
	this.parentid=parentid;
	} 	
	public String getParentid(){
	return parentid;
	}
	public void setAdminNo(String adminNo){
	this.adminNo=adminNo;
	} 	
	public String getAdminNo(){
	return adminNo;
	}
	public void setClasses(String classes){
	this.classes=classes;
	} 	
	public String getClasses(){
	return classes;
	}	
	public void setBranchbalance(String branchbalance){
		this.branchbalance=branchbalance;
		} 	
		public String getBranchbalance(){
		return branchbalance;
		}	
		
}
