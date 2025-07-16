package magma.asset.dto;

import java.io.Serializable;

public class DisposeUncapitalise implements Serializable {
    private String disposalId;
    private String assetId;
    private String reason;
    private String buyerAcct;
    private double amount;
    private String raiseEntry;
    private double profitLoss;
    private String disposalDate;
    private String effDate;
    private String userId;
    private String status;
    private String branchId;
    private String deptId;
    private String categoryId;
    private String regNo;
    private String assetStatus;
    private String description;
    private double cost;
    private String assetUser;
    private String supervisor;

    public DisposeUncapitalise(String disposalId, String assetId, String reason,
                    String buyerAcct
                    , double amount, String raiseEntry, double profitLoss,
                    String disposalDate
                    , String effDate, String userId, String status,
                    String branchId, String deptId,
                    String categoryId, String regNo, String desc, double cost,
                    String assetUser, String assetStatus) {
        setDisposalId(disposalId);
        setAssetId(assetId);
        setReason(reason);
        setBuyerAcct(buyerAcct);
        setAmount(amount);
        setRaiseEntry(raiseEntry);
        setProfitLoss(profitLoss);
        setDisposalDate(disposalDate);
        setEffDate(effDate);
        setUserId(userId);
        setStatus(status);
        setBranchId(branchId);
        setDeptId(deptId);
        setCategoryId(categoryId);
        setRegNo(regNo);
        setDescription(desc);
        setCost(cost);
        setAssetUser(assetUser);
        setAssetStatus(assetStatus);

    }
//ganiyu

     public DisposeUncapitalise(String disposalId, String assetId, String reason,
                    String buyerAcct
                    , double amount, String raiseEntry, double profitLoss,
                    String disposalDate
                    , String effDate, String userId, String status,
                    String branchId, String deptId,
                    String categoryId, String regNo, String desc, double cost,
                    String assetUser, String assetStatus,String supervise) {
        setDisposalId(disposalId);
        setAssetId(assetId);
        setReason(reason);
        setBuyerAcct(buyerAcct);
        setAmount(amount);
        setRaiseEntry(raiseEntry);
        setProfitLoss(profitLoss);
        setDisposalDate(disposalDate);
        setEffDate(effDate);
        setUserId(userId);
        setStatus(status);
        setBranchId(branchId);
        setDeptId(deptId);
        setCategoryId(categoryId);
        setRegNo(regNo);
        setDescription(desc);
        setCost(cost);
        setAssetUser(assetUser);
        setAssetStatus(assetStatus);
        setSupervisor(supervise);

    }


    public String getDisposalId() {
        return disposalId;
    }

    public void setDisposalId(String disposalId) {
        this.disposalId = disposalId;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getBuyerAcct() {
        return buyerAcct;
    }

    public void setBuyerAcct(String buyerAcct) {
        this.buyerAcct = buyerAcct;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getRaiseEntry() {
        return raiseEntry;
    }

    public void setRaiseEntry(String raiseEntry) {
        this.raiseEntry = raiseEntry;
    }

    public double getProfitLoss() {
        return profitLoss;
    }

    public void setProfitLoss(double profitLoss) {
        this.profitLoss = profitLoss;
    }

    public String getDisposalDate() {
        return disposalDate;
    }

    public void setDisposalDate(String disposalDate) {
        this.disposalDate = disposalDate;
    }

    public String getEffDate() {
        return effDate;
    }

    public void setEffDate(String effDate) {
        this.effDate = effDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
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

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getAssetUser() {
        return assetUser;
    }

    public void setAssetUser(String assetUser) {
        this.assetUser = assetUser;
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


}