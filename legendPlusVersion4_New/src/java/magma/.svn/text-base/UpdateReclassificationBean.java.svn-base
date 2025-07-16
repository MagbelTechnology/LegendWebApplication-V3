package magma;

import java.sql.ResultSet;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class UpdateReclassificationBean extends legend.ConnectionClass {
    private String asset_id = "";
    private String description = "";
    private String registration_no = "";
    private String department = "";
    private String branch = "";
    private String vendor_ac = "";
    private Calendar depr_start_date = new GregorianCalendar();
    private Calendar purchase_date = new GregorianCalendar();
    private Calendar depr_end_date = new GregorianCalendar();
    private String cost_price = "";
    private String vatable_cost = "";
    private String subject_to_vat = "";
    private String vat_amount = "";
    private Calendar posting_date = new GregorianCalendar();
    private String monthly_depreciation = "";
    private String accum_depr = "";
    private String make = "";
    private String model = "";
    private String engine_number = "";
    private String serial_number = "";
    private String category = "";
    private String depreciation_rate = "";
    private String category_id = "";
    private String dep_rate = "";
    private Calendar reclass_date = new GregorianCalendar();
    private String reason_id = "";
    private String recalculate = "";
    private String raise_entry = "";

    public UpdateReclassificationBean() throws Exception {
        super();
    }

    /**
     * getReclassificationDetail
     *
     * @param s String
     */
    public void getReclassificationDetail(String s) throws Exception {
        ResultSet rs = getStatement().executeQuery(
                "am_msp_select_asset_reclassification_detail " + s);
        if (rs.next()) {
            asset_id = rs.getString(2);
            category = rs.getString(3);
            category_id = rs.getString(4);
            depreciation_rate = rs.getString(5);
            dep_rate = rs.getString(6);
            accum_depr = rs.getString(7);
            reclass_date.setTime(rs.getDate(8));
            reason_id = rs.getString(9);
            recalculate = rs.getString(10);
            raise_entry = rs.getString(11);
            registration_no = rs.getString(12);
            description = rs.getString(13);
            branch = rs.getString(14);
            department = rs.getString(15);
            vendor_ac = rs.getString(16);
            purchase_date.setTime(rs.getDate(17));
            depr_start_date.setTime(rs.getDate(18));
            depr_end_date.setTime(rs.getDate(19));
            cost_price = rs.getString(20);
            vatable_cost = rs.getString(21);
            vat_amount = rs.getString(22);
            subject_to_vat = rs.getString(23);
            posting_date.setTime(rs.getDate(24));
            monthly_depreciation = rs.getString(25);
            make = rs.getString(27);
            model = rs.getString(28);
            serial_number = rs.getString(29);
            engine_number = rs.getString(30);
        }
    }

    public String getAsset_id() {
        return asset_id;
    }

    public String getDescription() {
        return description;
    }

    public String getRegistration_no() {
        return registration_no;
    }

    public String getDepartment() {
        return department;
    }

    public String getBranch() {
        return branch;
    }

    public String getVendor_ac() {
        return vendor_ac;
    }

    public String getDepr_start_date() {
        return DateManipulations.CalendarToDate(depr_start_date);
    }

    public String getPurchase_date() {
        return DateManipulations.CalendarToDate(purchase_date);
    }

    public String getDepr_end_date() {
        return DateManipulations.CalendarToDate(depr_end_date);
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

    public String getPosting_date() {
        return DateManipulations.CalendarToDate(posting_date);
    }

    public String getMonthly_depreciation() {
        return monthly_depreciation;
    }

    public String getAccum_depr() {
        return accum_depr;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getEngine_number() {
        return engine_number;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public String getCategory() {
        return category;
    }

    public String getDepreciation_rate() {
        return depreciation_rate;
    }

    public String getCategory_id() {
        return category_id;
    }

    public String getDep_rate() {
        return dep_rate;
    }

    public String getReclass_date() {
        return DateManipulations.CalendarToDate(reclass_date);
    }

    public String getReason_id() {
        return reason_id;
    }

    public String getRecalculate() {
        return recalculate;
    }

    public String getRaise_entry() {
        return raise_entry;
    }
}
