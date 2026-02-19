package magma.net.manager;

import java.text.SimpleDateFormat;
import java.sql.*;
import java.util.*;

import magma.net.dao.MagmaDBConnection;
import magma.net.vao.AssetPayment;
import magma.net.vao.GroupAssets;

public class GroupAssetPaymentManager extends MagmaDBConnection {
	SimpleDateFormat sdf;

	public GroupAssetPaymentManager() {
		super();
		sdf = new SimpleDateFormat("dd-MM-yyyy");
	}

	public boolean createPaymentRecord(String assetId, double pay,
			String payDate, String raised, String payDesc,String userId,int tranId) {

		String INSERT_QUERY = "INSERT INTO AM_ASSET_PAYMENT "
				+ "(ASSET_ID,PAYMENT,PAY_DATE,RAISED,PAY_DESC,TYPE,STATUS,USER_ID,tranId) "
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
			ps.setString(6, "G");
			ps.setString(7, "A");
			ps.setString(8, userId);
			ps.setInt(9, tranId);
			done = (ps.executeUpdate() != -1);
			notifyAsset(assetId, pay);
			// notifyGroupAssets(assetId,pay);
			notifyGroup(assetId, pay);

		} catch (Exception ex) {

		} finally {
			closeConnection(con, ps);
		}
		return done;
	}

	public boolean updatePaymentRecord(double pay, double ppay, String payDesc, String payId, String assetid) {

		String UPDATE_QUERY = "UPDATE AM_ASSET_PAYMENT "
				+ "SET PAYMENT=?,PAY_DESC=? WHERE PAY_ID=? ";

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(UPDATE_QUERY);

			ps.setDouble(1, pay);
			// ps.setString(2, raised);
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
	
	public boolean raiseGRecord(String payId) {

		String UPDATE_QUERY = "UPDATE AM_GROUP_ASSET_MAIN "
				+ " SET RAISE_ENTRY=? WHERE GROUP_ID=? ";

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
				+ " WHERE PAY_ID=? ";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean done = false;

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(QUERY);
			ps.setString(1, payId);

			rs = ps.executeQuery();
			while (rs.next()) {
				done = (rs.getString("RAISED") == "R");
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
		String QUERY = "SELECT PAY_ID,ASSET_ID,PAYMENT,PAY_DATE,"
				+ "RAISED,PAY_DESC FROM AM_ASSET_PAYMENT WHERE STATUS='A' ";
		QUERY += filter;     
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		//System.out.print("<<<<<<<<QUERY in findPayments: "+QUERY);
		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(QUERY);
			rs = ps.executeQuery();
			while (rs.next()) {

				String assetId = rs.getString("ASSET_ID");
				double payment = rs.getDouble("PAYMENT");
				String payDate = sdf.format(rs.getDate("PAY_DATE"));
				String raised = rs.getString("RAISED");
				String payDesc = rs.getString("PAY_DESC");
				String payId = rs.getString("PAY_ID");

				pay = new AssetPayment(assetId, payment, payDate, raised,
						payDesc);
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
		String filter = " AND ASSET_ID ='" + assetId + "'";
		found = findPayments(filter);
		return found;
	}

	public AssetPayment findPaymentsByPayID(String payId) {
		AssetPayment found = null;
		String filter = " AND PAY_ID =" + payId;
		found = findPayments(filter).get(0);
		return found;
	}

	public ArrayList<AssetPayment> findPaymentsByQuery(String criterium) {
		ArrayList<AssetPayment> found = new ArrayList<AssetPayment>();
		String filter = " AND PAY_ID IS NOT NULL " + criterium;
		found = findPayments(filter);
		return found;
	}

	public boolean notifyAsset(String assetId, double pay) {

		String UPDATE_QUERY = "UPDATE AM_ASSET "
				+ " SET AMOUNT_PTD=AMOUNT_PTD+?,AMOUNT_REM=AMOUNT_REM-?, "
				+ "FULLY_PAID=? WHERE GROUP_ID=? ";

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(UPDATE_QUERY);
			String paid = "N";
			if (isFullyPaid(assetId)) {
				paid = "Y";
			}

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
						"FULLY_PAID=? WHERE GROUP_ID=? ";
		
		String UPDATE_QUERY2 = "UPDATE AM_ASSET "
			+ " SET AMOUNT_PTD=AMOUNT_PTD+?,AMOUNT_REM=AMOUNT_REM-?, " +
					"FULLY_PAID=? WHERE GROUP_ID=? ";
		
		String UPDATE_QUERY3 = "UPDATE AM_GROUP_ASSET_MAIN "
			+ " SET AMOUNT_PTD=AMOUNT_PTD+?,AMOUNT_REM=AMOUNT_REM-?, " +
					"FULLY_PAID=? WHERE GROUP_ID=? ";
		
		String UPDATE_QUERY25 = "UPDATE AM_ASSET "
			+ " SET AMOUNT_PTD=AMOUNT_PTD-?,AMOUNT_REM=AMOUNT_REM+?, " +
					"FULLY_PAID=? WHERE GROUP_ID=? ";

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
			
			ps = con.prepareStatement(UPDATE_QUERY25);
			
			
				ps.setDouble(1, ppay);
				ps.setDouble(2, ppay);
				ps.setString(3, paid);
				ps.setString(4, assetId);
			
			ps = con.prepareStatement(UPDATE_QUERY3);
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

	public boolean notifyRAsset(String assetId) {

		String UPDATE_QUERY = "UPDATE AM_ASSET "
				+ " SET RAISE_ENTRY='R' WHERE GROUP_ID=? ";

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(UPDATE_QUERY);

			ps.setString(1, assetId);

			done = (ps.executeUpdate() != -1);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(con, ps);
		}
		return done;
	}

	public boolean deleteGAsset(String assetId, String groupid) {

		String UPDATE_QUERY = "DELETE FROM AM_GROUP_ASSET "
				+ "  WHERE ASSET_ID=? ";

		String UPDATE_QUERY2 = "UPDATE  AM_GROUP_ASSET_MAIN "
				+ " SET QUANTITY = QUANTITY-1, Cost_Price=(Cost_Price-Cost_Price/QUANTITY)," +
						"Vatable_Cost=(Vatable_Cost-Vatable_Cost/QUANTITY) WHERE GROUP_ID=? ";

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(UPDATE_QUERY);
			ps.setString(1, assetId);
			done = (ps.executeUpdate() != -1);

			ps = con.prepareStatement(UPDATE_QUERY2);
			ps.setString(1, groupid);
			done = (ps.executeUpdate() != -1);

		
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(con, ps);
		}
		return done;
	}
	public boolean deleteUploadGAsset(String assetId, String groupid) {

		String UPDATE_QUERY = "DELETE FROM AM_GROUP_ASSET "
				+ "  WHERE ASSET_ID=? ";

		String UPDATE_QUERY2 = "UPDATE  AM_GROUP_ASSET_MAIN "
				+ " SET QUANTITY = QUANTITY-1, Cost_Price=(Cost_Price-Cost_Price/QUANTITY)," +
						"Vatable_Cost=(Vatable_Cost-Vatable_Cost/QUANTITY) WHERE CONVERT(VARCHAR,GROUP_ID)=? ";

		String UPDATE_QUERY3 = "DELETE FROM AM_ASSET "
			+ "  WHERE ASSET_ID=? ";
		
		String UPDATE_QUERY4 = "DELETE FROM AM_INVOICE_NO WHERE ASSET_ID=?";
		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(UPDATE_QUERY);
			ps.setString(1, assetId);
			done = (ps.executeUpdate() != -1);

			ps = con.prepareStatement(UPDATE_QUERY2);
			ps.setString(1, groupid);
			done = (ps.executeUpdate() != -1);

			ps = con.prepareStatement(UPDATE_QUERY3);
			ps.setString(1, assetId);
			done = (ps.executeUpdate() != -1);
			
			ps = con.prepareStatement(UPDATE_QUERY4);
			ps.setString(1, groupid);
			done = (ps.executeUpdate() != -1);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(con, ps);
		}
		return done;
	}

	public boolean deleteReconcileUploadAsset(String assetId, String groupid) {

		String UPDATE_QUERY = "DELETE FROM AM_GROUP_RECONCILIATION "
				+ "  WHERE ASSET_ID=? ";
		
		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(UPDATE_QUERY);
			ps.setString(1, assetId);
			done = (ps.executeUpdate() != -1);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(con, ps);
		}
		return done;
	}


	public boolean notifyGroup(String assetId, double pay) {

		String UPDATE_QUERY = "UPDATE AM_GROUP_ASSET_MAIN  "
				+ " SET AMOUNT_PTD=AMOUNT_PTD+?,AMOUNT_REM=AMOUNT_REM-?, "
				+ "FULLY_PAID=? WHERE GROUP_ID=? ";

		Connection con = null;
		PreparedStatement ps = null; 
		boolean done = false;

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(UPDATE_QUERY);
			String paid = "N";
			if (isFullyPaid(assetId)) {
				paid = "Y";
			}

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

	public boolean notifyRGroup(String assetId) {

		String UPDATE_QUERY = "UPDATE AM_GROUP_ASSET_MAIN  "
				+ " SET RAISED_ENTRY='R' WHERE GROUP_ID=? ";

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(UPDATE_QUERY);

			ps.setString(1, assetId);

			done = (ps.executeUpdate() != -1);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(con, ps);
		}
		return done;
	}

	public boolean isFullyPaid(String assetId) {

		String UPDATE_QUERY = "SELECT DISTINCT AMOUNT_REM FROM AM_ASSET "
				+ " WHERE GROUP_ID=? ";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean done = false;

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(UPDATE_QUERY);
			ps.setString(1, assetId);
			while (rs.next()) {
				rs = ps.executeQuery();
				done = (rs.getDouble("AMOUNT_REM") == 0.);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(con, ps);
		}
		return done;
	} 

	public ArrayList<GroupAssets> findGroupMainAssetByQuery(String queryFilter,String branch, String deptCode, String category, String registrationNumber, String assetId, String FromDate,String ToDate,String payType,String raiseEntry,String status,String order) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		GroupAssets groupAsset = null;
		ArrayList<GroupAssets> finder = new ArrayList<GroupAssets>();
		String selectQuery = "SELECT group_id, quantity, Registration_No, "
				+ "Branch_ID, Dept_ID, Category_ID, section_id, Description, "
				+ "Vendor_AC, Date_purchased, dep_rate, asset_make, asset_model, "
				+ "asset_serial_no, asset_engine_no, supplier_name, asset_user, "
				+ "asset_maintenance, Cost_Price, dep_end_date, residual_value, "
				+ "authorized_by, wh_tax, wh_tax_amount, req_redistribution, "
				+ "Posting_Date, effective_date, purchase_reason, location, "
				+ "Vatable_Cost, Vat, Req_Depreciation, Subject_TO_Vat, "
				+ "Who_TO_Rem, email1, who_to_rem_2, email2, Raise_entry, "
				+ "dep_ytd, Section, Asset_Status, state, driver, spare_1, "
				+ "spare_2, user_ID, province, WAR_START_DATE, WAR_MONTH, "
				+ "WAR_EXPIRY_DATE, branch_code, dept_code, section_code, "
				+ "category_code,AMOUNT_PTD,AMOUNT_REM,PART_PAY,FULLY_PAID "
				+ " FROM am_group_asset_main " + queryFilter +" " + order;
		//System.out.println("<<<<selectQuery: "+selectQuery);
		try { 
//			con = getConnection("legendPlus");
//			ps = con.prepareStatement(selectQuery);
//			rs = ps.executeQuery();
			
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery.toString());
          if(queryFilter.contains("BRANCH_ID") && !queryFilter.contains("DEPT_ID") && !queryFilter.contains("CATEGORY_ID") && !queryFilter.contains("POSTING_DATE")){  
        	  System.out.println("<========getAssetByQuery=======> Branch: "+branch);
        	  ps.setString(1, branch);
          }  

          if(queryFilter.contains("BRANCH_ID") && queryFilter.contains("DEPT_ID") && !queryFilter.contains("CATEGORY_ID") && !queryFilter.contains("POSTING_DATE")){  
        	  System.out.println("<========getAssetByQuery=======>Branch and Department: ");
        	  ps.setString(1, deptCode);
        	  ps.setString(2, branch);
          }
          
          if(!queryFilter.contains("BRANCH_ID") && queryFilter.contains("DEPT_ID") && !queryFilter.contains("CATEGORY_ID") && !queryFilter.contains("POSTING_DATE")){  
        	  System.out.println("<========getAssetByQuery=======>Category and Department: ");
        	  ps.setString(1, category);
        	  ps.setString(2, deptCode);
          }
          
          if(queryFilter.contains("BRANCH_ID") && !queryFilter.contains("DEPT_ID") && queryFilter.contains("CATEGORY_ID") && !queryFilter.contains("POSTING_DATE")){  
        	  System.out.println("<========getAssetByQuery=======>Branch and Catgeory: ");
        	  ps.setString(1, category);
        	  ps.setString(2, branch);   	
          }  
          if(queryFilter.contains("PART_PAY") && queryFilter.contains("RAISE_ENTRY")){  
        	  //System.out.println("<========getAssetByQuery=======>0 status: "+status);
        	  ps.setString(1, payType);
        	  ps.setString(2, raiseEntry);
          }  
          if(queryFilter.contains("BRANCH_ID") && queryFilter.contains("CATEGORY_ID") && queryFilter.contains("DEPT_ID") && !queryFilter.contains("POSTING_DATE") && !queryFilter.contains("RAISE_ENTRY")){
        	  System.out.println("<========getAssetByQuery=======>Category, Branch and Department");
        	  ps.setString(1, category);
        	  ps.setString(2, deptCode);
        	  ps.setString(3, branch);
          }       
//          if(queryFilter.contains("BRANCH_ID") && queryFilter.contains("DEPT_ID") && queryFilter.contains("POSTING_DATE")){
//        	  //System.out.println("<========getAssetByQuery=======>2");
//        	  ps.setString(1, branch);
//        	  ps.setString(2, branch);
//        	  ps.setString(3, deptCode);
//        	  ps.setString(4, FromDate);
//        	  ps.setString(5, ToDate);
//          }   
          if(queryFilter.contains("BRANCH_ID") && !queryFilter.contains("CATEGORY_ID") && queryFilter.contains("DEPT_ID") && queryFilter.contains("POSTING_DATE")){
        	  //System.out.println("<========getAssetByQuery=======>2");
        	  ps.setString(1, deptCode);
        	  ps.setString(2, branch);
        	  ps.setString(3, FromDate);
        	  ps.setString(4, ToDate);
          }   
//          if(queryFilter.contains("BRANCH_ID") && queryFilter.contains("CATEGORY_ID") && queryFilter.contains("DEPT_ID") && queryFilter.contains("POSTING_DATE")){
//        	  //System.out.println("<========getAssetByQuery=======>3");
//        	  ps.setString(1, branch);
//        	  ps.setString(2, category);
//        	  ps.setString(3, branch);
//        	  ps.setString(4, deptCode);
//        	  ps.setString(5, FromDate);
//        	  ps.setString(6, ToDate);
//          }   
          if(queryFilter.contains("BRANCH_ID") && queryFilter.contains("CATEGORY_ID") && queryFilter.contains("DEPT_ID") && queryFilter.contains("POSTING_DATE") && !queryFilter.contains("RAISE_ENTRY") && !queryFilter.contains("PART_PAY")){
        	  System.out.println("<========getAssetByQuery=======>Category, Branch Department and Posting Date");
        	  ps.setString(1, category);
        	  ps.setString(2, deptCode);
        	  ps.setString(3, branch); 
        	  ps.setString(4, FromDate);
        	  ps.setString(5, ToDate);
          }   
          
          if(queryFilter.contains("BRANCH_ID") && queryFilter.contains("CATEGORY_ID") && !queryFilter.contains("DEPT_ID") && queryFilter.contains("POSTING_DATE") && !queryFilter.contains("RAISE_ENTRY") && !queryFilter.contains("PART_PAY")){
        	  System.out.println("<========getAssetByQuery=======>Category, Branch and Posting Date");
        	  ps.setString(1, category);
        	  ps.setString(2, branch); 
        	  ps.setString(3, FromDate);
        	  ps.setString(4, ToDate);
          }   
          
          if(!queryFilter.contains("BRANCH_ID") && !queryFilter.contains("CATEGORY_ID") && queryFilter.contains("DEPT_ID") && queryFilter.contains("POSTING_DATE") && !queryFilter.contains("RAISE_ENTRY") && !queryFilter.contains("PART_PAY")){
        	  System.out.println("<========getAssetByQuery=======> Department and Posting Date");
        	  ps.setString(1, deptCode);
        	  ps.setString(2, FromDate);
        	  ps.setString(3, ToDate);
          }   

          if(queryFilter.contains("BRANCH_ID") && !queryFilter.contains("CATEGORY_ID") && !queryFilter.contains("DEPT_ID") && queryFilter.contains("POSTING_DATE") && !queryFilter.contains("RAISE_ENTRY") && !queryFilter.contains("PART_PAY")){
        	  System.out.println("<========getAssetByQuery=======> Branch and Posting Date");
        	  ps.setString(1, branch);
        	  ps.setString(2, FromDate);
        	  ps.setString(3, ToDate);
          }   
          
          if(!queryFilter.contains("BRANCH_ID") && queryFilter.contains("CATEGORY_ID") && !queryFilter.contains("DEPT_ID") && queryFilter.contains("POSTING_DATE") && !queryFilter.contains("RAISE_ENTRY") && !queryFilter.contains("PART_PAY")){
        	  System.out.println("<========getAssetByQuery=======> Category and Posting Date");
        	  ps.setString(1, category);
        	  ps.setString(2, FromDate);
        	  ps.setString(3, ToDate);
          }   
          
          if(!queryFilter.contains("BRANCH_ID") && !queryFilter.contains("CATEGORY_ID") && !queryFilter.contains("DEPT_ID") && queryFilter.contains("POSTING_DATE") && !queryFilter.contains("RAISE_ENTRY") && !queryFilter.contains("PART_PAY")){
        	  System.out.println("<========getAssetByQuery=======> Posting Date");
        	  ps.setString(1, FromDate);
        	  ps.setString(2, ToDate);
          }   
          
          if(queryFilter.contains("PART_PAY") && queryFilter.contains("RAISE_ENTRY") && queryFilter.contains("BRANCH_ID")){  
        	  //System.out.println("<========getAssetByQuery=======>0 status: "+status);
        	  ps.setString(1, payType);
        	  ps.setString(2, raiseEntry);
        	  ps.setString(3, branch);
          }  
          if(queryFilter.contains("PART_PAY") && queryFilter.contains("RAISE_ENTRY") && queryFilter.contains("CATEGORY_ID") && queryFilter.contains("BRANCH_ID")){  
        	  //System.out.println("<========getAssetByQuery=======>0 status: "+status);
        	  ps.setString(1, payType);
        	  ps.setString(2, raiseEntry);
        	  ps.setString(3, category);
        	  ps.setString(4, branch);
          }  
          if(queryFilter.contains("PART_PAY") && queryFilter.contains("RAISE_ENTRY") && queryFilter.contains("BRANCH_ID") && queryFilter.contains("DATE_PURCHASED")){  
        	  //System.out.println("<========getAssetByQuery=======>0 status: "+status);
        	  ps.setString(1, payType);
        	  ps.setString(2, raiseEntry);
        	  ps.setString(3, branch);
        	  ps.setString(4, FromDate);
        	  ps.setString(5, ToDate);
          }  
          if(queryFilter.contains("PART_PAY") && queryFilter.contains("RAISE_ENTRY") && queryFilter.contains("CATEGORY_ID") && queryFilter.contains("BRANCH_ID") && queryFilter.contains("DATE_PURCHASED")){  
        	  //System.out.println("<========getAssetByQuery=======>0 status: "+status);
        	  ps.setString(1, payType);
        	  ps.setString(2, raiseEntry);
        	  ps.setString(3, category);
        	  ps.setString(4, branch);
        	  ps.setString(5, FromDate);
        	  ps.setString(6, ToDate);
          }  
            rs = ps.executeQuery();


			while (rs.next()) {
				groupAsset = new GroupAssets();
				groupAsset.setGid(rs.getLong("GROUP_ID"));
				groupAsset.setAmountPTD(rs.getString("AMOUNT_PTD"));
				groupAsset.setAmountREM(rs.getString("AMOUNT_REM"));
				groupAsset.setCategory_id(rs.getString("Category_ID"));
				groupAsset.setCost_price(rs.getString("cost_price"));
				groupAsset.setDate_of_purchase(sdf.format(rs.getDate("Date_purchased")));
				groupAsset.setDepartment_id(rs.getString("Dept_ID"));
				groupAsset.setDescription(rs.getString("description"));
				groupAsset.setFullyPAID(rs.getString("FULLY_PAID"));
				groupAsset.setLocation(rs.getString("location"));
				groupAsset.setMake(rs.getString("asset_make"));
				groupAsset.setModel(rs.getString("asset_model"));
				groupAsset.setPartPAY(rs.getString("PART_PAY"));
				groupAsset.setPosting_date(sdf.format(rs.getDate("posting_date")));
				groupAsset.setProvince(rs.getString("province"));
				groupAsset.setQuantity(rs.getInt("quantity"));
				groupAsset.setBranch_id(rs.getString("Branch_ID"));
				groupAsset.setSection_id(rs.getString("section_id"));
				groupAsset.setState(rs.getString("state"));
				groupAsset.setVendor_account(rs.getString("Vendor_AC"));
				groupAsset.setVat_amount(rs.getString("Vat"));
				groupAsset.setWh_tax_amount(rs.getString("wh_tax_amount"));
				groupAsset.setVatable_cost(rs.getString("Vatable_Cost"));
				groupAsset.setRaise_entry(rs.getString("Raise_entry"));
				groupAsset.setUser_id(rs.getString("user_id"));
				finder.add(groupAsset);

			}

		} catch (Exception e) {
			System.out.println("INFO:Error Fetching GroupAsset Records ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps, rs);
		}
		return finder;
	}


	public ArrayList<GroupAssets> findGroupMainAssetByQuery(String queryFilter) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		GroupAssets groupAsset = null;
		ArrayList<GroupAssets> finder = new ArrayList<GroupAssets>();
		String selectQuery = "SELECT group_id, quantity, Registration_No, "
				+ "Branch_ID, Dept_ID, Category_ID, section_id, Description, "
				+ "Vendor_AC, Date_purchased, dep_rate, asset_make, asset_model, "
				+ "asset_serial_no, asset_engine_no, supplier_name, asset_user, "
				+ "asset_maintenance, Cost_Price, dep_end_date, residual_value, "
				+ "authorized_by, wh_tax, wh_tax_amount, req_redistribution, "
				+ "Posting_Date, effective_date, purchase_reason, location, "
				+ "Vatable_Cost, Vat, Req_Depreciation, Subject_TO_Vat, "
				+ "Who_TO_Rem, email1, who_to_rem_2, email2, Raise_entry, "
				+ "dep_ytd, Section, Asset_Status, state, driver, spare_1, "
				+ "spare_2, user_ID, province, WAR_START_DATE, WAR_MONTH, "
				+ "WAR_EXPIRY_DATE, branch_code, dept_code, section_code, "
				+ "category_code,AMOUNT_PTD,AMOUNT_REM,PART_PAY,FULLY_PAID "
				+ " FROM am_group_asset_main " + queryFilter;
//		System.out.println("<<<<selectQuery: "+selectQuery);
		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(selectQuery);
			rs = ps.executeQuery();

			while (rs.next()) {
				groupAsset = new GroupAssets();
				groupAsset.setGid(rs.getLong("GROUP_ID"));
				groupAsset.setAmountPTD(rs.getString("AMOUNT_PTD"));
				groupAsset.setAmountREM(rs.getString("AMOUNT_REM"));
				groupAsset.setCategory_id(rs.getString("Category_ID"));
				groupAsset.setCost_price(rs.getString("cost_price"));
				groupAsset.setDate_of_purchase(sdf.format(rs.getDate("Date_purchased")));
				groupAsset.setDepartment_id(rs.getString("Dept_ID"));
				groupAsset.setDescription(rs.getString("description"));
				groupAsset.setFullyPAID(rs.getString("FULLY_PAID"));
				groupAsset.setLocation(rs.getString("location"));
				groupAsset.setMake(rs.getString("asset_make"));
				groupAsset.setModel(rs.getString("asset_model"));
				groupAsset.setPartPAY(rs.getString("PART_PAY"));
				groupAsset.setPosting_date(sdf.format(rs.getDate("posting_date")));
				groupAsset.setProvince(rs.getString("province"));
				groupAsset.setQuantity(rs.getInt("quantity"));
				groupAsset.setBranch_id(rs.getString("Branch_ID"));
				groupAsset.setSection_id(rs.getString("section_id"));
				groupAsset.setState(rs.getString("state"));
				groupAsset.setVendor_account(rs.getString("Vendor_AC"));
				groupAsset.setVat_amount(rs.getString("Vat"));
				groupAsset.setWh_tax_amount(rs.getString("wh_tax_amount"));
				groupAsset.setVatable_cost(rs.getString("Vatable_Cost"));
				groupAsset.setRaise_entry(rs.getString("Raise_entry"));
				groupAsset.setUser_id(rs.getString("user_id"));
				finder.add(groupAsset);

			}

		} catch (Exception e) {
			System.out.println("INFO:Error Fetching GroupAsset Records ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps, rs);
		}
		return finder;
	}

	
	public boolean isAssetMoved(String assetId) {

		String UPDATE_QUERY = "SELECT COUNT(*) FROM AM_GROUP_ASSET "
				+ " WHERE GROUP_ID=? ";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean done = false;

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(UPDATE_QUERY);
			ps.setString(1, assetId);
			rs = ps.executeQuery();
			while (rs.next()) {
				done = (rs.getInt(1) > 0);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(con, ps);
		}
		return done;
	}
public ArrayList<GroupAssets> findGroupMainAssetByQueryBranch(String queryFilter) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		GroupAssets groupAsset = null;
		ArrayList<GroupAssets> finder = new ArrayList<GroupAssets>();
		String selectQuery = "SELECT distinct (a.group_id), a.quantity, a.Registration_No, "
				+ "a.Branch_ID, a.Dept_ID, a.Category_ID, a.section_id, a.Description, "
				+ "a.Vendor_AC, a.Date_purchased, a.dep_rate, a.asset_make, a.asset_model, "
				+ "a.asset_serial_no, a.asset_engine_no, a.supplier_name, a.asset_user, "
				+ "a.asset_maintenance, a.Cost_Price, a.dep_end_date, a.residual_value, "
				+ "a.authorized_by, a.wh_tax, a.wh_tax_amount, a.req_redistribution, "
				+ "a.Posting_Date, a.effective_date, a.purchase_reason, a.location, "
				+ "a.Vatable_Cost, a.Vat, a.Req_Depreciation, a.Subject_TO_Vat, "
				+ "a.Who_TO_Rem, a.email1, a.who_to_rem_2, a.email2, a.Raise_entry, "
				+ "a.dep_ytd, a.Section, a.Asset_Status, a.state, a.driver, a.spare_1, "
				+ "a.spare_2, a.user_ID, a.province, a.WAR_START_DATE, a.WAR_MONTH, "
				+ "a.WAR_EXPIRY_DATE, a.branch_code, a.dept_code, a.section_code, "
				+ "a.category_code,a.AMOUNT_PTD,a.AMOUNT_REM,a.PART_PAY,a.FULLY_PAID "
				+ " FROM am_group_asset_main a join AM_GROUP_ASSET_UNCAPITALIZED b on a.group_id =b.group_id  " + queryFilter;

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(selectQuery);
			rs = ps.executeQuery();

			while (rs.next()) {
				groupAsset = new GroupAssets();
				groupAsset.setGid(rs.getLong("GROUP_ID"));
				groupAsset.setAmountPTD(rs.getString("AMOUNT_PTD"));
				groupAsset.setAmountREM(rs.getString("AMOUNT_REM"));
				groupAsset.setCategory_id(rs.getString("Category_ID"));
				groupAsset.setCost_price(rs.getString("cost_price"));
				groupAsset.setDate_of_purchase(sdf.format(rs
						.getDate("Date_purchased")));
				groupAsset.setDepartment_id(rs.getString("Dept_ID"));
				groupAsset.setDescription(rs.getString("description"));
				groupAsset.setFullyPAID(rs.getString("FULLY_PAID"));
				groupAsset.setLocation(rs.getString("location"));
				groupAsset.setMake(rs.getString("asset_make"));
				groupAsset.setModel(rs.getString("asset_model"));
				groupAsset.setPartPAY(rs.getString("PART_PAY"));
				groupAsset.setPosting_date(sdf.format(rs
						.getDate("posting_date")));
				groupAsset.setProvince(rs.getString("province"));
				groupAsset.setQuantity(rs.getInt("quantity"));
				groupAsset.setBranch_id(rs.getString("Branch_ID"));
				groupAsset.setSection_id(rs.getString("section_id"));
				groupAsset.setState(rs.getString("state"));
				groupAsset.setVendor_account(rs.getString("Vendor_AC"));
				groupAsset.setVat_amount(rs.getString("Vat"));
				groupAsset.setWh_tax_amount(rs.getString("wh_tax_amount"));
				groupAsset.setVatable_cost(rs.getString("Vatable_Cost"));
				groupAsset.setRaise_entry(rs.getString("Raise_entry"));
				groupAsset.setUser_id(rs.getString("user_id"));
				finder.add(groupAsset);

			}

		} catch (Exception e) {
			System.out.println("INFO:Error Fetching GroupAsset Records ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps, rs);
		}
		return finder;
	}

	public boolean deleteGAssetBranch(String assetId, String groupid) {

		String UPDATE_QUERY = "DELETE FROM AM_GROUP_ASSET_UNCAPITALIZED "
				+ "  WHERE ASSET_ID=? ";

		String UPDATE_QUERY2 = "UPDATE  AM_GROUP_ASSET_MAIN "
				+ " SET QUANTITY = QUANTITY-1, Cost_Price=(Cost_Price-Cost_Price/QUANTITY)," +
						"Vatable_Cost=(Vatable_Cost-Vatable_Cost/QUANTITY) WHERE GROUP_ID=? ";

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(UPDATE_QUERY);
			ps.setString(1, assetId);
			done = (ps.executeUpdate() != -1);

			ps = con.prepareStatement(UPDATE_QUERY2);
			ps.setString(1, groupid);
			done = (ps.executeUpdate() != -1);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(con, ps);
		}
		return done;
	}

	public boolean deleteGAsset2Branch(String assetId, String groupid) {

		String UPDATE_QUERY = "DELETE FROM AM_GROUP_ASSET2_UNCAPITALIZED "
				+ "  WHERE ASSET_ID=? ";

		String UPDATE_QUERY2 = "UPDATE  AM_GROUP_ASSET2_MAIN "
				+ " SET QUANTITY = QUANTITY-1, Cost_Price=(Cost_Price-Cost_Price/QUANTITY)," +
						"Vatable_Cost=(Vatable_Cost-Vatable_Cost/QUANTITY) WHERE GROUP_ID=? ";

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(UPDATE_QUERY);
			ps.setString(1, assetId);
			done = (ps.executeUpdate() != -1);

			ps = con.prepareStatement(UPDATE_QUERY2);
			ps.setString(1, groupid);
			done = (ps.executeUpdate() != -1);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(con, ps);
		}
		return done;
	}


	public ArrayList<GroupAssets> findGroupMainAssetByQuery2(String queryFilter) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		GroupAssets groupAsset = null;
		ArrayList<GroupAssets> finder = new ArrayList<GroupAssets>();
		String selectQuery = "SELECT distinct (a.group_id), a.quantity, a.Registration_No, "
			+ "a.Branch_ID, a.Dept_ID, a.Category_ID, a.section_id, a.Description, "
			+ "a.Vendor_AC, a.Date_purchased, a.dep_rate, a.asset_make, a.asset_model, "
			+ "a.asset_serial_no, a.asset_engine_no, a.supplier_name, a.asset_user, "
			+ "a.asset_maintenance, a.Cost_Price, a.dep_end_date, a.residual_value, "
			+ "a.authorized_by, a.wh_tax, a.wh_tax_amount, a.req_redistribution, "
			+ "a.Posting_Date, a.effective_date, a.purchase_reason, a.location, "
			+ "a.Vatable_Cost, a.Vat, a.Req_Depreciation, a.Subject_TO_Vat, "
			+ "a.Who_TO_Rem, a.email1, a.who_to_rem_2, a.email2, a.Raise_entry, "
			+ "a.dep_ytd, a.Section, a.Asset_Status, a.state, a.driver, a.spare_1, "
			+ "a.spare_2, a.user_ID, a.province, a.WAR_START_DATE, a.WAR_MONTH, "
			+ "a.WAR_EXPIRY_DATE, a.branch_code, a.dept_code, a.section_code, "
			+ "a.category_code,a.AMOUNT_PTD,a.AMOUNT_REM,a.PART_PAY,a.FULLY_PAID "
			+ " FROM am_group_asset_main a join AM_GROUP_ASSET b on a.group_id =b.group_id  " + queryFilter;

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(selectQuery);
			rs = ps.executeQuery();

			while (rs.next()) {
				groupAsset = new GroupAssets();
				groupAsset.setGid(rs.getLong("GROUP_ID"));
				groupAsset.setAmountPTD(rs.getString("AMOUNT_PTD"));
				groupAsset.setAmountREM(rs.getString("AMOUNT_REM"));
				groupAsset.setCategory_id(rs.getString("Category_ID"));
				groupAsset.setCost_price(rs.getString("cost_price"));
				groupAsset.setDate_of_purchase(sdf.format(rs
						.getDate("Date_purchased")));
				groupAsset.setDepartment_id(rs.getString("Dept_ID"));
				groupAsset.setDescription(rs.getString("description"));
				groupAsset.setFullyPAID(rs.getString("FULLY_PAID"));
				groupAsset.setLocation(rs.getString("location"));
				groupAsset.setMake(rs.getString("asset_make"));
				groupAsset.setModel(rs.getString("asset_model"));
				groupAsset.setPartPAY(rs.getString("PART_PAY"));
				groupAsset.setPosting_date(sdf.format(rs
						.getDate("posting_date")));
				groupAsset.setProvince(rs.getString("province"));
				groupAsset.setQuantity(rs.getInt("quantity"));
				groupAsset.setBranch_id(rs.getString("Branch_ID"));
				groupAsset.setSection_id(rs.getString("section_id"));
				groupAsset.setState(rs.getString("state"));
				groupAsset.setVendor_account(rs.getString("Vendor_AC"));
				groupAsset.setVat_amount(rs.getString("Vat"));
				groupAsset.setWh_tax_amount(rs.getString("wh_tax_amount"));
				groupAsset.setVatable_cost(rs.getString("Vatable_Cost"));
				groupAsset.setRaise_entry(rs.getString("Raise_entry"));
				groupAsset.setUser_id(rs.getString("user_id"));
				finder.add(groupAsset);

			}

		} catch (Exception e) {
			System.out.println("INFO:Error Fetching GroupAsset Records ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps, rs);
		}
		return finder;
	}
	
	public ArrayList<GroupAssets> findGroupMainAssetByQuery2(String queryFilter,String branch,String deptCode,String category,String registrationNumber,String assetId,String FromDate,String ToDate,String status, String ordering) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		GroupAssets groupAsset = null;
		ArrayList<GroupAssets> finder = new ArrayList<GroupAssets>();
		String selectQuery = "SELECT distinct (a.group_id), a.quantity, a.Registration_No, "
			+ "a.Branch_ID, a.Dept_ID, a.Category_ID, a.section_id, a.Description, "
			+ "a.Vendor_AC, a.Date_purchased, a.dep_rate, a.asset_make, a.asset_model, "
			+ "a.asset_serial_no, a.asset_engine_no, a.supplier_name, a.asset_user, "
			+ "a.asset_maintenance, a.Cost_Price, a.dep_end_date, a.residual_value, "
			+ "a.authorized_by, a.wh_tax, a.wh_tax_amount, a.req_redistribution, "
			+ "a.Posting_Date, a.effective_date, a.purchase_reason, a.location, "
			+ "a.Vatable_Cost, a.Vat, a.Req_Depreciation, a.Subject_TO_Vat, "
			+ "a.Who_TO_Rem, a.email1, a.who_to_rem_2, a.email2, a.Raise_entry, "
			+ "a.dep_ytd, a.Section, a.Asset_Status, a.state, a.driver, a.spare_1, "
			+ "a.spare_2, a.user_ID, a.province, a.WAR_START_DATE, a.WAR_MONTH, "
			+ "a.WAR_EXPIRY_DATE, a.branch_code, a.dept_code, a.section_code, "
			+ "a.category_code,a.AMOUNT_PTD,a.AMOUNT_REM,a.PART_PAY,a.FULLY_PAID "
			+ " FROM am_group_asset_main a join AM_GROUP_ASSET b on a.group_id =b.group_id  " + queryFilter;

		try {
//			con = getConnection("legendPlus");
//			ps = con.prepareStatement(selectQuery);
//			rs = ps.executeQuery();

            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery.toString());
          if(queryFilter.contains("a.Asset_Status") && !queryFilter.contains("a.BRANCH_ID") && !queryFilter.contains("a.CATEGORY_ID") && !queryFilter.contains("a.DEPT_ID") && !queryFilter.contains("a.POSTING_DATE")){ 
        //	  System.out.println("<========getAssetByQuery=======>status: ");
        	  ps.setString(1, status);
          }     
          if(queryFilter.contains("a.Asset_Status") && queryFilter.contains("a.BRANCH_ID")  && !queryFilter.contains("a.CATEGORY_ID") && !queryFilter.contains("a.DEPT_ID") && !queryFilter.contains("a.POSTING_DATE")){  
        //	  System.out.println("<========getAssetByQuery=======>0 status: "+status);
        	  ps.setString(1, status);
        	  ps.setString(2, branch);
          }  
          if(queryFilter.contains("a.Asset_Status") && queryFilter.contains("a.BRANCH_ID") && queryFilter.contains("a.DEPT_ID")  && !queryFilter.contains("a.CATEGORY_ID") && !queryFilter.contains("a.POSTING_DATE")){
        	//  System.out.println("<========getAssetByQuery=======>1 branch_Id: "+branch+"    status: "+status);
        	  ps.setString(1, status);
        	  ps.setString(2, branch);
        	  ps.setString(3, deptCode);
          }       
          if(queryFilter.contains("a.Asset_Status") && queryFilter.contains("a.CATEGORY_ID") && queryFilter.contains("a.BRANCH_ID") && queryFilter.contains("a.DEPT_ID") && !queryFilter.contains("a.POSTING_DATE")){
        	//  System.out.println("<========getAssetByQuery=======>2");
        	  ps.setString(1, status);
        	  ps.setString(2, category);
        	  ps.setString(3, branch);
        	  ps.setString(4, deptCode);
          }  
          if(queryFilter.contains("a.Asset_Status") && !queryFilter.contains("a.CATEGORY_ID") && queryFilter.contains("a.BRANCH_ID") && queryFilter.contains("a.DEPT_ID") && queryFilter.contains("a.POSTING_DATE")){
        	 // System.out.println("<========getAssetByQuery=======>3");
        	  ps.setString(1, status);
        	  ps.setString(2, branch);
        	  ps.setString(3, deptCode);
        	  ps.setString(4, FromDate);
        	  ps.setString(5, ToDate);
          }  
          if(queryFilter.contains("a.Asset_Status") && !queryFilter.contains("a.CATEGORY_ID") && queryFilter.contains("a.BRANCH_ID") && !queryFilter.contains("a.DEPT_ID") && queryFilter.contains("a.POSTING_DATE")){
        	 // System.out.println("<========getAssetByQuery=======>3A");
        	  ps.setString(1, status);
        	  ps.setString(2, branch);
        	  ps.setString(3, FromDate);
        	  ps.setString(4, ToDate);
          }  
          if(queryFilter.contains("a.Asset_Status") && queryFilter.contains("a.CATEGORY_ID") && queryFilter.contains("a.BRANCH_ID") && queryFilter.contains("a.DEPT_ID") && queryFilter.contains("a.POSTING_DATE")){
        	 // System.out.println("<========getAssetByQuery=======>3B");
        	  ps.setString(1, status);
        	  ps.setString(2, category);
        	  ps.setString(3, branch);
        	  ps.setString(4, deptCode);
        	  ps.setString(5, FromDate);
        	  ps.setString(6, ToDate);
          }   
          if(queryFilter.contains("a.Asset_Status") && !queryFilter.contains("a.CATEGORY_ID") && !queryFilter.contains("a.BRANCH_ID") && !queryFilter.contains("a.DEPT_ID") && queryFilter.contains("a.POSTING_DATE")){
        	 // System.out.println("<========getAssetByQuery=======>3C");
        	  ps.setString(1, status);
        	  ps.setString(2, FromDate);
        	  ps.setString(3, ToDate);
          }  

            rs = ps.executeQuery();

			while (rs.next()) {
				groupAsset = new GroupAssets();
				groupAsset.setGid(rs.getLong("GROUP_ID"));
				groupAsset.setAmountPTD(rs.getString("AMOUNT_PTD"));
				groupAsset.setAmountREM(rs.getString("AMOUNT_REM"));
				groupAsset.setCategory_id(rs.getString("Category_ID"));
				groupAsset.setCost_price(rs.getString("cost_price"));
				groupAsset.setDate_of_purchase(sdf.format(rs
						.getDate("Date_purchased")));
				groupAsset.setDepartment_id(rs.getString("Dept_ID"));
				groupAsset.setDescription(rs.getString("description"));
				groupAsset.setFullyPAID(rs.getString("FULLY_PAID"));
				groupAsset.setLocation(rs.getString("location"));
				groupAsset.setMake(rs.getString("asset_make"));
				groupAsset.setModel(rs.getString("asset_model"));
				groupAsset.setPartPAY(rs.getString("PART_PAY"));
				groupAsset.setPosting_date(sdf.format(rs
						.getDate("posting_date")));
				groupAsset.setProvince(rs.getString("province"));
				groupAsset.setQuantity(rs.getInt("quantity"));
				groupAsset.setBranch_id(rs.getString("Branch_ID"));
				groupAsset.setSection_id(rs.getString("section_id"));
				groupAsset.setState(rs.getString("state"));
				groupAsset.setVendor_account(rs.getString("Vendor_AC"));
				groupAsset.setVat_amount(rs.getString("Vat"));
				groupAsset.setWh_tax_amount(rs.getString("wh_tax_amount"));
				groupAsset.setVatable_cost(rs.getString("Vatable_Cost"));
				groupAsset.setRaise_entry(rs.getString("Raise_entry"));
				groupAsset.setUser_id(rs.getString("user_id"));
				finder.add(groupAsset);

			}

		} catch (Exception e) {
			System.out.println("INFO:Error Fetching GroupAsset Records ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps, rs);
		}
		return finder;
	}
	public boolean deleteUncapitalizedUploadGAsset(String assetId, String groupid) {

		String UPDATE_QUERY = "DELETE FROM AM_GROUP_ASSET_UNCAPITALIZED "
				+ "  WHERE ASSET_ID=? ";

		String UPDATE_QUERY2 = "UPDATE  AM_GROUP_ASSET_MAIN "
				+ " SET QUANTITY = QUANTITY-1, Cost_Price=(Cost_Price-Cost_Price/QUANTITY)," +
						"Vatable_Cost=(Vatable_Cost-Vatable_Cost/QUANTITY) WHERE GROUP_ID=? ";

		String UPDATE_QUERY3 = "DELETE FROM AM_ASSET_UNCAPITALIZED "
			+ "  WHERE ASSET_ID=? ";
		
		String UPDATE_QUERY4 = "DELETE FROM AM_INVOICE_NO WHERE ASSET_ID=?";
		
		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(UPDATE_QUERY);
			ps.setString(1, assetId);
			done = (ps.executeUpdate() != -1);

			ps = con.prepareStatement(UPDATE_QUERY2);
			ps.setString(1, groupid);
			done = (ps.executeUpdate() != -1);

			ps = con.prepareStatement(UPDATE_QUERY3);
			ps.setString(1, assetId);
			done = (ps.executeUpdate() != -1);
			
			ps = con.prepareStatement(UPDATE_QUERY4);
			ps.setString(1, groupid);
			done = (ps.executeUpdate() != -1);
			closeConnection(con, ps);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(con, ps);
		}
		return done;
	}
	public boolean deleteUploadGAssetImprove(String assetId, String groupid) {

		String UPDATE_QUERY = "DELETE FROM AM_GROUP_IMPROVEMENT "
				+ "  WHERE ASSET_ID=? AND REVALUE_ID=? ";
//		String DELETE_LOG_QUERY = "DELETE FROM am_uploadCheckErrorLog ";
		String UPDATE_QUERY4 = "DELETE FROM AM_INVOICE_NO WHERE ASSET_ID=?";
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		boolean done = false;

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(UPDATE_QUERY);
			ps.setString(1, assetId);
			ps.setString(2, groupid);
			done = (ps.executeUpdate() != -1);
			
//			ps1 = con.prepareStatement(DELETE_LOG_QUERY);
//			done = (ps1.executeUpdate() != -1);
			
			ps1 = con.prepareStatement(UPDATE_QUERY4);
			ps1.setString(1, groupid);
			done = (ps1.executeUpdate() != -1);			
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(con, ps);
//			closeConnection(con, ps1);
		}
		return done;
	}
	
	public boolean deleteUploadGAssetFleet(String groupid) {

		String UPDATE_QUERY = "DELETE FROM FT_GROUP_DUE_PERIOD "
				+ "  WHERE GROUP_ID=? ";
		String DELETE_LOG_QUERY = "DELETE FROM am_uploadCheckErrorLog ";
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		boolean done = false;
//		System.out.println("deleteUploadGAssetFleet UPDATE_QUERY: "+UPDATE_QUERY);
		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(UPDATE_QUERY);
	//		ps.setString(1, assetId);
			ps.setString(1, groupid);
			done = (ps.executeUpdate() != -1);
			
			ps1 = con.prepareStatement(DELETE_LOG_QUERY);
			done = (ps1.executeUpdate() != -1);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(con, ps);
			closeConnection(con, ps1);
		}
		return done;
	}

	public boolean deleteGAsset2(String assetId, String groupid) {

		String UPDATE_QUERY = "DELETE FROM AM_GROUP_ASSET2 "
				+ "  WHERE ASSET_ID=? ";

		String UPDATE_QUERY2 = "UPDATE  AM_GROUP_ASSET2_MAIN "
				+ " SET QUANTITY = QUANTITY-1, Cost_Price=(Cost_Price-Cost_Price/QUANTITY)," +
						"Vatable_Cost=(Vatable_Cost-Vatable_Cost/QUANTITY) WHERE GROUP_ID=? ";

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(UPDATE_QUERY);
			ps.setString(1, assetId);
			done = (ps.executeUpdate() != -1);

			ps = con.prepareStatement(UPDATE_QUERY2);
			ps.setString(1, groupid);
			done = (ps.executeUpdate() != -1);

		
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(con, ps);
		}
		return done;
	}

	public ArrayList<GroupAssets> findGroupMainAsset2ByQuery2(String queryFilter) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		GroupAssets groupAsset = null;
		ArrayList<GroupAssets> finder = new ArrayList<GroupAssets>();
		String selectQuery = "SELECT distinct (a.group_id), a.quantity, a.Registration_No, "
			+ "a.Branch_ID, a.Dept_ID, a.Category_ID, a.section_id, a.Description, "
			+ "a.Vendor_AC, a.Date_purchased, a.dep_rate, a.asset_make, a.asset_model, "
			+ "a.asset_serial_no, a.asset_engine_no, a.supplier_name, a.asset_user, "
			+ "a.asset_maintenance, a.Cost_Price, a.dep_end_date, a.residual_value, "
			+ "a.authorized_by, a.wh_tax, a.wh_tax_amount, a.req_redistribution, "
			+ "a.Posting_Date, a.effective_date, a.purchase_reason, a.location, "
			+ "a.Vatable_Cost, a.Vat, a.Req_Depreciation, a.Subject_TO_Vat, "
			+ "a.Who_TO_Rem, a.email1, a.who_to_rem_2, a.email2, a.Raise_entry, "
			+ "a.dep_ytd, a.Section, a.Asset_Status, a.state, a.driver, a.spare_1, "
			+ "a.spare_2, a.user_ID, a.province, a.WAR_START_DATE, a.WAR_MONTH, "
			+ "a.WAR_EXPIRY_DATE, a.branch_code, a.dept_code, a.section_code, "
			+ "a.category_code,a.AMOUNT_PTD,a.AMOUNT_REM,a.PART_PAY,a.FULLY_PAID "
			+ " FROM am_group_asset2_main a join AM_GROUP_ASSET2 b on a.group_id =b.group_id  " + queryFilter;
	//	System.out.println("-->selectQuery in findGroupMainAsset2ByQuery2 >--> "+selectQuery);
		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(selectQuery);
			rs = ps.executeQuery();

			while (rs.next()) {
				groupAsset = new GroupAssets();
				groupAsset.setGid(rs.getLong("GROUP_ID"));
				groupAsset.setAmountPTD(rs.getString("AMOUNT_PTD"));
				groupAsset.setAmountREM(rs.getString("AMOUNT_REM"));
				groupAsset.setCategory_id(rs.getString("Category_ID"));
				groupAsset.setCost_price(rs.getString("cost_price"));
				groupAsset.setDate_of_purchase(sdf.format(rs.getDate("Date_purchased")));
				groupAsset.setDepartment_id(rs.getString("Dept_ID"));
				groupAsset.setDescription(rs.getString("description"));
				groupAsset.setFullyPAID(rs.getString("FULLY_PAID"));
				groupAsset.setLocation(rs.getString("location"));
				groupAsset.setMake(rs.getString("asset_make"));
				groupAsset.setModel(rs.getString("asset_model"));
				groupAsset.setPartPAY(rs.getString("PART_PAY"));
				groupAsset.setPosting_date(sdf.format(rs.getDate("posting_date")));
				groupAsset.setProvince(rs.getString("province"));
				groupAsset.setQuantity(rs.getInt("quantity"));
				groupAsset.setBranch_id(rs.getString("Branch_ID"));
				groupAsset.setSection_id(rs.getString("section_id"));
				groupAsset.setState(rs.getString("state"));
				groupAsset.setVendor_account(rs.getString("Vendor_AC"));
				groupAsset.setVat_amount(rs.getString("Vat"));
				groupAsset.setWh_tax_amount(rs.getString("wh_tax_amount"));
				groupAsset.setVatable_cost(rs.getString("Vatable_Cost"));
				groupAsset.setRaise_entry(rs.getString("Raise_entry"));
				groupAsset.setUser_id(rs.getString("user_id"));
				finder.add(groupAsset);

			}

		} catch (Exception e) {
			System.out.println("INFO:Error Fetching GroupAsset2 Records in findGroupMainAsset2ByQuery2 ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps, rs);
		}
		return finder;
	}	
	
	public boolean deleteUploadGAsset2(String assetId, String groupid) {

		String UPDATE_QUERY = "DELETE FROM AM_GROUP_ASSET2 "
				+ "  WHERE ASSET_ID=? ";

		String UPDATE_QUERY2 = "UPDATE  AM_GROUP_ASSET2_MAIN "
				+ " SET QUANTITY = QUANTITY-1, Cost_Price=(Cost_Price-Cost_Price/QUANTITY)," +
						"Vatable_Cost=(Vatable_Cost-Vatable_Cost/QUANTITY) WHERE GROUP_ID=? ";

		String UPDATE_QUERY3 = "DELETE FROM AM_ASSET2 "
			+ "  WHERE ASSET_ID=? ";
		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(UPDATE_QUERY);
			ps.setString(1, assetId);
			done = (ps.executeUpdate() != -1);

			ps = con.prepareStatement(UPDATE_QUERY2);
			ps.setString(1, groupid);
			done = (ps.executeUpdate() != -1);

			ps = con.prepareStatement(UPDATE_QUERY3);
			ps.setString(1, assetId);
			done = (ps.executeUpdate() != -1);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(con, ps);
		}
		return done;
	}
	
	public boolean deleteUploadSBU() {

		String UPDATE_QUERY = "delete from Sbu_SetUp where sbu_code in (select sbu_code from Sbu_SetUp_Upload) ";

		String UPDATE_QUERY2 = "delete from Sbu_SetUp_Upload ";
		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(UPDATE_QUERY);
			done = (ps.executeUpdate() != -1);
			closeConnection(con, ps);
			con = getConnection("legendPlus");
			ps = con.prepareStatement(UPDATE_QUERY2);
			done = (ps.executeUpdate() != -1);

			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(con, ps);
		}
		return done;
	}
	
	public boolean deleteUserUpload() {

		String UPDATE_QUERY = "delete from am_gb_User where User_Name in (select USER_NAME from am_gb_User_Upload) ";

		String UPDATE_QUERY2 = "delete from am_gb_User_Upload ";
		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(UPDATE_QUERY);
			done = (ps.executeUpdate() != -1);
//			System.out.println("<<<<====UPDATE_QUERY in deleteUserUpload: "+UPDATE_QUERY+"   done: "+done);
			closeConnection(con, ps);
			con = getConnection("legendPlus");
			ps = con.prepareStatement(UPDATE_QUERY2);
			done = (ps.executeUpdate() != -1);
//			System.out.println("<<<<====UPDATE_QUERY in deleteUserUpload: "+UPDATE_QUERY2+"   done: "+done);
			closeConnection(con, ps);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(con, ps);
		}
		return done;
	}

	public ArrayList<AssetPayment> findPaymentsByStockID(String assetId) {
		ArrayList<AssetPayment> found = new ArrayList<AssetPayment>();
		String filter = " AND ASSET_ID ='" + assetId + "'";
		found = findStockPayments(filter);
		return found;
	}

	private ArrayList<AssetPayment> findStockPayments(String filter) {
		ArrayList<AssetPayment> found = new ArrayList<AssetPayment>();
		AssetPayment pay = null;
		String QUERY = "SELECT PAY_ID,ASSET_ID,PAYMENT,PAY_DATE,"
				+ "RAISED,PAY_DESC FROM AM_STOCK_PAYMENT WHERE TYPE='G' ";
		QUERY += filter;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		//System.out.print("<<<<<<<<QUERY in findStockPayments: "+QUERY);
		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(QUERY);
			rs = ps.executeQuery();
			while (rs.next()) {
				String assetId = rs.getString("ASSET_ID");
				double payment = rs.getDouble("PAYMENT");
				String payDate = sdf.format(rs.getDate("PAY_DATE"));
				String raised = rs.getString("RAISED");
				String payDesc = rs.getString("PAY_DESC");
				String payId = rs.getString("PAY_ID");

				pay = new AssetPayment(assetId, payment, payDate, raised,
						payDesc);
				pay.setPayId(payId);

				found.add(pay);
			}

		} catch (Exception ex) {

		} finally {
			closeConnection(con, ps);
		}

		return found;

	}


	public ArrayList<GroupAssets> findGroupMainStockByQuery(String queryFilter) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		GroupAssets groupAsset = null;
		ArrayList<GroupAssets> finder = new ArrayList<GroupAssets>();
		String selectQuery = "SELECT group_id, quantity, Registration_No, "
				+ "Branch_ID, Dept_ID, Category_ID, section_id, Description, "
				+ "Vendor_AC, Date_purchased, dep_rate, asset_make, asset_model, "
				+ "asset_serial_no, asset_engine_no, supplier_name, asset_user, "
				+ "asset_maintenance, Cost_Price, dep_end_date, residual_value, "
				+ "authorized_by, wh_tax, wh_tax_amount, req_redistribution, "
				+ "Posting_Date, effective_date, purchase_reason, location, "
				+ "Vatable_Cost, Vat, Req_Depreciation, Subject_TO_Vat, "
				+ "Who_TO_Rem, email1, who_to_rem_2, email2, Raise_entry, "
				+ "dep_ytd, Section, Asset_Status, state, driver, spare_1, "
				+ "spare_2, user_ID, province, WAR_START_DATE, WAR_MONTH, "
				+ "WAR_EXPIRY_DATE, branch_code, dept_code, section_code, "
				+ "category_code,AMOUNT_PTD,AMOUNT_REM,PART_PAY,FULLY_PAID "
				+ " FROM am_group_stock_main " + queryFilter;
		//System.out.println("<<<<selectQuery: "+selectQuery);
		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(selectQuery);
			rs = ps.executeQuery();

			while (rs.next()) {
				groupAsset = new GroupAssets();
				groupAsset.setGid(rs.getLong("GROUP_ID"));
				groupAsset.setAmountPTD(rs.getString("AMOUNT_PTD"));
				groupAsset.setAmountREM(rs.getString("AMOUNT_REM"));
				groupAsset.setCategory_id(rs.getString("Category_ID"));
				groupAsset.setCost_price(rs.getString("cost_price"));
				groupAsset.setDate_of_purchase(sdf.format(rs.getDate("Date_purchased")));
				groupAsset.setDepartment_id(rs.getString("Dept_ID"));
				groupAsset.setDescription(rs.getString("description"));
				groupAsset.setFullyPAID(rs.getString("FULLY_PAID"));
				groupAsset.setLocation(rs.getString("location"));
				groupAsset.setMake(rs.getString("asset_make"));
				groupAsset.setModel(rs.getString("asset_model"));
				groupAsset.setPartPAY(rs.getString("PART_PAY"));
				groupAsset.setPosting_date(sdf.format(rs.getDate("posting_date")));
				groupAsset.setProvince(rs.getString("province"));
				groupAsset.setQuantity(rs.getInt("quantity"));
				groupAsset.setBranch_id(rs.getString("Branch_ID"));
				groupAsset.setSection_id(rs.getString("section_id"));
				groupAsset.setState(rs.getString("state"));
				groupAsset.setVendor_account(rs.getString("Vendor_AC"));
				groupAsset.setVat_amount(rs.getString("Vat"));
				groupAsset.setWh_tax_amount(rs.getString("wh_tax_amount"));
				groupAsset.setVatable_cost(rs.getString("Vatable_Cost"));
				groupAsset.setRaise_entry(rs.getString("Raise_entry"));
				groupAsset.setUser_id(rs.getString("user_id"));
				finder.add(groupAsset);

			}

		} catch (Exception e) {
			System.out.println("INFO:Error Fetching GroupStock Records in findGroupMainStockByQuery ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps, rs);
		}
		return finder;
	}

	public boolean createStockPaymentRecord(String assetId, double pay,
			String payDate, String raised, String payDesc) {

		String INSERT_QUERY = "INSERT INTO AM_STOCK_PAYMENT "
				+ "(ASSET_ID,PAYMENT,PAY_DATE,RAISED,PAY_DESC,TYPE) "
				+ "VALUES(?,?,?,?,?,?)";

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
			ps.setString(6, "G");
			done = (ps.executeUpdate() != -1);
			notifyStock(assetId, pay);
			notifyStockGroup(assetId, pay);

		} catch (Exception ex) {

		} finally {
			closeConnection(con, ps);
		}
		return done;
	}

	public boolean isStockFullyPaid(String assetId) {

		String UPDATE_QUERY = "SELECT DISTINCT AMOUNT_REM FROM ST_STOCK "
				+ " WHERE GROUP_ID=? ";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean done = false;

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(UPDATE_QUERY);
			ps.setString(1, assetId);
			while (rs.next()) {
				rs = ps.executeQuery();
				done = (rs.getDouble("AMOUNT_REM") == 0.);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(con, ps);
		}
		return done;
	}
	

	public boolean notifyStock(String assetId, double pay) {

		String UPDATE_QUERY = "UPDATE ST_STOCK "
				+ " SET AMOUNT_PTD=AMOUNT_PTD+?,AMOUNT_REM=AMOUNT_REM-?, "
				+ "FULLY_PAID=? WHERE GROUP_ID=? ";

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(UPDATE_QUERY);
			String paid = "N";
			if (isStockFullyPaid(assetId)) {
				paid = "Y";
			}

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

	public boolean notifyStockGroup(String assetId, double pay) {

		String UPDATE_QUERY = "UPDATE AM_GROUP_STOCK_MAIN  "
				+ " SET AMOUNT_PTD=AMOUNT_PTD+?,AMOUNT_REM=AMOUNT_REM-?, "
				+ "FULLY_PAID=? WHERE GROUP_ID=? ";

		Connection con = null;
		PreparedStatement ps = null; 
		boolean done = false;

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(UPDATE_QUERY);
			String paid = "N";
			if (isStockFullyPaid(assetId)) {
				paid = "Y";
			}

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

	public boolean createStockTempPaymentRecord(String assetId, double pay,
			String payDate, String raised, String payDesc) {

		String INSERT_QUERY = "INSERT INTO AM_STOCK_PAYMENT_TEMP "
				+ "(ASSET_ID,PAYMENT,PAY_DATE,RAISED,PAY_DESC,TYPE) "
				+ "VALUES(?,?,?,?,?,?)";

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
			ps.setString(6, "G");
			done = (ps.executeUpdate() != -1);
			notifyStock(assetId, pay);
			notifyStockGroup(assetId, pay);

		} catch (Exception ex) {

		} finally {
			closeConnection(con, ps);
		}
		return done;
	}

	public boolean deleteUploadGAssetBid(String assetId, String groupid) {

		String UPDATE_QUERY = "DELETE FROM AM_GROUP_ASSET_BID "
				+ "  WHERE ASSET_ID=? ";

		String UPDATE_QUERY3 = "DELETE FROM AM_ASSET_BID "
			+ "  WHERE ASSET_ID=? ";
		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(UPDATE_QUERY);
			ps.setString(1, assetId);
			done = (ps.executeUpdate() != -1);

			ps = con.prepareStatement(UPDATE_QUERY3);
			ps.setString(1, assetId);
			done = (ps.executeUpdate() != -1);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(con, ps);
		}
		return done;
	}
	public boolean deleteUploadGAssetDisposal(String assetId, String groupid) {

		String UPDATE_QUERY = "DELETE FROM AM_GROUP_DISPOSAL "
				+ "  WHERE ASSET_ID=? AND DISPOSAL_ID=? ";
		String DELETE_LOG_QUERY = "DELETE FROM am_uploadCheckErrorLog ";
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		boolean done = false;

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(UPDATE_QUERY);
			ps.setString(1, assetId);
			ps.setString(2, groupid);
			done = (ps.executeUpdate() != -1);
			
			ps1 = con.prepareStatement(DELETE_LOG_QUERY);
			done = (ps1.executeUpdate() != -1);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(con, ps);
			closeConnection(con, ps1);
		}
		return done;
	}
	
	public boolean deleteUploadAssetListVrify(String assetId, String groupid) {

		String UPDATE_QUERY = "DELETE FROM AM_GROUP_ASSET_VERIFICATION "
				+ "  WHERE ASSET_ID!='' AND GROUP_ID=? ";
		String DELETE_LOG_QUERY = "DELETE FROM am_uploadCheckErrorLog WHERE TRANSACTION_TYPE = 'Asset Verification List' ";
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		boolean done = false;

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(UPDATE_QUERY);
//			ps.setString(1, assetId);
			ps.setString(1, groupid);
			done = (ps.executeUpdate() != -1);
			
			ps1 = con.prepareStatement(DELETE_LOG_QUERY);
			done = (ps1.executeUpdate() != -1);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(con, ps);
			closeConnection(con, ps1);
		}
		return done;
	}
	

	
}
