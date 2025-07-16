package magma.net.vao;

import java.io.Serializable;
import java.util.Calendar;

import magma.DateManipulations;

/**
 * <p>Title: GroupAsset.java</p>
 *
 * <p>Description: Holds details of a Group Asset.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Magbel Technologies LTD</p>
 *
 * @author Jejelowo.B.Festus
 * @version 1.0
 */
public class GroupAsset implements Serializable {

    private String id;
    private String registrationNo;
    private String description;
    private String branchCode;
    private String sectionCode;
    private String deptCode;
    private String category;
    private double costPrice;
    private double nbv;  
    private String dateCreated;
    private double accumulatedDepreciation;
    private String depreciationEndDate;
    private String user_id;
    private String make;
    private String model;
    private String tax;
    private String taxAmount;
    private String maintainBy;
    private String vat;
    private double vatableCost;
    private String process_flag;
    private String sbu_code;
    private String post_flag;
    private String reject_reason;
    private String asset_code;
    private int quantity=0;
    private String warehouseCode = "";
    private String itemCode = "";
    private String itemType = "";
    private String unitCode = "";
    private double oldcost_price;
    private String groupId;
    private String location;
    private String projectCode;
    private String vendorId;
    private String vendorAccount;
    private String glAccount;
    private String assetId;
    private String existBranchCode;
    private String bar_code;
    private String nextDateObtained;
    private String firstDateObtained;
    private String postingDate;
    private String license;
    private String transType;
    private String branchId;
    private String status;
    private String spare1;
    private String spare2;
    private String spare3;
    private String spare4;
    private String spare5;
    private String spare6;
    private String subcategory_code;
    private String lpo_no;
    private String branch_code;
    private String department_code;
    private String section_code;
    private String asset_id;
    private String category_code;
    private String department_id;
    private String section_id;
    private String category_id;
    private String subcategory_id;
    private String state;
    private String serial_number;
    private String engine_number;
    private String vendor_account;
    private String date_of_purchase;
    private String depreciation_start_date;
    private String depreciation_end_date;
    
    public GroupAsset(){
    	
    }
    public String getReject_reason() {
		return reject_reason;
	}

	public void setReject_reason(String reject_reason) {
		this.reject_reason = reject_reason;
	}

	public String getPost_flag() {
		return post_flag;
	}

	public void setPost_flag(String post_flag) {
		this.post_flag = post_flag;
	}

	public String getSbu_code() {
		return sbu_code;
	}
	public void setSbu_code(String sbu_code) {
		this.sbu_code = sbu_code;
	}
	
	public String getAsset_code() {
		return asset_code;
	}	  
	
	public void setAsset_code(String asset_code) {
		this.asset_code = asset_code;
	}	
	public GroupAsset(String id, String registrationNo, String description,
                      String branchCode, String deptCode, String sectionCode,
                      String category, double costPrice, String dateCreated,
                      double accumulatedDepreciation,
                      String depreciationEndDate,
                      String make, String model, String tax, String taxAmount,
                      String maintainBy, String vat, double vatableCost) {

        setId(id);
        setRegistrationNo(registrationNo);
        setDescription(description);
        setBranchCode(branchCode);
        setSectionCode(sectionCode);
        setDeptCode(deptCode);
        setCategory(category);
        setCostPrice(costPrice);
        setDateCreated(dateCreated);
        setAccumulatedDepreciation(accumulatedDepreciation);
        setDepreciationEndDate(depreciationEndDate);
        setMake(make);
        setModel(model);
        setTax(tax);
        setTaxAmount(taxAmount);
        setMaintainBy(maintainBy);
        setVat(vat);
        setVatableCost(vatableCost);

    }

    public String getProcess_flag() {
		return process_flag;
	}

	public void setProcess_flag(String process_flag) {
		this.process_flag = process_flag;
	}

	public void setId(String id) {
        this.id = id;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public void setSectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }

    public void setNbv(double nbv) {
        this.nbv = nbv;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setAccumulatedDepreciation(double accumulatedDepreciation) {
        this.accumulatedDepreciation = accumulatedDepreciation;
    }

    public void setDepreciationEndDate(String depreciationEndDate) {
        this.depreciationEndDate = depreciationEndDate;
    }

    public void setNextDateObtained(String nextDateObtained) {
        this.nextDateObtained = nextDateObtained;
    }

    public void setFirstDateObtained(String firstDateObtained) {
        this.firstDateObtained = firstDateObtained;
    }

    public void setPostingDate(String postingDate) {
        this.postingDate = postingDate;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }
    public void setStatus(String status) {
        this.status = status;
    }    
    public void setMake(String make) {
        this.make = make;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }

    public void setMaintainBy(String maintainBy) {
        this.maintainBy = maintainBy;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public void setVatableCost(double vatableCost) {
        this.vatableCost = vatableCost;
    }

    public String getId() {
        return id;
    } 

    public String getRegistrationNo() {
        return registrationNo;
    }

    public String getDescription() {
        return description;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public String getCategory() {
        return this.category;
    }

    public String getSectionCode() {
        return this.sectionCode;
    }

    public String setDeptCode() {
        return this.deptCode;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public double getNbv() {
        return nbv;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public double getAccumulatedDepreciation() {
        return accumulatedDepreciation;
    }

    public String getDepreciationEndDate() {
        return depreciationEndDate;
    }

    public String getNextDateObtained() {
        return nextDateObtained;
    }

    public String getFirstDateObtained() {
        return firstDateObtained;
    }

    public String getPostingDate() {
        return postingDate;
    }

    public String getLicense() {
        return license;
    }

    public String getTransType() {
        return transType;
    }

    public String getBranchId() {
        return branchId;
    }    
    public String getStatus() {
        return status;
    }
    
    public String getMake() {
        return this.make;
    }

    public String getModel() {
        return this.model;
    }

    public String getTax() {
        return this.tax;
    }

    public String getTaxAmount() {
        return this.taxAmount;
    }

    public String getMaintainBy() {
        return this.maintainBy;
    }

    public String getVat() {
        return this.vat = vat;
    }

    public double getVatableCost() {
        return this.vatableCost;
    }

	/**
	 * @return the user_id
	 */
	public String getUser_id() {
		return user_id;
	}

	/**
	 * @param user_id the user_id to set
	 */
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	 
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }    
        
    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }
    
    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    
    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
    
    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }  

    public double getOldcost_price()
    {
        return oldcost_price;
    }
    public void setOldcost_price(double oldcost_price)
    {
        this.oldcost_price = oldcost_price;
    }  

    public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

    public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

    public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

    public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

    public String getVendorAccount() {
		return vendorAccount;
	}

	public void setVendorAccount(String vendorAccount) {
		this.vendorAccount = vendorAccount;
	}

    public String getGlAccount() {
		return glAccount;
	}

	public void setGlAccount(String glAccount) {
		this.glAccount = glAccount;
	}

    public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}
    
    public String getExistBranchCode()
    {
        return existBranchCode;
    }
    public void setExistBranchCode(String existBranchCode)
    {
        this.existBranchCode = existBranchCode;
    }   
    public void setBar_code(String bar_code)
    {
        if(bar_code != null)
        {
            this.bar_code = bar_code;
        }
    } 

    public String getBar_code()
    {
        return bar_code;
    }
    
    public void setSpare1(String spare1)
    {
        if(spare1 != null)
        {
            this.spare1 = spare1;
        }
    }
    public String getSpare1()
    {
        return spare1;
    }    
    
    public void setSpare2(String spare2)
    {
        if(spare2 != null)
        {
            this.spare2 = spare2;
        }
    }
    public String getSpare2()
    {
        return spare2;
    }    
    
    
    public void setSpare3(String spare3)
    {
        if(spare3 != null)
        {
            this.spare3 = spare3;
        }
    }
    public String getSpare3()
    {
        return spare3;
    }    
    
    public void setSpare4(String spare4)
    {
        if(spare4 != null)
        {
            this.spare1 = spare4;
        }
    }
    public String getSpare4()
    {
        return spare4;
    }    
    
    public void setSpare5(String spare5)
    {
        if(spare5 != null)
        {
            this.spare5 = spare5;
        }
    }
    public String getSpare5()
    {
        return spare5;
    }    
    
    public void setSpare6(String spare6)
    {
        if(spare6 != null)
        {
            this.spare6 = spare6;
        }
    }
    public String getSpare6()
    {
        return spare6;
    }    
    public String getBranch_code()
    {
        return branch_code;
    }
    
    public String getLpo_no()
    {
        return lpo_no;
    }
    
    public String getSubcategory_code()
    {
        return subcategory_code;
    }
    
    public String getDepartment_code()
    {
        return department_code;
    }
    
    public String getSection_code()
    {
        return section_code;
    }
           
    public void setSubCategory_code(String subcategory_code)
    {
        if(subcategory_code != null)
        {
            this.subcategory_code = subcategory_code;
        }
    }
    
    public String getAsset_id()
    {
        return asset_id;
    }
    public void setAsset_id(String asset_id)
    {
        this.asset_id = asset_id;
    }     
    public void setLpo_no(String lpo_no)
    {
        if(lpo_no != null)
        {
            this.lpo_no = lpo_no;
        }
    }
    public void setBranch_code(String branch_code)
    {
        if(branch_code != null)
        {
            this.branch_code = branch_code;
        }
    }
    
    public void setDepartment_code(String department_code)
    {
        if(department_code != null)
        {
            this.department_code = department_code;
        }
    }
    public void setSection_code(String section_code)
    {
        if(section_code != null)
        {
            this.section_code = section_code;
        }
    }
  
    public void setCategory_code(String category_code)
    {
        if(category_code != null)
        {
            this.category_code = category_code;
        }
    }
    public void setSubCategory_id(String subcategory_id)
    {
        if(subcategory_id != null)
        {
            this.subcategory_id = subcategory_id;
        }
    }

    public void setDepartment_id(String department_id)
    {
        if(department_id != null)
        {
            this.department_id = department_id;
        }
    }

    public void setSection_id(String section_id)
    {
        if(section_id != null)
        {
            this.section_id = section_id;
        }
    }

    public void setState(String state)
    {
        if(state != null)
        {
            this.state = state;
        }
    }

    public void setSerial_number(String serial_number)
    {
        if(serial_number != null)
        {
            this.serial_number = serial_number;
        }
    }

    public void setEngine_number(String engine_number)
    {
        if(engine_number != null)
        {
            this.engine_number = engine_number;
        }
    }

    public void setVendor_account(String vendor_account)
    {
        if(vendor_account != null)
        {
            this.vendor_account = vendor_account;
        }
    }

    public void setDate_of_purchase(String date_of_purchase)
    {
        if(date_of_purchase != null)
        {
            this.date_of_purchase = date_of_purchase;
        }
    }

    public void setDepreciation_start_date(String depreciation_start_date)
    {
        if(depreciation_start_date != null)
        {
            this.depreciation_start_date = depreciation_start_date;
        }
    }

    public void setDepreciation_end_date(String depreciation_end_date)
    {
        if(depreciation_end_date != null)
        {
            this.depreciation_end_date = depreciation_end_date;
        }
    }    

    public String getDate_of_purchase()
    {
        return date_of_purchase;
    }

    public String getDepreciation_start_date()
    {
        return depreciation_start_date;
    }
    
    public String getDepreciation_end_date()
    {
        return depreciation_end_date;
    }

    public String getSubCategory_id()
    {
        return subcategory_id;
    }

    public String getDepartment_id()
    {
        return department_id;
    }

    public String getSection_id()
    {
        return section_id;
    }

    public String getState()
    {
        return state;
    }

    public String getSerial_number()
    {
        return serial_number;
    }

    public String getEngine_number()
    {
        return engine_number;
    }

    public String getVendor_account()
    {
        return vendor_account;
    }    
}
