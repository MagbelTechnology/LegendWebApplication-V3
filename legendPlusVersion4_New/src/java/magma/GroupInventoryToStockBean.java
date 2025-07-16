
package magma;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import magma.net.dao.MagmaDBConnection;
import magma.net.manager.FleetHistoryManager;
import magma.net.vao.FMppmAllocation;
import magma.util.Codes;
  











import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.ApprovalManager;
import com.magbel.legend.vao.SendMail;
import com.magbel.util.HtmlUtility;
import com.magbel.util.ApplicationHelper;
public class GroupInventoryToStockBean extends legend.ConnectionClass 
{
	public ApprovalRecords approvalRec;
	public ApprovalManager tagapprove;
        private String invoiceNum="";

        private String workstationIp ="";

        private int assetCode=0;
private String memoValue="";

        private String memo="";

        public String getMemoValue() {
    		return memoValue;
    	}

    	public void setMemoValue(String memoValue) {
    		this.memoValue = memoValue;
    	}


    	public String getMemo() { 
    		return memo;
    	}

    	public void setMemo(String memo) {
    		this.memo = memo;
    	}
    public String getWorkstationIp() {
        return workstationIp;
    }

    public void setWorkstationIp(String workstationIp) {
        this.workstationIp = workstationIp;
    }

        HtmlUtility htmlUtil;

    public String getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
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
 
    public GroupInventoryBean adGroup;
    private Codes code;
    private String upd_am_asset_status_RaiseEntry="update ST_STOCK set asset_status='APPROVED' , raise_entry='Y' where group_id = ";
    private String upd_am_grp_asset_RaiseEntry="update am_group_stock set raise_entry='Y' where group_id = ";
    private String upd_am_grp_asset_main_RaiseEntry="update am_group_stock_main set raise_entry='Y' where group_id = ";

    private String upd_am_asset_status_RaiseEntry_Archive="update ST_STOCK_ARCHIVE set asset_status='APPROVED' , raise_entry='Y' where group_id = ";
    private String upd_am_grp_asset_RaiseEntry_Archive="update am_group_stock_archive set raise_entry='Y' where group_id = ";
    private String upd_am_grp_asset_main_RaiseEntry_Archive="update am_group_stock_main_archive set raise_entry='Y' where group_id = ";

    private String upd_am_asset_branch_status_RaiseEntry="update am_asset_uncapitalized set asset_status='APPROVED' , raise_entry='Y' where group_id = ";
    private String upd_am_grp_asset_branch_RaiseEntry="update am_group_asset_uncapitalized set raise_entry='Y' where group_id = ";

    
	public String getPartPAY()
    {
	
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

	public String getDeferPay() {
		return deferPay;
	}

	public void setDeferPay(String deferPay) {
		this.deferPay = deferPay;
	}

	private String branch_id = "0";
    private String department_id = "0";
    private String section_id = "0";
    private String category_id = "0";
    private String subcategory_id = "0";
    private String make = "0";
    private String location = "0";
    private String maintained_by = "0";
    private String driver = "0";
    private String state = "0";
    private String supplied_by = "0";
    private String registration_no = "";
    private String date_of_purchase = null;
    private String depreciation_start_date = null;
    private String depreciation_end_date = null;
    private String authorized_by = "";
    private String reason = "";
    private String description = "";
    private String cost_price = "0";
    private String vatable_cost = "0";
    private String subject_to_vat = "N";
    private String vat_amount = "0";
    private String wh_tax_cb = "N";
    private String wh_tax_amount = "0";
    private String serial_number = "";
    private String engine_number = "";
    private String model = "";
    private String user = "";
    private String vatable_cost_balance="";
    private String sbu_code ="";
    private  double asset_costPrice=0;
    private int quantity=0;
    private String warehouseCode = "";
    private String itemCode = "";
    private String itemType = "";
    private String unitCode = "";    
 
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
    public String getSbu_code() {
		return sbu_code;
	}

	public void setSbu_code(String sbu_code) {
		this.sbu_code = sbu_code;
	}

	public String getVatable_cost_balance() {
		return vatable_cost_balance;
	}

	public void setVatable_cost_balance(String vatable_cost_balance) {
		this.vatable_cost_balance = vatable_cost_balance;
	}

	public String getAsset_id_new() {
		return asset_id_new;
	}

	public void setAsset_id_new(String asset_id_new) {
		this.asset_id_new = asset_id_new;
	}

	private String depreciation_rate = "100";
    private String residual_value = getResidual_value();
    private String require_depreciation = "N";
    private String vendor_account = "";
    private String spare_1 = "";
    private String spare_2 = "";
    private String spare_3 = "";
    private String spare_4 = "";
    private String spare_5 = "";
    private String spare_6 = "";
    private String email_1 = "";
    private String email2 = "";
    private String user_id = "0";
    private String asset_id = "";
    private String posting_date = null;
    private String accum_dep = "0";
    private String who_to_remind = "";
   // private String status = "A";
    private String raise_entry = "N";
    private String require_redistribution = "N";
    private String who_to_remind_2 = "";
    private String group_id = "0";
    private String province = "";
    private String multiple = "N";
    private String section = "";
    private String warrantyStartDate = "";
    private String asset_id_new="";
    private String partPAY="N";
    private String fullyPAID="N";
    private String deferPay="N";
    private String amountPTD="0";
    private String asset_Status ="PENDING";
  
    public String getAsset_Status() {
		return asset_Status;
	}

	public void setAsset_Status(String asset_Status) {
		this.asset_Status = asset_Status;
	}

	/*
     * The Branch_code & Category_code are used to allow insertion into the am_asset Table
     * since those columns will not allow null values.-ayojava 21/05/2009
     * */
    private String Branch_code ="";
    private String Category_code ="";
    private String SubCategory_code ="";
    public String getBranch_code() {
		return Branch_code;
	}

	public String getAmountPTD() {
		return amountPTD;
	}

	public void setAmountPTD(String amountPTD) {
		this.amountPTD = amountPTD;
	}

	public void setBranch_code(String branch_code) {
		Branch_code = branch_code;
	}

	public String getCategory_code() {
		return Category_code;
	}

	public String getSubCategory_code() {
		return SubCategory_code;
	}
	
	public void setCategory_code(String category_code) {
		Category_code = category_code;
	}
	
	public void setSubCategory_code(String subcategory_code) {
		SubCategory_code = subcategory_code;
	}

	public String getLpo() {
		return lpo;
	}

	public void setLpo(String lpo) {
		this.lpo = lpo;
	}

	public String getBar_code() {
		return bar_code;
	}

	public void setBar_code(String bar_code) {
		this.bar_code = bar_code;
	}

	private String noOfMonths = "";
    private String expiryDate = "";
    private String lpo="";
	private String  bar_code="";

    private MagmaDBConnection dbConnection;
    private String assetId;
    private StockRecordsBean ad;

    public GroupInventoryToStockBean() throws Exception {
        super();
        dbConnection = new MagmaDBConnection();
        approvalRec = new ApprovalRecords(); 
        tagapprove = new ApprovalManager();
        adGroup = new GroupInventoryBean(); 
        ad = new StockRecordsBean();
        code = new Codes();
        htmlUtil = new HtmlUtility();
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public void setBranch_id(String branch_id) {
        if (branch_id != null) {
            this.branch_id = branch_id;
        }
    }

    public void setDepartment_id(String department_id) {
        if (department_id != null) {
            this.department_id = department_id;
        }
    }

    public void setSection_id(String section_id) {
        if (section_id != null) {
            this.section_id = section_id;
        }
    }

    public void setSection(String section) {
        if (section != null) {
            this.section = section;
        }
    }

    public void setCategory_id(String category_id) {
        if (category_id != null) {
            this.category_id = category_id;
        }
    }

    public void setSubcategory_id(String subcategory_id) {
        if (subcategory_id != null) {
            this.subcategory_id = subcategory_id;
        }
    }

    public void setMake(String make) {
        if (make != null) {
            this.make = make;
        }
    }

    public void setLocation(String location) {
        if (location != null) {
            this.location = location;
        }
    }

    public void setMaintained_by(String maintained_by) {
        if (maintained_by != null) {
            this.maintained_by = maintained_by;
        }
    }

    public void setDriver(String driver) {
        if (driver != null) {
            this.driver = driver;
        }
    }

    public void setState(String state) {
        if (state != null) {
            this.state = state;
        }
    }

    public void setSupplied_by(String supplied_by) {
        if (supplied_by != null) {
            this.supplied_by = supplied_by;
        }
    }

    public void setRegistration_no(String registration_no) {
        if (registration_no != null) {
            this.registration_no = registration_no;
        }
    }

    public void setDate_of_purchase(String date_of_purchase) {
        if (date_of_purchase != null) {
            this.date_of_purchase = date_of_purchase;
        }
    }

    public void setDepreciation_start_date(String depreciation_start_date) {
        if (depreciation_start_date != null) {
            this.depreciation_start_date = depreciation_start_date;
        }
    }

    public void setDepreciation_end_date(String depreciation_end_date) {
        if (depreciation_end_date != null) {
            this.depreciation_end_date = depreciation_end_date;
        }
    }

    public void setAuthorized_by(String authorized_by) {
        if (authorized_by != null) {
            this.authorized_by = authorized_by;
        }
    }

    public void setReason(String reason) {
        if (reason != null) {
            this.reason = reason;
        }
    }

    public void setDescription(String description) {
        if (description != null) {
            this.description = description;
        }
    }

    public void setCost_price(String cost_price) {
        if (cost_price != null) {
            this.cost_price = cost_price.replaceAll(",", "");
        }
    }

    public void setVatable_cost(String vatable_cost) {
        if (vatable_cost != null) {
            this.vatable_cost = vatable_cost.replaceAll(",", "");
        }
    }

    public void setSubject_to_vat(String subject_to_vat) {
        if (subject_to_vat != null) {
            this.subject_to_vat = subject_to_vat.replaceAll(",", "");
        }
    }

    public void setVat_amount(String vat_amount) {
        if (vat_amount != null) {
            this.vat_amount = vat_amount.replaceAll(",", "");
        }
    }

    public void setWh_tax_cb(String wh_tax_cb) {
        if (wh_tax_cb != null) {
            this.wh_tax_cb = wh_tax_cb;
        }
    }

    public void setWh_tax_amount(String wh_tax_amount) {
        if (wh_tax_amount != null) {
            this.wh_tax_amount = wh_tax_amount.replaceAll(",", "");
        }
    }

    public void setSerial_number(String serial_number) {
        if (serial_number != null) {
            this.serial_number = serial_number;
        }
    }

    public void setEngine_number(String engine_number) {
        if (engine_number != null) {
            this.engine_number = engine_number;
        }
    }

    public void setModel(String model) {
        if (model != null) {
            this.model = model;
        }
    }

    public void setUser(String user) {
        if (user != null) {
            this.user = user;
        }
    }

    public void setDepreciation_rate(String depreciation_rate) {
        if (depreciation_rate != null) {
            this.depreciation_rate = depreciation_rate.replaceAll(",", "");
        }
    }

    public void setResidual_value(String residual_value) {
        if (residual_value != null) {
            this.residual_value = residual_value.replaceAll(",", "");
        }
    }

    public void setRequire_depreciation(String require_depreciation) {
        if (require_depreciation != null) {
            this.require_depreciation = require_depreciation;
        }
    }

    public void setVendor_account(String vendor_account) {
        if (vendor_account != null) {
            this.vendor_account = vendor_account;
        }
    }

    public void setSpare_1(String spare_1) {
        if (spare_1 != null) {
            this.spare_1 = spare_1;
        }
    }

    public void setSpare_2(String spare_2) {
        if (spare_2 != null) {
            this.spare_2 = spare_2;
        }
    }

    public void setSpare_3(String spare_3) {
        if (spare_3 != null) {
            this.spare_3 = spare_3;
        }
    }

    public void setSpare_4(String spare_4) {
        if (spare_4 != null) {
            this.spare_4 = spare_4;
        }
    }

    public void setSpare_5(String spare_5) {
        if (spare_5 != null) {
            this.spare_5 = spare_5;
        }
    }

    public void setSpare_6(String spare_6) {
        if (spare_6 != null) {
            this.spare_6 = spare_6;
        }
    }

    public void setEmail_1(String email_1) {
        if (email_1 != null) {
            this.email_1 = email_1;
        }
    }

    public void setEmail2(String email2) {
        if (email2 != null) {
            this.email2 = email2;
        }
    }

    public void setUser_id(String user_id) {
        if (user_id != null) {
            this.user_id = user_id;
        }
    }

    public void setAsset_id(String asset_id) {
        if (asset_id != null) {
            this.asset_id = asset_id;
        }
    }

    public void setPosting_date(String posting_date) {
        this.posting_date = posting_date;
    }

    public void setAccum_dep(String accum_dep) {
        this.accum_dep = accum_dep;
    }

    public void setWho_to_remind(String who_to_remind) {
        this.who_to_remind = who_to_remind;
    }

    public void setRaise_entry(String raise_entry) {
        this.raise_entry = raise_entry;
    }

    public void setRequire_redistribution(String require_redistribution) {
        this.require_redistribution = require_redistribution;
    }

    public void setWho_to_remind_2(String who_to_remind_2) {
        this.who_to_remind_2 = who_to_remind_2;
    }

    public void setGroup_id(String group_id) {
        if (group_id != null) {
            this.group_id = group_id;
        }
    }

    public void setMultiple(String multiple) {
        if (multiple != null) {
            this.multiple = multiple;
        }
    }

    public void setProvince(String province) {
        if (province != null) {
            this.province = province;
        }
    }

    public void setWarrantyStartDate(String warDate) {
        if (warrantyStartDate != null) {
            this.warrantyStartDate = warDate;
        }
    }

    public void setNoOfMonths(String months) {
        if (noOfMonths != null) {
            this.noOfMonths = months;
        }
    }

    public void setExpiryDate(String expiryDate) {
        if (expiryDate != null) {
            this.expiryDate = expiryDate;
        }
    }

    public String getAssetId() {
        return assetId;
    }

    public String getBranch_id() {
        return branch_id;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public String getSection_id() {
        return section_id;
    }

    public String getSection() {
        return section;
    }

    public String getCategory_id() {
        return category_id;
    }

    public String getSubcategory_id() {
        return subcategory_id;
    }

    public String getMake() {
        return make;
    }

    public String getLocation() {
        return location;
    }

    public String getMaintained_by() {
        return maintained_by;
    }

    public String getDriver() {
        return driver;
    }

    public String getState() {
        return state;
    }

    public String getSupplied_by() {
        return supplied_by;
    }

    public String getRegistration_no() {
        return registration_no;
    }

    public String getDate_of_purchase() {
        return date_of_purchase;
    }

    public String getDepreciation_start_date() {
        return depreciation_start_date;
    }

    public String getDepreciation_end_date() {
        return depreciation_end_date;
    }

    public String getAuthorized_by() {
        return authorized_by;
    }

    public String getReason() {
        return reason;
    }

    public String getDescription() {
        return description;
    }

    public String getCost_price() {
        return cost_price;
    }

    public String getVatable_cost() {
    	
        return vatable_cost;
    }
    
    public String getSubject_to_vat() {
        return subject_to_vat;
    }

    public String getVat_amount() {
        return vat_amount;
    }

    public String getWh_tax_cb() {
        return wh_tax_cb;
    }

    public String getWh_tax_amount() {
        return wh_tax_amount;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public String getEngine_number() {
        return engine_number;
    }

    public String getModel() {
        return model;
    }

    public String getUser() {
        return user;
    }

    public String getDepreciation_rate() {
        return depreciation_rate;
    }

    public String getResidual_value() {
        return residual_value;
    }

    public String getRequire_depreciation() {
        return require_depreciation;
    }

    public String getVendor_account() {
        return vendor_account;
    }

    public String getSpare_1() {
        return spare_1;
    }

    public String getSpare_2() {
        return spare_2;
    }

    public String getSpare_3() {
        return spare_3;
    }

    public String getSpare_4() {
        return spare_4;
    }

    public String getSpare_5() {
        return spare_5;
    }

    public String getSpare_6() {
        return spare_6;
    }

    public String getEmail_1() {
        return email_1;
    }

    public String getEmail2() {
        return email2;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getAsset_id() {
        return asset_id;
    }

    public String getPosting_date() {
        return posting_date;
    }

    public String getAccum_dep() {
        return accum_dep;
    }

    public String getWho_to_remind() {
        return who_to_remind;
    }

    public String getRaise_entry() {
        return raise_entry;
    }

    public String getRequire_redistribution() {
        return require_redistribution;
    }

    public String getWho_to_remind_2() {
        return who_to_remind_2;
    }

    public String getGroup_id() {
        return group_id;
    }

    public String getProvince() {
        return province;
    }

    public String getMultiple() {
        return multiple;
    }

    public String getWarrantyStartDate() {
        return warrantyStartDate;
    }

    public String getNoOfMonths() {
        return noOfMonths;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    /**
     * fetch
     *
     * @param s String
     */
    public void fetch(String s) throws Exception {

        String fetchQuery = "SELECT DISTINCT a.ASSET_ID,a.REGISTRATION_NO," +
                            "a.BRANCH_ID, a.DEPT_ID, a.CATEGORY_ID, " +
                            "a.SECTION_ID, a.DESCRIPTION, a.VENDOR_AC, a.DATE_PURCHASED," +
                            "a.DEP_RATE, a.ASSET_MAKE, a.ASSET_MODEL, a.ASSET_SERIAL_NO," +
                            "a.ASSET_ENGINE_NO, b.SUPPLIER_NAME, a.ASSET_USER, " +
                            "a.ASSET_MAINTENANCE, a.COST_PRICE, a.DEP_END_DATE," +
                            "a.RESIDUAL_VALUE, a.AUTHORIZED_BY," +
                            "a.WH_TAX, a.WH_TAX_AMOUNT, a.POSTING_DATE, a.EFFECTIVE_DATE," +
                            "a.PURCHASE_REASON, a.LOCATION, a.VATABLE_COST, a.VAT," +
                            "a.REQ_DEPRECIATION, a.SUBJECT_TO_VAT, a.WHO_TO_REM," +
                            "a.EMAIL1, a.WHO_TO_REM_2, a.EMAIL2, a.STATE," +
                            "a.DRIVER, a.SPARE_1, a.SPARE_2, a.USER_ID,a.PROVINCE, a.WAR_START_DATE, " +
                            "a.WAR_MONTH, a.WAR_EXPIRY_DATE,a.LPO,a.BAR_CODE,a.req_redistribution,a.Raise_entry, " +
                            "a.PART_PAY,a.FULLY_PAID,a.defer_pay,a.AMOUNT_PTD,a.Asset_Status,a.SBU_CODE,b.Invoice_No,  "+
                            "a.SUB_CATEGORY_ID,a.SPARE_3, a.SPARE_4, a.SPARE_5, a.SPARE_6 FROM ST_STOCK a, AM_GROUP_STOCK b   " +
                            "WHERE a.GROUP_ID = b.GROUP_ID AND a.ASSET_ID = '" + s + "'";
//Asset Status,deferpay,amountPTD
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
//        System.out.println("Fetch Query : " + fetchQuery);
        try {

            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(fetchQuery);
            rs = ps.executeQuery();

            if (rs.next()) {

                group_id = s;
                setAsset_id(rs.getString("Asset_id"));
                setBranch_id(rs.getString(3));
                setDepartment_id(rs.getString(4));
                setSection_id(rs.getString(6));
                setCategory_id(rs.getString(5));
                setMake(rs.getString(11));
                setLocation(rs.getString(27));
                setMaintained_by(rs.getString(17));
                setDriver(rs.getString(37));
                setState(rs.getString(36));
                setSupplied_by(rs.getString(15));
                setRegistration_no(rs.getString(2));
                date_of_purchase = dbConnection.formatDate(rs.getDate(9));
                depreciation_start_date = dbConnection.formatDate(rs.getDate(25));
                depreciation_end_date = dbConnection.formatDate(rs.getDate(19));
                posting_date = dbConnection.formatDate(rs.getDate(24));
                setAuthorized_by(rs.getString(21));
                setReason(rs.getString(26));
                setDescription(rs.getString(7));
                setCost_price(rs.getString(18));
                setVatable_cost(rs.getString(28));
                setSubject_to_vat(rs.getString(31));
                setVat_amount(rs.getString(29));
                setWh_tax_cb(rs.getString(22));
                setWh_tax_amount(rs.getString(23));
                setSerial_number(rs.getString(13));  
                setEngine_number(rs.getString(14));
                setModel(rs.getString(12));
                setUser(rs.getString(16));
                setDepreciation_rate(rs.getString(10));
                setResidual_value(rs.getString(20));
                setRequire_depreciation(rs.getString(30));
                setVendor_account(rs.getString(8));
                setWho_to_remind(rs.getString(32));
                setSpare_1(rs.getString(38));
                setSpare_2(rs.getString(39));
                setEmail_1(rs.getString(33));
                setWho_to_remind_2(rs.getString(34));  
                setEmail2(rs.getString(35));
                setProvince(rs.getString(41));
                this.setWarrantyStartDate(dbConnection.formatDate(rs.getDate("WAR_START_DATE")));
                this.setExpiryDate(dbConnection.formatDate(rs.getDate("WAR_EXPIRY_DATE")));
                this.setNoOfMonths(rs.getString("WAR_MONTH"));
                this.setLpo(rs.getString("LPO"));
                this.setBar_code(rs.getString("BAR_CODE"));
                this.setRequire_redistribution(rs.getString("REQ_REDISTRIBUTION"));
                this.setRaise_entry(rs.getString("RAISE_ENTRY"));
                this.setPartPAY(rs.getString("PART_PAY"));
                this.setFullyPAID(rs.getString("FULLY_PAID"));
                this.setDeferPay(rs.getString("defer_pay"));
                this.setAmountPTD(rs.getString("AMOUNT_PTD"));
                this.setAsset_Status(rs.getString("Asset_Status"));
                this.setSbu_code(rs.getString("SBU_CODE"));
      //          this.setInvoiceNum(rs.getString("Invoice_No"));
                this.setSubcategory_id(rs.getString("SUB_CATEGORY_ID"));
                this.setSpare_3(rs.getString("SPARE_3"));
                this.setSpare_4(rs.getString("SPARE_4"));
                this.setSpare_5(rs.getString("SPARE_5"));
                this.setSpare_6(rs.getString("SPARE_6"));
            }

        } catch (Exception e) {
            System.out.println("INFO:Error Fetching GroupAsset Records ->" +
                               e.getMessage());
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }
    }

    /**
     * save
     *
     * @return boolean
     */
    public boolean save() throws Exception, Throwable
    {
    	//subcategory_id =approvalRec.getCodeName(" SELECT full_name from am_gb_user where user_id='"+group_id+"'");
/*    	System.out.println("branch_id ::::::::: " + branch_id);
        System.out.println("department_id ::::::::: " + department_id);
        System.out.println("section_id ::::::::: " + section_id);
        System.out.println("category_id ::::::::: " + category_id);
        System.out.println("Branch Code : " +code.getBranchCode(branch_id));
        System.out.println("Category Code " + code.getCategoryCode(category_id));
        System.out.println("Sub Category Id : " +subcategory_id);
        System.out.println("Sub Category Code " + code.getSubCategoryCode(subcategory_id));        */
    	System.out.println("unitCode at the point of Saving::::::::: " + unitCode+"  "+branch_id);
        asset_id_new = new legend.AutoIDSetup().getIdentity(branch_id,
                department_id, section_id, category_id);
          assetCode = Integer.parseInt(new ApplicationHelper().getGeneratedId("ST_STOCK"));
        //System.out.println("New Asset_ID ::::::::: " + asset_id_new);
        Connection con = null;
        PreparedStatement ps = null;
        Connection con1 = null;
        PreparedStatement ps1 = null;
        boolean done = true;
        boolean result = true;
        if (make == null || make.equals("")) {
            make = "0";
        }
        if (maintained_by == null || maintained_by.equals("")) {
            maintained_by = "0";
        }
        if (supplied_by == null || supplied_by.equals("")) {
            supplied_by = "0";
        }
        if (user == null || user.equals("")) {
            user = "";
        }
        if (location == null || location.equals("")) {
            location = "0";
        }
        if (driver == null || driver.equals("")) {
            driver = "0";
        }
        if (state == null || state.equals("")) {
            state = "0";
        }
        if (department_id == null || department_id.equals("")) {
            department_id = "0";
        }
        if (vat_amount == null || vat_amount.equals("")) {
            vat_amount = "0.0";
        }
        if (vatable_cost == null || vatable_cost.equals("")) {
            vatable_cost = "0.0";
        }
        if (wh_tax_amount == null || wh_tax_amount.equals("")) {
            wh_tax_amount = "0";
        }
        if (branch_id == null || branch_id.equals("")) {
            branch_id = "0";
        }
        if (province == null || province.equals("")) {
            province = "0";
        }
        if (category_id == null || category_id.equals("")) {
            category_id = "0";
        }
        if (subcategory_id == null || subcategory_id.equals("")) {
            subcategory_id = "0";
        }
        
        if (residual_value == null || residual_value.equals("")) {
            residual_value = "0";
        }
        if (noOfMonths == null || noOfMonths.equals("")) {
            noOfMonths = "0";
        }
        if (warrantyStartDate == null || warrantyStartDate.equals("")) {
            warrantyStartDate = null;
        }
        if (expiryDate == null || expiryDate.equals("")) {
            expiryDate = null;
        }
        vat_amount = vat_amount.replaceAll(",", "");
        vatable_cost = vatable_cost.replaceAll(",", "");
        wh_tax_amount = wh_tax_amount.replaceAll(",", "");
        residual_value = residual_value.replaceAll(",", "");
        
        String createQuery = "INSERT INTO ST_STOCK         " +
        "(" +
        "ASSET_ID, REGISTRATION_NO, BRANCH_ID, DEPT_ID," +
        "SECTION_ID, CATEGORY_ID, [DESCRIPTION], VENDOR_AC," +
        "DATE_PURCHASED, DEP_RATE, ASSET_MAKE, ASSET_MODEL," +
        "ASSET_SERIAL_NO, ASSET_ENGINE_NO, SUPPLIER_NAME," +

        "ASSET_USER, ASSET_MAINTENANCE, ACCUM_DEP, MONTHLY_DEP," +
        "COST_PRICE, NBV, DEP_END_DATE, RESIDUAL_VALUE," +
        "AUTHORIZED_BY, POSTING_DATE, EFFECTIVE_DATE, PURCHASE_REASON," +
        "USEFUL_LIFE, TOTAL_LIFE, LOCATION, REMAINING_LIFE," +

        "VATABLE_COST,VAT, WH_TAX, WH_TAX_AMOUNT, REQ_DEPRECIATION," +
        "REQ_REDISTRIBUTION, SUBJECT_TO_VAT, WHO_TO_REM, EMAIL1," +
        "WHO_TO_REM_2, EMAIL2, RAISE_ENTRY, DEP_YTD, [SECTION]," +
        "STATE, DRIVER, SPARE_1, SPARE_2, ASSET_STATUS, [USER_ID]," +
        "MULTIPLE,PROVINCE, WAR_START_DATE, WAR_MONTH, " +
        "WAR_EXPIRY_DATE,LPO,BAR_CODE ,BRANCH_CODE,CATEGORY_CODE ," +
        "GROUP_ID,PART_PAY,FULLY_PAID,DEFER_PAY,SBU_CODE,SECTION_CODE,DEPT_CODE,system_ip,asset_code," +
        "memo,memoValue, SUB_CATEGORY_ID,SUB_CATEGORY_CODE, SPARE_3, SPARE_4, SPARE_5, SPARE_6," +
        "WAREHOUSE_CODE,ITEMTYPE, ITEM_CODE,QUANTITY,UNIT_CODE ) " +

        "VALUES" +
        "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        

        String create_Archive_Query = "INSERT INTO ST_STOCK_ARCHIVE         " +
        "(" +
        "ASSET_ID, REGISTRATION_NO, BRANCH_ID, DEPT_ID," +
        "SECTION_ID, CATEGORY_ID, [DESCRIPTION], VENDOR_AC," +
        "DATE_PURCHASED, DEP_RATE, ASSET_MAKE, ASSET_MODEL," +
        "ASSET_SERIAL_NO, ASSET_ENGINE_NO, SUPPLIER_NAME," +

        "ASSET_USER, ASSET_MAINTENANCE, ACCUM_DEP, MONTHLY_DEP," +
        "COST_PRICE, NBV, DEP_END_DATE, RESIDUAL_VALUE," +
        "AUTHORIZED_BY, POSTING_DATE, EFFECTIVE_DATE, PURCHASE_REASON," +
        "USEFUL_LIFE, TOTAL_LIFE, LOCATION, REMAINING_LIFE," +

        "VATABLE_COST,VAT, WH_TAX, WH_TAX_AMOUNT, REQ_DEPRECIATION," +
        "REQ_REDISTRIBUTION, SUBJECT_TO_VAT, WHO_TO_REM, EMAIL1," +
        "WHO_TO_REM_2, EMAIL2, RAISE_ENTRY, DEP_YTD, [SECTION]," +
        "STATE, DRIVER, SPARE_1, SPARE_2, ASSET_STATUS, [USER_ID]," +
        "MULTIPLE,PROVINCE, WAR_START_DATE, WAR_MONTH, " +
        "WAR_EXPIRY_DATE,LPO,BAR_CODE ,BRANCH_CODE,CATEGORY_CODE ," +
        "GROUP_ID,PART_PAY,FULLY_PAID,DEFER_PAY,SBU_CODE,SECTION_CODE,DEPT_CODE,system_ip,asset_code," +
        "SUB_CATEGORY_ID,SUB_CATEGORY_CODE, SPARE_3, SPARE_4, SPARE_5, SPARE_6 ) " +
        "VALUES" +
        "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        /*
         *First Create Asset Records
         * and then determine if it
         * should be made available for fleet.
         */
           try 
           {
            asset_costPrice = Double.parseDouble(vat_amount) + Double.parseDouble(vatable_cost);
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(createQuery);
            ps.setString(1, asset_id_new);
            ps.setString(2, registration_no);
            ps.setInt(3, Integer.parseInt(branch_id));
            ps.setInt(4, Integer.parseInt(department_id));
            ps.setInt(5, Integer.parseInt(section_id));
            ps.setInt(6, Integer.parseInt(category_id));
            ps.setString(7, description);
            ps.setString(8, vendor_account);
            ps.setDate(9, dbConnection.dateConvert(date_of_purchase));
            ps.setString(10, getDepreciationRate(category_id));
            ps.setString(11, make);
            ps.setString(12, model);
            ps.setString(13, serial_number);
            ps.setString(14, engine_number);
            ps.setInt(15, Integer.parseInt(supplied_by));
            ps.setString(16, user);
            ps.setInt(17, Integer.parseInt(maintained_by));
            ps.setInt(18, 0);
            ps.setInt(19, 0);
            ps.setDouble(20, asset_costPrice);
            ps.setDouble(21, (asset_costPrice - 10.00));
            ps.setDate(22, dbConnection.dateConvert(depreciation_end_date));
            ps.setDouble(23, Double.parseDouble(residual_value));
            ps.setString(24, authorized_by);
            ps.setDate(25, dbConnection.dateConvert(new java.util.Date()));
            ps.setDate(26, dbConnection.dateConvert(depreciation_start_date));
            ps.setString(27, reason);
            ps.setString(28, "0");
            ps.setString(29, computeTotalLife(getDepreciationRate(category_id)));
            ps.setInt(30, Integer.parseInt(location));
            ps.setString(31, computeTotalLife(getDepreciationRate(category_id)));
            ps.setDouble(32, Double.parseDouble(vatable_cost));
            ps.setDouble(33, Double.parseDouble(vat_amount));
            ps.setString(34, wh_tax_cb);
            ps.setDouble(35, Double.parseDouble(wh_tax_amount));
            ps.setString(36, require_depreciation);
            ps.setString(37, require_redistribution);
            ps.setString(38, subject_to_vat);
            ps.setString(39, who_to_remind);
            ps.setString(40, email_1);
            ps.setString(41, who_to_remind_2);
            ps.setString(42, email2);
            ps.setString(43, "N");
            ps.setString(44, "0");
            ps.setString(45, section);
            ps.setInt(46, Integer.parseInt(state));
            ps.setInt(47, Integer.parseInt(driver));
            ps.setString(48, spare_1);
            ps.setString(49, spare_2);
            ps.setString(50, asset_Status);
            ps.setString(51, user_id);
            ps.setString(52, multiple);
            ps.setString(53, province);
            ps.setDate(54, dbConnection.dateConvert(warrantyStartDate));
            ps.setInt(55, Integer.parseInt(noOfMonths));
            ps.setDate(56, dbConnection.dateConvert(expiryDate));
            ps.setString(57,lpo);
            ps.setString(58,bar_code);
            ps.setString(59,code.getBranchCode(branch_id));
            ps.setString(60,code.getCategoryCode(category_id));
            ps.setString(61,group_id);
            ps.setString(62, "N");
            ps.setString(63, "Y");
            ps.setString(64, "N");
            ps.setString(65,sbu_code);
            ps.setString(66,code.getSectionCode(section_id));
            ps.setString(67,code.getDeptCode(department_id));
            ps.setString(68,workstationIp);
            ps.setInt(69, assetCode);
            ps.setString(70,memo);
            ps.setString(71, memoValue);
            ps.setInt(72, Integer.parseInt(subcategory_id));
            ps.setString(73,code.getSubCategoryCode(subcategory_id));
            ps.setString(74, spare_3);
            ps.setString(75, spare_4);
            ps.setString(76, spare_5);
            ps.setString(77, spare_6);
            ps.setString(78, warehouseCode);
            ps.setString(79, itemType);
            ps.setString(80, itemCode);
            ps.setInt(81, quantity);
            ps.setString(82, unitCode);
            //not right
//            System.out.println("<<<<<<<<branch_id: "+code.getBranchCode(branch_id)+"      category_id: "+code.getCategoryCode(category_id)+"    section_id: "+code.getSectionCode(section_id)+"   department_id: "+code.getDeptCode(department_id));
 //           System.out.println("<<<<<<<<itemCode: "+itemCode+"      quantity: "+quantity+"    unitCode: "+unitCode);
            result = ps.execute();
            result = true;
//            System.out.println("<<<<<<<<result: "+result);
            con1 = dbConnection.getConnection("legendPlus");
            ps1 = con1.prepareStatement(create_Archive_Query);
            ps1.setString(1, asset_id_new);
            ps1.setString(2, registration_no);
            ps1.setInt(3, Integer.parseInt(branch_id));
            ps1.setInt(4, Integer.parseInt(department_id));
            ps1.setInt(5, Integer.parseInt(section_id));
            ps1.setInt(6, Integer.parseInt(category_id));
            ps1.setString(7, description);
            ps1.setString(8, vendor_account);
            ps1.setDate(9, dbConnection.dateConvert(date_of_purchase));
            ps1.setString(10, getDepreciationRate(category_id));
//            System.out.println("<<<<<<<<asset_id_new: "+asset_id_new+"      registration_no: "+registration_no+"    branch_id: "+branch_id+"   department_id: "+department_id+"  section_id: "+section_id+"  category_id: "+category_id+" description: "+description+"   vendor_account: "+vendor_account);
            ps1.setString(11, make);
            ps1.setString(12, model);
            ps1.setString(13, serial_number);
            ps1.setString(14, engine_number);
            ps1.setInt(15, Integer.parseInt(supplied_by));
            ps1.setString(16, user);
            ps1.setInt(17, Integer.parseInt(maintained_by));
            ps1.setInt(18, 0);
            ps1.setInt(19, 0);
 //           System.out.println("<<<<<<<<make: "+make+"      model: "+model+"    serial_number: "+serial_number+" engine_number: "+engine_number+"   supplied_by: "+supplied_by+"  user: "+user+"    maintained_by: "+maintained_by);
            ps1.setDouble(20, asset_costPrice); 
            ps1.setDouble(21, (asset_costPrice-10.00));
            ps1.setDate(22, dbConnection.dateConvert(depreciation_end_date));
            ps1.setDouble(23, Double.parseDouble(residual_value));
            ps1.setString(24, authorized_by);
            ps1.setDate(25, dbConnection.dateConvert(new java.util.Date()));
            ps1.setDate(26, dbConnection.dateConvert(depreciation_start_date));
            ps1.setString(27, reason);
            ps1.setString(28, "0");
 //           System.out.println("<<<<<<<<asset_costPrice: "+asset_costPrice+"      depreciation_end_date: "+depreciation_end_date+"    residual_value: "+residual_value+"  authorized_by: "+authorized_by+"    depreciation_start_date: "+depreciation_start_date);
            ps1.setString(29, computeTotalLife(getDepreciationRate(category_id)));
            ps1.setInt(30, Integer.parseInt(location));
            ps1.setString(31, computeTotalLife(getDepreciationRate(category_id)));
            ps1.setDouble(32, Double.parseDouble(vatable_cost));
            ps1.setDouble(33, Double.parseDouble(vat_amount));
            ps1.setString(34, wh_tax_cb);
//            System.out.println("<<<<<<<<location: "+location+"      vatable_cost: "+vatable_cost+"    vat_amount: "+vat_amount+"   wh_tax_cb: "+wh_tax_cb);
            ps1.setDouble(35, Double.parseDouble(wh_tax_amount));
            ps1.setString(36, require_depreciation);
            ps1.setString(37, require_redistribution);
            ps1.setString(38, subject_to_vat);
            ps1.setString(39, who_to_remind);
            ps1.setString(40, email_1);
  //          System.out.println("<<<<<<<<wh_tax_amount: "+wh_tax_amount+"      require_depreciation: "+require_depreciation+"    require_redistribution: "+require_redistribution+"    subject_to_vat: "+subject_to_vat+" who_to_remind: "+who_to_remind);
            ps1.setString(41, who_to_remind_2);
            ps1.setString(42, email2);
            ps1.setString(43, "N");
            ps1.setString(44, "0");
            ps1.setString(45, section);
            ps1.setInt(46, Integer.parseInt(state));
            ps1.setInt(47, Integer.parseInt(driver));
 //           System.out.println("<<<<<<<<section: "+section+"      state: "+state+"    driver: "+driver);
            ps1.setString(48, spare_1);
            ps1.setString(49, spare_2);
            ps1.setString(50, asset_Status);
            ps1.setString(51, user_id);
            ps1.setString(52, multiple);
            ps1.setString(53, province);
   //         System.out.println("<<<<<<<<asset_Status: "+asset_Status+"      user_id: "+user_id+"    multiple: "+multiple);
            ps1.setDate(54, dbConnection.dateConvert(warrantyStartDate));
            ps1.setInt(55, Integer.parseInt(noOfMonths));
            ps1.setDate(56, dbConnection.dateConvert(expiryDate));
            ps1.setString(57,lpo);
            ps1.setString(58,bar_code);
            ps1.setString(59,code.getBranchCode(branch_id));
            ps1.setString(60,code.getCategoryCode(category_id));
            ps1.setString(61,group_id);
  //          System.out.println("<<<<<<<<warrantyStartDate: "+warrantyStartDate+"      noOfMonths: "+noOfMonths+"    expiryDate: "+expiryDate+"   lpo: "+lpo+"   group_id: "+group_id);
            ps1.setString(62, "N");
            ps1.setString(63, "Y");
            ps1.setString(64, "N");
            ps1.setString(65,sbu_code);
            ps1.setString(66,code.getSectionCode(section_id));
            ps1.setString(67,code.getDeptCode(department_id));
            ps1.setString(68,workstationIp);
            ps1.setInt(69, assetCode);
        //    System.out.println("<<<<<<<<sbu_code: "+sbu_code+"      section_id: "+section_id+"    department_id: "+department_id+"   workstationIp: "+workstationIp+"   assetCode: "+assetCode);
            ps1.setInt(70, Integer.parseInt(subcategory_id));
            ps1.setString(71,code.getSubCategoryCode(subcategory_id));
            ps1.setString(72, spare_3);
            ps1.setString(73, spare_4);
            ps1.setString(74, spare_5);
            ps1.setString(75, spare_6);
           // System.out.println("<<<<<<<<subcategory_id: "+subcategory_id+"      spare_3: "+spare_3+"    spare_4: "+spare_4+" spare_4: "+spare_4+"  spare_5: "+spare_5+"  spare_6: "+spare_6);
           // System.out.println("=====================================================");
            System.out.println("Result Of Insertion into Stock Table From group Stock : " + result);
           // System.out.println("=====================================================");
             result = ps1.execute();
             result = true;
//             System.out.println("<<<<<<<<result2: "+result);
 			String tagstatus = "P";
 			boolean doneTagUpdate = tagapprove.NewTagUsed(bar_code,tagstatus);
           htmlUtil.insGrpToAm_Invoice_No(asset_id_new,lpo,invoiceNum,"Stock Creation",group_id);
//           System.out.println("<<<<<<<<doneTagUpdate: "+doneTagUpdate);
            FleetHistoryManager fm = new FleetHistoryManager();
            if (fm.isRequiredForFleet(asset_id))
            {
            	System.out.println(">>>>>>>>>>>>>>>>>> Stock is Required For Fleet <<<<<<<<<<<<<<<<<<<<<<<<<<<");
               fm.copyAssetDataToFleet(asset_id);
            }
            setAssetId(asset_id);
                      
            String page1 = "STOCK GROUP CREATION RAISE ENTRY";
            String flag= "";
      	  	String partPay="";
      	  	String qryBranch =" SELECT branch_name from am_ad_branch where branch_id='"+branch_id+"'";
      	   	String Branch = approvalRec.getCodeName(qryBranch);
      	   	String subjectT= adGroup.subjectToVat(group_id);
      	   	String whT= adGroup.whTax(group_id);
      	   	String Name =approvalRec.getCodeName(" SELECT full_name from am_gb_user where user_id='"+user_id+"'");
      	  System.out.println("<<<<<<<<Name: "+Name);
      	   	String url = "DocumentHelp.jsp?np=groupAssetPosting&amp;id=" + group_id + "&pageDirect=Y";
      	   	boolean approval_level_val =checkApprovalStatus("3");
      	  	boolean status = updateCreatedAssetStatus(asset_id,group_id,asset_id_new,assetCode);
      	  	
      	  	if ((!approval_level_val)&&(status))
	      	  	{
      	  			System.out.println(">>>>>>>>>>>> Inserting Into RaiseEntry <<<<<<<<<<");
      	  			String [] approvalResult=ad.setApprovalDataGroup(Long.parseLong(group_id));
      	  			approvalResult[10]="A";
  //    	  			String trans_id = adGroup.setGroupPendingTrans(approvalResult,"3",assetCode);

         //                      ad.setPendingTransArchive(approvalResult,"3",Integer.parseInt(approvalResult[0]),assetCode);

                                String assetRaiseEntry =approvalRec.getCodeName(" SELECT raise_entry from am_gb_company ");
      	  			if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("Y")){
                                approvalRec.insertApprovalx2
	      	  		(group_id, approvalResult[5], page1, flag, partPay,Name, Branch, subjectT,
                                        whT, url,Integer.parseInt(approvalResult[0]),assetCode);
                                }
      	  			String qry = upd_am_asset_status_RaiseEntry + group_id;
      	  			String qry2 = upd_am_grp_asset_RaiseEntry + group_id;
      	  			String qry3 = upd_am_grp_asset_main_RaiseEntry +  group_id;
                                String qry4 = upd_am_asset_status_RaiseEntry_Archive + group_id;
      	  			String qry5 = upd_am_grp_asset_RaiseEntry_Archive + group_id;
      	  			String qry6 = upd_am_grp_asset_main_RaiseEntry_Archive +  group_id;
	      	  		/*System.out.println("qry : " + qry);
	  	  			System.out.println("qry2 : " + qry2 );
	  	  			System.out.println("qry3 : " + qry3);*/
	      	  		updateStatusUtil(qry);
	      	  		updateStatusUtil(qry2);
	      	  		updateStatusUtil(qry3);
                                updateStatusUtil(qry4);
	      	  		updateStatusUtil(qry5);
	      	  		updateStatusUtil(qry6);
	      	  	}
      	  	
	      	if((status)&&(approval_level_val))
		      	{
		      	 System.out.println("====== Inserting into Approval ======");
		      	 changeGroupAssetStatus(group_id,"PENDING");
//		      	 String trans_id = adGroup.setGroupPendingTrans(ad.setApprovalDataGroup(Long.parseLong(group_id)),"3",assetCode);
		      	String [] approvalResult=ad.setApprovalDataGroup(Long.parseLong(group_id));
                    //     ad.setPendingTransArchive(ad.setApprovalDataGroup(Long.parseLong(group_id)),"3",Integer.parseInt(approvalResult[0]),assetCode);
                         ad.setPendingTrans(ad.setApprovalDataGroup(Long.parseLong(group_id)),"3",assetCode);
		      	 //write a method to change status to pending
		      	}	
	      	
        } 
           
        catch (Exception ex)
        {
            done = false;
            System.out.println("WARN:Error creating Stock->" + ex);
            dbConnection.closeConnection(con, ps);
            dbConnection.closeConnection(con1, ps1);
        }
        finally 
        {
            dbConnection.closeConnection(con, ps);
            dbConnection.closeConnection(con1, ps1);
        }

        return done;

    }

    
    public boolean updateAsset(String assetID) throws Exception, Throwable {
    	/*System.out.println("branch_id ::::::::: " + branch_id);
        System.out.println("department_id ::::::::: " + department_id);
        System.out.println("section_id ::::::::: " + section_id);
        System.out.println("category_id ::::::::: " + category_id);
        System.out.println("Branch Code : " +code.getBranchCode(branch_id));
        System.out.println("Category Code " + code.getCategoryCode(category_id));*/
         //System.out.println("New Asset_ID ::::::::: " + asset_id_new);
        Connection con = null;
        PreparedStatement ps = null;
        boolean done = true;
       
        if (make == null || make.equals("")) {
            make = "0";
        }
        if (maintained_by == null || maintained_by.equals("")) {
            maintained_by = "0";
        }
        if (supplied_by == null || supplied_by.equals("")) {
            supplied_by = "0";
        }
        if (user == null || user.equals("")) {
            user = "";
        }
        if (location == null || location.equals("")) {
            location = "0";
        }
        if (driver == null || driver.equals("")) {
            driver = "0";
        }
        if (state == null || state.equals("")) {
            state = "0";
        }
        if (department_id == null || department_id.equals("")) {
            department_id = "0";
        }
        if (vat_amount == null || vat_amount.equals("")) {
            vat_amount = "0.0";
        }
        if (vatable_cost == null || vatable_cost.equals("")) {
            vatable_cost = "0.0";
        }
        if (wh_tax_amount == null || wh_tax_amount.equals("")) {
            wh_tax_amount = "0";
        }
        if (branch_id == null || branch_id.equals("")) {
            branch_id = "0";
        }
        if (province == null || province.equals("")) {
            province = "0";
        }
        if (category_id == null || category_id.equals("")) {
            category_id = "0";
        }
        if (subcategory_id == null || subcategory_id.equals("")) {
            subcategory_id = "0";
        }
        
        if (residual_value == null || residual_value.equals("")) {
            residual_value = "0";
        }
        if (noOfMonths == null || noOfMonths.equals("")) {
            noOfMonths = "0";
        }
        if (warrantyStartDate == null || warrantyStartDate.equals("")) {
            warrantyStartDate = null;
        }
        if (expiryDate == null || expiryDate.equals("")) {
            expiryDate = null;
        }
        vat_amount = vat_amount.replaceAll(",", "");
        vatable_cost = vatable_cost.replaceAll(",", "");
        wh_tax_amount = wh_tax_amount.replaceAll(",", "");
        residual_value = residual_value.replaceAll(",", "");

        
        String createQuery = "Update ST_STOCK " +
        " set REGISTRATION_NO=?, DESCRIPTION=?, VENDOR_AC=?,"+
        " DATE_PURCHASED=?, ASSET_MAKE=?, ASSET_MODEL=?,"+
        " ASSET_SERIAL_NO=?, ASSET_ENGINE_NO=?, SUPPLIER_NAME=?," +

        " ASSET_USER=?, ASSET_MAINTENANCE=?, ACCUM_DEP=?, MONTHLY_DEP=?," +
        " COST_PRICE=?, NBV=?, DEP_END_DATE=?, RESIDUAL_VALUE=?,"+
        " AUTHORIZED_BY=?, POSTING_DATE=?, EFFECTIVE_DATE=?, PURCHASE_REASON=?,"+
        " USEFUL_LIFE=?,  LOCATION=?, "+

        " VATABLE_COST=?,VAT=?, WH_TAX=?, WH_TAX_AMOUNT=?, REQ_DEPRECIATION=?,"+
        " REQ_REDISTRIBUTION=?, SUBJECT_TO_VAT=?, WHO_TO_REM=?, EMAIL1=?,"+
        " WHO_TO_REM_2=?, EMAIL2=?, RAISE_ENTRY=?, DEP_YTD=?, [SECTION]=?,"+
        " STATE=?, DRIVER=?, SPARE_1=?, SPARE_2=?, ASSET_STATUS=?, [USER_ID]=?,"+
        " MULTIPLE=?,PROVINCE=?, WAR_START_DATE=?, WAR_MONTH=?,"+
        " WAR_EXPIRY_DATE=?,LPO=?,BAR_CODE=?,"+
        " PART_PAY=?,FULLY_PAID=?,DEFER_PAY=?,SBU_CODE=?,SECTION_CODE=?,"+
        " DEPT_ID=?,SECTION_ID=?,DEPT_CODE=? , SPARE_3=?, SPARE_4=?, SPARE_5=?, SPARE_6=?,SUB_CATEGORY_ID = ?,SUB_CATEGORY_CODE=? "+
        " WHERE ASSET_ID= ?"  ;
       

        //System.out.println("createQuery >>>>>  " + createQuery);
        /*
         *First Create Asset Records
         * and then determine if it
         * should be made available for fleet.
         */
           try 
           {
            asset_costPrice = Double.parseDouble(vat_amount) +
                               Double.parseDouble(vatable_cost);
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(createQuery);
            ps.setString(1, registration_no);
            ps.setString(2, description);
            ps.setString(3, vendor_account);
            ps.setDate(4, dbConnection.dateConvert(date_of_purchase));
            ps.setString(5, make);
            ps.setString(6, model);
            ps.setString(7, serial_number);
            ps.setString(8, engine_number);
            ps.setInt(9, Integer.parseInt(supplied_by));
            ps.setString(10, user);
            ps.setInt(11, Integer.parseInt(maintained_by));
            ps.setInt(12, 0);
            ps.setInt(13, 0);
            ps.setDouble(14, asset_costPrice);
            ps.setDouble(15, asset_costPrice);
            ps.setDate(16, dbConnection.dateConvert(depreciation_end_date));
            ps.setDouble(17, Double.parseDouble(residual_value));
            ps.setString(18, authorized_by);
            ps.setDate(19, dbConnection.dateConvert(new java.util.Date()));
            ps.setDate(20, dbConnection.dateConvert(depreciation_start_date));
            ps.setString(21, reason);
            ps.setString(22, "0");
            ps.setInt(23, Integer.parseInt(location));
            ps.setDouble(24, Double.parseDouble(vatable_cost));
            ps.setDouble(25, Double.parseDouble(vat_amount));
            ps.setString(26, wh_tax_cb);
            ps.setDouble(27, Double.parseDouble(wh_tax_amount));
            ps.setString(28, require_depreciation);
            ps.setString(29, require_redistribution);
            ps.setString(30, subject_to_vat);
            ps.setString(31, who_to_remind);
            ps.setString(32, email_1);
            ps.setString(33, who_to_remind_2);
            ps.setString(34, email2);
            ps.setString(35, "N");
            ps.setString(36, "0");
            ps.setString(37, section);
            ps.setInt(38, Integer.parseInt(state));
            ps.setInt(39, Integer.parseInt(driver));
            ps.setString(40, spare_1);
            ps.setString(41, spare_2);
            ps.setString(42, "PENDING");
            ps.setString(43, user_id);
            ps.setString(44, multiple);
            ps.setString(45, province);
            ps.setDate(46, dbConnection.dateConvert(warrantyStartDate));
            ps.setInt(47, Integer.parseInt(noOfMonths));
            ps.setDate(48, dbConnection.dateConvert(expiryDate));
            ps.setString(49,lpo);
            ps.setString(50,bar_code);
            ps.setString(51, partPAY);
            ps.setString(52, fullyPAID);
            ps.setString(53, deferPay);
            ps.setString(54,sbu_code);
            ps.setString(55,code.getSectionCode(section_id));
            
            ps.setInt(56, Integer.parseInt(department_id));
            ps.setInt(57, Integer.parseInt(section_id));
            ps.setString(58,code.getDeptCode(department_id));
            ps.setString(59, spare_3);
            ps.setString(60, spare_4);
            ps.setString(61, spare_5);
            ps.setString(62, spare_6);
            ps.setInt(63, Integer.parseInt(subcategory_id));
            ps.setString(64,code.getSubCategoryCode(subcategory_id));
            
            ps.setString(65,assetID);
            
            boolean result = ps.execute();
           //System.out.println("Result Of Update>>>>>> " + result);
            setAssetId(asset_id);
                
      	  	boolean approval_level_val =checkApprovalStatus("3");
      	  	boolean status = repostCreatedAsset(asset_id,group_id); 
      	  	if ((!approval_level_val)&&(status))
	      	  	{
      	  			/*
      	  			 * ASSETS THAT DON'T HAVE APPROVAL CAN ONLY BE REJECTED AT POSTING LEVEL*/
      	  			//System.out.println("ASSETS THAT DON'T HAVE APPROVAL CAN ONLY BE REJECTED AT POSTING LEVEL");
      	  			changeGroupAssetStatus(group_id,"APPROVED");
      	  			String [] approvalResult=ad.setApprovalDataGroup(Long.parseLong(group_id));
      	  			approvalResult[10]="A";
      	  			updateGroupPendingTrans(approvalResult,"3",group_id);
      	  			
	      	  		String page1 = "ASSET GROUP CREATION RAISE ENTRY";
	                String flag= "";
	          	  	String partPay="";
	          	  	String qryBranch =" SELECT branch_name from am_ad_branch where branch_id='"+branch_id+"'";
	          	  	String qryTrans_Id="SELECT transaction_id from am_asset_approval where asset_id='"+group_id + "'";
	          	   	String Branch = approvalRec.getCodeName(qryBranch);
	          	   	String trans_id=approvalRec.getCodeName(qryTrans_Id);
	          	   	System.out.println("trans_id during Update >>>> " + trans_id);
	          	   	String subjectT= adGroup.subjectToVat(group_id);
	          	   	String whT= adGroup.whTax(group_id);
	          	   	String Name =approvalRec.getCodeName(" SELECT full_name from am_gb_user where user_id='"+user_id+"'");
	          	   	String url = "DocumentHelp.jsp?np=groupAssetPosting&amp;id=" + group_id + "&pageDirect=Y";
	      	  		
	          	  approvalRec.insertApproval 
	      	  		(group_id, approvalResult[5], page1, flag, partPay,Name, Branch, subjectT, whT, url,Integer.parseInt(trans_id)); 
	          	  
	          	  
      	  			String qry = upd_am_asset_status_RaiseEntry + group_id;
      	  			String qry2 = upd_am_grp_asset_RaiseEntry + group_id;
      	  			String qry3 = upd_am_grp_asset_main_RaiseEntry +  group_id;
	      	  		/*System.out.println("qry : " + qry);
	  	  			System.out.println("qry2 : " + qry2 );
	  	  			System.out.println("qry3 : " + qry3);*/
	      	  		updateStatusUtil(qry);
	      	  		updateStatusUtil(qry2);
	      	  		updateStatusUtil(qry3);
	      	  		
	      	  	}
      	  	
	      	if((status)&&(approval_level_val))
		      	{
		      	 System.out.println("====== UPDATING  am_asset_Approval For Rejection ======");
		      	 changeGroupAssetStatus(group_id,"PENDING");
		      	 updateGroupPendingTrans(ad.setApprovalDataGroup(Long.parseLong(group_id)),"3",group_id);
		      	 //write a method to change status to pending in am_approval
		      	 //the amount in approval is the old amount so the best thing is to read again from the table
		      	 //and update the new values
		      	 //UPDATE THE SUPERVISOR VALUE !!!!!!!!!!!!!!!!
		      	}	
        } 
        catch (Exception ex)
        {
            done = false;
            System.out.println("WARN:GroupInventoryToStockBean - Error Updating am_asset->" + ex);
        }
        finally 
        {
            dbConnection.closeConnection(con, ps);
        }

        return done;

    }

    public void updateGroupPendingTrans(String[] Approv,String code,String grpID) 
    {
    	int transaction_level=0;
    	String update_Approval_Qry="update am_asset_approval set user_id=?,super_id=?,amount=?," +
    			"posting_date=?,description=?,effective_date=?,branchCode=?," +
    			"asset_status=?,tran_type=?,process_status=?,tran_sent_time=?,transaction_level=?,reject_reason=?," +
    			"approval1=?,approval2=?,approval3=?,approval4=?,approval5=?,approval_level_count=? "+
    			"where asset_id=?";
    	String tranLevelQuery = "select level from approval_level_setup where code ='"+code+"'";
    	Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");
    	try
    	{
    		con = dbConnection.getConnection("legendPlus");
    		ps = con.prepareStatement(tranLevelQuery);
            rs = ps.executeQuery();
            while(rs.next())
            {
            	transaction_level = rs.getInt(1);
            }
            ps = con.prepareStatement(update_Approval_Qry);
            ps.setString(1, (Approv[1]==null)?"":Approv[1]);
            ps.setString(2, (Approv[2]==null)?"":Approv[2]);
            ps.setDouble(3, (Approv[3]==null)?0:Double.parseDouble(Approv[3]));
            ps.setDate(4, (Approv[4])==null?null:dbConnection.dateConvert(Approv[4]));
            ps.setString(5, (Approv[5]==null)?"":Approv[5]);
            ps.setDate(6,(Approv[6])==null?null:dbConnection.dateConvert(Approv[6]));
            ps.setString(7, (Approv[7]==null)?"":Approv[7]);
            ps.setString(8, (Approv[8]==null)?"":Approv[8]);
            ps.setString(9, (Approv[9]==null)?"":Approv[9]);
            ps.setString(10, Approv[10]);
            ps.setString(11, timer.format(new java.util.Date()));
            ps.setInt(12, transaction_level);
            ps.setString(13,"");
            ps.setString(14, "");
            ps.setString(15, "");
            ps.setString(16, "");
            ps.setString(17, "");
            ps.setString(18, "");
            ps.setInt(19, 0);
            ps.setString(20, grpID);
            
            ps.executeUpdate();
            
    	}
		catch (Exception ex)
		{
			System.out.println("WARN:GroupInventoryToStockBean -Error Updating am_asset_approval->" + ex);
		}
		finally 
        {
            dbConnection.closeConnection(con, ps);
        }
	}

	public void updateStatusUtil(String query) 
    {
    	Connection con = null;
        PreparedStatement ps = null;
        try 
        {
        	con = dbConnection.getConnection("legendPlus");
        	ps = con.prepareStatement(query);
            int i =ps.executeUpdate();
         } 
        catch (Exception ex)
        {
            System.out.println("GroupInventoryToStockBean: updateStatusUtil -" + ex);
        }
        finally 
        {
            dbConnection.closeConnection(con, ps);
        }
	}

	public boolean updateCreatedAssetStatus(String old_asset_id, String group_id2, String new_asset_id)
    {
		// TODO Auto-generated method stub
    	Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean status = false;
        int chk_Process_flag=0;
        String process_flag ="N";
        
        String update_created_asset_qry ="update am_group_stock set process_flag= ?,asset_id=?,REGISTRATION_NO=?, " + 	  
        "BRANCH_ID=?, DEPT_ID=?,SECTION_ID=?, CATEGORY_ID=?,DESCRIPTION=?, VENDOR_AC=?," +
        "DATE_PURCHASED=?, DEP_RATE=?, ASSET_MAKE=?, ASSET_MODEL=?,"+ 
        "ASSET_SERIAL_NO=?, ASSET_ENGINE_NO=?, SUPPLIER_NAME=?," +
        "ASSET_USER=?, ASSET_MAINTENANCE=?, ACCUM_DEP=?," +
        "COST_PRICE=?, DEP_END_DATE=?, RESIDUAL_VALUE=?," +
        "AUTHORIZED_BY=?, POSTING_DATE=?, EFFECTIVE_DATE=?, PURCHASE_REASON=?," +
        " LOCATION=?, " +
        "VATABLE_COST=?,VAT=?, WH_TAX=?, WH_TAX_AMOUNT=?, REQ_DEPRECIATION=?," +
        "REQ_REDISTRIBUTION=?, SUBJECT_TO_VAT=?, WHO_TO_REM=?, EMAIL1=?," +
        "WHO_TO_REM_2=?, EMAIL2=?, RAISE_ENTRY=?, DEP_YTD=?, SECTION=?," +
        "STATE=?, DRIVER=?, SPARE_1=?, SPARE_2=?, [USER_ID]=?," +
        "PROVINCE=?, WAR_START_DATE=?, WAR_MONTH=?, " +
        "WAR_EXPIRY_DATE=?,LPO=?,BAR_CODE=? ,BRANCH_CODE=?,CATEGORY_CODE=? ," +
        "GROUP_ID=?,PART_PAY=?,FULLY_PAID=?,DEFER_PAY=?,SBU_CODE=?,[dept_code] = ?,[section_code] = ?, "+
        "SPARE_3=?, SPARE_4=?, SPARE_5=?, SPARE_6=?,SUB_CATEGORY_ID = ?,SUB_CATEGORY_CODE=?  where asset_id = ? " ;

        String update_created_asset_ARCHIVE_qry ="update am_group_stock_ARCHIVE set process_flag= ?,asset_id=?,REGISTRATION_NO=?, " +
        "BRANCH_ID=?, DEPT_ID=?,SECTION_ID=?, CATEGORY_ID=?,DESCRIPTION=?, VENDOR_AC=?," +
        "DATE_PURCHASED=?, DEP_RATE=?, ASSET_MAKE=?, ASSET_MODEL=?,"+
        "ASSET_SERIAL_NO=?, ASSET_ENGINE_NO=?, SUPPLIER_NAME=?," +
        "ASSET_USER=?, ASSET_MAINTENANCE=?, ACCUM_DEP=?," +
        "COST_PRICE=?, DEP_END_DATE=?, RESIDUAL_VALUE=?," +
        "AUTHORIZED_BY=?, POSTING_DATE=?, EFFECTIVE_DATE=?, PURCHASE_REASON=?," +
        " LOCATION=?, " +
        "VATABLE_COST=?,VAT=?, WH_TAX=?, WH_TAX_AMOUNT=?, REQ_DEPRECIATION=?," +
        "REQ_REDISTRIBUTION=?, SUBJECT_TO_VAT=?, WHO_TO_REM=?, EMAIL1=?," +
        "WHO_TO_REM_2=?, EMAIL2=?, RAISE_ENTRY=?, DEP_YTD=?, SECTION=?," +
        "STATE=?, DRIVER=?, SPARE_1=?, SPARE_2=?, [USER_ID]=?," +
        "PROVINCE=?, WAR_START_DATE=?, WAR_MONTH=?, " +
        "WAR_EXPIRY_DATE=?,LPO=?,BAR_CODE=? ,BRANCH_CODE=?,CATEGORY_CODE=? ," +
        "GROUP_ID=?,PART_PAY=?,FULLY_PAID=?,DEFER_PAY=?,SBU_CODE=?,[dept_code] = ?,[section_code] = ?, "+
        "SPARE_3=?, SPARE_4=?, SPARE_5=?, SPARE_6=?,SUB_CATEGORY_ID = ?,SUB_CATEGORY_CODE=?  where asset_id = ? " ;

        String chk_process_flag ="select count(*) from am_group_stock WHERE process_flag='" 
        						+ process_flag + "'" +"  and Group_id ='"+group_id2+"'";
        
        String update_created_asset_main_qry = "update am_group_stock_main set process_flag =? "+
       									" where group_id = ?";
        
        String update_created_asset_main_ARCHIVE_qry = "update am_group_stock_main_archive set process_flag =? "+
       									" where group_id = ?";
        try
	    {
        	con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(update_created_asset_qry);
            ps.setString(1, "Y");
            ps.setString(2, new_asset_id);
            ps.setString(3, registration_no);
            ps.setInt(4, Integer.parseInt(branch_id));
            ps.setInt(5, Integer.parseInt(department_id));
            ps.setInt(6, Integer.parseInt(section_id));
            ps.setInt(7, Integer.parseInt(category_id));
            ps.setString(8, description);
            ps.setString(9, vendor_account);
            ps.setDate(10, dbConnection.dateConvert(date_of_purchase));
            ps.setString(11, getDepreciationRate(category_id));
            ps.setString(12, make);
            ps.setString(13, model);
            ps.setString(14, serial_number);
            ps.setString(15, engine_number);
            ps.setInt(16, Integer.parseInt(supplied_by));
            ps.setString(17, user);
            ps.setInt(18, Integer.parseInt(maintained_by));
            ps.setInt(19, 0);
            ps.setDouble(20, asset_costPrice);
            ps.setDate(21, dbConnection.dateConvert(depreciation_end_date));
            ps.setDouble(22, Double.parseDouble(residual_value));
            ps.setString(23, authorized_by);
            ps.setDate(24, dbConnection.dateConvert(new java.util.Date()));
            ps.setDate(25, dbConnection.dateConvert(depreciation_start_date));
            ps.setString(26, reason);
            ps.setInt(27, Integer.parseInt(location));
            ps.setDouble(28, Double.parseDouble(vatable_cost));
            ps.setDouble(29, Double.parseDouble(vat_amount));
            ps.setString(30, wh_tax_cb);
            ps.setDouble(31, Double.parseDouble(wh_tax_amount));
            ps.setString(32, require_depreciation);
            ps.setString(33, require_redistribution);
            ps.setString(34, subject_to_vat);
            ps.setString(35, who_to_remind);
            ps.setString(36, email_1);
            ps.setString(37, who_to_remind_2);
            ps.setString(38, email2);
            ps.setString(39, "N");
            ps.setString(40, "0");
            ps.setString(41, section);
            ps.setInt(42, Integer.parseInt(state));
            ps.setInt(43, Integer.parseInt(driver));
            ps.setString(44, spare_1);
            ps.setString(45, spare_2);
           // ps.setString(46, "ACTIVE");
            ps.setString(46, user_id);
      
            ps.setString(47, province);
            ps.setDate(48, dbConnection.dateConvert(warrantyStartDate));
            ps.setInt(49, Integer.parseInt(noOfMonths));
            ps.setDate(50, dbConnection.dateConvert(expiryDate));
            ps.setString(51,lpo);
            ps.setString(52,bar_code);
            ps.setString(53,code.getBranchCode(branch_id));
            ps.setString(54,code.getCategoryCode(category_id));
            ps.setString(55,group_id);
            ps.setString(56, partPAY);
            ps.setString(57, fullyPAID);
            ps.setString(58, deferPay);
            ps.setString(59,sbu_code);
            ps.setString(60,code.getDeptCode(department_id));
            ps.setString(61,code.getSectionCode(section_id));
            ps.setString(62, spare_3);
            ps.setString(63, spare_4);
            ps.setString(64, spare_5);
            ps.setString(65, spare_6);
            ps.setInt(66, Integer.parseInt(subcategory_id));
            ps.setString(67,code.getSubCategoryCode(subcategory_id));
            
            ps.setString(68, old_asset_id);
            int result = ps.executeUpdate();

            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(update_created_asset_ARCHIVE_qry);
            ps.setString(1, "Y");
            ps.setString(2, new_asset_id);
            ps.setString(3, registration_no);
            ps.setInt(4, Integer.parseInt(branch_id));
            ps.setInt(5, Integer.parseInt(department_id));
            ps.setInt(6, Integer.parseInt(section_id));
            ps.setInt(7, Integer.parseInt(category_id));
            ps.setString(8, description);
            ps.setString(9, vendor_account);
            ps.setDate(10, dbConnection.dateConvert(date_of_purchase));
            ps.setString(11, getDepreciationRate(category_id));
            ps.setString(12, make);
            ps.setString(13, model);
            ps.setString(14, serial_number);
            ps.setString(15, engine_number);
            ps.setInt(16, Integer.parseInt(supplied_by));
            ps.setString(17, user);
            ps.setInt(18, Integer.parseInt(maintained_by));
            ps.setInt(19, 0);
            ps.setDouble(20, asset_costPrice);
            ps.setDate(21, dbConnection.dateConvert(depreciation_end_date));
            ps.setDouble(22, Double.parseDouble(residual_value));
            ps.setString(23, authorized_by);
            ps.setDate(24, dbConnection.dateConvert(new java.util.Date()));
            ps.setDate(25, dbConnection.dateConvert(depreciation_start_date));
            ps.setString(26, reason);
            ps.setInt(27, Integer.parseInt(location));
            ps.setDouble(28, Double.parseDouble(vatable_cost));
            ps.setDouble(29, Double.parseDouble(vat_amount));
            ps.setString(30, wh_tax_cb);
            ps.setDouble(31, Double.parseDouble(wh_tax_amount));
            ps.setString(32, require_depreciation);
            ps.setString(33, require_redistribution);
            ps.setString(34, subject_to_vat);
            ps.setString(35, who_to_remind);
            ps.setString(36, email_1);
            ps.setString(37, who_to_remind_2);
            ps.setString(38, email2);
            ps.setString(39, "N");
            ps.setString(40, "0");
            ps.setString(41, section);
            ps.setInt(42, Integer.parseInt(state));
            ps.setInt(43, Integer.parseInt(driver));
            ps.setString(44, spare_1);
            ps.setString(45, spare_2);
           // ps.setString(46, "ACTIVE");
            ps.setString(46, user_id);

            ps.setString(47, province);
            ps.setDate(48, dbConnection.dateConvert(warrantyStartDate));
            ps.setInt(49, Integer.parseInt(noOfMonths));
            ps.setDate(50, dbConnection.dateConvert(expiryDate));
            ps.setString(51,lpo);
            ps.setString(52,bar_code);
            ps.setString(53,code.getBranchCode(branch_id));
            ps.setString(54,code.getCategoryCode(category_id));
            ps.setString(55,group_id);
            ps.setString(56, partPAY);
            ps.setString(57, fullyPAID);
            ps.setString(58, deferPay);
            ps.setString(59,sbu_code);
            ps.setString(60,code.getDeptCode(department_id));
            ps.setString(61,code.getSectionCode(section_id));
            ps.setString(62, spare_3);
            ps.setString(63, spare_4);
            ps.setString(64, spare_5);
            ps.setString(65, spare_6);
            ps.setInt(66, Integer.parseInt(subcategory_id));
            ps.setString(67,code.getSubCategoryCode(subcategory_id));
            
            ps.setString(68, old_asset_id);
           // System.out.println("RESULT AFTER UPDATING :::::::::::::::: " + result);
            /*
             * chk if all the entries in am_group_asset have been updated
             * update am_group_asset_main process_flag to Y
             * chk if the approval level setup isn't zero
             * call ad.setPendingtrans
             */
            System.out.println("chk_process_flag qry :::::: " + chk_process_flag);
            ps = con.prepareStatement(chk_process_flag);
            rs = ps.executeQuery();
            if (rs.next()) 
            {
            	chk_Process_flag = rs.getInt(1);
	            if(chk_Process_flag == 0)
	            {
	            	System.out.println("Nothing to update in am_group_stock!!!!!!");
	            	ps = con.prepareStatement(update_created_asset_main_qry);
	            	ps.setString(1, "Y");
	            	ps.setString(2, group_id2);
	            	ps.executeUpdate();

                        ps = con.prepareStatement(update_created_asset_main_ARCHIVE_qry);
	            	ps.setString(1, "Y");
	            	ps.setString(2, group_id2);
	            	ps.executeUpdate();
                        
	            	status = true;
	            }
            }
	    }
        catch(Exception ex) 
        {
            System.out.println("WARN: Error updateCreatedStockStatus ->" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }
        return status;
	}

	public void changeGroupAssetStatus(String id,String status) 
	{
		// TODO Auto-generated method stub
		Connection con = null;
	    PreparedStatement ps = null;
	    String query_r ="update am_group_stock set asset_status=? " +
		" where Group_id = '"+id+"'";

            String query_archive="update am_group_stock_archive set asset_status=? " +
		" where Group_id = '"+id+"'";
	    try 
	    	{
	    	con = dbConnection.getConnection("legendPlus");
	    	ps = con.prepareStatement(query_r);
	    	ps.setString(1,status);
	       	int i =ps.executeUpdate();

                ps = con.prepareStatement(query_archive);
	    	ps.setString(1,status);
	       	i =ps.executeUpdate();

	        changeGroupAssetMainStatus(id,status);
	        } 
		catch (Exception ex)
		    {
		        System.out.println("GroupInventoryToStockBean: Error Updating am_group_stock " + ex);
		    } 
		finally 
			{
	            dbConnection.closeConnection(con, ps);
	        }

		
	}

	public void changeGroupAssetMainStatus(String id, String status2)
	{
		// TODO Auto-generated method stub
		String query_r ="update am_group_stock_main set asset_status=? " +
		"where Group_id = '"+id+"'";

                String query_archive ="update am_group_stock_main_archive set asset_status=? " +
		"where Group_id = '"+id+"'";
                
		Connection con = null;
	    PreparedStatement ps = null;
	    try 
		{
		con = dbConnection.getConnection("legendPlus");
		ps = con.prepareStatement(query_r);
		ps.setString(1,status2);
	    int i =ps.executeUpdate();

            ps = con.prepareStatement(query_archive);
		ps.setString(1,status2);
	     i =ps.executeUpdate();

	    } 
	    catch (Exception ex)
	    {
	        System.out.println("GroupInventoryToStockBean: Error Updating am_group_stock_main : " + ex);
	    } 
	    finally 
		{
	        dbConnection.closeConnection(con, ps);
	    }
	}

	public String computeTotalLife(String depRate) {

        String totalLife = "0";
        if (depRate == null || depRate.equals("")) {
            depRate = "0.0";
        }

        double division = 100 / (Double.parseDouble(depRate));
        int intTotal = (int) (division * 12);

        totalLife = Integer.toString(intTotal);

        return totalLife;

    }

    private void confirmGroupAssetDelete(String group_id) throws Exception
    {
    	System.out.println(" ================= Called ConfirmGroupAssetDelete ===================");
    	Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String cnt_query = " Select COUNT(*), SUM([Cost_Price]),SUM([wh_tax_amount])," +
        		" SUM([amount_rem]), SUM([vatable_cost]), SUM(vat) " +
        		" from am_group_stock "
        	+ "where Group_id = " + group_id;
        String del_query ="Delete from am_group_stock_main where group_id = " +	group_id ;
        
        String update_grp_asset_main_qry ="update am_group_stock_main set process_flag= ? " + 
        									"  where group_id = ? " ;
        
        String del_RaiseEntry ="Delete from am_raisentry_post where Id = "+"'" + group_id + "'" ;
        
        System.out.println("cnt_query >>>> " + cnt_query);
        System.out.println("del_query >>>> "  + del_query);
        System.out.println("del_RaiseEntry >>>> " + del_RaiseEntry);
        try 
        {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(cnt_query);
            rs = ps.executeQuery();
            if (rs.next()) 
            {
            	int count = rs.getInt(1);
            	double cost_price = rs.getDouble(2);
            	double amount_rem = rs.getDouble(4);
            	double wh_tax_amount = rs.getDouble(3);
            	double vatable_cost = rs.getDouble(5);
            	double vat = rs.getDouble(6);
            	System.out.println("No of Stock Left in Am_Group_Asset : " + count);
            	System.out.println("Cost_Price in Am_Group_Stock : " + cost_price);
            	System.out.println("Amount_rem in Am_Group_Stock : " + amount_rem);
            	System.out.println("Wh_tax_amount in Am_Group_Stock : " + wh_tax_amount);
            	System.out.println("vatable_cost in Am_Group_Stock : " + vatable_cost);
            	System.out.println("vat in Am_Group_Stock : " + vat);
            	if(count < 1)
            	{
            		System.out.println("======== Updating in Am_Group_Stock_Main ==========");
            		ps = con.prepareStatement(update_grp_asset_main_qry);
            		ps.setString(1, "Y");
            		ps.setLong(2, Long.parseLong(group_id));
            		ps.executeUpdate();
            	}
            	else
            	{
            		 String update_qry ="update am_group_stock_main set quantity = ? " +
            		 		" ,AMOUNT_REM =? ,Cost_Price =? ,wh_tax_amount=? ," +
            		 		" Vatable_Cost =? ,Vat = ?  "  +
            		 					"  where group_id = ? " ;
            		 System.out.println("======== Updating Am_Group_Stock_Main ==========");
            		 ps = con.prepareStatement(update_qry);
            		 ps.setInt(1, count);
            		 ps.setDouble(2,amount_rem );
            		 ps.setDouble(3,cost_price );
            		 ps.setDouble(4,wh_tax_amount );
            		 ps.setDouble(5,vatable_cost );
            		 ps.setDouble(6,vat );
            		 ps.setLong(7, Long.parseLong(group_id));
            		 ps.executeUpdate();
            		 System.out.println("Finished Updating Am_Group_Stock Main");
            	}
            }
        }
        catch (Exception e)
        {
            System.out.println("INFO:Error Confirming Group Stock Delete ->" +
                               e.getMessage());
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }
	}

	/**
     * getDepreciationRate
     *
     * @param category_id String
     * @return String
     */
    private String getDepreciationRate(String category_id) throws Exception {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String rate = "0.0";
        String query = "SELECT DEP_RATE FROM AM_AD_CATEGORY " +
                       "WHERE CATEGORY_ID = " + category_id;
        System.out.println("<<<<<query: "+query);
        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                rate = rs.getString(1);
            }

        } catch (Exception ex) {
            System.out.println("WARN: Error fetching DepreciationRate ->" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }

        return rate;
    }

    /**
     * residualValue
     *
     * @return String
     */
    public String findResidualValue() {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String residual = "0.0";

        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(
                    "select residual_value from am_gb_company");
            rs = ps.executeQuery();

            while (rs.next()) {
                residual = rs.getString(1);
            }

        } catch (Exception ex) {
            System.out.println("WARN: Error fetching all stock ->" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }
        return residual;
    }

    
    public void updateVatableCostBalance(String vat_cost_balance ,String vat_cost,String groupId,String Count)
    {
    	double vat_cost_difference = ((Double.parseDouble(vat_cost_balance)) - (Double.parseDouble(vat_cost)));
    	System.out.println("Vatable Cost Difference : " + vat_cost_difference);
    	String updateQry = "update am_group_stock_main set Vatable_Cost_Bal = ? ,pend_GrpAssets=?  where group_id = ? ";
    	Connection con = null;
        PreparedStatement ps = null;
        try
	    {
        	con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(updateQry);
            ps.setDouble(1, vat_cost_difference);
            ps.setInt(2, Integer.parseInt(Count));
            ps.setLong(3, Long.parseLong(groupId));
            ps.executeUpdate();
	    }
        catch(Exception ex) 
        {
            System.out.println("WARN: Error updateVatableCostBalance ->" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }
    }
    
    public String []getVatableCostBalance(String query)
    {
    	System.out.println("Inside getVatableCostBalance : " + query);
    	Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String [] result = new String [2];
        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            if (rs.next()) 
            {
            	result[0] = Integer.toString(rs.getInt(1));
            	result[1] = Double.toString(rs.getDouble(2));
            }

        } catch (Exception ex) {
            System.out.println("WARN: Error fetching VatableCostBalance ->" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }
    	return result;
    }

    public String getUnprocessedGroupAsset(String query)
    {
    	//System.out.println("Inside getUnprocessedGroupAsset : " + query);
    	Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String  result = "";
        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            if (rs.next()) 
            {
            	result = rs.getString(1);
            }

        } catch (Exception ex) {
            System.out.println("WARN: Error fetching getUnprocessedGroupAsset ->" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }
    	return result;
    }
    private boolean checkApprovalStatus(String code)
    {
		// TODO Auto-generated method stub
		boolean status = false;
		String approval_status_qry = "select level from approval_level_setup where code ='"+code+"'";
		Connection con = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    int level = 0;
	    try
	    {
	    	con = dbConnection.getConnection("legendPlus");
	    	ps = con.prepareStatement(approval_status_qry);
	    	rs = ps.executeQuery();
	    	if(rs.next())
	    	{
	    		level= rs.getInt(1);
	    	}
	    	if (level > 0)
	    	{
	    		status = true;
	    	}
	    }
	    catch(Exception ex) 
	    {
	        System.out.println("WARN: Error checkApprovalStatus ->" + ex);
	    }
	    finally 
	    {
	        dbConnection.closeConnection(con, ps);
	    }   
	    return status;
	}
    
    public ArrayList retrieveGroupAsset(String grp_id)
    {
    	ArrayList _list = new ArrayList();
    	String qry ="select Asset_id,Description,Subject_TO_Vat,wh_tax,Branch_ID,user_ID from " +
    			"am_group_stock where Group_id =" + grp_id;
    	//System.out.println("retrieveGroupAsset_qry ::::: " + qry);
    	Connection con = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    GroupInventoryToStockBean grpAsset;
    	try
    	{
    		grpAsset = new GroupInventoryToStockBean();
    		con = dbConnection.getConnection("legendPlus");
	    	ps = con.prepareStatement(qry);
	    	rs = ps.executeQuery();
	    	while(rs.next())
	    	{
	    		grpAsset.setAsset_id_new(rs.getString(1));
	    		grpAsset.setDescription(rs.getString(2));
	    		grpAsset.setSubject_to_vat(rs.getString(3));
	    		grpAsset.setWh_tax_cb(rs.getString(4));
	    		grpAsset.setBranch_id(rs.getString(5));
	    		grpAsset.setUser_id(rs.getString(6));
	    		_list.add(grpAsset);
	    	}
	    	
    	}
	    catch(Exception ex) 
	    {
	        System.out.println("WARN: Error checkApprovalStatus ->" + ex);
	    }
	    finally 
	    {
	        dbConnection.closeConnection(con, ps);
	    }  
    	return _list;
    }
    
    public boolean repostCreatedAsset(String asset_id, String group_id2)
    {
		// TODO Auto-generated method stub
    	Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean status = false;
        int chk_Process_flag=0;
        String process_flag ="N";
        
        String update_created_asset_qry ="update am_group_stock set process_flag= ?,REGISTRATION_NO=?, " + 	  
        " DEPT_ID=?,SECTION_ID=?, DESCRIPTION=?, VENDOR_AC=?," +
        "DATE_PURCHASED=?, ASSET_MAKE=?, ASSET_MODEL=?,"+ 
        "ASSET_SERIAL_NO=?, ASSET_ENGINE_NO=?, SUPPLIER_NAME=?," +
        "ASSET_USER=?, ASSET_MAINTENANCE=?, ACCUM_DEP=?," +
        "COST_PRICE=?, DEP_END_DATE=?, RESIDUAL_VALUE=?," +
        "AUTHORIZED_BY=?, POSTING_DATE=?, EFFECTIVE_DATE=?, PURCHASE_REASON=?," +
        " LOCATION=?, " +
        "VATABLE_COST=?,VAT=?, WH_TAX=?, WH_TAX_AMOUNT=?, REQ_DEPRECIATION=?," +
        "REQ_REDISTRIBUTION=?, SUBJECT_TO_VAT=?, WHO_TO_REM=?, EMAIL1=?," +
        "WHO_TO_REM_2=?, EMAIL2=?, RAISE_ENTRY=?, DEP_YTD=?, SECTION=?," +
        "STATE=?, DRIVER=?, SPARE_1=?, SPARE_2=?, [USER_ID]=?," +
        "PROVINCE=?, WAR_START_DATE=?, WAR_MONTH=?, " +
        "WAR_EXPIRY_DATE=?,LPO=?,BAR_CODE=? ," +
        "PART_PAY=?,FULLY_PAID=?,DEFER_PAY=?,SBU_CODE=?,[dept_code] = ?,[section_code] = ?, "+
        "SPARE_3=?, SPARE_4=?, SPARE_5=?, SPARE_6=?,SUB_CATEGORY_ID = ?,SUB_CATEGORY_CODE=?   where asset_id = ? " ;
        String chk_process_flag ="select count(*) from am_group_stock WHERE process_flag='" 
        						+ process_flag + "'" +"  and Group_id ='"+group_id2+"'";
        
        String update_created_asset_main_qry = "update am_group_stock_main set process_flag =? "+
       									" where group_id = ?";
        try
	    {
        	con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(update_created_asset_qry);
            ps.setString(1, "Y");
            ps.setString(2, registration_no);
            ps.setInt(3, Integer.parseInt(department_id));
            ps.setInt(4, Integer.parseInt(section_id));
            ps.setString(5, description);
            ps.setString(6, vendor_account);
            ps.setDate(7, dbConnection.dateConvert(date_of_purchase));
            ps.setString(8, make);
            ps.setString(9, model);
            ps.setString(10, serial_number);
            ps.setString(11, engine_number);
            ps.setInt(12, Integer.parseInt(supplied_by));
            ps.setString(13, user);
            ps.setInt(14, Integer.parseInt(maintained_by));
            ps.setInt(15, 0);
            ps.setDouble(16, asset_costPrice);
            ps.setDate(17, dbConnection.dateConvert(depreciation_end_date));
            ps.setDouble(18, Double.parseDouble(residual_value));
            ps.setString(19, authorized_by);
            ps.setDate(20, dbConnection.dateConvert(new java.util.Date()));
            ps.setDate(21, dbConnection.dateConvert(depreciation_start_date));
            ps.setString(22, reason);
            ps.setInt(23, Integer.parseInt(location));
            ps.setDouble(24, Double.parseDouble(vatable_cost));
            ps.setDouble(25, Double.parseDouble(vat_amount));
            ps.setString(26, wh_tax_cb);
            ps.setDouble(27, Double.parseDouble(wh_tax_amount));
            ps.setString(28, require_depreciation);
            ps.setString(29, require_redistribution);
            ps.setString(30, subject_to_vat);
            ps.setString(31, who_to_remind);
            ps.setString(32, email_1);
            ps.setString(33, who_to_remind_2);
            ps.setString(34, email2);
            ps.setString(35, "N");
            ps.setString(36, "0");
            ps.setString(37, section);
            ps.setInt(38, Integer.parseInt(state));
            ps.setInt(39, Integer.parseInt(driver));
            ps.setString(40, spare_1);
            ps.setString(41, spare_2);
           // ps.setString(46, "ACTIVE");
            ps.setString(42, user_id);
      
            ps.setString(43, province);
            ps.setDate(44, dbConnection.dateConvert(warrantyStartDate));
            ps.setInt(45, Integer.parseInt(noOfMonths));
            ps.setDate(46, dbConnection.dateConvert(expiryDate));
            ps.setString(47,lpo);
            ps.setString(48,bar_code);
            
            
            ps.setString(49, partPAY);
            ps.setString(50, fullyPAID);
            ps.setString(51, deferPay);
            ps.setString(52,sbu_code);
            ps.setString(53,code.getDeptCode(department_id));
            ps.setString(54,code.getSectionCode(section_id));
            ps.setString(55, spare_3);
            ps.setString(56, spare_4);
            ps.setString(57, spare_5);
            ps.setString(58, spare_6);
            ps.setInt(59, Integer.parseInt(subcategory_id));
            ps.setString(60,code.getSubCategoryCode(subcategory_id));

            ps.setString(61, asset_id);
            int result = ps.executeUpdate();
           // System.out.println("RESULT AFTER UPDATING :::::::::::::::: " + result);
            /*
             * chk if all the entries in am_group_asset have been updated
             * update am_group_asset_main process_flag to Y
             * chk if the approval level setup isn't zero
             * call ad.setPendingtrans
             */
            System.out.println("chk_process_flag qry :::::: " + chk_process_flag);
            ps = con.prepareStatement(chk_process_flag);
            rs = ps.executeQuery();
            if (rs.next()) 
            {
            	chk_Process_flag = rs.getInt(1);
	            if(chk_Process_flag == 0)
	            {
	            	System.out.println("Nothing to update in am_group_stock!!!!!!");
	            	ps = con.prepareStatement(update_created_asset_main_qry);
	            	ps.setString(1, "Y");
	            	ps.setString(2, group_id2);
	            	ps.executeUpdate();
	            	status = true;
	            }
            }
	    }
        catch(Exception ex) 
        {
            System.out.println("WARN: Error updateCreatedStockStatus ->" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }
        return status;
	}
 
    
    public boolean deleteRaiseEntry(String grp_id,String page1) 
    {
		  boolean done = false;
	       Connection con = null;
	       PreparedStatement ps = null;
	//       String delete_QUERY ="delete from  am_raisentry_post  WHERE ID = "+ grp_id + " and page='"+ page1.trim() +"'";
	       String delete_QUERY ="delete from  am_raisentry_post  WHERE ID = '"+ grp_id + "' ";
	       System.out.println("delete_QUERY >>>>>> " + delete_QUERY);
	       try {
	           con =  dbConnection.getConnection("legendPlus");
	           ps = con.prepareStatement(delete_QUERY);
	           int result = ps.executeUpdate();
	           System.out.println("result of delete >>>> " + result);
	           if (result>0)
	           {
	        	   done=true;
	           }
	       } catch (Exception ex)
	       {
	    	   done=false;
	           System.out.println("WARNING: cannot delete am_raisentry_post+"+ex.getMessage());
	       } 
	       finally
	       {
	    	   dbConnection.closeConnection(con, ps);
	       }
	       return done;
	   }
   
    public boolean updateAsset2(String assetID) throws Exception, Throwable {
    	/*System.out.println("branch_id ::::::::: " + branch_id);
        System.out.println("department_id ::::::::: " + department_id);
        System.out.println("section_id ::::::::: " + section_id);
        System.out.println("category_id ::::::::: " + category_id);
        System.out.println("Branch Code : " +code.getBranchCode(branch_id));
        System.out.println("Category Code " + code.getCategoryCode(category_id));*/
         //System.out.println("New Asset_ID ::::::::: " + asset_id_new);
        Connection con = null;
        PreparedStatement ps = null;
        boolean done = true;
       
        if (make == null || make.equals("")) {
            make = "0";
        }
        if (maintained_by == null || maintained_by.equals("")) {
            maintained_by = "0";
        }
        if (supplied_by == null || supplied_by.equals("")) {
            supplied_by = "0";
        }
        if (user == null || user.equals("")) {
            user = "";
        }
        if (location == null || location.equals("")) {
            location = "0";
        }
        if (driver == null || driver.equals("")) {
            driver = "0";
        }
        if (state == null || state.equals("")) {
            state = "0";
        }
        if (department_id == null || department_id.equals("")) {
            department_id = "0";
        }
        if (vat_amount == null || vat_amount.equals("")) {
            vat_amount = "0.0";
        }
        if (vatable_cost == null || vatable_cost.equals("")) {
            vatable_cost = "0.0";
        }
        if (wh_tax_amount == null || wh_tax_amount.equals("")) {
            wh_tax_amount = "0";
        }
        if (branch_id == null || branch_id.equals("")) {
            branch_id = "0";
        }
        if (province == null || province.equals("")) {
            province = "0";
        }
        if (category_id == null || category_id.equals("")) {
            category_id = "0";
        }
        
        if (residual_value == null || residual_value.equals("")) {
            residual_value = "0";
        }
        if (noOfMonths == null || noOfMonths.equals("")) {
            noOfMonths = "0";
        }
        if (warrantyStartDate == null || warrantyStartDate.equals("")) {
            warrantyStartDate = null;
        }
        if (expiryDate == null || expiryDate.equals("")) {
            expiryDate = null;
        }
       // vat_amount = vat_amount.replaceAll(",", "");
        //vatable_cost = vatable_cost.replaceAll(",", "");
        wh_tax_amount = wh_tax_amount.replaceAll(",", "");
       // residual_value = residual_value.replaceAll(",", "");

        boolean approval_level_val =checkApprovalStatus("3");
        String chk_Asset_Status="APPROVED";
        if (approval_level_val)
        {
        	chk_Asset_Status="PENDING";
        }
        
        String createQuery = "Update ST_STOCK " +
        " set BAR_CODE=?,REGISTRATION_NO=?,Multiple=?,Asset_Serial_No=?, DESCRIPTION=?,Asset_Engine_No=? ," +
        " LPO=?,ASSET_MAKE=?,LOCATION=?,ASSET_MODEL=?, STATE=?,DRIVER=?," +
        " ASSET_MAINTENANCE=?,SPARE_1=?, SPARE_2=?, WAR_START_DATE=?,WAR_MONTH=?,WAR_EXPIRY_DATE=?,"+
        " WH_TAX=?,WH_TAX_AMOUNT=?,PART_PAY=?,FULLY_PAID=?,DEFER_PAY=?,  "+
        " DATE_PURCHASED=?,SUPPLIER_NAME=?,WHO_TO_REM=?, EMAIL1=?, VENDOR_AC=?," +
        " WHO_TO_REM_2=?, EMAIL2=?,EFFECTIVE_DATE=?,DEP_END_DATE=?,AUTHORIZED_BY=?,"+
        " PURCHASE_REASON=?,ASSET_USER=?,REQ_DEPRECIATION=?, REQ_REDISTRIBUTION=?,PROVINCE=?, " +
        " ASSET_STATUS=?,[USER_ID]=?,SECTION_CODE=?,SECTION_ID=?,SBU_CODE=?, DEPT_ID=?,DEPT_CODE=?, "+
        " SPARE_3=?, SPARE_4=?, SPARE_5=?, SPARE_6=?,SUB_CATEGORY_ID = ?,SUB_CATEGORY_CODE=?  WHERE ASSET_ID= ?"  ;
       

        //System.out.println("createQuery >>>>>  " + createQuery);
        /*
         *First Create Asset Records
         * and then determine if it
         * should be made available for fleet.
         */
           try 
           {
            asset_costPrice = Double.parseDouble(vat_amount) +
                               Double.parseDouble(vatable_cost);
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(createQuery);
            ps.setString(1, this.bar_code);
            ps.setString(2, registration_no);
            ps.setString(3,this.multiple);
            ps.setString(4,this.serial_number);
            ps.setString(5, description);
            ps.setString(6, this.engine_number);
            ps.setString(7, this.lpo);
            ps.setString(8, make);
            ps.setInt(9, Integer.parseInt(location));
            ps.setString(10, model);
            ps.setInt(11, Integer.parseInt(state));
            ps.setInt(12, Integer.parseInt(driver));
            ps.setInt(13, Integer.parseInt(maintained_by));
            ps.setString(14, spare_1);
            ps.setString(15, spare_2);
            ps.setDate(16, dbConnection.dateConvert(warrantyStartDate));
            ps.setInt(17, Integer.parseInt(noOfMonths));
            ps.setDate(18, dbConnection.dateConvert(expiryDate));
            ps.setString(19, wh_tax_cb);
            ps.setDouble(20, Double.parseDouble(wh_tax_amount));
            ps.setString(21, partPAY);
            ps.setString(22, fullyPAID);
            ps.setString(23, deferPay);
            ps.setDate(24, dbConnection.dateConvert(date_of_purchase));
            ps.setInt(25, Integer.parseInt(supplied_by));
            ps.setString(26, who_to_remind);
            ps.setString(27, email_1);
            ps.setString(28, vendor_account);
            ps.setString(29, who_to_remind_2);
            ps.setString(30, email2);
            ps.setDate(31, dbConnection.dateConvert(depreciation_start_date));
            ps.setDate(32, dbConnection.dateConvert(depreciation_end_date));
            ps.setString(33, authorized_by);
            ps.setString(34, reason);
            ps.setString(35, user);
            ps.setString(36, require_depreciation);
            ps.setString(37, require_redistribution);
            ps.setString(38, province);
            ps.setString(39, chk_Asset_Status);
            ps.setString(40, user_id);
            ps.setString(41,code.getSectionCode(section_id));
            ps.setInt(42, Integer.parseInt(section_id));
            ps.setString(43,sbu_code);
            ps.setInt(44, Integer.parseInt(department_id));
            ps.setString(45,code.getDeptCode(department_id));
            ps.setString(45, spare_3);
            ps.setString(46, spare_4);
            ps.setString(47, spare_5);
            ps.setString(48, spare_6);
            ps.setInt(49, Integer.parseInt(subcategory_id));
            ps.setString(50,code.getSubCategoryCode(subcategory_id));

            ps.setString(51,assetID);
            
            boolean result = ps.execute();
           //System.out.println("Result Of Update>>>>>> " + result);
            setAssetId(asset_id);
                
      	  	
      	  	boolean status = repostCreatedAsset2(asset_id,group_id); 
      	  	if ((!approval_level_val)&&(status))
	      	  	{
      	  			/*
      	  			 * ASSETS THAT DON'T HAVE APPROVAL CAN ONLY BE REJECTED AT POSTING LEVEL*/
      	  			//System.out.println("ASSETS THAT DON'T HAVE APPROVAL CAN ONLY BE REJECTED AT POSTING LEVEL");
      	  			changeGroupAssetStatus(group_id,"APPROVED");
      	  			String [] approvalResult=ad.setApprovalDataGroup(Long.parseLong(group_id));
      	  			approvalResult[10]="A";
      	  			updateGroupPendingTrans(approvalResult,"3",group_id);
      	  			
	      	  	}
      	  	
	      	if((status)&&(approval_level_val))
		      	{
		      	 System.out.println("====== UPDATING  am_asset_Approval For Approval ======");
		      	 //changeGroupAssetMainStatus(group_id,"PENDING");
		      	 updateGroupPendingTrans(ad.setApprovalDataGroup(Long.parseLong(group_id)),"3",group_id);
		      	 //write a method to change status to pending in am_approval
		      	 //the amount in approval is the old amount so the best thing is to read again from the table
		      	 //and update the new values
		      	 //UPDATE THE SUPERVISOR VALUE !!!!!!!!!!!!!!!!
		      	}	
        } 
        catch (Exception ex)
        {
            done = false;
            System.out.println("WARN:GroupInventoryToStockBean - Error Updating st_stock IN updateAsset2->" + ex);
        }
        finally 
        {
            dbConnection.closeConnection(con, ps);
        }

        return done;
    }
    
    public boolean repostCreatedAsset2(String asset_id, String group_id2)
    {
		// TODO Auto-generated method stub
    	Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean status = false;
        int chk_Process_flag=0;
        String post_flag ="R";
        
        String update_created_stock_qry ="Update am_group_stock " +
        " set BAR_CODE=?,REGISTRATION_NO=?,Asset_Serial_No=?, DESCRIPTION=?,Asset_Engine_No=? ," +
        " LPO=?,ASSET_MAKE=?,LOCATION=?,ASSET_MODEL=?, STATE=?,DRIVER=?," +
        " ASSET_MAINTENANCE=?,SPARE_1=?, SPARE_2=?, WAR_START_DATE=?,WAR_MONTH=?,WAR_EXPIRY_DATE=?,"+
        " WH_TAX=?,WH_TAX_AMOUNT=?,PART_PAY=?,FULLY_PAID=?,DEFER_PAY=?,  "+
        " DATE_PURCHASED=?,SUPPLIER_NAME=?,WHO_TO_REM=?, EMAIL1=?, VENDOR_AC=?," +
        " WHO_TO_REM_2=?, EMAIL2=?,EFFECTIVE_DATE=?,DEP_END_DATE=?,AUTHORIZED_BY=?,"+
        " PURCHASE_REASON=?,ASSET_USER=?,REQ_DEPRECIATION=?, REQ_REDISTRIBUTION=?,PROVINCE=?, " +
        " ASSET_STATUS=?,[USER_ID]=?,SECTION_CODE=?,SECTION_ID=?,SBU_CODE=?, DEPT_ID=?,DEPT_CODE=?, "+
        " [post_flag]=?,[reject_reason]=?,SPARE_3=?, SPARE_4=?, SPARE_5=?, SPARE_6=?,SUB_CATEGORY_ID = ?,SUB_CATEGORY_CODE=?  "+
        " WHERE ASSET_ID= ?" ;
        
        System.out.println("update_created_stock_qry >>>>>> " + update_created_stock_qry);
        
        try
	    {
        	con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(update_created_stock_qry);
            ps.setString(1, this.bar_code);
            ps.setString(2, registration_no);
            ps.setString(3,this.serial_number);
            ps.setString(4, description);
            ps.setString(5, this.engine_number);
            ps.setString(6, this.lpo);
            ps.setString(7, make);
            ps.setInt(8, Integer.parseInt(location));
            ps.setString(9, model);
            ps.setInt(10, Integer.parseInt(state));
            ps.setInt(11, Integer.parseInt(driver));
            ps.setInt(12, Integer.parseInt(maintained_by));
            ps.setString(13, spare_1);
            ps.setString(14, spare_2);
            ps.setDate(15, dbConnection.dateConvert(warrantyStartDate));
            ps.setInt(16, Integer.parseInt(noOfMonths));
            ps.setDate(17, dbConnection.dateConvert(expiryDate));
            ps.setString(18, wh_tax_cb);
            ps.setDouble(19, Double.parseDouble(wh_tax_amount));
            ps.setString(20, partPAY);
            ps.setString(21, fullyPAID);
            ps.setString(22, deferPay);
            ps.setDate(23, dbConnection.dateConvert(date_of_purchase));
            ps.setInt(24, Integer.parseInt(supplied_by));
            ps.setString(25, who_to_remind);
            ps.setString(26, email_1);
            ps.setString(27, vendor_account);
            ps.setString(28, who_to_remind_2);
            ps.setString(29, email2);
            ps.setDate(30, dbConnection.dateConvert(depreciation_start_date));
            ps.setDate(31, dbConnection.dateConvert(depreciation_end_date));
            ps.setString(32, authorized_by);
            ps.setString(33, reason);
            ps.setString(34, user);
            ps.setString(35, require_depreciation);
            ps.setString(36, require_redistribution);
            ps.setString(37, province);
            ps.setString(38, "PENDING");
            ps.setString(39, user_id);
            ps.setString(40,code.getSectionCode(section_id));
            ps.setInt(41, Integer.parseInt(section_id));
            ps.setString(42,sbu_code);
            ps.setInt(43, Integer.parseInt(department_id));
            ps.setString(44,code.getDeptCode(department_id));
            ps.setString(45,"P");
            ps.setString(46,"");
            ps.setString(47, spare_3);
            ps.setString(48, spare_4);
            ps.setString(49, spare_5);
            ps.setString(50, spare_6);
            ps.setInt(51, Integer.parseInt(subcategory_id));
            ps.setString(52,code.getSubCategoryCode(subcategory_id));

            ps.setString(53, asset_id);
            int result = ps.executeUpdate();
            System.out.println("RESULT AFTER UPDATING :::::::::::::::: " + result);
            /*
             * chk if all the entries in am_group_asset have been updated
             * update am_group_asset_main process_flag to Y
             * chk if the approval level setup isn't zero
             * call ad.setPendingtrans
             */
            String chk_post_flag ="select count(*) from am_group_stock WHERE post_flag='" 
				+ post_flag + "'" +"  and Group_id ='"+group_id2+"'";
            
            System.out.println("chk_post_flag qry :::::: " + chk_post_flag);
            
            String update_created_stock_main_qry = "update am_group_stock_main set Asset_Status =? "+
				" where group_id = ?";
            
            System.out.println("update_created_stock_main_qry  :::::: " + update_created_stock_main_qry);
            
            String update_pend_GrpAssets="update am_group_stock_main set pend_GrpAssets= ? " +
                " where group_id = ?"	;
            
            System.out.println("update_pend_GrpAssets qry :::::: " + update_pend_GrpAssets);
            
            ps = con.prepareStatement(chk_post_flag);
            rs = ps.executeQuery();
            if (rs.next()) 
            {
            	chk_Process_flag = rs.getInt(1);
            	
            	System.out.println("chk_Process_flag  :::::: " + chk_Process_flag);
            	
	            if(chk_Process_flag == 0)
	            {
	            	System.out.println("Nothing to update in am_group_stock!!!!!!");
	            	ps = con.prepareStatement(update_created_stock_main_qry);
	            	ps.setString(1, "PENDING");
	            	ps.setString(2, group_id2);
	            	ps.executeUpdate();
	            	
	            	ps = con.prepareStatement(update_pend_GrpAssets);
	            	ps.setInt(1, chk_Process_flag);
	            	ps.setString(2, group_id2);
	            	ps.executeUpdate();
	            	
	            	status = true;
	            }
	            else
	            { 
	            	ps = con.prepareStatement(update_pend_GrpAssets);
	            	ps.setInt(1, chk_Process_flag);
	            	ps.setString(2, group_id2);
	            	ps.executeUpdate();
	            	status = false;
	            }
            }
	    }
        catch(Exception ex) 
        {
            System.out.println("WARN: Error repostCreatedAsset2 ->" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }
        return status;
	}
       public String getUncapitalized(String category_id) throws Exception {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String getUncapitalized = "0.0";
        String query = "SELECT CATEGORY_TYPE FROM AM_AD_CATEGORY " +
                       "WHERE CATEGORY_ID = " + category_id;
        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
            	getUncapitalized = rs.getString(1);
            }

        } catch (Exception ex) {
            System.out.println("WARN: Error fetching DepreciationRate ->" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }

        return getUncapitalized;
    }


        public void fetchBranch(String s) throws Exception {

        String fetchQuery = "SELECT ASSET_ID,REGISTRATION_NO," +
                            "BRANCH_ID, DEPT_ID, CATEGORY_ID," +
                            "SECTION_ID, DESCRIPTION, VENDOR_AC, DATE_PURCHASED," +
                            "DEP_RATE, ASSET_MAKE, ASSET_MODEL, ASSET_SERIAL_NO," +
                            "ASSET_ENGINE_NO, SUPPLIER_NAME, ASSET_USER, " +
                            "ASSET_MAINTENANCE, COST_PRICE, DEP_END_DATE," +
                            "RESIDUAL_VALUE, AUTHORIZED_BY," +
                            "WH_TAX, WH_TAX_AMOUNT, POSTING_DATE, EFFECTIVE_DATE," +
                            "PURCHASE_REASON, LOCATION, VATABLE_COST, VAT," +
                            "REQ_DEPRECIATION, SUBJECT_TO_VAT, WHO_TO_REM," +
                            "EMAIL1, WHO_TO_REM_2, EMAIL2, STATE," +
                            "DRIVER, SPARE_1, SPARE_2, [USER_ID],PROVINCE, WAR_START_DATE, " +
                            "WAR_MONTH, WAR_EXPIRY_DATE,LPO,BAR_CODE,req_redistribution,Raise_entry, " +
                            "PART_PAY,FULLY_PAID,defer_pay,AMOUNT_PTD,Asset_Status,SBU_CODE,Invoice_No  "+
                            " FROM AM_GROUP_ASSET_UNCAPITALIZED,SPARE_3, SPARE_4, SPARE_5, SPARE_6,SUB_CATEGORY_ID,SUB_CATEGORY_CODE " +
                            "WHERE ASSET_ID = '" + s + "'";
//Asset Status,deferpay,amountPTD
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        //System.out.println("Fetch Query : " + fetchQuery);
        try {

            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(fetchQuery);
            rs = ps.executeQuery();

            if (rs.next()) {

                group_id = s;
                setAsset_id(rs.getString("Asset_id"));
                setBranch_id(rs.getString(3));
                setDepartment_id(rs.getString(4));
                setSection_id(rs.getString(6));
                setCategory_id(rs.getString(5));
                setMake(rs.getString(11));
                setLocation(rs.getString(27));
                setMaintained_by(rs.getString(17));
                setDriver(rs.getString(37));
                setState(rs.getString(36));
                setSupplied_by(rs.getString(15));
                setRegistration_no(rs.getString(2));
                date_of_purchase = dbConnection.formatDate(rs.getDate(9));
                depreciation_start_date = dbConnection.formatDate(rs.getDate(25));
                depreciation_end_date = dbConnection.formatDate(rs.getDate(19));
                posting_date = dbConnection.formatDate(rs.getDate(24));
                setAuthorized_by(rs.getString(21));
                setReason(rs.getString(26));
                setDescription(rs.getString(7));
                setCost_price(rs.getString(18));
                setVatable_cost(rs.getString(28));
                setSubject_to_vat(rs.getString(31));
                setVat_amount(rs.getString(29));
                setWh_tax_cb(rs.getString(22));
                setWh_tax_amount(rs.getString(23));
                setSerial_number(rs.getString(13));
                setEngine_number(rs.getString(14));
                setModel(rs.getString(12));
                setUser(rs.getString(16));
                setDepreciation_rate(rs.getString(10));
                setResidual_value(rs.getString(20));
                setRequire_depreciation(rs.getString(30));
                setVendor_account(rs.getString(8));
                setWho_to_remind(rs.getString(32));
                setSpare_1(rs.getString(38));
                setSpare_2(rs.getString(39));
                setEmail_1(rs.getString(33));
                setWho_to_remind_2(rs.getString(34));
                setEmail2(rs.getString(35));
                setProvince(rs.getString(41));
                this.setWarrantyStartDate(dbConnection.formatDate(rs.getDate("WAR_START_DATE")));
                this.setExpiryDate(dbConnection.formatDate(rs.getDate("WAR_EXPIRY_DATE")));
                this.setNoOfMonths(rs.getString("WAR_MONTH"));
                this.setSubcategory_id(rs.getString("SUB_CATEGORY_ID"));
                this.setSubCategory_code(rs.getString("SUB_CATEGORY_ID"));
                this.setSpare_3(rs.getString("SPARE_3"));
                this.setSpare_4(rs.getString("SPARE_4"));
                this.setSpare_5(rs.getString("SPARE_5"));
                this.setSpare_6(rs.getString("SPARE_6"));
                this.setLpo(rs.getString("LPO"));
                this.setBar_code(rs.getString("BAR_CODE"));
                this.setRequire_redistribution(rs.getString("REQ_REDISTRIBUTION"));
                this.setRaise_entry(rs.getString("RAISE_ENTRY"));
                this.setPartPAY(rs.getString("PART_PAY"));
                this.setFullyPAID(rs.getString("FULLY_PAID"));
                this.setDeferPay(rs.getString("defer_pay"));
                this.setAmountPTD(rs.getString("AMOUNT_PTD"));
                this.setAsset_Status(rs.getString("Asset_Status"));
                this.setSbu_code(rs.getString("SBU_CODE"));
                this.setInvoiceNum(rs.getString("Invoice_No"));
                this.setSbu_code(rs.getString("SBU_CODE"));
            }

        } catch (Exception e) {
            System.out.println("INFO:Error Fetching GroupAsset Records for fetchBranch ->" +
                               e.getMessage());
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }
    }

       public boolean saveBranch() throws Exception, Throwable
    {
    	/*System.out.println("branch_id ::::::::: " + branch_id);
        System.out.println("department_id ::::::::: " + department_id);
        System.out.println("section_id ::::::::: " + section_id);
        System.out.println("category_id ::::::::: " + category_id);
        System.out.println("Branch Code : " +code.getBranchCode(branch_id));
        System.out.println("Category Code " + code.getCategoryCode(category_id));*/
        asset_id_new = new legend.AutoIDSetup().getIdentity(branch_id,
                department_id, section_id, category_id);
  assetCode = Integer.parseInt(new ApplicationHelper().getGeneratedId("ST_STOCK"));
        //System.out.println("New Asset_ID ::::::::: " + asset_id_new);
        Connection con = null;
        PreparedStatement ps = null;
        boolean done = true;

        if (make == null || make.equals("")) {
            make = "0";
        }
        if (maintained_by == null || maintained_by.equals("")) {
            maintained_by = "0";
        }
        if (supplied_by == null || supplied_by.equals("")) {
            supplied_by = "0";
        }
        if (user == null || user.equals("")) {
            user = "";
        }
        if (location == null || location.equals("")) {
            location = "0";
        }
        if (driver == null || driver.equals("")) {
            driver = "0";
        }
        if (state == null || state.equals("")) {
            state = "0";
        }
        if (department_id == null || department_id.equals("")) {
            department_id = "0";
        }
        if (vat_amount == null || vat_amount.equals("")) {
            vat_amount = "0.0";
        }
        if (vatable_cost == null || vatable_cost.equals("")) {
            vatable_cost = "0.0";
        }
        if (wh_tax_amount == null || wh_tax_amount.equals("")) {
            wh_tax_amount = "0";
        }
        if (branch_id == null || branch_id.equals("")) {
            branch_id = "0";
        }
        if (province == null || province.equals("")) {
            province = "0";
        }
        if (category_id == null || category_id.equals("")) {
            category_id = "0";
        }

        if (residual_value == null || residual_value.equals("")) {
            residual_value = "0";
        }
        if (noOfMonths == null || noOfMonths.equals("")) {
            noOfMonths = "0";
        }
        if (warrantyStartDate == null || warrantyStartDate.equals("")) {
            warrantyStartDate = null;
        }
        if (expiryDate == null || expiryDate.equals("")) {
            expiryDate = null;
        }
        vat_amount = vat_amount.replaceAll(",", "");
        vatable_cost = vatable_cost.replaceAll(",", "");
        wh_tax_amount = wh_tax_amount.replaceAll(",", "");
        residual_value = residual_value.replaceAll(",", "");
        String    createQuery = "INSERT INTO AM_ASSET_UNCAPITALIZED         " +
           "(" +
           "ASSET_ID, REGISTRATION_NO, BRANCH_ID, DEPT_ID," +
           "SECTION_ID, CATEGORY_ID, [DESCRIPTION], VENDOR_AC," +
           "DATE_PURCHASED, DEP_RATE, ASSET_MAKE, ASSET_MODEL," +
           "ASSET_SERIAL_NO, ASSET_ENGINE_NO, SUPPLIER_NAME," +

           "ASSET_USER, ASSET_MAINTENANCE, ACCUM_DEP, MONTHLY_DEP," +
           "COST_PRICE, NBV, DEP_END_DATE, RESIDUAL_VALUE," +
           "AUTHORIZED_BY, POSTING_DATE, EFFECTIVE_DATE, PURCHASE_REASON," +
           "USEFUL_LIFE, TOTAL_LIFE, LOCATION, REMAINING_LIFE," +

           "VATABLE_COST,VAT, WH_TAX, WH_TAX_AMOUNT, REQ_DEPRECIATION," +
           "REQ_REDISTRIBUTION, SUBJECT_TO_VAT, WHO_TO_REM, EMAIL1," +
           "WHO_TO_REM_2, EMAIL2, RAISE_ENTRY, DEP_YTD, [SECTION]," +
           "STATE, DRIVER, SPARE_1, SPARE_2, ASSET_STATUS, [USER_ID]," +
           "MULTIPLE,PROVINCE, WAR_START_DATE, WAR_MONTH, " +
           "WAR_EXPIRY_DATE,LPO,BAR_CODE ,BRANCH_CODE,CATEGORY_CODE ," +
           "GROUP_ID,PART_PAY,FULLY_PAID,DEFER_PAY,SBU_CODE,SECTION_CODE,DEPT_CODE,system_ip," +
           "SUB_CATEGORY_ID,SUB_CATEGORY_CODE, SPARE_3, SPARE_4, SPARE_5, SPARE_6, asset_Code ) " +

           "VALUES" +
           "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
           "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        String create_Archive_Query = "INSERT INTO AM_ASSET_ARCHIVE         " +
        "(" +
        "ASSET_ID, REGISTRATION_NO, BRANCH_ID, DEPT_ID," +
        "SECTION_ID, CATEGORY_ID, [DESCRIPTION], VENDOR_AC," +
        "DATE_PURCHASED, DEP_RATE, ASSET_MAKE, ASSET_MODEL," +
        "ASSET_SERIAL_NO, ASSET_ENGINE_NO, SUPPLIER_NAME," +

        "ASSET_USER, ASSET_MAINTENANCE, ACCUM_DEP, MONTHLY_DEP," +
        "COST_PRICE, NBV, DEP_END_DATE, RESIDUAL_VALUE," +
        "AUTHORIZED_BY, POSTING_DATE, EFFECTIVE_DATE, PURCHASE_REASON," +
        "USEFUL_LIFE, TOTAL_LIFE, LOCATION, REMAINING_LIFE," +

        "VATABLE_COST,VAT, WH_TAX, WH_TAX_AMOUNT, REQ_DEPRECIATION," +
        "REQ_REDISTRIBUTION, SUBJECT_TO_VAT, WHO_TO_REM, EMAIL1," +
        "WHO_TO_REM_2, EMAIL2, RAISE_ENTRY, DEP_YTD, [SECTION]," +
        "STATE, DRIVER, SPARE_1, SPARE_2, ASSET_STATUS, [USER_ID]," +
        "MULTIPLE,PROVINCE, WAR_START_DATE, WAR_MONTH, " +
        "WAR_EXPIRY_DATE,LPO,BAR_CODE ,BRANCH_CODE,CATEGORY_CODE ," +
        "GROUP_ID,PART_PAY,FULLY_PAID,DEFER_PAY,SBU_CODE,SECTION_CODE,DEPT_CODE,system_ip," +
        "SUB_CATEGORY_ID,SUB_CATEGORY_CODE, SPARE_3, SPARE_4, SPARE_5, SPARE_6, asset_Code,asset_Code ) " +

        "VALUES" +
        "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        /*
         *First Create Asset Records
         * and then determine if it
         * should be made available for fleet.
         */
           try
           {
            asset_costPrice = Double.parseDouble(vat_amount) + Double.parseDouble(vatable_cost);
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(createQuery);
            ps.setString(1, asset_id_new);
            ps.setString(2, registration_no);
            ps.setInt(3, Integer.parseInt(branch_id));
            ps.setInt(4, Integer.parseInt(department_id));
            ps.setInt(5, Integer.parseInt(section_id));
            ps.setInt(6, Integer.parseInt(category_id));
            ps.setString(7, description);
            ps.setString(8, vendor_account);
            ps.setDate(9, dbConnection.dateConvert(date_of_purchase));
            ps.setString(10, getDepreciationRate(category_id));
            ps.setString(11, make);
            ps.setString(12, model);
            ps.setString(13, serial_number);
            ps.setString(14, engine_number);
            ps.setInt(15, Integer.parseInt(supplied_by));
            ps.setString(16, user);
            ps.setInt(17, Integer.parseInt(maintained_by));
            ps.setInt(18, 0);
            ps.setInt(19, 0);
            ps.setDouble(20, asset_costPrice);
            ps.setDouble(21, asset_costPrice);
            ps.setDate(22, dbConnection.dateConvert(depreciation_end_date));
            ps.setDouble(23, Double.parseDouble(residual_value));
            ps.setString(24, authorized_by);
            ps.setDate(25, dbConnection.dateConvert(new java.util.Date()));
            ps.setDate(26, dbConnection.dateConvert(depreciation_start_date));
            ps.setString(27, reason);
            ps.setString(28, "0");
            ps.setString(29, computeTotalLife(getDepreciationRate(category_id)));
            ps.setInt(30, Integer.parseInt(location));
            ps.setString(31, computeTotalLife(getDepreciationRate(category_id)));
            ps.setDouble(32, Double.parseDouble(vatable_cost));
            ps.setDouble(33, Double.parseDouble(vat_amount));
            ps.setString(34, wh_tax_cb);
            ps.setDouble(35, Double.parseDouble(wh_tax_amount));
            ps.setString(36, require_depreciation);
            ps.setString(37, require_redistribution);
            ps.setString(38, subject_to_vat);
            ps.setString(39, who_to_remind);
            ps.setString(40, email_1);
            ps.setString(41, who_to_remind_2);
            ps.setString(42, email2);
            ps.setString(43, "N");
            ps.setString(44, "0");
            ps.setString(45, section);
            ps.setInt(46, Integer.parseInt(state));
            ps.setInt(47, Integer.parseInt(driver));
            ps.setString(48, spare_1);
            ps.setString(49, spare_2);
            ps.setString(50, asset_Status);
            ps.setString(51, user_id);
            ps.setString(52, multiple);
            ps.setString(53, province);
            ps.setDate(54, dbConnection.dateConvert(warrantyStartDate));
            ps.setInt(55, Integer.parseInt(noOfMonths));
            ps.setDate(56, dbConnection.dateConvert(expiryDate));
            ps.setString(57,lpo);
            ps.setString(58,bar_code);
            ps.setString(59,code.getBranchCode(branch_id));
            ps.setString(60,code.getCategoryCode(category_id));
            ps.setString(61,group_id);
            /*ps.setString(62, partPAY);
            ps.setString(63, fullyPAID);
            ps.setString(64, deferPay);*/
            ps.setString(62, "N");
            ps.setString(63, "Y");
            ps.setString(64, "N");
            ps.setString(65,sbu_code);
            ps.setString(66,code.getSectionCode(section_id));
            ps.setString(67,code.getDeptCode(department_id));
            ps.setString(68,workstationIp);
            ps.setInt(69, Integer.parseInt(subcategory_id));
            ps.setString(70,code.getSubCategoryCode(subcategory_id));
            ps.setString(71, spare_3);
            ps.setString(72, spare_4);
            ps.setString(73, spare_5);
            ps.setString(74, spare_6);
               ps.setInt(65,assetCode);

            //not right

            boolean result = ps.execute();

            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(create_Archive_Query);
            ps.setString(1, asset_id_new);
            ps.setString(2, registration_no);
            ps.setInt(3, Integer.parseInt(branch_id));
            ps.setInt(4, Integer.parseInt(department_id));
            ps.setInt(5, Integer.parseInt(section_id));
            ps.setInt(6, Integer.parseInt(category_id));
            ps.setString(7, description);
            ps.setString(8, vendor_account);
            ps.setDate(9, dbConnection.dateConvert(date_of_purchase));
            ps.setString(10, getDepreciationRate(category_id));
            ps.setString(11, make);
            ps.setString(12, model);
            ps.setString(13, serial_number);
            ps.setString(14, engine_number);
            ps.setInt(15, Integer.parseInt(supplied_by));
            ps.setString(16, user);
            ps.setInt(17, Integer.parseInt(maintained_by));
            ps.setInt(18, 0);
            ps.setInt(19, 0);
            ps.setDouble(20, asset_costPrice);
            ps.setDouble(21, asset_costPrice);
            ps.setDate(22, dbConnection.dateConvert(depreciation_end_date));
            ps.setDouble(23, Double.parseDouble(residual_value));
            ps.setString(24, authorized_by);
            ps.setDate(25, dbConnection.dateConvert(new java.util.Date()));
            ps.setDate(26, dbConnection.dateConvert(depreciation_start_date));
            ps.setString(27, reason);
            ps.setString(28, "0");
            ps.setString(29, computeTotalLife(getDepreciationRate(category_id)));
            ps.setInt(30, Integer.parseInt(location));
            ps.setString(31, computeTotalLife(getDepreciationRate(category_id)));
            ps.setDouble(32, Double.parseDouble(vatable_cost));
            ps.setDouble(33, Double.parseDouble(vat_amount));
            ps.setString(34, wh_tax_cb);
            ps.setDouble(35, Double.parseDouble(wh_tax_amount));
            ps.setString(36, require_depreciation);
            ps.setString(37, require_redistribution);
            ps.setString(38, subject_to_vat);
            ps.setString(39, who_to_remind);
            ps.setString(40, email_1);
            ps.setString(41, who_to_remind_2);
            ps.setString(42, email2);
            ps.setString(43, "N");
            ps.setString(44, "0");
            ps.setString(45, section);
            ps.setInt(46, Integer.parseInt(state));
            ps.setInt(47, Integer.parseInt(driver));
            ps.setString(48, spare_1);
            ps.setString(49, spare_2);
            ps.setString(50, asset_Status);
            ps.setString(51, user_id);
            ps.setString(52, multiple);
            ps.setString(53, province);
            ps.setDate(54, dbConnection.dateConvert(warrantyStartDate));
            ps.setInt(55, Integer.parseInt(noOfMonths));
            ps.setDate(56, dbConnection.dateConvert(expiryDate));
            ps.setString(57,lpo);
            ps.setString(58,bar_code);
            ps.setString(59,code.getBranchCode(branch_id));
            ps.setString(60,code.getCategoryCode(category_id));
            ps.setString(61,group_id);
            /*ps.setString(62, partPAY);
            ps.setString(63, fullyPAID);
            ps.setString(64, deferPay);*/
            ps.setString(62, "N");
            ps.setString(63, "Y");
            ps.setString(64, "N");
            ps.setString(65,sbu_code);
            ps.setString(66,code.getSectionCode(section_id));
            ps.setString(67,code.getDeptCode(department_id));
            ps.setString(68,workstationIp);
            ps.setInt(69, Integer.parseInt(subcategory_id));
            ps.setString(70,code.getSubCategoryCode(subcategory_id));
            ps.setString(71, spare_3);
            ps.setString(72, spare_4);
            ps.setString(73, spare_5);
            ps.setString(74, spare_6);
               ps.setInt(75,assetCode);
           // System.out.println("=====================================================");
           // System.out.println("Result Of Insertion into Asset Table From group Asset : " + result);
           // System.out.println("=====================================================");
             result = ps.execute();


           htmlUtil.insGrpToAm_Invoice_No(asset_id_new,lpo,invoiceNum,"Asset Creation",group_id);

            FleetHistoryManager fm = new FleetHistoryManager();
            if (fm.isRequiredForFleet(asset_id))
            {
            	System.out.println(">>>>>>>>>>>>>>>>>> Asset is Required For Fleet <<<<<<<<<<<<<<<<<<<<<<<<<<<");
               fm.copyAssetDataToFleet(asset_id);
            }
            setAssetId(asset_id);

            String page1 = "ASSET GROUP CREATION RAISE ENTRY";
            String flag= "";
      	  	String partPay="";
      	  	String qryBranch =" SELECT branch_name from am_ad_branch where branch_id='"+branch_id+"'";
      	   	String Branch = approvalRec.getCodeName(qryBranch);
      	   	String subjectT= adGroup.subjectToVat(group_id);
      	   	String whT= adGroup.whTax(group_id);
      	   	String Name =approvalRec.getCodeName(" SELECT full_name from am_gb_user where user_id='"+user_id+"'");
      	   	String url = "DocumentHelp.jsp?np=groupAssetPosting&amp;id=" + group_id + "&pageDirect=Y";
      	   	boolean approval_level_val =checkApprovalStatus("19");
      	  	boolean status = updateCreatedAssetStatusBranch(asset_id,group_id,asset_id_new,assetCode);

      	  	if ((!approval_level_val)&&(status))
	      	  	{
      	  			//System.out.println(">>>>>>>>>>>> Inserting Into RaiseEntry <<<<<<<<<<");
      	  			String [] approvalResult=ad.setApprovalDataGroup(Long.parseLong(group_id));
      	  			approvalResult[10]="A";
      	  			String trans_id = adGroup.setGroupPendingTrans(approvalResult,"19",assetCode);

                                ad.setPendingTransArchive(approvalResult,"19",Integer.parseInt(trans_id),assetCode);

      	  			approvalRec.insertApprovalx2
	      	  		(group_id, approvalResult[5], page1, flag, partPay,Name, Branch, subjectT,
                                        whT, url,Integer.parseInt(trans_id),assetCode);
      	  			String qry = upd_am_asset_branch_status_RaiseEntry + group_id;
      	  			String qry2 = upd_am_grp_asset_branch_RaiseEntry + group_id;
      	  			String qry3 = upd_am_grp_asset_main_RaiseEntry +  group_id;
                                String qry4 = upd_am_asset_status_RaiseEntry_Archive + group_id;
      	  			String qry5 = upd_am_grp_asset_RaiseEntry_Archive + group_id;
      	  			String qry6 = upd_am_grp_asset_main_RaiseEntry_Archive +  group_id;
	      	  		/*System.out.println("qry : " + qry);
	  	  			System.out.println("qry2 : " + qry2 );
	  	  			System.out.println("qry3 : " + qry3);*/
	      	  		updateStatusUtil(qry);
	      	  		updateStatusUtil(qry2);
	      	  		updateStatusUtil(qry3);
                                updateStatusUtil(qry4);
	      	  		updateStatusUtil(qry5);
	      	  		updateStatusUtil(qry6);
	      	  	}

	      	if((status)&&(approval_level_val))
		      	{
		      	 System.out.println("====== Inserting into Approval ======");
		      	changeGroupAssetStatusBranch(group_id,"PENDING");
		      	 String trans_id = adGroup.setGroupPendingTrans(ad.setApprovalDataGroup(Long.parseLong(group_id)),"19",assetCode);
                         ad.setPendingTransArchive(ad.setApprovalDataGroup(Long.parseLong(group_id)),"19",Integer.parseInt(trans_id),assetCode);
		      	 //write a method to change status to pending
		      	}
        }
        catch (Exception ex)
        {
            done = false;
            System.out.println("WARN:Error creating asset->" + ex);
        }
        finally
        {
            dbConnection.closeConnection(con, ps);
        }

        return done;

    }

	public boolean updateCreatedAssetStatusBranch(String old_asset_id, String group_id2, String new_asset_id)
    {
		// TODO Auto-generated method stub
    	Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean status = false;
        int chk_Process_flag=0;
        String process_flag ="N";

        String update_created_asset_qry ="update am_group_asset_uncapitalized set process_flag= ?,asset_id=?,REGISTRATION_NO=?, " +
        "BRANCH_ID=?, DEPT_ID=?,SECTION_ID=?, CATEGORY_ID=?,DESCRIPTION=?, VENDOR_AC=?," +
        "DATE_PURCHASED=?, DEP_RATE=?, ASSET_MAKE=?, ASSET_MODEL=?,"+
        "ASSET_SERIAL_NO=?, ASSET_ENGINE_NO=?, SUPPLIER_NAME=?," +
        "ASSET_USER=?, ASSET_MAINTENANCE=?, ACCUM_DEP=?," +
        "COST_PRICE=?, DEP_END_DATE=?, RESIDUAL_VALUE=?," +
        "AUTHORIZED_BY=?, POSTING_DATE=?, EFFECTIVE_DATE=?, PURCHASE_REASON=?," +
        " LOCATION=?, " +
        "VATABLE_COST=?,VAT=?, WH_TAX=?, WH_TAX_AMOUNT=?, REQ_DEPRECIATION=?," +
        "REQ_REDISTRIBUTION=?, SUBJECT_TO_VAT=?, WHO_TO_REM=?, EMAIL1=?," +
        "WHO_TO_REM_2=?, EMAIL2=?, RAISE_ENTRY=?, DEP_YTD=?, SECTION=?," +
        "STATE=?, DRIVER=?, SPARE_1=?, SPARE_2=?, [USER_ID]=?," +
        "PROVINCE=?, WAR_START_DATE=?, WAR_MONTH=?, " +
        "WAR_EXPIRY_DATE=?,LPO=?,BAR_CODE=? ,BRANCH_CODE=?,CATEGORY_CODE=? ," +
        "GROUP_ID=?,PART_PAY=?,FULLY_PAID=?,DEFER_PAY=?,SBU_CODE=?,[dept_code] = ?,[section_code] = ?, "+
        "SPARE_3=?, SPARE_4=?, SPARE_5=?, SPARE_6=?,SUB_CATEGORY_ID = ?,SUB_CATEGORY_CODE=?  where asset_id = ? " ;

        String update_created_asset_ARCHIVE_qry ="update am_group_asset_ARCHIVE set process_flag= ?,asset_id=?,REGISTRATION_NO=?, " +
        "BRANCH_ID=?, DEPT_ID=?,SECTION_ID=?, CATEGORY_ID=?,DESCRIPTION=?, VENDOR_AC=?," +
        "DATE_PURCHASED=?, DEP_RATE=?, ASSET_MAKE=?, ASSET_MODEL=?,"+
        "ASSET_SERIAL_NO=?, ASSET_ENGINE_NO=?, SUPPLIER_NAME=?," +
        "ASSET_USER=?, ASSET_MAINTENANCE=?, ACCUM_DEP=?," +
        "COST_PRICE=?, DEP_END_DATE=?, RESIDUAL_VALUE=?," +
        "AUTHORIZED_BY=?, POSTING_DATE=?, EFFECTIVE_DATE=?, PURCHASE_REASON=?," +
        " LOCATION=?, " +
        "VATABLE_COST=?,VAT=?, WH_TAX=?, WH_TAX_AMOUNT=?, REQ_DEPRECIATION=?," +
        "REQ_REDISTRIBUTION=?, SUBJECT_TO_VAT=?, WHO_TO_REM=?, EMAIL1=?," +
        "WHO_TO_REM_2=?, EMAIL2=?, RAISE_ENTRY=?, DEP_YTD=?, SECTION=?," +
        "STATE=?, DRIVER=?, SPARE_1=?, SPARE_2=?, [USER_ID]=?," +
        "PROVINCE=?, WAR_START_DATE=?, WAR_MONTH=?, " +
        "WAR_EXPIRY_DATE=?,LPO=?,BAR_CODE=? ,BRANCH_CODE=?,CATEGORY_CODE=? ," +
        "GROUP_ID=?,PART_PAY=?,FULLY_PAID=?,DEFER_PAY=?,SBU_CODE=?,[dept_code] = ?,[section_code] = ?, "+
        "SPARE_3=?, SPARE_4=?, SPARE_5=?, SPARE_6=?,SUB_CATEGORY_ID = ?,SUB_CATEGORY_CODE=?  where asset_id = ? " ;

        String chk_process_flag ="select count(*) from am_group_asset_uncapitalized WHERE process_flag='"
        						+ process_flag + "'" +"  and Group_id ='"+group_id2+"'";

        String update_created_asset_main_qry = "update am_group_asset_main set process_flag =? "+
       									" where group_id = ?";

        String update_created_asset_main_ARCHIVE_qry = "update am_group_asset_main_archive set process_flag =? "+
       									" where group_id = ?";
        try
	    {
        	con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(update_created_asset_qry);
            ps.setString(1, "Y");
            ps.setString(2, new_asset_id);
            ps.setString(3, registration_no);
            ps.setInt(4, Integer.parseInt(branch_id));
            ps.setInt(5, Integer.parseInt(department_id));
            ps.setInt(6, Integer.parseInt(section_id));
            ps.setInt(7, Integer.parseInt(category_id));
            ps.setString(8, description);
            ps.setString(9, vendor_account);
            ps.setDate(10, dbConnection.dateConvert(date_of_purchase));
            ps.setString(11, getDepreciationRate(category_id));
            ps.setString(12, make);
            ps.setString(13, model);
            ps.setString(14, serial_number);
            ps.setString(15, engine_number);
            ps.setInt(16, Integer.parseInt(supplied_by));
            ps.setString(17, user);
            ps.setInt(18, Integer.parseInt(maintained_by));
            ps.setInt(19, 0);
            ps.setDouble(20, asset_costPrice);
            ps.setDate(21, dbConnection.dateConvert(depreciation_end_date));
            ps.setDouble(22, Double.parseDouble(residual_value));
            ps.setString(23, authorized_by);
            ps.setDate(24, dbConnection.dateConvert(new java.util.Date()));
            ps.setDate(25, dbConnection.dateConvert(depreciation_start_date));
            ps.setString(26, reason);
            ps.setInt(27, Integer.parseInt(location));
            ps.setDouble(28, Double.parseDouble(vatable_cost));
            ps.setDouble(29, Double.parseDouble(vat_amount));
            ps.setString(30, wh_tax_cb);
            ps.setDouble(31, Double.parseDouble(wh_tax_amount));
            ps.setString(32, require_depreciation);
            ps.setString(33, require_redistribution);
            ps.setString(34, subject_to_vat);
            ps.setString(35, who_to_remind);
            ps.setString(36, email_1);
            ps.setString(37, who_to_remind_2);
            ps.setString(38, email2);
            ps.setString(39, "N");
            ps.setString(40, "0");
            ps.setString(41, section);
            ps.setInt(42, Integer.parseInt(state));
            ps.setInt(43, Integer.parseInt(driver));
            ps.setString(44, spare_1);
            ps.setString(45, spare_2);
           // ps.setString(46, "ACTIVE");
            ps.setString(46, user_id);

            ps.setString(47, province);
            ps.setDate(48, dbConnection.dateConvert(warrantyStartDate));
            ps.setInt(49, Integer.parseInt(noOfMonths));
            ps.setDate(50, dbConnection.dateConvert(expiryDate));
            ps.setString(51,lpo);
            ps.setString(52,bar_code);
            ps.setString(53,code.getBranchCode(branch_id));
            ps.setString(54,code.getCategoryCode(category_id));
            ps.setString(55,group_id);
            ps.setString(56, partPAY);
            ps.setString(57, fullyPAID);
            ps.setString(58, deferPay);
            ps.setString(59,sbu_code);
            ps.setString(60,code.getDeptCode(department_id));
            ps.setString(61,code.getSectionCode(section_id));
            ps.setString(62, spare_3);
            ps.setString(63, spare_4);
            ps.setString(64, spare_5);
            ps.setString(65, spare_6);
            ps.setInt(66, Integer.parseInt(subcategory_id));
            ps.setString(67,code.getSubCategoryCode(subcategory_id));
            ps.setString(68, old_asset_id);
            int result = ps.executeUpdate();

            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(update_created_asset_ARCHIVE_qry);
            ps.setString(1, "Y");
            ps.setString(2, new_asset_id);
            ps.setString(3, registration_no);
            ps.setInt(4, Integer.parseInt(branch_id));
            ps.setInt(5, Integer.parseInt(department_id));
            ps.setInt(6, Integer.parseInt(section_id));
            ps.setInt(7, Integer.parseInt(category_id));
            ps.setString(8, description);
            ps.setString(9, vendor_account);
            ps.setDate(10, dbConnection.dateConvert(date_of_purchase));
            ps.setString(11, getDepreciationRate(category_id));
            ps.setString(12, make);
            ps.setString(13, model);
            ps.setString(14, serial_number);
            ps.setString(15, engine_number);
            ps.setInt(16, Integer.parseInt(supplied_by));
            ps.setString(17, user);
            ps.setInt(18, Integer.parseInt(maintained_by));
            ps.setInt(19, 0);
            ps.setDouble(20, asset_costPrice);
            ps.setDate(21, dbConnection.dateConvert(depreciation_end_date));
            ps.setDouble(22, Double.parseDouble(residual_value));
            ps.setString(23, authorized_by);
            ps.setDate(24, dbConnection.dateConvert(new java.util.Date()));
            ps.setDate(25, dbConnection.dateConvert(depreciation_start_date));
            ps.setString(26, reason);
            ps.setInt(27, Integer.parseInt(location));
            ps.setDouble(28, Double.parseDouble(vatable_cost));
            ps.setDouble(29, Double.parseDouble(vat_amount));
            ps.setString(30, wh_tax_cb);
            ps.setDouble(31, Double.parseDouble(wh_tax_amount));
            ps.setString(32, require_depreciation);
            ps.setString(33, require_redistribution);
            ps.setString(34, subject_to_vat);
            ps.setString(35, who_to_remind);
            ps.setString(36, email_1);
            ps.setString(37, who_to_remind_2);
            ps.setString(38, email2);
            ps.setString(39, "N");
            ps.setString(40, "0");
            ps.setString(41, section);
            ps.setInt(42, Integer.parseInt(state));
            ps.setInt(43, Integer.parseInt(driver));
            ps.setString(44, spare_1);
            ps.setString(45, spare_2);
           // ps.setString(46, "ACTIVE");
            ps.setString(46, user_id);

            ps.setString(47, province);
            ps.setDate(48, dbConnection.dateConvert(warrantyStartDate));
            ps.setInt(49, Integer.parseInt(noOfMonths));
            ps.setDate(50, dbConnection.dateConvert(expiryDate));
            ps.setString(51,lpo);
            ps.setString(52,bar_code);
            ps.setString(53,code.getBranchCode(branch_id));
            ps.setString(54,code.getCategoryCode(category_id));
            ps.setString(55,group_id);
            ps.setString(56, partPAY);
            ps.setString(57, fullyPAID);
            ps.setString(58, deferPay);
            ps.setString(59,sbu_code);
            ps.setString(60,code.getDeptCode(department_id));
            ps.setString(61,code.getSectionCode(section_id));
            ps.setString(62, spare_3);
            ps.setString(63, spare_4);
            ps.setString(64, spare_5);
            ps.setString(65, spare_6);
            ps.setInt(66, Integer.parseInt(subcategory_id));
            ps.setString(67,code.getSubCategoryCode(subcategory_id));
            ps.setString(68, old_asset_id);
           // System.out.println("RESULT AFTER UPDATING :::::::::::::::: " + result);
            /*
             * chk if all the entries in am_group_asset have been updated
             * update am_group_asset_main process_flag to Y
             * chk if the approval level setup isn't zero
             * call ad.setPendingtrans
             */
            System.out.println("chk_process_flag qry :::::: " + chk_process_flag);
            ps = con.prepareStatement(chk_process_flag);
            rs = ps.executeQuery();
            if (rs.next())
            {
            	chk_Process_flag = rs.getInt(1);
	            if(chk_Process_flag == 0)
	            {
	            	System.out.println("Nothing to update in am_group_asset!!!!!!");
	            	ps = con.prepareStatement(update_created_asset_main_qry);
	            	ps.setString(1, "Y");
	            	ps.setString(2, group_id2);
	            	ps.executeUpdate();

                        ps = con.prepareStatement(update_created_asset_main_ARCHIVE_qry);
	            	ps.setString(1, "Y");
	            	ps.setString(2, group_id2);
	            	ps.executeUpdate();

	            	status = true;
	            }
            }
	    }
        catch(Exception ex)
        {
            System.out.println("WARN: Error updateCreatedAssetStatus ->" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }
        return status;
	}
	public void changeGroupAssetStatusBranch(String id,String status)
	{
		// TODO Auto-generated method stub
		Connection con = null;
	    PreparedStatement ps = null;
	    String query_r ="update am_group_asset_uncapitalized set asset_status=? " +
		" where Group_id = '"+id+"'";

            String query_archive="update am_group_asset_archive set asset_status=? " +
		" where Group_id = '"+id+"'";
	    try
	    	{
	    	con = dbConnection.getConnection("legendPlus");
	    	ps = con.prepareStatement(query_r);
	    	ps.setString(1,status);
	       	int i =ps.executeUpdate();

                ps = con.prepareStatement(query_archive);
	    	ps.setString(1,status);
	       	i =ps.executeUpdate();

	        changeGroupAssetMainStatus(id,status);
	        }
		catch (Exception ex)
		    {
		        System.out.println("GroupInventoryToStockBean: Error Updating am_group_asset " + ex);
		    }
		finally
			{
	            dbConnection.closeConnection(con, ps);
	        }


	}

        	public boolean updateCreatedAssetStatus(String old_asset_id, String group_id2, String new_asset_id,int assetCode)
    {
		// TODO Auto-generated method stub
    	Connection con = null;
        PreparedStatement ps = null;
    	Connection con1 = null;
        PreparedStatement ps1 = null;        
        ResultSet rs = null;
        boolean status = false;
        int chk_Process_flag=0;
        String process_flag ="N";

        String update_created_asset_qry ="update am_group_stock set process_flag= ?,asset_id=?,REGISTRATION_NO=?, " +
        "BRANCH_ID=?, DEPT_ID=?,SECTION_ID=?, CATEGORY_ID=?,DESCRIPTION=?, VENDOR_AC=?," +
        "DATE_PURCHASED=?, DEP_RATE=?, ASSET_MAKE=?, ASSET_MODEL=?,"+
        "ASSET_SERIAL_NO=?, ASSET_ENGINE_NO=?, SUPPLIER_NAME=?," +
        "ASSET_USER=?, ASSET_MAINTENANCE=?, ACCUM_DEP=?," +
        "COST_PRICE=?, DEP_END_DATE=?, RESIDUAL_VALUE=?," +
        "AUTHORIZED_BY=?, POSTING_DATE=?, EFFECTIVE_DATE=?, PURCHASE_REASON=?," +
        " LOCATION=?, " +
        "VATABLE_COST=?,VAT=?, WH_TAX=?, WH_TAX_AMOUNT=?, REQ_DEPRECIATION=?," +
        "REQ_REDISTRIBUTION=?, SUBJECT_TO_VAT=?, WHO_TO_REM=?, EMAIL1=?," +
        "WHO_TO_REM_2=?, EMAIL2=?, RAISE_ENTRY=?, DEP_YTD=?, SECTION=?," +
        "STATE=?, DRIVER=?, SPARE_1=?, SPARE_2=?, [USER_ID]=?," +
        "PROVINCE=?, WAR_START_DATE=?, WAR_MONTH=?, " +
        "WAR_EXPIRY_DATE=?,LPO=?,BAR_CODE=? ,BRANCH_CODE=?,CATEGORY_CODE=? ," +
        "GROUP_ID=?,PART_PAY=?,FULLY_PAID=?,DEFER_PAY=?,SBU_CODE=?,[dept_code] = ?,[section_code] = ? ,asset_code=?, "+
        "SPARE_3=?, SPARE_4=?, SPARE_5=?, SPARE_6=?,SUB_CATEGORY_ID = ?,SUB_CATEGORY_CODE=?  where asset_id = ? " ;

        String update_created_asset_ARCHIVE_qry ="update am_group_stock_ARCHIVE set process_flag= ?,asset_id=?,REGISTRATION_NO=?, " +
        "BRANCH_ID=?, DEPT_ID=?,SECTION_ID=?, CATEGORY_ID=?,DESCRIPTION=?, VENDOR_AC=?," +
        "DATE_PURCHASED=?, DEP_RATE=?, ASSET_MAKE=?, ASSET_MODEL=?,"+
        "ASSET_SERIAL_NO=?, ASSET_ENGINE_NO=?, SUPPLIER_NAME=?," +
        "ASSET_USER=?, ASSET_MAINTENANCE=?, ACCUM_DEP=?," +
        "COST_PRICE=?, DEP_END_DATE=?, RESIDUAL_VALUE=?," +
        "AUTHORIZED_BY=?, POSTING_DATE=?, EFFECTIVE_DATE=?, PURCHASE_REASON=?," +
        " LOCATION=?, " +
        "VATABLE_COST=?,VAT=?, WH_TAX=?, WH_TAX_AMOUNT=?, REQ_DEPRECIATION=?," +
        "REQ_REDISTRIBUTION=?, SUBJECT_TO_VAT=?, WHO_TO_REM=?, EMAIL1=?," +
        "WHO_TO_REM_2=?, EMAIL2=?, RAISE_ENTRY=?, DEP_YTD=?, SECTION=?," +
        "STATE=?, DRIVER=?, SPARE_1=?, SPARE_2=?, [USER_ID]=?," +
        "PROVINCE=?, WAR_START_DATE=?, WAR_MONTH=?, " +
        "WAR_EXPIRY_DATE=?,LPO=?,BAR_CODE=? ,BRANCH_CODE=?,CATEGORY_CODE=? ," +
        "GROUP_ID=?,PART_PAY=?,FULLY_PAID=?,DEFER_PAY=?,SBU_CODE=?,[dept_code] = ?,[section_code] = ? ,asset_code=?, "+
        "SPARE_3=?, SPARE_4=?, SPARE_5=?, SPARE_6=?,SUB_CATEGORY_ID = ?,SUB_CATEGORY_CODE=?  where asset_id = ? " ;

        String chk_process_flag ="select count(*) from am_group_stock WHERE process_flag='"
        						+ process_flag + "'" +"  and Group_id ='"+group_id2+"'";

        String update_created_asset_main_qry = "update am_group_stock_main set process_flag =? "+
       									" where group_id = ?";

        String update_created_asset_main_ARCHIVE_qry = "update am_group_stock_main_archive set process_flag =? "+
       									" where group_id = ?";
        try
	    {
        	con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(update_created_asset_qry);
            ps.setString(1, "Y");
            ps.setString(2, new_asset_id);
            ps.setString(3, registration_no);
            ps.setInt(4, Integer.parseInt(branch_id));
            ps.setInt(5, Integer.parseInt(department_id));
            ps.setInt(6, Integer.parseInt(section_id));
            ps.setInt(7, Integer.parseInt(category_id));
            ps.setString(8, description);
            ps.setString(9, vendor_account);
            ps.setDate(10, dbConnection.dateConvert(date_of_purchase));
            ps.setString(11, getDepreciationRate(category_id));
            ps.setString(12, make);
            ps.setString(13, model);
            ps.setString(14, serial_number);
            ps.setString(15, engine_number);
            ps.setInt(16, Integer.parseInt(supplied_by));
            ps.setString(17, user);
            ps.setInt(18, Integer.parseInt(maintained_by));
            ps.setInt(19, 0);
            ps.setDouble(20, asset_costPrice);
            ps.setDate(21, dbConnection.dateConvert(depreciation_end_date));
            ps.setDouble(22, Double.parseDouble(residual_value));
            ps.setString(23, authorized_by);
            ps.setDate(24, dbConnection.dateConvert(new java.util.Date()));
            ps.setDate(25, dbConnection.dateConvert(depreciation_start_date));
            ps.setString(26, reason);
            ps.setInt(27, Integer.parseInt(location));
            ps.setDouble(28, Double.parseDouble(vatable_cost));
            ps.setDouble(29, Double.parseDouble(vat_amount));
            ps.setString(30, wh_tax_cb);
            ps.setDouble(31, Double.parseDouble(wh_tax_amount));
            ps.setString(32, require_depreciation);
            ps.setString(33, require_redistribution);
            ps.setString(34, subject_to_vat);
            ps.setString(35, who_to_remind);
            ps.setString(36, email_1);
            ps.setString(37, who_to_remind_2);
            ps.setString(38, email2);
            ps.setString(39, "N");
            ps.setString(40, "0");
            ps.setString(41, section);
            ps.setInt(42, Integer.parseInt(state));
            ps.setInt(43, Integer.parseInt(driver));
            ps.setString(44, spare_1);
            ps.setString(45, spare_2);
           // ps.setString(46, "ACTIVE");
            ps.setString(46, user_id);

            ps.setString(47, province);
            ps.setDate(48, dbConnection.dateConvert(warrantyStartDate));
            ps.setInt(49, Integer.parseInt(noOfMonths));
            ps.setDate(50, dbConnection.dateConvert(expiryDate));
            ps.setString(51,lpo);
            ps.setString(52,bar_code);
            ps.setString(53,code.getBranchCode(branch_id));
            ps.setString(54,code.getCategoryCode(category_id));
            ps.setString(55,group_id);
            ps.setString(56, partPAY);
            ps.setString(57, fullyPAID);
            ps.setString(58, deferPay);
            ps.setString(59,sbu_code);
            ps.setString(60,code.getDeptCode(department_id));
            ps.setString(61,code.getSectionCode(section_id));
             ps.setInt(62, assetCode);
             ps.setString(63, spare_3);
             ps.setString(64, spare_4);
             ps.setString(65, spare_5);
             ps.setString(66, spare_6);
             ps.setInt(67, Integer.parseInt(subcategory_id));
             ps.setString(68,code.getSubCategoryCode(subcategory_id));             
            ps.setString(69, old_asset_id);
            
            int result = ps.executeUpdate();

            con1 = dbConnection.getConnection("legendPlus");
            ps1 = con.prepareStatement(update_created_asset_ARCHIVE_qry);
            ps1.setString(1, "Y");
            ps1.setString(2, new_asset_id);
            ps1.setString(3, registration_no);
            ps1.setInt(4, Integer.parseInt(branch_id));
            ps1.setInt(5, Integer.parseInt(department_id));
            ps1.setInt(6, Integer.parseInt(section_id));
            ps1.setInt(7, Integer.parseInt(category_id));
            ps1.setString(8, description);
            ps1.setString(9, vendor_account);
            ps1.setDate(10, dbConnection.dateConvert(date_of_purchase));
            ps1.setString(11, getDepreciationRate(category_id));
            ps1.setString(12, make);
            ps1.setString(13, model);
            ps1.setString(14, serial_number);
            ps1.setString(15, engine_number);
            ps1.setInt(16, Integer.parseInt(supplied_by));
            ps1.setString(17, user);
            ps1.setInt(18, Integer.parseInt(maintained_by));
            ps1.setInt(19, 0);
            ps1.setDouble(20, asset_costPrice);
            ps1.setDate(21, dbConnection.dateConvert(depreciation_end_date));
            ps1.setDouble(22, Double.parseDouble(residual_value));
            ps1.setString(23, authorized_by);
            ps1.setDate(24, dbConnection.dateConvert(new java.util.Date()));
            ps1.setDate(25, dbConnection.dateConvert(depreciation_start_date));
            ps1.setString(26, reason);
            ps1.setInt(27, Integer.parseInt(location));
            ps1.setDouble(28, Double.parseDouble(vatable_cost));
            ps1.setDouble(29, Double.parseDouble(vat_amount));
            ps1.setString(30, wh_tax_cb);
            ps1.setDouble(31, Double.parseDouble(wh_tax_amount));
            ps1.setString(32, require_depreciation);
            ps1.setString(33, require_redistribution);
            ps1.setString(34, subject_to_vat);
            ps1.setString(35, who_to_remind);
            ps1.setString(36, email_1);
            ps1.setString(37, who_to_remind_2);
            ps1.setString(38, email2);
            ps1.setString(39, "N");
            ps1.setString(40, "0");
            ps1.setString(41, section);
            ps1.setInt(42, Integer.parseInt(state));
            ps1.setInt(43, Integer.parseInt(driver));
            ps1.setString(44, spare_1);
            ps1.setString(45, spare_2);
           // ps.setString(46, "ACTIVE");
            ps1.setString(46, user_id);

            ps1.setString(47, province);
            ps1.setDate(48, dbConnection.dateConvert(warrantyStartDate));
            ps1.setInt(49, Integer.parseInt(noOfMonths));
            ps1.setDate(50, dbConnection.dateConvert(expiryDate));
            ps1.setString(51,lpo);
            ps1.setString(52,bar_code);
            ps1.setString(53,code.getBranchCode(branch_id));
            ps1.setString(54,code.getCategoryCode(category_id));
            ps1.setString(55,group_id);
            ps1.setString(56, partPAY);
            ps1.setString(57, fullyPAID);
            ps1.setString(58, deferPay);
            ps1.setString(59,sbu_code);
            ps1.setString(60,code.getDeptCode(department_id));
            ps1.setString(61,code.getSectionCode(section_id));
              ps1.setInt(62, assetCode);
              ps1.setString(63, spare_3);
              ps1.setString(64, spare_4);
              ps1.setString(65, spare_5);
              ps1.setString(66, spare_6);
              ps1.setInt(67, Integer.parseInt(subcategory_id));
              ps1.setString(68,code.getSubCategoryCode(subcategory_id));                
            ps1.setString(69, old_asset_id);
           
           // System.out.println("RESULT AFTER UPDATING :::::::::::::::: " + result);
            /*
             * chk if all the entries in am_group_asset have been updated
             * update am_group_asset_main process_flag to Y
             * chk if the approval level setup isn't zero
             * call ad.setPendingtrans
             */
            System.out.println("chk_process_flag qry :::::: " + chk_process_flag);
            ps1 = con.prepareStatement(chk_process_flag);
            rs = ps1.executeQuery();
            if (rs.next())
            {
            	chk_Process_flag = rs.getInt(1);
	            if(chk_Process_flag == 0)
	            {
	            	System.out.println("Nothing to update in am_group_stock!!!!!!");
	            	ps = con.prepareStatement(update_created_asset_main_qry);
	            	ps.setString(1, "Y");
	            	ps.setString(2, group_id2);
	            	ps.executeUpdate();

                        ps = con.prepareStatement(update_created_asset_main_ARCHIVE_qry);
	            	ps.setString(1, "Y");
	            	ps.setString(2, group_id2);
	            	ps.executeUpdate();

	            	status = true;
	            }
            }
	    }
        catch(Exception ex)
        {
            System.out.println("WARN: Error updateCreatedStockStatus ->" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
            dbConnection.closeConnection(con1, ps1);
        }
        return status;
	}
  public boolean updateCreatedAssetStatusBranch(String old_asset_id, String group_id2, String new_asset_id,int assetCode)
    {
		// TODO Auto-generated method stub
    	Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean status = false;
        int chk_Process_flag=0;
        String process_flag ="N";

        String update_created_asset_qry ="update am_group_asset_uncapitalized set process_flag= ?,asset_id=?,REGISTRATION_NO=?, " +
        "BRANCH_ID=?, DEPT_ID=?,SECTION_ID=?, CATEGORY_ID=?,DESCRIPTION=?, VENDOR_AC=?," +
        "DATE_PURCHASED=?, DEP_RATE=?, ASSET_MAKE=?, ASSET_MODEL=?,"+
        "ASSET_SERIAL_NO=?, ASSET_ENGINE_NO=?, SUPPLIER_NAME=?," +
        "ASSET_USER=?, ASSET_MAINTENANCE=?, ACCUM_DEP=?," +
        "COST_PRICE=?, DEP_END_DATE=?, RESIDUAL_VALUE=?," +
        "AUTHORIZED_BY=?, POSTING_DATE=?, EFFECTIVE_DATE=?, PURCHASE_REASON=?," +
        " LOCATION=?, " +
        "VATABLE_COST=?,VAT=?, WH_TAX=?, WH_TAX_AMOUNT=?, REQ_DEPRECIATION=?," +
        "REQ_REDISTRIBUTION=?, SUBJECT_TO_VAT=?, WHO_TO_REM=?, EMAIL1=?," +
        "WHO_TO_REM_2=?, EMAIL2=?, RAISE_ENTRY=?, DEP_YTD=?, SECTION=?," +
        "STATE=?, DRIVER=?, SPARE_1=?, SPARE_2=?, [USER_ID]=?," +
        "PROVINCE=?, WAR_START_DATE=?, WAR_MONTH=?, " +
        "WAR_EXPIRY_DATE=?,LPO=?,BAR_CODE=? ,BRANCH_CODE=?,CATEGORY_CODE=? ," +
        "GROUP_ID=?,PART_PAY=?,FULLY_PAID=?,DEFER_PAY=?,SBU_CODE=?,[dept_code] = ?,[section_code] = ?,asset_Code=?, "+
        "SPARE_3=?, SPARE_4=?, SPARE_5=?, SPARE_6=?,SUB_CATEGORY_ID = ?,SUB_CATEGORY_CODE=?  where asset_id = ? " ;

        String update_created_asset_ARCHIVE_qry ="update am_group_stock_ARCHIVE set process_flag= ?,asset_id=?,REGISTRATION_NO=?, " +
        "BRANCH_ID=?, DEPT_ID=?,SECTION_ID=?, CATEGORY_ID=?,DESCRIPTION=?, VENDOR_AC=?," +
        "DATE_PURCHASED=?, DEP_RATE=?, ASSET_MAKE=?, ASSET_MODEL=?,"+
        "ASSET_SERIAL_NO=?, ASSET_ENGINE_NO=?, SUPPLIER_NAME=?," +
        "ASSET_USER=?, ASSET_MAINTENANCE=?, ACCUM_DEP=?," +
        "COST_PRICE=?, DEP_END_DATE=?, RESIDUAL_VALUE=?," +
        "AUTHORIZED_BY=?, POSTING_DATE=?, EFFECTIVE_DATE=?, PURCHASE_REASON=?," +
        " LOCATION=?, " +
        "VATABLE_COST=?,VAT=?, WH_TAX=?, WH_TAX_AMOUNT=?, REQ_DEPRECIATION=?," +
        "REQ_REDISTRIBUTION=?, SUBJECT_TO_VAT=?, WHO_TO_REM=?, EMAIL1=?," +
        "WHO_TO_REM_2=?, EMAIL2=?, RAISE_ENTRY=?, DEP_YTD=?, SECTION=?," +
        "STATE=?, DRIVER=?, SPARE_1=?, SPARE_2=?, [USER_ID]=?," +
        "PROVINCE=?, WAR_START_DATE=?, WAR_MONTH=?, " +
        "WAR_EXPIRY_DATE=?,LPO=?,BAR_CODE=? ,BRANCH_CODE=?,CATEGORY_CODE=? ," +
        "GROUP_ID=?,PART_PAY=?,FULLY_PAID=?,DEFER_PAY=?,SBU_CODE=?,[dept_code] = ?,[section_code] = ? ,asset_Code=?, "+
        "SPARE_3=?, SPARE_4=?, SPARE_5=?, SPARE_6=?,SUB_CATEGORY_ID = ?,SUB_CATEGORY_CODE=?  where asset_id = ? " ;

        String chk_process_flag ="select count(*) from am_group_asset_uncapitalized WHERE process_flag='"
        						+ process_flag + "'" +"  and Group_id ='"+group_id2+"'";

        String update_created_asset_main_qry = "update am_group_asset_main set process_flag =? "+
       									" where group_id = ?";

        String update_created_asset_main_ARCHIVE_qry = "update am_group_asset_main_archive set process_flag =? "+
       									" where group_id = ?";
        try
	    {
        	con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(update_created_asset_qry);
            ps.setString(1, "Y");
            ps.setString(2, new_asset_id);
            ps.setString(3, registration_no);
            ps.setInt(4, Integer.parseInt(branch_id));
            ps.setInt(5, Integer.parseInt(department_id));
            ps.setInt(6, Integer.parseInt(section_id));
            ps.setInt(7, Integer.parseInt(category_id));
            ps.setString(8, description);
            ps.setString(9, vendor_account);
            ps.setDate(10, dbConnection.dateConvert(date_of_purchase));
            ps.setString(11, getDepreciationRate(category_id));
            ps.setString(12, make);
            ps.setString(13, model);
            ps.setString(14, serial_number);
            ps.setString(15, engine_number);
            ps.setInt(16, Integer.parseInt(supplied_by));
            ps.setString(17, user);
            ps.setInt(18, Integer.parseInt(maintained_by));
            ps.setInt(19, 0);
            ps.setDouble(20, asset_costPrice);
            ps.setDate(21, dbConnection.dateConvert(depreciation_end_date));
            ps.setDouble(22, Double.parseDouble(residual_value));
            ps.setString(23, authorized_by);
            ps.setDate(24, dbConnection.dateConvert(new java.util.Date()));
            ps.setDate(25, dbConnection.dateConvert(depreciation_start_date));
            ps.setString(26, reason);
            ps.setInt(27, Integer.parseInt(location));
            ps.setDouble(28, Double.parseDouble(vatable_cost));
            ps.setDouble(29, Double.parseDouble(vat_amount));
            ps.setString(30, wh_tax_cb);
            ps.setDouble(31, Double.parseDouble(wh_tax_amount));
            ps.setString(32, require_depreciation);
            ps.setString(33, require_redistribution);
            ps.setString(34, subject_to_vat);
            ps.setString(35, who_to_remind);
            ps.setString(36, email_1);
            ps.setString(37, who_to_remind_2);
            ps.setString(38, email2);
            ps.setString(39, "N");
            ps.setString(40, "0");
            ps.setString(41, section);
            ps.setInt(42, Integer.parseInt(state));
            ps.setInt(43, Integer.parseInt(driver));
            ps.setString(44, spare_1);
            ps.setString(45, spare_2);
           // ps.setString(46, "ACTIVE");
            ps.setString(46, user_id);

            ps.setString(47, province);
            ps.setDate(48, dbConnection.dateConvert(warrantyStartDate));
            ps.setInt(49, Integer.parseInt(noOfMonths));
            ps.setDate(50, dbConnection.dateConvert(expiryDate));
            ps.setString(51,lpo);
            ps.setString(52,bar_code);
            ps.setString(53,code.getBranchCode(branch_id));
            ps.setString(54,code.getCategoryCode(category_id));
            ps.setString(55,group_id);
            ps.setString(56, partPAY);
            ps.setString(57, fullyPAID);
            ps.setString(58, deferPay);
            ps.setString(59,sbu_code);
            ps.setString(60,code.getDeptCode(department_id));
            ps.setString(61,code.getSectionCode(section_id));
             ps.setInt(62, assetCode);
             ps.setString(63, spare_3);
             ps.setString(64, spare_4);
             ps.setString(65, spare_5);
             ps.setString(66, spare_6);
             ps.setInt(67, Integer.parseInt(subcategory_id));
             ps.setString(68,code.getSubCategoryCode(subcategory_id));                  
            ps.setString(69, old_asset_id);
            int result = ps.executeUpdate();

            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(update_created_asset_ARCHIVE_qry);
            ps.setString(1, "Y");
            ps.setString(2, new_asset_id);
            ps.setString(3, registration_no);
            ps.setInt(4, Integer.parseInt(branch_id));
            ps.setInt(5, Integer.parseInt(department_id));
            ps.setInt(6, Integer.parseInt(section_id));
            ps.setInt(7, Integer.parseInt(category_id));
            ps.setString(8, description);
            ps.setString(9, vendor_account);
            ps.setDate(10, dbConnection.dateConvert(date_of_purchase));
            ps.setString(11, getDepreciationRate(category_id));
            ps.setString(12, make);
            ps.setString(13, model);
            ps.setString(14, serial_number);
            ps.setString(15, engine_number);
            ps.setInt(16, Integer.parseInt(supplied_by));
            ps.setString(17, user);
            ps.setInt(18, Integer.parseInt(maintained_by));
            ps.setInt(19, 0);
            ps.setDouble(20, asset_costPrice);
            ps.setDate(21, dbConnection.dateConvert(depreciation_end_date));
            ps.setDouble(22, Double.parseDouble(residual_value));
            ps.setString(23, authorized_by);
            ps.setDate(24, dbConnection.dateConvert(new java.util.Date()));
            ps.setDate(25, dbConnection.dateConvert(depreciation_start_date));
            ps.setString(26, reason);
            ps.setInt(27, Integer.parseInt(location));
            ps.setDouble(28, Double.parseDouble(vatable_cost));
            ps.setDouble(29, Double.parseDouble(vat_amount));
            ps.setString(30, wh_tax_cb);
            ps.setDouble(31, Double.parseDouble(wh_tax_amount));
            ps.setString(32, require_depreciation);
            ps.setString(33, require_redistribution);
            ps.setString(34, subject_to_vat);
            ps.setString(35, who_to_remind);
            ps.setString(36, email_1);
            ps.setString(37, who_to_remind_2);
            ps.setString(38, email2);
            ps.setString(39, "N");
            ps.setString(40, "0");
            ps.setString(41, section);
            ps.setInt(42, Integer.parseInt(state));
            ps.setInt(43, Integer.parseInt(driver));
            ps.setString(44, spare_1);
            ps.setString(45, spare_2);
           // ps.setString(46, "ACTIVE");
            ps.setString(46, user_id);

            ps.setString(47, province);
            ps.setDate(48, dbConnection.dateConvert(warrantyStartDate));
            ps.setInt(49, Integer.parseInt(noOfMonths));
            ps.setDate(50, dbConnection.dateConvert(expiryDate));
            ps.setString(51,lpo);
            ps.setString(52,bar_code);
            ps.setString(53,code.getBranchCode(branch_id));
            ps.setString(54,code.getCategoryCode(category_id));
            ps.setString(55,group_id);
            ps.setString(56, partPAY);
            ps.setString(57, fullyPAID);
            ps.setString(58, deferPay);
            ps.setString(59,sbu_code);
            ps.setString(60,code.getDeptCode(department_id));
            ps.setString(61,code.getSectionCode(section_id));
            ps.setInt(62, assetCode);
            ps.setString(63, spare_3);
            ps.setString(64, spare_4);
            ps.setString(65, spare_5);
            ps.setString(66, spare_6);
            ps.setInt(67, Integer.parseInt(subcategory_id));
            ps.setString(68,code.getSubCategoryCode(subcategory_id));                    
            ps.setString(69, old_asset_id);
           // System.out.println("RESULT AFTER UPDATING :::::::::::::::: " + result);
            /*
             * chk if all the entries in am_group_asset have been updated
             * update am_group_asset_main process_flag to Y
             * chk if the approval level setup isn't zero
             * call ad.setPendingtrans
             */
            System.out.println("chk_process_flag qry :::::: " + chk_process_flag);
            ps = con.prepareStatement(chk_process_flag);
            rs = ps.executeQuery();
            if (rs.next())
            {
            	chk_Process_flag = rs.getInt(1);
	            if(chk_Process_flag == 0)
	            {
	            	System.out.println("Nothing to update in am_group_stock!!!!!!");
	            	ps = con.prepareStatement(update_created_asset_main_qry);
	            	ps.setString(1, "Y");
	            	ps.setString(2, group_id2);
	            	ps.executeUpdate();

                        ps = con.prepareStatement(update_created_asset_main_ARCHIVE_qry);
	            	ps.setString(1, "Y");
	            	ps.setString(2, group_id2);
	            	ps.executeUpdate();

	            	status = true;
	            }
            }
	    }
        catch(Exception ex)
        {
            System.out.println("WARN: Error updateCreatedStockStatus ->" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }
        return status;
}

      public boolean updateAssetBranch(String assetID) throws Exception, Throwable {
    	/*System.out.println("branch_id ::::::::: " + branch_id);
        System.out.println("department_id ::::::::: " + department_id);
        System.out.println("section_id ::::::::: " + section_id);
        System.out.println("category_id ::::::::: " + category_id);
        System.out.println("Branch Code : " +code.getBranchCode(branch_id));
        System.out.println("Category Code " + code.getCategoryCode(category_id));*/
         //System.out.println("New Asset_ID ::::::::: " + asset_id_new);
        Connection con = null;
        PreparedStatement ps = null;
        boolean done = true;

        if (make == null || make.equals("")) {
            make = "0";
        }
        if (maintained_by == null || maintained_by.equals("")) {
            maintained_by = "0";
        }
        if (supplied_by == null || supplied_by.equals("")) {
            supplied_by = "0";
        }
        if (user == null || user.equals("")) {
            user = "";
        }
        if (location == null || location.equals("")) {
            location = "0";
        }
        if (driver == null || driver.equals("")) {
            driver = "0";
        }
        if (state == null || state.equals("")) {
            state = "0";
        }
        if (department_id == null || department_id.equals("")) {
            department_id = "0";
        }
        if (vat_amount == null || vat_amount.equals("")) {
            vat_amount = "0.0";
        }
        if (vatable_cost == null || vatable_cost.equals("")) {
            vatable_cost = "0.0";
        }
        if (wh_tax_amount == null || wh_tax_amount.equals("")) {
            wh_tax_amount = "0";
        }
        if (branch_id == null || branch_id.equals("")) {
            branch_id = "0";
        }
        if (province == null || province.equals("")) {
            province = "0";
        }
        if (category_id == null || category_id.equals("")) {
            category_id = "0";
        }
        if (subcategory_id == null || subcategory_id.equals("")) {
            subcategory_id = "0";
        }
        if (residual_value == null || residual_value.equals("")) {
            residual_value = "0";
        }
        if (noOfMonths == null || noOfMonths.equals("")) {
            noOfMonths = "0";
        }
        if (warrantyStartDate == null || warrantyStartDate.equals("")) {
            warrantyStartDate = null;
        }
        if (expiryDate == null || expiryDate.equals("")) {
            expiryDate = null;
        }
        vat_amount = vat_amount.replaceAll(",", "");
        vatable_cost = vatable_cost.replaceAll(",", "");
        wh_tax_amount = wh_tax_amount.replaceAll(",", "");
        residual_value = residual_value.replaceAll(",", "");


        String createQuery = "Update AM_ASSET_UNCAPITALIZED " +
        " set REGISTRATION_NO=?, DESCRIPTION=?, VENDOR_AC=?,"+
        " DATE_PURCHASED=?, ASSET_MAKE=?, ASSET_MODEL=?,"+
        " ASSET_SERIAL_NO=?, ASSET_ENGINE_NO=?, SUPPLIER_NAME=?," +

        " ASSET_USER=?, ASSET_MAINTENANCE=?, ACCUM_DEP=?, MONTHLY_DEP=?," +
        " COST_PRICE=?, NBV=?, DEP_END_DATE=?, RESIDUAL_VALUE=?,"+
        " AUTHORIZED_BY=?, POSTING_DATE=?, EFFECTIVE_DATE=?, PURCHASE_REASON=?,"+
        " USEFUL_LIFE=?,  LOCATION=?, "+

        " VATABLE_COST=?,VAT=?, WH_TAX=?, WH_TAX_AMOUNT=?, REQ_DEPRECIATION=?,"+
        " REQ_REDISTRIBUTION=?, SUBJECT_TO_VAT=?, WHO_TO_REM=?, EMAIL1=?,"+
        " WHO_TO_REM_2=?, EMAIL2=?, RAISE_ENTRY=?, DEP_YTD=?, [SECTION]=?,"+
        " STATE=?, DRIVER=?, SPARE_1=?, SPARE_2=?, ASSET_STATUS=?, [USER_ID]=?,"+
        " MULTIPLE=?,PROVINCE=?, WAR_START_DATE=?, WAR_MONTH=?,"+
        " WAR_EXPIRY_DATE=?,LPO=?,BAR_CODE=?,"+
        " PART_PAY=?,FULLY_PAID=?,DEFER_PAY=?,SBU_CODE=?,SECTION_CODE=?,"+
        " DEPT_ID=?,SECTION_ID=?,DEPT_CODE=?, "+
        " SPARE_3=?, SPARE_4=?, SPARE_5=?, SPARE_6=?,SUB_CATEGORY_ID = ?,SUB_CATEGORY_CODE=? WHERE ASSET_ID= ?"  ;


        //System.out.println("createQuery >>>>>  " + createQuery);
        /*
         *First Create Asset Records
         * and then determine if it
         * should be made available for fleet.
         */
           try
           {
            asset_costPrice = Double.parseDouble(vat_amount) +
                               Double.parseDouble(vatable_cost);
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(createQuery);
            ps.setString(1, registration_no);
            ps.setString(2, description);
            ps.setString(3, vendor_account);
            ps.setDate(4, dbConnection.dateConvert(date_of_purchase));
            ps.setString(5, make);
            ps.setString(6, model);
            ps.setString(7, serial_number);
            ps.setString(8, engine_number);
            ps.setInt(9, Integer.parseInt(supplied_by));
            ps.setString(10, user);
            ps.setInt(11, Integer.parseInt(maintained_by));
            ps.setInt(12, 0);
            ps.setInt(13, 0);
            ps.setDouble(14, asset_costPrice);
            ps.setDouble(15, asset_costPrice);
            ps.setDate(16, dbConnection.dateConvert(depreciation_end_date));
            ps.setDouble(17, Double.parseDouble(residual_value));
            ps.setString(18, authorized_by);
            ps.setDate(19, dbConnection.dateConvert(new java.util.Date()));
            ps.setDate(20, dbConnection.dateConvert(depreciation_start_date));
            ps.setString(21, reason);
            ps.setString(22, "0");
            ps.setInt(23, Integer.parseInt(location));
            ps.setDouble(24, Double.parseDouble(vatable_cost));
            ps.setDouble(25, Double.parseDouble(vat_amount));
            ps.setString(26, wh_tax_cb);
            ps.setDouble(27, Double.parseDouble(wh_tax_amount));
            ps.setString(28, require_depreciation);
            ps.setString(29, require_redistribution);
            ps.setString(30, subject_to_vat);
            ps.setString(31, who_to_remind);
            ps.setString(32, email_1);
            ps.setString(33, who_to_remind_2);
            ps.setString(34, email2);
            ps.setString(35, "N");
            ps.setString(36, "0");
            ps.setString(37, section);
            ps.setInt(38, Integer.parseInt(state));
            ps.setInt(39, Integer.parseInt(driver));
            ps.setString(40, spare_1);
            ps.setString(41, spare_2);
            ps.setString(42, "PENDING");
            ps.setString(43, user_id);
            ps.setString(44, multiple);
            ps.setString(45, province);
            ps.setDate(46, dbConnection.dateConvert(warrantyStartDate));
            ps.setInt(47, Integer.parseInt(noOfMonths));
            ps.setDate(48, dbConnection.dateConvert(expiryDate));
            ps.setString(49,lpo);
            ps.setString(50,bar_code);
            ps.setString(51, partPAY);
            ps.setString(52, fullyPAID);
            ps.setString(53, deferPay);
            ps.setString(54,sbu_code);
            ps.setString(55,code.getSectionCode(section_id));

            ps.setInt(56, Integer.parseInt(department_id));
            ps.setInt(57, Integer.parseInt(section_id));
            ps.setString(58,code.getDeptCode(department_id));
            ps.setString(59, spare_3);
            ps.setString(60, spare_4);
            ps.setString(61, spare_5);
            ps.setString(62, spare_6);
            ps.setInt(63, Integer.parseInt(subcategory_id));
            ps.setString(64,code.getSubCategoryCode(subcategory_id));    

            ps.setString(65,assetID);

            boolean result = ps.execute();
           //System.out.println("Result Of Update>>>>>> " + result);
            setAssetId(asset_id);

      	  	boolean approval_level_val =checkApprovalStatus("19");
      	  	boolean status = repostCreatedAssetBranch(asset_id,group_id);
      	  	if ((!approval_level_val)&&(status))
	      	  	{
      	  			/*
      	  			 * ASSETS THAT DON'T HAVE APPROVAL CAN ONLY BE REJECTED AT POSTING LEVEL*/
      	  			//System.out.println("ASSETS THAT DON'T HAVE APPROVAL CAN ONLY BE REJECTED AT POSTING LEVEL");
      	  			changeGroupAssetStatusBranch(group_id,"APPROVED");
      	  			String [] approvalResult=ad.setApprovalDataGroup(Long.parseLong(group_id));
      	  			approvalResult[10]="A";
      	  			updateGroupPendingTrans(approvalResult,"3",group_id);

	      	  		String page1 = "ASSET GROUP CREATION RAISE ENTRY";
	                String flag= "";
	          	  	String partPay="";
	          	  	String qryBranch =" SELECT branch_name from am_ad_branch where branch_id='"+branch_id+"'";
	          	  	String qryTrans_Id="SELECT transaction_id from am_asset_approval where asset_id='"+group_id + "'";
	          	   	String Branch = approvalRec.getCodeName(qryBranch);
	          	   	String trans_id=approvalRec.getCodeName(qryTrans_Id);
	          	   	System.out.println("trans_id during Update >>>> " + trans_id);
	          	   	String subjectT= adGroup.subjectToVat(group_id);
	          	   	String whT= adGroup.whTax(group_id);
	          	   	String Name =approvalRec.getCodeName(" SELECT full_name from am_gb_user where user_id='"+user_id+"'");
	          	   	String url = "DocumentHelp.jsp?np=groupAssetPosting&amp;id=" + group_id + "&pageDirect=Y";

	          	  approvalRec.insertApproval
	      	  		(group_id, approvalResult[5], page1, flag, partPay,Name, Branch, subjectT, whT, url,Integer.parseInt(trans_id));


      	  			String qry = upd_am_asset_branch_status_RaiseEntry + group_id;
      	  			String qry2 = upd_am_grp_asset_branch_RaiseEntry + group_id;
      	  			String qry3 = upd_am_grp_asset_main_RaiseEntry +  group_id;
	      	  		/*System.out.println("qry : " + qry);
	  	  			System.out.println("qry2 : " + qry2 );
	  	  			System.out.println("qry3 : " + qry3);*/
	      	  		updateStatusUtil(qry);
	      	  		updateStatusUtil(qry2);
	      	  		updateStatusUtil(qry3);

	      	  	}

	      	if((status)&&(approval_level_val))
		      	{
		      	 System.out.println("====== UPDATING  am_asset_Approval For Rejection ======");
		      	 changeGroupAssetStatusBranch(group_id,"PENDING");
		      	 updateGroupPendingTrans(ad.setApprovalDataGroup(Long.parseLong(group_id)),"3",group_id);
		      	 //write a method to change status to pending in am_approval
		      	 //the amount in approval is the old amount so the best thing is to read again from the table
		      	 //and update the new values
		      	 //UPDATE THE SUPERVISOR VALUE !!!!!!!!!!!!!!!!
		      	}
        }
        catch (Exception ex)
        {
            done = false;
            System.out.println("WARN:GroupInventoryToStockBean - Error Updating am_asset->" + ex);
        }
        finally
        {
            dbConnection.closeConnection(con, ps);
        }

        return done;

    }

       public boolean repostCreatedAssetBranch(String asset_id, String group_id2)
    {
		// TODO Auto-generated method stub
    	Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean status = false;
        int chk_Process_flag=0;
        String process_flag ="N";

        String update_created_asset_qry ="update am_group_asset_uncapitalized set process_flag= ?,REGISTRATION_NO=?, " +
        " DEPT_ID=?,SECTION_ID=?, DESCRIPTION=?, VENDOR_AC=?," +
        "DATE_PURCHASED=?, ASSET_MAKE=?, ASSET_MODEL=?,"+
        "ASSET_SERIAL_NO=?, ASSET_ENGINE_NO=?, SUPPLIER_NAME=?," +
        "ASSET_USER=?, ASSET_MAINTENANCE=?, ACCUM_DEP=?," +
        "COST_PRICE=?, DEP_END_DATE=?, RESIDUAL_VALUE=?," +
        "AUTHORIZED_BY=?, POSTING_DATE=?, EFFECTIVE_DATE=?, PURCHASE_REASON=?," +
        " LOCATION=?, " +
        "VATABLE_COST=?,VAT=?, WH_TAX=?, WH_TAX_AMOUNT=?, REQ_DEPRECIATION=?," +
        "REQ_REDISTRIBUTION=?, SUBJECT_TO_VAT=?, WHO_TO_REM=?, EMAIL1=?," +
        "WHO_TO_REM_2=?, EMAIL2=?, RAISE_ENTRY=?, DEP_YTD=?, SECTION=?," +
        "STATE=?, DRIVER=?, SPARE_1=?, SPARE_2=?, [USER_ID]=?," +
        "PROVINCE=?, WAR_START_DATE=?, WAR_MONTH=?, " +
        "WAR_EXPIRY_DATE=?,LPO=?,BAR_CODE=? ," +
        "PART_PAY=?,FULLY_PAID=?,DEFER_PAY=?,SBU_CODE=?,[dept_code] = ?,[section_code] = ?, "+
        "SPARE_3=?, SPARE_4=?, SPARE_5=?, SPARE_6=?,SUB_CATEGORY_ID = ?,SUB_CATEGORY_CODE=?  where asset_id = ? " ;
        String chk_process_flag ="select count(*) from am_group_asset_uncapitalized WHERE process_flag='"
        						+ process_flag + "'" +"  and Group_id ='"+group_id2+"'";

        String update_created_asset_main_qry = "update am_group_asset_main set process_flag =? "+
       									" where group_id = ?";
        try
	    {
        	con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(update_created_asset_qry);
            ps.setString(1, "Y");
            ps.setString(2, registration_no);
            ps.setInt(3, Integer.parseInt(department_id));
            ps.setInt(4, Integer.parseInt(section_id));
            ps.setString(5, description);
            ps.setString(6, vendor_account);
            ps.setDate(7, dbConnection.dateConvert(date_of_purchase));
            ps.setString(8, make);
            ps.setString(9, model);
            ps.setString(10, serial_number);
            ps.setString(11, engine_number);
            ps.setInt(12, Integer.parseInt(supplied_by));
            ps.setString(13, user);
            ps.setInt(14, Integer.parseInt(maintained_by));
            ps.setInt(15, 0);
            ps.setDouble(16, asset_costPrice);
            ps.setDate(17, dbConnection.dateConvert(depreciation_end_date));
            ps.setDouble(18, Double.parseDouble(residual_value));
            ps.setString(19, authorized_by);
            ps.setDate(20, dbConnection.dateConvert(new java.util.Date()));
            ps.setDate(21, dbConnection.dateConvert(depreciation_start_date));
            ps.setString(22, reason);
            ps.setInt(23, Integer.parseInt(location));
            ps.setDouble(24, Double.parseDouble(vatable_cost));
            ps.setDouble(25, Double.parseDouble(vat_amount));
            ps.setString(26, wh_tax_cb);
            ps.setDouble(27, Double.parseDouble(wh_tax_amount));
            ps.setString(28, require_depreciation);
            ps.setString(29, require_redistribution);
            ps.setString(30, subject_to_vat);
            ps.setString(31, who_to_remind);
            ps.setString(32, email_1);
            ps.setString(33, who_to_remind_2);
            ps.setString(34, email2);
            ps.setString(35, "N");
            ps.setString(36, "0");
            ps.setString(37, section);
            ps.setInt(38, Integer.parseInt(state));
            ps.setInt(39, Integer.parseInt(driver));
            ps.setString(40, spare_1);
            ps.setString(41, spare_2);
           // ps.setString(46, "ACTIVE");
            ps.setString(42, user_id);

            ps.setString(43, province);
            ps.setDate(44, dbConnection.dateConvert(warrantyStartDate));
            ps.setInt(45, Integer.parseInt(noOfMonths));
            ps.setDate(46, dbConnection.dateConvert(expiryDate));
            ps.setString(47,lpo);
            ps.setString(48,bar_code);


            ps.setString(49, partPAY);
            ps.setString(50, fullyPAID);
            ps.setString(51, deferPay);
            ps.setString(52,sbu_code);
            ps.setString(53,code.getDeptCode(department_id));
            ps.setString(54,code.getSectionCode(section_id));
            ps.setString(55, spare_3);
            ps.setString(56, spare_4);
            ps.setString(57, spare_5);
            ps.setString(58, spare_6);
            ps.setInt(59, Integer.parseInt(subcategory_id));
            ps.setString(60,code.getSubCategoryCode(subcategory_id));    
            
            ps.setString(61, asset_id);
            int result = ps.executeUpdate();
           // System.out.println("RESULT AFTER UPDATING :::::::::::::::: " + result);
            /*
             * chk if all the entries in am_group_asset have been updated
             * update am_group_asset_main process_flag to Y
             * chk if the approval level setup isn't zero
             * call ad.setPendingtrans
             */
            System.out.println("chk_process_flag qry :::::: " + chk_process_flag);
            ps = con.prepareStatement(chk_process_flag);
            rs = ps.executeQuery();
            if (rs.next())
            {
            	chk_Process_flag = rs.getInt(1);
	            if(chk_Process_flag == 0)
	            {
	            	System.out.println("Nothing to update in am_group_asset!!!!!!");
	            	ps = con.prepareStatement(update_created_asset_main_qry);
	            	ps.setString(1, "Y");
	            	ps.setString(2, group_id2);
	            	ps.executeUpdate();
	            	status = true;
	            }
            }
	    }
        catch(Exception ex)
        {
            System.out.println("WARN: Error updateCreatedAssetStatus ->" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }
        return status;
	}
       public boolean saveBranch2() throws Exception, Throwable 
    {
    	/*System.out.println("branch_id ::::::::: " + branch_id);
        System.out.println("department_id ::::::::: " + department_id);
        System.out.println("section_id ::::::::: " + section_id);
        System.out.println("category_id ::::::::: " + category_id);
        System.out.println("Branch Code : " +code.getBranchCode(branch_id));
        System.out.println("Category Code " + code.getCategoryCode(category_id));*/
        asset_id_new = new legend.AutoIDSetup().getIdentity(branch_id,
                department_id, section_id, category_id);
  assetCode = Integer.parseInt(new ApplicationHelper().getGeneratedId("ST_STOCK"));
        //System.out.println("New Asset_ID ::::::::: " + asset_id_new);
        Connection con = null;
        PreparedStatement ps = null;
        boolean done = true;

        if (make == null || make.equals("")) {
            make = "0";
        }
        if (maintained_by == null || maintained_by.equals("")) {
            maintained_by = "0";
        }
        if (supplied_by == null || supplied_by.equals("")) {
            supplied_by = "0";
        }
        if (user == null || user.equals("")) {
            user = "";
        }
        if (location == null || location.equals("")) {
            location = "0";
        }
        if (driver == null || driver.equals("")) {
            driver = "0";
        }
        if (state == null || state.equals("")) {
            state = "0";
        }
        if (department_id == null || department_id.equals("")) {
            department_id = "0";
        }
        if (vat_amount == null || vat_amount.equals("")) {
            vat_amount = "0.0";
        }
        if (vatable_cost == null || vatable_cost.equals("")) {
            vatable_cost = "0.0";
        }
        if (wh_tax_amount == null || wh_tax_amount.equals("")) {
            wh_tax_amount = "0";
        }
        if (branch_id == null || branch_id.equals("")) {
            branch_id = "0";
        }
        if (province == null || province.equals("")) {
            province = "0";
        }
        if (category_id == null || category_id.equals("")) {
            category_id = "0";
        }
        if (subcategory_id == null || subcategory_id.equals("")) {
            subcategory_id = "0";
        }
        
        if (residual_value == null || residual_value.equals("")) {
            residual_value = "0";
        }
        if (noOfMonths == null || noOfMonths.equals("")) {
            noOfMonths = "0";
        }
        if (warrantyStartDate == null || warrantyStartDate.equals("")) {
            warrantyStartDate = null;
        }
        if (expiryDate == null || expiryDate.equals("")) {
            expiryDate = null;
        }
        vat_amount = vat_amount.replaceAll(",", "");
        vatable_cost = vatable_cost.replaceAll(",", "");
        wh_tax_amount = wh_tax_amount.replaceAll(",", "");
        residual_value = residual_value.replaceAll(",", "");
        String    createQuery = "INSERT INTO AM_ASSET_UNCAPITALIZED         " +
           "(" +
           "ASSET_ID, REGISTRATION_NO, BRANCH_ID, DEPT_ID," +
           "SECTION_ID, CATEGORY_ID, [DESCRIPTION], VENDOR_AC," +
           "DATE_PURCHASED, DEP_RATE, ASSET_MAKE, ASSET_MODEL," +
           "ASSET_SERIAL_NO, ASSET_ENGINE_NO, SUPPLIER_NAME," +

           "ASSET_USER, ASSET_MAINTENANCE, ACCUM_DEP, MONTHLY_DEP," +
           "COST_PRICE, NBV, DEP_END_DATE, RESIDUAL_VALUE," +
           "AUTHORIZED_BY, POSTING_DATE, EFFECTIVE_DATE, PURCHASE_REASON," +
           "USEFUL_LIFE, TOTAL_LIFE, LOCATION, REMAINING_LIFE," +

           "VATABLE_COST,VAT, WH_TAX, WH_TAX_AMOUNT, REQ_DEPRECIATION," +
           "REQ_REDISTRIBUTION, SUBJECT_TO_VAT, WHO_TO_REM, EMAIL1," +
           "WHO_TO_REM_2, EMAIL2, RAISE_ENTRY, DEP_YTD, [SECTION]," +
           "STATE, DRIVER, SPARE_1, SPARE_2, ASSET_STATUS, [USER_ID]," +
           "MULTIPLE,PROVINCE, WAR_START_DATE, WAR_MONTH, " +
           "WAR_EXPIRY_DATE,LPO,BAR_CODE ,BRANCH_CODE,CATEGORY_CODE ," +
           "GROUP_ID,PART_PAY,FULLY_PAID,DEFER_PAY,SBU_CODE,SECTION_CODE,DEPT_CODE,system_ip,asset_Code,"+
           "SUB_CATEGORY_ID,SUB_CATEGORY_CODE, SPARE_3, SPARE_4, SPARE_5, SPARE_6 ) " +

           "VALUES" +
           "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
           "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


        String create_Archive_Query = "INSERT INTO AM_ASSET_ARCHIVE         " +
        "(" +
        "ASSET_ID, REGISTRATION_NO, BRANCH_ID, DEPT_ID," +
        "SECTION_ID, CATEGORY_ID, [DESCRIPTION], VENDOR_AC," +
        "DATE_PURCHASED, DEP_RATE, ASSET_MAKE, ASSET_MODEL," +
        "ASSET_SERIAL_NO, ASSET_ENGINE_NO, SUPPLIER_NAME," +

        "ASSET_USER, ASSET_MAINTENANCE, ACCUM_DEP, MONTHLY_DEP," +
        "COST_PRICE, NBV, DEP_END_DATE, RESIDUAL_VALUE," +
        "AUTHORIZED_BY, POSTING_DATE, EFFECTIVE_DATE, PURCHASE_REASON," +
        "USEFUL_LIFE, TOTAL_LIFE, LOCATION, REMAINING_LIFE," +

        "VATABLE_COST,VAT, WH_TAX, WH_TAX_AMOUNT, REQ_DEPRECIATION," +
        "REQ_REDISTRIBUTION, SUBJECT_TO_VAT, WHO_TO_REM, EMAIL1," +
        "WHO_TO_REM_2, EMAIL2, RAISE_ENTRY, DEP_YTD, [SECTION]," +
        "STATE, DRIVER, SPARE_1, SPARE_2, ASSET_STATUS, [USER_ID]," +
        "MULTIPLE,PROVINCE, WAR_START_DATE, WAR_MONTH, " +
        "WAR_EXPIRY_DATE,LPO,BAR_CODE ,BRANCH_CODE,CATEGORY_CODE ," +
        "GROUP_ID,PART_PAY,FULLY_PAID,DEFER_PAY,SBU_CODE,SECTION_CODE,DEPT_CODE,system_ip,asset_Code," +
        "SUB_CATEGORY_ID,SUB_CATEGORY_CODE, SPARE_3, SPARE_4, SPARE_5, SPARE_6 ) " +

        "VALUES" +
        "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        /*
         *First Create Asset Records
         * and then determine if it
         * should be made available for fleet.
         */
           try
           {
            asset_costPrice = Double.parseDouble(vat_amount) + Double.parseDouble(vatable_cost);
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(createQuery);
            ps.setString(1, asset_id_new);
            ps.setString(2, registration_no);
            ps.setInt(3, Integer.parseInt(branch_id));
            ps.setInt(4, Integer.parseInt(department_id));
            ps.setInt(5, Integer.parseInt(section_id));
            ps.setInt(6, Integer.parseInt(category_id));
            ps.setString(7, description);
            ps.setString(8, vendor_account);
            ps.setDate(9, dbConnection.dateConvert(date_of_purchase));
            ps.setString(10, getDepreciationRate(category_id));
            ps.setString(11, make);
            ps.setString(12, model);
            ps.setString(13, serial_number);
            ps.setString(14, engine_number);
            ps.setInt(15, Integer.parseInt(supplied_by));
            ps.setString(16, user);
            ps.setInt(17, Integer.parseInt(maintained_by));
            ps.setInt(18, 0);
            ps.setInt(19, 0);
            ps.setDouble(20, asset_costPrice);
            ps.setDouble(21, asset_costPrice);
            ps.setDate(22, dbConnection.dateConvert(depreciation_end_date));
            ps.setDouble(23, Double.parseDouble(residual_value));
            ps.setString(24, authorized_by);
            ps.setDate(25, dbConnection.dateConvert(new java.util.Date()));
            ps.setDate(26, dbConnection.dateConvert(depreciation_start_date));
            ps.setString(27, reason);
            ps.setString(28, "0");
            ps.setString(29, computeTotalLife(getDepreciationRate(category_id)));
            ps.setInt(30, Integer.parseInt(location));
            ps.setString(31, computeTotalLife(getDepreciationRate(category_id)));
            ps.setDouble(32, Double.parseDouble(vatable_cost));
            ps.setDouble(33, Double.parseDouble(vat_amount));
            ps.setString(34, wh_tax_cb);
            ps.setDouble(35, Double.parseDouble(wh_tax_amount));
            ps.setString(36, require_depreciation);
            ps.setString(37, require_redistribution);
            ps.setString(38, subject_to_vat);
            ps.setString(39, who_to_remind);
            ps.setString(40, email_1);
            ps.setString(41, who_to_remind_2);
            ps.setString(42, email2);
            ps.setString(43, "N");
            ps.setString(44, "0");
            ps.setString(45, section);
            ps.setInt(46, Integer.parseInt(state));
            ps.setInt(47, Integer.parseInt(driver));
            ps.setString(48, spare_1);
            ps.setString(49, spare_2);
            ps.setString(50, asset_Status);
            ps.setString(51, user_id);
            ps.setString(52, multiple);
            ps.setString(53, province);
            ps.setDate(54, dbConnection.dateConvert(warrantyStartDate));
            ps.setInt(55, Integer.parseInt(noOfMonths));
            ps.setDate(56, dbConnection.dateConvert(expiryDate));
            ps.setString(57,lpo);
            ps.setString(58,bar_code);
            ps.setString(59,code.getBranchCode(branch_id));
            ps.setString(60,code.getCategoryCode(category_id));
            ps.setString(61,group_id);
            /*ps.setString(62, partPAY);
            ps.setString(63, fullyPAID);
            ps.setString(64, deferPay);*/
            ps.setString(62, "N");
            ps.setString(63, "Y");
            ps.setString(64, "N");
            ps.setString(65,sbu_code);
            ps.setString(66,code.getSectionCode(section_id));
            ps.setString(67,code.getDeptCode(department_id));
            ps.setString(68,workstationIp);
           ps.setInt(69,assetCode);
           ps.setInt(70, Integer.parseInt(subcategory_id));
           ps.setString(71,code.getSubCategoryCode(subcategory_id));
           ps.setString(72, spare_3);
           ps.setString(73, spare_4);
           ps.setString(74, spare_5);
           ps.setString(75, spare_6);

            //not right

            boolean result = ps.execute();

            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(create_Archive_Query);
            ps.setString(1, asset_id_new);
            ps.setString(2, registration_no);
            ps.setInt(3, Integer.parseInt(branch_id));
            ps.setInt(4, Integer.parseInt(department_id));
            ps.setInt(5, Integer.parseInt(section_id));
            ps.setInt(6, Integer.parseInt(category_id));
            ps.setString(7, description);
            ps.setString(8, vendor_account);
            ps.setDate(9, dbConnection.dateConvert(date_of_purchase));
            ps.setString(10, getDepreciationRate(category_id));
            ps.setString(11, make);
            ps.setString(12, model);
            ps.setString(13, serial_number);
            ps.setString(14, engine_number);
            ps.setInt(15, Integer.parseInt(supplied_by));
            ps.setString(16, user);
            ps.setInt(17, Integer.parseInt(maintained_by));
            ps.setInt(18, 0);
            ps.setInt(19, 0);
            ps.setDouble(20, asset_costPrice);
            ps.setDouble(21, asset_costPrice);
            ps.setDate(22, dbConnection.dateConvert(depreciation_end_date));
            ps.setDouble(23, Double.parseDouble(residual_value));
            ps.setString(24, authorized_by);
            ps.setDate(25, dbConnection.dateConvert(new java.util.Date()));
            ps.setDate(26, dbConnection.dateConvert(depreciation_start_date));
            ps.setString(27, reason);
            ps.setString(28, "0");
            ps.setString(29, computeTotalLife(getDepreciationRate(category_id)));
            ps.setInt(30, Integer.parseInt(location));
            ps.setString(31, computeTotalLife(getDepreciationRate(category_id)));
            ps.setDouble(32, Double.parseDouble(vatable_cost));
            ps.setDouble(33, Double.parseDouble(vat_amount));
            ps.setString(34, wh_tax_cb);
            ps.setDouble(35, Double.parseDouble(wh_tax_amount));
            ps.setString(36, require_depreciation);
            ps.setString(37, require_redistribution);
            ps.setString(38, subject_to_vat);
            ps.setString(39, who_to_remind);
            ps.setString(40, email_1);
            ps.setString(41, who_to_remind_2);
            ps.setString(42, email2);
            ps.setString(43, "N");
            ps.setString(44, "0");
            ps.setString(45, section);
            ps.setInt(46, Integer.parseInt(state));
            ps.setInt(47, Integer.parseInt(driver));
            ps.setString(48, spare_1);
            ps.setString(49, spare_2);
            ps.setString(50, asset_Status);
            ps.setString(51, user_id);
            ps.setString(52, multiple);
            ps.setString(53, province);
            ps.setDate(54, dbConnection.dateConvert(warrantyStartDate));
            ps.setInt(55, Integer.parseInt(noOfMonths));
            ps.setDate(56, dbConnection.dateConvert(expiryDate));
            ps.setString(57,lpo);
            ps.setString(58,bar_code);
            ps.setString(59,code.getBranchCode(branch_id));
            ps.setString(60,code.getCategoryCode(category_id));
            ps.setString(61,group_id);
            /*ps.setString(62, partPAY);
            ps.setString(63, fullyPAID);
            ps.setString(64, deferPay);*/
            ps.setString(62, "N");
            ps.setString(63, "Y");
            ps.setString(64, "N");
            ps.setString(65,sbu_code);
            ps.setString(66,code.getSectionCode(section_id));
            ps.setString(67,code.getDeptCode(department_id));
            ps.setString(68,workstationIp);
               ps.setInt(69,assetCode);
               ps.setInt(70, Integer.parseInt(subcategory_id));
               ps.setString(71,code.getSubCategoryCode(subcategory_id));
               ps.setString(72, spare_3);
               ps.setString(73, spare_4);
               ps.setString(74, spare_5);
               ps.setString(75, spare_6);               
           // System.out.println("=====================================================");
           // System.out.println("Result Of Insertion into Asset Table From group Asset : " + result);
           // System.out.println("=====================================================");
             result = ps.execute();


           htmlUtil.insGrpToAm_Invoice_No(asset_id_new,lpo,invoiceNum,"Asset Creation",group_id);

            FleetHistoryManager fm = new FleetHistoryManager();
            if (fm.isRequiredForFleet(asset_id))
            {
            	System.out.println(">>>>>>>>>>>>>>>>>> Asset is Required For Fleet <<<<<<<<<<<<<<<<<<<<<<<<<<<");
               fm.copyAssetDataToFleet(asset_id);
            }
            setAssetId(asset_id);

            String page1 = "ASSET GROUP CREATION RAISE ENTRY";
            String flag= "";
      	  	String partPay="";
      	  	String qryBranch =" SELECT branch_name from am_ad_branch where branch_id='"+branch_id+"'";
      	   	String Branch = approvalRec.getCodeName(qryBranch);
      	   	String subjectT= adGroup.subjectToVat(group_id);
      	   	String whT= adGroup.whTax(group_id);
      	   	String Name =approvalRec.getCodeName(" SELECT full_name from am_gb_user where user_id='"+user_id+"'");
      	   	String url = "DocumentHelp.jsp?np=groupAssetPosting&amp;id=" + group_id + "&pageDirect=Y";
      	   	boolean approval_level_val =checkApprovalStatus("18");
      	  	boolean status = updateCreatedAssetStatusBranch(asset_id,group_id,asset_id_new,assetCode);

      	  	if ((!approval_level_val)&&(status))
	      	  	{
      	  			//System.out.println(">>>>>>>>>>>> Inserting Into RaiseEntry <<<<<<<<<<");
      	  			String [] approvalResult=ad.setApprovalDataGroup(Long.parseLong(group_id));
      	  			approvalResult[10]="A";
      	  			String trans_id = adGroup.setGroupPendingTrans(approvalResult,"18",assetCode);

                                ad.setPendingTransArchive(approvalResult,"18",Integer.parseInt(trans_id),assetCode);

      	  			approvalRec.insertApprovalx2
	      	  		(group_id, approvalResult[5], page1, flag, partPay,Name, Branch, subjectT,
                                        whT, url,Integer.parseInt(trans_id),assetCode);
      	  			String qry = upd_am_asset_branch_status_RaiseEntry + group_id;
      	  			String qry2 = upd_am_grp_asset_branch_RaiseEntry + group_id;
      	  			String qry3 = upd_am_grp_asset_main_RaiseEntry +  group_id;
                                String qry4 = upd_am_asset_status_RaiseEntry_Archive + group_id;
      	  			String qry5 = upd_am_grp_asset_RaiseEntry_Archive + group_id;
      	  			String qry6 = upd_am_grp_asset_main_RaiseEntry_Archive +  group_id;
	      	  		/*System.out.println("qry : " + qry);
	  	  			System.out.println("qry2 : " + qry2 );
	  	  			System.out.println("qry3 : " + qry3);*/
	      	  		updateStatusUtil(qry);
	      	  		updateStatusUtil(qry2);
	      	  		updateStatusUtil(qry3);
                                updateStatusUtil(qry4);
	      	  		updateStatusUtil(qry5);
	      	  		updateStatusUtil(qry6);
	      	  	}

	      	if((status)&&(approval_level_val))
		      	{
		      	 System.out.println("====== Inserting into Approval ======");
		      	changeGroupAssetStatusBranch(group_id,"PENDING");
		      	 String trans_id = adGroup.setGroupPendingTrans(ad.setApprovalDataGroup(Long.parseLong(group_id)),"19",assetCode);
                         ad.setPendingTransArchive(ad.setApprovalDataGroup(Long.parseLong(group_id)),"18",Integer.parseInt(trans_id),assetCode);
		      	 //write a method to change status to pending
		      	}
        }
        catch (Exception ex)
        {
            done = false;
            System.out.println("WARN:Error creating asset->" + ex);
        }
        finally
        {
            dbConnection.closeConnection(con, ps);
        }

        return done;

    }
       public boolean RecordProcess(String GroupId,String catRate) {
           boolean done = false;
           Connection con = null;
           PreparedStatement ps = null;
           ResultSet rs = null;
           int rate = 100*12;
           int state = 1;
           int Dep_Rate = 0;   
           String memo = "N";  
           double RateValue =  0;
           if(catRate.equalsIgnoreCase("0.00")){catRate = "1";
           Dep_Rate = Integer.parseInt(catRate);
           RateValue = 0;
           }  
           else{
        	   double DepRate = Double.parseDouble(catRate);
        	   RateValue =(100/DepRate)*12;
           } 
            
           String FINDER_QUERY = "insert into ST_STOCK (Asset_id,Registration_No,Branch_ID,Dept_ID,Category_ID,Section_id,Description, Vendor_AC,Date_purchased,Dep_Rate,Asset_Make,Asset_Model,Asset_Serial_No,Asset_Engine_No,Supplier_Name,Asset_User," +
				"Asset_Maintenance,Accum_Dep,Cost_Price,Dep_End_Date,Residual_Value,Authorized_By,Wh_Tax," +
				"Wh_Tax_Amount,Req_Redistribution,Posting_Date,Effective_Date,Purchase_Reason,Location," +
				"Vatable_Cost,Vat,Req_Depreciation,Subject_TO_Vat,Who_TO_Rem,Email1,Who_To_Rem_2,Email2,Raise_Entry," +
				"Dep_Ytd,Section,Asset_Status,State,Driver,Spare_1,Spare_2,User_ID,PROVINCE,WAR_START_DATE," +
				"WAR_MONTH,WAR_EXPIRY_DATE,BRANCH_CODE,SECTION_CODE,DEPT_CODE,CATEGORY_CODE,AMOUNT_PTD,AMOUNT_REM," +
				"PART_PAY,FULLY_PAID,GROUP_ID,BAR_CODE,SBU_CODE,LPO,supervisor,defer_pay," +
				"mac_address,asset_code,NBV,remaining_life,memo,total_life,SUB_CATEGORY_ID,SUB_CATEGORY_CODE, SPARE_3, SPARE_4, SPARE_5, SPARE_6) "+
				"select Asset_id,Registration_No,Branch_ID,Dept_ID,Category_ID,Section_id,Description," +
				"Vendor_AC,Date_purchased,Dep_Rate,Asset_Make,Asset_Model,Asset_Serial_No,Asset_Engine_No,Supplier_Name,Asset_User," +
				"Asset_Maintenance,Accum_Dep,Cost_Price,Dep_End_Date,Residual_Value,Authorized_By,Wh_Tax," +
				"Wh_Tax_Amount,Req_Redistribution,Posting_Date,Effective_Date,Purchase_Reason,Location," +
				"Vatable_Cost,Vat,Req_Depreciation,Subject_TO_Vat,Who_TO_Rem,Email1,Who_To_Rem_2,Email2,Raise_Entry," +
				"Dep_Ytd,Section,'PENDING','"+state+"',Driver,Spare_1,Spare_2,User_ID,PROVINCE,WAR_START_DATE," +
				"WAR_MONTH,WAR_EXPIRY_DATE,BRANCH_CODE,SECTION_CODE,DEPT_CODE,CATEGORY_CODE,AMOUNT_PTD,AMOUNT_REM," +
				"PART_PAY,FULLY_PAID,GROUP_ID,BAR_CODE,SBU_CODE,LPO,supervisor,defer_pay," +
				"mac_address, Asset_code,Cost_Price,"+RateValue+",'"+memo+"',"+RateValue+",SUB_CATEGORY_ID,SUB_CATEGORY_CODE, SPARE_3, SPARE_4, SPARE_5, SPARE_6 " +
				"from AM_GROUP_ASSET where process_flag='N' and group_id = '"+GroupId+"'"; 
 //          System.out.println("RecordProcess GroupId === "+GroupId); 
 //          System.out.println("RecordProcess FINDER_QUERY === "+FINDER_QUERY);   
           try {
               con = dbConnection.getConnection("legendPlus");
               ps = con.prepareStatement(FINDER_QUERY);
 //              ps.setString(1, GroupId);
 //              rs = ps.executeQuery();                         
               done = (ps.executeUpdate() != -1);
            //   while (rs.next()) {
           //        done = true;
      //             System.out.println("After === "+done);
            //   }
 
           } catch (Exception ex) {
               System.out.println("WARNING: cannot Post upload records into  [Am_Asset]->"
                       + ex.getMessage());
           } finally {
        	     dbConnection.closeConnection(con, ps);
           }

           return done;

       }
       public boolean UploadRecordsSelection(String code,String AssetId,int AssetCode) {
    //	   System.out.println("Before Update Asset Id ====  "+AssetId);
    //	   System.out.println("Before Update Asset Code ====  "+AssetCode);
    	    Connection con = null;
    	    PreparedStatement ps = null;
    	    ResultSet rs = null;
    	    boolean done;
    	    done = false;
    	    String UpadteQuerry = "UPDATE am_asset set Asset_Code = '"+AssetCode+"' where ASSET_ID = '"+AssetId+"'";
    	    try {
    	        con = dbConnection.getConnection("legendPlus");
    	        ps = con.prepareStatement(UpadteQuerry);
    	        done = (ps.executeUpdate() != -1);
    	    //    	System.out.println("After UpdateAsset Id ====  "+AssetId);

    	    } catch (Exception ex) {
    	        System.out.println("WARN: Error fetching all asset ->" + ex);
    	    } finally {
    	        dbConnection.closeConnection(con, ps);
    	    }
    	    return done;
    	}
       public boolean RecordUncapitalizedProcess(String GroupId) {
           boolean done = false;
           Connection con = null;
           PreparedStatement ps = null;
           ResultSet rs = null;
           int rate = 100*12;
           int state = 1;
           String memo = "N";
           String FINDER_QUERY = "insert into AM_ASSET_UNCAPITALIZED (Asset_id,Registration_No,Branch_ID,Dept_ID,Category_ID,Section_id,Description, Vendor_AC,Date_purchased,Dep_Rate,Asset_Make,Asset_Model,Asset_Serial_No,Asset_Engine_No,Supplier_Name,Asset_User," +
				"Asset_Maintenance,Accum_Dep,Cost_Price,Dep_End_Date,Residual_Value,Authorized_By,Wh_Tax," +
				"Wh_Tax_Amount,Req_Redistribution,Posting_Date,Effective_Date,Purchase_Reason,Location," +
				"Vatable_Cost,Vat,Req_Depreciation,Subject_TO_Vat,Who_TO_Rem,Email1,Who_To_Rem_2,Email2,Raise_Entry," +
				"Dep_Ytd,Section,Asset_Status,State,Driver,Spare_1,Spare_2,User_ID,PROVINCE,WAR_START_DATE," +
				"WAR_MONTH,WAR_EXPIRY_DATE,BRANCH_CODE,SECTION_CODE,DEPT_CODE,CATEGORY_CODE,AMOUNT_PTD,AMOUNT_REM," +
				"PART_PAY,FULLY_PAID,GROUP_ID,BAR_CODE,SBU_CODE,LPO,supervisor,defer_pay," +
				"mac_address,asset_code,NBV,remaining_life,memo,total_life,SUB_CATEGORY_ID,SUB_CATEGORY_CODE, SPARE_3, SPARE_4, SPARE_5, SPARE_6) "+
				"select Asset_id,Registration_No,Branch_ID,Dept_ID,Category_ID,Section_id,Description," +
				"Vendor_AC,Date_purchased,Dep_Rate,Asset_Make,Asset_Model,Asset_Serial_No,Asset_Engine_No,Supplier_Name,Asset_User," +
				"Asset_Maintenance,Accum_Dep,Cost_Price,Dep_End_Date,Residual_Value,Authorized_By,Wh_Tax," +
				"Wh_Tax_Amount,Req_Redistribution,Posting_Date,Effective_Date,Purchase_Reason,Location," +
				"Vatable_Cost,Vat,Req_Depreciation,Subject_TO_Vat,Who_TO_Rem,Email1,Who_To_Rem_2,Email2,Raise_Entry," +
				"Dep_Ytd,Section,'PENDING','"+state+"',Driver,Spare_1,Spare_2,User_ID,PROVINCE,WAR_START_DATE," +
				"WAR_MONTH,WAR_EXPIRY_DATE,BRANCH_CODE,SECTION_CODE,DEPT_CODE,CATEGORY_CODE,AMOUNT_PTD,AMOUNT_REM," +
				"PART_PAY,FULLY_PAID,GROUP_ID,BAR_CODE,SBU_CODE,LPO,supervisor,defer_pay," +
				"mac_address, Asset_code,Cost_Price,0,'"+memo+"',0,SUB_CATEGORY_ID,SUB_CATEGORY_CODE, SPARE_3, SPARE_4, SPARE_5, SPARE_6 " +
				" from AM_GROUP_ASSET_UNCAPITALIZED where process_flag='N' and group_id = '"+GroupId+"'"; 
  //         System.out.println("RecordProcess GroupId in RecordUncapitalizedProcess === "+GroupId); 
        //   System.out.println("RecordProcess FINDER_QUERY === "+FINDER_QUERY);   
           try {
               con = dbConnection.getConnection("legendPlus");
               ps = con.prepareStatement(FINDER_QUERY);
 //              ps.setString(1, GroupId);
 //              rs = ps.executeQuery();                         
               done = (ps.executeUpdate() != -1);
            //   while (rs.next()) {
           //        done = true;
      //             System.out.println("After === "+done);
            //   }
 
           } catch (Exception ex) {
               System.out.println("WARNING: cannot Post upload records into  [AM_ASSET_UNCAPITALIZED]->"
                       + ex.getMessage());
           } finally {
        	     dbConnection.closeConnection(con, ps);
           }

           return done;

       }
       public boolean UploadUncapitalizedRecordSelection(String code,String AssetId,int AssetCode) {
    	    //	   System.out.println("Before Update Asset Id ====  "+AssetId);
    	    //	   System.out.println("Before Update Asset Code ====  "+AssetCode);
    	    	    Connection con = null;
    	    	    PreparedStatement ps = null;
    	    	    ResultSet rs = null;
    	    	    boolean done;
    	    	    done = false;
    	    	    String UpadteQuerry = "UPDATE AM_ASSET_UNCAPITALIZED set Asset_Code = '"+AssetCode+"' where ASSET_ID = '"+AssetId+"'";
    	    	    try {
    	    	        con = dbConnection.getConnection("legendPlus");
    	    	        ps = con.prepareStatement(UpadteQuerry);
    	    	        done = (ps.executeUpdate() != -1);
    	    	    //    	System.out.println("After UpdateAsset Id ====  "+AssetId);

    	    	    } catch (Exception ex) {
    	    	        System.out.println("WARN: Error fetching all asset ->" + ex);
    	    	    } finally {
    	    	        dbConnection.closeConnection(con, ps);
    	    	    }
    	    	    return done;
    	    	}     

public java.util.ArrayList retrieveStock(String grp_id)
 {
 	java.util.ArrayList list = new java.util.ArrayList();
   	String qry ="select Asset_id,Description,Subject_TO_Vat,wh_tax,Branch_ID,user_ID from " +
   			"st_stock where Group_id =" + grp_id;
   	System.out.println("retrieveGroupAsset_qry ::::: " + qry);
 	Connection c = null;
	ResultSet rs = null;
	Statement s = null;
	try {
		    c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(qry);
			while (rs.next())
			   {
				GroupInventoryToStockBean grpAsset = new GroupInventoryToStockBean();
   	    		grpAsset.setAsset_id_new(rs.getString(1));
   	    		System.out.println("<<<<<<rs.getString(1): "+rs.getString(1));
   	    		grpAsset.setDescription(rs.getString(2));
   	    		grpAsset.setSubject_to_vat(rs.getString(3));
   	    		grpAsset.setWh_tax_cb(rs.getString(4));
   	    		grpAsset.setBranch_id(rs.getString(5));
   	    		grpAsset.setUser_id(rs.getString(6));
   	    		list.add(grpAsset);
				
			   }
			}
				 catch (Exception e)
					{
						e.printStackTrace();
					}
					finally
					{
						dbConnection.closeConnection(c, s);
					}
				return list;
			 }

       public boolean retrieveStockRecords(String groupId,String compCode)
       {
     	  boolean result;
     	  result = false;
       	Connection con = null;
   	    PreparedStatement ps = null;
       	try
       	{ 
			ArrayList list = retrieveStock(groupId);
			System.out.println("Size ::::: " + list.size()+"      GroupId: "+groupId);
   	      //================================
   	     for(int i=0;i<list.size();i++)
   	     {
			GroupInventoryToStockBean grpBean = (GroupInventoryToStockBean)list.get(i);
			String asset_id_new = grpBean.getAsset_id_new();
			System.out.println("asset_id_new : " + asset_id_new);
			String desc = grpBean.getDescription();
			System.out.println("desc : " + desc);
			String userid = grpBean.getUser_id();
			System.out.println("userid : " + userid);
			String subject_T= grpBean.getSubject_to_vat();
			System.out.println("subject_T : " + subject_T);
			String wh_T= grpBean.getWh_tax_cb();
			System.out.println("wh_T : " + wh_T);
			String branchid = grpBean.getBranch_id();
			System.out.println("branchid : " + branchid);
			String full_Name =approvalRec.getCodeName(" SELECT full_name from am_gb_user where user_id="+userid+"");
			System.out.println("full_Name : " + full_Name);
			String branch_Name= approvalRec.getCodeName(" SELECT branch_name from am_ad_branch where branch_id='"+branchid+"'");
			System.out.println("branch_Name : " + branch_Name);
			tagapprove.groupStockApproval(compCode,groupId,asset_id_new);
			System.out.println("Record Count ::::: " +i+"     groupId: "+groupId+"      asset_id_new: "+asset_id_new);
   	     }   
   	     //======================================
   	    	
       	}
   	    catch(Exception ex) 
   	    {
   	        System.out.println("WARN: Error retrieveStockRecords ->" + ex);
   	    }
   	    finally 
   	    {
   	        dbConnection.closeConnection(con, ps);
   	    }  
       	return result;
       }

       
}