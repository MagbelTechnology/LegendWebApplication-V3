package magma.net.vao;

import java.io.Serializable;
import java.util.Date;

import magma.net.dao.MagmaDBConnection;
import magma.util.Codes;

import com.magbel.util.DatetimeFormat;

/**
 * <p>Title: fileName.java</p>
 *
 * <p>Description: File Description</p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * <p>Company: Magbel Technologies LTD</p>
 *
 * @author Ayojava
 * @version 1.0
 */
public class Inventory implements Serializable 
{

    private String id;
    private String registrationNo;
    private String branchId;
    private String departmentId;
    private String sectionID;
    private String categoryID;
    private String description;
    private String datePurchased;
    private double depreciationRate;
    private String Stock_Make;
    private String stock_User;
    private String stock_status;
    private String stock_Maintenance;
    private double accumulatedDepreciation;
    private double monthlyDepreciation;
    private double cost;
    private double vatablecost;
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
    private String authorized_by = "";
    private String sbuCode;
    private String barCode;
    private String spare1;
    private String spare2;
    private String lpoNum;
    private String wareHouse_Code;
    private String itemtype;
    private String stateID;
    private String locationID;
    private String subjectToVat;
    private String reason = "";
    private String model = "";
    private String user = "";
    private String subject_to_vat = "";
    private String date_of_purchase = null;
    private String who_to_remind = "";
    private String email_1 = "";
    private String email2 = "";
    private String who_to_remind_2 = "";
    private String spare_1 = "";
    private String spare_2 = "";
    private double accum_dep = 0.0d;
     private String wh_tax_amount = "0";
    private String province = "";
    private String multiple = "N";
    private String warrantyStartDate = "";
    private String noOfMonths = "";
    private String expiryDate = "";
    private String supervisor ="";
    private int selectTax = 0;
    private String systemIp;
    private String macAddress;
    private String vat_amount ;
    private String wh_tax_cb ;
    private String Supplier_ID;
    private String remark;
    private 	String remarkDate;

 public String getRemarkDate() {
		return remarkDate;
	}

	public void setRemarkDate(String remarkDate) {
		this.remarkDate = remarkDate;
	}

public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

public String getSupplier_ID() {
		return Supplier_ID;
	}

	public void setSupplier_ID(String supplier_ID) {
		Supplier_ID = supplier_ID;
	}

public String getWh_tax_cb() {
		return wh_tax_cb;
	}

	public void setWh_tax_cb(String wh_tax_cb) {
		this.wh_tax_cb = wh_tax_cb;
	}

public String getVat_amount() {
		return vat_amount;
	}

	public void setVat_amount(String vat_amount) {
		this.vat_amount = vat_amount;
	}

public Inventory()
 {
 }

public Inventory(String id, String registrationNo, String branchId,
		String departmentId, String sectionID, String categoryID,
		String description, String datePurchased, double depreciationRate,
		String stock_Make, String stock_User, String stock_status,
		String stock_Maintenance, double accumulatedDepreciation,
		double monthlyDepreciation, double cost, double vatablecost,
		String depreciationEndDate, double residualValue, String postingDate,
		String entryRaised, double depreciationYearToDate, double nbv,
		int remainingLife, int totalLife, Date effectiveDate,
		String isFleetEnabled, String classifyDate, String reclassify_id,
		String vendorAc, String serialNo, String engineNo, int supplierName,
		int assetMaintain, String authorized_by, String sbuCode,
		String barCode, String spare1, String spare2, String lpoNum,
		String wareHouse_Code, String itemtype, String stateID,
		String locationID, String subjectToVat, String reason, String model,
		String user, String subject_to_vat, String date_of_purchase,
		String who_to_remind, String email_1, String email2,
		String who_to_remind_2, String spare_1, String spare_2,
		double accum_dep, String wh_tax_amount, String province,
		String multiple, String warrantyStartDate, String noOfMonths,
		String expiryDate, String supervisor, int selectTax, String systemIp,
		String macAddress) {
	super();
	this.id = id;
	this.registrationNo = registrationNo;
	this.branchId = branchId;
	this.departmentId = departmentId;
	this.sectionID = sectionID;
	this.categoryID = categoryID;
	this.description = description;
	this.datePurchased = datePurchased;
	this.depreciationRate = depreciationRate;
	Stock_Make = stock_Make;
	this.stock_User = stock_User;
	this.stock_status = stock_status;
	this.stock_Maintenance = stock_Maintenance;
	this.accumulatedDepreciation = accumulatedDepreciation;
	this.monthlyDepreciation = monthlyDepreciation;
	this.cost = cost;
	this.vatablecost = vatablecost;
	this.depreciationEndDate = depreciationEndDate;
	this.residualValue = residualValue;
	this.postingDate = postingDate;
	this.entryRaised = entryRaised;
	this.depreciationYearToDate = depreciationYearToDate;
	this.nbv = nbv;
	this.remainingLife = remainingLife;
	this.totalLife = totalLife;
	this.effectiveDate = effectiveDate;
	this.isFleetEnabled = isFleetEnabled;
	this.classifyDate = classifyDate;
	this.reclassify_id = reclassify_id;
	this.vendorAc = vendorAc;
	this.serialNo = serialNo;
	this.engineNo = engineNo;
	this.supplierName = supplierName;
	this.assetMaintain = assetMaintain;
	this.authorized_by = authorized_by;
	this.sbuCode = sbuCode;
	this.barCode = barCode;
	this.spare1 = spare1;
	this.spare2 = spare2;
	this.lpoNum = lpoNum;
	this.wareHouse_Code = wareHouse_Code;
	this.itemtype = itemtype;
	this.stateID = stateID;
	this.locationID = locationID;
	this.subjectToVat = subjectToVat;
	this.reason = reason;
	this.model = model;
	this.user = user;
	this.subject_to_vat = subject_to_vat;
	this.date_of_purchase = date_of_purchase;
	this.who_to_remind = who_to_remind;
	this.email_1 = email_1;
	this.email2 = email2;
	this.who_to_remind_2 = who_to_remind_2;
	this.spare_1 = spare_1;
	this.spare_2 = spare_2;
	this.accum_dep = accum_dep;
	this.wh_tax_amount = wh_tax_amount;
	this.province = province;
	this.multiple = multiple;
	this.warrantyStartDate = warrantyStartDate;
	this.noOfMonths = noOfMonths;
	this.expiryDate = expiryDate;
	this.supervisor = supervisor;
	this.selectTax = selectTax;
	this.systemIp = systemIp;
	this.macAddress = macAddress;
}

public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
}

public String getRegistrationNo() {
	return registrationNo;
}

public void setRegistrationNo(String registrationNo) {
	this.registrationNo = registrationNo;
}

public String getBranchId() {
	return branchId;
}

public void setBranchId(String branchId) {
	this.branchId = branchId;
}

public String getDepartmentId() {
	return departmentId;
}

public void setDepartmentId(String departmentId) {
	this.departmentId = departmentId;
}

public String getSectionID() {
	return sectionID;
}

public void setSectionID(String sectionID) {
	this.sectionID = sectionID;
}

public String getCategoryID() {
	return categoryID;
}

public void setCategoryID(String categoryID) {
	this.categoryID = categoryID;
}

public String getDescription() {
	return description;
}

public void setDescription(String description) {
	this.description = description;
}

public String getDatePurchased() {
	return datePurchased;
}

public void setDatePurchased(String datePurchased) {
	this.datePurchased = datePurchased;
}

public double getDepreciationRate() {
	return depreciationRate;
}

public void setDepreciationRate(double depreciationRate) {
	this.depreciationRate = depreciationRate;
}

public String getStock_Make() {
	return Stock_Make;
}

public void setStock_Make(String stock_Make) {
	Stock_Make = stock_Make;
}

public String getStock_User() {
	return stock_User;
}

public void setStock_User(String stock_User) {
	this.stock_User = stock_User;
}

public String getStock_status() {
	return stock_status;
}

public void setStock_status(String stock_status) {
	this.stock_status = stock_status;
}

public String getStock_Maintenance() {
	return stock_Maintenance;
}

public void setStock_Maintenance(String stock_Maintenance) {
	this.stock_Maintenance = stock_Maintenance;
}

public double getAccumulatedDepreciation() {
	return accumulatedDepreciation;
}

public void setAccumulatedDepreciation(double accumulatedDepreciation) {
	this.accumulatedDepreciation = accumulatedDepreciation;
}

public double getMonthlyDepreciation() {
	return monthlyDepreciation;
}

public void setMonthlyDepreciation(double monthlyDepreciation) {
	this.monthlyDepreciation = monthlyDepreciation;
}

public double getCost() {
	return cost;
}

public void setCost(double cost) {
	this.cost = cost;
}

public double getVatablecost() {
	return vatablecost;
}

public void setVatablecost(double vatablecost) {
	this.vatablecost = vatablecost;
}

public String getDepreciationEndDate() {
	return depreciationEndDate;
}

public void setDepreciationEndDate(String depreciationEndDate) {
	this.depreciationEndDate = depreciationEndDate;
}

public double getResidualValue() {
	return residualValue;
}

public void setResidualValue(double residualValue) {
	this.residualValue = residualValue;
}

public String getPostingDate() {
	return postingDate;
}

public void setPostingDate(String postingDate) {
	this.postingDate = postingDate;
}

public String getEntryRaised() {
	return entryRaised;
}

public void setEntryRaised(String entryRaised) {
	this.entryRaised = entryRaised;
}

public double getDepreciationYearToDate() {
	return depreciationYearToDate;
}

public void setDepreciationYearToDate(double depreciationYearToDate) {
	this.depreciationYearToDate = depreciationYearToDate;
}

public double getNbv() {
	return nbv;
}

public void setNbv(double nbv) {
	this.nbv = nbv;
}

public int getRemainingLife() {
	return remainingLife;
}

public void setRemainingLife(int remainingLife) {
	this.remainingLife = remainingLife;
}

public int getTotalLife() {
	return totalLife;
}

public void setTotalLife(int totalLife) {
	this.totalLife = totalLife;
}

public Date getEffectiveDate() {
	return effectiveDate;
}

public void setEffectiveDate(Date effectiveDate) {
	this.effectiveDate = effectiveDate;
}

public String getIsFleetEnabled() {
	return isFleetEnabled;
}

public void setIsFleetEnabled(String isFleetEnabled) {
	this.isFleetEnabled = isFleetEnabled;
}

public String getClassifyDate() {
	return classifyDate;
}

public void setClassifyDate(String classifyDate) {
	this.classifyDate = classifyDate;
}

public String getReclassify_id() {
	return reclassify_id;
}

public void setReclassify_id(String reclassify_id) {
	this.reclassify_id = reclassify_id;
}

public String getVendorAc() {
	return vendorAc;
}

public void setVendorAc(String vendorAc) {
	this.vendorAc = vendorAc;
}

public String getSerialNo() {
	return serialNo;
}

public void setSerialNo(String serialNo) {
	this.serialNo = serialNo;
}

public String getEngineNo() {
	return engineNo;
}

public void setEngineNo(String engineNo) {
	this.engineNo = engineNo;
}

public int getSupplierName() {
	return supplierName;
}

public void setSupplierName(int supplierName) {
	this.supplierName = supplierName;
}

public int getAssetMaintain() {
	return assetMaintain;
}

public void setAssetMaintain(int assetMaintain) {
	this.assetMaintain = assetMaintain;
}

public String getAuthorized_by() {
	return authorized_by;
}

public void setAuthorized_by(String authorized_by) {
	this.authorized_by = authorized_by;
}

public String getSbuCode() {
	return sbuCode;
}

public void setSbuCode(String sbuCode) {
	this.sbuCode = sbuCode;
}

public String getBarCode() {
	return barCode;
}

public void setBarCode(String barCode) {
	this.barCode = barCode;
}

public String getSpare1() {
	return spare1;
}

public void setSpare1(String spare1) {
	this.spare1 = spare1;
}

public String getSpare2() {
	return spare2;
}

public void setSpare2(String spare2) {
	this.spare2 = spare2;
}

public String getLpoNum() {
	return lpoNum;
}

public void setLpoNum(String lpoNum) {
	this.lpoNum = lpoNum;
}

public String getWareHouse_Code() {
	return wareHouse_Code;
}

public void setWareHouse_Code(String wareHouse_Code) {
	this.wareHouse_Code = wareHouse_Code;
}

public String getItemtype() {
	return itemtype;
}

public void setItemtype(String itemtype) {
	this.itemtype = itemtype;
}

public String getStateID() {
	return stateID;
}

public void setStateID(String stateID) {
	this.stateID = stateID;
}

public String getLocationID() {
	return locationID;
}

public void setLocationID(String locationID) {
	this.locationID = locationID;
}

public String getSubjectToVat() {
	return subjectToVat;
}

public void setSubjectToVat(String subjectToVat) {
	this.subjectToVat = subjectToVat;
}

public String getReason() {
	return reason;
}

public void setReason(String reason) {
	this.reason = reason;
}

public String getModel() {
	return model;
}

public void setModel(String model) {
	this.model = model;
}

public String getUser() {
	return user;
}

public void setUser(String user) {
	this.user = user;
}

public String getSubject_to_vat() {
	return subject_to_vat;
}

public void setSubject_to_vat(String subject_to_vat) {
	this.subject_to_vat = subject_to_vat;
}

public String getDate_of_purchase() {
	return date_of_purchase;
}

public void setDate_of_purchase(String date_of_purchase) {
	this.date_of_purchase = date_of_purchase;
}

public String getWho_to_remind() {
	return who_to_remind;
}

public void setWho_to_remind(String who_to_remind) {
	this.who_to_remind = who_to_remind;
}

public String getEmail_1() {
	return email_1;
}

public void setEmail_1(String email_1) {
	this.email_1 = email_1;
}

public String getEmail2() {
	return email2;
}

public void setEmail2(String email2) {
	this.email2 = email2;
}

public String getWho_to_remind_2() {
	return who_to_remind_2;
}

public void setWho_to_remind_2(String who_to_remind_2) {
	this.who_to_remind_2 = who_to_remind_2;
}

public String getSpare_1() {
	return spare_1;
}

public void setSpare_1(String spare_1) {
	this.spare_1 = spare_1;
}

public String getSpare_2() {
	return spare_2;
}

public void setSpare_2(String spare_2) {
	this.spare_2 = spare_2;
}

public double getAccum_dep() {
	return accum_dep;
}

public void setAccum_dep(double accum_dep) {
	this.accum_dep = accum_dep;
}

public String getWh_tax_amount() {
	return wh_tax_amount;
}

public void setWh_tax_amount(String wh_tax_amount) {
	this.wh_tax_amount = wh_tax_amount;
}

public String getProvince() {
	return province;
}

public void setProvince(String province) {
	this.province = province;
}

public String getMultiple() {
	return multiple;
}

public void setMultiple(String multiple) {
	this.multiple = multiple;
}

public String getWarrantyStartDate() {
	return warrantyStartDate;
}

public void setWarrantyStartDate(String warrantyStartDate) {
	this.warrantyStartDate = warrantyStartDate;
}

public String getNoOfMonths() {
	return noOfMonths;
}

public void setNoOfMonths(String noOfMonths) {
	this.noOfMonths = noOfMonths;
}

public String getExpiryDate() {
	return expiryDate;
}

public void setExpiryDate(String expiryDate) {
	this.expiryDate = expiryDate;
}

public String getSupervisor() {
	return supervisor;
}

public void setSupervisor(String supervisor) {
	this.supervisor = supervisor;
}

public int getSelectTax() {
	return selectTax;
}

public void setSelectTax(int selectTax) {
	this.selectTax = selectTax;
}

public String getSystemIp() {
	return systemIp;
}

public void setSystemIp(String systemIp) {
	this.systemIp = systemIp;
}

public String getMacAddress() {
	return macAddress;
}

public void setMacAddress(String macAddress) {
	this.macAddress = macAddress;
}

    
}
