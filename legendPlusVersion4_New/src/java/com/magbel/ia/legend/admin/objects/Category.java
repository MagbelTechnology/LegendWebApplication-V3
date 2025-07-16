package com.magbel.ia.legend.admin.objects;

public class Category {
	private String categoryId;

	private String categoryCode;

	private String categoryName;

	private String categoryAcronym;

	private String requiredforFleet;

	private String categoryClass;

	private String pmCyclePeriod;

	private String mileage;

	private String notifyMaintdays;

	private String notifyEveryDays;

	private String residualValue;

	private String depRate;

	private String assetLedger;

	private String depLedger;

	private String accumDepLedger;

	private String glAccount;

	private String insuranceAcct;

	private String licenseLedger;

	private String fuelLedger;

	private String accidentLedger;

	private String categoryStatus;

	private String userId;

	private String createDate;

	private String acctType;

	private String currencyId;

        private String enforceBarcode;

        private String categoryType;

	public Category() {

	}
        

	public Category(String categoryId, String categoryCode,
			String categoryName, String categoryAcronym,
			String requiredforFleet, String categoryClass,
			String pmCyclePeriod, String mileage, String notifyMaintdays,
			String notifyEveryDays, String residualValue, String depRate,
			String assetLedger, String depLedger, String accumDepLedger,
			String glAccount, String insuranceAcct, String licenseLedger,
			String fuelLedger, String accidentLedger, String categoryStatus,
			String userId, String createDate, String acctType, String currencyId) {

		this.categoryId = categoryId;
		this.categoryCode = categoryCode;

		this.categoryName = categoryName;
		this.categoryAcronym = categoryAcronym;
		this.requiredforFleet = requiredforFleet;
		this.categoryClass = categoryClass;
		this.pmCyclePeriod = pmCyclePeriod;
		this.mileage = mileage;
		this.notifyMaintdays = notifyMaintdays;
		this.notifyEveryDays = notifyEveryDays;
		this.residualValue = residualValue;
		this.depRate = depRate;
		this.assetLedger = assetLedger;
		this.depLedger = depLedger;
		this.accumDepLedger = accumDepLedger;
		this.glAccount = glAccount;
		this.insuranceAcct = insuranceAcct;
		this.licenseLedger = licenseLedger;
		this.fuelLedger = fuelLedger;
		this.accidentLedger = accidentLedger;
		this.categoryStatus = categoryStatus;
		this.userId = userId;
		this.createDate = createDate;
		this.acctType = acctType;
		this.currencyId = currencyId;

	}

    public Category(String categoryId, String categoryCode, String categoryName, String categoryAcronym,
            String requiredforFleet, String categoryClass, String pmCyclePeriod, String mileage,
            String notifyMaintdays, String notifyEveryDays, String residualValue, String depRate,
            String assetLedger, String depLedger, String accumDepLedger, String glAccount,
            String insuranceAcct, String licenseLedger, String fuelLedger, String accidentLedger,
            String categoryStatus, String userId, String createDate, String acctType, String currencyId,
            String enforceBarcode)
    {
            this.categoryId = categoryId;
		this.categoryCode = categoryCode;

		this.categoryName = categoryName;
		this.categoryAcronym = categoryAcronym;
		this.requiredforFleet = requiredforFleet;
		this.categoryClass = categoryClass;
		this.pmCyclePeriod = pmCyclePeriod;
		this.mileage = mileage;
		this.notifyMaintdays = notifyMaintdays;
		this.notifyEveryDays = notifyEveryDays;
		this.residualValue = residualValue;
		this.depRate = depRate;
		this.assetLedger = assetLedger;
		this.depLedger = depLedger;
		this.accumDepLedger = accumDepLedger;
		this.glAccount = glAccount;
		this.insuranceAcct = insuranceAcct;
		this.licenseLedger = licenseLedger;
		this.fuelLedger = fuelLedger;
		this.accidentLedger = accidentLedger;
		this.categoryStatus = categoryStatus;
		this.userId = userId;
		this.createDate = createDate;
		this.acctType = acctType;
		this.currencyId = currencyId;
                this.enforceBarcode=enforceBarcode;
    }
public Category(String categoryId, String categoryCode,
			String categoryName, String categoryAcronym,
			String requiredforFleet, String categoryClass,
			String pmCyclePeriod, String mileage, String notifyMaintdays,
			String notifyEveryDays, String residualValue, String depRate,
			String assetLedger, String depLedger, String accumDepLedger,
			String glAccount, String insuranceAcct, String licenseLedger,
			String fuelLedger, String accidentLedger, String categoryStatus,
			String userId, String createDate, String acctType,
			String currencyId, String enforceBarcode, String categoryType) {
		super();
		this.categoryId = categoryId;
		this.categoryCode = categoryCode;
		this.categoryName = categoryName;
		this.categoryAcronym = categoryAcronym;
		this.requiredforFleet = requiredforFleet;
		this.categoryClass = categoryClass;
		this.pmCyclePeriod = pmCyclePeriod;
		this.mileage = mileage;
		this.notifyMaintdays = notifyMaintdays;
		this.notifyEveryDays = notifyEveryDays;
		this.residualValue = residualValue;
		this.depRate = depRate;
		this.assetLedger = assetLedger;
		this.depLedger = depLedger;
		this.accumDepLedger = accumDepLedger;
		this.glAccount = glAccount;
		this.insuranceAcct = insuranceAcct;
		this.licenseLedger = licenseLedger;
		this.fuelLedger = fuelLedger;
		this.accidentLedger = accidentLedger;
		this.categoryStatus = categoryStatus;
		this.userId = userId;
		this.createDate = createDate;
		this.acctType = acctType;
		this.currencyId = currencyId;
		this.enforceBarcode = enforceBarcode;
		this.categoryType = categoryType;
	}
        public String getCategoryType() {
		return categoryType;
	}


	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}


	/**
	 * @param categoryId
	 *            the categoryId to set
	 */
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * @return the categoryId
	 */
	public String getCategoryId() {
		return categoryId;
	}

	/**
	 * @param categoryCode
	 *            the categoryCode to set
	 */
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	/**
	 * @return the categoryCode
	 */
	public String getCategoryCode() {
		return categoryCode;
	}

	/**
	 * @param categoryName
	 *            the categoryName to set
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	/**
	 * @return the categoryName
	 */
	public String getCategoryName() {
		return categoryName;
	}

	/**
	 * @param categoryAcronym
	 *            the categoryAcronym to set
	 */
	public void setCategoryAcronym(String categoryAcronym) {
		this.categoryAcronym = categoryAcronym;
	}

	/**
	 * @return the categoryAcronym
	 */
	public String getCategoryAcronym() {
		return categoryAcronym;
	}

	/**
	 * @param requiredforFleet
	 *            the requiredforFleet to set
	 */
	public void setRequiredforFleet(String requiredforFleet) {
		this.requiredforFleet = requiredforFleet;
	}

	/**
	 * @return the requiredforFleet
	 */
	public String getRequiredforFleet() {
		return requiredforFleet;
	}

	/**
	 * @param categoryClass
	 *            the categoryClass to set
	 */
	public void setCategoryClass(String categoryClass) {
		this.categoryClass = categoryClass;
	}

	/**
	 * @return the categoryClass
	 */
	public String getCategoryClass() {
		return categoryClass;
	}

	/**
	 * @param pmCyclePeriod
	 *            the pmCyclePeriod to set
	 */
	public void setPmCyclePeriod(String pmCyclePeriod) {
		this.pmCyclePeriod = pmCyclePeriod;
	}

	/**
	 * @return the pmCyclePeriod
	 */
	public String getPmCyclePeriod() {
		return pmCyclePeriod;
	}

	/**
	 * @param mileage
	 *            the mileage to set
	 */
	public void setMileage(String mileage) {
		this.mileage = mileage;
	}

	/**
	 * @return the mileage
	 */
	public String getMileage() {
		return mileage;
	}

	/**
	 * @param notifyMaintdays
	 *            the notifyMaintdays to set
	 */
	public void setNotifyMaintdays(String notifyMaintdays) {
		this.notifyMaintdays = notifyMaintdays;
	}

	/**
	 * @return the notifyMaintdays
	 */
	public String getNotifyMaintdays() {
		return notifyMaintdays;
	}

	/**
	 * @param notifyEveryDays
	 *            the notifyEveryDays to set
	 */
	public void setNotifyEveryDays(String notifyEveryDays) {
		this.notifyEveryDays = notifyEveryDays;
	}

	/**
	 * @return the notifyEveryDays
	 */
	public String getNotifyEveryDays() {
		return notifyEveryDays;
	}

	/**
	 * @param residualValue
	 *            the residualValue to set
	 */
	public void setResidualValue(String residualValue) {
		this.residualValue = residualValue;
	}

	/**
	 * @return the residualValue
	 */
	public String getResidualValue() {
		return residualValue;
	}

	/**
	 * @param depRate
	 *            the depRate to set
	 */
	public void setDepRate(String depRate) {
		this.depRate = depRate;
	}

	/**
	 * @return the depRate
	 */
	public String getDepRate() {
		return depRate;
	}

	/**
	 * @param assetLedger
	 *            the assetLedger to set
	 */
	public void setAssetLedger(String assetLedger) {
		this.assetLedger = assetLedger;
	}

	/**
	 * @return the assetLedger
	 */
	public String getAssetLedger() {
		return assetLedger;
	}

	/**
	 * @param depLedger
	 *            the depLedger to set
	 */
	public void setDepLedger(String depLedger) {
		this.depLedger = depLedger;
	}

	/**
	 * @return the depLedger
	 */
	public String getDepLedger() {
		return depLedger;
	}

	/**
	 * @param accumDepLedger
	 *            the accumDepLedger to set
	 */
	public void setAccumDepLedger(String accumDepLedger) {
		this.accumDepLedger = accumDepLedger;
	}

	/**
	 * @return the accumDepLedger
	 */
	public String getAccumDepLedger() {
		return accumDepLedger;
	}

	/**
	 * @param glAccount
	 *            the glAccount to set
	 */
	public void setGlAccount(String glAccount) {
		this.glAccount = glAccount;
	}

	/**
	 * @return the glAccount
	 */
	public String getGlAccount() {
		return glAccount;
	}

	/**
	 * @param insuranceAcct
	 *            the insuranceAcct to set
	 */
	public void setInsuranceAcct(String insuranceAcct) {
		this.insuranceAcct = insuranceAcct;
	}

	/**
	 * @return the insuranceAcct
	 */
	public String getInsuranceAcct() {
		return insuranceAcct;
	}

	/**
	 * @param licenseLedger
	 *            the licenseLedger to set
	 */
	public void setLicenseLedger(String licenseLedger) {
		this.licenseLedger = licenseLedger;
	}

	/**
	 * @return the licenseLedger
	 */
	public String getLicenseLedger() {
		return licenseLedger;
	}

	/**
	 * @param fuelLedger
	 *            the fuelLedger to set
	 */
	public void setFuelLedger(String fuelLedger) {
		this.fuelLedger = fuelLedger;
	}

	/**
	 * @return the fuelLedger
	 */
	public String getFuelLedger() {
		return fuelLedger;
	}

	/**
	 * @param accidentLedger
	 *            the accidentLedger to set
	 */
	public void setAccidentLedger(String accidentLedger) {
		this.accidentLedger = accidentLedger;
	}

	/**
	 * @return the accidentLedger
	 */
	public String getAccidentLedger() {
		return accidentLedger;
	}

	/**
	 * @param categoryStatus
	 *            the categoryStatus to set
	 */
	public void setCategoryStatus(String categoryStatus) {
		this.categoryStatus = categoryStatus;
	}

	/**
	 * @return the categoryStatus
	 */
	public String getCategoryStatus() {
		return categoryStatus;
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

	/**
	 * @param acctType
	 *            the acctType to set
	 */
	public void setAcctType(String acctType) {
		this.acctType = acctType;
	}

	/**
	 * @return the acctType
	 */
	public String getAcctType() {
		return acctType;
	}

	/**
	 * @param currencyId
	 *            the currencyId to set
	 */
	public void setCurrencyId(String currencyId) {
		this.currencyId = currencyId;
	}

    public String getEnforceBarcode() {
        return enforceBarcode;
    }

    

    public void setEnforceBarcode(String enforceBarcode) {
        this.enforceBarcode = enforceBarcode;
    }

	/**
	 * @return the currencyId
	 */
	public String getCurrencyId() {
		return currencyId;
	}

}
