package magma;

import java.sql.ResultSet;


public class RaiseAssetDetailBean extends legend.ConnectionClass {
    private String ledger = "";
    private String vendor = "";
    private String narration_1 = "";
    private String narration_2 = "";
    private String narration_3 = "";
    private String narration_4 = "";
    private String narration_5 = "";
    private String narration_6 = "";
    private String amount = "0";
    private String vat_account = "";
    private String vat_amount = "0";
    private String wh_amount = "0";
    private String wh_account = "";
    private String asset_id = "";
    private String category = "";
    private String entry_date = new java.text.SimpleDateFormat("dd/MM/yyyy").
                                format(new java.util.Date());

    public RaiseAssetDetailBean() throws Exception {
        super();
    }

    /**
     * getAssetDetail
     */
    public void getAssetDetail() throws Exception {
        if (asset_id != "") {

            StringBuffer b = new StringBuffer(100);
            b.append("am_msp_select_asset_raise_detail '");
            b.append(asset_id);
            b.append("'");

            ResultSet rs = getStatement().executeQuery(b.toString());
            if (rs.next()) {
                ledger = rs.getString(2);
                vendor = rs.getString(3);
                narration_1 = rs.getString(5);
                narration_2 = rs.getString(6);
                amount = rs.getString(4);
                vat_account = rs.getString(8);
                vat_amount = rs.getString(7);
                narration_3 = rs.getString(9);
                narration_4 = rs.getString(10);
                narration_5 = rs.getString(11);
                narration_6 = rs.getString(12);
                wh_account = rs.getString(13);
                wh_amount = rs.getString(14);
                category = rs.getString(16);
            }
        }

    }

    public void setNarration_1(String narration_1) {
        if (narration_1 != null) {
            this.narration_1 = narration_1;
        }
    }

    public void setNarration_2(String narration_2) {
        if (narration_2 != null) {
            this.narration_2 = narration_2;
        }
    }

    public void setAsset_id(String asset_id) {
        if (asset_id != null) {
            this.asset_id = asset_id;
        }
    }

    public void setNarration_3(String narration_3) {
        if (narration_3 != null) {
            this.narration_3 = narration_3;
        }
    }

    public void setNarration_4(String narration_4) {
        if (narration_4 != null) {
            this.narration_4 = narration_4;
        }
    }

    public void setNarration_5(String narration_5) {
        if (narration_5 != null) {
            this.narration_5 = narration_5;
        }
    }

    public void setNarration_6(String narration_6) {
        if (narration_6 != null) {
            this.narration_6 = narration_6;
        }
    }

    public void setAmount(String amount) {
        if (amount != null) {
            this.amount = amount.replaceAll(",", "");
        }
    }

    public void setEntry_date(String entry_date) {
        if (entry_date != null) {
            this.entry_date = entry_date;
        }
    }

    public void setLedger(String ledger) {
        if (ledger != null) {
            this.ledger = ledger;
        }
    }

    public void setVat_account(String vat_account) {
        if (vat_account != null) {
            this.vat_account = vat_account;
        }
    }

    public void setVat_amount(String vat_amount) {
        if (vat_amount != null) {
            this.vat_amount = vat_amount.replaceAll(",", "");
        }
    }

    public void setVendor(String vendor) {
        if (vendor != null) {
            this.vendor = vendor;
        }
    }

    public void setWh_account(String wh_account) {
        if (wh_account != null) {
            this.wh_account = wh_account;
        }
    }

    public void setWh_amount(String wh_amount) {
        if (wh_amount != null) {
            this.wh_amount = wh_amount.replaceAll("'", "");
        }
    }

    public String getLedger() {
        return ledger;
    }

    public String getVendor() {
        return vendor;
    }

    public String getNarration_1() {
        return narration_1;
    }

    public String getNarration_2() {
        return narration_2;
    }

    public String getAmount() {
        return amount;
    }

    public String getVat_account() {
        return vat_account;
    }

    public String getVat_amount() {
        return vat_amount;
    }

    public String getAsset_id() {
        return asset_id;
    }

    public String getNarration_3() {
        return narration_3;
    }

    public String getNarration_4() {
        return narration_4;
    }

    public String getNarration_5() {
        return narration_5;
    }

    public String getNarration_6() {
        return narration_6;
    }

    public String getWh_amount() {
        return wh_amount;
    }

    public String getWh_account() {
        return wh_account;
    }

    public String getEntry_date() {
        return entry_date;
    }

    /**
     * raiseEntry
     */
    public boolean raiseEntry() throws Exception {
        StringBuffer sb = new StringBuffer(300);
        sb.append("update am_asset set raise_entry = 'R' where asset_id = '");
        sb.append(asset_id);
        sb.append("'");

        int i = getStatement().executeUpdate(sb.toString());

        //get unique number to use as batch_id
        String batch_id = getIdentity();

        //raise cost entries
        sb = new StringBuffer(300);
        sb.append("am_msp_insert_entry_table '");
        sb.append(ledger);
        sb.append("','");
        sb.append(vendor);
        sb.append("','");
        sb.append(narration_1);
        sb.append("','");
        sb.append(narration_2);
        sb.append("',");
        sb.append(amount);
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
        System.out.print(sb.toString());

        i = getStatement().executeUpdate(sb.toString());

        if (Float.parseFloat(vat_amount) > 0) {
            //raise vat entries
            sb = new StringBuffer(300);
            sb.append("am_msp_insert_entry_table '");
            sb.append(ledger);
            sb.append("','");
            sb.append(vat_account);
            sb.append("','");
            sb.append(narration_3);
            sb.append("','");
            sb.append(narration_4);
            sb.append("',");
            sb.append(vat_amount);
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
            System.out.print(sb.toString());

            i = getStatement().executeUpdate(sb.toString());
        }

        if (Float.parseFloat(wh_amount) > 0) {
            //raise w/h tax entries
            sb = new StringBuffer(300);
            sb.append("am_msp_insert_entry_table '");
            sb.append(vendor);
            sb.append("','");
            sb.append(wh_account);
            sb.append("','");
            sb.append(narration_5);
            sb.append("','");
            sb.append(narration_6);
            sb.append("',");
            sb.append(wh_amount);
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
            System.out.print(sb.toString());

            i = getStatement().executeUpdate(sb.toString());
        }

        return true;
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
