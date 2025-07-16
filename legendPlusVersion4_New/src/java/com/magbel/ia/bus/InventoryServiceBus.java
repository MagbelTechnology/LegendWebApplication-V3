package com.magbel.ia.bus;

import java.text.SimpleDateFormat;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import com.magbel.ia.dao.PersistenceServiceDAO;
import com.magbel.ia.util.ApplicationHelper;
import com.magbel.ia.util.CodeGenerator;
import com.magbel.ia.vao.SalesInvoice;
import com.magbel.ia.vao.WareHouse;
import com.magbel.ia.vao.ItemType;
import com.magbel.ia.vao.InventoryAdjustment;
import com.magbel.ia.vao.InventoryHistory;
import com.magbel.ia.vao.InventoryItem;
import com.magbel.ia.vao.InventoryReceipt;
import com.magbel.ia.vao.InventoryTotal;
import com.magbel.ia.vao.InventoryTotals;
import com.magbel.ia.vao.ItemApprovalDetail;
import com.magbel.ia.vao.WarehouseTransfer; 










import java.sql.SQLException;


public class InventoryServiceBus extends PersistenceServiceDAO {

    SimpleDateFormat sdf;

            final String space = "  ";

            final String comma = ",";
            java.util.Date date;
            com.magbel.util.DatetimeFormat df;
            public String auotoGenCode = "";

            ApplicationHelper helper;
            CodeGenerator cg;
            public String id;
            SalesOrderServiceBus sos;

    public InventoryServiceBus() {
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        df = new com.magbel.util.DatetimeFormat();
        helper = new ApplicationHelper();
        cg = new CodeGenerator();
        sos = new SalesOrderServiceBus();

    }
    
    public boolean createWareHouse(String warehouseCode,String name,int userId,String address,String branchCode,String status,String companyCode){

                String CREATE_QUERY = "INSERT INTO ST_WAREHOUSE "
                                + "(WAREHOUSE_CODE,NAME,MTID,USERID,ADDRESS,BRANCH_CODE,status,COMP_CODE) " + "VALUES(?,?,?,?,?,?,?,?)";
                Connection con = null;
                PreparedStatement ps = null;
                boolean done = false;
                String isAutoGen = getCodeName("SELECT auto_generate_ID FROM AM_GB_COMPANY");
                id = helper.getGeneratedId("ST_WAREHOUSE");
                warehouseCode = isAutoGen.trim().equalsIgnoreCase("Y") ? id : warehouseCode;//cg.generateCode("INVENTORY","","","") : warehouseCode;
                auotoGenCode = warehouseCode;
                //String status = 
                
                try {

                        con = getConnection();
                        ps = con.prepareStatement(CREATE_QUERY);
                        ps.setString(1, auotoGenCode);
                        ps.setString(2, name);
                        ps.setString(3, id);
                        ps.setInt(4,userId);
                       ps.setString(5,address);
                       ps.setString(6,branchCode);
                       ps.setString(7,status);
					   ps.setString(8,companyCode);
                    done = (ps.executeUpdate() != -1);

                } catch (Exception er) {
                        System.out.println("Error creating WareHouse... ->"
                                        + er.getMessage());
                } finally {
                        closeConnection(con, ps);
                }
                   return done;
        }

        /**
         * Deletes a WareHouse
         * 
         * @param id
         *            String
         */
        public void deleteWareHouse(String id) 
		{
                String DELETE_QUERY = "DELETE FROM ST_WAREHOUSE WHERE MTID = ?";
                Connection con = null;
                PreparedStatement ps = null;
                try {

                        con = getConnection();
                        ps = con.prepareStatement(DELETE_QUERY);
                        ps.setString(1, id);

                        ps.execute();

                } catch (Exception er) {
                        System.out.println("Error Deleting WAREHOUSE... ->"
                                        + er.getMessage());
                } finally {
                        closeConnection(con, ps);
                }

        }

        public boolean updateWareHouse(String warehouseCode,String name, String address,String branchCode,String status) {

                String UPDATE_QUERY = "UPDATE ST_WAREHOUSE "
                                + "SET NAME=?,ADDRESS=?,BRANCH_CODE=?,status=? " + " WHERE WAREHOUSE_CODE = ?";
                Connection con = null;
                PreparedStatement ps = null;
                boolean done = false;
                try {

                        con = getConnection();
                        ps = con.prepareStatement(UPDATE_QUERY);
                        ps.setString(1, name);
                        ps.setString(2,address);
                        ps.setString(3,branchCode);
                        ps.setString(4,status);
                        ps.setString(5,warehouseCode);
                        
                    done = (ps.executeUpdate() != -1);

                } catch (Exception er) {
                        System.out.println("Error UPDATING WAREHOUSE... ->"
                                        + er.getMessage());
                } finally {
                        closeConnection(con, ps);
                }
               return done;
        }

        public ArrayList findAllWareHouse() 
		{
                ArrayList records = new ArrayList();
                String SELECT_QUERY = "SELECT MTID, WAREHOUSE_CODE,NAME,USERID,ADDRESS,BRANCH_CODE,status"
                                + " FROM ST_WAREHOUSE";
                Connection con = null;
                PreparedStatement ps = null;
                ResultSet rs = null;

                try {

                        con = getConnection();
                        ps = con.prepareStatement(SELECT_QUERY);

                        rs = ps.executeQuery();

                        while (rs.next()) {

                                String code = rs.getString("WAREHOUSE_CODE");
                                String name = rs.getString("NAME");
                                String id = rs.getString("MTID");
                                String userId = rs.getString("USERID");
                                String address = rs.getString("ADDRESS");
                                String branchCode = rs.getString("BRANCH_CODE");
                                String status = rs.getString("status");
                                WareHouse wareHouse = new WareHouse(id,code,name,userId,address,branchCode,status);

                                records.add(wareHouse);
                        }

                } catch (Exception er) {
                        System.out.println("Error finding All WAREHOUSE...->"
                                        + er.getMessage());
                } finally {
                        closeConnection(con, ps, rs);
                }

                return records;
        }
    public ArrayList findAllWareHouseByQuery(String filter2,String filter) 
	{
            ArrayList records = new ArrayList();
            String SELECT_QUERY = "SELECT MTID,WAREHOUSE_CODE,NAME,USERID,ADDRESS,BRANCH_CODE,STATUS"
                            + " FROM ST_WAREHOUSE WHERE COMP_CODE = '"+filter2+"' AND MTID IS NOT NULL "+filter;
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
               con = getConnection();
               ps = con.prepareStatement(SELECT_QUERY);
               rs = ps.executeQuery();
               while (rs.next()) {
                String code = rs.getString("WAREHOUSE_CODE");
                String name = rs.getString("NAME");
                String id = rs.getString("MTID");
                String userId = rs.getString("USERID");
                String address = rs.getString("ADDRESS");
                    String branchCode = rs.getString("branch_code");
                String status = rs.getString("status");
                WareHouse wareHouse = new WareHouse(id,code,name,userId,address,branchCode,status);
                records.add(wareHouse);
                }

            } catch (Exception er) {
                    System.out.println("Error finding All WAREHOUSE...->"
                                    + er.getMessage());
            } finally {
                    closeConnection(con, ps, rs);
            }

            return records;
    }
        public WareHouse findWareHouseById(String id) 
		{
                String FIND_QUERY = "SELECT MTID,WAREHOUSE_CODE,NAME,USERID,ADDRESS,BRANCH_CODE,status "
                                + "FROM ST_WAREHOUSE WHERE  WAREHOUSE_CODE=?";

                Connection con = null;
                PreparedStatement ps = null;
                ResultSet rs = null;
                WareHouse wareHouse = null;

                try {
                        con = getConnection();
                        ps = con.prepareStatement(FIND_QUERY);

                        ps.setString(1, id);

                        rs = ps.executeQuery();

                        while (rs.next()) {

                                String code = rs.getString("WAREHOUSE_CODE");
                                String name = rs.getString("NAME");
                            String userId = rs.getString("USERID");
                            String address = rs.getString("ADDRESS");
                            String branchCode = rs.getString("BRANCH_CODE");
                            String status = rs.getString("status");
                            
                           wareHouse = new WareHouse(id,code,name,userId,address,branchCode,status);

                        }

                } catch (Exception er) {
                        System.out.println("Error finding WAREHOUSEByID ->" + er);
                } finally {
                        closeConnection(con, ps, rs);
                }

                return wareHouse;
        }

        public boolean isWareHouseExisting(String id) 
		{
                boolean exists = false;
                String updateQuery = "SELECT count(WAREHOUSE_CODE) FROM ST_WAREHOUSE "
                                + "WHERE  WAREHOUSE_CODE = ?";
                Connection con = null;
                PreparedStatement ps = null;
                ResultSet rs = null;

                try {

                        con = getConnection();
                        ps = con.prepareStatement(updateQuery);

                        ps.setString(1, id);
                        rs = ps.executeQuery();

                        while (rs.next()) {
                                int counted = rs.getInt(1);
                                if (counted > 0) {
                                        exists = true;
                                }
                        }

                } catch (Exception er) {
                        System.out.println("Error in isWareHouseExisting()... ->" + er);
                } finally {
                        closeConnection(con, ps);
                }

                return exists;
        }

            /**
             * Create new ItemType createItemType
             * 
             * @param code
             *            String;
             * @param name
             *            String;
             */
                 
             
            public boolean createItemType(String itemTypeCode,String companyCode,String name,int userId,String inventory,String costMethod,String status,String categoryCode,String unitMeasurment) 
			{  
                    String CREATE_QUERY = "INSERT INTO ST_ITEMTYPE "
                                    + "(ITEMTYPE_CODE,COMP_CODE,NAME,MTID,USERID,IS_INVENTORY,COST_METHOD,status,CATEGORY_CODE,UNIT_MEASUREMENT) " + "VALUES(?,?,?,?,?,?,?,?,?,?)";
                    Connection con = null;
                    PreparedStatement ps = null;
                 //   id = helper.getGeneratedId("ST_ITEMTYPE");
                    
                boolean done = false;
                String isAutoGen = getCodeName("SELECT auto_generate_ID FROM AM_GB_COMPANY");
                if(isAutoGen.equalsIgnoreCase("Y")){itemTypeCode = helper.getGeneratedId("ST_ITEMTYPE");}
//                itemTypeCode = isAutoGen.trim().equalsIgnoreCase("Y") ? id : itemTypeCode;//cg.generateCode("INVENTORY","","","") : warehouseCode;
//                auotoGenCode = itemTypeCode;
                    try {

                            con = getConnection();
                            ps = con.prepareStatement(CREATE_QUERY);
                            ps.setString(1, itemTypeCode);
                            ps.setString(2,companyCode);
							ps.setString(3,name);
                            ps.setString(4, itemTypeCode);
                            ps.setInt(5,userId);
                           ps.setString(6,inventory);
                           ps.setString(7,costMethod);
                           ps.setString(8,status);
                           ps.setString(9,categoryCode);
                           ps.setString(10,unitMeasurment);
                        done = (ps.executeUpdate() != -1);

                    } catch (Exception er) {
                            System.out.println("Error creating ItemType in createItemType... ->"
                                            + er.getMessage());
                    } finally {
                            closeConnection(con, ps);
                    }
                   return done;
            }

            /**
             * Deletes a ItemType deleteItemType
             * 
             * @param id
             *            String
             */
            public void deleteItemType(String id) 
			{
                    String DELETE_QUERY = "DELETE FROM ST_ITEMTYPE WHERE MTID = ?";
                    Connection con = null;
                    PreparedStatement ps = null;
                    try {

                            con = getConnection();
                            ps = con.prepareStatement(DELETE_QUERY);
                            ps.setString(1, id);

                            ps.execute();

                    } catch (Exception er) {
                            System.out.println("Error Deleting ItemType... ->"
                                            + er.getMessage());
                    } finally {
                            closeConnection(con, ps);
                    }

            }

            public boolean updateItemType(String name, String itemTypeCode,String inventory,String costMethod,String status, String categoryCode,String unitMeasurment) 
			{
                    String UPDATE_QUERY = "UPDATE  ST_ITEMTYPE "
                                    + "SET STATUS=?,NAME=?,IS_INVENTORY=?,COST_METHOD=?, CATEGORY_CODE = ?, UNIT_MEASUREMENT = ? " + " WHERE ITEMTYPE_CODE = ?";
                    Connection con = null;
                    PreparedStatement ps = null;
                    boolean done = false;
                    try {

                            con = getConnection();
                            ps = con.prepareStatement(UPDATE_QUERY);
                            ps.setString(1,status);
                            ps.setString(2, name);
                            ps.setString(3,inventory);
                            ps.setString(4,costMethod);
                            ps.setString(5,categoryCode);
                            ps.setString(6,unitMeasurment);
                            ps.setString(7,itemTypeCode);
                             
                           done = (ps.executeUpdate() != -1);

                    } catch (Exception er) {
                            System.out.println("Error UPDATING ItemType... ->"
                                            + er.getMessage());
                    } finally {
                            closeConnection(con, ps);
                    }
                    return done;
            }

            public ArrayList findAllItemType() 
			{
                    ArrayList records = new ArrayList();
                    String SELECT_QUERY = "SELECT MTID, ITEMTYPE_CODE,NAME,USERID,IS_INVENTORY,COST_METHOD,status,CATEGORY_CODE, UNIT_MEASUREMENT"
                                    + " FROM ST_ITEMTYPE";
                    Connection con = null;
                    PreparedStatement ps = null;
                    ResultSet rs = null;

                    try {

                            con = getConnection();
                            ps = con.prepareStatement(SELECT_QUERY);

                            rs = ps.executeQuery();

                            while (rs.next()) {

                                    String code = rs.getString("ITEMTYPE_CODE");
                                    String name = rs.getString("NAME");
                                    String id = rs.getString("MTID");
                                    String userId = rs.getString("USERID");
                                    String inventory = rs.getString("IS_INVENTORY");
                                    String costMethod = rs.getString("COST_METHOD");
                                    String status = rs.getString("status");
                                    String categoryCode = rs.getString("CATEGORY_CODE");
                                    String unitMeasurment = rs.getString("UNIT_MEASUREMENT");
                                    ItemType itemtype = new ItemType(id,code,name,userId,inventory,costMethod,status);
                                    itemtype.setCategoryCode(categoryCode);
                                    itemtype.setUnitMeasurment(unitMeasurment);
                                    records.add(itemtype);
                            }

                    } catch (Exception er) {
                            System.out.println("Error finding All ItemType...->"
                                            + er.getMessage());
                    } finally {
                            closeConnection(con, ps, rs);
                    }

                    return records;
            }
    public ArrayList findAllItemTypeByQuery(String filter2,String filter) 
	{
            ArrayList records = new ArrayList();
            String SELECT_QUERY = "SELECT MTID, ITEMTYPE_CODE,NAME,USERID,IS_INVENTORY,COST_METHOD,STATUS,CATEGORY_CODE,UNIT_MEASUREMENT "
                            + " FROM ST_ITEMTYPE WHERE COMP_CODE='"+filter2+"' AND MTID IS NOT NULL "+filter;
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try {

                    con = getConnection();
                    ps = con.prepareStatement(SELECT_QUERY);

                    rs = ps.executeQuery();

                    while (rs.next()) {

                            String code = rs.getString("ITEMTYPE_CODE");
                            String name = rs.getString("NAME");
                            String id = rs.getString("MTID");
                            String userId = rs.getString("USERID");
                            String inventory = rs.getString("IS_INVENTORY");
                            String costMethod = rs.getString("COST_METHOD");
                            String status = rs.getString("status");
                            String categoryCode = rs.getString("CATEGORY_CODE");
                            String unitMeasurment = rs.getString("UNIT_MEASUREMENT");
                            ItemType itemtype = new ItemType(id,code,name,userId,inventory,costMethod,status);
                            itemtype.setCategoryCode(categoryCode);
                            itemtype.setUnitMeasurment(unitMeasurment);
                            records.add(itemtype);
                    }

            } catch (Exception er) {
                    System.out.println("Error finding All ItemType By Query...->"
                                    + er.getMessage());
            } finally {
                    closeConnection(con, ps, rs);
            }

            return records;
    }
            public ItemType findItemTypeById(String id) 
			{

                    String FIND_QUERY = "SELECT MTID,ITEMTYPE_CODE,NAME,USERID,IS_INVENTORY,COST_METHOD,status,CATEGORY_CODE,UNIT_MEASUREMENT  "
                                    + "FROM ST_ITEMTYPE WHERE  MTID=?";

                    Connection con = null;
                    PreparedStatement ps = null;
                    ResultSet rs = null;
                    ItemType itemtype = null;

                    try {
                            con = getConnection();
                            ps = con.prepareStatement(FIND_QUERY);

                            ps.setString(1, id);

                            rs = ps.executeQuery();

                            while (rs.next()) {

                                    String code = rs.getString("ITEMTYPE_CODE");
                                    String name = rs.getString("NAME");
                                String userId = rs.getString("USERID");
                                String inventory = rs.getString("IS_INVENTORY");
                                String costMethod = rs.getString("COST_METHOD");
                                String status = rs.getString("status");
                                String categoryCode = rs.getString("CATEGORY_CODE");
                                String unitMeasurment = rs.getString("UNIT_MEASUREMENT");
                                    itemtype = new ItemType(id, code, name,userId,inventory,costMethod,status);
                                    itemtype.setCategoryCode(categoryCode);
                                    itemtype.setUnitMeasurment(unitMeasurment);
                            }

                    } catch (Exception er) {
                            System.out.println("Error finding ItemTypeByID ->" + er);
                    } finally {
                            closeConnection(con, ps, rs);
                    }

                    return itemtype;
            }

            public boolean isItemTypeExisting(String id) 
			{
                    boolean exists = false;
                    String updateQuery = "SELECT count(ITEMTYPE_CODE) FROM ST_ITEMTYPE "
                                    + "WHERE  ITEMTYPE_CODE = ?";
                    Connection con = null;
                    PreparedStatement ps = null;
                    ResultSet rs = null;

                    try {

                            con = getConnection();
                            ps = con.prepareStatement(updateQuery);

                            ps.setString(1, id);
                            rs = ps.executeQuery();

                            while (rs.next()) {
                                    int counted = rs.getInt(1);
                                    if (counted > 0) {
                                            exists = true;
                                    }
                            }

                    } catch (Exception er) {
                            System.out.println("Error in isItemTypeExisting()... ->" + er);
                    } finally {
                            closeConnection(con, ps);
                    }

                    return exists;
            }

            

            public boolean createInventoryReceipt(String orderNo, String receiptNo,
                            String vendorCode, String date, int period, int quantityReceived,
                            int quantityOrder, String vendorItemNo, String description,
                            double unitCost, String overrideDescription) 
			{
                    String query = "INSERT INTO ST_INVENTORY_RECEIPT"
                                    + "( ORDERNO, RECEIPTNO, VENDORCODE,"
                                    + "TRANSDATE, PERIOD, QUANTITY_RECEIVED, QUANTITY_ORDER,"
                                    + "VENDOR_ITEM_NO, DESCRIPTION, UNITCOST, OVERRIDE_DESC,MTID)"
                                    + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";

                    Connection con = null;
                    PreparedStatement ps = null;
                    boolean done = false;

                    try {

                            con = getConnection();
                            ps = con.prepareStatement(query);
                            ps.setString(1, orderNo);
                            ps.setString(2, receiptNo);
                            ps.setDate(3, df.dateConvert(date));
                            ps.setString(4, description);
                            ps.setInt(5, period);
                            ps.setInt(6, quantityReceived);
                            ps.setInt(7, quantityOrder);
                            ps.setString(8, vendorItemNo);
                            ps.setString(9, description);
                            ps.setDouble(10, unitCost);
                            ps.setString(11, overrideDescription);
                            ps.setString(12, helper.getGeneratedId("ST_INVENTORY_RECEIPT"));

                            done = (ps.executeUpdate() != -1);
                    } catch (Exception ex) {
                            System.out.println("ERROR Creating InventoryReceipt"
                                            + ex.getMessage());
                            ex.printStackTrace();
                    } finally {
                            closeConnection(con, ps);
                    }
                    return done;
            }

            public boolean updateInventoryReceipt(String id, String receiptNo,
                            String vendorCode, int period, int quantityReceived,
                            int quantityOrder, String vendorItemNo, String description,
                            double unitCost, String overrideDescription) 
			{
                    String query = " UPDATE ST_INVENTORY_RECEIPT "
                                    + " SET  RECEIPTNO=?, VENDORCODE=?, PERIOD=?, QUANTITY_RECEIVED=?, "
                                    + " PERIOD=?, QUANTITY_RECEIVED=?, QUANTITY_ORDER=?, VENDOR_ITEM_NO=?, "
                                    + " DESCRIPTION=?, UNITCOST=?, OVERRIDE_DESC=? "
                                    + " WHERE MTID=?";

                    Connection con = null;
                    PreparedStatement ps = null;
                    boolean done = false;

                    try {
                            ps = con.prepareStatement(query);
                            ps.setString(1, receiptNo);
                            ps.setString(2, vendorCode);
                            ps.setInt(3, period);
                            ps.setInt(4, quantityReceived);
                            ps.setInt(5, quantityOrder);
                            ps.setString(6, vendorItemNo);
                            ps.setString(7, description);
                            ps.setString(9, description);
                            ps.setDouble(10, unitCost);
                            ps.setString(11, overrideDescription);
                            ps.setString(12, id);
                            done = (ps.executeUpdate() != -1);
                    } catch (Exception ex) {
                            System.out.println("ERROR Updating InventoryReceipt "
                                            + ex.getMessage());
                            ex.printStackTrace();
                    } finally {
                            closeConnection(con, ps);
                    }
                    return done;
            }
/*
            public ArrayList findInventoryAdjustment() {

                    ArrayList records = new ArrayList();
                    String query = "SELECT MTID, ITEMCODE, TRANSDATE,PERIOD, "
                                    + "WAREHOUSECODE, DESCRIPTION, QUANTITY,USERID,POSTED "
                                    + " FROM ST_INVENTORY_ADJUSTMT ";
                    Connection con = null;
                    PreparedStatement ps = null;
                    ResultSet rs = null;

                    try {

                            con = getConnection();
                            ps = con.prepareStatement(query);

                            rs = ps.executeQuery();

                            while (rs.next()) {

                                    String id = rs.getString("MTID");
                                    String itemNo = rs.getString("itemCode");
                                    String date = formatDate(rs.getDate("transDate"));
                                    String period = rs.getString("period");
                                    String warehouse = rs.getString("wareHouseCode");
                                    String description = rs.getString("description");
                                    String quantity = rs.getString("quantity");
                                    String userId = rs.getString("userId");
                                    String posted = rs.getString("POSTED");
                                    InventoryAdjustment ia = new InventoryAdjustment(id, itemNo,
                                                    date, period, warehouse, description, quantity,userId,posted);
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

                    String query = "SELECT MTID, ITEMCODE, TRANSDATE,PERIOD, "
                                    + "WAREHOUSECODE, DESCRIPTION, QUANTITY,USERID,POSTED "
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

                                    String iitemNo = rs.getString("itemCode");
                                    String idate = formatDate(rs.getDate("transDate"));
                                    String iperiod = rs.getString("period");
                                    String iwarehouse = rs.getString("wareHouseCode");
                                    String idescription = rs.getString("description");
                                    String iquantity = rs.getString("quantity");
                                    String userId = rs.getString("userId");
                                    String posted = rs.getString("POSTED");
                                    ia = new InventoryAdjustment(id, iitemNo, idate, iperiod,
                                                    iwarehouse, idescription, iquantity,userId,posted);
                            }

                    } catch (Exception er) {
                            System.out.println("Error finding Inventory Adjustment By ID ->"
                                            + er);
                    } finally {
                            closeConnection(con, ps, rs);
                    }

                    return ia;
            }
*/
            private java.util.ArrayList<InventoryReceipt> getInventoryReceipts(
                            String filter) 
			{
                    java.util.ArrayList<InventoryReceipt> irList = new java.util.ArrayList<InventoryReceipt>();

                    InventoryReceipt invr = null;

                    Connection con = null;
                    PreparedStatement ps = null;
                    ResultSet rs = null;

                    String query = "SELECT MTID, ORDERNO, RECEIPTNO, VENDORCODE, "
                                    + "TRANSDATE, PERIOD,QUANTITY_RECEIVED, QUANTITY_ORDER,"
                                    + " VENDOR_ITEM_NO, DESCRIPTION, UNITCOST, OVERRIDE_DESC "
                                    + " FROM ST_INVENTORY_RECEIPT ";
                    query += filter;
                    try {
                            con = getConnection();
                            ps = con.prepareStatement(query);

                            rs = ps.executeQuery();

                            while (rs.next()) {

                                    String orderNo = rs.getString("ORDERNO");
                                    String receiptNo = rs.getString("RECEIPTNO");
                                    String vendorCode = rs.getString("VENDORCODE");
                                    String date = sdf.format(rs.getDate("TRANSDATE"));
                                    int period = rs.getInt("PERIOD");
                                    int quantityReceived = rs.getInt("QUANTITY_RECEIVED");
                                    int quantityOrder = rs.getInt("QUANTITY_ORDER");
                                    String vendorItemNo = rs.getString("VENDOR_ITEM_NO");
                                    String description = rs.getString("DESCRIPTION");
                                    double unitCost = rs.getDouble("UNITCOST");
                                    String overrideDescription = rs.getString("OVERRIDE_DESC");
                                    String id = rs.getString("MTID");
                                    invr = new InventoryReceipt(id, orderNo, receiptNo, vendorCode,
                                                    date, period, quantityReceived, quantityOrder,
                                                    vendorItemNo, description, unitCost,
                                                    overrideDescription);

                                    irList.add(invr);

                            }

                    } catch (Exception ex) {
                            System.out.println("ERROR fetching Inventory Reciepts "
                                            + ex.getMessage());
                            ex.printStackTrace();
                    } finally {
                            closeConnection(con, ps, rs);
                    }

                    return irList;
            }

            private InventoryReceipt getAnInventoryReceipt(String filter) 
			{
                    // java.util.ArrayList<InventoryReceipt> irList = new
                    // java.util.ArrayList<InventoryReceipt>();

                    InventoryReceipt invr = null;

                    Connection con = null;
                    PreparedStatement ps = null;
                    ResultSet rs = null;

                    String query = "SELECT MTID, ORDERNO, RECEIPTNO, VENDORCODE, "
                                    + "TRANSDATE, PERIOD,QUANTITY_RECEIVED, QUANTITY_ORDER,"
                                    + " VENDOR_ITEM_NO, DESCRIPTION, UNITCOST, OVERRIDE_DESC "
                                    + " FROM ST_INVENTORY_RECEIPT ";
                    query += filter;
                    try {
                            con = getConnection();
                            ps = con.prepareStatement(query);

                            rs = ps.executeQuery();

                            while (rs.next()) {

                                    String orderNo = rs.getString("ORDERNO");
                                    String receiptNo = rs.getString("RECEIPTNO");
                                    String vendorCode = rs.getString("VENDORCODE");
                                    String date = sdf.format(rs.getDate("TRANSDATE"));
                                    int period = rs.getInt("PERIOD");
                                    int quantityReceived = rs.getInt("QUANTITY_RECEIVED");
                                    int quantityOrder = rs.getInt("QUANTITY_ORDER");
                                    String vendorItemNo = rs.getString("VENDOR_ITEM_NO");
                                    String description = rs.getString("DESCRIPTION");
                                    double unitCost = rs.getDouble("UNITCOST");
                                    String overrideDescription = rs.getString("OVERRIDE_DESC");
                                    String id = rs.getString("MTID");
                                    invr = new InventoryReceipt(id, orderNo, receiptNo, vendorCode,
                                                    date, period, quantityReceived, quantityOrder,
                                                    vendorItemNo, description, unitCost,
                                                    overrideDescription);
                                    // irList.add(invr);

                            }

                    } catch (Exception ex) {
                            System.out.println("ERROR fetching Inventory Reciepts "
                                            + ex.getMessage());
                            ex.printStackTrace();
                    } finally {
                            closeConnection(con, ps, rs);
                    }

                    return invr;
            }

            public java.util.ArrayList<InventoryReceipt> getInventoryReceiptsByQuery(
                            String filter) 
			{
                    java.util.ArrayList<InventoryReceipt> irList = new java.util.ArrayList<InventoryReceipt>();
                    String criteria = " WHERE MTID IS NOT NULL " + filter;
                    irList = getInventoryReceipts(criteria);
                    return irList;

            }

            public InventoryReceipt getInventoryReceiptsByID(String id) 
			{
                    InventoryReceipt invr = new InventoryReceipt();
                    String criteria = " WHERE MTID = ' " + id + "'";
                    invr = getAnInventoryReceipt(criteria);
                    return invr;

            }

            public boolean isInventoryAdjustmentExisting(String id) 
			{

                    boolean exists = false;
                    String updateQuery = "SELECT COUNT(MTID) FROM ST_INVENTORY_ADJUSTMT WHERE MTID=?";
                    Connection con = null;
                    PreparedStatement ps = null;
                    ResultSet rs = null;

                    try {

                            con = getConnection();
                            ps = con.prepareStatement(updateQuery);

                            ps.setString(1, id);
                            rs = ps.executeQuery();

                            while (rs.next()) {
                                    int counted = rs.getInt(1);
                                    if (counted > 0) {
                                            exists = true;
                                    }
                            }

                    } catch (Exception er) {
                            System.out
                                            .println("Error in is Inventory Adjustment Existing()... ->"
                                                            + er);
                    } finally {
                            closeConnection(con, ps);
                    }

                    return exists;
            }
            

            /**
             * Inventory Items
             *
             * @param formId String
            */


            
        public boolean createInventoryItem(String ItemNo, String Status, String Type, 
                String Description, String TaxCode, int MinimumQuantity, double WeightAvgCost, double StandardCost,double FIFO,
                double Weight, String BackOrderable, int UserId,int reorderLevel,String salesAcct,
                String COGSold,String inventoryAcct,String reqApproval,int maxApproveLevel,double minAmtLimit,
                double maxAmtLimit,String adjustAcct,int adjustMaxApproveLevel,double adjustMinAmtLimit,
                double adjustMaxAmtLimit,String companyCode,String receivable,String categoryCode,String unitcode)
				{ //passed
                
                 String isAutoGen = getCodeName("SELECT auto_generate_ID FROM AM_GB_COMPANY");
                
                        String CREATE_QUERY = "INSERT INTO  ST_INVENTORY_ITEMS( "+
                                              "MtId, Item_Code, Status, ItemType_Code, Description, Tax_Code , Min_Quantity, "+
                                              "Weight_Avg_Cost, Standard_Cost,FIFO, Weight, BackOrderable, UserId,Reorder_Level,"+
                                              "sales_Acct,COG_Sold,inventory_Acct,req_Approval,Max_Approve_Level,min_amt_limit,"+
                                              "max_amt_limit,adjust_acct,adj_Max_Approve_Level,adj_min_amt_limit,adj_max_amt_limit,comp_Code,receivable,CATEGORY_CODE,MEASURING_CODE) "+
                                              "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                        Connection con = null;
                        PreparedStatement ps = null;
                        boolean done = false;
                        id = helper.getGeneratedId("ST_INVENTORY_ITEMS");
                        
                    ItemNo = isAutoGen.trim().equalsIgnoreCase("Y") ? cg.generateCode("INVENTORY","","","") : ItemNo;
                    auotoGenCode = ItemNo;

                        try
                        {
                                con = getConnection();
                                ps = con.prepareStatement(CREATE_QUERY);
                                ps.setString(1, id);
                                ps.setString(2, ItemNo);
                                ps.setString(3, Status);
                                ps.setString(4, Type);
                                ps.setString(5, Description);
                                ps.setString(6, TaxCode);
                                ps.setInt(7, MinimumQuantity);
                                ps.setDouble(8, WeightAvgCost);
                                ps.setDouble(9, StandardCost);
								ps.setDouble(10,FIFO);
                                ps.setDouble(11, Weight);
                                ps.setString(12, BackOrderable);
                                ps.setInt(13, UserId);
                                ps.setInt(14,reorderLevel);
                                ps.setString(15,salesAcct);
                                ps.setString(16,COGSold);
                                ps.setString(17,inventoryAcct);
                                ps.setString(18,reqApproval);
                                ps.setInt(19,maxApproveLevel);
                                ps.setDouble(20,minAmtLimit);
                                ps.setDouble(21,maxAmtLimit);
                                ps.setString(22,adjustAcct);
                                ps.setInt(23,adjustMaxApproveLevel);
                                ps.setDouble(24,adjustMinAmtLimit);
                                ps.setDouble(25,adjustMaxAmtLimit);
								ps.setString(26,companyCode);
								ps.setString(27,receivable);
								ps.setString(28,categoryCode);
								ps.setString(29,unitcode);
                               done = (ps.executeUpdate() != -1);

                        }catch(Exception er){
                                System.out.println("Error creating InventoryItem... ->"+er.getMessage());
                                done = false;
                        }finally{
                                closeConnection(con,ps);
                        }
                          return done;
                }

                public void deleteInventoryItem(String id)
				{//Passed
                        String DELETE_QUERY = "DELETE FROM  ST_INVENTORY_ITEMS  WHERE MTID = ? ";
                    Connection con = null;
                        PreparedStatement ps = null;
                        try{

                                con = getConnection();
                                //con = getConnection2();
                                //if(con == null)
                                // {  con = getConnection();    }
                                ps = con.prepareStatement(DELETE_QUERY);
                                ps.setString(1, id);

                                ps.execute();


                        }catch(Exception er){
                                System.out.println("Error Deleting InventoryItem... ->"+er.getMessage());
                        }finally{
                                closeConnection(con,ps);
                        }

                }

               public boolean updateInventoryItem(String ItemNo, String Status, String Type, String Description, String TaxCode, int MinimumQuantity, 
                              double WeightAvgCost, double StandardCost,double FIFO, double Weight, String BackOrderable,int reorderLevel,String salesAcct,
                              String COGSoldAcct,String inventoryAcct,String reqApproval,int maxApproveLevel,double minAmtLimit,double maxAmtLimit,
                              String adjustAcct,int adjustMaxApproveLevel,double adjustMinAmtLimit,double adjustMaxAmtLimit,String receivable,String categoryCode)
				{//passed

                        String UPDATE_QUERY = "UPDATE ST_INVENTORY_ITEMS "+
                                              "SET Status = ?, ItemType_Code = ?, Description = ?, Tax_Code = ?,"+
                                              "Min_Quantity = ?, Weight_Avg_Cost = ?, Standard_Cost = ?,FIFO=?, Weight = ?, BackOrderable = ?,reorder_Level=?," +
                                              "sales_acct=?,COG_SOLD=?,inventory_acct=?,req_Approval=?,Max_Approve_Level=?,min_amt_limit=?,max_amt_limit=?,"+
                                              "ADJUST_ACCT=?,ADJ_MAX_APPROVE_LEVEL=?,ADJ_MIN_AMT_LIMIT=?," +
                                              "ADJ_MAX_AMT_LIMIT=?,receivable=?,CATEGORY_CODE=? WHERE item_code = ? ";
                        Connection con = null;
                        PreparedStatement ps = null;
                        boolean done = false;

                        try{
                                con = getConnection();
                                ps = con.prepareStatement(UPDATE_QUERY);
                                ps.setString(1,Status);
                                ps.setString(2,Type);
                                ps.setString(3,Description);
                                ps.setString(4,TaxCode);
                                ps.setInt(5,MinimumQuantity);
                                ps.setDouble(6,WeightAvgCost);
                                ps.setDouble(7,StandardCost);
								ps.setDouble(8,FIFO);
                                ps.setDouble(9,Weight);
                                ps.setString(10,BackOrderable);
                                ps.setInt(11,reorderLevel);
                                ps.setString(12,salesAcct);
                                ps.setString(13,COGSoldAcct);
                                ps.setString(14,inventoryAcct);
                                ps.setString(15,reqApproval);
                                ps.setInt(16,maxApproveLevel);
                                ps.setDouble(17,minAmtLimit);
                                ps.setDouble(18,maxAmtLimit);
                                ps.setString(19,adjustAcct);
                            ps.setInt(20,adjustMaxApproveLevel);
                            ps.setDouble(21,adjustMinAmtLimit);
                            ps.setDouble(22,adjustMaxAmtLimit);
							ps.setString(23,receivable);
							ps.setString(24,categoryCode);
                            ps.setString(25,ItemNo);

                            
                            done = (ps.executeUpdate() != -1);

                        }catch(Exception er){
                                System.out.println("Error UPDATING ST_INVENTORY_ITEMS... ->"+er.getMessage());
                                done = false;
                        }finally{
                                closeConnection(con,ps);
                        }
                       return done;
                }
      public ArrayList findAllInventoryItems()
	            {
                        String SELECT_QUERY = "SELECT * FROM ST_INVENTORY_ITEMS";

                        Connection con = null;
                        //Statement stmt = null;
                        PreparedStatement ps = null;
                        ResultSet rs = null;

                                ArrayList records = new java.util.ArrayList();
                          try{
                                 con = getConnection();
                                 ps = con.prepareStatement(SELECT_QUERY);
                                 rs  = ps.executeQuery();
                                 while(rs.next())
                                   {
                                         String mtid = rs.getString("MtId");
                                         String itemno = rs.getString("Item_Code");
                                         String status = rs.getString("Status");
                                         String type = rs.getString("ITEMTYPE_CODE");
                                         String description = rs.getString("Description");
                                         String taxcode = rs.getString("Tax_Code");
                                         int minqty = rs.getInt("Min_Quantity");
                                         double weightavgcost = Double.parseDouble(rs.getString("Weight_Avg_Cost"));
                                         double stdcost = Double.parseDouble(rs.getString("Standard_Cost"));
                                         double weight = Double.parseDouble(rs.getString("Weight"));
                                         String backorderable = rs.getString("BackOrderable");
                                         String userid = rs.getString("USERID");
                                         int reorderLevel = rs.getInt("reorder_Level");
                                         String salesAcct = rs.getString("sales_acct");
                                         String COGSoldAcct = rs.getString("COG_Sold");
                                         String inventoryAcct = rs.getString("inventory_Acct");
                                         String reqApproval = rs.getString("Req_Approval");
                                         int maxApproveLevel = rs.getInt("Max_Approve_Level");
                                         double minAmtLimit = rs.getDouble("min_Amt_Limit");
                                         double maxAmtLimit = rs.getDouble("max_amt_limit");
                                         String adjustAcct = rs.getString("adjustAcct");
                                         int adjustMaxApproveLevel = rs.getInt("adj_Max_Approve_Level");
                                         double adjustMinAmtLimit = rs.getDouble("adj_min_Amt_Limit");
                                         double adjustMaxAmtLimit = rs.getDouble("adj_max_amt_limit");
										 double fifo = rs.getDouble("FIFO");
										 String receivable = rs.getString("RECEIVABLE");
										 String categoryCode = rs.getString("CATEGORY_CODE");
										 String unitcode = rs.getString("MEASURING_CODE");

                                         InventoryItem invItem = new InventoryItem(mtid, itemno, status, type, description, taxcode, minqty, 
                                                                        weightavgcost,stdcost, weight, backorderable,userid,reorderLevel,
                                                                        salesAcct,COGSoldAcct,inventoryAcct,reqApproval,maxApproveLevel,
                                                                        minAmtLimit,maxAmtLimit,adjustAcct,adjustMaxApproveLevel,
                                                                        adjustMinAmtLimit,adjustMaxAmtLimit,fifo,receivable,categoryCode,unitcode);
                                         records.add(invItem);
                                         }
                        }catch(Exception er){
                                System.out.println("Error RETRIEVING All InventoryItem... ->"+er.getMessage());
                        }finally{
                                closeConnection(con,ps,rs);
                        }

                        return records ;
                }
    public ArrayList findAllInventoryItems(String filter2,String filter)
	{
                      String SELECT_QUERY = "SELECT * FROM ST_INVENTORY_ITEMS WHERE COMP_CODE='"+filter2+"' AND ITEM_CODE IS NOT NULL "+filter;

                      Connection con = null;
                      //Statement stmt = null;
                      PreparedStatement ps = null;
                      ResultSet rs = null;

                              ArrayList records = new java.util.ArrayList();
                        try{
                                con = getConnection();
                              
                               ps = con.prepareStatement(SELECT_QUERY);

                               rs  = ps.executeQuery();

                               while(rs.next())
                                 {
                                       String mtid = rs.getString("MtId");
                                       String itemno = rs.getString("Item_Code");
                                       String status = rs.getString("Status");
                                       String type = rs.getString("ITEMTYPE_CODE");
                                       String description = rs.getString("Description");
                                       String taxcode = rs.getString("Tax_Code");
                                       int minqty = rs.getInt("Min_Quantity");
                                       double weightavgcost = Double.parseDouble(rs.getString("Weight_Avg_Cost"));
                                       double stdcost = Double.parseDouble(rs.getString("Standard_Cost"));
									   //double FIFO = Double.parseDouble(rs.getString("FIFO"));
                                       double weight = Double.parseDouble(rs.getString("Weight"));
                                       String backorderable = rs.getString("BackOrderable");
                                       String userid = rs.getString("USERID");
                                       int reorderLevel = rs.getInt("reorder_Level");
                                       String salesAcct = rs.getString("sales_acct");
                                       String COGSoldAcct = rs.getString("COG_Sold");
                                       String inventoryAcct = rs.getString("inventory_Acct");
                                       String reqApproval = rs.getString("Req_Approval");
                                       int maxApproveLevel = rs.getInt("Max_Approve_Level");
                                       int minAmtLimit = rs.getInt("min_Amt_Limit");
                                       int maxAmtLimit = rs.getInt("max_amt_limit");
                                       String adjustAcct = rs.getString("adjust_Acct");
                                       int adjustMaxApproveLevel = rs.getInt("adj_Max_Approve_Level");
                                       double adjustMinAmtLimit = rs.getDouble("adj_min_Amt_Limit");
                                       double adjustMaxAmtLimit = rs.getDouble("adj_max_amt_limit");
									   double fifo = rs.getDouble("FIFO");
									   String receivable = rs.getString("RECEIVABLE");
									   String categoryCode = rs.getString("CATEGORY_CODE");
									   String unitcode = rs.getString("MEASURING_CODE");

                                       InventoryItem invItem = new InventoryItem(mtid, itemno, status, type, description, taxcode, minqty, 
                                                                      weightavgcost,stdcost, weight, backorderable,userid,reorderLevel,
                                                                      salesAcct,COGSoldAcct,inventoryAcct,reqApproval,maxApproveLevel,
                                                                      minAmtLimit,maxAmtLimit,adjustAcct,adjustMaxApproveLevel,adjustMinAmtLimit,
                                                                      adjustMaxAmtLimit,fifo,receivable,categoryCode,unitcode);

                                       records.add(invItem);

                                       }

                      }catch(Exception er){
                              System.out.println("Error RETRIEVING All InventoryItem... ->"+er.getMessage());
                      }finally{
                              closeConnection(con,ps,rs);
                      }

                      return records ;
    }
                public InventoryItem findInventoryItemById(String Id)
				{


                        String SELECT_QUERY = "SELECT * FROM ST_INVENTORY_ITEMS WHERE ITEM_CODE = ? ";

                        Connection con = null;
                        //Statement stmt = null;
                        PreparedStatement ps = null;
                        ResultSet rs = null;

                        InventoryItem  inventoryitem = null;
                        
                        try{
                                con = getConnection();
                               
                                 ps = con.prepareStatement(SELECT_QUERY);
                                 ps.setString(1,Id);
                                 rs  = ps.executeQuery();

                                 while(rs.next())
                                   {
                                         
                                          String mtid = rs.getString("MtId");
                                          String itemno = Id;//rs.getString("Item_Code");
                                         String status = rs.getString("Status");
                                         String type = rs.getString("ITEMTYPE_CODE");
                                         String description = rs.getString("Description");
                                         String taxcode = rs.getString("Tax_Code");
                                         int minqty = rs.getInt("Min_Quantity");
                                         double weightavgcost = Double.parseDouble(rs.getString("Weight_Avg_Cost"));
                                         double stdcost = Double.parseDouble(rs.getString("Standard_Cost"));
										// double FIFO = Double.parseDouble(rs.getString("FIFO"));
                                         double weight = Double.parseDouble(rs.getString("Weight"));
                                         String backorderable = rs.getString("BackOrderable");
                                        String userid = rs.getString("USERID");
                                        int reorderLevel = rs.getInt("reorder_Level");
                                        String salesAcct = rs.getString("sales_acct");
                                        String COGSoldAcct = rs.getString("COG_Sold");
                                        String inventoryAcct = rs.getString("inventory_Acct");
                                        String reqApproval = rs.getString("Req_Approval");
                                        int maxApproveLevel = rs.getInt("max_Approve_Level");
                                        double minAmtLimit = rs.getInt("min_amt_limit");
                                        double maxAmtLimit = rs.getInt("max_amt_limit");
                                        String adjustAcct = rs.getString("adjust_Acct");
                                        int adjustMaxApproveLevel = rs.getInt("adj_Max_Approve_Level");
                                        double adjustMinAmtLimit = rs.getDouble("adj_min_Amt_Limit");
                                        double adjustMaxAmtLimit = rs.getDouble("adj_max_amt_limit");
										double fifo = rs.getDouble("FIFO");
										String receivable = rs.getString("RECEIVABLE");
										String categoryCode = rs.getString("CATEGORY_CODE");
										String unitcode = rs.getString("MEASURING_CODE");

										inventoryitem = new InventoryItem(mtid, itemno, status, type, description, taxcode, minqty, 
                                                                       weightavgcost,stdcost, weight, backorderable,userid,reorderLevel,
                                                                       salesAcct,COGSoldAcct,inventoryAcct,reqApproval,maxApproveLevel,
                                                                       minAmtLimit,maxAmtLimit,adjustAcct,adjustMaxApproveLevel,
                                                                       adjustMinAmtLimit,adjustMaxAmtLimit,fifo,receivable,categoryCode,unitcode);
                                         }

                        }catch(Exception er){
                                System.out.println("Error RETRIEVING InventoryItem By Id... ->"+er.getMessage());
                        }finally{
                                closeConnection(con,ps,rs);
                        }
            return  inventoryitem;
        }
        public boolean isExistingInventoryItemId(String Id)
        {//passed

        boolean exists = false;
                        String SELECT_QUERY = "SELECT count(*) FROM  ST_INVENTORY_ITEMS "+
                                                                 " WHERE  ITEM_CODE = ? ";
                        Connection con = null;
                        PreparedStatement ps = null;
                        ResultSet rs = null;

                        try{

                                con = getConnection();
                                
                                ps = con.prepareStatement(SELECT_QUERY);

                                ps.setString(1, Id);
                                rs = ps.executeQuery();

                                while(rs.next()){
                                        int counted = rs.getInt(1);
                                        if(counted > 0){
                                        exists = true;
                                 }
                                }

                        }catch(Exception er){
                                System.out.println("Error in isST_INVENTORY_ITEMSExisting()... ->"+er);
                        }finally{
                                closeConnection(con,ps);
                        }

                        return exists;

        }
    public boolean createItemApprovalDetail
    (String itemCode,int organPosition,int concurrence,int userId,double minAmtLimit,
    		double maxAmtLimit,String companyCode) {

            String query = "INSERT INTO ST_ITEM_APPROVAL_DETAIL("
                            + "MTID,ITEM_CODE,APPROVE_LEVEL,CONCURRENCE,USERID,TRANS_DATE,min_amt_limit" +
                            		",max_amt_limit,COMP_CODE) VALUES (?,?,?,?,?,?,?,?,?)";

            Connection con = null;
            PreparedStatement ps = null;
            boolean done = false;
            id = helper.getGeneratedId("ST_ITEM_APPROVAL_DETAIL");

            try {
                    con = getConnection();
                    ps = con.prepareStatement(query);
                    ps.setString(1,id);
                    ps.setString(2,itemCode);
                    ps.setInt(3,organPosition);
                    ps.setInt(4,concurrence);
                    ps.setInt(5,userId);
                    ps.setDate(6, dateConvert(new java.util.Date()));
                    ps.setDouble(7,minAmtLimit);
                    ps.setDouble(8,maxAmtLimit);
					//ccode
					ps.setString(9,companyCode);
                    done = (ps.executeUpdate() != -1);
                    
            } catch (Exception ex) {
                    done = false;
                    System.out.println("ERROR Creating Item Approval Detail "
                                    + ex.getMessage());
            } finally {
                    closeConnection(con, ps);
            }
            return done;
    }
    public boolean updateItemApprovalDetail(String itemCode,int organPosition,int concurrence,String mtId,double minAmtLimit,double maxAmtLimit){
            String query = "UPDATE ST_ITEM_APPROVAL_DETAIL SET APPROVE_LEVEL=?, " +
                           "CONCURRENCE=?,MIN_AMT_LIMIT=?,MAX_AMT_LIMIT=? WHERE ITEM_CODE=? AND MTID=?";

            Connection con = null;
            PreparedStatement ps = null;
            boolean done = false;

            try {
                    con = getConnection();
                    ps = con.prepareStatement(query);
                    ps.setInt(1,organPosition);
                    ps.setInt(2,concurrence);
                    ps.setDouble(3,minAmtLimit);
                    ps.setDouble(4,maxAmtLimit);
                    ps.setString(5,itemCode);
                    ps.setString(6,mtId);
                    done = (ps.executeUpdate() != -1);
            } catch (Exception ex) {
                    System.out.println("ERROR Updating Item Approval Detail "
                                    + ex.getMessage());
                    ex.printStackTrace();
            } finally {
                    closeConnection(con, ps);
            }
            return done;
    }   
    public ArrayList findAllItemApprovalDetail(String itemCode){

                      String SELECT_QUERY = "SELECT * FROM ST_ITEM_APPROVAL_DETAIL WHERE ITEM_CODE='"+itemCode+"' ORDER BY APPROVE_LEVEL DESC";
 //                     System.out.println("<<<<<<findAllItemApprovalDetail SELECT_QUERY: "+SELECT_QUERY);
                      Connection con = null;
                      //Statement stmt = null;
                      PreparedStatement ps = null;
                      ResultSet rs = null;

                              ArrayList records = new java.util.ArrayList();
                        try{
                                con = getConnection();
                              
                               ps = con.prepareStatement(SELECT_QUERY);

                               rs  = ps.executeQuery();

                               while(rs.next())
                                 {
                                       String mtId = rs.getString("MtId");
                                       //String itemCode = rs.getString("Item_Code");
                                       int organPosition = rs.getInt("APPROVE_LEVEL");
                                       int concurrence = rs.getInt("CONCURRENCE");
                                       int userId = rs.getInt("USERID");
                                       String transDate = formatDate(rs.getDate("TRANS_DATE"));
                                       double minAmtLimit = rs.getDouble("min_amt_limit");
                                       double maxAmtLimit = rs.getDouble("max_amt_limit");
                                       ItemApprovalDetail itemApprove = new ItemApprovalDetail(mtId,itemCode,organPosition,userId,transDate,concurrence,minAmtLimit,maxAmtLimit);

                                       records.add(itemApprove);

                                       }

                      }catch(Exception er){
                              System.out.println("Error RETRIEVING All ST_ITEM_APPROVAL_DETAIL... ->"+er.getMessage());
                      }finally{
                              closeConnection(con,ps,rs);
                      }

                      return records ;
              }
              
    public ItemApprovalDetail findItemApprovalDetailById(String ID){

                      String SELECT_QUERY = "SELECT * FROM ST_ITEM_APPROVAL_DETAIL WHERE ITEM_CODE='"+ID+"'";
//                      System.out.println("<<<<findItemApprovalDetailById SELECT_QUERY: "+SELECT_QUERY);
                      Connection con = null;  
                      //Statement stmt = null; 
                      PreparedStatement ps = null;
                      ResultSet rs = null;

                        ItemApprovalDetail itemApprove = null;
                        try{
                                con = getConnection();
                              
                               ps = con.prepareStatement(SELECT_QUERY);

                               rs  = ps.executeQuery();

                               while(rs.next())
                                 {
                                       String mtId = ID;//rs.getString("MtId");
                                       String itemCode = rs.getString("Item_Code");
                                       int organPosition = rs.getInt("APPROVE_LEVEL");
                                       int concurrence = rs.getInt("CONCURRENCE");
                                       int userId = rs.getInt("USERID");
                                       String transDate = formatDate(rs.getDate("TRANS_DATE"));
                                       int minAmtLimit = rs.getInt("min_amt_limit");
                                       int maxAmtLimit = rs.getInt("max_amt_limit");
                                       itemApprove = new ItemApprovalDetail(mtId,itemCode,organPosition,userId,transDate,concurrence,minAmtLimit,maxAmtLimit);

                                       }

                      }catch(Exception er){
                              System.out.println("Error RETRIEVING ST_ITEM_APPROVAL_DETAIL By ID... ->"+er.getMessage());
                      }finally{
                              closeConnection(con,ps,rs);
                      }

                      return itemApprove ;
              }      
        public void createInventoryTotals(String mtId, String itemCode, int itemBalance, String desc,String wareHouseCode,  int userId)
         {

                        String CREATE_QUERY = "INSERT INTO ST_INVENTORY_TOTALS (MTID, ITEMCODE, ITEM_BALANCE,  DESCRIPTION,  WAREHOUSECODE,  USERID ) "+
                                              "VALUES (?,?,?,?,?,?)";
                        Connection con = null;
                        PreparedStatement ps = null;
                        String id = helper.getGeneratedId("ST_INVENTORY_TOTALS");

                        try{
                                con = getConnection();
                                ps = con.prepareStatement(CREATE_QUERY);
                                ps.setString(1,mtId);
                                ps.setString(2,itemCode);
                                ps.setInt(3,itemBalance);
                                ps.setString(4,desc);
                                ps.setString(5,wareHouseCode);
                                ps.setInt(6,userId);
                                
                                ps.execute();

                        }catch(Exception er){
                                System.out.println("Error CREATING InventoryHistory... ->"+er.getMessage());
                        }finally{
                                closeConnection(con,ps);
                        }

                }

        /**
         * Deletes a Mandatory Field
         * deleteMandatoryField
         *
         * @param formId String
        */


                public void deleteInventoryTotals(String id){
                        String DELETE_QUERY = "DELETE FROM ST_INVENTORY_TOTALS  WHERE MtId = ?";
                    Connection con = null;
                        PreparedStatement ps = null;
                        try{
                                con = getConnection();
                                //con = getConnection2();
                                //if(con == null)
                                // {  con = getConnection();    }
                                ps = con.prepareStatement(DELETE_QUERY);
                                ps.setString(1, id);

                                ps.execute();


                        }catch(Exception er){
                                System.out.println("Error DELETING InventoryHistory... ->"+er.getMessage());
                        }finally{
                                closeConnection(con,ps);
                        }

                }
                
        public void updateInventoryTotals(String mtId, String itemCode, int itemBalance, String desc, String
                                          wareHouseCode,  int userId)
         {

                        String UPDATE_QUERY = "UPDATE ST_INVENTORY_TOTALS  "+
                                              " SET  ITEM_BALANCE = ITEM_BALANCE + ?, WAREHOUSECODE = ? WHERE  ITEMCODE = ? ";
                        Connection con = null;
                        PreparedStatement ps = null;


                        try{
                                con = getConnection();
                                ps = con.prepareStatement(UPDATE_QUERY);
                                ps.setInt(1,itemBalance);
                                ps.setString(2,wareHouseCode);
                                ps.setString(3, itemCode);
                                
                                ps.execute();

                        }catch(Exception er){
                                System.out.println("Error UPDATING InventoryTotals.. ->"+er.getMessage());
                        }finally{
                                closeConnection(con,ps);
                        }

                }

         public ArrayList findAllInventoryTotalByQuery(String filter2,String filter){

                        String SELECT_QUERY = "SELECT DISTINCT b.Category_Code,a.item_code,a.balance,a.TMP_BALANCE,a.warehouse_code,a.userid,a.balance_ltd," +
                        		"a.balance_ytd ,b.comp_code FROM ST_INVENTORY_TOTALS a,ST_INVENTORY_ITEMS b " +
                        		"WHERE b.comp_code='"+filter2+"' and a.item_code=b.item_code" +filter;
                        
                        Connection con = null;
                        //Statement stmt = null;
                        PreparedStatement ps = null;
                        ResultSet rs = null;

                        java.util.ArrayList records = new java.util.ArrayList();
                        InventoryTotal inventoryTotals = null;
                        try{
                              con = getConnection();
                              ps = con.prepareStatement(SELECT_QUERY);
                              rs  = ps.executeQuery();

                                 while(rs.next())
                                   {
                                        // String mtId = rs.getString("MTID");
                                         String categoryCode = rs.getString("CATEGORY_CODE");
                                         String itemCode = rs.getString("ITEM_CODE");
                                          int  itemBalance = rs.getInt("BALANCE");
                                          int tmpBalance = rs.getInt("TMP_BALANCE");
                                         String description = "";
                                         String warehouseCode = rs.getString("WAREHOUSE_CODE");
                                        int userId = rs.getInt("USERID");
                                       int itemBalanceLtd = rs.getInt("BALANCE_LTD");
                                       int itemBalanceYtd = rs.getInt("BALANCE_YTD");
                                                                         
                                         inventoryTotals = new  InventoryTotal("",itemCode,itemBalance,description,warehouseCode,userId,itemBalanceLtd,itemBalanceYtd);
                                         inventoryTotals.setCategoryCode(categoryCode);
                                         inventoryTotals.setItemtmpBalance(tmpBalance);
                                         records.add(inventoryTotals);
                                         
                                }
                                         
                        }catch(Exception er){
                                System.out.println("Error RETRIEVING All findAllInventoryTotalByQuery ... ->"+er.getMessage());
                        }finally{
                                closeConnection(con,ps,rs);
                        }


                        return records ;
                }
                
                public InventoryTotals  findInventoryTotalsById(String itemCode){


                                String SELECT_QUERY = "SELECT * FROM ST_INVENTORY_TOTALS WHERE ITEMCODE  =  ?";

                        Connection con = null;
                        //Statement stmt = null;
                        PreparedStatement ps = null;
                        ResultSet rs = null;

                         InventoryTotals inventoryTotals = null;
                         
                        try{
                              con = getConnection();
                              ps = con.prepareStatement(SELECT_QUERY);
                              ps.setString(1,itemCode);
                              rs  = ps.executeQuery();

                              while(rs.next())
                                   {
                                     String mtId = rs.getString("MTID");
                                    //     String itemCode = rs.getString("ITEMCODE");
                                          int  itemBalance = rs.getInt("ITEM_BALANCE");
                                         String desc = rs.getString("DESCRIPTION");
                                         String wareHouseCode = rs.getString("WAREHOUSECODE");
                                        int userId = rs.getInt("USERID");
                                                                         
                                         inventoryTotals = new  InventoryTotals(mtId, itemCode, itemBalance, desc,  wareHouseCode, userId);
                                         
                                         }
                                         
                        }catch(Exception er){
                                System.out.println("Error RETRIEVING InventoryTotals By Id... ->"+er.getMessage());
                        }finally{
                                closeConnection(con,ps,rs);
                        }
            return  inventoryTotals ;
        }
        
        public ArrayList findAllInventoryHistoryByQuery(String filter2,String filter){
/*
            String SELECT_QUERY = "SELECT DISTINCT A.MTID,A.ITEM_CODE,A.TRANS_DESC," +
            		"A.QUANTITY,A.WAREHOUSE_CODE,A.TRANS_DATE,A.BATCH_CODE,A.USERID," +
            		"B.COMP_CODE FROM ST_INVENTORY_HISTORY A,ST_INVENTORY_ITEMS B " +
            		"WHERE B.COMP_CODE='"+filter2+"' AND A.ITEM_CODE = B.ITEM_CODE "+filter;
            */
            String SELECT_QUERY = "SELECT DISTINCT A.MTID,A.ITEM_CODE,A.TRANS_DESC," +
            		"A.QUANTITY,A.WAREHOUSE_CODE,A.TRANS_DATE,A.BATCH_CODE,A.USERID," +
            		"B.COMP_CODE FROM ST_INVENTORY_HISTORY A,ST_INVENTORY_ITEMS B " +
            		"WHERE B.COMP_CODE='"+filter2+"' AND A.ITEM_CODE = B.ITEM_CODE "+filter;            
System.out.println("SELECT_QUERY >>>>>>>>>> " + SELECT_QUERY);
                        Connection con = null;
                        //Statement stmt = null;
                        PreparedStatement ps = null;
                        ResultSet rs = null;

                        java.util.ArrayList records = new java.util.ArrayList();
                        InventoryHistory inventoryHistory = null;

                        try{
                              con = getConnection();
                                ps = con.prepareStatement(SELECT_QUERY);
                                //ps.setString(1,itemCode);
                                 rs  = ps.executeQuery();

                                 while(rs.next())
                                   {
                                      String mtId = rs.getString("MTID");
                                        String itemCode = rs.getString("ITEM_CODE");
                                         String desc= "";//rs.getString("DESCRIPTION");
                                        String transDesc = rs.getString("TRANS_DESC");
                                         int quantity = rs.getInt("QUANTITY");
                                         String warehouseCode = rs.getString("WAREHOUSE_CODE");
                                         String transDate = formatDate(rs.getDate("TRANS_DATE"));
                                         String batchCode = rs.getString("BATCH_CODE");
                                        int userId = rs.getInt("USERID");
                                       // if(transDesc.equalsIgnoreCase("Stock Issued")){quantity = quantity * -1;}
                                         inventoryHistory = new InventoryHistory(mtId,itemCode,desc,transDesc,quantity,warehouseCode,transDate,userId,batchCode);
                                         
                                         records.add(inventoryHistory);
                                         
                                         }
                                         
                        }catch(Exception er){
                                System.out.println("Error RETRIEVING All InventoryHistoryByQuery ... ->"+er.getMessage());
                        }finally{
                                closeConnection(con,ps,rs);
                        }


                        return records ;
                }
                
    public String getCodeName(String query)
    {
     String result = "";
     Connection con = null;
     ResultSet rs = null; 
     PreparedStatement ps = null;
     
     try
     {
      con = getConnection();
      ps = con.prepareStatement(query);
      rs = ps.executeQuery();
      while(rs.next())
      {
       result = rs.getString(1) == null ? "" : rs.getString(1);
      }
     }
     catch(Exception er)
     {
        System.out.println("Error in getCodeName()... ->"+er);
        er.printStackTrace();
     }finally{
        closeConnection(con,ps);
      }   
      return result;
     }
    public boolean isRecordExisting(String query){

             boolean exists = false;
             String updateQuery = query;
             Connection con = null;
             PreparedStatement ps = null;
             ResultSet rs = null;

             try{

                     con = getConnection();
                     ps = con.prepareStatement(updateQuery);

                    // ps.setString(1,batchCode);
                     rs = ps.executeQuery();

                     while(rs.next()){
                             int counted = rs.getInt(1);
                             if(counted > 0){
                             exists = true;
                      }
                     }

             }catch(Exception er){
                     System.out.println("Error in isRecordExisting()... ->"+er);
                     er.printStackTrace();
             }finally{
                     closeConnection(con,ps);
             }

             return exists;
     }
     
     //method to transfer from/to warehouse
      public boolean processWarehouseTransfer(String itemCode,String transDesc,String quantity,String fromWarehouseCode,String toWarehouseCode,int userId,String companyCode,String availableQuantFrom_,String availableQuantTo_){
      
          String createQuery = "INSERT INTO ST_INVENTORY_HISTORY (MTID,ITEM_CODE,TRANS_DESC,QUANTITY,WAREHOUSE_CODE,TRANS_DATE,BATCH_CODE,USERID,COMP_CODE,PREVIOUS_BALANCE) " +
                                   "VALUES (?,?,?,?,?,?,?,?,?,?)";
          String updateQuery = "UPDATE ST_INVENTORY_TOTALS SET BALANCE = BALANCE + ?, TMP_BALANCE = TMP_BALANCE + ? WHERE ITEM_CODE = ? AND WAREHOUSE_CODE = ?";
          String insertQuery = "INSERT INTO ST_INVENTORY_TOTALS (BALANCE,USERID,ITEM_CODE,WAREHOUSE_CODE,COMP_CODE) VALUES (?,?,?,?,?)";
          
          //Connection con = null;
          boolean autoCommit = false;
          Connection con = null;
          PreparedStatement ps = null;         
          id = helper.getGeneratedId("ST_INVENTORY_HISTORY");
          boolean done = false;
          boolean isItemExist = false;
          int j = 0;
          int jj = 0;
          int k = 0;
          int kk = 0;
          try{
                          
             isItemExist = sos.isItemExistInInventoryTotals(itemCode,toWarehouseCode);
             con = getConnection();
             autoCommit = con.getAutoCommit();
             con.setAutoCommit(false);
             //insert history //from warehouse
             ps = con.prepareStatement(createQuery);
             ps.setString(1,id);
             ps.setString(2,itemCode);
             ps.setString(3,transDesc);
             ps.setInt(4,Integer.parseInt("-"+quantity));
             ps.setString(5,fromWarehouseCode);
             ps.setDate(6,dateConvert(new java.util.Date()));
             ps.setString(7,id);
             ps.setInt(8,userId);
			 ps.setString(9,companyCode);
			 ps.setString(10,availableQuantFrom_);
             j = ps.executeUpdate();
                 
             //insert history //to warehouse
             ps = con.prepareStatement(createQuery);
             ps.setString(1,id);
             ps.setString(2,itemCode);
             ps.setString(3,transDesc);
             ps.setInt(4,Integer.parseInt(quantity));
             ps.setString(5,toWarehouseCode);
             ps.setDate(6,dateConvert(new java.util.Date()));
             ps.setString(7,id);
             ps.setInt(8,userId);
			 ps.setString(9,companyCode);
			 ps.setString(10,availableQuantTo_);
             jj = ps.executeUpdate();
             
            //update From Warehouse Inventory Totals         
             ps = con.prepareStatement(updateQuery);
             ps.setInt(1,Integer.parseInt("-"+quantity));
             ps.setInt(2,Integer.parseInt("-"+quantity));
             ps.setString(3,itemCode);
             ps.setString(4,fromWarehouseCode); 
             k = ps.executeUpdate();
            
             //update to Warehouse Inventory Totals
             if(isItemExist){ 
              ps = con.prepareStatement(updateQuery);     
              ps.setInt(1,Integer.parseInt(quantity));
              ps.setInt(2,Integer.parseInt(quantity));
              ps.setString(3,itemCode);
              ps.setString(4,toWarehouseCode); 
              kk = ps.executeUpdate();
              }
              else{
               ps = con.prepareStatement(insertQuery);
               ps.setInt(1,Integer.parseInt(quantity));
               ps.setInt(2,userId);
               ps.setString(3,itemCode);
               ps.setString(4,toWarehouseCode);
			   ps.setString(5,companyCode);
               kk = ps.executeUpdate();
              }           
              
              if((j != -1) &&(jj != -1) && (k != -1) && (kk != -1))
               {
               con.commit();
               con.setAutoCommit(autoCommit);
               done = true;
              }              
             }catch(Exception er){
                      System.out.println("Error Posting Warehouse Transfer 2 Inventory History/Totals ->"+er.getMessage());
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
      
    public boolean createWarehouseTransfer(String itemCode,int quantity,String fromWarehouse,
                                           String toWarehouse,int userId,String fromBranch,String toBranch,String companyCode) {

            String query = "INSERT INTO ST_WAREHOUSE_TRANSFER("
                            + "MTID,ITEM_CODE,TRANS_DATE,FROM_WAREHOUSE,TO_WAREHOUSE,"
                            + "QUANTITY,USERID,FROM_BRANCH,TO_BRANCH,COMP_CODE) VALUES (?,?,?,?,?,?,?,?,?,?)";

            Connection con = null;
            PreparedStatement ps = null;
            boolean done = false;
            id = helper.getGeneratedId("ST_WAREHOUSE_TRANSFER");
            auotoGenCode = id;
            try {
                    con = getConnection();
                    ps = con.prepareStatement(query);
                    ps.setString(1,id);
                    ps.setString(2,itemCode);
                    ps.setDate(3, dateConvert(new java.util.Date()));
                    ps.setString(4,fromWarehouse);
                    ps.setString(5,toWarehouse);
                    ps.setInt(6, quantity);
                    ps.setInt(7,userId);
                    ps.setString(8,fromBranch);
                    ps.setString(9,toBranch);
					ps.setString(10,companyCode);
                    done = (ps.executeUpdate() != -1);
            } catch (Exception ex) {
                    done = false;
                    System.out.println("ERROR Creating Warehouse Transfer "
                                    + ex.getMessage());
            } finally {
                    closeConnection(con, ps);
            }
            return done;
    }
	
	public ArrayList findAllBranchWarehouseCode(String filter){ 

                   String SELECT_QUERY = "select distinct(b.warehouse_code),b.name from ST_INVENTORY_TOTALS a,ST_WAREHOUSE b where a.warehouse_code=b.warehouse_code and a.balance > 0 ";
                   SELECT_QUERY = SELECT_QUERY + filter; 
                   Connection con = null;
                   System.out.print("<<<<findAllBranchWarehouseCode SELECT_QUERY: "+SELECT_QUERY);;
                   //Statement stmt = null;
                   PreparedStatement ps = null;
                   ResultSet rs = null;

                   ArrayList records = new ArrayList();
		           WareHouse WareHouses = null;
                   try{
                         con = getConnection();
                         ps = con.prepareStatement(SELECT_QUERY);
                         rs  = ps.executeQuery();

                            while(rs.next())
                              {    
							         String warehouse_code = rs.getString("warehouse_code");
                                     String warehouse_name = rs.getString("name");
                                     WareHouses = new WareHouse("",warehouse_code,warehouse_name,"","","","");
				                     records.add(WareHouses);                                 
                           }                                   
                   }catch(Exception er){
                           System.out.println("Error RETRIEVING Item By warehouse Code ... ->"+er.getMessage());
                   }finally{
                           closeConnection(con,ps,rs);
                   }


                   return records ;
           }
	
	
    public ArrayList findAllInventoryTotalItemByQuery(String filter)
    {

                   String SELECT_QUERY = "SELECT A.ITEM_CODE,B.DESCRIPTION FROM ST_INVENTORY_TOTALS A," +
                   		"ST_INVENTORY_ITEMS B WHERE A.ITEM_CODE=B.ITEM_CODE ";
                   SELECT_QUERY = SELECT_QUERY + filter; 
                   Connection con = null;
                   //Statement stmt = null;
                   PreparedStatement ps = null;
                   ResultSet rs = null;

                   ArrayList records = new ArrayList();
                   InventoryTotal inventoryTotals = null;
                   try{
                         con = getConnection();
                         ps = con.prepareStatement(SELECT_QUERY);
                         rs  = ps.executeQuery();
 
                            while(rs.next())
                              {
                                   // String mtId = rs.getString("MTID");
                                     String itemCode = rs.getString("ITEM_CODE");
                                     String desc = rs.getString("DESCRIPTION");
                                     int itemBalanceLtd = 0;
                                     int itemBalanceYtd = 0;
                                     inventoryTotals = new InventoryTotal("",itemCode,0,desc,"",0,itemBalanceYtd,itemBalanceLtd);
                                    
                                    records.add(inventoryTotals);
                                    
                           }
                                    
                   }catch(Exception er){
                           System.out.println("Error RETRIEVING Item By warehouse ... ->"+er.getMessage());
                   }finally{
                           closeConnection(con,ps,rs);
                   }


                   return records ;
           }
    
    public ArrayList findInventoryItemByWarehouse(String filter)
    {

                   String SELECT_QUERY = "SELECT distinct (A.ITEM_CODE),B.DESCRIPTION FROM ST_INVENTORY_TOTALS A," +
                   		"ST_INVENTORY_ITEMS B WHERE A.ITEM_CODE=B.ITEM_CODE ";
                   SELECT_QUERY = SELECT_QUERY + filter; 
                   Connection con = null;
                   //Statement stmt = null;
                   PreparedStatement ps = null;
                   ResultSet rs = null;

                   ArrayList records = new ArrayList();
                   InventoryTotal inventoryTotals = null;
                   try{
                         con = getConnection();
                         ps = con.prepareStatement(SELECT_QUERY);
                         rs  = ps.executeQuery();
 
                            while(rs.next())
                              {
                                   // String mtId = rs.getString("MTID");
                                     String itemCode = rs.getString("ITEM_CODE");
                                     String desc = rs.getString("DESCRIPTION");
                                     int itemBalanceLtd = 0;
                                     int itemBalanceYtd = 0;
                                     
                                     inventoryTotals = new InventoryTotal("",itemCode,0,desc,"",0,itemBalanceYtd,itemBalanceLtd);
                                    
                                    records.add(inventoryTotals);
                                    
                           }
                                    
                   }catch(Exception er){
                           System.out.println("Error RETRIEVING Item By warehouse ... ->"+er.getMessage());
                   }finally{
                           closeConnection(con,ps,rs);
                   }


                   return records ;
           }
    
    
    public ArrayList findWarehouseByInventoryItem(String filter)
    {

                   String SELECT_QUERY = "select distinct(a.warehouse_code), b.name,a.BALANCE,a.BALANCE_YTD,a.BALANCE_LTD from ST_INVENTORY_TOTALS A,ST_WAREHOUSE B "+ 
                	   						" where a.warehouse_code=b.warehouse_code ";
                   SELECT_QUERY = SELECT_QUERY + filter; 
  //                 System.out.println("SELECT_QUERY >>>>>>>>> " + SELECT_QUERY);
                   Connection con = null;
                   //Statement stmt = null;
                   PreparedStatement ps = null;
                   ResultSet rs = null;

                   ArrayList records = new ArrayList();
                   InventoryTotal inventoryTotals = null;
                   try{
                         con = getConnection();
                         ps = con.prepareStatement(SELECT_QUERY);
                         rs  = ps.executeQuery();
 
                            while(rs.next())
                              {
                                   // String mtId = rs.getString("MTID");
                                     String itemCode = rs.getString("warehouse_code");
                                     String desc = rs.getString("name");
                                     int itemBalance = rs.getInt("BALANCE");
                                     int itemBalanceLtd = rs.getInt("BALANCE_LTD");
                                     int itemBalanceYtd = rs.getInt("BALANCE_YTD");
                                     inventoryTotals = new InventoryTotal("",itemCode,itemBalance,desc,"",0,itemBalanceYtd,itemBalanceLtd);
                                    
                                    records.add(inventoryTotals);
                                    
                           }
                                    
                   }catch(Exception er){
                           System.out.println("Error RETRIEVING Item By warehouse ... ->"+er.getMessage());
                   }finally{
                           closeConnection(con,ps,rs);
                   }


                   return records ;
           }
    
    public WarehouseTransfer findWarehouseTransferById(String id){
           String SELECT_QUERY = "SELECT * FROM ST_WAREHOUSE_TRANSFER WHERE MTID  =  ?";

            Connection con = null;
            //Statement stmt = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            WarehouseTransfer whtransfer = null;
             
            try{
                  con = getConnection();
                  ps = con.prepareStatement(SELECT_QUERY);
                  ps.setString(1,id);
                  rs  = ps.executeQuery();

                  while(rs.next())
                  {
                   String mtId = rs.getString("MTID");
                   String itemCode = rs.getString("ITEM_CODE");
                   String fromWarehouse = rs.getString("FROM_WAREHOUSE");
                   String toWarehouse = rs.getString("TO_WAREHOUSE");
                   String transDate = formatDate(rs.getDate("TRANS_DATE"));
                   int quantity = rs.getInt("QUANTITY");
                   int userId = rs.getInt("USERID");
                      String fromBranch = rs.getString("FROM_BRANCH");
                      String toBranch = rs.getString("TO_BRANCH");
                      
                   whtransfer = new  WarehouseTransfer(mtId,itemCode,fromWarehouse,toWarehouse,
                                                      transDate,quantity,userId,fromBranch,toBranch);
                             
                  }
                             
            }catch(Exception er){
                    System.out.println("Error RETRIEVING Warehouse Transfer By Id... ->"+er.getMessage());
            }finally{
                    closeConnection(con,ps,rs);
            }
         return  whtransfer ;
    }
    
    public ArrayList findWarehouseTransferByQuery(String filter2,String filter){
	

	
	
          
            String SELECT_QUERY = "SELECT * FROM ST_WAREHOUSE_TRANSFER WHERE comp_code =" +
            		"'"+filter2+"' and MTID IS NOT NULL "+filter+" ORDER BY TRANS_DATE DESC";
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            java.util.ArrayList list = new java.util.ArrayList();
             WarehouseTransfer whtransfer = null;
         
        try{
              con = getConnection();
              ps = con.prepareStatement(SELECT_QUERY);
              rs  = ps.executeQuery();

              while(rs.next())
              {
               String mtId = rs.getString("MTID");
               String itemCode = rs.getString("ITEM_CODE");
               String fromWarehouse = rs.getString("FROM_WAREHOUSE");
               String toWarehouse = rs.getString("TO_WAREHOUSE");
               String transDate = formatDate(rs.getDate("TRANS_DATE"));
               int quantity = rs.getInt("QUANTITY");
               int userId = rs.getInt("USERID");
               String fromBranch = rs.getString("from_branch");
               String toBranch = rs.getString("to_branch");
			   String companyCode = rs.getString("comp_Code");
               whtransfer = new  WarehouseTransfer(mtId,itemCode,fromWarehouse,toWarehouse,transDate,quantity,userId,fromBranch,toBranch);
               list.add(whtransfer);          
              }
                         
        }catch(Exception er){
                System.out.println("Error RETRIEVING Warehouse Transfer By Query... ->"+er.getMessage());
        }finally{
                closeConnection(con,ps,rs);
        }

            return list ;
    }
    //method to transfer from/to warehouse
    public boolean processWarehouseTransfer(String itemCode,String transDesc,String quantity,String fromWarehouseCode,String toWarehouseCode,int userId){
    
        String createQuery = "INSERT INTO ST_INVENTORY_HISTORY (MTID,ITEM_CODE,TRANS_DESC,QUANTITY,WAREHOUSE_CODE,TRANS_DATE,BATCH_CODE,USERID) " +
                                 "VALUES (?,?,?,?,?,?,?,?)";
        String updateQuery = "UPDATE ST_INVENTORY_TOTALS SET BALANCE = BALANCE + ?, TMP_BALANCE = TMP_BALANCE + ? WHERE ITEM_CODE = ? AND WAREHOUSE_CODE = ?";
        String insertQuery = "INSERT INTO ST_INVENTORY_TOTALS (BALANCE,USERID,ITEM_CODE,WAREHOUSE_CODE) VALUES (?,?,?,?)";
        
        //Connection con = null;
        boolean autoCommit = false;
        Connection con = null;
        PreparedStatement ps = null;         
        id = helper.getGeneratedId("ST_INVENTORY_HISTORY");
        boolean done = false;
        boolean isItemExist = false;
        int j = 0;
        int jj = 0;
        int k = 0;
        int kk = 0;
        try{
                        
           isItemExist = sos.isItemExistInInventoryTotals(itemCode,toWarehouseCode);
           con = getConnection();
           autoCommit = con.getAutoCommit();
           con.setAutoCommit(false);
           //insert history //from warehouse
           ps = con.prepareStatement(createQuery);
           ps.setString(1,id);
           ps.setString(2,itemCode);
           ps.setString(3,transDesc);
           ps.setInt(4,Integer.parseInt("-"+quantity));
           ps.setString(5,fromWarehouseCode);
           ps.setDate(6,dateConvert(new java.util.Date()));
           ps.setString(7,id);
           ps.setInt(8,userId);
           j = ps.executeUpdate();
               
           //insert history //to warehouse
           ps = con.prepareStatement(createQuery);
           ps.setString(1,id);
           ps.setString(2,itemCode);
           ps.setString(3,transDesc);
           ps.setInt(4,Integer.parseInt(quantity));
           ps.setString(5,toWarehouseCode);
           ps.setDate(6,dateConvert(new java.util.Date()));
           ps.setString(7,id);
           ps.setInt(8,userId);
           jj = ps.executeUpdate();
           
          //update From Warehouse Inventory Totals   
           ps = con.prepareStatement(updateQuery);
           ps.setInt(1,Integer.parseInt("-"+quantity));
           ps.setInt(2,Integer.parseInt("-"+quantity));
           ps.setString(3,itemCode);
           ps.setString(4,fromWarehouseCode); 
           k = ps.executeUpdate();
 //          System.out.println("----------k-----------"+k);
           //update to Warehouse Inventory Totals
           if(isItemExist){ 
            ps = con.prepareStatement(updateQuery);     
            ps.setInt(1,Integer.parseInt(quantity));
            ps.setInt(2,Integer.parseInt(quantity));
            ps.setString(3,itemCode);
            ps.setString(4,toWarehouseCode); 
            kk = ps.executeUpdate();
//            System.out.println("-----------kk-----"+kk);
            }
            else{
             ps = con.prepareStatement(insertQuery);
             ps.setInt(1,Integer.parseInt(quantity));
             ps.setInt(2,userId);
             ps.setString(3,itemCode);
             ps.setString(4,toWarehouseCode);
             kk = ps.executeUpdate();
            }           
            
            if((j != -1) &&(jj != -1) && (k != -1) && (kk != -1))
             {
             con.commit();
             con.setAutoCommit(autoCommit);
             done = true;
            }              
           }catch(Exception er){
                    System.out.println("Error Posting Warehouse Transfer 2 Inventory History/Totals ->"+er.getMessage());
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
  /*      
    public ArrayList findPettyCashByQuery(String filter)
    {
        ArrayList iaList = new ArrayList();
        String criteria = (new StringBuilder(" WHERE MTID IS NOT NULL ")).append(filter).toString();
        iaList = findPettycash(criteria);
        return iaList;
    }   
    private ArrayList findPettycash(String filter)
    {
        ArrayList iaList;
        Connection con;
        PreparedStatement ps;
        String query;
        iaList = new ArrayList();
        SalesInvoice impacc = null;
        con = null;
        ps = null;
        ResultSet rs = null;
        System.out.println("findPettyCash filter : "+filter);
        query = "SELECT MTID,CUSTOMER_NAME,ORDER_NO,ITEM_DESCRIPTION,TOTAL_AMOUNT,TOTAL_QUANTITY,UNIT_PRICE,I" +
"NVOICE_NO,ACCOUNT_NO,TRANSACTION_DATE,BANK,CHEQUE_NO,EXPEND_HEAD,BRANCH_CODE,USERID,TERM FROM ST_PC_SUMMARY ";
        query = (new StringBuilder(String.valueOf(query))).append(filter).toString();       
        System.out.println(" findPettycash query:  "+query);
        try{
        con = getConnection();
        ps = con.prepareStatement(query);
        rs = ps.executeQuery();
  //      SalesInvoice impacc;
        while(rs.next())
        {
     //   for( rs = ps.executeQuery(); rs.next(); iaList.add(impacc))
     //   {
            String mtId = rs.getString("MTID");
            String customerName = rs.getString("CUSTOMER_NAME");
            String orderNo = rs.getString("ORDER_NO");
            String itemDescription = rs.getString("ITEM_DESCRIPTION");
            double amount = rs.getDouble("TOTAL_AMOUNT");
            int quantity = rs.getInt("TOTAL_QUANTITY");
            double unitPrice = rs.getDouble("UNIT_PRICE");
            String invoiceNo = rs.getString("INVOICE_NO");
            String accountNo = rs.getString("ACCOUNT_NO");
            String bank = rs.getString("BANK");
            String chequeNo = rs.getString("CHEQUE_NO");
            String expendhead = rs.getString("EXPEND_HEAD");
            String branchcode = rs.getString("BRANCH_CODE");
            int userid = rs.getInt("USERID");
            String term = rs.getString("TERM");
            String transactionDate = "";
            
            if(rs.getString("TRANSACTION_DATE") != null)
            {
                transactionDate = sdf.format(rs.getDate("TRANSACTION_DATE"));
            }
            impacc = new SalesInvoice(mtId, customerName, orderNo, itemDescription, amount, quantity, unitPrice, invoiceNo, accountNo, transactionDate,bank,chequeNo,expendhead,branchcode,userid,term);
            iaList.add(impacc);
        }
        }catch(Exception er){
		        System.out.println("ERROR fetching Petty Cash"+er.getMessage());
		}finally{
		        closeConnection(con,ps);
		}
		
		    return iaList ;
		}  
    public SalesInvoice findImprestByRefNo(String invoiceNo)
    {
        SalesInvoice imp = new SalesInvoice();
        String criteria = (new StringBuilder(" WHERE ORDER_NO = '")).append(invoiceNo).append("'").toString();
        imp = (SalesInvoice)findInvoice(criteria).get(0);
        return imp;
    }
    private ArrayList findInvoice(String filter)
    {
        ArrayList iaList;
        Connection con;
        PreparedStatement ps;
        String query;
        iaList = new ArrayList();
        SalesInvoice impacc = null;
        con = null;
        ps = null;
        ResultSet rs = null;
        System.out.println("findInvoice filter : "+filter);
        query = "SELECT MTID,CUSTOMER_NAME,ORDER_NO,ITEM_DESCRIPTION,TOTAL_AMOUNT,TOTAL_QUANTITY,UNIT_PRICE,I" +
"NVOICE_NO,ACCOUNT_NO,TRANSACTION_DATE,BANK,CHEQUE_NO,EXPEND_HEAD,BRANCH_CODE,USERID,TERM FROM ST_PV_SUMMARY ";
        query = (new StringBuilder(String.valueOf(query))).append(filter).toString();       
        System.out.println(" findInvoice query:  "+query);
        try{
        con = getConnection();
        ps = con.prepareStatement(query);
        rs = ps.executeQuery();
  //      SalesInvoice impacc;
        while(rs.next())
        {
     //   for( rs = ps.executeQuery(); rs.next(); iaList.add(impacc))
     //   {
            String mtId = rs.getString("MTID");
            String customerName = rs.getString("CUSTOMER_NAME");
            String orderNo = rs.getString("ORDER_NO");
            String itemDescription = rs.getString("ITEM_DESCRIPTION");
            double amount = rs.getDouble("TOTAL_AMOUNT");
            int quantity = rs.getInt("TOTAL_QUANTITY");
            double unitPrice = rs.getDouble("UNIT_PRICE");
            String invoiceNo = rs.getString("INVOICE_NO");
            String accountNo = rs.getString("ACCOUNT_NO");
            String bank = rs.getString("BANK");
            String chequeNo = rs.getString("CHEQUE_NO");
            String expendhead = rs.getString("EXPEND_HEAD"); 
            String branchcode = rs.getString("BRANCH_CODE");
            int userid = rs.getInt("USERID");
            String term = rs.getString("TERM");
            String transactionDate = "";    
            if(rs.getString("TRANSACTION_DATE") != null)
            {
                transactionDate = sdf.format(rs.getDate("TRANSACTION_DATE"));
            }
            impacc = new SalesInvoice(mtId, customerName, orderNo, itemDescription, amount, quantity, unitPrice, invoiceNo, accountNo, transactionDate,bank,chequeNo,expendhead,branchcode,userid,term);
            iaList.add(impacc);
        }
        }catch(Exception er){
		        System.out.println("ERROR fetching Invoice"+er.getMessage());
		}finally{
		        closeConnection(con,ps);
		}
		
		    return iaList ;
		}    
		*/  
    
    public ArrayList findInventoryAdjustment(String filter2,String filter){
	

	
	
        String query = "SELECT MTID, ITEM_CODE, TRANS_DATE,PERIOD, "
                + "WAREHOUSE_CODE, DESCRIPTION, QUANTITY,USERID,POSTED, ADJUST_OPTION "
                + " FROM ST_INVENTORY_ADJUSTMT WHERE comp_code =" +
            		"'"+filter2+"' and MTID IS NOT NULL "+filter+" ORDER BY TRANS_DATE DESC";
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            java.util.ArrayList list = new java.util.ArrayList();
            InventoryAdjustment adjust = null;
         
        try{
              con = getConnection();
              ps = con.prepareStatement(query);
              rs  = ps.executeQuery();

              while(rs.next())
              {
                  String id = rs.getString("MTID");
                  String itemNo = rs.getString("item_Code");
                  String date = formatDate(rs.getDate("trans_Date"));
                  String period = rs.getString("period");
                  String warehouse = rs.getString("wareHouse_Code");
                  String description = rs.getString("description");
                  String quantity = rs.getString("quantity");
                  String userId = rs.getString("userId");
                  String posted = rs.getString("POSTED");
                  String adjustoption = rs.getString("ADJUST_OPTION");
                  InventoryAdjustment ia = new InventoryAdjustment(id, itemNo,
                                  date, period, warehouse, description, Integer.parseInt(quantity),Integer.parseInt(userId),posted,adjustoption); 
                  list.add(adjust);  
              }
                         
        }catch(Exception er){
                System.out.println("Error RETRIEVING Inventory Adjustment By Query... ->"+er.getMessage());
        }finally{
                closeConnection(con,ps,rs);
        }

            return list ;
    }
    //method to transfer from/to warehouse   
    //method to transfer from/to warehouse
    public boolean processNewStock(String itemCode,String transDesc,String quantity,String fromWarehouseCode,String toWarehouseCode,int userId){
    
        String createQuery = "INSERT INTO ST_INVENTORY_HISTORY (MTID,ITEM_CODE,TRANS_DESC,QUANTITY,WAREHOUSE_CODE,TRANS_DATE,BATCH_CODE,USERID) " +
                                 "VALUES (?,?,?,?,?,?,?,?)";
        String updateQuery = "UPDATE ST_INVENTORY_TOTALS SET BALANCE = BALANCE + ?, TMP_BALANCE = TMP_BALANCE + ? WHERE ITEM_CODE = ? AND WAREHOUSE_CODE = ?";
        String insertQuery = "INSERT INTO ST_INVENTORY_TOTALS (BALANCE,USERID,ITEM_CODE,WAREHOUSE_CODE) VALUES (?,?,?,?)";
        
        //Connection con = null;
        boolean autoCommit = false;
        Connection con = null;
        PreparedStatement ps = null;         
        id = helper.getGeneratedId("ST_INVENTORY_HISTORY");
        boolean done = false;
        boolean isItemExist = false;
        int j = 0;
        int jj = 0;
        int k = 0;
        int kk = 0;
        try{
                        
           isItemExist = sos.isItemExistInInventoryTotals(itemCode,toWarehouseCode);
           System.out.println("<<<<<<<isItemExist: "+isItemExist);
           con = getConnection();
           autoCommit = con.getAutoCommit();
           con.setAutoCommit(false);
           //insert history //from warehouse
           System.out.println("<<<<<<<createQuery: "+createQuery);
           System.out.println("<<<<<<<itemCode: "+itemCode+"   transDesc: "+transDesc+"    quantity: "+Integer.parseInt("-"+quantity)+"   fromWarehouseCode: "+fromWarehouseCode+"    id: "+id);
           ps = con.prepareStatement(createQuery);
           ps.setString(1,id);
           ps.setString(2,itemCode);
           ps.setString(3,transDesc);
           ps.setInt(4,Integer.parseInt("-"+quantity));
           ps.setString(5,fromWarehouseCode);
           ps.setDate(6,dateConvert(new java.util.Date()));
           ps.setString(7,id);
           ps.setInt(8,userId);
           j = ps.executeUpdate();
               
           //insert history //to warehouse
           System.out.println("<<<<<<<createQuery: "+createQuery);
           ps = con.prepareStatement(createQuery);
           ps.setString(1,id);
           ps.setString(2,itemCode);
           ps.setString(3,transDesc);
           ps.setInt(4,Integer.parseInt(quantity));
           ps.setString(5,toWarehouseCode);
           ps.setDate(6,dateConvert(new java.util.Date()));
           ps.setString(7,id);
           ps.setInt(8,userId);
           jj = ps.executeUpdate();
           
          //update From Warehouse Inventory Totals   
           System.out.println("<<<<<<<updateQuery: "+updateQuery);
           ps = con.prepareStatement(updateQuery);
           ps.setInt(1,Integer.parseInt("-"+quantity));
           ps.setInt(2,Integer.parseInt("-"+quantity));
           ps.setString(3,itemCode);
           ps.setString(4,fromWarehouseCode); 
           k = ps.executeUpdate();
//           System.out.println("----------k-----------"+k);
           //update to Warehouse Inventory Totals
           if(isItemExist){ 
            ps = con.prepareStatement(updateQuery);     
            ps.setInt(1,Integer.parseInt(quantity));
            ps.setInt(2,Integer.parseInt(quantity));
            ps.setString(3,itemCode);
            ps.setString(4,toWarehouseCode); 
            kk = ps.executeUpdate();
//            System.out.println("-----------kk-----"+kk);
            }
            else{
            	System.out.println("<<<<<<<insertQuery: "+insertQuery);
             ps = con.prepareStatement(insertQuery);
             ps.setInt(1,Integer.parseInt(quantity));
             ps.setInt(2,userId);
             ps.setString(3,itemCode);
             ps.setString(4,toWarehouseCode);
             kk = ps.executeUpdate();
            }           
            
            if((j != -1) &&(jj != -1) && (k != -1) && (kk != -1))
             {
             con.commit();
             con.setAutoCommit(autoCommit);
             done = true;
            }              
           }catch(Exception er){
                    System.out.println("Error Posting Warehouse Transfer 2 Inventory History/Totals ->"+er.getMessage());
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
