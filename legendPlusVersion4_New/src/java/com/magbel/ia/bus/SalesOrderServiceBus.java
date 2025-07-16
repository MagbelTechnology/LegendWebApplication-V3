/**
 *
 */
package com.magbel.ia.bus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.magbel.ia.dao.PersistenceServiceDAO;
import com.magbel.ia.util.ApplicationHelper;

import com.magbel.ia.util.CodeGenerator;
import com.magbel.ia.vao.JournalVoucher;
import com.magbel.ia.vao.SalesOrder;
import com.magbel.ia.vao.Shipment;
//import com.magbel.ia.vao.GlAccountType;

import com.magbel.ia.vao.InventoryHistory;
import com.magbel.ia.vao.InventoryTotal;
import com.magbel.ia.vao.PurchaseOrderDeliveryItem;
import com.magbel.ia.vao.PurchaseOrderItem;

import com.magbel.ia.vao.SalesOrderDeliveryItem;
import com.magbel.ia.vao.SalesOrderItem;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 */
public class SalesOrderServiceBus extends PersistenceServiceDAO {
	SimpleDateFormat sdf;

	final String space = "  ";
	final String comma = ",";
        public String auotoGenCode = "";

	java.util.Date date;

	com.magbel.util.DatetimeFormat df;
	ApplicationHelper helper;
        CodeGenerator cg;
	/**
	 *
	 */
	public SalesOrderServiceBus() {
		// TODO Auto-generated constructor stub
		sdf = new SimpleDateFormat("dd-MM-yyyy");
		df = new com.magbel.util.DatetimeFormat();
		helper = new ApplicationHelper();
                cg = new CodeGenerator();
	}

       public boolean createSalesOrder(String orderCode,String customerNo, String po,String reqPersId, String shipDate, 
                        String freight,	String carrier, String description,int userId,int approveOfficer,
                        String projectCode,String companyCode) {

		String query = "INSERT INTO IA_SALES_ORDER (SORDER_CODE,DESCRIPTION,CUSTOMER_CODE,PORDER_CODE,REQ_PERS_IDENTITY,"+
                               "SHIP_DATE,FREIGHT_CODE,CARRIER_CODE,MTID,USERID,APPROVE_OFFICER,TRANS_DATE," +
                               "PROJECT_CODE,COMP_CODE) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	    
                String isAutoGen = getCodeName("SELECT auto_generate_ID FROM IA_GB_COMPANY");
		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String id = helper.getGeneratedId("IA_SALES_ORDER");
                
	        orderCode = isAutoGen.trim().equalsIgnoreCase("Y") ? cg.generateCode("SALES ORDER","","","") : orderCode;
                auotoGenCode = orderCode;
		try {

			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1,orderCode);
			ps.setString(2,description);
			ps.setString(3,customerNo);
			ps.setString(4,po);
			ps.setString(5,reqPersId);
			ps.setDate(6,dateConvert(shipDate));
			ps.setString(7,freight);
			ps.setString(8,carrier);
			ps.setString(9,id);
                        ps.setInt(10,userId);
                        ps.setInt(11,approveOfficer);
                        ps.setDate(12,dateConvert(new java.util.Date()));
                        ps.setString(13,projectCode);
						ps.setString(14,companyCode);

			done = (ps.executeUpdate() != -1);
		} catch (Exception ex) {
		        done = false;
			System.out.println("ERROR Creating Sales Order " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			closeConnection(con, ps);
		}
		return done;
	}
        
    //update temporary balance    
    public boolean updateInventoryTmpBalance(String warehouseCode,String itemCode,int tmpBalance) {

            String query = "UPDATE ST_INVENTORY_TOTALS SET TMP_BALANCE = TMP_BALANCE - ? "+ 
                           "WHERE ITEM_CODE = ? AND WAREHOUSE_CODE = ?";

            Connection con = null;
            PreparedStatement ps = null;
            boolean done = false;

            try {
                    con = getConnection();
                    ps = con.prepareStatement(query);
                    ps.setString(1,warehouseCode);
                    ps.setString(2,itemCode);
                    ps.setInt(3,tmpBalance);
                    
                    done = (ps.executeUpdate() != -1);
            } catch (Exception ex) {
                    System.out.println("ERROR Updating updateInventoryTmpBalance " + ex.getMessage());
                    ex.printStackTrace();
            } finally {
                    closeConnection(con, ps);
            }
            return done;
    }
	public boolean createShipment(String orderNo, String warehouse, String date,
								int period, int quantityShipped,int quantityOrdered,
								String itemNo,  double unitPrice) {

		String query = "INSERT INTO IA_SHIPMENTS("+
						"SORDER_CODE,WAREHOUSE_CODE,"+
						"SHIPMENT_DATE,QUANTITY_ORDER,QUANTITY_SHIP,"+
						"ITEMCODE,UNITPRICE,MTID) VALUES(?,?,?,?,?,?,?,?)";

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String id = helper.getGeneratedId("IA_SHIPMENTS");

		try {

			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, orderNo);
			ps.setString(2, warehouse);
			ps.setDate(3, df.dateConvert(date));
			ps.setInt(4, quantityOrdered);
			ps.setInt(5, quantityShipped);
			ps.setString(6, itemNo);
			ps.setDouble(7, unitPrice);
			ps.setInt(7, period);
			ps.setString(8, id);
			//ps.setString(8, freight);
			//ps.setString(9, carrier);
			//ps.setString(13, description);
			// ps.setDouble(13, amount_paid);

			done = (ps.executeUpdate() != -1);
		} catch (Exception ex) {
			System.out.println("ERROR Creating Shipment " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			closeConnection(con, ps);
		}
		return done;
	}

	public boolean updateSalesOrder(String po,String freight, String carrier,
        				String customerCode,String orderCode,String shipDate,String reqPersId,int approveOfficer,String projectCode) {

		String query = "UPDATE IA_SALES_ORDER SET CUSTOMER_CODE=?,PORDER_CODE=?,SHIP_DATE=?,FREIGHT_CODE = ?,CARRIER_CODE = ?, "+
                               "REQ_PERS_IDENTITY=?,APPROVE_OFFICER=?,PROJECT_CODE=? WHERE SORDER_CODE = ?";

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
/*		
		System.out.println("Customer Code >>>>>>>>>>> " + customerCode);
		System.out.println("po >>>>>>>>>>> " + po);
		System.out.println("shipDate >>>>>>>>>>> " + dateConvert(shipDate));
		System.out.println("freight >>>>>>>>>>> " + freight);
		System.out.println("carrier >>>>>>>>>>> " + carrier);
		System.out.println("reqPersId >>>>>>>>>>> " + reqPersId);
		System.out.println("approveOfficer >>>>>>>>>>> " + approveOfficer);
		System.out.println("projectCode >>>>>>>>>>> " + projectCode);
		System.out.println("orderCode >>>>>>>>>>> " + orderCode);
*/
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1,customerCode);
			ps.setString(2,po);
			ps.setDate(3,dateConvert(shipDate));
			ps.setString(4,freight);
			ps.setString(5,carrier);
                        ps.setString(6,reqPersId);
                        ps.setInt(7,approveOfficer);
                        ps.setString(8,projectCode);
			ps.setString(9,orderCode);

			done = (ps.executeUpdate() != -1);
		} catch (Exception ex) {
			System.out.println("ERROR Updating Sales Order " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			closeConnection(con, ps);
		}
		return done;
	}
        
    public boolean createSalesOrderItemDetail(String orderCode,int quantity, double unitPrice,double advancePymt,String itemCode,String warehouseCode,String companyCode){

       String CREATE_QUERY = "INSERT INTO IA_SO_ITEM "+
                      "(SORDER_CODE,QUANTITY,UNITPRICE,AMOUNT,MTID,ADVANCE_PYMT,ITEM_CODE,WAREHOUSE_CODE,COMP_CODE) "+
                      " VALUES (?,?,?,?,?,?,?,?,?)";
                       
            Connection con = null;
            PreparedStatement ps = null;
            String id = helper.getGeneratedId("IA_SO_ITEM");
            boolean done = false;
            try{
                    con = getConnection();
                    ps = con.prepareStatement(CREATE_QUERY);
                    ps.setString(1,orderCode);
                    ps.setInt(2,quantity);
                    ps.setDouble(3,unitPrice);
                    ps.setDouble(4,(unitPrice*quantity));
                    ps.setString(5,id);
                    ps.setDouble(6,advancePymt);
                    ps.setString(7,itemCode);
                    ps.setString(8,warehouseCode);
					ps.setString(9,companyCode);
                    
                done = (ps.executeUpdate() != -1);

            }catch(Exception er){
                    done = false;
                    System.out.println("Error creating SalesOrderItemDetail... ->"+er.getMessage());
            }finally{
                    closeConnection(con,ps);
            }
          return done;
       }
    public boolean updateSalesOrderItemDetail(String id,String orderCode,int quantity, double unitPrice,String warehouseCode,String itemCode){

            String UPDATE_QUERY =  "UPDATE IA_SO_ITEM SET QUANTITY=?,"+
                                   "UNITPRICE=?,AMOUNT=? WHERE ITEM_CODE=? AND SORDER_CODE=? AND WAREHOUSE_CODE=?";
            Connection con = null;
            PreparedStatement ps = null;
            boolean done = false;

            try{
                    con = getConnection();
                    ps = con.prepareStatement(UPDATE_QUERY);
                    ps.setInt(1,quantity);
                    ps.setDouble(2,unitPrice);
                    ps.setDouble(3,(quantity*unitPrice));
                    ps.setString(4,itemCode);
                    ps.setString(5,orderCode);
                    ps.setString(6,warehouseCode);
                   // ps.setString(6,id);
                                        
                done = (ps.executeUpdate() != -1);

            }catch(Exception er){
                    done = false;
                    System.out.println("Error UPDATING SalesOrderItemDetail... ->"+er.getMessage());
            }finally{
                    closeConnection(con,ps);
            }
    return done;
    }
    
    public SalesOrderItem findSalesOrderItemDetailById(String id,String orderCode){
           
        String FIND_QUERY = "SELECT MTID,SORDER_CODE,QUANTITY,AMOUNT,UNITPRICE,ADVANCE_PYMT,ITEM_CODE,QUANTITY_DELIVER,QUANTITY_REMAIN,WAREHOUSE_CODE "+
                            "FROM IA_SO_ITEM WHERE SORDER_CODE =? AND MTID=? ";

        Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            SalesOrderItem orderItem = null;

            try{
                    con = getConnection();
                    ps = con.prepareStatement(FIND_QUERY);
                    ps.setString(1,orderCode);
                    ps.setString(2,id);
                    rs = ps.executeQuery();
                    while(rs.next()){

             int quantity = rs.getInt("QUANTITY");
             double amount = rs.getDouble("AMOUNT");
             //String id = rs.getString("MTID");
             double unitPrice = rs.getDouble("UNITPRICE");
             double advancePymt = rs.getDouble("ADVANCE_PYMT");
             String itemCode = rs.getString("ITEM_CODE");
            int quantDeliver = rs.getInt("QUANTITY_DELIVER");
            int quantRemain = rs.getInt("QUANTITY_REMAIN");
            String warehouseCode = rs.getString("WAREHOUSE_CODE");
              orderItem = new SalesOrderItem(id,orderCode,quantity,unitPrice,amount,advancePymt,itemCode,quantDeliver,quantRemain,warehouseCode);
              
            
        }

            }catch(Exception er){
                    System.out.println("Error finding findSalesOrderItemDetailById ->"+er);
            }finally{
                    closeConnection(con,ps,rs);
            }
         return orderItem;
    }
    public SalesOrderItem findSalesOrderItemDetailById(String orderCode,String itemCode,String warehouseCode){
           
        String FIND_QUERY = "SELECT MTID,SORDER_CODE,QUANTITY,AMOUNT,UNITPRICE,ADVANCE_PYMT,ITEM_CODE,QUANTITY_DELIVER,QUANTITY_REMAIN,WAREHOUSE_CODE "+
                            "FROM IA_SO_ITEM WHERE SORDER_CODE =? AND ITEM_CODE=? AND WAREHOUSE_CODE=? ";

        Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            SalesOrderItem orderItem = null;

            try{
                    con = getConnection();
                    ps = con.prepareStatement(FIND_QUERY);
                    ps.setString(1,orderCode);
                    ps.setString(2,itemCode);
                    ps.setString(3,warehouseCode);
                    rs = ps.executeQuery();
                    while(rs.next()){

             int quantity = rs.getInt("QUANTITY");
             double amount = rs.getDouble("AMOUNT");
             String id = rs.getString("MTID");
             double unitPrice = rs.getDouble("UNITPRICE");
             double advancePymt = rs.getDouble("ADVANCE_PYMT");
             //String itemCode = rs.getString("ITEM_CODE");
            int quantDeliver = rs.getInt("QUANTITY_DELIVER");
            int quantRemain = rs.getInt("QUANTITY_REMAIN");
            //String warehouseCode = rs.getString("WAREHOUSE_CODE");
              orderItem = new SalesOrderItem(id,orderCode,quantity,unitPrice,amount,advancePymt,itemCode,quantDeliver,quantRemain,warehouseCode);
              
            
        }

            }catch(Exception er){
                    System.out.println("Error finding findSalesOrderItemDetailById(???) ->"+er);
            }finally{
                    closeConnection(con,ps,rs);
            }
         return orderItem;
    }
    public ArrayList findSalesOrderDeliveryByQuery(String filter2,String filter){

            ArrayList records = new ArrayList();
            String SELECT_QUERY = "SELECT DISTINCT(a.BATCH_CODE),a.SORDER_CODE,a.TRANS_DATE,b.CUSTOMER_CODE " + 
                                  "FROM IA_SO_DELIVERY_ITEM a,IA_SALES_ORDER b " + 
                                  "WHERE a.MTID IS NOT NULL AND a.SORDER_CODE = b.SORDER_CODE AND b.COMP_CODE='"+filter2+"' "+filter;
                                  
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try{
                    con = getConnection();
                    ps = con.prepareStatement(SELECT_QUERY);
                    rs = ps.executeQuery();
                    while(rs.next()){
                       String id = "";//rs.getString("MTID");
                       String batchCode = rs.getString("BATCH_CODE");
                       String orderCode = rs.getString("SORDER_CODE");
                       String transDate = formatDate(rs.getDate("TRANS_DATE"));
                       String desc  = "";//rs.getString("DESCRIPTION");
                       int quantity = 0;
                       double unitPrice = 0;
                       double amount = 0;
                       double advancePymt = 0;
                       String itemCode = "";
                       int quantDeliver = 0;
                       int quantRemain = 0;
                       int userId = 0;
                       String customerCode = rs.getString("CUSTOMER_CODE");
                       String warehouseCode = "";//rs.getString("WAREHOUSE_CODE");
                       SalesOrderDeliveryItem invoice = new SalesOrderDeliveryItem(id,orderCode,quantity,unitPrice,amount,
                                                        advancePymt,itemCode,quantDeliver,quantRemain,batchCode,transDate,
                                                        userId,customerCode,warehouseCode);
                       records.add(invoice);
                    }

            }catch(Exception er){
                    System.out.println("Error finding All findSalesOrderDeliveryByQuery...->"+er.getMessage());
                    //er.printStackTrace();
            }finally{
                    closeConnection(con,ps,rs);
            }

            return records ;
    }
    
    public ArrayList findAllSODeliveryItemList(String orderCode,String batchCode){

            ArrayList records = new ArrayList();
            String SELECT_QUERY = "SELECT MTID,SORDER_CODE,QUANTITY,AMOUNT,UNITPRICE,ADVANCE_PYMT,ITEM_CODE, "+
                                  "QUANTITY_DELIVER,QUANTITY_REMAIN,BATCH_CODE,TRANS_DATE,USERID,WAREHOUSE_CODE FROM IA_SO_DELIVERY_ITEM "+
                                  "WHERE SORDER_CODE =? AND BATCH_CODE=?";
                                  
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try{
                    con = getConnection();
                    ps = con.prepareStatement(SELECT_QUERY);
                    ps.setString(1,orderCode);
                    ps.setString(2,batchCode);
                    rs = ps.executeQuery();
                    while(rs.next()){
                            int quantity = rs.getInt("QUANTITY");
                            double amount = rs.getDouble("AMOUNT");
                            String id = rs.getString("MTID");
                            double unitPrice = rs.getDouble("UNITPRICE");
                            double advancePymt = rs.getDouble("ADVANCE_PYMT");
                            String itemCode = rs.getString("ITEM_CODE");
                            int quantDeliver = rs.getInt("QUANTITY_DELIVER");
                            int quantRemain = rs.getInt("QUANTITY_REMAIN");
                            //String batchCode = rs.getString("BATCH_CODE");
                            String transDate = formatDate(rs.getDate("TRANS_DATE"));
                             int userId = rs.getInt("USERID");
                             String customerCode = "";
                             String warehouseCode = rs.getString("WAREHOUSE_CODE");
                             SalesOrderDeliveryItem invoice = new SalesOrderDeliveryItem(id,orderCode,quantity,unitPrice,amount,advancePymt,
                             itemCode,quantDeliver,quantRemain,batchCode,transDate,userId,customerCode,warehouseCode);
                            records.add(invoice);
                    }

            }catch(Exception er){
                    System.out.println("Error finding All findAllSODeliveryItemList...->"+er.getMessage());
            }finally{
                    closeConnection(con,ps,rs);
            }

            return records ;
    }
    
    public ArrayList findAllSalesOrderItemDetail(String orderCode){

            ArrayList records = new ArrayList();
            String SELECT_QUERY = "SELECT MTID,SORDER_CODE,QUANTITY,AMOUNT,UNITPRICE,ADVANCE_PYMT,ITEM_CODE, "+
                                  "QUANTITY_DELIVER,QUANTITY_REMAIN,WAREHOUSE_CODE FROM IA_SO_ITEM WHERE SORDER_CODE =?";
                                  
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try{
                    con = getConnection();
                    ps = con.prepareStatement(SELECT_QUERY);
                    ps.setString(1,orderCode);
                    rs = ps.executeQuery();
                    while(rs.next()){
                            //String invoiceCode = rs.getString("INVOICECODE");
                            int quantity = rs.getInt("QUANTITY");
                            double amount = rs.getDouble("AMOUNT");
                            String id = rs.getString("MTID");
                            double unitPrice = rs.getDouble("UNITPRICE");
                            double advancePymt = rs.getDouble("ADVANCE_PYMT");
                            String itemCode = rs.getString("ITEM_CODE");
                            int quantDeliver = rs.getInt("QUANTITY_DELIVER");
                            int quantRemain = rs.getInt("QUANTITY_REMAIN");
                        String warehouseCode = rs.getString("WAREHOUSE_CODE");
                             SalesOrderItem invoice = new SalesOrderItem(id,orderCode,quantity,unitPrice,amount,
                                                    advancePymt,itemCode,quantDeliver,quantRemain,warehouseCode);
                            records.add(invoice);
                    }

            }catch(Exception er){
                    System.out.println("Error finding All findAllSalesOrderItemDetail...->"+er.getMessage());
            }finally{
                    closeConnection(con,ps,rs);
            }

            return records ;
    }
    public ArrayList findAllSalesOrderItemDetailByQuery(String query){

            ArrayList records = new ArrayList();
            String SELECT_QUERY = "SELECT MTID,SORDER_CODE,QUANTITY,AMOUNT,UNITPRICE,ADVANCE_PYMT,ITEM_CODE, "+
                                  "QUANTITY_DELIVER,QUANTITY_REMAIN,WAREHOUSE_CODE FROM IA_SO_ITEM WHERE MTID IS NOT NULL ";
                                  
            SELECT_QUERY = SELECT_QUERY + query;
            
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try{
                    con = getConnection();
                    ps = con.prepareStatement(SELECT_QUERY);
                    rs = ps.executeQuery();
                    while(rs.next()){
                            String orderCode = rs.getString("SORDER_CODE");
                            int quantity = rs.getInt("QUANTITY");
                            double amount = rs.getDouble("AMOUNT");
                            String id = rs.getString("MTID");
                            double unitPrice = rs.getDouble("UNITPRICE");
                            double advancePymt = rs.getDouble("ADVANCE_PYMT");
                            String itemCode = rs.getString("ITEM_CODE");
                            int quantDeliver = rs.getInt("QUANTITY_DELIVER");
                            int quantRemain = rs.getInt("QUANTITY_REMAIN");
                        String warehouseCode = rs.getString("WAREHOUSE_CODE");
                             SalesOrderItem invoice = new SalesOrderItem(id,orderCode,quantity,unitPrice,amount,
                                                    advancePymt,itemCode,quantDeliver,quantRemain,warehouseCode);
                            records.add(invoice);
                    }

            }catch(Exception er){
                    System.out.println("Error finding All findAllSalesOrderItemDetailByQuery...->"+er.getMessage());
            }finally{
                    closeConnection(con,ps,rs);
            }

            return records ;
    }
	public boolean updateShipment(String orderNo, String printed,
			String customerNo, String shipTo, String warehouse, String date,
			int period, String freight, String carrier, int quantityShipped,
			int quantityOrdered, String itemNo, String description,
			double unitPrice, String id) 
	{
		String query = "UPDATE  IA_SHIPMENTS   "+
						"WAREHOUSE_CODE = ? ,SHIPMENT_DATE = ?,"+
						"QUANTITY_ORDER = ? , QUANTITY_SHIP = ?,"+
						"ITEMCODE = ?,UNITPRICE = ?  "+
						"WHERE MTID = ?";
		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
	  try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, warehouse);
			ps.setDate(2, df.dateConvert(date));
			ps.setInt(3, quantityOrdered);
			ps.setInt(4, quantityShipped);
			ps.setString(5, itemNo);
			ps.setDouble(6, unitPrice);
			ps.setString(7, id);
			done = (ps.executeUpdate() != -1);
		} catch (Exception ex) {
			System.out.println("ERROR updating Shipment " + ex.getMessage());
			ex.printStackTrace();
		} 
  finally 
		{
			closeConnection(con, ps);
		}
		return done;
	}


	public void postVoucher(String no,String autoReverse,String date,String period,
		                      String description,double amount,String glAccount,String sbu,
		                      String name){
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	String id = helper.getGeneratedId("IA_JOURNAL_VOUCHER");
	final String JV_QUERY = "INSERT INTO IA_JOURNAL_VOUCHER("+
							"MTID,JOURNAL_CODE,AUTO_REVERSE,TRANSDATE,"+
							"PERIOD,DESCRIPTION,GLACCOUNT_NO,AMOUNT) "+
							"VALUES(?,?,?,?,?,?,?,?)";
         try{
				con = getConnection();
				ps = con.prepareStatement(JV_QUERY);
				ps.setString(1,id);
				ps.setString(2,no);
				ps.setString(3,autoReverse);
				ps.setDate(4,dateConvert(new java.util.Date()));
				ps.setString(5,period);
				ps.setString(6,description);
				ps.setString(7,glAccount);
				ps.setDouble(8,amount);

				ps.execute();

			}
			catch(Exception ex){
								System.out.println("WARN:Error posting voucher "+ex);
			                    }
								finally
								{
				                closeConnection(con,ps,rs);
		                        }
	}

	public void postInvoice(){

	}

	public void makePayment(){

	}

	/*public boolean createGlAccountType(String code,String name){

			String query = "INSERT INTO IA_GLACCOUNT_TYPE("+
						   "MTID,GLTYPE,NAME) VALUES(?,?,?)";
			Connection con = null;
			PreparedStatement ps = null;
			boolean done = false;
			String id = helper.getGeneratedId("IA_GLACCOUNT_TYPE");

			try{

				ps = con.prepareStatement(query);
				ps.setString(1, id);
				ps.setString(2,code);
				ps.setString(3, name);

				done = (ps.executeUpdate()!=-1);

			}catch(Exception ex){
				System.out.println("ERROR Creating Gl Account Type "+ex.getMessage());
			}finally{
				closeConnection(con,ps);
			}
			return done;
		}

  */
/*	public boolean updateGlAccountType(String id,String name) {

		String query = "UPDATE IA_GLACCOUNT_TYPE SET NAME = ? "+
					   "WHERE MTID = ?";

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;

			try{

				ps = con.prepareStatement(query);
				ps.setString(1,name);
				ps.setString(2,id);

				done = (ps.executeUpdate()!=-1);
			}catch(Exception ex){
				System.out.println("ERROR Updating Gl Account Type "+ex.getMessage());
				ex.printStackTrace();
			}finally{
				closeConnection(con,ps);
			}
		return done;
	}
*/
/*
	public void deleteGlAccountType(String id) {

		String Query = "DELETE FROM IA_GLACCOUNT_TYPE  "+
		   "WHERE MTID = ?";
		Connection con = null;
		PreparedStatement ps = null;
		try{

			con = getConnection();
			ps = con.prepareStatement(Query);
			ps.setString(1, id);

			ps.execute();


		}catch(Exception er){
			System.out.println("Error Deleting Gl Accoun Type... ->"+er.getMessage());
		}finally{
			closeConnection(con,ps);
		}

	}
*/
private java.util.ArrayList<SalesOrder> findSalesOrders(String filter) {
		java.util.ArrayList<SalesOrder> soList = new java.util.ArrayList<SalesOrder>();

		SalesOrder sorder = null;

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT MTID,SORDER_CODE,DESCRIPTION,POSTED,STATUS,"+
                               "CUSTOMER_CODE,PORDER_CODE,TRANS_DATE,SHIP_DATE,"+
                               "FREIGHT_CODE,CARRIER_CODE,REQ_PERS_IDENTITY,APPROVE_OFFICER,USERID,PROJECT_CODE FROM IA_SALES_ORDER ";
                               
		query += filter;
                
		try {

			con = getConnection();
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {

				String orderNo = rs.getString("SORDER_CODE");
                                String desc = rs.getString("DESCRIPTION");
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
                                String reqPersIdentity = rs.getString("req_Pers_Identity");
                                String approveOfficer = rs.getString("approve_officer");
                                String projectCode = rs.getString("PROJECT_CODE");
				sorder = new SalesOrder(id,orderNo,desc,posted,status,customerNo,
                                po,date,shipDate,freight,carrier,userId,reqPersIdentity,approveOfficer,projectCode);
				soList.add(sorder);

			}
		}catch (Exception ex) {
					System.out.println("ERROR Querying Sales Order " + ex.getMessage());
					ex.printStackTrace();
				} finally {
					closeConnection(con, ps,rs);
				}

		return soList;
	}
        
    //this method is used to create new delivery (sales Delivery)
    public boolean createSODeliveryItem(String orderCode,String batchCode,String[] id,String[] itemCode,
                                        String[] quantity,String[] quantDeliver,int userId,String[] unitPrice,
                                        String[] amount,String[] warehouseCode){
        //ArrayList list = new ArrayList();
         String CREATE_QUERY = "INSERT INTO IA_SO_DELIVERY_ITEM "+
                          "(SORDER_CODE,BATCH_CODE,MTID,ITEM_CODE,QUANTITY_DELIVER,"+
                          "QUANTITY_REMAIN,USERID,QUANTITY,UNITPRICE,AMOUNT,TRANS_DATE,CUSTOMER_CODE,WAREHOUSE_CODE) " +
                          "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
                      
            Connection con = null;
            PreparedStatement ps = null;
            //id = helper.getGeneratedId("IA_PO_DELIVERY_ITEM");
            boolean done = false;
            //list = findAllPurchaseOrderItemDetail(orderCode);
            String customerCode = getCodeName("SELECT CUSTOMER_CODE FROM IA_SALES_ORDER WHERE SORDER_CODE='"+orderCode+"'");
            try{
                    int intQuantity = 0;
                    int intQuantDeliver = 0;
                    int intQuantRemain = 0;
                    double unitPrice_ = 0;
                    double amount_ = 0;
                    
                    con = getConnection();
                    ps = con.prepareStatement(CREATE_QUERY);
                    for(int i=0; i<id.length; i++)
                    {
                     intQuantDeliver = Integer.parseInt(quantDeliver[i]!=null?quantDeliver[i]:"0");
                     intQuantity = Integer.parseInt(quantity[i]!=null?quantity[i]:"0");
                     intQuantRemain = intQuantity - intQuantDeliver; 
                     unitPrice_ = Double.parseDouble(unitPrice[i]!=null?unitPrice[i]:"0");
                     amount_ = Double.parseDouble(amount[i]!=null?amount[i]:"0");
                     if(intQuantDeliver > 0){
                     ps.setString(1,orderCode);
                     ps.setString(2,batchCode);
                     ps.setString(3,id[i]);
                     ps.setString(4,itemCode[i]);
                     ps.setInt(5,Integer.parseInt(quantDeliver[i]!=null?quantDeliver[i]:"0"));
                     ps.setInt(6,intQuantRemain);
                     ps.setInt(7,userId);
                     ps.setInt(8,intQuantity);
                     ps.setDouble(9,unitPrice_);
                     ps.setDouble(10,amount_);
                     ps.setDate(11,dateConvert(new java.util.Date()));
                     ps.setString(12,customerCode);
                     ps.setString(13,warehouseCode[i]);
                     ps.addBatch();
                     } 
                    }
                done = (ps.executeBatch().length != -1);

            }catch(Exception er){
                    System.out.println("Error creating SODeliveryItem()... ->"+er.getMessage());
            }finally{
                    closeConnection(con,ps);
            }
         return done;
    }
    
    //for delivery update
     public boolean updateSODeliveryItem(String orderCode,String batchCode,String[] id,
                                         String[] quantity,String[] quantDeliver){
         //ArrayList list = new ArrayList();
          String UPDATE_QUERY =  "UPDATE IA_SO_DELIVERY_ITEM SET QUANTITY_DELIVER=?,QUANTITY_REMAIN=? WHERE SORDER_CODE=? AND BATCH_CODE=? AND MTID=?";
          
             Connection con = null;
             PreparedStatement ps = null;
              boolean done = false;
              
         try{
                   int intQuantity = 0;
                   int intQuantDeliver = 0;
                   int intQuantRemain = 0;
                   con = getConnection();
                   ps = con.prepareStatement(UPDATE_QUERY);
                    
                     for(int i=0; i < id.length; i++)
                     { 
                      intQuantDeliver = Integer.parseInt(quantDeliver[i]!=null?quantDeliver[i]:"0");
                      intQuantity = Integer.parseInt(quantity[i] != null ? quantity[i] : "0");
                      intQuantRemain = intQuantity - intQuantDeliver; 
                      ps.setInt(1,intQuantDeliver);
                      ps.setInt(2,intQuantRemain);
                      ps.setString(3,orderCode);
                      ps.setString(4,batchCode);
                      ps.setString(5,id[i]);
                      ps.addBatch();
                     }
                    done = ((ps.executeBatch()).length != -1);
                 

             }catch(Exception er){
                     System.out.println("Error updating SODeliveryItem()... ->"+er.getMessage());
                     er.printStackTrace();
             }finally{
                     closeConnection(con,ps);
             }
         
         return done;
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
     
    public boolean isItemExistInInventoryTotals(String itemCode,String warehouseCode){

             boolean exists = false;
             String updateQuery = "SELECT COUNT(*) FROM ST_INVENTORY_TOTALS WHERE ITEM_CODE=? AND WAREHOUSE_CODE=?";
             Connection con = null;
             PreparedStatement ps = null;
             ResultSet rs = null;
             //StringBuffer sb = new StringBuffer("INSERT INTO ST_INVENTORY_TOTALS "+
             //                  "(BALANCE,USERID,ITEM_CODE,WAREHOUSE_CODE) VALUES (?,?,?,?)");
             
             try{
                     con = getConnection();
                     ps = con.prepareStatement(updateQuery);
                    ps.setString(1,itemCode);
                    ps.setString(2,warehouseCode);
                     rs = ps.executeQuery();

                     while(rs.next()){
                             int counted = rs.getInt(1);
                             if(counted > 0){
                             exists = true;
                            // sb = new StringBuffer("UPDATE ST_INVENTORY_TOTALS "+
                            //      "SET BALANCE = BALANCE - ?,USERID = ? WHERE ITEM_CODE = ? AND WAREHOUSE_CODE = ?)");
                      }
                     }

             }catch(Exception er){
                     System.out.println("Error in isItemExistInInvestoryTotals()... ->"+er);
                     er.printStackTrace();
             }finally{
                     closeConnection(con,ps);
             }
             
             return exists;
     }
    //post Shipment 2 update Inventory Items/History
     public boolean postShipment2InventoryHistory(String orderCode,String batchCode,String companyCode,String [] quantDeliver,String filter){
     
         ArrayList list = new ArrayList();
         String CREATE_QUERY = "INSERT INTO ST_INVENTORY_HISTORY "+
                               "(MTID,ITEM_CODE,TRANS_DESC,QUANTITY,WAREHOUSE_CODE,TRANS_DATE,BATCH_CODE,USERID,COMP_CODE,PREVIOUS_BALANCE) " +
                               "VALUES (?,?,?,?,?,?,?,?,?,?)";
          
          //check item from warehouse b4 insert or update inventory totals
          //String query =  this.isItemExistInInventory("SELECT ITEM_CODE,WAREHOUSE_CODE FROM ST_INVENTORY_TOTALS");
                               
          String UPDATE_QUERY = "UPDATE IA_SO_DELIVERY_ITEM SET POSTED = ? WHERE BATCH_CODE=?";                     
          //String warehouseCode = getCodeName("SELECT WAREHOUSE_CODE FROM IA_SO_ITEM WHERE SORDER_CODE = '"+orderCode+"'");    
            
           boolean autoCommit = false;
             Connection con = null;
             PreparedStatement ps = null;         
             //id = helper.getGeneratedId("IA_PO_DELIVERY_ITEM");
             boolean done = false;
             list = findAllSODeliveryItemList(orderCode,batchCode,filter);
                    
             try{
                     con = getConnection();
                     autoCommit = con.getAutoCommit();
                     con.setAutoCommit(false);
                     //insert 
                     ps = con.prepareStatement(CREATE_QUERY);
                     for(int i=0; i<list.size(); i++)
                     {
                      ps.setString(1,((SalesOrderDeliveryItem)list.get(i)).getId());
                      ps.setString(2,((SalesOrderDeliveryItem)list.get(i)).getItemCode());
                      ps.setString(3,"SALES ORDER SHIPMENT");
                      ps.setInt(4,((SalesOrderDeliveryItem)list.get(i)).getQuantDeliver());
                      ps.setString(5,((SalesOrderDeliveryItem)list.get(i)).getWarehouseCode());
                      ps.setDate(6,dateConvert(new java.util.Date()));
                      ps.setString(7,((SalesOrderDeliveryItem)list.get(i)).getBatchCode());
                      ps.setInt(8,((SalesOrderDeliveryItem)list.get(i)).getUserId());
					  ps.setString(9,companyCode);
					  ps.setString(10,quantDeliver[i]);
                      ps.addBatch();
                     }
                 int j = (ps.executeBatch()).length;
                 //update
                 ps = con.prepareStatement(UPDATE_QUERY);
                 ps.setString(1,"Y");
                 ps.setString(2,batchCode);
                 int k = ps.executeUpdate();
                 if((j != -1) && (k > 0))
                 {
                   //con.rollback();
                   con.commit();
                   con.setAutoCommit(autoCommit);
                   done = true;
                 }
                 
                 //done = (ps.executeBatch().length != -1);
                           
             }catch(Exception er){
                     System.out.println("Error posting Shipment 2 Inventory History... ->"+er.getMessage());
                     er.printStackTrace();
                    try{
                        con.rollback();
                        done = false;
                    }
                    catch(SQLException ex){
                        System.out.println("Error In Transaction....Rollback ->"+er.getMessage());
                    
                    }
                 
             }finally{
                     closeConnection(con,ps);
             }
           
         return done;
     }
    
    //post Shipment 2 update Inventory Items/Totals
     public boolean postShipment2InventoryTotals(String orderCode,String batchCode,String filter){
     
            ArrayList list = new ArrayList();
            ArrayList ii = new ArrayList();
            ArrayList uu = new ArrayList();
            String warehouseCode = "";
                       
             //check item from warehouse b4 insert or update inventory totals
             //String query =  this.isItemExistInInventoryTotals("","");
             Connection con = null;
             PreparedStatement ps = null;         
             //id = helper.getGeneratedId("IA_PO_DELIVERY_ITEM");
             boolean done = false;
             String mtId = "";
             String itemCode = "";
             int itemBalance = 0;
             String desc = "";
             int userId = 0;
             //warehouseCode = getCodeName("SELECT WAREHOUSE_CODE FROM IA_SALES_ORDER WHERE PORDER_CODE = '"+orderCode+"'");  
             
             list = findAllSODeliveryItemList(orderCode,batchCode,filter);
             for(int i=0; i<list.size(); i++)
             {
              if(isItemExistInInventoryTotals(((SalesOrderDeliveryItem)list.get(i)).getItemCode(),((SalesOrderDeliveryItem)list.get(i)).getWarehouseCode())){
                mtId = ((SalesOrderDeliveryItem)list.get(i)).getId();
                itemCode = ((SalesOrderDeliveryItem)list.get(i)).getItemCode();
                itemBalance = ((SalesOrderDeliveryItem)list.get(i)).getQuantDeliver();
                desc = "";
                warehouseCode = ((SalesOrderDeliveryItem)list.get(i)).getWarehouseCode();
                userId = ((SalesOrderDeliveryItem)list.get(i)).getUserId();
                InventoryTotal uupdate = new InventoryTotal(mtId,itemCode,itemBalance,desc,warehouseCode,userId,0,0);
                uu.add(uupdate); 
             }
             else{
                 mtId = ((SalesOrderDeliveryItem)list.get(i)).getId();
                 itemCode = ((SalesOrderDeliveryItem)list.get(i)).getItemCode();
                 itemBalance = ((SalesOrderDeliveryItem)list.get(i)).getQuantDeliver();
                 desc = "";
                  warehouseCode = ((SalesOrderDeliveryItem)list.get(i)).getWarehouseCode();
                 userId = ((SalesOrderDeliveryItem)list.get(i)).getUserId();
                 InventoryTotal iinsert = new InventoryTotal(mtId,itemCode,itemBalance,desc,warehouseCode,userId,0,0); 
                 ii.add(iinsert);
             }
          
            }
            try{
                  con = getConnection();
                   //insert 
                   if(uu.size() > 0){
                    ps = con.prepareStatement("UPDATE ST_INVENTORY_TOTALS SET BALANCE = BALANCE - ?,USERID = ? "+
                                              "WHERE ITEM_CODE = ? AND WAREHOUSE_CODE = ?");     
                                              
                   for(int i=0; i<uu.size(); i++)
                   {
                     ps.setInt(1,((InventoryTotal)uu.get(i)).getItemBalance());
                     ps.setInt(2,((InventoryTotal)uu.get(i)).getUserId());
                     ps.setString(3,((InventoryTotal)uu.get(i)).getItemCode());
                     ps.setString(4,((InventoryTotal)uu.get(i)).getWareHouseCode());
                     ps.addBatch();
                   }
                   done = (ps.executeBatch()).length != -1;
                   }
                 if(ii.size() > 0){  
                 ps = con.prepareStatement("INSERT INTO ST_INVENTORY_TOTALS "+ 
                                           "(BALANCE,USERID,ITEM_CODE,WAREHOUSE_CODE) VALUES (?,?,?,?)");
                                           
                 for(int i=0; i<ii.size(); i++)
                 {
                     ps.setInt(1,((InventoryTotal)ii.get(i)).getItemBalance());
                     ps.setInt(2,((InventoryTotal)ii.get(i)).getUserId());
                     ps.setString(3,((InventoryTotal)ii.get(i)).getItemCode());
                     ps.setString(4,((InventoryTotal)ii.get(i)).getWareHouseCode());
                     ps.addBatch();
                 }
                   done = (ps.executeBatch()).length != -1;
                }   
                                           
             }catch(Exception er){
                     System.out.println("Error posting Shipment 2 Inventory Totals... ->"+er.getMessage());
                     er.printStackTrace();
                   
             }finally{
                     closeConnection(con,ps);
             }
           
         return done;
     } 
    public ArrayList findAllSODeliveryItemList(String orderCode,String batchCode,String filter){

            ArrayList records = new ArrayList();
           /* String para = "";
            for(int i=0; i<printChk.length; i++){
             para = para + ",'"+printChk[i]+"'";
                
            }*/
            
            String SELECT_QUERY = "SELECT MTID,SORDER_CODE,QUANTITY,AMOUNT,UNITPRICE,ADVANCE_PYMT,ITEM_CODE, "+
                                  "QUANTITY_DELIVER,QUANTITY_REMAIN,BATCH_CODE,TRANS_DATE,USERID,WAREHOUSE_CODE "+
                                  "FROM IA_SO_DELIVERY_ITEM WHERE SORDER_CODE =? AND BATCH_CODE=? "+filter;// AND MTID IN ("+para.substring(1)+")";
                                  
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try{
                    con = getConnection();
                    ps = con.prepareStatement(SELECT_QUERY);
                    ps.setString(1,orderCode);
                    ps.setString(2,batchCode);
                    rs = ps.executeQuery();
                    while(rs.next()){
                            int quantity = rs.getInt("QUANTITY");
                            double amount = rs.getDouble("AMOUNT");
                            String id = rs.getString("MTID");
                            double unitPrice = rs.getDouble("UNITPRICE");
                            double advancePymt = rs.getDouble("ADVANCE_PYMT");
                            String itemCode = rs.getString("ITEM_CODE");
                            int quantDeliver = rs.getInt("QUANTITY_DELIVER");
                            int quantRemain = rs.getInt("QUANTITY_REMAIN");
                            //String batchCode = rs.getString("BATCH_CODE");
                            String transDate = formatDate(rs.getDate("TRANS_DATE"));
                        int userId = rs.getInt("USERID");
                        String warehouseCode = rs.getString("WAREHOUSE_CODE");
                        String customerCode = "";
                             SalesOrderDeliveryItem invoice = new SalesOrderDeliveryItem(id,orderCode,quantity,unitPrice,amount,advancePymt,
                                                                 itemCode,quantDeliver,quantRemain,batchCode,transDate,userId,customerCode,warehouseCode);
                            records.add(invoice);
                    }
               
            }catch(Exception er){
                    System.out.println("Error finding All findAllSODeliveryItemList(???)...->"+er.getMessage());
            }finally{
                    closeConnection(con,ps,rs);
            }

            return records ;
    }

/*
	private java.util.ArrayList<Shipment> findShipments(String filter) {
		java.util.ArrayList<Shipment> shList = new java.util.ArrayList<Shipment>();

		Shipment ship = null;

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT MTID,SHIPMENT_CODE,SORDER_CODE,"+
						"WAREHOUSE_CODE,SHIPMENT_DATE,QUANTITY_ORDER,"+
						"QUANTITY_SHIP,ITEMCODE,UNITPRICE    "+
						"FROM IA_SHIPMENTS ";
		query += filter;
		try {
			con = getConnection();
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				String orderNo = rs.getString("SORDER_CODE");
				String printed = "";
				String customerNo = "";
				String shipTo = "";
				String warehouse = rs.getString("WAREHOUSE_CODE");
				String date = sdf.format(rs.getDate("SHIPMENT_DATE"));
				int period = 0;
				String freight = "";
				String carrier = "";
				int quantityShipped = rs.getInt("QUANTITY_SHIP");
				int quantityOrdered = rs.getInt("QUANTITY_ORDER");
				String itemNo = rs.getString("ITEMCODE");
				String description = "";
				double unitPrice = rs.getDouble("UNITPRICE");
				String id = rs.getString("MTID");

				ship = new Shipment(id,orderNo, printed, customerNo, shipTo,
						warehouse, date, period, freight, carrier,
						quantityShipped, quantityOrdered, itemNo, description,
						unitPrice);

				shList.add(ship);

			}

		} catch (Exception ex) {
			System.out.println("ERROR querying Shipment " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			closeConnection(con, ps,rs);
		}

		return shList;
	}
     */
	public java.util.ArrayList<SalesOrder> findSalesOrdersByQuery(String filter2,String filter) {
		java.util.ArrayList<SalesOrder> soList = new java.util.ArrayList<SalesOrder>();
		String criteria = " WHERE COMP_CODE = '"+filter2+"' AND MTID IS NOT NULL " + filter;
		soList = findSalesOrders(criteria);
		return soList;
	}


	public SalesOrder findSalesOrderByOrderNo(String orderno) {
		SalesOrder sorder = new SalesOrder();
		String criteria = " WHERE SORDER_CODE = '"+orderno+"'"; // Where OrderNo = filter
		sorder = (SalesOrder)findSalesOrders(criteria).get(0);
		return sorder;
	}

	public SalesOrder findSalesOrderByID(String id) {
		SalesOrder sorder = new SalesOrder();
		String criteria = " WHERE MTID = '" + id + "'"; // Where OrderNo =
														// filter
		sorder = (SalesOrder)findSalesOrders(criteria).get(0);
		return sorder;
	}
    public String getCodeName(String query)
    {
     String result = "";
     Connection con = null;
     ResultSet rs = null; 
     PreparedStatement ps = null;
 //    System.out.println("getCodeName Query: "+query);
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
        System.out.println("Error in SalesOrderServiceBus- getCodeName()... ->"+er);
        er.printStackTrace();
     }finally{
        closeConnection(con,ps);
      }   
      return result;
     }
	/*public java.util.ArrayList<Shipment> findShipmentByQuery(String filter) {
		java.util.ArrayList<Shipment> shList = new java.util.ArrayList<Shipment>();
		String criteria = " WHERE MTID IS NOT NULL " + filter;
		shList = findShipments(criteria);
		return shList;
	}

	public Shipment findShipmentByOrderNo(String filter) {
		Shipment sorder = new Shipment();
		String criteria = " WHERE  SHIPMENT_CODE ='" + filter+"'"; // Where OrderNo = filter
		sorder = (Shipment)findShipments(criteria).get(0);
		return sorder;
	}

	public Shipment findShipmentByID(String id) {
		Shipment sorder = new Shipment();
		String criteria = " WHERE MTID = '" + id + "'"; // Where OrderNo =
														// filter
		sorder = (Shipment)findShipments(criteria).get(0);
		return sorder;
	}
*/
	/*public ArrayList findGlAccountType(){

		ArrayList records = new ArrayList();
		String query = "SELECT MTID,GLTYPE,NAME FROM IA_GLACCOUNT_TYPE ";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try{

			con = getConnection();
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while(rs.next()){

				String id = rs.getString("MTID");
				String code = rs.getString("GLTYPE");
				String name = rs.getString("NAME");
				GlAccountType glt = new GlAccountType(id,code,name);
				records.add(glt);

			}

		}catch(Exception er){
			System.out.println("Error finding All Gl Account Type...->"+er.getMessage());
		}finally{
			closeConnection(con,ps,rs);
		}

		return records ;
	}
*/
	/*public GlAccountType findGlAccountTypeById(String id){

		String query = "SELECT MTID,GLTYPE,NAME FROM IA_GLACCOUNT_TYPE "+
						"WHERE MTID = ?";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		GlAccountType glt = null;

		try{

			con = getConnection();
			ps = con.prepareStatement(query);

			ps.setString(1, id);

			rs = ps.executeQuery();

			while(rs.next()){
				String iid = rs.getString("MTID");
				String icode = rs.getString("GLTYPE");
				String iname = rs.getString("NAME");
				glt = new GlAccountType(iid,icode,iname);

			}

		}catch(Exception er){
			System.out.println("Error finding Gl Account Type By ID ->"+er);
		}finally{
			closeConnection(con,ps,rs);
		}


		return glt;
	}

*/
/*
	public boolean isGlAccountTypeExisting(String id){

		//GlAccountType ia = null;
		boolean exists = false;
		String Query = "";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try{

			con = getConnection();
			ps = con.prepareStatement(Query);

			ps.setString(1, id);
			rs = ps.executeQuery();

			while(rs.next()){
				int counted = rs.getInt(1);
				if(counted > 0){
				exists = true;
			 }
			}

		}catch(Exception er){
			System.out.println("Error detecting accountType... ->"+er);
		}finally{
			closeConnection(con,ps);
		}

		return exists;
	        
        }
  */      
    public double getSalesOrderTotalAmount(String orderCode){
       
           String query = "";
           double result = 0;
           query = "SELECT SUM(QUANTITY*UNITPRICE) FROM ST_SO_ITEM WHERE SORDER_CODE=?";
       
       Connection con = null;
       PreparedStatement ps = null;
       ResultSet rs = null;

       try{
       
           con = getConnection();
           ps = con.prepareStatement(query);
           ps.setString(1,orderCode);
           rs = ps.executeQuery();
           while(rs.next()){
           
             result = Double.parseDouble(rs.getString(1)==null?"0":rs.getString(1));
           }

           }catch(Exception er){
                   System.out.println("Error in getSalesOrderTotalAmount()... ->"+er);
           }finally{
                   closeConnection(con,ps);
           }   
       
           return result;
       }
	   
	   
	public ArrayList findAllGlAccount(String glaccount)
	{

            ArrayList records = new ArrayList();
            String SELECT_QUERY = "SELECT distinct gl_acount from ia_imprest_items";
                                  
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try
		        {
                    con = getConnection();
                    ps = con.prepareStatement(SELECT_QUERY);
                    ps.setString(1,glaccount);
                    rs = ps.executeQuery();
                    while(rs.next()){ 
                    glaccount = rs.getString("glaccount");
							}
                 }
               
           catch(Exception er)
            {
                    System.out.println("Error finding All findAllSODeliveryItemList(???)...->"+er.getMessage());
            }
           finally
            {
                    closeConnection(con,ps,rs);
            }

            return records ;
          
	}   
	  public boolean updateInventoryTotals(int quantity,String warehouseCode,String itemCode)
	  {

          String UPDATE_QUERY =  "UPDATE ST_INVENTORY_TOTALS SET BALANCE = BALANCE-? , TMP_BALANCE = coalesce(TMP_BALANCE,0)+?  WHERE ITEM_CODE=?  " +
          		" AND WAREHOUSE_CODE=?";
          Connection con = null;
          PreparedStatement ps = null;
          boolean done = false;
      //    System.out.println("<<<<<<<=======quantity: "+quantity+"    warehouseCode: "+warehouseCode+"    itemCode: "+itemCode);
          try{
                  con = getConnection();
                  ps = con.prepareStatement(UPDATE_QUERY);
                  ps.setInt(1,quantity); 
                  ps.setInt(2,quantity); 
                  ps.setString(3,itemCode); 
                  ps.setString(4,warehouseCode); 
                                      
              done = (ps.executeUpdate() != -1);

          }catch(Exception er){
                  done = false;
                  System.out.println("Error UPDATING ST_INVENTORY_TOTALS... ->"+er.getMessage());
                  er.printStackTrace();
          }finally{
                  closeConnection(con,ps);
          }
  return done;
  }
	  
	  public boolean updateInventoryTotals2(int quantity,String warehouseCode,String itemCode)
	  {

          String UPDATE_QUERY =  "UPDATE ST_INVENTORY_TOTALS SET BALANCE = BALANCE+? AND TMP_BALANCE = TMP_BALANCE - ?  WHERE ITEM_CODE=?   AND WAREHOUSE_CODE=?";
          Connection con = null;
          PreparedStatement ps = null;
          boolean done = false;

          try{
                  con = getConnection();
                  ps = con.prepareStatement(UPDATE_QUERY);
                  ps.setInt(1,quantity);  
                  ps.setInt(2,quantity);  
                  ps.setString(3,itemCode); 
                  ps.setString(4,warehouseCode); 
                                      
              done = (ps.executeUpdate() != -1);

          }catch(Exception er){
                  done = false;
                  System.out.println("Error UPDATING ST_INVENTORY_TOTALS... ->"+er.getMessage());
                  er.printStackTrace();
          }finally{
                  closeConnection(con,ps);
          }
  return done;
  }

}
