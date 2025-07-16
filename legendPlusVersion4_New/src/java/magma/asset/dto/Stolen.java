package magma.asset.dto;

public class Stolen 
{
  private String stolenId;
  private String assetId;
  private String location;
  private String details;
  private String policeStation;
  private String policeDate;
  private String insuranceCompany;
  private String insuranceDate;
  private String effDate;
  private int userId;
  private String raiseEntry;
  private String stolenDate;
  private int branchId;
  private int deptId;
  private int categoryId;
  private String regNo;
  private String assetStatus;
  private String description;
  private double cost;
  private String assetUser;
  private String recoverDate;
  private String recoverLocation;
  private String recoverBy;
  private String status;

  public Stolen(String stolenId,String assetId,String stolenDate,String location,
			          String details,String policeStation,String policeDate,String insuranceCompany,
                String insuranceDate,String effDate,int userId,String raiseEntry,int branchId,int deptId,
                int categoryId,String regNo,String desc,double cost,String assetUser,String assetStatus,
                String recoverDate,String recoverLocation,String recoverBy,String status)
  {
   setStolenId(stolenId);
   setAssetId(assetId);
   setStolenDate(stolenDate);
   setLocation(location);
   setDetails(details);
   setPoliceStation(policeStation);
   setPoliceDate(policeDate);
   setInsuranceCompany(insuranceCompany);
   setInsuranceDate(insuranceDate);
   setEffDate(effDate);
   setUserId(userId);
   setRaiseEntry(raiseEntry);
   setBranchId(branchId);
   setDeptId(deptId);
   setCategoryId(categoryId);
   setRegNo(regNo);
   setDescription(desc);
   setCost(cost);
   setAssetUser(assetUser);
   setAssetStatus(assetStatus);
   setRecoverDate(recoverDate);
   setRecoverLocation(recoverLocation);
   setRecoverBy(recoverBy);
   setStatus(status);
  
  }

  public String getStolenId()
  {
    return stolenId;
  }

  public void setStolenId(String stolenId)
  {
    this.stolenId = stolenId;
  }

  public String getAssetId()
  {
    return assetId;
  }

  public void setAssetId(String assetId)
  {
    this.assetId = assetId;
  }

  public String getLocation()
  {
    return location;
  }

  public void setLocation(String location)
  {
    this.location = location;
  }

  public String getDetails()
  {
    return details;
  }

  public void setDetails(String details)
  {
    this.details = details;
  }

  public String getPoliceStation()
  {
    return policeStation;
  }

  public void setPoliceStation(String policeStation)
  {
    this.policeStation = policeStation;
  }

  public String getPoliceDate()
  {
    return policeDate;
  }

  public void setPoliceDate(String policeDate)
  {
    this.policeDate = policeDate;
  }

  public String getInsuranceCompany()
  {
    return insuranceCompany;
  }

  public void setInsuranceCompany(String insuranceCompany)
  {
    this.insuranceCompany = insuranceCompany;
  }

  public String getInsuranceDate()
  {
    return insuranceDate;
  }

  public void setInsuranceDate(String insuranceDate)
  {
    this.insuranceDate = insuranceDate;
  }

  public String getEffDate()
  {
    return effDate;
  }

  public void setEffDate(String effDate)
  {
    this.effDate = effDate;
  }

  public int getUserId()
  {
    return userId;
  }

  public void setUserId(int userId)
  {
    this.userId = userId;
  }

  public String getRaiseEntry()
  {
    return raiseEntry;
  }

  public void setRaiseEntry(String raiseEntry)
  {
    this.raiseEntry = raiseEntry;
  }

  public String getStolenDate()
  {
    return stolenDate;
  }

  public void setStolenDate(String stolenDate)
  {
    this.stolenDate = stolenDate;
  }
  public int getBranchId()
  {
    return branchId;
  }

  public void setBranchId(int branchId)
  {
    this.branchId = branchId;
  }

  public int getDeptId()
  {
    return deptId;
  }

  public void setDeptId(int deptId)
  {
    this.deptId = deptId;
  }

  public int getCategoryId()
  {
    return categoryId;
  }

  public void setCategoryId(int categoryId)
  {
    this.categoryId = categoryId;
  }

  public String getRegNo()
  {
    return regNo;
  }

  public void setRegNo(String regNo)
  {
    this.regNo = regNo;
  }

  public String getAssetStatus()
  {
    return assetStatus;
  }

  public void setAssetStatus(String assetStatus)
  {
    this.assetStatus = assetStatus;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public double getCost()
  {
    return cost;
  }

  public void setCost(double cost)
  {
    this.cost = cost;
  }

  public String getAssetUser()
  {
    return assetUser;
  }

  public void setAssetUser(String assetUser)
  {
    this.assetUser = assetUser;
  }

  public String getRecoverDate()
  {
    return recoverDate;
  }

  public void setRecoverDate(String recoverDate)
  {
    this.recoverDate = recoverDate;
  }

  public String getRecoverLocation()
  {
    return recoverLocation;
  }

  public void setRecoverLocation(String recoverLocation)
  {
    this.recoverLocation = recoverLocation;
  }

  public String getRecoverBy()
  {
    return recoverBy;
  }

  public void setRecoverBy(String recoverBy)
  {
    this.recoverBy = recoverBy;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus(String status)
  {
    this.status = status;
  }

}