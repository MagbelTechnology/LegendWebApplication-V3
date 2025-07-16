package com.magbel.ia.vao;


public class Asset
{

    private String mtid;
    private String assetEquiptAssignmentId;
    private String assetId;
    private String assetName;
    private String purchaseDate;
    private double costPrice;
    private String supplier;
    private String warantyPeriod;
    private String username;
    private String branchAssigned;
    private String dept;
    private String status;
    private String lastPeriodMaintd;
    private String lastUpgrade;
    private String contactPerson;
    private String contactPersonEmail;
    private String contactPersonPhoneNo;

    public Asset(String mtid, String assetEquiptAssignmentId, String assetId, String assetName, String purchaseDate, double costPrice, 
            String supplier, String warantyPeriod, String username, String branchAssigned, String dept, String status, String lastPeriodMaintd, 
            String lastUpgrade, String contactPerson, String contactPersonEmail, String contactPersonPhoneNo)
    {
        this.costPrice = 0.0D;
        this.mtid = mtid;
        this.assetEquiptAssignmentId = assetEquiptAssignmentId;
        this.assetId = assetId;
        this.assetName = assetName;
        this.purchaseDate = purchaseDate;
        this.costPrice = costPrice;
        this.supplier = supplier;
        this.warantyPeriod = warantyPeriod;
        this.username = username;
        this.branchAssigned = branchAssigned;
        this.dept = dept;
        this.status = status;
        this.lastPeriodMaintd = lastPeriodMaintd;
        this.lastUpgrade = lastUpgrade;
        this.contactPerson = contactPerson;
        this.contactPersonEmail = contactPersonEmail;
        this.contactPersonPhoneNo = contactPersonPhoneNo;
    }

    public Asset()
    {
        costPrice = 0.0D;
    }

    public String getMtId()
    {
        return mtid;
    }

    public void setMtId(String mtid)
    {
        this.mtid = mtid;
    }

    public String getAssetEquiptAssignmentId()
    {
        return assetEquiptAssignmentId;
    }

    public void setAssetEquiptAssignmentId(String assetEquiptAssignmentId)
    {
        this.assetEquiptAssignmentId = assetEquiptAssignmentId;
    }

    public String getAssetId()
    {
        return assetId;
    }

    public void setAssetId(String assetId)
    {
        this.assetId = assetId;
    }

    public String getAssetName()
    {
        return assetName;
    }

    public void setAssetName(String assetName)
    {
        this.assetName = assetName;
    }

    public String getPurchaseDate()
    {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate)
    {
        this.purchaseDate = purchaseDate;
    }

    public double getCostPrice()
    {
        return costPrice;
    }

    public void setCostPrice(double costPrice)
    {
        this.costPrice = costPrice;
    }

    public String getSupplier()
    {
        return supplier;
    }

    public void setSupplier(String supplier)
    {
        this.supplier = supplier;
    }

    public String getWarantyPeriod()
    {
        return warantyPeriod;
    }

    public void setWarantyPeriod(String warantyPeriod)
    {
        this.warantyPeriod = warantyPeriod;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getBranchAssigned()
    {
        return branchAssigned;
    }

    public void setBranchAssigned(String branchAssigned)
    {
        this.branchAssigned = branchAssigned;
    }

    public String getDept()
    {
        return dept;
    }

    public void setDept(String dept)
    {
        this.dept = dept;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getLastPeriodMaintd()
    {
        return lastPeriodMaintd;
    }

    public void setLastPeriodMaintd(String lastPeriodMaintd)
    {
        this.lastPeriodMaintd = lastPeriodMaintd;
    }

    public String getLastUpgrade()
    {
        return lastUpgrade;
    }

    public void setLastUpgrade(String lastUpgrade)
    {
        this.lastUpgrade = lastUpgrade;
    }

    public String getContactPerson()
    {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson)
    {
        this.contactPerson = contactPerson;
    }

    public String getContactPersonEmail()
    {
        return contactPersonEmail;
    }

    public void setContactPersonEmail(String contactPersonEmail)
    {
        this.contactPersonEmail = contactPersonEmail;
    }

    public String getContactPersonPhoneNo()
    {
        return contactPersonPhoneNo;
    }

    public void setContactPersonPhoneNo(String contactPersonPhoneNo)
    {
        this.contactPersonPhoneNo = contactPersonPhoneNo;
    }
}
