package com.magbel.util;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import magma.net.vao.Category;
import com.magbel.legend.bus.Report;

public class HtmlUtility {

    private static final int QUERY_TIMEOUT_SECONDS = 30;

    private final DatetimeFormat df;

    public HtmlUtility() {
        df = new DatetimeFormat();
    }

    /* =========================================================
       SAFE CONNECTION HELPER
       ========================================================= */
    private Connection getLegendConnection() throws Exception {
        return new DataConnect("legendPlus").getConnection();
    }

    private Connection getOtherConnection() throws Exception {
        return new DataConnect("otherDataSource").getConnection();
    }
    
    public String getResources(String selected, String query) throws Exception {

        StringBuilder html = new StringBuilder();

        if (selected == null || selected.equalsIgnoreCase("null")) {
            selected = "ALL";
        }

        try (Connection con = new DataConnect("legendPlus").getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                String id = rs.getString(1);
                String label = rs.getString(2);

                String selectedAttr =
                        (id != null && selected != null &&
                         id.trim().equalsIgnoreCase(selected.trim()))
                        ? " selected"
                        : "";

                html.append("<option value='")
                    .append(escapeHtml(id))
                    .append("'")
                    .append(selectedAttr)
                    .append(">")
                    .append(escapeHtml(label))
                    .append("</option>\n");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error loading resources", e);
        }

        return html.toString();
    }
    
    public String getResources(int selected, String query) throws Exception {

        StringBuilder html = new StringBuilder();

        try (Connection con = new DataConnect("legendPlus").getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                int id = rs.getInt(1);
                String label = rs.getString(2);

                String selectedAttr =
                        (id == selected) ? " selected" : "";

                html.append("<option value='")
                    .append(id)
                    .append("'")
                    .append(selectedAttr)
                    .append(">")
                    .append(escapeHtml(label))
                    .append("</option>\n");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error loading resources", e);
        }

        return html.toString();
    }
    
    private String escapeHtml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }

    /* =========================================================
       SAFE findObject()
       ========================================================= */

    public String findObject(String query) {

        String result = "UNKNOWN";

        try (Connection con = getLegendConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setQueryTimeout(QUERY_TIMEOUT_SECONDS);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = rs.getString(1);
                }
            }

        } catch (Exception e) {
            System.err.println("ERROR findObject(): " + e.getMessage());
        }

        return result == null ? "" : result;
    }

    public String findObject(String query, String filter) {

        String result = "UNKNOWN";

        try (Connection con = getLegendConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setQueryTimeout(QUERY_TIMEOUT_SECONDS);
            ps.setString(1, filter);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = rs.getString(1);
                }
            }

        } catch (Exception e) {
            System.err.println("ERROR findObject(filter): " + e.getMessage());
        }

        return result == null ? "" : result;
    }
    
    /* =========================================================
    SAFE getOperand()
    ========================================================= */
    
    public String getOperand(String col){

        String op = "";

         String FINDER_QUERY = "SELECT DISTINCT operand from COL_FILTER WHERE COLUMN_NAME =?";

          try(Connection con  = (new DataConnect("legendPlus")).getConnection();
          	PreparedStatement ps = con.prepareStatement(FINDER_QUERY);) {
          	

              ps.setString(1, col);
             try( ResultSet rs = ps.executeQuery();) {

        while (rs.next()) {
                  op = rs.getString(1);
     }
             }

          } catch (Exception ex) {
              System.out.println("WARNING: cannot fetch OPERAND from COL_LOOK_UP->" +
                      ex.getMessage());
          } 

          return op;
  }

    /* =========================================================
       SAFE getCodeName()
       ========================================================= */

    public String getCodeName(String query) {

        String result = "";

        try (Connection con = getLegendConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setQueryTimeout(QUERY_TIMEOUT_SECONDS);

            if (query.trim().toUpperCase().startsWith("UPDATE")) {
                ps.executeUpdate();
                return "";
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = rs.getString(1);
                }
            }

        } catch (Exception e) {
            System.err.println("ERROR getCodeName(): " + e.getMessage());
        }

        return result == null ? "" : result;
    }

    public String getCodeName(String query, String param) {

        String result = "";

        try (Connection con = getLegendConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setQueryTimeout(QUERY_TIMEOUT_SECONDS);
            ps.setString(1, param);

            if (query.trim().toUpperCase().startsWith("UPDATE")) {
                ps.executeUpdate();
                return "";
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = rs.getString(1);
                }
            }

        } catch (Exception e) {
            System.err.println("ERROR getCodeName(param): " + e.getMessage());
        }

        return result == null ? "" : result;
    }

    /* =========================================================
       SAFE INSERT METHODS
       ========================================================= */

    public void insToAm_Invoice_No(String assetID, String lpo,
                                   String invoiceNo, String transType) {

        String sql = "INSERT INTO Am_Invoice_no " +
                "(asset_id,lpo,invoice_no,trans_type,create_date) " +
                "VALUES (?,?,?,?,?)";

        try (Connection con = getLegendConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setQueryTimeout(QUERY_TIMEOUT_SECONDS);

            ps.setString(1, assetID);
            ps.setString(2, lpo);
            ps.setString(3, invoiceNo);
            ps.setString(4, transType);
            ps.setString(5, String.valueOf(df.dateConvert(new Date())));

            ps.executeUpdate();

        } catch (Exception e) {
            System.err.println("ERROR insToAm_Invoice_No(): " + e.getMessage());
        }
    }

    public void insGrpToAm_Invoice_No(String assetID, String lpo,
                                      String invoiceNo, String transType,
                                      String grpID) {

        String sql = "INSERT INTO Am_Invoice_no " +
                "(asset_id,lpo,invoice_no,trans_type,group_id,create_date) " +
                "VALUES (?,?,?,?,?,?)";

        try (Connection con = getLegendConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setQueryTimeout(QUERY_TIMEOUT_SECONDS);

            ps.setString(1, assetID);
            ps.setString(2, lpo);
            ps.setString(3, invoiceNo);
            ps.setString(4, transType);
            ps.setString(5, grpID);
            ps.setString(6, String.valueOf(df.dateConvert(new Date())));

            ps.executeUpdate();

        } catch (Exception e) {
            System.err.println("ERROR insGrpToAm_Invoice_No(): " + e.getMessage());
        }
    }

    /* =========================================================
       SAFE findCategoryItems()
       ========================================================= */

    public ArrayList<Category> findCategoryItems(String categoryId,
                                                 String categoryCode,
                                                 String status) {

        ArrayList<Category> list = new ArrayList<>();

        String sql = "SELECT itemCode,isInventory,itemName " +
                "FROM am_ad_categoryItems " +
                "WHERE category_id=? AND category_code=? AND status=?";

        try (Connection con = getLegendConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setQueryTimeout(QUERY_TIMEOUT_SECONDS);

            ps.setString(1, categoryId);
            ps.setString(2, categoryCode);
            ps.setString(3, status);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Category c = new Category();
                    c.setCode(rs.getString("itemCode"));
                    c.setInventoryItem(rs.getString("isInventory"));
                    c.setName(rs.getString("itemName"));
                    list.add(c);
                }
            }

        } catch (Exception e) {
            System.err.println("ERROR findCategoryItems(): " + e.getMessage());
        }

        return list;
    }

    /* =========================================================
       SAFE updateAssetStatusChange()
       ========================================================= */

    public void updateAssetStatusChange(String query) {

        try (Connection con = getLegendConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setQueryTimeout(QUERY_TIMEOUT_SECONDS);
            ps.executeUpdate();

        } catch (Exception e) {
            System.err.println("ERROR updateAssetStatusChange(): " + e.getMessage());
        }
    }

    public void updateAssetStatusChange(String query,
                                        Timestamp date,
                                        int tranId) {

        try (Connection con = getLegendConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setQueryTimeout(QUERY_TIMEOUT_SECONDS);
            ps.setTimestamp(1, date);
            ps.setInt(2, tranId);
            ps.executeUpdate();

        } catch (Exception e) {
            System.err.println("ERROR updateAssetStatusChange(): " + e.getMessage());
        }
    }
    
    public void updateAssetStatusChange(String query_r,String naration,Timestamp date,int tranId){

    	try(Connection con = (new DataConnect("legendPlus")).getConnection();
    		PreparedStatement ps = con.prepareStatement(query_r.toString());) {
    		
    		  ps.setString(1, naration);
    		  ps.setTimestamp(2, date);
    		  ps.setInt(3, tranId);
    	       int i =ps.executeUpdate();
    	            //ps.execute();

    	        } catch (Exception ex) {

    	            System.out.println("HtmlUtility: updateAssetStatusChange()>>>>>" + ex);
    	        } 


    	}//updateAssetStatus()
    
    public String findObjectParam(String id)
    {
    	//System.out.println("====findObject query=====  "+query);

        String found = null;

        String finder = "UNKNOWN";
        String query = "select dept_code from am_gb_user where user_id = ? ";
        double sequence = 0.00d;
        try (Connection Con2 = new DataConnect("legendPlus").getConnection();
            	PreparedStatement Stat = Con2.prepareStatement(query)){

            Stat.setString(1, id);
           try( ResultSet result = Stat.executeQuery()){

            while (result.next()) {
                finder = result.getString(1);
            }
           }
        } catch (Exception ee2) {
            System.out.println("WARN:ERROR OBTAINING OBJ --> " + ee2);
            ee2.printStackTrace();
        } 
        return finder;
    }
    
    public String getUserListResources(String selected, String query) {

        StringBuilder html = new StringBuilder();
        String id = "";

        if (selected == null || selected.equalsIgnoreCase("null")) {
            selected = "ALL"; // Change this to match your default <option value="ALL">
        }

        try(Connection mcon =  new DataConnect("legendPlus").getConnection();
            	PreparedStatement mps = mcon.prepareStatement(query)) {
        	
        	
//            System.out.println("getUserListResources Parameter query:->>"+query);
//            System.out.println("getUserListResources Parameter selected:->>"+selected);
        	try(ResultSet mrs = mps.executeQuery();) {
            while (mrs.next()) {
                id = mrs.getString(1);
                String label = mrs.getString(2);
//                System.out.println("getUserListResources Id: "+id+"     label: "+label);
                String selectedAttr = (id != null && id.trim().equalsIgnoreCase(selected.trim())) ? " selected" : "";

                html.append("<option value='")
                    .append(id)
                    .append("'")
                    .append(selectedAttr)
                    .append(">")
                    .append(label)
                    .append("</option>\n");
            }
        	}
        } catch (Exception ee) {
            System.out.println("WARN HtmlUtil.getResources error: " + ee);
        } 

        return html.toString();
    }
    
    public String getResources(int selected) {

        StringBuilder html = new StringBuilder();

        String query = "SELECT BRANCH_ID, BRANCH_NAME " +
                       "FROM am_ad_BRANCH " +
                       "WHERE brnch_id = ? " +
                       "ORDER BY BRANCH_NAME";

        try (Connection mcon = (new DataConnect("legendPlus")).getConnection();
             PreparedStatement mps = mcon.prepareStatement(query)) {

            mps.setInt(1, selected);   

            try (ResultSet mrs = mps.executeQuery()) {

                while (mrs.next()) {

                    String id = mrs.getString("BRANCH_ID");

                    html.append("<option ")
                        .append(id != null && id.equals(String.valueOf(selected))
                                ? " selected='true' "
                                : "")
                        .append(" value='")
                        .append(id)
                        .append("'>")
                        .append(mrs.getString("BRANCH_NAME"))
                        .append("</option>");
                }
            }

        } catch (Exception ee) {
            System.out.println("WARN HtmlUtil getResources -> " + ee);
        }

        return html.toString();
    }


}
