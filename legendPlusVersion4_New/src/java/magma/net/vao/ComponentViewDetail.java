package magma.net.vao;


import java.io.Serializable;
import java.util.Date;
public class ComponentViewDetail implements Serializable {

    private String id;
    private String parentAssetId;
    private String parentCompId;
    private String assetId;
    private String category;
    private String description;
    private String serialNumber;
    private String make;
    private String model;
    private String additionalField;
    private String status;
    private String component;
    private double cost; 
    private double assigned; 
    private double depreciation;
    private String Registration_No;
    private int Branch_ID;
    private int Dept_ID;
    private int Section_id;
    private String Vendor_AC;
    private Date Date_purchased;
    private double Dep_Rate;
    private String Asset_Engine_No;
    private int Supplier_Name;
    private String Asset_User;
    private int Asset_Maintenance;
    private double Accum_Dep;
    private double Monthly_Dep;
    private double Cost_Price;
    private double NBV;
    private Date Dep_End_Date;
    private double Residual_Value;
    private String Authorized_By;
    private String Wh_Tax;
    private double Wh_Tax_Amount;
    private String Req_Redistribution;
    private Date Posting_Date;
    private Date Effective_Date;
    private String Purchase_Reason;
    private int Useful_Life;
    private int Total_Life;
    private int Location;
    private int Remaining_Life;
    private double Vatable_Cost;
    private double Vat;
    private String Req_Depreciation;
    private String Subject_TO_Vat;
    private String Who_TO_Rem;
    private String Email1;
    private String Who_To_Rem_2;
    private String Email2;
    private String Raise_Entry;
    private double Dep_Ytd;
    private String Section;
    private int State;
    private int Driver;
    private String Spare_2;
    private int User_ID;
    private Date Date_Disposed;
    private int PROVINCE;
    private String Multiple;
    private Date WAR_START_DATE;
    private int WAR_MONTH;
    private Date WAR_EXPIRY_DATE;
    private Date Last_Dep_Date;
    private String BRANCH_CODE;
    private String SECTION_CODE;
    private String DEPT_CODE;
    private String CATEGORY_CODE;
    private double AMOUNT_PTD;
    private double AMOUNT_REM;
    private String PART_PAY;
    private String FULLY_PAID;
    private int GROUP_ID;
    private String BAR_CODE;
    private String SBU_CODE;
    private String LPO;
    private String supervisor;
	public ComponentViewDetail(String id, String parentAssetId, String parentCompId, String assetId, String category, String description, String serialNumber, String make, String model, String additionalField, String status, String component, double cost, double assigned, double depreciation, String registration_No, int branch_ID, int dept_ID, int section_id, String vendor_AC, Date date_purchased, double dep_Rate, String asset_Engine_No, int supplier_Name, String asset_User, int asset_Maintenance, double accum_Dep, double monthly_Dep, double cost_Price, double nbv, Date dep_End_Date, double residual_Value, String authorized_By, String wh_Tax, double wh_Tax_Amount, String req_Redistribution, Date posting_Date, Date effective_Date, String purchase_Reason, int useful_Life, int total_Life, int location, int remaining_Life, double vatable_Cost, double vat, String req_Depreciation, String subject_TO_Vat, String who_TO_Rem, String email1, String who_To_Rem_2, String email2, String raise_Entry, double dep_Ytd, String section, int state, int driver, String spare_2, int user_ID, Date date_Disposed, int province, String multiple, Date war_start_date, int war_month, Date war_expiry_date, Date last_Dep_Date, String branch_code, String section_code, String dept_code, String category_code, double amount_ptd, double amount_rem, String part_pay, String fully_paid, int group_id, String bar_code, String sbu_code, String lpo, String supervisor) {
		super();
		this.id = id;
		this.parentAssetId = parentAssetId;
		this.parentCompId = parentCompId;
		this.assetId = assetId;
		this.category = category;
		this.description = description;
		this.serialNumber = serialNumber;
		this.make = make;
		this.model = model;
		this.additionalField = additionalField;
		this.status = status;
		this.component = component;
		this.cost = cost;
		this.assigned = assigned;
		this.depreciation = depreciation;
		Registration_No = registration_No;
		Branch_ID = branch_ID;
		Dept_ID = dept_ID;
		Section_id = section_id;
		Vendor_AC = vendor_AC;
		Date_purchased = date_purchased;
		Dep_Rate = dep_Rate;
		Asset_Engine_No = asset_Engine_No;
		Supplier_Name = supplier_Name;
		Asset_User = asset_User;
		Asset_Maintenance = asset_Maintenance;
		Accum_Dep = accum_Dep;
		Monthly_Dep = monthly_Dep;
		Cost_Price = cost_Price;
		NBV = nbv;
		Dep_End_Date = dep_End_Date;
		Residual_Value = residual_Value;
		Authorized_By = authorized_By;
		Wh_Tax = wh_Tax;
		Wh_Tax_Amount = wh_Tax_Amount;
		Req_Redistribution = req_Redistribution;
		Posting_Date = posting_Date;
		Effective_Date = effective_Date;
		Purchase_Reason = purchase_Reason;
		Useful_Life = useful_Life;
		Total_Life = total_Life;
		Location = location;
		Remaining_Life = remaining_Life;
		Vatable_Cost = vatable_Cost;
		Vat = vat;
		Req_Depreciation = req_Depreciation;
		Subject_TO_Vat = subject_TO_Vat;
		Who_TO_Rem = who_TO_Rem;
		Email1 = email1;
		Who_To_Rem_2 = who_To_Rem_2;
		Email2 = email2;
		Raise_Entry = raise_Entry;
		Dep_Ytd = dep_Ytd;
		Section = section;
		State = state;
		Driver = driver;
		Spare_2 = spare_2;
		User_ID = user_ID;
		Date_Disposed = date_Disposed;
		PROVINCE = province;
		Multiple = multiple;
		WAR_START_DATE = war_start_date;
		WAR_MONTH = war_month;
		WAR_EXPIRY_DATE = war_expiry_date;
		Last_Dep_Date = last_Dep_Date;
		BRANCH_CODE = branch_code;
		SECTION_CODE = section_code;
		DEPT_CODE = dept_code;
		CATEGORY_CODE = category_code;
		AMOUNT_PTD = amount_ptd;
		AMOUNT_REM = amount_rem;
		PART_PAY = part_pay;
		FULLY_PAID = fully_paid;
		GROUP_ID = group_id;
		BAR_CODE = bar_code;
		SBU_CODE = sbu_code;
		LPO = lpo;
		this.supervisor = supervisor;
	}
	public double getAccum_Dep() {
		return Accum_Dep;
	}
	public void setAccum_Dep(double accum_Dep) {
		Accum_Dep = accum_Dep;
	}
	public String getAdditionalField() {
		return additionalField;
	}
	public void setAdditionalField(String additionalField) {
		this.additionalField = additionalField;
	}
	public double getAMOUNT_PTD() {
		return AMOUNT_PTD;
	}
	public void setAMOUNT_PTD(double amount_ptd) {
		AMOUNT_PTD = amount_ptd;
	}
	public double getAMOUNT_REM() {
		return AMOUNT_REM;
	}
	public void setAMOUNT_REM(double amount_rem) {
		AMOUNT_REM = amount_rem;
	}
	public String getAsset_Engine_No() {
		return Asset_Engine_No;
	}
	public void setAsset_Engine_No(String asset_Engine_No) {
		Asset_Engine_No = asset_Engine_No;
	}
	public int getAsset_Maintenance() {
		return Asset_Maintenance;
	}
	public void setAsset_Maintenance(int asset_Maintenance) {
		Asset_Maintenance = asset_Maintenance;
	}
	public String getAsset_User() {
		return Asset_User;
	}
	public void setAsset_User(String asset_User) {
		Asset_User = asset_User;
	}
	public String getAssetId() {
		return assetId;
	}
	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}
	public double getAssigned() {
		return assigned;
	}
	public void setAssigned(double assigned) {
		this.assigned = assigned;
	}
	public String getAuthorized_By() {
		return Authorized_By;
	}
	public void setAuthorized_By(String authorized_By) {
		Authorized_By = authorized_By;
	}
	public String getBAR_CODE() {
		return BAR_CODE;
	}
	public void setBAR_CODE(String bar_code) {
		BAR_CODE = bar_code;
	}
	public String getBRANCH_CODE() {
		return BRANCH_CODE;
	}
	public void setBRANCH_CODE(String branch_code) {
		BRANCH_CODE = branch_code;
	}
	public int getBranch_ID() {
		return Branch_ID;
	}
	public void setBranch_ID(int branch_ID) {
		Branch_ID = branch_ID;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getCATEGORY_CODE() {
		return CATEGORY_CODE;
	}
	public void setCATEGORY_CODE(String category_code) {
		CATEGORY_CODE = category_code;
	}
	public String getComponent() {
		return component;
	}
	public void setComponent(String component) {
		this.component = component;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public double getCost_Price() {
		return Cost_Price;
	}
	public void setCost_Price(double cost_Price) {
		Cost_Price = cost_Price;
	}
	public Date getDate_Disposed() {
		return Date_Disposed;
	}
	public void setDate_Disposed(Date date_Disposed) {
		Date_Disposed = date_Disposed;
	}
	public Date getDate_purchased() {
		return Date_purchased;
	}
	public void setDate_purchased(Date date_purchased) {
		Date_purchased = date_purchased;
	}
	public Date getDep_End_Date() {
		return Dep_End_Date;
	}
	public void setDep_End_Date(Date dep_End_Date) {
		Dep_End_Date = dep_End_Date;
	}
	public double getDep_Rate() {
		return Dep_Rate;
	}
	public void setDep_Rate(double dep_Rate) {
		Dep_Rate = dep_Rate;
	}
	public double getDep_Ytd() {
		return Dep_Ytd;
	}
	public void setDep_Ytd(double dep_Ytd) {
		Dep_Ytd = dep_Ytd;
	}
	public double getDepreciation() {
		return depreciation;
	}
	public void setDepreciation(double depreciation) {
		this.depreciation = depreciation;
	}
	public String getDEPT_CODE() {
		return DEPT_CODE;
	}
	public void setDEPT_CODE(String dept_code) {
		DEPT_CODE = dept_code;
	}
	public int getDept_ID() {
		return Dept_ID;
	}
	public void setDept_ID(int dept_ID) {
		Dept_ID = dept_ID;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getDriver() {
		return Driver;
	}
	public void setDriver(int driver) {
		Driver = driver;
	}
	public Date getEffective_Date() {
		return Effective_Date;
	}
	public void setEffective_Date(Date effective_Date) {
		Effective_Date = effective_Date;
	}
	public String getEmail1() {
		return Email1;
	}
	public void setEmail1(String email1) {
		Email1 = email1;
	}
	public String getEmail2() {
		return Email2;
	}
	public void setEmail2(String email2) {
		Email2 = email2;
	}
	public String getFULLY_PAID() {
		return FULLY_PAID;
	}
	public void setFULLY_PAID(String fully_paid) {
		FULLY_PAID = fully_paid;
	}
	public int getGROUP_ID() {
		return GROUP_ID;
	}
	public void setGROUP_ID(int group_id) {
		GROUP_ID = group_id;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getLast_Dep_Date() {
		return Last_Dep_Date;
	}
	public void setLast_Dep_Date(Date last_Dep_Date) {
		Last_Dep_Date = last_Dep_Date;
	}
	public int getLocation() {
		return Location;
	}
	public void setLocation(int location) {
		Location = location;
	}
	public String getLPO() {
		return LPO;
	}
	public void setLPO(String lpo) {
		LPO = lpo;
	}
	public String getMake() {
		return make;
	}
	public void setMake(String make) {
		this.make = make;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public double getMonthly_Dep() {
		return Monthly_Dep;
	}
	public void setMonthly_Dep(double monthly_Dep) {
		Monthly_Dep = monthly_Dep;
	}
	public String getMultiple() {
		return Multiple;
	}
	public void setMultiple(String multiple) {
		Multiple = multiple;
	}
	public double getNBV() {
		return NBV;
	}
	public void setNBV(double nbv) {
		NBV = nbv;
	}
	public String getParentAssetId() {
		return parentAssetId;
	}
	public void setParentAssetId(String parentAssetId) {
		this.parentAssetId = parentAssetId;
	}
	public String getParentCompId() {
		return parentCompId;
	}
	public void setParentCompId(String parentCompId) {
		this.parentCompId = parentCompId;
	}
	public String getPART_PAY() {
		return PART_PAY;
	}
	public void setPART_PAY(String part_pay) {
		PART_PAY = part_pay;
	}
	public Date getPosting_Date() {
		return Posting_Date;
	}
	public void setPosting_Date(Date posting_Date) {
		Posting_Date = posting_Date;
	}
	public int getPROVINCE() {
		return PROVINCE;
	}
	public void setPROVINCE(int province) {
		PROVINCE = province;
	}
	public String getPurchase_Reason() {
		return Purchase_Reason;
	}
	public void setPurchase_Reason(String purchase_Reason) {
		Purchase_Reason = purchase_Reason;
	}
	public String getRaise_Entry() {
		return Raise_Entry;
	}
	public void setRaise_Entry(String raise_Entry) {
		Raise_Entry = raise_Entry;
	}
	public String getRegistration_No() {
		return Registration_No;
	}
	public void setRegistration_No(String registration_No) {
		Registration_No = registration_No;
	}
	public int getRemaining_Life() {
		return Remaining_Life;
	}
	public void setRemaining_Life(int remaining_Life) {
		Remaining_Life = remaining_Life;
	}
	public String getReq_Depreciation() {
		return Req_Depreciation;
	}
	public void setReq_Depreciation(String req_Depreciation) {
		Req_Depreciation = req_Depreciation;
	}
	public String getReq_Redistribution() {
		return Req_Redistribution;
	}
	public void setReq_Redistribution(String req_Redistribution) {
		Req_Redistribution = req_Redistribution;
	}
	public double getResidual_Value() {
		return Residual_Value;
	}
	public void setResidual_Value(double residual_Value) {
		Residual_Value = residual_Value;
	}
	public String getSBU_CODE() {
		return SBU_CODE;
	}
	public void setSBU_CODE(String sbu_code) {
		SBU_CODE = sbu_code;
	}
	public String getSection() {
		return Section;
	}
	public void setSection(String section) {
		Section = section;
	}
	public String getSECTION_CODE() {
		return SECTION_CODE;
	}
	public void setSECTION_CODE(String section_code) {
		SECTION_CODE = section_code;
	}
	public int getSection_id() {
		return Section_id;
	}
	public void setSection_id(int section_id) {
		Section_id = section_id;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getSpare_2() {
		return Spare_2;
	}
	public void setSpare_2(String spare_2) {
		Spare_2 = spare_2;
	}
	public int getState() {
		return State;
	}
	public void setState(int state) {
		State = state;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSubject_TO_Vat() {
		return Subject_TO_Vat;
	}
	public void setSubject_TO_Vat(String subject_TO_Vat) {
		Subject_TO_Vat = subject_TO_Vat;
	}
	public String getSupervisor() {
		return supervisor;
	}
	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}
	public int getSupplier_Name() {
		return Supplier_Name;
	}
	public void setSupplier_Name(int supplier_Name) {
		Supplier_Name = supplier_Name;
	}
	public int getTotal_Life() {
		return Total_Life;
	}
	public void setTotal_Life(int total_Life) {
		Total_Life = total_Life;
	}
	public int getUseful_Life() {
		return Useful_Life;
	}
	public void setUseful_Life(int useful_Life) {
		Useful_Life = useful_Life;
	}
	public int getUser_ID() {
		return User_ID;
	}
	public void setUser_ID(int user_ID) {
		User_ID = user_ID;
	}
	public double getVat() {
		return Vat;
	}
	public void setVat(double vat) {
		Vat = vat;
	}
	public double getVatable_Cost() {
		return Vatable_Cost;
	}
	public void setVatable_Cost(double vatable_Cost) {
		Vatable_Cost = vatable_Cost;
	}
	public String getVendor_AC() {
		return Vendor_AC;
	}
	public void setVendor_AC(String vendor_AC) {
		Vendor_AC = vendor_AC;
	}
	public Date getWAR_EXPIRY_DATE() {
		return WAR_EXPIRY_DATE;
	}
	public void setWAR_EXPIRY_DATE(Date war_expiry_date) {
		WAR_EXPIRY_DATE = war_expiry_date;
	}
	public int getWAR_MONTH() {
		return WAR_MONTH;
	}
	public void setWAR_MONTH(int war_month) {
		WAR_MONTH = war_month;
	}
	public Date getWAR_START_DATE() {
		return WAR_START_DATE;
	}
	public void setWAR_START_DATE(Date war_start_date) {
		WAR_START_DATE = war_start_date;
	}
	public String getWh_Tax() {
		return Wh_Tax;
	}
	public void setWh_Tax(String wh_Tax) {
		Wh_Tax = wh_Tax;
	}
	public double getWh_Tax_Amount() {
		return Wh_Tax_Amount;
	}
	public void setWh_Tax_Amount(double wh_Tax_Amount) {
		Wh_Tax_Amount = wh_Tax_Amount;
	}
	public String getWho_TO_Rem() {
		return Who_TO_Rem;
	}
	public void setWho_TO_Rem(String who_TO_Rem) {
		Who_TO_Rem = who_TO_Rem;
	}
	public String getWho_To_Rem_2() {
		return Who_To_Rem_2;
	}
	public void setWho_To_Rem_2(String who_To_Rem_2) {
		Who_To_Rem_2 = who_To_Rem_2;
	}
    
    
	
}