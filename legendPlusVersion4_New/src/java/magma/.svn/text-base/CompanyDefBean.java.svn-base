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
public class CompanyDefBean extends magma.ConnectionClass {
    private String[] company_info = new String[27];

    public CompanyDefBean() throws Exception {
        super();
    }

    /**
     * getCompanyDefs
     *
     * @return String[]
     * @throws Exception
     */
    public String[] getCompanyDefs() throws Exception {
        ResultSet rs = getStatement().executeQuery("am_msp_select_company");
        company_info = null;
        if (rs.next()) {
            company_info = new String[26];
            for (int k = 0; k < 26; k++) {
                company_info[k] = rs.getString(k + 1);
            }
        }
        return company_info;
    }

    /**
     * updateCompanyDef
     *
     * @return boolean
     * @throws Exception
     */
    public boolean updateCompanyDef() throws Exception {
        StringBuffer sb = new StringBuffer(200);
        if (company_info != null) {
            sb.append("am_msp_update_company ");
            sb.append("'" + company_info[0] + "'");
            for (int i = 1; i < 26; i++) {
                switch (i) {
                case 5:
                case 9:
                case 12:
                case 16:
                case 22:
                case 23:
                case 24:
                    sb.append(",");
                    sb.append(company_info[i]);
                    break;
                default:
                    sb.append(",'");
                    sb.append(company_info[i]);
                    sb.append("'");
                }
            }
        }
        return (getStatement().executeUpdate(
                sb.toString()) == -1);
    }

    public void setCompany_info(String[] company_info) {
        if (company_info != null) {
            this.company_info = company_info;
        }
    }

    public String[] getCompany_info() {
        return company_info;
    }
}
