package magma;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class UpdateDisposalBean extends legend.ConnectionClass {
    private String asset_id = "";
    private String disposal_id = "";
    private String disposal_reason = "";
    private String buyer_account = "";
    private String disposal_amount = "";
    private String raise_entry = "";
    private String profit_loss = "";
    private Calendar disposal_date = new GregorianCalendar();
    private Calendar effective_date = new GregorianCalendar();
    private String category_name = "";
    private String branch_name = "";
    private String dept_name = "";
    private Calendar date_purchased = new GregorianCalendar();
    private Calendar depr_start_date = new GregorianCalendar();
    private String cost_price = "";
    private String nbv = "";
    private String accum_dep = "";
    private String asset_ledger = "";
    private String dep_rate = "";

    public UpdateDisposalBean() throws Exception {
        super();
    }

    public String getAsset_id() {
        return asset_id;
    }

    public String getDisposal_reason() {
        return disposal_reason;
    }

    public String getBuyer_account() {
        return buyer_account;
    }

    public String getDisposal_amount() {
        return disposal_amount;
    }

    public String getRaise_entry() {
        return raise_entry;
    }

    public String getProfit_loss() {
        return profit_loss;
    }

    public String getDisposal_date() {
        return DateManipulations.CalendarToDate(disposal_date);
    }

    public String getEffective_date() {
        return DateManipulations.CalendarToDate(effective_date);
    }

    public String getCategory_name() {
        return category_name;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public String getDept_name() {
        return dept_name;
    }

    public String getDate_purchased() {
        return DateManipulations.CalendarToDate(date_purchased);
    }

    public String getDepr_start_date() {
        return DateManipulations.CalendarToDate(depr_start_date);
    }

    public String getCost_price() {
        return cost_price;
    }

    public String getNbv() {
        return nbv;
    }

    public String getAccum_dep() {
        return accum_dep;
    }

    public String getAsset_ledger() {
        return asset_ledger;
    }

    public String getDep_rate() {
        return dep_rate;
    }

    public void setDisposal_id(String disposal_id) {
        this.disposal_id = disposal_id;
    }

    public void setDisposal_reason(String disposal_reason) {
        this.disposal_reason = disposal_reason;
    }

    public void setBuyer_account(String buyer_account) {
        this.buyer_account = buyer_account;
    }

    public void setDisposal_amount(String disposal_amount) {
        this.disposal_amount = disposal_amount;
    }

    public void setRaise_entry(String raise_entry) {
        this.raise_entry = raise_entry;
    }

    public void setProfit_loss(String profit_loss) {
        this.profit_loss = profit_loss;
    }

    public void setDisposal_date(String disposal_date) {
        if (disposal_date != null) {
            this.disposal_date = DateManipulations.DateToCalendar(disposal_date);
        }
    }

    public void setEffective_date(String effective_date) {
        if (effective_date != null) {
            this.effective_date = DateManipulations.DateToCalendar(
                    effective_date);
        }
    }

    /**
     * getDisposedAsset
     *
     * @param s String
     */
    public void getDisposedAsset(String s) throws Exception {
        java.sql.ResultSet rs = getStatement().executeQuery(
                "am_msp_select_asset_disposal_detail " + s);
        if (rs.next()) {
            disposal_id = rs.getString(1);
            disposal_reason = rs.getString(2);
            buyer_account = rs.getString(3);
            disposal_amount = rs.getString(4);
            raise_entry = rs.getString(5);
            profit_loss = rs.getString(6);
            disposal_date.setTime(rs.getDate(7));
            effective_date.setTime(rs.getDate(8));
            asset_id = rs.getString(9);
            category_name = rs.getString(10);
            branch_name = rs.getString(11);
            dept_name = rs.getString(12);
            date_purchased.setTime(rs.getDate(13));
            effective_date.setTime(rs.getDate(14));
            cost_price = rs.getString(15);
            nbv = rs.getString(16);
            accum_dep = rs.getString(17);
            asset_ledger = rs.getString(18);
            dep_rate = rs.getString(19);
        }
    }
}
