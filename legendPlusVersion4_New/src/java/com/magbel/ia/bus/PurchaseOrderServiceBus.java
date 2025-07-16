/**
 *
 */
package com.magbel.ia.bus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import com.magbel.ia.dao.PersistenceServiceDAO;

import com.magbel.ia.vao.Vendor;
import com.magbel.ia.vao.Invoice;
import com.magbel.ia.vao.Carrier;
import com.magbel.ia.vao.Freight;
import com.magbel.ia.vao.Cheque;
import com.magbel.ia.vao.PurchaseOrder;
import com.magbel.ia.vao.PoVendorItem;

import com.magbel.ia.dao.PersistenceServiceDAO;
import com.magbel.ia.util.ApplicationHelper;

import com.magbel.ia.util.CodeGenerator;
import com.magbel.ia.vao.APInvoice;

import com.magbel.ia.vao.APInvoiceAccount;

import com.magbel.ia.vao.InventoryTotal;
import com.magbel.ia.vao.PurchaseOrderDelivery;
import com.magbel.ia.vao.PurchaseOrderDeliveryItem;
import com.magbel.ia.vao.PurchaseOrderItem;

import com.magbel.ia.vao.SalesOrderDeliveryItem;
import com.magbel.util.DataConnect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PurchaseOrderServiceBus extends PersistenceServiceDAO {


	SimpleDateFormat sdf;
	final String space = "  ";
	final String comma = ",";
	java.util.Date date;
        public String auotoGenCode = "";

	com.magbel.util.DatetimeFormat df;
	ApplicationHelper helper;
        CodeGenerator cg;
        public String id;
        

	public PurchaseOrderServiceBus() {
		super();
		// TODO Auto-generated constructor stub
		sdf = new SimpleDateFormat("dd-MM-yyyy");
		df = new com.magbel.util.DatetimeFormat();
		helper = new ApplicationHelper();
	        cg = new CodeGenerator();
	}

	public boolean createPurchaseOrder(String orderCode, String vendorCode,double amount,String receiptDate,
			String orderedBy, String warehouse,String carrier,
			String freight,String desc,int userId,String advancePymtOpt,String projectCode,String companyCode) {
                        
		String query = "INSERT INTO IA_PURCHASE_ORDER("+
						"MTID,PORDER_CODE,VENDOR_CODE,TOTAL_AMOUNT,TRANS_DATE,DATE_RECEIVED,"+
						"ORDERBY,WAREHOUSE_CODE,CARRIER_CODE,"+
						"FREIGHT_CODE,USERID,ADVANCE_PYMT,DESCRIPTION,PROJECT_CODE,COMP_CODE) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		String isAutoGen = getCodeName("SELECT auto_generate_ID FROM IA_GB_COMPANY");
                Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
	      
		String id = helper.getGeneratedId("IA_PURCHASE_ORDER");
	         orderCode = isAutoGen.trim().equalsIgnoreCase("Y") ? cg.generateCode("PURCHASE ORDER","","","") : orderCode;
                  auotoGenCode = orderCode;
	           
                try {
		        con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1,id);
			ps.setString(2,orderCode);
			ps.setString(3,vendorCode);
			ps.setDouble(4,amount);
			ps.setDate(5,dateConvert(new java.util.Date()));
			ps.setDate(6,dateConvert(receiptDate));
			ps.setString(7,orderedBy);
			ps.setString(8,warehouse);
			ps.setString(9,carrier);
			ps.setString(10, freight);
                        ps.setInt(11,userId);
                        ps.setString(12,advancePymtOpt);
                        ps.setString(13,desc);
                        ps.setString(14,projectCode);
						ps.setString(15,companyCode);
                        
                        done = (ps.executeUpdate()!=-1);
		}catch(Exception ex){
		        done = false;
			System.out.println("ERROR Creating Purchase Order "+ex.getMessage());
		}finally{
			closeConnection(con,ps);
		}
		return done;
	}


	/*public boolean updatePurchaseOrder(String orderNo, String vendorCode, String printed,
			String status, double amount, String date, String dateReceived,
			String orderedBy, String warehouse, String comment, String carrier,
			String freight, int quantity, String vendorItemNo,
			String description, double unitCost, double amount_paid,
			String overrideDescription, int quantityReceived,
			int quantityBO, String completed) {

		String query = "UPDATE IA_PURCHASE_ORDER   "+
						"SET PRINTED = ?,STATUS = ?,TOTAL_AMOUNT = ?,"+
						"TRANS_DATE = ?,DATE_RECEIVED = ?,ORDERBY = ?,"+
						"WAREHOUSE_CODE = ?,COMMENT = ?,CARRIER_CODE = ? ,"+
						"FREIGHT_CODE = ? ,QUANTITY = ?,ITEMCODE = ? ,"+
						"UNITCOST = ?,AMOUNT = ? , OVERRIDE_DESC = ? ,"+
						"QUANT_RECEIVED = ?,QUANTBO = ?, COMPLETED = ?  "+
						"WHERE MTID = ?";

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;

		try {

			ps = con.prepareStatement(query);

			done = (ps.executeUpdate()!=-1);
		}catch(Exception ex) {
			System.out.println("ERROR Updating Purchase Order "+ex.getMessage());
		}finally{
			closeConnection(con,ps);
		}
		return done;
	}*/

public boolean createPoVendorItem(String itemCode, String vendorCode,
			String preference, String status, String description, double cost,
			String date, int quantity, double total) {

		final String query = "INSERT INTO IA_VENDOR_ITEMS("+
							"ITEM_CODE,VENDOR_CODE,PREFERENCE,"+
							"STATUS,DESCRIPTION,COST,TRANS_DATE,"+
							"QUANTITY,TOTAL_AMOUNT,MTID) VALUES("+
							"?,?,?,?,?,?,?,?,?,?)";

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String id = helper.getGeneratedId("IA_VENDOR_ITEMS");

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, itemCode);
			ps.setString(2, vendorCode);
			ps.setString(3, preference);
			ps.setString(4, "ACTIVE");
			ps.setString(5, description);
			ps.setDouble(6, cost);
			ps.setDate(7, df.dateConvert(date));
			ps.setInt(8, quantity);
			ps.setDouble(9, total);
			ps.setString(10, id);

			done = (ps.executeUpdate() != -1);
		} catch (Exception ex) {
			System.out.println("ERROR Creating POVENDORITEM "
					+ ex.getMessage());
			ex.printStackTrace();
		} finally {
			closeConnection(con, ps);
		}
		return done;
	}

	/**
	 * <p>
	 * updatePurchaseOrder:boolean
	 * </p>
	 *
	 * @param orderNo
	 * @param vendorCode
	 * @param printed
	 * @param status
	 * @param amount
	 * @param date
	 * @param dateReceived
	 * @param orderedBy
	 * @param warehouse
	 * @param comment
	 * @param carrier
	 * @param freight
	 * @param quantity
	 * @param vendorItemNo
	 * @param description
	 * @param unitCost
	 * @param amount_paid
	 * @param overrideDescription
	 * @param quantityReceived
	 * @param quantityBO
	 * @param completed
	 * @return
	 */
	public boolean updatePurchaseOrder(String orderCode, String vendorCode,double amount,String receiptDate,
			String orderedBy, String warehouse,String carrier,
			String freight,String desc,String advancePymtOpt,String projectCode) {

		String query = "UPDATE IA_PURCHASE_ORDER SET TOTAL_AMOUNT = ?,"+
                               "TRANS_DATE = ?,DATE_RECEIVED = ?,ORDERBY = ?,"+
                               "WAREHOUSE_CODE = ? ,CARRIER_CODE = ?"+
                               ",FREIGHT_CODE = ?,ADVANCE_PYMT=?,DESCRIPTION=?,VENDOR_CODE=?,PROJECT_CODE=? WHERE PORDER_CODE = ?";

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
                                
		try {

			con = getConnection();
			ps = con.prepareStatement(query);
			
			ps.setDouble(1, amount);
			ps.setDate(2, dateConvert(new java.util.Date()));
			ps.setDate(3, dateConvert(receiptDate));
			ps.setString(4, orderedBy);
			ps.setString(5, warehouse);
			ps.setString(6, carrier);
			ps.setString(7, freight);
                        ps.setString(8,advancePymtOpt);
                        ps.setString(9,desc);
			ps.setString(10, vendorCode);
                        ps.setString(11,projectCode);
                        ps.setString(12,orderCode);

			done = (ps.executeUpdate() != -1);
		} catch (Exception ex) {
			System.out.println("ERROR Updating Purchase Order "
					+ ex.getMessage());
			ex.printStackTrace();
		} finally {
			closeConnection(con, ps);
		}
		return done;
	}

	public boolean updatePoVendorItem(String id,String itemCode, String vendorCode,
			String preference, String status, String description, double cost,
			String date, int quantity, double total) {

		String query = "UPDATE IA_VENDOR_ITEMS   SET "+
					   "VENDOR_CODE = ?, PREFERENCE = ?,STATUS = ?,"+
					   "DESCRIPTION = ?, COST = ?,TRANS_DATE = ?,"+
					   "QUANTITY = ?, TOTAL_AMOUNT = ? WHERE MTID = ?";

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		//String id = helper.getGeneratedId("IA_VENDOR_ITEMS");

		try {

			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, itemCode);
			ps.setString(2, vendorCode);
			ps.setString(3, preference);
			ps.setString(4, description);
			ps.setString(5, status);
			ps.setDouble(6, cost);
			ps.setDate(7, df.dateConvert(date));
			ps.setInt(8, quantity);
			ps.setDouble(9, total);
			ps.setString(10, id);

			done = (ps.executeUpdate() != -1);
		} catch (Exception ex) {
			System.out.println("ERROR Updating PoVendorItem "
					+ ex.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;
	}



	/**
 * Create new Freight
 * createFreight
 *
 * @param code String;
 * @param name String;
 */
	public void createFreight(String code,String name){

	String CREATE_QUERY = "INSERT INTO IA_FREIGHT "+
                          "(FREIGHT_CODE,NAME,MTID )"+
		                  "VALUES(?,?,?)";
		Connection con = null;
		PreparedStatement ps = null;
		String id = helper.getGeneratedId("IA_FREIGHT");
		try{

			con = getConnection();
			ps = con.prepareStatement(CREATE_QUERY);
			ps.setString(1, code);
			ps.setString(2, name);
			ps.setString(3, id);
			ps.execute();

		}catch(Exception er){
			System.out.println("Error creating Freight... ->"+er.getMessage());
		}finally{
			closeConnection(con,ps);
		}

	}
/**
 * Deletes a Freight
 * deleteFreight
 *
 * @param id String
*/
	public void deleteFreight(String id){
		String DELETE_QUERY = "DELETE FROM IA_FREIGHT  WHERE MTID = ?";
	    Connection con = null;
		PreparedStatement ps = null;
		try{

			con = getConnection();
			ps = con.prepareStatement(DELETE_QUERY);
			ps.setString(1, id);

			ps.execute();


		}catch(Exception er){
			System.out.println("Error Deleting Freight... ->"+er.getMessage());
		}finally{
			closeConnection(con,ps);
		}

	}

	public void updateFreight(String id,String name){

		String UPDATE_QUERY =  "UPDATE IA_FREIGHT "+
                                       "SET FREIGHT_CODE=?,NAME=? "+
                                       "WHERE MTID = ?";
		Connection con = null;
		PreparedStatement ps = null;

		try{

			con = getConnection();
			ps = con.prepareStatement(UPDATE_QUERY);
			ps.setString(1, name);
			ps.setString(2,id);

			ps.execute();

		}catch(Exception er){
			System.out.println("Error UPDATING Freight... ->"+er.getMessage());
		}finally{
			closeConnection(con,ps);
		}

	}
	/**
 * Create new Carrier
 * createCarrier
 *
 * @param code String;
 * @param name String;
 */
	public void createCarrier(String code,String name){

	String CREATE_QUERY = "INSERT INTO IA_CARRIER "+
                          "(CARRIER_CODE,NAME,MTID )"+
		                  "VALUES(?,?,?)";
		Connection con = null;
		PreparedStatement ps = null;
		String id = helper.getGeneratedId("IA_CARRIER");
		try{

			con = getConnection();
			ps = con.prepareStatement(CREATE_QUERY);
			ps.setString(1, code);
			ps.setString(2, name);
			ps.setString(3, id);
			ps.execute();

		}catch(Exception er){
			System.out.println("Error creating Carrier... ->"+er.getMessage());
		}finally{
			closeConnection(con,ps);
		}

	}

/**
 * Deletes a Carrier
 * deleteCarrier
 *
 * @param id String
*/
	public void deleteCarrier(String id){
		String DELETE_QUERY = "DELETE FROM IA_CARRIER  WHERE MTID = ?";
	    Connection con = null;
		PreparedStatement ps = null;
		try{

			con = getConnection();
			ps = con.prepareStatement(DELETE_QUERY);
			ps.setString(1, id);

			ps.execute();


		}catch(Exception er){
			System.out.println("Error Deleting Carrier... ->"+er.getMessage());
		}finally{
			closeConnection(con,ps);
		}

	}

	public void updateCarrier(String code,String name,String id){

		String UPDATE_QUERY =  "UPDATE FROM IA_CARRIER "+
                                       "SET CARRIER_CODE=?,NAME=? "+
                                       "WHERE MTID = ?";
		Connection con = null;
		PreparedStatement ps = null;

		try{

			con = getConnection();
			ps = con.prepareStatement(UPDATE_QUERY);
			ps.setString(1, code);
			ps.setString(2, name);
			ps.setString(3, id);
			ps.execute();

		}catch(Exception er){
			System.out.println("Error UPDATING Carrier... ->"+er.getMessage());
		}finally{
			closeConnection(con,ps);
		}

	}


public java.util.ArrayList<PurchaseOrder> findPurchaseOrdersByQuery(String filter2,String filter) 
	{
		java.util.ArrayList<PurchaseOrder> poList = new java.util.ArrayList<PurchaseOrder>();
		String criteria = " WHERE COMP_CODE = '"+filter2+"' AND MTID IS NOT NULL " + filter;
		poList = findPurchaseOrders(criteria);
		return poList;
	}

	public PurchaseOrder findPurchaseOrdersByOrderNo(String filter) {
		PurchaseOrder porder = new PurchaseOrder();
		String criteria = " WHERE  PORDER_CODE ='" + filter+"'"; // Where OrderNo = filter
		porder = (PurchaseOrder)findPurchaseOrders(criteria).get(0);
		return porder;
	}

	public PurchaseOrder findPurchaseOrdersByID(String orderCode) {
		PurchaseOrder porder = new PurchaseOrder();
		String criteria = " WHERE PORDER_CODE ='" + orderCode +"'"; // Where id = id
		porder = (PurchaseOrder)findPurchaseOrders(criteria).get(0);
		return porder;
	}

	public java.util.ArrayList<PoVendorItem> findPoVendorItemsByQuery(
			String filter) {
		java.util.ArrayList<PoVendorItem> poList = new java.util.ArrayList<PoVendorItem>();
		String criteria = "  WHERE MTID IS NOT NULL  " + filter;
		poList = findPoVendorItems(criteria);
		return poList;
	}

	public PoVendorItem findPoVendorItemsByItemCode(String filter) {
		PoVendorItem porder = new PoVendorItem();
		String criteria = " WHERE ITEM_CODE =' " + filter+"'"; // Where OrderNo = filter
		porder = (PoVendorItem)findPoVendorItems(criteria).get(0);
		return porder;
	}

	public PoVendorItem findPoVendorItemByID(String id) {
		PoVendorItem porder = new PoVendorItem();
		String criteria = " WHERE MTID ='" + id +"'"; // Where id = id
		porder = (PoVendorItem)findPoVendorItems(criteria).get(0);
		return porder;
	}

	private java.util.ArrayList<PurchaseOrder> findPurchaseOrders(String filter) {
			java.util.ArrayList<PurchaseOrder> poList = new java.util.ArrayList<PurchaseOrder>();

			PurchaseOrder porder = null;

			Connection con = null;
			PreparedStatement ps = null;
			ResultSet rs = null;

			String query = "SELECT MTID,PORDER_CODE,VENDOR_CODE,TOTAL_AMOUNT,TRANS_DATE,DATE_RECEIVED,"+
                                       "ORDERBY,WAREHOUSE_CODE,COMMENT,CARRIER_CODE,"+
					"FREIGHT_CODE,QUANTITY,ITEM_CODE,UNITCOST,"+
					"AMOUNT,ADVANCE_PYMT,QUANT_RECEIVED,QUANTBO,"+
					"COMPLETED,DESCRIPTION,PROJECT_CODE FROM IA_PURCHASE_ORDER ";
			query += filter;
//			System.out.println("query in findPurchaseOrders: "+query);
			try {
				con = getConnection();
				ps = con.prepareStatement(query);

				rs = ps.executeQuery();

				while (rs.next()) {
					String orderNo = rs.getString("PORDER_CODE");
					String vendorCode = rs.getString("VENDOR_CODE");
					String printed = "";//rs.getString("PRINTED");
					String status = "";//rs.getString("STATUS");
					double amount = rs.getDouble("TOTAL_AMOUNT");
					String date = sdf.format(rs.getDate("TRANS_DATE"));
					String dateReceived = sdf.format(rs.getDate("DATE_RECEIVED"));
					String orderedBy = rs.getString("ORDERBY");
					String warehouse = rs.getString("WAREHOUSE_CODE");
					String comment = rs.getString("COMMENT");
					String carrier = rs.getString("CARRIER_CODE");
					String freight = rs.getString("FREIGHT_CODE");
					int quantity = rs.getInt("QUANTITY");
					String vendorItemNo = rs.getString("ITEM_CODE");
					String description = rs.getString("DESCRIPTION");
					double unitCost = rs.getDouble("UNITCOST");
					double amount_paid = rs.getDouble("AMOUNT");
					String advancePymtOpt = rs.getString("ADVANCE_PYMT");
					int quantityReceived = rs.getInt("QUANT_RECEIVED");
					int quantityBO = rs.getInt("QUANTBO");
					String completed = "";//rs.getString("");
					String id = rs.getString("MTID");
                                        String projectCode = rs.getString("PROJECT_CODE");
					porder = new PurchaseOrder(id, orderNo, vendorCode,printed,
								status, amount, date,dateReceived,
								orderedBy, warehouse, comment, carrier,
								freight, quantity, vendorItemNo,
								description, unitCost, amount_paid,
								advancePymtOpt, quantityReceived,
								quantityBO, completed,projectCode);

					poList.add(porder);

				}

			} catch (Exception ex) {
				System.out.println("ERROR fetching Purchase Order "
						+ ex.getMessage());
			} finally {
				closeConnection(con, ps,rs);
			}

			return poList;
		}

		private java.util.ArrayList<PoVendorItem> findPoVendorItems(String filter) {
			java.util.ArrayList<PoVendorItem> piList = new java.util.ArrayList<PoVendorItem>();

			PoVendorItem povitem = null;

			Connection con = null;
			PreparedStatement ps = null;
			ResultSet rs = null;

			String query = "SELECT MTID,ITEM_CODE,VENDOR_CODE,PREFERENCE,"+
							"STATUS,DESCRIPTION,COST,TRANS_DATE,QUANTITY,"+
							"TOTAL_AMOUNT FROM IA_VENDOR_ITEMS ";
			query += filter;
			try {
				con = getConnection();
				ps = con.prepareStatement(query);

				rs = ps.executeQuery();

				while (rs.next()) {

					String itemCode = rs.getString("ITEM_CODE");
					String vendorCode = rs.getString("VENDOR_CODE");
					String preference = rs.getString("PREFERENCE");
					String status = rs.getString("STATUS");
					String description = rs.getString("DESCRIPTION");
					double cost = rs.getDouble("COST");
					String date = sdf.format(rs.getDate("TRANS_DATE"));
					int quantity = rs.getInt("QUANTITY");
					double total = rs.getDouble("TOTAL_AMOUNT");
					String id = rs.getString("MTID");
					povitem = new PoVendorItem(id,itemCode, vendorCode, preference,
							status, description, cost, date, quantity, total);

					piList.add(povitem);

				}

			} catch (Exception ex) {
				System.out.println("ERROR Creating fetching PoVendorItem "
						+ ex.getMessage());
				ex.printStackTrace();
			} finally {
				closeConnection(con, ps,rs);
			}

			return piList;
	}

	
	public ArrayList findAllCarrier(){

		ArrayList records = new ArrayList();
		String SELECT_QUERY = "SELECT MTID,CARRIER_CODE,NAME "+
							   "FROM IA_CARRIER";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try{

			con = getConnection();
			ps = con.prepareStatement(SELECT_QUERY);

			rs = ps.executeQuery();

			while(rs.next()){

				String code = rs.getString("CARRIER_CODE");
				String name = rs.getString("NAME");
				String id = rs.getString("MTID");

				Carrier carrier = new Carrier(id,code,name);

				records.add(carrier);
			}

		}catch(Exception er){
			System.out.println("Error finding All Carrier...->"+er.getMessage());
		}finally{
			closeConnection(con,ps,rs);
		}

		return records ;
	}

	public Carrier findCarrierById(String id){

		String FIND_QUERY = "SELECT MTID,CARRIER_CODE,NAME "+
							"FROM IA_CARRIER WHERE  MTID=?";

	    Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Carrier carrier = null;

		try{
			con = getConnection();
			ps = con.prepareStatement(FIND_QUERY);

			ps.setString(1, id);

			rs = ps.executeQuery();

			while(rs.next()){

				String code = rs.getString("CARRIER_CODE");
				String name = rs.getString("NAME");
				String id2 = rs.getString("MTID");

				carrier = new Carrier(id2,code,name);

            }

		}catch(Exception er){
			System.out.println("Error finding CarrierByID ->"+er);
		}finally{
			closeConnection(con,ps,rs);
		}


		return carrier;
	}


	public boolean isCarrierExisting(String id){

		
		boolean exists = false;
		String updateQuery = "SELECT count(MTID) FROM IA_CARRIER "+
							  "WHERE  MTID = ?";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try{

			con = getConnection();
			ps = con.prepareStatement(updateQuery);

			ps.setString(1, id);
			rs = ps.executeQuery();

			while(rs.next()){
				int counted = rs.getInt(1);
				if(counted > 0){
				exists = true;
			 }
			}

		}catch(Exception er){
			System.out.println("Error in isCarrierExisting()... ->"+er);
		}finally{
			closeConnection(con,ps);
		}

		return exists;
	}

	public ArrayList findAllFreight(){

		ArrayList records = new ArrayList();
		String SELECT_QUERY = "SELECT MTID,FREIGHT_CODE,NAME "+
							   "FROM IA_FREIGHT";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try{

			con = getConnection();
			ps = con.prepareStatement(SELECT_QUERY);

			rs = ps.executeQuery();

			while(rs.next()){

				String code = rs.getString("FREIGHT_CODE");
				String name = rs.getString("NAME");
				String id = rs.getString("MTID");

				
				Freight freight = new Freight(id,code,name);

				records.add(freight);
			}

		}catch(Exception er){
			System.out.println("Error finding All Freight...->"+er.getMessage());
		}finally{
			closeConnection(con,ps,rs);
		}

		return records ;
	}

	public Freight findFreightById(String id){

		String FIND_QUERY = "SELECT FREIGHTCODE,NAME "+
							"FROM IA_FREIGHT WHERE  ID=?";

	    Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Freight freight = null;

		try{
			con = getConnection();
			ps = con.prepareStatement(FIND_QUERY);

			ps.setString(1, id);

			rs = ps.executeQuery();

			while(rs.next()){

				String code = rs.getString("FREIGHTCODE");
				String name = rs.getString("NAME");
				String ids = rs.getString("MTID");
				freight = new Freight(ids,code,name);

            }

		}catch(Exception er){
			System.out.println("Error finding FreightByID ->"+er);
		}finally{
			closeConnection(con,ps,rs);
		}


		return freight;
	}


	public boolean isFreightExisting(String id){

		
		boolean exists = false;
		String updateQuery = "SELECT count(MTID) FROM IA_FREIGHT "+
							  "WHERE  MTID = ?";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try{

			con = getConnection();
			ps = con.prepareStatement(updateQuery);

			ps.setString(1, id);
			rs = ps.executeQuery();

			while(rs.next()){
				int counted = rs.getInt(1);
				if(counted > 0){
				exists = true;
			 }
			}

		}catch(Exception er){
			System.out.println("Error in isFreightExisting()... ->"+er);
		}finally{
			closeConnection(con,ps);
		}

		return exists;
	}

	public boolean createCheque(String type, String chequeno,String currency,
			String vendorCode, String transDate, String chequeDate,
			String period) {
		String query = "INSERT INTO IA_CHEQUES(MTID, CHEQUENO, TYPE, "
				+ "CURRENCY, VENDOR_CODE, TRANS_DATE, CHEQUE_DATE, PERIOD)"
				+ " VALUES(?,?,?,?,?,?,?,?)";

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String id = helper.getGeneratedId("IA_MANDATORY_FIELD");

		try {
			ps = con.prepareStatement(query);
			ps.setString(1, id);
			ps.setString(2, type);
			ps.setString(3, chequeno);
			ps.setString(4, currency);
			ps.setString(5, vendorCode);
			ps.setDate(6, dateConvert(transDate));
			ps.setDate(7, dateConvert(chequeDate));
			ps.setString(8, period);

			done = (ps.executeUpdate() != -1);
		} catch (Exception ex) {
			System.out.println("ERROR Creating Cheque " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			closeConnection(con, ps);
		}
		return done;
	}

	public boolean updateCheque(String id, String type, String currency,
			String vendorCode, String transDate, String chequeDate,
			String period) {
		String query = "UPDATE IA_CHEQUES " + "SET  TYPE=?, CURRENCY=?, "
				+ "VENDOR_CODE=?,TRANS_DATE=?, CHEQUE_DATE=?, PERIOD=?"
				+ "WHERE MTID=?";

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;

		try {
			ps = con.prepareStatement(query);
			ps.setString(1, type);
			ps.setString(2, currency);
			ps.setString(3, vendorCode);
			ps.setDate(4, df.dateConvert(transDate));
			ps.setDate(5, df.dateConvert(chequeDate));
			ps.setString(6, period);
			ps.setString(7, id);

			done = (ps.executeUpdate() != -1);
		} catch (Exception ex) {
			System.out.println("ERROR Updating Cheque " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			closeConnection(con, ps);
		}
		return done;
	}

	public void deleteCheque(String id) {

		String Query = "DELETE FROM IA_CHEQUES WHERE WHERE MTID=?";
		Connection con = null;
		PreparedStatement ps = null;
		try {

			con = getConnection();
			ps = con.prepareStatement(Query);
			ps.setString(1, id);

			ps.execute();

		} catch (Exception er) {
			System.out.println("Error Deleting Cheque... ->" + er.getMessage());
		} finally {
			closeConnection(con, ps);
		}

	}

	public ArrayList findCheque() {

		ArrayList records = new ArrayList();
		String query = "SELECT MTID, CHEQUENO, TYPE, "
				+ "CURRENCY, VENDOR_CODE, TRANS_DATE,"
				+ "CHEQUE_DATE, PERIOD, USERID" + " FROM IA_CHEQUES";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = getConnection();
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {

				String id = rs.getString("MTID");
				String chequeno = rs.getString("CHEQUENO");
				String type = rs.getString("Type");
				String currency = rs.getString("Currency");
				String vendorCode = rs.getString("VendorCode");
				String transDate = rs.getString("TransDate");
				String chequeDate = rs.getString("ChequeDate");
				String period = rs.getString("Period");
				Cheque ia = new Cheque(id, type, currency, vendorCode,
						transDate, chequeDate, period, chequeno);
				records.add(ia);

			}

		} catch (Exception er) {
			System.out.println("Error finding All Cheque...->"
					+ er.getMessage());
		} finally {
			closeConnection(con, ps, rs);
		}

		return records;
	}

	public Cheque findChequeById(String id) {

		String query = "SELECT MTID, CHEQUENO, TYPE, "
				+ "CURRENCY, VENDOR_CODE, TRANS_DATE,"
				+ "CHEQUE_DATE, PERIOD, USERID"
				+ " FROM IA_CHEQUES WHERE MTID=?";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Cheque ia = null;

		try {
			con = getConnection();
			ps = con.prepareStatement(query);

			ps.setString(1, id);

			rs = ps.executeQuery();

			while (rs.next()) {
				String iid = rs.getString("MTID");
				String chequeno = rs.getString("CHEQUENO");
				String itype = rs.getString("Type");
				String icurrency = rs.getString("Currency");
				String ivendorCode = rs.getString("VendorCode");
				String itransDate = rs.getString("TransDate");
				String ichequeDate = rs.getString("ChequeDate");
				String iperiod = rs.getString("Period");
				ia = new Cheque(iid, itype, icurrency, ivendorCode, itransDate,
						ichequeDate, iperiod, chequeno);

			}

		} catch (Exception er) {
			System.out.println("Error finding Cheque By ID ->" + er);
		} finally {
			closeConnection(con, ps, rs);
		}

		return ia;
	}

	public Cheque findChequeByNo(String chequeno) {

		String query = "SELECT MTID, CHEQUENO, TYPE, "
				+ "CURRENCY, VENDOR_CODE, TRANS_DATE,"
				+ "CHEQUE_DATE, PERIOD, USERID"
				+ " FROM IA_CHEQUES WHERE CHEQUENO=?";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Cheque ia = null;

		try {
			con = getConnection();
			ps = con.prepareStatement(query);

			ps.setString(1, chequeno);

			rs = ps.executeQuery();

			while (rs.next()) {
				String iid = rs.getString("MTID");
				//String chequeno = rs.getString("CHEQUENO");
				String itype = rs.getString("Type");
				String icurrency = rs.getString("Currency");
				String ivendorCode = rs.getString("VendorCode");
				String itransDate = rs.getString("TransDate");
				String ichequeDate = rs.getString("ChequeDate");
				String iperiod = rs.getString("Period");
				ia = new Cheque(iid, itype, icurrency, ivendorCode, itransDate,
						ichequeDate, iperiod, chequeno);

			}

		} catch (Exception er) {
			System.out.println("Error finding Cheque By ID ->" + er);
		} finally {
			closeConnection(con, ps, rs);
		}

		return ia;
	}

	public boolean isChequeExisting(String id) {

		Cheque ia = null;
		boolean exists = false;
		String Query = "SELECT COUNT(MTID)" + " FROM IA_CHEQUES WHERE MTID=?";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = getConnection();
			ps = con.prepareStatement(Query);

			ps.setString(1, id);
			rs = ps.executeQuery();

			while (rs.next()) {
				int counted = rs.getInt(1);
				if (counted > 0) {
					exists = true;
				}
			}

		} catch (Exception er) {
			System.out.println("Error Cheque Already Existing()... ->" + er);
		} finally {
			closeConnection(con, ps);
		}

		return exists;
	}

    /**
    * Create new Invoice
    * createInvoice
    *
    * @param invoiceCode String;
    * @param invoiceNo String;
    * @param PO String;
    * @param description String;
    * @param amount double;
    * @param invoiceDate String;
    * @param period String ;
    * @param dueDate String;
    * @param discount double;
    * @param percentageDiscount double;

    */
     public boolean createPurchaseOrderItemDetail(String orderCode,int quantity, double unitPrice,double advancePymt,String itemCode,
                                                  double advancePymtPerc,double discount,double discountPerc,String companyCode){

     String CREATE_QUERY = "INSERT INTO IA_PO_ITEM "+
                       "(PORDER_CODE,QUANTITY,UNITPRICE,AMOUNT,MTID,ADVANCE_PYMT,ITEM_CODE,ADVANCE_PYMT_PERC,DISCOUNT,DISCOUNT_PERC,COMP_CODE) "+
                               " VALUES (?,?,?,?,?,?,?,?,?,?,?)";
         
                
             Connection con = null;
             PreparedStatement ps = null;
             id = helper.getGeneratedId("IA_PO_ITEM");
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
                     ps.setDouble(8,advancePymtPerc);
                     ps.setDouble(9,discount);
                     ps.setDouble(10,discountPerc);
					 ps.setString(11,companyCode);
                     
                 done = (ps.executeUpdate() != -1);

             }catch(Exception er){
                     done = false;
                     System.out.println("Error creating PurchaseOrderItemDetail... ->"+er.getMessage());
             }finally{
                     closeConnection(con,ps);
             }
           return done;
     }
     
    //this method is used to create new delivery (receipt)
    public boolean createPODeliveryItem(String orderCode,String batchCode,String[] id,String[] itemCode,
                                        String[] quantity,String[] quantDeliver,int userId,String[] unitPrice,String[] amount,String companyCode){
        //ArrayList list = new ArrayList();
         String CREATE_QUERY = "INSERT INTO IA_PO_DELIVERY_ITEM "+
                          "(PORDER_CODE,BATCH_CODE,MTID,ITEM_CODE,QUANTITY_DELIVER,QUANTITY_REMAIN,USERID,QUANTITY,UNITPRICE,AMOUNT,TRANS_DATE,COMP_CODE) " +
                          "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
                      
            Connection con = null;
            PreparedStatement ps = null;
            //id = helper.getGeneratedId("IA_PO_DELIVERY_ITEM");
            boolean done = false;
            //list = findAllPurchaseOrderItemDetail(orderCode);
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
					 ps.setString(12,companyCode);
                     ps.addBatch();
                     }
                    }
                done = (ps.executeBatch().length != -1);

            }catch(Exception er){
                    System.out.println("Error creating PODeliveryItem()... ->"+er.getMessage());
            }finally{
                    closeConnection(con,ps);
            }
         return done;
    }
    //for delivery update
     public boolean updatePODeliveryItem(String orderCode,String batchCode,String[] id,
                                         String[] quantity,String[] quantDeliver){
         //ArrayList list = new ArrayList();
          String UPDATE_QUERY =  "UPDATE IA_PO_DELIVERY_ITEM SET QUANTITY_DELIVER=?,QUANTITY_REMAIN=? WHERE PORDER_CODE=? AND BATCH_CODE=? AND MTID=?";
          
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
                     System.out.println("Error updating PODeliveryItem()... ->"+er.getMessage());
                     er.printStackTrace();
             }finally{
                     closeConnection(con,ps);
             }
         
         return done;
     }
    
     public void deletePurchaseOrderItemDetail(String id){
             String DELETE_QUERY = "DELETE FROM IA_PO_ITEM  WHERE MTID = ?";
         Connection con = null;
             PreparedStatement ps = null;
             try{

                     con = getConnection();
                     ps = con.prepareStatement(DELETE_QUERY);
                     ps.setString(1, id);

                     ps.execute();


             }catch(Exception er){
                     System.out.println("Error Deleting PurchaseOrderItemDetail... ->"+er.getMessage());
             }finally{
                     closeConnection(con,ps);
             }

     }

     public boolean updatePurchaseOrderItemDetail(String id,String orderCode,int quantity, double unitPrice,
                                                  double advancePymt,String itemCode,double advancePymtPerc,
                                                  double discount,double discountPerc){

             String UPDATE_QUERY =  "UPDATE IA_PO_ITEM SET QUANTITY=?,"+
                                    "UNITPRICE=?,AMOUNT=?,ADVANCE_PYMT=?,ADVANCE_PYMT_PERC=?,DISCOUNT=?,DISCOUNT_PERC=? WHERE ITEM_CODE=? AND PORDER_CODE=?";
             Connection con = null;
             PreparedStatement ps = null;
             boolean done = false;

             try{
                     con = getConnection();
                     ps = con.prepareStatement(UPDATE_QUERY);
                     ps.setInt(1,quantity);
                     ps.setDouble(2,unitPrice);
                    ps.setDouble(3,(quantity*unitPrice));
                    ps.setDouble(4,advancePymt);
                    ps.setDouble(5,advancePymtPerc);
                    ps.setDouble(6,discount);
                    ps.setDouble(7,discountPerc);
                    ps.setString(8,itemCode);
                    ps.setString(9,orderCode);
                     
                 done = (ps.executeUpdate() != -1);

             }catch(Exception er){
                     System.out.println("Error UPDATING PurchaseOrderItemDetail... ->"+er.getMessage());
             }finally{
                     closeConnection(con,ps);
             }
     return done;
     }
    
    //for Delivery Update 
    public boolean updatePurchaseOrderItemDetail(String id,String orderCode,int quantity, int quantDeliver){

            String UPDATE_QUERY =  "UPDATE IA_PO_ITEM SET QUANTITY_DELIVER=?,QUANTITY_REMAIN=? WHERE MTID=? AND PORDER_CODE=?";
            Connection con = null;
            PreparedStatement ps = null;
            boolean done = false;

            try{
                    con = getConnection();
                    ps = con.prepareStatement(UPDATE_QUERY);
                    ps.setInt(1,quantDeliver);
                    ps.setInt(2,quantity-quantDeliver);
                    ps.setString(3,id);
                    ps.setString(4,orderCode);
                    
                done = (ps.executeUpdate() != -1);

            }catch(Exception er){
                    System.out.println("Error UPDATING PurchaseOrderItemDetail(????)... ->"+er.getMessage());
            }finally{
                    closeConnection(con,ps);
            }
    return done;
    }
    
     public ArrayList findAllPurchaseOrderItemDetail(String orderCode){

             ArrayList records = new ArrayList();
             String SELECT_QUERY = "SELECT MTID,PORDER_CODE,QUANTITY,AMOUNT,UNITPRICE,ADVANCE_PYMT,ITEM_CODE, "+
                                   "QUANTITY_DELIVER,QUANTITY_REMAIN,ADVANCE_PYMT_PERC,DISCOUNT,DISCOUNT_PERC FROM IA_PO_ITEM WHERE PORDER_CODE =?";
                                   //
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
                         double advancePymtPerc = rs.getDouble("ADVANCE_PYMT_PERC");
                         double discount = rs.getDouble("DISCOUNT");
                         double discountPerc = rs.getDouble("DISCOUNT_PERC");
                              PurchaseOrderItem invoice = new PurchaseOrderItem(id,orderCode,quantity,unitPrice,amount,advancePymt,
                                                          itemCode,quantDeliver,quantRemain,advancePymtPerc,
                                                          discount,discountPerc);
                             records.add(invoice);
                     }

             }catch(Exception er){
                     System.out.println("Error finding All findAllPurchaseOrderItemDetail...->"+er.getMessage());
             }finally{
                     closeConnection(con,ps,rs);
             }

             return records ;
     }
    public ArrayList findAllPurchaseOrderItemDetailByQuery(String query){

            ArrayList records = new ArrayList();
            String SELECT_QUERY = "SELECT MTID,PORDER_CODE,QUANTITY,AMOUNT,UNITPRICE,ADVANCE_PYMT,ITEM_CODE, "+
                                  "QUANTITY_DELIVER,QUANTITY_REMAIN,ADVANCE_PYMT_PERC,DISCOUNT,DISCOUNT_PERC FROM IA_PO_ITEM WHERE MTID IS NOT NULL ";
                                  
            SELECT_QUERY = SELECT_QUERY + query;
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try{
                    con = getConnection();
                    ps = con.prepareStatement(SELECT_QUERY);
                    rs = ps.executeQuery();
                    while(rs.next()){
                            String orderCode = rs.getString("PORDER_CODE");
                            int quantity = rs.getInt("QUANTITY");
                            double amount = rs.getDouble("AMOUNT");
                            String id = rs.getString("MTID");
                            double unitPrice = rs.getDouble("UNITPRICE");
                            double advancePymt = rs.getDouble("ADVANCE_PYMT");
                            String itemCode = rs.getString("ITEM_CODE");
                            int quantDeliver = rs.getInt("QUANTITY_DELIVER");
                            int quantRemain = rs.getInt("QUANTITY_REMAIN");
                        double advancePymtPerc = rs.getDouble("ADVANCE_PYMT_PERC");
                        double discount = rs.getDouble("DISCOUNT");
                        double discountPerc = rs.getDouble("DISCOUNT_PERC");
                             PurchaseOrderItem invoice = new PurchaseOrderItem(id,orderCode,quantity,unitPrice,amount,
                                                             advancePymt,itemCode,quantDeliver,quantRemain,advancePymtPerc,
                                                             discount,discountPerc);
                            records.add(invoice);
                    }

            }catch(Exception er){
                    System.out.println("Error finding All findAllPurchaseOrderItemDetailByQuery...->"+er.getMessage());
            }finally{
                    closeConnection(con,ps,rs);
            }

            return records ;
    } 
     
    public PurchaseOrderItem findPurchaseOrderItemDetailById(String orderCode,String itemCode){
           
        String FIND_QUERY = "SELECT MTID,PORDER_CODE,QUANTITY,AMOUNT,UNITPRICE,ADVANCE_PYMT,ITEM_CODE,QUANTITY_DELIVER,QUANTITY_REMAIN,ADVANCE_PYMT_PERC,DISCOUNT,DISCOUNT_PERC "+
                            "FROM IA_PO_ITEM WHERE PORDER_CODE =? AND ITEM_CODE=? ";

        Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            PurchaseOrderItem orderItem = null;

            try{
                    con = getConnection();
                    ps = con.prepareStatement(FIND_QUERY);
                    ps.setString(1,orderCode);
                    ps.setString(2,itemCode);
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
            double advancePymtPerc = rs.getDouble("ADVANCE_PYMT_PERC");
            double discount = rs.getDouble("DISCOUNT");
            double discountPerc = rs.getDouble("DISCOUNT_PERC");
              orderItem = new PurchaseOrderItem(id,orderCode,quantity,unitPrice,amount,advancePymt,
                                                itemCode,quantDeliver,quantRemain,advancePymtPerc,
                                                discount,discountPerc);
              
            
        }

            }catch(Exception er){
                    System.out.println("Error finding findPurchaseOrderItemDetailById ->"+er);
            }finally{
                    closeConnection(con,ps,rs);
            }
         return orderItem;
    }
    public PurchaseOrderDeliveryItem findPODeliveryItemDetailById(String orderCode,String batchCode,String id){
           
        String FIND_QUERY = "SELECT MTID,PORDER_CODE,QUANTITY,AMOUNT,UNITPRICE,ADVANCE_PYMT,ITEM_CODE,QUANTITY_DELIVER,QUANTITY_REMAIN,USERID "+ 
                            "FROM IA_PO_DELIVERY_ITEM WHERE PORDER_CODE =? AND BATCH_CODE=? AND MTID=? ";

        Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            PurchaseOrderDeliveryItem orderItem = null;

            try{
                    con = getConnection();
                    ps = con.prepareStatement(FIND_QUERY);
                    ps.setString(1,orderCode);
                    ps.setString(2,batchCode);
                    ps.setString(3,id);
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
            String transDate = formatDate(rs.getDate("TRANS_DATE"));
            int userId = rs.getInt("USERID");
            String vendorCode = "";
              orderItem = new PurchaseOrderDeliveryItem(id,orderCode,quantity,unitPrice,amount,
              advancePymt,itemCode,quantDeliver,quantRemain,batchCode,transDate,userId,vendorCode);
         }

            }catch(Exception er){
                    System.out.println("Error finding findPODeliveryItemDetailById ->"+er);
            }finally{
                    closeConnection(con,ps,rs);
            }
         return orderItem;
    }
    public ArrayList findAllPODeliveryItemList(String orderCode,String batchCode){

            ArrayList records = new ArrayList();
            String SELECT_QUERY = "SELECT MTID,PORDER_CODE,QUANTITY,AMOUNT,UNITPRICE,ADVANCE_PYMT,ITEM_CODE, "+
                                  "QUANTITY_DELIVER,QUANTITY_REMAIN,BATCH_CODE,TRANS_DATE,USERID FROM IA_PO_DELIVERY_ITEM "+
                                  "WHERE PORDER_CODE =? AND BATCH_CODE=?";
                                  
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
                        String vendorCode = "";
                             PurchaseOrderDeliveryItem invoice = new PurchaseOrderDeliveryItem(id,orderCode,quantity,
                             unitPrice,amount,advancePymt,itemCode,quantDeliver,quantRemain,batchCode,transDate,userId,vendorCode);
                            records.add(invoice);
                    }

            }catch(Exception er){
                    System.out.println("Error finding All findAllPODeliveryItemList...->"+er.getMessage());
            }finally{
                    closeConnection(con,ps,rs);
            }

            return records ;
    }
    //for receipt printing
     public ArrayList findAllPODeliveryItemList(String orderCode,String batchCode,String filter){

             ArrayList records = new ArrayList();
            /* String para = "";
             for(int i=0; i<printChk.length; i++){
              para = para + ",'"+printChk[i]+"'";
                 
             }*/
             
             String SELECT_QUERY = "SELECT MTID,PORDER_CODE,QUANTITY,AMOUNT,UNITPRICE,ADVANCE_PYMT,ITEM_CODE, "+
                                   "QUANTITY_DELIVER,QUANTITY_REMAIN,BATCH_CODE,TRANS_DATE,USERID "+
                                   "FROM IA_PO_DELIVERY_ITEM WHERE PORDER_CODE =? AND BATCH_CODE=? "+filter;// AND MTID IN ("+para.substring(1)+")";
                                   
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
                             String vendorCode = "";
                              PurchaseOrderDeliveryItem invoice = new PurchaseOrderDeliveryItem(id,orderCode,quantity,unitPrice,amount,advancePymt,
                                                                  itemCode,quantDeliver,quantRemain,batchCode,transDate,userId,vendorCode);
                             records.add(invoice);
                     }
                
             }catch(Exception er){
                     System.out.println("Error finding All findAllPODeliveryItemList(???)...->"+er.getMessage());
             }finally{
                     closeConnection(con,ps,rs);
             }

             return records ;
     }
    
   /* public boolean isDeliveryBatchExisting(String batchCode){

             boolean exists = false;
             String updateQuery = "SELECT COUNT(BATCH_CODE) FROM IA_PO_DELIVERY_ITEM WHERE BATCH_CODE = ?";
             Connection con = null;
             PreparedStatement ps = null;
             ResultSet rs = null;

             try{

                     con = getConnection();
                     ps = con.prepareStatement(updateQuery);

                     ps.setString(1,batchCode);
                     rs = ps.executeQuery();

                     while(rs.next()){
                             int counted = rs.getInt(1);
                             if(counted > 0){
                             exists = true;
                      }
                     }

             }catch(Exception er){
                     System.out.println("Error in isDeliveryBatchExisting()... ->"+er);
             }finally{
                     closeConnection(con,ps);
             }

             return exists;
     }
     */
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

   public boolean isInvoiceExisting(String id){

            boolean exists = false;
            String updateQuery = "SELECT count(MTID) FROM IA_AP_INVOICES "+
                                                      "WHERE  MTID = ?";
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try{

                    con = getConnection();
                    ps = con.prepareStatement(updateQuery);

                    ps.setString(1, id);
                    rs = ps.executeQuery();

                    while(rs.next()){
                            int counted = rs.getInt(1);
                            if(counted > 0){
                            exists = true;
                     }
                    }

            }catch(Exception er){
                    System.out.println("Error in isAPInvoiceExisting()... ->"+er);
            }finally{
                    closeConnection(con,ps);
            }

            return exists;
    }

 public double getPurchaseOrderTotalAmount(String orderCode){
    
        String query = "";
        double result = 0;
        query = "SELECT SUM(QUANTITY*UNITPRICE) FROM IA_PO_ITEM WHERE PORDER_CODE=?";
    
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
                System.out.println("Error in getPurchaseOrderTotalAmount()... ->"+er);
        }finally{
                closeConnection(con,ps);
        }   
    
        return result;
    }
    
public double getPurchaseOrderTotalAdvancePymt(String orderCode){
  
      String query = "";
      double result = 0;
  
      query = "SELECT SUM(ADVANCE_PYMT) FROM IA_PO_ITEM WHERE PORDER_CODE=?";
       
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
              System.out.println("Error in getPurchaseOrderTotalAdvancePymt()... ->"+er);
      }finally{
              closeConnection(con,ps);
      }   
  
      return result;
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
     
    /*
     * Delivery Methods
     * 
     * 
     * 
     * */
     
     public boolean createPurchaseOrderDelivery(String batchCode,String desc,int userId){

     String CREATE_QUERY = "INSERT INTO IA_PURCHASE_ORDER_DELIVERY "+
                           "(BATCH_CODE,DESCRIPTION,TRANS_DATE,MTID,USERID) "+
                               " VALUES (?,?,?,?,?)";
                         
             Connection con = null;
             PreparedStatement ps = null;
             id = helper.getGeneratedId("IA_PURCHASE_ORDER_DELIVERY");
             boolean done = false;
             try{
                     con = getConnection();
                     ps = con.prepareStatement(CREATE_QUERY);
                     ps.setString(1,batchCode);
                     ps.setString(2,desc);
                     ps.setDate(3,dateConvert(new java.util.Date()));
                     ps.setString(4,id);
                     ps.setInt(5,userId);
                                         
                 done = (ps.executeUpdate() != -1);

             }catch(Exception er){
                     System.out.println("Error creating PurchaseOrderDelivery... ->"+er.getMessage());
             }finally{
                     closeConnection(con,ps);
             }
           return done;
     }
     
     public void deletePurchaseOrderDelivery(String id){
             String DELETE_QUERY = "DELETE FROM IA_PURCHASE_ORDER_DELIVERY  WHERE MTID = ?";
         Connection con = null;
             PreparedStatement ps = null;
             try{

                     con = getConnection();
                     ps = con.prepareStatement(DELETE_QUERY);
                     ps.setString(1, id);

                     ps.execute();


             }catch(Exception er){
                     System.out.println("Error Deleting PurchaseOrderDelivery... ->"+er.getMessage());
             }finally{
                     closeConnection(con,ps);
             }

     }

     public boolean updatePurchaseOrderDelivery(String batchCode,String desc){

             String UPDATE_QUERY =  "UPDATE IA_PURCHASE_ORDER_DELIVERY SET DESCRIPTION=?,"+
                                    "TRANS_DATE=? WHERE BATCH_CODE=?";
             Connection con = null;
             PreparedStatement ps = null;
             boolean done = false;

             try{
                     con = getConnection();
                     ps = con.prepareStatement(UPDATE_QUERY);
                     ps.setString(1,desc);
                     ps.setDate(2,dateConvert(new java.util.Date()));
                     ps.setString(3,batchCode);
                     
                 done = (ps.executeUpdate() != -1);

             }catch(Exception er){
                     System.out.println("Error UPDATING PurchaseOrderDelivery... ->"+er.getMessage());
             }finally{
                     closeConnection(con,ps);
             }
     return done;
     }
     
     public ArrayList findPurchaseOrderDeliveryByQuery(String filter2,String filter){

             ArrayList records = new ArrayList();
             String SELECT_QUERY = "SELECT DISTINCT(a.BATCH_CODE),a.PORDER_CODE,a.TRANS_DATE,b.VENDOR_CODE "+
                                   "FROM IA_PO_DELIVERY_ITEM a,IA_PURCHASE_ORDER b "+
                                   "WHERE a.MTID IS NOT NULL AND a.PORDER_CODE = b.PORDER_CODE AND a.COMP_CODE = '"+filter2+"' "+filter;
                                   
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
                        String orderCode = rs.getString("PORDER_CODE");
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
                        String vendorCode = rs.getString("VENDOR_CODE");
                        PurchaseOrderDeliveryItem invoice = new PurchaseOrderDeliveryItem(id,orderCode,quantity,unitPrice,amount,
                                                                advancePymt,itemCode,quantDeliver,quantRemain,batchCode,transDate,userId,vendorCode);
                        records.add(invoice);
                     }

             }catch(Exception er){
                     System.out.println("Error finding All findPurchaseOrderDeliveryByQuery...->"+er.getMessage());
             }finally{
                     closeConnection(con,ps,rs);
             }
           return records ;
     }
     
     public PurchaseOrderDelivery findPurchaseOrderDeliveryById(String batchCode){
           
        String FIND_QUERY = "SELECT MTID,BATCH_CODE,DESCRIPTION,TRANS_DATE FROM IA_PURCHASE_ORDER_DELIVERY WHERE BATCH_CODE =?";

        Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            PurchaseOrderDelivery orderItem = null;

            try{
                    con = getConnection();
                    ps = con.prepareStatement(FIND_QUERY);
                    ps.setString(1,batchCode);
                    rs = ps.executeQuery();
                    while(rs.next()){

             String id = rs.getString("MTID");
             //String batchCode = rs.getString("BATCH_CODE");
             String desc = rs.getString("DESCRIPTION");
             String transDate = formatDate(rs.getDate("TRANS_DATE"));
              orderItem = new PurchaseOrderDelivery(id,batchCode,desc,transDate);
              
            
        }

            }catch(Exception er){
                    System.out.println("Error finding findPurchaseOrderDeliveryById ->"+er);
            }finally{
                    closeConnection(con,ps,rs);
            }
         return orderItem;
     }
     
    public boolean isItemExistInInventoryTotals(String itemCode,String warehouseCode){

             boolean exists = false;
             String updateQuery = "SELECT COUNT(*) FROM IA_INVENTORY_TOTALS WHERE ITEM_CODE=? AND WAREHOUSE_CODE=?";
             Connection con = null;
             PreparedStatement ps = null;
             ResultSet rs = null;
             //StringBuffer sb = new StringBuffer("INSERT INTO IA_INVENTORY_TOTALS "+
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
                            // sb = new StringBuffer("UPDATE IA_INVENTORY_TOTALS "+
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
     
     //post Receipt 2 update Inventory Items/History
      public boolean postReceipt2InventoryHistory(String orderCode,String batchCode,String filter){
      
          ArrayList list = new ArrayList();
          String warehouseCode = "";
          String CREATE_QUERY = "INSERT INTO IA_INVENTORY_HISTORY "+
                                "(MTID,ITEM_CODE,TRANS_DESC,QUANTITY,WAREHOUSE_CODE,TRANS_DATE,BATCH_CODE,USERID) " +
                                "VALUES (?,?,?,?,?,?,?,?)";
                                
           String UPDATE_QUERY = "UPDATE IA_PO_DELIVERY_ITEM SET POSTED = ? WHERE BATCH_CODE=?";                     
             
           warehouseCode = getCodeName("SELECT WAREHOUSE_CODE FROM IA_PURCHASE_ORDER WHERE PORDER_CODE = '"+orderCode+"'");  
          boolean autoCommit = false;
              Connection con = null;
              PreparedStatement ps = null;         
              //id = helper.getGeneratedId("IA_PO_DELIVERY_ITEM");
              boolean done = false;
              list = findAllPODeliveryItemList(orderCode,batchCode,filter);
                     
              try{
                      con = getConnection();
                      autoCommit = con.getAutoCommit();
                      con.setAutoCommit(false);
                     //insert 
                      ps = con.prepareStatement(CREATE_QUERY);
                      for(int i=0; i<list.size(); i++)
                      {
                       ps.setString(1,((PurchaseOrderDeliveryItem)list.get(i)).getId());
                       ps.setString(2,((PurchaseOrderDeliveryItem)list.get(i)).getItemCode());
                       ps.setString(3,"PURCHASE ORDER RECEIPT");
                       ps.setInt(4,((PurchaseOrderDeliveryItem)list.get(i)).getQuantDeliver());
                       ps.setString(5,warehouseCode);
                       ps.setDate(6,dateConvert(new java.util.Date()));
                       ps.setString(7,((PurchaseOrderDeliveryItem)list.get(i)).getBatchCode());
                       ps.setInt(8,((PurchaseOrderDeliveryItem)list.get(i)).getUserId());
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
                      System.out.println("Error posting Receipt 2 Inventory History... ->"+er.getMessage());
                      er.printStackTrace();
                      done = false;
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
      
    //post Receipt 2 update Inventory Items/Totals
     public boolean postReceipt2InventoryTotals(String orderCode,String batchCode,String filter){
     
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
             warehouseCode = getCodeName("SELECT WAREHOUSE_CODE FROM IA_PURCHASE_ORDER WHERE PORDER_CODE = '"+orderCode+"'");  
             
             list = findAllPODeliveryItemList(orderCode,batchCode,filter);
             for(int i=0; i<list.size(); i++)
             {
              if(isItemExistInInventoryTotals(((PurchaseOrderDeliveryItem)list.get(i)).getItemCode(),warehouseCode)){
                mtId = ((PurchaseOrderDeliveryItem)list.get(i)).getId();
                itemCode = ((PurchaseOrderDeliveryItem)list.get(i)).getItemCode();
                itemBalance = ((PurchaseOrderDeliveryItem)list.get(i)).getQuantDeliver();
                desc = "";
                //warehouseCode = "";
                userId = ((PurchaseOrderDeliveryItem)list.get(i)).getUserId();
                InventoryTotal uupdate = new InventoryTotal(mtId,itemCode,itemBalance,desc,warehouseCode,userId,0,0);
                uu.add(uupdate); 
                
             }
             else{
                 mtId = ((PurchaseOrderDeliveryItem)list.get(i)).getId();
                 itemCode = ((PurchaseOrderDeliveryItem)list.get(i)).getItemCode();
                 itemBalance = ((PurchaseOrderDeliveryItem)list.get(i)).getQuantDeliver();
                 desc = "";
                 //warehouseCode = "";
                 userId = ((PurchaseOrderDeliveryItem)list.get(i)).getUserId();
                 InventoryTotal iinsert = new InventoryTotal(mtId,itemCode,itemBalance,desc,warehouseCode,userId,0,0); 
                 ii.add(iinsert);
                 
             }
          
            }
            try{
                  con = getConnection();
                   //insert 
                   
                   if(uu.size() > 0){  
                       ps = con.prepareStatement("UPDATE IA_INVENTORY_TOTALS SET BALANCE = BALANCE + ?,USERID = ? "+
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
                     ps = con.prepareStatement("INSERT INTO IA_INVENTORY_TOTALS "+ 
                                             "(BALANCE,USERID,ITEM_CODE,WAREHOUSE_CODE) VALUES (?,?,?,?)");
                 for(int i=0; i<ii.size(); i++)
                 {
                     ps.setInt(1,((InventoryTotal)ii.get(i)).getItemBalance());
                     ps.setInt(2,((InventoryTotal)ii.get(i)).getUserId());
                     ps.setString(3,((InventoryTotal)ii.get(i)).getItemCode());
                     ps.setString(4,((InventoryTotal)ii.get(i)).getWareHouseCode());
                     //ps.setString(5,((InventoryTotal)ii.get(i)).getMtId());
                     ps.addBatch();
                  }
                   done = (ps.executeBatch()).length != -1;
                 }   
                                           
             }catch(Exception er){
                     System.out.println("Error posting Receipt 2 Inventory Totals... ->"+er.getMessage());
                     er.printStackTrace();
                   
             }finally{
                     closeConnection(con,ps);
             }
           
         return done;
     } 
     
}
