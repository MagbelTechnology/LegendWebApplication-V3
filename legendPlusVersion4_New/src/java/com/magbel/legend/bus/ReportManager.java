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
import java.util.Date;

import magma.net.dao.MagmaDBConnection;
import magma.AnnualReportBean;
import magma.AssetRecordsBean;

import com.magbel.util.HtmlUtility;
/**
 *Insertfixed_asset_schedule_archive
 * @author Ganiyu
 */
public class ReportManager extends MagmaDBConnection {

	HtmlUtility htmlUtil = new HtmlUtility();
    public ReportManager() { 
        super();
    }

    public boolean runStoredProcedure(String branchCode,String startDate, String endDate,String yearEnd,String reportType) {
        Connection con = null;
        //PreparedStatement ps = null;
         Statement stmt = null;
        ResultSet rs = null;
        boolean outcome = false;  
        String sql = "";
       
//        System.out.println("===yearEnd in runStoredProcedure:"+yearEnd+"     reportType: "+reportType);
        if(reportType.equals("BankWide")) {     
//        	System.out.println("===reportType in BankWide: "+reportType+"   BranchCode: "+branchCode);
//        if(yearEnd.equalsIgnoreCase("Y")){
//        	sql = "EXEC msp_assets_YearEnd_schedule'" + startDate + "','" + endDate + "'";
//        }else{
//        	sql = "EXEC msp_fixed_assets_schedule'" + startDate + "','" + endDate + "'";
//        	}
        	System.out.println("===reportType in BankWide: "+reportType+"   BranchCode: "+branchCode+" startDate: "+startDate+"  endDate: "+endDate);
        	sql = "EXEC msp_fixed_assets_schedule'" + startDate + "','" + endDate + "'";     	
        }
        if(reportType.equals("ByBranch")) {
//        	System.out.println("===reportType in ByBranch: "+reportType+"   BranchCode: "+branchCode+"     startDate: "+startDate+"  endDate: "+endDate);
 //       if(yearEnd.equalsIgnoreCase("N")){
        	sql = "EXEC msp_fixed_assets_schedule_ByBranch'" + branchCode + "','" + startDate + "','" + endDate + "'";
 //       }    
        }
//        System.out.println("===>>>>>>>>>sql: "+sql);
//        System.out.println("\n\n calling runStoredProcedure() with this stored procedure " +sql);
        try {
            con = getConnection("legendPlus");
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

public ArrayList getReportBean(String reportType){
ArrayList list = new ArrayList();
AnnualReportBean arb = null;//new AnnualReportBean();
String sql = "";
String q = "update fixed_asset_schedule set nbv_open_bal = (cost_open_bal-Dep_open_bal)";
htmlUtil.updateAssetStatusChange(q);        
if(reportType.equals("BankWide")) {  
 sql = "select * from fixed_asset_schedule order by class_code";
}
if(reportType.equals("ByBranch")) {  
	 sql = "select * from fixed_asset_schedule_ByBranch order by class_code";
	}
Connection con = null;
       PreparedStatement ps = null;

        ResultSet rs = null;

try{
 con = getConnection("legendPlus");
 ps = con.prepareStatement(sql);
 rs = ps.executeQuery();
while(rs.next()){
arb = new AnnualReportBean();
arb.setClassCode(rs.getString("class_code"));
arb.setClassName(rs.getString("class_name"));
arb.setCost_open_bal(rs.getString("cost_open_bal"));
arb.setCost_disposal(rs.getString("cost_disposal"));
arb.setCost_TransferFrom(rs.getString("cost_TransferFrom"));
arb.setCost_TransferTo(rs.getString("cost_TransferTo"));
arb.setCost_additions(rs.getString("cost_additions"));
arb.setCost_reclass(rs.getString("cost_reclass"));
arb.setCost_CloseAsset(rs.getString("cost_CloseAsset"));
arb.setDep_open_bal(rs.getString("dep_open_bal"));
arb.setDep_charge(rs.getString("dep_charge"));
arb.setDep_disposal(rs.getString("dep_disposal"));
arb.setDep_CloseAsset(rs.getString("dep_CloseAsset"));
arb.setDep_reclass(rs.getString("dep_reclass"));
//arb.setRevaluation(rs.getString("revaluation"));
arb.setImprovement(rs.getString("improvement"));
arb.setAccelerate_charge(rs.getString("Accelerate_charge"));
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
//String sql = "select cost_open_bal + improvement + cost_additions+cost_reclass + revaluation + cost_disposal"+
//        " from fixed_asset_schedule where class_code='"+classcode+"'";
String sql = "select cost_open_bal + improvement + cost_additions+cost_reclass + cost_disposal + cost_TransferFrom + cost_TransferTo + cost_CloseAsset "+
        " from fixed_asset_schedule where class_code='"+classcode+"'";

Connection con = null;
       PreparedStatement ps = null;

        ResultSet rs = null;

try{
 con = getConnection("legendPlus");
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
String sql = "select dep_open_bal + dep_charge + dep_disposal + dep_reclass + accelerate_charge + dep_CloseAsset "+
        " from fixed_asset_schedule where class_code='"+classcode+"'";

Connection con = null;
       PreparedStatement ps = null;

        ResultSet rs = null;

try{
 con = getConnection("legendPlus");
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

public boolean runImprovement(String startDate, String endDate) {
    Connection con = null;
    PreparedStatement ps = null;
    int SumCost=0;
    String code="";
    PreparedStatement ds = null;
     Statement stmt = null;
    ResultSet rs = null;
    boolean ks = false;
    boolean outcome = false;
    String sql = "select sum(a.cost_price) improveCostOpen,d.class_code from am_asset_improvement a,am_ad_category b,am_ad_category_class d where revalue_date between '" + startDate + "' and '" + endDate + "'"+
    "and a.category_code = b.category_code and d.class_status = 'ACTIVE'"+
    "and b.category_class = d.class_id and approval_status = 'ACTIVE'"+
    "group by d.class_code,d.class_name";
//    String sql = "EXEC msp_fixed_assets_schedule'" + startDate + "','" + endDate + "'";
    System.out.println("\n\n calling runImprovement() with this stored procedure " +sql);
    try {
    //    System.out.println("====Inside===>> ");
        con = getConnection("legendPlus");
        ps = con.prepareStatement(sql);
        rs = ps.executeQuery();
       while(rs.next()){
    	   SumCost = rs.getInt(1);
    	   code = rs.getString(2);
           updatefixed_asset_schedule();
       }        
/*      String sql1 = "Update fixed_asset_schedule set cost_open_bal = ? where class_code='"+code+"'";
      System.out.println("====sql1===>> "+sql1);      
      ds = con.prepareStatement(sql1);
      ks = ds.execute(sql1);
      ds.setInt(1, SumCost);  
      ds.execute();
      System.out.println("====sql2===>> "+sql1);  */
    } catch (Exception ex) {  
  
        System.out.println("Error occurred in runImprovement() of ReportManager  >>" +ex);
    } finally {
    closeConnection(con,stmt,rs);
    }
    

return outcome;
}

public boolean updatefixed_asset_schedule () 
{ 
	Connection con = null;
    PreparedStatement ps = null;
    boolean done = false;   
    //String query = "Update fixed_asset_schedule set cost_open_bal =  cost_open_bal - "+SumCost+" where class_code='"+code+"'";
    //String query = "update fixed_asset_schedule set cost_open_bal = (cost_open_bal - (cost_additions+cost_reclass+improvement+revaluation))-cost_disposal, dep_open_bal = (dep_open_bal-(dep_charge +dep_reclass))-dep_disposal";
    String query = "update fixed_asset_schedule set cost_open_bal = (cost_open_bal - (cost_additions+cost_reclass+improvement))-cost_disposal-cost_CloseAsset, dep_open_bal = (dep_open_bal-(dep_charge +dep_reclass))-dep_disposal - dep_CloseAsset";
       try { 
    	con = getConnection("legendPlus");    	   
        ps = con.prepareStatement(query); 
        done = (ps.executeUpdate() != -1);
  //      System.out.println("====sql2===>> "+query);        
    } catch (Exception e) {
        System.out.println("WARNING:Error executing Query updatefixed_asset_schedule ->" + e.getMessage());
    } finally {
        closeConnection(con, ps);
    }
    return done;
  }

public ArrayList getConsolidateReportBean(String MonthValu,String YearValu){
ArrayList list = new ArrayList();
AnnualReportBean arb = null;//new AnnualReportBean();
String sql = "select * from fixed_asset_schedule_archive where month_val = '"+MonthValu+"' and year_val = '"+YearValu+"' order by class_code";

System.out.println("==== sql in getConsolidateReportBean: "+sql);
Connection con = null;
       PreparedStatement ps = null;

        ResultSet rs = null;
//System.out.println("Matanmi sql:  "+sql);
try{
 con = getConnection("legendPlus");
 ps = con.prepareStatement(sql);
 rs = ps.executeQuery();
while(rs.next()){
arb = new AnnualReportBean();
arb.setClassCode(rs.getString("class_code"));
arb.setClassName(rs.getString("class_name"));
arb.setCost_open_bal(rs.getString("cost_open_bal"));
arb.setCost_disposal(rs.getString("cost_disposal"));
arb.setCost_TransferFrom(rs.getString("cost_TransferFrom"));
arb.setCost_TransferTo(rs.getString("cost_TransferTo"));
arb.setCost_CloseAsset(rs.getString("cost_CloseAsset"));
arb.setCost_additions(rs.getString("cost_additions"));
arb.setCost_reclass(rs.getString("cost_reclass"));
arb.setDep_open_bal(rs.getString("dep_open_bal"));
arb.setDep_charge(rs.getString("dep_charge"));
arb.setDep_disposal(rs.getString("dep_disposal"));
arb.setDep_reclass(rs.getString("dep_reclass"));
arb.setDep_CloseAsset(rs.getString("dep_CloseAsset"));
arb.setImprovement(rs.getString("improvement"));
arb.setAccelerate_charge(rs.getString("Accelerate_charge"));
arb.setNbv_open_bal(rs.getString("nbv_open_bal"));
arb.setNbv_closing_bal(rs.getString("nbv_closing_bal"));
//arb.setCost_additionsGroupAssets(rs.getString("cost_additionsGroupAssets"));
//arb.setCost_additionsImprovement(rs.getString("cost_additionsImprovement"));
//arb.setCost_additionsNewAsset(rs.getString("cost_additionsNewAsset"));
//arb.setCost_additionsPartPayment(rs.getString("cost_additionsPartPayment"));
list.add(arb);
}

}catch(Exception ex){
    System.out.println("Error occurred in getConsolidateReportBean() of ReportManager  >>" +ex);
}finally{
  closeConnection(con,ps,rs);
}

return list;

}

public String getConsolidatedDepBalance(String classcode){
String ans="";
//System.out.println("getConsolidatedDepBalance Class Code: "+classcode);
String sql = "select dep_open_bal + dep_charge + dep_disposal + dep_reclass + Accelerate_charge + dep_CloseAsset"+
        " from fixed_asset_schedule_archive where class_code='"+classcode+"'";
//System.out.println("getConsolidatedDepBalance sql: "+sql);
Connection con = null;
       PreparedStatement ps = null;

        ResultSet rs = null;

try{
 con = getConnection("legendPlus");
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


public String getConsolidateCostBalance(String classcode){
String ans="";
//String sql = "select cost_open_bal + improvement + cost_additions+cost_reclass + revaluation + cost_disposal"+
//        " from fixed_asset_schedule_archive where class_code='"+classcode+"'";
String sql = "select cost_open_bal + improvement + cost_additions+cost_reclass + cost_disposal + cost_TransferFrom + cost_TransferTo + cost_CloseAsset"+
        " from fixed_asset_schedule_archive where class_code='"+classcode+"'";

Connection con = null;
       PreparedStatement ps = null;

        ResultSet rs = null;

try{
 con = getConnection("legendPlus");
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

public boolean Insertfixed_asset_schedule_archive () 
{ 
	Connection con = null;
    PreparedStatement ps = null;
    boolean done = false;   
    String ProcessingDate = htmlUtil.findObject("SELECT PROCESSING_DATE FROM am_gb_company");
    
    
    String Processdate = ProcessingDate;
    String ProcessDay = Processdate.substring(8,10);
    String ProcessMonth = Processdate.substring(5,7);
    String ProcessYear = Processdate.substring(0,4);
 //   System.out.println("Processdate: "+Processdate);
//    System.out.println("ProcessMonth: "+ProcessMonth);
//    System.out.println("ProcessDay: "+ProcessDay);
    String strnewDateMonth = "";
    System.out.println("Process Month: "+ProcessMonth.length());
	if(ProcessMonth.equalsIgnoreCase("01")){strnewDateMonth = "Jan";}
	if(ProcessMonth.equalsIgnoreCase("02")){strnewDateMonth = "Feb";}
	if(ProcessMonth.equalsIgnoreCase("03")){strnewDateMonth = "Mar";}
	if(ProcessMonth.equalsIgnoreCase("04")){strnewDateMonth = "Apr";}
	if(ProcessMonth.equalsIgnoreCase("05")){strnewDateMonth = "May";}
	if(ProcessMonth.equalsIgnoreCase("06")){strnewDateMonth = "Jun";}
	if(ProcessMonth.equalsIgnoreCase("07")){strnewDateMonth = "Jul";}
	if(ProcessMonth.equalsIgnoreCase("08")){strnewDateMonth = "Aug";}
	if(ProcessMonth.equalsIgnoreCase("09")){strnewDateMonth = "Sep";}    
	if(ProcessMonth.equalsIgnoreCase("10")){strnewDateMonth = "Oct";} 
	if(ProcessMonth.equalsIgnoreCase("11")){strnewDateMonth = "Nov";} 
	if(ProcessMonth.equalsIgnoreCase("12")){strnewDateMonth = "Dec";} 

//   String query = "INSERT INTO fixed_asset_schedule_archive SELECT MTID,class_code,start_date,end_date,class_name," +
//    		"cost_open_bal,cost_disposal,cost_additions,cost_additionsUpload,cost_additionsNoRateUpload,cost_WipReclas,cost_reclass,dep_open_bal,dep_charge,dep_disposal," +
//    		"dep_reclass,revaluation,improvement,nbv_open_bal,nbv_closing_bal," +		   
//    		"'"+ProcessDay+"','"+ProcessMonth+"','"+ProcessYear+"','"+Processdate+"'  " +
//    		" FROM fixed_asset_schedule WHERE SUBSTRING(CAST(end_date AS varchar(12)), 0, 4) = '"+strnewDateMonth+"' AND SUBSTRING(CAST(end_date AS varchar(12)), 8, 4)  = '"+ProcessYear+"' ";
   
   String query = "INSERT INTO fixed_asset_schedule_archive SELECT MTID,class_code,start_date,end_date,class_name," +
   		"cost_open_bal,cost_disposal,cost_TransferFrom,cost_TransferTo,cost_CloseAsset,cost_additions,cost_additionsUpload,cost_additionsNoRateUpload,cost_WipReclas,cost_reclass,dep_open_bal,dep_charge,dep_disposal," +
   		"dep_reclass,dep_CloseAsset,improvement,Accelerate_charge,nbv_open_bal,nbv_closing_bal," +		   
   		"'"+ProcessDay+"','"+ProcessMonth+"','"+ProcessYear+"','"+Processdate+"'  " +
   		" FROM fixed_asset_schedule WHERE SUBSTRING(CAST(end_date AS varchar(12)), 0, 4) = '"+strnewDateMonth+"' AND SUBSTRING(CAST(end_date AS varchar(12)), 8, 4)  = '"+ProcessYear+"' ";

    //String query = "Update fixed_asset_schedule set cost_open_bal =  cost_open_bal - "+SumCost+" where class_code='"+code+"'";
 //   String query = "update fixed_asset_schedule set cost_open_bal = (cost_open_bal - (cost_additions+cost_reclass+improvement+revaluation))-cost_disposal, dep_open_bal = (dep_open_bal-(dep_charge +dep_reclass))-dep_disposal";
//   System.out.println("====Insertfixed_asset_schedule_archive sql2===>> "+query);  
       try { 
    	con = getConnection("legendPlus");    	   
        ps = con.prepareStatement(query); 
        done = (ps.executeUpdate() != -1);
        
          
        
    } catch (Exception e) {
        System.out.println("WARNING:Error executing Query Insertfixed_asset_schedule_archive ->" + e.getMessage());
    } finally {
        closeConnection(con, ps);
    }
    return done;
  }


public boolean Insertfixed_asset_schedule_archivedelete () 
{ 
	Connection con = null;
    PreparedStatement ps = null;
    boolean done = false;   
    String ProcessingDate = htmlUtil.findObject("SELECT PROCESSING_DATE FROM am_gb_company");
    
    
    String Processdate = ProcessingDate;
    String ProcessDay = Processdate.substring(8,10);
    String ProcessMonth = Processdate.substring(5,7);
    String ProcessYear = Processdate.substring(0,4);


	String deletequery = "delete from  fixed_asset_schedule_archive WHERE month_val = '"+ProcessMonth+"' AND year_val  = '"+ProcessYear+"' ";
       try { 
    	con = getConnection("legendPlus");    	   
        ps = con.prepareStatement(deletequery); 
        done = (ps.executeUpdate() != -1);
        
       // System.out.println("====sql2===>> "+query);    
        
    } catch (Exception e) {
        System.out.println("WARNING:Error executing Query Insertfixed_asset_schedule_archive Delete ->" + e.getMessage());
    } finally {
        closeConnection(con, ps);
    }
    return done;
  }

}
