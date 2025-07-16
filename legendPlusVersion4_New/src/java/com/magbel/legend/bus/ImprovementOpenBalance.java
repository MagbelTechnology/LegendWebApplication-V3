/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magbel.legend.bus;

//import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import magma.net.dao.MagmaDBConnection;
import magma.AnnualReportBean;

/**
 *
 * @author Ganiyu
 */
public class ImprovementOpenBalance extends MagmaDBConnection {

    public ImprovementOpenBalance() {
        super();
    }

    public boolean runImprovement(String startDate, String endDate) {
        Connection con = null;
        PreparedStatement ps = null;
        int SumCost=0;
        String code="legendPlus";
        //PreparedStatement ps = null;
         Statement stmt = null;
        ResultSet rs = null;
        boolean outcome = false;
        String sql = "select sum(a.cost_price) improveCostOpen,d.class_code from am_asset_improvement a,am_ad_category b,am_ad_category_class d where revalue_date between '" + startDate + "' and '" + endDate + "'"+
        "and a.category_code = b.category_code and d.class_status = 'ACTIVE'"+
        "and b.category_class = d.class_id and approval_status = 'ACTIVE'"+
        "and d.class_code = '1' group by d.class_code,d.class_name";
//        String sql = "EXEC msp_fixed_assets_schedule'" + startDate + "','" + endDate + "'";
//        System.out.println("\n\n calling runStoredProcedure() with this stored procedure " +sql);
        try {
            con = getConnection("legendPlus");
             stmt = con.createStatement();
          stmt.execute(sql);
          SumCost = rs.getInt(1);
          code = rs.getString(2);
          System.out.println("====SumCost===>> "+SumCost);
          System.out.println("====code===>> "+code);          
          String sql1 = "Update cost_fixed_asset_schedule set cost_open_bal = "+SumCost+" where class_code='"+code+"'";
          ps = con.prepareStatement(sql);
          rs = ps.executeQuery();
        } catch (Exception ex) {

            System.out.println("Error occurred in runStoredProcedure() of ReportManager  >>" +ex);
        } finally {
        closeConnection(con,stmt,rs);
        }

return outcome;
    }



}
