package com.magbel.legend.vao;

public class ViewVendorTransactionDetails 
{
	private String transId ;     
	private String vendorId ;  
	private String description;                                                                                                                                                                                                                                                     
	private String costPrice;                                       
	private String transactionDt;  
	private String transactionType ;      
	private String draccountNo;
	private String craccountNo;
	private String location;
	private String projectCode;
	private int userid;
	public ViewVendorTransactionDetails() {
		super();
	}
	public ViewVendorTransactionDetails(String transId, String description,
			String costPrice, String transactionDt, String transactionType,
			String draccountNo, String craccountNo, String vendorId, String location, String projectCode) {
		
		super();
		this.transId = transId;
		this.description = description;
		this.costPrice = costPrice;
		this.transactionDt = transactionDt;
		this.transactionType = transactionType;
		this.draccountNo = draccountNo;
		this.craccountNo = craccountNo;
		this.location = location;
		this.vendorId = vendorId;
		this.projectCode = projectCode;
	}
	public ViewVendorTransactionDetails(String transId, String description,
			String costPrice, String transactionDt, String transactionType,
			String draccountNo, String craccountNo, int userid, String vendorId, String location) {
		super();
		this.transId = transId;
		this.description = description;
		this.costPrice = costPrice;
		this.transactionDt = transactionDt;
		this.transactionType = transactionType;
		this.draccountNo = draccountNo;
		this.craccountNo = craccountNo;
		this.userid = userid;
		this.location = location;
		this.vendorId = vendorId;
	}	
	public String getTransId() {
		return transId;
	}
	public void setTransId(String transId) {
		this.transId = transId;
	}
	public String getVendorId() {
		return vendorId;
	}
	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(String costPrice) {
		this.costPrice = costPrice;
	}
	public String getTransactionDt() {
		return transactionDt;
	}
	public void setTransactionDt(String transactionDt) {
		this.transactionDt = transactionDt;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getCraccountNo() {
		return craccountNo;
	}
	public void setCraccountNo(String craccountNo) {
		this.craccountNo = craccountNo;
	}
	public String getDraccountNo() {
		return draccountNo;
	}
	public void setDraccountNo(String draccountNo) {
		this.draccountNo = draccountNo;
	}	
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}	
	
}
