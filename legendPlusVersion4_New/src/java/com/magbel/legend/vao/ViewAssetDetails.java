package com.magbel.legend.vao;

public class ViewAssetDetails 
{
	private String assetId ;                                                                                            
	private String description;                                                                                                                                                                                                                                                     
	private String costPrice;                                       
	private String transactionDt;  
	private String transactionType ;      
	private String assetCode;
	private int userid;
	private int id;
	private double depRate;
	
	public ViewAssetDetails() {
		super();
	}
	public ViewAssetDetails(String assetId, String description,
			String costPrice, String transactionDt, String transactionType,
			String assetCode) {
		super();
		this.assetId = assetId;
		this.description = description;
		this.costPrice = costPrice;
		this.transactionDt = transactionDt;
		this.transactionType = transactionType;
		this.assetCode = assetCode;
	}
	public ViewAssetDetails(String assetId, String description,
			String costPrice, String transactionDt, String transactionType,
			String assetCode, int userid) {
		super();
		this.assetId = assetId;
		this.description = description;
		this.costPrice = costPrice;
		this.transactionDt = transactionDt;
		this.transactionType = transactionType;
		this.assetCode = assetCode;
		this.userid = userid;
	}	
	public String getAssetId() {
		return assetId;
	}
	public void setAssetId(String assetId) {
		this.assetId = assetId;
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
	public String getAssetCode() {
		return assetCode;
	}
	public void setAssetCode(String assetCode) {
		this.assetCode = assetCode;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}	
	public double getDepRate() {
		return depRate;
	}
	public void setDepRate(double depRate) {
		this.depRate = depRate;
	}	
		
	
}
