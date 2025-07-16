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
import com.magbel.ia.vao.Requisition;
import com.magbel.ia.vao.SalesOrder;
import com.magbel.ia.vao.DistributionOrder;
import com.magbel.ia.vao.Shipment;
//import com.magbel.ia.vao.GlAccountType;

import com.magbel.ia.vao.InventoryHistory;
import com.magbel.ia.vao.InventoryTotal;
import com.magbel.ia.vao.PurchaseOrderDeliveryItem;
import com.magbel.ia.vao.PurchaseOrderItem;
import com.magbel.ia.vao.SalesOrderDeliveryItem;
import com.magbel.ia.vao.SalesOrderItem;
import com.magbel.ia.vao.DistributionOrderItem;
import com.magbel.ia.vao.WarehouseTransfer;
import com.magbel.legend.vao.newAssetTransaction;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import magma.net.vao.Transaction;

/**
 *
 */
public class DistributionOrderServiceBus extends PersistenceServiceDAO {
	SimpleDateFormat sdf;

	final String space = "  ";
	final String comma = ",";
        public String auotoGenCode = "";

	java.util.Date date;

	com.magbel.util.DatetimeFormat df;
	ApplicationHelper helper;
        CodeGenerator cg;
        Requisition reqn = null;
	/**
	 *
	 */
	public DistributionOrderServiceBus() 
	{
		// TODO Auto-generated constructor stub
		sdf = new SimpleDateFormat("dd-MM-yyyy");
		df = new com.magbel.util.DatetimeFormat();
		helper = new ApplicationHelper();
        cg = new CodeGenerator();
	}
	//http://localhost:8080/ias/DocumentHelp.jsp?np=distributionOrderDetail&dstrbCode=DOR/9
	
	/*select distinct(a.warehouse_code), b.name from ST_INVENTORY_TOTALS A,ST_WAREHOUSE B  
	where a.warehouse_code=b.warehouse_code and  item_code='INV/18'*/

       public boolean createDistributionOrder(String requisitionId,String dstrbCode,String customerNo, String po,String reqPersId, String shipDate, 
                        String freight,	String carrier, String projDesc,String reqnDesc,int userId,int approveOfficer,
                        String projectCode,String companyCode,String reqnID,String itemCode,int quantity,double unitPrice, int quantityRemain,String unitCode) {
    	   System.out.println("<<<<<<About to insert into table ST_DISTRIBUTION_ORDER>>");
    	   System.out.println("=====>requisitionId: "+requisitionId+"   requisitionId Length: "+requisitionId.length()+"   unitCode: "+unitCode);
		String query = "INSERT INTO ST_DISTRIBUTION_ORDER (DORDER_CODE,PROJ_DESCRIPTION,REQN_DESCRIPTION,CUSTOMER_CODE," +
						"PORDER_CODE,REQ_PERS_IDENTITY,SHIP_DATE,FREIGHT_CODE,CARRIER_CODE,MTID,USERID,APPROVE_OFFICER,TRANS_DATE," +
                               "PROJECT_CODE,COMP_CODE,REQNID,ApprovalLevel,ApprovalLevelLimit,STATUS,QUANTITY,AMOUNT,QUANTITY_REMAINING,TRANS_TIME,MEASURING_CODE,BATCH_ID) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	    
                String isAutoGen = getCodeName("SELECT auto_generate_ID FROM am_gb_company");
		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String id = helper.getGeneratedId("ST_DISTRIBUTION_ORDER");
         
		//commented by ayojava -19/05/2010
		//dstrbCode = isAutoGen.trim().equalsIgnoreCase("Y") ? cg.generateCode("DISTRIBUTION ORDER","","","") : dstrbCode;
		
		 int apprvLevel=0;
	     int apprvLevelLimit=0;
	     
	     if (approveOfficer > 0)
	     {
	    	 apprvLevelLimit = Integer.parseInt(getCodeName("select max_approve_level from ia_inventory_items where " +
	    	 		"item_code='"+itemCode+"'"));
	     }
		
		 SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");
		 
                auotoGenCode = dstrbCode;
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1,dstrbCode);
			ps.setString(2,projDesc);
			ps.setString(3,reqnDesc);
			ps.setString(4,customerNo);
			ps.setString(5,po);
			ps.setString(6,reqPersId);
			ps.setDate(7,dateConvert(shipDate));
			ps.setString(8,freight);
			ps.setString(9,carrier);
			ps.setString(10,id);
            ps.setInt(11,userId);
            ps.setInt(12,approveOfficer);
            ps.setDate(13,dateConvert(new java.util.Date()));
            ps.setString(14,projectCode);
			ps.setString(15,companyCode);
			ps.setString(16,reqnID);
			ps.setInt(17,apprvLevel);
			ps.setInt(18,apprvLevelLimit);
			ps.setString(19,"available");
			ps.setInt(20,quantity);
			ps.setDouble(21, unitPrice);
			ps.setInt(22,quantityRemain);
			ps.setString(23, timer.format(new java.util.Date()));
			ps.setString(24,unitCode);
			ps.setString(25,requisitionId);

			done = (ps.executeUpdate() != -1);
		} catch (Exception ex) {
		        done = false;
			System.out.println("ERROR Creating Distribution Order " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			closeConnection(con, ps);
		}
		return done;
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
           System.out.println("Error in SalesOrderServiceBus- getCodeName()... ->"+er);
           er.printStackTrace();
        }finally{
           closeConnection(con,ps);
         }   
         return result;
        }  
       
       public boolean createDistributionOrderItemDetail(String orderCode,int quantityRequested,int quantity, double unitPrice,
    		   double advancePymt,String itemCode,String warehouseCode,String companyCode,String reqnID,int userId,String reqnDesc,
    		   String assetId,int quantityRemain,String unitcode,String rfidtag){

           String CREATE_QUERY = "INSERT INTO ST_DISTRIBUTION_ITEM "+
                          "(DORDER_CODE,QUANTITY_REQUEST,QUANTITY_DELIVER,UNITPRICE,COST_PRICE,MTID,ADVANCE_PYMT,ITEM_CODE,"
                          + "WAREHOUSE_CODE,COMP_CODE,REQNID,CREATE_DATE,Accept_Reject,USER_ID,Description,STOCK_CODE,QUANTITY_REMAIN,MEASURING_CODE,RFID_TAG) "+
                          " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                           
                Connection con = null;
                PreparedStatement ps = null;
                String id = helper.getGeneratedId("ST_DISTRIBUTION_ITEM");
 //               System.out.println("<<<<<<<<<<<<Identity: "+id);
                boolean done = false;
                try{    
                        con = getConnection();
                        ps = con.prepareStatement(CREATE_QUERY);   
                        ps.setString(1,orderCode); 
                        ps.setInt(2,quantityRequested);
                        ps.setInt(3,quantity);
                        ps.setDouble(4,unitPrice);
                        ps.setDouble(5,(unitPrice*quantity));
                        ps.setString(6,id);
                        ps.setDouble(7,advancePymt);
                        ps.setString(8,itemCode);
                        ps.setString(9,warehouseCode);
    					ps.setString(10,companyCode);
    					ps.setString(11,reqnID);
    					ps.setDate(12,dateConvert(new java.util.Date()));
    					ps.setString(13,"N");
    					ps.setInt(14,userId);
    					ps.setString(15,reqnDesc);
    					ps.setString(16,assetId);
    					ps.setInt(17,quantityRemain);
    					ps.setString(18,unitcode);
    					ps.setString(19,rfidtag);
    					
    					
                    done = (ps.executeUpdate() != -1);
 //                   System.out.println("<<<<<<<<<<<<done: "+done);
                }catch(Exception er){
                        done = false;
                        System.out.println("Error creating DistributionOrderItemDetail... ->"+er.getMessage());
                }finally{
                        closeConnection(con,ps);
                }
              return done;
           }
       
       public boolean createDistributionOrderItemDetailForBulk(String orderCode,int quantityRequested,int quantity, double unitPrice,
    		   double advancePymt,String itemCode,String warehouseCode,String companyCode,String reqnID,int userId,String reqnDesc,
    		   String assetId,int quantityRemain,String unitcode,String rfidtag,String projectCode){

           String CREATE_QUERY = "INSERT INTO ST_DISTRIBUTION_ITEM "+
                          "(DORDER_CODE,QUANTITY_REQUEST,QUANTITY_DELIVER,UNITPRICE,COST_PRICE,MTID,ADVANCE_PYMT,ITEM_CODE,"
                          + "WAREHOUSE_CODE,COMP_CODE,REQNID,CREATE_DATE,Accept_Reject,USER_ID,Description,STOCK_CODE,QUANTITY_REMAIN,MEASURING_CODE,RFID_TAG,PROJECT_CODE) "+
                          " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                           
                Connection con = null;
                PreparedStatement ps = null;
                String id = helper.getGeneratedId("ST_DISTRIBUTION_ITEM");
 //               System.out.println("<<<<<<<<<<<<Identity: "+id);
                boolean done = false;
                try{    
                        con = getConnection();
                        ps = con.prepareStatement(CREATE_QUERY);   
                        ps.setString(1,orderCode); 
                        ps.setInt(2,quantityRequested);
                        ps.setInt(3,quantity);
                        ps.setDouble(4,unitPrice);
                        ps.setDouble(5,(unitPrice*quantity));
                        ps.setString(6,id);
                        ps.setDouble(7,advancePymt);
                        ps.setString(8,itemCode);
                        ps.setString(9,warehouseCode);
    					ps.setString(10,companyCode);
    					ps.setString(11,reqnID);
    					ps.setDate(12,dateConvert(new java.util.Date()));
    					ps.setString(13,"N");
    					ps.setInt(14,userId);
    					ps.setString(15,reqnDesc);
    					ps.setString(16,assetId);
    					ps.setInt(17,quantityRemain);
    					ps.setString(18,unitcode);
    					ps.setString(19,rfidtag);
    					ps.setString(20,projectCode);
    					
                    done = (ps.executeUpdate() != -1);
 //                   System.out.println("<<<<<<<<<<<<done: "+done);
                }catch(Exception er){
                        done = false;
                        System.out.println("Error creating createDistributionOrderItemDetailForBulk... ->"+er.getMessage());
                }finally{
                        closeConnection(con,ps);
                }
              return done;
           }
              
       public boolean updateRequisitionQty(String reqnID,int quantity,int qtyDistributed,int qtyRequisitioned)
 	  {
    	//   System.out.println(">>>>>qtyRequisitioned: "+qtyRequisitioned+"   quantity: "+quantity+"    Sum: "+(quantity + qtyDistributed));
    	   String UPDATE_QUERY = "";
    	   if (qtyRequisitioned > (quantity + qtyDistributed))
    	   {
    		//   System.out.println(">>>>>Inside with reqnID when qtyRequisitioned is greater: "+reqnID);
    		   UPDATE_QUERY =  "UPDATE am_ad_Requisition SET distributedQty = coalesce(distributedQty,0) + ?  WHERE ReqnID=? ";
    	   }
    	   else
    	   {
    		//   System.out.println(">>>>>Inside with reqnID when qtyRequisitioned is lower: "+reqnID);
    		   UPDATE_QUERY =  "UPDATE am_ad_Requisition SET distributedQty = coalesce(distributedQty,0) + ?," +
    		   		"distributedStatus='Fully Serviced'  WHERE ReqnID=?   ";
    	   } 
    	   
           Connection con = null;
           PreparedStatement ps = null;
           boolean done = false;

           try{
                   con = getConnection();
                   ps = con.prepareStatement(UPDATE_QUERY);
                   ps.setInt(1,quantity); 
                   ps.setString(2,reqnID); 
                                                          
               done = (ps.executeUpdate() != -1);

           }catch(Exception er){
                   done = false;
                   System.out.println("Error UPDATING am_ad_Requisition... ->"+er.getMessage());
                   er.printStackTrace();
           }finally{
                   closeConnection(con,ps);
           }
   return done;
   }
       
       public DistributionOrder findDistributionOrderByOrderNo(String orderno) {
    	   DistributionOrder dorder = new DistributionOrder();
   		String criteria = " AND DORDER_CODE = '"+orderno+"'"; // Where OrderNo = filter
   		dorder = (DistributionOrder)findDistributionOrders(criteria).get(0);
   		return dorder;
   	}

   	public DistributionOrder findDistributionOrderByID(String id) 
   	{
   		DistributionOrder dorder = new DistributionOrder();
   		String criteria = " WHERE MTID = '" + id + "'"; // Where OrderNo =
   		dorder = (DistributionOrder)findDistributionOrders(criteria).get(0);
   		return dorder;
   	}
  /*  
   	private java.util.ArrayList<DistributionOrder> findDistributionOrders(String filter) {
		java.util.ArrayList<DistributionOrder> doList = new java.util.ArrayList<DistributionOrder>();

		DistributionOrder Dorder = null;
  
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT MTID,DORDER_CODE,REQN_DESCRIPTION,POSTED,STATUS,"+
                       "CUSTOMER_CODE,PORDER_CODE,TRANS_DATE,SHIP_DATE,"+
                       "FREIGHT_CODE,CARRIER_CODE,REQ_PERS_IDENTITY,APPROVE_OFFICER,USERID,PROJECT_CODE," +
                       "REQNID, QUANTITY FROM ST_DISTRIBUTION_ORDER ";
                               
		query += filter;
                
		try {

			con = getConnection();
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
                
                Dorder = new DistributionOrder(id,orderNo,desc,posted,status,customerNo,
                po,date,shipDate,freight,carrier,userId,reqPersIdentity,approveOfficer,projectCode,quantity);
                
                Dorder.setReqnID(reqnID);
                
                doList.add(Dorder); 

			}
		}catch (Exception ex) {
					System.out.println("ERROR Querying Distribution Order " + ex.getMessage());
					ex.printStackTrace();
				} finally {
					closeConnection(con, ps,rs);
				}

		return doList;
	}*/
   	
   	public double getDistributionOrderTotalAmount(String orderCode){
        
        String query = "";
        double result = 0;
        query = "SELECT SUM(QUANTITY*UNITPRICE) FROM ST_DISTRIBUTION_ITEM WHERE DORDER_CODE=?";
    
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
                System.out.println("Error in getDistributionOrderTotalAmount()... ->"+er);
        }finally{
                closeConnection(con,ps);
        }   
    
        return result;
    }
   	
   	public ArrayList<DistributionOrderItem> findAllDistributionOrderItemDetail(String orderCode){

        ArrayList<DistributionOrderItem> records = new ArrayList<DistributionOrderItem>();
        
        String SELECT_QUERY = " SELECT MTID,DORDER_CODE,QUANTITY_REQUEST,UNITPRICE,ADVANCE_PYMT,ITEM_CODE, "+
                              " QUANTITY_DELIVER,QUANTITY_REMAIN,WAREHOUSE_CODE,REQNID FROM ST_DISTRIBUTION_ITEM " +
                              " WHERE DORDER_CODE =?";
                              
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
                con = getConnection();
                ps = con.prepareStatement(SELECT_QUERY);
                ps.setString(1,orderCode);
                rs = ps.executeQuery();
                while(rs.next())
                {
                        //String invoiceCode = rs.getString("INVOICECODE");
                        int quantity = rs.getInt("QUANTITY_REQUEST");
                        //double amount = rs.getDouble("AMOUNT");
                        double amount = 0.00;
                        String id = rs.getString("MTID");
                        double unitPrice = rs.getDouble("UNITPRICE");
                        double advancePymt = rs.getDouble("ADVANCE_PYMT");
                        String itemCode = rs.getString("ITEM_CODE");
                        int quantDeliver = rs.getInt("QUANTITY_DELIVER");
                        int quantRemain = rs.getInt("QUANTITY_REMAIN");
                        String warehouseCode = rs.getString("WAREHOUSE_CODE");
                        String reqnID = rs.getString("REQNID");
                        
                    DistributionOrderItem invoice = new DistributionOrderItem(id,orderCode,quantity,unitPrice,amount,
                                                advancePymt,itemCode,quantDeliver,quantRemain,warehouseCode);
                    invoice.setReqnID(reqnID);
                        records.add(invoice);
                }

        }catch(Exception er){
                System.out.println("Error finding All findAllDistributionOrderItemDetail...->"+er.getMessage());
        }finally{
                closeConnection(con,ps,rs);
        }

        return records ;
}
   	
   	public boolean updateDistributionOrder(String po,String freight, String carrier,
			String customerCode,String orderCode,String shipDate,String reqPersId,int approveOfficer,String projectCode) {
			
			String query = "UPDATE ST_DISTRIBUTION_ORDER SET CUSTOMER_CODE=?,PORDER_CODE=?,SHIP_DATE=?,FREIGHT_CODE = ?," +
					"CARRIER_CODE = ?, REQ_PERS_IDENTITY=?,APPROVE_OFFICER=?,PROJECT_CODE=? WHERE DORDER_CODE = ?";
			
			Connection con = null;
			
			PreparedStatement ps = null;
			boolean done = false;
			
			
			System.out.println("====================================================");
			System.out.println("customerCode >>>>>>>>>>> " + customerCode);
			System.out.println("po >>>>>>>>>>> " + po);
			System.out.println("shipDate >>>>>>>>>>> " + dateConvert(shipDate));
			System.out.println("freight >>>>>>>>>>> " + freight);
			System.out.println("carrier >>>>>>>>>>> " + carrier);
			System.out.println("reqPersId >>>>>>>>>>> " + reqPersId);
			System.out.println("approveOfficer >>>>>>>>>>> " + approveOfficer);
			System.out.println("projectCode >>>>>>>>>>> " + projectCode);
			System.out.println("orderCode >>>>>>>>>>> " + orderCode);
			System.out.println("====================================================");
			
			try 
			{
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1,customerCode);
			ps.setString(2,po);
			ps.setDate(3,dateConvert(shipDate));
			ps.setString(4,freight);
			ps.setString(5,carrier);
            ps.setString(6,reqPersId.trim());
            ps.setInt(7,approveOfficer);
            ps.setString(8,projectCode);
			ps.setString(9,orderCode);
			
			done = (ps.executeUpdate() != -1);
			} catch (Exception ex) {
			System.out.println("ERROR Updating Distribution Order " + ex.getMessage());
			ex.printStackTrace();
			} finally {
			closeConnection(con, ps);
			}
			return done;
			}

   	
   	
   	public boolean createTransactionApproval(String transCode,String transType, String desc,int userId,
            int approveOfficer,String status,String itemCode,int quantity,String remoteAdd,String compCode) 
   	{

   		String query = "INSERT INTO ST_TRANSACTION_APPROVAL (MTID,TRANS_CODE,TRANS_TYPE,DESCRIPTION,USERID,APPROVE_OFFICER," +
   				"TRANS_DATE,STATUS,MAX_APPROVE_LEVEL,CONCURRENCE,ITEM_CODE,QUANTITY,workstationip,company_code)"+
                                       " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		int maxApproveLevel = 0;
		int organPosition = 0;
		int concurrence = 0;
		if(quantity > 0)
		{
			maxApproveLevel = Integer.parseInt(getCodeName("SELECT MAX_APPROVE_LEVEL FROM ST_INVENTORY_ITEMS WHERE " +
			"ITEM_CODE='"+itemCode+"'"));
			
			organPosition = Integer.parseInt(getCodeName("SELECT APPROVE_LEVEL FROM MG_GB_USER WHERE USER_ID='"+userId+"'"));
			
			String strConcurrence = getCodeName("SELECT CONCURRENCE FROM ST_ITEM_APPROVAL_DETAIL WHERE " +
			"ITEM_CODE='"+itemCode+"' AND APPROVE_LEVEL='"+organPosition+"'"); 
			
			concurrence = ((strConcurrence==null)||strConcurrence.equals("")) ? 0 :Integer.parseInt(strConcurrence);
		}
		
		String id = helper.getGeneratedId("ST_TRANSACTION_APPROVAL");
		      
		try 
		{
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1,id);
			ps.setString(2,transCode);
			ps.setString(3,transType);
			ps.setString(4,desc);
			ps.setInt(5,userId);
			ps.setInt(6,approveOfficer);
			ps.setDate(7,dateConvert(new java.util.Date()));
			ps.setString(8,status);
			ps.setInt(9,maxApproveLevel);
			ps.setInt(10,concurrence);
			ps.setString(11,itemCode);
			ps.setInt(12,quantity);
			ps.setString(13,remoteAdd);
			ps.setString(14,compCode);
			
			done = (ps.executeUpdate() != -1);
		} 
		catch (Exception ex) 
		{
			done = false;
			System.out.println("ERROR Creating Transaction Approval " + ex.getMessage());
			ex.printStackTrace();
		} 
		finally 
		{
			closeConnection(con, ps);
		}
		return done;
}

   	
   /*    
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

		String query = "INSERT INTO ST_SHIPMENTS("+
						"SORDER_CODE,WAREHOUSE_CODE,"+
						"SHIPMENT_DATE,QUANTITY_ORDER,QUANTITY_SHIP,"+
						"ITEMCODE,UNITPRICE,MTID) VALUES(?,?,?,?,?,?,?,?)";

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String id = helper.getGeneratedId("ST_SHIPMENTS");

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

	
   
    public boolean updateSalesOrderItemDetail(String id,String orderCode,int quantity, double unitPrice,String warehouseCode,String itemCode){

            String UPDATE_QUERY =  "UPDATE ST_SO_ITEM SET QUANTITY=?,"+
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
                            "FROM ST_SO_ITEM WHERE SORDER_CODE =? AND MTID=? ";

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
                            "FROM ST_SO_ITEM WHERE SORDER_CODE =? AND ITEM_CODE=? AND WAREHOUSE_CODE=? ";

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
                                  "FROM ST_SO_DELIVERY_ITEM a,ST_SALES_ORDER b " + 
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
                                                        advancePymt,itemCode,quantDeliver,quantRemain,batchCode,transDate,userId,customerCode,warehouseCode);
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
                                  "QUANTITY_DELIVER,QUANTITY_REMAIN,BATCH_CODE,TRANS_DATE,USERID,WAREHOUSE_CODE FROM ST_SO_DELIVERY_ITEM "+
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
    
    
    public ArrayList findAllSalesOrderItemDetailByQuery(String query){

            ArrayList records = new ArrayList();
            String SELECT_QUERY = "SELECT MTID,SORDER_CODE,QUANTITY,AMOUNT,UNITPRICE,ADVANCE_PYMT,ITEM_CODE, "+
                                  "QUANTITY_DELIVER,QUANTITY_REMAIN,WAREHOUSE_CODE FROM ST_SO_ITEM WHERE MTID IS NOT NULL ";
                                  
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
		String query = "UPDATE  ST_SHIPMENTS   "+
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
	String id = helper.getGeneratedId("ST_JOURNAL_VOUCHER");
	final String JV_QUERY = "INSERT INTO ST_JOURNAL_VOUCHER("+
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

	public boolean createGlAccountType(String code,String name){

			String query = "INSERT INTO ST_GLACCOUNT_TYPE("+
						   "MTID,GLTYPE,NAME) VALUES(?,?,?)";
			Connection con = null;
			PreparedStatement ps = null;
			boolean done = false;
			String id = helper.getGeneratedId("ST_GLACCOUNT_TYPE");

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

  
	public boolean updateGlAccountType(String id,String name) {

		String query = "UPDATE ST_GLACCOUNT_TYPE SET NAME = ? "+
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


	public void deleteGlAccountType(String id) {

		String Query = "DELETE FROM ST_GLACCOUNT_TYPE  "+
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


        
    //this method is used to create new delivery (sales Delivery)
    public boolean createSODeliveryItem(String orderCode,String batchCode,String[] id,String[] itemCode,
                                        String[] quantity,String[] quantDeliver,int userId,String[] unitPrice,
                                        String[] amount,String[] warehouseCode){
        //ArrayList list = new ArrayList();
         String CREATE_QUERY = "INSERT INTO ST_SO_DELIVERY_ITEM "+
                          "(SORDER_CODE,BATCH_CODE,MTID,ITEM_CODE,QUANTITY_DELIVER,"+
                          "QUANTITY_REMAIN,USERID,QUANTITY,UNITPRICE,AMOUNT,TRANS_DATE,CUSTOMER_CODE,WAREHOUSE_CODE) " +
                          "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
                      
            Connection con = null;
            PreparedStatement ps = null;
            //id = helper.getGeneratedId("ST_PO_DELIVERY_ITEM");
            boolean done = false;
            //list = findAllPurchaseOrderItemDetail(orderCode);
            String customerCode = getCodeName("SELECT CUSTOMER_CODE FROM ST_SALES_ORDER WHERE SORDER_CODE='"+orderCode+"'");
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
          String UPDATE_QUERY =  "UPDATE ST_SO_DELIVERY_ITEM SET QUANTITY_DELIVER=?,QUANTITY_REMAIN=? WHERE SORDER_CODE=? AND BATCH_CODE=? AND MTID=?";
          
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
                               
          String UPDATE_QUERY = "UPDATE ST_SO_DELIVERY_ITEM SET POSTED = ? WHERE BATCH_CODE=?";                     
          //String warehouseCode = getCodeName("SELECT WAREHOUSE_CODE FROM ST_SO_ITEM WHERE SORDER_CODE = '"+orderCode+"'");    
            
           boolean autoCommit = false;
             Connection con = null;
             PreparedStatement ps = null;         
             //id = helper.getGeneratedId("ST_PO_DELIVERY_ITEM");
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
             //id = helper.getGeneratedId("ST_PO_DELIVERY_ITEM");
             boolean done = false;
             String mtId = "";
             String itemCode = "";
             int itemBalance = 0;
             String desc = "";
             int userId = 0;
             //warehouseCode = getCodeName("SELECT WAREHOUSE_CODE FROM ST_SALES_ORDER WHERE PORDER_CODE = '"+orderCode+"'");  
             
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
            String para = "";
            for(int i=0; i<printChk.length; i++){
             para = para + ",'"+printChk[i]+"'";
                
            }
            
            String SELECT_QUERY = "SELECT MTID,SORDER_CODE,QUANTITY,AMOUNT,UNITPRICE,ADVANCE_PYMT,ITEM_CODE, "+
                                  "QUANTITY_DELIVER,QUANTITY_REMAIN,BATCH_CODE,TRANS_DATE,USERID,WAREHOUSE_CODE "+
                                  "FROM ST_SO_DELIVERY_ITEM WHERE SORDER_CODE =? AND BATCH_CODE=? "+filter;// AND MTID IN ("+para.substring(1)+")";
                                  
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


	private java.util.ArrayList<Shipment> findShipments(String filter) {
		java.util.ArrayList<Shipment> shList = new java.util.ArrayList<Shipment>();

		Shipment ship = null;

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT MTID,SHIPMENT_CODE,SORDER_CODE,"+
						"WAREHOUSE_CODE,SHIPMENT_DATE,QUANTITY_ORDER,"+
						"QUANTITY_SHIP,ITEMCODE,UNITPRICE    "+
						"FROM ST_SHIPMENTS ";
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
     
	public java.util.ArrayList<SalesOrder> findSalesOrdersByQuery(String filter2,String filter) {
		java.util.ArrayList<SalesOrder> soList = new java.util.ArrayList<SalesOrder>();
		String criteria = " WHERE COMP_CODE = '"+filter2+"' AND MTID IS NOT NULL " + filter;
		soList = findSalesOrders(criteria);
		return soList;
	}


	
    
	public java.util.ArrayList<Shipment> findShipmentByQuery(String filter) {
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

	public ArrayList findGlAccountType(){

		ArrayList records = new ArrayList();
		String query = "SELECT MTID,GLTYPE,NAME FROM ST_GLACCOUNT_TYPE ";
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

	public GlAccountType findGlAccountTypeById(String id){

		String query = "SELECT MTID,GLTYPE,NAME FROM ST_GLACCOUNT_TYPE "+
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
	  
	  
	  public boolean updateInventoryTotals2(int quantity,String warehouseCode,String itemCode)
	  {

          String UPDATE_QUERY =  "UPDATE ST_INVENTORY_TOTALS SET TMP_BALANCE = TMP_BALANCE + ?  WHERE ITEM_CODE=?   AND WAREHOUSE_CODE=?";
          Connection con = null;
          PreparedStatement ps = null;
          boolean done = false;

          try{
                  con = getConnection();
                  ps = con.prepareStatement(UPDATE_QUERY);
                  ps.setInt(1,quantity);  
                  ps.setString(2,itemCode); 
                  ps.setString(3,warehouseCode); 
                                      
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
*/


    public ArrayList findDistributionOrdersList(String filter)
    {
		String query = "SELECT DISTINCT MTID,DORDER_CODE,REQN_DESCRIPTION,POSTED,a.STATUS,"+
                "CUSTOMER_CODE,PORDER_CODE,TRANS_DATE,SHIP_DATE,"+
                "FREIGHT_CODE,CARRIER_CODE,REQ_PERS_IDENTITY,APPROVE_OFFICER,a.USERID,PROJECT_CODE," +
                "a.REQNID, a.QUANTITY FROM ST_DISTRIBUTION_ORDER a, am_ad_Requisition b ";
                         
	query += filter;
	System.out.println("<<<<findDistributionOrdersList query: "+query);
		ArrayList list = new ArrayList();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {

			con = getConnection();
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

    public DistributionOrder findDistributionOrderByCode(String ordercode){
        String SELECT_QUERY = "SELECT * FROM ST_DISTRIBUTION_ORDER WHERE DORDER_CODE  =  ?";

         Connection con = null;
         //Statement stmt = null;
         PreparedStatement ps = null;
         ResultSet rs = null;
         DistributionOrder Dorder  = null;
          
         try{
               con = getConnection();
               ps = con.prepareStatement(SELECT_QUERY);
               ps.setString(1,ordercode);
               rs  = ps.executeQuery();

               while(rs.next())
               {
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
                int quantityBalance = rs.getInt("QUANTITY_REMAINING");
                String reqPersIdentity = rs.getString("req_Pers_Identity");
                String approveOfficer = rs.getString("approve_officer");
                String projectCode = rs.getString("PROJECT_CODE");
                String projectDesc = rs.getString("PROJ_DESCRIPTION");
                String reqnDescript = rs.getString("REQN_DESCRIPTION");
                String reqnID = rs.getString("reqnID");
                double unitPrice = rs.getDouble("AMOUNT");
                String measurecode = rs.getString("MEASURING_CODE"); 
                String warehouseCode = getCodeName("SELECT WAREHOUSE_CODE FROM ST_DISTRIBUTION_ITEM WHERE DORDER_CODE = '"+orderNo+"'");
                String itemRequested =  getCodeName("SELECT STOCK_CODE FROM ST_DISTRIBUTION_ITEM WHERE DORDER_CODE = '"+orderNo+"'");
                Dorder = new DistributionOrder(id,orderNo,desc,posted,status,customerNo,
                po,date,shipDate,freight,carrier,userId,reqPersIdentity,approveOfficer,projectCode,quantity);
                Dorder.setDescription(projectDesc);
                Dorder.setQuantitybalance(quantityBalance);
                Dorder.setQuantity(quantity);
                Dorder.setUnitPrice(unitPrice);
                Dorder.setWarehouseCode(warehouseCode);
                Dorder.setReqnDescription(reqnDescript);
                Dorder.setReqnID(reqnID);
                Dorder.setItemRequested(itemRequested);
                Dorder.setUnitCode(measurecode);
               }
                          
         }catch(Exception er){
                 System.out.println("Error RETRIEVING Distribution Record By Id... ->"+er.getMessage());
         }finally{
                 closeConnection(con,ps,rs);
         }
      return  Dorder ;
 }
    
public boolean updateForAcceptanceItem(String dstrbCode,String assetId,String status,String itemCode,String warehouseCode)
  {

   String UPDATE_QUERY0 = "";
   String UPDATE_QUERY2 = "";
   if (status.equalsIgnoreCase("ACCEPTED"))
   {
	   status = "ACCEPTED";
	   UPDATE_QUERY0 =  "UPDATE ST_STOCK SET ASSET_STATUS = ?  WHERE ASSET_ID=? ";
	   UPDATE_QUERY2 =  "UPDATE ST_DISTRIBUTION_ITEM SET Accept_Reject = ?  WHERE DORDER_CODE=? ";
   }
    Connection con0 = null;
    PreparedStatement ps0 = null; 
    Connection con2 = null;
    PreparedStatement ps2 = null;  
    boolean done = false;

    try{   System.out.println("<<<<<UPDATE_QUERY0 in updateForAcceptance: "+UPDATE_QUERY0+"    assetId: "+assetId);
            con0 = getConnection();
            ps0 = con0.prepareStatement(UPDATE_QUERY0);
            ps0.setString(1,status); 
            ps0.setString(2,assetId); 
                                                   
        done = (ps0.executeUpdate() != -1);

        System.out.println("<<<<<UPDATE_QUERY2 in updateForAcceptance: "+UPDATE_QUERY2+"    assetId: "+assetId);
        con2 = getConnection();
        ps2 = con2.prepareStatement(UPDATE_QUERY2);
        ps2.setString(1,status); 
        ps2.setString(2,dstrbCode); 
                                               
        done = (ps2.executeUpdate() != -1);
        
    }catch(Exception er){
            done = false;
            System.out.println("Error UPDATING ALL DISTRIBUTION TABLES FOR ACCEPTANCE IN updateForAcceptanceItem... ->"+er.getMessage());
                er.printStackTrace();
        }finally{
                closeConnection(con0,ps0);
                closeConnection(con2,ps2);
        }
return done;
}


public boolean updateForAcceptanceTotal(String dstrbCode,String status,String itemCode,String warehouseCode,int QtyAccepted)
{

String UPDATE_QUERY1 = "";
String UPDATE_QUERY3 = "";
//int quantity = 1;
if (status.equalsIgnoreCase("ACCEPTED"))
{
   status = "ACCEPTED";
   UPDATE_QUERY1 =  "UPDATE ST_DISTRIBUTION_ORDER SET STATUS = ?  WHERE DORDER_CODE=? ";
   UPDATE_QUERY3 =  "UPDATE ST_INVENTORY_TOTALS SET TMP_BALANCE = coalesce(TMP_BALANCE,0)-?  WHERE ITEM_CODE=? AND WAREHOUSE_CODE=?";
}
Connection con1 = null;
PreparedStatement ps1 = null;    
Connection con3 = null;
PreparedStatement ps3 = null;    
boolean done = false;

try{   
    System.out.println("<<<<<UPDATE_QUERY1 in updateForAcceptance: "+UPDATE_QUERY1+"    itemCode: "+itemCode);
    con1 = getConnection();
    ps1 = con1.prepareStatement(UPDATE_QUERY1);
    ps1.setString(1,status); 
    ps1.setString(2,dstrbCode); 
                                           
    done = (ps1.executeUpdate() != -1);
    System.out.println("<<<<<UPDATE_QUERY3 in updateForAcceptance: "+UPDATE_QUERY3+"    itemCode: "+itemCode+"   QtyAccepted: "+QtyAccepted);
    con3 = getConnection();
    ps3 = con3.prepareStatement(UPDATE_QUERY3);
    ps3.setInt(1,QtyAccepted); 
    ps3.setString(2,itemCode); 
    ps3.setString(3,warehouseCode); 
                                           
    done = (ps3.executeUpdate() != -1);
    
}catch(Exception er){
        done = false;
        System.out.println("Error UPDATING ALL DISTRIBUTION TABLES FOR ACCEPTANCE IN updateForAcceptanceTotal... ->"+er.getMessage());
            er.printStackTrace();
    }finally{
            closeConnection(con1,ps1);
            closeConnection(con3,ps3);
    }
return done;
}

public boolean updateForRejection(String dstrbCode,String assetId,String status,String itemCode,String warehouseCode)
{

String UPDATE_QUERY2 = "";
String UPDATE_QUERY0 = "";
int quantity = 1;
if (status.equalsIgnoreCase("Reject"))
{
   status = "REJECTED";
   UPDATE_QUERY0 =  "UPDATE ST_STOCK SET ASSET_STATUS = ?  WHERE ASSET_ID=? ";
   UPDATE_QUERY2 =  "UPDATE ST_DISTRIBUTION_ITEM SET Accept_Reject = ?  WHERE DORDER_CODE=? ";
} 
//System.out.println("UPDATE_QUERY0: "+UPDATE_QUERY0+"  itemCode: "+itemCode+"   warehouseCode: "+warehouseCode);
Connection con0 = null;
PreparedStatement ps0 = null;    
Connection con2 = null;
PreparedStatement ps2 = null;
boolean done = false;

try{
    con0 = getConnection();
    ps0 = con0.prepareStatement(UPDATE_QUERY0);
    ps0.setString(1,"ACTIVE"); 
    ps0.setString(2,dstrbCode); 
                                           
    done = (ps0.executeUpdate() != -1);
    
    con2 = getConnection();
    ps2 = con2.prepareStatement(UPDATE_QUERY2);
    ps2.setString(1,status); 
    ps2.setString(2,dstrbCode); 
                                           
    done = (ps2.executeUpdate() != -1);
    
}catch(Exception er){
        done = false;
        System.out.println("Error UPDATING ALL DISTRIBUTION TABLES FOR REJECTION... ->"+er.getMessage());
            er.printStackTrace();
    }finally{
            closeConnection(con0,ps0);
            closeConnection(con2,ps2);
    }
return done;
}

public boolean updateTotalForRejection(String dstrbCode,String status,String itemCode,String warehouseCode,int QtyAccepted)
{

String UPDATE_QUERY1 = "";
String UPDATE_QUERY3 = "";
int quantity = 1;
if (status.equalsIgnoreCase("Reject"))
{
   status = "REJECTED";
   UPDATE_QUERY1 =  "UPDATE ST_DISTRIBUTION_ORDER SET STATUS = ?  WHERE DORDER_CODE=? ";
   UPDATE_QUERY3 =  "UPDATE ST_INVENTORY_TOTALS SET BALANCE = BALANCE+?, TMP_BALANCE = coalesce(TMP_BALANCE,0)-?  WHERE ITEM_CODE=? AND WAREHOUSE_CODE=?";
} 
//System.out.println("UPDATE_QUERY3: "+UPDATE_QUERY3+"  itemCode: "+itemCode+"   warehouseCode: "+warehouseCode);
Connection con1 = null;
PreparedStatement ps1 = null;  
Connection con3 = null;
PreparedStatement ps3 = null;    
boolean done = false;

try{
    con1 = getConnection();
    ps1 = con1.prepareStatement(UPDATE_QUERY1);
    ps1.setString(1,status); 
    ps1.setString(2,dstrbCode); 
                                           
    done = (ps1.executeUpdate() != -1);

    con3 = getConnection();
    ps3 = con3.prepareStatement(UPDATE_QUERY3);
    ps3.setInt(1,QtyAccepted); 
    ps3.setInt(2,QtyAccepted); 
    ps3.setString(3,itemCode); 
    ps3.setString(4,warehouseCode); 
                                           
    done = (ps3.executeUpdate() != -1);
    
}catch(Exception er){
        done = false;
        System.out.println("Error UPDATING ALL DISTRIBUTION TABLES FOR REJECTION... ->"+er.getMessage());
            er.printStackTrace();
    }finally{
            closeConnection(con1,ps1);
            closeConnection(con3,ps3);
    }
return done;
}


public boolean updateStockQry(String stockId)
{
   String UPDATE_QUERY =  "UPDATE ST_STOCK SET ASSET_STATUS = ?  WHERE Asset_id=? ";
	   
    Connection con = null;
    PreparedStatement ps = null;
    boolean done = false;
    System.out.println("<<<<<<<=======updateStockQry stockId: "+stockId);
    try{
            con = getConnection();
            ps = con.prepareStatement(UPDATE_QUERY);
//            ps.setString(1,"ISSUED"); 
            ps.setString(1,"ACTIVE"); 
            ps.setString(2,stockId); 
                                                   
        done = (ps.executeUpdate() != -1);

    }catch(Exception er){
            done = false;
            System.out.println("Error UPDATING ST_STOCK Table... ->"+er.getMessage());
            er.printStackTrace();
    }finally{
            closeConnection(con,ps);
    }
return done;
}

public java.util.ArrayList getStockDetailrecords(String integrifyId)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	//System.out.println("====eschool Server Date-----> "+date);
	date = date.substring(0, 10);
	String finacleTransId= null;
		String query = " SELECT STOCK_CODE,ITEM_CODE,Description " +
				"FROM ST_DISTRIBUTION_ITEM WHERE  DORDER_CODE = '"+integrifyId+"' ";
//	Transaction transaction = null;
  
	Connection c = null;
	ResultSet rs = null;
	Statement s = null;

	try {
		    c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next())
			   {				
				String stockCode = rs.getString("STOCK_CODE");
				String itemCode = rs.getString("ITEM_CODE");				
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setIntegrifyId(stockCode);
				newTransaction.setDescription(itemCode);
				_list.add(newTransaction);
			   }
	 }   
				 catch (Exception e)
					{
						e.printStackTrace();
					}
					finally
					{
						closeConnection(c, s, rs);
					}
	return _list;
}

public boolean deleteDistributionOrder(String transId)
{
    String query;
    Connection con;
    PreparedStatement ps;
    boolean done;
    query = "DELETE FROM ST_DISTRIBUTION_ORDER WHERE TRANS_ID = ?";
    con = null;
    ps = null;
    done = false;
    try
    {
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, transId);
        done = ps.executeUpdate() != -1;
    }catch(Exception er){
        done = false;
        System.out.println("ERROR in deleteDistributionOrder() "+er.getMessage());
        er.printStackTrace();
}finally{
        closeConnection(con,ps);
}        
    return done;
}

public ArrayList findDistributionOrders(String filter)
{
    ArrayList doList;
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    String query;
    doList = new ArrayList();
    DistributionOrder Dorder = null;
    con = null;
    ps = null;
    rs = null;
    query = "SELECT a.MTID,a.DORDER_CODE,a.REQN_DESCRIPTION,a.POSTED,a.STATUS,a.CUSTOMER_CODE,a.PORDER_CODE," +
"a.TRANS_DATE,a.SHIP_DATE,a.FREIGHT_CODE,a.CARRIER_CODE,a.REQ_PERS_IDENTITY,a.APPROVE_OFFICER,b.ItemRequested,b.ItemType," +
"a.USERID,a.PROJECT_CODE,a.PROJ_DESCRIPTION,a.REQNID, a.QUANTITY, a.AMOUNT,a.MEASURING_CODE FROM ST_DISTRIBUTION_ORDER a, am_ad_Requisition b "+
"WHERE a.REQNID = b.ReqnID"
;
    query = (new StringBuilder(String.valueOf(query))).append(filter).toString();
    System.out.println("<<<<findDistributionOrders query: "+query);
    try
    {
        con = getConnection();
        ps = con.prepareStatement(query);
  //      DistributionOrder Dorder;
        for(rs = ps.executeQuery(); rs.next(); doList.add(Dorder))
        {
            String orderNo = rs.getString("DORDER_CODE");
            String desc = rs.getString("REQN_DESCRIPTION");
            String posted = rs.getString("POSTED");
            String status = rs.getString("STATUS");
            String customerNo = rs.getString("CUSTOMER_CODE");
            String po = rs.getString("PROJECT_CODE");
            String date = formatDate(rs.getDate("TRANS_DATE"));
            String shipDate = formatDate(rs.getDate("SHIP_DATE"));
            String freight = rs.getString("FREIGHT_CODE");
            String carrier = rs.getString("CARRIER_CODE");
            String id = rs.getString("MTID");
            int userId = rs.getInt("USERID");
            String reqPersIdentity = rs.getString("req_Pers_Identity");
            String approveOfficer = rs.getString("approve_officer");
            String projectCode = rs.getString("PROJ_DESCRIPTION");
            String reqnID = rs.getString("reqnID");
            int quantity = rs.getInt("QUANTITY");
            double amount = rs.getDouble("AMOUNT");
            String itemrequested = rs.getString("ItemRequested");
            String itemtype = rs.getString("ItemType");
    //        String warehouse = rs.getString("WAREHOUSE_CODE");
            String measuringCode = rs.getString("MEASURING_CODE");
            Dorder = new DistributionOrder(id, orderNo, desc, posted, status, customerNo, po, date, shipDate, freight, carrier, userId, reqPersIdentity, approveOfficer, projectCode);
            Dorder.setReqnID(reqnID);
            Dorder.setQuantity(quantity);
            Dorder.setAmount(amount);
            Dorder.setItemRequested(itemrequested);
            Dorder.setItemType(itemtype);
  //          Dorder.setWarehouse(warehouse);
            Dorder.setMeasuringCode(measuringCode);
        }
    }
    catch (Exception e) {
		String warning = "ERROR Querying Distribution Order "
				+ " ->" + e.getMessage();
		System.out.println(warning);
	} finally {
		closeConnection(con, ps, rs);
	}
    return doList;
}


public boolean undoRequisitionQty(String reqnID, int quantity)
{
    String UPDATE_QUERY;
    Connection con;
    PreparedStatement ps;
    boolean done;
    UPDATE_QUERY = "";
    UPDATE_QUERY = "UPDATE am_ad_Requisition SET distributedQty = distributedQty - ?,distributedStat" +
"us='pending' WHERE ReqnID=? "
;
    con = null;
    ps = null;
    done = false;
    try
    {
        con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);
        ps.setInt(1, quantity);
        ps.setString(2, reqnID);
        done = ps.executeUpdate() != -1;
    }
    catch(Exception er){
        done = false;
        System.out.println("Error UPDATING am_ad_Requisition... ->"+er.getMessage());
        er.printStackTrace();
		}finally{
		        closeConnection(con,ps);
		}            
    return done;
}


public boolean undoInventoryTotalsForDistributionOrder(int quantity, String warehouseCode, String itemCode)
{
    String UPDATE_QUERY;
    Connection con;
    PreparedStatement ps;
    boolean done;
    UPDATE_QUERY = "UPDATE ST_INVENTORY_TOTALS SET TMP_BALANCE = coalesce(TMP_BALANCE,0)+?  WHERE ITEM_CODE=?   " +
"AND WAREHOUSE_CODE=?"
;  
    con = null;
    ps = null;
    done = false;
    try
    {
        con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);
        ps.setInt(1, quantity);
        ps.setString(2, itemCode);
        ps.setString(3, warehouseCode);
        done = ps.executeUpdate() != -1;
    }
    catch(Exception er){
        done = false;
        System.out.println("Error UPDATING ST_INVENTORY_TOTALS... ->"+er.getMessage());
        er.printStackTrace();
		}finally{
		        closeConnection(con,ps);
		}       
    return done;
}

public boolean returnStockForDistributionAccepted(String assetId,String status,String distributionStatus)
{

    Connection con;
    PreparedStatement ps;
    PreparedStatement ps1;
    boolean done;
    String UPDATE_QUERY = "UPDATE ST_STOCK SET ASSET_STATUS = ?  WHERE ASSET_ID=? ";
    String UPDATE_QUERY2 = "UPDATE ST_DISTRIBUTION_ITEM SET ACCEPT_REJECT = ?  WHERE STOCK_CODE=? ";
    con = null;
    ps = null;
    ps1 = null;
    done = false;
    try
    {
        con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);
        ps.setString(1, status);
        ps.setString(2, assetId);
        done = ps.executeUpdate() != -1;

        ps1 = con.prepareStatement(UPDATE_QUERY2);
        ps1.setString(1, status);
        ps1.setString(2, assetId);
        done = ps1.executeUpdate() != -1;        
    }
    catch(Exception er){
        done = false;
        System.out.println("Error UPDATING ST_STOCK in returnStockForDistributionAccepted... ->"+er.getMessage());
        er.printStackTrace();
		}finally{
		        closeConnection(con,ps);
		        closeConnection(con,ps1);
		}       
    return done;
}

public boolean ApprovalStatusUpdate(String reqnID,String status,String distributionStatus,String postingDate, String userID)
{
    Connection con;
    PreparedStatement ps;
    PreparedStatement ps1;
    boolean done;
 //   String UPDATE_QUERY = "UPDATE am_ad_Requisition SET Status = ?,distributedstatus = ?  WHERE ReqnID=? ";
    String UPDATE_QUERY2 = "UPDATE am_asset_approval SET process_status = ?,DATE_APPROVED = ?  WHERE asset_id=? AND SUPER_ID = ?";
    con = null;
    ps = null;
    ps1 = null;
    done = false;
//    System.out.println("QUERY IN UPDATING APPROVAL in ApprovalStatusUpdate "+UPDATE_QUERY2);
    try
    {
        con = getConnection();
 /*
        ps = con.prepareStatement(UPDATE_QUERY);
        ps.setString(1, status);
        ps.setString(2, distributionStatus);
        ps.setString(3, reqnID);
        done = ps.executeUpdate() != -1;
*/
        ps1 = con.prepareStatement(UPDATE_QUERY2);
        ps1.setString(1, status);
        ps1.setString(2, postingDate);
        ps1.setString(3, reqnID);
        ps1.setString(4, userID);
        done = ps1.executeUpdate() != -1;        
    }
    catch(Exception er){
        done = false;
        System.out.println("Error UPDATING APPROVAL in ApprovalStatusUpdate... ->"+er.getMessage());
        er.printStackTrace();
		}finally{
		     //   closeConnection(con,ps);
		        closeConnection(con,ps1);
		}       
    return done;
}

public ArrayList findReturnedAcceptanceList(String filter)
{
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	Requisition reqn = null;
	ArrayList doList;
	doList = new ArrayList();
	String query="select * from am_ad_Requisition ";
	query += filter;
//	System.out.println("query in findReturnedAcceptanceList >>>> " + query);
	try 
	{
		con = getConnection();
		ps = con.prepareStatement(query);
		rs = ps.executeQuery(); 
		for(rs = ps.executeQuery(); rs.next(); doList.add(reqn))
		{
			String reqnID = rs.getString("ReqnID");
			String userId = rs.getString("UserId");
			String requestingBranch = rs.getString("ReqnBranch");
			String requestingSection = rs.getString("ReqnSection");
			String requestingDept = rs.getString("ReqnDepartment");
			String reqnUserId = rs.getString("ReqnUserID");
			String itemType = rs.getString("ItemType");
			String itemsReturned = rs.getString("ItemRequested");
			String stockReturned = rs.getString("RETURNED_STOCK");
			String remark = rs.getString("Remark");
			String No_Of_Items = rs.getString("Quantity");
			String projCode = rs.getString("projectCode");
			String returnedCategory = rs.getString("ReturnedCategory");
			String reqnType = rs.getString("ReqnType");
			String measuringCode = rs.getString("MEASURING_CODE");
			String returnedStock = rs.getString("RETURNED_ORDERNO");
			String category = rs.getString("Category");	
			String distributedQty = rs.getString("distributedQty");	
			reqn = new Requisition(reqnID,requestingBranch,requestingDept,requestingSection,
                     remark, reqnUserId,distributedQty, returnedCategory, 
                    reqnType, measuringCode,userId,returnedStock,
                    category, projCode, No_Of_Items,itemType,itemsReturned,stockReturned);

	//		System.out.println(">>>>>>>Project Code: "+rs.getString("projectCode"));
		}    
	} 
	catch (Exception ex) 
	{
		System.out.println("WARN: Error findReturnedAcceptanceList ->" + ex);
	} 
	finally 
	{
		closeConnection(con, ps);
	}
		
	return doList;
}

public Requisition findReturnedAcceptanceByCode(String ReqnID){
    String SELECT_QUERY = "SELECT * FROM am_ad_Requisition WHERE REQNID  =  ?";

     Connection con = null;
     //Statement stmt = null;
     PreparedStatement ps = null;
     ResultSet rs = null;
     Requisition reqn  = null;
      
     try{
           con = getConnection();
           ps = con.prepareStatement(SELECT_QUERY);
           ps.setString(1,ReqnID);
           rs  = ps.executeQuery();

           while(rs.next())
           {           
			String reqnID = rs.getString("ReqnID");
			String userId = rs.getString("UserId");
			String requestingBranch = rs.getString("ReqnBranch");
			String requestingSection = rs.getString("ReqnSection");
			String requestingDept = rs.getString("ReqnDepartment");
			String reqnUserId = rs.getString("ReqnUserID");
			String itemType = rs.getString("ItemType");
			String itemsReturned = rs.getString("ItemRequested");
			String stockReturned = rs.getString("RETURNED_STOCK");
			String remark = rs.getString("Remark");
			String No_Of_Items = rs.getString("Quantity");
			String projCode = rs.getString("projectCode");
			String returnedCategory = rs.getString("ReturnedCategory");
			String reqnType = rs.getString("ReqnType");
			String measuringCode = rs.getString("MEASURING_CODE");
			String returnedStock = rs.getString("RETURNED_STOCK");
			String category = rs.getString("Category");	
			String distributedQty = rs.getString("distributedQty");	
			String returnOrder = rs.getString("RETURNED_ORDERNO");	
			reqn = new Requisition(reqnID,requestingBranch,requestingDept,requestingSection,
                     remark, reqnUserId,distributedQty, returnedCategory, 
                    reqnType, measuringCode,userId,returnedStock,
                    category, projCode, No_Of_Items,itemType,itemsReturned,stockReturned);
					reqn.setReturnedOrderNo(returnOrder);  
           }          
                         
     }catch(Exception er){
             System.out.println("Error RETRIEVING Requisition Record By Id... ->"+er.getMessage());
     }finally{
             closeConnection(con,ps,rs);
     }
  return  reqn ;
}

public ArrayList findDistributionOrdersListforReturn(String filter)
{
	String query = "SELECT a.MTID,a.DORDER_CODE,a.REQN_DESCRIPTION,a.POSTED,a.STATUS,"+
            "a.CUSTOMER_CODE,a.PORDER_CODE,a.TRANS_DATE,a.SHIP_DATE,"+
            "a.FREIGHT_CODE,a.CARRIER_CODE,a.REQ_PERS_IDENTITY,a.APPROVE_OFFICER,a.USERID,a.PROJECT_CODE," +
            "a.REQNID, a.QUANTITY FROM ST_DISTRIBUTION_ORDER a, am_ad_Requisition b " +
            "WHERE a.DORDER_CODE != b.RETURNED_ORDERNO ";
                    
query += filter;  
System.out.println("<<<<findDistributionOrdersListforReturn query: "+query);
	ArrayList list = new ArrayList();
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	
	try {

		con = getConnection();
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

public boolean updateTotalForRequisition(String status,String itemCode,String warehouseCode,int QtyApproved)
{
//System.out.println("QtyApproved >>>> " +QtyApproved+"  itemCode: "+itemCode+"    warehouseCode: "+warehouseCode+"    status: "+status);
String UPDATE_QUERY = "";
   UPDATE_QUERY =  "UPDATE ST_INVENTORY_TOTALS SET BALANCE = BALANCE - "+QtyApproved+", TMP_BALANCE = coalesce(TMP_BALANCE,0) + "+QtyApproved+"  WHERE ITEM_CODE='"+itemCode+"' AND WAREHOUSE_CODE='"+warehouseCode+"'";
Connection con = null;  
PreparedStatement ps = null;    
boolean done = false;
System.out.println("Update arrayy >>>> " +UPDATE_QUERY);
try{
    con = getConnection();
    ps = con.prepareStatement(UPDATE_QUERY);
    done = (ps.executeUpdate() != -1);
    
}catch(Exception er){
        done = false;
        System.out.println("Error UPDATING ALL DISTRIBUTION TABLES FOR REQUISITION... ->"+er.getMessage());
            er.printStackTrace();
    }finally{
            closeConnection(con,ps);
    }
return done;
}

	
	public ArrayList<DistributionOrderItem> findAllDistributionOrderItemDetaillist(String orderCode){

    ArrayList<DistributionOrderItem> records = new ArrayList<DistributionOrderItem>();
    
    String SELECT_QUERY = " SELECT MTID,DORDER_CODE,QUANTITY_REQUEST,UNITPRICE,ADVANCE_PYMT,ITEM_CODE,STOCK_CODE,DESCRIPTION, "+
                          " QUANTITY_DELIVER,QUANTITY_REMAIN,WAREHOUSE_CODE,REQNID,RFID_TAG FROM ST_DISTRIBUTION_ITEM " +
                          " WHERE DORDER_CODE =?";
                          
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try{
            con = getConnection();
            ps = con.prepareStatement(SELECT_QUERY);
            ps.setString(1,orderCode);
            rs = ps.executeQuery();
            while(rs.next())
            {
                    //String invoiceCode = rs.getString("INVOICECODE");
            		String stockCode = rs.getString("STOCK_CODE");
            		String description = rs.getString("DESCRIPTION");
            		String barCode= rs.getString("RFID_TAG");
                    int quantity = rs.getInt("QUANTITY_REQUEST");
                    //double amount = rs.getDouble("AMOUNT");
                    double amount = 0.00;
                    String id = rs.getString("MTID");
                    double unitPrice = rs.getDouble("UNITPRICE");
                    double advancePymt = rs.getDouble("ADVANCE_PYMT");
                    String itemCode = rs.getString("ITEM_CODE");
                    int quantDeliver = rs.getInt("QUANTITY_DELIVER");
                    int quantRemain = rs.getInt("QUANTITY_REMAIN");
                    String warehouseCode = rs.getString("WAREHOUSE_CODE");
                    String reqnID = rs.getString("REQNID");
                    
                    
                DistributionOrderItem invoice = new DistributionOrderItem(id,orderCode,quantity,unitPrice,amount,
                                            advancePymt,itemCode,quantDeliver,quantRemain,warehouseCode);
                invoice.setReqnID(reqnID);
                invoice.setStockCode(stockCode);
                invoice.setDesc(description);
                invoice.setBarcode(barCode);
                    records.add(invoice);
            }

    }catch(Exception er){
            System.out.println("Error finding All findAllDistributionOrderItemDetail...->"+er.getMessage());
    }finally{
            closeConnection(con,ps,rs);
    }

    return records ;
}

public boolean ApprovalStatusUpdateforMultiple(String reqnID,String status,String distributionStatus,String postingDate, String userID)
{
    Connection con;
    PreparedStatement ps;
    PreparedStatement ps1;
    boolean done;
    String UPDATE_QUERY = "UPDATE am_ad_Requisition SET Status = ?,distributedstatus = ?  WHERE ReqnID=? ";
    String UPDATE_QUERY2 = "UPDATE am_asset_approval SET process_status = ?,DATE_APPROVED = ?  WHERE asset_id=? AND SUPER_ID = ?";
    con = null;
    ps = null;
    ps1 = null;
    done = false;
    System.out.println("UPDATING APPROVAL in ApprovalStatusUpdate");
    try
    {
        con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);
        ps.setString(1, status);
        ps.setString(2, distributionStatus);
        ps.setString(3, reqnID);
        done = ps.executeUpdate() != -1;

        ps1 = con.prepareStatement(UPDATE_QUERY2);
        ps1.setString(1, status);
        ps1.setString(2, postingDate);
        ps1.setString(3, reqnID);
        ps1.setString(4, userID);
        done = ps1.executeUpdate() != -1;        
    }
    catch(Exception er){
        done = false;
        System.out.println("Error UPDATING APPROVAL in ApprovalStatusUpdate... ->"+er.getMessage());
        er.printStackTrace();
		}finally{
		        closeConnection(con,ps);
		        closeConnection(con,ps1);
		}       
    return done;
}

public ArrayList findStockDistributionOrdersList(String filter)
{
	String query = "SELECT DISTINCT MTID,DORDER_CODE,REQN_DESCRIPTION,POSTED,a.STATUS,"+
            "CUSTOMER_CODE,PORDER_CODE,TRANS_DATE,SHIP_DATE,"+
            "FREIGHT_CODE,CARRIER_CODE,REQ_PERS_IDENTITY,APPROVE_OFFICER,b.transferby_id AS USERID,a.PROJECT_CODE," +
            "a.REQNID, a.QUANTITY FROM ST_DISTRIBUTION_ORDER a, am_ad_TransferRequisition b ";
                     
query += filter;
System.out.println("<<<<findDistributionOrdersList query: "+query);
	ArrayList list = new ArrayList();
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	
	try {

		con = getConnection();
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

	

}
