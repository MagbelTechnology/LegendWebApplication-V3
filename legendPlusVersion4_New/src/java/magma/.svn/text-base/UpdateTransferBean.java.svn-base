package magma;

import java.sql.ResultSet;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class UpdateTransferBean extends legend.ConnectionClass {
    private String asset_id = "";
    private String new_dept_id = "";
    private String old_dept_name = "";
    private String new_branch_id = "";
    private String old_branch_name = "";
    private String new_asset_user = "";
    private String old_asset_user = "";
    private String old_section = "";
    private String new_section = "";
    private String raise_entry = "";
    private Calendar transfer_date = new GregorianCalendar();

    public UpdateTransferBean() throws Exception {
        super();
    }

    /**
     * getTransferDetail
     *
     * @param s String
     */
    public void getTransferDetail(String s) throws Exception {
        ResultSet rs = getStatement().executeQuery(
                "am_msp_select_asset_transfer_detail " + s);
        if (rs.next()) {
            asset_id = rs.getString(2);
            new_dept_id = rs.getString(3);
            old_dept_name = rs.getString(4);
            new_branch_id = rs.getString(5);
            old_branch_name = rs.getString(6);
            new_asset_user = rs.getString(7);
            old_asset_user = rs.getString(8);
            old_section = rs.getString(9);
            new_section = rs.getString(10);
            raise_entry = rs.getString(11);
            transfer_date.setTime(rs.getDate(12));
        }
    }

    public String getAsset_id() {
        return asset_id;
    }

    public String getNew_dept_id() {
        return new_dept_id;
    }

    public String getOld_dept_name() {
        return old_dept_name;
    }

    public String getNew_branch_id() {
        return new_branch_id;
    }

    public String getOld_branch_name() {
        return old_branch_name;
    }

    public String getNew_asset_user() {
        return new_asset_user;
    }

    public String getOld_asset_user() {
        return old_asset_user;
    }

    public String getOld_section() {
        return old_section;
    }

    public String getNew_section() {
        return new_section;
    }

    public String getRaise_entry() {
        return raise_entry;
    }

    public String getTransfer_date() {
        return DateManipulations.CalendarToDate(transfer_date);
    }
}
