package legend;

//import java.sql.ResultSet;
import java.sql.*;
import magma.net.dao.MagmaDBConnection;
import audit.*;

public class CategorySetupBean_ extends ConnectionClass {
    private String[] categories = new String[22];

    private AuditInfo ai = new AuditInfo();
    private AuditTrailGen atg = new AuditTrailGen();

    public final void setAuditInfo(AuditInfo AI) {
        this.ai = AI;
    }

    public final void setAuditInfo(String TableName, String BranchCode,
                                   int LoginId, String RowId, boolean

                                   ReqInsertAudit) {
        ai.setTableName(TableName);
        ai.setBranchCode(BranchCode);
        ai.setLoginId(LoginId);
        ai.setRowId(RowId);
        ai.setReqInsertAudit(ReqInsertAudit);
    }

    public final AuditInfo getAuditInfo() {
        return this.ai;
    }

    public CategorySetupBean_() throws Exception {
    }

    public void setCategories(String[] categories) {
        if (categories != null) {
            this.categories = categories;
        }
    }

    public String[] getCategories() {
        return categories;
    }

    /**
     * selectcategories
     *
     * @param con String
     * @return String[][]
     * @throws Throwable
     */
    //modified by Olabo
    /* private ResultSet countCategories(String con, String sta)
     {
      Connection cnn = null;
      PreparedStatement ps = null;
      ResultSet rc = null;
      MagmaDBConnection dbConn = new MagmaDBConnection();

      String queryCount = "";
      String param = "";

      if (con.equalsIgnoreCase("0"))
      {
     queryCount ="select count(*) from am_ad_category where category_status = ?";


       //querySelect = "select * from am_ad_category where category_status = ? order by category_name asc";
      // System.out.println("if...");
      }
      else if (!con.equalsIgnoreCase("0"))
      {
     queryCount = "select count(*) from am_ad_category where category_id = ?";
       //querySelect = "select * from am_ad_category where category_id = ?";
       //System.out.println("else if...");
      }
     else
      {
      }
      //String[][] values = null;
      try
      {
      cnn = dbConn.getConnection("fixedasset");
      ps = cnn.prepareStatement(queryCount);

      ps.setString(1,sta);
      rc = ps.executeQuery();

      ps = cnn.prepareStatement(querySelect);
      ps.setString(1,con);
      rv = ps.executeQuery();

      while(rc.next())

      values = new String[rc.getInt(1)][22];

       for (int x = 0; x < values.length; x++) {
         rv.next();
         for (int y = 0; y < 22; y++) {
           values[x][y] = rv.getString(y + 1);
         }
       }
      }
      catch (Exception e)
      {
       System.out.println("INFO:Error selectCategories() ->" +
                                  e.getMessage());
      }
      finally
      {
       dbConn.closeConnection(cnn, ps, rs);
      }
      return values;
     }*/
    public String[][] selectCategories(String con, String sta) throws Throwable {
        /*StringBuffer cq = new StringBuffer(100);
             cq.append("am_msp_count_categories"
                  +" '"+con+"','"+sta+"'");
             ResultSet rc = getStatement().executeQuery(
          cq.toString());

             StringBuffer sq = new StringBuffer(100);
             sq.append("am_msp_select_categories"
                  +" '"+con+"','"+sta+"'");
             ResultSet rv = getStatement().executeQuery(
          sq.toString());*/
        Connection cnn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        MagmaDBConnection dbConn = new MagmaDBConnection();
        String param = "";
        String queryCount = "";
        String querySelect = "";

        if (con.equalsIgnoreCase("0")) {
            queryCount =
                    "select count(*) from am_ad_category where category_status = ?";
            querySelect = "select * from am_ad_category where category_status = ? order by category_name asc";
            param = sta;
        } else if (!con.equalsIgnoreCase("0")) {
            queryCount =
                    "select count(*) from am_ad_category where category_id = ?";
            querySelect = "select * from am_ad_category where category_id = ?";
            param = con;
        } else {
        }
        String[][] values = null;
        int j = 0;
        try {
            cnn = dbConn.getConnection("fixedasset");
            ps = cnn.prepareStatement(queryCount);

            ps.setString(1, param);
            rs = ps.executeQuery();
            while (rs.next()) {
                j = rs.getInt(1);
            }

            ps = cnn.prepareStatement(querySelect);
            ps.setString(1, param);
            rs = ps.executeQuery();

            //while(rc.next())

            values = new String[j][22];

            for (int x = 0; x < values.length; x++) {
                rs.next();
                for (int y = 0; y < 22; y++) {
                    values[x][y] = rs.getString(y + 1);
                }
            }
        } catch (Exception e) {
            System.out.println("INFO:Error selectCategories() ->" +
                               e.getMessage());
        } finally {
            dbConn.closeConnection(cnn, ps, rs);
        }
        return values;
    }

    /**
     * updatecategories
     *
     * @param con String
     * @return boolean
     * @throws Throwable
     */
    public boolean updateCategories(String con) throws Throwable {
        StringBuffer iq = new StringBuffer(100);
        iq.append("am_msp_update_categories");
        iq.append("'");
        iq.append(Integer.parseInt(con));
        iq.append("'");

        for (int i = 0; i < 21; i++) {
            switch (i) {
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                iq.append(", ");
                iq.append(Double.parseDouble(categories[i]));
                break;

            case 10:
                iq.append(", ");
                iq.append(Double.parseDouble(categories[i]));
                break;
            default:
                iq.append(",'");
                iq.append(categories[i]);
                iq.append("'");
            }
        }

        int ret = getStatement().executeUpdate(
                iq.toString());
        if (ret > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * insertcategories
     *
     * @return boolean
     * @throws Throwable
     */
    public boolean insertCategories() throws Throwable {
        StringBuffer iq = new StringBuffer(100);
        iq.append("am_msp_insert_categories");
        iq.append("'");
        iq.append(categories[0]);
        iq.append("'");

        for (int i = 1; i <= 20; i++) {
            switch (i) {
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                iq.append(", ");
                iq.append(categories[i]);
                break;
            default:
                iq.append(",'");
                iq.append(categories[i]);
                iq.append("'");
            }
        }

        if (ai.reqInsertAudit() == true) {
            atg.select(1, "SELECT * FROM am_ad_category WHERE category_Id = (SELECT MIN(category_Id) FROM AM_AD_CATEGORY)");
            atg.captureAuditFields(categories);
            atg.logAuditTrail(ai);
        }
        return (getStatement().executeUpdate(
                iq.toString()) != -1);
    }
}
