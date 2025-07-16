package magma;

import java.sql.ResultSet;
import java.util.Calendar;
import java.util.GregorianCalendar;

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
public class AssetDisposalBean extends legend.ConnectionClass {
    private String asset_id = "";
    private String category = "0";
    private String registration_no = "";
    private String start_date = "";
    private String authorized_by = "";
    private String date_of_disposal = "";
    private String location = "";
    private String branch = "0";
    private String date_of_purchase = "";
    private String buyer_account = "";
    private String accumulated_depreciation = "";
    private String asset_account = "";
    private String cost_price = "0";
    private String profit_loss = "0";
    private String nbv = "0";
    private String disposal_reason = "";
    private String disposal_value = "";
    private String depreciation_rate = "";
    private String raise_entry = "N";
    private String user_id = "1";
    private Calendar effective_date = new GregorianCalendar();
    private String date_disposed = new java.text.SimpleDateFormat("dd/MM/yyyy").
                                   format(new java.util.Date());

    public AssetDisposalBean() throws Exception {
        super();
    }

    /**
     * insertAssetDisposal
     *
     * @return boolean
     */
    public boolean insertAssetDisposal() throws Exception {
        StringBuffer b = new StringBuffer(500);
        b.append("am_msp_asset_disposal ");
        b.append("'");
        b.append(asset_id);
        b.append("',");
        b.append(disposal_reason);
        b.append(",'");
        b.append(buyer_account);
        b.append("',");
        b.append(disposal_value);
        b.append(",'");
        b.append(raise_entry);
        b.append("',");
        b.append(profit_loss);
        b.append(",'");
        b.append(DateManipulations.CalendarToDb(this.effective_date));
        b.append("',");
        b.append(user_id);

        int i = getStatement().executeUpdate(b.toString());
        return (i == -1);
    }

    /**
     * updateAssetDisposal
     *
     * @return boolean
     */
    public boolean updateAssetDisposal() {
        return false;
    }

    /**
     * getAssetDisposals
     */
    public void getAssetDisposals() throws Exception {
        if (asset_id != "") {
            ResultSet rs = getStatement().executeQuery(
                    "am_msp_select_asset_disposal '" + asset_id + "'");

            if (rs.next()) {
                category = rs.getString(5);
                registration_no = rs.getString(2);
                start_date = rs.getString(25);
                authorized_by = rs.getString(23);
                location = rs.getString(29);
                branch = rs.getString(3);
                date_of_purchase = rs.getString(8);
                accumulated_depreciation = rs.getString(17);
                asset_account = rs.getString(42);
                cost_price = rs.getString(19);
                nbv = rs.getString(20);
                depreciation_rate = rs.getString(9);

                setProfit_loss(rs.getString(50));
                setRaise_entry(rs.getString(49));
                date_disposed = new java.text.SimpleDateFormat("dd/MM/yyyy").
                                format(new java.util.Date());
            }
        }
    }

    public void setAsset_id(String asset_id) {
        if (asset_id != null) {
            this.asset_id = asset_id;
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

    public void setStart_date(String start_date) {
        if (start_date != null) {
            this.start_date = start_date;
        }
    }

    public void setAuthorized_by(String authorized_by) {
        if (authorized_by != null) {
            this.authorized_by = authorized_by;
        }
    }

    public void setDate_of_disposal(String date_of_disposal) {
        if (date_of_disposal != null) {
            this.date_of_disposal = date_of_disposal;
        }
    }

    public void setLocation(String location) {
        if (location != null) {
            this.location = location;
        }
    }

    public void setBranch(String branch) {
        if (branch != null) {
            this.branch = branch;
        }
    }

    public void setDate_of_purchase(String date_of_purchase) {
        if (date_of_purchase != null) {
            this.date_of_purchase = date_of_purchase;
        }
    }

    public void setBuyer_account(String buyer_account) {
        if (buyer_account != null) {
            this.buyer_account = buyer_account;
        }
    }

    public void setAccumulated_depreciation(String accumulated_depreciation) {
        if (accumulated_depreciation != null) {
            this.accumulated_depreciation = accumulated_depreciation;
        }
    }

    public void setAsset_account(String asset_account) {
        if (asset_account != null) {
            this.asset_account = asset_account;
        }
    }

    public void setCost_price(String cost_price) {
        if (cost_price != null) {
            this.cost_price = cost_price;
        }
    }

    public void setProfit_loss(String profit_loss) {
        if (profit_loss != null) {
            this.profit_loss = profit_loss;
        }
    }

    public void setNbv(String nbv) {
        if (nbv != null) {
            this.nbv = nbv;
        }
    }

    public void setDisposal_reason(String disposal_reason) {
        if (disposal_reason != null) {
            this.disposal_reason = disposal_reason;
        }
    }

    public void setDisposal_value(String disposal_value) {
        if (disposal_value != null) {
            this.disposal_value = disposal_value.replaceAll(",", "");
            this.profit_loss = Float.toString(Float.parseFloat(this.
                    disposal_value) - Float.parseFloat(this.nbv));
        }
    }

    public void setDepreciation_rate(String depreciation_rate) {
        if (depreciation_rate != null) {
            this.depreciation_rate = depreciation_rate;
        }
    }

    public void setRaise_entry(String raise_entry) {
        if (raise_entry != null) {
            this.raise_entry = raise_entry;
        }
    }

    public void setUser_id(String user_id) {
        if (user_id != null) {
            this.user_id = user_id;
        }
    }

    public void setEffective_date(String effective_date) {
        if (effective_date != null) {
            this.effective_date = DateManipulations.DateToCalendar(
                    effective_date);
        }
    }

    public void setDate_disposed(String date_disposed) {
        //



    }

    public String getAsset_id() {
        return asset_id;
    }

    public String getCategory() {
        return category;
    }

    public String getRegistration_no() {
        return registration_no;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getAuthorized_by() {
        return authorized_by;
    }

    public String getDate_of_disposal() {
        return date_of_disposal;
    }

    public String getLocation() {
        return location;
    }

    public String getBranch() {
        return branch;
    }

    public String getDate_of_purchase() {
        return date_of_purchase;
    }

    public String getBuyer_account() {
        return buyer_account;
    }

    public String getAccumulated_depreciation() {
        return accumulated_depreciation;
    }

    public String getAsset_account() {
        return asset_account;
    }

    public String getCost_price() {
        return cost_price;
    }

    public String getProfit_loss() {
        return profit_loss;
    }

    public String getNbv() {
        return nbv;
    }

    public String getDisposal_reason() {
        return disposal_reason;
    }

    public String getDisposal_value() {
        return disposal_value;
    }

    public String getDepreciation_rate() {
        return depreciation_rate;
    }

    public String getRaise_entry() {
        return raise_entry;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getEffective_date() {
        return DateManipulations.CalendarToDate(this.effective_date);
    }

    public String getDate_disposed() {
        return date_disposed;
    }
}
