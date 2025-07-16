package magma.asset.manager;

import magma.CurrentDateTime;
import magma.net.dao.MagmaDBConnection;
import java.sql.*;
import java.util.*;
import magma.asset.dto.*;
import magma.util.Codes;
import com.magbel.util.ApplicationHelper;

public class WIPAssetManager extends MagmaDBConnection {

    private Codes code;


    public WIPAssetManager() {
        System.out.println("USING magma.asset.manager.WIPAssetManager");
        code = new Codes();
    }

    public void updateWIPInfo(String assetId) {
        System.out.println("\n\nhere in NNNNNNNNNNNNNNNNNNNNN wip assset manager " +assetId);
        updateDepRate(assetId);
        updateLastDepDate(assetId);
        updateTotalLife(assetId);
        updateUsefulLife(assetId);
        updateRemainingLife(assetId);
        updateDepEndDate(assetId);
        updateMonthlyDep(assetId);
        updateAccumDep(assetId);
        updateNBV(assetId);
    }


     public void updateWIPInfoFuture(String assetId) {
        System.out.println("\n\nhere in NNNNNNNNNNNNNNNNNNNNN wip assset manager Future " +assetId);
        updateDepRateFuture(assetId);
        updateTotalLifeFuture(assetId);
        updateDepEndDateFuture(assetId);
        updateNBVFuture(assetId);
        updateRemainingLifeFuture(assetId);
        //updateDepEndDate(assetId);
        //updateMonthlyDep(assetId);
        //updateAccumDep(assetId);
        //updateNBV(assetId);
    }

    public void updateDepRate(String assetID) {



        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int i = 0;

        String updateQuery = "update am_asset set dep_rate = c.dep_rate from am_asset a," +
                " am_ad_category c where a.category_code = c.category_code and a.asset_id ='"+assetID+"'";
                
        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(updateQuery);
            //ps.setString(1, assetID);


            i = ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("INFO:Error in updateDepRate() of WIPAssetManager ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        //return i;
    }

    public void updateLastDepDate(String assetID) {



        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int i = 0;

        String updateQuery = "update  am_asset set  last_dep_date  =  processing_date from am_asset, " +
                "am_gb_company where  effective_date  <=  processing_date and asset_id =?";



        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(updateQuery);
            ps.setString(1, assetID);


            i = ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("INFO:Error in updateLastDepDate() of WIPAssetManager ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        //return i;
    }

    public void updateTotalLife(String assetID) {



        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int i = 0;

        String updateQuery = "update am_asset set Total_life = 100/dep_rate * 12 where asset_id = ?";



        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(updateQuery);
            ps.setString(1, assetID);


            i = ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("INFO:Error in updateTotalLife() of WIPAssetManager ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        //return i;
    }

    public void updateUsefulLife(String assetID) {



        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int i = 0;

        String updateQuery = "update  am_asset set useful_life = datediff (mm,effective_date,last_dep_date) +1 where asset_id = ?";



        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(updateQuery);
            ps.setString(1, assetID);


            i = ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("INFO:Error in updateUsefulLife() of WIPAssetManager ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        //return i;
    }

    public void updateRemainingLife(String assetID) {



        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int i = 0;

        String updateQuery = "update am_asset set remaining_life = Total_life - useful_life where asset_id = ?";



        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(updateQuery);
            ps.setString(1, assetID);


            i = ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("INFO:Error in updateRemainingLife() of WIPAssetManager ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        //return i;
    }

    public void updateDepEndDate(String assetID) {



        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int i = 0;

        String updateQuery1 = "update  am_asset set  dep_end_date  =   dateadd ( mm , total_life , effective_date )where asset_id = ?";

        String updateQuery2 = "update  am_asset set  dep_end_date  =   dateadd ( dd ,-1 , dep_end_date ) where asset_id = ?";

        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(updateQuery1);
            ps.setString(1, assetID);
            i = ps.executeUpdate();

            if (i > 0) {
                ps = con.prepareStatement(updateQuery2);
                ps.setString(1, assetID);
                i = ps.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println("INFO:Error in updateDepEndDate() of WIPAssetManager ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        //return i;
    }

    public void updateMonthlyDep(String assetID) {



        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int i = 0;

        String updateQuery = "update am_asset set monthly_dep = (vatable_cost - residual_value)/ total_life where asset_id = ?";



        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(updateQuery);
            ps.setString(1, assetID);


            i = ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("INFO:Error in updateMonthlyDep() of WIPAssetManager ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        //return i;
    }

    public void updateAccumDep(String assetID) {



        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int i = 0;

        String updateQuery = "update  am_asset set  accum_dep  =  useful_life  *  monthly_dep where asset_id = ?";



        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(updateQuery);
            ps.setString(1, assetID);


            i = ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("INFO:Error in updateAccumDep() of WIPAssetManager ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        //return i;
    }

    public void updateNBV(String assetID) {



        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int i = 0;

        String updateQuery = "update am_asset set NBV = cost_price - accum_dep where asset_id = ?";



        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(updateQuery);
            ps.setString(1, assetID);


            i = ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("INFO:Error in updateNBV() of WIPAssetManager ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        //return i;
    }



    public boolean getWIPInfo(String assetID) {
        String selectQuery1 = "SELECT processing_date FROM AM_GB_COMPANY";
        String selectQuery2 = "SELECT Effective_Date FROM AM_ASSET WHERE ASSET_ID ='"+assetID+"'";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean result=false;

        java.sql.Date processDate = null;
        java.sql.Date effectiveDate = null;
        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(selectQuery1);
            rs = ps.executeQuery();

            while (rs.next()) {
               processDate = rs.getDate(1);
                System.out.println("the value of process date is >>>>> " + processDate);
            }


            ps=con.prepareStatement(selectQuery2);
            rs = ps.executeQuery();
             while (rs.next()) {
               effectiveDate = rs.getDate(1);
               System.out.println("the value of effective date is >>>>> " + effectiveDate);
            }

            if(effectiveDate.before(processDate))result = true;

        } catch (Exception e) {
            System.out.println("INFO:Error in getWIPInfo() of WIPAssetManager  ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return result;

    }


     public void updateDepRateFuture(String assetID) {



        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int i = 0;

        String updateQuery = "update am_asset set dep_rate = c.dep_rate from am_asset a," +
                " am_ad_category c where a.category_code = c.category_code and a.asset_id ='"+assetID+"'";

        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(updateQuery);
            //ps.setString(1, assetID);


            i = ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("INFO:Error in updateDepRateFuture() of WIPAssetManager ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        //return i;
    }


      public void updateTotalLifeFuture(String assetID) {



        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int i = 0;

        String updateQuery = "update am_asset set Total_life = 100/dep_rate * 12 where asset_id= ?";



        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(updateQuery);
            ps.setString(1, assetID);


            i = ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("INFO:Error in updateTotalLifeFuture() of WIPAssetManager ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        //return i;
    }


public void updateDepEndDateFuture(String assetID) {



        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int i = 0;

        String updateQuery1 = "update  am_asset set  dep_end_date  =   dateadd ( mm , total_life , effective_date )where asset_id = ?";

        String updateQuery2 = "update  am_asset set  dep_end_date  =   dateadd ( dd ,-1 , dep_end_date ) where asset_id = ?";

        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(updateQuery1);
            ps.setString(1, assetID);
            i = ps.executeUpdate();

            if (i > 0) {
                ps = con.prepareStatement(updateQuery2);
                ps.setString(1, assetID);
                i = ps.executeUpdate();
            }

        } catch (Exception e) {
            System.out.println("INFO:Error in updateDepEndDateFuture() of WIPAssetManager ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        //return i;
    }

public void updateNBVFuture(String assetID) {



        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int i = 0;

        String updateQuery = "update am_asset set NBV = cost_price where asset_id= ?";



        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(updateQuery);
            ps.setString(1, assetID);


            i = ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("INFO:Error in updateNBVFuture() of WIPAssetManager ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        //return i;
    }

public void updateRemainingLifeFuture(String assetID) {



        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int i = 0;

        String updateQuery = "update am_asset set remaining_life = Total_life where asset_id = ?";



        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(updateQuery);
            ps.setString(1, assetID);


            i = ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("INFO:Error in updateRemainingLifeFuture() of WIPAssetManager ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        //return i;
    }



public String getPurchaseDate(String assetID) {
        String selectQuery1 = "SELECT Date_purchased FROM AM_ASSET WHERE ASSET_ID ='"+assetID+"'";
       // String selectQuery2 = "SELECT Effective_Date FROM AM_ASSET WHERE ASSET_ID ='"+assetID+"'";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String result="";


        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(selectQuery1);
            rs = ps.executeQuery();

            while (rs.next()) {
               result = formatDate(rs.getDate(1))  ;
                //System.out.println("the value of process date is >>>>> " + result);
            }




        } catch (Exception e) {
            System.out.println("INFO:Error in getWIPInfo() of WIPAssetManager  ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return result;

    }


public void updatePostRejectWIPInfo(String assetID) {



        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int i = 0;

        String updateQuery = "update am_asset set effective_date = posting_date,dep_rate = 0.00,"+
                "last_dep_date  =  NULL,Total_life = 0.00,useful_life = 0.00,remaining_life= 0.00,"+
                "dep_end_date  =   posting_date,monthly_dep = 0.00,accum_dep  =  0.00,NBV = cost_price"+
                " where asset_id = ?";



        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(updateQuery);
            ps.setString(1, assetID);


            i = ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("INFO:Error in updatePostRejectWIPInfo() of WIPAssetManager ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        //return i;
    }
}
