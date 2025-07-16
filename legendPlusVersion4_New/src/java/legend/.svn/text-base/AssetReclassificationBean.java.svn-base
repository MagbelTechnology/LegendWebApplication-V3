package legend;

import java.sql.ResultSet;

/**
 * <p>Title: Magma.net System</p>
 *
 * <p>Description: Fixed Assets Manager</p>
 *
 * <p>Copyright: Copyright (c) 2006. All rights reserved.</p>
 *
 * <p>Company: Magbel Technologies Limited.</p>
 *
 * @author Charles Ayoola Ayodele-Peters.
 * @version 1.0
 */
public class AssetReclassificationBean extends ConnectionClass {
    private String date_of_purchase = "";
    private String depreciation_start_date = "";
    private String depreciation_end_date = "";
    private String posting_date = "";
    private String make = "";
    private String subject_to_vat = "";
    private String accumulated_depreciation = "";
    private String asset_status = "";
    private String monthly_depreciation = "";
    private String maintained_by = "";
    private String authorized_by = "";
    private String supplied_by = "";
    private String asset_id = "";
    private String description = "";
    private String vendor_account = "";
    private String cost_price = "";
    private String vatable_cost = "";
    private String vat_amount = "";
    private String serial_number = "";
    private String engine_number = "";
    private String model = "";
    private String user = "";
    private String depreciation_rate = "";
    private String nbv = "";
    private String brought_forward = "";
    private String reason = "";
    private String scrap_value = "";
    private String new_category = "";
    private String reclassification_date = "";
    private String new_code = "";
    private String new_depreciation_rate = "";
    private String reclassification_reason = "";
    private String branch = "";
    private String department = "";
    private String category = "";
    private String registration_no = "";
    private String user_id = "1";
    private String raise_entry = "N";

    public AssetReclassificationBean() throws Exception {
        super();
    }

    /**
     * insertAssetReclassification
     *
     * @return boolean
     */
    public boolean insertAssetReclassification() throws Exception {
        StringBuffer b = new StringBuffer(500);
        b.append("am_msp_asset_reclassification ");
        b.append("'");
        b.append(getIdentity());
        b.append("','");
        b.append(asset_id);
        b.append("','");
        b.append(getCategoryId(category));
        b.append("','");
        b.append(new_category);
        b.append("',");
        b.append(getDepreciationRate(getCategoryId(category)));
        b.append(",");
        b.append(getDepreciationRate(new_category));
        b.append("','");
        b.append(reclassification_reason);
        b.append("','");
        b.append(user_id);
        b.append("','");
        b.append(raise_entry);
        b.append("'");

        int i = getStatement().executeUpdate(b.toString());
        return (i == -1);
    }

    /**
     * updateAssetReclassification
     *
     * @return boolean
     */
    public boolean updateAssetReclassification() {
        return false;
    }

    /**
     * getAssetReclassifications
     */
    public void getAssetReclassifications() throws Exception {
        if (asset_id != "") {
            ResultSet rs = getStatement().executeQuery(
                    "am_msp_select_asset_details '" + asset_id + "'");

            if (rs.next()) {
                date_of_purchase = rs.getString(8);
                depreciation_start_date = rs.getString(25);
                depreciation_end_date = rs.getString(21);
                posting_date = rs.getString(24);
                make = rs.getString(10);
                subject_to_vat = rs.getString(34);
                accumulated_depreciation = rs.getString(17);
                monthly_depreciation = rs.getString(18);
                maintained_by = rs.getString(16);
                authorized_by = rs.getString(23);
                supplied_by = rs.getString(14);
                asset_id = rs.getString(1);
                description = rs.getString(6);
                vendor_account = rs.getString(7);
                cost_price = rs.getString(19);
                vatable_cost = rs.getString(31);
                vat_amount = rs.getString(32);
                serial_number = rs.getString(12);
                engine_number = rs.getString(13);
                model = rs.getString(11);
                user = rs.getString(15);
                depreciation_rate = rs.getString(9);
                nbv = rs.getString(20);
                brought_forward = rs.getString(39);
                scrap_value = rs.getString(22);
                branch = rs.getString(3);
                department = rs.getString(4);
                category = rs.getString(5);
                registration_no = rs.getString(2);
            }
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

    public void setPosting_date(String posting_date) {
        if (posting_date != null) {
            this.posting_date = posting_date;
        }
    }

    public void setMake(String make) {
        if (make != null) {
            this.make = make;
        }
    }

    public void setSubject_to_vat(String subject_to_vat) {
        if (subject_to_vat != null) {
            this.subject_to_vat = subject_to_vat;
        }
    }

    public void setAccumulated_depreciation(String accumulated_depreciation) {
        if (accumulated_depreciation != null) {
            this.accumulated_depreciation = accumulated_depreciation;
        }
    }

    public void setAsset_status(String asset_status) {
        if (asset_status != null) {
            this.asset_status = asset_status;
        }
    }

    public void setMonthly_depreciation(String monthly_depreciation) {
        if (monthly_depreciation != null) {
            this.monthly_depreciation = monthly_depreciation;
        }
    }

    public void setMaintained_by(String maintained_by) {
        if (maintained_by != null) {
            this.maintained_by = maintained_by;
        }
    }

    public void setAuthorized_by(String authorized_by) {
        if (authorized_by != null) {
            this.authorized_by = authorized_by;
        }
    }

    public void setSupplied_by(String supplied_by) {
        if (supplied_by != null) {
            this.supplied_by = supplied_by;
        }
    }

    public void setAsset_id(String asset_id) {
        if (asset_id != null) {
            this.asset_id = asset_id;
        }
    }

    public void setDescription(String description) {
        if (description != null) {
            this.description = description;
        }
    }

    public void setVendor_account(String vendor_account) {
        if (vendor_account != null) {
            this.vendor_account = vendor_account;
        }
    }

    public void setCost_price(String cost_price) {
        if (cost_price != null) {
            this.cost_price = cost_price;
        }
    }

    public void setVatable_cost(String vatable_cost) {
        if (vatable_cost != null) {
            this.vatable_cost = vatable_cost;
        }
    }

    public void setVat_amount(String vat_amount) {
        if (vat_amount != null) {
            this.vat_amount = vat_amount;
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
            this.depreciation_rate = depreciation_rate;
        }
    }

    public void setNbv(String nbv) {
        if (nbv != null) {
            this.nbv = nbv;
        }
    }

    public void setBrought_forward(String brought_forward) {
        if (brought_forward != null) {
            this.brought_forward = brought_forward;
        }
    }

    public void setReason(String reason) {
        if (reason != null) {
            this.reason = reason;
        }
    }

    public void setScrap_value(String scrap_value) {
        if (scrap_value != null) {
            this.scrap_value = scrap_value;
        }
    }

    public void setNew_category(String new_category) {
        if (new_category != null) {
            this.new_category = new_category;
        }
    }

    public void setReclassification_date(String reclassification_date) {
        if (reclassification_date != null) {
            this.reclassification_date = reclassification_date;
        }
    }

    public void setNew_code(String new_code) {
        if (new_code != null) {
            this.new_code = new_code;
        }
    }

    public void setNew_depreciation_rate(String new_depreciation_rate) {
        if (new_depreciation_rate != null) {
            this.new_depreciation_rate = new_depreciation_rate;
        }
    }

    public void setReclassification_reason(String reclassification_reason) {
        if (reclassification_reason != null) {
            this.reclassification_reason = reclassification_reason;
        }
    }

    public void setBranch(String branch) {
        if (branch != null) {
            this.branch = branch;
        }
    }

    public void setDepartment(String department) {
        if (department != null) {
            this.department = department;
        }
    }

    public void setCategory(String category) {
        if (category != null) {
            this.category = category;
        }
    }

    public void setRegistration_no(String registration_no) {
        if (registration_no != null) {
            this.registration_no = registration_no;
        }
    }

    public void setUser_id(String user_id) {
        if (user_id != null) {
            this.user_id = user_id;
        }
    }

    public void setRaise_entry(String raise_entry) {
        if (raise_entry != null) {
            this.raise_entry = raise_entry;
        }
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

    public String getPosting_date() {
        return posting_date;
    }

    public String getMake() {
        return make;
    }

    public String getSubject_to_vat() {
        return subject_to_vat;
    }

    public String getAccumulated_depreciation() {
        return accumulated_depreciation;
    }

    public String getAsset_status() {
        return asset_status;
    }

    public String getMonthly_depreciation() {
        return monthly_depreciation;
    }

    public String getMaintained_by() {
        return maintained_by;
    }

    public String getAuthorized_by() {
        return authorized_by;
    }

    public String getSupplied_by() {
        return supplied_by;
    }

    public String getAsset_id() {
        return asset_id;
    }

    public String getDescription() {
        return description;
    }

    public String getVendor_account() {
        return vendor_account;
    }

    public String getCost_price() {
        return cost_price;
    }

    public String getVatable_cost() {
        return vatable_cost;
    }

    public String getVat_amount() {
        return vat_amount;
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

    public String getNbv() {
        return nbv;
    }

    public String getBrought_forward() {
        return brought_forward;
    }

    public String getReason() {
        return reason;
    }

    public String getScrap_value() {
        return scrap_value;
    }

    public String getNew_category() {
        return new_category;
    }

    public String getReclassification_date() {
        return reclassification_date;
    }

    public String getNew_code() {
        return new_code;
    }

    public String getNew_depreciation_rate() {
        return new_depreciation_rate;
    }

    public String getReclassification_reason() {
        return reclassification_reason;
    }

    public String getBranch() {
        return branch;
    }

    public String getDepartment() {
        return department;
    }

    public String getCategory() {
        return category;
    }

    public String getRegistration_no() {
        return registration_no;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getRaise_entry() {
        return raise_entry;
    }

    /**
     * getCategoryId
     *
     * @param categoryName String
     * @return String
     */
    public String getCategoryId(String categoryName) throws Exception {
        ResultSet rs = getStatement().executeQuery("am_msp_get_category_id '" +
                categoryName + "'");
        rs.next();
        return rs.getString(1);
    }

    /**
     * getDepreciationRate
     *
     * @param categoryId String
     * @return String
     */
    public String getDepreciationRate(String categoryId) throws Exception {
        ResultSet rs = getStatement().executeQuery(
                "am_msp_get_depreciation_rate '" + categoryId + "'");
        rs.next();
        return rs.getString(1);
    }
}
