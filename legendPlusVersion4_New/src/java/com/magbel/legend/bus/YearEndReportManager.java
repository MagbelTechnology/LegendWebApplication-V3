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
public class YearEndReportManager extends MagmaDBConnection {

    public YearEndReportManager() {
        super();
    }

    public boolean runStoredProcedure(String startDate, String endDate) {
        Connection con = null;
        //PreparedStatement ps = null;
         Statement stmt = null;
        ResultSet rs = null;
        boolean outcome = false;
        String sql = "EXEC msp_fixed_assets_scheduleYearEnd'" + startDate + "','" + endDate + "'";
        System.out.println("\n\n calling runStoredProcedure() with this stored procedure " +sql);
        try {
            con = getConnection("");
             stmt = con.createStatement();
          stmt.execute(sql);
         outcome = true;

        } catch (Exception ex) {

            System.out.println("Error occurred in runStoredProcedure() of ReportManager  >>" +ex);
        } finally {
        closeConnection(con,stmt,rs);
        }
  
return outcome;
    }

public ArrayList getReportBean(){
ArrayList list = new ArrayList();
AnnualReportBean arb = null;//new AnnualReportBean();
String sql = "select * from fixed_asset_schedule order by class_code";
Connection con = null;
       PreparedStatement ps = null;

        ResultSet rs = null;

try{
 con = getConnection("");
 ps = con.prepareStatement(sql);
 rs = ps.executeQuery();
while(rs.next()){
arb = new AnnualReportBean();
arb.setClassCode(rs.getString("class_code"));
arb.setClassName(rs.getString("class_name"));
arb.setCost_open_bal(rs.getString("cost_open_bal"));
arb.setCost_disposal(rs.getString("cost_disposal"));
arb.setCost_additions(rs.getString("cost_additions"));
arb.setCost_reclass(rs.getString("cost_reclass"));
arb.setDep_open_bal(rs.getString("dep_open_bal"));
arb.setDep_charge(rs.getString("dep_charge"));
arb.setDep_disposal(rs.getString("dep_disposal"));
arb.setDep_reclass(rs.getString("dep_reclass"));
arb.setRevaluation(rs.getString("revaluation"));
arb.setImprovement(rs.getString("improvement"));
arb.setNbv_open_bal(rs.getString("nbv_open_bal"));
arb.setNbv_closing_bal(rs.getString("nbv_closing_bal"));
//arb.setCost_additionsGroupAssets(rs.getString("cost_additionsGroupAssets"));
//arb.setCost_additionsImprovement(rs.getString("cost_additionsImprovement"));
//arb.setCost_additionsNewAsset(rs.getString("cost_additionsNewAsset"));
//arb.setCost_additionsPartPayment(rs.getString("cost_additionsPartPayment"));
list.add(arb);
}

}catch(Exception ex){
    System.out.println("Error occurred in getReportBean() of ReportManager  >>" +ex);
}finally{
  closeConnection(con,ps,rs);
}

return list;


}


public String getCostBalance(String classcode){
String ans="";
String sql = "select cost_open_bal + improvement + cost_additions+cost_reclass + revaluation + cost_disposal"+
        " from fixed_asset_schedule where class_code='"+classcode+"'";

Connection con = null;
       PreparedStatement ps = null;

        ResultSet rs = null;

try{
 con = getConnection("");
 ps = con.prepareStatement(sql);
 rs = ps.executeQuery();
while(rs.next()){
ans = rs.getString(1);
}

}catch(Exception ex){
    System.out.println("Error occurred in getCostBalance() of ReportManager  >>" +ex);
}finally{
  closeConnection(con,ps,rs);
}

return ans;


}



public String getDepBalance(String classcode){
String ans="";
String sql = "select dep_open_bal + dep_charge + dep_disposal + dep_reclass "+
        " from fixed_asset_schedule where class_code='"+classcode+"'";

Connection con = null;
       PreparedStatement ps = null;

        ResultSet rs = null;

try{
 con = getConnection("");
 ps = con.prepareStatement(sql);
 rs = ps.executeQuery();
while(rs.next()){
ans = rs.getString(1);
}

}catch(Exception ex){
    System.out.println("Error occurred in getCostBalance() of ReportManager  >>" +ex);
}finally{
  closeConnection(con,ps,rs);
}

return ans;


}



}
