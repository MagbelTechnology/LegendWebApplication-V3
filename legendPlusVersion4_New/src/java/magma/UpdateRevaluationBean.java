package magma;

import java.sql.ResultSet;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class UpdateRevaluationBean extends legend.ConnectionClass {
    private String asset_id = "";
    private String cost_increase = "";
    private String revalue_reason = "";
    private Calendar revalue_date = new GregorianCalendar();
    private String raise_entry = "";
    private String r_vendor_ac = "";
    private String branch_name = "";
    private String category_name = "";
    private String dept_name = "";
    private String description = "";
    private String registration_no = "";
    private Calendar date_purchased = new GregorianCalendar();
    private Calendar effective_date = new GregorianCalendar();
    private Calendar dep_end_date = new GregorianCalendar();
    private String cost_price = "";
    private String vatable_cost = "";
    private String dep_rate = "";
    private String monthly_dep = "";
    private String accum_dep = "";
    private String nbv = "";

    public UpdateRevaluationBean() throws Exception {
        super();
    }

    /**
     * getRevaluationDetail
     *
     * @param s String
     */
    public void getRevaluationDetail(String s) throws Exception {
        ResultSet rs = getStatement().executeQuery(
                "am_msp_select_asset_revaluation_detail " + s);
        if (rs.next()) {
            asset_id = rs.getString(2);
            cost_increase = rs.getString(3);
            revalue_reason = rs.getString(4);
            revalue_date.setTime(rs.getDate(5));
            raise_entry = rs.getString(6);
            r_vendor_ac = rs.getString(7);
            branch_name = rs.getString(8);
            category_name = rs.getString(9);
            dept_name = rs.getString(10);
            description = rs.getString(11);
            registration_no = rs.getString(12);
            date_purchased.setTime(rs.getDate(13));
            effective_date.setTime(rs.getTime(14));
            dep_end_date.setTime(rs.getDate(15));
            cost_price = rs.getString(16);
            vatable_cost = rs.getString(17);
            dep_rate = rs.getString(18);
            monthly_dep = rs.getString(19);
            accum_dep = rs.getString(20);
            nbv = rs.getString(21);
        }
    }

    public String getAsset_id() {
        return asset_id;
    }

    public String getCost_increase() {
        return cost_increase;
    }

    public String getRevalue_reason() {
        return revalue_reason;
    }

    public String getRevalue_date() {
        return DateManipulations.CalendarToDate(revalue_date);
    }

    public String getRaise_entry() {
        return raise_entry;
    }

    public String getR_vendor_ac() {
        return r_vendor_ac;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public String getCategory_name() {
        return category_name;
    }

    public String getDept_name() {
        return dept_name;
    }

    public String getDescription() {
        return description;
    }

    public String getRegistration_no() {
        return registration_no;
    }

    public String getDate_purchased() {
        return DateManipulations.CalendarToDate(date_purchased);
    }

    public String getEffective_date() {
        return DateManipulations.CalendarToDate(effective_date);
    }

    public String getDep_end_date() {
        return DateManipulations.CalendarToDate(dep_end_date);
    }

    public String getCost_price() {
        return cost_price;
    }

    public String getVatable_cost() {
        return vatable_cost;
    }

    public String getDep_rate() {
        return dep_rate;
    }

    public String getMonthly_dep() {
        return monthly_dep;
    }

    public String getAccum_dep() {
        return accum_dep;
    }

    public String getNbv() {
        return nbv;
    }
}
