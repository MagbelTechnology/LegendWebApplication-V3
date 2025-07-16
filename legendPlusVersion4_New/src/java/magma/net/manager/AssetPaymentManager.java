package magma.net.manager;

import java.text.SimpleDateFormat;
import java.sql.*;
import java.util.*;

import magma.net.dao.MagmaDBConnection;
import magma.net.vao.AssetPayment;

public class AssetPaymentManager extends MagmaDBConnection {
	SimpleDateFormat sdf;

	public AssetPaymentManager() {
		super();
		sdf = new SimpleDateFormat("dd-MM-yyyy");
	}

	public boolean createPaymentRecord(String assetId, double pay,
			String payDate, String raised, String payDesc, String userId,double vatAmount,
			double whtAmount,double vatRate,double whtRate,double vatableCost) {

		String INSERT_QUERY = "INSERT INTO AM_ASSET_PAYMENT "
				+ "(ASSET_ID,PAYMENT,PAY_DATE,RAISED,PAY_DESC,USER_ID,VATAMOUNT,WHTAMOUNT,VATRATE,WHTRATE,VATABLE_COST) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?)";
//		System.out.println("<<<<<<===assetId in createPaymentRecord: "+assetId);
		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(INSERT_QUERY);
			ps.setString(1, assetId);
			ps.setDouble(2, pay);
			ps.setDate(3, dateConvert(payDate));
			ps.setString(4, raised);
			ps.setString(5, payDesc);
			ps.setString(6, userId);
			ps.setDouble(7, vatAmount);
			ps.setDouble(8, whtAmount);
			ps.setDouble(9, vatRate);
			ps.setDouble(10, whtRate);
			ps.setDouble(11, vatableCost);
			
			done = (ps.executeUpdate() != -1);
			notifyAsset(assetId,pay);

		} catch (Exception ex) {

		} finally {
			closeConnection(con, ps);
		}
		return done;
	}


        public boolean createPaymentRecord(String assetId, double pay,
			String payDate, String raised, String payDesc,int tranId, String userId,double vatAmount,
			double whtAmount,double vatRate,double whtRate,double vatableCost) {

		String INSERT_QUERY = "INSERT INTO AM_ASSET_PAYMENT "
				+ "(ASSET_ID,PAYMENT,PAY_DATE,RAISED,PAY_DESC,tranId,Status,USER_ID,VATAMOUNT,WHTAMOUNT,VATRATE,WHTRATE,VATABLE_COST) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(INSERT_QUERY);
			ps.setString(1, assetId);
			ps.setDouble(2, pay);
			ps.setDate(3, dateConvert(payDate));
			ps.setString(4, raised);
			ps.setString(5, payDesc);
            ps.setInt(6, tranId);
            ps.setString(7, "A");
            ps.setString(8, userId);
			ps.setDouble(9, vatAmount);
			ps.setDouble(10, whtAmount);
			ps.setDouble(11, vatRate);
			ps.setDouble(12, whtRate);
			ps.setDouble(13, vatableCost);
			
			done = (ps.executeUpdate() != -1);
			notifyAsset(assetId,pay);

		} catch (Exception ex) {

		} finally {
			closeConnection(con, ps);
		}
		return done;
	}


	public boolean updatePaymentRecord(double pay,double ppay,
			 String payDesc,String payId,String assetid) {

		String UPDATE_QUERY = "UPDATE AM_ASSET_PAYMENT "
				+ "SET PAYMENT=?,PAY_DESC=? WHERE PAY_ID=? ";

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(UPDATE_QUERY);
		
			ps.setDouble(1, pay);
			//ps.setString(2, raised);
			ps.setString(2, payDesc);
			ps.setString(3, payId);

			done = (ps.executeUpdate() != -1);
			notifyuAsset(assetid, pay, ppay);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(con, ps);
		}
		return done;
	}
	public boolean raisePaymentRecord(String payId) {

		String UPDATE_QUERY = "UPDATE AM_ASSET_PAYMENT "
				+ " SET RAISED=? WHERE PAY_ID=? ";

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(UPDATE_QUERY);
		
		
			ps.setString(1, "R");
			ps.setString(2, payId);

			done = (ps.executeUpdate() != -1);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(con, ps);
		}
		return done;
	}
	public boolean isRaised(String payId) {

		String QUERY = "SELECT  RAISED FROM AM_ASSET_PAYMENT "
				+" WHERE PAY_ID=? ";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean done = false;

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(QUERY);
			ps.setString(1, payId);

			rs=ps.executeQuery();
			while(rs.next()){
				done = (rs.getString("RAISED")=="R");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(con, ps);
		}
		return done;
	}
	private ArrayList<AssetPayment> findPayments(String filter) {
		ArrayList<AssetPayment> found = new ArrayList<AssetPayment>();
		AssetPayment pay = null;
		String QUERY = "SELECT PAY_ID,ASSET_ID,PAYMENT,VATABLE_COST,VATAMOUNT,WHTAMOUNT,PAY_DATE,"
				+ "RAISED,PAY_DESC,tranId FROM AM_ASSET_PAYMENT ";
		QUERY += filter;
//		System.out.println("<<<<<<<<=====QUERY: "+QUERY);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(QUERY);
			rs = ps.executeQuery();
			while(rs.next()){
				
			String assetId = rs.getString("ASSET_ID");
			double payment = rs.getDouble("PAYMENT");
			double vatableCost = rs.getDouble("VATABLE_COST");
			double vatAmount = rs.getDouble("VATAMOUNT");
			double whtAmount = rs.getDouble("WHTAMOUNT");
			String payDate = sdf.format(rs.getDate("PAY_DATE"));
			String raised = rs.getString("RAISED");
			String payDesc = rs.getString("PAY_DESC");
			String payId =rs.getString("PAY_ID");
                        int tranId = rs.getInt("tranId");
			
			pay = new AssetPayment( assetId,  payment, vatableCost, vatAmount, whtAmount, payDate,  raised,  payDesc,tranId);
			pay.setPayId(payId);
			
			found.add(pay);
			}
			

		} catch (Exception ex) {

		} finally {
			closeConnection(con, ps);
		}

		return found;

	}
	public ArrayList<AssetPayment> findPaymentsByAssetID(String assetId) {
		ArrayList<AssetPayment> found = new ArrayList<AssetPayment>();
		String filter =" WHERE ASSET_ID ='"+assetId+"' and status ='A' ";
		found = findPayments(filter);
		return found;
	}
	public AssetPayment findPaymentsByPayID(String payId) {
		AssetPayment found = null;
		String filter =" WHERE PAY_ID ="+payId;
		found = findPayments(filter).get(0);
		return found;
	}
	public AssetPayment findPaymentsByPayTransID(String payId) {
		AssetPayment found = null;
		String filter =" WHERE TRANID ="+payId;
		found = findPayments(filter).get(0);
		return found;
	}	
	public ArrayList<AssetPayment> findPaymentsByQuery(String criterium) {
		ArrayList<AssetPayment> found = new ArrayList<AssetPayment>();
		String filter =" WHERE PAY_ID IS NOT NULL "+criterium;
		found = findPayments(filter);
		return found;
	}
	
	public boolean notifyAsset(String assetId,double pay) {

		String UPDATE_QUERY = "UPDATE AM_ASSET "
				+ " SET AMOUNT_PTD=AMOUNT_PTD+?,AMOUNT_REM=AMOUNT_REM-?, " +
						"FULLY_PAID=? WHERE ASSET_ID=? ";

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(UPDATE_QUERY);
		String paid="N";
		if(isFullyPaid(assetId)){ paid="Y";}
		
			ps.setDouble(1, pay);
			ps.setDouble(2, pay);
			ps.setString(3, paid);
			ps.setString(4, assetId);

			done = (ps.executeUpdate() != -1);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(con, ps);
		}
		return done;
	}
	public boolean notifyuAsset(String assetId,double pay,double ppay) {

		String UPDATE_QUERY = "UPDATE AM_ASSET "
				+ " SET AMOUNT_PTD=AMOUNT_PTD-?,AMOUNT_REM=AMOUNT_REM+?, " +
						"FULLY_PAID=? WHERE ASSET_ID=? ";
		
		String UPDATE_QUERY2 = "UPDATE AM_ASSET "
			+ " SET AMOUNT_PTD=AMOUNT_PTD+?,AMOUNT_REM=AMOUNT_REM-?, " +
					"FULLY_PAID=? WHERE ASSET_ID=? ";

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(UPDATE_QUERY);
		String paid="N";
		if(isFullyPaid(assetId)){ paid="Y";}
		
			ps.setDouble(1, ppay);
			ps.setDouble(2, ppay);
			ps.setString(3, paid);
			ps.setString(4, assetId);

			done = (ps.executeUpdate() != -1);
			
			ps = con.prepareStatement(UPDATE_QUERY2);
			ps.setDouble(1, pay);
			ps.setDouble(2, pay);
			ps.setString(3, paid);
			ps.setString(4, assetId);

			done = (ps.executeUpdate() != -1);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(con, ps);
		}
		return done;
	}
	public boolean isFullyPaid(String assetId) {

		String UPDATE_QUERY = "SELECT AMOUNT_REM FROM AM_ASSET "
				+ " WHERE ASSET_ID=? ";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean done = false;

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(UPDATE_QUERY);
			ps.setString(1, assetId);
			rs = ps.executeQuery();
                        while(rs.next()){
			done = (rs.getDouble("AMOUNT_REM") == 0.);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(con, ps);
		}
		return done;
	}
    // lanre new method for getting unique asset part payment
	public ArrayList<AssetPayment> findPaymentsByAssetID(String assetId,String pid) {
		ArrayList<AssetPayment> found = new ArrayList<AssetPayment>();
		String filter =" WHERE ASSET_ID ='"+assetId+"' AND PAY_ID = '"+pid+"' ";
		found = findPayments(filter);
		return found;
	}

	public ArrayList<AssetPayment> findPaymentsByAssetIDByTranId(String assetId,String pid) {
		ArrayList<AssetPayment> found = new ArrayList<AssetPayment>();
		String filter =" WHERE ASSET_ID ='"+assetId+"' AND TRANID = '"+pid+"' ";
		found = findPayments(filter);
		return found;
	}
	
         public boolean createPaymentRecord(String assetId, double pay,
			String payDate, String raised, String payDesc,int tranId,int assetCode,String userId) {

		String INSERT_QUERY = "INSERT INTO AM_ASSET_PAYMENT "
				+ "(ASSET_ID,PAYMENT,PAY_DATE,RAISED,PAY_DESC,tranId,Status,asset_code,USER_ID) "
				+ "VALUES(?,?,?,?,?,?,?,?,?)";

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;

		try { 
			con = getConnection("legendPlus");
			ps = con.prepareStatement(INSERT_QUERY);
			ps.setString(1, assetId);
			ps.setDouble(2, pay);
			ps.setDate(3, dateConvert(payDate));
			ps.setString(4, raised);
			ps.setString(5, payDesc);
            ps.setInt(6, tranId);
            ps.setString(7, "A");
            ps.setInt(8,assetCode);
            ps.setString(9,userId);
			done = (ps.executeUpdate() != -1);
			notifyAsset(assetId,pay);

		} catch (Exception ex) {
                    System.out.println("there is error in createPaymentRecord of AssetPaymentManager " +ex);
		} finally {
			closeConnection(con, ps);
		}
		return done;
	}

         public boolean createPaymentRecord(String assetId, double pay,
			String payDate, String raised, String payDesc,int tranId,int assetCode,String userId,double vatRate,double whtRate, 
			double vatAmount, double whtAmount,double VatableCost) {

		String INSERT_QUERY = "INSERT INTO AM_ASSET_PAYMENT "
				+ "(ASSET_ID,PAYMENT,PAY_DATE,RAISED,PAY_DESC,tranId,Status,asset_code,USER_ID,"
				+ "VATABLE_COST,VATRATE,WHTRATE,VATAMOUNT,WHTAMOUNT) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;

		try { 
			con = getConnection("legendPlus");
			ps = con.prepareStatement(INSERT_QUERY);
			ps.setString(1, assetId);
			ps.setDouble(2, pay);
			ps.setDate(3, dateConvert(payDate));
			ps.setString(4, raised);
			ps.setString(5, payDesc);
            ps.setInt(6, tranId);
            ps.setString(7, "A");
            ps.setInt(8,assetCode);
            ps.setString(9,userId);
            ps.setDouble(10, VatableCost);
            ps.setDouble(11, vatRate);
            ps.setDouble(12, whtRate);
            ps.setDouble(13, vatAmount);
            ps.setDouble(14, whtAmount);
			done = (ps.executeUpdate() != -1);
			notifyAsset(assetId,pay);

		} catch (Exception ex) {
                    System.out.println("there is error in createPaymentRecord of AssetPaymentManager " +ex);
		} finally {
			closeConnection(con, ps);
		}
		return done;
	}

         public boolean createPaymentRecord(String assetId, double pay,
			String payDate, String raised, String payDesc,int tranId,int assetCode,String userId,double vatRate,double whtRate, 
			double vatAmount, double whtAmount) {

		String INSERT_QUERY = "INSERT INTO AM_ASSET_PAYMENT "
				+ "(ASSET_ID,PAYMENT,PAY_DATE,RAISED,PAY_DESC,tranId,Status,asset_code,USER_ID,"
				+ "VATABLE_COST,VATRATE,WHTRATE,VATAMOUNT,WHTAMOUNT) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;

		try { 
			con = getConnection("legendPlus");
			ps = con.prepareStatement(INSERT_QUERY);
			ps.setString(1, assetId);
			ps.setDouble(2, pay);
			ps.setDate(3, dateConvert(payDate));
			ps.setString(4, raised);
			ps.setString(5, payDesc);
            ps.setInt(6, tranId);
            ps.setString(7, "A");
            ps.setInt(8,assetCode);
            ps.setString(9,userId);
            ps.setDouble(10, pay);
            ps.setDouble(11, vatRate);
            ps.setDouble(12, whtRate);
            ps.setDouble(13, vatAmount);
            ps.setDouble(14, whtAmount);
			done = (ps.executeUpdate() != -1);
			notifyAsset(assetId,pay);

		} catch (Exception ex) {
                    System.out.println("there is error in createPaymentRecord of AssetPaymentManager " +ex);
		} finally {
			closeConnection(con, ps);
		}
		return done;
	}
         
         public boolean createStockPaymentRecord(String assetId, double pay,
			String payDate, String raised, String payDesc,int tranId,int assetCode) {

		String INSERT_QUERY = "INSERT INTO AM_STOCK_PAYMENT "
				+ "(ASSET_ID,PAYMENT,PAY_DATE,RAISED,PAY_DESC,tranId,Status,asset_code) "
				+ "VALUES(?,?,?,?,?,?,?,?)";

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(INSERT_QUERY);
			ps.setString(1, assetId);
			ps.setDouble(2, pay);
			ps.setDate(3, dateConvert(payDate));
			ps.setString(4, raised);
			ps.setString(5, payDesc);
                        ps.setInt(6, tranId);
                        ps.setString(7, "A");
                        ps.setInt(8,assetCode);

			done = (ps.executeUpdate() != -1);
			notifyAsset(assetId,pay);

		} catch (Exception ex) {
                    System.out.println("there is error in createStockPaymentRecord of AssetPaymentManager " +ex);
		} finally {
			closeConnection(con, ps);
		}
		return done;
	}  
         
     	public ArrayList<AssetPayment> findPaymentsByStockID(String assetId,String pid) {
    		ArrayList<AssetPayment> found = new ArrayList<AssetPayment>();
    		String filter =" WHERE ASSET_ID ='"+assetId+"' AND PAY_ID = '"+pid+"' ";
    		found = findStockPayments(filter);
    		return found;
    	}
    	private ArrayList<AssetPayment> findStockPayments(String filter) {
    		ArrayList<AssetPayment> found = new ArrayList<AssetPayment>();
    		AssetPayment pay = null;
    		String QUERY = "SELECT PAY_ID,ASSET_ID,PAYMENT,PAY_DATE,"
    				+ "RAISED,PAY_DESC,tranId FROM AM_STOCK_PAYMENT ";
    		QUERY += filter;
    		//System.out.println("<<===findStockPayments QUERY: "+QUERY);
    		Connection con = null;
    		PreparedStatement ps = null;
    		ResultSet rs = null;

    		try {
    			con = getConnection("legendPlus");
    			ps = con.prepareStatement(QUERY);
    			rs = ps.executeQuery();
    			while(rs.next()){
    				
    			String assetId = rs.getString("ASSET_ID");
    			double payment = rs.getDouble("PAYMENT");
    			String payDate = sdf.format(rs.getDate("PAY_DATE"));
    			String raised = rs.getString("RAISED");
    			String payDesc = rs.getString("PAY_DESC");
    			String payId =rs.getString("PAY_ID");
                            int tranId = rs.getInt("tranId");
    			
    			pay = new AssetPayment( assetId,  payment,  payDate,  raised,  payDesc,tranId);
    			pay.setPayId(payId);
    			
    			found.add(pay);
    			}
    			

    		} catch (Exception ex) {

    		} finally {
    			closeConnection(con, ps);
    		}

    		return found;

    	}         

    	public AssetPayment findStockPaymentsByPayID(String payId) {
    		AssetPayment found = null;
    		String filter =" WHERE PAY_ID ="+payId;
    		found = findStockPayments(filter).get(0);
    		return found;
    	}

    	public AssetPayment findFacilityPaymentsById(String payId) {
    		AssetPayment found = null;
    		String filter =" WHERE transaction_id ="+payId;
    		found = findFacilityPayments(filter).get(0);
    		return found;
    	}
    	
    	private ArrayList<AssetPayment> findFacilityPayments(String filter) {
    		ArrayList<AssetPayment> found = new ArrayList<AssetPayment>();
    		AssetPayment pay = null;
    		String QUERY = "select batch_id, asset_id,amount,posting_date,description,transaction_id,* from am_asset_approval ";
    		QUERY += filter;
    		//System.out.println("<<===findFacilityPayments QUERY: "+QUERY);
    		Connection con = null; 
    		PreparedStatement ps = null;
    		ResultSet rs = null;
   
    		try {
    			con = getConnection("legendPlus");
    			ps = con.prepareStatement(QUERY);
    			rs = ps.executeQuery();
    			while(rs.next()){
    				
    			String assetId = rs.getString("ASSET_ID");
    			double payment = rs.getDouble("amount");
    			String payDate = sdf.format(rs.getDate("posting_date"));
    	//		String raised = rs.getString("RAISED");
    			String raised = "Y";
    			String payDesc = rs.getString("description");
    			String payId =rs.getString("batch_id");
                int tranId = rs.getInt("transaction_id");
    			
    			pay = new AssetPayment( assetId,  payment,  payDate,  raised,  payDesc,tranId);
    			pay.setPayId(payId);
    			
    			found.add(pay);
    			}
    			

    		} catch (Exception ex) {

    		} finally {
    			closeConnection(con, ps);
    		}

    		return found;
 
    	}         

    	public boolean updatePaymentRecord(double pay,double ppay,
    			 String payDesc,String payId,String assetid, double vatAmount,
    				double whtAmount,double vatRate,double whtRate,double vatableCost) {
    		
    		String UPDATE_QUERY = "UPDATE AM_ASSET_PAYMENT "
    				+ "SET PAYMENT=?,PAY_DESC=?,VATAMOUNT=?,WHTAMOUNT=?,VATRATE=?,WHTRATE=?,VATABLE_COST=? WHERE ASSET_ID = ? AND PAY_ID=? ";

    		Connection con = null;
    		PreparedStatement ps = null;
    		boolean done = false;

    		try {
    			con = getConnection("legendPlus");
    			ps = con.prepareStatement(UPDATE_QUERY);
    		
    			ps.setDouble(1, pay);
    			ps.setString(2, payDesc);
    			ps.setDouble(3, vatAmount);
    			ps.setDouble(4, whtAmount);
    			ps.setDouble(5, vatRate);
    			ps.setDouble(6, whtRate);
    			ps.setDouble(7, vatableCost);
    			ps.setString(8, assetid);
    			ps.setString(9, payId);
    			done = (ps.executeUpdate() != -1);
 //   			notifyuAsset(assetid, pay, ppay);
    			
    		} catch (Exception ex) {
    			ex.printStackTrace();
    		} finally {
    			closeConnection(con, ps);
    		}
    		return done;
    	}

        public void updateStatusSingleWIPPAYMENT(String payId,String assetid)
        {
            Connection con;
            PreparedStatement ps;
            String query = "";
            PreparedStatement ps1;
            String query2 = "";
            con = null;
            ps = null;
                query = "update am_asset_approval set PROCESS_STATUS='PR',ASSET_STATUS='REJECTED' where BATCH_ID!=? and asset_id=? and PROCESS_STATUS = 'R' and tran_type = 'Asset Part Payment'";
                query2 = "update AM_ASSET_PAYMENT set STATUS='R' where TRANID=? and asset_id=? ";
            try
            {
                con = getConnection("legendPlus");
                ps = con.prepareStatement(query);
                ps.setString(1, payId);
                ps.setString(2, assetid);
                ps.execute();
                ps1 = con.prepareStatement(query2);
                ps1.setString(1, payId);
                ps1.setString(2, assetid);
                ps1.execute();
            } catch (Exception ex) {

                System.out.println("WARNING:cannot update am_asset_approval And AM_ASSET_PAYMENT Status for Single->");
                ex.printStackTrace();
            } finally {
                closeConnection(con, ps);
            }
        }

        public void updateStatusGroupWIPPayment(String payId,String assetid)
        {
            Connection con;
            PreparedStatement ps;
            String query = "";
            PreparedStatement ps1;
            String query2 = "";
            con = null;
            ps = null;
                query = "update am_asset_approval set PROCESS_STATUS='PR',ASSET_STATUS='REJECTED' where BATCH_ID!=? and asset_id=? and PROCESS_STATUS = 'R' and tran_type = 'Group Asset Part Payment'";
                query2 = "update AM_ASSET_PAYMENT set STATUS='R' where TRANID=? and asset_id=? ";
            try
            {
                con = getConnection("legendPlus");
                ps = con.prepareStatement(query);
                ps.setString(1, payId);
                ps.setString(2, assetid);
                ps.execute();
                ps1 = con.prepareStatement(query2);
                ps1.setString(1, payId);
                ps1.setString(2, assetid);
                ps1.execute();
            } catch (Exception ex) {

                System.out.println("WARNING:cannot update am_asset_approval And AM_ASSET_PAYMENT Status For GROUP->");
                ex.printStackTrace();
            } finally {
                closeConnection(con, ps);
            }
        }

    	
}
