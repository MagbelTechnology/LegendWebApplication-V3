package com.magbel.legend.vao;

import java.util.Date;

public class Approval 
{
	private String id;
	private String description;
	private String tranType;
	private String page;
	private String flag;
	private String partPay;
	private String userId;
	private String Branch;
	private String subjectToVat;
	private String whTax;
	private String operation1;
	private String exitPage;
	private String url;
    private String assetCreator;
    private int tranID;
    private int assetCode;
    private String posting_date;
    private String approved_date;
    private double costPrice;

	public Approval(String id, String description, String page, String flag, String partPay, String userId, String branch, String subjectToVat, String whTax, String operation1, String exitPage, String url) {
		super();
		this.id = id;
		this.description = description;
		this.page = page;
		this.flag = flag;
		this.partPay = partPay;
		this.userId = userId;
		Branch = branch;
		this.subjectToVat = subjectToVat;
		this.whTax = whTax;
		this.operation1 = operation1;
		this.exitPage = exitPage;
		this.url = url;
	}
	public Approval(String id, String tranType, String description) {
		super();
		this.id = id;
		this.tranType = tranType;
		this.description = description;
	}
		
	public String getBranch() {
		return Branch;
	}
	public void setBranch(String branch) {
		Branch = branch;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTranType() {
		return tranType;
	}
	public void setTranType(String tranType) {
		this.tranType = tranType;
	}
	public String getExitPage() {
		return exitPage;
	}
	public void setExitPage(String exitPage) {
		this.exitPage = exitPage;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOperation1() {
		return operation1;
	}
	public void setOperation1(String operation1) {
		this.operation1 = operation1;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getPartPay() {
		return partPay;
	}
	public void setPartPay(String partPay) {
		this.partPay = partPay;
	}
	public String getSubjectToVat() {
		return subjectToVat;
	}
	public void setSubjectToVat(String subjectToVat) {
		this.subjectToVat = subjectToVat;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getWhTax() {
		return whTax;
	}
	public void setWhTax(String whTax) {
		this.whTax = whTax;
	}

    /**
     * @return the assetCreator
     */
    public String getAssetCreator() {
        return assetCreator;
    }

    /**
     * @param assetCreator the assetCreator to set
     */
    public void setAssetCreator(String assetCreator) {
        this.assetCreator = assetCreator;
    }

    /**
     * @return the tranID
     */
    public int getTranID() {
        return tranID;
    }

    /**
     * @param tranID the tranID to set
     */
    public void setTranID(int tranID) {
        this.tranID = tranID;
    }

    /**
     * @return the assetCode
     */
    public int getAssetCode() {
        return assetCode;
    }

    /**
     * @param assetCode the assetCode to set
     */
    public void setAssetCode(int assetCode) {
        this.assetCode = assetCode; 
    }

	public String getPosting_date() {
		return posting_date;
	}
	public void setPosting_date(String posting_date) {
		this.posting_date = posting_date;
	}

	public String getApproved_date() {
		return approved_date;
	}
	public void setApproved_date(String approved_date) {
		this.approved_date = approved_date;
	}

	public Double getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(Double costPrice) {
		this.costPrice = costPrice;
	}
}
