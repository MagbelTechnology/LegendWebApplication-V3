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
public class Asset implements Serializable {

    private String id;
    private String registrationNo;
    private String branchId;
    private String departmentId;
    private String section;
    private String category;
    private String subcategoryId;
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
    private String newsbuCode;
    private String barCode;
    private String spare1;
    private String spare2;
    private String spare3;
    private String spare4;
    private String spare5;
    private String spare6;    
    private int assetCode;
    private String workorder;
    private String assetcode;
    private String reqcode;
    private double contractbal;
    private String assetmodel;
    private String wh_tax;
    private double Wh_Tax_Amount;
    private String Req_Redistribution;
    private int Useful_Life;
    private int Location;
    private double Vatable_Cost;
    private double Vat;
    private String Req_Depreciation;
    private String Subject_TO_Vat;
    private String Who_TO_Rem;
    private String Email1;
    private String Who_To_Rem_2;
    private String Email2;
    private int state;
    private int driver;
    private int userid;
    private String Date_Disposed;
    private int PROVINCE;
    private String Multiple;
    private String WAR_START_DATE;
    private int WAR_MONTH;
    private String WAR_EXPIRY_DATE;
    private String Last_Dep_Date;
    private String SECTION_CODE;
    private double AMOUNT_PTD;
    private double AMOUNT_REM;
    private String PART_PAY;
    private String FULLY_PAID;
    private long GROUP_ID;
    private String GROUP_IDd;
    private String barcode2;
    private String LPO;
    private String supervisor;
    private String defer_pay;
    private String OLD_ASSET_ID;
    private int WHT_PERCENT;
    private String Post_reject_reason;
    private String Finacle_Posted_Date;
    private String system_ip;
    private String mac_address;
    private String depcode;
    private String branchCode;
    private String categoryCode;
    private String subcategoryCode;
    private String effectivedate2;
    private int CategoryId;
    private int AssetCode;
    private int UseageYears;
    private double bid;
    private String firstname;
    private String surname;
    private String bidcode;
    private String address1;
    private String address2;
    private String bidtitle;
    private String biddate;
    private String username;
    private String password;
    private String phoneNo;
    private String initials;
    private String catID;
    private String bidPeriod;
    private String chkBid;
    private String start_Date;
    private String end_Date;
    private double revalue_cost;
    private double improvaccumulatedDepreciation;
    private double improvmonthlyDepreciation;
    private double improvcost;
    private int improvusefulLife;
    private int improvremainingLife;
    private int improvtotalLife;
    private double improvTotalNbv;
    private double improvnbv;
    private String improvEndDate;
    private Date improvEffectiveDate;
    private int quantity;
    private String projectCode;
    private String assetId;
    private String comments = "";
    private String assetsighted = "";
    private String assetfunction = "";
    private String existBranchCode;
    private String itemType;
    private String warehouseCode = "";
    private String itemCode = "";
    private String bidTag = "";
    private String staffId = "";
    private String locationCode = "";
    private double basePrice;
    private double unitPrice;
    private int issuanceStatus = 0;
 public Asset(){
 
    }

    public Asset(String id, String registrationNo, String branchId,
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
        setAssetUser(assetUser);
        setAssetMaintenance(assetMaintenance);
        setAccumulatedDepreciation(accumulatedDepreciation);
        setMonthlyDepreciation(monthlyDepreciation);
        setCost(cost);
        setResidualValue(residualValue);
        setPostingDate(postingDate);
        setEntryRaised(entryRaised);
        setDepreciationYearToDate(depreciationYearToDate);
        setImprovaccumulatedDepreciation(improvaccumulatedDepreciation);
        setImprovmonthlyDepreciation(improvmonthlyDepreciation);
        setImprovcost(improvcost);
        setImprovusefulLife(improvusefulLife);
        setImprovremainingLife(improvremainingLife);
        setImprovtotalLife(improvtotalLife);
        setImprovTotalNbv(improvTotalNbv);
        setImprovnbv(improvnbv);
        setImprovEffectiveDate(improvEffectiveDate);
        setImprovEndDate(improvEndDate); 
    }
    public Asset(String id, String registrationNo, String branchId,
            String departmentId, String section, String category,
            String description,
            String datePurchased, double depreciationRate,
            String assetMake,
            String assetUser, String assetMaintenance,
            double accumulatedDepreciation,
            double monthlyDepreciation, double cost,
            String depreciationEndDate,
            double residualValue, String postingDate, String entryRaised,
            double depreciationYearToDate,double revalue_cost) {

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
   setAssetUser(assetUser);
   setAssetMaintenance(assetMaintenance);
   setAccumulatedDepreciation(accumulatedDepreciation);
   setMonthlyDepreciation(monthlyDepreciation);
   setCost(cost);
   setResidualValue(residualValue);
   setPostingDate(postingDate);
   setEntryRaised(entryRaised);
   setDepreciationYearToDate(depreciationYearToDate);
   setImprovaccumulatedDepreciation(improvaccumulatedDepreciation);
   setImprovmonthlyDepreciation(improvmonthlyDepreciation);
   setImprovcost(improvcost);
   setImprovusefulLife(improvusefulLife);
   setImprovremainingLife(improvremainingLife);
   setImprovtotalLife(improvtotalLife);
   setImprovTotalNbv(improvTotalNbv);
   setImprovnbv(improvnbv);
   setImprovEffectiveDate(improvEffectiveDate);
   setImprovEndDate(improvEndDate);
}
    public void setId(String id) {
        this.id = id;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        if (itemCode != null) {
            this.itemCode = itemCode;
        }
    }

    public void setWarehouseCode(String warehouseCode) {
        if (warehouseCode != null) {
            this.warehouseCode = warehouseCode;
        }
    }   

    public String getWarehouseCode() {
        return warehouseCode;
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

    public void setSubcategoryId(String subcategoryId) {
        this.subcategoryId = subcategoryId;
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

    public void setImprovaccumulatedDepreciation(double improvaccumulatedDepreciation) {
        this.improvaccumulatedDepreciation = improvaccumulatedDepreciation;
    }

    public void setImprovmonthlyDepreciation(double improvmonthlyDepreciation) {
        this.improvmonthlyDepreciation = improvmonthlyDepreciation;
    }

    public void setImprovcost(double improvcost) {
        this.improvcost = improvcost;
    }
    public void setImprovTotalNbv(double improvTotalNbv) {
        this.improvTotalNbv = improvTotalNbv;
    }
    public void setImprovnbv(double improvnbv) {
        this.improvnbv = improvnbv;
    }
    public void setImprovusefulLife(int improvusefulLife) {
        this.improvusefulLife = improvusefulLife;
    }
    
    public void setImprovremainingLife(int improvremainingLife) {
        this.improvremainingLife = improvremainingLife;
    }
    public void setImprovtotalLife(int improvtotalLife) {
        this.improvtotalLife = improvtotalLife;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public String getSubcategoryId() {
        return subcategoryId;
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

    public double getImprovaccumulatedDepreciation() {
        return improvaccumulatedDepreciation;
    }

    public double getImprovmonthlyDepreciation() {
        return improvmonthlyDepreciation;
    }
    public double getImprovcost() {
        return improvcost;
    }
    public double getImprovTotalNbv() {
        return improvTotalNbv;
    }
    public double getImprovnbv() {
        return improvnbv;
    }
    public int getImprovusefulLife() {
        return improvusefulLife;
    }
    public int getImprovremainingLife() {
        return improvremainingLife;
    }
    public int getImprovtotalLife() {
        return improvtotalLife;
    }
    
    public int getQuantity() {
        return quantity;
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
     * @param newsbuCode the newsbuCode to set
     */
    public void setNewsbuCode(String newsbuCode) {
        this.newsbuCode = newsbuCode;
    }

    /**
     * @return the newsbuCode
     */
    public String getNewsbuCode() {
        return newsbuCode;
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
     * @return the spare3
     */
    public String getSpare3() {
        return spare3;
    }

    /**
     * @param spare3 the spare3 to set
     */
    public void setSpare3(String spare3) {
        this.spare3 = spare3;
    }

    /**
     * @return the spare4
     */
    public String getSpare4() {
        return spare4;
    }

    /**
     * @param spare4 the spare4 to set
     */
    public void setSpare4(String spare4) {
        this.spare4 = spare4;
    }

    /**
     * @return the spare5
     */
    public String getSpare5() {
        return spare5;
    }

    /**
     * @param spare5 the spare5 to set
     */
    public void setSpare5(String spare5) {
        this.spare5 = spare5;
    }

    /**
     * @return the spare6
     */
    public String getSpare6() {
        return spare6;
    }

    /**
     * @param spare6 the spare6 to set
     */
    public void setSpare6(String spare6) {
        this.spare6 = spare6;
    }

    /**
     * @param sectionId the sectionId to set
     */
    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
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

    /**
     * @return the workorder
     */
    public String getWorkorder() {
        return workorder;
    }

    /**
     * @param workorder the workorder to set
     */
    public void setWorkorder(String workorder) {
        this.workorder = workorder;
    }

    /**
     * @return the assetcode
     */
    public String getAssetcode() {
        return assetcode;
    }

    /**
     * @param assetcode the assetcode to set
     */
    public void setAssetcode(String assetcode) {
        this.assetcode = assetcode;
    }

    /**
     * @return the reqcode
     */
    public String getReqcode() {
        return reqcode;
    }

    /**
     * @param reqcode the reqcode to set
     */
    public void setReqcode(String reqcode) {
        this.reqcode = reqcode;
    }

    /**
     * @return the contractbal
     */
    public double getContractbal() {
        return contractbal;
    }

    /**
     * @param contractbal the contractbal to set
     */
    public void setContractbal(double contractbal) {
        this.contractbal = contractbal;
    }

    /**
     * @return the assetmodel
     */
    public String getAssetmodel() {
        return assetmodel;
    }

    /**
     * @param assetmodel the assetmodel to set
     */
    public void setAssetmodel(String assetmodel) {
        this.assetmodel = assetmodel;
    }

    /**
     * @return the wh_tax
     */
    public String getWh_tax() {
        return wh_tax;
    }

    /**
     * @param wh_tax the wh_tax to set
     */
    public void setWh_tax(String wh_tax) {
        this.wh_tax = wh_tax;
    }

    /**
     * @return the Wh_Tax_Amount
     */
    public double getWh_Tax_Amount() {
        return Wh_Tax_Amount;
    }

    /**
     * @param Wh_Tax_Amount the Wh_Tax_Amount to set
     */
    public void setWh_Tax_Amount(double Wh_Tax_Amount) {
        this.Wh_Tax_Amount = Wh_Tax_Amount;
    }

    /**
     * @return the Req_Redistribution
     */
    public String getReq_Redistribution() {
        return Req_Redistribution;
    }

    /**
     * @param Req_Redistribution the Req_Redistribution to set
     */
    public void setReq_Redistribution(String Req_Redistribution) {
        this.Req_Redistribution = Req_Redistribution;
    }

    /**
     * @return the Useful_Life
     */
    public int getUseful_Life() {
        return Useful_Life;
    }

    /**
     * @param Useful_Life the Useful_Life to set
     */
    public void setUseful_Life(int Useful_Life) {
        this.Useful_Life = Useful_Life;
    }

    /**
     * @return the Location
     */
    public int getLocation() {
        return Location;
    }

    /**
     * @param Location the Location to set
     */
    public void setLocation(int Location) {
        this.Location = Location;
    }

    /**
     * @return the Vatable_Cost
     */
    public double getVatable_Cost() {
        return Vatable_Cost;
    }

    /**
     * @param Vatable_Cost the Vatable_Cost to set
     */
    public void setVatable_Cost(double Vatable_Cost) {
        this.Vatable_Cost = Vatable_Cost;
    }

    /**
     * @return the Vat
     */
    public double getVat() {
        return Vat;
    }

    /**
     * @param Vat the Vat to set
     */
    public void setVat(double Vat) {
        this.Vat = Vat;
    }

    /**
     * @return the Req_Depreciation
     */
    public String getReq_Depreciation() {
        return Req_Depreciation;
    }

    /**
     * @param Req_Depreciation the Req_Depreciation to set
     */
    public void setReq_Depreciation(String Req_Depreciation) {
        this.Req_Depreciation = Req_Depreciation;
    }

    /**
     * @return the Subject_TO_Vat
     */
    public String getSubject_TO_Vat() {
        return Subject_TO_Vat;
    }

    /**
     * @param Subject_TO_Vat the Subject_TO_Vat to set
     */
    public void setSubject_TO_Vat(String Subject_TO_Vat) {
        this.Subject_TO_Vat = Subject_TO_Vat;
    }

    /**
     * @return the Who_TO_Rem
     */
    public String getWho_TO_Rem() {
        return Who_TO_Rem;
    }

    /**
     * @param Who_TO_Rem the Who_TO_Rem to set
     */
    public void setWho_TO_Rem(String Who_TO_Rem) {
        this.Who_TO_Rem = Who_TO_Rem;
    }

    /**
     * @return the Email1
     */
    public String getEmail1() {
        return Email1;
    }

    /**
     * @param Email1 the Email1 to set
     */
    public void setEmail1(String Email1) {
        this.Email1 = Email1;
    }

    /**
     * @return the Who_To_Rem_2
     */
    public String getWho_To_Rem_2() {
        return Who_To_Rem_2;
    }

    /**
     * @param Who_To_Rem_2 the Who_To_Rem_2 to set
     */
    public void setWho_To_Rem_2(String Who_To_Rem_2) {
        this.Who_To_Rem_2 = Who_To_Rem_2;
    }

    /**
     * @return the Email2
     */
    public String getEmail2() {
        return Email2;
    }

    /**
     * @param Email2 the Email2 to set
     */
    public void setEmail2(String Email2) {
        this.Email2 = Email2;
    }

    /**
     * @return the state
     */
    public int getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(int state) {
        this.state = state;
    }

    /**
     * @return the driver
     */
    public int getDriver() {
        return driver;
    }

    /**
     * @param driver the driver to set
     */
    public void setDriver(int driver) {
        this.driver = driver;
    }

    /**
     * @return the userid
     */
    public int getUserid() {
        return userid;
    }

    /**
     * @param userid the userid to set
     */
    public void setUserid(int userid) {
        this.userid = userid;
    }

    /**
     * @return the Date_Disposed
     */
    public String getDate_Disposed() {
        return Date_Disposed;
    }

    /**
     * @param Date_Disposed the Date_Disposed to set
     */
    public void setDate_Disposed(String Date_Disposed) {
        this.Date_Disposed = Date_Disposed;
    }

    /**
     * @return the PROVINCE
     */
    public int getPROVINCE() {
        return PROVINCE;
    }

    /**
     * @param PROVINCE the PROVINCE to set
     */
    public void setPROVINCE(int PROVINCE) {
        this.PROVINCE = PROVINCE;
    }

    /**
     * @return the Multiple
     */
    public String getMultiple() {
        return Multiple;
    }

    /**
     * @param Multiple the Multiple to set
     */
    public void setMultiple(String Multiple) {
        this.Multiple = Multiple;
    }

    /**
     * @return the WAR_START_DATE
     */
    public String getWAR_START_DATE() {
        return WAR_START_DATE;
    }

    /**
     * @param WAR_START_DATE the WAR_START_DATE to set
     */
    public void setWAR_START_DATE(String WAR_START_DATE) {
        this.WAR_START_DATE = WAR_START_DATE;
    }

    /**
     * @return the WAR_MONTH
     */
    public int getWAR_MONTH() {
        return WAR_MONTH;
    }

    /**
     * @param WAR_MONTH the WAR_MONTH to set
     */
    public void setWAR_MONTH(int WAR_MONTH) {
        this.WAR_MONTH = WAR_MONTH;
    }

    /**
     * @return the WAR_EXPIRY_DATE
     */
    public String getWAR_EXPIRY_DATE() {
        return WAR_EXPIRY_DATE;
    }

    /**
     * @param WAR_EXPIRY_DATE the WAR_EXPIRY_DATE to set
     */
    public void setWAR_EXPIRY_DATE(String WAR_EXPIRY_DATE) {
        this.WAR_EXPIRY_DATE = WAR_EXPIRY_DATE;
    }

    /**
     * @return the Last_Dep_Date
     */
    public String getLast_Dep_Date() {
        return Last_Dep_Date;
    }

    /**
     * @param Last_Dep_Date the Last_Dep_Date to set
     */
    public void setLast_Dep_Date(String Last_Dep_Date) {
        this.Last_Dep_Date = Last_Dep_Date;
    }

    /**
     * @return the SECTION_CODE
     */
    public String getSECTION_CODE() {
        return SECTION_CODE;
    }

    /**
     * @param SECTION_CODE the SECTION_CODE to set
     */
    public void setSECTION_CODE(String SECTION_CODE) {
        this.SECTION_CODE = SECTION_CODE;
    }

    /**
     * @return the AMOUNT_PTD
     */
    public double getAMOUNT_PTD() {
        return AMOUNT_PTD;
    }

    /**
     * @param AMOUNT_PTD the AMOUNT_PTD to set
     */
    public void setAMOUNT_PTD(double AMOUNT_PTD) {
        this.AMOUNT_PTD = AMOUNT_PTD;
    }

    /**
     * @return the AMOUNT_REM
     */
    public double getAMOUNT_REM() {
        return AMOUNT_REM;
    }

    /**
     * @param AMOUNT_REM the AMOUNT_REM to set
     */
    public void setAMOUNT_REM(double AMOUNT_REM) {
        this.AMOUNT_REM = AMOUNT_REM;
    }

    /**
     * @return the PART_PAY
     */
    public String getPART_PAY() {
        return PART_PAY;
    }

    /**
     * @param PART_PAY the PART_PAY to set
     */
    public void setPART_PAY(String PART_PAY) {
        this.PART_PAY = PART_PAY;
    }

    /**
     * @return the FULLY_PAID
     */
    public String getFULLY_PAID() {
        return FULLY_PAID;
    }

    /**
     * @param FULLY_PAID the FULLY_PAID to set
     */
    public void setFULLY_PAID(String FULLY_PAID) {
        this.FULLY_PAID = FULLY_PAID;
    }

    /**
     * @return the GROUP_ID
     */
    public long getGROUP_ID() {
        return GROUP_ID;
    }

    /**
     * @param GROUP_ID the GROUP_ID to set
     */
    public void setGROUP_ID(long GROUP_ID) {
        this.GROUP_ID = GROUP_ID;
    }

    /**
     * @return the GROUP_IDd
     */
    public String getGROUP_IDd() {
        return GROUP_IDd;
    }

    /**
     * @param GROUP_IDd the GROUP_IDd to set
     */
    public void setGROUP_IDd(String GROUP_IDd) {
        this.GROUP_IDd = GROUP_IDd;
    }

    /**
     * @return the barcode2
     */
    public String getBarcode2() {
        return barcode2;
    }

    /**
     * @param barcode2 the barcode2 to set
     */
    public void setBarcode2(String barcode2) {
        this.barcode2 = barcode2;
    }

    /**
     * @return the LPO
     */
    public String getLPO() {
        return LPO;
    }

    /**
     * @param LPO the LPO to set
     */
    public void setLPO(String LPO) {
        this.LPO = LPO;
    }

    /**
     * @return the supervisor
     */
    public String getSupervisor() {
        return supervisor;
    }

    /**
     * @param supervisor the supervisor to set
     */
    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    /**
     * @return the defer_pay
     */
    public String getDefer_pay() {
        return defer_pay;
    }

    /**
     * @param defer_pay the defer_pay to set
     */
    public void setDefer_pay(String defer_pay) {
        this.defer_pay = defer_pay;
    }

    /**
     * @return the OLD_ASSET_ID
     */
    public String getOLD_ASSET_ID() {
        return OLD_ASSET_ID;
    }

    /**
     * @param OLD_ASSET_ID the OLD_ASSET_ID to set
     */
    public void setOLD_ASSET_ID(String OLD_ASSET_ID) {
        this.OLD_ASSET_ID = OLD_ASSET_ID;
    }

    /**
     * @return the WHT_PERCENT
     */
    public int getWHT_PERCENT() {
        return WHT_PERCENT;
    }

    /**
     * @param WHT_PERCENT the WHT_PERCENT to set
     */
    public void setWHT_PERCENT(int WHT_PERCENT) {
        this.WHT_PERCENT = WHT_PERCENT;
    }

    /**
     * @return the Post_reject_reason
     */
    public String getPost_reject_reason() {
        return Post_reject_reason;
    }

    /**
     * @param Post_reject_reason the Post_reject_reason to set
     */
    public void setPost_reject_reason(String Post_reject_reason) {
        this.Post_reject_reason = Post_reject_reason;
    }

    /**
     * @return the Finacle_Posted_Date
     */
    public String getFinacle_Posted_Date() {
        return Finacle_Posted_Date;
    }

    /**
     * @param Finacle_Posted_Date the Finacle_Posted_Date to set
     */
    public void setFinacle_Posted_Date(String Finacle_Posted_Date) {
        this.Finacle_Posted_Date = Finacle_Posted_Date;
    }

    /**
     * @return the system_ip
     */
    public String getSystem_ip() {
        return system_ip;
    }

    /**
     * @param system_ip the system_ip to set
     */
    public void setSystem_ip(String system_ip) {
        this.system_ip = system_ip;
    }

    /**
     * @return the mac_address
     */
    public String getMac_address() {
        return mac_address;
    }

    /**
     * @param mac_address the mac_address to set
     */
    public void setMac_address(String mac_address) {
        this.mac_address = mac_address;
    }

    /**
     * @return the depcode
     */
    public String getDepcode() {
        return depcode;
    }

    /**
     * @param depcode the depcode to set
     */
    public void setDepcode(String depcode) {
        this.depcode = depcode;
    }

    /**
     * @return the branchCode
     */
    public String getBranchCode() {
        return branchCode;
    }

    /**
     * @param branchCode the branchCode to set
     */
    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    /**
     * @return the categoryCode
     */
    public String getCategoryCode() {
        return categoryCode;
    }

    /**
     * @param categoryCode the categoryCode to set
     */
    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    /**
     * @return the subcategoryCode
     */
    public String getSubcategoryCode() {
        return subcategoryCode;
    }

    /**
     * @param subcategoryCode the subcategoryCode to set
     */
    public void setSubcategoryCode(String subcategoryCode) {
        this.subcategoryCode = subcategoryCode;
    }

    /**
     * @return the effectivedate2
     */
    public String getEffectivedate2() {
        return effectivedate2;
    }

    /**
     * @param effectivedate2 the effectivedate2 to set
     */
    public void setEffectivedate2(String effectivedate2) {
        this.effectivedate2 = effectivedate2;
    }

    /**
     * @return the CategoryId
     */
    public int getCategoryId() {
        return CategoryId;
    }

    /**
     * @param CategoryId the CategoryId to set
     */
    public void setCategoryId(int CategoryId) {
        this.CategoryId = CategoryId;
    }

    /**
     * @return the UseageYears
     */
    public int getUseageYears() {
        return UseageYears;
    }

    /**
     * @param UseageYears the UseageYears to set
     */
    public void setUseageYears(int UseageYears) {
        this.UseageYears = UseageYears;
    }

    /**
     * @return the bid
     */
    public double getBid() {
        return bid;
    }

    /**
     * @param bid the bid to set
     */
    public void setBid(double bid) {
        this.bid = bid;
    }

    /**
     * @return the firstname
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * @param firstname the firstname to set
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * @return the surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * @param surname the surname to set
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * @return the bidcode
     */
    public String getBidcode() {
        return bidcode;
    }

    /**
     * @param bidcode the bidcode to set
     */
    public void setBidcode(String bidcode) {
        this.bidcode = bidcode;
    }

    /**
     * @return the address1
     */
    public String getAddress1() {
        return address1;
    }

    /**
     * @param address1 the address1 to set
     */
    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    /**
     * @return the address2
     */
    public String getAddress2() {
        return address2;
    }

    /**
     * @param address2 the address2 to set
     */
    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    /**
     * @return the bidtitle
     */
    public String getBidtitle() {
        return bidtitle;
    }

    /**
     * @param bidtitle the bidtitle to set
     */
    public void setBidtitle(String bidtitle) {
        this.bidtitle = bidtitle;
    }

    /**
     * @return the biddate
     */
    public String getBiddate() {
        return biddate;
    }

    /**
     * @param biddate the biddate to set
     */
    public void setBiddate(String biddate) {
        this.biddate = biddate;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the phoneNo
     */
    public String getPhoneNo() {
        return phoneNo;
    }

    /**
     * @param phoneNo the phoneNo to set
     */
    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    /**
     * @return the projectCode
     */
    public String getProjectCode() {
        return projectCode;
    }

    /**
     * @param projectCode the projectCode to set
     */
    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    /**
     * @return the assetId
     */
    public String getAssetId() {
        return assetId;
    }

    /**
     * @param assetId the assetId to set
     */
    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    
    /** 
     * @return the initials
     */
    public String getInitials() {
        return initials;
    }

    /**
     * @param initials the initials to set
     */
    public void setInitials(String initials) {
        this.initials = initials;
    }

    /**
     * @return the catID
     */
    public String getCatID() {
        return catID;
    }

    /**
     * @param catID the catID to set
     */
    public void setCatID(String catID) {
        this.catID = catID;
    }

    /**
     * @return the bidPeriod
     */
    public String getBidPeriod() {
        return bidPeriod;
    }

    /**
     * @param bidPeriod the bidPeriod to set
     */
    public void setBidPeriod(String bidPeriod) {
        this.bidPeriod = bidPeriod;
    }

    /**
     * @return the chkBid
     */
    public String getChkBid() {
        return chkBid;
    }

    /**
     * @param chkBid the chkBid to set
     */
    public void setChkBid(String chkBid) {
        this.chkBid = chkBid;
    }

    /**
     * @return the start_Date
     */
    public String getStart_Date() {
        return start_Date;
    }

    /**
     * @param start_Date the start_Date to set
     */
    public void setStart_Date(String start_Date) {
        this.start_Date = start_Date;
    }

    /**
     * @return the end_Date
     */
    public String getEnd_Date() {
        return end_Date;
    }

    /**
     * @param end_Date the end_Date to set
     */
    public void setEnd_Date(String end_Date) {
        this.end_Date = end_Date;
    }
    public double getRevalue_cost() {
        return revalue_cost;
    }    
    public void setRevalue_cost(double revalue_cost) {
        this.revalue_cost = revalue_cost;
    }   
    /**
     * @return the IMPROVE_START_DATE
     */
    public Date getImprovEffectiveDate() {
        return improvEffectiveDate;
    }

    /**
     * @param IMPROVE_START_DATE the WAR_START_DATE to set
     */
    public void setImprovEffectiveDate(Date improvEffectiveDate) {
        this.improvEffectiveDate = improvEffectiveDate;
    }    
    /**
     * @return the IMPROVE_END_DATE
     */
    public String getImprovEndDate() {
        return improvEndDate;
    }

    /**
     * @param IMPROVE_END_DATE the IMPROVE_END_DATE to set
     */
    public void setImprovEndDate(String improvEndDate) {
        this.improvEndDate = improvEndDate;
    }    
    
    public void setComments(String comments) {
        if (comments != null) {
            this.comments = comments;
        }
    } 
    public void setAssetsighted(String assetsighted) {
        if (assetsighted != null) {
            this.assetsighted = assetsighted;
        }
    } 
    public void setAssetfunction(String assetfunction) {
        if (assetfunction != null) {
            this.assetfunction = assetfunction;
        }
    } 
    
    public String getComments() {
        return comments;
    }
    
    public String getAssetsighted() {
        return assetsighted;
    }
    
    public String getAssetfunction() {
        return assetfunction;
    }
    
    public String getExistBranchCode()
    {
        return existBranchCode;
    }
    public void setExistBranchCode(String existBranchCode)
    {
        this.existBranchCode = existBranchCode;
    }  

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        if (itemType != null) {
            this.itemType = itemType;
        }
    }

    /**
     * @return the bidTag
     */
    public String getBidTag() {
        return bidTag;
    }

    /**
     * @param bidTag the bidTag to set
     */
    public void setBidTag(String bidTag) {
        this.bidTag = bidTag;
    }

    /**
     * @return the staffId
     */
    public String getStaffId() {
        return staffId;
    }

    /**
     * @param staffId the staffId to set
     */
    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }
    
    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }    
    public double getBasePrice() {
        return basePrice;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }
    
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }    
    public double getUnitPrice() {
        return unitPrice;
    }

    public void setIssuanceStatus(int issuanceStatus) {
        this.issuanceStatus = issuanceStatus;
    }  
    
    public int getIssuanceStatus() {
        return issuanceStatus;
    }          
}
