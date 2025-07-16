package com.magbel.ia.vao;

/**
 * <p>
 * Title: Company.java
 * </p>
 * 
 * <p>
 * Description: Company
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: Magbel Technologies LTD
 * </p>
 * 
 * @author Lanre O
 * @version 1.0
 */

public class Stock {

	   	private String inventory_id = "";
	    private String department_id = "";
	    private String category_id = "";
	    private String depreciation_start_date = null;
	    private String depreciation_end_date = null;
	    private String posting_date = null;
	    private String make = "";
	    private String location = "";
	    private String maintained_by = "";
	    private String authorized_by = "";
	    private String supplied_by = "";
	    private String reason = "";
	    private String aprovelevel = "";
	    private String description = "";
	    private String vendor_account = "";
	    private String cost_price = "0";
	    private String vatable_cost = "0";
	    private String vat_amount = "0";
	    private String serial_number = "";
	    private String engine_number = "";
	    private String model = "";
	    private String user = "";
	    private String depreciation_rate = "100";
	    private String residual_value = "0.00";
	    private String subject_to_vat = "";
	    private String date_of_purchase = null;
	    private String registration_no = "";
	    private String require_depreciation = "";
	    private String who_to_remind = "";
	    private String email_1 = "";
	    private String email2 = "";
	    private String raise_entry = "N";
	    private String section = "";
	    private String user_id = "1";
	    private String state = "";
	    private String driver = "";
	    private String who_to_remind_2 = "";
	    private String spare_1 = "";
	    private String spare_2 = "";
	    private double accum_dep = 0.0d;
	    private String section_id = "";
	    private String wh_tax_cb = "N";
	    private String wh_tax_amount = "0";
	    private String require_redistribution = "";
	    private String status = "PENDING";
	    private String province = "";
	    private String multiple = "N";
	    private String warrantyStartDate = "";
	    private String noOfMonths = "";
	    private String expiryDate = "";
	    private String amountPTD;
	    private String amountREM;
	    private String partPAY="N";
	    private String fullyPAID="N";
	    private String group_id; 
	    private String auth_user;
	    private String authuser; 
	    private String bar_code="";
	    private String sbu_code ="";
	    private String lpo ="";
	    private String supervisor ="";
	    private String deferPay ="N";
	    private int selectTax = 0;
	    private String supervisor2 ="";
	    private String supervisor3 ="";
	    private String supervisor4="";
	    private String supervisor5 =""; 
	    private String Item_TypeCode ="";
	    private String  Warehouse_Code ="";
	    private String  compCode ="";
	    private String branch_id="";

	public Stock() {
	}

	public Stock(
			String inventory_id, String department_id, String category_id,
			String depreciation_start_date, String depreciation_end_date,
			String posting_date, String make, String location,
			String maintained_by, String authorized_by, String supplied_by,
			String reason,String aprovelevel, String description,
			String vendor_account, String cost_price, String vatable_cost,
			String vat_amount, String serial_number, String engine_number,
			String model, String user, String depreciation_rate,
			String residual_value, String subject_to_vat,
			String date_of_purchase, String registration_no,
			String require_depreciation, String who_to_remind, String email_1,
			String email2, String raise_entry, String section, String user_id,
			String state, String driver, String who_to_remind_2,
			String spare_1, String spare_2, double accum_dep,
			String section_id, String wh_tax_cb, String wh_tax_amount,
			String require_redistribution, String status, String province,
			String multiple, String warrantyStartDate, String noOfMonths,
			String expiryDate, String amountPTD, String amountREM,
			String partPAY, String fullyPAID, String group_id,
			String auth_user, String authuser, String bar_code,
			String sbu_code, String lpo, String supervisor, String deferPay,
			int selectTax, String supervisor2, String supervisor3,
			String supervisor4, String supervisor5,String Item_TypeCode, 
			String  Warehouse_Code ,String  compCode,String branch_id) {
		super();
		this.inventory_id = inventory_id;
		this.department_id = department_id;
		this.category_id = category_id;
		this.depreciation_start_date = depreciation_start_date;
		this.depreciation_end_date = depreciation_end_date;
		this.posting_date = posting_date;
		this.make = make;
		this.location = location;
		this.maintained_by = maintained_by;
		this.authorized_by = authorized_by;
		this.supplied_by = supplied_by;
		this.reason = reason;
		this.aprovelevel = aprovelevel;
		this.description = description;
		this.vendor_account = vendor_account;
		this.cost_price = cost_price;
		this.vatable_cost = vatable_cost;
		this.vat_amount = vat_amount;
		this.serial_number = serial_number;
		this.engine_number = engine_number;
		this.model = model;
		this.user = user;
		this.depreciation_rate = depreciation_rate;
		this.residual_value = residual_value;
		this.subject_to_vat = subject_to_vat;
		this.date_of_purchase = date_of_purchase;
		this.registration_no = registration_no;
		this.require_depreciation = require_depreciation;
		this.who_to_remind = who_to_remind;
		this.email_1 = email_1;
		this.email2 = email2;
		this.raise_entry = raise_entry;
		this.section = section;
		this.user_id = user_id;
		this.state = state;
		this.driver = driver;
		this.who_to_remind_2 = who_to_remind_2;
		this.spare_1 = spare_1;
		this.spare_2 = spare_2;
		this.accum_dep = accum_dep;
		this.section_id = section_id;
		this.wh_tax_cb = wh_tax_cb;
		this.wh_tax_amount = wh_tax_amount;
		this.require_redistribution = require_redistribution;
		this.status = status;
		this.province = province;
		this.multiple = multiple;
		this.warrantyStartDate = warrantyStartDate;
		this.noOfMonths = noOfMonths;
		this.expiryDate = expiryDate;
		this.amountPTD = amountPTD;
		this.amountREM = amountREM;
		this.partPAY = partPAY;
		this.fullyPAID = fullyPAID;
		this.group_id = group_id;
		this.auth_user = auth_user;
		this.authuser = authuser; 
		this.bar_code = bar_code;
		this.sbu_code = sbu_code;
		this.lpo = lpo;
		this.supervisor = supervisor;
		this.deferPay = deferPay;
		this.selectTax = selectTax;
		this.supervisor2 = supervisor2;
		this.supervisor3 = supervisor3;
		this.supervisor4 = supervisor4;
		this.supervisor5 = supervisor5;
		this.Item_TypeCode =Item_TypeCode;
		this.Warehouse_Code =Warehouse_Code; 
		this.compCode =compCode;
		this.branch_id=branch_id;
	}

	public String getInventory_id() {
		return inventory_id;
	}

	public void setInventory_id(String inventory_id) {
		this.inventory_id = inventory_id;
	}

	public String getDepartment_id() {
		return department_id;
	}

	public void setDepartment_id(String department_id) {
		this.department_id = department_id;
	}

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public String getDepreciation_start_date() {
		return depreciation_start_date;
	}

	public void setDepreciation_start_date(String depreciation_start_date) {
		this.depreciation_start_date = depreciation_start_date;
	}

	public String getDepreciation_end_date() {
		return depreciation_end_date;
	}

	public void setDepreciation_end_date(String depreciation_end_date) {
		this.depreciation_end_date = depreciation_end_date;
	}

	public String getPosting_date() {
		return posting_date;
	}

	public void setPosting_date(String posting_date) {
		this.posting_date = posting_date;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getMaintained_by() {
		return maintained_by;
	}

	public void setMaintained_by(String maintained_by) {
		this.maintained_by = maintained_by;
	}

	public String getAuthorized_by() {
		return authorized_by;
	}

	public void setAuthorized_by(String authorized_by) {
		this.authorized_by = authorized_by;
	}

	public String getSupplied_by() {
		return supplied_by;
	}

	public void setSupplied_by(String supplied_by) {
		this.supplied_by = supplied_by;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getAprovelevel() {
		return aprovelevel;
	}

	public void setAprovelevel(String aprovelevel) {
		this.aprovelevel = aprovelevel;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVendor_account() {
		return vendor_account;
	}

	public void setVendor_account(String vendor_account) {
		this.vendor_account = vendor_account;
	}

	public String getCost_price() {
		return cost_price;
	}

	public void setCost_price(String cost_price) {
		this.cost_price = cost_price;
	}

	public String getVatable_cost() {
		return vatable_cost;
	}

	public void setVatable_cost(String vatable_cost) {
		this.vatable_cost = vatable_cost;
	}

	public String getVat_amount() {
		return vat_amount;
	}

	public void setVat_amount(String vat_amount) {
		this.vat_amount = vat_amount;
	}

	public String getSerial_number() {
		return serial_number;
	}

	public void setSerial_number(String serial_number) {
		this.serial_number = serial_number;
	}

	public String getEngine_number() {
		return engine_number;
	}

	public void setEngine_number(String engine_number) {
		this.engine_number = engine_number;
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

	public String getDepreciation_rate() {
		return depreciation_rate;
	}

	public void setDepreciation_rate(String depreciation_rate) {
		this.depreciation_rate = depreciation_rate;
	}

	public String getResidual_value() {
		return residual_value;
	}

	public void setResidual_value(String residual_value) {
		this.residual_value = residual_value;
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

	public String getRegistration_no() {
		return registration_no;
	}

	public void setRegistration_no(String registration_no) {
		this.registration_no = registration_no;
	}

	public String getRequire_depreciation() {
		return require_depreciation;
	}

	public void setRequire_depreciation(String require_depreciation) {
		this.require_depreciation = require_depreciation;
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

	public String getRaise_entry() {
		return raise_entry;
	}

	public void setRaise_entry(String raise_entry) {
		this.raise_entry = raise_entry;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
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

	public String getSection_id() {
		return section_id;
	}

	public void setSection_id(String section_id) {
		this.section_id = section_id;
	}

	public String getWh_tax_cb() {
		return wh_tax_cb;
	}

	public void setWh_tax_cb(String wh_tax_cb) {
		this.wh_tax_cb = wh_tax_cb;
	}

	public String getWh_tax_amount() {
		return wh_tax_amount;
	}

	public void setWh_tax_amount(String wh_tax_amount) {
		this.wh_tax_amount = wh_tax_amount;
	}

	public String getRequire_redistribution() {
		return require_redistribution;
	}

	public void setRequire_redistribution(String require_redistribution) {
		this.require_redistribution = require_redistribution;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getAmountPTD() {
		return amountPTD;
	}

	public void setAmountPTD(String amountPTD) {
		this.amountPTD = amountPTD;
	}

	public String getAmountREM() {
		return amountREM;
	}

	public void setAmountREM(String amountREM) {
		this.amountREM = amountREM;
	}

	public String getPartPAY() {
		return partPAY;
	}

	public void setPartPAY(String partPAY) {
		this.partPAY = partPAY;
	}

	public String getFullyPAID() {
		return fullyPAID;
	}

	public void setFullyPAID(String fullyPAID) {
		this.fullyPAID = fullyPAID;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getAuth_user() {
		return auth_user;
	}

	public void setAuth_user(String auth_user) {
		this.auth_user = auth_user;
	}

	public String getAuthuser() {
		return authuser;
	}

	public void setAuthuser(String authuser) {
		this.authuser = authuser;
	}

 

	public String getBar_code() {
		return bar_code;
	}

	public void setBar_code(String bar_code) {
		this.bar_code = bar_code;
	}

	public String getSbu_code() {
		return sbu_code;
	}

	public void setSbu_code(String sbu_code) {
		this.sbu_code = sbu_code;
	}

	public String getLpo() {
		return lpo;
	}

	public void setLpo(String lpo) {
		this.lpo = lpo;
	}

	public String getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}

	public String getDeferPay() {
		return deferPay;
	}

	public void setDeferPay(String deferPay) {
		this.deferPay = deferPay;
	}

	public int getSelectTax() {
		return selectTax;
	}

	public void setSelectTax(int selectTax) {
		this.selectTax = selectTax;
	}

	public String getSupervisor2() {
		return supervisor2;
	}

	public void setSupervisor2(String supervisor2) {
		this.supervisor2 = supervisor2;
	}

	public String getSupervisor3() {
		return supervisor3;
	}

	public void setSupervisor3(String supervisor3) {
		this.supervisor3 = supervisor3;
	}

	public String getSupervisor4() {
		return supervisor4;
	}

	public void setSupervisor4(String supervisor4) {
		this.supervisor4 = supervisor4;
	}

	public String getSupervisor5() {
		return supervisor5;
	}

	public void setSupervisor5(String supervisor5) {
		this.supervisor5 = supervisor5;
	}
 
	public String getItem_TypeCode() {
		return Item_TypeCode;
	}

	public void setItem_TypeCode(String Item_TypeCode) {
		this.Item_TypeCode = Item_TypeCode;
	}

	public String getWarehouse_Code() {
		return Warehouse_Code;
	}

	public void setWarehouse_Code(String Warehouse_Code) {
		this.Warehouse_Code = Warehouse_Code;
	}

	public String getCompCode() {
		return compCode;
	}

	public void setCompCode(String compCode) {
		this.compCode = compCode;
	}
	
	public String getBranch_id() {
		return branch_id;
	}

	public void setBranch_id(String branch_id) {
		this.branch_id = branch_id;
	}
	
}
