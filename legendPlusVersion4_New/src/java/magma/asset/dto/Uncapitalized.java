package magma.asset.dto;

import java.io.Serializable;
import java.util.Date;

public class Uncapitalized implements Serializable {
  private String assetId;
  private String regNo;
  private int branchId;
  private int deptId;
  private int sectionId;
  private int categoryId;
  private int subcategoryId;
  private String subcategoryName;
  private String description;
  private String datePurchased;
  private double depRate;
  private String make;
  private String assetUser;
  private double accumDep;
  private double monthDep;
  private double cost;
  private String depEndDate;
  private double residualValue;
  private String postingDate;
  private String entryRaised;
  private double nbv;
  private String effDate;
  private String vendorAcct;
  private String model;
  //private String makeId;
  private String engineNo;
  private String categoryName;
  private String sectionName;
  private String branchName;
  private String deptName;
  private String serialNo;
  private String maintainedBy;
  private int regionId;
  private String regionName;
  private String email1;
  private String email2;
  private String whoToRem1;
  private String whoToRem2;
  private String reqReDistbtn;
  private double vatAmt;
  private double whtAmt;
  private String subj2Vat;
  private String subj2Wht;
  private double vatableCost;
  private int wht_percent;
  private String systemIp;
  private int assetCode;
  private String integrify;
  private double impraccumDep;
  private double imprmonthDep;
  private double imprcost;
  private double imprnbv;

  public Uncapitalized(String assetId, String regNo, int branchId,int deptId,
               int sectionId, int categoryId,
               String description,String datePurchased,double depRate,String make,
               String assetUser,double accumDep,double monthDep,double cost,String depEndDate,
               double residualValue,String entryRaised,double nbv,
               String effDate,String vendorAcct,String model,String engineNo,String email1,String email2,
               String whoToRem1,String whoToRem2,String reqReDistbtn,double vatAmt,double whtAmt,
               String subj2Vat,String subj2Wht,double vatableCost) 
  {

   setAssetId(assetId);
   setRegNo(regNo);
   setBranchId(branchId);
   setBranchName(branchName);
   setDeptId(deptId);
   setDeptName(deptName);
   setSectionId(sectionId);
   setSectionName(sectionName);
   setDeptName(deptName);
   setCategoryId(categoryId);
   setCategoryName(categoryName);
   setRegionId(regionId);
   setRegionName(regionName);
   setDescription(description);
   setDatePurchased(datePurchased);
   setDepRate(depRate);
   setMake(make);
   setAssetUser(assetUser);
   setAccumDep(accumDep);
   setMonthDep(monthDep);
   setCost(cost);
   setModel(model);
   setDepEndDate(depEndDate);
   setResidualValue(residualValue);
   //setPostingDate(postingDate);
   setEntryRaised(entryRaised);
   setNbv(nbv);
   setEffDate(effDate);
   setVendorAcct(vendorAcct);
   setModel(model);
   setEngineNo(engineNo);
   setEmail1(email1);
   setEmail2(email2);
   setWhoToRem1(whoToRem1);
   setWhoToRem2(whoToRem2);
   setReqReDistbtn(reqReDistbtn);
   
   setVatAmt(vatAmt);
   setWhtAmt(whtAmt);
   setSubj2Vat(subj2Vat);
   setSubj2Wht(subj2Wht);
   setVatableCost(vatableCost);
   }
  public Uncapitalized(String assetId, String regNo, int branchId,String branchName,int deptId,
          String deptName,int sectionId,String sectionName,int categoryId,String categoryName,
          int regionId,String regionName,String description,String datePurchased,double depRate,String make,
          String assetUser,double accumDep,double monthDep,double cost,String depEndDate,
          double residualValue,String entryRaised,double nbv,
          String effDate,String vendorAcct,String model,String engineNo,String email1,String email2,
          String whoToRem1,String whoToRem2,String reqReDistbtn,double vatAmt,double whtAmt,
          String subj2Vat,String subj2Wht,double vatableCost) 
{

setAssetId(assetId);
setRegNo(regNo);
setBranchId(branchId);
setBranchName(branchName);
setDeptId(deptId);
setDeptName(deptName);
setSectionId(sectionId);
setSectionName(sectionName);
setDeptName(deptName);
setCategoryId(categoryId);
setCategoryName(categoryName);
setRegionId(regionId);
setRegionName(regionName);
setDescription(description);
setDatePurchased(datePurchased);
setDepRate(depRate);
setMake(make);
setAssetUser(assetUser);
setAccumDep(accumDep);
setMonthDep(monthDep);
setCost(cost);
setModel(model);
setDepEndDate(depEndDate);
setResidualValue(residualValue);
//setPostingDate(postingDate);
setEntryRaised(entryRaised);
setNbv(nbv);
setEffDate(effDate);
setVendorAcct(vendorAcct);
setModel(model);
setEngineNo(engineNo);
setEmail1(email1);
setEmail2(email2);
setWhoToRem1(whoToRem1);
setWhoToRem2(whoToRem2);
setReqReDistbtn(reqReDistbtn);

setVatAmt(vatAmt);
setWhtAmt(whtAmt);
setSubj2Vat(subj2Vat);
setSubj2Wht(subj2Wht);
setVatableCost(vatableCost);
}


  public Uncapitalized(String assetId, String regNo, int branchId,String branchName,int deptId,
               String deptName,int sectionId,String sectionName,int categoryId,String categoryName,
               int regionId,String regionName,String description,String datePurchased,double depRate,String make,
               String assetUser,double accumDep,double monthDep,double cost,String depEndDate,
               double residualValue,String entryRaised,double nbv,
               String effDate,String vendorAcct,String model,String engineNo,String email1,String email2,
               String whoToRem1,String whoToRem2,String reqReDistbtn,double vatAmt,double whtAmt,
               String subj2Vat,String subj2Wht,double vatableCost,String integrify) 
  {

   setAssetId(assetId);
   setRegNo(regNo);
   setBranchId(branchId);
   setBranchName(branchName);
   setDeptId(deptId);
   setDeptName(deptName);
   setSectionId(sectionId);
   setSectionName(sectionName);
   setDeptName(deptName);
   setCategoryId(categoryId);
   setCategoryName(categoryName);
   setRegionId(regionId);
   setRegionName(regionName);
   setDescription(description);
   setDatePurchased(datePurchased);
   setDepRate(depRate);
   setMake(make);
   setAssetUser(assetUser);
   setAccumDep(accumDep);
   setMonthDep(monthDep);
   setCost(cost);
   setModel(model);
   setDepEndDate(depEndDate);
   setResidualValue(residualValue);
   //setPostingDate(postingDate);
   setEntryRaised(entryRaised);
   setNbv(nbv);
   setEffDate(effDate);
   setVendorAcct(vendorAcct);
   setModel(model);
   setEngineNo(engineNo);
   setEmail1(email1);
   setEmail2(email2);
   setWhoToRem1(whoToRem1);
   setWhoToRem2(whoToRem2);
   setReqReDistbtn(reqReDistbtn);
   
   setVatAmt(vatAmt);
   setWhtAmt(whtAmt);
   setSubj2Vat(subj2Vat);
   setSubj2Wht(subj2Wht);
   setVatableCost(vatableCost);
   setIntegrify(integrify);
   setImpraccumDep(impraccumDep);
   setImprmonthDep(imprmonthDep);
   setImprcost(imprcost);
   setImprnbv(imprnbv);
   }

    public Uncapitalized()
    {
       
    }

    public void setAssetId(String assetId) {
    this.assetId = assetId;
    }

    public void setRegNo(String regNo) {
    this.regNo = regNo;
    }

    public void setBranchId(int branchId) {
    this.branchId = branchId;
    }

    public void setDeptId(int deptId) {
    this.deptId = deptId;
    }

    public void setCategoryId(int categoryId) {
    this.categoryId = categoryId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDatePurchased(String datePurchased) {
        this.datePurchased = datePurchased;
    }

    public void setDepRate(double depRate) {
    this.depRate = depRate;
    }

    public void setMake(String m) {
    this.make = make;
    }

    public void setAssetUser(String assetUser) {
        this.assetUser = assetUser;
    }


    public void setAccumDep(double accumDep) {
    this.accumDep = accumDep;
    }

    public void setMonthDep(double monthDep) {
    this.monthDep = monthDep;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setDepEndDate(String depEndDate) {
    this.depEndDate = depEndDate;
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


    public void setSectionId(int sectionId) {
    this.sectionId = sectionId;
    }

    public void setNbv(double nbv) {
        this.nbv = nbv;
    }

    

    public void setEffDate(String effDate) {
    this.effDate = effDate;
    }


    public String getAssetId() {
    return assetId;
    }

    public String getRegNo() {
    return regNo;
    }

    public int getBranchId() {
    return branchId;
    }

    public int getDeptId() {
    return deptId;
    }

    public int getCategoryId() {
    return categoryId;
    }

    public String getDescription() {
        return description;
    }

    public String getDatePurchased() {
        return datePurchased;
    }

    public double getDepRate() {
    return depRate;
    }

    public String getMake() {
    return make;
    }

    public String getAssetUser() {
        return assetUser;
    }


    public double getAccumDep() {
    return accumDep;
    }

    public double getMonthDep() {
    return monthDep;
    }

    public double getCost() {
        return cost;
    }

    public String getDepEndDate() {
    return depEndDate;
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


    public int getSectionId() {
    return sectionId;
    }

    public double getNbv() {
        return nbv;
    }



    public String getEffDate() {
    return effDate;
    }

  public String getVendorAcct()
  {
    return vendorAcct;
  }

  public void setVendorAcct(String vendorAcct)
  {
    this.vendorAcct = vendorAcct;
  }

  public String getModel()
  {
    return model;
  }

  public void setModel(String model)
  {
    this.model = model;
  }

  public String getEngineNo()
  {
    return engineNo;
  }

  public void setEngineNo(String engineNo)
  {
    this.engineNo = engineNo;
  }

  public String getCategoryName()
  {
    return categoryName;
  }

  public void setCategoryName(String categoryName)
  {
    this.categoryName = categoryName;
  }

  public String getSectionName()
  {
    return sectionName;
  }

  public void setSectionName(String sectionName)
  {
    this.sectionName = sectionName;
  }

  public String getBranchName()
  {
    return branchName;
  }

  public void setBranchName(String branchName)
  {
    this.branchName = branchName;
  }

  public String getDeptName()
  {
    return deptName;
  }

  public void setDeptName(String deptName)
  {
    this.deptName = deptName;
  }

  public String getSerialNo()
  {
    return serialNo;
  }

  public void setSerialNo(String serialNo)
  {
    this.serialNo = serialNo;
  }

  public String getMaintainedBy()
  {
    return maintainedBy;
  }

  public void setMaintainedBy(String maintainedBy)
  {
    this.maintainedBy = maintainedBy;
  }

  public int getRegionId()
  {
    return regionId;
  }

  public void setRegionId(int regionId)
  {
    this.regionId = regionId;
  }

  public String getRegionName()
  {
    return regionName;
  }

  public void setRegionName(String regionName)
  {
    this.regionName = regionName;
  }

  public String getEmail1()
  {
    return email1;
  }

  public void setEmail1(String email1)
  {
    this.email1 = email1;
  }

  public String getEmail2()
  {
    return email2;
  }

  public void setEmail2(String email2)
  {
    this.email2 = email2;
  }

  public String getWhoToRem1()
  {
    return whoToRem1;
  }

  public void setWhoToRem1(String whoToRem1)
  {
    this.whoToRem1 = whoToRem1;
  }

  public String getWhoToRem2()
  {
    return whoToRem2;
  }

  public void setWhoToRem2(String whoToRem2)
  {
    this.whoToRem2 = whoToRem2;
  }

  public String getReqReDistbtn()
  {
    return reqReDistbtn;
  }

  public void setReqReDistbtn(String reqReDistbtn)
  {
    this.reqReDistbtn = reqReDistbtn;
  }





  public double getVatAmt()
  {
    return vatAmt;
  }

  public void setVatAmt(double vatAmt)
  {
    this.vatAmt = vatAmt;
  }

  public double getWhtAmt()
  {
    return whtAmt;
  }

  public void setWhtAmt(double whtAmt)
  {
    this.whtAmt = whtAmt;
  }

  public String getSubj2Vat()
  {
    return subj2Vat;
  }

  public void setSubj2Vat(String subj2Vat)
  {
    this.subj2Vat = subj2Vat;
  }

  public String getSubj2Wht()
  {
    return subj2Wht;
  }

  public void setSubj2Wht(String subj2Wht)
  {
    this.subj2Wht = subj2Wht;
  }

  public double getVatableCost()
  {
    return vatableCost;
  }

  public void setVatableCost(double vatableCost)
  {
    this.vatableCost = vatableCost;
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
     * @return the systemIp
     */
    public String getSystemIp() {
        return systemIp;
    }

    /**
     * @param systemIp the systemIp to set
     */
    public void setSystemIp(String systemIp) {
        this.systemIp = systemIp;
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

    public void setImpraccumDep(double impraccumDep) {
    this.impraccumDep = impraccumDep;
    }

    public void setImprmonthDep(double imprmonthDep) {
    this.imprmonthDep = imprmonthDep;
    }

    public void setImprcost(double imprcost) {
        this.imprcost = imprcost;
    }
    public void setImprnbv(double imprnbv) {
        this.imprnbv = imprnbv;
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
    
    public void setSubcategoryId(int subcategoryId) {
    this.subcategoryId = subcategoryId;
    }

    public String getSubcategoryName()
    {
      return subcategoryName;
    }

    public void setSubcategoryName(String subcategoryName)
    {
      this.subcategoryName = subcategoryName;
    }

    public double getImprcost() {
        return imprcost;
    }

    public double getImpraccumDep() {
    return impraccumDep;
    }
    
    public double getImprnbv() {
    return imprnbv;
    }
    
}
