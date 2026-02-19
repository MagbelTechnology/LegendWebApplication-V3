/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magbel.legend.bus;

/**
 *
 * @author Lekan Matanmi
 */
import magma.net.dao.MagmaDBConnection;

import com.magbel.ia.vao.Stock;
import com.magbel.legend.vao.ApprovalRemark;
import com.magbel.legend.vao.RFID;
import com.magbel.legend.vao.ViewVendorTransactionDetails;
import com.magbel.legend.bus.ApprovalRecords;

import legend.*;

import java.sql.*;

import com.magbel.util.DatetimeFormat;

import java.util.ArrayList;
import java.util.Date;

import com.magbel.legend.vao.ViewAssetDetails;
public class ApprovalManager extends ConnectionClass {

	public ApprovalRecords approvalRec;
    private MagmaDBConnection dbConnection;
    private DatetimeFormat dateFormat;
    private java.text.SimpleDateFormat sdf;
    private java.text.SimpleDateFormat timer;
    com.magbel.util.DatetimeFormat df;

    public ApprovalManager() {

        try {
        	System.out.println("Instantiating the ApprovalManager.....");
            freeResource();
            sdf = new java.text.SimpleDateFormat("dd-MM-yyyy hh:mm:ss.SSS");
            timer = new java.text.SimpleDateFormat("hh:mm:ss");
            dbConnection = new MagmaDBConnection();
            dateFormat = new DatetimeFormat();
            df = new com.magbel.util.DatetimeFormat();
            approvalRec = new ApprovalRecords(); 
        } catch (Exception ex) {
        	
        }
    }

    public boolean createApprovalRemark(ApprovalRemark remark) {

        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;
        String query = "  insert into am_approval_remark(asset_id,supervisorID,Remark,status," +
                "ApprovalLevel,system_ip,transaction_id,RemarkDate," +
                "DateRequisitioned,tran_Type)  values(?,?,?,?,?,?,?,?,?,?)";

        try
        {
            
           con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            ps.setString(1, remark.getAssetId());
            ps.setInt(2, remark.getSupervisorID());
            //ps.setTimestamp(3,dbConnection.getDateTime(getPostingTime(remark.getTransactionId())));
            ps.setString(3, remark.getRemark());
            ps.setString(4, remark.getStatus());
            ps.setInt(5, remark.getApprovalLevel());
            ps.setString(6, remark.getIPAddress());
            ps.setLong(7, remark.getTransactionId());
            ps.setTimestamp(8, dbConnection.getDateTime(new java.util.Date()));
            ps.setTimestamp(9,getPostingTime(remark.getTransactionId()));
            ps.setString(10,remark.getTranType());
            ps.execute();
            done = true;

        } catch (Exception e) {
            done = false;
            e.printStackTrace();
            System.out.println("WARNING:Error in  createApprovalRemark ->" + e.getMessage());
        } finally {
            dbConnection.closeConnection(con, ps);
            //closeConnection(con, ps);
        }
        return done;

    }
  
    public boolean createInitialApprovalRemark(ApprovalRemark remark) {

        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;
        String query = "  insert into am_approval_remark(asset_id,supervisorID,DateRequisitioned ,transaction_id)  values(?,?,?,?)";


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            ps.setString(1, remark.getAssetId());
            ps.setInt(2, remark.getSupervisorID());
            ps.setDate(3, dbConnection.dateConvert(new java.util.Date()));
            ps.setLong(4, remark.getTransactionId());

            ps.execute();
            done = true;

        } catch (Exception e) {
            done = false;
            e.printStackTrace();
            System.out.println("WARNING:Error in  createInitialApprovalRemark ->" + e.getMessage());
        } finally {
            dbConnection.closeConnection(con, ps);
            //closeConnection(con, ps);
        }
        return done;

    }

    public boolean updateApprovalRemark(String remark, String status, String ip_address, int approvalLevel, int tranId, String assetId, int superId) {
        boolean done = true;

        Connection con = null;
        PreparedStatement ps = null;
        String query = "update am_approval_remark set remark=?,status=? ,system_ip=?,ApprovalLevel=?,RemarkDate=? where transactionId=? and asset_id=? and supervisorid=? ";

        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            ps.setString(1, remark);
            ps.setString(2, status);
            ps.setString(3, ip_address);
            ps.setInt(4, approvalLevel);
            ps.setDate(5, dbConnection.dateConvert(new java.util.Date()));
            ps.setInt(6, tranId);
            ps.setString(7, assetId);
            ps.setInt(8, superId);

            ps.execute();

        } catch (Exception ex) {
            done = false;
            System.out.println("WARNING:Error occurred in updateApprovalRemark ->");
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps);
        }
        return done;
    }


    public java.sql.Timestamp getPostingTime(long tranId) {

        String query =
                "select posting_date from am_asset_approval where transaction_id=?";
        java.sql.Timestamp postingtime = null;
        Connection con = null;
       PreparedStatement ps = null;
       ResultSet rs = null;

        try {
            con = dbConnection.getConnection("legendPlus");
                       ps = con.prepareStatement(query);
                       ps.setLong(1, tranId);
                       rs = ps.executeQuery();

            while (rs.next()) {

                postingtime = rs.getTimestamp("posting_date") ;

            }

        } catch (Exception ex) {
            System.out.println("Error occurred in getPostingTime --> ");
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }

        return postingtime;

    }


     public ArrayList getApprovalRemarks_old(int tranId,String filter) {
               ApprovalRemark ar = null;
            ArrayList list = new ArrayList();
            int supervisor =0;
            int approvelevel =0;
            String sentDate ="";
            String approveDate="";
            String remarks="";
            String status="";
        String query =
                "select supervisorID,DateRequisitioned,Remark,status,ApprovalLevel,RemarkDate from am_approval_remark where transaction_id=? "+ filter;
      //  java.sql.Timestamp postingtime = null;
        Connection con = null;
       PreparedStatement ps = null;
       ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
                       ps = con.prepareStatement(query);
                       ps.setInt(1, tranId);
                       rs = ps.executeQuery();

            while (rs.next()) {
                supervisor = rs.getInt(1);
                sentDate=dbConnection.formatDate(rs.getDate(2));
                remarks = rs.getString(3);
                status = rs.getString(4);
                approvelevel = rs.getInt(5);
                approveDate = dbConnection.formatDate(rs.getDate(6));


            ar = new ApprovalRemark(supervisor,approvelevel,sentDate,approveDate,remarks,status);
            list.add(ar);
               // postingtime = rs.getTimestamp("posting_date") ;

            }

        } catch (Exception ex) {
            System.out.println("Error occurred in getPostingTime --> ");
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }

        return list;

    }


public ArrayList getApprovalRemarks(String assetId,String tranType,String filter) {
               ApprovalRemark ar = null;
            ArrayList list = new ArrayList();
            int supervisor =0;
            int approvelevel =0;
            String sentDate ="";
            String approveDate="";
            String remarks="";
            String status="";

               if(tranType.equalsIgnoreCase("Facility Mgt Acceptance")){
            //--assetId = appRecords.getCodeName("select reqnid from FM_MAITENANCE_DUE where acceptance_code='"+assetId+"'");
            tranType="Facility Mgt Requisition";

            }

        String query =
                "select supervisorID,DateRequisitioned,Remark,status,ApprovalLevel,RemarkDate from am_approval_remark where asset_id=? and tran_Type=? "+ filter;
      //  java.sql.Timestamp postingtime = null;
        Connection con = null;
       PreparedStatement ps = null;
       ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
                       ps = con.prepareStatement(query);
                       ps.setString(1, assetId);
                       ps.setString(2, tranType);
                       rs = ps.executeQuery();

            while (rs.next()) {
                supervisor = rs.getInt(1);
                sentDate=dbConnection.formatDate(rs.getDate(2));
                remarks = rs.getString(3);
                status = rs.getString(4);
                approvelevel = rs.getInt(5);
                approveDate = dbConnection.formatDate(rs.getDate(6));


            ar = new ApprovalRemark(supervisor,approvelevel,sentDate,approveDate,remarks,status);
            list.add(ar);
               // postingtime = rs.getTimestamp("posting_date") ;

            }

        } catch (Exception ex) {
            System.out.println("Error occurred in getPostingTime --> ");
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }

        return list;

    }


public String[] remarkInfoFromApproval(String assetId) {
            
            String[] data =new String[4];
            
           
        String query =
                "select transaction_id,posting_date,tran_type,transaction_level from am_asset_approval where asset_id=?";

        Connection con = null;
       PreparedStatement ps = null;
       ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
                       ps = con.prepareStatement(query);
                       ps.setString(1, assetId);
                     
                       rs = ps.executeQuery();

            while (rs.next()) {
                data[0] = rs.getString(1);
                data[1]=rs.getString(2);
                data[2] = rs.getString(3);
                data[3] = rs.getString(4); 

               // postingtime = rs.getTimestamp("posting_date") ;

            }

        } catch (Exception ex) {
            System.out.println("Error occurred in remarkInfoFromApproval --> ");
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }

        return data;

    }



public String[] remarkInfoFromPostedEntry(String assetId) {
            
            String[] data =new String[2];
            
           
        String query =
                "select User_id,system_ip from am_Raisentry_Transaction where asset_id=?";

        Connection con = null;
       PreparedStatement ps = null;
       ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
                       ps = con.prepareStatement(query);
                       ps.setString(1, assetId);
                     
                       rs = ps.executeQuery();

            while (rs.next()) {
                data[0] = rs.getString(1);
                data[1] = rs.getString(2);

               // postingtime = rs.getTimestamp("posting_date") ;

            }

        } catch (Exception ex) {
            System.out.println("Error occurred in remarkInfoFromPostedEntry --> ");
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }

        return data;

    }



public void infoFromApproval(String assetId,String page) {
            
            int tranId = 0;
            java.sql.Date postDate=null;
            String tranType ="";
            int tranLevel = 0;
            int userId= 0;
            String systemIp="";
            
           
        String query =
                "select distinct(a.transaction_id),a.posting_date,a.tran_type,a.transaction_level,b.User_id,b.system_ip "+
"from am_asset_approval a, am_Raisentry_Transaction b where a.asset_id= b.asset_id and a.asset_id=? and b.page1=?";

        String query1 = "insert into am_approval_remark (asset_id,supervisorID,DateRequisitioned,status,ApprovalLevel,RemarkDate,system_ip,transaction_id,tran_Type)"+
        
                " values(?,?,?,?,?,?,?,?,?)";

                Connection con = null;
       PreparedStatement ps = null;
       ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
                       ps = con.prepareStatement(query);
                       ps.setString(1, assetId);
                       ps.setString(2, page);
                       rs = ps.executeQuery();

            while (rs.next()) {
                tranId = rs.getInt(1);
                postDate=rs.getDate(2);
                tranType = rs.getString(3);
                tranLevel = rs.getInt(4); 
                userId = rs.getInt(5);
                systemIp=rs.getString(6);

              // postingtime = rs.getTimestamp("posting_date") ;
            }

               System.out.println("the >>>>>> transaction id retrieved is " +tranId);
             ps=con.prepareStatement(query1);         
             ps.setString(1,assetId);
             ps.setInt(2,userId);
             ps.setDate(3,postDate);
             ps.setString(4,"Approved");
             ps.setInt(5,tranLevel + 1);
             ps.setDate(6,dbConnection.dateConvert(new java.util.Date()));
             ps.setString(7,systemIp);
             ps.setInt(8,tranId);
             ps.setString(9,tranType);
             //ps.setString();
             
             ps.execute();          

        } catch (Exception ex) {
            System.out.println("Error occurred in InfoFromApproval --> ");
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }

       

    }


public void infoFromRejection(int tranId,int userId,String systemIp,String rejectReason) {

           // int tranId = 0;
            java.sql.Date postDate=null;
            String tranType ="";
            int tranLevel = 0;
           // int userId= 0;
           // String systemIp="";
           String assetId="";

        String query =
                "select posting_date,tran_type,transaction_level,asset_id "+
                "from am_asset_approval  where transaction_id=?";

        String query1 = "insert into am_approval_remark (asset_id,supervisorID,DateRequisitioned,status,ApprovalLevel,RemarkDate,system_ip,transaction_id,tran_Type,Remark)"+

                " values(?,?,?,?,?,?,?,?,?,?)";

                Connection con = null;
       PreparedStatement ps = null;
       ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
                       ps = con.prepareStatement(query);
                       ps.setInt(1, tranId);
                      // ps.setString(2, page);
                       rs = ps.executeQuery();

            while (rs.next()) {
                //tranId = rs.getInt(1);
                postDate=rs.getDate(1);
                tranType = rs.getString(2);
                tranLevel = rs.getInt(3);
                assetId= rs.getString(4);
                //userId = rs.getInt(5);
                //systemIp=rs.getString(6);

              // postingtime = rs.getTimestamp("posting_date") ;
            }

//               System.out.println("the >>>>>> transaction id retrieved is " +tranId);
             ps=con.prepareStatement(query1);
             ps.setString(1,assetId);
             ps.setInt(2,userId);
             ps.setDate(3,postDate);
             ps.setString(4,"Rejected");
             ps.setInt(5,tranLevel + 1);
             //ps.setDate(6,dbConnection.dateConvert(new java.util.Date()));
             ps.setTimestamp(6, dbConnection.getDateTime(new java.util.Date()));
             ps.setString(7,systemIp);
             ps.setInt(8,tranId);
             ps.setString(9,tranType);
             ps.setString(10,rejectReason);

             ps.execute();

        } catch (Exception ex) {
            System.out.println("Error occurred in InfoFromApproval --> ");
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }



    }

public ArrayList getViewAssetDetails(String assetCode)
{
 ViewAssetDetails view = null;
 ArrayList list = new ArrayList();

Connection con = null;
PreparedStatement ps = null;
ResultSet rs = null;
String assetId ="";
String description="";
String costPrice="";
String transactionDt="";
String transactionType ="";
String assetCodeObject="";
//System.out.println("=======getQuery(): "+getQuery());
try {
 con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(getQuery());
            ps.setString(1, assetCode);
            ps.setString(2, assetCode);
            ps.setString(3, assetCode);
            ps.setString(4, assetCode);
            rs = ps.executeQuery();

 while (rs.next())
 {
	 assetId =rs.getString("asset_id");
	 description=rs.getString("description");
	 costPrice=rs.getString("CostPrice");
	 transactionDt=rs.getString("TransactionDt");
	 transactionType =rs.getString("Transaction_Type");
     view = new ViewAssetDetails(assetId,description,costPrice,transactionDt,transactionType,assetCode);
     list.add(view);

 }

} catch (Exception ex) {
 ex.printStackTrace();
} finally {
 dbConnection.closeConnection(con, ps, rs);
} 

return list;

}


public ArrayList getViewVendorTransctionDetails(String vendorCode)
{
 ViewVendorTransactionDetails view = null;
 ArrayList list = new ArrayList();

Connection con = null;
PreparedStatement ps = null;
ResultSet rs = null;
String tranId ="";
String description="";
String costPrice="";
String transactionDt="";
String transactionType ="";
String location = "";
String draccountNo = "";
String craccountNo = "";
String projectCode = "";

try {
 con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(getVendorTransactionQuery());
            ps.setString(1, vendorCode);
 /*           ps.setString(2, vendorCode);
            ps.setString(3, vendorCode);
            ps.setString(4, vendorCode);
            ps.setString(5, vendorCode);
            ps.setString(6, vendorCode);
            ps.setString(7, vendorCode);
            ps.setString(8, vendorCode);
            ps.setString(9, vendorCode);
            ps.setString(10, vendorCode);*/
            rs = ps.executeQuery();
System.out.println("<<<<<<<vendorCode: "+vendorCode);
 while (rs.next())
 {
	 tranId =rs.getString("TRANS_ID");
	 description=rs.getString("DESCRIPTION");
	 costPrice=rs.getString("COST_PRICE");
	 transactionDt=rs.getString("TRANSACTION_DATE");
	 transactionType =rs.getString("TRANSACTION_TYPE");
	 location = rs.getString("LOCATION");
	 draccountNo = rs.getString("DRACCOUNT_NO");
	 craccountNo = rs.getString("CRACCOUNT_NO");
	 vendorCode = rs.getString("VENDOR_CODE");
	 projectCode = rs.getString("PROJECT_CODE");
     view = new ViewVendorTransactionDetails(tranId,description,costPrice,transactionDt,transactionType,draccountNo,craccountNo,vendorCode,location,projectCode);
     list.add(view);

 }

} catch (Exception ex) {
 ex.printStackTrace();
} finally {
 dbConnection.closeConnection(con, ps, rs);
} 

return list;

}

public String getQuery()
 {

			//Improvement Query
	String query=" select m.asset_id,m.description,m.new_cost_price CostPrice,m.effdate TransactionDt, "
				+" 'Asset Improvement' 'Transaction_Type' ,m.asset_code"
				+" from  am_asset a, am_asset_improvement m"
				+" where ((m.Asset_code = a.Asset_code)and a.asset_code =?)"
				+" and m.approval_status = 'Active'"


				+" union all"
//Transfer Query
				+" select t.new_asset_id, t.description+' - Old Asset Id - '+t.asset_id,t.cost_price CostPrice,t.effdate TransactionDt,"
				+" 'Asset Transfer' 'Transaction Type',t.asset_code"
				+" from  am_asset a, am_assettransfer t"
				+" where ((t.Asset_code = a.Asset_code)and a.asset_code =?)"
				+" and t.approval_status = 'Active'"

				+" union all"
//Asset Reclassification
				+" select  r.new_asset_id, r.description,r.cost_price CostPrice,r.reclassify_date TransactionDt,"
				+" 'Asset Reclassification' 'Transaction Type', r.asset_code"
				+" from  am_asset a, dbo.am_assetReclassification r"
				+" where ((r.Asset_code = a.Asset_code)and a.asset_code =?)"

				+" union all"
//Wip Reclassification
				+" select w.asset_id, a.description,a.cost_price CostPrice,w.Transfer_date TransactionDt,"
				+" 'WIP Reclassification' 'Transaction Type',w.asset_code"
				+" from  am_asset a, dbo.AM_WIP_RECLASSIFICATION w"
				+" where ((w.Asset_code = a.Asset_code)and a.asset_code =?)"
				+" order by m.effdate";
    return query;
 }

public boolean createInventoryTotal(String compCode, String inventCode, String warehouseCode,  int quantity, int userid,int branchId) {

    Connection con = null;
    PreparedStatement ps = null;
    boolean done = false;
    String query = "  insert into ST_INVENTORY_TOTALS(COMP_CODE,WAREHOUSE_CODE,ITEM_CODE,BALANCE,STATUS," +
            "USERID,BRANCH_ID)  values(?,?,?,?,?,?,?)";
    System.out.println("<<<<<<<<<<<<<compCode in inserting record ApprovalManager: "+compCode+"  inventCode: "+inventCode+"   warehouseCode: "+warehouseCode+"  quantity: "+quantity+"  userid: "+userid+"  branchId: "+branchId);
    try
    {
        
       con = dbConnection.getConnection("legendPlus");
        ps = con.prepareStatement(query);
        ps.setString(1, compCode);
        ps.setString(2, warehouseCode);
        ps.setString(3, inventCode);
        ps.setInt(4, quantity);
        ps.setString(5, "ACTIVE");
        ps.setInt(6, userid);
        ps.setInt(7, branchId);
        System.out.println("<<<<<<warehouseCode in createInventoryTotal: "+warehouseCode+"   inventCode: "+inventCode+"   quantity: "+quantity+"     branchId: "+branchId); 
        ps.execute();
        done = true;

    } catch (Exception e) {
        done = false;
        e.printStackTrace();
        System.out.println("WARNING:Error in  createInventoryTotal ->" + e.getMessage());
    } finally {
        dbConnection.closeConnection(con, ps);
        //closeConnection(con, ps);
    }
    return done;

}

public boolean createInventoryTotalAddition(String compCode, String inventCode, String warehouseCode,  int quantity, int userid,int branchId) {

    Connection con = null;
    PreparedStatement ps = null;
    boolean done = false;
    String query = "  insert into ST_INVENTORY_TOTALS(COMP_CODE,WAREHOUSE_CODE,ITEM_CODE,BALANCE,STATUS," +
            "USERID,BRANCH_ID)  values(?,?,?,?,?,?,?)";
    System.out.println("<<<<<<<<<<<<<compCode: "+compCode+"  inventCode: "+inventCode+"   warehouseCode: "+warehouseCode+"  quantity: "+quantity+"  userid: "+userid+"  branchId: "+branchId);
    try
    {
        
       con = dbConnection.getConnection("legendPlus");
        ps = con.prepareStatement(query);
        ps.setString(1, compCode);
        ps.setString(2, warehouseCode);
        ps.setString(3, inventCode);
        ps.setInt(4, quantity);
        ps.setString(5, "ACTIVE");
        ps.setInt(6, userid);
        ps.setInt(7, branchId);
  //      System.out.println("<<<<<<warehouseCode in createInventoryTotal: "+warehouseCode+"   inventCode: "+inventCode+"   quantity: "+quantity+"     branchId: "+branchId); 
        ps.execute();
        done = true;

    } catch (Exception e) {
        done = false;
        e.printStackTrace();
        System.out.println("WARNING:Error in  createInventoryTotal ->" + e.getMessage());
    } finally {
        dbConnection.closeConnection(con, ps);
        //closeConnection(con, ps);
    }
    return done;

}


public boolean createInventoryTotalSubtraction(String compCode, String inventCode, String warehouseCode,  int quantity, int userid,int branchId) {

    Connection con = null;
    PreparedStatement ps = null;
    boolean done = false;
    String query = "  insert into ST_INVENTORY_TOTALS(COMP_CODE,WAREHOUSE_CODE,ITEM_CODE,BALANCE,STATUS," +
            "USERID,BRANCH_ID)  values(?,?,?,?,?,?,?)";
    System.out.println("<<<<<<<<<<<<<compCode: "+compCode+"  inventCode: "+inventCode+"   warehouseCode: "+warehouseCode+"  quantity: "+quantity+"  userid: "+userid+"  branchId: "+branchId);
    try
    {
    	quantity = 0 - quantity;
       con = dbConnection.getConnection("legendPlus");
        ps = con.prepareStatement(query);
        ps.setString(1, compCode);
        ps.setString(2, warehouseCode);
        ps.setString(3, inventCode);
        ps.setInt(4, quantity);
        ps.setString(5, "ACTIVE");
        ps.setInt(6, userid);
        ps.setInt(7, branchId);
        System.out.println("<<<<<<warehouseCode in createInventoryTotal: "+warehouseCode+"   inventCode: "+inventCode+"   quantity: "+quantity+"     branchId: "+branchId); 
        ps.execute();
        done = true;

    } catch (Exception e) {
        done = false;
        e.printStackTrace();
        System.out.println("WARNING:Error in  createInventoryTotal ->" + e.getMessage());
    } finally {
        dbConnection.closeConnection(con, ps);
        //closeConnection(con, ps);
    }
    return done;

}

public boolean updateInventoryTotal(String compCode, String inventCode, String warehouseCode,  int quantity,int branchId) {
    boolean done = true;

    Connection con = null;
    PreparedStatement ps = null;
    String query = "update ST_INVENTORY_TOTALS set BALANCE= BALANCE + ? where COMP_CODE=? AND WAREHOUSE_CODE=?  AND ITEM_CODE=? AND BRANCH_ID = ? ";
System.out.println("compCode in updateInventoryTotal: "+compCode+"  inventCode: "+inventCode+"  warehouseCode: "+warehouseCode+"    quantity: "+quantity+"   branchId: "+branchId);
    try {
        con = dbConnection.getConnection("legendPlus");
        ps = con.prepareStatement(query); 
        ps.setInt(1, quantity);
        ps.setString(2, compCode);
        ps.setString(3, warehouseCode);
        ps.setString(4, inventCode); 
        ps.setInt(5, branchId);
        ps.execute();

    } catch (Exception ex) {
        done = false;
        System.out.println("WARNING:Error occurred in ST_INVENTORY_TOTALS Update ->");
        ex.printStackTrace();
    } finally {
        dbConnection.closeConnection(con, ps);
    }
    return done;
}

public ArrayList findRFIDByQuery(String filter) {
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
       String stockDescription = "";
   String query =
           "select MTID,RFID_TAG,LOCATION,SCANN_TYPE,SCANN_STATUS,USER_ID,CREATE_DATE,CREATE_DATETIME,STOCK_DESCRIPTION from ST_INVENTORY_RFID where MTID IS NOT NULL "+ filter;
   Connection con = null;
  PreparedStatement ps = null;
  ResultSet rs = null;
  //System.out.println("<<<<query in findRFIDByQuery: "+query);
   try {
       con = dbConnection.getConnection("legendPlus");
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
           stockDescription = rs.getString(9);
           
       ar = new RFID(rfidTag,mtid,createDate, location, scannstatus, createDateTime, scannType, userId,stockDescription,"");
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


public ArrayList findUnusedRFIDByQuery(String filter) {
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
           "select MTID,RFID_TAG,LOCATION,SCANN_TYPE,SCANN_STATUS,USER_ID,CREATE_DATE,CREATE_DATETIME from ST_INVENTORY_RFID_UNUSED where MTID IS NOT NULL "+ filter;
   Connection con = null;
  PreparedStatement ps = null;
  ResultSet rs = null;

   try {
       con = dbConnection.getConnection("legendPlus");
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

public ArrayList findNewRFIDByQuery(String filter) {
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
   String query =
    "select DISTINCT e.MTID,e.RFID_TAG,e.LOCATION,e.SCANN_TYPE,e.SCANN_STATUS,e.USER_ID,e.CREATE_DATE,e.CREATE_DATETIME,c.QUANTITY_DELIVER AS QUANTITY,a.ITEM_CODE,a.ItemType,a.Description,a.UNIT_CODE  "
    + " from ST_STOCK a,ST_DISTRIBUTION_ITEM c, am_ad_Requisition d,ST_INVENTORY_MATERIAL_USED e "
    + "where a.BAR_CODE = e.RFID_TAG and a.Asset_id = c.STOCK_CODE and c.REQNID = d.ReqnID AND a.BAR_CODE = e.RFID_TAG  "+ filter;
   Connection con = null;
  PreparedStatement ps = null;
  ResultSet rs = null;
  System.out.println("<<<<query in findNewRFIDByQuery: "+query);
   try {
       con = dbConnection.getConnection("legendPlus");
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
           quantity = rs.getInt(9);
           itemCode = rs.getString(10);
           itemType = rs.getString(11);
           description = rs.getString(12);
           unitCode = rs.getString(13);
           
       ar = new RFID(rfidTag,mtid,createDate, location, scannstatus, createDateTime, scannType, userId,description,"");
       ar.setQuantity(quantity);
       ar.setItemCode(itemCode);
       ar.setItemType(itemType);
       ar.setUnitCode(unitCode);
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

public boolean createApprovalRFIDTagList(String rfidtag,int Supervisor,String createDate,String description,String groupId,String scanstatus,String location) {
	createDate = String.valueOf(df.dateConvert(new java.util.Date()));
Timestamp CreateTime = dbConnection.getDateTime(new java.util.Date());
String usedBy = "";
int quantity = 0;
int quantitybal = 0;
String faultId = "";
	String query = "INSERT INTO ST_INVENTORY_RFID_APPROVED SELECT MTID,USER_ID,RFID_TAG,'"+location+"',SCANN_TYPE,'"+scanstatus+"',"+Supervisor+",'"+createDate+"','"+CreateTime+"','"+description+"','"+groupId+"','"+usedBy+"',"+quantity+","+quantitybal+",'"+faultId+"'   FROM ST_INVENTORY_NEW_RFID WHERE RFID_TAG =?";
System.out.println("<<<<<<<<===query in createRFIDTagList: "+query);
	Connection con = null;
PreparedStatement ps = null;
boolean done = false;
  
try {
con = getConnection();
ps = con.prepareStatement(query);
ps.setString(1,rfidtag);

done = (ps.executeUpdate() != -1);
} catch (Exception ex) {
done = false;
System.out.println("ERROR Creating RFID Tag Records " + ex.getMessage());
ex.printStackTrace();
} finally {
	dbConnection.closeConnection(con, ps);
}
return done;
}

public boolean createApprovalRFIDTagList(String rfidtag,int Supervisor,String createDate,String description,String groupId,String scanstatus) {
	createDate = String.valueOf(df.dateConvert(new java.util.Date()));
Timestamp CreateTime = dbConnection.getDateTime(new java.util.Date());
String usedBy = "";
int quantity = 0;
int quantitybal = 0;
String faultId = "";
	String query = "INSERT INTO ST_INVENTORY_RFID_APPROVED SELECT MTID,USER_ID,RFID_TAG,LOCATION,SCANN_TYPE,'"+scanstatus+"',"+Supervisor+",'"+createDate+"','"+CreateTime+"','"+description+"','"+groupId+"','"+usedBy+"',"+quantity+","+quantitybal+",'"+faultId+"'   FROM ST_INVENTORY_NEW_RFID WHERE RFID_TAG =?";
//System.out.println("<<<<<<<<===query in createRFIDTagList: "+query);
	Connection con = null;
PreparedStatement ps = null;
boolean done = false;
  
try {
con = getConnection();
ps = con.prepareStatement(query);
ps.setString(1,rfidtag);

done = (ps.executeUpdate() != -1);
} catch (Exception ex) {
done = false;
System.out.println("ERROR Creating RFID Tag Records " + ex.getMessage());
ex.printStackTrace();
} finally {
	dbConnection.closeConnection(con, ps);
}
return done;
}

private String substring(String createDate, int i, int j) {
	// TODO Auto-generated method stub
	return null;
}

public boolean approvalofRFIDTagList(String groupId,String Supervisor,String createDate,String userID,String description,String branchCode,String apprioveId,int level,String tranType) {
//Timestamp CreateTime = dbConnection.getDateTime(new java.util.Date());
//System.out.println("<<<<<<<<===Supervisor in createRFIDTagList: "+Supervisor+"    approvDate: "+createDate);
System.out.println("<<<<<<<<===groupId in approvalofRFIDTagList: "+groupId+"    userID: "+userID+"  description:"+description+"   branchCode: "+branchCode);
    String ins_am_asset_approval_qry = "insert into am_asset_approval(asset_id,user_id,super_id,posting_date,description" +
",effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time" +
",transaction_id,batch_id,transaction_level) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
;
	String status = "P";
	String reqnStatus = "U";
Connection con = null;
PreparedStatement ps = null;
boolean done = false;
String []spuervisorlist = Supervisor.split("#");
int No = spuervisorlist.length;
try {
	
	con = getConnection();
	ps = con.prepareStatement(ins_am_asset_approval_qry);
	for(int j=0;j<No;j++){
    ps.setString(1,groupId);
    ps.setString(2, userID);
    ps.setString(3, spuervisorlist[j]);
    ps.setTimestamp(4, dbConnection.getDateTime(new Date()));
    ps.setString(5, description);
    ps.setTimestamp(6, dbConnection.getDateTime(new Date()));
    ps.setString(7, branchCode);
    ps.setString(8, reqnStatus);
    ps.setString(9, tranType);
    ps.setString(10, status);
    ps.setString(11, timer.format(new Date()));
    ps.setString(12, apprioveId);
    ps.setString(13, apprioveId);
    ps.setInt(14, level);
    //done = ps.executeUpdate() == -1;
    ps.execute();
    done = true;
  //  System.out.println("<<<<<<done Identifiaction>>>>>>>: "+done);
}
} catch (Exception ex) {
done = false;
System.out.println("ERROR Creating RFID Tag Records " + ex.getMessage());
ex.printStackTrace();
} finally {
	dbConnection.closeConnection(con, ps);
}
return done;
}

public ArrayList findApprovalForNewRFIDByQuery(String filter) {
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
       String description = "";
       String groupid = "";
       int branchId = 0;
       int deptId = 0;
   String query =
           "select MTID,RFID_TAG,LOCATION,SCANN_TYPE,SCANN_STATUS,USER_ID,CREATE_DATE,CREATE_DATETIME,REMARK,GROUP_ID,branchId,deptId from ST_INVENTORY_RFID_APPROVED where MTID IS NOT NULL "+ filter;
   Connection con = null;
  PreparedStatement ps = null;
  ResultSet rs = null;
 // System.out.println("query in findApprovalForNewRFIDByQuery: "+query);
   try {
       con = dbConnection.getConnection("legendPlus");
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
           description = rs.getString(9);
           groupid = rs.getString(10);
           branchId = rs.getInt(11);
           deptId = rs.getInt(12);
           System.out.println("groupid in findApprovalForNewRFIDByQuery: "+groupid);
           
       ar = new RFID(rfidTag,mtid,createDate, location, scannstatus, createDateTime, scannType, userId,description,groupid);
       ar.setBranchId(branchId);
       ar.setDeptId(deptId);
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

public boolean AcceptRfidTag(String groupId, String rfidtag,String ReqnID,int recordNo) {
    boolean done = true;

    Connection con = null;
    PreparedStatement ps = null;
    PreparedStatement ps1 = null; 
    PreparedStatement ps2 = null;
    String query = "UPDATE ST_INVENTORY_RFID_APPROVED SET SCANN_STATUS= ? where RFID_TAG = ? ";
    String query2 = "UPDATE ST_INVENTORY_NEW_RFID SET SCANN_STATUS= ? where RFID_TAG = ? ";
    String query3 = "UPDATE am_asset_approval SET PROCESS_STATUS= 'A', ASSET_STATUS = 'APPROVED' where ASSET_ID = '"+groupId+"' ";
//System.out.println("query in AcceptRfidTag: "+query);
//System.out.println("query2 in AcceptRfidTag: "+query2);
//System.out.println("query3 in AcceptRfidTag: "+query3);
    try {
        con = dbConnection.getConnection("legendPlus");
        ps = con.prepareStatement(query);
        ps.setString(1, "A");
        ps.setString(2, rfidtag);
        ps.execute();

        ps1 = con.prepareStatement(query2);
        ps1.setString(1, "A");
        ps1.setString(2, rfidtag);
        ps1.execute();
        
 //       if((recordNo==1)||(recordNo==1)){
        ps2 = con.prepareStatement(query3);
  //      ps2.setString(1, "A");
  //      ps2.setString(2, groupId);
        ps2.execute();
//        }
        
    } catch (Exception ex) {
        done = false;
        System.out.println("WARNING:Error occurred in RFID TAG TABLES Update ->");
        ex.printStackTrace();
    } finally {
        dbConnection.closeConnection(con, ps);
        dbConnection.closeConnection(con, ps1);
        if((recordNo==1)||(recordNo==1)){
        dbConnection.closeConnection(con, ps2);
        }
    }
    return done;
}

public boolean RejectRfidTagTables(String groupId, String rfidtag,String ReqnID,int recordNo) {
    boolean done = true;

    Connection con = null;
    PreparedStatement ps = null;
    PreparedStatement ps1 = null;
    PreparedStatement ps2 = null;
    String query = "UPDATE ST_INVENTORY_RFID_APPROVED SET SCANN_STATUS= ? where RFID_TAG = ? ";
    String query2 = "UPDATE ST_INVENTORY_NEW_RFID SET SCANN_STATUS= ? where RFID_TAG = ? ";
    String query3 = "UPDATE am_asset_approval SET PROCESS_STATUS= ? where ASSET_ID = ? ";

    try {
        con = dbConnection.getConnection("legendPlus");
        ps = con.prepareStatement(query);
        ps.setString(1, "R");
        ps.setString(2, rfidtag);
        ps.execute();

        ps1 = con.prepareStatement(query2);
        ps1.setString(1, "I");
        ps1.setString(2, rfidtag);
        ps1.execute();
        
  //      if((recordNo==1)||(recordNo==1)||(recordNo==0)){
        ps2 = con.prepareStatement(query3);
        ps2.setString(1, "R");
        ps2.setString(2, ReqnID);
        ps2.execute();
  //      }
    } catch (Exception ex) {
        done = false;
        System.out.println("WARNING:Error occurred in RFID TAG TABLES Update ->");
        ex.printStackTrace();
    } finally {
        dbConnection.closeConnection(con, ps);
        dbConnection.closeConnection(con, ps1);
        if((recordNo==1)||(recordNo==1)){
        dbConnection.closeConnection(con, ps2);
        }
    }
    return done;
}

public boolean AttachedRfidTagToStock(String rfidtag) {
    boolean done = true;

    Connection con = null;
    PreparedStatement ps = null;
    PreparedStatement ps1 = null;
    PreparedStatement ps2 = null;
    String query = "UPDATE ST_INVENTORY_NEW_RFID SET SCANN_STATUS= ? where RFID_TAG = ? ";
//System.out.println("query in AttachedRfidTagToStock: "+query);
    try {
        con = dbConnection.getConnection("legendPlus");
        ps = con.prepareStatement(query);
        ps.setString(1, "P");
        ps.setString(2, rfidtag);
        ps.execute();
        
    } catch (Exception ex) {
        done = false;
        System.out.println("WARNING:Error occurred in RFID TAG TABLES Update ->");
        ex.printStackTrace();
    } finally {
        dbConnection.closeConnection(con, ps);
    }
    return done;
}


public boolean RFIDAvailableForUseRecord(String rfidtag,String stockDescription) {
	String status = "N";
String query = " INSERT INTO ST_INVENTORY_RFID  SELECT USER_ID,RFID_TAG,LOCATION,SCANN_TYPE,'"+status+"',CREATE_DATE,CREATE_DATETIME,'"+stockDescription+"' FROM ST_INVENTORY_NEW_RFID WHERE RFID_TAG = ?";
//System.out.println("<<<<<<<<===rfidtag in RFIDAvailableForUseRecord: "+rfidtag);
//System.out.println("<<<<<<<<===query in RFIDAvailableForUseRecord: "+query);
	Connection con = null;
PreparedStatement ps = null; 
boolean done = false;
  
try {
con = getConnection();  
ps = con.prepareStatement(query);
ps.setString(1,rfidtag);

done = (ps.executeUpdate() != -1);
} catch (Exception ex) {
done = false;
System.out.println("ERROR Creating RFID Tag Records for verification " + ex.getMessage());
ex.printStackTrace();
} finally {
	dbConnection.closeConnection(con, ps); 
}
return done;
}


public boolean MaterialUtilizationUsedRecord(String compCode,String branchCode,String assetId,String rfidtag,String description,String warehouseCode,int quantity,String usedBy,int userId) {
String query = "INSERT INTO ST_INVENTORY_USED (COMP_CODE,BRANCH_CODE,ASSET_ID,RFID_TAG,ASSET_DESCRIPTION,QUANTITY,TRANS_DATE,USER_ID,USED_BY) " +
		 "VALUES(?,?,?,?,?,?,?,?,?)";
//System.out.println("<<<<<<<<===query in MaterialUtilizationUsedRecord: "+query);

	Connection con = null;
PreparedStatement ps = null;
boolean done = false;
  
try {
con = getConnection();  
ps = con.prepareStatement(query);
ps.setString(1,"N");


ps.setString(1,compCode);
ps.setString(2, branchCode);
ps.setString(3, assetId);
ps.setString(4,rfidtag);
ps.setString(5,description);
ps.setInt(6, quantity);
ps.setTimestamp(7, dbConnection.getDateTime(new Date()));
ps.setInt(8, userId);
ps.setString(9,usedBy);
done = (ps.executeUpdate() != -1);
} catch (Exception ex) {
done = false;
System.out.println("ERROR Creating Material Utilization Records " + ex.getMessage());
ex.printStackTrace();
} finally {
	dbConnection.closeConnection(con, ps); 
}
return done;
}


public boolean MaterialUsedUpdate(String groupId, String rfidtag,String ReqnID,int recordNo) {
    boolean done = true;

    Connection con = null;
    PreparedStatement ps = null;
    PreparedStatement ps1 = null;
    PreparedStatement ps2 = null;
    PreparedStatement ps3 = null;
    String query = "UPDATE ST_INVENTORY_RFID_APPROVED SET SCANN_STATUS= ? where RFID_TAG = ? ";
    String query2 = "UPDATE ST_INVENTORY_NEW_RFID SET SCANN_STATUS= ? where RFID_TAG = ? ";
    String query3 = "UPDATE am_asset_approval SET PROCESS_STATUS= ? where ASSET_ID = ? ";
    String query4 = "UPDATE ST_STOCK SET ASSET_STATUS= ? where BAR_CODE = ? ";
    try {
        con = dbConnection.getConnection("legendPlus");
        ps = con.prepareStatement(query);
        ps.setString(1, "A");
        ps.setString(2, rfidtag);
        ps.execute();

        ps1 = con.prepareStatement(query2);
        ps1.setString(1, "A");
        ps1.setString(2, rfidtag);
        ps1.execute();

        ps3 = con.prepareStatement(query4);
        ps3.setString(1, "USED");
        ps3.setString(2, rfidtag);
        ps3.execute();
        
//        if((recordNo==1)||(recordNo==1)){
        ps2 = con.prepareStatement(query3);
        ps2.setString(1, "A");
        ps2.setString(2, groupId);
        ps2.execute();
  //      }
        
    } catch (Exception ex) {
        done = false;
        System.out.println("WARNING:Error occurred for Material Utilization Update ->");
        ex.printStackTrace();
    } finally {
        dbConnection.closeConnection(con, ps);
        dbConnection.closeConnection(con, ps1);
        dbConnection.closeConnection(con, ps3);
        if((recordNo==1)||(recordNo==1)){
        dbConnection.closeConnection(con, ps2);
        }
    }
    return done;
}

public boolean createApprovalUsageList(String rfidtag,String Supervisor,String createDate,String description,String groupId,String scanstatus,String usedBy,int quantityUsed,int quantityBalance) {
	createDate = String.valueOf(df.dateConvert(new java.util.Date()));
Timestamp CreateTime = dbConnection.getDateTime(new java.util.Date());
String faultId = "";
	String query = "INSERT INTO ST_INVENTORY_RFID_APPROVED SELECT MTID,USER_ID,RFID_TAG,LOCATION,SCANN_TYPE,'"+scanstatus+"',"+Supervisor+",'"+createDate+"','"+CreateTime+"','"+description+"','"+groupId+"','"+usedBy+"',"+quantityUsed+","+quantityBalance+",'"+faultId+"'   FROM ST_INVENTORY_RFID_USED WHERE RFID_TAG =?";
System.out.println("<<<<<<<<===query in createApprovalUsageList: "+query);
	Connection con = null;
PreparedStatement ps = null;
boolean done = false;
  
try {
con = getConnection();
ps = con.prepareStatement(query);
ps.setString(1,rfidtag);

done = (ps.executeUpdate() != -1);
} catch (Exception ex) {
done = false;
System.out.println("ERROR Creating Material Utilization for Approval  Records " + ex.getMessage());
ex.printStackTrace();
} finally {
	dbConnection.closeConnection(con, ps);
}
return done;
}  

public ArrayList findApprovalForUsedMaterialDByQuery(String filter) {
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
       String description = "";
       String groupid = "";
       String usedBy = "";
       int quantity =0;
       int quantitybal =0;
   String query =
           "select MTID,RFID_TAG,LOCATION,SCANN_TYPE,SCANN_STATUS,USER_ID,CREATE_DATE,CREATE_DATETIME,REMARK,GROUP_ID,USED_BY,QUANTITY,QUANTITYBAL from ST_INVENTORY_RFID_APPROVED where MTID IS NOT NULL "+ filter;
   Connection con = null;
  PreparedStatement ps = null;
  ResultSet rs = null;
  System.out.println("<<<<<<<<===query in findApprovalForUsedMaterialDByQuery: "+query);
   try {
       con = dbConnection.getConnection("legendPlus");
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
           description = rs.getString(9);
           groupid = rs.getString(10);
           usedBy = rs.getString(11);
           quantity = rs.getInt(12);
           quantitybal = rs.getInt(13);
           
       ar = new RFID(rfidTag,mtid,createDate, location, scannstatus, createDateTime, scannType, userId,description,groupid,usedBy,quantity,quantitybal);
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

public boolean RejectRfidTagforMaterialUsage(String groupId, String rfidtag,String ReqnID,int recordNo) {
    boolean done = true;

    Connection con = null;
    PreparedStatement ps = null;
    PreparedStatement ps1 = null;
    PreparedStatement ps2 = null;
    String query = "UPDATE ST_INVENTORY_RFID_APPROVED SET SCANN_STATUS= ? where RFID_TAG = ? ";
    String query2 = "UPDATE ST_INVENTORY_RFID_USED SET SCANN_STATUS= ? where RFID_TAG = ? ";
    String query3 = "UPDATE am_asset_approval SET PROCESS_STATUS= ? where ASSET_ID = ? ";

    try {
        con = dbConnection.getConnection("legendPlus");
        ps = con.prepareStatement(query);
        ps.setString(1, "R");
        ps.setString(2, rfidtag);
        ps.execute();

        ps1 = con.prepareStatement(query2);
        ps1.setString(1, "U");
        ps1.setString(2, rfidtag);
        ps1.execute();
        
  //      if((recordNo==1)||(recordNo==1)||(recordNo==0)){
        ps2 = con.prepareStatement(query3);
        ps2.setString(1, "R");
        ps2.setString(2, ReqnID);
        ps2.execute();
  //      }
    } catch (Exception ex) {
        done = false;
        System.out.println("WARNING:Error occurred in RFID TAG TABLES Update ->");
        ex.printStackTrace();
    } finally {
        dbConnection.closeConnection(con, ps);
        dbConnection.closeConnection(con, ps1);
        if((recordNo==1)||(recordNo==1)){
        dbConnection.closeConnection(con, ps2);
        }
    }
    return done;
}

public ArrayList findNewRFIDAssignByQuery(String filter) {
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
 // System.out.println("<<<<query in findNewRFIDAssignByQuery: "+query);
   try {
       con = dbConnection.getConnection("legendPlus");
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
       dbConnection.closeConnection(con, ps, rs);
   }

   return list;

}

public boolean NewTagUsed(String rfidtag, String status) {
    boolean done = false;
    Connection con = null;
    PreparedStatement ps = null;
    String query = "";
  //  System.out.println(" query in NewTagUsed: ");
    if(status.equalsIgnoreCase("P")){
    query = "UPDATE ST_INVENTORY_RFID SET SCANN_STATUS= '"+status+"' where RFID_TAG = ? ";
	}
    if(status.equalsIgnoreCase("N")){
    query = "UPDATE ST_INVENTORY_RFID SET SCANN_STATUS= '"+status+"' where RFID_TAG = ? ";
	}
    if(status.equalsIgnoreCase("D")){
    query = "DELETE FROM ST_INVENTORY_RFID WHERE RFID_TAG = ? ";
    }
  //  System.out.println(" query in NewTagUsed: "+query);
    try {
        con = dbConnection.getConnection("legendPlus");
        ps = con.prepareStatement(query);
        ps.setString(1, rfidtag);
        ps.execute();
        done = true;
        
    } catch (Exception ex) {
        done = false;
        System.out.println("WARNING:Error occurred in RFID TAG TABLE use for stock creation ->");
        ex.printStackTrace();
    } finally {
        dbConnection.closeConnection(con, ps);
    }
    return done;
}

public boolean createRFIDRecordInVerify(String rfidTag, String location, String createDate, String createDateTime, int userId)
{
    Connection con;
    PreparedStatement ps;
    boolean done; 
    String familyID;
    String rfidquery = "INSERT INTO ST_INVENTORY_VERIFY_RFID (RFID_TAG,LOCATION,SCANN_STATUS,SCANN_TYPE,CREATE_DATE," +
"CREATE_DATETIME,USER_ID,PROCESSED)VALUES (?,?,?,?,?,?,?,?)";    
//    System.out.println("<<<<query in createRFIDRecordInVerify: "+rfidquery);
    con = null;
    ps = null;
    done = false;
    int i,j,k;
    try{  
    con = getConnection();
    done = con.getAutoCommit(); 
    con.setAutoCommit(false);
    done = false;
    ps = con.prepareStatement(rfidquery);
    ps.setString(1, rfidTag);
    ps.setString(2, location);
    ps.setString(3, "I");
    ps.setString(4, "N");
    ps.setString(5, createDate);
    ps.setString(6, createDateTime);
    ps.setInt(7, userId);
    ps.setString(8, "N");
    j = ps.executeUpdate();  
 //   System.out.println("<<<<j in createRFIDRecordInVerify: "+j);
 if((j != -1))
 {
   con.commit();
   con.setAutoCommit(done);
   done = true;
  }
// System.out.println("done 2=== "+done);
 dbConnection.closeConnection(con, ps);
	}catch(Exception ex){
    System.out.println((new StringBuilder()).append("ERROR Creating RFID Transaction Posting in createRFIDRecordInVerify.. ").append(ex.getMessage()).toString());
    ex.printStackTrace();
    done = false;
    dbConnection.closeConnection(con, ps);
	} finally{
		dbConnection.closeConnection(con, ps);
	}
    return done;
}   


public ArrayList groupStockApproval(String compCode, String groupId,String assetId) {
       String barCode = "";
       String stockId ="";
       int userid = 0;
       String itemCode ="";
       String warehouseCode="";
       int quantity = 0;
       java.util.ArrayList list = new java.util.ArrayList();
//   String query = "SELECT ASSET_ID,BRANCH_ID,BAR_CODE,ITEM_CODE,ITEMTYPE,ASSET_STATUS,WAREHOUSE_CODE,DESCRIPTION,QUANTITY,USER_ID FROM ST_STOCK WHERE GROUP_ID = '"+groupId+"' ";
   Connection con = null;
  PreparedStatement ps = null;
  ResultSet rs = null;
  String branch_Id = "SELECT Branch_id FROM ST_STOCK WHERE GROUP_ID = '"+groupId+"' AND ASSET_ID = '"+assetId+"' ";
  int branchId =Integer.parseInt(approvalRec.getCodeName(branch_Id));
  String stockqty = "SELECT SUM(QUANTITY) AS QUANTITY FROM ST_STOCK WHERE GROUP_ID = '"+groupId+"' AND ASSET_ID = '"+assetId+"' ";
  quantity =Integer.parseInt(approvalRec.getCodeName(stockqty));
  String srcCode = "SELECT DISTINCT ITEM_CODE FROM ST_STOCK WHERE GROUP_ID = '"+groupId+"' AND ASSET_ID = '"+assetId+"' ";
  itemCode = approvalRec.getCodeName(srcCode);
  String srcwarehouse = "SELECT DISTINCT WAREHOUSE_CODE FROM ST_STOCK WHERE GROUP_ID = '"+groupId+"' AND ASSET_ID = '"+assetId+"' ";
  warehouseCode = approvalRec.getCodeName(srcwarehouse);
  String query1 = "SELECT COUNT(*) FROM ST_INVENTORY_TOTALS WHERE COMP_CODE = '"+compCode+"' AND WAREHOUSE_CODE = '"+warehouseCode+"' AND ITEM_CODE = '"+itemCode+"'";
  String inventTotal =approvalRec.getCodeName(query1);
  String querybal = "SELECT BALANCE FROM ST_INVENTORY_TOTALS WHERE COMP_CODE = '"+compCode+"' AND WAREHOUSE_CODE = '"+warehouseCode+"' AND ITEM_CODE = '"+itemCode+"'";
  String itemBalance =approvalRec.getCodeName(querybal);
  System.out.println("itemBalance in groupStockApproval: "+itemBalance);
if(itemBalance==null || itemBalance ==""){itemBalance = "0";}
System.out.println("itemBalance after in groupStockApproval: "+itemBalance);
   try {
       if(!inventTotal.equalsIgnoreCase("0")){
			updateInventoryTotal(compCode, itemCode, warehouseCode, quantity,branchId);
	//		AttachedRfidTagToStock(barCode);
		}	
	   if(inventTotal.equalsIgnoreCase("0")){
		   createInventoryTotal(compCode, itemCode, warehouseCode,  quantity, userid,branchId);
		//		AttachedRfidTagToStock(barCode);
		}
	   insertHistoryTable(compCode, itemCode, warehouseCode,  quantity, userid,groupId,Integer.parseInt(itemBalance));   
    	   //}      
   } catch (Exception ex) {
       System.out.println("Error occurred in approving the stock  --> "+stockId);
       ex.printStackTrace();
   } finally {
   //    dbConnection.closeConnection(con, ps, rs);
   }

   return list;

}



public java.util.ArrayList groupStockApprovalNew(String compCode, String groupId)
 {
 	java.util.ArrayList list = new java.util.ArrayList();
 	String notSignOutquery =  "SELECT ASSET_ID,BRANCH_ID,BAR_CODE,ITEM_CODE,ITEMTYPE,ASSET_STATUS,WAREHOUSE_CODE,DESCRIPTION,QUANTITY,USER_ID FROM ST_STOCK WHERE GROUP_ID = '"+groupId+"' ";
	Connection c = null;
	ResultSet rs = null;
	Statement s = null;
    String barCode = "";
    String stockId ="";
    int branchId =0; 
    int userid = 0;
    String itemCode ="";
    String itemType="";
    String warehouseCode="";
    String assetStatus = "";
    String scannstatus="";
    String stockDescription = "";	
    int quantity = 0;
//	System.out.println("<<<<<<<<notSignOutquery in getUsernotSignOutRecords: "+notSignOutquery);
	try {
		    c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(notSignOutquery);
			while (rs.next())
			   {
		    	   stockId = rs.getString(1); 
		    	   System.out.println("<<<<<<<======stockId: "+stockId);
		    	   branchId = rs.getInt(2); 
		    	   barCode = rs.getString(3);
		    	   itemCode = rs.getString(4);
		    	   itemType = rs.getString(5);
		    	   assetStatus = rs.getString(6);
		           warehouseCode = rs.getString(7);
		           stockDescription = rs.getString(8);
		           quantity = rs.getInt(9);
		           System.out.println("<<<<<<<======quantity: "+quantity);
		           userid = rs.getInt(10);	
		           String query1 = "SELECT COUNT(*) FROM ST_INVENTORY_TOTALS WHERE COMP_CODE = '"+compCode+"' AND WAREHOUSE_CODE = '"+warehouseCode+"' AND ITEM_CODE = '"+itemCode+"'";
		           String inventTotal =approvalRec.getCodeName(query1);
			        if(inventTotal.equalsIgnoreCase("0")){createInventoryTotal(compCode, itemCode, warehouseCode,  quantity, userid,branchId);
						AttachedRfidTagToStock(barCode);
					}
					else{
						
					updateInventoryTotal(compCode, itemCode, warehouseCode, quantity,branchId);
					AttachedRfidTagToStock(barCode);
					}			           
//				String notSignoutuserId = rs.getString("user_id");
//				list.add(notSignoutuserId);
			   }
	 }
				 catch (Exception e)
					{
						e.printStackTrace();
					}
					finally
					{
						dbConnection.closeConnection(c, s, rs);
					}
 	return list;
 }

public boolean reductionOfInventoryTotal(String compCode, String inventCode, String warehouseCode,  int quantity,int branchId) {
    boolean done = true;

    Connection con = null;
    PreparedStatement ps = null;
    String query = "update ST_INVENTORY_TOTALS set BALANCE= BALANCE - ? where COMP_CODE=? AND WAREHOUSE_CODE=?  AND ITEM_CODE=? AND BRANCH_ID = ? ";

    try {
        con = dbConnection.getConnection("legendPlus");
        ps = con.prepareStatement(query);
        ps.setInt(1, quantity);
        ps.setString(2, compCode);
        ps.setString(3, warehouseCode);
        ps.setString(4, inventCode); 
        ps.setInt(5, branchId);
        ps.execute();

    } catch (Exception ex) {
        done = false;
        System.out.println("WARNING:Error occurred in ST_INVENTORY_TOTALS Update ->");
        ex.printStackTrace();
    } finally {
        dbConnection.closeConnection(con, ps);
    }
    return done;
}


public boolean returnOfStockQuantity(String compCode, String inventCode, String warehouseCode,  int quantity,int branchId,String tag) {
    boolean done = true;

    Connection con = null;
    PreparedStatement ps = null;
    String returnquantity = "update ST_STOCK set QUANTITY= QUANTITY + ? where BAR_CODE=? ";

    try {
        con = dbConnection.getConnection("legendPlus");
        ps = con.prepareStatement(returnquantity);
        ps.setInt(1, quantity);
        ps.setString(2, tag);
        ps.execute();

    } catch (Exception ex) {
        done = false;
        System.out.println("WARNING:Error occurred in ST_INVENTORY_TOTALS Update ->");
        ex.printStackTrace();
    } finally {
        dbConnection.closeConnection(con, ps);
    }
    return done;
}

public boolean NewArrivalTagUsed(String rfidtag, String status) {
    boolean done = false;
    Connection con = null;
    PreparedStatement ps = null;
    String query = "";
  //  System.out.println(" query in NewArrivalTagUsed: ");
    if(status.equalsIgnoreCase("P")){
    query = "UPDATE ST_INVENTORY_NEW_RFID SET SCANN_STATUS= '"+status+"' where RFID_TAG = ? ";
	}
    if(status.equalsIgnoreCase("N")){
    query = "UPDATE ST_INVENTORY_NEW_RFID SET SCANN_STATUS= '"+status+"' where RFID_TAG = ? ";
	}
    if(status.equalsIgnoreCase("D")){
    query = "DELETE FROM ST_INVENTORY_NEW_RFID WHERE RFID_TAG = ? ";
    }
//    System.out.println(" query in NewArrivalTagUsed: "+query);
    try {
        con = dbConnection.getConnection("legendPlus");
        ps = con.prepareStatement(query);
        ps.setString(1, rfidtag);
        ps.execute();
        done = true;
        
    } catch (Exception ex) {
        done = false;
        System.out.println("WARNING:Error occurred in RFID TAG TABLE use for stock creation ->");
        ex.printStackTrace();
    } finally {
        dbConnection.closeConnection(con, ps);
    }
    return done;
}
public boolean approvalofNewRFIDTagList(String groupId,String Supervisor,String createDate,String userID,String description,String branchCode,String apprioveId,int level,String tranType) {
	//Timestamp CreateTime = dbConnection.getDateTime(new java.util.Date());
	//System.out.println("<<<<<<<<===Supervisor in createRFIDTagList: "+Supervisor+"    approvDate: "+createDate);
	//System.out.println("<<<<<<<<===groupId in approvalofRFIDTagList: "+groupId+"    userID: "+userID+"  description:"+description+"   branchCode: "+branchCode);
	    String ins_am_asset_approval_qry = "insert into am_asset_approval(asset_id,user_id,super_id,posting_date,description" +
	",effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time" +
	",transaction_id,batch_id,transaction_level) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
	;
		String status = "P";
		String reqnStatus = "PENDING";
	Connection con = null;
	PreparedStatement ps = null;
	boolean done = false;
	String []spuervisorlist = Supervisor.split("#");
	int No = spuervisorlist.length;
	//System.out.println("<<<<<<Before Identifiaction>>>>>>>");
	//String mtid = applHelper.getGeneratedId("am_asset_approval");
	//System.out.println("<<<<<<Identifiaction: "+apprioveId+"   LEVEL: "+level+"   TIME: "+timer.format(new Date()));
	//String adm_Approv_Lvl_Qry = "select level from Approval_Level_Setup where transaction_type='New Tag Attachment'";
	//System.out.println("<<<<<<<adm_Approv_Lvl_Qry Value: "+aprecords.getCodeName(adm_Approv_Lvl_Qry));
	//int var = Integer.parseInt(aprecords.getCodeName(adm_Approv_Lvl_Qry));

	try { 
		
		con = getConnection();
		ps = con.prepareStatement(ins_am_asset_approval_qry);
		for(int j=0;j<No;j++){
	    ps.setString(1,groupId);
	    ps.setString(2, userID);
	    ps.setString(3, spuervisorlist[j]);
	    ps.setTimestamp(4, dbConnection.getDateTime(new Date()));
	    ps.setString(5, description);
	    ps.setTimestamp(6, dbConnection.getDateTime(new Date()));
	    ps.setString(7, branchCode);
	    ps.setString(8, reqnStatus);
	    ps.setString(9, tranType);
	    ps.setString(10, status);
	    ps.setString(11, timer.format(new Date()));
	    ps.setString(12, apprioveId);
	    ps.setString(13, apprioveId);
	    ps.setInt(14, level);
	    //done = ps.executeUpdate() == -1;
	    ps.execute();
	    done = true;
	  //  System.out.println("<<<<<<done Identifiaction>>>>>>>: "+done);
		}
	} catch (Exception ex) {
	done = false;
	System.out.println("ERROR Creating RFID Tag Records " + ex.getMessage());
	ex.printStackTrace();
	} finally {
		dbConnection.closeConnection(con, ps);
	}
	return done;
	}

public boolean MaterialUtilizationTransactionRecord(String compCode,String branchCode,String assetId,String rfidtag,String description,String warehouseCode,int quantity,String usedBy,int userId, int Quantitybalance) {
String query = "INSERT INTO ST_MATERIAL_TRANSACTION (COMP_CODE,BRANCH_CODE,ASSET_ID,RFID_TAG,ASSET_DESCRIPTION,QUANTITY,TRANS_DATE,USER_ID,USED_BY,UNIT_COST,USED_COST,QUANTITYBAL) " +
		 "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
System.out.println("<<<<<<<<===query in MaterialUtilizationUsedRecord: "+query);

	Connection con = null;
PreparedStatement ps = null;
boolean done = false;
String query1 = "SELECT Vatable_Cost/QUANTITY AS UNIT_COST, *FROM ST_STOCK WHERE BAR_CODE = '"+rfidtag+"' ";
String qryunitcost =approvalRec.getCodeName(query1);  

double unitCost = qryunitcost != null ? Double.parseDouble(qryunitcost) : 0.0D;
double usedCost = unitCost*quantity;
//System.out.println("<<<<<<<<===qryunitcost in MaterialUtilizationTransactionRecord: "+qryunitcost+"    unitCost: "+unitCost+"  usedCost: "+usedCost);
try {
con = getConnection();  
ps = con.prepareStatement(query);
ps.setString(1,"N");


ps.setString(1,compCode);
ps.setString(2, branchCode);
ps.setString(3, assetId);
ps.setString(4,rfidtag);
ps.setString(5,description);
ps.setInt(6, quantity);
ps.setTimestamp(7, dbConnection.getDateTime(new Date()));
ps.setInt(8, userId);
ps.setString(9,usedBy);
ps.setDouble(10, unitCost);
ps.setDouble(11, usedCost);
ps.setInt(12, Quantitybalance);
done = (ps.executeUpdate() != -1);
} catch (Exception ex) {
done = false;
System.out.println("ERROR Creating Material Utilization Transaction Records " + ex.getMessage());
ex.printStackTrace();
} finally {
	dbConnection.closeConnection(con, ps); 
}
return done;
}

public boolean deletedistributedRfidRecordforTransfer(String rfidTag) {
  Connection con = null;
  PreparedStatement ps = null; 
  Connection con1 = null;
  PreparedStatement ps1 = null;   
 // String DELETE_QUERY = "UPDATE ST_INVENTORY_RFID_UNUSED SET PROCESSED = 'P' WHERE RFID_TAG = '"+rfidTag+"' ";
  String DELETE_QUERY = "DELETE FROM ST_INVENTORY_RFID_UNUSED WHERE RFID_TAG = '"+rfidTag+"' ";
  String DELETE_QUERY2 = "DELETE FROM ST_STOCK_DISTRBUTE WHERE RFID_TAG = '"+rfidTag+"' ";
  System.out.println("<<<<<<<<===DELETE_QUERY in deletedistributedRfidRecordforTransfer: "+DELETE_QUERY);
  System.out.println("<<<<<<<<===DELETE_QUERY2 in deletedistributedRfidRecordforTransfer: "+DELETE_QUERY2);
  boolean done;
  done = false;
  try {
  	con = getConnection();
      ps = con.prepareStatement(DELETE_QUERY);         
      ps.executeUpdate();         
    	con1 = getConnection();
        ps1 = con1.prepareStatement(DELETE_QUERY2);         
        ps1.executeUpdate();  
        done = true;
  } catch (Exception ex) {
      System.out.println("WARNING: cannot update table ST_INVENTORY_RFID_UNUSED & ST_STOCK_DISTRBUTE in deletedistributedRfidRecord+" + ex.getMessage());
  } finally {
	  dbConnection.closeConnection(con, ps);
	  dbConnection.closeConnection(con1, ps1);
  }
  return done;   
}

public boolean updateInventoryTotalforTransfer(String compCode, String inventCode, String warehouseCode,  int quantity,int branchId) {
    boolean done = true;

    Connection con = null;
    PreparedStatement ps = null;
    String query = "update ST_INVENTORY_TOTALS set BALANCE= BALANCE - ? where COMP_CODE=? AND WAREHOUSE_CODE=?  AND ITEM_CODE=? AND BRANCH_ID = ? ";
//System.out.println("compCode in updateInventoryTotal: "+compCode+"  inventCode: "+inventCode+"  warehouseCode: "+warehouseCode+"    quantity: "+quantity+"   branchId: "+branchId);
    try {
        con = dbConnection.getConnection("legendPlus");
        ps = con.prepareStatement(query); 
        ps.setInt(1, quantity);
        ps.setString(2, compCode);
        ps.setString(3, warehouseCode);
        ps.setString(4, inventCode); 
        ps.setInt(5, branchId);
        ps.execute();

    } catch (Exception ex) {
        done = false;
        System.out.println("WARNING:Error occurred in ST_INVENTORY_TOTALS Update ->");
        ex.printStackTrace();
    } finally {
        dbConnection.closeConnection(con, ps);
    }
    return done;
}


public boolean updateInventoryTotalforSubtraction(String compCode, String inventCode, String warehouseCode,  int quantity,int branchId) {
    boolean done = true;

    Connection con = null;
    PreparedStatement ps = null;
    String query = "update ST_INVENTORY_TOTALS set BALANCE= BALANCE - ? where COMP_CODE=? AND WAREHOUSE_CODE=?  AND ITEM_CODE=? AND BRANCH_ID = ? ";
//System.out.println("compCode in updateInventoryTotal: "+compCode+"  inventCode: "+inventCode+"  warehouseCode: "+warehouseCode+"    quantity: "+quantity+"   branchId: "+branchId);
    try {
        con = dbConnection.getConnection("legendPlus");
        ps = con.prepareStatement(query); 
        ps.setInt(1, quantity);
        ps.setString(2, compCode);
        ps.setString(3, warehouseCode);
        ps.setString(4, inventCode); 
        ps.setInt(5, branchId);
        ps.execute();

    } catch (Exception ex) {
        done = false;
        System.out.println("WARNING:Error occurred in ST_INVENTORY_TOTALS Update ->");
        ex.printStackTrace();
    } finally {
        dbConnection.closeConnection(con, ps);
    }
    return done;
}

public boolean updateInventoryTotalforAddition(String compCode, String inventCode, String warehouseCode,  int quantity,int branchId) {
    boolean done = true;

    Connection con = null;
    PreparedStatement ps = null;
    String query = "update ST_INVENTORY_TOTALS set BALANCE= BALANCE + ? where COMP_CODE=? AND WAREHOUSE_CODE=?  AND ITEM_CODE=? AND BRANCH_ID = ? ";
System.out.println("compCode in updateInventoryTotalforAddition: "+compCode+"  inventCode: "+inventCode+"  warehouseCode: "+warehouseCode+"    quantity: "+quantity+"   branchId: "+branchId);
    try {
        con = dbConnection.getConnection("legendPlus");
        ps = con.prepareStatement(query); 
        ps.setInt(1, quantity);
        ps.setString(2, compCode);
        ps.setString(3, warehouseCode);
        ps.setString(4, inventCode); 
        ps.setInt(5, branchId);
        ps.execute();

    } catch (Exception ex) {
        done = false;
        System.out.println("WARNING:Error occurred in ST_INVENTORY_TOTALS Update ->");
        ex.printStackTrace();
    } finally {
        dbConnection.closeConnection(con, ps);
    }
    return done;
}


public boolean updateInventoryTotalforIssuance(String compCode, String inventCode, String warehouseCode,  int quantity,int branchId) {
    boolean done = true;

    Connection con = null;
    PreparedStatement ps = null;
    String query = "update ST_INVENTORY_TOTALS set BALANCE= BALANCE - ? where COMP_CODE=? AND WAREHOUSE_CODE=?  AND ITEM_CODE=? AND BRANCH_ID = ? ";
System.out.println("compCode in updateInventoryTotalforAddition: "+compCode+"  inventCode: "+inventCode+"  warehouseCode: "+warehouseCode+"    quantity: "+quantity+"   branchId: "+branchId);
    try {
        con = dbConnection.getConnection("legendPlus");
        ps = con.prepareStatement(query); 
        ps.setInt(1, quantity);
        ps.setString(2, compCode);
        ps.setString(3, warehouseCode);
        ps.setString(4, inventCode); 
        ps.setInt(5, branchId);
        ps.execute();

    } catch (Exception ex) {
        done = false;
        System.out.println("WARNING:Error occurred in ST_INVENTORY_TOTALS Update in updateInventoryTotalforIssuance ->");
        ex.printStackTrace();
    } finally {
        dbConnection.closeConnection(con, ps);
    }
    return done;
}

public String SupervisorApprovalList(String departCode,String userid,String userBranch) {
        String data = "";
        String delimeter = "#";
        String query = "select user_id from am_gb_User WHERE BRANCH = ? AND DEPT_CODE = ? AND is_supervisor = 'Y' AND USER_ID <> ? ";
        Connection con = null;
       PreparedStatement ps = null;
       ResultSet rs = null;
        try {
            con = dbConnection.getConnection("legendPlus");
                       ps = con.prepareStatement(query);
                       ps.setString(1, userBranch);
                       ps.setString(2, departCode);
                       ps.setString(3, userid); 
                       rs = ps.executeQuery();
            while (rs.next()) {
                data = data+rs.getString(1)+delimeter;
            }

        } catch (Exception ex) {
            System.out.println("Error occurred in SupervisorApprovalList --> ");
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }

        return data;

    }

public boolean deleteMaterialUtilizeRfidRecord(String rfidTag) {
  Connection con = null;
  PreparedStatement ps = null;  
  String DELETE_QUERY = "DELETE FROM ST_INVENTORY_MATERIAL_USED WHERE RFID_TAG = '"+rfidTag+"' ";
  boolean done;
  done = false;
  try {
  	con = getConnection();
      ps = con.prepareStatement(DELETE_QUERY);         
      ps.executeUpdate();         
        done = true;
  } catch (Exception ex) {
      System.out.println("WARNING: cannot update table ST_INVENTORY_MATERIAL_USED in deleteMaterialUtilizeRfidRecord+" + ex.getMessage());
  } finally {
	  dbConnection.closeConnection(con, ps);
  }
  return done;   
}

public boolean deletedistributedRfidRecord(String rfidTag) {
  Connection con = null;
  PreparedStatement ps = null;  
 // String DELETE_QUERY = "UPDATE ST_INVENTORY_RFID_UNUSED SET PROCESSED = 'P' WHERE RFID_TAG = '"+rfidTag+"' ";
  String DELETE_QUERY = "DELETE FROM ST_INVENTORY_RFID_UNUSED WHERE RFID_TAG = '"+rfidTag+"' ";
  System.out.println("<<<<<<<<===DELETE_QUERY in deletedistributedRfidRecord: "+DELETE_QUERY);
  boolean done;
  done = false;
  try {
  	con = getConnection();
      ps = con.prepareStatement(DELETE_QUERY);         
      ps.executeUpdate();         
        done = true;
  } catch (Exception ex) {
      System.out.println("WARNING: cannot update table ST_INVENTORY_RFID_UNUSED & ST_STOCK_DISTRBUTE in deletedistributedRfidRecord+" + ex.getMessage());
  } finally {
	  dbConnection.closeConnection(con, ps);
  }
  return done;   
}

public boolean deletedistributedafterIssuanceRfidRecord(String rfidTag) {
	  Connection con = null;
	  PreparedStatement ps = null;  
	 // String DELETE_QUERY = "UPDATE ST_INVENTORY_RFID_UNUSED SET PROCESSED = 'P' WHERE RFID_TAG = '"+rfidTag+"' ";
	  String DELETE_QUERY = "DELETE FROM ST_STOCK_DISTRBUTE WHERE RFID_TAG = '"+rfidTag+"' ";
	  System.out.println("<<<<<<<<===DELETE_QUERY in deletedistributedafterIssuanceRfidRecord: "+DELETE_QUERY);
	  boolean done;
	  done = false;
	  try {
	  	con = getConnection();
	      ps = con.prepareStatement(DELETE_QUERY);         
	      ps.executeUpdate();         
	        done = true;
	  } catch (Exception ex) {
	      System.out.println("WARNING: cannot update table ST_INVENTORY_RFID_UNUSED & ST_STOCK_DISTRBUTE in deletedistributedRfidRecord+" + ex.getMessage());
	  } finally {
		  dbConnection.closeConnection(con, ps);
	  }
	  return done;   
	}


public String getVendorTransactionQuery()
 {

	String query=" select MTID,VENDOR_CODE,TRANS_ID, DESCRIPTION,COST_PRICE,TRANSACTION_DATE,TRANSACTION_TYPE, "
				+" LOCATION,DRACCOUNT_NO,CRACCOUNT_NO,PROJECT_CODE"
				+" from  VENDOR_TRANSACTIONS "
				+" WHERE VENDOR_CODE =?"
				+" order by TRANSACTION_DATE";
	System.out.println("<<<<<<<query in getVendorTransactionQuery: "+query);
    return query;
    
 }


public boolean NewTagUsedforSplit(String rfidtag, String status) {
    boolean done = false;
    Connection con = null;
    PreparedStatement ps = null;
    String query = "";
  //  System.out.println(" query in NewTagUsed: ");
    if(status.equalsIgnoreCase("P")){
    query = "UPDATE ST_INVENTORY_NEW_RFID SET SCANN_STATUS= '"+status+"' where RFID_TAG = ? ";
	}
    if(status.equalsIgnoreCase("N")){
    query = "UPDATE ST_INVENTORY_NEW_RFID SET SCANN_STATUS= '"+status+"' where RFID_TAG = ? ";
	}
    if(status.equalsIgnoreCase("D")){
    query = "DELETE FROM ST_INVENTORY_NEW_RFID WHERE RFID_TAG = ? ";
    }
  //  System.out.println(" query in NewTagUsed: "+query);
    try {
        con = dbConnection.getConnection("legendPlus");
        ps = con.prepareStatement(query);
        ps.setString(1, rfidtag);
        ps.execute();
        done = true;
        
    } catch (Exception ex) {
        done = false;
        System.out.println("WARNING:Error occurred in RFID TAG TABLE use for stock Split creation ->");
        ex.printStackTrace();
    } finally {
        dbConnection.closeConnection(con, ps);
    }
    return done;
}

public String SupervisorMailAddressList(String batch_id) {
        String data = "";
        String delimeter = "#";
        String query = "select super_id from am_asset_approval WHERE BATCH_ID = ?  AND PROCESS_STATUS != 'A' ";
        Connection con = null;
       PreparedStatement ps = null;
       ResultSet rs = null;
        try {
            con = dbConnection.getConnection("legendPlus");
                       ps = con.prepareStatement(query);
                       ps.setString(1, batch_id);
                       rs = ps.executeQuery();
            while (rs.next()) {
                data = data+rs.getString(1)+delimeter;
            }

        } catch (Exception ex) {
            System.out.println("Error occurred in SupervisorMailAddressList --> ");
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }

        return data;

    }

public boolean createApprovalRFIDTagList(String rfidtag,int Supervisor,String createDate,String description,String groupId,String scanstatus,String location,String branchId,String deptId) {
	createDate = String.valueOf(df.dateConvert(new java.util.Date()));
Timestamp CreateTime = dbConnection.getDateTime(new java.util.Date());
String usedBy = "";
int quantity = 0;
int quantitybal = 0;
String faultId = "";
String query = "INSERT INTO ST_INVENTORY_RFID_APPROVED SELECT MTID,USER_ID,RFID_TAG,'"+location+"',SCANN_TYPE,'"+scanstatus+"',"+Supervisor+",'"+createDate+"','"+CreateTime+"','"+description+"','"+groupId+"','"+usedBy+"',"+quantity+","+quantitybal+",'"+faultId+"','"+branchId+"','"+deptId+"'   FROM ST_INVENTORY_NEW_RFID WHERE RFID_TAG =?";
System.out.println("<<<<<<<<===query in createRFIDTagList: "+query);
	Connection con = null;
PreparedStatement ps = null;
boolean done = false;
  
try {
con = getConnection();
ps = con.prepareStatement(query);
ps.setString(1,rfidtag);

done = (ps.executeUpdate() != -1);
} catch (Exception ex) {
done = false;
System.out.println("ERROR Creating RFID Tag Records " + ex.getMessage());
ex.printStackTrace();
} finally {
	dbConnection.closeConnection(con, ps);
}
return done;
}

public boolean RFIDAvailableForUseRecord(String rfidtag,String stockDescription,int branchId,int deptId) {
	String status = "N";
String query = " INSERT INTO ST_INVENTORY_RFID  SELECT USER_ID,RFID_TAG,LOCATION,SCANN_TYPE,'"+status+"',CREATE_DATE,CREATE_DATETIME,'"+stockDescription+"',"+branchId+","+deptId+" FROM ST_INVENTORY_NEW_RFID WHERE RFID_TAG = ?";
	Connection con = null;
PreparedStatement ps = null; 
boolean done = false;
  
try {
con = getConnection();  
ps = con.prepareStatement(query);
ps.setString(1,rfidtag);

done = (ps.executeUpdate() != -1);
} catch (Exception ex) {
done = false;
System.out.println("ERROR Creating RFID Tag Records for verification in RFIDAvailableForUseRecord " + ex.getMessage());
ex.printStackTrace();
} finally {
	dbConnection.closeConnection(con, ps); 
}
return done;
}

public boolean insertHistoryTable(String compCode, String inventCode, String warehouseCode,  int quantity, int userid,String batchId,int prevBalance) {

    Connection con = null;
    PreparedStatement ps = null;
    boolean done = false;
    String query = "  insert into ST_INVENTORY_HISTORY(COMP_CODE,WAREHOUSE_CODE,ITEM_CODE,QUANTITY,STATUS," +
            "USERID,BATCH_CODE,PREVIOUS_BALANCE,TRANS_DESC,TRANS_DATE)  values(?,?,?,?,?,?,?,?,?,?)";
    try
    {
      System.out.println("inventCode in insertHistoryTable: "+inventCode);  
       con = dbConnection.getConnection("legendPlus");
        ps = con.prepareStatement(query);
        ps.setString(1, compCode);
        ps.setString(2, warehouseCode);
        ps.setString(3, inventCode);
        ps.setInt(4, quantity);
        ps.setString(5, "ACTIVE");
        ps.setInt(6, userid);
        ps.setString(7, batchId);
        ps.setInt(8, prevBalance);
        ps.setString(9, "New Stock");
        ps.setString(10, String.valueOf(df.dateConvert(new java.util.Date())));
        ps.execute();
        done = true;

    } catch (Exception e) {
        done = false;
        e.printStackTrace();
        System.out.println("WARNING:Error in  insertHistoryTableforIssuance ->" + e.getMessage());
    } finally {
        dbConnection.closeConnection(con, ps);
        //closeConnection(con, ps);
    }
    return done;

}

public boolean updateStockQuantityforIssuance(String AssetId, int quantity) {
    boolean done = true;

    Connection con = null;
    PreparedStatement ps = null;
    System.out.println("====>AssetId: "+AssetId+"      quantity: "+quantity);
    String query = "update ST_STOCK set QUANTITY= QUANTITY - ?,ASSET_STATUS = ? where ASSET_ID=? ";
    try {
        con = dbConnection.getConnection("legendPlus");
        ps = con.prepareStatement(query); 
        ps.setInt(1, quantity);
        ps.setString(2, "ACTIVE");
        ps.setString(3, AssetId);
        ps.execute();

    } catch (Exception ex) {
        done = false;
        System.out.println("WARNING:Error occurred in ST_STOCK Update in updateStockQuantityforIssuance ->");
        ex.printStackTrace();
    } finally {
        dbConnection.closeConnection(con, ps);
    }
    return done;
}

public boolean insertHistoryTableforIssuance(String compCode, String inventCode, String warehouseCode,  int quantity, int userid,String batchId,int prevBalance) {

    Connection con = null;
    PreparedStatement ps = null;
    boolean done = false;
    String query = "  insert into ST_INVENTORY_HISTORY(COMP_CODE,WAREHOUSE_CODE,ITEM_CODE,QUANTITY,STATUS," +
            "USERID,BATCH_CODE,PREVIOUS_BALANCE,BALANCE,TRANS_DESC,TRANS_DATE)  values(?,?,?,?,?,?,?,?,?,?,?)";
    try
    {
    	int balance = prevBalance - quantity;
      System.out.println("inventCode in insertHistoryTableforIssuance: "+inventCode);  
       con = dbConnection.getConnection("legendPlus");
        ps = con.prepareStatement(query);
        ps.setString(1, compCode);
        ps.setString(2, warehouseCode);
        ps.setString(3, inventCode);
        ps.setInt(4, quantity);
        ps.setString(5, "ACTIVE");
        ps.setInt(6, userid);
        ps.setString(7, batchId);
        ps.setInt(8, prevBalance);
        ps.setInt(9, balance);
        ps.setString(10, "Stock Issued");
        ps.setString(11, String.valueOf(df.dateConvert(new java.util.Date())));
        ps.execute();
        done = true;

    } catch (Exception e) {
        done = false;
        e.printStackTrace();
        System.out.println("WARNING:Error in  insertHistoryTableforIssuance ->" + e.getMessage());
    } finally {
        dbConnection.closeConnection(con, ps);
        //closeConnection(con, ps);
    }
    return done;

}

}
