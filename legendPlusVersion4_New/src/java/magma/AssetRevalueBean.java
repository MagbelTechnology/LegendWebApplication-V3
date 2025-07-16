package magma;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;

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
public class AssetRevalueBean extends legend.ConnectionClass {
    private String date_of_purchase = "";
    private String start_date = "";
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
    private String cost_increse = "0";
    private String asset_id = "";
    private String description = "";
    private String vendor_account = "";
    private String new_vendor_account = "";
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
    private String reason_for_revaluation = "";
    private String branch = "";
    private String department = "";
    private String category = "";
    private String registration_no = "";
    private String user_id = "1";
    private String raise_entry = "N";
    private String date_revalued = "";

    public AssetRevalueBean() throws Exception {
        super();
    }

    /**
     * insertAssetRevaluation
     *
     * @return boolean
     */
    public boolean insertAssetRevaluation() throws Exception {
        StringBuffer b = new StringBuffer(600);
        b.append("am_msp_asset_revaluation ");
        b.append("'");
        b.append(asset_id);
        b.append("',");
        b.append(cost_increse);
        b.append(",'");
        b.append(reason_for_revaluation);
        b.append("',");
        b.append(user_id);

        int i = getStatement().executeUpdate(b.toString());
        return (i == -1);
    }  

    /**
     * updateAssetRevaluation
     *
     * @return boolean
     */
    public boolean updateAssetRevaluation() {
        return false;
    }

    /**
     * getAssetRevaluations
     *
     */
    public void getAssetRevaluations() throws Exception {
        if (asset_id != "") {
            ResultSet rs = getStatement().executeQuery(
                    "am_msp_select_asset_details '" + asset_id + "'");

            if (rs.next()) {
                date_of_purchase = rs.getString(8);
                start_date = rs.getString(25);
                depreciation_end_date = rs.getString(21);
                posting_date = rs.getString(24);
                make = rs.getString(10);
                subject_to_vat = rs.getString(34);
                accumulated_depreciation = rs.getString(17);
                asset_status = "";
                monthly_depreciation = rs.getString(18);
                maintained_by = rs.getString(16);
                authorized_by = rs.getString(23);
                supplied_by = rs.getString(14);
                //setCost_increse(rs.getString(44));
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
                brought_forward = "";
                reason = rs.getString(26);
                scrap_value = rs.getString(22);
                reason_for_revaluation = "";
                branch = rs.getString(3);
                department = rs.getString(4);
                category = rs.getString(5);
                registration_no = rs.getString(2);
                //setDate_revalued(rs.getString(43));
                date_revalued = new SimpleDateFormat("dd/MM/yyyy").format(new
                        java.util.Date());
            }
        }

    }

    public void setDate_of_purchase(String date_of_purchase) {
        if (date_of_purchase != null) {
            this.date_of_purchase = date_of_purchase;
        }
    }

    public void setStart_date(String start_date) {
        if (start_date != null) {
            this.start_date = start_date;
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

    public void setCost_increse(String cost_increse) {
        if (cost_increse != null) {
            this.cost_increse = cost_increse.replaceAll(",", "");
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

    public void setReason_for_revaluation(String reason_for_revaluation) {
        if (reason_for_revaluation != null) {
            this.reason_for_revaluation = reason_for_revaluation;
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

    public void setDate_revalued(String date_revalued) {
        if (date_revalued != null) {
            this.date_revalued = date_revalued;
        }
    }

    public void setNew_vendor_account(String new_vendor_account) {
        if (new_vendor_account != null) {
            this.new_vendor_account = new_vendor_account;
        }
    }

    public String getDate_of_purchase() {
        return date_of_purchase;
    }

    public String getStart_date() {
        return start_date;
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

    public String getCost_increse() {
        return cost_increse;
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

    public String getReason_for_revaluation() {
        return reason_for_revaluation;
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

    public String getDate_revalued() {
        return date_revalued;
    }

    public String getNew_vendor_account() {
        return new_vendor_account;
    }

    public String getAssetLedger() throws Exception {
        java.sql.ResultSet rs = getStatement().executeQuery("select asset_ledger from am_asset inner join am_ad_category on am_asset.category_id = am_ad_category.category_id where asset_id='" +
                asset_id + "'");
        rs.next();
        return rs.getString(1);
    }
}
