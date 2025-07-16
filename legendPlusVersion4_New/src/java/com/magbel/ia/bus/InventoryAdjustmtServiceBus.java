package com.magbel.ia.bus;

import com.magbel.ia.dao.PersistenceServiceDAO;

import com.magbel.ia.util.ApplicationHelper;

import com.magbel.ia.vao.InventoryAdjustment;

import com.magbel.ia.vao.InventoryTotal;
import com.magbel.ia.vao.SalesOrderDeliveryItem;

import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.sql.SQLException;

import java.text.SimpleDateFormat;

import java.util.ArrayList;

public class InventoryAdjustmtServiceBus extends PersistenceServiceDAO{
    ApplicationHelper helper;
    private SalesOrderServiceBus sos;
    public String id;
    
    public InventoryAdjustmtServiceBus() {
         helper = new ApplicationHelper();
        sos = new SalesOrderServiceBus();
    }

    public boolean createInventoryAdjustment(String itemNo,String warehouse, String description, 
                                            int quantity,int userId,String posted,String adjustOpt,String companyCode) {

            String query = "INSERT INTO ST_INVENTORY_ADJUSTMT("
                            + "MTID,ITEM_CODE,TRANS_DATE,WAREHOUSE_CODE,"
                            + "DESCRIPTION,QUANTITY,USERID,POSTED,ADJUST_OPTION,COMP_CODE) VALUES (?,?,?,?,?,?,?,?,?,?)";

            Connection con = null;
            PreparedStatement ps = null;
            boolean done = false;
            id = helper.getGeneratedId("ST_INVENTORY_ADJUSTMT");

            try {
                    con = getConnection();
                    ps = con.prepareStatement(query);
                    ps.setString(1, id);
                    ps.setString(2, itemNo);
                    ps.setDate(3, dateConvert(new java.util.Date()));
                    //ps.setString(4, period);
                    ps.setString(4, warehouse);
                    ps.setString(5, description);
                    ps.setInt(6, quantity);
                    ps.setInt(7,userId);
                    ps.setString(8,posted);
                    ps.setString(9,adjustOpt);
					ps.setString(10,companyCode);
                    done = (ps.executeUpdate() != -1);
            } catch (Exception ex) {
                    done = false;
                    System.out.println("ERROR Creating Inventory Adjustment "
                                    + ex.getMessage());
            } finally {
                    closeConnection(con, ps);
            }
            return done;
    }

    public boolean updateInventoryAdjustment(String id,String itemNo,
                     String warehouse,String description,int quantity,String posted){
            String query = "UPDATE ST_INVENTORY_ADJUSTMT SET ITEM_CODE=?, " +
                           "WAREHOUSE_CODE=?,DESCRIPTION=?, " +
                           "QUANTITY=?, POSTED = ? WHERE  MTID=?";

            Connection con = null;
            PreparedStatement ps = null;
            boolean done = false;

            try {
                    con = getConnection();
                    ps = con.prepareStatement(query);
                    ps.setString(1, itemNo);
                    //ps.setString(2, period);
                    ps.setString(2, warehouse);
                    ps.setString(3, description);
                    ps.setInt(4, quantity);
                    ps.setString(5,posted);
                    ps.setString(6, id);
                    done = (ps.executeUpdate() != -1);
            } catch (Exception ex) {
                    System.out.println("ERROR Updating Inventory Adjustment "
                                    + ex.getMessage());
                    ex.printStackTrace();
            } finally {
                    closeConnection(con, ps);
            }
            return done;
    }

    public void deleteInventoryAdjustment(String id)
	{

            String Query = "DELETE FROM ST_INVENTORY_ADJUSTMT WHERE MTID = ?";
            Connection con = null;
            PreparedStatement ps = null;
            try {

                    con = getConnection();
                    ps = con.prepareStatement(Query);
                    ps.setString(1, id);

                    ps.execute();

            } catch (Exception er) {
                    System.out.println("Error Deleting Inventory Adjustment... ->"
                                    + er.getMessage());
            } finally {
                    closeConnection(con, ps);
            }

    }
    
    public ArrayList findInventoryAdjustment(String filter2,String query_) {

                    ArrayList records = new ArrayList();
                    String query = "SELECT MTID, ITEM_CODE, TRANS_DATE,PERIOD, "
                                    + "WAREHOUSE_CODE, DESCRIPTION, QUANTITY,USERID,POSTED,ADJUST_OPTION,REASON "
                                    + " FROM ST_INVENTORY_ADJUSTMT WHERE COMP_CODE= '"+filter2+"' AND MTID IS NOT NULL "+query_;
                    Connection con = null;
                    PreparedStatement ps = null;
                    ResultSet rs = null;

                    try {

                            con = getConnection();
                            ps = con.prepareStatement(query);

                            rs = ps.executeQuery();

                            while (rs.next()) {
                                    String id = rs.getString("MTID");
                                    String itemNo = rs.getString("item_Code");
                                    String date = formatDate(rs.getDate("trans_Date"));
                                    String period = "";//rs.getString("period");
                                    String warehouse = rs.getString("wareHouse_Code");
                                    String description = rs.getString("description");
                                    int quantity = rs.getInt("quantity");
                                    int userId = rs.getInt("userId");
                                    String posted = rs.getString("POSTED");
                                    String adjustOpt = rs.getString("ADJUST_OPTION");
                                    String reason = rs.getString("REASON");
                                    InventoryAdjustment ia = new InventoryAdjustment(id,itemNo,
                                                    date,period,warehouse,description,quantity,userId,posted,adjustOpt);
                                    ia.setReason(reason);
                                    records.add(ia);
                                    
                            }

                    } catch (Exception er) {
                            System.out.println("Error finding All Inventory Adjustment...->"
                                            + er.getMessage());
                    } finally {
                            closeConnection(con, ps, rs);
                    }

                    return records;
            }

    public ArrayList findInventoryAdjustment() {

                    ArrayList records = new ArrayList();
                    String query = "SELECT MTID, ITEM_CODE, TRANS_DATE,PERIOD, "
                                    + "WAREHOUSE_CODE, DESCRIPTION, QUANTITY,USERID,POSTED,ADJUST_OPTION "
                                    + " FROM ST_INVENTORY_ADJUSTMT";
                    Connection con = null;
                    PreparedStatement ps = null;
                    ResultSet rs = null;

                    try {

                            con = getConnection();
                            ps = con.prepareStatement(query);

                            rs = ps.executeQuery();

                            while (rs.next()) {
                                    String id = rs.getString("MTID");
                                    String itemNo = rs.getString("item_Code");
                                    String date = formatDate(rs.getDate("trans_Date"));
                                    String period = "";//rs.getString("period");
                                    String warehouse = rs.getString("wareHouse_Code");
                                    String description = rs.getString("description");
                                    int quantity = rs.getInt("quantity");
                                    int userId = rs.getInt("userId");
                                    String posted = rs.getString("POSTED");
                                    String adjustOpt = rs.getString("ADJUST_OPTION");
                                    InventoryAdjustment ia = new InventoryAdjustment(id,itemNo,
                                                    date,period,warehouse,description,quantity,userId,posted,adjustOpt);
                                    records.add(ia);
                            }

                    } catch (Exception er) {
                            System.out.println("Error finding All Inventory Adjustment...->"
                                            + er.getMessage());
                    } finally {
                            closeConnection(con, ps, rs);
                    }

                    return records;
            }

            public InventoryAdjustment findInventoryAdjustmentById(String id) {

                    String query = "SELECT MTID, ITEM_CODE, TRANS_DATE,PERIOD, "
                                    + "WAREHOUSE_CODE, DESCRIPTION, QUANTITY,USERID,POSTED,ADJUST_OPTION "
                                    + " FROM ST_INVENTORY_ADJUSTMT WHERE MTID=?";

                    Connection con = null;
                    PreparedStatement ps = null;
                    ResultSet rs = null;
                    InventoryAdjustment ia = null;

                    try {
                            con = getConnection();
                            ps = con.prepareStatement(query);

                            ps.setString(1, id);

                            rs = ps.executeQuery();

                            while (rs.next()) {

                                    String iitemNo = rs.getString("item_Code");
                                    String idate = formatDate(rs.getDate("trans_Date"));
                                    String iperiod = "";//rs.getString("period");
                                    String iwarehouse = rs.getString("wareHouse_Code");
                                    String idescription = rs.getString("description");
                                    int iquantity = rs.getInt("quantity");
                                    int userId = rs.getInt("userId");
                                    String posted = rs.getString("POSTED");
                                    String adjustOpt = rs.getString("ADJUST_OPTION");
                                    ia = new InventoryAdjustment(id, iitemNo, idate, iperiod,
                                                    iwarehouse, idescription, iquantity,userId,posted,adjustOpt);
                            }

                    } catch (Exception er) {
                            System.out.println("Error finding Inventory Adjustment By ID ->"
                                            + er);
                    } finally {
                            closeConnection(con, ps, rs);
                    }

                    return ia;
            }
            
    public boolean postAdjustment2InventoryHistory(String itemCode,String transDesc,int quantity,String warehouseCode,int userId){
    
        String CREATE_QUERY = "INSERT INTO ST_INVENTORY_HISTORY (MTID,ITEM_CODE,TRANS_DESC,QUANTITY,WAREHOUSE_CODE,TRANS_DATE,BATCH_CODE,USERID) " +
                                 "VALUES (?,?,?,?,?,?,?,?)";
        String updateQuery = "UPDATE ST_INVENTORY_HISTORY SET BALANCE = BALANCE + ? ,TMP_BALANCE = TMP_BALANCE + ? WHERE ITEM_CODE = ? AND WAREHOUSE_CODE = ?";
        String insertQuery = "INSERT INTO ST_INVENTORY_HISTORY (BALANCE,USERID,ITEM_CODE,WAREHOUSE_CODE) VALUES (?,?,?,?)";
        //Connection con = null;
        boolean autoCommit = false;
        Connection con = null;
        PreparedStatement ps = null;         
        id = helper.getGeneratedId("ST_INVENTORY_HISTORY");
        boolean done = false;
        boolean isItemExist = false;
        int j = 0;
        int k = 0;
        try{
           
           isItemExist = sos.isItemExistInInventoryTotals(itemCode,warehouseCode);
           con = getConnection();
           autoCommit = con.getAutoCommit();
           con.setAutoCommit(false);
           //insert history
           ps = con.prepareStatement(CREATE_QUERY);
           ps.setString(1,id);
           ps.setString(2,itemCode);
           ps.setString(3,transDesc);
           ps.setInt(4,quantity);
           ps.setString(5,warehouseCode);
           ps.setDate(6,dateConvert(new java.util.Date()));
           ps.setString(7,id);
           ps.setInt(8,userId);
           j = ps.executeUpdate();
                    
           if(isItemExist){
            ps = con.prepareStatement(updateQuery);     
            ps.setInt(1,quantity);
            ps.setInt(2,quantity);
            ps.setString(3,itemCode);
            ps.setString(4,warehouseCode);
            k = ps.executeUpdate();
            }
            else{
             ps = con.prepareStatement(insertQuery);
             ps.setInt(1,quantity);
             ps.setInt(2,userId);
             ps.setString(3,itemCode);
             ps.setString(4,warehouseCode); 
             k = ps.executeUpdate();
            }           
            if((j != -1) && (k != -1))
             {
             con.commit();
             con.setAutoCommit(autoCommit);
             done = true;
            }              
           }catch(Exception er){
                    System.out.println("Error Posting Adjustment 2 Inventory History/Totals ->"+er.getMessage());
                    er.printStackTrace();
                try{
                    con.rollback();
                    done = false;
                }
                catch(SQLException ex){
                    System.out.println("Error In Transaction....Rollback ->"+er.getMessage());
                    er.printStackTrace(); 
                }       
                 
            }finally{
                    closeConnection(con,ps);
            }
          
        return done;
    }
    
    //post Adjustment 2 update Inventory Items/Totals
    public boolean postAdjustment2InventoryTotals(String itemCode,int quantity,String warehouseCode,int userId){
                
        String updateQuery = "UPDATE ST_INVENTORY_HISTORY SET BALANCE = BALANCE + ? WHERE ITEM_CODE = ? AND WAREHOUSE_CODE = ?";
        String insertQuery = "INSERT INTO ST_INVENTORY_HISTORY (BALANCE,USERID,ITEM_CODE,WAREHOUSE_CODE) VALUES (?,?,?,?)";
        Connection con = null;
        PreparedStatement ps = null;         
        //id = helper.getGeneratedId("IA_PO_DELIVERY_ITEM");
        boolean done = false;
        
        try
        {
          con = getConnection();
          if(sos.isItemExistInInventoryTotals(itemCode,warehouseCode)){
            ps = con.prepareStatement(updateQuery);     
            ps.setInt(1,quantity);
            ps.setString(2,itemCode);
            ps.setString(3,warehouseCode);
            done = (ps.executeUpdate() != -1);
          }
          else{
            ps = con.prepareStatement(insertQuery);
            ps.setInt(1,quantity);
            ps.setInt(2,userId);
            ps.setString(3,itemCode);
            ps.setString(4,warehouseCode);
            done = (ps.executeUpdate() != -1);
          }
         }
        catch(Exception er){
              System.out.println("Error posting Adjustment 2 Inventory Totals... ->"+er.getMessage());
              er.printStackTrace();
              done = false;
        }
        finally{
                    closeConnection(con,ps);
            }
        return done;
     }      
    
    
    public boolean postAdjustment2InventoryHistory(String itemCode,String transDesc,int quantity,String warehouseCode,int userId,String adjusted,String companyCode){
    
        String CREATE_QUERY = "INSERT INTO ST_INVENTORY_HISTORY (MTID,ITEM_CODE,TRANS_DESC,QUANTITY,WAREHOUSE_CODE,TRANS_DATE,BATCH_CODE,PREVIOUS_BALANCE,USERID,COMP_CODE) " +
                                 "VALUES (?,?,?,?,?,?,?,?,?,?)";
        String updateQuery = "UPDATE ST_INVENTORY_HISTORY SET BALANCE = BALANCE + ?,TMP_BALANCE = TMP_BALANCE + ? WHERE ITEM_CODE = ? AND WAREHOUSE_CODE = ?";
        String insertQuery = "INSERT INTO ST_INVENTORY_HISTORY (BALANCE,USERID,ITEM_CODE,WAREHOUSE_CODE) VALUES (?,?,?,?)";
        //Connection con = null;
        boolean autoCommit = false;
        Connection con = null; 
        PreparedStatement ps = null;         
        id = helper.getGeneratedId("ST_INVENTORY_HISTORY");
        boolean done = false;
        boolean isItemExist = false;
        int j = 0;
        int k = 0;
        try{
           
           isItemExist = sos.isItemExistInInventoryTotals(itemCode,warehouseCode);
           con = getConnection();
           autoCommit = con.getAutoCommit();
           con.setAutoCommit(false);
           //insert history
           ps = con.prepareStatement(CREATE_QUERY);
           ps.setString(1,id);
           ps.setString(2,itemCode);
           ps.setString(3,transDesc);
           ps.setInt(4,quantity);
           ps.setString(5,warehouseCode);
           ps.setDate(6,dateConvert(new java.util.Date()));
           ps.setString(7,id);
		   ps.setString(8,adjusted);
           ps.setInt(9,userId);
		   ps.setString(10,companyCode);
           j = ps.executeUpdate();
                    
           if(isItemExist){
            ps = con.prepareStatement(updateQuery);     
            ps.setInt(1,quantity);
            ps.setInt(2,quantity);
            ps.setString(3,itemCode);
            ps.setString(4,warehouseCode);
            k = ps.executeUpdate();
            }
            else{
             ps = con.prepareStatement(insertQuery);
             ps.setInt(1,quantity);
             ps.setInt(2,userId);
             ps.setString(3,itemCode);
             ps.setString(4,warehouseCode);
             k = ps.executeUpdate();
            }           
            if((j != -1) && (k != -1))
             {
             con.commit();
             con.setAutoCommit(autoCommit);
             done = true;
            }              
           }catch(Exception er){
                    System.out.println("Error Posting Adjustment 2 Inventory History/Totals ->"+er.getMessage());
                    er.printStackTrace();
                try{
                    con.rollback();
                    done = false;
                }
                catch(SQLException ex){
                    System.out.println("Error In Transaction....Rollback ->"+er.getMessage());
                    er.printStackTrace(); 
                }       
                 
            }finally{
                    closeConnection(con,ps);
            }
          
        return done;
    }
    
    public boolean postStockInventoryHistory(String itemCode,String transDesc,int quantity,String warehouseCode,int userId,String adjusted,String companyCode){
    
        String CREATE_QUERY = "INSERT INTO ST_INVENTORY_HISTORY (MTID,ITEM_CODE,TRANS_DESC,QUANTITY,WAREHOUSE_CODE,TRANS_DATE,BATCH_CODE,PREVIOUS_BALANCE,USERID,COMP_CODE) " +
                                 "VALUES (?,?,?,?,?,?,?,?,?,?)";
        boolean autoCommit = false;
        Connection con = null; 
        PreparedStatement ps = null;         
        id = helper.getGeneratedId("ST_INVENTORY_HISTORY");
        boolean done = false;
        boolean isItemExist = false;
        int j = 0;
        int k = 0;
        try{
           
           isItemExist = sos.isItemExistInInventoryTotals(itemCode,warehouseCode);
           con = getConnection();
           autoCommit = con.getAutoCommit();
           con.setAutoCommit(false);
           //insert history
           ps = con.prepareStatement(CREATE_QUERY);
           ps.setString(1,id);
           ps.setString(2,itemCode);
           ps.setString(3,transDesc);
           ps.setInt(4,quantity);
           ps.setString(5,warehouseCode);
           ps.setDate(6,dateConvert(new java.util.Date()));
           ps.setString(7,id);
		   ps.setString(8,adjusted);
           ps.setInt(9,userId);
		   ps.setString(10,companyCode);
           j = ps.executeUpdate();
         
            if(j != -1)
             {
             con.commit();
             con.setAutoCommit(autoCommit);
             done = true;
            }              
           }catch(Exception er){
                    System.out.println("Error Posting Transaction to Inventory History ->"+er.getMessage());
                    er.printStackTrace();
                try{
                    con.rollback();
                    done = false;
                }
                catch(SQLException ex){
                    System.out.println("Error In Transaction....Rollback ->"+er.getMessage());
                    er.printStackTrace(); 
                }       
                 
            }finally{
                    closeConnection(con,ps);
            }
          
        return done;
    }
        
}
