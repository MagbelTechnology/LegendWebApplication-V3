package legend;

import java.sql.ResultSet;

public class AccountTypes extends ConnectionClass {
    private String acct_types[] = null;
    public AccountTypes() throws Exception {
    }

    public String[] getAcct_type() {
        return acct_types;
    }

    public void setAcct_type(String[] acct_types) {
        if (acct_types != null) {
            this.acct_types = acct_types;
        }

    }

    public String[] getAccountTypes() {
    	ResultSet rs = null;
    	ResultSet rsc = null;
        try {
            String cquery = "SELECT COUNT(*) FROM am_ad_account_type";
            rsc = getStatement().executeQuery(cquery);
            String query = "SELECT * FROM am_ad_account_type";
            rs = getStatement().executeQuery(query);
            String[] result = null;
            if (rsc.next()) {
                result = new String[rsc.getInt(1)];
            }
  
            int i = 0;

            while (rs.next()) {
                result[i] = rs.getString("acct_type");
                i++;
            }
            if (rsc != null) {
            	rsc.close();
            }
            if (rs != null) {
                rs.close();
            }
            freeResource();
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                if (rsc != null) {
                	rsc.close();
                }
                if (rs != null) {
                    rs.close();
                }
                freeResource();
            } catch (Exception exs) {
                exs.printStackTrace();

            }
        }
        return null;
    }

    public boolean acctRequired() {
        try {
            String query = "SELECT req_accttype FROM AM_AD_LEGACY_SYS_CONFIG";
            ResultSet rs = getStatement().executeQuery(query);
            boolean result = false;
            if (rs.next()) {
                if (rs.getString("req_accttype").equalsIgnoreCase("Y")) {
                    result = true;
                }
                freeResource();

                return result;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                freeResource();
            } catch (Exception exs) {
                exs.printStackTrace();

            }
        }
        
        return false;
    }


}
