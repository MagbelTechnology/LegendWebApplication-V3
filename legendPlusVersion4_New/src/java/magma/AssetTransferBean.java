package magma;

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
public class AssetTransferBean extends legend.ConnectionClass {
    private String asset_id = "";
    private String asset_name = "";
    private String asset_user = "";
    private String category = "";
    private String department = "";
    private String branch = "";
    private String new_department = "";
    private String new_branch = "";
    private String new_user = "";
    private String registration_no = "";
    private String who_to_remind = "";
    private String email_1 = "";
    private String email_2 = "";
    private String raise_entry = "N";
    private String user_id = "1";
    private String section = "";
    private String new_section = "";
    private String who_to_remind_2 = "";

    private String dr_asset_ledger = "";
    private String cr_asset_ledger = "";
    private String cost_amount = "0";

    private String dr_accum_ledger = "";
    private String cr_accum_ledger = "";
    private String accum_amount = "0";

    public AssetTransferBean() throws Exception {
        super();
    }

    /**
     * insertAssetTransfer
     *
     * @return boolean
     */
    public boolean insertAssetTransfer() throws Exception {
        StringBuffer b = new StringBuffer(600);
        b.append("am_msp_asset_transfer ");
        b.append("'");
        b.append(asset_id);
        b.append("',");
        b.append(new_department);
        b.append(",");
        b.append(getDepartmentId(department));
        b.append(",");
        b.append(new_branch);
        b.append(",");
        b.append(getBranchId(branch));
        b.append(",'");
        b.append(new_user);
        b.append("','");
        b.append(asset_user);
        b.append("',");
        b.append(new_section);
        b.append(",");
        b.append(getSectionId(section));
        b.append(",'");
        b.append(raise_entry);
        b.append("','");
        b.append(who_to_remind);
        b.append("','");
        b.append(email_1);
        b.append("','");
        b.append(who_to_remind_2);
        b.append("','");
        b.append(email_2);
        b.append("',");
        b.append(user_id);

        int i = getStatement().executeUpdate(b.toString());

        if (raise_entry.equals("Y")) {
            java.sql.ResultSet rst = getStatement().executeQuery(
                    "select cost_price, accum_dep, category_id from am_asset where asset_id = '" +
                    asset_id + "'");
            rst.next();
            cost_amount = rst.getString(1);
            accum_amount = rst.getString(2);
            String c = rst.getString(3);
            String np, op;

            rst = getStatement().executeQuery(
                    "select sbu_required from am_gb_company");
            rst.next();
            String r = rst.getString(1);
            if (r.equals("Y")) {
                rst = getStatement().executeQuery(
                        "select gl_prefix from am_ad_branch where branch_id = " +
                        getBranchId(branch));
                rst.next();
                op = rst.getString(1);

                rst = getStatement().executeQuery(
                        "select gl_prefix from am_ad_branch where branch_id = " +
                        new_branch);
                rst.next();
                np = rst.getString(1);

            } else {
                rst = getStatement().executeQuery(
                        "select gl_prefix from am_ad_department where department_id = " +
                        getDepartmentId(department));
                rst.next();
                op = rst.getString(1);

                rst = getStatement().executeQuery(
                        "select gl_prefix from am_ad_department where department_id = " +
                        new_department);
                rst.next();
                np = rst.getString(1);

            }

            rst = getStatement().executeQuery(
                    "select asset_ledger, accum_dep_ledger from am_ad_category where category_id = " +
                    c);
            rst.next();

            String al = rst.getString(1);
            String dl = rst.getString(2);

            dr_asset_ledger = np + "-" + al;
            cr_asset_ledger = op + "-" + al;

            dr_accum_ledger = op + "-" + dl;
            cr_accum_ledger = np + "-" + dl;
        }
        return (i == -1);
    }

    /**
     * updateAssetTransfer
     *
     * @return boolean
     */
    public boolean updateAssetTransfer() {
        return false;
    }

    /**
     * getAssetTransfers
     */
    public void getAssetTransfers() throws Exception {
        if (asset_id != "") {
            ResultSet rs = getStatement().executeQuery(
                    "am_msp_select_asset_details '" + asset_id + "'");

            if (rs.next()) {
                setAsset_id(rs.getString(1));
                setAsset_name(rs.getString(6));
                setAsset_user(rs.getString(15));
                setCategory(rs.getString(5));
                setDepartment(rs.getString(4));
                setBranch(rs.getString(3));
                setRegistration_no(rs.getString(2));
                setSection(rs.getString(40));
                setWho_to_remind(rs.getString(35));
                setEmail_1(rs.getString(36));
                setEmail_2(rs.getString(37));
                setWho_to_remind_2(rs.getString(45));
            }
        }

    }

    public void setAsset_id(String asset_id) {
        if (asset_id != null) {
            this.asset_id = asset_id;
        }
    }

    public void setAsset_name(String asset_name) {
        if (asset_name != null) {
            this.asset_name = asset_name;
        }
    }

    public void setAsset_user(String asset_user) {
        if (asset_user != null) {
            this.asset_user = asset_user;
        }
    }

    public void setCategory(String category) {
        if (category != null) {
            this.category = category;
        }
    }

    public void setDepartment(String department) {
        if (department != null) {
            this.department = department;
        }
    }

    public void setBranch(String branch) {
        if (branch != null) {
            this.branch = branch;
        }
    }

    public void setNew_department(String new_department) {
        if (new_department != null) {
            this.new_department = new_department;
        }
    }

    public void setNew_branch(String new_branch) {
        if (new_branch != null) {
            this.new_branch = new_branch;
        }
    }

    public void setNew_user(String new_user) {
        if (new_user != null) {
            this.new_user = new_user;
        }
    }

    public void setRegistration_no(String registration_no) {
        if (registration_no != null) {
            this.registration_no = registration_no;
        }
    }

    public void setWho_to_remind(String who_to_remind) {
        if (who_to_remind != null) {
            this.who_to_remind = who_to_remind;
        }
    }

    public void setEmail_1(String email_1) {
        if (email_1 != null) {
            this.email_1 = email_1;
        }
    }

    public void setEmail_2(String email_2) {
        if (email_2 != null) {
            this.email_2 = email_2;
        }
    }

    public void setRaise_entry(String raise_entry) {
        if (raise_entry != null) {
            this.raise_entry = raise_entry;
            try {
                if (new_branch == getBranchId(branch)) {
                    this.raise_entry = "N";
                }
            } catch (Exception ex) {
            }
        }
    }

    public void setUser_id(String user_id) {
        if (user_id != null) {
            this.user_id = user_id;
        }
    }

    public void setSection(String section) {
        if (section != null) {
            this.section = section;
        }
    }

    public void setNew_section(String new_section) {
        if (new_section != null) {
            this.new_section = new_section;
        }
    }

    public void setWho_to_remind_2(String who_to_remind_2) {
        if (who_to_remind_2 != null) {
            this.who_to_remind_2 = who_to_remind_2;
        }
    }

    public String getAsset_id() {
        return asset_id;
    }

    public String getAsset_name() {
        return asset_name;
    }

    public String getAsset_user() {
        return asset_user;
    }

    public String getCategory() {
        return category;
    }

    public String getDepartment() {
        return department;
    }

    public String getBranch() {
        return branch;
    }

    public String getNew_department() {
        return new_department;
    }

    public String getNew_branch() {
        return new_branch;
    }

    public String getNew_user() {
        return new_user;
    }

    public String getRegistration_no() {
        return registration_no;
    }

    public String getWho_to_remind() {
        return who_to_remind;
    }

    public String getEmail_1() {
        return email_1;
    }

    public String getEmail_2() {
        return email_2;
    }

    public String getRaise_entry() {
        return raise_entry;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getSection() {
        return section;
    }

    public String getNew_section() {
        return new_section;
    }

    public String getWho_to_remind_2() {
        return who_to_remind_2;
    }

    public String getCost_amount() {
        return cost_amount;
    }

    public String getAccum_amount() {
        return accum_amount;
    }

    public String getDr_asset_ledger() {
        return dr_asset_ledger;
    }

    public String getCr_asset_ledger() {
        return cr_asset_ledger;
    }

    public String getDr_accum_ledger() {
        return dr_accum_ledger;
    }

    public String getCr_accum_ledger() {
        return cr_accum_ledger;
    }

    /**
     * getBranchId
     *
     * @param branch String
     * @return String
     */
    public String getBranchId(String branch) throws Exception {
        ResultSet rs = getStatement().executeQuery("am_msp_get_branch_id '" +
                branch + "'");
        rs.next();
        return rs.getString(1);
    }

    /**
     * getDepartmentId
     *
     * @param dept String
     * @return String
     */
    public String getDepartmentId(String dept) throws Exception {
        ResultSet rs = getStatement().executeQuery("am_msp_get_department_id '" +
                dept + "'");
        rs.next();
        return rs.getString(1);
    }

    public String getSectionId(String section) throws Exception {
        ResultSet rs = getStatement().executeQuery("am_msp_get_section_id '" +
                section + "'");
        rs.next();
        return rs.getString(1);
    }

}
