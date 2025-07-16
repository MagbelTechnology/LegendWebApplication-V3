package magma.net.vao;

import java.io.Serializable;
import java.util.Date;

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
public class Asset_18_08_2010 implements Serializable {

    private String id;
    private String registrationNo;
    private String branchId;
    private String departmentId;
    private String section;
    private String category;
    private String description;
    private String datePurchased;
    private double depreciationRate;
    private String assetMake;
    private String assetUser;
    private String asset_status;
    private String assetMaintenance;
    private double accumulatedDepreciation;
    private double monthlyDepreciation;
    private double cost;
    private String depreciationEndDate;
    private double residualValue;
    private String postingDate;
    private String entryRaised;
    private double depreciationYearToDate;
    private double nbv;
    private int remainingLife;
    private int totalLife;
    private Date effectiveDate;
    private String isFleetEnabled;
    private String classifyDate;
    private String reclassify_id;
    private String vendorAc;
    private String serialNo;
    private String engineNo;
    private int supplierName;
    private int assetMaintain;
    private String authorizeBy;
    private String purchaseReason;
    private String sbuCode;
    private String barCode;
    private String spare1;
    private String spare2;



 public Asset_18_08_2010(){

    }

    public Asset_18_08_2010(String id, String registrationNo, String branchId,
                 String departmentId, String section, String category,
                 String description,
                 String datePurchased, double depreciationRate,
                 String assetMake,
                 String assetUser, String assetMaintenance,
                 double accumulatedDepreciation,
                 double monthlyDepreciation, double cost,
                 String depreciationEndDate,
                 double residualValue, String postingDate, String entryRaised,
                 double depreciationYearToDate) {

        setId(id);
        setRegistrationNo(registrationNo);
        setBranchId(branchId);
        setDepartmentId(departmentId);
        setSection(section);
        setCategory(category);
        setDescription(description);
        setDatePurchased(datePurchased);
        setDepreciationRate(depreciationRate);
        setAssetMake(assetMake);
        setAssetMaintenance(assetMaintenance);
        setAccumulatedDepreciation(accumulatedDepreciation);
        setMonthlyDepreciation(monthlyDepreciation);
        setCost(cost);
        setResidualValue(residualValue);
        setPostingDate(postingDate);
        setEntryRaised(entryRaised);
        setDepreciationYearToDate(depreciationYearToDate);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDatePurchased(String datePurchased) {
        this.datePurchased = datePurchased;
    }

    public void setDepreciationRate(double depreciationRate) {
        this.depreciationRate = depreciationRate;
    }

    public void setAssetMake(String assetMake) {
        this.assetMake = assetMake;
    }

    public void setAsset_status(String asset_status) {
        this.asset_status = asset_status;
    }

    public void setAssetUser(String assetUser) {
        this.assetUser = assetUser;
    }

    public void setAssetMaintenance(String assetMaintenance) {
        this.assetMaintenance = assetMaintenance;
    }

    public void setAccumulatedDepreciation(double accumulatedDepreciation) {
        this.accumulatedDepreciation = accumulatedDepreciation;
    }

    public void setMonthlyDepreciation(double monthlyDepreciation) {
        this.monthlyDepreciation = monthlyDepreciation;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setDepreciationEndDate(String depreciationEndDate) {
        this.depreciationEndDate = depreciationEndDate;
    }

    public void setResidualValue(double residulaValue) {
        this.residualValue = residulaValue;
    }

    public void setPostingDate(String postingDate) {
        this.postingDate = postingDate;
    }

    public void setEntryRaised(String enteryRaised) {
        this.entryRaised = enteryRaised;
    }

    public void setDepreciationYearToDate(double depreciationYearToDate) {
        this.depreciationYearToDate = depreciationYearToDate;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public void setNbv(double nbv) {
        this.nbv = nbv;
    }

    public void setRemainLife(int remainLife) {
        this.setRemainingLife(remainLife);
    }

    public void setTotalLife(int totalLife) {
        this.totalLife = totalLife;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public void setClassifyDate(String classifyDate) {
        this.classifyDate = classifyDate;
    }

    public void setIsFleetEnabled(String isFleetEnabled) {
        this.isFleetEnabled = isFleetEnabled;
    }

    public void setReclassify_id(String reclassify_id) {
        this.reclassify_id = reclassify_id;
    }

    public void setReq_distribution(String req_distribution) {
        this.req_distribution = req_distribution;
    }

    public void setSectionId(String sectionId) {
        this.setSectionId(Integer.parseInt(sectionId));
    }

    public String getId() {
        return id;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public String getBranchId() {
        return branchId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getDatePurchased() {
        return datePurchased;
    }

    public double getDepreciationRate() {
        return depreciationRate;
    }

    public String getAssetMake() {
        return assetMake;
    }

    public String getAssetUser() {
        return assetUser;
    }

    public String getAsset_status() {
        return asset_status;
    }

    public String getAssetMaintenance() {
        return assetMaintenance;
    }

    public double getAccumulatedDepreciation() {
        return accumulatedDepreciation;
    }

    public double getMonthlyDepreciation() {
        return monthlyDepreciation;
    }

    public double getCost() {
        return cost;
    }

    public String getDepreciationEndDate() {
        return depreciationEndDate;
    }

    public double getResidualValue() {
        return residualValue;
    }

    public String getPostingDate() {
        return postingDate;
    }

    public String getEntryRaised() {
        return entryRaised;
    }

    public double getDepreciationYearToDate() {
        return depreciationYearToDate;
    }

    public String getSection() {
        return section;
    }

    public double getNbv() {
        return nbv;
    }

    public int getRemainingLife() {
        return remainingLife;
    }

    public int getTotalLife() {
        return totalLife;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public String getIsFleetEnabled() {
        return isFleetEnabled;
    }

    public String getClassifyDate() {
        return classifyDate;
    }

    public String getReclassify_id() {
        return reclassify_id;
    }

    public int getSectionId() {
        return sectionId;
    }

    public String getReq_distribution() {
        return req_distribution;
    }

    private int sectionId;
    private String req_distribution;

    /**
     * @return the vendorAc
     */
    public String getVendorAc() {
        return vendorAc;
    }

    /**
     * @param vendorAc the vendorAc to set
     */
    public void setVendorAc(String vendorAc) {
        this.vendorAc = vendorAc;
    }

    /**
     * @return the serialNo
     */
    public String getSerialNo() {
        return serialNo;
    }

    /**
     * @param serialNo the serialNo to set
     */
    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    /**
     * @return the engineNo
     */
    public String getEngineNo() {
        return engineNo;
    }

    /**
     * @param engineNo the engineNo to set
     */
    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    /**
     * @return the supplierName
     */
    public int getSupplierName() {
        return supplierName;
    }

    /**
     * @param supplierName the supplierName to set
     */
    public void setSupplierName(int supplierName) {
        this.supplierName = supplierName;
    }

    /**
     * @return the assetMaintain
     */
    public int getAssetMaintain() {
        return assetMaintain;
    }

    /**
     * @param assetMaintain the assetMaintain to set
     */
    public void setAssetMaintain(int assetMaintain) {
        this.assetMaintain = assetMaintain;
    }

    /**
     * @return the authorizeBy
     */
    public String getAuthorizeBy() {
        return authorizeBy;
    }

    /**
     * @param authorizeBy the authorizeBy to set
     */
    public void setAuthorizeBy(String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    /**
     * @param remainingLife the remainingLife to set
     */
    public void setRemainingLife(int remainingLife) {
        this.remainingLife = remainingLife;
    }

    /**
     * @return the purchaseReason
     */
    public String getPurchaseReason() {
        return purchaseReason;
    }

    /**
     * @param purchaseReason the purchaseReason to set
     */
    public void setPurchaseReason(String purchaseReason) {
        this.purchaseReason = purchaseReason;
    }

    /**
     * @return the sbuCode
     */
    public String getSbuCode() {
        return sbuCode;
    }

    /**
     * @param sbuCode the sbuCode to set
     */
    public void setSbuCode(String sbuCode) {
        this.sbuCode = sbuCode;
    }

    /**
     * @return the barCode
     */
    public String getBarCode() {
        return barCode;
    }

    /**
     * @param barCode the barCode to set
     */
    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    /**
     * @return the spare1
     */
    public String getSpare1() {
        return spare1;
    }

    /**
     * @param spare1 the spare1 to set
     */
    public void setSpare1(String spare1) {
        this.spare1 = spare1;
    }

    /**
     * @return the spare2
     */
    public String getSpare2() {
        return spare2;
    }

    /**
     * @param spare2 the spare2 to set
     */
    public void setSpare2(String spare2) {
        this.spare2 = spare2;
    }

    /**
     * @param sectionId the sectionId to set
     */
    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }
}
