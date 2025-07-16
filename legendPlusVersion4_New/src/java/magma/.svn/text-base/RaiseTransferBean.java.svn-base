package magma;

public class RaiseTransferBean extends legend.ConnectionClass {
    private String dr_gl = "";
    private String dr_gl_narration = "";
    private String cr_gl = "";
    private String cr_gl_narration = "";
    private String dr_a_dep = "";
    private String dr_a_dep_narration = "";
    private String cr_a_dep = "";
    private String cr_a_dep_narration = "";
    private String cost_amount = "0";
    private String dep_amount = "0";
    private String entry_date = new java.text.SimpleDateFormat("dd/MM/yyyy").
                                format(new java.util.Date());

    public RaiseTransferBean() throws Exception {
        super();
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

    /**
     * process
     *
     * @return boolean
     */
    public boolean process() throws Exception {
        StringBuffer sb;
        String batch_id = getIdentity();
        //cost entries
        sb = new StringBuffer(300);
        sb.append("am_msp_insert_entry_table '");
        sb.append(dr_gl);
        sb.append("','");
        sb.append(cr_gl);
        sb.append("','");
        sb.append(dr_gl_narration);
        sb.append("','");
        sb.append(cr_gl_narration);
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
        System.out.print(sb.toString());
        getStatement().executeUpdate(sb.toString());

        //accum dep entries
        sb = new StringBuffer(300);
        sb.append("am_msp_insert_entry_table '");
        sb.append(dr_a_dep);
        sb.append("','");
        sb.append(dr_a_dep_narration);
        sb.append("','");
        sb.append(cr_a_dep);
        sb.append("','");
        sb.append(cr_a_dep_narration);
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
        System.out.print(sb.toString());
        getStatement().executeUpdate(sb.toString());

        return false;
    }

    public void setDr_gl(String dr_gl) {
        if (dr_gl != null) {
            this.dr_gl = dr_gl;
        }
    }

    public void setDr_gl_narration(String dr_gl_narration) {
        if (dr_gl_narration != null) {
            this.dr_gl_narration = dr_gl_narration;
        }
    }

    public void setCr_gl(String cr_gl) {
        if (cr_gl != null) {
            this.cr_gl = cr_gl;
        }
    }

    public void setCr_gl_narration(String cr_gl_narration) {
        if (cr_gl_narration != null) {
            this.cr_gl_narration = cr_gl_narration;
        }
    }

    public void setDr_a_dep(String dr_a_dep) {
        if (dr_a_dep != null) {
            this.dr_a_dep = dr_a_dep;
        }
    }

    public void setDr_a_dep_narration(String dr_a_dep_narration) {
        if (dr_a_dep_narration != null) {
            this.dr_a_dep_narration = dr_a_dep_narration;
        }
    }

    public void setCr_a_dep(String cr_a_dep) {
        if (cr_a_dep != null) {
            this.cr_a_dep = cr_a_dep;
        }
    }

    public void setCr_a_dep_narration(String cr_a_dep_narration) {
        if (cr_a_dep_narration != null) {
            this.cr_a_dep_narration = cr_a_dep_narration;
        }
    }

    public void setCost_amount(String cost_amount) {
        if (cost_amount != null) {
            this.cost_amount = cost_amount;
        }
    }

    public void setDep_amount(String dep_amount) {
        if (dep_amount != null) {
            this.dep_amount = dep_amount;
        }
    }

    public void setEntry_date(String entry_date) {
        //this.entry_date = entry_date;
    }

    public String getDr_gl() {
        return dr_gl;
    }

    public String getDr_gl_narration() {
        return dr_gl_narration;
    }

    public String getCr_gl() {
        return cr_gl;
    }

    public String getCr_gl_narration() {
        return cr_gl_narration;
    }

    public String getDr_a_dep() {
        return dr_a_dep;
    }

    public String getDr_a_dep_narration() {
        return dr_a_dep_narration;
    }

    public String getCr_a_dep() {
        return cr_a_dep;
    }

    public String getCr_a_dep_narration() {
        return cr_a_dep_narration;
    }

    public String getCost_amount() {
        return cost_amount;
    }

    public String getDep_amount() {
        return dep_amount;
    }

    public String getEntry_date() {
        return entry_date;
    }
}
