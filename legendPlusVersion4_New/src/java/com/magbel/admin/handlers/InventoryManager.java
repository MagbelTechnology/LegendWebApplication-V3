/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magbel.admin.handlers;

/**


 * @author Ganiyu
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.magbel.ia.vao.DistributionOrder;
import com.magbel.ia.vao.InventoryTotal;
import com.magbel.util.ApplicationHelper;
import com.magbel.ia.dao.PersistenceServiceDAO;
import com.magbel.legend.vao.RFID;

import magma.StockRecordsBean;
import magma.net.dao.MagmaDBConnection;
import magma.net.manager.FleetTransactManager;

import java.sql.*;

import com.magbel.util.DatetimeFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import magma.net.vao.Asset;

//import com.magbel.admin.objects.ComplaintRequisition;
//import com.magbel.admin.handlers.MailSender;





import magma.net.vao.Requisition;
import magma.net.vao.Stock;

import java.io.FileInputStream;

import com.magbel.util.DatetimeFormat;
import com.magbel.util.CurrentDateTime;
   
     
public class InventoryManager extends MagmaDBConnection {   
    FleetTransactManager tranManager;
    DatetimeFormat df;
    CurrentDateTime dfs;
    private DatetimeFormat dateFormat;
    private java.text.SimpleDateFormat sdf;
    ApplicationHelper applHelper = null;
    SimpleDateFormat timer = null;
    MagmaDBConnection mgDbCon = null;
    Requisition reqn = null;
    public InventoryManager() throws Exception {
        try {
            tranManager = new FleetTransactManager();
            sdf = new SimpleDateFormat("dd-MM-yyyy");
            df = new com.magbel.util.DatetimeFormat();
            dfs = new com.magbel.util.CurrentDateTime();
            reqn = new Requisition();

        } catch (Exception ex) {
        }
    }
    private MagmaDBConnection dbConnection = new MagmaDBConnection();
    Connection con1 = null;
       PreparedStatement ps1 = null;
       ResultSet rs1 = null;
       String JNDI = "";
       
    public ArrayList findAssetByQuery(String branchCode,String queryFilter) {
        //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
        double minCost =0;
        double maxCost =0;
        String selectQuery = "SELECT c.Batch_Id AS ReqnID, c.QUANTITY,c.REQNCOLLECTOR AS ReqnUserID,a.ASSET_ID,a.REGISTRATION_NO,a.BRANCH_ID,a.DEPT_ID,a.SECTION,a.CATEGORY_ID,a.DESCRIPTION,a.DATE_PURCHASED,a.DEP_RATE,a.ASSET_MAKE,"+
        "a.ASSET_USER,a.ASSET_MAINTENANCE,a.ACCUM_DEP,a.MONTHLY_DEP,a.COST_PRICE,a.DEP_END_DATE,a.RESIDUAL_VALUE,a.POSTING_DATE,a.RAISE_ENTRY,a.DEP_YTD,a.NBV,REVALUE_COST,"+
        "a.REMAINING_LIFE,a.TOTAL_LIFE,a.EFFECTIVE_DATE,a.ASSET_STATUS,a.asset_code,a.IMPROV_COST,a.IMPROV_NBV,a.IMPROV_ACCUMDEP,a.IMPROV_MONTHLYDEP,a.TOTAL_NBV,c.QUANTITY "+
        "from ST_STOCK a, ST_STOCK_DISTRBUTE b, am_ad_TransferRequisition c where a.BAR_CODE = b.RFID_TAG and a.asset_id = c.asset_id  " +
        "and asset_status = 'ACTIVE'  AND BRANCH_ID = '"+branchCode+"' "+
        "UNION "+
        "SELECT ReqnID, c.Quantity,c.ReqnUserID,a.ASSET_ID,a.REGISTRATION_NO,a.BRANCH_ID,a.DEPT_ID,a.SECTION,a.CATEGORY_ID,a.DESCRIPTION,a.DATE_PURCHASED,a.DEP_RATE,a.ASSET_MAKE,"+
        "a.ASSET_USER,a.ASSET_MAINTENANCE,a.ACCUM_DEP,a.MONTHLY_DEP,a.COST_PRICE,a.DEP_END_DATE,a.RESIDUAL_VALUE,a.POSTING_DATE,a.RAISE_ENTRY,a.DEP_YTD,a.NBV,REVALUE_COST,"+
        "a.REMAINING_LIFE,a.TOTAL_LIFE,a.EFFECTIVE_DATE,a.ASSET_STATUS,a.asset_code,a.IMPROV_COST,a.IMPROV_NBV,a.IMPROV_ACCUMDEP,a.IMPROV_MONTHLYDEP,a.TOTAL_NBV,c.QUANTITY "+
        "FROM ST_STOCK a, am_ad_Requisition c WHERE a.item_code = c.ItemRequested and distributedstatus = 'pending' "+
        "AND BRANCH_ID = '"+branchCode+"' "+
        queryFilter;
//        System.out.println("the value of selectQuery in findAssetByQuery is ]]]]]]]]]]]]]]]]]]" +selectQuery);      		
        Connection con = null;
        PreparedStatement ps = null;

        ResultSet rs = null;
        ArrayList list = new ArrayList();
 
        try {
            con = getConnection(JNDI);
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {
            	String batchId = rs.getString("ReqnID");
                String id = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String branchId = rs.getString("BRANCH_ID");
                String departmentId = rs.getString("DEPT_ID");
                String section = rs.getString("SECTION");
                String category = rs.getString("CATEGORY_ID");
                String description = rs.getString("DESCRIPTION");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                double depreciationRate = rs.getDouble("DEP_RATE");
                String assetMake = rs.getString("ASSET_MAKE");
                String assetUser = rs.getString("ReqnUserID");
                String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
                double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
                double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
                double cost = rs.getDouble("COST_PRICE");
                String depreciationEndDate = formatDate(rs.getDate(
                        "DEP_END_DATE"));
                double residualValue = rs.getDouble("RESIDUAL_VALUE");
                String postingDate = rs.getString("POSTING_DATE");
                String entryRaised = rs.getString("RAISE_ENTRY");
                double depreciationYearToDate = rs.getDouble("DEP_YTD");
                double nbv = rs.getDouble("NBV");
                double revalue_cost = rs.getDouble("REVALUE_COST");
                int remainingLife = rs.getInt("REMAINING_LIFE");
                int totalLife = rs.getInt("TOTAL_LIFE");
                java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
                String asset_status=rs.getString("ASSET_STATUS");
                int assetCode = rs.getInt("asset_code");
                double improvcost = rs.getDouble("IMPROV_COST");
                double improvnbv = rs.getDouble("IMPROV_NBV");
                double improveAccumdep = rs.getDouble("IMPROV_ACCUMDEP");
                double improveMonthlydep = rs.getDouble("IMPROV_MONTHLYDEP");
                double improveTotalNbv = rs.getDouble("TOTAL_NBV");
                int quantity = rs.getInt("QUANTITY");
                String ReqnUserID = rs.getString("ReqnUserID");
 
                Asset aset = new Asset(id, registrationNo, branchId,
                                       departmentId, section, category,
                                       description,
                                       datePurchased, depreciationRate,
                                       assetMake,
                                       assetUser, assetMaintenance,
                                       accumulatedDepreciation,
                                       monthlyDepreciation, cost,
                                       depreciationEndDate,
                                       residualValue, postingDate, entryRaised,
                                       depreciationYearToDate);
                aset.setGROUP_IDd(batchId);
                aset.setNbv(nbv);
                aset.setRemainLife(remainingLife);
                aset.setTotalLife(totalLife);
                aset.setEffectiveDate(effectiveDate);
                aset.setAsset_status(asset_status);
                aset.setAssetCode(assetCode);
                aset.setRevalue_cost(revalue_cost);
                aset.setImprovcost(improvcost);
                aset.setImprovnbv(improvnbv);
                aset.setImprovaccumulatedDepreciation(improveAccumdep);
                aset.setImprovmonthlyDepreciation(improveMonthlydep);
                aset.setImprovTotalNbv(improveTotalNbv);
                aset.setQuantity(quantity);
                
                list.add(aset);

                minCost =Math.min(minCost, cost);
                maxCost = Math.max(maxCost, cost);


                //getMinMaxAssetCost(minCost,maxCost);
            //    setMinCost(minCost);
             //   setMaxCost(maxCost);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching ALL Asset ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }
    
 public ArrayList findAssetByQuery(String queryFilter,String branchCode,String queryFilter2) {
//     System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
     double minCost =0;
     double maxCost =0;
/*     String filter0 = "AND CATEGORY_CODE IN ";
     if(!queryFilter.equalsIgnoreCase("")){queryFilter = filter0+queryFilter;}
     queryFilter = "";
     String selectQuery = "select *from ST_STOCK where asset_status = 'ACTIVE' AND BRANCH_CODE = '"+branchCode+"' " +
    		 queryFilter + " " + queryFilter2;
     */
//     System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter2);
     String selectQuery = "select *from ST_STOCK where asset_status = 'ACTIVE' AND BRANCH_CODE = '"+branchCode+"' " +
    		 queryFilter2;
//     System.out.println("the value of selectQuery is ]]]]]]]]]]]]]]]]]]" +selectQuery);     		
     Connection con = null;
     PreparedStatement ps = null;
 
     ResultSet rs = null;
     ArrayList list = new ArrayList();

     try {
         con = getConnection(JNDI);
         ps = con.prepareStatement(selectQuery);
         rs = ps.executeQuery();

         while (rs.next()) {

             String id = rs.getString("ASSET_ID");
             String registrationNo = rs.getString("REGISTRATION_NO");
             String branchId = rs.getString("BRANCH_ID");
             String departmentId = rs.getString("DEPT_ID");
             String section = rs.getString("SECTION");
             String category = rs.getString("CATEGORY_ID");
             String description = rs.getString("DESCRIPTION");
             String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
             double depreciationRate = rs.getDouble("DEP_RATE");
             String assetMake = rs.getString("ASSET_MAKE");
             String assetUser = rs.getString("ASSET_USER");
             String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
             double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
             double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
             double cost = rs.getDouble("COST_PRICE");
             String depreciationEndDate = formatDate(rs.getDate(
                     "DEP_END_DATE"));
             double residualValue = rs.getDouble("RESIDUAL_VALUE");
             String postingDate = rs.getString("POSTING_DATE");
             String entryRaised = rs.getString("RAISE_ENTRY");
             double depreciationYearToDate = rs.getDouble("DEP_YTD");
             double nbv = rs.getDouble("NBV");
             double revalue_cost = rs.getDouble("REVALUE_COST");
             int remainingLife = rs.getInt("REMAINING_LIFE");
             int totalLife = rs.getInt("TOTAL_LIFE");
             java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
             String asset_status=rs.getString("ASSET_STATUS");
             int assetCode = rs.getInt("asset_code");
             double improvcost = rs.getDouble("IMPROV_COST");
             double improvnbv = rs.getDouble("IMPROV_NBV");
             double improveAccumdep = rs.getDouble("IMPROV_ACCUMDEP");
             double improveMonthlydep = rs.getDouble("IMPROV_MONTHLYDEP");
             double improveTotalNbv = rs.getDouble("TOTAL_NBV");
             
             Asset aset = new Asset(id, registrationNo, branchId,
                                    departmentId, section, category,
                                    description,
                                    datePurchased, depreciationRate,
                                    assetMake,
                                    assetUser, assetMaintenance,
                                    accumulatedDepreciation,
                                    monthlyDepreciation, cost,
                                    depreciationEndDate,
                                    residualValue, postingDate, entryRaised,
                                    depreciationYearToDate);
             aset.setNbv(nbv);
             aset.setRemainLife(remainingLife);
             aset.setTotalLife(totalLife);
             aset.setEffectiveDate(effectiveDate);
             aset.setAsset_status(asset_status);
             aset.setAssetCode(assetCode);
             aset.setRevalue_cost(revalue_cost);
             aset.setImprovcost(improvcost);
             aset.setImprovnbv(improvnbv);
             aset.setImprovaccumulatedDepreciation(improveAccumdep);
             aset.setImprovmonthlyDepreciation(improveMonthlydep);
             aset.setImprovTotalNbv(improveTotalNbv);
             
             list.add(aset);

             minCost =Math.min(minCost, cost);
             maxCost = Math.max(maxCost, cost);


             //getMinMaxAssetCost(minCost,maxCost);
         //    setMinCost(minCost);
          //   setMaxCost(maxCost);
         }

     } catch (Exception e) {
         System.out.println("INFO:Error fetching ALL ST_STOCK ->" +
                            e.getMessage());
     } finally {
         closeConnection(con, ps, rs);
     }

     return list;

 }

    public ArrayList findAllInventoryTotalByQuery(String filter2,String filter){

                   String SELECT_QUERY = "SELECT DISTINCT a.item_code,b.DESCRIPTION,a.balance,a.warehouse_code,a.userid,a.balance_ltd," +
                   		"a.balance_ytd ,b.comp_code FROM ST_INVENTORY_TOTALS a,ST_INVENTORY_ITEMS b " +
                   		"WHERE b.comp_code='"+filter2+"' and a.item_code=b.item_code" +filter;
                   Connection con = null;
                   //Statement stmt = null;
                   PreparedStatement ps = null;
                   ResultSet rs = null;

                   java.util.ArrayList records = new java.util.ArrayList();
                   InventoryTotal inventoryTotals = null;
                   try{
                         con = getConnection(JNDI);
                         ps = con.prepareStatement(SELECT_QUERY);
                         rs  = ps.executeQuery();

                            while(rs.next())
                              {
                                   // String mtId = rs.getString("MTID");
                                    String itemCode = rs.getString("ITEM_CODE");
                                     int  itemBalance = rs.getInt("BALANCE");
                                     String description =rs.getString("DESCRIPTION");
                                    String warehouseCode = rs.getString("WAREHOUSE_CODE");
                                   int userId = rs.getInt("USERID");
                                  int itemBalanceLtd = rs.getInt("BALANCE_LTD");
                                  int itemBalanceYtd = rs.getInt("BALANCE_YTD");
                                                                    
                                    inventoryTotals = new  InventoryTotal("",itemCode,itemBalance,description,warehouseCode,userId,itemBalanceLtd,itemBalanceYtd);
                                    
                                    records.add(inventoryTotals);
                                    
                           }
                                    
                   }catch(Exception er){
                           System.out.println("Error RETRIEVING All findAllInventoryTotalByQuery ... ->"+er.getMessage());
                   }finally{
                           closeConnection(con,ps,rs);
                   }


                   return records ;
           }
    
 public ArrayList findAssetDisposedByQuery(String branchCode,String queryFilter) {
     //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
     double minCost =0;
     double maxCost =0;

     String selectQuery = "select *from AM_ASSET where asset_status = 'DISPOSED' and Asset_id !=''  AND BRANCH_CODE = '"+branchCode+"' " +
     					queryFilter;
//     System.out.println("the value of selectQuery in findAssetDisposedByQuery is ]]]]]]]]]]]]]]]]]]" +selectQuery);
     Connection con = null;
     PreparedStatement ps = null;

     ResultSet rs = null;
     ArrayList list = new ArrayList();

     try {
         con = getConnection(JNDI);
         ps = con.prepareStatement(selectQuery);
         rs = ps.executeQuery();

         while (rs.next()) {

             String id = rs.getString("ASSET_ID");
             String registrationNo = rs.getString("REGISTRATION_NO");
             String branchId = rs.getString("BRANCH_ID");
             String departmentId = rs.getString("DEPT_ID");
             String section = rs.getString("SECTION");
             String category = rs.getString("CATEGORY_ID");
             String description = rs.getString("DESCRIPTION");
             String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
             double depreciationRate = rs.getDouble("DEP_RATE");
             String assetMake = rs.getString("ASSET_MAKE");
             String assetUser = rs.getString("ASSET_USER");
             String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
             double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
             double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
             double cost = rs.getDouble("COST_PRICE");
             String depreciationEndDate = formatDate(rs.getDate(
                     "DEP_END_DATE"));
             double residualValue = rs.getDouble("RESIDUAL_VALUE");
             String postingDate = rs.getString("POSTING_DATE");
             String entryRaised = rs.getString("RAISE_ENTRY");
             double depreciationYearToDate = rs.getDouble("DEP_YTD");
             double nbv = rs.getDouble("NBV");
             double revalue_cost = rs.getDouble("REVALUE_COST");
             int remainingLife = rs.getInt("REMAINING_LIFE");
             int totalLife = rs.getInt("TOTAL_LIFE");
             java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
             String asset_status=rs.getString("ASSET_STATUS");
             int assetCode = rs.getInt("asset_code");
             double improvcost = rs.getDouble("IMPROV_COST");
             double improvnbv = rs.getDouble("IMPROV_NBV");
             double improveAccumdep = rs.getDouble("IMPROV_ACCUMDEP");
             double improveMonthlydep = rs.getDouble("IMPROV_MONTHLYDEP");
             double improveTotalNbv = rs.getDouble("TOTAL_NBV");
             
             Asset aset = new Asset(id, registrationNo, branchId,
                                    departmentId, section, category,
                                    description,
                                    datePurchased, depreciationRate,
                                    assetMake,
                                    assetUser, assetMaintenance,
                                    accumulatedDepreciation,
                                    monthlyDepreciation, cost,
                                    depreciationEndDate,
                                    residualValue, postingDate, entryRaised,
                                    depreciationYearToDate);
             aset.setNbv(nbv);
             aset.setRemainLife(remainingLife);
             aset.setTotalLife(totalLife);
             aset.setEffectiveDate(effectiveDate);
             aset.setAsset_status(asset_status);
             aset.setAssetCode(assetCode);
             aset.setRevalue_cost(revalue_cost);
             aset.setImprovcost(improvcost);
             aset.setImprovnbv(improvnbv);
             aset.setImprovaccumulatedDepreciation(improveAccumdep);
             aset.setImprovmonthlyDepreciation(improveMonthlydep);
             aset.setImprovTotalNbv(improveTotalNbv);
             
             list.add(aset);

             minCost =Math.min(minCost, cost);
             maxCost = Math.max(maxCost, cost);


             //getMinMaxAssetCost(minCost,maxCost);
         //    setMinCost(minCost);
          //   setMaxCost(maxCost);
         }

     } catch (Exception e) {
         System.out.println("INFO:Error fetching ALL Disposed ST_STOCK ->" +
                            e.getMessage());
     } finally {
         closeConnection(con, ps, rs);
     }

     return list;

 }

 
public ArrayList findAssetDisposedByQuery(String branchCode,String fromDate,String toDate) {
  //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
  double minCost =0;
  double maxCost =0;

  String selectQuery = "select *from AM_ASSET where asset_status = 'DISPOSED' and Asset_id !=''  "
  		+ "AND BRANCH_CODE = ?  AND Date_Disposed BETWEEN  ? AND ?";
//  System.out.println("the value of selectQuery in findAssetDisposedByQuery is ]]]]]]]]]]]]]]]]]]" +selectQuery);
  Connection con = null;
  PreparedStatement ps = null;
  
  ResultSet rs = null;
  ArrayList list = new ArrayList();

  try {
//      con = getConnection(JNDI);
//      ps = con.prepareStatement(selectQuery);
//      rs = ps.executeQuery();
      
      con = getConnection(JNDI);
      ps = con.prepareStatement(selectQuery);
      ps.setString(1, branchCode);
      ps.setString(2, fromDate);
      ps.setString(3, toDate);
      rs = ps.executeQuery();

      while (rs.next()) {

          String id = rs.getString("ASSET_ID");
          String registrationNo = rs.getString("REGISTRATION_NO");
          String branchId = rs.getString("BRANCH_ID");
          String departmentId = rs.getString("DEPT_ID");
          String section = rs.getString("SECTION");
          String category = rs.getString("CATEGORY_ID");
          String description = rs.getString("DESCRIPTION");
          String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
          double depreciationRate = rs.getDouble("DEP_RATE");
          String assetMake = rs.getString("ASSET_MAKE");
          String assetUser = rs.getString("ASSET_USER");
          String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
          double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
          double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
          double cost = rs.getDouble("COST_PRICE");
          String depreciationEndDate = formatDate(rs.getDate(
                  "DEP_END_DATE"));
          double residualValue = rs.getDouble("RESIDUAL_VALUE");
          String postingDate = rs.getString("POSTING_DATE");
          String entryRaised = rs.getString("RAISE_ENTRY");
          double depreciationYearToDate = rs.getDouble("DEP_YTD");
          double nbv = rs.getDouble("NBV");
          double revalue_cost = rs.getDouble("REVALUE_COST");
          int remainingLife = rs.getInt("REMAINING_LIFE");
          int totalLife = rs.getInt("TOTAL_LIFE");
          java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
          String asset_status=rs.getString("ASSET_STATUS");
          int assetCode = rs.getInt("asset_code");
          double improvcost = rs.getDouble("IMPROV_COST");
          double improvnbv = rs.getDouble("IMPROV_NBV");
          double improveAccumdep = rs.getDouble("IMPROV_ACCUMDEP");
          double improveMonthlydep = rs.getDouble("IMPROV_MONTHLYDEP");
          double improveTotalNbv = rs.getDouble("TOTAL_NBV");
          
          Asset aset = new Asset(id, registrationNo, branchId,
                                 departmentId, section, category,
                                 description,
                                 datePurchased, depreciationRate,
                                 assetMake,
                                 assetUser, assetMaintenance,
                                 accumulatedDepreciation,
                                 monthlyDepreciation, cost,
                                 depreciationEndDate,
                                 residualValue, postingDate, entryRaised,
                                 depreciationYearToDate);
          aset.setNbv(nbv);
          aset.setRemainLife(remainingLife);
          aset.setTotalLife(totalLife);
          aset.setEffectiveDate(effectiveDate);
          aset.setAsset_status(asset_status);
          aset.setAssetCode(assetCode);
          aset.setRevalue_cost(revalue_cost);
          aset.setImprovcost(improvcost);
          aset.setImprovnbv(improvnbv);
          aset.setImprovaccumulatedDepreciation(improveAccumdep);
          aset.setImprovmonthlyDepreciation(improveMonthlydep);
          aset.setImprovTotalNbv(improveTotalNbv);
          
          list.add(aset);

          minCost =Math.min(minCost, cost);
          maxCost = Math.max(maxCost, cost);


          //getMinMaxAssetCost(minCost,maxCost);
      //    setMinCost(minCost);
       //   setMaxCost(maxCost);
      }

  } catch (Exception e) {
      System.out.println("INFO:Error fetching ALL Disposed ST_STOCK ->" +
                         e.getMessage());
  } finally {
      closeConnection(con, ps, rs);
  }

  return list;

}


public ArrayList findAllStockWaitingCreationByQuery(String compCode,String filter) {
          RFID ar = null;
       ArrayList list = new ArrayList();
       String rfidTag = "";
       int mtid =0;
       int userId =0; 
       String createDate ="";
       String createDateTime="";
       String location="";
       String scannType = "";
       String scannstatus="";
   String query =
           "select MTID,RFID_TAG,LOCATION,SCANN_TYPE,SCANN_STATUS,USER_ID,CREATE_DATE,CREATE_DATETIME from ST_INVENTORY_RFID where MTID IS NOT NULL AND SCANN_STATUS = 'N' "+ filter;
   Connection con = null;
  PreparedStatement ps = null;
  ResultSet rs = null;


   try {
       con = dbConnection.getConnection("fixedasset");
                  ps = con.prepareStatement(query);
//                  ps.setInt(1, tranId);
                  rs = ps.executeQuery();

       while (rs.next()) {
           mtid = rs.getInt(1); 
           rfidTag = rs.getString(2);
           location = rs.getString(3);
           scannType = rs.getString(4);
           scannstatus = rs.getString(5);
           userId = rs.getInt(6);
           createDate=dbConnection.formatDate(rs.getDate(7));           
           createDateTime = dbConnection.formatDate(rs.getDate(8));

           
       ar = new RFID(rfidTag,mtid,createDate, location, scannstatus, createDateTime, scannType, userId,"","");
       list.add(ar);

       }

   } catch (Exception ex) {
       System.out.println("Error occurred in getPostingTime --> ");
       ex.printStackTrace();
   } finally {
       dbConnection.closeConnection(con, ps, rs);
   }

   return list;

}

public ArrayList findAcceptanceQry(String compCode,String branchCode,String filter)
{
	String query = "SELECT DISTINCT MTID,DORDER_CODE,REQN_DESCRIPTION,POSTED,STATUS,"+
            "CUSTOMER_CODE,PORDER_CODE,TRANS_DATE,SHIP_DATE,"+
            "FREIGHT_CODE,CARRIER_CODE,REQ_PERS_IDENTITY,APPROVE_OFFICER,USERID,PROJECT_CODE," +
            "REQNID, QUANTITY FROM ST_DISTRIBUTION_ORDER ";
                    
query += filter;
//System.out.println("<<<<findDistributionOrdersList query: "+query);
	ArrayList list = new ArrayList();
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	
	try {

		con = getConnection(JNDI);
		ps = con.prepareStatement(query);

		rs = ps.executeQuery();

		while (rs.next()) {

			String orderNo = rs.getString("DORDER_CODE");
            String desc = rs.getString("REQN_DESCRIPTION");
			String posted = rs.getString("POSTED");
			String status = rs.getString("STATUS");
			String customerNo = rs.getString("CUSTOMER_CODE");
			String po = rs.getString("PORDER_CODE");
			String date = formatDate(rs.getDate("TRANS_DATE"));
			String shipDate = formatDate(rs.getDate("SHIP_DATE"));
			String freight = rs.getString("FREIGHT_CODE");
			String carrier = rs.getString("CARRIER_CODE");
			String id = rs.getString("MTID");
            int userId = rs.getInt("USERID");
            int quantity = rs.getInt("QUANTITY");
            String reqPersIdentity = rs.getString("req_Pers_Identity");
            String approveOfficer = rs.getString("approve_officer");
            String projectCode = rs.getString("PROJECT_CODE");
            String reqnID = rs.getString("reqnID");
            DistributionOrder Dorder = new DistributionOrder(id,orderNo,desc,posted,status,customerNo,
            po,date,shipDate,freight,carrier,userId,reqPersIdentity,approveOfficer,projectCode,quantity);
            
            Dorder.setReqnID(reqnID);
            list.add(Dorder);

		}

	} catch (Exception e) {
		String warning = "WARNING:Error Fetching Transaction for Approval"
				+ " ->" + e.getMessage();
		System.out.println(warning);
	} finally {
		closeConnection(con, ps, rs);
	}
	      //System.out.println("the size of the arrayy list is>>>>" +list.size());
    return list;
}
 

public ArrayList findAdminRequisitionListByQry(String branchCode,String filterQry)
	{
		ArrayList _list = new ArrayList();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String rmarkQry="select distinct b.user_id AS ReqnUserID,a.Description AS itemRequested,CONVERT(VARCHAR(10),a.TRANSFER_DATE,101) AS ReqnDate,a.Batch_id AS ReqnID, "+
						"b.transaction_id,a.PROCESS_STATUS AS status from am_ad_TransferRequisition a,am_asset_approval b "+ 
						"where (a.Batch_id = b.asset_id) and a.process_status='A'";
//		System.out.println("rmarkQry >>> " + rmarkQry);
		 
		try 
		{  
			
			con = getConnection(JNDI);
			ps = con.prepareStatement(rmarkQry);
			rs = ps.executeQuery();
			while(rs.next())
			{
				reqn = new Requisition();
				reqn.setUserId(rs.getString("ReqnUserID"));
				reqn.setItemType(rs.getString("itemRequested"));
				reqn.setRequisitionDate(rs.getString("ReqnDate"));
				reqn.setReqnID(rs.getString("ReqnID"));
				reqn.setTranID(rs.getString("transaction_id"));
				reqn.setStatus(rs.getString("status"));
				_list.add(reqn);
			}
			
		} 
		catch (Exception e) 
		{
			System.out.println(this.getClass().getName()+ " Error  findAdminRequisitionListByQry ->" + e.getMessage());
		}
		
		finally 
		{
			closeConnection(con, ps, rs);
		}
		
		return _list;
	} 


public ArrayList findStockAwaitingCreationQry(String branchCode,String filter)
{
	String query = "select MTID,RFID_TAG,LOCATION,SCANN_TYPE,SCANN_STATUS,USER_ID,CREATE_DATE,CREATE_DATETIME,STOCK_DESCRIPTION from ST_INVENTORY_RFID  ";
	              
query += filter;  
//System.out.println("<<<<findDistributionOrdersList query: "+query);
	RFID ar = null;
	ArrayList list = new ArrayList();
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
    String rfidTag = "";
    int mtid =0;
    int userId =0; 
    String createDate ="";
    String createDateTime="";
    String location="";
    String scannType = "";
    String scannstatus="";
    String stockDescription = "";	
	try {  

		con = getConnection(JNDI);
		ps = con.prepareStatement(query);

		rs = ps.executeQuery();

	       while (rs.next()) {
	           mtid = rs.getInt(1); 
	           rfidTag = rs.getString(2);
	           location = rs.getString(3);
	           scannType = rs.getString(4);
	           scannstatus = rs.getString(5);
	           userId = rs.getInt(6);
	           createDate=dbConnection.formatDate(rs.getDate(7));           
	           createDateTime = dbConnection.formatDate(rs.getDate(8));
	           stockDescription = rs.getString(9);
	           
	       ar = new RFID(rfidTag,mtid,createDate, location, scannstatus, createDateTime, scannType, userId,stockDescription,"");
	       list.add(ar);

		}

	} catch (Exception e) {
		String warning = "WARNING:Error Fetching Transaction for Approval"
				+ " ->" + e.getMessage();
		System.out.println(warning);
	} finally {
		closeConnection(con, ps, rs);
	}
	      //System.out.println("the size of the arrayy list is>>>>" +list.size());
    return list;
}

public ArrayList findAllInventoryUnusedTagByQuery(String filter)
{
          RFID ar = null;
       ArrayList list = new ArrayList();
       String rfidTag = "";
       int mtid =0;
       int userId =0; 
       String createDate ="";
       String createDateTime="";
       String location="";
       String scannType = "";
       String scannstatus="";
       int quantity = 0;
       String itemCode = "";
       String itemType = "";
       String description = "";
       String unitCode = "";
       String process = "";
   String query =
    "select MTID,RFID_TAG,LOCATION,SCANN_TYPE,SCANN_STATUS,USER_ID,CREATE_DATE,CREATE_DATETIME,PROCESSED  "
    + " from ST_INVENTORY_NEW_RFID   where PROCESSED = 'N'   AND SCANN_STATUS='I'  "+ filter;
   Connection con = null;
  PreparedStatement ps = null;
  ResultSet rs = null;
 // System.out.println("<<<<query in findAllInventoryUnusedTagByQuery: "+query);
   try {
	   con = getConnection(JNDI);
                  ps = con.prepareStatement(query);
//                  ps.setInt(1, tranId);
                  rs = ps.executeQuery();

       while (rs.next()) {
           mtid = rs.getInt(1); 
           rfidTag = rs.getString(2);
           location = rs.getString(3);
           scannType = rs.getString(4);
           scannstatus = rs.getString(5);
           userId = rs.getInt(6);
           createDate=dbConnection.formatDate(rs.getDate(7));           
           createDateTime = dbConnection.formatDate(rs.getDate(8));
           process = rs.getString(9);

           
       ar = new RFID(rfidTag,mtid,createDate, location, scannstatus, createDateTime, scannType, userId,description,"");
       ar.setProcessed(process);
       list.add(ar);

       }

   } catch (Exception ex) {
       System.out.println("Error occurred in getPostingTime --> ");
       ex.printStackTrace();
   } finally {
       closeConnection(con, ps, rs);
   }

   return list;

}

public ArrayList findStockByQuery(String branchCode,String queryFilter) {
	 //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
	 double minCost =0;
	 double maxCost =0;
	 
	 String selectQuery = "select *from ST_STOCK  where asset_status = 'ACTIVE'  AND BRANCH_ID = '"+branchCode+"' " +
	 					queryFilter;
//	 System.out.println("the value of selectQuery in findAssetByQuery is ]]]]]]]]]]]]]]]]]]" +selectQuery);      		
	 Connection con = null;
	 PreparedStatement ps = null;

	 ResultSet rs = null;
	 ArrayList list = new ArrayList();

	 try {
	     con = getConnection(JNDI);
	     ps = con.prepareStatement(selectQuery);
	     rs = ps.executeQuery();

	     while (rs.next()) {

	         String id = rs.getString("ASSET_ID");
	         String registrationNo = rs.getString("REGISTRATION_NO");
	         String branchId = rs.getString("BRANCH_ID");
	         String departmentId = rs.getString("DEPT_ID");
	         String section = rs.getString("SECTION");
	         String category = rs.getString("CATEGORY_ID");
	         String description = rs.getString("DESCRIPTION");
	         String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
	         double depreciationRate = rs.getDouble("DEP_RATE");
	         String assetMake = rs.getString("ASSET_MAKE");
	         String assetUser = rs.getString("ASSET_USER");
	         String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
	         double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
	         double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
	         double cost = rs.getDouble("COST_PRICE");
	         String depreciationEndDate = formatDate(rs.getDate(
	                 "DEP_END_DATE"));
	         double residualValue = rs.getDouble("RESIDUAL_VALUE");
	         String postingDate = rs.getString("POSTING_DATE");
	         String entryRaised = rs.getString("RAISE_ENTRY");
	         double depreciationYearToDate = rs.getDouble("DEP_YTD");
	         double nbv = rs.getDouble("NBV");
	         double revalue_cost = rs.getDouble("REVALUE_COST");
	         int remainingLife = rs.getInt("REMAINING_LIFE");
	         int totalLife = rs.getInt("TOTAL_LIFE");
	         java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
	         String asset_status=rs.getString("ASSET_STATUS");
	         int assetCode = rs.getInt("asset_code");
	         double improvcost = rs.getDouble("IMPROV_COST");
	         double improvnbv = rs.getDouble("IMPROV_NBV");
	         double improveAccumdep = rs.getDouble("IMPROV_ACCUMDEP");
	         double improveMonthlydep = rs.getDouble("IMPROV_MONTHLYDEP");
	         double improveTotalNbv = rs.getDouble("TOTAL_NBV");
	         
	         Asset aset = new Asset(id, registrationNo, branchId,
	                                departmentId, section, category,
	                                description,
	                                datePurchased, depreciationRate,
	                                assetMake,
	                                assetUser, assetMaintenance,
	                                accumulatedDepreciation,
	                                monthlyDepreciation, cost,
	                                depreciationEndDate,
	                                residualValue, postingDate, entryRaised,
	                                depreciationYearToDate);
	         aset.setNbv(nbv);
	         aset.setRemainLife(remainingLife);
	         aset.setTotalLife(totalLife);
	         aset.setEffectiveDate(effectiveDate);
	         aset.setAsset_status(asset_status);
	         aset.setAssetCode(assetCode);
	         aset.setRevalue_cost(revalue_cost);
	         aset.setImprovcost(improvcost);
	         aset.setImprovnbv(improvnbv);
	         aset.setImprovaccumulatedDepreciation(improveAccumdep);
	         aset.setImprovmonthlyDepreciation(improveMonthlydep);
	         aset.setImprovTotalNbv(improveTotalNbv);
	         
	         list.add(aset);

	         minCost =Math.min(minCost, cost);
	         maxCost = Math.max(maxCost, cost);


	         //getMinMaxAssetCost(minCost,maxCost);
	     //    setMinCost(minCost);
	      //   setMaxCost(maxCost);
	     }

	 } catch (Exception e) {
	     System.out.println("INFO:Error fetching ALL Asset ->" +
	                        e.getMessage());
	 } finally {
	     closeConnection(con, ps, rs);
	 }

	 return list;

	}


public ArrayList findStockwithrequisitionByQuery(String branchCode,String queryFilter) {
 //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
 double minCost =0;
 double maxCost =0;
 String selectQuery = "";
if(branchCode !=""){
        selectQuery = "SELECT c.Batch_Id, c.QUANTITY,c.REQNCOLLECTOR AS ReqnUserID,a.ASSET_ID,a.REGISTRATION_NO,a.BRANCH_ID,a.DEPT_ID,a.SECTION,a.CATEGORY_ID,a.DESCRIPTION,a.DATE_PURCHASED,a.DEP_RATE,a.ASSET_MAKE,"+
        "a.ASSET_USER,a.ASSET_MAINTENANCE,a.ACCUM_DEP,a.MONTHLY_DEP,a.COST_PRICE,a.DEP_END_DATE,a.RESIDUAL_VALUE,a.POSTING_DATE,a.RAISE_ENTRY,a.DEP_YTD,a.NBV,"+
        "a.REMAINING_LIFE,a.TOTAL_LIFE,a.EFFECTIVE_DATE,a.ASSET_STATUS,a.asset_code,a.IMPROV_COST,a.IMPROV_NBV,a.IMPROV_ACCUMDEP,a.IMPROV_MONTHLYDEP,a.TOTAL_NBV,c.QUANTITY "+
        "from ST_STOCK a,am_ad_TransferRequisition c where a.asset_id = c.asset_id  " +
        "and asset_status = 'ACTIVE'  AND BRANCH_ID = '"+branchCode+"' "+
        "UNION "+
        "SELECT ReqnID, c.Quantity,ReqnUserID,a.ASSET_ID,a.REGISTRATION_NO,a.BRANCH_ID,a.DEPT_ID,a.SECTION,a.CATEGORY_ID,a.DESCRIPTION,a.DATE_PURCHASED,a.DEP_RATE,a.ASSET_MAKE,"+
        "a.ASSET_USER,a.ASSET_MAINTENANCE,a.ACCUM_DEP,a.MONTHLY_DEP,a.COST_PRICE,a.DEP_END_DATE,a.RESIDUAL_VALUE,a.POSTING_DATE,a.RAISE_ENTRY,a.DEP_YTD,a.NBV,"+
        "a.REMAINING_LIFE,a.TOTAL_LIFE,a.EFFECTIVE_DATE,a.ASSET_STATUS,a.asset_code,a.IMPROV_COST,a.IMPROV_NBV,a.IMPROV_ACCUMDEP,a.IMPROV_MONTHLYDEP,a.TOTAL_NBV,c.QUANTITY "+
        "FROM ST_STOCK a, am_ad_Requisition c WHERE a.item_code = c.ItemRequested and distributedstatus = 'pending' "+
        "AND BRANCH_ID = '"+branchCode+"'  "+
        queryFilter;
        }  
if(branchCode ==""){      
		selectQuery = "SELECT c.Batch_Id AS ReqnID, c.QUANTITY,c.REQNCOLLECTOR AS ReqnUserID,a.ASSET_ID,a.REGISTRATION_NO,a.BRANCH_ID,a.DEPT_ID,a.SECTION,a.CATEGORY_ID,a.DESCRIPTION,a.DATE_PURCHASED,a.DEP_RATE,a.ASSET_MAKE,"+
        "a.ASSET_USER,a.ASSET_MAINTENANCE,a.ACCUM_DEP,a.MONTHLY_DEP,a.COST_PRICE,a.DEP_END_DATE,a.RESIDUAL_VALUE,a.POSTING_DATE,a.RAISE_ENTRY,a.DEP_YTD,a.NBV,REVALUE_COST,"+
        "a.REMAINING_LIFE,a.TOTAL_LIFE,a.EFFECTIVE_DATE,a.ASSET_STATUS,a.asset_code,a.IMPROV_COST,a.IMPROV_NBV,a.IMPROV_ACCUMDEP,a.IMPROV_MONTHLYDEP,a.TOTAL_NBV,c.QUANTITY "+
        "from ST_STOCK a, am_ad_TransferRequisition c where a.asset_id = c.asset_id  " +
        "and a.asset_status = 'ACTIVE' AND c.PROCESS_STATUS = 'N' AND a.QUANTITY > 0 "+
        "UNION "+
        "SELECT ReqnID, c.Quantity,ReqnUserID,a.ASSET_ID,a.REGISTRATION_NO,a.BRANCH_ID,a.DEPT_ID,a.SECTION,a.CATEGORY_ID,a.DESCRIPTION,a.DATE_PURCHASED,a.DEP_RATE,a.ASSET_MAKE,"+
        "a.ASSET_USER,a.ASSET_MAINTENANCE,a.ACCUM_DEP,a.MONTHLY_DEP,a.COST_PRICE,a.DEP_END_DATE,a.RESIDUAL_VALUE,a.POSTING_DATE,a.RAISE_ENTRY,a.DEP_YTD,a.NBV,a.REVALUE_COST,"+
        "a.REMAINING_LIFE,a.TOTAL_LIFE,a.EFFECTIVE_DATE,a.ASSET_STATUS,a.asset_code,a.IMPROV_COST,a.IMPROV_NBV,a.IMPROV_ACCUMDEP,a.IMPROV_MONTHLYDEP,a.TOTAL_NBV,c.QUANTITY "+
        "FROM ST_STOCK a, am_ad_Requisition c WHERE a.item_code = c.ItemRequested and distributedstatus = 'pending' AND a.QUANTITY > 0 "+	
		queryFilter;}
// System.out.println("the value of selectQuery in findAssetByQuery is ]]]]]]]]]]]]]]]]]]" +selectQuery);      		
 Connection con = null;
 PreparedStatement ps = null;

 ResultSet rs = null;
 ArrayList list = new ArrayList();

 try {
     con = getConnection(JNDI);
     ps = con.prepareStatement(selectQuery);
     rs = ps.executeQuery();

     while (rs.next()) {
    	 String batchId = rs.getString("ReqnID");
         String id = rs.getString("ASSET_ID");
         String registrationNo = rs.getString("REGISTRATION_NO");
         String branchId = rs.getString("BRANCH_ID");
         String departmentId = rs.getString("DEPT_ID");
         String section = rs.getString("SECTION");
         String category = rs.getString("CATEGORY_ID");
         String description = rs.getString("DESCRIPTION");
         String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
         double depreciationRate = rs.getDouble("DEP_RATE");
         String assetMake = rs.getString("ASSET_MAKE");
         String assetUser = rs.getString("ReqnUserID");
         String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
         double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
         double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
         double cost = rs.getDouble("COST_PRICE");
         String depreciationEndDate = formatDate(rs.getDate(
                 "DEP_END_DATE"));
         double residualValue = rs.getDouble("RESIDUAL_VALUE");
         String postingDate = rs.getString("POSTING_DATE");
         String entryRaised = rs.getString("RAISE_ENTRY");
         double depreciationYearToDate = rs.getDouble("DEP_YTD");
         double nbv = rs.getDouble("NBV");
         double revalue_cost = rs.getDouble("REVALUE_COST");
         int remainingLife = rs.getInt("REMAINING_LIFE");
         int totalLife = rs.getInt("TOTAL_LIFE");
         java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
         String asset_status=rs.getString("ASSET_STATUS");
         int assetCode = rs.getInt("asset_code");
         double improvcost = rs.getDouble("IMPROV_COST");
         double improvnbv = rs.getDouble("IMPROV_NBV");
         double improveAccumdep = rs.getDouble("IMPROV_ACCUMDEP");
         double improveMonthlydep = rs.getDouble("IMPROV_MONTHLYDEP");
         double improveTotalNbv = rs.getDouble("TOTAL_NBV");
         int quantity = rs.getInt("QUANTITY");
//         System.out.println("<<<<<====assetUser: "+assetUser);
         Asset aset = new Asset(id, registrationNo, branchId,
                                departmentId, section, category,
                                description,
                                datePurchased, depreciationRate,
                                assetMake,
                                assetUser, assetMaintenance,
                                accumulatedDepreciation,
                                monthlyDepreciation, cost,
                                depreciationEndDate,
                                residualValue, postingDate, entryRaised,
                                depreciationYearToDate);
         aset.setGROUP_IDd(batchId);
         aset.setNbv(nbv);
         aset.setRemainLife(remainingLife);
         aset.setTotalLife(totalLife);
         aset.setEffectiveDate(effectiveDate);
         aset.setAsset_status(asset_status);
         aset.setAssetCode(assetCode);
         aset.setRevalue_cost(revalue_cost);
         aset.setImprovcost(improvcost);
         aset.setImprovnbv(improvnbv);
         aset.setImprovaccumulatedDepreciation(improveAccumdep);
         aset.setImprovmonthlyDepreciation(improveMonthlydep);
         aset.setImprovTotalNbv(improveTotalNbv);
         aset.setQuantity(quantity);
         list.add(aset);

         minCost =Math.min(minCost, cost);
         maxCost = Math.max(maxCost, cost);


         //getMinMaxAssetCost(minCost,maxCost);
     //    setMinCost(minCost);
      //   setMaxCost(maxCost);
     }

 } catch (Exception e) {
     System.out.println("INFO:Error fetching ALL Stock ->" +
                        e.getMessage());
 } finally {
     closeConnection(con, ps, rs);
 }

 return list;

}

public ArrayList findStockRequestedListByQry(String branchCode,String filterQry)
	{
		ArrayList _list = new ArrayList();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String rmarkQry="select distinct b.user_id AS ReqnUserID,a.Description AS itemRequested,CONVERT(VARCHAR(10),a.TRANSFER_DATE,101) AS ReqnDate,a.Batch_id AS ReqnID, "+
						"b.transaction_id,a.PROCESS_STATUS AS status from am_ad_TransferRequisition a,am_asset_approval b "+ 
						"where (a.Batch_id = b.asset_id) and a.process_status='P'";
//		System.out.println("rmarkQry >>> " + rmarkQry);
		 
		try 
		{  
			
			con = getConnection(JNDI);
			ps = con.prepareStatement(rmarkQry);
			rs = ps.executeQuery();
			while(rs.next())
			{
				reqn = new Requisition();
				reqn.setUserId(rs.getString("ReqnUserID"));
				reqn.setItemType(rs.getString("itemRequested"));
				reqn.setRequisitionDate(rs.getString("ReqnDate"));
				reqn.setReqnID(rs.getString("ReqnID"));
				reqn.setTranID(rs.getString("transaction_id"));
				reqn.setStatus(rs.getString("status"));
				_list.add(reqn);
			}
			
		} 
		catch (Exception e) 
		{
			System.out.println(this.getClass().getName()+ " Error  findAdminRequisitionListByQry ->" + e.getMessage());
		}
		
		finally 
		{
			closeConnection(con, ps, rs);
		}
		
		return _list;
	} 

public ArrayList findAssetTotalByQuery(String branchCode,String queryFilter) {
 //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
 double minCost =0;
 double maxCost =0;
 String selectQuery = "select *from AM_ASSET where asset_status = 'ACTIVE' AND BRANCH_CODE = ? " +
 queryFilter;
// System.out.println("the value of selectQuery in findAssetTotalByQuery is ]]]]]]]]]]]]]]]]]]" +selectQuery);      		
 Connection con = null;
 PreparedStatement ps = null;
   
 ResultSet rs = null;
 ArrayList list = new ArrayList();

 try {
//     con = getConnection(JNDI);
//     ps = con.prepareStatement(selectQuery);
//     rs = ps.executeQuery();

     con = getConnection(JNDI);
     ps = con.prepareStatement(selectQuery);
     ps.setString(1, branchCode);
     rs = ps.executeQuery();
     while (rs.next()) {
     	String batchId = rs.getString("GROUP_ID");
         String id = rs.getString("ASSET_ID");
         String registrationNo = rs.getString("REGISTRATION_NO");
         String branchId = rs.getString("BRANCH_ID");
         String departmentId = rs.getString("DEPT_ID");
         String section = rs.getString("SECTION");
         String category = rs.getString("CATEGORY_ID");
         String description = rs.getString("DESCRIPTION");
         String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
         double depreciationRate = rs.getDouble("DEP_RATE");
         String assetMake = rs.getString("ASSET_MAKE");
         String assetUser = rs.getString("Asset_User");
         String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
         double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
         double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
         double cost = rs.getDouble("COST_PRICE");
         String depreciationEndDate = formatDate(rs.getDate(
                 "DEP_END_DATE"));
         double residualValue = rs.getDouble("RESIDUAL_VALUE");
         String postingDate = rs.getString("POSTING_DATE");
         String entryRaised = rs.getString("RAISE_ENTRY");
         double depreciationYearToDate = rs.getDouble("DEP_YTD");
         double nbv = rs.getDouble("NBV");
         double revalue_cost = rs.getDouble("REVALUE_COST");
         int remainingLife = rs.getInt("REMAINING_LIFE");
         int totalLife = rs.getInt("TOTAL_LIFE");
         java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
         String asset_status=rs.getString("ASSET_STATUS");
         int assetCode = rs.getInt("asset_code");
         double improvcost = rs.getDouble("IMPROV_COST");
         double improvnbv = rs.getDouble("IMPROV_NBV");
         double improveAccumdep = rs.getDouble("IMPROV_ACCUMDEP");
         double improveMonthlydep = rs.getDouble("IMPROV_MONTHLYDEP");
         double improveTotalNbv = rs.getDouble("TOTAL_NBV");
         int quantity = rs.getInt("QUANTITY");
//         String ReqnUserID = rs.getString("ReqnUserID");

         Asset aset = new Asset(id, registrationNo, branchId,
                                departmentId, section, category,
                                description,
                                datePurchased, depreciationRate,
                                assetMake,
                                assetUser, assetMaintenance,
                                accumulatedDepreciation,
                                monthlyDepreciation, cost,
                                depreciationEndDate,
                                residualValue, postingDate, entryRaised,
                                depreciationYearToDate);
         aset.setGROUP_IDd(batchId);
         aset.setNbv(nbv);
         aset.setRemainLife(remainingLife);
         aset.setTotalLife(totalLife);
         aset.setEffectiveDate(effectiveDate);
         aset.setAsset_status(asset_status);
         aset.setAssetCode(assetCode);
         aset.setRevalue_cost(revalue_cost);
         aset.setImprovcost(improvcost);
         aset.setImprovnbv(improvnbv);
         aset.setImprovaccumulatedDepreciation(improveAccumdep);
         aset.setImprovmonthlyDepreciation(improveMonthlydep);
         aset.setImprovTotalNbv(improveTotalNbv);
         aset.setQuantity(quantity);
         
         list.add(aset);

         minCost =Math.min(minCost, cost);
         maxCost = Math.max(maxCost, cost);


         //getMinMaxAssetCost(minCost,maxCost);
     //    setMinCost(minCost);
      //   setMaxCost(maxCost);
     }

 } catch (Exception e) {
     System.out.println("INFO:Error fetching ALL Asset in findAssetTotalByQuery ->" +
                        e.getMessage());
 } finally {
     closeConnection(con, ps, rs);
 }

 return list;

}


public ArrayList findAssetAdditionByQuery(String branchCode,String queryFilter) {
 //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
 double minCost =0;
 double maxCost =0;
 String selectQuery = "select *from AM_ASSET where asset_status = 'ACTIVE' AND BRANCH_CODE = '"+branchCode+"' " +
 queryFilter;
// System.out.println("the value of selectQuery in findAssetAdditionByQuery is ]]]]]]]]]]]]]]]]]]" +selectQuery);      		
 Connection con = null;
 PreparedStatement ps = null;

 ResultSet rs = null;     
 ArrayList list = new ArrayList();

 try {
     con = getConnection(JNDI);
     ps = con.prepareStatement(selectQuery);
     rs = ps.executeQuery();

     while (rs.next()) {
     	String batchId = rs.getString("GROUP_ID");
         String id = rs.getString("ASSET_ID");
         String registrationNo = rs.getString("REGISTRATION_NO");
         String branchId = rs.getString("BRANCH_ID");
         String departmentId = rs.getString("DEPT_ID");
         String section = rs.getString("SECTION");
         String category = rs.getString("CATEGORY_ID");
         String description = rs.getString("DESCRIPTION");
         String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
         double depreciationRate = rs.getDouble("DEP_RATE");
         String assetMake = rs.getString("ASSET_MAKE");
         String assetUser = rs.getString("Asset_User");
         String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
         double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
         double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
         double cost = rs.getDouble("COST_PRICE");
         String depreciationEndDate = formatDate(rs.getDate("DEP_END_DATE"));
         double residualValue = rs.getDouble("RESIDUAL_VALUE");
         String postingDate = rs.getString("POSTING_DATE");
         String entryRaised = rs.getString("RAISE_ENTRY");
         double depreciationYearToDate = rs.getDouble("DEP_YTD");
         double nbv = rs.getDouble("NBV");
         double revalue_cost = rs.getDouble("REVALUE_COST");
         int remainingLife = rs.getInt("REMAINING_LIFE");
         int totalLife = rs.getInt("TOTAL_LIFE");
         java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
         String asset_status=rs.getString("ASSET_STATUS");
         int assetCode = rs.getInt("asset_code");
         double improvcost = rs.getDouble("IMPROV_COST");
         double improvnbv = rs.getDouble("IMPROV_NBV");
         double improveAccumdep = rs.getDouble("IMPROV_ACCUMDEP");
         double improveMonthlydep = rs.getDouble("IMPROV_MONTHLYDEP");
         double improveTotalNbv = rs.getDouble("TOTAL_NBV");
         int quantity = rs.getInt("QUANTITY");
//         String ReqnUserID = rs.getString("ReqnUserID");

         Asset aset = new Asset(id, registrationNo, branchId,
                                departmentId, section, category,
                                description,
                                datePurchased, depreciationRate,
                                assetMake,
                                assetUser, assetMaintenance,
                                accumulatedDepreciation,
                                monthlyDepreciation, cost,
                                depreciationEndDate,
                                residualValue, postingDate, entryRaised,
                                depreciationYearToDate);
         aset.setGROUP_IDd(batchId);
         aset.setNbv(nbv);
         aset.setRemainLife(remainingLife);
         aset.setTotalLife(totalLife);
         aset.setEffectiveDate(effectiveDate);
         aset.setAsset_status(asset_status);
         aset.setAssetCode(assetCode);
         aset.setRevalue_cost(revalue_cost);
         aset.setImprovcost(improvcost);
         aset.setImprovnbv(improvnbv);
         aset.setImprovaccumulatedDepreciation(improveAccumdep);
         aset.setImprovmonthlyDepreciation(improveMonthlydep);
         aset.setImprovTotalNbv(improveTotalNbv);
         aset.setQuantity(quantity);
         
         list.add(aset);

         minCost =Math.min(minCost, cost);
         maxCost = Math.max(maxCost, cost);


         //getMinMaxAssetCost(minCost,maxCost);
     //    setMinCost(minCost);
      //   setMaxCost(maxCost);
     }

 } catch (Exception e) {
     System.out.println("INFO:Error fetching ALL Asset Addition in findAssetAdditionByQuery->" +
                        e.getMessage());
 } finally {
     closeConnection(con, ps, rs);
 }

 return list;

}

public ArrayList findAssetAdditionByQuery(String branchCode,String fromDate, String toDate) {
 //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
 double minCost =0;
 double maxCost =0;
 String selectQuery = "select *from AM_ASSET where asset_status = 'ACTIVE' AND BRANCH_CODE = ? "+
 	 "AND POSTING_DATE BETWEEN ? AND ? ";
// System.out.println("the value of selectQuery in findAssetAdditionByQuery is ]]]]]]]]]]]]]]]]]]" +selectQuery);      		
 Connection con = null;
 PreparedStatement ps = null;

 ResultSet rs = null;     
 ArrayList list = new ArrayList();

 try {
//     con = getConnection(JNDI);
//     ps = con.prepareStatement(selectQuery);
//     rs = ps.executeQuery();
     
     con = getConnection(JNDI);
     ps = con.prepareStatement(selectQuery);
     ps.setString(1, branchCode);
     ps.setString(2, fromDate);
     ps.setString(3, toDate);
     rs = ps.executeQuery();

     while (rs.next()) {
     	String batchId = rs.getString("GROUP_ID");
         String id = rs.getString("ASSET_ID");
         String registrationNo = rs.getString("REGISTRATION_NO");
         String branchId = rs.getString("BRANCH_ID");
         String departmentId = rs.getString("DEPT_ID");
         String section = rs.getString("SECTION");
         String category = rs.getString("CATEGORY_ID");
         String description = rs.getString("DESCRIPTION");
         String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
         double depreciationRate = rs.getDouble("DEP_RATE");
         String assetMake = rs.getString("ASSET_MAKE");
         String assetUser = rs.getString("Asset_User");
         String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
         double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
         double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
         double cost = rs.getDouble("COST_PRICE");
         String depreciationEndDate = formatDate(rs.getDate("DEP_END_DATE"));
         double residualValue = rs.getDouble("RESIDUAL_VALUE");
         String postingDate = rs.getString("POSTING_DATE");
         String entryRaised = rs.getString("RAISE_ENTRY");
         double depreciationYearToDate = rs.getDouble("DEP_YTD");
         double nbv = rs.getDouble("NBV");
         double revalue_cost = rs.getDouble("REVALUE_COST");
         int remainingLife = rs.getInt("REMAINING_LIFE");
         int totalLife = rs.getInt("TOTAL_LIFE");
         java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
         String asset_status=rs.getString("ASSET_STATUS");
         int assetCode = rs.getInt("asset_code");
         double improvcost = rs.getDouble("IMPROV_COST");
         double improvnbv = rs.getDouble("IMPROV_NBV");
         double improveAccumdep = rs.getDouble("IMPROV_ACCUMDEP");
         double improveMonthlydep = rs.getDouble("IMPROV_MONTHLYDEP");
         double improveTotalNbv = rs.getDouble("TOTAL_NBV");
         int quantity = rs.getInt("QUANTITY");
//         String ReqnUserID = rs.getString("ReqnUserID");

         Asset aset = new Asset(id, registrationNo, branchId,
                                departmentId, section, category,
                                description,
                                datePurchased, depreciationRate,
                                assetMake,
                                assetUser, assetMaintenance,
                                accumulatedDepreciation,
                                monthlyDepreciation, cost,
                                depreciationEndDate,
                                residualValue, postingDate, entryRaised,
                                depreciationYearToDate);
         aset.setGROUP_IDd(batchId);
         aset.setNbv(nbv);
         aset.setRemainLife(remainingLife);
         aset.setTotalLife(totalLife);
         aset.setEffectiveDate(effectiveDate);
         aset.setAsset_status(asset_status);
         aset.setAssetCode(assetCode);
         aset.setRevalue_cost(revalue_cost);
         aset.setImprovcost(improvcost);
         aset.setImprovnbv(improvnbv);
         aset.setImprovaccumulatedDepreciation(improveAccumdep);
         aset.setImprovmonthlyDepreciation(improveMonthlydep);
         aset.setImprovTotalNbv(improveTotalNbv);
         aset.setQuantity(quantity);
         
         list.add(aset);

         minCost =Math.min(minCost, cost);
         maxCost = Math.max(maxCost, cost);


         //getMinMaxAssetCost(minCost,maxCost);
     //    setMinCost(minCost);
      //   setMaxCost(maxCost);
     }

 } catch (Exception e) {
     System.out.println("INFO:Error fetching ALL Asset Addition in findAssetAdditionByQuery->" +
                        e.getMessage());
 } finally {
     closeConnection(con, ps, rs);
 }

 return list;

}


public ArrayList findAssetTransferdByQuery(String branchCode,String queryFilter) {
 //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
 double minCost =0;
 double maxCost =0;
 String selectQuery = "select distinct a.asset_id, New_Asset_user,a.Description,a.Monthly_Dep,a.Cost_Price,a.new_asset_id,a.TRANSFER_DATE,a.asset_code,a.NBV   "
 +"from am_assetTransfer a, am_Raisentry_Transaction b "
 +"where a.asset_id = b.asset_id and page1 = 'ASSET TRANSFER RAISE ENTRY' and transactionId = '21' "
 +"AND a.OLD_BRANCH_ID = "+branchCode+" "
 +queryFilter
 +" UNION "
 +"select a.asset_id, NewAsset_user AS New_Asset_user,a.Description,a.Monthly_Dep,a.Cost_Price,a.new_asset_id,a.TRANSFER_DATE,a.asset_code,a.NBV  "
 +"from am_gb_bulkTransfer a, am_raisentry_post b "
 +"where CONVERT(varchar, a.Batch_id) = CONVERT(varchar, b.Id) and b.entryPostFlag = 'Y' and b.GroupIdStatus = 'Y' "
 +"AND a.OLDBRANCH_ID = "+branchCode+" "
 +queryFilter;
// System.out.println("the value of selectQuery in findAssetTransferdByQuery is ]]]]]]]]]]]]]]]]]]" +selectQuery);      		
 Connection con = null;
 PreparedStatement ps = null;

 ResultSet rs = null;     
 ArrayList list = new ArrayList();

 try {
     con = getConnection(JNDI);
     ps = con.prepareStatement(selectQuery);
     rs = ps.executeQuery();

     while (rs.next()) {
 //    	String batchId = rs.getString("GROUP_ID");
         String id = rs.getString("ASSET_ID");
//         String registrationNo = rs.getString("REGISTRATION_NO");
//         String branchId = rs.getString("BRANCH_ID");
//         String departmentId = rs.getString("DEPT_ID");
//         String section = rs.getString("SECTION");
//         String category = rs.getString("CATEGORY_ID");
         String description = rs.getString("DESCRIPTION");
//         String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
//         double depreciationRate = rs.getDouble("DEP_RATE");
//         String assetMake = rs.getString("ASSET_MAKE");
//         String assetUser = rs.getString("Asset_User");
//         String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
//         double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
//         double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
         double cost = rs.getDouble("COST_PRICE");
//         String depreciationEndDate = formatDate(rs.getDate("DEP_END_DATE"));
      //   double residualValue = rs.getDouble("RESIDUAL_VALUE");
         String postingDate = rs.getString("TRANSFER_DATE");
      //   String entryRaised = rs.getString("RAISE_ENTRY");
      //   double depreciationYearToDate = rs.getDouble("DEP_YTD");
         double nbv = rs.getDouble("NBV");
      //   double revalue_cost = rs.getDouble("REVALUE_COST");
      //   int remainingLife = rs.getInt("REMAINING_LIFE");
      //   int totalLife = rs.getInt("TOTAL_LIFE");
      //   java.util.Date effectiveDate = rs.getDate("TRANSFER_DATE");
      //   String asset_status=rs.getString("ASSET_STATUS");
         int assetCode = rs.getInt("asset_code");
//         double improvcost = rs.getDouble("IMPROV_COST");
//         double improvnbv = rs.getDouble("IMPROV_NBV");
//         double improveAccumdep = rs.getDouble("IMPROV_ACCUMDEP");
//         double improveMonthlydep = rs.getDouble("IMPROV_MONTHLYDEP");
//         double improveTotalNbv = rs.getDouble("TOTAL_NBV");
//         int quantity = rs.getInt("QUANTITY");
//         String ReqnUserID = rs.getString("ReqnUserID");

         Asset aset = new Asset();
         aset.setId(id);
         aset.setDescription(description);
         aset.setNbv(nbv);
         aset.setPostingDate(postingDate);
         aset.setAssetCode(assetCode);
         aset.setCost(cost);
         
         list.add(aset);

         minCost =Math.min(minCost, cost);
         maxCost = Math.max(maxCost, cost);


         //getMinMaxAssetCost(minCost,maxCost);
     //    setMinCost(minCost);
      //   setMaxCost(maxCost);
     }

 } catch (Exception e) {
     System.out.println("INFO:Error fetching ALL Asset Transfer 1 in findAssetTransferdByQuery->" +
                        e.getMessage());
 } finally {
     closeConnection(con, ps, rs);
 }
  
 return list;

}

public ArrayList findAssetTransferdByQuery(String branchCode,String fromDate, String toDate) {
 //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
 double minCost =0;
 double maxCost =0;
 String selectQuery = "select distinct a.asset_id, New_Asset_user,a.Description,a.Monthly_Dep,a.Cost_Price,a.new_asset_id,a.TRANSFER_DATE,a.asset_code,a.NBV   "
 +"from am_assetTransfer a, am_Raisentry_Transaction b "
 +"where a.asset_id = b.asset_id and page1 = 'ASSET TRANSFER RAISE ENTRY' and transactionId = '21' "
 +"AND a.BRANCH_CODE = ? AND a.TRANSFER_DATE BETWEEN ? AND ?"
 +" UNION "
 +"select a.asset_id, NewAsset_user AS New_Asset_user,a.Description,a.Monthly_Dep,a.Cost_Price,a.new_asset_id,a.TRANSFER_DATE,a.asset_code,a.NBV  "
 +"from am_gb_bulkTransfer a, am_raisentry_post b "
 +"where CONVERT(varchar, a.Batch_id) = CONVERT(varchar, b.Id) and b.entryPostFlag = 'Y' and b.GroupIdStatus = 'Y' "
 +"AND a.BRANCH_CODE = ? AND a.TRANSFER_DATE BETWEEN ? AND ? ";
// System.out.println("the value of selectQuery in findAssetTransferdByQuery is ]]]]]]]]]]]]]]]]]]" +selectQuery);      		
 Connection con = null;
 PreparedStatement ps = null;

 ResultSet rs = null;     
 ArrayList list = new ArrayList();

 try {
//     con = getConnection(JNDI);
//     ps = con.prepareStatement(selectQuery);
//     rs = ps.executeQuery();
     con = getConnection(JNDI);
     ps = con.prepareStatement(selectQuery);
     ps.setString(1, branchCode);
     ps.setString(2, fromDate);
     ps.setString(3, toDate);
     ps.setString(4, branchCode);
     ps.setString(5, fromDate);
     ps.setString(6, toDate);
     rs = ps.executeQuery();
	 
     while (rs.next()) {
 //    	String batchId = rs.getString("GROUP_ID");
         String id = rs.getString("ASSET_ID");
//         String registrationNo = rs.getString("REGISTRATION_NO");
//         String branchId = rs.getString("BRANCH_ID");
//         String departmentId = rs.getString("DEPT_ID");
//         String section = rs.getString("SECTION");
//         String category = rs.getString("CATEGORY_ID");
         String description = rs.getString("DESCRIPTION");
//         String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
//         double depreciationRate = rs.getDouble("DEP_RATE");
//         String assetMake = rs.getString("ASSET_MAKE");
//         String assetUser = rs.getString("Asset_User");
//         String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
//         double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
//         double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
         double cost = rs.getDouble("COST_PRICE");
//         String depreciationEndDate = formatDate(rs.getDate("DEP_END_DATE"));
      //   double residualValue = rs.getDouble("RESIDUAL_VALUE");
         String postingDate = rs.getString("TRANSFER_DATE");
      //   String entryRaised = rs.getString("RAISE_ENTRY");
      //   double depreciationYearToDate = rs.getDouble("DEP_YTD");
         double nbv = rs.getDouble("NBV");
      //   double revalue_cost = rs.getDouble("REVALUE_COST");
      //   int remainingLife = rs.getInt("REMAINING_LIFE");
      //   int totalLife = rs.getInt("TOTAL_LIFE");
      //   java.util.Date effectiveDate = rs.getDate("TRANSFER_DATE");
      //   String asset_status=rs.getString("ASSET_STATUS");
         int assetCode = rs.getInt("asset_code");
//         double improvcost = rs.getDouble("IMPROV_COST");
//         double improvnbv = rs.getDouble("IMPROV_NBV");
//         double improveAccumdep = rs.getDouble("IMPROV_ACCUMDEP");
//         double improveMonthlydep = rs.getDouble("IMPROV_MONTHLYDEP");
//         double improveTotalNbv = rs.getDouble("TOTAL_NBV");
//         int quantity = rs.getInt("QUANTITY");
//         String ReqnUserID = rs.getString("ReqnUserID");

         Asset aset = new Asset();
         aset.setId(id);
         aset.setDescription(description);
         aset.setNbv(nbv);
         aset.setPostingDate(postingDate);
         aset.setAssetCode(assetCode);
         aset.setCost(cost);
         
         list.add(aset);

         minCost =Math.min(minCost, cost);
         maxCost = Math.max(maxCost, cost);


         //getMinMaxAssetCost(minCost,maxCost);
     //    setMinCost(minCost);
      //   setMaxCost(maxCost);
     }

 } catch (Exception e) {
     System.out.println("INFO:Error fetching ALL Asset Transfer 2 in findAssetTransferdByQuery->" +
                        e.getMessage());
 } finally {
     closeConnection(con, ps, rs);
 }
  
 return list;

}

public ArrayList findAssetImprovebbByQuery(String branchCode,String queryFilter) {
 //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
 double minCost =0;
 double maxCost =0; 
 String selectQuery = "select distinct a.asset_id,a.cost_price,a.revalue_Date,a.Description,a.nbv,a.asset_code "
 +"from am_asset_improvement a, am_Raisentry_Transaction b "
 +"where a.asset_id = b.asset_id and page1 = 'ASSET IMPROVEMENT RAISE ENTRY' and transactionId = '26' "
 +"AND a.BRANCH_CODE = '"+branchCode+"' "
 +queryFilter
 +" UNION "
 +"select a.asset_id,a.cost_price,a.revalue_Date,a.Description,a.nbv,a.asset_code  "
 +"from am_asset_improvement_Upload a, am_raisentry_post b "
 +"where CONVERT(varchar, a.Revalue_ID) = CONVERT(varchar, b.Id) and b.entryPostFlag = 'Y' and b.GroupIdStatus = 'Y' "
 +"AND a.BRANCH_CODE = '"+branchCode+"' "
 +queryFilter;
// System.out.println("the value of selectQuery in findAssetImproveddByQuery is ]]]]]]]]]]]]]]]]]]" +selectQuery);      		
 Connection con = null;
 PreparedStatement ps = null;

 ResultSet rs = null;     
 ArrayList list = new ArrayList();

 try {
     con = getConnection(JNDI);
     ps = con.prepareStatement(selectQuery);
     rs = ps.executeQuery();

     while (rs.next()) {
 //    	String batchId = rs.getString("GROUP_ID");
         String id = rs.getString("ASSET_ID");
//         String registrationNo = rs.getString("REGISTRATION_NO");
//         String branchId = rs.getString("BRANCH_ID");
//         String departmentId = rs.getString("DEPT_ID");
//         String section = rs.getString("SECTION");
//         String category = rs.getString("CATEGORY_ID");
         String description = rs.getString("DESCRIPTION");
//         String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
//         double depreciationRate = rs.getDouble("DEP_RATE");
//         String assetMake = rs.getString("ASSET_MAKE");
//         String assetUser = rs.getString("Asset_User");
//         String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
//         double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
//         double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
         double cost = rs.getDouble("COST_PRICE");
//         String depreciationEndDate = formatDate(rs.getDate("DEP_END_DATE"));
      //   double residualValue = rs.getDouble("RESIDUAL_VALUE");
         String postingDate = rs.getString("revalue_Date");
      //   String entryRaised = rs.getString("RAISE_ENTRY");
      //   double depreciationYearToDate = rs.getDouble("DEP_YTD");
         double nbv = rs.getDouble("NBV");
      //   double revalue_cost = rs.getDouble("REVALUE_COST");
      //   int remainingLife = rs.getInt("REMAINING_LIFE");
      //   int totalLife = rs.getInt("TOTAL_LIFE");
      //   java.util.Date effectiveDate = rs.getDate("TRANSFER_DATE");
      //   String asset_status=rs.getString("ASSET_STATUS");
         int assetCode = rs.getInt("asset_code");
//         double improvcost = rs.getDouble("IMPROV_COST");
//         double improvnbv = rs.getDouble("IMPROV_NBV");
//         double improveAccumdep = rs.getDouble("IMPROV_ACCUMDEP");
//         double improveMonthlydep = rs.getDouble("IMPROV_MONTHLYDEP");
//         double improveTotalNbv = rs.getDouble("TOTAL_NBV");
//         int quantity = rs.getInt("QUANTITY");
//         String ReqnUserID = rs.getString("ReqnUserID");

         Asset aset = new Asset();
         aset.setId(id);
         aset.setDescription(description);
         aset.setNbv(nbv);
         aset.setPostingDate(postingDate);
         aset.setAssetCode(assetCode);
         aset.setCost(cost);
         
         list.add(aset);

         minCost =Math.min(minCost, cost);
         maxCost = Math.max(maxCost, cost);


         //getMinMaxAssetCost(minCost,maxCost);
     //    setMinCost(minCost);
      //   setMaxCost(maxCost);
     }

 } catch (Exception e) {
     System.out.println("INFO:Error fetching ALL Asset Improvement  in findAssetImprovebbByQuery->" +
                        e.getMessage());
 } finally {
     closeConnection(con, ps, rs);
 }

 return list;

}

public ArrayList findAssetImproveddByQuery(String branchCode,String fromDate, String toDate) {
 //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
	
 double minCost =0;
 double maxCost =0; 
 String selectQuery = "select distinct a.asset_id,a.cost_price,a.revalue_Date,a.Description,a.nbv,a.asset_code "
 +"from am_asset_improvement a, am_Raisentry_Transaction b "
 +"where a.asset_id = b.asset_id and page1 = 'ASSET IMPROVEMENT RAISE ENTRY' and transactionId = '26' "
 +"AND a.BRANCH_CODE = ? AND a.revalue_Date BETWEEN ? AND ?"
 //+queryFilter
 +" UNION "
 +"select a.asset_id,a.cost_price,a.revalue_Date,a.Description,a.nbv,a.asset_code  "
 +"from am_asset_improvement_Upload a, am_raisentry_post b "
 +"where CONVERT(varchar, a.Revalue_ID) = CONVERT(varchar, b.Id) and b.entryPostFlag = 'Y' and b.GroupIdStatus = 'Y' "
 +"AND a.BRANCH_CODE = ? AND a.revalue_Date BETWEEN ? AND ?";
// +queryFilter;
// System.out.println("the value of selectQuery in findAssetImproveddByQuery is ]]]]]]]]]]]]]]]]]]" +selectQuery);      		
 Connection con = null;
 PreparedStatement ps = null;  

 ResultSet rs = null;     
 ArrayList list = new ArrayList();

 try {
//     con = getConnection(JNDI);
//     ps = con.prepareStatement(selectQuery);
//     rs = ps.executeQuery();
	 
     con = getConnection(JNDI);
     ps = con.prepareStatement(selectQuery);
     ps.setString(1, branchCode);
     ps.setString(2, fromDate);
     ps.setString(3, toDate);
     ps.setString(4, branchCode);
     ps.setString(5, fromDate);
     ps.setString(6, toDate);
     rs = ps.executeQuery();
	 
     while (rs.next()) {
 //    	String batchId = rs.getString("GROUP_ID");
         String id = rs.getString("ASSET_ID");
//         String registrationNo = rs.getString("REGISTRATION_NO");
//         String branchId = rs.getString("BRANCH_ID");
//         String departmentId = rs.getString("DEPT_ID");
//         String section = rs.getString("SECTION");
//         String category = rs.getString("CATEGORY_ID");
         String description = rs.getString("DESCRIPTION");
//         String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
//         double depreciationRate = rs.getDouble("DEP_RATE");
//         String assetMake = rs.getString("ASSET_MAKE");
//         String assetUser = rs.getString("Asset_User");
//         String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
//         double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
//         double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
         double cost = rs.getDouble("COST_PRICE");
//         String depreciationEndDate = formatDate(rs.getDate("DEP_END_DATE"));
      //   double residualValue = rs.getDouble("RESIDUAL_VALUE");
         String postingDate = rs.getString("revalue_Date");
      //   String entryRaised = rs.getString("RAISE_ENTRY");
      //   double depreciationYearToDate = rs.getDouble("DEP_YTD");
         double nbv = rs.getDouble("NBV");
      //   double revalue_cost = rs.getDouble("REVALUE_COST");
      //   int remainingLife = rs.getInt("REMAINING_LIFE");
      //   int totalLife = rs.getInt("TOTAL_LIFE");
      //   java.util.Date effectiveDate = rs.getDate("TRANSFER_DATE");
      //   String asset_status=rs.getString("ASSET_STATUS");
         int assetCode = rs.getInt("asset_code");
//         double improvcost = rs.getDouble("IMPROV_COST");
//         double improvnbv = rs.getDouble("IMPROV_NBV");
//         double improveAccumdep = rs.getDouble("IMPROV_ACCUMDEP");
//         double improveMonthlydep = rs.getDouble("IMPROV_MONTHLYDEP");
//         double improveTotalNbv = rs.getDouble("TOTAL_NBV");
//         int quantity = rs.getInt("QUANTITY");
//         String ReqnUserID = rs.getString("ReqnUserID");

         Asset aset = new Asset();
         aset.setId(id);
         aset.setDescription(description);
         aset.setNbv(nbv);
         aset.setPostingDate(postingDate);
         aset.setAssetCode(assetCode);
         aset.setCost(cost);
         
         list.add(aset);

         minCost =Math.min(minCost, cost);
         maxCost = Math.max(maxCost, cost);


         //getMinMaxAssetCost(minCost,maxCost);
     //    setMinCost(minCost);
      //   setMaxCost(maxCost);
     }

 } catch (Exception e) {
     System.out.println("INFO:Error fetching ALL Asset Improvement 2 in findAssetImproveddByQuery->" +
                        e.getMessage());
 } finally {
     closeConnection(con, ps, rs);
 }

 return list;

}


public ArrayList findAssetImproveddByQuery(String fromDate, String toDate) {
 //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
	
 double minCost =0;
 double maxCost =0; 
 String selectQuery = "select distinct a.asset_id,a.cost_price,a.revalue_Date,a.Description,a.nbv,a.asset_code "
 +"from am_asset_improvement a, am_Raisentry_Transaction b "
 +"where a.asset_id = b.asset_id and page1 = 'ASSET IMPROVEMENT RAISE ENTRY' and transactionId = '26' "
 +"AND a.revalue_Date BETWEEN ? AND ?"
 //+queryFilter
 +" UNION "
 +"select a.asset_id,a.cost_price,a.revalue_Date,a.Description,a.nbv,a.asset_code  "
 +"from am_asset_improvement_Upload a, am_raisentry_post b "
 +"where CONVERT(varchar, a.Revalue_ID) = CONVERT(varchar, b.Id) and b.entryPostFlag = 'Y' and b.GroupIdStatus = 'Y' "
 +"AND a.revalue_Date BETWEEN ? AND ?";
// +queryFilter;
// System.out.println("the value of selectQuery in findAssetImproveddByQuery is ]]]]]]]]]]]]]]]]]]" +selectQuery);      		
 Connection con = null;
 PreparedStatement ps = null;  

 ResultSet rs = null;     
 ArrayList list = new ArrayList();

 try {
//     con = getConnection(JNDI);
//     ps = con.prepareStatement(selectQuery);
//     rs = ps.executeQuery();
	 
     con = getConnection(JNDI);
     ps = con.prepareStatement(selectQuery);
     ps.setString(1, fromDate);
     ps.setString(2, toDate);
     ps.setString(3, fromDate);
     ps.setString(4, toDate);
     rs = ps.executeQuery();
	 
     while (rs.next()) {
 //    	String batchId = rs.getString("GROUP_ID");
         String id = rs.getString("ASSET_ID");
//         String registrationNo = rs.getString("REGISTRATION_NO");
//         String branchId = rs.getString("BRANCH_ID");
//         String departmentId = rs.getString("DEPT_ID");
//         String section = rs.getString("SECTION");
//         String category = rs.getString("CATEGORY_ID");
         String description = rs.getString("DESCRIPTION");
//         String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
//         double depreciationRate = rs.getDouble("DEP_RATE");
//         String assetMake = rs.getString("ASSET_MAKE");
//         String assetUser = rs.getString("Asset_User");
//         String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
//         double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
//         double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
         double cost = rs.getDouble("COST_PRICE");
//         String depreciationEndDate = formatDate(rs.getDate("DEP_END_DATE"));
      //   double residualValue = rs.getDouble("RESIDUAL_VALUE");
         String postingDate = rs.getString("revalue_Date");
      //   String entryRaised = rs.getString("RAISE_ENTRY");
      //   double depreciationYearToDate = rs.getDouble("DEP_YTD");
         double nbv = rs.getDouble("NBV");
      //   double revalue_cost = rs.getDouble("REVALUE_COST");
      //   int remainingLife = rs.getInt("REMAINING_LIFE");
      //   int totalLife = rs.getInt("TOTAL_LIFE");
      //   java.util.Date effectiveDate = rs.getDate("TRANSFER_DATE");
      //   String asset_status=rs.getString("ASSET_STATUS");
         int assetCode = rs.getInt("asset_code");
//         double improvcost = rs.getDouble("IMPROV_COST");
//         double improvnbv = rs.getDouble("IMPROV_NBV");
//         double improveAccumdep = rs.getDouble("IMPROV_ACCUMDEP");
//         double improveMonthlydep = rs.getDouble("IMPROV_MONTHLYDEP");
//         double improveTotalNbv = rs.getDouble("TOTAL_NBV");
//         int quantity = rs.getInt("QUANTITY");
//         String ReqnUserID = rs.getString("ReqnUserID");

         Asset aset = new Asset();
         aset.setId(id);
         aset.setDescription(description);
         aset.setNbv(nbv);
         aset.setPostingDate(postingDate);
         aset.setAssetCode(assetCode);
         aset.setCost(cost);
         
         list.add(aset);

         minCost =Math.min(minCost, cost);
         maxCost = Math.max(maxCost, cost);


         //getMinMaxAssetCost(minCost,maxCost);
     //    setMinCost(minCost);
      //   setMaxCost(maxCost);
     }

 } catch (Exception e) {
     System.out.println("INFO:Error fetching ALL Asset Improvement 3 in findAssetImproveddByQuery->" +
                        e.getMessage());
 } finally {
     closeConnection(con, ps, rs);
 }

 return list;

}



public ArrayList findTotalSingleStockByQuery(String branchCode,String queryFilter) {
	 //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
	 double minCost =0;
	 double maxCost =0;
	 
	 String selectQuery = "select *from ST_STOCK a, ST_INVENTORY_ITEMS b, ST_MEASURING_UNIT c   " +
			 "where a.ITEM_CODE = b.ITEM_CODE and b.MEASURING_CODE = c.UNIT_CODE and c.PACK_QUANTITY IS NULL "+
			 queryFilter;
//	 System.out.println("the value of selectQuery in findTotalSingleStockByQuery is ]]]]]]]]]]]]]]]]]]" +selectQuery);      		
	 Connection con = null;
	 PreparedStatement ps = null;

	 ResultSet rs = null;
	 ArrayList list = new ArrayList();

	 try {
	     con = getConnection(JNDI);
	     ps = con.prepareStatement(selectQuery);
	     rs = ps.executeQuery();

	     while (rs.next()) {

	         String id = rs.getString("ASSET_ID");
	         String registrationNo = rs.getString("REGISTRATION_NO");
	         String branchId = rs.getString("BRANCH_ID");
	         String departmentId = rs.getString("DEPT_ID");
	         String section = rs.getString("SECTION");
	         String category = rs.getString("CATEGORY_ID");
	         String description = rs.getString("DESCRIPTION");
	         String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
	         double depreciationRate = rs.getDouble("DEP_RATE");
	         String assetMake = rs.getString("ASSET_MAKE");
	         String assetUser = rs.getString("ASSET_USER");
	         String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
	         double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
	         double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
	         double cost = rs.getDouble("COST_PRICE");
	         String depreciationEndDate = formatDate(rs.getDate(
	                 "DEP_END_DATE"));
	         double residualValue = rs.getDouble("RESIDUAL_VALUE");
	         String postingDate = rs.getString("POSTING_DATE");
	         String entryRaised = rs.getString("RAISE_ENTRY");
	         double depreciationYearToDate = rs.getDouble("DEP_YTD");
	         double nbv = rs.getDouble("NBV");
	         double revalue_cost = rs.getDouble("REVALUE_COST");
	         int remainingLife = rs.getInt("REMAINING_LIFE");
	         int totalLife = rs.getInt("TOTAL_LIFE");
	         java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
	         String asset_status=rs.getString("ASSET_STATUS");
	         int assetCode = rs.getInt("asset_code");
	         double improvcost = rs.getDouble("IMPROV_COST");
	         double improvnbv = rs.getDouble("IMPROV_NBV");
	         double improveAccumdep = rs.getDouble("IMPROV_ACCUMDEP");
	         double improveMonthlydep = rs.getDouble("IMPROV_MONTHLYDEP");
	         double improveTotalNbv = rs.getDouble("TOTAL_NBV");
	         
	         Asset aset = new Asset(id, registrationNo, branchId,
	                                departmentId, section, category,
	                                description,
	                                datePurchased, depreciationRate,
	                                assetMake,
	                                assetUser, assetMaintenance,
	                                accumulatedDepreciation,
	                                monthlyDepreciation, cost,
	                                depreciationEndDate,
	                                residualValue, postingDate, entryRaised,
	                                depreciationYearToDate);
	         aset.setNbv(nbv);
	         aset.setRemainLife(remainingLife);
	         aset.setTotalLife(totalLife);
	         aset.setEffectiveDate(effectiveDate);
	         aset.setAsset_status(asset_status);
	         aset.setAssetCode(assetCode);
	         aset.setRevalue_cost(revalue_cost);
	         aset.setImprovcost(improvcost);
	         aset.setImprovnbv(improvnbv);
	         aset.setImprovaccumulatedDepreciation(improveAccumdep);
	         aset.setImprovmonthlyDepreciation(improveMonthlydep);
	         aset.setImprovTotalNbv(improveTotalNbv);
	         
	         list.add(aset);

	         minCost =Math.min(minCost, cost);
	         maxCost = Math.max(maxCost, cost);


	         //getMinMaxAssetCost(minCost,maxCost);
	     //    setMinCost(minCost);
	      //   setMaxCost(maxCost);
	     }

	 } catch (Exception e) {
	     System.out.println("INFO:Error fetching ALL Stock in findTotalSingleStockByQuery ->" +
	                        e.getMessage());
	 } finally {
	     closeConnection(con, ps, rs);
	 }

	 return list;

	}

public ArrayList findTotalPackStockByQuery(String branchCode,String queryFilter) {
	 //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
	 double minCost =0;
	 double maxCost =0;
	 
	 String selectQuery = "select *from ST_STOCK a, ST_INVENTORY_ITEMS b, ST_MEASURING_UNIT c   " +
			 "where a.ITEM_CODE = b.ITEM_CODE and b.MEASURING_CODE = c.UNIT_CODE and c.PACK_QUANTITY IS NOT NULL "+
			 queryFilter;
//	 System.out.println("the value of selectQuery in findTotalPackStockByQuery is ]]]]]]]]]]]]]]]]]]" +selectQuery);      		
	 Connection con = null;
	 PreparedStatement ps = null;

	 ResultSet rs = null;
	 ArrayList list = new ArrayList();

	 try {
	     con = getConnection(JNDI);
	     ps = con.prepareStatement(selectQuery);
	     rs = ps.executeQuery();

	     while (rs.next()) {

	         String id = rs.getString("ASSET_ID");
	         String registrationNo = rs.getString("REGISTRATION_NO");
	         String branchId = rs.getString("BRANCH_ID");
	         String departmentId = rs.getString("DEPT_ID");
	         String section = rs.getString("SECTION");
	         String category = rs.getString("CATEGORY_ID");
	         String description = rs.getString("DESCRIPTION");
	         String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
	         double depreciationRate = rs.getDouble("DEP_RATE");
	         String assetMake = rs.getString("ASSET_MAKE");
	         String assetUser = rs.getString("ASSET_USER");
	         String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
	         double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
	         double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
	         double cost = rs.getDouble("COST_PRICE");
	         String depreciationEndDate = formatDate(rs.getDate(
	                 "DEP_END_DATE"));
	         double residualValue = rs.getDouble("RESIDUAL_VALUE");
	         String postingDate = rs.getString("POSTING_DATE");
	         String entryRaised = rs.getString("RAISE_ENTRY");
	         double depreciationYearToDate = rs.getDouble("DEP_YTD");
	         double nbv = rs.getDouble("NBV");
	         double revalue_cost = rs.getDouble("REVALUE_COST");
	         int remainingLife = rs.getInt("REMAINING_LIFE");
	         int totalLife = rs.getInt("TOTAL_LIFE");
	         java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
	         String asset_status=rs.getString("ASSET_STATUS");
	         int assetCode = rs.getInt("asset_code");
	         double improvcost = rs.getDouble("IMPROV_COST");
	         double improvnbv = rs.getDouble("IMPROV_NBV");
	         double improveAccumdep = rs.getDouble("IMPROV_ACCUMDEP");
	         double improveMonthlydep = rs.getDouble("IMPROV_MONTHLYDEP");
	         double improveTotalNbv = rs.getDouble("TOTAL_NBV");
	         
	         Asset aset = new Asset(id, registrationNo, branchId,
	                                departmentId, section, category,
	                                description,
	                                datePurchased, depreciationRate,
	                                assetMake,
	                                assetUser, assetMaintenance,
	                                accumulatedDepreciation,
	                                monthlyDepreciation, cost,
	                                depreciationEndDate,
	                                residualValue, postingDate, entryRaised,
	                                depreciationYearToDate);
	         aset.setNbv(nbv);
	         aset.setRemainLife(remainingLife);
	         aset.setTotalLife(totalLife);
	         aset.setEffectiveDate(effectiveDate);
	         aset.setAsset_status(asset_status);
	         aset.setAssetCode(assetCode);
	         aset.setRevalue_cost(revalue_cost);
	         aset.setImprovcost(improvcost);
	         aset.setImprovnbv(improvnbv);
	         aset.setImprovaccumulatedDepreciation(improveAccumdep);
	         aset.setImprovmonthlyDepreciation(improveMonthlydep);
	         aset.setImprovTotalNbv(improveTotalNbv);
	         
	         list.add(aset);

	         minCost =Math.min(minCost, cost);
	         maxCost = Math.max(maxCost, cost);


	         //getMinMaxAssetCost(minCost,maxCost);
	     //    setMinCost(minCost);
	      //   setMaxCost(maxCost);
	     }

	 } catch (Exception e) {
	     System.out.println("INFO:Error fetching ALL Stock in findTotalPackStockByQuery ->" +
	                        e.getMessage());
	 } finally {
	     closeConnection(con, ps, rs);
	 }

	 return list;

	}

public ArrayList findStockAlreadyIssueByQuery(String branchCode,String queryFilter) {
 //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
 double minCost =0;
 double maxCost =0; 
 String selectQuery = "select *from ST_DISTRIBUTION_ITEM " 
 +queryFilter;
// System.out.println("the value of selectQuery in findStockAlreadyIssueByQuery is ]]]]]]]]]]]]]]]]]]" +selectQuery);      		
 Connection con = null;
 PreparedStatement ps = null;

 ResultSet rs = null;     
 ArrayList list = new ArrayList();

 try {
     con = getConnection(JNDI);
     ps = con.prepareStatement(selectQuery);
     rs = ps.executeQuery();

     while (rs.next()) {
         String id = rs.getString("STOCK_CODE");
         String description = rs.getString("DESCRIPTION");
         double cost = rs.getDouble("COST_PRICE");
         String postingDate = rs.getString("CREATE_DATE");
         double nbv = rs.getDouble("UNITPRICE");
         int assetCode = rs.getInt("asset_code");
         int quantityRemain = rs.getInt("QUANTITY_REMAIN");
         int quantityIssued = rs.getInt("QUANTITY_DELIVER");
         int quantityRequested = rs.getInt("QUANTITY_REQUEST");

         StockRecordsBean stok = new StockRecordsBean();
         stok.setAsset_id(id);
         stok.setDescription(description);
         stok.setPosting_date(postingDate);
         stok.setAssetCode(assetCode);
         stok.setCost_price(String.valueOf(cost));
         stok.setQtyRequested(quantityRequested);
         stok.setQtyRemain(quantityRemain);
         stok.setQtyIssued(quantityIssued);
         list.add(stok);

         //getMinMaxAssetCost(minCost,maxCost);
     //    setMinCost(minCost);
      //   setMaxCost(maxCost);
     }

 } catch (Exception e) {
     System.out.println("INFO:Error fetching ALL Stock Issued in findStockAlreadyIssueByQuery->" +
                        e.getMessage());
 } finally {
     closeConnection(con, ps, rs);
 }

 return list;

}

public ArrayList findTotalStockByQuery(String branchCode,String queryFilter) {
 //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
 double minCost =0;
 double maxCost =0; 
 String selectQuery = "select *from ST_STOCK " 
 +queryFilter;
// System.out.println("the value of selectQuery in findStockAlreadyIssueByQuery is ]]]]]]]]]]]]]]]]]]" +selectQuery);      		
 Connection con = null;
 PreparedStatement ps = null;

 ResultSet rs = null;     
 ArrayList list = new ArrayList();

 try {
     con = getConnection(JNDI);
     ps = con.prepareStatement(selectQuery);
     rs = ps.executeQuery();

     while (rs.next()) {
         String id = rs.getString("STOCK_CODE");
         String description = rs.getString("DESCRIPTION");
         double cost = rs.getDouble("COST_PRICE");
         String postingDate = rs.getString("CREATE_DATE");
         double nbv = rs.getDouble("UNIT_PRICE");
         int assetCode = rs.getInt("asset_code");
         int quantityRemain = rs.getInt("QUANTITY_REMAIN");
         int quantityIssued = rs.getInt("QUANTITY_DELIVER");
         int quantityRequested = rs.getInt("QUANTITY_REQUEST");

         StockRecordsBean stok = new StockRecordsBean();
         stok.setAsset_id(id);
         stok.setDescription(description);
         stok.setPosting_date(postingDate);
         stok.setAssetCode(assetCode);
         stok.setCost_price(String.valueOf(cost));
         stok.setQtyRequested(quantityRequested);
         stok.setQtyRemain(quantityRemain);
         stok.setQtyIssued(quantityIssued);
         list.add(stok);

         //getMinMaxAssetCost(minCost,maxCost);
     //    setMinCost(minCost);
      //   setMaxCost(maxCost);
     }

 } catch (Exception e) {
     System.out.println("INFO:Error fetching ALL Stock Issued in findStockAlreadyIssueByQuery->" +
                        e.getMessage());
 } finally {
     closeConnection(con, ps, rs);
 }

 return list;

}


public ArrayList findStocktByQuery(String branchCode,String queryFilter) {
// System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
 double minCost =0;
 double maxCost =0;
 String selectQuery = "select *from ST_STOCK " +
		 queryFilter;
// System.out.println("the value of selectQuery is ]]]]]]]]]]]]]]]]]]" +selectQuery);     		
 Connection con = null;
 PreparedStatement ps = null;

 ResultSet rs = null;
 ArrayList list = new ArrayList();
  
 try {
     con = getConnection(JNDI);
     ps = con.prepareStatement(selectQuery);
     rs = ps.executeQuery();

     while (rs.next()) {

         String id = rs.getString("ASSET_ID");
         String registrationNo = rs.getString("REGISTRATION_NO");
         String branchId = rs.getString("BRANCH_ID");
         String departmentId = rs.getString("DEPT_ID");
         String section = rs.getString("SECTION");
         String category = rs.getString("CATEGORY_ID");
         String description = rs.getString("DESCRIPTION");
         String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
         double depreciationRate = rs.getDouble("DEP_RATE");
         String assetMake = rs.getString("ASSET_MAKE");
         String assetUser = rs.getString("ASSET_USER");
         String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
         double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
         double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
         double cost = rs.getDouble("COST_PRICE");
         String depreciationEndDate = formatDate(rs.getDate("DEP_END_DATE"));
         double residualValue = rs.getDouble("RESIDUAL_VALUE");
         String postingDate = rs.getString("POSTING_DATE");
         String entryRaised = rs.getString("RAISE_ENTRY");
         double depreciationYearToDate = rs.getDouble("DEP_YTD");
         double nbv = rs.getDouble("NBV");
         double revalue_cost = rs.getDouble("REVALUE_COST");
         int remainingLife = rs.getInt("REMAINING_LIFE");
         int totalLife = rs.getInt("TOTAL_LIFE");
         java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
         String asset_status=rs.getString("ASSET_STATUS");
         int assetCode = rs.getInt("asset_code");
         double improvcost = rs.getDouble("IMPROV_COST");
         double improvnbv = rs.getDouble("IMPROV_NBV");
         double improveAccumdep = rs.getDouble("IMPROV_ACCUMDEP");
         double improveMonthlydep = rs.getDouble("IMPROV_MONTHLYDEP");
         double improveTotalNbv = rs.getDouble("TOTAL_NBV");
         
         Asset aset = new Asset(id, registrationNo, branchId,
                                departmentId, section, category,
                                description,
                                datePurchased, depreciationRate,
                                assetMake,
                                assetUser, assetMaintenance,
                                accumulatedDepreciation,
                                monthlyDepreciation, cost,
                                depreciationEndDate,
                                residualValue, postingDate, entryRaised,
                                depreciationYearToDate);
         aset.setNbv(nbv);
         aset.setRemainLife(remainingLife);
         aset.setTotalLife(totalLife);
         aset.setEffectiveDate(effectiveDate);
         aset.setAsset_status(asset_status);
         aset.setAssetCode(assetCode);
         aset.setRevalue_cost(revalue_cost);
         aset.setImprovcost(improvcost);
         aset.setImprovnbv(improvnbv);
         aset.setImprovaccumulatedDepreciation(improveAccumdep);
         aset.setImprovmonthlyDepreciation(improveMonthlydep);
         aset.setImprovTotalNbv(improveTotalNbv);
         
         list.add(aset);

     }

 } catch (Exception e) {
     System.out.println("INFO:Error fetching ALL ST_STOCK in findStocktByQuery ->" +
                        e.getMessage());
 } finally {
     closeConnection(con, ps, rs);
 }

 return list;

}

public ArrayList findAssetTotalByQuery(String queryFilter) {
 //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
 double minCost =0;
 double maxCost =0;
 String selectQuery = "select *from AM_ASSET where asset_status = 'ACTIVE' " +
 queryFilter;
// System.out.println("the value of selectQuery in findAssetTotalByQuery is ]]]]]]]]]]]]]]]]]]" +selectQuery);      		
 Connection con = null;
 PreparedStatement ps = null;
   
 ResultSet rs = null;
 ArrayList list = new ArrayList();

 try {
//     con = getConnection(JNDI);
//     ps = con.prepareStatement(selectQuery);
//     rs = ps.executeQuery();

     con = getConnection(JNDI);
     ps = con.prepareStatement(selectQuery);
//     ps.setString(1, branchCode);
     rs = ps.executeQuery();
     while (rs.next()) {
     	String batchId = rs.getString("GROUP_ID");
         String id = rs.getString("ASSET_ID");
         String registrationNo = rs.getString("REGISTRATION_NO");
         String branchId = rs.getString("BRANCH_ID");
         String departmentId = rs.getString("DEPT_ID");
         String section = rs.getString("SECTION");
         String category = rs.getString("CATEGORY_ID");
         String description = rs.getString("DESCRIPTION");
         String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
         double depreciationRate = rs.getDouble("DEP_RATE");
         String assetMake = rs.getString("ASSET_MAKE");
         String assetUser = rs.getString("Asset_User");
         String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
         double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
         double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
         double cost = rs.getDouble("COST_PRICE");
         String depreciationEndDate = formatDate(rs.getDate(
                 "DEP_END_DATE"));
         double residualValue = rs.getDouble("RESIDUAL_VALUE");
         String postingDate = rs.getString("POSTING_DATE");
         String entryRaised = rs.getString("RAISE_ENTRY");
         double depreciationYearToDate = rs.getDouble("DEP_YTD");
         double nbv = rs.getDouble("NBV");
         double revalue_cost = rs.getDouble("REVALUE_COST");
         int remainingLife = rs.getInt("REMAINING_LIFE");
         int totalLife = rs.getInt("TOTAL_LIFE");
         java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
         String asset_status=rs.getString("ASSET_STATUS");
         int assetCode = rs.getInt("asset_code");
         double improvcost = rs.getDouble("IMPROV_COST");
         double improvnbv = rs.getDouble("IMPROV_NBV");
         double improveAccumdep = rs.getDouble("IMPROV_ACCUMDEP");
         double improveMonthlydep = rs.getDouble("IMPROV_MONTHLYDEP");
         double improveTotalNbv = rs.getDouble("TOTAL_NBV");
         int quantity = rs.getInt("QUANTITY");
//         String ReqnUserID = rs.getString("ReqnUserID");

         Asset aset = new Asset(id, registrationNo, branchId,
                                departmentId, section, category,
                                description,
                                datePurchased, depreciationRate,
                                assetMake,
                                assetUser, assetMaintenance,
                                accumulatedDepreciation,
                                monthlyDepreciation, cost,
                                depreciationEndDate,
                                residualValue, postingDate, entryRaised,
                                depreciationYearToDate);
         aset.setGROUP_IDd(batchId);
         aset.setNbv(nbv);
         aset.setRemainLife(remainingLife);
         aset.setTotalLife(totalLife);
         aset.setEffectiveDate(effectiveDate);
         aset.setAsset_status(asset_status);
         aset.setAssetCode(assetCode);
         aset.setRevalue_cost(revalue_cost);
         aset.setImprovcost(improvcost);
         aset.setImprovnbv(improvnbv);
         aset.setImprovaccumulatedDepreciation(improveAccumdep);
         aset.setImprovmonthlyDepreciation(improveMonthlydep);
         aset.setImprovTotalNbv(improveTotalNbv);
         aset.setQuantity(quantity);
         
         list.add(aset);

         minCost =Math.min(minCost, cost);
         maxCost = Math.max(maxCost, cost);


         //getMinMaxAssetCost(minCost,maxCost);
     //    setMinCost(minCost);
      //   setMaxCost(maxCost);
     }

 } catch (Exception e) {
     System.out.println("INFO:Error fetching ALL Asset in findAssetTotalByQuery ->" +
                        e.getMessage());
 } finally {
     closeConnection(con, ps, rs);
 }

 return list;

}


public ArrayList findAssetImprovedByQuery(String fromDate, String toDate) {
 //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
	
 double minCost =0;
 double maxCost =0; 
 String selectQuery = "select distinct a.asset_id,a.cost_price,a.revalue_Date,a.Description,a.nbv,a.asset_code "
 +"from am_asset_improvement a, am_Raisentry_Transaction b "
 +"where a.asset_id = b.asset_id and page1 = 'ASSET IMPROVEMENT RAISE ENTRY' and transactionId = '26' "
 +"AND a.revalue_Date BETWEEN ? AND ?"
 //+queryFilter
 +" UNION "
 +"select a.asset_id,a.cost_price,a.revalue_Date,a.Description,a.nbv,a.asset_code  "
 +"from am_asset_improvement_Upload a, am_raisentry_post b "
 +"where CONVERT(varchar, a.Revalue_ID) = CONVERT(varchar, b.Id) and b.entryPostFlag = 'Y' and b.GroupIdStatus = 'Y' "
 +"AND a.revalue_Date BETWEEN ? AND ?";
// +queryFilter;
// System.out.println("the value of selectQuery in findAssetImproveddByQuery is ]]]]]]]]]]]]]]]]]]" +selectQuery);      		
 Connection con = null;
 PreparedStatement ps = null;  

 ResultSet rs = null;     
 ArrayList list = new ArrayList();

 try {
//     con = getConnection(JNDI);
//     ps = con.prepareStatement(selectQuery);
//     rs = ps.executeQuery();
	 
     con = getConnection(JNDI);
     ps = con.prepareStatement(selectQuery);
//     ps.setString(1, branchCode);
     ps.setString(1, fromDate);
     ps.setString(2, toDate);
//     ps.setString(4, branchCode);
     ps.setString(3, fromDate);
     ps.setString(4, toDate);
     rs = ps.executeQuery();
	 
     while (rs.next()) {
 //    	String batchId = rs.getString("GROUP_ID");
         String id = rs.getString("ASSET_ID");
//         String registrationNo = rs.getString("REGISTRATION_NO");
//         String branchId = rs.getString("BRANCH_ID");
//         String departmentId = rs.getString("DEPT_ID");
//         String section = rs.getString("SECTION");
//         String category = rs.getString("CATEGORY_ID");
         String description = rs.getString("DESCRIPTION");
//         String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
//         double depreciationRate = rs.getDouble("DEP_RATE");
//         String assetMake = rs.getString("ASSET_MAKE");
//         String assetUser = rs.getString("Asset_User");
//         String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
//         double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
//         double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
         double cost = rs.getDouble("COST_PRICE");
//         String depreciationEndDate = formatDate(rs.getDate("DEP_END_DATE"));
      //   double residualValue = rs.getDouble("RESIDUAL_VALUE");
         String postingDate = rs.getString("revalue_Date");
      //   String entryRaised = rs.getString("RAISE_ENTRY");
      //   double depreciationYearToDate = rs.getDouble("DEP_YTD");
         double nbv = rs.getDouble("NBV");
      //   double revalue_cost = rs.getDouble("REVALUE_COST");
      //   int remainingLife = rs.getInt("REMAINING_LIFE");
      //   int totalLife = rs.getInt("TOTAL_LIFE");
      //   java.util.Date effectiveDate = rs.getDate("TRANSFER_DATE");
      //   String asset_status=rs.getString("ASSET_STATUS");
         int assetCode = rs.getInt("asset_code");
//         double improvcost = rs.getDouble("IMPROV_COST");
//         double improvnbv = rs.getDouble("IMPROV_NBV");
//         double improveAccumdep = rs.getDouble("IMPROV_ACCUMDEP");
//         double improveMonthlydep = rs.getDouble("IMPROV_MONTHLYDEP");
//         double improveTotalNbv = rs.getDouble("TOTAL_NBV");
//         int quantity = rs.getInt("QUANTITY");
//         String ReqnUserID = rs.getString("ReqnUserID");

         Asset aset = new Asset();
         aset.setId(id);
         aset.setDescription(description);
         aset.setNbv(nbv);
         aset.setPostingDate(postingDate);
         aset.setAssetCode(assetCode);
         aset.setCost(cost);
         
         list.add(aset);

         minCost =Math.min(minCost, cost);
         maxCost = Math.max(maxCost, cost);


         //getMinMaxAssetCost(minCost,maxCost);
     //    setMinCost(minCost);
      //   setMaxCost(maxCost);
     }

 } catch (Exception e) {
     System.out.println("INFO:Error fetching ALL Asset Transfer in findAssetImprovedByQuery->" +
                        e.getMessage());
 } finally {
     closeConnection(con, ps, rs);
 }

 return list;

}


public ArrayList findAssetTransferddByQuery(String fromDate, String toDate) {
 //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
 double minCost =0;
 double maxCost =0;
 String selectQuery = "select distinct a.asset_id, New_Asset_user,a.Description,a.Monthly_Dep,a.Cost_Price,a.new_asset_id,a.TRANSFER_DATE,a.asset_code,a.NBV   "
 +"from am_assetTransfer a, am_Raisentry_Transaction b "
 +"where a.asset_id = b.asset_id and page1 = 'ASSET TRANSFER RAISE ENTRY' and transactionId = '21' "
 +"AND a.TRANSFER_DATE BETWEEN ? AND ?"
 +" UNION "
 +"select a.asset_id, NewAsset_user AS New_Asset_user,a.Description,a.Monthly_Dep,a.Cost_Price,a.new_asset_id,a.TRANSFER_DATE,a.asset_code,a.NBV  "
 +"from am_gb_bulkTransfer a, am_raisentry_post b "
 +"where CONVERT(varchar, a.Batch_id) = CONVERT(varchar, b.Id) and b.entryPostFlag = 'Y' and b.GroupIdStatus = 'Y' "
 +"AND a.TRANSFER_DATE BETWEEN ? AND ? ";
// System.out.println("the value of selectQuery in findAssetTransferddByQuery is ]]]]]]]]]]]]]]]]]]" +selectQuery);      		
 Connection con = null;
 PreparedStatement ps = null;

 ResultSet rs = null;     
 ArrayList list = new ArrayList();

 try {
//     con = getConnection(JNDI);
//     ps = con.prepareStatement(selectQuery);
//     rs = ps.executeQuery();
     con = getConnection(JNDI);
     ps = con.prepareStatement(selectQuery);
//     ps.setString(1, branchCode);
     ps.setString(1, fromDate);
     ps.setString(2, toDate);
//     ps.setString(4, branchCode);
     ps.setString(3, fromDate);
     ps.setString(4, toDate);
     rs = ps.executeQuery();
	 
     while (rs.next()) {
 //    	String batchId = rs.getString("GROUP_ID");
         String id = rs.getString("ASSET_ID");
//         String registrationNo = rs.getString("REGISTRATION_NO");
//         String branchId = rs.getString("BRANCH_ID");
//         String departmentId = rs.getString("DEPT_ID");
//         String section = rs.getString("SECTION");
//         String category = rs.getString("CATEGORY_ID");
         String description = rs.getString("DESCRIPTION");
//         String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
//         double depreciationRate = rs.getDouble("DEP_RATE");
//         String assetMake = rs.getString("ASSET_MAKE");
//         String assetUser = rs.getString("Asset_User");
//         String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
//         double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
//         double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
         double cost = rs.getDouble("COST_PRICE");
//         String depreciationEndDate = formatDate(rs.getDate("DEP_END_DATE"));
      //   double residualValue = rs.getDouble("RESIDUAL_VALUE");
         String postingDate = rs.getString("TRANSFER_DATE");
      //   String entryRaised = rs.getString("RAISE_ENTRY");
      //   double depreciationYearToDate = rs.getDouble("DEP_YTD");
         double nbv = rs.getDouble("NBV");
      //   double revalue_cost = rs.getDouble("REVALUE_COST");
      //   int remainingLife = rs.getInt("REMAINING_LIFE");
      //   int totalLife = rs.getInt("TOTAL_LIFE");
      //   java.util.Date effectiveDate = rs.getDate("TRANSFER_DATE");
      //   String asset_status=rs.getString("ASSET_STATUS");
         int assetCode = rs.getInt("asset_code");
//         double improvcost = rs.getDouble("IMPROV_COST");
//         double improvnbv = rs.getDouble("IMPROV_NBV");
//         double improveAccumdep = rs.getDouble("IMPROV_ACCUMDEP");
//         double improveMonthlydep = rs.getDouble("IMPROV_MONTHLYDEP");
//         double improveTotalNbv = rs.getDouble("TOTAL_NBV");
//         int quantity = rs.getInt("QUANTITY");
//         String ReqnUserID = rs.getString("ReqnUserID");

         Asset aset = new Asset();
         aset.setId(id);
         aset.setDescription(description);
         aset.setNbv(nbv);
         aset.setPostingDate(postingDate);
         aset.setAssetCode(assetCode);
         aset.setCost(cost);
         
         list.add(aset);

         minCost =Math.min(minCost, cost);
         maxCost = Math.max(maxCost, cost);


         //getMinMaxAssetCost(minCost,maxCost);
     //    setMinCost(minCost);
      //   setMaxCost(maxCost);
     }

 } catch (Exception e) {
     System.out.println("INFO:Error fetching ALL Asset Transfer 5 in findAssetTransferddByQuery->" +
                        e.getMessage());
 } finally {
     closeConnection(con, ps, rs);
 }
  
 return list;

}

public ArrayList findAssetDisposeddByQuery(String fromDate,String toDate) {
 //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
 double minCost =0;
 double maxCost =0;

 String selectQuery = "select *from AM_ASSET where asset_status = 'DISPOSED' AND Date_Disposed BETWEEN  ? AND ?";
// System.out.println("the value of selectQuery in findAssetDisposeddByQuery is ]]]]]]]]]]]]]]]]]]" +selectQuery);
 Connection con = null;
 PreparedStatement ps = null;
 
 ResultSet rs = null;
 ArrayList list = new ArrayList();

 try {
//     con = getConnection(JNDI);
//     ps = con.prepareStatement(selectQuery);
//     rs = ps.executeQuery();
     
     con = getConnection(JNDI);
     ps = con.prepareStatement(selectQuery);
//     ps.setString(1, branchCode);
     ps.setString(1, fromDate);
     ps.setString(2, toDate);
     rs = ps.executeQuery();

     while (rs.next()) {

         String id = rs.getString("ASSET_ID");
         String registrationNo = rs.getString("REGISTRATION_NO");
         String branchId = rs.getString("BRANCH_ID");
         String departmentId = rs.getString("DEPT_ID");
         String section = rs.getString("SECTION");
         String category = rs.getString("CATEGORY_ID");
         String description = rs.getString("DESCRIPTION");
         String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
         double depreciationRate = rs.getDouble("DEP_RATE");
         String assetMake = rs.getString("ASSET_MAKE");
         String assetUser = rs.getString("ASSET_USER");
         String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
         double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
         double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
         double cost = rs.getDouble("COST_PRICE");
         String depreciationEndDate = formatDate(rs.getDate(
                 "DEP_END_DATE"));
         double residualValue = rs.getDouble("RESIDUAL_VALUE");
         String postingDate = rs.getString("POSTING_DATE");
         String entryRaised = rs.getString("RAISE_ENTRY");
         double depreciationYearToDate = rs.getDouble("DEP_YTD");
         double nbv = rs.getDouble("NBV");
         double revalue_cost = rs.getDouble("REVALUE_COST");
         int remainingLife = rs.getInt("REMAINING_LIFE");
         int totalLife = rs.getInt("TOTAL_LIFE");
         java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
         String asset_status=rs.getString("ASSET_STATUS");
         int assetCode = rs.getInt("asset_code");
         double improvcost = rs.getDouble("IMPROV_COST");
         double improvnbv = rs.getDouble("IMPROV_NBV");
         double improveAccumdep = rs.getDouble("IMPROV_ACCUMDEP");
         double improveMonthlydep = rs.getDouble("IMPROV_MONTHLYDEP");
         double improveTotalNbv = rs.getDouble("TOTAL_NBV");
         
         Asset aset = new Asset(id, registrationNo, branchId,
                                departmentId, section, category,
                                description,
                                datePurchased, depreciationRate,
                                assetMake,
                                assetUser, assetMaintenance,
                                accumulatedDepreciation,
                                monthlyDepreciation, cost,
                                depreciationEndDate,
                                residualValue, postingDate, entryRaised,
                                depreciationYearToDate);
         aset.setNbv(nbv);
         aset.setRemainLife(remainingLife);
         aset.setTotalLife(totalLife);
         aset.setEffectiveDate(effectiveDate);
         aset.setAsset_status(asset_status);
         aset.setAssetCode(assetCode);
         aset.setRevalue_cost(revalue_cost);
         aset.setImprovcost(improvcost);
         aset.setImprovnbv(improvnbv);
         aset.setImprovaccumulatedDepreciation(improveAccumdep);
         aset.setImprovmonthlyDepreciation(improveMonthlydep);
         aset.setImprovTotalNbv(improveTotalNbv);
         
         list.add(aset);

         minCost =Math.min(minCost, cost);
         maxCost = Math.max(maxCost, cost);


         //getMinMaxAssetCost(minCost,maxCost);
     //    setMinCost(minCost);
      //   setMaxCost(maxCost);
     }

 } catch (Exception e) {
     System.out.println("INFO:Error fetching ALL Disposed ST_STOCK in findAssetDisposeddByQuery ->" +
                        e.getMessage());
 } finally {
     closeConnection(con, ps, rs);
 }

 return list;

}

public ArrayList findAssetAdditionedByQuery(String fromDate, String toDate) {
 //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
 double minCost =0;
 double maxCost =0;
 String selectQuery = "select *from AM_ASSET where asset_status = 'ACTIVE' "+
 	 "AND POSTING_DATE BETWEEN ? AND ? ";
// System.out.println("the value of selectQuery in findAssetAdditionedByQuery is ]]]]]]]]]]]]]]]]]]" +selectQuery);      		
 Connection con = null;
 PreparedStatement ps = null;

 ResultSet rs = null;     
 ArrayList list = new ArrayList();

 try {
//     con = getConnection(JNDI);
//     ps = con.prepareStatement(selectQuery);
//     rs = ps.executeQuery();
     
     con = getConnection(JNDI);
     ps = con.prepareStatement(selectQuery);
//     ps.setString(1, branchCode);
     ps.setString(1, fromDate);
     ps.setString(2, toDate);
     rs = ps.executeQuery();

     while (rs.next()) {
     	String batchId = rs.getString("GROUP_ID");
         String id = rs.getString("ASSET_ID");
         String registrationNo = rs.getString("REGISTRATION_NO");
         String branchId = rs.getString("BRANCH_ID");
         String departmentId = rs.getString("DEPT_ID");
         String section = rs.getString("SECTION");
         String category = rs.getString("CATEGORY_ID");
         String description = rs.getString("DESCRIPTION");
         String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
         double depreciationRate = rs.getDouble("DEP_RATE");
         String assetMake = rs.getString("ASSET_MAKE");
         String assetUser = rs.getString("Asset_User");
         String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
         double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
         double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
         double cost = rs.getDouble("COST_PRICE");
         String depreciationEndDate = formatDate(rs.getDate("DEP_END_DATE"));
         double residualValue = rs.getDouble("RESIDUAL_VALUE");
         String postingDate = rs.getString("POSTING_DATE");
         String entryRaised = rs.getString("RAISE_ENTRY");
         double depreciationYearToDate = rs.getDouble("DEP_YTD");
         double nbv = rs.getDouble("NBV");
         double revalue_cost = rs.getDouble("REVALUE_COST");
         int remainingLife = rs.getInt("REMAINING_LIFE");
         int totalLife = rs.getInt("TOTAL_LIFE");
         java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
         String asset_status=rs.getString("ASSET_STATUS");
         int assetCode = rs.getInt("asset_code");
         double improvcost = rs.getDouble("IMPROV_COST");
         double improvnbv = rs.getDouble("IMPROV_NBV");
         double improveAccumdep = rs.getDouble("IMPROV_ACCUMDEP");
         double improveMonthlydep = rs.getDouble("IMPROV_MONTHLYDEP");
         double improveTotalNbv = rs.getDouble("TOTAL_NBV");
         int quantity = rs.getInt("QUANTITY");
//         String ReqnUserID = rs.getString("ReqnUserID");

         Asset aset = new Asset(id, registrationNo, branchId,
                                departmentId, section, category,
                                description,
                                datePurchased, depreciationRate,
                                assetMake,
                                assetUser, assetMaintenance,
                                accumulatedDepreciation,
                                monthlyDepreciation, cost,
                                depreciationEndDate,
                                residualValue, postingDate, entryRaised,
                                depreciationYearToDate);
         aset.setGROUP_IDd(batchId);
         aset.setNbv(nbv);
         aset.setRemainLife(remainingLife);
         aset.setTotalLife(totalLife);
         aset.setEffectiveDate(effectiveDate);
         aset.setAsset_status(asset_status);
         aset.setAssetCode(assetCode);
         aset.setRevalue_cost(revalue_cost);
         aset.setImprovcost(improvcost);
         aset.setImprovnbv(improvnbv);
         aset.setImprovaccumulatedDepreciation(improveAccumdep);
         aset.setImprovmonthlyDepreciation(improveMonthlydep);
         aset.setImprovTotalNbv(improveTotalNbv);
         aset.setQuantity(quantity);
         
         list.add(aset);

         minCost =Math.min(minCost, cost);
         maxCost = Math.max(maxCost, cost);


         //getMinMaxAssetCost(minCost,maxCost);
     //    setMinCost(minCost);
      //   setMaxCost(maxCost);
     }

 } catch (Exception e) {
     System.out.println("INFO:Error fetching ALL Asset Addition in findAssetAdditionedByQuery->" +
                        e.getMessage());
 } finally {
     closeConnection(con, ps, rs);
 }

 return list;

}





}
