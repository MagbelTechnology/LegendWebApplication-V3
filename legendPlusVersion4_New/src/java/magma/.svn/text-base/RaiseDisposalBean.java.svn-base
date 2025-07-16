package magma;

import java.sql.ResultSet;

public class RaiseDisposalBean extends legend.ConnectionClass {
    private String asset_id = "";
    private String cost_disposal_account = "";
    private String cost_asset_account = "";
    private String cost_amount = "";
    private String cost_cr_narration = "";
    private String cost_dr_narration = "";
    private String dep_acct = "";
    private String dep_disposal_acct = "";
    private String dep_amount = "";
    private String dep_cr_narration = "";
    private String dep_dr_narration = "";
    private String sale_purchase_acct = "";
    private String sale_disposal_acct = "";
    private String sale_amount = "";
    private String sale_cr_narration = "";
    private String sale_dr_narration = "";
    private String profit_cr_account = "";
    private String profit_dr_account = "";
    private String profit_amount = "";
    private String profit_cr_narration = "";
    private String profit_dr_narration = "";
    private String entry_date = new java.text.SimpleDateFormat("dd/MM/yyyy").
                                format(new java.util.Date());

    public RaiseDisposalBean() throws Exception {
        super();
    }

    /**
     * raiseEntry
     *
     * @return boolean
     */
    public boolean raiseEntry() throws Exception {
        //generate batch id
        String batch_id = getIdentity();

        //cost entries
        if (Float.parseFloat(cost_amount) > 0) {
            StringBuffer sb = new StringBuffer(300);
            sb.append("am_msp_insert_entry_table '");
            sb.append(cost_disposal_account);
            sb.append("','");
            sb.append(cost_asset_account);
            sb.append("','");
            sb.append(cost_dr_narration);
            sb.append("','");
            sb.append(cost_cr_narration);
            sb.append("',");
            sb.append(cost_amount);
            sb.append(",'");
            //user
            sb.append("','");
            //supervisor
            sb.append("','");
            //legacy id
            sb.append("','");
            //transaction code
            sb.append("','");
            sb.append(correctDate(entry_date));
            sb.append("','P','N','','");
            sb.append(batch_id);
            sb.append("'");

            int i = getStatement().executeUpdate(sb.toString());

        }

        //depreciation entries
        if (Float.parseFloat(dep_amount) != 0) {
            StringBuffer sb = new StringBuffer(300);
            sb.append("am_msp_insert_entry_table '");
            sb.append(dep_acct);
            sb.append("','");
            sb.append(dep_disposal_acct);
            sb.append("','");
            sb.append(dep_dr_narration);
            sb.append("','");
            sb.append(dep_cr_narration);
            sb.append("',");
            sb.append(dep_amount);
            sb.append(",'");
            //user
            sb.append("','");
            //supervisor
            sb.append("','");
            //legacy id
            sb.append("','");
            //transaction code
            sb.append("','");
            sb.append(correctDate(entry_date));
            sb.append("','P','N','','");
            sb.append(batch_id);
            sb.append("'");

            int i = getStatement().executeUpdate(sb.toString());

        }

        //proceed of sale entries
        if (Float.parseFloat(sale_amount) != 0) {
            StringBuffer sb = new StringBuffer(300);
            sb.append("am_msp_insert_entry_table '");
            sb.append(sale_purchase_acct);
            sb.append("','");
            sb.append(sale_disposal_acct);
            sb.append("','");
            sb.append(sale_dr_narration);
            sb.append("','");
            sb.append(sale_cr_narration);
            sb.append("',");
            sb.append(sale_amount);
            sb.append(",'");
            //user
            sb.append("','");
            //supervisor
            sb.append("','");
            //legacy id
            sb.append("','");
            //transaction code
            sb.append("','");
            sb.append(correctDate(entry_date));
            sb.append("','P','N','','");
            sb.append(batch_id);
            sb.append("'");

            int i = getStatement().executeUpdate(sb.toString());

        }

        //profit/loss entries
        if (Float.parseFloat(profit_amount) != 0) {
            StringBuffer sb = new StringBuffer(300);
            sb.append("am_msp_insert_entry_table '");
            sb.append(profit_dr_account);
            sb.append("','");
            sb.append(profit_cr_account);
            sb.append("','");
            sb.append(profit_dr_narration);
            sb.append("','");
            sb.append(profit_cr_narration);
            sb.append("',");
            sb.append(profit_amount);
            sb.append(",'");
            //user
            sb.append("','");
            //supervisor
            sb.append("','");
            //legacy id
            sb.append("','");
            //transaction code
            sb.append("','");
            sb.append(correctDate(entry_date));
            sb.append("','P','N','','");
            sb.append(batch_id);
            sb.append("'");

            int i = getStatement().executeUpdate(sb.toString());

        }

        return true;
    }

    /**
     * getAssetDetail
     */
    public void getAssetDetail() throws Exception {
        if (asset_id != "") {
            StringBuffer sb = new StringBuffer(100);
            sb.append("am_msp_select_raise_disposal_details '");
            sb.append(asset_id);
            sb.append("'");

            ResultSet rs = getStatement().executeQuery(sb.toString());
            if (rs.next()) {
                cost_disposal_account = rs.getString(12);
                cost_asset_account = rs.getString(13);
                cost_amount = rs.getString(9);
                cost_cr_narration = "";
                cost_dr_narration = "";
                dep_acct = rs.getString(8);
                dep_disposal_acct = rs.getString(12);
                dep_amount = rs.getString(10);
                dep_cr_narration = "";
                dep_dr_narration = "";
                sale_purchase_acct = rs.getString(3);
                sale_disposal_acct = rs.getString(12);
                sale_amount = rs.getString(4);
                sale_cr_narration = "";
                sale_dr_narration = "";
                profit_cr_account = rs.getString(11);
                profit_dr_account = rs.getString(12);
                profit_amount = rs.getString(5);
                profit_cr_narration = "";
                profit_dr_narration = "";
            }
        }
    }

    public void setAsset_id(String asset_id) {
        if (asset_id != null) {
            this.asset_id = asset_id;
        }
    }

    public void setCost_disposal_account(String cost_disposal_account) {
        if (cost_disposal_account != null) {
            this.cost_disposal_account = cost_disposal_account;
        }
    }

    public void setCost_asset_account(String cost_asset_account) {
        if (cost_asset_account != null) {
            this.cost_asset_account = cost_asset_account;
        }
    }

    public void setCost_amount(String cost_amount) {
        if (cost_amount != null) {
            this.cost_amount = cost_amount;
        }
    }

    public void setCost_cr_narration(String cost_cr_narration) {
        if (cost_cr_narration != null) {
            this.cost_cr_narration = cost_cr_narration;
        }
    }

    public void setCost_dr_narration(String cost_dr_narration) {
        if (cost_dr_narration != null) {
            this.cost_dr_narration = cost_dr_narration;
        }
    }

    public void setDep_acct(String dep_acct) {
        if (dep_acct != null) {
            this.dep_acct = dep_acct;
        }
    }

    public void setDep_disposal_acct(String dep_disposal_acct) {
        if (dep_disposal_acct != null) {
            this.dep_disposal_acct = dep_disposal_acct;
        }
    }

    public void setDep_amount(String dep_amount) {
        if (dep_amount != null) {
            this.dep_amount = dep_amount;
        }
    }

    public void setDep_cr_narration(String dep_cr_narration) {
        if (dep_cr_narration != null) {
            this.dep_cr_narration = dep_cr_narration;
        }
    }

    public void setDep_dr_narration(String dep_dr_narration) {
        if (dep_dr_narration != null) {
            this.dep_dr_narration = dep_dr_narration;
        }
    }

    public void setSale_purchase_acct(String sale_purchase_acct) {
        if (sale_purchase_acct != null) {
            this.sale_purchase_acct = sale_purchase_acct;
        }
    }

    public void setSale_disposal_acct(String sale_disposal_acct) {
        if (sale_disposal_acct != null) {
            this.sale_disposal_acct = sale_disposal_acct;
        }
    }

    public void setSale_amount(String sale_amount) {
        if (sale_amount != null) {
            this.sale_amount = sale_amount;
        }
    }

    public void setSale_cr_narration(String sale_cr_narration) {
        if (sale_cr_narration != null) {
            this.sale_cr_narration = sale_cr_narration;
        }
    }

    public void setSale_dr_narration(String sale_dr_narration) {
        if (sale_dr_narration != null) {
            this.sale_dr_narration = sale_dr_narration;
        }
    }

    public void setProfit_cr_account(String profit_cr_account) {
        if (profit_cr_account != null) {
            this.profit_cr_account = profit_cr_account;
        }
    }

    public void setProfit_dr_account(String profit_dr_account) {
        if (profit_dr_account != null) {
            this.profit_dr_account = profit_dr_account;
        }
    }

    public void setProfit_amount(String profit_amount) {
        if (profit_amount != null) {
            this.profit_amount = profit_amount;
        }
    }

    public void setProfit_cr_narration(String profit_cr_narration) {
        if (profit_cr_narration != null) {
            this.profit_cr_narration = profit_cr_narration;
        }
    }

    public void setProfit_dr_narration(String profit_dr_narration) {
        if (profit_dr_narration != null) {
            this.profit_dr_narration = profit_dr_narration;
        }
    }


    public String getAsset_id() {
        return asset_id;
    }

    public String getCost_disposal_account() {
        return cost_disposal_account;
    }

    public String getCost_asset_account() {
        return cost_asset_account;
    }

    public String getCost_amount() {
        return cost_amount;
    }

    public String getCost_cr_narration() {
        return cost_cr_narration;
    }

    public String getCost_dr_narration() {
        return cost_dr_narration;
    }

    public String getDep_acct() {
        return dep_acct;
    }

    public String getDep_disposal_acct() {
        return dep_disposal_acct;
    }

    public String getDep_amount() {
        return dep_amount;
    }

    public String getDep_cr_narration() {
        return dep_cr_narration;
    }

    public String getDep_dr_narration() {
        return dep_dr_narration;
    }

    public String getSale_purchase_acct() {
        return sale_purchase_acct;
    }

    public String getSale_disposal_acct() {
        return sale_disposal_acct;
    }

    public String getSale_amount() {
        return sale_amount;
    }

    public String getSale_cr_narration() {
        return sale_cr_narration;
    }

    public String getSale_dr_narration() {
        return sale_dr_narration;
    }

    public String getProfit_cr_account() {
        return profit_cr_account;
    }

    public String getProfit_dr_account() {
        return profit_dr_account;
    }

    public String getProfit_amount() {
        return profit_amount;
    }

    public String getProfit_cr_narration() {
        return profit_cr_narration;
    }

    public String getProfit_dr_narration() {
        return profit_dr_narration;
    }

    public String getEntry_date() {
        return entry_date;
    }

    /**
     * correctDate
     *
     * @param s String
     * @return String
     */
    private String correctDate(String s) {
        return DateManipulations.CalendarToDb(DateManipulations.DateToCalendar(
                s));
    }

}
