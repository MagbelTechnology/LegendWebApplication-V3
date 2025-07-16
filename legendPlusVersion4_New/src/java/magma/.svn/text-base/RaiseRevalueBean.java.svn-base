package magma;

public class RaiseRevalueBean extends legend.ConnectionClass {
    private String dr_account = "";
    private String cr_account = "";
    private String dr_narration = "";
    private String cr_narration = "";
    private String entry_date = new java.text.SimpleDateFormat("dd/MM/yyyy").
                                format(new java.util.Date());

    private String amount = "0";

    public RaiseRevalueBean() throws Exception {
        super();
    }

    public void setDr_account(String dr_account) {
        if (dr_account != null) {
            this.dr_account = dr_account;
        }
    }

    public void setCr_account(String cr_account) {
        if (cr_account != null) {
            this.cr_account = cr_account;
        }
    }

    public void setDr_narration(String dr_narration) {
        if (dr_narration != null) {
            this.dr_narration = dr_narration.replaceAll(",", "");
        }
    }

    public void setCr_narration(String cr_narration) {
        if (cr_narration != null) {
            this.cr_narration = cr_narration.replaceAll(",", "");
        }
    }

    public void setAmount(String amount) {
        if (amount != null) {
            this.amount = amount;
            if (Float.parseFloat(this.amount) < 0) {
                String t = this.dr_account;
                this.dr_account = this.cr_account;
                this.cr_account = t;
                this.amount = String.valueOf(Float.parseFloat(this.amount) * -1);
            }
        }
    }

    public String getDr_account() {
        return dr_account;
    }

    public String getCr_account() {
        return cr_account;
    }

    public String getDr_narration() {
        return dr_narration;
    }

    public String getCr_narration() {
        return cr_narration;
    }

    public String getEntry_date() {
        return entry_date;
    }

    public String getAmount() {
        return amount;
    }

    /**
     * process()
     *
     * @return boolean
     */
    public boolean process() throws Exception {
        StringBuffer sb = new StringBuffer(200);

        //get unique number to use as batch_id
        String batch_id = getIdentity();

        //raise cost entries
        sb = new StringBuffer(300);
        sb.append("am_msp_insert_entry_table '");
        sb.append(dr_account);
        sb.append("','");
        sb.append(cr_account);
        sb.append("','");
        sb.append(dr_narration);
        sb.append("','");
        sb.append(cr_narration);
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

        int i = getStatement().executeUpdate(sb.toString());

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
