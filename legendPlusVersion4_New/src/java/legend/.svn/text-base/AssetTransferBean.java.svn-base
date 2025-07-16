package legend;

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
public class AssetTransferBean extends ConnectionClass {
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
        b.append(getIdentity());
        b.append("','");
        b.append(asset_id);
        b.append("','");
        b.append(new_department);
        b.append("','");
        b.append(getDepartmentId(department));
        b.append("','");
        b.append(new_branch);
        b.append("','");
        b.append(getBranchId(branch));
        b.append("','");
        b.append(new_user);
        b.append("','");
        b.append(asset_user);
        b.append("','");
        //new section
        b.append("','");
        //old section
        b.append("','");
        b.append(raise_entry);
        b.append("','");
        b.append(who_to_remind);
        b.append("','");
        b.append(email_1);
        b.append("','");
        b.append(email_2);
        b.append("','");
        b.append(user_id);
        b.append("'");

        int i = getStatement().executeUpdate(b.toString());
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
                asset_id = rs.getString(1);
                asset_name = rs.getString(6);
                asset_user = rs.getString(15);
                category = rs.getString(5);
                department = rs.getString(4);
                branch = rs.getString(3);
                registration_no = rs.getString(2);
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
        }
    }

    public void setUser_id(String user_id) {
        if (user_id != null) {
            this.user_id = user_id;
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
}
