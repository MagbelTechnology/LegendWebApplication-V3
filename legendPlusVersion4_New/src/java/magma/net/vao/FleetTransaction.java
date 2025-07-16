package magma.net.vao;

import java.io.Serializable;

/**
 * <p>Title: fileName.java</p>
 *
 * <p>Description: File Description</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Magbel Technologies LTD</p>
 *
 * @author Jejelowo.B.Festus
 * @version 1.0
 */
public class FleetTransaction implements Serializable {

    private String assetId;
    private String registrationNo;
    private String branchId;
    private String deptId;
    private String category;
    private String datePurchased;
    private String assetMake;
    private String assetUser;
    private String assetMaintenance;
    private String createDate;
    private String effectiveDate;
    private String location;
    private boolean entryRaised;
    private double depreciation;
    private double premiumLiveToDate;
    private double premimumPeriodToDate;
    private double maintLiveToDate;
    private double maintPeriodToDate;
    private double fuelLiveToDate;
    private double fuelPeriodToDate;
    private int accidentCount;
    private double accidentCostLiveToDate;
    private double accidentCostPeriodToDate;
    private double licencePermitLiveToDate;
    private double licencePermitPeriodToDate;
    private double insuranceLiveToDate;
    private double insurancePremiumToDate;

    private String lastUpdateDate;
    private String status;
    private String userId;
    private String vendorId;

    public FleetTransaction(String assetId, String registrationNo,
                            String branchId, String deptId, String category,
                            String datePurchased, String assetMake,
                            String assetUser, String assetMaintenance,
                            String createDate, String effectiveDate,
                            String location, boolean entryRaised,
                            double depreciation, double premiumLiveToDate,
                            double premimumPeriodToDate, double maintLiveToDate,
                            double maintPeriodToDate, double fuelLiveToDate,
                            double fuelPeriodToDate, int accidentCount,
                            double accidentCostLiveToDate,
                            double accidentCostPeriodToDate,
                            double licencePermitLiveToDate,
                            double licencePermitPeriodToDate,
                            String lastUpdateDate, String status,
                            String userId) {

        setAssetId(assetId);
        setRegistrationNo(registrationNo);
        setBranchId(branchId);
        setDeptId(deptId);
        setCategory(category);
        setDatePurchased(datePurchased);
        setAssetMake(assetMake);
        setAssetUser(assetUser);
        setAssetMaintenance(assetMaintenance);
        setCreateDate(createDate);
        setEffectiveDate(effectiveDate);
        setLocation(location);
        setEntryRaised(entryRaised);
        setDepreciation(depreciation);
        setPremiumLiveToDate(premiumLiveToDate);
        setPremimumPeriodToDate(premimumPeriodToDate);
        setMaintLiveToDate(maintLiveToDate);
        setMaintPeriodToDate(maintPeriodToDate);
        setFuelLiveToDate(fuelLiveToDate);
        setFuelPeriodToDate(fuelPeriodToDate);
        setAccidentCount(accidentCount);
        setAccidentCostLiveToDate(accidentCostLiveToDate);
        setAccidentCostPeriodToDate(accidentCostPeriodToDate);
        setLicencePermitLiveToDate(licencePermitLiveToDate);
        setLicencePermitPeriodToDate(licencePermitPeriodToDate);
        setLastUpdateDate(lastUpdateDate);
        setStatus(status);
        setUserId(userId);

    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDatePurchased(String datePurchased) {
        this.datePurchased = datePurchased;
    }

    public void setAssetMake(String assetMake) {
        this.assetMake = assetMake;
    }

    public void setAssetUser(String assetUser) {
        this.assetUser = assetUser;
    }

    public void setAssetMaintenance(String assetMaintenance) {
        this.assetMaintenance = assetMaintenance;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setEntryRaised(boolean entryRaised) {
        this.entryRaised = entryRaised;
    }

    public void setDepreciation(double depreciation) {
        this.depreciation = depreciation;
    }

    public void setPremiumLiveToDate(double premiumLiveToDate) {
        this.premiumLiveToDate = premiumLiveToDate;
    }

    public void setPremimumPeriodToDate(double premimumPeriodToDate) {
        this.premimumPeriodToDate = premimumPeriodToDate;
    }

    public void setMaintLiveToDate(double maintLiveToDate) {
        this.maintLiveToDate = maintLiveToDate;
    }

    public void setMaintPeriodToDate(double maintPeriodToDate) {
        this.maintPeriodToDate = maintPeriodToDate;
    }

    public void setFuelLiveToDate(double fuelLiveToDate) {
        this.fuelLiveToDate = fuelLiveToDate;
    }

    public void setFuelPeriodToDate(double fuelPeriodToDate) {
        this.fuelPeriodToDate = fuelPeriodToDate;
    }

    public void setAccidentCount(int accidentCount) {
        this.accidentCount = accidentCount;
    }

    public void setAccidentCostLiveToDate(double accidentCostLiveToDate) {
        this.accidentCostLiveToDate = accidentCostLiveToDate;
    }

    public void setAccidentCostPeriodToDate(double accidentCostPeriodToDate) {
        this.accidentCostPeriodToDate = accidentCostPeriodToDate;
    }

    public void setLicencePermitLiveToDate(double licencePermitLiveToDate) {
        this.licencePermitLiveToDate = licencePermitLiveToDate;
    }

    public void setLicencePermitPeriodToDate(double licencePermitPeriodToDate) {
        this.licencePermitPeriodToDate = licencePermitPeriodToDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }    

    public void setInsuranceLiveToDate(double insuranceLiveToDate) {
        this.insuranceLiveToDate = insuranceLiveToDate;
    }

    public void setInsurancePremiumToDate(double insurancePremiumToDate) {
        this.insurancePremiumToDate = insurancePremiumToDate;
    }

    public String getAssetId() {
        return assetId;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public String getBranchId() {
        return branchId;
    }

    public String getDeptId() {
        return deptId;
    }

    public String getCategory() {
        return category;
    }

    public String getDatePurchased() {
        return datePurchased;
    }

    public String getAssetMake() {
        return assetMake;
    }

    public String getAssetUser() {
        return assetUser;
    }

    public String getAssetMaintenance() {
        return assetMaintenance;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public String getLocation() {
        return location;
    }

    public boolean isEntryRaised() {
        return entryRaised;
    }

    public double getDepreciation() {
        return depreciation;
    }

    public double getPremiumLiveToDate() {
        return premiumLiveToDate;
    }

    public double getPremimumPeriodToDate() {
        return premimumPeriodToDate;
    }

    public double getMaintLiveToDate() {
        return maintLiveToDate;
    }

    public double getMaintPeriodToDate() {
        return maintPeriodToDate;
    }

    public double getFuelLiveToDate() {
        return fuelLiveToDate;
    }

    public double getFuelPeriodToDate() {
        return fuelPeriodToDate;
    }

    public int getAccidentCount() {
        return accidentCount;
    }

    public double getAccidentCostLiveToDate() {
        return accidentCostLiveToDate;
    }

    public double getAccidentCostPeriodToDate() {
        return accidentCostPeriodToDate;
    }

    public double getLicencePermitLiveToDate() {
        return licencePermitLiveToDate;
    }

    public double getLicencePermitPeriodToDate() {
        return licencePermitPeriodToDate;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public String getStatus() {
        return status;
    }

    public String getUserId() {
        return userId;
    }

    public String getVendorId() {
        return vendorId;
    }

    public double getInsuranceLiveToDate() {
        return insuranceLiveToDate;
    }

    public double getInsurancePremiumToDate() {
        return insurancePremiumToDate;
    }
}
