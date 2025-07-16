package com.magbel.ia.vao;
/**
 * <p>Title: GLPosting.java</p>
 *
 * <p>Description: File Description</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: Magbel Technologies LTD</p>
 *
 * @author Bolanle M. Sule
 * @version 1.0
 */
public class GLPosting {

	private String id;
	private String crType;
	private String crDistribution;
    private String crAccount;
	private String crDescription;
	private String crBusinessUnit;
	private String crTranCode;
	private String crCurrency;
    private double crAmount  =  0.00d;
	private String crReference;
	private String crReversalDate;
	private String drType;
	private String drDistribution;
    private String drAccount;
	private String drDescription;
	private String drBusinessUnit;
	private String drTranCode;
	private String drCurrency;
    private  double	drAmount  =  0.00d;
	private String drReference;
	private String drReversalDate;
	private double	sellRate   =  0.00d;
	private double	buyRate   =  0.00d;
	private String effectiveDate;
	private String status;
    

    public GLPosting(String id, String crType, String crDistribution, String crAccount, String crDescription, 
	                 String crBusinessUnit, String crTranCode, String crCurrency, double crAmount, 
					 String crReference, String crReversalDate, String drType, String drDistribution,
					 String drAccount, String drDescription, String drBusinessUnit, String drTranCode, 
					 String drCurrency, double drAmount, String drReference, String drReversalDate, 
					 double sellRate, double buyRate, String effectiveDate, String status) {
					 super();

		this.id = id;
		this.crType = crType;
		this.crDistribution = crDistribution;
		this.crAccount = crAccount;
		this.crDescription = crDescription;
		this.crBusinessUnit = crBusinessUnit;
		this.crTranCode = crTranCode;
		this.crCurrency = crCurrency;
		this.crAmount = crAmount;
		this.crReference = crReference;
		this.crReversalDate = crReversalDate;
		this.drType = drType;
		this.drDistribution = drDistribution;
		this.drAccount = drAccount;
		this.drDescription = drDescription;
		this.drBusinessUnit = drBusinessUnit;
		this.drTranCode = drTranCode;
		this.drCurrency = drCurrency;
		this.drAmount = drAmount;
		this.drReference = drReference;
		this.drReversalDate = drReversalDate;
		this.sellRate = sellRate;
		this.buyRate = buyRate;
		this.effectiveDate = effectiveDate;
		this.status = status;
    			
    }

	/**
	 * @return the buyRate
	 */
	public double getBuyRate() {
		return buyRate;
	}

	/**
	 * @param buyRate the buyRate to set
	 */
	public void setBuyRate(double buyRate) {
		this.buyRate = buyRate;
	}

	/**
	 * @return the crAccount
	 */
	public String getCrAccount() {
		return crAccount;
	}

	/**
	 * @param crAccount the crAccount to set
	 */
	public void setCrAccount(String crAccount) {
		this.crAccount = crAccount;
	}

	/**
	 * @return the crAmount
	 */
	public double getCrAmount() {
		return crAmount;
	}

	/**
	 * @param crAmount the crAmount to set
	 */
	public void setCrAmount(double crAmount) {
		this.crAmount = crAmount;
	}

	/**
	 * @return the crBusinessUnit
	 */
	public String getCrBusinessUnit() {
		return crBusinessUnit;
	}

	/**
	 * @param crBusinessUnit the crBusinessUnit to set
	 */
	public void setCrBusinessUnit(String crBusinessUnit) {
		this.crBusinessUnit = crBusinessUnit;
	}

	/**
	 * @return the crCurrency
	 */
	public String getCrCurrency() {
		return crCurrency;
	}

	/**
	 * @param crCurrency the crCurrency to set
	 */
	public void setCrCurrency(String crCurrency) {
		this.crCurrency = crCurrency;
	}

	/**
	 * @return the crDescription
	 */
	public String getCrDescription() {
		return crDescription;
	}

	/**
	 * @param crDescription the crDescription to set
	 */
	public void setCrDescription(String crDescription) {
		this.crDescription = crDescription;
	}

	/**
	 * @return the crDistribution
	 */
	public String getCrDistribution() {
		return crDistribution;
	}

	/**
	 * @param crDistribution the crDistribution to set
	 */
	public void setCrDistribution(String crDistribution) {
		this.crDistribution = crDistribution;
	}

	/**
	 * @return the crReference
	 */
	public String getCrReference() {
		return crReference;
	}

	/**
	 * @param crReference the crReference to set
	 */
	public void setCrReference(String crReference) {
		this.crReference = crReference;
	}

	/**
	 * @return the crReversalDate
	 */
	public String getCrReversalDate() {
		return crReversalDate;
	}

	/**
	 * @param crReversalDate the crReversalDate to set
	 */
	public void setCrReversalDate(String crReversalDate) {
		this.crReversalDate = crReversalDate;
	}

	/**
	 * @return the crTranCode
	 */
	public String getCrTranCode() {
		return crTranCode;
	}

	/**
	 * @param crTranCode the crTranCode to set
	 */
	public void setCrTranCode(String crTranCode) {
		this.crTranCode = crTranCode;
	}

	/**
	 * @return the crType
	 */
	public String getCrType() {
		return crType;
	}

	/**
	 * @param crType the crType to set
	 */
	public void setCrType(String crType) {
		this.crType = crType;
	}

	/**
	 * @return the drAccount
	 */
	public String getDrAccount() {
		return drAccount;
	}

	/**
	 * @param drAccount the drAccount to set
	 */
	public void setDrAccount(String drAccount) {
		this.drAccount = drAccount;
	}

	/**
	 * @return the drAmount
	 */
	public double getDrAmount() {
		return drAmount;
	}

	/**
	 * @param drAmount the drAmount to set
	 */
	public void setDrAmount(double drAmount) {
		this.drAmount = drAmount;
	}

	/**
	 * @return the drBusinessUnit
	 */
	public String getDrBusinessUnit() {
		return drBusinessUnit;
	}

	/**
	 * @param drBusinessUnit the drBusinessUnit to set
	 */
	public void setDrBusinessUnit(String drBusinessUnit) {
		this.drBusinessUnit = drBusinessUnit;
	}

	/**
	 * @return the drCurrency
	 */
	public String getDrCurrency() {
		return drCurrency;
	}

	/**
	 * @param drCurrency the drCurrency to set
	 */
	public void setDrCurrency(String drCurrency) {
		this.drCurrency = drCurrency;
	}

	/**
	 * @return the drDescription
	 */
	public String getDrDescription() {
		return drDescription;
	}

	/**
	 * @param drDescription the drDescription to set
	 */
	public void setDrDescription(String drDescription) {
		this.drDescription = drDescription;
	}

	/**
	 * @return the drDistribution
	 */
	public String getDrDistribution() {
		return drDistribution;
	}

	/**
	 * @param drDistribution the drDistribution to set
	 */
	public void setDrDistribution(String drDistribution) {
		this.drDistribution = drDistribution;
	}

	/**
	 * @return the drReference
	 */
	public String getDrReference() {
		return drReference;
	}

	/**
	 * @param drReference the drReference to set
	 */
	public void setDrReference(String drReference) {
		this.drReference = drReference;
	}

	/**
	 * @return the drReversalDate
	 */
	public String getDrReversalDate() {
		return drReversalDate;
	}

	/**
	 * @param drReversalDate the drReversalDate to set
	 */
	public void setDrReversalDate(String drReversalDate) {
		this.drReversalDate = drReversalDate;
	}

	/**
	 * @return the drTranCode
	 */
	public String getDrTranCode() {
		return drTranCode;
	}

	/**
	 * @param drTranCode the drTranCode to set
	 */
	public void setDrTranCode(String drTranCode) {
		this.drTranCode = drTranCode;
	}

	/**
	 * @return the drType
	 */
	public String getDrType() {
		return drType;
	}

	/**
	 * @param drType the drType to set
	 */
	public void setDrType(String drType) {
		this.drType = drType;
	}

	/**
	 * @return the effectiveDate
	 */
	public String getEffectiveDate() {
		return effectiveDate;
	}

	/**
	 * @param effectiveDate the effectiveDate to set
	 */
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the sellRate
	 */
	public double getSellRate() {
		return sellRate;
	}

	/**
	 * @param sellRate the sellRate to set
	 */
	public void setSellRate(double sellRate) {
		this.sellRate = sellRate;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	} 
	
}
