package magma.asset.dto;

public class Improvement {
    private int revalueId;
    private String assetId;
    private double costIncrease;
    private String revalueReason;
    private String revalueDate;
    private int userId;
    private String raiseEntry;
    private String revVendorAcct;
    private double cost;
    private double vatableCost;
    private double vatAmt;
    private double whtAmt;
    private double nbv;
    private double accumDep;
    private double oldCost;
    private double oldVatableCost;
    private double oldVatAmt;
    private double oldWhtAmt;
    private double oldNbv;
    private double oldAccumDep;
    private String effDate;
    private int branchId;
    private int deptId;
    private int categoryId;
    private String regNo;
    private String assetStatus;
    private String description;
    //private double cost;
    private String assetUser;
    private String wh_tax;
    private int wht_percent;
    private String subject_to_vat;
  
    private double new_vatable_cost;
    private double new_cost_price;
    private double new_nbv;
    private double new_vat_amount;
    private double new_wht_amount;
    private int newVendorId;
    private String oldVendorAcc;
    private int oldVendorId;
    private String integrify;
    private int usefullife;
    private String invoiceNo;
    private String lpoNo;
    private String location;
    private String projectCode;
    private double oldimprovCost;
    private double oldimprovvatableCost;
    private double oldimprovNBV;
    private double oldimprovaccum;
    private String sbuCode;
    
    public Improvement(int revalueId, String assetId, double costIncrease,
                       String revalueReason,
                       String revalueDate, int userId, String raiseEntry,
                       String revVendorAcct,
                       double cost, double vatableCost, double vatAmt,
                       double whtAmt, double nbv, double accumDep,
                       double oldCost, double oldVatableCost, double oldVatAmt,
                       double oldWhtAmt,
                       double oldNbv, double oldAccumDep, String effDate,
                       int branchId, int deptId,
                       int categoryId, String regNo, String desc,
                       String assetUser, String assetStatus) {
        setRevalueId(revalueId);
        setAssetId(assetId);
        setCostIncrease(costIncrease);
        setRevalueReason(revalueReason);
        setRevalueDate(revalueDate);
        setUserId(userId);
        setRaiseEntry(raiseEntry);
        setRevVendorAcct(revVendorAcct);
        setCost(cost);
        setVatableCost(vatableCost);
        setVatAmt(vatAmt);
        setWhtAmt(whtAmt);
        setNbv(nbv);
        setAccumDep(accumDep);
        setOldCost(oldCost);
        setOldVatableCost(oldVatableCost);
        setOldVatAmt(oldVatAmt);
        setOldWhtAmt(oldWhtAmt);
        setOldNbv(oldNbv);
        setOldAccumDep(oldAccumDep);
        setEffDate(effDate);
        setBranchId(branchId);
        setDeptId(deptId);
        setCategoryId(categoryId);
        setRegNo(regNo);
        setDescription(desc);
        //setCost(cost);
        setAssetUser(assetUser);
        setAssetStatus(assetStatus);

    }
    public Improvement(int revalueId, String assetId, double costIncrease,
            String revalueReason,
            String revalueDate, int userId, String raiseEntry,
            String revVendorAcct,
            double cost, double vatableCost, double vatAmt,
            double whtAmt, double nbv, double accumDep,
            double oldCost, double oldVatableCost, double oldVatAmt,
            double oldWhtAmt,
            double oldNbv, double oldAccumDep, String effDate,
            int branchId, int deptId,
            int categoryId, String regNo, String desc,
            String assetUser, String assetStatus,String integrify, int usefullife,String lpoNo,String invoiceNo) {
setRevalueId(revalueId);
setAssetId(assetId);
setCostIncrease(costIncrease);
setRevalueReason(revalueReason);
setRevalueDate(revalueDate);
setUserId(userId);
setRaiseEntry(raiseEntry);
setRevVendorAcct(revVendorAcct);
setCost(cost);
setVatableCost(vatableCost);
setVatAmt(vatAmt);
setWhtAmt(whtAmt);
setNbv(nbv);
setAccumDep(accumDep);
setOldCost(oldCost);
setOldVatableCost(oldVatableCost);
setOldVatAmt(oldVatAmt);
setOldWhtAmt(oldWhtAmt);
setOldNbv(oldNbv);
setOldAccumDep(oldAccumDep);
setEffDate(effDate);
setBranchId(branchId);
setDeptId(deptId);
setCategoryId(categoryId);
setRegNo(regNo);
setDescription(desc);
//setCost(cost);
setAssetUser(assetUser);
setAssetStatus(assetStatus);
setIntegrify(integrify);
setUsefullife(usefullife);
setLpoNo(lpoNo);
setInvoiceNo(invoiceNo);
}
    
    public int getRevalueId() {
        return revalueId;
    }

    public void setRevalueId(int revalueId) {
        this.revalueId = revalueId;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public double getCostIncrease() {
        return costIncrease;
    }

    public void setCostIncrease(double costIncrease) {
        this.costIncrease = costIncrease;
    }

    public String getRevalueReason() {
        return revalueReason;
    }

    public void setRevalueReason(String revalueReason) {
        this.revalueReason = revalueReason;
    }

    public String getRevalueDate() {
        return revalueDate;
    }

    public void setRevalueDate(String revalueDate) {
        this.revalueDate = revalueDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRaiseEntry() {
        return raiseEntry;
    }

    public void setRaiseEntry(String raiseEntry) {
        this.raiseEntry = raiseEntry;
    }

    public String getRevVendorAcct() {
        return revVendorAcct;
    }

    public void setRevVendorAcct(String revVendorAcct) {
        this.revVendorAcct = revVendorAcct;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getVatableCost() {
        return vatableCost;
    }

    public void setVatableCost(double vatableCost) {
        this.vatableCost = vatableCost;
    }

    public double getVatAmt() {
        return vatAmt;
    }

    public void setVatAmt(double vatAmt) {
        this.vatAmt = vatAmt;
    }

    public double getWhtAmt() {
        return whtAmt;
    }

    public void setWhtAmt(double whtAmt) {
        this.whtAmt = whtAmt;
    }

    public double getNbv() {
        return nbv;
    }

    public void setNbv(double nbv) {
        this.nbv = nbv;
    }

    public double getAccumDep() {
        return accumDep;
    }

    public void setAccumDep(double accumDep) {
        this.accumDep = accumDep;
    }

    public double getOldCost() {
        return oldCost;
    }

    public void setOldCost(double oldCost) {
        this.oldCost = oldCost;
    }

    public double getOldVatableCost() {
        return oldVatableCost;
    }

    public void setOldVatableCost(double oldVatableCost) {
        this.oldVatableCost = oldVatableCost;
    }

    public double getOldVatAmt() {
        return oldVatAmt;
    }

    public void setOldVatAmt(double oldVatAmt) {
        this.oldVatAmt = oldVatAmt;
    }

    public double getOldWhtAmt() {
        return oldWhtAmt;
    }

    public void setOldWhtAmt(double oldWhtAmt) {
        this.oldWhtAmt = oldWhtAmt;
    }

    public double getOldNbv() {
        return oldNbv;
    }

    public void setOldNbv(double oldNbv) {
        this.oldNbv = oldNbv;
    }

    public double getOldAccumDep() {
        return oldAccumDep;
    }

    public void setOldAccumDep(double oldAccumDep) {
        this.oldAccumDep = oldAccumDep;
    }

    public String getEffDate() {
        return effDate;
    }

    public void setEffDate(String effDate) {
        this.effDate = effDate;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getAssetStatus() {
        return assetStatus;
    }

    public void setAssetStatus(String assetStatus) {
        this.assetStatus = assetStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssetUser() {
        return assetUser;
    }

    public void setAssetUser(String assetUser) {
        this.assetUser = assetUser;
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
     * @return the wht_percent
     */
    public int getWht_percent() {
        return wht_percent;
    }

    /**
     * @param wht_percent the wht_percent to set
     */
    public void setWht_percent(int wht_percent) {
        this.wht_percent = wht_percent;
    }

    /**
     * @return the subject_to_vat
     */
    public String getSubject_to_vat() {
        return subject_to_vat;
    }

    /**
     * @param subject_to_vat the subject_to_vat to set
     */
    public void setSubject_to_vat(String subject_to_vat) {
        this.subject_to_vat = subject_to_vat;
    }

    /**
     * @return the new_vatable_cost
     */
    public double getNew_vatable_cost() {
        return new_vatable_cost;
    }

    /**
     * @param new_vatable_cost the new_vatable_cost to set
     */
    public void setNew_vatable_cost(double new_vatable_cost) {
        this.new_vatable_cost = new_vatable_cost;
    }

    /**
     * @return the new_cost_price
     */
    public double getNew_cost_price() {
        return new_cost_price;
    }

    /**
     * @param new_cost_price the new_cost_price to set
     */
    public void setNew_cost_price(double new_cost_price) {
        this.new_cost_price = new_cost_price;
    }

    /**
     * @return the new_nbv
     */
    public double getNew_nbv() {
        return new_nbv;
    }

    /**
     * @param new_nbv the new_nbv to set
     */
    public void setNew_nbv(double new_nbv) {
        this.new_nbv = new_nbv;
    }

    /**
     * @return the new_vat_amount
     */
    public double getNew_vat_amount() {
        return new_vat_amount;
    }

    /**
     * @param new_vat_amount the new_vat_amount to set
     */
    public void setNew_vat_amount(double new_vat_amount) {
        this.new_vat_amount = new_vat_amount;
    }

    /**
     * @return the new_wht_amount
     */
    public double getNew_wht_amount() {
        return new_wht_amount;
    }

    /**
     * @param new_wht_amount the new_wht_amount to set
     */
    public void setNew_wht_amount(double new_wht_amount) {
        this.new_wht_amount = new_wht_amount;
    }

    /**
     * @return the newVendorId
     */
    public int getNewVendorId() {
        return newVendorId;
    }

    /**
     * @param newVendorId the newVendorId to set
     */
    public void setNewVendorId(int newVendorId) {
        this.newVendorId = newVendorId;
    }

    /**
     * @return the oldVendorAcc
     */
    public String getOldVendorAcc() {
        return oldVendorAcc;
    }

    /**
     * @param oldVendorAcc the oldVendorAcc to set
     */
    public void setOldVendorAcc(String oldVendorAcc) {
        this.oldVendorAcc = oldVendorAcc;
    }

    /**
     * @return the oldVendorId
     */
    public int getOldVendorId() {
        return oldVendorId;
    }

    /**
     * @param oldVendorId the oldVendorId to set
     */
    public void setOldVendorId(int oldVendorId) {
        this.oldVendorId = oldVendorId;
    }
    /**
     * @return the invoiceNo
     */
    public String getInvoiceNo() {
        return invoiceNo;
    }

    /**
     * @param invoiceNo the invoiceNo to set
     */
    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }
    /**
     * @return the usefullife
     */
    public int getUsefullife() {
        return usefullife;
    }
    /**
     * @param usefullife the usefullife to set
     */
    public void setUsefullife(int usefullife) {
        this.usefullife = usefullife;
    }      
    /**
     * @return the integrify
     */
    public String getIntegrify() {
        return integrify;
    }

    /**
     * @param integrify the integrify to set
     */
    public void setIntegrify(String integrify) {
        this.integrify = integrify;
    } 
    /**
     * @return the lpoNo
     */
    public String getLpoNo() {
        return lpoNo;
    }

    /**
     * @param lpoNo the lpoNo to set
     */
    public void setLpoNo(String lpoNo) {
        this.lpoNo = lpoNo;
    }  
    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
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

    public double getOldimprovCost() {
        return oldimprovCost;
    }

    public void setOldimprovCost(double oldimprovCost) {
        this.oldimprovCost = oldimprovCost;
    }

    public double getOldimprovvatableCost() {
        return oldimprovvatableCost;
    }

    public void setOldimprovvatableCost(double oldimprovvatableCost) {
        this.oldimprovvatableCost = oldimprovvatableCost;
    }

    public double getOldimprovNBV() {
        return oldimprovNBV;
    }

    public void setOldimprovNBV(double oldimprovNBV) {
        this.oldimprovNBV = oldimprovNBV;
    }

    public double getOldimprovaccum() {
        return oldimprovaccum;
    }

    public void setOldimprovaccum(double oldimprovaccum) {
        this.oldimprovaccum = oldimprovaccum;
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
}