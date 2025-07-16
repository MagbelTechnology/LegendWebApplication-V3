package com.magbel.legend.vao;

import java.io.Serializable;

public class newAssetTransaction_18_02_2025
    implements Serializable
{

    private static final long serialVersionUID = 1L;
    private String integrifyId;
    private String description;
    private String registrationNo;
    private String vendorAC;
    private String datepurchased;  
    private String assetMake;
    private String assetModel;
    private String assetSerialNo;
    private String assetEngineNo;
    private String supplierName;
    private String assetUser;
    private String assetMaintenance;
    private double costPrice;
    private double monthlyDep;
    private double accumDep;
    private double nbv;
    private double improvcostPrice;
    private double improvmonthlyDep;
    private double improvaccumDep;
    private double improvnbv;
    private double totalnbv;
    private double deprChargeToDate;
    private double totalCost;
    
    private String effectiveDate;
    private String dependDate;
    private String depDate;
    private String authorizedBy;
    private String whTax;
    private double whTaxValue;
    private double vatValue;
    private String postingDate;
    private String purchaseReason;
    private String subjectTOVat;
    private String assetStatus;
    private String state;
    private String driver;
    private String userID;
    private String branchCode;
    private String branchId;
    private String sectionCode;
    private String sectionName;
    private String deptCode;
    private String deptName;
    private String categoryCode;
    private String categoryName;
    private String subcategoryCode;
    private String barCode;
    private String sbuCode;
    private String sbuName;
    private String newsbuCode;
    private String oldsbuCode;
    private String lpo;
    private String invoiceNo;
    private String posted;
    private String assetId;
    private String oldassetId;
    private String assetCode;
    private String supervisor;
    private String systemIp;
    private String errormessage;
    private String assetType;
    private String tranType;
    private int noofitems;
    private String location;
    private String spare1;
    private String spare2;
    private String spare3;
    private String spare4;
    private String spare5;
    private String spare6;
    private String multiple;
    private String memo;
    private String memovalue;
    private int usefullife;
    private int improvtotallife;
    private String projectCode;
    private String assetsighted;
    private String assetfunction;
    private String serialNo;
    private String engineNo;
    private String vendorCode;
    private String vendorName;
    private String comments;
    private int calcLifeSpan;
    private double depRate;
    private int remainLife;
    private int improveRemainLife;
    private String branchName;
    private String debitAccount;
    private String creditAccount;
    private String debitAccountName;
    private String creditAccountName;
    private String initiatorId;
    private String supervisorId;
    private String postedBy;
    private String transDate;
    private double amount;
    private String response;
    private String action;
    private String chargeYear;
    private double prevMonthlyDep;
    private double prevAccumDep;
    private double prevCostPrice;
    private double prevNBV;
    private double monthlyDifference;
    private double accumDifference;
    private double nbvDifference;
    private double prevIMPROVMonthlyDep;
    private double prevIMPROVAccumDep;
    private double prevIMPROVCostPrice;
    private double prevIMPROVNBV;
    private double prevMonthlyDifference;
    private double lifeSpan;
    private String disposalDate;
    private String disposeReason;
    private String qrCode;
    private double profitAmount;
    private double disposalProceed;
    private double disposalAmount;
    private String accumDepLedger;
    private String depLedger;
    private String assetLedger;
    private String glAccount;
    private String legacyPostedDate;
    private String processingDate;
    private String oldBranchId;
    private String oldDeptId;
    private String oldAssetUser;
    private String oldSection;
    private String oldBranchCode;
    private String oldSectionCode;
    private String oldDeptCode;
    private String oldCategoryCode;
    private String approvalStatus;
    private String newBranchCode;
    private String newSectionCode;
    private String newDeptCode;
    private String newAssetId;
    
    
   public newAssetTransaction_18_02_2025()  
    {
          
    }

	public newAssetTransaction_18_02_2025(String integrifyId,String description,String registrationNo,String vendorAC, String supplierName,
                          String datepurchased, String assetMake,String assetUser, 
                          String assetModel, String assetEngineNo,int userId,double costPrice,
                          String effectiveDate,String assetSerialNo,String assetMaintenance,
                          String authorizedBy,String whTax,double whTaxValue,double vatValue, String postingDate,String purchaseReason,
                          String subjectTOVat,String assetStatus,String state,String driver,String userID,
                          String branchCode,String sectionCode,String deptCode,String categoryCode,String subcategoryCode,
                          String barCode,String sbuCode,String lpo,String invoiceNo,String posted,String assetId,String assetCode,
                          String supervisor,String systemIp,String errormessage,String assetType,String tranType,int noofitems,
                          String location, String spare1,String spare2,String spare3,String spare4,String spare5,String spare6,
                          String memo,String memovalue,String multiple,int usefullife) {
	
        this.integrifyId = integrifyId;
        this.description = description;
		this.registrationNo = registrationNo;
		this.vendorAC = vendorAC;
		this.supplierName = supplierName;		
		this.datepurchased = datepurchased;
		this.assetMake = assetMake;
		this.assetUser = assetUser;
		this.assetModel = assetModel;
		this.assetEngineNo = assetEngineNo;
        this.costPrice = costPrice;
        this.effectiveDate = effectiveDate;  
        this.assetSerialNo = assetSerialNo;
        this.assetMaintenance = assetMaintenance;
        this.authorizedBy = authorizedBy;
        this.whTax = whTax;
        this.whTaxValue = whTaxValue;
        this.vatValue = vatValue;
        this.postingDate = postingDate;
        this.purchaseReason = purchaseReason;
        this.subjectTOVat = subjectTOVat;
        this.assetStatus = assetStatus;
        this.state = state;
        this.driver = driver;
        this.userID = userID;
        this.branchCode = branchCode;
        this.sectionCode = sectionCode;
        this.deptCode = deptCode;
        this.categoryCode = categoryCode;
        this.subcategoryCode = subcategoryCode;
        this.barCode = barCode;
        this.sbuCode = sbuCode;
        this.lpo = lpo;
        this.invoiceNo = invoiceNo;
        this.posted = posted;
        this.assetId =assetId;
        this.assetCode = assetCode;
        this.supervisor = supervisor;
        this.systemIp = systemIp;
        this.errormessage = errormessage;
        this.assetType = assetType;
        this.tranType = tranType;
        this.noofitems = noofitems;
        this.location = location;
        this.spare1 = spare1;
        this.spare2 = spare2;
        this.spare3 = spare3;
        this.spare4 = spare4;
        this.spare5 = spare5;
        this.spare6 = spare6;
        this.memo = memo;
        this.memovalue = memovalue;
        this.multiple = multiple;
        this.usefullife = usefullife;
	}

	public newAssetTransaction_18_02_2025(String integrifyId,String description,String registrationNo,String vendorAC, String supplierName,
                          String datepurchased, String assetMake,String assetUser, 
                          String assetModel, String assetEngineNo,int userId,double costPrice,
                          String effectiveDate,String dependDate,String depDate,String assetSerialNo,String assetMaintenance,
                          String authorizedBy,String whTax,double whTaxValue,double vatValue, String postingDate,String purchaseReason,
                          String subjectTOVat,String assetStatus,String state,String driver,String userID,
                          String branchCode,String sectionCode,String deptCode,String categoryCode,String subcategoryCode,
                          String barCode,String sbuCode,String lpo,String invoiceNo,String posted,String assetId,String assetCode,
                          String supervisor,String systemIp,String errormessage,String assetType,String tranType,int noofitems,
                          String location, String spare1,String spare2,String spare3,String spare4,String spare5,String spare6,
                          String memo,String memovalue,String multiple,int usefullife,int improvtotallife) {
	
        this.integrifyId = integrifyId;
        this.description = description;
		this.registrationNo = registrationNo;
		this.vendorAC = vendorAC;
		this.supplierName = supplierName;		
		this.datepurchased = datepurchased;
		this.assetMake = assetMake;
		this.assetUser = assetUser;
		this.assetModel = assetModel;
		this.assetEngineNo = assetEngineNo;
        this.costPrice = costPrice;
        this.effectiveDate = effectiveDate;  
        this.dependDate = dependDate;  
        this.depDate = depDate;  
        this.assetSerialNo = assetSerialNo;
        this.assetMaintenance = assetMaintenance;
        this.authorizedBy = authorizedBy;
        this.whTax = whTax;
        this.whTaxValue = whTaxValue;
        this.vatValue = vatValue;
        this.postingDate = postingDate;
        this.purchaseReason = purchaseReason;
        this.subjectTOVat = subjectTOVat;
        this.assetStatus = assetStatus;
        this.state = state;
        this.driver = driver;
        this.userID = userID;
        this.branchCode = branchCode;
        this.sectionCode = sectionCode;
        this.deptCode = deptCode;
        this.categoryCode = categoryCode;
        this.subcategoryCode = subcategoryCode;
        this.barCode = barCode;
        this.sbuCode = sbuCode;
        this.lpo = lpo;
        this.invoiceNo = invoiceNo;
        this.posted = posted;
        this.assetId =assetId;
        this.assetCode = assetCode;
        this.supervisor = supervisor;
        this.systemIp = systemIp;
        this.errormessage = errormessage;
        this.assetType = assetType;
        this.tranType = tranType;
        this.noofitems = noofitems;
        this.location = location;
        this.spare1 = spare1;
        this.spare2 = spare2;
        this.spare3 = spare3;
        this.spare4 = spare4;
        this.spare5 = spare5;
        this.spare6 = spare6;
        this.memo = memo;
        this.memovalue = memovalue;
        this.multiple = multiple;
        this.usefullife = usefullife;
        this.improvtotallife = improvtotallife;
	}
    public final String getIntegrifyId()
    {
        return integrifyId;
    }

    public final void setIntegrifyId(String IntegrifyId)
    {
    	integrifyId = IntegrifyId;
    }
    public final String getDescription()
    {
        return description;
    }

    public final void setDescription(String description)
    {
        this.description = description;
    }    
    public final String getRegistrationNo()
    {
        return registrationNo;
    }

    public final void setRegistrationNo(String RegistrationNo)
    {
        this.registrationNo = RegistrationNo;
    }    

    public final String getVendorAC()
    {
        return vendorAC;
    }

    public final void setVendorAC(String VendorAC)
    {
    	vendorAC = VendorAC;
    }
    public final String getDatepurchased()
    {
        return datepurchased;
    }
 
    public final void setDatepurchased(String Datepurchased)
    {
    	datepurchased = Datepurchased;
    }

    public final String getAssetMake()
    {
        return assetMake;
    }

    public final void setAssetMake(String AssetMake)
    {
    	assetMake = AssetMake;
    }
    public final String getAssetUser()
    {
        return assetUser;
    }

    public final void setAssetUser(String AssetUser)
    {
    	assetUser = AssetUser;
    }    
    
    public final String getAssetModel()
    {
        return assetModel;
    }

    public final void setAssetModel(String AssetModel)
    {
    	assetModel = AssetModel;
    }    
    public final String getAssetEngineNo()
    {
        return assetEngineNo;
    }

    public final void setAssetEngineNo(String AssetEngineNo)
    {
    	assetEngineNo = AssetEngineNo;
    }
    public final String getSupplierName()
    {
        return supplierName;
    }
    public final void setSupplierName(String SupplierName)
    {
    	supplierName = SupplierName;
    }    
    public final double getCostPrice()
    {
        return costPrice;
    }

    public final void setCostPrice(double costPrice)
    {
        this.costPrice = costPrice;
    }    

    public final double getMonthlyDep()
    {
        return monthlyDep;
    }

    public final void setMonthlyDep(double monthlyDep)
    {
        this.monthlyDep = monthlyDep;
    }  
    
    public final double getAccumDep()
    {
        return accumDep;
    }

    public final void setAccumDep(double accumDep)
    {
        this.accumDep = accumDep;
    }  

    public final double getNbv()
    {
        return nbv;
    }

    public final void setNbv(double nbv)
    {
        this.nbv = nbv;
    }  

    public final double getImprovcostPrice()
    {
        return improvcostPrice;
    }

    public final void setImprovcostPrice(double improvcostPrice)
    {
        this.improvcostPrice = improvcostPrice;
    }  
    
    public final double getImprovmonthlyDep()
    {
        return improvmonthlyDep;
    }

    public final void setImprovmonthlyDep(double improvmonthlyDep)
    {
        this.improvmonthlyDep = improvmonthlyDep;
    }  
    
    public final double getImprovaccumDep()
    {
        return improvaccumDep;
    }

    public final void setImprovaccumDep(double improvaccumDep)
    {
        this.improvaccumDep = improvaccumDep;
    }  
    
    public final double getImprovnbv()
    {
        return improvnbv;
    }

    public final void setImprovnbv(double improvnbv)
    {
        this.improvnbv = improvnbv;
    }      
    
    public final double getTotalnbv()
    {
        return totalnbv;
    }

    public final void setTotalnbv(double totalnbv)
    {
        this.totalnbv = totalnbv;
    }      
    
    public final double getDeprChargeToDate()
    {
        return deprChargeToDate;
    } 

    public final void setDeprChargeToDate(double deprChargeToDate)
    {
        this.deprChargeToDate = deprChargeToDate;
    }      
    
    public final double getTotalCost()
    {
        return totalCost;
    }

    public final void setTotalCost(double totalCost)
    {
        this.totalCost = totalCost;
    }      
                      
    public final String getEffectiveDate()
    {
        return effectiveDate;
    }

    public final void setEffectiveDate(String EffectiveDate)
    {
    	effectiveDate = EffectiveDate;
    }
    
	public final String getDependDate()
	{
	return dependDate;
	}
	
	public final void setDependDate(String DependDate)
	{
		dependDate = DependDate;
	}
    
	public final String getDepDate()
	{
	return depDate;
	}
	
	public final void setDepDate(String DepDate)
	{
		depDate = DepDate;
	}

/*
    public final int getUserId()
    {
        return userId;
    }

    public final void setUserId(int UserId)
    {
        userId = UserId;
    }
    */
    public final String getAssetSerialNo()
    {
        return assetSerialNo;
    }

    public final void setAssetSerialNo(String AssetSerialNo)
    {
    	assetSerialNo = AssetSerialNo;
    }      
    public final String getAssetMaintenance()
    {
        return assetMaintenance;
    }

    public final void setAssetMaintenance(String AssetMaintenance)
    {
    	assetMaintenance = AssetMaintenance;
    }   
     
    public final String getAuthorizedBy()
    {
        return authorizedBy;
    }

    public final void setAuthorizedBy(String AuthorizedBy)
    {
    	authorizedBy = AuthorizedBy;
    }  
    public final String getWhTax()
    {
        return whTax;
    }

    public final void setWhTax(String WhTax)
    {
    	whTax = WhTax;
    }  
    public final double getVatValue()
    {
        return vatValue;
    }

    public final void setVatValue(double VatValue)
    {
    	vatValue = VatValue;
    }     
    
    public final double getWhTaxValue()
    {
        return whTaxValue;
    }

    public final void setWhTaxValue(double WhTaxValue)
    {
    	whTaxValue = WhTaxValue;
    }     
    
    public final String getPostingDate()
    {
        return postingDate;
    }

    public final void setPostingDate(String PostingDate)
    {
    	postingDate = PostingDate;
    } 
    public final String getPurchaseReason()
    {
        return purchaseReason;
    }

    public final void setPurchaseReason(String PurchaseReason)
    {
    	purchaseReason = PurchaseReason;
    } 
    public final String getSubjectTOVat()
    {
        return subjectTOVat;
    }

    public final void setSubjectTOVat(String SubjectTOVat)
    {
    	subjectTOVat = SubjectTOVat;
    } 
    public final String getAssetStatus()
    {
        return assetStatus;
    }

    public final void setAssetStatus(String AssetStatus)
    {
    	assetStatus = AssetStatus;
    } 
    public final String getState()
    {
        return state;
    }

    public final void setState(String State)
    {
    	state = State;
    }     
    public final String getDriver()
    {
        return driver;
    }

    public final void setDriver(String Driver)
    {
    	driver = Driver;
    }  
    public final String getUserID()
    {
        return userID;
    }

    public final void setUserID(String UserID)
    {
    	userID = UserID;
    }  
    public final String getBranchCode()
    {
        return branchCode;
    }

    public final void setBranchCode(String BranchCode)
    {
    	branchCode = BranchCode;
    }  
    public final String getBranchId()
    {
        return branchId;
    }

    public final void setBranchId(String BranchId)
    {
    	branchId = BranchId;
    } 
    public final String getSectionCode()
    {
        return sectionCode;
    }

    public final void setSectionCode(String SectionCode)
    {
    	sectionCode = SectionCode;
    }  
    public final String getSectionName()
    {
        return sectionName;
    }

    public final void setSectionName(String SectionName)
    {
    	sectionName = SectionName;
    }  
    public final String getDeptCode()
    {
        return deptCode;
    }

    public final void setDeptCode(String DeptCode)
    {
    	deptCode = DeptCode;
    }      
    public final String getDeptName()
    {
        return deptName;
    }

    public final void setDeptName(String DeptName)
    {
    	deptName = DeptName;
    }   
    public final String getCategoryCode()
    {
        return categoryCode;
    }
    
    public final void setCategoryCode(String CategoryCode)
    {
    	categoryCode = CategoryCode;
    }  
    public final String getCategoryName()
    {
        return categoryName;
    }
    
    public final void setCategoryName(String CategoryName)
    {
    	categoryName = CategoryName;
    }  
    public final String getSubcategoryCode()
    {
        return subcategoryCode;
    }    
    public final void setSubcategoryCode(String SubcategoryCode)
    {
    	subcategoryCode = SubcategoryCode;
    }      
    public final String getBarCode()
    {
        return barCode;
    }

    public final void setBarCode(String BarCode)
    {
    	barCode = BarCode;
    }  
    public final String getSbuCode()
    {
        return sbuCode;
    }

    public final void setSbuCode(String SbuCode)
    {
    	sbuCode = SbuCode;
    }  
    public final String getSbuName()
    {
        return sbuName;
    }

    public final void setSbuName(String SbuName)
    {
    	sbuName = SbuName;
    }  
    public final String getNewsbuCode()
    {
        return newsbuCode;
    }

    public final void setNewsbuCode(String NewsbuCode)
    {
    	newsbuCode = NewsbuCode;
    }  
    public final String getOldsbuCode()
    {
        return oldsbuCode;
    }

    public final void setOldsbuCode(String OldsbuCode)
    {
    	oldsbuCode = OldsbuCode;
    }      
    public final String getLpo()
    {
        return lpo;
    }

    public final void setLpo(String Lpo)
    {
    	lpo = Lpo;
    }  
    
    public final String getInvoiceNo()
    {
        return invoiceNo;
    }
    public final void setInvoiceNo(String InvoiceNo)
    {
    	invoiceNo = InvoiceNo;
    }  
    
    public final String getposted()
    {
        return posted;
    }

    public final void setPosted(String Posted)
    {
    	posted = Posted;
    }  
    public final String getSupervisor()
    {
        return supervisor;
    }

    public final void setSupervisor(String Supervisor)
    {
    	supervisor = Supervisor;
    }  
    public final String getAssetId()
    {
        return assetId;
    }

    public final void setAssetId(String AssetId)
    {
    	assetId = AssetId;
    }
    public final String getOldassetId()
    {
        return oldassetId;
    }

    public final void setOldassetId(String OldassetId)
    {
    	oldassetId = OldassetId;  
    }    
    public final String getAssetCode()
    {
        return assetCode;
    }

    public final void setAssetCode(String AssetCode)
    {
    	assetCode = AssetCode;
    }
    public final String getSystemIp()
    {
        return systemIp;
    }

    public final void setSystemIp(String SystemIp)
    {
    	systemIp = SystemIp;
    }
    public final String getErrormessage()
    {
        return errormessage;
    }

    public final void setErrormessage(String Errormessage)
    {
    	errormessage = Errormessage;
    }
    public final String getAssetType()
    {
        return assetType;
    }

    public final void setAssetType(String AssetType)
    {
    	assetType = AssetType;
    }    
    public final String getTranType()
    {
        return tranType;
    }

    public final void setTranType(String TranType)
    {
    	tranType = TranType;
    }  
    public final int getNoofitems()
    {
        return noofitems;
    }

    public final void setNoofitems(int Noofitems)
    {
    	noofitems = Noofitems;
    }    
    public final String getMemovalue()
    {
        return memovalue;
    }

    public final void setMemovalue(String Memovalue)
    {
    	memovalue = Memovalue;
    }  
    public final String getLocation()
    {
        return location;
    }

    public final void setLocation(String Location)
    {
    	location = Location;
    } 
    public final String getSpare1()
    {
        return spare1;
    }

    public final void setSpare1(String Spare1)
    {
    	spare1 = Spare1;
    } 
    public final String getSpare2()
    {
        return spare2;
    }

    public final void setSpare2(String Spare2)
    {
    	spare2 = Spare2;
    }    
    
    public final String getSpare3()
    {
        return spare3;
    }

    public final void setSpare3(String Spare3)
    {
    	spare3 = Spare3;
    }  
    public final String getSpare4()
    {
        return spare4;
    }

    public final void setSpare4(String Spare4)
    {
    	spare4 = Spare4;
    }  
    public final String getSpare5()
    {
        return spare5;
    }

    public final void setSpare5(String Spare5)
    {
    	spare5 = Spare5;
    }  
    public final String getSpare6()
    {
        return spare6;
    }

    public final void setSpare6(String Spare6)
    {
    	spare6 = Spare6;
    }  
    
    public final String getMemo()
    {
        return memo;
    }

    public final void setMemo(String Memo)
    {
    	memo = Memo;
    }   
    public final String getMultiple()
    {
        return multiple;
    }

    public final void setMultiple(String Multiple)
    {
    	multiple = Multiple;
    }   
    public final int getUsefullife()
    {
        return usefullife;
    }

    public final void setUsefullife(int Usefullife)
    {
    	usefullife = Usefullife;
    }   
    public final int getImprovtotallife()
    {
        return improvtotallife;
    }

    public final void setImprovtotallife(int Improvtotallife)
    {
    	improvtotallife = Improvtotallife;
    }   
        
    public final String getProjectCode()
    {
        return projectCode;
    }

    public final void setProjectCode(String ProjectCode)
    {
    	projectCode = ProjectCode;
    }      
    
    public final String getAssetsighted()
    {
        return assetsighted;
    }

    public final void setAssetsighted(String Assetsighted)
    {
    	assetsighted = Assetsighted;
    } 
    
    public final String getAssetfunction()
    {
        return assetfunction;
    }

    public final void setAssetfunction(String Assetfunction)
    {
    	assetfunction = Assetfunction;
    }     
    
    public final String getSerialNo()
    {
        return serialNo;
    }

    public final void setSerialNo(String SerialNo)
    {
    	serialNo = SerialNo;
    }   
    
    public final String getEngineNo()
    {
        return engineNo;
    }

    public final void setEngineNo(String EngineNo)
    {
    	engineNo = EngineNo;
    } 
    
    public final String getVendorCode()
    {
        return vendorCode;
    }

    public final void setVendorCode(String VendorCode)
    {
    	vendorCode = VendorCode;
    }  
    
    public final String getVendorName()
    {
        return vendorName;
    }

    public final void setVendorName(String VendorName)
    {
    	vendorName = VendorName;
    }  
    
    public final String getComments()
    {
        return comments;
    }

    public final void setComments(String Comments)
    {
    	comments = Comments;
    } 
    
    public final int getCalcLifeSpan()
    {
        return calcLifeSpan;
    }

    public final void setCalcLifeSpan(int CalcLifeSpan)
    {
    	calcLifeSpan = CalcLifeSpan;
    }      
    
    public final double getDepRate()
    {
        return depRate;
    }

    public final void setDepRate(double DepRate)
    {
    	depRate = DepRate;
    }   
    public final int getRemainLife()
    {
        return remainLife;
    }

    public final void setRemainLife(int RemainLife)
    {
    	remainLife = RemainLife;
    }     
    public final int getImproveRemainLife()
    {
        return improveRemainLife;
    }

    public final void setImproveRemainLife(int ImproveRemainLife)
    {
    	improveRemainLife = ImproveRemainLife;
    }   
    
    public final String getBranchName()
    {
        return branchName;
    }

    public final void setBranchName(String BranchName)
    {
    	branchName = BranchName;
    } 
    
    public final String getDebitAccount()
    {
        return debitAccount;
    }

    public final void setDebitAccount(String DebitAccount)
    {
    	debitAccount = DebitAccount;
    } 
    
    public final String getCreditAccount()
    {
        return creditAccount;
    }

    public final void setCreditAccount(String CreditAccount)
    {
    	creditAccount = CreditAccount;
    } 
    
    public final String getDebitAccountName()
    {
        return debitAccountName;
    }

    public final void setDebitAccountName(String DebitAccountName)
    {
    	debitAccountName = DebitAccountName;
    } 
    
    public final String getCreditAccountName()
    {
        return creditAccountName;
    }

    public final void setCreditAccountName(String CreditAccountName)
    {
    	creditAccountName = CreditAccountName;
    } 
    
    public final String getInitiatorId()
    {
        return initiatorId;
    }

    public final void setInitiatorId(String InitiatorId)
    {
    	initiatorId = InitiatorId;
    } 
    
    public final String getSupervisorId()
    {
        return supervisorId;
    }

    public final void setSupervisorId(String SupervisorId)
    {
    	supervisorId = SupervisorId;
    } 
    
    public final String getPostedBy()
    {
        return postedBy;
    }

    public final void setPostedBy(String PostedBy)
    {
    	postedBy = PostedBy;
    } 
    
    public final String getTransDate()
    {
        return transDate;
    }

    public final void setTransDate(String TransDate)
    {
    	transDate = TransDate;
    } 

    public final double getAmount()
    {
        return amount;
    }

    public final void setAmount(double amount)
    {
        this.amount = amount;
    }    
    
    public final String getResponse()
    {
        return response;
    }

    public final void setResponse(String Response)
    {
    	response = Response;
    } 
    
    
    public final String getAction()
    {
        return action;
    }

    public final void setAction(String Action)
    {
    	action = Action;
    } 
    
    public final String getChargeYear()
    {
        return chargeYear;
    }

    public final void setChargeYear(String ChargeYear)
    {
    	chargeYear = ChargeYear;
    } 

    public final double getPrevMonthlyDep()
    {
        return prevMonthlyDep;
    }

    public final void setPrevMonthlyDep(double prevMonthlyDep)
    {
        this.prevMonthlyDep = prevMonthlyDep;
    }    

    public final double getPrevAccumDep()
    {
        return prevAccumDep;
    }

    public final void setPrevAccumDep(double prevAccumDep)
    {
        this.prevAccumDep = prevAccumDep;
    }    

    public final double getPrevCostPrice()
    {
        return prevCostPrice;
    }

    public final void setPrevCostPrice(double prevCostPrice)
    {
        this.prevCostPrice = prevCostPrice;
    }    

    public final double getPrevNBV()
    {
        return prevNBV;
    }

    public final void setPrevNBV(double prevNBV)
    {
        this.prevNBV = prevNBV;
    }    

    public final double getMonthlyDifference()
    {
        return monthlyDifference;
    }

    public final void setMonthlyDifference(double monthlyDifference)
    {
        this.monthlyDifference = monthlyDifference;
    }    

    public final double getAccumDifference()
    {
        return accumDifference;
    }

    public final void setAccumDifference(double accumDifference)
    {
        this.accumDifference = accumDifference;
    }    
    
    public final double getNbvDifference()
    {
        return nbvDifference;
    }

    public final void setNbvDifference(double nbvDifference)
    {
        this.nbvDifference = nbvDifference;
    }    

    public final double getPrevIMPROVMonthlyDep()
    {
        return prevIMPROVMonthlyDep;
    }

    public final void setPrevIMPROVMonthlyDep(double prevIMPROVMonthlyDep)
    {
        this.prevIMPROVMonthlyDep = prevIMPROVMonthlyDep;
    }    

    public final double getPrevIMPROVAccumDep()
    {
        return prevIMPROVAccumDep;
    }

    public final void setPrevIMPROVAccumDep(double prevIMPROVAccumDep)
    {
        this.prevIMPROVAccumDep = prevIMPROVAccumDep;
    }    

    public final double getPrevIMPROVCostPrice()
    {
        return prevIMPROVCostPrice;
    }

    public final void setPrevIMPROVCostPrice(double prevIMPROVCostPrice)
    {
        this.prevIMPROVCostPrice = prevIMPROVCostPrice;
    }    

    public final double getPrevIMPROVNBV()
    {
        return prevIMPROVNBV;
    }

    public final void setPrevIMPROVNBV(double prevIMPROVNBV)
    {
        this.prevIMPROVNBV = prevIMPROVNBV;
    }    

    public final double getPrevMonthlyDifference()
    {
        return prevMonthlyDifference;
    }

    public final void setPrevMonthlyDifference(double prevMonthlyDifference)
    {
        this.prevMonthlyDifference = prevMonthlyDifference;
    }    

    public final double getLifeSpan()
    {
        return lifeSpan;
    }

    public final void setLifeSpan(double lifeSpan)
    {
        this.lifeSpan = lifeSpan;
    }  
    
    public final String getDisposalDate()
    {
        return disposalDate;
    }

    public final void setDisposalDate(String DisposalDate)
    {
    	disposalDate = DisposalDate;
    } 
    
    public final String getDisposeReason()
    {
        return disposeReason;
    }

    public final void setDisposeReason(String DisposeReason)
    {
    	disposeReason = DisposeReason;
    } 
    
    public final String getQrCode()
    {
        return qrCode;
    }

    public final void setQrCode(String QrCode)
    {
    	qrCode = QrCode;
    } 

    public final double getProfitAmount()
    {
        return profitAmount;
    }

    public final void setProfitAmount(double profitAmount)
    {
        this.profitAmount = profitAmount;
    }  

    public final double getDisposalProceed()
    {
        return disposalProceed;
    }

    public final void setDisposalProceed(double disposalProceed)
    {
        this.disposalProceed = disposalProceed;
    }  

    public final double getDisposalAmount()
    {
        return disposalAmount;
    }

    public final void setDisposalAmount(double disposalAmount)
    {
        this.disposalAmount = disposalAmount;
    }

	public String getAccumDepLedger() {
		return accumDepLedger;
	}

	public void setAccumDepLedger(String accumDepLedger) {
		this.accumDepLedger = accumDepLedger;
	}

	public String getDepLedger() {
		return depLedger;
	}

	public void setDepLedger(String depLedger) {
		this.depLedger = depLedger;
	}

	public String getAssetLedger() {
		return assetLedger;
	}

	public void setAssetLedger(String assetLedger) {
		this.assetLedger = assetLedger;
	}

	public String getGlAccount() {
		return glAccount;
	}

	public void setGlAccount(String glAccount) {
		this.glAccount = glAccount;
	}

	public String getLegacyPostedDate() {
		return legacyPostedDate;
	}

	public void setLegacyPostedDate(String legacyPostedDate) {
		this.legacyPostedDate = legacyPostedDate;
	}

	public String getProcessingDate() {
		return processingDate;
	}

	public void setProcessingDate(String processingDate) {
		this.processingDate = processingDate;
	}

	public String getOldBranchId() {
		return oldBranchId;
	}

	public void setOldBranchId(String oldBranchId) {
		this.oldBranchId = oldBranchId;
	}

	public String getOldDeptId() {
		return oldDeptId;
	}

	public void setOldDeptId(String oldDeptId) {
		this.oldDeptId = oldDeptId;
	}

	public String getOldAssetUser() {
		return oldAssetUser;
	}

	public void setOldAssetUser(String oldAssetUser) {
		this.oldAssetUser = oldAssetUser;
	}

	public String getOldSection() {
		return oldSection;
	}

	public void setOldSection(String oldSection) {
		this.oldSection = oldSection;
	}

	public String getOldBranchCode() {
		return oldBranchCode;
	}

	public void setOldBranchCode(String oldBranchCode) {
		this.oldBranchCode = oldBranchCode;
	}

	public String getOldSectionCode() {
		return oldSectionCode;
	}

	public void setOldSectionCode(String oldSectionCode) {
		this.oldSectionCode = oldSectionCode;
	}

	public String getOldDeptCode() {
		return oldDeptCode;
	}

	public void setOldDeptCode(String oldDeptCode) {
		this.oldDeptCode = oldDeptCode;
	}

	public String getOldCategoryCode() {
		return oldCategoryCode;
	}

	public void setOldCategoryCode(String oldCategoryCode) {
		this.oldCategoryCode = oldCategoryCode;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public String getNewBranchCode() {
		return newBranchCode;
	}

	public void setNewBranchCode(String newBranchCode) {
		this.newBranchCode = newBranchCode;
	}

	public String getNewSectionCode() {
		return newSectionCode;
	}

	public void setNewSectionCode(String newSectionCode) {
		this.newSectionCode = newSectionCode;
	}

	public String getNewDeptCode() {
		return newDeptCode;
	}

	public void setNewDeptCode(String newDeptCode) {
		this.newDeptCode = newDeptCode;
	}

	public String getNewAssetId() {
		return newAssetId;
	}

	public void setNewAssetId(String newAssetId) {
		this.newAssetId = newAssetId;
	} 
	
	
      
}   
