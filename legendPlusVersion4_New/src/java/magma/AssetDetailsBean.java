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
public class AssetDetailsBean extends legend.ConnectionClass {
    private String department = "0";
    private String branch = "0";
    private String category = "0";
    private String status = "*";
    private String asset_id = "";
    private int start_record = 0;
    private int rec_count = 10;
    private String registration_no = "";

    public AssetDetailsBean() throws Exception {
        super();
    }


    public String[][] getAssetList() throws Exception {
        StringBuffer b = new StringBuffer(400);
        b.append("am_msp_select_asset_list '");
        b.append(asset_id);
        b.append("',");
        b.append(department);
        b.append(",");
        b.append(branch);
        b.append(",");
        b.append(category);
        b.append(",'");
        b.append(status);
        b.append("','");
        b.append(registration_no);
        b.append("'");

        ResultSet rs = getStatement().executeQuery(b.toString());

        b = new StringBuffer(400);
        b.append("am_msp_count_asset_list '");
        b.append(asset_id);
        b.append("',");
        b.append(department);
        b.append(",");
        b.append(branch);
        b.append(",");
        b.append(category);
        b.append(",'");
        b.append(status);
        b.append("','");
        b.append(registration_no);
        b.append("'");

        ResultSet rsc = getStatement().executeQuery(b.toString());
        rsc.next();
        int count = rsc.getInt(1);

        if (count > 0) {
            String a[][] = new String[count][9];
            int i = 0;
            while (rs.next()) {
                for (int j = 0; j < 9; j++) {
                    a[i][j] = rs.getString(j + 1);
                }
                i++;
            }
            return a;
        }
        return null;
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

    public void setCategory(String category) {
        if (category != null) {
            this.category = category;
        }
    }

    public void setStatus(String status) {
        if (status != null) {
            this.status = status;
        }
    }

    public void setAsset_id(String asset_id) {
        if (asset_id != null) {
            this.asset_id = asset_id;
        }
    }

    public void setRegistration_no(String registration_no) {
        if (registration_no != null) {
            this.registration_no = registration_no;
        }
    }

    public void setStart_record(String start_record) {
        if (start_record != null) {
            this.start_record = Integer.parseInt(start_record);
        }
    }

    public void setRec_count(String rec_count) {
        if (rec_count != null) {
            this.rec_count = Integer.parseInt(rec_count);
        }
    }

    public String getDepartment() {
        return department;
    }

    public String getBranch() {
        return branch;
    }

    public String getCategory() {
        return category;
    }

    public String getStatus() {
        return status;
    }

    public String getAsset_id() {
        return asset_id;
    }

    public String getRegistration_no() {
        return registration_no;
    }

    public String getStart_record() {
        return String.valueOf(start_record);
    }

    public String getRec_count() {
        return String.valueOf(rec_count);
    }
}
