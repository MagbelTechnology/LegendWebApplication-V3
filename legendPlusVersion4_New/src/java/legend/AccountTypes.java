package legend;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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

    public String[] getAccountTypesOld() {
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
              
            } catch (Exception exs) {
                exs.printStackTrace();

            }
        }
        return null;
    }
    
    public String[] getAccountTypes() {
        String query = "SELECT acct_type FROM am_ad_account_type";
        List<String> resultList = new ArrayList<>();

        try (Connection con = getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                resultList.add(rs.getString("acct_type"));
            }

            return resultList.toArray(new String[0]);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public boolean acctRequiredold() {
        try {
            String query = "SELECT req_accttype FROM AM_AD_LEGACY_SYS_CONFIG";
            ResultSet rs = getStatement().executeQuery(query);
            boolean result = false;
            if (rs.next()) {
                if (rs.getString("req_accttype").equalsIgnoreCase("Y")) {
                    result = true;
                }
               

                return result;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
               
            } catch (Exception exs) {
                exs.printStackTrace();

            }
        }
        
        return false;
    }
    public boolean acctRequired() {
        String query = "SELECT req_accttype FROM AM_AD_LEGACY_SYS_CONFIG";

        try (Connection con = getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                return "Y".equalsIgnoreCase(rs.getString("req_accttype"));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }

}
